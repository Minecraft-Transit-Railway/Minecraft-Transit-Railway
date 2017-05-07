package MTR.blocks;

import java.util.Random;

import MTR.MTR;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockEscalatorSideLanding extends BlockWithDirection {

	private static final String name = "BlockEscalatorSideLanding";
	public static final PropertyBool SIDE = PropertyBool.create("side");

	public BlockEscalatorSideLanding() {
		super();
		GameRegistry.registerBlock(this, name);
		setCreativeTab(null);
		setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(SIDE, false));
		setUnlocalizedName(name);
	}

	@Override
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
		EnumFacing facing = state.getValue(FACING);
		BlockPos posSide = pos.offset(state.getValue(SIDE) ? facing.getOpposite() : facing);
		if (!(worldIn.getBlockState(pos.down()).getBlock() instanceof BlockEscalatorLanding
				&& worldIn.getBlockState(posSide).getBlock() instanceof BlockEscalatorSideLanding
				&& worldIn.getBlockState(posSide.down()).getBlock() instanceof BlockEscalatorLanding)) {
			worldIn.setBlockToAir(pos);
			worldIn.setBlockToAir(pos.up(2));
		}
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos) {
		EnumFacing var3 = access.getBlockState(pos).getValue(FACING);
		boolean side = access.getBlockState(pos).getValue(SIDE);
		switch (var3.getHorizontalIndex()) {
		case 0:
			setBlockBounds(0.5F, 0.0F, side ? 0.75F : 0.0F, 1.0F, 1.0F, side ? 1.0F : 0.25F);
			break;
		case 1:
			setBlockBounds(side ? 0.0F : 0.75F, 0.0F, 0.5F, side ? 0.25F : 1.0F, 1.0F, 1.0F);
			break;
		case 2:
			setBlockBounds(0.0F, 0.0F, side ? 0.0F : 0.75F, 0.5F, 1.0F, side ? 0.25F : 1.0F);
			break;
		case 3:
			setBlockBounds(side ? 0.75F : 0.0F, 0.0F, 0.0F, side ? 1.0F : 0.25F, 1.0F, 0.5F);
			break;
		}
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta % 4)).withProperty(SIDE,
				(meta & 4) > 0);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int var3 = state.getValue(FACING).getHorizontalIndex();
		if (state.getValue(SIDE))
			var3 = var3 + 4;
		return var3;
	}

	@Override
	public BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { FACING, SIDE });
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return null;
	}

	@Override
	public Item getItem(World worldIn, BlockPos pos) {
		return MTR.itemescalator;
	}

	public String getName() {
		return name;
	}
}
