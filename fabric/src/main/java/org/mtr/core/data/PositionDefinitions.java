package org.mtr.core.data;

import org.mtr.core.generated.PositionDefinitionsSchema;
import org.mtr.core.serializers.ReaderBase;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.function.BiConsumer;

public final class PositionDefinitions extends PositionDefinitionsSchema {

	private final Object2ObjectOpenHashMap<String, PositionDefinition> nameToDefinition = new Object2ObjectOpenHashMap<>();

	public PositionDefinitions(ReaderBase readerBase) {
		super(readerBase);
		updateData(readerBase);
		positionDefinitions.forEach(positionDefinition -> nameToDefinition.put(positionDefinition.getName(), positionDefinition));
	}

	void getPositionDefinition(String name, BiConsumer<ObjectArrayList<PartPosition>, ObjectArrayList<PartPosition>> consumer) {
		final PositionDefinition positionDefinition = nameToDefinition.get(name);
		if (positionDefinition != null) {
			positionDefinition.getPositionLists(consumer);
		}
	}
}
