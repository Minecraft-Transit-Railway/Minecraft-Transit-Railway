package mtr.proxy;

import mtr.entity.EntityLightRail1;
import mtr.entity.EntitySP1900;
import mtr.render.RenderLightRail1;
import mtr.render.RenderSP1900;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy implements IProxy {

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		RenderingRegistry.registerEntityRenderingHandler(EntityLightRail1.class, RenderLightRail1::new);
		RenderingRegistry.registerEntityRenderingHandler(EntitySP1900.class, RenderSP1900::new);
	}
}
