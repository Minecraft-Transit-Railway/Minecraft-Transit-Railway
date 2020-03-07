package mtr.block;

import mtr.MTRUtilities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockPlatform extends BlockHorizontal {

	public static final PropertyEnum<EnumDoorType> DOOR_TYPE = PropertyEnum.create("door_type", EnumDoorType.class);
	public static final PropertyEnum<EnumDoorState> OPEN = PropertyEnum.create("open", EnumDoorState.class);
	public static final PropertyInteger SIDE = PropertyInteger.create("side", 0, 4);

	public BlockPlatform() {
		super(Material.ROCK);
		setHardness(2);
		setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
		setDefaultState(blockState.getBaseState().withProperty(DOOR_TYPE, EnumDoorType.NONE).withProperty(SIDE, 0));
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		if (state.getValue(OPEN) == EnumDoorState.CLOSING)
			worldIn.setBlockState(pos, state.withProperty(OPEN, EnumDoorState.CLOSED));
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		EnumFacing facing = searchBlock(worldIn, pos, BlockRailBase.class, 3);
		if (facing == null)
			facing = searchBlock(worldIn, pos, BlockAir.class, 1);
		if (facing == null)
			facing = EnumFacing.NORTH;

		final Block blockAbove = worldIn.getBlockState(pos.up()).getBlock();

		EnumDoorType doorType;
		if (blockAbove instanceof BlockPSDDoor || blockAbove instanceof BlockPSDGlass || blockAbove instanceof BlockPSDGlassEnd)
			doorType = EnumDoorType.PSD;
		else if (blockAbove instanceof BlockAPGDoor || blockAbove instanceof BlockAPGGlass)
			doorType = EnumDoorType.APG;
		else
			doorType = EnumDoorType.NONE;

		final boolean aboveIsDoor = blockAbove instanceof BlockPSDAPGDoorBase;
		final boolean leftAboveIsDoor = worldIn.getBlockState(pos.up().offset(facing.rotateYCCW())).getBlock() instanceof BlockPSDAPGDoorBase;
		final boolean rightAboveIsDoor = worldIn.getBlockState(pos.up().offset(facing.rotateY())).getBlock() instanceof BlockPSDAPGDoorBase;

		int side;
		if (aboveIsDoor && rightAboveIsDoor)
			side = 2;
		else if (aboveIsDoor && leftAboveIsDoor)
			side = 3;
		else if (rightAboveIsDoor)
			side = 1;
		else if (leftAboveIsDoor)
			side = 4;
		else
			side = 0;

		return state.withProperty(FACING, facing).withProperty(DOOR_TYPE, doorType).withProperty(SIDE, side);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(OPEN).ordinal();
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		final EnumDoorState doorState = EnumDoorState.values()[meta];
		return getDefaultState().withProperty(OPEN, doorState == EnumDoorState.CLOSING ? EnumDoorState.CLOSED : doorState);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING, DOOR_TYPE, OPEN, SIDE);
	}

	public void updateOpen(World world, IBlockState state, BlockPos pos, EnumFacing direction, boolean open) {
		world.setBlockState(pos, state.withProperty(OPEN, open ? EnumDoorState.OPENED : EnumDoorState.CLOSING));
		world.scheduleUpdate(pos, this, MTRUtilities.DOOR_DURATION_TICKS);

		final Block blockOffset = world.getBlockState(pos.offset(direction)).getBlock();
		if (blockOffset instanceof BlockPlatform)
			((BlockPlatform) blockOffset).updateOpen(world, state, pos.offset(direction), direction, open);
	}

	private EnumFacing searchBlock(IBlockAccess worldIn, BlockPos pos, Class<? extends Block> blockClass, int maxRadius) {
		for (int radius = 1; radius <= maxRadius; radius++)
			for (final EnumFacing facing : EnumFacing.HORIZONTALS)
				if (blockClass.isInstance(worldIn.getBlockState(pos.offset(facing, radius)).getBlock()))
					return facing;

		return null;
	}

	public enum EnumDoorState implements IStringSerializable {

		CLOSED("closed"), CLOSING("closing"), OPENED("opened");

		private final String name;

		EnumDoorState(String nameIn) {
			name = nameIn;
		}

		@Override
		public String getName() {
			return name;
		}
	}

	private enum EnumDoorType implements IStringSerializable {

		NONE("none"), PSD("psd"), APG("apg");

		private final String name;

		EnumDoorType(String nameIn) {
			name = nameIn;
		}

		@Override
		public String getName() {
			return name;
		}
	}
}
