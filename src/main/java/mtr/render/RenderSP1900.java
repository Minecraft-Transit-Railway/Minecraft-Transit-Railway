package mtr.render;

import mtr.entity.EntitySP1900;
import mtr.model.ModelSP1900;
import mtr.model.ModelTrainBase;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.util.Identifier;

public class RenderSP1900 extends RenderTrainBase<EntitySP1900> {

	private static final Identifier texture = new Identifier("mtr:textures/entity/sp1900.png");
	private static final ModelSP1900 model = new ModelSP1900();

	public RenderSP1900(EntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public Identifier getTexture(EntitySP1900 entity) {
		return texture;
	}

	@Override
	protected ModelTrainBase getModel() {
		return model;
	}
}
