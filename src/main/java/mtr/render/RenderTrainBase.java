package mtr.render;

import mtr.block.BlockPSDAPGDoorBase;
import mtr.entity.EntityTrainBase;
import mtr.model.ModelTrainBase;
import mtr.path.PathFinderBase;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.MathHelper;

public abstract class RenderTrainBase<T extends EntityTrainBase> extends EntityRenderer<T> {

	public RenderTrainBase(EntityRenderDispatcher dispatcher) {
		super(dispatcher);
		shadowRadius = 0;
	}

	@Override
	public void render(T entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
		final int doorValue = entity.getDoorValue();
		final float doorLeftValue = entity.getDoorLeft() ? (float) doorValue / BlockPSDAPGDoorBase.MAX_OPEN_VALUE : 0;
		final float doorRightValue = entity.getDoorRight() ? (float) doorValue / BlockPSDAPGDoorBase.MAX_OPEN_VALUE : 0;

		matrices.push();
		matrices.translate(0, 1, 0);
		matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(yaw));
		matrices.multiply(Vector3f.NEGATIVE_X.getDegreesQuaternion(180 + MathHelper.lerp(tickDelta, entity.prevPitch, entity.pitch)));
		getModel().render(matrices, vertexConsumers, getTexture(entity), light, doorLeftValue, doorRightValue, entity.getIsEnd1Head(), entity.getIsEnd2Head(), entity.getHead1IsFront());
		matrices.pop();

		final MinecraftClient minecraftClient = MinecraftClient.getInstance();
		final PlayerEntity player = minecraftClient.player;
		if (player != null && entity.hasPassenger(player)) {
			final Text text;
			if (entity.getDoorValue() > 0) {
				text = new TranslatableText("mount.onboard", minecraftClient.options.keySneak.getBoundKeyLocalizedText());
			} else {
				final double speed = Math.sqrt(PathFinderBase.distanceSquaredBetween((float) entity.prevX, (float) entity.prevY, (float) entity.prevZ, (float) entity.getX(), (float) entity.getY(), (float) entity.getZ())) * 20;
				text = new TranslatableText("gui.mtr.train_speed").append(String.format(" %s m/s (%s km/h)", Math.round(speed * 10) / 10F, Math.round(speed * 36) / 10F));
			}
			player.sendMessage(text, true);
		}
	}

	protected abstract ModelTrainBase getModel();
}
