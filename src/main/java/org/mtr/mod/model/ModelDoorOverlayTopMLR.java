package mtr.model;

import com.mojang.blaze3d.vertex.PoseStack;
import mtr.mappings.ModelDataWrapper;
import mtr.mappings.ModelMapper;
import mtr.render.MoreRenderLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;

public class ModelDoorOverlayTopMLR extends ModelDoorOverlayTopBase {

	private final ModelMapper left;
	private final ModelMapper outer_roof_1_r1;
	private final ModelMapper right;
	private final ModelMapper outer_roof_2_r1;

	private final ResourceLocation texture;

	public ModelDoorOverlayTopMLR(String texture) {
		this.texture = new ResourceLocation(texture);

		final int textureWidth = 24;
		final int textureHeight = 3;

		final ModelDataWrapper modelDataWrapper = new ModelDataWrapper(this, textureWidth, textureHeight);

		left = new ModelMapper(modelDataWrapper);
		left.setPos(0, 24, 0);


		outer_roof_1_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_1_r1.setPos(-20.7F, -13, 0);
		left.addChild(outer_roof_1_r1);
		ModelTrainBase.setRotationAngle(outer_roof_1_r1, 0, 0, 0.1107F);
		outer_roof_1_r1.texOffs(0, -12).addBox(-0.2F, -19, 0, 0, 3, 12, 0, false);

		right = new ModelMapper(modelDataWrapper);
		right.setPos(0, 24, 0);


		outer_roof_2_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_2_r1.setPos(-20.7F, -13, 0);
		right.addChild(outer_roof_2_r1);
		ModelTrainBase.setRotationAngle(outer_roof_2_r1, 0, 3.1416F, 0.1107F);
		outer_roof_2_r1.texOffs(0, -12).addBox(0.2F, -19, 0, 0, 3, 12, 0, false);

		modelDataWrapper.setModelPart(textureWidth, textureHeight);
		left.setModelPart();
		right.setModelPart();
	}

	@Override
	public void render(PoseStack matrices, MultiBufferSource vertexConsumers, int light, int position, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
		ModelTrainBase.renderOnce(left, matrices, vertexConsumers.getBuffer(MoreRenderLayers.getExterior(texture)), light, doorRightX, position + doorRightZ);
		ModelTrainBase.renderOnce(right, matrices, vertexConsumers.getBuffer(MoreRenderLayers.getExterior(texture)), light, doorRightX, position - doorRightZ);
		ModelTrainBase.renderOnceFlipped(left, matrices, vertexConsumers.getBuffer(MoreRenderLayers.getExterior(texture)), light, doorLeftX, position - doorLeftZ);
		ModelTrainBase.renderOnceFlipped(right, matrices, vertexConsumers.getBuffer(MoreRenderLayers.getExterior(texture)), light, doorLeftX, position + doorLeftZ);
	}
}