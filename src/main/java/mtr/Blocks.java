package mtr;

import mtr.block.*;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;

public interface Blocks {

	Block APG_DOOR = new BlockAPGDoor();
	Block APG_GLASS = new BlockAPGGlass();
	Block APG_GLASS_END = new BlockAPGGlassEnd();
	Block CEILING = new BlockCeiling(FabricBlockSettings.of(Material.METAL, MaterialColor.QUARTZ).hardness(2).luminance(15));
	Block ESCALATOR_SIDE = new BlockEscalatorSide();
	Block ESCALATOR_STEP = new BlockEscalatorStep();
	Block LOGO = new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.IRON).requiresTool().hardness(2).luminance(10));
	Block PIDS_1 = new BlockPIDS1(FabricBlockSettings.of(Material.METAL, MaterialColor.IRON).hardness(2).luminance(5));
	Block PLATFORM = new BlockPlatform(FabricBlockSettings.of(Material.METAL, MaterialColor.YELLOW).requiresTool().hardness(2));
	Block PLATFORM_RAIL = new BlockPlatformRail(FabricBlockSettings.of(Material.METAL, MaterialColor.BROWN).requiresTool().hardness(2).nonOpaque());
	Block PSD_DOOR = new BlockPSDDoor();
	Block PSD_GLASS = new BlockPSDGlass();
	Block PSD_GLASS_END = new BlockPSDGlassEnd();
	Block PSD_TOP = new BlockPSDTop();
	Block STATION_COLOR_CONCRETE = new BlockStationColor(FabricBlockSettings.of(Material.STONE, MaterialColor.IRON).hardness(2));
	Block STATION_COLOR_CONCRETE_POWDER = new BlockStationColor(FabricBlockSettings.of(Material.SOIL, MaterialColor.IRON).hardness(2));
	Block STATION_COLOR_DARK_PRISMARINE = new BlockStationColor(FabricBlockSettings.of(Material.STONE, MaterialColor.IRON).hardness(2));
	Block STATION_COLOR_DIORITE = new BlockStationColor(FabricBlockSettings.of(Material.STONE, MaterialColor.IRON).hardness(2));
	Block STATION_COLOR_IRON_BLOCK = new BlockStationColor(FabricBlockSettings.of(Material.METAL, MaterialColor.IRON).hardness(2));
	Block STATION_COLOR_METAL = new BlockStationColor(FabricBlockSettings.of(Material.METAL, MaterialColor.IRON).hardness(2));
	Block STATION_COLOR_PLANKS = new BlockStationColor(FabricBlockSettings.of(Material.WOOD, MaterialColor.IRON).hardness(2));
	Block STATION_COLOR_POLISHED_DIORITE = new BlockStationColor(FabricBlockSettings.of(Material.STONE, MaterialColor.IRON).hardness(2));
	Block STATION_COLOR_STAINED_GLASS = new BlockStationColor(FabricBlockSettings.of(Material.GLASS, MaterialColor.IRON).hardness(2).nonOpaque());
	Block STATION_COLOR_STONE = new BlockStationColor(FabricBlockSettings.of(Material.STONE, MaterialColor.IRON).hardness(2));
	Block STATION_COLOR_STONE_BRICKS = new BlockStationColor(FabricBlockSettings.of(Material.STONE, MaterialColor.IRON).hardness(2));
	Block STATION_COLOR_WOOL = new BlockStationColor(FabricBlockSettings.of(Material.WOOL, MaterialColor.IRON).hardness(2));
	Block STATION_NAME = new BlockStationName(FabricBlockSettings.of(Material.METAL, MaterialColor.IRON).requiresTool().hardness(2).nonOpaque());
	Block STATION_NAME_BLOCK = new BlockStationNameBlock(FabricBlockSettings.of(Material.METAL, MaterialColor.IRON).requiresTool().hardness(2).nonOpaque());
	Block STATION_POLE = new BlockStationPole(FabricBlockSettings.of(Material.STONE, MaterialColor.STONE).requiresTool().hardness(2).nonOpaque());
}
