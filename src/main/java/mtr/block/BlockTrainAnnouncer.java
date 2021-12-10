package mtr.block;

import mtr.MTR;
import mtr.packet.PacketTrainDataGuiServer;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BlockTrainAnnouncer extends BlockTrainSensorBase {

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntityTrainAnnouncer(pos, state);
	}

	public static class TileEntityTrainAnnouncer extends TileEntityTrainSensorBase {

		private String message = "";
		private final Map<PlayerEntity, Long> lastAnnouncedMillis = new HashMap<>();
		private static final int ANNOUNCE_COOL_DOWN_MILLIS = 5000;
		private static final String KEY_MESSAGE = "message";

		public TileEntityTrainAnnouncer(BlockPos pos, BlockState state) {
			super(MTR.TRAIN_ANNOUNCER_TILE_ENTITY, pos, state);
		}

		@Override
		public void readNbtCompound(NbtCompound nbtCompound) {
			message = nbtCompound.getString(KEY_MESSAGE);
			super.readNbtCompound(nbtCompound);
		}

		@Override
		public void writeNbtCompound(NbtCompound nbtCompound) {
			nbtCompound.putString(KEY_MESSAGE, message);
			super.writeNbtCompound(nbtCompound);
		}

		@Override
		public void setData(Set<Long> filterRouteIds, int number, String string) {
			message = string;
			setData(filterRouteIds);
		}

		public String getMessage() {
			return message;
		}

		public void announce(PlayerEntity player) {
			final long currentMillis = System.currentTimeMillis();
			if (player != null && (!lastAnnouncedMillis.containsKey(player) || currentMillis - lastAnnouncedMillis.get(player) >= ANNOUNCE_COOL_DOWN_MILLIS)) {
				lastAnnouncedMillis.put(player, System.currentTimeMillis());
				PacketTrainDataGuiServer.announceS2C((ServerPlayerEntity) player, message);
			}
		}
	}
}
