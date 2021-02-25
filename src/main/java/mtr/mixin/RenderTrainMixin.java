package mtr.mixin;

import mtr.data.Pos3f;
import mtr.data.TrainType;
import mtr.gui.ClientData;
import mtr.gui.IGui;
import mtr.model.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.MinecartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.vehicle.MinecartEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import net.minecraft.world.LightType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class RenderTrainMixin implements IGui {

	private static final int DETAIL_RADIUS_SQUARED = 32 * 32;

	private static final EntityModel<MinecartEntity> MODEL_MINECART = new MinecartEntityModel<>();
	private static final ModelSP1900 MODEL_SP1900 = new ModelSP1900();
	private static final ModelSP1900Mini MODEL_SP1900_MINI = new ModelSP1900Mini();
	private static final ModelMTrain MODEL_M_TRAIN = new ModelMTrain();
	private static final ModelMTrainMini MODEL_M_TRAIN_MINI = new ModelMTrainMini();
	private static final ModelLightRail1 MODEL_LIGHT_RAIL_1 = new ModelLightRail1();

	@Shadow
	private ClientWorld world;

	@Shadow
	@Final
	private BufferBuilderStorage bufferBuilders;

	@Shadow
	@Final
	private MinecraftClient client;

	@Inject(method = "render", at = @At("RETURN"))
	private void injectMethod(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, CallbackInfo ci) {
		final VertexConsumerProvider.Immediate vertexConsumers = bufferBuilders.getEntityVertexConsumers();
		final Vec3d cameraPos = camera.getPos();
		final float renderDistanceSquared = MathHelper.square(MinecraftClient.getInstance().options.viewDistance * 16);

		final ClientPlayerEntity player = client.player;
		if (player == null) {
			return;
		}

		matrices.push();
		matrices.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);

		final float worldTime = world.getLunarTime() + tickDelta;

		ClientData.routes.forEach(route -> route.getPositionYaw(world, worldTime, client.getLastFrameDuration(), ((x, y, z, yaw, pitch, trainType, isEnd1Head, isEnd2Head, doorLeftValue, doorRightValue) -> {
			final double squaredDistance = player.getPos().squaredDistanceTo(x, y, z);
			if (squaredDistance > renderDistanceSquared) {
				return;
			}

			final BlockPos posAverage = new BlockPos(x, y, z);
			final int light = LightmapTextureManager.pack(world.getLightLevel(LightType.BLOCK, posAverage), world.getLightLevel(LightType.SKY, posAverage));
			final boolean isMinecart = trainType == TrainType.MINECART;

			matrices.push();
			matrices.translate(x, y, z);
			matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(180 + yaw));
			matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(180 + pitch));

			if (isMinecart) {
				matrices.translate(0, 0.5, 0);
				matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(90));
				final VertexConsumer vertexConsumer = vertexConsumers.getBuffer(MODEL_MINECART.getLayer(new Identifier("textures/entity/minecart.png")));
				MODEL_MINECART.setAngles(null, 0, 0, -0.1F, 0, 0);
				MODEL_MINECART.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1);
			} else {
				getModel(trainType).render(matrices, vertexConsumers, getTrainTexture(trainType.id), light, doorLeftValue, doorRightValue, isEnd1Head, isEnd2Head, true, squaredDistance <= DETAIL_RADIUS_SQUARED);
			}

			matrices.pop();
		}), (prevPos1, prevPos2, prevPos3, prevPos4, thisPos1, thisPos2, thisPos3, thisPos4, x, y, z, trainType) -> {
			final double squaredDistance = player.getPos().squaredDistanceTo(x, y, z);
			if (squaredDistance > renderDistanceSquared) {
				return;
			}

			final BlockPos posAverage = new BlockPos(x, y, z);
			final int light = LightmapTextureManager.pack(world.getLightLevel(LightType.BLOCK, posAverage), world.getLightLevel(LightType.SKY, posAverage));

			final String connectorExteriorTexture = getConnectorTextureString(trainType.id, "exterior");
			final String connectorSideTexture = getConnectorTextureString(trainType.id, "side");
			final String connectorRoofTexture = getConnectorTextureString(trainType.id, "roof");
			final String connectorFloorTexture = getConnectorTextureString(trainType.id, "floor");

			drawTexture(matrices, vertexConsumers, connectorExteriorTexture, thisPos2, prevPos3, prevPos4, thisPos1, light);
			drawTexture(matrices, vertexConsumers, connectorExteriorTexture, prevPos2, thisPos3, thisPos4, prevPos1, light);
			drawTexture(matrices, vertexConsumers, connectorExteriorTexture, prevPos3, thisPos2, thisPos3, prevPos2, light);
			drawTexture(matrices, vertexConsumers, connectorExteriorTexture, prevPos1, thisPos4, thisPos1, prevPos4, light);

			drawTexture(matrices, vertexConsumers, connectorSideTexture, thisPos3, prevPos2, prevPos1, thisPos4, MAX_LIGHT);
			drawTexture(matrices, vertexConsumers, connectorSideTexture, prevPos3, thisPos2, thisPos1, prevPos4, MAX_LIGHT);
			drawTexture(matrices, vertexConsumers, connectorRoofTexture, prevPos2, thisPos3, thisPos2, prevPos3, MAX_LIGHT);
			drawTexture(matrices, vertexConsumers, connectorFloorTexture, prevPos4, thisPos1, thisPos4, prevPos1, MAX_LIGHT);
		}));

		// rendering something invisible to flush the buffers, or else the last rendered train will render incorrectly
		matrices.push();
		new MinecartEntityModel<>().render(matrices, vertexConsumers.getBuffer(RenderLayer.LINES), 0, 0, 0, 0, 0, 0);
		matrices.pop();

		matrices.pop();
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
				return MODEL_M_TRAIN;
		}
	}
}
