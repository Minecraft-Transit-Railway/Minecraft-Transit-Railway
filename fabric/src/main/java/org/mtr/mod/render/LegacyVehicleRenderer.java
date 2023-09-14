package org.mtr.mod.render;

import org.mtr.mod.model.ModelTrainBase;

import javax.annotation.Nullable;

public class LegacyVehicleRenderer {

	public final ModelTrainBase model;
	public final String textureId;
	public final String gangwayConnectionId;
	public final String trainBarrierId;

	public LegacyVehicleRenderer(@Nullable ModelTrainBase model, String textureId, String gangwayConnectionId, String trainBarrierId) {
		this.model = model;
		this.textureId = textureId;
		this.gangwayConnectionId = gangwayConnectionId;
		this.trainBarrierId = trainBarrierId;
	}
}
