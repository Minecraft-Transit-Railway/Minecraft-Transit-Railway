package MTR.items;

import java.util.List;

import MTR.EntitySP1900;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemSP1900 extends ItemSpawnTrain<EntitySP1900> {

	private static final String[] name = { "ItemSP19001", "ItemSP19002", "ItemSP19003", "ItemSP19004" };
	private static final String name2 = "ItemSP1900";

	public ItemSP1900() {
		super(name, name2);
		setHasSubtypes(true);
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
		String text = "";
		switch (stack.getMetadata()) {
		case 0:
			text = I18n.format("gui.sp1", new Object[0]);
			break;
		case 1:
			text = I18n.format("gui.sp2", new Object[0]);
			break;
		case 2:
			text = I18n.format("gui.sp3", new Object[0]);
			break;
		case 3:
			text = I18n.format("gui.sp4", new Object[0]);
			break;
		}
		list.add(getCars(stack) + " " + I18n.format("gui.car", new Object[0]));
		list.add("(" + text + ")");
	}

	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, List subItems) {
		for (int var4 = 0; var4 < 4; ++var4)
			subItems.add(new ItemStack(itemIn, 1, var4));
	}

	@Override
	protected EntitySP1900 createTrain(World worldIn, double x, double y, double z, boolean f, int h) {
		return new EntitySP1900(worldIn, x, y, z, f, h);
	}

	@Override
	protected int getCars(ItemStack stack) {
		switch (stack.getMetadata()) {
		case 0:
		default:
			return 4;
		case 1:
			return 7;
		case 2:
			return 8;
		case 3:
			return 12;
		}
	}

	@Override
	protected int[] getTrainOrder(ItemStack stack) {
		// 1 2 3 4 5 6 7
		// D P M H C F D
		int[] order4 = { 1, 2, 3, 7 };
		int[] order7 = { 1, 2, 3, 5, 3, 2, 7 };
		int[] order8 = { 1, 2, 3, 4, 5, 3, 2, 7 };
		int[] order12 = { 1, 2, 3, 4, 5, 4, 3, 2, 6, 3, 2, 7 };
		switch (stack.getMetadata()) {
		case 0:
			return order4;
		case 1:
			return order7;
		case 2:
			return order8;
		case 3:
		default:
			return order12;
		}
	}
}