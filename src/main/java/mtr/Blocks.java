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
	Block STATION_NAME = new BlockStationName(FabricBlockSettings.of(Material.METAL, MaterialColor.IRON).requiresTool().hardness(2).nonOpaque());
}
