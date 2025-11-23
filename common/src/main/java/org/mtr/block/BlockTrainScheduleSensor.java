package org.mtr.block;

import it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import lombok.Getter;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.mtr.MTRClient;
import org.mtr.core.operation.ArrivalResponse;
import org.mtr.data.ArrivalsCacheClient;
import org.mtr.packet.PacketTurnOnBlockEntity;
import org.mtr.registry.BlockEntityTypes;
import org.mtr.registry.RegistryClient;

import javax.annotation.Nonnull;

public class BlockTrainScheduleSensor extends BlockTrainPoweredSensorBase {

	public BlockTrainScheduleSensor(AbstractBlock.Settings settings) {
		super(settings);
	}

	@Nonnull
	@Override
	public BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new TrainScheduleSensorBlockEntity(blockPos, blockState);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return type == BlockEntityTypes.TRAIN_SCHEDULE_SENSOR.get() && world.isClient ? (world1, pos, state1, blockEntity) -> MTRClient.findClosePlatform(pos.up(), 5, platform -> {
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
		protected void readNbt(NbtCompound nbtCompound) {
			seconds = nbtCompound.getInt(KEY_SECONDS);
			realtimeOnly = nbtCompound.getBoolean(KEY_REALTIME_ONLY);
		}

		@Override
		protected void writeNbt(NbtCompound nbtCompound) {
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
