package mtr.item;

import mtr.Blocks;
import mtr.CreativeModeTabs;
import mtr.block.BlockEscalatorBase;
import mtr.block.BlockEscalatorSide;
import mtr.block.BlockEscalatorStep;
import mtr.block.IBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class ItemEscalator extends ItemWithCreativeTabBase implements IBlock {

	public ItemEscalator() {
		super(CreativeModeTabs.ESCALATORS_LIFTS);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		if (ItemPSDAPGBase.blocksNotReplaceable(context, 2, 2, null)) {
			return InteractionResult.FAIL;
		}

		final Level world = context.getLevel();
		Direction playerFacing = context.getHorizontalDirection();
		BlockPos pos1 = context.getClickedPos().relative(context.getClickedFace());
		BlockPos pos2 = pos1.relative(playerFacing.getClockWise());

		final BlockState frontState = world.getBlockState(pos1.relative(playerFacing));
		if (frontState.getBlock() instanceof BlockEscalatorBase) {
			if (IBlock.getStatePropertySafe(frontState, BlockEscalatorBase.FACING) == playerFacing.getOpposite()) {
				playerFacing = playerFacing.getOpposite();
				final BlockPos pos3 = pos1;
				pos1 = pos2;
				pos2 = pos3;
			}
		}

		final BlockState stepState = Blocks.ESCALATOR_STEP.get().defaultBlockState().setValue(BlockEscalatorStep.FACING, playerFacing);
		world.setBlockAndUpdate(pos1, stepState.setValue(SIDE, EnumSide.LEFT));
		world.setBlockAndUpdate(pos2, stepState.setValue(SIDE, EnumSide.RIGHT));

		final BlockState sideState = Blocks.ESCALATOR_SIDE.get().defaultBlockState().setValue(BlockEscalatorSide.FACING, playerFacing);
		world.setBlockAndUpdate(pos1.above(), sideState.setValue(SIDE, EnumSide.LEFT));
		world.setBlockAndUpdate(pos2.above(), sideState.setValue(SIDE, EnumSide.RIGHT));

		context.getItemInHand().shrink(1);
		return InteractionResult.SUCCESS;
	}
}
