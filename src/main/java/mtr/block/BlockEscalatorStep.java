package mtr.block;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;

public class BlockEscalatorStep extends BlockEscalatorBase {

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING, DIRECTION, ORIENTATION, SIDE });
	}
}
