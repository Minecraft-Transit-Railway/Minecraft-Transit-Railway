package org.mtr.render;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
//? if >= 26.1 {
/*import net.minecraft.client.renderer.rendertype.RenderTypes;
*///? }
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.mtr.resource.RenderStage;

import java.util.function.Supplier;

public class MoreRenderLayers {

	private static final Object2ObjectOpenHashMap<ResourceLocation, RenderType> LIGHT_CACHE = new Object2ObjectOpenHashMap<>();
	private static final Object2ObjectOpenHashMap<ResourceLocation, RenderType> LIGHT_TRANSLUCENT_CACHE = new Object2ObjectOpenHashMap<>();
	private static final Object2ObjectOpenHashMap<ResourceLocation, RenderType> LIGHT_2_CACHE = new Object2ObjectOpenHashMap<>();
	private static final Object2ObjectOpenHashMap<ResourceLocation, RenderType> INTERIOR_CACHE = new Object2ObjectOpenHashMap<>();
	private static final Object2ObjectOpenHashMap<ResourceLocation, RenderType> INTERIOR_TRANSLUCENT_CACHE = new Object2ObjectOpenHashMap<>();
	private static final Object2ObjectOpenHashMap<ResourceLocation, RenderType> EXTERIOR_CACHE = new Object2ObjectOpenHashMap<>();
	private static final Object2ObjectOpenHashMap<ResourceLocation, RenderType> EXTERIOR_TRANSLUCENT_CACHE = new Object2ObjectOpenHashMap<>();

	public static void removeFromCache(ResourceLocation identifier) {
		LIGHT_CACHE.remove(identifier);
		LIGHT_TRANSLUCENT_CACHE.remove(identifier);
		LIGHT_2_CACHE.remove(identifier);
		INTERIOR_CACHE.remove(identifier);
		INTERIOR_TRANSLUCENT_CACHE.remove(identifier);
		EXTERIOR_CACHE.remove(identifier);
		EXTERIOR_TRANSLUCENT_CACHE.remove(identifier);
	}

	/**
	 * The render layer a model of the given stage is drawn with.
	 *
	 * <p>Kept here rather than at the draw site because the answer is also needed while the model is
	 * being built: from 26.1 the layer's pipeline declares the vertex layout, so a mesh has to be
	 * packed in the format of the layer that will draw it. Both callers have to agree, so they ask
	 * the same question.</p>
	 */
	public static RenderType get(RenderStage renderStage, ResourceLocation texture) {
		return switch (renderStage) {
			case LIGHT -> getLight(texture, false);
			case ALWAYS_ON_LIGHT -> getLight(texture, true);
			case INTERIOR -> getInterior(texture);
			case INTERIOR_TRANSLUCENT -> getInteriorTranslucent(texture);
			case EXTERIOR -> getExterior(texture);
		};
	}

	public static RenderType getLight(ResourceLocation texture, boolean isTranslucent) {
		return checkCache(texture, () -> RenderType.beaconBeam(texture, isTranslucent), isTranslucent ? LIGHT_TRANSLUCENT_CACHE : LIGHT_CACHE);
	}

	public static RenderType getLight2(ResourceLocation texture) {
		return checkCache(texture, () -> RenderType.text(texture), LIGHT_2_CACHE);
	}

	public static RenderType getInterior(ResourceLocation texture) {
		return checkCache(texture, () -> RenderType.entityCutout(texture), INTERIOR_CACHE);
	}

	public static RenderType getInteriorTranslucent(ResourceLocation texture) {
		return checkCache(texture, () -> RenderType.itemEntityTranslucentCull(texture), INTERIOR_TRANSLUCENT_CACHE);
	}

	public static RenderType getExterior(ResourceLocation texture) {
		return checkCache(texture, () -> RenderType.entityCutout(texture), EXTERIOR_CACHE);
	}

	public static RenderType getExteriorTranslucent(ResourceLocation texture) {
		return checkCache(texture, () -> RenderType.itemEntityTranslucentCull(texture), EXTERIOR_TRANSLUCENT_CACHE);
	}

	private static RenderType checkCache(ResourceLocation identifier, Supplier<RenderType> supplier, Object2ObjectOpenHashMap<ResourceLocation, RenderType> cache) {
		if (cache.containsKey(identifier)) {
			return cache.get(identifier);
		} else {
			final RenderType renderLayer = supplier.get();
			cache.put(identifier, renderLayer);
			return renderLayer;
		}
	}
}
