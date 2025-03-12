package org.mtr.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.mtr.packet.PacketOpenBlockEntityScreen;
import org.mtr.registry.Registry;

import javax.annotation.Nonnull;

public abstract class BlockRouteSignBase extends BlockDirectionalDoubleBlockBase implements IBlock, BlockEntityProvider {

	public static final IntProperty ARROW_DIRECTION = IntProperty.of("propagate_property", 0, 3);

	public BlockRouteSignBase(AbstractBlock.Settings settings) {
		super(settings.luminance(blockState -> 15));
	}

	@Nonnull
	@Override
	protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
		final double y = hit.getPos().y;
		final boolean isUpper = IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER;
		return IBlock.checkHoldingBrush(world, player, () -> {
			if (isUpper && y - Math.floor(y) > 0.8125) {
				world.setBlockState(pos, state.cycle(ARROW_DIRECTION));
				propagate(world, pos, Direction.DOWN, ARROW_DIRECTION, 1);
			} else {
				final BlockEntity entity = world.getBlockEntity(pos.down(isUpper ? 1 : 0));
				if (entity instanceof BlockEntityBase) {
					Registry.sendPacketToClient((ServerPlayerEntity) player, new PacketOpenBlockEntityScreen(entity.getPos()));
				}
			}
		});
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(Properties.HORIZONTAL_FACING);
		builder.add(HALF);
		builder.add(ARROW_DIRECTION);
	}

	public static abstract class BlockEntityBase extends BlockEntity {

		private long platformId;
		private static final String KEY_PLATFORM_ID = "platform_id";

		public BlockEntityBase(BlockEntityType<?> type, BlockPos pos, BlockState state) {
			super(type, pos, state);
		}

		@Override
		protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
			platformId = nbt.getLong(KEY_PLATFORM_ID);
		}

		@Override
		protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
			nbt.putLong(KEY_PLATFORM_ID, platformId);
		}

		public void setPlatformId(long platformId) {
			this.platformId = platformId;
			markDirty();
		}

		public long getPlatformId() {
			return platformId;
		}
	}
}
