package mtr.render;

import mtr.block.BlockPSDAPGDoorBase;
import mtr.entity.EntityTrainBase;
import mtr.gui.ClientData;
import mtr.gui.IGui;
import mtr.model.ModelTrainBase;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.MathHelper;

public abstract class RenderTrainBase<T extends EntityTrainBase> extends EntityRenderer<T> {

	private static final int VIEW_DISTANCE = 32;

	public RenderTrainBase(EntityRenderDispatcher dispatcher) {
		super(dispatcher);
		shadowRadius = 0;
	}

	@Override
	public void render(T entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
		final int doorValue = entity.getDoorValue();
		final float doorLeftValue = entity.getDoorLeft() ? (float) doorValue / BlockPSDAPGDoorBase.MAX_OPEN_VALUE : 0;
		final float doorRightValue = entity.getDoorRight() ? (float) doorValue / BlockPSDAPGDoorBase.MAX_OPEN_VALUE : 0;

		float yawDifference = (entity.yaw - entity.prevYaw);
		if (yawDifference < -180) {
			yawDifference += 360;
		} else if (yawDifference > 180) {
			yawDifference -= 360;
		}

		final Entity cameraEntity = MinecraftClient.getInstance().cameraEntity;
		final boolean renderDetails = cameraEntity == null || Math.abs(cameraEntity.getX() - entity.getX()) < VIEW_DISTANCE && Math.abs(cameraEntity.getY() - entity.getY()) < VIEW_DISTANCE && Math.abs(cameraEntity.getZ() - entity.getZ()) < VIEW_DISTANCE;

		matrices.push();
		matrices.translate(0, 1, 0);
		matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(entity.prevYaw + tickDelta * yawDifference));
		matrices.multiply(Vector3f.NEGATIVE_X.getDegreesQuaternion(180 + MathHelper.lerp(tickDelta, entity.prevPitch, entity.pitch)));
		getModel().render(matrices, vertexConsumers, getTexture(entity), light, doorLeftValue, doorRightValue, entity.getIsEnd1Head(), entity.getIsEnd2Head(), entity.getHead1IsFront(), renderDetails);
		matrices.pop();

		final MinecraftClient minecraftClient = MinecraftClient.getInstance();
		final PlayerEntity player = minecraftClient.player;
		if (player != null && entity.hasPassenger(player)) {
			final Text text;
			if (entity.getDoorValue() > 0) {
				text = new TranslatableText("mount.onboard", minecraftClient.options.keySneak.getBoundKeyLocalizedText());
			} else {
				final float speed = entity.getSpeed() * 20;
				if (speed <= 5) {
					final String stationName = ClientData.stations.stream().filter(station -> station.inStation((int) player.getX(), (int) player.getZ())).map(station -> station.name).findFirst().orElse("");
					text = Text.of(IGui.formatStationName(IGui.addToStationName(stationName, "", "", "站", " Station")));
				} else {
					text = new TranslatableText("gui.mtr.train_speed").append(String.format(" %s m/s (%s km/h)", Math.round(speed * 10) / 10F, Math.round(speed * 36) / 10F));
				}
			}
			player.sendMessage(text, true);
		}
	}

	protected abstract ModelTrainBase getModel();
}
