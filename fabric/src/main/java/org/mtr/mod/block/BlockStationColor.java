package org.mtr.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockExtension;
import org.mtr.mapping.mapper.BlockHelper;
import org.mtr.mapping.mapper.TextHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class BlockStationColor extends BlockExtension {

	public BlockStationColor() {
		super(BlockHelper.createBlockSettings(false));
	}

	@Nonnull
	@Override
	public String getTranslationKey2() {
		return super.getTranslationKey2().replace("block.mtr.station_color_", "block.minecraft.");
	}

	@Override
	public void addTooltips(ItemStack stack, @Nullable BlockView world, List<MutableText> tooltip, TooltipContext options) {
		tooltip.add(TextHelper.translatable("tooltip.mtr.station_color").formatted(TextFormatting.GRAY));
	}
}
