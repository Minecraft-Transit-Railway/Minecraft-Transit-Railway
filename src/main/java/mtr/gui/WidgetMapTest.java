package mtr.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import mtr.data.Platform;
import mtr.data.Route;
import mtr.data.Station;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.Element;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Heightmap;

import java.util.ArrayList;
import java.util.List;


public class WidgetMapTest implements Drawable, Element, IGui {

	private final int x;
	private final int y;
	private final int width;
	private final int height;
	private final double playerX, playerZ;
	private final Runnable onMapChange;
	private final ClientWorld world;
	private final TextRenderer textRenderer;

	private double scale;
	private double centerX;
	private double centerY;
	private Station editingStation;
	private Route editingRoute;
	private Pair<Integer, Integer> drawStation1, drawStation2;
	private List<Station> moreStations;

	private static final int LEFT_MOUSE_BUTTON = 0;
	private static final int SCALE_UPPER_LIMIT = 16;
	private static final double SCALE_LOWER_LIMIT = 0.0078125;

	public WidgetMapTest(int x, int y, int width, int height, Runnable onMapChange) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.onMapChange = onMapChange;

		final MinecraftClient minecraftClient = MinecraftClient.getInstance();
		world = minecraftClient.world;
		textRenderer = minecraftClient.textRenderer;
		if (minecraftClient.player == null) {
			centerX = playerX = 0;
			centerY = playerZ = 0;
		} else {
			centerX = playerX = minecraftClient.player.getX();
			centerY = playerZ = minecraftClient.player.getZ();
		}
		scale = 1;
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
					final int color = IGui.divideColorRGB(world.getBlockState(new BlockPos(i, world.getTopY(Heightmap.Type.MOTION_BLOCKING, i, j) - 1, j)).getBlock().getDefaultMaterialColor().color, 2);
					drawRectangleFromWorldCoords(buffer, i, j, i + increment, j + increment, ARGB_BLACK + color);
				}
			}
		}

		for (Platform platform : ScreenBase.GuiBase.platforms) {
			final BlockPos posStart = platform.getPos1();
			final BlockPos posEnd = platform.getPos2().add(1, 0, 1);
			drawRectangleFromWorldCoords(buffer, posStart.getX(), posStart.getZ(), posEnd.getX(), posEnd.getZ(), ARGB_WHITE);
		}
		for (Station station : ScreenBase.GuiBase.stations) {
			drawRectangleFromWorldCoords(buffer, station.corner1, station.corner2, ARGB_BLACK_TRANSLUCENT + station.color);
		}

		if (editingStation != null) {
			if (drawStation1 != null && drawStation2 != null) {
				drawRectangleFromWorldCoords(buffer, drawStation1, drawStation2, ARGB_WHITE_TRANSLUCENT);
			}
		}

		final double playerCoordX = (playerX - centerX) * scale + width / 2D;
		final double playerCoordY = (playerZ - centerY) * scale + height / 2D;
		drawRectangle(buffer, playerCoordX - 2, playerCoordY - 3, playerCoordX + 2, playerCoordY + 3, ARGB_WHITE);
		drawRectangle(buffer, playerCoordX - 3, playerCoordY - 2, playerCoordX + 3, playerCoordY + 2, ARGB_WHITE);
		drawRectangle(buffer, playerCoordX - 2, playerCoordY - 2, playerCoordX + 2, playerCoordY + 2, ARGB_BLUE);

		tessellator.draw();
		RenderSystem.enableTexture();
		RenderSystem.disableBlend();


		if (editingStation != null) {
			DrawableHelper.drawStringWithShadow(matrices, textRenderer, new TranslatableText("gui.mtr.edit_station").getString(), x + TEXT_PADDING, y + TEXT_PADDING, ARGB_WHITE);
		}

		if (editingRoute != null) {
			DrawableHelper.drawStringWithShadow(matrices, textRenderer, new TranslatableText("gui.mtr.edit_route").getString(), x + TEXT_PADDING, y + TEXT_PADDING, ARGB_WHITE);
			for (int i = 0; i < moreStations.size(); i++) {
				DrawableHelper.drawStringWithShadow(matrices, textRenderer, IGui.formatStationName(moreStations.get(i).name), x + TEXT_PADDING, y + TEXT_PADDING * 2 + LINE_HEIGHT * (i + 1), ARGB_WHITE);
			}
		}

		final Pair<Integer, Integer> mouseWorldPos = coordsToWorldPos(mouseX - x, mouseY - y);
		final String mousePosText = String.format("(%d, %d)", mouseWorldPos.getLeft(), mouseWorldPos.getRight());
		DrawableHelper.drawStringWithShadow(matrices, textRenderer, mousePosText, x + width - TEXT_PADDING - textRenderer.getWidth(mousePosText), y + TEXT_PADDING, ARGB_WHITE);
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
		if (editingStation != null && button == LEFT_MOUSE_BUTTON) {
			drawStation2 = coordsToWorldPos((int) Math.round(mouseX - x), (int) Math.round(mouseY - y));
			if (drawStation1.getLeft().equals(drawStation2.getLeft())) {
				drawStation2 = new Pair<>(drawStation2.getLeft() + 1, drawStation2.getRight());
			}
			if (drawStation1.getRight().equals(drawStation2.getRight())) {
				drawStation2 = new Pair<>(drawStation2.getLeft(), drawStation2.getRight() + 1);
			}
		} else {
			centerX -= deltaX / scale;
			centerY -= deltaY / scale;
		}
		return true;
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (isMouseOver(mouseX, mouseY)) {
			if (editingStation != null && button == LEFT_MOUSE_BUTTON) {
				drawStation1 = coordsToWorldPos((int) (mouseX - x), (int) (mouseY - y));
				drawStation2 = new Pair<>(drawStation1.getLeft() + 1, drawStation1.getRight() + 1);
			} else if (editingRoute != null) {
				Pair<Integer, Integer> worldPos = coordsToWorldPos((int) (mouseX - x), (int) (mouseY - y));
				ScreenBase.GuiBase.stations.stream().filter(station -> station.inStation(worldPos.getLeft(), worldPos.getRight())).findAny().ifPresent(station -> moreStations.add(station));
			}
			onMapChange.run();
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
		return mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height - SQUARE_SIZE * 2;
	}

	public void scale(double amount) {
		scale *= Math.pow(2, amount);
		scale = MathHelper.clamp(scale, SCALE_LOWER_LIMIT, SCALE_UPPER_LIMIT);
	}

	public void startEditingStation(Station editingStation) {
		this.editingStation = editingStation;
		editingRoute = null;
		drawStation1 = editingStation.corner1;
		drawStation2 = editingStation.corner2;
	}

	public void startEditingRoute(Route editingRoute) {
		editingStation = null;
		this.editingRoute = editingRoute;
		moreStations = new ArrayList<>();
	}

	public void stopEditing() {
		editingStation = null;
		editingRoute = null;
	}

	public boolean stationDrawn() {
		return drawStation1 != null && drawStation2 != null;
	}

	public void onDoneEditingStation(String name, int color) {
		ScreenBase.GuiBase.stations.remove(editingStation);
		editingStation.name = name;
		editingStation.corner1 = drawStation1;
		editingStation.corner2 = drawStation2;
		editingStation.color = color;
		ScreenBase.GuiBase.stations.add(editingStation);
	}

	public void onDoneEditingRoute(String name, int color) {
		ScreenBase.GuiBase.routes.remove(editingRoute);
		editingRoute.name = name;
		editingRoute.color = color;
		moreStations.forEach(station -> editingRoute.stationIds.add(station.id));
		ScreenBase.GuiBase.routes.add(editingRoute);
	}

	private Pair<Integer, Integer> coordsToWorldPos(int mouseX, int mouseY) {
		final int left = (int) Math.floor((mouseX - width / 2D) / scale + centerX);
		final int right = (int) Math.floor((mouseY - height / 2D) / scale + centerY);
		return new Pair<>(left, right);
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
			IGui.drawRectangle(buffer, x + x1, y + y1, x + x2, y + y2, color);
		}
	}
}
