package org.mtr.mod.resource;

import org.mtr.core.serializer.ReaderBase;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mod.Init;
import org.mtr.mod.client.CustomResourceLoader;
import org.mtr.mod.generated.resource.VehicleModelWrapperSchema;

import javax.annotation.Nullable;

public final class VehicleModelWrapper extends VehicleModelWrapperSchema {

	VehicleModelWrapper(
			String modelResource,
			String textureResource,
			String minecraftModelPropertiesResource,
			String minecraftPositionDefinitionsResource,
			boolean flipTextureV,
			ObjectArrayList<ModelPropertiesPartWrapper> parts,
			double modelYOffset,
			String gangwayInnerSideResource,
			String gangwayInnerTopResource,
			String gangwayInnerBottomResource,
			String gangwayOuterSideResource,
			String gangwayOuterTopResource,
			String gangwayOuterBottomResource,
			double gangwayWidth,
			double gangwayHeight,
			double gangwayYOffset,
			double gangwayZOffset,
			String barrierInnerSideResource,
			String barrierInnerTopResource,
			String barrierInnerBottomResource,
			String barrierOuterSideResource,
			String barrierOuterTopResource,
			String barrierOuterBottomResource,
			double barrierWidth,
			double barrierHeight,
			double barrierYOffset,
			double barrierZOffset
	) {
		super(
				modelResource,
				textureResource,
				minecraftModelPropertiesResource,
				minecraftPositionDefinitionsResource,
				flipTextureV,
				modelYOffset,
				gangwayInnerSideResource,
				gangwayInnerTopResource,
				gangwayInnerBottomResource,
				gangwayOuterSideResource,
				gangwayOuterTopResource,
				gangwayOuterBottomResource,
				gangwayWidth,
				gangwayHeight,
				gangwayYOffset,
				gangwayZOffset,
				barrierInnerSideResource,
				barrierInnerTopResource,
				barrierInnerBottomResource,
				barrierOuterSideResource,
				barrierOuterTopResource,
				barrierOuterBottomResource,
				barrierWidth,
				barrierHeight,
				barrierYOffset,
				barrierZOffset
		);
		this.parts.addAll(parts);
	}

	public VehicleModelWrapper(ReaderBase readerBase) {
		super(readerBase);
		updateData(readerBase);
	}

	VehicleModel toVehicleModel(
			ResourceProvider resourceProvider,
			@Nullable Object2ObjectArrayMap<String, ModelProperties> modelPropertiesMap,
			@Nullable Object2ObjectArrayMap<String, PositionDefinitions> positionDefinitionsMap
	) {
		final ObjectArrayList<ModelPropertiesPart> modelPropertiesPartList = new ObjectArrayList<>();
		final ObjectArrayList<PositionDefinition> positionDefinitionList = new ObjectArrayList<>();
		parts.forEach(part -> {
			final ObjectObjectImmutablePair<ModelPropertiesPart, PositionDefinition> modelPropertiesPartAndPositionDefinition = part.toModelPropertiesPartAndPositionDefinition();
			modelPropertiesPartList.add(modelPropertiesPartAndPositionDefinition.left());
			positionDefinitionList.add(modelPropertiesPartAndPositionDefinition.right());
		});

		final boolean isMinecraftResource = CustomResourceLoader.getMinecraftModelResources().stream().anyMatch(minecraftModelResource -> minecraftModelResource.matchesModelResource(modelResource));
		final String modelPropertiesResource = isMinecraftResource ? minecraftModelPropertiesResource : new Identifier(Init.MOD_ID, String.format("properties_%s.json", Init.randomString())).data.toString();
		final ModelProperties modelProperties = new ModelProperties(
				modelPropertiesPartList,
				modelYOffset,
				gangwayInnerSideResource,
				gangwayInnerTopResource,
				gangwayInnerBottomResource,
				gangwayOuterSideResource,
				gangwayOuterTopResource,
				gangwayOuterBottomResource,
				gangwayWidth,
				gangwayHeight,
				gangwayYOffset,
				gangwayZOffset,
				barrierInnerSideResource,
				barrierInnerTopResource,
				barrierInnerBottomResource,
				barrierOuterSideResource,
				barrierOuterTopResource,
				barrierOuterBottomResource,
				barrierWidth,
				barrierHeight,
				barrierYOffset,
				barrierZOffset
		);
		final String positionDefinitionsResource = isMinecraftResource ? minecraftPositionDefinitionsResource : new Identifier(Init.MOD_ID, String.format("definition_%s.json", Init.randomString())).data.toString();
		final PositionDefinitions positionDefinitions = new PositionDefinitions(positionDefinitionList);

		if (!isMinecraftResource && modelPropertiesMap != null && positionDefinitionsMap != null) {
			modelPropertiesMap.put(modelPropertiesResource, modelProperties);
			positionDefinitionsMap.put(positionDefinitionsResource, positionDefinitions);
		}

		return new VehicleModel(
				modelResource,
				textureResource,
				modelPropertiesResource,
				positionDefinitionsResource,
				flipTextureV,
				identifier -> {
					final String identifierString = identifier.data.toString();
					if (!isMinecraftResource) {
						if (identifierString.equals(modelPropertiesResource)) {
							return Utilities.getJsonObjectFromData(modelProperties).toString();
						} else if (identifierString.equals(positionDefinitionsResource)) {
							return Utilities.getJsonObjectFromData(positionDefinitions).toString();
						} else {
							return resourceProvider.get(identifier);
						}
					} else {
						return resourceProvider.get(identifier);
					}
				}
		);
	}

	void clean() {
		parts.removeIf(modelPropertiesPartWrapper -> modelPropertiesPartWrapper.getName().isEmpty());
	}
}
