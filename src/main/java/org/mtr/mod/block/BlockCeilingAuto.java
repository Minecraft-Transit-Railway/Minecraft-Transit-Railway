package mtr.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class BlockCeilingAuto extends BlockCeiling {

	public static final BooleanProperty LIGHT = BooleanProperty.create("light");

	public BlockCeilingAuto(Properties settings) {
		super(settings);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		final boolean facing = ctx.getHorizontalDirection().getAxis() == Direction.Axis.X;
		return defaultBlockState().setValue(FACING, facing).setValue(LIGHT, hasLight(facing, ctx.getClickedPos()));
	}

	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor world, BlockPos pos, BlockPos posFrom) {
		return state.setValue(LIGHT, hasLight(IBlock.getStatePropertySafe(state, FACING), pos));
	}

	@Override
	public void randomTick(BlockState state, ServerLevel world, BlockPos pos) {
		final boolean light = hasLight(IBlock.getStatePropertySafe(state, FACING), pos);
		if (IBlock.getStatePropertySafe(state, LIGHT) != light) {
			world.setBlockAndUpdate(pos, state.setValue(LIGHT, light));
		}
	}

	@Override
	public boolean isRandomlyTicking(BlockState blockState) {
		return true;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
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
