package org.mtr.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.mtr.data.TicketSystem;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.registry.SoundEvents;

import javax.annotation.Nonnull;

public class BlockTicketProcessorEnquiry extends BlockTicketProcessor {

	public BlockTicketProcessorEnquiry(AbstractBlock.Settings settings) {
		super(settings, false, false, false);
	}

	@Nonnull
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos blockPos, PlayerEntity player, BlockHitResult hit) {
		if (!world.isClient()) {
			final int playerScore = TicketSystem.getBalance(world, player);
			player.sendMessage(TranslationProvider.GUI_MTR_BALANCE.getText(String.valueOf(playerScore)), true);
			world.playSound(null, blockPos, SoundEvents.TICKET_PROCESSOR_ENTRY.get(), SoundCategory.BLOCKS, 1, 1);
		}
		return ActionResult.SUCCESS;
	}
}
