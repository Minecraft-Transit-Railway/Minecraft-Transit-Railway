package org.mtr.data;

import org.mtr.generated.lang.TranslationProvider;

public enum RailActionType {
	BRIDGE(TranslationProvider.GUI_MTR_PERCENTAGE_COMPLETE_BRIDGE, TranslationProvider.GUI_MTR_BUILDING_PROGRESS_BRIDGE, TranslationProvider.GUI_MTR_RAIL_ACTION_BRIDGE, 0xFFCCCCCC),
	BRIDGE_WALL(TranslationProvider.GUI_MTR_PERCENTAGE_COMPLETE_BRIDGE_WALL, TranslationProvider.GUI_MTR_BUILDING_PROGRESS_BRIDGE_WALL, TranslationProvider.GUI_MTR_RAIL_ACTION_BRIDGE_WALL, 0xFF666666),
	TUNNEL(TranslationProvider.GUI_MTR_PERCENTAGE_COMPLETE_TUNNEL, TranslationProvider.GUI_MTR_BUILDING_PROGRESS_TUNNEL, TranslationProvider.GUI_MTR_RAIL_ACTION_TUNNEL, 0xFF663300),
	TUNNEL_WALL(TranslationProvider.GUI_MTR_PERCENTAGE_COMPLETE_TUNNEL_WALL, TranslationProvider.GUI_MTR_BUILDING_PROGRESS_TUNNEL_WALL, TranslationProvider.GUI_MTR_RAIL_ACTION_TUNNEL_WALL, 0xFF666666);

	public final TranslationProvider.TranslationHolder progressTranslation;
	public final TranslationProvider.TranslationHolder batchProgressTranslation;
	public final TranslationProvider.TranslationHolder nameTranslation;
	public final int color;

	RailActionType(TranslationProvider.TranslationHolder progressTranslation, TranslationProvider.TranslationHolder batchProgressTranslation, TranslationProvider.TranslationHolder nameTranslation, int color) {
		this.progressTranslation = progressTranslation;
		this.batchProgressTranslation = batchProgressTranslation;
		this.nameTranslation = nameTranslation;
		this.color = color;
	}
}
