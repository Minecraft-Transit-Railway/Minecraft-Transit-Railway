package org.mtr.mod.block;

import org.mtr.core.data.Depot;
import org.mtr.core.operation.NearbyAreasRequest;
import org.mtr.core.operation.NearbyAreasResponse;
import org.mtr.core.servlet.OperationProcessor;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.*;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.BlockEntityTypes;
import org.mtr.mod.Init;
import org.mtr.mod.Items;
import org.mtr.mod.item.ItemDepotDriverKey;
import org.mtr.mod.packet.PacketOpenBlockEntityScreen;

import javax.annotation.Nonnull;
import java.util.List;

public class BlockDriverKeyDispenser extends BlockWaterloggable implements DirectionHelper, BlockWithEntity {

	public static final BooleanProperty TRIGGERED = BooleanProperty.of("triggered");

	public BlockDriverKeyDispenser(BlockSettings blockSettings) {
		super(blockSettings.nonOpaque());
	}

	@Nonnull
	@Override
	public ActionResult onUse2(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		return IBlock.checkHoldingBrush(world, player, () -> {
			final org.mtr.mapping.holder.BlockEntity entity = world.getBlockEntity(pos);
			if (entity != null && entity.data instanceof BlockEntity) {
				((BlockEntity) entity.data).markDirty2();
				Init.REGISTRY.sendPacketToClient(ServerPlayerEntity.cast(player), new PacketOpenBlockEntityScreen(pos));
			}
		});
	}

	@Nonnull
	@Override
	public BlockState getPlacementState2(ItemPlacementContext itemPlacementContext) {
		final Direction facing = itemPlacementContext.getPlayerFacing().getOpposite();
		return super.getPlacementState2(itemPlacementContext).with(new Property<>(FACING.data), facing.data);
	}

	@Override
	public void neighborUpdate2(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
		final boolean hasPower = isReceivingRedstonePower(world, pos) || isReceivingRedstonePower(world, pos.up());
		final boolean isTriggered = state.get(new Property<>(TRIGGERED.data));
		if (hasPower && !isTriggered) {
			scheduleBlockTick(world, pos, new Block(this), 4);
			world.setBlockState(pos, state.with(new Property<>(TRIGGERED.data), true), 2);
		} else if (!hasPower && isTriggered) {
			world.setBlockState(pos, state.with(new Property<>(TRIGGERED.data), false), 2);
		}
	}

	@Override
	public void scheduledTick2(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		final org.mtr.mapping.holder.BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity != null && blockEntity.data instanceof BlockEntity) {
			((BlockEntity) blockEntity.data).dispense(Direction.convert(state.get(new Property<>(FACING.data))));
		}
	}

	@Override
	public void addBlockProperties(List<HolderBase<?>> properties) {
		super.addBlockProperties(properties);
		properties.add(FACING);
		properties.add(TRIGGERED);
	}

	@Nonnull
	@Override
	public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new BlockEntity(blockPos, blockState);
	}

	/**
	 * See {@link net.minecraft.world.RedstoneView#isReceivingRedstonePower(net.minecraft.util.math.BlockPos)}
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

	public static class BlockEntity extends BlockEntityExtension {

		private boolean dispenseBasicDriverKey = false;
		private boolean dispenseAdvancedDriverKey = false;
		private boolean dispenseGuardKey = false;
		private long timeout = Depot.MILLIS_PER_HOUR;

		private static final String KEY_DISPENSE_BASIC_DRIVER_KEY = "dispense_basic_driver_key";
		private static final String KEY_DISPENSE_ADVANCED_DRIVER_KEY = "dispense_advanced_driver_key";
		private static final String KEY_DISPENSE_GUARD_KEY = "dispense_gaurd_key";
		private static final String KEY_TIMEOUT = "timeout";

		public BlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.DRIVER_KEY_DISPENSER.get(), pos, state);
		}

		@Override
		public void readCompoundTag(CompoundTag compoundTag) {
			dispenseBasicDriverKey = compoundTag.getBoolean(KEY_DISPENSE_BASIC_DRIVER_KEY);
			dispenseAdvancedDriverKey = compoundTag.getBoolean(KEY_DISPENSE_ADVANCED_DRIVER_KEY);
			dispenseGuardKey = compoundTag.getBoolean(KEY_DISPENSE_GUARD_KEY);
			timeout = compoundTag.getLong(KEY_TIMEOUT);
			super.readCompoundTag(compoundTag);
		}

		@Override
		public void writeCompoundTag(CompoundTag compoundTag) {
			compoundTag.putBoolean(KEY_DISPENSE_BASIC_DRIVER_KEY, dispenseBasicDriverKey);
			compoundTag.putBoolean(KEY_DISPENSE_ADVANCED_DRIVER_KEY, dispenseAdvancedDriverKey);
			compoundTag.putBoolean(KEY_DISPENSE_GUARD_KEY, dispenseGuardKey);
			compoundTag.putLong(KEY_TIMEOUT, timeout);
			super.writeCompoundTag(compoundTag);
		}

		public void setData(boolean dispenseBasicDriverKey, boolean dispenseAdvancedDriverKey, boolean dispenseGuardKey, long timeout) {
			this.dispenseBasicDriverKey = dispenseBasicDriverKey;
			this.dispenseAdvancedDriverKey = dispenseAdvancedDriverKey;
			this.dispenseGuardKey = dispenseGuardKey;
			this.timeout = timeout;
			markDirty2();
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
			final World world = getWorld2();
			if (world != null && !world.isClient()) {
				Init.sendMessageC2S(OperationProcessor.NEARBY_DEPOTS, world.getServer(), world, new NearbyAreasRequest<>(Init.blockPosToPosition(getPos2()), 0), nearbyAreasResponse -> {
					for (final Depot depot : nearbyAreasResponse.getDepots()) {
						if (dispenseBasicDriverKey) {
							spawnItemStack(world, depot, Items.BASIC_DRIVER_KEY.get().getDefaultStack(), direction);
						}

						if (dispenseAdvancedDriverKey) {
							spawnItemStack(world, depot, Items.ADVANCED_DRIVER_KEY.get().getDefaultStack(), direction);
						}

						if (dispenseGuardKey) {
							spawnItemStack(world, depot, Items.GUARD_KEY.get().getDefaultStack(), direction);
						}

						break;
					}
				}, NearbyAreasResponse.class);
			}
		}

		private void spawnItemStack(World world, Depot depot, ItemStack itemStack, Direction direction) {
			ItemDepotDriverKey.setData(itemStack, depot, timeout);
			itemStack.setCustomName(new Text(TextHelper.literal(String.format("%s (%s)", itemStack.getItem().getName().getString(), depot.getName())).data));
			EntityHelper.spawnItem(ServerWorld.cast(world), getPos2().offset(direction), itemStack);
		}
	}
}
