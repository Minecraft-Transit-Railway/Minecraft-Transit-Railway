package mtr.gui;

import mtr.data.NamedColoredBase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class WidgetSet<T extends NamedColoredBase> extends WidgetListBase<T> {

	public WidgetSet(int width) {
		super(width);
	}

	public void refreshList(Set<T> setMain, String button1Path, OnClick<T> onButton1, String button2Path, OnClick<T> onButton2, String button3Path, OnClick<T> onButton3, Runnable childCallback) {
		List<T> listSortedMain = new ArrayList<>(setMain);
		Collections.sort(listSortedMain);

		refreshList(listSortedMain, button1Path, onButton1, button2Path, onButton2, button3Path, onButton3, childCallback);
	}

	@Override
	protected String getName(T item) {
		return item.name;
	}

	@Override
	protected int getColor(T item) {
		return item.color;
	}
}
