package org.mtr.screen;

import net.minecraft.resources.ResourceLocation;
import org.mtr.core.data.NameColorDataBase;
import org.mtr.core.data.Route;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectCollection;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import org.mtr.widget.ListComponent;
import org.mtr.widget.ListItem;

import java.util.function.Consumer;

public final class RouteListSelectorScreen extends ListSelectorScreen<Route, NameColorDataBase> {

	private final boolean canSelectDuplicateAndCanManuallySortSelectedList;
	private final boolean flattenRoutesByColor;

	public RouteListSelectorScreen(Consumer<ObjectArrayList<Route>> onClose, boolean canSelectDuplicate, boolean canManuallySortSelectedList, boolean flattenRoutesByColor, WindowBase previousScreen) {
		super(true, canSelectDuplicate, canManuallySortSelectedList, onClose, previousScreen);
		this.flattenRoutesByColor = flattenRoutesByColor;
		canSelectDuplicateAndCanManuallySortSelectedList = canSelectDuplicate && canManuallySortSelectedList;
	}

	@Override
	protected void setData(ListComponent<Route> listComponent, ObjectCollection<Route> dataList, boolean isSelectedList, ObjectArrayList<ObjectObjectImmutablePair<ResourceLocation, ListItem.ActionConsumer<Route>>> actions) {
		if (canSelectDuplicateAndCanManuallySortSelectedList && isSelectedList) {
			ListComponent.setGeneric(listComponent, dataList, NameColorDataBase::getName, NameColorDataBase::getColor, actions);
		} else {
			ListComponent.setRoutes(listComponent, dataList, null, flattenRoutesByColor, actions);
		}
	}
}
