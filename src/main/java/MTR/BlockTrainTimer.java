package MTR;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockTrainTimer extends Block implements ITileEntityProvider {

	private static final String name = "BlockTrainTimer";
	public static final PropertyInteger FACING = PropertyInteger.create("facing", 0, 4);
	public static final PropertyBool LIGHT = PropertyBool.create("light");
	public static final PropertyBool TRACK = PropertyBool.create("track");

	protected BlockTrainTimer() {
		super(Material.circuits);
		setHardness(0.5F);
		GameRegistry.registerBlock(this, name);
		setCreativeTab(MTR.MTRtab);
		setDefaultState(blockState.getBaseState().withProperty(FACING, 0).withProperty(LIGHT, false).withProperty(TRACK,
				false));
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
		setTickRandomly(true);
		setUnlocalizedName(name);
	}

	@Override
	public TileEntity createNewTileEntity(World arg0, int arg1) {
		return new TileEntityTrainTimerEntity();
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		if (worldIn.isRemote) {
			TileEntityTrainTimerEntity te = (TileEntityTrainTimerEntity) worldIn.getTileEntity(pos);
			;
			// MTR.proxy.openGUI(te);
		}
		return true;
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		BlockPos pos1 = pos.add(0, 0, -1); // north
		BlockPos pos2 = pos.add(1, 0, 0); // east
		BlockPos pos3 = pos.add(0, 0, 1); // south
		BlockPos pos4 = pos.add(-1, 0, 0); // west
		int facing = 0;
		if (worldIn.getBlockState(pos1).getBlock() instanceof BlockRailStation)
			facing = 1;
		else if (worldIn.getBlockState(pos2).getBlock() instanceof BlockRailStation)
			facing = 2;
		else if (worldIn.getBlockState(pos3).getBlock() instanceof BlockRailStation)
			facing = 3;
		else if (worldIn.getBlockState(pos4).getBlock() instanceof BlockRailStation)
			facing = 4;
		return state.withProperty(FACING, facing);
	}

	public void onTrainCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entity) {
		try {
			EntityTrainBase entityTrains = (EntityTrainBase) entity;
			if (!(Boolean) state.getValue(LIGHT) && !(Boolean) state.getValue(TRACK)) {
				worldIn.setBlockState(pos, state.withProperty(LIGHT, true).withProperty(TRACK, false));
				worldIn.scheduleUpdate(pos, this, 160);
			}
		} catch (Exception e) {
		}
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		if (state.getValue(TRACK))
			worldIn.setBlockState(pos, state.withProperty(LIGHT, false).withProperty(TRACK, false));
		if (state.getValue(LIGHT)) {
			worldIn.setBlockState(pos, state.withProperty(LIGHT, false).withProperty(TRACK, true));
			worldIn.scheduleUpdate(pos, this, 160);
		}
	}

	@Override
	public int getWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
		boolean railside = false, powered = false;
		if (worldIn.getBlockState(pos.offset(side.getOpposite())).getBlock() instanceof BlockRailStation)
			railside = true;
		if (state.getValue(LIGHT) && !railside || state.getValue(TRACK) && railside)
			powered = true;
		return powered ? 15 : 0;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(LIGHT, meta % 2 == 1).withProperty(TRACK, meta > 1);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int a = state.getValue(LIGHT) ? 1 : 0;
		a = a + (state.getValue(TRACK) ? 2 : 0);
		return a;
	}

	@Override
	public boolean canProvidePower() {
		return true;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
		return null;
	}

	@Override
	public int getRenderType() {
		return 3;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public EnumWorldBlockLayer getBlockLayer() {
		return EnumWorldBlockLayer.CUTOUT;
	}

	@Override
	public boolean isFullCube() {
		return false;
	}

	@Override
	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { FACING, LIGHT, TRACK });
	}

	public String getName() {
		return name;
	}
}
