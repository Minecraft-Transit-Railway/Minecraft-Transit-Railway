package mtr.gui;

import mtr.data.IGui;
import mtr.data.Route;
import mtr.packet.IPacket;
import mtr.packet.PacketTrainDataGuiClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class EditRouteScreen extends EditNameColorScreenBase<Route> implements IGui, IPacket {

	private final Text lightRailRouteNumberText = new TranslatableText("gui.mtr.light_rail_route_number");

	private final TextFieldWidget textFieldLightRailRouteNumber;
	private final WidgetBetterCheckbox buttonIsLightRailRoute;

	private static final int LIGHT_RAIL_WIDTH = 160;
	private static final int LIGHT_RAIL_ROUTE_NUMBER_MAX_LENGTH = 20;

	public EditRouteScreen(Route route, DashboardScreen dashboardScreen) {
		super(route, dashboardScreen, "gui.mtr.route_name", "gui.mtr.route_color");

		textRenderer = MinecraftClient.getInstance().textRenderer;
		textFieldLightRailRouteNumber = new TextFieldWidget(textRenderer, 0, 0, 0, SQUARE_SIZE, new LiteralText(""));
		buttonIsLightRailRoute = new WidgetBetterCheckbox(0, 0, 0, SQUARE_SIZE, new TranslatableText("gui.mtr.is_light_rail_route"), this::setIsLightRailRoute);

	}

	@Override
	protected void init() {
		setPositionsAndInit(SQUARE_SIZE, width / 4 * 3 - SQUARE_SIZE, width - SQUARE_SIZE);

		IDrawing.setPositionAndWidth(buttonIsLightRailRoute, SQUARE_SIZE, SQUARE_SIZE * 3, LIGHT_RAIL_WIDTH);
		IDrawing.setPositionAndWidth(textFieldLightRailRouteNumber, SQUARE_SIZE + TEXT_FIELD_PADDING / 2, SQUARE_SIZE * 5 + TEXT_FIELD_PADDING / 2, LIGHT_RAIL_WIDTH - TEXT_FIELD_PADDING);
		textFieldLightRailRouteNumber.setText(data.lightRailRouteNumber);
		textFieldLightRailRouteNumber.setMaxLength(LIGHT_RAIL_ROUTE_NUMBER_MAX_LENGTH);

		addDrawableChild(textFieldLightRailRouteNumber);
		addDrawableChild(buttonIsLightRailRoute);

		setIsLightRailRoute(data.isLightRailRoute);
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		try {
			renderBackground(matrices);
			renderTextFields(matrices, mouseX, mouseY, delta);

			textFieldLightRailRouteNumber.render(matrices, mouseX, mouseY, delta);
			if (textFieldLightRailRouteNumber.visible) {
				drawTextWithShadow(matrices, textRenderer, lightRailRouteNumberText, SQUARE_SIZE, SQUARE_SIZE * 4 + TEXT_PADDING, ARGB_WHITE);
			}

			super.render(matrices, mouseX, mouseY, delta);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClose() {
		super.onClose();
		data.isLightRailRoute = buttonIsLightRailRoute.isChecked();
		data.lightRailRouteNumber = textFieldLightRailRouteNumber.getText();
		data.setLightRailRoute(packet -> PacketTrainDataGuiClient.sendUpdate(PACKET_UPDATE_ROUTE, packet));
	}

	private void setIsLightRailRoute(boolean isLightRailRoute) {
		buttonIsLightRailRoute.setChecked(isLightRailRoute);
		textFieldLightRailRouteNumber.visible = isLightRailRoute;
	}
}
