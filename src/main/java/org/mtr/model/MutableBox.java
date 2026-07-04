package org.mtr.model;

import net.minecraft.world.phys.AABB;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;

public final class MutableBox {

	private final double[] min = {Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE};
	private final double[] max = {-Double.MAX_VALUE, -Double.MAX_VALUE, -Double.MAX_VALUE};
	private final ObjectArrayList<AABB> boxes = new ObjectArrayList<>();

	public void add(AABB box) {
		min[0] = Math.min(box.minX, min[0]);
		min[1] = Math.min(box.minY, min[1]);
		min[2] = Math.min(box.minZ, min[2]);
		max[0] = Math.max(box.maxX, max[0]);
		max[1] = Math.max(box.maxY, max[1]);
		max[2] = Math.max(box.maxZ, max[2]);
		boxes.add(box);
	}

	public void add(MutableBox mutableBox) {
		min[0] = Math.min(mutableBox.min[0], min[0]);
		min[1] = Math.min(mutableBox.min[1], min[1]);
		min[2] = Math.min(mutableBox.min[2], min[2]);
		max[0] = Math.max(mutableBox.max[0], max[0]);
		max[1] = Math.max(mutableBox.max[1], max[1]);
		max[2] = Math.max(mutableBox.max[2], max[2]);
		boxes.addAll(mutableBox.boxes);
	}

	public AABB get() {
		return new AABB(min[0], min[1], min[2], max[0], max[1], max[2]);
	}

	public ObjectArrayList<AABB> getAll() {
		return boxes;
	}
}
