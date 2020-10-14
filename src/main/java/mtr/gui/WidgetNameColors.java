package mtr.gui;

import io.github.cottonmc.cotton.gui.widget.WBox;
import io.github.cottonmc.cotton.gui.widget.data.Axis;
import mtr.data.NamedColoredBase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class WidgetNameColors<T extends NamedColoredBase> extends WBox implements IGui {

	public WidgetNameColors(int width) {
		super(Axis.VERTICAL);
		spacing = 0;
		this.width = width;
	}

	public void refreshList(Set<T> ts, OnClick<T> onFind, OnClick<T> onEdit, OnClick<T> onDelete) {
		children.clear();
		List<T> tSorted = new ArrayList<>(ts);
		Collections.sort(tSorted);
		for (T t : tSorted) {
			WidgetNameColor<T> panel = new WidgetNameColor<>(width, t);
			panel.setOnClick(() -> onFind.onClick(t), () -> onEdit.onClick(t), () -> onDelete.onClick(t));
			add(panel);
		}
	}

	@FunctionalInterface
	public interface OnClick<T extends NamedColoredBase> {
		void onClick(T t);
	}
}
