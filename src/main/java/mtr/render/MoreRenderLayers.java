package mtr.render;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;

public class MoreRenderLayers extends RenderPhase {

	public MoreRenderLayers(String name, Runnable beginAction, Runnable endAction) {
		super(name, beginAction, endAction);
	}

	public static RenderLayer getLight(Identifier texture) {
		final RenderLayer.MultiPhaseParameters multiPhaseParameters = getParameters(texture).alpha(ONE_TENTH_ALPHA).build(true);
		return RenderLayer.of("train_light", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, 7, 256, false, false, multiPhaseParameters);
	}

	public static RenderLayer getInterior(Identifier texture) {
		final RenderLayer.MultiPhaseParameters multiPhaseParameters = getParameters(texture).diffuseLighting(ENABLE_DIFFUSE_LIGHTING).alpha(ONE_TENTH_ALPHA).overlay(ENABLE_OVERLAY_COLOR).build(true);
		return RenderLayer.of("train_interior", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, 7, 256, true, true, multiPhaseParameters);
	}

	public static RenderLayer getInteriorTranslucent(Identifier texture) {
		final RenderLayer.MultiPhaseParameters multiPhaseParameters = getParameters(texture).transparency(TRANSLUCENT_TRANSPARENCY).diffuseLighting(ENABLE_DIFFUSE_LIGHTING).alpha(ONE_TENTH_ALPHA).overlay(ENABLE_OVERLAY_COLOR).build(true);
		return RenderLayer.of("train_interior", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, 7, 256, true, true, multiPhaseParameters);
	}

	public static RenderLayer getExterior(Identifier texture) {
		final RenderLayer.MultiPhaseParameters multiPhaseParameters = getParameters(texture).diffuseLighting(ENABLE_DIFFUSE_LIGHTING).alpha(ONE_TENTH_ALPHA).lightmap(ENABLE_LIGHTMAP).overlay(ENABLE_OVERLAY_COLOR).build(true);
		return RenderLayer.of("train_exterior", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, 7, 256, true, true, multiPhaseParameters);
	}

	private static RenderLayer.MultiPhaseParameters.Builder getParameters(Identifier texture) {
		return RenderLayer.MultiPhaseParameters.builder().texture(new Texture(texture, false, false));
	}
}
