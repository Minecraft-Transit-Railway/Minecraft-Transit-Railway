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
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;

public class BlockAPGGlass extends BlockPSDAPGGlassBase implements EntityBlockMapper {

	public static final IntegerProperty ARROW_DIRECTION = IntegerProperty.create("propagate_property", 0, 3);

	@Override
	public Item asItem() {
		return Items.APG_GLASS.get();
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
		final double y = blockHitResult.getLocation().y;
		if (IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER && y - Math.floor(y) > 0.21875) {
			return IBlock.checkHoldingBrush(world, player, () -> {
				world.setBlockAndUpdate(pos, state.cycle(ARROW_DIRECTION));
				propagate(world, pos, IBlock.getStatePropertySafe(state, FACING).getClockWise(), ARROW_DIRECTION, 3);
				propagate(world, pos, IBlock.getStatePropertySafe(state, FACING).getCounterClockWise(), ARROW_DIRECTION, 3);
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
		builder.add(FACING, HALF, SIDE_EXTENDED, ARROW_DIRECTION);
	}

	public static class TileEntityAPGGlass extends BlockPSDTop.TileEntityRouteBase {

		public TileEntityAPGGlass(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.APG_GLASS_TILE_ENTITY.get(), pos, state);
		}
	}
}
