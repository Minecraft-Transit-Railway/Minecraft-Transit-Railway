package mtr.block;

import mods.railcraft.api.tracks.TrackToolsAPI;
import mtr.MTRUtilities;
import mtr.item.ItemBrush;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
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

import java.util.Random;

public class BlockDoorController extends Block {

	public static final PropertyDirection FACING = PropertyDirection.create("facing");
	public static final PropertyEnum<EnumTrainState> STATE = PropertyEnum.create("state", EnumTrainState.class);
	public static final PropertyBool TIMER = PropertyBool.create("timer");

	private static final float TOLERANCE = 0.001F;

	public BlockDoorController() {
		super(Material.ROCK);
		setHardness(2);
		setCreativeTab(CreativeTabs.REDSTONE);
		setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.UP).withProperty(STATE, EnumTrainState.GONE).withProperty(TIMER, false));
	}

	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		if (entityIn instanceof EntityMinecart && Math.abs(entityIn.motionX) < TOLERANCE && Math.abs(entityIn.motionZ) < TOLERANCE) {
			final boolean isGone = state.getValue(STATE) == EnumTrainState.GONE;
			final boolean isOpen = state.getValue(STATE) == EnumTrainState.OPENED && !worldIn.isBlockPowered(pos);
			final boolean noUpdate = (state.getValue(TIMER) || !isOpen) && !worldIn.isUpdateScheduled(pos, this);
			if (isGone || noUpdate)
				worldIn.scheduleUpdate(pos, this, 1);
		}
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		switch (state.getValue(STATE)) {
			case OPENING_SOON:
				worldIn.setBlockState(pos, state.withProperty(STATE, EnumTrainState.OPENED));
				setDoors(worldIn, pos, true);

				if (state.getValue(TIMER))
					worldIn.scheduleUpdate(pos, this, MTRUtilities.DOOR_DURATION_TICKS + 80);
				else if (worldIn.isBlockPowered(pos))
					worldIn.scheduleUpdate(pos, this, 1);
				break;
			case OPENED:
				worldIn.setBlockState(pos, state.withProperty(STATE, EnumTrainState.CLOSED));
				setDoors(worldIn, pos, false);
				worldIn.scheduleUpdate(pos, this, MTRUtilities.DOOR_DURATION_TICKS + 20);
				break;
			case CLOSED:
				worldIn.setBlockState(pos, state.withProperty(STATE, EnumTrainState.LEAVING));
				worldIn.scheduleUpdate(pos, this, 40);
				break;
			case LEAVING:
				worldIn.setBlockState(pos, state.withProperty(STATE, EnumTrainState.GONE));
				break;
			case GONE:
				worldIn.setBlockState(pos, state.withProperty(STATE, EnumTrainState.OPENING_SOON));
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
		if (!state.getValue(TIMER) && state.getValue(STATE) == EnumTrainState.OPENED && worldIn.isBlockPowered(pos))
			worldIn.scheduleUpdate(pos, this, 1);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (playerIn.getHeldItem(hand).getItem() instanceof ItemBrush) {
			worldIn.setBlockState(pos, state.cycleProperty(TIMER));
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		final EnumFacing trackDirection = getTrackDirection(blockAccess, pos);
		if (trackDirection == null)
			return 0;
		return blockState.getValue(STATE) == EnumTrainState.LEAVING && trackDirection.getOpposite() == side ? 15 : 0;
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(STATE).ordinal() + (state.getValue(TIMER) ? 8 : 0);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		int state = meta & 7;
		if (state >= EnumTrainState.values().length)
			state = 0;
		final EnumTrainState trainState = EnumTrainState.values()[state];
		return getDefaultState().withProperty(STATE, trainState == EnumTrainState.OPENED ? EnumTrainState.OPENING_SOON : trainState).withProperty(TIMER, (meta & 8) > 0);
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
		return new BlockStateContainer(this, FACING, STATE, TIMER);
	}

	private EnumFacing getTrackDirection(IBlockAccess worldIn, BlockPos pos) {
		if (TrackToolsAPI.isRailBlockAt(worldIn, pos.north()))
			return EnumFacing.NORTH;
		if (TrackToolsAPI.isRailBlockAt(worldIn, pos.east()))
			return EnumFacing.EAST;
		if (TrackToolsAPI.isRailBlockAt(worldIn, pos.south()))
			return EnumFacing.SOUTH;
		if (TrackToolsAPI.isRailBlockAt(worldIn, pos.west()))
			return EnumFacing.WEST;
		return null;
	}

	private void setDoors(World worldIn, BlockPos pos, boolean open) {
//		final EnumFacing trackDirection = getTrackDirection(worldIn, pos);
//
//		final List<EntityMinecart> train = worldIn.getEntitiesWithinAABB(EntityMinecart.class, new AxisAlignedBB(pos));
//		if (!train.isEmpty())
//			LinkageManager.INSTANCE.streamTrain(train.get(0)).filter(t -> t instanceof EntityTrain).forEach(t -> ((EntityTrain) t).setDoors(open ? trackDirection : null));
//
//		if (trackDirection != null) {
//			final BlockPos platformPos = pos.offset(trackDirection.getOpposite());
//			final IBlockState platformState = worldIn.getBlockState(platformPos);
//			if (platformState.getBlock() instanceof BlockPlatform) {
//				((BlockPlatform) platformState.getBlock()).updateOpen(worldIn, platformState, platformPos, EnumFacing.NORTH, open);
//				((BlockPlatform) platformState.getBlock()).updateOpen(worldIn, platformState, platformPos, EnumFacing.EAST, open);
//				((BlockPlatform) platformState.getBlock()).updateOpen(worldIn, platformState, platformPos, EnumFacing.SOUTH, open);
//				((BlockPlatform) platformState.getBlock()).updateOpen(worldIn, platformState, platformPos, EnumFacing.WEST, open);
//			}
//		}
	}

	private enum EnumTrainState implements IStringSerializable {

		OPENING_SOON("opening_soon"), OPENED("opened"), CLOSED("closed"), LEAVING("leaving"), GONE("gone");

		private final String name;

		EnumTrainState(String nameIn) {
			name = nameIn;
		}

		@Override
		public String getName() {
			return name;
		}
	}
}
