package org.mtr.screen;

import gg.essential.elementa.constraints.PixelConstraint;
import gg.essential.elementa.constraints.RelativeConstraint;
import gg.essential.elementa.constraints.SiblingConstraint;
import org.jspecify.annotations.Nullable;
import org.mtr.client.MinecraftClientData;
import org.mtr.core.data.Route;
import org.mtr.core.data.RouteType;
import org.mtr.core.data.Station;
import org.mtr.core.operation.UpdateDataRequest;
import org.mtr.core.tool.Utilities;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import org.mtr.packet.PacketCheckRouteIdHasDisabledAnnouncements;
import org.mtr.packet.PacketSetRouteIdHasDisabledAnnouncements;
import org.mtr.packet.PacketUpdateData;
import org.mtr.registry.RegistryClient;
import org.mtr.tool.GuiHelper;
import org.mtr.tool.ReleasedDynamicTextureRegistry;
import org.mtr.widget.ButtonComponent;
import org.mtr.widget.CheckboxComponent;
import org.mtr.widget.TextInputComponent;

public final class RouteScreen extends NameColorDataScreenBase<Route> {

	private RouteType routeType = RouteType.NORMAL;

	private final TextInputComponent routeNumberTextInput;
	@Nullable
	private final ButtonComponent routeTypeButton;
	private final CheckboxComponent isHiddenCheckbox;
	private final CheckboxComponent disableNextStationAnnouncementsCheckbox;
	@Nullable
	private final CheckboxComponent isClockwiseCheckbox;
	@Nullable
	private final CheckboxComponent isAnticlockwiseCheckbox;

	public RouteScreen(Route route, @Nullable ScreenBase previousScreenLegacy) {
		super(route, ObjectImmutableList.of(
			new ObjectObjectImmutablePair<>(ReleasedDynamicTextureRegistry.BRUSH_TEXTURE.get(), TranslationProvider.GUI_MTR_ROUTES.getString()),
			new ObjectObjectImmutablePair<>(ReleasedDynamicTextureRegistry.POPPY_TEXTURE.get(), TranslationProvider.GUI_MTR_ROUTE_COLOR.getString())
		), TranslationProvider.GUI_MTR_ROUTE_NAME, Utilities::formatName, TranslationProvider.GUI_MTR_ROUTE_COLOR, previousScreenLegacy);

		GuiHelper.createLabel(firstTabScrollComponent, TranslationProvider.GUI_MTR_ROUTE_NUMBER.getString());

		routeNumberTextInput = (TextInputComponent) new TextInputComponent()
			.setChildOf(firstTabScrollComponent)
			.setY(new SiblingConstraint())
			.setWidth(new RelativeConstraint())
			.setHeight(new PixelConstraint(20));

		routeNumberTextInput.setText(data.getRouteNumber());

		if (data.getTransportMode().hasRouteTypeVariation) {
			GuiHelper.createSpacing(firstTabScrollComponent);
			GuiHelper.createLabel(firstTabScrollComponent, TranslationProvider.GUI_MTR_ROUTE_TYPE.getString());

			routeTypeButton = (ButtonComponent) new ButtonComponent(true)
				.setChildOf(firstTabScrollComponent)
				.setY(new SiblingConstraint())
				.setWidth(new RelativeConstraint())
				.setHeight(new PixelConstraint(20));

			routeTypeButton.onClick(() -> {
				routeType = routeType.next();
				updateRouteTypeText();
			});

			routeType = data.getRouteType();
			updateRouteTypeText();
		} else {
			routeTypeButton = null;
		}

		GuiHelper.createSpacing(firstTabScrollComponent);

		isHiddenCheckbox = (CheckboxComponent) new CheckboxComponent()
			.setChildOf(firstTabScrollComponent)
			.setY(new SiblingConstraint())
			.setWidth(new RelativeConstraint());

		isHiddenCheckbox.setText(TranslationProvider.GUI_MTR_IS_ROUTE_HIDDEN.getString());
		isHiddenCheckbox.setChecked(data.getHidden());

		GuiHelper.createSpacing(firstTabScrollComponent);

		disableNextStationAnnouncementsCheckbox = (CheckboxComponent) new CheckboxComponent()
			.setChildOf(firstTabScrollComponent)
			.setY(new SiblingConstraint())
			.setWidth(new RelativeConstraint());

		disableNextStationAnnouncementsCheckbox.setText(TranslationProvider.GUI_MTR_DISABLE_NEXT_STATION_ANNOUNCEMENTS.getString());
		RegistryClient.sendPacketToServer(new PacketCheckRouteIdHasDisabledAnnouncements(data.getId(), disableNextStationAnnouncementsCheckbox::setChecked));

		final boolean isCircular;
		if (!route.getRoutePlatforms().isEmpty()) {
			final Station firstStation = Utilities.getElement(route.getRoutePlatforms(), 0).platform.area;
			final Station lastStation = Utilities.getElement(route.getRoutePlatforms(), -1).platform.area;
			isCircular = firstStation != null && lastStation != null && firstStation.getId() == lastStation.getId();
		} else {
			isCircular = false;
		}

		if (isCircular) {
			GuiHelper.createSpacing(firstTabScrollComponent);

			isClockwiseCheckbox = (CheckboxComponent) new CheckboxComponent()
				.setChildOf(firstTabScrollComponent)
				.setY(new SiblingConstraint())
				.setWidth(new RelativeConstraint());

			GuiHelper.createSpacing(firstTabScrollComponent);

			isAnticlockwiseCheckbox = (CheckboxComponent) new CheckboxComponent()
				.setChildOf(firstTabScrollComponent)
				.setY(new SiblingConstraint())
				.setWidth(new RelativeConstraint());

			isClockwiseCheckbox.setText(TranslationProvider.GUI_MTR_IS_CLOCKWISE_ROUTE.getString());
			isClockwiseCheckbox.setChecked(data.getCircularState() == Route.CircularState.CLOCKWISE);
			isClockwiseCheckbox.onClick(() -> {
				if (isClockwiseCheckbox.isChecked() && isAnticlockwiseCheckbox.isChecked()) {
					isAnticlockwiseCheckbox.setChecked(false);
				}
			});

			isAnticlockwiseCheckbox.setText(TranslationProvider.GUI_MTR_IS_ANTICLOCKWISE_ROUTE.getString());
			isAnticlockwiseCheckbox.setChecked(data.getCircularState() == Route.CircularState.ANTICLOCKWISE);
			isAnticlockwiseCheckbox.onClick(() -> {
				if (isAnticlockwiseCheckbox.isChecked() && isClockwiseCheckbox.isChecked()) {
					isClockwiseCheckbox.setChecked(false);
				}
			});
		} else {
			isClockwiseCheckbox = null;
			isAnticlockwiseCheckbox = null;
		}
	}

	@Override
	protected void onClose() {
		data.setRouteNumber(routeNumberTextInput.getText());
		data.setRouteType(routeType);
		data.setHidden(isHiddenCheckbox.isChecked());

		if (isClockwiseCheckbox != null && isAnticlockwiseCheckbox != null) {
			data.setCircularState(isClockwiseCheckbox.isChecked() ? Route.CircularState.CLOCKWISE : (isAnticlockwiseCheckbox.isChecked() ? Route.CircularState.ANTICLOCKWISE : Route.CircularState.NONE));
		} else {
			data.setCircularState(Route.CircularState.NONE);
		}

		RegistryClient.sendPacketToServer(new PacketUpdateData(new UpdateDataRequest(MinecraftClientData.getDashboardInstance()).addRoute(data)));
		RegistryClient.sendPacketToServer(new PacketSetRouteIdHasDisabledAnnouncements(data.getId(), disableNextStationAnnouncementsCheckbox.isChecked()));
	}

	private void updateRouteTypeText() {
		if (routeTypeButton != null) {
			routeTypeButton.setText(switch (data.getTransportMode()) {
				case TRAIN -> (routeType == RouteType.LIGHT_RAIL ? TranslationProvider.GUI_MTR_ROUTE_TYPE_TRAIN_LIGHT_RAIL : (routeType == RouteType.HIGH_SPEED ? TranslationProvider.GUI_MTR_ROUTE_TYPE_TRAIN_HIGH_SPEED : TranslationProvider.GUI_MTR_ROUTE_TYPE_TRAIN_NORMAL)).getString();
				case BOAT -> (routeType == RouteType.LIGHT_RAIL ? TranslationProvider.GUI_MTR_ROUTE_TYPE_BOAT_LIGHT_RAIL : (routeType == RouteType.HIGH_SPEED ? TranslationProvider.GUI_MTR_ROUTE_TYPE_BOAT_HIGH_SPEED : TranslationProvider.GUI_MTR_ROUTE_TYPE_BOAT_NORMAL)).getString();
				default -> "";
			});
		}
	}
}
