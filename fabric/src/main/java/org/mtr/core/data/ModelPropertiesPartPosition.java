package org.mtr.core.data;

import org.mtr.core.generated.ModelPropertiesPartPositionSchema;
import org.mtr.core.serializers.ReaderBase;

public final class ModelPropertiesPartPosition extends ModelPropertiesPartPositionSchema {

	public ModelPropertiesPartPosition(ReaderBase readerBase) {
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
