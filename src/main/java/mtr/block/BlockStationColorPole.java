package mtr.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

import java.util.List;

public class BlockStationColorPole extends Block {

	private final boolean showTooltip;

	public BlockStationColorPole(Settings settings, boolean showTooltip) {
		super(settings);
		this.showTooltip = showTooltip;
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return getStationPoleShape();
	}

	@Override
	public void appendTooltip(ItemStack stack, BlockView world, List<Text> tooltip, TooltipContext options) {
		if (showTooltip) {
			tooltip.add(new TranslatableText("tooltip.mtr.station_color").setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
		}
	}

	public static VoxelShape getStationPoleShape() {
		return Block.createCuboidShape(6, 0, 6, 10, 16, 10);
	}
}
