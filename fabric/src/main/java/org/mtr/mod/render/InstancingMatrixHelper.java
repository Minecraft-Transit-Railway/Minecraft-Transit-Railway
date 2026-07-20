package org.mtr.mod.render;

import org.mtr.mapping.holder.Matrix4f;
import org.mtr.mapping.holder.Vector3d;
import org.mtr.mapping.mapper.GraphicsHolder;

import javax.annotation.Nullable;

public final class InstancingMatrixHelper {

	public static final Vector3d ZERO_OFFSET = new Vector3d(0, 0, 0);

	private InstancingMatrixHelper() {
	}

	public static Matrix4f captureMatrix(StoredMatrixTransformations storedMatrixTransformations, Vector3d offset) {
		final Matrix4f[] matrixHolder = new Matrix4f[1];
		GraphicsHolder.createInstanceSafe(null, null, graphicsHolder -> {
			storedMatrixTransformations.transform(graphicsHolder, offset);
			matrixHolder[0] = graphicsHolder.copyPositionMatrix();
		});
		return matrixHolder[0] == null ? new Matrix4f() : matrixHolder[0];
	}

	public static Matrix4f captureMatrix(@Nullable GraphicsHolder graphicsHolder, StoredMatrixTransformations storedMatrixTransformations, Vector3d offset) {
		if (graphicsHolder == null) {
			return captureMatrix(storedMatrixTransformations, offset);
		}

		try {
			storedMatrixTransformations.transform(graphicsHolder, offset);
			return graphicsHolder.copyPositionMatrix();
		} catch (Exception ignored) {
			return captureMatrix(storedMatrixTransformations, offset);
		} finally {
			graphicsHolder.pop();
		}
	}
}
