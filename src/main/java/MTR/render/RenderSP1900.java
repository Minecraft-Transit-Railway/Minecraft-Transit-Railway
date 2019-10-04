package mtr.render;

import mtr.entity.EntitySP1900;
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
	protected void render(EntitySP1900 entity, float leftDoor, float rightDoor) {
		final int head = 1;
		bindTexture(new ResourceLocation("mtr:textures/signs/door.png"));
		model.renderDoorLabels(0.0625F, leftDoor, rightDoor, head == 6);
		bindEntityTexture(entity);
		model.render(entity, 0, 0, -0.1F, 0, 0, 0.0625F);
		if (head == 6)
			model.renderFirstClass(0.0625F, leftDoor, rightDoor);
		else {
			model.renderDoors(0.0625F, leftDoor, rightDoor);
			if (head == 7)
				GlStateManager.rotate(180, 0, 1, 0);
			model.renderMain(0.0625F);
			if (head == 1 || head == 7)
				model.renderEnd(0.0625F);
			else
				model.renderNonEnd(0.0625F);
		}
		GlStateManager.disableLighting();
		model.renderLights(0.0625F);
		GlStateManager.enableLighting();
	}
}