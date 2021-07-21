package mtr.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

import java.util.List;

public class BlockGlassFence extends BlockDirectionalDoubleBlockBase {

	public static final IntProperty NUMBER = IntProperty.of("number", 1, 7);

	public BlockGlassFence() {
		super(FabricBlockSettings.of(Material.STONE, MapColor.IRON_GRAY).requiresTool().hardness(2).nonOpaque());
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		final Direction facing = IBlock.getStatePropertySafe(state, FACING);
		if (IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER) {
			return IBlock.getVoxelShapeByDirection(0, 0, 0, 16, 3, 3, facing);
		} else {
			return IBlock.getVoxelShapeByDirection(0, 0, 0, 16, 16, 3, facing);
		}
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		final Direction facing = IBlock.getStatePropertySafe(state, FACING);
		return VoxelShapes.union(getOutlineShape(state, world, pos, context), IBlock.getVoxelShapeByDirection(0, 0, 0, 16, 8, 3, facing));
	}

	@Override
	public void appendTooltip(ItemStack stack, BlockView world, List<Text> tooltip, TooltipContext options) {
		tooltip.add(new TranslatableText("tooltip." + stack.getItem().getTranslationKey()).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING, HALF, NUMBER);
	}

	@Override
	protected BlockState getAdditionalState(BlockPos pos, Direction facing) {
		return getDefaultState().with(NUMBER, getNumber(pos, facing));
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
