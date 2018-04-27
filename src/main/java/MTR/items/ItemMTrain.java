package MTR.items;

import java.util.List;

import MTR.EntityMTrain;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemMTrain extends ItemSpawnTrain<EntityMTrain> {

	private static final String[] name = { "ItemMTrain1", "ItemMTrain2" };
	private static final String name2 = "ItemMTrain";

	public ItemMTrain() {
		super(name, name2);
		setHasSubtypes(true);
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
		list.add(getCars(stack) + " " + I18n.format("gui.car", new Object[0]));
	}

	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, List subItems) {
		for (int var4 = 0; var4 < 2; ++var4)
			subItems.add(new ItemStack(itemIn, 1, var4));
	}

	@Override
	protected EntityMTrain createTrain(World worldIn, double x, double y, double z, boolean f, int h) {
		return new EntityMTrain(worldIn, x, y, z, f, h);
	}

	@Override
	protected int getCars(ItemStack stack) {
		return stack.getMetadata() == 0 ? 4 : 8;
	}

	@Override
	protected int[] getTrainOrder(ItemStack stack) {
		// 1 2 3 4 2 3 2 1
		// A C D B C D C A
		// int[] order4 = { 1, 2, 2, 1 };
		// int[] order8 = { 1, 2, 3, 4, 2, 3, 2, 1 };
		int[] order4 = { 1, 1, 1, 1 };
		int[] order8 = { 1, 1, 1, 1, 1, 1, 1, 1 };
		switch (stack.getMetadata()) {
		case 0:
		default:
			return order4;
		case 1:
			return order8;
		}
	}
}