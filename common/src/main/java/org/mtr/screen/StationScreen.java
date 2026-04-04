package org.mtr.screen;

import gg.essential.elementa.components.UIContainer;
import gg.essential.elementa.components.UIWrappedText;
import gg.essential.elementa.constraints.*;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import net.minecraft.text.Text;
import org.mtr.client.MinecraftClientData;
import org.mtr.core.data.Station;
import org.mtr.core.data.StationExit;
import org.mtr.core.operation.UpdateDataRequest;
import org.mtr.core.tool.Utilities;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.packet.PacketUpdateData;
import org.mtr.registry.RegistryClient;
import org.mtr.tool.GuiHelper;
import org.mtr.tool.ReleasedDynamicTextureRegistry;
import org.mtr.widget.ButtonComponent;
import org.mtr.widget.ListComponent;
import org.mtr.widget.SlotBackgroundComponent;
import org.mtr.widget.TextInputComponent;

import javax.annotation.Nullable;
import java.awt.*;

public final class StationScreen extends NameColorDataScreenBase<Station> {

	@Nullable
	private EditingStationExit editingStationExit;

	private final UIContainer exitListContainer = (UIContainer) new UIContainer()
			.setChildOf(backgroundComponent.containers[2])
			.setWidth(new RelativeConstraint())
			.setHeight(new RelativeConstraint());

	private final UIContainer editExitContainer = (UIContainer) new UIContainer()
			.setChildOf(backgroundComponent.containers[2])
			.setWidth(new RelativeConstraint())
			.setHeight(new RelativeConstraint());

	private final TextInputComponent zoneXTextInput;
	private final TextInputComponent zoneYTextInput;
	private final TextInputComponent zoneZTextInput;
	private final ListComponent<StationExit> stationExitListComponent;
	private final TextInputComponent stationExitParentTextInput;
	private final TextInputComponent stationExitDestinationTextInput;
	private final ButtonComponent editExitButton;

	public StationScreen(Station station, @Nullable ScreenBase previousScreenLegacy) {
		super(station, ObjectImmutableList.of(
				new ObjectObjectImmutablePair<>(ReleasedDynamicTextureRegistry.BRUSH_TEXTURE.get(), TranslationProvider.GUI_MTR_STATION.getString()),
				new ObjectObjectImmutablePair<>(ReleasedDynamicTextureRegistry.POPPY_TEXTURE.get(), TranslationProvider.GUI_MTR_STATION_COLOR.getString()),
				new ObjectObjectImmutablePair<>(ReleasedDynamicTextureRegistry.EXIT_TEXTURE.get(), TranslationProvider.GUI_MTR_EXITS.getString())
		), TranslationProvider.GUI_MTR_STATION_NAME, name -> TranslationProvider.GUI_MTR_STATION.getString(Utilities.formatName(name)), TranslationProvider.GUI_MTR_STATION_COLOR, previousScreenLegacy);

		GuiHelper.createLabel(firstTabScrollComponent, TranslationProvider.GUI_MTR_ZONE.getString());

		final UIContainer zoneContainer = (UIContainer) new UIContainer()
				.setChildOf(firstTabScrollComponent)
				.setY(new SiblingConstraint())
				.setWidth(new RelativeConstraint())
				.setHeight(new PixelConstraint(20));

		zoneXTextInput = createZoneTextInput(zoneContainer, station.getZone1());
		zoneYTextInput = createZoneTextInput(zoneContainer, station.getZone2());
		zoneZTextInput = createZoneTextInput(zoneContainer, station.getZone3());

		new UIWrappedText(TranslationProvider.GUI_MTR_EXITS.getString(), false)
				.setChildOf(exitListContainer)
				.setWidth(new RelativeConstraint())
				.setColor(new Color(GuiHelper.MINECRAFT_GUI_TITLE_TEXT_COLOR));

		final ButtonComponent addExitButton = (ButtonComponent) (new ButtonComponent(true)
				.setChildOf(exitListContainer)
				.setY(new SiblingConstraint(GuiHelper.DEFAULT_PADDING))
				.setWidth(new RelativeConstraint()));

		final SlotBackgroundComponent slotBackgroundComponent = (SlotBackgroundComponent) new SlotBackgroundComponent()
				.setChildOf(exitListContainer)
				.setY(new SiblingConstraint(GuiHelper.DEFAULT_PADDING))
				.setWidth(new RelativeConstraint())
				.setHeight(new SubtractiveConstraint(new FillConstraint(), new PixelConstraint(GuiHelper.DEFAULT_PADDING * 2)));

		stationExitListComponent = GuiHelper.createListComponent(slotBackgroundComponent);
		updateStationExitListComponent();

		new UIWrappedText(TranslationProvider.GUI_MTR_ADD_EXIT.getString(), false)
				.setChildOf(editExitContainer)
				.setWidth(new RelativeConstraint())
				.setColor(new Color(GuiHelper.MINECRAFT_GUI_TITLE_TEXT_COLOR));

		GuiHelper.createSpacing(editExitContainer);
		GuiHelper.createLabel(editExitContainer, TranslationProvider.GUI_MTR_EXIT_NAME.getString());

		stationExitParentTextInput = (TextInputComponent) new TextInputComponent()
				.setChildOf(editExitContainer)
				.setY(new SiblingConstraint())
				.setWidth(new RelativeConstraint())
				.setHeight(new PixelConstraint(20));

		stationExitParentTextInput.onChange(this::updateEditExitButton);

		GuiHelper.createSpacing(editExitContainer);
		GuiHelper.createLabel(editExitContainer, TranslationProvider.GUI_MTR_EXIT_DESTINATION.getString());

		stationExitDestinationTextInput = (TextInputComponent) new TextInputComponent()
				.setChildOf(editExitContainer)
				.setY(new SiblingConstraint())
				.setWidth(new RelativeConstraint())
				.setHeight(new PixelConstraint(20));

		stationExitDestinationTextInput.onChange(this::updateEditExitButton);

		final UIContainer buttonContainer = (UIContainer) new UIContainer()
				.setChildOf(editExitContainer)
				.setY(new SiblingConstraint(GuiHelper.DEFAULT_PADDING))
				.setWidth(new RelativeConstraint())
				.setHeight(new RelativeConstraint());

		editExitButton = (ButtonComponent) new ButtonComponent(false)
				.setChildOf(buttonContainer)
				.setWidth(new ScaleConstraint(new RelativeConstraint(), 0.5F));

		editExitButton.onClick(() -> {
			if (editingStationExit != null) {
				final String newName = stationExitParentTextInput.getText();
				final String newDestination = stationExitDestinationTextInput.getText();

				if (editingStationExit.index >= 0) {
					// Existing exit
					final ObjectArrayList<String> existingDestinations = findOrCreateExit(editingStationExit.name).getDestinations();
					if (editingStationExit.name.equals(newName)) {
						existingDestinations.set(editingStationExit.index, newDestination);
					} else {
						existingDestinations.remove(editingStationExit.index);
						findOrCreateExit(newName).getDestinations().add(newDestination);
					}
				} else {
					// New exit
					findOrCreateExit(newName).getDestinations().add(newDestination);
				}
			}

			editingStationExit = null;
			updateStationExitListComponent();
			updateContainers();
		});

		final ButtonComponent cancelButton = (ButtonComponent) new ButtonComponent(false)
				.setChildOf(buttonContainer)
				.setX(new SiblingConstraint())
				.setWidth(new ScaleConstraint(new RelativeConstraint(), 0.5F));

		cancelButton.setText(Text.translatable("gui.cancel").getString());
		cancelButton.onClick(() -> {
			editingStationExit = null;
			updateContainers();
		});

		addExitButton.setText(TranslationProvider.GUI_MTR_ADD_EXIT.getString());
		addExitButton.onClick(() -> {
			editingStationExit = new EditingStationExit("", "", -1);
			stationExitParentTextInput.setText("");
			stationExitDestinationTextInput.setText("");
			updateContainers();
		});

		updateContainers();
	}

	@Override
	public void onClose() {
		try {
			data.setZone1(Long.parseLong(zoneXTextInput.getText()));
		} catch (Exception ignored) {
			data.setZone1(0);
		}
		try {
			data.setZone2(Long.parseLong(zoneYTextInput.getText()));
		} catch (Exception ignored) {
			data.setZone2(0);
		}
		try {
			data.setZone3(Long.parseLong(zoneZTextInput.getText()));
		} catch (Exception ignored) {
			data.setZone3(0);
		}

		RegistryClient.sendPacketToServer(new PacketUpdateData(new UpdateDataRequest(MinecraftClientData.getDashboardInstance()).addStation(data)));
	}

	private void updateStationExitListComponent() {
		data.getExits().removeIf(stationExit -> stationExit.getDestinations().isEmpty());
		ListComponent.setStationExits(stationExitListComponent, data.getExits(), false, ObjectArrayList.of(
				new ObjectObjectImmutablePair<>(GuiHelper.EDIT_TEXTURE_ID, (indexList, data) -> {
					final int index = indexList.getLast();
					final String destination = data.getDestinations().get(index);
					editingStationExit = new EditingStationExit(data.getName(), destination, index);
					stationExitParentTextInput.setText(data.getName());
					stationExitDestinationTextInput.setText(destination);
					editExitButton.setText(TranslationProvider.GUI_MTR_EDIT_EXIT.getString());
					updateContainers();
				}),
				new ObjectObjectImmutablePair<>(GuiHelper.DELETE_TEXTURE_ID, (indexList, data) -> {
					data.getDestinations().remove((int) indexList.getLast());
					updateStationExitListComponent();
				})
		));
	}

	private void updateContainers() {
		if (editingStationExit == null) {
			exitListContainer.unhide(true);
			editExitContainer.hide(true);
		} else {
			exitListContainer.hide(true);
			editExitContainer.unhide(true);
			editExitButton.setText((editingStationExit.index >= 0 ? TranslationProvider.GUI_MTR_EDIT_EXIT : TranslationProvider.GUI_MTR_ADD_EXIT).getString());
			updateEditExitButton();
		}
	}

	private void updateEditExitButton() {
		if (editingStationExit != null) {
			final String name = stationExitParentTextInput.getText();
			final String destination = stationExitDestinationTextInput.getText();
			editExitButton.setDisabled(name.equals(editingStationExit.name) && destination.equals(editingStationExit.destination) || name.isEmpty() || destination.isEmpty());
		}
	}

	private StationExit findOrCreateExit(String name) {
		for (final StationExit stationExit : data.getExits()) {
			if (stationExit.getName().equals(name)) {
				return stationExit;
			}
		}

		final StationExit stationExit = new StationExit();
		stationExit.setName(name);
		data.getExits().add(stationExit);
		return stationExit;
	}

	private static TextInputComponent createZoneTextInput(UIContainer container, long existingZone) {
		final TextInputComponent textInput = (TextInputComponent) new TextInputComponent()
				.setChildOf(container)
				.setX(new SiblingConstraint())
				.setWidth(new ScaleConstraint(new RelativeConstraint(), 1F / 3))
				.setHeight(new PixelConstraint(20));

		textInput.setFilter("[^-\\d]");
		textInput.setText(String.valueOf(existingZone));
		return textInput;
	}

	private record EditingStationExit(String name, String destination, int index) {
	}
}
