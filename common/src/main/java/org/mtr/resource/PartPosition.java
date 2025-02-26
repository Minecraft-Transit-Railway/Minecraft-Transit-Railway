package org.mtr.mod.resource;

import org.mtr.core.serializer.ReaderBase;
import org.mtr.mod.generated.resource.PartPositionSchema;

public final class PartPosition extends PartPositionSchema {

	public PartPosition(ReaderBase readerBase) {
		super(readerBase);
		updateData(readerBase);
	}

	PartPosition() {
		super(0, 0, 0);
	}

	double getX() {
		return x;
	}

	double getY() {
		return y;
	}

	double getZ() {
		return z;
	}
}
