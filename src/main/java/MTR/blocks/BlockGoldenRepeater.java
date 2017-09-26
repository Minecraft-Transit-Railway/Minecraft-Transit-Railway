package MTR.blocks;

import java.util.Random;

import MTR.MTR;
import MTR.MTRBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneRepeater;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockGoldenRepeater extends BlockRedstoneRepeater {

	private static final String name = "BlockGoldenRepeater";

	public BlockGoldenRepeater(boolean powered) {
		super(powered);
		if (!powered)
			setCreativeTab(MTR.MTRTab);
		setUnlocalizedName(name + (powered ? "On" : "Off"));
		setRegistryName(name + (powered ? "On" : "Off"));
	}

	public void registerItemModel(ItemBlock itemBlock) {
		MTR.proxy.registerItemRenderer(itemBlock, 0, name + "Off");
		MTR.proxy.registerItemRenderer(itemBlock, 0, name + "On");
	}

	@Override
	protected IBlockState getUnpoweredState(IBlockState state) {
		Boolean locked = state.getValue(LOCKED);
		EnumFacing facing = state.getValue(FACING);
		return MTRBlocks.blockgoldenrepeateroff.getDefaultState().withProperty(FACING, facing).withProperty(LOCKED,
				locked);
	}

	@Override
	protected IBlockState getPoweredState(IBlockState state) {
		Boolean locked = state.getValue(LOCKED);
		EnumFacing facing = state.getValue(FACING);
		return MTRBlocks.blockgoldenrepeateron.getDefaultState().withProperty(FACING, facing).withProperty(LOCKED,
				locked);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		return false;
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
		if (canBlockStay(worldIn, pos))
			update(worldIn, pos, state);
		else {
			dropBlockAsItem(worldIn, pos, state, 0);
			worldIn.setBlockToAir(pos);
			for (EnumFacing enumfacing : EnumFacing.values())
				worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this);
		}
	}

	private void update(World worldIn, BlockPos pos, IBlockState state) {
		if (!isLocked(worldIn, pos, state)) {
			boolean flag = shouldBePowered(worldIn, pos, state);
			if (isRepeaterPowered && !flag) {
				worldIn.setBlockState(pos, getUnpoweredState(state), 2);
				worldIn.notifyNeighborsOfStateChange(pos.up(), this);
			} else if (!isRepeaterPowered && flag) {
				worldIn.setBlockState(pos, getPoweredState(state), 2);
				worldIn.notifyNeighborsOfStateChange(pos.up(), this);
			}
		}
	}

	@Override
	protected int getTickDelay(IBlockState state) {
		return 0;
	}

	@Override
	protected int getDelay(IBlockState state) {
		return 0;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).getHorizontalIndex();
	}

	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
		return new ItemStack(MTRBlocks.blockgoldenrepeateroff);
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return new ItemStack(MTRBlocks.blockgoldenrepeateroff).getItem();
	}
}
