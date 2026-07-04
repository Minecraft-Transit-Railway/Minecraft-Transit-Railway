package org.mtr.resource;

import net.minecraft.resources.ResourceLocation;
import org.jspecify.annotations.Nullable;
import org.mtr.core.serializer.ReaderBase;
import org.mtr.generated.resource.ModelPropertiesSchema;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectSet;

import java.util.function.Consumer;

public final class ModelProperties extends ModelPropertiesSchema {

	@Nullable
	public final ResourceLocation gangwayInnerSideTexture;
	@Nullable
	public final ResourceLocation gangwayInnerTopTexture;
	@Nullable
	public final ResourceLocation gangwayInnerBottomTexture;
	@Nullable
	public final ResourceLocation gangwayOuterSideTexture;
	@Nullable
	public final ResourceLocation gangwayOuterTopTexture;
	@Nullable
	public final ResourceLocation gangwayOuterBottomTexture;
	@Nullable
	public final ResourceLocation barrierInnerSideTexture;
	@Nullable
	public final ResourceLocation barrierInnerTopTexture;
	@Nullable
	public final ResourceLocation barrierInnerBottomTexture;
	@Nullable
	public final ResourceLocation barrierOuterSideTexture;
	@Nullable
	public final ResourceLocation barrierOuterTopTexture;
	@Nullable
	public final ResourceLocation barrierOuterBottomTexture;

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

	ModelProperties(double modelYOffset) {
		super(modelYOffset, "", "", "", "", "", "", 0, 0, 0, 0, "", "", "", "", "", "", 0, 0, 0, 0);
		gangwayInnerSideTexture = null;
		gangwayInnerTopTexture = null;
		gangwayInnerBottomTexture = null;
		gangwayOuterSideTexture = null;
		gangwayOuterTopTexture = null;
		gangwayOuterBottomTexture = null;
		barrierInnerSideTexture = null;
		barrierInnerTopTexture = null;
		barrierInnerBottomTexture = null;
		barrierOuterSideTexture = null;
		barrierOuterTopTexture = null;
		barrierOuterBottomTexture = null;
	}

	ModelProperties(
		ObjectArrayList<ModelPropertiesPart> parts,
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

	VehicleModelWrapper toVehicleModelWrapper(String modelResource, String textureResource, String modelPropertiesResource, String positionDefinitionsResource, boolean flipTextureV, ObjectArrayList<ModelPropertiesPartWrapper> parts) {
		return new VehicleModelWrapper(
			modelResource,
			textureResource,
			modelPropertiesResource,
			positionDefinitionsResource,
			flipTextureV,
			parts,
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
	}

	public void addPartsIfEmpty(ObjectSet<String> partNames) {
		if (parts.isEmpty()) {
			parts.add(new ModelPropertiesPart(partNames));
		}
	}
}
