package mtr.screen;

import mtr.client.ClientData;
import mtr.client.IDrawing;
import mtr.data.*;
import mtr.mappings.Text;
import mtr.mappings.UtilitiesClient;
import mtr.packet.IPacket;
import mtr.packet.PacketTrainDataGuiClient;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

import java.util.Locale;

public class EditRouteScreen extends EditNameColorScreenBase<Route> implements IGui, IPacket {

	private RouteType routeType;

	private final Component lightRailRouteNumberText = Text.translatable("gui.mtr.light_rail_route_number");

	private final WidgetBetterTextField textFieldLightRailRouteNumber;
	private final Button buttonRouteType;
	private final WidgetBetterCheckbox buttonIsLightRailRoute;
	private final WidgetBetterCheckbox buttonIsRouteHidden;
	private final WidgetBetterCheckbox buttonDisableNextStationAnnouncements;
	private final WidgetBetterCheckbox buttonIsClockwiseRoute;
	private final WidgetBetterCheckbox buttonIsAntiClockwiseRoute;

	private final boolean isCircular;

	private static final int CHECKBOX_WIDTH = 160;

	public EditRouteScreen(Route route, DashboardScreen dashboardScreen) {
		super(route, dashboardScreen, "gui.mtr.route_name", "gui.mtr.route_color");

		textFieldLightRailRouteNumber = new WidgetBetterTextField("");
		buttonRouteType = UtilitiesClient.newButton(Text.translatable("gui.mtr.add_value"), button -> setRouteTypeText(data.transportMode, routeType.next()));
		buttonIsLightRailRoute = new WidgetBetterCheckbox(0, 0, 0, SQUARE_SIZE, Text.translatable("gui.mtr.is_light_rail_route"), this::setIsLightRailRoute);
		buttonIsRouteHidden = new WidgetBetterCheckbox(0, 0, 0, SQUARE_SIZE, Text.translatable("gui.mtr.is_route_hidden"), this::setIsRouteHidden);
		buttonDisableNextStationAnnouncements = new WidgetBetterCheckbox(0, 0, 0, SQUARE_SIZE, Text.translatable("gui.mtr.disable_next_station_announcements"), this::setDisableNextStationAnnouncements);
		buttonIsClockwiseRoute = new WidgetBetterCheckbox(0, 0, 0, SQUARE_SIZE, Text.translatable("gui.mtr.is_clockwise_route"), this::setIsClockwise);
		buttonIsAntiClockwiseRoute = new WidgetBetterCheckbox(0, 0, 0, SQUARE_SIZE, Text.translatable("gui.mtr.is_anticlockwise_route"), this::setIsAntiClockwise);

		if (route.platformIds.size() > 0) {
			final Station firstStation = ClientData.DATA_CACHE.platformIdToStation.get(route.getFirstPlatformId());
			final Station lastStation = ClientData.DATA_CACHE.platformIdToStation.get(route.getLastPlatformId());
			isCircular = firstStation != null && lastStation != null && firstStation.id == lastStation.id;
		} else {
			isCircular = false;
		}
	}

	@Override
	protected void init() {
		setPositionsAndInit(SQUARE_SIZE, width / 4 * 3 - SQUARE_SIZE, width - SQUARE_SIZE);

		IDrawing.setPositionAndWidth(buttonRouteType, SQUARE_SIZE, SQUARE_SIZE * 3, CHECKBOX_WIDTH);
		setRouteTypeText(data.transportMode, data.routeType);

		IDrawing.setPositionAndWidth(buttonIsRouteHidden, SQUARE_SIZE, SQUARE_SIZE * 4, CHECKBOX_WIDTH);
		IDrawing.setPositionAndWidth(buttonDisableNextStationAnnouncements, SQUARE_SIZE, SQUARE_SIZE * 5, CHECKBOX_WIDTH);
		IDrawing.setPositionAndWidth(buttonIsLightRailRoute, SQUARE_SIZE, SQUARE_SIZE * 6, CHECKBOX_WIDTH);
		IDrawing.setPositionAndWidth(textFieldLightRailRouteNumber, SQUARE_SIZE + TEXT_FIELD_PADDING / 2, SQUARE_SIZE * 8 + TEXT_FIELD_PADDING / 2, CHECKBOX_WIDTH - TEXT_FIELD_PADDING);
		textFieldLightRailRouteNumber.setValue(data.lightRailRouteNumber);

		IDrawing.setPositionAndWidth(buttonIsClockwiseRoute, SQUARE_SIZE, SQUARE_SIZE * 9 + TEXT_FIELD_PADDING, CHECKBOX_WIDTH);
		IDrawing.setPositionAndWidth(buttonIsAntiClockwiseRoute, SQUARE_SIZE, SQUARE_SIZE * 10 + TEXT_FIELD_PADDING, CHECKBOX_WIDTH);

		if (data.transportMode.hasRouteTypeVariation) {
			addDrawableChild(buttonRouteType);
		}
		addDrawableChild(textFieldLightRailRouteNumber);
		addDrawableChild(buttonIsLightRailRoute);
		addDrawableChild(buttonIsRouteHidden);
		addDrawableChild(buttonDisableNextStationAnnouncements);
		if (isCircular) {
			addDrawableChild(buttonIsClockwiseRoute);
			addDrawableChild(buttonIsAntiClockwiseRoute);
		}

		setIsLightRailRoute(data.isLightRailRoute);
		setIsRouteHidden(data.isHidden);
		setDisableNextStationAnnouncements(data.disableNextStationAnnouncements);
		setIsClockwise(data.circularState == Route.CircularState.CLOCKWISE);
		setIsAntiClockwise(data.circularState == Route.CircularState.ANTICLOCKWISE);
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
		try {
			renderBackground(guiGraphics);
			renderTextFields(guiGraphics);

			if (textFieldLightRailRouteNumber.visible) {
				guiGraphics.drawString(font, lightRailRouteNumberText, SQUARE_SIZE, SQUARE_SIZE * 7 + TEXT_PADDING, ARGB_WHITE);
			}

			super.render(guiGraphics, mouseX, mouseY, delta);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void saveData() {
		super.saveData();

		data.routeType = routeType;
		data.isLightRailRoute = buttonIsLightRailRoute.selected();
		data.lightRailRouteNumber = textFieldLightRailRouteNumber.getValue();
		data.isHidden = buttonIsRouteHidden.selected();
		data.disableNextStationAnnouncements = buttonDisableNextStationAnnouncements.selected();

		if (isCircular) {
			data.circularState = buttonIsClockwiseRoute.selected() ? Route.CircularState.CLOCKWISE : buttonIsAntiClockwiseRoute.selected() ? Route.CircularState.ANTICLOCKWISE : Route.CircularState.NONE;
		} else {
			data.circularState = Route.CircularState.NONE;
		}

		data.setExtraData(packet -> PacketTrainDataGuiClient.sendUpdate(PACKET_UPDATE_ROUTE, packet));
	}

	private void setRouteTypeText(TransportMode transportMode, RouteType newRouteType) {
		routeType = newRouteType;
		buttonRouteType.setMessage(Text.translatable(String.format("gui.mtr.route_type_%s_%s", transportMode, routeType).toLowerCase(Locale.ENGLISH)));
	}

	private void setIsLightRailRoute(boolean isLightRailRoute) {
		buttonIsLightRailRoute.setChecked(isLightRailRoute);
		textFieldLightRailRouteNumber.visible = isLightRailRoute;
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
