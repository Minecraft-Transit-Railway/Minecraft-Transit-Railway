package mtr.block;

import mtr.BlockEntityTypes;
import mtr.mappings.BlockEntityMapper;
import mtr.packet.PacketTrainDataGuiServer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BlockTrainAnnouncer extends BlockTrainSensorBase {

	@Override
	public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntityTrainAnnouncer(pos, state);
	}

	public static class TileEntityTrainAnnouncer extends TileEntityTrainSensorBase {

		private String message = "";
		private ResourceLocation soundId;
		private final Map<Player, Long> lastAnnouncedMillis = new HashMap<>();
		private static final int ANNOUNCE_COOL_DOWN_MILLIS = 20000;
		private static final String KEY_MESSAGE = "message";
		private static final String KEY_SOUND_ID = "sound_id";

		public TileEntityTrainAnnouncer(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.TRAIN_ANNOUNCER_TILE_ENTITY.get(), pos, state);
		}

		@Override
		public void readCompoundTag(CompoundTag compoundTag) {
			message = compoundTag.getString(KEY_MESSAGE);
			final String soundIdString = compoundTag.getString(KEY_SOUND_ID);
			soundId = soundIdString.isEmpty() ? null : new ResourceLocation(soundIdString);
			super.readCompoundTag(compoundTag);
		}

		@Override
		public void writeCompoundTag(CompoundTag compoundTag) {
			compoundTag.putString(KEY_MESSAGE, message);
			compoundTag.putString(KEY_SOUND_ID, getSoundIdString());
			super.writeCompoundTag(compoundTag);
		}

		@Override
		public void setData(Set<Long> filterRouteIds, boolean stoppedOnly, boolean movingOnly, int number, String... strings) {
			if (strings.length >= 2) {
				message = strings[0];
				final String soundIdString = strings[1];
				soundId = soundIdString.isEmpty() ? null : new ResourceLocation(soundIdString);
			}
			setData(filterRouteIds, stoppedOnly, movingOnly);
		}

		public String getMessage() {
			return message;
		}

		public String getSoundIdString() {
			return soundId == null ? "" : soundId.toString();
		}

		public void announce(Player player) {
			final long currentMillis = System.currentTimeMillis();
			if (player != null && (!lastAnnouncedMillis.containsKey(player) || currentMillis - lastAnnouncedMillis.get(player) >= ANNOUNCE_COOL_DOWN_MILLIS)) {
				lastAnnouncedMillis.put(player, System.currentTimeMillis());
				PacketTrainDataGuiServer.announceS2C((ServerPlayer) player, message, soundId);
			}
		}
	}
}
