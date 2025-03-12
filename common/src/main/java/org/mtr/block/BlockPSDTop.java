package org.mtr.block;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;
import org.mtr.item.ItemBrush;
import org.mtr.registry.BlockEntityTypes;
import org.mtr.registry.Items;

import javax.annotation.Nonnull;

public class BlockPSDTop extends Block implements IBlock, BlockEntityProvider {

	private static final float PERSISTENT_OFFSET = 7.5F;
	public static final float PERSISTENT_OFFSET_SMALL = PERSISTENT_OFFSET / 16;

	public static final BooleanProperty AIR_LEFT = BooleanProperty.of("air_left");
	public static final BooleanProperty AIR_RIGHT = BooleanProperty.of("air_right");
	public static final IntProperty ARROW_DIRECTION = IntProperty.of("propagate_property", 0, 3);
	public static final EnumProperty<EnumPersistent> PERSISTENT = EnumProperty.of("persistent", EnumPersistent.class);

	public BlockPSDTop(AbstractBlock.Settings settings) {
		super(settings.nonOpaque());
	}

	@Nonnull
	@Override
	protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
		return IBlock.checkHoldingItem(world, player, item -> {
			if (item instanceof ItemBrush) {
				world.setBlockState(pos, state.cycle(ARROW_DIRECTION));
				propagate(world, pos, IBlock.getStatePropertySafe(state, Properties.HORIZONTAL_FACING).rotateYClockwise(), ARROW_DIRECTION, 1);
				propagate(world, pos, IBlock.getStatePropertySafe(state, Properties.HORIZONTAL_FACING).rotateYCounterclockwise(), ARROW_DIRECTION, 1);
			} else {
				final boolean shouldBePersistent = IBlock.getStatePropertySafe(state, PERSISTENT) == EnumPersistent.NONE;
				setState(world, pos, shouldBePersistent);
				propagate(world, pos, IBlock.getStatePropertySafe(state, Properties.HORIZONTAL_FACING).rotateYClockwise(), offsetPos -> setState(world, offsetPos, shouldBePersistent), 1);
				propagate(world, pos, IBlock.getStatePropertySafe(state, Properties.HORIZONTAL_FACING).rotateYCounterclockwise(), offsetPos -> setState(world, offsetPos, shouldBePersistent), 1);
			}
		}, null, Items.BRUSH.get(), net.minecraft.item.Items.SHEARS);
	}

	private void setState(World world, BlockPos pos, boolean shouldBePersistent) {
		final Block blockBelow = world.getBlockState(pos.down()).getBlock();
		if (blockBelow instanceof BlockPSDDoor || blockBelow instanceof BlockPSDGlass || blockBelow instanceof BlockPSDGlassEnd) {
			if (shouldBePersistent) {
				world.setBlockState(pos, world.getBlockState(pos).with(PERSISTENT, blockBelow instanceof BlockPSDDoor ? EnumPersistent.ARROW : blockBelow instanceof BlockPSDGlass ? EnumPersistent.ROUTE : EnumPersistent.BLANK));
			} else {
				world.setBlockState(pos, world.getBlockState(pos).with(PERSISTENT, EnumPersistent.NONE));
			}
		}
	}

	@Nonnull
	@Override
	public Item asItem() {
		return Items.PSD_GLASS_1.createAndGet();
	}

	@Nonnull
	@Override
	protected ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state, boolean includeData) {
		return new ItemStack(asItem());
	}

	@Override
	public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		final Block blockDown = world.getBlockState(pos.down()).getBlock();
		if (blockDown instanceof BlockPSDAPGBase) {
			blockDown.onBreak(world, pos.down(), world.getBlockState(pos.down()), player);
			world.setBlockState(pos.down(), Blocks.AIR.getDefaultState());
		}
		return super.onBreak(world, pos, state, player);
	}

	@Nonnull
	@Override
	protected BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {
		if (direction == Direction.DOWN && IBlock.getStatePropertySafe(state, PERSISTENT) == EnumPersistent.NONE && !(neighborState.getBlock() instanceof BlockPSDAPGBase)) {
			return Blocks.AIR.getDefaultState();
		} else {
			return getActualState(world, pos);
		}
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		final VoxelShape baseShape = IBlock.getVoxelShapeByDirection(0, IBlock.getStatePropertySafe(state, PERSISTENT) == EnumPersistent.NONE ? 0 : PERSISTENT_OFFSET, 0, 16, 16, 6, IBlock.getStatePropertySafe(state, Properties.HORIZONTAL_FACING));
		final boolean airLeft = IBlock.getStatePropertySafe(state, AIR_LEFT);
		final boolean airRight = IBlock.getStatePropertySafe(state, AIR_RIGHT);
		if (airLeft || airRight) {
			return BlockPSDAPGGlassEndBase.getEndOutlineShape(baseShape, state, 16, 6, airLeft, airRight);
		} else {
			return baseShape;
		}
	}

	@Nonnull
	@Override
	public BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new PSDTopBlockEntity(blockPos, blockState);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(Properties.HORIZONTAL_FACING);
		builder.add(SIDE_EXTENDED);
		builder.add(AIR_LEFT);
		builder.add(AIR_RIGHT);
		builder.add(ARROW_DIRECTION);
		builder.add(PERSISTENT);
	}

	public static BlockState getActualState(WorldView world, BlockPos pos) {
		Direction facing = null;
		EnumSide side = null;
		boolean airLeft = false, airRight = false;

		final BlockState stateBelow = world.getBlockState(pos.down());
		final Block blockBelow = stateBelow.getBlock();
		if (blockBelow instanceof BlockPSDGlass || blockBelow instanceof BlockPSDDoor || blockBelow instanceof BlockPSDGlassEnd) {
			if (blockBelow instanceof BlockPSDDoor) {
				side = IBlock.getStatePropertySafe(stateBelow, SIDE);
			} else {
				side = IBlock.getStatePropertySafe(stateBelow, SIDE_EXTENDED);
			}

			if (blockBelow instanceof BlockPSDGlassEnd) {
				if (IBlock.getStatePropertySafe(stateBelow, BlockPSDGlassEnd.TOUCHING_LEFT) == BlockPSDGlassEnd.EnumPSDAPGGlassEndSide.AIR) {
					airLeft = true;
				}
				if (IBlock.getStatePropertySafe(stateBelow, BlockPSDGlassEnd.TOUCHING_RIGHT) == BlockPSDGlassEnd.EnumPSDAPGGlassEndSide.AIR) {
					airRight = true;
				}
			}

			facing = IBlock.getStatePropertySafe(stateBelow, Properties.HORIZONTAL_FACING);
		}

		final BlockState oldState = world.getBlockState(pos);
		BlockState neighborState = (oldState.getBlock() instanceof BlockPSDTop ? oldState : org.mtr.registry.Blocks.PSD_TOP.createAndGet().getDefaultState()).with(AIR_LEFT, airLeft).with(AIR_RIGHT, airRight);
		if (facing != null) {
			neighborState = neighborState.with(Properties.HORIZONTAL_FACING, facing);
		}
		if (side != null) {
			neighborState = neighborState.with(SIDE_EXTENDED, side);
		}
		return neighborState;
	}

	public static class PSDTopBlockEntity extends BlockEntityBase {

		public PSDTopBlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.PSD_TOP.createAndGet(), pos, state);
		}
	}

	public static class BlockEntityBase extends BlockEntity {

		public BlockEntityBase(BlockEntityType<?> type, BlockPos pos, BlockState state) {
			super(type, pos, state);
		}
	}

	public enum EnumDoorLight implements StringIdentifiable {

		ON("on"), OFF("off"), NONE("none");
		private final String name;

		EnumDoorLight(String nameIn) {
			name = nameIn;
		}

		@Nonnull
		@Override
		public String asString() {
			return name;
		}
	}

	public enum EnumPersistent implements StringIdentifiable {

		NONE("none"), ARROW("arrow"), ROUTE("route"), BLANK("blank");
		private final String name;

		EnumPersistent(String nameIn) {
			name = nameIn;
		}

		@Nonnull
		@Override
		public String asString() {
			return name;
		}
	}
}
