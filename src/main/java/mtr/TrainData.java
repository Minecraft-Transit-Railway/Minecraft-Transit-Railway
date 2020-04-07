package mtr;

import mods.railcraft.api.tracks.TrackKit;
import mods.railcraft.api.tracks.TrackRegistry;
import mods.railcraft.api.tracks.TrackToolsAPI;
import mods.railcraft.api.tracks.TrackType;
import mtr.data.RailSection;
import net.minecraft.block.BlockRailBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;

import java.util.HashSet;
import java.util.Set;

public class TrainData extends WorldSavedData {

	private static final String DATA_NAME = MTR.MODID + "_TrainData";

	private final Set<RailSection> rails = new HashSet<>();

	public TrainData() {
		super(DATA_NAME);
	}

	public TrainData(String name) {
		super(name);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		int size = nbt.getInteger("size");
		for (int i = 0; i < size; i++) {
			BlockPos pos = BlockPos.fromLong(nbt.getLong("rail_pos_" + i));
			TrackKit trackKit = TrackRegistry.TRACK_KIT.get(nbt.getString("rail_track_kit_" + i));
			TrackType trackType = TrackRegistry.TRACK_TYPE.get(nbt.getString("rail_track_type_" + i));
			BlockRailBase.EnumRailDirection railDirection = BlockRailBase.EnumRailDirection.byMetadata(nbt.getInteger("rail_direction_" + i));
			rails.add(new RailSection(pos, trackKit, trackType, railDirection));
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		int size = rails.size();
		compound.setInteger("size", size);

		int i = 0;
		for (RailSection rail : rails) {
			compound.setLong("rail_pos_" + i, rail.pos.toLong());
			compound.setString("rail_track_kit_" + i, rail.trackKit.getLocalizationTag());
			compound.setString("rail_track_type_" + i, rail.trackType.getLocalizationTag());
			compound.setInteger("rail_direction_" + i, rail.trackDirection.getMetadata());
			i++;
		}

		return compound;
	}

	public boolean addRail(World world, BlockPos pos) {
		TrackKit trackKit = TrackToolsAPI.getTrackKitSafe(world, pos);
		TrackType trackType = RailTools.getTrackType(world, pos);
		BlockRailBase.EnumRailDirection trackDirection = RailTools.getTrackDirection(world, pos);
		rails.add(new RailSection(pos, trackKit, trackType, trackDirection));
		return true;
	}

	public static TrainData get(World world) {
		MapStorage storage = world.getPerWorldStorage();
		TrainData instance = (TrainData) storage.getOrLoadData(TrainData.class, DATA_NAME);

		if (instance == null) {
			instance = new TrainData();
			storage.setData(DATA_NAME, instance);
		}

		return instance;
	}

}
