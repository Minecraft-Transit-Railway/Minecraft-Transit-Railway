package org.mtr.mod.resource;

import org.joml.Matrix4f;
import org.mtr.core.serializer.ReaderBase;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectSet;
import org.mtr.mapping.mapper.OptimizedRenderer;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mapping.render.batch.MaterialProperties;
import org.mtr.mod.Init;
import org.mtr.mod.config.Config;
import org.mtr.mod.generated.resource.RailResourceSchema;
import org.mtr.mod.render.DynamicVehicleModel;
import org.mtr.mod.render.GpuObjDebugStats;
import org.mtr.mod.render.GpuObjRenderer;
import org.mtr.mod.render.InstancingMatrixHelper;
import org.mtr.mod.render.ObjBatchKey;
import org.mtr.mod.render.StaticObjMesh;
import org.mtr.mod.render.StoredMatrixTransformations;

import javax.annotation.Nullable;

public final class RailResource extends RailResourceSchema implements StoredModelResourceBase {

	private static final ObjectSet<String> LOGGED_EMPTY_GPU_CACHE_MODELS = new ObjectOpenHashSet<>();
	public final boolean shouldPreload;
	private final CachedResource<ObjectObjectImmutablePair<OptimizedModelWrapper, DynamicVehicleModel>> cachedRailResource;
	private final CachedResource<RailGpuCache> cachedGpuRailResource;
	private final ResourceProvider resourceProvider;

	public RailResource(ReaderBase readerBase, ResourceProvider resourceProvider) {
		super(readerBase, resourceProvider);
		updateData(readerBase);
		shouldPreload = Config.getClient().matchesPreloadResourcePattern(id);
		this.resourceProvider = resourceProvider;
		cachedRailResource = new CachedResource<>(() -> load(modelResource, textureResource, flipTextureV, 0, resourceProvider), shouldPreload ? Integer.MAX_VALUE : VehicleModel.MODEL_LIFESPAN);
		cachedGpuRailResource = new CachedResource<>(this::createGpuCache, shouldPreload ? Integer.MAX_VALUE : VehicleModel.MODEL_LIFESPAN);
	}

	/**
	 * Used to create the default rail
	 */
	public RailResource(String id, String name, ResourceProvider resourceProvider) {
		super(id, name, "777777", "", "", false, 0, 0, resourceProvider);
		shouldPreload = false;
		this.resourceProvider = resourceProvider;
		cachedRailResource = new CachedResource<>(() -> null, Integer.MAX_VALUE);
		cachedGpuRailResource = new CachedResource<>(() -> new RailGpuCache(new ObjectArrayList<>(), GpuObjDebugStats.RailFallbackReason.STYLE_NOT_OBJ), Integer.MAX_VALUE);
	}

	@Override
	@Nullable
	public OptimizedModelWrapper getOptimizedModel() {
		final ObjectObjectImmutablePair<OptimizedModelWrapper, DynamicVehicleModel> railResource = cachedRailResource.getData(false);
		return railResource == null ? null : railResource.left();
	}

	@Override
	@Nullable
	public DynamicVehicleModel getDynamicVehicleModel() {
		final ObjectObjectImmutablePair<OptimizedModelWrapper, DynamicVehicleModel> railResource = cachedRailResource.getData(false);
		return railResource == null ? null : railResource.right();
	}

	@Override
	public void preload() {
		cachedRailResource.getData(true);
		cachedGpuRailResource.getData(true);
	}

	public boolean queueGpu(double x, double y, double z, double yaw, double pitch, boolean flip, float rollDegrees, int light, boolean useDefaultOffset) {
		final long startNanos = System.nanoTime();
		try {
			if (!Config.getClient().getEnableGpuObjInstancing()) {
				GpuObjDebugStats.recordRailOutcome(false, GpuObjDebugStats.RailFallbackReason.CONFIG_DISABLED);
				return false;
			}

			if (!OptimizedRenderer.hasOptimizedRendering()) {
				GpuObjDebugStats.recordRailOutcome(false, GpuObjDebugStats.RailFallbackReason.OPTIMIZED_RENDERING_UNAVAILABLE);
				return false;
			}

			final RailGpuCache railGpuCache = cachedGpuRailResource.getData(false);
			if (railGpuCache == null) {
				GpuObjDebugStats.recordRailOutcome(false, GpuObjDebugStats.RailFallbackReason.GPU_CACHE_UNAVAILABLE);
				return false;
			}

			if (!railGpuCache.hasEntries()) {
				GpuObjDebugStats.recordRailOutcome(false, railGpuCache.fallbackReason);
				return false;
			}

			final int packedLight = org.mtr.mapping.render.tool.Utilities.exchangeLightmapUVBits(light);
			final StoredMatrixTransformations storedMatrixTransformations = createStoredMatrixTransformations(x, y, z, yaw, pitch, flip, rollDegrees, useDefaultOffset);
			final boolean diagnosticsEnabled = GpuObjDebugStats.isDiagnosticEnabled();
			final Matrix4f diagnosticMatrix = diagnosticsEnabled ? GpuObjRenderer.INSTANCE.captureFrameMatrix(storedMatrixTransformations, InstancingMatrixHelper.ZERO_OFFSET) : null;
			final org.mtr.mapping.holder.Vector3d drawOffset = GpuObjDebugStats.shouldSkipCameraOffset() ? InstancingMatrixHelper.ZERO_OFFSET : GpuObjRenderer.INSTANCE.getFrameOffset();
			final Matrix4f drawMatrix = GpuObjRenderer.INSTANCE.captureFrameMatrix(storedMatrixTransformations, drawOffset);
			final OptimizedModelWrapper normalReferenceModel = diagnosticsEnabled ? getOptimizedModel() : null;
			boolean queuedAny = false;
			for (final RailGpuCache.Entry entry : railGpuCache.entries) {
				queuedAny = true;
				final GpuObjDebugStats.DiagnosticSample diagnosticSample = GpuObjRenderer.INSTANCE.queue(entry.batchKey, entry.materialProperties, entry.mesh, drawMatrix, diagnosticMatrix, packedLight, 0xFFFFFFFF, useDefaultOffset, GpuObjDebugStats.Source.RAIL);
				if (diagnosticSample != null) {
					diagnosticSample.setCaptureOffset(drawOffset);
					diagnosticSample.setLight(light, packedLight);
					diagnosticSample.setNormalReference(normalReferenceModel, storedMatrixTransformations.copy(), diagnosticMatrix, true, "rail=" + id);
				}
			}
			GpuObjDebugStats.recordRailOutcome(queuedAny, GpuObjDebugStats.RailFallbackReason.QUEUE_RETURNED_FALSE_AFTER_CACHE_LOOKUP);
			return queuedAny;
		} finally {
			GpuObjDebugStats.recordRailQueueNanos(System.nanoTime() - startNanos);
		}
	}

	public static StoredMatrixTransformations createStoredMatrixTransformations(double x, double y, double z, double yaw, double pitch, boolean flip, float rollDegrees, boolean useDefaultOffset) {
		final StoredMatrixTransformations storedMatrixTransformations = useDefaultOffset ? new StoredMatrixTransformations(x, y, z) : new StoredMatrixTransformations();
		if (!useDefaultOffset) {
			storedMatrixTransformations.add(graphicsHolder -> graphicsHolder.translate(x, y, z));
		}
		storedMatrixTransformations.add(graphicsHolder -> {
			graphicsHolder.rotateYRadians((float) (Math.PI / 2 - yaw + (flip ? Math.PI : 0)));
			graphicsHolder.rotateXRadians((float) (Math.PI - pitch * (flip ? -1 : 1)));
			graphicsHolder.rotateZDegrees(rollDegrees);
		});
		return storedMatrixTransformations;
	}

	@Nullable
	private GpuObjModelWrapper getGpuObjModel() {
		return modelResource.endsWith(".obj") ? GpuObjModelRegistry.getOrCreate(modelResource, CustomResourceTools.formatIdentifierWithDefault(textureResource, "png"), flipTextureV, resourceProvider) : null;
	}

	private RailGpuCache createGpuCache() {
		if (!modelResource.endsWith(".obj")) {
			return new RailGpuCache(new ObjectArrayList<>(), GpuObjDebugStats.RailFallbackReason.STYLE_NOT_OBJ);
		}

		final GpuObjModelWrapper gpuObjModelWrapper = getGpuObjModel();
		if (gpuObjModelWrapper == null) {
			return new RailGpuCache(new ObjectArrayList<>(), GpuObjDebugStats.RailFallbackReason.GPU_CACHE_UNAVAILABLE);
		}

		if (gpuObjModelWrapper.hasTranslucentMeshes()) {
			return new RailGpuCache(new ObjectArrayList<>(), GpuObjDebugStats.RailFallbackReason.HAS_TRANSLUCENT_MESH);
		}

		final ObjectArrayList<RailGpuCache.Entry> entries = new ObjectArrayList<>();
		for (final StaticObjMesh staticObjMesh : gpuObjModelWrapper.getAllMeshes()) {
			final ObjBatchKey batchKey = new ObjBatchKey(staticObjMesh.texture, RenderStage.EXTERIOR, RenderStage.EXTERIOR.shaderType, false);
			final MaterialProperties materialProperties = new MaterialProperties(RenderStage.EXTERIOR.shaderType, staticObjMesh.texture, null);
			entries.add(new RailGpuCache.Entry(staticObjMesh, batchKey, materialProperties));
		}
		if (entries.isEmpty()) {
			logEmptyGpuCache(gpuObjModelWrapper);
			return new RailGpuCache(new ObjectArrayList<>(), GpuObjDebugStats.RailFallbackReason.GPU_CACHE_EMPTY);
		}
		return new RailGpuCache(entries, null);
	}

	private void logEmptyGpuCache(GpuObjModelWrapper gpuObjModelWrapper) {
		if (LOGGED_EMPTY_GPU_CACHE_MODELS.add(modelResource)) {
			Init.LOGGER.warn("[{}] GPU rail cache is empty; wrapper groups={}, meshes={}, translucent={}, sampleGroups={}", modelResource, gpuObjModelWrapper.getGroupCount(), gpuObjModelWrapper.getMeshCount(), gpuObjModelWrapper.hasTranslucentMeshes(), sampleGroups(gpuObjModelWrapper.getGroupNames()));
		}
	}

	private static String sampleGroups(Iterable<String> groupNames) {
		final StringBuilder stringBuilder = new StringBuilder();
		int count = 0;
		for (final String groupName : groupNames) {
			if (count >= 8) {
				stringBuilder.append(", ...");
				break;
			}
			if (count > 0) {
				stringBuilder.append(", ");
			}
			stringBuilder.append(groupName);
			count++;
		}
		return count == 0 ? "<none>" : stringBuilder.toString();
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return TextHelper.translatable(name).getString();
	}

	public int getColor() {
		return CustomResourceTools.colorStringToInt(color);
	}

	public double getRepeatInterval() {
		return repeatInterval;
	}

	public double getModelYOffset() {
		return modelYOffset;
	}

	public static String getIdWithoutDirection(String id) {
		return id.endsWith("_1") || id.endsWith("_2") ? id.substring(0, id.length() - 2) : id;
	}
}
