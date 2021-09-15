package mtr.gui;

import mtr.config.CustomResources;
import mtr.data.DataConverter;
import mtr.data.NameColorDataBase;
import mtr.data.Siding;
import mtr.data.TrainType;
import mtr.packet.IPacket;
import mtr.packet.PacketTrainDataGuiClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SidingScreen extends SavedRailScreenBase<Siding> {

	private boolean isSelectingTrain;

	private final ButtonWidget buttonSelectTrain;
	private final DashboardList availableTrainsList;
	private final WidgetBetterCheckbox buttonUnlimitedTrains;

	public SidingScreen(Siding siding, DashboardScreen dashboardScreen) {
		super(siding, dashboardScreen);
		buttonSelectTrain = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new LiteralText(""), button -> onSelectingTrain());
		availableTrainsList = new DashboardList(this::addButton, this::addChild, null, null, null, null, this::onAdd, null, null);
		buttonUnlimitedTrains = new WidgetBetterCheckbox(0, 0, 0, SQUARE_SIZE, new TranslatableText("gui.mtr.unlimited_trains"), checked -> savedRailBase.setUnlimitedTrains(checked, packet -> PacketTrainDataGuiClient.sendUpdate(PACKET_UPDATE_SIDING, packet)));
	}

	@Override
	protected void init() {
		super.init();
		setIsSelectingTrain(false);

		IDrawing.setPositionAndWidth(buttonSelectTrain, startX + textWidth, height / 2 + TEXT_FIELD_PADDING / 2, SLIDER_WIDTH);
		IDrawing.setPositionAndWidth(buttonUnlimitedTrains, startX, height / 2 + TEXT_FIELD_PADDING / 2 + SQUARE_SIZE, SLIDER_WIDTH + textWidth);

		addButton(buttonSelectTrain);
		addButton(buttonUnlimitedTrains);

		availableTrainsList.y = SQUARE_SIZE * 2;
		availableTrainsList.height = height - SQUARE_SIZE * 5;
		availableTrainsList.width = PANEL_WIDTH;
		availableTrainsList.init();

		buttonUnlimitedTrains.setChecked(savedRailBase.getUnlimitedTrains());
	}

	@Override
	public void tick() {
		super.tick();
		availableTrainsList.tick();
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		try {
			renderBackground(matrices);
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.render(matrices, mouseX, mouseY, delta);
	}

	@Override
	public void onClose() {
		savedRailBase.setNameColor(packet -> PacketTrainDataGuiClient.sendUpdate(getPacketIdentifier(), packet));
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
	protected void renderExtra(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		availableTrainsList.render(matrices, textRenderer, mouseX, mouseY, delta);
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
	protected Identifier getPacketIdentifier() {
		return PACKET_UPDATE_SIDING;
	}

	private void onSelectingTrain() {
		final List<DataConverter> trainList = new ArrayList<>();
		Arrays.stream(TrainType.values()).map(trainType -> new DataConverter(trainType.getName(), trainType.color)).forEach(trainList::add);

		final List<String> sortedKeys = new ArrayList<>(CustomResources.customTrains.keySet());
		Collections.sort(sortedKeys);
		sortedKeys.forEach(key -> {
			final CustomResources.CustomTrain customTrain = CustomResources.customTrains.get(key);
			trainList.add(new DataConverter(customTrain.name, customTrain.color));
		});

		availableTrainsList.setData(trainList, false, false, false, false, true, false);
		setIsSelectingTrain(true);
	}

	private void setIsSelectingTrain(boolean isSelectingTrain) {
		this.isSelectingTrain = isSelectingTrain;
		buttonSelectTrain.visible = !isSelectingTrain;
		buttonUnlimitedTrains.visible = !isSelectingTrain;
		final CustomResources.TrainMapping trainMapping = savedRailBase.getTrainMapping();
		buttonSelectTrain.setMessage(CustomResources.customTrains.containsKey(trainMapping.customId) ? new LiteralText(CustomResources.customTrains.get(trainMapping.customId).name) : new TranslatableText("train.mtr." + trainMapping.trainType));
		availableTrainsList.x = isSelectingTrain ? width / 2 - PANEL_WIDTH / 2 : width;
	}

	private void onAdd(NameColorDataBase data, int index) {
		final int trainTypesCount = TrainType.values().length;
		final int customTrainCount = CustomResources.customTrains.size();

		if (index < trainTypesCount + customTrainCount) {
			final String customId;
			final TrainType trainType;
			if (index < trainTypesCount) {
				customId = "";
				trainType = TrainType.values()[index];
			} else {
				final List<String> sortedKeys = new ArrayList<>(CustomResources.customTrains.keySet());
				Collections.sort(sortedKeys);
				customId = sortedKeys.get(index - trainTypesCount);
				trainType = customId == null ? null : CustomResources.customTrains.get(customId).baseTrainType;
			}

			if (trainType != null) {
				savedRailBase.setTrainMapping(customId, trainType, packet -> PacketTrainDataGuiClient.sendUpdate(IPacket.PACKET_UPDATE_SIDING, packet));
			}
		}
		setIsSelectingTrain(false);
	}
}
