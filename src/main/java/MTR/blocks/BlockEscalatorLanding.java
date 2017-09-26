package MTR.blocks;

import MTR.BlockBase;
import MTR.MTRBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockSandStone;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockEscalatorLanding extends BlockBase {

	private static final String name = "BlockEscalatorLanding";

	public BlockEscalatorLanding() {
		super(Material.ROCK, name);
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
		if (!(worldIn.getBlockState(pos.up()).getBlock() instanceof BlockEscalatorSideLanding))
			worldIn.setBlockToAir(pos);
		BlockPos posStep = pos;
		if (worldIn.getBlockState(pos.north()).getBlock() instanceof BlockEscalatorStep)
			posStep = pos.north();
		if (worldIn.getBlockState(pos.east()).getBlock() instanceof BlockEscalatorStep)
			posStep = pos.east();
		if (worldIn.getBlockState(pos.south()).getBlock() instanceof BlockEscalatorStep)
			posStep = pos.south();
		if (worldIn.getBlockState(pos.west()).getBlock() instanceof BlockEscalatorStep)
			posStep = pos.west();
		try {
			IBlockState stateStep = worldIn.getBlockState(posStep);
			BlockEscalatorStep blockStep = (BlockEscalatorStep) stateStep.getBlock();
			Block blockUnder = worldIn.getBlockState(pos.down(2)).getBlock();
			if (stateStep.getValue(BlockEscalatorStep.FACING) == worldIn.getBlockState(pos.up())
					.getValue(BlockEscalatorSideLanding.FACING)) {
				int initial = stateStep.getValue(BlockEscalatorStep.UP);
				if (worldIn.isBlockPowered(pos.down())) {
					if (2 != initial && blockUnder instanceof BlockSandStone) {
						worldIn.setBlockState(posStep, stateStep.withProperty(BlockEscalatorStep.UP, 2));
						blockStep.updateNeighbors(worldIn, posStep);
					}
					if (1 != initial && blockUnder instanceof BlockSand) {
						worldIn.setBlockState(posStep, stateStep.withProperty(BlockEscalatorStep.UP, 1));
						blockStep.updateNeighbors(worldIn, posStep);
					}
				} else if (initial != 0) {
					worldIn.setBlockState(posStep, stateStep.withProperty(BlockEscalatorStep.UP, 0));
					blockStep.updateNeighbors(worldIn, posStep);
				}
			}
		} catch (Exception e) {
		}
	}

	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ,
			int meta, EntityLivingBase placer) {
		EnumFacing var3 = placer.getHorizontalFacing().rotateY();
		BlockPos posRight = pos;
		switch (var3) {
		case NORTH:
			posRight = pos.add(0, 0, -1);
			break;
		case SOUTH:
			posRight = pos.add(0, 0, 1);
			break;
		case EAST:
			posRight = pos.add(1, 0, 0);
			break;
		case WEST:
			posRight = pos.add(-1, 0, 0);
			break;
		default:
		}
		if (worldIn.getBlockState(posRight).getBlock().isReplaceable(worldIn, posRight)
				&& worldIn.getBlockState(pos.up()).getBlock().isReplaceable(worldIn, pos.up())
				&& worldIn.getBlockState(posRight.up()).getBlock().isReplaceable(worldIn, posRight.up())
				&& worldIn.getBlockState(pos.up(3)).getBlock().isReplaceable(worldIn, pos.up(3))
				&& worldIn.getBlockState(posRight.up(3)).getBlock().isReplaceable(worldIn, posRight.up(3))) {
			PropertyDirection FACING = BlockWithDirection.FACING;
			PropertyBool SIDE = BlockEscalatorSideLanding.SIDE;
			worldIn.setBlockState(posRight, getDefaultState());
			worldIn.setBlockState(pos.up(), MTRBlocks.blockescalatorsidelanding.getDefaultState()
					.withProperty(FACING, var3).withProperty(SIDE, false));
			worldIn.setBlockState(posRight.up(), MTRBlocks.blockescalatorsidelanding.getDefaultState()
					.withProperty(FACING, var3).withProperty(SIDE, true));
			worldIn.setBlockState(pos.up(3), MTRBlocks.blockescalatorsign.getDefaultState().withProperty(FACING, var3));
			worldIn.setBlockState(posRight.up(3),
					MTRBlocks.blockescalatorsign.getDefaultState().withProperty(FACING, var3.getOpposite()));
			return super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer);
		} else
			return Blocks.AIR.getDefaultState();
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
}