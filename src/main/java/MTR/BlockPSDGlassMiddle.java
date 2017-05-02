package MTR;

import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockPSDGlassMiddle extends BlockPSD {

	private static final String name = "BlockPSDGlassMiddle";

	protected BlockPSDGlassMiddle() {
		super();
		GameRegistry.registerBlock(this, name);
		setUnlocalizedName(name);
	}

	@Override
	public int damageDropped(IBlockState state) {
		return 1;
	}

	public String getName() {
		return name;
	}
}
