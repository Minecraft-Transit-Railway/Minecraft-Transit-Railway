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

import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
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
	public static final String VERSION = "1.8.9-2.0.1";

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
	public static Block blockmtrsign;
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
	public static CreativeTabs MTRtab = new CreativeTabs("MTR") {
		@Override
		@SideOnly(Side.CLIENT)
		public Item getTabIconItem() {
			return MTR.itemtrain;
		}
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
		itemstationname = new ItemStationName();
		itemstationnamepole = new ItemStationNamePole();
		itemrail = new ItemRail();
		// item variants
		if (event.getSide() == Side.CLIENT) {
			ModelBakery.registerItemVariants(itempsd, new ResourceLocation("MTR:ItemPSDDoor"),
					new ResourceLocation("MTR:ItemPSD"), new ResourceLocation("MTR:ItemPSDEnd"),
					new ResourceLocation("MTR:ItemPSD2015"));
			ModelBakery.registerItemVariants(itemapg, new ResourceLocation("MTR:ItemAPGDoor"),
					new ResourceLocation("MTR:ItemAPG"));
			ModelBakery.registerItemVariants(itemmtrpane, new ResourceLocation("MTR:ItemMTRPane1"),
					new ResourceLocation("MTR:ItemMTRPane2"), new ResourceLocation("MTR:ItemMTRPane3"),
					new ResourceLocation("MTR:ItemMTRPane4"), new ResourceLocation("MTR:ItemMTRPane5"),
					new ResourceLocation("MTR:ItemMTRPane6"), new ResourceLocation("MTR:ItemMTRPane7"),
					new ResourceLocation("MTR:ItemMTRPane8"));
			ModelBakery.registerItemVariants(itemstationname, new ResourceLocation("MTR:ItemStationName1"),
					new ResourceLocation("MTR:ItemStationName2"), new ResourceLocation("MTR:ItemStationName3"),
					new ResourceLocation("MTR:ItemStationName4"), new ResourceLocation("MTR:ItemStationName5"),
					new ResourceLocation("MTR:ItemStationName6"), new ResourceLocation("MTR:ItemStationName7"),
					new ResourceLocation("MTR:ItemStationName8"), new ResourceLocation("MTR:ItemStationName9"),
					new ResourceLocation("MTR:ItemStationName10"), new ResourceLocation("MTR:ItemStationName11"),
					new ResourceLocation("MTR:ItemStationName12"), new ResourceLocation("MTR:ItemStationName13"),
					new ResourceLocation("MTR:ItemStationName14"), new ResourceLocation("MTR:ItemStationName15"),
					new ResourceLocation("MTR:ItemStationName16"), new ResourceLocation("MTR:ItemStationName17"),
					new ResourceLocation("MTR:ItemStationName18"), new ResourceLocation("MTR:ItemStationName19"),
					new ResourceLocation("MTR:ItemStationName20"), new ResourceLocation("MTR:ItemStationName21"),
					new ResourceLocation("MTR:ItemStationName22"));
			ModelBakery.registerItemVariants(itemstationnamepole, new ResourceLocation("MTR:ItemStationNamePole1"),
					new ResourceLocation("MTR:ItemStationNamePole2"), new ResourceLocation("MTR:ItemStationNamePole3"),
					new ResourceLocation("MTR:ItemStationNamePole4"), new ResourceLocation("MTR:ItemStationNamePole5"),
					new ResourceLocation("MTR:ItemStationNamePole6"), new ResourceLocation("MTR:ItemStationNamePole7"),
					new ResourceLocation("MTR:ItemStationNamePole8"), new ResourceLocation("MTR:ItemStationNamePole9"),
					new ResourceLocation("MTR:ItemStationNamePole10"),
					new ResourceLocation("MTR:ItemStationNamePole11"),
					new ResourceLocation("MTR:ItemStationNamePole12"),
					new ResourceLocation("MTR:ItemStationNamePole13"),
					new ResourceLocation("MTR:ItemStationNamePole14"),
					new ResourceLocation("MTR:ItemStationNamePole15"),
					new ResourceLocation("MTR:ItemStationNamePole16"),
					new ResourceLocation("MTR:ItemStationNamePole17"),
					new ResourceLocation("MTR:ItemStationNamePole18"),
					new ResourceLocation("MTR:ItemStationNamePole19"),
					new ResourceLocation("MTR:ItemStationNamePole20"),
					new ResourceLocation("MTR:ItemStationNamePole21"),
					new ResourceLocation("MTR:ItemStationNamePole22"));
			ModelBakery.registerItemVariants(itemrail, new ResourceLocation("MTR:ItemRailNormal"),
					new ResourceLocation("MTR:ItemRailBooster"), new ResourceLocation("MTR:ItemRailSlopeUp"),
					new ResourceLocation("MTR:ItemRailSlopeDown"), new ResourceLocation("MTR:ItemRailDetector"),
					new ResourceLocation("MTR:ItemRailStation"));

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
		blockmtrsign = new BlockMTRSign();
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
		GameRegistry.registerTileEntity(TileEntityClockEntity.class, "Clock");
		GameRegistry.registerTileEntity(TileEntityMTRSignEntity.class, "MTRSign");
		GameRegistry.registerTileEntity(TileEntityAPGGlassEntity.class, "APGGlass");
		GameRegistry.registerTileEntity(TileEntityDoorEntity.class, "Door");
		GameRegistry.registerTileEntity(TileEntityNextTrainEntity.class, "NextTrain");
		GameRegistry.registerTileEntity(TileEntityPIDS1Entity.class, "PIDS1");
		GameRegistry.registerTileEntity(TileEntityPSDTopEntity.class, "PSDTop");
		GameRegistry.registerTileEntity(TileEntityRailEntity.class, "Rail");
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
		GameRegistry.addShapelessRecipe(new ItemStack(MTR.itemtrain), new Object[] { Items.compass, Items.minecart });
		GameRegistry.addShapelessRecipe(new ItemStack(MTR.blocklogo),
				new Object[] { new ItemStack(Items.dye, 1, 1), Blocks.stone });

		GameRegistry.addShapelessRecipe(new ItemStack(MTR.itempsd, 8),
				new Object[] { Blocks.glass, Items.redstone, Items.oak_door });
		GameRegistry.addShapelessRecipe(new ItemStack(MTR.itempsd, 1, 1),
				new Object[] { new ItemStack(MTR.itempsd, 1, 0) });
		GameRegistry.addShapelessRecipe(new ItemStack(MTR.itempsd, 1, 2),
				new Object[] { new ItemStack(MTR.itempsd, 1, 1) });
		GameRegistry.addShapelessRecipe(new ItemStack(MTR.itempsd, 1, 0),
				new Object[] { new ItemStack(MTR.itempsd, 1, 2) });

		GameRegistry.addShapelessRecipe(new ItemStack(MTR.itemapg, 8),
				new Object[] { Blocks.glass, Items.redstone, Blocks.oak_fence_gate });
		GameRegistry.addShapelessRecipe(new ItemStack(MTR.itemapg, 1, 1),
				new Object[] { new ItemStack(MTR.itemapg, 1, 0) });
		GameRegistry.addShapelessRecipe(new ItemStack(MTR.itemapg, 1, 0),
				new Object[] { new ItemStack(MTR.itemapg, 1, 1) });

		GameRegistry.addShapelessRecipe(new ItemStack(MTR.itemmtrpane, 1, 1),
				new Object[] { new ItemStack(MTR.itemmtrpane, 1, 0) });
		GameRegistry.addShapelessRecipe(new ItemStack(MTR.itemmtrpane, 1, 2),
				new Object[] { new ItemStack(MTR.itemmtrpane, 1, 1) });
		GameRegistry.addShapelessRecipe(new ItemStack(MTR.itemmtrpane, 1, 3),
				new Object[] { new ItemStack(MTR.itemmtrpane, 1, 2) });
		GameRegistry.addShapelessRecipe(new ItemStack(MTR.itemmtrpane, 1, 4),
				new Object[] { new ItemStack(MTR.itemmtrpane, 1, 3) });
		GameRegistry.addShapelessRecipe(new ItemStack(MTR.itemmtrpane, 1, 5),
				new Object[] { new ItemStack(MTR.itemmtrpane, 1, 4) });
		GameRegistry.addShapelessRecipe(new ItemStack(MTR.itemmtrpane, 1, 6),
				new Object[] { new ItemStack(MTR.itemmtrpane, 1, 5) });
		GameRegistry.addShapelessRecipe(new ItemStack(MTR.itemmtrpane, 1, 7),
				new Object[] { new ItemStack(MTR.itemmtrpane, 1, 6) });
		GameRegistry.addShapelessRecipe(new ItemStack(MTR.itemmtrpane, 1, 0),
				new Object[] { new ItemStack(MTR.itemmtrpane, 1, 7) });

		GameRegistry.addShapelessRecipe(new ItemStack(MTR.itemminecartspecial),
				new Object[] { new ItemStack(Items.minecart), new ItemStack(Items.iron_pickaxe) });

		GameRegistry.addShapelessRecipe(new ItemStack(MTR.itemlightrail1, 1, 1),
				new Object[] { new ItemStack(MTR.itemlightrail1, 1, 0) });
		GameRegistry.addShapelessRecipe(new ItemStack(MTR.itemlightrail1, 1, 0),
				new Object[] { new ItemStack(MTR.itemlightrail1, 1, 1) });

		GameRegistry.addShapelessRecipe(new ItemStack(MTR.itemkilltrain),
				new Object[] { new ItemStack(Items.minecart), new ItemStack(Items.lava_bucket) });

		GameRegistry.addShapelessRecipe(new ItemStack(MTR.blockadside, 4), new Object[] { Items.sign });
		GameRegistry.addShapelessRecipe(new ItemStack(MTR.blockpids1, 2),
				new Object[] { Items.sign, Blocks.redstone_block, Items.iron_ingot });
		GameRegistry.addShapelessRecipe(new ItemStack(MTR.blockplatform), new Object[] { Blocks.stonebrick });
		GameRegistry.addShapelessRecipe(new ItemStack(MTR.blockroutechanger, 2),
				new Object[] { Blocks.stone, Blocks.redstone_block });
		GameRegistry.addShapelessRecipe(new ItemStack(MTR.blockstationnamewall1, 8),
				new Object[] { Items.sign, Items.sign });
		GameRegistry.addShapelessRecipe(new ItemStack(MTR.blockceiling, 2),
				new Object[] { Blocks.stone_slab, Blocks.torch });

		GameRegistry.addShapelessRecipe(new ItemStack(MTR.blockmtrmap),
				new Object[] { new ItemStack(MTR.blockmtrmapside) });
		GameRegistry.addShapelessRecipe(new ItemStack(MTR.blockmtrmapside),
				new Object[] { new ItemStack(MTR.blockmtrmap) });

		// shaped recipes
		GameRegistry.addShapedRecipe(new ItemStack(MTR.blocklogo, 8), "xxx", "xyx", "xxx", 'x', Blocks.stone, 'y',
				new ItemStack(Items.dye, 1, 1));
		GameRegistry.addShapedRecipe(new ItemStack(MTR.blockclock, 16), " x ", "xyx", " x ", 'x', Items.iron_ingot, 'y',
				Items.clock);
		GameRegistry.addShapedRecipe(new ItemStack(MTR.blockmtrmap, 16), " x ", "xyx", " x ", 'x', Items.iron_ingot,
				'y', Items.map);
		GameRegistry.addShapedRecipe(new ItemStack(MTR.blockrubbishbin1, 8), "x x", "x x", "xxx", 'x',
				Items.iron_ingot);
		GameRegistry.addShapedRecipe(new ItemStack(MTR.blockticketmachine, 4), "xxx", "xyx", "xxx", 'x',
				Items.iron_ingot, 'y', Items.map);
		GameRegistry.addShapedRecipe(new ItemStack(MTR.blocktraintimer, 2), "   ", "zyz", "xxx", 'x', Blocks.stone, 'y',
				Items.compass, 'z', Blocks.redstone_torch);

		GameRegistry.addShapedRecipe(new ItemStack(MTR.itemrail, 1, 0), " x ", " x ", " x ", 'x', Blocks.rail);
		GameRegistry.addShapedRecipe(new ItemStack(MTR.itemrail, 1, 1), " x ", " x ", " x ", 'x', Blocks.golden_rail);
		GameRegistry.addShapedRecipe(new ItemStack(MTR.itemrail, 1, 2), "  x", " xx", "xxx", 'x', Blocks.rail);
		GameRegistry.addShapedRecipe(new ItemStack(MTR.itemrail, 1, 3), "xxx", "xx ", "x  ", 'x', Blocks.rail);
		GameRegistry.addShapedRecipe(new ItemStack(MTR.itemrail, 1, 4), " x ", " x ", " x ", 'x', Blocks.detector_rail);
		GameRegistry.addShapedRecipe(new ItemStack(MTR.itemrail, 1, 5), " x ", "yxy", " x ", 'x', Blocks.rail, 'y',
				Items.redstone);

		GameRegistry.addShapedRecipe(new ItemStack(MTR.itemlightrail1), "x  ", "   ", "   ", 'x',
				new ItemStack(Items.minecart));
		GameRegistry.addShapedRecipe(new ItemStack(MTR.itemsp1900), " x ", "   ", "   ", 'x',
				new ItemStack(Items.minecart));

		GameRegistry.addShapedRecipe(new ItemStack(MTR.itembrush), "x", "y", 'x', Blocks.wool, 'y', Items.stick);

		GameRegistry.addShapedRecipe(new ItemStack(MTR.itemmtrpane, 1, 0), "x  ", "   ", "   ", 'x',
				new ItemStack(Blocks.glass_pane));
		GameRegistry.addShapedRecipe(new ItemStack(MTR.itemmtrpane, 1, 1), " x ", "   ", "   ", 'x',
				new ItemStack(Blocks.glass_pane));
		GameRegistry.addShapedRecipe(new ItemStack(MTR.itemmtrpane, 1, 2), "  x", "   ", "   ", 'x',
				new ItemStack(Blocks.glass_pane));
		GameRegistry.addShapedRecipe(new ItemStack(MTR.itemmtrpane, 1, 3), "   ", "x  ", "   ", 'x',
				new ItemStack(Blocks.glass_pane));
		GameRegistry.addShapedRecipe(new ItemStack(MTR.itemmtrpane, 1, 4), "   ", " x ", "   ", 'x',
				new ItemStack(Blocks.glass_pane));
		GameRegistry.addShapedRecipe(new ItemStack(MTR.itemmtrpane, 1, 5), "   ", "  x", "   ", 'x',
				new ItemStack(Blocks.glass_pane));
		GameRegistry.addShapedRecipe(new ItemStack(MTR.itemmtrpane, 1, 6), "   ", "   ", "x  ", 'x',
				new ItemStack(Blocks.glass_pane));
		GameRegistry.addShapedRecipe(new ItemStack(MTR.itemmtrpane, 1, 7), "   ", "   ", " x ", 'x',
				new ItemStack(Blocks.glass_pane));

		// models
		if (event.getSide() == Side.CLIENT) {
			ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
			mesher.register(itemlightrail1, 0,
					new ModelResourceLocation("MTR:" + ItemLightRail1.getName(), "inventory"));
			mesher.register(itemlightrail1, 1,
					new ModelResourceLocation("MTR:" + ItemLightRail1.getName(), "inventory"));
			mesher.register(itemsp1900, 0, new ModelResourceLocation("MTR:" + ItemSP1900.getName(), "inventory"));
			mesher.register(itemsp1900, 1, new ModelResourceLocation("MTR:" + ItemSP1900.getName(), "inventory"));
			mesher.register(itemminecartspecial, 0,
					new ModelResourceLocation("MTR:" + ItemMinecartSpecial.getName(), "inventory"));
			mesher.register(itembrush, 0, new ModelResourceLocation("MTR:" + ItemBrush.getName(), "inventory"));
			mesher.register(itemescalator, 0,
					new ModelResourceLocation("MTR:" + ItemEscalator.getName1(), "inventory"));
			mesher.register(itemescalator, 1,
					new ModelResourceLocation("MTR:" + ItemEscalator.getName5(), "inventory"));
			mesher.register(itemescalator, 2,
					new ModelResourceLocation("MTR:" + ItemEscalator.getName10(), "inventory"));
			mesher.register(itemescalator, 3,
					new ModelResourceLocation("MTR:" + ItemEscalator.getName20(), "inventory"));
			mesher.register(itemescalator, 4,
					new ModelResourceLocation("MTR:" + ItemEscalator.getName50(), "inventory"));
			mesher.register(itemkilltrain, 0, new ModelResourceLocation("MTR:" + ItemKillTrain.getName(), "inventory"));
			mesher.register(itemtrain, 0, new ModelResourceLocation("MTR:" + ItemTrain.getName(), "inventory"));
			mesher.register(itemapg, 0, new ModelResourceLocation("MTR:" + ItemAPG.getName2(), "inventory"));
			mesher.register(itemapg, 1, new ModelResourceLocation("MTR:" + ItemAPG.getName1(), "inventory"));
			mesher.register(itempsd, 0, new ModelResourceLocation("MTR:" + ItemPSD.getName1(), "inventory"));
			mesher.register(itempsd, 1, new ModelResourceLocation("MTR:" + ItemPSD.getName2(), "inventory"));
			mesher.register(itempsd, 2, new ModelResourceLocation("MTR:" + ItemPSD.getName3(), "inventory"));
			mesher.register(itempsd, 3, new ModelResourceLocation("MTR:" + ItemPSD.getName4(), "inventory"));
			mesher.register(itemrail, 0, new ModelResourceLocation("MTR:" + ItemRail.getName1(), "inventory"));
			mesher.register(itemrail, 1, new ModelResourceLocation("MTR:" + ItemRail.getName2(), "inventory"));
			mesher.register(itemrail, 2, new ModelResourceLocation("MTR:" + ItemRail.getName3(), "inventory"));
			mesher.register(itemrail, 3, new ModelResourceLocation("MTR:" + ItemRail.getName4(), "inventory"));
			mesher.register(itemrail, 4, new ModelResourceLocation("MTR:" + ItemRail.getName5(), "inventory"));
			mesher.register(itemrail, 5, new ModelResourceLocation("MTR:" + ItemRail.getName6(), "inventory"));
			for (int a = 0; a < 8; a++)
				mesher.register(itemmtrpane, a,
						new ModelResourceLocation("MTR:" + ItemMTRPane.getName(a + 1), "inventory"));
			for (int a = 0; a < 22; a++) {
				mesher.register(itemstationname, a,
						new ModelResourceLocation("MTR:" + ItemStationName.getName(a + 1), "inventory"));
				mesher.register(itemstationnamepole, a,
						new ModelResourceLocation("MTR:" + ItemStationNamePole.getName(a + 1), "inventory"));
			}

			mesher.register(Item.getItemFromBlock(blockstation), 0,
					new ModelResourceLocation("MTR:" + BlockStation.getName(), "inventory"));
			mesher.register(Item.getItemFromBlock(blocklogo), 0,
					new ModelResourceLocation("MTR:" + BlockLogo.getName(), "inventory"));
			mesher.register(Item.getItemFromBlock(blockroutechanger), 0,
					new ModelResourceLocation("MTR:" + BlockRouteChanger.getName(), "inventory"));
			mesher.register(Item.getItemFromBlock(blockadside), 0,
					new ModelResourceLocation("MTR:" + ((BlockAdSide) blockadside).getName(), "inventory"));
			mesher.register(Item.getItemFromBlock(blockceiling), 0,
					new ModelResourceLocation("MTR:" + ((BlockCeiling) blockceiling).getName(), "inventory"));
			mesher.register(Item.getItemFromBlock(blockclock), 0,
					new ModelResourceLocation("MTR:" + ((BlockClock) blockclock).getName(), "inventory"));
			mesher.register(Item.getItemFromBlock(blockescalatorstep), 0, new ModelResourceLocation(
					"MTR:" + ((BlockEscalatorStep) blockescalatorstep).getName(), "inventory"));
			mesher.register(Item.getItemFromBlock(blockescalatorside), 0, new ModelResourceLocation(
					"MTR:" + ((BlockEscalatorSide) blockescalatorside).getName(), "inventory"));
			mesher.register(Item.getItemFromBlock(blockescalatorsidelanding), 0, new ModelResourceLocation(
					"MTR:" + ((BlockEscalatorSideLanding) blockescalatorsidelanding).getName(), "inventory"));
			mesher.register(Item.getItemFromBlock(blockescalatorlanding), 0, new ModelResourceLocation(
					"MTR:" + ((BlockEscalatorLanding) blockescalatorlanding).getName(), "inventory"));
			mesher.register(Item.getItemFromBlock(blockescalatorsign), 0, new ModelResourceLocation(
					"MTR:" + ((BlockEscalatorSign) blockescalatorsign).getName(), "inventory"));
			mesher.register(Item.getItemFromBlock(blockmtrmap), 0,
					new ModelResourceLocation("MTR:" + ((BlockMTRMap) blockmtrmap).getName(), "inventory"));
			mesher.register(Item.getItemFromBlock(blockmtrmapside), 0,
					new ModelResourceLocation("MTR:" + ((BlockMTRMapSide) blockmtrmapside).getName(), "inventory"));
			mesher.register(Item.getItemFromBlock(blockmtrsign), 0,
					new ModelResourceLocation("MTR:" + ((BlockMTRSign) blockmtrsign).getName(), "inventory"));
			mesher.register(Item.getItemFromBlock(blocknexttrain), 0,
					new ModelResourceLocation("MTR:" + ((BlockNextTrain) blocknexttrain).getName(), "inventory"));
			mesher.register(Item.getItemFromBlock(blockpids1), 0,
					new ModelResourceLocation("MTR:" + ((BlockPIDS1) blockpids1).getName(), "inventory"));
			mesher.register(Item.getItemFromBlock(blockplatform), 0,
					new ModelResourceLocation("MTR:" + ((BlockPlatform) blockplatform).getName(), "inventory"));

			mesher.register(Item.getItemFromBlock(blockapgdoorclosed), 0, new ModelResourceLocation(
					"MTR:" + ((BlockAPGDoorClosed) blockapgdoorclosed).getName(), "inventory"));
			mesher.register(Item.getItemFromBlock(blockapgglassbottom), 0, new ModelResourceLocation(
					"MTR:" + ((BlockAPGGlassBottom) blockapgglassbottom).getName(), "inventory"));
			mesher.register(Item.getItemFromBlock(blockapgglasstop), 0,
					new ModelResourceLocation("MTR:" + ((BlockAPGGlassTop) blockapgglasstop).getName(), "inventory"));
			mesher.register(Item.getItemFromBlock(blockapgglassmiddle), 0, new ModelResourceLocation(
					"MTR:" + ((BlockAPGGlassMiddle) blockapgglassmiddle).getName(), "inventory"));
			mesher.register(Item.getItemFromBlock(blockpsddoor), 0,
					new ModelResourceLocation("MTR:" + ((BlockPSDDoor) blockpsddoor).getName(), "inventory"));
			mesher.register(Item.getItemFromBlock(blockpsddoorclosed), 0, new ModelResourceLocation(
					"MTR:" + ((BlockPSDDoorClosed) blockpsddoorclosed).getName(), "inventory"));
			mesher.register(Item.getItemFromBlock(blockpsdglass), 0,
					new ModelResourceLocation("MTR:" + ((BlockPSDGlass) blockpsdglass).getName(), "inventory"));
			mesher.register(Item.getItemFromBlock(blockpsdglassend), 0,
					new ModelResourceLocation("MTR:" + ((BlockPSDGlassEnd) blockpsdglassend).getName(), "inventory"));
			mesher.register(Item.getItemFromBlock(blockpsdglass2015), 0,
					new ModelResourceLocation("MTR:" + ((BlockPSDGlass2015) blockpsdglass2015).getName(), "inventory"));
			mesher.register(Item.getItemFromBlock(blockpsdglassmiddle), 0, new ModelResourceLocation(
					"MTR:" + ((BlockPSDGlassMiddle) blockpsdglassmiddle).getName(), "inventory"));
			mesher.register(Item.getItemFromBlock(blockpsdtop), 0,
					new ModelResourceLocation("MTR:" + ((BlockPSDTop) blockpsdtop).getName(), "inventory"));

			mesher.register(Item.getItemFromBlock(blockraildummy), 0,
					new ModelResourceLocation("MTR:" + ((BlockRailDummy) blockraildummy).getName(), "inventory"));
			mesher.register(Item.getItemFromBlock(blockrailstraight), 0,
					new ModelResourceLocation("MTR:" + ((BlockRailStraight) blockrailstraight).getName(), "inventory"));
			mesher.register(Item.getItemFromBlock(blockrailbooster), 0,
					new ModelResourceLocation("MTR:" + ((BlockRailBooster) blockrailbooster).getName(), "inventory"));
			mesher.register(Item.getItemFromBlock(blockrailslope1), 0,
					new ModelResourceLocation("MTR:" + ((BlockRailSlope1) blockrailslope1).getName(), "inventory"));
			mesher.register(Item.getItemFromBlock(blockrailslope2), 0,
					new ModelResourceLocation("MTR:" + ((BlockRailSlope2) blockrailslope2).getName(), "inventory"));
			mesher.register(Item.getItemFromBlock(blockraildetector), 0, new ModelResourceLocation(
					"MTR:" + ((BlockRailDetector2) blockraildetector).getName(), "inventory"));
			mesher.register(Item.getItemFromBlock(blockrailstation), 0,
					new ModelResourceLocation("MTR:" + ((BlockRailStation) blockrailstation).getName(), "inventory"));
			mesher.register(Item.getItemFromBlock(blockrailcurved), 0,
					new ModelResourceLocation("MTR:" + ((BlockRailCurved) blockrailcurved).getName(), "inventory"));
			mesher.register(Item.getItemFromBlock(blockrubbishbin1), 0,
					new ModelResourceLocation("MTR:" + ((BlockRubbishBin1) blockrubbishbin1).getName(), "inventory"));
			mesher.register(Item.getItemFromBlock(blockstationnamewall1), 0, new ModelResourceLocation(
					"MTR:" + ((BlockStationNameWall1) blockstationnamewall1).getName(), "inventory"));
			mesher.register(Item.getItemFromBlock(blockticketmachine), 0, new ModelResourceLocation(
					"MTR:" + ((BlockTicketMachine) blockticketmachine).getName(), "inventory"));
			mesher.register(Item.getItemFromBlock(blocktraintimer), 0,
					new ModelResourceLocation("MTR:" + ((BlockTrainTimer) blocktraintimer).getName(), "inventory"));
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