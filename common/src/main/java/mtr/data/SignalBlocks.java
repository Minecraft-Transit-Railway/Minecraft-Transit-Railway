package mtr.data;

import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.DyeColor;

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
			Collections.sort(connectedSignalBlocks);
			final SignalBlock firstSignalBlock = connectedSignalBlocks.remove(0);
			firstSignalBlock.rails.add(rail);
			connectedSignalBlocks.forEach(signalBlock -> firstSignalBlock.rails.addAll(signalBlock.rails));
			signalBlocks.removeIf(connectedSignalBlocks::contains);
			return 0;
		}
	}

	public long remove(long id, DyeColor color, UUID rail) {
		SignalBlock connectedSignalBlock = null;
		for (final SignalBlock signalBlock : signalBlocks) {
			if (signalBlock.color == color && signalBlock.isConnected(rail)) {
				connectedSignalBlock = signalBlock;
				break;
			}
		}

		if (connectedSignalBlock != null) {
			signalBlocks.remove(connectedSignalBlock);
			connectedSignalBlock.rails.remove(rail);

			if (!connectedSignalBlock.rails.isEmpty()) {
				final List<UUID> rails = new ArrayList<>(connectedSignalBlock.rails);
				Collections.sort(rails);
				add(connectedSignalBlock.id, color, rails.remove(0));

				long returnId = 0;
				for (final UUID existingRail : rails) {
					final long newId = add(id, color, existingRail);
					if (newId != connectedSignalBlock.id) {
						returnId = newId;
					}
				}

				return returnId;
			}
		}

		return 0;
	}

	public void occupy(UUID currentRail, List<Map<UUID, Long>> trainPositions, long trainId) {
		if (trainPositions.size() < 2) {
			return;
		}

		final Set<UUID> railsToAdd = new HashSet<>();
		railsToAdd.add(currentRail);

		signalBlocks.forEach(signalBlock -> {
			if (signalBlock.rails.contains(currentRail)) {
				railsToAdd.addAll(signalBlock.rails);
				signalBlock.occupied = 2;
			}
		});

		for (final Map<UUID, Long> trainPositionsMap : trainPositions) {
			if (railsToAdd.stream().anyMatch(rail -> trainPositionsMap.containsKey(rail) && trainPositionsMap.get(rail) != trainId)) {
				return;
			}
		}

		railsToAdd.forEach(rail -> trainPositions.get(1).put(rail, trainId));
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

	public FriendlyByteBuf getValidationPacket(Map<BlockPos, Map<BlockPos, Rail>> rails) {
		final List<UUID> railsToRemove = new ArrayList<>();
		final List<DyeColor> colorsToRemove = new ArrayList<>();

		signalBlocks.forEach(signalBlock -> signalBlock.rails.forEach(rail -> {
			final BlockPos pos1 = BlockPos.of(rail.getLeastSignificantBits());
			final BlockPos pos2 = BlockPos.of(rail.getMostSignificantBits());
			if (!RailwayData.containsRail(rails, pos1, pos2)) {
				railsToRemove.add(rail);
				colorsToRemove.add(signalBlock.color);
			}
		}));

		if (railsToRemove.isEmpty()) {
			return null;
		} else {
			final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
			packet.writeInt(railsToRemove.size());
			for (int i = 0; i < railsToRemove.size(); i++) {
				final DyeColor color = colorsToRemove.get(i);
				final UUID rail = railsToRemove.get(i);
				packet.writeLong(remove(0, color, rail));
				packet.writeInt(color.ordinal());
				packet.writeUUID(rail);
			}
			return packet;
		}
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

		public SignalBlock(CompoundTag compoundTag) {
			super(compoundTag);
			DyeColor savedColor;
			try {
				savedColor = DyeColor.values()[compoundTag.getInt(KEY_COLOR)];
			} catch (Exception e) {
				e.printStackTrace();
				savedColor = DyeColor.RED;
			}
			color = savedColor;
			final CompoundTag compoundTagRails = compoundTag.getCompound(KEY_RAILS);
			compoundTagRails.getAllKeys().forEach(key -> rails.add(compoundTagRails.getUUID(key)));
		}

		public SignalBlock(FriendlyByteBuf packet) {
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
				rails.add(packet.readUUID());
			}
		}

		@Override
		public CompoundTag toCompoundTag() {
			final CompoundTag compoundTag = super.toCompoundTag();
			compoundTag.putInt(KEY_COLOR, color.ordinal());
			final CompoundTag compoundTagRails = new CompoundTag();
			int i = 0;
			for (final UUID rail : rails) {
				compoundTagRails.putUUID(KEY_RAILS + i, rail);
				i++;
			}
			compoundTag.put(KEY_RAILS, compoundTagRails);
			return compoundTag;
		}

		@Override
		public void writePacket(FriendlyByteBuf packet) {
			super.writePacket(packet);
			packet.writeInt(color.ordinal());
			packet.writeInt(rails.size());
			rails.forEach(packet::writeUUID);
		}

		@Override
		protected boolean hasTransportMode() {
			return false;
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

		@Override
		public int compareTo(NameColorDataBase compare) {
			return Long.compare(id, compare.id);
		}
	}
}
