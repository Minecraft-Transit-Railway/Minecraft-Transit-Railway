package MTR;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockPSDGlassVeryEnd extends BlockPSD {

	private static final String name = "BlockPSDGlassVeryEnd";

	protected BlockPSDGlassVeryEnd() {
		super();
		GameRegistry.registerBlock(this, name);
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		setUnlocalizedName(name);
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos) {
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	public int damageDropped(IBlockState state) {
		return 2;
	}

	public String getName() {
		return name;
	}
}
