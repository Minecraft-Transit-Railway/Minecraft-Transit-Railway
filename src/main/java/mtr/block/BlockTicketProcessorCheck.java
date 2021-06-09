package mtr.block;

import mtr.MTR;
import mtr.data.TicketSystem;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class BlockTicketProcessorCheck extends BlockTicketProcessorBase {

	public static final String BALANCE_OBJECTIVE = "mtr_balance";

	public BlockTicketProcessorCheck(boolean haveLight) {
		super(haveLight);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		int playerScore = TicketSystem.getPlayerScore(world, player, BALANCE_OBJECTIVE).getScore();

		player.sendMessage(new TranslatableText("gui.mtr.balance", String.valueOf(playerScore)), true);
		world.playSound(null, pos, MTR.TICKET_PROCESSOR_ENTRY, SoundCategory.BLOCKS, 1, 1);
		return ActionResult.SUCCESS;
	}
}