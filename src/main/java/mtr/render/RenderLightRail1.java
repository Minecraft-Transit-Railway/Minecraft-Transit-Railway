package mtr.render;

import mtr.entity.EntityLightRail1;
import mtr.model.ModelSP1900;
import mtr.model.ModelTrainBase;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.util.Identifier;

public class RenderLightRail1 extends RenderTrainBase<EntityLightRail1> {

	private static final Identifier texture = new Identifier("textures/block/glowstone.png");
	private static final ModelSP1900 model = new ModelSP1900();

	public RenderLightRail1(EntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public Identifier getTexture(EntityLightRail1 entity) {
		return texture;
	}

	@Override
	protected ModelTrainBase getModel() {
		return model;
	}
}
