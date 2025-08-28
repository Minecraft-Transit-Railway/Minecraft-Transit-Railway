package org.mtr.screen;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.mtr.client.MinecraftClientData;
import org.mtr.core.data.Siding;
import org.mtr.core.operation.UpdateDataRequest;
import org.mtr.core.tool.Utilities;
import org.mtr.data.RailType;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.packet.PacketUpdateData;
import org.mtr.registry.RegistryClient;
import org.mtr.tool.GuiHelper;
import org.mtr.widget.BetterButtonWidget;
import org.mtr.widget.BetterCheckboxWidget;
import org.mtr.widget.BetterSliderWidget;
import org.mtr.widget.BetterTextFieldWidget;

import javax.annotation.Nullable;

public final class SidingScreen extends ScrollableScreenBase {

	private final Siding siding;

	private final BetterTextFieldWidget sidingNumberTextField;
	private final BetterButtonWidget selectVehicleButton;
	private final BetterTextFieldWidget maxVehiclesTextField;
	private final BetterCheckboxWidget unlimitedVehiclesCheckbox;
	private final BetterSliderWidget accelerationConstantSlider;
	private final BetterSliderWidget decelerationConstantSlider;
	private final BetterSliderWidget delayedVehicleSpeedIncreasePercentageSlider;
	private final BetterSliderWidget delayedVehicleReduceDwellTimePercentageSlider;
	private final BetterCheckboxWidget earlyVehicleIncreaseDwellTimeCheckbox;
	private final BetterCheckboxWidget isManualCheckbox;
	private final BetterSliderWidget maxManualSpeedSlider;
	private final BetterSliderWidget drivableTimeoutSlider;

	private static final int ACCELERATION_DECELERATION_SLIDER_SCALE = 40000000;
	private static final float ACCELERATION_UNIT_CONVERSION_1 = Utilities.MILLIS_PER_SECOND * Utilities.MILLIS_PER_SECOND; // m/ms^2 to m/s^2
	private static final float ACCELERATION_UNIT_CONVERSION_2 = ACCELERATION_UNIT_CONVERSION_1 * Utilities.MILLIS_PER_HOUR / 1000 / Utilities.MILLIS_PER_SECOND; // m/ms^2 to km/h/s
	private static final int DRIVABLE_TIMEOUT_SLIDER_SCALE = 500; // 500 ms interval
	private static final int MAX_DRIVABLE_TIMEOUT = 10 * Utilities.MILLIS_PER_MINUTE; // 10 minutes

	public SidingScreen(Siding siding, @Nullable Screen previousScreen) {
		super(previousScreen);
		this.siding = siding;

		sidingNumberTextField = new BetterTextFieldWidget(16, TextCase.DEFAULT, null, TranslationProvider.GUI_MTR_SIDING_NUMBER.getString(), HALF_WIDGET_WIDTH, null);
		selectVehicleButton = new BetterButtonWidget(GuiHelper.EDIT_TEXTURE_ID, TranslationProvider.GUI_MTR_SELECTED_VEHICLE.getString(), HALF_WIDGET_WIDTH, () -> MinecraftClient.getInstance().setScreen(new VehicleSelectorScreen(siding, this)));

		maxVehiclesTextField = new BetterTextFieldWidget(3, TextCase.DEFAULT, "\\D", TranslationProvider.GUI_MTR_MAX_VEHICLES.getString(), HALF_WIDGET_WIDTH, this::maxVehiclesTextFieldCallback);
		unlimitedVehiclesCheckbox = new BetterCheckboxWidget(TranslationProvider.GUI_MTR_UNLIMITED_VEHICLES.getString(), this::maxVehiclesCheckboxCallback);

		accelerationConstantSlider = new BetterSliderWidget((int) Math.round((Siding.MAX_ACCELERATION - Siding.MIN_ACCELERATION) * ACCELERATION_DECELERATION_SLIDER_SCALE), SidingScreen::accelerationSliderFormatter, TranslationProvider.GUI_MTR_ACCELERATION.getString(), FULL_WIDGET_WIDTH, null);
		decelerationConstantSlider = new BetterSliderWidget((int) Math.round((Siding.MAX_ACCELERATION - Siding.MIN_ACCELERATION) * ACCELERATION_DECELERATION_SLIDER_SCALE), SidingScreen::accelerationSliderFormatter, TranslationProvider.GUI_MTR_DECELERATION.getString(), FULL_WIDGET_WIDTH, null);

		delayedVehicleSpeedIncreasePercentageSlider = new BetterSliderWidget(100, SidingScreen::percentageFormatter, TranslationProvider.GUI_MTR_DELAYED_VEHICLE_SPEED_INCREASE_PERCENTAGE.getString(), HALF_WIDGET_WIDTH, null);
		delayedVehicleReduceDwellTimePercentageSlider = new BetterSliderWidget(100, SidingScreen::percentageFormatter, TranslationProvider.GUI_MTR_DELAYED_VEHICLE_REDUCE_DWELL_TIME_PERCENTAGE.getString(), HALF_WIDGET_WIDTH, null);

		earlyVehicleIncreaseDwellTimeCheckbox = new BetterCheckboxWidget(TranslationProvider.GUI_MTR_EARLY_VEHICLE_INCREASE_DWELL_TIME.getString(), null);

		isManualCheckbox = new BetterCheckboxWidget(TranslationProvider.GUI_MTR_IS_MANUAL.getString(), this::isManualCheckboxCallback);
		maxManualSpeedSlider = new BetterSliderWidget(RailType.DIAMOND.ordinal(), SidingScreen::speedSliderFormatter, TranslationProvider.GUI_MTR_MAX_MANUAL_SPEED.getString(), HALF_WIDGET_WIDTH, null);

		drivableTimeoutSlider = new BetterSliderWidget(MAX_DRIVABLE_TIMEOUT / DRIVABLE_TIMEOUT_SLIDER_SCALE - 1, SidingScreen::timeoutFormatter, TranslationProvider.GUI_MTR_MANUAL_TO_AUTOMATIC_TIME.getString(), FULL_WIDGET_WIDTH, null);
	}

	@Override
	protected void init() {
		super.init();
		final int widgetColumn1 = getWidgetColumn1();
		final int widgetColumn2Of2 = getWidgetColumn2Of2();

		int widgetY = 0;
		sidingNumberTextField.setPosition(widgetColumn1, widgetY);
		sidingNumberTextField.setText(siding.getName());
		selectVehicleButton.setPosition(widgetColumn2Of2, widgetY);

		widgetY += GuiHelper.DEFAULT_LINE_SIZE + GuiHelper.DEFAULT_PADDING;
		maxVehiclesTextField.setPosition(widgetColumn1, widgetY);
		maxVehiclesTextField.setText(siding.getIsUnlimited() ? "" : String.valueOf(siding.getMaxVehicles()));
		unlimitedVehiclesCheckbox.setPosition(widgetColumn2Of2, widgetY);
		unlimitedVehiclesCheckbox.isChecked = siding.getIsUnlimited();

		widgetY += GuiHelper.DEFAULT_LINE_SIZE * 2;
		accelerationConstantSlider.setPosition(widgetColumn1, widgetY);
		accelerationConstantSlider.setValue((int) Math.round((siding.getAcceleration() - Siding.MIN_ACCELERATION) * ACCELERATION_DECELERATION_SLIDER_SCALE));

		widgetY += GuiHelper.DEFAULT_LINE_SIZE + GuiHelper.DEFAULT_PADDING;
		decelerationConstantSlider.setPosition(widgetColumn1, widgetY);
		decelerationConstantSlider.setValue((int) Math.round((siding.getDeceleration() - Siding.MIN_ACCELERATION) * ACCELERATION_DECELERATION_SLIDER_SCALE));

		widgetY += GuiHelper.DEFAULT_LINE_SIZE * 2;
		delayedVehicleSpeedIncreasePercentageSlider.setPosition(widgetColumn1, widgetY);
		delayedVehicleSpeedIncreasePercentageSlider.setValue(siding.getDelayedVehicleSpeedIncreasePercentage());
		delayedVehicleReduceDwellTimePercentageSlider.setPosition(widgetColumn2Of2, widgetY);
		delayedVehicleReduceDwellTimePercentageSlider.setValue(siding.getDelayedVehicleReduceDwellTimePercentage());

		widgetY += GuiHelper.DEFAULT_LINE_SIZE + GuiHelper.DEFAULT_PADDING;
		earlyVehicleIncreaseDwellTimeCheckbox.setPosition(widgetColumn1, widgetY);
		earlyVehicleIncreaseDwellTimeCheckbox.isChecked = siding.getEarlyVehicleIncreaseDwellTime();

		widgetY += GuiHelper.DEFAULT_LINE_SIZE * 2;
		isManualCheckbox.setPosition(widgetColumn1, widgetY);
		isManualCheckbox.isChecked = siding.getIsManual();
		maxManualSpeedSlider.setPosition(widgetColumn2Of2, widgetY);
		for (final RailType railType : RailType.values()) {
			if (Math.abs(Utilities.kilometersPerHourToMetersPerMillisecond(railType.speedLimit) - siding.getMaxManualSpeed()) < 0.001) {
				maxManualSpeedSlider.setValue(railType.ordinal());
				break;
			}
		}

		widgetY += GuiHelper.DEFAULT_LINE_SIZE + GuiHelper.DEFAULT_PADDING;
		drivableTimeoutSlider.setPosition(widgetColumn1, widgetY);
		drivableTimeoutSlider.setValue(siding.getManualToAutomaticTime() / DRIVABLE_TIMEOUT_SLIDER_SCALE - 1);

		addDrawableChild(sidingNumberTextField);
		addDrawableChild(selectVehicleButton);

		if (!siding.getTransportMode().continuousMovement) {
			addDrawableChild(maxVehiclesTextField);
			addDrawableChild(unlimitedVehiclesCheckbox);
			addDrawableChild(accelerationConstantSlider);
			addDrawableChild(decelerationConstantSlider);
			addDrawableChild(delayedVehicleSpeedIncreasePercentageSlider);
			addDrawableChild(delayedVehicleReduceDwellTimePercentageSlider);
			addDrawableChild(earlyVehicleIncreaseDwellTimeCheckbox);
			addDrawableChild(isManualCheckbox);
			addDrawableChild(maxManualSpeedSlider);
			addDrawableChild(drivableTimeoutSlider);
		}

		setButtons();
	}

	@Override
	public void close() {
		int maxTrains;
		try {
			maxTrains = Math.max(0, Integer.parseInt(maxVehiclesTextField.getText()));
		} catch (Exception ignored) {
			maxTrains = 0;
		}

		double accelerationConstant;
		try {
			accelerationConstant = Utilities.round(Math.clamp((float) accelerationConstantSlider.getIntValue() / ACCELERATION_DECELERATION_SLIDER_SCALE + Siding.MIN_ACCELERATION, Siding.MIN_ACCELERATION, Siding.MAX_ACCELERATION), 8);
		} catch (Exception ignored) {
			accelerationConstant = Siding.ACCELERATION_DEFAULT;
		}

		double decelerationConstant;
		try {
			decelerationConstant = Utilities.round(Math.clamp((float) decelerationConstantSlider.getIntValue() / ACCELERATION_DECELERATION_SLIDER_SCALE + Siding.MIN_ACCELERATION, Siding.MIN_ACCELERATION, Siding.MAX_ACCELERATION), 8);
		} catch (Exception ignored) {
			decelerationConstant = Siding.ACCELERATION_DEFAULT;
		}

		siding.setName(sidingNumberTextField.getText());

		if (isManualCheckbox.isChecked) {
			siding.setIsManual(true);
		} else if (unlimitedVehiclesCheckbox.isChecked) {
			siding.setUnlimitedVehicles(true);
		} else {
			siding.setMaxVehicles(maxTrains);
		}

		siding.setAcceleration(accelerationConstant);
		siding.setDeceleration(decelerationConstant);
		siding.setDelayedVehicleSpeedIncreasePercentage(delayedVehicleSpeedIncreasePercentageSlider.getIntValue());
		siding.setDelayedVehicleReduceDwellTimePercentage(delayedVehicleReduceDwellTimePercentageSlider.getIntValue());
		siding.setEarlyVehicleIncreaseDwellTime(earlyVehicleIncreaseDwellTimeCheckbox.isChecked);

		siding.setMaxManualSpeed(Utilities.kilometersPerHourToMetersPerMillisecond(convertMaxManualSpeed(maxManualSpeedSlider.getIntValue()).speedLimit));
		siding.setManualToAutomaticTime(drivableTimeoutSlider.getIntValue() * DRIVABLE_TIMEOUT_SLIDER_SCALE + DRIVABLE_TIMEOUT_SLIDER_SCALE);

		RegistryClient.sendPacketToServer(new PacketUpdateData(new UpdateDataRequest(MinecraftClientData.getDashboardInstance()).addSiding(siding)));
		super.close();
	}

	@Override
	public ObjectArrayList<MutableText> getScreenTitle() {
		return ObjectArrayList.of(TranslationProvider.GUI_MTR_SIDING.getMutableText(Utilities.formatName(sidingNumberTextField.getText())));
	}

	@Override
	public ObjectArrayList<MutableText> getScreenSubtitle() {
		return ObjectArrayList.of(Text.literal(Utilities.formatName(siding.getDepotName())));
	}

	@Override
	public ObjectArrayList<MutableText> getScreenDescription() {
		return new ObjectArrayList<>();
	}

	private void maxVehiclesTextFieldCallback(String text) {
		unlimitedVehiclesCheckbox.isChecked = text.isEmpty();
		if (!text.equals("1")) {
			isManualCheckbox.isChecked = false;
			setButtons();
		}
	}

	private void maxVehiclesCheckboxCallback() {
		if (unlimitedVehiclesCheckbox.isChecked) {
			isManualCheckbox.isChecked = false;
			setButtons();
		}
		if (unlimitedVehiclesCheckbox.isChecked && !maxVehiclesTextField.getText().isEmpty()) {
			maxVehiclesTextField.setText("");
		} else if (!unlimitedVehiclesCheckbox.isChecked && maxVehiclesTextField.getText().isEmpty()) {
			maxVehiclesTextField.setText("1");
		}
	}

	private void isManualCheckboxCallback() {
		if (isManualCheckbox.isChecked && !maxVehiclesTextField.getText().equals("1")) {
			maxVehiclesTextField.setText("1");
			unlimitedVehiclesCheckbox.isChecked = false;
		}
		setButtons();
	}

	private void setButtons() {
		maxManualSpeedSlider.visible = isManualCheckbox.isChecked;
		drivableTimeoutSlider.visible = isManualCheckbox.isChecked;
	}

	private static String accelerationSliderFormatter(int value) {
		final double valueMeterPerMillisecondSquared = ((double) value / ACCELERATION_DECELERATION_SLIDER_SCALE + Siding.MIN_ACCELERATION);
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

	private static String timeoutFormatter(int value) {
		final int millis = value * DRIVABLE_TIMEOUT_SLIDER_SCALE + DRIVABLE_TIMEOUT_SLIDER_SCALE;
		return String.format("%s %s", TranslationProvider.GUI_MTR_MINUTES.getString(millis / 60 / 1000), TranslationProvider.GUI_MTR_SECONDS.getString((millis / 1000F) % 60));
	}
}
