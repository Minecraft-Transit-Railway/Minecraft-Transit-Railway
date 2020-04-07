package mtr.proxy;

import mtr.render.TileEntityAPGDoorRenderer;
import mtr.render.TileEntityPSDDoorRenderer;
import mtr.tile.TileEntityAPGDoor;
import mtr.tile.TileEntityPSDDoor;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy implements IProxy {

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAPGDoor.class, new TileEntityAPGDoorRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPSDDoor.class, new TileEntityPSDDoorRenderer());
	}

	@Override
	public void init(FMLInitializationEvent event) {
	}
}
