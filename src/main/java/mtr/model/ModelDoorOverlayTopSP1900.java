package mtr.model;

import mtr.render.MoreRenderLayers;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class ModelDoorOverlayTopSP1900 extends ModelDoorOverlayTopBase {

	private final ModelPart bb_main;
	private final ModelPart outer_roof_2_r1;
	private final ModelPart outer_roof_1_r1;

	private static final Identifier TEXTURE_ID = new Identifier("mtr:textures/sign/door_overlay_sp1900_top.png");

	public ModelDoorOverlayTopSP1900() {
		textureWidth = 24;
		textureHeight = 3;
		bb_main = new ModelPart(this);
		bb_main.setPivot(0, 24, 0);


		outer_roof_2_r1 = new ModelPart(this);
		outer_roof_2_r1.setPivot(-20, -14, 0);
		bb_main.addChild(outer_roof_2_r1);
		ModelTrainBase.setRotationAngle(outer_roof_2_r1, 0, 3.1416F, 0.1107F);
		outer_roof_2_r1.setTextureOffset(0, -12).addCuboid(1.1F, -21, 0, 0, 3, 12, 0, false);

		outer_roof_1_r1 = new ModelPart(this);
		outer_roof_1_r1.setPivot(-20, -14, 0);
		bb_main.addChild(outer_roof_1_r1);
		ModelTrainBase.setRotationAngle(outer_roof_1_r1, 0, 0, 0.1107F);
		outer_roof_1_r1.setTextureOffset(0, -12).addCuboid(-1.1F, -21, 0, 0, 3, 12, 0, false);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int position, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
		ModelTrainBase.renderMirror(bb_main, matrices, vertexConsumers.getBuffer(MoreRenderLayers.getExterior(TEXTURE_ID)), light, position);
	}
}