package org.mtr.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import org.mtr.Patreon;
import org.mtr.client.DynamicTextureCache;
import org.mtr.client.IDrawing;
import org.mtr.config.Client;
import org.mtr.config.Config;
import org.mtr.data.IGui;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.widget.ShorterSliderWidget;

import javax.annotation.Nullable;

public class ConfigScreen extends MTRScreenBase implements IGui {

	private final Client client = Config.getClient();

	private final ButtonWidget buttonShowAnnouncementMessages;
	private final ButtonWidget buttonUseTTSAnnouncements;
	private final ButtonWidget buttonHideTranslucentParts;
	private final ButtonWidget buttonLanguageOptions;
	private final ShorterSliderWidget sliderDynamicTextureResolution;
	private final ShorterSliderWidget sliderTrainOscillationMultiplier;
	private final ButtonWidget buttonDefaultRail3D;
	private final ButtonWidget buttonUseMTRFont;
	private final ButtonWidget buttonDisableShadowsForShaders;
	private final ButtonWidget buttonSupportPatreon;

	private static final int BUTTON_WIDTH = 60;
	private static final int BUTTON_HEIGHT = TEXT_HEIGHT + TEXT_PADDING;

	public ConfigScreen(@Nullable Screen previousScreen) {
		super(previousScreen);

		buttonShowAnnouncementMessages = ButtonWidget.builder(Text.empty(), button -> {
			client.toggleChatAnnouncements();
			setButtonText(button, client.getChatAnnouncements());
		}).build();
		buttonUseTTSAnnouncements = ButtonWidget.builder(Text.empty(), button -> {
			client.toggleTextToSpeechAnnouncements();
			setButtonText(button, client.getTextToSpeechAnnouncements());
		}).build();
		buttonHideTranslucentParts = ButtonWidget.builder(Text.empty(), button -> {
			client.toggleHideTranslucentParts();
			setButtonText(button, client.getHideTranslucentParts());
		}).build();
		buttonLanguageOptions = ButtonWidget.builder(Text.empty(), button -> {
			client.cycleLanguageDisplay();
			button.setMessage(client.getLanguageDisplay().translationKey.getText());
		}).build();
		sliderDynamicTextureResolution = new ShorterSliderWidget(0, 0, Client.DYNAMIC_RESOLUTION_COUNT - 1, String::valueOf, null);
		sliderTrainOscillationMultiplier = new ShorterSliderWidget(0, 0, Client.TRAIN_OSCILLATION_COUNT, i -> (i * 10) + "%", null);
		buttonDefaultRail3D = ButtonWidget.builder(Text.empty(), button -> {
			client.toggleDefaultRail3D();
			setButtonText(button, client.getDefaultRail3D());
		}).build();
		buttonUseMTRFont = ButtonWidget.builder(Text.empty(), button -> {
			client.toggleUseMTRFont();
			setButtonText(button, client.getUseMTRFont());
		}).build();
		buttonDisableShadowsForShaders = ButtonWidget.builder(Text.empty(), button -> {
			client.toggleDisableShadowsForShaders();
			setButtonText(button, client.getDisableShadowsForShaders());
		}).build();
		buttonSupportPatreon = ButtonWidget.builder(Text.empty(), button -> Util.getOperatingSystem().open("https://www.patreon.com/minecraft_transit_railway")).build();
	}

	@Override
	protected void init() {
		super.init();

		int i = 1;
		IDrawing.setPositionAndWidth(buttonShowAnnouncementMessages, width - SQUARE_SIZE - BUTTON_WIDTH, SQUARE_SIZE, BUTTON_WIDTH);
		IDrawing.setPositionAndWidth(buttonUseTTSAnnouncements, width - SQUARE_SIZE - BUTTON_WIDTH, BUTTON_HEIGHT * (i++) + SQUARE_SIZE, BUTTON_WIDTH);
		IDrawing.setPositionAndWidth(buttonHideTranslucentParts, width - SQUARE_SIZE - BUTTON_WIDTH, BUTTON_HEIGHT * (i++) + SQUARE_SIZE, BUTTON_WIDTH);
		IDrawing.setPositionAndWidth(buttonLanguageOptions, width - SQUARE_SIZE - BUTTON_WIDTH, BUTTON_HEIGHT * (i++) + SQUARE_SIZE, BUTTON_WIDTH);
		IDrawing.setPositionAndWidth(sliderDynamicTextureResolution, width - SQUARE_SIZE - BUTTON_WIDTH, BUTTON_HEIGHT * (i++) + SQUARE_SIZE, BUTTON_WIDTH - TEXT_PADDING - textRenderer.getWidth("100%"));
		IDrawing.setPositionAndWidth(sliderTrainOscillationMultiplier, width - SQUARE_SIZE - BUTTON_WIDTH, BUTTON_HEIGHT * (i++) + SQUARE_SIZE, BUTTON_WIDTH - TEXT_PADDING - textRenderer.getWidth("100%"));
		IDrawing.setPositionAndWidth(buttonDefaultRail3D, width - SQUARE_SIZE - BUTTON_WIDTH, BUTTON_HEIGHT * (i++) + SQUARE_SIZE, BUTTON_WIDTH);
		IDrawing.setPositionAndWidth(buttonUseMTRFont, width - SQUARE_SIZE - BUTTON_WIDTH, BUTTON_HEIGHT * (i++) + SQUARE_SIZE, BUTTON_WIDTH);
		IDrawing.setPositionAndWidth(buttonDisableShadowsForShaders, width - SQUARE_SIZE - BUTTON_WIDTH, BUTTON_HEIGHT * (i++) + SQUARE_SIZE, BUTTON_WIDTH);
		IDrawing.setPositionAndWidth(buttonSupportPatreon, width - SQUARE_SIZE - BUTTON_WIDTH, BUTTON_HEIGHT * i + SQUARE_SIZE, BUTTON_WIDTH);

		buttonShowAnnouncementMessages.setHeight(BUTTON_HEIGHT);
		buttonUseTTSAnnouncements.setHeight(BUTTON_HEIGHT);
		buttonHideTranslucentParts.setHeight(BUTTON_HEIGHT);
		buttonLanguageOptions.setHeight(BUTTON_HEIGHT);
		buttonDefaultRail3D.setHeight(BUTTON_HEIGHT);
		buttonUseMTRFont.setHeight(BUTTON_HEIGHT);
		buttonDisableShadowsForShaders.setHeight(BUTTON_HEIGHT);
		buttonSupportPatreon.setHeight(BUTTON_HEIGHT);

		setButtonText(buttonShowAnnouncementMessages, client.getChatAnnouncements());
		setButtonText(buttonUseTTSAnnouncements, client.getTextToSpeechAnnouncements());
		setButtonText(buttonHideTranslucentParts, client.getHideTranslucentParts());
		setButtonText(buttonDefaultRail3D, client.getDefaultRail3D());
		setButtonText(buttonUseMTRFont, client.getUseMTRFont());
		setButtonText(buttonDisableShadowsForShaders, client.getDisableShadowsForShaders());

		buttonLanguageOptions.setMessage(client.getLanguageDisplay().translationKey.getText());
		sliderDynamicTextureResolution.setHeight(BUTTON_HEIGHT);
		sliderDynamicTextureResolution.setValue(client.getDynamicTextureResolution());
		sliderTrainOscillationMultiplier.setHeight(BUTTON_HEIGHT);
		sliderTrainOscillationMultiplier.setValue((int) (client.getVehicleOscillationMultiplier() * 10));
		buttonDefaultRail3D.active = true;
		buttonSupportPatreon.setMessage(TranslationProvider.GUI_MTR_SUPPORT.getText());

		addDrawableChild(buttonShowAnnouncementMessages);
		addDrawableChild(buttonUseTTSAnnouncements);
		addDrawableChild(buttonHideTranslucentParts);
		addDrawableChild(buttonLanguageOptions);
		addDrawableChild(sliderDynamicTextureResolution);
		addDrawableChild(sliderTrainOscillationMultiplier);
		addDrawableChild(buttonDefaultRail3D);
		addDrawableChild(buttonUseMTRFont);
		addDrawableChild(buttonDisableShadowsForShaders);
		addDrawableChild(buttonSupportPatreon);
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);
		context.drawCenteredTextWithShadow(textRenderer, TranslationProvider.GUI_MTR_MTR_OPTIONS.getMutableText(), width / 2, TEXT_PADDING, ARGB_WHITE);

		final int yStart1 = SQUARE_SIZE + TEXT_PADDING / 2;
		int i = 1;
		context.drawText(textRenderer, TranslationProvider.OPTIONS_MTR_SHOW_ANNOUNCEMENT_MESSAGES.getMutableText(), SQUARE_SIZE, yStart1, ARGB_WHITE, true);
		context.drawText(textRenderer, TranslationProvider.OPTIONS_MTR_USE_TTS_ANNOUNCEMENTS.getMutableText(), SQUARE_SIZE, BUTTON_HEIGHT * (i++) + yStart1, ARGB_WHITE, true);
		context.drawText(textRenderer, TranslationProvider.OPTIONS_MTR_HIDE_TRANSLUCENT_PARTS.getMutableText(), SQUARE_SIZE, BUTTON_HEIGHT * (i++) + yStart1, ARGB_WHITE, true);
		context.drawText(textRenderer, TranslationProvider.OPTIONS_MTR_LANGUAGE_OPTIONS.getMutableText(), SQUARE_SIZE, BUTTON_HEIGHT * (i++) + yStart1, ARGB_WHITE, true);
		context.drawText(textRenderer, TranslationProvider.OPTIONS_MTR_DYNAMIC_TEXTURE_RESOLUTION.getMutableText(), SQUARE_SIZE, BUTTON_HEIGHT * (i++) + yStart1, ARGB_WHITE, true);
		context.drawText(textRenderer, TranslationProvider.OPTIONS_MTR_VEHICLE_OSCILLATION_MULTIPLIER.getMutableText(), SQUARE_SIZE, BUTTON_HEIGHT * (i++) + yStart1, ARGB_WHITE, true);
		context.drawText(textRenderer, TranslationProvider.OPTIONS_MTR_DEFAULT_RAIL_3D.getMutableText(), SQUARE_SIZE, BUTTON_HEIGHT * (i++) + yStart1, ARGB_WHITE, true);
		context.drawText(textRenderer, TranslationProvider.OPTIONS_MTR_USE_MTR_FONT.getMutableText(), SQUARE_SIZE, BUTTON_HEIGHT * (i++) + yStart1, ARGB_WHITE, true);
		context.drawText(textRenderer, TranslationProvider.OPTIONS_MTR_DISABLE_SHADOWS_FOR_SHADERS.getMutableText(), SQUARE_SIZE, BUTTON_HEIGHT * (i++) + yStart1, ARGB_WHITE, true);
		context.drawText(textRenderer, TranslationProvider.OPTIONS_MTR_SUPPORT_PATREON.getMutableText(), SQUARE_SIZE, BUTTON_HEIGHT * (i++) + yStart1, ARGB_WHITE, true);

		final int yStart2 = BUTTON_HEIGHT * (i + 1) + yStart1;
		String tierTitle = "";
		int y = 0;
		int x = 0;
		int maxWidth = 0;
		for (final Patreon patreon : Patreon.PATREON_LIST) {
			if (!patreon.tierTitle.equals(tierTitle)) {
				x += maxWidth + TEXT_PADDING;
				y = 0;
				final String text = patreon.tierTitle;
				maxWidth = textRenderer.getWidth(text);
				context.drawText(textRenderer, text, SQUARE_SIZE - TEXT_PADDING + x, yStart2, patreon.tierColor, false);
			} else if (y + yStart2 + TEXT_HEIGHT + SQUARE_SIZE > height) {
				x += maxWidth + TEXT_PADDING;
				y = 0;
				maxWidth = 0;
			}

			tierTitle = patreon.tierTitle;
			final MutableText text = patreon.tierAmount < 1000 ? TranslationProvider.OPTIONS_MTR_ANONYMOUS.getMutableText() : Text.literal(patreon.name);
			maxWidth = Math.max(maxWidth, textRenderer.getWidth(text));
			context.drawText(textRenderer, text, SQUARE_SIZE - TEXT_PADDING + x, yStart2 + y + TEXT_HEIGHT + TEXT_PADDING, ARGB_LIGHT_GRAY, false);
			y += TEXT_HEIGHT + 2;
		}
	}

	@Override
	public void close() {
		super.close();
		client.setDynamicTextureResolution(sliderDynamicTextureResolution.getIntValue());
		client.setVehicleOscillationMultiplier(sliderTrainOscillationMultiplier.getIntValue() / 10.0);
		DynamicTextureCache.instance.reload();
		Config.save();
	}

	private static void setButtonText(ButtonWidget button, boolean state) {
		button.setMessage((state ? TranslationProvider.OPTIONS_MTR_ON : TranslationProvider.OPTIONS_MTR_OFF).getText());
	}
}
