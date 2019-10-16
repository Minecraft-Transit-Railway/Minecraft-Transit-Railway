package mtr.item;

import java.util.List;

import javax.annotation.Nullable;

import mtr.entity.EntityTrain;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemCrowbar extends Item {

	public EntityTrain train = null;

	public ItemCrowbar() {
		super();
		setMaxStackSize(1);
		setCreativeTab(CreativeTabs.TOOLS);
		addPropertyOverride(new ResourceLocation("connecting"), new IItemPropertyGetter() {
			@Override
			@SideOnly(Side.CLIENT)
			public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
				// TODO send packet to client
				return train != null ? 1 : 0;
			}
		});
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if (train != null)
			tooltip.add(train.toString());
	}
}
