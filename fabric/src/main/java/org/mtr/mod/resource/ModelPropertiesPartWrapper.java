package org.mtr.mod.resource;

import org.mtr.core.serializer.ReaderBase;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import org.mtr.mod.Init;
import org.mtr.mod.generated.resource.ModelPropertiesPartWrapperSchema;

public final class ModelPropertiesPartWrapper extends ModelPropertiesPartWrapperSchema {

	ModelPropertiesPartWrapper(
			PositionDefinition positionDefinition,
			PartCondition condition,
			RenderStage renderStage,
			PartType type,
			double displayXPadding,
			double displayYPadding,
			String displayColorCjk,
			String displayColor,
			double displayMaxLineHeight,
			double displayCjkSizeRatio,
			ObjectArrayList<String> displayOptions,
			double displayPadZeros,
			DisplayType displayType,
			String displayDefaultText,
			double doorXMultiplier,
			double doorZMultiplier,
			DoorAnimationType doorAnimationType,
			long renderFromOpeningDoorTime,
			long renderUntilOpeningDoorTime,
			long renderFromClosingDoorTime,
			long renderUntilClosingDoorTime,
			long flashOnTime,
			long flashOffTime
	) {
		super(
				positionDefinition,
				condition,
				renderStage,
				type,
				displayXPadding,
				displayYPadding,
				displayColorCjk,
				displayColor,
				displayMaxLineHeight,
				displayCjkSizeRatio,
				displayPadZeros,
				displayType,
				displayDefaultText,
				doorXMultiplier,
				doorZMultiplier,
				doorAnimationType,
				renderFromOpeningDoorTime,
				renderUntilOpeningDoorTime,
				renderFromClosingDoorTime,
				renderUntilClosingDoorTime,
				flashOnTime,
				flashOffTime
		);
		this.displayOptions.addAll(displayOptions);
	}

	public ModelPropertiesPartWrapper(ReaderBase readerBase) {
		super(readerBase);
		updateData(readerBase);
	}

	String getName() {
		return positionDefinition.getName();
	}

	ObjectObjectImmutablePair<ModelPropertiesPart, PositionDefinition> toModelPropertiesPartAndPositionDefinition() {
		final String name = Init.randomString();
		final ObjectArrayList<PartPosition> positions = new ObjectArrayList<>();
		final ObjectArrayList<PartPosition> positionsFlipped = new ObjectArrayList<>();
		positionDefinition.getPositionLists((partPositions, partPositionsFlipped) -> {
			positions.addAll(partPositions);
			positionsFlipped.addAll(partPositionsFlipped);
		});
		return new ObjectObjectImmutablePair<>(new ModelPropertiesPart(
				ObjectArrayList.of(positionDefinition.getName()),
				ObjectArrayList.of(name),
				condition,
				renderStage,
				type,
				displayXPadding,
				displayYPadding,
				displayColorCjk,
				displayColor,
				displayMaxLineHeight,
				displayCjkSizeRatio,
				displayOptions,
				displayPadZeros,
				displayType,
				displayDefaultText,
				doorXMultiplier,
				doorZMultiplier,
				doorAnimationType,
				renderFromOpeningDoorTime,
				renderUntilOpeningDoorTime,
				renderFromClosingDoorTime,
				renderUntilClosingDoorTime,
				flashOnTime,
				flashOffTime
		), new PositionDefinition(name, positions, positionsFlipped));
	}
}
