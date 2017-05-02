package MTR;

import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemTrain extends Item {

	public static final String name = "ItemTrain";

	public ItemTrain() {
		// setCreativeTab(MTR.MTRtab);
		GameRegistry.registerItem(this, name);
		setUnlocalizedName(name);
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
		list.add(I18n.format("gui.finder", new Object[0]));
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side,
			float hitX, float hitY, float hitZ) {
		if (!world.isRemote)
			player.addChatComponentMessage(
					new ChatComponentText("It is a case where the nearest MTR station is at..."));
		return true;
	}

	public static String getName() {
		return name;
	}
}
