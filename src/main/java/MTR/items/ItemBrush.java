package MTR.items;

import java.util.List;

import MTR.MTR;
import MTR.blocks.BlockRailStraight;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemBrush extends Item {

	public static final String name = "ItemBrush";

	public ItemBrush() {
		setCreativeTab(MTR.MTRTab);
		GameRegistry.registerItem(this, name);
		setUnlocalizedName(name);
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side,
			float hitX, float hitY, float hitZ) {
		Block blockIn = worldIn.getBlockState(pos).getBlock();
		if (blockIn instanceof BlockRailStraight) {
			BlockRailStraight block = (BlockRailStraight) blockIn;
			int rotation = (Integer) worldIn.getBlockState(pos).getValue(BlockRailStraight.ROTATION);
			block.destroy1(worldIn, pos, 10, rotation);
			block.destroy2(worldIn, pos, 10, rotation);
		}
		return true;
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
		list.add(I18n.format("gui.brush", new Object[0]));
	}

	public static String getName() {
		return name;
	}
}
