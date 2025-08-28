package org.mtr.screen;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectIntImmutablePair;
import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.math.ColorHelper;
import org.jetbrains.annotations.Nullable;
import org.mtr.client.MinecraftClientData;
import org.mtr.core.data.Station;
import org.mtr.core.data.StationExit;
import org.mtr.core.operation.UpdateDataRequest;
import org.mtr.core.tool.Utilities;
import org.mtr.data.FormattedStationExit;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.packet.PacketUpdateData;
import org.mtr.registry.RegistryClient;
import org.mtr.tool.GuiHelper;
import org.mtr.widget.*;

import java.util.stream.Collectors;

public final class StationScreen extends ScrollableScreenBase {

	@Nullable
	private ObjectIntImmutablePair<FormattedStationExit> editingExitWithIndex;
	private int tempColor;
	private final ObjectArrayList<FormattedStationExit> tempStationExits = new ObjectArrayList<>();

	private final Station station;

	private final BetterTextFieldWidget nameTextField;
	private final BetterButtonWidget openColorSelectorButton;

	private final BetterTextFieldWidget textFieldZoneX;
	private final BetterTextFieldWidget textFieldZoneY;
	private final BetterTextFieldWidget textFieldZoneZ;

	private final BetterButtonWidget addExitButton;
	private final BetterTextFieldWidget textFieldExitName;
	private final BetterTextFieldWidget textFieldExitDestination;
	private final BetterButtonWidget doneEditingExitButton;

	private final ScrollableListWidget<ObjectIntImmutablePair<FormattedStationExit>> exitsListWidget = new ScrollableListWidget<>();
	private final ColorSelectorWidget colorSelector;

	public StationScreen(Station station, Screen previousScreen) {
		super(previousScreen);
		this.station = station;

		openColorSelectorButton = new BetterButtonWidget(GuiHelper.COLOR_TEXTURE_ID, null, 0, this::onOpenColorSelector);
		nameTextField = new BetterTextFieldWidget(1024, TextCase.DEFAULT, null, TranslationProvider.GUI_MTR_STATION_NAME.getString(), FULL_WIDGET_WIDTH - GuiHelper.DEFAULT_PADDING - openColorSelectorButton.getWidth(), null);

		textFieldZoneX = new BetterTextFieldWidget(DashboardScreen.MAX_COLOR_ZONE_LENGTH, TextCase.DEFAULT, "[^-\\d]", TranslationProvider.GUI_MTR_ZONE_X.getString(), ONE_THIRD_WIDGET_WIDTH, null);
		textFieldZoneY = new BetterTextFieldWidget(DashboardScreen.MAX_COLOR_ZONE_LENGTH, TextCase.DEFAULT, "[^-\\d]", TranslationProvider.GUI_MTR_ZONE_Y.getString(), ONE_THIRD_WIDGET_WIDTH, null);
		textFieldZoneZ = new BetterTextFieldWidget(DashboardScreen.MAX_COLOR_ZONE_LENGTH, TextCase.DEFAULT, "[^-\\d]", TranslationProvider.GUI_MTR_ZONE_Z.getString(), ONE_THIRD_WIDGET_WIDTH, null);

		addExitButton = new BetterButtonWidget(GuiHelper.ADD_TEXTURE_ID, TranslationProvider.GUI_MTR_ADD_EXIT.getString(), FULL_WIDGET_WIDTH, () -> startEditingExitCallback(null));
		textFieldExitName = new BetterTextFieldWidget(4, TextCase.DEFAULT, null, TranslationProvider.GUI_MTR_EXIT_NAME.getString(), HALF_WIDGET_WIDTH / 2, null);
		doneEditingExitButton = new BetterButtonWidget(GuiHelper.CHECK_TEXTURE_ID, Text.translatable("gui.done").getString(), 0, this::doneEditingExitCallback);
		textFieldExitDestination = new BetterTextFieldWidget(1024, TextCase.DEFAULT, null, TranslationProvider.GUI_MTR_EXIT_DESTINATION.getString(), FULL_WIDGET_WIDTH - GuiHelper.DEFAULT_PADDING * 2 - textFieldExitName.getWidth() - doneEditingExitButton.getWidth(), null);

		exitsListWidget.setBounds(FULL_WIDGET_WIDTH, 0, FULL_WIDGET_WIDTH, Integer.MAX_VALUE);
		colorSelector = new ColorSelectorWidget(width / 2, () -> enableControls(true), this::applyBlur);
	}

	@Override
	protected void init() {
		super.init();
		final int widgetColumn1 = getWidgetColumn1();
		final int widgetColumn2Of3 = getWidgetColumn2Of3();
		final int widgetColumn3Of3 = getWidgetColumn3Of3();

		int widgetY = 0;
		nameTextField.setPosition(widgetColumn1, widgetY);
		nameTextField.setText(station.getName());
		openColorSelectorButton.setPosition(widgetColumn1 + FULL_WIDGET_WIDTH - openColorSelectorButton.getWidth(), widgetY);
		openColorSelectorButton.setBackgroundColor(ColorHelper.fullAlpha(station.getColor()));
		tempColor = station.getColor();

		widgetY += GuiHelper.DEFAULT_LINE_SIZE + GuiHelper.DEFAULT_PADDING;
		textFieldZoneX.setPosition(widgetColumn1, widgetY);
		textFieldZoneX.setText(String.valueOf(station.getZone1()));
		textFieldZoneY.setPosition(widgetColumn2Of3, widgetY);
		textFieldZoneY.setText(String.valueOf(station.getZone2()));
		textFieldZoneZ.setPosition(widgetColumn3Of3, widgetY);
		textFieldZoneZ.setText(String.valueOf(station.getZone3()));

		widgetY += GuiHelper.DEFAULT_LINE_SIZE * 2;
		addExitButton.setPosition(widgetColumn1, widgetY);
		textFieldExitName.setPosition(widgetColumn1, widgetY);
		textFieldExitDestination.setPosition(widgetColumn1 + GuiHelper.DEFAULT_PADDING + textFieldExitName.getWidth(), widgetY);
		doneEditingExitButton.setPosition(widgetColumn1 + FULL_WIDGET_WIDTH - doneEditingExitButton.getWidth(), widgetY);

		widgetY += GuiHelper.DEFAULT_LINE_SIZE;
		exitsListWidget.setPosition(widgetColumn1, widgetY);
		tempStationExits.clear();
		tempStationExits.addAll(FormattedStationExit.getFormattedStationExits(station.getExits(), false));
		doneEditingExitCallback();

		colorSelector.setPosition(width / 4, height * 2);

		addDrawableChild(openColorSelectorButton);
		addDrawableChild(nameTextField);

		addDrawableChild(textFieldZoneX);
		addDrawableChild(textFieldZoneY);
		addDrawableChild(textFieldZoneZ);

		addDrawableChild(addExitButton);
		addDrawableChild(textFieldExitName);
		addDrawableChild(textFieldExitDestination);
		addDrawableChild(doneEditingExitButton);

		addDrawableChild(exitsListWidget);
		addDrawableChild(colorSelector);
	}

	@Override
	public void close() {
		station.setName(nameTextField.getText());
		station.setColor(tempColor);

		try {
			station.setZone1(Integer.parseInt(textFieldZoneX.getText()));
		} catch (Exception ignored) {
			station.setZone1(0);
		}
		try {
			station.setZone2(Integer.parseInt(textFieldZoneY.getText()));
		} catch (Exception ignored) {
			station.setZone2(0);
		}
		try {
			station.setZone3(Integer.parseInt(textFieldZoneZ.getText()));
		} catch (Exception ignored) {
			station.setZone3(0);
		}

		cleanExits();
		station.getExits().clear();
		tempStationExits.forEach(exit -> {
			final StationExit stationExit = new StationExit();
			stationExit.setName(exit.name);
			stationExit.getDestinations().addAll(exit.destinations);
			station.getExits().add(stationExit);
		});

		RegistryClient.sendPacketToServer(new PacketUpdateData(new UpdateDataRequest(MinecraftClientData.getDashboardInstance()).addStation(station)));
		super.close();
	}

	@Override
	public ObjectArrayList<MutableText> getScreenTitle() {
		return ObjectArrayList.of(Text.literal(Utilities.formatName(nameTextField.getText())));
	}

	@Override
	public ObjectArrayList<MutableText> getScreenSubtitle() {
		return ObjectArrayList.of(TranslationProvider.GUI_MTR_PLATFORMS_IN_STATION.getMutableText(station.savedRails.size()));
	}

	@Override
	public ObjectArrayList<MutableText> getScreenDescription() {
		return new ObjectArrayList<>();
	}

	private void onOpenColorSelector() {
		colorSelector.setColorCallback(color -> {
			tempColor = color;
			openColorSelectorButton.setBackgroundColor(ColorHelper.fullAlpha(color));
			cleanExits();
			setExitListItems(null);
		}, tempColor);
		colorSelector.setY((height - colorSelector.getHeight()) / 2);
		enableControls(false);
	}

	private void startEditingExitCallback(@Nullable ObjectIntImmutablePair<FormattedStationExit> editingExitWithIndex) {
		addExitButton.visible = false;
		textFieldExitName.visible = true;
		textFieldExitDestination.visible = true;
		doneEditingExitButton.visible = true;

		this.editingExitWithIndex = editingExitWithIndex;

		if (this.editingExitWithIndex == null) {
			textFieldExitName.setText("");
			textFieldExitDestination.setText("");
		} else {
			final FormattedStationExit editingExit = this.editingExitWithIndex.left();
			textFieldExitName.setText(editingExit.name);
			textFieldExitDestination.setText(Utilities.getElement(editingExit.destinations, this.editingExitWithIndex.rightInt(), ""));
		}
	}

	private void doneEditingExitCallback() {
		addExitButton.visible = true;
		textFieldExitName.visible = false;
		textFieldExitDestination.visible = false;
		doneEditingExitButton.visible = false;

		final String exitName = textFieldExitName.getText();
		final String destination = textFieldExitDestination.getText();
		final String expandedExitName;

		// Don't modify station.getExits() directly until the screen is closed
		if (!exitName.isEmpty() && !destination.isEmpty()) {
			final FormattedStationExit newExit = new FormattedStationExit(exitName, ObjectArrayList.of(destination));
			expandedExitName = newExit.name;
			final FormattedStationExit existingExit = tempStationExits.stream().filter(exit -> exit.name.equals(newExit.name)).findFirst().orElse(null);

			if (existingExit == null) {
				if (editingExitWithIndex != null) {
					// If the exit name has been edited, remove the existing destination
					tempStationExits.stream().filter(exit -> exit.name.equals(editingExitWithIndex.left().name)).findFirst().ifPresent(exit -> Utilities.removeElement(exit.destinations, editingExitWithIndex.rightInt()));
				}
				// Adding a new exit parent
				tempStationExits.add(newExit);
			} else {
				// Editing an existing exit parent
				if (editingExitWithIndex == null) {
					// Add a new destination to the existing exit parent
					existingExit.destinations.add(destination);
				} else {
					final String oldExitName = editingExitWithIndex.left().name;
					if (oldExitName.equals(newExit.name)) {
						// If the exit name has not been edited, replace the existing destination
						Utilities.setElement(existingExit.destinations, editingExitWithIndex.rightInt(), destination);
					} else {
						// If the exit name has been edited, remove the existing destination and add it to the right parent
						tempStationExits.stream().filter(exit -> exit.name.equals(oldExitName)).findFirst().ifPresent(exit -> Utilities.removeElement(exit.destinations, editingExitWithIndex.rightInt()));
						existingExit.destinations.add(destination);
					}
				}
			}
		} else {
			expandedExitName = null;
		}

		editingExitWithIndex = null;
		cleanExits();
		setExitListItems(expandedExitName);
	}

	private void enableControls(boolean enabled) {
		nameTextField.active = enabled;
		openColorSelectorButton.active = enabled;

		textFieldZoneX.active = enabled;
		textFieldZoneY.active = enabled;
		textFieldZoneZ.active = enabled;

		addExitButton.active = enabled;
		textFieldExitName.active = enabled;
		textFieldExitDestination.active = enabled;
		doneEditingExitButton.active = enabled;

		exitsListWidget.active = enabled;
	}

	/**
	 * Checks for empty destinations and duplicate exit names and removes them from the list.
	 */
	private void cleanExits() {
		final ObjectArrayList<FormattedStationExit> exitsToRemove = new ObjectArrayList<>();
		final ObjectOpenHashSet<String> visitedExits = new ObjectOpenHashSet<>();
		tempStationExits.forEach(exit -> {
			if (exit.destinations.isEmpty() || visitedExits.contains(exit.name)) {
				exitsToRemove.add(exit);
			}
			visitedExits.add(exit.name);
		});
		exitsToRemove.forEach(tempStationExits::remove);
	}

	/**
	 * Writes the station exits to the list widget.
	 *
	 * @param expandedExitName the exit name that should be expanded by default in the list widget
	 */
	private void setExitListItems(@Nullable String expandedExitName) {
		exitsListWidget.setData(tempStationExits.stream().sorted().map(exit -> {
			final ObjectArrayList<ListItem<ObjectIntImmutablePair<FormattedStationExit>>> destinationListItems = new ObjectArrayList<>();

			for (int i = 0; i < exit.destinations.size(); i++) {
				destinationListItems.add(ListItem.createChild((drawing, x, y) -> {
				}, GuiHelper.MINECRAFT_FONT_SIZE, exit.destinations.get(i), new ObjectIntImmutablePair<>(exit, i), ObjectArrayList.of(
						new ObjectObjectImmutablePair<>(GuiHelper.EDIT_TEXTURE_ID, this::startEditingExitCallback),
						new ObjectObjectImmutablePair<>(GuiHelper.UP_TEXTURE_ID, exitWithIndex -> moveExitDestination(exitWithIndex.left(), exitWithIndex.rightInt(), -1)),
						new ObjectObjectImmutablePair<>(GuiHelper.DOWN_TEXTURE_ID, exitWithIndex -> moveExitDestination(exitWithIndex.left(), exitWithIndex.rightInt(), 1)),
						new ObjectObjectImmutablePair<>(GuiHelper.DELETE_TEXTURE_ID, exitWithIndex -> deleteExitDestination(exitWithIndex.left(), exitWithIndex.rightInt()))
				)));
			}

			final int destinationsCount = exit.destinations.size();
			final ListItem<ObjectIntImmutablePair<FormattedStationExit>> parentListItem = ListItem.createParent(
					(drawing, x, y) -> drawing.setVerticesWH(x + GuiHelper.DEFAULT_PADDING, y + GuiHelper.DEFAULT_PADDING, GuiHelper.MINECRAFT_FONT_SIZE, GuiHelper.MINECRAFT_FONT_SIZE).setColor(ColorHelper.fullAlpha(tempColor)).draw(),
					GuiHelper.DEFAULT_PADDING + GuiHelper.MINECRAFT_FONT_SIZE,
					String.format("%s%s%s", exit.name, destinationsCount == 0 ? "" : " " + exit.destinations.getFirst(), destinationsCount > 1 ? String.format(" (+%s)", destinationsCount - 1) : ""),
					Utilities.formatName(exit.name),
					destinationListItems
			);

			if (exit.name.equals(expandedExitName) && !parentListItem.isExpanded()) {
				parentListItem.toggle();
			}

			return parentListItem;
		}).collect(Collectors.toCollection(ObjectArrayList::new)));
	}

	private void moveExitDestination(FormattedStationExit exit, int index, int direction) {
		if (direction > 0 && index < exit.destinations.size() - 1 || direction < 0 && index > 0) {
			final String destination = exit.destinations.remove(index);
			if (hasShiftDown()) {
				if (direction > 0) {
					exit.destinations.add(destination);
				} else {
					exit.destinations.addFirst(destination);
				}
			} else {
				exit.destinations.add(index + direction, destination);
			}
		}

		cleanExits();
		setExitListItems(null);
	}

	private void deleteExitDestination(FormattedStationExit exit, int index) {
		Utilities.removeElement(exit.destinations, index);
		cleanExits();
		setExitListItems(null);
	}
}
