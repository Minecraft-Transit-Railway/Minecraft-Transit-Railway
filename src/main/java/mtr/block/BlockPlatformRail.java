package mtr.block;

import mtr.Items;
import mtr.data.Platform;
import mtr.data.RailwayData;
import mtr.entity.EntityTrainBase;
import mtr.packet.PacketTrainDataGuiServer;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.enums.RailShape;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Property;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.Tag;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class BlockPlatformRail extends AbstractRailBlock {


	public static final EnumProperty<RailShape> SHAPE = EnumProperty.of("shape", RailShape.class, (shape) -> shape == RailShape.NORTH_SOUTH || shape == RailShape.EAST_WEST);

	public BlockPlatformRail(Settings settings) {
		super(true, settings);
	}

	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		super.onBlockAdded(state, world, pos, oldState, notify);
		if (!world.isClient()) {
			RailwayData railwayData = RailwayData.getInstance(world);
			if (railwayData != null) {
				railwayData.addPlatform(scanPlatform(world, pos, state));
			}
		}
	}

	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		if (state.get(SHAPE) == RailShape.NORTH_SOUTH) {
			checkAndBreak(world, pos, state, Direction.NORTH);
			checkAndBreak(world, pos, state, Direction.SOUTH);
		} else {
			checkAndBreak(world, pos, state, Direction.EAST);
			checkAndBreak(world, pos, state, Direction.WEST);
		}
	}

	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		if (!world.isClient && entity instanceof EntityTrainBase) {
			final EntityTrainBase entityTrainBase = (EntityTrainBase) entity;
			final RailShape railShape = state.get(SHAPE);
			for (int x = -2; x <= 2; x++) {
				for (int y = 0; y <= 2; y++) {
					updateStationCoolDown(world, (railShape == RailShape.NORTH_SOUTH ? pos.east(x) : pos.north(x)).up(y), entityTrainBase.stationCoolDown);
				}
			}
		}
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (!world.isClient()) {
			final RailwayData railwayData = RailwayData.getInstance(world);

			if (railwayData != null) {
				BlockPos platformPos = pos;
				final Direction moveDirection = state.get(SHAPE) == RailShape.NORTH_SOUTH ? Direction.NORTH : Direction.WEST;
				while (world.getBlockState(platformPos).getBlock() instanceof BlockPlatformRail && world.getBlockState(platformPos).get(SHAPE) == state.get(SHAPE)) {
					platformPos = platformPos.offset(moveDirection);
				}
				platformPos = platformPos.offset(moveDirection.getOpposite());

				if (player.getStackInHand(hand).getItem() == Items.BRUSH) {
					PacketTrainDataGuiServer.openPlatformScreenS2C(player, railwayData.getStations(), railwayData.getPlatforms(world), railwayData.getRoutes(), platformPos);
				} else {
					PacketTrainDataGuiServer.openScheduleScreenS2C(player, railwayData.getStations(), railwayData.getPlatforms(world), railwayData.getRoutes(), platformPos);
				}

				return ActionResult.CONSUME;
			}
		}
		return ActionResult.SUCCESS;
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return VoxelShapes.empty();
	}

	@Override
	public boolean isIn(Tag<Block> tag) {
		return tag == BlockTags.RAILS;
	}

	@Override
	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return state;
	}

	@Override
	public Property<RailShape> getShapeProperty() {
		return SHAPE;
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(SHAPE);
	}

	@Override
	public PistonBehavior getPistonBehavior(BlockState state) {
		return PistonBehavior.BLOCK;
	}

	@Override
	protected BlockState updateBlockState(World world, BlockPos pos, BlockState state, boolean forceUpdate) {
		try {
			return super.updateBlockState(world, pos, state, forceUpdate);
		} catch (Exception e) {
			return state;
		}
	}

	private void updateStationCoolDown(World world, BlockPos pos, int coolDown) {
		BlockState state = world.getBlockState(pos);

		if (state.getBlock() instanceof BlockPSDAPGDoorBase) {
			if (coolDown < RailwayData.TRAIN_STOP_TIME || coolDown >= RailwayData.STATION_COOL_DOWN - RailwayData.TRAIN_STOP_TIME) {
				world.setBlockState(pos, state.with(BlockPSDAPGDoorBase.OPEN, 0));
			} else if (coolDown < RailwayData.TRAIN_STOP_TIME + BlockPSDAPGDoorBase.MAX_OPEN_VALUE) {
				world.setBlockState(pos, state.with(BlockPSDAPGDoorBase.OPEN, coolDown - RailwayData.TRAIN_STOP_TIME));
			} else if (coolDown >= RailwayData.STATION_COOL_DOWN - RailwayData.TRAIN_STOP_TIME - BlockPSDAPGDoorBase.MAX_OPEN_VALUE) {
				world.setBlockState(pos, state.with(BlockPSDAPGDoorBase.OPEN, RailwayData.STATION_COOL_DOWN - RailwayData.TRAIN_STOP_TIME - coolDown));
			} else {
				world.setBlockState(pos, state.with(BlockPSDAPGDoorBase.OPEN, BlockPSDAPGDoorBase.MAX_OPEN_VALUE));
			}
		}
	}

	private void checkAndBreak(World world, BlockPos pos, BlockState state, Direction direction) {
		BlockPos checkPos = pos.offset(direction);
		BlockState checkState = world.getBlockState(checkPos);
		if (checkState == state) {
			((BlockPlatformRail) checkState.getBlock()).checkAndBreak(world, checkPos, state, direction);
		}
		world.breakBlock(pos, false);
	}

	private Platform scanPlatform(WorldAccess world, BlockPos pos, BlockState state) {
		final Direction scanDirection;
		final Direction.Axis axis;

		if (state.get(SHAPE) == RailShape.NORTH_SOUTH) {
			scanDirection = Direction.NORTH;
			axis = Direction.Axis.Z;
		} else {
			scanDirection = Direction.WEST;
			axis = Direction.Axis.X;
		}

		int length = -2;
		BlockPos startPos = pos;
		do {
			startPos = startPos.offset(scanDirection);
			length++;
		} while (world.getBlockState(startPos).equals(state));

		BlockPos lengthPos = pos;
		do {
			lengthPos = lengthPos.offset(scanDirection.getOpposite());
			length++;
		} while (world.getBlockState(lengthPos).equals(state));

		return new Platform(startPos.offset(scanDirection.getOpposite()), axis, length);
	}
}
