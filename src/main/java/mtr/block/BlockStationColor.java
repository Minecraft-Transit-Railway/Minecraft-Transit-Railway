package mtr.block;

import net.minecraft.block.Block;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.BlockView;

import java.util.List;

public class BlockStationColor extends Block {

	public BlockStationColor(Settings settings) {
		super(settings);
	}

	@Override
	public void appendTooltip(ItemStack stack, BlockView world, List<Text> tooltip, TooltipContext options) {
		tooltip.add(new TranslatableText("tooltip.mtr.station_color").setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
	}
}
