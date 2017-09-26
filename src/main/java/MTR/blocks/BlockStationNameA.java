package MTR.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockStationNameA extends BlockStationNameBase {

	private static final String name = "BlockStationNameCKT";

	public BlockStationNameA() {
		super(name);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		if (state.getValue(POLE))
			return new AxisAlignedBB(0.375F, 0.0F, 0.375F, 0.625F, 1.0F, 0.625F);
		else {
			EnumFacing var3 = state.getValue(FACING);
			if (state.getValue(SIDE))
				switch (var3) {
				case NORTH:
					return new AxisAlignedBB(0.0F, 0.0F, 0.03125F, 0.0625F, 1.0F, 0.96875F);
				case SOUTH:
					return new AxisAlignedBB(0.9375F, 0.0F, 0.03125F, 1.0F, 1.0F, 0.96875F);
				case EAST:
					return new AxisAlignedBB(0.03125F, 0.0F, 0.0F, 0.96875F, 1.0F, 0.0625F);
				case WEST:
					return new AxisAlignedBB(0.03125F, 0.0F, 0.9375F, 0.96875F, 1.0F, 1.0F);
				default:
					return NULL_AABB;
				}
			else if (var3.getAxis() == EnumFacing.Axis.X)
				return new AxisAlignedBB(0.03125F, 0.0F, 0.3125F, 0.96875F, 1.0F, 0.6875F);
			else
				return new AxisAlignedBB(0.3125F, 0.0F, 0.03125F, 0.6875F, 1.0F, 0.96875F);
		}
	}

	@Override
	public int damageDropped(IBlockState arg0) {
		return 0;
	}
}
