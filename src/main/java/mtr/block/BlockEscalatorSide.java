package mtr.block;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;

public class BlockEscalatorSide extends BlockEscalatorBase {

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING, ORIENTATION, SIDE });
	}
}
