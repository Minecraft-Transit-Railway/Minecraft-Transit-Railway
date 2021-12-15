package mtr.block;

import minecraftmappings.BlockEntityClientSerializableMapper;
import minecraftmappings.BlockEntityProviderMapper;
import mtr.packet.PacketTrainDataGuiServer;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public abstract class BlockPIDSBase extends HorizontalFacingBlock implements BlockEntityProviderMapper {

	public BlockPIDSBase() {
		super(FabricBlockSettings.of(Material.METAL, MapColor.IRON_GRAY).requiresTool().hardness(2).luminance(5));
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		return IBlock.checkHoldingBrush(world, player, () -> {
			final BlockPos otherPos = pos.offset(IBlock.getStatePropertySafe(state, FACING));
			final BlockEntity entity1 = world.getBlockEntity(pos);
			final BlockEntity entity2 = world.getBlockEntity(otherPos);

			if (entity1 instanceof TileEntityBlockPIDSBase && entity2 instanceof TileEntityBlockPIDSBase) {
				((TileEntityBlockPIDSBase) entity1).sync();
				((TileEntityBlockPIDSBase) entity2).sync();
				PacketTrainDataGuiServer.openPIDSConfigScreenS2C((ServerPlayerEntity) player, pos, otherPos, ((TileEntityBlockPIDSBase) entity1).getMaxArrivals());
			}
		});
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
		if (IBlock.getStatePropertySafe(state, FACING) == direction && !newState.isOf(this)) {
			return Blocks.AIR.getDefaultState();
		} else {
			return state;
		}
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		final Direction direction = ctx.getPlayerFacing().getOpposite();
		return IBlock.isReplaceable(ctx, direction, 2) ? getDefaultState().with(FACING, direction) : null;
	}

	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		final Direction facing = IBlock.getStatePropertySafe(state, FACING);
		if (facing == Direction.SOUTH || facing == Direction.WEST) {
			IBlock.onBreakCreative(world, player, pos.offset(facing));
		}
		super.onBreak(world, pos, state, player);
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		if (!world.isClient) {
			final Direction direction = IBlock.getStatePropertySafe(state, FACING);
			world.setBlockState(pos.offset(direction), getDefaultState().with(FACING, direction.getOpposite()), 3);
			world.updateNeighbors(pos, Blocks.AIR);
			state.updateNeighbors(world, pos, 3);
			final BlockEntity entity1 = world.getBlockEntity(pos);
			final BlockEntity entity2 = world.getBlockEntity(pos.offset(direction));
			if (entity1 instanceof TileEntityBlockPIDSBase && entity2 instanceof TileEntityBlockPIDSBase) {
				System.arraycopy(((TileEntityBlockPIDSBase) entity1).messages, 0, ((TileEntityBlockPIDSBase) entity2).messages, 0, Math.min(((TileEntityBlockPIDSBase) entity1).messages.length, ((TileEntityBlockPIDSBase) entity2).messages.length));
			}
		}
	}

	@Override
	public PistonBehavior getPistonBehavior(BlockState state) {
		return PistonBehavior.BLOCK;
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	public abstract static class TileEntityBlockPIDSBase extends BlockEntityClientSerializableMapper {

		private final String[] messages = new String[getMaxArrivals()];
		private final boolean[] hideArrival = new boolean[getMaxArrivals()];
		private static final String KEY_MESSAGE = "message";
		private static final String KEY_HIDE_ARRIVAL = "hide_arrival";

		public TileEntityBlockPIDSBase(BlockEntityType<?> type, BlockPos pos, BlockState state) {
			super(type, pos, state);
		}

		@Override
		public void readNbtCompound(NbtCompound nbtCompound) {
			for (int i = 0; i < getMaxArrivals(); i++) {
				messages[i] = nbtCompound.getString(KEY_MESSAGE + i);
				hideArrival[i] = nbtCompound.getBoolean(KEY_HIDE_ARRIVAL + i);
			}
		}

		@Override
		public void writeNbtCompound(NbtCompound nbtCompound) {
			for (int i = 0; i < getMaxArrivals(); i++) {
				nbtCompound.putString(KEY_MESSAGE + i, messages[i] == null ? "" : messages[i]);
				nbtCompound.putBoolean(KEY_HIDE_ARRIVAL + i, hideArrival[i]);
			}
		}

		public void setData(String[] messages, boolean[] hideArrival) {
			System.arraycopy(messages, 0, this.messages, 0, Math.min(messages.length, this.messages.length));
			System.arraycopy(hideArrival, 0, this.hideArrival, 0, Math.min(hideArrival.length, this.hideArrival.length));
			markDirty();
			sync();
		}

		public String getMessage(int index) {
			if (index >= 0 && index < getMaxArrivals()) {
				if (messages[index] == null) {
					messages[index] = "";
				}
				return messages[index];
			} else {
				return "";
			}
		}

		public boolean getHideArrival(int index) {
			if (index >= 0 && index < getMaxArrivals()) {
				return hideArrival[index];
			} else {
				return false;
			}
		}

		protected abstract int getMaxArrivals();
	}
}