package org.mtr.block;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.mtr.generated.lang.TranslationProvider;

import java.util.List;

public class BlockStationColorSlab extends SlabBlock {

	public BlockStationColorSlab(BlockBehaviour.Properties settings) {
		super(settings);
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag options) {
		tooltip.add(TranslationProvider.TOOLTIP_MTR_STATION_COLOR.getMutableText().withStyle(ChatFormatting.GRAY));
	}
}
