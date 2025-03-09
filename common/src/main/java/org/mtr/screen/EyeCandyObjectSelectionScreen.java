package org.mtr.screen;

import it.unimi.dsi.fastutil.longs.LongCollection;
import it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import net.minecraft.client.gui.screen.Screen;

public class EyeCandyObjectSelectionScreen extends DashboardListSelectorScreen {

	private final Runnable updateData;

	public EyeCandyObjectSelectionScreen(ObjectImmutableList<DashboardListItem> allData, LongCollection selectedIds, Runnable updateData, Screen previousScreen) {
		super(allData, selectedIds, true, false, previousScreen);
		this.updateData = updateData;
	}

	@Override
	protected void updateList() {
		super.updateList();
		updateData.run();
	}
}
