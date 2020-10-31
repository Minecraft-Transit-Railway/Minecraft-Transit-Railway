package mtr.render;

import mtr.entity.EntityLightRail1;
import net.minecraft.client.model.Model;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.model.MinecartEntityModel;
import net.minecraft.util.Identifier;

public class RenderLightRail1 extends RenderTrainBase<EntityLightRail1> {

	public RenderLightRail1(EntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public Identifier getTexture(EntityLightRail1 entity) {
		return new Identifier("textures/block/glowstone.png");
	}

	@Override
	protected Model getModel() {
		return new MinecartEntityModel<>();
	}
}
