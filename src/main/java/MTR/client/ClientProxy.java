package MTR.client;

import MTR.CommonProxy;
import MTR.EntityLightRail1;
import MTR.EntityMinecartSpecial;
import MTR.EntitySP1900;
import MTR.GUIMakePlatform;
import MTR.GUIShowPlatforms;
import MTR.MTRSounds;
import MTR.PlatformData;
import MTR.TileEntityAPGGlassEntity;
import MTR.TileEntityAPGGlassRenderer;
import MTR.TileEntityClockEntity;
import MTR.TileEntityClockRenderer;
import MTR.TileEntityDoorEntity;
import MTR.TileEntityDoorRenderer;
import MTR.TileEntityNextTrainEntity;
import MTR.TileEntityPIDS1Entity;
import MTR.TileEntityPIDS1Renderer;
import MTR.TileEntityPSDTopEntity;
import MTR.TileEntityPSDTopRenderer;
import MTR.TileEntityRailBoosterEntity;
import MTR.TileEntityRailBoosterRenderer;
import MTR.TileEntityRailEntity;
import MTR.TileEntityRailRenderer;
import MTR.TileEntityStationNameEntity;
import MTR.TileEntityStationNameRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {

	@Override
	public void registerRenderers() {
		// render tile entities
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAPGGlassEntity.class, new TileEntityAPGGlassRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityClockEntity.class, new TileEntityClockRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDoorEntity.class, new TileEntityDoorRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPIDS1Entity.class, new TileEntityPIDS1Renderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPSDTopEntity.class, new TileEntityPSDTopRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRailBoosterEntity.class,
				new TileEntityRailBoosterRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityStationNameEntity.class,
				new TileEntityStationNameRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRailEntity.class, new TileEntityRailRenderer());
	}

	@Override
	public void registerSounds() {
		MTRSounds.init();
	}

	@Override
	public void renderEntities() {
		// render entities
		RenderingRegistry.registerEntityRenderingHandler(EntityMinecartSpecial.class, RenderMinecartSpecial::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityLightRail1.class, RenderLightRail1::new);
		RenderingRegistry.registerEntityRenderingHandler(EntitySP1900.class, RenderSP1900::new);
		// obj
		OBJLoader.INSTANCE.addDomain("mtr");
	}

	@Override
	public void registerItemRenderer(Item item, int meta, String id) {
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation("mtr:" + id, "inventory"));
	}

	@Override
	public void openGUI(int x, int y, int z) {
		Minecraft.getMinecraft().displayGuiScreen(new GUIMakePlatform(x, y, z));
	}

	@Override
	public void openGUI(PlatformData data) {
		Minecraft.getMinecraft().displayGuiScreen(new GUIShowPlatforms(data));
	}

	@Override
	public void openGUI(PlatformData data, TileEntityNextTrainEntity te) {
		Minecraft.getMinecraft().displayGuiScreen(new GUIShowPlatforms(data, te));
	}

	@Override
	public void openGUI(PlatformData data, TileEntityPIDS1Entity te) {
		Minecraft.getMinecraft().displayGuiScreen(new GUIShowPlatforms(data, te));
	}
}