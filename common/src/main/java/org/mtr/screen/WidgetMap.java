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
import net.minecraft.client.util.math.MatrixStack;
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
import org.mtr.tool.Drawing;

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

		final MatrixStack matrixStack = context.getMatrices();

		// Player position indicator
		if (player != null) {
			drawFromWorldCoords(player.getX(), player.getZ(), PLAYER_ARROW_SIZE / 2F, PLAYER_ARROW_SIZE / 2F, (x, y) -> {
				matrixStack.push();
				matrixStack.translate(getX() + x, getY() + y, 5);
				IDrawing.rotateZDegrees(matrixStack, player.getYaw() + 180);
				new Drawing(
						matrixStack,
						MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers().getBuffer(RenderLayer.getGuiTextured(Identifier.of("textures/gui/sprites/mtr/dashboard_player_arrow.png")))
				).setVerticesWH(-PLAYER_ARROW_SIZE / 2F, -PLAYER_ARROW_SIZE / 2F, PLAYER_ARROW_SIZE, PLAYER_ARROW_SIZE).setUv().draw();
				matrixStack.pop();
			});
		}

		final Drawing drawing = new Drawing(matrixStack, MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers().getBuffer(RenderLayer.getGui())).setGuiBoundsWH(getX(), getY(), width, height);
		hoverStations.clear();
		hoverPlatforms.clear();
		hoverDepots.clear();
		hoverSidings.clear();

		// Platforms and sidings
		if (showStations) {
			drawSavedRails(matrixStack, drawing, flatPositionToPlatformsMap, widgetScrollableList == null && (mapState == MapState.DEFAULT || mapState == MapState.EDITING_ROUTE) ? hoverPlatforms : null, mouseX, mouseY);
		}
		if (showDepots) {
			drawSavedRails(matrixStack, drawing, flatPositionToSidingsMap, widgetScrollableList == null && mapState == MapState.DEFAULT ? hoverSidings : null, mouseX, mouseY);
		}

		// Stations and depots
		final boolean canHoverAreas = widgetScrollableList == null && mapState == MapState.DEFAULT && hoverPlatforms.isEmpty() && hoverSidings.isEmpty();
		if (showStations) {
			drawAreas(matrixStack, drawing, MinecraftClientData.getDashboardInstance().stations, canHoverAreas ? hoverStations : null, mouseX, mouseY);
		}
		if (showDepots) {
			drawAreas(matrixStack, drawing, MinecraftClientData.getDashboardInstance().depots, canHoverAreas ? hoverDepots : null, mouseX, mouseY);
		}

		// Editing rectangle
		if (mapState == MapState.EDITING_AREA && drawArea1 != null && drawArea2 != null) {
			drawFromWorldCoords(Math.min(drawArea1.leftInt(), drawArea2.leftInt()), Math.min(drawArea1.rightInt(), drawArea2.rightInt()), 0, 0, (x1, y1) ->
					drawFromWorldCoords(Math.max(drawArea1.leftInt(), drawArea2.leftInt()) + 1, Math.max(drawArea1.rightInt(), drawArea2.rightInt()) + 1, 0, 0, (x2, y2) ->
							drawing.setVertices(getX() + x1, getY() + y1, getX() + x2, getY() + y2, 2).setColor(IGui.ARGB_WHITE_TRANSLUCENT).draw()
					)
			);
		}

		// Hover popup
		if (widgetScrollableList != null) {
			drawing.setVertices(widgetScrollableList.x1, widgetScrollableList.y1, widgetScrollableList.x2, widgetScrollableList.y2, 6).setColor(IGui.ARGB_BLACK).draw();
			drawShadow(drawing, widgetScrollableList.x1, widgetScrollableList.y1, widgetScrollableList.x2, widgetScrollableList.y2, 2, HOVER_WINDOW_SHADOW_RADIUS);

			matrixStack.push();
			matrixStack.translate(0, 0, 6);
			widgetScrollableList.renderWidget(context, mouseX, mouseY, delta);
			matrixStack.pop();

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

	private <T extends SavedRailBase<T, U>, U extends AreaBase<U, T>> void drawAreas(MatrixStack matrixStack, Drawing drawing, ObjectArraySet<U> areas, @Nullable ObjectArrayList<U> hoverDataList, int mouseX, int mouseY) {
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
					drawing.setVertices(x1, y1, x2, y2, 2).setColorARGB(0x66, (int) (0.8 * color.getRed()), (int) (0.8 * color.getGreen()), (int) (0.8 * color.getBlue())).draw();
					drawShadow(drawing, x1, y1, x2, y2, 2, shadowRadius);

					// Draw border
					drawing.setVertices(x1, y1, x1 + borderSize, y2, 2).setColor(IGui.ARGB_BLACK | area.getColor()).draw();
					drawing.setVertices(x2 - borderSize, y1, x2, y2, 2).setColor(IGui.ARGB_BLACK | area.getColor()).draw();
					drawing.setVertices(x1, y1, x2, y1 + borderSize, 2).setColor(IGui.ARGB_BLACK | area.getColor()).draw();
					drawing.setVertices(x1, y2 - borderSize, x2, y2, 2).setColor(IGui.ARGB_BLACK | area.getColor()).draw();

					// Draw overlay text
					final double clampedAreaWidth = areaWidth - Math.max(0, getX() - x1) - Math.max(0, x2 - getX() - width) - AREA_NAME_PADDING * 2;
					final double clampedAreaHeight = areaHeight - Math.max(0, getY() - y1) - Math.max(0, y2 - getY() - height) - AREA_NAME_PADDING * 2;
					if (clampedAreaWidth > 0 && clampedAreaHeight > 0) {
						matrixStack.push();
						matrixStack.translate(Math.max(getX(), x1) + AREA_NAME_PADDING, Math.max(getY(), y1) + AREA_NAME_PADDING, 4);
						FontGroups.renderMTR(drawing, area.getName(), FontRenderOptions.builder()
								.horizontalSpace((float) clampedAreaWidth)
								.verticalSpace((float) clampedAreaHeight)
								.lineBreak(FontRenderOptions.LineBreak.FORCE_ONE_LINE)
								.textOverflow(FontRenderOptions.TextOverflow.SCALE)
								.maxFontSize(4)
								.build()
						);
						matrixStack.pop();
					}
				});
			}
		});
	}

	private <T extends SavedRailBase<T, U>, U extends AreaBase<U, T>> void drawSavedRails(MatrixStack matrixStack, Drawing drawing, Object2ObjectAVLTreeMap<Position, ObjectArrayList<T>> flatPositionToSavedRailsMap, @Nullable ObjectArrayList<T> hoverDataList, int mouseX, int mouseY) {
		flatPositionToSavedRailsMap.forEach((position, savedRails) -> drawFromWorldCoords(position.getX() + 0.5, position.getZ() + 0.5, scale / 2, scale / 2, (x, y) -> {
			final double x1 = getX() + x - scale / 2;
			final double y1 = getY() + y - scale / 2;
			final double x2 = x1 + scale;
			final double y2 = y1 + scale;

			// Check for hover
			if (hoverDataList != null && hoverDataList.isEmpty() && Utilities.isBetween(mouseX, x1, x2) && Utilities.isBetween(mouseY, y1, y2)) {
				hoverDataList.addAll(savedRails);
			}

			// Draw saved rail
			drawing.setVertices(x1, y1, x2, y2, 3).setColor(IGui.ARGB_WHITE).draw();
			drawShadow(drawing, x1, y1, x2, y2, 2, SAVED_RAIL_SHADOW_RADIUS * scale);

			// Draw overlay text
			if (scale > SAVED_RAIL_NAME_PADDING * 2) {
				matrixStack.push();
				matrixStack.translate(x1 + SAVED_RAIL_NAME_PADDING, y1 + SAVED_RAIL_NAME_PADDING, 4);
				FontGroups.renderMTR(drawing, savedRails.stream().map(NameColorDataBase::getName).collect(Collectors.joining("|")), FontRenderOptions.builder()
						.horizontalSpace((float) scale - SAVED_RAIL_NAME_PADDING * 2)
						.verticalSpace((float) scale - SAVED_RAIL_NAME_PADDING * 2)
						.horizontalTextAlignment(FontRenderOptions.Alignment.CENTER)
						.verticalTextAlignment(FontRenderOptions.Alignment.CENTER)
						.textOverflow(FontRenderOptions.TextOverflow.SCALE)
						.color(IGui.ARGB_BLACK)
						.maxFontSize(4)
						.build()
				);
				matrixStack.pop();
			}
		}));
	}

	private void drawShadow(Drawing drawing, double x1, double y1, double x2, double y2, int z, double shadowRadius) {
		final double r1 = shadowRadius > 0 ? shadowRadius : 0;
		final double r2 = shadowRadius < 0 ? -shadowRadius : 0;
		final int color1 = shadowRadius > 0 ? SHADOW_COLOR : 0;
		final int color2 = shadowRadius < 0 ? SHADOW_COLOR : 0;
		drawing.setVertices(x1 - r1, y1 - r1, z, x1 - r1, y2 + r1, z, x1 + r2, y2 - r2, z, x1 + r2, y1 + r2, z).setColor(color2, color2, color1, color1).draw();
		drawing.setVertices(x2 - r2, y1 + r2, z, x2 - r2, y2 - r2, z, x2 + r1, y2 + r1, z, x2 + r1, y1 - r1, z).setColor(color1, color1, color2, color2).draw();
		drawing.setVertices(x1 - r1, y1 - r1, z, x1 + r2, y1 + r2, z, x2 - r2, y1 + r2, z, x2 + r1, y1 - r1, z).setColor(color2, color1, color1, color2).draw();
		drawing.setVertices(x1 + r2, y2 - r2, z, x1 - r1, y2 + r1, z, x2 + r1, y2 + r1, z, x2 - r2, y2 - r2, z).setColor(color1, color2, color2, color1).draw();
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
