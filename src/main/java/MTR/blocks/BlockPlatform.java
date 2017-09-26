package MTR.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPlatform extends BlockWithDirection {

	private static final String name = "BlockPlatform";
	public static final PropertyInteger TEXTURE = PropertyInteger.create("texture", 0, 2);
	public static final PropertyInteger FLOOR = PropertyInteger.create("floor", 0, 4);

	public BlockPlatform() {
		super(name);
		setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(FLOOR, 0)
				.withProperty(TEXTURE, 0));
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos0) {
		BlockPos pos = pos0.up();
		Block block = worldIn.getBlockState(pos).getBlock();
		if (block instanceof BlockAPGDoor || block instanceof BlockAPGDoorClosed || block instanceof BlockPSDDoor
				|| block instanceof BlockPSDDoorClosed) {
			boolean side = worldIn.getBlockState(pos).getValue(BlockPSD.SIDE);
			EnumFacing facing = worldIn.getBlockState(pos).getValue(BlockPSD.FACING);
			return state.withProperty(FACING, facing).withProperty(FLOOR, side ? 3 : 2).withProperty(TEXTURE,
					block instanceof BlockAPGDoor || block instanceof BlockAPGDoorClosed ? 2 : 1);
		}
		EnumFacing var3 = state.getValue(FACING);
		BlockPos pos2 = pos;
		BlockPos pos3 = pos;
		switch (var3) {
		case NORTH:
			pos2 = pos.add(0, 0, 1);
			pos3 = pos.add(0, 0, -1);
			break;
		case SOUTH:
			pos2 = pos.add(0, 0, -1);
			pos3 = pos.add(0, 0, 1);
			break;
		case EAST:
			pos2 = pos.add(-1, 0, 0);
			pos3 = pos.add(1, 0, 0);
			break;
		case WEST:
			pos2 = pos.add(1, 0, 0);
			pos3 = pos.add(-1, 0, 0);
			break;
		default:
		}
		Block blockleft = worldIn.getBlockState(pos2).getBlock();
		Block blockright = worldIn.getBlockState(pos3).getBlock();
		if (blockleft instanceof BlockAPGDoor || blockleft instanceof BlockAPGDoorClosed
				|| blockleft instanceof BlockPSDDoor || blockleft instanceof BlockPSDDoorClosed) {
			EnumFacing facing = worldIn.getBlockState(pos2).getValue(BlockPSD.FACING);
			return state.withProperty(FACING, facing).withProperty(FLOOR, 4).withProperty(TEXTURE,
					blockleft instanceof BlockAPGDoor || blockleft instanceof BlockAPGDoorClosed ? 2 : 1);
		} else if (blockright instanceof BlockAPGDoor || blockright instanceof BlockAPGDoorClosed
				|| blockright instanceof BlockPSDDoor || blockright instanceof BlockPSDDoorClosed) {
			EnumFacing facing = worldIn.getBlockState(pos3).getValue(BlockPSD.FACING);
			return state.withProperty(FACING, facing).withProperty(FLOOR, 1).withProperty(TEXTURE,
					blockright instanceof BlockAPGDoor || blockright instanceof BlockAPGDoorClosed ? 2 : 1);
		} else {
			int texture;
			if (block instanceof BlockAPGGlassBottom || block instanceof BlockAPGGlassMiddle)
				texture = 2;
			else if (block instanceof BlockPSDGlass || block instanceof BlockPSDGlass2015
					|| block instanceof BlockPSDGlassEnd || block instanceof BlockPSDGlassMiddle
					|| block instanceof BlockPSDGlassVeryEnd)
				texture = 1;
			else
				texture = 0;
			return state.withProperty(FLOOR, 0).withProperty(TEXTURE, texture);
		}
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).getHorizontalIndex();
	}

	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ,
			int meta, EntityLivingBase placer) {
		EnumFacing var9 = placer.getHorizontalFacing().rotateY();
		return super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(FACING, var9);
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING, TEXTURE, FLOOR });
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return true;
	}

	@Override
	public EnumPushReaction getMobilityFlag(IBlockState state) {
		return EnumPushReaction.BLOCK;
	}
}
