package mtr.gui;

import mtr.data.DataConverter;
import mtr.data.NameColorDataBase;
import mtr.data.Siding;
import mtr.model.TrainClientRegistry;
import mtr.packet.IPacket;
import mtr.packet.PacketTrainDataGuiClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class SidingScreen extends SavedRailScreenBase<Siding> {

	private boolean isSelectingTrain;

	private final ButtonWidget buttonSelectTrain;
	private final DashboardList availableTrainsList;
	private final WidgetBetterCheckbox buttonUnlimitedTrains;
	private final TextFieldWidget textFieldMaxTrains;

	private static final Text MAX_TRAINS_TEXT = new TranslatableText("gui.mtr.max_trains");
	private static final int MAX_TRAINS_TEXT_LENGTH = 3;
	private static final int MAX_TRAINS_WIDTH = 40;

	public SidingScreen(Siding siding, DashboardScreen dashboardScreen) {
		super(siding, dashboardScreen, MAX_TRAINS_TEXT);
		buttonSelectTrain = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new LiteralText(""), button -> onSelectingTrain());
		availableTrainsList = new DashboardList(this::addButton, this::addChild, null, null, null, null, this::onAdd, null, null, () -> ClientData.TRAINS_SEARCH, text -> ClientData.TRAINS_SEARCH = text);
		textFieldMaxTrains = new TextFieldWidget(MinecraftClient.getInstance().textRenderer, 0, 0, 0, SQUARE_SIZE, new LiteralText(""));
		buttonUnlimitedTrains = new WidgetBetterCheckbox(0, 0, 0, SQUARE_SIZE, new TranslatableText("gui.mtr.unlimited_trains"), checked -> {
			if (checked && !textFieldMaxTrains.getText().isEmpty()) {
				textFieldMaxTrains.setText("");
			} else if (!checked && textFieldMaxTrains.getText().isEmpty()) {
				textFieldMaxTrains.setText("1");
			}
		});
	}

	@Override
	protected void init() {
		super.init();
		setIsSelectingTrain(false);

		IDrawing.setPositionAndWidth(buttonSelectTrain, startX + textWidth, height / 2 + TEXT_FIELD_PADDING / 2, SLIDER_WIDTH);
		IDrawing.setPositionAndWidth(buttonUnlimitedTrains, startX + textWidth + MAX_TRAINS_WIDTH + TEXT_FIELD_PADDING + TEXT_FIELD_PADDING / 2, height / 2 + TEXT_FIELD_PADDING + TEXT_FIELD_PADDING / 2 + SQUARE_SIZE, SLIDER_WIDTH);

		addButton(buttonSelectTrain);
		addButton(buttonUnlimitedTrains);

		availableTrainsList.y = SQUARE_SIZE * 2;
		availableTrainsList.height = height - SQUARE_SIZE * 5;
		availableTrainsList.width = PANEL_WIDTH;
		availableTrainsList.init();

		buttonUnlimitedTrains.setChecked(savedRailBase.getUnlimitedTrains());

		IDrawing.setPositionAndWidth(textFieldMaxTrains, startX + textWidth + TEXT_FIELD_PADDING / 2, height / 2 + TEXT_FIELD_PADDING + TEXT_FIELD_PADDING / 2 + SQUARE_SIZE, MAX_TRAINS_WIDTH - TEXT_FIELD_PADDING);
		textFieldMaxTrains.setText(savedRailBase.getUnlimitedTrains() ? "" : String.valueOf(savedRailBase.getMaxTrains() + 1));
		textFieldMaxTrains.setMaxLength(MAX_TRAINS_TEXT_LENGTH);
		textFieldMaxTrains.setChangedListener(text -> {
			final String newText = text.replaceAll("[^0-9]", "");
			if (!newText.equals(text)) {
				textFieldMaxTrains.setText(newText);
			}
			buttonUnlimitedTrains.setChecked(newText.isEmpty());
		});

		addChild(textFieldMaxTrains);
	}

	@Override
	public void tick() {
		super.tick();
		availableTrainsList.tick();
		textFieldMaxTrains.tick();
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		super.render(matrices, mouseX, mouseY, delta);
		textFieldMaxTrains.render(matrices, mouseX, mouseY, delta);
		if (!isSelectingTrain) {
			textRenderer.draw(matrices, MAX_TRAINS_TEXT, startX, height / 2F + TEXT_FIELD_PADDING + TEXT_FIELD_PADDING / 2F + TEXT_PADDING + SQUARE_SIZE, ARGB_WHITE);
		}
	}

	@Override
	public void onClose() {
		int maxTrains;
		try {
			maxTrains = Math.max(0, Integer.parseInt(textFieldMaxTrains.getText()) - 1);
		} catch (Exception ignored) {
			maxTrains = 0;
		}
		savedRailBase.setUnlimitedTrains(buttonUnlimitedTrains.isChecked(), maxTrains, packet -> PacketTrainDataGuiClient.sendUpdate(getPacketIdentifier(), packet));
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
		TrainClientRegistry.forEach((id, trainProperties) -> trainList.add(new DataConverter(trainProperties.name.getString(), trainProperties.color)));
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
		savedRailBase.setTrainIdAndBaseType(TrainClientRegistry.getTrainId(index), TrainClientRegistry.getTrainProperties(index).baseTrainType, packet -> PacketTrainDataGuiClient.sendUpdate(IPacket.PACKET_UPDATE_SIDING, packet));
		setIsSelectingTrain(false);
	}
}
