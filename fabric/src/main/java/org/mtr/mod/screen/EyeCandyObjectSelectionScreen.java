package org.mtr.mod.screen;

import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongCollection;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectImmutableList;

public class EyeCandyObjectSelectionScreen extends DashboardListSelectorScreen {

	private final Runnable updateData;

	public EyeCandyObjectSelectionScreen(ObjectImmutableList<DashboardListItem> allData, LongCollection selectedIds, Runnable updateData) {
		super(allData, selectedIds, true, false);
		this.updateData = updateData;
	}

	@Override
	protected void updateList() {
		super.updateList();
		updateData.run();
	}
}
