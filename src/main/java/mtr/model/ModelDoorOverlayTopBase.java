package mtr.model;

import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

public abstract class ModelDoorOverlayTopBase extends EntityModel<Entity> {

	@Override
	public void setAngles(Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
	}

	@Override
	public final void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
	}

	public abstract void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int position, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ);
}
