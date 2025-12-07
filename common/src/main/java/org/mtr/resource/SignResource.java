package org.mtr.resource;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import it.unimi.dsi.fastutil.longs.LongArraySet;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.mtr.MTRClient;
import org.mtr.cache.GenericLongCache;
import org.mtr.client.CustomResourceLoader;
import org.mtr.client.MinecraftClientData;
import org.mtr.core.data.*;
import org.mtr.core.serializer.ReaderBase;
import org.mtr.font.FontRenderHelper;
import org.mtr.font.FontRenderOptions;
import org.mtr.generated.resource.SignResourceSchema;
import org.mtr.render.SpecialSignPlatformRenderer;
import org.mtr.render.SpecialSignRouteRenderer;
import org.mtr.render.SpecialSignStationExitRenderer;
import org.mtr.render.SpecialSignStationRenderer;
import org.mtr.screen.RailwaySignScreenNew;
import org.mtr.tool.Drawing;
import org.mtr.tool.GuiHelper;
import org.mtr.tool.RouteHelper;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public final class SignResource extends SignResourceSchema {

	public final Identifier textureId;
	public final boolean hasCustomText;
	/**
	 * Default signs (the ones bundled with Minecraft Transit Railway) have IDs starting with {@code !}
	 */
	public final boolean isDefault;
	public final String signId;

	public static final float SMALL_SIGN_PADDING = 0.125F;

	private static final int CACHE_TIMEOUT = 5000;
	private static final GenericLongCache<Station> SIGN_STATION_CACHE = new GenericLongCache<>(CACHE_TIMEOUT, true);
	private static final GenericLongCache<ObjectArrayList<Platform>> SIGN_PLATFORMS_CACHE = new GenericLongCache<>(CACHE_TIMEOUT, true);
	private static final GenericLongCache<ObjectObjectImmutablePair<IntArrayList, String>> SIGN_PLATFORM_COLORS_AND_DESTINATIONS_CACHE = new GenericLongCache<>(CACHE_TIMEOUT, true);
	private static final GenericLongCache<ObjectArrayList<Route>> SIGN_ROUTES_CACHE = new GenericLongCache<>(CACHE_TIMEOUT, true);
	private static final GenericLongCache<ObjectArrayList<Station>> SIGN_STATIONS_CACHE = new GenericLongCache<>(CACHE_TIMEOUT, true);
	private static final SpecialSignPlatformRenderer SPECIAL_SIGN_PLATFORM_RENDERER = new SpecialSignPlatformRenderer();
	private static final SpecialSignRouteRenderer SPECIAL_SIGN_ROUTE_RENDERER = new SpecialSignRouteRenderer();
	private static final SpecialSignStationRenderer SPECIAL_SIGN_STATION_RENDERER = new SpecialSignStationRenderer();
	private static final SpecialSignStationExitRenderer SPECIAL_SIGN_STATION_EXIT_RENDERER = new SpecialSignStationExitRenderer();

	public SignResource(ReaderBase readerBase) {
		super(readerBase);
		updateData(readerBase);
		textureId = CustomResourceTools.formatIdentifierWithDefault(textureResource, "png");
		hasCustomText = !customText.isEmpty() || signType != SignType.NORMAL;
		isDefault = id.startsWith("!");
		signId = isDefault ? id.substring(1) : id;
	}

	public boolean getFlipTexture() {
		return flipTexture;
	}

	public SignType getSignType() {
		return signType;
	}

	public String getCustomText() {
		return customText.isEmpty() ? "" : Text.translatable(customText).getString();
	}

	public boolean getFlipCustomText() {
		return flipCustomText;
	}

	public boolean getSmall() {
		return small;
	}

	public int getBackgroundColor() {
		return CustomResourceTools.colorStringToInt(backgroundColor);
	}

	public static void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, BlockPos blockPos, LongAVLTreeSet[] selectedIds, String[] signIds, float signSize, float zOffset, boolean renderPlaceholder) {
		final SignResource[] signResources = new SignResource[signIds.length];
		for (int i = 0; i < signIds.length; i++) {
			signResources[i] = CustomResourceLoader.getSignById(signIds[i]);
		}
		render(matrixStack, vertexConsumerProvider, blockPos, 0, 0, selectedIds, signResources, signSize, zOffset, renderPlaceholder);
	}

	public static void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, BlockPos blockPos, float x, float y, LongAVLTreeSet[] selectedIds, SignResource[] signResources, float signSize, float zOffset, boolean renderPlaceholder) {
		final float signPadding = SMALL_SIGN_PADDING * signSize;
		boolean renderBackground = false;
		int backgroundColor = 0;

		// Get sign resources from sign IDs and find the background colour
		for (final SignResource signResource : signResources) {
			if (signResource != null) {
				renderBackground = true;
				backgroundColor = signResource.getBackgroundColor();
				if (backgroundColor != 0) {
					break;
				}
			}
		}

		// Draw background
		if (renderBackground) {
			new Drawing(matrixStack, vertexConsumerProvider.getBuffer(RenderLayer.getGui())).setVerticesWH(x, y, signResources.length * signSize, signSize).setColor(GuiHelper.BLACK_COLOR | backgroundColor).draw();
		}

		final ObjectArrayList<Consumer<MatrixStack>> deferredRenders = new ObjectArrayList<>();

		// Draw sign tiles and text
		for (int i = 0; i < signResources.length; i++) {
			final SignResource signResource = signResources[i];

			if (signResource != null) {
				final Drawing textureDrawing = new Drawing(matrixStack, vertexConsumerProvider.getBuffer(RenderLayer.getGuiTextured(signResource.textureId)));
				final Identifier font = signResource.font.isEmpty() ? FontRenderHelper.MTR_FONT : Identifier.of(signResource.font);

				if (signResource.signType == SignType.NORMAL) {
					textureDrawing.setVertices(
							x + i * signSize + (signResource.small ? signPadding : 0),
							y + (signResource.small ? signPadding : 0),
							x + (i + 1) * signSize - (signResource.small ? signPadding : 0),
							y + signSize - (signResource.small ? signPadding : 0),
							-zOffset
					).setUv(signResource.flipTexture ? 1 : 0, 0, signResource.flipTexture ? 0 : 1, 1).draw();

					final float textSpace = signResource.hasCustomText ? getTextSpace(signResources, i) * signSize : 0;

					if (textSpace > 0) {
						final float offsetX = (i + (signResource.flipCustomText ? 0 : 1) + (signResource.small ? 0 : (signResource.flipCustomText ? -1 : 1) * SMALL_SIGN_PADDING)) * signSize;
						deferredRenders.add(matrixStackNew -> FontRenderHelper.render(matrixStackNew, signResource.getCustomText(), FontRenderOptions.builder()
								.font(font)
								.horizontalSpace(textSpace)
								.verticalSpace(signSize * (1 - SMALL_SIGN_PADDING * 2))
								.horizontalTextAlignment(signResource.flipCustomText ? FontRenderOptions.Alignment.END : FontRenderOptions.Alignment.START)
								.verticalTextAlignment(FontRenderOptions.Alignment.CENTER)
								.horizontalPositioning(signResource.flipCustomText ? FontRenderOptions.Alignment.END : FontRenderOptions.Alignment.START)
								.offsetX(x + offsetX)
								.offsetY(y + signSize * SMALL_SIGN_PADDING)
								.offsetZ(-zOffset)
								.cjkScaling(2)
								.maxFontSize(signSize / 4)
								.lineBreak(FontRenderOptions.LineBreak.SPLIT)
								.textOverflow(FontRenderOptions.TextOverflow.COMPRESS)
								.build())
						);
					}
				} else {
					final LongAVLTreeSet selectedIdsSet = selectedIds[i];
					final float totalSpace = (getTextSpace(signResources, i) + 1 + (signResource.small ? -1 : (signResource.signType == SignType.EXIT || signResource.signType == SignType.ROUTE ? 0 : 1)) * SMALL_SIGN_PADDING) * signSize;

					switch (signResource.signType) {
						case EXIT:
							SPECIAL_SIGN_STATION_EXIT_RENDERER.render(
									textureDrawing, deferredRenders,
									x + i * signSize, y, zOffset,
									signSize, getStationExits(blockPos).stream().filter(stationExit -> selectedIdsSet.contains(RailwaySignScreenNew.serializeExit(stationExit.getName()))).collect(Collectors.toCollection(ObjectArrayList::new)),
									signResource.flipTexture, signResource.flipCustomText, signResource.small, signResource.getCustomText(), font,
									totalSpace, renderPlaceholder
							);
							break;
						case PLATFORM:
							SPECIAL_SIGN_PLATFORM_RENDERER.render(
									textureDrawing, deferredRenders,
									x + i * signSize, y, zOffset,
									signSize, getPlatforms(blockPos).stream().filter(platform -> selectedIdsSet.contains(platform.getId())).collect(Collectors.toCollection(ObjectArrayList::new)),
									signResource.flipTexture, signResource.flipCustomText, signResource.small, signResource.getCustomText(), font,
									totalSpace, renderPlaceholder
							);
							break;
						case ROUTE:
							SPECIAL_SIGN_ROUTE_RENDERER.render(
									textureDrawing, deferredRenders,
									x + i * signSize, y, zOffset,
									signSize, getRoutes(blockPos).stream().filter(route -> selectedIdsSet.contains(route.getColor())).collect(Collectors.toCollection(ObjectArrayList::new)),
									signResource.flipTexture, signResource.flipCustomText, signResource.small, signResource.getCustomText(), font,
									totalSpace, renderPlaceholder
							);
							break;
						case STATION:
							SPECIAL_SIGN_STATION_RENDERER.render(
									textureDrawing, deferredRenders,
									x + i * signSize, y, zOffset,
									signSize, getStations(blockPos).stream().filter(station -> selectedIdsSet.contains(station.getId())).collect(Collectors.toCollection(ObjectArrayList::new)),
									signResource.flipTexture, signResource.flipCustomText, signResource.small, signResource.getCustomText(), font,
									totalSpace, renderPlaceholder
							);
							break;
					}
				}
			}
		}

		deferredRenders.forEach(deferredRender -> deferredRender.accept(matrixStack));
	}

	public static ObjectArrayList<Platform> getPlatforms(@Nullable BlockPos blockPos) {
		return blockPos == null ? new ObjectArrayList<>() : SIGN_PLATFORMS_CACHE.get(blockPos.asLong(), () -> {
			final Station station = getStation(blockPos);
			if (station == null) {
				return new ObjectArrayList<>();
			}

			final ObjectArrayList<Platform> platforms = new ObjectArrayList<>(station.savedRails);
			Collections.sort(platforms);
			return platforms;
		});
	}

	public static ObjectArrayList<Route> getRoutes(@Nullable BlockPos blockPos) {
		return blockPos == null ? new ObjectArrayList<>() : SIGN_ROUTES_CACHE.get(blockPos.asLong(), () -> {
			final Station station = getStation(blockPos);
			if (station == null) {
				return new ObjectArrayList<>();
			}

			final ObjectArrayList<Route> routes = new ObjectArrayList<>();
			final MinecraftClientData minecraftClientData = MinecraftClientData.getInstance();
			final LongArraySet platformIds = station.savedRails.stream().map(NameColorDataBase::getId).collect(Collectors.toCollection(LongArraySet::new));

			minecraftClientData.simplifiedRoutes.forEach(simplifiedRoute -> {
				if (simplifiedRoute.getPlatforms().stream().anyMatch(simplifiedRoutePlatform -> platformIds.contains(simplifiedRoutePlatform.getPlatformId()))) {
					final Route route = new Route(TransportMode.values()[0], minecraftClientData);
					route.setName(simplifiedRoute.getName());
					route.setColor(simplifiedRoute.getColor());
					routes.add(route);
				}
			});

			Collections.sort(routes);
			return routes;
		});
	}

	public static ObjectArrayList<Station> getStations(@Nullable BlockPos blockPos) {
		return blockPos == null ? new ObjectArrayList<>() : SIGN_STATIONS_CACHE.get(blockPos.asLong(), () -> {
			final Station station = getStation(blockPos);
			if (station == null) {
				return new ObjectArrayList<>();
			}

			final ObjectArrayList<Station> stations = new ObjectArrayList<>(station.connectedStations);
			Collections.sort(stations);
			stations.add(0, station);
			return stations;
		});
	}

	public static ObjectArrayList<StationExit> getStationExits(@Nullable BlockPos blockPos) {
		final Station station = blockPos == null ? null : getStation(blockPos);
		return station == null ? new ObjectArrayList<>() : station.getExits();
	}

	public static ObjectObjectImmutablePair<IntArrayList, String> getPlatformColorsAndDestinations(long platformId) {
		return SIGN_PLATFORM_COLORS_AND_DESTINATIONS_CACHE.get(platformId, () -> RouteHelper.getRouteColorsAndDestinationString(platformId, true, false));
	}

	@Nullable
	private static Station getStation(@Nullable BlockPos blockPos) {
		return blockPos == null ? null : SIGN_STATION_CACHE.get(blockPos.asLong(), () -> MTRClient.findStation(blockPos));
	}

	private static float getTextSpace(SignResource[] signResources, int index) {
		final SignResource signResource = signResources[index];
		final int direction = signResource.flipCustomText ? -1 : 1;
		final boolean useRawUnits = (signResource.signType == SignType.EXIT || signResource.signType == SignType.ROUTE) && !signResource.small;
		int checkIndex = index + direction;
		float space = useRawUnits ? 0 : -SMALL_SIGN_PADDING * (signResource.small ? 1 : 2);

		while (checkIndex >= 0 && checkIndex < signResources.length) {
			final SignResource checkSignResource = signResources[checkIndex];
			if (checkSignResource == null) {
				space++;
			} else {
				if (!useRawUnits && checkSignResource.small) {
					space += SMALL_SIGN_PADDING;
				}
				if (checkSignResource.hasCustomText && checkSignResource.flipCustomText != signResource.flipCustomText) {
					space = (space - SMALL_SIGN_PADDING) / 2;
				}
				break;
			}
			checkIndex += direction;
		}

		return space;
	}
}
