package org.mtr.widget;

import gg.essential.elementa.UIComponent;
import gg.essential.elementa.components.ScrollComponent;
import gg.essential.elementa.components.UIContainer;
import gg.essential.elementa.constraints.*;
import gg.essential.universal.UMatrixStack;

import java.awt.*;

public final class ScrollPanelComponent extends UIContainer {

	public static final int SCROLL_BAR_WIDTH = 4;
	private static final int SCROLL_BAR_PADDING = 1;

	public final ScrollComponent contentContainer = (ScrollComponent) new ScrollComponent()
			.setChildOf(this)
			.setWidth(new FillConstraint())
			.setHeight(new RelativeConstraint());

	private final ScrollBarComponent scrollBarComponent;

	public ScrollPanelComponent(boolean autoHideScrollbar) {
		final UIContainer scrollBarContainer = (UIContainer) new UIContainer()
				.setChildOf(this)
				.setX(new SiblingConstraint())
				.setWidth(new ChildBasedSizeConstraint())
				.setHeight(new RelativeConstraint());

		scrollBarComponent = (ScrollBarComponent) new ScrollBarComponent()
				.setChildOf(scrollBarContainer)
				.setWidth(new PixelConstraint(SCROLL_BAR_WIDTH));

		contentContainer.setVerticalScrollBarComponent(scrollBarComponent, autoHideScrollbar);
	}

	public void setScrollbarColor(Color scrollbarColor) {
		scrollBarComponent.scrollbarColor = scrollbarColor;
	}

	private static class ScrollBarComponent extends UIComponent {

		private boolean highlighted = false;
		private Color scrollbarColor = Color.BLACK;

		private ScrollBarComponent() {
			onMouseEnterRunnable(() -> highlighted = true);
			onMouseLeaveRunnable(() -> highlighted = false);
		}

		@Override
		public void draw(UMatrixStack matrixStack) {
			beforeDrawCompat(matrixStack);
			final float x1 = getLeft() + SCROLL_BAR_PADDING;
			final float y1 = getTop();
			final float x2 = getRight() - SCROLL_BAR_PADDING;
			final float y2 = getBottom();
			final Color color = new Color(scrollbarColor.getRed(), scrollbarColor.getGreen(), scrollbarColor.getBlue(), highlighted ? 0x99 : 0x33);
			ImageComponentBase.drawRectangle(vertexConsumer -> {
				vertexConsumer.pos(matrixStack, x1, y2, 0).color(color).endVertex();
				vertexConsumer.pos(matrixStack, x2, y2, 0).color(color).endVertex();
				vertexConsumer.pos(matrixStack, x2, y1, 0).color(color).endVertex();
				vertexConsumer.pos(matrixStack, x1, y1, 0).color(color).endVertex();
			}, true);
			super.draw(matrixStack);
		}
	}
}
