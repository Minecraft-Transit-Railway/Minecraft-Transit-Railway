package mtr.gui;

import mtr.data.*;
import mtr.packet.PacketTrainDataGuiClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PlatformScreen extends Screen implements IGui {

	private boolean addingRoute, addingTrain;

	private final BlockPos platformPos;
	private final int sliderX;
	private final int sliderWidthWithText;
	private final int rightPanelsX;

	private static final int SLIDER_WIDTH = 48;
	private static final int SETTINGS_HEIGHT = SQUARE_SIZE * 4 + TEXT_PADDING + TEXT_FIELD_PADDING;
	private static final int MAX_TRAINS_PER_HOUR = 5;
	private static final int SECONDS_PER_MC_HOUR = 50;
	private static final int MAX_PLATFORM_NAME_LENGTH = 10;

	private final WidgetShorterSlider[] sliders = new WidgetShorterSlider[Platform.HOURS_IN_DAY];

	private final ButtonWidget buttonAddRoute;
	private final ButtonWidget buttonAddTrains;
	private final ButtonWidget buttonCancel;
	private final WidgetBetterCheckbox buttonShuffleRoutes;
	private final WidgetBetterCheckbox buttonShuffleTrains;

	private final TextFieldWidget textFieldName;

	private final DashboardList addNewList;
	private final DashboardList routeList;
	private final DashboardList trainList;

	public PlatformScreen(BlockPos platformPos) {
		super(new LiteralText(""));
		this.platformPos = platformPos;

		client = MinecraftClient.getInstance();
		sliderX = client.textRenderer.getWidth(getTimeString(0)) + TEXT_PADDING * 2;
		sliderWidthWithText = SLIDER_WIDTH + TEXT_PADDING + client.textRenderer.getWidth(getSliderString(0));
		rightPanelsX = sliderX + SLIDER_WIDTH + TEXT_PADDING * 2 + client.textRenderer.getWidth(getSliderString(1));

		for (int i = 0; i < Platform.HOURS_IN_DAY; i++) {
			final int index = i;
			sliders[i] = new WidgetShorterSlider(sliderX, SLIDER_WIDTH, MAX_TRAINS_PER_HOUR * 2, value -> {
				getPlatform().setFrequencies(value, index);
				sendUpdate();
			}, PlatformScreen::getSliderString);
		}

		buttonAddRoute = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new TranslatableText("gui.mtr.add_route"), button -> onAddingRoute());
		buttonAddTrains = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new TranslatableText("gui.mtr.add_train"), button -> onAddingTrain());
		buttonCancel = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new TranslatableText("gui.cancel"), button -> setAdding(false, false));
		buttonShuffleRoutes = new WidgetBetterCheckbox(0, 0, 0, SQUARE_SIZE, new TranslatableText("gui.mtr.shuffle_routes"), this::onShuffleRoutesCheckedChanged);
		buttonShuffleTrains = new WidgetBetterCheckbox(0, 0, 0, SQUARE_SIZE, new TranslatableText("gui.mtr.shuffle_trains"), this::onShuffleTrainsCheckedChanged);

		textFieldName = new TextFieldWidget(client.textRenderer, 0, 0, 0, SQUARE_SIZE, new LiteralText(""));

		addNewList = new DashboardList(this::addButton, null, null, this::onAdded, null, null, this::sendUpdate);
		routeList = new DashboardList(this::addButton, null, null, null, this::onDeleteRoute, this::getRouteList, this::sendUpdate);
		trainList = new DashboardList(this::addButton, null, null, null, this::onDeleteTrain, this::getTrainList, this::sendUpdate);
	}

	@Override
	protected void init() {
		IGui.setPositionAndWidth(buttonAddRoute, rightPanelsX, height - SQUARE_SIZE, getRightPanelWidth());
		IGui.setPositionAndWidth(buttonAddTrains, rightPanelsX + getRightPanelWidth(), height - SQUARE_SIZE, getRightPanelWidth());
		IGui.setPositionAndWidth(buttonCancel, (width - PANEL_WIDTH) / 2, height - SQUARE_SIZE * 2, PANEL_WIDTH);
		IGui.setPositionAndWidth(buttonShuffleRoutes, rightPanelsX + TEXT_PADDING, SQUARE_SIZE * 2 + TEXT_FIELD_PADDING, getRightPanelWidth());
		IGui.setPositionAndWidth(buttonShuffleTrains, rightPanelsX + TEXT_PADDING, SQUARE_SIZE * 3 + TEXT_FIELD_PADDING, getRightPanelWidth());
		IGui.setPositionAndWidth(textFieldName, rightPanelsX + getRightPanelWidth(), SQUARE_SIZE, getRightPanelWidth() - TEXT_PADDING - TEXT_FIELD_PADDING);

		addNewList.y = SQUARE_SIZE * 2;
		addNewList.height = height - SQUARE_SIZE * 4;
		addNewList.width = PANEL_WIDTH;

		routeList.y = SETTINGS_HEIGHT + SQUARE_SIZE;
		routeList.height = height - SETTINGS_HEIGHT - SQUARE_SIZE * 2;
		routeList.width = getRightPanelWidth();

		trainList.y = SETTINGS_HEIGHT + SQUARE_SIZE;
		trainList.height = height - SETTINGS_HEIGHT - SQUARE_SIZE * 2;
		trainList.width = getRightPanelWidth();

		textFieldName.setText(getPlatform().name);
		textFieldName.setMaxLength(MAX_PLATFORM_NAME_LENGTH);
		textFieldName.setChangedListener(text -> {
			textFieldName.setSuggestion(text.isEmpty() ? "1" : "");
			getPlatform().name = IGui.textOrUntitled(textFieldName.getText());
			sendUpdate();
		});

		addNewList.init();
		routeList.init();
		trainList.init();

		for (WidgetShorterSlider slider : sliders) {
			addButton(slider);
		}

		addButton(buttonAddRoute);
		addButton(buttonAddTrains);
		addButton(buttonCancel);
		addButton(buttonShuffleRoutes);
		addButton(buttonShuffleTrains);

		addChild(textFieldName);

		setAdding(false, false);
	}

	@Override
	public void tick() {
		textFieldName.tick();
		final Platform platform = getPlatform();

		routeList.setData(platform.routeIds.stream().map(routeId -> RailwayData.getDataById(ClientData.routes, routeId)).collect(Collectors.toList()), false, false, true, false, true);
		trainList.setData(platform.trainTypes.stream().map(trainType -> new DataConverter(trainType.getName(), trainType.getColor())).collect(Collectors.toList()), false, false, true, false, true);

		for (int i = 0; i < Platform.HOURS_IN_DAY; i++) {
			sliders[i].setValue(platform.getFrequency(i));
		}

		buttonShuffleRoutes.setChecked(platform.shuffleRoutes);
		buttonShuffleTrains.setChecked(platform.shuffleTrains);
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		if (addingRoute || addingTrain) {
			renderBackground(matrices);
			addNewList.render(matrices, textRenderer);
			super.render(matrices, mouseX, mouseY, delta);
			drawCenteredText(matrices, textRenderer, new TranslatableText("gui.mtr." + (addingRoute ? "add_route" : "add_train")), width / 2, SQUARE_SIZE + TEXT_PADDING, ARGB_LIGHT_GRAY);
		} else {
			drawVerticalLine(matrices, rightPanelsX - 1, -1, height, ARGB_WHITE_TRANSLUCENT);
			drawHorizontalLine(matrices, rightPanelsX, width, SETTINGS_HEIGHT, ARGB_WHITE_TRANSLUCENT);
			renderBackground(matrices);
			textFieldName.render(matrices, mouseX, mouseY, delta);
			routeList.render(matrices, textRenderer);
			trainList.render(matrices, textRenderer);
			super.render(matrices, mouseX, mouseY, delta);

			drawTextWithShadow(matrices, textRenderer, new TranslatableText("gui.mtr.platform_number"), rightPanelsX + TEXT_PADDING, SQUARE_SIZE + TEXT_PADDING, ARGB_WHITE);
			drawCenteredText(matrices, textRenderer, new TranslatableText("gui.mtr.game_time"), sliderX / 2, TEXT_PADDING, ARGB_LIGHT_GRAY);
			drawCenteredText(matrices, textRenderer, new TranslatableText("gui.mtr.trains_per_hour"), sliderX + sliderWidthWithText / 2, TEXT_PADDING, ARGB_LIGHT_GRAY);
			drawCenteredText(matrices, textRenderer, new TranslatableText("gui.mtr.settings"), rightPanelsX + getRightPanelWidth(), TEXT_PADDING, ARGB_LIGHT_GRAY);
			drawCenteredText(matrices, textRenderer, new TranslatableText("gui.mtr.routes"), rightPanelsX + getRightPanelWidth() / 2, SETTINGS_HEIGHT + TEXT_PADDING, ARGB_LIGHT_GRAY);
			drawCenteredText(matrices, textRenderer, new TranslatableText("gui.mtr.trains"), rightPanelsX + 3 * getRightPanelWidth() / 2, SETTINGS_HEIGHT + TEXT_PADDING, ARGB_LIGHT_GRAY);

			final int lineHeight = Math.min(SQUARE_SIZE, (height - SQUARE_SIZE) / Platform.HOURS_IN_DAY);
			for (int i = 0; i < Platform.HOURS_IN_DAY; i++) {
				drawStringWithShadow(matrices, textRenderer, getTimeString(i), TEXT_PADDING, SQUARE_SIZE + lineHeight * i + (int) ((lineHeight - TEXT_HEIGHT) / 2F), ARGB_WHITE);
				sliders[i].y = SQUARE_SIZE + lineHeight * i;
				sliders[i].setHeight(lineHeight);
			}
		}
	}

	@Override
	public void mouseMoved(double mouseX, double mouseY) {
		addNewList.mouseMoved(mouseX, mouseY);
		routeList.mouseMoved(mouseX, mouseY);
		trainList.mouseMoved(mouseX, mouseY);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
		addNewList.mouseScrolled(amount);
		routeList.mouseScrolled(amount);
		trainList.mouseScrolled(amount);
		return super.mouseScrolled(mouseX, mouseY, amount);
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	private void onAddingRoute() {
		addNewList.setData(ClientData.routes, false, false, false, true, false);
		setAdding(true, false);
	}

	private void onAddingTrain() {
		addNewList.setData(Arrays.stream(Train.TrainType.values()).map(trainType -> new DataConverter(trainType.getName(), trainType.getColor())).collect(Collectors.toList()), false, false, false, true, false);
		setAdding(false, true);
	}

	private void onAdded(DataBase data, int index) {
		if (addingRoute) {
			getPlatform().routeIds.add(data.id);
			sendUpdate();
		} else if (addingTrain) {
			getPlatform().trainTypes.add(Train.TrainType.values()[index]);
			sendUpdate();
		}
		setAdding(false, false);
	}

	private void onDeleteRoute(DataBase data, int index) {
		getPlatform().routeIds.remove(index);
		sendUpdate();
	}

	private void onDeleteTrain(DataBase data, int index) {
		getPlatform().trainTypes.remove(index);
		sendUpdate();
	}

	private List<Long> getRouteList() {
		return getPlatform().routeIds;
	}

	private List<Train.TrainType> getTrainList() {
		return getPlatform().trainTypes;
	}

	private void setAdding(boolean addingRoute, boolean addingTrain) {
		this.addingRoute = addingRoute;
		this.addingTrain = addingTrain;

		final boolean adding = addingRoute || addingTrain;
		for (WidgetShorterSlider slider : sliders) {
			slider.visible = !adding;
		}
		buttonAddRoute.visible = !adding;
		buttonAddTrains.visible = !adding;
		buttonCancel.visible = adding;
		buttonShuffleRoutes.visible = !adding;
		buttonShuffleTrains.visible = !adding;
		textFieldName.visible = !adding;

		addNewList.x = adding ? (width - PANEL_WIDTH) / 2 : width;
		routeList.x = adding ? width : rightPanelsX;
		trainList.x = adding ? width : rightPanelsX + getRightPanelWidth();
	}

	private void onShuffleRoutesCheckedChanged(boolean checked) {
		getPlatform().shuffleRoutes = checked;
		sendUpdate();
	}

	private void onShuffleTrainsCheckedChanged(boolean checked) {
		getPlatform().shuffleTrains = checked;
		sendUpdate();
	}

	private int getRightPanelWidth() {
		return (width - rightPanelsX) / 2;
	}

	private Platform getPlatform() {
		return RailwayData.getPlatformByPos(ClientData.platforms, platformPos);
	}

	private void sendUpdate() {
		PacketTrainDataGuiClient.sendPlatformC2S(getPlatform());
	}

	private static String getSliderString(int value) {
		final String headwayText;
		if (value == 0) {
			headwayText = "";
		} else {
			headwayText = " (" + (Math.round(20F * SECONDS_PER_MC_HOUR / value) / 10F) + new TranslatableText("gui.mtr.s").getString() + ")";
		}
		return value / 2F + new TranslatableText("gui.mtr.tph").getString() + headwayText;
	}

	private static String getTimeString(int hour) {
		final String hourString = StringUtils.leftPad(String.valueOf(hour), 2, "0");
		return String.format("%s:00-%s:59", hourString, hourString);
	}
}
