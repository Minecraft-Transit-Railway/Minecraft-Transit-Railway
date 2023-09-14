package org.mtr.mod.screen;

import org.mtr.core.data.NameColorDataBase;

import java.util.Locale;

public class DashboardListItem implements Comparable<DashboardListItem> {

	public final long id;
	public final String name;
	public final int color;
	public final NameColorDataBase data;

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

	private String combineNameColorId() {
		return (name + color + id).toLowerCase(Locale.ENGLISH);
	}

	@Override
	public int compareTo(DashboardListItem compare) {
		return combineNameColorId().compareTo(compare.combineNameColorId());
	}
}
