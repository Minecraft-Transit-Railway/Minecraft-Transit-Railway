package org.mtr.mod.resource;

import org.mtr.core.serializer.ReaderBase;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mod.generated.resource.PositionDefinitionSchema;

import java.util.function.BiConsumer;

public final class PositionDefinition extends PositionDefinitionSchema {

	public PositionDefinition(ReaderBase readerBase) {
		super(readerBase);
		updateData(readerBase);
	}

	PositionDefinition() {
		super("");
		positions.add(new PartPosition());
	}

	public String getName() {
		return name;
	}

	void getPositionLists(BiConsumer<ObjectArrayList<PartPosition>, ObjectArrayList<PartPosition>> consumer) {
		consumer.accept(positions, positionsFlipped);
	}
}
