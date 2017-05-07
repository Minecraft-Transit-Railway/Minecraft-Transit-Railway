package MTR.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockStationNameA extends BlockStationNameBase {

	private static final String name = "BlockStationNameCKT";

	public BlockStationNameA() {
		super();
		GameRegistry.registerBlock(this, name);
		setUnlocalizedName(name);
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos) {
		if ((Boolean) access.getBlockState(pos).getValue(POLE))
			setBlockBounds(0.375F, 0.0F, 0.375F, 0.625F, 1.0F, 0.625F);
		else {
			EnumFacing var3 = (EnumFacing) access.getBlockState(pos).getValue(FACING);
			if ((Boolean) access.getBlockState(pos).getValue(SIDE))
				switch (var3) {
				case NORTH:
					setBlockBounds(0.0F, 0.0F, 0.03125F, 0.0625F, 1.0F, 0.96875F);
					break;
				case SOUTH:
					setBlockBounds(0.9375F, 0.0F, 0.03125F, 1.0F, 1.0F, 0.96875F);
					break;
				case EAST:
					setBlockBounds(0.03125F, 0.0F, 0.0F, 0.96875F, 1.0F, 0.0625F);
					break;
				case WEST:
					setBlockBounds(0.03125F, 0.0F, 0.9375F, 0.96875F, 1.0F, 1.0F);
					break;
				default:
				}
			else if (var3.getAxis() == EnumFacing.Axis.X)
				setBlockBounds(0.03125F, 0.0F, 0.3125F, 0.96875F, 1.0F, 0.6875F);
			else
				setBlockBounds(0.3125F, 0.0F, 0.03125F, 0.6875F, 1.0F, 0.96875F);
		}
	}

	@Override
	public int damageDropped(IBlockState arg0) {
		return 0;
	}

	public static String getName() {
		return name;
	}
}
