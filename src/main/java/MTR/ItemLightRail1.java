package MTR;

import java.util.List;
import java.util.UUID;

import net.minecraft.block.BlockDispenser;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemLightRail1 extends Item {

	public static final String name = "ItemLightRail1";

	public ItemLightRail1() {
		setHasSubtypes(true);
		maxStackSize = 1;
		setCreativeTab(MTR.MTRtab);
		GameRegistry.registerItem(this, name);
		setUnlocalizedName(name);
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
		list.add(stack.getMetadata() == 0 ? I18n.format("gui.lrt1", new Object[0])
				: I18n.format("gui.lrt2", new Object[0]));
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side,
			float hitX, float hitY, float hitZ) {
		IBlockState state = worldIn.getBlockState(pos);
		if (state.getBlock() instanceof BlockRailBooster) {
			int var3 = state.getValue(BlockRailBooster.ROTATION);
			int angle = (int) (-45D * var3 + 450D) % 360;
			double a2 = Math.toRadians(angle - 90);
			EntityLightRail1 main1 = new EntityLightRail1(worldIn, pos.getX() + 0.5D, pos.getY() + 0.0625D,
					pos.getZ() + 0.5D);
			EntityLightRail1 main2 = new EntityLightRail1(worldIn, pos.getX() + 0.5D + 23D * Math.sin(a2),
					pos.getY() + 0.0625D, pos.getZ() + 0.5D + 23D * Math.cos(a2));

			if (!worldIn.isRemote) {
				main1.setID(new UUID(0, 0), new UUID(0, 0));
				main1.rotationYaw = angle;
				worldIn.spawnEntityInWorld(main1);
				if (stack.getMetadata() == 1) {
					main1.setID(new UUID(0, 0), main2.getUniqueID());
					main2.setID(main1.getUniqueID(), new UUID(0, 0));
					main2.rotationYaw = angle;
					worldIn.spawnEntityInWorld(main2);
				}
			}
		}
		return true;
	}

	public static String getName() {
		return name;
	}

	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, List subItems) {
		for (int var4 = 0; var4 < 2; ++var4)
			subItems.add(new ItemStack(itemIn, 1, var4));
	}
}