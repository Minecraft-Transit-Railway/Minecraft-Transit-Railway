package mtr.block;

import mtr.MTR;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class BlockClock extends Block implements BlockEntityProvider {

	public static final BooleanProperty FACING = BooleanProperty.of("facing");

	public BlockClock(Settings settings) {
		super(settings);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		final boolean facing = ctx.getPlayerFacing().getAxis() == Direction.Axis.X;
		return getDefaultState().with(FACING, facing);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		final Direction facing = IBlock.getStatePropertySafe(state, FACING) ? Direction.EAST : Direction.NORTH;
		return VoxelShapes.union(IBlock.getVoxelShapeByDirection(3, 0, 6, 13, 12, 10, facing), Block.createCuboidShape(7.5, 12, 7.5, 8.5, 16, 8.5));
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new TileEntityClock();
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	public static class TileEntityClock extends BlockEntity {

		public TileEntityClock() {
			super(MTR.CLOCK_TILE_ENTITY);
		}
	}
}
