package org.mtr.mod.resource;

import org.mtr.core.data.TransportMode;
import org.mtr.core.serializer.ReaderBase;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArraySet;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import org.mtr.mapping.holder.Box;
import org.mtr.mapping.holder.MutableText;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mod.client.CustomResourceLoader;
import org.mtr.mod.data.VehicleExtension;
import org.mtr.mod.generated.resource.VehicleResourceSchema;
import org.mtr.mod.render.DynamicVehicleModel;
import org.mtr.mod.render.RenderTrains;
import org.mtr.mod.render.StoredMatrixTransformations;
import org.mtr.mod.sound.BveVehicleSound;
import org.mtr.mod.sound.BveVehicleSoundConfig;
import org.mtr.mod.sound.LegacyVehicleSound;
import org.mtr.mod.sound.VehicleSoundBase;

import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class VehicleResource extends VehicleResourceSchema {

	public final ObjectImmutableList<Box> floors;
	public final ObjectImmutableList<Box> doorways;
	public final Supplier<VehicleSoundBase> createVehicleSoundBase;
	private final Object2ObjectOpenHashMap<PartCondition, OptimizedModelWrapper> optimizedModels;
	private final Object2ObjectOpenHashMap<PartCondition, OptimizedModelWrapper> optimizedModelsDoorsClosed;
	private final Object2ObjectOpenHashMap<PartCondition, OptimizedModelWrapper> optimizedModelsBogie1;
	private final Object2ObjectOpenHashMap<PartCondition, OptimizedModelWrapper> optimizedModelsBogie2;

	public VehicleResource(ReaderBase readerBase, @Nullable VehicleModel extraModel, @Nullable Box extraFloor, ObjectArraySet<Box> doorways) {
		super(readerBase);
		updateData(readerBase);

		final ObjectArraySet<Box> floors = new ObjectArraySet<>();
		final Object2ObjectOpenHashMap<PartCondition, ObjectArrayList<OptimizedModelWrapper.MaterialGroupWrapper>> materialGroupsModel = new Object2ObjectOpenHashMap<>();
		final Object2ObjectOpenHashMap<PartCondition, ObjectArrayList<OptimizedModelWrapper.MaterialGroupWrapper>> materialGroupsModelDoorsClosed = new Object2ObjectOpenHashMap<>();
		final Object2ObjectOpenHashMap<PartCondition, ObjectArrayList<OptimizedModelWrapper.MaterialGroupWrapper>> materialGroupsBogie1Model = new Object2ObjectOpenHashMap<>();
		final Object2ObjectOpenHashMap<PartCondition, ObjectArrayList<OptimizedModelWrapper.MaterialGroupWrapper>> materialGroupsBogie2Model = new Object2ObjectOpenHashMap<>();

		if (extraModel != null) {
			models.add(extraModel);
		}
		if (extraFloor != null) {
			floors.add(extraFloor);
		}

		forEachNonNull(models, dynamicVehicleModel -> dynamicVehicleModel.writeFloorsAndDoorways(floors, doorways, materialGroupsModel, materialGroupsModelDoorsClosed));
		forEachNonNull(models, dynamicVehicleModel -> dynamicVehicleModel.modelProperties.iterateParts(modelPropertiesPart -> modelPropertiesPart.mapDoors(doorways)));
		forEachNonNull(bogie1Models, dynamicVehicleModel -> dynamicVehicleModel.writeFloorsAndDoorways(new ObjectArraySet<>(), new ObjectArraySet<>(), new Object2ObjectOpenHashMap<>(), materialGroupsBogie1Model));
		forEachNonNull(bogie2Models, dynamicVehicleModel -> dynamicVehicleModel.writeFloorsAndDoorways(new ObjectArraySet<>(), new ObjectArraySet<>(), new Object2ObjectOpenHashMap<>(), materialGroupsBogie2Model));

		this.floors = new ObjectImmutableList<>(floors);
		this.doorways = new ObjectImmutableList<>(doorways);

		optimizedModels = writeToOptimizedModels(materialGroupsModel);
		optimizedModelsDoorsClosed = writeToOptimizedModels(materialGroupsModelDoorsClosed);
		optimizedModelsBogie1 = writeToOptimizedModels(materialGroupsBogie1Model);
		optimizedModelsBogie2 = writeToOptimizedModels(materialGroupsBogie2Model);

		if (bveSoundBaseResource.isEmpty()) {
			final LegacyVehicleSound legacyVehicleSound = new LegacyVehicleSound(legacySpeedSoundBaseResource, (int) legacySpeedSoundCount, legacyUseAccelerationSoundsWhenCoasting, legacyConstantPlaybackSpeed, legacyDoorSoundBaseResource, legacyDoorCloseSoundTime);
			createVehicleSoundBase = () -> legacyVehicleSound;
		} else {
			final BveVehicleSoundConfig bveVehicleSoundConfig = new BveVehicleSoundConfig(bveSoundBaseResource);
			createVehicleSoundBase = () -> new BveVehicleSound(bveVehicleSoundConfig);
		}
	}

	public VehicleResource(ReaderBase readerBase) {
		this(readerBase, null, null, new ObjectArraySet<>());
	}

	public void queue(StoredMatrixTransformations storedMatrixTransformations, VehicleExtension vehicle, int light, ObjectArrayList<Box> openDoorways) {
		if (openDoorways.isEmpty()) {
			queue(optimizedModelsDoorsClosed, storedMatrixTransformations, vehicle, light, true);
		} else {
			queue(optimizedModels, storedMatrixTransformations, vehicle, light, false);
		}
	}

	public void queueBogie(int bogieIndex, StoredMatrixTransformations storedMatrixTransformations, VehicleExtension vehicle, int light) {
		if (Utilities.isBetween(bogieIndex, 0, 1)) {
			queue(bogieIndex == 0 ? optimizedModelsBogie1 : optimizedModelsBogie2, storedMatrixTransformations, vehicle, light, true);
		}
	}

	public String getId() {
		return id;
	}

	public MutableText getName() {
		return TextHelper.translatable(name);
	}

	public int getColor() {
		return CustomResourceTools.colorStringToInt(color);
	}

	public TransportMode getTransportMode() {
		return transportMode;
	}

	public double getLength() {
		return length;
	}

	public double getWidth() {
		return width;
	}

	public double getBogie1Position() {
		return bogie1Position;
	}

	public double getBogie2Position() {
		return bogie2Position;
	}

	public double getCouplingPadding1() {
		return couplingPadding1;
	}

	public double getCouplingPadding2() {
		return couplingPadding2;
	}

	public MutableText getDescription() {
		return TextHelper.translatable(description);
	}

	public String getWikipediaArticle() {
		return wikipediaArticle;
	}

	public void iterateModels(ModelConsumer modelConsumer) {
		for (int i = 0; i < models.size(); i++) {
			final VehicleModel vehicleModel = models.get(i);
			if (vehicleModel != null && vehicleModel.model != null) {
				modelConsumer.accept(i, vehicleModel.model);
			}
		}
	}

	public void iterateBogieModels(int bogieIndex, Consumer<DynamicVehicleModel> consumer) {
		if (Utilities.isBetween(bogieIndex, 0, 1)) {
			(bogieIndex == 0 ? bogie1Models : bogie2Models).forEach(vehicleModel -> {
				if (vehicleModel.model != null) {
					consumer.accept(vehicleModel.model);
				}
			});
		}
	}

	public boolean hasGangway1() {
		return hasGangway1;
	}

	public boolean hasGangway2() {
		return hasGangway2;
	}

	public boolean hasBarrier1() {
		return hasBarrier1;
	}

	public boolean hasBarrier2() {
		return hasBarrier2;
	}

	public static boolean matchesCondition(VehicleExtension vehicle, PartCondition partCondition, boolean noOpenDoorways) {
		switch (partCondition) {
			case AT_DEPOT:
				return !vehicle.getIsOnRoute();
			case ON_ROUTE_FORWARDS:
				return vehicle.getIsOnRoute() && !vehicle.getReversed();
			case ON_ROUTE_BACKWARDS:
				return vehicle.getIsOnRoute() && vehicle.getReversed();
			case DOORS_CLOSED:
				return vehicle.persistentVehicleData.getDoorValue() == 0 || noOpenDoorways;
			case DOORS_OPENED:
				return vehicle.persistentVehicleData.getDoorValue() > 0 && !noOpenDoorways;
			default:
				return true;
		}
	}

	private static void queue(Object2ObjectOpenHashMap<PartCondition, OptimizedModelWrapper> optimizedModels, StoredMatrixTransformations storedMatrixTransformations, VehicleExtension vehicle, int light, boolean noOpenDoorways) {
		optimizedModels.forEach((partCondition, optimizedModel) -> {
			if (matchesCondition(vehicle, partCondition, noOpenDoorways)) {
				RenderTrains.scheduleRender(RenderTrains.QueuedRenderLayer.TEXT, (graphicsHolder, offset) -> {
					storedMatrixTransformations.transform(graphicsHolder, offset);
					CustomResourceLoader.OPTIMIZED_RENDERER_WRAPPER.queue(optimizedModel, graphicsHolder, light);
					graphicsHolder.pop();
				});
			}
		});
	}

	private static Object2ObjectOpenHashMap<PartCondition, OptimizedModelWrapper> writeToOptimizedModels(Object2ObjectOpenHashMap<PartCondition, ObjectArrayList<OptimizedModelWrapper.MaterialGroupWrapper>> materialGroupsModel) {
		final Object2ObjectOpenHashMap<PartCondition, OptimizedModelWrapper> optimizedModels = new Object2ObjectOpenHashMap<>();
		materialGroupsModel.forEach((partCondition, materialGroups) -> optimizedModels.put(partCondition, new OptimizedModelWrapper(materialGroups)));
		return optimizedModels;
	}

	private static void forEachNonNull(ObjectArrayList<VehicleModel> models, Consumer<DynamicVehicleModel> consumer) {
		models.forEach(vehicleModel -> {
			if (vehicleModel.model != null) {
				consumer.accept(vehicleModel.model);
			}
		});
	}

	@FunctionalInterface
	public interface ModelConsumer {
		void accept(int index, DynamicVehicleModel dynamicVehicleModel);
	}
}
