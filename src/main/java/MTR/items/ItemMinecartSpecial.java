package MTR.items;

import java.util.List;
import java.util.UUID;

import MTR.EntityMinecartSpecial;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemMinecartSpecial extends ItemSpawnTrain<EntityMinecartSpecial> {

	private static final String[] name = { "ItemMinecartSpecial" };

	public ItemMinecartSpecial() {
		super(name);
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
		list.add(I18n.format("gui.minecartspecial", new Object[0]));
	}

	@Override
	protected void spawnTrain(World worldIn, ItemStack stack, BlockPos pos, int angle) {
		EntityMinecartSpecial main1 = new EntityMinecartSpecial(worldIn, pos.getX() + 0.5D, pos.getY() + 0.0625D,
				pos.getZ() + 0.5D, true, 0);
		if (!worldIn.isRemote) {
			main1.setID(new UUID(0, 0), new UUID(0, 0));
			main1.rotationYaw = angle;
			worldIn.spawnEntityInWorld(main1);
		}
	}

	@Override
	protected int[] getTrainOrder(ItemStack stack) {
		int[] order = { 1 };
		return order;
	}
}