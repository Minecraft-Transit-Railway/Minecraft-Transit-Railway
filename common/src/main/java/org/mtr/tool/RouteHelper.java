package org.mtr.tool;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import org.mtr.MTR;
import org.mtr.client.MinecraftClientData;
import org.mtr.core.data.SimplifiedRoute;
import org.mtr.data.IGui;
import org.mtr.generated.lang.TranslationProvider;

public final class RouteHelper {

	private static final String TEMP_CIRCULAR_MARKER_CLOCKWISE = String.format("temp_circular_marker_%s_clockwise", MTR.randomString());
	private static final String TEMP_CIRCULAR_MARKER_ANTICLOCKWISE = String.format("temp_circular_marker_%s_anticlockwise", MTR.randomString());

	/**
	 * Iterate through the routes serving a {@link org.mtr.core.data.Platform}.
	 *
	 * @param platformId         the id of the platform
	 * @param includeTerminating whether to include terminating routes in the {@code routeConsumer} and the resulting colours
	 * @param routeConsumer      a callback supplying the {@link SimplifiedRoute} and the current station index
	 * @return a list of colours (sorted by route)
	 */
	public static IntArrayList getRouteStream(long platformId, boolean includeTerminating, RouteConsumer routeConsumer) {
		final IntArrayList colors = new IntArrayList();
		final IntArrayList terminatingColors = new IntArrayList();

		MinecraftClientData.getInstance().simplifiedRoutes.stream().filter(simplifiedRoute -> simplifiedRoute.getPlatformIndex(platformId) >= 0 && !simplifiedRoute.getName().isEmpty()).sorted().forEach(simplifiedRoute -> {
			final int currentStationIndex = simplifiedRoute.getPlatformIndex(platformId);
			if (includeTerminating || currentStationIndex < simplifiedRoute.getPlatforms().size() - 1) {
				routeConsumer.accept(simplifiedRoute, currentStationIndex);
				if (!colors.contains(simplifiedRoute.getColor())) {
					colors.add(simplifiedRoute.getColor());
				}
			} else {
				if (!terminatingColors.contains(simplifiedRoute.getColor())) {
					terminatingColors.add(simplifiedRoute.getColor());
				}
			}
		});

		if (colors.isEmpty()) {
			colors.addAll(terminatingColors);
		}

		return colors;
	}

	public static ObjectObjectImmutablePair<IntArrayList, String> getRouteColorsAndDestinationString(long platformId, boolean includeTerminating, boolean showToString) {
		final ObjectArrayList<String> destinations = new ObjectArrayList<>();

		final IntArrayList colors = RouteHelper.getRouteStream(platformId, includeTerminating, (simplifiedRoute, currentStationIndex) -> {
			final String tempMarker = switch (simplifiedRoute.getCircularState()) {
				case CLOCKWISE -> TEMP_CIRCULAR_MARKER_CLOCKWISE;
				case ANTICLOCKWISE -> TEMP_CIRCULAR_MARKER_ANTICLOCKWISE;
				default -> "";
			};
			destinations.add(tempMarker + simplifiedRoute.getPlatforms().get(currentStationIndex).getDestination());
		});

		if (destinations.isEmpty()) {
			return new ObjectObjectImmutablePair<>(colors, "");
		} else {

			String destinationString = IGui.mergeStations(destinations);
			final boolean isClockwise = destinationString.startsWith(TEMP_CIRCULAR_MARKER_CLOCKWISE);
			final boolean isAnticlockwise = destinationString.startsWith(TEMP_CIRCULAR_MARKER_ANTICLOCKWISE);
			destinationString = destinationString.replace(TEMP_CIRCULAR_MARKER_CLOCKWISE, "").replace(TEMP_CIRCULAR_MARKER_ANTICLOCKWISE, "");

			if (!destinationString.isEmpty()) {
				if (isClockwise) {
					destinationString = IGui.insertTranslation(TranslationProvider.GUI_MTR_CLOCKWISE_VIA_CJK, TranslationProvider.GUI_MTR_CLOCKWISE_VIA, 1, destinationString);
				} else if (isAnticlockwise) {
					destinationString = IGui.insertTranslation(TranslationProvider.GUI_MTR_ANTICLOCKWISE_VIA_CJK, TranslationProvider.GUI_MTR_ANTICLOCKWISE_VIA, 1, destinationString);
				} else if (showToString) {
					destinationString = IGui.insertTranslation(TranslationProvider.GUI_MTR_TO_CJK, TranslationProvider.GUI_MTR_TO, 1, destinationString);
				}
			}

			return new ObjectObjectImmutablePair<>(colors, destinationString);
		}
	}

	@FunctionalInterface
	public interface RouteConsumer {
		void accept(SimplifiedRoute simplifiedRoute, int currentStationIndex);
	}
}
