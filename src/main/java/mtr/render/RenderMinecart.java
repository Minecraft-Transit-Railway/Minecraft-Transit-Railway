package mtr.render;

import mtr.entity.EntityMinecart;
import net.minecraft.client.model.Model;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.MinecartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class RenderMinecart extends RenderTrainBase<EntityMinecart> {

	private static final Identifier texture = new Identifier("textures/entity/minecart.png");
	private static final EntityModel<EntityMinecart> model = new MinecartEntityModel<>();

	public RenderMinecart(EntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public void render(EntityMinecart entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
		matrices.push();
		matrices.translate(0, 0.5, 0);
		matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(yaw + 90));
		matrices.multiply(Vector3f.NEGATIVE_Z.getDegreesQuaternion(180 + MathHelper.lerp(tickDelta, entity.prevPitch, entity.pitch)));
		model.setAngles(entity, 0, 0, -0.1F, 0, 0);
		VertexConsumer vertexConsumer = vertexConsumers.getBuffer(model.getLayer(texture));
		model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
		matrices.pop();
	}

	@Override
	public Identifier getTexture(EntityMinecart entity) {
		return texture;
	}

	@Override
	protected Model getModel() {
		return model;
	}
}
