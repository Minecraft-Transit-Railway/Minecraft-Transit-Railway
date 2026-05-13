package org.mtr.screen;

import net.minecraft.util.Identifier;
import org.mtr.core.data.StationExit;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectCollection;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import org.mtr.widget.ListComponent;
import org.mtr.widget.ListItem;

import java.util.function.Consumer;

public final class StationExitListSelectorScreen extends ListSelectorScreen<StationExit, StationExit> {

	public StationExitListSelectorScreen(Consumer<ObjectArrayList<StationExit>> onClose, WindowBase previousScreen) {
		super(true, false, false, onClose, previousScreen);
	}

	@Override
	protected void setData(ListComponent<StationExit> listComponent, ObjectCollection<StationExit> dataList, ObjectArrayList<ObjectObjectImmutablePair<Identifier, ListItem.ActionConsumer<StationExit>>> actions) {
		ListComponent.setStationExits(listComponent, dataList, true, actions);
	}
}
