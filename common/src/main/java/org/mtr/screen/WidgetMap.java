package org.mtr.screen;

import it.unimi.dsi.fastutil.doubles.DoubleDoubleImmutablePair;
import it.unimi.dsi.fastutil.ints.IntIntImmutablePair;
import it.unimi.dsi.fastutil.objects.Object2ObjectAVLTreeMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.joml.Matrix4f;
import org.mtr.client.MinecraftClientData;
import org.mtr.core.data.*;
import org.mtr.core.tool.Utilities;
import org.mtr.data.IGui;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.map.MapTileProvider;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;


public class WidgetMap extends ClickableWidget implements IGui {

	private double scale;
	private double centerX;
	private double centerY;
	private IntIntImmutablePair drawArea1, drawArea2;
	private MapState mapState;
	private boolean showStations;

	private final TransportMode transportMode;
	private final OnDrawCorners onDrawCorners;
	private final Runnable onDrawCornersMouseRelease;
	private final Consumer<Long> onClickAddPlatformToRoute;
	private final Consumer<SavedRailBase<?, ?>> onClickEditSavedRail;
	private final BiFunction<Double, Double, Boolean> isRestrictedMouseArea;
	private final ClientPlayerEntity player;
	@Nullable
	private final MapTileProvider mapTileProvider;
	private final Object2ObjectAVLTreeMap<Position, ObjectArrayList<Platform>> flatPositionToPlatformMap;
	private final Object2ObjectAVLTreeMap<Position, ObjectArrayList<Siding>> flatPositionToSidingMap;

	private static final int ARGB_BLUE = 0xFF4285F4;
	private static final int SCALE_UPPER_LIMIT = 64;
	private static final double SCALE_LOWER_LIMIT = 1 / 128D;

	public WidgetMap(TransportMode transportMode, OnDrawCorners onDrawCorners, Runnable onDrawCornersMouseRelease, Consumer<Long> onClickAddPlatformToRoute, Consumer<SavedRailBase<?, ?>> onClickEditSavedRail, BiFunction<Double, Double, Boolean> isRestrictedMouseArea) {
		super(0, 0, 0, 0, Text.empty());
		this.transportMode = transportMode;
		this.onDrawCorners = onDrawCorners;
		this.onDrawCornersMouseRelease = onDrawCornersMouseRelease;
		this.onClickAddPlatformToRoute = onClickAddPlatformToRoute;
		this.onClickEditSavedRail = onClickEditSavedRail;
		this.isRestrictedMouseArea = isRestrictedMouseArea;

		final MinecraftClient minecraftClient = MinecraftClient.getInstance();
		final ClientWorld world = minecraftClient.world;
		player = minecraftClient.player;
		if (player == null) {
			centerX = 0;
			centerY = 0;
		} else {
			centerX = player.getX();
			centerY = player.getZ();
		}

		scale = 1;
		mapTileProvider = world == null ? null : new MapTileProvider(world, MapTileProvider.MapType.SATELLITE);
		setShowStations(true);

		flatPositionToPlatformMap = MinecraftClientData.getFlatPositionToSavedRails(MinecraftClientData.getDashboardInstance().platforms, transportMode);
		flatPositionToSidingMap = MinecraftClientData.getFlatPositionToSavedRails(MinecraftClientData.getDashboardInstance().sidings, transportMode);
	}

	@Override
	protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
		final DoubleDoubleImmutablePair mouseWorldPos = coordsToWorldPos((double) mouseX - getX(), mouseY - getY());
		final Matrix4f matrix4f = context.getMatrices().peek().getPositionMatrix();
		final VertexConsumer vertexConsumer = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers().getBuffer(RenderLayer.getGui());

		// Background
		context.fill(getX(), getY(), getX() + width, getY() + height, ARGB_BLACK);

		// World map
		if (mapTileProvider != null) {
			final double tileSize = scale * MapTileProvider.TILE_SIZE;
			final DoubleDoubleImmutablePair topLeftWorldCoords = coordsToWorldPos(0D, 0D);
			final double offsetX = (topLeftWorldCoords.leftDouble() - Math.floor(topLeftWorldCoords.leftDouble() / MapTileProvider.TILE_SIZE) * MapTileProvider.TILE_SIZE) * scale;
			final double offsetY = (topLeftWorldCoords.rightDouble() - Math.floor(topLeftWorldCoords.rightDouble() / MapTileProvider.TILE_SIZE) * MapTileProvider.TILE_SIZE) * scale;

			for (double x = 0; x < width + tileSize; x += tileSize) {
				for (double y = 0; y < height + tileSize; y += tileSize) {
					final DoubleDoubleImmutablePair worldCoords = coordsToWorldPos(x, y);
					final byte[] tile = mapTileProvider.getTile(BlockPos.ofFloored(worldCoords.leftDouble(), player.getY(), worldCoords.rightDouble()));

					if (tile != null) {
						int pixelOffsetX = 0;
						int pixelOffsetY = 0;

						for (int i = 0; i < tile.length; i += 5) {
							final Color color = new Color(
									tile[i + 1] & 0xFF,
									tile[i + 2] & 0xFF,
									tile[i + 3] & 0xFF,
									tile[i] & 0xFF
							);
							int count = (tile[i + 4] & 0xFF) + 1;

							while (count > 0) {
								final int length = Math.min(MapTileProvider.TILE_SIZE - pixelOffsetX, count);

								if (color.getAlpha() > 0) {
									final double x1 = getX() + x - offsetX + pixelOffsetX * scale;
									final double x2 = x1 + length * scale;
									final double y1 = getY() + y - offsetY + pixelOffsetY * scale;
									final double y2 = y1 + scale;
									draw(matrix4f, vertexConsumer, x1, y1, x2, y2, color.getRGB());
								}

								pixelOffsetX += length;
								count -= length;

								if (pixelOffsetX == MapTileProvider.TILE_SIZE) {
									pixelOffsetX = 0;
									pixelOffsetY++;
								}
							}
						}
					}
				}
			}
		}

		if (showStations) {
			flatPositionToPlatformMap.forEach((position, platforms) -> drawRectangleFromWorldCoords(matrix4f, vertexConsumer, position.getX(), position.getZ(), position.getX() + 1, position.getZ() + 1, ARGB_WHITE));
			for (final Station station : MinecraftClientData.getDashboardInstance().stations) {
				if (AreaBase.validCorners(station)) {
					drawRectangleFromWorldCoords(matrix4f, vertexConsumer, station.getMinX(), station.getMinZ(), station.getMaxX(), station.getMaxZ(), ARGB_BLACK_TRANSLUCENT + station.getColor());
				}
			}
			mouseOnSavedRail(mouseWorldPos, (savedRail, x1, z1, x2, z2) -> drawRectangleFromWorldCoords(matrix4f, vertexConsumer, x1, z1, x2, z2, ARGB_WHITE), true);
		} else {
			flatPositionToSidingMap.forEach((position, sidings) -> drawRectangleFromWorldCoords(matrix4f, vertexConsumer, position.getX(), position.getZ(), position.getX() + 1, position.getZ() + 1, ARGB_WHITE));
			for (final Depot depot : MinecraftClientData.getDashboardInstance().depots) {
				if (depot.isTransportMode(transportMode) && AreaBase.validCorners(depot)) {
					drawRectangleFromWorldCoords(matrix4f, vertexConsumer, depot.getMinX(), depot.getMinZ(), depot.getMaxX(), depot.getMaxZ(), ARGB_BLACK_TRANSLUCENT + depot.getColor());
				}
			}
			mouseOnSavedRail(mouseWorldPos, (savedRail, x1, z1, x2, z2) -> drawRectangleFromWorldCoords(matrix4f, vertexConsumer, x1, z1, x2, z2, ARGB_WHITE), false);
		}

		if (mapState == MapState.EDITING_AREA && drawArea1 != null && drawArea2 != null) {
			drawRectangleFromWorldCoords(matrix4f, vertexConsumer, drawArea1, drawArea2, ARGB_WHITE_TRANSLUCENT);
		}

		if (player != null) {
			drawPlayerSymbol(matrix4f, vertexConsumer, player);
		}

		final TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

		if (mapState == MapState.EDITING_AREA) {
			context.drawText(textRenderer, TranslationProvider.GUI_MTR_EDIT_AREA.getMutableText(), getX() + TEXT_PADDING, getY() + TEXT_PADDING, ARGB_WHITE, false);
		} else if (mapState == MapState.EDITING_ROUTE) {
			context.drawText(textRenderer, TranslationProvider.GUI_MTR_EDIT_ROUTE.getMutableText(), getX() + TEXT_PADDING, getY() + TEXT_PADDING, ARGB_WHITE, false);
		}

		if (scale >= 8) {
			if (showStations) {
				flatPositionToPlatformMap.forEach((position, platforms) -> drawSavedRail(context, position, platforms));
			} else {
				flatPositionToSidingMap.forEach((position, sidings) -> drawSavedRail(context, position, sidings));
			}
		}

		if (showStations) {
			drawAreas(context, MinecraftClientData.getDashboardInstance().stations);
		} else {
			drawAreas(context, MinecraftClientData.getDashboardInstance().depots);
		}

		drawMousePositionText(context, mouseWorldPos);
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
		if (mapState == MapState.EDITING_AREA) {
			drawArea2 = coordsToWorldPos((int) Math.round(mouseX - getX()), (int) Math.round(mouseY - getY()));
			if (drawArea1.leftInt() == drawArea2.leftInt()) {
				drawArea2 = new IntIntImmutablePair(drawArea2.leftInt() + 1, drawArea2.rightInt());
			}
			if (drawArea1.rightInt() == drawArea2.rightInt()) {
				drawArea2 = new IntIntImmutablePair(drawArea2.leftInt(), drawArea2.rightInt() + 1);
			}
			onDrawCorners.onDrawCorners(drawArea1, drawArea2);
		} else {
			centerX -= deltaX / scale;
			centerY -= deltaY / scale;
		}
		return true;
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		if (mapState == MapState.EDITING_AREA) {
			onDrawCornersMouseRelease.run();
		}
		return true;
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (isMouseOver(mouseX, mouseY)) {
			if (MinecraftClientData.hasPermission()) {
				if (mapState == MapState.EDITING_AREA) {
					drawArea1 = coordsToWorldPos((int) (mouseX - getX()), (int) (mouseY - getY()));
					drawArea2 = null;
				} else if (mapState == MapState.EDITING_ROUTE) {
					final DoubleDoubleImmutablePair mouseWorldPos = coordsToWorldPos(mouseX - getX(), mouseY - getY());
					mouseOnSavedRail(mouseWorldPos, (savedRail, x1, z1, x2, z2) -> onClickAddPlatformToRoute.accept(savedRail.getId()), true);
				} else {
					final DoubleDoubleImmutablePair mouseWorldPos = coordsToWorldPos(mouseX - getX(), mouseY - getY());
					mouseOnSavedRail(mouseWorldPos, (savedRail, x1, z1, x2, z2) -> onClickEditSavedRail.accept(savedRail), showStations);
				}
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
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
		return true;
	}

	@Override
	public boolean isMouseOver(double mouseX, double mouseY) {
		return mouseX >= getX() && mouseY >= getY() && mouseX < getX() + width && mouseY < getY() + height && !isRestrictedMouseArea.apply(mouseX, mouseY);
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

	private <T extends SavedRailBase<T, U>, U extends AreaBase<U, T>> void drawAreas(DrawContext context, ObjectArraySet<U> areas) {
		for (final U area : areas) {
			if (canDrawAreaText(area)) {
				final Position position = area.getCenter();
				final String additionalString;
				if (area instanceof Station) {
					additionalString = TranslationProvider.GUI_MTR_ZONE_NUMBER.getString(((Station) area).getZone1());
				} else {
					additionalString = null;
				}
				drawFromWorldCoords(position.getX(), position.getZ(), (x1, y1) -> {
					context.getMatrices().push();
					context.getMatrices().translate(getX() + x1.floatValue(), getY() + y1.floatValue(), 0);
//					IDrawing.drawStringWithFont(context, additionalString == null ? area.getName() : String.format("%s|(%s)", area.getName(), additionalString), 0, 0, DEFAULT_LIGHT);
					context.getMatrices().pop();
				});
			}
		}
	}

	private void drawPlayerSymbol(Matrix4f matrix4f, VertexConsumer vertexConsumer, ClientPlayerEntity player) {
		drawFromWorldCoords(player.getX(), player.getZ(), (x1, y1) -> {
			draw(matrix4f, vertexConsumer, getX() + x1 - 2, getY() + y1 - 3, getX() + x1 + 2, getY() + y1 + 3, ARGB_WHITE);
			draw(matrix4f, vertexConsumer, getX() + x1 - 3, getY() + y1 - 2, getX() + x1 + 3, getY() + y1 + 2, ARGB_WHITE);
			draw(matrix4f, vertexConsumer, getX() + x1 - 2, getY() + y1 - 2, getX() + x1 + 2, getY() + y1 + 2, ARGB_BLUE);
		});
	}

	private void drawMousePositionText(DrawContext context, DoubleDoubleImmutablePair mouseWorldPos) {
		final String mousePosText = String.format("(%.1f, %.1f)", mouseWorldPos.leftDouble(), mouseWorldPos.rightDouble());
		final TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
		context.drawText(textRenderer, mousePosText, getX() + width - TEXT_PADDING - textRenderer.getWidth(mousePosText), getY() + TEXT_PADDING, ARGB_WHITE, false);
	}

	private void draw(Matrix4f matrix4f, VertexConsumer vertexConsumer, double x1, double y1, double x2, double y2, int color) {
		if (Utilities.isIntersecting(x1, x2, getX(), getX() + width) && Utilities.isIntersecting(y1, y2, getY(), getY() + height)) {
			final float newX1 = (float) Math.max(x1, getX());
			final float newY1 = (float) Math.max(y1, getY());
			final float newX2 = (float) Math.min(x2, getX() + width);
			final float newY2 = (float) Math.min(y2, getY() + height);
			vertexConsumer.vertex(matrix4f, newX1, newY1, 0).color(color);
			vertexConsumer.vertex(matrix4f, newX1, newY2, 0).color(color);
			vertexConsumer.vertex(matrix4f, newX2, newY2, 0).color(color);
			vertexConsumer.vertex(matrix4f, newX2, newY1, 0).color(color);
		}
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

	public void startEditingArea(AreaBase<?, ?> editingArea) {
		mapState = MapState.EDITING_AREA;
		drawArea1 = new IntIntImmutablePair((int) editingArea.getMinX(), (int) editingArea.getMinY());
		drawArea2 = new IntIntImmutablePair((int) editingArea.getMaxX(), (int) editingArea.getMaxY());
	}

	public void startEditingRoute() {
		mapState = MapState.EDITING_ROUTE;
	}

	public void stopEditing() {
		mapState = MapState.DEFAULT;
	}

	public void setShowStations(boolean showStations) {
		this.showStations = showStations;
	}

	public void setMapOverlayMode(WorldMap.MapOverlayMode mapOverlayMode) {
		// TODO
	}

	public boolean isMapOverlayMode(WorldMap.MapOverlayMode mapOverlayMode) {
		// TODO
		return true;
	}

	private void mouseOnSavedRail(DoubleDoubleImmutablePair mouseWorldPos, MouseOnSavedRailCallback mouseOnSavedRailCallback, boolean isPlatform) {
		(isPlatform ? flatPositionToPlatformMap : flatPositionToSidingMap).forEach((position, savedRails) -> {
			final int savedRailCount = savedRails.size();
			for (int i = 0; i < savedRailCount; i++) {
				final long left = position.getX();
				final long right = position.getX() + 1;
				final long top = position.getZ();
				final long bottom = position.getZ() + 1;
				if (Utilities.isBetween(mouseWorldPos.leftDouble(), left, right) && Utilities.isBetween(mouseWorldPos.rightDouble(), top, bottom)) {
					mouseOnSavedRailCallback.mouseOnSavedRailCallback(savedRails.get(i), left, top, right, bottom);
				}
			}
		});
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

	private void drawFromWorldCoords(double worldX, double worldZ, BiConsumer<Double, Double> callback) {
		final double coordsX = (worldX - centerX) * scale + width / 2D;
		final double coordsY = (worldZ - centerY) * scale + height / 2D;
		if (Utilities.isBetween(coordsX, 0, width) && Utilities.isBetween(coordsY, 0, height)) {
			callback.accept(coordsX, coordsY);
		}
	}

	private void drawRectangleFromWorldCoords(Matrix4f matrix4f, VertexConsumer vertexConsumer, IntIntImmutablePair corner1, IntIntImmutablePair corner2, int color) {
		drawRectangleFromWorldCoords(matrix4f, vertexConsumer, corner1.leftInt(), corner1.rightInt(), corner2.leftInt(), corner2.rightInt(), color);
	}

	private void drawRectangleFromWorldCoords(Matrix4f matrix4f, VertexConsumer vertexConsumer, long posX1, long posZ1, long posX2, long posZ2, int color) {
		draw(
				matrix4f, vertexConsumer,
				getX() + (Math.min(posX1, posX2) - centerX) * scale + width / 2D,
				getY() + (Math.min(posZ1, posZ2) - centerY) * scale + height / 2D,
				getX() + (Math.max(posX1, posX2) + 1 - centerX) * scale + width / 2D,
				getY() + (Math.max(posZ1, posZ2) + 1 - centerY) * scale + height / 2D,
				color
		);
	}

	private <T extends SavedRailBase<T, U>, U extends AreaBase<U, T>> boolean canDrawAreaText(U areaBase) {
		return areaBase.getCenter() != null && scale >= 80F / Math.max(areaBase.getMaxX() - areaBase.getMinX(), areaBase.getMaxZ() - areaBase.getMinZ());
	}

	private <T extends SavedRailBase<T, U>, U extends AreaBase<U, T>> void drawSavedRail(DrawContext context, Position position, ObjectArrayList<T> sortedSavedRails) {
		final int savedRailCount = sortedSavedRails.size();
		for (int i = 0; i < savedRailCount; i++) {
			final int index = i;
			drawFromWorldCoords(position.getX() + 0.5, position.getZ() + (i + 0.5) / savedRailCount, (x1, y1) -> context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, sortedSavedRails.get(index).getName(), getX() + x1.intValue(), getY() + y1.intValue() - TEXT_HEIGHT / 2, ARGB_WHITE));
		}
	}

	@FunctionalInterface
	public interface OnDrawCorners {
		void onDrawCorners(IntIntImmutablePair corner1, IntIntImmutablePair corner2);
	}

	@FunctionalInterface
	private interface MouseOnSavedRailCallback {
		void mouseOnSavedRailCallback(SavedRailBase<?, ?> savedRail, long x1, long z1, long x2, long z2);
	}

	private enum MapState {DEFAULT, EDITING_AREA, EDITING_ROUTE}
}
