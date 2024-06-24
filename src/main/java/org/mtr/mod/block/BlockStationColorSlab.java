package mtr.block;

import mtr.mappings.Text;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.SlabBlock;

import java.util.List;

public class BlockStationColorSlab extends SlabBlock {

	public BlockStationColorSlab(Properties settings) {
		super(settings);
	}

	@Override
	public String getDescriptionId() {
		return super.getDescriptionId().replace("block.mtr.station_color_", "block.minecraft.");
	}

	@Override
	public void appendHoverText(ItemStack itemStack, BlockGetter blockGetter, List<Component> tooltip, TooltipFlag tooltipFlag) {
		tooltip.add(Text.translatable("tooltip.mtr.station_color").setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
	}
}
