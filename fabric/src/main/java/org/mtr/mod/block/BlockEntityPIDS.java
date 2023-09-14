package org.mtr.mod.block;

import it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import org.mtr.mapping.holder.BlockEntityType;
import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.BlockState;
import org.mtr.mapping.holder.CompoundTag;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mod.data.IPIDSRenderChild;

import java.util.ArrayList;

public abstract class BlockEntityPIDS extends BlockEntityExtension implements IPIDSRenderChild {

	public final String[] messages = new String[getMaxArrivals() * getLinesPerArrival()];
	private final boolean[] hideArrival = new boolean[getMaxArrivals()];
	private final LongAVLTreeSet filterPlatformIds = new LongAVLTreeSet();
	private int displayPage = 0;
	private static final String KEY_MESSAGE = "message";
	private static final String KEY_HIDE_ARRIVAL = "hide_arrival";
	private static final String KEY_PLATFORM_IDS = "platform_ids";
	private static final String KEY_DISPLAY_PAGE = "display_page";

	public BlockEntityPIDS(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public void readCompoundTag(CompoundTag compoundTag) {
		for (int i = 0; i < getMaxArrivals() * getLinesPerArrival(); i++) {
			messages[i] = compoundTag.getString(KEY_MESSAGE + i);
		}
		for (int i = 0; i < getMaxArrivals(); i++) {
			hideArrival[i] = compoundTag.getBoolean(KEY_HIDE_ARRIVAL + i);
		}
		filterPlatformIds.clear();
		final long[] platformIdsArray = compoundTag.getLongArray(KEY_PLATFORM_IDS);
		for (final long platformId : platformIdsArray) {
			filterPlatformIds.add(platformId);
		}
		displayPage = compoundTag.getInt(KEY_DISPLAY_PAGE);
	}

	@Override
	public void writeCompoundTag(CompoundTag compoundTag) {
		for (int i = 0; i < getMaxArrivals() * getLinesPerArrival(); i++) {
			compoundTag.putString(KEY_MESSAGE + i, messages[i] == null ? "" : messages[i]);
		}
		for (int i = 0; i < getMaxArrivals(); i++) {
			compoundTag.putBoolean(KEY_HIDE_ARRIVAL + i, hideArrival[i]);
		}
		compoundTag.putLongArray(KEY_PLATFORM_IDS, new ArrayList<>(filterPlatformIds));
		compoundTag.putInt(KEY_DISPLAY_PAGE, displayPage);
	}

	public void setData(String[] messages, boolean[] hideArrival, LongAVLTreeSet filterPlatformIds, int displayPage) {
		System.arraycopy(messages, 0, this.messages, 0, Math.min(messages.length, this.messages.length));
		System.arraycopy(hideArrival, 0, this.hideArrival, 0, Math.min(hideArrival.length, this.hideArrival.length));
		this.filterPlatformIds.clear();
		this.filterPlatformIds.addAll(filterPlatformIds);
		this.displayPage = displayPage;
		markDirty2();
	}

	public int getDisplayPage() {
		return displayPage;
	}

	public LongAVLTreeSet getFilterPlatformIds() {
		return filterPlatformIds;
	}

	public String getMessage(int index) {
		if (index >= 0 && index < getMaxArrivals() * getLinesPerArrival()) {
			if (messages[index] == null) {
				messages[index] = "";
			}
			return messages[index];
		} else {
			return "";
		}
	}

	public boolean getHideArrival(int index) {
		if (index >= 0 && index < getMaxArrivals()) {
			return hideArrival[index];
		} else {
			return false;
		}
	}

	public abstract int getMaxArrivals();

	public abstract int getLinesPerArrival();
}
