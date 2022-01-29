package mtr.block;

import mtr.BlockEntityTypes;
import mtr.data.RailwayData;
import mtr.data.ScheduleEntry;
import mtr.mappings.BlockEntityMapper;
import mtr.mappings.TickableMapper;
import mtr.mappings.Utilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

import java.util.*;

public class BlockTrainScheduleSensor extends BlockTrainSensorBase {

	public static final BooleanProperty POWERED = BooleanProperty.create("powered");

	public BlockTrainScheduleSensor() {
		super();
		registerDefaultState(defaultBlockState().setValue(POWERED, false));
	}

	@Override
	public void tick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
		world.setBlockAndUpdate(pos, state.setValue(POWERED, false));
	}

	@Override
	public boolean isSignalSource(BlockState blockState) {
		return true;
	}

	@Override
	public int getSignal(BlockState state, BlockGetter blockGetter, BlockPos blockPos, Direction direction) {
		return state.getValue(POWERED) ? 15 : 0;
	}

	@Override
	public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntityTrainScheduleSensor(pos, state);
	}

	@Override
	public <T extends BlockEntityMapper> void tick(Level world, BlockPos pos, T blockEntity) {
		TileEntityTrainScheduleSensor.tick(world, pos, blockEntity);
	}

	@Override
	public BlockEntityType<? extends BlockEntityMapper> getType() {
		return BlockEntityTypes.TRAIN_SCHEDULE_SENSOR_TILE_ENTITY;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(POWERED);
	}

	public static class TileEntityTrainScheduleSensor extends TileEntityTrainSensorBase implements TickableMapper {

		private int seconds = 10;
		private static final String KEY_SECONDS = "seconds";

		public TileEntityTrainScheduleSensor(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.TRAIN_SCHEDULE_SENSOR_TILE_ENTITY, pos, state);
		}

		@Override
		public void tick() {
			if (level != null) {
				tick(level, worldPosition, this);
			}
		}

		@Override
		public void readCompoundTag(CompoundTag compoundTag) {
			seconds = compoundTag.getInt(KEY_SECONDS);
			super.readCompoundTag(compoundTag);
		}

		@Override
		public void writeCompoundTag(CompoundTag compoundTag) {
			compoundTag.putInt(KEY_SECONDS, seconds);
			super.writeCompoundTag(compoundTag);
		}

		@Override
		public void setData(Set<Long> filterRouteIds, boolean stoppedOnly, boolean movingOnly, int number, String string) {
			seconds = number;
			setData(filterRouteIds, stoppedOnly, movingOnly);
		}

		public int getSeconds() {
			return seconds;
		}

		public static <T extends BlockEntityMapper> void tick(Level world, BlockPos pos, T blockEntity) {
			if (world != null && !world.isClientSide) {
				final BlockState state = world.getBlockState(pos);
				final boolean isActive = IBlock.getStatePropertySafe(state, POWERED) && world.getBlockTicks().hasScheduledTick(pos, state.getBlock());

				if (isActive || !(state.getBlock() instanceof BlockTrainScheduleSensor) || !(blockEntity instanceof BlockTrainScheduleSensor.TileEntityTrainScheduleSensor)) {
					return;
				}

				final RailwayData railwayData = RailwayData.getInstance(world);
				if (railwayData == null) {
					return;
				}

				final long platformId = RailwayData.getClosePlatformId(railwayData.platforms, railwayData.dataCache, pos, 4, 4, 0);
				if (platformId == 0) {
					return;
				}

				final List<ScheduleEntry> schedules = railwayData.getSchedulesAtPlatform(platformId);
				if (schedules == null) {
					return;
				}

				final List<ScheduleEntry> scheduleList = new ArrayList<>();
				schedules.forEach(scheduleEntry -> {
					if (((TileEntityTrainScheduleSensor) blockEntity).matchesFilter(scheduleEntry.routeId, -1)) {
						scheduleList.add(scheduleEntry);
					}
				});
				if (!scheduleList.isEmpty()) {
					Collections.sort(scheduleList);
					if ((scheduleList.get(0).arrivalMillis - System.currentTimeMillis()) / 1000 == ((TileEntityTrainScheduleSensor) blockEntity).seconds) {
						world.setBlockAndUpdate(pos, state.setValue(POWERED, true));
						Utilities.scheduleBlockTick(world, pos, state.getBlock(), 20);
					}
				}
			}
		}
	}
}
