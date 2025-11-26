package org.mtr.widget;

import gg.essential.elementa.components.UIContainer;
import gg.essential.elementa.components.Window;
import gg.essential.elementa.constraints.*;
import gg.essential.universal.UMatrixStack;
import gg.essential.universal.utils.ReleasedDynamicTexture;
import it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import org.mtr.tool.ReleasedDynamicTextureRegistry;

/**
 * Renders a generic Minecraft background. The background will have a maximum size with a padding from the window edges.
 */
public final class BackgroundComponent extends StitchedImageComponent {

	private int selectedTab;
	public final UIContainer[] containers;
	private final ObjectImmutableList<ObjectObjectImmutablePair<ReleasedDynamicTexture, String>> tabs;

	private static final int TEXTURE_BORDER = 4;
	private static final int INNER_PADDING = 8;
	private static final int OUTER_PADDING = 40;
	private static final int TAB_WIDTH = 26;
	private static final int TAB_HEIGHT = 32;
	private static final int TAB_ICON_SIZE = 16;

	public BackgroundComponent(Window parent, ObjectImmutableList<ObjectObjectImmutablePair<ReleasedDynamicTexture, String>> tabs) {
		super(256, 256, 176, 222, TEXTURE_BORDER, -INNER_PADDING, 6, 6, 170, 16, ReleasedDynamicTextureRegistry.BACKGROUND_TEXTURE.get());
		containers = new UIContainer[tabs.size()];
		this.tabs = tabs;

		for (int i = 0; i < tabs.size(); i++) {
			containers[i] = (UIContainer) new UIContainer()
					.setChildOf(this)
					.setWidth(new RelativeConstraint())
					.setHeight(new RelativeConstraint());
		}

		setChildOf(parent);
		setWidth(new CoerceAtMostConstraint(new SubtractiveConstraint(new RelativeConstraint(), new PixelConstraint(OUTER_PADDING * 2)), new PixelConstraint(320)));
		setHeight(new CoerceAtMostConstraint(new SubtractiveConstraint(new RelativeConstraint(), new PixelConstraint(OUTER_PADDING * 2)), new PixelConstraint(240)));
		setX(new CenterConstraint());
		setY(new CenterConstraint());
		updateContainers();

		parent.onMouseClickConsumer(clickEvent -> {
			final float x = (clickEvent.getAbsoluteX() - getLeft() + INNER_PADDING) / TAB_WIDTH;
			final float y = (clickEvent.getAbsoluteY() - getTop() + INNER_PADDING) / TAB_HEIGHT;

			if (x >= 0 && x < tabs.size() && y >= -1 && y < 0) {
				final int newTab = (int) Math.floor(x);
				if (selectedTab != newTab) {
					selectedTab = newTab;
					updateContainers();
				}
			}
		});
	}

	@Override
	public void draw(UMatrixStack matrixStack) {
		matrixStack.push();

		// Deselected tabs
		for (int i = 0; i < tabs.size(); i++) {
			if (i != selectedTab) {
				final int currentTab = i;
				drawTexture(i == 0 ? ReleasedDynamicTextureRegistry.TAB_LEFT_TEXTURE.get() : ReleasedDynamicTextureRegistry.TAB_MIDDLE_TEXTURE.get(), vertexConsumer -> {
					final float x = getLeft() - INNER_PADDING + currentTab * TAB_WIDTH;
					final float y = getTop() - TEXTURE_BORDER;
					drawTexturedQuad(matrixStack, vertexConsumer, x, y - TAB_HEIGHT, x + TAB_WIDTH, y, 0, 0, 1, 1);
				});
			}
		}

		matrixStack.pop();
		super.draw(matrixStack);
		matrixStack.push();

		// Selected tab
		if (!tabs.isEmpty()) {
			drawTexture(selectedTab == 0 ? ReleasedDynamicTextureRegistry.TAB_LEFT_SELECTED_TEXTURE.get() : ReleasedDynamicTextureRegistry.TAB_MIDDLE_SELECTED_TEXTURE.get(), vertexConsumer -> {
				final float x = getLeft() - INNER_PADDING + selectedTab * TAB_WIDTH;
				final float y = getTop() - TEXTURE_BORDER;
				drawTexturedQuad(matrixStack, vertexConsumer, x, y - TAB_HEIGHT, x + TAB_WIDTH, y, 0, 0, 1, 1);
			});
		}

		// Tab icons
		for (int i = 0; i < tabs.size(); i++) {
			final int currentTab = i;
			drawTexture(tabs.get(i).left(), vertexConsumer -> {
				final int texturePadding = (TAB_WIDTH - TAB_ICON_SIZE) / 2;
				final float x = getLeft() - INNER_PADDING + currentTab * TAB_WIDTH + texturePadding;
				final float y = getTop() - TAB_HEIGHT - 2 + texturePadding;
				drawTexturedQuad(matrixStack, vertexConsumer, x, y, x + TAB_ICON_SIZE, y + TAB_ICON_SIZE, 0, 0, 1, 1);
			});
		}

		matrixStack.pop();
	}

	private void updateContainers() {
		for (int i = 0; i < tabs.size(); i++) {
			if (i == selectedTab) {
				containers[i].unhide(true);
			} else {
				containers[i].hide(true);
			}
		}
	}
}
