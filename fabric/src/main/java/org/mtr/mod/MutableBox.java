package org.mtr.mod;

import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArraySet;
import org.mtr.mapping.holder.Box;

public final class MutableBox {

	private final double[] min = {Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE};
	private final double[] max = {-Double.MAX_VALUE, -Double.MAX_VALUE, -Double.MAX_VALUE};
	private final ObjectArraySet<Box> boxes = new ObjectArraySet<>();

	public void add(Box box) {
		min[0] = Math.min(box.getMinXMapped(), min[0]);
		min[1] = Math.min(box.getMinYMapped(), min[1]);
		min[2] = Math.min(box.getMinZMapped(), min[2]);
		max[0] = Math.max(box.getMaxXMapped(), max[0]);
		max[1] = Math.max(box.getMaxYMapped(), max[1]);
		max[2] = Math.max(box.getMaxZMapped(), max[2]);
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

	public Box get() {
		return new Box(min[0], min[1], min[2], max[0], max[1], max[2]);
	}

	public ObjectArraySet<Box> getAll() {
		return boxes;
	}
}
