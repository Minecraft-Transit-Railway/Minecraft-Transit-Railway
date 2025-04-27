package org.mtr.widget;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import net.minecraft.util.Identifier;
import org.mtr.tool.Drawing;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public final class ListItem<T> {

	private boolean expanded;

	public final DrawIcon drawIcon;
	public final int iconWidth;
	public final String text;
	@Nullable
	private final ObjectArrayList<ListItem<T>> children;
	@Nullable
	private final T data;
	@Nullable
	private final ObjectArrayList<ObjectObjectImmutablePair<Identifier, Consumer<T>>> actions;

	public static <T> ListItem<T> createParent(DrawIcon drawIcon, int iconWidth, String text, ObjectArrayList<ListItem<T>> children) {
		return new ListItem<>(drawIcon, iconWidth, text, children, null, null);
	}

	public static <T> ListItem<T> createChild(DrawIcon drawIcon, int iconWidth, String text, T data, ObjectArrayList<ObjectObjectImmutablePair<Identifier, Consumer<T>>> actions) {
		return new ListItem<>(drawIcon, iconWidth, text, null, data, actions);
	}

	private ListItem(DrawIcon drawIcon, int iconWidth, String text, @Nullable ObjectArrayList<ListItem<T>> children, @Nullable T data, @Nullable ObjectArrayList<ObjectObjectImmutablePair<Identifier, Consumer<T>>> actions) {
		this.drawIcon = drawIcon;
		this.iconWidth = iconWidth;
		this.text = text;
		this.children = children;
		this.data = data;
		this.actions = actions;
	}

	public boolean isParent() {
		return data == null;
	}

	public boolean isExpanded() {
		return isParent() && expanded;
	}

	public void toggle() {
		expanded = isParent() && !expanded;
	}

	public int actionCount() {
		return actions == null ? 0 : actions.size();
	}

	public void iterateActions(ActionsConsumer<T> consumer) {
		if (actions != null && !isParent()) {
			for (int i = 0; i < actions.size(); i++) {
				final ObjectObjectImmutablePair<Identifier, Consumer<T>> action = actions.get(i);
				consumer.accept(i, action.left(), () -> action.right().accept(data));
			}
		}
	}

	public static <T> void iterateData(ObjectArrayList<ListItem<T>> dataList, ListItemConsumer<T> consumer) {
		iterateData(dataList, consumer, 0, 0);
	}

	private static <T> void iterateData(ObjectArrayList<ListItem<T>> dataList, ListItemConsumer<T> consumer, int index, int level) {
		int newIndex = index;

		for (final ListItem<T> listItem : dataList) {
			if (listItem.isParent()) {
				if (consumer.accept(newIndex, level, listItem)) {
					return;
				}

				newIndex++;

				if (listItem.expanded && listItem.children != null) {
					iterateData(listItem.children, consumer, newIndex, level + 1);
				}
			} else {
				if (consumer.accept(newIndex, level, listItem)) {
					return;
				}

				newIndex++;
			}
		}
	}

	@FunctionalInterface
	public interface DrawIcon {
		void draw(Drawing drawing, int x, double y);
	}

	@FunctionalInterface
	public interface ActionsConsumer<T> {
		void accept(int index, Identifier identifier, Runnable callback);
	}

	@FunctionalInterface
	public interface ListItemConsumer<T> {
		/**
		 * @return {@code true} to stop iteration
		 */
		boolean accept(int index, int level, ListItem<T> listItem);
	}
}
