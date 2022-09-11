package mtr.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import mtr.Keys;
import mtr.Patreon;
import mtr.client.ClientData;
import mtr.client.Config;
import mtr.client.IDrawing;
import mtr.data.IGui;
import mtr.mappings.ScreenMapper;
import mtr.mappings.Text;
import mtr.packet.PacketTrainDataGuiClient;
import net.minecraft.Util;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class ConfigScreen extends ScreenMapper implements IGui {

	private boolean useMTRFont;
	private boolean showAnnouncementMessages;
	private boolean useTTSAnnouncements;
	private boolean hideSpecialRailColors;
	private boolean hideTranslucentParts;
	private boolean shiftToToggleSitting;
	private boolean useDynamicFPS;

	private final boolean hasTimeAndWindControls;
	private final boolean useTimeAndWindSync;

	private final WidgetBetterCheckbox checkboxUseTimeAndWindSync;
	private final Button buttonUseMTRFont;
	private final Button buttonShowAnnouncementMessages;
	private final Button buttonUseTTSAnnouncements;
	private final Button buttonHideSpecialRailColors;
	private final Button buttonHideTranslucentParts;
	private final Button buttonShiftToToggleSitting;
	private final Button buttonUseDynamicFPS;
	private final WidgetShorterSlider sliderTrackTextureOffset;
	private final WidgetShorterSlider sliderDynamicTextureResolution;
	private final WidgetShorterSlider sliderTrainRenderDistanceRatio;
	private final Button buttonSupportPatreon;

	private static final int BUTTON_WIDTH = 60;
	private static final int BUTTON_HEIGHT = TEXT_HEIGHT + TEXT_PADDING;

	public ConfigScreen() {
		this(false, false);
	}

	public ConfigScreen(boolean useTimeAndWindSync) {
		this(true, useTimeAndWindSync);
	}

	private ConfigScreen(boolean hasTimeAndWindControls, boolean useTimeAndWindSync) {
		super(Text.literal(""));

		this.hasTimeAndWindControls = hasTimeAndWindControls && ClientData.hasPermission();
		this.useTimeAndWindSync = useTimeAndWindSync;

		checkboxUseTimeAndWindSync = new WidgetBetterCheckbox(0, 0, 0, SQUARE_SIZE, Text.translatable("gui.mtr.use_time_and_wind_sync"), PacketTrainDataGuiClient::sendUseTimeAndWindSyncC2S);

		buttonUseMTRFont = new Button(0, 0, 0, BUTTON_HEIGHT, Text.literal(""), button -> {
			useMTRFont = Config.setUseMTRFont(!useMTRFont);
			setButtonText(button, useMTRFont);
		});
		buttonShowAnnouncementMessages = new Button(0, 0, 0, BUTTON_HEIGHT, Text.literal(""), button -> {
			showAnnouncementMessages = Config.setShowAnnouncementMessages(!showAnnouncementMessages);
			setButtonText(button, showAnnouncementMessages);
		});
		buttonUseTTSAnnouncements = new Button(0, 0, 0, BUTTON_HEIGHT, Text.literal(""), button -> {
			useTTSAnnouncements = Config.setUseTTSAnnouncements(!useTTSAnnouncements);
			setButtonText(button, useTTSAnnouncements);
		});
		buttonHideSpecialRailColors = new Button(0, 0, 0, BUTTON_HEIGHT, Text.literal(""), button -> {
			hideSpecialRailColors = Config.setHideSpecialRailColors(!hideSpecialRailColors);
			setButtonText(button, hideSpecialRailColors);
		});
		buttonHideTranslucentParts = new Button(0, 0, 0, BUTTON_HEIGHT, Text.literal(""), button -> {
			hideTranslucentParts = Config.setHideTranslucentParts(!hideTranslucentParts);
			setButtonText(button, hideTranslucentParts);
		});
		buttonShiftToToggleSitting = new Button(0, 0, 0, BUTTON_HEIGHT, Text.literal(""), button -> {
			shiftToToggleSitting = Config.setShiftToToggleSitting(!shiftToToggleSitting);
			setButtonText(button, shiftToToggleSitting);
		});
		buttonUseDynamicFPS = new Button(0, 0, 0, BUTTON_HEIGHT, Text.literal(""), button -> {
			useDynamicFPS = Config.setUseDynamicFPS(!useDynamicFPS);
			setButtonText(button, useDynamicFPS);
		});
		sliderTrackTextureOffset = new WidgetShorterSlider(0, 0, Config.TRACK_OFFSET_COUNT - 1, Object::toString, null);
		sliderDynamicTextureResolution = new WidgetShorterSlider(0, 0, Config.DYNAMIC_RESOLUTION_COUNT - 1, Object::toString, null);
		sliderTrainRenderDistanceRatio = new WidgetShorterSlider(0, 0, Config.TRAIN_RENDER_DISTANCE_RATIO_COUNT - 1, num -> String.format("%d%%", (num + 1) * 100 / Config.TRAIN_RENDER_DISTANCE_RATIO_COUNT), null);
		buttonSupportPatreon = new Button(0, 0, 0, BUTTON_HEIGHT, Text.literal(""), button -> Util.getPlatform().openUri("https://www.patreon.com/minecraft_transit_railway"));
	}

	@Override
	protected void init() {
		super.init();
		Config.refreshProperties();
		useMTRFont = Config.useMTRFont();
		showAnnouncementMessages = Config.showAnnouncementMessages();
		useTTSAnnouncements = Config.useTTSAnnouncements();
		hideSpecialRailColors = Config.hideSpecialRailColors();
		hideTranslucentParts = Config.hideTranslucentParts();
		shiftToToggleSitting = Config.shiftToToggleSitting();
		useDynamicFPS = Config.useDynamicFPS();

		final int offsetY;
		if (hasTimeAndWindControls) {
			IDrawing.setPositionAndWidth(checkboxUseTimeAndWindSync, SQUARE_SIZE, SQUARE_SIZE, width);
			checkboxUseTimeAndWindSync.setChecked(useTimeAndWindSync);
			offsetY = SQUARE_SIZE;
		} else {
			offsetY = 0;
		}

		int i = 1;
		IDrawing.setPositionAndWidth(buttonUseMTRFont, width - SQUARE_SIZE - BUTTON_WIDTH, SQUARE_SIZE + offsetY, BUTTON_WIDTH);
		if (!Keys.LIFTS_ONLY) {
			IDrawing.setPositionAndWidth(buttonShowAnnouncementMessages, width - SQUARE_SIZE - BUTTON_WIDTH, BUTTON_HEIGHT * (i++) + SQUARE_SIZE + offsetY, BUTTON_WIDTH);
			IDrawing.setPositionAndWidth(buttonUseTTSAnnouncements, width - SQUARE_SIZE - BUTTON_WIDTH, BUTTON_HEIGHT * (i++) + SQUARE_SIZE + offsetY, BUTTON_WIDTH);
			IDrawing.setPositionAndWidth(buttonHideSpecialRailColors, width - SQUARE_SIZE - BUTTON_WIDTH, BUTTON_HEIGHT * (i++) + SQUARE_SIZE + offsetY, BUTTON_WIDTH);
			IDrawing.setPositionAndWidth(buttonHideTranslucentParts, width - SQUARE_SIZE - BUTTON_WIDTH, BUTTON_HEIGHT * (i++) + SQUARE_SIZE + offsetY, BUTTON_WIDTH);
			IDrawing.setPositionAndWidth(buttonShiftToToggleSitting, width - SQUARE_SIZE - BUTTON_WIDTH, BUTTON_HEIGHT * (i++) + SQUARE_SIZE + offsetY, BUTTON_WIDTH);
			IDrawing.setPositionAndWidth(buttonUseDynamicFPS, width - SQUARE_SIZE - BUTTON_WIDTH, BUTTON_HEIGHT * (i++) + SQUARE_SIZE + offsetY, BUTTON_WIDTH);
			IDrawing.setPositionAndWidth(sliderTrackTextureOffset, width - SQUARE_SIZE - BUTTON_WIDTH, BUTTON_HEIGHT * (i++) + SQUARE_SIZE + offsetY, BUTTON_WIDTH - TEXT_PADDING - font.width("100%"));
			IDrawing.setPositionAndWidth(sliderDynamicTextureResolution, width - SQUARE_SIZE - BUTTON_WIDTH, BUTTON_HEIGHT * (i++) + SQUARE_SIZE + offsetY, BUTTON_WIDTH - TEXT_PADDING - font.width("100%"));
			IDrawing.setPositionAndWidth(sliderTrainRenderDistanceRatio, width - SQUARE_SIZE - BUTTON_WIDTH, BUTTON_HEIGHT * (i++) + SQUARE_SIZE + offsetY, BUTTON_WIDTH - TEXT_PADDING - font.width("100%"));
		}
		IDrawing.setPositionAndWidth(buttonSupportPatreon, width - SQUARE_SIZE - BUTTON_WIDTH, BUTTON_HEIGHT * i + SQUARE_SIZE + offsetY, BUTTON_WIDTH);
		setButtonText(buttonUseMTRFont, useMTRFont);
		setButtonText(buttonShowAnnouncementMessages, showAnnouncementMessages);
		setButtonText(buttonUseTTSAnnouncements, useTTSAnnouncements);
		setButtonText(buttonHideSpecialRailColors, hideSpecialRailColors);
		setButtonText(buttonHideTranslucentParts, hideTranslucentParts);
		setButtonText(buttonShiftToToggleSitting, shiftToToggleSitting);
		setButtonText(buttonUseDynamicFPS, useDynamicFPS);
		sliderTrackTextureOffset.setHeight(BUTTON_HEIGHT);
		sliderTrackTextureOffset.setValue(Config.trackTextureOffset());
		sliderDynamicTextureResolution.setHeight(BUTTON_HEIGHT);
		sliderDynamicTextureResolution.setValue(Config.dynamicTextureResolution());
		sliderTrainRenderDistanceRatio.setHeight(BUTTON_HEIGHT);
		sliderTrainRenderDistanceRatio.setValue(Config.trainRenderDistanceRatio());
		buttonSupportPatreon.setMessage(Text.translatable("gui.mtr.support"));

		if (hasTimeAndWindControls) {
			addDrawableChild(checkboxUseTimeAndWindSync);
		}
		addDrawableChild(buttonUseMTRFont);
		if (!Keys.LIFTS_ONLY) {
			addDrawableChild(buttonShowAnnouncementMessages);
			addDrawableChild(buttonUseTTSAnnouncements);
			addDrawableChild(buttonHideSpecialRailColors);
			addDrawableChild(buttonHideTranslucentParts);
			addDrawableChild(buttonShiftToToggleSitting);
			addDrawableChild(buttonUseDynamicFPS);
			addDrawableChild(sliderTrackTextureOffset);
			addDrawableChild(sliderDynamicTextureResolution);
			addDrawableChild(sliderTrainRenderDistanceRatio);
		}
		addDrawableChild(buttonSupportPatreon);
	}

	@Override
	public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
		try {
			renderBackground(matrices);
			drawCenteredString(matrices, font, Text.translatable("gui.mtr.mtr_options"), width / 2, TEXT_PADDING, ARGB_WHITE);

			final int yStart1 = SQUARE_SIZE + TEXT_PADDING / 2 + (hasTimeAndWindControls ? SQUARE_SIZE : 0);
			int i = 1;
			drawString(matrices, font, Text.translatable("options.mtr.use_mtr_font"), SQUARE_SIZE, yStart1, ARGB_WHITE);
			if (!Keys.LIFTS_ONLY) {
				drawString(matrices, font, Text.translatable("options.mtr.show_announcement_messages"), SQUARE_SIZE, BUTTON_HEIGHT * (i++) + yStart1, ARGB_WHITE);
				drawString(matrices, font, Text.translatable("options.mtr.use_tts_announcements"), SQUARE_SIZE, BUTTON_HEIGHT * (i++) + yStart1, ARGB_WHITE);
				drawString(matrices, font, Text.translatable("options.mtr.hide_special_rail_colors"), SQUARE_SIZE, BUTTON_HEIGHT * (i++) + yStart1, ARGB_WHITE);
				drawString(matrices, font, Text.translatable("options.mtr.hide_translucent_parts"), SQUARE_SIZE, BUTTON_HEIGHT * (i++) + yStart1, ARGB_WHITE);
				drawString(matrices, font, Text.translatable("options.mtr.shift_to_toggle_sitting", minecraft == null ? "" : minecraft.options.keyShift.getTranslatedKeyMessage()), SQUARE_SIZE, BUTTON_HEIGHT * (i++) + yStart1, ARGB_WHITE);
				drawString(matrices, font, Text.translatable("options.mtr.use_dynamic_fps"), SQUARE_SIZE, BUTTON_HEIGHT * (i++) + yStart1, ARGB_WHITE);
				drawString(matrices, font, Text.translatable("options.mtr.track_texture_offset"), SQUARE_SIZE, BUTTON_HEIGHT * (i++) + yStart1, ARGB_WHITE);
				drawString(matrices, font, Text.translatable("options.mtr.dynamic_texture_resolution"), SQUARE_SIZE, BUTTON_HEIGHT * (i++) + yStart1, ARGB_WHITE);
				drawString(matrices, font, Text.translatable("options.mtr.vehicle_render_distance_ratio"), SQUARE_SIZE, BUTTON_HEIGHT * (i++) + yStart1, ARGB_WHITE);
			}
			drawString(matrices, font, Text.translatable("options.mtr.support_patreon"), SQUARE_SIZE, BUTTON_HEIGHT * (i++) + yStart1, ARGB_WHITE);

			final int yStart2 = BUTTON_HEIGHT * (i + 1) + yStart1;
			String tierTitle = "";
			int y = 0;
			int x = 0;
			int maxWidth = 0;
			for (final Patreon patreon : Config.PATREON_LIST) {
				if (!patreon.tierTitle.equals(tierTitle)) {
					x += maxWidth + TEXT_PADDING;
					y = 0;
					final Component text = Text.literal(patreon.tierTitle);
					maxWidth = font.width(text);
					drawString(matrices, font, text, SQUARE_SIZE - TEXT_PADDING + x, yStart2, patreon.tierColor);
				} else if (y + yStart2 + TEXT_HEIGHT + SQUARE_SIZE > height) {
					x += maxWidth + TEXT_PADDING;
					y = 0;
					maxWidth = 0;
				}

				tierTitle = patreon.tierTitle;
				final Component text = patreon.tierAmount < 1000 ? Text.translatable("options.mtr.anonymous") : Text.literal(patreon.name);
				maxWidth = Math.max(maxWidth, font.width(text));
				drawString(matrices, font, text, SQUARE_SIZE - TEXT_PADDING + x, yStart2 + y + TEXT_HEIGHT + TEXT_PADDING, ARGB_LIGHT_GRAY);
				y += TEXT_HEIGHT;
			}

			super.render(matrices, mouseX, mouseY, delta);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClose() {
		super.onClose();
		Config.setTrackTextureOffset(sliderTrackTextureOffset.getIntValue());
		Config.setDynamicTextureResolution(sliderDynamicTextureResolution.getIntValue());
		Config.setTrainRenderDistanceRatio(sliderTrainRenderDistanceRatio.getIntValue());
		ClientData.DATA_CACHE.sync();
		ClientData.SIGNAL_BLOCKS.writeCache();
	}

	private static void setButtonText(Button button, boolean state) {
		button.setMessage(Text.translatable(state ? "options.mtr.on" : "options.mtr.off"));
	}
}
