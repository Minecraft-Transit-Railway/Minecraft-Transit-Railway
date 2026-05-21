package org.mtr.mod.render;

import org.joml.Matrix4f;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mapping.mapper.OptimizedModel;
import org.mtr.mapping.render.batch.MaterialProperties;
import org.mtr.mapping.render.model.RawMesh;
import org.mtr.mod.Init;
import org.mtr.mod.api.instancing.InstancedMeshSource;
import org.mtr.mod.api.instancing.InstancedModelHandle;
import org.mtr.mod.api.instancing.InstancedModelOptions;
import org.mtr.mod.api.instancing.InstancedModelSource;
import org.mtr.mod.api.instancing.InstancedRenderStage;
import org.mtr.mod.api.instancing.InstancingFallbackReason;
import org.mtr.mod.api.instancing.InstancingUploadMode;
import org.mtr.mod.client.CustomResourceLoader;
import org.mtr.mod.config.Config;
import org.mtr.mod.resource.RenderStage;

import javax.annotation.Nullable;

final class InstancedModelHandleImpl implements InstancedModelHandle {

	private final ObjectArrayList<Entry> entries = new ObjectArrayList<>();
	private boolean closed;
	private boolean rendererReloaded;
	private InstancingFallbackReason fallbackReason;

	private InstancedModelHandleImpl(InstancingFallbackReason fallbackReason) {
		this.fallbackReason = fallbackReason;
	}

	static InstancedModelHandle upload(@Nullable InstancedModelSource source, InstancedModelOptions options) {
		final InstancingFallbackReason preflightReason = validateSource(source, options);
		if (preflightReason != InstancingFallbackReason.NONE) {
			return new InstancedModelHandleImpl(preflightReason);
		}

		final InstancedModelHandleImpl handle = new InstancedModelHandleImpl(InstancingFallbackReason.NONE);
		try {
			CustomResourceLoader.OPTIMIZED_RENDERER_WRAPPER.runWithProtectedState(() -> {
				for (final InstancedMeshSource meshSource : source.getMeshes()) {
					handle.addMesh(meshSource);
				}
			});
			ExternalInstancedModelRegistry.register(handle);
			return handle;
		} catch (Exception e) {
			handle.close();
			Init.LOGGER.warn("[MTR Instancing API] Failed to upload instanced model '{}'", options.debugName == null ? "" : options.debugName, e);
			return new InstancedModelHandleImpl(InstancingFallbackReason.UPLOAD_FAILED);
		}
	}

	private static InstancingFallbackReason validateSource(@Nullable InstancedModelSource source, InstancedModelOptions options) {
		if (options.uploadMode == InstancingUploadMode.FORCE_FALLBACK) {
			return InstancingFallbackReason.FORCED_FALLBACK;
		}
		if (!GpuObjCompat.isSupported() || !Config.getClient().getEnableGpuObjInstancing()) {
			return InstancingFallbackReason.DISABLED;
		}
		if (!options.immutableGeometry) {
			return InstancingFallbackReason.MUTABLE_GEOMETRY_NOT_SUPPORTED;
		}
		if (source == null) {
			return InstancingFallbackReason.INVALID_SOURCE;
		}
		if (source.isEmpty()) {
			return InstancingFallbackReason.EMPTY_MODEL;
		}
		for (final InstancedMeshSource meshSource : source.getMeshes()) {
			final InstancingFallbackReason reason = validateMesh(meshSource);
			if (reason != InstancingFallbackReason.NONE) {
				return reason;
			}
		}
		return InstancingFallbackReason.NONE;
	}

	private static InstancingFallbackReason validateMesh(@Nullable InstancedMeshSource meshSource) {
		if (meshSource == null || meshSource.rawMesh == null || meshSource.stage == null || meshSource.rawMesh.materialProperties == null) {
			return InstancingFallbackReason.INVALID_SOURCE;
		}
		if (!isSupportedStage(meshSource.stage)) {
			return meshSource.stage == InstancedRenderStage.INTERIOR_TRANSLUCENT ? InstancingFallbackReason.TRANSLUCENT_NOT_SUPPORTED : InstancingFallbackReason.UNSUPPORTED_RENDER_STAGE;
		}
		if (meshSource.rawMesh.materialProperties.translucent) {
			return InstancingFallbackReason.TRANSLUCENT_NOT_SUPPORTED;
		}
		if (meshSource.rawMesh.materialProperties.shaderType != OptimizedModel.ShaderType.CUTOUT) {
			return InstancingFallbackReason.UNSUPPORTED_SHADER;
		}
		if (meshSource.rawMesh.vertices.isEmpty()) {
			return InstancingFallbackReason.EMPTY_MODEL;
		}
		return InstancingFallbackReason.NONE;
	}

	private void addMesh(InstancedMeshSource meshSource) {
		final RenderStage renderStage = toInternalStage(meshSource.stage);
		final RawMesh rawMesh = meshSource.rawMesh;
		final StaticObjMesh staticObjMesh = new StaticObjMesh(rawMesh);
		final MaterialProperties materialProperties = new MaterialProperties(renderStage.shaderType, staticObjMesh.texture, null);
		entries.add(new Entry(staticObjMesh, new ObjBatchKey(renderStage, materialProperties), materialProperties));
	}

	@Override
	public boolean isValid() {
		return !closed && !rendererReloaded && fallbackReason == InstancingFallbackReason.NONE && !entries.isEmpty();
	}

	@Override
	public boolean queue(StoredMatrixTransformations transformations, int color, int light) {
		if (!isValidForQueue()) {
			return false;
		}
		if (transformations == null) {
			fallbackReason = InstancingFallbackReason.INVALID_SOURCE;
			return false;
		}
		if (!GpuObjRenderer.INSTANCE.isFrameActive()) {
			fallbackReason = InstancingFallbackReason.NOT_IN_RENDER_FRAME;
			return false;
		}
		final Matrix4f drawMatrix = GpuObjRenderer.INSTANCE.captureFrameMatrix(transformations, GpuObjRenderer.INSTANCE.getFrameOffset());
		return queueDrawMatrix(drawMatrix, color, light);
	}

	@Override
	public boolean queueDrawMatrix(Matrix4f drawMatrix, int color, int light) {
		if (!isValidForQueue()) {
			return false;
		}
		if (drawMatrix == null) {
			fallbackReason = InstancingFallbackReason.INVALID_SOURCE;
			return false;
		}
		if (!GpuObjRenderer.INSTANCE.isFrameActive()) {
			fallbackReason = InstancingFallbackReason.NOT_IN_RENDER_FRAME;
			return false;
		}

		final int packedLight = org.mtr.mapping.render.tool.Utilities.exchangeLightmapUVBits(light);
		for (final Entry entry : entries) {
			GpuObjRenderer.INSTANCE.queue(entry.batchKey, entry.materialProperties, entry.staticObjMesh, drawMatrix, null, packedLight, color, true, GpuObjDebugStats.Source.VEHICLE);
		}
		fallbackReason = InstancingFallbackReason.NONE;
		return true;
	}

	@Override
	public InstancingFallbackReason getFallbackReason() {
		return fallbackReason;
	}

	@Override
	public void close() {
		if (closed) {
			return;
		}
		closed = true;
		for (final Entry entry : entries) {
			entry.staticObjMesh.close();
		}
		entries.clear();
		if (!rendererReloaded) {
			fallbackReason = InstancingFallbackReason.CLOSED;
		}
		ExternalInstancedModelRegistry.unregister(this);
	}

	void markRendererReloaded() {
		if (rendererReloaded) {
			return;
		}
		rendererReloaded = true;
		for (final Entry entry : entries) {
			entry.staticObjMesh.close();
		}
		entries.clear();
		fallbackReason = InstancingFallbackReason.RENDERER_RELOADED;
	}

	private boolean isValidForQueue() {
		if (closed) {
			fallbackReason = InstancingFallbackReason.CLOSED;
			return false;
		}
		if (rendererReloaded) {
			fallbackReason = InstancingFallbackReason.RENDERER_RELOADED;
			return false;
		}
		if (!GpuObjCompat.isSupported() || !Config.getClient().getEnableGpuObjInstancing()) {
			fallbackReason = InstancingFallbackReason.DISABLED;
			return false;
		}
		if (entries.isEmpty()) {
			fallbackReason = InstancingFallbackReason.EMPTY_MODEL;
			return false;
		}
		return true;
	}

	private static boolean isSupportedStage(InstancedRenderStage stage) {
		return stage == InstancedRenderStage.EXTERIOR || stage == InstancedRenderStage.INTERIOR;
	}

	private static RenderStage toInternalStage(InstancedRenderStage stage) {
		return stage == InstancedRenderStage.INTERIOR ? RenderStage.INTERIOR : RenderStage.EXTERIOR;
	}

	private static final class Entry {

		private final StaticObjMesh staticObjMesh;
		private final ObjBatchKey batchKey;
		private final MaterialProperties materialProperties;

		private Entry(StaticObjMesh staticObjMesh, ObjBatchKey batchKey, MaterialProperties materialProperties) {
			this.staticObjMesh = staticObjMesh;
			this.batchKey = batchKey;
			this.materialProperties = materialProperties;
		}
	}
}
