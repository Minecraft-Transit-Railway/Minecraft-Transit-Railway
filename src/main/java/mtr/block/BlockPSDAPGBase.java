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
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class BlockPSDAPGBase extends BlockHorizontal {

	public static final PropertyEnum<EnumPSDAPGSide> SIDE = PropertyEnum.create("side", EnumPSDAPGSide.class);
	public static final PropertyBool TOP = PropertyBool.create("top");

	public BlockPSDAPGBase() {
		super(Material.ROCK);
		setHardness(2);
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		final Block blockUp = worldIn.getBlockState(pos.up()).getBlock();
		final Block blockDown = worldIn.getBlockState(pos.down()).getBlock();

		if (!Block.isEqualTo(this, blockUp) && !Block.isEqualTo(this, blockDown))
			worldIn.setBlockToAir(pos);
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
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(getItemDropped(state, null, 0), 1, damageDropped(state));
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		return state.withProperty(TOP, isTop(worldIn, pos));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return (state.getValue(SIDE).ordinal() << 2) + state.getValue(FACING).getHorizontalIndex();
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(SIDE, EnumPSDAPGSide.values()[meta >> 2]).withProperty(FACING, EnumFacing.getHorizontal(meta & 3));
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		final boolean isAPG = this instanceof BlockAPGGlass || this instanceof BlockAPGDoor;
		final double height = isAPG && isTop(source, pos) ? 0.5625 : 1;

		switch (state.getValue(FACING)) {
		case NORTH:
			return new AxisAlignedBB(0, 0, 0, 1, height, 0.125);
		case EAST:
			return new AxisAlignedBB(0.875, 0, 0, 1, height, 1);
		case SOUTH:
			return new AxisAlignedBB(0, 0, 0.875, 1, height, 1);
		case WEST:
			return new AxisAlignedBB(0, 0, 0, 0.125, height, 1);
		default:
			return NULL_AABB;
		}
	}

	@Override
	public EnumPushReaction getMobilityFlag(IBlockState state) {
		return EnumPushReaction.BLOCK;
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

	public final boolean isTop(IBlockAccess worldIn, BlockPos pos) {
		return Block.isEqualTo(this, worldIn.getBlockState(pos.down()).getBlock());
	}

	private void connectGlass(World worldIn, BlockPos pos, IBlockState state) {
		final EnumFacing facing = state.getValue(FACING);

		final BlockPos leftPos = pos.offset(facing.rotateYCCW());
		final IBlockState leftState = worldIn.getBlockState(leftPos);
		final boolean leftValid = Block.isEqualTo(this, leftState.getBlock());

		if (leftValid) {
			final EnumPSDAPGSide side = leftState.getValue(SIDE);
			EnumPSDAPGSide newLeftSide;

			if (side == EnumPSDAPGSide.RIGHT)
				newLeftSide = EnumPSDAPGSide.MIDDLE;
			else if (side == EnumPSDAPGSide.SINGLE)
				newLeftSide = EnumPSDAPGSide.LEFT;
			else
				newLeftSide = side;

			worldIn.setBlockState(leftPos, leftState.withProperty(SIDE, newLeftSide));
		}

		final BlockPos rightPos = pos.offset(facing.rotateY());
		final IBlockState rightState = worldIn.getBlockState(rightPos);
		final boolean rightValid = Block.isEqualTo(this, rightState.getBlock());

		if (rightValid) {
			final EnumPSDAPGSide side = rightState.getValue(SIDE);
			EnumPSDAPGSide newRightSide;

			if (side == EnumPSDAPGSide.LEFT)
				newRightSide = EnumPSDAPGSide.MIDDLE;
			else if (side == EnumPSDAPGSide.SINGLE)
				newRightSide = EnumPSDAPGSide.RIGHT;
			else
				newRightSide = side;

			worldIn.setBlockState(rightPos, rightState.withProperty(SIDE, newRightSide));
		}

		EnumPSDAPGSide newSide;
		if (leftValid && rightValid)
			newSide = EnumPSDAPGSide.MIDDLE;
		else if (leftValid)
			newSide = EnumPSDAPGSide.RIGHT;
		else if (rightValid)
			newSide = EnumPSDAPGSide.LEFT;
		else
			newSide = EnumPSDAPGSide.SINGLE;

		worldIn.setBlockState(pos, state.withProperty(SIDE, newSide));
	}

	public enum EnumPSDAPGSide implements IStringSerializable {

		LEFT("left"), RIGHT("right"), MIDDLE("middle"), SINGLE("single");

		private String name;

		private EnumPSDAPGSide(String nameIn) {
			name = nameIn;
		}

		@Override
		public String getName() {
			return name;
		}
	}
}
