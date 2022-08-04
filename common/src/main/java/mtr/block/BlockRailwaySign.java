package mtr.block;

import mtr.BlockEntityTypes;
import mtr.mappings.*;
import mtr.packet.PacketTrainDataGuiServer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.*;

public class BlockRailwaySign extends BlockDirectionalMapper implements EntityBlockMapper, IBlock {

	public final int length;
	public final boolean isOdd;

	public static final float SMALL_SIGN_PERCENTAGE = 0.75F;

	public BlockRailwaySign(int length, boolean isOdd) {
		super(Properties.of(Material.METAL, MaterialColor.COLOR_GRAY).requiresCorrectToolForDrops().strength(2).lightLevel(state -> 15));
		this.length = length;
		this.isOdd = isOdd;
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult hit) {
		return IBlock.checkHoldingBrush(world, player, () -> {
			final Direction facing = IBlock.getStatePropertySafe(state, FACING);
			final Direction hitSide = hit.getDirection();
			if (hitSide == facing || hitSide == facing.getOpposite()) {
				final BlockPos checkPos = findEndWithDirection(world, pos, hitSide.getOpposite(), false);
				if (checkPos != null) {
					PacketTrainDataGuiServer.openRailwaySignScreenS2C((ServerPlayer) player, checkPos);
				}
			}
		});
	}

	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor world, BlockPos pos, BlockPos posFrom) {
		final Direction facing = IBlock.getStatePropertySafe(state, FACING);
		final boolean isNext = direction == facing.getClockWise() || state.is(mtr.Blocks.RAILWAY_SIGN_MIDDLE.get()) && direction == facing.getCounterClockWise();
		if (isNext && !(newState.getBlock() instanceof BlockRailwaySign)) {
			return Blocks.AIR.defaultBlockState();
		} else {
			return state;
		}
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		final Direction facing = ctx.getHorizontalDirection();
		return IBlock.isReplaceable(ctx, facing.getClockWise(), getMiddleLength() + 2) ? defaultBlockState().setValue(FACING, facing) : null;
	}

	@Override
	public void playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
		final Direction facing = IBlock.getStatePropertySafe(state, FACING);

		final BlockPos checkPos = findEndWithDirection(world, pos, facing, true);
		if (checkPos != null) {
			IBlock.onBreakCreative(world, player, checkPos);
		}

		super.playerWillDestroy(world, pos, state, player);
	}

	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		if (!world.isClientSide) {
			final Direction facing = IBlock.getStatePropertySafe(state, FACING);
			for (int i = 1; i <= getMiddleLength(); i++) {
				world.setBlock(pos.relative(facing.getClockWise(), i), mtr.Blocks.RAILWAY_SIGN_MIDDLE.get().defaultBlockState().setValue(FACING, facing), 3);
			}
			world.setBlock(pos.relative(facing.getClockWise(), getMiddleLength() + 1), defaultBlockState().setValue(FACING, facing.getOpposite()), 3);
			world.updateNeighborsAt(pos, Blocks.AIR);
			state.updateNeighbourShapes(world, pos, 3);
		}
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
		final Direction facing = IBlock.getStatePropertySafe(state, FACING);
		if (state.is(mtr.Blocks.RAILWAY_SIGN_MIDDLE.get())) {
			return IBlock.getVoxelShapeByDirection(0, 0, 7, 16, 12, 9, facing);
		} else {
			final int xStart = getXStart();
			final VoxelShape main = IBlock.getVoxelShapeByDirection(xStart - 0.75, 0, 7, 16, 12, 9, facing);
			final VoxelShape pole = IBlock.getVoxelShapeByDirection(xStart - 2, 0, 7, xStart - 0.75, 16, 9, facing);
			return Shapes.or(main, pole);
		}
	}

	@Override
	public String getDescriptionId() {
		return "block.mtr.railway_sign";
	}

	@Override
	public void appendHoverText(ItemStack itemStack, BlockGetter blockGetter, List<Component> tooltip, TooltipFlag tooltipFlag) {
		tooltip.add(Text.translatable("tooltip.mtr.railway_sign_length", length).setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
		tooltip.add(Text.translatable(isOdd ? "tooltip.mtr.railway_sign_odd" : "tooltip.mtr.railway_sign_even").setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
	}

	@Override
	public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
		if (this == mtr.Blocks.RAILWAY_SIGN_MIDDLE.get()) {
			return null;
		} else {
			return new TileEntityRailwaySign(length, isOdd, pos, state);
		}
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	public int getXStart() {
		switch (length % 4) {
			default:
				return isOdd ? 8 : 16;
			case 1:
				return isOdd ? 4 : 12;
			case 2:
				return isOdd ? 16 : 8;
			case 3:
				return isOdd ? 12 : 4;
		}
	}

	private int getMiddleLength() {
		return (length - (4 - getXStart() / 4)) / 2;
	}

	private BlockPos findEndWithDirection(Level world, BlockPos startPos, Direction direction, boolean allowOpposite) {
		int i = 0;
		while (true) {
			final BlockPos checkPos = startPos.relative(direction.getCounterClockWise(), i);
			final BlockState checkState = world.getBlockState(checkPos);
			if (checkState.getBlock() instanceof BlockRailwaySign) {
				final Direction facing = IBlock.getStatePropertySafe(checkState, FACING);
				if (!checkState.is(mtr.Blocks.RAILWAY_SIGN_MIDDLE.get()) && (facing == direction || allowOpposite && facing == direction.getOpposite())) {
					return checkPos;
				}
			} else {
				return null;
			}
			i++;
		}
	}

	public static class TileEntityRailwaySign extends BlockEntityClientSerializableMapper {

		private final Set<Long> selectedIds;
		private final String[] signIds;
		private static final String KEY_SELECTED_IDS = "selected_ids";
		private static final String KEY_SIGN_LENGTH = "sign_length";

		public TileEntityRailwaySign(int length, boolean isOdd, BlockPos pos, BlockState state) {
			super(getType(length, isOdd), pos, state);
			signIds = new String[length];
			selectedIds = new HashSet<>();
		}

		@Override
		public void readCompoundTag(CompoundTag compoundTag) {
			selectedIds.clear();
			Arrays.stream(compoundTag.getLongArray(KEY_SELECTED_IDS)).forEach(selectedIds::add);
			for (int i = 0; i < signIds.length; i++) {
				final String signId = compoundTag.getString(KEY_SIGN_LENGTH + i);
				signIds[i] = signId.isEmpty() ? null : signId;
			}
		}

		@Override
		public void writeCompoundTag(CompoundTag compoundTag) {
			compoundTag.putLongArray(KEY_SELECTED_IDS, new ArrayList<>(selectedIds));
			for (int i = 0; i < signIds.length; i++) {
				compoundTag.putString(KEY_SIGN_LENGTH + i, signIds[i] == null ? "" : signIds[i]);
			}
		}

		public AABB getRenderBoundingBox() {
			return new AABB(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
		}

		public void setData(Set<Long> selectedIds, String[] signTypes) {
			this.selectedIds.clear();
			this.selectedIds.addAll(selectedIds);
			if (signIds.length == signTypes.length) {
				System.arraycopy(signTypes, 0, signIds, 0, signTypes.length);
			}
			setChanged();
			syncData();
		}

		public Set<Long> getSelectedIds() {
			return selectedIds;
		}

		public String[] getSignIds() {
			return signIds;
		}

		private static BlockEntityType<?> getType(int length, boolean isOdd) {
			switch (length) {
				case 2:
					return isOdd ? BlockEntityTypes.RAILWAY_SIGN_2_ODD_TILE_ENTITY.get() : BlockEntityTypes.RAILWAY_SIGN_2_EVEN_TILE_ENTITY.get();
				case 3:
					return isOdd ? BlockEntityTypes.RAILWAY_SIGN_3_ODD_TILE_ENTITY.get() : BlockEntityTypes.RAILWAY_SIGN_3_EVEN_TILE_ENTITY.get();
				case 4:
					return isOdd ? BlockEntityTypes.RAILWAY_SIGN_4_ODD_TILE_ENTITY.get() : BlockEntityTypes.RAILWAY_SIGN_4_EVEN_TILE_ENTITY.get();
				case 5:
					return isOdd ? BlockEntityTypes.RAILWAY_SIGN_5_ODD_TILE_ENTITY.get() : BlockEntityTypes.RAILWAY_SIGN_5_EVEN_TILE_ENTITY.get();
				case 6:
					return isOdd ? BlockEntityTypes.RAILWAY_SIGN_6_ODD_TILE_ENTITY.get() : BlockEntityTypes.RAILWAY_SIGN_6_EVEN_TILE_ENTITY.get();
				case 7:
					return isOdd ? BlockEntityTypes.RAILWAY_SIGN_7_ODD_TILE_ENTITY.get() : BlockEntityTypes.RAILWAY_SIGN_7_EVEN_TILE_ENTITY.get();
				default:
					return null;
			}
		}
	}

	public enum SignType {
		ARROW_LEFT("arrow", true, false),
		ARROW_RIGHT("arrow", true, true),
		ARROW_UP("arrow_up", true, false),
		ARROW_DOWN("arrow_down", true, false),
		ARROW_UP_LEFT("arrow_up_left", true, false),
		ARROW_UP_RIGHT("arrow_up_left", true, true),
		ARROW_DOWN_LEFT("arrow_down_left", true, false),
		ARROW_DOWN_RIGHT("arrow_down_left", true, true),
		ARROW_TURN_BACK_LEFT("arrow_turn_back", true, false),
		ARROW_TURN_BACK_RIGHT("arrow_turn_back", true, true),
		EXIT_1("exit_1", false, false),
		EXIT_2("exit_2", true, false),
		EXIT_3("exit_3", true, false),
		ESCALATOR("escalator", true, false),
		ESCALATOR_FLIPPED("escalator", true, true),
		STAIRS_UP("stairs_up", true, false),
		STAIRS_UP_FLIPPED("stairs_up", true, true),
		STAIRS_DOWN_FLIPPED("stairs_down", true, true),
		STAIRS_DOWN("stairs_down", true, false),
		LIFT_1("lift_1", true, false),
		LIFT_2("lift_2", true, false),
		WHEELCHAIR("wheelchair", true, false),
		TOILET("toilets", false, false),
		FEMALE("female", true, false),
		MALE("male", true, false),
		TRAIN("train", true, false),
		TRAIN_OLD("train_old", true, false),
		AIRPORT_EXPRESS("airport_express", true, false),
		LIGHT_RAIL_1("light_rail_1", true, false),
		LIGHT_RAIL_2("light_rail_2", false, false),
		LIGHT_RAIL_3("light_rail_3", true, false),
		LIGHT_RAIL_4("light_rail_4", false, false),
		XRL_1("xrl_1", true, false),
		XRL_2("xrl_2", true, false),
		SP1900("sp1900", true, false),
		YELLOW_HEAD_1("yellow_head_1", true, false),
		YELLOW_HEAD_2("yellow_head_2", false, false),
		BOAT("boat", true, false),
		CABLE_CAR("cable_car", true, false),
		CROSS("cross", true, false),
		LOGO("logo", false, false),
		EXIT_LETTER("exit_letter", true, false, true),
		EXIT_LETTER_FLIPPED("exit_letter", true, true, true),
		ESCALATOR_TO_CONCOURSE_UP("escalator", "escalator_to_concourse_up", true, true, false, true, 0),
		ESCALATOR_TO_CONCOURSE_UP_FLIPPED("escalator", "escalator_to_concourse_up", true, false, true, true, 0),
		ESCALATOR_TO_CONCOURSE_DOWN("escalator", "escalator_to_concourse_down", true, false, false, true, 0),
		ESCALATOR_TO_CONCOURSE_DOWN_FLIPPED("escalator", "escalator_to_concourse_down", true, true, true, true, 0),
		PLATFORM("platform", true, false, true),
		PLATFORM_FLIPPED("platform", true, true, true),
		LINE("line", true, false, true),
		LINE_FLIPPED("line", true, true, true),
		LIFT_1_TEXT("lift_1", true, false, true),
		LIFT_1_TEXT_FLIPPED("lift_1", true, true, true),
		LIFT_2_TEXT("lift_2", true, false, true),
		LIFT_2_TEXT_FLIPPED("lift_2", true, true, true),
		TOILETS("toilets", false, false, true),
		TOILETS_FLIPPED("toilets", false, true, true),
		FEMALE_TOILETS("female", true, false, true),
		FEMALE_TOILETS_FLIPPED("female", true, true, true),
		MALE_TOILETS("male", true, false, true),
		MALE_TOILETS_FLIPPED("male", true, true, true),
		WHEELCHAIR_TOILETS("wheelchair", true, false, true),
		WHEELCHAIR_TOILETS_FLIPPED("wheelchair", true, true, true),
		TRAINS("train", true, false, true),
		TRAINS_FLIPPED("train", true, true, true),
		TRAINS_OLD("train_old", true, false, true),
		TRAINS_OLD_FLIPPED("train_old", true, true, true),
		AIRPORT_EXPRESS_TRAINS("airport_express", true, false, true),
		AIRPORT_EXPRESS_TRAINS_FLIPPED("airport_express", true, true, true),
		AIRPORT_EXPRESS_TRAINS_CITY("airport_express", "airport_express_city", true, false, true),
		AIRPORT_EXPRESS_TRAINS_CITY_FLIPPED("airport_express", "airport_express_city", true, true, true),
		IN_TOWN_CHECK_IN("check_in", "in_town_check_in", true, false, true),
		IN_TOWN_CHECK_IN_FLIPPED("check_in", "in_town_check_in", true, true, true),
		CHECK_IN_PASSENGERS("check_in", "check_in_passengers", true, false, true),
		CHECK_IN_PASSENGERS_FLIPPED("check_in", "check_in_passengers", true, true, true),
		LIGHT_RAIL_1_TRAINS("light_rail_1", true, false, true),
		LIGHT_RAIL_1_TRAINS_FLIPPED("light_rail_1", true, true, true),
		LIGHT_RAIL_2_TRAINS("light_rail_2", false, false, true),
		LIGHT_RAIL_2_TRAINS_FLIPPED("light_rail_2", false, true, true),
		LIGHT_RAIL_3_TRAINS("light_rail_3", true, false, true),
		LIGHT_RAIL_3_TRAINS_FLIPPED("light_rail_3", true, true, true),
		LIGHT_RAIL_4_TRAINS("light_rail_4", false, false, true),
		LIGHT_RAIL_4_TRAINS_FLIPPED("light_rail_4", false, true, true),
		XRL_1_TRAINS("xrl_1", true, false, true),
		XRL_1_TRAINS_FLIPPED("xrl_1", true, true, true),
		XRL_2_TRAINS("xrl_2", true, false, true),
		XRL_2_TRAINS_FLIPPED("xrl_2", true, true, true),
		SP1900_TRAINS("sp1900", true, false, true),
		SP1900_TRAINS_FLIPPED("sp1900", true, true, true),
		YELLOW_HEAD_1_TRAINS("yellow_head_1", true, false, true),
		YELLOW_HEAD_1_TRAINS_FLIPPED("yellow_head_1", true, true, true),
		YELLOW_HEAD_2_TRAINS("yellow_head_2", false, false, true),
		YELLOW_HEAD_2_TRAINS_FLIPPED("yellow_head_2", false, true, true),
		BOAT_BOATS("boat", true, false, true),
		BOAT_BOATS_FLIPPED("boat", true, true, true),
		CABLE_CAR_CABLE_CARS("cable_car", true, false, true),
		CABLE_CAR_CABLE_CARS_FLIPPED("cable_car", true, true, true),
		CUSTOMER_SERVICE_CENTRE("customer_service_centre", true, false, true),
		CUSTOMER_SERVICE_CENTRE_FLIPPED("customer_service_centre", true, true, true),
		TICKETS("tickets", true, false, true),
		TICKETS_FLIPPED("tickets", true, true, true),
		NO_ENTRY("cross", true, false, true),
		NO_ENTRY_FLIPPED("cross", true, true, true),
		EMERGENCY_EXIT("emergency_exit", "emergency_exit", false, false, false, true, 0x00944F),
		EMERGENCY_EXIT_FLIPPED("emergency_exit", "emergency_exit", false, true, true, true, 0x00944F),
		WIFI("wifi", "wifi", true, false, false, true, 0xFA7B22),
		WIFI_FLIPPED("wifi", "wifi", true, true, true, true, 0xFA7B22),
		LOGO_TEXT("logo", false, false, true),
		LOGO_TEXT_FLIPPED("logo", false, true, true);

		public final ResourceLocation textureId;
		public final String customText;
		public final boolean small;
		public final boolean flipTexture;
		public final boolean flipCustomText;
		public final int backgroundColor;

		SignType(String texture, String translation, boolean small, boolean flipTexture, boolean flipCustomText, boolean hasCustomText, int backgroundColor) {
			textureId = new ResourceLocation("mtr:textures/sign/" + texture + ".png");
			customText = hasCustomText ? Text.translatable("sign.mtr." + translation + "_cjk").append("|").append(Text.translatable("sign.mtr." + translation)).getString() : "";
			this.small = small;
			this.flipTexture = flipTexture;
			this.flipCustomText = flipCustomText;
			this.backgroundColor = backgroundColor;
		}

		SignType(String texture, String translation, boolean small, boolean flipTexture, boolean hasCustomText) {
			this(texture, translation, small, false, flipTexture, hasCustomText, 0);
		}

		SignType(String texture, boolean small, boolean flipCustomText, boolean hasCustomText) {
			this(texture, texture, small, false, flipCustomText, hasCustomText, 0);
		}

		SignType(String texture, boolean small, boolean flipTexture) {
			this(texture, texture, small, flipTexture, false, false, 0);
		}
	}
}
