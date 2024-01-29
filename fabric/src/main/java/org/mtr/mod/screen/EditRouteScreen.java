package org.mtr.mod.screen;

import org.mtr.core.data.Route;
import org.mtr.core.data.RouteType;
import org.mtr.core.data.Station;
import org.mtr.core.servlet.IntegrationServlet;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectSet;
import org.mtr.mapping.holder.ClickableWidget;
import org.mtr.mapping.holder.MutableText;
import org.mtr.mapping.holder.Text;
import org.mtr.mapping.mapper.*;
import org.mtr.mapping.tool.TextCase;
import org.mtr.mod.Init;
import org.mtr.mod.InitClient;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.data.IGui;
import org.mtr.mod.packet.IPacket;
import org.mtr.mod.packet.PacketData;

public class EditRouteScreen extends EditNameColorScreenBase<Route> implements IGui, IPacket {

	private final MutableText lightRailRouteNumberText = TextHelper.translatable("gui.mtr.light_rail_route_number");

	private final TextFieldWidgetExtension textFieldLightRailRouteNumber;
	private final ButtonWidgetExtension buttonRouteType;
	private final CheckboxWidgetExtension buttonIsRouteHidden;
	private final CheckboxWidgetExtension buttonDisableNextStationAnnouncements;
	private final CheckboxWidgetExtension buttonIsClockwiseRoute;
	private final CheckboxWidgetExtension buttonIsAntiClockwiseRoute;

	private final boolean isCircular;

	private static final int CHECKBOX_WIDTH = 160;

	public EditRouteScreen(Route route, DashboardScreen dashboardScreen) {
		super(route, dashboardScreen, "gui.mtr.route_name", "gui.mtr.route_color");

		textFieldLightRailRouteNumber = new TextFieldWidgetExtension(0, 0, 0, SQUARE_SIZE, 256, TextCase.DEFAULT, null, null);
		buttonRouteType = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, TextHelper.translatable("gui.mtr.add_value"), button -> setRouteType(route, route.getRouteType().next()));
		buttonIsRouteHidden = new CheckboxWidgetExtension(0, 0, 0, SQUARE_SIZE, true, this::setIsRouteHidden);
		buttonIsRouteHidden.setMessage2(new Text(TextHelper.translatable("gui.mtr.is_route_hidden").data));
		buttonDisableNextStationAnnouncements = new CheckboxWidgetExtension(0, 0, 0, SQUARE_SIZE, true, this::setDisableNextStationAnnouncements);
		buttonDisableNextStationAnnouncements.setMessage2(new Text(TextHelper.translatable("gui.mtr.disable_next_station_announcements").data));
		buttonIsClockwiseRoute = new CheckboxWidgetExtension(0, 0, 0, SQUARE_SIZE, true, this::setIsClockwise);
		buttonIsClockwiseRoute.setMessage2(new Text(TextHelper.translatable("gui.mtr.is_clockwise_route").data));
		buttonIsAntiClockwiseRoute = new CheckboxWidgetExtension(0, 0, 0, SQUARE_SIZE, true, this::setIsAntiClockwise);
		buttonIsAntiClockwiseRoute.setMessage2(new Text(TextHelper.translatable("gui.mtr.is_anticlockwise_route").data));

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
		// TODO setDisableNextStationAnnouncements(data.disableNextStationAnnouncements);
		setIsClockwise(data.getCircularState() == Route.CircularState.CLOCKWISE);
		setIsAntiClockwise(data.getCircularState() == Route.CircularState.ANTICLOCKWISE);
	}

	@Override
	public void render(GraphicsHolder graphicsHolder, int mouseX, int mouseY, float delta) {
		try {
			renderBackground(graphicsHolder);
			renderTextFields(graphicsHolder);

			if (textFieldLightRailRouteNumber.visible) {
				graphicsHolder.drawText(lightRailRouteNumberText, SQUARE_SIZE, SQUARE_SIZE * 6 + TEXT_PADDING, ARGB_WHITE, false, MAX_LIGHT_GLOWING);
			}

			super.render(graphicsHolder, mouseX, mouseY, delta);
		} catch (Exception e) {
			Init.logException(e);
		}
	}

	@Override
	protected void saveData() {
		super.saveData();

		data.setRouteNumber(textFieldLightRailRouteNumber.getText2());
		data.setHidden(buttonIsRouteHidden.isChecked2());
		// TODO data.disableNextStationAnnouncements = buttonDisableNextStationAnnouncements.isChecked2();

		if (isCircular) {
			data.setCircularState(buttonIsClockwiseRoute.isChecked2() ? Route.CircularState.CLOCKWISE : buttonIsAntiClockwiseRoute.isChecked2() ? Route.CircularState.ANTICLOCKWISE : Route.CircularState.NONE);
		} else {
			data.setCircularState(Route.CircularState.NONE);
		}

		InitClient.REGISTRY_CLIENT.sendPacketToServer(PacketData.fromRoutes(IntegrationServlet.Operation.UPDATE, ObjectSet.of(data)));
	}

	private void setRouteType(Route route, RouteType newRouteType) {
		route.setRouteType(newRouteType);
		buttonRouteType.setMessage2(new Text(TextHelper.translatable(route.getRouteTypeKey()).data));
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
}
