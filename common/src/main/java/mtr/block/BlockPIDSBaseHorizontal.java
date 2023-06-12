package mtr.block;

import mtr.data.IPIDS;
import mtr.mappings.BlockDirectionalMapper;
import mtr.mappings.EntityBlockMapper;
import mtr.mappings.Text;
import mtr.packet.PacketTrainDataGuiServer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;

import java.util.List;

public abstract class BlockPIDSBaseHorizontal extends BlockDirectionalMapper implements EntityBlockMapper, IPIDS {

	public BlockPIDSBaseHorizontal() {
		super(Properties.of(Material.METAL, MaterialColor.COLOR_GRAY).requiresCorrectToolForDrops().strength(2).lightLevel(state -> 5));
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
		return IBlock.checkHoldingBrush(world, player, () -> {
			final BlockPos otherPos = pos.relative(IBlock.getStatePropertySafe(state, FACING));
			final BlockEntity entity1 = world.getBlockEntity(pos);
			final BlockEntity entity2 = world.getBlockEntity(otherPos);

			if (entity1 instanceof TileEntityBlockPIDSBaseHorizontal && entity2 instanceof TileEntityBlockPIDSBaseHorizontal) {
				((TileEntityBlockPIDSBaseHorizontal) entity1).syncData();
				((TileEntityBlockPIDSBaseHorizontal) entity2).syncData();
				PacketTrainDataGuiServer.openPIDSConfigScreenS2C((ServerPlayer) player, pos, otherPos, ((TileEntityBlockPIDSBaseHorizontal) entity1).getMaxArrivals(), ((TileEntityBlockPIDSBaseHorizontal) entity1).getLinesPerArrival());
			}
		});
	}

	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor world, BlockPos pos, BlockPos posFrom) {
		if (IBlock.getStatePropertySafe(state, FACING) == direction && !newState.is(this)) {
			return Blocks.AIR.defaultBlockState();
		} else {
			return state;
		}
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		final Direction direction = ctx.getHorizontalDirection().getOpposite();
		return IBlock.isReplaceable(ctx, direction, 2) ? defaultBlockState().setValue(FACING, direction) : null;
	}

	@Override
	public void playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
		final Direction facing = IBlock.getStatePropertySafe(state, FACING);
		if (facing == Direction.SOUTH || facing == Direction.WEST) {
			IBlock.onBreakCreative(world, player, pos.relative(facing));
		}
		super.playerWillDestroy(world, pos, state, player);
	}

	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		if (!world.isClientSide) {
			final Direction direction = IBlock.getStatePropertySafe(state, FACING);
			world.setBlock(pos.relative(direction), defaultBlockState().setValue(FACING, direction.getOpposite()), 3);
			world.updateNeighborsAt(pos, Blocks.AIR);
			state.updateNeighbourShapes(world, pos, 3);
			final BlockEntity entity1 = world.getBlockEntity(pos);
			final BlockEntity entity2 = world.getBlockEntity(pos.relative(direction));
			if (entity1 instanceof TileEntityBlockPIDSBaseHorizontal && entity2 instanceof TileEntityBlockPIDSBaseHorizontal) {
				System.arraycopy(((TileEntityBlockPIDSBaseHorizontal) entity1).messages, 0, ((TileEntityBlockPIDSBaseHorizontal) entity2).messages, 0, Math.min(((TileEntityBlockPIDSBaseHorizontal) entity1).messages.length, ((TileEntityBlockPIDSBaseHorizontal) entity2).messages.length));
			}
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, BlockGetter blockGetter, List<Component> tooltip, TooltipFlag tooltipFlag) {
		final BlockEntity blockEntity = createBlockEntity(new BlockPos(0, 0, 0), null);
		if (blockEntity instanceof TileEntityPIDS) {
			tooltip.add(Text.translatable("tooltip.mtr.arrivals", ((TileEntityPIDS) blockEntity).getMaxArrivals()).setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
		}
	}

	@Override
	public PushReaction getPistonPushReaction(BlockState blockState) {
		return PushReaction.BLOCK;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	public abstract static class TileEntityBlockPIDSBaseHorizontal extends TileEntityPIDS {

		public TileEntityBlockPIDSBaseHorizontal(BlockEntityType<?> type, BlockPos pos, BlockState state) {
			super(type, pos, state);
		}

		@Override
		public int getLinesPerArrival() {
			return 1;
		}
	}
}
