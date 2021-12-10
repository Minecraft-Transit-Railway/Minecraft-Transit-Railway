package mtr.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;

import java.util.Random;

public class BlockCeilingAuto extends BlockCeiling {

	public static final BooleanProperty LIGHT = BooleanProperty.of("light");

	public BlockCeilingAuto(Settings settings) {
		super(settings);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		final boolean facing = ctx.getPlayerFacing().getAxis() == Direction.Axis.X;
		return getDefaultState().with(FACING, facing).with(LIGHT, hasLight(facing, ctx.getBlockPos()));
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
		return state.with(LIGHT, hasLight(IBlock.getStatePropertySafe(state, FACING), pos));
	}

	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		final boolean light = hasLight(IBlock.getStatePropertySafe(state, FACING), pos);
		if (IBlock.getStatePropertySafe(state, LIGHT) != light) {
			world.setBlockState(pos, state.with(LIGHT, light));
		}
	}

	@Override
	public boolean hasRandomTicks(BlockState state) {
		return true;
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING, LIGHT);
	}

	private static boolean hasLight(boolean facing, BlockPos pos) {
		if (facing) {
			return pos.getZ() % 3 == 0;
		} else {
			return pos.getX() % 3 == 0;
		}
	}
}
