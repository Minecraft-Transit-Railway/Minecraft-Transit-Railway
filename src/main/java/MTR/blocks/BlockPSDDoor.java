package MTR.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumBlockRenderType;

public class BlockPSDDoor extends BlockDoorBase {

	private static final String name = "BlockPSDDoor";

	public BlockPSDDoor() {
		super(name);
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.INVISIBLE;
	}
}