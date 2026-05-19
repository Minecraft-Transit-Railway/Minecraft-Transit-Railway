package org.mtr.mod.resource;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectAVLTreeMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.mapper.OptimizedModel;
import org.mtr.mapping.render.model.RawMesh;
import org.mtr.mapping.render.obj.AtlasManager;
import org.mtr.mapping.render.obj.ObjModelLoader;
import org.mtr.mod.Init;
import org.mtr.mod.client.CustomResourceLoader;

import java.util.List;
import java.util.Map;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.lang.reflect.Field;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public final class ModelResourceLoader {

	public static boolean isSupportedModelResource(String modelResource) {
		return modelResource.endsWith(".obj") || modelResource.endsWith(".mqo") || modelResource.endsWith(".mqoz");
	}

	public static Object2ObjectAVLTreeMap<String, OptimizedModel.ObjModel> loadModel(
			String modelResource,
			Identifier textureId,
			boolean flipTextureV,
			ResourceProvider resourceProvider
	) {
		if (modelResource.endsWith(".mqo") || modelResource.endsWith(".mqoz")) {
			final MqoModelConverter.ConvertedModel convertedModel = MqoModelConverter.convert(getMqoContent(modelResource, resourceProvider));
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
		} else if (name.endsWith(".mqoz")) {
			return getModelParts(name, content.getBytes(StandardCharsets.UTF_8));
		} else {
			return new ObjectArrayList<>(OptimizedModel.ObjModel.loadModel(content, mtlString -> "", textureString -> new Identifier(""), null, true, false).keySet());
		}
	}

	public static ObjectArrayList<String> getModelParts(String name, byte[] bytes) {
		if (name.endsWith(".mqoz")) {
			return MqoModelConverter.convert(extractMqoContentFromMqoz(name, bytes)).getModelParts();
		} else {
			return getModelParts(name, new String(bytes, StandardCharsets.UTF_8));
		}
	}

	public static Map<String, List<RawMesh>> loadRawModel(
			String modelResource,
			Identifier textureId,
			boolean flipTextureV,
			ResourceProvider resourceProvider
	) {
		CustomResourceLoader.OPTIMIZED_RENDERER_WRAPPER.beginReload();
		try {
			final AtlasManager atlasManager = getObjAtlasManager();
			if (atlasManager == null) {
				throw new IllegalStateException("AtlasManager unavailable for GPU OBJ loading");
			}
			if (modelResource.endsWith(".mqo") || modelResource.endsWith(".mqoz")) {
				final MqoModelConverter.ConvertedModel convertedModel = MqoModelConverter.convert(getMqoContent(modelResource, resourceProvider));
				return ObjModelLoader.loadModel(
						convertedModel.getObjContent(),
						mtlString -> convertedModel.getMtlContent(),
						textureString -> StringUtils.isEmpty(textureString) ? OptimizedModelWrapper.WHITE_TEXTURE : StringUtils.equals(textureString, "default.png") ? textureId : CustomResourceTools.getResourceFromSamePath(modelResource, textureString, "png"),
						atlasManager,
						flipTextureV
				);
			} else {
				return ObjModelLoader.loadModel(
						resourceProvider.get(CustomResourceTools.formatIdentifierWithDefault(modelResource, "obj")),
						mtlString -> resourceProvider.get(CustomResourceTools.getResourceFromSamePath(modelResource, mtlString, "mtl")),
						textureString -> StringUtils.isEmpty(textureString) ? OptimizedModelWrapper.WHITE_TEXTURE : StringUtils.equals(textureString, "default.png") ? textureId : CustomResourceTools.getResourceFromSamePath(modelResource, textureString, "png"),
						atlasManager,
						flipTextureV
				);
			}
		} finally {
			CustomResourceLoader.OPTIMIZED_RENDERER_WRAPPER.finishReload();
		}
	}

	private static AtlasManager getObjAtlasManager() {
		try {
			final Field field = OptimizedModel.class.getDeclaredField("ATLAS_MANAGER");
			field.setAccessible(true);
			final Object object = field.get(null);
			if (object instanceof AtlasManager) {
				return (AtlasManager) object;
			}
		} catch (Exception e) {
			Init.LOGGER.warn("Failed to access OptimizedModel.ATLAS_MANAGER for GPU OBJ loading", e);
		}
		return null;
	}

	public static String extractMqoContentFromMqoz(String modelResource, byte[] bytes) {
		final ObjectArrayList<MqozEntry> mqoEntries = new ObjectArrayList<>();
		try (final ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(bytes))) {
			ZipEntry zipEntry;
			while ((zipEntry = zipInputStream.getNextEntry()) != null) {
				if (!zipEntry.isDirectory() && zipEntry.getName().toLowerCase().endsWith(".mqo")) {
					mqoEntries.add(new MqozEntry(zipEntry.getName(), new String(IOUtils.toByteArray(zipInputStream), StandardCharsets.UTF_8)));
				}
				zipInputStream.closeEntry();
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("Invalid MQOZ archive", e);
		}

		if (mqoEntries.isEmpty()) {
			throw new IllegalArgumentException("MQOZ archive does not contain an MQO file");
		}

		final String modelBaseName = getFileBaseName(modelResource);
		mqoEntries.sort(Comparator.comparing(entry -> entry.name));
		for (final MqozEntry entry : mqoEntries) {
			if (StringUtils.equals(getFileBaseName(entry.name), modelBaseName)) {
				return entry.content;
			}
		}
		return mqoEntries.get(0).content;
	}

	private static String getMqoContent(String modelResource, ResourceProvider resourceProvider) {
		if (modelResource.endsWith(".mqoz")) {
			return extractMqoContentFromMqoz(modelResource, resourceProvider.getBytes(CustomResourceTools.formatIdentifierWithDefault(modelResource, "mqoz")));
		} else {
			return resourceProvider.get(CustomResourceTools.formatIdentifierWithDefault(modelResource, "mqo"));
		}
	}

	private static String getFileBaseName(String path) {
		String normalizedPath = path.replace('\\', '/');
		final int namespaceIndex = normalizedPath.indexOf(':');
		if (namespaceIndex >= 0) {
			normalizedPath = normalizedPath.substring(namespaceIndex + 1);
		}
		final String[] pathSplit = normalizedPath.split("/");
		final String fileName = pathSplit[pathSplit.length - 1];
		final int extensionIndex = fileName.lastIndexOf('.');
		return extensionIndex < 0 ? fileName : fileName.substring(0, extensionIndex);
	}

	private ModelResourceLoader() {
	}

	private static final class MqozEntry {

		private final String name;
		private final String content;

		private MqozEntry(String name, String content) {
			this.name = name;
			this.content = content;
		}
	}
}
