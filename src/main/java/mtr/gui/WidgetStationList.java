package mtr.gui;

import mtr.data.Station;

import java.util.Set;

public class WidgetStationList extends WidgetListBase<Station> {

	public WidgetStationList(int width) {
		super(width);
	}

	public void refreshList(Set<Station> stations, OnClick<Station> onButton1, OnClick<Station> onButton2, OnClick<Station> onDelete) {
		refreshList(stations, null, "icon_find", onButton1, "icon_edit", onButton2, "icon_delete", onDelete, null);
	}
}
