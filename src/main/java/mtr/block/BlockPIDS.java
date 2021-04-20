package mtr.block;

import mtr.MTR;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class BlockPIDS extends HorizontalFacingBlock implements BlockEntityProvider {

	private final int style;

	public BlockPIDS(int style) {
		super(FabricBlockSettings.of(Material.METAL, MaterialColor.IRON).requiresTool().hardness(2).luminance(5));
		this.style = style;
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
		if (IBlock.getStatePropertySafe(state, FACING) == direction && !newState.isOf(this)) {
			return Blocks.AIR.getDefaultState();
		} else {
			return state;
		}
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		final Direction direction = ctx.getPlayerFacing().getOpposite();
		return IBlock.isReplaceable(ctx, direction, 2) ? getDefaultState().with(FACING, direction) : null;
	}

	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		final Direction facing = IBlock.getStatePropertySafe(state, FACING);
		if (facing == Direction.SOUTH || facing == Direction.WEST) {
			IBlock.onBreakCreative(world, player, pos.offset(facing));
		}
		super.onBreak(world, pos, state, player);
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		if (!world.isClient) {
			final Direction direction = IBlock.getStatePropertySafe(state, FACING);
			world.setBlockState(pos.offset(direction), getDefaultState().with(FACING, direction.getOpposite()), 3);
			world.updateNeighbors(pos, Blocks.AIR);
			state.updateNeighbors(world, pos, 3);
		}
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		VoxelShape shape1 = IBlock.getVoxelShapeByDirection(6, 0, 0, 10, style == 1 ? 11 : 9, 16, IBlock.getStatePropertySafe(state, FACING));
		VoxelShape shape2 = IBlock.getVoxelShapeByDirection(7.5, 11, style == 1 ? 11 : 9, 8.5, 16, 13.5, IBlock.getStatePropertySafe(state, FACING));
		return VoxelShapes.union(shape1, shape2);
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new TileEntityBlockPIDS(style);
	}

	@Override
	public PistonBehavior getPistonBehavior(BlockState state) {
		return PistonBehavior.BLOCK;
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}


	public static class TileEntityBlockPIDS extends BlockEntity {

		public TileEntityBlockPIDS(int style) {
			super(style == 1 ? MTR.PIDS_2_TILE_ENTITY : MTR.PIDS_1_TILE_ENTITY);
		}
	}
}
