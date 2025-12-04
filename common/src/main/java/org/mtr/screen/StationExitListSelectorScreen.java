package org.mtr.screen;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import net.minecraft.util.Identifier;
import org.mtr.core.data.StationExit;
import org.mtr.widget.ListComponent;
import org.mtr.widget.ListItem;

import java.util.function.Consumer;

public final class StationExitListSelectorScreen extends ListSelectorScreen<StationExit, StationExit> {

	public StationExitListSelectorScreen(Consumer<ObjectArrayList<StationExit>> onClose, WindowBase previousScreen) {
		super(true, false, false, onClose, previousScreen);
	}

	@Override
	protected void setData(ListComponent<StationExit> listComponent, ObjectCollection<StationExit> dataList, ObjectArrayList<ObjectObjectImmutablePair<Identifier, ListItem.ActionConsumer<StationExit>>> actions) {
		ListComponent.setStationExits(listComponent, dataList, actions);
	}
}
