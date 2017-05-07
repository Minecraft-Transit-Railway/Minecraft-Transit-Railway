package MTR.client;

import MTR.CommonProxy;
import MTR.GUIMap;
import MTR.GUINextTrain;
import MTR.GUIPSD;
import MTR.GUIRailBooster;
import MTR.GUIRouteChanger;
import MTR.GUIStationName;
import MTR.GUITrainTimer;
import MTR.TileEntityAPGGlassEntity;
import MTR.TileEntityAPGGlassRenderer;
import MTR.TileEntityClockEntity;
import MTR.TileEntityClockRenderer;
import MTR.TileEntityDoorEntity;
import MTR.TileEntityDoorRenderer;
import MTR.TileEntityNextTrainEntity;
import MTR.TileEntityPIDS1Entity;
import MTR.TileEntityPIDS1Renderer;
import MTR.TileEntityPSDBase;
import MTR.TileEntityPSDTopEntity;
import MTR.TileEntityPSDTopRenderer;
import MTR.TileEntityRailBoosterEntity;
import MTR.TileEntityRailBoosterRenderer;
import MTR.TileEntityRouteChangerEntity;
import MTR.TileEntityStationNameEntity;
import MTR.TileEntityStationNameRenderer;
import MTR.TileEntityTrainTimerEntity;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.client.registry.ClientRegistry;

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
		// ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRailEntity.class,
		// new TileEntityRailRenderer());
	}

	@Override
	public void openGUI(TileEntityPSDBase te) {
		Minecraft.getMinecraft().displayGuiScreen(new GUIPSD(te));
	}

	@Override
	public void openGUI(TileEntityNextTrainEntity te) {
		Minecraft.getMinecraft().displayGuiScreen(new GUINextTrain(te));
	}

	@Override
	public void openGUI(TileEntityRouteChangerEntity te) {
		Minecraft.getMinecraft().displayGuiScreen(new GUIRouteChanger(te));
	}

	@Override
	public void openGUI(TileEntityTrainTimerEntity te) {
		Minecraft.getMinecraft().displayGuiScreen(new GUITrainTimer(te));
	}

	@Override
	public void openGUI(TileEntityRailBoosterEntity te) {
		Minecraft.getMinecraft().displayGuiScreen(new GUIRailBooster(te));
	}

	@Override
	public void openGUI(TileEntityStationNameEntity te) {
		Minecraft.getMinecraft().displayGuiScreen(new GUIStationName(te));
	}

	@Override
	public void openMapGUI() {
		Minecraft.getMinecraft().displayGuiScreen(new GUIMap());
	}
}