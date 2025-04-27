package org.mtr.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import it.unimi.dsi.fastutil.doubles.DoubleDoubleImmutablePair;
import it.unimi.dsi.fastutil.ints.IntIntImmutablePair;
import it.unimi.dsi.fastutil.longs.Long2FloatAVLTreeMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectAVLTreeMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
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
import net.minecraft.util.math.ColorHelper;
import org.joml.Matrix4f;
import org.mtr.MTRClient;
import org.mtr.client.IDrawing;
import org.mtr.client.MinecraftClientData;
import org.mtr.core.data.*;
import org.mtr.core.operation.DeleteDataRequest;
import org.mtr.core.tool.Utilities;
import org.mtr.data.IGui;
import org.mtr.font.FontGroups;
import org.mtr.font.FontRenderOptions;
import org.mtr.map.MapTileProvider;
import org.mtr.tool.Drawing;
import org.mtr.tool.GuiAnimation;
import org.mtr.tool.GuiHelper;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;


public final class MapWidget extends ClickableWidget {

	@Nullable
	private IntIntImmutablePair drawArea1, drawArea2;
	private MapState mapState;
	private boolean showStations;
	private boolean showDepots;
	@Nullable
	private ObjectObjectImmutablePair<ScrollableListWidget<?>, IntIntImmutablePair> popupDetails;

	private final TransportMode transportMode;
	private final BiConsumer<NameColorDataBase, DeleteDataRequest> onDeleteArea;
	private final BiConsumer<IntIntImmutablePair, IntIntImmutablePair> onDrawCorners;
	private final Runnable onDrawCornersMouseRelease;
	private final ClientPlayerEntity player;
	private final Object2ObjectAVLTreeMap<Position, ObjectArrayList<Platform>> flatPositionToPlatformsMap;
	private final Object2ObjectAVLTreeMap<Position, ObjectArrayList<Siding>> flatPositionToSidingsMap;

	private final Long2FloatAVLTreeMap tileOpacityValues = new Long2FloatAVLTreeMap();
	private final ObjectArraySet<Station> hoverStations = new ObjectArraySet<>();
	private final ObjectArraySet<Platform> hoverPlatforms = new ObjectArraySet<>();
	private final ObjectArraySet<Depot> hoverDepots = new ObjectArraySet<>();
	private final ObjectArraySet<Siding> hoverSidings = new ObjectArraySet<>();

	private final GuiAnimation guiAnimationX = new GuiAnimation(ANIMATION_DURATION);
	private final GuiAnimation guiAnimationY = new GuiAnimation(ANIMATION_DURATION);
	private final GuiAnimation guiAnimationScale = new GuiAnimation(ANIMATION_DURATION);

	private static final int PLAYER_ARROW_SIZE = 6;
	private static final int SCALE_UPPER_LIMIT = 64;
	private static final double SCALE_LOWER_LIMIT = 1 / 128D;
	private static final int AREA_NAME_PADDING = 3;
	private static final int AREA_SHADOW_RADIUS = 2;
	private static final int SAVED_RAIL_NAME_PADDING = 1;
	private static final int SAVED_RAIL_SHADOW_RADIUS = 1;
	private static final int HOVER_WINDOW_SHADOW_RADIUS = 8;
	private static final float DARKEN_MAP = 0.8F;
	private static final int ANIMATION_DURATION = 1000;

	public MapWidget(TransportMode transportMode, BiConsumer<NameColorDataBase, DeleteDataRequest> onDeleteArea, BiConsumer<IntIntImmutablePair, IntIntImmutablePair> onDrawCorners, Runnable onDrawCornersMouseRelease) {
		super(0, 0, 0, 0, Text.empty());
		this.transportMode = transportMode;
		this.onDeleteArea = onDeleteArea;
		this.onDrawCorners = onDrawCorners;
		this.onDrawCornersMouseRelease = onDrawCornersMouseRelease;

		player = MinecraftClient.getInstance().player;
		if (player == null) {
			guiAnimationX.setValue(0);
			guiAnimationY.setValue(0);
		} else {
			guiAnimationX.setValue(player.getX());
			guiAnimationY.setValue(player.getZ());
		}

		guiAnimationScale.setValue(1);
		setShowStations(true);

		flatPositionToPlatformsMap = MinecraftClientData.getFlatPositionToSavedRails(MinecraftClientData.getDashboardInstance().platforms, transportMode);
		flatPositionToSidingsMap = MinecraftClientData.getFlatPositionToSavedRails(MinecraftClientData.getDashboardInstance().sidings, transportMode);
	}

	@Override
	protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
		// Draw layers (z):
		// 0: Background
		// 1: Map tile
		// 2: Areas and all shadows
		// 3: Saved rails
		// 4: Overlay text
		// 5: Player position indicator
		// 6: Hover popup

		// Background
		context.fill(getX(), getY(), getX() + width, getY() + height, GuiHelper.BLACK_COLOR);

		final int newMouseX = active ? mouseX : -1;
		final int newMouseY = active ? mouseY : -1;
		context.enableScissor(getX(), getY(), getX() + width, getY() + height);
		guiAnimationX.tick();
		guiAnimationY.tick();
		guiAnimationScale.tick();

		// World map
		final MapTileProvider mapTileProvider = MTRClient.getMapTileProvider();
		if (mapTileProvider != null) {
			final double tileSize = guiAnimationScale.getCurrentValue() * MapTileProvider.TILE_SIZE;
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
									new Matrix4f(RenderSystem.getModelViewMatrix()).translate(newX, newY, 0).scale((float) guiAnimationScale.getCurrentValue(), (float) guiAnimationScale.getCurrentValue(), 1).translate(offsetX, offsetY, 1),
									RenderSystem.getProjectionMatrix(),
									RenderSystem.getShader()
							);
						});
					}
				}
			}

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
			drawSavedRails(drawing, flatPositionToPlatformsMap, popupDetails == null && (mapState == MapState.DEFAULT || mapState == MapState.EDITING_ROUTE) ? hoverPlatforms : null, newMouseX, newMouseY);
		}
		if (showDepots) {
			drawSavedRails(drawing, flatPositionToSidingsMap, popupDetails == null && mapState == MapState.DEFAULT ? hoverSidings : null, newMouseX, newMouseY);
		}

		// Stations and depots
		final boolean canHoverAreas = popupDetails == null && mapState == MapState.DEFAULT && hoverPlatforms.isEmpty() && hoverSidings.isEmpty();
		if (showStations) {
			drawAreas(drawing, MinecraftClientData.getDashboardInstance().stations, canHoverAreas ? hoverStations : null, newMouseX, newMouseY);
		}
		if (showDepots) {
			drawAreas(drawing, MinecraftClientData.getDashboardInstance().depots, canHoverAreas ? hoverDepots : null, newMouseX, newMouseY);
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
		if (popupDetails != null) {
			final ScrollableListWidget<?> scrollableListWidget = popupDetails.left();
			final IntIntImmutablePair popupMousePosition = popupDetails.right();
			final int leftBound = getX() + HOVER_WINDOW_SHADOW_RADIUS;
			final int rightBound = getX() + width - HOVER_WINDOW_SHADOW_RADIUS;
			final int topBound = getY() + HOVER_WINDOW_SHADOW_RADIUS;
			final int bottomBound = getY() + height - HOVER_WINDOW_SHADOW_RADIUS;
			scrollableListWidget.setX(Math.clamp(popupMousePosition.leftInt() - scrollableListWidget.getWidth() / 2, leftBound, rightBound - scrollableListWidget.getWidth()));
			scrollableListWidget.setY(Math.clamp(popupMousePosition.rightInt() - scrollableListWidget.getHeight() / 2, topBound, bottomBound - scrollableListWidget.getHeight()));

			drawing.setVerticesWH(scrollableListWidget.getX(), scrollableListWidget.getY(), scrollableListWidget.getWidth(), scrollableListWidget.getHeight(), 6).setColor(GuiHelper.BACKGROUND_COLOR).draw();
			GuiHelper.drawShadowWH(drawing, scrollableListWidget.getX(), scrollableListWidget.getY(), scrollableListWidget.getWidth(), scrollableListWidget.getHeight(), 2, HOVER_WINDOW_SHADOW_RADIUS, 2);

			matrixStack.push();
			matrixStack.translate(0, 0, 6);
			scrollableListWidget.renderWidget(context, newMouseX, newMouseY, delta);
			matrixStack.pop();

			if (!Utilities.isBetween(
					Utilities.clamp(newMouseX, leftBound, rightBound - 1),
					scrollableListWidget.getX() - 1, scrollableListWidget.getX() + scrollableListWidget.getWidth()
			) || !Utilities.isBetween(
					Utilities.clamp(newMouseY, topBound, bottomBound - 1),
					scrollableListWidget.getY() - 1, scrollableListWidget.getY() + scrollableListWidget.getHeight()
			)) {
				popupDetails = null;
			}
		}

		context.disableScissor();
	}

	@Override
	protected void onDrag(double mouseX, double mouseY, double deltaX, double deltaY) {
		if (popupDetails == null) {
			if (mapState == MapState.EDITING_AREA) {
				drawArea2 = coordsToWorldPos((int) Math.round(mouseX - getX()), (int) Math.round(mouseY - getY()));
				onDrawCorners.accept(drawArea1, drawArea2);
			} else {
				guiAnimationX.setValue(guiAnimationX.getCurrentValue() - deltaX / guiAnimationScale.getCurrentValue());
				guiAnimationY.setValue(guiAnimationY.getCurrentValue() - deltaY / guiAnimationScale.getCurrentValue());
			}
		} else {
			popupDetails.left().mouseDragged(mouseX, mouseY, 0, deltaX, deltaY);
		}
	}

	@Override
	public void onRelease(double mouseX, double mouseY) {
		if (mapState == MapState.EDITING_AREA) {
			onDrawCornersMouseRelease.run();
			drawArea1 = null;
			drawArea2 = null;
		}

		if (popupDetails == null) {
			if (!hoverStations.isEmpty()) {
				final ScrollableListWidget<Station> scrollableListWidget = createPopup(mouseX, mouseY);
				ScrollableListWidget.setAreas(scrollableListWidget, hoverStations, ObjectArrayList.of(
						new ObjectObjectImmutablePair<>(Identifier.of("textures/gui/sprites/mtr/icon_edit.png"), station -> System.out.println("editing " + station.getName())),
						new ObjectObjectImmutablePair<>(Identifier.of("textures/gui/sprites/mtr/icon_delete.png"), station -> onDeleteArea.accept(station, new DeleteDataRequest().addStationId(station.getId())))
				));
			}
			if (!hoverPlatforms.isEmpty()) {
				final ScrollableListWidget<Platform> scrollableListWidget = createPopup(mouseX, mouseY);
				ScrollableListWidget.setSavedRails(scrollableListWidget, hoverPlatforms, ObjectArrayList.of(
						new ObjectObjectImmutablePair<>(Identifier.of("textures/gui/sprites/mtr/icon_edit.png"), platform -> System.out.println("editing " + platform.getName()))
				));
			}
			if (!hoverDepots.isEmpty()) {
				final ScrollableListWidget<Depot> scrollableListWidget = createPopup(mouseX, mouseY);
				ScrollableListWidget.setAreas(scrollableListWidget, hoverDepots, ObjectArrayList.of(
						new ObjectObjectImmutablePair<>(Identifier.of("textures/gui/sprites/mtr/icon_edit.png"), depot -> System.out.println("editing " + depot.getName())),
						new ObjectObjectImmutablePair<>(Identifier.of("textures/gui/sprites/mtr/icon_delete.png"), depot -> onDeleteArea.accept(depot, new DeleteDataRequest().addDepotId(depot.getId())))
				));
			}
			if (!hoverSidings.isEmpty()) {
				final ScrollableListWidget<Siding> scrollableListWidget = createPopup(mouseX, mouseY);
				ScrollableListWidget.setSavedRails(scrollableListWidget, hoverSidings, ObjectArrayList.of(
						new ObjectObjectImmutablePair<>(Identifier.of("textures/gui/sprites/mtr/icon_edit.png"), siding -> System.out.println("editing " + siding.getName()))
				));
			}
		} else {
			popupDetails.left().onRelease(mouseX, mouseY);
		}
	}

	@Override
	public void onClick(double mouseX, double mouseY) {
		if (popupDetails == null) {
			if (isMouseOver(mouseX, mouseY) && MinecraftClientData.hasPermission() && mapState == MapState.EDITING_AREA) {
				drawArea1 = coordsToWorldPos((int) (mouseX - getX()), (int) (mouseY - getY()));
				drawArea2 = null;
			}
		} else {
			popupDetails.left().onClick(mouseX, mouseY);
		}
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
		if (active) {
			if (popupDetails == null) {
				final double oldScale = guiAnimationScale.getCurrentValue();
				if (oldScale > SCALE_LOWER_LIMIT && verticalAmount < 0) {
					guiAnimationX.setValue(guiAnimationX.getCurrentValue() - (mouseX - getX() - width / 2D) / guiAnimationScale.getCurrentValue());
					guiAnimationY.setValue(guiAnimationY.getCurrentValue() - (mouseY - getY() - height / 2D) / guiAnimationScale.getCurrentValue());
				}
				scale(verticalAmount);
				if (oldScale < SCALE_UPPER_LIMIT && verticalAmount > 0) {
					guiAnimationX.setValue(guiAnimationX.getCurrentValue() + (mouseX - getX() - width / 2D) / guiAnimationScale.getCurrentValue());
					guiAnimationY.setValue(guiAnimationY.getCurrentValue() + (mouseY - getY() - height / 2D) / guiAnimationScale.getCurrentValue());
				}
			} else {
				popupDetails.left().mouseScrolled(mouseX, mouseY, 0, verticalAmount);
			}

			return true;
		} else {
			return false;
		}
	}

	@Override
	public void setFocused(boolean focused) {
	}

	@Override
	public boolean isFocused() {
		return false;
	}

	@Override
	protected boolean isValidClickButton(int button) {
		return active && super.isValidClickButton(button);
	}

	@Override
	protected void appendClickableNarrations(NarrationMessageBuilder builder) {
	}

	public void scale(double amount) {
		guiAnimationScale.setValue(Utilities.clamp(guiAnimationScale.getCurrentValue() * Math.pow(2, amount), SCALE_LOWER_LIMIT, SCALE_UPPER_LIMIT));
	}

	public <T extends AreaBase<T, U>, U extends SavedRailBase<U, T>> void find(T savedArea) {
		guiAnimationX.animate((savedArea.getMinX() + savedArea.getMaxX() + 1) / 2F);
		guiAnimationY.animate((savedArea.getMinZ() + savedArea.getMaxZ() + 1) / 2F);
		final double scaleX = Math.max(1F, width - GuiHelper.DEFAULT_LINE_SIZE) / (savedArea.getMaxX() - savedArea.getMinX() + 1);
		final double scaleY = Math.max(1F, height - GuiHelper.DEFAULT_LINE_SIZE) / (savedArea.getMaxZ() - savedArea.getMinZ() + 1);
		guiAnimationScale.animate(Utilities.clamp(Math.min(scaleX, scaleY), SCALE_LOWER_LIMIT, SCALE_UPPER_LIMIT));
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

	public void setShowStations(boolean showStations) {
		this.showStations = showStations;
		this.showDepots = !showStations;
	}

	private <T extends SavedRailBase<T, U>, U extends AreaBase<U, T>> void drawAreas(Drawing drawing, ObjectArraySet<U> areas, @Nullable ObjectArraySet<U> hoverDataList, int mouseX, int mouseY) {
		areas.forEach(area -> {
			if (area.isTransportMode(transportMode) && AreaBase.validCorners(area)) {
				final double areaWidth = (area.getMaxX() + 1 - area.getMinX()) * guiAnimationScale.getCurrentValue();
				final double areaHeight = (area.getMaxZ() + 1 - area.getMinZ()) * guiAnimationScale.getCurrentValue();
				final double shadowRadius = AREA_SHADOW_RADIUS * guiAnimationScale.getCurrentValue();

				drawFromWorldCoords((area.getMinX() + area.getMaxX() + 1) / 2F, (area.getMinZ() + area.getMaxZ() + 1) / 2F, areaWidth / 2 + shadowRadius, areaHeight / 2 + shadowRadius, (x, y) -> {
					final double x1 = getX() + x - areaWidth / 2;
					final double x2 = x1 + areaWidth;
					final double y1 = getY() + y - areaHeight / 2;
					final double y2 = y1 + areaHeight;

					// Check for hover
					final double borderSize;
					if (hoverDataList != null && Utilities.isBetween(mouseX, x1, x2 - 1) && Utilities.isBetween(mouseY, y1, y2 - 1)) {
						hoverDataList.add(area);
						borderSize = AREA_NAME_PADDING / 2F;
					} else {
						borderSize = 1 / Math.min(2, MinecraftClient.getInstance().getWindow().getScaleFactor());
					}

					// Draw area
					final Color color = new Color(area.getColor());
					drawing.setVertices(x1, y1, x2, y2, 2).setColorARGB(0x66, (int) (0.8 * color.getRed()), (int) (0.8 * color.getGreen()), (int) (0.8 * color.getBlue())).draw();
					GuiHelper.drawShadow(drawing, x1, y1, x2, y2, 2, shadowRadius, 1);

					// Draw border
					drawing.setVertices(x1, y1, x1 + borderSize, y2, 2).setColor(ColorHelper.fullAlpha(area.getColor())).draw();
					drawing.setVertices(x2 - borderSize, y1, x2, y2, 2).setColor(ColorHelper.fullAlpha(area.getColor())).draw();
					drawing.setVertices(x1, y1, x2, y1 + borderSize, 2).setColor(ColorHelper.fullAlpha(area.getColor())).draw();
					drawing.setVertices(x1, y2 - borderSize, x2, y2, 2).setColor(ColorHelper.fullAlpha(area.getColor())).draw();

					// Draw overlay text
					final double clampedAreaWidth = areaWidth - Math.max(0, getX() - x1) - Math.max(0, x2 - getX() - width) - AREA_NAME_PADDING * 2;
					final double clampedAreaHeight = areaHeight - Math.max(0, getY() - y1) - Math.max(0, y2 - getY() - height) - AREA_NAME_PADDING * 2;
					if (clampedAreaWidth > 0 && clampedAreaHeight > 0) {
						FontGroups.renderMTR(drawing, area.getName(), FontRenderOptions.builder()
								.horizontalSpace((float) clampedAreaWidth)
								.verticalSpace((float) clampedAreaHeight)
								.offsetX((float) Math.max(getX(), x1) + AREA_NAME_PADDING)
								.offsetY((float) Math.max(getY(), y1) + AREA_NAME_PADDING)
								.offsetZ(4)
								.lineBreak(FontRenderOptions.LineBreak.FORCE_ONE_LINE)
								.textOverflow(FontRenderOptions.TextOverflow.SCALE)
								.build()
						);
					}
				});
			}
		});
	}

	private <T extends SavedRailBase<T, U>, U extends AreaBase<U, T>> void drawSavedRails(Drawing drawing, Object2ObjectAVLTreeMap<Position, ObjectArrayList<T>> flatPositionToSavedRailsMap, @Nullable ObjectArraySet<T> hoverDataList, int mouseX, int mouseY) {
		flatPositionToSavedRailsMap.forEach((position, savedRails) -> drawFromWorldCoords(position.getX() + 0.5, position.getZ() + 0.5, guiAnimationScale.getCurrentValue() / 2, guiAnimationScale.getCurrentValue() / 2, (x, y) -> {
			final double x1 = getX() + x - guiAnimationScale.getCurrentValue() / 2;
			final double y1 = getY() + y - guiAnimationScale.getCurrentValue() / 2;
			final double x2 = x1 + guiAnimationScale.getCurrentValue();
			final double y2 = y1 + guiAnimationScale.getCurrentValue();

			// Check for hover
			if (hoverDataList != null && hoverDataList.isEmpty() && Utilities.isBetween(mouseX, x1, x2 - 1) && Utilities.isBetween(mouseY, y1, y2 - 1)) {
				hoverDataList.addAll(savedRails);
			}

			// Draw saved rail
			drawing.setVertices(x1, y1, x2, y2, 3).setColor(IGui.ARGB_WHITE).draw();
			GuiHelper.drawShadow(drawing, x1, y1, x2, y2, 2, SAVED_RAIL_SHADOW_RADIUS * guiAnimationScale.getCurrentValue(), 1);

			// Draw overlay text
			if (guiAnimationScale.getCurrentValue() > SAVED_RAIL_NAME_PADDING * 2) {
				FontGroups.renderMTR(drawing, savedRails.stream().map(NameColorDataBase::getName).collect(Collectors.joining("|")), FontRenderOptions.builder()
						.horizontalSpace((float) guiAnimationScale.getCurrentValue() - SAVED_RAIL_NAME_PADDING * 2)
						.verticalSpace((float) guiAnimationScale.getCurrentValue() - SAVED_RAIL_NAME_PADDING * 2)
						.horizontalTextAlignment(FontRenderOptions.Alignment.CENTER)
						.verticalTextAlignment(FontRenderOptions.Alignment.CENTER)
						.offsetX((float) x1 + SAVED_RAIL_NAME_PADDING)
						.offsetY((float) y1 + SAVED_RAIL_NAME_PADDING)
						.offsetZ(4)
						.textOverflow(FontRenderOptions.TextOverflow.SCALE)
						.color(GuiHelper.BLACK_COLOR)
						.build()
				);
			}
		}));
	}

	private <T extends NameColorDataBase> ScrollableListWidget<T> createPopup(double mouseX, double mouseY) {
		final ScrollableListWidget<T> scrollableListWidget = new ScrollableListWidget<>();
		scrollableListWidget.setBounds(0, 0, width - HOVER_WINDOW_SHADOW_RADIUS * 2, height - HOVER_WINDOW_SHADOW_RADIUS * 2);
		popupDetails = new ObjectObjectImmutablePair<>(scrollableListWidget, new IntIntImmutablePair((int) Math.round(mouseX), (int) Math.round(mouseY)));
		return scrollableListWidget;
	}

	private IntIntImmutablePair coordsToWorldPos(int mouseX, int mouseY) {
		final DoubleDoubleImmutablePair worldPos = coordsToWorldPos((double) mouseX, mouseY);
		return new IntIntImmutablePair((int) Math.floor(worldPos.leftDouble()), (int) Math.floor(worldPos.rightDouble()));
	}

	private DoubleDoubleImmutablePair coordsToWorldPos(double mouseX, double mouseY) {
		final double left = (mouseX - width / 2D) / guiAnimationScale.getCurrentValue() + guiAnimationX.getCurrentValue();
		final double right = (mouseY - height / 2D) / guiAnimationScale.getCurrentValue() + guiAnimationY.getCurrentValue();
		return new DoubleDoubleImmutablePair(left, right);
	}

	private void drawFromWorldCoords(double worldX, double worldZ, double xPadding, double yPadding, BiConsumer<Double, Double> callback) {
		final double coordsX = (worldX - guiAnimationX.getCurrentValue()) * guiAnimationScale.getCurrentValue() + width / 2D;
		final double coordsY = (worldZ - guiAnimationY.getCurrentValue()) * guiAnimationScale.getCurrentValue() + height / 2D;
		if (Utilities.isBetween(coordsX, -xPadding, width + xPadding) && Utilities.isBetween(coordsY, -yPadding, height + yPadding)) {
			callback.accept(coordsX, coordsY);
		}
	}

	private static int clampTileSize(double value) {
		return (int) Math.floor(value / MapTileProvider.TILE_SIZE) * MapTileProvider.TILE_SIZE;
	}

	private enum MapState {DEFAULT, EDITING_AREA, EDITING_ROUTE}
}
