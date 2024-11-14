package org.mtr.mod.resource;

import org.mtr.core.serializer.ReaderBase;
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
			double displayPadZeros,
			DisplayType displayType,
			String displayDefaultText,
			double doorXMultiplier,
			double doorZMultiplier,
			DoorAnimationType doorAnimationType
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
				doorAnimationType
		);
	}

	public ModelPropertiesPartWrapper(ReaderBase readerBase) {
		super(readerBase);
	}
}
