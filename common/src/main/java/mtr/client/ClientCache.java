package mtr.client;

import com.mojang.blaze3d.platform.NativeImage;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import mtr.MTR;
import mtr.block.BlockLiftTrackFloor;
import mtr.data.*;
import mtr.mappings.Utilities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.text.AttributedString;
import java.util.List;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ClientCache extends DataCache implements IGui {

	private Font font;
	private Font fontCjk;

	public final Map<Long, Map<Integer, ColorNameTuple>> stationIdToRoutes = new HashMap<>();

	private final Set<LiftClient> liftsClient;
	public final Map<Long, LiftClient> liftsClientIdMap = new HashMap<>();
	private final Map<TransportMode, Map<BlockPos, List<Platform>>> posToPlatforms = new HashMap<>();
	private final Map<TransportMode, Map<BlockPos, List<Siding>>> posToSidings = new HashMap<>();
	private final Map<Long, Map<Long, Platform>> stationIdToPlatforms = new HashMap<>();
	private final Map<Long, Map<Long, Siding>> depotIdToSidings = new HashMap<>();
	private final Map<Long, List<PlatformRouteDetails>> platformIdToRoutes = new HashMap<>();

	private final List<Long> clearStationIdToPlatforms = new ArrayList<>();
	private final List<Long> clearDepotIdToSidings = new ArrayList<>();
	private final List<Long> clearPlatformIdToRoutes = new ArrayList<>();

	private final Object2ObjectLinkedOpenHashMap<String, DynamicResource> dynamicResources = new Object2ObjectLinkedOpenHashMap<>();
	private final ObjectLinkedOpenHashSet<String> resourcesToRefresh = new ObjectLinkedOpenHashSet<>();
	private final List<Runnable> resourceRegistryQueue = new ArrayList<>();

	public static final float LINE_HEIGHT_MULTIPLIER = 1.25F;
	private static final ResourceLocation DEFAULT_BLACK_RESOURCE = new ResourceLocation(MTR.MOD_ID, "textures/block/black.png");
	private static final ResourceLocation DEFAULT_WHITE_RESOURCE = new ResourceLocation(MTR.MOD_ID, "textures/block/white.png");
	private static final ResourceLocation DEFAULT_TRANSPARENT_RESOURCE = new ResourceLocation(MTR.MOD_ID, "textures/block/transparent.png");

	public ClientCache(Set<Station> stations, Set<Platform> platforms, Set<Siding> sidings, Set<Route> routes, Set<Depot> depots, Set<LiftClient> lifts) {
		super(stations, platforms, sidings, routes, depots, new HashSet<>());
		for (final TransportMode transportMode : TransportMode.values()) {
			posToPlatforms.put(transportMode, new HashMap<>());
			posToSidings.put(transportMode, new HashMap<>());
		}
		liftsClient = lifts;
	}

	@Override
	protected void syncAdditional() {
		syncLiftIds();

		for (final TransportMode transportMode : TransportMode.values()) {
			mapPosToSavedRails(posToPlatforms.get(transportMode), platforms, transportMode);
			mapPosToSavedRails(posToSidings.get(transportMode), sidings, transportMode);
		}

		stationIdToRoutes.clear();
		routes.forEach(route -> {
			if (!route.isHidden) {
				route.platformIds.forEach(platformId -> {
					final Station station = platformIdToStation.get(platformId.platformId);
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
	}

	public void refreshDynamicResources() {
		System.out.println("Refreshing dynamic resources");
		resourcesToRefresh.addAll(dynamicResources.keySet());
	}

	public void syncLiftIds() {
		mapIds(liftsClientIdMap, liftsClient);
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
			platformIdToRoutes.put(platformId, routes.stream().map(route -> {
				final int index = route.getPlatformIdIndex(platformId);
				if (index < 0) {
					return null;
				} else {
					final List<PlatformRouteDetails.StationDetails> stationDetails = route.platformIds.stream().map(platformId2 -> {
						final Station station = platformIdToStation.get(platformId2.platformId);
						if (station == null || !stationIdToRoutes.containsKey(station.id)) {
							return new PlatformRouteDetails.StationDetails("", new ArrayList<>());
						} else {
							return new PlatformRouteDetails.StationDetails(station.name, stationIdToRoutes.get(station.id).values().stream().filter(colorNameTuple -> colorNameTuple.color != route.color).collect(Collectors.toList()));
						}
					}).collect(Collectors.toList());
					return new PlatformRouteDetails(route.name.split("\\|\\|")[0], route.color, route.circularState, index, stationDetails);
				}
			}).filter(Objects::nonNull).collect(Collectors.toList()));
		}
		return platformIdToRoutes.get(platformId);
	}

	public String[] requestLiftFloorText(BlockPos pos) {
		// TODO cache this
		final Level world = Minecraft.getInstance().level;
		final String[] text = {"", ""};
		if (world != null && pos != null) {
			final BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof BlockLiftTrackFloor.TileEntityLiftTrackFloor) {
				text[0] = ((BlockLiftTrackFloor.TileEntityLiftTrackFloor) blockEntity).getFloorNumber();
				text[1] = ((BlockLiftTrackFloor.TileEntityLiftTrackFloor) blockEntity).getFloorDescription();
			}
		}
		return text;
	}

	public Set<Station> getConnectingStationsIncludingThisOne(Station station) {
		final Set<Station> stationsToCheck = new HashSet<>();
		stationsToCheck.add(station);
		if (stationIdToConnectingStations.containsKey(station)) {
			stationsToCheck.addAll(stationIdToConnectingStations.get(station));
		}
		return stationsToCheck;
	}

	public Map<Integer, ColorNameTuple> getAllRoutesIncludingConnectingStations(Station station) {
		final Map<Integer, ColorNameTuple> routeMap = new HashMap<>();
		getConnectingStationsIncludingThisOne(station).forEach(checkStation -> {
			if (stationIdToRoutes.containsKey(checkStation.id)) {
				routeMap.putAll(stationIdToRoutes.get(checkStation.id));
			}
		});
		return routeMap;
	}

	public String getFormattedRouteDestination(Route route, int currentStationIndex, String circularMarker) {
		try {
			final String customDestination = route.getDestination(currentStationIndex);
			if (customDestination != null) {
				return customDestination;
			}

			if (route.circularState == Route.CircularState.NONE) {
				return platformIdToStation.get(route.getLastPlatformId()).name;
			} else {
				boolean isVia = false;
				String text = "";

				for (int i = currentStationIndex + 1; i < route.platformIds.size() - 1; i++) {
					if (stationIdToRoutes.get(platformIdToStation.get(route.platformIds.get(i).platformId).id).size() > 1) {
						text = platformIdToStation.get(route.platformIds.get(i).platformId).name;
						isVia = true;
						break;
					}
				}

				if (!isVia) {
					text = platformIdToStation.get(route.getLastPlatformId()).name;
				}

				final String translationString = String.format("%s_%s", route.circularState == Route.CircularState.CLOCKWISE ? "clockwise" : "anticlockwise", isVia ? "via" : "to");
				return circularMarker + IGui.insertTranslation("gui.mtr." + translationString + "_cjk", "gui.mtr." + translationString, 1, text);
			}
		} catch (Exception ignored) {
			return "";
		}
	}

	public DynamicResource getColorStrip(long platformId) {
		return getResource(String.format("color_%s", platformId), () -> RouteMapGenerator.generateColorStrip(platformId), DefaultRenderingColor.TRANSPARENT);
	}

	public DynamicResource getStationName(String stationName, float aspectRatio) {
		return getResource(String.format("station_name_%s_%s", stationName, aspectRatio), () -> RouteMapGenerator.generateStationName(stationName, aspectRatio), DefaultRenderingColor.TRANSPARENT);
	}

	public DynamicResource getTallStationName(int textColor, String stationName, int stationColor, float aspectRatio) {
		return getResource(String.format("tall_station_name_%s_%s_%s_%s", textColor, stationName, stationColor, aspectRatio), () -> RouteMapGenerator.generateTallStationName(textColor, stationName, stationColor, aspectRatio), DefaultRenderingColor.TRANSPARENT);
	}

	public DynamicResource getStationNameEntrance(int textColor, String stationName, float aspectRatio) {
		return getResource(String.format("station_name_entrance_%s_%s_%s", textColor, stationName, aspectRatio), () -> RouteMapGenerator.generateStationNameEntrance(textColor, stationName, aspectRatio), DefaultRenderingColor.TRANSPARENT);
	}

	public DynamicResource getSingleRowStationName(long platformId, float aspectRatio) {
		return getResource(String.format("single_row_station_name_%s_%s", platformId, aspectRatio), () -> RouteMapGenerator.generateSingleRowStationName(platformId, aspectRatio), DefaultRenderingColor.WHITE);
	}

	public DynamicResource getSignText(String text, HorizontalAlignment horizontalAlignment, float paddingScale, int backgroundColor, int textColor) {
		return getResource(String.format("sign_text_%s_%s_%s_%s_%s", text, horizontalAlignment, paddingScale, backgroundColor, textColor), () -> RouteMapGenerator.generateSignText(text, horizontalAlignment, paddingScale, backgroundColor, textColor), DefaultRenderingColor.TRANSPARENT);
	}

	public DynamicResource getLiftPanelDisplay(String originalText, int textColor) {
		return getResource(String.format("lift_panel_display_%s", originalText), () -> RouteMapGenerator.generateLiftPanel(originalText, textColor), DefaultRenderingColor.BLACK);
	}

	public DynamicResource getExitSignLetter(String exitLetter, String exitNumber, int backgroundColor) {
		return getResource(String.format("exit_sign_letter_%s_%s", exitLetter, exitNumber), () -> RouteMapGenerator.generateExitSignLetter(exitLetter, exitNumber, backgroundColor), DefaultRenderingColor.TRANSPARENT);
	}

	public DynamicResource getRouteSquare(int color, String routeName, IGui.HorizontalAlignment horizontalAlignment) {
		return getResource(String.format("route_square_%s_%s_%s", color, routeName, horizontalAlignment), () -> RouteMapGenerator.generateRouteSquare(color, routeName, horizontalAlignment), DefaultRenderingColor.TRANSPARENT);
	}

	public DynamicResource getDirectionArrow(long platformId, boolean hasLeft, boolean hasRight, IGui.HorizontalAlignment horizontalAlignment, boolean showToString, float paddingScale, float aspectRatio, int backgroundColor, int textColor, int transparentColor) {
		return getResource(String.format("direction_arrow_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s", platformId, hasLeft, hasRight, horizontalAlignment, showToString, paddingScale, aspectRatio, backgroundColor, textColor, transparentColor), () -> RouteMapGenerator.generateDirectionArrow(platformId, hasLeft, hasRight, horizontalAlignment, showToString, paddingScale, aspectRatio, backgroundColor, textColor, transparentColor), transparentColor == 0 && backgroundColor == ARGB_WHITE ? DefaultRenderingColor.WHITE : DefaultRenderingColor.TRANSPARENT);
	}

	public DynamicResource getRouteMap(long platformId, boolean vertical, boolean flip, float aspectRatio, boolean transparentWhite) {
		return getResource(String.format("route_map_%s_%s_%s_%s_%s", platformId, vertical, flip, aspectRatio, transparentWhite), () -> RouteMapGenerator.generateRouteMap(platformId, vertical, flip, aspectRatio, transparentWhite), transparentWhite ? DefaultRenderingColor.TRANSPARENT : DefaultRenderingColor.WHITE);
	}

	public byte[] getTextPixels(String text, int[] dimensions, int fontSizeCjk, int fontSize) {
		return getTextPixels(text, dimensions, Integer.MAX_VALUE, (int) (Math.max(fontSizeCjk, fontSize) * LINE_HEIGHT_MULTIPLIER), fontSizeCjk, fontSize, 0, null);
	}

	public byte[] getTextPixels(String text, int[] dimensions, int maxWidth, int maxHeight, int fontSizeCjk, int fontSize, int padding, IGui.HorizontalAlignment horizontalAlignment) {
		if (maxWidth <= 0) {
			dimensions[0] = 0;
			dimensions[1] = 0;
			return new byte[0];
		}

		final boolean oneRow = horizontalAlignment == null;
		final String[] textSplit = IGui.textOrUntitled(text).split("\\|");
		final AttributedString[] attributedStrings = new AttributedString[textSplit.length];
		final int[] textWidths = new int[textSplit.length];
		final int[] fontSizes = new int[textSplit.length];
		final FontRenderContext context = new FontRenderContext(new AffineTransform(), false, false);
		int width = 0;
		int height = 0;

		for (int index = 0; index < textSplit.length; index++) {
			final int newFontSize = IGui.isCjk(textSplit[index]) || font.canDisplayUpTo(textSplit[index]) >= 0 ? fontSizeCjk : fontSize;
			attributedStrings[index] = new AttributedString(textSplit[index]);
			fontSizes[index] = newFontSize;

			final Font fontSized = font.deriveFont(Font.PLAIN, newFontSize);
			final Font fontCjkSized = fontCjk.deriveFont(Font.PLAIN, newFontSize);

			for (int characterIndex = 0; characterIndex < textSplit[index].length(); characterIndex++) {
				final char character = textSplit[index].charAt(characterIndex);
				final Font newFont;
				if (fontSized.canDisplay(character)) {
					newFont = fontSized;
				} else if (fontCjkSized.canDisplay(character)) {
					newFont = fontCjkSized;
				} else {
					Font defaultFont = null;
					for (final Font testFont : GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts()) {
						if (testFont.canDisplay(character)) {
							defaultFont = testFont;
							break;
						}
					}
					newFont = (defaultFont == null ? new Font(null) : defaultFont).deriveFont(Font.PLAIN, newFontSize);
				}
				textWidths[index] += newFont.getStringBounds(textSplit[index].substring(characterIndex, characterIndex + 1), context).getBounds().width;
				attributedStrings[index].addAttribute(TextAttribute.FONT, newFont, characterIndex, characterIndex + 1);
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
		final int imageHeight = Math.min(height, maxHeight);
		final BufferedImage image = new BufferedImage(width + (oneRow ? 0 : padding * 2), imageHeight + (oneRow ? 0 : padding * 2), BufferedImage.TYPE_BYTE_GRAY);
		final Graphics2D graphics2D = image.createGraphics();
		graphics2D.setColor(Color.WHITE);
		graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		for (int index = 0; index < textSplit.length; index++) {
			if (oneRow) {
				graphics2D.drawString(attributedStrings[index].getIterator(), textOffset, height / LINE_HEIGHT_MULTIPLIER);
				textOffset += textWidths[index] + padding;
			} else {
				final float scaleY = (float) imageHeight / height;
				final float textWidth = Math.min(maxWidth, textWidths[index] * scaleY);
				final float scaleX = textWidth / textWidths[index];
				final AffineTransform stretch = new AffineTransform();
				stretch.concatenate(AffineTransform.getScaleInstance(scaleX, scaleY));
				graphics2D.setTransform(stretch);
				graphics2D.drawString(attributedStrings[index].getIterator(), horizontalAlignment.getOffset(0, textWidth - width) / scaleY + padding / scaleX, textOffset + fontSizes[index] + padding / scaleY);
				textOffset += fontSizes[index] * LINE_HEIGHT_MULTIPLIER;
			}
		}

		dimensions[0] = width + (oneRow ? 0 : padding * 2);
		dimensions[1] = imageHeight + (oneRow ? 0 : padding * 2);
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

	private DynamicResource getResource(String key, Supplier<NativeImage> supplier, DefaultRenderingColor defaultRenderingColor) {
		final Minecraft minecraftClient = Minecraft.getInstance();
		if (font == null || fontCjk == null) {
			final ResourceManager resourceManager = minecraftClient.getResourceManager();
			try {
				font = Font.createFont(Font.TRUETYPE_FONT, Utilities.getInputStream(resourceManager.getResource(new ResourceLocation(MTR.MOD_ID, "font/noto-sans-semibold.ttf"))));
				fontCjk = Font.createFont(Font.TRUETYPE_FONT, Utilities.getInputStream(resourceManager.getResource(new ResourceLocation(MTR.MOD_ID, "font/noto-serif-cjk-tc-semibold.ttf"))));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (!resourceRegistryQueue.isEmpty()) {
			resourceRegistryQueue.remove(0).run();
		}

		final boolean needsRefresh = resourcesToRefresh.contains(key);
		final DynamicResource dynamicResource = dynamicResources.get(key);

		if (dynamicResource != null && !needsRefresh) {
			return dynamicResource;
		}

		RouteMapGenerator.setConstants();
		CompletableFuture.supplyAsync(supplier).thenAccept(nativeImage -> resourceRegistryQueue.add(() -> {
			final DynamicResource staticTextureProviderOld = dynamicResources.get(key);
			if (staticTextureProviderOld != null) {
				staticTextureProviderOld.remove();
			}

			final DynamicResource dynamicResourceNew;
			if (nativeImage == null) {
				dynamicResourceNew = defaultRenderingColor.dynamicResource;
			} else {
				final DynamicTexture dynamicTexture = new DynamicTexture(nativeImage);
				final ResourceLocation resourceLocation = new ResourceLocation(MTR.MOD_ID, "dynamic_texture_" + key.toLowerCase(Locale.ENGLISH).replaceAll("[^0-9a-z_]", ""));
				minecraftClient.getTextureManager().register(resourceLocation, dynamicTexture);
				dynamicResourceNew = new DynamicResource(resourceLocation, dynamicTexture);
			}

			dynamicResources.put(key, dynamicResourceNew);
		}));

		if (needsRefresh) {
			resourcesToRefresh.remove(key);
		}

		if (dynamicResource == null) {
			dynamicResources.put(key, defaultRenderingColor.dynamicResource);
			return defaultRenderingColor.dynamicResource;
		} else {
			return dynamicResource;
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

	public static class DynamicResource {

		public final int width;
		public final int height;
		public final ResourceLocation resourceLocation;

		private DynamicResource(ResourceLocation resourceLocation, DynamicTexture dynamicTexture) {
			this.resourceLocation = resourceLocation;
			if (dynamicTexture != null) {
				final NativeImage nativeImage = dynamicTexture.getPixels();
				if (nativeImage != null) {
					width = nativeImage.getWidth();
					height = nativeImage.getHeight();
				} else {
					width = 16;
					height = 16;
				}
			} else {
				width = 16;
				height = 16;
			}
		}

		private void remove() {
			if (!resourceLocation.equals(DEFAULT_BLACK_RESOURCE) && !resourceLocation.equals(DEFAULT_WHITE_RESOURCE) && !resourceLocation.equals(DEFAULT_TRANSPARENT_RESOURCE)) {
				final TextureManager textureManager = Minecraft.getInstance().getTextureManager();
				textureManager.release(resourceLocation);
				final AbstractTexture abstractTexture = textureManager.getTexture(resourceLocation);
				if (abstractTexture != null) {
					abstractTexture.releaseId();
					abstractTexture.close();
				}
			}
		}
	}

	private enum DefaultRenderingColor {
		BLACK(DEFAULT_BLACK_RESOURCE),
		WHITE(DEFAULT_WHITE_RESOURCE),
		TRANSPARENT(DEFAULT_TRANSPARENT_RESOURCE);

		private final DynamicResource dynamicResource;

		DefaultRenderingColor(ResourceLocation resourceLocation) {
			dynamicResource = new DynamicResource(resourceLocation, null);
		}
	}
}
