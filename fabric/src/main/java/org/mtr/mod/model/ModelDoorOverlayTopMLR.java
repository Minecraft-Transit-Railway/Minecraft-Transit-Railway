package org.mtr.mod.model;

import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.mapper.ModelPartExtension;
import org.mtr.mod.render.RenderTrains;
import org.mtr.mod.render.StoredMatrixTransformations;

public class ModelDoorOverlayTopMLR extends ModelDoorOverlayTopBase {

	private final ModelPartExtension left;
	private final ModelPartExtension outer_roof_1_r1;
	private final ModelPartExtension right;
	private final ModelPartExtension outer_roof_2_r1;

	private final Identifier texture;

	public ModelDoorOverlayTopMLR(String texture) {
		super(24, 3);
		this.texture = new Identifier(texture);

		left = createModelPart();
		left.setPivot(0, 24, 0);


		outer_roof_1_r1 = createModelPart();
		outer_roof_1_r1.setPivot(-20.7F, -13, 0);
		left.addChild(outer_roof_1_r1);
		ModelTrainBase.setRotationAngle(outer_roof_1_r1, 0, 0, 0.1107F);
		outer_roof_1_r1.setTextureUVOffset(0, -12).addCuboid(-0.2F, -19, 0, 0, 3, 12, 0, false);

		right = createModelPart();
		right.setPivot(0, 24, 0);


		outer_roof_2_r1 = createModelPart();
		outer_roof_2_r1.setPivot(-20.7F, -13, 0);
		right.addChild(outer_roof_2_r1);
		ModelTrainBase.setRotationAngle(outer_roof_2_r1, 0, 3.1416F, 0.1107F);
		outer_roof_2_r1.setTextureUVOffset(0, -12).addCuboid(0.2F, -19, 0, 0, 3, 12, 0, false);

		buildModel();
	}

	@Override
	public void renderNew(StoredMatrixTransformations storedMatrixTransformations, int light, int position, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
		RenderTrains.scheduleRender(texture, false, RenderTrains.QueuedRenderLayer.EXTERIOR, (graphicsHolder, offset) -> {
			storedMatrixTransformations.transform(graphicsHolder, offset);
			ModelTrainBase.renderOnce(left, graphicsHolder, light, doorRightX, position + doorRightZ);
			ModelTrainBase.renderOnce(right, graphicsHolder, light, doorRightX, position - doorRightZ);
			ModelTrainBase.renderOnceFlipped(left, graphicsHolder, light, doorLeftX, position - doorLeftZ);
			ModelTrainBase.renderOnceFlipped(right, graphicsHolder, light, doorLeftX, position + doorLeftZ);
			graphicsHolder.pop();
		});
	}
}