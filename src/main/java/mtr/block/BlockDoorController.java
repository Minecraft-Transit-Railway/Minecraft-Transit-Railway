package mtr.block;

import java.util.List;
import java.util.Random;

import mods.railcraft.common.blocks.tracks.TrackTools;
import mods.railcraft.common.carts.LinkageManager;
import mods.railcraft.common.items.ItemCrowbar;
import mtr.entity.EntityTrain;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockDoorController extends Block {

	public static final PropertyDirection FACING = PropertyDirection.create("facing");
	public static final PropertyEnum<TrainState> STATE = PropertyEnum.create("state", TrainState.class);
	public static final PropertyBool TIMER = PropertyBool.create("timer");

	private static final float TOLERANCE = 0.001F;

	public BlockDoorController() {
		super(Material.ROCK);
		setHardness(2);
		setCreativeTab(CreativeTabs.REDSTONE);
		setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.UP).withProperty(STATE, TrainState.GONE).withProperty(TIMER, false));
	}

	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		if (entityIn instanceof EntityMinecart && Math.abs(entityIn.motionX) < TOLERANCE && Math.abs(entityIn.motionZ) < TOLERANCE) {
			final boolean isGone = state.getValue(STATE) == TrainState.GONE;
			final boolean isOpen = state.getValue(STATE) == TrainState.OPENED && !worldIn.isBlockPowered(pos);
			final boolean noUpdate = (state.getValue(TIMER) ? true : !isOpen) && !worldIn.isUpdateScheduled(pos, this);
			if (isGone || noUpdate)
				worldIn.scheduleUpdate(pos, this, 1);
		}
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		switch (state.getValue(STATE)) {
		case OPENING_SOON:
			worldIn.setBlockState(pos, state.withProperty(STATE, TrainState.OPENED));
			setDoors(worldIn, pos, getTrackDirection(worldIn, pos));

			if (state.getValue(TIMER))
				worldIn.scheduleUpdate(pos, this, 120);
			else if (worldIn.isBlockPowered(pos))
				worldIn.scheduleUpdate(pos, this, 1);
			break;
		case OPENED:
			worldIn.setBlockState(pos, state.withProperty(STATE, TrainState.CLOSED));
			setDoors(worldIn, pos, null);
			worldIn.scheduleUpdate(pos, this, 60);
			break;
		case CLOSED:
			worldIn.setBlockState(pos, state.withProperty(STATE, TrainState.LEAVING));
			worldIn.scheduleUpdate(pos, this, 40);
			break;
		case LEAVING:
			worldIn.setBlockState(pos, state.withProperty(STATE, TrainState.GONE));
			break;
		case GONE:
			worldIn.setBlockState(pos, state.withProperty(STATE, TrainState.OPENING_SOON));
			worldIn.scheduleUpdate(pos, this, 20);
			break;
		}
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		final EnumFacing direction = getTrackDirection(worldIn, pos);
		return state.withProperty(FACING, direction == null ? EnumFacing.UP : direction);
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		if (!state.getValue(TIMER) && state.getValue(STATE) == TrainState.OPENED && worldIn.isBlockPowered(pos))
			worldIn.scheduleUpdate(pos, this, 1);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (playerIn.getHeldItem(hand).getItem() instanceof ItemCrowbar) {
			worldIn.setBlockState(pos, state.cycleProperty(TIMER));
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		final boolean isLeaving = blockState.getValue(STATE) == TrainState.LEAVING;
		final boolean isTrack = getTrackDirection(blockAccess, pos).getOpposite() == side;
		return isLeaving && isTrack ? 15 : 0;
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(STATE).ordinal() + (state.getValue(TIMER) ? 8 : 0);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		int state = meta & 7;
		if (state < 0 || state >= TrainState.values().length)
			state = 0;
		final TrainState trainState = TrainState.values()[state];
		return getDefaultState().withProperty(STATE, trainState == TrainState.OPENED ? TrainState.OPENING_SOON : trainState).withProperty(TIMER, (meta & 8) > 0);
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
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public boolean isTopSolid(IBlockState state) {
		return false;
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return face == EnumFacing.DOWN ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return NULL_AABB;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return new AxisAlignedBB(0, 0, 0, 1, 0.25, 1);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING, STATE, TIMER });
	}

	private EnumFacing getTrackDirection(IBlockAccess worldIn, BlockPos pos) {
		if (TrackTools.isRailBlockAt(worldIn, pos.north()))
			return EnumFacing.NORTH;
		if (TrackTools.isRailBlockAt(worldIn, pos.east()))
			return EnumFacing.EAST;
		if (TrackTools.isRailBlockAt(worldIn, pos.south()))
			return EnumFacing.SOUTH;
		if (TrackTools.isRailBlockAt(worldIn, pos.west()))
			return EnumFacing.WEST;
		return null;
	}

	private void setDoors(World worldIn, BlockPos pos, EnumFacing doorDirection) {
		final List<EntityMinecart> train = worldIn.getEntitiesWithinAABB(EntityMinecart.class, new AxisAlignedBB(pos));
		if (!train.isEmpty())
			LinkageManager.INSTANCE.streamTrain(train.get(0)).filter(t -> t instanceof EntityTrain).forEach(t -> ((EntityTrain) t).setDoors(doorDirection));
	}

	private enum TrainState implements IStringSerializable {

		OPENING_SOON("opening_soon"), OPENED("opened"), CLOSED("closed"), LEAVING("leaving"), GONE("gone");

		private String name;

		private TrainState(String nameIn) {
			name = nameIn;
		}

		@Override
		public String getName() {
			return name;
		}
	}
}
