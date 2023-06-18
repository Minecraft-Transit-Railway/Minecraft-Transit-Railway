package mtr.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import mtr.Keys;
import mtr.client.ClientData;
import mtr.client.Config;
import mtr.client.IDrawing;
import mtr.data.IGui;
import mtr.mappings.ScreenMapper;
import mtr.mappings.Text;
import net.minecraft.client.gui.screens.Screen;

public class ViewDistanceConfigScreen extends ScreenMapper implements IGui {
    private final Screen parent;
    private final WidgetShorterSlider sliderPIDSMaxDistance;
    private final WidgetShorterSlider sliderAPGMaxDistance;
    private final WidgetShorterSlider sliderClockMaxDistance;
    private final WidgetShorterSlider sliderLiftButtonMaxDistance;
    private final WidgetShorterSlider sliderLiftPanelMaxDistance;
    private final WidgetShorterSlider sliderPSDAPGDoorMaxDistance;
    private final WidgetShorterSlider sliderPSDTopMaxDistance;
    private final WidgetShorterSlider sliderRailwaySignMaxDistance;
    private final WidgetShorterSlider sliderRouteSignMaxDistance;
    private final WidgetShorterSlider sliderSignalMaxDistance;
    private final WidgetShorterSlider sliderStationNameTallMaxDistance;
    private final WidgetShorterSlider sliderStationNameTiledMaxDistance;
    private final WidgetShorterSlider sliderLiftMaxDistance;
    private static final int BUTTON_WIDTH = 60;
    private static final int BUTTON_HEIGHT = TEXT_HEIGHT + TEXT_PADDING;

    public ViewDistanceConfigScreen(Screen parent) {
        super(Text.literal(""));
        sliderPIDSMaxDistance = new WidgetShorterSlider(0, 0, Config.MAX_VIEW_DISTANCE, num -> String.format("%d", num < 4 ? 4 : num), null);
        sliderAPGMaxDistance = new WidgetShorterSlider(0, 0, Config.MAX_VIEW_DISTANCE, num -> String.format("%d", num < 4 ? 4 : num), null);
        sliderClockMaxDistance = new WidgetShorterSlider(0, 0, Config.MAX_VIEW_DISTANCE, num -> String.format("%d", num < 4 ? 4 : num), null);
        sliderLiftButtonMaxDistance = new WidgetShorterSlider(0, 0, Config.MAX_VIEW_DISTANCE, num -> String.format("%d", num < 4 ? 4 : num), null);
        sliderLiftPanelMaxDistance = new WidgetShorterSlider(0, 0, Config.MAX_VIEW_DISTANCE, num -> String.format("%d", num < 4 ? 4 : num), null);
        sliderPSDAPGDoorMaxDistance = new WidgetShorterSlider(0, 0, Config.MAX_VIEW_DISTANCE, num -> String.format("%d", num < 4 ? 4 : num), null);
        sliderPSDTopMaxDistance = new WidgetShorterSlider(0, 0, Config.MAX_VIEW_DISTANCE, num -> String.format("%d", num < 4 ? 4 : num), null);
        sliderRailwaySignMaxDistance = new WidgetShorterSlider(0, 0, Config.MAX_VIEW_DISTANCE, num -> String.format("%d", num < 4 ? 4 : num), null);
        sliderRouteSignMaxDistance = new WidgetShorterSlider(0, 0, Config.MAX_VIEW_DISTANCE, num -> String.format("%d", num < 4 ? 4 : num), null);
        sliderSignalMaxDistance = new WidgetShorterSlider(0, 0, Config.MAX_VIEW_DISTANCE, num -> String.format("%d", num < 4 ? 4 : num), null);
        sliderStationNameTallMaxDistance = new WidgetShorterSlider(0, 0, Config.MAX_VIEW_DISTANCE, num -> String.format("%d", num < 4 ? 4 : num), null);
        sliderStationNameTiledMaxDistance = new WidgetShorterSlider(0, 0, Config.MAX_VIEW_DISTANCE, num -> String.format("%d", num < 4 ? 4 : num), null);
        sliderLiftMaxDistance = new WidgetShorterSlider(0, 0, Config.MAX_VIEW_DISTANCE, num -> String.format("%d", num < 4 ? 4 : num), null);
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();
        Config.refreshProperties();
        final int offsetY = 0;
        int i = 1;
        if (!Keys.LIFTS_ONLY) {
            IDrawing.setPositionAndWidth(sliderPIDSMaxDistance, width - (SQUARE_SIZE * 10) - BUTTON_WIDTH, BUTTON_HEIGHT * (i++) + SQUARE_SIZE + offsetY, BUTTON_WIDTH - TEXT_PADDING - font.width("256") + (SQUARE_SIZE * 9));
            IDrawing.setPositionAndWidth(sliderAPGMaxDistance, width - (SQUARE_SIZE * 10) - BUTTON_WIDTH, BUTTON_HEIGHT * (i++) + SQUARE_SIZE + offsetY, BUTTON_WIDTH - TEXT_PADDING - font.width("256") + (SQUARE_SIZE * 9));
            IDrawing.setPositionAndWidth(sliderClockMaxDistance, width - (SQUARE_SIZE * 10) - BUTTON_WIDTH, BUTTON_HEIGHT * (i++) + SQUARE_SIZE + offsetY, BUTTON_WIDTH - TEXT_PADDING - font.width("256") + (SQUARE_SIZE * 9));
            IDrawing.setPositionAndWidth(sliderLiftButtonMaxDistance, width - (SQUARE_SIZE * 10) - BUTTON_WIDTH, BUTTON_HEIGHT * (i++) + SQUARE_SIZE + offsetY, BUTTON_WIDTH - TEXT_PADDING - font.width("256") + (SQUARE_SIZE * 9));
            IDrawing.setPositionAndWidth(sliderLiftPanelMaxDistance, width - (SQUARE_SIZE * 10) - BUTTON_WIDTH, BUTTON_HEIGHT * (i++) + SQUARE_SIZE + offsetY, BUTTON_WIDTH - TEXT_PADDING - font.width("256") + (SQUARE_SIZE * 9));
            IDrawing.setPositionAndWidth(sliderPSDAPGDoorMaxDistance, width - (SQUARE_SIZE * 10) - BUTTON_WIDTH, BUTTON_HEIGHT * (i++) + SQUARE_SIZE + offsetY, BUTTON_WIDTH - TEXT_PADDING - font.width("256") + (SQUARE_SIZE * 9));
            IDrawing.setPositionAndWidth(sliderPSDTopMaxDistance, width - (SQUARE_SIZE * 10) - BUTTON_WIDTH, BUTTON_HEIGHT * (i++) + SQUARE_SIZE + offsetY, BUTTON_WIDTH - TEXT_PADDING - font.width("256") + (SQUARE_SIZE * 9));
            IDrawing.setPositionAndWidth(sliderRailwaySignMaxDistance, width - (SQUARE_SIZE * 10) - BUTTON_WIDTH, BUTTON_HEIGHT * (i++) + SQUARE_SIZE + offsetY, BUTTON_WIDTH - TEXT_PADDING - font.width("256") + (SQUARE_SIZE * 9));
            IDrawing.setPositionAndWidth(sliderRouteSignMaxDistance, width - (SQUARE_SIZE * 10) - BUTTON_WIDTH, BUTTON_HEIGHT * (i++) + SQUARE_SIZE + offsetY, BUTTON_WIDTH - TEXT_PADDING - font.width("256") + (SQUARE_SIZE * 9));
            IDrawing.setPositionAndWidth(sliderSignalMaxDistance, width - (SQUARE_SIZE * 10) - BUTTON_WIDTH, BUTTON_HEIGHT * (i++) + SQUARE_SIZE + offsetY, BUTTON_WIDTH - TEXT_PADDING - font.width("256") + (SQUARE_SIZE * 9));
            IDrawing.setPositionAndWidth(sliderStationNameTallMaxDistance, width - (SQUARE_SIZE * 10) - BUTTON_WIDTH, BUTTON_HEIGHT * (i++) + SQUARE_SIZE + offsetY, BUTTON_WIDTH - TEXT_PADDING - font.width("256") + (SQUARE_SIZE * 9));
            IDrawing.setPositionAndWidth(sliderStationNameTiledMaxDistance, width - (SQUARE_SIZE * 10) - BUTTON_WIDTH, BUTTON_HEIGHT * (i++) + SQUARE_SIZE + offsetY, BUTTON_WIDTH - TEXT_PADDING - font.width("256") + (SQUARE_SIZE * 9));
            IDrawing.setPositionAndWidth(sliderLiftMaxDistance, width - (SQUARE_SIZE * 10) - BUTTON_WIDTH, BUTTON_HEIGHT * (i++) + SQUARE_SIZE + offsetY, BUTTON_WIDTH - TEXT_PADDING - font.width("256") + (SQUARE_SIZE * 9));
        }
        sliderPIDSMaxDistance.setHeight(BUTTON_HEIGHT);
        sliderAPGMaxDistance.setHeight(BUTTON_HEIGHT);
        sliderClockMaxDistance.setHeight(BUTTON_HEIGHT);
        sliderLiftButtonMaxDistance.setHeight(BUTTON_HEIGHT);
        sliderLiftPanelMaxDistance.setHeight(BUTTON_HEIGHT);
        sliderPSDAPGDoorMaxDistance.setHeight(BUTTON_HEIGHT);
        sliderPSDTopMaxDistance.setHeight(BUTTON_HEIGHT);
        sliderRailwaySignMaxDistance.setHeight(BUTTON_HEIGHT);
        sliderRouteSignMaxDistance.setHeight(BUTTON_HEIGHT);
        sliderSignalMaxDistance.setHeight(BUTTON_HEIGHT);
        sliderStationNameTallMaxDistance.setHeight(BUTTON_HEIGHT);
        sliderStationNameTiledMaxDistance.setHeight(BUTTON_HEIGHT);
        sliderLiftMaxDistance.setHeight(BUTTON_HEIGHT);
        sliderPIDSMaxDistance.setValue(Config.PIDSMaxDistance());
        sliderAPGMaxDistance.setValue(Config.APGMaxDistance());
        sliderClockMaxDistance.setValue(Config.clockMaxDistance());
        sliderLiftButtonMaxDistance.setValue(Config.liftButtonMaxDistance());
        sliderLiftPanelMaxDistance.setValue(Config.liftPanelMaxDistance());
        sliderPSDAPGDoorMaxDistance.setValue(Config.PSDAPGDoorMaxDistance());
        sliderPSDTopMaxDistance.setValue(Config.PSDTopMaxDistance());
        sliderRailwaySignMaxDistance.setValue(Config.railwaySignMaxDistance());
        sliderRouteSignMaxDistance.setValue(Config.routeSignMaxDistance());
        sliderSignalMaxDistance.setValue(Config.signalMaxDistance());
        sliderStationNameTallMaxDistance.setValue(Config.stationNameTallMaxDistance());
        sliderStationNameTiledMaxDistance.setValue(Config.stationNameTiledMaxDistance());
        sliderLiftMaxDistance.setValue(Config.liftMaxDistance());
        if (!Keys.LIFTS_ONLY) {
            addDrawableChild(sliderPIDSMaxDistance);
            addDrawableChild(sliderAPGMaxDistance);
            addDrawableChild(sliderClockMaxDistance);
            addDrawableChild(sliderLiftButtonMaxDistance);
            addDrawableChild(sliderLiftPanelMaxDistance);
            addDrawableChild(sliderPSDAPGDoorMaxDistance);
            addDrawableChild(sliderPSDTopMaxDistance);
            addDrawableChild(sliderRailwaySignMaxDistance);
            addDrawableChild(sliderRouteSignMaxDistance);
            addDrawableChild(sliderSignalMaxDistance);
            addDrawableChild(sliderStationNameTallMaxDistance);
            addDrawableChild(sliderStationNameTiledMaxDistance);
            addDrawableChild(sliderLiftMaxDistance);
        }
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        try {
            renderBackground(matrices);
            drawCenteredString(matrices, font, Text.translatable("gui.mtr.mtr_options"), width / 2, TEXT_PADDING, ARGB_WHITE);
            final int yStart1 = SQUARE_SIZE + TEXT_PADDING / 2;
            int i = 1;
            if (!Keys.LIFTS_ONLY) {
                drawString(matrices, font, Text.translatable("options.mtr.pids_max_view_distance"), SQUARE_SIZE, BUTTON_HEIGHT * (i++) + yStart1, ARGB_WHITE);
                drawString(matrices, font, Text.translatable("options.mtr.apg_max_view_distance"), SQUARE_SIZE, BUTTON_HEIGHT * (i++) + yStart1, ARGB_WHITE);
                drawString(matrices, font, Text.translatable("options.mtr.clock_max_view_distance"), SQUARE_SIZE, BUTTON_HEIGHT * (i++) + yStart1, ARGB_WHITE);
                drawString(matrices, font, Text.translatable("options.mtr.lift_bottom_max_view_distance"), SQUARE_SIZE, BUTTON_HEIGHT * (i++) + yStart1, ARGB_WHITE);
                drawString(matrices, font, Text.translatable("options.mtr.lift_panel_max_view_distance"), SQUARE_SIZE, BUTTON_HEIGHT * (i++) + yStart1, ARGB_WHITE);
                drawString(matrices, font, Text.translatable("options.mtr.psd_apg_door_max_view_distance"), SQUARE_SIZE, BUTTON_HEIGHT * (i++) + yStart1, ARGB_WHITE);
                drawString(matrices, font, Text.translatable("options.mtr.psd_top_max_view_distance"), SQUARE_SIZE, BUTTON_HEIGHT * (i++) + yStart1, ARGB_WHITE);
                drawString(matrices, font, Text.translatable("options.mtr.railway_sign_max_view_distance"), SQUARE_SIZE, BUTTON_HEIGHT * (i++) + yStart1, ARGB_WHITE);
                drawString(matrices, font, Text.translatable("options.mtr.route_sign_max_view_distance"), SQUARE_SIZE, BUTTON_HEIGHT * (i++) + yStart1, ARGB_WHITE);
                drawString(matrices, font, Text.translatable("options.mtr.signal_max_view_distance"), SQUARE_SIZE, BUTTON_HEIGHT * (i++) + yStart1, ARGB_WHITE);
                drawString(matrices, font, Text.translatable("options.mtr.station_name_tall_max_view_distance"), SQUARE_SIZE, BUTTON_HEIGHT * (i++) + yStart1, ARGB_WHITE);
                drawString(matrices, font, Text.translatable("options.mtr.station_name_tile_max_view_distance"), SQUARE_SIZE, BUTTON_HEIGHT * (i++) + yStart1, ARGB_WHITE);
                drawString(matrices, font, Text.translatable("options.mtr.lift_max_view_distance"), SQUARE_SIZE, BUTTON_HEIGHT * (i++) + yStart1, ARGB_WHITE);
            }
            super.render(matrices, mouseX, mouseY, delta);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClose() {
        super.onClose();
        Config.setPIDSMaxDistance(Math.max(4, sliderPIDSMaxDistance.getIntValue()));
        Config.setAPGMaxDistance(Math.max(4, sliderAPGMaxDistance.getIntValue()));
        Config.setClockMaxDistance(Math.max(4, sliderClockMaxDistance.getIntValue()));
        Config.setLiftButtonMaxDistance(Math.max(4, sliderLiftButtonMaxDistance.getIntValue()));
        Config.setLiftPanelMaxDistance(Math.max(4, sliderLiftPanelMaxDistance.getIntValue()));
        Config.setPSDAPGDoorMaxDistance(Math.max(4, sliderPSDAPGDoorMaxDistance.getIntValue()));
        Config.setPSDTopMaxDistance(Math.max(4, sliderPSDTopMaxDistance.getIntValue()));
        Config.setRailwaySignMaxDistance(Math.max(4, sliderRailwaySignMaxDistance.getIntValue()));
        Config.setRouteSignMaxDistance(Math.max(4, sliderRouteSignMaxDistance.getIntValue()));
        Config.setSignalMaxDistance(Math.max(4, sliderSignalMaxDistance.getIntValue()));
        Config.setStationNameTallMaxDistance(Math.max(4, sliderStationNameTallMaxDistance.getIntValue()));
        Config.setStationNameTiledMaxDistance(Math.max(4, sliderStationNameTiledMaxDistance.getIntValue()));
        Config.setLiftMaxDistance(Math.max(4, sliderLiftMaxDistance.getIntValue()));
        ClientData.DATA_CACHE.sync();
        ClientData.DATA_CACHE.refreshDynamicResources();
        ClientData.SIGNAL_BLOCKS.writeCache();
        this.minecraft.setScreen(parent);
    }
}
