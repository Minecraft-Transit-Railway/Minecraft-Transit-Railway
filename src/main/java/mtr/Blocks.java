package mtr;

import mtr.block.*;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;

public class Blocks {

	public static final Block APG_DOOR = new BlockAPGDoor();
	public static final Block APG_GLASS = new BlockAPGGlass();
	public static final Block APG_GLASS_END = new BlockAPGGlassEnd();
	public static final Block CEILING = new BlockCeiling(FabricBlockSettings.of(Material.METAL, MaterialColor.QUARTZ).hardness(2).lightLevel(15));
	public static final Block ESCALATOR_SIDE = new BlockEscalatorSide();
	public static final Block ESCALATOR_STEP = new BlockEscalatorStep();
	public static final Block LOGO = new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.IRON).requiresTool().hardness(2).lightLevel(10));
	public static final Block PIDS_1 = new BlockPIDS1(FabricBlockSettings.of(Material.METAL, MaterialColor.IRON).hardness(2).lightLevel(5));
	public static final Block PLATFORM = new BlockPlatform(FabricBlockSettings.of(Material.METAL, MaterialColor.YELLOW).requiresTool().hardness(2));
	public static final Block PLATFORM_RAIL = new BlockPlatformRail(FabricBlockSettings.of(Material.METAL, MaterialColor.BROWN).requiresTool().hardness(2).nonOpaque());
	public static final Block PSD_DOOR = new BlockPSDDoor();
	public static final Block PSD_GLASS = new BlockPSDGlass();
	public static final Block PSD_GLASS_END = new BlockPSDGlassEnd();
	public static final Block PSD_TOP = new BlockPSDTop(FabricBlockSettings.of(Material.GLASS, MaterialColor.QUARTZ).requiresTool().hardness(2).lightLevel(15));
}
