package org.mtr.mod.resource;

import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectSet;
import org.mtr.mod.render.StaticObjMesh;

import java.io.Closeable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public final class GpuObjModelWrapper implements Closeable {

	private final Object2ObjectOpenHashMap<String, ObjectArrayList<StaticObjMesh>> nameToMeshes = new Object2ObjectOpenHashMap<>();
	private final ObjectArrayList<StaticObjMesh> allMeshes = new ObjectArrayList<>();
	private final ObjectSet<String> translucentGroupNames = new ObjectOpenHashSet<>();
	private final boolean hasTranslucentMeshes;

	GpuObjModelWrapper(Map<String, List<StaticObjMesh>> meshesByName, ObjectSet<String> translucentGroupNames, boolean hasTranslucentMeshes) {
		this.hasTranslucentMeshes = hasTranslucentMeshes;
		this.translucentGroupNames.addAll(translucentGroupNames);
		meshesByName.forEach((groupName, meshes) -> {
			final ObjectArrayList<StaticObjMesh> newMeshes = new ObjectArrayList<>(meshes);
			nameToMeshes.put(groupName, newMeshes);
			allMeshes.addAll(newMeshes);
		});
	}

	public Collection<StaticObjMesh> getAllMeshes() {
		return allMeshes;
	}

	public Collection<StaticObjMesh> getMeshes(String name) {
		return nameToMeshes.getOrDefault(name, new ObjectArrayList<>());
	}

	public Collection<String> getGroupNames() {
		return nameToMeshes.keySet();
	}

	public int getGroupCount() {
		return nameToMeshes.size();
	}

	public int getMeshCount() {
		return allMeshes.size();
	}

	public boolean hasTranslucentMeshes() {
		return hasTranslucentMeshes;
	}

	public boolean isGroupTranslucent(String name) {
		return translucentGroupNames.contains(name);
	}

	@Override
	public void close() {
		allMeshes.forEach(StaticObjMesh::close);
		allMeshes.clear();
		nameToMeshes.clear();
		translucentGroupNames.clear();
	}
}
