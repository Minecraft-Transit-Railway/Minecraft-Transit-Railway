package org.mtr.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.Init;
import org.mtr.mod.data.TicketSystem;
import org.mtr.mod.packet.PacketOpenTicketMachineScreen;

import javax.annotation.Nonnull;
import java.util.List;

public class BlockTicketMachine extends BlockDirectionalDoubleBlockBase {

	public BlockTicketMachine(BlockSettings blockSettings) {
		super(blockSettings);
	}

	@Nonnull
	@Override
	public ActionResult onUse2(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (!world.isClient()) {
			Init.REGISTRY.sendPacketToClient(ServerPlayerEntity.cast(player), new PacketOpenTicketMachineScreen(TicketSystem.getBalance(world, player)));
		}
		return ActionResult.SUCCESS;
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		final Direction facing = IBlock.getStatePropertySafe(state, FACING);
		final int height = IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER ? 14 : 16;
		return IBlock.getVoxelShapeByDirection(0, 0, 2, 16, height, 14, facing);
	}

	@Override
	public void addBlockProperties(List<HolderBase<?>> properties) {
		properties.add(FACING);
		properties.add(HALF);
	}
}
