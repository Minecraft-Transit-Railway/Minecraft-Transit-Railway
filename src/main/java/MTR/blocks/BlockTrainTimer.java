package MTR.blocks;

import java.util.Random;

import MTR.BlockBase;
import MTR.items.ItemBrush;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTrainTimer extends BlockBase {

	private static final String name = "BlockTrainTimer";
	public static final PropertyInteger FACING = PropertyInteger.create("facing", 0, 4);
	public static final PropertyBool LIGHT = PropertyBool.create("light");
	public static final PropertyInteger TIME = PropertyInteger.create("time", 0, 3);
	public static final PropertyBool TRACK = PropertyBool.create("track");
	private static final int[] times = { 5, 10, 20, 30 };

	public BlockTrainTimer() {
		super(Material.CIRCUITS, name);
		setHardness(0.5F);
		setDefaultState(blockState.getBaseState().withProperty(FACING, 0).withProperty(LIGHT, false)
				.withProperty(TIME, 0).withProperty(TRACK, false));
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
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
		if (!worldIn.isUpdateScheduled(pos, worldIn.getBlockState(pos).getBlock()))
			worldIn.scheduleUpdate(pos, this, 40);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, ItemStack stack, EnumFacing side, float hitX, float hitY, float hitZ) {
		ItemStack itemStack = playerIn.inventory.getCurrentItem();
		if (itemStack != null && itemStack.getItem() instanceof ItemBrush) {
			worldIn.setBlockState(pos, state.cycleProperty(TIME));
			int time = worldIn.getBlockState(pos).getValue(TIME);
			if (!worldIn.isRemote)
				playerIn.addChatComponentMessage(new TextComponentString(I18n.format("gui.timertext", new Object[0])
						+ " " + times[time] + " " + I18n.format("gui.seconds", new Object[0])));
			return true;
		} else
			return false;
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		if (!state.getValue(LIGHT) && !state.getValue(TRACK)) {
			worldIn.setBlockState(pos, state.withProperty(LIGHT, true).withProperty(TRACK, false));
			worldIn.scheduleUpdate(pos, this, 20 * times[state.getValue(TIME)]);
		}
		if (state.getValue(TRACK))
			worldIn.setBlockState(pos, state.withProperty(LIGHT, false).withProperty(TRACK, false));
		if (state.getValue(LIGHT)) {
			worldIn.setBlockState(pos, state.withProperty(LIGHT, false).withProperty(TRACK, true));
			worldIn.scheduleUpdate(pos, this, 160);
		}
	}

	@Override
	public int getWeakPower(IBlockState state, IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
		boolean railside = false, powered = false;
		if (worldIn.getBlockState(pos.offset(side.getOpposite())).getBlock() instanceof BlockRailStation)
			railside = true;
		if (state.getValue(LIGHT) && !railside || state.getValue(TRACK) && railside)
			powered = true;
		return powered ? 15 : 0;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(TIME, meta % 4).withProperty(LIGHT, (meta & 4) > 0).withProperty(TRACK,
				(meta & 8) > 0);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(TIME) + (state.getValue(LIGHT) ? 4 : 0) + (state.getValue(TRACK) ? 8 : 0);
	}

	@Override
	public boolean canProvidePower(IBlockState state) {
		return true;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState state, World worldIn, BlockPos pos) {
		return null;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING, LIGHT, TIME, TRACK });
	}
}
