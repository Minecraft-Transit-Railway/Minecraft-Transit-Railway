package mtr.render;

import mtr.block.BlockStationNameBase;
import mtr.block.BlockStationNameTallBase;
import mtr.data.IGui;
import mtr.gui.IDrawing;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;

public class RenderStationNameTall<T extends BlockStationNameTallBase.TileEntityStationNameTallBase> extends RenderStationNameBase<T> {

	@Override
	protected void drawStationName(BlockStationNameBase.TileEntityStationNameBase entity, MatrixStack matrices, VertexConsumerProvider vertexConsumers, VertexConsumerProvider.Immediate immediate, String stationName, int color, int light) {
		IDrawing.drawStringWithFont(matrices, MinecraftClient.getInstance().textRenderer, immediate, IGui.formatVerticalChinese(stationName), HorizontalAlignment.CENTER, VerticalAlignment.CENTER, 0, 0, 0.75F, 1.5F, 80, color, false, light, null);
	}
}
