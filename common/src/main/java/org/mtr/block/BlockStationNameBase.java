package org.mtr.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.state.property.IntProperty;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import org.mtr.data.IGui;
import org.mtr.generated.lang.TranslationProvider;

import java.util.List;

public abstract class BlockStationNameBase extends Block implements BlockEntityProvider {

	public static final IntProperty COLOR = IntProperty.of("color", 0, 2);

	protected BlockStationNameBase(AbstractBlock.Settings blockSettings) {
		super(blockSettings.nonOpaque());
	}

	@Override
	public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
		tooltip.add(TranslationProvider.TOOLTIP_MTR_STATION_COLOR_NAME.getMutableText().formatted(Formatting.GRAY));
	}

	public abstract static class BlockEntityBase extends BlockEntity implements IGui {

		public final float yOffset;
		public final float zOffset;
		public final boolean isDoubleSided;

		public BlockEntityBase(BlockEntityType<?> type, BlockPos pos, BlockState state, float yOffset, float zOffset, boolean isDoubleSided) {
			super(type, pos, state);
			this.yOffset = yOffset;
			this.zOffset = zOffset;
			this.isDoubleSided = isDoubleSided;
		}

		public abstract int getColor(BlockState state);
	}
}
