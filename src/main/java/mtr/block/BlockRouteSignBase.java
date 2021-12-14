package mtr.block;

import mtr.packet.PacketTrainDataGuiServer;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public abstract class BlockRouteSignBase extends BlockDirectionalDoubleBlockBase implements BlockEntityProvider, IPropagateBlock, IBlock {

	public BlockRouteSignBase() {
		super(FabricBlockSettings.of(Material.METAL, MapColor.IRON_GRAY).requiresTool().hardness(2).luminance(15).nonOpaque());
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		final double y = hit.getPos().y;
		final boolean isUpper = IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER;
		return IBlock.checkHoldingBrush(world, player, () -> {
			if (isUpper && y - (int) y > 0.8125) {
				world.setBlockState(pos, state.cycle(PROPAGATE_PROPERTY));
				propagate(world, pos, Direction.DOWN, 1);
			} else {
				final BlockEntity entity = world.getBlockEntity(pos.down(isUpper ? 1 : 0));
				if (entity instanceof TileEntityRouteSignBase) {
					PacketTrainDataGuiServer.openRailwaySignScreenS2C((ServerPlayerEntity) player, entity.getPos());
				}
			}
		});
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING, HALF, PROPAGATE_PROPERTY);
	}

	public static abstract class TileEntityRouteSignBase extends BlockEntity implements BlockEntityClientSerializable {

		private long platformId;
		private static final String KEY_PLATFORM_ID = "platform_id";

		public TileEntityRouteSignBase(BlockEntityType<?> type) {
			super(type);
		}

		@Override
		public void fromTag(BlockState state, NbtCompound nbtCompound) {
			super.fromTag(state, nbtCompound);
			fromClientTag(nbtCompound);
		}

		@Override
		public NbtCompound writeNbt(NbtCompound nbtCompound) {
			super.writeNbt(nbtCompound);
			toClientTag(nbtCompound);
			return nbtCompound;
		}

		@Override
		public void fromClientTag(NbtCompound nbtCompound) {
			platformId = nbtCompound.getLong(KEY_PLATFORM_ID);
		}

		@Override
		public NbtCompound toClientTag(NbtCompound nbtCompound) {
			nbtCompound.putLong(KEY_PLATFORM_ID, platformId);
			return nbtCompound;
		}

		public void setPlatformId(long platformId) {
			this.platformId = platformId;
			markDirty();
			sync();
		}

		public long getPlatformId() {
			return platformId;
		}
	}
}
