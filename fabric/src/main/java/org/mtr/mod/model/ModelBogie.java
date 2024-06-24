package org.mtr.mod.model;

import org.mtr.mapping.holder.EntityAbstractMapping;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.mapper.EntityModelExtension;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.ModelPartExtension;
import org.mtr.mod.Init;
import org.mtr.mod.render.RenderTrains;
import org.mtr.mod.render.StoredMatrixTransformations;

public class ModelBogie extends EntityModelExtension<EntityAbstractMapping> {

	private final ModelPartExtension bogie;
	private final Identifier texture = new Identifier(Init.MOD_ID, "textures/vehicle/bogie_1.png");

	public ModelBogie() {
		super(186, 77);

		bogie = createModelPart();
		bogie.setPivot(0, 24, 0);
		bogie.setTextureUVOffset(0, 0).addCuboid(-14, 1, -32, 1, 13, 64, 0, false);
		bogie.setTextureUVOffset(0, 0).addCuboid(-13, 1.5F, 16, 1, 14, 14, 0, false);
		bogie.setTextureUVOffset(0, 0).addCuboid(-13, 1.5F, -30, 1, 14, 14, 0, false);
		bogie.setTextureUVOffset(66, 0).addCuboid(-13, 1, -23.5F, 13, 8, 47, 0, false);

		buildModel();
	}

	public void render(StoredMatrixTransformations storedMatrixTransformations, int light) {
		RenderTrains.scheduleRender(texture, false, RenderTrains.QueuedRenderLayer.EXTERIOR, (graphicsHolder, offset) -> {
			storedMatrixTransformations.transform(graphicsHolder, offset);
			ModelTrainBase.renderMirror(bogie, graphicsHolder, light, 0);
			graphicsHolder.pop();
		});
	}

	@Override
	public void setAngles2(EntityAbstractMapping entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
	}

	@Override
	public final void render(GraphicsHolder graphicsHolder, int light, int overlay, float red, float green, float blue, float alpha) {
	}
}
