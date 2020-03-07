package mtr.block;

import mtr.Blocks;
import mtr.MTR;
import mtr.tile.TileEntityBridgeCreator;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class BlockBridgeCreator extends BlockContainer {

	public static final PropertyBool BURNING = PropertyBool.create("burning");

	private static boolean keepInventory;

	public BlockBridgeCreator() {
		super(Material.ROCK);
		setHardness(2);
		setCreativeTab(CreativeTabs.DECORATIONS);
		setDefaultState(blockState.getBaseState().withProperty(BURNING, false));
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!worldIn.isRemote) {
			final TileEntity tileEntity = worldIn.getTileEntity(pos);
			if (tileEntity instanceof TileEntityBridgeCreator)
				playerIn.openGui(MTR.instance, 0, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}

	@Override
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		if (stateIn.getValue(BURNING)) {
			final double d0 = pos.getX() + 0.5;
			final double d1 = pos.getY() + rand.nextDouble() * 6 / 16;
			final double d2 = pos.getZ() + 0.5;
			final double d3 = 0.52;
			final double d4 = rand.nextDouble() * 0.6 - 0.3;

			worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 - 0.52D, d1, d2 + d4, 0, 0, 0);
			worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 - 0.52, d1, d2 + d4, 0, 0, 0);

			worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + 0.52D, d1, d2 + d4, 0, 0, 0);
			worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + 0.52, d1, d2 + d4, 0, 0, 0);

			worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d4, d1, d2 - 0.52, 0, 0, 0);
			worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + d4, d1, d2 - 0.52, 0, 0, 0);

			worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d4, d1, d2 + 0.52, 0, 0, 0);
			worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + d4, d1, d2 + 0.52, 0, 0, 0);
		}
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		if (!keepInventory) {
			final TileEntity tileentity = worldIn.getTileEntity(pos);
			if (tileentity instanceof TileEntityBridgeCreator) {
				InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityBridgeCreator) tileentity);
				worldIn.updateComparatorOutputLevel(pos, this);
			}
		}
		super.breakBlock(worldIn, pos, state);
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(BURNING, meta > 0);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(BURNING) ? 1 : 0;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityBridgeCreator();
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, BURNING);
	}

	public static void setState(boolean burning, World worldIn, BlockPos pos) {
		final TileEntity tileEntity = worldIn.getTileEntity(pos);
		keepInventory = true;
		worldIn.setBlockState(pos, Blocks.bridge_creator.setLightLevel(burning ? 0.8125F : 0).getDefaultState().withProperty(BURNING, burning), 3);
		keepInventory = false;
		if (tileEntity != null) {
			tileEntity.validate();
			worldIn.setTileEntity(pos, tileEntity);
		}
	}
}
