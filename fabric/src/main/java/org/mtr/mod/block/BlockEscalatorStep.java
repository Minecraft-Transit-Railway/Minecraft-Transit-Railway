package org.mtr.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.tool.HolderBase;

import javax.annotation.Nonnull;
import java.util.List;

public class BlockEscalatorStep extends BlockEscalatorBase {

	public static final BooleanProperty DIRECTION = BooleanProperty.of("direction");
	public static final BooleanProperty STATUS = BooleanProperty.of("status");

	@Nonnull
	@Override
	public BlockState getStateForNeighborUpdate2(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		if (direction == Direction.UP && !(world.getBlockState(pos.up()).getBlock().data instanceof BlockEscalatorSide)) {
			return Blocks.getAirMapped().getDefaultState();
		} else {
			return super.getStateForNeighborUpdate2(state, direction, neighborState, world, pos, neighborPos);
		}
	}

	@Override
	public void onBreak2(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		if (IBlock.getStatePropertySafe(state, SIDE) == EnumSide.RIGHT) {
			IBlock.onBreakCreative(world, player, pos.offset(IBlock.getSideDirection(state)));
		}
		super.onBreak2(world, pos, state, player);
	}

	@Nonnull
	@Override
	public VoxelShape getCollisionShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		final EnumEscalatorOrientation orientation = IBlock.getStatePropertySafe(state, new Property<>(ORIENTATION.data));
		if (orientation == EnumEscalatorOrientation.FLAT || orientation == EnumEscalatorOrientation.TRANSITION_BOTTOM) {
			return Block.createCuboidShape(0, 0, 0, 16, 15, 16);
		} else {
			return VoxelShapes.combine(Block.createCuboidShape(1, 0, 1, 15, 16, 15), super.getCollisionShape2(state, world, pos, context), BooleanBiFunction.getAndMapped());
		}
	}

	@Override
	public void onEntityCollision2(BlockState state, World world, BlockPos pos, Entity entity) {
		super.onEntityCollision2(state, world, pos, entity);
		final Direction facing = IBlock.getStatePropertySafe(state, FACING);
		final boolean direction = IBlock.getStatePropertySafe(state, DIRECTION);
		final float speed = 0.1F;

		if (IBlock.getStatePropertySafe(state, STATUS)) {
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
	}

	@Nonnull
	@Override
	public ActionResult onUse2(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		return IBlock.checkHoldingBrush(world, player, () -> {
			final boolean direction = IBlock.getStatePropertySafe(state, DIRECTION);
			final boolean running = IBlock.getStatePropertySafe(state, STATUS);
			final Direction blockFacing = IBlock.getStatePropertySafe(state, FACING);
			final boolean newDirection;
			final boolean newRunning;

			if (direction && running) {
				// FORWARD to BACKWARD
				newDirection = false;
				newRunning = true;
			} else if (!direction && running) {
				// BACKWARD to STOP
				newDirection = false;
				newRunning = false;
			} else {
				// STOP to FORWARD
				newDirection = true;
				newRunning = true;
			}

			update(world, pos, blockFacing, newDirection, newRunning);
			update(world, pos, blockFacing.getOpposite(), newDirection, newRunning);

			final BlockPos sidePos = pos.offset(IBlock.getSideDirection(state));
			if (isStep(world, sidePos)) {
				final BlockEscalatorStep block = (BlockEscalatorStep) world.getBlockState(sidePos).getBlock().data;
				block.update(world, sidePos, blockFacing, newDirection, newRunning);
				block.update(world, sidePos, blockFacing.getOpposite(), newDirection, newRunning);
			}
		});
	}

	@Override
	public void addBlockProperties(List<HolderBase<?>> properties) {
		properties.add(FACING);
		properties.add(DIRECTION);
		properties.add(ORIENTATION);
		properties.add(SIDE);
		properties.add(STATUS);
	}

	private void update(World world, BlockPos pos, Direction offset, boolean direction, boolean running) {
		world.setBlockState(pos, world.getBlockState(pos).with(new Property<>(DIRECTION.data), direction).with(new Property<>(STATUS.data), running));
		final BlockPos offsetPos = pos.offset(offset);

		if (isStep(world, offsetPos)) {
			update(world, offsetPos, offset, direction, running);
		}
		if (isStep(world, offsetPos.up())) {
			update(world, offsetPos.up(), offset, direction, running);
		}
		if (isStep(world, offsetPos.down())) {
			update(world, offsetPos.down(), offset, direction, running);
		}
	}

	private boolean isStep(World world, BlockPos pos) {
		final Block block = world.getBlockState(pos).getBlock();
		return block.data instanceof BlockEscalatorStep;
	}
}
