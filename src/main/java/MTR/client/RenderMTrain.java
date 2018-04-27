package MTR.client;

import MTR.EntityMTrain;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderMTrain extends RenderTrain<EntityMTrain> {

	private ModelMTrain model = new ModelMTrain();

	public RenderMTrain(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityMTrain entity) {
		return new ResourceLocation("mtr:textures/entity/EntityMTrain.png");
	}

	@Override
	protected void render(EntityMTrain entity, float leftDoor, float rightDoor) {
		int head = entity.mtrHead;
		bindTexture(new ResourceLocation("mtr:textures/signs/Door.png"));
		model.renderDoorLabels(0.0625F, leftDoor, rightDoor);
		bindEntityTexture(entity);
		model.render(entity, 0, 0, -0.1F, 0, 0, 0.0625F);
		model.renderDoors(0.0625F, leftDoor, rightDoor);
		// GlStateManager.disableLighting();
		// model.renderLights(0.0625F);
		// GlStateManager.enableLighting();
	}
}