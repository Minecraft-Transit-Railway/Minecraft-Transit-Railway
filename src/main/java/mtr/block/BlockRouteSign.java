package mtr.block;

import mtr.MTR;
import mtr.gui.IGui;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class BlockRouteSign extends BlockDirectionalDoubleBlockBase implements BlockEntityProvider, IPropagateBlock, IBlock {

	public BlockRouteSign() {
		super(FabricBlockSettings.of(Material.METAL, MaterialColor.IRON).requiresTool().hardness(2).luminance(15).nonOpaque());
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		return IBlock.checkHoldingBrush(world, player, () -> {
			if (IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER) {
				world.setBlockState(pos, state.cycle(PROPAGATE_PROPERTY));
				propagate(world, pos, Direction.DOWN, 1);
			} else {
				final BlockEntity entity = world.getBlockEntity(pos);
				if (entity instanceof TileEntityRouteSign) {
					((TileEntityRouteSign) entity).cyclePlatformIndex();
				}
			}
		});
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		final int bottom = IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.LOWER ? 10 : 0;
		return IBlock.getVoxelShapeByDirection(2, bottom, 0, 14, 16, 1, IBlock.getStatePropertySafe(state, FACING));
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new TileEntityRouteSign();
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING, HALF, PROPAGATE_PROPERTY);
	}

	public static class TileEntityRouteSign extends BlockEntity implements BlockEntityClientSerializable, IGui {

		private int platformIndex;
		private static final String KEY_PLATFORM_INDEX = "platform_index";

		public TileEntityRouteSign() {
			super(MTR.ROUTE_SIGN_TILE_ENTITY);
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
			platformIndex = tag.getInt(KEY_PLATFORM_INDEX);
		}

		@Override
		public CompoundTag toClientTag(CompoundTag tag) {
			tag.putInt(KEY_PLATFORM_INDEX, platformIndex);
			return tag;
		}

		public void cyclePlatformIndex() {
			platformIndex++;
			markDirty();
			sync();
		}

		public int getPlatformIndex() {
			return platformIndex;
		}
	}
}
