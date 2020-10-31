package mtr.render;

import mtr.entity.EntityMTrain;
import mtr.model.ModelMTrain;
import net.minecraft.client.model.Model;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.util.Identifier;

public class RenderMTrain extends RenderTrainBase<EntityMTrain> {

	public RenderMTrain(EntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public Identifier getTexture(EntityMTrain entity) {
		return new Identifier("mtr:textures/entity/m_train.png");
	}

	@Override
	protected Model getModel() {
		return new ModelMTrain();
	}
}
