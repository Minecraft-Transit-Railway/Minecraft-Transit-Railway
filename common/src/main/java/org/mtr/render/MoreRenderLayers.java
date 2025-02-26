package org.mtr.mod.render;

import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.holder.RenderLayer;

import java.util.function.Supplier;

public class MoreRenderLayers {

	private static final Object2ObjectOpenHashMap<Identifier, RenderLayer> LIGHT_CACHE = new Object2ObjectOpenHashMap<>();
	private static final Object2ObjectOpenHashMap<Identifier, RenderLayer> LIGHT_TRANSLUCENT_CACHE = new Object2ObjectOpenHashMap<>();
	private static final Object2ObjectOpenHashMap<Identifier, RenderLayer> LIGHT_2_CACHE = new Object2ObjectOpenHashMap<>();
	private static final Object2ObjectOpenHashMap<Identifier, RenderLayer> INTERIOR_CACHE = new Object2ObjectOpenHashMap<>();
	private static final Object2ObjectOpenHashMap<Identifier, RenderLayer> INTERIOR_TRANSLUCENT_CACHE = new Object2ObjectOpenHashMap<>();
	private static final Object2ObjectOpenHashMap<Identifier, RenderLayer> EXTERIOR_CACHE = new Object2ObjectOpenHashMap<>();
	private static final Object2ObjectOpenHashMap<Identifier, RenderLayer> EXTERIOR_TRANSLUCENT_CACHE = new Object2ObjectOpenHashMap<>();

	public static void removeFromCache(Identifier identifier) {
		LIGHT_CACHE.remove(identifier);
		LIGHT_TRANSLUCENT_CACHE.remove(identifier);
		LIGHT_2_CACHE.remove(identifier);
		INTERIOR_CACHE.remove(identifier);
		INTERIOR_TRANSLUCENT_CACHE.remove(identifier);
		EXTERIOR_CACHE.remove(identifier);
		EXTERIOR_TRANSLUCENT_CACHE.remove(identifier);
	}

	public static RenderLayer getLight(Identifier texture, boolean isTranslucent) {
		return checkCache(texture, () -> RenderLayer.getBeaconBeam(texture, isTranslucent), isTranslucent ? LIGHT_TRANSLUCENT_CACHE : LIGHT_CACHE);
	}

	public static RenderLayer getLight2(Identifier texture) {
		return checkCache(texture, () -> RenderLayer.getText(texture), LIGHT_2_CACHE);
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

	private static RenderLayer checkCache(Identifier identifier, Supplier<RenderLayer> supplier, Object2ObjectOpenHashMap<Identifier, RenderLayer> cache) {
		if (cache.containsKey(identifier)) {
			return cache.get(identifier);
		} else {
			final RenderLayer renderLayer = supplier.get();
			cache.put(identifier, renderLayer);
			return renderLayer;
		}
	}
}
