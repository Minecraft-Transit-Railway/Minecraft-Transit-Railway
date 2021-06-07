package mtr.block;

import mtr.MTR;
import mtr.data.TicketSystem;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockTicketProcessor extends BlockTicketProcessorBase {

	public boolean isEntrance;
	public boolean isExit;
	public boolean remindIfNoRecord;
	public BlockTicketProcessor(boolean entrance, boolean exit, boolean remindNoRecord) {
		isEntrance = entrance;
		isExit = exit;
		remindIfNoRecord = remindNoRecord;
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (!world.isClient && IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER) {
			final TicketSystem.EnumTicketBarrierOpen open = TicketSystem.passThrough(world, pos, player, isEntrance, isExit, MTR.TICKET_PROCESSOR_ENTRY, MTR.TICKET_PROCESSOR_ENTRY_CONCESSIONARY, MTR.TICKET_PROCESSOR_EXIT, MTR.TICKET_PROCESSOR_EXIT_CONCESSIONARY, MTR.TICKET_PROCESSOR_FAIL, remindIfNoRecord);
			world.setBlockState(pos, state.with(LIGHTS, open.isOpen() ? EnumTicketProcessorLights.GREEN : EnumTicketProcessorLights.RED));
			world.getBlockTickScheduler().schedule(pos, this, 20);
		}
		return ActionResult.SUCCESS;
	}
}