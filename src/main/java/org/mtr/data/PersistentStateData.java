package org.mtr.data;

import lombok.Getter;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedData;
import org.mtr.MTR;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
//? if >= 26.1 {
/*import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.stream.LongStream;
*///? }

/**
 * This class is for storing extra world data that is not stored in Transport Simulation Core.
 * For example, "Disable Next Station Announcements" is a Minecraft-only setting which isn't tracked by Transport Simulation Core.
 */
public final class PersistentStateData extends SavedData {

	@Getter
	private final String uniqueWorldId;
	private final LongAVLTreeSet routeIdsWithDisabledAnnouncements = new LongAVLTreeSet();

	private static final String KEY_UNIQUE_WORLD_ID = "unique_world_id";
	private static final String KEY_ROUTE_IDS_WITH_DISABLED_ANNOUNCEMENTS = "route_ids_with_disabled_announcements";

//? if >= 26.1 {
	/*// Saved data is described by a codec from 26.1 rather than by a pair of read and write methods.
	//
	// The route identifiers go through LONG_STREAM rather than a list of longs, because that is what
	// writes a long array tag, which is the shape putLongArray wrote and therefore the shape already
	// on disk. A list of longs compiles just as well and reads back as a list of separate tags, which
	// would silently find nothing in an existing world.
	//
	// The array is carried rather than the stream itself: a stream can only be read once, and the
	// getter is not promised to be called only once.
	private static final Codec<long[]> ROUTE_IDS_CODEC = Codec.LONG_STREAM.xmap(LongStream::toArray, LongStream::of);

	public static final SavedDataType<PersistentStateData> SAVED_DATA_TYPE = new SavedDataType<>(
		ResourceLocation.fromNamespaceAndPath(MTR.MOD_ID, MTR.MOD_ID),
		PersistentStateData::new,
		RecordCodecBuilder.create(instance -> instance.group(
			Codec.STRING.optionalFieldOf(KEY_UNIQUE_WORLD_ID, "").forGetter(persistentStateData -> persistentStateData.uniqueWorldId),
			ROUTE_IDS_CODEC.optionalFieldOf(KEY_ROUTE_IDS_WITH_DISABLED_ANNOUNCEMENTS, new long[0]).forGetter(persistentStateData -> persistentStateData.routeIdsWithDisabledAnnouncements.toLongArray())
		).apply(instance, PersistentStateData::new)),
		DataFixTypes.LEVEL
	);
*///? }

	public PersistentStateData() {
		super();
		uniqueWorldId = MTR.randomString();
	}

//? if >= 26.1 {
	/*private PersistentStateData(String savedUniqueWorldId, long[] savedRouteIdsWithDisabledAnnouncements) {
		super();
		if (savedUniqueWorldId.isEmpty()) {
			uniqueWorldId = MTR.randomString();
			setDirty();
		} else {
			uniqueWorldId = savedUniqueWorldId;
		}
		for (final long routeId : savedRouteIdsWithDisabledAnnouncements) {
			routeIdsWithDisabledAnnouncements.add(routeId);
		}
	}
*///? } else {
	public PersistentStateData(CompoundTag nbt) {
		super();
		final String tempUniqueWorldId = nbt.getString(KEY_UNIQUE_WORLD_ID);
		if (tempUniqueWorldId.isEmpty()) {
			uniqueWorldId = MTR.randomString();
			setDirty();
		} else {
			uniqueWorldId = tempUniqueWorldId;
		}
		for (final long routeId : nbt.getLongArray(KEY_ROUTE_IDS_WITH_DISABLED_ANNOUNCEMENTS)) {
			routeIdsWithDisabledAnnouncements.add(routeId);
		}
	}

	@Override
	public CompoundTag save(CompoundTag nbt, HolderLookup.Provider registries) {
		nbt.putString(KEY_UNIQUE_WORLD_ID, uniqueWorldId);
		nbt.putLongArray(KEY_ROUTE_IDS_WITH_DISABLED_ANNOUNCEMENTS, routeIdsWithDisabledAnnouncements.toLongArray());
		return nbt;
	}
//? }

	/**
	 * Sits here rather than at each of the three packets that ask for it, so that the difference
	 * between describing this data by a factory and describing it by a type is written once.
	 */
	public static PersistentStateData get(ServerLevel serverLevel) {
//? if >= 26.1 {
		/*return serverLevel.getDataStorage().computeIfAbsent(SAVED_DATA_TYPE);
*///? } else {
		return serverLevel.getDataStorage().computeIfAbsent(new SavedData.Factory<>(PersistentStateData::new, (nbt, wrapperLookup) -> new PersistentStateData(nbt), DataFixTypes.LEVEL), MTR.MOD_ID);
//? }
	}

	public boolean getRouteIdHasDisabledAnnouncements(long routeId) {
		return routeIdsWithDisabledAnnouncements.contains(routeId);
	}

	public void setRouteIdHasDisabledAnnouncements(long routeId, boolean isDisabled) {
		if (isDisabled) {
			routeIdsWithDisabledAnnouncements.add(routeId);
		} else {
			routeIdsWithDisabledAnnouncements.remove(routeId);
		}
		setDirty();
	}
}
