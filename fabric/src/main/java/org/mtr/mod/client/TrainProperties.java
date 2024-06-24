package org.mtr.mod.client;

import org.mtr.mapping.holder.MutableText;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mod.render.LegacyVehicleRenderer;
import org.mtr.mod.sound.LegacyVehicleSound;
import org.mtr.mod.sound.VehicleSoundBase;

import javax.annotation.Nullable;

public class TrainProperties {

	public final String baseTrainType;
	public final MutableText name;
	public final String description;
	public final String wikipediaArticle;
	public final int color;
	public final float riderOffset;
	public final float riderOffsetDismounting;
	public final float bogiePosition;
	public final boolean isJacobsBogie;
	public final boolean hasGangwayConnection;
	public final LegacyVehicleRenderer legacyVehicleRenderer;
	public final VehicleSoundBase vehicleSound;

	public TrainProperties(String baseTrainType, MutableText name, @Nullable String description, @Nullable String wikipediaArticle, int color, float riderOffset, float riderOffsetDismounting, float bogiePosition, boolean isJacobsBogie, boolean hasGangwayConnection, LegacyVehicleRenderer legacyVehicleRenderer, VehicleSoundBase vehicleSound) {
		this.baseTrainType = baseTrainType;
		this.name = name;
		this.description = description;
		this.wikipediaArticle = wikipediaArticle;
		this.color = color;
		this.riderOffset = riderOffset;
		this.riderOffsetDismounting = riderOffsetDismounting;
		this.bogiePosition = bogiePosition;
		this.isJacobsBogie = isJacobsBogie;
		this.hasGangwayConnection = hasGangwayConnection;
		this.legacyVehicleRenderer = legacyVehicleRenderer;
		this.vehicleSound = vehicleSound;
	}

	public static TrainProperties getBlankProperties() {
		return new TrainProperties(
				"",
				TextHelper.translatable(""),
				null,
				null,
				0,
				0,
				0,
				0,
				false,
				false,
				new LegacyVehicleRenderer(null, "", "", ""),
				new LegacyVehicleSound("", 0, false, false, "", 0)
		);
	}
}
