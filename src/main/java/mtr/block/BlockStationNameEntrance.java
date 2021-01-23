package mtr.block;

import mtr.MTR;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

public class BlockStationNameEntrance extends BlockStationNameBase {

	public BlockStationNameEntrance(Settings settings) {
		super(settings);
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		final Direction facing = IBlock.getStatePropertySafe(state, FACING);
		return world.getBlockState(pos.offset(facing)).isSideSolidFullSquare(world, pos.offset(facing), facing.getOpposite());
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		final Direction side = ctx.getSide();
		if (side != Direction.UP && side != Direction.DOWN) {
			return getDefaultState().with(FACING, side.getOpposite());
		} else {
			return null;
		}
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
		if (direction.getOpposite() == IBlock.getStatePropertySafe(state, FACING).getOpposite() && !state.canPlaceAt(world, pos)) {
			return Blocks.AIR.getDefaultState();
		} else {
			return state;
		}
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return IBlock.getVoxelShapeByDirection(0, 4, 0, 16, 12, 1, IBlock.getStatePropertySafe(state, FACING));
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return VoxelShapes.empty();
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new TileEntityStationNameEntrance();
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	public static class TileEntityStationNameEntrance extends TileEntityStationNameBase {

		public TileEntityStationNameEntrance() {
			super(MTR.STATION_NAME_ENTRANCE_TILE_ENTITY, false, false, 80, HorizontalAlignment.LEFT, 0, 0.000625F);
		}

		@Override
		public boolean shouldRender() {
			return world != null && !(world.getBlockState(pos.offset(IBlock.getStatePropertySafe(world, pos, FACING).rotateYCounterclockwise())).getBlock() instanceof BlockStationNameEntrance);
		}
	}
}
