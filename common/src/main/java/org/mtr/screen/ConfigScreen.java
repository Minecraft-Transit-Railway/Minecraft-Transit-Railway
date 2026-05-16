package org.mtr.screen;

import gg.essential.elementa.components.UIContainer;
import gg.essential.elementa.components.UIWrappedText;
import gg.essential.elementa.constraints.PixelConstraint;
import gg.essential.elementa.constraints.RelativeConstraint;
import gg.essential.elementa.constraints.ScaleConstraint;
import gg.essential.elementa.constraints.SiblingConstraint;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.Util;
import org.jspecify.annotations.Nullable;
import org.mtr.client.DynamicTextureCache;
import org.mtr.config.Client;
import org.mtr.config.Config;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.tool.GuiHelper;
import org.mtr.widget.ButtonComponent;
import org.mtr.widget.NumberInputComponent;

import java.awt.*;

/**
 * Elementa configuration screen for client-side toggles and numeric options.
 */
public class ConfigScreen extends SingleTabBackgroundScreenBase {

	private final Client client = Config.getClient();
	private final NumberInputComponent dynamicTextureResolutionInput;
	private final NumberInputComponent trainOscillationInput;

	public ConfigScreen(@Nullable Screen previousScreenLegacy) {
		super(previousScreenLegacy, TranslationProvider.GUI_MTR_MTR_OPTIONS.getString());
		createToggleRow(TranslationProvider.OPTIONS_MTR_SHOW_ANNOUNCEMENT_MESSAGES.getString(), client.getChatAnnouncements(), client::toggleChatAnnouncements);
		createToggleRow(TranslationProvider.OPTIONS_MTR_USE_TTS_ANNOUNCEMENTS.getString(), client.getTextToSpeechAnnouncements(), client::toggleTextToSpeechAnnouncements);
		createToggleRow(TranslationProvider.OPTIONS_MTR_HIDE_TRANSLUCENT_PARTS.getString(), client.getHideTranslucentParts(), client::toggleHideTranslucentParts);
		createLanguageRow();

		dynamicTextureResolutionInput = createNumberRow(TranslationProvider.OPTIONS_MTR_DYNAMIC_TEXTURE_RESOLUTION.getString(), 0, Client.DYNAMIC_RESOLUTION_COUNT - 1, 1, client.getDynamicTextureResolution());
		trainOscillationInput = createNumberRow(TranslationProvider.OPTIONS_MTR_VEHICLE_OSCILLATION_MULTIPLIER.getString(), 0, Client.TRAIN_OSCILLATION_COUNT, 1, (int) (client.getVehicleOscillationMultiplier() * 10));
		trainOscillationInput.setSuffix("0%");

		createToggleRow(TranslationProvider.OPTIONS_MTR_DEFAULT_RAIL_3D.getString(), client.getDefaultRail3D(), client::toggleDefaultRail3D);
		createToggleRow(TranslationProvider.OPTIONS_MTR_USE_MTR_FONT.getString(), client.getUseMTRFont(), client::toggleUseMTRFont);
		createToggleRow(TranslationProvider.OPTIONS_MTR_DISABLE_SHADOWS_FOR_SHADERS.getString(), client.getDisableShadowsForShaders(), client::toggleDisableShadowsForShaders);

		final ButtonComponent supportButton = (ButtonComponent) new ButtonComponent(false)
			.setChildOf(contentContainer)
			.setY(new SiblingConstraint(GuiHelper.DEFAULT_PADDING))
			.setWidth(new RelativeConstraint());
		supportButton.setText(TranslationProvider.GUI_MTR_SUPPORT.getString());
		supportButton.onClick(() -> Util.getOperatingSystem().open("https://www.patreon.com/minecraft_transit_railway"));
	}

	@Override
	public void onScreenClose() {
		client.setDynamicTextureResolution((int) dynamicTextureResolutionInput.getValue());
		client.setVehicleOscillationMultiplier(trainOscillationInput.getValue() / 10.0);
		DynamicTextureCache.instance.refresh();
		Config.save();
		super.onScreenClose();
	}

	private void createToggleRow(String label, boolean initialState, Runnable toggleAction) {
		final UIContainer row = (UIContainer) new UIContainer()
			.setChildOf(contentContainer)
			.setY(new SiblingConstraint(GuiHelper.DEFAULT_PADDING / 2F))
			.setWidth(new RelativeConstraint())
			.setHeight(new PixelConstraint(20));

		new UIWrappedText(label, false)
			.setChildOf(row)
			.setWidth(new ScaleConstraint(new RelativeConstraint(), 0.7F))
			.setColor(new Color(GuiHelper.WHITE_COLOR));

		final ButtonComponent toggleButton = (ButtonComponent) new ButtonComponent(false)
			.setChildOf(row)
			.setX(new SiblingConstraint(GuiHelper.DEFAULT_PADDING))
			.setWidth(new RelativeConstraint());
		toggleButton.setText(getOnOffText(initialState));
		toggleButton.onClick(() -> {
			toggleAction.run();
//			toggleButton.setText(getOnOffText(!toggleButton.getText().equals(getOnOffText(true))));
		});
	}

	private void createLanguageRow() {
		final UIContainer row = (UIContainer) new UIContainer()
			.setChildOf(contentContainer)
			.setY(new SiblingConstraint(GuiHelper.DEFAULT_PADDING / 2F))
			.setWidth(new RelativeConstraint())
			.setHeight(new PixelConstraint(20));

		new UIWrappedText(TranslationProvider.OPTIONS_MTR_LANGUAGE_OPTIONS.getString(), false)
			.setChildOf(row)
			.setWidth(new ScaleConstraint(new RelativeConstraint(), 0.7F))
			.setColor(new Color(GuiHelper.WHITE_COLOR));

		final ButtonComponent languageButton = (ButtonComponent) new ButtonComponent(false)
			.setChildOf(row)
			.setX(new SiblingConstraint(GuiHelper.DEFAULT_PADDING))
			.setWidth(new RelativeConstraint());
		languageButton.setText(client.getLanguageDisplay().translationKey.getString());
		languageButton.onClick(() -> {
			client.cycleLanguageDisplay();
			languageButton.setText(client.getLanguageDisplay().translationKey.getString());
		});
	}

	private NumberInputComponent createNumberRow(String label, double min, double max, double step, double initialValue) {
		new UIWrappedText(label, false)
			.setChildOf(contentContainer)
			.setY(new SiblingConstraint(GuiHelper.DEFAULT_PADDING / 2F))
			.setWidth(new RelativeConstraint())
			.setColor(new Color(GuiHelper.WHITE_COLOR));

		final NumberInputComponent numberInputComponent = (NumberInputComponent) new NumberInputComponent(min, max, step, false, null)
			.setChildOf(contentContainer)
			.setY(new SiblingConstraint())
			.setWidth(new PixelConstraint(80));
		numberInputComponent.setValue(initialValue);
		return numberInputComponent;
	}

	private static String getOnOffText(boolean state) {
		return (state ? TranslationProvider.OPTIONS_MTR_ON : TranslationProvider.OPTIONS_MTR_OFF).getString();
	}
}
