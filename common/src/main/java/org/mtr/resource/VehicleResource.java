package org.mtr.resource;

import it.unimi.dsi.fastutil.ints.Int2ObjectAVLTreeMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectAVLTreeMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;
import org.mtr.MTR;
import org.mtr.config.Config;
import org.mtr.core.data.TransportMode;
import org.mtr.core.serializer.ReaderBase;
import org.mtr.core.tool.Utilities;
import org.mtr.data.VehicleExtension;
import org.mtr.generated.resource.VehicleResourceSchema;
import org.mtr.model.BuiltVehicleModelHolder;
import org.mtr.render.StoredMatrixTransformations;
import org.mtr.sound.BveVehicleSound;
import org.mtr.sound.BveVehicleSoundConfig;
import org.mtr.sound.LegacyVehicleSound;
import org.mtr.sound.VehicleSoundBase;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public final class VehicleResource extends VehicleResourceSchema {

	public final Supplier<VehicleSoundBase> createVehicleSoundBase;
	public final boolean shouldPreload;
	@Nullable
	private final LegacyVehicleSupplier<ObjectArrayList<VehicleModel>> extraModelsSupplier;
	private final Int2ObjectAVLTreeMap<Int2ObjectAVLTreeMap<ObjectArrayList<VehicleModel>>> allModels = new Int2ObjectAVLTreeMap<>();
	private final Int2ObjectAVLTreeMap<Int2ObjectAVLTreeMap<CachedResource<CachedResource<CachedResource<VehicleResourceCache>>>>> cachedVehicleResource = new Int2ObjectAVLTreeMap<>();

	public VehicleResource(ReaderBase readerBase, @Nullable LegacyVehicleSupplier<ObjectArrayList<VehicleModel>> extraModelsSupplier, ResourceProvider resourceProvider) {
		super(readerBase, resourceProvider);
		updateData(readerBase);
		this.extraModelsSupplier = extraModelsSupplier;
		createVehicleSoundBase = createVehicleSoundBaseInitializer();
		shouldPreload = Config.getClient().matchesPreloadResourcePattern(id);
	}

	public VehicleResource(ReaderBase readerBase, ResourceProvider resourceProvider) {
		this(readerBase, null, resourceProvider);
	}

	VehicleResource(
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
			ObjectArrayList<VehicleModel> models,
			ObjectArrayList<VehicleModel> bogie1Models,
			ObjectArrayList<VehicleModel> bogie2Models,
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
			double legacyDoorCloseSoundTime,
			ResourceProvider resourceProvider
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
				legacyDoorCloseSoundTime,
				resourceProvider
		);
		this.tags.addAll(tags);
		this.models.addAll(models);
		this.bogie1Models.addAll(bogie1Models);
		this.bogie2Models.addAll(bogie2Models);
		this.extraModelsSupplier = null;
		createVehicleSoundBase = createVehicleSoundBaseInitializer();
		shouldPreload = Config.getClient().matchesPreloadResourcePattern(id);
	}

	@Nonnull
	@Override
	protected ResourceProvider modelsResourceProviderParameter() {
		return resourceProvider;
	}

	@Nonnull
	@Override
	protected ResourceProvider bogie1ModelsResourceProviderParameter() {
		return resourceProvider;
	}

	@Nonnull
	@Override
	protected ResourceProvider bogie2ModelsResourceProviderParameter() {
		return resourceProvider;
	}

	@Nullable
	public VehicleResourceCache getCachedVehicleResource(int carNumber, int totalCars, boolean force) {
		final int newCarNumber = extraModelsSupplier == null ? 0 : carNumber;
		final int newTotalCars = extraModelsSupplier == null ? 0 : totalCars;
		final CachedResource<CachedResource<VehicleResourceCache>> data1 = cachedVehicleResource.computeIfAbsent(newCarNumber, key -> new Int2ObjectAVLTreeMap<>()).computeIfAbsent(newTotalCars, key -> cachedVehicleResourceInitializer(newCarNumber, newTotalCars, force)).getData(force);
		if (data1 != null) {
			final CachedResource<VehicleResourceCache> data2 = data1.getData(force);
			if (data2 != null) {
				return data2.getData(force);
			}
		}
		return null;
	}

	public void queueBogie(int bogieIndex, StoredMatrixTransformations storedMatrixTransformations, VehicleExtension vehicle, int light) {
		final VehicleResourceCache vehicleResourceCache = getCachedVehicleResource(0, 1, false);
		if (vehicleResourceCache != null && Utilities.isBetween(bogieIndex, 0, 1)) {
			(bogieIndex == 0 ? vehicleResourceCache.builtBogie1Models() : vehicleResourceCache.builtBogie2Models()).forEach(builtVehicleModelHolder -> builtVehicleModelHolder.render(storedMatrixTransformations, vehicle, 0, new int[0], light, new ObjectArrayList<>(), false));
		}
	}

	public String getId() {
		return id;
	}

	public MutableText getName() {
		return Text.translatable(name);
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
		return Text.translatable(description);
	}

	public String getWikipediaArticle() {
		return wikipediaArticle;
	}

	public void iterateModels(int carNumber, int totalCars, ModelConsumer modelConsumer) {
		iterateModels(getAllModels(carNumber, totalCars), modelConsumer);
	}

	public void iterateBogieModels(int bogieIndex, ModelConsumer modelConsumer) {
		if (Utilities.isBetween(bogieIndex, 0, 1)) {
			iterateModels(bogieIndex == 0 ? bogie1Models : bogie2Models, modelConsumer);
		}
	}

	public VehicleResourceWrapper toVehicleResourceWrapper() {
		final int carNumber = id.endsWith("trailer") ? 1 : id.endsWith("cab_2") ? 2 : 0;
		final int totalCars = id.endsWith("cab_3") ? 1 : 3;
		getCachedVehicleResource(carNumber, totalCars, true);
		return new VehicleResourceWrapper(
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
				getAllModels(carNumber, totalCars).stream().map(VehicleModel::toVehicleModelWrapper).collect(Collectors.toCollection(ObjectArrayList::new)),
				bogie1Models.stream().map(VehicleModel::toVehicleModelWrapper).collect(Collectors.toCollection(ObjectArrayList::new)),
				bogie2Models.stream().map(VehicleModel::toVehicleModelWrapper).collect(Collectors.toCollection(ObjectArrayList::new)),
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
	}

	public void writeMinecraftResource(ObjectArraySet<MinecraftModelResource> minecraftModelResources, ObjectArraySet<String> minecraftTextureResources) {
		models.forEach(vehicleModel -> {
			minecraftModelResources.add(vehicleModel.getAsMinecraftResource());
			vehicleModel.addToTextureResource(minecraftTextureResources);
		});
		bogie1Models.forEach(vehicleModel -> {
			minecraftModelResources.add(vehicleModel.getAsMinecraftResource());
			vehicleModel.addToTextureResource(minecraftTextureResources);
		});
		bogie2Models.forEach(vehicleModel -> {
			minecraftModelResources.add(vehicleModel.getAsMinecraftResource());
			vehicleModel.addToTextureResource(minecraftTextureResources);
		});
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

	private ObjectArrayList<VehicleModel> getAllModels(int carNumber, int totalCars) {
		if (extraModelsSupplier == null) {
			return models;
		} else {
			return allModels.getOrDefault(carNumber, new Int2ObjectAVLTreeMap<>()).getOrDefault(totalCars, new ObjectArrayList<>());
		}
	}

	public void collectTags(Object2ObjectAVLTreeMap<String, Object2ObjectAVLTreeMap<String, ObjectArrayList<String>>> tagMap) {
		tags.forEach(tag -> {
			final String[] tagSplit = tag.split(":");
			if (tagSplit.length == 2) {
				tagMap.computeIfAbsent(tagSplit[0], key -> new Object2ObjectAVLTreeMap<>()).computeIfAbsent(tagSplit[1], key -> new ObjectArrayList<>()).add(id);
			}
		});
	}

	private CachedResource<CachedResource<CachedResource<VehicleResourceCache>>> cachedVehicleResourceInitializer(int carNumber, int totalCars, boolean force) {
		final int modelLifespan = shouldPreload ? Integer.MAX_VALUE : VehicleModel.MODEL_LIFESPAN;
		return new CachedResource<>(() -> {
			final ObjectArrayList<VehicleModel> allModelsList = allModels.computeIfAbsent(carNumber, key -> new Int2ObjectAVLTreeMap<>()).computeIfAbsent(totalCars, key -> new ObjectArrayList<>());
			allModelsList.clear();
			allModelsList.addAll(models);

			if (extraModelsSupplier != null) {
				final long startMillis = System.currentTimeMillis();
				allModelsList.addAll(extraModelsSupplier.apply(carNumber, totalCars));
				final long endMillis = System.currentTimeMillis();
				if (endMillis - startMillis >= 100) {
					MTR.LOGGER.warn("[{}] Model loading took {} ms, which is longer than usual!", id, endMillis - startMillis);
				}
			}

			return new CachedResource<>(() -> {
				final ObjectArrayList<BuiltVehicleModelHolder> builtModels = new ObjectArrayList<>();
				final ObjectArrayList<Box> floors = new ObjectArrayList<>();
				final ObjectArrayList<Box> doorways = new ObjectArrayList<>();
				allModelsList.forEach(vehicleModel -> {
					builtModels.add(vehicleModel.builtVehicleModelHolder);
					floors.addAll(vehicleModel.builtVehicleModelHolder.floors);
					doorways.addAll(vehicleModel.builtVehicleModelHolder.doorways);
				});

				// TODO don't rebuild shared models, e.g. bogies
				final ObjectArrayList<BuiltVehicleModelHolder> builtBogie1Models = new ObjectArrayList<>();
				bogie1Models.forEach(vehicleModel -> builtBogie1Models.add(vehicleModel.builtVehicleModelHolder));

				final ObjectArrayList<BuiltVehicleModelHolder> builtBogie2Models = new ObjectArrayList<>();
				bogie2Models.forEach(vehicleModel -> builtBogie2Models.add(vehicleModel.builtVehicleModelHolder));

				if (floors.isEmpty() && doorways.isEmpty()) {
					MTR.LOGGER.info("[{}] No floors or doorways found in vehicle models", id);
					final double x1 = width / 2 + 0.25;
					final double x2 = width / 2 + 0.5;
					final double y = 1 + legacyRiderOffset;
					final double z = length / 2 - 0.5;
					builtModels.getFirst().floors.add(new Box(-x1, y, -z, x1, y, z));
					for (double j = -z; j <= z + 0.001; j++) {
						builtModels.getFirst().doorways.add(new Box(-x1, y, j, -x2, y, j + 1));
						builtModels.getFirst().doorways.add(new Box(x1, y, j, x2, y, j + 1));
					}
				}

				return new CachedResource<>(() -> new VehicleResourceCache(builtModels, builtBogie1Models, builtBogie2Models, new ObjectImmutableList<>(floors), new ObjectImmutableList<>(doorways)), modelLifespan);
			}, modelLifespan);
		}, modelLifespan);
	}

	private Supplier<VehicleSoundBase> createVehicleSoundBaseInitializer() {
		if (bveSoundBaseResource.isEmpty()) {
			final LegacyVehicleSound legacyVehicleSound = new LegacyVehicleSound(
					legacySpeedSoundBaseResource,
					(int) legacySpeedSoundCount,
					legacyUseAccelerationSoundsWhenCoasting,
					legacyConstantPlaybackSpeed,
					legacyDoorSoundBaseResource,
					legacyDoorCloseSoundTime
			);
			return () -> legacyVehicleSound;
		} else {
			final BveVehicleSoundConfig bveVehicleSoundConfig = new BveVehicleSoundConfig(bveSoundBaseResource);
			return () -> new BveVehicleSound(bveVehicleSoundConfig);
		}
	}

	private static void iterateModels(ObjectArrayList<VehicleModel> models, ModelConsumer modelConsumer) {
		for (int i = 0; i < models.size(); i++) {
			final VehicleModel vehicleModel = models.get(i);
			if (vehicleModel != null) {
				modelConsumer.accept(i, vehicleModel.builtVehicleModelHolder);
			}
		}
	}

	@FunctionalInterface
	public interface ModelConsumer {
		void accept(int index, BuiltVehicleModelHolder builtVehicleModelHolder);
	}

	@FunctionalInterface
	public interface LegacyVehicleSupplier<T> {
		T apply(int carNumber, int totalCars);
	}
}
