package MTR;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockRailSlope1 extends BlockRailBase2 {

	private static final String name = "BlockRailSlope1";

	public static final PropertyInteger LEVEL = PropertyInteger.create("level", 0, 15);
	public static final PropertyInteger ROTATION = PropertyInteger.create("rotation", 0, 7);

	protected BlockRailSlope1() {
		super();
		GameRegistry.registerBlock(this, name);
		setDefaultState(blockState.getBaseState().withProperty(LEVEL, 0).withProperty(ROTATION, 0));
		setUnlocalizedName(name);
		setBlockBounds(0, 0, 0, 1, 1, 1);
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		IBlockState state2 = worldIn.getBlockState(pos.add(0, 1, 0));
		int var3 = 0;
		if (state2.getBlock() instanceof BlockRailSlope2)
			var3 = (Integer) state2.getValue(BlockRailSlope2.ROTATION);
		return state.withProperty(ROTATION, var3);
	}

	@Override
	public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List list,
			Entity collidingEntity) {
		setBlockBounds(0, 0, 0, 1, (Integer) state.getValue(LEVEL) / 16F, 1);
		super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
		setBlockBounds(0, 0, 0, 1, 1, 1);
	}

	@Override
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
		if (!(worldIn.getBlockState(pos.add(0, 1, 0)).getBlock() instanceof BlockRailSlope2))
			breakOtherBlocks(worldIn, pos);
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
		worldIn.setBlockToAir(pos.add(0, 1, 0));
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
		if (block instanceof BlockRailSlope1)
			((BlockRailSlope1) block).breakOtherBlocks(worldIn, pos);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(LEVEL, meta);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return (Integer) state.getValue(LEVEL);
	}

	@Override
	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { LEVEL, ROTATION });
	}

	@Override
	public int damageDropped(IBlockState state) {
		return 2;
	}

	public static String getName() {
		return name;
	}
}