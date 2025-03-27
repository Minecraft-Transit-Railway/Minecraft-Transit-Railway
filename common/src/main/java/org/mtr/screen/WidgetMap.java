package org.mtr.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import it.unimi.dsi.fastutil.doubles.DoubleDoubleImmutablePair;
import it.unimi.dsi.fastutil.ints.IntIntImmutablePair;
import it.unimi.dsi.fastutil.longs.Long2FloatAVLTreeMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectAVLTreeMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.Window;
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
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.map.MapTileProvider;

import java.util.function.BiConsumer;
import java.util.function.Consumer;


public final class WidgetMap extends ClickableWidget implements IGui {

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
	private final ClientPlayerEntity player;
	private final Object2ObjectAVLTreeMap<Position, ObjectArrayList<Platform>> flatPositionToPlatformMap;
	private final Object2ObjectAVLTreeMap<Position, ObjectArrayList<Siding>> flatPositionToSidingMap;

	private final Long2FloatAVLTreeMap tileOpacityValues = new Long2FloatAVLTreeMap();

	private static final int PLAYER_ARROW_SIZE = 6;
	private static final int SCALE_UPPER_LIMIT = 64;
	private static final double SCALE_LOWER_LIMIT = 1 / 128D;
	private static final float DARKEN_MAP = 0.8F;

	public WidgetMap(TransportMode transportMode, OnDrawCorners onDrawCorners, Runnable onDrawCornersMouseRelease, Consumer<Long> onClickAddPlatformToRoute, Consumer<SavedRailBase<?, ?>> onClickEditSavedRail) {
		super(0, 0, 0, 0, Text.empty());
		this.transportMode = transportMode;
		this.onDrawCorners = onDrawCorners;
		this.onDrawCornersMouseRelease = onDrawCornersMouseRelease;
		this.onClickAddPlatformToRoute = onClickAddPlatformToRoute;
		this.onClickEditSavedRail = onClickEditSavedRail;

		final MinecraftClient minecraftClient = MinecraftClient.getInstance();
		player = minecraftClient.player;
		if (player == null) {
			centerX = 0;
			centerY = 0;
		} else {
			centerX = player.getX();
			centerY = player.getZ();
		}

		scale = 1;
		setShowStations(true);

		flatPositionToPlatformMap = MinecraftClientData.getFlatPositionToSavedRails(MinecraftClientData.getDashboardInstance().platforms, transportMode);
		flatPositionToSidingMap = MinecraftClientData.getFlatPositionToSavedRails(MinecraftClientData.getDashboardInstance().sidings, transportMode);
	}

	@Override
	protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
		final Window window = MinecraftClient.getInstance().getWindow();
		final int windowScale = (int) Math.round(window.getScaleFactor());
		RenderSystem.enableScissor(getX() * windowScale, window.getHeight() - (getY() + height) * windowScale, width * windowScale, height * windowScale);
		final DoubleDoubleImmutablePair mouseWorldPos = coordsToWorldPos((double) mouseX - getX(), mouseY - getY());

		// Background
		context.fill(getX(), getY(), getX() + width, getY() + height, ARGB_BLACK);

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
						final float[] shaderColor = RenderSystem.getShaderColor();
						final float oldShaderR = shaderColor[0];
						final float oldShaderG = shaderColor[1];
						final float oldShaderB = shaderColor[2];
						final float newOpacity = Math.min(1, opacity + delta / 10);

						if (opacity < 1) {
							tileOpacityValues.put(key, newOpacity);
						}

						RenderSystem.setShaderColor(newOpacity * DARKEN_MAP, newOpacity * DARKEN_MAP, newOpacity * DARKEN_MAP, shaderColor[3]);
						vertexBuffer.bind();
						vertexBuffer.draw(
								new Matrix4f(RenderSystem.getModelViewMatrix()).translate((float) x + getX(), (float) y + getY(), 0).scale((float) scale, (float) scale, 1).translate(offsetX, offsetY, 1),
								RenderSystem.getProjectionMatrix(),
								RenderSystem.getShader()
						);
						RenderSystem.setShaderColor(oldShaderR, oldShaderG, oldShaderB, shaderColor[3]);
					}
				}
			}

			VertexBuffer.unbind();
			RenderLayer.getGui().endDrawing();
		}

		// Player position indicator
		if (player != null) {
			drawFromWorldCoords(player.getX(), player.getZ(), PLAYER_ARROW_SIZE / 2, (x, y) -> {
				context.getMatrices().push();
				context.getMatrices().translate(getX() + x, getY() + y, 3);
				IDrawing.rotateZDegrees(context.getMatrices(), player.getYaw() + 180);
				final Matrix4f matrix4f = context.getMatrices().peek().getPositionMatrix();
				final VertexConsumer vertexConsumer2 = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers().getBuffer(RenderLayer.getGuiTextured(Identifier.of("textures/gui/sprites/mtr/dashboard_player_arrow.png")));
				final float x1 = -PLAYER_ARROW_SIZE / 2F;
				final float y1 = -PLAYER_ARROW_SIZE / 2F;
				final float x2 = x1 + PLAYER_ARROW_SIZE;
				final float y2 = y1 + PLAYER_ARROW_SIZE;
				vertexConsumer2.vertex(matrix4f, x1, y1, 0).texture(0, 0).color(ARGB_WHITE);
				vertexConsumer2.vertex(matrix4f, x1, y2, 0).texture(0, 1).color(ARGB_WHITE);
				vertexConsumer2.vertex(matrix4f, x2, y2, 0).texture(1, 1).color(ARGB_WHITE);
				vertexConsumer2.vertex(matrix4f, x2, y1, 0).texture(1, 0).color(ARGB_WHITE);
				context.getMatrices().pop();
			});
		}

		final Matrix4f matrix4f = context.getMatrices().peek().getPositionMatrix();
		final VertexConsumer vertexConsumer = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers().getBuffer(RenderLayer.getGui());

		// Platforms and sidings
		if (showStations) {
			flatPositionToPlatformMap.forEach((position, platforms) -> drawRectangleFromWorldCoords(matrix4f, vertexConsumer, position.getX(), position.getZ(), position.getX() + 1, position.getZ() + 1, 2, ARGB_WHITE));
			for (final Station station : MinecraftClientData.getDashboardInstance().stations) {
				if (AreaBase.validCorners(station)) {
					drawRectangleFromWorldCoords(matrix4f, vertexConsumer, station.getMinX(), station.getMinZ(), station.getMaxX(), station.getMaxZ(), 2, ARGB_BLACK_TRANSLUCENT + station.getColor());
				}
			}
			mouseOnSavedRail(mouseWorldPos, (savedRail, x1, z1, x2, z2) -> drawRectangleFromWorldCoords(matrix4f, vertexConsumer, x1, z1, x2, z2, 2, ARGB_WHITE), true);
		} else {
			flatPositionToSidingMap.forEach((position, sidings) -> drawRectangleFromWorldCoords(matrix4f, vertexConsumer, position.getX(), position.getZ(), position.getX() + 1, position.getZ() + 1, 2, ARGB_WHITE));
			for (final Depot depot : MinecraftClientData.getDashboardInstance().depots) {
				if (depot.isTransportMode(transportMode) && AreaBase.validCorners(depot)) {
					drawRectangleFromWorldCoords(matrix4f, vertexConsumer, depot.getMinX(), depot.getMinZ(), depot.getMaxX(), depot.getMaxZ(), 2, ARGB_BLACK_TRANSLUCENT + depot.getColor());
				}
			}
			mouseOnSavedRail(mouseWorldPos, (savedRail, x1, z1, x2, z2) -> drawRectangleFromWorldCoords(matrix4f, vertexConsumer, x1, z1, x2, z2, 2, ARGB_WHITE), false);
		}

		// Editing rectangle
		if (mapState == MapState.EDITING_AREA && drawArea1 != null && drawArea2 != null) {
			drawRectangleFromWorldCoords(matrix4f, vertexConsumer, drawArea1, drawArea2, 2, ARGB_WHITE_TRANSLUCENT);
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
		RenderSystem.disableScissor();
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
		return mouseX >= getX() && mouseY >= getY() && mouseX < getX() + width && mouseY < getY() + height;
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
				drawFromWorldCoords(position.getX(), position.getZ(), 0, (x1, y1) -> {
					context.getMatrices().push();
					context.getMatrices().translate(getX() + x1.floatValue(), getY() + y1.floatValue(), 0);
//					IDrawing.drawStringWithFont(context, additionalString == null ? area.getName() : String.format("%s|(%s)", area.getName(), additionalString), 0, 0, DEFAULT_LIGHT);
					context.getMatrices().pop();
				});
			}
		}
	}

	private void drawMousePositionText(DrawContext context, DoubleDoubleImmutablePair mouseWorldPos) {
		final String mousePosText = String.format("(%.1f, %.1f)", mouseWorldPos.leftDouble(), mouseWorldPos.rightDouble());
		final TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
		context.drawText(textRenderer, mousePosText, getX() + width - TEXT_PADDING - textRenderer.getWidth(mousePosText), getY() + TEXT_PADDING, ARGB_WHITE, false);
	}

	private void draw(Matrix4f matrix4f, VertexConsumer vertexConsumer, double x1, double y1, double x2, double y2, int z, int color) {
		if (Utilities.isIntersecting(x1, x2, getX(), getX() + width) && Utilities.isIntersecting(y1, y2, getY(), getY() + height)) {
			final float newX1 = (float) Math.max(x1, getX());
			final float newY1 = (float) Math.max(y1, getY());
			final float newX2 = (float) Math.min(x2, getX() + width);
			final float newY2 = (float) Math.min(y2, getY() + height);
			vertexConsumer.vertex(matrix4f, newX1, newY1, z).color(color);
			vertexConsumer.vertex(matrix4f, newX1, newY2, z).color(color);
			vertexConsumer.vertex(matrix4f, newX2, newY2, z).color(color);
			vertexConsumer.vertex(matrix4f, newX2, newY1, z).color(color);
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

	private void drawFromWorldCoords(double worldX, double worldZ, int padding, BiConsumer<Double, Double> callback) {
		final double coordsX = (worldX - centerX) * scale + width / 2D;
		final double coordsY = (worldZ - centerY) * scale + height / 2D;
		if (Utilities.isBetween(coordsX, -padding, width + padding) && Utilities.isBetween(coordsY, -padding, height + padding)) {
			callback.accept(coordsX, coordsY);
		}
	}

	private void drawRectangleFromWorldCoords(Matrix4f matrix4f, VertexConsumer vertexConsumer, IntIntImmutablePair corner1, IntIntImmutablePair corner2, int z, int color) {
		drawRectangleFromWorldCoords(matrix4f, vertexConsumer, corner1.leftInt(), corner1.rightInt(), corner2.leftInt(), corner2.rightInt(), z, color);
	}

	private void drawRectangleFromWorldCoords(Matrix4f matrix4f, VertexConsumer vertexConsumer, long posX1, long posZ1, long posX2, long posZ2, int z, int color) {
		draw(
				matrix4f, vertexConsumer,
				getX() + (Math.min(posX1, posX2) - centerX) * scale + width / 2D,
				getY() + (Math.min(posZ1, posZ2) - centerY) * scale + height / 2D,
				getX() + (Math.max(posX1, posX2) + 1 - centerX) * scale + width / 2D,
				getY() + (Math.max(posZ1, posZ2) + 1 - centerY) * scale + height / 2D,
				z, color
		);
	}

	private <T extends SavedRailBase<T, U>, U extends AreaBase<U, T>> boolean canDrawAreaText(U areaBase) {
		return areaBase.getCenter() != null && scale >= 80F / Math.max(areaBase.getMaxX() - areaBase.getMinX(), areaBase.getMaxZ() - areaBase.getMinZ());
	}

	private <T extends SavedRailBase<T, U>, U extends AreaBase<U, T>> void drawSavedRail(DrawContext context, Position position, ObjectArrayList<T> sortedSavedRails) {
		final int savedRailCount = sortedSavedRails.size();
		for (int i = 0; i < savedRailCount; i++) {
			final int index = i;
			drawFromWorldCoords(position.getX() + 0.5, position.getZ() + (i + 0.5) / savedRailCount, 0, (x1, y1) -> context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, sortedSavedRails.get(index).getName(), getX() + x1.intValue(), getY() + y1.intValue() - TEXT_HEIGHT / 2, ARGB_WHITE));
		}
	}

	private static int clampTileSize(double value) {
		return (int) Math.floor(value / MapTileProvider.TILE_SIZE) * MapTileProvider.TILE_SIZE;
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
