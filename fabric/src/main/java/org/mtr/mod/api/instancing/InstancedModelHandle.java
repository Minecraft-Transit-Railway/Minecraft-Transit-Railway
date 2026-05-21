package org.mtr.mod.api.instancing;

import org.joml.Matrix4f;
import org.mtr.mod.render.StoredMatrixTransformations;

public interface InstancedModelHandle extends AutoCloseable {

	boolean isValid();

	boolean queue(StoredMatrixTransformations transformations, int color, int light);

	boolean queueDrawMatrix(Matrix4f drawMatrix, int color, int light);

	InstancingFallbackReason getFallbackReason();

	@Override
	void close();
}
