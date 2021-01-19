package mtr.block;

import mtr.Items;
import mtr.MTR;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class BlockAPGGlass extends BlockPSDAPGGlassBase implements BlockEntityProvider, IPropagateBlock {

	@Override
	public Item asItem() {
		return Items.APG_GLASS;
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER) {
			return IBlock.checkHoldingBrush(world, player, () -> {
				world.setBlockState(pos, state.cycle(PROPAGATE_PROPERTY));
				propagate(world, pos, IBlock.getStatePropertySafe(state, FACING).rotateYClockwise());
				propagate(world, pos, IBlock.getStatePropertySafe(state, FACING).rotateYCounterclockwise());
			});
		} else {
			return super.onUse(state, world, pos, player, hand, hit);
		}
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new TileEntityAPGGlass();
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING, HALF, SIDE_EXTENDED, PROPAGATE_PROPERTY);
	}

	public static class TileEntityAPGGlass extends BlockEntity {

		public TileEntityAPGGlass() {
			super(MTR.APG_GLASS_TILE_ENTITY);
		}
	}
}
