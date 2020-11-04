package mtr.gui;

import mtr.data.NamedColoredBase;
import mtr.data.Route;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class WidgetRouteChildrenSet extends WidgetSet<Route> {

	public WidgetRouteChildrenSet(int width) {
		super(width);
	}

	public void refreshList(Set<Route> routes, OnClick<Route> onEdit, OnClick<Route> onDelete, Runnable onSave) {
		refreshList(routes, null, null, "icon_edit", onEdit, "icon_delete", (route) -> {
			onDelete.onClick(route);
			onSave.run();
		}, onSave);
	}

	@Override
	protected void addChildren(Route route, Runnable onSave) {
		final List<Long> stationIds = route.stationIds;
		for (int i = 0; i < stationIds.size(); i++) {
			final long stationId = stationIds.get(i);
			final Optional<? extends NamedColoredBase> optionalStation = ScreenBase.GuiBase.stations.stream().filter(station -> station.id == stationId).findAny();
			if (optionalStation.isPresent()) {
				add(createUpDownPanel(width, String.format("%d. %s", i + 1, optionalStation.get().name), -1, stationIds, onSave, i, stationIds.size()));
			}
		}
	}
}
