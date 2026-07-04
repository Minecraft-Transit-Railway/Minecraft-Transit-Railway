package org.mtr.screen;

import gg.essential.elementa.components.ScrollComponent;
import gg.essential.elementa.components.UIContainer;
import gg.essential.elementa.components.UIWrappedText;
import gg.essential.elementa.constraints.*;
import gg.essential.universal.utils.ReleasedDynamicTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;
import org.mtr.client.MinecraftClientData;
import org.mtr.core.data.Siding;
import org.mtr.core.operation.UpdateDataRequest;
import org.mtr.core.tool.Utilities;
import org.mtr.data.RailType;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import org.mtr.packet.PacketUpdateData;
import org.mtr.registry.RegistryClient;
import org.mtr.tool.GuiHelper;
import org.mtr.tool.ReleasedDynamicTextureRegistry;
import org.mtr.widget.*;

public final class SidingScreen extends NameColorDataScreenBase<Siding> {

	private final UIWrappedText selectedVehiclesText;

	@Nullable
	private final TextInputComponent maxVehiclesTextInput;
	@Nullable
	private final CheckboxComponent unlimitedVehiclesCheckbox;
	@Nullable
	private final NumberInputComponent accelerationConstantNumberInput;
	@Nullable
	private final NumberInputComponent decelerationConstantNumberInput;
	@Nullable
	private final NumberInputComponent delayedVehicleSpeedIncreasePercentageNumberInput;
	@Nullable
	private final NumberInputComponent delayedVehicleReduceDwellTimePercentageNumberInput;
	@Nullable
	private final CheckboxComponent earlyVehicleIncreaseDwellTimeCheckbox;
	@Nullable
	private final CheckboxComponent isManualCheckbox;
	@Nullable
	private final ScrollComponent manualScrollComponent;
	@Nullable
	private final NumberInputComponent maxManualSpeedNumberInput;
	@Nullable
	private final NumberInputComponent drivableTimeoutMinutesNumberInput;
	@Nullable
	private final NumberInputComponent drivableTimeoutSecondsNumberInput;

	private static final int ACCELERATION_DECELERATION_SLIDER_SCALE = 40000000;
	private static final int METERS_PER_MILLISECOND_TO_KILOMETERS_PER_HOUR = Utilities.MILLIS_PER_HOUR / 1000;
	private static final int METERS_PER_MILLISECOND_2_TO_METERS_PER_SECOND_2 = Utilities.MILLIS_PER_SECOND * Utilities.MILLIS_PER_SECOND;
	private static final float METERS_PER_SECOND_2_TO_KILOMETERS_PER_HOUR_PER_SECOND = (float) METERS_PER_MILLISECOND_TO_KILOMETERS_PER_HOUR / Utilities.MILLIS_PER_SECOND;
	private static final int MAX_DRIVABLE_TIMEOUT = 10; // 10 minutes
	private static final int LEFT_WIDTH = 96;

	public SidingScreen(Siding siding, @Nullable WindowBase previousScreen) {
		super(siding, getTabs(siding), TranslationProvider.GUI_MTR_SIDING_NUMBER, name -> (Utilities.formatName(siding.getDepotName()) + "   " + TranslationProvider.GUI_MTR_SIDING.getString(Utilities.formatName(name))).trim(), null, previousScreen);

		selectedVehiclesText = GuiHelper.createLabel(firstTabScrollComponent, "");

		final ButtonComponent selectVehicleButton = (ButtonComponent) new ButtonComponent(true)
			.setChildOf(firstTabScrollComponent)
			.setY(new SiblingConstraint())
			.setWidth(new PixelConstraint(LEFT_WIDTH));

		selectVehicleButton.setText(Component.translatable("selectWorld.edit").getString());
		selectVehicleButton.onClick(() -> Minecraft.getInstance().setScreen(new VehicleSelectorScreen(siding, this)));

		if (!siding.getTransportMode().continuousMovement) {
			GuiHelper.createSpacing(firstTabScrollComponent);
			GuiHelper.createLabel(firstTabScrollComponent, TranslationProvider.GUI_MTR_MAX_VEHICLES.getString());

			final UIContainer maxVehiclesContainer = (UIContainer) new UIContainer()
				.setChildOf(firstTabScrollComponent)
				.setY(new SiblingConstraint())
				.setWidth(new RelativeConstraint())
				.setHeight(new PixelConstraint(20));

			maxVehiclesTextInput = (TextInputComponent) new TextInputComponent()
				.setChildOf(maxVehiclesContainer)
				.setWidth(new PixelConstraint(LEFT_WIDTH))
				.setHeight(new PixelConstraint(20));

			maxVehiclesTextInput.setMaxLength(3);
			maxVehiclesTextInput.setFilter("\\D");
			maxVehiclesTextInput.setText(siding.getIsUnlimited() ? "" : String.valueOf(siding.getMaxVehicles()));
			maxVehiclesTextInput.onChange(this::maxVehiclesTextFieldCallback);

			unlimitedVehiclesCheckbox = (CheckboxComponent) new CheckboxComponent()
				.setChildOf(maxVehiclesContainer)
				.setX(new SiblingConstraint(GuiHelper.DEFAULT_PADDING))
				.setWidth(new SubtractiveConstraint(new FillConstraint(), new PixelConstraint(GuiHelper.DEFAULT_PADDING)));

			unlimitedVehiclesCheckbox.setText(TranslationProvider.GUI_MTR_UNLIMITED_VEHICLES.getString());
			unlimitedVehiclesCheckbox.setChecked(siding.getIsUnlimited());
			unlimitedVehiclesCheckbox.onClick(this::maxVehiclesCheckboxCallback);

			GuiHelper.createSpacing(firstTabScrollComponent);
			GuiHelper.createLabel(firstTabScrollComponent, TranslationProvider.GUI_MTR_ACCELERATION.getString());

			accelerationConstantNumberInput = (NumberInputComponent) new NumberInputComponent(Siding.MIN_ACCELERATION * METERS_PER_MILLISECOND_2_TO_METERS_PER_SECOND_2, Siding.MAX_ACCELERATION * METERS_PER_MILLISECOND_2_TO_METERS_PER_SECOND_2, 0.1, true, null)
				.setChildOf(firstTabScrollComponent)
				.setY(new SiblingConstraint())
				.setWidth(new RelativeConstraint());

			accelerationConstantNumberInput.setValue(siding.getAcceleration() * METERS_PER_MILLISECOND_2_TO_METERS_PER_SECOND_2);

			GuiHelper.createSpacing(firstTabScrollComponent);
			GuiHelper.createLabel(firstTabScrollComponent, TranslationProvider.GUI_MTR_DECELERATION.getString());

			decelerationConstantNumberInput = (NumberInputComponent) new NumberInputComponent(Siding.MIN_ACCELERATION * METERS_PER_MILLISECOND_2_TO_METERS_PER_SECOND_2, Siding.MAX_ACCELERATION * METERS_PER_MILLISECOND_2_TO_METERS_PER_SECOND_2, 0.1, true, null)
				.setChildOf(firstTabScrollComponent)
				.setY(new SiblingConstraint())
				.setWidth(new RelativeConstraint());

			decelerationConstantNumberInput.setValue(siding.getDeceleration() * METERS_PER_MILLISECOND_2_TO_METERS_PER_SECOND_2);

			GuiHelper.createSpacing(firstTabScrollComponent);
			GuiHelper.createLabel(firstTabScrollComponent, TranslationProvider.GUI_MTR_DELAYED_VEHICLE_SPEED_INCREASE_PERCENTAGE.getString());

			delayedVehicleSpeedIncreasePercentageNumberInput = (NumberInputComponent) new NumberInputComponent(0, 100, 1, false, null)
				.setChildOf(firstTabScrollComponent)
				.setY(new SiblingConstraint())
				.setWidth(new PixelConstraint(LEFT_WIDTH));

			delayedVehicleSpeedIncreasePercentageNumberInput.setSuffix("%");
			delayedVehicleSpeedIncreasePercentageNumberInput.setValue(siding.getDelayedVehicleSpeedIncreasePercentage());

			GuiHelper.createSpacing(firstTabScrollComponent);
			GuiHelper.createLabel(firstTabScrollComponent, TranslationProvider.GUI_MTR_DELAYED_VEHICLE_REDUCE_DWELL_TIME_PERCENTAGE.getString());

			delayedVehicleReduceDwellTimePercentageNumberInput = (NumberInputComponent) new NumberInputComponent(0, 100, 1, false, null)
				.setChildOf(firstTabScrollComponent)
				.setY(new SiblingConstraint())
				.setWidth(new PixelConstraint(LEFT_WIDTH));

			delayedVehicleReduceDwellTimePercentageNumberInput.setSuffix("%");
			delayedVehicleReduceDwellTimePercentageNumberInput.setValue(siding.getDelayedVehicleReduceDwellTimePercentage());

			GuiHelper.createSpacing(firstTabScrollComponent);

			earlyVehicleIncreaseDwellTimeCheckbox = (CheckboxComponent) new CheckboxComponent()
				.setChildOf(firstTabScrollComponent)
				.setY(new SiblingConstraint())
				.setWidth(new RelativeConstraint());

			earlyVehicleIncreaseDwellTimeCheckbox.setText(TranslationProvider.GUI_MTR_EARLY_VEHICLE_INCREASE_DWELL_TIME.getString());
			earlyVehicleIncreaseDwellTimeCheckbox.setChecked(siding.getEarlyVehicleIncreaseDwellTime());

			final UIContainer manualContainer = (UIContainer) new UIContainer()
				.setChildOf(backgroundComponent.containers[1])
				.setWidth(new RelativeConstraint())
				.setHeight(new RelativeConstraint());

			isManualCheckbox = (CheckboxComponent) new CheckboxComponent()
				.setChildOf(manualContainer)
				.setWidth(new RelativeConstraint());

			isManualCheckbox.setText(TranslationProvider.GUI_MTR_IS_MANUAL.getString());
			isManualCheckbox.setChecked(siding.getIsManual());
			isManualCheckbox.onClick(this::isManualCheckboxCallback);

			GuiHelper.createSpacing(manualContainer);

			manualScrollComponent = ((ScrollPanelComponent) new ScrollPanelComponent(true)
				.setChildOf(manualContainer)
				.setY(new SiblingConstraint())
				.setWidth(new RelativeConstraint())
				.setHeight(new FillConstraint())).contentContainer;

			GuiHelper.createLabel(manualScrollComponent, TranslationProvider.GUI_MTR_MAX_MANUAL_SPEED.getString());

			maxManualSpeedNumberInput = (NumberInputComponent) new NumberInputComponent(1, RailType.DIAMOND.speedLimit, 5, false, null)
				.setChildOf(manualScrollComponent)
				.setY(new SiblingConstraint())
				.setWidth(new PixelConstraint(LEFT_WIDTH));

			maxManualSpeedNumberInput.setSuffix(" km/h");
			maxManualSpeedNumberInput.setValue(siding.getMaxManualSpeed() <= 0 ? RailType.DIAMOND.speedLimit : siding.getMaxManualSpeed() * METERS_PER_MILLISECOND_TO_KILOMETERS_PER_HOUR);

			GuiHelper.createSpacing(manualScrollComponent);
			GuiHelper.createLabel(manualScrollComponent, TranslationProvider.GUI_MTR_MANUAL_TO_AUTOMATIC_TIME.getString());

			drivableTimeoutMinutesNumberInput = (NumberInputComponent) new NumberInputComponent(0, MAX_DRIVABLE_TIMEOUT, 1, false, null)
				.setChildOf(manualScrollComponent)
				.setY(new SiblingConstraint())
				.setWidth(new PixelConstraint(LEFT_WIDTH));

			drivableTimeoutMinutesNumberInput.setSuffix(TranslationProvider.GUI_MTR_MINUTES.getString(""));
			final long dwellTimeMinutes = siding.getManualToAutomaticTime() / Utilities.MILLIS_PER_MINUTE;
			drivableTimeoutMinutesNumberInput.setValue(dwellTimeMinutes);

			drivableTimeoutSecondsNumberInput = (NumberInputComponent) new NumberInputComponent(0, 59.5, 0.5, true, null)
				.setChildOf(manualScrollComponent)
				.setY(new SiblingConstraint())
				.setWidth(new PixelConstraint(LEFT_WIDTH));

			drivableTimeoutSecondsNumberInput.setSuffix(TranslationProvider.GUI_MTR_SECONDS.getString(""));
			drivableTimeoutSecondsNumberInput.setValue(((double) (siding.getManualToAutomaticTime() % Utilities.MILLIS_PER_MINUTE) / Utilities.MILLIS_PER_SECOND));

			setButtons();
		} else {
			maxVehiclesTextInput = null;
			unlimitedVehiclesCheckbox = null;
			accelerationConstantNumberInput = null;
			decelerationConstantNumberInput = null;
			delayedVehicleSpeedIncreasePercentageNumberInput = null;
			delayedVehicleReduceDwellTimePercentageNumberInput = null;
			earlyVehicleIncreaseDwellTimeCheckbox = null;
			isManualCheckbox = null;
			manualScrollComponent = null;
			maxManualSpeedNumberInput = null;
			drivableTimeoutMinutesNumberInput = null;
			drivableTimeoutSecondsNumberInput = null;
		}
	}

	@Override
	public void onTick() {
		super.onTick();
		selectedVehiclesText.setText(TranslationProvider.GUI_MTR_SELECTED_VEHICLE_CARS.getString(data.getVehicleCars().size()));

		if (accelerationConstantNumberInput != null) {
			accelerationConstantNumberInput.setSuffix(accelerationSliderFormatter(accelerationConstantNumberInput.getValue()));
		}

		if (decelerationConstantNumberInput != null) {
			decelerationConstantNumberInput.setSuffix(accelerationSliderFormatter(decelerationConstantNumberInput.getValue()));
		}
	}

	@Override
	protected void close() {
		if (isManualCheckbox != null && unlimitedVehiclesCheckbox != null && maxVehiclesTextInput != null) {
			if (isManualCheckbox.isChecked()) {
				data.setIsManual(true);
			} else if (unlimitedVehiclesCheckbox.isChecked()) {
				data.setUnlimitedVehicles(true);
			} else {
				int maxTrains;
				try {
					maxTrains = Math.max(0, Integer.parseInt(maxVehiclesTextInput.getText()));
				} catch (Exception ignored) {
					maxTrains = 0;
				}
				data.setMaxVehicles(maxTrains);
			}
		}

		if (accelerationConstantNumberInput != null) {
			data.setAcceleration(Utilities.round(Utilities.clampSafe(accelerationConstantNumberInput.getValue() / METERS_PER_MILLISECOND_2_TO_METERS_PER_SECOND_2, Siding.MIN_ACCELERATION, Siding.MAX_ACCELERATION), 8));
		}

		if (decelerationConstantNumberInput != null) {
			data.setDeceleration(Utilities.round(Utilities.clampSafe(decelerationConstantNumberInput.getValue() / METERS_PER_MILLISECOND_2_TO_METERS_PER_SECOND_2, Siding.MIN_ACCELERATION, Siding.MAX_ACCELERATION), 8));
		}

		if (delayedVehicleSpeedIncreasePercentageNumberInput != null) {
			data.setDelayedVehicleSpeedIncreasePercentage((int) delayedVehicleSpeedIncreasePercentageNumberInput.getValue());
		}

		if (delayedVehicleReduceDwellTimePercentageNumberInput != null) {
			data.setDelayedVehicleReduceDwellTimePercentage((int) delayedVehicleReduceDwellTimePercentageNumberInput.getValue());
		}

		if (earlyVehicleIncreaseDwellTimeCheckbox != null) {
			data.setEarlyVehicleIncreaseDwellTime(earlyVehicleIncreaseDwellTimeCheckbox.isChecked());
		}

		if (maxManualSpeedNumberInput != null) {
			data.setMaxManualSpeed(Utilities.kilometersPerHourToMetersPerMillisecond(maxManualSpeedNumberInput.getValue()));
		}

		if (drivableTimeoutMinutesNumberInput != null && drivableTimeoutSecondsNumberInput != null) {
			data.setManualToAutomaticTime((int) (drivableTimeoutMinutesNumberInput.getValue() * Utilities.MILLIS_PER_MINUTE + drivableTimeoutSecondsNumberInput.getValue() * Utilities.MILLIS_PER_SECOND));
		}

		RegistryClient.sendPacketToServer(new PacketUpdateData(new UpdateDataRequest(MinecraftClientData.getDashboardInstance()).addSiding(data)));
	}

	private void maxVehiclesTextFieldCallback() {
		if (maxVehiclesTextInput != null && unlimitedVehiclesCheckbox != null) {
			if (maxVehiclesTextInput.getText().isEmpty()) {
				unlimitedVehiclesCheckbox.setChecked(true);
				if (isManualCheckbox != null) {
					isManualCheckbox.setChecked(false);
				}
			} else {
				unlimitedVehiclesCheckbox.setChecked(false);
			}
			if (!maxVehiclesTextInput.getText().equals("1") && isManualCheckbox != null) {
				isManualCheckbox.setChecked(false);
			}
		}
		setButtons();
	}

	private void maxVehiclesCheckboxCallback() {
		if (maxVehiclesTextInput != null && unlimitedVehiclesCheckbox != null) {
			if (unlimitedVehiclesCheckbox.isChecked()) {
				if (isManualCheckbox != null) {
					isManualCheckbox.setChecked(false);
				}
				if (!maxVehiclesTextInput.getText().isEmpty()) {
					maxVehiclesTextInput.setText("");
				}
			} else if (maxVehiclesTextInput.getText().isEmpty()) {
				maxVehiclesTextInput.setText("1");
			}
		}
		setButtons();
	}

	private void isManualCheckboxCallback() {
		if (maxVehiclesTextInput != null && unlimitedVehiclesCheckbox != null && isManualCheckbox != null && isManualCheckbox.isChecked() && !maxVehiclesTextInput.getText().equals("1")) {
			maxVehiclesTextInput.setText("1");
			unlimitedVehiclesCheckbox.setChecked(false);
		}
		setButtons();
	}

	private void setButtons() {
		if (isManualCheckbox != null && manualScrollComponent != null) {
			if (isManualCheckbox.isChecked()) {
				manualScrollComponent.unhide(true);
			} else {
				manualScrollComponent.hide(true);
			}
		}
	}

	private static String accelerationSliderFormatter(double value) {
		return String.format(" m/s\u00B2 (%s km/h/s)", Utilities.round(value * METERS_PER_SECOND_2_TO_KILOMETERS_PER_HOUR_PER_SECOND, 1));
	}

	private static ObjectImmutableList<ObjectObjectImmutablePair<ReleasedDynamicTexture, String>> getTabs(Siding siding) {
		if (siding.getTransportMode().continuousMovement) {
			return ObjectImmutableList.of(
				new ObjectObjectImmutablePair<>(ReleasedDynamicTextureRegistry.BRUSH_TEXTURE.get(), TranslationProvider.GUI_MTR_STATION.getString())
			);
		} else {
			return ObjectImmutableList.of(
				new ObjectObjectImmutablePair<>(ReleasedDynamicTextureRegistry.BRUSH_TEXTURE.get(), TranslationProvider.GUI_MTR_STATION.getString()),
				new ObjectObjectImmutablePair<>(ReleasedDynamicTextureRegistry.DRIVER_KEY_TEXTURE.get(), TranslationProvider.GUI_MTR_IS_MANUAL.getString())
			);
		}
	}
}
