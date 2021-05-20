package mtr.block;

import mtr.MTR;
import mtr.data.Rail;
import mtr.data.RailType;
import mtr.data.RailwayData;
import mtr.packet.PacketTrainDataGuiServer;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.*;

public class BlockRail extends HorizontalFacingBlock implements BlockEntityProvider {

	public static final BooleanProperty FACING = BooleanProperty.of("facing");
	public static final BooleanProperty IS_CONNECTED = BooleanProperty.of("is_connected");

	public BlockRail(Settings settings) {
		super(settings);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		final boolean facing = ctx.getPlayerFacing().getAxis() == Direction.Axis.X;
		return getDefaultState().with(FACING, facing).with(IS_CONNECTED, false);
	}

	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		if (!world.isClient) {
			final RailwayData railwayData = RailwayData.getInstance(world);
			if (railwayData != null) {
				railwayData.removeNode(pos);
				PacketTrainDataGuiServer.removeNodeS2C(world, pos);
			}
		}
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return Block.createCuboidShape(0, 0, 0, 16, 1, 16);
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return VoxelShapes.empty();
	}

	@Override
	public PistonBehavior getPistonBehavior(BlockState state) {
		return PistonBehavior.BLOCK;
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING, IS_CONNECTED);
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new TileEntityRail();
	}

	public static void resetRailNode(World world, BlockPos pos) {
		world.setBlockState(pos, world.getBlockState(pos).with(BlockRail.IS_CONNECTED, false));
	}

	public static class TileEntityRail extends BlockEntity implements BlockEntityClientSerializable {

		public final Map<BlockPos, Rail> railMap = new HashMap<>();
		private static final String KEY_LIST_LENGTH = "list_length";
		private static final String KEY_BLOCK_POS = "block_pos";

		public TileEntityRail() {
			super(MTR.RAIL_TILE_ENTITY);
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
			railMap.clear();
			final int listLength = tag.getInt(KEY_LIST_LENGTH);
			for (int i = 0; i < listLength; i++) {
				final BlockPos newPos = BlockPos.fromLong(tag.getLong(KEY_BLOCK_POS + i));
				railMap.put(newPos, new Rail(tag.getCompound(KEY_LIST_LENGTH + i)));
			}
		}

		@Override
		public CompoundTag toClientTag(CompoundTag tag) {
			tag.putInt(KEY_LIST_LENGTH, railMap.size());
			final List<BlockPos> keys = new ArrayList<>(railMap.keySet());
			for (int i = 0; i < railMap.size(); i++) {
				tag.putLong(KEY_BLOCK_POS + i, keys.get(i).asLong());
				tag.put(KEY_LIST_LENGTH + i, railMap.get(keys.get(i)).toCompoundTag());
			}
			return tag;
		}

		public void addRail(Direction facing1, BlockPos newPos, Direction facing2, RailType railType) {
			if (world != null && world.getBlockState(newPos).getBlock() instanceof BlockRail) {
				railMap.put(newPos, new Rail(pos, facing1, newPos, facing2, railType));

				markDirty();
				sync();

				final BlockState state = world.getBlockState(pos);
				if (state.getBlock() instanceof BlockRail) {
					world.setBlockState(pos, state.with(IS_CONNECTED, true));
				}
			}
		}

		public void removeRail(BlockPos newPos) {
			railMap.remove(newPos);
			markDirty();
			sync();

			if (world != null) {
				final BlockState state = world.getBlockState(pos);
				if (state.getBlock() instanceof BlockRail) {
					world.setBlockState(pos, state.with(IS_CONNECTED, !railMap.isEmpty()));
				}
			}
		}

		public boolean hasPlatform() {
			return railMap.values().stream().anyMatch(rail -> rail.railType == RailType.PLATFORM);
		}

		public Set<BlockPos> getConnectedPositions(BlockPos posFrom) {
			final Set<BlockPos> positions = new HashSet<>();
			final Rail railFrom = railMap.get(posFrom);
			if (railFrom != null) {
				final Direction findDirection = railFrom.facingStart.getOpposite();
				railMap.forEach((pos, rail) -> {
					if (rail.facingStart == findDirection) {
						positions.add(pos);
					}
				});
			}
			return positions;
		}
	}
}
