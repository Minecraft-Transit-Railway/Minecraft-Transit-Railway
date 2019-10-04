package mtr;

import org.apache.logging.log4j.Logger;

import mtr.proxy.ClientProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = MTR.MODID, version = MTR.VERSION)
public class MTR {

	@SidedProxy(clientSide = "mtr.proxy.ClientProxy")
	public static ClientProxy clientProxy;

	public static final String MODID = "mtr";
	public static final String VERSION = "3.0.0";

	private static Logger logger;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		clientProxy.registerEntityRenderers();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		logger.info("Hi!");
	}
}