package org.mtr.mod.screen;

import org.mtr.core.data.AreaBase;
import org.mtr.core.data.SavedRailBase;
import org.mtr.core.data.TransportMode;
import org.mtr.mapping.holder.ClickableWidget;
import org.mtr.mapping.holder.MutableText;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.ScreenExtension;
import org.mtr.mapping.mapper.TextFieldWidgetExtension;
import org.mtr.mapping.tool.TextCase;
import org.mtr.mod.Init;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.data.IGui;
import org.mtr.mod.generated.lang.TranslationProvider;

public abstract class SavedRailScreenBase<T extends SavedRailBase<T, U>, U extends AreaBase<U, T>> extends MTRScreenBase implements IGui {

	protected final T savedRailBase;
	protected final int textWidth;
	protected final boolean showScheduleControls;
	protected final WidgetShorterSlider sliderDwellTimeMin;
	protected final WidgetShorterSlider sliderDwellTimeSec;

	private final TextFieldWidgetExtension textFieldSavedRailNumber;

	private final MutableText savedRailNumberText;

	protected static final int SECONDS_PER_MINUTE = 60;
	private static final int MAX_DWELL_TIME = 1200;
	private static final int MAX_SAVED_RAIL_NUMBER_LENGTH = 10;

	public SavedRailScreenBase(T savedRailBase, TransportMode transportMode, ScreenExtension previousScreenExtension, MutableText... additionalTexts) {
		super(previousScreenExtension);
		this.savedRailBase = savedRailBase;
		showScheduleControls = !transportMode.continuousMovement;
		savedRailNumberText = getNumberStringKey().getMutableText();

		textFieldSavedRailNumber = new TextFieldWidgetExtension(0, 0, 0, SQUARE_SIZE, MAX_SAVED_RAIL_NUMBER_LENGTH, TextCase.DEFAULT, null, "1");

		int additionalTextWidths = 0;
		for (final MutableText additionalText : additionalTexts) {
			additionalTextWidths = Math.max(additionalTextWidths, GraphicsHolder.getTextWidth(additionalText));
		}
		textWidth = Math.max(GraphicsHolder.getTextWidth(savedRailNumberText), additionalTextWidths) + TEXT_PADDING;

		sliderDwellTimeMin = new WidgetShorterSlider(0, 0, (int) Math.floor(MAX_DWELL_TIME / 2F / SECONDS_PER_MINUTE), TranslationProvider.GUI_MTR_ARRIVAL_MIN::getString, null);
		sliderDwellTimeSec = new WidgetShorterSlider(0, 0, SECONDS_PER_MINUTE * 2 - 1, 10, 2, value -> TranslationProvider.GUI_MTR_ARRIVAL_SEC.getString(value / 2F), null);
	}

	@Override
	protected void init2() {
		super.init2();

		IDrawing.setPositionAndWidth(textFieldSavedRailNumber, SQUARE_SIZE + textWidth + TEXT_FIELD_PADDING / 2, SQUARE_SIZE + TEXT_FIELD_PADDING / 2, width - textWidth - SQUARE_SIZE * 2 - TEXT_FIELD_PADDING);
		textFieldSavedRailNumber.setText2(savedRailBase.getName());
		textFieldSavedRailNumber.setChangedListener2(text -> savedRailBase.setName(textFieldSavedRailNumber.getText2()));

		final int sliderTextWidth = Math.max(GraphicsHolder.getTextWidth(TranslationProvider.GUI_MTR_ARRIVAL_MIN.getMutableText("88")), GraphicsHolder.getTextWidth(TranslationProvider.GUI_MTR_ARRIVAL_SEC.getString("88.8"))) + TEXT_PADDING;
		sliderDwellTimeMin.setX2(SQUARE_SIZE + textWidth);
		sliderDwellTimeMin.setHeight(SQUARE_SIZE / 2);
		sliderDwellTimeMin.setWidth2(width - textWidth - SQUARE_SIZE * 2 - sliderTextWidth);

		sliderDwellTimeSec.setX2(SQUARE_SIZE + textWidth);
		sliderDwellTimeSec.setHeight(SQUARE_SIZE / 2);
		sliderDwellTimeSec.setWidth2(width - textWidth - SQUARE_SIZE * 2 - sliderTextWidth);

		addChild(new ClickableWidget(textFieldSavedRailNumber));
		if (showScheduleControls) {
			addChild(new ClickableWidget(sliderDwellTimeMin));
			addChild(new ClickableWidget(sliderDwellTimeSec));
		}
	}

	@Override
	public void tick2() {
		textFieldSavedRailNumber.tick2();
		textFieldSavedRailNumber.setX2(SQUARE_SIZE + textWidth + TEXT_FIELD_PADDING / 2);

		final int maxMin = (int) Math.floor(MAX_DWELL_TIME / 2F / SECONDS_PER_MINUTE);
		if (sliderDwellTimeMin.getIntValue() == 0 && sliderDwellTimeSec.getIntValue() == 0) {
			sliderDwellTimeSec.setValue(1);
		}
		if (sliderDwellTimeMin.getIntValue() == maxMin && sliderDwellTimeSec.getIntValue() > MAX_DWELL_TIME % (SECONDS_PER_MINUTE * 2)) {
			sliderDwellTimeSec.setValue(MAX_DWELL_TIME % (SECONDS_PER_MINUTE * 2));
		}
	}

	@Override
	public void render(GraphicsHolder graphicsHolder, int mouseX, int mouseY, float delta) {
		try {
			renderBackground(graphicsHolder);
			graphicsHolder.drawText(savedRailNumberText, SQUARE_SIZE, SQUARE_SIZE + TEXT_FIELD_PADDING / 2 + TEXT_PADDING, ARGB_WHITE, false, GraphicsHolder.getDefaultLight());
			super.render(graphicsHolder, mouseX, mouseY, delta);
		} catch (Exception e) {
			Init.LOGGER.error("", e);
		}
	}

	@Override
	public boolean isPauseScreen2() {
		return false;
	}

	protected abstract TranslationProvider.TranslationHolder getNumberStringKey();
}
