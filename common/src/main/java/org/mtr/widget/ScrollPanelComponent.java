package org.mtr.widget;

import gg.essential.elementa.UIComponent;
import gg.essential.elementa.components.ScrollComponent;
import gg.essential.elementa.components.UIContainer;
import gg.essential.elementa.constraints.*;
import gg.essential.universal.UMatrixStack;

public final class ScrollPanelComponent extends UIContainer {

	public final ScrollComponent contentContainer = (ScrollComponent) new ScrollComponent()
			.setChildOf(this)
			.setWidth(new FillConstraint())
			.setHeight(new RelativeConstraint());

	public ScrollPanelComponent() {
		final UIContainer scrollBarContainer = (UIContainer) new UIContainer()
				.setChildOf(this)
				.setX(new SiblingConstraint())
				.setWidth(new ChildBasedSizeConstraint())
				.setHeight(new RelativeConstraint());

		final ScrollBarComponent scrollBarComponent = (ScrollBarComponent) new ScrollBarComponent()
				.setChildOf(scrollBarContainer)
				.setWidth(new PixelConstraint(0));

		contentContainer.setVerticalScrollBarComponent(scrollBarComponent, true);
		onMouseEnterRunnable(() -> scrollBarComponent.visible = true);
		onMouseLeaveRunnable(() -> scrollBarComponent.visible = false);
	}

	private static class ScrollBarComponent extends UIComponent {

		private boolean visible = false;

		@Override
		public void draw(UMatrixStack matrixStack) {
			beforeDrawCompat(matrixStack);
			if (visible) {
				final float x1 = getLeft() + 2;
				final float y1 = getTop();
				final float x2 = x1 + 2;
				final float y2 = getBottom();
				ImageComponentBase.drawRectangle(vertexConsumer -> {
					vertexConsumer.pos(matrixStack, x1, y2, 0).color(0, 0, 0, 0x33).endVertex();
					vertexConsumer.pos(matrixStack, x2, y2, 0).color(0, 0, 0, 0x33).endVertex();
					vertexConsumer.pos(matrixStack, x2, y1, 0).color(0, 0, 0, 0x33).endVertex();
					vertexConsumer.pos(matrixStack, x1, y1, 0).color(0, 0, 0, 0x33).endVertex();
				}, true);
			}
			super.draw(matrixStack);
		}
	}
}
