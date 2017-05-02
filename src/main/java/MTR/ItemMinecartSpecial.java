package MTR;

import java.util.List;
import java.util.UUID;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemMinecartSpecial extends Item {

	public static final String name = "ItemMinecartSpecial";

	public ItemMinecartSpecial() {
		maxStackSize = 1;
		setCreativeTab(MTR.MTRtab);
		GameRegistry.registerItem(this, name);
		setUnlocalizedName(name);
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
		list.add(I18n.format("gui.minecartspecial", new Object[0]));
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side,
			float hitX, float hitY, float hitZ) {
		IBlockState state = worldIn.getBlockState(pos);
		if (state.getBlock() instanceof BlockRailBooster) {
			int var3 = state.getValue(BlockRailBooster.ROTATION);
			int angle = (int) (-45D * var3 + 450D) % 360;
			EntityMinecartSpecial main1 = new EntityMinecartSpecial(worldIn, pos.getX() + 0.5D, pos.getY() + 0.0625D,
					pos.getZ() + 0.5D);
			if (!worldIn.isRemote) {
				main1.setID(new UUID(0, 0), new UUID(0, 0));
				main1.rotationYaw = angle;
				worldIn.spawnEntityInWorld(main1);
			}
		}
		return true;
	}

	public static String getName() {
		return name;
	}
}