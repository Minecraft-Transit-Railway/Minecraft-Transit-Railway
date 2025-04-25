package org.mtr.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.MutableText;
import org.mtr.client.IDrawing;
import org.mtr.core.data.NameColorDataBase;
import org.mtr.core.tool.Utilities;
import org.mtr.data.IGui;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.widget.BetterTextFieldWidget;
import org.mtr.widget.ColorSelectorWidget;

public abstract class EditNameColorScreenBase<T extends NameColorDataBase> extends MTRScreenBase implements Utilities, IGui {

	private int nameStart;
	private int colorStart;
	private int colorEnd;

	protected final T data;
	private final MutableText nameText;
	private final MutableText colorText;

	private final BetterTextFieldWidget textFieldName;
	private final ColorSelectorWidget colorSelector;

	public EditNameColorScreenBase(T data, TranslationProvider.TranslationHolder nameKey, TranslationProvider.TranslationHolder colorKey, Screen previousScreen) {
		super(previousScreen);
		this.data = data;
		nameText = nameKey.getMutableText();
		colorText = colorKey.getMutableText();

		textFieldName = new BetterTextFieldWidget(1024, TextCase.DEFAULT, null, null);
		colorSelector = new ColorSelectorWidget(this, true, () -> {
		});
	}

	@Override
	public void close() {
		super.close();
		saveData();
	}

	@Override
	public boolean shouldPause() {
		return false;
	}

	protected void setPositionsAndInit(int nameStart, int colorStart, int colorEnd) {
		this.nameStart = nameStart;
		this.colorStart = colorStart;
		this.colorEnd = colorEnd;

		super.init();
		final int yStart = SQUARE_SIZE + TEXT_FIELD_PADDING / 2;
		IDrawing.setPositionAndWidth(textFieldName, nameStart + TEXT_FIELD_PADDING / 2, yStart, colorStart - nameStart - TEXT_FIELD_PADDING);
		IDrawing.setPositionAndWidth(colorSelector, colorStart + TEXT_FIELD_PADDING / 2, yStart, colorEnd - colorStart - TEXT_FIELD_PADDING);

		textFieldName.setText(data.getName());
		colorSelector.setColor(data.getColor());

		addDrawableChild(textFieldName);
		addDrawableChild(colorSelector);
	}

	protected void renderTextFields(DrawContext context) {
		context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, nameText, (nameStart + colorStart) / 2, TEXT_PADDING, ARGB_WHITE);
		context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, colorText, (colorStart + colorEnd) / 2, TEXT_PADDING, ARGB_WHITE);
	}

	protected void saveData() {
		data.setName(textFieldName.getText());
		data.setColor(colorSelector.getColor());
	}
}
