package org.mtr.screen;

import gg.essential.elementa.constraints.PixelConstraint;
import gg.essential.elementa.constraints.RelativeConstraint;
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
import org.mtr.widget.CheckboxComponent;
import org.mtr.widget.NumberInputComponent;

/**
 * Elementa configuration screen for client-side toggles and numeric options.
 */
public final class ConfigScreen extends SingleTabBackgroundScreenBase {

	private final Client client = Config.getClient();
	private final NumberInputComponent dynamicTextureResolutionInputComponent;
	private final NumberInputComponent trainOscillationInputComponent;

	private static final int LEFT_WIDTH = 96;

	public ConfigScreen(@Nullable Screen previousScreenLegacy) {
		super(previousScreenLegacy, TranslationProvider.GUI_MTR_MTR_OPTIONS.getString());

		createCheckbox(TranslationProvider.OPTIONS_MTR_SHOW_ANNOUNCEMENT_MESSAGES.getString(), client.getChatAnnouncements(), client::toggleChatAnnouncements);
		createCheckbox(TranslationProvider.OPTIONS_MTR_USE_TTS_ANNOUNCEMENTS.getString(), client.getTextToSpeechAnnouncements(), client::toggleTextToSpeechAnnouncements);
		createCheckbox(TranslationProvider.OPTIONS_MTR_HIDE_TRANSLUCENT_PARTS.getString(), client.getHideTranslucentParts(), client::toggleHideTranslucentParts);

		GuiHelper.createSpacing(contentContainer);
		createLanguageOptions();

		dynamicTextureResolutionInputComponent = createNumberInput(TranslationProvider.OPTIONS_MTR_DYNAMIC_TEXTURE_RESOLUTION.getString(), Client.DYNAMIC_RESOLUTION_COUNT - 1, client.getDynamicTextureResolution());
		trainOscillationInputComponent = createNumberInput(TranslationProvider.OPTIONS_MTR_VEHICLE_OSCILLATION_MULTIPLIER.getString(), Client.TRAIN_OSCILLATION_COUNT, Math.round(client.getVehicleOscillationMultiplier() * 100));
		trainOscillationInputComponent.setSuffix("%");

		createCheckbox(TranslationProvider.OPTIONS_MTR_DEFAULT_RAIL_3D.getString(), client.getDefaultRail3D(), client::toggleDefaultRail3D);
		createCheckbox(TranslationProvider.OPTIONS_MTR_USE_MTR_FONT.getString(), client.getUseMTRFont(), client::toggleUseMTRFont);
		createCheckbox(TranslationProvider.OPTIONS_MTR_DISABLE_SHADOWS_FOR_SHADERS.getString(), client.getDisableShadowsForShaders(), client::toggleDisableShadowsForShaders);

		GuiHelper.createSpacing(contentContainer);
		final ButtonComponent supportButton = (ButtonComponent) new ButtonComponent(false)
			.setChildOf(contentContainer)
			.setY(new SiblingConstraint(GuiHelper.DEFAULT_PADDING))
			.setWidth(new PixelConstraint(LEFT_WIDTH));

		supportButton.setText(TranslationProvider.GUI_MTR_SUPPORT.getString());
		supportButton.onClick(() -> Util.getOperatingSystem().open("https://www.patreon.com/minecraft_transit_railway"));
	}

	@Override
	public void onScreenClose() {
		client.setDynamicTextureResolution((int) dynamicTextureResolutionInputComponent.getValue());
		client.setVehicleOscillationMultiplier(trainOscillationInputComponent.getValue() / 100);
		DynamicTextureCache.instance.refresh();
		Config.save();
		super.onScreenClose();
	}

	private void createCheckbox(String label, boolean initialState, Runnable toggleAction) {
		final CheckboxComponent checkboxComponent = (CheckboxComponent) new CheckboxComponent()
			.setChildOf(contentContainer)
			.setY(new SiblingConstraint())
			.setWidth(new RelativeConstraint());

		checkboxComponent.setText(label);
		checkboxComponent.onClick(toggleAction);
		checkboxComponent.setChecked(initialState);

		GuiHelper.createSpacing(contentContainer);
	}

	private void createLanguageOptions() {
		GuiHelper.createLabel(contentContainer, TranslationProvider.OPTIONS_MTR_LANGUAGE_OPTIONS.getString());

		final ButtonComponent languageButton = (ButtonComponent) new ButtonComponent(false)
			.setChildOf(contentContainer)
			.setX(new SiblingConstraint(GuiHelper.DEFAULT_PADDING))
			.setWidth(new PixelConstraint(LEFT_WIDTH));

		languageButton.setText(client.getLanguageDisplay().translationKey.getString());
		languageButton.onClick(() -> {
			client.cycleLanguageDisplay();
			languageButton.setText(client.getLanguageDisplay().translationKey.getString());
		});
	}

	private NumberInputComponent createNumberInput(String label, double max, double initialValue) {
		GuiHelper.createSpacing(contentContainer);
		GuiHelper.createLabel(contentContainer, label);

		final NumberInputComponent numberInputComponent = (NumberInputComponent) new NumberInputComponent(0, max, 1, false, null)
			.setChildOf(contentContainer)
			.setY(new SiblingConstraint())
			.setWidth(new PixelConstraint(LEFT_WIDTH));

		numberInputComponent.setValue(initialValue);
		return numberInputComponent;
	}
}
