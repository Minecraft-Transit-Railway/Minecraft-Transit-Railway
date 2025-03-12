package org.mtr.block;

import it.unimi.dsi.fastutil.ints.IntAVLTreeSet;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.mtr.core.tool.Angle;
import org.mtr.packet.PacketOpenBlockEntityScreen;
import org.mtr.registry.Registry;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public abstract class BlockSignalBase extends Block implements BlockEntityProvider {

	public static final EnumProperty<EnumBooleanInverted> IS_22_5 = EnumProperty.of("is_22_5", EnumBooleanInverted.class);
	public static final EnumProperty<EnumBooleanInverted> IS_45 = EnumProperty.of("is_45", EnumBooleanInverted.class);

	public BlockSignalBase(AbstractBlock.Settings blockSettings) {
		super(blockSettings);
	}

	@Nonnull
	@Override
	protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
		return IBlock.checkHoldingBrush(world, player, () -> {
			final BlockEntity entity = world.getBlockEntity(pos);
			if (entity instanceof BlockEntityBase) {
				entity.markDirty();
				Registry.sendPacketToClient((ServerPlayerEntity) player, new PacketOpenBlockEntityScreen(pos));
			}
		});
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		final int quadrant = Angle.getQuadrant(ctx.getPlayerYaw(), true);
		return getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.fromHorizontalDegrees(quadrant / 4F)).with(IS_45, EnumBooleanInverted.fromBoolean(quadrant % 4 >= 2)).with(IS_22_5, EnumBooleanInverted.fromBoolean(quadrant % 2 == 1));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(Properties.HORIZONTAL_FACING);
		builder.add(IS_22_5);
		builder.add(IS_45);
	}

	public static float getAngle(BlockState state) {
		return IBlock.getStatePropertySafe(state, Properties.HORIZONTAL_FACING).getPositiveHorizontalDegrees() + (IBlock.getStatePropertySafe(state, BlockSignalBase.IS_22_5).booleanValue ? 22.5F : 0) + (IBlock.getStatePropertySafe(state, BlockSignalBase.IS_45).booleanValue ? 45 : 0);
	}

	public static abstract class BlockEntityBase extends BlockEntity {

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
		protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
			signalColors1.clear();
			for (final int color : nbt.getIntArray(KEY_SIGNAL_COLORS_1)) {
				signalColors1.add(color);
			}
			signalColors2.clear();
			for (final int color : nbt.getIntArray(KEY_SIGNAL_COLORS_2)) {
				signalColors2.add(color);
			}
			super.readNbt(nbt, registries);
		}

		@Override
		protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
			nbt.putIntArray(KEY_SIGNAL_COLORS_1, new ArrayList<>(signalColors1));
			nbt.putIntArray(KEY_SIGNAL_COLORS_2, new ArrayList<>(signalColors2));
			super.writeNbt(nbt, registries);
		}

		public void setData(IntAVLTreeSet signalColors, boolean isBackSide) {
			getSignalColors(isBackSide).clear();
			getSignalColors(isBackSide).addAll(signalColors);
			markDirty();
		}

		public IntAVLTreeSet getSignalColors(boolean isBackSide) {
			return isBackSide ? signalColors2 : signalColors1;
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
		public String asString() {
			return String.valueOf(booleanValue);
		}

		private static EnumBooleanInverted fromBoolean(boolean value) {
			return value ? TRUE : FALSE;
		}
	}
}
