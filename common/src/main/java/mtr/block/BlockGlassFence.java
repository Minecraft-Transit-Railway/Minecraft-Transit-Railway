package mtr.block;

import mtr.mappings.Text;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

public class BlockGlassFence extends BlockDirectionalDoubleBlockBase {

	public static final IntegerProperty NUMBER = IntegerProperty.create("number", 1, 7);

	public BlockGlassFence() {
		super(Properties.of().requiresCorrectToolForDrops().strength(2).noOcclusion());
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
		final Direction facing = IBlock.getStatePropertySafe(state, FACING);
		if (IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER) {
			return IBlock.getVoxelShapeByDirection(0, 0, 0, 16, 3, 3, facing);
		} else {
			return IBlock.getVoxelShapeByDirection(0, 0, 0, 16, 16, 3, facing);
		}
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		final Direction facing = IBlock.getStatePropertySafe(state, FACING);
		return Shapes.or(getShape(state, world, pos, context), IBlock.getVoxelShapeByDirection(0, 0, 0, 16, 8, 3, facing));
	}

	@Override
	public VoxelShape getVisualShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
		return Shapes.empty();
	}

	@Override
	public void appendHoverText(ItemStack stack, BlockGetter blockGetter, List<Component> tooltip, TooltipFlag tooltipFlag) {
		tooltip.add(Text.translatable("tooltip." + stack.getItem().getDescriptionId()).setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, HALF, NUMBER);
	}

	@Override
	protected BlockState getAdditionalState(BlockPos pos, Direction facing) {
		return defaultBlockState().setValue(NUMBER, getNumber(pos, facing));
	}

	private static int getNumber(BlockPos pos, Direction facing) {
		final int x = (pos.getX() % 7 + 7) % 7;
		final int z = (pos.getZ() % 7 + 7) % 7;
		if (facing == Direction.NORTH || facing == Direction.EAST) {
			return ((x + z) % 7) + 1;
		} else {
			return ((-x - z) % 7) + 7;
		}
	}
}
