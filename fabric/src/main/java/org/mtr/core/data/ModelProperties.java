package org.mtr.core.data;

import org.mtr.core.generated.ModelPropertiesSchema;
import org.mtr.core.serializers.ReaderBase;
import org.mtr.mapping.holder.Identifier;

import java.util.function.Consumer;

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
		gangwayInnerSideTexture = CustomResourceTools.formatIdentifier(gangwayInnerSideResource, "png");
		gangwayInnerTopTexture = CustomResourceTools.formatIdentifier(gangwayInnerTopResource, "png");
		gangwayInnerBottomTexture = CustomResourceTools.formatIdentifier(gangwayInnerBottomResource, "png");
		gangwayOuterSideTexture = CustomResourceTools.formatIdentifier(gangwayOuterSideResource, "png");
		gangwayOuterTopTexture = CustomResourceTools.formatIdentifier(gangwayOuterTopResource, "png");
		gangwayOuterBottomTexture = CustomResourceTools.formatIdentifier(gangwayOuterBottomResource, "png");
		barrierInnerSideTexture = CustomResourceTools.formatIdentifier(barrierInnerSideResource, "png");
		barrierInnerTopTexture = CustomResourceTools.formatIdentifier(barrierInnerTopResource, "png");
		barrierInnerBottomTexture = CustomResourceTools.formatIdentifier(barrierInnerBottomResource, "png");
		barrierOuterSideTexture = CustomResourceTools.formatIdentifier(barrierOuterSideResource, "png");
		barrierOuterTopTexture = CustomResourceTools.formatIdentifier(barrierOuterTopResource, "png");
		barrierOuterBottomTexture = CustomResourceTools.formatIdentifier(barrierOuterBottomResource, "png");
	}

	public void iterateParts(Consumer<ModelPropertiesPart> consumer) {
		parts.forEach(consumer);
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
