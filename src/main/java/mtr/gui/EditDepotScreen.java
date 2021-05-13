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

import java.util.Objects;
import java.util.stream.Collectors;

public class EditDepotScreen extends Screen implements IGui, IPacket {

	private boolean addingTrain;

	private final Depot depot;
	private final DashboardScreen dashboardScreen;

	private final Text depotNameText = new TranslatableText("gui.mtr.depot_name");
	private final Text depotColorText = new TranslatableText("gui.mtr.depot_color");

	private final TextFieldWidget textFieldName;
	private final TextFieldWidget textFieldColor;

	private final ButtonWidget buttonAddTrains;
	private final ButtonWidget buttonDone;

	private final DashboardList addNewList;
	private final DashboardList trainList;

	private static final int PANELS_START = SQUARE_SIZE * 3 + TEXT_FIELD_PADDING + TEXT_PADDING;
	private static final int EDIT_WIDTH = 160;

	public EditDepotScreen(Depot depot, DashboardScreen dashboardScreen) {
		super(new LiteralText(""));
		this.depot = depot;
		this.dashboardScreen = dashboardScreen;

		textRenderer = MinecraftClient.getInstance().textRenderer;
		textFieldName = new TextFieldWidget(textRenderer, 0, 0, 0, SQUARE_SIZE, new LiteralText(""));
		textFieldColor = new TextFieldWidget(textRenderer, 0, 0, 0, SQUARE_SIZE, new LiteralText(""));

		buttonAddTrains = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new TranslatableText("gui.mtr.edit_instructions"), button -> setIsSelecting(true));
		buttonDone = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new TranslatableText("gui.done"), button -> setIsSelecting(false));

		addNewList = new DashboardList(this::addButton, this::addChild, null, null, null, null, this::onAdded, null, null);
		trainList = new DashboardList(this::addButton, this::addChild, null, null, null, this::onSort, null, this::onRemove, () -> depot.routeIds);
	}

	@Override
	protected void init() {
		super.init();
		final int yStart = SQUARE_SIZE + TEXT_FIELD_PADDING / 2;
		IGui.setPositionAndWidth(textFieldName, TEXT_FIELD_PADDING / 2, yStart, width / 4 * 3 - TEXT_FIELD_PADDING);
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

		IGui.setPositionAndWidth(buttonAddTrains, width - EDIT_WIDTH, PANELS_START, EDIT_WIDTH);
		IGui.setPositionAndWidth(buttonDone, (width - PANEL_WIDTH) / 2, height - SQUARE_SIZE * 2, PANEL_WIDTH);

		addNewList.y = SQUARE_SIZE * 2;
		addNewList.height = height - SQUARE_SIZE * 5;
		addNewList.width = PANEL_WIDTH;

		trainList.y = SQUARE_SIZE * 2;
		trainList.height = height - SQUARE_SIZE * 5;
		trainList.width = PANEL_WIDTH;

		addNewList.init();
		trainList.init();
		setIsSelecting(false);

		addChild(textFieldName);
		addChild(textFieldColor);
		addButton(buttonAddTrains);
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
				textFieldName.render(matrices, mouseX, mouseY, delta);
				textFieldColor.render(matrices, mouseX, mouseY, delta);

				drawCenteredText(matrices, textRenderer, depotNameText, width / 8 * 3, TEXT_PADDING, ARGB_WHITE);
				drawCenteredText(matrices, textRenderer, depotColorText, width / 8 * 7, TEXT_PADDING, ARGB_WHITE);

				super.render(matrices, mouseX, mouseY, delta);
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
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	private void setIsSelecting(boolean isSelecting) {
		addingTrain = isSelecting;
		addNewList.x = addingTrain ? width / 2 - PANEL_WIDTH - SQUARE_SIZE : width;
		trainList.x = addingTrain ? width / 2 + SQUARE_SIZE : width;
		buttonAddTrains.visible = !addingTrain;
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
}
