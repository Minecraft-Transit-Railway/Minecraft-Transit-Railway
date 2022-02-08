package mtr.gui;

import mtr.data.*;
import mtr.model.TrainClientRegistry;
import mtr.packet.IPacket;
import mtr.packet.PacketTrainDataGuiClient;
import net.minecraft.client.gui.widget.ButtonWidget;
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
	private final WidgetBetterCheckbox buttonTrainBarrier;
	private final WidgetBetterTextField textFieldMaxTrains;
	private static final Text MAX_TRAINS_TEXT = new TranslatableText("gui.mtr.max_trains");
	private static final int MAX_TRAINS_TEXT_LENGTH = 3;
	private static final int MAX_TRAINS_WIDTH = 40;

	public SidingScreen(Siding siding, DashboardScreen dashboardScreen) {
		super(siding, dashboardScreen, MAX_TRAINS_TEXT);
		buttonSelectTrain = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new LiteralText(""), button -> onSelectingTrain());
		availableTrainsList = new DashboardList(null, null, null, null, this::onAdd, null, null, () -> ClientData.TRAINS_SEARCH, text -> ClientData.TRAINS_SEARCH = text);
		textFieldMaxTrains = new WidgetBetterTextField(WidgetBetterTextField.TextFieldFilter.POSITIVE_INTEGER, "", MAX_TRAINS_TEXT_LENGTH);
		buttonUnlimitedTrains = new WidgetBetterCheckbox(0, 0, 0, SQUARE_SIZE, new TranslatableText("gui.mtr.unlimited_trains"), checked -> {
			if (checked && !textFieldMaxTrains.getText().isEmpty()) {
				textFieldMaxTrains.setText("");
			} else if (!checked && textFieldMaxTrains.getText().isEmpty()) {
				textFieldMaxTrains.setText("1");
			}
		});
		buttonTrainBarrier = new WidgetBetterCheckbox(0, 0, 0, SQUARE_SIZE, new TranslatableText("gui.mtr.train_barrier_button"), checked -> {
				if (checked) {
					if (siding.getBaseTrainType() == TrainType.R179) {
						savedRailBase.setTrainIdAndBaseType("r179_tb", TrainType.R179_TB, packet -> PacketTrainDataGuiClient.sendUpdate(IPacket.PACKET_UPDATE_SIDING, packet));
					}
				} else if (!checked) {
					if (siding.getBaseTrainType() == TrainType.R179_TB) {
						savedRailBase.setTrainIdAndBaseType("r179", TrainType.R179, packet -> PacketTrainDataGuiClient.sendUpdate(IPacket.PACKET_UPDATE_SIDING, packet));
					}
			   }
		});
	}

	@Override
	protected void init() {
		super.init();
		setIsSelectingTrain(false);

		IDrawing.setPositionAndWidth(buttonSelectTrain, startX + textWidth, height / 2 + TEXT_FIELD_PADDING / 2, SLIDER_WIDTH);
		IDrawing.setPositionAndWidth(buttonUnlimitedTrains, startX + textWidth + MAX_TRAINS_WIDTH + TEXT_FIELD_PADDING + TEXT_FIELD_PADDING / 2, height / 2 + TEXT_FIELD_PADDING + TEXT_FIELD_PADDING / 2 + SQUARE_SIZE, SLIDER_WIDTH);
		IDrawing.setPositionAndWidth(buttonTrainBarrier, startX + 179, height / 2 + SQUARE_SIZE * 4 - 31, SLIDER_WIDTH);

		addDrawableChild(buttonSelectTrain);
		addDrawableChild(buttonUnlimitedTrains);
		addDrawableChild(buttonTrainBarrier);

		availableTrainsList.y = SQUARE_SIZE * 2;
		availableTrainsList.height = height - SQUARE_SIZE * 5;
		availableTrainsList.width = PANEL_WIDTH;
		availableTrainsList.init(this::addDrawableChild);

		buttonUnlimitedTrains.setChecked(savedRailBase.getUnlimitedTrains());
		buttonTrainBarrier.setChecked(savedRailBase.getTrainBarrier());

		IDrawing.setPositionAndWidth(textFieldMaxTrains, startX + textWidth + TEXT_FIELD_PADDING / 2, height / 2 + TEXT_FIELD_PADDING + TEXT_FIELD_PADDING / 2 + SQUARE_SIZE, MAX_TRAINS_WIDTH - TEXT_FIELD_PADDING);
		textFieldMaxTrains.setText(savedRailBase.getUnlimitedTrains() ? "" : String.valueOf(savedRailBase.getMaxTrains() + 1));
		textFieldMaxTrains.setChangedListener(text -> buttonUnlimitedTrains.setChecked(text.isEmpty()));

		addDrawableChild(textFieldMaxTrains);
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
		savedRailBase.setTrainBarrier(buttonTrainBarrier.isChecked());
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
		availableTrainsList.render(matrices, textRenderer);
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
		buttonTrainBarrier.visible = !isSelectingTrain;
		availableTrainsList.x = isSelectingTrain ? width / 2 - PANEL_WIDTH / 2 : width;
	}

	private void onAdd(NameColorDataBase data, int index) {
		savedRailBase.setTrainIdAndBaseType(TrainClientRegistry.getTrainId(index), TrainClientRegistry.getTrainProperties(index).baseTrainType, packet -> PacketTrainDataGuiClient.sendUpdate(IPacket.PACKET_UPDATE_SIDING, packet));
		setIsSelectingTrain(false);
		SidingScreen sidingScreen = new SidingScreen( savedRailBase, new DashboardScreen());
		if (buttonTrainBarrier.isChecked() && savedRailBase.getBaseTrainType() == TrainType.R179) {
			sidingScreen.savedRailBase.getBaseTrainType();
			savedRailBase.setTrainIdAndBaseType("r179_tb", TrainType.R179_TB, packet -> PacketTrainDataGuiClient.sendUpdate(IPacket.PACKET_UPDATE_SIDING, packet));
		}
	}
}



