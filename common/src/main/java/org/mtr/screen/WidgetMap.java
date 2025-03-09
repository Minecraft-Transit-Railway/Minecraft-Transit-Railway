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
import net.minecraft.client.world.ClientWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.mtr.client.MinecraftClientData;
import org.mtr.core.data.*;
import org.mtr.core.tool.Utilities;
import org.mtr.data.IGui;
import org.mtr.generated.lang.TranslationProvider;

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
	private final ClientWorld world;
	private final ClientPlayerEntity player;
	private final WorldMap worldMap;
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
		world = minecraftClient.world;
		player = minecraftClient.player;
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
	protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
		final DoubleDoubleImmutablePair mouseWorldPos = coordsToWorldPos((double) mouseX - getX(), mouseY - getY());
		final IntIntImmutablePair topLeft = coordsToWorldPos(0, 0);
		final IntIntImmutablePair bottomRight = coordsToWorldPos(width, height);

		// Background
		context.fill(getX(), getY(), getX() + width, getY() + height, ARGB_BLACK);

		// World map
		if (world != null) {
			worldMap.tick(world, player, delta);
			worldMap.forEachTile(mapImage -> {
				final int posX = mapImage.chunkX * WorldMap.CHUNK_SIZE;
				final int posZ = mapImage.chunkZ * WorldMap.CHUNK_SIZE;
				if (posX + WorldMap.CHUNK_SIZE < topLeft.leftInt() || posZ + WorldMap.CHUNK_SIZE < (topLeft).rightInt() || posX > bottomRight.leftInt() || posZ > bottomRight.rightInt()) {
					return;
				}
				drawRectangleFromWorldCoords(context, mapImage.textureId, posX, posZ, posX + WorldMap.CHUNK_SIZE, posZ + WorldMap.CHUNK_SIZE);
			});
		}

		if (showStations) {
			flatPositionToPlatformMap.forEach((position, platforms) -> drawRectangleFromWorldCoords(context, position.getX(), position.getZ(), position.getX() + 1, position.getZ() + 1, ARGB_WHITE));
			for (final Station station : MinecraftClientData.getDashboardInstance().stations) {
				if (AreaBase.validCorners(station)) {
					drawRectangleFromWorldCoords(context, station.getMinX(), station.getMinZ(), station.getMaxX(), station.getMaxZ(), ARGB_BLACK_TRANSLUCENT + station.getColor());
				}
			}
			mouseOnSavedRail(mouseWorldPos, (savedRail, x1, z1, x2, z2) -> drawRectangleFromWorldCoords(context, x1, z1, x2, z2, ARGB_WHITE), true);
		} else {
			flatPositionToSidingMap.forEach((position, sidings) -> drawRectangleFromWorldCoords(context, position.getX(), position.getZ(), position.getX() + 1, position.getZ() + 1, ARGB_WHITE));
			for (final Depot depot : MinecraftClientData.getDashboardInstance().depots) {
				if (depot.isTransportMode(transportMode) && AreaBase.validCorners(depot)) {
					drawRectangleFromWorldCoords(context, depot.getMinX(), depot.getMinZ(), depot.getMaxX(), depot.getMaxZ(), ARGB_BLACK_TRANSLUCENT + depot.getColor());
				}
			}
			mouseOnSavedRail(mouseWorldPos, (savedRail, x1, z1, x2, z2) -> drawRectangleFromWorldCoords(context, x1, z1, x2, z2, ARGB_WHITE), false);
		}

		if (mapState == MapState.EDITING_AREA && drawArea1 != null && drawArea2 != null) {
			drawRectangleFromWorldCoords(context, drawArea1, drawArea2, ARGB_WHITE_TRANSLUCENT);
		}

		if (player != null) {
			drawPlayerSymbol(context, player);
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

	private void drawPlayerSymbol(DrawContext context, ClientPlayerEntity player) {
		drawFromWorldCoords(player.getX(), player.getZ(), (x1, y1) -> {
			context.fill(getX() + (int) Math.max(0, x1 - 2), getY() + y1.intValue() - 3, getX() + x1.intValue() + 2, getY() + y1.intValue() + 3, ARGB_WHITE);
			context.fill(getX() + (int) Math.max(0, x1 - 3), getY() + y1.intValue() - 2, getX() + x1.intValue() + 3, getY() + y1.intValue() + 2, ARGB_WHITE);
			context.fill(getX() + (int) Math.max(0, x1 - 2), getY() + y1.intValue() - 2, getX() + x1.intValue() + 2, getY() + y1.intValue() + 2, ARGB_BLUE);
		});
	}

	private void drawMousePositionText(DrawContext context, DoubleDoubleImmutablePair mouseWorldPos) {
		final String mousePosText = String.format("(%.1f, %.1f)", mouseWorldPos.leftDouble(), mouseWorldPos.rightDouble());
		final TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
		context.drawText(textRenderer, mousePosText, getX() + width - TEXT_PADDING - textRenderer.getWidth(mousePosText), getY() + TEXT_PADDING, ARGB_WHITE, false);
	}

	// Implicit overrides, don't add @Override
	public void setFocused2(boolean focused) {
	}

	// Implicit overrides, don't add @Override
	public boolean isFocused2() {
		return false;
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
		worldMap.setMapOverlayMode(mapOverlayMode);
		if (world != null) {
			worldMap.updateMap(world, player);
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

	private void drawRectangleFromWorldCoords(DrawContext context, IntIntImmutablePair corner1, IntIntImmutablePair corner2, int color) {
		drawRectangleFromWorldCoords(context, corner1.leftInt(), corner1.rightInt(), corner2.leftInt(), corner2.rightInt(), color);
	}

	private void drawRectangleFromWorldCoords(DrawContext context, double posX1, double posZ1, double posX2, double posZ2, int color) {
		final double x1 = (posX1 - centerX) * scale + width / 2D;
		final double z1 = (posZ1 - centerY) * scale + height / 2D;
		final double x2 = (posX2 - centerX) * scale + width / 2D;
		final double z2 = (posZ2 - centerY) * scale + height / 2D;
		context.fill(getX() + (int) Math.max(0, x1), getY() + (int) z1, getX() + (int) x2, getY() + (int) z2, color);
	}

	private void drawRectangleFromWorldCoords(DrawContext context, Identifier texture, double posX1, double posZ1, double posX2, double posZ2) {
		final double x1 = (posX1 - centerX) * scale + width / 2D;
		final double z1 = (posZ1 - centerY) * scale + height / 2D;
		final double x2 = (posX2 - centerX) * scale + width / 2D;
		final double z2 = (posZ2 - centerY) * scale + height / 2D;
		context.drawGuiTexture(RenderLayer::getGuiTextured, texture, getX() + (int) x1, getY() + (int) z1, (int) (x2 - x1), (int) (z2 - z1));
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
		void mouseOnSavedRailCallback(SavedRailBase<?, ?> savedRail, double x1, double z1, double x2, double z2);
	}

	private enum MapState {DEFAULT, EDITING_AREA, EDITING_ROUTE}
}
