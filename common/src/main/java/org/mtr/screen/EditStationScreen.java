package org.mtr.screen;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.mtr.MTR;
import org.mtr.client.IDrawing;
import org.mtr.client.MinecraftClientData;
import org.mtr.core.data.Station;
import org.mtr.core.data.StationExit;
import org.mtr.core.operation.UpdateDataRequest;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.packet.PacketUpdateData;
import org.mtr.registry.RegistryClient;
import org.mtr.widget.BetterTextFieldWidget;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Locale;
import java.util.stream.Collectors;

public class EditStationScreen extends EditNameColorScreenBase<Station> {

	private StationExit editingExit;
	private int editingDestinationIndex;
	private int clickDelay;

	private final MutableText stationZoneText = TranslationProvider.GUI_MTR_ZONE.getMutableText();
	private final MutableText exitParentsText = TranslationProvider.GUI_MTR_EXIT_PARENTS.getMutableText();
	private final MutableText exitDestinationsText = TranslationProvider.GUI_MTR_EXIT_DESTINATIONS.getMutableText();

	private final BetterTextFieldWidget textFieldZone;
	private final BetterTextFieldWidget textFieldExitParentLetter;
	private final BetterTextFieldWidget textFieldExitParentNumber;
	private final BetterTextFieldWidget textFieldExitDestination;

	private final ButtonWidget buttonAddExitParent;
	private final ButtonWidget buttonDoneExitParent;
	private final ButtonWidget buttonAddExitDestination;
	private final ButtonWidget buttonDoneExitDestination;

	private final DashboardList exitParentList;
	private final DashboardList exitDestinationList;

	private static final int EXIT_PANELS_START = SQUARE_SIZE * 3 + TEXT_FIELD_PADDING + TEXT_PADDING;

	public EditStationScreen(Station station, Screen previousScreen) {
		super(station, TranslationProvider.GUI_MTR_STATION_NAME, TranslationProvider.GUI_MTR_STATION_COLOR, previousScreen);
		textFieldZone = new BetterTextFieldWidget(DashboardScreen.MAX_COLOR_ZONE_LENGTH, TextCase.DEFAULT, "[^-\\d]", null, text -> {
		});
		textFieldExitParentLetter = new BetterTextFieldWidget(2, TextCase.UPPER, "[^A-Z]", "A", text -> {
		});
		textFieldExitParentNumber = new BetterTextFieldWidget(2, TextCase.DEFAULT, "\\D", "1", text -> {
		});
		textFieldExitDestination = new BetterTextFieldWidget(1024, TextCase.DEFAULT, null, null, text -> {
		});

		buttonAddExitParent = ButtonWidget.builder(TranslationProvider.GUI_MTR_ADD_EXIT.getMutableText(), button -> checkClickDelay(() -> changeEditingExit(new StationExit(), -1))).build();
		buttonDoneExitParent = ButtonWidget.builder(Text.translatable("gui.done"), button -> checkClickDelay(this::onDoneExitParent)).build();
		buttonAddExitDestination = ButtonWidget.builder(TranslationProvider.GUI_MTR_ADD_EXIT_DESTINATION.getMutableText(), button -> checkClickDelay(() -> changeEditingExit(editingExit, editingExit == null ? -1 : editingExit.getDestinations().size()))).build();
		buttonDoneExitDestination = ButtonWidget.builder(Text.translatable("gui.done"), button -> checkClickDelay(this::onDoneExitDestination)).build();

		exitParentList = new DashboardList(null, null, this::onEditExitParent, null, null, this::onDeleteExitParent, null, () -> MinecraftClientData.EXIT_PARENTS_SEARCH, text -> MinecraftClientData.EXIT_PARENTS_SEARCH = text);
		exitDestinationList = new DashboardList(null, null, this::onEditExitDestination, this::onSortExitDestination, null, this::onDeleteExitDestination, this::getExitDestinationList, () -> MinecraftClientData.EXIT_DESTINATIONS_SEARCH, text -> MinecraftClientData.EXIT_DESTINATIONS_SEARCH = text);
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

		textFieldZone.setText(String.valueOf(data.getZone1()));

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

		exitParentList.tick();
		exitDestinationList.tick();

		exitParentList.setData(getExitsForDashboardList(getStationExits(data, false)), false, false, true, false, false, true);
		final ObjectArrayList<DashboardListItem> exitDestinations = parentExists() ? editingExit.getDestinations().stream().map(value -> new DashboardListItem(0, value, 0)).collect(Collectors.toCollection(ObjectArrayList::new)) : new ObjectArrayList<>();
		exitDestinationList.setData(exitDestinations, false, false, true, true, false, true);
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		try {
			renderBackground(context, mouseX, mouseY, delta);
			renderTextFields(context);

			context.fill(width / 2, EXIT_PANELS_START - SQUARE_SIZE, width / 2 + 1, height, ARGB_WHITE_TRANSLUCENT);

			exitParentList.render(context);
			exitDestinationList.render(context);

			context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, stationZoneText, width / 8 * 7, TEXT_PADDING, ARGB_WHITE);
			context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, exitParentsText, width / 4, EXIT_PANELS_START - SQUARE_SIZE + TEXT_PADDING, ARGB_WHITE);
			if (parentExists()) {
				context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, exitDestinationsText, 3 * width / 4, EXIT_PANELS_START - SQUARE_SIZE + TEXT_PADDING, ARGB_WHITE);
			}

			super.render(context, mouseX, mouseY, delta);
		} catch (Exception e) {
			MTR.LOGGER.error("", e);
		}
	}

	@Override
	public void mouseMoved(double mouseX, double mouseY) {
		exitParentList.mouseMoved(mouseX, mouseY);
		exitDestinationList.mouseMoved(mouseX, mouseY);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
		exitParentList.mouseScrolled(mouseX, mouseY, verticalAmount);
		exitDestinationList.mouseScrolled(mouseX, mouseY, verticalAmount);
		return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
	}

	@Override
	protected void saveData() {
		super.saveData();
		try {
			data.setZone1(Integer.parseInt(textFieldZone.getText()));
		} catch (Exception ignored) {
			data.setZone1(0);
		}

		final ObjectArrayList<StationExit> exitsToRemove = new ObjectArrayList<>();
		final ObjectOpenHashSet<String> visitedExits = new ObjectOpenHashSet<>();
		data.getExits().forEach(exit -> {
			if (visitedExits.contains(exit.getName())) {
				exitsToRemove.add(exit);
			} else {
				visitedExits.add(exit.getName());
			}
		});
		exitsToRemove.forEach(data.getExits()::remove);

		RegistryClient.sendPacketToServer(new PacketUpdateData(new UpdateDataRequest(MinecraftClientData.getDashboardInstance()).addStation(data)));
	}

	private void changeEditingExit(@Nullable StationExit editingExit, int editingDestinationIndex) {
		this.editingExit = editingExit;
		this.editingDestinationIndex = parentExists() ? editingDestinationIndex : -1;

		if (editingExit != null) {
			textFieldExitParentLetter.setText(editingExit.getName().toUpperCase(Locale.ENGLISH).replaceAll("[^A-Z]", ""));
			textFieldExitParentNumber.setText(editingExit.getName().replaceAll("\\D", ""));
		}
		if (editingExit != null && editingDestinationIndex >= 0 && editingDestinationIndex < editingExit.getDestinations().size()) {
			textFieldExitDestination.setText(editingExit.getDestinations().get(editingDestinationIndex));
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
		if (!parentLetter.isEmpty() && !parentNumber.isEmpty() && parentExists()) {
			try {
				final String exitName = parentLetter + Integer.parseInt(parentNumber);
				editingExit.setName(exitName);
				if (!data.getExits().contains(editingExit)) {
					final StationExit overlappingExit = getStationExit(exitName);
					if (overlappingExit == null) {
						data.getExits().add(editingExit);
					} else {
						overlappingExit.getDestinations().addAll(editingExit.getDestinations());
					}
				}
			} catch (Exception e) {
				MTR.LOGGER.error("", e);
			}
		}
		changeEditingExit(null, -1);
	}

	private void onDoneExitDestination() {
		final String destination = textFieldExitDestination.getText();
		if (parentExists() && editingDestinationIndex >= 0 && !destination.isEmpty()) {
			final ObjectArrayList<String> destinations = editingExit.getDestinations();
			if (editingDestinationIndex < destinations.size()) {
				destinations.set(editingDestinationIndex, destination);
			} else {
				destinations.add(destination);
			}
		}
		changeEditingExit(editingExit, -1);
	}

	private void onEditExitParent(DashboardListItem dashboardListItem, int index) {
		changeEditingExit(getStationExit(formatExitName(dashboardListItem.getName(true))), -1);
	}

	private void onDeleteExitParent(DashboardListItem dashboardListItem, int index) {
		data.getExits().remove(index);
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
			editingExit.getDestinations().remove(dashboardListItem.getName(true));
		}
		changeEditingExit(editingExit, -1);
	}

	private ObjectArrayList<String> getExitDestinationList() {
		return parentExists() ? editingExit.getDestinations() : new ObjectArrayList<>();
	}

	private void checkClickDelay(Runnable callback) {
		if (clickDelay == 0) {
			callback.run();
			clickDelay = 10;
		}
	}

	private boolean parentExists() {
		return editingExit != null;
	}

	@Nullable
	private StationExit getStationExit(String exitName) {
		for (final StationExit exit : data.getExits()) {
			if (exit.getName().equals(exitName)) {
				return exit;
			}
		}
		return null;
	}

	public static ObjectArrayList<StationExit> getStationExits(Station station, boolean addExitParents) {
		final ObjectArrayList<StationExit> newExits = new ObjectArrayList<>();
		final ObjectArrayList<StationExit> exits = station.getExits();
		final Object2ObjectOpenHashMap<String, StationExit> addedParentExits = new Object2ObjectOpenHashMap<>();
		Collections.sort(exits);
		exits.forEach(exit -> {
			if (addExitParents) {
				final String parentExitName = exit.getName().substring(0, 1);
				if (!addedParentExits.containsKey(parentExitName)) {
					final StationExit newExit = new StationExit();
					newExit.setName(parentExitName);
					newExits.add(newExit);
					addedParentExits.put(parentExitName, newExit);
				}
				final StationExit parentExit = addedParentExits.get(parentExitName);
				if (parentExit != null && parentExit != exit) {
					parentExit.getDestinations().addAll(exit.getDestinations());
				}
			}
			newExits.add(exit);
		});
		return newExits;
	}

	public static ObjectArrayList<DashboardListItem> getExitsForDashboardList(ObjectArrayList<StationExit> exits) {
		return exits.stream().map(stationExit -> {
			final ObjectArrayList<String> destinations = stationExit.getDestinations();
			final String additional = destinations.size() > 1 ? String.format("|(+%s)", destinations.size() - 1) : "";
			return new DashboardListItem(serializeExit(stationExit.getName()), String.format("%s%s", stationExit.getName(), destinations.isEmpty() ? "" : String.format("|%s%s", destinations.get(0), additional)), 0);
		}).collect(Collectors.toCollection(ObjectArrayList::new));
	}

	public static String deserializeExit(long code) {
		final StringBuilder exit = new StringBuilder();
		long charCodes = code;
		while (charCodes > 0) {
			exit.insert(0, (char) (charCodes & 0xFF));
			charCodes = charCodes >> 8;
		}
		return exit.toString();
	}

	private static long serializeExit(String exitName) {
		final char[] characters = exitName.toCharArray();
		long code = 0;
		for (final char character : characters) {
			code = code << 8;
			code += character;
		}
		return code;
	}

	private static String formatExitName(String text) {
		return text.split("\\|")[0];
	}
}
