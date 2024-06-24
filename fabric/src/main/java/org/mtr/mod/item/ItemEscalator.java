package org.mtr.mod.item;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.ItemExtension;
import org.mtr.mod.Blocks;
import org.mtr.mod.block.BlockEscalatorBase;
import org.mtr.mod.block.BlockEscalatorSide;
import org.mtr.mod.block.BlockEscalatorStep;
import org.mtr.mod.block.IBlock;

import javax.annotation.Nonnull;

public class ItemEscalator extends ItemExtension implements IBlock {

	public ItemEscalator(ItemSettings itemSettings) {
		super(itemSettings);
	}

	@Nonnull
	@Override
	public ActionResult useOnBlock2(ItemUsageContext context) {
		if (ItemPSDAPGBase.blocksNotReplaceable(context, 2, 2, null)) {
			return ActionResult.FAIL;
		}

		final World world = context.getWorld();
		Direction playerFacing = context.getPlayerFacing();
		BlockPos pos1 = context.getBlockPos().offset(context.getSide());
		BlockPos pos2 = pos1.offset(playerFacing.rotateYClockwise());

		final BlockState frontState = world.getBlockState(pos1.offset(playerFacing));
		if (frontState.getBlock().data instanceof BlockEscalatorBase) {
			if (IBlock.getStatePropertySafe(frontState, BlockEscalatorBase.FACING) == playerFacing.getOpposite()) {
				playerFacing = playerFacing.getOpposite();
				final BlockPos pos3 = pos1;
				pos1 = pos2;
				pos2 = pos3;
			}
		}

		final BlockState stepState = Blocks.ESCALATOR_STEP.get().getDefaultState().with(new Property<>(BlockEscalatorStep.FACING.data), playerFacing.data);
		world.setBlockState(pos1, stepState.with(new Property<>(SIDE.data), EnumSide.LEFT));
		world.setBlockState(pos2, stepState.with(new Property<>(SIDE.data), EnumSide.RIGHT));

		final BlockState sideState = Blocks.ESCALATOR_SIDE.get().getDefaultState().with(new Property<>(BlockEscalatorSide.FACING.data), playerFacing.data);
		world.setBlockState(pos1.up(), sideState.with(new Property<>(SIDE.data), EnumSide.LEFT));
		world.setBlockState(pos2.up(), sideState.with(new Property<>(SIDE.data), EnumSide.RIGHT));

		context.getStack().decrement(1);
		return ActionResult.SUCCESS;
	}
}
