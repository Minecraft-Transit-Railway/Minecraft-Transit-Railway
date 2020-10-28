package mtr.render;

import mtr.entity.EntitySP1900;
import net.minecraft.client.model.Model;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.model.MinecartEntityModel;
import net.minecraft.util.Identifier;

public class RenderMTrain extends RenderTrainBase<EntitySP1900> {

	public RenderMTrain(EntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public Identifier getTexture(EntitySP1900 entity) {
		return new Identifier("textures/block/iron_block.png");
	}

	@Override
	protected Model getModel() {
		return new MinecartEntityModel<>();
	}
}
