package org.mtr.core.data;

import org.mtr.core.generated.ModelPropertiesPartSchema;
import org.mtr.core.serializers.ReaderBase;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArraySet;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectIntImmutablePair;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.holder.OverlayTexture;
import org.mtr.mapping.mapper.ModelPartExtension;
import org.mtr.mod.data.IGui;
import org.mtr.mod.data.VehicleExtension;
import org.mtr.mod.render.RenderTrains;
import org.mtr.mod.render.StoredMatrixTransformations;

public final class ModelPropertiesPart extends ModelPropertiesPartSchema implements IGui {

	private final ObjectArrayList<RenderProperties> renderPropertiesList = new ObjectArrayList<>();

	public ModelPropertiesPart(ReaderBase readerBase) {
		super(readerBase);
		updateData(readerBase);
	}

	public void writeCache(Object2ObjectOpenHashMap<String, ModelPartExtension> nameToParentModelPart, PositionDefinitions positionDefinitionsObject) {
		final ObjectArraySet<ModelPartExtension> modelParts = new ObjectArraySet<>();
		names.forEach(name -> {
			final ModelPartExtension modelPart = nameToParentModelPart.get(name);
			if (modelPart != null) {
				modelParts.add(modelPart);
			}
		});
		positionDefinitions.forEach(positionDefinitionName -> positionDefinitionsObject.getPositionDefinition(positionDefinitionName, (positions, positionsFlipped) -> modelParts.forEach(modelPart -> renderPropertiesList.add(new RenderProperties(modelPart, positions, positionsFlipped)))));
	}

	public void render(Identifier texture, StoredMatrixTransformations storedMatrixTransformations, VehicleExtension vehicle, int light) {
		renderPropertiesList.forEach(renderProperties -> {
			switch (type) {
				case NORMAL:
					renderNormal(texture, storedMatrixTransformations, vehicle, getRenderProperties(renderStage, light, vehicle), renderProperties.modelPart, renderProperties.positions, renderProperties.positionsFlipped);
					break;
				case DISPLAY:
					break;
			}
		});
	}

	private void renderNormal(Identifier texture, StoredMatrixTransformations storedMatrixTransformations, VehicleExtension vehicle, ObjectIntImmutablePair<RenderTrains.QueuedRenderLayer> renderProperties, ModelPartExtension modelPart, ObjectArrayList<PartPosition> positions, ObjectArrayList<PartPosition> positionsFlipped) {
		RenderTrains.scheduleRender(texture, false, renderProperties.left(), (graphicsHolder, offset) -> {
			storedMatrixTransformations.transform(graphicsHolder, offset);
			positions.forEach(position -> modelPart.render(
					graphicsHolder,
					(float) position.getX(),
					(float) position.getY(),
					(float) position.getZ(),
					0,
					renderProperties.rightInt(),
					OverlayTexture.getDefaultUvMapped()
			));
			positionsFlipped.forEach(position -> modelPart.render(
					graphicsHolder,
					(float) position.getX(),
					(float) position.getY(),
					(float) position.getZ(),
					(float) Math.PI,
					renderProperties.rightInt(),
					OverlayTexture.getDefaultUvMapped()
			));
			graphicsHolder.pop();
		});
	}

	private static ObjectIntImmutablePair<RenderTrains.QueuedRenderLayer> getRenderProperties(RenderStage renderStage, int light, VehicleExtension vehicle) {
		if (renderStage == RenderStage.ALWAYS_ON_LIGHT) {
			return new ObjectIntImmutablePair<>(RenderTrains.QueuedRenderLayer.LIGHT_TRANSLUCENT, MAX_LIGHT_GLOWING);
		} else {
			if (vehicle.getIsOnRoute()) {
				switch (renderStage) {
					case LIGHT:
						return new ObjectIntImmutablePair<>(RenderTrains.QueuedRenderLayer.LIGHT, MAX_LIGHT_GLOWING);
					case INTERIOR:
						return new ObjectIntImmutablePair<>(RenderTrains.QueuedRenderLayer.INTERIOR, MAX_LIGHT_INTERIOR);
					case INTERIOR_TRANSLUCENT:
						return new ObjectIntImmutablePair<>(RenderTrains.QueuedRenderLayer.INTERIOR_TRANSLUCENT, MAX_LIGHT_INTERIOR);
				}
			} else {
				if (renderStage == RenderStage.INTERIOR_TRANSLUCENT) {
					return new ObjectIntImmutablePair<>(RenderTrains.QueuedRenderLayer.EXTERIOR_TRANSLUCENT, light);
				}
			}
		}

		return new ObjectIntImmutablePair<>(RenderTrains.QueuedRenderLayer.EXTERIOR, light);
	}

	private static class RenderProperties {

		private final ModelPartExtension modelPart;
		private final ObjectArrayList<PartPosition> positions;
		private final ObjectArrayList<PartPosition> positionsFlipped;

		private RenderProperties(ModelPartExtension modelPart, ObjectArrayList<PartPosition> positions, ObjectArrayList<PartPosition> positionsFlipped) {
			this.modelPart = modelPart;
			this.positions = positions;
			this.positionsFlipped = positionsFlipped;
		}
	}
}
