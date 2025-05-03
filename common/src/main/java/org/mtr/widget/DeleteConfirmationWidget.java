package org.mtr.widget;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectIntImmutablePair;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import org.mtr.data.IGui;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.tool.GuiHelper;

import javax.annotation.Nullable;

public final class DeleteConfirmationWidget extends PopupWidgetBase {

	private Runnable deleteCallback;
	private final Runnable onDismiss;
	private final ObjectArrayList<ObjectIntImmutablePair<OrderedText>> mainTextLines = new ObjectArrayList<>();

	public DeleteConfirmationWidget(int minWidth, Runnable onDismiss, Runnable applyBlur) {
		super(minWidth, applyBlur, Text.translatable("gui.yes").getString(), Text.translatable("gui.no").getString());
		this.onDismiss = onDismiss;
	}

	@Override
	protected void render(DrawContext context, int mouseX, int mouseY) {
		visible = deleteCallback != null;

		// Draw main text
		final TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
		for (int i = 0; i < mainTextLines.size(); i++) {
			final ObjectIntImmutablePair<OrderedText> lineDetails = mainTextLines.get(i);
			context.drawText(textRenderer, lineDetails.left(), getX() + width / 2 - lineDetails.rightInt() / 2, getY() + GuiHelper.DEFAULT_PADDING + i * GuiHelper.MINECRAFT_TEXT_LINE_HEIGHT, GuiHelper.WHITE_COLOR, false);
		}
	}

	@Override
	protected void onClickAction(int index) {
		switch (index) {
			case 0 -> {
				if (deleteCallback != null) {
					deleteCallback.run();
				}
				setDeleteCallbackInternal(null, null);
			}
			case 1 -> setDeleteCallbackInternal(null, null);
		}
	}

	@Override
	protected void setWidgetHeight() {
		setHeight(GuiHelper.DEFAULT_PADDING * 2 + GuiHelper.MINECRAFT_TEXT_LINE_HEIGHT * (mainTextLines.size() - 1) + GuiHelper.MINECRAFT_FONT_SIZE + GuiHelper.DEFAULT_LINE_SIZE);
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
			textRenderer.wrapLines(TranslationProvider.GUI_MTR_DELETE_CONFIRMATION.getMutableText(IGui.formatStationName(name)), width - GuiHelper.DEFAULT_PADDING * 2).forEach(line -> mainTextLines.add(new ObjectIntImmutablePair<>(line, textRenderer.getWidth(line))));
			setWidgetHeight();
		}
		visible = this.deleteCallback != null;
	}
}
