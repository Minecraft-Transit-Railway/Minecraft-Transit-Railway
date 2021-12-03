package mapper;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexFormat;

public abstract class RenderLayerMapper extends RenderLayer {

	public RenderLayerMapper() {
		super(null, null, null, 0, false, false, null, null);
	}
}
