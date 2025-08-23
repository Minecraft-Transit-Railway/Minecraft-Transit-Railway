package org.mtr.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.mtr.MTR;
import org.mtr.client.IDrawing;
import org.mtr.client.MinecraftClientData;
import org.mtr.core.data.Route;
import org.mtr.core.data.RouteType;
import org.mtr.core.data.Station;
import org.mtr.core.operation.UpdateDataRequest;
import org.mtr.core.tool.Utilities;
import org.mtr.data.IGui;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.packet.PacketCheckRouteIdHasDisabledAnnouncements;
import org.mtr.packet.PacketSetRouteIdHasDisabledAnnouncements;
import org.mtr.packet.PacketUpdateData;
import org.mtr.registry.RegistryClient;
import org.mtr.widget.BetterTextFieldWidget;

public class EditRouteScreen extends EditNameColorScreenBase<Route> implements IGui {

	private final MutableText lightRailRouteNumberText = TranslationProvider.GUI_MTR_LIGHT_RAIL_ROUTE_NUMBER.getMutableText();

	private final BetterTextFieldWidget textFieldLightRailRouteNumber;
	private final ButtonWidget buttonRouteType;
	private final CheckboxWidget buttonIsRouteHidden;
	private final CheckboxWidget buttonDisableNextStationAnnouncements;
	private final CheckboxWidget buttonIsClockwiseRoute;
	private final CheckboxWidget buttonIsAntiClockwiseRoute;

	private final boolean isCircular;

	private static final int CHECKBOX_WIDTH = 160;

	public EditRouteScreen(Route route, Screen previousScreen) {
		super(route, TranslationProvider.GUI_MTR_ROUTE_NAME, TranslationProvider.GUI_MTR_ROUTE_COLOR, previousScreen);

		textFieldLightRailRouteNumber = new BetterTextFieldWidget(256, TextCase.DEFAULT, null, null, 100, text -> {
		});
		buttonRouteType = ButtonWidget.builder(TranslationProvider.GUI_MTR_ADD_VALUE.getMutableText(), button -> setRouteType(route, route.getRouteType().next())).build();
		buttonIsRouteHidden = CheckboxWidget.builder(TranslationProvider.GUI_MTR_IS_ROUTE_HIDDEN.getText(), textRenderer).callback((checkboxWidget, checked) -> setIsRouteHidden(checked)).build();
		buttonDisableNextStationAnnouncements = CheckboxWidget.builder(TranslationProvider.GUI_MTR_DISABLE_NEXT_STATION_ANNOUNCEMENTS.getText(), textRenderer).callback((checkboxWidget, checked) -> setDisableNextStationAnnouncements(checked)).build();
		buttonIsClockwiseRoute = CheckboxWidget.builder(TranslationProvider.GUI_MTR_IS_CLOCKWISE_ROUTE.getText(), textRenderer).callback((checkboxWidget, checked) -> setIsClockwise(checked)).build();
		buttonIsAntiClockwiseRoute = CheckboxWidget.builder(TranslationProvider.GUI_MTR_IS_ANTICLOCKWISE_ROUTE.getText(), textRenderer).callback((checkboxWidget, checked) -> setIsAntiClockwise(checked)).build();

		if (!route.getRoutePlatforms().isEmpty()) {
			final Station firstStation = Utilities.getElement(route.getRoutePlatforms(), 0).platform.area;
			final Station lastStation = Utilities.getElement(route.getRoutePlatforms(), -1).platform.area;
			isCircular = firstStation != null && lastStation != null && firstStation.getId() == lastStation.getId();
		} else {
			isCircular = false;
		}
	}

	@Override
	protected void init() {
		setPositionsAndInit(SQUARE_SIZE, width / 4 * 3 - SQUARE_SIZE, width - SQUARE_SIZE);

		IDrawing.setPositionAndWidth(buttonRouteType, SQUARE_SIZE, SQUARE_SIZE * 3, CHECKBOX_WIDTH);
		setRouteType(data, data.getRouteType());

		IDrawing.setPositionAndWidth(buttonIsRouteHidden, SQUARE_SIZE, SQUARE_SIZE * 4, CHECKBOX_WIDTH);
		IDrawing.setPositionAndWidth(buttonDisableNextStationAnnouncements, SQUARE_SIZE, SQUARE_SIZE * 5, CHECKBOX_WIDTH);
		IDrawing.setPositionAndWidth(textFieldLightRailRouteNumber, SQUARE_SIZE + TEXT_FIELD_PADDING / 2, SQUARE_SIZE * 7 + TEXT_FIELD_PADDING / 2, CHECKBOX_WIDTH - TEXT_FIELD_PADDING);
		textFieldLightRailRouteNumber.setText(data.getRouteNumber());

		IDrawing.setPositionAndWidth(buttonIsClockwiseRoute, SQUARE_SIZE, SQUARE_SIZE * 8 + TEXT_FIELD_PADDING, CHECKBOX_WIDTH);
		IDrawing.setPositionAndWidth(buttonIsAntiClockwiseRoute, SQUARE_SIZE, SQUARE_SIZE * 9 + TEXT_FIELD_PADDING, CHECKBOX_WIDTH);

		if (data.getTransportMode().hasRouteTypeVariation) {
			addDrawableChild(buttonRouteType);
		}
		addDrawableChild(textFieldLightRailRouteNumber);
		addDrawableChild(buttonIsRouteHidden);
		addDrawableChild(buttonDisableNextStationAnnouncements);
		if (isCircular) {
			addDrawableChild(buttonIsClockwiseRoute);
			addDrawableChild(buttonIsAntiClockwiseRoute);
		}

		setIsRouteHidden(data.getHidden());
		RegistryClient.sendPacketToServer(new PacketCheckRouteIdHasDisabledAnnouncements(data.getId(), this::setDisableNextStationAnnouncements));
		setIsClockwise(data.getCircularState() == Route.CircularState.CLOCKWISE);
		setIsAntiClockwise(data.getCircularState() == Route.CircularState.ANTICLOCKWISE);
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		try {
			renderBackground(context, mouseX, mouseY, delta);
			renderTextFields(context);

			if (textFieldLightRailRouteNumber.visible) {
				context.drawText(textRenderer, lightRailRouteNumberText, SQUARE_SIZE, SQUARE_SIZE * 6 + TEXT_PADDING, ARGB_WHITE, false);
			}

			super.render(context, mouseX, mouseY, delta);
		} catch (Exception e) {
			MTR.LOGGER.error("", e);
		}
	}

	@Override
	protected void saveData() {
		super.saveData();

		data.setRouteNumber(textFieldLightRailRouteNumber.getText());
		data.setHidden(buttonIsRouteHidden.isChecked());
		final boolean routeIdHasDisabledAnnouncements = buttonDisableNextStationAnnouncements.isChecked();
		MinecraftClientData.getInstance().setRouteIdHasDisabledAnnouncements(data.getId(), routeIdHasDisabledAnnouncements);

		if (isCircular) {
			data.setCircularState(buttonIsClockwiseRoute.isChecked() ? Route.CircularState.CLOCKWISE : buttonIsAntiClockwiseRoute.isChecked() ? Route.CircularState.ANTICLOCKWISE : Route.CircularState.NONE);
		} else {
			data.setCircularState(Route.CircularState.NONE);
		}

		RegistryClient.sendPacketToServer(new PacketUpdateData(new UpdateDataRequest(MinecraftClientData.getDashboardInstance()).addRoute(data)));
		RegistryClient.sendPacketToServer(new PacketSetRouteIdHasDisabledAnnouncements(data.getId(), routeIdHasDisabledAnnouncements));
	}

	private void setRouteType(Route route, RouteType newRouteType) {
		route.setRouteType(newRouteType);
		buttonRouteType.setMessage(getRouteTypeText(route));
	}

	private void setIsRouteHidden(boolean isRouteHidden) {
		IGui.setChecked(buttonIsRouteHidden, isRouteHidden);
	}

	private void setDisableNextStationAnnouncements(boolean hasNextStationAnnouncements) {
		IGui.setChecked(buttonDisableNextStationAnnouncements, hasNextStationAnnouncements);
	}

	private void setIsClockwise(boolean isClockwise) {
		IGui.setChecked(buttonIsClockwiseRoute, isClockwise);
		if (isClockwise) {
			IGui.setChecked(buttonIsAntiClockwiseRoute, false);
		}
	}

	private void setIsAntiClockwise(boolean isAntiClockwise) {
		IGui.setChecked(buttonIsAntiClockwiseRoute, isAntiClockwise);
		if (isAntiClockwise) {
			IGui.setChecked(buttonIsClockwiseRoute, false);
		}
	}

	private static Text getRouteTypeText(Route route) {
		final RouteType routeType = route.getRouteType();
		return switch (route.getTransportMode()) {
			case TRAIN -> (routeType == RouteType.LIGHT_RAIL ? TranslationProvider.GUI_MTR_ROUTE_TYPE_TRAIN_LIGHT_RAIL : routeType == RouteType.HIGH_SPEED ? TranslationProvider.GUI_MTR_ROUTE_TYPE_TRAIN_HIGH_SPEED : TranslationProvider.GUI_MTR_ROUTE_TYPE_TRAIN_NORMAL).getText();
			case BOAT -> (routeType == RouteType.LIGHT_RAIL ? TranslationProvider.GUI_MTR_ROUTE_TYPE_BOAT_LIGHT_RAIL : routeType == RouteType.HIGH_SPEED ? TranslationProvider.GUI_MTR_ROUTE_TYPE_BOAT_HIGH_SPEED : TranslationProvider.GUI_MTR_ROUTE_TYPE_BOAT_NORMAL).getText();
			default -> Text.empty();
		};
	}
}
