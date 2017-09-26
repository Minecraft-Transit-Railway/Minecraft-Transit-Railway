package MTR.blocks;

import MTR.MTRBlocks;
import MTR.TileEntityRailEntity;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class BlockRailCurved extends BlockRailBase2 implements ITileEntityProvider {

	private static final String name = "BlockRailCurved";

	public BlockRailCurved() {
		super(name);
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
		int straight = 0;
		if (getSwitchPos(worldIn, pos) != null)
			straight = worldIn.getBlockState(getSwitchPos(worldIn, pos)).getValue(BlockRailSwitch.POWERED) ? 2 : 1;
		change(worldIn, pos, straight);
	}

	public static BlockPos getSwitchPos(World worldIn, BlockPos pos) {
		if (worldIn.getBlockState(pos.north()).getBlock() instanceof BlockRailSwitch)
			return pos.north();
		if (worldIn.getBlockState(pos.east()).getBlock() instanceof BlockRailSwitch)
			return pos.east();
		if (worldIn.getBlockState(pos.south()).getBlock() instanceof BlockRailSwitch)
			return pos.south();
		if (worldIn.getBlockState(pos.west()).getBlock() instanceof BlockRailSwitch)
			return pos.west();
		return null;
	}

	private void change(World worldIn, BlockPos pos, int straight) {
		int direction = worldIn.getBlockState(pos).getValue(ROTATION);
		int x = 0, z = 0;
		switch (direction) {
		case 0:
			z = 1;
			break;
		case 1:
			x = 1;
			z = -1;
			break;
		case 2:
			x = 1;
			break;
		case 3:
			x = 1;
			z = 1;
			break;
		}
		changeDummyRails(worldIn, pos, direction, straight, x, z);
		changeDummyRails(worldIn, pos, direction, straight, -x, -z);
	}

	private void changeDummyRails(World worldIn, BlockPos pos, int direction, int straight, int x, int z) {
		int a = x, b = z;
		while (worldIn.getBlockState(pos.add(a, 0, b)).getBlock() instanceof BlockRailDummy) {
			worldIn.setBlockState(pos.add(a, 0, b), MTRBlocks.blockraildummy.getDefaultState()
					.withProperty(BlockRailDummy.ROTATION, direction + 4 * straight));
			a += x;
			b += z;
		}
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
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityRailEntity();
	}
}