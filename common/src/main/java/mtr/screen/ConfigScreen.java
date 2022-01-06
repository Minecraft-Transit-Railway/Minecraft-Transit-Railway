package mtr.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import mtr.Patreon;
import mtr.client.ClientData;
import mtr.client.Config;
import mtr.client.IDrawing;
import mtr.data.IGui;
import mtr.mappings.ScreenMapper;
import net.minecraft.Util;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

public class ConfigScreen extends ScreenMapper implements IGui {

	private boolean useMTRFont;
	private boolean showAnnouncementMessages;
	private boolean useTTSAnnouncements;
	private boolean hideSpecialRailColors;
	private boolean hideTranslucentParts;
	private boolean useDynamicFPS;

	private final Button buttonUseMTRFont;
	private final Button buttonShowAnnouncementMessages;
	private final Button buttonUseTTSAnnouncements;
	private final Button buttonHideSpecialRailColors;
	private final Button buttonHideTranslucentParts;
	private final Button buttonUseDynamicFPS;
	private final WidgetShorterSlider sliderTrackTextureOffset;
	private final WidgetShorterSlider sliderDynamicTextureResolution;
	private final Button buttonSupportPatreon;

	private static final int BUTTON_WIDTH = 60;
	private static final int BUTTON_HEIGHT = TEXT_HEIGHT + TEXT_PADDING;

	public ConfigScreen() {
		super(new TextComponent(""));

		buttonUseMTRFont = new Button(0, 0, 0, BUTTON_HEIGHT, new TextComponent(""), button -> {
			useMTRFont = Config.setUseMTRFont(!useMTRFont);
			setButtonText(button, useMTRFont);
		});
		buttonShowAnnouncementMessages = new Button(0, 0, 0, BUTTON_HEIGHT, new TextComponent(""), button -> {
			showAnnouncementMessages = Config.setShowAnnouncementMessages(!showAnnouncementMessages);
			setButtonText(button, showAnnouncementMessages);
		});
		buttonUseTTSAnnouncements = new Button(0, 0, 0, BUTTON_HEIGHT, new TextComponent(""), button -> {
			useTTSAnnouncements = Config.setUseTTSAnnouncements(!useTTSAnnouncements);
			setButtonText(button, useTTSAnnouncements);
		});
		buttonHideSpecialRailColors = new Button(0, 0, 0, BUTTON_HEIGHT, new TextComponent(""), button -> {
			hideSpecialRailColors = Config.setHideSpecialRailColors(!hideSpecialRailColors);
			setButtonText(button, hideSpecialRailColors);
		});
		buttonHideTranslucentParts = new Button(0, 0, 0, BUTTON_HEIGHT, new TextComponent(""), button -> {
			hideTranslucentParts = Config.setHideTranslucentParts(!hideTranslucentParts);
			setButtonText(button, hideTranslucentParts);
		});
		buttonUseDynamicFPS = new Button(0, 0, 0, BUTTON_HEIGHT, new TextComponent(""), button -> {
			useDynamicFPS = Config.setUseDynamicFPS(!useDynamicFPS);
			setButtonText(button, useDynamicFPS);
		});
		sliderTrackTextureOffset = new WidgetShorterSlider(0, 0, Config.TRACK_OFFSET_COUNT - 1, Object::toString);
		sliderDynamicTextureResolution = new WidgetShorterSlider(0, 0, Config.DYNAMIC_RESOLUTION_COUNT - 1, Object::toString);
		buttonSupportPatreon = new Button(0, 0, 0, BUTTON_HEIGHT, new TextComponent(""), button -> Util.getPlatform().openUri("https://www.patreon.com/minecraft_transit_railway"));
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
		useDynamicFPS = Config.useDynamicFPS();

		int i = 1;
		IDrawing.setPositionAndWidth(buttonUseMTRFont, width - SQUARE_SIZE - BUTTON_WIDTH, SQUARE_SIZE, BUTTON_WIDTH);
		IDrawing.setPositionAndWidth(buttonShowAnnouncementMessages, width - SQUARE_SIZE - BUTTON_WIDTH, BUTTON_HEIGHT * (i++) + SQUARE_SIZE, BUTTON_WIDTH);
		IDrawing.setPositionAndWidth(buttonUseTTSAnnouncements, width - SQUARE_SIZE - BUTTON_WIDTH, BUTTON_HEIGHT * (i++) + SQUARE_SIZE, BUTTON_WIDTH);
		IDrawing.setPositionAndWidth(buttonHideSpecialRailColors, width - SQUARE_SIZE - BUTTON_WIDTH, BUTTON_HEIGHT * (i++) + SQUARE_SIZE, BUTTON_WIDTH);
		IDrawing.setPositionAndWidth(buttonHideTranslucentParts, width - SQUARE_SIZE - BUTTON_WIDTH, BUTTON_HEIGHT * (i++) + SQUARE_SIZE, BUTTON_WIDTH);
		IDrawing.setPositionAndWidth(buttonUseDynamicFPS, width - SQUARE_SIZE - BUTTON_WIDTH, BUTTON_HEIGHT * (i++) + SQUARE_SIZE, BUTTON_WIDTH);
		IDrawing.setPositionAndWidth(sliderTrackTextureOffset, width - SQUARE_SIZE - BUTTON_WIDTH, BUTTON_HEIGHT * (i++) + SQUARE_SIZE, BUTTON_WIDTH - TEXT_PADDING - font.width("88"));
		IDrawing.setPositionAndWidth(sliderDynamicTextureResolution, width - SQUARE_SIZE - BUTTON_WIDTH, BUTTON_HEIGHT * (i++) + SQUARE_SIZE, BUTTON_WIDTH - TEXT_PADDING - font.width("88"));
		IDrawing.setPositionAndWidth(buttonSupportPatreon, width - SQUARE_SIZE - BUTTON_WIDTH, BUTTON_HEIGHT * i + SQUARE_SIZE, BUTTON_WIDTH);
		setButtonText(buttonUseMTRFont, useMTRFont);
		setButtonText(buttonShowAnnouncementMessages, showAnnouncementMessages);
		setButtonText(buttonUseTTSAnnouncements, useTTSAnnouncements);
		setButtonText(buttonHideSpecialRailColors, hideSpecialRailColors);
		setButtonText(buttonHideTranslucentParts, hideTranslucentParts);
		setButtonText(buttonUseDynamicFPS, useDynamicFPS);
		sliderTrackTextureOffset.setHeight(BUTTON_HEIGHT);
		sliderTrackTextureOffset.setValue(Config.trackTextureOffset());
		sliderDynamicTextureResolution.setHeight(BUTTON_HEIGHT);
		sliderDynamicTextureResolution.setValue(Config.dynamicTextureResolution());
		buttonSupportPatreon.setMessage(new TranslatableComponent("gui.mtr.support"));

		addDrawableChild(buttonUseMTRFont);
		addDrawableChild(buttonShowAnnouncementMessages);
		addDrawableChild(buttonUseTTSAnnouncements);
		addDrawableChild(buttonHideSpecialRailColors);
		addDrawableChild(buttonHideTranslucentParts);
		addDrawableChild(buttonUseDynamicFPS);
		addDrawableChild(sliderTrackTextureOffset);
		addDrawableChild(sliderDynamicTextureResolution);
		addDrawableChild(buttonSupportPatreon);
	}

	@Override
	public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
		try {
			renderBackground(matrices);
			drawCenteredString(matrices, font, new TranslatableComponent("gui.mtr.mtr_options"), width / 2, TEXT_PADDING, ARGB_WHITE);

			final int yStart1 = SQUARE_SIZE + TEXT_PADDING / 2;
			int i = 1;
			drawString(matrices, font, new TranslatableComponent("options.mtr.use_mtr_font"), SQUARE_SIZE, yStart1, ARGB_WHITE);
			drawString(matrices, font, new TranslatableComponent("options.mtr.show_announcement_messages"), SQUARE_SIZE, BUTTON_HEIGHT * (i++) + yStart1, ARGB_WHITE);
			drawString(matrices, font, new TranslatableComponent("options.mtr.use_tts_announcements"), SQUARE_SIZE, BUTTON_HEIGHT * (i++) + yStart1, ARGB_WHITE);
			drawString(matrices, font, new TranslatableComponent("options.mtr.hide_special_rail_colors"), SQUARE_SIZE, BUTTON_HEIGHT * (i++) + yStart1, ARGB_WHITE);
			drawString(matrices, font, new TranslatableComponent("options.mtr.hide_translucent_parts"), SQUARE_SIZE, BUTTON_HEIGHT * (i++) + yStart1, ARGB_WHITE);
			drawString(matrices, font, new TranslatableComponent("options.mtr.use_dynamic_fps"), SQUARE_SIZE, BUTTON_HEIGHT * (i++) + yStart1, ARGB_WHITE);
			drawString(matrices, font, new TranslatableComponent("options.mtr.track_texture_offset"), SQUARE_SIZE, BUTTON_HEIGHT * (i++) + yStart1, ARGB_WHITE);
			drawString(matrices, font, new TranslatableComponent("options.mtr.dynamic_texture_resolution"), SQUARE_SIZE, BUTTON_HEIGHT * (i++) + yStart1, ARGB_WHITE);
			drawString(matrices, font, new TranslatableComponent("options.mtr.support_patreon"), SQUARE_SIZE, BUTTON_HEIGHT * (i++) + yStart1, ARGB_WHITE);

			final int yStart2 = BUTTON_HEIGHT * (i + 1) + yStart1;
			String tierTitle = "";
			int y = 0;
			int x = 0;
			int maxWidth = 0;
			for (final Patreon patreon : Config.PATREON_LIST) {
				if (!patreon.tierTitle.equals(tierTitle)) {
					x += maxWidth + TEXT_PADDING;
					y = 0;
					final Component text = new TextComponent(patreon.tierTitle);
					maxWidth = font.width(text);
					drawString(matrices, font, text, SQUARE_SIZE - TEXT_PADDING + x, yStart2, patreon.tierColor);
				} else if (y + yStart2 + TEXT_HEIGHT + SQUARE_SIZE > height) {
					x += maxWidth + TEXT_PADDING;
					y = 0;
					maxWidth = 0;
				}

				tierTitle = patreon.tierTitle;
				final Component text = patreon.tierAmount < 1000 ? new TranslatableComponent("options.mtr.anonymous") : new TextComponent(patreon.name);
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
		ClientData.DATA_CACHE.sync();
	}

	private static void setButtonText(Button button, boolean state) {
		button.setMessage(new TranslatableComponent(state ? "options.mtr.on" : "options.mtr.off"));
	}
}
