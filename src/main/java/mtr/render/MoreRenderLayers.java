package mtr.render;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.util.Identifier;

public class MoreRenderLayers extends RenderLayer {

	public MoreRenderLayers(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, Runnable startAction, Runnable endAction) {
		super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, startAction, endAction);
	}

	public static RenderLayer getLight(Identifier texture) {
		return getBeaconBeam(texture, true);
	}

	public static RenderLayer getInterior(Identifier texture) {
		return getEntityCutout(texture);
	}

	public static RenderLayer getInteriorTranslucent(Identifier texture) {
		return getEntityTranslucentCull(texture);
	}

	public static RenderLayer getExterior(Identifier texture) {
		return getEntityCutout(texture);
	}

	public static RenderLayer getExteriorTranslucent(Identifier texture) {
		return getEntityTranslucentCull(texture);
	}
}
