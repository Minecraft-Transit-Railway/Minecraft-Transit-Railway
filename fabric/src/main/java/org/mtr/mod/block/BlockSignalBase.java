package org.mtr.mod.block;

import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongArrayList;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mapping.mapper.BlockExtension;
import org.mtr.mapping.mapper.BlockWithEntity;
import org.mtr.mapping.mapper.DirectionHelper;
import org.mtr.mapping.tool.HolderBase;

import java.util.List;

public abstract class BlockSignalBase extends BlockExtension implements DirectionHelper, BlockWithEntity {

	public BlockSignalBase(BlockSettings blockSettings) {
		super(blockSettings);
	}

	@Override
	public BlockState getPlacementState2(ItemPlacementContext ctx) {
		return getDefaultState2().with(new Property<>(FACING.data), ctx.getPlayerFacing().data);
	}

	@Override
	public void addBlockProperties(List<HolderBase<?>> properties) {
		properties.add(FACING);
	}

	public static abstract class BlockEntityBase extends BlockEntityExtension {

		public final LongArrayList signalColors1 = new LongArrayList();
		public final LongArrayList signalColors2 = new LongArrayList();
		private static final String KEY_SIGNAL_COLORS_1 = "signal_colors_1";
		private static final String KEY_SIGNAL_COLORS_2 = "signal_colors_2";

		public BlockEntityBase(BlockEntityType<?> type, BlockPos pos, BlockState state) {
			super(type, pos, state);
		}

		@Override
		public void readCompoundTag(CompoundTag compoundTag) {
			signalColors1.clear();
			for (final long color : compoundTag.getLongArray(KEY_SIGNAL_COLORS_1)) {
				signalColors1.add(color);
			}
			signalColors2.clear();
			for (final long color : compoundTag.getLongArray(KEY_SIGNAL_COLORS_2)) {
				signalColors2.add(color);
			}
			super.readCompoundTag(compoundTag);
		}

		@Override
		public void writeCompoundTag(CompoundTag compoundTag) {
			compoundTag.putLongArray(KEY_SIGNAL_COLORS_1, signalColors1);
			compoundTag.putLongArray(KEY_SIGNAL_COLORS_2, signalColors2);
			super.writeCompoundTag(compoundTag);
		}
	}
}
