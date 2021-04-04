package mtr.block;

import mtr.data.Station;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.scoreboard.ScoreboardPlayerScore;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.World;

public class BlockTicketBarrierEntrance extends BlockTicketBarrierBase {

	public BlockTicketBarrierEntrance(Settings settings) {
		super(settings);
	}

	@Override
	protected void onPassBarrier(World world, Station station, PlayerEntity player, ScoreboardPlayerScore balanceScore, ScoreboardPlayerScore entryZoneScore) {
		final int entryZone = entryZoneScore.getScore();

		if (entryZone > 0) {
			entryZoneScore.setScore(0);
			balanceScore.incrementScore(-EVASION_FINE);
		}

		if (canOpen(balanceScore.getScore())) {
			entryZoneScore.setScore(station.zone + 1);
			player.sendMessage(new TranslatableText("gui.mtr.enter_barrier", String.format("%s (%s)", station.name.replace('|', ' '), station.zone), balanceScore.getScore()), true);
		} else {
			player.sendMessage(new TranslatableText("gui.mtr.insufficient_balance", balanceScore.getScore()), true);
		}
	}

	@Override
	protected boolean canOpen(int balance) {
		return balance >= 0;
	}
}
