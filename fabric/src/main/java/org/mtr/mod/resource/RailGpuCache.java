package org.mtr.mod.resource;

import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mapping.render.batch.MaterialProperties;
import org.mtr.mod.render.ObjBatchKey;
import org.mtr.mod.render.StaticObjMesh;

public final class RailGpuCache {

	public static final RailGpuCache EMPTY = new RailGpuCache(new ObjectArrayList<>());
	public final ObjectArrayList<Entry> entries;

	public RailGpuCache(ObjectArrayList<Entry> entries) {
		this.entries = entries;
	}

	public boolean hasEntries() {
		return !entries.isEmpty();
	}

	public static final class Entry {

		public final StaticObjMesh mesh;
		public final ObjBatchKey batchKey;
		public final MaterialProperties materialProperties;

		public Entry(StaticObjMesh mesh, ObjBatchKey batchKey, MaterialProperties materialProperties) {
			this.mesh = mesh;
			this.batchKey = batchKey;
			this.materialProperties = materialProperties;
		}
	}
}
