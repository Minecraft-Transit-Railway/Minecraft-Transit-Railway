package org.mtr.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.mtr.data.TicketSystem;
import org.mtr.packet.PacketOpenTicketMachineScreen;
import org.mtr.registry.Registry;

import javax.annotation.Nonnull;

public class BlockTicketMachine extends BlockDirectionalDoubleBlockBase {

	public BlockTicketMachine(AbstractBlock.Settings settings) {
		super(settings.luminance(blockState -> 5));
	}

	@Nonnull
	@Override
	protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
		if (!world.isClient()) {
			Registry.sendPacketToClient((ServerPlayerEntity) player, new PacketOpenTicketMachineScreen(TicketSystem.getBalance(world, player)));
		}
		return ActionResult.SUCCESS;
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		final Direction facing = IBlock.getStatePropertySafe(state, Properties.FACING);
		final int height = IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER ? 14 : 16;
		return IBlock.getVoxelShapeByDirection(0, 0, 2, 16, height, 14, facing);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(Properties.FACING);
		builder.add(HALF);
	}
}
