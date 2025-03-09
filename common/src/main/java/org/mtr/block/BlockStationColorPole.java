package org.mtr.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import org.mtr.generated.lang.TranslationProvider;

import javax.annotation.Nonnull;
import java.util.List;

public class BlockStationColorPole extends Block {

	private final boolean showTooltip;

	public BlockStationColorPole(AbstractBlock.Settings settings, boolean showTooltip) {
		super(settings);
		this.showTooltip = showTooltip;
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return getStationPoleShape();
	}

	@Override
	public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
		if (showTooltip) {
			tooltip.add(TranslationProvider.TOOLTIP_MTR_STATION_COLOR.getMutableText().formatted(Formatting.GRAY));
		}
	}

	public static VoxelShape getStationPoleShape() {
		return Block.createCuboidShape(6, 0, 6, 10, 16, 10);
	}
}
