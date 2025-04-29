package org.mtr.mod.screen;

import org.mtr.core.data.Position;
import org.mtr.core.data.*;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.it.unimi.dsi.fastutil.doubles.DoubleDoubleImmutablePair;
import org.mtr.libraries.it.unimi.dsi.fastutil.ints.IntIntImmutablePair;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectAVLTreeMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArraySet;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.ClickableWidgetExtension;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.GuiDrawing;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.client.MinecraftClientData;
import org.mtr.mod.data.IGui;
import org.mtr.mod.generated.lang.TranslationProvider;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;


public class WidgetMap extends ClickableWidgetExtension implements IGui {

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
	private final ClientWorld world;
	private final ClientPlayerEntity player;
	private final WorldMap worldMap;
	private final Object2ObjectAVLTreeMap<Position, ObjectArrayList<Platform>> flatPositionToPlatformMap;
	private final Object2ObjectAVLTreeMap<Position, ObjectArrayList<Siding>> flatPositionToSidingMap;

	private static final int ARGB_BLUE = 0xFF4285F4;
	private static final int SCALE_UPPER_LIMIT = 64;
	private static final double SCALE_LOWER_LIMIT = 1 / 128D;

	public WidgetMap(TransportMode transportMode, OnDrawCorners onDrawCorners, Runnable onDrawCornersMouseRelease, Consumer<Long> onClickAddPlatformToRoute, Consumer<SavedRailBase<?, ?>> onClickEditSavedRail, BiFunction<Double, Double, Boolean> isRestrictedMouseArea) {
		super(0, 0, 0, 0);
		this.transportMode = transportMode;
		this.onDrawCorners = onDrawCorners;
		this.onDrawCornersMouseRelease = onDrawCornersMouseRelease;
		this.onClickAddPlatformToRoute = onClickAddPlatformToRoute;
		this.onClickEditSavedRail = onClickEditSavedRail;
		this.isRestrictedMouseArea = isRestrictedMouseArea;

		final MinecraftClient minecraftClient = MinecraftClient.getInstance();
		world = minecraftClient.getWorldMapped();
		player = minecraftClient.getPlayerMapped();
		if (player == null) {
			centerX = 0;
			centerY = 0;
		} else {
			centerX = player.getX();
			centerY = player.getZ();
		}

		scale = 1;
		worldMap = new WorldMap();
		setShowStations(true);

		flatPositionToPlatformMap = MinecraftClientData.getFlatPositionToSavedRails(MinecraftClientData.getDashboardInstance().platforms, transportMode);
		flatPositionToSidingMap = MinecraftClientData.getFlatPositionToSavedRails(MinecraftClientData.getDashboardInstance().sidings, transportMode);
	}

	@Override
	public void render(GraphicsHolder graphicsHolder, int mouseX, int mouseY, float delta) {
		final GuiDrawing guiDrawing = new GuiDrawing(graphicsHolder);
		final DoubleDoubleImmutablePair mouseWorldPos = coordsToWorldPos((double) mouseX - getX2(), mouseY - getY2());
		final IntIntImmutablePair topLeft = coordsToWorldPos(0, 0);
		final IntIntImmutablePair bottomRight = coordsToWorldPos(width, height);

		// Background
		guiDrawing.beginDrawingRectangle();
		guiDrawing.drawRectangle(getX2(), getY2(), getX2() + width, getY2() + height, ARGB_BLACK);
		guiDrawing.finishDrawingRectangle();

		// World map
		if (world != null) {
			worldMap.tick(World.cast(world), player, delta);
			worldMap.forEachTile(mapImage -> {
				final int posX = mapImage.chunkX * WorldMap.CHUNK_SIZE;
				final int posZ = mapImage.chunkZ * WorldMap.CHUNK_SIZE;
				if (posX + WorldMap.CHUNK_SIZE < topLeft.leftInt() || posZ + WorldMap.CHUNK_SIZE < (topLeft).rightInt() || posX > bottomRight.leftInt() || posZ > bottomRight.rightInt()) {
					return;
				}
				drawTextureFromWorldCoords(guiDrawing, mapImage.textureId, posX, posZ, posX + WorldMap.CHUNK_SIZE, posZ + WorldMap.CHUNK_SIZE);
			});
		}

		// Continue drawing our overlay
		guiDrawing.beginDrawingRectangle();

		if (showStations) {
			flatPositionToPlatformMap.forEach((position, platforms) -> drawRectangleFromWorldCoords(guiDrawing, position.getX(), position.getZ(), position.getX() + 1, position.getZ() + 1, ARGB_WHITE));
			for (final Station station : MinecraftClientData.getDashboardInstance().stations) {
				if (AreaBase.validCorners(station)) {
					drawRectangleFromWorldCoords(guiDrawing, station.getMinX(), station.getMinZ(), station.getMaxX(), station.getMaxZ(), ARGB_BLACK_TRANSLUCENT + station.getColor());
				}
			}
			mouseOnSavedRail(mouseWorldPos, (savedRail, x1, z1, x2, z2) -> drawRectangleFromWorldCoords(guiDrawing, x1, z1, x2, z2, ARGB_WHITE), true);
		} else {
			flatPositionToSidingMap.forEach((position, sidings) -> drawRectangleFromWorldCoords(guiDrawing, position.getX(), position.getZ(), position.getX() + 1, position.getZ() + 1, ARGB_WHITE));
			for (final Depot depot : MinecraftClientData.getDashboardInstance().depots) {
				if (depot.isTransportMode(transportMode) && AreaBase.validCorners(depot)) {
					drawRectangleFromWorldCoords(guiDrawing, depot.getMinX(), depot.getMinZ(), depot.getMaxX(), depot.getMaxZ(), ARGB_BLACK_TRANSLUCENT + depot.getColor());
				}
			}
			mouseOnSavedRail(mouseWorldPos, (savedRail, x1, z1, x2, z2) -> drawRectangleFromWorldCoords(guiDrawing, x1, z1, x2, z2, ARGB_WHITE), false);
		}

		if (mapState == MapState.EDITING_AREA && drawArea1 != null && drawArea2 != null) {
			drawRectangleFromWorldCoords(guiDrawing, drawArea1, drawArea2, ARGB_WHITE_TRANSLUCENT);
		}

		if (player != null) {
			drawPlayerSymbol(guiDrawing, player);
		}

		guiDrawing.finishDrawingRectangle();

		if (mapState == MapState.EDITING_AREA) {
			graphicsHolder.drawText(TranslationProvider.GUI_MTR_EDIT_AREA.getMutableText(), getX2() + TEXT_PADDING, getY2() + TEXT_PADDING, ARGB_WHITE, false, GraphicsHolder.getDefaultLight());
		} else if (mapState == MapState.EDITING_ROUTE) {
			graphicsHolder.drawText(TranslationProvider.GUI_MTR_EDIT_ROUTE.getMutableText(), getX2() + TEXT_PADDING, getY2() + TEXT_PADDING, ARGB_WHITE, false, GraphicsHolder.getDefaultLight());
		}

		if (scale >= 8) {
			if (showStations) {
				flatPositionToPlatformMap.forEach((position, platforms) -> drawSavedRail(graphicsHolder, position, platforms));
			} else {
				flatPositionToSidingMap.forEach((position, sidings) -> drawSavedRail(graphicsHolder, position, sidings));
			}
		}

		if (showStations) {
			drawAreas(graphicsHolder, MinecraftClientData.getDashboardInstance().stations);
		} else {
			drawAreas(graphicsHolder, MinecraftClientData.getDashboardInstance().depots);
		}

		drawMousePositionText(graphicsHolder, mouseWorldPos);
	}

	@Override
	public boolean mouseDragged2(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
		if (mapState == MapState.EDITING_AREA) {
			drawArea2 = coordsToWorldPos((int) Math.round(mouseX - getX2()), (int) Math.round(mouseY - getY2()));
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
	public boolean mouseReleased2(double mouseX, double mouseY, int button) {
		if (mapState == MapState.EDITING_AREA) {
			onDrawCornersMouseRelease.run();
		}
		return true;
	}

	@Override
	public boolean mouseClicked2(double mouseX, double mouseY, int button) {
		if (isMouseOver2(mouseX, mouseY)) {
			if (MinecraftClientData.hasPermission()) {
				if (mapState == MapState.EDITING_AREA) {
					drawArea1 = coordsToWorldPos((int) (mouseX - getX2()), (int) (mouseY - getY2()));
					drawArea2 = null;
				} else if (mapState == MapState.EDITING_ROUTE) {
					final DoubleDoubleImmutablePair mouseWorldPos = coordsToWorldPos(mouseX - getX2(), mouseY - getY2());
					mouseOnSavedRail(mouseWorldPos, (savedRail, x1, z1, x2, z2) -> onClickAddPlatformToRoute.accept(savedRail.getId()), true);
				} else {
					final DoubleDoubleImmutablePair mouseWorldPos = coordsToWorldPos(mouseX - getX2(), mouseY - getY2());
					mouseOnSavedRail(mouseWorldPos, (savedRail, x1, z1, x2, z2) -> onClickEditSavedRail.accept(savedRail), showStations);
				}
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean mouseScrolled2(double mouseX, double mouseY, double amount) {
		final double oldScale = scale;
		if (oldScale > SCALE_LOWER_LIMIT && amount < 0) {
			centerX -= (mouseX - getX2() - width / 2D) / scale;
			centerY -= (mouseY - getY2() - height / 2D) / scale;
		}
		scale(amount);
		if (oldScale < SCALE_UPPER_LIMIT && amount > 0) {
			centerX += (mouseX - getX2() - width / 2D) / scale;
			centerY += (mouseY - getY2() - height / 2D) / scale;
		}
		return true;
	}

	@Override
	public boolean isMouseOver2(double mouseX, double mouseY) {
		return mouseX >= getX2() && mouseY >= getY2() && mouseX < getX2() + width && mouseY < getY2() + height && !isRestrictedMouseArea.apply(mouseX, mouseY);
	}

	private <T extends SavedRailBase<T, U>, U extends AreaBase<U, T>> void drawAreas(GraphicsHolder graphicsHolder, ObjectArraySet<U> areas) {
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
					graphicsHolder.push();
					graphicsHolder.translate(getX2() + x1.floatValue(), getY2() + y1.floatValue(), 0);
					IDrawing.drawStringWithFont(graphicsHolder, additionalString == null ? area.getName() : String.format("%s|(%s)", area.getName(), additionalString), 0, 0, GraphicsHolder.getDefaultLight());
					graphicsHolder.pop();
				});
			}
		}
	}

	private void drawPlayerSymbol(GuiDrawing guiDrawing, ClientPlayerEntity player) {
		drawFromWorldCoords(player.getX(), player.getZ(), (x1, y1) -> {
			guiDrawing.drawRectangle(getX2() + Math.max(0, x1 - 2), getY2() + y1 - 3, getX2() + x1 + 2, getY2() + y1 + 3, ARGB_WHITE);
			guiDrawing.drawRectangle(getX2() + Math.max(0, x1 - 3), getY2() + y1 - 2, getX2() + x1 + 3, getY2() + y1 + 2, ARGB_WHITE);
			guiDrawing.drawRectangle(getX2() + Math.max(0, x1 - 2), getY2() + y1 - 2, getX2() + x1 + 2, getY2() + y1 + 2, ARGB_BLUE);
		});
	}

	private void drawMousePositionText(GraphicsHolder graphicsHolder, DoubleDoubleImmutablePair mouseWorldPos) {
		final String mousePosText = String.format("(%.1f, %.1f)", mouseWorldPos.leftDouble(), mouseWorldPos.rightDouble());
		graphicsHolder.drawText(mousePosText, getX2() + width - TEXT_PADDING - GraphicsHolder.getTextWidth(mousePosText), getY2() + TEXT_PADDING, ARGB_WHITE, false, GraphicsHolder.getDefaultLight());
	}

	// Implicit overrides, don't add @Override
	public void setFocused2(boolean focused) {
	}

	// Implicit overrides, don't add @Override
	public boolean isFocused2() {
		return false;
	}

	public void setPositionAndSize(int x, int y, int width, int height) {
		setX2(x);
		setY2(y);
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
		worldMap.setMapOverlayMode(mapOverlayMode);
		if (world != null) {
			worldMap.updateMap(World.cast(world), player);
		}
	}

	public boolean isMapOverlayMode(WorldMap.MapOverlayMode mapOverlayMode) {
		return worldMap.isMapOverlayMode(mapOverlayMode);
	}

	public void onClose() {
		worldMap.disposeImages();
	}

	private void mouseOnSavedRail(DoubleDoubleImmutablePair mouseWorldPos, MouseOnSavedRailCallback mouseOnSavedRailCallback, boolean isPlatform) {
		(isPlatform ? flatPositionToPlatformMap : flatPositionToSidingMap).forEach((position, savedRails) -> {
			final int savedRailCount = savedRails.size();
			for (int i = 0; i < savedRailCount; i++) {
				final double left = position.getX();
				final double right = position.getX() + 1;
				final double top = position.getZ() + (double) i / savedRailCount;
				final double bottom = position.getZ() + (i + 1D) / savedRailCount;
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

	private void drawRectangleFromWorldCoords(GuiDrawing guiDrawing, IntIntImmutablePair corner1, IntIntImmutablePair corner2, int color) {
		drawRectangleFromWorldCoords(guiDrawing, corner1.leftInt(), corner1.rightInt(), corner2.leftInt(), corner2.rightInt(), color);
	}

	private void drawRectangleFromWorldCoords(GuiDrawing guiDrawing, double posX1, double posZ1, double posX2, double posZ2, int color) {
		final double x1 = (posX1 - centerX) * scale + width / 2D;
		final double z1 = (posZ1 - centerY) * scale + height / 2D;
		final double x2 = (posX2 - centerX) * scale + width / 2D;
		final double z2 = (posZ2 - centerY) * scale + height / 2D;

		final double minX = Math.min(x1, x2);
		final double maxX = Math.max(x1, x2);
		final double minZ = Math.min(z1, z2);
		final double maxZ = Math.max(z1, z2);

		guiDrawing.drawRectangle(getX2() + Math.max(0, minX), getY2() + minZ, getX2() + maxX, getY2() + maxZ, color);
	}

	private void drawTextureFromWorldCoords(GuiDrawing guiDrawing, Identifier texture, double posX1, double posZ1, double posX2, double posZ2) {
		guiDrawing.beginDrawingTexture(texture);
		final double x1 = (posX1 - centerX) * scale + width / 2D;
		final double z1 = (posZ1 - centerY) * scale + height / 2D;
		final double x2 = (posX2 - centerX) * scale + width / 2D;
		final double z2 = (posZ2 - centerY) * scale + height / 2D;
		final float uScale = x1 >= 0 ? 0 : 1F - (float) ((x2 - 0) / (x2 - x1));
		guiDrawing.drawTexture(getX2() + Math.max(0, x1), getY2() + z1, getX2() + x2, getY2() + z2, uScale, 0, 1, 1);
		guiDrawing.finishDrawingTexture();
	}

	private <T extends SavedRailBase<T, U>, U extends AreaBase<U, T>> boolean canDrawAreaText(U areaBase) {
		return areaBase.getCenter() != null && scale >= 80F / Math.max(areaBase.getMaxX() - areaBase.getMinX(), areaBase.getMaxZ() - areaBase.getMinZ());
	}

	private <T extends SavedRailBase<T, U>, U extends AreaBase<U, T>> void drawSavedRail(GraphicsHolder graphicsHolder, Position position, ObjectArrayList<T> sortedSavedRails) {
		final int savedRailCount = sortedSavedRails.size();
		for (int i = 0; i < savedRailCount; i++) {
			final int index = i;
			drawFromWorldCoords(position.getX() + 0.5, position.getZ() + (i + 0.5) / savedRailCount, (x1, y1) -> graphicsHolder.drawCenteredText(sortedSavedRails.get(index).getName(), getX2() + x1.intValue(), getY2() + y1.intValue() - TEXT_HEIGHT / 2, ARGB_WHITE));
		}
	}

	@FunctionalInterface
	public interface OnDrawCorners {
		void onDrawCorners(IntIntImmutablePair corner1, IntIntImmutablePair corner2);
	}

	@FunctionalInterface
	private interface MouseOnSavedRailCallback {
		void mouseOnSavedRailCallback(SavedRailBase<?, ?> savedRail, double x1, double z1, double x2, double z2);
	}

	private enum MapState {DEFAULT, EDITING_AREA, EDITING_ROUTE}
}
