package mtr.render;

import com.google.gson.JsonObject;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
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
import mtr.mappings.Utilities;
import mtr.mappings.UtilitiesClient;
import mtr.model.ModelCableCarGrip;
import mtr.path.PathData;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RenderTrains extends EntityRendererMapper<EntitySeat> implements IGui {

	public static int maxTrainRenderDistance;
	public static String creatorModelFileName = "";
	public static JsonObject creatorModel = new JsonObject();
	public static String creatorPropertiesFileName = "";
	public static JsonObject creatorProperties = new JsonObject();
	public static String creatorTextureFileName = "";
	public static ResourceLocation creatorTexture;

	private static float lastRenderedTick;
	private static int prevPlatformCount;
	private static int prevSidingCount;
	private static UUID renderedUuid;

	public static final int PLAYER_RENDER_OFFSET = 1000;

	private static final Set<String> AVAILABLE_TEXTURES = new HashSet<>();
	private static final Set<String> UNAVAILABLE_TEXTURES = new HashSet<>();

	private static final int DETAIL_RADIUS = 32;
	private static final int DETAIL_RADIUS_SQUARED = DETAIL_RADIUS * DETAIL_RADIUS;
	private static final int MAX_RADIUS_REPLAY_MOD = 64 * 16;
	private static final int TICKS_PER_SECOND = 20;

	private static final EntityModel<Minecart> MODEL_MINECART = UtilitiesClient.getMinecartModel();
	private static final EntityModel<Boat> MODEL_BOAT = UtilitiesClient.getBoatModel();
	private static final Map<Long, FakeBoat> BOATS = new HashMap<>();
	private static final ModelCableCarGrip MODEL_CABLE_CAR_GRIP = new ModelCableCarGrip();

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

		final int renderDistanceChunks = client.options.renderDistance;
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

		matrices.pushPose();
		if (!backupRendering) {
			final double entityX = Mth.lerp(tickDelta, entity.xOld, entity.getX());
			final double entityY = Mth.lerp(tickDelta, entity.yOld, entity.getY());
			final double entityZ = Mth.lerp(tickDelta, entity.zOld, entity.getZ());
			matrices.translate(-entityX, -entityY, -entityZ);
		}

		final Camera camera = client.gameRenderer.getMainCamera();
		final float cameraYaw = camera.getYRot();
		final Vec3 cameraOffset = camera.isDetached() ? player.getEyePosition(client.getFrameTime()) : camera.getPosition();
		final boolean secondF5 = Math.abs(Utilities.getYaw(player) - cameraYaw) > 90;

		ClientData.TRAINS.forEach(train -> train.simulateTrain(world, client.isPaused() || lastRenderedTick == MTRClient.getGameTick() ? 0 : lastFrameDuration, (x, y, z, yaw, pitch, trainId, baseTrainType, isEnd1Head, isEnd2Head, head1IsFront, doorLeftValue, doorRightValue, opening, lightsOn, isTranslucent, playerOffset, ridingPositions) -> renderWithLight(world, x, y, z, playerOffset == null, (light, posAverage) -> {
			final TrainClientRegistry.TrainProperties trainProperties = TrainClientRegistry.getTrainProperties(trainId, baseTrainType);
			if (trainProperties.model == null && isTranslucent) {
				return;
			}

			matrices.pushPose();
			if (playerOffset != null) {
				matrices.translate(cameraOffset.x, cameraOffset.y, cameraOffset.z);
				matrices.mulPose(Vector3f.YP.rotationDegrees(Utilities.getYaw(player) - cameraYaw + (secondF5 ? 180 : 0)));
				matrices.translate(-playerOffset.x, -playerOffset.y, -playerOffset.z);
			}

			matrices.pushPose();
			matrices.translate(x, y, z);
			matrices.mulPose(Vector3f.YP.rotation((float) Math.PI + yaw));
			matrices.mulPose(Vector3f.XP.rotation((float) Math.PI + (baseTrainType.transportMode.hasPitch ? pitch : 0)));

			if (trainProperties.model == null || trainProperties.textureId == null) {
				final boolean isBoat = baseTrainType.transportMode == TransportMode.BOAT;

				matrices.translate(0, isBoat ? 0.875 : 0.5, 0);
				matrices.mulPose(Vector3f.YP.rotationDegrees(90));

				final EntityModel<? extends Entity> model = isBoat ? MODEL_BOAT : MODEL_MINECART;
				final VertexConsumer vertexConsumer = vertexConsumers.getBuffer(model.renderType(resolveTexture(trainProperties, textureId -> textureId + ".png")));

				if (isBoat) {
					if (!BOATS.containsKey(train.id)) {
						BOATS.put(train.id, new FakeBoat());
					}
					MODEL_BOAT.setupAnim(BOATS.get(train.id), (train.getSpeed() + Train.ACCELERATION_DEFAULT) * (doorLeftValue == 0 && doorRightValue == 0 ? lastFrameDuration : 0), 0, -0.1F, 0, 0);
				} else {
					model.setupAnim(null, 0, 0, -0.1F, 0, 0);
				}

				model.renderToBuffer(matrices, vertexConsumer, light, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
			} else {
				final boolean renderDetails = MTRClient.isReplayMod() || posAverage.distSqr(camera.getBlockPosition()) <= DETAIL_RADIUS_SQUARED;
				trainProperties.model.render(matrices, vertexConsumers, resolveTexture(trainProperties, textureId -> textureId + ".png"), light, doorLeftValue, doorRightValue, opening, isEnd1Head, isEnd2Head, head1IsFront, lightsOn, isTranslucent, renderDetails);
			}

			if (baseTrainType.transportMode == TransportMode.CABLE_CAR) {
				matrices.translate(0, TransportMode.CABLE_CAR.railOffset + 0.5, 0);
				if (!baseTrainType.transportMode.hasPitch) {
					matrices.mulPose(Vector3f.XP.rotation(pitch));
				}
				if (baseTrainType.toString().endsWith("_RHT")) {
					matrices.mulPose(Vector3f.YP.rotationDegrees(180));
				}
				MODEL_CABLE_CAR_GRIP.render(matrices, vertexConsumers, light);
			}

			matrices.popPose();

			final EntityRenderDispatcher entityRenderDispatcher = client.getEntityRenderDispatcher();
			matrices.pushPose();
			matrices.translate(0, PLAYER_RENDER_OFFSET, 0);
			ridingPositions.forEach((uuid, offset) -> {
				final Player renderPlayer = world.getPlayerByUUID(uuid);
				if (renderPlayer != null && (!uuid.equals(player.getUUID()) || camera.isDetached())) {
					entityRenderDispatcher.render(renderPlayer, offset.x, offset.y, offset.z, 0, 1, matrices, vertexConsumers, 0xF000F0);
				}
			});
			matrices.popPose();

			matrices.popPose();
		}), (prevPos1, prevPos2, prevPos3, prevPos4, thisPos1, thisPos2, thisPos3, thisPos4, x, y, z, yaw, trainId, baseTrainType, lightsOn, playerOffset) -> renderWithLight(world, x, y, z, playerOffset == null, (light, posAverage) -> {
			final TrainClientRegistry.TrainProperties trainProperties = TrainClientRegistry.getTrainProperties(trainId, baseTrainType);
			if (trainProperties.textureId == null) {
				return;
			}

			matrices.pushPose();
			if (playerOffset != null) {
				matrices.translate(cameraOffset.x, cameraOffset.y, cameraOffset.z);
				matrices.mulPose(Vector3f.YP.rotationDegrees(Utilities.getYaw(player) - cameraYaw + (secondF5 ? 180 : 0)));
				matrices.translate(-playerOffset.x, -playerOffset.y, -playerOffset.z);
			}

			final VertexConsumer vertexConsumerExterior = vertexConsumers.getBuffer(MoreRenderLayers.getExterior(getConnectorTextureString(trainProperties, "exterior")));
			drawTexture(matrices, vertexConsumerExterior, thisPos2, prevPos3, prevPos4, thisPos1, light);
			drawTexture(matrices, vertexConsumerExterior, prevPos2, thisPos3, thisPos4, prevPos1, light);
			drawTexture(matrices, vertexConsumerExterior, prevPos3, thisPos2, thisPos3, prevPos2, light);
			drawTexture(matrices, vertexConsumerExterior, prevPos1, thisPos4, thisPos1, prevPos4, light);

			final int lightOnLevel = lightsOn ? MAX_LIGHT_INTERIOR : light;
			final VertexConsumer vertexConsumerSide = vertexConsumers.getBuffer(MoreRenderLayers.getInterior(getConnectorTextureString(trainProperties, "side")));
			drawTexture(matrices, vertexConsumerSide, thisPos3, prevPos2, prevPos1, thisPos4, lightOnLevel);
			drawTexture(matrices, vertexConsumerSide, prevPos3, thisPos2, thisPos1, prevPos4, lightOnLevel);
			drawTexture(matrices, vertexConsumers.getBuffer(MoreRenderLayers.getInterior(getConnectorTextureString(trainProperties, "roof"))), prevPos2, thisPos3, thisPos2, prevPos3, lightOnLevel);
			drawTexture(matrices, vertexConsumers.getBuffer(MoreRenderLayers.getInterior(getConnectorTextureString(trainProperties, "floor"))), prevPos4, thisPos1, thisPos4, prevPos1, lightOnLevel);

			matrices.popPose();
		}), (speed, stopIndex, routeIds) -> {
			if (!(speed <= 5 && RailwayData.useRoutesAndStationsFromIndex(stopIndex, routeIds, ClientData.DATA_CACHE, (currentStationIndex, thisRoute, nextRoute, thisStation, nextStation, lastStation) -> {
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
				player.displayClientMessage(new TranslatableComponent("gui.mtr.vehicle_speed", RailwayData.round(speed, 1), RailwayData.round(speed * 3.6F, 1)), true);
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
							final VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderType.lines());
							final Matrix4f matrix4f = matrices.last().pose();
							final Matrix3f matrix3f = matrices.last().normal();
							final int r = renderColors ? (rail.railType.color >> 16) & 0xFF : 0;
							final int g = renderColors ? (rail.railType.color >> 8) & 0xFF : 0;
							final int b = renderColors ? rail.railType.color & 0xFF : 0;
							vertexConsumer.vertex(matrix4f, (float) x1, (float) y1 + 0.5F, (float) z1).color(r, g, b, 0xFF).normal(matrix3f, 0, 1, 0).endVertex();
							vertexConsumer.vertex(matrix4f, (float) x3, (float) y2 + 0.5F, (float) z3).color(r, g, b, 0xFF).normal(matrix3f, 0, 1, 0).endVertex();
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
		final int maxRailDistance = Minecraft.getInstance().options.renderDistance * 16;

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
		final int maxRailDistance = Minecraft.getInstance().options.renderDistance * 16;
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

	private static void renderWithLight(Level world, double x, double y, double z, boolean noOffset, RenderCallback renderCallback) {
		final Entity camera = Minecraft.getInstance().cameraEntity;
		final Vec3 cameraPos = camera == null ? null : camera.position();
		final BlockPos posAverage = new BlockPos(x + (noOffset || cameraPos == null ? 0 : cameraPos.x), y + (noOffset || cameraPos == null ? 0 : cameraPos.y), z + (noOffset || cameraPos == null ? 0 : cameraPos.z));

		if (!shouldNotRender(cameraPos, posAverage, Minecraft.getInstance().options.renderDistance * (Config.trainRenderDistanceRatio() + 1), null)) {
			renderCallback.renderCallback(LightTexture.pack(world.getBrightness(LightLayer.BLOCK, posAverage), world.getBrightness(LightLayer.SKY, posAverage)), posAverage);
		}
	}

	private static Component getStationText(Station station, String textKey) {
		if (station != null) {
			return new TextComponent(IGui.formatStationName(IGui.insertTranslation("gui.mtr." + textKey + "_station_cjk", "gui.mtr." + textKey + "_station", 1, IGui.textOrUntitled(station.name))));
		} else {
			return new TextComponent("");
		}
	}

	private static void drawTexture(PoseStack matrices, VertexConsumer vertexConsumer, Vec3 pos1, Vec3 pos2, Vec3 pos3, Vec3 pos4, int light) {
		IDrawing.drawTexture(matrices, vertexConsumer, (float) pos1.x, (float) pos1.y, (float) pos1.z, (float) pos2.x, (float) pos2.y, (float) pos2.z, (float) pos3.x, (float) pos3.y, (float) pos3.z, (float) pos4.x, (float) pos4.y, (float) pos4.z, 0, 0, 1, 1, Direction.UP, -1, light);
	}

	private static ResourceLocation resolveTexture(TrainClientRegistry.TrainProperties trainProperties, Function<String, String> formatter) {
		final String textureString = formatter.apply(trainProperties.textureId);
		final ResourceLocation id = new ResourceLocation(textureString);
		final boolean available;

		if (!AVAILABLE_TEXTURES.contains(textureString) && !UNAVAILABLE_TEXTURES.contains(textureString)) {
			available = Minecraft.getInstance().getResourceManager().hasResource(id);
			(available ? AVAILABLE_TEXTURES : UNAVAILABLE_TEXTURES).add(textureString);
			if (!available) {
				System.out.println("Texture " + textureString + " not found, using default");
			}
		} else {
			available = AVAILABLE_TEXTURES.contains(textureString);
		}

		if (available) {
			return id;
		} else {
			final String textureId = TrainClientRegistry.getTrainProperties(trainProperties.baseTrainType.toString(), trainProperties.baseTrainType).textureId;
			return new ResourceLocation(textureId == null ? "mtr:textures/block/transparent.png" : formatter.apply(textureId));
		}
	}

	private static ResourceLocation getConnectorTextureString(TrainClientRegistry.TrainProperties trainProperties, String connectorPart) {
		return resolveTexture(trainProperties, textureId -> textureId + "_connector_" + connectorPart + ".png");
	}

	private static class FakeBoat extends Boat {

		private float progress;

		public FakeBoat() {
			super(EntityType.BOAT, null);
		}

		@Override
		public float getRowingTime(int paddle, float newProgress) {
			progress += newProgress;
			return progress;
		}
	}

	@Deprecated // TODO remove
	public static float getGameTicks() {
		return MTRClient.getGameTick();
	}

	@FunctionalInterface
	private interface RenderCallback {
		void renderCallback(int light, BlockPos posAverage);
	}
}
