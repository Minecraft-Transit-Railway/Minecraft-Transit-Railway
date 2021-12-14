package mtr.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;

public class BlockTrainCargo extends Block {

	public final boolean isLoader;

	public BlockTrainCargo(boolean isLoader) {
		super(FabricBlockSettings.of(Material.METAL, MapColor.IRON_GRAY).requiresTool().hardness(2));
		this.isLoader = isLoader;
	}
}
