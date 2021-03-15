package mtr.gui;

import mtr.config.Config;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;

public class ConfigScreen extends Screen implements IGui {

	private boolean useMTRFont;
	private boolean useTTSAnnouncements;

	private final ButtonWidget buttonUseMTRFont;
	private final ButtonWidget buttonUseTTSAnnouncements;

	private static final int BUTTON_WIDTH = 60;

	public ConfigScreen() {
		super(new LiteralText(""));

		buttonUseMTRFont = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new LiteralText(""), button -> {
			useMTRFont = Config.setUseMTRFont(!useMTRFont);
			button.setMessage(new LiteralText(String.valueOf(useMTRFont)));
		});
		buttonUseTTSAnnouncements = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new LiteralText(""), button -> {
			useTTSAnnouncements = Config.setUseTTSAnnouncements(!useTTSAnnouncements);
			button.setMessage(new LiteralText(String.valueOf(useTTSAnnouncements)));
		});
	}

	@Override
	protected void init() {
		super.init();
		Config.refreshProperties();
		useMTRFont = Config.useMTRFont();
		useTTSAnnouncements = Config.useTTSAnnouncements();

		IGui.setPositionAndWidth(buttonUseMTRFont, width - SQUARE_SIZE - BUTTON_WIDTH, SQUARE_SIZE * 2 - TEXT_PADDING, BUTTON_WIDTH);
		IGui.setPositionAndWidth(buttonUseTTSAnnouncements, width - SQUARE_SIZE - BUTTON_WIDTH, SQUARE_SIZE * 3 - TEXT_PADDING, BUTTON_WIDTH);
		buttonUseMTRFont.setMessage(new LiteralText(String.valueOf(useMTRFont)));
		buttonUseTTSAnnouncements.setMessage(new LiteralText(String.valueOf(useTTSAnnouncements)));

		addButton(buttonUseMTRFont);
		addButton(buttonUseTTSAnnouncements);
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		try {
			renderBackground(matrices);
			drawCenteredText(matrices, textRenderer, new TranslatableText("gui.mtr.mtr_options"), width / 2, SQUARE_SIZE, ARGB_WHITE);
			drawTextWithShadow(matrices, textRenderer, new TranslatableText("gui.mtr.use_mtr_font"), SQUARE_SIZE, SQUARE_SIZE * 2, ARGB_WHITE);
			drawTextWithShadow(matrices, textRenderer, new TranslatableText("gui.mtr.use_tts_announcements"), SQUARE_SIZE, SQUARE_SIZE * 3, ARGB_WHITE);
			super.render(matrices, mouseX, mouseY, delta);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
