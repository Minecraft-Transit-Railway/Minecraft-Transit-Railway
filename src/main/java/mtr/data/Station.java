package mtr.data;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public final class Station extends AreaBase {

	public int zone;
	public final Map<String, List<String>> exits;

	private static final String KEY_ZONE = "zone";
	private static final String KEY_EXITS = "exits";

	private static final String KEY_EXIT_EDIT_PARENT = "exit_edit_parent";
	private static final String KEY_EXIT_DELETE_PARENT = "exit_delete_parent";
	private static final String KEY_EXIT_DESTINATIONS = "exit_destinations";

	public Station() {
		super();
		exits = new HashMap<>();
	}

	public Station(long id) {
		super(id);
		exits = new HashMap<>();
	}

	public Station(NbtCompound nbtCompound) {
		super(nbtCompound);
		zone = nbtCompound.getInt(KEY_ZONE);

		exits = new HashMap<>();
		final NbtCompound tagExits = nbtCompound.getCompound(KEY_EXITS);
		for (final String keyParent : tagExits.getKeys()) {
			final List<String> destinations = new ArrayList<>();
			final NbtCompound tagDestinations = tagExits.getCompound(keyParent);
			for (final String keyDestination : tagDestinations.getKeys()) {
				destinations.add(tagDestinations.getString(keyDestination));
			}
			exits.put(keyParent, destinations);
		}
	}

	public Station(PacketByteBuf packet) {
		super(packet);
		zone = packet.readInt();
		exits = new HashMap<>();
		final int exitCount = packet.readInt();
		for (int i = 0; i < exitCount; i++) {
			final String parent = packet.readString(PACKET_STRING_READ_LENGTH);
			final List<String> destinations = new ArrayList<>();
			final int destinationCount = packet.readInt();
			for (int j = 0; j < destinationCount; j++) {
				destinations.add(packet.readString(PACKET_STRING_READ_LENGTH));
			}
			exits.put(parent, destinations);
		}
	}

	@Override
	public NbtCompound toCompoundTag() {
		final NbtCompound nbtCompound = super.toCompoundTag();
		nbtCompound.putInt(KEY_ZONE, zone);

		final NbtCompound tagExits = new NbtCompound();
		exits.forEach((parent, destinations) -> {
			final NbtCompound tagDestinations = new NbtCompound();
			for (int i = 0; i < destinations.size(); i++) {
				tagDestinations.putString(KEY_EXITS + i, destinations.get(i));
			}
			tagExits.put(parent, tagDestinations);
		});
		nbtCompound.put(KEY_EXITS, tagExits);
		return nbtCompound;
	}

	@Override
	public void writePacket(PacketByteBuf packet) {
		super.writePacket(packet);
		packet.writeInt(zone);
		packet.writeInt(exits.size());
		exits.forEach((parent, destinations) -> {
			packet.writeString(parent);
			packet.writeInt(destinations.size());
			destinations.forEach(packet::writeString);
		});
	}

	@Override
	public void update(String key, PacketByteBuf packet) {
		switch (key) {
			case KEY_EXIT_EDIT_PARENT:
				final String oldParent = packet.readString(PACKET_STRING_READ_LENGTH);
				final String newParent = packet.readString(PACKET_STRING_READ_LENGTH);
				setExitParent(oldParent, newParent);
				break;
			case KEY_EXIT_DELETE_PARENT:
				exits.remove(packet.readString(PACKET_STRING_READ_LENGTH));
				break;
			case KEY_EXIT_DESTINATIONS:
				final String parent = packet.readString(PACKET_STRING_READ_LENGTH);
				if (parentExists(parent)) {
					exits.get(parent).clear();
					final int destinationCount = packet.readInt();
					for (int i = 0; i < destinationCount; i++) {
						exits.get(parent).add(packet.readString(PACKET_STRING_READ_LENGTH));
					}
				}
				break;
			case KEY_ZONE:
				name = packet.readString(PACKET_STRING_READ_LENGTH);
				color = packet.readInt();
				zone = packet.readInt();
				break;
			default:
				super.update(key, packet);
				break;
		}
	}

	public void setZone(Consumer<PacketByteBuf> sendPacket) {
		final PacketByteBuf packet = PacketByteBufs.create();
		packet.writeLong(id);
		packet.writeString(KEY_ZONE);
		packet.writeString(name);
		packet.writeInt(color);
		packet.writeInt(zone);
		sendPacket.accept(packet);
	}

	public void setExitParent(String oldParent, String newParent, Consumer<PacketByteBuf> sendPacket) {
		setExitParent(oldParent, newParent);
		final PacketByteBuf packet = PacketByteBufs.create();
		packet.writeLong(id);
		packet.writeString(KEY_EXIT_EDIT_PARENT);
		packet.writeString(oldParent);
		packet.writeString(newParent);
		sendPacket.accept(packet);
	}

	public void deleteExitParent(String parent, Consumer<PacketByteBuf> sendPacket) {
		exits.remove(parent);
		final PacketByteBuf packet = PacketByteBufs.create();
		packet.writeLong(id);
		packet.writeString(KEY_EXIT_DELETE_PARENT);
		packet.writeString(parent);
		sendPacket.accept(packet);
	}

	public void setExitDestinations(String parent, Consumer<PacketByteBuf> sendPacket) {
		if (parentExists(parent)) {
			final PacketByteBuf packet = PacketByteBufs.create();
			packet.writeLong(id);
			packet.writeString(KEY_EXIT_DESTINATIONS);
			packet.writeString(parent);
			packet.writeInt(exits.get(parent).size());
			exits.get(parent).forEach(packet::writeString);
			sendPacket.accept(packet);
		}
	}

	public Map<String, List<String>> getGeneratedExits() {
		final List<String> exitParents = new ArrayList<>(exits.keySet());
		exitParents.sort(String::compareTo);

		final Map<String, List<String>> generatedExits = new HashMap<>();
		exitParents.forEach(parent -> {
			final String exitLetter = parent.substring(0, 1);
			if (!generatedExits.containsKey(exitLetter)) {
				generatedExits.put(exitLetter, new ArrayList<>());
			}

			generatedExits.get(exitLetter).addAll(exits.get(parent));
			generatedExits.put(parent, exits.get(parent));
		});

		return generatedExits;
	}

	private void setExitParent(String oldParent, String newParent) {
		if (parentExists(oldParent)) {
			final List<String> existingDestinations = exits.get(oldParent);
			exits.remove(oldParent);
			exits.put(newParent, existingDestinations == null ? new ArrayList<>() : existingDestinations);
		} else {
			exits.put(newParent, new ArrayList<>());
		}
	}

	private boolean parentExists(String parent) {
		return parent != null && exits.containsKey(parent);
	}

	public static long serializeExit(String exit) {
		final char[] characters = exit.toCharArray();
		long code = 0;
		for (final char character : characters) {
			code = code << 8;
			code += character;
		}
		return code;
	}

	public static String deserializeExit(long code) {
		StringBuilder exit = new StringBuilder();
		long charCodes = code;
		while (charCodes > 0) {
			exit.insert(0, (char) (charCodes & 0xFF));
			charCodes = charCodes >> 8;
		}
		return exit.toString();
	}
}
