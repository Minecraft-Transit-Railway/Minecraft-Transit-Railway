package mtr.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mtr.mappings.ModelDataWrapper;
import mtr.mappings.ModelMapper;
import mtr.render.MoreRenderLayers;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class ModelCableCarGrip extends EntityModel<Entity> {

	private final ModelMapper grip;
	private final ResourceLocation texture = new ResourceLocation("mtr:textures/entity/cable_car_grip.png");

	public ModelCableCarGrip() {
		final int textureWidth = 48;
		final int textureHeight = 48;

		final ModelDataWrapper modelDataWrapper = new ModelDataWrapper(this, textureWidth, textureHeight);

		grip = new ModelMapper(modelDataWrapper);
		grip.setPos(0, 24, 0);
		grip.texOffs(1, 24).addBox(0, -0.2F, -8, 0, 0, 16, 0.2F, false);
		grip.texOffs(14, 0).addBox(0, -1, -3, 1, 1, 6, 0, false);
		grip.texOffs(12, 13).addBox(2, -1, -6, 1, 5, 5, 0, false);
		grip.texOffs(0, 13).addBox(2, -1, 1, 1, 5, 5, 0, false);
		grip.texOffs(0, 0).addBox(3, 0, -5, 2, 3, 10, 0, false);
		grip.texOffs(0, 0).addBox(5, 0, -1, 3, 3, 2, 0, false);
		grip.texOffs(19, 13).addBox(0, -2, -1, 5, 2, 2, 0, false);

		modelDataWrapper.setModelPart(textureWidth, textureHeight);
		grip.setModelPart();
	}

	public void render(PoseStack matrices, MultiBufferSource vertexConsumers, int light) {
		ModelTrainBase.renderOnce(grip, matrices, vertexConsumers.getBuffer(MoreRenderLayers.getExterior(texture)), light, 0);
	}

	@Override
	public void setupAnim(Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
	}

	@Override
	public final void renderToBuffer(PoseStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
	}
}
