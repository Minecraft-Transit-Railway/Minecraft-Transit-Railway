package org.mtr.block;

import it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.packet.PacketOpenBlockEntityScreen;
import org.mtr.registry.BlockEntityTypes;
import org.mtr.registry.Registry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class BlockRailwaySign extends Block implements IBlock, BlockEntityProvider {

	public final int length;
	public final boolean isOdd;

	public static final float SMALL_SIGN_PERCENTAGE = 0.75F;
	// Legacy sign IDs in MTR 3.x.x. In MTR 4.x.x they are migrated to use lower-case.
	private static final String[] LEGACY_SIGNS = {"ARROW_LEFT", "ARROW_RIGHT", "ARROW_UP", "ARROW_DOWN", "ARROW_UP_LEFT", "ARROW_UP_RIGHT", "ARROW_DOWN_LEFT", "ARROW_DOWN_RIGHT", "ARROW_TURN_BACK_LEFT", "ARROW_TURN_BACK_RIGHT", "EXIT_1", "EXIT_2", "EXIT_3", "ESCALATOR", "ESCALATOR_FLIPPED", "STAIRS_UP", "STAIRS_UP_FLIPPED", "STAIRS_DOWN_FLIPPED", "STAIRS_DOWN", "LIFT_1", "LIFT_2", "WHEELCHAIR", "TOILET", "FEMALE", "MALE", "TRAIN", "TRAIN_OLD", "AIRPORT_EXPRESS", "LIGHT_RAIL_1", "LIGHT_RAIL_2", "LIGHT_RAIL_3", "LIGHT_RAIL_4", "XRL_1", "XRL_2", "SP1900", "YELLOW_HEAD_1", "YELLOW_HEAD_2", "BOAT", "CABLE_CAR", "AIRPLANE", "AIRPLANE_LEFT", "AIRPLANE_RIGHT", "AIRPLANE_UP_LEFT", "AIRPLANE_UP_RIGHT", "CROSS", "LOGO", "EXIT_LETTER", "EXIT_LETTER_FLIPPED", "ESCALATOR_TO_CONCOURSE_UP", "ESCALATOR_TO_CONCOURSE_UP_FLIPPED", "ESCALATOR_TO_CONCOURSE_DOWN", "ESCALATOR_TO_CONCOURSE_DOWN_FLIPPED", "PLATFORM", "PLATFORM_FLIPPED", "LINE", "LINE_FLIPPED", "STATION", "STATION_FLIPPED", "LIFT_1_TEXT", "LIFT_1_TEXT_FLIPPED", "LIFT_2_TEXT", "LIFT_2_TEXT_FLIPPED", "TOILETS", "TOILETS_FLIPPED", "FEMALE_TOILETS", "FEMALE_TOILETS_FLIPPED", "MALE_TOILETS", "MALE_TOILETS_FLIPPED", "WHEELCHAIR_TOILETS", "WHEELCHAIR_TOILETS_FLIPPED", "TRAINS", "TRAINS_FLIPPED", "TRAINS_OLD", "TRAINS_OLD_FLIPPED", "AIRPORT_EXPRESS_TRAINS", "AIRPORT_EXPRESS_TRAINS_FLIPPED", "AIRPORT_EXPRESS_TRAINS_CITY", "AIRPORT_EXPRESS_TRAINS_CITY_FLIPPED", "IN_TOWN_CHECK_IN", "IN_TOWN_CHECK_IN_FLIPPED", "CHECK_IN_PASSENGERS", "CHECK_IN_PASSENGERS_FLIPPED", "LIGHT_RAIL_1_TRAINS", "LIGHT_RAIL_1_TRAINS_FLIPPED", "LIGHT_RAIL_2_TRAINS", "LIGHT_RAIL_2_TRAINS_FLIPPED", "LIGHT_RAIL_3_TRAINS", "LIGHT_RAIL_3_TRAINS_FLIPPED", "LIGHT_RAIL_4_TRAINS", "LIGHT_RAIL_4_TRAINS_FLIPPED", "XRL_1_TRAINS", "XRL_1_TRAINS_FLIPPED", "XRL_2_TRAINS", "XRL_2_TRAINS_FLIPPED", "SP1900_TRAINS", "SP1900_TRAINS_FLIPPED", "YELLOW_HEAD_1_TRAINS", "YELLOW_HEAD_1_TRAINS_FLIPPED", "YELLOW_HEAD_2_TRAINS", "YELLOW_HEAD_2_TRAINS_FLIPPED", "BOAT_BOATS", "BOAT_BOATS_FLIPPED", "CABLE_CAR_CABLE_CARS", "CABLE_CAR_CABLE_CARS_FLIPPED", "AIRPORT", "AIRPORT_FLIPPED", "AIRPORT_LEFT", "AIRPORT_RIGHT", "AIRPORT_UP_LEFT", "AIRPORT_UP_RIGHT", "AIRPORT_ARRIVALS", "AIRPORT_ARRIVALS_FLIPPED", "AIRPORT_DEPARTURES", "AIRPORT_DEPARTURES_FLIPPED", "AIRPORT_TRANSFER", "AIRPORT_TRANSFER_FLIPPED", "BAGGAGE_CLAIM", "BAGGAGE_CLAIM_FLIPPED", "CUSTOMER_SERVICE_CENTRE", "CUSTOMER_SERVICE_CENTRE_FLIPPED", "TICKETS", "TICKETS_FLIPPED", "NO_ENTRY", "NO_ENTRY_FLIPPED", "EMERGENCY_EXIT", "EMERGENCY_EXIT_FLIPPED", "WIFI", "WIFI_FLIPPED", "LOGO_TEXT", "LOGO_TEXT_FLIPPED"};

	public BlockRailwaySign(AbstractBlock.Settings settings, int length, boolean isOdd) {
		super(settings.luminance(blockState -> 15).overrideTranslationKey("block.mtr.railway_sign"));
		this.length = length;
		this.isOdd = isOdd;
	}

	@Nonnull
	@Override
	protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
		return IBlock.checkHoldingBrush(world, player, () -> {
			final Direction facing = IBlock.getStatePropertySafe(state, Properties.HORIZONTAL_FACING);
			final Direction hitSide = hit.getSide();
			if (hitSide == facing || hitSide == facing.getOpposite()) {
				final BlockPos checkPos = findEndWithDirection(world, pos, hitSide.getOpposite(), false);
				if (checkPos != null) {
					Registry.sendPacketToClient((ServerPlayerEntity) player, new PacketOpenBlockEntityScreen(checkPos));
				}
			}
		});
	}

	@Nonnull
	@Override
	protected BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {
		final Direction facing = IBlock.getStatePropertySafe(state, Properties.HORIZONTAL_FACING);
		final boolean isNext = direction == facing.rotateYClockwise() || state.isOf(org.mtr.registry.Blocks.RAILWAY_SIGN_MIDDLE.get()) && direction == facing.rotateYCounterclockwise();
		if (isNext && !(neighborState.getBlock() instanceof BlockRailwaySign)) {
			return Blocks.AIR.getDefaultState();
		} else {
			return state;
		}
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		final Direction facing = ctx.getHorizontalPlayerFacing();
		return IBlock.isReplaceable(ctx, facing.rotateYClockwise(), getMiddleLength() + 2) ? getDefaultState().with(Properties.HORIZONTAL_FACING, facing) : null;
	}

	@Override
	public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		final Direction facing = IBlock.getStatePropertySafe(state, Properties.HORIZONTAL_FACING);

		final BlockPos checkPos = findEndWithDirection(world, pos, facing, true);
		if (checkPos != null) {
			IBlock.onBreakCreative(world, player, checkPos);
		}

		return super.onBreak(world, pos, state, player);
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		if (!world.isClient()) {
			final Direction facing = IBlock.getStatePropertySafe(state, Properties.HORIZONTAL_FACING);
			for (int i = 1; i <= getMiddleLength(); i++) {
				world.setBlockState(pos.offset(facing.rotateYClockwise(), i), org.mtr.registry.Blocks.RAILWAY_SIGN_MIDDLE.createAndGet().getDefaultState().with(Properties.HORIZONTAL_FACING, facing), 3);
			}
			world.setBlockState(pos.offset(facing.rotateYClockwise(), getMiddleLength() + 1), getDefaultState().with(Properties.HORIZONTAL_FACING, facing.getOpposite()), 3);
			world.updateNeighbors(pos, Blocks.AIR);
			state.updateNeighbors(world, pos, 3);
		}
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		final Direction facing = IBlock.getStatePropertySafe(state, Properties.HORIZONTAL_FACING);
		if (state.isOf(org.mtr.registry.Blocks.RAILWAY_SIGN_MIDDLE.get())) {
			return IBlock.getVoxelShapeByDirection(0, 0, 7, 16, 12, 9, facing);
		} else {
			final int xStart = getXStart();
			final VoxelShape main = IBlock.getVoxelShapeByDirection(xStart - 0.75, 0, 7, 16, 12, 9, facing);
			final VoxelShape pole = IBlock.getVoxelShapeByDirection(xStart - 2, 0, 7, xStart - 0.75, 16, 9, facing);
			return VoxelShapes.union(main, pole);
		}
	}

	@Override
	public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
		tooltip.add(TranslationProvider.TOOLTIP_MTR_RAILWAY_SIGN_LENGTH.getMutableText(length).formatted(Formatting.GRAY));
		tooltip.add((isOdd ? TranslationProvider.TOOLTIP_MTR_RAILWAY_SIGN_ODD : TranslationProvider.TOOLTIP_MTR_RAILWAY_SIGN_EVEN).getMutableText().formatted(Formatting.GRAY));
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState) {
		if (this == org.mtr.registry.Blocks.RAILWAY_SIGN_MIDDLE.get()) {
			return null;
		} else {
			return new RailwaySignBlockEntity(length, isOdd, blockPos, blockState);
		}
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(Properties.HORIZONTAL_FACING);
	}

	public int getXStart() {
		return switch (length % 4) {
			case 1 -> isOdd ? 4 : 12;
			case 2 -> isOdd ? 16 : 8;
			case 3 -> isOdd ? 12 : 4;
			default -> isOdd ? 8 : 16;
		};
	}

	private int getMiddleLength() {
		return (length - (4 - getXStart() / 4)) / 2;
	}

	private BlockPos findEndWithDirection(World world, BlockPos startPos, Direction direction, boolean allowOpposite) {
		int i = 0;
		while (true) {
			final BlockPos checkPos = startPos.offset(direction.rotateYCounterclockwise(), i);
			final BlockState checkState = world.getBlockState(checkPos);
			if (checkState.getBlock() instanceof BlockRailwaySign) {
				final Direction facing = IBlock.getStatePropertySafe(checkState, Properties.HORIZONTAL_FACING);
				if (!checkState.isOf(org.mtr.registry.Blocks.RAILWAY_SIGN_MIDDLE.get()) && (facing == direction || allowOpposite && facing == direction.getOpposite())) {
					return checkPos;
				}
			} else {
				return null;
			}
			i++;
		}
	}

	public static class RailwaySignBlockEntity extends BlockEntity {

		private final LongAVLTreeSet selectedIds;
		private final String[] signIds;
		private static final String KEY_SELECTED_IDS = "selected_ids";
		private static final String KEY_SIGN_LENGTH = "sign_length";

		public RailwaySignBlockEntity(int length, boolean isOdd, BlockPos pos, BlockState state) {
			super(getType(length, isOdd), pos, state);
			signIds = new String[length];
			selectedIds = new LongAVLTreeSet();
		}

		@Override
		protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
			selectedIds.clear();
			Arrays.stream(nbt.getLongArray(KEY_SELECTED_IDS)).forEach(selectedIds::add);
			for (int i = 0; i < signIds.length; i++) {
				final String signId = nbt.getString(KEY_SIGN_LENGTH + i);
				signIds[i] = signId.isEmpty() ? null : Arrays.asList(LEGACY_SIGNS).contains(signId) ? signId.toLowerCase(Locale.ENGLISH) : signId;
			}
		}

		@Override
		protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
			nbt.putLongArray(KEY_SELECTED_IDS, new ArrayList<>(selectedIds));
			for (int i = 0; i < signIds.length; i++) {
				nbt.putString(KEY_SIGN_LENGTH + i, signIds[i] == null ? "" : signIds[i]);
			}
		}

		public void setData(LongAVLTreeSet selectedIds, String[] signTypes) {
			this.selectedIds.clear();
			this.selectedIds.addAll(selectedIds);
			if (signIds.length == signTypes.length) {
				System.arraycopy(signTypes, 0, signIds, 0, signTypes.length);
			}
			markDirty();
		}

		public LongAVLTreeSet getSelectedIds() {
			return selectedIds;
		}

		public String[] getSignIds() {
			return signIds;
		}

		private static BlockEntityType<? extends BlockEntity> getType(int length, boolean isOdd) {
			return switch (length) {
				case 2 -> isOdd ? BlockEntityTypes.RAILWAY_SIGN_2_ODD.get() : BlockEntityTypes.RAILWAY_SIGN_2_EVEN.get();
				case 3 -> isOdd ? BlockEntityTypes.RAILWAY_SIGN_3_ODD.get() : BlockEntityTypes.RAILWAY_SIGN_3_EVEN.get();
				case 4 -> isOdd ? BlockEntityTypes.RAILWAY_SIGN_4_ODD.get() : BlockEntityTypes.RAILWAY_SIGN_4_EVEN.get();
				case 5 -> isOdd ? BlockEntityTypes.RAILWAY_SIGN_5_ODD.get() : BlockEntityTypes.RAILWAY_SIGN_5_EVEN.get();
				case 6 -> isOdd ? BlockEntityTypes.RAILWAY_SIGN_6_ODD.get() : BlockEntityTypes.RAILWAY_SIGN_6_EVEN.get();
				case 7 -> isOdd ? BlockEntityTypes.RAILWAY_SIGN_7_ODD.get() : BlockEntityTypes.RAILWAY_SIGN_7_EVEN.get();
				default -> BlockEntityTypes.RAILWAY_SIGN_2_EVEN.get();
			};
		}
	}
}
