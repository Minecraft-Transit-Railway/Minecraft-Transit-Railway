package mtr.gui;

import mtr.data.Depot;
import mtr.data.NameColorDataBase;
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
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;
import java.util.stream.Collectors;

public class EditDepotScreen extends Screen implements IGui, IPacket {

	private boolean addingTrain;

	private final Depot depot;
	private final DashboardScreen dashboardScreen;
	private final int sliderX;
	private final int sliderWidthWithText;
	private final int rightPanelsX;
	private final int trainCount;

	private final Text depotNameText = new TranslatableText("gui.mtr.depot_name");
	private final Text depotColorText = new TranslatableText("gui.mtr.depot_color");

	private final TextFieldWidget textFieldName;
	private final TextFieldWidget textFieldColor;

	private final WidgetShorterSlider[] sliders = new WidgetShorterSlider[Depot.HOURS_IN_DAY];
	private final ButtonWidget buttonEditInstructions;
	private final ButtonWidget buttonDone;

	private final DashboardList addNewList;
	private final DashboardList trainList;

	private static final int PANELS_START = SQUARE_SIZE * 2 + TEXT_FIELD_PADDING;
	private static final int SLIDER_WIDTH = 64;
	private static final int MAX_TRAINS_PER_HOUR = 5;
	private static final int SECONDS_PER_MC_HOUR = 50;

	public EditDepotScreen(Depot depot, DashboardScreen dashboardScreen) {
		super(new LiteralText(""));
		this.depot = depot;
		this.dashboardScreen = dashboardScreen;
		trainCount = ClientData.sidingsInDepot.containsKey(depot.id) ? ClientData.sidingsInDepot.get(depot.id).size() : 0;

		textRenderer = MinecraftClient.getInstance().textRenderer;
		textFieldName = new TextFieldWidget(textRenderer, 0, 0, 0, SQUARE_SIZE, new LiteralText(""));
		textFieldColor = new TextFieldWidget(textRenderer, 0, 0, 0, SQUARE_SIZE, new LiteralText(""));

		client = MinecraftClient.getInstance();
		sliderX = client.textRenderer.getWidth(getTimeString(0)) + TEXT_PADDING * 2;
		sliderWidthWithText = SLIDER_WIDTH + TEXT_PADDING + client.textRenderer.getWidth(getSliderString(0));
		rightPanelsX = sliderX + SLIDER_WIDTH + TEXT_PADDING * 2 + client.textRenderer.getWidth(getSliderString(1));

		for (int i = 0; i < Depot.HOURS_IN_DAY; i++) {
			final int index = i;
			sliders[i] = new WidgetShorterSlider(sliderX, SLIDER_WIDTH, MAX_TRAINS_PER_HOUR * 2, value -> depot.setFrequencies(value, index, packet -> PacketTrainDataGuiClient.sendUpdate(PACKET_UPDATE_DEPOT, packet)), EditDepotScreen::getSliderString);
		}

		buttonEditInstructions = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new TranslatableText("gui.mtr.edit_instructions"), button -> setIsSelecting(true));
		buttonDone = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new TranslatableText("gui.done"), button -> setIsSelecting(false));

		addNewList = new DashboardList(this::addButton, this::addChild, null, null, null, null, this::onAdded, null, null);
		trainList = new DashboardList(this::addButton, this::addChild, null, null, null, this::onSort, null, this::onRemove, () -> depot.routeIds);
	}

	@Override
	protected void init() {
		super.init();
		final int yStart = SQUARE_SIZE + TEXT_FIELD_PADDING / 2;
		IGui.setPositionAndWidth(textFieldName, rightPanelsX + TEXT_FIELD_PADDING / 2, yStart, width / 4 * 3 - TEXT_FIELD_PADDING - rightPanelsX);
		IGui.setPositionAndWidth(textFieldColor, width / 4 * 3 + TEXT_FIELD_PADDING / 2, yStart, width / 4 - TEXT_FIELD_PADDING);

		textFieldName.setMaxLength(DashboardScreen.MAX_NAME_LENGTH);
		textFieldName.setText(depot.name);
		textFieldColor.setMaxLength(DashboardScreen.MAX_COLOR_ZONE_LENGTH);
		textFieldColor.setText(DashboardScreen.colorIntToString(depot.color));
		textFieldColor.setChangedListener(text -> {
			final String newText = text.toUpperCase().replaceAll("[^0-9A-F]", "");
			if (!newText.equals(text)) {
				textFieldColor.setText(newText);
			}
		});

		IGui.setPositionAndWidth(buttonEditInstructions, rightPanelsX, PANELS_START, width - rightPanelsX);
		IGui.setPositionAndWidth(buttonDone, (width - PANEL_WIDTH) / 2, height - SQUARE_SIZE * 2, PANEL_WIDTH);

		addNewList.y = SQUARE_SIZE * 2;
		addNewList.height = height - SQUARE_SIZE * 5;
		addNewList.width = PANEL_WIDTH;

		trainList.y = SQUARE_SIZE * 2;
		trainList.height = height - SQUARE_SIZE * 5;
		trainList.width = PANEL_WIDTH;

		for (WidgetShorterSlider slider : sliders) {
			addButton(slider);
		}
		for (int i = 0; i < Depot.HOURS_IN_DAY; i++) {
			sliders[i].setValue(depot.getFrequency(i));
		}

		addNewList.init();
		trainList.init();
		setIsSelecting(false);

		addChild(textFieldName);
		addChild(textFieldColor);
		addButton(buttonEditInstructions);
		addButton(buttonDone);
	}

	@Override
	public void tick() {
		textFieldName.tick();
		textFieldColor.tick();
		addNewList.tick();
		trainList.tick();

		addNewList.setData(ClientData.routes, false, false, false, false, true, false);
		trainList.setData(depot.routeIds.stream().map(ClientData.routeIdMap::get).filter(Objects::nonNull).collect(Collectors.toList()), false, false, false, true, false, true);
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		try {
			if (addingTrain) {
				renderBackground(matrices);
				addNewList.render(matrices, textRenderer, mouseX, mouseY, delta);
				trainList.render(matrices, textRenderer, mouseX, mouseY, delta);
				super.render(matrices, mouseX, mouseY, delta);
				drawCenteredText(matrices, textRenderer, new TranslatableText("gui.mtr.edit_instructions"), width / 2, SQUARE_SIZE + TEXT_PADDING, ARGB_LIGHT_GRAY);
			} else {
				renderBackground(matrices);
				drawVerticalLine(matrices, rightPanelsX - 1, -1, height, ARGB_WHITE_TRANSLUCENT);
				textFieldName.render(matrices, mouseX, mouseY, delta);
				textFieldColor.render(matrices, mouseX, mouseY, delta);

				super.render(matrices, mouseX, mouseY, delta);

				drawCenteredText(matrices, textRenderer, depotNameText, width / 8 * 3 + rightPanelsX / 2, TEXT_PADDING, ARGB_WHITE);
				drawCenteredText(matrices, textRenderer, depotColorText, width / 8 * 7, TEXT_PADDING, ARGB_WHITE);

				textRenderer.draw(matrices, new TranslatableText("gui.mtr.trains_in_depot", trainCount), rightPanelsX + TEXT_PADDING, PANELS_START + SQUARE_SIZE + TEXT_PADDING, ARGB_WHITE);

				drawCenteredText(matrices, textRenderer, new TranslatableText("gui.mtr.game_time"), sliderX / 2, TEXT_PADDING, ARGB_LIGHT_GRAY);
				drawCenteredText(matrices, textRenderer, new TranslatableText("gui.mtr.trains_per_hour"), sliderX + sliderWidthWithText / 2, TEXT_PADDING, ARGB_LIGHT_GRAY);

				final int lineHeight = Math.min(SQUARE_SIZE, (height - SQUARE_SIZE) / Depot.HOURS_IN_DAY);
				for (int i = 0; i < Depot.HOURS_IN_DAY; i++) {
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
		addNewList.mouseScrolled(mouseX, mouseY, amount);
		trainList.mouseScrolled(mouseX, mouseY, amount);
		return super.mouseScrolled(mouseX, mouseY, amount);
	}

	@Override
	public void onClose() {
		super.onClose();
		if (client != null) {
			client.openScreen(dashboardScreen);
		}

		depot.name = textFieldName.getText();
		depot.color = DashboardScreen.colorStringToInt(textFieldColor.getText());
		depot.setNameColor(packet -> PacketTrainDataGuiClient.sendUpdate(PACKET_UPDATE_DEPOT, packet));

		if (client.world != null) {
			ClientData.sidings.forEach(siding -> siding.generateRoute(client.world, ClientData.rails, ClientData.platforms, ClientData.routes, ClientData.depots));
		}
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	private void setIsSelecting(boolean isSelecting) {
		addingTrain = isSelecting;

		for (final WidgetShorterSlider slider : sliders) {
			slider.visible = !addingTrain;
		}
		addNewList.x = addingTrain ? width / 2 - PANEL_WIDTH - SQUARE_SIZE : width;
		trainList.x = addingTrain ? width / 2 + SQUARE_SIZE : width;
		buttonEditInstructions.visible = !addingTrain;
		buttonDone.visible = addingTrain;
	}

	private void onAdded(NameColorDataBase data, int index) {
		depot.routeIds.add(data.id);
		depot.setRouteIds(packet -> PacketTrainDataGuiClient.sendUpdate(PACKET_UPDATE_DEPOT, packet));
	}

	private void onSort() {
		depot.setRouteIds(packet -> PacketTrainDataGuiClient.sendUpdate(PACKET_UPDATE_DEPOT, packet));
	}

	private void onRemove(NameColorDataBase data, int index) {
		depot.routeIds.remove(index);
		depot.setRouteIds(packet -> PacketTrainDataGuiClient.sendUpdate(PACKET_UPDATE_DEPOT, packet));
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
