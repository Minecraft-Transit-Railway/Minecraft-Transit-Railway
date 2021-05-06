package mtr.render;

import com.mojang.text2speech.Narrator;
import mtr.config.Config;
import mtr.config.CustomResources;
import mtr.data.*;
import mtr.entity.EntitySeat;
import mtr.gui.ClientData;
import mtr.gui.IGui;
import mtr.item.ItemRailModifier;
import mtr.model.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.MinecartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.MinecartEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameRules;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RenderSeat extends EntityRenderer<EntitySeat> implements IGui {

	private long nextStationId;
	private int announceTime;
	private String thisRouteName;

	public static int maxTrainRenderDistance;

	private static final int DETAIL_RADIUS_SQUARED = EntitySeat.DETAIL_RADIUS * EntitySeat.DETAIL_RADIUS;
	private static final int MAX_RADIUS_REPLAY_MOD = 64 * 16;
	private static final int ANNOUNCE_DELAY = 100;

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
	private static final ModelLightRail1 MODEL_LIGHT_RAIL_1 = new ModelLightRail1();

	public RenderSeat(EntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public void render(EntitySeat entity, float entityYaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int entityLight) {
		final MinecraftClient client = MinecraftClient.getInstance();
		final ClientPlayerEntity player = client.player;
		if (player == null || !isReplayMod(player) && player != entity.getPlayer()) {
			return;
		}
		final World world = entity.world;
		if (world == null) {
			return;
		}

		if (Config.useDynamicFPS()) {
			final float lastFrameDuration = client.getLastFrameDuration();
			if (lastFrameDuration > 0.8) {
				maxTrainRenderDistance = Math.max(maxTrainRenderDistance - (maxTrainRenderDistance - EntitySeat.DETAIL_RADIUS) / 2, EntitySeat.DETAIL_RADIUS);
			} else if (lastFrameDuration < 0.5) {
				maxTrainRenderDistance = Math.min(maxTrainRenderDistance + 1, MAX_RADIUS_REPLAY_MOD);
			}
		} else {
			maxTrainRenderDistance = client.options.viewDistance * 8;
		}

		matrices.push();
		final double entityX = MathHelper.lerp(tickDelta, entity.lastRenderX, entity.getX());
		final double entityY = MathHelper.lerp(tickDelta, entity.lastRenderY, entity.getY());
		final double entityZ = MathHelper.lerp(tickDelta, entity.lastRenderZ, entity.getZ());
		matrices.translate(-entityX, -entityY, -entityZ);

		final long worldTime = world.getLunarTime();

		try {
			ClientData.routes.forEach(route -> route.getPositionYaw(world, worldTime + (world.getGameRules().getBoolean(GameRules.DO_DAYLIGHT_CYCLE) ? tickDelta : 0), tickDelta, entity, (x, y, z, yaw, pitch, customId, trainType, isEnd1Head, isEnd2Head, doorLeftValue, doorRightValue, opening, shouldOffsetRender) -> {
				final double offsetX = x + (shouldOffsetRender ? entityX : 0);
				final double offsetY = y + (shouldOffsetRender ? entityY : 0);
				final double offsetZ = z + (shouldOffsetRender ? entityZ : 0);
				final BlockPos posAverage = new BlockPos(offsetX, offsetY, offsetZ);
				if (shouldNotRender(player, posAverage, maxTrainRenderDistance)) {
					return;
				}
				final int light = LightmapTextureManager.pack(world.getLightLevel(LightType.BLOCK, posAverage), world.getLightLevel(LightType.SKY, posAverage));
				final ModelTrainBase model = getModel(trainType);

				matrices.push();
				matrices.translate(offsetX, offsetY, offsetZ);
				matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(180 + yaw));
				matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(180 + pitch));

				if (model == null) {
					matrices.translate(0, 0.5, 0);
					matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(90));
					final VertexConsumer vertexConsumer = vertexConsumers.getBuffer(MODEL_MINECART.getLayer(new Identifier("textures/entity/minecart.png")));
					MODEL_MINECART.setAngles(null, 0, 0, -0.1F, 0, 0);
					MODEL_MINECART.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1);
				} else {
					model.render(matrices, vertexConsumers, getTrainTexture(customId, trainType.id), light, doorLeftValue, doorRightValue, opening, isEnd1Head, isEnd2Head, true, player.getPos().squaredDistanceTo(offsetX, offsetY, offsetZ) <= DETAIL_RADIUS_SQUARED);
				}

				matrices.pop();
			}, (prevPos1, prevPos2, prevPos3, prevPos4, thisPos1, thisPos2, thisPos3, thisPos4, x, y, z, trainType, shouldOffsetRender) -> {
				final double offsetX = x + (shouldOffsetRender ? entityX : 0);
				final double offsetY = y + (shouldOffsetRender ? entityY : 0);
				final double offsetZ = z + (shouldOffsetRender ? entityZ : 0);
				final BlockPos posAverage = new BlockPos(offsetX, offsetY, offsetZ);
				if (shouldNotRender(player, posAverage, maxTrainRenderDistance)) {
					return;
				}
				final int light = LightmapTextureManager.pack(world.getLightLevel(LightType.BLOCK, posAverage), world.getLightLevel(LightType.SKY, posAverage));

				final String connectorExteriorTexture = getConnectorTextureString(trainType.id, "exterior");
				final String connectorSideTexture = getConnectorTextureString(trainType.id, "side");
				final String connectorRoofTexture = getConnectorTextureString(trainType.id, "roof");
				final String connectorFloorTexture = getConnectorTextureString(trainType.id, "floor");

				matrices.push();
				if (shouldOffsetRender) {
					matrices.translate(entityX, entityY, entityZ);
				}

				drawTexture(matrices, vertexConsumers, connectorExteriorTexture, thisPos2, prevPos3, prevPos4, thisPos1, light);
				drawTexture(matrices, vertexConsumers, connectorExteriorTexture, prevPos2, thisPos3, thisPos4, prevPos1, light);
				drawTexture(matrices, vertexConsumers, connectorExteriorTexture, prevPos3, thisPos2, thisPos3, prevPos2, light);
				drawTexture(matrices, vertexConsumers, connectorExteriorTexture, prevPos1, thisPos4, thisPos1, prevPos4, light);

				drawTexture(matrices, vertexConsumers, connectorSideTexture, thisPos3, prevPos2, prevPos1, thisPos4, MAX_LIGHT);
				drawTexture(matrices, vertexConsumers, connectorSideTexture, prevPos3, thisPos2, thisPos1, prevPos4, MAX_LIGHT);
				drawTexture(matrices, vertexConsumers, connectorRoofTexture, prevPos2, thisPos3, thisPos2, prevPos3, MAX_LIGHT);
				drawTexture(matrices, vertexConsumers, connectorFloorTexture, prevPos4, thisPos1, thisPos4, prevPos1, MAX_LIGHT);
				matrices.pop();
			}, (speed, x, y, z) -> {
				final Text text;

				if (speed <= 5) {
					switch ((int) ((worldTime / 20) % 3)) {
						default:
							text = getThisStationText(x, z);
							break;
						case 1:
							final Station nextStation = getNextStation(route, new BlockPos(x, y, z));
							if (nextStation == null) {
								text = getThisStationText(x, z);
								if (speed == 0) {
									nextStationId = 0;
								}
							} else {
								text = getNextStationText(nextStation);
								if (speed == 0) {
									nextStationId = nextStation.id;
								}
							}
							break;
						case 2:
							text = getLastStationText(route);
							break;
					}

					announceTime = (int) ((worldTime + ANNOUNCE_DELAY) % Route.TICKS_PER_DAY);
					thisRouteName = route.name.split("\\|\\|")[0];
				} else {
					text = new TranslatableText("gui.mtr.train_speed", Math.round(speed * 10) / 10F, Math.round(speed * 36) / 10F);
				}
				player.sendMessage(text, true);
			}));
		} catch (Exception e) {
			e.printStackTrace();
		}

		matrices.translate(0, 0.0625 + SMALL_OFFSET, 0);
		final boolean renderColors = player.isHolding(item -> item instanceof ItemRailModifier);
		ClientData.rails.forEach(railEntry -> railEntry.connections.forEach((endPos, rail) -> {
			if (!RailwayData.isBetween(player.getX(), railEntry.pos.getX(), endPos.getX(), maxTrainRenderDistance) || !RailwayData.isBetween(player.getZ(), railEntry.pos.getZ(), endPos.getZ(), maxTrainRenderDistance)) {
				return;
			}

			rail.render((x1, z1, x2, z2, x3, z3, x4, z4, y1, y2) -> {
				if (shouldNotRender(player, new BlockPos(x1, y1, z1), maxTrainRenderDistance)) {
					return;
				}

				final float textureOffset = (((int) (x1 + z1)) % 4) * 0.25F;
				final BlockPos pos2 = new BlockPos(x1, y1, z1);
				final int light2 = LightmapTextureManager.pack(world.getLightLevel(LightType.BLOCK, pos2), world.getLightLevel(LightType.SKY, pos2));
				final int color = renderColors || rail.railType == Rail.RailType.PLATFORM ? rail.railType.color : -1;

				IGui.drawTexture(matrices, vertexConsumers, "textures/block/rail.png", x1, y1, z1, x2, y1 + SMALL_OFFSET, z2, x3, y2, z3, x4, y2 + SMALL_OFFSET, z4, 0, 0.1875F + textureOffset, 1, 0.3125F + textureOffset, Direction.UP, color, light2);
				IGui.drawTexture(matrices, vertexConsumers, "textures/block/rail.png", x4, y2 + SMALL_OFFSET, z4, x3, y2, z3, x2, y1 + SMALL_OFFSET, z2, x1, y1, z1, 0, 0.1875F + textureOffset, 1, 0.3125F + textureOffset, Direction.UP, color, light2);
			});
		}));

		matrices.pop();

		final boolean showAnnouncementMessages = Config.showAnnouncementMessages();
		final boolean useTTSAnnouncements = Config.useTTSAnnouncements();
		if ((showAnnouncementMessages || useTTSAnnouncements) && worldTime % Route.TICKS_PER_DAY == announceTime && entity.hasPassengers()) {
			final List<String> messages = new ArrayList<>();
			final String fullstopCJK = new TranslatableText("gui.mtr.fullstop_cjk").getString() + " ";
			final String fullstop = new TranslatableText("gui.mtr.fullstop").getString() + " ";

			final String stationName = ClientData.stationNames.get(nextStationId);
			if (stationName != null) {
				messages.add(IGui.addToStationName(stationName, new TranslatableText("gui.mtr.next_station_announcement_cjk").getString(), new TranslatableText("gui.mtr.next_station_announcement").getString(), fullstopCJK, fullstop));
			}

			final Map<Integer, ClientData.ColorNamePair> routesInStation = ClientData.routesInStation.get(nextStationId);
			if (routesInStation != null) {
				final List<String> interchangeRoutes = routesInStation.values().stream().filter(interchangeRoute -> !interchangeRoute.name.split("\\|\\|")[0].equals(thisRouteName)).map(interchangeRoute -> interchangeRoute.name).collect(Collectors.toList());
				final String mergedStations = IGui.mergeStations(interchangeRoutes).replace(new TranslatableText("gui.mtr.separator_cjk").getString(), ", ").replace(new TranslatableText("gui.mtr.separator").getString(), ", ");
				if (!mergedStations.isEmpty()) {
					messages.add(IGui.addToStationName(mergedStations, new TranslatableText("gui.mtr.interchange_announcement_cjk").getString(), new TranslatableText("gui.mtr.interchange_announcement").getString(), fullstopCJK, fullstop));
				}
			}

			final String message = IGui.formatStationName(IGui.mergeStations(messages).replace(new TranslatableText("gui.mtr.separator_cjk").getString(), "").replace(new TranslatableText("gui.mtr.separator").getString(), "")).replace("  ", " ");
			if (useTTSAnnouncements) {
				Narrator.getNarrator().say(message, false);
			}
			if (showAnnouncementMessages) {
				player.sendMessage(Text.of(message), false);
			}
			announceTime = -1;
		}
	}

	@Override
	public Identifier getTexture(EntitySeat entity) {
		return null;
	}

	public static boolean shouldNotRender(PlayerEntity player, BlockPos pos, int maxDistance) {
		return player == null || player.getBlockPos().getManhattanDistance(pos) > (isReplayMod(player) ? MAX_RADIUS_REPLAY_MOD : maxDistance);
	}

	public static boolean shouldNotRender(BlockPos pos, int maxDistance) {
		final PlayerEntity player = MinecraftClient.getInstance().player;
		return shouldNotRender(player, pos, maxDistance);
	}

	private static Text getThisStationText(int x, int z) {
		final String stationName = ClientData.stations.stream().filter(station -> station.inStation(x, z)).map(station -> station.name).findFirst().orElse("");
		return Text.of(IGui.formatStationName(IGui.addToStationName(IGui.textOrUntitled(stationName), new TranslatableText("gui.mtr.this_station_cjk").getString(), new TranslatableText("gui.mtr.this_station").getString(), "", "")));
	}

	private static Station getNextStation(Route route, BlockPos pos) {
		final Platform currentPlatform = ClientData.platforms.stream().filter(platform -> platform.isCloseToPlatform(pos) && route.platformIds.contains(platform.id)).findFirst().orElse(null);
		if (currentPlatform != null) {
			final int nextPlatformIndex = route.platformIds.indexOf(currentPlatform.id) + 1;
			if (nextPlatformIndex < route.platformIds.size()) {
				return ClientData.platformIdToStation.get(route.platformIds.get(nextPlatformIndex));
			}
		}
		return null;
	}

	private static Text getNextStationText(Station station) {
		return Text.of(IGui.formatStationName(IGui.addToStationName(IGui.textOrUntitled(station == null ? "" : station.name), new TranslatableText("gui.mtr.next_station_cjk").getString(), new TranslatableText("gui.mtr.next_station").getString(), "", "")));
	}

	private static Text getLastStationText(Route route) {
		final Station station = route.platformIds.size() > 0 ? ClientData.platformIdToStation.get(route.platformIds.get(route.platformIds.size() - 1)) : null;
		return Text.of(IGui.formatStationName(IGui.addToStationName(IGui.textOrUntitled(station == null ? "" : station.name), new TranslatableText("gui.mtr.last_station_cjk").getString(), new TranslatableText("gui.mtr.last_station").getString(), "", "")));
	}

	private static void drawTexture(MatrixStack matrices, VertexConsumerProvider vertexConsumers, String texture, Pos3f pos1, Pos3f pos2, Pos3f pos3, Pos3f pos4, int light) {
		IGui.drawTexture(matrices, vertexConsumers, texture, pos1.x, pos1.y, pos1.z, pos2.x, pos2.y, pos2.z, pos3.x, pos3.y, pos3.z, pos4.x, pos4.y, pos4.z, 0, 0, 1, 1, Direction.UP, -1, light);
	}

	private static boolean isReplayMod(PlayerEntity playerEntity) {
		return playerEntity.getClass().toGenericString().toLowerCase().contains("replaymod");
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
			default:
				return null;
		}
	}
}
