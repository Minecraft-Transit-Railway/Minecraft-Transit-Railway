package mtr.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;

public class BlockCeiling extends Block {

	public static final BooleanProperty FACING = BooleanProperty.of("facing");
	public static final BooleanProperty LIGHT = BooleanProperty.of("light");

	public BlockCeiling(Settings settings) {
		super(settings);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		boolean facing = ctx.getPlayerFacing().getAxis() == Direction.Axis.X;
		return getDefaultState().with(FACING, facing).with(LIGHT, hasLight(facing, ctx.getBlockPos()));
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return Block.createCuboidShape(0, 7, 0, 16, 10, 16);
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
		return state.with(LIGHT, hasLight(state.get(FACING), pos));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING, LIGHT);
	}

	private boolean hasLight(boolean facing, BlockPos pos) {
		if (facing) {
			return pos.getZ() % 3 == 0;
		} else {
			return pos.getX() % 3 == 0;
		}
	}
}
