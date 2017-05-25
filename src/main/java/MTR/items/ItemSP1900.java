package MTR.items;

import java.util.UUID;

import MTR.EntitySP1900;
import MTR.MTR;
import MTR.blocks.BlockRailBooster;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemSP1900 extends Item {

	public static final String name = "ItemSP1900";

	public ItemSP1900() {
		// setHasSubtypes(true);
		maxStackSize = 1;
		setCreativeTab(MTR.MTRTab);
		GameRegistry.registerItem(this, name);
		setUnlocalizedName(name);
	}

	// @Override
	// public void addInformation(ItemStack stack, EntityPlayer player, List
	// list, boolean par4) {
	// list.add(stack.getMetadata() == 1 ? I18n.format("gui.sp2", new Object[0])
	// : I18n.format("gui.sp1", new Object[0]));
	// }

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side,
			float hitX, float hitY, float hitZ) {
		IBlockState state = worldIn.getBlockState(pos);
		if (state.getBlock() instanceof BlockRailBooster) {
			int var3 = state.getValue(BlockRailBooster.ROTATION);
			int angle = (-45 * var3 + 450) % 360;
			double a2 = Math.toRadians(angle - 90);
			EntitySP1900 main1 = new EntitySP1900(worldIn, pos.getX() + 0.5, pos.getY() + 0.0625, pos.getZ() + 0.5);
			EntitySP1900 main2 = new EntitySP1900(worldIn, pos.getX() + 0.5 + 25 * Math.sin(a2), pos.getY() + 0.0625,
					pos.getZ() + 0.5 + 25 * Math.cos(a2));
			EntitySP1900 main3 = new EntitySP1900(worldIn, pos.getX() + 0.5 + 50 * Math.sin(a2), pos.getY() + 0.0625,
					pos.getZ() + 0.5 + 50 * Math.cos(a2));
			EntitySP1900 main4 = new EntitySP1900(worldIn, pos.getX() + 0.5 + 75 * Math.sin(a2), pos.getY() + 0.0625,
					pos.getZ() + 0.5 + 75 * Math.cos(a2));

			if (!worldIn.isRemote) {
				main1.setID(new UUID(0, 0), main2.getUniqueID());
				main1.rotationYaw = angle;
				worldIn.spawnEntityInWorld(main1);
				main2.setID(main1.getUniqueID(), main3.getUniqueID());
				main2.rotationYaw = angle;
				worldIn.spawnEntityInWorld(main2);
				main3.setID(main2.getUniqueID(), main4.getUniqueID());
				main3.rotationYaw = angle;
				worldIn.spawnEntityInWorld(main3);
				main4.setID(main3.getUniqueID(), new UUID(0, 0));
				main4.rotationYaw = angle;
				worldIn.spawnEntityInWorld(main4);
			}
		}
		return true;
	}

	// @Override
	// public void getSubItems(Item itemIn, CreativeTabs tab, List subItems) {
	// for (int var4 = 0; var4 < 2; ++var4)
	// subItems.add(new ItemStack(itemIn, 1, var4));
	// }

	public static String getName() {
		return name;
	}
}