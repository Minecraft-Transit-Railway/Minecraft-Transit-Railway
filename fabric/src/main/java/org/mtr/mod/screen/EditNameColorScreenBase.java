package org.mtr.mod.screen;

import org.mtr.core.data.NameColorDataBase;
import org.mtr.core.tool.Utilities;
import org.mtr.mapping.holder.ClickableWidget;
import org.mtr.mapping.holder.MutableText;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.ScreenExtension;
import org.mtr.mapping.mapper.TextFieldWidgetExtension;
import org.mtr.mapping.tool.TextCase;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.data.IGui;
import org.mtr.mod.generated.lang.TranslationProvider;

public abstract class EditNameColorScreenBase<T extends NameColorDataBase> extends MTRScreenBase implements Utilities, IGui {

	private int nameStart;
	private int colorStart;
	private int colorEnd;

	protected final T data;
	private final MutableText nameText;
	private final MutableText colorText;

	private final TextFieldWidgetExtension textFieldName;
	private final WidgetColorSelector colorSelector;

	public EditNameColorScreenBase(T data, TranslationProvider.TranslationHolder nameKey, TranslationProvider.TranslationHolder colorKey, ScreenExtension previousScreenExtension) {
		super(previousScreenExtension);
		this.data = data;
		nameText = nameKey.getMutableText();
		colorText = colorKey.getMutableText();

		textFieldName = new TextFieldWidgetExtension(0, 0, 0, SQUARE_SIZE, 1024, TextCase.DEFAULT, null, null);
		colorSelector = new WidgetColorSelector(this, true, () -> {
		});
	}

	@Override
	public void tick2() {
		textFieldName.tick2();
	}

	@Override
	public void onClose2() {
		super.onClose2();
		saveData();
	}

	@Override
	public boolean isPauseScreen2() {
		return false;
	}

	protected void setPositionsAndInit(int nameStart, int colorStart, int colorEnd) {
		this.nameStart = nameStart;
		this.colorStart = colorStart;
		this.colorEnd = colorEnd;

		super.init2();
		final int yStart = SQUARE_SIZE + TEXT_FIELD_PADDING / 2;
		IDrawing.setPositionAndWidth(textFieldName, nameStart + TEXT_FIELD_PADDING / 2, yStart, colorStart - nameStart - TEXT_FIELD_PADDING);
		IDrawing.setPositionAndWidth(colorSelector, colorStart + TEXT_FIELD_PADDING / 2, yStart, colorEnd - colorStart - TEXT_FIELD_PADDING);

		textFieldName.setText2(data.getName());
		colorSelector.setColor(data.getColor());

		addChild(new ClickableWidget(textFieldName));
		addChild(new ClickableWidget(colorSelector));
	}

	protected void renderTextFields(GraphicsHolder graphicsHolder) {
		graphicsHolder.drawCenteredText(nameText, (nameStart + colorStart) / 2, TEXT_PADDING, ARGB_WHITE);
		graphicsHolder.drawCenteredText(colorText, (colorStart + colorEnd) / 2, TEXT_PADDING, ARGB_WHITE);
	}

	protected void saveData() {
		data.setName(textFieldName.getText2());
		data.setColor(colorSelector.getColor());
	}
}
