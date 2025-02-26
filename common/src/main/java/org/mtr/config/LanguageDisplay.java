package org.mtr.mod.config;

import org.mtr.mod.generated.lang.TranslationProvider;

public enum LanguageDisplay {
	NORMAL(TranslationProvider.OPTIONS_MTR_LANGUAGE_OPTIONS_NORMAL),
	CJK_ONLY(TranslationProvider.OPTIONS_MTR_LANGUAGE_OPTIONS_CJK_ONLY),
	NON_CJK_ONLY(TranslationProvider.OPTIONS_MTR_LANGUAGE_OPTIONS_NON_CJK_ONLY);

	public final TranslationProvider.TranslationHolder translationKey;

	LanguageDisplay(TranslationProvider.TranslationHolder translationKey) {
		this.translationKey = translationKey;
	}
}
