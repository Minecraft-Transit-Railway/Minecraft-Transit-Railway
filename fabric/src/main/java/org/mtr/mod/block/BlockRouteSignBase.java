package org.mtr.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mapping.mapper.BlockWithEntity;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.Blocks;
import org.mtr.mod.Init;
import org.mtr.mod.packet.PacketOpenBlockEntityScreen;

import javax.annotation.Nonnull;
import java.util.List;

public abstract class BlockRouteSignBase extends BlockDirectionalDoubleBlockBase implements IBlock, BlockWithEntity {

	public static final IntegerProperty ARROW_DIRECTION = IntegerProperty.of("propagate_property", 0, 3);

	public BlockRouteSignBase() {
		super(Blocks.createDefaultBlockSettings(true, blockState -> 15));
	}

	@Nonnull
	@Override
	public ActionResult onUse2(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		final double y = hit.getPos().getYMapped();
		final boolean isUpper = IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER;
		return IBlock.checkHoldingBrush(world, player, () -> {
			if (isUpper && y - Math.floor(y) > 0.8125) {
				world.setBlockState(pos, state.cycle(new Property<>(ARROW_DIRECTION.data)));
				propagate(world, pos, Direction.DOWN, new Property<>(ARROW_DIRECTION.data), 1);
			} else {
				final BlockEntity entity = world.getBlockEntity(pos.down(isUpper ? 1 : 0));
				if (entity != null && entity.data instanceof BlockEntityBase) {
					Init.REGISTRY.sendPacketToClient(ServerPlayerEntity.cast(player), new PacketOpenBlockEntityScreen(entity.getPos()));
				}
			}
		});
	}

	@Override
	public void addBlockProperties(List<HolderBase<?>> properties) {
		properties.add(FACING);
		properties.add(HALF);
		properties.add(ARROW_DIRECTION);
	}

	public static abstract class BlockEntityBase extends BlockEntityExtension {

		private long platformId;
		private static final String KEY_PLATFORM_ID = "platform_id";

		public BlockEntityBase(BlockEntityType<?> type, BlockPos pos, BlockState state) {
			super(type, pos, state);
		}

		@Override
		public void readCompoundTag(CompoundTag compoundTag) {
			platformId = compoundTag.getLong(KEY_PLATFORM_ID);
		}

		@Override
		public void writeCompoundTag(CompoundTag compoundTag) {
			compoundTag.putLong(KEY_PLATFORM_ID, platformId);
		}

		public void setPlatformId(long platformId) {
			this.platformId = platformId;
			markDirty2();
		}

		public long getPlatformId() {
			return platformId;
		}
	}
}
