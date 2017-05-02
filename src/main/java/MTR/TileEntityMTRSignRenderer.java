package MTR;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import MTR.client.ModelMTRSign;

public class TileEntityMTRSignRenderer extends TileEntitySpecialRenderer {

	private final ModelMTRSign model;

	public TileEntityMTRSignRenderer() {
		this.model = new ModelMTRSign();
	}

	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z,
			float scale, int arg5) {
		int meta = te.getBlockMetadata();
		GL11.glPushMatrix();
		// This is setting the initial location.
		GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
		ResourceLocation textures = new ResourceLocation(
				"MTR:textures/blocks/TileEntityMTRSign.png");
		Minecraft.getMinecraft().renderEngine.bindTexture(textures);
		GL11.glPushMatrix();
		GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
		GL11.glRotatef(meta * 90 - 90, 0.0F, 1.0F, 0.0F);
		this.model
				.render((Entity) null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
		GL11.glPopMatrix();
		GL11.glPopMatrix();
	}

}