package MTR;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockAPGDoor extends BlockDoorBase {

	private static final String name = "BlockAPGDoor";

	protected BlockAPGDoor() {
		super();
		GameRegistry.registerBlock(this, name);
		setUnlocalizedName(name);
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return MTR.itemapg;
	}

	@Override
	public Item getItem(World worldIn, BlockPos pos) {
		return MTR.itemapg;
	}

	public String getName() {
		return name;
	}
}
