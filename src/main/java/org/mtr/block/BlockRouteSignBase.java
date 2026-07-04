package org.mtr.block;

import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.mtr.packet.PacketOpenBlockEntityScreen;

public abstract class BlockRouteSignBase extends BlockDirectionalDoubleBlockBase implements IBlock, EntityBlock {

	public static final IntegerProperty ARROW_DIRECTION = IntegerProperty.create("propagate_property", 0, 3);

	public BlockRouteSignBase(BlockBehaviour.Properties settings) {
		super(settings.lightLevel(blockState -> 15));
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
		final double y = hit.getLocation().y;
		final boolean isUpper = IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER;
		return IBlock.checkHoldingBrush(world, player, () -> {
			if (isUpper && y - Math.floor(y) > 0.8125) {
				world.setBlockAndUpdate(pos, state.cycle(ARROW_DIRECTION));
				propagate(world, pos, Direction.DOWN, ARROW_DIRECTION, 1);
			} else {
				final BlockEntity entity = world.getBlockEntity(pos.below(isUpper ? 1 : 0));
				if (entity instanceof BlockEntityBase) {
					PacketOpenBlockEntityScreen.sendDirectlyToServer((ServerLevel) world, (ServerPlayer) player, entity.getBlockPos());
				}
			}
		});
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.HORIZONTAL_FACING);
		builder.add(HALF);
		builder.add(ARROW_DIRECTION);
	}

	public static abstract class BlockEntityBase extends BlockEntityExtension {

		@Getter
		private long platformId;
		private static final String KEY_PLATFORM_ID = "platform_id";

		public BlockEntityBase(BlockEntityType<?> type, BlockPos pos, BlockState state) {
			super(type, pos, state);
		}

		@Override
		protected void readNbt(CompoundTag nbtCompound) {
			platformId = nbtCompound.getLong(KEY_PLATFORM_ID);
		}

		@Override
		protected void writeNbt(CompoundTag nbtCompound) {
			nbtCompound.putLong(KEY_PLATFORM_ID, platformId);
		}

		public void setPlatformId(long platformId) {
			this.platformId = platformId;
			setChanged();
		}
	}
}
