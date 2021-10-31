package mtr.gui;

import mtr.config.Config;
import mtr.data.IGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;

public class ConfigScreen extends Screen implements IGui {

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

	private static final int BUTTON_WIDTH = 60;

	public ConfigScreen() {
		super(new LiteralText(""));

		buttonUseMTRFont = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new LiteralText(""), button -> {
			useMTRFont = Config.setUseMTRFont(!useMTRFont);
			button.setMessage(new LiteralText(String.valueOf(useMTRFont)));
		});
		buttonShowAnnouncementMessages = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new LiteralText(""), button -> {
			showAnnouncementMessages = Config.setShowAnnouncementMessages(!showAnnouncementMessages);
			button.setMessage(new LiteralText(String.valueOf(showAnnouncementMessages)));
		});
		buttonUseTTSAnnouncements = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new LiteralText(""), button -> {
			useTTSAnnouncements = Config.setUseTTSAnnouncements(!useTTSAnnouncements);
			button.setMessage(new LiteralText(String.valueOf(useTTSAnnouncements)));
		});
		buttonHideSpecialRailColors = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new LiteralText(""), button -> {
			hideSpecialRailColors = Config.setHideSpecialRailColors(!hideSpecialRailColors);
			button.setMessage(new LiteralText(String.valueOf(hideSpecialRailColors)));
		});
		buttonHideTranslucentParts = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new LiteralText(""), button -> {
			hideTranslucentParts = Config.setHideTranslucentParts(!hideTranslucentParts);
			button.setMessage(new LiteralText(String.valueOf(hideTranslucentParts)));
		});
		buttonUseDynamicFPS = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new LiteralText(""), button -> {
			useDynamicFPS = Config.setUseDynamicFPS(!useDynamicFPS);
			button.setMessage(new LiteralText(String.valueOf(useDynamicFPS)));
		});
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
		buttonUseMTRFont.setMessage(new LiteralText(String.valueOf(useMTRFont)));
		buttonShowAnnouncementMessages.setMessage(new LiteralText(String.valueOf(showAnnouncementMessages)));
		buttonUseTTSAnnouncements.setMessage(new LiteralText(String.valueOf(useTTSAnnouncements)));
		buttonHideSpecialRailColors.setMessage(new LiteralText(String.valueOf(hideSpecialRailColors)));
		buttonHideTranslucentParts.setMessage(new LiteralText(String.valueOf(hideTranslucentParts)));
		buttonUseDynamicFPS.setMessage(new LiteralText(String.valueOf(useDynamicFPS)));

		addButton(buttonUseMTRFont);
		addButton(buttonShowAnnouncementMessages);
		addButton(buttonUseTTSAnnouncements);
		addButton(buttonHideSpecialRailColors);
		addButton(buttonHideTranslucentParts);
		addButton(buttonUseDynamicFPS);
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
			super.render(matrices, mouseX, mouseY, delta);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
