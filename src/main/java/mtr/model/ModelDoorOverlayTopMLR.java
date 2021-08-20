package mtr.model;

import mtr.render.MoreRenderLayers;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class ModelDoorOverlayTopMLR extends ModelDoorOverlayTopBase {

	private final ModelPart left;
	private final ModelPart outer_roof_1_r1;
	private final ModelPart right;
	private final ModelPart outer_roof_2_r1;

	private static final Identifier TEXTURE_ID = new Identifier("mtr:textures/sign/door_overlay_mlr_top.png");

	public ModelDoorOverlayTopMLR() {
		textureWidth = 24;
		textureHeight = 3;
		left = new ModelPart(this);
		left.setPivot(0, 24, 0);


		outer_roof_1_r1 = new ModelPart(this);
		outer_roof_1_r1.setPivot(-20.7F, -13, 0);
		left.addChild(outer_roof_1_r1);
		ModelTrainBase.setRotationAngle(outer_roof_1_r1, 0, 0, 0.1107F);
		outer_roof_1_r1.setTextureOffset(0, -12).addCuboid(-0.2F, -19, 0, 0, 3, 12, 0, false);

		right = new ModelPart(this);
		right.setPivot(0, 24, 0);


		outer_roof_2_r1 = new ModelPart(this);
		outer_roof_2_r1.setPivot(-20.7F, -13, 0);
		right.addChild(outer_roof_2_r1);
		ModelTrainBase.setRotationAngle(outer_roof_2_r1, 0, 3.1416F, 0.1107F);
		outer_roof_2_r1.setTextureOffset(0, -12).addCuboid(0.2F, -19, 0, 0, 3, 12, 0, false);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int position, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
		ModelTrainBase.renderOnce(left, matrices, vertexConsumers.getBuffer(MoreRenderLayers.getExterior(TEXTURE_ID)), light, doorRightX, position + doorRightZ);
		ModelTrainBase.renderOnce(right, matrices, vertexConsumers.getBuffer(MoreRenderLayers.getExterior(TEXTURE_ID)), light, doorRightX, position - doorRightZ);
		ModelTrainBase.renderOnceFlipped(left, matrices, vertexConsumers.getBuffer(MoreRenderLayers.getExterior(TEXTURE_ID)), light, doorLeftX, position - doorLeftZ);
		ModelTrainBase.renderOnceFlipped(right, matrices, vertexConsumers.getBuffer(MoreRenderLayers.getExterior(TEXTURE_ID)), light, doorLeftX, position + doorLeftZ);
	}
}