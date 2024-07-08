package org.mtr.mod.block;

import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mapping.holder.*;
import org.mtr.mod.generated.lang.TranslationProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class BlockLiftTrackHorizontal extends BlockLiftTrackBase {

	public BlockLiftTrackHorizontal() {
		super();
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return IBlock.getVoxelShapeByDirection(0, 6, 0, 16, 10, 1, IBlock.getStatePropertySafe(state, FACING));
	}

	@Override
	public void addTooltips(ItemStack stack, @Nullable BlockView world, List<MutableText> tooltip, TooltipContext options) {
		tooltip.add(TranslationProvider.TOOLTIP_MTR_LIFT_TRACK_HORIZONTAL.getMutableText().formatted(TextFormatting.GRAY));
	}

	@Override
	public ObjectArrayList<Direction> getConnectingDirections(BlockState blockState) {
		final Direction facing = IBlock.getStatePropertySafe(blockState, FACING);
		return ObjectArrayList.of(facing.rotateYClockwise(), facing.rotateYCounterclockwise());
	}
}
