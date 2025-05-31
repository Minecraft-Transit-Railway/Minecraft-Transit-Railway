package org.mtr.mod.block;

import org.mtr.core.data.Vehicle;
import org.mtr.core.tool.Utilities;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mapping.mapper.BlockWithEntity;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.data.IGui;
import org.mtr.mod.generated.lang.TranslationProvider;
import org.mtr.mod.render.RenderVehicleHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public abstract class BlockPSDAPGDoorBase extends BlockPSDAPGBase implements BlockWithEntity {

	public static final BooleanProperty END = BooleanProperty.of("end");
	public static final BooleanProperty UNLOCKED = BooleanProperty.of("unlocked");

	@Nonnull
	@Override
	public BlockState getStateForNeighborUpdate2(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		if (IBlock.getSideDirection(state) == direction && !neighborState.isOf(new Block(this))) {
			return Blocks.getAirMapped().getDefaultState();
		} else {
			final BlockState superState = super.getStateForNeighborUpdate2(state, direction, neighborState, world, pos, neighborPos);
			if (superState.getBlock().equals(Blocks.getAirMapped())) {
				return superState;
			} else {
				final boolean end = world.getBlockState(pos.offset(IBlock.getSideDirection(state).getOpposite())).getBlock().data instanceof BlockPSDAPGGlassEndBase;
				return superState.with(new Property<>(END.data), end);
			}
		}
	}

	@Override
	public void onBreak2(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		BlockPos offsetPos = pos;
		if (IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER) {
			offsetPos = offsetPos.down();
		}
		if (IBlock.getStatePropertySafe(state, SIDE) == EnumSide.RIGHT) {
			offsetPos = offsetPos.offset(IBlock.getSideDirection(state));
		}
		IBlock.onBreakCreative(world, player, offsetPos);
		super.onBreak2(world, pos, state, player);
	}

	@Nonnull
	@Override
	public ActionResult onUse2(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
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

	@Nonnull
	@Override
	public VoxelShape getCollisionShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		// The serverside collision shape is always empty, and the clientside collision shape is determined by the vehicle door positions the client sees
		final BlockEntity entity = world.getBlockEntity(pos);
		if (entity != null && entity.data instanceof BlockEntityBase && entity.getWorld() != null && entity.getWorld().isClient() && ((BlockEntityBase) entity.data).getDoorValue() == 0) {
			return super.getCollisionShape2(state, world, pos, context);
		} else {
			return VoxelShapes.empty();
		}
	}

	@Override
	public void onEntityCollision2(BlockState state, World world, BlockPos pos, Entity entity) {
		if (world.isClient() && PlayerEntity.isInstance(entity)) {
			final Direction facing = IBlock.getStatePropertySafe(state, FACING);
			final boolean inDoorHitbox;

			// TODO don't hard code these bounds
			switch (facing) {
				case NORTH:
					inDoorHitbox = entity.getZ() + RenderVehicleHelper.HALF_PLAYER_WIDTH > pos.getZ() + 0.01 && entity.getZ() - RenderVehicleHelper.HALF_PLAYER_WIDTH < pos.getZ() + 0.24;
					break;
				case EAST:
					inDoorHitbox = entity.getX() + RenderVehicleHelper.HALF_PLAYER_WIDTH > pos.getX() + 1 - 0.24 && entity.getX() - RenderVehicleHelper.HALF_PLAYER_WIDTH < pos.getX() + 1 - 0.01;
					break;
				case SOUTH:
					inDoorHitbox = entity.getZ() + RenderVehicleHelper.HALF_PLAYER_WIDTH > pos.getZ() + 1 - 0.24 && entity.getZ() - RenderVehicleHelper.HALF_PLAYER_WIDTH < pos.getZ() + 1 - 0.01;
					break;
				case WEST:
					inDoorHitbox = entity.getX() + RenderVehicleHelper.HALF_PLAYER_WIDTH > pos.getX() + 0.01 && entity.getX() - RenderVehicleHelper.HALF_PLAYER_WIDTH < pos.getX() + 0.24;
					break;
				default:
					inDoorHitbox = false;
			}

			if (inDoorHitbox) {
				final boolean southWest = facing == Direction.SOUTH || facing == Direction.WEST;
				final boolean side = IBlock.getStatePropertySafe(state, SIDE) == EnumSide.RIGHT;
				final double doorBlockedAmount;

				switch (facing) {
					case NORTH:
					case SOUTH:
						doorBlockedAmount = Utilities.isBetween(entity.getX(), pos.getX(), pos.getX() + 1) ? ((side == southWest) ? pos.getX() + 1 - entity.getX() : entity.getX() - pos.getX()) + RenderVehicleHelper.HALF_PLAYER_WIDTH : 0;
						break;
					case EAST:
					case WEST:
						doorBlockedAmount = Utilities.isBetween(entity.getZ(), pos.getZ(), pos.getZ() + 1) ? ((side == southWest) ? pos.getZ() + 1 - entity.getZ() : entity.getZ() - pos.getZ()) + RenderVehicleHelper.HALF_PLAYER_WIDTH : 0;
						break;
					default:
						doorBlockedAmount = 0;
				}

				if (doorBlockedAmount > 0) {
					// TODO
				}
			}
		}
	}

	@Nonnull
	@Override
	public BlockRenderType getRenderType2(BlockState state) {
		return BlockRenderType.getEntityblockAnimatedMapped();
	}

	@Override
	public void addBlockProperties(List<HolderBase<?>> properties) {
		properties.add(END);
		properties.add(FACING);
		properties.add(HALF);
		properties.add(SIDE);
		properties.add(UNLOCKED);
	}

	private static void lockDoor(World world, BlockPos pos, BlockState state, boolean unlocked) {
		final Direction facing = IBlock.getStatePropertySafe(state, FACING);
		final BlockPos leftPos = pos.offset(facing.rotateYCounterclockwise());
		final BlockPos rightPos = pos.offset(facing.rotateYClockwise());
		final BlockState leftState = world.getBlockState(leftPos);
		final BlockState rightState = world.getBlockState(rightPos);

		if (leftState.isOf(state.getBlock())) {
			final BlockState toggled = leftState.with(new Property<>(UNLOCKED.data), unlocked);
			world.setBlockState(leftPos, toggled);
		}

		if (rightState.isOf(state.getBlock())) {
			final BlockState toggled = rightState.with(new Property<>(UNLOCKED.data), unlocked);
			world.setBlockState(rightPos, toggled);
		}

		world.setBlockState(pos, state.with(new Property<>(UNLOCKED.data), unlocked));
	}

	@Nullable
	private static BlockEntityBase getBottomBlockEntity(@Nullable World world, BlockPos pos) {
		final BlockEntity blockEntity = world == null ? null : world.getBlockEntity(pos.down(IBlock.getStatePropertySafe(world.getBlockState(pos), HALF) == DoubleBlockHalf.UPPER ? 1 : 0));
		return blockEntity != null && blockEntity.data instanceof BlockEntityBase ? (BlockEntityBase) blockEntity.data : null;
	}

	public static abstract class BlockEntityBase extends BlockEntityExtension implements IGui {

		private double doorValue;
		private double doorOverrideValue;
		private int doorTarget;

		private static final int REDSTONE_DETECT_DEPTH = 3;

		public BlockEntityBase(BlockEntityType<?> type, BlockPos pos, BlockState state) {
			super(type, pos, state);
		}

		public void setDoorValue(double vehicleDoorValue) {
			final BlockEntityBase blockEntityBase = getBottomBlockEntity(getWorld2(), getPos2());
			if (blockEntityBase != null) {
				blockEntityBase.doorValue = Utilities.clamp(vehicleDoorValue, 0, 1);
				blockEntityBase.doorTarget = 1;
			}
		}

		public double getDoorValue() {
			final BlockState state = getCachedState2();
			final Direction facing = IBlock.getStatePropertySafe(state, FACING);
			final Direction otherDirection = IBlock.getStatePropertySafe(state, SIDE) == EnumSide.RIGHT ? facing.rotateYCounterclockwise() : facing.rotateYClockwise();
			final BlockEntityBase blockEntityBase1 = getBottomBlockEntity(getWorld2(), getPos2());
			final BlockEntityBase blockEntityBase2 = getBottomBlockEntity(getWorld2(), getPos2().offset(otherDirection));
			return Math.max(blockEntityBase1 == null ? 0 : blockEntityBase1.doorValue, blockEntityBase2 == null ? 0 : blockEntityBase2.doorValue);
		}

		public void tick(float tickDelta) {
			final World world = getWorld2();
			if (world == null) {
				return;
			}

			// Only tick the bottom blocks
			if (IBlock.getStatePropertySafe(getCachedState2(), HALF) == DoubleBlockHalf.UPPER) {
				return;
			}

			if (receivedRedstonePower(world, getPos2(), getCachedState2())) {
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

			final Direction facing = IBlock.getStatePropertySafe(state, FACING);
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