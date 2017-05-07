package MTR.blocks;

import MTR.TileEntityRailEntity;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockRailCurved extends BlockRailBase2 implements ITileEntityProvider {

	private static final String name = "BlockRailCurved";
	public static final PropertyInteger ROTATION = PropertyInteger.create("rotation", 0, 15);

	public BlockRailCurved() {
		super();
		GameRegistry.registerBlock(this, name);
		setUnlocalizedName(name);
	}

	@Override
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
		breakOtherBlocks(worldIn, pos);
	}

	@Override
	public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn) {
		breakOtherBlocks(worldIn, pos);
	}

	public void breakOtherBlocks(World worldIn, BlockPos pos) {
		worldIn.setBlockToAir(pos);
		BlockPos pos1 = pos.add(1, 0, 0), pos2 = pos.add(0, 0, 1), pos3 = pos.add(-1, 0, 0), pos4 = pos.add(0, 0, -1);
		BlockPos pos5 = pos.add(1, 0, 1), pos6 = pos.add(1, 0, -1), pos7 = pos.add(-1, 0, 1), pos8 = pos.add(-1, 0, -1);
		Block block1 = worldIn.getBlockState(pos1).getBlock();
		Block block2 = worldIn.getBlockState(pos2).getBlock();
		Block block3 = worldIn.getBlockState(pos3).getBlock();
		Block block4 = worldIn.getBlockState(pos4).getBlock();
		Block block5 = worldIn.getBlockState(pos5).getBlock();
		Block block6 = worldIn.getBlockState(pos6).getBlock();
		Block block7 = worldIn.getBlockState(pos7).getBlock();
		Block block8 = worldIn.getBlockState(pos8).getBlock();
		breakOtherBlocks2(worldIn, block1, pos1);
		breakOtherBlocks2(worldIn, block2, pos2);
		breakOtherBlocks2(worldIn, block3, pos3);
		breakOtherBlocks2(worldIn, block4, pos4);
		breakOtherBlocks2(worldIn, block5, pos5);
		breakOtherBlocks2(worldIn, block6, pos6);
		breakOtherBlocks2(worldIn, block7, pos7);
		breakOtherBlocks2(worldIn, block8, pos8);
	}

	private void breakOtherBlocks2(World worldIn, Block block, BlockPos pos) {
		if (block instanceof BlockRailDummy)
			((BlockRailDummy) block).breakOtherBlocks(worldIn, pos);
		if (block instanceof BlockRailCurved)
			((BlockRailCurved) block).breakOtherBlocks(worldIn, pos);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(ROTATION, meta % 8);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return (Integer) state.getValue(ROTATION);
	}

	@Override
	public BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { ROTATION });
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityRailEntity();
	}

	public static String getName() {
		return name;
	}
}