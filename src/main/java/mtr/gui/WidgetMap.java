package mtr.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import mtr.data.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.Element;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Heightmap;

import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.function.Consumer;


public class WidgetMap implements Drawable, Element, IGui {

	private int x;
	private int y;
	private int width;
	private int height;
	private double scale;
	private double centerX;
	private double centerY;
	private Pair<Integer, Integer> drawArea1, drawArea2;
	private MapState mapState;
	private boolean showStations;

	private final OnDrawCorners onDrawCorners;
	private final Runnable onDrawCornersMouseRelease;
	private final Consumer<Long> onClickAddPlatformToRoute;
	private final Consumer<SavedRailBase> onClickEditSavedRail;
	private final ClientWorld world;
	private final ClientPlayerEntity player;
	private final TextRenderer textRenderer;

	private static final int ARGB_BLUE = 0xFF4285F4;
	private static final int SCALE_UPPER_LIMIT = 64;
	private static final double SCALE_LOWER_LIMIT = 1 / 128D;

	public WidgetMap(OnDrawCorners onDrawCorners, Runnable onDrawCornersMouseRelease, Consumer<Long> onClickAddPlatformToRoute, Consumer<SavedRailBase> onClickEditSavedRail) {
		this.onDrawCorners = onDrawCorners;
		this.onDrawCornersMouseRelease = onDrawCornersMouseRelease;
		this.onClickAddPlatformToRoute = onClickAddPlatformToRoute;
		this.onClickEditSavedRail = onClickEditSavedRail;

		final MinecraftClient minecraftClient = MinecraftClient.getInstance();
		world = minecraftClient.world;
		player = minecraftClient.player;
		textRenderer = minecraftClient.textRenderer;
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
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		final Tessellator tessellator = Tessellator.getInstance();
		final BufferBuilder buffer = tessellator.getBuffer();
		RenderSystem.enableBlend();
		RenderSystem.disableTexture();
		RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);
		buffer.begin(7, VertexFormats.POSITION_COLOR);

		final Pair<Integer, Integer> topLeft = coordsToWorldPos(0, 0);
		final Pair<Integer, Integer> bottomRight = coordsToWorldPos(width, height);
		final int increment = scale >= 1 ? 1 : (int) Math.ceil(1 / scale);
		for (int i = topLeft.getLeft(); i <= bottomRight.getLeft(); i += increment) {
			for (int j = topLeft.getRight(); j <= bottomRight.getRight(); j += increment) {
				if (world != null) {
					final int color = divideColorRGB(world.getBlockState(new BlockPos(i, world.getTopY(Heightmap.Type.MOTION_BLOCKING, i, j) - 1, j)).getBlock().getDefaultMapColor().color, 2);
					drawRectangleFromWorldCoords(buffer, i, j, i + increment, j + increment, ARGB_BLACK + color);
				}
			}
		}

		final Pair<Double, Double> mouseWorldPos = coordsToWorldPos((double) mouseX - x, mouseY - y);

		try {
			if (showStations) {
				ClientData.DATA_CACHE.posToPlatforms.forEach((platformPos, platforms) -> drawRectangleFromWorldCoords(buffer, platformPos.getX(), platformPos.getZ(), platformPos.getX() + 1, platformPos.getZ() + 1, ARGB_WHITE));
				for (final Station station : ClientData.STATIONS) {
					if (AreaBase.nonNullCorners(station)) {
						drawRectangleFromWorldCoords(buffer, station.corner1, station.corner2, ARGB_BLACK_TRANSLUCENT + station.color);
					}
				}
				mouseOnSavedRail(mouseWorldPos, (savedRail, x1, z1, x2, z2) -> drawRectangleFromWorldCoords(buffer, x1, z1, x2, z2, ARGB_WHITE), true);
			} else {
				ClientData.DATA_CACHE.posToSidings.forEach((sidingPos, sidings) -> drawRectangleFromWorldCoords(buffer, sidingPos.getX(), sidingPos.getZ(), sidingPos.getX() + 1, sidingPos.getZ() + 1, ARGB_WHITE));
				for (final Depot depot : ClientData.DEPOTS) {
					if (AreaBase.nonNullCorners(depot)) {
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

		tessellator.draw();
		RenderSystem.enableTexture();
		RenderSystem.disableBlend();


		if (mapState == MapState.EDITING_AREA) {
			DrawableHelper.drawStringWithShadow(matrices, textRenderer, new TranslatableText("gui.mtr.edit_area").getString(), x + TEXT_PADDING, y + TEXT_PADDING, ARGB_WHITE);
		} else if (mapState == MapState.EDITING_ROUTE) {
			DrawableHelper.drawStringWithShadow(matrices, textRenderer, new TranslatableText("gui.mtr.edit_route").getString(), x + TEXT_PADDING, y + TEXT_PADDING, ARGB_WHITE);
		}
		if (scale >= 8) {
			try {
				if (showStations) {
					ClientData.DATA_CACHE.posToPlatforms.forEach((platformPos, platforms) -> drawSavedRail(matrices, platformPos, platforms));
				} else {
					ClientData.DATA_CACHE.posToSidings.forEach((sidingPos, sidings) -> drawSavedRail(matrices, sidingPos, sidings));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (scale >= 2) {
			final VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
			if (showStations) {
				for (final Station station : ClientData.STATIONS) {
					final BlockPos pos = station.getCenter();
					if (pos != null) {
						final String stationString = String.format("%s|(%s)", station.name, new TranslatableText("gui.mtr.zone_number", station.zone).getString());
						drawFromWorldCoords(pos.getX(), pos.getZ(), (x1, y1) -> IDrawing.drawStringWithFont(matrices, textRenderer, immediate, stationString, x + (float) x1, y + (float) y1, MAX_LIGHT_GLOWING));
					}
				}
			} else {
				for (final Depot depot : ClientData.DEPOTS) {
					final BlockPos pos = depot.getCenter();
					if (pos != null) {
						drawFromWorldCoords(pos.getX(), pos.getZ(), (x1, y1) -> IDrawing.drawStringWithFont(matrices, textRenderer, immediate, depot.name, x + (float) x1, y + (float) y1, MAX_LIGHT_GLOWING));
					}
				}
			}
			immediate.draw();
		}

		final String mousePosText = String.format("(%s, %s)", Math.round(mouseWorldPos.getLeft() * 10) / 10F, Math.round(mouseWorldPos.getRight() * 10) / 10F);
		DrawableHelper.drawStringWithShadow(matrices, textRenderer, mousePosText, x + width - TEXT_PADDING - textRenderer.getWidth(mousePosText), y + TEXT_PADDING, ARGB_WHITE);
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
		if (mapState == MapState.EDITING_AREA) {
			drawArea2 = coordsToWorldPos((int) Math.round(mouseX - x), (int) Math.round(mouseY - y));
			if (drawArea1.getLeft().equals(drawArea2.getLeft())) {
				drawArea2 = new Pair<>(drawArea2.getLeft() + 1, drawArea2.getRight());
			}
			if (drawArea1.getRight().equals(drawArea2.getRight())) {
				drawArea2 = new Pair<>(drawArea2.getLeft(), drawArea2.getRight() + 1);
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
			if (mapState == MapState.EDITING_AREA) {
				drawArea1 = coordsToWorldPos((int) (mouseX - x), (int) (mouseY - y));
				drawArea2 = null;
			} else if (mapState == MapState.EDITING_ROUTE) {
				final Pair<Double, Double> mouseWorldPos = coordsToWorldPos(mouseX - x, mouseY - y);
				mouseOnSavedRail(mouseWorldPos, (savedRail, x1, z1, x2, z2) -> onClickAddPlatformToRoute.accept(savedRail.id), true);
			} else {
				final Pair<Double, Double> mouseWorldPos = coordsToWorldPos(mouseX - x, mouseY - y);
				mouseOnSavedRail(mouseWorldPos, (savedRail, x1, z1, x2, z2) -> onClickEditSavedRail.accept(savedRail), showStations);
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
		return mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height && !(mouseX >= x + width - SQUARE_SIZE * 5 && mouseY >= y + height - SQUARE_SIZE);
	}

	public void setPositionAndSize(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public void scale(double amount) {
		scale *= Math.pow(2, amount);
		scale = MathHelper.clamp(scale, SCALE_LOWER_LIMIT, SCALE_UPPER_LIMIT);
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

	private void mouseOnSavedRail(Pair<Double, Double> mouseWorldPos, MouseOnSavedRailCallback mouseOnSavedRailCallback, boolean isPlatform) {
		try {
			(isPlatform ? ClientData.DATA_CACHE.posToPlatforms : ClientData.DATA_CACHE.posToSidings).forEach((savedRailPos, savedRails) -> {
				final int savedRailCount = savedRails.size();
				for (int i = 0; i < savedRailCount; i++) {
					final float left = savedRailPos.getX();
					final float right = savedRailPos.getX() + 1;
					final float top = savedRailPos.getZ() + (float) i / savedRailCount;
					final float bottom = savedRailPos.getZ() + (i + 1F) / savedRailCount;
					if (RailwayData.isBetween(mouseWorldPos.getLeft(), left, right) && RailwayData.isBetween(mouseWorldPos.getRight(), top, bottom)) {
						mouseOnSavedRailCallback.mouseOnSavedRailCallback(savedRails.get(i), left, top, right, bottom);
					}
				}
			});
		} catch (ConcurrentModificationException ignored) {
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Pair<Integer, Integer> coordsToWorldPos(int mouseX, int mouseY) {
		final Pair<Double, Double> worldPos = coordsToWorldPos((double) mouseX, mouseY);
		return new Pair<>((int) Math.floor(worldPos.getLeft()), (int) Math.floor(worldPos.getRight()));
	}

	private Pair<Double, Double> coordsToWorldPos(double mouseX, double mouseY) {
		final double left = (mouseX - width / 2D) / scale + centerX;
		final double right = (mouseY - height / 2D) / scale + centerY;
		return new Pair<>(left, right);
	}

	private void drawFromWorldCoords(double worldX, double worldZ, DrawFromWorldCoords callback) {
		final double coordsX = (worldX - centerX) * scale + width / 2D;
		final double coordsY = (worldZ - centerY) * scale + height / 2D;
		callback.drawFromWorldCoords(coordsX, coordsY);
	}

	private void drawRectangleFromWorldCoords(BufferBuilder buffer, Pair<Integer, Integer> corner1, Pair<Integer, Integer> corner2, int color) {
		drawRectangleFromWorldCoords(buffer, corner1.getLeft(), corner1.getRight(), corner2.getLeft(), corner2.getRight(), color);
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
			IDrawing.drawRectangle(buffer, x + x1, y + y1, x + x2, y + y2, color);
		}
	}

	private void drawSavedRail(MatrixStack matrices, BlockPos savedRailPos, List<? extends SavedRailBase> savedRails) {
		final int savedRailCount = savedRails.size();
		for (int i = 0; i < savedRailCount; i++) {
			final int index = i;
			drawFromWorldCoords(savedRailPos.getX() + 0.5, savedRailPos.getZ() + (i + 0.5) / savedRailCount, (x1, y1) -> DrawableHelper.drawCenteredText(matrices, textRenderer, savedRails.get(index).name, x + (int) x1, y + (int) y1 - TEXT_HEIGHT / 2, ARGB_WHITE));
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
		void onDrawCorners(Pair<Integer, Integer> corner1, Pair<Integer, Integer> corner2);
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
