package mtr.render;

import mtr.entity.EntityLightRail1;
import mtr.model.ModelLightRail1;
import mtr.model.ModelTrainBase;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.util.Identifier;

public class RenderLightRail1 extends RenderTrainBase<EntityLightRail1> {

	private static final Identifier texture = new Identifier("mtr:textures/entity/light_rail_1.png");
	private static final ModelLightRail1 model = new ModelLightRail1();

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
