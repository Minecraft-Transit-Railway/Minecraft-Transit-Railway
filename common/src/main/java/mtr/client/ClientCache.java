package mtr.client;

import mtr.MTR;
import mtr.data.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.text.AttributedString;
import java.util.List;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ClientCache extends DataCache {

	private Font font;
	private Font fontCjk;

	public final Map<Long, Map<Integer, ColorNameTuple>> stationIdToRoutes = new HashMap<>();

	private final Map<TransportMode, Map<BlockPos, List<Platform>>> posToPlatforms = new HashMap<>();
	private final Map<TransportMode, Map<BlockPos, List<Siding>>> posToSidings = new HashMap<>();
	private final Map<Long, Map<Long, Platform>> stationIdToPlatforms = new HashMap<>();
	private final Map<Long, Map<Long, Siding>> depotIdToSidings = new HashMap<>();
	private final Map<Long, List<PlatformRouteDetails>> platformIdToRoutes = new HashMap<>();

	private final List<Long> clearStationIdToPlatforms = new ArrayList<>();
	private final List<Long> clearDepotIdToSidings = new ArrayList<>();
	private final List<Long> clearPlatformIdToRoutes = new ArrayList<>();

	private final Map<String, DynamicResource> dynamicResources = new HashMap<>();
	private boolean canGenerateResource = true;

	private static final float LINE_HEIGHT_MULTIPLIER = 1.25F;
	private static final ResourceLocation DEFAULT_BLACK_RESOURCE = new ResourceLocation(MTR.MOD_ID, "textures/block/black.png");
	private static final ResourceLocation DEFAULT_WHITE_RESOURCE = new ResourceLocation(MTR.MOD_ID, "textures/block/white.png");
	private static final ResourceLocation DEFAULT_TRANSPARENT_RESOURCE = new ResourceLocation(MTR.MOD_ID, "textures/block/transparent.png");

	public ClientCache(Set<Station> stations, Set<Platform> platforms, Set<Siding> sidings, Set<Route> routes, Set<Depot> depots) {
		super(stations, platforms, sidings, routes, depots);
		for (final TransportMode transportMode : TransportMode.values()) {
			posToPlatforms.put(transportMode, new HashMap<>());
			posToSidings.put(transportMode, new HashMap<>());
		}
	}

	@Override
	protected void syncAdditional() {
		for (final TransportMode transportMode : TransportMode.values()) {
			mapPosToSavedRails(posToPlatforms.get(transportMode), platforms, transportMode);
			mapPosToSavedRails(posToSidings.get(transportMode), sidings, transportMode);
		}

		stationIdToRoutes.clear();
		routes.forEach(route -> {
			if (!route.isHidden) {
				route.platformIds.forEach(platformId -> {
					final Station station = platformIdToStation.get(platformId);
					if (station != null) {
						if (!stationIdToRoutes.containsKey(station.id)) {
							stationIdToRoutes.put(station.id, new HashMap<>());
						}
						stationIdToRoutes.get(station.id).put(route.color, new ColorNameTuple(route.color, route.name.split("\\|\\|")[0]));
					}
				});
			}
		});

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
		dynamicResources.forEach((key, dynamicResource) -> dynamicResource.remove());
		dynamicResources.clear();
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
					if (station == null || !stationIdToRoutes.containsKey(station.id)) {
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

	public String getFormattedRouteDestination(Route route, int currentStationIndex, String circularMarker) {
		try {
			if (route.circularState == Route.CircularState.NONE) {
				return platformIdToStation.get(route.platformIds.get(route.platformIds.size() - 1)).name;
			} else {
				boolean isVia = false;
				String text = "";
				for (int i = currentStationIndex + 1; i < route.platformIds.size() - 1; i++) {
					if (stationIdToRoutes.get(platformIdToStation.get(route.platformIds.get(i)).id).size() > 1) {
						text = platformIdToStation.get(route.platformIds.get(i)).name;
						isVia = true;
						break;
					}
				}
				if (!isVia) {
					text = platformIdToStation.get(route.platformIds.get(route.platformIds.size() - 1)).name;
				}
				final String translationString = String.format("%s_%s", route.circularState == Route.CircularState.CLOCKWISE ? "clockwise" : "anticlockwise", isVia ? "via" : "to");
				return circularMarker + IGui.insertTranslation("gui.mtr." + translationString + "_cjk", "gui.mtr." + translationString, 1, text);
			}
		} catch (Exception ignored) {
			return "";
		}
	}

	public ResourceLocation getColorStrip(long platformId) {
		return getResource(String.format("color_%s", platformId), () -> RouteMapGenerator.generateColorStrip(platformId), DefaultRenderingColor.TRANSPARENT);
	}

	public ResourceLocation getStationName(long platformId, float aspectRatio) {
		return getResource(String.format("name_%s_%s", platformId, aspectRatio), () -> RouteMapGenerator.generateStationName(platformId, aspectRatio), DefaultRenderingColor.WHITE);
	}

	public ResourceLocation getDirectionArrow(long platformId, boolean invert, boolean hasLeft, boolean hasRight, IGui.HorizontalAlignment horizontalAlignment, boolean showToString, float paddingScale, float aspectRatio, boolean transparentWhite) {
		return getResource(String.format("map_%s_%s_%s_%s_%s_%s_%s_%s_%s", platformId, invert, hasLeft, hasRight, horizontalAlignment, showToString, paddingScale, aspectRatio, transparentWhite), () -> RouteMapGenerator.generateDirectionArrow(platformId, invert, hasLeft, hasRight, horizontalAlignment, showToString, paddingScale, aspectRatio, transparentWhite), invert ? DefaultRenderingColor.BLACK : transparentWhite ? DefaultRenderingColor.TRANSPARENT : DefaultRenderingColor.WHITE);
	}

	public ResourceLocation getRouteMap(long platformId, boolean vertical, boolean flip, float aspectRatio, boolean transparentWhite) {
		return getResource(String.format("map_%s_%s_%s_%s_%s", platformId, vertical, flip, aspectRatio, transparentWhite), () -> RouteMapGenerator.generateRouteMap(platformId, vertical, flip, aspectRatio, transparentWhite), transparentWhite ? DefaultRenderingColor.TRANSPARENT : DefaultRenderingColor.WHITE);
	}

	public byte[] getTextPixels(String text, int[] dimensions, int fontSizeCjk, int fontSize) {
		return getTextPixels(text, dimensions, Integer.MAX_VALUE, fontSizeCjk, fontSize, 0, null);
	}

	public byte[] getTextPixels(String text, int[] dimensions, int maxWidth, int fontSizeCjk, int fontSize, int padding, IGui.HorizontalAlignment horizontalAlignment) {
		final boolean oneRow = horizontalAlignment == null;
		final String[] textSplit = IGui.textOrUntitled(text).split("\\|");
		final AttributedString[] attributedStrings = new AttributedString[textSplit.length];
		final int[] textWidths = new int[textSplit.length];
		final int[] fontSizes = new int[textSplit.length];
		final FontRenderContext context = new FontRenderContext(new AffineTransform(), false, false);
		int width = 0;
		int height = 0;

		for (int index = 0; index < textSplit.length; index++) {
			final boolean isCjk = textSplit[index].codePoints().anyMatch(Character::isIdeographic);
			final Font mainFont = font.deriveFont(Font.PLAIN, isCjk ? fontSizeCjk : fontSize);
			final Font fallbackFont = isCjk ? fontCjk.deriveFont(Font.PLAIN, fontSizeCjk) : mainFont;

			attributedStrings[index] = new AttributedString(textSplit[index]);
			if (textSplit[index].length() > 0) {
				attributedStrings[index].addAttribute(TextAttribute.FONT, mainFont, 0, textSplit[index].length());
			}
			fontSizes[index] = isCjk ? fontSizeCjk : fontSize;

			for (int characterIndex = 0; characterIndex < textSplit[index].length(); characterIndex++) {
				final boolean useFallback = !mainFont.canDisplay(textSplit[index].charAt(characterIndex));
				textWidths[index] += (useFallback ? fallbackFont : mainFont).getStringBounds(textSplit[index].substring(characterIndex, characterIndex + 1), context).getBounds().width;
				attributedStrings[index].addAttribute(TextAttribute.FONT, (useFallback ? fallbackFont : mainFont), characterIndex, characterIndex + 1);
			}

			if (oneRow) {
				if (index > 0) {
					width += padding;
				}
				width += textWidths[index];
				height = Math.max(height, (int) (fontSizes[index] * LINE_HEIGHT_MULTIPLIER));
			} else {
				width = Math.max(width, Math.min(maxWidth, textWidths[index]));
				height += fontSizes[index] * LINE_HEIGHT_MULTIPLIER;
			}
		}

		int textOffset = 0;
		final BufferedImage image = new BufferedImage(width + (oneRow ? 0 : padding * 2), height + (oneRow ? 0 : padding * 2), BufferedImage.TYPE_BYTE_GRAY);
		final Graphics2D graphics2D = image.createGraphics();
		graphics2D.setColor(Color.WHITE);
		graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		for (int index = 0; index < textSplit.length; index++) {
			if (oneRow) {
				graphics2D.drawString(attributedStrings[index].getIterator(), textOffset, height / LINE_HEIGHT_MULTIPLIER);
				textOffset += textWidths[index] + padding;
			} else {
				final int textWidth = Math.min(maxWidth, textWidths[index]);
				final AffineTransform stretch = new AffineTransform();
				final float scale = (float) textWidth / textWidths[index];
				stretch.concatenate(AffineTransform.getScaleInstance(scale, 1));
				graphics2D.setTransform(stretch);
				graphics2D.drawString(attributedStrings[index].getIterator(), horizontalAlignment.getOffset(0, textWidth - width) + padding / scale, textOffset + fontSizes[index] + padding);
				textOffset += fontSizes[index] * LINE_HEIGHT_MULTIPLIER;
			}
		}

		dimensions[0] = width + (oneRow ? 0 : padding * 2);
		dimensions[1] = height + (oneRow ? 0 : padding * 2);
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
	}

	public Map<BlockPos, List<Platform>> getPosToPlatforms(TransportMode transportMode) {
		return posToPlatforms.get(transportMode);
	}

	public Map<BlockPos, List<Siding>> getPosToSidings(TransportMode transportMode) {
		return posToSidings.get(transportMode);
	}

	private ResourceLocation getResource(String key, Supplier<DynamicTexture> supplier, DefaultRenderingColor defaultRenderingColor) {
		final Minecraft minecraftClient = Minecraft.getInstance();
		if (font == null || fontCjk == null) {
			final ResourceManager resourceManager = minecraftClient.getResourceManager();
			try {
				font = Font.createFont(Font.TRUETYPE_FONT, resourceManager.getResource(new ResourceLocation(MTR.MOD_ID, "font/noto-sans-semibold.ttf")).getInputStream());
				fontCjk = Font.createFont(Font.TRUETYPE_FONT, resourceManager.getResource(new ResourceLocation(MTR.MOD_ID, "font/noto-serif-cjk-tc-semibold.ttf")).getInputStream());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		final Set<String> keysToRemove = new HashSet<>();
		dynamicResources.forEach((checkKey, dynamicResource) -> {
			if (dynamicResource.removeIfOld()) {
				keysToRemove.add(checkKey);
			}
		});
		if (!keysToRemove.isEmpty()) {
			keysToRemove.forEach(dynamicResources::remove);
		}

		final boolean hasKey = dynamicResources.containsKey(key);
		if (hasKey) {
			return dynamicResources.get(key).getResourceLocation();
		} else {
			final ResourceLocation defaultLocation = defaultRenderingColor.resourceLocation;

			if (canGenerateResource) {
				canGenerateResource = false;
				RouteMapGenerator.setConstants();

				new Thread(() -> {
					final DynamicTexture dynamicTexture = supplier.get();
					minecraftClient.execute(() -> {
						dynamicResources.put(key, new DynamicResource(dynamicTexture == null ? defaultLocation : minecraftClient.getTextureManager().register(MTR.MOD_ID, dynamicTexture)));
						canGenerateResource = true;
					});
				}).start();
			}

			return defaultLocation;
		}
	}

	private static <U extends AreaBase, V extends SavedRailBase> Map<Long, V> areaIdToSavedRails(U area, Set<V> savedRails) {
		final Map<Long, V> savedRailMap = new HashMap<>();
		savedRails.forEach(savedRail -> {
			final BlockPos pos = savedRail.getMidPos();
			if (area.isTransportMode(savedRail.transportMode) && area.inArea(pos.getX(), pos.getZ())) {
				savedRailMap.put(savedRail.id, savedRail);
			}
		});
		return savedRailMap;
	}

	private static <U extends SavedRailBase> void mapPosToSavedRails(Map<BlockPos, List<U>> posToSavedRails, Set<U> savedRails, TransportMode transportMode) {
		posToSavedRails.clear();
		savedRails.forEach(savedRail -> {
			if (savedRail.isTransportMode(transportMode)) {
				final BlockPos pos = savedRail.getMidPos(true);
				if (!posToSavedRails.containsKey(pos)) {
					posToSavedRails.put(pos, new ArrayList<>());
				}
				posToSavedRails.get(pos).add(savedRail);
			}
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

	private static class DynamicResource {

		private int age;
		private final ResourceLocation resourceLocation;
		private static final int MAX_AGE = 10000;

		private DynamicResource(ResourceLocation resourceLocation) {
			age = 0;
			this.resourceLocation = resourceLocation;
		}

		private ResourceLocation getResourceLocation() {
			age = 0;
			return resourceLocation;
		}

		private void remove() {
			if (!resourceLocation.equals(DEFAULT_BLACK_RESOURCE) && !resourceLocation.equals(DEFAULT_WHITE_RESOURCE) && !resourceLocation.equals(DEFAULT_TRANSPARENT_RESOURCE)) {
				final TextureManager textureManager = Minecraft.getInstance().getTextureManager();
				textureManager.release(resourceLocation);
				final AbstractTexture abstractTexture = textureManager.getTexture(resourceLocation);
				if (abstractTexture != null) {
					abstractTexture.releaseId();
				}
			}
		}

		private boolean removeIfOld() {
			age++;
			if (age >= MAX_AGE) {
				remove();
				return true;
			} else {
				return false;
			}
		}
	}

	private enum DefaultRenderingColor {
		BLACK(DEFAULT_BLACK_RESOURCE),
		WHITE(DEFAULT_WHITE_RESOURCE),
		TRANSPARENT(DEFAULT_TRANSPARENT_RESOURCE);

		private final ResourceLocation resourceLocation;

		DefaultRenderingColor(ResourceLocation resourceLocation) {
			this.resourceLocation = resourceLocation;
		}
	}
}
