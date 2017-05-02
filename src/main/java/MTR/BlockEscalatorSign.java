package MTR;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockEscalatorSign extends BlockWithDirection {

	private static final String name = "BlockEscalatorSign";
	public static final PropertyInteger SIGN = PropertyInteger.create("sign", 0, 2);
	public static final PropertyBool TYPE = PropertyBool.create("type");

	protected BlockEscalatorSign() {
		super();
		GameRegistry.registerBlock(this, name);
		setCreativeTab(null);
		setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(SIGN, 0)
				.withProperty(TYPE, false));
		setUnlocalizedName(name);
		setLightLevel(0.3125F);
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		int sign = 0;
		try {
			IBlockState stateSide = worldIn.getBlockState(pos.down(2));
			boolean type = state.getValue(TYPE);
			EnumFacing facingSide = stateSide.getValue(BlockEscalatorSideLanding.FACING);
			IBlockState stateStep = worldIn.getBlockState(pos.down(3).offset(facingSide.rotateYCCW()));
			EnumFacing facingStep = stateStep.getValue(BlockEscalatorStep.FACING);
			int direction = stateStep.getValue(BlockEscalatorStep.UP);
			if (direction == 1 && facingSide != facingStep || direction == 2 && facingSide == facingStep)
				sign = type ? 2 : 1;
		} catch (Exception e) {
		}
		return state.withProperty(SIGN, sign);
	}

	@Override
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
		if (!(worldIn.getBlockState(pos.offset(state.getValue(FACING))).getBlock() instanceof BlockEscalatorSign))
			worldIn.setBlockToAir(pos);
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos) {
		EnumFacing var3 = access.getBlockState(pos).getValue(FACING);
		switch (var3.getHorizontalIndex()) {
		case 0:
			setBlockBounds(0.4375F, 0.0F, 0.46875F, 0.5625F, 0.625F, 1.0F);
			break;
		case 1:
			setBlockBounds(0.0F, 0.0F, 0.4375F, 0.53125F, 0.625F, 0.5625F);
			break;
		case 2:
			setBlockBounds(0.4375F, 0.0F, 0.0F, 0.5625F, 0.625F, 0.53125F);
			break;
		case 3:
			setBlockBounds(0.46875F, 0.0F, 0.4375F, 1.0F, 0.625F, 0.5625F);
			break;
		}
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		ItemStack itemStack = playerIn.inventory.getCurrentItem();
		if (itemStack != null && itemStack.getItem() == MTR.itembrush) {
			EnumFacing facing = state.getValue(FACING);
			worldIn.setBlockState(pos.offset(facing), worldIn.getBlockState(pos.offset(facing)).cycleProperty(TYPE));
			worldIn.setBlockState(pos,
					state.withProperty(TYPE, worldIn.getBlockState(pos.offset(facing)).getValue(TYPE)));
			return true;
		} else
			return false;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta % 4)).withProperty(TYPE,
				(meta & 4) > 0);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int var3 = state.getValue(FACING).getHorizontalIndex();
		if (state.getValue(TYPE))
			var3 = var3 + 4;
		return var3;
	}

	@Override
	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { FACING, SIGN, TYPE });
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
