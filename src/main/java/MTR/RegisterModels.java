package MTR;

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
import MTR.blocks.BlockMTRSignPost;
import MTR.blocks.BlockNextTrain;
import MTR.blocks.BlockPIDS1;
import MTR.blocks.BlockPSDDoor;
import MTR.blocks.BlockPSDDoorClosed;
import MTR.blocks.BlockPSDGlass;
import MTR.blocks.BlockPSDGlass2015;
import MTR.blocks.BlockPSDGlassEnd;
import MTR.blocks.BlockPSDGlassMiddle;
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
import MTR.blocks.BlockStationNameWall1;
import MTR.blocks.BlockTicketMachine;
import MTR.blocks.BlockTrainTimer;
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
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class RegisterModels extends MTR {

	public static void registerModels(ItemModelMesher mesher) {
		mesher.register(itemlightrail1, 0, new ModelResourceLocation("MTR:" + ItemLightRail1.getName(), "inventory"));
		mesher.register(itemlightrail1, 1, new ModelResourceLocation("MTR:" + ItemLightRail1.getName(), "inventory"));
		mesher.register(itemsp1900, 0, new ModelResourceLocation("MTR:" + ItemSP1900.getName(), "inventory"));
		mesher.register(itemsp1900, 1, new ModelResourceLocation("MTR:" + ItemSP1900.getName(), "inventory"));
		mesher.register(itemminecartspecial, 0,
				new ModelResourceLocation("MTR:" + ItemMinecartSpecial.getName(), "inventory"));
		mesher.register(itembrush, 0, new ModelResourceLocation("MTR:" + ItemBrush.getName(), "inventory"));
		mesher.register(itemescalator, 0, new ModelResourceLocation("MTR:" + ItemEscalator.getName1(), "inventory"));
		mesher.register(itemescalator, 1, new ModelResourceLocation("MTR:" + ItemEscalator.getName2(), "inventory"));
		mesher.register(itemescalator, 2, new ModelResourceLocation("MTR:" + ItemEscalator.getName3(), "inventory"));
		mesher.register(itemescalator, 3, new ModelResourceLocation("MTR:" + ItemEscalator.getName4(), "inventory"));
		mesher.register(itemescalator, 4, new ModelResourceLocation("MTR:" + ItemEscalator.getName5(), "inventory"));
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
		mesher.register(itemspawnplatform, 0,
				new ModelResourceLocation("MTR:" + ItemSpawnPlatform.getName(), "inventory"));
		mesher.register(itemspawnplatform, 1,
				new ModelResourceLocation("MTR:" + ItemSpawnPlatform.getName(), "inventory"));
		mesher.register(itemspawnplatform, 2,
				new ModelResourceLocation("MTR:" + ItemSpawnPlatform.getName(), "inventory"));
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
		mesher.register(Item.getItemFromBlock(blockescalatorstep), 0,
				new ModelResourceLocation("MTR:" + ((BlockEscalatorStep) blockescalatorstep).getName(), "inventory"));
		mesher.register(Item.getItemFromBlock(blockescalatorside), 0,
				new ModelResourceLocation("MTR:" + ((BlockEscalatorSide) blockescalatorside).getName(), "inventory"));
		mesher.register(Item.getItemFromBlock(blockescalatorsidelanding), 0, new ModelResourceLocation(
				"MTR:" + ((BlockEscalatorSideLanding) blockescalatorsidelanding).getName(), "inventory"));
		mesher.register(Item.getItemFromBlock(blockescalatorlanding), 0, new ModelResourceLocation(
				"MTR:" + ((BlockEscalatorLanding) blockescalatorlanding).getName(), "inventory"));
		mesher.register(Item.getItemFromBlock(blockescalatorsign), 0,
				new ModelResourceLocation("MTR:" + ((BlockEscalatorSign) blockescalatorsign).getName(), "inventory"));
		mesher.register(Item.getItemFromBlock(blockmtrmap), 0,
				new ModelResourceLocation("MTR:" + ((BlockMTRMap) blockmtrmap).getName(), "inventory"));
		mesher.register(Item.getItemFromBlock(blockmtrmapside), 0,
				new ModelResourceLocation("MTR:" + ((BlockMTRMapSide) blockmtrmapside).getName(), "inventory"));
		mesher.register(Item.getItemFromBlock(blockmtrsignpost), 0,
				new ModelResourceLocation("MTR:" + ((BlockMTRSignPost) blockmtrsignpost).getName(), "inventory"));
		mesher.register(Item.getItemFromBlock(blocknexttrain), 0,
				new ModelResourceLocation("MTR:" + ((BlockNextTrain) blocknexttrain).getName(), "inventory"));
		mesher.register(Item.getItemFromBlock(blockpids1), 0,
				new ModelResourceLocation("MTR:" + ((BlockPIDS1) blockpids1).getName(), "inventory"));
		mesher.register(Item.getItemFromBlock(blockplatform), 0,
				new ModelResourceLocation("MTR:" + ((BlockPlatform) blockplatform).getName(), "inventory"));

		mesher.register(Item.getItemFromBlock(blockapgdoorclosed), 0,
				new ModelResourceLocation("MTR:" + ((BlockAPGDoorClosed) blockapgdoorclosed).getName(), "inventory"));
		mesher.register(Item.getItemFromBlock(blockapgglassbottom), 0,
				new ModelResourceLocation("MTR:" + ((BlockAPGGlassBottom) blockapgglassbottom).getName(), "inventory"));
		mesher.register(Item.getItemFromBlock(blockapgglasstop), 0,
				new ModelResourceLocation("MTR:" + ((BlockAPGGlassTop) blockapgglasstop).getName(), "inventory"));
		mesher.register(Item.getItemFromBlock(blockapgglassmiddle), 0,
				new ModelResourceLocation("MTR:" + ((BlockAPGGlassMiddle) blockapgglassmiddle).getName(), "inventory"));
		mesher.register(Item.getItemFromBlock(blockpsddoor), 0,
				new ModelResourceLocation("MTR:" + ((BlockPSDDoor) blockpsddoor).getName(), "inventory"));
		mesher.register(Item.getItemFromBlock(blockpsddoorclosed), 0,
				new ModelResourceLocation("MTR:" + ((BlockPSDDoorClosed) blockpsddoorclosed).getName(), "inventory"));
		mesher.register(Item.getItemFromBlock(blockpsdglass), 0,
				new ModelResourceLocation("MTR:" + ((BlockPSDGlass) blockpsdglass).getName(), "inventory"));
		mesher.register(Item.getItemFromBlock(blockpsdglassend), 0,
				new ModelResourceLocation("MTR:" + ((BlockPSDGlassEnd) blockpsdglassend).getName(), "inventory"));
		mesher.register(Item.getItemFromBlock(blockpsdglass2015), 0,
				new ModelResourceLocation("MTR:" + ((BlockPSDGlass2015) blockpsdglass2015).getName(), "inventory"));
		mesher.register(Item.getItemFromBlock(blockpsdglassmiddle), 0,
				new ModelResourceLocation("MTR:" + ((BlockPSDGlassMiddle) blockpsdglassmiddle).getName(), "inventory"));
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
		mesher.register(Item.getItemFromBlock(blockraildetector), 0,
				new ModelResourceLocation("MTR:" + ((BlockRailDetector2) blockraildetector).getName(), "inventory"));
		mesher.register(Item.getItemFromBlock(blockrailstation), 0,
				new ModelResourceLocation("MTR:" + ((BlockRailStation) blockrailstation).getName(), "inventory"));
		mesher.register(Item.getItemFromBlock(blockrailcurved), 0,
				new ModelResourceLocation("MTR:" + ((BlockRailCurved) blockrailcurved).getName(), "inventory"));
		mesher.register(Item.getItemFromBlock(blockrubbishbin1), 0,
				new ModelResourceLocation("MTR:" + ((BlockRubbishBin1) blockrubbishbin1).getName(), "inventory"));
		mesher.register(Item.getItemFromBlock(blockstationnamewall1), 0, new ModelResourceLocation(
				"MTR:" + ((BlockStationNameWall1) blockstationnamewall1).getName(), "inventory"));
		mesher.register(Item.getItemFromBlock(blockticketmachine), 0,
				new ModelResourceLocation("MTR:" + ((BlockTicketMachine) blockticketmachine).getName(), "inventory"));
		mesher.register(Item.getItemFromBlock(blocktraintimer), 0,
				new ModelResourceLocation("MTR:" + ((BlockTrainTimer) blocktraintimer).getName(), "inventory"));
	}

	public static void registerItemVariants() {
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
				new ResourceLocation("MTR:ItemStationNamePole10"), new ResourceLocation("MTR:ItemStationNamePole11"),
				new ResourceLocation("MTR:ItemStationNamePole12"), new ResourceLocation("MTR:ItemStationNamePole13"),
				new ResourceLocation("MTR:ItemStationNamePole14"), new ResourceLocation("MTR:ItemStationNamePole15"),
				new ResourceLocation("MTR:ItemStationNamePole16"), new ResourceLocation("MTR:ItemStationNamePole17"),
				new ResourceLocation("MTR:ItemStationNamePole18"), new ResourceLocation("MTR:ItemStationNamePole19"),
				new ResourceLocation("MTR:ItemStationNamePole20"), new ResourceLocation("MTR:ItemStationNamePole21"),
				new ResourceLocation("MTR:ItemStationNamePole22"));
		ModelBakery.registerItemVariants(itemrail, new ResourceLocation("MTR:ItemRailNormal"),
				new ResourceLocation("MTR:ItemRailBooster"), new ResourceLocation("MTR:ItemRailSlopeUp"),
				new ResourceLocation("MTR:ItemRailSlopeDown"), new ResourceLocation("MTR:ItemRailDetector"),
				new ResourceLocation("MTR:ItemRailStation"));
		ModelBakery.registerItemVariants(itemescalator, new ResourceLocation("MTR:ItemEscalator"),
				new ResourceLocation("MTR:ItemEscalator5"), new ResourceLocation("MTR:ItemEscalator10"),
				new ResourceLocation("MTR:ItemEscalator20"), new ResourceLocation("MTR:ItemEscalator50"));
	}
}
