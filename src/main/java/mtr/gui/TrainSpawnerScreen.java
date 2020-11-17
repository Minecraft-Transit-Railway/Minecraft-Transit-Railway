package mtr.gui;

import mtr.data.*;
import mtr.packet.PacketTrainDataGuiClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TrainSpawnerScreen extends Screen implements IGui {

	private boolean addingRoute, addingTrain;

	private final BlockPos spawnerPos;
	private final int sliderX;
	private final int sliderWidthWithText;
	private final int rightPanelsX;

	private static final int SLIDER_WIDTH = 48;
	private static final int SETTINGS_HEIGHT = SQUARE_SIZE * 4 + TEXT_PADDING;
	private static final int MAX_TRAINS_PER_HOUR = 5;
	private static final int SECONDS_PER_MC_HOUR = 50;

	private final WidgetShorterSlider[] sliders = new WidgetShorterSlider[TrainSpawner.HOURS_IN_DAY];

	private final ButtonWidget buttonAddRoute;
	private final ButtonWidget buttonAddTrains;
	private final ButtonWidget buttonCancel;
	private final WidgetBetterCheckbox buttonRemoveTrains;
	private final WidgetBetterCheckbox buttonShuffleRoutes;
	private final WidgetBetterCheckbox buttonShuffleTrains;

	private final DashboardList addNewList;
	private final DashboardList routeList;
	private final DashboardList trainList;

	public TrainSpawnerScreen(BlockPos spawnerPos) {
		super(new LiteralText(""));
		this.spawnerPos = spawnerPos;

		client = MinecraftClient.getInstance();
		sliderX = client.textRenderer.getWidth(getTimeString(0)) + TEXT_PADDING * 2;
		sliderWidthWithText = SLIDER_WIDTH + TEXT_PADDING + client.textRenderer.getWidth(getSliderString(0));
		rightPanelsX = sliderX + SLIDER_WIDTH + TEXT_PADDING * 2 + client.textRenderer.getWidth(getSliderString(1));

		for (int i = 0; i < TrainSpawner.HOURS_IN_DAY; i++) {
			final int index = i;
			sliders[i] = new WidgetShorterSlider(sliderX, SLIDER_WIDTH, MAX_TRAINS_PER_HOUR * 2, value -> {
				getTrainSpawner().frequencies[index] = value;
				sendUpdate();
			}, TrainSpawnerScreen::getSliderString);
		}

		buttonAddRoute = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new TranslatableText("gui.mtr.add_route"), button -> onAddingRoute());
		buttonAddTrains = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new TranslatableText("gui.mtr.add_train"), button -> onAddingTrain());
		buttonCancel = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new TranslatableText("gui.cancel"), button -> setAdding(false, false));
		buttonRemoveTrains = new WidgetBetterCheckbox(0, 0, 0, SQUARE_SIZE, new TranslatableText("gui.mtr.remove_trains"), this::onRemoveTrainsCheckedChanged);
		buttonShuffleRoutes = new WidgetBetterCheckbox(0, 0, 0, SQUARE_SIZE, new TranslatableText("gui.mtr.shuffle_routes"), this::onShuffleRoutesCheckedChanged);
		buttonShuffleTrains = new WidgetBetterCheckbox(0, 0, 0, SQUARE_SIZE, new TranslatableText("gui.mtr.shuffle_trains"), this::onShuffleTrainsCheckedChanged);

		addNewList = new DashboardList(this::addButton, null, null, this::onAdded, null, null, this::sendUpdate);
		routeList = new DashboardList(this::addButton, null, null, null, this::onDeleteRoute, this::getRouteList, this::sendUpdate);
		trainList = new DashboardList(this::addButton, null, null, null, this::onDeleteTrain, this::getTrainList, this::sendUpdate);
	}

	@Override
	protected void init() {
		IGui.setPositionAndWidth(buttonAddRoute, rightPanelsX, height - SQUARE_SIZE, getRightPanelWidth());
		IGui.setPositionAndWidth(buttonAddTrains, rightPanelsX + getRightPanelWidth(), height - SQUARE_SIZE, getRightPanelWidth());
		IGui.setPositionAndWidth(buttonCancel, (width - PANEL_WIDTH) / 2, height - SQUARE_SIZE * 2, PANEL_WIDTH);
		IGui.setPositionAndWidth(buttonRemoveTrains, rightPanelsX + TEXT_PADDING, SQUARE_SIZE, getRightPanelWidth());
		IGui.setPositionAndWidth(buttonShuffleRoutes, rightPanelsX + TEXT_PADDING, SQUARE_SIZE * 2, getRightPanelWidth());
		IGui.setPositionAndWidth(buttonShuffleTrains, rightPanelsX + TEXT_PADDING, SQUARE_SIZE * 3, getRightPanelWidth());

		addNewList.y = SQUARE_SIZE * 2;
		addNewList.height = height - SQUARE_SIZE * 4;
		addNewList.width = PANEL_WIDTH;

		routeList.y = SETTINGS_HEIGHT + SQUARE_SIZE;
		routeList.height = height - SETTINGS_HEIGHT - SQUARE_SIZE * 2;
		routeList.width = getRightPanelWidth();

		trainList.y = SETTINGS_HEIGHT + SQUARE_SIZE;
		trainList.height = height - SETTINGS_HEIGHT - SQUARE_SIZE * 2;
		trainList.width = getRightPanelWidth();

		addNewList.init();
		routeList.init();
		trainList.init();

		for (WidgetShorterSlider slider : sliders) {
			addButton(slider);
		}

		addButton(buttonAddRoute);
		addButton(buttonAddTrains);
		addButton(buttonCancel);
		addButton(buttonRemoveTrains);
		addButton(buttonShuffleRoutes);
		addButton(buttonShuffleTrains);

		setAdding(false, false);
	}

	@Override
	public void tick() {
		final TrainSpawner trainSpawner = getTrainSpawner();

		routeList.setData(trainSpawner.routeIds.stream().map(routeId -> RailwayData.getRouteById(ClientData.routes, routeId)).collect(Collectors.toList()), false, false, true, false, true);
		trainList.setData(trainSpawner.trainTypes.stream().map(trainType -> new NamedColoredConverter(trainType.getName(), trainType.getColor())).collect(Collectors.toList()), false, false, true, false, true);

		for (int i = 0; i < TrainSpawner.HOURS_IN_DAY; i++) {
			sliders[i].setValue(trainSpawner.frequencies[i]);
		}

		buttonRemoveTrains.setChecked(trainSpawner.removeTrains);
		buttonShuffleRoutes.setChecked(trainSpawner.shuffleRoutes);
		buttonShuffleTrains.setChecked(trainSpawner.shuffleTrains);
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
			routeList.render(matrices, textRenderer);
			trainList.render(matrices, textRenderer);
			super.render(matrices, mouseX, mouseY, delta);

			drawCenteredText(matrices, textRenderer, new TranslatableText("gui.mtr.game_time"), sliderX / 2, TEXT_PADDING, ARGB_LIGHT_GRAY);
			drawCenteredText(matrices, textRenderer, new TranslatableText("gui.mtr.trains_per_hour"), sliderX + sliderWidthWithText / 2, TEXT_PADDING, ARGB_LIGHT_GRAY);
			drawCenteredText(matrices, textRenderer, new TranslatableText("gui.mtr.settings"), rightPanelsX + getRightPanelWidth(), TEXT_PADDING, ARGB_LIGHT_GRAY);
			drawCenteredText(matrices, textRenderer, new TranslatableText("gui.mtr.routes"), rightPanelsX + getRightPanelWidth() / 2, SETTINGS_HEIGHT + TEXT_PADDING, ARGB_LIGHT_GRAY);
			drawCenteredText(matrices, textRenderer, new TranslatableText("gui.mtr.trains"), rightPanelsX + 3 * getRightPanelWidth() / 2, SETTINGS_HEIGHT + TEXT_PADDING, ARGB_LIGHT_GRAY);

			final int lineHeight = Math.min(SQUARE_SIZE, (height - SQUARE_SIZE) / TrainSpawner.HOURS_IN_DAY);
			for (int i = 0; i < TrainSpawner.HOURS_IN_DAY; i++) {
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
		addNewList.setData(Arrays.stream(Train.TrainType.values()).map(trainType -> new NamedColoredConverter(trainType.getName(), trainType.getColor())).collect(Collectors.toList()), false, false, false, true, false);
		setAdding(false, true);
	}

	private void onAdded(NamedColoredBase data, int index) {
		if (addingRoute) {
			getTrainSpawner().routeIds.add(data.id);
			sendUpdate();
		} else if (addingTrain) {
			getTrainSpawner().trainTypes.add(Train.TrainType.values()[index]);
			sendUpdate();
		}
		setAdding(false, false);
	}

	private void onDeleteRoute(NamedColoredBase data, int index) {
		getTrainSpawner().routeIds.remove(index);
		sendUpdate();
	}

	private void onDeleteTrain(NamedColoredBase data, int index) {
		getTrainSpawner().trainTypes.remove(index);
		sendUpdate();
	}

	private List<Long> getRouteList() {
		return getTrainSpawner().routeIds;
	}

	private List<Train.TrainType> getTrainList() {
		return getTrainSpawner().trainTypes;
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
		buttonRemoveTrains.visible = !adding;
		buttonShuffleRoutes.visible = !adding;
		buttonShuffleTrains.visible = !adding;

		addNewList.x = adding ? (width - PANEL_WIDTH) / 2 : width;
		routeList.x = adding ? width : rightPanelsX;
		trainList.x = adding ? width : rightPanelsX + getRightPanelWidth();
	}

	private void onRemoveTrainsCheckedChanged(boolean checked) {
		getTrainSpawner().removeTrains = checked;
		sendUpdate();
	}

	private void onShuffleRoutesCheckedChanged(boolean checked) {
		getTrainSpawner().shuffleRoutes = checked;
		sendUpdate();
	}

	private void onShuffleTrainsCheckedChanged(boolean checked) {
		getTrainSpawner().shuffleTrains = checked;
		sendUpdate();
	}

	private int getRightPanelWidth() {
		return (width - rightPanelsX) / 2;
	}

	private TrainSpawner getTrainSpawner() {
		return RailwayData.getTrainSpawnerByPos(ClientData.trainSpawners, spawnerPos);
	}

	private void sendUpdate() {
		PacketTrainDataGuiClient.sendTrainSpawnerC2S(getTrainSpawner());
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
