package mtr.model;

import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

public abstract class ModelDoorOverlayBase extends EntityModel<Entity> {

	protected static final Identifier DOOR_OVERLAY_TEXTURE_LEFT = new Identifier("mtr:textures/sign/door_overlay_default_left.png");
	protected static final Identifier DOOR_OVERLAY_TEXTURE_RIGHT = new Identifier("mtr:textures/sign/door_overlay_default_right.png");

	@Override
	public final void setAngles(Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
	}

	@Override
	public final void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
	}

	protected abstract void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, ModelTrainBase.RenderStage renderStage, int light, int position, float doorLeftValue, float doorRightValue);
}
