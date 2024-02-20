package org.mtr.mod.screen;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.mtr.core.data.Position;
import org.mtr.core.data.*;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.it.unimi.dsi.fastutil.doubles.DoubleDoubleImmutablePair;
import org.mtr.libraries.it.unimi.dsi.fastutil.ints.IntIntImmutablePair;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectAVLTreeMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.*;
import org.mtr.mod.Init;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.client.MinecraftClientData;
import org.mtr.mod.data.IGui;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;


public class WidgetMap extends ClickableWidgetExtension implements IGui {
	private double scale;
	private double centerX;
	private double centerY;
	private IntIntImmutablePair drawArea1, drawArea2;
	private MapState mapState;
	private MapOverlayMode mapOverlayMode;
	private boolean showStations;
	private double updateMapTimer;

	private final TransportMode transportMode;
	private final OnDrawCorners onDrawCorners;
	private final Runnable onDrawCornersMouseRelease;
	private final Consumer<Long> onClickAddPlatformToRoute;
	private final Consumer<SavedRailBase<?, ?>> onClickEditSavedRail;
	private final BiFunction<Double, Double, Boolean> isRestrictedMouseArea;
	private final ClientWorld world;
	private final ClientPlayerEntity player;
	private final Object2ObjectAVLTreeMap<Position, ObjectArrayList<Platform>> flatPositionToPlatformMap;
	private final Object2ObjectAVLTreeMap<Position, ObjectArrayList<Siding>> flatPositionToSidingMap;
	private final Object2ObjectOpenHashMap<IntIntImmutablePair, MapImage> mapTextures = new Object2ObjectOpenHashMap<>();
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
		updateMapTimer = -1;
		scale = 1;
		mapOverlayMode = MapOverlayMode.TOP_VIEW;
		setShowStations(true);

		flatPositionToPlatformMap = MinecraftClientData.getFlatPositionToSavedRails(MinecraftClientData.getDashboardInstance().platforms, transportMode);
		flatPositionToSidingMap = MinecraftClientData.getFlatPositionToSavedRails(MinecraftClientData.getDashboardInstance().sidings, transportMode);
	}

	@Override
	public void render(GraphicsHolder graphicsHolder, int mouseX, int mouseY, float delta) {
		if(updateMapTimer == -1 || updateMapTimer >= 40) {
			updateMapTimer = 0;
			if(world != null) updateMap(World.cast(world), mapOverlayMode);
		}
		updateMapTimer += delta;

		final GuiDrawing guiDrawing = new GuiDrawing(graphicsHolder);
		// Background
		guiDrawing.beginDrawingRectangle();
		guiDrawing.drawRectangle(getX2(), getY2(), getX2() + width, getY2() + height, ARGB_BLACK);
		guiDrawing.finishDrawingRectangle();

		for(MapImage mapImage : mapTextures.values()) {
			if(!world.getChunkManager().isChunkLoaded(mapImage.chunkX, mapImage.chunkZ)) continue;

			int xTL = mapImage.chunkX * 16;
			int zTL = mapImage.chunkZ * 16;
			if(!mapImage.disposed) {
				drawRectangleFromWorldCoords(guiDrawing, xTL, zTL, xTL + 16, zTL + 16, mapImage.textureId);
			}
		}


		// Continue drawing our overlay
		guiDrawing.beginDrawingRectangle();
		final DoubleDoubleImmutablePair mouseWorldPos = coordsToWorldPos((double) mouseX - getX2(), mouseY - getY2());

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
			drawFromWorldCoords(player.getX(), player.getZ(), (x1, y1) -> {
				guiDrawing.drawRectangle(getX2() + Math.max(0, x1 - 2), getY2() + y1 - 3, getX2() + x1 + 2, getY2() + y1 + 3, ARGB_WHITE);
				guiDrawing.drawRectangle(getX2() + Math.max(0, x1 - 3), getY2() + y1 - 2, getX2() + x1 + 3, getY2() + y1 + 2, ARGB_WHITE);
				guiDrawing.drawRectangle(getX2() + Math.max(0, x1 - 2), getY2() + y1 - 2, getX2() + x1 + 2, getY2() + y1 + 2, ARGB_BLUE);
			});
		}

		guiDrawing.finishDrawingRectangle();

		if (mapState == MapState.EDITING_AREA) {
			graphicsHolder.drawText(TextHelper.translatable("gui.mtr.edit_area").getString(), getX2() + TEXT_PADDING, getY2() + TEXT_PADDING, ARGB_WHITE, false, MAX_LIGHT_GLOWING);
		} else if (mapState == MapState.EDITING_ROUTE) {
			graphicsHolder.drawText(TextHelper.translatable("gui.mtr.edit_route").getString(), getX2() + TEXT_PADDING, getY2() + TEXT_PADDING, ARGB_WHITE, false, MAX_LIGHT_GLOWING);
		}

		if (scale >= 8) {
			if (showStations) {
				flatPositionToPlatformMap.forEach((position, platforms) -> drawSavedRail(graphicsHolder, position, platforms));
			} else {
				flatPositionToSidingMap.forEach((position, sidings) -> drawSavedRail(graphicsHolder, position, sidings));
			}
		}

		if (showStations) {
			for (final Station station : MinecraftClientData.getDashboardInstance().stations) {
				if (canDrawAreaText(station)) {
					final Position position = station.getCenter();
					final String stationString = String.format("%s|(%s)", station.getName(), TextHelper.translatable("gui.mtr.zone_number", station.getZone1()).getString());
					drawFromWorldCoords(position.getX(), position.getZ(), (x1, y1) -> IDrawing.drawStringWithFont(graphicsHolder, stationString, getX2() + x1.floatValue(), getY2() + y1.floatValue(), MAX_LIGHT_GLOWING));
				}
			}
		} else {
			for (final Depot depot : MinecraftClientData.getDashboardInstance().depots) {
				if (canDrawAreaText(depot)) {
					final Position position = depot.getCenter();
					drawFromWorldCoords(position.getX(), position.getZ(), (x1, y1) -> IDrawing.drawStringWithFont(graphicsHolder, depot.getName(), getX2() + x1.floatValue(), getY2() + y1.floatValue(), MAX_LIGHT_GLOWING));
				}
			}
		}

		final String mousePosText = String.format("(%.1f, %.1f)", mouseWorldPos.leftDouble(), mouseWorldPos.rightDouble());
		graphicsHolder.drawText(mousePosText, getX2() + width - TEXT_PADDING - GraphicsHolder.getTextWidth(mousePosText), getY2() + TEXT_PADDING, ARGB_WHITE, false, MAX_LIGHT_GLOWING);
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
	public boolean mouseScrolled3(double mouseX, double mouseY, double amount) {
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
		return mouseX >= getX2() && mouseY >= getY2() && mouseX < getX2() + width && mouseY < getY2() + height && !(mouseX >= getX2() + width - SQUARE_SIZE * 12 && mouseY >= getY2() + height - SQUARE_SIZE) && !isRestrictedMouseArea.apply(mouseX, mouseY);
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

	public void setOverlayMode(MapOverlayMode overlayMode) {
		this.mapOverlayMode = overlayMode;
		disposeMapTextures();
		if(world != null) updateMap(World.cast(world), mapOverlayMode);
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

	public void updateMap(World world, MapOverlayMode overlayMode) {
		final IntIntImmutablePair topLeft = coordsToWorldPos(0, 0);
		final IntIntImmutablePair bottomRight = coordsToWorldPos(width, height);

		for (int i = topLeft.leftInt(); i <= bottomRight.leftInt(); i += 16) {
			for (int j = topLeft.rightInt(); j <= bottomRight.rightInt(); j += 16) {
				int chunkX = i / 16;
				int chunkZ = j / 16;

				if(!world.getChunkManager().isChunkLoaded(chunkX, chunkZ)) continue;

				int chunkXStart = chunkX * 16;
				int chunkZStart = chunkZ * 16;
				IntIntImmutablePair chunkXZ = new IntIntImmutablePair(chunkX, chunkZ);

				MapImage mapTexture = mapTextures.get(chunkXZ);
				if(mapTexture == null) {
					NativeImage nativeImage = new NativeImage(16, 16, false);
					mapTexture = new MapImage(chunkX, chunkZ, new NativeImageBackedTexture(nativeImage));
				}

				// Loop each block in chunk
                for(int k = 0; k < 16; k++) {
                    for(int l = 0; l < 16; l++) {
                        final int blockX = chunkXStart + k;
                        final int blockZ = chunkZStart + l;
						// The y position of the highest block in this coordinates
						final int topY = world.getTopY(HeightMapType.getMotionBlockingMapped(), blockX, blockZ) - 1;
						final int blockY;
						if(overlayMode == MapOverlayMode.TOP_VIEW) {
							blockY = topY;
						} else {
							int currentY = Math.min(topY, player.getBlockPos().getY());

							// Find lowest Y level so it can be projected
							while(true) {
								if(currentY < -64 || !world.getBlockState(Init.newBlockPos(blockX, currentY, blockZ)).isAir()) {
									break;
								} else {
									currentY--;
								}
							}
							blockY = currentY;
						}

                        final BlockPos finalPos = Init.newBlockPos(blockX, blockY, blockZ);
                        final BlockPos lightReferencePos = finalPos.up();
                        int lightLevelBlock = world.getLightLevel(LightType.getBlockMapped(), lightReferencePos);
                        int lightLevelSky = world.getLightLevel(LightType.getSkyMapped(), lightReferencePos);
                        // Light Level determined by the max brightness of either what the sky produce or what the block produce
						int lightLevel = Math.max(lightLevelBlock, lightLevelSky);

                        int color = divideColorRGB(world.getBlockState(finalPos).getBlock().getDefaultMapColor().getColorMapped(), overlayMode == MapOverlayMode.TOP_VIEW ? 2 : 1 + ((15 - lightLevel) / 4));
                        mapTexture.texture.getImage().setPixelColor(k, l, convertColorABGR(color));
                    }
                }

                mapTexture.texture.upload();
                mapTextures.put(chunkXZ, mapTexture);
            }
		}
	}

	public void disposeMapTextures() {
		for(MapImage tex : mapTextures.values()) {
			tex.close();
		}
		mapTextures.clear();
	}

	private void drawRectangleFromWorldCoords(GuiDrawing guiDrawing, IntIntImmutablePair corner1, IntIntImmutablePair corner2, int color) {
		drawRectangleFromWorldCoords(guiDrawing, corner1.leftInt(), corner1.rightInt(), corner2.leftInt(), corner2.rightInt(), color);
	}

	private void drawRectangleFromWorldCoords(GuiDrawing guiDrawing, double posX1, double posZ1, double posX2, double posZ2, int color) {
		final double x1 = (posX1 - centerX) * scale + width / 2D;
		final double z1 = (posZ1 - centerY) * scale + height / 2D;
		final double x2 = (posX2 - centerX) * scale + width / 2D;
		final double z2 = (posZ2 - centerY) * scale + height / 2D;
		guiDrawing.drawRectangle(getX2() + Math.max(0, x1), getY2() + z1, getX2() + x2, getY2() + z2, color);
	}

	private void drawRectangleFromWorldCoords(GuiDrawing guiDrawing, double posX1, double posZ1, double posX2, double posZ2, Identifier img) {
		guiDrawing.beginDrawingTexture(img);
		final double x1 = (posX1 - centerX) * scale + width / 2D;
		final double z1 = (posZ1 - centerY) * scale + height / 2D;
		final double x2 = (posX2 - centerX) * scale + width / 2D;
		final double z2 = (posZ2 - centerY) * scale + height / 2D;

		guiDrawing.drawTexture(getX2() + Math.max(0, x1), getY2() + z1, getX2() + x2, getY2() + z2, 0, 0, 1, 1);
		guiDrawing.finishDrawingTexture();
	}

	private boolean canDrawAreaText(AreaBase<?, ?> areaBase) {
		return areaBase.getCenter() != null && scale >= 80F / Math.max(areaBase.getMaxX() - areaBase.getMinX(), areaBase.getMaxZ() - areaBase.getMinZ());
	}

	private <T extends SavedRailBase<T, U>, U extends AreaBase<U, T>> void drawSavedRail(GraphicsHolder graphicsHolder, Position position, ObjectArrayList<T> sortedSavedRails) {
		final int savedRailCount = sortedSavedRails.size();
		for (int i = 0; i < savedRailCount; i++) {
			final int index = i;
			drawFromWorldCoords(position.getX() + 0.5, position.getZ() + (i + 0.5) / savedRailCount, (x1, y1) -> graphicsHolder.drawCenteredText(sortedSavedRails.get(index).getName(), getX2() + x1.intValue(), getY2() + y1.intValue() - TEXT_HEIGHT / 2, ARGB_WHITE));
		}
	}

	private static int convertColorABGR(int rgb) {
		int a = 255;
		int r = (rgb >> 16) & 255;
		int g = (rgb >> 8) & 255;
		int b = (rgb) & 255;
		return a << 24 | b << 16 | g << 8 | r;
	}

	private static int divideColorRGB(int color, double amount) {
		final int r = (int)(((color >> 16) & 0xFF) / amount);
		final int g = (int)(((color >> 8) & 0xFF) / amount);
		final int b = (int)((color & 0xFF) / amount);
		return (r << 16) + (g << 8) + b;
	}

	@FunctionalInterface
	public interface OnDrawCorners {
		void onDrawCorners(IntIntImmutablePair corner1, IntIntImmutablePair corner2);
	}

	@FunctionalInterface
	private interface MouseOnSavedRailCallback {
		void mouseOnSavedRailCallback(SavedRailBase<?, ?> savedRail, double x1, double z1, double x2, double z2);
	}
	private class MapImage {
		private final Identifier textureId;
		private final NativeImageBackedTexture texture;
		private final int chunkX;
		private final int chunkZ;
		private boolean disposed = false;
		private MapImage(int chunkX, int chunkZ, NativeImageBackedTexture texture)  {
			this.textureId = MinecraftClient.getInstance().getTextureManager().registerDynamicTexture("mtr_dashboard_map", texture);
			this.texture = texture;
			this.chunkX = chunkX;
			this.chunkZ = chunkZ;
		}

		public void close() {
			disposed = true;
			MinecraftClient.getInstance().getTextureManager().destroyTexture(this.textureId);
		}
	}
	public enum MapOverlayMode { TOP_VIEW, CURRENT_Y }

	private enum MapState {DEFAULT, EDITING_AREA, EDITING_ROUTE}
}
