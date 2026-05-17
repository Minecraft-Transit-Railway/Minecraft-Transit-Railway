package org.mtr.mod.render;

import org.joml.Matrix4f;

public final class ObjInstance {

	public final Matrix4f matrix;
	public final int packedLight;
	public final int packedColor;
	public final int flags;
	public final boolean useDefaultOffset;

	public ObjInstance(Matrix4f matrix, int packedLight, int packedColor, int flags, boolean useDefaultOffset) {
		this.matrix = matrix;
		this.packedLight = packedLight;
		this.packedColor = packedColor;
		this.flags = flags;
		this.useDefaultOffset = useDefaultOffset;
	}
}
