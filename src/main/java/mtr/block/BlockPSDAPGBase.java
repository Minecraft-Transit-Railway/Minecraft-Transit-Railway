package mtr.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;

public abstract class BlockPSDAPGBase extends HorizontalFacingBlock {

	public static final BooleanProperty TOP = BooleanProperty.of("top");

	public BlockPSDAPGBase() {
		super(FabricBlockSettings.of(Material.METAL, MaterialColor.IRON).requiresTool().hardness(2).nonOpaque());
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
		boolean isTop = state.get(TOP);
		if ((isTop && direction == Direction.DOWN || !isTop && direction == Direction.UP) && !newState.isOf(this)) {
			return Blocks.AIR.getDefaultState();
		} else {
			return state;
		}
	}

	@Override
	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return state;
	}

	@Override
	public BlockState mirror(BlockState state, BlockMirror mirror) {
		return state;
	}

	@Override
	public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
		return new ItemStack(asItem());
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		final int height = isAPG() && state.get(TOP) ? 9 : 16;
		return IBlock.getVoxelShapeByDirection(0, 0, 0, 16, height, 4, state.get(FACING));
	}

	@Override
	public PistonBehavior getPistonBehavior(BlockState state) {
		return PistonBehavior.BLOCK;
	}

	protected boolean isAPG() {
		return this instanceof BlockAPGDoor || this instanceof BlockAPGGlass || this instanceof BlockAPGGlassEnd;
	}
}
