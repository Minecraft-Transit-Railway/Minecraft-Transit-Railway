package mtr.block;

import mtr.BlockEntityTypes;
import mtr.Items;
import mtr.item.ItemPSDAPGBase;
import mtr.mappings.BlockEntityMapper;
import mtr.mappings.EntityBlockMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockPSDTop extends HorizontalDirectionalBlock implements EntityBlockMapper, IBlock {

	private static final float PERSISTENT_OFFSET = 7.5F;
	public static final float PERSISTENT_OFFSET_SMALL = PERSISTENT_OFFSET / 16;

	public static final EnumProperty<EnumDoorLight> DOOR_LIGHT = EnumProperty.create("door_light", EnumDoorLight.class);
	public static final BooleanProperty AIR_LEFT = BooleanProperty.create("air_left");
	public static final BooleanProperty AIR_RIGHT = BooleanProperty.create("air_right");
	public static final IntegerProperty ARROW_DIRECTION = IntegerProperty.create("propagate_property", 0, 3);
	public static final EnumProperty<EnumPersistent> PERSISTENT = EnumProperty.create("persistent", EnumPersistent.class);

	public BlockPSDTop() {
		super(Properties.of(Material.METAL, MaterialColor.QUARTZ).requiresCorrectToolForDrops().strength(2).noOcclusion());
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
		return IBlock.checkHoldingItem(world, player, item -> {
			if (item == Items.BRUSH) {
				if(IBlock.getStatePropertySafe(state, SIDE) == EnumSide.LEFT) {
					System.out.println("Middle");
						final BlockState scanState = world.getBlockState(pos);
						if (state.is(scanState.getBlock())) {
							connectGlass(world, pos, scanState);
						}
				}
				world.setBlockAndUpdate(pos, state.cycle(ARROW_DIRECTION));
				propagate(world, pos, IBlock.getStatePropertySafe(state, FACING).getClockWise(), ARROW_DIRECTION, 1);
				propagate(world, pos, IBlock.getStatePropertySafe(state, FACING).getCounterClockWise(), ARROW_DIRECTION, 1);
			} else {
				final boolean shouldBePersistent = IBlock.getStatePropertySafe(state, PERSISTENT) == EnumPersistent.NONE;
				setState(world, pos, shouldBePersistent);
				propagate(world, pos, IBlock.getStatePropertySafe(state, FACING).getClockWise(), offsetPos -> setState(world, offsetPos, shouldBePersistent), 1);
				propagate(world, pos, IBlock.getStatePropertySafe(state, FACING).getCounterClockWise(), offsetPos -> setState(world, offsetPos, shouldBePersistent), 1);
			}
		}, null, Items.BRUSH, net.minecraft.world.item.Items.SHEARS);
	}

	private void connectGlass(Level world, BlockPos pos, BlockState state) {
		final Direction facing = IBlock.getStatePropertySafe(state, BlockPSDTop.FACING);

		final BlockPos leftPos = pos.relative(facing.getCounterClockWise());
		final BlockState leftState = world.getBlockState(leftPos);
		final boolean leftValid = state.is(leftState.getBlock()) && IBlock.getStatePropertySafe(leftState, PERSISTENT) == IBlock.getStatePropertySafe(state, PERSISTENT);

		if (leftValid) {
			final EnumSide side = IBlock.getStatePropertySafe(leftState, SIDE_EXTENDED);
			EnumSide newLeftSide;

			if (side == EnumSide.RIGHT) {
				newLeftSide = EnumSide.MIDDLE;
			} else if (side == EnumSide.SINGLE) {
				newLeftSide = EnumSide.LEFT;
			} else {
				newLeftSide = side;
			}

			world.setBlockAndUpdate(leftPos, leftState.setValue(SIDE_EXTENDED, newLeftSide));
		}

		final BlockPos rightPos = pos.relative(facing.getClockWise());
		final BlockState rightState = world.getBlockState(rightPos);
		final boolean rightValid = state.is(rightState.getBlock()) && IBlock.getStatePropertySafe(rightState, PERSISTENT) == IBlock.getStatePropertySafe(state, PERSISTENT);

		if (rightValid) {
			final EnumSide side = IBlock.getStatePropertySafe(rightState, SIDE_EXTENDED);
			EnumSide newRightSide;

			if (side == EnumSide.LEFT) {
				newRightSide = EnumSide.MIDDLE;
			} else if (side == EnumSide.SINGLE) {
				newRightSide = EnumSide.RIGHT;
			} else {
				newRightSide = side;
			}

			world.setBlockAndUpdate(rightPos, rightState.setValue(SIDE_EXTENDED, newRightSide));
		}

		EnumSide newSide;
		if (leftValid && rightValid) {
			newSide = EnumSide.MIDDLE;
		} else if (leftValid) {
			newSide = EnumSide.RIGHT;
		} else if (rightValid) {
			newSide = EnumSide.LEFT;
		} else {
			newSide = EnumSide.SINGLE;
		}

		world.setBlockAndUpdate(pos, state.setValue(SIDE_EXTENDED, newSide));
	}

	private void setState(Level world, BlockPos pos, boolean shouldBePersistent) {
		final Block blockBelow = world.getBlockState(pos.below()).getBlock();
		if (blockBelow instanceof BlockPSDDoor || blockBelow instanceof BlockPSDGlass || blockBelow instanceof BlockPSDGlassEnd) {
			if (shouldBePersistent) {
				world.setBlockAndUpdate(pos, world.getBlockState(pos).setValue(PERSISTENT, blockBelow instanceof BlockPSDDoor ? EnumPersistent.ARROW : blockBelow instanceof BlockPSDGlass ? EnumPersistent.ROUTE : EnumPersistent.BLANK));
			} else {
				world.setBlockAndUpdate(pos, world.getBlockState(pos).setValue(PERSISTENT, EnumPersistent.NONE));
			}
		}
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		return state;
	}

	@Override
	public Item asItem() {
		return Items.PSD_GLASS_1;
	}

	@Override
	public ItemStack getCloneItemStack(BlockGetter blockGetter, BlockPos blockPos, BlockState blockState) {
		return new ItemStack(asItem());
	}

	@Override
	public void playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
		final Block blockDown = world.getBlockState(pos.below()).getBlock();
		if (blockDown instanceof BlockPSDAPGBase && IBlock.getStatePropertySafe(state, PERSISTENT) == EnumPersistent.NONE) {
			blockDown.playerWillDestroy(world, pos.below(), world.getBlockState(pos.below()), player);
			world.setBlockAndUpdate(pos.below(), Blocks.AIR.defaultBlockState());
		}
		super.playerWillDestroy(world, pos, state, player);
	}

	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor world, BlockPos pos, BlockPos posFrom) {
		if (direction == Direction.DOWN && IBlock.getStatePropertySafe(state, PERSISTENT) == EnumPersistent.NONE && !(newState.getBlock() instanceof BlockPSDAPGBase)) {
			return Blocks.AIR.defaultBlockState();
		} else {
			return getActualState(world, pos);
		}
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
		final VoxelShape baseShape = IBlock.getVoxelShapeByDirection(0, IBlock.getStatePropertySafe(state, PERSISTENT) == EnumPersistent.NONE ? 0 : PERSISTENT_OFFSET, 0, 16, 16, 6, IBlock.getStatePropertySafe(state, FACING));
		final boolean airLeft = IBlock.getStatePropertySafe(state, AIR_LEFT);
		final boolean airRight = IBlock.getStatePropertySafe(state, AIR_RIGHT);
		if (airLeft || airRight) {
			return BlockPSDAPGGlassEndBase.getEndOutlineShape(baseShape, state, 16, 6, airLeft, airRight);
		} else {
			return baseShape;
		}
	}

	@Override
	public PushReaction getPistonPushReaction(BlockState blockState) {
		return PushReaction.BLOCK;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(DOOR_LIGHT, FACING, SIDE_EXTENDED, AIR_LEFT, AIR_RIGHT, ARROW_DIRECTION, PERSISTENT);
	}

	@Override
	public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntityPSDTop(pos, state);
	}

	public static BlockState getActualState(BlockGetter world, BlockPos pos) {
		EnumDoorLight doorLight = EnumDoorLight.NONE;
		Direction facing = null;
		EnumSide side = null;
		boolean airLeft = false, airRight = false;

		final BlockState stateBelow = world.getBlockState(pos.below());
		final Block blockBelow = stateBelow.getBlock();
		if (blockBelow instanceof BlockPSDGlass || blockBelow instanceof BlockPSDDoor || blockBelow instanceof BlockPSDGlassEnd) {
			if (blockBelow instanceof BlockPSDDoor) {
				doorLight = IBlock.getStatePropertySafe(stateBelow, BlockPSDDoor.OPEN) > 0 ? EnumDoorLight.ON : EnumDoorLight.OFF;
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

			facing = IBlock.getStatePropertySafe(stateBelow, FACING);
		}

		final BlockState oldState = world.getBlockState(pos);
		BlockState newState = (oldState.getBlock() instanceof BlockPSDTop ? oldState : mtr.Blocks.PSD_TOP.defaultBlockState()).setValue(DOOR_LIGHT, doorLight).setValue(AIR_LEFT, airLeft).setValue(AIR_RIGHT, airRight);
		if (facing != null) {
			newState = newState.setValue(FACING, facing);
		}
		if (side != null) {
			newState = newState.setValue(SIDE_EXTENDED, side);
		}
		return newState;
	}

	public static class TileEntityPSDTop extends BlockEntityMapper {

		public TileEntityPSDTop(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.PSD_TOP_TILE_ENTITY, pos, state);
		}
	}

	public enum EnumDoorLight implements StringRepresentable {

		ON("on"), OFF("off"), NONE("none");
		private final String name;

		EnumDoorLight(String nameIn) {
			name = nameIn;
		}

		@Override
		public String getSerializedName() {
			return name;
		}
	}

	public enum EnumPersistent implements StringRepresentable {

		NONE("none"), ARROW("arrow"), ROUTE("route"), BLANK("blank");
		private final String name;

		EnumPersistent(String nameIn) {
			name = nameIn;
		}

		@Override
		public String getSerializedName() {
			return name;
		}
	}
}
