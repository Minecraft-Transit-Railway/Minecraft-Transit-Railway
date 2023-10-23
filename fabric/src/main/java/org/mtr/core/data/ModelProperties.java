package org.mtr.core.data;

import org.mtr.core.generated.ModelPropertiesSchema;
import org.mtr.core.serializers.ReaderBase;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.mapper.ModelPartExtension;
import org.mtr.mod.data.VehicleExtension;
import org.mtr.mod.render.StoredMatrixTransformations;

public final class ModelProperties extends ModelPropertiesSchema {

	public final boolean hasGangway;
	public final boolean hasBarrier;
	public final Identifier gangwayInnerSideTexture;
	public final Identifier gangwayInnerTopTexture;
	public final Identifier gangwayInnerBottomTexture;
	public final Identifier gangwayOuterSideTexture;
	public final Identifier gangwayOuterTopTexture;
	public final Identifier gangwayOuterBottomTexture;
	public final Identifier barrierInnerSideTexture;
	public final Identifier barrierInnerTopTexture;
	public final Identifier barrierInnerBottomTexture;
	public final Identifier barrierOuterSideTexture;
	public final Identifier barrierOuterTopTexture;
	public final Identifier barrierOuterBottomTexture;

	public ModelProperties(ReaderBase readerBase) {
		super(readerBase);
		updateData(readerBase);
		hasGangway = !gangwayInnerSideResource.isEmpty() ||
				!gangwayInnerTopResource.isEmpty() ||
				!gangwayInnerBottomResource.isEmpty() ||
				!gangwayOuterSideResource.isEmpty() ||
				!gangwayOuterTopResource.isEmpty() ||
				!gangwayOuterBottomResource.isEmpty();
		hasBarrier = !barrierInnerSideResource.isEmpty() ||
				!barrierInnerTopResource.isEmpty() ||
				!barrierInnerBottomResource.isEmpty() ||
				!barrierOuterSideResource.isEmpty() ||
				!barrierOuterTopResource.isEmpty() ||
				!barrierOuterBottomResource.isEmpty();
		gangwayInnerSideTexture = VehicleModel.formatIdentifier(gangwayInnerSideResource, "png");
		gangwayInnerTopTexture = VehicleModel.formatIdentifier(gangwayInnerTopResource, "png");
		gangwayInnerBottomTexture = VehicleModel.formatIdentifier(gangwayInnerBottomResource, "png");
		gangwayOuterSideTexture = VehicleModel.formatIdentifier(gangwayOuterSideResource, "png");
		gangwayOuterTopTexture = VehicleModel.formatIdentifier(gangwayOuterTopResource, "png");
		gangwayOuterBottomTexture = VehicleModel.formatIdentifier(gangwayOuterBottomResource, "png");
		barrierInnerSideTexture = VehicleModel.formatIdentifier(barrierInnerSideResource, "png");
		barrierInnerTopTexture = VehicleModel.formatIdentifier(barrierInnerTopResource, "png");
		barrierInnerBottomTexture = VehicleModel.formatIdentifier(barrierInnerBottomResource, "png");
		barrierOuterSideTexture = VehicleModel.formatIdentifier(barrierOuterSideResource, "png");
		barrierOuterTopTexture = VehicleModel.formatIdentifier(barrierOuterTopResource, "png");
		barrierOuterBottomTexture = VehicleModel.formatIdentifier(barrierOuterBottomResource, "png");
	}

	public void render(Identifier texture, StoredMatrixTransformations storedMatrixTransformations, VehicleExtension vehicle, int light, Object2ObjectOpenHashMap<String, ModelPartExtension> nameToModelPart) {
		parts.forEach(modelPropertiesPart -> modelPropertiesPart.render(texture, storedMatrixTransformations, vehicle, light, nameToModelPart));
	}

	public double getGangwayWidth() {
		return gangwayWidth;
	}

	public double getGangwayHeight() {
		return gangwayHeight;
	}

	public double getGangwayYOffset() {
		return gangwayYOffset;
	}

	public double getGangwayZOffset() {
		return gangwayZOffset;
	}

	public double getBarrierWidth() {
		return barrierWidth;
	}

	public double getBarrierHeight() {
		return barrierHeight;
	}

	public double getBarrierYOffset() {
		return barrierYOffset;
	}

	public double getBarrierZOffset() {
		return barrierZOffset;
	}
}
