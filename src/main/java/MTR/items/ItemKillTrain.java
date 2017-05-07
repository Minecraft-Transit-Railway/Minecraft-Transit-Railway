package MTR.items;

import java.util.List;

import MTR.MTR;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemKillTrain extends Item {

	public static final String name = "ItemKillTrain";

	public ItemKillTrain() {
		setCreativeTab(MTR.MTRtab);
		GameRegistry.registerItem(this, name);
		setUnlocalizedName(name);
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
		list.add(I18n.format("gui.killtrain", new Object[0]));
	}

	public static String getName() {
		return name;
	}
}
