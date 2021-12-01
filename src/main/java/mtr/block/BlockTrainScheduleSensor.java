package mtr.block;

import mapper.TickableMapper;
import mapper.Utilities;
import mtr.MTR;
import mtr.data.Platform;
import mtr.data.RailwayData;
import mtr.data.Route;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.*;

public class BlockTrainScheduleSensor extends BlockTrainSensorBase {

	public static final BooleanProperty POWERED = BooleanProperty.of("powered");

	public BlockTrainScheduleSensor() {
		super();
		setDefaultState(getStateManager().getDefaultState().with(POWERED, false));
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		world.setBlockState(pos, state.with(POWERED, false));
	}

	@Override
	public boolean emitsRedstonePower(BlockState state) {
		return true;
	}

	@Override
	public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
		return state.get(POWERED) ? 15 : 0;
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new TileEntityTrainScheduleSensor(null, null);
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntityTrainScheduleSensor(pos, state);
	}

	@Override
	public <T extends BlockEntity> void tick(World world, BlockPos pos, T blockEntity) {
		TileEntityTrainScheduleSensor.tick(world, pos, blockEntity);
	}

	@Override
	public BlockEntityType<? extends BlockEntity> getType() {
		return MTR.TRAIN_SCHEDULE_SENSOR_TILE_ENTITY;
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(POWERED);
	}

	public static class TileEntityTrainScheduleSensor extends TileEntityTrainSensorBase implements TickableMapper {

		private int seconds = 10;
		private static final String KEY_SECONDS = "seconds";

		public TileEntityTrainScheduleSensor(BlockPos pos, BlockState state) {
			super(MTR.TRAIN_SCHEDULE_SENSOR_TILE_ENTITY, pos, state);
		}

		@Override
		public void tick() {
			if (world != null) {
				tick(world, pos, this);
			}
		}

		@Override
		public void readNbtCompound(NbtCompound nbtCompound) {
			seconds = nbtCompound.getInt(KEY_SECONDS);
		}

		@Override
		public void writeNbtCompound(NbtCompound nbtCompound) {
			nbtCompound.putInt(KEY_SECONDS, seconds);
		}

		@Override
		public void setData(Set<Long> filterRouteIds, int number, String string) {
			seconds = number;
			setData(filterRouteIds);
		}

		public int getSeconds() {
			return seconds;
		}

		public static <T extends BlockEntity> void tick(World world, BlockPos pos, T blockEntity) {
			if (world != null && !world.isClient) {
				final BlockState state = world.getBlockState(pos);
				final boolean isActive = IBlock.getStatePropertySafe(state, POWERED) && Utilities.isScheduled(world, pos, state.getBlock());

				if (isActive || !(state.getBlock() instanceof BlockTrainScheduleSensor) || !(blockEntity instanceof BlockTrainScheduleSensor.TileEntityTrainScheduleSensor)) {
					return;
				}

				final RailwayData railwayData = RailwayData.getInstance(world);
				if (railwayData == null) {
					return;
				}

				final Platform platform = RailwayData.getClosePlatform(railwayData.platforms, pos, 4, 4, 0);
				if (platform == null) {
					return;
				}

				final Set<Route.ScheduleEntry> schedules = railwayData.getSchedulesAtPlatform(platform.id);
				if (schedules == null) {
					return;
				}

				final List<Route.ScheduleEntry> scheduleList = new ArrayList<>();
				schedules.forEach(scheduleEntry -> {
					if (((TileEntityTrainScheduleSensor) blockEntity).matchesFilter(scheduleEntry.routeId)) {
						scheduleList.add(scheduleEntry);
					}
				});
				if (!scheduleList.isEmpty()) {
					Collections.sort(scheduleList);
					if ((scheduleList.get(0).arrivalMillis - System.currentTimeMillis()) / 1000 == ((TileEntityTrainScheduleSensor) blockEntity).seconds) {
						world.setBlockState(pos, state.with(POWERED, true));
						Utilities.scheduleBlockTick(world, pos, state.getBlock(), 20);
					}
				}
			}
		}
	}
}
