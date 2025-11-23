package org.mtr.block;

import it.unimi.dsi.fastutil.ints.IntAVLTreeSet;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.mtr.core.operation.BlockRails;
import org.mtr.core.tool.Angle;
import org.mtr.packet.PacketBlockRails;
import org.mtr.packet.PacketOpenBlockEntityScreen;
import org.mtr.packet.PacketTurnOnBlockEntity;
import org.mtr.registry.Registry;
import org.mtr.registry.RegistryClient;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public abstract class BlockSignalBase extends Block implements BlockEntityProvider {

	public static final EnumProperty<EnumBooleanInverted> IS_22_5 = EnumProperty.of("is_22_5", EnumBooleanInverted.class);
	public static final EnumProperty<EnumBooleanInverted> IS_45 = EnumProperty.of("is_45", EnumBooleanInverted.class);
	public static final IntProperty POWER = IntProperty.of("power", 0, 15);

	private static final int COOLDOWN_1 = 2000;
	private static final int COOLDOWN_2 = COOLDOWN_1 + 2000;
	private static final int ACCEPT_REDSTONE_COOLDOWN = 800;

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
	public boolean emitsRedstonePower(BlockState blockState) {
		return true;
	}

	@Override
	public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
		return IBlock.getStatePropertySafe(state, POWER);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(Properties.HORIZONTAL_FACING);
		builder.add(IS_22_5);
		builder.add(IS_45);
		builder.add(POWER);
	}

	public void power(World world, BlockState state, BlockPos pos, int level) {
		final int oldPowered = IBlock.getStatePropertySafe(state, POWER);
		if (oldPowered != level) {
			world.setBlockState(pos, state.with(POWER, level));
		}
	}

	public static float getAngle(BlockState state) {
		return IBlock.getStatePropertySafe(state, Properties.HORIZONTAL_FACING).getPositiveHorizontalDegrees() + (IBlock.getStatePropertySafe(state, BlockSignalBase.IS_22_5).booleanValue ? 22.5F : 0) + (IBlock.getStatePropertySafe(state, BlockSignalBase.IS_45).booleanValue ? 45 : 0);
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
		protected void readNbt(NbtCompound nbtCompound) {
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
		protected void writeNbt(NbtCompound nbtCompound) {
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
			markDirty();
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
				RegistryClient.sendPacketToServer(new PacketTurnOnBlockEntity(getPos(), newRedstoneLevel));
			}

			final long currentTime = System.currentTimeMillis();
			final World world = getWorld();

			if (getAcceptRedstone() && currentTime - lastAcceptedRedstoneTime > ACCEPT_REDSTONE_COOLDOWN && world != null) {
				lastAcceptedRedstoneTime = currentTime;
				for (final Direction direction : Direction.values()) {
					if (world.isEmittingRedstonePower(getPos().offset(direction.getOpposite()), direction)) {
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
