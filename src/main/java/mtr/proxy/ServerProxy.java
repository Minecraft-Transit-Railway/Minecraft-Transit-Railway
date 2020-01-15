package mtr.proxy;

import mtr.MTR;
import mtr.message.MessageOBAController;
import mtr.message.MessageOBAController.MessageOBAControllerServerHandler;
import mtr.message.MessageOBAData;
import mtr.message.MessageOBAData.MessageOBADataServerHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

public class ServerProxy implements IProxy {

	@Override
	public void preInit(FMLPreInitializationEvent event) {
	}

	@Override
	public void init(FMLInitializationEvent event) {
		MTR.NETWORK.registerMessage(MessageOBAControllerServerHandler.class, MessageOBAController.class, 0, Side.SERVER);
		MTR.NETWORK.registerMessage(MessageOBAControllerServerHandler.class, MessageOBAController.class, 0, Side.CLIENT);
		MTR.NETWORK.registerMessage(MessageOBADataServerHandler.class, MessageOBAData.class, 1, Side.CLIENT);
	}
}
