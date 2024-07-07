package org.mtr.mod.model;

import org.mtr.mapping.holder.EntityAbstractMapping;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.mapper.EntityModelExtension;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.ModelPartExtension;
import org.mtr.mod.render.MainRenderer;
import org.mtr.mod.render.QueuedRenderLayer;
import org.mtr.mod.render.StoredMatrixTransformations;

public class ModelSmallCube extends EntityModelExtension<EntityAbstractMapping> {

	private final Identifier texture;
	private final ModelPartExtension part;

	public ModelSmallCube(Identifier texture) {
		super(16, 16);
		this.texture = texture;

		part = createModelPart();
		part.setPivot(0, 0, 0);
		part.setTextureUVOffset(0, 0).addCuboid(4, 4, 4, 8, 8, 8, 0, false);

		buildModel();
	}

	public void render(StoredMatrixTransformations storedMatrixTransformations, int light) {
		MainRenderer.scheduleRender(texture, false, QueuedRenderLayer.EXTERIOR, (graphicsHolder, offset) -> {
			storedMatrixTransformations.transform(graphicsHolder, offset);
			ModelTrainBase.renderOnce(part, graphicsHolder, light, 0);
			graphicsHolder.pop();
		});
	}

	@Override
	public void setAngles2(EntityAbstractMapping entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
	}

	@Override
	public final void render(GraphicsHolder graphicsHolder, int light, int overlay, float red, float green, float blue, float alpha) {
	}
}
