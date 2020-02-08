package mtr.block;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockPSDAPGDoorBase extends BlockPSDAPGBase implements ITileEntityProvider {

	public static final PropertyEnum<PSDAPGDoor> SIDE_DOOR = PropertyEnum.create("side_door", PSDAPGDoor.class);

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		super.neighborChanged(state, worldIn, pos, blockIn, fromPos);

		final PSDAPGSide side = state.getValue(SIDE);
		final EnumFacing facing = state.getValue(FACING);

		BlockPos newPos = pos;
		if (side == PSDAPGSide.LEFT)
			newPos = pos.offset(facing.rotateY());
		else if (side == PSDAPGSide.RIGHT)
			newPos = pos.offset(facing.rotateYCCW());

		if (!Block.isEqualTo(this, worldIn.getBlockState(newPos).getBlock()))
			worldIn.setBlockToAir(pos);
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		state = super.getActualState(state, worldIn, pos);

		final boolean open;
		for (int i = 1; i <= 2; i++) {
			final IBlockState platformState = worldIn.getBlockState(pos.down(i));
			if (platformState.getBlock() instanceof BlockPlatform && platformState.getValue(BlockPlatform.OPEN))
				return state.withProperty(SIDE_DOOR, PSDAPGDoor.OPEN);
		}

		final PSDAPGSide side = state.getValue(SIDE);
		final EnumFacing facing = state.getValue(FACING);

		if (side == PSDAPGSide.LEFT) {
			final BlockPos checkPos = pos.offset(facing.rotateYCCW());
			if (worldIn.getBlockState(checkPos).getBlock() instanceof BlockPSDGlassEnd)
				return state.withProperty(SIDE_DOOR, PSDAPGDoor.LEFT_END);
			else
				return state.withProperty(SIDE_DOOR, PSDAPGDoor.LEFT);
		} else if (side == PSDAPGSide.RIGHT) {
			final BlockPos checkPos = pos.offset(facing.rotateY());
			if (worldIn.getBlockState(checkPos).getBlock() instanceof BlockPSDGlassEnd)
				return state.withProperty(SIDE_DOOR, PSDAPGDoor.RIGHT_END);
			else
				return state.withProperty(SIDE_DOOR, PSDAPGDoor.RIGHT);
		}

		return state;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING, SIDE, SIDE_DOOR, TOP });
	}

	private enum PSDAPGDoor implements IStringSerializable {

		LEFT("left"), RIGHT("right"), LEFT_END("left_end"), RIGHT_END("right_end"), OPEN("open");

		private final String name;

		private PSDAPGDoor(String nameIn) {
			name = nameIn;
		}

		@Override
		public String getName() {
			return name;
		}
	}
}
