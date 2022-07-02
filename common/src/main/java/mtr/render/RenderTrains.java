package mtr.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mtr.MTRClient;
import mtr.block.BlockNode;
import mtr.block.BlockPlatform;
import mtr.block.BlockSignalLightBase;
import mtr.block.BlockSignalSemaphoreBase;
import mtr.client.*;
import mtr.data.*;
import mtr.entity.EntitySeat;
import mtr.item.ItemNodeModifierBase;
import mtr.mappings.EntityRendererMapper;
import mtr.mappings.Text;
import mtr.mappings.Utilities;
import mtr.mappings.UtilitiesClient;
import mtr.path.PathData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;

import java.util.*;
import java.util.stream.Collectors;

public class RenderTrains extends EntityRendererMapper<EntitySeat> implements IGui {

	public static int maxTrainRenderDistance;
	public static ResourcePackCreatorProperties creatorProperties = new ResourcePackCreatorProperties();

	private static float lastRenderedTick;
	private static int prevPlatformCount;
	private static int prevSidingCount;
	private static UUID renderedUuid;

	public static final int PLAYER_RENDER_OFFSET = 1000;

	public static final Set<String> AVAILABLE_TEXTURES = new HashSet<>();
	public static final Set<String> UNAVAILABLE_TEXTURES = new HashSet<>();

	public static final int DETAIL_RADIUS = 32;
	public static final int DETAIL_RADIUS_SQUARED = DETAIL_RADIUS * DETAIL_RADIUS;
	private static final int MAX_RADIUS_REPLAY_MOD = 64 * 16;
	private static final int TICKS_PER_SECOND = 20;

	public RenderTrains(Object parameter) {
		super(parameter);
	}

	@Override
	public void render(EntitySeat entity, float entityYaw, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int entityLight) {
		render(entity, tickDelta, matrices, vertexConsumers);
	}

	@Override
	public ResourceLocation getTextureLocation(EntitySeat entity) {
		return null;
	}

	public static void render(EntitySeat entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers) {
		final Minecraft client = Minecraft.getInstance();
		final boolean backupRendering = entity == null;
		final boolean alreadyRendered = renderedUuid != null && (backupRendering || entity.getUUID() != renderedUuid);

		if (backupRendering) {
			renderedUuid = null;
		}

		final LocalPlayer player = client.player;
		final Level world = client.level;

		if (alreadyRendered || player == null || world == null) {
			return;
		}

		if (!backupRendering) {
			renderedUuid = entity.getUUID();
		}

		final int renderDistanceChunks = UtilitiesClient.getRenderDistance();
		final float lastFrameDuration = MTRClient.getLastFrameDuration();
		final boolean useAnnouncements = Config.useTTSAnnouncements() || Config.showAnnouncementMessages();

		if (Config.useDynamicFPS()) {
			if (lastFrameDuration > 0.5) {
				maxTrainRenderDistance = Math.max(maxTrainRenderDistance - (maxTrainRenderDistance - DETAIL_RADIUS) / 2, DETAIL_RADIUS);
			} else if (lastFrameDuration < 0.4) {
				maxTrainRenderDistance = Math.min(maxTrainRenderDistance + 1, renderDistanceChunks * (Config.trainRenderDistanceRatio() + 1));
			}
		} else {
			maxTrainRenderDistance = renderDistanceChunks * (Config.trainRenderDistanceRatio() + 1);
		}

		if (!backupRendering) {
			matrices.popPose();
			matrices.pushPose();
			final Vec3 cameraPosition = client.gameRenderer.getMainCamera().getPosition();
			matrices.translate(-cameraPosition.x, -cameraPosition.y, -cameraPosition.z);
		}
		matrices.pushPose();

		TrainRendererBase.setupStaticInfo(matrices, vertexConsumers, entity, tickDelta);

		ClientData.TRAINS.forEach(train -> train.simulateTrain(world, client.isPaused() || lastRenderedTick == MTRClient.getGameTick() ? 0 : lastFrameDuration, (speed, stopIndex, routeIds) -> {
			if ((!train.isCurrentlyManual() || !Train.isHoldingKey(player)) && !(speed <= 5 && RailwayData.useRoutesAndStationsFromIndex(stopIndex, routeIds, ClientData.DATA_CACHE, (currentStationIndex, thisRoute, nextRoute, thisStation, nextStation, lastStation) -> {
				final Component text;
				switch ((int) ((System.currentTimeMillis() / 1000) % 3)) {
					default:
						text = getStationText(thisStation, "this");
						break;
					case 1:
						if (nextStation == null) {
							text = getStationText(thisStation, "this");
						} else {
							text = getStationText(nextStation, "next");
						}
						break;
					case 2:
						text = getStationText(lastStation, "last_" + thisRoute.transportMode.toString().toLowerCase());
						break;
				}
				player.displayClientMessage(text, true);
			}))) {
				player.displayClientMessage(Text.translatable("gui.mtr.vehicle_speed", RailwayData.round(speed, 1), RailwayData.round(speed * 3.6F, 1)), true);
			}
		}, (stopIndex, routeIds) -> {
			if (useAnnouncements) {
				RailwayData.useRoutesAndStationsFromIndex(stopIndex, routeIds, ClientData.DATA_CACHE, (currentStationIndex, thisRoute, nextRoute, thisStation, nextStation, lastStation) -> {
					final List<String> messages = new ArrayList<>();
					final String thisRouteSplit = thisRoute.name.split("\\|\\|")[0];
					final String nextRouteSplit = nextRoute == null ? null : nextRoute.name.split("\\|\\|")[0];

					if (nextStation != null) {
						final boolean isLightRailRoute = thisRoute.isLightRailRoute;
						messages.add(IGui.insertTranslation(isLightRailRoute ? "gui.mtr.next_station_light_rail_announcement_cjk" : "gui.mtr.next_station_announcement_cjk", isLightRailRoute ? "gui.mtr.next_station_light_rail_announcement" : "gui.mtr.next_station_announcement", 1, nextStation.name));

						final Map<Integer, ClientCache.ColorNameTuple> routesInStation = ClientData.DATA_CACHE.stationIdToRoutes.get(nextStation.id);
						if (routesInStation != null) {
							final List<String> interchangeRoutes = routesInStation.values().stream().filter(interchangeRoute -> {
								final String routeName = interchangeRoute.name.split("\\|\\|")[0];
								return !routeName.equals(thisRouteSplit) && (nextRoute == null || !routeName.equals(nextRouteSplit));
							}).map(interchangeRoute -> interchangeRoute.name).collect(Collectors.toList());
							final String mergedStations = IGui.mergeStations(interchangeRoutes, ", ");
							if (!mergedStations.isEmpty()) {
								messages.add(IGui.insertTranslation("gui.mtr.interchange_announcement_cjk", "gui.mtr.interchange_announcement", 1, mergedStations));
							}
						}

						if (lastStation != null && nextStation.id == lastStation.id && nextRoute != null && !nextRoute.platformIds.isEmpty() && !nextRouteSplit.equals(thisRouteSplit)) {
							final Station nextFinalStation = ClientData.DATA_CACHE.platformIdToStation.get(nextRoute.platformIds.get(nextRoute.platformIds.size() - 1));
							if (nextFinalStation != null) {
								final String modeString = thisRoute.transportMode.toString().toLowerCase();
								if (nextRoute.isLightRailRoute) {
									messages.add(IGui.insertTranslation("gui.mtr.next_route_" + modeString + "_light_rail_announcement_cjk", "gui.mtr.next_route_" + modeString + "_light_rail_announcement", nextRoute.lightRailRouteNumber, 1, nextFinalStation.name.split("\\|\\|")[0]));
								} else {
									messages.add(IGui.insertTranslation("gui.mtr.next_route_" + modeString + "_announcement_cjk", "gui.mtr.next_route_" + modeString + "_announcement", 2, nextRouteSplit, nextFinalStation.name.split("\\|\\|")[0]));
								}
							}
						}
					}

					IDrawing.narrateOrAnnounce(IGui.mergeStations(messages, " "));
				});
			}
		}, (stopIndex, routeIds) -> {
			if (useAnnouncements) {
				RailwayData.useRoutesAndStationsFromIndex(stopIndex, routeIds, ClientData.DATA_CACHE, (currentStationIndex, thisRoute, nextRoute, thisStation, nextStation, lastStation) -> {
					if (thisRoute.isLightRailRoute && lastStation != null) {
						IDrawing.narrateOrAnnounce(IGui.insertTranslation("gui.mtr.light_rail_route_announcement_cjk", "gui.mtr.light_rail_route_announcement", thisRoute.lightRailRouteNumber, 1, lastStation.name));
					}
				});
			}
		}));
		if (!Config.hideTranslucentParts()) {
			ClientData.TRAINS.forEach(TrainClient::renderTranslucent);
		}

		final boolean renderColors = isHoldingRailRelated(player);
		final int maxRailDistance = renderDistanceChunks * 16;
		final Map<UUID, RailType> renderedRailMap = new HashMap<>();
		ClientData.RAILS.forEach((startPos, railMap) -> railMap.forEach((endPos, rail) -> {
			if (!RailwayData.isBetween(player.getX(), startPos.getX(), endPos.getX(), maxRailDistance) || !RailwayData.isBetween(player.getZ(), startPos.getZ(), endPos.getZ(), maxRailDistance)) {
				return;
			}

			final UUID railProduct = PathData.getRailProduct(startPos, endPos);
			if (renderedRailMap.containsKey(railProduct)) {
				if (renderedRailMap.get(railProduct) == rail.railType) {
					return;
				}
			} else {
				renderedRailMap.put(railProduct, rail.railType);
			}

			switch (rail.transportMode) {
				case TRAIN:
					renderRailStandard(world, matrices, vertexConsumers, rail, 0.0625F + SMALL_OFFSET, renderColors, 1);
					if (renderColors) {
						renderSignalsStandard(world, matrices, vertexConsumers, rail, startPos, endPos);
					}
					break;
				case BOAT:
					if (renderColors) {
						renderRailStandard(world, matrices, vertexConsumers, rail, 0.0625F + SMALL_OFFSET, true, 0.5F);
						renderSignalsStandard(world, matrices, vertexConsumers, rail, startPos, endPos);
					}
					break;
				case CABLE_CAR:
					if (rail.railType.hasSavedRail || rail.railType == RailType.CABLE_CAR_STATION) {
						renderRailStandard(world, matrices, vertexConsumers, rail, 0.25F + SMALL_OFFSET, renderColors, 0.25F, "mtr:textures/block/metal.png", 0.25F, 0, 0.75F, 1);
					}
					if (renderColors && !rail.railType.hasSavedRail) {
						renderRailStandard(world, matrices, vertexConsumers, rail, 0.5F + SMALL_OFFSET, true, 1, "mtr:textures/block/one_way_rail_arrow.png", 0, 0.75F, 1, 0.25F);
					}

					if (rail.railType != RailType.NONE) {
						rail.render((x1, z1, x2, z2, x3, z3, x4, z4, y1, y2) -> {
							final int r = renderColors ? (rail.railType.color >> 16) & 0xFF : 0;
							final int g = renderColors ? (rail.railType.color >> 8) & 0xFF : 0;
							final int b = renderColors ? rail.railType.color & 0xFF : 0;
							IDrawing.drawLine(matrices, vertexConsumers, (float) x1, (float) y1 + 0.5F, (float) z1, (float) x3, (float) y2 + 0.5F, (float) z3, r, g, b);
						}, 0, 0);
					}

					break;
			}
		}));

		matrices.popPose();
		lastRenderedTick = MTRClient.getGameTick();

		if (prevPlatformCount != ClientData.PLATFORMS.size() || prevSidingCount != ClientData.SIDINGS.size()) {
			ClientData.DATA_CACHE.sync();
		}
		prevPlatformCount = ClientData.PLATFORMS.size();
		prevSidingCount = ClientData.SIDINGS.size();
		ClientData.DATA_CACHE.clearDataIfNeeded();
	}

	public static boolean shouldNotRender(BlockPos pos, int maxDistance, Direction facing) {
		final Entity camera = Minecraft.getInstance().cameraEntity;
		return shouldNotRender(camera == null ? null : camera.position(), pos, maxDistance, facing);
	}

	public static void clearTextureAvailability() {
		AVAILABLE_TEXTURES.clear();
		UNAVAILABLE_TEXTURES.clear();
	}

	public static boolean isHoldingRailRelated(Player player) {
		return Utilities.isHolding(player,
				item -> item instanceof ItemNodeModifierBase ||
						Block.byItem(item) instanceof BlockSignalLightBase ||
						Block.byItem(item) instanceof BlockNode ||
						Block.byItem(item) instanceof BlockSignalSemaphoreBase ||
						Block.byItem(item) instanceof BlockPlatform
		);
	}

	private static double maxDistanceXZ(Vec3 pos1, BlockPos pos2) {
		return Math.max(Math.abs(pos1.x - pos2.getX()), Math.abs(pos1.z - pos2.getZ()));
	}

	private static boolean shouldNotRender(Vec3 cameraPos, BlockPos pos, int maxDistance, Direction facing) {
		final boolean playerFacingAway;
		if (cameraPos == null || facing == null) {
			playerFacingAway = false;
		} else {
			if (facing.getAxis() == Direction.Axis.X) {
				final double playerXOffset = cameraPos.x - pos.getX() - 0.5;
				playerFacingAway = Math.signum(playerXOffset) == facing.getStepX() && Math.abs(playerXOffset) >= 0.5;
			} else {
				final double playerZOffset = cameraPos.z - pos.getZ() - 0.5;
				playerFacingAway = Math.signum(playerZOffset) == facing.getStepZ() && Math.abs(playerZOffset) >= 0.5;
			}
		}
		return cameraPos == null || playerFacingAway || maxDistanceXZ(cameraPos, pos) > (MTRClient.isReplayMod() ? MAX_RADIUS_REPLAY_MOD : maxDistance);
	}

	private static void renderRailStandard(Level world, PoseStack matrices, MultiBufferSource vertexConsumers, Rail rail, float yOffset, boolean renderColors, float railWidth) {
		renderRailStandard(world, matrices, vertexConsumers, rail, yOffset, renderColors, railWidth, renderColors && rail.railType == RailType.QUARTZ ? "mtr:textures/block/rail_preview.png" : "textures/block/rail.png", -1, -1, -1, -1);
	}

	private static void renderRailStandard(Level world, PoseStack matrices, MultiBufferSource vertexConsumers, Rail rail, float yOffset, boolean renderColors, float railWidth, String texture, float u1, float v1, float u2, float v2) {
		final int maxRailDistance = UtilitiesClient.getRenderDistance() * 16;

		rail.render((x1, z1, x2, z2, x3, z3, x4, z4, y1, y2) -> {
			final BlockPos pos2 = new BlockPos(x1, y1, z1);
			if (shouldNotRender(pos2, maxRailDistance, null)) {
				return;
			}
			final int light2 = LightTexture.pack(world.getBrightness(LightLayer.BLOCK, pos2), world.getBrightness(LightLayer.SKY, pos2));

			if (rail.railType == RailType.NONE) {
				if (rail.transportMode != TransportMode.CABLE_CAR && renderColors) {
					final VertexConsumer vertexConsumerArrow = vertexConsumers.getBuffer(MoreRenderLayers.getExterior(new ResourceLocation("mtr:textures/block/one_way_rail_arrow.png")));
					IDrawing.drawTexture(matrices, vertexConsumerArrow, (float) x1, (float) y1 + yOffset, (float) z1, (float) x2, (float) y1 + yOffset + SMALL_OFFSET, (float) z2, (float) x3, (float) y2 + yOffset, (float) z3, (float) x4, (float) y2 + yOffset + SMALL_OFFSET, (float) z4, 0, 0.25F, 1, 0.75F, Direction.UP, -1, light2);
					IDrawing.drawTexture(matrices, vertexConsumerArrow, (float) x2, (float) y1 + yOffset + SMALL_OFFSET, (float) z2, (float) x1, (float) y1 + yOffset, (float) z1, (float) x4, (float) y2 + yOffset + SMALL_OFFSET, (float) z4, (float) x3, (float) y2 + yOffset, (float) z3, 0, 0.25F, 1, 0.75F, Direction.UP, -1, light2);
				}
			} else {
				final float textureOffset = (((int) (x1 + z1)) % 4) * 0.25F + (float) Config.trackTextureOffset() / Config.TRACK_OFFSET_COUNT;
				final int color = renderColors || !Config.hideSpecialRailColors() && rail.railType.hasSavedRail ? rail.railType.color : -1;
				final VertexConsumer vertexConsumer = vertexConsumers.getBuffer(MoreRenderLayers.getExterior(new ResourceLocation(texture)));
				IDrawing.drawTexture(matrices, vertexConsumer, (float) x1, (float) y1 + yOffset, (float) z1, (float) x2, (float) y1 + yOffset + SMALL_OFFSET, (float) z2, (float) x3, (float) y2 + yOffset, (float) z3, (float) x4, (float) y2 + yOffset + SMALL_OFFSET, (float) z4, u1 < 0 ? 0 : u1, v1 < 0 ? 0.1875F + textureOffset : v1, u2 < 0 ? 1 : u2, v2 < 0 ? 0.3125F + textureOffset : v2, Direction.UP, color, light2);
				IDrawing.drawTexture(matrices, vertexConsumer, (float) x2, (float) y1 + yOffset + SMALL_OFFSET, (float) z2, (float) x1, (float) y1 + yOffset, (float) z1, (float) x4, (float) y2 + yOffset + SMALL_OFFSET, (float) z4, (float) x3, (float) y2 + yOffset, (float) z3, u1 < 0 ? 0 : u1, v1 < 0 ? 0.1875F + textureOffset : v1, u2 < 0 ? 1 : u2, v2 < 0 ? 0.3125F + textureOffset : v2, Direction.UP, color, light2);
			}
		}, -railWidth, railWidth);
	}

	private static void renderSignalsStandard(Level world, PoseStack matrices, MultiBufferSource vertexConsumers, Rail rail, BlockPos startPos, BlockPos endPos) {
		final int maxRailDistance = UtilitiesClient.getRenderDistance() * 16;
		final List<SignalBlocks.SignalBlock> signalBlocks = ClientData.SIGNAL_BLOCKS.getSignalBlocksAtTrack(PathData.getRailProduct(startPos, endPos));
		final float width = 1F / DyeColor.values().length;

		for (int i = 0; i < signalBlocks.size(); i++) {
			final SignalBlocks.SignalBlock signalBlock = signalBlocks.get(i);
			final boolean shouldGlow = signalBlock.isOccupied() && (((int) Math.floor(MTRClient.getGameTick())) % TICKS_PER_SECOND) < TICKS_PER_SECOND / 2;
			final VertexConsumer vertexConsumer = shouldGlow ? vertexConsumers.getBuffer(MoreRenderLayers.getLight(new ResourceLocation("mtr:textures/block/white.png"), false)) : vertexConsumers.getBuffer(MoreRenderLayers.getExterior(new ResourceLocation("textures/block/white_wool.png")));
			final float u1 = width * i + 1 - width * signalBlocks.size() / 2;
			final float u2 = u1 + width;

			final int color = ARGB_BLACK | signalBlock.color.getMaterialColor().col;
			rail.render((x1, z1, x2, z2, x3, z3, x4, z4, y1, y2) -> {
				final BlockPos pos2 = new BlockPos(x1, y1, z1);
				if (shouldNotRender(pos2, maxRailDistance, null)) {
					return;
				}
				final int light2 = shouldGlow ? MAX_LIGHT_GLOWING : LightTexture.pack(world.getBrightness(LightLayer.BLOCK, pos2), world.getBrightness(LightLayer.SKY, pos2));

				IDrawing.drawTexture(matrices, vertexConsumer, (float) x1, (float) y1, (float) z1, (float) x2, (float) y1 + SMALL_OFFSET, (float) z2, (float) x3, (float) y2, (float) z3, (float) x4, (float) y2 + SMALL_OFFSET, (float) z4, u1, 0, u2, 1, Direction.UP, color, light2);
				IDrawing.drawTexture(matrices, vertexConsumer, (float) x4, (float) y2 + SMALL_OFFSET, (float) z4, (float) x3, (float) y2, (float) z3, (float) x2, (float) y1 + SMALL_OFFSET, (float) z2, (float) x1, (float) y1, (float) z1, u1, 0, u2, 1, Direction.UP, color, light2);
			}, u1 - 1, u2 - 1);
		}
	}

	private static Component getStationText(Station station, String textKey) {
		if (station != null) {
			return Text.literal(IGui.formatStationName(IGui.insertTranslation("gui.mtr." + textKey + "_station_cjk", "gui.mtr." + textKey + "_station", 1, IGui.textOrUntitled(station.name))));
		} else {
			return Text.literal("");
		}
	}

	@Deprecated // TODO remove
	public static float getGameTicks() {
		return MTRClient.getGameTick();
	}
}
