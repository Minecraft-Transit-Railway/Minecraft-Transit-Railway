package org.mtr.mod.api.instancing;

import org.mtr.mapping.render.model.RawMesh;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class InstancedModelSource {

	private final ArrayList<InstancedMeshSource> meshes = new ArrayList<>();

	public void addMesh(RawMesh rawMesh, InstancedRenderStage stage) {
		meshes.add(new InstancedMeshSource(rawMesh, stage));
	}

	public boolean isEmpty() {
		return meshes.isEmpty();
	}

	public List<InstancedMeshSource> getMeshes() {
		return Collections.unmodifiableList(meshes);
	}
}
