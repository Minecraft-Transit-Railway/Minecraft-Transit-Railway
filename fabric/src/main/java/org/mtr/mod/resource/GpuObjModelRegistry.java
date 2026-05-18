package org.mtr.mod.resource;

import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectSet;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.render.model.RawMesh;
import org.mtr.mod.Init;
import org.mtr.mod.render.StaticObjMesh;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public final class GpuObjModelRegistry {

	private static final Object2ObjectOpenHashMap<Key, GpuObjModelWrapper> CACHE = new Object2ObjectOpenHashMap<>();
	private static final ObjectSet<Key> FAILED_KEYS = new ObjectOpenHashSet<>();

	@Nullable
	public static GpuObjModelWrapper getOrCreate(String modelResource, Identifier textureId, boolean flipTextureV, ResourceProvider resourceProvider) {
		if (!modelResource.endsWith(".obj")) {
			return null;
		}

		final Key key = new Key(modelResource, textureId.data.toString(), flipTextureV);
		if (FAILED_KEYS.contains(key)) {
			return null;
		}

		final GpuObjModelWrapper cachedModel = CACHE.get(key);
		if (cachedModel != null) {
			return cachedModel;
		}

		try {
			final Map<String, List<RawMesh>> rawModel = ModelResourceLoader.loadRawModel(modelResource, textureId, flipTextureV, resourceProvider);
			final boolean hasTranslucentMeshes = rawModel.values().stream().flatMap(List::stream).anyMatch(rawMesh -> rawMesh.materialProperties.translucent);
			final GpuObjModelWrapper gpuObjModelWrapper = new GpuObjModelWrapper(rawModel.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().stream().filter(rawMesh -> !rawMesh.materialProperties.translucent).map(StaticObjMesh::new).collect(Collectors.toList()))), hasTranslucentMeshes);
			CACHE.put(key, gpuObjModelWrapper);
			return gpuObjModelWrapper;
		} catch (Exception e) {
			FAILED_KEYS.add(key);
			Init.LOGGER.error("[{}] Failed to build GPU OBJ cache, falling back to standard rendering", modelResource, e);
			return null;
		}
	}

	public static void clear() {
		CACHE.values().forEach(GpuObjModelWrapper::close);
		CACHE.clear();
		FAILED_KEYS.clear();
	}

	private GpuObjModelRegistry() {
	}

	private static final class Key {

		private final String modelResource;
		private final String texture;
		private final boolean flipTextureV;

		private Key(String modelResource, String texture, boolean flipTextureV) {
			this.modelResource = modelResource;
			this.texture = texture;
			this.flipTextureV = flipTextureV;
		}

		@Override
		public boolean equals(Object object) {
			if (this == object) {
				return true;
			}
			if (!(object instanceof Key)) {
				return false;
			}
			final Key key = (Key) object;
			return flipTextureV == key.flipTextureV && Objects.equals(modelResource, key.modelResource) && Objects.equals(texture, key.texture);
		}

		@Override
		public int hashCode() {
			return Objects.hash(modelResource, texture, flipTextureV);
		}
	}
}
