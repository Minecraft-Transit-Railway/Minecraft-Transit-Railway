package org.mtr.mod.data;

import org.mtr.mod.generated.lang.TranslationProvider;

public enum RailActionType {
	BRIDGE(TranslationProvider.GUI_MTR_PERCENTAGE_COMPLETE_BRIDGE, TranslationProvider.GUI_MTR_RAIL_ACTION_BRIDGE, 0xFFCCCCCC),
	TUNNEL(TranslationProvider.GUI_MTR_PERCENTAGE_COMPLETE_TUNNEL, TranslationProvider.GUI_MTR_RAIL_ACTION_TUNNEL, 0xFF663300),
	TUNNEL_WALL(TranslationProvider.GUI_MTR_PERCENTAGE_COMPLETE_TUNNEL_WALL, TranslationProvider.GUI_MTR_RAIL_ACTION_TUNNEL_WALL, 0xFF666666);

	public final TranslationProvider.TranslationHolder progressTranslation;
	public final TranslationProvider.TranslationHolder nameTranslation;
	public final int color;

	RailActionType(TranslationProvider.TranslationHolder progressTranslation, TranslationProvider.TranslationHolder nameTranslation, int color) {
		this.progressTranslation = progressTranslation;
		this.nameTranslation = nameTranslation;
		this.color = color;
	}
}
