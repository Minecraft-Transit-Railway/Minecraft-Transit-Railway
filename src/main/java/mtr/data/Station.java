package mtr.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;

import java.util.ArrayList;
import java.util.List;

public final class Station {

	private final String name;
	private final List<Platform> platforms;

	private static final String KEY_NAME = "name";
	private static final String KEY_PLATFORM = "platform_";

	public Station(String name) {
		this.name = name;
		platforms = new ArrayList<>();
	}

	public Station(CompoundTag tag) {
		name = tag.getString(KEY_NAME);
		platforms = new ArrayList<>();
		for (final String key : tag.getKeys()) {
			if (!key.equals(KEY_NAME)) {
				platforms.add(new Platform(tag.getCompound(key)));
			}
		}
	}

	public Station(PacketByteBuf packet) {
		name = packet.readString();
		platforms = new ArrayList<>();
		final int platformCount = packet.readInt();
		for (int i = 0; i < platformCount; i++) {
			platforms.add(new Platform(packet));
		}
	}

	public CompoundTag toCompoundTag() {
		final CompoundTag tag = new CompoundTag();
		tag.putString(KEY_NAME, name);
		int i = 0;
		for (final Platform platform : platforms) {
			tag.put(KEY_PLATFORM + i, platform.toCompoundTag());
			i++;
		}
		return tag;
	}

	public void writePacket(PacketByteBuf packet) {
		packet.writeString(name);
		packet.writeInt(platforms.size());
		for (final Platform platform : platforms) {
			platform.writePacket(packet);
		}
	}

	public String getName() {
		return name;
	}

	public void addPlatform(Platform platform) {
		platforms.add(platform);
	}

	public List<Platform> getPlatforms() {
		return platforms;
	}

	@Override
	public String toString() {
		return String.format("Station (%s): %s", name, platforms);
	}
}
