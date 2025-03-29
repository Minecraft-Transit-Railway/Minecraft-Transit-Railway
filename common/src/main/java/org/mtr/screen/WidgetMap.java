package org.mtr.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import it.unimi.dsi.fastutil.doubles.DoubleDoubleImmutablePair;
import it.unimi.dsi.fastutil.ints.IntIntImmutablePair;
import it.unimi.dsi.fastutil.longs.Long2FloatAVLTreeMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectAVLTreeMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.joml.Matrix4f;
import org.mtr.MTRClient;
import org.mtr.client.IDrawing;
import org.mtr.client.MinecraftClientData;
import org.mtr.core.data.*;
import org.mtr.core.tool.Utilities;
import org.mtr.data.IGui;
import org.mtr.font.FontGroups;
import org.mtr.font.FontRenderOptions;
import org.mtr.map.MapTileProvider;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;


public final class WidgetMap extends ClickableWidget {

	private double scale;
	private double centerX;
	private double centerY;
	@Nullable
	private IntIntImmutablePair drawArea1, drawArea2;
	private MapState mapState;
	private boolean showStations;
	private boolean showDepots;
	@Nullable
	private WidgetScrollableList<?> widgetScrollableList;

	private final TransportMode transportMode;
	private final BiConsumer<IntIntImmutablePair, IntIntImmutablePair> onDrawCorners;
	private final Runnable onDrawCornersMouseRelease;
	private final ClientPlayerEntity player;
	private final Object2ObjectAVLTreeMap<Position, ObjectArrayList<Platform>> flatPositionToPlatformsMap;
	private final Object2ObjectAVLTreeMap<Position, ObjectArrayList<Siding>> flatPositionToSidingsMap;

	private final Long2FloatAVLTreeMap tileOpacityValues = new Long2FloatAVLTreeMap();
	private final ObjectArrayList<Station> hoverStations = new ObjectArrayList<>();
	private final ObjectArrayList<Platform> hoverPlatforms = new ObjectArrayList<>();
	private final ObjectArrayList<Depot> hoverDepots = new ObjectArrayList<>();
	private final ObjectArrayList<Siding> hoverSidings = new ObjectArrayList<>();

	private static final int PLAYER_ARROW_SIZE = 6;
	private static final int SCALE_UPPER_LIMIT = 64;
	private static final double SCALE_LOWER_LIMIT = 1 / 128D;
	private static final int AREA_NAME_PADDING = 2;
	private static final int AREA_SHADOW_RADIUS = 2;
	private static final int SAVED_RAIL_NAME_PADDING = 1;
	private static final int SAVED_RAIL_SHADOW_RADIUS = 1;
	private static final int HOVER_WINDOW_SHADOW_RADIUS = 8;
	private static final int SHADOW_COLOR = 0x11000000;
	private static final float DARKEN_MAP = 0.8F;

	public WidgetMap(TransportMode transportMode, BiConsumer<IntIntImmutablePair, IntIntImmutablePair> onDrawCorners, Runnable onDrawCornersMouseRelease) {
		super(0, 0, 0, 0, Text.empty());
		this.transportMode = transportMode;
		this.onDrawCorners = onDrawCorners;
		this.onDrawCornersMouseRelease = onDrawCornersMouseRelease;

		player = MinecraftClient.getInstance().player;
		if (player == null) {
			centerX = 0;
			centerY = 0;
		} else {
			centerX = player.getX();
			centerY = player.getZ();
		}

		scale = 1;
		setShowData(true, true);

		flatPositionToPlatformsMap = MinecraftClientData.getFlatPositionToSavedRails(MinecraftClientData.getDashboardInstance().platforms, transportMode);
		flatPositionToSidingsMap = MinecraftClientData.getFlatPositionToSavedRails(MinecraftClientData.getDashboardInstance().sidings, transportMode);
	}

	@Override
	protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
		// Draw layers (z):
		// 0: Black background
		// 1: Map tile
		// 2: Areas and all shadows
		// 3: Saved rails
		// 4: Overlay text
		// 5: Player position indicator
		// 6: Hover popup

		// Background
		context.fill(getX(), getY(), getX() + width, getY() + height, IGui.ARGB_BLACK);

		context.enableScissor(getX(), getY(), getX() + width, getY() + height);

		// World map
		final MapTileProvider mapTileProvider = MTRClient.getMapTileProvider();
		if (mapTileProvider != null) {
			final double tileSize = scale * MapTileProvider.TILE_SIZE;
			final DoubleDoubleImmutablePair topLeftWorldCoords = coordsToWorldPos(0D, 0D);
			final float offsetX = clampTileSize(topLeftWorldCoords.leftDouble()) - (float) topLeftWorldCoords.leftDouble();
			final float offsetY = clampTileSize(topLeftWorldCoords.rightDouble()) - (float) topLeftWorldCoords.rightDouble();
			RenderLayer.getGui().startDrawing();

			for (double x = 0; x < width + tileSize; x += tileSize) {
				for (double y = 0; y < height + tileSize; y += tileSize) {
					final DoubleDoubleImmutablePair worldCoords = coordsToWorldPos(x, y);
					final BlockPos tilePos = new BlockPos(clampTileSize(worldCoords.leftDouble()), player.getBlockPos().getY(), clampTileSize(worldCoords.rightDouble()));
					final long key = tilePos.asLong();
					final VertexBuffer vertexBuffer = mapTileProvider.getTile(tilePos);

					if (vertexBuffer == null) {
						tileOpacityValues.remove(key);
					} else {
						final float opacity = tileOpacityValues.get(key);
						final float newOpacity = Math.min(1, opacity + delta / 10);

						if (opacity < 1) {
							tileOpacityValues.put(key, newOpacity);
						}

						final float newX = (float) x + getX();
						final float newY = (float) y + getY();
						IDrawing.changeShaderColor(new Color(newOpacity * DARKEN_MAP, newOpacity * DARKEN_MAP, newOpacity * DARKEN_MAP, 1), () -> {
							vertexBuffer.bind();
							vertexBuffer.draw(
									new Matrix4f(RenderSystem.getModelViewMatrix()).translate(newX, newY, 0).scale((float) scale, (float) scale, 1).translate(offsetX, offsetY, 1),
									RenderSystem.getProjectionMatrix(),
									RenderSystem.getShader()
							);
						});
					}
				}
			}

			VertexBuffer.unbind();
			RenderLayer.getGui().endDrawing();
		}

		// Player position indicator
		if (player != null) {
			drawFromWorldCoords(player.getX(), player.getZ(), PLAYER_ARROW_SIZE / 2F, PLAYER_ARROW_SIZE / 2F, (x, y) -> {
				context.getMatrices().push();
				context.getMatrices().translate(getX() + x, getY() + y, 5);
				IDrawing.rotateZDegrees(context.getMatrices(), player.getYaw() + 180);
				final Matrix4f matrix4f = context.getMatrices().peek().getPositionMatrix();
				final VertexConsumer vertexConsumer = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers().getBuffer(RenderLayer.getGuiTextured(Identifier.of("textures/gui/sprites/mtr/dashboard_player_arrow.png")));
				final float x1 = -PLAYER_ARROW_SIZE / 2F;
				final float y1 = -PLAYER_ARROW_SIZE / 2F;
				final float x2 = x1 + PLAYER_ARROW_SIZE;
				final float y2 = y1 + PLAYER_ARROW_SIZE;
				vertexConsumer.vertex(matrix4f, x1, y1, 0).texture(0, 0).color(IGui.ARGB_WHITE);
				vertexConsumer.vertex(matrix4f, x1, y2, 0).texture(0, 1).color(IGui.ARGB_WHITE);
				vertexConsumer.vertex(matrix4f, x2, y2, 0).texture(1, 1).color(IGui.ARGB_WHITE);
				vertexConsumer.vertex(matrix4f, x2, y1, 0).texture(1, 0).color(IGui.ARGB_WHITE);
				context.getMatrices().pop();
			});
		}

		final VertexConsumer vertexConsumer = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers().getBuffer(RenderLayer.getGui());
		hoverStations.clear();
		hoverPlatforms.clear();
		hoverDepots.clear();
		hoverSidings.clear();

		// Platforms and sidings
		if (showStations) {
			drawSavedRails(context, vertexConsumer, flatPositionToPlatformsMap, widgetScrollableList == null && (mapState == MapState.DEFAULT || mapState == MapState.EDITING_ROUTE) ? hoverPlatforms : null, mouseX, mouseY);
		}
		if (showDepots) {
			drawSavedRails(context, vertexConsumer, flatPositionToSidingsMap, widgetScrollableList == null && mapState == MapState.DEFAULT ? hoverSidings : null, mouseX, mouseY);
		}

		// Stations and depots
		final boolean canHoverAreas = widgetScrollableList == null && mapState == MapState.DEFAULT && hoverPlatforms.isEmpty() && hoverSidings.isEmpty();
		if (showStations) {
			drawAreas(context, vertexConsumer, MinecraftClientData.getDashboardInstance().stations, canHoverAreas ? hoverStations : null, mouseX, mouseY);
		}
		if (showDepots) {
			drawAreas(context, vertexConsumer, MinecraftClientData.getDashboardInstance().depots, canHoverAreas ? hoverDepots : null, mouseX, mouseY);
		}

		// Editing rectangle
		if (mapState == MapState.EDITING_AREA && drawArea1 != null && drawArea2 != null) {
			drawFromWorldCoords(Math.min(drawArea1.leftInt(), drawArea2.leftInt()), Math.min(drawArea1.rightInt(), drawArea2.rightInt()), 0, 0, (x1, y1) ->
					drawFromWorldCoords(Math.max(drawArea1.leftInt(), drawArea2.leftInt()) + 1, Math.max(drawArea1.rightInt(), drawArea2.rightInt()) + 1, 0, 0, (x2, y2) ->
							draw(context.getMatrices().peek().getPositionMatrix(), vertexConsumer, getX() + x1, getY() + y1, getX() + x2, getY() + y2, 2, IGui.ARGB_WHITE_TRANSLUCENT)
					)
			);
		}

		// Hover popup
		if (widgetScrollableList != null) {
			final Matrix4f matrix4f = context.getMatrices().peek().getPositionMatrix();
			draw(matrix4f, vertexConsumer, widgetScrollableList.x1, widgetScrollableList.y1, widgetScrollableList.x2, widgetScrollableList.y2, 6, IGui.ARGB_BLACK);
			drawShadow(matrix4f, vertexConsumer, widgetScrollableList.x1, widgetScrollableList.y1, widgetScrollableList.x2, widgetScrollableList.y2, 2, HOVER_WINDOW_SHADOW_RADIUS);

			context.getMatrices().push();
			context.getMatrices().translate(0, 0, 6);
			widgetScrollableList.renderWidget(context, mouseX, mouseY, delta);
			context.getMatrices().pop();

			if (!Utilities.isBetween(mouseX, widgetScrollableList.x1, widgetScrollableList.x2) || !Utilities.isBetween(mouseY, widgetScrollableList.y1, widgetScrollableList.y2)) {
				widgetScrollableList = null;
			}
		}

		context.disableScissor();
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
		if (widgetScrollableList == null) {
			if (mapState == MapState.EDITING_AREA) {
				drawArea2 = coordsToWorldPos((int) Math.round(mouseX - getX()), (int) Math.round(mouseY - getY()));
				onDrawCorners.accept(drawArea1, drawArea2);
			} else {
				centerX -= deltaX / scale;
				centerY -= deltaY / scale;
			}
		} else {
			widgetScrollableList.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
		}

		return true;
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		if (mapState == MapState.EDITING_AREA) {
			onDrawCornersMouseRelease.run();
			drawArea1 = null;
			drawArea2 = null;
		}

		if (widgetScrollableList == null) {
			final int x1 = getX() + HOVER_WINDOW_SHADOW_RADIUS;
			final int y1 = getY() + HOVER_WINDOW_SHADOW_RADIUS;
			final int x2 = getX() + width - HOVER_WINDOW_SHADOW_RADIUS;
			final int y2 = getY() + height - HOVER_WINDOW_SHADOW_RADIUS;
			if (!hoverStations.isEmpty()) {
				widgetScrollableList = WidgetScrollableList.createFlexible(new ObjectArrayList<>(hoverStations), ObjectArrayList.of(), station -> Utilities.formatName(station.getName()), (context, siding) -> {
					context.fill(WidgetScrollableList.PADDING, WidgetScrollableList.PADDING, WidgetScrollableList.PADDING + WidgetScrollableList.FONT_SIZE, WidgetScrollableList.PADDING + WidgetScrollableList.FONT_SIZE, IGui.ARGB_BLACK | siding.getColor());
				}, WidgetScrollableList.PADDING + WidgetScrollableList.FONT_SIZE, (int) mouseX, (int) mouseY, x1, y1, x2, y2);
			}
			if (!hoverPlatforms.isEmpty()) {
				widgetScrollableList = WidgetScrollableList.createFlexible(new ObjectArrayList<>(hoverPlatforms), ObjectArrayList.of(), platform -> Utilities.formatName(platform.getName()), (context, siding) -> {
					context.fill(WidgetScrollableList.PADDING, WidgetScrollableList.PADDING, WidgetScrollableList.PADDING + WidgetScrollableList.FONT_SIZE, WidgetScrollableList.PADDING + WidgetScrollableList.FONT_SIZE, IGui.ARGB_BLACK | siding.getColor());
				}, WidgetScrollableList.PADDING + WidgetScrollableList.FONT_SIZE, (int) mouseX, (int) mouseY, x1, y1, x2, y2);
			}
			if (!hoverDepots.isEmpty()) {
				widgetScrollableList = WidgetScrollableList.createFlexible(new ObjectArrayList<>(hoverDepots), ObjectArrayList.of(), depot -> Utilities.formatName(depot.getName()), (context, siding) -> {
					context.fill(WidgetScrollableList.PADDING, WidgetScrollableList.PADDING, WidgetScrollableList.PADDING + WidgetScrollableList.FONT_SIZE, WidgetScrollableList.PADDING + WidgetScrollableList.FONT_SIZE, IGui.ARGB_BLACK | siding.getColor());
				}, WidgetScrollableList.PADDING + WidgetScrollableList.FONT_SIZE, (int) mouseX, (int) mouseY, x1, y1, x2, y2);
			}
			if (!hoverSidings.isEmpty()) {
				widgetScrollableList = WidgetScrollableList.createFlexible(new ObjectArrayList<>(hoverSidings), ObjectArrayList.of(), siding -> Utilities.formatName(siding.getName()), (context, siding) -> {
					context.fill(WidgetScrollableList.PADDING, WidgetScrollableList.PADDING, WidgetScrollableList.PADDING + WidgetScrollableList.FONT_SIZE, WidgetScrollableList.PADDING + WidgetScrollableList.FONT_SIZE, IGui.ARGB_BLACK | siding.getColor());
				}, WidgetScrollableList.PADDING + WidgetScrollableList.FONT_SIZE, (int) mouseX, (int) mouseY, x1, y1, x2, y2);
			}
		}

		return true;
	}

	@Override
	public void onRelease(double mouseX, double mouseY) {
		if (widgetScrollableList != null) {
			widgetScrollableList.onRelease(mouseX, mouseY);
		}

		super.onRelease(mouseX, mouseY);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (widgetScrollableList == null) {
			if (isMouseOver(mouseX, mouseY)) {
				if (MinecraftClientData.hasPermission()) {
					if (mapState == MapState.EDITING_AREA) {
						drawArea1 = coordsToWorldPos((int) (mouseX - getX()), (int) (mouseY - getY()));
						drawArea2 = null;
					}
				}
				return true;
			} else {
				return false;
			}
		} else {
			return widgetScrollableList.mouseClicked(mouseX, mouseY, button);
		}
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
		if (widgetScrollableList == null) {
			final double oldScale = scale;
			if (oldScale > SCALE_LOWER_LIMIT && verticalAmount < 0) {
				centerX -= (mouseX - getX() - width / 2D) / scale;
				centerY -= (mouseY - getY() - height / 2D) / scale;
			}
			scale(verticalAmount);
			if (oldScale < SCALE_UPPER_LIMIT && verticalAmount > 0) {
				centerX += (mouseX - getX() - width / 2D) / scale;
				centerY += (mouseY - getY() - height / 2D) / scale;
			}
		} else {
			widgetScrollableList.mouseScrolled(mouseX, mouseY, 0, verticalAmount);
		}

		return true;
	}

	@Override
	public void setFocused(boolean focused) {
	}

	@Override
	public boolean isFocused() {
		return false;
	}

	@Override
	protected void appendClickableNarrations(NarrationMessageBuilder builder) {
	}

	public void setPositionAndSize(int x, int y, int width, int height) {
		setX(x);
		setY(y);
		this.width = width;
		this.height = height;
	}

	public void scale(double amount) {
		scale *= Math.pow(2, amount);
		scale = MathHelper.clamp(scale, SCALE_LOWER_LIMIT, SCALE_UPPER_LIMIT);
	}

	public void find(Position position) {
		centerX = position.getX();
		centerY = position.getZ();
		scale = Math.max(8, scale);
	}

	public void startEditingArea() {
		mapState = MapState.EDITING_AREA;
		drawArea1 = null;
		drawArea2 = null;
	}

	public void startEditingRoute() {
		mapState = MapState.EDITING_ROUTE;
	}

	public void stopEditing() {
		mapState = MapState.DEFAULT;
	}

	public void setShowData(boolean showStations, boolean showDepots) {
		this.showStations = showStations;
		this.showDepots = showDepots;
	}

	private <T extends SavedRailBase<T, U>, U extends AreaBase<U, T>> void drawAreas(DrawContext context, VertexConsumer vertexConsumer, ObjectArraySet<U> areas, @Nullable ObjectArrayList<U> hoverDataList, int mouseX, int mouseY) {
		areas.forEach(area -> {
			if (area.isTransportMode(transportMode) && AreaBase.validCorners(area)) {
				final double areaWidth = (area.getMaxX() + 1 - area.getMinX()) * scale;
				final double areaHeight = (area.getMaxZ() + 1 - area.getMinZ()) * scale;
				final double shadowRadius = AREA_SHADOW_RADIUS * scale;

				drawFromWorldCoords((area.getMinX() + area.getMaxX() + 1) / 2F, (area.getMinZ() + area.getMaxZ() + 1) / 2F, areaWidth / 2 + shadowRadius, areaHeight / 2 + shadowRadius, (x, y) -> {
					final double x1 = getX() + x - areaWidth / 2;
					final double x2 = x1 + areaWidth;
					final double y1 = getY() + y - areaHeight / 2;
					final double y2 = y1 + areaHeight;
					final Matrix4f matrix4f = context.getMatrices().peek().getPositionMatrix();

					// Check for hover
					final double borderSize;
					if (hoverDataList != null && Utilities.isBetween(mouseX, x1, x2) && Utilities.isBetween(mouseY, y1, y2)) {
						hoverDataList.add(area);
						borderSize = AREA_NAME_PADDING / 2F;
					} else {
						borderSize = 1 / Math.min(2, MinecraftClient.getInstance().getWindow().getScaleFactor());
					}

					// Draw area
					final Color color = new Color(area.getColor());
					final Color newColor = new Color((int) (0.8 * color.getRed()), (int) (0.8 * color.getGreen()), (int) (0.8 * color.getBlue()), 0x66);
					draw(matrix4f, vertexConsumer, x1, y1, x2, y2, 2, newColor.getRGB());
					drawShadow(matrix4f, vertexConsumer, x1, y1, x2, y2, 2, shadowRadius);

					// Draw border
					draw(matrix4f, vertexConsumer, x1, y1, x1 + borderSize, y2, 2, IGui.ARGB_BLACK + area.getColor());
					draw(matrix4f, vertexConsumer, x2 - borderSize, y1, x2, y2, 2, IGui.ARGB_BLACK + area.getColor());
					draw(matrix4f, vertexConsumer, x1, y1, x2, y1 + borderSize, 2, IGui.ARGB_BLACK + area.getColor());
					draw(matrix4f, vertexConsumer, x1, y2 - borderSize, x2, y2, 2, IGui.ARGB_BLACK + area.getColor());

					// Draw overlay text
					final double clampedAreaWidth = areaWidth - Math.max(0, getX() - x1) - Math.max(0, x2 - getX() - width) - AREA_NAME_PADDING * 2;
					final double clampedAreaHeight = areaHeight - Math.max(0, getY() - y1) - Math.max(0, y2 - getY() - height) - AREA_NAME_PADDING * 2;
					if (clampedAreaWidth > 0 && clampedAreaHeight > 0) {
						context.getMatrices().push();
						context.getMatrices().translate(Math.max(getX(), x1) + AREA_NAME_PADDING, Math.max(getY(), y1) + AREA_NAME_PADDING, 4);
						FontGroups.renderMTR(
								context.getMatrices().peek().getPositionMatrix(),
								vertexConsumer,
								area.getName(),
								FontRenderOptions.builder()
										.horizontalSpace((float) clampedAreaWidth)
										.verticalSpace((float) clampedAreaHeight)
										.lineBreak(FontRenderOptions.LineBreak.FORCE_ONE_LINE)
										.textOverflow(FontRenderOptions.TextOverflow.SCALE)
										.maxFontSize(4)
										.build()
						);
						context.getMatrices().pop();
					}
				});
			}
		});
	}

	private <T extends SavedRailBase<T, U>, U extends AreaBase<U, T>> void drawSavedRails(DrawContext context, VertexConsumer vertexConsumer, Object2ObjectAVLTreeMap<Position, ObjectArrayList<T>> flatPositionToSavedRailsMap, @Nullable ObjectArrayList<T> hoverDataList, int mouseX, int mouseY) {
		flatPositionToSavedRailsMap.forEach((position, savedRails) -> drawFromWorldCoords(position.getX() + 0.5, position.getZ() + 0.5, scale / 2, scale / 2, (x, y) -> {
			final double x1 = getX() + x - scale / 2;
			final double y1 = getY() + y - scale / 2;
			final double x2 = x1 + scale;
			final double y2 = y1 + scale;
			final Matrix4f matrix4f = context.getMatrices().peek().getPositionMatrix();

			// Check for hover
			if (hoverDataList != null && hoverDataList.isEmpty() && Utilities.isBetween(mouseX, x1, x2) && Utilities.isBetween(mouseY, y1, y2)) {
				hoverDataList.addAll(savedRails);
			}

			// Draw saved rail
			draw(matrix4f, vertexConsumer, x1, y1, x2, y2, 3, IGui.ARGB_WHITE);
			drawShadow(matrix4f, vertexConsumer, x1, y1, x2, y2, 2, SAVED_RAIL_SHADOW_RADIUS * scale);

			// Draw overlay text
			if (scale > SAVED_RAIL_NAME_PADDING * 2) {
				context.getMatrices().push();
				context.getMatrices().translate(x1 + SAVED_RAIL_NAME_PADDING, y1 + SAVED_RAIL_NAME_PADDING, 4);
				FontGroups.renderMTR(
						context.getMatrices().peek().getPositionMatrix(),
						vertexConsumer,
						savedRails.stream().map(NameColorDataBase::getName).collect(Collectors.joining("|")),
						FontRenderOptions.builder()
								.horizontalSpace((float) scale - SAVED_RAIL_NAME_PADDING * 2)
								.verticalSpace((float) scale - SAVED_RAIL_NAME_PADDING * 2)
								.horizontalTextAlignment(FontRenderOptions.Alignment.CENTER)
								.verticalTextAlignment(FontRenderOptions.Alignment.CENTER)
								.textOverflow(FontRenderOptions.TextOverflow.SCALE)
								.color(IGui.ARGB_BLACK)
								.maxFontSize(4)
								.build()
				);
				context.getMatrices().pop();
			}
		}));
	}

	private void draw(Matrix4f matrix4f, VertexConsumer vertexConsumer, double x1, double y1, double x2, double y2, int z, int color) {
		draw(matrix4f, vertexConsumer, x1, y1, x1, y2, x2, y2, x2, y1, z, color, color, color, color);
	}

	private void draw(Matrix4f matrix4f, VertexConsumer vertexConsumer, double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4, int z, int color1, int color2, int color3, int color4) {
		if (Utilities.isIntersecting(Math.min(Math.min(x1, x2), Math.min(x3, x4)), Math.max(Math.max(x1, x2), Math.max(x3, x4)), getX(), getX() + width) && Utilities.isIntersecting(Math.min(Math.min(y1, y2), Math.min(y3, y4)), Math.max(Math.max(y1, y2), Math.max(y3, y4)), getY(), getY() + height)) {
			vertexConsumer.vertex(matrix4f, (float) x1, (float) y1, z).color(color1);
			vertexConsumer.vertex(matrix4f, (float) x2, (float) y2, z).color(color2);
			vertexConsumer.vertex(matrix4f, (float) x3, (float) y3, z).color(color3);
			vertexConsumer.vertex(matrix4f, (float) x4, (float) y4, z).color(color4);
		}
	}

	private void drawShadow(Matrix4f matrix4f, VertexConsumer vertexConsumer, double x1, double y1, double x2, double y2, int z, double shadowRadius) {
		final double r1 = shadowRadius > 0 ? shadowRadius : 0;
		final double r2 = shadowRadius < 0 ? -shadowRadius : 0;
		final int color1 = shadowRadius > 0 ? SHADOW_COLOR : 0;
		final int color2 = shadowRadius < 0 ? SHADOW_COLOR : 0;
		draw(matrix4f, vertexConsumer, x1 - r1, y1 - r1, x1 - r1, y2 + r1, x1 + r2, y2 - r2, x1 + r2, y1 + r2, z, color2, color2, color1, color1);
		draw(matrix4f, vertexConsumer, x2 - r2, y1 + r2, x2 - r2, y2 - r2, x2 + r1, y2 + r1, x2 + r1, y1 - r1, z, color1, color1, color2, color2);
		draw(matrix4f, vertexConsumer, x1 - r1, y1 - r1, x1 + r2, y1 + r2, x2 - r2, y1 + r2, x2 + r1, y1 - r1, z, color2, color1, color1, color2);
		draw(matrix4f, vertexConsumer, x1 + r2, y2 - r2, x1 - r1, y2 + r1, x2 + r1, y2 + r1, x2 - r2, y2 - r2, z, color1, color2, color2, color1);
	}

	private IntIntImmutablePair coordsToWorldPos(int mouseX, int mouseY) {
		final DoubleDoubleImmutablePair worldPos = coordsToWorldPos((double) mouseX, mouseY);
		return new IntIntImmutablePair((int) Math.floor(worldPos.leftDouble()), (int) Math.floor(worldPos.rightDouble()));
	}

	private DoubleDoubleImmutablePair coordsToWorldPos(double mouseX, double mouseY) {
		final double left = (mouseX - width / 2D) / scale + centerX;
		final double right = (mouseY - height / 2D) / scale + centerY;
		return new DoubleDoubleImmutablePair(left, right);
	}

	private void drawFromWorldCoords(double worldX, double worldZ, double xPadding, double yPadding, BiConsumer<Double, Double> callback) {
		final double coordsX = (worldX - centerX) * scale + width / 2D;
		final double coordsY = (worldZ - centerY) * scale + height / 2D;
		if (Utilities.isBetween(coordsX, -xPadding, width + xPadding) && Utilities.isBetween(coordsY, -yPadding, height + yPadding)) {
			callback.accept(coordsX, coordsY);
		}
	}

	private static int clampTileSize(double value) {
		return (int) Math.floor(value / MapTileProvider.TILE_SIZE) * MapTileProvider.TILE_SIZE;
	}

	private enum MapState {DEFAULT, EDITING_AREA, EDITING_ROUTE}
}
