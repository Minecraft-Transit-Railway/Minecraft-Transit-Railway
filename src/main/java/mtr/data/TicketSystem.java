package mtr.data;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.scoreboard.ScoreboardPlayerScore;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TicketSystem {

	public static final String BALANCE_OBJECTIVE = "mtr_balance";
	private static final String ENTRY_ZONE_OBJECTIVE = "mtr_entry_zone";
	private static final int BASE_FARE = 2;
	private static final int ZONE_FARE = 1;
	private static final int EVASION_FINE = 500;

	public static boolean passThrough(World world, BlockPos pos, PlayerEntity player, boolean isEntrance, boolean isExit, SoundEvent sound, SoundEvent concessionarySound) {
		final RailwayData railwayData = RailwayData.getInstance(world);
		if (railwayData == null) {
			return false;
		}

		final Station station = railwayData.getStations().stream().filter(station1 -> station1.inStation(pos.getX(), pos.getZ())).findFirst().orElse(null);
		if (station == null) {
			return false;
		}

		addObjectivesIfMissing(world);

		final ScoreboardPlayerScore balanceScore = getPlayerScore(world, player, BALANCE_OBJECTIVE);
		final ScoreboardPlayerScore entryZoneScore = getPlayerScore(world, player, ENTRY_ZONE_OBJECTIVE);

		final boolean canOpen;
		if (isEntrance && isExit) {
			if (entryZoneScore.getScore() != 0) {
				onExit(station, player, balanceScore, entryZoneScore);
				canOpen = true;
			} else {
				canOpen = onEnter(station, player, balanceScore, entryZoneScore);
			}
		} else if (isEntrance) {
			canOpen = onEnter(station, player, balanceScore, entryZoneScore);
		} else {
			onExit(station, player, balanceScore, entryZoneScore);
			canOpen = true;
		}

		if (canOpen) {
			world.playSound(null, pos, isConcessionary(player) ? concessionarySound : sound, SoundCategory.BLOCKS, 1, 1);
		}
		return canOpen;
	}

	public static void addObjectivesIfMissing(World world) {
		try {
			world.getScoreboard().addObjective(BALANCE_OBJECTIVE, ScoreboardCriterion.DUMMY, new LiteralText("Balance"), ScoreboardCriterion.RenderType.INTEGER);
		} catch (Exception ignored) {
		}
		try {
			world.getScoreboard().addObjective(ENTRY_ZONE_OBJECTIVE, ScoreboardCriterion.DUMMY, new LiteralText("Entry Zone"), ScoreboardCriterion.RenderType.INTEGER);
		} catch (Exception ignored) {
		}
	}

	public static ScoreboardPlayerScore getPlayerScore(World world, PlayerEntity player, String objectiveName) {
		return world.getScoreboard().getPlayerScore(player.getGameProfile().getName(), world.getScoreboard().getObjective(objectiveName));
	}

	private static boolean onEnter(Station station, PlayerEntity player, ScoreboardPlayerScore balanceScore, ScoreboardPlayerScore entryZoneScore) {
		final int entryZone = entryZoneScore.getScore();

		if (entryZone != 0) {
			entryZoneScore.setScore(0);
			balanceScore.incrementScore(-EVASION_FINE);
		}

		if (balanceScore.getScore() >= 0) {
			entryZoneScore.setScore(encodeZone(station.zone));
			player.sendMessage(new TranslatableText("gui.mtr.enter_barrier", String.format("%s (%s)", station.name.replace('|', ' '), station.zone), balanceScore.getScore()), true);
			return true;
		} else {
			player.sendMessage(new TranslatableText("gui.mtr.insufficient_balance", balanceScore.getScore()), true);
			return false;
		}
	}

	private static void onExit(Station station, PlayerEntity player, ScoreboardPlayerScore balanceScore, ScoreboardPlayerScore entryZoneScore) {
		final int entryZone = entryZoneScore.getScore();
		final int fare = BASE_FARE + ZONE_FARE * Math.abs(station.zone - decodeZone(entryZone));
		final int finalFare = entryZone != 0 ? isConcessionary(player) ? (int) Math.ceil(fare / 2F) : fare : EVASION_FINE;

		entryZoneScore.setScore(0);
		balanceScore.incrementScore(-finalFare);

		player.sendMessage(new TranslatableText("gui.mtr.exit_barrier", String.format("%s (%s)", station.name.replace('|', ' '), station.zone), finalFare, balanceScore.getScore()), true);
	}

	private static boolean isConcessionary(PlayerEntity player) {
		return player.isCreative();
	}

	private static int encodeZone(int zone) {
		return zone >= 0 ? zone + 1 : zone;
	}

	private static int decodeZone(int zone) {
		return zone > 0 ? zone - 1 : zone;
	}
}
