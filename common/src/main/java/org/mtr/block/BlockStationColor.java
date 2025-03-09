package org.mtr.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.mtr.generated.lang.TranslationProvider;

import java.util.List;

public class BlockStationColor extends Block {

	public BlockStationColor(AbstractBlock.Settings settings) {
		super(settings);
	}

	@Override
	public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
		tooltip.add(TranslationProvider.TOOLTIP_MTR_STATION_COLOR.getMutableText().formatted(Formatting.GRAY));
	}
}
