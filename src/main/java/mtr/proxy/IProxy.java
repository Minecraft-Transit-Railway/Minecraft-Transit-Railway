package mtr.proxy;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public interface IProxy {

	public void preInit(FMLPreInitializationEvent event);

	public void init(FMLInitializationEvent event);
}
