package org.mtr.core.data;

import org.mtr.core.generated.PartPositionSchema;
import org.mtr.core.serializers.ReaderBase;

public final class PartPosition extends PartPositionSchema {

	public PartPosition(ReaderBase readerBase) {
		super(readerBase);
		updateData(readerBase);
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
