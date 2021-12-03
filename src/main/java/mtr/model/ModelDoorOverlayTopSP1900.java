package mtr.model;

import mapper.ModelDataWrapper;
import mapper.ModelMapper;
import mtr.render.MoreRenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class ModelDoorOverlayTopSP1900 extends ModelDoorOverlayTopBase {

	private final ModelMapper bb_main;
	private final ModelMapper outer_roof_2_r1;
	private final ModelMapper outer_roof_1_r1;

	private static final Identifier TEXTURE_ID = new Identifier("mtr:textures/sign/door_overlay_sp1900_top.png");

	public ModelDoorOverlayTopSP1900() {
		final int textureWidth = 24;
		final int textureHeight = 3;

		final ModelDataWrapper modelDataWrapper = new ModelDataWrapper(this, textureWidth, textureHeight);

		bb_main = new ModelMapper(modelDataWrapper);
		bb_main.setPivot(0, 24, 0);


		outer_roof_2_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_2_r1.setPivot(-20, -14, 0);
		bb_main.addChild(outer_roof_2_r1);
		ModelTrainBase.setRotationAngle(outer_roof_2_r1, 0, 3.1416F, 0.1107F);
		outer_roof_2_r1.setTextureOffset(0, -12).addCuboid(1.1F, -21, 0, 0, 3, 12, 0, false);

		outer_roof_1_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_1_r1.setPivot(-20, -14, 0);
		bb_main.addChild(outer_roof_1_r1);
		ModelTrainBase.setRotationAngle(outer_roof_1_r1, 0, 0, 0.1107F);
		outer_roof_1_r1.setTextureOffset(0, -12).addCuboid(-1.1F, -21, 0, 0, 3, 12, 0, false);

		modelDataWrapper.setModelPart(textureWidth, textureHeight);
		bb_main.setModelPart();
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int position, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
		ModelTrainBase.renderMirror(bb_main, matrices, vertexConsumers.getBuffer(MoreRenderLayers.getExterior(TEXTURE_ID)), light, position);
	}
}