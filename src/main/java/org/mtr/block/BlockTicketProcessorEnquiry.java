package org.mtr.block;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.mtr.data.TicketSystem;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.registry.SoundEvents;

public class BlockTicketProcessorEnquiry extends BlockTicketProcessor {

	public BlockTicketProcessorEnquiry(BlockBehaviour.Properties settings) {
		super(settings, false, false, false);
	}

	@Override
	public InteractionResult useWithoutItem(BlockState state, Level world, BlockPos blockPos, Player player, BlockHitResult hit) {
		if (!world.isClientSide()) {
			final int playerScore = TicketSystem.getBalance(world, player);
			player.displayClientMessage(TranslationProvider.GUI_MTR_BALANCE.getText(String.valueOf(playerScore)), true);
			world.playSound(null, blockPos, SoundEvents.TICKET_PROCESSOR_ENTRY.get(), SoundSource.BLOCKS, 1, 1);
		}
		return InteractionResult.SUCCESS;
	}
}
