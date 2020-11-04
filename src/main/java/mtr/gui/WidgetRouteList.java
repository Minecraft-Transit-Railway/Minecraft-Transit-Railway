package mtr.gui;

import mtr.data.Route;

public class WidgetRouteList extends WidgetListBase<Route> {

	public WidgetRouteList(int width) {
		super(width);
	}

	@Override
	protected String getName(Route item) {
		return item.name;
	}

	@Override
	protected int getColor(Route item) {
		return item.color;
	}
}
