package MTR.client;

import MTR.EntityMinecartSpecial;
import net.minecraft.client.model.ModelMinecart;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderMinecartSpecial extends RenderTrain<EntityMinecartSpecial> {

	private ModelMinecart model = new ModelMinecart();

	public RenderMinecartSpecial(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityMinecartSpecial entity) {
		return new ResourceLocation("textures/entity/minecart.png");
	}

	@Override
	protected void renderBogie(EntityMinecartSpecial entity) {
		bindEntityTexture(entity);
		GlStateManager.scale(-1, -1, 1);
		GlStateManager.translate(0, -0.5, 0);
		model.render(entity, 0, 0, -0.1F, 0, 0, 0.0625F);
	}
}
