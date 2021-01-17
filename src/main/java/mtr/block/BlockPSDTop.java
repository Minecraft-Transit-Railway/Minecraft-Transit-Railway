package mtr.block;

import mtr.Items;
import mtr.MTR;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.explosion.Explosion;

public class BlockPSDTop extends HorizontalFacingBlock implements BlockEntityProvider, IBlock, IPropagateBlock {

	public static final EnumProperty<EnumDoorLight> DOOR_LIGHT = EnumProperty.of("door_light", EnumDoorLight.class);
	public static final BooleanProperty AIR_LEFT = BooleanProperty.of("air_left");
	public static final BooleanProperty AIR_RIGHT = BooleanProperty.of("air_right");

	public BlockPSDTop() {
		super(FabricBlockSettings.of(Material.METAL, MaterialColor.QUARTZ).requiresTool().hardness(2).luminance(15).nonOpaque());
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		return IBlock.checkHoldingBrush(world, player, () -> {
			world.setBlockState(pos, state.cycle(PROPAGATE_PROPERTY));
			propagate(world, pos, state.get(FACING).rotateYClockwise());
			propagate(world, pos, state.get(FACING).rotateYCounterclockwise());
		});
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
		final Block blockDown = world.getBlockState(pos.down()).getBlock();
		if (blockDown instanceof BlockPSDAPGBase) {
			blockDown.onBreak(world, pos.down(), world.getBlockState(pos.down()), player);
			world.setBlockState(pos.down(), Blocks.AIR.getDefaultState());
		}
		super.onBreak(world, pos, state, player);
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
			return IBlock.getVoxelShapeByDirection(0, 0, 0, 16, 16, 6, state.get(FACING));
		}
	}

	@Override
	public PistonBehavior getPistonBehavior(BlockState state) {
		return PistonBehavior.BLOCK;
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(DOOR_LIGHT, FACING, SIDE_EXTENDED, AIR_LEFT, AIR_RIGHT, PROPAGATE_PROPERTY);
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new TileEntityPSDTop();
	}

	public static BlockState getActualState(WorldAccess world, BlockPos pos) {
		EnumDoorLight doorLight = EnumDoorLight.NONE;
		Direction facing = Direction.NORTH;
		EnumSide side = EnumSide.SINGLE;
		boolean airLeft = false, airRight = false;

		final BlockState stateBelow = world.getBlockState(pos.down());
		if (stateBelow.getBlock() instanceof BlockPSDAPGBase) {
			if (stateBelow.getBlock() instanceof BlockPSDAPGDoorBase) {
				doorLight = stateBelow.get(BlockPSDAPGDoorBase.OPEN) > 0 ? EnumDoorLight.ON : EnumDoorLight.OFF;
				side = stateBelow.get(SIDE);
			} else {
				side = stateBelow.get(SIDE_EXTENDED);
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

		final BlockState oldState = world.getBlockState(pos);
		return (oldState.getBlock() instanceof BlockPSDTop ? oldState : mtr.Blocks.PSD_TOP.getDefaultState()).with(DOOR_LIGHT, doorLight).with(FACING, facing).with(SIDE_EXTENDED, side).with(AIR_LEFT, airLeft).with(AIR_RIGHT, airRight);
	}

	public static class TileEntityPSDTop extends BlockEntity {

		public TileEntityPSDTop() {
			super(MTR.PSD_TOP_TILE_ENTITY);
		}
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
