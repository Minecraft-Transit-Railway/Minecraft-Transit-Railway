package mtr.block;

import mtr.MTR;
import mtr.data.Rail;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

import java.util.ArrayList;
import java.util.List;

public class BlockRail extends HorizontalFacingBlock implements BlockEntityProvider {

	public static final BooleanProperty IS_CONNECTED = BooleanProperty.of("is_connected");

	public BlockRail(Settings settings) {
		super(settings);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return getDefaultState().with(FACING, ctx.getPlayerFacing());
	}

	@Override
	public PistonBehavior getPistonBehavior(BlockState state) {
		return PistonBehavior.BLOCK;
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING, IS_CONNECTED);
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new TileEntityRail();
	}

	public static class TileEntityRail extends BlockEntity implements BlockEntityClientSerializable {

		public final List<Rail> railList = new ArrayList<>();
		private static final String KEY_LIST_LENGTH = "list_length";

		public TileEntityRail() {
			super(MTR.RAIL_TILE_ENTITY);
		}

		@Override
		public void fromTag(BlockState state, CompoundTag tag) {
			super.fromTag(state, tag);
			fromClientTag(tag);
		}

		@Override
		public CompoundTag toTag(CompoundTag tag) {
			super.toTag(tag);
			toClientTag(tag);
			return tag;
		}

		@Override
		public void fromClientTag(CompoundTag tag) {
			railList.clear();
			final int listLength = tag.getInt(KEY_LIST_LENGTH);
			for (int i = 0; i < listLength; i++) {
				railList.add(new Rail(tag.getCompound(KEY_LIST_LENGTH + i)));
			}
		}

		@Override
		public CompoundTag toClientTag(CompoundTag tag) {
			tag.putInt(KEY_LIST_LENGTH, railList.size());
			for (int i = 0; i < railList.size(); i++) {
				tag.put(KEY_LIST_LENGTH + i, railList.get(i).toTag());
			}
			return tag;
		}

		public void addRail(BlockPos newPos) {
			if (world != null && world.getBlockState(newPos).getBlock() instanceof BlockRail) {
				final Direction facing1 = IBlock.getStatePropertySafe(world, pos, HorizontalFacingBlock.FACING);
				final Direction facing2 = IBlock.getStatePropertySafe(world, newPos, HorizontalFacingBlock.FACING);
				railList.add(new Rail(pos, facing1, newPos, facing2));
				markDirty();
				sync();
			}
		}
	}
}
