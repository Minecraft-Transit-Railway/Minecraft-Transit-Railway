package mtr.mixin;

import mtr.data.Pos3f;
import mtr.data.Route;
import mtr.data.Train;
import mtr.gui.ClientData;
import mtr.gui.IGui;
import mtr.model.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.MinecartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

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

	@Inject(method = "render", at = @At("RETURN"))
	private void injectMethod(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, CallbackInfo ci) {
		VertexConsumerProvider.Immediate vertexConsumers = bufferBuilders.getEntityVertexConsumers();
		final Vec3d cameraPos = camera.getPos();

		matrices.push();
		matrices.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);

		final float worldTime = world.getLunarTime() + tickDelta + 30000;

		ClientData.routes.forEach(route -> route.schedule.forEach((scheduleTime, trainType) -> {
			final List<Pos3f> positions = route.getPositions((worldTime - scheduleTime) % (Route.HOURS_IN_DAY * Route.TICKS_PER_HOUR), trainType.getSpacing());

			for (int i = 0; i < positions.size() - 1; i++) {
				final Pos3f pos1 = positions.get(i);
				final Pos3f pos2 = positions.get(i + 1);

				if (pos1 != null && pos2 != null) {
					final float xAverage = (pos1.getX() + pos2.getX()) / 2;
					final float yAverage = (pos1.getY() + pos2.getY()) / 2;
					final float zAverage = (pos1.getZ() + pos2.getZ()) / 2;

					final float yaw = (float) Math.toDegrees(MathHelper.atan2(pos2.getX() - pos1.getX(), pos2.getZ() - pos1.getZ()));
					final float pitch = (float) Math.toDegrees(Math.asin((pos2.getY() - pos1.getY()) / trainType.getSpacing()));

					matrices.push();
					matrices.translate(xAverage, yAverage + 5, zAverage);
					matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(180 + yaw));
					matrices.multiply(Vector3f.NEGATIVE_X.getDegreesQuaternion(180 + pitch));
					getModel(trainType).render(matrices, vertexConsumers, getTexture(trainType), IGui.MAX_LIGHT, 0, 0, i == 0, i == positions.size() - 2, i == 0, true);
					matrices.pop();
				}
			}
		}));

		// rendering something invisible to flush the buffers, or else the last rendered train will render incorrectly
		matrices.push();
		new MinecartEntityModel<>().render(matrices, vertexConsumers.getBuffer(RenderLayer.LINES), 0, 0, 0, 0, 0, 0);
		matrices.pop();

		matrices.pop();
	}

	private static ModelTrainBase getModel(Train.TrainType trainType) {
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

	private static Identifier getTexture(Train.TrainType trainType) {
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
