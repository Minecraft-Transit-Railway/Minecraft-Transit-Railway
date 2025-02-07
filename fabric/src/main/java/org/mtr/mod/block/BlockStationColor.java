package org.mtr.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockExtension;
import org.mtr.mod.Blocks;
import org.mtr.mod.generated.lang.TranslationProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class BlockStationColor extends BlockExtension {

	public BlockStationColor() {
		this(Blocks.createDefaultBlockSettings(false));
	}

	protected BlockStationColor(BlockSettings blockSettings) {
		super(blockSettings);
	}

	@Nonnull
	@Override
	public String getTranslationKey2() {
		return super.getTranslationKey2().replace("block.mtr.station_color_", "block.minecraft.");
	}

	@Override
	public void addTooltips(ItemStack stack, @Nullable BlockView world, List<MutableText> tooltip, TooltipContext options) {
		tooltip.add(TranslationProvider.TOOLTIP_MTR_STATION_COLOR.getMutableText().formatted(TextFormatting.GRAY));
	}
}
