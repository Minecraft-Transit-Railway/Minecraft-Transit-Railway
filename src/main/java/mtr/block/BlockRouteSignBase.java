package mtr.block;

import mtr.gui.IGui;
import mtr.packet.PacketTrainDataGuiServer;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
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
		super(FabricBlockSettings.of(Material.METAL, MaterialColor.IRON).requiresTool().hardness(2).luminance(15).nonOpaque());
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

	public static abstract class TileEntityRouteSignBase extends BlockEntity implements BlockEntityClientSerializable, IGui {

		private long platformId;
		private static final String KEY_PLATFORM_ID = "platform_id";

		public TileEntityRouteSignBase(BlockEntityType<?> type) {
			super(type);
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
			platformId = tag.getLong(KEY_PLATFORM_ID);
		}

		@Override
		public CompoundTag toClientTag(CompoundTag tag) {
			tag.putLong(KEY_PLATFORM_ID, platformId);
			return tag;
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
