package MTR;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockMTRMap extends BlockWithDirection {

	private static final String name = "BlockMTRMap";
	public static final PropertyInteger TOP = PropertyInteger.create("top", 0, 2);

	protected BlockMTRMap() {
		super();
		GameRegistry.registerBlock(this, name);
		setUnlocalizedName(name);
		setLightLevel(1F);
		setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(TOP, 0));
	}

	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ,
			int meta, EntityLivingBase placer) {
		EnumFacing var9 = placer.getHorizontalFacing().rotateY();
		BlockPos pos2 = pos;
		switch (var9) {
		case NORTH:
			pos2 = pos.add(1, 0, 0);
			break;
		case EAST:
			pos2 = pos.add(0, 0, 1);
			break;
		case SOUTH:
			pos2 = pos.add(-1, 0, 0);
			break;
		case WEST:
			pos2 = pos.add(0, 0, -1);
			break;
		default:
			break;
		}
		if (worldIn.getBlockState(pos2).getBlock().isReplaceable(worldIn, pos2)
				&& worldIn.getBlockState(pos2.up()).getBlock().isReplaceable(worldIn, pos2.up())
				&& worldIn.getBlockState(pos2.up(2)).getBlock().isReplaceable(worldIn, pos2.up(2))
				&& worldIn.getBlockState(pos.up()).getBlock().isReplaceable(worldIn, pos.up())
				&& worldIn.getBlockState(pos.up(2)).getBlock().isReplaceable(worldIn, pos.up(2))) {
			worldIn.setBlockState(pos2,
					getDefaultState().withProperty(FACING, var9.getOpposite()).withProperty(TOP, 0));
			worldIn.setBlockState(pos2.add(0, 1, 0),
					getDefaultState().withProperty(FACING, var9.getOpposite()).withProperty(TOP, 1));
			worldIn.setBlockState(pos2.add(0, 2, 0),
					getDefaultState().withProperty(FACING, var9.getOpposite()).withProperty(TOP, 2));
			worldIn.setBlockState(pos.add(0, 1, 0), getDefaultState().withProperty(FACING, var9).withProperty(TOP, 1));
			worldIn.setBlockState(pos.add(0, 2, 0), getDefaultState().withProperty(FACING, var9).withProperty(TOP, 2));
			return super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(FACING, var9)
					.withProperty(TOP, 0);
		} else
			return Blocks.air.getDefaultState();
	}

	@Override
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
		BlockPos pos2 = pos;
		boolean b = false;
		switch (state.getValue(FACING)) {
		case NORTH:
			pos2 = pos.add(1, 0, 0);
			break;
		case EAST:
			pos2 = pos.add(0, 0, 1);
			break;
		case SOUTH:
			pos2 = pos.add(-1, 0, 0);
			break;
		case WEST:
			pos2 = pos.add(0, 0, -1);
			break;
		default:
			break;
		}
		switch (state.getValue(TOP)) {
		case 0:
			b = !(worldIn.getBlockState(pos.add(0, 1, 0)).getBlock() instanceof BlockMTRMap)
					|| !(worldIn.getBlockState(pos.add(0, 2, 0)).getBlock() instanceof BlockMTRMap);
			break;
		case 1:
			b = !(worldIn.getBlockState(pos.add(0, -1, 0)).getBlock() instanceof BlockMTRMap)
					|| !(worldIn.getBlockState(pos.add(0, 1, 0)).getBlock() instanceof BlockMTRMap);
			break;
		case 2:
			b = !(worldIn.getBlockState(pos.add(0, -1, 0)).getBlock() instanceof BlockMTRMap)
					|| !(worldIn.getBlockState(pos.add(0, -2, 0)).getBlock() instanceof BlockMTRMap);
			break;
		}
		if (!(worldIn.getBlockState(pos2).getBlock() instanceof BlockMTRMap) || b)
			worldIn.setBlockToAir(pos);
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos) {
		EnumFacing var3 = access.getBlockState(pos).getValue(FACING);
		if (var3.getAxis() == EnumFacing.Axis.X)
			setBlockBounds(0.4375F, 0.0F, 0.0F, 0.5625F, 1.0F, 1.0F);
		else
			setBlockBounds(0.0F, 0.0F, 0.4375F, 1.0F, 1.0F, 0.5625F);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		if (worldIn.isRemote)
			MTR.proxy.openMapGUI();
		return true;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta % 4)).withProperty(TOP, meta / 4);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).getHorizontalIndex() + 4 * state.getValue(TOP);
	}

	@Override
	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { FACING, TOP });
	}

	public static String getName() {
		return name;
	}
}
