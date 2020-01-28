package mtr.block;

import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Stream;

import mods.railcraft.common.blocks.tracks.TrackTools;
import mods.railcraft.common.carts.LinkageManager;
import mtr.entity.EntityTrain;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockDoorController extends Block {

	public static final PropertyDirection FACING = PropertyDirection.create("facing");

	private TrainState trainState;
	private Supplier<Stream<EntityMinecart>> train;
	private static final float TOLERANCE = 0.01F;

	public BlockDoorController() {
		super(Material.ROCK);
		setHardness(2);
		setCreativeTab(CreativeTabs.REDSTONE);
		setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.UP));
	}

	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		if (entityIn instanceof EntityTrain && Math.abs(entityIn.motionX) < TOLERANCE && Math.abs(entityIn.motionZ) < TOLERANCE) {
			if (trainState == TrainState.GONE) {
				train = () -> LinkageManager.INSTANCE.streamTrain((EntityMinecart) entityIn).filter(t -> t instanceof EntityTrain);
				updateTick(worldIn, pos, state, null);
			}
		} else {
			trainState = TrainState.GONE;
		}
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		trainState = TrainState.GONE;
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		switch (trainState) {
		case OPENING_SOON:
			trainState = TrainState.OPENED;
			train.get().forEach(t -> ((EntityTrain) t).setDoors(true, true));
			worldIn.scheduleUpdate(pos, this, 60);
			break;
		case OPENED:
			trainState = TrainState.CLOSED;
			train.get().forEach(t -> ((EntityTrain) t).setDoors(false, false));
			worldIn.scheduleUpdate(pos, this, 40);
			break;
		case CLOSED:
			trainState = TrainState.LEAVING;
			worldIn.scheduleUpdate(pos, this, 40);
			break;
		case LEAVING:
			trainState = TrainState.GONE;
			break;
		case GONE:
			trainState = TrainState.OPENING_SOON;
			worldIn.scheduleUpdate(pos, this, 20);
			break;
		}
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		if (TrackTools.isRailBlockAt(worldIn, pos.north()))
			return state.withProperty(FACING, EnumFacing.NORTH);
		if (TrackTools.isRailBlockAt(worldIn, pos.east()))
			return state.withProperty(FACING, EnumFacing.EAST);
		if (TrackTools.isRailBlockAt(worldIn, pos.south()))
			return state.withProperty(FACING, EnumFacing.SOUTH);
		if (TrackTools.isRailBlockAt(worldIn, pos.west()))
			return state.withProperty(FACING, EnumFacing.WEST);
		return state.withProperty(FACING, EnumFacing.UP);
	}

	@Override
	public boolean canProvidePower(IBlockState state) {
		return true;
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return 0;
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
		return new BlockStateContainer(this, new IProperty[] { FACING });
	}

	private enum TrainState {
		OPENING_SOON, OPENED, CLOSED, LEAVING, GONE
	}
}
