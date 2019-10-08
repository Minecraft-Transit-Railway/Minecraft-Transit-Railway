package mtr.render;

import java.io.IOException;

import mtr.entity.EntityLightRail1;
import mtr.entity.EntityTrain.EnumTrainType;
import mtr.model.ModelLightRail1;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderLightRail1 extends RenderTrain<EntityLightRail1> {

	private final ModelLightRail1 model = new ModelLightRail1();

	public RenderLightRail1(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityLightRail1 entity) {
		return new ResourceLocation("mtr:textures/entity/light_rail_1.png");
	}

	@Override
	protected void render(EntityLightRail1 entity, EnumTrainType trainType, float leftDoor, float rightDoor) {
		int route = 751;
		boolean p;
		final boolean down = route < 0;
		route = Math.abs(route);
		p = route >= 1000;
		if (p)
			route = route - 1000;
		final ResourceLocation texture2 = getSignTexture(route, p, down ? 2 : 1);
		final ResourceLocation texture3 = getSignTexture(route, p, down ? 4 : 3);
		final ResourceLocation texture4 = getSignTexture(route, p, down ? 6 : 5);
		final ResourceLocation texture5 = getSignTexture(route, p, 7);

		model.render(entity, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);

		leftDoor = 1 - leftDoor;
		if (leftDoor < 1)
			leftDoor = (float) (-7.9D * Math.pow(leftDoor, 3) + 12.66D * Math.pow(leftDoor, 2) - 5.75D * leftDoor + 1D);
		else
			leftDoor = 0;
		model.renderDoors(0.0625F, leftDoor);

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
	}

	private ResourceLocation getSignTexture(int route, boolean p, int n) {
		ResourceLocation texture = new ResourceLocation("mtr:textures/displays/lightrail/MTR-" + route + (p ? "P-" : "-") + n + ".png");
		try {
			Minecraft.getMinecraft().getResourceManager().getResource(texture);
		} catch (final IOException e) {
			texture = new ResourceLocation("mtr:textures/entity/MTR-default-" + n + ".png");
		}
		return texture;
	}
}
