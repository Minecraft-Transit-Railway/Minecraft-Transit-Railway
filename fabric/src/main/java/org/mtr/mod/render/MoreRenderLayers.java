package org.mtr.mod.render;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.holder.RenderLayer;

import java.util.function.Supplier;

public class MoreRenderLayers {

	private static final Object2ObjectOpenHashMap<String, RenderLayer> LIGHT_CACHE = new Object2ObjectOpenHashMap<>();
	private static final Object2ObjectOpenHashMap<Identifier, RenderLayer> INTERIOR_CACHE = new Object2ObjectOpenHashMap<>();
	private static final Object2ObjectOpenHashMap<Identifier, RenderLayer> INTERIOR_TRANSLUCENT_CACHE = new Object2ObjectOpenHashMap<>();
	private static final Object2ObjectOpenHashMap<Identifier, RenderLayer> EXTERIOR_CACHE = new Object2ObjectOpenHashMap<>();
	private static final Object2ObjectOpenHashMap<Identifier, RenderLayer> EXTERIOR_TRANSLUCENT_CACHE = new Object2ObjectOpenHashMap<>();

	public static RenderLayer getLight(Identifier texture, boolean isTranslucent) {
		return checkCache(texture.toString() + isTranslucent, () -> RenderLayer.getBeaconBeam(texture, isTranslucent), LIGHT_CACHE);
	}

	public static RenderLayer getInterior(Identifier texture) {
		return checkCache(texture, () -> RenderLayer.getEntityCutout(texture), INTERIOR_CACHE);
	}

	public static RenderLayer getInteriorTranslucent(Identifier texture) {
		return checkCache(texture, () -> RenderLayer.getEntityTranslucentCull(texture), INTERIOR_TRANSLUCENT_CACHE);
	}

	public static RenderLayer getExterior(Identifier texture) {
		return checkCache(texture, () -> RenderLayer.getEntityCutout(texture), EXTERIOR_CACHE);
	}

	public static RenderLayer getExteriorTranslucent(Identifier texture) {
		return checkCache(texture, () -> RenderLayer.getEntityTranslucentCull(texture), EXTERIOR_TRANSLUCENT_CACHE);
	}

	private static <T> RenderLayer checkCache(T identifier, Supplier<RenderLayer> supplier, Object2ObjectOpenHashMap<T, RenderLayer> cache) {
		if (cache.containsKey(identifier)) {
			return cache.get(identifier);
		} else {
			final RenderLayer renderLayer = supplier.get();
			cache.put(identifier, renderLayer);
			return renderLayer;
		}
	}
}
