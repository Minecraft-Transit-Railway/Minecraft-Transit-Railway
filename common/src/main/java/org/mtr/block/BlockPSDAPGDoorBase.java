package org.mtr.block;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
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
import org.mtr.core.tool.Utilities;
import org.mtr.data.IGui;
import org.mtr.generated.lang.TranslationProvider;

import javax.annotation.Nonnull;

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
		builder.add(Properties.FACING);
		builder.add(HALF);
		builder.add(SIDE);
		builder.add(UNLOCKED);
	}

	private static void lockDoor(World world, BlockPos pos, BlockState state, boolean unlocked) {
		final Direction facing = IBlock.getStatePropertySafe(state, Properties.FACING);
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

	public static abstract class BlockEntityBase extends BlockEntity implements IGui {

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
			final World world = getWorld();
			final double delta = (tickDelta / 20) / 2;

			if (world != null && receivedRedstonePower(world, getPos(), getCachedState())) {
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
			final Direction facing = IBlock.getStatePropertySafe(state, Properties.FACING);
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