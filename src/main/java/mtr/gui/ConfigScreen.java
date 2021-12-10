package mtr.gui;

import minecraftmappings.ScreenMapper;
import mtr.Patreon;
import mtr.config.Config;
import mtr.data.IGui;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Util;

public class ConfigScreen extends ScreenMapper implements IGui {

	private boolean useMTRFont;
	private boolean showAnnouncementMessages;
	private boolean useTTSAnnouncements;
	private boolean hideSpecialRailColors;
	private boolean hideTranslucentParts;
	private boolean useDynamicFPS;

	private final ButtonWidget buttonUseMTRFont;
	private final ButtonWidget buttonShowAnnouncementMessages;
	private final ButtonWidget buttonUseTTSAnnouncements;
	private final ButtonWidget buttonHideSpecialRailColors;
	private final ButtonWidget buttonHideTranslucentParts;
	private final ButtonWidget buttonUseDynamicFPS;
	private final ButtonWidget buttonSupportPatreon;

	private static final int BUTTON_WIDTH = 60;

	public ConfigScreen() {
		super(new LiteralText(""));

		buttonUseMTRFont = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new LiteralText(""), button -> {
			useMTRFont = Config.setUseMTRFont(!useMTRFont);
			setButtonText(button, useMTRFont);
		});
		buttonShowAnnouncementMessages = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new LiteralText(""), button -> {
			showAnnouncementMessages = Config.setShowAnnouncementMessages(!showAnnouncementMessages);
			setButtonText(button, showAnnouncementMessages);
		});
		buttonUseTTSAnnouncements = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new LiteralText(""), button -> {
			useTTSAnnouncements = Config.setUseTTSAnnouncements(!useTTSAnnouncements);
			setButtonText(button, useTTSAnnouncements);
		});
		buttonHideSpecialRailColors = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new LiteralText(""), button -> {
			hideSpecialRailColors = Config.setHideSpecialRailColors(!hideSpecialRailColors);
			setButtonText(button, hideSpecialRailColors);
		});
		buttonHideTranslucentParts = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new LiteralText(""), button -> {
			hideTranslucentParts = Config.setHideTranslucentParts(!hideTranslucentParts);
			setButtonText(button, hideTranslucentParts);
		});
		buttonUseDynamicFPS = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new LiteralText(""), button -> {
			useDynamicFPS = Config.setUseDynamicFPS(!useDynamicFPS);
			setButtonText(button, useDynamicFPS);
		});
		buttonSupportPatreon = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new LiteralText(""), button -> Util.getOperatingSystem().open("https://www.patreon.com/minecraft_transit_railway"));
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

		IDrawing.setPositionAndWidth(buttonUseMTRFont, width - SQUARE_SIZE - BUTTON_WIDTH, SQUARE_SIZE * 2 - TEXT_PADDING, BUTTON_WIDTH);
		IDrawing.setPositionAndWidth(buttonShowAnnouncementMessages, width - SQUARE_SIZE - BUTTON_WIDTH, SQUARE_SIZE * 3 - TEXT_PADDING, BUTTON_WIDTH);
		IDrawing.setPositionAndWidth(buttonUseTTSAnnouncements, width - SQUARE_SIZE - BUTTON_WIDTH, SQUARE_SIZE * 4 - TEXT_PADDING, BUTTON_WIDTH);
		IDrawing.setPositionAndWidth(buttonHideSpecialRailColors, width - SQUARE_SIZE - BUTTON_WIDTH, SQUARE_SIZE * 5 - TEXT_PADDING, BUTTON_WIDTH);
		IDrawing.setPositionAndWidth(buttonHideTranslucentParts, width - SQUARE_SIZE - BUTTON_WIDTH, SQUARE_SIZE * 6 - TEXT_PADDING, BUTTON_WIDTH);
		IDrawing.setPositionAndWidth(buttonUseDynamicFPS, width - SQUARE_SIZE - BUTTON_WIDTH, SQUARE_SIZE * 7 - TEXT_PADDING, BUTTON_WIDTH);
		IDrawing.setPositionAndWidth(buttonSupportPatreon, width - SQUARE_SIZE - BUTTON_WIDTH, SQUARE_SIZE * 8 - TEXT_PADDING, BUTTON_WIDTH);
		setButtonText(buttonUseMTRFont, useMTRFont);
		setButtonText(buttonShowAnnouncementMessages, showAnnouncementMessages);
		setButtonText(buttonUseTTSAnnouncements, useTTSAnnouncements);
		setButtonText(buttonHideSpecialRailColors, hideSpecialRailColors);
		setButtonText(buttonHideTranslucentParts, hideTranslucentParts);
		setButtonText(buttonUseDynamicFPS, useDynamicFPS);
		buttonSupportPatreon.setMessage(new TranslatableText("gui.mtr.support"));

		addDrawableChild(buttonUseMTRFont);
		addDrawableChild(buttonShowAnnouncementMessages);
		addDrawableChild(buttonUseTTSAnnouncements);
		addDrawableChild(buttonHideSpecialRailColors);
		addDrawableChild(buttonHideTranslucentParts);
		addDrawableChild(buttonUseDynamicFPS);
		addDrawableChild(buttonSupportPatreon);
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		try {
			renderBackground(matrices);
			drawCenteredText(matrices, textRenderer, new TranslatableText("gui.mtr.mtr_options"), width / 2, SQUARE_SIZE, ARGB_WHITE);
			drawTextWithShadow(matrices, textRenderer, new TranslatableText("options.mtr.use_mtr_font"), SQUARE_SIZE, SQUARE_SIZE * 2, ARGB_WHITE);
			drawTextWithShadow(matrices, textRenderer, new TranslatableText("options.mtr.show_announcement_messages"), SQUARE_SIZE, SQUARE_SIZE * 3, ARGB_WHITE);
			drawTextWithShadow(matrices, textRenderer, new TranslatableText("options.mtr.use_tts_announcements"), SQUARE_SIZE, SQUARE_SIZE * 4, ARGB_WHITE);
			drawTextWithShadow(matrices, textRenderer, new TranslatableText("options.mtr.hide_special_rail_colors"), SQUARE_SIZE, SQUARE_SIZE * 5, ARGB_WHITE);
			drawTextWithShadow(matrices, textRenderer, new TranslatableText("options.mtr.hide_translucent_parts"), SQUARE_SIZE, SQUARE_SIZE * 6, ARGB_WHITE);
			drawTextWithShadow(matrices, textRenderer, new TranslatableText("options.mtr.use_dynamic_fps"), SQUARE_SIZE, SQUARE_SIZE * 7, ARGB_WHITE);
			drawTextWithShadow(matrices, textRenderer, new TranslatableText("options.mtr.support_patreon"), SQUARE_SIZE, SQUARE_SIZE * 8, ARGB_WHITE);

			final int yStart = SQUARE_SIZE * 9;
			String tierTitle = "";
			int y = 0;
			int x = 0;
			int maxWidth = 0;
			for (final Patreon patreon : Config.PATREON_LIST) {
				if (!patreon.tierTitle.equals(tierTitle)) {
					x += maxWidth + TEXT_PADDING;
					y = 0;
					final Text text = new LiteralText(patreon.tierTitle);
					maxWidth = textRenderer.getWidth(text);
					drawTextWithShadow(matrices, textRenderer, text, SQUARE_SIZE - TEXT_PADDING + x, yStart, patreon.tierColor);
				} else if (y + yStart + TEXT_HEIGHT + SQUARE_SIZE > height) {
					x += maxWidth + TEXT_PADDING;
					y = 0;
					maxWidth = 0;
				}

				tierTitle = patreon.tierTitle;
				final Text text = patreon.tierAmount < 1000 ? new TranslatableText("options.mtr.anonymous") : new LiteralText(patreon.name);
				maxWidth = Math.max(maxWidth, textRenderer.getWidth(text));
				drawTextWithShadow(matrices, textRenderer, text, SQUARE_SIZE - TEXT_PADDING + x, yStart + y + TEXT_HEIGHT + TEXT_PADDING, ARGB_LIGHT_GRAY);
				y += TEXT_HEIGHT;
			}

			super.render(matrices, mouseX, mouseY, delta);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void setButtonText(ButtonWidget button, boolean state) {
		button.setMessage(new TranslatableText(state ? "options.mtr.on" : "options.mtr.off"));
	}
}
