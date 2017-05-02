package MTR;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import MTR.client.ModelTicketMachineTop;

public class TileEntityTicketMachineRenderer extends TileEntitySpecialRenderer {

	private final ModelTicketMachineTop model;

	public TileEntityTicketMachineRenderer() {
		this.model = new ModelTicketMachineTop();
	}

	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z,
			float scale, int arg5) {
		int meta = te.getBlockMetadata();
		if ((meta & 4) > 0) {
			GL11.glPushMatrix();
			// This is setting the initial location.
			GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F,
					(float) z + 0.5F);
			ResourceLocation textures = new ResourceLocation(
					"MTR:textures/blocks/TileEntityTicketMachineTop.png");
			Minecraft.getMinecraft().renderEngine.bindTexture(textures);
			GL11.glPushMatrix();
			GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(meta * 90 - 90, 0.0F, 1.0F, 0.0F);
			this.model.render((Entity) null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F,
					0.0625F);
			GL11.glPopMatrix();
			GL11.glPopMatrix();
		}
	}

}