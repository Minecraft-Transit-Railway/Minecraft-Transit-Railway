package org.mtr.core.data;

import org.mtr.core.generated.PositionDefinitionSchema;
import org.mtr.core.serializers.ReaderBase;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.function.BiConsumer;

public final class PositionDefinition extends PositionDefinitionSchema {

	public PositionDefinition(ReaderBase readerBase) {
		super(readerBase);
		updateData(readerBase);
	}

	public String getName() {
		return name;
	}

	void getPositionLists(BiConsumer<ObjectArrayList<PartPosition>, ObjectArrayList<PartPosition>> consumer) {
		consumer.accept(positions, positionsFlipped);
	}
}
