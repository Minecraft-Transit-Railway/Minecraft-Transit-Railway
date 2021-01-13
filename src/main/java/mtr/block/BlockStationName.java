package mtr.block;

import mtr.Items;
import mtr.MTR;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

public class BlockStationName extends HorizontalFacingBlock implements BlockEntityProvider {

	public static final IntProperty COLOR = IntProperty.of("color", 0, 2);

	public BlockStationName(Settings settings) {
		super(settings);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (!world.isClient() && player.isHolding(Items.BRUSH)) {
			world.setBlockState(pos, state.cycle(COLOR));
			return ActionResult.CONSUME;
		}
		return ActionResult.SUCCESS;
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		final Direction facing = state.get(FACING);
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
		if (direction.getOpposite() == state.get(FACING).getOpposite() && !state.canPlaceAt(world, pos)) {
			return Blocks.AIR.getDefaultState();
		} else {
			return state;
		}
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		switch (state.get(FACING)) {
			case NORTH:
				return Block.createCuboidShape(0, 0, 0, 16, 16, 1);
			case EAST:
				return Block.createCuboidShape(15, 0, 0, 16, 16, 16);
			case SOUTH:
				return Block.createCuboidShape(0, 0, 15, 16, 16, 16);
			case WEST:
				return Block.createCuboidShape(0, 0, 0, 1, 16, 16);
			default:
				return VoxelShapes.fullCube();
		}
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(COLOR, FACING);
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new TileEntityStationName();
	}

	public static class TileEntityStationName extends BlockEntity {

		public TileEntityStationName() {
			super(MTR.STATION_NAME_TILE_ENTITY);
		}
	}
}
