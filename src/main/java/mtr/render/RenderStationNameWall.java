package mtr.render;

import mtr.block.BlockStationNameBase;
import mtr.block.BlockStationNameWall;
import mtr.gui.IGui;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;

public class RenderStationNameWall extends RenderStationNameBase<BlockStationNameWall.TileEntityStationNameWall> {

	public RenderStationNameWall(BlockEntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	protected void drawStationName(BlockStationNameBase.TileEntityStationNameBase entity, MatrixStack matrices, VertexConsumerProvider vertexConsumers, String stationName, int color) {
		IGui.drawStringWithFont(matrices, MinecraftClient.getInstance().textRenderer, stationName, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, 0, 0, 60, color, false, null);
	}
}
