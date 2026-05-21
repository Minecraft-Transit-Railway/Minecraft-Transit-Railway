package org.mtr.mod.api.instancing;

import org.joml.Matrix4f;

public interface InstancedModelHandle extends AutoCloseable {

	boolean isValid();

	boolean queue(Matrix4f matrix, int color, int light);

	InstancingFallbackReason getFallbackReason();

	@Override
	void close();
}
