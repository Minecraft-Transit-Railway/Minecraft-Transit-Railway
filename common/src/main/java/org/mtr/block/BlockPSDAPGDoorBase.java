package org.mtr.block;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
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
import org.mtr.core.data.Vehicle;
import org.mtr.core.tool.Utilities;
import org.mtr.data.IGui;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.render.RenderVehicleHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class BlockPSDAPGDoorBase extends BlockPSDAPGBase implements BlockEntityProvider {

	public static final BooleanProperty END = BooleanProperty.of("end");
	public static final BooleanProperty UNLOCKED = BooleanProperty.of("unlocked");

	public BlockPSDAPGDoorBase(AbstractBlock.Settings settings) {
		super(settings);
	}

	@Nonnull
	@Override
	protected BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {
		if (IBlock.getSideDirection(state) == direction && !neighborState.isOf(this)) {
			return Blocks.AIR.getDefaultState();
		} else {
			final BlockState superState = super.getStateForNeighborUpdate(state, world, tickView, pos, direction, neighborPos, neighborState, random);
			if (superState.getBlock().equals(Blocks.AIR)) {
				return superState;
			} else {
				final boolean end = world.getBlockState(pos.offset(IBlock.getSideDirection(state).getOpposite())).getBlock() instanceof BlockPSDAPGGlassEndBase;
				return superState.with(END, end);
			}
		}
	}

	@Override
	public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		BlockPos offsetPos = pos;
		if (IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER) {
			offsetPos = offsetPos.down();
		}
		if (IBlock.getStatePropertySafe(state, SIDE) == EnumSide.RIGHT) {
			offsetPos = offsetPos.offset(IBlock.getSideDirection(state));
		}
		IBlock.onBreakCreative(world, player, offsetPos);
		return super.onBreak(world, pos, state, player);
	}

	@Nonnull
	@Override
	protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
		return IBlock.checkHoldingBrush(world, player, () -> {
			final boolean unlocked = IBlock.getStatePropertySafe(state, UNLOCKED);
			for (int y = -1; y <= 1; y++) {
				final BlockState scanState = world.getBlockState(pos.up(y));
				if (state.isOf(scanState.getBlock())) {
					lockDoor(world, pos.up(y), scanState, !unlocked);
				}
			}
			player.sendMessage((unlocked ? TranslationProvider.GUI_MTR_PSD_APG_DOOR_LOCKED : TranslationProvider.GUI_MTR_PSD_APG_DOOR_UNLOCKED).getText(), true);
		});
	}

	@Override
	protected void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		if (world.isClient() && entity instanceof PlayerEntity) {
			final Direction facing = IBlock.getStatePropertySafe(state, Properties.HORIZONTAL_FACING);
			// TODO don't hard code these bounds
			final boolean inDoorHitbox = switch (facing) {
				case NORTH -> entity.getZ() + RenderVehicleHelper.HALF_PLAYER_WIDTH > pos.getZ() + 0.01 && entity.getZ() - RenderVehicleHelper.HALF_PLAYER_WIDTH < pos.getZ() + 0.24;
				case EAST -> entity.getX() + RenderVehicleHelper.HALF_PLAYER_WIDTH > pos.getX() + 1 - 0.24 && entity.getX() - RenderVehicleHelper.HALF_PLAYER_WIDTH < pos.getX() + 1 - 0.01;
				case SOUTH -> entity.getZ() + RenderVehicleHelper.HALF_PLAYER_WIDTH > pos.getZ() + 1 - 0.24 && entity.getZ() - RenderVehicleHelper.HALF_PLAYER_WIDTH < pos.getZ() + 1 - 0.01;
				case WEST -> entity.getX() + RenderVehicleHelper.HALF_PLAYER_WIDTH > pos.getX() + 0.01 && entity.getX() - RenderVehicleHelper.HALF_PLAYER_WIDTH < pos.getX() + 0.24;
				default -> false;
			};

			if (inDoorHitbox) {
				final boolean southWest = facing == Direction.SOUTH || facing == Direction.WEST;
				final boolean side = IBlock.getStatePropertySafe(state, SIDE) == EnumSide.RIGHT;
				final double doorBlockedAmount = switch (facing) {
					case NORTH, SOUTH -> Utilities.isBetween(entity.getX(), pos.getX(), pos.getX() + 1) ? ((side == southWest) ? pos.getX() + 1 - entity.getX() : entity.getX() - pos.getX()) + RenderVehicleHelper.HALF_PLAYER_WIDTH : 0;
					case EAST, WEST -> Utilities.isBetween(entity.getZ(), pos.getZ(), pos.getZ() + 1) ? ((side == southWest) ? pos.getZ() + 1 - entity.getZ() : entity.getZ() - pos.getZ()) + RenderVehicleHelper.HALF_PLAYER_WIDTH : 0;
					default -> 0;
				};

				if (doorBlockedAmount > 0) {
					// TODO
				}
			}
		}
	}

	@Nonnull
	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		// The serverside collision shape is always empty, and the clientside collision shape is determined by the vehicle door positions the client sees
		final BlockEntity entity = world.getBlockEntity(pos);
		if (entity instanceof BlockEntityBase && entity.getWorld() != null && entity.getWorld().isClient() && ((BlockEntityBase) entity).getDoorValue() == 0) {
			return super.getCollisionShape(state, world, pos, context);
		} else {
			return VoxelShapes.empty();
		}
	}

	@Nonnull
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(END);
		builder.add(Properties.HORIZONTAL_FACING);
		builder.add(HALF);
		builder.add(SIDE);
		builder.add(UNLOCKED);
	}

	private static void lockDoor(World world, BlockPos pos, BlockState state, boolean unlocked) {
		final Direction facing = IBlock.getStatePropertySafe(state, Properties.HORIZONTAL_FACING);
		final BlockPos leftPos = pos.offset(facing.rotateYCounterclockwise());
		final BlockPos rightPos = pos.offset(facing.rotateYClockwise());
		final BlockState leftState = world.getBlockState(leftPos);
		final BlockState rightState = world.getBlockState(rightPos);

		if (leftState.isOf(state.getBlock())) {
			final BlockState toggled = leftState.with(UNLOCKED, unlocked);
			world.setBlockState(leftPos, toggled);
		}

		if (rightState.isOf(state.getBlock())) {
			final BlockState toggled = rightState.with(UNLOCKED, unlocked);
			world.setBlockState(rightPos, toggled);
		}

		world.setBlockState(pos, state.with(UNLOCKED, unlocked));
	}

	@Nullable
	private static BlockEntityBase getBottomBlockEntity(@Nullable World world, BlockPos pos) {
		final BlockEntity blockEntity = world == null ? null : world.getBlockEntity(pos.down(IBlock.getStatePropertySafe(world.getBlockState(pos), HALF) == DoubleBlockHalf.UPPER ? 1 : 0));
		return blockEntity instanceof BlockEntityBase ? (BlockEntityBase) blockEntity : null;
	}

	public static abstract class BlockEntityBase extends BlockEntity implements IGui {

		private double doorValue;
		private double doorOverrideValue;
		private int doorTarget;

		private static final int REDSTONE_DETECT_DEPTH = 3;

		public BlockEntityBase(BlockEntityType<?> type, BlockPos pos, BlockState state) {
			super(type, pos, state);
		}

		public void setDoorValue(double vehicleDoorValue) {
			final BlockEntityBase blockEntityBase = getBottomBlockEntity(getWorld(), getPos());
			if (blockEntityBase != null) {
				blockEntityBase.doorValue = Utilities.clamp(vehicleDoorValue, 0, 1);
				blockEntityBase.doorTarget = 1;
			}
		}

		public double getDoorValue() {
			final BlockState state = getCachedState();
			final Direction facing = IBlock.getStatePropertySafe(state, Properties.HORIZONTAL_FACING);
			final Direction otherDirection = IBlock.getStatePropertySafe(state, SIDE) == EnumSide.RIGHT ? facing.rotateYCounterclockwise() : facing.rotateYClockwise();
			final BlockEntityBase blockEntityBase1 = getBottomBlockEntity(getWorld(), getPos());
			final BlockEntityBase blockEntityBase2 = getBottomBlockEntity(getWorld(), getPos().offset(otherDirection));
			return Math.max(blockEntityBase1 == null ? 0 : blockEntityBase1.doorValue, blockEntityBase2 == null ? 0 : blockEntityBase2.doorValue);
		}

		public void tick(float tickDelta) {
			final World world = getWorld();
			if (world == null) {
				return;
			}

			// Only tick the bottom blocks
			if (IBlock.getStatePropertySafe(getCachedState(), HALF) == DoubleBlockHalf.UPPER) {
				return;
			}

			if (receivedRedstonePower(world, getPos(), getCachedState())) {
				doorTarget = 2;
			}

			final double millisElapsed = tickDelta * 20;

			if (doorTarget == 2) {
				doorValue = Math.min(1, doorValue + millisElapsed / Vehicle.DOOR_MOVE_TIME * 2);
			}

			if (doorTarget >= 0) {
				doorTarget--;
			} else {
				doorValue = Math.max(doorOverrideValue, doorValue - millisElapsed / Vehicle.DOOR_MOVE_TIME * 2);
			}

			doorOverrideValue = 0;
		}

		private static boolean receivedRedstonePower(World world, BlockPos pos, BlockState state) {
			if (!IBlock.getStatePropertySafe(state, UNLOCKED)) {
				return false;
			}

			final Direction facing = IBlock.getStatePropertySafe(state, Properties.HORIZONTAL_FACING);
			final Direction otherDirection = IBlock.getStatePropertySafe(state, SIDE) == EnumSide.RIGHT ? facing.rotateYCounterclockwise() : facing.rotateYClockwise();

			for (int i = 2; i <= REDSTONE_DETECT_DEPTH; i++) {
				final BlockPos checkPos = pos.down(i);
				final boolean emit = world.isEmittingRedstonePower(checkPos, Direction.UP);
				final boolean emitNearby = world.isEmittingRedstonePower(checkPos.offset(otherDirection), Direction.UP);
				if (emit || emitNearby) {
					return true;
				}
			}

			return false;
		}
	}
}