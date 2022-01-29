package mtr.block;

import mtr.BlockEntityTypes;
import mtr.Items;
import mtr.mappings.BlockEntityMapper;
import mtr.mappings.EntityBlockMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.BlockHitResult;

public class BlockAPGGlass extends BlockPSDAPGGlassBase implements EntityBlockMapper, IPropagateBlock {

	@Override
	public Item asItem() {
		return Items.APG_GLASS;
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
		final double y = blockHitResult.getLocation().y;
		if (IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER && y - Math.floor(y) > 0.21875) {
			return IBlock.checkHoldingBrush(world, player, () -> {
				world.setBlockAndUpdate(pos, state.cycle(PROPAGATE_PROPERTY));
				propagate(world, pos, IBlock.getStatePropertySafe(state, FACING).getClockWise(), 3);
				propagate(world, pos, IBlock.getStatePropertySafe(state, FACING).getCounterClockWise(), 3);
			});
		} else {
			return super.use(state, world, pos, player, interactionHand, blockHitResult);
		}
	}

	@Override
	public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntityAPGGlass(pos, state);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, HALF, SIDE_EXTENDED, PROPAGATE_PROPERTY);
	}

	public static class TileEntityAPGGlass extends BlockEntityMapper {

		public TileEntityAPGGlass(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.APG_GLASS_TILE_ENTITY, pos, state);
		}
	}
}
