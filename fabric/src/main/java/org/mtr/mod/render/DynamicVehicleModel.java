package org.mtr.mod.render;

import org.mtr.core.data.*;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArraySet;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import org.mtr.mapping.holder.Box;
import org.mtr.mapping.holder.EntityAbstractMapping;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.mapper.EntityModelExtension;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.ModelPartExtension;
import org.mtr.mod.MutableBox;
import org.mtr.mod.ObjectHolder;
import org.mtr.mod.data.VehicleExtension;

import java.util.function.Consumer;
import java.util.function.Predicate;

public final class DynamicVehicleModel extends EntityModelExtension<EntityAbstractMapping> {

	public final ModelProperties modelProperties;
	public final ObjectImmutableList<Box> floors;
	public final ObjectImmutableList<Box> doorways;
	private final Identifier texture;

	public DynamicVehicleModel(BlockbenchModel blockbenchModel, Identifier texture, ModelProperties modelProperties, PositionDefinitions positionDefinitions) {
		super(blockbenchModel.getTextureWidth(), blockbenchModel.getTextureHeight());

		final Object2ObjectOpenHashMap<String, BlockbenchElement> uuidToBlockbenchElement = new Object2ObjectOpenHashMap<>();
		blockbenchModel.getElements().forEach(blockbenchElement -> uuidToBlockbenchElement.put(blockbenchElement.getUuid(), blockbenchElement));

		final Object2ObjectOpenHashMap<String, ObjectObjectImmutablePair<ModelPartExtension, Box>> nameToPart = new Object2ObjectOpenHashMap<>();
		blockbenchModel.getOutlines().forEach(blockbenchOutline -> {
			final ObjectHolder<ModelPartExtension> parentModelPart = new ObjectHolder<>(this::createModelPart);
			final MutableBox mutableBox = new MutableBox();

			iterateChildren(blockbenchOutline, uuid -> {
				final BlockbenchElement blockbenchElement = uuidToBlockbenchElement.remove(uuid);
				if (blockbenchElement != null) {
					parentModelPart.create();
					final ModelPartExtension childModelPart = createModelPart();
					parentModelPart.createAndGet().addChild(childModelPart);
					mutableBox.add(blockbenchElement.setModelPart(childModelPart));
				}
			});

			if (parentModelPart.exists()) {
				nameToPart.put(blockbenchOutline.getName(), new ObjectObjectImmutablePair<>(parentModelPart.createAndGet(), mutableBox.get()));
			}
		});

		buildModel();
		this.texture = texture;
		this.modelProperties = modelProperties;

		final ObjectArraySet<Box> floors = new ObjectArraySet<>();
		final ObjectArraySet<Box> doorways = new ObjectArraySet<>();
		modelProperties.iterateParts(modelPropertiesPart -> modelPropertiesPart.writeCache(nameToPart, positionDefinitions, floors, doorways));
		modelProperties.iterateParts(modelPropertiesPart -> modelPropertiesPart.mapDoors(doorways));
		this.floors = new ObjectImmutableList<>(floors);
		this.doorways = new ObjectImmutableList<>(doorways);
	}

	@Override
	public void setAngles2(EntityAbstractMapping entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
	}

	@Override
	public void render(GraphicsHolder graphicsHolder, int light, int overlay, float red, float green, float blue, float alpha) {
	}

	public void render(StoredMatrixTransformations storedMatrixTransformations, VehicleExtension vehicle, int light, Predicate<Box> checkDoors) {
		modelProperties.iterateParts(modelPropertiesPart -> modelPropertiesPart.render(texture, storedMatrixTransformations, vehicle, light, checkDoors));
	}

	private static void iterateChildren(BlockbenchOutline blockbenchOutline, Consumer<String> consumer) {
		blockbenchOutline.childrenUuid.forEach(consumer);
		blockbenchOutline.getChildren().forEach(childOutline -> iterateChildren(childOutline, consumer));
	}
}
