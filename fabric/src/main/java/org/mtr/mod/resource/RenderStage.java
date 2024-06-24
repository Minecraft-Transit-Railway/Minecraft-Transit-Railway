package org.mtr.mod.resource;

import org.mtr.mapping.mapper.OptimizedModel;

public enum RenderStage {
	LIGHT(OptimizedModel.ShaderType.CUTOUT_GLOWING),
	ALWAYS_ON_LIGHT(OptimizedModel.ShaderType.TRANSLUCENT_GLOWING),
	INTERIOR(OptimizedModel.ShaderType.CUTOUT_BRIGHT),
	INTERIOR_TRANSLUCENT(OptimizedModel.ShaderType.TRANSLUCENT_BRIGHT),
	EXTERIOR(OptimizedModel.ShaderType.CUTOUT);

	public final OptimizedModel.ShaderType shaderType;

	RenderStage(OptimizedModel.ShaderType shaderType) {
		this.shaderType = shaderType;
	}
}
