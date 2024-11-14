package org.mtr.mod.resource;

import org.mtr.core.serializer.ReaderBase;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mod.generated.resource.VehicleModelWrapperSchema;

public final class VehicleModelWrapper extends VehicleModelWrapperSchema {

	VehicleModelWrapper(
			String modelResource,
			String textureResource,
			String minecraftModelPropertiesResource,
			String minecraftPositionDefinitionsResource,
			boolean flipTextureV,
			ObjectArrayList<ModelPropertiesPartWrapper> parts,
			double modelYOffset,
			String gangwayInnerSideResource,
			String gangwayInnerTopResource,
			String gangwayInnerBottomResource,
			String gangwayOuterSideResource,
			String gangwayOuterTopResource,
			String gangwayOuterBottomResource,
			double gangwayWidth,
			double gangwayHeight,
			double gangwayYOffset,
			double gangwayZOffset,
			String barrierInnerSideResource,
			String barrierInnerTopResource,
			String barrierInnerBottomResource,
			String barrierOuterSideResource,
			String barrierOuterTopResource,
			String barrierOuterBottomResource,
			double barrierWidth,
			double barrierHeight,
			double barrierYOffset,
			double barrierZOffset
	) {
		super(
				modelResource,
				textureResource,
				minecraftModelPropertiesResource,
				minecraftPositionDefinitionsResource,
				flipTextureV,
				modelYOffset,
				gangwayInnerSideResource,
				gangwayInnerTopResource,
				gangwayInnerBottomResource,
				gangwayOuterSideResource,
				gangwayOuterTopResource,
				gangwayOuterBottomResource,
				gangwayWidth,
				gangwayHeight,
				gangwayYOffset,
				gangwayZOffset,
				barrierInnerSideResource,
				barrierInnerTopResource,
				barrierInnerBottomResource,
				barrierOuterSideResource,
				barrierOuterTopResource,
				barrierOuterBottomResource,
				barrierWidth,
				barrierHeight,
				barrierYOffset,
				barrierZOffset
		);
		this.parts.addAll(parts);
	}

	public VehicleModelWrapper(ReaderBase readerBase) {
		super(readerBase);
		updateData(readerBase);
	}
}
