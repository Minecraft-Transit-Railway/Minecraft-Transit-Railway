package mtr.render;

import com.mojang.text2speech.Narrator;
import mtr.config.Config;
import mtr.config.CustomResources;
import mtr.data.*;
import mtr.gui.ClientData;
import mtr.gui.IDrawing;
import mtr.item.ItemRailModifier;
import mtr.model.*;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.MinecartEntityModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.MinecartEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class RenderTrains implements IGui {

	public static int maxTrainRenderDistance;

	private static final int DETAIL_RADIUS = 32;
	private static final int DETAIL_RADIUS_SQUARED = DETAIL_RADIUS * DETAIL_RADIUS;
	private static final int MAX_RADIUS_REPLAY_MOD = 64 * 16;

	private static final EntityModel<MinecartEntity> MODEL_MINECART = new MinecartEntityModel<>();
	private static final ModelSP1900 MODEL_SP1900 = new ModelSP1900(false);
	private static final ModelSP1900Mini MODEL_SP1900_MINI = new ModelSP1900Mini(false);
	private static final ModelSP1900 MODEL_C1141A = new ModelSP1900(true);
	private static final ModelSP1900Mini MODEL_C1141A_MINI = new ModelSP1900Mini(true);
	private static final ModelMTrain MODEL_M_TRAIN = new ModelMTrain();
	private static final ModelMTrainMini MODEL_M_TRAIN_MINI = new ModelMTrainMini();
	private static final ModelKTrain MODEL_K_TRAIN = new ModelKTrain();
	private static final ModelKTrainMini MODEL_K_TRAIN_MINI = new ModelKTrainMini();
	private static final ModelATrain MODEL_A_TRAIN_TCL = new ModelATrain(false);
	private static final ModelATrainMini MODEL_A_TRAIN_TCL_MINI = new ModelATrainMini(false);
	private static final ModelATrain MODEL_A_TRAIN_AEL = new ModelATrain(true);
	private static final ModelATrainMini MODEL_A_TRAIN_AEL_MINI = new ModelATrainMini(true);
	private static final ModelLightRail MODEL_LIGHT_RAIL_1 = new ModelLightRail(1);
	private static final ModelLightRail MODEL_LIGHT_RAIL_1R = new ModelLightRail(4);
	private static final ModelLightRail MODEL_LIGHT_RAIL_3 = new ModelLightRail(3);
	private static final ModelLightRail MODEL_LIGHT_RAIL_4 = new ModelLightRail(4);
	private static final ModelLightRail MODEL_LIGHT_RAIL_5 = new ModelLightRail(5);

	private static final String TEXTURE_PATH_RAIL = "textures/block/rail.png";

	private static class BlockTextureCache {
		public String textureFile;
		public BlockColorProvider provider;
		public BlockState defaultState;

		public BlockTextureCache(String s, BlockColorProvider provider, BlockState state) {
			textureFile = s; this.provider = provider; this.defaultState = state;
		}

		public int getTint(World world, BlockPos pos) {
			if (provider == null) return 0xffffff;
			return provider.getColor(defaultState, world, pos, 0);
		}
	}
	private static final HashMap<String, BlockTextureCache> blockTextureCacheMap = new HashMap<>();

	public static void render(World world, MatrixStack matrices, VertexConsumerProvider vertexConsumers, Vec3d cameraPos) {
		final MinecraftClient client = MinecraftClient.getInstance();
		final ClientPlayerEntity player = client.player;
		if (player == null) {
			return;
		}

		final int renderDistanceChunks = client.options.viewDistance;
		final boolean isReplayMod = isReplayMod(player);
		final float lastFrameDuration = isReplayMod ? 20F / 60 : client.getLastFrameDuration();

		if (Config.useDynamicFPS()) {
			if (lastFrameDuration > 0.8) {
				maxTrainRenderDistance = Math.max(maxTrainRenderDistance - (maxTrainRenderDistance - DETAIL_RADIUS) / 2, DETAIL_RADIUS);
			} else if (lastFrameDuration < 0.5) {
				maxTrainRenderDistance = Math.min(maxTrainRenderDistance + 1, renderDistanceChunks * 8);
			}
		} else {
			maxTrainRenderDistance = renderDistanceChunks * 8;
		}

		final Vec3d cameraOffset = client.gameRenderer.getCamera().isThirdPerson() ? player.getCameraPosVec(client.getTickDelta()).subtract(cameraPos) : Vec3d.ZERO;

		ClientData.updateReferences();
		ClientData.schedulesForPlatform.clear();
		ClientData.sidings.forEach(siding -> siding.simulateTrain(client.player, client.isPaused() ? 0 : lastFrameDuration, null, (x, y, z, yaw, pitch, customId, trainType, isEnd1Head, isEnd2Head, head1IsFront, doorLeftValue, doorRightValue, opening, lightsOn, offsetRender) -> renderWithLight(world, x, y, z, cameraPos.add(cameraOffset), player, offsetRender, (light, posAverage) -> {
			final ModelTrainBase model = getModel(trainType);

			matrices.push();
			if (offsetRender) {
				matrices.translate(x + cameraOffset.x, y + cameraOffset.y, z + cameraOffset.z);
			} else {
				matrices.translate(x - cameraPos.x, y - cameraPos.y, z - cameraPos.z);
			}
			matrices.multiply(Vec3f.POSITIVE_Y.getRadialQuaternion((float) Math.PI + yaw));
			matrices.multiply(Vec3f.POSITIVE_X.getRadialQuaternion((float) Math.PI + pitch));

			if (model == null) {
				matrices.translate(0, 0.5, 0);
				matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(90));
				final VertexConsumer vertexConsumer = vertexConsumers.getBuffer(MODEL_MINECART.getLayer(new Identifier("textures/entity/minecart.png")));
				MODEL_MINECART.setAngles(null, 0, 0, -0.1F, 0, 0);
				MODEL_MINECART.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1);
			} else {
				model.render(matrices, vertexConsumers, getTrainTexture(customId, trainType.id), light, doorLeftValue, doorRightValue, opening, isEnd1Head, isEnd2Head, head1IsFront, lightsOn, isReplayMod || posAverage.getSquaredDistance(player.getBlockPos()) <= DETAIL_RADIUS_SQUARED);
			}

			matrices.pop();
		}), (prevPos1, prevPos2, prevPos3, prevPos4, thisPos1, thisPos2, thisPos3, thisPos4, x, y, z, trainType, lightsOn, offsetRender) -> renderWithLight(world, x, y, z, cameraPos.add(cameraOffset), player, offsetRender, (light, posAverage) -> {
			matrices.push();
			if (offsetRender) {
				matrices.translate(cameraOffset.x, cameraOffset.y, cameraOffset.z);
			} else {
				matrices.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
			}

			final VertexConsumer vertexConsumerExterior = vertexConsumers.getBuffer(MoreRenderLayers.getExterior(new Identifier(getConnectorTextureString(trainType.id, "exterior"))));
			drawTexture(matrices, vertexConsumerExterior, thisPos2, prevPos3, prevPos4, thisPos1, light);
			drawTexture(matrices, vertexConsumerExterior, prevPos2, thisPos3, thisPos4, prevPos1, light);
			drawTexture(matrices, vertexConsumerExterior, prevPos3, thisPos2, thisPos3, prevPos2, light);
			drawTexture(matrices, vertexConsumerExterior, prevPos1, thisPos4, thisPos1, prevPos4, light);

			final int lightOnLevel = lightsOn ? MAX_LIGHT_INTERIOR : light;
			final VertexConsumer vertexConsumerSide = vertexConsumers.getBuffer(MoreRenderLayers.getInterior(new Identifier(getConnectorTextureString(trainType.id, "side"))));
			drawTexture(matrices, vertexConsumerSide, thisPos3, prevPos2, prevPos1, thisPos4, lightOnLevel);
			drawTexture(matrices, vertexConsumerSide, prevPos3, thisPos2, thisPos1, prevPos4, lightOnLevel);
			drawTexture(matrices, vertexConsumers.getBuffer(MoreRenderLayers.getInterior(new Identifier(getConnectorTextureString(trainType.id, "roof")))), prevPos2, thisPos3, thisPos2, prevPos3, lightOnLevel);
			drawTexture(matrices, vertexConsumers.getBuffer(MoreRenderLayers.getInterior(new Identifier(getConnectorTextureString(trainType.id, "floor")))), prevPos4, thisPos1, thisPos4, prevPos1, lightOnLevel);

			matrices.pop();
		}), (speed, stopIndex, routeIds) -> {
			if (!(speed <= 5 && useRoutesAndStationsFromIndex(stopIndex, routeIds, (thisRoute, nextRoute, thisStation, nextStation, lastStation) -> {
				final Text text;
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
				player.sendMessage(text, true);
			}))) {
				player.sendMessage(new TranslatableText("gui.mtr.train_speed", Math.round(speed * 10) / 10F, Math.round(speed * 36) / 10F), true);
			}
		}, (stopIndex, routeIds) -> {
			final boolean showAnnouncementMessages = Config.showAnnouncementMessages();
			final boolean useTTSAnnouncements = Config.useTTSAnnouncements();

			if (showAnnouncementMessages || useTTSAnnouncements) {
				useRoutesAndStationsFromIndex(stopIndex, routeIds, (thisRoute, nextRoute, thisStation, nextStation, lastStation) -> {
					final List<String> messages = new ArrayList<>();
					final String fullstopCJK = new TranslatableText("gui.mtr.fullstop_cjk").getString() + " ";
					final String fullstop = new TranslatableText("gui.mtr.fullstop").getString() + " ";

					if (nextStation != null) {
						messages.add(IGui.addToStationName(nextStation.name, new TranslatableText("gui.mtr.next_station_announcement_cjk").getString(), new TranslatableText("gui.mtr.next_station_announcement").getString(), fullstopCJK, fullstop));

						final Map<Integer, ClientData.ColorNamePair> routesInStation = ClientData.routesInStation.get(nextStation.id);
						if (routesInStation != null) {
							final List<String> interchangeRoutes = routesInStation.values().stream().filter(interchangeRoute -> !interchangeRoute.name.split("\\|\\|")[0].equals(thisRoute.name.split("\\|\\|")[0])).map(interchangeRoute -> interchangeRoute.name).collect(Collectors.toList());
							final String mergedStations = IGui.mergeStations(interchangeRoutes).replace(new TranslatableText("gui.mtr.separator_cjk").getString(), ", ").replace(new TranslatableText("gui.mtr.separator").getString(), ", ");
							if (!mergedStations.isEmpty()) {
								messages.add(IGui.addToStationName(mergedStations, new TranslatableText("gui.mtr.interchange_announcement_cjk").getString(), new TranslatableText("gui.mtr.interchange_announcement").getString(), fullstopCJK, fullstop));
							}
						}
					}

					final String message = IGui.formatStationName(IGui.mergeStations(messages).replace(new TranslatableText("gui.mtr.separator_cjk").getString(), "").replace(new TranslatableText("gui.mtr.separator").getString(), "")).replace("  ", " ");
					if (useTTSAnnouncements) {
						Narrator.getNarrator().say(message, false);
					}
					if (showAnnouncementMessages) {
						player.sendMessage(Text.of(message), false);
					}
				});
			}
		}, (platformId, arrivalMillis, departureMillis, trainType, stopIndex, routeIds) -> useRoutesAndStationsFromIndex(stopIndex, routeIds, (thisRoute, nextRoute, thisStation, nextStation, lastStation) -> {
			if (lastStation != null) {
				if (!ClientData.schedulesForPlatform.containsKey(platformId)) {
					ClientData.schedulesForPlatform.put(platformId, new HashSet<>());
				}
				ClientData.schedulesForPlatform.get(platformId).add(new Route.ScheduleEntry(arrivalMillis, departureMillis, trainType, platformId, lastStation.name, nextStation == null));
			}
		})));

		matrices.translate(-cameraPos.x, 0.0625 + SMALL_OFFSET - cameraPos.y, -cameraPos.z);
		final boolean renderColors = player.isHolding(item -> item instanceof ItemRailModifier);
		final int maxRailDistance = renderDistanceChunks * 16;

		final ArrayList<Rail> dynamicRenderRailList = new ArrayList<>();
		ClientData.rails.forEach((startPos, railMap) -> railMap.forEach((endPos, rail) -> {
			// Save some memory?
			if (!RailwayData.isBetween(player.getX(), startPos.getX(), endPos.getX(), maxRailDistance * 2) || !RailwayData.isBetween(player.getZ(), startPos.getZ(), endPos.getZ(), maxRailDistance * 2)) {
				rail.quadCache.reset();
				return;
			}
			if (!RailwayData.isBetween(player.getX(), startPos.getX(), endPos.getX(), maxRailDistance) || !RailwayData.isBetween(player.getZ(), startPos.getZ(), endPos.getZ(), maxRailDistance)) {
				return;
			}

			final BlockTextureCache ballastBlockMetadata = getBlockTextureMetadata(rail.ballastTexture);
			if (renderColors && rail.railType == RailType.NONE) {
				dynamicRenderRailList.add(rail);
			}

			rail.quadCache.renderWithCache(vertexConsumers, matrices, world, (cache) -> rail.render((h, k, r, t1, t2, y1, y2, isStraight, isEnd) -> {
				final int yf = (int)Math.floor(Math.min(y1, y2));
				final int yc = (int) Math.ceil(Math.max(y1, y2));
				final Pos3f rc1 = Rail.getPositionXZ(h, k, r, t1, -1, isStraight);
				final Pos3f rc2 = Rail.getPositionXZ(h, k, r, t1, 1, isStraight);
				final Pos3f rc3 = Rail.getPositionXZ(h, k, r, t2, 1, isStraight);
				final Pos3f rc4 = Rail.getPositionXZ(h, k, r, t2, -1, isStraight);
				if (shouldNotRender(player, new BlockPos(rc1.x, y1, rc1.z), maxRailDistance)) {
					return;
				}
				final Pos3f bc1 = Rail.getPositionXZ(h, k, r, t1, -1.5F, isStraight);
				final Pos3f bc2 = Rail.getPositionXZ(h, k, r, t1, 1.5F, isStraight);
				final Pos3f bc3 = Rail.getPositionXZ(h, k, r, t2, 1.5F, isStraight);
				final Pos3f bc4 = Rail.getPositionXZ(h, k, r, t2, -1.5F, isStraight);
				final float dY = 0.0625F /*+ SMALL_OFFSET*/;
				final float y1d = y1 - dY, y2d = y2 - dY;
				int alignment = getAxisAlignment(bc1, bc2, bc3, bc4);
				final float dV = Math.abs(t2 - t1);
				BlockPos lightRefPos = new BlockPos(rc1.x, yc, rc1.z);

				final float textureOffset = (((int) (rc1.x + rc1.z)) % 4) * 0.25F;
				final int color = renderColors || rail.railType.hasSavedRail ? rail.railType.color : -1;
				final QuadCache.QuadCacheList vcRail = cache.withTexture(TEXTURE_PATH_RAIL, false);
				vcRail.addNonBlockFace(rc1.x, y1, rc1.z, rc2.x, y1 + SMALL_OFFSET, rc2.z, rc3.x, y2, rc3.z, rc4.x, y2 + SMALL_OFFSET, rc4.z, 0, 0.1875F + textureOffset, 1, 0.3125F + textureOffset, Direction.UP, color, lightRefPos);
				vcRail.addNonBlockFace(rc4.x, y2 + SMALL_OFFSET, rc4.z, rc3.x, y2, rc3.z, rc2.x, y1 + SMALL_OFFSET, rc2.z, rc1.x, y1, rc1.z, 0, 0.1875F + textureOffset, 1, 0.3125F + textureOffset, Direction.UP, color, lightRefPos);

				if (ballastBlockMetadata != null) {
					final QuadCache.QuadCacheList vcBallast = cache.withTexture(ballastBlockMetadata.textureFile, true);
					final int tint = ballastBlockMetadata.getTint(world, lightRefPos);
					vcBallast.addNonBlockFace(bc1.x, y1d, bc1.z, bc2.x, y1d + SMALL_OFFSET / 3, bc2.z,
							bc3.x, y2d, bc3.z, bc4.x, y2d + SMALL_OFFSET / 3, bc4.z, 0, 0, 3, dV, Direction.UP,
							0xFF000000 | tint, lightRefPos);

					if (rail.isStraight() && !rail.isFlat()) {
						final float xmin = Math.min(Math.min(bc1.x, bc2.x), bc3.x);
						final float zmin = Math.min(Math.min(bc1.z, bc2.z), bc3.z);
						final float xmax = Math.max(Math.max(bc1.x, bc2.x), bc3.x);
						final float zmax = Math.max(Math.max(bc1.z, bc2.z), bc3.z);
						final int xblock = (int) Math.floor(xmin);
						final int zblock = (int) Math.floor(zmin);
						final float dxmin = xmin - xblock, dxmax = xmax - xblock, dzmin = zmin - zblock, dzmax = zmax - zblock;
						if (alignment == 1) {
							final float yl = bc1.z < bc3.z ? y1 : y2;
							final float ym = bc1.z < bc3.z ? y2 : y1;
							for (int i = 0; i < 3; i++) {
								drawSlopeBlock(vcBallast, new BlockPos(xblock + i, yf, zblock),
										yl, ym, ym, yl, 0, dzmin, 1, dzmax, alignment, isEnd, i, tint);
							}
						} else if (alignment == 2) {
							final float yl = bc1.x < bc3.x ? y1 : y2;
							final float ym = bc1.x < bc3.x ? y2 : y1;
							for (int i = 0; i < 3; i++) {
								drawSlopeBlock(vcBallast, new BlockPos(xblock, yf, zblock + i),
										yl, yl, ym, ym, dxmin, 0, dxmax, 1, alignment, isEnd, i, tint);
							}
						}
					}
				}
			}));
		}));
		dynamicRenderRailList.forEach((rail) -> {
			rail.render((h, k, r, t1, t2, y1, y2, isStraight, isEnd) -> {
				final int yf = (int)Math.floor(Math.min(y1, y2));
				final int yc = (int) Math.ceil(Math.max(y1, y2));
				final Pos3f rc1 = Rail.getPositionXZ(h, k, r, t1, -1, isStraight);
				final Pos3f rc2 = Rail.getPositionXZ(h, k, r, t1, 1, isStraight);
				final Pos3f rc3 = Rail.getPositionXZ(h, k, r, t2, 1, isStraight);
				final Pos3f rc4 = Rail.getPositionXZ(h, k, r, t2, -1, isStraight);
				if (shouldNotRender(player, new BlockPos(rc1.x, y1, rc1.z), maxRailDistance)) {
					return;
				}
				BlockPos lightRefPos = new BlockPos(rc1.x, yc, rc1.z);
				final int lightRail = WorldRenderer.getLightmapCoordinates(world, lightRefPos);

				if (rail.railType == RailType.NONE && renderColors) {
					final VertexConsumer vcRailArrow = vertexConsumers.getBuffer(MoreRenderLayers.getExterior(new Identifier("mtr:textures/block/one_way_rail_arrow.png")));
					IDrawing.drawTexture(matrices, vcRailArrow, rc1.x, y1, rc1.z, rc2.x, y1 + SMALL_OFFSET * 2, rc2.z, rc3.x, y2, rc3.z, rc4.x, y2 + SMALL_OFFSET * 2, rc4.z, 0, 0.25F, 1, 0.75F, Direction.UP, -1, lightRail);
					IDrawing.drawTexture(matrices, vcRailArrow, rc2.x, y1 + SMALL_OFFSET * 2, rc2.z, rc1.x, y1, rc1.z, rc4.x, y2 + SMALL_OFFSET * 2, rc4.z, rc3.x, y2, rc3.z, 0, 0.25F, 1, 0.75F, Direction.UP, -1, lightRail);
				}
			});
		});
	}

	public static boolean shouldNotRender(PlayerEntity player, BlockPos pos, int maxDistance) {
		return player == null || player.getBlockPos().getManhattanDistance(pos) > (isReplayMod(player) ? MAX_RADIUS_REPLAY_MOD : maxDistance);
	}

	public static boolean shouldNotRender(BlockPos pos, int maxDistance) {
		final PlayerEntity player = MinecraftClient.getInstance().player;
		return shouldNotRender(player, pos, maxDistance);
	}

	public static boolean useRoutesAndStationsFromIndex(int stopIndex, List<Long> routeIds, RouteAndStationsCallback routeAndStationsCallback) {
		if (stopIndex < 0) {
			return false;
		}

		int sum = 0;
		for (int i = 0; i < routeIds.size(); i++) {
			final Route thisRoute = ClientData.routeIdMap.get(routeIds.get(i));
			final Route nextRoute = i < routeIds.size() - 1 ? ClientData.routeIdMap.get(routeIds.get(i + 1)) : null;
			if (thisRoute != null) {
				final int difference = stopIndex - sum;
				sum += thisRoute.platformIds.size();
				if (!thisRoute.platformIds.isEmpty() && nextRoute != null && !nextRoute.platformIds.isEmpty() && thisRoute.platformIds.get(thisRoute.platformIds.size() - 1).equals(nextRoute.platformIds.get(0))) {
					sum--;
				}
				if (stopIndex < sum) {
					final Station thisStation = ClientData.platformIdToStation.get(thisRoute.platformIds.get(difference));
					final Station nextStation = difference < thisRoute.platformIds.size() - 1 ? ClientData.platformIdToStation.get(thisRoute.platformIds.get(difference + 1)) : null;
					final Station lastStation = thisRoute.platformIds.isEmpty() ? null : ClientData.platformIdToStation.get(thisRoute.platformIds.get(thisRoute.platformIds.size() - 1));
					routeAndStationsCallback.routeAndStationsCallback(thisRoute, nextRoute, thisStation, nextStation, lastStation);
					return true;
				}
			}
		}
		return false;
	}

	private static void renderWithLight(World world, float x, float y, float z, Vec3d cameraPos, PlayerEntity player, boolean offsetRender, RenderCallback renderCallback) {
		final BlockPos posAverage = offsetRender ? new BlockPos(cameraPos).add(x, y, z) : new BlockPos(x, y, z);
		if (!shouldNotRender(player, posAverage, MinecraftClient.getInstance().options.viewDistance * 8)) {
			renderCallback.renderCallback(LightmapTextureManager.pack(world.getLightLevel(LightType.BLOCK, posAverage), world.getLightLevel(LightType.SKY, posAverage)), posAverage);
		}
	}

	private static Text getStationText(Station station, String textKey) {
		if (station != null) {
			return Text.of(IGui.formatStationName(IGui.addToStationName(IGui.textOrUntitled(station.name), new TranslatableText("gui.mtr." + textKey + "_station_cjk").getString(), new TranslatableText("gui.mtr." + textKey + "_station").getString(), "", "")));
		} else {
			return new LiteralText("");
		}
	}

	private static void drawTexture(MatrixStack matrices, VertexConsumer vertexConsumer, Pos3f pos1, Pos3f pos2, Pos3f pos3, Pos3f pos4, int light) {
		IDrawing.drawTexture(matrices, vertexConsumer, pos1.x, pos1.y, pos1.z, pos2.x, pos2.y, pos2.z, pos3.x, pos3.y, pos3.z, pos4.x, pos4.y, pos4.z, 0, 0, 1, 1, Direction.UP, -1, light);
	}

	private static boolean isReplayMod(PlayerEntity player) {
		if (player == null) {
			return false;
		} else {
			return player.getClass().toGenericString().toLowerCase().contains("replaymod");
		}
	}

	private static Identifier getTrainTexture(String customId, String trainId) {
		if (customId.isEmpty() || !CustomResources.customTrains.containsKey(customId)) {
			return new Identifier("mtr:textures/entity/" + trainId + ".png");
		} else {
			return CustomResources.customTrains.get(customId).textureId;
		}
	}

	private static String getConnectorTextureString(String trainId, String connectorPart) {
		return "mtr:textures/entity/" + trainId + "_connector_" + connectorPart + ".png";
	}

	private static ModelTrainBase getModel(TrainType trainType) {
		switch (trainType) {
			case SP1900:
				return MODEL_SP1900;
			case C1141A:
				return MODEL_C1141A;
			case SP1900_MINI:
				return MODEL_SP1900_MINI;
			case C1141A_MINI:
				return MODEL_C1141A_MINI;
			case M_TRAIN:
				return MODEL_M_TRAIN;
			case M_TRAIN_MINI:
				return MODEL_M_TRAIN_MINI;
			case K_TRAIN:
				return MODEL_K_TRAIN;
			case K_TRAIN_MINI:
				return MODEL_K_TRAIN_MINI;
			case A_TRAIN_TCL:
				return MODEL_A_TRAIN_TCL;
			case A_TRAIN_TCL_MINI:
				return MODEL_A_TRAIN_TCL_MINI;
			case A_TRAIN_AEL:
				return MODEL_A_TRAIN_AEL;
			case A_TRAIN_AEL_MINI:
				return MODEL_A_TRAIN_AEL_MINI;
			case LIGHT_RAIL_1:
				return MODEL_LIGHT_RAIL_1;
			case LIGHT_RAIL_1R:
				return MODEL_LIGHT_RAIL_1R;
			case LIGHT_RAIL_3:
				return MODEL_LIGHT_RAIL_3;
			case LIGHT_RAIL_4:
				return MODEL_LIGHT_RAIL_4;
			case LIGHT_RAIL_5:
				return MODEL_LIGHT_RAIL_5;
			default:
				return null;
		}
	}

	// l: less, m: more, lm: the corner of a block where x is minimum and z is maximum (i.e. southwest)

	private static void drawSlopeBlock(QuadCache.QuadCacheList cacheList, BlockPos pos,
		   float yll, float ylm, float ymm, float yml, float dxl, float dzl, float dxm, float dzm,
		   int alignment, boolean isEnd, int step, int color) {
		// if (!blockState.isAir() && !(blockState.getBlock() instanceof mtr.block.BlockRail)) return;
		float yf = pos.getY();
		/* IDrawing.drawBlockFace(
				matrices, vertexConsumer,
				dxl, yll - yf, dzl, dxl, ylm - yf, dzm,
				dxm, ymm - yf, dzm, dxm, yml - yf, dzl,
				dxl, dzl, dxl, dzm, dxm, dzm, dxm, dzl,
				Direction.UP, pos, world
		); */
		if (isEnd) {
			cacheList.addBlockFace(
					dxl, 0, dzl, dxm, 0, dzl,
					dxm, 0, dzm, dxl, 0, dzm,
					dxl, dzl, dxm, dzl, dxm, dzm, dxl, dzm,
					Direction.DOWN, pos, color
			);
		}
		if ((alignment == 1 && step == 0) || isEnd) {
			cacheList.addBlockFace(
					dxl, yll - yf, dzl, dxl, 0, dzl,
					dxl, 0, dzm, dxl, ylm - yf, dzm,
					dzl, 1 - (yll - yf), dzl, 1, dzm, 1, dzm, 1 - (ylm - yf),
					Direction.WEST, pos, color
			);
		}
		if ((alignment == 1 && step == 2) || isEnd) {
			cacheList.addBlockFace(
					dxm, ymm - yf, dzm, dxm, 0, dzm,
					dxm, 0, dzl, dxm, yml - yf, dzl,
					dzm, 1 - (ymm - yf), dzm, 1, dzl, 1, dzl, 1 - (yml - yf),
					Direction.EAST, pos, color
			);
		}
		if ((alignment == 2 && step == 2) || isEnd) {
			cacheList.addBlockFace(
					dxl, ylm - yf, dzm, dxl, 0, dzm,
					dxm, 0, dzm, dxm, ymm - yf, dzm,
					dxl, 1 - (ylm - yf), dxl, 1, dxm, 1, dxm, 1 - (ymm - yf),
					Direction.SOUTH, pos, color
			);
		}
		if ((alignment == 2 && step == 0) || isEnd) {
			cacheList.addBlockFace(
					dxm, yml - yf, dzl, dxm, 0, dzl,
					dxl, 0, dzl, dxl, yll - yf, dzl,
					dxm, 1 - (yml - yf), dxm, 1, dxl, 1, dxl, 1 - (yll - yf),
					Direction.NORTH, pos, color
			);
		}
	}

	private static int getAxisAlignment(Pos3f c1, Pos3f c2, Pos3f c3, Pos3f c4) {
		if (c1.x == c2.x) {
			return c3.x == c4.x && c1.z == c4.z && c2.z == c3.z ? 2 : 0;
		} else if (c1.z == c2.z) {
			return c3.z == c4.z && c1.x == c4.x && c2.x == c3.x ? 1 : 0;
		} else {
			return 0;
		}
	}

	private static BlockTextureCache getBlockTextureMetadata(String blockName) {
		if (!blockName.isEmpty()) {
			if (blockTextureCacheMap.containsKey(blockName)) {
				return blockTextureCacheMap.get(blockName);
			} else {
				final Block ballastBlock = Registry.BLOCK.get(new Identifier(blockName));
				if (ballastBlock != Blocks.AIR) {
					final BlockColorProvider provider = ColorProviderRegistry.BLOCK.get(ballastBlock);
					final BlockState ballastBlockState = ballastBlock.getDefaultState();
					final List<BakedQuad> quads = MinecraftClient.getInstance().getBlockRenderManager().getModel(ballastBlockState)
							.getQuads(ballastBlockState, Direction.UP, new Random(0));
					if (quads.size() > 0) {
						BlockTextureCache bbm = new BlockTextureCache("textures/" + quads.get(0).sprite.getId().getPath() + ".png",
								provider, ballastBlockState);
						blockTextureCacheMap.put(blockName, bbm);
						return bbm;
					}
				}
			}
		} else {
			// For benchmark enable this
			return new BlockTextureCache("textures/block/gravel.png", null, Blocks.GRAVEL.getDefaultState());
		}
		return null;
	}

	@FunctionalInterface
	public interface RouteAndStationsCallback {
		void routeAndStationsCallback(Route thisRoute, Route nextRoute, Station thisStation, Station nextStation, Station lastStation);
	}

	@FunctionalInterface
	private interface RenderCallback {
		void renderCallback(int light, BlockPos posAverage);
	}
}
