package mtr.render;

import mtr.entity.EntityMTrainMini;
import mtr.model.ModelMTrainMini;
import mtr.model.ModelTrainBase;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.util.Identifier;

public class RenderMTrainMini extends RenderTrainBase<EntityMTrainMini> {

	private static final Identifier texture = new Identifier("mtr:textures/entity/m_train.png");
	private static final ModelMTrainMini model = new ModelMTrainMini();

	public RenderMTrainMini(EntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public Identifier getTexture(EntityMTrainMini entity) {
		return texture;
	}

	@Override
	protected ModelTrainBase getModel() {
		return model;
	}
}
