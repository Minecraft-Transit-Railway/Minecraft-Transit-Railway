package mtr.gui;

import io.github.cottonmc.cotton.gui.widget.WBox;
import io.github.cottonmc.cotton.gui.widget.data.Axis;
import mtr.data.NamedColoredBase;
import mtr.data.Route;
import mtr.data.Station;

import java.util.*;

public class WidgetNameColors<T extends NamedColoredBase> extends WBox implements IGui {

	public WidgetNameColors(int width) {
		super(Axis.VERTICAL);
		spacing = 0;
		this.width = width;
	}

	public void refreshList(Set<T> routes, Set<Station> stations, OnClick<T> onEdit, OnClick<T> onDeleteRoute, Runnable onSave) {
		children.clear();
		List<T> routesSorted = new ArrayList<>(routes);
		Collections.sort(routesSorted);
		for (T route : routesSorted) {
			WidgetNameColor panel = new WidgetNameColor(width, route.name, route.color, null, "icon_edit");
			panel.setOnClick(null, () -> onEdit.onClick(route), () -> {
				onDeleteRoute.onClick(route);
				onSave.run();
			});
			add(panel);

			List<Long> stationIds = ((Route) route).stationIds;
			for (int i = 0; i < stationIds.size(); i++) {
				final long stationId = stationIds.get(i);
				Optional<Station> findStation = stations.stream().filter(station -> station.id == stationId).findAny();
				if (findStation.isPresent()) {
					Station station = findStation.get();
					WidgetNameColor panelStation = new WidgetNameColor(width, String.format("%d. %s", i + 1, station.name), -1, "icon_up", "icon_down");
					final int index = i;
					panelStation.setOnClick(i == 0 ? null : () -> {
						long aboveId = ((Route) route).stationIds.get(index - 1);
						long thisId = ((Route) route).stationIds.get(index);
						((Route) route).stationIds.set(index - 1, thisId);
						((Route) route).stationIds.set(index, aboveId);
						onSave.run();
					}, i == stationIds.size() - 1 ? null : () -> {
						long thisId = ((Route) route).stationIds.get(index);
						long belowId = ((Route) route).stationIds.get(index + 1);
						((Route) route).stationIds.set(index, belowId);
						((Route) route).stationIds.set(index + 1, thisId);
						onSave.run();
					}, () -> {
						((Route) route).stationIds.remove(index);
						onSave.run();
					});
					add(panelStation);
				}
			}
		}
	}

	public void refreshList(Set<T> ts, OnClick<T> onButton1, OnClick<T> onButton2, OnClick<T> onDelete) {
		children.clear();
		List<T> tSorted = new ArrayList<>(ts);
		Collections.sort(tSorted);
		for (T t : tSorted) {
			WidgetNameColor panel = new WidgetNameColor(width, t.name, t.color, "icon_find", "icon_edit");
			panel.setOnClick(() -> onButton1.onClick(t), () -> onButton2.onClick(t), () -> onDelete.onClick(t));
			add(panel);
		}
	}

	@FunctionalInterface
	public interface OnClick<T extends NamedColoredBase> {
		void onClick(T t);
	}
}
