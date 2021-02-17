package mtr.gui;

import mtr.data.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PlatformScreen extends Screen implements IGui {

	private boolean addingTrain;

	private final Route route;
	private final int sliderX;
	private final int sliderWidthWithText;
	private final int rightPanelsX;

	private static final int SLIDER_WIDTH = 64;
	private static final int SETTINGS_HEIGHT = SQUARE_SIZE * 3 + TEXT_PADDING + TEXT_FIELD_PADDING;
	private static final int MAX_TRAINS_PER_HOUR = 5;
	private static final int SECONDS_PER_MC_HOUR = 50;
	private static final int CUSTOM_DESTINATION_X_OFFSET = 108;
	private static final int MAX_CUSTOM_DESTINATION_LENGTH = 128;

	private final WidgetShorterSlider[] sliders = new WidgetShorterSlider[Platform.HOURS_IN_DAY];
	private final ButtonWidget buttonAddTrains;
	private final ButtonWidget buttonCancel;
	private final WidgetBetterCheckbox buttonShuffleTrains;
	private final TextFieldWidget textFieldCustomDestination;

	private final DashboardList addNewList;
	private final DashboardList trainList;

	public PlatformScreen(Route route) {
		super(new LiteralText(""));
		this.route = route;

		client = MinecraftClient.getInstance();
		sliderX = client.textRenderer.getWidth(getTimeString(0)) + TEXT_PADDING * 2;
		sliderWidthWithText = SLIDER_WIDTH + TEXT_PADDING + client.textRenderer.getWidth(getSliderString(0));
		rightPanelsX = sliderX + SLIDER_WIDTH + TEXT_PADDING * 2 + client.textRenderer.getWidth(getSliderString(1));

		for (int i = 0; i < Platform.HOURS_IN_DAY; i++) {
			final int index = i;
			sliders[i] = new WidgetShorterSlider(sliderX, SLIDER_WIDTH, MAX_TRAINS_PER_HOUR * 2, value -> this.route.setFrequencies(value, index), PlatformScreen::getSliderString);
		}

		buttonAddTrains = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new TranslatableText("gui.mtr.add_train"), button -> onAddingTrain());
		buttonCancel = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new TranslatableText("gui.cancel"), button -> setAdding(false));
		buttonShuffleTrains = new WidgetBetterCheckbox(0, 0, 0, SQUARE_SIZE, new TranslatableText("gui.mtr.shuffle_trains"), checked -> this.route.shuffleTrains = checked);

		textFieldCustomDestination = new TextFieldWidget(client.textRenderer, 0, 0, 0, SQUARE_SIZE, new LiteralText(""));

		addNewList = new DashboardList(this::addButton, null, null, null, this::onAdded, null, null);
		trainList = new DashboardList(this::addButton, null, null, null, null, (data, index) -> this.route.trainTypes.remove(index), this::getTrainList);
	}

	@Override
	protected void init() {
		IGui.setPositionAndWidth(buttonAddTrains, rightPanelsX, height - SQUARE_SIZE, width - rightPanelsX);
		IGui.setPositionAndWidth(buttonCancel, (width - PANEL_WIDTH) / 2, height - SQUARE_SIZE * 2, PANEL_WIDTH);
		IGui.setPositionAndWidth(buttonShuffleTrains, rightPanelsX + TEXT_PADDING, SQUARE_SIZE * 2 + TEXT_FIELD_PADDING, width - rightPanelsX);
		IGui.setPositionAndWidth(textFieldCustomDestination, rightPanelsX + CUSTOM_DESTINATION_X_OFFSET, SQUARE_SIZE, width - rightPanelsX - CUSTOM_DESTINATION_X_OFFSET - TEXT_FIELD_PADDING - TEXT_PADDING);

		addNewList.y = SQUARE_SIZE * 2;
		addNewList.height = height - SQUARE_SIZE * 4;
		addNewList.width = PANEL_WIDTH;

		trainList.y = SETTINGS_HEIGHT + SQUARE_SIZE;
		trainList.height = height - SETTINGS_HEIGHT - SQUARE_SIZE * 2;
		trainList.width = width - rightPanelsX;

		textFieldCustomDestination.setText(route.customDestination);
		textFieldCustomDestination.setMaxLength(MAX_CUSTOM_DESTINATION_LENGTH);
		textFieldCustomDestination.setChangedListener(text -> route.customDestination = textFieldCustomDestination.getText());

		addNewList.init();
		trainList.init();

		for (WidgetShorterSlider slider : sliders) {
			addButton(slider);
		}

		addButton(buttonAddTrains);
		addButton(buttonCancel);
		addButton(buttonShuffleTrains);

		addChild(textFieldCustomDestination);

		setAdding(false);

		for (int i = 0; i < Platform.HOURS_IN_DAY; i++) {
			sliders[i].setValue(route.getFrequency(i));
		}

		buttonShuffleTrains.setChecked(route.shuffleTrains);
	}

	@Override
	public void tick() {
		textFieldCustomDestination.tick();
		trainList.setData(route.trainTypes.stream().map(trainType -> new DataConverter(trainType.getName(), trainType.getColor())).collect(Collectors.toList()), false, false, false, true, false, true);
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		try {
			if (addingTrain) {
				renderBackground(matrices);
				addNewList.render(matrices, textRenderer);
				super.render(matrices, mouseX, mouseY, delta);
				drawCenteredText(matrices, textRenderer, new TranslatableText("gui.mtr.add_train"), width / 2, SQUARE_SIZE + TEXT_PADDING, ARGB_LIGHT_GRAY);
			} else {
				drawVerticalLine(matrices, rightPanelsX - 1, -1, height, ARGB_WHITE_TRANSLUCENT);
				drawHorizontalLine(matrices, rightPanelsX, width, SETTINGS_HEIGHT, ARGB_WHITE_TRANSLUCENT);
				renderBackground(matrices);
				textFieldCustomDestination.render(matrices, mouseX, mouseY, delta);
				trainList.render(matrices, textRenderer);
				super.render(matrices, mouseX, mouseY, delta);

				drawTextWithShadow(matrices, textRenderer, new TranslatableText("gui.mtr.custom_destination"), rightPanelsX + TEXT_PADDING, SQUARE_SIZE + TEXT_PADDING, ARGB_WHITE);
				drawCenteredText(matrices, textRenderer, new TranslatableText("gui.mtr.game_time"), sliderX / 2, TEXT_PADDING, ARGB_LIGHT_GRAY);
				drawCenteredText(matrices, textRenderer, new TranslatableText("gui.mtr.trains_per_hour"), sliderX + sliderWidthWithText / 2, TEXT_PADDING, ARGB_LIGHT_GRAY);
				drawCenteredText(matrices, textRenderer, new TranslatableText("gui.mtr.settings"), (rightPanelsX + width) / 2, TEXT_PADDING, ARGB_LIGHT_GRAY);
				drawCenteredText(matrices, textRenderer, new TranslatableText("gui.mtr.trains"), (rightPanelsX + width) / 2, SETTINGS_HEIGHT + TEXT_PADDING, ARGB_LIGHT_GRAY);

				final int lineHeight = Math.min(SQUARE_SIZE, (height - SQUARE_SIZE) / Platform.HOURS_IN_DAY);
				for (int i = 0; i < Platform.HOURS_IN_DAY; i++) {
					drawStringWithShadow(matrices, textRenderer, getTimeString(i), TEXT_PADDING, SQUARE_SIZE + lineHeight * i + (int) ((lineHeight - TEXT_HEIGHT) / 2F), ARGB_WHITE);
					sliders[i].y = SQUARE_SIZE + lineHeight * i;
					sliders[i].setHeight(lineHeight);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void mouseMoved(double mouseX, double mouseY) {
		addNewList.mouseMoved(mouseX, mouseY);
		trainList.mouseMoved(mouseX, mouseY);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
		addNewList.mouseScrolled(amount);
		trainList.mouseScrolled(amount);
		return super.mouseScrolled(mouseX, mouseY, amount);
	}

	@Override
	public void onClose() {
		super.onClose();
		if (client != null) {
			client.openScreen(new DashboardScreen(1));
		}
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	private void onAddingTrain() {
		addNewList.setData(Arrays.stream(Train.TrainType.values()).map(trainType -> new DataConverter(trainType.getName(), trainType.getColor())).collect(Collectors.toList()), false, false, false, false, true, false);
		setAdding(true);
	}

	private void onAdded(DataBase data, int index) {
		if (addingTrain) {
			route.trainTypes.add(Train.TrainType.values()[index]);
		}
		setAdding(false);
	}

	private List<Train.TrainType> getTrainList() {
		return route.trainTypes;
	}

	private void setAdding(boolean addingTrain) {
		this.addingTrain = addingTrain;

		for (WidgetShorterSlider slider : sliders) {
			slider.visible = !addingTrain;
		}
		buttonAddTrains.visible = !addingTrain;
		buttonCancel.visible = addingTrain;
		buttonShuffleTrains.visible = !addingTrain;
		textFieldCustomDestination.visible = !addingTrain;

		addNewList.x = addingTrain ? (width - PANEL_WIDTH) / 2 : width;
		trainList.x = addingTrain ? width : rightPanelsX;
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
