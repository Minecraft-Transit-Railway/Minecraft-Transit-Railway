package org.mtr.render;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import org.mtr.core.data.Platform;
import org.mtr.resource.SignResource;

import javax.annotation.Nullable;

public final class SpecialSignPlatformRenderer extends SpecialSignPlatformStationRendererBase<Platform> {

	@Nullable
	@Override
	protected ObjectObjectImmutablePair<IntArrayList, String> getColorsAndText(@Nullable Platform data, String customText) {
		return data == null ? null : SignResource.getPlatformColorsAndDestinations(data.getId());
	}

	@Override
	protected String getOverlayText(@Nullable Platform data) {
		return data == null ? String.valueOf(((System.currentTimeMillis() / 2000) % 4) + 1) : data.getName();
	}
}
