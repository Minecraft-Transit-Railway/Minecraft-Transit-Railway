package org.mtr.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.MutableText;
import org.mtr.MTR;
import org.mtr.client.IDrawing;
import org.mtr.core.data.AreaBase;
import org.mtr.core.data.SavedRailBase;
import org.mtr.core.data.TransportMode;
import org.mtr.data.IGui;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.widget.BetterTextFieldWidget;
import org.mtr.widget.ShorterSliderWidget;

public abstract class SavedRailScreenBase<T extends SavedRailBase<T, U>, U extends AreaBase<U, T>> extends ScreenBase implements IGui {

	protected final T savedRailBase;
	protected final int textWidth;
	protected final boolean showScheduleControls;
	protected final ShorterSliderWidget sliderDwellTimeMin;
	protected final ShorterSliderWidget sliderDwellTimeSec;

	private final BetterTextFieldWidget textFieldSavedRailNumber;

	private final MutableText savedRailNumberText;

	protected static final int SECONDS_PER_MINUTE = 60;
	private static final int MAX_DWELL_TIME = 1200;
	private static final int MAX_SAVED_RAIL_NUMBER_LENGTH = 10;

	public SavedRailScreenBase(T savedRailBase, TransportMode transportMode, Screen previousScreen, MutableText... additionalTexts) {
		super(previousScreen);
		this.savedRailBase = savedRailBase;
		showScheduleControls = !transportMode.continuousMovement;
		savedRailNumberText = getNumberStringKey().getMutableText();

		textFieldSavedRailNumber = new BetterTextFieldWidget(MAX_SAVED_RAIL_NUMBER_LENGTH, TextCase.DEFAULT, null, "1", savedRailBase::setName);

		int additionalTextWidths = 0;
		for (final MutableText additionalText : additionalTexts) {
			additionalTextWidths = Math.max(additionalTextWidths, textRenderer.getWidth(additionalText));
		}
		textWidth = Math.max(textRenderer.getWidth(savedRailNumberText), additionalTextWidths) + TEXT_PADDING;

		sliderDwellTimeMin = new ShorterSliderWidget(0, 0, (int) Math.floor(MAX_DWELL_TIME / 2F / SECONDS_PER_MINUTE), TranslationProvider.GUI_MTR_ARRIVAL_MIN::getString, null);
		sliderDwellTimeSec = new ShorterSliderWidget(0, 0, SECONDS_PER_MINUTE * 2 - 1, 10, 2, value -> TranslationProvider.GUI_MTR_ARRIVAL_SEC.getString(value / 2F), null);
	}

	@Override
	protected void init() {
		super.init();

		IDrawing.setPositionAndWidth(textFieldSavedRailNumber, SQUARE_SIZE + textWidth + TEXT_FIELD_PADDING / 2, SQUARE_SIZE + TEXT_FIELD_PADDING / 2, width - textWidth - SQUARE_SIZE * 2 - TEXT_FIELD_PADDING);
		textFieldSavedRailNumber.setText(savedRailBase.getName());

		final int sliderTextWidth = Math.max(textRenderer.getWidth(TranslationProvider.GUI_MTR_ARRIVAL_MIN.getMutableText("88")), textRenderer.getWidth(TranslationProvider.GUI_MTR_ARRIVAL_SEC.getString("88.8"))) + TEXT_PADDING;
		sliderDwellTimeMin.setX(SQUARE_SIZE + textWidth);
		sliderDwellTimeMin.setHeight(SQUARE_SIZE / 2);
		sliderDwellTimeMin.setWidth(width - textWidth - SQUARE_SIZE * 2 - sliderTextWidth);

		sliderDwellTimeSec.setX(SQUARE_SIZE + textWidth);
		sliderDwellTimeSec.setHeight(SQUARE_SIZE / 2);
		sliderDwellTimeSec.setWidth(width - textWidth - SQUARE_SIZE * 2 - sliderTextWidth);

		addDrawableChild(textFieldSavedRailNumber);
		if (showScheduleControls) {
			addDrawableChild(sliderDwellTimeMin);
			addDrawableChild(sliderDwellTimeSec);
		}
	}

	@Override
	public void tick() {
		textFieldSavedRailNumber.setX(SQUARE_SIZE + textWidth + TEXT_FIELD_PADDING / 2);

		final int maxMin = (int) Math.floor(MAX_DWELL_TIME / 2F / SECONDS_PER_MINUTE);
		if (sliderDwellTimeMin.getIntValue() == 0 && sliderDwellTimeSec.getIntValue() == 0) {
			sliderDwellTimeSec.setValue(1);
		}
		if (sliderDwellTimeMin.getIntValue() == maxMin && sliderDwellTimeSec.getIntValue() > MAX_DWELL_TIME % (SECONDS_PER_MINUTE * 2)) {
			sliderDwellTimeSec.setValue(MAX_DWELL_TIME % (SECONDS_PER_MINUTE * 2));
		}
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		try {
			renderBackground(context, mouseX, mouseY, delta);
			context.drawText(textRenderer, savedRailNumberText, SQUARE_SIZE, SQUARE_SIZE + TEXT_FIELD_PADDING / 2 + TEXT_PADDING, ARGB_WHITE, false);
			super.render(context, mouseX, mouseY, delta);
		} catch (Exception e) {
			MTR.LOGGER.error("", e);
		}
	}

	@Override
	public boolean shouldPause() {
		return false;
	}

	protected abstract TranslationProvider.TranslationHolder getNumberStringKey();
}
