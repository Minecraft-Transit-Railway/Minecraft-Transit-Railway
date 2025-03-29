package org.mtr.mod.screen;

import org.mtr.core.data.Route;
import org.mtr.core.data.RouteType;
import org.mtr.core.data.Station;
import org.mtr.core.operation.UpdateDataRequest;
import org.mtr.core.tool.Utilities;
import org.mtr.mapping.holder.ClickableWidget;
import org.mtr.mapping.holder.MutableText;
import org.mtr.mapping.holder.Text;
import org.mtr.mapping.mapper.*;
import org.mtr.mapping.tool.TextCase;
import org.mtr.mod.Init;
import org.mtr.mod.InitClient;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.client.MinecraftClientData;
import org.mtr.mod.data.IGui;
import org.mtr.mod.generated.lang.TranslationProvider;
import org.mtr.mod.packet.PacketCheckRouteIdHasDisabledAnnouncements;
import org.mtr.mod.packet.PacketSetRouteIdHasDisabledAnnouncements;
import org.mtr.mod.packet.PacketUpdateData;

public class EditRouteScreen extends EditNameColorScreenBase<Route> implements IGui {

	private final MutableText lightRailRouteNumberText = TranslationProvider.GUI_MTR_LIGHT_RAIL_ROUTE_NUMBER.getMutableText();

	private final TextFieldWidgetExtension textFieldLightRailRouteNumber;
	private final ButtonWidgetExtension buttonRouteType;
	private final CheckboxWidgetExtension buttonIsRouteHidden;
	private final CheckboxWidgetExtension buttonDisableNextStationAnnouncements;
	private final CheckboxWidgetExtension buttonIsClockwiseRoute;
	private final CheckboxWidgetExtension buttonIsAntiClockwiseRoute;

	private final boolean isCircular;

	private static final int CHECKBOX_WIDTH = 160;

	public EditRouteScreen(Route route, ScreenExtension previousScreenExtension) {
		super(route, TranslationProvider.GUI_MTR_ROUTE_NAME, TranslationProvider.GUI_MTR_ROUTE_COLOR, previousScreenExtension);

		textFieldLightRailRouteNumber = new TextFieldWidgetExtension(0, 0, 0, SQUARE_SIZE, 256, TextCase.DEFAULT, null, null);
		buttonRouteType = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, TranslationProvider.GUI_MTR_ADD_VALUE.getMutableText(), button -> setRouteType(route, route.getRouteType().next()));
		buttonIsRouteHidden = new CheckboxWidgetExtension(0, 0, 0, SQUARE_SIZE, true, this::setIsRouteHidden);
		buttonIsRouteHidden.setMessage2(TranslationProvider.GUI_MTR_IS_ROUTE_HIDDEN.getText());
		buttonDisableNextStationAnnouncements = new CheckboxWidgetExtension(0, 0, 0, SQUARE_SIZE, true, this::setDisableNextStationAnnouncements);
		buttonDisableNextStationAnnouncements.setMessage2(TranslationProvider.GUI_MTR_DISABLE_NEXT_STATION_ANNOUNCEMENTS.getText());
		buttonIsClockwiseRoute = new CheckboxWidgetExtension(0, 0, 0, SQUARE_SIZE, true, this::setIsClockwise);
		buttonIsClockwiseRoute.setMessage2(TranslationProvider.GUI_MTR_IS_CLOCKWISE_ROUTE.getText());
		buttonIsAntiClockwiseRoute = new CheckboxWidgetExtension(0, 0, 0, SQUARE_SIZE, true, this::setIsAntiClockwise);
		buttonIsAntiClockwiseRoute.setMessage2(TranslationProvider.GUI_MTR_IS_ANTICLOCKWISE_ROUTE.getText());

		if (!route.getRoutePlatforms().isEmpty()) {
			final Station firstStation = Utilities.getElement(route.getRoutePlatforms(), 0).platform.area;
			final Station lastStation = Utilities.getElement(route.getRoutePlatforms(), -1).platform.area;
			isCircular = firstStation != null && lastStation != null && firstStation.getId() == lastStation.getId();
		} else {
			isCircular = false;
		}
	}

	@Override
	protected void init2() {
		setPositionsAndInit(SQUARE_SIZE, width / 4 * 3 - SQUARE_SIZE, width - SQUARE_SIZE);

		IDrawing.setPositionAndWidth(buttonRouteType, SQUARE_SIZE, SQUARE_SIZE * 3, CHECKBOX_WIDTH);
		setRouteType(data, data.getRouteType());

		IDrawing.setPositionAndWidth(buttonIsRouteHidden, SQUARE_SIZE, SQUARE_SIZE * 4, CHECKBOX_WIDTH);
		IDrawing.setPositionAndWidth(buttonDisableNextStationAnnouncements, SQUARE_SIZE, SQUARE_SIZE * 5, CHECKBOX_WIDTH);
		IDrawing.setPositionAndWidth(textFieldLightRailRouteNumber, SQUARE_SIZE + TEXT_FIELD_PADDING / 2, SQUARE_SIZE * 7 + TEXT_FIELD_PADDING / 2, CHECKBOX_WIDTH - TEXT_FIELD_PADDING);
		textFieldLightRailRouteNumber.setText2(data.getRouteNumber());

		IDrawing.setPositionAndWidth(buttonIsClockwiseRoute, SQUARE_SIZE, SQUARE_SIZE * 8 + TEXT_FIELD_PADDING, CHECKBOX_WIDTH);
		IDrawing.setPositionAndWidth(buttonIsAntiClockwiseRoute, SQUARE_SIZE, SQUARE_SIZE * 9 + TEXT_FIELD_PADDING, CHECKBOX_WIDTH);

		if (data.getTransportMode().hasRouteTypeVariation) {
			addChild(new ClickableWidget(buttonRouteType));
		}
		addChild(new ClickableWidget(textFieldLightRailRouteNumber));
		addChild(new ClickableWidget(buttonIsRouteHidden));
		addChild(new ClickableWidget(buttonDisableNextStationAnnouncements));
		if (isCircular) {
			addChild(new ClickableWidget(buttonIsClockwiseRoute));
			addChild(new ClickableWidget(buttonIsAntiClockwiseRoute));
		}

		setIsRouteHidden(data.getHidden());
		InitClient.REGISTRY_CLIENT.sendPacketToServer(new PacketCheckRouteIdHasDisabledAnnouncements(data.getId(), this::setDisableNextStationAnnouncements));
		setIsClockwise(data.getCircularState() == Route.CircularState.CLOCKWISE);
		setIsAntiClockwise(data.getCircularState() == Route.CircularState.ANTICLOCKWISE);
	}

	@Override
	public void render(GraphicsHolder graphicsHolder, int mouseX, int mouseY, float delta) {
		try {
			renderBackground(graphicsHolder);
			renderTextFields(graphicsHolder);

			if (textFieldLightRailRouteNumber.visible) {
				graphicsHolder.drawText(lightRailRouteNumberText, SQUARE_SIZE, SQUARE_SIZE * 6 + TEXT_PADDING, ARGB_WHITE, false, GraphicsHolder.getDefaultLight());
			}

			super.render(graphicsHolder, mouseX, mouseY, delta);
		} catch (Exception e) {
			Init.LOGGER.error("", e);
		}
	}

	@Override
	protected void saveData() {
		super.saveData();

		data.setRouteNumber(textFieldLightRailRouteNumber.getText2());
		data.setHidden(buttonIsRouteHidden.isChecked2());
		final boolean routeIdHasDisabledAnnouncements = buttonDisableNextStationAnnouncements.isChecked2();
		MinecraftClientData.getInstance().setRouteIdHasDisabledAnnouncements(data.getId(), routeIdHasDisabledAnnouncements);

		if (isCircular) {
			data.setCircularState(buttonIsClockwiseRoute.isChecked2() ? Route.CircularState.CLOCKWISE : buttonIsAntiClockwiseRoute.isChecked2() ? Route.CircularState.ANTICLOCKWISE : Route.CircularState.NONE);
		} else {
			data.setCircularState(Route.CircularState.NONE);
		}

		InitClient.REGISTRY_CLIENT.sendPacketToServer(new PacketUpdateData(new UpdateDataRequest(MinecraftClientData.getDashboardInstance()).addRoute(data)));
		InitClient.REGISTRY_CLIENT.sendPacketToServer(new PacketSetRouteIdHasDisabledAnnouncements(data.getId(), routeIdHasDisabledAnnouncements));
	}

	private void setRouteType(Route route, RouteType newRouteType) {
		route.setRouteType(newRouteType);
		buttonRouteType.setMessage2(getRouteTypeText(route));
	}

	private void setIsRouteHidden(boolean isRouteHidden) {
		buttonIsRouteHidden.setChecked(isRouteHidden);
	}

	private void setDisableNextStationAnnouncements(boolean hasNextStationAnnouncements) {
		buttonDisableNextStationAnnouncements.setChecked(hasNextStationAnnouncements);
	}

	private void setIsClockwise(boolean isClockwise) {
		buttonIsClockwiseRoute.setChecked(isClockwise);
		if (isClockwise) {
			buttonIsAntiClockwiseRoute.setChecked(false);
		}
	}

	private void setIsAntiClockwise(boolean isAntiClockwise) {
		buttonIsAntiClockwiseRoute.setChecked(isAntiClockwise);
		if (isAntiClockwise) {
			buttonIsClockwiseRoute.setChecked(false);
		}
	}

	private static Text getRouteTypeText(Route route) {
		final RouteType routeType = route.getRouteType();
		switch (route.getTransportMode()) {
			case TRAIN:
				return (routeType == RouteType.LIGHT_RAIL ? TranslationProvider.GUI_MTR_ROUTE_TYPE_TRAIN_LIGHT_RAIL : routeType == RouteType.HIGH_SPEED ? TranslationProvider.GUI_MTR_ROUTE_TYPE_TRAIN_HIGH_SPEED : TranslationProvider.GUI_MTR_ROUTE_TYPE_TRAIN_NORMAL).getText();
			case BOAT:
				return (routeType == RouteType.LIGHT_RAIL ? TranslationProvider.GUI_MTR_ROUTE_TYPE_BOAT_LIGHT_RAIL : routeType == RouteType.HIGH_SPEED ? TranslationProvider.GUI_MTR_ROUTE_TYPE_BOAT_HIGH_SPEED : TranslationProvider.GUI_MTR_ROUTE_TYPE_BOAT_NORMAL).getText();
			default:
				return new Text(TextHelper.literal("").data);
		}
	}
}
