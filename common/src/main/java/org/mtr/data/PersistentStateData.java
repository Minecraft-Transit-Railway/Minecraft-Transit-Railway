package org.mtr.data;

import it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.PersistentState;

/**
 * This class is for storing extra world data that is not stored in Transport Simulation Core.
 * For example, "Disable Next Station Announcements" is a Minecraft-only setting which isn't tracked by Transport Simulation Core.
 */
public final class PersistentStateData extends PersistentState {

	private final LongAVLTreeSet routeIdsWithDisabledAnnouncements = new LongAVLTreeSet();

	private static final String KEY_DATA = "route_ids_with_disabled_announcements";

	public PersistentStateData() {
		super();
	}

	public PersistentStateData(NbtCompound nbt) {
		super();
		for (final long routeId : nbt.getLongArray(KEY_DATA)) {
			routeIdsWithDisabledAnnouncements.add(routeId);
		}
	}

	@Override
	public NbtCompound writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
		nbt.putLongArray(KEY_DATA, routeIdsWithDisabledAnnouncements.toLongArray());
		return nbt;
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
