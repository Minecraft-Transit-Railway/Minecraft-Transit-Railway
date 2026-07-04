package org.mtr.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;
import org.mtr.core.data.Vehicle;
import org.mtr.core.tool.Utilities;
import org.mtr.data.IGui;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.render.RenderVehicleHelper;

//? if >= 1.21.4 {
import net.minecraft.world.level.ScheduledTickAccess;
//? } else {
/*import net.minecraft.world.level.LevelAccessor;
 *///? }

public abstract class BlockPSDAPGDoorBase extends BlockPSDAPGBase implements EntityBlock {

	public static final BooleanProperty END = BooleanProperty.create("end");
	public static final BooleanProperty UNLOCKED = BooleanProperty.create("unlocked");

	public BlockPSDAPGDoorBase(BlockBehaviour.Properties settings) {
		super(settings);
	}

	@Override
//? if >= 1.21.4 {
	protected BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
//? } else {
	/*protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
//
*///? }
		if (IBlock.getSideDirection(state) == direction && !neighborState.is(this)) {
			return Blocks.AIR.defaultBlockState();
		} else {
//? if >= 1.21.4 {
			final BlockState superState = super.updateShape(state, world, tickView, pos, direction, neighborPos, neighborState, random);
//? } else {
			/*final BlockState superState = super.updateShape(state, direction, neighborState, world, pos, neighborPos);
//
*///? }
			if (superState.getBlock().equals(Blocks.AIR)) {
				return superState;
			} else {
				final boolean end = world.getBlockState(pos.relative(IBlock.getSideDirection(state).getOpposite())).getBlock() instanceof BlockPSDAPGGlassEndBase;
				return superState.setValue(END, end);
			}
		}
	}

	@Override
	public BlockState playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
		BlockPos offsetPos = pos;
		if (IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER) {
			offsetPos = offsetPos.below();
		}
		if (IBlock.getStatePropertySafe(state, SIDE) == EnumSide.RIGHT) {
			offsetPos = offsetPos.relative(IBlock.getSideDirection(state));
		}
		IBlock.playerWillDestroyCreative(world, player, offsetPos);
		return super.playerWillDestroy(world, pos, state, player);
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
		return IBlock.checkHoldingBrush(world, player, () -> {
			final boolean unlocked = IBlock.getStatePropertySafe(state, UNLOCKED);
			for (int y = -1; y <= 1; y++) {
				final BlockState scanState = world.getBlockState(pos.above(y));
				if (state.is(scanState.getBlock())) {
					lockDoor(world, pos.above(y), scanState, !unlocked);
				}
			}
			player.displayClientMessage((unlocked ? TranslationProvider.GUI_MTR_PSD_APG_DOOR_LOCKED : TranslationProvider.GUI_MTR_PSD_APG_DOOR_UNLOCKED).getText(), true);
		});
	}

	@Override
	protected void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
		if (world.isClientSide() && entity instanceof Player) {
			final Direction facing = IBlock.getStatePropertySafe(state, BlockStateProperties.HORIZONTAL_FACING);
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

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		// The serverside collision shape is always empty, and the clientside collision shape is determined by the vehicle door positions the client sees
		final BlockEntity entity = world.getBlockEntity(pos);
		if (entity instanceof BlockEntityBase && entity.getLevel() != null && entity.getLevel().isClientSide() && ((BlockEntityBase) entity).getDoorValue() == 0) {
			return super.getCollisionShape(state, world, pos, context);
		} else {
			return Shapes.empty();
		}
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(END);
		builder.add(BlockStateProperties.HORIZONTAL_FACING);
		builder.add(HALF);
		builder.add(SIDE);
		builder.add(UNLOCKED);
	}

	private static void lockDoor(Level world, BlockPos pos, BlockState state, boolean unlocked) {
		final Direction facing = IBlock.getStatePropertySafe(state, BlockStateProperties.HORIZONTAL_FACING);
		final BlockPos leftPos = pos.relative(facing.getCounterClockWise());
		final BlockPos rightPos = pos.relative(facing.getClockWise());
		final BlockState leftState = world.getBlockState(leftPos);
		final BlockState rightState = world.getBlockState(rightPos);

		if (leftState.is(state.getBlock())) {
			final BlockState toggled = leftState.setValue(UNLOCKED, unlocked);
			world.setBlockAndUpdate(leftPos, toggled);
		}

		if (rightState.is(state.getBlock())) {
			final BlockState toggled = rightState.setValue(UNLOCKED, unlocked);
			world.setBlockAndUpdate(rightPos, toggled);
		}

		world.setBlockAndUpdate(pos, state.setValue(UNLOCKED, unlocked));
	}

	@Nullable
	private static BlockEntityBase getBottomBlockEntity(@Nullable Level world, BlockPos pos) {
		final BlockEntity blockEntity = world == null ? null : world.getBlockEntity(pos.below(IBlock.getStatePropertySafe(world.getBlockState(pos), HALF) == DoubleBlockHalf.UPPER ? 1 : 0));
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
			final BlockEntityBase blockEntityBase = getBottomBlockEntity(getLevel(), getBlockPos());
			if (blockEntityBase != null) {
				blockEntityBase.doorValue = Math.clamp(vehicleDoorValue, 0, 1);
				blockEntityBase.doorTarget = 1;
			}
		}

		public double getDoorValue() {
			final BlockState state = getBlockState();
			final Direction facing = IBlock.getStatePropertySafe(state, BlockStateProperties.HORIZONTAL_FACING);
			final Direction otherDirection = IBlock.getStatePropertySafe(state, SIDE) == EnumSide.RIGHT ? facing.getCounterClockWise() : facing.getClockWise();
			final BlockEntityBase blockEntityBase1 = getBottomBlockEntity(getLevel(), getBlockPos());
			final BlockEntityBase blockEntityBase2 = getBottomBlockEntity(getLevel(), getBlockPos().relative(otherDirection));
			return Math.max(blockEntityBase1 == null ? 0 : blockEntityBase1.doorValue, blockEntityBase2 == null ? 0 : blockEntityBase2.doorValue);
		}

		public void tick(float tickDelta) {
			final Level world = getLevel();
			if (world == null) {
				return;
			}

			// Only tick the bottom blocks
			if (IBlock.getStatePropertySafe(getBlockState(), HALF) == DoubleBlockHalf.UPPER) {
				return;
			}

			if (receivedRedstonePower(world, getBlockPos(), getBlockState())) {
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

		private static boolean receivedRedstonePower(Level world, BlockPos pos, BlockState state) {
			if (!IBlock.getStatePropertySafe(state, UNLOCKED)) {
				return false;
			}

			final Direction facing = IBlock.getStatePropertySafe(state, BlockStateProperties.HORIZONTAL_FACING);
			final Direction otherDirection = IBlock.getStatePropertySafe(state, SIDE) == EnumSide.RIGHT ? facing.getCounterClockWise() : facing.getClockWise();

			for (int i = 2; i <= REDSTONE_DETECT_DEPTH; i++) {
				final BlockPos checkPos = pos.below(i);
				final boolean emit = world.hasSignal(checkPos, Direction.UP);
				final boolean emitNearby = world.hasSignal(checkPos.relative(otherDirection), Direction.UP);
				if (emit || emitNearby) {
					return true;
				}
			}

			return false;
		}
	}
}
