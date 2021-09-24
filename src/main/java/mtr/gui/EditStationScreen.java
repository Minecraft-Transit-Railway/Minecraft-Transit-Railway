package mtr.gui;

import mtr.data.DataConverter;
import mtr.data.NameColorDataBase;
import mtr.data.Station;
import mtr.packet.PacketTrainDataGuiClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EditStationScreen extends EditNameColorScreenBase<Station> {

	String editingExit;
	int editingDestinationIndex;

	private final Text stationZoneText = new TranslatableText("gui.mtr.zone");
	private final Text exitParentsText = new TranslatableText("gui.mtr.exit_parents");
	private final Text exitDestinationsText = new TranslatableText("gui.mtr.exit_destinations");

	private final TextFieldWidget textFieldZone;
	private final TextFieldWidget textFieldExitParentLetter;
	private final TextFieldWidget textFieldExitParentNumber;
	private final TextFieldWidget textFieldExitDestination;

	private final ButtonWidget buttonAddExitParent;
	private final ButtonWidget buttonDoneExitParent;
	private final ButtonWidget buttonAddExitDestination;
	private final ButtonWidget buttonDoneExitDestination;

	private final DashboardList exitParentList;
	private final DashboardList exitDestinationList;

	private static final int EXIT_PANELS_START = SQUARE_SIZE * 3 + TEXT_FIELD_PADDING + TEXT_PADDING;

	public EditStationScreen(Station station, DashboardScreen dashboardScreen) {
		super(station, dashboardScreen, "gui.mtr.station_name", "gui.mtr.station_color");
		textRenderer = MinecraftClient.getInstance().textRenderer;
		textFieldZone = new TextFieldWidget(textRenderer, 0, 0, 0, SQUARE_SIZE, new LiteralText(""));
		textFieldExitParentLetter = new TextFieldWidget(textRenderer, 0, 0, 0, SQUARE_SIZE, new LiteralText(""));
		textFieldExitParentNumber = new TextFieldWidget(textRenderer, 0, 0, 0, SQUARE_SIZE, new LiteralText(""));
		textFieldExitDestination = new TextFieldWidget(textRenderer, 0, 0, 0, SQUARE_SIZE, new LiteralText(""));

		buttonAddExitParent = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new TranslatableText("gui.mtr.add_exit"), button -> changeEditingExit("", -1));
		buttonDoneExitParent = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new TranslatableText("gui.done"), button -> onDoneExitParent());
		buttonAddExitDestination = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new TranslatableText("gui.mtr.add_exit_destination"), button -> changeEditingExit(editingExit, station.exits.containsKey(editingExit) ? station.exits.get(editingExit).size() : -1));
		buttonDoneExitDestination = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new TranslatableText("gui.done"), button -> onDoneExitDestination());

		exitParentList = new DashboardList(this::addButton, this::addChild, null, null, this::onEditExitParent, null, null, this::onDeleteExitParent, null, () -> ClientData.EXIT_PARENTS_SEARCH, text -> ClientData.EXIT_PARENTS_SEARCH = text);
		exitDestinationList = new DashboardList(this::addButton, this::addChild, null, null, this::onEditExitDestination, this::onSortExitDestination, null, this::onDeleteExitDestination, this::getExitDestinationList, () -> ClientData.EXIT_DESTINATIONS_SEARCH, text -> ClientData.EXIT_DESTINATIONS_SEARCH = text);
	}

	@Override
	protected void init() {
		setPositionsAndInit(0, width / 2, width / 4 * 3);

		IDrawing.setPositionAndWidth(textFieldZone, width / 4 * 3 + TEXT_FIELD_PADDING / 2, SQUARE_SIZE + TEXT_FIELD_PADDING / 2, width / 4 - TEXT_FIELD_PADDING);

		final int yExitText = height - SQUARE_SIZE * 2 - TEXT_FIELD_PADDING / 2;
		IDrawing.setPositionAndWidth(textFieldExitParentLetter, TEXT_FIELD_PADDING / 2, yExitText, width / 4 - TEXT_FIELD_PADDING);
		IDrawing.setPositionAndWidth(textFieldExitParentNumber, TEXT_FIELD_PADDING / 2 + width / 4, yExitText, width / 4 - TEXT_FIELD_PADDING);
		IDrawing.setPositionAndWidth(textFieldExitDestination, width / 2 + TEXT_FIELD_PADDING / 2, yExitText, width / 2 - TEXT_FIELD_PADDING);

		IDrawing.setPositionAndWidth(buttonAddExitParent, 0, height - SQUARE_SIZE, width / 2);
		IDrawing.setPositionAndWidth(buttonDoneExitParent, 0, height - SQUARE_SIZE, width / 2);
		IDrawing.setPositionAndWidth(buttonAddExitDestination, width / 2, height - SQUARE_SIZE, width / 2);
		IDrawing.setPositionAndWidth(buttonDoneExitDestination, width / 2, height - SQUARE_SIZE, width / 2);

		textFieldZone.setMaxLength(DashboardScreen.MAX_COLOR_ZONE_LENGTH);
		textFieldZone.setText(String.valueOf(data.zone));
		textFieldZone.setChangedListener(text -> {
			final String newText = text.replaceAll("[^0-9-]", "");
			if (!newText.equals(text)) {
				textFieldZone.setText(newText);
			}
		});

		textFieldExitParentLetter.setMaxLength(1);
		textFieldExitParentLetter.setChangedListener(text -> {
			final String newText = text.toUpperCase().replaceAll("[^A-Z]", "");
			if (!newText.equals(text)) {
				textFieldExitParentLetter.setText(newText);
			}
			textFieldExitParentLetter.setSuggestion(newText.isEmpty() ? "A" : "");
		});
		textFieldExitParentNumber.setMaxLength(2);
		textFieldExitParentNumber.setChangedListener(text -> {
			final String newText = text.replaceAll("[^0-9]", "");
			if (!newText.equals(text)) {
				textFieldExitParentNumber.setText(newText);
			}
			textFieldExitParentNumber.setSuggestion(newText.isEmpty() ? "1" : "");
		});
		textFieldExitDestination.setMaxLength(DashboardScreen.MAX_NAME_LENGTH);

		exitParentList.x = 0;
		exitParentList.y = EXIT_PANELS_START;
		exitParentList.height = height - EXIT_PANELS_START - SQUARE_SIZE;
		exitParentList.width = width / 2;

		exitDestinationList.x = width / 2;
		exitDestinationList.y = EXIT_PANELS_START;
		exitDestinationList.height = height - EXIT_PANELS_START - SQUARE_SIZE;
		exitDestinationList.width = width / 2;

		exitParentList.init();
		exitDestinationList.init();

		addChild(textFieldZone);
		addChild(textFieldExitParentLetter);
		addChild(textFieldExitParentNumber);
		addChild(textFieldExitDestination);
		addButton(buttonAddExitParent);
		addButton(buttonDoneExitParent);
		addButton(buttonAddExitDestination);
		addButton(buttonDoneExitDestination);

		changeEditingExit(null, -1);
	}

	@Override
	public void tick() {
		super.tick();
		textFieldZone.tick();
		textFieldExitParentLetter.tick();
		textFieldExitParentNumber.tick();
		textFieldExitDestination.tick();

		exitParentList.tick();
		exitDestinationList.tick();

		final List<DataConverter> exitParents = data.exits.keySet().stream().sorted().map(value -> {
			final List<String> destinations = data.exits.get(value);
			final String additional = destinations.size() > 1 ? "(+" + (destinations.size() - 1) + ")" : "";
			return new DataConverter(destinations.size() > 0 ? value + "|" + destinations.get(0) + "|" + additional : value, 0);
		}).collect(Collectors.toList());
		exitParentList.setData(exitParents, false, false, true, false, false, true);

		final List<DataConverter> exitDestinations = parentExists() ? data.exits.get(editingExit).stream().map(value -> new DataConverter(value, 0)).collect(Collectors.toList()) : new ArrayList<>();
		exitDestinationList.setData(exitDestinations, false, false, true, true, false, true);
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		try {
			renderBackground(matrices);
			renderTextFields(matrices, mouseX, mouseY, delta);

			textFieldZone.render(matrices, mouseX, mouseY, delta);
			textFieldExitParentLetter.render(matrices, mouseX, mouseY, delta);
			textFieldExitParentNumber.render(matrices, mouseX, mouseY, delta);
			textFieldExitDestination.render(matrices, mouseX, mouseY, delta);

			drawVerticalLine(matrices, width / 2, EXIT_PANELS_START - SQUARE_SIZE, height, ARGB_WHITE_TRANSLUCENT);

			exitParentList.render(matrices, textRenderer, mouseX, mouseY, delta);
			exitDestinationList.render(matrices, textRenderer, mouseX, mouseY, delta);

			drawCenteredText(matrices, textRenderer, stationZoneText, width / 8 * 7, TEXT_PADDING, ARGB_WHITE);
			drawCenteredText(matrices, textRenderer, exitParentsText, width / 4, EXIT_PANELS_START - SQUARE_SIZE + TEXT_PADDING, ARGB_WHITE);
			if (parentExists()) {
				drawCenteredText(matrices, textRenderer, exitDestinationsText, 3 * width / 4, EXIT_PANELS_START - SQUARE_SIZE + TEXT_PADDING, ARGB_WHITE);
			}

			super.render(matrices, mouseX, mouseY, delta);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void mouseMoved(double mouseX, double mouseY) {
		exitParentList.mouseMoved(mouseX, mouseY);
		exitDestinationList.mouseMoved(mouseX, mouseY);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
		exitParentList.mouseScrolled(mouseX, mouseY, amount);
		exitDestinationList.mouseScrolled(mouseX, mouseY, amount);
		return super.mouseScrolled(mouseX, mouseY, amount);
	}

	@Override
	public void onClose() {
		super.onClose();
		try {
			data.zone = Integer.parseInt(textFieldZone.getText());
		} catch (Exception ignored) {
			data.zone = 0;
		}
		data.setZone(packet -> PacketTrainDataGuiClient.sendUpdate(PACKET_UPDATE_STATION, packet));
	}

	private void changeEditingExit(String editingExit, int editingDestinationIndex) {
		this.editingExit = editingExit;
		this.editingDestinationIndex = parentExists() ? editingDestinationIndex : -1;

		if (editingExit != null) {
			textFieldExitParentLetter.setText(editingExit.toUpperCase().replaceAll("[^A-Z]", ""));
			textFieldExitParentNumber.setText(editingExit.replaceAll("[^0-9]", ""));
		}
		if (editingDestinationIndex >= 0 && editingDestinationIndex < data.exits.get(editingExit).size()) {
			textFieldExitDestination.setText(data.exits.get(editingExit).get(editingDestinationIndex));
		} else {
			textFieldExitDestination.setText("");
		}

		textFieldExitParentLetter.visible = editingExit != null;
		textFieldExitParentNumber.visible = editingExit != null;
		textFieldExitDestination.visible = editingDestinationIndex >= 0;
		buttonAddExitParent.visible = editingExit == null;
		buttonDoneExitParent.visible = editingExit != null;
		buttonAddExitDestination.visible = parentExists() && editingDestinationIndex < 0;
		buttonDoneExitDestination.visible = editingDestinationIndex >= 0;
		exitDestinationList.x = parentExists() ? width / 2 : width;
		exitParentList.height = height - EXIT_PANELS_START - (editingExit == null ? SQUARE_SIZE : SQUARE_SIZE * 2 + TEXT_FIELD_PADDING);
		exitDestinationList.height = height - EXIT_PANELS_START - (editingDestinationIndex >= 0 ? SQUARE_SIZE * 2 + TEXT_FIELD_PADDING : SQUARE_SIZE);
	}

	private void onDoneExitParent() {
		final String parentLetter = textFieldExitParentLetter.getText();
		final String parentNumber = textFieldExitParentNumber.getText();
		if (!parentLetter.isEmpty() && !parentNumber.isEmpty()) {
			try {
				final String exitParent = parentLetter + Integer.parseInt(parentNumber);
				data.setExitParent(editingExit, exitParent, packet -> PacketTrainDataGuiClient.sendUpdate(PACKET_UPDATE_STATION, packet));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		changeEditingExit(null, -1);
	}

	private void onDoneExitDestination() {
		final String destination = textFieldExitDestination.getText();
		if (parentExists() && editingDestinationIndex >= 0 && !destination.isEmpty()) {
			final List<String> destinations = data.exits.get(editingExit);
			if (editingDestinationIndex < destinations.size()) {
				destinations.set(editingDestinationIndex, destination);
			} else {
				destinations.add(destination);
			}
			data.setExitDestinations(editingExit, packet -> PacketTrainDataGuiClient.sendUpdate(PACKET_UPDATE_STATION, packet));
		}
		changeEditingExit(editingExit, -1);
	}

	private void onEditExitParent(NameColorDataBase listData, int index) {
		changeEditingExit(formatExitName(listData.name), -1);
	}

	private void onDeleteExitParent(NameColorDataBase listData, int index) {
		data.deleteExitParent(formatExitName(listData.name), packet -> PacketTrainDataGuiClient.sendUpdate(PACKET_UPDATE_STATION, packet));
		changeEditingExit(null, -1);
	}

	private void onEditExitDestination(NameColorDataBase listData, int index) {
		changeEditingExit(editingExit, index);
	}

	private void onSortExitDestination() {
		data.setExitDestinations(editingExit, packet -> PacketTrainDataGuiClient.sendUpdate(PACKET_UPDATE_STATION, packet));
		changeEditingExit(editingExit, -1);
	}

	private void onDeleteExitDestination(NameColorDataBase listData, int index) {
		if (parentExists()) {
			data.exits.get(editingExit).remove(listData.name);
			data.setExitDestinations(editingExit, packet -> PacketTrainDataGuiClient.sendUpdate(PACKET_UPDATE_STATION, packet));
		}
		changeEditingExit(editingExit, -1);
	}

	private List<String> getExitDestinationList() {
		return parentExists() ? data.exits.get(editingExit) : new ArrayList<>();
	}

	private boolean parentExists() {
		return editingExit != null && data.exits.containsKey(editingExit);
	}

	private static String formatExitName(String text) {
		return text.split("\\|")[0];
	}
}
