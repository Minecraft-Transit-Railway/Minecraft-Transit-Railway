package org.mtr.mod.block;

import it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.BlockEntityTypes;

import javax.annotation.Nullable;
import java.util.List;

public class BlockTrainScheduleSensor extends BlockTrainPoweredSensorBase {

	public BlockTrainScheduleSensor() {
		super();
	}

	@Override
	public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new BlockEntity(blockPos, blockState);
	}

	@Override
	public void addBlockProperties(List<HolderBase<?>> properties) {
		properties.add(POWERED);
	}

	public static class BlockEntity extends BlockEntityBase {

		private int seconds = 10;
		private static final String KEY_SECONDS = "seconds";

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
			super.readCompoundTag(compoundTag);
		}

		@Override
		public void writeCompoundTag(CompoundTag compoundTag) {
			compoundTag.putInt(KEY_SECONDS, seconds);
			super.writeCompoundTag(compoundTag);
		}

		@Override
		public void setData(LongAVLTreeSet filterRouteIds, boolean stoppedOnly, boolean movingOnly, int number, String... strings) {
			seconds = number;
			setData(filterRouteIds, stoppedOnly, movingOnly);
		}

		public int getSeconds() {
			return seconds;
		}

		public static <T extends BlockEntityExtension> void tick(@Nullable World world, BlockPos pos, T blockEntity) {
			if (world != null && !world.isClient()) {
				final BlockState state = world.getBlockState(pos);
				final Block block = state.getBlock();
				final boolean isActive = IBlock.getStatePropertySafe(state, POWERED) > 1 && hasScheduledTick(world, pos, block);

				if (isActive || !(block.data instanceof BlockTrainScheduleSensor) || !(blockEntity instanceof BlockEntity)) {
					return;
				}

				// TODO
			}
		}
	}
}
