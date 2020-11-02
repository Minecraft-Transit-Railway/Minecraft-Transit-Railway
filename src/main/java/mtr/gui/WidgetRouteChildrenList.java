package mtr.gui;

import mtr.data.NamedColoredBase;
import mtr.data.Route;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class WidgetRouteChildrenList extends WidgetListBase<Route> {

	public WidgetRouteChildrenList(int width) {
		super(width);
	}

	public void refreshList(Set<Route> routes, Set<? extends NamedColoredBase> stations, OnClick<Route> onEdit, OnClick<Route> onDeleteRoute, Runnable onSave) {
		refreshList(routes, stations, null, null, "icon_edit", onEdit, "icon_delete", (route) -> {
			onDeleteRoute.onClick(route);
			onSave.run();
		}, onSave);
	}

	@Override
	protected void addChildren(Route route, Set<? extends NamedColoredBase> stations, Runnable onSave) {
		List<Long> stationIds = route.stationIds;
		for (int i = 0; i < stationIds.size(); i++) {
			final long stationId = stationIds.get(i);
			Optional<? extends NamedColoredBase> findStation = stations.stream().filter(station -> station.id == stationId).findAny();
			if (findStation.isPresent()) {
				NamedColoredBase station = findStation.get();
				WidgetNameColor panelStation = new WidgetNameColor(width, String.format("%d. %s", i + 1, station.name), -1, "icon_up", "icon_down", "icon_delete");
				final int index = i;
				panelStation.setOnClick(i == 0 ? null : () -> {
					long aboveId = route.stationIds.get(index - 1);
					long thisId = route.stationIds.get(index);
					route.stationIds.set(index - 1, thisId);
					route.stationIds.set(index, aboveId);
					onSave.run();
				}, i == stationIds.size() - 1 ? null : () -> {
					long thisId = route.stationIds.get(index);
					long belowId = route.stationIds.get(index + 1);
					route.stationIds.set(index, belowId);
					route.stationIds.set(index + 1, thisId);
					onSave.run();
				}, () -> {
					route.stationIds.remove(index);
					onSave.run();
				});
				add(panelStation);
			}
		}
	}
}
