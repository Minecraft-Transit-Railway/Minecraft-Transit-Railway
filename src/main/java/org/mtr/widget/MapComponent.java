package org.mtr.widget;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexBuffer;
import gg.essential.elementa.UIComponent;
import gg.essential.elementa.components.UIBlock;
import gg.essential.elementa.constraints.CoerceAtMostConstraint;
import gg.essential.elementa.constraints.PixelConstraint;
import gg.essential.elementa.constraints.RelativeConstraint;
import gg.essential.elementa.constraints.SubtractiveConstraint;
import gg.essential.universal.UMatrixStack;
import gg.essential.universal.UMinecraft;
import kotlin.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import org.jspecify.annotations.Nullable;
import org.mtr.MTR;
import org.mtr.MTRClient;
import org.mtr.client.IDrawing;
import org.mtr.client.MinecraftClientData;
import org.mtr.core.data.*;
import org.mtr.core.operation.DeleteDataRequest;
import org.mtr.core.tool.Utilities;
import org.mtr.data.IGui;
import org.mtr.font.FontRenderHelper;
import org.mtr.font.FontRenderOptions;
import org.mtr.libraries.it.unimi.dsi.fastutil.doubles.DoubleDoubleImmutablePair;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.Long2FloatAVLTreeMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectAVLTreeMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArraySet;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import org.mtr.map.MapTileProvider;
import org.mtr.registry.UConverters;
import org.mtr.screen.*;
import org.mtr.tool.Drawing;
import org.mtr.tool.GuiAnimation;
import org.mtr.tool.GuiHelper;

import java.awt.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public final class MapComponent extends UIComponent {

	@Nullable
	private SimpleAreaBase editingArea;
	@Nullable
	private Route editingRoute;
	private double lastClickedX;
	private double lastClickedY;
	private double lastMouseX;
	private double lastMouseY;
	private double lastTickedMillis;

	private boolean showStations;
	private boolean showDepots;
	private boolean showPopup;
	private boolean canDrag;

	private final TransportMode transportMode;
	private final boolean hasPermission = MinecraftClientData.hasPermission();
	@Nullable
	private final LocalPlayer player;
	private final Object2ObjectAVLTreeMap<Position, ObjectArrayList<Platform>> flatPositionToPlatformsMap;
	private final Object2ObjectAVLTreeMap<Position, ObjectArrayList<Siding>> flatPositionToSidingsMap;

	private final Long2FloatAVLTreeMap tileOpacityValues = new Long2FloatAVLTreeMap();
	private final ObjectArraySet<SimpleAreaBase> hoverStationsHomesLandmarks = new ObjectArraySet<>();
	private final ObjectArraySet<Platform> hoverPlatforms = new ObjectArraySet<>();
	private final ObjectArraySet<Depot> hoverDepots = new ObjectArraySet<>();
	private final ObjectArraySet<Siding> hoverSidings = new ObjectArraySet<>();

	private final GuiAnimation guiAnimationX = new GuiAnimation();
	private final GuiAnimation guiAnimationY = new GuiAnimation();
	private final GuiAnimation guiAnimationScale = new GuiAnimation();

	private final UIBlock stationsHomesLandmarksPopupBlock;
	private final UIBlock platformsPopupBlock;
	private final UIBlock depotsPopupBlock;
	private final UIBlock sidingsPopupBlock;

	private final ListComponent<SimpleAreaBase> stationsHomesLandmarksPopupListComponent;
	private final ListComponent<Platform> platformsPopupListComponent;
	private final ListComponent<Depot> depotsPopupListComponent;
	private final ListComponent<Siding> sidingsPopupListComponent;

	private static final int PLAYER_ARROW_SIZE = 6;
	private static final int POPUP_MAX_WIDTH = 128;
	private static final int POPUP_MAX_HEIGHT = 96;
	private static final int SCALE_UPPER_LIMIT = 64;
	private static final double SCALE_LOWER_LIMIT = 1 / 128D;
	private static final int AREA_NAME_PADDING = 3;
	private static final int AREA_SHADOW_RADIUS = 2;
	private static final int SAVED_RAIL_NAME_PADDING = 1;
	private static final int SAVED_RAIL_SHADOW_RADIUS = 1;
	private static final int HOVER_WINDOW_SHADOW_RADIUS = 8;
	private static final float DARKEN_MAP = 0.8F;
	private static final int ANIMATION_DURATION = 1000;
	private static final int ANIMATION_DURATION_FAST = 200;
	private static final int DRAG_TIMEOUT_MILLIS = 2000;

	public MapComponent(DashboardScreen dashboardScreen, TransportMode transportMode, Consumer<SimpleAreaBase> onStartEditingArea, BiConsumer<NameColorDataBase, DeleteDataRequest> onDeleteData) {
		this.transportMode = transportMode;

		stationsHomesLandmarksPopupBlock = createPopupBlock();
		platformsPopupBlock = createPopupBlock();
		depotsPopupBlock = createPopupBlock();
		sidingsPopupBlock = createPopupBlock();

		stationsHomesLandmarksPopupListComponent = createPopupListComponent(stationsHomesLandmarksPopupBlock);
		platformsPopupListComponent = createPopupListComponent(platformsPopupBlock);
		depotsPopupListComponent = createPopupListComponent(depotsPopupBlock);
		sidingsPopupListComponent = createPopupListComponent(sidingsPopupBlock);

		player = Minecraft.getInstance().player;
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

		onMouseScrollConsumer(scrollEvent -> {
			final float width = getWidth();
			final float height = getHeight();
			final float left = getLeft();
			final float top = getTop();
			final Pair<Float, Float> mousePosition = getMousePosition();
			final float mouseX = mousePosition.getFirst();
			final float mouseY = mousePosition.getSecond();
			final double verticalAmount = scrollEvent.getScrollY();

			if (!showPopup) {
				final double oldScale = guiAnimationScale.getCurrentValue();
				if (oldScale > SCALE_LOWER_LIMIT && verticalAmount < 0) {
					guiAnimationX.setValue(guiAnimationX.getCurrentValue() - (mouseX - left - width / 2D) / guiAnimationScale.getCurrentValue());
					guiAnimationY.setValue(guiAnimationY.getCurrentValue() - (mouseY - top - height / 2D) / guiAnimationScale.getCurrentValue());
				}
				scale(verticalAmount, true);
				if (oldScale < SCALE_UPPER_LIMIT && verticalAmount > 0) {
					guiAnimationX.setValue(guiAnimationX.getCurrentValue() + (mouseX - left - width / 2D) / guiAnimationScale.getCurrentValue());
					guiAnimationY.setValue(guiAnimationY.getCurrentValue() + (mouseY - top - height / 2D) / guiAnimationScale.getCurrentValue());
				}
			}
		});

		onMouseDragConsumer((relativeMouseX, relativeMouseY, mouseButton) -> {
			final Pair<Float, Float> mousePosition = getMousePosition();
			final float mouseX = mousePosition.getFirst();
			final float mouseY = mousePosition.getSecond();

			if (!showPopup && canDrag) {
				if (editingArea == null) {
					guiAnimationX.setValue(guiAnimationX.getCurrentValue() - (mouseX - lastMouseX) / guiAnimationScale.getCurrentValue());
					guiAnimationY.setValue(guiAnimationY.getCurrentValue() - (mouseY - lastMouseY) / guiAnimationScale.getCurrentValue());
				} else {
					final float left = getLeft();
					final float top = getTop();
					final DoubleDoubleImmutablePair worldPos1 = coordsToWorldPos(lastClickedX - left, lastClickedY - top);
					final DoubleDoubleImmutablePair worldPos2 = coordsToWorldPos(mouseX - left, mouseY - top);
					editingArea.setCorners(
						new Position((int) Math.floor(worldPos1.leftDouble()), editingArea.getMinY(), (int) Math.floor(worldPos1.rightDouble())),
						new Position((int) Math.floor(worldPos2.leftDouble()), editingArea.getMaxY(), (int) Math.floor(worldPos2.rightDouble()))
					);
				}
			}
		});

		onMouseClickConsumer(clickEvent -> {
			final Pair<Float, Float> mousePosition = getMousePosition();
			lastClickedX = mousePosition.getFirst();
			lastClickedY = mousePosition.getSecond();
			canDrag = true;
		});

		onMouseReleaseRunnable(() -> {
			final float left = getLeft();
			final float top = getTop();
			final Pair<Float, Float> mousePosition = getMousePosition();
			final float mouseX = mousePosition.getFirst();
			final float mouseY = mousePosition.getSecond();

			if (!showPopup) {
				if (Math.abs(lastClickedX - mouseX) < 1 && Math.abs(lastClickedY - mouseY) < 1) {
					if (!hoverStationsHomesLandmarks.isEmpty()) {
						showPopup(stationsHomesLandmarksPopupBlock, hoverStationsHomesLandmarks.size(), mouseX - left, mouseY - top);
						ListComponent.setAreas(stationsHomesLandmarksPopupListComponent, hoverStationsHomesLandmarks, null, hasPermission ? ObjectArrayList.of(
							new ObjectObjectImmutablePair<>(GuiHelper.SELECT_TEXTURE_ID, (indexList, area) -> onStartEditingArea.accept(area)),
							new ObjectObjectImmutablePair<>(GuiHelper.EDIT_TEXTURE_ID, (indexList, area) -> UMinecraft.setCurrentScreenObj(switch (area) {
								case Home home -> new HomeScreen(home, dashboardScreen);
								case Landmark landmark -> new LandmarkScreen(landmark, dashboardScreen);
								default -> new StationScreen((Station) area, dashboardScreen);
							})),
							new ObjectObjectImmutablePair<>(GuiHelper.DELETE_TEXTURE_ID, (indexList, area) -> onDeleteData.accept(area, switch (area) {
								case Home ignored -> new DeleteDataRequest().addHomeId(area.getId());
								case Landmark ignored -> new DeleteDataRequest().addLandmarkId(area.getId());
								default -> new DeleteDataRequest().addStationId(area.getId());
							}))
						) : new ObjectArrayList<>());
						stationsHomesLandmarksPopupListComponent.tryTrigger();
					}
					if (!hoverPlatforms.isEmpty()) {
						showPopup(platformsPopupBlock, hoverPlatforms.size(), mouseX - left, mouseY - top);
						ListComponent.setSavedRails(platformsPopupListComponent, hoverPlatforms, hasPermission ? editingRoute == null ? ObjectArrayList.of(
							new ObjectObjectImmutablePair<>(GuiHelper.EDIT_TEXTURE_ID, (indexList, platform) -> UMinecraft.setCurrentScreenObj(new PlatformScreen(platform, dashboardScreen)))
						) : ObjectArrayList.of(
							new ObjectObjectImmutablePair<>(GuiHelper.SELECT_TEXTURE_ID, (indexList, platform) -> {
								if (editingRoute != null) {
									final RoutePlatformData routePlatformData = new RoutePlatformData(platform.getId());
									if (Screen.hasShiftDown()) {
										editingRoute.getRoutePlatforms().addFirst(routePlatformData);
									} else {
										editingRoute.getRoutePlatforms().add(routePlatformData);
									}
									routePlatformData.writePlatformCache(editingRoute, MinecraftClientData.getDashboardInstance().platformIdMap);
								}
							})
						) : new ObjectArrayList<>());
						platformsPopupListComponent.tryTrigger();
					}
					if (!hoverDepots.isEmpty()) {
						showPopup(depotsPopupBlock, hoverDepots.size(), mouseX - left, mouseY - top);
						ListComponent.setAreas(depotsPopupListComponent, hoverDepots, null, hasPermission ? ObjectArrayList.of(
							new ObjectObjectImmutablePair<>(GuiHelper.SELECT_TEXTURE_ID, (indexList, depot) -> onStartEditingArea.accept(depot)),
							new ObjectObjectImmutablePair<>(GuiHelper.EDIT_TEXTURE_ID, (indexList, depot) -> UMinecraft.setCurrentScreenObj(new DepotScreen(depot, dashboardScreen))),
							new ObjectObjectImmutablePair<>(GuiHelper.DELETE_TEXTURE_ID, (indexList, depot) -> onDeleteData.accept(depot, new DeleteDataRequest().addDepotId(depot.getId())))
						) : new ObjectArrayList<>());
						depotsPopupListComponent.tryTrigger();
					}
					if (!hoverSidings.isEmpty()) {
						showPopup(sidingsPopupBlock, hoverSidings.size(), mouseX - left, mouseY - top);
						ListComponent.setSavedRails(sidingsPopupListComponent, hoverSidings, hasPermission ? ObjectArrayList.of(
							new ObjectObjectImmutablePair<>(GuiHelper.EDIT_TEXTURE_ID, (indexList, siding) -> UMinecraft.setCurrentScreenObj(new SidingScreen(siding, dashboardScreen)))
						) : new ObjectArrayList<>());
						sidingsPopupListComponent.tryTrigger();
					}
				}
			}

			canDrag = false;
		});
	}

	@Override
	public void draw(UMatrixStack uMatrixStack) {
		// Apply effects
		beforeDrawCompat(uMatrixStack);

		// Draw layers (z):
		// 0: Background
		// 1: Map tile
		// 2: Areas and all shadows
		// 3: Saved rails
		// 4: Overlay text
		// 5: Player position indicator
		// 6: Hover popup

		final float width = getWidth();
		final float height = getHeight();
		final float left = getLeft();
		final float top = getTop();
		final Pair<Float, Float> mousePosition = getMousePosition();
		final float mouseX = mousePosition.getFirst();
		final float mouseY = mousePosition.getSecond();
		final PoseStack matrixStack = UConverters.convert(uMatrixStack);
		final float delta = MTRClient.getGameTimeDeltaTicks();

		// Background
		new Drawing(matrixStack, RenderType.gui()).setVerticesWH(left, top, width, height).setColor(Color.BLACK).draw();

		guiAnimationX.tick();
		guiAnimationY.tick();
		guiAnimationScale.tick();
		applyScissor();

		// World map
		final MapTileProvider mapTileProvider = MTRClient.getMapTileProvider();
		if (mapTileProvider != null) {
			final double tileSize = guiAnimationScale.getCurrentValue() * MapTileProvider.TILE_SIZE;
			final DoubleDoubleImmutablePair topLeftWorldCoords = coordsToWorldPos(0D, 0D);
			final float offsetX = clampTileSize(topLeftWorldCoords.leftDouble()) - (float) topLeftWorldCoords.leftDouble();
			final float offsetY = clampTileSize(topLeftWorldCoords.rightDouble()) - (float) topLeftWorldCoords.rightDouble();
			RenderType.gui().setupRenderState();

			for (double x = 0; x < width + tileSize; x += tileSize) {
				for (double y = 0; y < height + tileSize; y += tileSize) {
					final DoubleDoubleImmutablePair worldCoords = coordsToWorldPos(x, y);
					final BlockPos tilePos = new BlockPos(clampTileSize(worldCoords.leftDouble()), player == null ? 0 : player.blockPosition().getY(), clampTileSize(worldCoords.rightDouble()));
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

						final float newX = (float) x + left;
						final float newY = (float) y + top;
						IDrawing.changeShaderColor(new Color(newOpacity * DARKEN_MAP, newOpacity * DARKEN_MAP, newOpacity * DARKEN_MAP, 1), () -> {
							vertexBuffer.bind();
							vertexBuffer.drawWithShader(
								new Matrix4f(RenderSystem.getModelViewMatrix()).translate(newX, newY, 0).scale((float) guiAnimationScale.getCurrentValue(), (float) guiAnimationScale.getCurrentValue(), 1).translate(offsetX, offsetY, 1),
								RenderSystem.getProjectionMatrix(),
								RenderSystem.getShader()
							);
						});
					}
				}
			}

			RenderType.gui().clearRenderState();
		}

		final ObjectArrayList<Consumer<PoseStack>> deferredRenders = new ObjectArrayList<>();

		// Player position indicator
		if (player != null) {
			drawFromWorldCoords(player.getX(), player.getZ(), PLAYER_ARROW_SIZE / 2F, PLAYER_ARROW_SIZE / 2F, (x, y) -> {
				matrixStack.pushPose();
				matrixStack.translate(left + x, top + y, 5);
				Drawing.rotateZDegrees(matrixStack, player.getYRot() + 180);
				new Drawing(matrixStack, GuiHelper.getGuiTexturedRenderType(ResourceLocation.fromNamespaceAndPath(MTR.MOD_ID, "textures/gui/dashboard_player_arrow.png")))
					.setVerticesWH(-PLAYER_ARROW_SIZE / 2F, -PLAYER_ARROW_SIZE / 2F, PLAYER_ARROW_SIZE, PLAYER_ARROW_SIZE)
					.setUv()
					.draw();
				matrixStack.popPose();
			});
		}

		final Drawing drawing = new Drawing(matrixStack, RenderType.gui()).setGuiBoundsWH(left, top, width, height);
		hoverStationsHomesLandmarks.clear();
		hoverPlatforms.clear();
		hoverDepots.clear();
		hoverSidings.clear();

		// Platforms and sidings
		if (showStations) {
			drawSavedRails(drawing, deferredRenders, flatPositionToPlatformsMap, !showPopup && editingArea == null ? hoverPlatforms : null, mouseX, mouseY);
		}
		if (showDepots) {
			drawSavedRails(drawing, deferredRenders, flatPositionToSidingsMap, !showPopup && editingArea == null && editingRoute == null ? hoverSidings : null, mouseX, mouseY);
		}

		// Stations, depots, homes, and landmarks
		final boolean canHoverAreas = !showPopup && editingArea == null && editingRoute == null && hoverPlatforms.isEmpty() && hoverSidings.isEmpty();
		if (showStations) {
			final ObjectArraySet<Station> stations = new ObjectArraySet<>(MinecraftClientData.getDashboardInstance().stations);
			if (editingArea != null && editingArea instanceof Station) {
				stations.add((Station) editingArea);
			}
			drawAreas(drawing, deferredRenders, stations, canHoverAreas ? hoverStationsHomesLandmarks : null, mouseX, mouseY);

			final ObjectArraySet<Home> homes = new ObjectArraySet<>(MinecraftClientData.getDashboardInstance().homes);
			if (editingArea != null && editingArea instanceof Home) {
				homes.add((Home) editingArea);
			}
			drawAreas(drawing, deferredRenders, homes, canHoverAreas ? hoverStationsHomesLandmarks : null, mouseX, mouseY);

			final ObjectArraySet<Landmark> landmarks = new ObjectArraySet<>(MinecraftClientData.getDashboardInstance().landmarks);
			if (editingArea != null && editingArea instanceof Landmark) {
				landmarks.add((Landmark) editingArea);
			}
			drawAreas(drawing, deferredRenders, landmarks, canHoverAreas ? hoverStationsHomesLandmarks : null, mouseX, mouseY);
		}
		if (showDepots) {
			final ObjectArraySet<Depot> depots = new ObjectArraySet<>(MinecraftClientData.getDashboardInstance().depots);
			if (editingArea != null && editingArea instanceof Depot) {
				depots.add((Depot) editingArea);
			}
			drawAreas(drawing, deferredRenders, depots, canHoverAreas ? hoverDepots : null, mouseX, mouseY);
		}

		// Hover popup
		if (showPopup) {
			showPopup = checkPopupHover(stationsHomesLandmarksPopupBlock, drawing, mouseX, mouseY) ||
				checkPopupHover(platformsPopupBlock, drawing, mouseX, mouseY) ||
				checkPopupHover(depotsPopupBlock, drawing, mouseX, mouseY) ||
				checkPopupHover(sidingsPopupBlock, drawing, mouseX, mouseY);
		}

		deferredRenders.forEach(deferredRender -> deferredRender.accept(matrixStack));
		RenderSystem.disableScissor();
		lastMouseX = mouseX;
		lastMouseY = mouseY;

		// Temporary fix for drag persisting after opening a screen through popups
		final long millis = System.currentTimeMillis();
		if (millis - lastTickedMillis > DRAG_TIMEOUT_MILLIS) {
			canDrag = false;
		}
		lastTickedMillis = millis;

		uMatrixStack.push();
		uMatrixStack.translate(0, 0, 6);
		super.draw(uMatrixStack);
		uMatrixStack.pop();
	}

	public void scale(double amount) {
		scale(amount, false);
	}

	public <T extends SimpleAreaBase> void find(T savedArea) {
		guiAnimationX.animate((savedArea.getMinX() + savedArea.getMaxX() + 1) / 2F, ANIMATION_DURATION);
		guiAnimationY.animate((savedArea.getMinZ() + savedArea.getMaxZ() + 1) / 2F, ANIMATION_DURATION);
		final double scaleX = Math.max(1F, getWidth() - GuiHelper.DEFAULT_LINE_SIZE) / (savedArea.getMaxX() - savedArea.getMinX() + 1);
		final double scaleY = Math.max(1F, getHeight() - GuiHelper.DEFAULT_LINE_SIZE) / (savedArea.getMaxZ() - savedArea.getMinZ() + 1);
		guiAnimationScale.animate(Math.clamp(Math.min(scaleX, scaleY), SCALE_LOWER_LIMIT, SCALE_UPPER_LIMIT), ANIMATION_DURATION);
	}

	public void startEditingArea(SimpleAreaBase editingArea) {
		this.editingArea = editingArea;
	}

	public void startEditingRoute(Route editingRoute) {
		this.editingRoute = editingRoute;
	}

	public void stopEditing() {
		editingArea = null;
		editingRoute = null;
	}

	public void setShowStations(boolean showStations) {
		this.showStations = showStations;
		this.showDepots = !showStations;
	}

	private void scale(double amount, boolean instant) {
		final double scale = Math.clamp(guiAnimationScale.getCurrentValue() * Math.pow(2, amount), SCALE_LOWER_LIMIT, SCALE_UPPER_LIMIT);
		if (instant) {
			guiAnimationScale.setValue(scale);
		} else {
			guiAnimationScale.animate(scale, ANIMATION_DURATION_FAST);
		}
	}

	private <T extends SimpleAreaBase, U extends T> void drawAreas(Drawing drawing, ObjectArrayList<Consumer<PoseStack>> deferredRenders, ObjectArraySet<U> areas, @Nullable ObjectArraySet<T> hoverDataList, float mouseX, float mouseY) {
		final float width = getWidth();
		final float height = getHeight();
		final float left = getLeft();
		final float top = getTop();

		areas.forEach(area -> {
			if (area.isTransportMode(transportMode) && AreaBase.validCorners(area)) {
				final double areaWidth = (area.getMaxX() + 1 - area.getMinX()) * guiAnimationScale.getCurrentValue();
				final double areaHeight = (area.getMaxZ() + 1 - area.getMinZ()) * guiAnimationScale.getCurrentValue();
				final double shadowRadius = AREA_SHADOW_RADIUS * guiAnimationScale.getCurrentValue();

				drawFromWorldCoords((area.getMinX() + area.getMaxX() + 1) / 2F, (area.getMinZ() + area.getMaxZ() + 1) / 2F, areaWidth / 2 + shadowRadius, areaHeight / 2 + shadowRadius, (x, y) -> {
					final double x1 = left + x - areaWidth / 2;
					final double x2 = x1 + areaWidth;
					final double y1 = top + y - areaHeight / 2;
					final double y2 = y1 + areaHeight;

					// Check for hover
					final double borderSize;
					final double minBorderSize = 1 / Math.min(2, Minecraft.getInstance().getWindow().getGuiScale());
					if (editingArea != null && area.getId() == editingArea.getId()) {
						borderSize = minBorderSize + (AREA_NAME_PADDING - minBorderSize) * (Math.sin(Math.PI * System.currentTimeMillis() / 1000) + 1) / 2;
					} else if (hoverDataList != null && Utilities.isBetween(mouseX, x1, x2) && Utilities.isBetween(mouseY, y1, y2)) {
						hoverDataList.add(area);
						borderSize = AREA_NAME_PADDING / 2F;
					} else {
						borderSize = minBorderSize;
					}

					// Draw area
					final Color color = new Color(area.getColor());
					drawing.setVertices(x1, y1, x2, y2, 2).setColorARGB(0x66, (int) (0.8 * color.getRed()), (int) (0.8 * color.getGreen()), (int) (0.8 * color.getBlue())).draw();
					GuiHelper.drawShadow(drawing, x1, y1, x2, y2, 2, shadowRadius, 1);

					// Draw border
					drawing.setVertices(x1, y1, x1 + borderSize, y2, 2).setColor((area.getColor() | 0xFF000000)).draw();
					drawing.setVertices(x2 - borderSize, y1, x2, y2, 2).setColor((area.getColor() | 0xFF000000)).draw();
					drawing.setVertices(x1, y1, x2, y1 + borderSize, 2).setColor((area.getColor() | 0xFF000000)).draw();
					drawing.setVertices(x1, y2 - borderSize, x2, y2, 2).setColor((area.getColor() | 0xFF000000)).draw();

					// Draw overlay text
					final double clampedAreaWidth = areaWidth - Math.max(0, left - x1) - Math.max(0, x2 - left - width) - AREA_NAME_PADDING * 2;
					final double clampedAreaHeight = areaHeight - Math.max(0, top - y1) - Math.max(0, y2 - top - height) - AREA_NAME_PADDING * 2;
					if (clampedAreaWidth > 0 && clampedAreaHeight > 0) {
						deferredRenders.add(matrixStack -> FontRenderHelper.render(matrixStack, area.getName(), FontRenderOptions.builder()
							.horizontalSpace((float) clampedAreaWidth)
							.verticalSpace((float) clampedAreaHeight)
							.offsetX((float) Math.max(left, x1) + AREA_NAME_PADDING)
							.offsetY((float) Math.max(top, y1) + AREA_NAME_PADDING)
							.offsetZ(4)
							.lineBreak(FontRenderOptions.LineBreak.FORCE_ONE_LINE)
							.textOverflow(FontRenderOptions.TextOverflow.SCALE)
							.build()
						));
					}
				});
			}
		});
	}

	private <T extends SavedRailBase<T, U>, U extends AreaBase<U, T>> void drawSavedRails(Drawing drawing, ObjectArrayList<Consumer<PoseStack>> deferredRenders, Object2ObjectAVLTreeMap<Position, ObjectArrayList<T>> flatPositionToSavedRailsMap, @Nullable ObjectArraySet<T> hoverDataList, float mouseX, float mouseY) {
		final float left = getLeft();
		final float top = getTop();

		flatPositionToSavedRailsMap.forEach((position, savedRails) -> drawFromWorldCoords(position.getX() + 0.5, position.getZ() + 0.5, guiAnimationScale.getCurrentValue() / 2, guiAnimationScale.getCurrentValue() / 2, (x, y) -> {
			final double x1 = left + x - guiAnimationScale.getCurrentValue() / 2;
			final double y1 = top + y - guiAnimationScale.getCurrentValue() / 2;
			final double x2 = x1 + guiAnimationScale.getCurrentValue();
			final double y2 = y1 + guiAnimationScale.getCurrentValue();

			// Check for hover
			if (hoverDataList != null && hoverDataList.isEmpty() && Utilities.isBetween(mouseX, x1, x2) && Utilities.isBetween(mouseY, y1, y2)) {
				hoverDataList.addAll(savedRails);
			}

			// Draw saved rail
			drawing.setVertices(x1, y1, x2, y2, 3).setColor(IGui.ARGB_WHITE).draw();
			GuiHelper.drawShadow(drawing, x1, y1, x2, y2, 2, SAVED_RAIL_SHADOW_RADIUS * guiAnimationScale.getCurrentValue(), 1);

			// Draw overlay text
			if (guiAnimationScale.getCurrentValue() > SAVED_RAIL_NAME_PADDING * 2) {
				deferredRenders.add(matrixStack -> FontRenderHelper.render(matrixStack, savedRails.stream().map(NameColorDataBase::getName).collect(Collectors.joining("|")), FontRenderOptions.builder()
					.horizontalSpace((float) guiAnimationScale.getCurrentValue() - SAVED_RAIL_NAME_PADDING * 2)
					.verticalSpace((float) guiAnimationScale.getCurrentValue() - SAVED_RAIL_NAME_PADDING * 2)
					.horizontalTextAlignment(FontRenderOptions.Alignment.CENTER)
					.verticalTextAlignment(FontRenderOptions.Alignment.CENTER)
					.offsetX((float) x1 + SAVED_RAIL_NAME_PADDING)
					.offsetY((float) y1 + SAVED_RAIL_NAME_PADDING)
					.offsetZ(4)
					.textOverflow(FontRenderOptions.TextOverflow.SCALE)
					.color(Color.BLACK)
					.build()
				));
			}
		}));
	}

	private UIBlock createPopupBlock() {
		final UIBlock popupBlock = (UIBlock) new UIBlock(new Color(GuiHelper.BACKGROUND_COLOR))
			.setChildOf(this)
			.setWidth(new CoerceAtMostConstraint(new SubtractiveConstraint(new RelativeConstraint(), new PixelConstraint(HOVER_WINDOW_SHADOW_RADIUS * 2)), new PixelConstraint(POPUP_MAX_WIDTH)));
		popupBlock.hide(true);
		return popupBlock;
	}

	private DoubleDoubleImmutablePair coordsToWorldPos(double mouseX, double mouseY) {
		final double left = (mouseX - getWidth() / 2D) / guiAnimationScale.getCurrentValue() + guiAnimationX.getCurrentValue();
		final double right = (mouseY - getHeight() / 2D) / guiAnimationScale.getCurrentValue() + guiAnimationY.getCurrentValue();
		return new DoubleDoubleImmutablePair(left, right);
	}

	private void drawFromWorldCoords(double worldX, double worldZ, double xPadding, double yPadding, BiConsumer<Double, Double> callback) {
		final float width = getWidth();
		final float height = getHeight();
		final double coordsX = (worldX - guiAnimationX.getCurrentValue()) * guiAnimationScale.getCurrentValue() + width / 2D;
		final double coordsY = (worldZ - guiAnimationY.getCurrentValue()) * guiAnimationScale.getCurrentValue() + height / 2D;
		if (Utilities.isBetween(coordsX, -xPadding, width + xPadding) && Utilities.isBetween(coordsY, -yPadding, height + yPadding)) {
			callback.accept(coordsX, coordsY);
		}
	}

	private void showPopup(UIBlock popupBlock, int childItems, float mouseX, float mouseY) {
		final float width = getWidth();
		final float height = getHeight();
		final float popupWidth = popupBlock.getWidth();
		final int popupHeight = Math.min(childItems * GuiHelper.DEFAULT_LINE_SIZE, POPUP_MAX_HEIGHT);
		popupBlock.setX(new PixelConstraint(Utilities.clampSafe(mouseX - popupWidth / 2, HOVER_WINDOW_SHADOW_RADIUS, width - HOVER_WINDOW_SHADOW_RADIUS - popupWidth)));
		popupBlock.setY(new PixelConstraint(Utilities.clampSafe(mouseY - popupHeight / 2F, HOVER_WINDOW_SHADOW_RADIUS, height - HOVER_WINDOW_SHADOW_RADIUS - popupHeight)));
		popupBlock.setHeight(new PixelConstraint(popupHeight));
		popupBlock.unhide(true);
		showPopup = true;
	}

	private void applyScissor() {
		final Window window = Minecraft.getInstance().getWindow();
		final double guiScale = window.getGuiScale();
		final double leftScaled = getLeft() * guiScale;
		final double bottomScaled = window.getHeight() - getBottom() * guiScale;
		final double widthScaled = getWidth() * guiScale;
		final double heightScaled = getHeight() * guiScale;
		RenderSystem.enableScissor((int) leftScaled, (int) bottomScaled, (int) widthScaled, (int) heightScaled);
	}

	private static <T extends NameColorDataBase> ListComponent<T> createPopupListComponent(UIBlock block) {
		final ScrollPanelComponent scrollPanelComponent = (ScrollPanelComponent) new ScrollPanelComponent(true)
			.setChildOf(block)
			.setWidth(new RelativeConstraint())
			.setHeight(new RelativeConstraint());

		final ListComponent<T> popupListComponent = new ListComponent<>();
		popupListComponent.setChildOf(scrollPanelComponent.contentContainer)
			.setWidth(new RelativeConstraint())
			.setHeight(new RelativeConstraint());

		return popupListComponent;
	}

	private static boolean checkPopupHover(UIBlock popupBlock, Drawing drawing, float mouseX, float mouseY) {
		final float width = popupBlock.getWidth();
		final float height = popupBlock.getHeight();
		final float left = popupBlock.getLeft();
		final float top = popupBlock.getTop();

		if (height > 0) {
			GuiHelper.drawShadowWH(drawing, left, top, width, height, 2, HOVER_WINDOW_SHADOW_RADIUS, 2);
		}

		if (!Utilities.isBetween(
			mouseX,
			left - HOVER_WINDOW_SHADOW_RADIUS, left + width + HOVER_WINDOW_SHADOW_RADIUS
		) || !Utilities.isBetween(
			mouseY,
			top - HOVER_WINDOW_SHADOW_RADIUS, top + height + HOVER_WINDOW_SHADOW_RADIUS
		)) {
			popupBlock.hide(true);
			popupBlock.setHeight(new PixelConstraint(0));
			return false;
		} else {
			return true;
		}
	}

	private static int clampTileSize(double value) {
		return (int) Math.floor(value / MapTileProvider.TILE_SIZE) * MapTileProvider.TILE_SIZE;
	}
}
