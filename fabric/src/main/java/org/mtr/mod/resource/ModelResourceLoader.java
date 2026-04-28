package org.mtr.mod.resource;

import org.apache.commons.lang3.StringUtils;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectAVLTreeMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.mapper.OptimizedModel;

public final class ModelResourceLoader {

	public static boolean isObjOrMqo(String modelResource) {
		return modelResource.endsWith(".obj") || modelResource.endsWith(".mqo");
	}

	public static Object2ObjectAVLTreeMap<String, OptimizedModel.ObjModel> loadObjOrMqo(
			String modelResource,
			Identifier textureId,
			boolean flipTextureV,
			ResourceProvider resourceProvider
	) {
		if (modelResource.endsWith(".mqo")) {
			final MqoModelConverter.ConvertedModel convertedModel = MqoModelConverter.convert(resourceProvider.get(CustomResourceTools.formatIdentifierWithDefault(modelResource, "mqo")));
			return new Object2ObjectAVLTreeMap<>(OptimizedModel.ObjModel.loadModel(
					convertedModel.getObjContent(),
					mtlString -> convertedModel.getMtlContent(),
					textureString -> StringUtils.isEmpty(textureString) ? OptimizedModelWrapper.WHITE_TEXTURE : StringUtils.equals(textureString, "default.png") ? textureId : CustomResourceTools.getResourceFromSamePath(modelResource, textureString, "png"),
					null, true, flipTextureV
			));
		} else {
			return new Object2ObjectAVLTreeMap<>(OptimizedModel.ObjModel.loadModel(
					resourceProvider.get(CustomResourceTools.formatIdentifierWithDefault(modelResource, "obj")),
					mtlString -> resourceProvider.get(CustomResourceTools.getResourceFromSamePath(modelResource, mtlString, "mtl")),
					textureString -> StringUtils.isEmpty(textureString) ? OptimizedModelWrapper.WHITE_TEXTURE : StringUtils.equals(textureString, "default.png") ? textureId : CustomResourceTools.getResourceFromSamePath(modelResource, textureString, "png"),
					null, true, flipTextureV
			));
		}
	}

	public static ObjectArrayList<String> getModelParts(String name, String content) {
		if (name.endsWith(".mqo")) {
			return MqoModelConverter.convert(content).getModelParts();
		} else {
			return new ObjectArrayList<>(OptimizedModel.ObjModel.loadModel(content, mtlString -> "", textureString -> new Identifier(""), null, true, false).keySet());
		}
	}

	private ModelResourceLoader() {
	}
}
