package org.mtr.render;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import org.mtr.core.data.Station;
import org.mtr.tool.GuiHelper;

import javax.annotation.Nullable;

public final class SpecialSignStationRenderer extends SpecialSignPlatformStationRendererBase<Station> {

	private static final IntArrayList WHITE_COLOR_LIST = IntArrayList.of(GuiHelper.WHITE_COLOR);

	@Override
	protected ObjectObjectImmutablePair<IntArrayList, String> getColorsAndText(@Nullable Station data, String customText) {
		return new ObjectObjectImmutablePair<>(WHITE_COLOR_LIST, data == null ? customText : data.getName());
	}

	@Nullable
	@Override
	protected String getOverlayText(@Nullable Station data) {
		return null;
	}
}
