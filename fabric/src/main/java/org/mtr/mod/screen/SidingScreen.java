package org.mtr.mod.screen;

import org.mtr.core.data.Depot;
import org.mtr.core.data.Siding;
import org.mtr.core.data.TransportMode;
import org.mtr.core.servlet.IntegrationServlet;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectSet;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.*;
import org.mtr.mapping.tool.TextCase;
import org.mtr.mod.Icons;
import org.mtr.mod.InitClient;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.data.RailType;
import org.mtr.mod.packet.PacketData;

public class SidingScreen extends SavedRailScreenBase<Siding, Depot> implements Icons {

	private final ButtonWidgetExtension buttonSelectTrain;
	private final CheckboxWidgetExtension buttonUnlimitedTrains;
	private final TextFieldWidgetExtension textFieldMaxTrains;
	private final WidgetShorterSlider sliderAccelerationConstant;
	private final CheckboxWidgetExtension buttonIsManual;
	private final WidgetShorterSlider sliderMaxManualSpeed;

	private static final MutableText SELECTED_TRAIN_TEXT = TextHelper.translatable("gui.mtr.selected_vehicle");
	private static final MutableText MAX_TRAINS_TEXT = TextHelper.translatable("gui.mtr.max_vehicles");
	private static final MutableText ACCELERATION_CONSTANT_TEXT = TextHelper.translatable("gui.mtr.acceleration");
	private static final MutableText MANUAL_TO_AUTOMATIC_TIME = TextHelper.translatable("gui.mtr.manual_to_automatic_time");
	private static final MutableText MAX_MANUAL_SPEED = TextHelper.translatable("gui.mtr.max_manual_speed");
	private static final int MAX_TRAINS_TEXT_LENGTH = 3;
	private static final int MAX_TRAINS_WIDTH = 80;
	private static final int SLIDER_SCALE = 1000 * 50 * 50;
	private static final float ACCELERATION_UNIT_CONVERSION_1 = 1000 * 1000; // m/ms^2 to m/s^2
	private static final float ACCELERATION_UNIT_CONVERSION_2 = ACCELERATION_UNIT_CONVERSION_1 * 3.6F; // m/ms^2 to km/h/s

	public SidingScreen(Siding siding, TransportMode transportMode, DashboardScreen dashboardScreen) {
		super(siding, transportMode, dashboardScreen, SELECTED_TRAIN_TEXT, MAX_TRAINS_TEXT, ACCELERATION_CONSTANT_TEXT, MANUAL_TO_AUTOMATIC_TIME, MAX_MANUAL_SPEED);
		buttonSelectTrain = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, button -> MinecraftClient.getInstance().openScreen(new Screen(new VehicleSelectorScreen(this, savedRailBase))));
		buttonSelectTrain.setMessage2(new Text(TextHelper.translatable("selectWorld.edit").data));
		textFieldMaxTrains = new TextFieldWidgetExtension(0, 0, 0, SQUARE_SIZE, MAX_TRAINS_TEXT_LENGTH, TextCase.DEFAULT, "\\D", null);
		sliderAccelerationConstant = new WidgetShorterSlider(0, MAX_TRAINS_WIDTH, (int) Math.round((Siding.MAX_ACCELERATION - Siding.MIN_ACCELERATION) * SLIDER_SCALE), this::accelerationSliderFormatter, null);
		buttonIsManual = new CheckboxWidgetExtension(0, 0, 0, SQUARE_SIZE, true, checked -> {
			if (checked && !textFieldMaxTrains.getText2().equals("1")) {
				textFieldMaxTrains.setText2("1");
			}
			setButtons();
		});
		buttonIsManual.setMessage2(new Text(TextHelper.translatable("gui.mtr.is_manual").data));
		sliderMaxManualSpeed = new WidgetShorterSlider(0, MAX_TRAINS_WIDTH, RailType.DIAMOND.ordinal(), this::speedSliderFormatter, null);
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
		buttonUnlimitedTrains.setMessage2(new Text(TextHelper.translatable("gui.mtr.unlimited_vehicles").data));
	}

	@Override
	protected void init2() {
		super.init2();

		IDrawing.setPositionAndWidth(buttonSelectTrain, SQUARE_SIZE + textWidth, SQUARE_SIZE * 2 + TEXT_FIELD_PADDING, width - textWidth - SQUARE_SIZE * 2);
		IDrawing.setPositionAndWidth(buttonUnlimitedTrains, SQUARE_SIZE + textWidth + MAX_TRAINS_WIDTH + TEXT_FIELD_PADDING + TEXT_FIELD_PADDING, SQUARE_SIZE * 3 + TEXT_FIELD_PADDING * 3 / 2, width - textWidth - SQUARE_SIZE * 2);

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
		sliderAccelerationConstant.setHeight2(SQUARE_SIZE);
		sliderAccelerationConstant.setValue((int) Math.round((savedRailBase.getAcceleration() - Siding.MIN_ACCELERATION) * SLIDER_SCALE));

		IDrawing.setPositionAndWidth(buttonIsManual, SQUARE_SIZE, SQUARE_SIZE * 6 + TEXT_FIELD_PADDING * 2, width - textWidth - SQUARE_SIZE * 2);

		sliderMaxManualSpeed.setX2(SQUARE_SIZE + textWidth);
		sliderMaxManualSpeed.setY2(SQUARE_SIZE * 7 + TEXT_FIELD_PADDING * 2);
		sliderMaxManualSpeed.setHeight2(SQUARE_SIZE);
		sliderMaxManualSpeed.setValue(0); // TODO

		sliderDwellTimeMin.setY2(SQUARE_SIZE * 8 + TEXT_FIELD_PADDING * 2);
		sliderDwellTimeSec.setY2(SQUARE_SIZE * 17 / 2 + TEXT_FIELD_PADDING * 2);

		if (showScheduleControls) {
			addChild(new ClickableWidget(buttonUnlimitedTrains));
			addChild(new ClickableWidget(textFieldMaxTrains));
			addChild(new ClickableWidget(sliderAccelerationConstant));
			addChild(new ClickableWidget(buttonIsManual));
			addChild(new ClickableWidget(sliderMaxManualSpeed));
		}

		setButtons();
	}

	@Override
	public void tick2() {
		super.tick2();
		textFieldMaxTrains.tick3();
	}

	@Override
	public void render(GraphicsHolder graphicsHolder, int mouseX, int mouseY, float delta) {
		super.render(graphicsHolder, mouseX, mouseY, delta);
		graphicsHolder.drawText(SELECTED_TRAIN_TEXT, SQUARE_SIZE, SQUARE_SIZE * 2 + TEXT_FIELD_PADDING + TEXT_PADDING, ARGB_WHITE, false, MAX_LIGHT_GLOWING);
		if (showScheduleControls) {
			graphicsHolder.drawText(MAX_TRAINS_TEXT, SQUARE_SIZE, SQUARE_SIZE * 3 + TEXT_FIELD_PADDING * 3 / 2 + TEXT_PADDING, ARGB_WHITE, false, MAX_LIGHT_GLOWING);
			graphicsHolder.drawText(ACCELERATION_CONSTANT_TEXT, SQUARE_SIZE, SQUARE_SIZE * 4 + TEXT_FIELD_PADDING * 2 + TEXT_PADDING, ARGB_WHITE, false, MAX_LIGHT_GLOWING);
			if (buttonIsManual.isChecked2()) {
				graphicsHolder.drawText(MAX_MANUAL_SPEED, SQUARE_SIZE, SQUARE_SIZE * 7 + TEXT_FIELD_PADDING * 2 + TEXT_PADDING, ARGB_WHITE, false, MAX_LIGHT_GLOWING);
				graphicsHolder.drawText(MANUAL_TO_AUTOMATIC_TIME, SQUARE_SIZE, SQUARE_SIZE * 8 + TEXT_FIELD_PADDING * 2 + TEXT_PADDING, ARGB_WHITE, false, MAX_LIGHT_GLOWING);
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

		if (buttonIsManual.isChecked2()) {
			savedRailBase.setIsManual(true);
		} else if (buttonUnlimitedTrains.isChecked2()) {
			savedRailBase.setUnlimitedVehicles(true);
		} else {
			savedRailBase.setMaxVehicles(maxTrains);
		}
		savedRailBase.setAcceleration(accelerationConstant);

		InitClient.REGISTRY_CLIENT.sendPacketToServer(PacketData.fromSidings(IntegrationServlet.Operation.UPDATE, ObjectSet.of(savedRailBase)));

		super.onClose2();
	}

	@Override
	protected String getNumberStringKey() {
		return "gui.mtr.siding_number";
	}

	private void setButtons() {
		sliderMaxManualSpeed.visible = buttonIsManual.isChecked2();
		sliderDwellTimeMin.visible = buttonIsManual.isChecked2();
		sliderDwellTimeSec.visible = buttonIsManual.isChecked2();
	}

	private String accelerationSliderFormatter(int value) {
		final double valueMeterPerMillisecondSquared = ((double) value / SLIDER_SCALE + Siding.MIN_ACCELERATION);
		return String.format("%s m/s² (%s km/h/s)", Utilities.round(valueMeterPerMillisecondSquared * ACCELERATION_UNIT_CONVERSION_1, 1), Utilities.round(valueMeterPerMillisecondSquared * ACCELERATION_UNIT_CONVERSION_2, 1));
	}

	private String speedSliderFormatter(int value) {
		final RailType railType = convertMaxManualSpeed(value);
		return railType == null ? TextHelper.translatable("gui.mtr.unlimited").getString() : String.format("%s km/h", railType.speedLimit);
	}

	private static RailType convertMaxManualSpeed(int maxManualSpeed) {
		if (maxManualSpeed >= 0 && maxManualSpeed <= RailType.DIAMOND.ordinal()) {
			return RailType.values()[maxManualSpeed];
		} else {
			return null;
		}
	}
}
