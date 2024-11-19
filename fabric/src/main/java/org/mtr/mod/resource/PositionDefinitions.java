package org.mtr.mod.resource;

import org.mtr.core.serializer.ReaderBase;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mod.generated.resource.PositionDefinitionsSchema;

import java.util.function.BiConsumer;

public final class PositionDefinitions extends PositionDefinitionsSchema {

	private final Object2ObjectOpenHashMap<String, PositionDefinition> nameToDefinition = new Object2ObjectOpenHashMap<>();

	public PositionDefinitions(ReaderBase readerBase) {
		super(readerBase);
		updateData(readerBase);
		positionDefinitions.forEach(positionDefinition -> nameToDefinition.put(positionDefinition.getName(), positionDefinition));
	}

	PositionDefinitions() {
		super();
		final PositionDefinition positionDefinition = new PositionDefinition();
		positionDefinitions.add(positionDefinition);
		nameToDefinition.put("", positionDefinition);
	}

	PositionDefinitions(ObjectArrayList<PositionDefinition> positionDefinitions) {
		super();
		this.positionDefinitions.addAll(positionDefinitions);
		positionDefinitions.forEach(positionDefinition -> nameToDefinition.put(positionDefinition.getName(), positionDefinition));
	}

	void getPositionDefinition(String name, BiConsumer<ObjectArrayList<PartPosition>, ObjectArrayList<PartPosition>> consumer) {
		final PositionDefinition positionDefinition = nameToDefinition.get(name);
		if (positionDefinition != null) {
			positionDefinition.getPositionLists(consumer);
		}
	}
}
