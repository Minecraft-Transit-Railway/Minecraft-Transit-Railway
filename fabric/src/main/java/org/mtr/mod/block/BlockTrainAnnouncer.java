package org.mtr.mod.block;

import org.mtr.core.tool.Utilities;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.Long2ObjectAVLTreeMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mapping.mapper.SoundHelper;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mod.BlockEntityTypes;
import org.mtr.mod.client.IDrawing;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.stream.Collectors;

public class BlockTrainAnnouncer extends BlockTrainSensorBase {

	private static final Long2ObjectAVLTreeMap<ObjectArrayList<Runnable>> QUEUE = new Long2ObjectAVLTreeMap<>();

	@Nonnull
	@Override
	public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new BlockEntity(blockPos, blockState);
	}

	public static void processQueue() {
		final LongArrayList itemsToRemove = new LongArrayList();
		final long currentMillis = System.currentTimeMillis();
		QUEUE.forEach((time, tasks) -> {
			if (time <= currentMillis) {
				tasks.forEach(Runnable::run);
				itemsToRemove.add(time.longValue());
			}
		});
		itemsToRemove.forEach(QUEUE::remove);
	}

	public static class BlockEntity extends BlockEntityBase implements Utilities {

		private String message = "";
		private String soundId = "";
		private int delay;
		private long lastAnnouncedMillis;
		private static final int ANNOUNCE_COOLDOWN_MILLIS = 20000;
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
			delay = compoundTag.getInt(KEY_DELAY);
			super.readCompoundTag(compoundTag);
		}

		@Override
		public void writeCompoundTag(CompoundTag compoundTag) {
			compoundTag.putString(KEY_MESSAGE, message);
			compoundTag.putString(KEY_SOUND_ID, soundId);
			compoundTag.putInt(KEY_DELAY, delay);
			super.writeCompoundTag(compoundTag);
		}

		public void setData(LongAVLTreeSet filterRouteIds, boolean stoppedOnly, boolean movingOnly, String message, String soundId, int delay) {
			this.message = message;
			this.soundId = soundId;
			this.delay = delay;
			setData(filterRouteIds, stoppedOnly, movingOnly);
		}

		public String getMessage() {
			return message;
		}

		public String getSoundId() {
			return soundId;
		}

		public int getDelay() {
			return delay;
		}

		public void announce() {
			final long currentMillis = System.currentTimeMillis();
			if (currentMillis - lastAnnouncedMillis >= ANNOUNCE_COOLDOWN_MILLIS) {
				final ObjectArrayList<Runnable> tasks = new ObjectArrayList<>();
				QUEUE.put(currentMillis + (long) delay * MILLIS_PER_SECOND, tasks);
				if (!message.isEmpty()) {
					tasks.add(() -> IDrawing.narrateOrAnnounce(Utilities.formatName(message), Arrays.stream(message.split("\\|")).map(TextHelper::literal).collect(Collectors.toCollection(ObjectArrayList::new))));
				}
				if (!soundId.isEmpty()) {
					tasks.add(() -> {
						final ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().getPlayerMapped();
						if (clientPlayerEntity != null) {
							clientPlayerEntity.playSound(SoundHelper.createSoundEvent(new Identifier(soundId)), 1000, 1);
						}
					});
				}
				lastAnnouncedMillis = currentMillis;
			}
		}
	}
}
