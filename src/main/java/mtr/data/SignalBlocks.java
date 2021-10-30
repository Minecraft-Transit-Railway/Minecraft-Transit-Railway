package mtr.data;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.DyeColor;

import java.util.*;
import java.util.stream.Collectors;

public class SignalBlocks {

	public final Set<SignalBlock> signalBlocks = new HashSet<>();

	public long add(long id, DyeColor color, UUID rail) {
		final List<SignalBlock> connectedSignalBlocks = new ArrayList<>();
		signalBlocks.forEach(signalBlock -> {
			if (signalBlock.color == color && signalBlock.isConnected(rail)) {
				connectedSignalBlocks.add(signalBlock);
			}
		});

		if (connectedSignalBlocks.isEmpty()) {
			final SignalBlock newSignalBlock = new SignalBlock(id, color, rail);
			signalBlocks.add(newSignalBlock);
			return newSignalBlock.id;
		} else {
			final SignalBlock firstSignalBlock = connectedSignalBlocks.remove(0);
			firstSignalBlock.rails.add(rail);
			connectedSignalBlocks.forEach(signalBlock -> firstSignalBlock.rails.addAll(signalBlock.rails));
			signalBlocks.removeIf(connectedSignalBlocks::contains);
			return 0;
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
			signalBlock.rails.forEach(existingRail -> add(0, color, existingRail));
		});

		signalBlocks.removeIf(signalBlock -> signalBlock.rails.isEmpty());
	}

	public void occupy(UUID rail, Map<UUID, Long> trainPositions, long trainId) {
		trainPositions.put(rail, trainId);
		signalBlocks.forEach(signalBlock -> {
			if (signalBlock.rails.contains(rail)) {
				signalBlock.rails.forEach(rail1 -> trainPositions.put(rail1, trainId));
				signalBlock.occupied = 2;
			}
		});
	}

	public void resetOccupied() {
		signalBlocks.forEach(signalBlock -> {
			if (signalBlock.isOccupied()) {
				signalBlock.occupied--;
			}
		});
	}

	public List<SignalBlock> getSignalBlocksAtTrack(UUID rail) {
		return signalBlocks.stream().filter(signalBlock -> signalBlock.rails.contains(rail)).sorted(Comparator.comparingInt(signalBlock -> signalBlock.color.ordinal())).collect(Collectors.toList());
	}

	public boolean isOccupied(UUID rail) {
		return signalBlocks.stream().anyMatch(signalBlock -> signalBlock.rails.contains(rail) && signalBlock.isOccupied());
	}

	public void getSignalBlockStatus(Map<Long, Boolean> signalBlockStatus, UUID rail) {
		signalBlocks.forEach(signalBlock -> {
			if (signalBlock.rails.contains(rail)) {
				signalBlockStatus.put(signalBlock.id, signalBlock.isOccupied());
			}
		});
	}

	public void writeSignalBlockStatus(Map<Long, Boolean> signalBlockStatus) {
		signalBlockStatus.forEach((id, occupied) -> signalBlocks.forEach(signalBlock -> {
			if (signalBlock.id == id) {
				signalBlock.occupied = occupied ? 2 : 0;
			}
		}));
	}

	public static class SignalBlock extends NameColorDataBase {

		public final DyeColor color;
		private final Set<UUID> rails = new HashSet<>();
		private int occupied = 0;

		private static final String KEY_COLOR = "color";
		private static final String KEY_RAILS = "rails";

		private SignalBlock(long id, DyeColor color, UUID rail) {
			super(id);
			this.color = color;
			rails.add(rail);
		}

		public SignalBlock(NbtCompound nbtCompound) {
			super(nbtCompound);
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
			super(packet);
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
			final NbtCompound nbtCompound = super.toCompoundTag();
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
			super.writePacket(packet);
			packet.writeInt(color.ordinal());
			packet.writeInt(rails.size());
			rails.forEach(packet::writeUuid);
		}

		public boolean isOccupied() {
			return occupied > 0;
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
