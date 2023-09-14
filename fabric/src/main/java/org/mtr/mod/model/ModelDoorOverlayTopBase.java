package org.mtr.mod.model;

import org.mtr.mapping.holder.EntityAbstractMapping;
import org.mtr.mapping.mapper.EntityModelExtension;
import org.mtr.mapping.mapper.GraphicsHolder;

public abstract class ModelDoorOverlayTopBase extends EntityModelExtension<EntityAbstractMapping> {

	public ModelDoorOverlayTopBase(int textureWidth, int textureHeight) {
		super(textureWidth, textureHeight);
	}

	@Override
	public void setAngles2(EntityAbstractMapping entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
	}

	@Override
	public final void render(GraphicsHolder graphicsHolder, int light, int overlay, float red, float green, float blue, float alpha) {
	}

	public abstract void renderNew(GraphicsHolder graphicsHolder, int light, int position, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ);
}
