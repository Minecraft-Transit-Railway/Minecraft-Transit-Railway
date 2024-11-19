package org.mtr.mod.block;

import org.mtr.core.tool.Utilities;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mapping.mapper.BlockWithEntity;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.data.IGui;
import org.mtr.mod.generated.lang.TranslationProvider;

import javax.annotation.Nonnull;
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

	public static abstract class BlockEntityBase extends BlockEntityExtension implements IGui {

		private double doorValue;
		private double redstoneDoorValue;
		private static final int REDSTONE_DETECT_DEPTH = 2;

		public BlockEntityBase(BlockEntityType<?> type, BlockPos pos, BlockState state) {
			super(type, pos, state);
		}

		public void open(double vehicleDoorValue) {
			doorValue = Utilities.clamp(vehicleDoorValue * 2, 0, 1);
		}

		public double getDoorValue() {
			return Math.max(doorValue, redstoneDoorValue);
		}

		public void updateRedstone(float tickDelta) {
			final World world = getWorldMapped();
			final double delta = (tickDelta / 20) / 2;

			if (world != null && receivedRedstonePower(world, getPos2(), getCachedState2())) {
				redstoneDoorValue = Utilities.clamp(redstoneDoorValue + delta, 0, 1);
			} else {
				redstoneDoorValue = Utilities.clamp(redstoneDoorValue - delta, 0, 1);
			}
		}

		private boolean receivedRedstonePower(World world, BlockPos pos, BlockState state) {
			if (!IBlock.getStatePropertySafe(state, UNLOCKED)) {
				return false;
			}

			final DoubleBlockHalf half = IBlock.getStatePropertySafe(state, HALF);
			final Direction facing = IBlock.getStatePropertySafe(state, FACING);
			final EnumSide side = IBlock.getStatePropertySafe(state, SIDE);
			final Direction otherDirection = side == EnumSide.LEFT ? facing.rotateYClockwise() : facing.rotateYCounterclockwise();
			final BlockPos platformPos = (half == DoubleBlockHalf.UPPER) ? pos.down(2) : pos.down(1);

			for (int i = 0; i < REDSTONE_DETECT_DEPTH; i++) {
				final BlockPos checkPos = platformPos.offset(Direction.DOWN, i + 1);
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