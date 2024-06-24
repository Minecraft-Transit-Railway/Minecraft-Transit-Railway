package org.mtr.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.BlockEntityTypes;

import javax.annotation.Nonnull;
import java.util.List;

public class BlockStationNameEntrance extends BlockStationNameBase implements IBlock {

	public static final IntegerProperty STYLE = IntegerProperty.of("propagate_property", 0, 5);

	public BlockStationNameEntrance(BlockSettings blockSettings) {
		super(blockSettings);
	}

	@Nonnull
	@Override
	public ActionResult onUse2(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		return IBlock.checkHoldingBrush(world, player, () -> {
			world.setBlockState(pos, state.cycle(new Property<>(STYLE.data)));
			propagate(world, pos, IBlock.getStatePropertySafe(state, FACING).rotateYClockwise(), new Property<>(STYLE.data), 1);
			propagate(world, pos, IBlock.getStatePropertySafe(state, FACING).rotateYCounterclockwise(), new Property<>(STYLE.data), 1);
		});
	}

	@Override
	public BlockState getPlacementState2(ItemPlacementContext ctx) {
		final Direction side = ctx.getSide();
		if (side != Direction.UP && side != Direction.DOWN) {
			return getDefaultState2().with(new Property<>(FACING.data), side.getOpposite().data);
		} else {
			return null;
		}
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		final boolean tall = IBlock.getStatePropertySafe(state, STYLE) % 2 == 1;
		return IBlock.getVoxelShapeByDirection(0, tall ? 0 : 4, 0, 16, tall ? 16 : 12, 1, IBlock.getStatePropertySafe(state, FACING));
	}

	@Nonnull
	@Override
	public VoxelShape getCollisionShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return VoxelShapes.empty();
	}

	@Nonnull
	@Override
	public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new BlockEntity(blockPos, blockState);
	}

	@Override
	public void addBlockProperties(List<HolderBase<?>> properties) {
		properties.add(FACING);
		properties.add(STYLE);
	}

	public static class BlockEntity extends BlockEntityBase {

		public BlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.STATION_NAME_ENTRANCE.get(), pos, state, 0, 0.00625F, false);
		}

		@Override
		public int getColor(BlockState state) {
			switch (IBlock.getStatePropertySafe(state, BlockStationNameBase.COLOR)) {
				case 1:
					return ARGB_LIGHT_GRAY;
				case 2:
					return ARGB_BLACK;
				default:
					return ARGB_WHITE;
			}
		}
	}
}
