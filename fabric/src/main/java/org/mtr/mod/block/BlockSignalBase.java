package org.mtr.mod.block;

import org.mtr.core.tool.Angle;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongArrayList;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mapping.mapper.BlockExtension;
import org.mtr.mapping.mapper.BlockWithEntity;
import org.mtr.mapping.mapper.DirectionHelper;
import org.mtr.mapping.tool.HolderBase;

import javax.annotation.Nonnull;
import java.util.List;

public abstract class BlockSignalBase extends BlockExtension implements DirectionHelper, BlockWithEntity {

	public static final EnumProperty<EnumBooleanInverted> IS_22_5 = EnumProperty.of("is_22_5", EnumBooleanInverted.class);
	public static final EnumProperty<EnumBooleanInverted> IS_45 = EnumProperty.of("is_45", EnumBooleanInverted.class);

	public BlockSignalBase(BlockSettings blockSettings) {
		super(blockSettings);
	}

	@Override
	public BlockState getPlacementState2(ItemPlacementContext ctx) {
		final int quadrant = Angle.getQuadrant(ctx.getPlayerYaw(), true);
		return getDefaultState2().with(new Property<>(FACING.data), Direction.fromHorizontal(quadrant / 4).data).with(new Property<>(IS_45.data), EnumBooleanInverted.fromBoolean(quadrant % 4 >= 2)).with(new Property<>(IS_22_5.data), EnumBooleanInverted.fromBoolean(quadrant % 2 == 1));
	}

	@Override
	public void addBlockProperties(List<HolderBase<?>> properties) {
		properties.add(FACING);
		properties.add(IS_22_5);
		properties.add(IS_45);
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

	public enum EnumBooleanInverted implements StringIdentifiable {

		FALSE(false), TRUE(true);
		public final boolean booleanValue;

		EnumBooleanInverted(boolean booleanValue) {
			this.booleanValue = booleanValue;
		}

		@Nonnull
		@Override
		public String asString2() {
			return String.valueOf(booleanValue);
		}

		private static EnumBooleanInverted fromBoolean(boolean value) {
			return value ? TRUE : FALSE;
		}
	}
}
