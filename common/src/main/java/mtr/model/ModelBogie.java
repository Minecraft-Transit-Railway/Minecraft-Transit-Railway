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

public class ModelBogie extends EntityModel<Entity> {

	private final ModelMapper bogie;
	private final ResourceLocation texture = new ResourceLocation("mtr:textures/entity/bogie_1.png");

	public ModelBogie() {
		final int textureWidth = 186;
		final int textureHeight = 77;

		final ModelDataWrapper modelDataWrapper = new ModelDataWrapper(this, textureWidth, textureHeight);

		bogie = new ModelMapper(modelDataWrapper);
		bogie.setPos(0, 24, 0);
		bogie.texOffs(0, 0).addBox(-14, 1, -32, 1, 13, 64, 0, false);
		bogie.texOffs(0, 0).addBox(-13, 1.5F, 16, 1, 14, 14, 0, false);
		bogie.texOffs(0, 0).addBox(-13, 1.5F, -30, 1, 14, 14, 0, false);
		bogie.texOffs(66, 0).addBox(-13, 1, -23.5F, 13, 8, 47, 0, false);

		modelDataWrapper.setModelPart(textureWidth, textureHeight);
		bogie.setModelPart();
	}

	public void render(PoseStack matrices, MultiBufferSource vertexConsumers, int light, int position) {
		ModelTrainBase.renderMirror(bogie, matrices, vertexConsumers.getBuffer(MoreRenderLayers.getExterior(texture)), light, position);
	}

	@Override
	public void setupAnim(Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
	}

	@Override
	public final void renderToBuffer(PoseStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
	}
}
