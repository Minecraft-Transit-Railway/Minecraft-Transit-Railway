package org.mtr.mod.resource;

import org.joml.Matrix4f;
import org.mtr.core.serializer.ReaderBase;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import org.mtr.mapping.mapper.OptimizedRenderer;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mapping.render.batch.MaterialProperties;
import org.mtr.mod.config.Config;
import org.mtr.mod.generated.resource.RailResourceSchema;
import org.mtr.mod.render.DynamicVehicleModel;
import org.mtr.mod.render.GpuObjDebugStats;
import org.mtr.mod.render.GpuObjRenderer;
import org.mtr.mod.render.ObjBatchKey;
import org.mtr.mod.render.StaticObjMesh;

import javax.annotation.Nullable;

public final class RailResource extends RailResourceSchema implements StoredModelResourceBase {

	public final boolean shouldPreload;
	private final CachedResource<ObjectObjectImmutablePair<OptimizedModelWrapper, DynamicVehicleModel>> cachedRailResource;
	private final CachedResource<RailGpuCache> cachedGpuRailResource;
	private final ResourceProvider resourceProvider;
	private final Matrix4f gpuMatrix = new Matrix4f();

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
		cachedGpuRailResource = new CachedResource<>(() -> RailGpuCache.EMPTY, Integer.MAX_VALUE);
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
	}

	public boolean queueGpu(double x, double y, double z, double yaw, double pitch, boolean flip, float rollDegrees, int light, boolean useDefaultOffset) {
		if (!OptimizedRenderer.hasOptimizedRendering() || !Config.getClient().getEnableGpuObjInstancing()) {
			GpuObjDebugStats.recordRailQueueResult(false);
			return false;
		}

		final RailGpuCache railGpuCache = cachedGpuRailResource.getData(false);
		if (railGpuCache == null || !railGpuCache.hasEntries()) {
			GpuObjDebugStats.recordRailQueueResult(false);
			return false;
		}

		final int packedLight = org.mtr.mapping.render.tool.Utilities.exchangeLightmapUVBits(light);
		final Matrix4f matrix = gpuMatrix.identity()
				.translate((float) x, (float) y, (float) z)
				.rotateY((float) (Math.PI / 2 - yaw + (flip ? Math.PI : 0)))
				.rotateX((float) (Math.PI - pitch * (flip ? -1 : 1)))
				.rotateZ((float) Math.toRadians(rollDegrees));
		boolean queuedAny = false;
		for (final RailGpuCache.Entry entry : railGpuCache.entries) {
			queuedAny = true;
			GpuObjRenderer.INSTANCE.queue(entry.batchKey, entry.materialProperties, entry.mesh, matrix, packedLight, 0xFFFFFFFF, useDefaultOffset, GpuObjDebugStats.Source.RAIL);
		}
		GpuObjDebugStats.recordRailQueueResult(queuedAny);
		return queuedAny;
	}

	@Nullable
	private GpuObjModelWrapper getGpuObjModel() {
		return modelResource.endsWith(".obj") ? GpuObjModelRegistry.getOrCreate(modelResource, CustomResourceTools.formatIdentifierWithDefault(textureResource, "png"), flipTextureV, resourceProvider) : null;
	}

	private RailGpuCache createGpuCache() {
		final GpuObjModelWrapper gpuObjModelWrapper = getGpuObjModel();
		if (gpuObjModelWrapper == null || gpuObjModelWrapper.hasTranslucentMeshes()) {
			return RailGpuCache.EMPTY;
		}

		final ObjectArrayList<RailGpuCache.Entry> entries = new ObjectArrayList<>();
		for (final StaticObjMesh staticObjMesh : gpuObjModelWrapper.getAllMeshes()) {
			final ObjBatchKey batchKey = new ObjBatchKey(staticObjMesh.texture, RenderStage.EXTERIOR, RenderStage.EXTERIOR.shaderType, false);
			final MaterialProperties materialProperties = new MaterialProperties(RenderStage.EXTERIOR.shaderType, staticObjMesh.texture, null);
			entries.add(new RailGpuCache.Entry(staticObjMesh, batchKey, materialProperties));
		}
		return entries.isEmpty() ? RailGpuCache.EMPTY : new RailGpuCache(entries);
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
