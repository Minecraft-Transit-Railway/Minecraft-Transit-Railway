package org.mtr.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.mtr.item.ItemBrush;
import org.mtr.registry.BlockEntityTypes;
import org.mtr.registry.Items;

//? if >= 1.21.4 {
import net.minecraft.world.level.ScheduledTickAccess;
//? } else {
/*import net.minecraft.world.level.LevelAccessor;
 *///? }

public class BlockPSDTop extends Block implements IBlock, EntityBlock {

	private static final float PERSISTENT_OFFSET = 7.5F;
	public static final float PERSISTENT_OFFSET_SMALL = PERSISTENT_OFFSET / 16;

	public static final BooleanProperty AIR_LEFT = BooleanProperty.create("air_left");
	public static final BooleanProperty AIR_RIGHT = BooleanProperty.create("air_right");
	public static final IntegerProperty ARROW_DIRECTION = IntegerProperty.create("propagate_property", 0, 3);
	public static final EnumProperty<EnumPersistent> PERSISTENT = EnumProperty.create("persistent", EnumPersistent.class);

	public BlockPSDTop(BlockBehaviour.Properties settings) {
		super(settings.noOcclusion());
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
		return IBlock.checkHoldingItem(world, player, item -> {
			if (item instanceof ItemBrush) {
				world.setBlockAndUpdate(pos, state.cycle(ARROW_DIRECTION));
				propagate(world, pos, IBlock.getStatePropertySafe(state, BlockStateProperties.HORIZONTAL_FACING).getClockWise(), ARROW_DIRECTION, 1);
				propagate(world, pos, IBlock.getStatePropertySafe(state, BlockStateProperties.HORIZONTAL_FACING).getCounterClockWise(), ARROW_DIRECTION, 1);
			} else {
				final boolean shouldBePersistent = IBlock.getStatePropertySafe(state, PERSISTENT) == EnumPersistent.NONE;
				setState(world, pos, shouldBePersistent);
				propagate(world, pos, IBlock.getStatePropertySafe(state, BlockStateProperties.HORIZONTAL_FACING).getClockWise(), offsetPos -> setState(world, offsetPos, shouldBePersistent), 1);
				propagate(world, pos, IBlock.getStatePropertySafe(state, BlockStateProperties.HORIZONTAL_FACING).getCounterClockWise(), offsetPos -> setState(world, offsetPos, shouldBePersistent), 1);
			}
		}, null, Items.BRUSH.get(), net.minecraft.world.item.Items.SHEARS);
	}

	private void setState(Level world, BlockPos pos, boolean shouldBePersistent) {
		final Block blockBelow = world.getBlockState(pos.below()).getBlock();
		if (blockBelow instanceof BlockPSDDoor || blockBelow instanceof BlockPSDGlass || blockBelow instanceof BlockPSDGlassEnd) {
			if (shouldBePersistent) {
				world.setBlockAndUpdate(pos, world.getBlockState(pos).setValue(PERSISTENT, blockBelow instanceof BlockPSDDoor ? EnumPersistent.ARROW : (blockBelow instanceof BlockPSDGlass ? EnumPersistent.ROUTE : EnumPersistent.BLANK)));
			} else {
				world.setBlockAndUpdate(pos, world.getBlockState(pos).setValue(PERSISTENT, EnumPersistent.NONE));
			}
		}
	}

	@Override
	public Item asItem() {
		return Items.PSD_GLASS_1.get();
	}

	@Override
//? if >= 1.21.4 {
	protected ItemStack getCloneItemStack(LevelReader world, BlockPos pos, BlockState state, boolean includeData) {
//? } else {
	/*public ItemStack getCloneItemStack(LevelReader levelReader, BlockPos blockPos, BlockState blockState) {
//
*///? }
		return new ItemStack(asItem());
	}

	@Override
	public BlockState playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
		final Block blockDown = world.getBlockState(pos.below()).getBlock();
		if (blockDown instanceof BlockPSDAPGBase) {
			blockDown.playerWillDestroy(world, pos.below(), world.getBlockState(pos.below()), player);
			world.setBlockAndUpdate(pos.below(), Blocks.AIR.defaultBlockState());
		}
		return super.playerWillDestroy(world, pos, state, player);
	}

	@Override
//? if >= 1.21.4 {
	protected BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
//? } else {
	/*protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
//
*///? }
		if (direction == Direction.DOWN && IBlock.getStatePropertySafe(state, PERSISTENT) == EnumPersistent.NONE && !(neighborState.getBlock() instanceof BlockPSDAPGBase)) {
			return Blocks.AIR.defaultBlockState();
		} else {
			return getActualState(world, pos);
		}
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		final VoxelShape baseShape = IBlock.getVoxelShapeByDirection(0, IBlock.getStatePropertySafe(state, PERSISTENT) == EnumPersistent.NONE ? 0 : PERSISTENT_OFFSET, 0, 16, 16, 6, IBlock.getStatePropertySafe(state, BlockStateProperties.HORIZONTAL_FACING));
		final boolean airLeft = IBlock.getStatePropertySafe(state, AIR_LEFT);
		final boolean airRight = IBlock.getStatePropertySafe(state, AIR_RIGHT);
		if (airLeft || airRight) {
			return BlockPSDAPGGlassEndBase.getEndOutlineShape(baseShape, state, 16, 6, airLeft, airRight);
		} else {
			return baseShape;
		}
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new PSDTopBlockEntity(blockPos, blockState);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.HORIZONTAL_FACING);
		builder.add(SIDE_EXTENDED);
		builder.add(AIR_LEFT);
		builder.add(AIR_RIGHT);
		builder.add(ARROW_DIRECTION);
		builder.add(PERSISTENT);
	}

	public static BlockState getActualState(LevelReader world, BlockPos pos) {
		Direction facing = null;
		EnumSide side = null;
		boolean airLeft = false, airRight = false;

		final BlockState stateBelow = world.getBlockState(pos.below());
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

			facing = IBlock.getStatePropertySafe(stateBelow, BlockStateProperties.HORIZONTAL_FACING);
		}

		final BlockState oldState = world.getBlockState(pos);
		BlockState neighborState = (oldState.getBlock() instanceof BlockPSDTop ? oldState : org.mtr.registry.Blocks.PSD_TOP.get().defaultBlockState()).setValue(AIR_LEFT, airLeft).setValue(AIR_RIGHT, airRight);
		if (facing != null) {
			neighborState = neighborState.setValue(BlockStateProperties.HORIZONTAL_FACING, facing);
		}
		if (side != null) {
			neighborState = neighborState.setValue(SIDE_EXTENDED, side);
		}
		return neighborState;
	}

	public static class PSDTopBlockEntity extends BlockEntityBase {

		public PSDTopBlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.PSD_TOP.get(), pos, state);
		}
	}

	public static class BlockEntityBase extends BlockEntityExtension {

		public BlockEntityBase(BlockEntityType<?> type, BlockPos pos, BlockState state) {
			super(type, pos, state);
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
