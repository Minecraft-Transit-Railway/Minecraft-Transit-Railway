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

import MTR.blocks.BlockAPGDoor;
import MTR.blocks.BlockAPGDoorClosed;
import MTR.blocks.BlockAPGGlassBottom;
import MTR.blocks.BlockAPGGlassMiddle;
import MTR.blocks.BlockAPGGlassTop;
import MTR.blocks.BlockAdSide;
import MTR.blocks.BlockCeiling;
import MTR.blocks.BlockClock;
import MTR.blocks.BlockEscalatorLanding;
import MTR.blocks.BlockEscalatorSide;
import MTR.blocks.BlockEscalatorSideLanding;
import MTR.blocks.BlockEscalatorSign;
import MTR.blocks.BlockEscalatorStep;
import MTR.blocks.BlockLogo;
import MTR.blocks.BlockMTRMap;
import MTR.blocks.BlockMTRMapSide;
import MTR.blocks.BlockMTRPaneA;
import MTR.blocks.BlockMTRPaneB;
import MTR.blocks.BlockMTRPaneC;
import MTR.blocks.BlockMTRPaneD;
import MTR.blocks.BlockMTRPaneE;
import MTR.blocks.BlockMTRPaneF;
import MTR.blocks.BlockMTRPaneG;
import MTR.blocks.BlockMTRPaneH;
import MTR.blocks.BlockMTRSignPost;
import MTR.blocks.BlockNextTrain;
import MTR.blocks.BlockPIDS1;
import MTR.blocks.BlockPSDDoor;
import MTR.blocks.BlockPSDDoorClosed;
import MTR.blocks.BlockPSDGlass;
import MTR.blocks.BlockPSDGlass2015;
import MTR.blocks.BlockPSDGlassEnd;
import MTR.blocks.BlockPSDGlassMiddle;
import MTR.blocks.BlockPSDGlassVeryEnd;
import MTR.blocks.BlockPSDTop;
import MTR.blocks.BlockPlatform;
import MTR.blocks.BlockRailBooster;
import MTR.blocks.BlockRailCurved;
import MTR.blocks.BlockRailDetector2;
import MTR.blocks.BlockRailDummy;
import MTR.blocks.BlockRailSlope1;
import MTR.blocks.BlockRailSlope2;
import MTR.blocks.BlockRailStation;
import MTR.blocks.BlockRailStraight;
import MTR.blocks.BlockRouteChanger;
import MTR.blocks.BlockRubbishBin1;
import MTR.blocks.BlockStation;
import MTR.blocks.BlockStationNameA;
import MTR.blocks.BlockStationNameB;
import MTR.blocks.BlockStationNameC;
import MTR.blocks.BlockStationNameD;
import MTR.blocks.BlockStationNameE;
import MTR.blocks.BlockStationNameF;
import MTR.blocks.BlockStationNameG;
import MTR.blocks.BlockStationNameH;
import MTR.blocks.BlockStationNameI;
import MTR.blocks.BlockStationNameJ;
import MTR.blocks.BlockStationNameK;
import MTR.blocks.BlockStationNameL;
import MTR.blocks.BlockStationNameM;
import MTR.blocks.BlockStationNameN;
import MTR.blocks.BlockStationNameO;
import MTR.blocks.BlockStationNameP;
import MTR.blocks.BlockStationNameQ;
import MTR.blocks.BlockStationNameR;
import MTR.blocks.BlockStationNameS;
import MTR.blocks.BlockStationNameT;
import MTR.blocks.BlockStationNameU;
import MTR.blocks.BlockStationNameV;
import MTR.blocks.BlockStationNameWall1;
import MTR.blocks.BlockTicketMachine;
import MTR.blocks.BlockTrainTimer;
import MTR.client.RenderLightRail1;
import MTR.client.RenderMinecartSpecial;
import MTR.client.RenderSP1900;
import MTR.items.ItemAPG;
import MTR.items.ItemBrush;
import MTR.items.ItemEscalator;
import MTR.items.ItemKillTrain;
import MTR.items.ItemLightRail1;
import MTR.items.ItemMTRPane;
import MTR.items.ItemMinecartSpecial;
import MTR.items.ItemPSD;
import MTR.items.ItemRail;
import MTR.items.ItemSP1900;
import MTR.items.ItemSpawnPlatform;
import MTR.items.ItemStationName;
import MTR.items.ItemStationNamePole;
import MTR.items.ItemTrain;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
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

@Mod(modid = MTR.MODID, name = MTR.MODNAME, version = MTR.VERSION)
public class MTR {

	public static final String MODID = "MTR";
	public static final String MODNAME = "Minecraft Transit Railway";
	public static final String VERSION = "1.8.9-2.1.1";

	public static String verseOfTheDay = "";

	@Instance
	public static MTR instance = new MTR();

	public static Item itemlightrail1;
	public static Item itemsp1900;
	public static Item itemminecartspecial;
	public static Item itemapg;
	public static Item itempsd;
	public static Item itembrush;
	public static Item itemescalator;
	public static Item itemkilltrain;
	public static Item itemmtrpane;
	public static Item itemrail;
	public static Item itemspawnplatform;
	public static Item itemstationname;
	public static Item itemstationnamepole;
	public static Item itemtrain;
	public static Block blockadside;
	public static Block blockapgdoor;
	public static Block blockapgdoorclosed;
	public static Block blockapgglasstop;
	public static Block blockapgglassbottom;
	public static Block blockapgglassmiddle;
	public static Block blockceiling;
	public static Block blockclock;
	public static Block blockescalatorstep;
	public static Block blockescalatorside;
	public static Block blockescalatorsidelanding;
	public static Block blockescalatorlanding;
	public static Block blockescalatorsign;
	public static Block blocklogo;
	public static Block blockmtrmap;
	public static Block blockmtrmapside;
	public static Block blockmtrsignpost;
	public static Block blockmtrpanea;
	public static Block blockmtrpaneb;
	public static Block blockmtrpanec;
	public static Block blockmtrpaned;
	public static Block blockmtrpanee;
	public static Block blockmtrpanef;
	public static Block blockmtrpaneg;
	public static Block blockmtrpaneh;
	public static Block blocknexttrain;
	public static Block blockpids1;
	public static Block blockplatform;
	public static Block blockpsddoor;
	public static Block blockpsddoorclosed;
	public static Block blockpsdglass;
	public static Block blockpsdglassend;
	public static Block blockpsdglass2015;
	public static Block blockpsdglassmiddle;
	public static Block blockpsdglassveryend;
	public static Block blockpsdtop;
	public static Block blockraildummy;
	public static Block blockrailstraight;
	public static Block blockrailbooster;
	public static Block blockrailslope1;
	public static Block blockrailslope2;
	public static Block blockraildetector;
	public static Block blockrailstation;
	public static Block blockrailcurved;
	public static Block blockrailcurved2;
	public static Block blockroutechanger;
	public static Block blockrubbishbin1;
	public static Block blockstation;
	public static Block blockstationnamewall1;
	public static Block blockstationnamea;
	public static Block blockstationnameb;
	public static Block blockstationnamec;
	public static Block blockstationnamed;
	public static Block blockstationnamee;
	public static Block blockstationnamef;
	public static Block blockstationnameg;
	public static Block blockstationnameh;
	public static Block blockstationnamei;
	public static Block blockstationnamej;
	public static Block blockstationnamek;
	public static Block blockstationnamel;
	public static Block blockstationnamem;
	public static Block blockstationnamen;
	public static Block blockstationnameo;
	public static Block blockstationnamep;
	public static Block blockstationnameq;
	public static Block blockstationnamer;
	public static Block blockstationnames;
	public static Block blockstationnamet;
	public static Block blockstationnameu;
	public static Block blockstationnamev;
	public static Block blockticketmachine;
	public static Block blocktraintimer;
	public static SimpleNetworkWrapper network;

	@SidedProxy(clientSide = "MTR.client.ClientProxy", serverSide = "MTR.CommonProxy")
	public static CommonProxy proxy;
	public static CreativeTabs MTRTab = new CreativeTabs("MTR") {
		@Override
		@SideOnly(Side.CLIENT)
		public Item getTabIconItem() {
			return MTR.itemtrain;
		}
	};
	public static CreativeTabs MTRTabStationName = new CreativeTabs("MTRStationName") {
		@Override
		@SideOnly(Side.CLIENT)
		public Item getTabIconItem() {
			return MTR.itemstationname;
		}

		@Override
		public int getIconItemDamage() {
			return 10;
		};
	};

	@EventHandler
	public void preinit(FMLPreInitializationEvent event) {
		// items
		itemlightrail1 = new ItemLightRail1();
		itemsp1900 = new ItemSP1900();
		itemminecartspecial = new ItemMinecartSpecial();
		itemtrain = new ItemTrain();
		itemapg = new ItemAPG();
		itempsd = new ItemPSD();
		itembrush = new ItemBrush();
		itemescalator = new ItemEscalator();
		itemkilltrain = new ItemKillTrain();
		itemmtrpane = new ItemMTRPane();
		itemspawnplatform = new ItemSpawnPlatform();
		itemstationname = new ItemStationName();
		itemstationnamepole = new ItemStationNamePole();
		itemrail = new ItemRail();

		// item variants
		if (event.getSide() == Side.CLIENT) {
			RegisterModels.registerItemVariants();

			// render entities
			RenderingRegistry.registerEntityRenderingHandler(EntityMinecartSpecial.class, RenderMinecartSpecial::new);
			RenderingRegistry.registerEntityRenderingHandler(EntityLightRail1.class, RenderLightRail1::new);
			RenderingRegistry.registerEntityRenderingHandler(EntitySP1900.class, RenderSP1900::new);
		} else {

		}
		// blocks
		blockstation = new BlockStation();
		blocklogo = new BlockLogo();
		blockadside = new BlockAdSide();
		blockapgdoor = new BlockAPGDoor();
		blockapgdoorclosed = new BlockAPGDoorClosed();
		blockapgglasstop = new BlockAPGGlassTop();
		blockapgglassbottom = new BlockAPGGlassBottom();
		blockapgglassmiddle = new BlockAPGGlassMiddle();
		blockceiling = new BlockCeiling();
		blockclock = new BlockClock();
		blockescalatorstep = new BlockEscalatorStep();
		blockescalatorside = new BlockEscalatorSide();
		blockescalatorsidelanding = new BlockEscalatorSideLanding();
		blockescalatorlanding = new BlockEscalatorLanding();
		blockescalatorsign = new BlockEscalatorSign();
		blockpsddoor = new BlockPSDDoor();
		blockpsddoorclosed = new BlockPSDDoorClosed();
		blockpsdglass = new BlockPSDGlass();
		blockpsdglassend = new BlockPSDGlassEnd();
		blockpsdglass2015 = new BlockPSDGlass2015();
		blockpsdglassmiddle = new BlockPSDGlassMiddle();
		blockpsdglassveryend = new BlockPSDGlassVeryEnd();
		blockpsdtop = new BlockPSDTop();
		blockmtrmap = new BlockMTRMap();
		blockmtrmapside = new BlockMTRMapSide();
		blockmtrsignpost = new BlockMTRSignPost();
		blockmtrpanea = new BlockMTRPaneA();
		blockmtrpaneb = new BlockMTRPaneB();
		blockmtrpanec = new BlockMTRPaneC();
		blockmtrpaned = new BlockMTRPaneD();
		blockmtrpanee = new BlockMTRPaneE();
		blockmtrpanef = new BlockMTRPaneF();
		blockmtrpaneg = new BlockMTRPaneG();
		blockmtrpaneh = new BlockMTRPaneH();
		blocknexttrain = new BlockNextTrain();
		blockpids1 = new BlockPIDS1();
		blockplatform = new BlockPlatform();
		blockraildummy = new BlockRailDummy();
		blockrailstraight = new BlockRailStraight();
		blockrailbooster = new BlockRailBooster();
		blockrailslope1 = new BlockRailSlope1();
		blockrailslope2 = new BlockRailSlope2();
		blockraildetector = new BlockRailDetector2();
		blockrailstation = new BlockRailStation();
		blockrailcurved = new BlockRailCurved();
		blockroutechanger = new BlockRouteChanger();
		blockrubbishbin1 = new BlockRubbishBin1();
		blockstationnamewall1 = new BlockStationNameWall1();
		blockstationnamea = new BlockStationNameA();
		blockstationnameb = new BlockStationNameB();
		blockstationnamec = new BlockStationNameC();
		blockstationnamed = new BlockStationNameD();
		blockstationnamee = new BlockStationNameE();
		blockstationnamef = new BlockStationNameF();
		blockstationnameg = new BlockStationNameG();
		blockstationnameh = new BlockStationNameH();
		blockstationnamei = new BlockStationNameI();
		blockstationnamej = new BlockStationNameJ();
		blockstationnamek = new BlockStationNameK();
		blockstationnamel = new BlockStationNameL();
		blockstationnamem = new BlockStationNameM();
		blockstationnamen = new BlockStationNameN();
		blockstationnameo = new BlockStationNameO();
		blockstationnamep = new BlockStationNameP();
		blockstationnameq = new BlockStationNameQ();
		blockstationnamer = new BlockStationNameR();
		blockstationnames = new BlockStationNameS();
		blockstationnamet = new BlockStationNameT();
		blockstationnameu = new BlockStationNameU();
		blockstationnamev = new BlockStationNameV();
		blockticketmachine = new BlockTicketMachine();
		blocktraintimer = new BlockTrainTimer();
		// entities
		EntityRegistry.registerModEntity(EntityTrainBase.class, "TrainBase", 0, this, 80, 5, false);
		EntityRegistry.registerModEntity(EntityMinecartSpecial.class, "MinecartSpecial", 2, this, 80, 5, false);
		EntityRegistry.registerModEntity(EntityLightRail1.class, "LightRail1", 3, this, 80, 5, false);
		EntityRegistry.registerModEntity(EntitySP1900.class, "SP1900", 4, this, 80, 5, false);
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
		GameRegistry.registerTileEntity(TileEntityTicketMachineEntity.class, "TicketMachine");
		GameRegistry.registerTileEntity(TileEntityTrainTimerEntity.class, "TrainTimer");
		// network
		network = NetworkRegistry.INSTANCE.newSimpleChannel("MyChannel");
		network.registerMessage(MessageBoosterRail.Handler.class, MessageBoosterRail.class, 0, Side.SERVER);
		network.registerMessage(MessagePSD.Handler.class, MessagePSD.class, 1, Side.SERVER);
		network.registerMessage(MessageRouteChanger.Handler.class, MessageRouteChanger.class, 2, Side.SERVER);
		// chunk loading
		ForgeChunkManager.setForcedChunkLoadingCallback(this, new EntityTrainBase(null));
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		// shapeless recipes
		Recipes.addShapelessRecipes();

		// shaped recipes
		Recipes.addShapedRecipes();

		// models
		if (event.getSide() == Side.CLIENT) {
			ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
			RegisterModels.registerModels(mesher);
		}

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

		// dispenser action
		BlockDispenser.dispenseBehaviorRegistry.putObject(itemkilltrain, new MTRDispenserBehavior.DispenseKillTrain());
		BlockDispenser.dispenseBehaviorRegistry.putObject(itemlightrail1,
				new MTRDispenserBehavior.DispenseLightRail1());
		BlockDispenser.dispenseBehaviorRegistry.putObject(itemsp1900, new MTRDispenserBehavior.DispenseSP1900());

		// event
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