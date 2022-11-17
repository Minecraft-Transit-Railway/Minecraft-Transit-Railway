package mtr.render;

import mtr.block.BlockStationNameBase;
import mtr.block.BlockStationNameEntrance;
import mtr.block.IBlock;
import mtr.client.ClientData;
import mtr.client.IDrawing;
import mtr.data.IGui;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class RenderStationNameTiled<T extends BlockStationNameBase.TileEntityStationNameBase> extends RenderStationNameBase<T> {

	private final boolean showLogo;

	public RenderStationNameTiled(BlockEntityRenderDispatcher dispatcher, boolean showLogo) {
		super(dispatcher);
		this.showLogo = showLogo;
	}

	@Override
	protected void drawStationName(BlockGetter world, BlockPos pos, BlockState state, Direction facing, StoredMatrixTransformations storedMatrixTransformations, MultiBufferSource vertexConsumers, String stationName, int stationColor, int color, int light) {
		final int lengthLeft = getLength(world, pos, false);
		final int lengthRight = getLength(world, pos, true);

		final int totalLength = lengthLeft + lengthRight - 1;
		if (showLogo) {
			final int propagateProperty = IBlock.getStatePropertySafe(world, pos, BlockStationNameEntrance.STYLE);
			final float logoSize = propagateProperty % 2 == 0 ? 0.5F : 1;
			RenderTrains.scheduleRender(ClientData.DATA_CACHE.getStationNameEntrance(propagateProperty < 2 || propagateProperty >= 4 ? ARGB_WHITE : ARGB_BLACK, IGui.insertTranslation("gui.mtr.station_cjk", "gui.mtr.station", 1, stationName), totalLength / logoSize).resourceLocation, false, RenderTrains.QueuedRenderLayer.INTERIOR, (matrices, vertexConsumer) -> {
				storedMatrixTransformations.transform(matrices);
				IDrawing.drawTexture(matrices, vertexConsumer, -0.5F, -logoSize / 2, 1, logoSize, (float) (lengthLeft - 1) / totalLength, 0, (float) lengthLeft / totalLength, 1, facing, color, light);
				matrices.popPose();
			});
		} else {
			RenderTrains.scheduleRender(ClientData.DATA_CACHE.getStationName(stationName, totalLength).resourceLocation, false, RenderTrains.QueuedRenderLayer.EXTERIOR, (matrices, vertexConsumer) -> {
				storedMatrixTransformations.transform(matrices);
				IDrawing.drawTexture(matrices, vertexConsumer, -0.5F, -0.5F, 1, 1, (float) (lengthLeft - 1) / totalLength, 0, (float) lengthLeft / totalLength, 1, facing, color, light);
				matrices.popPose();
			});
		}
	}

	private int getLength(BlockGetter world, BlockPos pos, boolean lookRight) {
		if (world == null) {
			return 1;
		}
		final Direction facing = IBlock.getStatePropertySafe(world, pos, BlockStationNameBase.FACING);
		final Block thisBlock = world.getBlockState(pos).getBlock();

		int length = 1;
		while (true) {
			final Block checkBlock = world.getBlockState(pos.relative(lookRight ? facing.getClockWise() : facing.getCounterClockWise(), length)).getBlock();
			if (checkBlock instanceof BlockStationNameBase && checkBlock == thisBlock) {
				length++;
			} else {
				break;
			}
		}

		return length;
	}
}
