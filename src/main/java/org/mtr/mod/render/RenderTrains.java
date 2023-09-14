package org.mtr.mod.render;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.ObjectAVLTreeSet;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import org.mtr.core.data.InterchangeColorsForStationName;
import org.mtr.core.data.Rail;
import org.mtr.core.tools.Utilities;
import org.mtr.init.MTR;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.MinecraftClientHelper;
import org.mtr.mapping.mapper.PlayerHelper;
import org.mtr.mod.Init;
import org.mtr.mod.InitClient;
import org.mtr.mod.block.BlockNode;
import org.mtr.mod.block.BlockPlatform;
import org.mtr.mod.block.BlockSignalLightBase;
import org.mtr.mod.block.BlockSignalSemaphoreBase;
import org.mtr.mod.client.ClientData;
import org.mtr.mod.client.Config;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.client.ResourcePackCreatorProperties;
import org.mtr.mod.data.IGui;
import org.mtr.mod.data.RailType;
import org.mtr.mod.item.ItemNodeModifierBase;

import javax.annotation.Nullable;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class RenderTrains implements IGui {

	public static int maxTrainRenderDistance;
	public static ResourcePackCreatorProperties creatorProperties = new ResourcePackCreatorProperties();

	private static float lastRenderedTick;

	public static final int PLAYER_RENDER_OFFSET = 1000;

	public static final ObjectAVLTreeSet<String> AVAILABLE_TEXTURES = new ObjectAVLTreeSet<>();
	public static final ObjectAVLTreeSet<String> UNAVAILABLE_TEXTURES = new ObjectAVLTreeSet<>();

	public static final int DETAIL_RADIUS = 32;
	public static final int DETAIL_RADIUS_SQUARED = DETAIL_RADIUS * DETAIL_RADIUS;
	private static final int TOTAL_RENDER_STAGES = 2;
	private static final ObjectArrayList<ObjectArrayList<Object2ObjectArrayMap<Identifier, ObjectArraySet<Consumer<GraphicsHolder>>>>> RENDERS = new ObjectArrayList<>(TOTAL_RENDER_STAGES);
	private static final ObjectArrayList<ObjectArrayList<Object2ObjectArrayMap<Identifier, ObjectArraySet<Consumer<GraphicsHolder>>>>> CURRENT_RENDERS = new ObjectArrayList<>(TOTAL_RENDER_STAGES);

	static {
		for (int i = 0; i < TOTAL_RENDER_STAGES; i++) {
			final int renderStageCount = QueuedRenderLayer.values().length;
			final ObjectArrayList<Object2ObjectArrayMap<Identifier, ObjectArraySet<Consumer<GraphicsHolder>>>> rendersList = new ObjectArrayList<>(renderStageCount);
			final ObjectArrayList<Object2ObjectArrayMap<Identifier, ObjectArraySet<Consumer<GraphicsHolder>>>> currentRendersList = new ObjectArrayList<>(renderStageCount);

			for (int j = 0; j < renderStageCount; j++) {
				rendersList.add(j, new Object2ObjectArrayMap<>());
				currentRendersList.add(j, new Object2ObjectArrayMap<>());
			}

			RENDERS.add(i, rendersList);
			CURRENT_RENDERS.add(i, currentRendersList);
		}
	}

	public static void render(GraphicsHolder graphicsHolder) {
		// TODO

		final MinecraftClient minecraftClient = MinecraftClient.getInstance();
		final ClientWorld clientWorld = minecraftClient.getWorldMapped();
		final ClientPlayerEntity clientPlayerEntity = minecraftClient.getPlayerMapped();

		if (clientWorld == null || clientPlayerEntity == null) {
			return;
		}

		final boolean renderColors = isHoldingRailRelated(clientPlayerEntity);
		final int maxRailDistance = MinecraftClientHelper.getRenderDistance() * 16;

		ClientData.instance.positionToRailConnections.forEach((startPos, railMap) -> railMap.forEach((endPos, rail) -> {
			if (!Utilities.isBetween(clientPlayerEntity.getX(), startPos.getX(), endPos.getX(), maxRailDistance) || !Utilities.isBetween(clientPlayerEntity.getZ(), startPos.getZ(), endPos.getZ(), maxRailDistance)) {
				return;
			}

			switch (rail.getTransportMode()) {
				case TRAIN:
					renderRailStandard(clientWorld, rail, renderColors, 1);
					if (renderColors) {
//						renderSignalsStandard(clientWorld, matrices, vertexConsumers, rail, startPos, endPos);
					}
					break;
				case BOAT:
					if (renderColors) {
						renderRailStandard(clientWorld, rail, true, 0.5F);
//						renderSignalsStandard(clientWorld, matrices, vertexConsumers, rail, startPos, endPos);
					}
					break;
				case CABLE_CAR:
					if (rail.isSavedRail() || rail.getSpeedLimitKilometersPerHour() == RailType.CABLE_CAR_STATION.speedLimit) {
						renderRailStandard(clientWorld, rail, 0.25F + SMALL_OFFSET, renderColors, 0.25F, new Identifier(MTR.MOD_ID, "textures/block/metal.png"), 0.25F, 0, 0.75F, 1);
					}
					if (renderColors && !rail.isSavedRail()) {
						renderRailStandard(clientWorld, rail, 0.5F + SMALL_OFFSET, true, 1, null, 0, 0.75F, 1, 0.25F);
					}

					final Vector3d playerOffset = getPlayerOffset();
					graphicsHolder.createVertexConsumer(RenderLayer.getLines());
					rail.render((x1, z1, x2, z2, x3, z3, x4, z4, y1, y2) -> {
						graphicsHolder.drawLineInWorld(
								(float) (x1 - playerOffset.getXMapped()),
								(float) (y1 - playerOffset.getYMapped() + 0.5),
								(float) (z1 - playerOffset.getZMapped()),
								(float) (x3 - playerOffset.getXMapped()),
								(float) (y2 - playerOffset.getYMapped() + 0.5),
								(float) (z3 - playerOffset.getZMapped()),
								renderColors ? RailType.getRailColor(rail) : ARGB_BLACK
						);
					}, 0, 0);

					break;
				case AIRPLANE:
					if (renderColors) {
						renderRailStandard(clientWorld, rail, true, 1);
//						renderSignalsStandard(clientWorld, matrices, vertexConsumers, rail, startPos, endPos);
					} else {
						renderRailStandard(clientWorld, rail, 0.0625F + SMALL_OFFSET, false, 0.25F, new Identifier("textures/block/iron_block.png"), 0.25F, 0, 0.75F, 1);
					}
					break;
			}
		}));

		if (lastRenderedTick != InitClient.getGameTick()) {
			for (int i = 0; i < TOTAL_RENDER_STAGES; i++) {
				for (int j = 0; j < QueuedRenderLayer.values().length; j++) {
					CURRENT_RENDERS.get(i).get(j).clear();
					CURRENT_RENDERS.get(i).get(j).putAll(RENDERS.get(i).get(j));
					RENDERS.get(i).get(j).clear();
				}
			}
		}

		for (int i = 0; i < TOTAL_RENDER_STAGES; i++) {
			for (int j = 0; j < QueuedRenderLayer.values().length; j++) {
				final QueuedRenderLayer queuedRenderLayer = QueuedRenderLayer.values()[j];
				CURRENT_RENDERS.get(i).get(j).forEach((key, value) -> {
					final RenderLayer renderLayer;
					switch (queuedRenderLayer) {
						case LIGHT:
							renderLayer = MoreRenderLayers.getLight(key, false);
							break;
						case LIGHT_TRANSLUCENT:
							renderLayer = MoreRenderLayers.getLight(key, true);
							break;
						case INTERIOR:
							renderLayer = MoreRenderLayers.getInterior(key);
							break;
						default:
							renderLayer = MoreRenderLayers.getExterior(key);
							break;
					}
					graphicsHolder.createVertexConsumer(renderLayer);
					value.forEach(renderer -> renderer.accept(graphicsHolder));
				});
			}
		}

		lastRenderedTick = InitClient.getGameTick();
	}

	public static boolean shouldNotRender(BlockPos pos, int maxDistance, @Nullable Direction facing) {
		final Entity camera = MinecraftClient.getInstance().getCameraEntityMapped();
		return shouldNotRender(camera == null ? null : camera.getPos(), pos, maxDistance, facing);
	}

	public static void clearTextureAvailability() {
		AVAILABLE_TEXTURES.clear();
		UNAVAILABLE_TEXTURES.clear();
	}

	public static boolean isHoldingRailRelated(ClientPlayerEntity clientPlayerEntity) {
		return PlayerHelper.isHolding(new PlayerEntity(clientPlayerEntity.data),
				item -> item.data instanceof ItemNodeModifierBase ||
						Block.getBlockFromItem(item).data instanceof BlockSignalLightBase ||
						Block.getBlockFromItem(item).data instanceof BlockNode ||
						Block.getBlockFromItem(item).data instanceof BlockSignalSemaphoreBase ||
						Block.getBlockFromItem(item).data instanceof BlockPlatform
		);
	}

	public static void scheduleRender(Identifier identifier, boolean priority, QueuedRenderLayer queuedRenderLayer, Consumer<GraphicsHolder> callback) {
		final Object2ObjectArrayMap<Identifier, ObjectArraySet<Consumer<GraphicsHolder>>> map = RENDERS.get(priority ? 1 : 0).get(queuedRenderLayer.ordinal());
		if (!map.containsKey(identifier)) {
			map.put(identifier, new ObjectArraySet<>());
		}
		map.get(identifier).add(callback);
	}

	public static String getInterchangeRouteNames(Consumer<BiConsumer<String, InterchangeColorsForStationName>> getInterchanges) {
		final ObjectArrayList<String> interchangeRouteNames = new ObjectArrayList<>();
		getInterchanges.accept((connectingStationName, interchangeColorsForStationName) -> interchangeColorsForStationName.forEach((color, interchangeRouteNamesForColor) -> interchangeRouteNamesForColor.forEach(interchangeRouteNames::add)));
		return IGui.mergeStationsWithCommas(interchangeRouteNames);
	}

	private static void renderRailStandard(ClientWorld clientWorld, Rail rail, boolean renderColors, float railWidth) {
		renderRailStandard(clientWorld, rail, 0.065625F, renderColors, railWidth, renderColors && RailType.getRailColor(rail) == RailType.QUARTZ.color ? new Identifier(MTR.MOD_ID, "textures/block/rail_preview.png") : new Identifier("textures/block/rail.png"), -1, -1, -1, -1);
	}

	private static void renderRailStandard(ClientWorld clientWorld, Rail rail, float yOffset, boolean renderColors, float railWidth, @Nullable Identifier texture, float u1, float v1, float u2, float v2) {
		final int maxRailDistance = MinecraftClientHelper.getRenderDistance() * 16;

		if (renderColors || texture != null) {
			rail.render((x1, z1, x2, z2, x3, z3, x4, z4, y1, y2) -> {
				final BlockPos pos2 = Init.newBlockPos(x1, y1, z1);
				if (shouldNotRender(pos2, maxRailDistance, null)) {
					return;
				}

				if (renderColors && ClientData.instance.oneWayRails.contains(rail)) {
					scheduleRender(new Identifier(MTR.MOD_ID, "textures/block/one_way_rail_arrow.png"), false, QueuedRenderLayer.EXTERIOR, graphicsHolder -> {
						final Vector3d playerOffset = getPlayerOffset();
						IDrawing.drawTexture(graphicsHolder, x1, y1 + yOffset, z1, x2, y1 + yOffset + SMALL_OFFSET, z2, x3, y2 + yOffset, z3, x4, y2 + yOffset + SMALL_OFFSET, z4, playerOffset, 0, 0.75F, 1, 0.25F, Direction.UP, ARGB_WHITE, MAX_LIGHT_GLOWING);
						IDrawing.drawTexture(graphicsHolder, x2, y1 + yOffset + SMALL_OFFSET, z2, x1, y1 + yOffset, z1, x4, y2 + yOffset + SMALL_OFFSET, z4, x3, y2 + yOffset, z3, playerOffset, 0, 0.75F, 1, 0.25F, Direction.UP, ARGB_WHITE, MAX_LIGHT_GLOWING);
					});
				}

				if (texture != null) {
					final float textureOffset = (((int) (x1 + z1)) % 4) * 0.25F + (float) Config.trackTextureOffset() / Config.TRACK_OFFSET_COUNT;
					final int color = renderColors || !Config.hideSpecialRailColors() && rail.isSavedRail() ? RailType.getRailColor(rail) : ARGB_WHITE;
					final int light = renderColors ? MAX_LIGHT_GLOWING : LightmapTextureManager.pack(clientWorld.getLightLevel(LightType.getBlockMapped(), pos2), clientWorld.getLightLevel(LightType.getSkyMapped(), pos2));
					scheduleRender(texture, false, QueuedRenderLayer.EXTERIOR, graphicsHolder -> {
						final Vector3d playerOffset = getPlayerOffset();
						IDrawing.drawTexture(graphicsHolder, x1, y1 + yOffset, z1, x2, y1 + yOffset + SMALL_OFFSET, z2, x3, y2 + yOffset, z3, x4, y2 + yOffset + SMALL_OFFSET, z4, playerOffset, u1 < 0 ? 0 : u1, v1 < 0 ? 0.1875F + textureOffset : v1, u2 < 0 ? 1 : u2, v2 < 0 ? 0.3125F + textureOffset : v2, Direction.UP, color, light);
						IDrawing.drawTexture(graphicsHolder, x2, y1 + yOffset + SMALL_OFFSET, z2, x1, y1 + yOffset, z1, x4, y2 + yOffset + SMALL_OFFSET, z4, x3, y2 + yOffset, z3, playerOffset, u1 < 0 ? 0 : u1, v1 < 0 ? 0.1875F + textureOffset : v1, u2 < 0 ? 1 : u2, v2 < 0 ? 0.3125F + textureOffset : v2, Direction.UP, color, light);
					});
				}
			}, -railWidth, railWidth);
		}
	}

	private static Vector3d getPlayerOffset() {
		return MinecraftClient.getInstance().getGameRendererMapped().getCamera().getPos();
	}

	private static double maxDistanceXZ(Vector3d pos1, BlockPos pos2) {
		return Math.max(Math.abs(pos1.getXMapped() - pos2.getX()), Math.abs(pos1.getZMapped() - pos2.getZ()));
	}

	private static boolean shouldNotRender(@Nullable Vector3d cameraPos, BlockPos pos, int maxDistance, @Nullable Direction facing) {
		final boolean playerFacingAway;
		if (cameraPos == null || facing == null) {
			playerFacingAway = false;
		} else {
			if (facing.getAxis() == Axis.X) {
				final double playerXOffset = cameraPos.getXMapped() - pos.getX() - 0.5;
				playerFacingAway = Math.signum(playerXOffset) == facing.getOffsetX() && Math.abs(playerXOffset) >= 0.5;
			} else {
				final double playerZOffset = cameraPos.getZMapped() - pos.getZ() - 0.5;
				playerFacingAway = Math.signum(playerZOffset) == facing.getOffsetZ() && Math.abs(playerZOffset) >= 0.5;
			}
		}
		return cameraPos == null || playerFacingAway || maxDistanceXZ(cameraPos, pos) > maxDistance;
	}

	public enum QueuedRenderLayer {LIGHT, LIGHT_TRANSLUCENT, INTERIOR, EXTERIOR}
}
