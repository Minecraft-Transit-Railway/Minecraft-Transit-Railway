package org.mtr.mod.data;

import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import org.mtr.mapping.holder.CompoundTag;
import org.mtr.mapping.mapper.PersistenceStateExtension;
import org.mtr.mod.Init;

import javax.annotation.Nonnull;

/**
 * This class is for storing extra world data that is not stored in Transport Simulation Core.
 * For example, "Disable Next Station Announcements" is a Minecraft-only setting which isn't tracked by Transport Simulation Core.
 */
public final class PersistentStateData extends PersistenceStateExtension {

	private final LongAVLTreeSet routeIdsWithDisabledAnnouncements = new LongAVLTreeSet();

	private static final String KEY_DATA = "route_ids_with_disabled_announcements";

	public PersistentStateData() {
		super(Init.MOD_ID);
	}

	@Override
	public void readNbt(CompoundTag compoundTag) {
		routeIdsWithDisabledAnnouncements.clear();
		for (final long routeId : compoundTag.getLongArray(KEY_DATA)) {
			routeIdsWithDisabledAnnouncements.add(routeId);
		}
	}

	@Nonnull
	@Override
	public CompoundTag writeNbt2(CompoundTag compoundTag) {
		compoundTag.putLongArray(KEY_DATA, routeIdsWithDisabledAnnouncements.toLongArray());
		return compoundTag;
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
		markDirty2();
	}
}
