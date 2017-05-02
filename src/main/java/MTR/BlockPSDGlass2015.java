package MTR;

import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockPSDGlass2015 extends BlockPSD {

	private static final String name = "BlockPSDGlass2015";

	protected BlockPSDGlass2015() {
		super();
		GameRegistry.registerBlock(this, name);
		setUnlocalizedName(name);
	}

	@Override
	public int damageDropped(IBlockState state) {
		return 3;
	}

	public String getName() {
		return name;
	}
}
