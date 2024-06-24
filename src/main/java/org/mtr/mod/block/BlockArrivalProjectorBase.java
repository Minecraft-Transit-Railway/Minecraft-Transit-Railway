package org.mtr.mod.block;

import it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.*;
import org.mtr.mapping.registry.Registry;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.data.IPIDSRenderChild;
import org.mtr.mod.packet.PacketOpenBlockEntityScreen;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class BlockArrivalProjectorBase extends BlockExtension implements DirectionHelper, BlockWithEntity {

	public BlockArrivalProjectorBase() {
		super(BlockHelper.createBlockSettings(true, blockState -> 5));
	}

	@Nonnull
	@Override
	public ActionResult onUse2(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		return IBlock.checkHoldingBrush(world, player, () -> {
			final BlockEntity entity = world.getBlockEntity(pos);

			if (entity != null && entity.data instanceof BlockEntityBase) {
				((BlockEntityBase) entity.data).markDirty2();
				Registry.sendPacketToClient(ServerPlayerEntity.cast(player), new PacketOpenBlockEntityScreen(pos));
			}
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
		final Direction facing = IBlock.getStatePropertySafe(state, FACING);
		return IBlock.getVoxelShapeByDirection(0, 0, 0, 16, 16, 1, facing);
	}

	@Override
	public void addBlockProperties(List<HolderBase<?>> properties) {
		properties.add(FACING);
	}

	public static class BlockEntityBase extends BlockEntityExtension implements IPIDSRenderChild {

		private final LongAVLTreeSet platformIds = new LongAVLTreeSet();
		private int displayPage;

		private static final String KEY_PLATFORM_IDS = "platform_ids";
		private static final String KEY_DISPLAY_PAGE = "display_page";

		public BlockEntityBase(BlockEntityType<?> type, BlockPos pos, BlockState state) {
			super(type, pos, state);
		}

		@Override
		public void readCompoundTag(CompoundTag compoundTag) {
			platformIds.clear();
			final long[] platformIdsArray = compoundTag.getLongArray(KEY_PLATFORM_IDS);
			for (final long platformId : platformIdsArray) {
				platformIds.add(platformId);
			}
			displayPage = compoundTag.getInt(KEY_DISPLAY_PAGE);
		}

		@Override
		public void writeCompoundTag(CompoundTag compoundTag) {
			compoundTag.putLongArray(KEY_PLATFORM_IDS, new ArrayList<>(platformIds));
			compoundTag.putInt(KEY_DISPLAY_PAGE, displayPage);
		}

		public LongAVLTreeSet getPlatformIds() {
			return platformIds;
		}

		public int getDisplayPage() {
			return displayPage;
		}

		public void setData(Set<Long> platformIds, int displayPage) {
			this.platformIds.clear();
			this.platformIds.addAll(platformIds);
			this.displayPage = displayPage;
			markDirty2();
		}
	}
}
