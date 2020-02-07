package mtr.block;

import java.util.Random;

import mtr.Items;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;

public class BlockAPGGlass extends BlockPSDAPGBase {

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Items.apg;
	}

	@Override
	public int damageDropped(IBlockState state) {
		return 1;
	}
}
