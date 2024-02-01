package org.mtr.mod.screen;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.*;
import org.mtr.mod.Init;
import org.mtr.mod.InitClient;
import org.mtr.mod.Patreon;
import org.mtr.mod.client.ClientData;
import org.mtr.mod.client.Config;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.data.IGui;
import org.mtr.mod.packet.PacketRequestData;

public class ConfigScreen extends ScreenExtension implements IGui {

	private boolean useMTRFont;
	private boolean showAnnouncementMessages;
	private boolean useTTSAnnouncements;
	private boolean hideSpecialRailColors;
	private boolean hideTranslucentParts;
	private boolean shiftToToggleSitting;
	private int languageOptions;
	private boolean useDynamicFPS;

	private final boolean hasTimeAndWindControls;
	private final boolean useTimeAndWindSync;

	private final CheckboxWidgetExtension checkboxUseTimeAndWindSync;
	private final ButtonWidgetExtension buttonUseMTRFont;
	private final ButtonWidgetExtension buttonShowAnnouncementMessages;
	private final ButtonWidgetExtension buttonUseTTSAnnouncements;
	private final ButtonWidgetExtension buttonHideSpecialRailColors;
	private final ButtonWidgetExtension buttonHideTranslucentParts;
	private final ButtonWidgetExtension buttonShiftToToggleSitting;
	private final ButtonWidgetExtension buttonLanguageOptions;
	private final ButtonWidgetExtension buttonUseDynamicFPS;
	private final WidgetShorterSlider sliderTrackTextureOffset;
	private final WidgetShorterSlider sliderDynamicTextureResolution;
	private final WidgetShorterSlider sliderTrainRenderDistanceRatio;
	private final ButtonWidgetExtension buttonSupportPatreon;

	private static final int BUTTON_WIDTH = 60;
	private static final int BUTTON_HEIGHT = TEXT_HEIGHT + TEXT_PADDING;

	public ConfigScreen() {
		this(false, false);
	}

	public ConfigScreen(boolean useTimeAndWindSync) {
		this(true, useTimeAndWindSync);
	}

	private ConfigScreen(boolean hasTimeAndWindControls, boolean useTimeAndWindSync) {
		super();

		this.hasTimeAndWindControls = hasTimeAndWindControls && ClientData.hasPermission();
		this.useTimeAndWindSync = useTimeAndWindSync;

		checkboxUseTimeAndWindSync = new CheckboxWidgetExtension(0, 0, 0, SQUARE_SIZE, true, checked -> {
		});
		checkboxUseTimeAndWindSync.setMessage2(new Text(TextHelper.translatable("gui.mtr.use_time_and_wind_sync").data));

		buttonUseMTRFont = new ButtonWidgetExtension(0, 0, 0, BUTTON_HEIGHT, TextHelper.literal(""), button -> {
			useMTRFont = Config.setUseMTRFont(!useMTRFont);
			setButtonText(button, useMTRFont);
		});
		buttonShowAnnouncementMessages = new ButtonWidgetExtension(0, 0, 0, BUTTON_HEIGHT, TextHelper.literal(""), button -> {
			showAnnouncementMessages = Config.setShowAnnouncementMessages(!showAnnouncementMessages);
			setButtonText(button, showAnnouncementMessages);
		});
		buttonUseTTSAnnouncements = new ButtonWidgetExtension(0, 0, 0, BUTTON_HEIGHT, TextHelper.literal(""), button -> {
			useTTSAnnouncements = Config.setUseTTSAnnouncements(!useTTSAnnouncements);
			setButtonText(button, useTTSAnnouncements);
		});
		buttonHideSpecialRailColors = new ButtonWidgetExtension(0, 0, 0, BUTTON_HEIGHT, TextHelper.literal(""), button -> {
			hideSpecialRailColors = Config.setHideSpecialRailColors(!hideSpecialRailColors);
			setButtonText(button, hideSpecialRailColors);
		});
		buttonHideTranslucentParts = new ButtonWidgetExtension(0, 0, 0, BUTTON_HEIGHT, TextHelper.literal(""), button -> {
			hideTranslucentParts = Config.setHideTranslucentParts(!hideTranslucentParts);
			setButtonText(button, hideTranslucentParts);
		});
		buttonShiftToToggleSitting = new ButtonWidgetExtension(0, 0, 0, BUTTON_HEIGHT, TextHelper.literal(""), button -> {
			shiftToToggleSitting = Config.setShiftToToggleSitting(!shiftToToggleSitting);
			setButtonText(button, shiftToToggleSitting);
		});
		buttonLanguageOptions = new ButtonWidgetExtension(0, 0, 0, BUTTON_HEIGHT, TextHelper.literal(""), button -> {
			languageOptions = Config.setLanguageOptions(languageOptions + 1);
			button.setMessage(new Text(TextHelper.translatable("options.mtr.language_options_" + languageOptions).data));
		});
		buttonUseDynamicFPS = new ButtonWidgetExtension(0, 0, 0, BUTTON_HEIGHT, TextHelper.literal(""), button -> {
			useDynamicFPS = Config.setUseDynamicFPS(!useDynamicFPS);
			setButtonText(button, useDynamicFPS);
		});
		sliderTrackTextureOffset = new WidgetShorterSlider(0, 0, Config.TRACK_OFFSET_COUNT - 1, String::valueOf, null);
		sliderDynamicTextureResolution = new WidgetShorterSlider(0, 0, Config.DYNAMIC_RESOLUTION_COUNT - 1, String::valueOf, null);
		sliderTrainRenderDistanceRatio = new WidgetShorterSlider(0, 0, Config.TRAIN_RENDER_DISTANCE_RATIO_COUNT - 1, num -> String.format("%d%%", (num + 1) * 100 / Config.TRAIN_RENDER_DISTANCE_RATIO_COUNT), null);
		buttonSupportPatreon = new ButtonWidgetExtension(0, 0, 0, BUTTON_HEIGHT, TextHelper.literal(""), button -> Util.getOperatingSystem().open("https://www.patreon.com/minecraft_transit_railway"));
	}

	@Override
	protected void init2() {
		super.init2();
		Config.refreshProperties();
		useMTRFont = Config.useMTRFont();
		showAnnouncementMessages = Config.showAnnouncementMessages();
		useTTSAnnouncements = Config.useTTSAnnouncements();
		hideSpecialRailColors = Config.hideSpecialRailColors();
		hideTranslucentParts = Config.hideTranslucentParts();
		shiftToToggleSitting = Config.shiftToToggleSitting();
		languageOptions = Config.languageOptions();
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
		IDrawing.setPositionAndWidth(buttonShowAnnouncementMessages, width - SQUARE_SIZE - BUTTON_WIDTH, BUTTON_HEIGHT * (i++) + SQUARE_SIZE + offsetY, BUTTON_WIDTH);
		IDrawing.setPositionAndWidth(buttonUseTTSAnnouncements, width - SQUARE_SIZE - BUTTON_WIDTH, BUTTON_HEIGHT * (i++) + SQUARE_SIZE + offsetY, BUTTON_WIDTH);
		IDrawing.setPositionAndWidth(buttonHideSpecialRailColors, width - SQUARE_SIZE - BUTTON_WIDTH, BUTTON_HEIGHT * (i++) + SQUARE_SIZE + offsetY, BUTTON_WIDTH);
		IDrawing.setPositionAndWidth(buttonHideTranslucentParts, width - SQUARE_SIZE - BUTTON_WIDTH, BUTTON_HEIGHT * (i++) + SQUARE_SIZE + offsetY, BUTTON_WIDTH);
		IDrawing.setPositionAndWidth(buttonShiftToToggleSitting, width - SQUARE_SIZE - BUTTON_WIDTH, BUTTON_HEIGHT * (i++) + SQUARE_SIZE + offsetY, BUTTON_WIDTH);
		IDrawing.setPositionAndWidth(buttonLanguageOptions, width - SQUARE_SIZE - BUTTON_WIDTH, BUTTON_HEIGHT * (i++) + SQUARE_SIZE + offsetY, BUTTON_WIDTH);
		IDrawing.setPositionAndWidth(buttonUseDynamicFPS, width - SQUARE_SIZE - BUTTON_WIDTH, BUTTON_HEIGHT * (i++) + SQUARE_SIZE + offsetY, BUTTON_WIDTH);
		IDrawing.setPositionAndWidth(sliderTrackTextureOffset, width - SQUARE_SIZE - BUTTON_WIDTH, BUTTON_HEIGHT * (i++) + SQUARE_SIZE + offsetY, BUTTON_WIDTH - TEXT_PADDING - GraphicsHolder.getTextWidth("100%"));
		IDrawing.setPositionAndWidth(sliderDynamicTextureResolution, width - SQUARE_SIZE - BUTTON_WIDTH, BUTTON_HEIGHT * (i++) + SQUARE_SIZE + offsetY, BUTTON_WIDTH - TEXT_PADDING - GraphicsHolder.getTextWidth("100%"));
		IDrawing.setPositionAndWidth(sliderTrainRenderDistanceRatio, width - SQUARE_SIZE - BUTTON_WIDTH, BUTTON_HEIGHT * (i++) + SQUARE_SIZE + offsetY, BUTTON_WIDTH - TEXT_PADDING - GraphicsHolder.getTextWidth("100%"));
		IDrawing.setPositionAndWidth(buttonSupportPatreon, width - SQUARE_SIZE - BUTTON_WIDTH, BUTTON_HEIGHT * i + SQUARE_SIZE + offsetY, BUTTON_WIDTH);
		setButtonText(new ButtonWidget(buttonUseMTRFont), useMTRFont);
		setButtonText(new ButtonWidget(buttonShowAnnouncementMessages), showAnnouncementMessages);
		setButtonText(new ButtonWidget(buttonUseTTSAnnouncements), useTTSAnnouncements);
		setButtonText(new ButtonWidget(buttonHideSpecialRailColors), hideSpecialRailColors);
		setButtonText(new ButtonWidget(buttonHideTranslucentParts), hideTranslucentParts);
		setButtonText(new ButtonWidget(buttonShiftToToggleSitting), shiftToToggleSitting);
		buttonLanguageOptions.setMessage2(new Text(TextHelper.translatable("options.mtr.language_options_" + languageOptions).data));
		setButtonText(new ButtonWidget(buttonUseDynamicFPS), useDynamicFPS);
		sliderTrackTextureOffset.setHeight2(BUTTON_HEIGHT);
		sliderTrackTextureOffset.setValue(Config.trackTextureOffset());
		sliderDynamicTextureResolution.setHeight2(BUTTON_HEIGHT);
		sliderDynamicTextureResolution.setValue(Config.dynamicTextureResolution());
		sliderTrainRenderDistanceRatio.setHeight2(BUTTON_HEIGHT);
		sliderTrainRenderDistanceRatio.setValue(Config.trainRenderDistanceRatio());
		buttonSupportPatreon.setMessage2(new Text(TextHelper.translatable("gui.mtr.support").data));

		if (hasTimeAndWindControls) {
			addChild(new ClickableWidget(checkboxUseTimeAndWindSync));
		}
		addChild(new ClickableWidget(buttonUseMTRFont));
		addChild(new ClickableWidget(buttonShowAnnouncementMessages));
		addChild(new ClickableWidget(buttonUseTTSAnnouncements));
		addChild(new ClickableWidget(buttonHideSpecialRailColors));
		addChild(new ClickableWidget(buttonHideTranslucentParts));
		addChild(new ClickableWidget(buttonShiftToToggleSitting));
		addChild(new ClickableWidget(buttonLanguageOptions));
		addChild(new ClickableWidget(buttonUseDynamicFPS));
		addChild(new ClickableWidget(sliderTrackTextureOffset));
		addChild(new ClickableWidget(sliderDynamicTextureResolution));
		addChild(new ClickableWidget(sliderTrainRenderDistanceRatio));
		addChild(new ClickableWidget(buttonSupportPatreon));
	}

	@Override
	public void render(GraphicsHolder graphicsHolder, int mouseX, int mouseY, float delta) {
		try {
			renderBackground(graphicsHolder);
			graphicsHolder.drawCenteredText(TextHelper.translatable("gui.mtr.mtr_options"), width / 2, TEXT_PADDING, ARGB_WHITE);

			final int yStart1 = SQUARE_SIZE + TEXT_PADDING / 2 + (hasTimeAndWindControls ? SQUARE_SIZE : 0);
			int i = 1;
			graphicsHolder.drawText(TextHelper.translatable("options.mtr.use_mtr_font"), SQUARE_SIZE, yStart1, ARGB_WHITE, false, MAX_LIGHT_GLOWING);
			graphicsHolder.drawText(TextHelper.translatable("options.mtr.show_announcement_messages"), SQUARE_SIZE, BUTTON_HEIGHT * (i++) + yStart1, ARGB_WHITE, false, MAX_LIGHT_GLOWING);
			graphicsHolder.drawText(TextHelper.translatable("options.mtr.use_tts_announcements"), SQUARE_SIZE, BUTTON_HEIGHT * (i++) + yStart1, ARGB_WHITE, false, MAX_LIGHT_GLOWING);
			graphicsHolder.drawText(TextHelper.translatable("options.mtr.hide_special_rail_colors"), SQUARE_SIZE, BUTTON_HEIGHT * (i++) + yStart1, ARGB_WHITE, false, MAX_LIGHT_GLOWING);
			graphicsHolder.drawText(TextHelper.translatable("options.mtr.hide_translucent_parts"), SQUARE_SIZE, BUTTON_HEIGHT * (i++) + yStart1, ARGB_WHITE, false, MAX_LIGHT_GLOWING);
			graphicsHolder.drawText(TextHelper.translatable("options.mtr.shift_to_toggle_sitting", InitClient.getShiftText()), SQUARE_SIZE, BUTTON_HEIGHT * (i++) + yStart1, ARGB_WHITE, false, MAX_LIGHT_GLOWING);
			graphicsHolder.drawText(TextHelper.translatable("options.mtr.language_options"), SQUARE_SIZE, BUTTON_HEIGHT * (i++) + yStart1, ARGB_WHITE, false, MAX_LIGHT_GLOWING);
			graphicsHolder.drawText(TextHelper.translatable("options.mtr.use_dynamic_fps"), SQUARE_SIZE, BUTTON_HEIGHT * (i++) + yStart1, ARGB_WHITE, false, MAX_LIGHT_GLOWING);
			graphicsHolder.drawText(TextHelper.translatable("options.mtr.track_texture_offset"), SQUARE_SIZE, BUTTON_HEIGHT * (i++) + yStart1, ARGB_WHITE, false, MAX_LIGHT_GLOWING);
			graphicsHolder.drawText(TextHelper.translatable("options.mtr.dynamic_texture_resolution"), SQUARE_SIZE, BUTTON_HEIGHT * (i++) + yStart1, ARGB_WHITE, false, MAX_LIGHT_GLOWING);
			graphicsHolder.drawText(TextHelper.translatable("options.mtr.vehicle_render_distance_ratio"), SQUARE_SIZE, BUTTON_HEIGHT * (i++) + yStart1, ARGB_WHITE, false, MAX_LIGHT_GLOWING);
			graphicsHolder.drawText(TextHelper.translatable("options.mtr.support_patreon"), SQUARE_SIZE, BUTTON_HEIGHT * (i++) + yStart1, ARGB_WHITE, false, MAX_LIGHT_GLOWING);

			final int yStart2 = BUTTON_HEIGHT * (i + 1) + yStart1;
			String tierTitle = "";
			int y = 0;
			int x = 0;
			int maxWidth = 0;
			for (final Patreon patreon : Config.PATREON_LIST) {
				if (!patreon.tierTitle.equals(tierTitle)) {
					x += maxWidth + TEXT_PADDING;
					y = 0;
					final MutableText text = TextHelper.literal(patreon.tierTitle);
					maxWidth = GraphicsHolder.getTextWidth(text);
					graphicsHolder.drawText(text, SQUARE_SIZE - TEXT_PADDING + x, yStart2, patreon.tierColor, false, MAX_LIGHT_GLOWING);
				} else if (y + yStart2 + TEXT_HEIGHT + SQUARE_SIZE > height) {
					x += maxWidth + TEXT_PADDING;
					y = 0;
					maxWidth = 0;
				}

				tierTitle = patreon.tierTitle;
				final MutableText text = patreon.tierAmount < 1000 ? TextHelper.translatable("options.mtr.anonymous") : TextHelper.literal(patreon.name);
				maxWidth = Math.max(maxWidth, GraphicsHolder.getTextWidth(text));
				graphicsHolder.drawText(text, SQUARE_SIZE - TEXT_PADDING + x, yStart2 + y + TEXT_HEIGHT + TEXT_PADDING, ARGB_LIGHT_GRAY, false, MAX_LIGHT_GLOWING);
				y += TEXT_HEIGHT + 2;
			}

			super.render(graphicsHolder, mouseX, mouseY, delta);
		} catch (Exception e) {
			Init.logException(e);
		}
	}

	@Override
	public void onClose2() {
		super.onClose2();
		Config.setTrackTextureOffset(sliderTrackTextureOffset.getIntValue());
		Config.setDynamicTextureResolution(sliderDynamicTextureResolution.getIntValue());
		Config.setTrainRenderDistanceRatio(sliderTrainRenderDistanceRatio.getIntValue());
		if (MinecraftClient.getInstance().getWorldMapped() != null) {
			InitClient.REGISTRY_CLIENT.sendPacketToServer(new PacketRequestData(true));
		}
	}

	private static void setButtonText(ButtonWidget button, boolean state) {
		button.setMessage(new Text(TextHelper.translatable(state ? "options.mtr.on" : "options.mtr.off").data));
	}
}
