package mtr.block;

import mtr.MTR;
import mtr.data.RailwayData;
import mtr.packet.PacketTrainDataGuiServer;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.*;

public class BlockRailwaySign extends HorizontalFacingBlock implements BlockEntityProvider, IBlock {

	public final int length;
	public final boolean isOdd;

	public static final float SMALL_SIGN_PERCENTAGE = 0.75F;

	public BlockRailwaySign(int length, boolean isOdd) {
		super(FabricBlockSettings.of(Material.METAL, MaterialColor.IRON).requiresTool().hardness(2).luminance(15));
		this.length = length;
		this.isOdd = isOdd;
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		return IBlock.checkHoldingBrush(world, player, () -> {
			final Direction facing = IBlock.getStatePropertySafe(state, FACING);
			final Direction hitSide = hit.getSide();
			if (hitSide == facing || hitSide == facing.getOpposite()) {
				final BlockPos checkPos = findEndWithDirection(world, pos, hitSide.getOpposite(), hitSide.getOpposite());
				if (checkPos != null) {
					final RailwayData railwayData = RailwayData.getInstance(world);
					if (railwayData != null) {
						PacketTrainDataGuiServer.openRailwaySignScreenS2C((ServerPlayerEntity) player, railwayData.getStations(), railwayData.getPlatforms(world), railwayData.getRoutes(), checkPos);
					}
				}
			}
		});
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
		final Direction facing = IBlock.getStatePropertySafe(state, FACING);
		final boolean isNext = direction == facing.rotateYClockwise() || is(mtr.Blocks.RAILWAY_SIGN_MIDDLE) && direction == facing.rotateYCounterclockwise();
		if (isNext && !(newState.getBlock() instanceof BlockRailwaySign)) {
			return Blocks.AIR.getDefaultState();
		} else {
			return state;
		}
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		final Direction facing = ctx.getPlayerFacing();
		return IBlock.isReplaceable(ctx, facing.rotateYClockwise(), getMiddleLength() + 2) ? getDefaultState().with(FACING, facing) : null;
	}

	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		final Direction facing = IBlock.getStatePropertySafe(state, FACING);
		final boolean isNorthOrSouth = facing == Direction.NORTH || facing == Direction.SOUTH;

		final BlockPos checkPos = findEndWithDirection(world, pos, facing, isNorthOrSouth ? Direction.NORTH : Direction.EAST);
		if (checkPos != null) {
			IBlock.onBreakCreative(world, player, checkPos);
		} else {
			final BlockPos checkPos2 = findEndWithDirection(world, pos, facing.getOpposite(), isNorthOrSouth ? Direction.NORTH : Direction.EAST);
			if (checkPos2 != null) {
				IBlock.onBreakCreative(world, player, checkPos2);
			}
		}

		super.onBreak(world, pos, state, player);
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		if (!world.isClient) {
			final Direction facing = IBlock.getStatePropertySafe(state, FACING);
			for (int i = 1; i <= getMiddleLength(); i++) {
				world.setBlockState(pos.offset(facing.rotateYClockwise(), i), mtr.Blocks.RAILWAY_SIGN_MIDDLE.getDefaultState().with(FACING, facing), 3);
			}
			world.setBlockState(pos.offset(facing.rotateYClockwise(), getMiddleLength() + 1), getDefaultState().with(FACING, facing.getOpposite()), 3);
			world.updateNeighbors(pos, Blocks.AIR);
			state.updateNeighbors(world, pos, 3);
		}
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		final Direction facing = IBlock.getStatePropertySafe(state, FACING);
		if (is(mtr.Blocks.RAILWAY_SIGN_MIDDLE)) {
			return IBlock.getVoxelShapeByDirection(0, 0, 7, 16, 12, 9, facing);
		} else {
			final int xStart = getXStart();
			final VoxelShape main = IBlock.getVoxelShapeByDirection(xStart - 0.75, 0, 7, 16, 12, 9, facing);
			final VoxelShape pole = IBlock.getVoxelShapeByDirection(xStart - 2, 0, 7, xStart - 0.75, 16, 9, facing);
			return VoxelShapes.union(main, pole);
		}
	}

	@Override
	public String getTranslationKey() {
		return "block.mtr.railway_sign";
	}

	@Override
	public void appendTooltip(ItemStack stack, BlockView world, List<Text> tooltip, TooltipContext options) {
		tooltip.add(new TranslatableText("tooltip.mtr.railway_sign_length", length).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
		tooltip.add(new TranslatableText(isOdd ? "tooltip.mtr.railway_sign_odd" : "tooltip.mtr.railway_sign_even").setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		if (is(mtr.Blocks.RAILWAY_SIGN_MIDDLE)) {
			return null;
		} else {
			return new TileEntityRailwaySign(length, isOdd);
		}
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
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

	private BlockPos findEndWithDirection(World world, BlockPos startPos, Direction startDirection, Direction endDirection) {
		int i = 0;
		while (true) {
			final BlockPos checkPos = startPos.offset(startDirection.rotateYCounterclockwise(), i);
			final BlockState checkState = world.getBlockState(checkPos);
			if (checkState.getBlock() instanceof BlockRailwaySign) {
				if (!checkState.isOf(mtr.Blocks.RAILWAY_SIGN_MIDDLE) && IBlock.getStatePropertySafe(checkState, FACING) == endDirection) {
					return checkPos;
				}
			} else {
				return null;
			}
			i++;
		}
	}

	public static class TileEntityRailwaySign extends BlockEntity implements BlockEntityClientSerializable {

		private final Set<Long> selectedIds;
		private final SignType[] signTypes;
		private static final String KEY_SELECTED_IDS = "selected_ids";
		private static final String KEY_SIGN_LENGTH = "sign_length";

		public TileEntityRailwaySign(int length, boolean isOdd) {
			super(getType(length, isOdd));
			signTypes = new SignType[length];
			selectedIds = new HashSet<>();
		}

		@Override
		public void fromTag(BlockState state, CompoundTag tag) {
			super.fromTag(state, tag);
			fromClientTag(tag);
		}

		@Override
		public CompoundTag toTag(CompoundTag tag) {
			super.toTag(tag);
			toClientTag(tag);
			return tag;
		}

		@Override
		public void fromClientTag(CompoundTag tag) {
			selectedIds.clear();
			Arrays.stream(tag.getLongArray(KEY_SELECTED_IDS)).forEach(selectedIds::add);
			for (int i = 0; i < signTypes.length; i++) {
				try {
					signTypes[i] = SignType.valueOf(tag.getString(KEY_SIGN_LENGTH + i));
				} catch (Exception e) {
					signTypes[i] = null;
				}
			}
		}

		@Override
		public CompoundTag toClientTag(CompoundTag tag) {
			tag.putLongArray(KEY_SELECTED_IDS, new ArrayList<>(selectedIds));
			for (int i = 0; i < signTypes.length; i++) {
				tag.putString(KEY_SIGN_LENGTH + i, signTypes[i] == null ? "" : signTypes[i].toString());
			}
			return tag;
		}

		public void setData(Set<Long> selectedIds, SignType[] signTypes) {
			this.selectedIds.clear();
			this.selectedIds.addAll(selectedIds);
			if (this.signTypes.length == signTypes.length) {
				System.arraycopy(signTypes, 0, this.signTypes, 0, signTypes.length);
			}
			markDirty();
			sync();
		}

		public Set<Long> getSelectedIds() {
			return selectedIds;
		}

		public SignType[] getSign() {
			return signTypes;
		}

		private static BlockEntityType<?> getType(int length, boolean isOdd) {
			switch (length) {
				case 2:
					return isOdd ? MTR.RAILWAY_SIGN_2_ODD_TILE_ENTITY : MTR.RAILWAY_SIGN_2_EVEN_TILE_ENTITY;
				case 3:
					return isOdd ? MTR.RAILWAY_SIGN_3_ODD_TILE_ENTITY : MTR.RAILWAY_SIGN_3_EVEN_TILE_ENTITY;
				case 4:
					return isOdd ? MTR.RAILWAY_SIGN_4_ODD_TILE_ENTITY : MTR.RAILWAY_SIGN_4_EVEN_TILE_ENTITY;
				case 5:
					return isOdd ? MTR.RAILWAY_SIGN_5_ODD_TILE_ENTITY : MTR.RAILWAY_SIGN_5_EVEN_TILE_ENTITY;
				case 6:
					return isOdd ? MTR.RAILWAY_SIGN_6_ODD_TILE_ENTITY : MTR.RAILWAY_SIGN_6_EVEN_TILE_ENTITY;
				case 7:
					return isOdd ? MTR.RAILWAY_SIGN_7_ODD_TILE_ENTITY : MTR.RAILWAY_SIGN_7_EVEN_TILE_ENTITY;
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
		YELLOW_HEAD_1("yellow_head_1", true, false),
		YELLOW_HEAD_2("yellow_head_2", false, false),
		CROSS("cross", true, false),
		LOGO("logo", false, false),
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
		YELLOW_HEAD_1_TRAINS("yellow_head_1", true, false, true),
		YELLOW_HEAD_1_TRAINS_FLIPPED("yellow_head_1", true, true, true),
		YELLOW_HEAD_2_TRAINS("yellow_head_2", false, false, true),
		YELLOW_HEAD_2_TRAINS_FLIPPED("yellow_head_2", false, true, true),
		CUSTOMER_SERVICE_CENTRE("customer_service_centre", true, false, true),
		CUSTOMER_SERVICE_CENTRE_FLIPPED("customer_service_centre", true, true, true),
		TICKETS("tickets", true, false, true),
		TICKETS_FLIPPED("tickets", true, true, true),
		NO_ENTRY("cross", true, false, true),
		NO_ENTRY_FLIPPED("cross", true, true, true),
		LOGO_TEXT("logo", false, false, true),
		LOGO_TEXT_FLIPPED("logo", false, true, true);

		public final Identifier id;
		public final String text;
		public final boolean small;
		public final boolean flipped;
		public final boolean hasCustomText;

		SignType(String texture, boolean small, boolean flipped, boolean hasCustomText) {
			id = new Identifier("mtr:textures/sign/" + texture + ".png");
			text = new TranslatableText("sign.mtr." + texture + "_cjk").append("|").append(new TranslatableText("sign.mtr." + texture)).getString();
			this.small = small;
			this.flipped = flipped;
			this.hasCustomText = hasCustomText;
		}

		SignType(String texture, boolean small, boolean flipped) {
			this(texture, small, flipped, false);
		}
	}
}
