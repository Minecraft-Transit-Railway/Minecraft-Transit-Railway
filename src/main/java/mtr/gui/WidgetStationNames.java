package mtr.gui;

import io.github.cottonmc.cotton.gui.widget.WBox;
import io.github.cottonmc.cotton.gui.widget.data.Axis;
import mtr.data.Station;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class WidgetStationNames extends WBox implements IGui {

	public WidgetStationNames(int width) {
		super(Axis.VERTICAL);
		spacing = 0;
		this.width = width;
	}

	public void refreshStations(Set<Station> stations, OnClick onFind, OnClick onEdit, OnClick onDelete) {
		children.clear();
		List<Station> stationsSorted = new ArrayList<>(stations);
		Collections.sort(stationsSorted);
		for (Station station : stationsSorted) {
			WidgetStationName panelStation = new WidgetStationName(width, station.name);
			panelStation.setOnClick(() -> onFind.onClick(station), () -> onEdit.onClick(station), () -> onDelete.onClick(station));
			add(panelStation);
		}
	}

	@FunctionalInterface
	public interface OnClick {
		void onClick(Station station);
	}
}
