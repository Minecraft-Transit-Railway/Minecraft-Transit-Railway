package mtr.render;

import mtr.entity.EntityMTrain;
import mtr.model.ModelSP1900;
import mtr.model.ModelTrainBase;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.util.Identifier;

public class RenderMTrain extends RenderTrainBase<EntityMTrain> {

	private static final Identifier texture = new Identifier("mtr:textures/entity/m_train.png");
	private static final ModelSP1900 model = new ModelSP1900();

	public RenderMTrain(EntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public Identifier getTexture(EntityMTrain entity) {
		return texture;
	}

	@Override
	protected ModelTrainBase getModel() {
		return model;
	}
}
