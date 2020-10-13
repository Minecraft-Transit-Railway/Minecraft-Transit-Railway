package mtr.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.HashSet;
import java.util.Set;

public class TrainData extends PersistentState {

	public static final String NAME = "mtr_train_data";
	public static final String KEY_STATIONS = "stations";
	public static final String KEY_STATION = "station_";
	public static final String KEY_PLATFORMS = "platforms";
	public static final String KEY_PLATFORM = "platform_";

	private final Set<Station> stations;
	private final Set<Platform> platforms;

	public TrainData() {
		super(NAME);
		stations = new HashSet<>();
		platforms = new HashSet<>();
	}

	@Override
	public void fromTag(CompoundTag tag) {
		final CompoundTag tagStations = tag.getCompound(KEY_STATIONS);
		for (String key : tagStations.getKeys()) {
			stations.add(new Station(tagStations.getCompound(key)));
		}

		final CompoundTag tagNewPlatforms = tag.getCompound(KEY_PLATFORMS);
		for (String key : tagNewPlatforms.getKeys()) {
			platforms.add(new Platform(tagNewPlatforms.getCompound(key)));
		}
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		final CompoundTag tagStations = new CompoundTag();
		int i = 0;
		for (Station station : stations) {
			tagStations.put(KEY_STATION + i, station.toCompoundTag());
			i++;
		}
		tag.put(KEY_STATIONS, tagStations);

		final CompoundTag tagNewPlatforms = new CompoundTag();
		int j = 0;
		for (Platform platform : platforms) {
			tagNewPlatforms.put(KEY_PLATFORM + j, platform.toCompoundTag());
			j++;
		}
		tag.put(KEY_PLATFORMS, tagNewPlatforms);

		return tag;
	}

	public void addStation(Station station) {
		stations.add(station);
		markDirty();
	}

	public Set<Station> getStations() {
		return stations;
	}

	public void addPlatform(Platform newPlatform) {
		platforms.removeIf(newPlatform::overlaps);
		platforms.add(newPlatform);
		markDirty();
	}

	public Set<Platform> getPlatforms(WorldAccess world) {
		validateData(world);
		return platforms;
	}

	public void setData(WorldAccess world, Set<Station> stations, Set<Platform> platforms) {
		this.stations.clear();
		this.stations.addAll(stations);
		this.platforms.clear();
		this.platforms.addAll(platforms);
		validateData(world);
	}

	private void validateData(WorldAccess world) {
		platforms.removeIf(platform -> !platform.hasRail(world));
		markDirty();
	}

	public static TrainData getInstance(World world) {
		MinecraftServer minecraftServer = world.getServer();
		if (minecraftServer == null) {
			return null;
		} else {
			return minecraftServer.getOverworld().getPersistentStateManager().getOrCreate(TrainData::new, NAME);
		}
	}
}
