package org.mtr.mod.block;

import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.BlockState;
import org.mtr.mapping.holder.CompoundTag;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mod.BlockEntityTypes;
import org.mtr.mod.InitClient;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class BlockTrainAnnouncer extends BlockTrainSensorBase {

	public static final Map<Long, Runnable> DELAYED_TASKS = new HashMap<>();

	@Nonnull
	@Override
	public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new BlockEntity(blockPos, blockState);
	}

	public static class BlockEntity extends BlockEntityBase {

		private String message = "";
		private String soundId = "";
		private String delay = "0";
		private long lastAnnouncedMillis;
		private long nextAnnouncementMillis;
		private static final int ANNOUNCE_COOL_DOWN_MILLIS = 20000;
		private static final String KEY_MESSAGE = "message";
		private static final String KEY_SOUND_ID = "sound_id";
		private static final String KEY_DELAY = "delay";

		public BlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.TRAIN_ANNOUNCER.get(), pos, state);
		}

		@Override
		public void readCompoundTag(CompoundTag compoundTag) {
			message = compoundTag.getString(KEY_MESSAGE);
			soundId = compoundTag.getString(KEY_SOUND_ID);
			delay = compoundTag.getString(KEY_DELAY);
			super.readCompoundTag(compoundTag);
		}

		@Override
		public void writeCompoundTag(CompoundTag compoundTag) {
			compoundTag.putString(KEY_MESSAGE, message);
			compoundTag.putString(KEY_SOUND_ID, soundId);
			compoundTag.putString(KEY_DELAY, delay);
			super.writeCompoundTag(compoundTag);
		}

		public void setData(LongAVLTreeSet filterRouteIds, boolean stoppedOnly, boolean movingOnly, String message, String soundId, String delay) {
			this.message = message;
			this.soundId = soundId;
			this.delay = delay;
			this.nextAnnouncementMillis = System.currentTimeMillis() + getDelayInMillis(delay);
			setData(filterRouteIds, stoppedOnly, movingOnly);
		}

		public String getMessage() {
			return message;
		}

		public String getSoundId() {
			return soundId;
		}

		public String getDelay() {
			return delay;
		}

		private long getDelayInMillis(String delayStr) {
			try {
				int delayInt = Integer.parseInt(delayStr);
				return delayInt * 1000L;
			} catch (NumberFormatException e) {
				return 0;
			}
		}

		public void announce(Consumer<String> messageConsumer, Consumer<String> soundIdConsumer, double delayInSec) {
			long gameMillisNow = InitClient.getGameMillis();
			long delayMillis = (long) (delayInSec * 1000);
			DELAYED_TASKS.put(gameMillisNow + delayMillis, () -> {
				final long currentMillis = System.currentTimeMillis();
				if (currentMillis >= nextAnnouncementMillis) {
					if (!message.isEmpty()) {
						messageConsumer.accept(message);
					}
					if (!soundId.isEmpty()) {
						soundIdConsumer.accept(soundId);
					}
					lastAnnouncedMillis = currentMillis;
					nextAnnouncementMillis = currentMillis + getDelayInMillis(delay);
				}
			});
		}
	}
}
