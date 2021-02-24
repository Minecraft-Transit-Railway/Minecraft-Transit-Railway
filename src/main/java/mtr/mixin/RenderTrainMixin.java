package mtr.mixin;

import mtr.data.TrainType;
import mtr.gui.ClientData;
import mtr.model.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.MinecartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LightType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class RenderTrainMixin {

	private static final Identifier TEXTURE_MINECART = new Identifier("textures/entity/minecart.png");
	private static final Identifier TEXTURE_SP1900 = new Identifier("mtr:textures/entity/sp1900.png");
	private static final Identifier TEXTURE_M_TRAIN = new Identifier("mtr:textures/entity/m_train.png");
	private static final Identifier TEXTURE_LIGHT_RAIL_1 = new Identifier("mtr:textures/entity/light_rail_1.png");
	private static final EntityModel<Entity> MODEL_MINECART = new MinecartEntityModel<>();
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
		VertexConsumerProvider.Immediate vertexConsumers = bufferBuilders.getEntityVertexConsumers();
		final Vec3d cameraPos = camera.getPos();

		matrices.push();
		matrices.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);

		final float worldTime = world.getLunarTime() + tickDelta;

		ClientData.routes.forEach(route -> route.getPositionYaw(world, worldTime, client.getLastFrameDuration(), ((x, y, z, yaw, pitch, trainType, isEnd1Head, isEnd2Head, doorLeftValue, doorRightValue) -> {
			final BlockPos posAverage = new BlockPos(x, y, z);
			final int light = LightmapTextureManager.pack(world.getLightLevel(LightType.BLOCK, posAverage), world.getLightLevel(LightType.SKY, posAverage));

			matrices.push();
			matrices.translate(x, y, z);
			matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(180 + yaw));
			matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(180 + pitch));
			getModel(trainType).render(matrices, vertexConsumers, getTexture(trainType), light, doorLeftValue, doorRightValue, isEnd1Head, isEnd2Head, true, true);
			matrices.pop();
		})));

		// rendering something invisible to flush the buffers, or else the last rendered train will render incorrectly
		matrices.push();
		new MinecartEntityModel<>().render(matrices, vertexConsumers.getBuffer(RenderLayer.LINES), 0, 0, 0, 0, 0, 0);
		matrices.pop();

		matrices.pop();
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

	private static Identifier getTexture(TrainType trainType) {
		switch (trainType) {
			case SP1900:
			case SP1900_MINI:
				return TEXTURE_SP1900;
			case M_TRAIN:
			case M_TRAIN_MINI:
				return TEXTURE_M_TRAIN;
			case LIGHT_RAIL_1:
				return TEXTURE_LIGHT_RAIL_1;
			default:
				return TEXTURE_MINECART;
		}
	}
}
