package org.mtr.screen;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import net.minecraft.util.Identifier;
import org.mtr.core.data.NameColorDataBase;
import org.mtr.core.data.Station;
import org.mtr.widget.ListComponent;
import org.mtr.widget.ListItem;

import java.util.function.Consumer;

public final class StationListSelectorScreen extends ListSelectorScreen<Station, NameColorDataBase> {

	public StationListSelectorScreen(Consumer<ObjectArrayList<Station>> onClose, WindowBase previousScreen) {
		super(true, false, false, onClose, previousScreen);
	}

	@Override
	protected void setData(ListComponent<Station> listComponent, ObjectCollection<Station> dataList, ObjectArrayList<ObjectObjectImmutablePair<Identifier, ListItem.ActionConsumer<Station>>> actions) {
		ListComponent.setAreas(listComponent, dataList, null, actions);
	}
}
