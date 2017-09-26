package MTR;

import MTR.client.ModelClock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class TileEntityClockRenderer extends TileEntitySpecialRenderer<TileEntityClockEntity> {

	private final ModelClock model;

	public TileEntityClockRenderer() {
		model = new ModelClock();
	}

	@Override
	public void renderTileEntityAt(TileEntityClockEntity te, double x, double y, double z, float partialTicks,
			int arg5) {
		boolean facing = te.getBlockMetadata() >= 8;
		model.time = getWorld().getWorldTime();
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) x + 0.5, (float) y + 1.5, (float) z + 0.5);
		ResourceLocation textures = new ResourceLocation("MTR:textures/blocks/TileEntityClockArm.png");
		GlStateManager.pushMatrix();
		GlStateManager.rotate(180F, 0.0F, 0.0F, 1.0F);
		if (facing)
			GlStateManager.rotate(90, 0.0F, 1.0F, 0.0F);
		Minecraft.getMinecraft().renderEngine.bindTexture(textures);
		GlStateManager.disableLighting();
		model.render((Entity) null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
		GlStateManager.enableLighting();
		GlStateManager.popMatrix();
		GlStateManager.popMatrix();
	}
}