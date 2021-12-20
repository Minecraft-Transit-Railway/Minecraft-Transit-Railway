package mtr.block;

import mtr.BlockEntityTypes;
import mtr.Items;
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
import net.minecraft.world.level.Explosion;
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
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockPSDTop extends HorizontalDirectionalBlock implements EntityBlockMapper, IBlock, IPropagateBlock {

	public static final EnumProperty<EnumDoorLight> DOOR_LIGHT = EnumProperty.create("door_light", EnumDoorLight.class);
	public static final BooleanProperty AIR_LEFT = BooleanProperty.create("air_left");
	public static final BooleanProperty AIR_RIGHT = BooleanProperty.create("air_right");

	public BlockPSDTop() {
		super(Properties.of(Material.METAL, MaterialColor.QUARTZ).requiresCorrectToolForDrops().strength(2).noOcclusion());
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
		return IBlock.checkHoldingBrush(world, player, () -> {
			world.setBlockAndUpdate(pos, state.cycle(PROPAGATE_PROPERTY));
			propagate(world, pos, IBlock.getStatePropertySafe(state, FACING).getClockWise(), 1);
			propagate(world, pos, IBlock.getStatePropertySafe(state, FACING).getCounterClockWise(), 1);
		});
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
		if (blockDown instanceof BlockPSDAPGBase) {
			blockDown.playerWillDestroy(world, pos.below(), world.getBlockState(pos.below()), player);
			world.setBlockAndUpdate(pos.below(), Blocks.AIR.defaultBlockState());
		}
		super.playerWillDestroy(world, pos, state, player);
	}

	@Override
	public void wasExploded(Level world, BlockPos pos, Explosion explosion) {
		playerWillDestroy(world, pos, null, null);
	}

	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor world, BlockPos pos, BlockPos posFrom) {
		if (direction == Direction.DOWN && !(newState.getBlock() instanceof BlockPSDAPGBase)) {
			return Blocks.AIR.defaultBlockState();
		} else {
			return getActualState(world, pos);
		}
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
		final VoxelShape baseShape = IBlock.getVoxelShapeByDirection(0, 0, 0, 16, 16, 6, IBlock.getStatePropertySafe(state, FACING));
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
		builder.add(DOOR_LIGHT, FACING, SIDE_EXTENDED, AIR_LEFT, AIR_RIGHT, PROPAGATE_PROPERTY);
	}

	@Override
	public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntityPSDTop(pos, state);
	}

	public static BlockState getActualState(BlockGetter world, BlockPos pos) {
		EnumDoorLight doorLight = EnumDoorLight.NONE;
		Direction facing = Direction.NORTH;
		EnumSide side = EnumSide.SINGLE;
		boolean airLeft = false, airRight = false;

		final BlockState stateBelow = world.getBlockState(pos.below());
		if (stateBelow.getBlock() instanceof BlockPSDAPGBase) {
			if (stateBelow.getBlock() instanceof BlockPSDAPGDoorBase) {
				doorLight = IBlock.getStatePropertySafe(stateBelow, BlockPSDAPGDoorBase.OPEN) > 0 ? EnumDoorLight.ON : EnumDoorLight.OFF;
				side = IBlock.getStatePropertySafe(stateBelow, SIDE);
			} else {
				side = IBlock.getStatePropertySafe(stateBelow, SIDE_EXTENDED);
			}

			if (stateBelow.getBlock() instanceof BlockPSDAPGGlassEndBase) {
				if (IBlock.getStatePropertySafe(stateBelow, BlockPSDAPGGlassEndBase.TOUCHING_LEFT) == BlockPSDAPGGlassEndBase.EnumPSDAPGGlassEndSide.AIR) {
					airLeft = true;
				}
				if (IBlock.getStatePropertySafe(stateBelow, BlockPSDAPGGlassEndBase.TOUCHING_RIGHT) == BlockPSDAPGGlassEndBase.EnumPSDAPGGlassEndSide.AIR) {
					airRight = true;
				}
			}

			facing = IBlock.getStatePropertySafe(stateBelow, FACING);
		}

		final BlockState oldState = world.getBlockState(pos);
		return (oldState.getBlock() instanceof BlockPSDTop ? oldState : mtr.Blocks.PSD_TOP.defaultBlockState()).setValue(DOOR_LIGHT, doorLight).setValue(FACING, facing).setValue(SIDE_EXTENDED, side).setValue(AIR_LEFT, airLeft).setValue(AIR_RIGHT, airRight);
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
}
