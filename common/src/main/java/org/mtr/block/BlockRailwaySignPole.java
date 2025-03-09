package org.mtr.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.mtr.generated.lang.TranslationProvider;

import javax.annotation.Nonnull;

public class BlockRailwaySignPole extends BlockPoleCheckBase {

	public static final IntProperty TYPE = IntProperty.of("type", 0, 3);

	public BlockRailwaySignPole(AbstractBlock.Settings blockSettings) {
		super(blockSettings);
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		final Direction facing = IBlock.getStatePropertySafe(state, Properties.FACING);
		return switch (IBlock.getStatePropertySafe(state, TYPE)) {
			case 0 -> IBlock.getVoxelShapeByDirection(14, 0, 7, 15.25, 16, 9, facing);
			case 1 -> IBlock.getVoxelShapeByDirection(10, 0, 7, 11.25, 16, 9, facing);
			case 2 -> IBlock.getVoxelShapeByDirection(6, 0, 7, 7.25, 16, 9, facing);
			case 3 -> IBlock.getVoxelShapeByDirection(2, 0, 7, 3.25, 16, 9, facing);
			default -> VoxelShapes.fullCube();
		};
	}

	@Override
	protected BlockState placeWithState(BlockState stateBelow) {
		final int type;
		final Block block = stateBelow.getBlock();
		if (block instanceof BlockRailwaySign) {
			type = (((BlockRailwaySign) block).length + (((BlockRailwaySign) block).isOdd ? 2 : 0)) % 4;
		} else {
			type = IBlock.getStatePropertySafe(stateBelow, TYPE);
		}
		return super.placeWithState(stateBelow).with(TYPE, type);
	}

	@Override
	protected boolean isBlock(Block block) {
		return (block instanceof BlockRailwaySign && ((BlockRailwaySign) block).length > 0) || block instanceof BlockRailwaySignPole;
	}

	@Override
	protected Text getTooltipBlockText() {
		return TranslationProvider.BLOCK_MTR_RAILWAY_SIGN.getText();
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(Properties.FACING);
		builder.add(TYPE);
	}
}