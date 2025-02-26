package org.mtr.mod.resource;

import org.mtr.core.data.TransportMode;
import org.mtr.core.serializer.ReaderBase;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mod.generated.resource.VehicleResourceWrapperSchema;

import javax.annotation.Nullable;
import java.util.stream.Collectors;

public final class VehicleResourceWrapper extends VehicleResourceWrapperSchema {

	VehicleResourceWrapper(
			String id,
			String name,
			String color,
			TransportMode transportMode,
			double length,
			double width,
			double bogie1Position,
			double bogie2Position,
			double couplingPadding1,
			double couplingPadding2,
			String description,
			String wikipediaArticle,
			ObjectArrayList<String> tags,
			ObjectArrayList<VehicleModelWrapper> models,
			ObjectArrayList<VehicleModelWrapper> bogie1Models,
			ObjectArrayList<VehicleModelWrapper> bogie2Models,
			boolean hasGangway1,
			boolean hasGangway2,
			boolean hasBarrier1,
			boolean hasBarrier2,
			double legacyRiderOffset,
			String bveSoundBaseResource,
			String legacySpeedSoundBaseResource,
			long legacySpeedSoundCount,
			boolean legacyUseAccelerationSoundsWhenCoasting,
			boolean legacyConstantPlaybackSpeed,
			String legacyDoorSoundBaseResource,
			double legacyDoorCloseSoundTime
	) {
		super(
				id,
				name,
				color,
				transportMode,
				length,
				width,
				bogie1Position,
				bogie2Position,
				couplingPadding1,
				couplingPadding2,
				description,
				wikipediaArticle,
				hasGangway1,
				hasGangway2,
				hasBarrier1,
				hasBarrier2,
				legacyRiderOffset,
				bveSoundBaseResource,
				legacySpeedSoundBaseResource,
				legacySpeedSoundCount,
				legacyUseAccelerationSoundsWhenCoasting,
				legacyConstantPlaybackSpeed,
				legacyDoorSoundBaseResource,
				legacyDoorCloseSoundTime
		);
		this.tags.addAll(tags);
		this.models.addAll(models);
		this.bogie1Models.addAll(bogie1Models);
		this.bogie2Models.addAll(bogie2Models);
	}

	public VehicleResourceWrapper(ReaderBase readerBase) {
		super(readerBase);
		updateData(readerBase);
	}

	public VehicleResource toVehicleResource(
			ResourceProvider resourceProvider,
			@Nullable Object2ObjectArrayMap<String, ModelProperties> modelPropertiesMap,
			@Nullable Object2ObjectArrayMap<String, PositionDefinitions> positionDefinitionsMap
	) {
		return new VehicleResource(
				id,
				name,
				color,
				transportMode,
				length,
				width,
				bogie1Position,
				bogie2Position,
				couplingPadding1,
				couplingPadding2,
				description,
				wikipediaArticle,
				tags,
				models.stream().map(vehicleModelWrapper -> vehicleModelWrapper.toVehicleModel(resourceProvider, modelPropertiesMap, positionDefinitionsMap)).collect(Collectors.toCollection(ObjectArrayList::new)),
				bogie1Models.stream().map(vehicleModelWrapper -> vehicleModelWrapper.toVehicleModel(resourceProvider, modelPropertiesMap, positionDefinitionsMap)).collect(Collectors.toCollection(ObjectArrayList::new)),
				bogie2Models.stream().map(vehicleModelWrapper -> vehicleModelWrapper.toVehicleModel(resourceProvider, modelPropertiesMap, positionDefinitionsMap)).collect(Collectors.toCollection(ObjectArrayList::new)),
				hasGangway1,
				hasGangway2,
				hasBarrier1,
				hasBarrier2,
				legacyRiderOffset,
				bveSoundBaseResource,
				legacySpeedSoundBaseResource,
				legacySpeedSoundCount,
				legacyUseAccelerationSoundsWhenCoasting,
				legacyConstantPlaybackSpeed,
				legacyDoorSoundBaseResource,
				legacyDoorCloseSoundTime,
				resourceProvider
		);
	}

	public String getId() {
		return id;
	}

	void clean() {
		models.forEach(VehicleModelWrapper::clean);
	}
}
