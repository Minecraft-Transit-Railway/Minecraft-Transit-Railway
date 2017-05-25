package MTR.blocks;

import MTR.MTR;
import MTR.TileEntityNextTrainEntity;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockNextTrain extends Block implements ITileEntityProvider {

	private static final String name = "BlockNextTrain";

	public BlockNextTrain() {
		super(Material.rock);
		// GameRegistry.registerBlock(this, name);
		setCreativeTab(MTR.MTRTab);
		setUnlocalizedName(name);
	}

	@Override
	public TileEntity createNewTileEntity(World arg0, int arg1) {
		return new TileEntityNextTrainEntity();
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		ItemStack itemStack = playerIn.inventory.getCurrentItem();
		if (itemStack != null && itemStack.getItem() == MTR.itembrush) {
			TileEntityNextTrainEntity te = (TileEntityNextTrainEntity) worldIn.getTileEntity(pos);
			if (worldIn.isRemote)
				MTR.proxy.openGUI(te);
			return true;
		} else
			return false;
	}

	public static String getName() {
		return name;
	}
}
