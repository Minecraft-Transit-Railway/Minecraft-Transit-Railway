package org.mtr.resource;

import org.mtr.data.VehicleExtension;
import org.mtr.render.StoredMatrixTransformations;

import javax.annotation.Nullable;

public final class ModelDisplayPart {

	private final ModelPropertiesPart modelPropertiesPart;
	public final StoredMatrixTransformations storedMatrixTransformations;
	public final int width;
	public final int height;
	public final double x;
	public final double y;
	public final double z;
	public final boolean flipped;

	public ModelDisplayPart(ModelPropertiesPart modelPropertiesPart, StoredMatrixTransformations storedMatrixTransformations, int width, int height, double x, double y, double z, boolean flipped) {
		this.modelPropertiesPart = modelPropertiesPart;
		this.storedMatrixTransformations = storedMatrixTransformations;
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;
		this.z = z;
		this.flipped = flipped;
	}

	public void render(StoredMatrixTransformations additionalStoredMatrixTransformations, @Nullable VehicleExtension vehicle, int carNumber, int[] scrollingDisplayIndexTracker, boolean fromResourcePackCreator) {
		if (vehicle != null) {
			modelPropertiesPart.renderDisplay(additionalStoredMatrixTransformations, this, vehicle, carNumber, scrollingDisplayIndexTracker, fromResourcePackCreator);
		}
	}
}
