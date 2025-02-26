package org.mtr.mod.resource;

import org.mtr.core.serializer.ReaderBase;
import org.mtr.mod.generated.resource.ModelMirrorSchema;

public final class ModelMirror extends ModelMirrorSchema {

	public ModelMirror(ReaderBase readerBase) {
		super(readerBase);
		updateData(readerBase);
	}

	ModelMirror() {
		super(false, false, false);
	}

	boolean getX() {
		return x;
	}

	boolean getY() {
		return y;
	}

	boolean getZ() {
		return z;
	}
}
