package mtr.block;

import mtr.MTR;
import mtr.packet.PacketTrainDataGuiServer;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class BlockTrainAnnouncer extends Block implements BlockEntityProvider {

	public BlockTrainAnnouncer(Settings settings) {
		super(settings);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		return IBlock.checkHoldingBrush(world, player, () -> {
			final BlockEntity entity = world.getBlockEntity(pos);
			if (entity instanceof TileEntityTrainAnnouncer) {
				((TileEntityTrainAnnouncer) entity).sync();
				PacketTrainDataGuiServer.openTrainAnnouncerScreenS2C((ServerPlayerEntity) player, pos);
			}
		});
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntityTrainAnnouncer(pos, state);
	}

	public static class TileEntityTrainAnnouncer extends BlockEntity implements BlockEntityClientSerializable {

		private String message = "";
		private final Map<PlayerEntity, Long> lastAnnouncedMillis = new HashMap<>();
		private static final int ANNOUNCE_COOL_DOWN_MILLIS = 5000;
		private static final String KEY_MESSAGE = "message";

		public TileEntityTrainAnnouncer(BlockPos pos, BlockState state) {
			super(MTR.TRAIN_ANNOUNCER_TILE_ENTITY, pos, state);
		}

		@Override
		public void readNbt(NbtCompound nbtCompound) {
			super.readNbt(nbtCompound);
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
			message = nbtCompound.getString(KEY_MESSAGE);
		}

		@Override
		public NbtCompound toClientTag(NbtCompound nbtCompound) {
			nbtCompound.putString(KEY_MESSAGE, message);
			return nbtCompound;
		}

		public void setMessage(String message) {
			this.message = message;
			markDirty();
			sync();
		}

		public String getMessage() {
			return message;
		}

		public void announce(PlayerEntity player) {
			final long currentMillis = System.currentTimeMillis();
			if (player != null && (!lastAnnouncedMillis.containsKey(player) || currentMillis - lastAnnouncedMillis.get(player) >= ANNOUNCE_COOL_DOWN_MILLIS)) {
				lastAnnouncedMillis.put(player, System.currentTimeMillis());
				PacketTrainDataGuiServer.announceS2C((ServerPlayerEntity) player, message);
			}
		}
	}
}
