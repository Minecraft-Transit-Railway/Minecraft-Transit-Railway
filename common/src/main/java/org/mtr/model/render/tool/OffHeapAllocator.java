package org.mtr.model.render.tool;

import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;

public final class OffHeapAllocator {

	private static final MemoryUtil.MemoryAllocator ALLOCATOR = MemoryUtil.getAllocator(false);

	public static ByteBuffer allocate(int size) {
		final long pointer = ALLOCATOR.malloc(size);
		if (pointer == 0) {
			throw new OutOfMemoryError();
		} else {
			return MemoryUtil.memByteBuffer(pointer, size);
		}
	}

	public static void free(ByteBuffer byteBuffer) {
		ALLOCATOR.free(MemoryUtil.memAddress0(byteBuffer));
	}
}
