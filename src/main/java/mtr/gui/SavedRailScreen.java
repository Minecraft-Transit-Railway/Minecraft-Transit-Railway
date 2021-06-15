package mtr.gui;

import mtr.config.CustomResources;
import mtr.data.*;
import mtr.packet.IPacket;
import mtr.packet.PacketTrainDataGuiClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SavedRailScreen extends Screen implements IGui, IPacket {

	private boolean isSelectingTrain;

	private final SavedRailBase savedRailBase;
	private final DashboardScreen dashboardScreen;
	private final TextFieldWidget textFieldPlatformNumber;
	private final WidgetShorterSlider sliderDwellTime;
	private final ButtonWidget buttonSelectTrain;
	private final DashboardList availableTrainsList;

	private final Text platformNumberText;
	private final Text dwellTimeOrTrainText;

	private final int textWidth, startX;

	private static final int MAX_PLATFORM_NAME_LENGTH = 10;
	private static final int SLIDER_WIDTH = 160;

	public SavedRailScreen(SavedRailBase savedRailBase, DashboardScreen dashboardScreen) {
		super(new LiteralText(""));
		this.savedRailBase = savedRailBase;
		this.dashboardScreen = dashboardScreen;
		platformNumberText = new TranslatableText(savedRailBase instanceof Platform ? "gui.mtr.platform_number" : "gui.mtr.siding_number");
		dwellTimeOrTrainText = new TranslatableText(savedRailBase instanceof Platform ? "gui.mtr.dwell_time" : "gui.mtr.selected_train");

		textRenderer = MinecraftClient.getInstance().textRenderer;
		textFieldPlatformNumber = new TextFieldWidget(textRenderer, 0, 0, 0, SQUARE_SIZE, new LiteralText(""));

		textWidth = Math.max(textRenderer.getWidth(platformNumberText), textRenderer.getWidth(dwellTimeOrTrainText)) + TEXT_PADDING;
		startX = (width - textWidth - SLIDER_WIDTH) / 2 + SLIDER_WIDTH;

		sliderDwellTime = new WidgetShorterSlider(startX + textWidth, SLIDER_WIDTH, Platform.MAX_DWELL_TIME - 1, value -> {
			if (savedRailBase instanceof Platform) {
				((Platform) savedRailBase).setDwellTime(value + 1, packet -> PacketTrainDataGuiClient.sendUpdate(PACKET_UPDATE_PLATFORM, packet));
			}
		}, value -> String.format("%ss", (value + 1) / 2F));

		buttonSelectTrain = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new LiteralText(""), button -> onSelectingTrain());
		availableTrainsList = new DashboardList(this::addButton, this::addChild, null, null, null, null, this::onAdd, null, null);
	}

	@Override
	protected void init() {
		super.init();
		setIsSelectingTrain(false);

		IDrawing.setPositionAndWidth(textFieldPlatformNumber, startX + textWidth + TEXT_FIELD_PADDING / 2, height / 2 - SQUARE_SIZE - TEXT_FIELD_PADDING / 2, SLIDER_WIDTH - TEXT_FIELD_PADDING);
		textFieldPlatformNumber.setText(savedRailBase.name);
		textFieldPlatformNumber.setMaxLength(MAX_PLATFORM_NAME_LENGTH);
		textFieldPlatformNumber.setChangedListener(text -> {
			textFieldPlatformNumber.setSuggestion(text.isEmpty() ? "1" : "");
			savedRailBase.name = textFieldPlatformNumber.getText();
		});

		addChild(textFieldPlatformNumber);

		if (savedRailBase instanceof Platform) {
			sliderDwellTime.y = height / 2 + TEXT_FIELD_PADDING / 2;
			sliderDwellTime.setHeight(SQUARE_SIZE);
			sliderDwellTime.setValue(((Platform) savedRailBase).getDwellTime() - 1);
			addButton(sliderDwellTime);
		} else if (savedRailBase instanceof Siding) {
			IDrawing.setPositionAndWidth(buttonSelectTrain, startX + textWidth, height / 2 + TEXT_FIELD_PADDING / 2, SLIDER_WIDTH);
			addButton(buttonSelectTrain);
		}

		availableTrainsList.y = SQUARE_SIZE * 2;
		availableTrainsList.height = height - SQUARE_SIZE * 5;
		availableTrainsList.width = PANEL_WIDTH;

		availableTrainsList.init();
	}

	@Override
	public void tick() {
		textFieldPlatformNumber.tick();
		availableTrainsList.tick();

	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		try {
			renderBackground(matrices);
			if (isSelectingTrain) {
				availableTrainsList.render(matrices, textRenderer, mouseX, mouseY, delta);
			} else {
				textFieldPlatformNumber.render(matrices, mouseX, mouseY, delta);
				textRenderer.draw(matrices, platformNumberText, startX, height / 2F - SQUARE_SIZE - TEXT_FIELD_PADDING / 2F + TEXT_PADDING, ARGB_WHITE);
				textRenderer.draw(matrices, dwellTimeOrTrainText, startX, height / 2F + TEXT_FIELD_PADDING / 2F + TEXT_PADDING, ARGB_WHITE);
			}
			super.render(matrices, mouseX, mouseY, delta);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
	public void onClose() {
		super.onClose();
		savedRailBase.setNameColor(packet -> PacketTrainDataGuiClient.sendUpdate(savedRailBase instanceof Platform ? PACKET_UPDATE_PLATFORM : PACKET_UPDATE_SIDING, packet));
		if (client != null) {
			client.openScreen(dashboardScreen);
		}
	}

	@Override
	public boolean isPauseScreen() {
		return false;
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
		sliderDwellTime.visible = !isSelectingTrain;
		buttonSelectTrain.visible = !isSelectingTrain;
		if (savedRailBase instanceof Siding) {
			final CustomResources.TrainMapping trainTypeMapping = ((Siding) savedRailBase).getTrainTypeMapping();
			buttonSelectTrain.setMessage(CustomResources.customTrains.containsKey(trainTypeMapping.customId) ? new LiteralText(CustomResources.customTrains.get(trainTypeMapping.customId).name) : new TranslatableText("train.mtr." + trainTypeMapping.trainType));
		}
		availableTrainsList.x = isSelectingTrain ? width / 2 - PANEL_WIDTH / 2 : width;
	}

	private void onAdd(NameColorDataBase data, int index) {
		if (savedRailBase instanceof Siding) {
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
					((Siding) savedRailBase).setTrainTypeMapping(customId, trainType, packet -> PacketTrainDataGuiClient.sendUpdate(IPacket.PACKET_UPDATE_SIDING, packet));
					if (client != null && client.world != null) {
						((Siding) savedRailBase).generateRoute(client.world, ClientData.rails, ClientData.platforms, ClientData.routes, ClientData.depots);
					}
				}
			}
		}
		setIsSelectingTrain(false);
	}
}
