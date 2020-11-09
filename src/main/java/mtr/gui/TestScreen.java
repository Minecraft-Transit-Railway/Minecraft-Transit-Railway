package mtr.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import mtr.data.NamedColoredBase;
import mtr.data.Route;
import mtr.data.Station;
import mtr.packet.PacketTrainDataGuiClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TestScreen extends Screen implements IGui {

	private int selectedTab;

	private final WidgetMapTest widgetMap;

	private final ButtonWidget buttonTabStations;
	private final ButtonWidget buttonTabRoutes;
	private final ButtonWidget buttonTabTrains;
	private final ButtonWidget buttonAddStation;
	private final ButtonWidget buttonAddRoute;
	private final ButtonWidget buttonDoneEditingStation;
	private final ButtonWidget buttonDoneEditingRoute;
	private final ButtonWidget buttonCancel;
	private final ButtonWidget buttonZoomIn;
	private final ButtonWidget buttonZoomOut;

	private final TextFieldWidget textFieldName;
	private final TextFieldWidget textFieldColor;

	private static final int LEFT_PANEL_WIDTH = 144;
	private static final int MAX_STATION_LENGTH = 128;
	private static final int MAX_COLOR_LENGTH = 6;

	public TestScreen() {
		super(new LiteralText(""));

		final MinecraftClient minecraftClient = MinecraftClient.getInstance();
		final Window window = minecraftClient.getWindow();
		final int windowWidth = window.getScaledWidth();
		final int windowHeight = window.getScaledHeight();

		final int tabCount = 3;
		final int middleWidth = windowWidth - LEFT_PANEL_WIDTH - SQUARE_SIZE * 2;
		final int addStationWidth = (int) Math.floor(middleWidth / 2D);
		final int addRouteWidth = (int) Math.ceil(middleWidth / 2D);
		final int nameWidth = middleWidth - SQUARE_SIZE * 3;

		widgetMap = new WidgetMapTest(LEFT_PANEL_WIDTH, 0, windowWidth - LEFT_PANEL_WIDTH, windowHeight, this::onMapChange);

		textFieldName = new TextFieldWidget(minecraftClient.textRenderer, LEFT_PANEL_WIDTH + TEXT_FIELD_PADDING / 2, windowHeight - SQUARE_SIZE * 2 - TEXT_FIELD_PADDING / 2, nameWidth - TEXT_FIELD_PADDING, SQUARE_SIZE, new TranslatableText("gui.mtr.name"));
		textFieldColor = new TextFieldWidget(minecraftClient.textRenderer, LEFT_PANEL_WIDTH + nameWidth + TEXT_FIELD_PADDING / 2, windowHeight - SQUARE_SIZE * 2 - TEXT_FIELD_PADDING / 2, SQUARE_SIZE * 3 - TEXT_FIELD_PADDING, SQUARE_SIZE, new TranslatableText("gui.mtr.color"));

		buttonTabStations = new ButtonWidget(0, 0, LEFT_PANEL_WIDTH / tabCount, SQUARE_SIZE, new TranslatableText("gui.mtr.stations"), button -> onSelectTab(0));
		buttonTabRoutes = new ButtonWidget(LEFT_PANEL_WIDTH / tabCount, 0, LEFT_PANEL_WIDTH / tabCount, SQUARE_SIZE, new TranslatableText("gui.mtr.routes"), button -> onSelectTab(1));
		buttonTabTrains = new ButtonWidget(2 * LEFT_PANEL_WIDTH / tabCount, 0, LEFT_PANEL_WIDTH / tabCount, SQUARE_SIZE, new TranslatableText("gui.mtr.trains"), button -> onSelectTab(2));
		buttonAddStation = new ButtonWidget(LEFT_PANEL_WIDTH, windowHeight - SQUARE_SIZE, addStationWidth, SQUARE_SIZE, new TranslatableText("gui.mtr.add_station"), button -> startEditingStation(new Station()));
		buttonAddRoute = new ButtonWidget(LEFT_PANEL_WIDTH + addStationWidth, windowHeight - SQUARE_SIZE, addRouteWidth, SQUARE_SIZE, new TranslatableText("gui.mtr.add_route"), button -> startEditingRoute(new Route()));
		buttonDoneEditingStation = new ButtonWidget(LEFT_PANEL_WIDTH, windowHeight - SQUARE_SIZE, addStationWidth, SQUARE_SIZE, new TranslatableText("gui.done"), button -> {
			widgetMap.onDoneEditingStation(textFieldName.getText(), colorStringToInt(textFieldColor.getText()));
			PacketTrainDataGuiClient.sendStationsAndRoutesC2S(ScreenBase.GuiBase.stations, ScreenBase.GuiBase.routes);
			stopEditing();
		});
		buttonDoneEditingRoute = new ButtonWidget(LEFT_PANEL_WIDTH, windowHeight - SQUARE_SIZE, addStationWidth, SQUARE_SIZE, new TranslatableText("gui.done"), button -> {
			widgetMap.onDoneEditingRoute(textFieldName.getText(), colorStringToInt(textFieldColor.getText()));
			PacketTrainDataGuiClient.sendStationsAndRoutesC2S(ScreenBase.GuiBase.stations, ScreenBase.GuiBase.routes);
			stopEditing();
		});
		buttonCancel = new ButtonWidget(LEFT_PANEL_WIDTH + addStationWidth, windowHeight - SQUARE_SIZE, addRouteWidth, SQUARE_SIZE, new TranslatableText("gui.cancel"), button -> stopEditing());
		buttonZoomIn = new ButtonWidget(LEFT_PANEL_WIDTH + middleWidth, windowHeight - SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE, new LiteralText("+"), button -> widgetMap.scale(1));
		buttonZoomOut = new ButtonWidget(LEFT_PANEL_WIDTH + middleWidth + SQUARE_SIZE, windowHeight - SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE, new LiteralText("-"), button -> widgetMap.scale(-1));

		onSelectTab(0);
	}

	@Override
	protected void init() {
		buttonDoneEditingRoute.visible = false;
		buttonDoneEditingStation.visible = false;
		buttonCancel.visible = false;

		textFieldName.setVisible(false);
		textFieldName.setMaxLength(MAX_STATION_LENGTH);
		textFieldName.setChangedListener(text -> textFieldName.setSuggestion(text.isEmpty() ? new TranslatableText("gui.mtr.name").getString() : ""));
		textFieldColor.setVisible(false);
		textFieldColor.setMaxLength(MAX_COLOR_LENGTH);
		textFieldColor.setChangedListener(text -> {
			final String newText = text.toUpperCase().replaceAll("[^0-9A-F]", "");
			if (!newText.equals(text)) {
				textFieldColor.setText(newText);
			}
			textFieldColor.setSuggestion(newText.isEmpty() ? new TranslatableText("gui.mtr.color").getString() : "");
		});

		addChild(widgetMap);

		addButton(buttonTabStations);
		addButton(buttonTabRoutes);
		addButton(buttonTabTrains);
		addButton(buttonAddStation);
		addButton(buttonAddRoute);
		addButton(buttonDoneEditingStation);
		addButton(buttonDoneEditingRoute);
		addButton(buttonCancel);
		addButton(buttonZoomIn);
		addButton(buttonZoomOut);

		addChild(textFieldName);
		addChild(textFieldColor);
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		widgetMap.render(matrices, mouseX, mouseY, delta);
		DrawableHelper.fill(matrices, 0, 0, LEFT_PANEL_WIDTH, height, ARGB_BLACK);
		super.render(matrices, mouseX, mouseY, delta);
		textFieldName.render(matrices, mouseX, mouseY, delta);
		textFieldColor.render(matrices, mouseX, mouseY, delta);

		for (int i = 0; i < height / SQUARE_SIZE - 1; i++) {
			final List<NamedColoredBase> dataSorted;
			switch (selectedTab) {
				default:
					dataSorted = new ArrayList<>(ScreenBase.GuiBase.stations);
					break;
				case 1:
					dataSorted = new ArrayList<>(ScreenBase.GuiBase.routes);
					break;
				case 2:
					dataSorted = new ArrayList<>(ScreenBase.GuiBase.trains);
					break;
			}
			Collections.sort(dataSorted);
			if (i < dataSorted.size()) {
				final int y = SQUARE_SIZE * (i + 1) + TEXT_PADDING;
				final NamedColoredBase data = dataSorted.get(i);

				Tessellator tessellator = Tessellator.getInstance();
				BufferBuilder buffer = tessellator.getBuffer();
				RenderSystem.enableBlend();
				RenderSystem.disableTexture();
				RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);
				buffer.begin(7, VertexFormats.POSITION_COLOR);
				IGui.drawRectangle(buffer, TEXT_PADDING, y, TEXT_PADDING + TEXT_HEIGHT, y + TEXT_HEIGHT, ARGB_BLACK + data.color);
				tessellator.draw();
				RenderSystem.enableTexture();
				RenderSystem.disableBlend();

				DrawableHelper.drawStringWithShadow(matrices, textRenderer, IGui.formatStationName(data.name), TEXT_PADDING * 2 + TEXT_HEIGHT, y, ARGB_WHITE);
			}
		}
	}

	@Override
	public void tick() {
		textFieldName.tick();
		textFieldColor.tick();
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	private void onSelectTab(int tab) {
		selectedTab = tab;
		buttonTabStations.active = tab != 0;
		buttonTabRoutes.active = tab != 1;
		buttonTabTrains.active = tab != 2;
	}

	private void onMapChange() {
		buttonDoneEditingStation.active = widgetMap.stationDrawn();
	}

	private void startEditingStation(Station editingStation) {
		textFieldName.setText(editingStation.name);
		textFieldColor.setText(colorIntToString(editingStation.color));

		buttonAddStation.visible = false;
		buttonAddRoute.visible = false;
		buttonDoneEditingStation.visible = true;
		buttonDoneEditingRoute.visible = false;
		buttonCancel.visible = true;
		textFieldName.visible = true;
		textFieldColor.visible = true;
		widgetMap.startEditingStation(editingStation);
	}

	private void startEditingRoute(Route editingRoute) {
		textFieldName.setText(editingRoute.name);
		textFieldColor.setText(colorIntToString(editingRoute.color));

		buttonAddStation.visible = false;
		buttonAddRoute.visible = false;
		buttonDoneEditingStation.visible = false;
		buttonDoneEditingRoute.visible = true;
		buttonCancel.visible = true;
		textFieldName.visible = true;
		textFieldColor.visible = true;
		widgetMap.startEditingRoute(editingRoute);
	}

	private void stopEditing() {
		buttonAddStation.visible = true;
		buttonAddRoute.visible = true;
		buttonDoneEditingStation.visible = false;
		buttonDoneEditingRoute.visible = false;
		buttonCancel.visible = false;
		textFieldName.visible = false;
		textFieldColor.visible = false;
		widgetMap.stopEditing();
	}

	private static int colorStringToInt(String string) {
		try {
			return Integer.parseInt(string, 16);
		} catch (Exception ignored) {
			return 0;
		}
	}

	private static String colorIntToString(int color) {
		return StringUtils.leftPad(Integer.toHexString(color == 0 ? (new Random()).nextInt(RGB_WHITE + 1) : color).toUpperCase(), 6, "0");
	}
}
