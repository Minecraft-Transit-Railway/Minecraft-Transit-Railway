package mtr.block;

import minecraftmappings.BlockEntityClientSerializableMapper;
import minecraftmappings.BlockEntityProviderMapper;
import mtr.MTR;
import mtr.data.Rail;
import mtr.data.RailAngle;
import mtr.data.RailType;
import mtr.data.RailwayData;
import mtr.packet.PacketTrainDataGuiServer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.*;

public class BlockRail extends HorizontalFacingBlock implements BlockEntityProviderMapper {

	public static final BooleanProperty FACING = BooleanProperty.of("facing");
	public static final BooleanProperty IS_22_5 = BooleanProperty.of("is_22_5");
	public static final BooleanProperty IS_45 = BooleanProperty.of("is_45");
	public static final BooleanProperty IS_CONNECTED = BooleanProperty.of("is_connected");

	public BlockRail(Settings settings) {
		super(settings);
		setDefaultState(stateManager.getDefaultState().with(FACING, false).with(IS_22_5, false).with(IS_45, false));
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		final int quadrant = RailAngle.getQuadrant(ctx.getPlayerYaw());
		return getDefaultState().with(FACING, quadrant % 8 >= 4).with(IS_45, quadrant % 4 >= 2).with(IS_22_5, quadrant % 2 >= 1).with(IS_CONNECTED, false);
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
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntityRail(pos, state);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING, IS_22_5, IS_45, IS_CONNECTED);
	}

	public static void resetRailNode(World world, BlockPos pos) {
		world.setBlockState(pos, world.getBlockState(pos).with(BlockRail.IS_CONNECTED, false));
	}

	public static float getAngle(BlockState state) {
		return (IBlock.getStatePropertySafe(state, BlockRail.FACING) ? 0 : 90) + (IBlock.getStatePropertySafe(state, BlockRail.IS_22_5) ? 22.5F : 0) + (IBlock.getStatePropertySafe(state, BlockRail.IS_45) ? 45 : 0);
	}

	public static class TileEntityRail extends BlockEntityClientSerializableMapper {

		public final Map<BlockPos, Rail> railMap = new HashMap<>();
		private static final String KEY_LIST_LENGTH = "list_length";
		private static final String KEY_BLOCK_POS = "block_pos";

		public TileEntityRail(BlockPos pos, BlockState state) {
			super(MTR.RAIL_TILE_ENTITY, pos, state);
		}

		@Override
		public void readNbtCompound(NbtCompound nbtCompound) {
			railMap.clear();
			final int listLength = nbtCompound.getInt(KEY_LIST_LENGTH);
			for (int i = 0; i < listLength; i++) {
				final BlockPos newPos = BlockPos.fromLong(nbtCompound.getLong(KEY_BLOCK_POS + i));
				railMap.put(newPos, new Rail(nbtCompound.getCompound(KEY_LIST_LENGTH + i)));
			}
		}

		@Override
		public void writeNbtCompound(NbtCompound nbtCompound) {
			nbtCompound.putInt(KEY_LIST_LENGTH, railMap.size());
			final List<BlockPos> keys = new ArrayList<>(railMap.keySet());
			for (int i = 0; i < railMap.size(); i++) {
				nbtCompound.putLong(KEY_BLOCK_POS + i, keys.get(i).asLong());
				nbtCompound.put(KEY_LIST_LENGTH + i, railMap.get(keys.get(i)).toCompoundTag());
			}
		}

		public void addRail(RailAngle facing1, BlockPos newPos, RailAngle facing2, RailType railType) {
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
