package org.mtr.mod.render;

import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.mapper.OptimizedModel;
import org.mtr.mod.resource.RenderStage;

import java.util.Objects;

public final class ObjBatchKey {

	public final Identifier texture;
	public final RenderStage renderStage;
	public final OptimizedModel.ShaderType shaderType;
	public final boolean translucent;

	public ObjBatchKey(Identifier texture, RenderStage renderStage, OptimizedModel.ShaderType shaderType, boolean translucent) {
		this.texture = texture;
		this.renderStage = renderStage;
		this.shaderType = shaderType;
		this.translucent = translucent;
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
		return translucent == that.translucent && Objects.equals(texture, that.texture) && renderStage == that.renderStage && shaderType == that.shaderType;
	}

	@Override
	public int hashCode() {
		return Objects.hash(texture, renderStage, shaderType, translucent);
	}
}
