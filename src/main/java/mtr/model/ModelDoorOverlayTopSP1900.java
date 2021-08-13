package mtr.model;

import mtr.render.MoreRenderLayers;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.TexturedModelData;
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

		final ModelData modelData = new ModelData();
		final ModelPartData modelPartData = modelData.getRoot();

		bb_main = new ModelMapper(modelPartData);
		bb_main.setPivot(0, 24, 0);


		outer_roof_2_r1 = new ModelMapper(modelPartData);
		outer_roof_2_r1.setPivot(-20, -14, 0);
		bb_main.addChild(outer_roof_2_r1);
		ModelTrainBase.setRotationAngle(outer_roof_2_r1, 0, 3.1416F, 0.1107F);
		outer_roof_2_r1.setTextureOffset(0, -12).addCuboid(1.1F, -21, 0, 0, 3, 12, 0, false);

		outer_roof_1_r1 = new ModelMapper(modelPartData);
		outer_roof_1_r1.setPivot(-20, -14, 0);
		bb_main.addChild(outer_roof_1_r1);
		ModelTrainBase.setRotationAngle(outer_roof_1_r1, 0, 0, 0.1107F);
		outer_roof_1_r1.setTextureOffset(0, -12).addCuboid(-1.1F, -21, 0, 0, 3, 12, 0, false);

		final ModelPart modelPart = TexturedModelData.of(modelData, textureWidth, textureHeight).createModel();
		bb_main.setModelPart(modelPart);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int position, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
		ModelTrainBase.renderMirror(bb_main, matrices, vertexConsumers.getBuffer(MoreRenderLayers.getExterior(TEXTURE_ID)), light, position);
	}
}