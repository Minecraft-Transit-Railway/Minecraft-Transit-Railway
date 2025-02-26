package org.mtr.mod.config;

import org.mtr.core.serializer.ReaderBase;
import org.mtr.core.tool.Utilities;
import org.mtr.mod.Keys;
import org.mtr.mod.generated.config.ClientSchema;

public final class Client extends ClientSchema {

	public static final int DYNAMIC_RESOLUTION_COUNT = 8;
	public static final int TRAIN_OSCILLATION_COUNT = 15;

	public Client(ReaderBase readerBase) {
		super(readerBase);
		updateData(readerBase);
	}

	public boolean getChatAnnouncements() {
		return chatAnnouncements;
	}

	public boolean getTextToSpeechAnnouncements() {
		return textToSpeechAnnouncements;
	}

	public boolean getHideTranslucentParts() {
		return hideTranslucentParts;
	}

	public LanguageDisplay getLanguageDisplay() {
		return languageDisplay;
	}

	public int getDynamicTextureResolution() {
		return (int) dynamicTextureResolution;
	}

	public double getVehicleOscillationMultiplier() {
		return vehicleOscillationMultiplier;
	}

	public boolean getDefaultRail3D() {
		return defaultRail3D;
	}

	public boolean getUseMTRFont() {
		return useMTRFont;
	}

	public boolean getDisableShadowsForShaders() {
		return disableShadowsForShaders;
	}

	public boolean matchesPreloadResourcePattern(String id) {
		return id.replaceFirst(preloadResourcePattern, "").isEmpty();
	}

	public boolean showBetaWarningScreen() {
		return !Keys.MOD_VERSION.equals(betaWarningVersion);
	}

	public void toggleChatAnnouncements() {
		chatAnnouncements = !chatAnnouncements;
	}

	public void toggleTextToSpeechAnnouncements() {
		textToSpeechAnnouncements = !textToSpeechAnnouncements;
	}

	public void toggleHideTranslucentParts() {
		hideTranslucentParts = !hideTranslucentParts;
	}

	public void cycleLanguageDisplay() {
		languageDisplay = LanguageDisplay.values()[(languageDisplay.ordinal() + 1) % LanguageDisplay.values().length];
	}

	public void setDynamicTextureResolution(int dynamicTextureResolution) {
		this.dynamicTextureResolution = Utilities.clamp(dynamicTextureResolution, 0, DYNAMIC_RESOLUTION_COUNT);
	}

	public void setVehicleOscillationMultiplier(double trainOscillationMultiplier) {
		this.vehicleOscillationMultiplier = Utilities.clamp(trainOscillationMultiplier, 0, (TRAIN_OSCILLATION_COUNT / 10.0));
	}

	public void toggleDefaultRail3D() {
		defaultRail3D = !defaultRail3D;
	}

	public void toggleUseMTRFont() {
		useMTRFont = !useMTRFont;
	}

	public void toggleDisableShadowsForShaders() {
		disableShadowsForShaders = !disableShadowsForShaders;
	}

	public void hideBetaWarningScreen() {
		betaWarningVersion = Keys.MOD_VERSION;
	}
}
