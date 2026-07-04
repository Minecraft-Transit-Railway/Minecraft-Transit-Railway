package org.mtr.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;
import org.mtr.registry.BlockEntityTypes;

public class BlockStationNameEntrance extends BlockStationNameBase implements IBlock {

	public static final IntegerProperty STYLE = IntegerProperty.create("propagate_property", 0, 5);

	public BlockStationNameEntrance(BlockBehaviour.Properties blockSettings) {
		super(blockSettings);
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
		return IBlock.checkHoldingBrush(world, player, () -> {
			world.setBlockAndUpdate(pos, state.cycle(STYLE));
			propagate(world, pos, IBlock.getStatePropertySafe(state, BlockStateProperties.HORIZONTAL_FACING).getClockWise(), STYLE, 1);
			propagate(world, pos, IBlock.getStatePropertySafe(state, BlockStateProperties.HORIZONTAL_FACING).getCounterClockWise(), STYLE, 1);
		});
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		final BlockPos pos = ctx.getClickedPos();
		final Direction side = ctx.getClickedFace();
		final Direction facing = side.getOpposite();

		if (side != Direction.UP && side != Direction.DOWN) {
			final BlockState leftState = ctx.getLevel().getBlockState(pos.relative(facing.getCounterClockWise()));
			final BlockState rightState = ctx.getLevel().getBlockState(pos.relative(facing.getClockWise()));

			final int nearbyStyle;
			if (leftState.getBlock() instanceof BlockStationNameEntrance) {
				nearbyStyle = IBlock.getStatePropertySafe(leftState, STYLE);
			} else if (rightState.getBlock() instanceof BlockStationNameEntrance) {
				nearbyStyle = IBlock.getStatePropertySafe(rightState, STYLE);
			} else {
				nearbyStyle = 0;
			}

			return defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, facing).setValue(STYLE, nearbyStyle);
		} else {
			return null;
		}
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		final boolean tall = IBlock.getStatePropertySafe(state, STYLE) % 2 == 1;
		return IBlock.getVoxelShapeByDirection(0, tall ? 0 : 4, 0, 16, tall ? 16 : 12, 1, IBlock.getStatePropertySafe(state, BlockStateProperties.HORIZONTAL_FACING));
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return Shapes.empty();
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new StationNameEntranceBlockEntity(blockPos, blockState);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.HORIZONTAL_FACING);
		builder.add(STYLE);
	}

	public static class StationNameEntranceBlockEntity extends BlockEntityBase {

		public StationNameEntranceBlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.STATION_NAME_ENTRANCE.get(), pos, state, 0, 0.00625F, false);
		}

		@Override
		public int getColor(BlockState state) {
			return switch (IBlock.getStatePropertySafe(state, BlockStationNameBase.COLOR)) {
				case 1 -> ARGB_LIGHT_GRAY;
				case 2 -> ARGB_BLACK;
				default -> ARGB_WHITE;
			};
		}
	}
}
