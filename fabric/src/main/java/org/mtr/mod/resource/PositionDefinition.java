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

	PositionDefinition(String name, ObjectArrayList<PartPosition> positions, ObjectArrayList<PartPosition> positionsFlipped) {
		super(name);
		this.positions.addAll(positions);
		this.positionsFlipped.addAll(positionsFlipped);
	}

	String getName() {
		return name;
	}

	void getPositionLists(BiConsumer<ObjectArrayList<PartPosition>, ObjectArrayList<PartPosition>> consumer) {
		consumer.accept(positions, positionsFlipped);
	}
}
