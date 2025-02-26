package org.mtr.mod.screen;

import org.mtr.core.data.NameColorDataBase;

import java.util.Locale;

public class DashboardListItem implements Comparable<DashboardListItem> {

	public final long id;
	public final NameColorDataBase data;
	private final String name;
	private final int color;

	public DashboardListItem(long id, String name, int color) {
		this.id = id;
		this.name = name;
		this.color = color;
		data = null;
	}

	public DashboardListItem(NameColorDataBase data) {
		this.id = data.getId();
		this.name = data.getName();
		this.color = data.getColor();
		this.data = data;
	}

	public String getName(boolean formatted) {
		return name;
	}

	public int getColor(boolean formatted) {
		return color;
	}

	private String combineNameColorId() {
		return (name + color + id).toLowerCase(Locale.ENGLISH);
	}

	@Override
	public int compareTo(DashboardListItem compare) {
		return combineNameColorId().compareTo(compare.combineNameColorId());
	}
}
