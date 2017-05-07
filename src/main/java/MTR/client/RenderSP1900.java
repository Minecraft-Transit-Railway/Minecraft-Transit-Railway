package MTR.client;

import MTR.EntityTrainBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderSP1900 extends Render {

	private static final ResourceLocation minecartTextures = new ResourceLocation(
			"MTR:textures/entity/EntitySP1900.png");
	protected ModelSP1900 model = new ModelSP1900();

	public RenderSP1900(RenderManager renderManager) {
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
		GlStateManager.translate((float) x, (float) y + 1.5F, (float) z);
		GlStateManager.rotate((float) (entity1.clientYaw - 90D), 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate((float) entity1.clientPitch, 1.0F, 0.0F, 0.0F);
		GlStateManager.scale(-1.0F, -1.0F, 1.0F);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);
		float leftdoor = entity1.getLeftDoor() / 60F;
		float rightdoor = entity1.getRightDoor() / 60F;
		model.renderDoors(0.0625F, leftdoor, rightdoor);
		if (headOrTail == 2)
			GlStateManager.rotate(180F, 0, 1, 0);
		model.render(entity, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
		if (headOrTail == 0)
			model.renderNonEnd(0.0625F);
		else
			model.renderEnd(0.0625F);
		if (entity.riddenByEntity == null)
			model.renderPole(0.0625F);
		GlStateManager.disableLighting();
		model.renderLights(0.0625F);
		GlStateManager.enableLighting();
		GlStateManager.popMatrix();
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}
}
