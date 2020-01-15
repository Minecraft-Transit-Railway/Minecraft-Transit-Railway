package mtr.render;

import mtr.entity.EntityMTrain;
import mtr.entity.EntityTrain.EnumTrainType;
import mtr.model.ModelMTrain;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderMTrain extends RenderTrain<EntityMTrain> {

	private final ModelMTrain model = new ModelMTrain();

	public RenderMTrain(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityMTrain entity) {
		return new ResourceLocation("mtr:textures/entity/m_train.png");
	}

	@Override
	protected void render(EntityMTrain entity, EnumTrainType trainType, float leftDoor, float rightDoor) {
		bindTexture(new ResourceLocation("mtr:textures/led_map.png"));
		model.renderLEDMap(0.0625F);
		bindTexture(new ResourceLocation("mtr:textures/signs/door.png"));
		model.renderDoorLabels(0.0625F, leftDoor, rightDoor);
		bindEntityTexture(entity);
		model.render(entity, 0, 0, -0.1F, 0, 0, 0.0625F);
		model.renderDoors(0.0625F, leftDoor, rightDoor);
	}

	@Override
	protected void renderLights() {
	}
}