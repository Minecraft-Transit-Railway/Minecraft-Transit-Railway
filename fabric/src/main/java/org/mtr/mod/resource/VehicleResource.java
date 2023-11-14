package org.mtr.mod.resource;

import org.mtr.core.data.TransportMode;
import org.mtr.core.serializer.ReaderBase;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArraySet;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import org.mtr.mapping.holder.Box;
import org.mtr.mapping.holder.MutableText;
import org.mtr.mapping.mapper.OptimizedModel;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mod.Init;
import org.mtr.mod.client.CustomResourceLoader;
import org.mtr.mod.generated.resource.VehicleResourceSchema;
import org.mtr.mod.render.DynamicVehicleModel;
import org.mtr.mod.render.RenderTrains;
import org.mtr.mod.render.StoredMatrixTransformations;

import java.util.Locale;
import java.util.function.Consumer;

public final class VehicleResource extends VehicleResourceSchema {

	public final ObjectImmutableList<Box> floors;
	public final ObjectImmutableList<Box> doorways;
	private final OptimizedModel optimizedModel;
	private final OptimizedModel optimizedModelBogie1;
	private final OptimizedModel optimizedModelBogie2;

	public VehicleResource(ReaderBase readerBase) {
		super(readerBase);
		updateData(readerBase);
		final ObjectArraySet<Box> floors = new ObjectArraySet<>();
		final ObjectArraySet<Box> doorways = new ObjectArraySet<>();
		final ObjectArrayList<OptimizedModel.MaterialGroup> materialGroupsModel = new ObjectArrayList<>();
		final ObjectArrayList<OptimizedModel.MaterialGroup> materialGroupsBogie1Model = new ObjectArrayList<>();
		final ObjectArrayList<OptimizedModel.MaterialGroup> materialGroupsBogie2Model = new ObjectArrayList<>();
		models.forEach(vehicleModel -> vehicleModel.model.writeFloorsAndDoorways(floors, doorways, materialGroupsModel));
		models.forEach(vehicleModel -> vehicleModel.model.modelProperties.iterateParts(modelPropertiesPart -> modelPropertiesPart.mapDoors(doorways)));
		bogie1Models.forEach(vehicleModel -> vehicleModel.model.writeFloorsAndDoorways(new ObjectArraySet<>(), new ObjectArraySet<>(), materialGroupsBogie1Model));
		bogie2Models.forEach(vehicleModel -> vehicleModel.model.writeFloorsAndDoorways(new ObjectArraySet<>(), new ObjectArraySet<>(), materialGroupsBogie2Model));
		this.floors = new ObjectImmutableList<>(floors);
		this.doorways = new ObjectImmutableList<>(doorways);
		optimizedModel = new OptimizedModel(materialGroupsModel);
		optimizedModelBogie1 = new OptimizedModel(materialGroupsBogie1Model);
		optimizedModelBogie2 = new OptimizedModel(materialGroupsBogie2Model);
	}

	public void print() {
		Init.LOGGER.info(String.format("%s:%s", transportMode.toString().toLowerCase(Locale.ENGLISH), id));
	}

	public void queue(StoredMatrixTransformations storedMatrixTransformations, int light) {
		queue(optimizedModel, storedMatrixTransformations, light);
	}

	public void queueBogie(int bogieIndex, StoredMatrixTransformations storedMatrixTransformations, int light) {
		if (Utilities.isBetween(bogieIndex, 0, 1)) {
			queue(bogieIndex == 0 ? optimizedModelBogie1 : optimizedModelBogie2, storedMatrixTransformations, light);
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
			if (vehicleModel != null) {
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

	private static void queue(OptimizedModel optimizedModel, StoredMatrixTransformations storedMatrixTransformations, int light) {
		RenderTrains.scheduleRender(RenderTrains.QueuedRenderLayer.TEXT, (graphicsHolder, offset) -> {
			storedMatrixTransformations.transform(graphicsHolder, offset);
			CustomResourceLoader.OPTIMIZED_RENDERER.queue(optimizedModel, graphicsHolder, light);
			graphicsHolder.pop();
		});
	}

	@FunctionalInterface
	public interface ModelConsumer {
		void accept(int index, DynamicVehicleModel dynamicVehicleModel);
	}
}
