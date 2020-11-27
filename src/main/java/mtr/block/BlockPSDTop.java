package mtr.block;

import mtr.Items;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.explosion.Explosion;

public class BlockPSDTop extends HorizontalFacingBlock {

	public static final EnumProperty<EnumDoorLight> DOOR_LIGHT = EnumProperty.of("door_light", EnumDoorLight.class);
	public static final EnumProperty<BlockPSDAPGGlassBase.EnumPSDAPGGlassSide> SIDE = EnumProperty.of("side", BlockPSDAPGGlassBase.EnumPSDAPGGlassSide.class);
	public static final BooleanProperty AIR_LEFT = BooleanProperty.of("air_left");
	public static final BooleanProperty AIR_RIGHT = BooleanProperty.of("air_right");

	public BlockPSDTop() {
		super(FabricBlockSettings.of(Material.GLASS, MaterialColor.QUARTZ).requiresTool().hardness(2).luminance(15).nonOpaque());
	}

	@Override
	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return state;
	}

	@Override
	public Item asItem() {
		return Items.PSD_GLASS;
	}

	@Override
	public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
		return new ItemStack(asItem());
	}

	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		if (world.getBlockState(pos.down()).getBlock() instanceof BlockPSDAPGBase) {
			world.setBlockState(pos.down(), Blocks.AIR.getDefaultState());
		}
	}

	@Override
	public void onDestroyedByExplosion(World world, BlockPos pos, Explosion explosion) {
		onBreak(world, pos, null, null);
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
		if (direction == Direction.DOWN && !(newState.getBlock() instanceof BlockPSDAPGBase)) {
			return Blocks.AIR.getDefaultState();
		} else {
			return getActualState(world, pos);
		}
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		if (state.get(AIR_LEFT) || state.get(AIR_RIGHT)) {
			return VoxelShapes.fullCube();
		} else {
			switch (state.get(FACING)) {
				case NORTH:
					return Block.createCuboidShape(0, 0, 0, 16, 16, 6);
				case EAST:
					return Block.createCuboidShape(10, 0, 0, 16, 16, 16);
				case SOUTH:
					return Block.createCuboidShape(0, 0, 10, 16, 16, 16);
				case WEST:
					return Block.createCuboidShape(0, 0, 0, 6, 16, 16);
				default:
					return VoxelShapes.fullCube();
			}
		}
	}

	@Override
	public PistonBehavior getPistonBehavior(BlockState state) {
		return PistonBehavior.BLOCK;
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(DOOR_LIGHT, FACING, SIDE, AIR_LEFT, AIR_RIGHT);
	}

	public static BlockState getActualState(WorldAccess world, BlockPos pos) {
		EnumDoorLight doorLight = EnumDoorLight.NONE;
		Direction facing = Direction.NORTH;
		BlockPSDAPGGlassBase.EnumPSDAPGGlassSide side = BlockPSDAPGGlassBase.EnumPSDAPGGlassSide.SINGLE;
		boolean airLeft = false, airRight = false;

		final BlockState stateBelow = world.getBlockState(pos.down());
		if (stateBelow.getBlock() instanceof BlockPSDAPGBase) {
			if (stateBelow.getBlock() instanceof BlockPSDAPGDoorBase) {
				doorLight = stateBelow.get(BlockPSDAPGDoorBase.OPEN) > 0 ? EnumDoorLight.ON : EnumDoorLight.OFF;
				side = stateBelow.get(BlockPSDAPGDoorBase.SIDE) == BlockPSDAPGDoorBase.EnumPSDAPGDoorSide.LEFT ? BlockPSDAPGGlassBase.EnumPSDAPGGlassSide.LEFT : BlockPSDAPGGlassBase.EnumPSDAPGGlassSide.RIGHT;
			} else {
				side = stateBelow.get(SIDE);
			}

			if (stateBelow.getBlock() instanceof BlockPSDAPGGlassEndBase) {
				if (stateBelow.get(BlockPSDAPGGlassEndBase.TOUCHING_LEFT) == BlockPSDAPGGlassEndBase.EnumPSDAPGGlassEndSide.AIR) {
					airLeft = true;
				}
				if (stateBelow.get(BlockPSDAPGGlassEndBase.TOUCHING_RIGHT) == BlockPSDAPGGlassEndBase.EnumPSDAPGGlassEndSide.AIR) {
					airRight = true;
				}
			}

			facing = stateBelow.get(FACING);
		}

		return mtr.Blocks.PSD_TOP.getDefaultState().with(DOOR_LIGHT, doorLight).with(FACING, facing).with(SIDE, side).with(AIR_LEFT, airLeft).with(AIR_RIGHT, airRight);
	}

	public enum EnumDoorLight implements StringIdentifiable {

		ON("on"), OFF("off"), NONE("none");

		private final String name;

		EnumDoorLight(String nameIn) {
			name = nameIn;
		}

		@Override
		public String asString() {
			return name;
		}
	}
}
