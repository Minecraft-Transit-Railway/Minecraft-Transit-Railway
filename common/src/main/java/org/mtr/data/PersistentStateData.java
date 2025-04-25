package org.mtr.data;

import it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.PersistentState;
import org.mtr.MTR;

/**
 * This class is for storing extra world data that is not stored in Transport Simulation Core.
 * For example, "Disable Next Station Announcements" is a Minecraft-only setting which isn't tracked by Transport Simulation Core.
 */
public final class PersistentStateData extends PersistentState {

	private final String uniqueWorldId;
	private final LongAVLTreeSet routeIdsWithDisabledAnnouncements = new LongAVLTreeSet();

	private static final String KEY_UNIQUE_WORLD_ID = "unique_world_id";
	private static final String KEY_ROUTE_IDS_WITH_DISABLED_ANNOUNCEMENTS = "route_ids_with_disabled_announcements";

	public PersistentStateData() {
		super();
		uniqueWorldId = MTR.randomString();
	}

	public PersistentStateData(NbtCompound nbt) {
		super();
		final String tempUniqueWorldId = nbt.getString(KEY_UNIQUE_WORLD_ID);
		if (tempUniqueWorldId.isEmpty()) {
			uniqueWorldId = MTR.randomString();
			markDirty();
		} else {
			uniqueWorldId = tempUniqueWorldId;
		}
		for (final long routeId : nbt.getLongArray(KEY_ROUTE_IDS_WITH_DISABLED_ANNOUNCEMENTS)) {
			routeIdsWithDisabledAnnouncements.add(routeId);
		}
	}

	@Override
	public NbtCompound writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
		nbt.putString(KEY_UNIQUE_WORLD_ID, uniqueWorldId);
		nbt.putLongArray(KEY_ROUTE_IDS_WITH_DISABLED_ANNOUNCEMENTS, routeIdsWithDisabledAnnouncements.toLongArray());
		return nbt;
	}

	public String getUniqueWorldId() {
		return uniqueWorldId;
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
		markDirty();
	}
}
