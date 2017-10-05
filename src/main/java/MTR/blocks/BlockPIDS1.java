package MTR.blocks;

import MTR.MTR;
import MTR.MessageWorldData;
import MTR.PlatformData;
import MTR.TileEntityPIDS1Entity;
import MTR.items.ItemBrush;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPIDS1 extends BlockWithDirection implements ITileEntityProvider {

	private static final String name = "BlockPIDS1";

	public BlockPIDS1() {
		super(name);
		setLightLevel(0.3125F);
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
		BlockPos pos2 = pos;
		switch (state.getValue(FACING)) {
		case NORTH:
			pos2 = pos.add(1, 0, 0);
			break;
		case EAST:
			pos2 = pos.add(0, 0, 1);
			break;
		case SOUTH:
			pos2 = pos.add(-1, 0, 0);
			break;
		case WEST:
			pos2 = pos.add(0, 0, -1);
			break;
		default:
			break;
		}
		if (!(worldIn.getBlockState(pos2).getBlock() instanceof BlockPIDS1))
			worldIn.setBlockToAir(pos);
	}

	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ,
			int meta, EntityLivingBase placer) {
		EnumFacing var9 = placer.getHorizontalFacing().rotateY();
		BlockPos pos2 = pos;
		switch (var9) {
		case NORTH:
			pos2 = pos.add(1, 0, 0);
			break;
		case EAST:
			pos2 = pos.add(0, 0, 1);
			break;
		case SOUTH:
			pos2 = pos.add(-1, 0, 0);
			break;
		case WEST:
			pos2 = pos.add(0, 0, -1);
			break;
		default:
			break;
		}
		if (worldIn.getBlockState(pos2).getBlock().isReplaceable(worldIn, pos2)) {
			worldIn.setBlockState(pos2, getDefaultState().withProperty(FACING, var9.getOpposite()));
			return super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(FACING, var9);
		} else
			return Blocks.AIR.getDefaultState();
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, ItemStack stack, EnumFacing side, float hitX, float hitY, float hitZ) {
		PlatformData data = PlatformData.get(worldIn);
		if (!worldIn.isRemote)
			MTR.network.sendToAll(new MessageWorldData(data.platformX, data.platformY, data.platformZ,
					data.platformAlias, data.platformNumber, data.arrivals));
		ItemStack itemStack = playerIn.inventory.getCurrentItem();
		if (worldIn.isRemote && itemStack != null && itemStack.getItem() instanceof ItemBrush) {
			data = PlatformData.get(worldIn);
			TileEntityPIDS1Entity te = (TileEntityPIDS1Entity) worldIn.getTileEntity(pos);
			MTR.proxy.openGUI(data, te);
			return true;
		}
		return false;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		EnumFacing var3 = state.getValue(FACING);
		if (var3.getAxis() == EnumFacing.Axis.X)
			return new AxisAlignedBB(0.375F, 0.0F, 0.0F, 0.625F, 1.0F, 1.0F);
		else
			return new AxisAlignedBB(0.0F, 0.0F, 0.375F, 1.0F, 1.0F, 0.625F);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityPIDS1Entity();
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).getHorizontalIndex();
	}
}
