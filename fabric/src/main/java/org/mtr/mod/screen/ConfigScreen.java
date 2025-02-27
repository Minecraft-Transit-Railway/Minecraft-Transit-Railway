package org.mtr.mod.screen;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.*;
import org.mtr.mod.Init;
import org.mtr.mod.Keys;
import org.mtr.mod.Patreon;
import org.mtr.mod.client.DynamicTextureCache;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.config.Client;
import org.mtr.mod.config.Config;
import org.mtr.mod.data.IGui;
import org.mtr.mod.generated.lang.TranslationProvider;

import javax.annotation.Nullable;

public class ConfigScreen extends MTRScreenBase implements IGui {

	private final Client client = Config.getClient();

	private final ButtonWidgetExtension buttonShowAnnouncementMessages;
	private final ButtonWidgetExtension buttonUseTTSAnnouncements;
	private final ButtonWidgetExtension buttonHideTranslucentParts;
	private final ButtonWidgetExtension buttonLanguageOptions;
	private final WidgetShorterSlider sliderDynamicTextureResolution;
	private final WidgetShorterSlider sliderTrainOscillationMultiplier;
	private final ButtonWidgetExtension buttonDefaultRail3D;
	private final ButtonWidgetExtension buttonUseMTRFont;
	private final ButtonWidgetExtension buttonDisableShadowsForShaders;
	private final ButtonWidgetExtension buttonSupportPatreon;

	private static final Identifier HEADER_LOGO = new Identifier("mtr:textures/block/sign/logo.png");
	private static final int HEADER_LOGO_SIZE = 40;
	private static final int BUTTON_WIDTH = 60;
	private static final int BUTTON_HEIGHT = TEXT_HEIGHT * 2;

	public ConfigScreen(@Nullable Screen previousScreen) {
		super(previousScreen);

		buttonShowAnnouncementMessages = new ButtonWidgetExtension(0, 0, 0, BUTTON_HEIGHT, TextHelper.literal(""), button -> {
			client.toggleChatAnnouncements();
			setButtonText(button, client.getChatAnnouncements());
		});
		buttonUseTTSAnnouncements = new ButtonWidgetExtension(0, 0, 0, BUTTON_HEIGHT, TextHelper.literal(""), button -> {
			client.toggleTextToSpeechAnnouncements();
			setButtonText(button, client.getTextToSpeechAnnouncements());
		});
		buttonHideTranslucentParts = new ButtonWidgetExtension(0, 0, 0, BUTTON_HEIGHT, TextHelper.literal(""), button -> {
			client.toggleHideTranslucentParts();
			setButtonText(button, client.getHideTranslucentParts());
		});
		buttonLanguageOptions = new ButtonWidgetExtension(0, 0, 0, BUTTON_HEIGHT, TextHelper.literal(""), button -> {
			client.cycleLanguageDisplay();
			button.setMessage(client.getLanguageDisplay().translationKey.getText());
		});
		sliderDynamicTextureResolution = new WidgetShorterSlider(0, 0, Client.DYNAMIC_RESOLUTION_COUNT - 1, String::valueOf, null);
		sliderTrainOscillationMultiplier = new WidgetShorterSlider(0, 0, Client.TRAIN_OSCILLATION_COUNT, i -> (i * 10) + "%", null);
		buttonDefaultRail3D = new ButtonWidgetExtension(0, 0, 0, BUTTON_HEIGHT, TextHelper.literal(""), button -> {
			client.toggleDefaultRail3D();
			setButtonText(button, client.getDefaultRail3D());
		});
		buttonUseMTRFont = new ButtonWidgetExtension(0, 0, 0, BUTTON_HEIGHT, TextHelper.literal(""), button -> {
			client.toggleUseMTRFont();
			setButtonText(button, client.getUseMTRFont());
		});
		buttonDisableShadowsForShaders = new ButtonWidgetExtension(0, 0, 0, BUTTON_HEIGHT, TextHelper.literal(""), button -> {
			client.toggleDisableShadowsForShaders();
			setButtonText(button, client.getDisableShadowsForShaders());
		});
		buttonSupportPatreon = new ButtonWidgetExtension(0, 0, 0, BUTTON_HEIGHT, TextHelper.literal(""), button -> Util.getOperatingSystem().open("https://www.patreon.com/minecraft_transit_railway"));
	}

	@Override
	protected void init2() {
		super.init2();

		int startY = TEXT_PADDING;
		int i = 1;
		IDrawing.setPositionAndWidth(buttonShowAnnouncementMessages, width - SQUARE_SIZE - BUTTON_WIDTH, startY + BUTTON_HEIGHT * (i++) + SQUARE_SIZE, BUTTON_WIDTH);
		IDrawing.setPositionAndWidth(buttonUseTTSAnnouncements, width - SQUARE_SIZE - BUTTON_WIDTH, startY + BUTTON_HEIGHT * (i++) + SQUARE_SIZE, BUTTON_WIDTH);
		IDrawing.setPositionAndWidth(buttonHideTranslucentParts, width - SQUARE_SIZE - BUTTON_WIDTH, startY + BUTTON_HEIGHT * (i++) + SQUARE_SIZE, BUTTON_WIDTH);
		IDrawing.setPositionAndWidth(buttonLanguageOptions, width - SQUARE_SIZE - BUTTON_WIDTH, startY + BUTTON_HEIGHT * (i++) + SQUARE_SIZE, BUTTON_WIDTH);
		IDrawing.setPositionAndWidth(sliderDynamicTextureResolution, width - SQUARE_SIZE - BUTTON_WIDTH, startY + BUTTON_HEIGHT * (i++) + SQUARE_SIZE, BUTTON_WIDTH - TEXT_PADDING - GraphicsHolder.getTextWidth("100%"));
		IDrawing.setPositionAndWidth(sliderTrainOscillationMultiplier, width - SQUARE_SIZE - BUTTON_WIDTH, startY + BUTTON_HEIGHT * (i++) + SQUARE_SIZE, BUTTON_WIDTH - TEXT_PADDING - GraphicsHolder.getTextWidth("100%"));
		IDrawing.setPositionAndWidth(buttonDefaultRail3D, width - SQUARE_SIZE - BUTTON_WIDTH, startY + BUTTON_HEIGHT * (i++) + SQUARE_SIZE, BUTTON_WIDTH);
		IDrawing.setPositionAndWidth(buttonUseMTRFont, width - SQUARE_SIZE - BUTTON_WIDTH, startY + BUTTON_HEIGHT * (i++) + SQUARE_SIZE, BUTTON_WIDTH);
		IDrawing.setPositionAndWidth(buttonDisableShadowsForShaders, width - SQUARE_SIZE - BUTTON_WIDTH, startY + BUTTON_HEIGHT * (i++) + SQUARE_SIZE, BUTTON_WIDTH);
		IDrawing.setPositionAndWidth(buttonSupportPatreon, width - SQUARE_SIZE - BUTTON_WIDTH, startY + BUTTON_HEIGHT * i + SQUARE_SIZE, BUTTON_WIDTH);
		setButtonText(new ButtonWidget(buttonShowAnnouncementMessages), client.getChatAnnouncements());
		setButtonText(new ButtonWidget(buttonUseTTSAnnouncements), client.getTextToSpeechAnnouncements());
		setButtonText(new ButtonWidget(buttonHideTranslucentParts), client.getHideTranslucentParts());
		setButtonText(new ButtonWidget(buttonDefaultRail3D), client.getDefaultRail3D());
		setButtonText(new ButtonWidget(buttonUseMTRFont), client.getUseMTRFont());
		setButtonText(new ButtonWidget(buttonDisableShadowsForShaders), client.getDisableShadowsForShaders());
		buttonLanguageOptions.setMessage2(client.getLanguageDisplay().translationKey.getText());
		sliderDynamicTextureResolution.setHeight(BUTTON_HEIGHT);
		sliderDynamicTextureResolution.setValue(client.getDynamicTextureResolution());
		sliderTrainOscillationMultiplier.setHeight(BUTTON_HEIGHT);
		sliderTrainOscillationMultiplier.setValue((int)(client.getVehicleOscillationMultiplier() * 10));
		buttonDefaultRail3D.active = OptimizedRenderer.hasOptimizedRendering();
		buttonSupportPatreon.setMessage2(TranslationProvider.GUI_MTR_SUPPORT.getText());

		addChild(new ClickableWidget(buttonShowAnnouncementMessages));
		addChild(new ClickableWidget(buttonUseTTSAnnouncements));
		addChild(new ClickableWidget(buttonHideTranslucentParts));
		addChild(new ClickableWidget(buttonLanguageOptions));
		addChild(new ClickableWidget(sliderDynamicTextureResolution));
		addChild(new ClickableWidget(sliderTrainOscillationMultiplier));
		addChild(new ClickableWidget(buttonDefaultRail3D));
		addChild(new ClickableWidget(buttonUseMTRFont));
		addChild(new ClickableWidget(buttonDisableShadowsForShaders));
		addChild(new ClickableWidget(buttonSupportPatreon));
	}

	@Override
	public void render(GraphicsHolder graphicsHolder, int mouseX, int mouseY, float delta) {
		try {
			renderBackground(graphicsHolder);

			drawHeader(graphicsHolder);

			final int yStart1 = SQUARE_SIZE + TEXT_PADDING + TEXT_PADDING / 2;
			int i = 1;
			graphicsHolder.drawText(TranslationProvider.OPTIONS_MTR_SHOW_ANNOUNCEMENT_MESSAGES.getMutableText(), SQUARE_SIZE, BUTTON_HEIGHT * (i++) + yStart1, ARGB_WHITE, false, GraphicsHolder.getDefaultLight());
			graphicsHolder.drawText(TranslationProvider.OPTIONS_MTR_USE_TTS_ANNOUNCEMENTS.getMutableText(), SQUARE_SIZE, BUTTON_HEIGHT * (i++) + yStart1, ARGB_WHITE, false, GraphicsHolder.getDefaultLight());
			graphicsHolder.drawText(TranslationProvider.OPTIONS_MTR_HIDE_TRANSLUCENT_PARTS.getMutableText(), SQUARE_SIZE, BUTTON_HEIGHT * (i++) + yStart1, ARGB_WHITE, false, GraphicsHolder.getDefaultLight());
			graphicsHolder.drawText(TranslationProvider.OPTIONS_MTR_LANGUAGE_OPTIONS.getMutableText(), SQUARE_SIZE, BUTTON_HEIGHT * (i++) + yStart1, ARGB_WHITE, false, GraphicsHolder.getDefaultLight());
			graphicsHolder.drawText(TranslationProvider.OPTIONS_MTR_DYNAMIC_TEXTURE_RESOLUTION.getMutableText(), SQUARE_SIZE, BUTTON_HEIGHT * (i++) + yStart1, ARGB_WHITE, false, GraphicsHolder.getDefaultLight());
			graphicsHolder.drawText(TranslationProvider.OPTIONS_MTR_VEHICLE_OSCILLATION_MULTIPLIER.getMutableText(), SQUARE_SIZE, BUTTON_HEIGHT * (i++) + yStart1, ARGB_WHITE, false, GraphicsHolder.getDefaultLight());
			graphicsHolder.drawText(TranslationProvider.OPTIONS_MTR_DEFAULT_RAIL_3D.getMutableText(), SQUARE_SIZE, BUTTON_HEIGHT * (i++) + yStart1, ARGB_WHITE, false, GraphicsHolder.getDefaultLight());
			graphicsHolder.drawText(TranslationProvider.OPTIONS_MTR_USE_MTR_FONT.getMutableText(), SQUARE_SIZE, BUTTON_HEIGHT * (i++) + yStart1, ARGB_WHITE, false, GraphicsHolder.getDefaultLight());
			graphicsHolder.drawText(TranslationProvider.OPTIONS_MTR_DISABLE_SHADOWS_FOR_SHADERS.getMutableText(), SQUARE_SIZE, BUTTON_HEIGHT * (i++) + yStart1, ARGB_WHITE, false, GraphicsHolder.getDefaultLight());
			graphicsHolder.drawText(TranslationProvider.OPTIONS_MTR_SUPPORT_PATREON.getMutableText(), SQUARE_SIZE, BUTTON_HEIGHT * (i++) + yStart1, 0xFFFFFF66, false, GraphicsHolder.getDefaultLight());

			final int yStart2 = BUTTON_HEIGHT * (i + 1) + yStart1;
			String tierTitle = "";
			int y = 0;
			int x = 0;
			int maxWidth = 0;
			for (final Patreon patreon : Patreon.PATREON_LIST) {
				if (!patreon.tierTitle.equals(tierTitle)) {
					x += maxWidth + TEXT_PADDING;
					y = 0;
					final MutableText text = TextHelper.literal(patreon.tierTitle);
					maxWidth = GraphicsHolder.getTextWidth(text);
					graphicsHolder.drawText(text, SQUARE_SIZE - TEXT_PADDING + x, yStart2, patreon.tierColor, false, GraphicsHolder.getDefaultLight());
				} else if (y + yStart2 + TEXT_HEIGHT + SQUARE_SIZE > height) {
					x += maxWidth + TEXT_PADDING;
					y = 0;
					maxWidth = 0;
				}

				tierTitle = patreon.tierTitle;
				final MutableText text = patreon.tierAmount < 1000 ? TranslationProvider.OPTIONS_MTR_ANONYMOUS.getMutableText() : TextHelper.literal(patreon.name);
				maxWidth = Math.max(maxWidth, GraphicsHolder.getTextWidth(text));
				graphicsHolder.drawText(text, SQUARE_SIZE - TEXT_PADDING + x, yStart2 + y + TEXT_HEIGHT + TEXT_PADDING, ARGB_LIGHT_GRAY, false, GraphicsHolder.getDefaultLight());
				y += TEXT_HEIGHT + 2;
			}

			super.render(graphicsHolder, mouseX, mouseY, delta);
		} catch (Exception e) {
			Init.LOGGER.error("", e);
		}
	}

	@Override
	public void onClose2() {
		super.onClose2();
		client.setDynamicTextureResolution(sliderDynamicTextureResolution.getIntValue());
		client.setVehicleOscillationMultiplier(sliderTrainOscillationMultiplier.getIntValue() / 10.0);
		DynamicTextureCache.instance.reload();
		Config.save();
	}

	private void drawHeader(GraphicsHolder graphicsHolder) {
		MutableText titleText = TranslationProvider.GUI_MTR_BRAND.getMutableText();

		// Logo
		graphicsHolder.push();
		graphicsHolder.translate((getWidthMapped() - (GraphicsHolder.getTextWidth(titleText) * 1.5f) - HEADER_LOGO_SIZE - TEXT_PADDING) / 2, 0, 0);
		GuiDrawing guiDrawing = new GuiDrawing(graphicsHolder);
		guiDrawing.beginDrawingTexture(HEADER_LOGO);
		guiDrawing.drawTexture(0, 0, HEADER_LOGO_SIZE, HEADER_LOGO_SIZE, 0, 0, 1 ,1);
		guiDrawing.finishDrawingTexture();

		graphicsHolder.translate(HEADER_LOGO_SIZE, 0, 0);
		graphicsHolder.translate(TEXT_PADDING, 0, 0);

		// Brand Name
		graphicsHolder.push();
		graphicsHolder.scale(1.5f, 1.5f, 1.5f);
		graphicsHolder.drawText(titleText, 0, TEXT_PADDING, ARGB_WHITE, true, GraphicsHolder.getDefaultLight());
		graphicsHolder.pop();

		// Version
		graphicsHolder.drawText(Keys.MOD_VERSION, 0, (TEXT_PADDING * 4), ARGB_WHITE, true, GraphicsHolder.getDefaultLight());
		graphicsHolder.pop();
	}

	private static void setButtonText(ButtonWidget button, boolean state) {
		button.setMessage((state ? TranslationProvider.OPTIONS_MTR_ON : TranslationProvider.OPTIONS_MTR_OFF).getText());
	}
}
