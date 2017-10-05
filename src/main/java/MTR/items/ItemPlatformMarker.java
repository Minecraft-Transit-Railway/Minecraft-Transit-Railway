package MTR.items;

import java.util.List;

import MTR.ItemBase;
import MTR.MTR;
import MTR.MessageWorldData;
import MTR.PlatformData;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemPlatformMarker extends ItemBase {

	private static final String[] name = { "ItemPlatformMarker" };

	public ItemPlatformMarker() {
		super(name);
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
		list.add(I18n.format("gui.platformmark1", new Object[0]));
		list.add(I18n.format("gui.platformmark2", new Object[0]));
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		PlatformData data = PlatformData.get(worldIn);
		if (!worldIn.isRemote)
			MTR.network.sendToAll(new MessageWorldData(data.platformX, data.platformY, data.platformZ,
					data.platformAlias, data.platformNumber, data.arrivals));
		// data = PlatformData.get(worldIn);
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		// boolean repeat = false;
		// int j = 0;
		// for (int i = 0; i < 1000; i++)
		// if (data.platformX[i] == x && data.platformY[i] == y && data.platformZ[i] ==
		// z) {
		// repeat = true;
		// j = i;
		// }
		// if (repeat) {
		// int station = data.platformAlias[j];
		// playerIn.addChatComponentMessage(
		// new TextComponentString(I18n.format("gui.platformregistered", new Object[0])
		// + ":" + '\n'
		// + I18n.format("station." + station + "c", new Object[0]) + " "
		// + I18n.format("station." + station, new Object[0]) + " - "
		// + I18n.format("gui.platform", new Object[0]) + " " +
		// data.platformNumber[j]));
		// } else
		if (worldIn.isRemote && !playerIn.isSneaking())
			MTR.proxy.openGUI(x, y, z);
		return EnumActionResult.PASS;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn,
			EnumHand hand) {
		if (playerIn.isSneaking())
			if (!worldIn.isRemote) {
				PlatformData data = PlatformData.get(worldIn);
				MTR.network.sendToAll(new MessageWorldData(data.platformX, data.platformY, data.platformZ,
						data.platformAlias, data.platformNumber, data.arrivals));
			} else {
				PlatformData data = PlatformData.get(worldIn);
				MTR.proxy.openGUI(data, worldIn);
			}
		return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStackIn);
	}
}
