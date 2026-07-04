package org.mtr.block;

import lombok.Getter;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import org.mtr.packet.PacketOpenBlockEntityScreen;
import org.mtr.registry.BlockEntityTypes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

//? if >= 1.21.4 {
import net.minecraft.world.level.ScheduledTickAccess;
//? } else {
/*import net.minecraft.world.level.LevelAccessor;
 *///? }

public class BlockRailwaySign extends Block implements IBlock, EntityBlock {

	public final int length;
	public final boolean isOdd;

	// Legacy sign IDs in MTR 3.x.x. In MTR 4.x.x they are migrated to use lower-case.
	private static final String[] LEGACY_SIGNS = {"ARROW_LEFT", "ARROW_RIGHT", "ARROW_UP", "ARROW_DOWN", "ARROW_UP_LEFT", "ARROW_UP_RIGHT", "ARROW_DOWN_LEFT", "ARROW_DOWN_RIGHT", "ARROW_TURN_BACK_LEFT", "ARROW_TURN_BACK_RIGHT", "EXIT_1", "EXIT_2", "EXIT_3", "ESCALATOR", "ESCALATOR_FLIPPED", "STAIRS_UP", "STAIRS_UP_FLIPPED", "STAIRS_DOWN_FLIPPED", "STAIRS_DOWN", "LIFT_1", "LIFT_2", "WHEELCHAIR", "TOILET", "FEMALE", "MALE", "TRAIN", "TRAIN_OLD", "AIRPORT_EXPRESS", "LIGHT_RAIL_1", "LIGHT_RAIL_2", "LIGHT_RAIL_3", "LIGHT_RAIL_4", "XRL_1", "XRL_2", "SP1900", "YELLOW_HEAD_1", "YELLOW_HEAD_2", "BOAT", "CABLE_CAR", "AIRPLANE", "AIRPLANE_LEFT", "AIRPLANE_RIGHT", "AIRPLANE_UP_LEFT", "AIRPLANE_UP_RIGHT", "CROSS", "LOGO", "EXIT_LETTER", "EXIT_LETTER_FLIPPED", "ESCALATOR_TO_CONCOURSE_UP", "ESCALATOR_TO_CONCOURSE_UP_FLIPPED", "ESCALATOR_TO_CONCOURSE_DOWN", "ESCALATOR_TO_CONCOURSE_DOWN_FLIPPED", "PLATFORM", "PLATFORM_FLIPPED", "LINE", "LINE_FLIPPED", "STATION", "STATION_FLIPPED", "LIFT_1_TEXT", "LIFT_1_TEXT_FLIPPED", "LIFT_2_TEXT", "LIFT_2_TEXT_FLIPPED", "TOILETS", "TOILETS_FLIPPED", "FEMALE_TOILETS", "FEMALE_TOILETS_FLIPPED", "MALE_TOILETS", "MALE_TOILETS_FLIPPED", "WHEELCHAIR_TOILETS", "WHEELCHAIR_TOILETS_FLIPPED", "TRAINS", "TRAINS_FLIPPED", "TRAINS_OLD", "TRAINS_OLD_FLIPPED", "AIRPORT_EXPRESS_TRAINS", "AIRPORT_EXPRESS_TRAINS_FLIPPED", "AIRPORT_EXPRESS_TRAINS_CITY", "AIRPORT_EXPRESS_TRAINS_CITY_FLIPPED", "IN_TOWN_CHECK_IN", "IN_TOWN_CHECK_IN_FLIPPED", "CHECK_IN_PASSENGERS", "CHECK_IN_PASSENGERS_FLIPPED", "LIGHT_RAIL_1_TRAINS", "LIGHT_RAIL_1_TRAINS_FLIPPED", "LIGHT_RAIL_2_TRAINS", "LIGHT_RAIL_2_TRAINS_FLIPPED", "LIGHT_RAIL_3_TRAINS", "LIGHT_RAIL_3_TRAINS_FLIPPED", "LIGHT_RAIL_4_TRAINS", "LIGHT_RAIL_4_TRAINS_FLIPPED", "XRL_1_TRAINS", "XRL_1_TRAINS_FLIPPED", "XRL_2_TRAINS", "XRL_2_TRAINS_FLIPPED", "SP1900_TRAINS", "SP1900_TRAINS_FLIPPED", "YELLOW_HEAD_1_TRAINS", "YELLOW_HEAD_1_TRAINS_FLIPPED", "YELLOW_HEAD_2_TRAINS", "YELLOW_HEAD_2_TRAINS_FLIPPED", "BOAT_BOATS", "BOAT_BOATS_FLIPPED", "CABLE_CAR_CABLE_CARS", "CABLE_CAR_CABLE_CARS_FLIPPED", "AIRPORT", "AIRPORT_FLIPPED", "AIRPORT_LEFT", "AIRPORT_RIGHT", "AIRPORT_UP_LEFT", "AIRPORT_UP_RIGHT", "AIRPORT_ARRIVALS", "AIRPORT_ARRIVALS_FLIPPED", "AIRPORT_DEPARTURES", "AIRPORT_DEPARTURES_FLIPPED", "AIRPORT_TRANSFER", "AIRPORT_TRANSFER_FLIPPED", "BAGGAGE_CLAIM", "BAGGAGE_CLAIM_FLIPPED", "CUSTOMER_SERVICE_CENTRE", "CUSTOMER_SERVICE_CENTRE_FLIPPED", "TICKETS", "TICKETS_FLIPPED", "NO_ENTRY", "NO_ENTRY_FLIPPED", "EMERGENCY_EXIT", "EMERGENCY_EXIT_FLIPPED", "WIFI", "WIFI_FLIPPED", "LOGO_TEXT", "LOGO_TEXT_FLIPPED"};

	public BlockRailwaySign(BlockBehaviour.Properties settings, int length, boolean isOdd) {
		super(settings.lightLevel(blockState -> 15));
		this.length = length;
		this.isOdd = isOdd;
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
		return IBlock.checkHoldingBrush(world, player, () -> {
			final Direction facing = IBlock.getStatePropertySafe(state, BlockStateProperties.HORIZONTAL_FACING);
			final Direction hitSide = hit.getDirection();
			if (hitSide == facing || hitSide == facing.getOpposite()) {
				final BlockPos checkPos = findEndWithDirection(world, pos, hitSide.getOpposite(), false);
				if (checkPos != null) {
					PacketOpenBlockEntityScreen.sendDirectlyToServer((ServerLevel) world, (ServerPlayer) player, checkPos);
				}
			}
		});
	}

	@Override
//? if >= 1.21.4 {
	protected BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
//? } else {
	/*protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
//
*///? }
		final Direction facing = IBlock.getStatePropertySafe(state, BlockStateProperties.HORIZONTAL_FACING);
		final boolean isNext = direction == facing.getClockWise() || isRailwaySignMiddle(state) && direction == facing.getCounterClockWise();
		if (isNext && !(neighborState.getBlock() instanceof BlockRailwaySign)) {
			return Blocks.AIR.defaultBlockState();
		} else {
			return state;
		}
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		final Direction facing = ctx.getHorizontalDirection();
		return IBlock.isReplaceable(ctx, facing.getClockWise(), getMiddleLength() + 2) ? defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, facing) : null;
	}

	@Override
	public BlockState playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
		final Direction facing = IBlock.getStatePropertySafe(state, BlockStateProperties.HORIZONTAL_FACING);

		final BlockPos checkPos = findEndWithDirection(world, pos, facing, true);
		if (checkPos != null) {
			IBlock.playerWillDestroyCreative(world, player, checkPos);
		}

		return super.playerWillDestroy(world, pos, state, player);
	}

	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		if (!world.isClientSide()) {
			final Direction facing = IBlock.getStatePropertySafe(state, BlockStateProperties.HORIZONTAL_FACING);
			for (int i = 1; i <= getMiddleLength(); i++) {
				final Block railwaySignMiddle = getRailwaySignMiddle();
				if (railwaySignMiddle != null) {
					world.setBlock(pos.relative(facing.getClockWise(), i), railwaySignMiddle.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, facing), 3);
				}
			}
			world.setBlock(pos.relative(facing.getClockWise(), getMiddleLength() + 1), defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, facing.getOpposite()), 3);
			world.blockUpdated(pos, Blocks.AIR);
			state.updateNeighbourShapes(world, pos, 3);
		}
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		final Direction facing = IBlock.getStatePropertySafe(state, BlockStateProperties.HORIZONTAL_FACING);
		if (isRailwaySignMiddle(state)) {
			return IBlock.getVoxelShapeByDirection(0, 0, 7, 16, 12, 9, facing);
		} else {
			final int xStart = getXStart();
			final VoxelShape main = IBlock.getVoxelShapeByDirection(xStart - 0.75, 0, 7, 16, 12, 9, facing);
			final VoxelShape pole = IBlock.getVoxelShapeByDirection(xStart - 2, 0, 7, xStart - 0.75, 16, 9, facing);
			return Shapes.or(main, pole);
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag options) {
		tooltip.add(TranslationProvider.TOOLTIP_MTR_RAILWAY_SIGN_LENGTH.getMutableText(length).withStyle(ChatFormatting.GRAY));
		tooltip.add((isOdd ? TranslationProvider.TOOLTIP_MTR_RAILWAY_SIGN_ODD : TranslationProvider.TOOLTIP_MTR_RAILWAY_SIGN_EVEN).getMutableText().withStyle(ChatFormatting.GRAY));
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		if (this == getRailwaySignMiddle()) {
			return null;
		} else {
			return new RailwaySignBlockEntity(length, isOdd, blockPos, blockState);
		}
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.HORIZONTAL_FACING);
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

	@Nullable
	private BlockPos findEndWithDirection(Level world, BlockPos startPos, Direction direction, boolean allowOpposite) {
		int i = 0;
		while (true) {
			final BlockPos checkPos = startPos.relative(direction.getCounterClockWise(), i);
			final BlockState checkState = world.getBlockState(checkPos);
			if (checkState.getBlock() instanceof BlockRailwaySign) {
				final Direction facing = IBlock.getStatePropertySafe(checkState, BlockStateProperties.HORIZONTAL_FACING);
				if (!isRailwaySignMiddle(checkState) && (facing == direction || allowOpposite && facing == direction.getOpposite())) {
					return checkPos;
				}
			} else {
				return null;
			}
			i++;
		}
	}

	@Nullable
	private static Block getRailwaySignMiddle() {
		return org.mtr.registry.Blocks.RAILWAY_SIGN_MIDDLE == null ? null : org.mtr.registry.Blocks.RAILWAY_SIGN_MIDDLE.get();
	}

	private static boolean isRailwaySignMiddle(BlockState blockState) {
		final Block railwaySignMiddle = getRailwaySignMiddle();
		return railwaySignMiddle != null && blockState.is(railwaySignMiddle);
	}

	public static class RailwaySignBlockEntity extends BlockEntityExtension {

		@Getter
		private final LongAVLTreeSet[] selectedIds;
		@Getter
		private final @Nullable String[] signIds;
		private static final String KEY_SELECTED_IDS = "selected_ids";
		private static final String KEY_SIGN_LENGTH = "sign_length";

		public RailwaySignBlockEntity(int length, boolean isOdd, BlockPos pos, BlockState state) {
			super(getType(length, isOdd), pos, state);
			signIds = new String[length];
			selectedIds = new LongAVLTreeSet[length];
			for (int i = 0; i < selectedIds.length; i++) {
				selectedIds[i] = new LongAVLTreeSet();
			}
		}

		@Override
		protected void readNbt(CompoundTag nbtCompound) {
			final LongAVLTreeSet legacySelectedIds = new LongAVLTreeSet();
			Arrays.stream(nbtCompound.getLongArray(KEY_SELECTED_IDS)).forEach(legacySelectedIds::add);

			for (int i = 0; i < signIds.length; i++) {
				selectedIds[i].clear();
				selectedIds[i].addAll(legacySelectedIds);
				Arrays.stream(nbtCompound.getLongArray(KEY_SELECTED_IDS + i)).forEach(selectedIds[i]::add);

				final String signId = nbtCompound.getString(KEY_SIGN_LENGTH + i);
				signIds[i] = signId.isEmpty() ? null : (Arrays.asList(LEGACY_SIGNS).contains(signId) ? signId.toLowerCase(Locale.ENGLISH) : signId);
			}
		}

		@Override
		protected void writeNbt(CompoundTag nbtCompound) {
			for (int i = 0; i < signIds.length; i++) {
				nbtCompound.putLongArray(KEY_SELECTED_IDS + i, new ArrayList<>(selectedIds[i]));
				nbtCompound.putString(KEY_SIGN_LENGTH + i, signIds[i] == null ? "" : signIds[i]);
			}
		}

		public void setData(LongAVLTreeSet[] selectedIds, @Nullable String[] signIds) {
			for (int i = 0; i < this.signIds.length; i++) {
				final LongAVLTreeSet selectedIdsCopy = new LongAVLTreeSet(selectedIds[i]);
				this.selectedIds[i].clear();
				this.selectedIds[i].addAll(selectedIdsCopy);
			}

			if (this.signIds.length == signIds.length) {
				System.arraycopy(signIds, 0, this.signIds, 0, signIds.length);
			}

			setChanged();
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
