package mtr.block;

import mtr.MTR;
import mtr.data.Platform;
import mtr.data.RailwayData;
import mtr.data.Route;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

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
		return new TileEntityTrainScheduleSensor();
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(POWERED);
	}

	public static class TileEntityTrainScheduleSensor extends TileEntityTrainSensorBase implements Tickable {

		private int seconds = 10;
		private static final String KEY_SECONDS = "seconds";

		public TileEntityTrainScheduleSensor() {
			super(MTR.TRAIN_SCHEDULE_SENSOR_TILE_ENTITY);
		}

		@Override
		public void tick() {
			if (world != null && !world.isClient) {
				final BlockState state = world.getBlockState(pos);
				final boolean isActive = IBlock.getStatePropertySafe(state, POWERED) && world.getBlockTickScheduler().isScheduled(pos, state.getBlock());

				if (isActive || !(state.getBlock() instanceof BlockTrainScheduleSensor)) {
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
					if (matchesFilter(scheduleEntry.routeId)) {
						scheduleList.add(scheduleEntry);
					}
				});
				if (!scheduleList.isEmpty()) {
					Collections.sort(scheduleList);
					if ((scheduleList.get(0).arrivalMillis - System.currentTimeMillis()) / 1000 == seconds) {
						world.setBlockState(pos, state.with(POWERED, true));
						world.getBlockTickScheduler().schedule(pos, state.getBlock(), 20);
					}
				}
			}
		}

		@Override
		public void fromClientTag(NbtCompound nbtCompound) {
			super.fromClientTag(nbtCompound);
			seconds = nbtCompound.getInt(KEY_SECONDS);
		}

		@Override
		public NbtCompound toClientTag(NbtCompound nbtCompound) {
			nbtCompound.putInt(KEY_SECONDS, seconds);
			return super.toClientTag(nbtCompound);
		}

		@Override
		public void setData(Set<Long> filterRouteIds, int number, String string) {
			seconds = number;
			setData(filterRouteIds);
		}

		public int getSeconds() {
			return seconds;
		}

	}
}
