package mtr.block;

import mtr.BlockEntityTypes;
import mtr.data.Rail;
import mtr.data.RailAngle;
import mtr.data.RailType;
import mtr.data.RailwayData;
import mtr.mappings.BlockEntityClientSerializableMapper;
import mtr.mappings.BlockEntityMapper;
import mtr.mappings.EntityBlockMapper;
import mtr.packet.PacketTrainDataGuiServer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.*;

public class BlockRail extends HorizontalDirectionalBlock implements EntityBlockMapper {

	public static final BooleanProperty FACING = BooleanProperty.create("facing");
	public static final BooleanProperty IS_22_5 = BooleanProperty.create("is_22_5");
	public static final BooleanProperty IS_45 = BooleanProperty.create("is_45");
	public static final BooleanProperty IS_CONNECTED = BooleanProperty.create("is_connected");

	public BlockRail(Properties settings) {
		super(settings);
		registerDefaultState(defaultBlockState().setValue(FACING, false).setValue(IS_22_5, false).setValue(IS_45, false));
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		final int quadrant = RailAngle.getQuadrant(ctx.getRotation());
		return defaultBlockState().setValue(FACING, quadrant % 8 >= 4).setValue(IS_45, quadrant % 4 >= 2).setValue(IS_22_5, quadrant % 2 >= 1).setValue(IS_CONNECTED, false);
	}

	@Override
	public void playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
		if (!world.isClientSide) {
			final RailwayData railwayData = RailwayData.getInstance(world);
			if (railwayData != null) {
				railwayData.removeNode(pos);
				PacketTrainDataGuiServer.removeNodeS2C(world, pos);
			}
		}
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
		return Block.box(0, 0, 0, 16, 1, 16);
	}

	@Override
	public VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
		return Shapes.empty();
	}

	@Override
	public PushReaction getPistonPushReaction(BlockState blockState) {
		return PushReaction.BLOCK;
	}

	@Override
	public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntityRail(pos, state);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, IS_22_5, IS_45, IS_CONNECTED);
	}

	public static void resetRailNode(Level world, BlockPos pos) {
		world.setBlockAndUpdate(pos, world.getBlockState(pos).setValue(BlockRail.IS_CONNECTED, false));
	}

	public static float getAngle(BlockState state) {
		return (IBlock.getStatePropertySafe(state, BlockRail.FACING) ? 0 : 90) + (IBlock.getStatePropertySafe(state, BlockRail.IS_22_5) ? 22.5F : 0) + (IBlock.getStatePropertySafe(state, BlockRail.IS_45) ? 45 : 0);
	}

	public static class TileEntityRail extends BlockEntityClientSerializableMapper {

		public final Map<BlockPos, Rail> railMap = new HashMap<>();
		private static final String KEY_LIST_LENGTH = "list_length";
		private static final String KEY_BLOCK_POS = "block_pos";

		public TileEntityRail(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.RAIL_TILE_ENTITY, pos, state);
		}

		@Override
		public void readCompoundTag(CompoundTag compoundTag) {
			railMap.clear();
			final int listLength = compoundTag.getInt(KEY_LIST_LENGTH);
			for (int i = 0; i < listLength; i++) {
				final BlockPos newPos = BlockPos.of(compoundTag.getLong(KEY_BLOCK_POS + i));
				railMap.put(newPos, new Rail(compoundTag.getCompound(KEY_LIST_LENGTH + i)));
			}
		}

		@Override
		public void writeCompoundTag(CompoundTag compoundTag) {
			compoundTag.putInt(KEY_LIST_LENGTH, railMap.size());
			final List<BlockPos> keys = new ArrayList<>(railMap.keySet());
			for (int i = 0; i < railMap.size(); i++) {
				compoundTag.putLong(KEY_BLOCK_POS + i, keys.get(i).asLong());
				compoundTag.put(KEY_LIST_LENGTH + i, railMap.get(keys.get(i)).toCompoundTag());
			}
		}

		public void addRail(RailAngle facing1, BlockPos newPos, RailAngle facing2, RailType railType) {
			if (level != null && level.getBlockState(newPos).getBlock() instanceof BlockRail) {
				railMap.put(newPos, new Rail(worldPosition, facing1, newPos, facing2, railType));

				setChanged();
				syncData();

				final BlockState state = level.getBlockState(worldPosition);
				if (state.getBlock() instanceof BlockRail) {
					level.setBlockAndUpdate(worldPosition, state.setValue(IS_CONNECTED, true));
				}
			}
		}

		public void removeRail(BlockPos newPos) {
			railMap.remove(newPos);
			setChanged();
			syncData();

			if (level != null) {
				final BlockState state = level.getBlockState(worldPosition);
				if (state.getBlock() instanceof BlockRail) {
					level.setBlockAndUpdate(worldPosition, state.setValue(IS_CONNECTED, !railMap.isEmpty()));
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
				final RailAngle findDirection = railFrom.facingStart.getOpposite();
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
