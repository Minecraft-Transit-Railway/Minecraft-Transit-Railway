package mtr.proxy;

import mtr.MTR;
import mtr.entity.EntityLightRail1;
import mtr.entity.EntityMTrain;
import mtr.entity.EntitySP1900;
import mtr.message.MessageOBAController;
import mtr.message.MessageOBAController.MessageOBAControllerClientHandler;
import mtr.message.MessageOBAData;
import mtr.message.MessageOBAData.MessageOBADataClientHandler;
import mtr.render.RenderLightRail1;
import mtr.render.RenderMTrain;
import mtr.render.RenderSP1900;
import mtr.render.TileEntityOBAControllerRenderer;
import mtr.tile.TileEntityOBAController;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

public class ClientProxy implements IProxy {

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		RenderingRegistry.registerEntityRenderingHandler(EntityLightRail1.class, RenderLightRail1::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityMTrain.class, RenderMTrain::new);
		RenderingRegistry.registerEntityRenderingHandler(EntitySP1900.class, RenderSP1900::new);

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityOBAController.class, new TileEntityOBAControllerRenderer());
	}

	@Override
	public void init(FMLInitializationEvent event) {
		MTR.NETWORK.registerMessage(MessageOBAControllerClientHandler.class, MessageOBAController.class, 0, Side.SERVER);
		MTR.NETWORK.registerMessage(MessageOBAControllerClientHandler.class, MessageOBAController.class, 0, Side.CLIENT);
		MTR.NETWORK.registerMessage(MessageOBADataClientHandler.class, MessageOBAData.class, 1, Side.CLIENT);
	}
}
