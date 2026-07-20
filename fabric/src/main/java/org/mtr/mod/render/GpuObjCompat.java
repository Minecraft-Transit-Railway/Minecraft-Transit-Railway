package org.mtr.mod.render;

import org.mtr.mapping.mapper.OptimizedRenderer;

public final class GpuObjCompat {

	public static boolean isSupported() {
		return OptimizedRenderer.hasOptimizedRendering();
	}

	private GpuObjCompat() {
	}
}
