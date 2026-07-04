package org.mtr.block;

import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;
import org.mtr.MTRClient;
import org.mtr.core.operation.ArrivalResponse;
import org.mtr.data.ArrivalsCacheClient;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.packet.PacketTurnOnBlockEntity;
import org.mtr.registry.BlockEntityTypes;
import org.mtr.registry.RegistryClient;

public class BlockTrainScheduleSensor extends BlockTrainPoweredSensorBase {

	public BlockTrainScheduleSensor(BlockBehaviour.Properties settings) {
		super(settings);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new TrainScheduleSensorBlockEntity(blockPos, blockState);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
		return type == BlockEntityTypes.TRAIN_SCHEDULE_SENSOR.get() && world.isClientSide ? (world1, pos, state1, blockEntity) -> MTRClient.findClosePlatform(pos.above(), 5, platform -> {
			final ObjectArrayList<ArrivalResponse> arrivalResponseList = ArrivalsCacheClient.INSTANCE.requestArrivals(LongArrayList.of(platform.getId()));
			for (final ArrivalResponse arrival : arrivalResponseList) {
				if ((!((TrainScheduleSensorBlockEntity) blockEntity).realtimeOnly || arrival.getRealtime()) && BlockTrainSensorBase.matchesFilter(world1, pos, arrival.getRouteId(), 1) && (arrival.getArrival() - ArrivalsCacheClient.INSTANCE.getMillisOffset() - System.currentTimeMillis()) / 1000 == ((TrainScheduleSensorBlockEntity) blockEntity).seconds) {
					RegistryClient.sendPacketToServer(new PacketTurnOnBlockEntity(pos));
					break;
				}
			}
		}) : null;
	}

	public static class TrainScheduleSensorBlockEntity extends BlockEntityBase {

		@Getter
		private int seconds = 10;
		private boolean realtimeOnly = false;
		private static final String KEY_SECONDS = "seconds";
		private static final String KEY_REALTIME_ONLY = "realtime_only";

		public TrainScheduleSensorBlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.TRAIN_SCHEDULE_SENSOR.get(), pos, state);
		}

		@Override
		protected void readNbt(CompoundTag nbtCompound) {
			seconds = nbtCompound.getInt(KEY_SECONDS);
			realtimeOnly = nbtCompound.getBoolean(KEY_REALTIME_ONLY);
		}

		@Override
		protected void writeNbt(CompoundTag nbtCompound) {
			nbtCompound.putInt(KEY_SECONDS, seconds);
			nbtCompound.putBoolean(KEY_REALTIME_ONLY, realtimeOnly);
		}

		public void setData(LongAVLTreeSet filterRouteIds, boolean stoppedOnly, boolean movingOnly, int seconds, boolean realtimeOnly) {
			this.seconds = seconds;
			this.realtimeOnly = realtimeOnly;
			setData(filterRouteIds, stoppedOnly, movingOnly);
		}

		public boolean getRealtimeOnly() {
			return realtimeOnly;
		}
	}
}
