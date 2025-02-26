package org.mtr.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mapping.mapper.BlockExtension;
import org.mtr.mapping.mapper.BlockWithEntity;
import org.mtr.mapping.mapper.DirectionHelper;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.BlockEntityTypes;
import org.mtr.mod.Items;
import org.mtr.mod.item.ItemBrush;

import javax.annotation.Nonnull;
import java.util.List;

public class BlockPSDTop extends BlockExtension implements IBlock, DirectionHelper, BlockWithEntity {

	private static final float PERSISTENT_OFFSET = 7.5F;
	public static final float PERSISTENT_OFFSET_SMALL = PERSISTENT_OFFSET / 16;

	public static final BooleanProperty AIR_LEFT = BooleanProperty.of("air_left");
	public static final BooleanProperty AIR_RIGHT = BooleanProperty.of("air_right");
	public static final IntegerProperty ARROW_DIRECTION = IntegerProperty.of("propagate_property", 0, 3);
	public static final EnumProperty<EnumPersistent> PERSISTENT = EnumProperty.of("persistent", EnumPersistent.class);

	public BlockPSDTop() {
		super(org.mtr.mod.Blocks.createDefaultBlockSettings(true).nonOpaque());
	}

	@Nonnull
	@Override
	public ActionResult onUse2(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		return IBlock.checkHoldingItem(world, player, item -> {
			if (item.data instanceof ItemBrush) {
				world.setBlockState(pos, state.cycle(new Property<>(ARROW_DIRECTION.data)));
				propagate(world, pos, IBlock.getStatePropertySafe(state, FACING).rotateYClockwise(), new Property<>(ARROW_DIRECTION.data), 1);
				propagate(world, pos, IBlock.getStatePropertySafe(state, FACING).rotateYCounterclockwise(), new Property<>(ARROW_DIRECTION.data), 1);
			} else {
				final boolean shouldBePersistent = IBlock.getStatePropertySafe(state, new Property<>(PERSISTENT.data)) == EnumPersistent.NONE;
				setState(world, pos, shouldBePersistent);
				propagate(world, pos, IBlock.getStatePropertySafe(state, FACING).rotateYClockwise(), offsetPos -> setState(world, offsetPos, shouldBePersistent), 1);
				propagate(world, pos, IBlock.getStatePropertySafe(state, FACING).rotateYCounterclockwise(), offsetPos -> setState(world, offsetPos, shouldBePersistent), 1);
			}
		}, null, Items.BRUSH.get(), org.mtr.mapping.holder.Items.getShearsMapped());
	}

	private void setState(World world, BlockPos pos, boolean shouldBePersistent) {
		final Block blockBelow = world.getBlockState(pos.down()).getBlock();
		if (blockBelow.data instanceof BlockPSDDoor || blockBelow.data instanceof BlockPSDGlass || blockBelow.data instanceof BlockPSDGlassEnd) {
			if (shouldBePersistent) {
				world.setBlockState(pos, world.getBlockState(pos).with(new Property<>(PERSISTENT.data), blockBelow.data instanceof BlockPSDDoor ? EnumPersistent.ARROW : blockBelow.data instanceof BlockPSDGlass ? EnumPersistent.ROUTE : EnumPersistent.BLANK));
			} else {
				world.setBlockState(pos, world.getBlockState(pos).with(new Property<>(PERSISTENT.data), EnumPersistent.NONE));
			}
		}
	}

	@Nonnull
	@Override
	public Item asItem2() {
		return Items.PSD_GLASS_1.get();
	}

	@Nonnull
	@Override
	public ItemStack getPickStack2(BlockView world, BlockPos pos, BlockState state) {
		return new ItemStack(new ItemConvertible(asItem2().data));
	}

	@Override
	public void onBreak2(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		final Block blockDown = world.getBlockState(pos.down()).getBlock();
		if (blockDown.data instanceof BlockPSDAPGBase) {
			((BlockPSDAPGBase) blockDown.data).onBreak2(world, pos.down(), world.getBlockState(pos.down()), player);
			world.setBlockState(pos.down(), Blocks.getAirMapped().getDefaultState());
		}
		super.onBreak2(world, pos, state, player);
	}

	@Nonnull
	@Override
	public BlockState getStateForNeighborUpdate2(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		if (direction == Direction.DOWN && IBlock.getStatePropertySafe(state, new Property<>(PERSISTENT.data)) == EnumPersistent.NONE && !(neighborState.getBlock().data instanceof BlockPSDAPGBase)) {
			return Blocks.getAirMapped().getDefaultState();
		} else {
			return getActualState(world, pos);
		}
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		final VoxelShape baseShape = IBlock.getVoxelShapeByDirection(0, IBlock.getStatePropertySafe(state, new Property<>(PERSISTENT.data)) == EnumPersistent.NONE ? 0 : PERSISTENT_OFFSET, 0, 16, 16, 6, IBlock.getStatePropertySafe(state, FACING));
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
	public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new BlockEntity(blockPos, blockState);
	}

	@Override
	public void addBlockProperties(List<HolderBase<?>> properties) {
		properties.add(FACING);
		properties.add(SIDE_EXTENDED);
		properties.add(AIR_LEFT);
		properties.add(AIR_RIGHT);
		properties.add(ARROW_DIRECTION);
		properties.add(PERSISTENT);
	}

	public static BlockState getActualState(WorldAccess world, BlockPos pos) {
		Direction facing = null;
		EnumSide side = null;
		boolean airLeft = false, airRight = false;

		final BlockState stateBelow = world.getBlockState(pos.down());
		final Block blockBelow = stateBelow.getBlock();
		if (blockBelow.data instanceof BlockPSDGlass || blockBelow.data instanceof BlockPSDDoor || blockBelow.data instanceof BlockPSDGlassEnd) {
			if (blockBelow.data instanceof BlockPSDDoor) {
				side = IBlock.getStatePropertySafe(stateBelow, SIDE);
			} else {
				side = IBlock.getStatePropertySafe(stateBelow, SIDE_EXTENDED);
			}

			if (blockBelow.data instanceof BlockPSDGlassEnd) {
				if (IBlock.getStatePropertySafe(stateBelow, new Property<>(BlockPSDGlassEnd.TOUCHING_LEFT.data)) == BlockPSDGlassEnd.EnumPSDAPGGlassEndSide.AIR) {
					airLeft = true;
				}
				if (IBlock.getStatePropertySafe(stateBelow, new Property<>(BlockPSDGlassEnd.TOUCHING_RIGHT.data)) == BlockPSDGlassEnd.EnumPSDAPGGlassEndSide.AIR) {
					airRight = true;
				}
			}

			facing = IBlock.getStatePropertySafe(stateBelow, FACING);
		}

		final BlockState oldState = world.getBlockState(pos);
		BlockState neighborState = (oldState.getBlock().data instanceof BlockPSDTop ? oldState : org.mtr.mod.Blocks.PSD_TOP.get().getDefaultState()).with(new Property<>(AIR_LEFT.data), airLeft).with(new Property<>(AIR_RIGHT.data), airRight);
		if (facing != null) {
			neighborState = neighborState.with(new Property<>(FACING.data), facing.data);
		}
		if (side != null) {
			neighborState = neighborState.with(new Property<>(SIDE_EXTENDED.data), side);
		}
		return neighborState;
	}

	public static class BlockEntity extends BlockEntityBase {

		public BlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.PSD_TOP.get(), pos, state);
		}
	}

	public static class BlockEntityBase extends BlockEntityExtension {

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
		public String asString2() {
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
		public String asString2() {
			return name;
		}
	}
}
