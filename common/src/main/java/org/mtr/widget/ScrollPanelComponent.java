package org.mtr.widget;

import gg.essential.elementa.UIComponent;
import gg.essential.elementa.components.ScrollComponent;
import gg.essential.elementa.components.UIContainer;
import gg.essential.elementa.constraints.*;
import gg.essential.universal.UMatrixStack;

public final class ScrollPanelComponent extends UIContainer {

	public static final int SCROLL_BAR_WIDTH = 4;
	private static final int SCROLL_BAR_PADDING = 1;

	public final ScrollComponent contentContainer = (ScrollComponent) new ScrollComponent()
			.setChildOf(this)
			.setWidth(new FillConstraint())
			.setHeight(new RelativeConstraint());

	public ScrollPanelComponent(boolean autoHideScrollbar) {
		final UIContainer scrollBarContainer = (UIContainer) new UIContainer()
				.setChildOf(this)
				.setX(new SiblingConstraint())
				.setWidth(new ChildBasedSizeConstraint())
				.setHeight(new RelativeConstraint());

		final ScrollBarComponent scrollBarComponent = (ScrollBarComponent) new ScrollBarComponent()
				.setChildOf(scrollBarContainer)
				.setWidth(new PixelConstraint(SCROLL_BAR_WIDTH));

		contentContainer.setVerticalScrollBarComponent(scrollBarComponent, autoHideScrollbar);
	}

	private static class ScrollBarComponent extends UIComponent {

		private boolean highlighted = false;

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
			final int a = highlighted ? 0x99 : 0x33;
			ImageComponentBase.drawRectangle(vertexConsumer -> {
				vertexConsumer.pos(matrixStack, x1, y2, 0).color(0, 0, 0, a).endVertex();
				vertexConsumer.pos(matrixStack, x2, y2, 0).color(0, 0, 0, a).endVertex();
				vertexConsumer.pos(matrixStack, x2, y1, 0).color(0, 0, 0, a).endVertex();
				vertexConsumer.pos(matrixStack, x1, y1, 0).color(0, 0, 0, a).endVertex();
			}, true);
			super.draw(matrixStack);
		}
	}
}
