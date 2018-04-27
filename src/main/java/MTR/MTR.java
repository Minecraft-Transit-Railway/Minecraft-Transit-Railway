package MTR;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraft.block.BlockDispenser;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid = MTR.MODID, name = MTR.MODNAME, version = MTR.VERSION, updateJSON = "https://www.dropbox.com/s/0w92twckzlof7fe/MTRVersions.json?dl=1", useMetadata = true)
public class MTR {

	public static final String MODID = "mtr";
	public static final String MODNAME = "Minecraft Transit Railway";
	public static final String VERSION = "1.10.2-2.1.2";

	public static String verseOfTheDay = "";

	@Instance
	public static MTR instance = new MTR();

	public static SimpleNetworkWrapper network;

	@SidedProxy(clientSide = "MTR.client.ClientProxy", serverSide = "MTR.CommonProxy")
	public static CommonProxy proxy;
	public static CreativeTabs MTRTab = new CreativeTabs("MTR") {
		@Override
		@SideOnly(Side.CLIENT)
		public Item getTabIconItem() {
			return MTRItems.itemtrain;
		}

		@Override
		public String getBackgroundImageName() {
			return "item_search.png";
		}
	};
	public static CreativeTabs MTRTabStationName = new CreativeTabs("MTRStationName") {
		@Override
		@SideOnly(Side.CLIENT)
		public Item getTabIconItem() {
			return MTRItems.itemstationname;
		}

		@Override
		public int getIconItemDamage() {
			return 10;
		}

		@Override
		public boolean hasSearchBar() {
			return true;
		}

		@Override
		public String getBackgroundImageName() {
			return "item_search.png";
		}
	};

	@EventHandler
	public void preinit(FMLPreInitializationEvent event) {
		// blocks and items
		MTRBlocks.init();
		MTRItems.init();
		// entities
		EntityRegistry.registerModEntity(EntityMinecartSpecial.class, "MinecartSpecial", 0, this, 80, 1, false);
		EntityRegistry.registerModEntity(EntityLightRail1.class, "LightRail1", 1, this, 256, 1, false);
		EntityRegistry.registerModEntity(EntitySP1900.class, "SP1900", 2, this, 256, 1, false);
		EntityRegistry.registerModEntity(EntityMTrain.class, "MTrain", 3, this, 256, 1, false);
		// tile entities
		GameRegistry.registerTileEntity(TileEntityClockEntity.class, "MTRClock");
		GameRegistry.registerTileEntity(TileEntityAPGGlassEntity.class, "APGGlass");
		GameRegistry.registerTileEntity(TileEntityDoorEntity.class, "MTRDoor");
		GameRegistry.registerTileEntity(TileEntityNextTrainEntity.class, "NextTrain");
		GameRegistry.registerTileEntity(TileEntityPIDS1Entity.class, "PIDS1");
		GameRegistry.registerTileEntity(TileEntityPSDTopEntity.class, "PSDTop");
		GameRegistry.registerTileEntity(TileEntityRailEntity.class, "MTRRail");
		GameRegistry.registerTileEntity(TileEntityRailBoosterEntity.class, "BoosterRail");
		GameRegistry.registerTileEntity(TileEntityRouteChangerEntity.class, "RouteChanger");
		GameRegistry.registerTileEntity(TileEntityStationNameEntity.class, "StationName");
		// network
		network = NetworkRegistry.INSTANCE.newSimpleChannel("MyChannel");
		network.registerMessage(MessageBoosterRail.Handler.class, MessageBoosterRail.class, 0, Side.SERVER);
		network.registerMessage(MessageNextTrain.Handler.class, MessageNextTrain.class, 1, Side.SERVER);
		network.registerMessage(MessagePlatformMaker.Handler.class, MessagePlatformMaker.class, 2, Side.SERVER);
		network.registerMessage(MessagePSD.Handler.class, MessagePSD.class, 3, Side.SERVER);
		network.registerMessage(MessageRouteChanger.Handler.class, MessageRouteChanger.class, 4, Side.SERVER);
		network.registerMessage(MessageWorldData.Handler.class, MessageWorldData.class, 5, Side.CLIENT);
		// chunk loading
		ForgeChunkManager.setForcedChunkLoadingCallback(this, new EntityTrainBase(null));
		proxy.renderEntities();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		// recipes
		Recipes.addShapelessRecipes();
		Recipes.addShapedRecipes();

		// hi
		Logger log = FMLLog.getLogger();
		log.info("");
		log.info("HH      HH  IIIIIIIIII");
		log.info("HH      HH      II    ");
		log.info("HH      HH      II    ");
		log.info("HHHHHHHHHH      II    ");
		log.info("HH      HH      II    ");
		log.info("HH      HH      II    ");
		log.info("HH      HH  IIIIIIIIII");
		log.info("");

		// verse of the day
		try {
			URL url = new URL("https://www.biblegateway.com/votd/get/?format=json&version=NIV");
			verseOfTheDay = getVerse(url);
			log.info(verseOfTheDay + " NIV");
			log.info("Powered by BibleGateway.com");
		} catch (MalformedURLException e) {
		}
	}

	@EventHandler
	public void load(FMLInitializationEvent event) {
		proxy.registerRenderers();
		proxy.registerSounds();

		// dispenser action
		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(MTRItems.itemkilltrain,
				new MTRDispenserBehavior.DispenseKillTrain());
		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(MTRItems.itemlightrail1,
				new MTRDispenserBehavior.DispenseLightRail1());
		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(MTRItems.itemsp1900,
				new MTRDispenserBehavior.DispenseSP1900());
		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(MTRItems.itemmtrain,
				new MTRDispenserBehavior.DispenseMTrain());

		// event handler
		MinecraftForge.EVENT_BUS.register(new MTREventHandler());
	}

	@EventHandler
	public void serverLoad(FMLServerStartingEvent event) {
		// commands
		event.registerServerCommand(new CommandTicTacToe());
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		// Stub Method
	}

	private String getVerse(URL url) {
		BufferedReader in;
		String verse = "", reference = "";
		try {
			in = new BufferedReader(new InputStreamReader(url.openStream()));
			JsonParser parser = new JsonParser();
			JsonObject object = (JsonObject) parser.parse(in.readLine());
			verse = object.getAsJsonObject("votd").get("text").getAsString();
			verse = StringEscapeUtils.unescapeHtml4(verse);
			reference = object.getAsJsonObject("votd").get("display_ref").getAsString();
			reference = StringEscapeUtils.unescapeHtml4(reference);
			in.close();
		} catch (IOException e) {
		} catch (Exception e) {
		}
		return verse + " " + reference;
	}
}