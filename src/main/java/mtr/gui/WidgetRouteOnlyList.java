package mtr.gui;

import mtr.data.Route;

import java.util.Set;

public class WidgetRouteOnlyList extends WidgetListBase<Route> {

	public WidgetRouteOnlyList(int width) {
		super(width);
	}

	public void refreshList(Set<Route> routes, String buttonPath, OnClick<Route> onButton) {
		refreshList(routes, null, null, null, null, null, buttonPath, onButton, null);
	}
}
