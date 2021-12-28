package mtr.gui;

import mtr.MTR;
import mtr.data.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.Tuple;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.text.AttributedString;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class ClientCache extends DataCache {

	private Font fontSmall;
	private Font fontBig;
	private Font fontCjk;

	public final Map<BlockPos, List<Platform>> posToPlatforms = new HashMap<>();
	public final Map<BlockPos, List<Siding>> posToSidings = new HashMap<>();
	public final Map<Long, Map<Integer, ColorNameTuple>> stationIdToRoutes = new HashMap<>();

	private final Map<Long, Map<Long, Platform>> stationIdToPlatforms = new HashMap<>();
	private final Map<Long, Map<Long, Siding>> depotIdToSidings = new HashMap<>();
	private final Map<Long, List<PlatformRouteDetails>> platformIdToRoutes = new HashMap<>();

	private final List<Long> clearStationIdToPlatforms = new ArrayList<>();
	private final List<Long> clearDepotIdToSidings = new ArrayList<>();
	private final List<Long> clearPlatformIdToRoutes = new ArrayList<>();
	private final List<String> clearRouteMaps = new ArrayList<>();

	private final Map<String, ResourceLocation> routeMaps = new HashMap<>();
	private boolean canGenerateRouteMap = true;

	private static final float LINE_HEIGHT_MULTIPLIER = 1.25F;

	public ClientCache(Set<Station> stations, Set<Platform> platforms, Set<Siding> sidings, Set<Route> routes, Set<Depot> depots) {
		super(stations, platforms, sidings, routes, depots);
	}

	@Override
	protected void syncAdditional() {
		mapPosToSavedRails(posToPlatforms, platforms);
		mapPosToSavedRails(posToSidings, sidings);

		stationIdToRoutes.clear();
		routes.forEach(route -> route.platformIds.forEach(platformId -> {
			final Station station = platformIdToStation.get(platformId);
			if (station != null) {
				if (!stationIdToRoutes.containsKey(station.id)) {
					stationIdToRoutes.put(station.id, new HashMap<>());
				}
				stationIdToRoutes.get(station.id).put(route.color, new ColorNameTuple(route.color, route.name.split("\\|\\|")[0]));
			}
		}));

		stationIdToPlatforms.keySet().forEach(id -> {
			if (!clearStationIdToPlatforms.contains(id)) {
				clearStationIdToPlatforms.add(id);
			}
		});
		depotIdToSidings.keySet().forEach(id -> {
			if (!clearDepotIdToSidings.contains(id)) {
				clearDepotIdToSidings.add(id);
			}
		});
		platformIdToRoutes.keySet().forEach(id -> {
			if (!clearPlatformIdToRoutes.contains(id)) {
				clearPlatformIdToRoutes.add(id);
			}
		});
		routeMaps.keySet().forEach(id -> {
			if (!clearRouteMaps.contains(id)) {
				clearRouteMaps.add(id);
			}
		});
	}

	public Map<Long, Platform> requestStationIdToPlatforms(long stationId) {
		if (!stationIdToPlatforms.containsKey(stationId)) {
			final Station station = stationIdMap.get(stationId);
			if (station != null) {
				stationIdToPlatforms.put(stationId, areaIdToSavedRails(station, platforms));
			} else {
				stationIdToPlatforms.put(stationId, new HashMap<>());
			}
		}
		return stationIdToPlatforms.get(stationId);
	}

	public Map<Long, Siding> requestDepotIdToSidings(long depotId) {
		if (!depotIdToSidings.containsKey(depotId)) {
			final Depot depot = depotIdMap.get(depotId);
			if (depot != null) {
				depotIdToSidings.put(depotId, areaIdToSavedRails(depot, sidings));
			} else {
				depotIdToSidings.put(depotId, new HashMap<>());
			}
		}
		return depotIdToSidings.get(depotId);
	}

	public List<PlatformRouteDetails> requestPlatformIdToRoutes(long platformId) {
		if (!platformIdToRoutes.containsKey(platformId)) {
			platformIdToRoutes.put(platformId, routes.stream().filter(route -> route.platformIds.contains(platformId)).map(route -> {
				final List<PlatformRouteDetails.StationDetails> stationDetails = route.platformIds.stream().map(platformId2 -> {
					final Station station = platformIdToStation.get(platformId2);
					if (station == null) {
						return new PlatformRouteDetails.StationDetails("", new ArrayList<>());
					} else {
						return new PlatformRouteDetails.StationDetails(station.name, stationIdToRoutes.get(station.id).values().stream().filter(colorNameTuple -> colorNameTuple.color != route.color).collect(Collectors.toList()));
					}
				}).collect(Collectors.toList());
				return new PlatformRouteDetails(route.name.split("\\|\\|")[0], route.color, route.circularState, route.platformIds.indexOf(platformId), stationDetails);
			}).collect(Collectors.toList()));
		}
		return platformIdToRoutes.get(platformId);
	}

	public ResourceLocation getRouteMap(long platformId, boolean flip) {
		final Minecraft minecraftClient = Minecraft.getInstance();
		if (fontSmall == null || fontBig == null || fontCjk == null) {
			final ResourceManager resourceManager = minecraftClient.getResourceManager();
			try {
				final Font font = Font.createFont(Font.TRUETYPE_FONT, resourceManager.getResource(new ResourceLocation(MTR.MOD_ID, "font/noto-sans-semibold.ttf")).getInputStream());
				fontSmall = font.deriveFont(Font.PLAIN, RouteMapGenerator.FONT_SIZE_SMALL);
				fontBig = font.deriveFont(Font.PLAIN, RouteMapGenerator.FONT_SIZE_BIG);
				fontCjk = Font.createFont(Font.TRUETYPE_FONT, resourceManager.getResource(new ResourceLocation(MTR.MOD_ID, "font/noto-serif-cjk-tc-semibold.ttf")).getInputStream()).deriveFont(Font.PLAIN, RouteMapGenerator.FONT_SIZE_BIG);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		final String key = String.valueOf(platformId) + flip;
		if (routeMaps.containsKey(key)) {
			return routeMaps.get(key);
		} else {
			final ResourceLocation defaultLocation = new ResourceLocation(MTR.MOD_ID, "textures/block/white.png");
			if (canGenerateRouteMap) {
				canGenerateRouteMap = false;
				new Thread(() -> {
					final DynamicTexture dynamicTexture = RouteMapGenerator.generate(routes.stream().map(route -> {
						final int currentIndex = route.platformIds.indexOf(platformId);
						return currentIndex >= 0 && currentIndex + 1 < route.platformIds.size() ? new Tuple<>(route, route.platformIds.indexOf(platformId)) : null;
					}).filter(Objects::nonNull).collect(Collectors.toList()));
					minecraftClient.execute(() -> {
						routeMaps.put(key, dynamicTexture == null ? defaultLocation : minecraftClient.getTextureManager().register(MTR.MOD_ID, dynamicTexture));
						canGenerateRouteMap = true;
					});
				}).start();
			}

			return defaultLocation;
		}
	}

	public byte[] getTextPixels(String text, int[] dimensions, IGui.HorizontalAlignment horizontalAlignment) {
		final String[] textSplit = IGui.textOrUntitled(text).split("\\|");
		final AttributedString[] attributedStrings = new AttributedString[textSplit.length];
		final int[] textWidths = new int[textSplit.length];
		final int[] fontSizes = new int[textSplit.length];
		final FontRenderContext context = new FontRenderContext(new AffineTransform(), false, false);
		final int padding = RouteMapGenerator.FONT_SIZE_SMALL / 4;
		int width = 0;
		int height = 0;

		for (int row = 0; row < textSplit.length; row++) {
			final boolean isCjk = textSplit[row].codePoints().anyMatch(Character::isIdeographic);
			final Font mainFont = isCjk ? fontBig : fontSmall;
			final Font fallbackFont = isCjk ? fontCjk : fontSmall;

			attributedStrings[row] = new AttributedString(textSplit[row]);
			attributedStrings[row].addAttribute(TextAttribute.FONT, mainFont, 0, textSplit[row].length());
			fontSizes[row] = isCjk ? RouteMapGenerator.FONT_SIZE_BIG : RouteMapGenerator.FONT_SIZE_SMALL;

			for (int characterIndex = 0; characterIndex < textSplit[row].length(); characterIndex++) {
				final boolean useFallback = !mainFont.canDisplay(textSplit[row].charAt(characterIndex));
				textWidths[row] += (useFallback ? fallbackFont : mainFont).getStringBounds(textSplit[row].substring(characterIndex, characterIndex + 1), context).getBounds().width;
				attributedStrings[row].addAttribute(TextAttribute.FONT, (useFallback ? fallbackFont : mainFont), characterIndex, characterIndex + 1);
			}

			width = Math.max(width, textWidths[row]);
			height += fontSizes[row] * LINE_HEIGHT_MULTIPLIER;
		}

		int yTextOffset = 0;
		final BufferedImage image = new BufferedImage(width + padding * 2, height + padding * 2, BufferedImage.TYPE_BYTE_GRAY);
		final Graphics2D graphics2D = image.createGraphics();
		graphics2D.setColor(Color.WHITE);
		graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		for (int row = 0; row < textSplit.length; row++) {
			graphics2D.drawString(attributedStrings[row].getIterator(), horizontalAlignment.getOffset(0, textWidths[row] - width) + padding, yTextOffset + fontSizes[row] + padding);
			yTextOffset += fontSizes[row] * LINE_HEIGHT_MULTIPLIER;
		}

		dimensions[0] = width + padding * 2;
		dimensions[1] = height + padding * 2;
		final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		graphics2D.dispose();
		image.flush();
		return pixels;
	}

	public void clearDataIfNeeded() {
		if (!clearStationIdToPlatforms.isEmpty()) {
			stationIdToPlatforms.remove(clearStationIdToPlatforms.remove(0));
		}
		if (!clearDepotIdToSidings.isEmpty()) {
			depotIdToSidings.remove(clearDepotIdToSidings.remove(0));
		}
		if (!clearPlatformIdToRoutes.isEmpty()) {
			platformIdToRoutes.remove(clearPlatformIdToRoutes.remove(0));
		}
		if (!clearRouteMaps.isEmpty()) {
			routeMaps.remove(clearRouteMaps.remove(0));
		}
	}

	private static <U extends AreaBase, V extends SavedRailBase> Map<Long, V> areaIdToSavedRails(U area, Set<V> savedRails) {
		final Map<Long, V> savedRailMap = new HashMap<>();
		savedRails.forEach(savedRail -> {
			final BlockPos pos = savedRail.getMidPos();
			if (area.inArea(pos.getX(), pos.getZ())) {
				savedRailMap.put(savedRail.id, savedRail);
			}
		});
		return savedRailMap;
	}

	private static <U extends SavedRailBase> void mapPosToSavedRails(Map<BlockPos, List<U>> posToSavedRails, Set<U> savedRails) {
		posToSavedRails.clear();
		savedRails.forEach(savedRail -> {
			final BlockPos pos = savedRail.getMidPos(true);
			if (!posToSavedRails.containsKey(pos)) {
				posToSavedRails.put(pos, new ArrayList<>());
			}
			posToSavedRails.get(pos).add(savedRail);
		});
	}

	public static class PlatformRouteDetails {

		public final String routeName;
		public final int routeColor;
		public final Route.CircularState circularState;
		public final int currentStationIndex;
		public final List<StationDetails> stationDetails;

		public PlatformRouteDetails(String routeName, int routeColor, Route.CircularState circularState, int currentStationIndex, List<StationDetails> stationDetails) {
			this.routeName = routeName;
			this.routeColor = routeColor;
			this.circularState = circularState;
			this.currentStationIndex = currentStationIndex;
			this.stationDetails = stationDetails;
		}

		public static class StationDetails {

			public final String stationName;
			public final List<ColorNameTuple> interchangeRoutes;

			public StationDetails(String stationName, List<ColorNameTuple> interchangeRoutes) {
				this.stationName = stationName;
				this.interchangeRoutes = interchangeRoutes;
			}
		}
	}

	public static class ColorNameTuple {

		public final int color;
		public final String name;

		public ColorNameTuple(int color, String name) {
			this.color = color;
			this.name = name;
		}
	}
}
