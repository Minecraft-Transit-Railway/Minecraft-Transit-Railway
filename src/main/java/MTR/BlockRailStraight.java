package MTR;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockRailStraight extends BlockRailBase2 {

	private static final String name = "BlockRailStraight";

	protected BlockRailStraight() {
		super();
		GameRegistry.registerBlock(this, name);
		setUnlocalizedName(name);
	}

	public void destroy1(World worldIn, BlockPos pos, int a, int rotation) {
		if (a > 1) {
			BlockPos pos2 = pos;
			switch (rotation) {
			case 0:
				pos2 = pos2.north();
				break;
			case 1:
				pos2 = pos2.north().east();
				break;
			case 2:
				pos2 = pos2.east();
				break;
			case 3:
				pos2 = pos2.south().east();
				break;
			}
			IBlockState state2 = worldIn.getBlockState(pos2);
			if (state2.getBlock() instanceof BlockRailStraight && (Integer) state2.getValue(ROTATION) == rotation)
				destroy1(worldIn, pos2, a - 1, rotation);
		}
		worldIn.setBlockToAir(pos);
	}

	public void destroy2(World worldIn, BlockPos pos, int a, int rotation) {
		if (a > 1) {
			BlockPos pos2 = pos;
			switch (rotation) {
			case 0:
				pos2 = pos2.south();
				break;
			case 1:
				pos2 = pos2.south().west();
				break;
			case 2:
				pos2 = pos2.west();
				break;
			case 3:
				pos2 = pos2.north().west();
				break;
			}
			IBlockState state2 = worldIn.getBlockState(pos2);
			if (state2.getBlock() instanceof BlockRailStraight && (Integer) state2.getValue(ROTATION) == rotation)
				destroy2(worldIn, pos2, a - 1, rotation);
		}
		worldIn.setBlockToAir(pos);
	}

	public static String getName() {
		return name;
	}
}