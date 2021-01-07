package mtr.render;

import mtr.block.BlockPSDAPGDoorBase;
import mtr.entity.EntityTrainBase;
import mtr.model.ModelTrainBase;
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
		final int doorValue = entity.getDoorValue();
		final float doorLeftValue = entity.getDoorLeft() ? (float) doorValue / BlockPSDAPGDoorBase.MAX_OPEN_VALUE : 0;
		final float doorRightValue = entity.getDoorRight() ? (float) doorValue / BlockPSDAPGDoorBase.MAX_OPEN_VALUE : 0;

		matrices.push();
		matrices.translate(0, 1, 0);
		matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(yaw));
		matrices.multiply(Vector3f.NEGATIVE_X.getDegreesQuaternion(180 + MathHelper.lerp(tickDelta, entity.prevPitch, entity.pitch)));
		getModel().render(matrices, vertexConsumers, getTexture(entity), light, doorLeftValue, doorRightValue, true, false);
		matrices.pop();
	}

	protected abstract ModelTrainBase getModel();
}
