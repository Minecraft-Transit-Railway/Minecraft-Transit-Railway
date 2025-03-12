package org.mtr.item;

import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.mtr.block.BlockEscalatorBase;
import org.mtr.block.IBlock;
import org.mtr.registry.Blocks;

import javax.annotation.Nonnull;

public class ItemEscalator extends Item implements IBlock {

	public ItemEscalator(Item.Settings settings) {
		super(settings);
	}

	@Nonnull
	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		if (ItemPSDAPGBase.blocksNotReplaceable(context, 2, 2, null)) {
			return ActionResult.FAIL;
		}

		final World world = context.getWorld();
		Direction playerFacing = context.getHorizontalPlayerFacing();
		BlockPos pos1 = context.getBlockPos().offset(context.getSide());
		BlockPos pos2 = pos1.offset(playerFacing.rotateYClockwise());

		final BlockState frontState = world.getBlockState(pos1.offset(playerFacing));
		if (frontState.getBlock() instanceof BlockEscalatorBase) {
			if (IBlock.getStatePropertySafe(frontState, Properties.HORIZONTAL_FACING) == playerFacing.getOpposite()) {
				playerFacing = playerFacing.getOpposite();
				final BlockPos pos3 = pos1;
				pos1 = pos2;
				pos2 = pos3;
			}
		}

		final BlockState stepState = Blocks.ESCALATOR_STEP.createAndGet().getDefaultState().with(Properties.HORIZONTAL_FACING, playerFacing);
		world.setBlockState(pos1, stepState.with(SIDE, EnumSide.LEFT));
		world.setBlockState(pos2, stepState.with(SIDE, EnumSide.RIGHT));

		final BlockState sideState = Blocks.ESCALATOR_SIDE.createAndGet().getDefaultState().with(Properties.HORIZONTAL_FACING, playerFacing);
		world.setBlockState(pos1.up(), sideState.with(SIDE, EnumSide.LEFT));
		world.setBlockState(pos2.up(), sideState.with(SIDE, EnumSide.RIGHT));

		context.getStack().decrement(1);
		return ActionResult.SUCCESS;
	}
}
