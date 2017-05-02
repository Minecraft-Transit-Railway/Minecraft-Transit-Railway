package MTR;

import java.io.IOException;

import MTR.client.ModelLightRail1;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderLightRail1 extends Render {

	private static final ResourceLocation texture1 = new ResourceLocation("MTR:textures/entity/EntityLightRail1.png");
	protected ModelLightRail1 model = new ModelLightRail1();

	public RenderLightRail1(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return texture1;
	}

	@Override
	public void doRender(Entity entity, double x, double y, double z, float entityYaw, float partialTicks) {
		EntityLightRail1 entity1 = (EntityLightRail1) entity;
		renderTrain(entity1, x, y, z);
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}

	private void renderTrain(EntityLightRail1 entity, double x, double y, double z) {
		bindEntityTexture(entity);
		int route = entity.getRoute();
		boolean p;
		boolean down = route < 0;
		route = Math.abs(route);
		p = route >= 1000;
		if (p)
			route = route - 1000;
		ResourceLocation texture2 = new ResourceLocation(
				"mtr:textures/displays/lightrail/MTR-" + route + (p ? "P-" : "-") + (down ? "2" : "1") + ".png");
		ResourceLocation texture3 = new ResourceLocation(
				"mtr:textures/displays/lightrail/MTR-" + route + (p ? "P-" : "-") + (down ? "4" : "3") + ".png");
		ResourceLocation texture4 = new ResourceLocation(
				"mtr:textures/displays/lightrail/MTR-" + route + (p ? "P-" : "-") + (down ? "6" : "5") + ".png");
		ResourceLocation texture5 = new ResourceLocation(
				"mtr:textures/displays/lightrail/MTR-" + route + (p ? "P" : "") + "-7.png");
		try {
			Minecraft.getMinecraft().getResourceManager().getResource(texture2);
		} catch (IOException e) {
			texture2 = new ResourceLocation("mtr:textures/entity/MTR-default-1.png");
		}
		try {
			Minecraft.getMinecraft().getResourceManager().getResource(texture3);
		} catch (IOException e) {
			texture3 = new ResourceLocation("mtr:textures/entity/MTR-default-4.png");
		}
		try {
			Minecraft.getMinecraft().getResourceManager().getResource(texture4);
		} catch (IOException e) {
			texture4 = new ResourceLocation("mtr:textures/entity/MTR-default-5.png");
		}
		try {
			Minecraft.getMinecraft().getResourceManager().getResource(texture5);
		} catch (IOException e) {
			texture5 = new ResourceLocation("mtr:textures/entity/MTR-default-7.png");
		}
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) x, (float) y + 1.5F, (float) z);
		GlStateManager.rotate((float) (entity.clientYaw - 90D), 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate((float) entity.clientPitch, 1.0F, 0.0F, 0.0F);
		GlStateManager.scale(-1.0F, -1.0F, 1.0F);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);
		model.render(entity, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);

		float movedoor = 1 - entity.getLeftDoor() / 60F;
		if (movedoor < 1)
			movedoor = (float) (-7.9D * Math.pow(movedoor, 3) + 12.66D * Math.pow(movedoor, 2) - 5.75D * movedoor + 1D);
		else
			movedoor = 0;
		model.renderDoors(0.0625F, movedoor);

		GlStateManager.disableLighting();
		bindTexture(texture2);
		model.renderDisplay1(0.0625F);
		bindTexture(texture3);
		model.renderDisplay2(0.0625F);
		bindTexture(texture4);
		model.renderDisplay3(0.0625F);
		bindTexture(texture5);
		model.renderDisplay4(0.0625F);
		GlStateManager.enableLighting();

		GlStateManager.popMatrix();
	}
}
