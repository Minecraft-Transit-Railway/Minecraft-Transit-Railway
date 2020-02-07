package mtr.block;

import mods.railcraft.common.items.ItemCrowbar;
import mtr.MTRUtilities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class BlockPSDAPGBase extends BlockHorizontal {

	public static final PropertyEnum<PSDAPGSide> SIDE = PropertyEnum.create("side", PSDAPGSide.class);
	public static final PropertyBool TOP = PropertyBool.create("top");

	public BlockPSDAPGBase() {
		super(Material.ROCK);
		setHardness(2);
	}

	@Override
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
		for (int y = -1; y <= 1; y++)
			if (Block.isEqualTo(this, worldIn.getBlockState(pos.up(y)).getBlock()))
				worldIn.setBlockToAir(pos.up(y));
	}

	@Override
	public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn) {
		onBlockDestroyedByPlayer(worldIn, pos, null);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!(this instanceof BlockPSDAPGDoorBase) && MTRUtilities.getItemFromPlayer(playerIn, hand) instanceof ItemCrowbar) {
			for (int y = -1; y <= 1; y++)
				if (Block.isEqualTo(this, worldIn.getBlockState(pos.up(y)).getBlock()))
					connectGlass(worldIn, pos.up(y), state);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis) {
		return false;
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		return state.withProperty(TOP, Block.isEqualTo(this, worldIn.getBlockState(pos.down()).getBlock()));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return (state.getValue(SIDE).ordinal() << 2) + state.getValue(FACING).getHorizontalIndex();
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(SIDE, PSDAPGSide.values()[meta >> 2]).withProperty(FACING, EnumFacing.getHorizontal(meta & 3));
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		switch (state.getValue(FACING)) {
		case NORTH:
			return new AxisAlignedBB(0, 0, 0, 1, 1, 0.125);
		case EAST:
			return new AxisAlignedBB(0.875, 0, 0, 1, 1, 1);
		case SOUTH:
			return new AxisAlignedBB(0, 0, 0.875, 1, 1, 1);
		case WEST:
			return new AxisAlignedBB(0, 0, 0, 0.125, 1, 1);
		default:
			return NULL_AABB;
		}
	}

	@Override
	public EnumPushReaction getMobilityFlag(IBlockState state) {
		return EnumPushReaction.IGNORE;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isTopSolid(IBlockState state) {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING, SIDE, TOP });
	}

	private void connectGlass(World worldIn, BlockPos pos, IBlockState state) {
		final EnumFacing facing = state.getValue(FACING);

		final BlockPos leftPos = pos.offset(facing.rotateYCCW());
		final IBlockState leftState = worldIn.getBlockState(leftPos);
		final boolean leftValid = Block.isEqualTo(this, leftState.getBlock());

		if (leftValid) {
			final PSDAPGSide side = leftState.getValue(SIDE);
			PSDAPGSide newLeftSide;

			if (side == PSDAPGSide.RIGHT)
				newLeftSide = PSDAPGSide.MIDDLE;
			else if (side == PSDAPGSide.SINGLE)
				newLeftSide = PSDAPGSide.LEFT;
			else
				newLeftSide = side;

			worldIn.setBlockState(leftPos, leftState.withProperty(SIDE, newLeftSide));
		}

		final BlockPos rightPos = pos.offset(facing.rotateY());
		final IBlockState rightState = worldIn.getBlockState(rightPos);
		final boolean rightValid = Block.isEqualTo(this, rightState.getBlock());

		if (rightValid) {
			final PSDAPGSide side = rightState.getValue(SIDE);
			PSDAPGSide newRightSide;

			if (side == PSDAPGSide.LEFT)
				newRightSide = PSDAPGSide.MIDDLE;
			else if (side == PSDAPGSide.SINGLE)
				newRightSide = PSDAPGSide.RIGHT;
			else
				newRightSide = side;

			worldIn.setBlockState(rightPos, rightState.withProperty(SIDE, newRightSide));
		}

		PSDAPGSide newSide;
		if (leftValid && rightValid)
			newSide = PSDAPGSide.MIDDLE;
		else if (leftValid)
			newSide = PSDAPGSide.RIGHT;
		else if (rightValid)
			newSide = PSDAPGSide.LEFT;
		else
			newSide = PSDAPGSide.SINGLE;

		worldIn.setBlockState(pos, state.withProperty(SIDE, newSide));
	}

	public enum PSDAPGSide implements IStringSerializable {

		LEFT("left"), RIGHT("right"), MIDDLE("middle"), SINGLE("single");

		private String name;

		private PSDAPGSide(String nameIn) {
			name = nameIn;
		}

		@Override
		public String getName() {
			return name;
		}
	}
}
