package mtr;

import mtr.block.*;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;

public interface Blocks {

	Block APG_DOOR = new BlockAPGDoor();
	Block APG_GLASS = new BlockAPGGlass();
	Block APG_GLASS_END = new BlockAPGGlassEnd();
	Block CEILING = new BlockCeiling(FabricBlockSettings.of(Material.METAL, MaterialColor.QUARTZ).requiresTool().hardness(2).luminance(15));
	Block CLOCK = new BlockClock(FabricBlockSettings.of(Material.METAL, MaterialColor.QUARTZ).requiresTool().hardness(2).luminance(5));
	Block ESCALATOR_SIDE = new BlockEscalatorSide();
	Block ESCALATOR_STEP = new BlockEscalatorStep();
	Block LOGO = new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.IRON).requiresTool().hardness(2).luminance(10));
	Block PIDS_1 = new BlockPIDS1(FabricBlockSettings.of(Material.METAL, MaterialColor.IRON).requiresTool().hardness(2).luminance(5));
	Block PLATFORM = new BlockPlatform(FabricBlockSettings.of(Material.METAL, MaterialColor.YELLOW).requiresTool().hardness(2));
	Block PLATFORM_RAIL = new BlockPlatformRail(FabricBlockSettings.of(Material.METAL, MaterialColor.BROWN).hardness(2).nonOpaque());
	Block PSD_DOOR = new BlockPSDDoor();
	Block PSD_GLASS = new BlockPSDGlass();
	Block PSD_GLASS_END = new BlockPSDGlassEnd();
	Block PSD_TOP = new BlockPSDTop();
	Block STATION_COLOR_ANDESITE = new BlockStationColor(AbstractBlock.Settings.copy(net.minecraft.block.Blocks.ANDESITE));
	Block STATION_COLOR_BEDROCK = new BlockStationColor(AbstractBlock.Settings.copy(net.minecraft.block.Blocks.STONE));
	Block STATION_COLOR_BIRCH_WOOD = new BlockStationColor(AbstractBlock.Settings.copy(net.minecraft.block.Blocks.BIRCH_WOOD));
	Block STATION_COLOR_CHISELED_STONE_BRICKS = new BlockStationColor(AbstractBlock.Settings.copy(net.minecraft.block.Blocks.CHISELED_STONE_BRICKS));
	Block STATION_COLOR_CLAY = new BlockStationColor(AbstractBlock.Settings.copy(net.minecraft.block.Blocks.CLAY));
	Block STATION_COLOR_COAL_ORE = new BlockStationColor(AbstractBlock.Settings.copy(net.minecraft.block.Blocks.COAL_ORE));
	Block STATION_COLOR_COBBLESTONE = new BlockStationColor(AbstractBlock.Settings.copy(net.minecraft.block.Blocks.COBBLESTONE));
	Block STATION_COLOR_CONCRETE = new BlockStationColor(AbstractBlock.Settings.copy(net.minecraft.block.Blocks.WHITE_CONCRETE));
	Block STATION_COLOR_CONCRETE_POWDER = new BlockStationColor(AbstractBlock.Settings.copy(net.minecraft.block.Blocks.WHITE_CONCRETE_POWDER));
	Block STATION_COLOR_CRACKED_STONE_BRICKS = new BlockStationColor(AbstractBlock.Settings.copy(net.minecraft.block.Blocks.CRACKED_STONE_BRICKS));
	Block STATION_COLOR_DARK_PRISMARINE = new BlockStationColor(AbstractBlock.Settings.copy(net.minecraft.block.Blocks.DARK_PRISMARINE));
	Block STATION_COLOR_DIORITE = new BlockStationColor(AbstractBlock.Settings.copy(net.minecraft.block.Blocks.DIORITE));
	Block STATION_COLOR_GRAVEL = new BlockStationColor(AbstractBlock.Settings.copy(net.minecraft.block.Blocks.GRAVEL));
	Block STATION_COLOR_IRON_BLOCK = new BlockStationColor(AbstractBlock.Settings.copy(net.minecraft.block.Blocks.IRON_BLOCK));
	Block STATION_COLOR_METAL = new BlockStationColor(AbstractBlock.Settings.copy(LOGO));
	Block STATION_COLOR_PLANKS = new BlockStationColor(AbstractBlock.Settings.copy(net.minecraft.block.Blocks.OAK_PLANKS));
	Block STATION_COLOR_POLISHED_ANDESITE = new BlockStationColor(AbstractBlock.Settings.copy(net.minecraft.block.Blocks.POLISHED_ANDESITE));
	Block STATION_COLOR_POLISHED_DIORITE = new BlockStationColor(AbstractBlock.Settings.copy(net.minecraft.block.Blocks.POLISHED_DIORITE));
	Block STATION_COLOR_SMOOTH_STONE = new BlockStationColor(AbstractBlock.Settings.copy(net.minecraft.block.Blocks.SMOOTH_STONE));
	Block STATION_COLOR_STAINED_GLASS = new BlockStationColor(AbstractBlock.Settings.copy(net.minecraft.block.Blocks.WHITE_STAINED_GLASS));
	Block STATION_COLOR_STONE = new BlockStationColor(AbstractBlock.Settings.copy(net.minecraft.block.Blocks.STONE));
	Block STATION_COLOR_STONE_BRICKS = new BlockStationColor(AbstractBlock.Settings.copy(net.minecraft.block.Blocks.STONE_BRICKS));
	Block STATION_COLOR_WOOL = new BlockStationColor(AbstractBlock.Settings.copy(net.minecraft.block.Blocks.WHITE_WOOL));
	Block STATION_NAME_TALL_BLOCK = new BlockStationNameTallBlock();
	Block STATION_NAME_TALL_WALL = new BlockStationNameTallWall();
	Block STATION_NAME_WALL = new BlockStationNameWall(FabricBlockSettings.of(Material.METAL, MaterialColor.IRON).requiresTool().hardness(2).nonOpaque());
	Block STATION_POLE = new BlockStationPole(FabricBlockSettings.of(Material.STONE, MaterialColor.STONE).requiresTool().hardness(2).nonOpaque());
}
