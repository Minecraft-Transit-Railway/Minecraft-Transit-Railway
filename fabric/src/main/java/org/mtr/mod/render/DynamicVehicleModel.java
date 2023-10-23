package org.mtr.mod.render;

import org.mtr.core.data.BlockbenchElement;
import org.mtr.core.data.BlockbenchModel;
import org.mtr.core.data.BlockbenchOutline;
import org.mtr.core.data.ModelProperties;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.mtr.mapping.holder.EntityAbstractMapping;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.mapper.EntityModelExtension;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.ModelPartExtension;
import org.mtr.mod.ObjectHolder;
import org.mtr.mod.data.VehicleExtension;

import java.util.function.Consumer;

public final class DynamicVehicleModel extends EntityModelExtension<EntityAbstractMapping> {

	public final ModelProperties modelProperties;
	private final Identifier texture;
	private final Object2ObjectOpenHashMap<String, ModelPartExtension> nameToParentModelPart = new Object2ObjectOpenHashMap<>();

	public DynamicVehicleModel(BlockbenchModel blockbenchModel, Identifier texture, ModelProperties modelProperties) {
		super(blockbenchModel.getTextureWidth(), blockbenchModel.getTextureHeight());
		final Object2ObjectOpenHashMap<String, BlockbenchElement> uuidToBlockbenchElement = new Object2ObjectOpenHashMap<>();
		blockbenchModel.getElements().forEach(blockbenchElement -> uuidToBlockbenchElement.put(blockbenchElement.getUuid(), blockbenchElement));
		blockbenchModel.getOutlines().forEach(blockbenchOutline -> addChildren(blockbenchOutline, uuidToBlockbenchElement));
		buildModel();
		this.texture = texture;
		this.modelProperties = modelProperties;
	}

	@Override
	public void setAngles2(EntityAbstractMapping entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
	}

	@Override
	public void render(GraphicsHolder graphicsHolder, int light, int overlay, float red, float green, float blue, float alpha) {
	}

	public void render(StoredMatrixTransformations storedMatrixTransformations, VehicleExtension vehicle, int light) {
		modelProperties.render(texture, storedMatrixTransformations, vehicle, light, nameToParentModelPart);
	}

	private void addChildren(BlockbenchOutline blockbenchOutline, Object2ObjectOpenHashMap<String, BlockbenchElement> uuidToBlockbenchElement) {
		final ObjectHolder<ModelPartExtension> parentModelPart = new ObjectHolder<>(this::createModelPart);

		iterateChildren(blockbenchOutline, uuid -> {
			final BlockbenchElement blockbenchElement = uuidToBlockbenchElement.remove(uuid);
			if (blockbenchElement != null) {
				parentModelPart.create();
				final ModelPartExtension childModelPart = createModelPart();
				parentModelPart.createAndGet().addChild(childModelPart);
				blockbenchElement.setModelPart(childModelPart);
			}
		});

		if (parentModelPart.exists()) {
			nameToParentModelPart.put(blockbenchOutline.getName(), parentModelPart.createAndGet());
		}
	}

	private void iterateChildren(BlockbenchOutline blockbenchOutline, Consumer<String> consumer) {
		blockbenchOutline.childrenUuid.forEach(consumer);
		blockbenchOutline.getChildren().forEach(childOutline -> iterateChildren(childOutline, consumer));
	}
}
