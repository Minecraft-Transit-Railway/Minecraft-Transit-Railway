package org.mtr.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.*;
import org.mtr.mod.data.IGui;

import javax.annotation.Nullable;
import java.util.List;

public abstract class BlockStationNameBase extends BlockExtension implements DirectionHelper, BlockWithEntity {

	public static final IntegerProperty COLOR = IntegerProperty.of("color", 0, 2);

	protected BlockStationNameBase(BlockSettings blockSettings) {
		super(blockSettings.nonOpaque());
	}

	@Override
	public void addTooltips(ItemStack stack, @Nullable BlockView world, List<MutableText> tooltip, TooltipContext options) {
		tooltip.add(TextHelper.translatable("tooltip.mtr.station_color_name").formatted(TextFormatting.GRAY));
	}

	public abstract static class BlockEntityBase extends BlockEntityExtension implements IGui {

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
