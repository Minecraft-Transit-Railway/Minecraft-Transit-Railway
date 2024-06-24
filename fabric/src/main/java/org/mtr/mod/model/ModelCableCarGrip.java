package org.mtr.mod.model;

import org.mtr.mapping.holder.EntityAbstractMapping;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.mapper.EntityModelExtension;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.ModelPartExtension;
import org.mtr.mod.Init;
import org.mtr.mod.render.MoreRenderLayers;

public class ModelCableCarGrip extends EntityModelExtension<EntityAbstractMapping> {

	private final ModelPartExtension grip;
	private final Identifier texture = new Identifier(Init.MOD_ID, "textures/entity/cable_car_grip.png");

	public ModelCableCarGrip() {
		super(48, 48);

		grip = createModelPart();
		grip.setPivot(0, 24, 0);
		grip.setTextureUVOffset(1, 24).addCuboid(0, -0.2F, -8, 0, 0, 16, 0.2F, false);
		grip.setTextureUVOffset(14, 0).addCuboid(0, -1, -3, 1, 1, 6, 0, false);
		grip.setTextureUVOffset(12, 13).addCuboid(2, -1, -6, 1, 5, 5, 0, false);
		grip.setTextureUVOffset(0, 13).addCuboid(2, -1, 1, 1, 5, 5, 0, false);
		grip.setTextureUVOffset(0, 0).addCuboid(3, 0, -5, 2, 3, 10, 0, false);
		grip.setTextureUVOffset(0, 0).addCuboid(5, 0, -1, 3, 3, 2, 0, false);
		grip.setTextureUVOffset(19, 13).addCuboid(0, -2, -1, 5, 2, 2, 0, false);

		buildModel();
	}

	public void render(GraphicsHolder graphicsHolder, int light) {
		graphicsHolder.createVertexConsumer(MoreRenderLayers.getExterior(texture));
		ModelTrainBase.renderOnce(grip, graphicsHolder, light, 0);
	}

	@Override
	public void setAngles2(EntityAbstractMapping entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
	}

	@Override
	public final void render(GraphicsHolder graphicsHolder, int light, int overlay, float red, float green, float blue, float alpha) {
	}
}
