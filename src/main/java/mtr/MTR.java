package mtr;

import org.apache.logging.log4j.Logger;

import mtr.gui.GuiHandler;
import mtr.proxy.IProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

@Mod(modid = MTR.MODID, version = MTR.VERSION, dependencies = MTR.DEPENDENCIES)
public class MTR {

	@SidedProxy(clientSide = "mtr.proxy.ClientProxy", serverSide = "mtr.proxy.ServerProxy")
	public static IProxy proxy;

	public static final String MODID = "mtr";
	public static final String VERSION = "3.0.0";
	public static final String DEPENDENCIES = "required-after:railcraft";

	@Instance(MODID)
	public static MTR instance;

	public static final SimpleNetworkWrapper NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel("MTR");
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
		proxy.init(event);
	}
}