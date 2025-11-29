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
import org.mtr.core.tool.Utilities;
import org.mtr.font.FontGroupRegistry;
import org.mtr.font.FontRenderOptions;
import org.mtr.generated.resource.SignResourceSchema;
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

	private static final float SMALL_SIGN_PADDING = 0.125F;

	private static final int CACHE_TIMEOUT = 5000;
	private static final GenericLongCache<Station> SIGN_STATION_CACHE = new GenericLongCache<>(CACHE_TIMEOUT, true);
	private static final GenericLongCache<ObjectArrayList<Platform>> SIGN_PLATFORMS_CACHE = new GenericLongCache<>(CACHE_TIMEOUT, true);
	private static final GenericLongCache<ObjectObjectImmutablePair<IntArrayList, String>> SIGN_PLATFORM_COLORS_AND_DESTINATIONS_CACHE = new GenericLongCache<>(CACHE_TIMEOUT, true);
	private static final GenericLongCache<ObjectArrayList<Route>> SIGN_ROUTES_CACHE = new GenericLongCache<>(CACHE_TIMEOUT, true);
	private static final GenericLongCache<ObjectArrayList<Station>> SIGN_STATIONS_CACHE = new GenericLongCache<>(CACHE_TIMEOUT, true);

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

		final ObjectArrayList<Consumer<Drawing>> deferredRenders = new ObjectArrayList<>();

		// Draw sign tiles and text
		for (int i = 0; i < signResources.length; i++) {
			final SignResource signResource = signResources[i];

			if (signResource != null) {
				final Drawing textureDrawing = new Drawing(matrixStack, vertexConsumerProvider.getBuffer(RenderLayer.getGuiTextured(signResource.textureId)));

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
						deferredRenders.add(drawing -> FontGroupRegistry.MTR.get().render(drawing, signResource.getCustomText(), FontRenderOptions.builder()
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
					final float totalSpace = (getTextSpace(signResources, i) + 1 + (signResource.small ? -1 : 1) * SMALL_SIGN_PADDING) * signSize;

					switch (signResource.signType) {
						case PLATFORM:
							renderPlatforms(
									textureDrawing, deferredRenders,
									x + i * signSize, y, zOffset,
									signSize, getPlatforms(blockPos).stream().filter(platform -> selectedIdsSet.contains(platform.getId())).collect(Collectors.toCollection(ObjectArrayList::new)),
									signResource.flipTexture, signResource.flipCustomText, signResource.small, signResource.getCustomText(),
									totalSpace, renderPlaceholder
							);
							break;
						case ROUTE:
							renderRoutes(
									textureDrawing, deferredRenders,
									x + i * signSize, y, zOffset,
									signSize, getRoutes(blockPos).stream().filter(route -> selectedIdsSet.contains(route.getColor())).collect(Collectors.toCollection(ObjectArrayList::new)),
									signResource.flipTexture, signResource.flipCustomText, signResource.small, signResource.getCustomText(),
									totalSpace, renderPlaceholder
							);
							break;
					}
				}
			}
		}

		final Drawing drawing = new Drawing(matrixStack, vertexConsumerProvider.getBuffer(RenderLayer.getGui()));
		deferredRenders.forEach(deferredRender -> deferredRender.accept(drawing));
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

	public static ObjectObjectImmutablePair<IntArrayList, String> getPlatformColorsAndDestinations(long platformId) {
		return SIGN_PLATFORM_COLORS_AND_DESTINATIONS_CACHE.get(platformId, () -> RouteHelper.getRouteColorsAndDestinationString(platformId, true, false));
	}

	@Nullable
	private static Station getStation(@Nullable BlockPos blockPos) {
		return blockPos == null ? null : SIGN_STATION_CACHE.get(blockPos.asLong(), () -> MTRClient.findStation(blockPos));
	}

	private static void renderPlatforms(
			Drawing textureDrawing, ObjectArrayList<Consumer<Drawing>> deferredRenders,
			float x, float y, float zOffset,
			float signSize, ObjectArrayList<Platform> platforms,
			boolean flipTexture, boolean flipText, boolean small, String customText,
			float totalSpace, boolean renderPlaceholder
	) {
		if (platforms.isEmpty() && !renderPlaceholder) {
			return;
		}

		final int platformCount = Math.max(1, platforms.size());
		final float verticalSpace = small ? (1 - SMALL_SIGN_PADDING * 2) * signSize : signSize;
		final float largeTextureSize = verticalSpace / platformCount;
		final float smallTextureSize = (verticalSpace - (platformCount - 1) * SMALL_SIGN_PADDING * signSize / platformCount) / platformCount;
		final float textureSize = small ? smallTextureSize : largeTextureSize;

		final float x1 = flipText ? x + signSize - textureSize - (small ? signSize * SMALL_SIGN_PADDING : 0) : x + (small ? signSize * SMALL_SIGN_PADDING : 0);
		final float x2 = x1 + textureSize;

		for (int i = 0; i < platformCount; i++) {
			final Platform platform = Utilities.getElement(platforms, i);
			final ObjectObjectImmutablePair<IntArrayList, String> platformColorsAndDestinations = platform == null ? null : getPlatformColorsAndDestinations(platform.getId());
			final String platformName = platform == null ? String.valueOf(((System.currentTimeMillis() / 2000) % 4) + 1) : platform.getName();
			final IntArrayList colors = platformColorsAndDestinations == null ? null : platformColorsAndDestinations.left();
			final int colorCount = colors == null ? 1 : colors.size();
			final float y1 = y + (small ? signSize * SMALL_SIGN_PADDING : 0) + i * largeTextureSize;

			// Platform texture
			for (int j = 0; j < colorCount; j++) {
				textureDrawing.setVerticesWH(x1, y1 + textureSize * j / colorCount, textureSize, textureSize / colorCount, -zOffset).setColor(GuiHelper.BLACK_COLOR | (colors == null ? GuiHelper.rainbowColor().getRGB() : colors.getInt(j))).setUv(flipTexture ? 1 : 0, (float) j / colorCount, flipTexture ? 0 : 1, (float) (j + 1) / colorCount).draw();
			}

			deferredRenders.add(textDrawing -> {
				// Platform number
				FontGroupRegistry.MTR.get().render(textDrawing, platformName, FontRenderOptions.builder()
						.horizontalSpace(textureSize * 0.75F)
						.verticalSpace(textureSize * 0.75F)
						.horizontalTextAlignment(FontRenderOptions.Alignment.CENTER)
						.verticalTextAlignment(FontRenderOptions.Alignment.CENTER)
						.horizontalPositioning(FontRenderOptions.Alignment.CENTER)
						.verticalPositioning(FontRenderOptions.Alignment.CENTER)
						.offsetX(x1 + textureSize / 2)
						.offsetY(y1 + textureSize / 2)
						.offsetZ(-zOffset * 2)
						.maxFontSize(signSize)
						.lineBreak(FontRenderOptions.LineBreak.SPLIT)
						.textOverflow(FontRenderOptions.TextOverflow.COMPRESS)
						.build());

				// Platform destination
				final float textSpace = totalSpace - textureSize - signSize * SMALL_SIGN_PADDING;
				if (textSpace > 0) {
					FontGroupRegistry.MTR.get().render(textDrawing, platformColorsAndDestinations == null ? customText : platformColorsAndDestinations.right(), FontRenderOptions.builder()
							.horizontalSpace(textSpace)
							.verticalSpace(Math.min(signSize * (1 - SMALL_SIGN_PADDING * 2), smallTextureSize))
							.horizontalTextAlignment(flipText ? FontRenderOptions.Alignment.END : FontRenderOptions.Alignment.START)
							.verticalTextAlignment(FontRenderOptions.Alignment.CENTER)
							.horizontalPositioning(flipText ? FontRenderOptions.Alignment.END : FontRenderOptions.Alignment.START)
							.verticalPositioning(FontRenderOptions.Alignment.CENTER)
							.offsetX(flipText ? x1 - signSize * SMALL_SIGN_PADDING : x2 + signSize * SMALL_SIGN_PADDING)
							.offsetY(y1 + textureSize / 2)
							.offsetZ(-zOffset)
							.cjkScaling(2)
							.maxFontSize(signSize / 4)
							.lineBreak(FontRenderOptions.LineBreak.SPLIT)
							.textOverflow(FontRenderOptions.TextOverflow.COMPRESS)
							.build());
				}
			});
		}
	}

	private static void renderRoutes(
			Drawing textureDrawing, ObjectArrayList<Consumer<Drawing>> deferredRenders,
			float x, float y, float zOffset,
			float signSize, ObjectArrayList<Route> routes,
			boolean flipTexture, boolean flipText, boolean small, String customText,
			float totalSpace, boolean renderPlaceholder
	) {
		if (routes.isEmpty() && !renderPlaceholder) {
			return;
		}

		final int routeCount = Math.max(1, routes.size());
		final float height = signSize * (small ? 1 - SMALL_SIGN_PADDING * 2 : 1);
		final float paddingBetweenRoutes = signSize * SMALL_SIGN_PADDING / 2;
		final float[] routeTextWidths = new float[routeCount];
		final String[] routeNames = new String[routeCount];
		float rawWidth = signSize * SMALL_SIGN_PADDING * 2 * routeCount + paddingBetweenRoutes * (routeCount - 1);

		// Calculate text widths
		for (int i = 0; i < routeCount; i++) {
			final Route route = Utilities.getElement(routes, i);
			routeNames[i] = route == null ? customText : route.getName().split("\\|\\|")[0];
			routeTextWidths[i] = FontGroupRegistry.MTR.get().render(null, routeNames[i], FontRenderOptions.builder()
					.verticalSpace(height - signSize * SMALL_SIGN_PADDING * 2)
					.cjkScaling(2)
					.maxFontSize(signSize / 4)
					.lineBreak(FontRenderOptions.LineBreak.SPLIT)
					.build()).leftFloat();
			rawWidth += routeTextWidths[i];
		}

		final float scale = Math.min(1, totalSpace / rawWidth);
		final float y1 = y + (small ? signSize * SMALL_SIGN_PADDING : 0);
		float x1 = x + (flipText ? -1 : 1) * (small ? signSize * SMALL_SIGN_PADDING : 0) + (flipText ? signSize - rawWidth * scale : 0);

		// Route texture (tiled to the right length)
		for (int i = 0; i < routeCount; i++) {
			final Route route = Utilities.getElement(routes, i);
			final float textureWidth = (routeTextWidths[i] + signSize * SMALL_SIGN_PADDING * 2) * scale;
			final int color = GuiHelper.BLACK_COLOR | (route == null ? GuiHelper.rainbowColor().getRGB() : route.getColor());
			final float endTextureWidth;

			if (textureWidth <= height) {
				endTextureWidth = textureWidth / 2;
			} else {
				endTextureWidth = height / 4;
				for (float j = 0; j < textureWidth - height / 2; j += height / 2) {
					final float newX1 = x1 + endTextureWidth + j;
					final float newX2 = Math.min(x1 + textureWidth - endTextureWidth, newX1 + height / 2);
					final float u1 = endTextureWidth / height;
					final float u2 = u1 + (newX2 - newX1) / height;
					textureDrawing.setVertices(newX1, y1, newX2, y1 + height, -zOffset).setColor(color).setUv(flipTexture ? u2 : u1, 0, flipTexture ? u1 : u2, 1).draw();
				}
			}

			final float u1 = endTextureWidth / height;
			final float u2 = 1 - u1;
			textureDrawing.setVerticesWH(x1, y1, endTextureWidth, height, -zOffset).setColor(color).setUv(flipTexture ? u1 : 0, 0, flipTexture ? 0 : u1, 1).draw();
			textureDrawing.setVerticesWH(x1 + textureWidth - endTextureWidth, y1, endTextureWidth, height, -zOffset).setColor(color).setUv(flipTexture ? 1 : u2, 0, flipTexture ? u2 : 1, 1).draw();

			final float textStart = x1 + signSize * SMALL_SIGN_PADDING;
			final float textWidth = textureWidth - signSize * SMALL_SIGN_PADDING * 2;
			final String routeName = routeNames[i];

			if (textWidth > 0) {
				deferredRenders.add(textDrawing -> FontGroupRegistry.MTR.get().render(textDrawing, routeName, FontRenderOptions.builder()
						.horizontalSpace(textWidth)
						.verticalSpace(height - signSize * SMALL_SIGN_PADDING * 2)
						.horizontalTextAlignment(flipText ? FontRenderOptions.Alignment.END : FontRenderOptions.Alignment.START)
						.verticalTextAlignment(FontRenderOptions.Alignment.CENTER)
						.verticalPositioning(FontRenderOptions.Alignment.CENTER)
						.offsetX(textStart)
						.offsetY(y1 + height / 2)
						.offsetZ(-zOffset * 2)
						.cjkScaling(2)
						.maxFontSize(signSize / 4)
						.lineBreak(FontRenderOptions.LineBreak.SPLIT)
						.textOverflow(FontRenderOptions.TextOverflow.COMPRESS)
						.build()));
			}

			x1 += textureWidth + paddingBetweenRoutes * scale;
		}
	}

	private static float getTextSpace(SignResource[] signResources, int index) {
		final SignResource signResource = signResources[index];
		final int direction = signResource.flipCustomText ? -1 : 1;
		int checkIndex = index + direction;
		float space = -SMALL_SIGN_PADDING * (signResource.small ? 1 : 2);

		while (checkIndex >= 0 && checkIndex < signResources.length) {
			final SignResource checkSignResource = signResources[checkIndex];
			if (checkSignResource == null) {
				space++;
			} else {
				if (checkSignResource.small) {
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
