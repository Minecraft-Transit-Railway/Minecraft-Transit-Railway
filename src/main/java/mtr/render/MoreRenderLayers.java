package mtr.render;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class MoreRenderLayers extends RenderPhase {

	private static final Map<Identifier, RenderLayer> LIGHT_CACHE = new HashMap<>();
	private static final Map<Identifier, RenderLayer> INTERIOR_CACHE = new HashMap<>();
	private static final Map<Identifier, RenderLayer> INTERIOR_TRANSLUCENT_CACHE = new HashMap<>();
	private static final Map<Identifier, RenderLayer> EXTERIOR_CACHE = new HashMap<>();
	private static final Map<Identifier, RenderLayer> EXTERIOR_TRANSLUCENT_CACHE = new HashMap<>();

	public MoreRenderLayers(String name, Runnable beginAction, Runnable endAction) {
		super(name, beginAction, endAction);
	}

	public static RenderLayer getLight(Identifier texture) {
		return checkCache(texture, () -> {
			final RenderLayer.MultiPhaseParameters multiPhaseParameters = getParameters(texture).alpha(ONE_TENTH_ALPHA).build(true);
			return RenderLayer.of("train_light", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, 7, 256, false, false, multiPhaseParameters);
		}, LIGHT_CACHE);
	}

	public static RenderLayer getInterior(Identifier texture) {
		return checkCache(texture, () -> {
			final RenderLayer.MultiPhaseParameters multiPhaseParameters = getParameters(texture).diffuseLighting(ENABLE_DIFFUSE_LIGHTING).alpha(ONE_TENTH_ALPHA).overlay(ENABLE_OVERLAY_COLOR).build(true);
			return RenderLayer.of("train_interior", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, 7, 256, true, true, multiPhaseParameters);
		}, INTERIOR_CACHE);
	}

	public static RenderLayer getInteriorTranslucent(Identifier texture) {
		return checkCache(texture, () -> {
			final RenderLayer.MultiPhaseParameters multiPhaseParameters = getParameters(texture).transparency(TRANSLUCENT_TRANSPARENCY).diffuseLighting(ENABLE_DIFFUSE_LIGHTING).alpha(ONE_TENTH_ALPHA).overlay(ENABLE_OVERLAY_COLOR).build(true);
			return RenderLayer.of("train_interior_translucent", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, 7, 256, true, true, multiPhaseParameters);
		}, INTERIOR_TRANSLUCENT_CACHE);
	}

	public static RenderLayer getExterior(Identifier texture) {
		return checkCache(texture, () -> {
			final RenderLayer.MultiPhaseParameters multiPhaseParameters = getParameters(texture).diffuseLighting(ENABLE_DIFFUSE_LIGHTING).alpha(ONE_TENTH_ALPHA).lightmap(ENABLE_LIGHTMAP).overlay(ENABLE_OVERLAY_COLOR).build(true);
			return RenderLayer.of("train_exterior", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, 7, 256, true, true, multiPhaseParameters);
		}, EXTERIOR_CACHE);
	}

	public static RenderLayer getExteriorTranslucent(Identifier texture) {
		return checkCache(texture, () -> {
			final RenderLayer.MultiPhaseParameters multiPhaseParameters = getParameters(texture).transparency(TRANSLUCENT_TRANSPARENCY).diffuseLighting(ENABLE_DIFFUSE_LIGHTING).alpha(ONE_TENTH_ALPHA).lightmap(ENABLE_LIGHTMAP).overlay(ENABLE_OVERLAY_COLOR).build(true);
			return RenderLayer.of("train_exterior_translucent", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, 7, 256, true, true, multiPhaseParameters);
		}, EXTERIOR_TRANSLUCENT_CACHE);
	}

	private static RenderLayer.MultiPhaseParameters.Builder getParameters(Identifier texture) {
		return RenderLayer.MultiPhaseParameters.builder().texture(new Texture(texture, false, false));
	}

	private static RenderLayer checkCache(Identifier identifier, Supplier<RenderLayer> supplier, Map<Identifier, RenderLayer> cache) {
		if (cache.containsKey(identifier)) {
			return cache.get(identifier);
		} else {
			final RenderLayer renderLayer = supplier.get();
			cache.put(identifier, renderLayer);
			return renderLayer;
		}
	}
}
