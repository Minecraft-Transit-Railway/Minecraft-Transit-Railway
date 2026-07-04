package org.mtr.widget;

import gg.essential.universal.UMatrixStack;
import gg.essential.universal.utils.ReleasedDynamicTexture;
import gg.essential.universal.vertex.UVertexConsumer;

public final class DirectImageComponent extends ImageComponentBase {

	private final float u1;
	private final float v1;
	private final float u2;
	private final float v2;

	public DirectImageComponent(ReleasedDynamicTexture... releasedDynamicTextures) {
		this(0, 0, 1, 1, releasedDynamicTextures);
	}

	public DirectImageComponent(float u1, float v1, float u2, float v2, ReleasedDynamicTexture... releasedDynamicTextures) {
		super(releasedDynamicTextures);
		this.u1 = u1;
		this.v1 = v1;
		this.u2 = u2;
		this.v2 = v2;
	}

	@Override
	public void renderTexture(UMatrixStack matrixStack, UVertexConsumer vertexConsumer) {
		drawTexturedQuad(matrixStack, vertexConsumer, getLeft(), getTop(), getRight(), getBottom(), u1, v1, u2, v2);
	}
}
