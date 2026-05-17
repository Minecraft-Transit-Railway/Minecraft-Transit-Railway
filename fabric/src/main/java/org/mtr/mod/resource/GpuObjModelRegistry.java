package org.mtr.mod.resource;

import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.render.model.RawMesh;
import org.mtr.mod.render.StaticObjMesh;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public final class GpuObjModelRegistry {

	private static final Object2ObjectOpenHashMap<Key, GpuObjModelWrapper> CACHE = new Object2ObjectOpenHashMap<>();

	@Nullable
	public static GpuObjModelWrapper getOrCreate(String modelResource, Identifier textureId, boolean flipTextureV, ResourceProvider resourceProvider) {
		if (!modelResource.endsWith(".obj")) {
			return null;
		}
		return CACHE.computeIfAbsent(new Key(modelResource, textureId.data.toString(), flipTextureV), key -> {
			final Map<String, List<RawMesh>> rawModel = ModelResourceLoader.loadRawModel(modelResource, textureId, flipTextureV, resourceProvider);
			return new GpuObjModelWrapper(rawModel.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().stream().filter(rawMesh -> !rawMesh.materialProperties.translucent).map(StaticObjMesh::new).collect(Collectors.toList()))));
		});
	}

	public static void clear() {
		CACHE.values().forEach(GpuObjModelWrapper::close);
		CACHE.clear();
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
