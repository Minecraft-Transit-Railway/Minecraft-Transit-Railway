package org.mtr.mod.screen;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.*;
import org.mtr.mod.Init;
import org.mtr.mod.Patreon;
import org.mtr.mod.client.DynamicTextureCache;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.config.Client;
import org.mtr.mod.config.Config;
import org.mtr.mod.data.IGui;

import java.util.Locale;

public class ConfigScreen extends ScreenExtension implements IGui {

	private final Client client = Config.getClient();

	private final ButtonWidgetExtension buttonShowAnnouncementMessages;
	private final ButtonWidgetExtension buttonUseTTSAnnouncements;
	private final ButtonWidgetExtension buttonHideTranslucentParts;
	private final ButtonWidgetExtension buttonLanguageOptions;
	private final WidgetShorterSlider sliderDynamicTextureResolution;
	private final ButtonWidgetExtension buttonDefaultRail3D;
	private final ButtonWidgetExtension buttonDisableShadowsForShaders;
	private final ButtonWidgetExtension buttonSupportPatreon;

	private static final int BUTTON_WIDTH = 60;
	private static final int BUTTON_HEIGHT = TEXT_HEIGHT + TEXT_PADDING;

	public ConfigScreen() {
		super();

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
			button.setMessage(new Text(TextHelper.translatable("options.mtr.language_options_" + client.getLanguageDisplay().toString().toLowerCase(Locale.ENGLISH)).data));
		});
		sliderDynamicTextureResolution = new WidgetShorterSlider(0, 0, Client.DYNAMIC_RESOLUTION_COUNT - 1, String::valueOf, null);
		buttonDefaultRail3D = new ButtonWidgetExtension(0, 0, 0, BUTTON_HEIGHT, TextHelper.literal(""), button -> {
			client.toggleDefaultRail3D();
			setButtonText(button, client.getDefaultRail3D());
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

		int i = 1;
		IDrawing.setPositionAndWidth(buttonShowAnnouncementMessages, width - SQUARE_SIZE - BUTTON_WIDTH, SQUARE_SIZE, BUTTON_WIDTH);
		IDrawing.setPositionAndWidth(buttonUseTTSAnnouncements, width - SQUARE_SIZE - BUTTON_WIDTH, BUTTON_HEIGHT * (i++) + SQUARE_SIZE, BUTTON_WIDTH);
		IDrawing.setPositionAndWidth(buttonHideTranslucentParts, width - SQUARE_SIZE - BUTTON_WIDTH, BUTTON_HEIGHT * (i++) + SQUARE_SIZE, BUTTON_WIDTH);
		IDrawing.setPositionAndWidth(buttonLanguageOptions, width - SQUARE_SIZE - BUTTON_WIDTH, BUTTON_HEIGHT * (i++) + SQUARE_SIZE, BUTTON_WIDTH);
		IDrawing.setPositionAndWidth(sliderDynamicTextureResolution, width - SQUARE_SIZE - BUTTON_WIDTH, BUTTON_HEIGHT * (i++) + SQUARE_SIZE, BUTTON_WIDTH - TEXT_PADDING - GraphicsHolder.getTextWidth("100%"));
		IDrawing.setPositionAndWidth(buttonDefaultRail3D, width - SQUARE_SIZE - BUTTON_WIDTH, BUTTON_HEIGHT * (i++) + SQUARE_SIZE, BUTTON_WIDTH);
		IDrawing.setPositionAndWidth(buttonDisableShadowsForShaders, width - SQUARE_SIZE - BUTTON_WIDTH, BUTTON_HEIGHT * (i++) + SQUARE_SIZE, BUTTON_WIDTH);
		IDrawing.setPositionAndWidth(buttonSupportPatreon, width - SQUARE_SIZE - BUTTON_WIDTH, BUTTON_HEIGHT * i + SQUARE_SIZE, BUTTON_WIDTH);
		setButtonText(new ButtonWidget(buttonShowAnnouncementMessages), client.getChatAnnouncements());
		setButtonText(new ButtonWidget(buttonUseTTSAnnouncements), client.getTextToSpeechAnnouncements());
		setButtonText(new ButtonWidget(buttonHideTranslucentParts), client.getHideTranslucentParts());
		setButtonText(new ButtonWidget(buttonDefaultRail3D), client.getDefaultRail3D());
		setButtonText(new ButtonWidget(buttonDisableShadowsForShaders), client.getDisableShadowsForShaders());
		buttonLanguageOptions.setMessage2(new Text(TextHelper.translatable("options.mtr.language_options_" + client.getLanguageDisplay().toString().toLowerCase(Locale.ENGLISH)).data));
		sliderDynamicTextureResolution.setHeight(BUTTON_HEIGHT);
		sliderDynamicTextureResolution.setValue(client.getDynamicTextureResolution());
		buttonDefaultRail3D.active = OptimizedRenderer.hasOptimizedRendering();
		buttonSupportPatreon.setMessage2(new Text(TextHelper.translatable("gui.mtr.support").data));

		addChild(new ClickableWidget(buttonShowAnnouncementMessages));
		addChild(new ClickableWidget(buttonUseTTSAnnouncements));
		addChild(new ClickableWidget(buttonHideTranslucentParts));
		addChild(new ClickableWidget(buttonLanguageOptions));
		addChild(new ClickableWidget(sliderDynamicTextureResolution));
		addChild(new ClickableWidget(buttonDefaultRail3D));
		addChild(new ClickableWidget(buttonDisableShadowsForShaders));
		addChild(new ClickableWidget(buttonSupportPatreon));
	}

	@Override
	public void render(GraphicsHolder graphicsHolder, int mouseX, int mouseY, float delta) {
		try {
			renderBackground(graphicsHolder);
			graphicsHolder.drawCenteredText(TextHelper.translatable("gui.mtr.mtr_options"), width / 2, TEXT_PADDING, ARGB_WHITE);

			final int yStart1 = SQUARE_SIZE + TEXT_PADDING / 2;
			int i = 1;
			graphicsHolder.drawText(TextHelper.translatable("options.mtr.show_announcement_messages"), SQUARE_SIZE, yStart1, ARGB_WHITE, false, GraphicsHolder.getDefaultLight());
			graphicsHolder.drawText(TextHelper.translatable("options.mtr.use_tts_announcements"), SQUARE_SIZE, BUTTON_HEIGHT * (i++) + yStart1, ARGB_WHITE, false, GraphicsHolder.getDefaultLight());
			graphicsHolder.drawText(TextHelper.translatable("options.mtr.hide_translucent_parts"), SQUARE_SIZE, BUTTON_HEIGHT * (i++) + yStart1, ARGB_WHITE, false, GraphicsHolder.getDefaultLight());
			graphicsHolder.drawText(TextHelper.translatable("options.mtr.language_options"), SQUARE_SIZE, BUTTON_HEIGHT * (i++) + yStart1, ARGB_WHITE, false, GraphicsHolder.getDefaultLight());
			graphicsHolder.drawText(TextHelper.translatable("options.mtr.dynamic_texture_resolution"), SQUARE_SIZE, BUTTON_HEIGHT * (i++) + yStart1, ARGB_WHITE, false, GraphicsHolder.getDefaultLight());
			graphicsHolder.drawText(TextHelper.translatable("options.mtr.default_rail_3d"), SQUARE_SIZE, BUTTON_HEIGHT * (i++) + yStart1, ARGB_WHITE, false, GraphicsHolder.getDefaultLight());
			graphicsHolder.drawText(TextHelper.translatable("options.mtr.disable_shadows_for_shaders"), SQUARE_SIZE, BUTTON_HEIGHT * (i++) + yStart1, ARGB_WHITE, false, GraphicsHolder.getDefaultLight());
			graphicsHolder.drawText(TextHelper.translatable("options.mtr.support_patreon"), SQUARE_SIZE, BUTTON_HEIGHT * (i++) + yStart1, ARGB_WHITE, false, GraphicsHolder.getDefaultLight());

			final int yStart2 = BUTTON_HEIGHT * (i + 1) + yStart1;
			String tierTitle = "";
			int y = 0;
			int x = 0;
			int maxWidth = 0;
			for (final Patreon patreon : Patreon.LIST) {
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
				final MutableText text = patreon.tierAmount < 1000 ? TextHelper.translatable("options.mtr.anonymous") : TextHelper.literal(patreon.name);
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
		DynamicTextureCache.instance.reload();
		Config.save();
	}

	private static void setButtonText(ButtonWidget button, boolean state) {
		button.setMessage(new Text(TextHelper.translatable(state ? "options.mtr.on" : "options.mtr.off").data));
	}
}
