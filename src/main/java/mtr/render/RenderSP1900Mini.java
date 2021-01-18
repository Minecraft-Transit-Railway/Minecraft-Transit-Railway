package mtr.render;

import mtr.entity.EntitySP1900Mini;
import mtr.model.ModelSP1900Mini;
import mtr.model.ModelTrainBase;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.util.Identifier;

public class RenderSP1900Mini extends RenderTrainBase<EntitySP1900Mini> {

	private static final Identifier texture = new Identifier("mtr:textures/entity/sp1900.png");
	private static final ModelSP1900Mini model = new ModelSP1900Mini();

	public RenderSP1900Mini(EntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public Identifier getTexture(EntitySP1900Mini entity) {
		return texture;
	}

	@Override
	protected ModelTrainBase getModel() {
		return model;
	}
}
