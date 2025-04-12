package org.mtr.mod.block;

import org.mtr.core.tool.Angle;
import org.mtr.libraries.it.unimi.dsi.fastutil.ints.IntAVLTreeSet;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mapping.mapper.BlockExtension;
import org.mtr.mapping.mapper.BlockWithEntity;
import org.mtr.mapping.mapper.DirectionHelper;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.Init;
import org.mtr.mod.packet.PacketOpenBlockEntityScreen;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public abstract class BlockSignalBase extends BlockExtension implements DirectionHelper, BlockWithEntity {

	public static final EnumProperty<EnumBooleanInverted> IS_22_5 = EnumProperty.of("is_22_5", EnumBooleanInverted.class);
	public static final EnumProperty<EnumBooleanInverted> IS_45 = EnumProperty.of("is_45", EnumBooleanInverted.class);

	private static final int COOLDOWN_1 = 2000;
	private static final int COOLDOWN_2 = COOLDOWN_1 + 2000;

	public BlockSignalBase(BlockSettings blockSettings) {
		super(blockSettings);
	}

	@Nonnull
	@Override
	public ActionResult onUse2(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		return IBlock.checkHoldingBrush(world, player, () -> {
			final BlockEntity entity = world.getBlockEntity(pos);
			if (entity != null && entity.data instanceof BlockEntityBase) {
				((BlockEntityBase) entity.data).markDirty2();
				Init.REGISTRY.sendPacketToClient(ServerPlayerEntity.cast(player), new PacketOpenBlockEntityScreen(pos));
			}
		});
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

	public static float getAngle(BlockState state) {
		return IBlock.getStatePropertySafe(state, DirectionHelper.FACING).asRotation() + (IBlock.getStatePropertySafe(state, BlockSignalBase.IS_22_5).booleanValue ? 22.5F : 0) + (IBlock.getStatePropertySafe(state, BlockSignalBase.IS_45).booleanValue ? 45 : 0);
	}

	public static abstract class BlockEntityBase extends BlockEntityExtension {

		private long lastOccupiedTime1;
		private long lastOccupiedTime2;
		public final boolean isDoubleSided;

		private final IntAVLTreeSet signalColors1 = new IntAVLTreeSet();
		private final IntAVLTreeSet signalColors2 = new IntAVLTreeSet();
		private static final String KEY_SIGNAL_COLORS_1 = "signal_colors_1";
		private static final String KEY_SIGNAL_COLORS_2 = "signal_colors_2";

		public BlockEntityBase(BlockEntityType<?> type, boolean isDoubleSided, BlockPos pos, BlockState state) {
			super(type, pos, state);
			this.isDoubleSided = isDoubleSided;
		}

		@Override
		public void readCompoundTag(CompoundTag compoundTag) {
			signalColors1.clear();
			for (final int color : compoundTag.getIntArray(KEY_SIGNAL_COLORS_1)) {
				signalColors1.add(color);
			}
			signalColors2.clear();
			for (final int color : compoundTag.getIntArray(KEY_SIGNAL_COLORS_2)) {
				signalColors2.add(color);
			}
			super.readCompoundTag(compoundTag);
		}

		@Override
		public void writeCompoundTag(CompoundTag compoundTag) {
			compoundTag.putIntArray(KEY_SIGNAL_COLORS_1, new ArrayList<>(signalColors1));
			compoundTag.putIntArray(KEY_SIGNAL_COLORS_2, new ArrayList<>(signalColors2));
			super.writeCompoundTag(compoundTag);
		}

		public void setData(IntAVLTreeSet signalColors, boolean isBackSide) {
			getSignalColors(isBackSide).clear();
			getSignalColors(isBackSide).addAll(signalColors);
			markDirty2();
		}

		public IntAVLTreeSet getSignalColors(boolean isBackSide) {
			return isBackSide ? signalColors2 : signalColors1;
		}

		public int getActualAspect(boolean occupied, boolean isBackSide) {
			final long currentTime = System.currentTimeMillis();
			if (occupied) {
				if (isBackSide) {
					lastOccupiedTime2 = currentTime;
				} else {
					lastOccupiedTime1 = currentTime;
				}
				return 1;
			} else {
				final long difference = currentTime - (isBackSide ? lastOccupiedTime2 : lastOccupiedTime1);
				if (difference >= COOLDOWN_2) {
					return 0;
				} else if (difference >= COOLDOWN_1) {
					return 3;
				} else {
					return 2;
				}
			}
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
