package org.mtr.widget;

import gg.essential.elementa.components.UIContainer;
import gg.essential.elementa.components.UIText;
import gg.essential.elementa.constraints.PixelConstraint;
import gg.essential.elementa.constraints.RelativeConstraint;
import gg.essential.elementa.constraints.SiblingConstraint;
import org.jspecify.annotations.Nullable;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import org.mtr.tool.GuiHelper;

import java.awt.*;

public final class MultiLineTextWidget extends UIContainer {

	private final ObjectArrayList<ObjectObjectImmutablePair<UIContainer, ObjectArrayList<UIText>>> containersWithTextObjects = new ObjectArrayList<>();

	private static final Color DEFAULT_COLOR = new Color(GuiHelper.MINECRAFT_GUI_TITLE_TEXT_COLOR);

	public void write(ObjectArrayList<ObjectArrayList<ObjectObjectImmutablePair<String, @Nullable Color>>> lines) {
		while (containersWithTextObjects.size() < lines.size()) {
			containersWithTextObjects.add(new ObjectObjectImmutablePair<>(
				(UIContainer) new UIContainer().setChildOf(this).setWidth(new RelativeConstraint()).setHeight(new PixelConstraint(GuiHelper.MINECRAFT_TEXT_LINE_HEIGHT)),
				new ObjectArrayList<>()
			));
		}

		for (int i = 0; i < lines.size(); i++) {
			final ObjectArrayList<ObjectObjectImmutablePair<String, @Nullable Color>> line = lines.get(i);
			final ObjectObjectImmutablePair<UIContainer, ObjectArrayList<UIText>> containerWithTextObjects = containersWithTextObjects.get(i);
			final UIContainer container = containerWithTextObjects.left();
			final ObjectArrayList<UIText> textObjects = containerWithTextObjects.right();

			while (textObjects.size() < line.size()) {
				textObjects.add((UIText) new UIText("", false).setChildOf(container).setX(new SiblingConstraint()));
			}

			for (int j = 0; j < line.size(); j++) {
				final UIText textObject = textObjects.get(j);
				final Color color = line.get(j).right();
				textObject.setText(line.get(j).left());
				textObject.setColor(color == null ? DEFAULT_COLOR : color);
			}
		}

		for (int i = 0; i < containersWithTextObjects.size(); i++) {
			final ObjectObjectImmutablePair<UIContainer, ObjectArrayList<UIText>> containerWithTextObjects = containersWithTextObjects.get(i);
			if (i < lines.size()) {
				containerWithTextObjects.left().setY(new PixelConstraint(GuiHelper.MINECRAFT_TEXT_LINE_HEIGHT * i));
				for (int j = 0; j < containerWithTextObjects.right().size(); j++) {
					final UIText textObject = containerWithTextObjects.right().get(j);
					if (j < lines.get(i).size()) {
						textObject.unhide(true);
					} else {
						textObject.hide(true);
					}
				}
			} else {
				containerWithTextObjects.left().setY(new PixelConstraint(0));
				for (int j = 0; j < containerWithTextObjects.right().size(); j++) {
					containerWithTextObjects.right().get(j).setText("");
				}
			}
		}

		setHeight(new PixelConstraint(GuiHelper.MINECRAFT_TEXT_LINE_HEIGHT * lines.size()));
	}
}
