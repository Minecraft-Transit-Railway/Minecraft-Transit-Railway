package mtr.block;

import mtr.Items;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import java.util.Random;

public class BlockAPGGlass extends BlockPSDAPGBase {

	public static final PropertyEnum<EnumAPGGlassEnd> SIDE_END = PropertyEnum.create("side_end", EnumAPGGlassEnd.class);

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Items.apg;
	}

	@Override
	public int damageDropped(IBlockState state) {
		return 1;
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		final EnumAPGGlassEnd side = getSideEnd(worldIn, pos, state);
		return super.getActualState(state, worldIn, pos).withProperty(SIDE_END, side);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		final EnumAPGGlassEnd side = getSideEnd(source, pos, state);
		if (side == EnumAPGGlassEnd.NONE)
			return super.getBoundingBox(state, source, pos);
		else
			return new AxisAlignedBB(0, 0, 0, 1, isTop(source, pos) ? 0.5625 : 1, 1);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING, SIDE, SIDE_END, TOP);
	}

	private EnumAPGGlassEnd getSideEnd(IBlockAccess worldIn, BlockPos pos, IBlockState state) {
		final EnumFacing facing = state.getValue(FACING);
		final boolean top = isTop(worldIn, pos);

		final BlockPos leftPos = pos.offset(facing.rotateYCCW());
		final IBlockState leftState = worldIn.getBlockState(leftPos);
		final IBlockState leftStateOffset = worldIn.getBlockState(top ? leftPos.down() : leftPos.up());

		final BlockPos rightPos = pos.offset(facing.rotateY());
		final IBlockState rightState = worldIn.getBlockState(rightPos);
		final IBlockState rightStateOffset = worldIn.getBlockState(top ? rightPos.down() : rightPos.up());

		if (leftState.getMaterial().isReplaceable() && leftStateOffset.getMaterial().isReplaceable() && rightState.getBlock() instanceof BlockAPGGlass)
			return EnumAPGGlassEnd.LEFT;
		else if (rightState.getMaterial().isReplaceable() && rightStateOffset.getMaterial().isReplaceable() && leftState.getBlock() instanceof BlockAPGGlass)
			return EnumAPGGlassEnd.RIGHT;
		else
			return EnumAPGGlassEnd.NONE;
	}

	private enum EnumAPGGlassEnd implements IStringSerializable {

		LEFT("left"), RIGHT("right"), NONE("none");

		private final String name;

		EnumAPGGlassEnd(String nameIn) {
			name = nameIn;
		}

		@Override
		public String getName() {
			return name;
		}
	}
}
