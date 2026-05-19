package org.mtr.mod.render;

import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import org.mtr.mapping.holder.Vector3d;
import org.mtr.mapping.mapper.GraphicsHolder;

public final class InstancingMatrixHelper {

	public static final Vector3d ZERO_OFFSET = new Vector3d(0, 0, 0);

	private InstancingMatrixHelper() {
	}

	public static Matrix4f captureMatrix(StoredMatrixTransformations storedMatrixTransformations, Vector3d offset) {
		final MatrixStack matrixStack = new MatrixStack();
		final Matrix4f[] matrixHolder = new Matrix4f[1];
		GraphicsHolder.createInstanceSafe(matrixStack, null, graphicsHolder -> {
			storedMatrixTransformations.transform(graphicsHolder, offset);
			matrixHolder[0] = new Matrix4f(matrixStack.peek().getPositionMatrix());
		});
		return matrixHolder[0] == null ? new Matrix4f() : matrixHolder[0];
	}
}
