package org.mtr.resource;

import org.mtr.core.serializer.ReaderBase;
import org.mtr.generated.resource.ModelMirrorSchema;

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
