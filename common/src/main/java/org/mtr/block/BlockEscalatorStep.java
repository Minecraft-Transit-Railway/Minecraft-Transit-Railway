package org.mtr.block;

import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;

import javax.annotation.Nonnull;

public class BlockEscalatorStep extends BlockEscalatorBase {

	public static final BooleanProperty DIRECTION = BooleanProperty.of("direction");
	public static final BooleanProperty STATUS = BooleanProperty.of("status");

	public BlockEscalatorStep(AbstractBlock.Settings settings) {
		super(settings);
	}

	@Nonnull
	@Override
	protected BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {
		if (direction == Direction.UP && !(world.getBlockState(pos.up()).getBlock() instanceof BlockEscalatorSide)) {
			return Blocks.AIR.getDefaultState();
		} else {
			return super.getStateForNeighborUpdate(state, world, tickView, pos, direction, neighborPos, neighborState, random);
		}
	}

	@Override
	public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		if (IBlock.getStatePropertySafe(state, SIDE) == EnumSide.RIGHT) {
			IBlock.onBreakCreative(world, player, pos.offset(IBlock.getSideDirection(state)));
		}
		return super.onBreak(world, pos, state, player);
	}

	@Nonnull
	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		final EnumEscalatorOrientation orientation = IBlock.getStatePropertySafe(state, ORIENTATION);
		if (orientation == EnumEscalatorOrientation.FLAT || orientation == EnumEscalatorOrientation.TRANSITION_BOTTOM) {
			return Block.createCuboidShape(0, 0, 0, 16, 15, 16);
		} else {
			return VoxelShapes.combine(Block.createCuboidShape(1, 0, 1, 15, 16, 15), super.getCollisionShape(state, world, pos, context), BooleanBiFunction.AND);
		}
	}

	@Override
	protected void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		super.onEntityCollision(state, world, pos, entity);
		final Direction facing = IBlock.getStatePropertySafe(state, Properties.HORIZONTAL_FACING);
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
	protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
		return IBlock.checkHoldingBrush(world, player, () -> {
			final boolean direction = IBlock.getStatePropertySafe(state, DIRECTION);
			final boolean running = IBlock.getStatePropertySafe(state, STATUS);
			final Direction blockFacing = IBlock.getStatePropertySafe(state, Properties.HORIZONTAL_FACING);
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
				final BlockEscalatorStep block = (BlockEscalatorStep) world.getBlockState(sidePos).getBlock();
				block.update(world, sidePos, blockFacing, newDirection, newRunning);
				block.update(world, sidePos, blockFacing.getOpposite(), newDirection, newRunning);
			}
		});
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(Properties.HORIZONTAL_FACING);
		builder.add(DIRECTION);
		builder.add(ORIENTATION);
		builder.add(SIDE);
		builder.add(STATUS);
	}

	private void update(World world, BlockPos pos, Direction offset, boolean direction, boolean running) {
		world.setBlockState(pos, world.getBlockState(pos).with(DIRECTION, direction).with(STATUS, running));
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
		return world.getBlockState(pos).getBlock() instanceof BlockEscalatorStep;
	}
}
