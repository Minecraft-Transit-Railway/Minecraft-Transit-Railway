package org.mtr.mod.screen;

import org.mtr.core.data.Station;
import org.mtr.core.servlet.IntegrationServlet;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectAVLTreeMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectSet;
import org.mtr.mapping.holder.ClickableWidget;
import org.mtr.mapping.holder.MutableText;
import org.mtr.mapping.mapper.*;
import org.mtr.mapping.tool.TextCase;
import org.mtr.mod.Init;
import org.mtr.mod.InitClient;
import org.mtr.mod.client.ClientData;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.packet.PacketData;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.stream.Collectors;

public class EditStationScreen extends EditNameColorScreenBase<Station> {

	String editingExit;
	int editingDestinationIndex;
	int clickDelay;

	public final Object2ObjectAVLTreeMap<String, ObjectArrayList<String>> exits = new Object2ObjectAVLTreeMap<>(); // TODO

	private final MutableText stationZoneText = TextHelper.translatable("gui.mtr.zone");
	private final MutableText exitParentsText = TextHelper.translatable("gui.mtr.exit_parents");
	private final MutableText exitDestinationsText = TextHelper.translatable("gui.mtr.exit_destinations");

	private final TextFieldWidgetExtension textFieldZone;
	private final TextFieldWidgetExtension textFieldExitParentLetter;
	private final TextFieldWidgetExtension textFieldExitParentNumber;
	private final TextFieldWidgetExtension textFieldExitDestination;

	private final ButtonWidgetExtension buttonAddExitParent;
	private final ButtonWidgetExtension buttonDoneExitParent;
	private final ButtonWidgetExtension buttonAddExitDestination;
	private final ButtonWidgetExtension buttonDoneExitDestination;

	private final DashboardList exitParentList;
	private final DashboardList exitDestinationList;

	private static final int EXIT_PANELS_START = SQUARE_SIZE * 3 + TEXT_FIELD_PADDING + TEXT_PADDING;

	public EditStationScreen(Station station, DashboardScreen dashboardScreen) {
		super(station, dashboardScreen, "gui.mtr.station_name", "gui.mtr.station_color");
		textFieldZone = new TextFieldWidgetExtension(0, 0, 0, SQUARE_SIZE, DashboardScreen.MAX_COLOR_ZONE_LENGTH, TextCase.DEFAULT, "[^-\\d]", null);
		textFieldExitParentLetter = new TextFieldWidgetExtension(0, 0, 0, SQUARE_SIZE, 2, TextCase.UPPER, "[^A-Z]", "A");
		textFieldExitParentNumber = new TextFieldWidgetExtension(0, 0, 0, SQUARE_SIZE, 2, TextCase.DEFAULT, "\\D", "1");
		textFieldExitDestination = new TextFieldWidgetExtension(0, 0, 0, SQUARE_SIZE, 1024, TextCase.DEFAULT, null, null);

		buttonAddExitParent = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, TextHelper.translatable("gui.mtr.add_exit"), button -> checkClickDelay(() -> changeEditingExit("", -1)));
		buttonDoneExitParent = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, TextHelper.translatable("gui.done"), button -> checkClickDelay(this::onDoneExitParent));
		buttonAddExitDestination = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, TextHelper.translatable("gui.mtr.add_exit_destination"), button -> checkClickDelay(() -> changeEditingExit(editingExit, exits.containsKey(editingExit) ? exits.get(editingExit).size() : -1)));
		buttonDoneExitDestination = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, TextHelper.translatable("gui.done"), button -> checkClickDelay(this::onDoneExitDestination));

		exitParentList = new DashboardList(null, null, this::onEditExitParent, null, null, this::onDeleteExitParent, null, () -> ClientData.EXIT_PARENTS_SEARCH, text -> ClientData.EXIT_PARENTS_SEARCH = text);
		exitDestinationList = new DashboardList(null, null, this::onEditExitDestination, this::onSortExitDestination, null, this::onDeleteExitDestination, this::getExitDestinationList, () -> ClientData.EXIT_DESTINATIONS_SEARCH, text -> ClientData.EXIT_DESTINATIONS_SEARCH = text);
	}

	@Override
	protected void init2() {
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

		textFieldZone.setText2(String.valueOf(data.getZone1()));

		exitParentList.x = 0;
		exitParentList.y = EXIT_PANELS_START;
		exitParentList.height = height - EXIT_PANELS_START - SQUARE_SIZE;
		exitParentList.width = width / 2;

		exitDestinationList.x = width / 2;
		exitDestinationList.y = EXIT_PANELS_START;
		exitDestinationList.height = height - EXIT_PANELS_START - SQUARE_SIZE;
		exitDestinationList.width = width / 2;

		exitParentList.init(this::addChild);
		exitDestinationList.init(this::addChild);

		addChild(new ClickableWidget(textFieldZone));
		addChild(new ClickableWidget(textFieldExitParentLetter));
		addChild(new ClickableWidget(textFieldExitParentNumber));
		addChild(new ClickableWidget(textFieldExitDestination));
		addChild(new ClickableWidget(buttonAddExitParent));
		addChild(new ClickableWidget(buttonDoneExitParent));
		addChild(new ClickableWidget(buttonAddExitDestination));
		addChild(new ClickableWidget(buttonDoneExitDestination));

		changeEditingExit(null, -1);
	}

	@Override
	public void tick2() {
		super.tick2();

		if (clickDelay > 0) {
			clickDelay--;
		}

		textFieldZone.tick3();
		textFieldExitParentLetter.tick3();
		textFieldExitParentNumber.tick3();
		textFieldExitDestination.tick3();

		exitParentList.tick();
		exitDestinationList.tick();

		final ObjectArrayList<DashboardListItem> exitParents = exits.keySet().stream().sorted().map(value -> {
			final ObjectArrayList<String> destinations = exits.get(value);
			final String additional = destinations.size() > 1 ? "(+" + (destinations.size() - 1) + ")" : "";
			return new DashboardListItem(0, !destinations.isEmpty() ? value + "|" + destinations.get(0) + "|" + additional : value, 0);
		}).collect(Collectors.toCollection(ObjectArrayList::new));
		exitParentList.setData(exitParents, false, false, true, false, false, true);

		final ObjectArrayList<DashboardListItem> exitDestinations = parentExists() ? exits.get(editingExit).stream().map(value -> new DashboardListItem(0, value, 0)).collect(Collectors.toCollection(ObjectArrayList::new)) : new ObjectArrayList<>();
		exitDestinationList.setData(exitDestinations, false, false, true, true, false, true);
	}

	@Override
	public void render(GraphicsHolder graphicsHolder, int mouseX, int mouseY, float delta) {
		try {
			renderBackground(graphicsHolder);
			renderTextFields(graphicsHolder);

			final GuiDrawing guiDrawing = new GuiDrawing(graphicsHolder);
			guiDrawing.beginDrawingRectangle();
			guiDrawing.drawRectangle(width / 2F, EXIT_PANELS_START - SQUARE_SIZE, width / 2F + 1, height, ARGB_WHITE_TRANSLUCENT);
			guiDrawing.finishDrawingRectangle();

			exitParentList.render(graphicsHolder);
			exitDestinationList.render(graphicsHolder);

			graphicsHolder.drawCenteredText(stationZoneText, width / 8 * 7, TEXT_PADDING, ARGB_WHITE);
			graphicsHolder.drawCenteredText(exitParentsText, width / 4, EXIT_PANELS_START - SQUARE_SIZE + TEXT_PADDING, ARGB_WHITE);
			if (parentExists()) {
				graphicsHolder.drawCenteredText(exitDestinationsText, 3 * width / 4, EXIT_PANELS_START - SQUARE_SIZE + TEXT_PADDING, ARGB_WHITE);
			}

			super.render(graphicsHolder, mouseX, mouseY, delta);
		} catch (Exception e) {
			Init.logException(e);
		}
	}

	@Override
	public void mouseMoved2(double mouseX, double mouseY) {
		exitParentList.mouseMoved(mouseX, mouseY);
		exitDestinationList.mouseMoved(mouseX, mouseY);
	}

	@Override
	public boolean mouseScrolled3(double mouseX, double mouseY, double amount) {
		exitParentList.mouseScrolled(mouseX, mouseY, amount);
		exitDestinationList.mouseScrolled(mouseX, mouseY, amount);
		return super.mouseScrolled3(mouseX, mouseY, amount);
	}

	@Override
	protected void saveData() {
		super.saveData();
		try {
			data.setZone1(Integer.parseInt(textFieldZone.getText2()));
		} catch (Exception ignored) {
			data.setZone1(0);
		}
		InitClient.REGISTRY_CLIENT.sendPacketToServer(PacketData.fromStations(IntegrationServlet.Operation.UPDATE, ObjectSet.of(data)));
	}

	private void changeEditingExit(@Nullable String editingExit, int editingDestinationIndex) {
		this.editingExit = editingExit;
		this.editingDestinationIndex = parentExists() ? editingDestinationIndex : -1;

		if (editingExit != null) {
			textFieldExitParentLetter.setText2(editingExit.toUpperCase(Locale.ENGLISH).replaceAll("[^A-Z]", ""));
			textFieldExitParentNumber.setText2(editingExit.replaceAll("\\D", ""));
		}
		if (editingDestinationIndex >= 0 && editingDestinationIndex < exits.get(editingExit).size()) {
			textFieldExitDestination.setText2(exits.get(editingExit).get(editingDestinationIndex));
		} else {
			textFieldExitDestination.setText2("");
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
		final String parentLetter = textFieldExitParentLetter.getText2();
		final String parentNumber = textFieldExitParentNumber.getText2();
		if (!parentLetter.isEmpty() && !parentNumber.isEmpty()) {
			try {
				final String exitParent = parentLetter + Integer.parseInt(parentNumber);
			} catch (Exception e) {
				Init.logException(e);
			}
		}
		changeEditingExit(null, -1);
	}

	private void onDoneExitDestination() {
		final String destination = textFieldExitDestination.getText2();
		if (parentExists() && editingDestinationIndex >= 0 && !destination.isEmpty()) {
			final ObjectArrayList<String> destinations = exits.get(editingExit);
			if (editingDestinationIndex < destinations.size()) {
				destinations.set(editingDestinationIndex, destination);
			} else {
				destinations.add(destination);
			}
		}
		changeEditingExit(editingExit, -1);
	}

	private void onEditExitParent(DashboardListItem dashboardListItem, int index) {
		changeEditingExit(formatExitName(dashboardListItem.getName(true)), -1);
	}

	private void onDeleteExitParent(DashboardListItem dashboardListItem, int index) {
		changeEditingExit(null, -1);
	}

	private void onEditExitDestination(DashboardListItem dashboardListItem, int index) {
		changeEditingExit(editingExit, index);
	}

	private void onSortExitDestination() {
		changeEditingExit(editingExit, -1);
	}

	private void onDeleteExitDestination(DashboardListItem dashboardListItem, int index) {
		if (parentExists()) {
			exits.get(editingExit).remove(dashboardListItem.getName(true));
		}
		changeEditingExit(editingExit, -1);
	}

	private ObjectArrayList<String> getExitDestinationList() {
		return parentExists() ? exits.get(editingExit) : new ObjectArrayList<>();
	}

	private void checkClickDelay(Runnable callback) {
		if (clickDelay == 0) {
			callback.run();
			clickDelay = 10;
		}
	}

	private boolean parentExists() {
		return editingExit != null && exits.containsKey(editingExit);
	}

	private static String formatExitName(String text) {
		return text.split("\\|")[0];
	}
}
