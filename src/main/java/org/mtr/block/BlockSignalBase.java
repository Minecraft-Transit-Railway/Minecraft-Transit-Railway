package org.mtr.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.mtr.core.operation.BlockRails;
import org.mtr.core.tool.Angle;
import org.mtr.libraries.it.unimi.dsi.fastutil.ints.IntAVLTreeSet;
import org.mtr.libraries.it.unimi.dsi.fastutil.ints.IntArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.packet.PacketBlockRails;
import org.mtr.packet.PacketOpenBlockEntityScreen;
import org.mtr.packet.PacketTurnOnBlockEntity;
import org.mtr.registry.RegistryClient;

import java.util.ArrayList;

public abstract class BlockSignalBase extends Block implements EntityBlock {

	public static final EnumProperty<EnumBooleanInverted> IS_22_5 = EnumProperty.create("is_22_5", EnumBooleanInverted.class);
	public static final EnumProperty<EnumBooleanInverted> IS_45 = EnumProperty.create("is_45", EnumBooleanInverted.class);
	public static final IntegerProperty POWER = IntegerProperty.create("power", 0, 15);

	private static final int COOLDOWN_1 = 2000;
	private static final int COOLDOWN_2 = COOLDOWN_1 + 2000;
	private static final int ACCEPT_REDSTONE_COOLDOWN = 800;

	public BlockSignalBase(BlockBehaviour.Properties blockSettings) {
		super(blockSettings);
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
		return IBlock.checkHoldingBrush(world, player, () -> PacketOpenBlockEntityScreen.sendDirectlyToServer((ServerLevel) world, (ServerPlayer) player, pos));
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		final int quadrant = Angle.getQuadrant(ctx.getRotation(), true);
		return defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.fromYRot(quadrant / 4F)).setValue(IS_45, EnumBooleanInverted.fromBoolean(quadrant % 4 >= 2)).setValue(IS_22_5, EnumBooleanInverted.fromBoolean(quadrant % 2 == 1));
	}

	@Override
	public boolean isSignalSource(BlockState blockState) {
		return true;
	}

	@Override
	public int getSignal(BlockState state, BlockGetter world, BlockPos pos, Direction direction) {
		return IBlock.getStatePropertySafe(state, POWER);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.HORIZONTAL_FACING);
		builder.add(IS_22_5);
		builder.add(IS_45);
		builder.add(POWER);
	}

	public void power(Level world, BlockState state, BlockPos pos, int level) {
		final int oldPowered = IBlock.getStatePropertySafe(state, POWER);
		if (oldPowered != level) {
			world.setBlockAndUpdate(pos, state.setValue(POWER, level));
		}
	}

	public static float getAngle(BlockState state) {
		return IBlock.getStatePropertySafe(state, BlockStateProperties.HORIZONTAL_FACING).toYRot() + (IBlock.getStatePropertySafe(state, BlockSignalBase.IS_22_5).booleanValue ? 22.5F : 0) + (IBlock.getStatePropertySafe(state, BlockSignalBase.IS_45).booleanValue ? 45 : 0);
	}

	public static abstract class BlockEntityBase extends BlockEntityExtension {

		private long lastOccupiedTime1;
		private long lastOccupiedTime2;
		private int oldRedstoneLevel;
		private long lastAcceptedRedstoneTime;
		private boolean acceptRedstone;
		private boolean outputRedstone;
		public final boolean isDoubleSided;

		private final IntAVLTreeSet signalColors1 = new IntAVLTreeSet();
		private final IntAVLTreeSet signalColors2 = new IntAVLTreeSet();

		private static final String KEY_ACCEPT_REDSTONE = "accept_redstone";
		private static final String KEY_OUTPUT_REDSTONE = "output_redstone";
		private static final String KEY_SIGNAL_COLORS_1 = "signal_colors_1";
		private static final String KEY_SIGNAL_COLORS_2 = "signal_colors_2";

		public BlockEntityBase(BlockEntityType<?> type, boolean isDoubleSided, BlockPos pos, BlockState state) {
			super(type, pos, state);
			this.isDoubleSided = isDoubleSided;
		}

		@Override
		protected void readNbt(CompoundTag nbtCompound) {
			acceptRedstone = nbtCompound.getBoolean(KEY_ACCEPT_REDSTONE);
			outputRedstone = nbtCompound.getBoolean(KEY_OUTPUT_REDSTONE);
			signalColors1.clear();
			for (final int color : nbtCompound.getIntArray(KEY_SIGNAL_COLORS_1)) {
				signalColors1.add(color);
			}
			signalColors2.clear();
			for (final int color : nbtCompound.getIntArray(KEY_SIGNAL_COLORS_2)) {
				signalColors2.add(color);
			}
		}

		@Override
		protected void writeNbt(CompoundTag nbtCompound) {
			nbtCompound.putBoolean(KEY_ACCEPT_REDSTONE, acceptRedstone);
			nbtCompound.putBoolean(KEY_OUTPUT_REDSTONE, outputRedstone);
			nbtCompound.putIntArray(KEY_SIGNAL_COLORS_1, new ArrayList<>(signalColors1));
			nbtCompound.putIntArray(KEY_SIGNAL_COLORS_2, new ArrayList<>(signalColors2));
		}

		public void setData(boolean acceptRedstone, boolean outputRedstone, IntAVLTreeSet signalColors, boolean isBackSide) {
			this.acceptRedstone = acceptRedstone;
			this.outputRedstone = outputRedstone;
			getSignalColors(isBackSide).clear();
			getSignalColors(isBackSide).addAll(signalColors);
			setChanged();
		}

		public boolean getAcceptRedstone() {
			return acceptRedstone;
		}

		public boolean getOutputRedstone() {
			return outputRedstone && !acceptRedstone;
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

		public void checkForRedstoneUpdate(int redstoneLevel, ObjectArrayList<String> railIds1, ObjectArrayList<String> railIds2) {
			final int newRedstoneLevel = getOutputRedstone() ? redstoneLevel : 0;
			if (oldRedstoneLevel != newRedstoneLevel) {
				oldRedstoneLevel = newRedstoneLevel;
				RegistryClient.sendPacketToServer(new PacketTurnOnBlockEntity(getBlockPos(), newRedstoneLevel));
			}

			final long currentTime = System.currentTimeMillis();
			final Level world = getLevel();

			if (getAcceptRedstone() && currentTime - lastAcceptedRedstoneTime > ACCEPT_REDSTONE_COOLDOWN && world != null) {
				lastAcceptedRedstoneTime = currentTime;
				for (final Direction direction : Direction.values()) {
					if (world.hasSignal(getBlockPos().relative(direction.getOpposite()), direction)) {
						if (!railIds1.isEmpty()) {
							RegistryClient.sendPacketToServer(new PacketBlockRails(new BlockRails(railIds1, new IntArrayList(signalColors1))));
						}
						if (!railIds2.isEmpty()) {
							RegistryClient.sendPacketToServer(new PacketBlockRails(new BlockRails(railIds2, new IntArrayList(signalColors2))));
						}
						break;
					}
				}
			}
		}
	}

	public enum EnumBooleanInverted implements StringRepresentable {

		FALSE(false), TRUE(true);
		public final boolean booleanValue;

		EnumBooleanInverted(boolean booleanValue) {
			this.booleanValue = booleanValue;
		}

		@Override
		public String getSerializedName() {
			return String.valueOf(booleanValue);
		}

		private static EnumBooleanInverted fromBoolean(boolean value) {
			return value ? TRUE : FALSE;
		}
	}
}
