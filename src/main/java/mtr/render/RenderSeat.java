package mtr.render;

import mtr.data.Pos3f;
import mtr.data.TrainType;
import mtr.entity.EntitySeat;
import mtr.gui.ClientData;
import mtr.gui.IGui;
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
import net.minecraft.entity.vehicle.MinecartEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

public class RenderSeat extends EntityRenderer<EntitySeat> implements IGui {

	private static final int DETAIL_RADIUS_SQUARED = EntitySeat.DETAIL_RADIUS * EntitySeat.DETAIL_RADIUS;

	private static final EntityModel<MinecartEntity> MODEL_MINECART = new MinecartEntityModel<>();
	private static final ModelSP1900 MODEL_SP1900 = new ModelSP1900();
	private static final ModelSP1900Mini MODEL_SP1900_MINI = new ModelSP1900Mini();
	private static final ModelMTrain MODEL_M_TRAIN = new ModelMTrain();
	private static final ModelMTrainMini MODEL_M_TRAIN_MINI = new ModelMTrainMini();
	private static final ModelLightRail1 MODEL_LIGHT_RAIL_1 = new ModelLightRail1();

	public RenderSeat(EntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public void render(EntitySeat entity, float entityYaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int entityLight) {
		final MinecraftClient client = MinecraftClient.getInstance();
		final ClientPlayerEntity player = client.player;
		if (player == null || player != entity.getPlayer()) {
			return;
		}
		final World world = entity.world;
		if (world == null) {
			return;
		}
		final int halfRenderDistance = client.options.viewDistance * 8;

		matrices.push();
		final double entityX = MathHelper.lerp(tickDelta, entity.lastRenderX, entity.getX());
		final double entityY = MathHelper.lerp(tickDelta, entity.lastRenderY, entity.getY());
		final double entityZ = MathHelper.lerp(tickDelta, entity.lastRenderZ, entity.getZ());
		matrices.translate(-entityX, -entityY, -entityZ);

		ClientData.routes.forEach(route -> route.getPositionYaw(world, (int) world.getLunarTime() + tickDelta, entity, ((x, y, z, yaw, pitch, trainType, isEnd1Head, isEnd2Head, doorLeftValue, doorRightValue, shouldOffsetRender) -> {
			final double offsetX = x + (shouldOffsetRender ? entityX : 0);
			final double offsetY = y + (shouldOffsetRender ? entityY : 0);
			final double offsetZ = z + (shouldOffsetRender ? entityZ : 0);
			final BlockPos posAverage = new BlockPos(offsetX, offsetY, offsetZ);
			if (posAverage.getManhattanDistance(player.getBlockPos()) > halfRenderDistance) {
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
				model.render(matrices, vertexConsumers, getTrainTexture(trainType.id), light, doorLeftValue, doorRightValue, isEnd1Head, isEnd2Head, true, player.getPos().squaredDistanceTo(offsetX, offsetY, offsetZ) <= DETAIL_RADIUS_SQUARED);
			}

			matrices.pop();
		}), (prevPos1, prevPos2, prevPos3, prevPos4, thisPos1, thisPos2, thisPos3, thisPos4, x, y, z, trainType, shouldOffsetRender) -> {
			final double offsetX = x + (shouldOffsetRender ? entityX : 0);
			final double offsetY = y + (shouldOffsetRender ? entityY : 0);
			final double offsetZ = z + (shouldOffsetRender ? entityZ : 0);
			final BlockPos posAverage = new BlockPos(offsetX, offsetY, offsetZ);
			if (posAverage.getManhattanDistance(player.getBlockPos()) > halfRenderDistance) {
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
		}, (speed, x, z) -> {
			final Text text;
			if (speed <= 5) {
				final String stationName = ClientData.stations.stream().filter(station -> station.inStation(x, z)).map(station -> station.name).findFirst().orElse("");
				text = Text.of(IGui.formatStationName(IGui.addToStationName(stationName, "", "", new TranslatableText("gui.mtr.station_cjk").getString(), new TranslatableText("gui.mtr.station").getString())));
			} else {
				text = new TranslatableText("gui.mtr.train_speed", Math.round(speed * 10) / 10F, Math.round(speed * 36) / 10F);
			}
			player.sendMessage(text, true);
		}));

		matrices.pop();
	}

	@Override
	public Identifier getTexture(EntitySeat entity) {
		return null;
	}

	private static void drawTexture(MatrixStack matrices, VertexConsumerProvider vertexConsumers, String texture, Pos3f pos1, Pos3f pos2, Pos3f pos3, Pos3f pos4, int light) {
		IGui.drawTexture(matrices, vertexConsumers, texture, pos1.x, pos1.y, pos1.z, pos2.x, pos2.y, pos2.z, pos3.x, pos3.y, pos3.z, pos4.x, pos4.y, pos4.z, 0, 0, 1, 1, Direction.UP, -1, light);
	}

	private static Identifier getTrainTexture(String trainId) {
		return new Identifier("mtr:textures/entity/" + trainId + ".png");
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
			case M_TRAIN:
				return MODEL_M_TRAIN;
			case M_TRAIN_MINI:
				return MODEL_M_TRAIN_MINI;
			case LIGHT_RAIL_1:
				return MODEL_LIGHT_RAIL_1;
			default:
				return null;
		}
	}
}
