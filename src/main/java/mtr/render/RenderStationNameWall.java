package mtr.render;

import mtr.block.BlockStationNameBase;
import mtr.block.BlockStationNameWall;
import mtr.gui.IDrawing;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;

public class RenderStationNameWall extends RenderStationNameBase<BlockStationNameWall.TileEntityStationNameWall> {

	@Override
	protected void drawStationName(BlockStationNameBase.TileEntityStationNameBase entity, MatrixStack matrices, VertexConsumerProvider vertexConsumers, VertexConsumerProvider.Immediate immediate, String stationName, int color, int light) {
		IDrawing.drawStringWithFont(matrices, MinecraftClient.getInstance().textRenderer, immediate, stationName, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, 0, 0, 60, color, false, light, null);
	}
}
