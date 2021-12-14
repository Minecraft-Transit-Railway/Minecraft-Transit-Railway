package mtr.block;

import mtr.MTR;
import mtr.data.TicketSystem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class BlockTicketProcessorExit extends BlockTicketProcessorBase {
	public boolean entrance;
	public boolean exit;
	public boolean remindIfNoRecord;

	public BlockTicketProcessorExit(boolean Entrance, boolean Exit, boolean remindNoRecord) {
		entrance = Entrance;
		exit = Exit;
		remindIfNoRecord = remindNoRecord;
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (!world.isClient && IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER) {
			final TicketSystem.EnumTicketBarrierOpen open = TicketSystem.passThrough(world, pos, player, entrance, exit, MTR.TICKET_PROCESSOR_ENTRY, MTR.TICKET_PROCESSOR_ENTRY_CONCESSIONARY, MTR.TICKET_PROCESSOR_EXIT, MTR.TICKET_PROCESSOR_EXIT_CONCESSIONARY, MTR.TICKET_PROCESSOR_FAIL, remindIfNoRecord);
			world.setBlockState(pos, state.with(LIGHTS, open.isOpen() ? EnumTicketProcessorLights.GREEN : EnumTicketProcessorLights.RED));
			world.getBlockTickScheduler().schedule(pos, this, 20);
		}
		return ActionResult.SUCCESS;
	}
}