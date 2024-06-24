package org.mtr.mod.block;

import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mod.BlockEntityTypes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class BlockTrainAnnouncer extends BlockTrainSensorBase {

	@Nonnull
	@Override
	public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new BlockEntity(blockPos, blockState);
	}

	public static class BlockEntity extends BlockEntityBase {

		private String message = "";
		private Identifier soundId;
		private final Map<PlayerEntity, Long> lastAnnouncedMillis = new HashMap<>();
		private static final int ANNOUNCE_COOL_DOWN_MILLIS = 20000;
		private static final String KEY_MESSAGE = "message";
		private static final String KEY_SOUND_ID = "sound_id";

		public BlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.TRAIN_ANNOUNCER.get(), pos, state);
		}

		@Override
		public void readCompoundTag(CompoundTag compoundTag) {
			message = compoundTag.getString(KEY_MESSAGE);
			final String soundIdString = compoundTag.getString(KEY_SOUND_ID);
			soundId = soundIdString.isEmpty() ? null : new Identifier(soundIdString);
			super.readCompoundTag(compoundTag);
		}

		@Override
		public void writeCompoundTag(CompoundTag compoundTag) {
			compoundTag.putString(KEY_MESSAGE, message);
			compoundTag.putString(KEY_SOUND_ID, getSoundIdString());
			super.writeCompoundTag(compoundTag);
		}

		@Override
		public void setData(LongAVLTreeSet filterRouteIds, boolean stoppedOnly, boolean movingOnly, int number, String... strings) {
			if (strings.length >= 2) {
				message = strings[0];
				final String soundIdString = strings[1];
				soundId = soundIdString.isEmpty() ? null : new Identifier(soundIdString);
			}
			setData(filterRouteIds, stoppedOnly, movingOnly);
		}

		public String getMessage() {
			return message;
		}

		public String getSoundIdString() {
			return soundId == null ? "" : soundId.toString();
		}

		public void announce(@Nullable PlayerEntity player) {
			final long currentMillis = System.currentTimeMillis();
			if (player != null && (!lastAnnouncedMillis.containsKey(player) || currentMillis - lastAnnouncedMillis.get(player) >= ANNOUNCE_COOL_DOWN_MILLIS)) {
				lastAnnouncedMillis.put(player, System.currentTimeMillis());
				// TODO
			}
		}
	}
}
