package mtr.render;

import mtr.block.BlockStationNameBase;
import mtr.block.BlockStationNameEntrance;
import mtr.block.IBlock;
import mtr.block.IPropagateBlock;
import mtr.gui.IGui;
import mtr.model.ModelTrainBase;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;

public class RenderStationNameEntrance extends RenderStationNameBase<BlockStationNameEntrance.TileEntityStationNameEntrance> implements IPropagateBlock {

	public RenderStationNameEntrance(BlockEntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	protected void drawStationName(BlockStationNameBase.TileEntityStationNameBase entity, MatrixStack matrices, VertexConsumerProvider vertexConsumers, String stationName, int color) {
		final WorldAccess world = entity.getWorld();
		final BlockPos pos = entity.getPos();

		if (world == null) {
			return;
		}

		final int propagateProperty = IBlock.getStatePropertySafe(world, pos, PROPAGATE_PROPERTY);
		final float logoSize = propagateProperty % 2 == 0 ? 0.5F : 1;
		final int length = getLength(world, pos);
		IGui.drawStringWithFont(matrices, MinecraftClient.getInstance().textRenderer, IGui.addToStationName(stationName, "", "", "站", " Station"), HorizontalAlignment.LEFT, VerticalAlignment.CENTER, HorizontalAlignment.CENTER, (length + logoSize) / 2 - 0.5F, 0, length - logoSize, logoSize - 0.125F, 40 / logoSize, propagateProperty < 2 ? ARGB_WHITE : ARGB_BLACK, false, ((x1, y1, x2, y2) -> IGui.drawTexture(matrices, vertexConsumers, "mtr:textures/sign/logo.png", x1 - logoSize, -logoSize / 2, logoSize, logoSize, ModelTrainBase.MAX_LIGHT)));
	}

	private int getLength(WorldAccess world, BlockPos pos) {
		if (world == null) {
			return 1;
		}
		final Direction facing = IBlock.getStatePropertySafe(world, pos, BlockStationNameEntrance.FACING);

		int length = 1;
		while (true) {
			final BlockState state = world.getBlockState(pos.offset(facing.rotateYClockwise(), length));
			if (state.getBlock() instanceof BlockStationNameEntrance) {
				length++;
			} else {
				break;
			}
		}

		return length;
	}
}
