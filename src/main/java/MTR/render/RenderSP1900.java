package mtr.render;

import mtr.entity.EntitySP1900;
import mtr.entity.EntityTrain.EnumTrainType;
import mtr.model.ModelSP1900;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderSP1900 extends RenderTrain<EntitySP1900> {

	private final ModelSP1900 model = new ModelSP1900();

	public RenderSP1900(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntitySP1900 entity) {
		return new ResourceLocation("mtr:textures/entity/sp1900.png");
	}

	@Override
	protected void render(EntitySP1900 entity, EnumTrainType trainType, float leftDoor, float rightDoor) {
		bindTexture(new ResourceLocation("mtr:textures/signs/door.png"));
		model.renderDoorLabels(0.0625F, leftDoor, rightDoor, trainType == EnumTrainType.FIRST_CLASS);
		bindEntityTexture(entity);
		model.render(entity, 0, 0, -0.1F, 0, 0, 0.0625F);
		if (trainType == EnumTrainType.FIRST_CLASS)
			model.renderFirstClass(0.0625F, leftDoor, rightDoor);
		else {
			model.renderDoors(0.0625F, leftDoor, rightDoor);
			model.renderMain(0.0625F);
			if (trainType == EnumTrainType.HEAD)
				model.renderEnd(0.0625F);
			else
				model.renderNonEnd(0.0625F);
		}
		GlStateManager.disableLighting();
		model.renderLights(0.0625F);
		GlStateManager.enableLighting();
	}
}