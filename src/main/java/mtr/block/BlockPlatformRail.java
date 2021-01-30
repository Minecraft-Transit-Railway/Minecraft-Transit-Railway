package mtr.block;

import mtr.Blocks;
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
import net.minecraft.server.network.ServerPlayerEntity;
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
import net.minecraft.util.math.Vec3d;
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
		updateRailwayData(world, pos);
	}

	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
		super.neighborUpdate(state, world, pos, block, fromPos, notify);
		updateRailwayData(world, pos);
	}

	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		if (!world.isClient && entity instanceof EntityTrainBase) {
			final EntityTrainBase entityTrainBase = (EntityTrainBase) entity;
			if (entityTrainBase.inTrain(Vec3d.of(pos))) {
				final RailShape railShape = IBlock.getStatePropertySafe(state, SHAPE);
				for (int x = -2; x <= 2; x++) {
					for (int y = 0; y <= 2; y++) {
						updateStationCoolDown(world, (railShape == RailShape.NORTH_SOUTH ? pos.east(x) : pos.north(x)).up(y), entityTrainBase.getDoorValue());
					}
				}
			}
		}
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		return IBlock.checkHoldingBrush(world, player, () -> {
			final RailwayData railwayData = RailwayData.getInstance(world);
			if (railwayData != null) {
				BlockPos platformPos = getPlatformPos1(world, pos);
				PacketTrainDataGuiServer.openPlatformScreenS2C((ServerPlayerEntity) player, railwayData.getStations(), railwayData.getPlatforms(world), railwayData.getRoutes(), platformPos);
			}
		}, () -> {
			if (!player.isHolding(Blocks.PLATFORM_RAIL.asItem())) {
				final RailwayData railwayData = RailwayData.getInstance(world);
				if (railwayData != null) {
					BlockPos platformPos = getPlatformPos1(world, pos);
					PacketTrainDataGuiServer.openScheduleScreenS2C((ServerPlayerEntity) player, railwayData.getStations(), railwayData.getPlatforms(world), railwayData.getRoutes(), platformPos);
				}
			}
		});
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

	public static Platform createNewPlatform(WorldAccess world, BlockPos pos) {
		final BlockState state = world.getBlockState(pos);
		if (!(state.getBlock() instanceof BlockPlatformRail)) {
			return null;
		}

		final Direction scanDirection;
		final Direction.Axis axis;

		if (IBlock.getStatePropertySafe(state, SHAPE) == RailShape.NORTH_SOUTH) {
			scanDirection = Direction.SOUTH;
			axis = Direction.Axis.Z;
		} else {
			scanDirection = Direction.EAST;
			axis = Direction.Axis.X;
		}

		int length = 1;
		final BlockPos startPos = getPlatformPos1(world, pos);
		while (world.getBlockState(startPos.offset(scanDirection, length)).equals(state)) {
			length++;
		}

		return new Platform(startPos, axis, length - 1);
	}

	public static BlockPos getPlatformPos1(WorldAccess world, BlockPos pos) {
		final RailShape railShape = IBlock.getStatePropertySafe(world, pos, SHAPE);
		final Direction moveDirection = railShape == RailShape.NORTH_SOUTH ? Direction.NORTH : Direction.WEST;
		while (world.getBlockState(pos).getBlock() instanceof BlockPlatformRail && IBlock.getStatePropertySafe(world, pos, SHAPE) == railShape) {
			pos = pos.offset(moveDirection);
		}
		return pos.offset(moveDirection.getOpposite());
	}

	private static void updateRailwayData(World world, BlockPos pos) {
		if (!world.isClient()) {
			RailwayData railwayData = RailwayData.getInstance(world);
			if (railwayData != null) {
				railwayData.checkPlatformPos(world, pos);
			}
		}
	}

	private static void updateStationCoolDown(World world, BlockPos pos, int doorValue) {
		BlockState state = world.getBlockState(pos);
		if (state.getBlock() instanceof BlockPSDAPGDoorBase) {
			world.setBlockState(pos, state.with(BlockPSDAPGDoorBase.OPEN, doorValue));
		}
	}
}
