package org.mtr.mod.resource;

import org.joml.Matrix4f;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mapping.render.batch.MaterialProperties;
import org.mtr.mod.render.ObjBatchKey;
import org.mtr.mod.render.StaticObjMesh;

public final class VehicleGpuCache {

	public final Object2ObjectOpenHashMap<PartCondition, ObjectArrayList<Part>> partsByCondition;

	public VehicleGpuCache(Object2ObjectOpenHashMap<PartCondition, ObjectArrayList<Part>> partsByCondition) {
		this.partsByCondition = partsByCondition;
	}

	public static final class Part {

		public final PartCondition condition;
		public final StaticObjMesh mesh;
		public final ObjBatchKey batchKey;
		public final MaterialProperties materialProperties;
		public final Matrix4f localTransform;

		public Part(PartCondition condition, StaticObjMesh mesh, ObjBatchKey batchKey, MaterialProperties materialProperties, Matrix4f localTransform) {
			this.condition = condition;
			this.mesh = mesh;
			this.batchKey = batchKey;
			this.materialProperties = materialProperties;
			this.localTransform = localTransform;
		}
	}
}
