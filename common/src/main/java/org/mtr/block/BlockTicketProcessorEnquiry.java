package org.mtr.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mod.SoundEvents;
import org.mtr.mod.data.TicketSystem;
import org.mtr.mod.generated.lang.TranslationProvider;

import javax.annotation.Nonnull;

public class BlockTicketProcessorEnquiry extends BlockTicketProcessor {

	public BlockTicketProcessorEnquiry() {
		super(false, false, false);
	}

	@Nonnull
	@Override
	public ActionResult onUse2(BlockState state, World world, BlockPos blockPos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (!world.isClient()) {
			final int playerScore = TicketSystem.getBalance(world, player);
			player.sendMessage(TranslationProvider.GUI_MTR_BALANCE.getText(String.valueOf(playerScore)), true);
			world.playSound(null, blockPos, SoundEvents.TICKET_PROCESSOR_ENTRY.get(), SoundCategory.BLOCKS, 1, 1);
		}
		return ActionResult.SUCCESS;
	}
}
