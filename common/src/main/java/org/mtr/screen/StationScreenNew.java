package org.mtr.screen;

import gg.essential.elementa.components.ScrollComponent;
import gg.essential.elementa.components.UIContainer;
import gg.essential.elementa.components.UIWrappedText;
import gg.essential.elementa.constraints.*;
import it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import org.mtr.client.MinecraftClientData;
import org.mtr.core.data.Station;
import org.mtr.core.operation.UpdateDataRequest;
import org.mtr.core.tool.Utilities;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.packet.PacketUpdateData;
import org.mtr.registry.RegistryClient;
import org.mtr.tool.GuiHelper;
import org.mtr.tool.ReleasedDynamicTextureRegistry;
import org.mtr.widget.BackgroundComponent;
import org.mtr.widget.ColorInputComponent;
import org.mtr.widget.ScrollPanelComponent;
import org.mtr.widget.TextInputComponent;

import java.awt.*;

public final class StationScreenNew extends WindowBase {

	private final Station station;

	private final BackgroundComponent backgroundComponent = new BackgroundComponent(getWindow(), ObjectImmutableList.of(
			new ObjectObjectImmutablePair<>(ReleasedDynamicTextureRegistry.BRUSH_TEXTURE.get(), TranslationProvider.GUI_MTR_STATION.getString()),
			new ObjectObjectImmutablePair<>(ReleasedDynamicTextureRegistry.POPPY_TEXTURE.get(), TranslationProvider.GUI_MTR_STATION_COLOR.getString()),
			new ObjectObjectImmutablePair<>(ReleasedDynamicTextureRegistry.EXIT_TEXTURE.get(), TranslationProvider.GUI_MTR_EXITS.getString())
	));

	private final UIWrappedText titleText;
	private final TextInputComponent nameTextInput;
	private final TextInputComponent zoneXTextInput;
	private final TextInputComponent zoneYTextInput;
	private final TextInputComponent zoneZTextInput;
	private final ColorInputComponent colorInputComponent;

	public StationScreenNew(Station station) {
		this.station = station;

		titleText = (UIWrappedText) new UIWrappedText("", false)
				.setChildOf(backgroundComponent.containers[0])
				.setWidth(new RelativeConstraint())
				.setColor(new Color(GuiHelper.MINECRAFT_GUI_TITLE_TEXT_COLOR));

		final ScrollComponent scrollComponent = ((ScrollPanelComponent) new ScrollPanelComponent(true)
				.setChildOf(backgroundComponent.containers[0])
				.setY(new SiblingConstraint(GuiHelper.DEFAULT_PADDING))
				.setWidth(new RelativeConstraint())
				.setHeight(new SubtractiveConstraint(new FillConstraint(), new PixelConstraint(GuiHelper.DEFAULT_PADDING)))).contentContainer;

		GuiHelper.createLabel(scrollComponent, TranslationProvider.GUI_MTR_STATION_NAME.getString());

		nameTextInput = (TextInputComponent) new TextInputComponent()
				.setChildOf(scrollComponent)
				.setY(new SiblingConstraint())
				.setWidth(new RelativeConstraint())
				.setHeight(new PixelConstraint(20));

		nameTextInput.setText(station.getName());
		nameTextInput.onChange(this::updateTitle);
		updateTitle();

		GuiHelper.createSpacing(scrollComponent);
		GuiHelper.createLabel(scrollComponent, TranslationProvider.GUI_MTR_ZONE.getString());

		final UIContainer zoneContainer = (UIContainer) new UIContainer()
				.setChildOf(scrollComponent)
				.setY(new SiblingConstraint())
				.setWidth(new RelativeConstraint())
				.setHeight(new PixelConstraint(20));

		zoneXTextInput = createZoneTextInput(zoneContainer, station.getZone1());
		zoneYTextInput = createZoneTextInput(zoneContainer, station.getZone2());
		zoneZTextInput = createZoneTextInput(zoneContainer, station.getZone3());

		new UIWrappedText(TranslationProvider.GUI_MTR_STATION_COLOR.getString(), false)
				.setChildOf(backgroundComponent.containers[1])
				.setWidth(new RelativeConstraint())
				.setColor(new Color(GuiHelper.MINECRAFT_GUI_TITLE_TEXT_COLOR));

		colorInputComponent = (ColorInputComponent) new ColorInputComponent()
				.setChildOf(backgroundComponent.containers[1])
				.setWidth(new RelativeConstraint())
				.setHeight(new RelativeConstraint());

		colorInputComponent.setSelectedColor(new Color(station.getColor()));

		new UIWrappedText(TranslationProvider.GUI_MTR_EXITS.getString(), false)
				.setChildOf(backgroundComponent.containers[2])
				.setWidth(new RelativeConstraint())
				.setColor(new Color(GuiHelper.MINECRAFT_GUI_TITLE_TEXT_COLOR));
	}

	@Override
	public void onScreenClose() {
		station.setName(nameTextInput.getText());
		station.setColor(colorInputComponent.getSelectedColor().getRGB());

		try {
			station.setZone1(Long.parseLong(zoneXTextInput.getText()));
		} catch (Exception ignored) {
			station.setZone1(0);
		}
		try {
			station.setZone2(Long.parseLong(zoneYTextInput.getText()));
		} catch (Exception ignored) {
			station.setZone2(0);
		}
		try {
			station.setZone3(Long.parseLong(zoneZTextInput.getText()));
		} catch (Exception ignored) {
			station.setZone3(0);
		}

		// TODO exits

		RegistryClient.sendPacketToServer(new PacketUpdateData(new UpdateDataRequest(MinecraftClientData.getDashboardInstance()).addStation(station)));
		super.onScreenClose();
	}

	private void updateTitle() {
		titleText.setText(TranslationProvider.GUI_MTR_STATION.getString(Utilities.formatName(nameTextInput.getText())));
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
}
