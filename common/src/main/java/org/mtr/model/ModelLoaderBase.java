package org.mtr.model;

import net.minecraft.client.render.VertexFormat;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import org.jspecify.annotations.Nullable;
import org.mtr.libraries.it.unimi.dsi.fastutil.ints.IntIntImmutablePair;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import org.mtr.render.StoredMatrixTransformations;
import org.mtr.resource.*;

import java.util.Comparator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Shared infrastructure for the two concrete model loaders — {@link ObjModelLoader} and
 * {@link BlockbenchModelLoader}.
 *
 * <p>Each loader is a single-use vessel: subclasses parse the model bytes (off the main
 * thread when possible), populate {@link #nameToNewOptimizedModelGroup} and
 * {@link #nameToRawModelDisplayParts}, then call {@link #setModelLoaded()}. The first call
 * to one of the {@code get(...)} methods after that flag flips builds the corresponding
 * {@code builtModel} cache; subsequent calls return the cached value.</p>
 *
 * <p>There are <b>two</b> parallel build paths, intentionally so:</p>
 * <ol>
 *   <li>{@link #get(ModelProperties, PositionDefinitions)} — the <b>vehicle</b> path. It
 *       knows about floors, doorways, door mappings, conditional parts and display
 *       surfaces, and returns a {@link BuiltVehicleModelHolder}.</li>
 *   <li>{@link #get()} — the <b>non-vehicle</b> path used by {@code ObjectResource},
 *       {@code RailResource} and similar. It collapses every named group into a single
 *       {@code EXTERIOR}-stage blob.</li>
 * </ol>
 *
 * <p>Calling the wrong accessor for a loader's intended use returns valid-but-meaningless
 * data — see {@code docs/MIGRATIONS.md} §5 for the recommended consolidation.</p>
 *
 * <p><b>Threading:</b> {@link #addModel(String, NewOptimizedModelGroup)} and
 * {@link #addModelDisplayParts(String, ObjectArrayList)} may be called from any thread.
 * The {@code get(...)} methods must run on the render thread because they upload vertex
 * buffers via {@link NewOptimizedModelGroup#build(net.minecraft.client.render.VertexFormat.DrawMode)}.</p>
 */
public abstract class ModelLoaderBase {

	private boolean modelLoaded = false;
	@Nullable
	private BuiltVehicleModelHolder builtModel1;
	@Nullable
	private Object2ObjectOpenHashMap<RenderStage, ObjectArrayList<NewOptimizedModel>> builtModel2;

	protected final Identifier defaultTexture;
	private final VertexFormat.DrawMode drawMode;

	private final ConcurrentHashMap<String, NewOptimizedModelGroup> nameToNewOptimizedModelGroup = new ConcurrentHashMap<>();
	private final ConcurrentHashMap<String, ObjectArrayList<ObjectObjectImmutablePair<StoredMatrixTransformations, IntIntImmutablePair>>> nameToRawModelDisplayParts = new ConcurrentHashMap<>();

	protected ModelLoaderBase(Identifier defaultTexture, VertexFormat.DrawMode drawMode) {
		this.defaultTexture = defaultTexture;
		this.drawMode = drawMode;
	}

	/**
	 * Get the model, building it if necessary. This is generally used for vehicles.
	 *
	 * @return a {@link BuiltVehicleModelHolder} object, including door mappings, floors, and doorways
	 */
	@Nullable
	public final BuiltVehicleModelHolder get(ModelProperties modelProperties, PositionDefinitions positionDefinitions) {
		if (modelLoaded && builtModel1 == null) {
			final Object2ObjectOpenHashMap<PartCondition, NewOptimizedModelGroup> rawModels = new Object2ObjectOpenHashMap<>();
			final ObjectArrayList<ModelPropertiesPart.RawDoorModelDetails> rawDoorModelDetailsList = new ObjectArrayList<>();
			final Object2ObjectOpenHashMap<PartCondition, ObjectArrayList<ModelDisplayPart>> displays = new Object2ObjectOpenHashMap<>();
			final ObjectArrayList<Box> floors = new ObjectArrayList<>();
			final ObjectArrayList<Box> doorways = new ObjectArrayList<>();
			modelProperties.iterateParts(modelPropertiesPart -> modelPropertiesPart.writeCache(nameToNewOptimizedModelGroup, nameToRawModelDisplayParts, positionDefinitions, rawModels, rawDoorModelDetailsList, displays, floors, doorways, modelProperties.getModelYOffset()));
			builtModel1 = new BuiltVehicleModelHolder(modelProperties, get(rawModels), mapDoors(rawDoorModelDetailsList, doorways), displays, floors, doorways);
		}
		return builtModel1;
	}

	/**
	 * Get the model, building it if necessary. This is generally used for rails and other objects.
	 *
	 * @return a representation of {@link NewOptimizedModel} objects
	 */
	@Nullable
	public final Object2ObjectOpenHashMap<RenderStage, ObjectArrayList<NewOptimizedModel>> get() {
		if (modelLoaded && builtModel2 == null) {
			final NewOptimizedModelGroup newOptimizedModelGroupCombined = new NewOptimizedModelGroup();
			nameToNewOptimizedModelGroup.values().forEach(newOptimizedModelGroup -> newOptimizedModelGroupCombined.merge(newOptimizedModelGroup, RenderStage.EXTERIOR, 0, 0, 0, false));
			builtModel2 = newOptimizedModelGroupCombined.build(drawMode);
		}
		return builtModel2;
	}

	/**
	 * Reset the model build status, for example, during resource reload.
	 */
	public final void reset() {
		modelLoaded = false;
		builtModel1 = null;
		builtModel2 = null;
	}

	public final ObjectArrayList<String> getNames() {
		return new ObjectArrayList<>(nameToNewOptimizedModelGroup.keySet());
	}

	protected final void addModel(String name, NewOptimizedModelGroup newOptimizedModelGroup) {
		nameToNewOptimizedModelGroup.put(name, newOptimizedModelGroup);
	}

	protected final void addModelDisplayParts(String name, ObjectArrayList<ObjectObjectImmutablePair<StoredMatrixTransformations, IntIntImmutablePair>> modelDisplayParts) {
		nameToRawModelDisplayParts.put(name, modelDisplayParts);
	}

	protected final void setModelLoaded() {
		modelLoaded = true;
	}

	protected final boolean canLoadModel() {
		return !modelLoaded;
	}

	private Object2ObjectOpenHashMap<PartCondition, Object2ObjectOpenHashMap<RenderStage, ObjectArrayList<NewOptimizedModel>>> get(Object2ObjectOpenHashMap<PartCondition, NewOptimizedModelGroup> rawModels) {
		final Object2ObjectOpenHashMap<PartCondition, Object2ObjectOpenHashMap<RenderStage, ObjectArrayList<NewOptimizedModel>>> models = new Object2ObjectOpenHashMap<>();
		rawModels.forEach((partCondition, newOptimizedModelGroup) -> models.put(partCondition, newOptimizedModelGroup.build(drawMode)));
		return models;
	}

	/**
	 * If this part is a door, find the closest doorway.
	 */
	private ObjectArrayList<BuiltVehicleModelHolder.BuiltDoorModelDetails> mapDoors(
		ObjectArrayList<ModelPropertiesPart.RawDoorModelDetails> rawDoorModelDetailsList,
		ObjectArrayList<Box> doorways
	) {
		final ObjectArrayList<BuiltVehicleModelHolder.BuiltDoorModelDetails> builtDoorModelDetailsList = new ObjectArrayList<>();
		rawDoorModelDetailsList.forEach(rawDoorModelDetails -> {
			final Box closestDoorway = doorways.stream().min(Comparator.comparingDouble(checkDoorway -> rawDoorModelDetails.boxes().stream().map(box -> getClosestDistance(
				box.minX,
				box.maxX,
				checkDoorway.minX,
				checkDoorway.maxX
			) + getClosestDistance(
				box.minY,
				box.maxY,
				checkDoorway.minY,
				checkDoorway.maxY
			) + getClosestDistance(
				box.minZ,
				box.maxZ,
				checkDoorway.minZ,
				checkDoorway.maxZ
			)).min(Double::compareTo).orElse(Double.MAX_VALUE))).orElse(null);

			final Object2ObjectOpenHashMap<PartCondition, Object2ObjectOpenHashMap<RenderStage, ObjectArrayList<NewOptimizedModel>>> builtDoorModel = new Object2ObjectOpenHashMap<>();
			rawDoorModelDetails.rawModels().forEach((partCondition, newOptimizedModelGroup) -> builtDoorModel.put(partCondition, newOptimizedModelGroup.build(drawMode)));
			builtDoorModelDetailsList.add(new BuiltVehicleModelHolder.BuiltDoorModelDetails(builtDoorModel, rawDoorModelDetails.modelPropertiesPart(), closestDoorway, rawDoorModelDetails.flipped()));
		});
		return builtDoorModelDetailsList;
	}

	private static double getClosestDistance(double a1, double a2, double b1, double b2) {
		return Math.min(Math.min(Math.abs(b1 - a1), Math.abs(b1 - a2)), Math.min(Math.abs(b2 - a1), Math.abs(b2 - a2)));
	}
}
