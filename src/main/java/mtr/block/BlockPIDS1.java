package mtr.block;

import mtr.MTR;
import mtr.packet.PacketTrainDataGuiServer;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class BlockPIDS1 extends BlockPIDSBase {

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		VoxelShape shape1 = IBlock.getVoxelShapeByDirection(6, 0, 0, 10, 11, 16, IBlock.getStatePropertySafe(state, FACING));
		VoxelShape shape2 = IBlock.getVoxelShapeByDirection(7.5, 11, 12.5, 8.5, 16, 13.5, IBlock.getStatePropertySafe(state, FACING));
		return VoxelShapes.union(shape1, shape2);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		return IBlock.checkHoldingBrush(world, player, () -> {
			final BlockEntity entity = world.getBlockEntity(pos);

			if (entity instanceof BlockPIDS1.TileEntityBlockPIDS1) {
				((BlockPIDS1.TileEntityBlockPIDS1) entity).sync();
				PacketTrainDataGuiServer.openPIDSConfigScreenS2C((ServerPlayerEntity) player, pos);
			}
		});
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new TileEntityBlockPIDS1();
	}

	public static class TileEntityBlockPIDS1 extends BlockEntity implements BlockEntityClientSerializable {

		public TileEntityBlockPIDS1() {
			super(MTR.PIDS_1_TILE_ENTITY);
		}

		private String message = "";
		private static final String KEY_MESSAGE = "message";

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
	}
}