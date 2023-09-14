package org.mtr.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mod.SoundEvents;
import org.mtr.mod.data.TicketSystem;

import javax.annotation.Nonnull;

public class BlockTicketProcessorEnquiry extends BlockTicketProcessor {

	public BlockTicketProcessorEnquiry() {
		super(false, false, false);
	}

	@Nonnull
	@Override
	public ActionResult onUse2(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (!world.isClient()) {
			final int playerScore = TicketSystem.getBalance(world, player);
			player.sendMessage(new Text(TextHelper.translatable("gui.mtr.balance", String.valueOf(playerScore)).data), true);
			world.playSound((PlayerEntity) null, pos, SoundEvents.TICKET_PROCESSOR_ENTRY.get(), SoundCategory.BLOCKS, 1, 1);
		}
		return ActionResult.SUCCESS;
	}
}
