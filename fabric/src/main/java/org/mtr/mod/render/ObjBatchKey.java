package org.mtr.mod.render;

import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.mapper.OptimizedModel;
import org.mtr.mapping.render.batch.MaterialProperties;
import org.mtr.mod.resource.RenderStage;

import java.util.Objects;

public final class ObjBatchKey {

	public final Identifier texture;
	public final RenderStage renderStage;
	public final OptimizedModel.ShaderType shaderType;
	public final boolean translucent;
	private final MaterialProperties materialProperties;

	public ObjBatchKey(RenderStage renderStage, MaterialProperties materialProperties) {
		this.texture = materialProperties.getTexture();
		this.renderStage = renderStage;
		this.shaderType = materialProperties.shaderType;
		this.translucent = materialProperties.translucent;
		this.materialProperties = materialProperties;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof ObjBatchKey)) {
			return false;
		}
		final ObjBatchKey that = (ObjBatchKey) object;
		return renderStage == that.renderStage && Objects.equals(materialProperties, that.materialProperties);
	}

	@Override
	public int hashCode() {
		return Objects.hash(renderStage, materialProperties);
	}
}
