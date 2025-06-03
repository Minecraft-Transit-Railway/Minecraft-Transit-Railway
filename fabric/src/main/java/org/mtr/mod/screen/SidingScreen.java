package org.mtr.mod.screen;

import org.mtr.core.data.Depot;
import org.mtr.core.data.Siding;
import org.mtr.core.data.TransportMode;
import org.mtr.core.operation.UpdateDataRequest;
import org.mtr.core.tool.Utilities;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.*;
import org.mtr.mapping.tool.TextCase;
import org.mtr.mod.Icons;
import org.mtr.mod.InitClient;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.client.MinecraftClientData;
import org.mtr.mod.data.RailType;
import org.mtr.mod.generated.lang.TranslationProvider;
import org.mtr.mod.packet.PacketUpdateData;

public class SidingScreen extends SavedRailScreenBase<Siding, Depot> implements Icons {

	private final ButtonWidgetExtension buttonSelectTrain;
	private final CheckboxWidgetExtension buttonUnlimitedTrains;
	private final TextFieldWidgetExtension textFieldMaxTrains;
	private final WidgetShorterSlider sliderAccelerationConstant;
	private final WidgetShorterSlider sliderDecelerationConstant;
	private final WidgetShorterSlider sliderDelayedVehicleSpeedIncreasePercentage;
	private final WidgetShorterSlider sliderDelayedVehicleReduceDwellTimePercentage;
	private final CheckboxWidgetExtension buttonEarlyVehicleIncreaseDwellTime;
	private final CheckboxWidgetExtension buttonIsManual;
	private final WidgetShorterSlider sliderMaxManualSpeed;

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
	private static final int SLIDER_SCALE = 1000 * 50 * 50 * 4;
	private static final float ACCELERATION_UNIT_CONVERSION_1 = 1000 * 1000; // m/ms^2 to m/s^2
	private static final float ACCELERATION_UNIT_CONVERSION_2 = ACCELERATION_UNIT_CONVERSION_1 * 3.6F; // m/ms^2 to km/h/s

	public SidingScreen(Siding siding, TransportMode transportMode, ScreenExtension previousScreenExtension) {
		super(siding, transportMode, previousScreenExtension, SELECTED_TRAIN_TEXT, MAX_TRAINS_TEXT, ACCELERATION_CONSTANT_TEXT, DECELERATION_CONSTANT_TEXT, DELAYED_VEHICLE_SPEED_INCREASE_PERCENTAGE_TEXT, DELAYED_VEHICLE_REDUCE_DWELL_TIME_PERCENTAGE_TEXT, MANUAL_TO_AUTOMATIC_TIME, MAX_MANUAL_SPEED);
		buttonSelectTrain = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, button -> MinecraftClient.getInstance().openScreen(new Screen(new VehicleSelectorScreen(savedRailBase, this))));
		buttonSelectTrain.setMessage2(new Text(TextHelper.translatable("selectWorld.edit").data));
		textFieldMaxTrains = new TextFieldWidgetExtension(0, 0, 0, SQUARE_SIZE, MAX_TRAINS_TEXT_LENGTH, TextCase.DEFAULT, "\\D", null);
		sliderAccelerationConstant = new WidgetShorterSlider(0, MAX_TRAINS_WIDTH, (int) Math.round((Siding.MAX_ACCELERATION - Siding.MIN_ACCELERATION) * SLIDER_SCALE), SidingScreen::accelerationSliderFormatter, null);
		sliderDecelerationConstant = new WidgetShorterSlider(0, MAX_TRAINS_WIDTH, (int) Math.round((Siding.MAX_ACCELERATION - Siding.MIN_ACCELERATION) * SLIDER_SCALE), SidingScreen::accelerationSliderFormatter, null);
		sliderDelayedVehicleSpeedIncreasePercentage = new WidgetShorterSlider(0, MAX_TRAINS_WIDTH, 100, SidingScreen::percentageFormatter, null);
		sliderDelayedVehicleReduceDwellTimePercentage = new WidgetShorterSlider(0, MAX_TRAINS_WIDTH, 100, SidingScreen::percentageFormatter, null);
		buttonEarlyVehicleIncreaseDwellTime = new CheckboxWidgetExtension(0, 0, 0, SQUARE_SIZE, true, checked -> {
		});
		buttonEarlyVehicleIncreaseDwellTime.setMessage2(TranslationProvider.GUI_MTR_EARLY_VEHICLE_INCREASE_DWELL_TIME.getText());
		buttonIsManual = new CheckboxWidgetExtension(0, 0, 0, SQUARE_SIZE, true, checked -> {
			if (checked && !textFieldMaxTrains.getText2().equals("1")) {
				textFieldMaxTrains.setText2("1");
			}
			setButtons();
		});
		buttonIsManual.setMessage2(TranslationProvider.GUI_MTR_IS_MANUAL.getText());
		sliderMaxManualSpeed = new WidgetShorterSlider(0, MAX_TRAINS_WIDTH, RailType.DIAMOND.ordinal(), SidingScreen::speedSliderFormatter, null);
		buttonUnlimitedTrains = new CheckboxWidgetExtension(0, 0, 0, SQUARE_SIZE, true, checked -> {
			if (checked) {
				buttonIsManual.setChecked(false);
				setButtons();
			}
			if (checked && !textFieldMaxTrains.getText2().isEmpty()) {
				textFieldMaxTrains.setText2("");
			} else if (!checked && textFieldMaxTrains.getText2().isEmpty()) {
				textFieldMaxTrains.setText2("1");
			}
		});
		buttonUnlimitedTrains.setMessage2(TranslationProvider.GUI_MTR_UNLIMITED_VEHICLES.getText());
	}

	@Override
	protected void init2() {
		super.init2();

		IDrawing.setPositionAndWidth(buttonSelectTrain, SQUARE_SIZE + textWidth, SQUARE_SIZE * 2 + TEXT_FIELD_PADDING, width - textWidth - SQUARE_SIZE * 2);
		IDrawing.setPositionAndWidth(buttonUnlimitedTrains, SQUARE_SIZE + textWidth + MAX_TRAINS_WIDTH + TEXT_FIELD_PADDING * 3 / 2, SQUARE_SIZE * 3 + TEXT_FIELD_PADDING * 3 / 2, width - textWidth - SQUARE_SIZE * 2);

		addChild(new ClickableWidget(buttonSelectTrain));

		buttonIsManual.setChecked(savedRailBase.getIsManual());
		buttonUnlimitedTrains.setChecked(savedRailBase.getIsUnlimited());

		IDrawing.setPositionAndWidth(textFieldMaxTrains, SQUARE_SIZE + textWidth + TEXT_FIELD_PADDING / 2, SQUARE_SIZE * 3 + TEXT_FIELD_PADDING * 3 / 2, MAX_TRAINS_WIDTH - TEXT_FIELD_PADDING);
		textFieldMaxTrains.setText2(savedRailBase.getIsUnlimited() ? "" : String.valueOf(savedRailBase.getMaxVehicles()));
		textFieldMaxTrains.setChangedListener2(text -> {
			buttonUnlimitedTrains.setChecked(text.isEmpty());
			if (!text.equals("1")) {
				buttonIsManual.setChecked(false);
				setButtons();
			}
		});

		sliderAccelerationConstant.setX2(SQUARE_SIZE + textWidth);
		sliderAccelerationConstant.setY2(SQUARE_SIZE * 4 + TEXT_FIELD_PADDING * 2);
		sliderAccelerationConstant.setHeight(SQUARE_SIZE);
		sliderAccelerationConstant.setValue((int) Math.round((savedRailBase.getAcceleration() - Siding.MIN_ACCELERATION) * SLIDER_SCALE));

		sliderDecelerationConstant.setX2(SQUARE_SIZE + textWidth);
		sliderDecelerationConstant.setY2(SQUARE_SIZE * 5 + TEXT_FIELD_PADDING * 2);
		sliderDecelerationConstant.setHeight(SQUARE_SIZE);
		sliderDecelerationConstant.setValue((int) Math.round((savedRailBase.getDeceleration() - Siding.MIN_ACCELERATION) * SLIDER_SCALE));

		sliderDelayedVehicleSpeedIncreasePercentage.setX2(SQUARE_SIZE + textWidth);
		sliderDelayedVehicleSpeedIncreasePercentage.setY2(SQUARE_SIZE * 6 + TEXT_FIELD_PADDING * 2);
		sliderDelayedVehicleSpeedIncreasePercentage.setHeight(SQUARE_SIZE);
		sliderDelayedVehicleSpeedIncreasePercentage.setValue(savedRailBase.getDelayedVehicleSpeedIncreasePercentage());

		sliderDelayedVehicleReduceDwellTimePercentage.setX2(SQUARE_SIZE + textWidth);
		sliderDelayedVehicleReduceDwellTimePercentage.setY2(SQUARE_SIZE * 7 + TEXT_FIELD_PADDING * 2);
		sliderDelayedVehicleReduceDwellTimePercentage.setHeight(SQUARE_SIZE);
		sliderDelayedVehicleReduceDwellTimePercentage.setValue(savedRailBase.getDelayedVehicleReduceDwellTimePercentage());

		IDrawing.setPositionAndWidth(buttonEarlyVehicleIncreaseDwellTime, SQUARE_SIZE, SQUARE_SIZE * 8 + TEXT_FIELD_PADDING * 2, width - textWidth - SQUARE_SIZE * 2);
		buttonEarlyVehicleIncreaseDwellTime.setChecked(savedRailBase.getEarlyVehicleIncreaseDwellTime());

		IDrawing.setPositionAndWidth(buttonIsManual, SQUARE_SIZE, SQUARE_SIZE * 9 + TEXT_FIELD_PADDING * 2, width - textWidth - SQUARE_SIZE * 2);

		sliderMaxManualSpeed.setX2(SQUARE_SIZE + textWidth);
		sliderMaxManualSpeed.setY2(SQUARE_SIZE * 10 + TEXT_FIELD_PADDING * 2);
		sliderMaxManualSpeed.setHeight(SQUARE_SIZE);
		for (final RailType railType : RailType.values()) {
			if (Math.abs(Utilities.kilometersPerHourToMetersPerMillisecond(railType.speedLimit) - savedRailBase.getMaxManualSpeed()) < 0.001) {
				sliderMaxManualSpeed.setValue(railType.ordinal());
				break;
			}
		}

		sliderDwellTimeMin.setY2(SQUARE_SIZE * 11 + TEXT_FIELD_PADDING * 2);
		sliderDwellTimeSec.setY2(SQUARE_SIZE * 23 / 2 + TEXT_FIELD_PADDING * 2);
		sliderDwellTimeMin.setValue(savedRailBase.getManualToAutomaticTime() / SECONDS_PER_MINUTE / Utilities.MILLIS_PER_SECOND);
		sliderDwellTimeSec.setValue((savedRailBase.getManualToAutomaticTime() * 2 / Utilities.MILLIS_PER_SECOND) % (SECONDS_PER_MINUTE * 2));

		if (showScheduleControls) {
			addChild(new ClickableWidget(buttonUnlimitedTrains));
			addChild(new ClickableWidget(textFieldMaxTrains));
			addChild(new ClickableWidget(sliderAccelerationConstant));
			addChild(new ClickableWidget(sliderDecelerationConstant));
			addChild(new ClickableWidget(sliderDelayedVehicleSpeedIncreasePercentage));
			addChild(new ClickableWidget(sliderDelayedVehicleReduceDwellTimePercentage));
			addChild(new ClickableWidget(buttonEarlyVehicleIncreaseDwellTime));
			addChild(new ClickableWidget(buttonIsManual));
			addChild(new ClickableWidget(sliderMaxManualSpeed));
		}

		setButtons();
	}

	@Override
	public void tick2() {
		super.tick2();
		textFieldMaxTrains.tick2();
	}

	@Override
	public void render(GraphicsHolder graphicsHolder, int mouseX, int mouseY, float delta) {
		super.render(graphicsHolder, mouseX, mouseY, delta);
		graphicsHolder.drawText(SELECTED_TRAIN_TEXT, SQUARE_SIZE, SQUARE_SIZE * 2 + TEXT_FIELD_PADDING + TEXT_PADDING, ARGB_WHITE, false, GraphicsHolder.getDefaultLight());
		if (showScheduleControls) {
			graphicsHolder.drawText(MAX_TRAINS_TEXT, SQUARE_SIZE, SQUARE_SIZE * 3 + TEXT_FIELD_PADDING * 3 / 2 + TEXT_PADDING, ARGB_WHITE, false, GraphicsHolder.getDefaultLight());
			graphicsHolder.drawText(ACCELERATION_CONSTANT_TEXT, SQUARE_SIZE, SQUARE_SIZE * 4 + TEXT_FIELD_PADDING * 2 + TEXT_PADDING, ARGB_WHITE, false, GraphicsHolder.getDefaultLight());
			graphicsHolder.drawText(DECELERATION_CONSTANT_TEXT, SQUARE_SIZE, SQUARE_SIZE * 5 + TEXT_FIELD_PADDING * 2 + TEXT_PADDING, ARGB_WHITE, false, GraphicsHolder.getDefaultLight());
			graphicsHolder.drawText(DELAYED_VEHICLE_SPEED_INCREASE_PERCENTAGE_TEXT, SQUARE_SIZE, SQUARE_SIZE * 6 + TEXT_FIELD_PADDING * 2 + TEXT_PADDING, ARGB_WHITE, false, GraphicsHolder.getDefaultLight());
			graphicsHolder.drawText(DELAYED_VEHICLE_REDUCE_DWELL_TIME_PERCENTAGE_TEXT, SQUARE_SIZE, SQUARE_SIZE * 7 + TEXT_FIELD_PADDING * 2 + TEXT_PADDING, ARGB_WHITE, false, GraphicsHolder.getDefaultLight());
			if (buttonIsManual.isChecked2()) {
				graphicsHolder.drawText(MAX_MANUAL_SPEED, SQUARE_SIZE, SQUARE_SIZE * 10 + TEXT_FIELD_PADDING * 2 + TEXT_PADDING, ARGB_WHITE, false, GraphicsHolder.getDefaultLight());
				graphicsHolder.drawText(MANUAL_TO_AUTOMATIC_TIME, SQUARE_SIZE, SQUARE_SIZE * 11 + TEXT_FIELD_PADDING * 2 + TEXT_PADDING, ARGB_WHITE, false, GraphicsHolder.getDefaultLight());
			}
		}
	}

	@Override
	public void onClose2() {
		int maxTrains;
		try {
			maxTrains = Math.max(0, Integer.parseInt(textFieldMaxTrains.getText2()));
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

		if (buttonIsManual.isChecked2()) {
			savedRailBase.setIsManual(true);
		} else if (buttonUnlimitedTrains.isChecked2()) {
			savedRailBase.setUnlimitedVehicles(true);
		} else {
			savedRailBase.setMaxVehicles(maxTrains);
		}
		savedRailBase.setAcceleration(accelerationConstant);
		savedRailBase.setDeceleration(decelerationConstant);
		savedRailBase.setDelayedVehicleSpeedIncreasePercentage(sliderDelayedVehicleSpeedIncreasePercentage.getIntValue());
		savedRailBase.setDelayedVehicleReduceDwellTimePercentage(sliderDelayedVehicleReduceDwellTimePercentage.getIntValue());
		savedRailBase.setEarlyVehicleIncreaseDwellTime(buttonEarlyVehicleIncreaseDwellTime.isChecked2());

		savedRailBase.setMaxManualSpeed(Utilities.kilometersPerHourToMetersPerMillisecond(convertMaxManualSpeed(sliderMaxManualSpeed.getIntValue()).speedLimit));
		savedRailBase.setManualToAutomaticTime(sliderDwellTimeMin.getIntValue() * SECONDS_PER_MINUTE * Utilities.MILLIS_PER_SECOND + sliderDwellTimeSec.getIntValue() * Utilities.MILLIS_PER_SECOND / 2);

		InitClient.REGISTRY_CLIENT.sendPacketToServer(new PacketUpdateData(new UpdateDataRequest(MinecraftClientData.getDashboardInstance()).addSiding(savedRailBase)));

		super.onClose2();
	}

	@Override
	protected TranslationProvider.TranslationHolder getNumberStringKey() {
		return TranslationProvider.GUI_MTR_SIDING_NUMBER;
	}

	private void setButtons() {
		sliderMaxManualSpeed.visible = buttonIsManual.isChecked2();
		sliderDwellTimeMin.visible = buttonIsManual.isChecked2();
		sliderDwellTimeSec.visible = buttonIsManual.isChecked2();
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
		return String.format("%s km/h", railType.speedLimit);
	}

	private static RailType convertMaxManualSpeed(int maxManualSpeed) {
		if (maxManualSpeed >= 0 && maxManualSpeed <= RailType.DIAMOND.ordinal()) {
			return RailType.values()[maxManualSpeed];
		} else {
			return RailType.DIAMOND;
		}
	}
}
