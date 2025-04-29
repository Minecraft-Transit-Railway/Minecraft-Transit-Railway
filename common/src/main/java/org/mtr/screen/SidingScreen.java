package org.mtr.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import org.mtr.Icons;
import org.mtr.client.IDrawing;
import org.mtr.client.MinecraftClientData;
import org.mtr.core.data.Depot;
import org.mtr.core.data.Siding;
import org.mtr.core.data.TransportMode;
import org.mtr.core.operation.UpdateDataRequest;
import org.mtr.core.tool.Utilities;
import org.mtr.data.IGui;
import org.mtr.data.RailType;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.packet.PacketUpdateData;
import org.mtr.registry.RegistryClient;
import org.mtr.widget.BetterTextFieldWidget;
import org.mtr.widget.ShorterSliderWidget;

public class SidingScreen extends SavedRailScreenBase<Siding, Depot> implements Icons {

	private final ButtonWidget buttonSelectTrain;
	private final CheckboxWidget buttonUnlimitedTrains;
	private final BetterTextFieldWidget textFieldMaxTrains;
	private final ShorterSliderWidget sliderAccelerationConstant;
	private final ShorterSliderWidget sliderDecelerationConstant;
	private final ShorterSliderWidget sliderDelayedVehicleSpeedIncreasePercentage;
	private final ShorterSliderWidget sliderDelayedVehicleReduceDwellTimePercentage;
	private final CheckboxWidget buttonIsManual;
	private final ShorterSliderWidget sliderMaxManualSpeed;

	private static final MutableText SELECTED_TRAIN_TEXT = TranslationProvider.GUI_MTR_SELECTED_VEHICLE.getMutableText();
	private static final MutableText MAX_TRAINS_TEXT = TranslationProvider.GUI_MTR_MAX_VEHICLES.getMutableText();
	private static final MutableText ACCELERATION_CONSTANT_TEXT = TranslationProvider.GUI_MTR_ACCELERATION.getMutableText();
	private static final MutableText DECELERATION_CONSTANT_TEXT = TranslationProvider.GUI_MTR_DECELERATION.getMutableText();
	private static final MutableText DELAYED_VEHICLE_SPEED_INCREASE_PERCENTAGE_TEXT = TranslationProvider.GUI_MTR_DELAYED_VEHICLE_SPEED_INCREASE_PERCENTAGE.getMutableText();
	private static final MutableText DELAYED_VEHICLE_REDUCE_DWELL_TIME_PERCENTAGE_TEXT = TranslationProvider.GUI_MTR_DELAYED_VEHICLE_REDUCE_DWELL_TIME_PERCENTAGE.getMutableText();
	private static final MutableText MANUAL_TO_AUTOMATIC_TIME = TranslationProvider.GUI_MTR_MANUAL_TO_AUTOMATIC_TIME.getMutableText();
	private static final MutableText MAX_MANUAL_SPEED = TranslationProvider.GUI_MTR_MAX_MANUAL_SPEED.getMutableText();
	private static final int MAX_TRAINS_TEXT_LENGTH = 3;
	private static final int MAX_TRAINS_WIDTH = 80;
	private static final int SLIDER_SCALE = 1000 * 50 * 50;
	private static final float ACCELERATION_UNIT_CONVERSION_1 = 1000 * 1000; // m/ms^2 to m/s^2
	private static final float ACCELERATION_UNIT_CONVERSION_2 = ACCELERATION_UNIT_CONVERSION_1 * 3.6F; // m/ms^2 to km/h/s

	public SidingScreen(Siding siding, TransportMode transportMode, Screen previousScreen) {
		super(siding, transportMode, previousScreen, SELECTED_TRAIN_TEXT, MAX_TRAINS_TEXT, ACCELERATION_CONSTANT_TEXT, DECELERATION_CONSTANT_TEXT, DELAYED_VEHICLE_SPEED_INCREASE_PERCENTAGE_TEXT, DELAYED_VEHICLE_REDUCE_DWELL_TIME_PERCENTAGE_TEXT, MANUAL_TO_AUTOMATIC_TIME, MAX_MANUAL_SPEED);
		buttonSelectTrain = ButtonWidget.builder(Text.translatable("selectWorld.edit"), button -> MinecraftClient.getInstance().setScreen(new VehicleSelectorScreen(savedRailBase, this))).build();
		textFieldMaxTrains = new BetterTextFieldWidget(MAX_TRAINS_TEXT_LENGTH, TextCase.DEFAULT, "\\D", null, this::textFieldMaxTrainsCallback);
		sliderAccelerationConstant = new ShorterSliderWidget(0, MAX_TRAINS_WIDTH, (int) Math.round((Siding.MAX_ACCELERATION - Siding.MIN_ACCELERATION) * SLIDER_SCALE), SidingScreen::accelerationSliderFormatter, null);
		sliderDecelerationConstant = new ShorterSliderWidget(0, MAX_TRAINS_WIDTH, (int) Math.round((Siding.MAX_ACCELERATION - Siding.MIN_ACCELERATION) * SLIDER_SCALE), SidingScreen::accelerationSliderFormatter, null);
		sliderDelayedVehicleSpeedIncreasePercentage = new ShorterSliderWidget(0, MAX_TRAINS_WIDTH, 100, SidingScreen::percentageFormatter, null);
		sliderDelayedVehicleReduceDwellTimePercentage = new ShorterSliderWidget(0, MAX_TRAINS_WIDTH, 100, SidingScreen::percentageFormatter, null);
		buttonIsManual = CheckboxWidget.builder(TranslationProvider.GUI_MTR_IS_MANUAL.getText(), textRenderer).checked(savedRailBase.getIsManual()).callback((checkboxWidget, checked) -> {
			if (checked && !textFieldMaxTrains.getText().equals("1")) {
				textFieldMaxTrains.setText("1");
			}
			setButtons();
		}).build();
		sliderMaxManualSpeed = new ShorterSliderWidget(0, MAX_TRAINS_WIDTH, RailType.DIAMOND.ordinal(), SidingScreen::speedSliderFormatter, null);
		buttonUnlimitedTrains = CheckboxWidget.builder(TranslationProvider.GUI_MTR_UNLIMITED_VEHICLES.getText(), textRenderer).checked(savedRailBase.getIsUnlimited()).callback((checkboxWidget, checked) -> {
			if (checked) {
				IGui.setChecked(buttonIsManual, false);
				setButtons();
			}
			if (checked && !textFieldMaxTrains.getText().isEmpty()) {
				textFieldMaxTrains.setText("");
			} else if (!checked && textFieldMaxTrains.getText().isEmpty()) {
				textFieldMaxTrains.setText("1");
			}
		}).build();
	}

	@Override
	protected void init() {
		super.init();

		IDrawing.setPositionAndWidth(buttonSelectTrain, SQUARE_SIZE + textWidth, SQUARE_SIZE * 2 + TEXT_FIELD_PADDING, width - textWidth - SQUARE_SIZE * 2);
		IDrawing.setPositionAndWidth(buttonUnlimitedTrains, SQUARE_SIZE + textWidth + MAX_TRAINS_WIDTH + TEXT_FIELD_PADDING * 3 / 2, SQUARE_SIZE * 3 + TEXT_FIELD_PADDING * 3 / 2, width - textWidth - SQUARE_SIZE * 2);

		addDrawableChild(buttonSelectTrain);

		IDrawing.setPositionAndWidth(textFieldMaxTrains, SQUARE_SIZE + textWidth + TEXT_FIELD_PADDING / 2, SQUARE_SIZE * 3 + TEXT_FIELD_PADDING * 3 / 2, MAX_TRAINS_WIDTH - TEXT_FIELD_PADDING);
		textFieldMaxTrains.setText(savedRailBase.getIsUnlimited() ? "" : String.valueOf(savedRailBase.getMaxVehicles()));

		sliderAccelerationConstant.setX(SQUARE_SIZE + textWidth);
		sliderAccelerationConstant.setY(SQUARE_SIZE * 4 + TEXT_FIELD_PADDING * 2);
		sliderAccelerationConstant.setHeight(SQUARE_SIZE);
		sliderAccelerationConstant.setValue((int) Math.round((savedRailBase.getAcceleration() - Siding.MIN_ACCELERATION) * SLIDER_SCALE));

		sliderDecelerationConstant.setX(SQUARE_SIZE + textWidth);
		sliderDecelerationConstant.setY(SQUARE_SIZE * 5 + TEXT_FIELD_PADDING * 2);
		sliderDecelerationConstant.setHeight(SQUARE_SIZE);
		sliderDecelerationConstant.setValue((int) Math.round((savedRailBase.getDeceleration() - Siding.MIN_ACCELERATION) * SLIDER_SCALE));

		sliderDelayedVehicleSpeedIncreasePercentage.setX(SQUARE_SIZE + textWidth);
		sliderDelayedVehicleSpeedIncreasePercentage.setY(SQUARE_SIZE * 6 + TEXT_FIELD_PADDING * 2);
		sliderDelayedVehicleSpeedIncreasePercentage.setHeight(SQUARE_SIZE);
		sliderDelayedVehicleSpeedIncreasePercentage.setValue(savedRailBase.getDelayedVehicleSpeedIncreasePercentage());

		sliderDelayedVehicleReduceDwellTimePercentage.setX(SQUARE_SIZE + textWidth);
		sliderDelayedVehicleReduceDwellTimePercentage.setY(SQUARE_SIZE * 7 + TEXT_FIELD_PADDING * 2);
		sliderDelayedVehicleReduceDwellTimePercentage.setHeight(SQUARE_SIZE);
		sliderDelayedVehicleReduceDwellTimePercentage.setValue(savedRailBase.getDelayedVehicleReduceDwellTimePercentage());

		IDrawing.setPositionAndWidth(buttonIsManual, SQUARE_SIZE, SQUARE_SIZE * 8 + TEXT_FIELD_PADDING * 2, width - textWidth - SQUARE_SIZE * 2);

		sliderMaxManualSpeed.setX(SQUARE_SIZE + textWidth);
		sliderMaxManualSpeed.setY(SQUARE_SIZE * 9 + TEXT_FIELD_PADDING * 2);
		sliderMaxManualSpeed.setHeight(SQUARE_SIZE);
		sliderMaxManualSpeed.setValue(0); // TODO

		sliderDwellTimeMin.setY(SQUARE_SIZE * 10 + TEXT_FIELD_PADDING * 2);
		sliderDwellTimeSec.setY(SQUARE_SIZE * 21 / 2 + TEXT_FIELD_PADDING * 2);

		if (showScheduleControls) {
			addDrawableChild(buttonUnlimitedTrains);
			addDrawableChild(textFieldMaxTrains);
			addDrawableChild(sliderAccelerationConstant);
			addDrawableChild(sliderDecelerationConstant);
			addDrawableChild(sliderDelayedVehicleSpeedIncreasePercentage);
			addDrawableChild(sliderDelayedVehicleReduceDwellTimePercentage);
			addDrawableChild(buttonIsManual);
			addDrawableChild(sliderMaxManualSpeed);
		}

		setButtons();
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);
		context.drawText(textRenderer, SELECTED_TRAIN_TEXT, SQUARE_SIZE, SQUARE_SIZE * 2 + TEXT_FIELD_PADDING + TEXT_PADDING, ARGB_WHITE, false);
		if (showScheduleControls) {
			context.drawText(textRenderer, MAX_TRAINS_TEXT, SQUARE_SIZE, SQUARE_SIZE * 3 + TEXT_FIELD_PADDING * 3 / 2 + TEXT_PADDING, ARGB_WHITE, false);
			context.drawText(textRenderer, ACCELERATION_CONSTANT_TEXT, SQUARE_SIZE, SQUARE_SIZE * 4 + TEXT_FIELD_PADDING * 2 + TEXT_PADDING, ARGB_WHITE, false);
			context.drawText(textRenderer, DECELERATION_CONSTANT_TEXT, SQUARE_SIZE, SQUARE_SIZE * 5 + TEXT_FIELD_PADDING * 2 + TEXT_PADDING, ARGB_WHITE, false);
			context.drawText(textRenderer, DELAYED_VEHICLE_SPEED_INCREASE_PERCENTAGE_TEXT, SQUARE_SIZE, SQUARE_SIZE * 6 + TEXT_FIELD_PADDING * 2 + TEXT_PADDING, ARGB_WHITE, false);
			context.drawText(textRenderer, DELAYED_VEHICLE_REDUCE_DWELL_TIME_PERCENTAGE_TEXT, SQUARE_SIZE, SQUARE_SIZE * 7 + TEXT_FIELD_PADDING * 2 + TEXT_PADDING, ARGB_WHITE, false);
			if (buttonIsManual.isChecked()) {
				context.drawText(textRenderer, MAX_MANUAL_SPEED, SQUARE_SIZE, SQUARE_SIZE * 9 + TEXT_FIELD_PADDING * 2 + TEXT_PADDING, ARGB_WHITE, false);
				context.drawText(textRenderer, MANUAL_TO_AUTOMATIC_TIME, SQUARE_SIZE, SQUARE_SIZE * 10 + TEXT_FIELD_PADDING * 2 + TEXT_PADDING, ARGB_WHITE, false);
			}
		}
	}

	@Override
	public void close() {
		int maxTrains;
		try {
			maxTrains = Math.max(0, Integer.parseInt(textFieldMaxTrains.getText()));
		} catch (Exception ignored) {
			maxTrains = 0;
		}

		double accelerationConstant;
		try {
			accelerationConstant = Utilities.round(MathHelper.clamp((float) sliderAccelerationConstant.getIntValue() / SLIDER_SCALE + Siding.MIN_ACCELERATION, Siding.MIN_ACCELERATION, Siding.MAX_ACCELERATION), 8);
		} catch (Exception ignored) {
			accelerationConstant = Siding.ACCELERATION_DEFAULT;
		}

		double decelerationConstant;
		try {
			decelerationConstant = Utilities.round(MathHelper.clamp((float) sliderDecelerationConstant.getIntValue() / SLIDER_SCALE + Siding.MIN_ACCELERATION, Siding.MIN_ACCELERATION, Siding.MAX_ACCELERATION), 8);
		} catch (Exception ignored) {
			decelerationConstant = Siding.ACCELERATION_DEFAULT;
		}

		if (buttonIsManual.isChecked()) {
			savedRailBase.setIsManual(true);
		} else if (buttonUnlimitedTrains.isChecked()) {
			savedRailBase.setUnlimitedVehicles(true);
		} else {
			savedRailBase.setMaxVehicles(maxTrains);
		}
		savedRailBase.setAcceleration(accelerationConstant);
		savedRailBase.setDeceleration(decelerationConstant);
		savedRailBase.setDelayedVehicleSpeedIncreasePercentage(sliderDelayedVehicleSpeedIncreasePercentage.getIntValue());
		savedRailBase.setDelayedVehicleReduceDwellTimePercentage(sliderDelayedVehicleReduceDwellTimePercentage.getIntValue());

		RegistryClient.sendPacketToServer(new PacketUpdateData(new UpdateDataRequest(MinecraftClientData.getDashboardInstance()).addSiding(savedRailBase)));

		super.close();
	}

	@Override
	protected TranslationProvider.TranslationHolder getNumberStringKey() {
		return TranslationProvider.GUI_MTR_SIDING_NUMBER;
	}

	private void textFieldMaxTrainsCallback(String text) {
		IGui.setChecked(buttonUnlimitedTrains, text.isEmpty());
		if (!text.equals("1")) {
			IGui.setChecked(buttonIsManual, false);
			setButtons();
		}
	}

	private void setButtons() {
		sliderMaxManualSpeed.visible = buttonIsManual.isChecked();
		sliderDwellTimeMin.visible = buttonIsManual.isChecked();
		sliderDwellTimeSec.visible = buttonIsManual.isChecked();
	}

	private static String accelerationSliderFormatter(int value) {
		final double valueMeterPerMillisecondSquared = ((double) value / SLIDER_SCALE + Siding.MIN_ACCELERATION);
		return String.format("%s m/s² (%s km/h/s)", Utilities.round(valueMeterPerMillisecondSquared * ACCELERATION_UNIT_CONVERSION_1, 1), Utilities.round(valueMeterPerMillisecondSquared * ACCELERATION_UNIT_CONVERSION_2, 1));
	}

	private static String percentageFormatter(int value) {
		return value + "%";
	}

	private static String speedSliderFormatter(int value) {
		final RailType railType = convertMaxManualSpeed(value);
		return railType == null ? TranslationProvider.GUI_MTR_UNLIMITED.getString() : String.format("%s km/h", railType.speedLimit);
	}

	private static RailType convertMaxManualSpeed(int maxManualSpeed) {
		if (maxManualSpeed >= 0 && maxManualSpeed <= RailType.DIAMOND.ordinal()) {
			return RailType.values()[maxManualSpeed];
		} else {
			return null;
		}
	}
}
