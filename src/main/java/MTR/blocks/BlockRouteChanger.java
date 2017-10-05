package MTR.blocks;

import MTR.BlockBase;
import MTR.MTR;
import MTR.TileEntityRouteChangerEntity;
import MTR.items.ItemBrush;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockRouteChanger extends BlockBase implements ITileEntityProvider {

	private static final String name = "BlockRouteChanger";

	public BlockRouteChanger() {
		super(Material.ROCK, name);
	}

	@Override
	public TileEntity createNewTileEntity(World arg0, int arg1) {
		return new TileEntityRouteChangerEntity();
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, ItemStack stack, EnumFacing side, float hitX, float hitY, float hitZ) {
		ItemStack itemStack = playerIn.inventory.getCurrentItem();
		if (worldIn.isRemote && itemStack != null && itemStack.getItem() instanceof ItemBrush) {
			TileEntityRouteChangerEntity te = (TileEntityRouteChangerEntity) worldIn.getTileEntity(pos);
			MTR.proxy.openGUI(te);
			return true;
		}
		return false;
	}
}
