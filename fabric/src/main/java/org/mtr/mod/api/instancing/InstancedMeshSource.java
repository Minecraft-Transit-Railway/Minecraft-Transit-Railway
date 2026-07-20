package org.mtr.mod.api.instancing;

import org.mtr.mapping.render.model.RawMesh;

public final class InstancedMeshSource {

	public final RawMesh rawMesh;
	public final InstancedRenderStage stage;

	InstancedMeshSource(RawMesh rawMesh, InstancedRenderStage stage) {
		this.rawMesh = rawMesh;
		this.stage = stage;
	}
}
