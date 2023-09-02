package mtr.render;

import mtr.mappings.RenderLayerMapper;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class MoreRenderLayers extends RenderLayerMapper {

	private static final Map<String, RenderType> LIGHT_CACHE = new HashMap<>();
	private static final Map<ResourceLocation, RenderType> INTERIOR_CACHE = new HashMap<>();
	private static final Map<ResourceLocation, RenderType> INTERIOR_TRANSLUCENT_CACHE = new HashMap<>();
	private static final Map<ResourceLocation, RenderType> EXTERIOR_CACHE = new HashMap<>();
	private static final Map<ResourceLocation, RenderType> EXTERIOR_TRANSLUCENT_CACHE = new HashMap<>();

	public static RenderType getLight(ResourceLocation texture, boolean isTranslucent) {
		return checkCache(texture.toString() + isTranslucent, () -> beaconBeam(texture, isTranslucent), LIGHT_CACHE);
	}

	public static RenderType getInterior(ResourceLocation texture) {
		return checkCache(texture, () -> entityCutout(texture), INTERIOR_CACHE);
	}

	public static RenderType getInteriorTranslucent(ResourceLocation texture) {
		return checkCache(texture, () -> entityTranslucentCull(texture), INTERIOR_TRANSLUCENT_CACHE);
	}

	public static RenderType getExterior(ResourceLocation texture) {
		return checkCache(texture, () -> entityCutout(texture), EXTERIOR_CACHE);
	}

	public static RenderType getExteriorTranslucent(ResourceLocation texture) {
		return checkCache(texture, () -> entityTranslucentCull(texture), EXTERIOR_TRANSLUCENT_CACHE);
	}

	private static <T> RenderType checkCache(T identifier, Supplier<RenderType> supplier, Map<T, RenderType> cache) {
		if (cache.containsKey(identifier)) {
			return cache.get(identifier);
		} else {
			final RenderType renderLayer = supplier.get();
			cache.put(identifier, renderLayer);
			return renderLayer;
		}
	}
}
