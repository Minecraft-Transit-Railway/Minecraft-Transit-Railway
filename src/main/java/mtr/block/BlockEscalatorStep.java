package mtr.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class BlockEscalatorStep extends BlockEscalatorBase {

	public static final BooleanProperty DIRECTION = BooleanProperty.of("direction");

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
		if (direction == Direction.UP && !(world.getBlockState(pos.up()).getBlock() instanceof BlockEscalatorSide)) {
			return Blocks.AIR.getDefaultState();
		} else {
			return super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
		}
	}

	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		if (state.get(SIDE) == EnumSide.RIGHT) {
			IBlock.onBreakCreative(world, player, pos.offset(IBlock.getSideDirection(state)));
		}
		super.onBreak(world, pos, state, player);
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		EnumEscalatorOrientation orientation = state.get(ORIENTATION);
		if (orientation == EnumEscalatorOrientation.FLAT || orientation == EnumEscalatorOrientation.TRANSITION_BOTTOM) {
			return Block.createCuboidShape(0, 0, 0, 16, 15, 16);
		} else {
			return VoxelShapes.combineAndSimplify(Block.createCuboidShape(1, 0, 1, 15, 16, 15), super.getCollisionShape(state, world, pos, context), BooleanBiFunction.AND);
		}
	}

	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		final Direction facing = state.get(FACING);
		final boolean direction = state.get(DIRECTION);
		final float speed = 0.1F;

		switch (facing) {
			case NORTH:
				entity.addVelocity(0, 0, direction ? -speed : speed);
				break;
			case EAST:
				entity.addVelocity(direction ? speed : -speed, 0, 0);
				break;
			case SOUTH:
				entity.addVelocity(0, 0, direction ? speed : -speed);
				break;
			case WEST:
				entity.addVelocity(direction ? -speed : speed, 0, 0);
				break;
			default:
				break;
		}
	}

	@Override
	public void onLandedUpon(World world, BlockPos pos, Entity entity, float distance) {
		super.onLandedUpon(world, pos, entity, distance * 0.5F);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		return IBlock.checkHoldingBrush(world, player, () -> {
			final boolean direction = !state.get(DIRECTION);
			final Direction blockFacing = state.get(FACING);

			update(world, pos, blockFacing, direction);
			update(world, pos, blockFacing.getOpposite(), direction);

			final BlockPos sidePos = pos.offset(IBlock.getSideDirection(state));
			if (isStep(world, sidePos)) {
				final BlockEscalatorStep block = (BlockEscalatorStep) world.getBlockState(sidePos).getBlock();
				block.update(world, sidePos, blockFacing, direction);
				block.update(world, sidePos, blockFacing.getOpposite(), direction);
			}
		});
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING, DIRECTION, ORIENTATION, SIDE);
	}

	private void update(World world, BlockPos pos, Direction offset, boolean direction) {
		world.setBlockState(pos, world.getBlockState(pos).with(DIRECTION, direction));
		final BlockPos offsetPos = pos.offset(offset);

		if (isStep(world, offsetPos)) {
			update(world, offsetPos, offset, direction);
		}
		if (isStep(world, offsetPos.up())) {
			update(world, offsetPos.up(), offset, direction);
		}
		if (isStep(world, offsetPos.down())) {
			update(world, offsetPos.down(), offset, direction);
		}
	}

	private boolean isStep(World world, BlockPos pos) {
		final Block block = world.getBlockState(pos).getBlock();
		return block instanceof BlockEscalatorStep;
	}
}
