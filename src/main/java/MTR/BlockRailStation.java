package MTR;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockRailStation extends BlockRailBase2 {

	private static final String name = "BlockRailStation";
	public static final PropertyBool POWERED = PropertyBool.create("powered");

	protected BlockRailStation() {
		super();
		GameRegistry.registerBlock(this, name);
		setDefaultState(blockState.getBaseState().withProperty(POWERED, false).withProperty(ROTATION, 0));
		setUnlocalizedName(name);
	}

	@Override
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
		worldIn.setBlockState(pos, state.withProperty(POWERED, worldIn.isBlockPowered(pos)));
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(ROTATION, meta % 4).withProperty(POWERED, meta >= 4);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return (Integer) state.getValue(ROTATION) + ((Boolean) state.getValue(POWERED) ? 4 : 0);
	}

	@Override
	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { POWERED, ROTATION });
	}

	@Override
	public int damageDropped(IBlockState state) {
		return 4;
	}

	public static String getName() {
		return name;
	}
}
