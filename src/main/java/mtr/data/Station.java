package mtr.data;

import net.minecraft.nbt.CompoundTag;

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
		return String.format("Station (%s): %s}", name, platforms);
	}
}
