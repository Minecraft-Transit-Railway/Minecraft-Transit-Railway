package org.mtr.mod.block;

import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mapping.holder.*;
import org.mtr.mod.generated.lang.TranslationProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class BlockLiftTrackVertical extends BlockLiftTrackBase {

	public BlockLiftTrackVertical() {
		super();
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return IBlock.getVoxelShapeByDirection(6, 0, 0, 10, 16, 1, IBlock.getStatePropertySafe(state, FACING));
	}

	@Override
	public void addTooltips(ItemStack stack, @Nullable BlockView world, List<MutableText> tooltip, TooltipContext options) {
		tooltip.add(TranslationProvider.TOOLTIP_MTR_LIFT_TRACK_VERTICAL.getMutableText().formatted(TextFormatting.GRAY));
	}

	@Override
	public ObjectArrayList<Direction> getConnectingDirections(BlockState blockState) {
		return ObjectArrayList.of(Direction.UP, Direction.DOWN);
	}
}
