package mtr.gui;

import io.github.cottonmc.cotton.gui.widget.WBox;
import io.github.cottonmc.cotton.gui.widget.data.Axis;
import mtr.data.NamedColoredBase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public abstract class WidgetListBase<T extends NamedColoredBase> extends WBox implements IGui {

	public WidgetListBase(int width) {
		super(Axis.VERTICAL);
		spacing = 0;
		this.width = width;
	}

	protected void refreshList(Set<T> setMain, Set<? extends NamedColoredBase> setChildren, String button1Path, OnClick<T> onButton1, String button2Path, OnClick<T> onButton2, String button3Path, OnClick<T> onButton3, Runnable childCallback) {
		children.clear();
		List<T> listSortedMain = new ArrayList<>(setMain);
		Collections.sort(listSortedMain);

		for (T t : listSortedMain) {
			WidgetNameColor panel = new WidgetNameColor(width, t.name, t.color, button1Path, button2Path, button3Path);
			panel.setOnClick(() -> onButton1.onClick(t), () -> onButton2.onClick(t), () -> onButton3.onClick(t));
			add(panel);

			addChildren(t, setChildren, childCallback);
		}
	}

	protected void addChildren(T mainItem, Set<? extends NamedColoredBase> setChildren, Runnable childCallback) {
	}

	@FunctionalInterface
	public interface OnClick<T> {
		void onClick(T t);
	}
}
