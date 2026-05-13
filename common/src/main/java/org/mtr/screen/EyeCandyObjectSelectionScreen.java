package org.mtr.screen;

import net.minecraft.client.gui.screen.Screen;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongCollection;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectImmutableList;

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
