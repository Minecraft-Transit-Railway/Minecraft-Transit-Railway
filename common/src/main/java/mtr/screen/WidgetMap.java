package mtr.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import mtr.client.ClientData;
import mtr.client.IDrawing;
import mtr.data.*;
import mtr.mappings.SelectableMapper;
import mtr.mappings.Text;
import mtr.mappings.UtilitiesClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.Tuple;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;


public class WidgetMap implements Widget, SelectableMapper, GuiEventListener, IGui {

	private int x;
	private int y;
	private int width;
	private int height;
	private double scale;
	private double centerX;
	private double centerY;
	private Tuple<Integer, Integer> drawArea1, drawArea2;
	private MapState mapState;
	private boolean showStations;

	private final TransportMode transportMode;
	private final OnDrawCorners onDrawCorners;
	private final Runnable onDrawCornersMouseRelease;
	private final Consumer<Long> onClickAddPlatformToRoute;
	private final Consumer<SavedRailBase> onClickEditSavedRail;
	private final BiFunction<Double, Double, Boolean> isRestrictedMouseArea;
	private final ClientLevel world;
	private final LocalPlayer player;
	private final Font textRenderer;

	private static final int ARGB_BLUE = 0xFF4285F4;
	private static final int SCALE_UPPER_LIMIT = 64;
	private static final double SCALE_LOWER_LIMIT = 1 / 128D;

	public WidgetMap(TransportMode transportMode, OnDrawCorners onDrawCorners, Runnable onDrawCornersMouseRelease, Consumer<Long> onClickAddPlatformToRoute, Consumer<SavedRailBase> onClickEditSavedRail, BiFunction<Double, Double, Boolean> isRestrictedMouseArea) {
		this.transportMode = transportMode;
		this.onDrawCorners = onDrawCorners;
		this.onDrawCornersMouseRelease = onDrawCornersMouseRelease;
		this.onClickAddPlatformToRoute = onClickAddPlatformToRoute;
		this.onClickEditSavedRail = onClickEditSavedRail;
		this.isRestrictedMouseArea = isRestrictedMouseArea;

		final Minecraft minecraftClient = Minecraft.getInstance();
		world = minecraftClient.level;
		player = minecraftClient.player;
		textRenderer = minecraftClient.font;
		if (player == null) {
			centerX = 0;
			centerY = 0;
		} else {
			centerX = player.getX();
			centerY = player.getZ();
		}
		scale = 1;
		setShowStations(true);
	}

	@Override
	public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
		final Tesselator tesselator = Tesselator.getInstance();
		final BufferBuilder buffer = tesselator.getBuilder();
		UtilitiesClient.beginDrawingRectangle(buffer);
		RenderSystem.enableBlend();

		final Tuple<Integer, Integer> topLeft = coordsToWorldPos(0, 0);
		final Tuple<Integer, Integer> bottomRight = coordsToWorldPos(width, height);
		final int increment = scale >= 1 ? 1 : (int) Math.ceil(1 / scale);
		for (int i = topLeft.getA(); i <= bottomRight.getA(); i += increment) {
			for (int j = topLeft.getB(); j <= bottomRight.getB(); j += increment) {
				if (world != null) {
					final int color = divideColorRGB(world.getBlockState(new BlockPos(i, world.getHeight(Heightmap.Types.MOTION_BLOCKING, i, j) - 1, j)).getBlock().defaultMaterialColor().col, 2);
					drawRectangleFromWorldCoords(buffer, i, j, i + increment, j + increment, ARGB_BLACK | color);
				}
			}
		}

		final Tuple<Double, Double> mouseWorldPos = coordsToWorldPos((double) mouseX - x, mouseY - y);

		try {
			if (showStations) {
				ClientData.DATA_CACHE.getPosToPlatforms(transportMode).forEach((platformPos, platforms) -> drawRectangleFromWorldCoords(buffer, platformPos.getX(), platformPos.getZ(), platformPos.getX() + 1, platformPos.getZ() + 1, ARGB_WHITE));
				for (final Station station : ClientData.STATIONS) {
					if (AreaBase.nonNullCorners(station)) {
						drawRectangleFromWorldCoords(buffer, station.corner1, station.corner2, ARGB_BLACK_TRANSLUCENT + station.color);
					}
				}
				mouseOnSavedRail(mouseWorldPos, (savedRail, x1, z1, x2, z2) -> drawRectangleFromWorldCoords(buffer, x1, z1, x2, z2, ARGB_WHITE), true);
			} else {
				ClientData.DATA_CACHE.getPosToSidings(transportMode).forEach((sidingPos, sidings) -> drawRectangleFromWorldCoords(buffer, sidingPos.getX(), sidingPos.getZ(), sidingPos.getX() + 1, sidingPos.getZ() + 1, ARGB_WHITE));
				for (final Depot depot : ClientData.DEPOTS) {
					if (depot.isTransportMode(transportMode) && AreaBase.nonNullCorners(depot)) {
						drawRectangleFromWorldCoords(buffer, depot.corner1, depot.corner2, ARGB_BLACK_TRANSLUCENT + depot.color);
					}
				}
				mouseOnSavedRail(mouseWorldPos, (savedRail, x1, z1, x2, z2) -> drawRectangleFromWorldCoords(buffer, x1, z1, x2, z2, ARGB_WHITE), false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (mapState == MapState.EDITING_AREA && drawArea1 != null && drawArea2 != null) {
			drawRectangleFromWorldCoords(buffer, drawArea1, drawArea2, ARGB_WHITE_TRANSLUCENT);
		}

		if (player != null) {
			drawFromWorldCoords(player.getX(), player.getZ(), (x1, y1) -> {
				drawRectangle(buffer, x1 - 2, y1 - 3, x1 + 2, y1 + 3, ARGB_WHITE);
				drawRectangle(buffer, x1 - 3, y1 - 2, x1 + 3, y1 + 2, ARGB_WHITE);
				drawRectangle(buffer, x1 - 2, y1 - 2, x1 + 2, y1 + 2, ARGB_BLUE);
			});
		}

		tesselator.end();
		RenderSystem.disableBlend();
		UtilitiesClient.finishDrawingRectangle();

		if (mapState == MapState.EDITING_AREA) {
			Gui.drawString(matrices, textRenderer, Text.translatable("gui.mtr.edit_area").getString(), x + TEXT_PADDING, y + TEXT_PADDING, ARGB_WHITE);
		} else if (mapState == MapState.EDITING_ROUTE) {
			Gui.drawString(matrices, textRenderer, Text.translatable("gui.mtr.edit_route").getString(), x + TEXT_PADDING, y + TEXT_PADDING, ARGB_WHITE);
		}

		if (scale >= 8) {
			try {
				if (showStations) {
					ClientData.DATA_CACHE.getPosToPlatforms(transportMode).forEach((platformPos, platforms) -> drawSavedRail(matrices, platformPos, platforms));
				} else {
					ClientData.DATA_CACHE.getPosToSidings(transportMode).forEach((sidingPos, sidings) -> drawSavedRail(matrices, sidingPos, sidings));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		final MultiBufferSource.BufferSource immediate = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
		if (showStations) {
			for (final Station station : ClientData.STATIONS) {
				if (canDrawAreaText(station)) {
					final BlockPos pos = station.getCenter();
					final String stationString = String.format("%s|(%s)", station.name, Text.translatable("gui.mtr.zone_number", station.zone).getString());
					drawFromWorldCoords(pos.getX(), pos.getZ(), (x1, y1) -> IDrawing.drawStringWithFont(matrices, textRenderer, immediate, stationString, x + (float) x1, y + (float) y1, MAX_LIGHT_GLOWING));
				}
			}
		} else {
			for (final Depot depot : ClientData.DEPOTS) {
				if (canDrawAreaText(depot)) {
					final BlockPos pos = depot.getCenter();
					drawFromWorldCoords(pos.getX(), pos.getZ(), (x1, y1) -> IDrawing.drawStringWithFont(matrices, textRenderer, immediate, depot.name, x + (float) x1, y + (float) y1, MAX_LIGHT_GLOWING));
				}
			}
		}
		immediate.endBatch();

		final String mousePosText = String.format("(%s, %s)", RailwayData.round(mouseWorldPos.getA(), 1), RailwayData.round(mouseWorldPos.getB(), 1));
		Gui.drawString(matrices, textRenderer, mousePosText, x + width - TEXT_PADDING - textRenderer.width(mousePosText), y + TEXT_PADDING, ARGB_WHITE);
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
		if (mapState == MapState.EDITING_AREA) {
			drawArea2 = coordsToWorldPos((int) Math.round(mouseX - x), (int) Math.round(mouseY - y));
			if (drawArea1.getA().equals(drawArea2.getA())) {
				drawArea2 = new Tuple<>(drawArea2.getA() + 1, drawArea2.getB());
			}
			if (drawArea1.getB().equals(drawArea2.getB())) {
				drawArea2 = new Tuple<>(drawArea2.getA(), drawArea2.getB() + 1);
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
			if (ClientData.hasPermission()) {
				if (mapState == MapState.EDITING_AREA) {
					drawArea1 = coordsToWorldPos((int) (mouseX - x), (int) (mouseY - y));
					drawArea2 = null;
				} else if (mapState == MapState.EDITING_ROUTE) {
					final Tuple<Double, Double> mouseWorldPos = coordsToWorldPos(mouseX - x, mouseY - y);
					mouseOnSavedRail(mouseWorldPos, (savedRail, x1, z1, x2, z2) -> onClickAddPlatformToRoute.accept(savedRail.id), true);
				} else {
					final Tuple<Double, Double> mouseWorldPos = coordsToWorldPos(mouseX - x, mouseY - y);
					mouseOnSavedRail(mouseWorldPos, (savedRail, x1, z1, x2, z2) -> onClickEditSavedRail.accept(savedRail), showStations);
				}
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
		final double oldScale = scale;
		if (oldScale > SCALE_LOWER_LIMIT && amount < 0) {
			centerX -= (mouseX - x - width / 2D) / scale;
			centerY -= (mouseY - y - height / 2D) / scale;
		}
		scale(amount);
		if (oldScale < SCALE_UPPER_LIMIT && amount > 0) {
			centerX += (mouseX - x - width / 2D) / scale;
			centerY += (mouseY - y - height / 2D) / scale;
		}
		return true;
	}

	@Override
	public boolean isMouseOver(double mouseX, double mouseY) {
		return mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height && !(mouseX >= x + width - SQUARE_SIZE * 10 && mouseY >= y + height - SQUARE_SIZE) && !isRestrictedMouseArea.apply(mouseX, mouseY);
	}

	public void setPositionAndSize(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public void scale(double amount) {
		scale *= Math.pow(2, amount);
		scale = Mth.clamp(scale, SCALE_LOWER_LIMIT, SCALE_UPPER_LIMIT);
	}

	public void find(double x1, double z1, double x2, double z2) {
		centerX = (x1 + x2) / 2;
		centerY = (z1 + z2) / 2;
		scale = Math.max(2, scale);
	}

	public void find(BlockPos pos) {
		centerX = pos.getX();
		centerY = pos.getZ();
		scale = Math.max(8, scale);
	}

	public void startEditingArea(AreaBase editingArea) {
		mapState = MapState.EDITING_AREA;
		drawArea1 = editingArea.corner1;
		drawArea2 = editingArea.corner2;
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

	private void mouseOnSavedRail(Tuple<Double, Double> mouseWorldPos, MouseOnSavedRailCallback mouseOnSavedRailCallback, boolean isPlatform) {
		try {
			(isPlatform ? ClientData.DATA_CACHE.getPosToPlatforms(transportMode) : ClientData.DATA_CACHE.getPosToSidings(transportMode)).forEach((savedRailPos, savedRails) -> {
				final int savedRailCount = savedRails.size();
				for (int i = 0; i < savedRailCount; i++) {
					final float left = savedRailPos.getX();
					final float right = savedRailPos.getX() + 1;
					final float top = savedRailPos.getZ() + (float) i / savedRailCount;
					final float bottom = savedRailPos.getZ() + (i + 1F) / savedRailCount;
					if (RailwayData.isBetween(mouseWorldPos.getA(), left, right) && RailwayData.isBetween(mouseWorldPos.getB(), top, bottom)) {
						mouseOnSavedRailCallback.mouseOnSavedRailCallback(savedRails.get(i), left, top, right, bottom);
					}
				}
			});
		} catch (ConcurrentModificationException ignored) {
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Tuple<Integer, Integer> coordsToWorldPos(int mouseX, int mouseY) {
		final Tuple<Double, Double> worldPos = coordsToWorldPos((double) mouseX, mouseY);
		return new Tuple<>((int) Math.floor(worldPos.getA()), (int) Math.floor(worldPos.getB()));
	}

	private Tuple<Double, Double> coordsToWorldPos(double mouseX, double mouseY) {
		final double left = (mouseX - width / 2D) / scale + centerX;
		final double right = (mouseY - height / 2D) / scale + centerY;
		return new Tuple<>(left, right);
	}

	private void drawFromWorldCoords(double worldX, double worldZ, DrawFromWorldCoords callback) {
		final double coordsX = (worldX - centerX) * scale + width / 2D;
		final double coordsY = (worldZ - centerY) * scale + height / 2D;
		callback.drawFromWorldCoords(coordsX, coordsY);
	}

	private void drawRectangleFromWorldCoords(BufferBuilder buffer, Tuple<Integer, Integer> corner1, Tuple<Integer, Integer> corner2, int color) {
		drawRectangleFromWorldCoords(buffer, corner1.getA(), corner1.getB(), corner2.getA(), corner2.getB(), color);
	}

	private void drawRectangleFromWorldCoords(BufferBuilder buffer, double posX1, double posZ1, double posX2, double posZ2, int color) {
		final double x1 = (posX1 - centerX) * scale + width / 2D;
		final double z1 = (posZ1 - centerY) * scale + height / 2D;
		final double x2 = (posX2 - centerX) * scale + width / 2D;
		final double z2 = (posZ2 - centerY) * scale + height / 2D;
		drawRectangle(buffer, x1, z1, x2, z2, color);
	}

	private void drawRectangle(BufferBuilder buffer, double xA, double yA, double xB, double yB, int color) {
		final double x1 = Math.min(xA, xB);
		final double y1 = Math.min(yA, yB);
		final double x2 = Math.max(xA, xB);
		final double y2 = Math.max(yA, yB);
		if (x1 < width && y1 < height && x2 >= 0 && y2 >= 0) {
			IDrawing.drawRectangle(buffer, x + Math.max(0, x1), y + y1, x + x2, y + y2, color);
		}
	}

	private boolean canDrawAreaText(AreaBase areaBase) {
		return areaBase.getCenter() != null && scale >= 80F / Math.max(Math.abs(areaBase.corner1.getA() - areaBase.corner2.getA()), Math.abs(areaBase.corner1.getB() - areaBase.corner2.getB()));
	}

	private void drawSavedRail(PoseStack matrices, BlockPos savedRailPos, List<? extends SavedRailBase> savedRails) {
		final int savedRailCount = savedRails.size();
		for (int i = 0; i < savedRailCount; i++) {
			final int index = i;
			drawFromWorldCoords(savedRailPos.getX() + 0.5, savedRailPos.getZ() + (i + 0.5) / savedRailCount, (x1, y1) -> Gui.drawCenteredString(matrices, textRenderer, savedRails.get(index).name, x + (int) x1, y + (int) y1 - TEXT_HEIGHT / 2, ARGB_WHITE));
		}
	}

	private static int divideColorRGB(int color, int amount) {
		final int r = ((color >> 16) & 0xFF) / amount;
		final int g = ((color >> 8) & 0xFF) / amount;
		final int b = (color & 0xFF) / amount;
		return (r << 16) + (g << 8) + b;
	}

	@FunctionalInterface
	public interface OnDrawCorners {
		void onDrawCorners(Tuple<Integer, Integer> corner1, Tuple<Integer, Integer> corner2);
	}

	@FunctionalInterface
	private interface DrawFromWorldCoords {
		void drawFromWorldCoords(double x1, double y1);
	}

	@FunctionalInterface
	private interface MouseOnSavedRailCallback {
		void mouseOnSavedRailCallback(SavedRailBase savedRail, double x1, double z1, double x2, double z2);
	}

	private enum MapState {DEFAULT, EDITING_AREA, EDITING_ROUTE}
}
