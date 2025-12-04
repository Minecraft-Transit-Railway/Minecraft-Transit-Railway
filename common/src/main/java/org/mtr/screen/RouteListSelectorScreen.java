package org.mtr.screen;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import net.minecraft.util.Identifier;
import org.mtr.core.data.NameColorDataBase;
import org.mtr.core.data.Route;
import org.mtr.widget.ListComponent;
import org.mtr.widget.ListItem;

import java.util.function.Consumer;

public final class RouteListSelectorScreen extends ListSelectorScreen<Route, NameColorDataBase> {

	public RouteListSelectorScreen(Consumer<ObjectArrayList<Route>> onClose, WindowBase previousScreen) {
		super(true, false, false, onClose, previousScreen);
	}

	@Override
	protected void setData(ListComponent<Route> listComponent, ObjectCollection<Route> dataList, ObjectArrayList<ObjectObjectImmutablePair<Identifier, ListItem.ActionConsumer<Route>>> actions) {
		ListComponent.setRoutes(listComponent, dataList, null, true, actions);
	}
}
