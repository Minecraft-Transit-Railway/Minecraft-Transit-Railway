package mtr.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import mtr.client.ClientData;
import mtr.client.IDrawing;
import mtr.client.TrainClientRegistry;
import mtr.data.*;
import mtr.packet.IPacket;
import mtr.packet.PacketTrainDataGuiClient;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import java.util.ArrayList;
import java.util.List;

public class SidingScreen extends SavedRailScreenBase<Siding> {

	private boolean isSelectingTrain;
	private final float oldAcceleration;

	private final TransportMode transportMode;
	private final Button buttonSelectTrain;
	private final DashboardList availableTrainsList;
	private final WidgetBetterCheckbox buttonUnlimitedTrains;
	private final WidgetBetterTextField textFieldMaxTrains;
	private final WidgetShorterSlider sliderAccelerationConstant;

	private static final Component SELECTED_TRAIN_TEXT = new TranslatableComponent("gui.mtr.selected_train");
	private static final Component MAX_TRAINS_TEXT = new TranslatableComponent("gui.mtr.max_trains");
	private static final Component ACCELERATION_CONSTANT_TEXT = new TranslatableComponent("gui.mtr.acceleration");
	private static final int MAX_TRAINS_TEXT_LENGTH = 3;
	private static final int MAX_TRAINS_WIDTH = 80;
	private static final float MAX_ACCELERATION = 0.05F; // m/tick^2
	private static final float MIN_ACCELERATION = 0.001F; // m/tick^2
	private static final int SLIDER_SCALE = 1000;
	private static final float ACCELERATION_UNIT_CONVERSION_1 = 20 * 20; // m/tick^2 to m/s^2
	private static final float ACCELERATION_UNIT_CONVERSION_2 = ACCELERATION_UNIT_CONVERSION_1 * 3.6F; // m/tick^2 to km/h/s

	public SidingScreen(Siding siding, TransportMode transportMode, DashboardScreen dashboardScreen) {
		super(siding, dashboardScreen, SELECTED_TRAIN_TEXT, MAX_TRAINS_TEXT, ACCELERATION_CONSTANT_TEXT);
		this.transportMode = transportMode;
		buttonSelectTrain = new Button(0, 0, 0, SQUARE_SIZE, new TextComponent(""), button -> onSelectingTrain());
		availableTrainsList = new DashboardList(null, null, null, null, this::onAdd, null, null, () -> ClientData.TRAINS_SEARCH, text -> ClientData.TRAINS_SEARCH = text);
		textFieldMaxTrains = new WidgetBetterTextField(WidgetBetterTextField.TextFieldFilter.POSITIVE_INTEGER, "", MAX_TRAINS_TEXT_LENGTH);
		sliderAccelerationConstant = new WidgetShorterSlider(0, MAX_TRAINS_WIDTH, Math.round((MAX_ACCELERATION - MIN_ACCELERATION) * SLIDER_SCALE), this::sliderFormatter, null);
		buttonUnlimitedTrains = new WidgetBetterCheckbox(0, 0, 0, SQUARE_SIZE, new TranslatableComponent("gui.mtr.unlimited_trains"), checked -> {
			if (checked && !textFieldMaxTrains.getValue().isEmpty()) {
				textFieldMaxTrains.setValue("");
			} else if (!checked && textFieldMaxTrains.getValue().isEmpty()) {
				textFieldMaxTrains.setValue("1");
			}
		});
		oldAcceleration = savedRailBase.getAccelerationConstant();
	}

	@Override
	protected void init() {
		super.init();
		setIsSelectingTrain(false);

		IDrawing.setPositionAndWidth(buttonSelectTrain, startX + textWidth, height / 2 + TEXT_FIELD_PADDING / 2, SLIDER_WIDTH);
		IDrawing.setPositionAndWidth(buttonUnlimitedTrains, startX + textWidth + MAX_TRAINS_WIDTH + TEXT_FIELD_PADDING + TEXT_FIELD_PADDING / 2, height / 2 + TEXT_FIELD_PADDING + TEXT_FIELD_PADDING / 2 + SQUARE_SIZE, SLIDER_WIDTH);

		addDrawableChild(buttonSelectTrain);
		addDrawableChild(buttonUnlimitedTrains);

		availableTrainsList.y = SQUARE_SIZE * 2;
		availableTrainsList.height = height - SQUARE_SIZE * 5;
		availableTrainsList.width = SLIDER_WIDTH;
		availableTrainsList.init(this::addDrawableChild);

		buttonUnlimitedTrains.setChecked(savedRailBase.getUnlimitedTrains());

		IDrawing.setPositionAndWidth(textFieldMaxTrains, startX + textWidth + TEXT_FIELD_PADDING / 2, height / 2 + TEXT_FIELD_PADDING + TEXT_FIELD_PADDING / 2 + SQUARE_SIZE, MAX_TRAINS_WIDTH - TEXT_FIELD_PADDING);
		textFieldMaxTrains.setValue(savedRailBase.getUnlimitedTrains() ? "" : String.valueOf(savedRailBase.getMaxTrains() + 1));
		textFieldMaxTrains.setResponder(text -> buttonUnlimitedTrains.setChecked(text.isEmpty()));

		sliderAccelerationConstant.x = startX + textWidth;
		sliderAccelerationConstant.y = height / 2 + TEXT_FIELD_PADDING * 2 + TEXT_FIELD_PADDING / 2 + SQUARE_SIZE * 2;
		sliderAccelerationConstant.setHeight(SQUARE_SIZE);
		sliderAccelerationConstant.setValue(Math.round((savedRailBase.getAccelerationConstant() - MIN_ACCELERATION) * SLIDER_SCALE));

		addDrawableChild(textFieldMaxTrains);
		addDrawableChild(sliderAccelerationConstant);
	}

	@Override
	public void tick() {
		super.tick();
		availableTrainsList.tick();
		textFieldMaxTrains.tick();
	}

	@Override
	public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
		super.render(matrices, mouseX, mouseY, delta);
		if (!isSelectingTrain) {
			font.draw(matrices, SELECTED_TRAIN_TEXT, startX, height / 2F + TEXT_FIELD_PADDING / 2F + TEXT_PADDING, ARGB_WHITE);
			font.draw(matrices, MAX_TRAINS_TEXT, startX, height / 2F + TEXT_FIELD_PADDING + TEXT_FIELD_PADDING / 2F + TEXT_PADDING + SQUARE_SIZE, ARGB_WHITE);
			font.draw(matrices, ACCELERATION_CONSTANT_TEXT, startX, height / 2F + TEXT_FIELD_PADDING * 2 + TEXT_FIELD_PADDING / 2F + TEXT_PADDING + SQUARE_SIZE * 2, ARGB_WHITE);
		}
	}

	@Override
	public void onClose() {
		int maxTrains;
		try {
			maxTrains = Math.max(0, Integer.parseInt(textFieldMaxTrains.getValue()) - 1);
		} catch (Exception ignored) {
			maxTrains = 0;
		}
		float accelerationConstant;
		try {
			accelerationConstant = RailwayData.round(Mth.clamp((float) sliderAccelerationConstant.getIntValue() / SLIDER_SCALE + MIN_ACCELERATION, MIN_ACCELERATION, MAX_ACCELERATION), 3);
		} catch (Exception ignored) {
			accelerationConstant = Train.ACCELERATION_DEFAULT;
		}
		savedRailBase.setUnlimitedTrains(buttonUnlimitedTrains.selected(), maxTrains, oldAcceleration == accelerationConstant ? 0 : accelerationConstant, packet -> PacketTrainDataGuiClient.sendUpdate(getPacketIdentifier(), packet));
		super.onClose();
	}

	@Override
	public void mouseMoved(double mouseX, double mouseY) {
		availableTrainsList.mouseMoved(mouseX, mouseY);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
		availableTrainsList.mouseScrolled(mouseX, mouseY, amount);
		return super.mouseScrolled(mouseX, mouseY, amount);
	}

	@Override
	protected boolean shouldRenderExtra() {
		return isSelectingTrain;
	}

	@Override
	protected void renderExtra(PoseStack matrices, int mouseX, int mouseY, float delta) {
		availableTrainsList.render(matrices, font);
	}

	@Override
	protected String getNumberStringKey() {
		return "gui.mtr.siding_number";
	}

	@Override
	protected ResourceLocation getPacketIdentifier() {
		return PACKET_UPDATE_SIDING;
	}

	private void onSelectingTrain() {
		final List<DataConverter> trainList = new ArrayList<>();
		TrainClientRegistry.forEach(transportMode, (id, trainProperties) -> trainList.add(new DataConverter(trainProperties.name.getString(), trainProperties.color)));
		availableTrainsList.setData(trainList, false, false, false, false, true, false);
		setIsSelectingTrain(true);
	}

	private void setIsSelectingTrain(boolean isSelectingTrain) {
		this.isSelectingTrain = isSelectingTrain;
		buttonSelectTrain.visible = !isSelectingTrain;
		buttonUnlimitedTrains.visible = !isSelectingTrain;
		textFieldMaxTrains.visible = !isSelectingTrain;
		sliderAccelerationConstant.visible = !isSelectingTrain;
		buttonSelectTrain.setMessage(TrainClientRegistry.getTrainProperties(savedRailBase.getTrainId(), savedRailBase.getBaseTrainType()).name);
		availableTrainsList.x = isSelectingTrain ? width / 2 - SLIDER_WIDTH / 2 : width;
	}

	private void onAdd(NameColorDataBase data, int index) {
		savedRailBase.setTrainIdAndBaseType(TrainClientRegistry.getTrainId(transportMode, index), TrainClientRegistry.getTrainProperties(transportMode, index).baseTrainType, packet -> PacketTrainDataGuiClient.sendUpdate(IPacket.PACKET_UPDATE_SIDING, packet));
		setIsSelectingTrain(false);
	}

	private String sliderFormatter(int value) {
		final float valueMeterPerTickSquared = ((float) value / SLIDER_SCALE + MIN_ACCELERATION);
		return String.format("%s m/s² (%s km/h/s)", RailwayData.round(valueMeterPerTickSquared * ACCELERATION_UNIT_CONVERSION_1, 1), RailwayData.round(valueMeterPerTickSquared * ACCELERATION_UNIT_CONVERSION_2, 1));
	}
}
