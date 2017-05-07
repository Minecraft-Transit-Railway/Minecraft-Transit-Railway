package MTR;

import java.util.Calendar;

import org.lwjgl.opengl.GL11;

import MTR.blocks.BlockPSDTop;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class TileEntityPIDS1Renderer extends TileEntitySpecialRenderer {

	int position;
	boolean engText = false;
	private final int MAX_FRAMES = 550; // or 1000

	@Override
	public void renderTileEntityAt(TileEntity te2, double x, double y, double z, float partialTicks, int destroyStage) {
		int meta = te2.getBlockMetadata();
		GL11.glPushMatrix();
		GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
		GlStateManager.translate(x, y, z);
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		long calendar = Calendar.getInstance().getTimeInMillis();
		engText = calendar % 6000 >= 3000;
		if (!TileEntityPIDS1Entity.game && calendar > TileEntityPIDS1Entity.cooldown) {
			position = (int) (calendar * 3 / 100L % MAX_FRAMES);
			if (position > MAX_FRAMES || position < 0)
				position = 0;
			String s = "0";
			if (position < 10)
				s += "0";
			if (position < 100)
				s += "0";
			bindTexture(new ResourceLocation("mtr:textures/blocks/pids/PIDS" + s + position + ".png"));
		} else
			bindTexture(new ResourceLocation("mtr:textures/blocks/pids/ttt/Back.png"));
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDepthMask(true);
		GlStateManager.color(1, 1, 1);
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
		switch (meta) {
		case 0:
			// left
			worldrenderer.pos(-0.9375, 0.0625, 0.373).tex(1, 1).endVertex();
			worldrenderer.pos(-0.9375, 0.625, 0.373).tex(1, 0).endVertex();
			worldrenderer.pos(0.9375, 0.625, 0.373).tex(0, 0).endVertex();
			worldrenderer.pos(0.9375, 0.0625, 0.373).tex(0, 1).endVertex();
			// right
			worldrenderer.pos(0.9375, 0.0625, 0.626).tex(1, 1).endVertex();
			worldrenderer.pos(0.9375, 0.625, 0.626).tex(1, 0).endVertex();
			worldrenderer.pos(-0.9375, 0.625, 0.626).tex(0, 0).endVertex();
			worldrenderer.pos(-0.9375, 0.0625, 0.626).tex(0, 1).endVertex();
			break;
		case 1:
			// left
			worldrenderer.pos(0.626, 0.0625, -0.9375).tex(1, 1).endVertex();
			worldrenderer.pos(0.626, 0.625, -0.9375).tex(1, 0).endVertex();
			worldrenderer.pos(0.626, 0.625, 0.9375).tex(0, 0).endVertex();
			worldrenderer.pos(0.626, 0.0625, 0.9375).tex(0, 1).endVertex();
			// right
			worldrenderer.pos(0.373, 0.0625, 0.9375).tex(1, 1).endVertex();
			worldrenderer.pos(0.373, 0.625, 0.9375).tex(1, 0).endVertex();
			worldrenderer.pos(0.373, 0.625, -0.9375).tex(0, 0).endVertex();
			worldrenderer.pos(0.373, 0.0625, -0.9375).tex(0, 1).endVertex();
			break;
		case 2:
			// left
			worldrenderer.pos(1.9375, 0.0625, 0.626).tex(1, 1).endVertex();
			worldrenderer.pos(1.9375, 0.625, 0.626).tex(1, 0).endVertex();
			worldrenderer.pos(0.0625, 0.625, 0.626).tex(0, 0).endVertex();
			worldrenderer.pos(0.0625, 0.0625, 0.626).tex(0, 1).endVertex();
			// right
			worldrenderer.pos(0.0625, 0.0625, 0.373).tex(1, 1).endVertex();
			worldrenderer.pos(0.0625, 0.625, 0.373).tex(1, 0).endVertex();
			worldrenderer.pos(1.9375, 0.625, 0.373).tex(0, 0).endVertex();
			worldrenderer.pos(1.9375, 0.0625, 0.373).tex(0, 1).endVertex();
			break;
		case 3:
			// left
			worldrenderer.pos(0.373, 0.0625, 1.9375).tex(1, 1).endVertex();
			worldrenderer.pos(0.373, 0.625, 1.9375).tex(1, 0).endVertex();
			worldrenderer.pos(0.373, 0.625, 0.0625).tex(0, 0).endVertex();
			worldrenderer.pos(0.373, 0.0625, 0.0625).tex(0, 1).endVertex();
			// right
			worldrenderer.pos(0.626, 0.0625, 0.0625).tex(1, 1).endVertex();
			worldrenderer.pos(0.626, 0.625, 0.0625).tex(1, 0).endVertex();
			worldrenderer.pos(0.626, 0.625, 1.9375).tex(0, 0).endVertex();
			worldrenderer.pos(0.626, 0.0625, 1.9375).tex(0, 1).endVertex();
			break;
		}
		tessellator.draw();
		if (TileEntityPIDS1Entity.game || TileEntityPIDS1Entity.cooldown > calendar) {
			for (int i = 0; i < 9; i++) {
				int j = TileEntityPIDS1Entity.getSquare(i);
				if (j != 0) {
					bindTexture(new ResourceLocation(
							"mtr:textures/blocks/pids/ttt/" + String.valueOf(i + 1) + (j == 1 ? "x" : "o") + ".png"));
					worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
					switch (meta) {
					case 0:
						worldrenderer.pos(-0.9375, 0.0625, 0.372).tex(1, 1).endVertex();
						worldrenderer.pos(-0.9375, 0.625, 0.372).tex(1, 0).endVertex();
						worldrenderer.pos(0.9375, 0.625, 0.372).tex(0, 0).endVertex();
						worldrenderer.pos(0.9375, 0.0625, 0.372).tex(0, 1).endVertex();
						break;
					case 1:
						worldrenderer.pos(0.627, 0.0625, -0.9375).tex(1, 1).endVertex();
						worldrenderer.pos(0.627, 0.625, -0.9375).tex(1, 0).endVertex();
						worldrenderer.pos(0.627, 0.625, 0.9375).tex(0, 0).endVertex();
						worldrenderer.pos(0.627, 0.0625, 0.9375).tex(0, 1).endVertex();
						break;
					case 2:
						worldrenderer.pos(1.9375, 0.0625, 0.627).tex(1, 1).endVertex();
						worldrenderer.pos(1.9375, 0.625, 0.627).tex(1, 0).endVertex();
						worldrenderer.pos(0.0625, 0.625, 0.627).tex(0, 0).endVertex();
						worldrenderer.pos(0.0625, 0.0625, 0.627).tex(0, 1).endVertex();
						break;
					case 3:
						worldrenderer.pos(0.372, 0.0625, 1.9375).tex(1, 1).endVertex();
						worldrenderer.pos(0.372, 0.625, 1.9375).tex(1, 0).endVertex();
						worldrenderer.pos(0.372, 0.625, 0.0625).tex(0, 0).endVertex();
						worldrenderer.pos(0.372, 0.0625, 0.0625).tex(0, 1).endVertex();
						break;
					}
					tessellator.draw();
				}
			}
			for (int i = 0; i < TileEntityPIDS1Entity.wins.size(); i++) {
				bindTexture(new ResourceLocation(
						"mtr:textures/blocks/pids/ttt/Win" + TileEntityPIDS1Entity.wins.get(i) + ".png"));
				worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
				switch (meta) {
				case 0:
					worldrenderer.pos(-0.9375, 0.0625, 0.371).tex(1, 1).endVertex();
					worldrenderer.pos(-0.9375, 0.625, 0.371).tex(1, 0).endVertex();
					worldrenderer.pos(0.9375, 0.625, 0.371).tex(0, 0).endVertex();
					worldrenderer.pos(0.9375, 0.0625, 0.371).tex(0, 1).endVertex();
					break;
				case 1:
					worldrenderer.pos(0.628, 0.0625, -0.9375).tex(1, 1).endVertex();
					worldrenderer.pos(0.628, 0.625, -0.9375).tex(1, 0).endVertex();
					worldrenderer.pos(0.628, 0.625, 0.9375).tex(0, 0).endVertex();
					worldrenderer.pos(0.628, 0.0625, 0.9375).tex(0, 1).endVertex();
					break;
				case 2:
					worldrenderer.pos(1.9375, 0.0625, 0.628).tex(1, 1).endVertex();
					worldrenderer.pos(1.9375, 0.625, 0.628).tex(1, 0).endVertex();
					worldrenderer.pos(0.0625, 0.625, 0.628).tex(0, 0).endVertex();
					worldrenderer.pos(0.0625, 0.0625, 0.628).tex(0, 1).endVertex();
					break;
				case 3:
					worldrenderer.pos(0.371, 0.0625, 1.9375).tex(1, 1).endVertex();
					worldrenderer.pos(0.371, 0.625, 1.9375).tex(1, 0).endVertex();
					worldrenderer.pos(0.371, 0.625, 0.0625).tex(0, 0).endVertex();
					worldrenderer.pos(0.371, 0.0625, 0.0625).tex(0, 1).endVertex();
					break;
				}
				tessellator.draw();
			}
		}
		GL11.glPopAttrib();
		GL11.glPopMatrix();

		BlockPos pos = te2.getPos(), pos3 = pos, pos4 = pos;
		switch (meta) {
		case 0:
			pos3 = pos.add(-2, 0, 0);
			pos4 = pos.add(1, 0, 0);
			break;
		case 1:
			pos3 = pos.add(0, 0, -2);
			pos4 = pos.add(0, 0, 1);
			break;
		case 2:
			pos3 = pos.add(2, 0, 0);
			pos4 = pos.add(-1, 0, 0);
			break;
		case 3:
			pos3 = pos.add(0, 0, 2);
			pos4 = pos.add(0, 0, -1);
			break;
		}
		World worldIn = te2.getWorld();
		int color = 0, bound = 0;
		if (worldIn.getBlockState(pos3).getBlock() instanceof BlockPSDTop) {
			TileEntityPSDTopEntity te4 = (TileEntityPSDTopEntity) worldIn.getTileEntity(pos3);
			color = te4.color;
			bound = te4.bound;
		}
		if (worldIn.getBlockState(pos4).getBlock() instanceof BlockPSDTop) {
			TileEntityPSDTopEntity te4 = (TileEntityPSDTopEntity) worldIn.getTileEntity(pos4);
			color = te4.color;
			bound = te4.bound;
		}

		if (color > 0) {
			GlStateManager.pushMatrix();
			GlStateManager.pushAttrib();
			String chinese = "", english = "";
			chinese = I18n.format("destination." + String.valueOf(color * 4 - 4 + bound) + "c", new Object[0]);
			english = I18n.format("destination." + String.valueOf(color * 4 - 4 + bound), new Object[0]);
			FontRenderer var20 = getFontRenderer();
			GlStateManager.translate(x + 0.5, y, z + 0.5);
			GlStateManager.rotate(-meta * 90 + 180, 0, 1, 0);
			GlStateManager.translate(-0.4375, 0.21875, 0.13);
			GlStateManager.scale(0.015625, -0.015625, 0.015625);
			if (meta == 0 || meta == 2)
				GL11.glNormal3f(0.1F, 0, 0);
			else
				GL11.glNormal3f(0, 0, 0.1F);
			var20.drawString(engText ? english : chinese, 0, 0, 0xFF9900);
			GlStateManager.popAttrib();
			GlStateManager.popMatrix();
		}
	}
}