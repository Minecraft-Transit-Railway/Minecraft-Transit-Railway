package MTR.items;

import java.util.List;

import MTR.ItemBase;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ItemKillTrain extends ItemBase {

	private static final String[] name = { "ItemKillTrain" };

	public ItemKillTrain() {
		super(name);
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
		list.add(I18n.format("gui.killtrain", new Object[0]));
	}
}
