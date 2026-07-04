package org.mtr.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import org.jspecify.annotations.Nullable;
import org.mtr.data.IGui;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectIntImmutablePair;
import org.mtr.tool.GuiHelper;

public final class DeleteConfirmationWidget extends PopupWidgetBase {

	@Nullable
	private Runnable deleteCallback;
	private final Runnable onDismiss;
	private final ObjectArrayList<ObjectIntImmutablePair<FormattedCharSequence>> mainTextLines = new ObjectArrayList<>();

	public DeleteConfirmationWidget(int minWidth, Runnable onDismiss, Runnable applyBlur) {
		super(minWidth, applyBlur, Component.translatable("gui.yes").getString(), Component.translatable("gui.no").getString());
		this.onDismiss = onDismiss;
	}

	@Override
	protected void render(GuiGraphics context, int mouseX, int mouseY) {
		visible = deleteCallback != null;

		// Draw main text
		final Font textRenderer = Minecraft.getInstance().font;
		for (int i = 0; i < mainTextLines.size(); i++) {
			final ObjectIntImmutablePair<FormattedCharSequence> lineDetails = mainTextLines.get(i);
			context.drawString(textRenderer, lineDetails.left(), getX() + width / 2 - lineDetails.rightInt() / 2, getY() + GuiHelper.DEFAULT_PADDING + i * GuiHelper.MINECRAFT_TEXT_LINE_HEIGHT, GuiHelper.WHITE_COLOR, false);
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
			final Font textRenderer = Minecraft.getInstance().font;
			textRenderer.split(TranslationProvider.GUI_MTR_DELETE_CONFIRMATION.getMutableText(IGui.formatStationName(name)), width - GuiHelper.DEFAULT_PADDING * 2).forEach(line -> mainTextLines.add(new ObjectIntImmutablePair<>(line, textRenderer.width(line))));
			setWidgetHeight();
		}
		visible = this.deleteCallback != null;
	}
}
