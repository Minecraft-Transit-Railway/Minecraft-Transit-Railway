package org.mtr.widget;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectIntImmutablePair;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import org.mtr.core.tool.Utilities;
import org.mtr.data.IGui;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.tool.Drawing;
import org.mtr.tool.GuiHelper;

import javax.annotation.Nullable;

public final class DeleteConfirmationWidget extends ClickableWidget {

	private Runnable deleteCallback;
	private Runnable clickAction;
	private final Runnable onDismiss;
	private final Runnable applyBlur;
	private final ObjectArrayList<ObjectIntImmutablePair<OrderedText>> mainTextLines = new ObjectArrayList<>();

	private final Text yesText = Text.translatable("gui.yes");
	private final Text noText = Text.translatable("gui.no");
	private final int yesTextHalfWidth;
	private final int noTextHalfWidth;

	public DeleteConfirmationWidget(Runnable onDismiss, Runnable applyBlur) {
		super(0, 0, 0, 0, Text.empty());
		this.onDismiss = onDismiss;
		this.applyBlur = applyBlur;
		final TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
		yesTextHalfWidth = textRenderer.getWidth(yesText) / 2;
		noTextHalfWidth = textRenderer.getWidth(noText) / 2;
	}

	@Override
	protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
		clickAction = null;
		setWidgetHeight();

		if (deleteCallback != null) {
			final MatrixStack matrixStack = context.getMatrices();
			matrixStack.push();
			matrixStack.translate(0, 0, 100);
			applyBlur.run();

			// Draw background
			final Drawing drawing = new Drawing(matrixStack, MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers().getBuffer(RenderLayer.getGui()));
			GuiHelper.drawShadow(drawing, getX(), getY(), getX() + getWidth(), getY() + getHeight(), 0, 8, -1);
			drawing.setVerticesWH(getX(), getY(), getWidth(), getHeight()).setColor(GuiHelper.BACKGROUND_COLOR).draw();
			drawing.setVerticesWH(getX(), getY() + getHeight() - GuiHelper.DEFAULT_LINE_SIZE, getWidth(), 1).setColor(GuiHelper.BACKGROUND_ACCENT_COLOR).draw();

			// Hover detection
			final float halfWidth = getWidth() / 2F;
			if (Utilities.isBetween(mouseY, getY() + getHeight() - GuiHelper.DEFAULT_LINE_SIZE, getY() + getHeight() - 1)) {
				if (Utilities.isBetween(mouseX, getX(), getX() + halfWidth - 1)) {
					drawing.setVerticesWH(getX(), getY() + getHeight() - GuiHelper.DEFAULT_LINE_SIZE, halfWidth, GuiHelper.DEFAULT_LINE_SIZE).setColor(GuiHelper.HOVER_COLOR).draw();
					clickAction = () -> {
						deleteCallback.run();
						setDeleteCallbackInternal(null, null);
					};
				} else if (Utilities.isBetween(mouseX, getX() + halfWidth, getX() + getWidth() - 1)) {
					drawing.setVerticesWH(getX() + halfWidth, getY() + getHeight() - GuiHelper.DEFAULT_LINE_SIZE, halfWidth, GuiHelper.DEFAULT_LINE_SIZE).setColor(GuiHelper.HOVER_COLOR).draw();
					clickAction = () -> setDeleteCallbackInternal(null, null);
				}
			}

			// Draw main text
			final TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
			for (int i = 0; i < mainTextLines.size(); i++) {
				final ObjectIntImmutablePair<OrderedText> lineDetails = mainTextLines.get(i);
				context.drawText(textRenderer, lineDetails.left(), getX() + getWidth() / 2 - lineDetails.rightInt() / 2, getY() + GuiHelper.DEFAULT_PADDING + i * GuiHelper.MINECRAFT_TEXT_LINE_HEIGHT, GuiHelper.WHITE_COLOR, false);
			}

			// Draw button text
			final int buttonTextYStart = getY() + getHeight() - GuiHelper.DEFAULT_LINE_SIZE + GuiHelper.DEFAULT_PADDING;
			context.drawText(textRenderer, yesText, getX() + getWidth() / 4 - yesTextHalfWidth, buttonTextYStart, GuiHelper.WHITE_COLOR, false);
			context.drawText(textRenderer, noText, getX() + getWidth() * 3 / 4 - noTextHalfWidth, buttonTextYStart, GuiHelper.WHITE_COLOR, false);

			matrixStack.pop();
		}
	}

	@Override
	public void onClick(double mouseX, double mouseY) {
		if (clickAction != null) {
			clickAction.run();
		}
	}

	@Override
	protected void appendClickableNarrations(NarrationMessageBuilder builder) {
	}

	public void setDeleteCallback(Runnable deleteCallback, String name) {
		setDeleteCallbackInternal(deleteCallback, name);
	}

	private void setDeleteCallbackInternal(@Nullable Runnable deleteCallback, @Nullable String name) {
		mainTextLines.clear();
		if (deleteCallback == null || name == null) {
			onDismiss.run();
			this.deleteCallback = null;
		} else {
			this.deleteCallback = deleteCallback;
			final TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
			textRenderer.wrapLines(TranslationProvider.GUI_MTR_DELETE_CONFIRMATION.getMutableText(IGui.formatStationName(name)), getWidth() - GuiHelper.DEFAULT_PADDING * 2).forEach(line -> mainTextLines.add(new ObjectIntImmutablePair<>(line, textRenderer.getWidth(line))));
			setWidgetHeight();
		}
	}

	private void setWidgetHeight() {
		setHeight(GuiHelper.DEFAULT_PADDING * 2 + GuiHelper.MINECRAFT_TEXT_LINE_HEIGHT * (mainTextLines.size() - 1) + GuiHelper.MINECRAFT_FONT_SIZE + GuiHelper.DEFAULT_LINE_SIZE);
	}
}
