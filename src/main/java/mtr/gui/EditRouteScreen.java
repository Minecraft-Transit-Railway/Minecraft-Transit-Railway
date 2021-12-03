package mtr.gui;

import mtr.data.IGui;
import mtr.data.Route;
import mtr.data.RouteType;
import mtr.data.Station;
import mtr.packet.IPacket;
import mtr.packet.PacketTrainDataGuiClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class EditRouteScreen extends EditNameColorScreenBase<Route> implements IGui, IPacket {

	private RouteType routeType;

	private final Text lightRailRouteNumberText = new TranslatableText("gui.mtr.light_rail_route_number");

	private final WidgetBetterTextField textFieldLightRailRouteNumber;
	private final ButtonWidget buttonRouteType;
	private final WidgetBetterCheckbox buttonIsLightRailRoute;
	private final WidgetBetterCheckbox buttonIsClockwiseRoute;
	private final WidgetBetterCheckbox buttonIsAntiClockwiseRoute;

	private final boolean isCircular;

	private static final int CHECKBOX_WIDTH = 160;
	private static final int LIGHT_RAIL_ROUTE_NUMBER_MAX_LENGTH = 20;

	public EditRouteScreen(Route route, DashboardScreen dashboardScreen) {
		super(route, dashboardScreen, "gui.mtr.route_name", "gui.mtr.route_color");

		textFieldLightRailRouteNumber = new WidgetBetterTextField(null, "");
		buttonRouteType = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new TranslatableText("gui.mtr.add_value"), button -> setRouteTypeText(routeType.next()));
		buttonIsLightRailRoute = new WidgetBetterCheckbox(0, 0, 0, SQUARE_SIZE, new TranslatableText("gui.mtr.is_light_rail_route"), this::setIsLightRailRoute);
		buttonIsClockwiseRoute = new WidgetBetterCheckbox(0, 0, 0, SQUARE_SIZE, new TranslatableText("gui.mtr.is_clockwise_route"), this::setIsClockwise);
		buttonIsAntiClockwiseRoute = new WidgetBetterCheckbox(0, 0, 0, SQUARE_SIZE, new TranslatableText("gui.mtr.is_anticlockwise_route"), this::setIsAntiClockwise);

		if (route.platformIds.size() > 0) {
			final Station firstStation = ClientData.DATA_CACHE.platformIdToStation.get(route.platformIds.get(0));
			final Station lastStation = ClientData.DATA_CACHE.platformIdToStation.get(route.platformIds.get(route.platformIds.size() - 1));
			isCircular = firstStation != null && lastStation != null && firstStation.id == lastStation.id;
		} else {
			isCircular = false;
		}
	}

	@Override
	protected void init() {
		setPositionsAndInit(SQUARE_SIZE, width / 4 * 3 - SQUARE_SIZE, width - SQUARE_SIZE);

		IDrawing.setPositionAndWidth(buttonRouteType, SQUARE_SIZE, SQUARE_SIZE * 3, CHECKBOX_WIDTH);
		setRouteTypeText(data.routeType);

		IDrawing.setPositionAndWidth(buttonIsLightRailRoute, SQUARE_SIZE, SQUARE_SIZE * 4, CHECKBOX_WIDTH);
		IDrawing.setPositionAndWidth(textFieldLightRailRouteNumber, SQUARE_SIZE + TEXT_FIELD_PADDING / 2, SQUARE_SIZE * 6 + TEXT_FIELD_PADDING / 2, CHECKBOX_WIDTH - TEXT_FIELD_PADDING);
		textFieldLightRailRouteNumber.setText(data.lightRailRouteNumber);
		textFieldLightRailRouteNumber.setMaxLength(LIGHT_RAIL_ROUTE_NUMBER_MAX_LENGTH);

		IDrawing.setPositionAndWidth(buttonIsClockwiseRoute, SQUARE_SIZE, SQUARE_SIZE * 7 + TEXT_FIELD_PADDING, CHECKBOX_WIDTH);
		IDrawing.setPositionAndWidth(buttonIsAntiClockwiseRoute, SQUARE_SIZE, SQUARE_SIZE * 8 + TEXT_FIELD_PADDING, CHECKBOX_WIDTH);

		addDrawableChild(buttonRouteType);
		addDrawableChild(textFieldLightRailRouteNumber);
		addDrawableChild(buttonIsLightRailRoute);
		if (isCircular) {
			addDrawableChild(buttonIsClockwiseRoute);
			addDrawableChild(buttonIsAntiClockwiseRoute);
		}

		setIsLightRailRoute(data.isLightRailRoute);
		setIsClockwise(data.circularState == Route.CircularState.CLOCKWISE);
		setIsAntiClockwise(data.circularState == Route.CircularState.ANTICLOCKWISE);
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		try {
			renderBackground(matrices);
			renderTextFields(matrices);

			if (textFieldLightRailRouteNumber.visible) {
				drawTextWithShadow(matrices, textRenderer, lightRailRouteNumberText, SQUARE_SIZE, SQUARE_SIZE * 5 + TEXT_PADDING, ARGB_WHITE);
			}

			super.render(matrices, mouseX, mouseY, delta);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClose() {
		super.onClose();

		data.routeType = routeType;
		data.isLightRailRoute = buttonIsLightRailRoute.isChecked();
		data.lightRailRouteNumber = textFieldLightRailRouteNumber.getText();

		if (isCircular) {
			data.circularState = buttonIsClockwiseRoute.isChecked() ? Route.CircularState.CLOCKWISE : buttonIsAntiClockwiseRoute.isChecked() ? Route.CircularState.ANTICLOCKWISE : Route.CircularState.NONE;
		} else {
			data.circularState = Route.CircularState.NONE;
		}

		data.setExtraData(packet -> PacketTrainDataGuiClient.sendUpdate(PACKET_UPDATE_ROUTE, packet));
	}

	private void setRouteTypeText(RouteType newRouteType) {
		routeType = newRouteType;
		buttonRouteType.setMessage(new TranslatableText(routeType.key));
	}

	private void setIsLightRailRoute(boolean isLightRailRoute) {
		buttonIsLightRailRoute.setChecked(isLightRailRoute);
		textFieldLightRailRouteNumber.visible = isLightRailRoute;
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
