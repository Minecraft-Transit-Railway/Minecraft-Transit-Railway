package org.mtr.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockExtension;
import org.mtr.mod.Blocks;
import org.mtr.mod.generated.lang.TranslationProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class BlockStationColorPole extends BlockExtension {

	private final boolean showTooltip;

	public BlockStationColorPole(boolean showTooltip) {
		super(Blocks.createDefaultBlockSettings(false));
		this.showTooltip = showTooltip;
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return getStationPoleShape();
	}

	@Override
	public void addTooltips(ItemStack stack, @Nullable BlockView world, List<MutableText> tooltip, TooltipContext options) {
		if (showTooltip) {
			tooltip.add(TranslationProvider.TOOLTIP_MTR_STATION_COLOR.getMutableText().formatted(TextFormatting.GRAY));
		}
	}

	public static VoxelShape getStationPoleShape() {
		return Block.createCuboidShape(6, 0, 6, 10, 16, 10);
	}
}
