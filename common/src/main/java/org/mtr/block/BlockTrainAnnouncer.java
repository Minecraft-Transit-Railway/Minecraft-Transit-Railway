package org.mtr.block;

import it.unimi.dsi.fastutil.longs.Long2ObjectAVLTreeMap;
import it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.mtr.client.IDrawing;
import org.mtr.core.tool.Utilities;
import org.mtr.registry.BlockEntityTypes;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.stream.Collectors;

public class BlockTrainAnnouncer extends BlockTrainSensorBase {

	private static final Long2ObjectAVLTreeMap<ObjectArrayList<Runnable>> QUEUE = new Long2ObjectAVLTreeMap<>();

	public BlockTrainAnnouncer(AbstractBlock.Settings settings) {
		super(settings);
	}

	@Nonnull
	@Override
	public BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new TrainAnnouncerBlockEntity(blockPos, blockState);
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

	public static class TrainAnnouncerBlockEntity extends BlockEntityBase implements Utilities {

		private String message = "";
		private String soundId = "";
		private int delay;
		private long lastAnnouncedMillis;
		private static final int ANNOUNCE_COOLDOWN_MILLIS = 20000;
		private static final String KEY_MESSAGE = "message";
		private static final String KEY_SOUND_ID = "sound_id";
		private static final String KEY_DELAY = "delay";

		public TrainAnnouncerBlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.TRAIN_ANNOUNCER.get(), pos, state);
		}

		@Override
		protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
			message = nbt.getString(KEY_MESSAGE);
			soundId = nbt.getString(KEY_SOUND_ID);
			delay = nbt.getInt(KEY_DELAY);
			super.readNbt(nbt, registries);
		}

		@Override
		protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
			nbt.putString(KEY_MESSAGE, message);
			nbt.putString(KEY_SOUND_ID, soundId);
			nbt.putInt(KEY_DELAY, delay);
			super.writeNbt(nbt, registries);
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
					tasks.add(() -> IDrawing.narrateOrAnnounce(Utilities.formatName(message), Arrays.stream(message.split("\\|")).map(Text::literal).collect(Collectors.toCollection(ObjectArrayList::new))));
				}
				if (!soundId.isEmpty()) {
					tasks.add(() -> {
						final ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().player;
						if (clientPlayerEntity != null) {
							clientPlayerEntity.playSound(SoundEvent.of(Identifier.of(soundId)), 1000, 1);
						}
					});
				}
				lastAnnouncedMillis = currentMillis;
			}
		}
	}
}
