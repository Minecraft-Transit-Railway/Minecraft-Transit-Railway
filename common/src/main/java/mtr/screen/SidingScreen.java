package mtr.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import mtr.client.ClientData;
import mtr.client.IDrawing;
import mtr.client.TrainClientRegistry;
import mtr.data.DataConverter;
import mtr.data.NameColorDataBase;
import mtr.data.Siding;
import mtr.data.TransportMode;
import mtr.packet.IPacket;
import mtr.packet.PacketTrainDataGuiClient;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class SidingScreen extends SavedRailScreenBase<Siding> {

	private boolean isSelectingTrain;

	private final TransportMode transportMode;
	private final Button buttonSelectTrain;
	private final DashboardList availableTrainsList;
	private final WidgetBetterCheckbox buttonUnlimitedTrains;
	private final WidgetBetterTextField textFieldMaxTrains;

	private static final Component MAX_TRAINS_TEXT = new TranslatableComponent("gui.mtr.max_trains");
	private static final int MAX_TRAINS_TEXT_LENGTH = 3;
	private static final int MAX_TRAINS_WIDTH = 40;

	public SidingScreen(Siding siding, TransportMode transportMode, DashboardScreen dashboardScreen) {
		super(siding, dashboardScreen, MAX_TRAINS_TEXT);
		this.transportMode = transportMode;
		buttonSelectTrain = new Button(0, 0, 0, SQUARE_SIZE, new TextComponent(""), button -> onSelectingTrain());
		availableTrainsList = new DashboardList(null, null, null, null, this::onAdd, null, null, () -> ClientData.TRAINS_SEARCH, text -> ClientData.TRAINS_SEARCH = text);
		textFieldMaxTrains = new WidgetBetterTextField(WidgetBetterTextField.TextFieldFilter.POSITIVE_INTEGER, "", MAX_TRAINS_TEXT_LENGTH);
		buttonUnlimitedTrains = new WidgetBetterCheckbox(0, 0, 0, SQUARE_SIZE, new TranslatableComponent("gui.mtr.unlimited_trains"), checked -> {
			if (checked && !textFieldMaxTrains.getValue().isEmpty()) {
				textFieldMaxTrains.setValue("");
			} else if (!checked && textFieldMaxTrains.getValue().isEmpty()) {
				textFieldMaxTrains.setValue("1");
			}
		});
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
		availableTrainsList.width = PANEL_WIDTH;
		availableTrainsList.init(this::addDrawableChild);

		buttonUnlimitedTrains.setChecked(savedRailBase.getUnlimitedTrains());

		IDrawing.setPositionAndWidth(textFieldMaxTrains, startX + textWidth + TEXT_FIELD_PADDING / 2, height / 2 + TEXT_FIELD_PADDING + TEXT_FIELD_PADDING / 2 + SQUARE_SIZE, MAX_TRAINS_WIDTH - TEXT_FIELD_PADDING);
		textFieldMaxTrains.setValue(savedRailBase.getUnlimitedTrains() ? "" : String.valueOf(savedRailBase.getMaxTrains() + 1));
		textFieldMaxTrains.setResponder(text -> buttonUnlimitedTrains.setChecked(text.isEmpty()));

		addDrawableChild(textFieldMaxTrains);
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
			font.draw(matrices, MAX_TRAINS_TEXT, startX, height / 2F + TEXT_FIELD_PADDING + TEXT_FIELD_PADDING / 2F + TEXT_PADDING + SQUARE_SIZE, ARGB_WHITE);
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
		savedRailBase.setUnlimitedTrains(buttonUnlimitedTrains.selected(), maxTrains, packet -> PacketTrainDataGuiClient.sendUpdate(getPacketIdentifier(), packet));
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
	protected String getSecondStringKey() {
		return "gui.mtr.selected_train";
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
		buttonSelectTrain.setMessage(TrainClientRegistry.getTrainProperties(savedRailBase.getTrainId(), savedRailBase.getBaseTrainType()).name);
		availableTrainsList.x = isSelectingTrain ? width / 2 - PANEL_WIDTH / 2 : width;
	}

	private void onAdd(NameColorDataBase data, int index) {
		savedRailBase.setTrainIdAndBaseType(TrainClientRegistry.getTrainId(transportMode, index), TrainClientRegistry.getTrainProperties(transportMode, index).baseTrainType, packet -> PacketTrainDataGuiClient.sendUpdate(IPacket.PACKET_UPDATE_SIDING, packet));
		setIsSelectingTrain(false);
	}
}
