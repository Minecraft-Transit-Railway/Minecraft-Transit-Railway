package org.mtr.block;

import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.ticks.ScheduledTick;
import org.jspecify.annotations.Nullable;
import org.mtr.MTR;
import org.mtr.core.data.Depot;
import org.mtr.core.operation.NearbyAreasRequest;
import org.mtr.core.operation.NearbyAreasResponse;
import org.mtr.core.servlet.OperationProcessor;
import org.mtr.core.tool.Utilities;
import org.mtr.item.ItemDepotDriverKey;
import org.mtr.packet.PacketOpenBlockEntityScreen;
import org.mtr.registry.BlockEntityTypes;
import org.mtr.registry.Items;

//? if >= 1.21.4 {
import net.minecraft.world.level.redstone.Orientation;
//? }

public class BlockDriverKeyDispenser extends BlockWaterloggable implements EntityBlock {

	public static final BooleanProperty TRIGGERED = BooleanProperty.create("triggered");

	public BlockDriverKeyDispenser(BlockBehaviour.Properties blockSettings) {
		super(blockSettings.noOcclusion());
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
		return IBlock.checkHoldingBrush(world, player, () -> PacketOpenBlockEntityScreen.sendDirectlyToServer((ServerLevel) world, (ServerPlayer) player, pos));
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext itemPlacementContext) {
		return super.getStateForPlacement(itemPlacementContext).setValue(BlockStateProperties.HORIZONTAL_FACING, itemPlacementContext.getHorizontalDirection());
	}

	@Override
//? if >= 1.21.4 {
	protected void neighborChanged(BlockState state, Level world, BlockPos pos, Block sourceBlock, @Nullable Orientation wireOrientation, boolean notify) {
//? } else {
  /*public void neighborChanged(BlockState state, Level world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
//
*///? }
		final boolean hasPower = isReceivingRedstonePower(world, pos) || isReceivingRedstonePower(world, pos.above());
		final boolean isTriggered = state.getValue(TRIGGERED);
		if (hasPower && !isTriggered) {
			world.getBlockTicks().schedule(new ScheduledTick<>(this, pos, 4, 0));
			world.setBlock(pos, state.setValue(TRIGGERED, true), 2);
		} else if (!hasPower && isTriggered) {
			world.setBlock(pos, state.setValue(TRIGGERED, false), 2);
		}
	}

	@Override
	protected void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		final BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof DriverKeyDispenserBlockEntity driverKeyDispenserBlockEntity) {
			driverKeyDispenserBlockEntity.dispense(state.getValue(BlockStateProperties.HORIZONTAL_FACING));
		}
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(BlockStateProperties.HORIZONTAL_FACING, TRIGGERED);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new DriverKeyDispenserBlockEntity(blockPos, blockState);
	}

	/**
	 * See {@link net.minecraft.world.level.SignalGetter#hasNeighborSignal(BlockPos)}
	 */
	private static boolean isReceivingRedstonePower(Level world, BlockPos pos) {
		if (world.hasSignal(pos.below(), Direction.DOWN)) {
			return true;
		} else if (world.hasSignal(pos.above(), Direction.UP)) {
			return true;
		} else if (world.hasSignal(pos.north(), Direction.NORTH)) {
			return true;
		} else if (world.hasSignal(pos.south(), Direction.SOUTH)) {
			return true;
		} else if (world.hasSignal(pos.west(), Direction.WEST)) {
			return true;
		} else {
			return world.hasSignal(pos.east(), Direction.EAST);
		}
	}

	public static class DriverKeyDispenserBlockEntity extends BlockEntityExtension {

		private boolean dispenseBasicDriverKey = false;
		private boolean dispenseAdvancedDriverKey = false;
		private boolean dispenseGuardKey = false;
		@Getter
		private long timeout = Utilities.MILLIS_PER_HOUR;

		private static final String KEY_DISPENSE_BASIC_DRIVER_KEY = "dispense_basic_driver_key";
		private static final String KEY_DISPENSE_ADVANCED_DRIVER_KEY = "dispense_advanced_driver_key";
		private static final String KEY_DISPENSE_GUARD_KEY = "dispense_gaurd_key";
		private static final String KEY_TIMEOUT = "timeout";

		public DriverKeyDispenserBlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.DRIVER_KEY_DISPENSER.get(), pos, state);
		}

		@Override
		protected void readNbt(CompoundTag nbtCompound) {
			dispenseBasicDriverKey = nbtCompound.getBoolean(KEY_DISPENSE_BASIC_DRIVER_KEY);
			dispenseAdvancedDriverKey = nbtCompound.getBoolean(KEY_DISPENSE_ADVANCED_DRIVER_KEY);
			dispenseGuardKey = nbtCompound.getBoolean(KEY_DISPENSE_GUARD_KEY);
			timeout = nbtCompound.getLong(KEY_TIMEOUT);
		}

		@Override
		protected void writeNbt(CompoundTag nbtCompound) {
			nbtCompound.putBoolean(KEY_DISPENSE_BASIC_DRIVER_KEY, dispenseBasicDriverKey);
			nbtCompound.putBoolean(KEY_DISPENSE_ADVANCED_DRIVER_KEY, dispenseAdvancedDriverKey);
			nbtCompound.putBoolean(KEY_DISPENSE_GUARD_KEY, dispenseGuardKey);
			nbtCompound.putLong(KEY_TIMEOUT, timeout);
		}

		public void setData(boolean dispenseBasicDriverKey, boolean dispenseAdvancedDriverKey, boolean dispenseGuardKey, long timeout) {
			this.dispenseBasicDriverKey = dispenseBasicDriverKey;
			this.dispenseAdvancedDriverKey = dispenseAdvancedDriverKey;
			this.dispenseGuardKey = dispenseGuardKey;
			this.timeout = timeout;
			setChanged();
		}

		public boolean getDispenseBasicDriverKey() {
			return dispenseBasicDriverKey;
		}

		public boolean getDispenseAdvancedDriverKey() {
			return dispenseAdvancedDriverKey;
		}

		public boolean getDispenseGuardKey() {
			return dispenseGuardKey;
		}

		private void dispense(Direction direction) {
			final Level world = getLevel();
			if (world != null && !world.isClientSide()) {
				MTR.sendMessageC2S(OperationProcessor.NEARBY_DEPOTS, world.getServer(), world, new NearbyAreasRequest<>(MTR.blockPosToPosition(getBlockPos()), 0), nearbyAreasResponse -> {
					for (final Depot depot : nearbyAreasResponse.getDepots()) {
						if (dispenseBasicDriverKey) {
							spawnItemStack(world, depot, Items.BASIC_DRIVER_KEY.get().getDefaultInstance(), direction);
						}

						if (dispenseAdvancedDriverKey) {
							spawnItemStack(world, depot, Items.ADVANCED_DRIVER_KEY.get().getDefaultInstance(), direction);
						}

						if (dispenseGuardKey) {
							spawnItemStack(world, depot, Items.GUARD_KEY.get().getDefaultInstance(), direction);
						}

						break;
					}
				}, NearbyAreasResponse.class);
			}
		}

		private void spawnItemStack(Level world, Depot depot, ItemStack itemStack, Direction direction) {
			ItemDepotDriverKey.setData(itemStack, depot, timeout);
			itemStack.set(DataComponents.CUSTOM_NAME, Component.literal(String.format("%s (%s)", itemStack.getHoverName().getString(), depot.getName())));
			final BlockPos pos = getBlockPos().relative(direction);
			world.addFreshEntity(new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, itemStack));
		}
	}
}
