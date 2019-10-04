package mtr.proxy;

import mtr.entity.EntityLightRail1;
import mtr.entity.EntitySP1900;
import mtr.render.RenderLightRail1;
import mtr.render.RenderSP1900;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy {

	public void registerEntityRenderers() {
		RenderingRegistry.registerEntityRenderingHandler(EntityLightRail1.class, RenderLightRail1::new);
		RenderingRegistry.registerEntityRenderingHandler(EntitySP1900.class, RenderSP1900::new);
	}
}
