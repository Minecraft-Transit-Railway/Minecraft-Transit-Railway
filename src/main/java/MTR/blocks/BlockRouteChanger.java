package MTR.blocks;

import MTR.MTR;
import MTR.TileEntityRouteChangerEntity;
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
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockRouteChanger extends Block implements ITileEntityProvider {

	private static final String name = "BlockRouteChanger";

	public BlockRouteChanger() {
		super(Material.rock);
		setHardness(5F);
		GameRegistry.registerBlock(this, name);
		setCreativeTab(MTR.MTRTab);
		setUnlocalizedName(name);
	}

	@Override
	public TileEntity createNewTileEntity(World arg0, int arg1) {
		return new TileEntityRouteChangerEntity();
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		ItemStack itemStack = playerIn.inventory.getCurrentItem();
		if (itemStack != null && itemStack.getItem() == MTR.itembrush) {
			TileEntityRouteChangerEntity te = (TileEntityRouteChangerEntity) worldIn.getTileEntity(pos);
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
