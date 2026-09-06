package org.mtr.block;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
//? if >= 26.1 {
/*import net.minecraft.world.item.component.TooltipDisplay;
*///? }
import org.mtr.generated.lang.TranslationProvider;

import java.util.List;
import java.util.function.Consumer;

public class BlockStationColorSlab extends SlabBlock {

	public BlockStationColorSlab(BlockBehaviour.Properties settings) {
		super(settings);
	}

	@Override
//? if >= 26.1 {
/*	public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltip, TooltipFlag options) {
*///? } else {
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipList, TooltipFlag options) {
		final Consumer<Component> tooltip = tooltipList::add;
//? }
		tooltip.accept(TranslationProvider.TOOLTIP_MTR_STATION_COLOR.getMutableText().withStyle(ChatFormatting.GRAY));
	}
}
