package MTR.items;

import java.util.List;

import MTR.ItemBase;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class ItemTrain extends ItemBase {

	private static final String[] name = { "ItemTrain" };

	public ItemTrain() {
		super(name);
		setCreativeTab(null);
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
		list.add(I18n.format("gui.finder", new Object[0]));
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!worldIn.isRemote)
			playerIn.addChatComponentMessage(
					new TextComponentString("It is a case where the nearest MTR station is at..."));
		return EnumActionResult.PASS;
	}
}
