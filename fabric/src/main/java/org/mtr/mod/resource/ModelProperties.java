package org.mtr.mod.resource;

import org.mtr.core.serializer.ReaderBase;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mod.generated.resource.ModelPropertiesSchema;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public final class ModelProperties extends ModelPropertiesSchema {

	@Nullable
	public final Identifier gangwayInnerSideTexture;
	@Nullable
	public final Identifier gangwayInnerTopTexture;
	@Nullable
	public final Identifier gangwayInnerBottomTexture;
	@Nullable
	public final Identifier gangwayOuterSideTexture;
	@Nullable
	public final Identifier gangwayOuterTopTexture;
	@Nullable
	public final Identifier gangwayOuterBottomTexture;
	@Nullable
	public final Identifier barrierInnerSideTexture;
	@Nullable
	public final Identifier barrierInnerTopTexture;
	@Nullable
	public final Identifier barrierInnerBottomTexture;
	@Nullable
	public final Identifier barrierOuterSideTexture;
	@Nullable
	public final Identifier barrierOuterTopTexture;
	@Nullable
	public final Identifier barrierOuterBottomTexture;

	public ModelProperties(ReaderBase readerBase) {
		super(readerBase);
		updateData(readerBase);
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

	public double getModelYOffset() {
		return modelYOffset;
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
