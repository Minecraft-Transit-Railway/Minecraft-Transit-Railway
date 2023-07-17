package mtr.screen;

import mtr.client.ClientData;
import mtr.client.IDrawing;
import mtr.data.DataConverter;
import mtr.data.NameColorDataBase;
import mtr.data.Station;
import mtr.mappings.Text;
import mtr.mappings.UtilitiesClient;
import mtr.packet.PacketTrainDataGuiClient;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class EditStationScreen extends EditNameColorScreenBase<Station> {

	String editingExit;
	int editingDestinationIndex;
	int clickDelay;

	private final Component stationZoneText = Text.translatable("gui.mtr.zone");
	private final Component exitParentsText = Text.translatable("gui.mtr.exit_parents");
	private final Component exitDestinationsText = Text.translatable("gui.mtr.exit_destinations");

	private final WidgetBetterTextField textFieldZone;
	private final WidgetBetterTextField textFieldExitParentLetter;
	private final WidgetBetterTextField textFieldExitParentNumber;
	private final WidgetBetterTextField textFieldExitDestination;

	private final Button buttonAddExitParent;
	private final Button buttonDoneExitParent;
	private final Button buttonAddExitDestination;
	private final Button buttonDoneExitDestination;

	private final DashboardList exitParentList;
	private final DashboardList exitDestinationList;

	private static final int EXIT_PANELS_START = SQUARE_SIZE * 3 + TEXT_FIELD_PADDING + TEXT_PADDING;

	public EditStationScreen(Station station, DashboardScreen dashboardScreen) {
		super(station, dashboardScreen, "gui.mtr.station_name", "gui.mtr.station_color");
		textFieldZone = new WidgetBetterTextField(WidgetBetterTextField.TextFieldFilter.INTEGER, "", DashboardScreen.MAX_COLOR_ZONE_LENGTH);
		textFieldExitParentLetter = new WidgetBetterTextField(WidgetBetterTextField.TextFieldFilter.LETTER, "A", 1);
		textFieldExitParentNumber = new WidgetBetterTextField(WidgetBetterTextField.TextFieldFilter.POSITIVE_INTEGER, "1", 2);
		textFieldExitDestination = new WidgetBetterTextField("");

		buttonAddExitParent = UtilitiesClient.newButton(Text.translatable("gui.mtr.add_exit"), button -> checkClickDelay(() -> changeEditingExit("", -1)));
		buttonDoneExitParent = UtilitiesClient.newButton(Text.translatable("gui.done"), button -> checkClickDelay(this::onDoneExitParent));
		buttonAddExitDestination = UtilitiesClient.newButton(Text.translatable("gui.mtr.add_exit_destination"), button -> checkClickDelay(() -> changeEditingExit(editingExit, station.exits.containsKey(editingExit) ? station.exits.get(editingExit).size() : -1)));
		buttonDoneExitDestination = UtilitiesClient.newButton(Text.translatable("gui.done"), button -> checkClickDelay(this::onDoneExitDestination));

		exitParentList = new DashboardList(null, null, this::onEditExitParent, null, null, this::onDeleteExitParent, null, () -> ClientData.EXIT_PARENTS_SEARCH, text -> ClientData.EXIT_PARENTS_SEARCH = text);
		exitDestinationList = new DashboardList(null, null, this::onEditExitDestination, this::onSortExitDestination, null, this::onDeleteExitDestination, this::getExitDestinationList, () -> ClientData.EXIT_DESTINATIONS_SEARCH, text -> ClientData.EXIT_DESTINATIONS_SEARCH = text);
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

		textFieldZone.setValue(String.valueOf(data.zone));

		exitParentList.x = 0;
		exitParentList.y = EXIT_PANELS_START;
		exitParentList.height = height - EXIT_PANELS_START - SQUARE_SIZE;
		exitParentList.width = width / 2;

		exitDestinationList.x = width / 2;
		exitDestinationList.y = EXIT_PANELS_START;
		exitDestinationList.height = height - EXIT_PANELS_START - SQUARE_SIZE;
		exitDestinationList.width = width / 2;

		exitParentList.init(this::addDrawableChild);
		exitDestinationList.init(this::addDrawableChild);

		addDrawableChild(textFieldZone);
		addDrawableChild(textFieldExitParentLetter);
		addDrawableChild(textFieldExitParentNumber);
		addDrawableChild(textFieldExitDestination);
		addDrawableChild(buttonAddExitParent);
		addDrawableChild(buttonDoneExitParent);
		addDrawableChild(buttonAddExitDestination);
		addDrawableChild(buttonDoneExitDestination);

		changeEditingExit(null, -1);
	}

	@Override
	public void tick() {
		super.tick();

		if (clickDelay > 0) {
			clickDelay--;
		}

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
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
		try {
			renderBackground(guiGraphics);
			renderTextFields(guiGraphics);

			guiGraphics.vLine(width / 2, EXIT_PANELS_START - SQUARE_SIZE, height, ARGB_WHITE_TRANSLUCENT);

			exitParentList.render(guiGraphics, font);
			exitDestinationList.render(guiGraphics, font);

			guiGraphics.drawCenteredString(font, stationZoneText, width / 8 * 7, TEXT_PADDING, ARGB_WHITE);
			guiGraphics.drawCenteredString(font, exitParentsText, width / 4, EXIT_PANELS_START - SQUARE_SIZE + TEXT_PADDING, ARGB_WHITE);
			if (parentExists()) {
				guiGraphics.drawCenteredString(font, exitDestinationsText, 3 * width / 4, EXIT_PANELS_START - SQUARE_SIZE + TEXT_PADDING, ARGB_WHITE);
			}

			super.render(guiGraphics, mouseX, mouseY, delta);
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
	protected void saveData() {
		super.saveData();
		try {
			data.zone = Integer.parseInt(textFieldZone.getValue());
		} catch (Exception ignored) {
			data.zone = 0;
		}
		data.setZone(packet -> PacketTrainDataGuiClient.sendUpdate(PACKET_UPDATE_STATION, packet));
	}

	private void changeEditingExit(String editingExit, int editingDestinationIndex) {
		this.editingExit = editingExit;
		this.editingDestinationIndex = parentExists() ? editingDestinationIndex : -1;

		if (editingExit != null) {
			textFieldExitParentLetter.setValue(editingExit.toUpperCase(Locale.ENGLISH).replaceAll("[^A-Z]", ""));
			textFieldExitParentNumber.setValue(editingExit.replaceAll("\\D", ""));
		}
		if (editingDestinationIndex >= 0 && editingDestinationIndex < data.exits.get(editingExit).size()) {
			textFieldExitDestination.setValue(data.exits.get(editingExit).get(editingDestinationIndex));
		} else {
			textFieldExitDestination.setValue("");
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
		final String parentLetter = textFieldExitParentLetter.getValue();
		final String parentNumber = textFieldExitParentNumber.getValue();
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
		final String destination = textFieldExitDestination.getValue();
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

	private void checkClickDelay(Runnable callback) {
		if (clickDelay == 0) {
			callback.run();
			clickDelay = 10;
		}
	}

	private boolean parentExists() {
		return editingExit != null && data.exits.containsKey(editingExit);
	}

	private static String formatExitName(String text) {
		return text.split("\\|")[0];
	}
}
