package org.mtr.mod.block;

import org.mtr.core.operation.ArrivalResponse;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.BlockState;
import org.mtr.mapping.holder.CompoundTag;
import org.mtr.mapping.holder.World;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mod.BlockEntityTypes;
import org.mtr.mod.InitClient;
import org.mtr.mod.data.ArrivalsCacheClient;
import org.mtr.mod.packet.PacketTurnOnBlockEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockTrainScheduleSensor extends BlockTrainPoweredSensorBase {

	public BlockTrainScheduleSensor() {
		super();
	}

	@Nonnull
	@Override
	public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new BlockEntity(blockPos, blockState);
	}

	public static class BlockEntity extends BlockEntityBase {

		private int seconds = 10;
		private boolean realtimeOnly = false;
		private static final String KEY_SECONDS = "seconds";
		private static final String KEY_REALTIME_ONLY = "realtime_only";

		public BlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.TRAIN_SCHEDULE_SENSOR.get(), pos, state);
		}

		@Override
		public void blockEntityTick() {
			tick(getWorld2(), getPos2(), this);
		}

		@Override
		public void readCompoundTag(CompoundTag compoundTag) {
			seconds = compoundTag.getInt(KEY_SECONDS);
			realtimeOnly = compoundTag.getBoolean(KEY_REALTIME_ONLY);
			super.readCompoundTag(compoundTag);
		}

		@Override
		public void writeCompoundTag(CompoundTag compoundTag) {
			compoundTag.putInt(KEY_SECONDS, seconds);
			compoundTag.putBoolean(KEY_REALTIME_ONLY, realtimeOnly);
			super.writeCompoundTag(compoundTag);
		}

		public void setData(LongAVLTreeSet filterRouteIds, boolean stoppedOnly, boolean movingOnly, int seconds, boolean realtimeOnly) {
			this.seconds = seconds;
			this.realtimeOnly = realtimeOnly;
			setData(filterRouteIds, stoppedOnly, movingOnly);
		}

		public int getSeconds() {
			return seconds;
		}

		public boolean getRealtimeOnly() {
			return realtimeOnly;
		}

		public static <T extends BlockEntityExtension> void tick(@Nullable World world, BlockPos pos, T blockEntity) {
			if (world != null && world.isClient() && blockEntity instanceof BlockEntity) {
				InitClient.findClosePlatform(pos.up(), 5, platform -> {
					final ObjectArrayList<ArrivalResponse> arrivalResponseList = ArrivalsCacheClient.INSTANCE.requestArrivals(LongArrayList.of(platform.getId()));
					for (final ArrivalResponse arrival : arrivalResponseList) {
						if ((!((BlockEntity) blockEntity).realtimeOnly || arrival.getRealtime()) && BlockTrainSensorBase.matchesFilter(world, pos, arrival.getRouteId(), 1) && (arrival.getArrival() - ArrivalsCacheClient.INSTANCE.getMillisOffset() - System.currentTimeMillis()) / 1000 == ((BlockEntity) blockEntity).seconds) {
							InitClient.REGISTRY_CLIENT.sendPacketToServer(new PacketTurnOnBlockEntity(pos));
							break;
						}
					}
				});
			}
		}
	}
}
