package mtr.block;

import mtr.MTR;
import mtr.data.RailwayData;
import mtr.packet.PacketTrainDataGuiServer;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
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
				int i = 0;
				while (true) {
					final BlockPos checkPos = pos.offset(hitSide.rotateYClockwise(), i);
					final BlockState checkState = world.getBlockState(checkPos);
					if (checkState.getBlock() instanceof BlockRailwaySign) {
						if (!checkState.isOf(mtr.Blocks.RAILWAY_SIGN_MIDDLE) && IBlock.getStatePropertySafe(checkState, FACING) == hitSide.getOpposite()) {
							final RailwayData railwayData = RailwayData.getInstance(world);
							if (railwayData != null) {
								PacketTrainDataGuiServer.openRailwaySignScreenS2C((ServerPlayerEntity) player, railwayData.getStations(), railwayData.getPlatforms(world), railwayData.getRoutes(), checkPos);
							}
						}
					} else {
						return;
					}
					i++;
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
			return IBlock.getVoxelShapeByDirection(0, 0, 7, 16, 9, 9, facing);
		} else {
			final int xStart = getXStart();
			final VoxelShape main = IBlock.getVoxelShapeByDirection(xStart - 0.75, 0, 7, 16, 9, 9, facing);
			final VoxelShape pole = IBlock.getVoxelShapeByDirection(xStart - 2, 0, 7, xStart - 0.75, 16, 9, facing);
			return VoxelShapes.union(main, pole);
		}
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
		if (length % 2 == 1 && !isOdd) {
			return 4;
		} else if (length % 2 == 0 && !isOdd) {
			return 8;
		} else if (length % 2 == 1 && isOdd) {
			return 12;
		} else {
			return 16;
		}
	}

	private int getMiddleLength() {
		return isOdd ? (length + 2) / 4 * 2 - 1 : length / 4 * 2;
	}

	public static int[] serializeSignTypes(SignType[] signTypes) {
		final int[] ordinals = new int[signTypes.length];
		for (int i = 0; i < ordinals.length; i++) {
			ordinals[i] = signTypes[i] == null ? -1 : signTypes[i].ordinal();
		}
		return ordinals;
	}

	public static void deserializeSignTypes(int[] ordinals, SignType[] signTypes) {
		if (signTypes.length == ordinals.length) {
			for (int i = 0; i < ordinals.length; i++) {
				signTypes[i] = ordinals[i] < 0 ? null : SignType.values()[ordinals[i]];
			}
		}
	}

	public static class TileEntityRailwaySign extends BlockEntity implements BlockEntityClientSerializable {

		private final SignType[] signTypes;
		private static final String KEY_SIGN_TYPES = "sign_types";

		public TileEntityRailwaySign(int length, boolean isOdd) {
			super(getType(length, isOdd));
			signTypes = new SignType[length];
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
			deserializeSignTypes(tag.getIntArray(KEY_SIGN_TYPES), signTypes);
		}

		@Override
		public CompoundTag toClientTag(CompoundTag tag) {
			tag.putIntArray(KEY_SIGN_TYPES, serializeSignTypes(signTypes));
			return tag;
		}

		public void setSign(int[] ordinals) {
			deserializeSignTypes(ordinals, signTypes);
			markDirty();
			sync();
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
				default:
					return null;
			}
		}
	}

	public enum SignType {
		EXIT("exit", false, false),
		STAIRS("stairs", true, false),
		STAIRS_FLIPPED("stairs", true, true),
		ESCALATOR("escalator", true, false),
		ESCALATOR_FLIPPED("escalator", true, true),
		LIFT("lift", true, false),
		ARROW_LEFT("arrow", true, false),
		ARROW_RIGHT("arrow", true, true),
		TRAIN("train", true, false),
		TRAINS("train", true, false, true),
		TRAINS_FLIPPED("train", true, true, true),
		LINE("line", true, false, true),
		LINE_FLIPPED("line", true, true, true),
		LIFT_TEXT("lift", true, false, true),
		LIFT_TEXT_FLIPPED("lift", true, true, true),
		PLATFORM("circle", true, false, true),
		PLATFORM_FLIPPED("circle", true, true, true),
		CUSTOMER_SERVICE_CENTRE("customer_service_centre", true, false, true),
		CUSTOMER_SERVICE_CENTRE_FLIPPED("customer_service_centre", true, true, true);

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
