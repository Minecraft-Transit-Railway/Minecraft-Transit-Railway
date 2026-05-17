package org.mtr.mod.resource;

import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mod.render.StaticObjMesh;

import java.io.Closeable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public final class GpuObjModelWrapper implements Closeable {

	private final Object2ObjectOpenHashMap<String, ObjectArrayList<StaticObjMesh>> nameToMeshes = new Object2ObjectOpenHashMap<>();
	private final ObjectArrayList<StaticObjMesh> allMeshes = new ObjectArrayList<>();

	GpuObjModelWrapper(Map<String, List<StaticObjMesh>> meshesByName) {
		meshesByName.forEach((groupName, meshes) -> {
			final ObjectArrayList<StaticObjMesh> newMeshes = new ObjectArrayList<>(meshes);
			nameToMeshes.put(groupName, newMeshes);
			allMeshes.addAll(newMeshes);
		});
	}

	public Collection<StaticObjMesh> getMeshes(String name) {
		return nameToMeshes.getOrDefault(name, new ObjectArrayList<>());
	}

	@Override
	public void close() {
		allMeshes.forEach(StaticObjMesh::close);
		allMeshes.clear();
		nameToMeshes.clear();
	}
}
