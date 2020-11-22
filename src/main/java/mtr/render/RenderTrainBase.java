package mtr.render;

import mtr.entity.EntityTrainBase;
import net.minecraft.client.model.Model;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.math.MathHelper;

public abstract class RenderTrainBase<T extends EntityTrainBase> extends EntityRenderer<T> {

	public RenderTrainBase(EntityRenderDispatcher dispatcher) {
		super(dispatcher);
		shadowRadius = 0;
	}

	@Override
	public void render(T entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
		matrices.push();
		matrices.translate(0, 1.5, 0);
		matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(yaw));
		matrices.multiply(Vector3f.NEGATIVE_X.getDegreesQuaternion(180 + MathHelper.lerp(tickDelta, entity.prevPitch, entity.pitch)));
		VertexConsumer vertexConsumer = vertexConsumers.getBuffer(getModel().getLayer(getTexture(entity)));
		getModel().render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
		matrices.pop();
	}

	protected abstract Model getModel();
}
