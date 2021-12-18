package mtr.render;

import com.google.gson.JsonObject;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import mapper.Utilities;
import mapper.UtilitiesClient;
import mtr.MTRClient;
import mtr.block.BlockSignalLightBase;
import mtr.block.BlockSignalSemaphoreBase;
import mtr.config.Config;
import mtr.data.*;
import mtr.gui.ClientCache;
import mtr.gui.ClientData;
import mtr.gui.IDrawing;
import mtr.item.ItemNodeModifierBase;
import mtr.model.TrainClientRegistry;
import mtr.path.PathData;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RenderTrains implements IGui {

	public static int maxTrainRenderDistance;
	public static String creatorModelFileName = "";
	public static JsonObject creatorModel = new JsonObject();
	public static String creatorPropertiesFileName = "";
	public static JsonObject creatorProperties = new JsonObject();
	public static String creatorTextureFileName = "";
	public static ResourceLocation creatorTexture;

	private static float gameTick = 0;
	private static float lastPlayedTrainSoundsTick = 0;
	private static int prevPlatformCount;
	private static int prevSidingCount;

	public static final int TICKS_PER_SPEED_SOUND = 4;

	private static final Set<String> AVAILABLE_TEXTURES = new HashSet<>();
	private static final Set<String> UNAVAILABLE_TEXTURES = new HashSet<>();

	private static final int DETAIL_RADIUS = 32;
	private static final int DETAIL_RADIUS_SQUARED = DETAIL_RADIUS * DETAIL_RADIUS;
	private static final int MAX_RADIUS_REPLAY_MOD = 64 * 16;
	private static final int TICKS_PER_SECOND = 20;

	private static final EntityModel<Minecart> MODEL_MINECART = UtilitiesClient.getMinecartModel();

	public static void render(Level world, PoseStack matrices, MultiBufferSource vertexConsumers, Camera camera) {
		final Minecraft client = Minecraft.getInstance();
		final LocalPlayer player = client.player;
		if (player == null) {
			return;
		}

		final int renderDistanceChunks = client.options.renderDistance;
		final float lastFrameDuration = MTRClient.isReplayMod ? 20F / 60 : client.getDeltaFrameTime();
		gameTick += lastFrameDuration;

		final boolean useTTSAnnouncements = Config.useTTSAnnouncements();
		if (Config.useDynamicFPS()) {
			if (lastFrameDuration > 0.5) {
				maxTrainRenderDistance = Math.max(maxTrainRenderDistance - (maxTrainRenderDistance - DETAIL_RADIUS) / 2, DETAIL_RADIUS);
			} else if (lastFrameDuration < 0.4) {
				maxTrainRenderDistance = Math.min(maxTrainRenderDistance + 1, renderDistanceChunks * 8);
			}
		} else {
			maxTrainRenderDistance = renderDistanceChunks * 8;
		}

		final Vec3 cameraPos = camera.getPosition();
		final float cameraYaw = camera.getYRot();
		final Vec3 cameraOffset = client.gameRenderer.getMainCamera().isDetached() ? player.getPosition(client.getFrameTime()).subtract(cameraPos) : Vec3.ZERO;
		final boolean secondF5 = Math.abs(Utilities.getYaw(player) - client.gameRenderer.getMainCamera().getYRot()) > 90;

		ClientData.TRAINS.forEach(train -> train.simulateTrain(world, client.isPaused() ? 0 : lastFrameDuration, (x, y, z, yaw, pitch, trainId, baseTrainType, isEnd1Head, isEnd2Head, head1IsFront, doorLeftValue, doorRightValue, opening, lightsOn, isTranslucent, playerOffset) -> renderWithLight(world, x, y, z, cameraPos.add(cameraOffset), playerOffset != null, (light, posAverage) -> {
			final TrainClientRegistry.TrainProperties trainProperties = TrainClientRegistry.getTrainProperties(trainId, baseTrainType);
			if (trainProperties.model == null && isTranslucent) {
				return;
			}

			matrices.pushPose();
			if (playerOffset == null) {
				matrices.translate(x - cameraPos.x, y - cameraPos.y, z - cameraPos.z);
			} else {
				matrices.translate(cameraOffset.x, cameraOffset.y, cameraOffset.z);
				matrices.mulPose(Vector3f.YP.rotationDegrees(Utilities.getYaw(player) - cameraYaw + (secondF5 ? 180 : 0)));
				matrices.translate(x - playerOffset.x, y - playerOffset.y, z - playerOffset.z);
			}
			matrices.mulPose(Vector3f.YP.rotation((float) Math.PI + yaw));
			matrices.mulPose(Vector3f.XP.rotation((float) Math.PI + pitch));

			if (trainProperties.model == null || trainProperties.textureId == null) {
				matrices.translate(0, 0.5, 0);
				matrices.mulPose(Vector3f.YP.rotationDegrees(90));
				final VertexConsumer vertexConsumer = vertexConsumers.getBuffer(MODEL_MINECART.renderType(new ResourceLocation("textures/entity/minecart.png")));
				MODEL_MINECART.setupAnim(null, 0, 0, -0.1F, 0, 0);
				MODEL_MINECART.renderToBuffer(matrices, vertexConsumer, light, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
			} else {
				trainProperties.model.render(matrices, vertexConsumers, resolveTexture(trainProperties, textureId -> textureId + ".png"), light, doorLeftValue, doorRightValue, opening, isEnd1Head, isEnd2Head, head1IsFront, lightsOn, isTranslucent, MTRClient.isReplayMod || posAverage.distSqr(new BlockPos(cameraPos)) <= DETAIL_RADIUS_SQUARED);
			}

			matrices.popPose();
		}), (prevPos1, prevPos2, prevPos3, prevPos4, thisPos1, thisPos2, thisPos3, thisPos4, x, y, z, yaw, trainId, baseTrainType, lightsOn, playerOffset) -> renderWithLight(world, x, y, z, cameraPos.add(cameraOffset), playerOffset != null, (light, posAverage) -> {
			final TrainClientRegistry.TrainProperties trainProperties = TrainClientRegistry.getTrainProperties(trainId, baseTrainType);
			if (trainProperties.textureId == null) {
				return;
			}

			matrices.pushPose();
			if (playerOffset == null) {
				matrices.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
			} else {
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
			if (!(speed <= 5 && RailwayData.useRoutesAndStationsFromIndex(stopIndex, routeIds, ClientData.DATA_CACHE, (thisRoute, nextRoute, thisStation, nextStation, lastStation) -> {
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
						text = getStationText(lastStation, "last");
						break;
				}
				player.displayClientMessage(text, true);
			}))) {
				player.displayClientMessage(new TranslatableComponent("gui.mtr.train_speed", Math.round(speed * 10) / 10F, Math.round(speed * 36) / 10F), true);
			}
		}, (stopIndex, routeIds) -> {
			final boolean showAnnouncementMessages = Config.showAnnouncementMessages();

			if (showAnnouncementMessages || useTTSAnnouncements) {
				RailwayData.useRoutesAndStationsFromIndex(stopIndex, routeIds, ClientData.DATA_CACHE, (thisRoute, nextRoute, thisStation, nextStation, lastStation) -> {
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
								if (nextRoute.isLightRailRoute) {
									messages.add(IGui.insertTranslation("gui.mtr.next_route_light_rail_announcement_cjk", "gui.mtr.next_route_light_rail_announcement", nextRoute.lightRailRouteNumber, 1, nextFinalStation.name.split("\\|\\|")[0]));
								} else {
									messages.add(IGui.insertTranslation("gui.mtr.next_route_announcement_cjk", "gui.mtr.next_route_announcement", 2, nextRouteSplit, nextFinalStation.name.split("\\|\\|")[0]));
								}
							}
						}
					}

					IDrawing.narrateOrAnnounce(IGui.mergeStations(messages, " "));
				});
			}
		}, (stopIndex, routeIds) -> {
			if (useTTSAnnouncements) {
				RailwayData.useRoutesAndStationsFromIndex(stopIndex, routeIds, ClientData.DATA_CACHE, (thisRoute, nextRoute, thisStation, nextStation, lastStation) -> {
					if (thisRoute.isLightRailRoute && lastStation != null) {
						IDrawing.narrateOrAnnounce(IGui.insertTranslation("gui.mtr.light_rail_route_announcement_cjk", "gui.mtr.light_rail_route_announcement", thisRoute.lightRailRouteNumber, 1, lastStation.name));
					}
				});
			}
		}));
		if (!Config.hideTranslucentParts()) {
			ClientData.TRAINS.forEach(TrainClient::renderTranslucent);
		}

		matrices.translate(-cameraPos.x, 0.0625 + SMALL_OFFSET - cameraPos.y, -cameraPos.z);
		final boolean renderColors = Utilities.isHolding(player, item -> item instanceof ItemNodeModifierBase || Block.byItem(item) instanceof BlockSignalLightBase || Block.byItem(item) instanceof BlockSignalSemaphoreBase);
		final int maxRailDistance = renderDistanceChunks * 16;
		ClientData.RAILS.forEach((startPos, railMap) -> railMap.forEach((endPos, rail) -> {
			if (!RailwayData.isBetween(player.getX(), startPos.getX(), endPos.getX(), maxRailDistance) || !RailwayData.isBetween(player.getZ(), startPos.getZ(), endPos.getZ(), maxRailDistance)) {
				return;
			}

			rail.render((x1, z1, x2, z2, x3, z3, x4, z4, y1, y2) -> {
				final BlockPos pos2 = new BlockPos(x1, y1, z1);
				if (shouldNotRender(pos2, maxRailDistance, null)) {
					return;
				}
				final int light2 = LightTexture.pack(world.getBrightness(LightLayer.BLOCK, pos2), world.getBrightness(LightLayer.SKY, pos2));

				if (rail.railType == RailType.NONE) {
					if (renderColors) {
						final VertexConsumer vertexConsumerArrow = vertexConsumers.getBuffer(MoreRenderLayers.getExterior(new ResourceLocation("mtr:textures/block/one_way_rail_arrow.png")));
						IDrawing.drawTexture(matrices, vertexConsumerArrow, (float) x1, (float) y1, (float) z1, (float) x2, (float) y1 + SMALL_OFFSET, (float) z2, (float) x3, (float) y2, (float) z3, (float) x4, (float) y2 + SMALL_OFFSET, (float) z4, 0, 0.25F, 1, 0.75F, Direction.UP, -1, light2);
						IDrawing.drawTexture(matrices, vertexConsumerArrow, (float) x2, (float) y1 + SMALL_OFFSET, (float) z2, (float) x1, (float) y1, (float) z1, (float) x4, (float) y2 + SMALL_OFFSET, (float) z4, (float) x3, (float) y2, (float) z3, 0, 0.25F, 1, 0.75F, Direction.UP, -1, light2);
					}
				} else {
					final float textureOffset = (((int) (x1 + z1)) % 4) * 0.25F + (float) Config.trackTextureOffset() / Config.TRACK_OFFSET_COUNT;
					final int color = renderColors || !Config.hideSpecialRailColors() && rail.railType.hasSavedRail ? rail.railType.color : -1;

					final VertexConsumer vertexConsumer = vertexConsumers.getBuffer(MoreRenderLayers.getExterior(new ResourceLocation("textures/block/rail.png")));
					IDrawing.drawTexture(matrices, vertexConsumer, (float) x1, (float) y1, (float) z1, (float) x2, (float) y1 + SMALL_OFFSET, (float) z2, (float) x3, (float) y2, (float) z3, (float) x4, (float) y2 + SMALL_OFFSET, (float) z4, 0, 0.1875F + textureOffset, 1, 0.3125F + textureOffset, Direction.UP, color, light2);
					IDrawing.drawTexture(matrices, vertexConsumer, (float) x4, (float) y2 + SMALL_OFFSET, (float) z4, (float) x3, (float) y2, (float) z3, (float) x2, (float) y1 + SMALL_OFFSET, (float) z2, (float) x1, (float) y1, (float) z1, 0, 0.1875F + textureOffset, 1, 0.3125F + textureOffset, Direction.UP, color, light2);
				}
			}, -1, 1);
			if (renderColors) {
				final List<SignalBlocks.SignalBlock> signalBlocks = ClientData.SIGNAL_BLOCKS.getSignalBlocksAtTrack(PathData.getRailProduct(startPos, endPos));
				final float width = 1F / DyeColor.values().length;

				for (int i = 0; i < signalBlocks.size(); i++) {
					final SignalBlocks.SignalBlock signalBlock = signalBlocks.get(i);
					final boolean shouldGlow = signalBlock.isOccupied() && (((int) Math.floor(getGameTicks())) % TICKS_PER_SECOND) < TICKS_PER_SECOND / 2;
					final VertexConsumer vertexConsumer = shouldGlow ? vertexConsumers.getBuffer(MoreRenderLayers.getLight(new ResourceLocation("mtr:textures/block/white.png"), false)) : vertexConsumers.getBuffer(MoreRenderLayers.getExterior(new ResourceLocation("textures/block/white_wool.png")));
					final float u1 = width * i + 1 - width * signalBlocks.size() / 2;
					final float u2 = u1 + width;

					final int color = ARGB_BLACK + signalBlock.color.getMaterialColor().col;
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
		}));

		if (prevPlatformCount != ClientData.PLATFORMS.size() || prevSidingCount != ClientData.SIDINGS.size()) {
			ClientData.DATA_CACHE.sync();
		}
		prevPlatformCount = ClientData.PLATFORMS.size();
		prevSidingCount = ClientData.SIDINGS.size();
		ClientData.DATA_CACHE.clearDataIfNeeded();
	}

	public static float getGameTicks() {
		return gameTick;
	}

	public static boolean canPlaySound() {
		if (gameTick - lastPlayedTrainSoundsTick >= TICKS_PER_SPEED_SOUND) {
			lastPlayedTrainSoundsTick = gameTick;
		}
		return gameTick == lastPlayedTrainSoundsTick && !Minecraft.getInstance().isPaused();
	}

	public static boolean shouldNotRender(BlockPos pos, int maxDistance, Direction facing) {
		final Entity camera = Minecraft.getInstance().cameraEntity;
		return shouldNotRender(camera, pos, maxDistance, facing);
	}

	public static void clearTextureAvailability() {
		AVAILABLE_TEXTURES.clear();
		UNAVAILABLE_TEXTURES.clear();
	}

	private static boolean shouldNotRender(Entity camera, BlockPos pos, int maxDistance, Direction facing) {
		final boolean playerFacingAway;
		if (facing == null) {
			playerFacingAway = false;
		} else {
			if (facing.getAxis() == Direction.Axis.X) {
				final double playerXOffset = camera.getX() - pos.getX() - 0.5;
				playerFacingAway = Math.signum(playerXOffset) == facing.getStepX() && Math.abs(playerXOffset) >= 0.5;
			} else {
				final double playerZOffset = camera.getZ() - pos.getZ() - 0.5;
				playerFacingAway = Math.signum(playerZOffset) == facing.getStepZ() && Math.abs(playerZOffset) >= 0.5;
			}
		}
		return camera == null || playerFacingAway || camera.blockPosition().distManhattan(pos) > (MTRClient.isReplayMod ? MAX_RADIUS_REPLAY_MOD : maxDistance);
	}

	private static void renderWithLight(Level world, double x, double y, double z, Vec3 cameraPos, boolean offsetRender, RenderCallback renderCallback) {
		final BlockPos posAverage = offsetRender ? new BlockPos(cameraPos).offset(x, y, z) : new BlockPos(x, y, z);
		if (!shouldNotRender(posAverage, Minecraft.getInstance().options.renderDistance * 8, null)) {
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

		return available ? id : new ResourceLocation(formatter.apply(TrainClientRegistry.getTrainProperties(trainProperties.baseTrainType.toString(), trainProperties.baseTrainType).textureId));
	}

	private static ResourceLocation getConnectorTextureString(TrainClientRegistry.TrainProperties trainProperties, String connectorPart) {
		return resolveTexture(trainProperties, textureId -> textureId + "_connector_" + connectorPart + ".png");
	}

	@FunctionalInterface
	private interface RenderCallback {
		void renderCallback(int light, BlockPos posAverage);
	}
}
