package mtr.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Set;

public class TrainData extends PersistentState {

	public static final String NAME = "mtr_train_data";
	public static final String KEY_STATIONS = "stations";
	public static final String KEY_STATION = "station_";

	private final Set<Station> stations;

	public TrainData() {
		super(NAME);
		stations = new HashSet<>();
	}

	@Override
	public void fromTag(CompoundTag tag) {
		CompoundTag tagStations = tag.getCompound(KEY_STATIONS);
		for (String key : tagStations.getKeys()) {
			stations.add(new Station(tagStations.getCompound(key)));
		}
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		CompoundTag tagStations = new CompoundTag();
		int i = 0;
		for (Station station : stations) {
			tagStations.put(KEY_STATION + i, station.toCompoundTag());
			i++;
		}
		tag.put(KEY_STATIONS, tagStations);
		return tag;
	}

	public void addStation(Station station) {
		stations.add(station);
		markDirty();
	}

	public Set<Station> getStations() {
		return stations;
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
