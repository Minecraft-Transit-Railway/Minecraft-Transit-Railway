package org.mtr.data;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.core.data.StationExit;

import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class FormattedStationExit implements Comparable<FormattedStationExit> {

	public final String name;
	public final String parent;
	public final String number;
	public final ObjectArrayList<String> destinations;

	public FormattedStationExit(StationExit stationExit) {
		this(stationExit.getName(), stationExit.getDestinations());
	}

	public FormattedStationExit(String rawName, ObjectArrayList<String> destinations) {
		name = rawName.isEmpty() ? "A1" : rawName.length() == 1 ? rawName.replaceAll("[a-z]|[A-Z]", "").isEmpty() ? rawName + "1" : rawName + "A" : rawName;

		final Pattern pattern = Pattern.compile("^([a-z]+|[A-Z]+|[0-9]+)(.*)$");
		final Matcher matcher = pattern.matcher(name);

		if (matcher.matches()) {
			parent = matcher.group(1);
			number = matcher.group(2);
		} else {
			parent = "A";
			number = "1";
		}

		this.destinations = destinations;
	}

	public static ObjectArrayList<FormattedStationExit> getFormattedStationExits(ObjectArrayList<StationExit> exits, boolean addExitParents) {
		final ObjectArrayList<FormattedStationExit> newExits = new ObjectArrayList<>();
		final Object2ObjectOpenHashMap<String, FormattedStationExit> addedParentExits = new Object2ObjectOpenHashMap<>();
		Collections.sort(exits);
		exits.stream().map(FormattedStationExit::new).forEach(exit -> {
			if (addExitParents) {
				if (!addedParentExits.containsKey(exit.parent)) {
					final FormattedStationExit newExit = new FormattedStationExit(exit.parent, new ObjectArrayList<>());
					newExits.add(newExit);
					addedParentExits.put(exit.parent, newExit);
				}
				final FormattedStationExit parentExit = addedParentExits.get(exit.parent);
				if (parentExit != null && parentExit != exit) {
					parentExit.destinations.addAll(exit.destinations);
				}
			}
			newExits.add(exit);
		});
		return newExits;
	}

	@Override
	public int compareTo(FormattedStationExit formattedStationExit) {
		return equals(formattedStationExit) ? 0 : name.compareTo(formattedStationExit.name);
	}
}
