package mtr.render;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class MoreRenderLayers extends RenderLayer {

	private static final Map<String, RenderLayer> LIGHT_CACHE = new HashMap<>();
	private static final Map<Identifier, RenderLayer> INTERIOR_CACHE = new HashMap<>();
	private static final Map<Identifier, RenderLayer> INTERIOR_TRANSLUCENT_CACHE = new HashMap<>();
	private static final Map<Identifier, RenderLayer> EXTERIOR_CACHE = new HashMap<>();
	private static final Map<Identifier, RenderLayer> EXTERIOR_TRANSLUCENT_CACHE = new HashMap<>();

	public MoreRenderLayers(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, Runnable startAction, Runnable endAction) {
		super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, startAction, endAction);
	}

	public static RenderLayer getLight(Identifier texture, boolean isTranslucent) {
		return checkCache(texture.toUnderscoreSeparatedString() + isTranslucent, () -> getBeaconBeam(texture, isTranslucent), LIGHT_CACHE);
	}

	public static RenderLayer getInterior(Identifier texture) {
		return checkCache(texture, () -> getEntityCutout(texture), INTERIOR_CACHE);
	}

	public static RenderLayer getInteriorTranslucent(Identifier texture) {
		return checkCache(texture, () -> getEntityTranslucentCull(texture), INTERIOR_TRANSLUCENT_CACHE);
	}

	public static RenderLayer getExterior(Identifier texture) {
		return checkCache(texture, () -> getEntityCutout(texture), EXTERIOR_CACHE);
	}

	public static RenderLayer getExteriorTranslucent(Identifier texture) {
		return checkCache(texture, () -> getEntityTranslucentCull(texture), EXTERIOR_TRANSLUCENT_CACHE);
	}

	private static <T> RenderLayer checkCache(T identifier, Supplier<RenderLayer> supplier, Map<T, RenderLayer> cache) {
		if (cache.containsKey(identifier)) {
			return cache.get(identifier);
		} else {
			final RenderLayer renderLayer = supplier.get();
			cache.put(identifier, renderLayer);
			return renderLayer;
		}
	}
}
