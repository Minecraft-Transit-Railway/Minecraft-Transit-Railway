package MTR.client;

import MTR.EntityTrainBase;
import net.minecraft.client.model.ModelMinecart;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderMinecartSpecial extends Render {

	private static final ResourceLocation minecartTextures = new ResourceLocation("textures/entity/minecart.png");
	protected ModelMinecart model = new ModelMinecart();

	public RenderMinecartSpecial(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return minecartTextures;
	}

	@Override
	public void doRender(Entity entity, double x, double y, double z, float entityYaw, float partialTicks) {
		bindEntityTexture(entity);
		EntityTrainBase entity1 = (EntityTrainBase) entity;
		int headOrTail = entity1.getHeadOrTail();
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) x, (float) y + 0.5F, (float) z);
		GlStateManager.rotate((float) entity1.clientYaw, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate((float) entity1.clientPitch, 0.0F, 0.0F, 1.0F);
		GlStateManager.scale(-1.0F, -1.0F, 1.0F);
		model.render(entity, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
		GlStateManager.popMatrix();
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}
}
