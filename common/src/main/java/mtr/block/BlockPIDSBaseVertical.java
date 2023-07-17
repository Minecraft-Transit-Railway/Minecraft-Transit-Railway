package mtr.block;

import mtr.data.IPIDS;
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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.BlockHitResult;

import java.util.List;

public abstract class BlockPIDSBaseVertical extends BlockDirectionalDoubleBlockBase implements EntityBlockMapper, IPIDS {

	public BlockPIDSBaseVertical() {
		super(Properties.of().requiresCorrectToolForDrops().strength(2).lightLevel(state -> 5));
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
		final boolean isUpper = IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER;
		return IBlock.checkHoldingBrush(world, player, () -> {
			final BlockPos finalPos = isUpper ? pos : pos.relative(Direction.Axis.Y, 1);
			final BlockEntity entity1 = world.getBlockEntity(finalPos);

			if (entity1 instanceof BlockPIDSBaseVertical.TileEntityBlockPIDSBaseVertical) {
				((BlockPIDSBaseVertical.TileEntityBlockPIDSBaseVertical) entity1).syncData();
				PacketTrainDataGuiServer.openPIDSConfigScreenS2C((ServerPlayer) player, finalPos, finalPos, ((BlockPIDSBaseVertical.TileEntityBlockPIDSBaseVertical) entity1).getMaxArrivals(), ((BlockPIDSBaseVertical.TileEntityBlockPIDSBaseVertical) entity1).getLinesPerArrival());
			}
		});
	}

	@Override
	public void appendHoverText(ItemStack stack, BlockGetter blockGetter, List<Component> tooltip, TooltipFlag tooltipFlag) {
		final BlockEntity blockEntity = createBlockEntity(new BlockPos(0, 0, 0), null);
		if (blockEntity instanceof TileEntityPIDS) {
			tooltip.add(Text.translatable("tooltip.mtr.arrivals", ((TileEntityPIDS) blockEntity).getMaxArrivals()).setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
		}
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, HALF);
	}

	public abstract static class TileEntityBlockPIDSBaseVertical extends TileEntityPIDS {

		public TileEntityBlockPIDSBaseVertical(BlockEntityType<?> type, BlockPos pos, BlockState state) {
			super(type, pos, state);
		}

		@Override
		public abstract int getLinesPerArrival();
	}
}
