package org.mtr.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import org.mtr.tool.Drawing;
import org.mtr.tool.GuiHelper;

import java.util.function.Consumer;

public abstract class PopupWidgetBase extends ClickableWidgetBase {

	protected final ButtonGroupWidget buttonGroup;
	private final Runnable applyBlur;

	public PopupWidgetBase(int minWidth, Runnable applyBlur, String... messages) {
		this.applyBlur = applyBlur;
		buttonGroup = new ButtonGroupWidget(minWidth, this::onClickAction, messages);
		setWidth(buttonGroup.getWidth());
	}

	@Override
	public void init(Consumer<ClickableWidgetBase> addDrawableChild) {
		addDrawableChild.accept(buttonGroup);
	}

	@Override
	protected final void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
		buttonGroup.active = active;
		buttonGroup.visible = visible;

		if (active && visible) {
			setWidth(buttonGroup.getWidth());
			setWidgetHeight();
			buttonGroup.setPosition(getX(), getY() + height - GuiHelper.DEFAULT_LINE_SIZE);
			applyBlur.run();
			buttonGroup.renderWidget(context, mouseX, mouseY, delta);

			// Draw background
			final Drawing drawing = new Drawing(context.getMatrices(), MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers().getBuffer(RenderLayer.getGui()));
			GuiHelper.drawShadow(drawing, getX(), getY(), getX() + width, getY() + height, 0, 8, -1);
			drawing.setVerticesWH(getX(), getY(), width, height - GuiHelper.DEFAULT_LINE_SIZE).setColor(GuiHelper.BACKGROUND_COLOR).draw();
			drawing.setVerticesWH(getX(), getY() + height - GuiHelper.DEFAULT_LINE_SIZE, width, 1).setColor(GuiHelper.BACKGROUND_ACCENT_COLOR).draw();

			render(context, mouseX, mouseY);
		}
	}

	protected abstract void render(DrawContext context, int mouseX, int mouseY);

	protected abstract void onClickAction(int index);

	protected abstract void setWidgetHeight();
}
