package mtr.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.Entity;

public abstract class ModelDoorOverlayTopBase extends EntityModel<Entity> {

	@Override
	public void setupAnim(Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
	}

	@Override
	public final void renderToBuffer(PoseStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
	}

	public abstract void render(PoseStack matrices, MultiBufferSource vertexConsumers, int light, int position, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ);
}
