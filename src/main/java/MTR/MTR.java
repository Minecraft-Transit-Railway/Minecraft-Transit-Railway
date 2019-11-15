package mtr;

import org.apache.logging.log4j.Logger;

import mtr.gui.GuiHandler;
import mtr.message.MessageOBAController;
import mtr.message.MessageOBAController.MessageOBAControllerHandler;
import mtr.message.MessageOBAData;
import mtr.message.MessageOBAData.MessageOBADataHandler;
import mtr.proxy.IProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = MTR.MODID, version = MTR.VERSION)
public class MTR {

	@SidedProxy(clientSide = "mtr.proxy.ClientProxy", serverSide = "mtr.proxy.ServerProxy")
	public static IProxy proxy;

	public static final String MODID = "mtr";
	public static final String VERSION = "3.0.0";

	@Instance(MODID)
	public static MTR instance;

	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("MTR");
	private static Logger logger;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		proxy.preInit(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		logger.info("Hi!");
		NetworkRegistry.INSTANCE.registerGuiHandler(MTR.instance, new GuiHandler());

		INSTANCE.registerMessage(MessageOBAControllerHandler.class, MessageOBAController.class, 0, Side.SERVER);
		INSTANCE.registerMessage(MessageOBAControllerHandler.class, MessageOBAController.class, 0, Side.CLIENT);
		INSTANCE.registerMessage(MessageOBADataHandler.class, MessageOBAData.class, 1, Side.CLIENT);
	}
}