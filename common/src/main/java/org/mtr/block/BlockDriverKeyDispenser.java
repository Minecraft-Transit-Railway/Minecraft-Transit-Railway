package org.mtr.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.block.WireOrientation;
import net.minecraft.world.tick.OrderedTick;
import org.jetbrains.annotations.Nullable;
import org.mtr.MTR;
import org.mtr.core.data.Depot;
import org.mtr.core.operation.NearbyAreasRequest;
import org.mtr.core.operation.NearbyAreasResponse;
import org.mtr.core.servlet.OperationProcessor;
import org.mtr.item.ItemDepotDriverKey;
import org.mtr.packet.PacketOpenBlockEntityScreen;
import org.mtr.registry.BlockEntityTypes;
import org.mtr.registry.Items;
import org.mtr.registry.Registry;

import javax.annotation.Nonnull;

public class BlockDriverKeyDispenser extends BlockWaterloggable implements BlockEntityProvider {

	public static final BooleanProperty TRIGGERED = BooleanProperty.of("triggered");

	public BlockDriverKeyDispenser(AbstractBlock.Settings blockSettings) {
		super(blockSettings.nonOpaque());
	}

	@Override
	protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
		return IBlock.checkHoldingBrush(world, player, () -> {
			final BlockEntity entity = world.getBlockEntity(pos);
			if (entity instanceof DriverKeyDispenserBlockEntity) {
				entity.markDirty();
				Registry.sendPacketToClient((ServerPlayerEntity) player, new PacketOpenBlockEntityScreen(pos));
			}
		});
	}

	@Nonnull
	@Override
	public BlockState getPlacementState(ItemPlacementContext itemPlacementContext) {
		return super.getPlacementState(itemPlacementContext).with(Properties.HORIZONTAL_FACING, itemPlacementContext.getHorizontalPlayerFacing());
	}

	@Override
	protected void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, @Nullable WireOrientation wireOrientation, boolean notify) {
		final boolean hasPower = isReceivingRedstonePower(world, pos) || isReceivingRedstonePower(world, pos.up());
		final boolean isTriggered = state.get(TRIGGERED);
		if (hasPower && !isTriggered) {
			world.getBlockTickScheduler().scheduleTick(new OrderedTick<>(this, pos, 4, 0));
			world.setBlockState(pos, state.with(TRIGGERED, true), 2);
		} else if (!hasPower && isTriggered) {
			world.setBlockState(pos, state.with(TRIGGERED, false), 2);
		}
	}

	@Override
	protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		final BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof DriverKeyDispenserBlockEntity driverKeyDispenserBlockEntity) {
			driverKeyDispenserBlockEntity.dispense(state.get(Properties.HORIZONTAL_FACING));
		}
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(Properties.HORIZONTAL_FACING, TRIGGERED);
	}

	@Nonnull
	@Override
	public BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new DriverKeyDispenserBlockEntity(blockPos, blockState);
	}

	/**
	 * See {@link net.minecraft.world.RedstoneView#isReceivingRedstonePower(BlockPos)}
	 */
	private static boolean isReceivingRedstonePower(World world, BlockPos pos) {
		if (world.isEmittingRedstonePower(pos.down(), Direction.DOWN)) {
			return true;
		} else if (world.isEmittingRedstonePower(pos.up(), Direction.UP)) {
			return true;
		} else if (world.isEmittingRedstonePower(pos.north(), Direction.NORTH)) {
			return true;
		} else if (world.isEmittingRedstonePower(pos.south(), Direction.SOUTH)) {
			return true;
		} else if (world.isEmittingRedstonePower(pos.west(), Direction.WEST)) {
			return true;
		} else {
			return world.isEmittingRedstonePower(pos.east(), Direction.EAST);
		}
	}

	public static class DriverKeyDispenserBlockEntity extends BlockEntity {

		private boolean dispenseBasicDriverKey = false;
		private boolean dispenseAdvancedDriverKey = false;
		private boolean dispenseGuardKey = false;
		private long timeout = Depot.MILLIS_PER_HOUR;

		private static final String KEY_DISPENSE_BASIC_DRIVER_KEY = "dispense_basic_driver_key";
		private static final String KEY_DISPENSE_ADVANCED_DRIVER_KEY = "dispense_advanced_driver_key";
		private static final String KEY_DISPENSE_GUARD_KEY = "dispense_gaurd_key";
		private static final String KEY_TIMEOUT = "timeout";

		public DriverKeyDispenserBlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.DRIVER_KEY_DISPENSER.get(), pos, state);
		}

		@Override
		protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
			dispenseBasicDriverKey = nbt.getBoolean(KEY_DISPENSE_BASIC_DRIVER_KEY);
			dispenseAdvancedDriverKey = nbt.getBoolean(KEY_DISPENSE_ADVANCED_DRIVER_KEY);
			dispenseGuardKey = nbt.getBoolean(KEY_DISPENSE_GUARD_KEY);
			timeout = nbt.getLong(KEY_TIMEOUT);
		}

		@Override
		protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
			nbt.putBoolean(KEY_DISPENSE_BASIC_DRIVER_KEY, dispenseBasicDriverKey);
			nbt.putBoolean(KEY_DISPENSE_ADVANCED_DRIVER_KEY, dispenseAdvancedDriverKey);
			nbt.putBoolean(KEY_DISPENSE_GUARD_KEY, dispenseGuardKey);
			nbt.putLong(KEY_TIMEOUT, timeout);
		}

		public void setData(boolean dispenseBasicDriverKey, boolean dispenseAdvancedDriverKey, boolean dispenseGuardKey, long timeout) {
			this.dispenseBasicDriverKey = dispenseBasicDriverKey;
			this.dispenseAdvancedDriverKey = dispenseAdvancedDriverKey;
			this.dispenseGuardKey = dispenseGuardKey;
			this.timeout = timeout;
			markDirty();
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

		public long getTimeout() {
			return timeout;
		}

		private void dispense(Direction direction) {
			final World world = getWorld();
			if (world != null && !world.isClient()) {
				MTR.sendMessageC2S(OperationProcessor.NEARBY_DEPOTS, world.getServer(), world, new NearbyAreasRequest<>(MTR.blockPosToPosition(getPos()), 0), nearbyAreasResponse -> {
					for (final Depot depot : nearbyAreasResponse.getDepots()) {
						if (dispenseBasicDriverKey) {
							spawnItemStack(world, depot, Items.BASIC_DRIVER_KEY.createAndGet().getDefaultStack(), direction);
						}

						if (dispenseAdvancedDriverKey) {
							spawnItemStack(world, depot, Items.ADVANCED_DRIVER_KEY.createAndGet().getDefaultStack(), direction);
						}

						if (dispenseGuardKey) {
							spawnItemStack(world, depot, Items.GUARD_KEY.createAndGet().getDefaultStack(), direction);
						}

						break;
					}
				}, NearbyAreasResponse.class);
			}
		}

		private void spawnItemStack(World world, Depot depot, ItemStack itemStack, Direction direction) {
			ItemDepotDriverKey.setData(itemStack, depot, timeout);
			itemStack.set(DataComponentTypes.CUSTOM_NAME, Text.literal(String.format("%s (%s)", itemStack.getItem().getName().getString(), depot.getName())));
			final BlockPos pos = getPos().offset(direction);
			world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, itemStack));
		}
	}
}
