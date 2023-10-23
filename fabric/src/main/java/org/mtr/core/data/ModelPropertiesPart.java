package org.mtr.core.data;

import org.mtr.core.generated.ModelPropertiesPartSchema;
import org.mtr.core.serializers.ReaderBase;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectIntImmutablePair;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.holder.OverlayTexture;
import org.mtr.mapping.mapper.ModelPartExtension;
import org.mtr.mod.data.IGui;
import org.mtr.mod.data.VehicleExtension;
import org.mtr.mod.render.RenderTrains;
import org.mtr.mod.render.StoredMatrixTransformations;

public final class ModelPropertiesPart extends ModelPropertiesPartSchema implements IGui {

	public ModelPropertiesPart(ReaderBase readerBase) {
		super(readerBase);
		updateData(readerBase);
	}

	void render(Identifier texture, StoredMatrixTransformations storedMatrixTransformations, VehicleExtension vehicle, int light, Object2ObjectOpenHashMap<String, ModelPartExtension> nameToModelPart) {
		names.forEach(name -> {
			final ModelPartExtension modelPart = nameToModelPart.get(name);
			if (modelPart != null) {
				switch (type) {
					case NORMAL:
						renderNormal(texture, storedMatrixTransformations, vehicle, getRenderProperties(renderStage, light, vehicle), modelPart);
						break;
					case DISPLAY:
						break;
				}
			}
		});
	}

	private void renderNormal(Identifier texture, StoredMatrixTransformations storedMatrixTransformations, VehicleExtension vehicle, ObjectIntImmutablePair<RenderTrains.QueuedRenderLayer> renderProperties, ModelPartExtension modelPart) {
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
}
