package mtr.render;

import com.mojang.text2speech.Narrator;
import mtr.MTRClient;
import mtr.config.Config;
import mtr.config.CustomResources;
import mtr.data.*;
import mtr.gui.ClientData;
import mtr.gui.IDrawing;
import mtr.item.ItemRailModifier;
import mtr.model.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.MinecartEntityModel;
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
import net.minecraft.world.LightType;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RenderTrains implements IGui {

	public static int maxTrainRenderDistance;
	private static float gameTick = 0;

	private static final int DETAIL_RADIUS = 32;
	private static final int DETAIL_RADIUS_SQUARED = DETAIL_RADIUS * DETAIL_RADIUS;
	private static final int MAX_RADIUS_REPLAY_MOD = 64 * 16;

	private static final EntityModel<MinecartEntity> MODEL_MINECART = new MinecartEntityModel<>(MinecartEntityModel.getTexturedModelData().createModel());
	private static final ModelSP1900 MODEL_SP1900 = new ModelSP1900(false);
	private static final ModelSP1900Mini MODEL_SP1900_MINI = new ModelSP1900Mini(false);
	private static final ModelSP1900 MODEL_C1141A = new ModelSP1900(true);
	private static final ModelSP1900Mini MODEL_C1141A_MINI = new ModelSP1900Mini(true);
	private static final ModelMLR MODEL_MLR = new ModelMLR();
	private static final ModelMLRMini MODEL_MLR_MINI = new ModelMLRMini();
	private static final ModelMTrain MODEL_M_TRAIN = new ModelMTrain();
	private static final ModelMTrainMini MODEL_M_TRAIN_MINI = new ModelMTrainMini();
	private static final ModelKTrain MODEL_K_TRAIN = new ModelKTrain(false);
	private static final ModelKTrainMini MODEL_K_TRAIN_MINI = new ModelKTrainMini(false);
	private static final ModelKTrain MODEL_K_TRAIN_TCL = new ModelKTrain(true);
	private static final ModelKTrainMini MODEL_K_TRAIN_TCL_MINI = new ModelKTrainMini(true);
	private static final ModelKTrain MODEL_K_TRAIN_AEL = new ModelKTrain(true);
	private static final ModelKTrainMini MODEL_K_TRAIN_AEL_MINI = new ModelKTrainMini(true);
	private static final ModelATrain MODEL_A_TRAIN_TCL = new ModelATrain(false);
	private static final ModelATrainMini MODEL_A_TRAIN_TCL_MINI = new ModelATrainMini(false);
	private static final ModelATrain MODEL_A_TRAIN_AEL = new ModelATrain(true);
	private static final ModelATrainMini MODEL_A_TRAIN_AEL_MINI = new ModelATrainMini(true);
	private static final ModelR179Train MODEL_R179 = new ModelR179Train();
	private static final ModelLightRail MODEL_LIGHT_RAIL_1 = new ModelLightRail(1);
	private static final ModelLightRail MODEL_LIGHT_RAIL_1R = new ModelLightRail(4);
	private static final ModelLightRail MODEL_LIGHT_RAIL_2 = new ModelLightRail(2);
	private static final ModelLightRail MODEL_LIGHT_RAIL_3 = new ModelLightRail(3);
	private static final ModelLightRail MODEL_LIGHT_RAIL_4 = new ModelLightRail(4);
	private static final ModelLightRail MODEL_LIGHT_RAIL_5 = new ModelLightRail(5);
	private static final ModelE44 MODEL_E_44 = new ModelE44();
	private static final ModelE44Mini MODEL_E_44_MINI = new ModelE44Mini();

	public static void render(World world, MatrixStack matrices, VertexConsumerProvider vertexConsumers, Camera camera) {
		final MinecraftClient client = MinecraftClient.getInstance();
		final ClientPlayerEntity player = client.player;
		if (player == null) {
			return;
		}

		final int renderDistanceChunks = client.options.viewDistance;
		final float lastFrameDuration = MTRClient.isReplayMod ? 20F / 60 : client.getLastFrameDuration();
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

		final Vec3d cameraPos = camera.getPos();
		final float cameraYaw = camera.getYaw();
		final Vec3d cameraOffset = client.gameRenderer.getCamera().isThirdPerson() ? player.getCameraPosVec(client.getTickDelta()).subtract(cameraPos) : Vec3d.ZERO;

		ClientData.TRAINS.forEach(train -> train.render(world, client.isPaused() ? 0 : lastFrameDuration, (x, y, z, yaw, pitch, customId, trainType, isEnd1Head, isEnd2Head, head1IsFront, doorLeftValue, doorRightValue, opening, lightsOn, playerOffset) -> renderWithLight(world, x, y, z, cameraPos.add(cameraOffset), player, playerOffset != null, (light, posAverage) -> {
			final ModelTrainBase model = getModel(trainType);

			matrices.push();
			if (playerOffset == null) {
				matrices.translate(x - cameraPos.x, y - cameraPos.y, z - cameraPos.z);
			} else {
				matrices.translate(cameraOffset.x, cameraOffset.y, cameraOffset.z);
				matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(player.getYaw() - cameraYaw));
				matrices.translate(x - playerOffset.x, y - playerOffset.y, z - playerOffset.z);
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
				model.render(matrices, vertexConsumers, getTrainTexture(customId, trainType.id), light, doorLeftValue, doorRightValue, opening, isEnd1Head, isEnd2Head, head1IsFront, lightsOn, MTRClient.isReplayMod || posAverage.getSquaredDistance(player.getBlockPos()) <= DETAIL_RADIUS_SQUARED);
			}

			matrices.pop();
		}), (prevPos1, prevPos2, prevPos3, prevPos4, thisPos1, thisPos2, thisPos3, thisPos4, x, y, z, yaw, trainType, lightsOn, playerOffset) -> renderWithLight(world, x, y, z, cameraPos.add(cameraOffset), player, playerOffset != null, (light, posAverage) -> {
			matrices.push();
			if (playerOffset == null) {
				matrices.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
			} else {
				matrices.translate(cameraOffset.x, cameraOffset.y, cameraOffset.z);
				matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(player.getYaw() - cameraYaw));
				matrices.translate(-playerOffset.x, -playerOffset.y, -playerOffset.z);
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
			if (!(speed <= 5 && RailwayData.useRoutesAndStationsFromIndex(stopIndex, routeIds, ClientData.getDataCache(), (thisRoute, nextRoute, thisStation, nextStation, lastStation) -> {
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

			if (showAnnouncementMessages || useTTSAnnouncements) {
				RailwayData.useRoutesAndStationsFromIndex(stopIndex, routeIds, ClientData.getDataCache(), (thisRoute, nextRoute, thisStation, nextStation, lastStation) -> {
					final List<String> messages = new ArrayList<>();
					final String thisRouteSplit = thisRoute.name.split("\\|\\|")[0];
					final String nextRouteSplit = nextRoute == null ? null : nextRoute.name.split("\\|\\|")[0];

					if (nextStation != null) {
						final boolean isLightRailRoute = thisRoute.isLightRailRoute;
						messages.add(IGui.insertTranslation(isLightRailRoute ? "gui.mtr.next_station_light_rail_announcement_cjk" : "gui.mtr.next_station_announcement_cjk", isLightRailRoute ? "gui.mtr.next_station_light_rail_announcement" : "gui.mtr.next_station_announcement", 1, nextStation.name));

						final Map<Integer, DataCache.ColorNamePair> routesInStation = ClientData.getDataCache().stationIdToRoutes.get(nextStation.id);
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
							final Station nextFinalStation = ClientData.getDataCache().platformIdToStation.get(nextRoute.platformIds.get(nextRoute.platformIds.size() - 1));
							if (nextFinalStation != null) {
								if (nextRoute.isLightRailRoute) {
									messages.add(IGui.insertTranslation("gui.mtr.next_route_light_rail_announcement_cjk", "gui.mtr.next_route_light_rail_announcement", nextRoute.lightRailRouteNumber, 1, nextFinalStation.name.split("\\|\\|")[0]));
								} else {
									messages.add(IGui.insertTranslation("gui.mtr.next_route_announcement_cjk", "gui.mtr.next_route_announcement", 2, nextRouteSplit, nextFinalStation.name.split("\\|\\|")[0]));
								}
							}
						}
					}

					final String message = IGui.formatStationName(IGui.mergeStations(messages, " ")).replace("  ", " ");
					if (!message.isEmpty()) {
						if (useTTSAnnouncements) {
							Narrator.getNarrator().say(message, true);
						}
						if (showAnnouncementMessages) {
							player.sendMessage(Text.of(message), false);
						}
					}
				});
			}
		}, (stopIndex, routeIds) -> {
			if (useTTSAnnouncements) {
				RailwayData.useRoutesAndStationsFromIndex(stopIndex, routeIds, ClientData.getDataCache(), (thisRoute, nextRoute, thisStation, nextStation, lastStation) -> {
					if (thisRoute.isLightRailRoute && lastStation != null) {
						Narrator.getNarrator().say(IGui.insertTranslation("gui.mtr.light_rail_route_announcement_cjk", "gui.mtr.light_rail_route_announcement", thisRoute.lightRailRouteNumber.replace("", " "), 1, lastStation.name), true);
					}
				});
			}
		}));

		matrices.translate(-cameraPos.x, 0.0625 + SMALL_OFFSET - cameraPos.y, -cameraPos.z);
		final boolean renderColors = player.isHolding(itemStack -> itemStack.getItem() instanceof ItemRailModifier);
		final int maxRailDistance = renderDistanceChunks * 16;
		ClientData.RAILS.forEach((startPos, railMap) -> railMap.forEach((endPos, rail) -> {
			if (!RailwayData.isBetween(player.getX(), startPos.getX(), endPos.getX(), maxRailDistance) || !RailwayData.isBetween(player.getZ(), startPos.getZ(), endPos.getZ(), maxRailDistance)) {
				return;
			}

			rail.render((x1, z1, x2, z2, x3, z3, x4, z4, y1, y2) -> {
				if (shouldNotRender(player, new BlockPos(x1, y1, z1), maxRailDistance, null)) {
					return;
				}

				final BlockPos pos2 = new BlockPos(x1, y1, z1);
				final int light2 = LightmapTextureManager.pack(world.getLightLevel(LightType.BLOCK, pos2), world.getLightLevel(LightType.SKY, pos2));

				if (rail.railType == RailType.NONE) {
					if (renderColors) {
						final VertexConsumer vertexConsumerArrow = vertexConsumers.getBuffer(MoreRenderLayers.getExterior(new Identifier("mtr:textures/block/one_way_rail_arrow.png")));
						IDrawing.drawTexture(matrices, vertexConsumerArrow, (float) x1, (float) y1, (float) z1, (float) x2, (float) y1 + SMALL_OFFSET, (float) z2, (float) x3, (float) y2, (float) z3, (float) x4, (float) y2 + SMALL_OFFSET, (float) z4, 0, 0.25F, 1, 0.75F, Direction.UP, -1, light2);
						IDrawing.drawTexture(matrices, vertexConsumerArrow, (float) x2, (float) y1 + SMALL_OFFSET, (float) z2, (float) x1, (float) y1, (float) z1, (float) x4, (float) y2 + SMALL_OFFSET, (float) z4, (float) x3, (float) y2, (float) z3, 0, 0.25F, 1, 0.75F, Direction.UP, -1, light2);
					}
				} else {
					final float textureOffset = (((int) (x1 + z1)) % 4) * 0.25F;
					final int color = renderColors || rail.railType.hasSavedRail ? rail.railType.color : -1;

					final VertexConsumer vertexConsumer = vertexConsumers.getBuffer(MoreRenderLayers.getExterior(new Identifier("textures/block/rail.png")));
					IDrawing.drawTexture(matrices, vertexConsumer, (float) x1, (float) y1, (float) z1, (float) x2, (float) y1 + SMALL_OFFSET, (float) z2, (float) x3, (float) y2, (float) z3, (float) x4, (float) y2 + SMALL_OFFSET, (float) z4, 0, 0.1875F + textureOffset, 1, 0.3125F + textureOffset, Direction.UP, color, light2);
					IDrawing.drawTexture(matrices, vertexConsumer, (float) x4, (float) y2 + SMALL_OFFSET, (float) z4, (float) x3, (float) y2, (float) z3, (float) x2, (float) y1 + SMALL_OFFSET, (float) z2, (float) x1, (float) y1, (float) z1, 0, 0.1875F + textureOffset, 1, 0.3125F + textureOffset, Direction.UP, color, light2);
				}
			});
		}));
	}

	public static boolean shouldNotRender(PlayerEntity player, BlockPos pos, int maxDistance, Direction facing) {
		final boolean playerFacingAway;
		if (facing == null) {
			playerFacingAway = false;
		} else {
			if (facing.getAxis() == Direction.Axis.X) {
				final double playerXOffset = player.getX() - pos.getX() - 0.5;
				playerFacingAway = Math.signum(playerXOffset) == facing.getOffsetX() && Math.abs(playerXOffset) >= 0.5;
			} else {
				final double playerZOffset = player.getZ() - pos.getZ() - 0.5;
				playerFacingAway = Math.signum(playerZOffset) == facing.getOffsetZ() && Math.abs(playerZOffset) >= 0.5;
			}
		}
		return player == null || playerFacingAway || player.getBlockPos().getManhattanDistance(pos) > (MTRClient.isReplayMod ? MAX_RADIUS_REPLAY_MOD : maxDistance);
	}

	public static boolean shouldNotRender(BlockPos pos, int maxDistance, Direction facing) {
		final PlayerEntity player = MinecraftClient.getInstance().player;
		return shouldNotRender(player, pos, maxDistance, facing);
	}

	public static float getGameTicks() {
		return gameTick;
	}

	private static void renderWithLight(World world, double x, double y, double z, Vec3d cameraPos, PlayerEntity player, boolean offsetRender, RenderCallback renderCallback) {
		final BlockPos posAverage = offsetRender ? new BlockPos(cameraPos).add(x, y, z) : new BlockPos(x, y, z);
		if (!shouldNotRender(player, posAverage, MinecraftClient.getInstance().options.viewDistance * 8, null)) {
			renderCallback.renderCallback(LightmapTextureManager.pack(world.getLightLevel(LightType.BLOCK, posAverage), world.getLightLevel(LightType.SKY, posAverage)), posAverage);
		}
	}

	private static Text getStationText(Station station, String textKey) {
		if (station != null) {
			return Text.of(IGui.formatStationName(IGui.insertTranslation("gui.mtr." + textKey + "_station_cjk", "gui.mtr." + textKey + "_station", 1, IGui.textOrUntitled(station.name))));
		} else {
			return new LiteralText("");
		}
	}

	private static void drawTexture(MatrixStack matrices, VertexConsumer vertexConsumer, Vec3d pos1, Vec3d pos2, Vec3d pos3, Vec3d pos4, int light) {
		IDrawing.drawTexture(matrices, vertexConsumer, (float) pos1.x, (float) pos1.y, (float) pos1.z, (float) pos2.x, (float) pos2.y, (float) pos2.z, (float) pos3.x, (float) pos3.y, (float) pos3.z, (float) pos4.x, (float) pos4.y, (float) pos4.z, 0, 0, 1, 1, Direction.UP, -1, light);
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
			case SP1900_MINI:
				return MODEL_SP1900_MINI;
			case C1141A:
				return MODEL_C1141A;
			case C1141A_MINI:
				return MODEL_C1141A_MINI;
			case MLR:
				return MODEL_MLR;
			case MLR_MINI:
				return MODEL_MLR_MINI;
			case M_TRAIN:
				return MODEL_M_TRAIN;
			case M_TRAIN_MINI:
				return MODEL_M_TRAIN_MINI;
			case K_TRAIN:
				return MODEL_K_TRAIN;
			case K_TRAIN_MINI:
				return MODEL_K_TRAIN_MINI;
			case K_TRAIN_TCL:
				return MODEL_K_TRAIN_TCL;
			case K_TRAIN_TCL_MINI:
				return MODEL_K_TRAIN_TCL_MINI;
			case K_TRAIN_AEL:
				return MODEL_K_TRAIN_AEL;
			case K_TRAIN_AEL_MINI:
				return MODEL_K_TRAIN_AEL_MINI;
			case A_TRAIN_TCL:
				return MODEL_A_TRAIN_TCL;
			case A_TRAIN_TCL_MINI:
				return MODEL_A_TRAIN_TCL_MINI;
			case A_TRAIN_AEL:
				return MODEL_A_TRAIN_AEL;
			case A_TRAIN_AEL_MINI:
				return MODEL_A_TRAIN_AEL_MINI;
			case R179:
				return MODEL_R179;
			case LIGHT_RAIL_1:
				return MODEL_LIGHT_RAIL_1;
			case LIGHT_RAIL_1R:
				return MODEL_LIGHT_RAIL_1R;
			case LIGHT_RAIL_2:
				return MODEL_LIGHT_RAIL_2;
			case LIGHT_RAIL_3:
				return MODEL_LIGHT_RAIL_3;
			case LIGHT_RAIL_4:
				return MODEL_LIGHT_RAIL_4;
			case LIGHT_RAIL_5:
				return MODEL_LIGHT_RAIL_5;
			case E44:
				return MODEL_E_44;
			case E44_MINI:
				return MODEL_E_44_MINI;
			default:
				return null;
		}
	}

	@FunctionalInterface
	private interface RenderCallback {
		void renderCallback(int light, BlockPos posAverage);
	}
}
