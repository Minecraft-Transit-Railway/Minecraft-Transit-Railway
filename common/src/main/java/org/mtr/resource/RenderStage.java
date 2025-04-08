package org.mtr.resource;

import org.mtr.model.OptimizedModel;
import org.mtr.render.QueuedRenderLayer;

public enum RenderStage {
	LIGHT(OptimizedModel.ShaderType.CUTOUT_GLOWING, QueuedRenderLayer.LIGHT),
	ALWAYS_ON_LIGHT(OptimizedModel.ShaderType.TRANSLUCENT_GLOWING, QueuedRenderLayer.LIGHT_TRANSLUCENT),
	INTERIOR(OptimizedModel.ShaderType.CUTOUT_BRIGHT, QueuedRenderLayer.INTERIOR),
	INTERIOR_TRANSLUCENT(OptimizedModel.ShaderType.TRANSLUCENT_BRIGHT, QueuedRenderLayer.INTERIOR_TRANSLUCENT),
	EXTERIOR(OptimizedModel.ShaderType.CUTOUT, QueuedRenderLayer.EXTERIOR);

	public final OptimizedModel.ShaderType shaderType;
	public final QueuedRenderLayer queuedRenderLayer;

	RenderStage(OptimizedModel.ShaderType shaderType, QueuedRenderLayer queuedRenderLayer) {
		this.shaderType = shaderType;
		this.queuedRenderLayer = queuedRenderLayer;
	}
}
