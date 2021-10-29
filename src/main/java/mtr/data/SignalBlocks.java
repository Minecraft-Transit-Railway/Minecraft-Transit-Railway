package mtr.data;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.DyeColor;

import java.util.*;

public class SignalBlocks {

	public final Set<SignalBlock> signalBlocks = new HashSet<>();

	public void add(DyeColor color, UUID rail) {
		final List<SignalBlock> connectedSignalBlocks = new ArrayList<>();
		signalBlocks.forEach(signalBlock -> {
			if (signalBlock.color == color && signalBlock.isConnected(rail)) {
				connectedSignalBlocks.add(signalBlock);
			}
		});

		if (connectedSignalBlocks.isEmpty()) {
			signalBlocks.add(new SignalBlock(color, rail));
		} else {
			final SignalBlock firstSignalBlock = connectedSignalBlocks.remove(0);
			firstSignalBlock.rails.add(rail);
			connectedSignalBlocks.forEach(signalBlock -> firstSignalBlock.rails.addAll(signalBlock.rails));
			signalBlocks.removeIf(connectedSignalBlocks::contains);
		}
	}

	public void remove(DyeColor color, UUID rail) {
		final List<SignalBlock> connectedSignalBlocks = new ArrayList<>();
		signalBlocks.forEach(signalBlock -> {
			if (signalBlock.color == color && signalBlock.isConnected(rail)) {
				connectedSignalBlocks.add(signalBlock);
			}
		});

		signalBlocks.removeIf(connectedSignalBlocks::contains);
		connectedSignalBlocks.forEach(signalBlock -> {
			signalBlock.rails.remove(rail);
			signalBlock.rails.forEach(existingRail -> add(color, existingRail));
		});

		signalBlocks.removeIf(signalBlock -> signalBlock.rails.isEmpty());
	}

	public void occupy(UUID rail, Set<UUID> trainPositions) {
		if (!trainPositions.contains(rail)) {
			trainPositions.add(rail);
			signalBlocks.forEach(signalBlock -> {
				if (signalBlock.rails.contains(rail)) {
					trainPositions.addAll(signalBlock.rails);
					signalBlock.occupied = 2;
				}
			});
		}
	}

	public void resetOccupied() {
		signalBlocks.forEach(signalBlock -> signalBlock.occupied--);
	}

	public List<Integer> getColorMap(UUID rail) {
		final boolean[] hasColors = new boolean[DyeColor.values().length];
		signalBlocks.forEach(signalBlock -> {
			if (!hasColors[signalBlock.color.ordinal()] && signalBlock.rails.contains(rail)) {
				hasColors[signalBlock.color.ordinal()] = true;
			}
		});
		final List<Integer> colors = new ArrayList<>();
		for (int i = 0; i < DyeColor.values().length; i++) {
			if (hasColors[i]) {
				colors.add(i);
			}
		}
		return colors;
	}

	public static class SignalBlock extends SerializedDataBase {

		private final DyeColor color;
		private final Set<UUID> rails = new HashSet<>();
		private int occupied = 0;

		private static final String KEY_COLOR = "color";
		private static final String KEY_RAILS = "rails";

		public SignalBlock(DyeColor color, UUID rail) {
			this.color = color;
			rails.add(rail);
		}

		public SignalBlock(NbtCompound nbtCompound) {
			DyeColor savedColor;
			try {
				savedColor = DyeColor.values()[nbtCompound.getInt(KEY_COLOR)];
			} catch (Exception e) {
				e.printStackTrace();
				savedColor = DyeColor.RED;
			}
			color = savedColor;
			final NbtCompound nbtCompoundRails = nbtCompound.getCompound(KEY_RAILS);
			nbtCompoundRails.getKeys().forEach(key -> rails.add(nbtCompoundRails.getUuid(key)));
		}

		public SignalBlock(PacketByteBuf packet) {
			DyeColor savedColor;
			try {
				savedColor = DyeColor.values()[packet.readInt()];
			} catch (Exception e) {
				e.printStackTrace();
				savedColor = DyeColor.RED;
			}
			color = savedColor;
			final int railCount = packet.readInt();
			for (int i = 0; i < railCount; i++) {
				rails.add(packet.readUuid());
			}
		}

		@Override
		public NbtCompound toCompoundTag() {
			final NbtCompound nbtCompound = new NbtCompound();
			nbtCompound.putInt(KEY_COLOR, color.ordinal());
			final NbtCompound nbtCompoundRails = new NbtCompound();
			int i = 0;
			for (final UUID rail : rails) {
				nbtCompoundRails.putUuid(KEY_RAILS + i, rail);
				i++;
			}
			nbtCompound.put(KEY_RAILS, nbtCompoundRails);
			return nbtCompound;
		}

		@Override
		public void writePacket(PacketByteBuf packet) {
			packet.writeInt(color.ordinal());
			packet.writeInt(rails.size());
			rails.forEach(packet::writeUuid);
		}

		private boolean isConnected(UUID checkRail) {
			final long checkPos1 = checkRail.getLeastSignificantBits();
			final long checkPos2 = checkRail.getMostSignificantBits();
			return rails.stream().anyMatch(rail -> {
				final long pos1 = rail.getLeastSignificantBits();
				final long pos2 = rail.getMostSignificantBits();
				return checkPos1 == pos1 || checkPos1 == pos2 || checkPos2 == pos1 || checkPos2 == pos2;
			});
		}
	}
}
