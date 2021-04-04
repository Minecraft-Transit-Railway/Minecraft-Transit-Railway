package mtr.block;

import mtr.data.Station;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.scoreboard.ScoreboardPlayerScore;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.World;

public class BlockTicketBarrierExit extends BlockTicketBarrierBase {

	public BlockTicketBarrierExit(Settings settings) {
		super(settings);
	}

	@Override
	protected void onPassBarrier(World world, Station station, PlayerEntity player, ScoreboardPlayerScore balanceScore, ScoreboardPlayerScore entryZoneScore) {
		final int entryZone = entryZoneScore.getScore();
		final int fare = entryZone > 0 ? BASE_FARE + ZONE_FARE * Math.abs(station.zone + 1 - entryZone) : EVASION_FINE;
		final int finalFare = isConcessionary(player) ? (int) Math.ceil(fare / 2F) : fare;

		entryZoneScore.setScore(0);
		balanceScore.incrementScore(-finalFare);

		player.sendMessage(new TranslatableText("gui.mtr.exit_barrier", String.format("%s (%s)", station.name.replace('|', ' '), station.zone), finalFare, balanceScore.getScore()), true);
	}

	@Override
	protected boolean canOpen(int balance) {
		return true;
	}
}
