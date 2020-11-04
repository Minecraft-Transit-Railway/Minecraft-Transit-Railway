package mtr.gui;

import io.github.cottonmc.cotton.gui.widget.WBox;
import io.github.cottonmc.cotton.gui.widget.data.Axis;

import java.util.List;

public abstract class WidgetListBase<T> extends WBox {

	public WidgetListBase(int width) {
		super(Axis.VERTICAL);
		spacing = 0;
		this.width = width;
	}

	public void refreshList(List<T> listMain, String button1Path, OnClick<T> onButton1, String button2Path, OnClick<T> onButton2, String button3Path, OnClick<T> onButton3, Runnable childCallback) {
		refreshList(listMain, button1Path, onButton1, button2Path, onButton2, button3Path, onButton3, null, null, childCallback);
	}

	public <U> void refreshList(List<T> listMain, Runnable upDownOnSave, List<U> upDownList) {
		refreshList(listMain, null, null, null, null, null, null, upDownOnSave, upDownList, null);
	}

	private <U> void refreshList(List<T> listMain, String button1Path, OnClick<T> onButton1, String button2Path, OnClick<T> onButton2, String button3Path, OnClick<T> onButton3, Runnable upDownOnSave, List<U> upDownList, Runnable childCallback) {
		children.clear();

		final int listSize = listMain.size();
		for (int i = 0; i < listSize; i++) {
			final T item = listMain.get(i);
			final WidgetNameColor panel;
			if (upDownOnSave == null || upDownList == null) {
				panel = new WidgetNameColor(width, getName(item), getColor(item), button1Path, button2Path, button3Path);
				panel.setOnClick(() -> onButton1.onClick(item), () -> onButton2.onClick(item), () -> onButton3.onClick(item));
			} else {
				panel = createUpDownPanel(width, getName(item), getColor(item), upDownList, upDownOnSave, i, listSize);
			}
			add(panel);
			addChildren(item, childCallback);
		}
	}

	protected abstract String getName(T item);

	protected abstract int getColor(T item);

	protected void addChildren(T mainItem, Runnable childCallback) {
	}

	protected static <T> WidgetNameColor createUpDownPanel(int width, String name, int color, List<T> list, Runnable callback, int index, int size) {
		WidgetNameColor panel = new WidgetNameColor(width, name, color, "icon_up", "icon_down", "icon_delete");
		panel.setOnClick(index == 0 ? null : () -> {
			T aboveItem = list.get(index - 1);
			T thisItem = list.get(index);
			list.set(index - 1, thisItem);
			list.set(index, aboveItem);
			callback.run();
		}, index == size - 1 ? null : () -> {
			T thisItem = list.get(index);
			T belowItem = list.get(index + 1);
			list.set(index, belowItem);
			list.set(index + 1, thisItem);
			callback.run();
		}, () -> {
			list.remove(index);
			callback.run();
		});
		return panel;
	}

	@FunctionalInterface
	public interface OnClick<T> {
		void onClick(T t);
	}
}
