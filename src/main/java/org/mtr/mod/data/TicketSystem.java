package org.mtr.mod.data;

import org.mtr.core.data.Station;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.ScoreboardCriteria;
import org.mtr.mapping.mapper.TextHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TicketSystem {

	public static final String BALANCE_OBJECTIVE = "mtr_balance";
	public static final String BALANCE_OBJECTIVE_TITLE = "Balance";
	private static final String ENTRY_ZONE_OBJECTIVE = "mtr_entry_zone";
	private static final String ENTRY_ZONE_TITLE = "Entry Zone";
	private static final int BASE_FARE = 2;
	private static final int ZONE_FARE = 1;
	private static final int EVASION_FINE = 500;

	public static EnumTicketBarrierOpen passThrough(World world, BlockPos pos, PlayerEntity player, boolean isEntrance, boolean isExit, SoundEvent entrySound, SoundEvent entrySoundConcessionary, SoundEvent exitSound, SoundEvent exitSoundConcessionary, SoundEvent failSound, boolean remindIfNoRecord) {
		final Station station = null; // TODO
		if (station == null) {
			return EnumTicketBarrierOpen.CLOSED;
		}

		final ScoreboardPlayerScore balanceScore = getPlayerScore(world, player, BALANCE_OBJECTIVE, BALANCE_OBJECTIVE_TITLE);
		final ScoreboardPlayerScore entryZoneScore = getPlayerScore(world, player, ENTRY_ZONE_OBJECTIVE, ENTRY_ZONE_TITLE);
		if (balanceScore == null || entryZoneScore == null) {
			return EnumTicketBarrierOpen.CLOSED;
		}

		final boolean isEntering;
		if (isEntrance && isExit) {
			isEntering = entryZoneScore.getScore() == 0;
		} else {
			isEntering = isEntrance;
		}

		final boolean canOpen;
		if (isEntering) {
			canOpen = onEnter(station, player, balanceScore, entryZoneScore, remindIfNoRecord);
		} else {
			canOpen = onExit(station, player, balanceScore, entryZoneScore, remindIfNoRecord);
		}

		if (canOpen) {
			world.playSound((PlayerEntity) null, pos, isConcessionary(player) ? (isEntering ? entrySoundConcessionary : exitSoundConcessionary) : (isEntering ? entrySound : exitSound), SoundCategory.BLOCKS, 1, 1);
		} else if (failSound != null) {
			world.playSound((PlayerEntity) null, pos, failSound, SoundCategory.BLOCKS, 1, 1);
		}

		return canOpen ? isConcessionary(player) ? EnumTicketBarrierOpen.OPEN_CONCESSIONARY : EnumTicketBarrierOpen.OPEN : EnumTicketBarrierOpen.CLOSED;
	}

	public static int getBalance(World world, PlayerEntity player) {
		final ScoreboardPlayerScore scoreboardPlayerScore = getPlayerScore(world, player, BALANCE_OBJECTIVE, BALANCE_OBJECTIVE_TITLE);
		return scoreboardPlayerScore == null ? 0 : scoreboardPlayerScore.getScore();
	}

	public static void addBalance(World world, PlayerEntity player, int amount) {
		final ScoreboardPlayerScore scoreboardPlayerScore = getPlayerScore(world, player, BALANCE_OBJECTIVE, BALANCE_OBJECTIVE_TITLE);
		if (scoreboardPlayerScore != null) {
			scoreboardPlayerScore.setScore(scoreboardPlayerScore.getScore() + amount);
		}
	}

	@Nullable
	private static ScoreboardPlayerScore getPlayerScore(World world, PlayerEntity player, String objective, String title) {
		try {
			world.getScoreboard().addObjective(objective, ScoreboardCriteria.DUMMY, new Text(TextHelper.literal(title).data), ScoreboardCriterionRenderType.INTEGER);
		} catch (Exception ignored) {
		}
		final ScoreboardObjective scoreboardObjective = world.getScoreboard().getObjective(objective);
		return scoreboardObjective == null ? null : world.getScoreboard().getPlayerScore(player.getGameProfile().getName(), scoreboardObjective);
	}

	private static boolean onEnter(Station station, PlayerEntity player, ScoreboardPlayerScore balanceScore, ScoreboardPlayerScore entryZoneScore, boolean remindIfNoRecord) {
		final int entryZone = entryZoneScore.getScore();

		if (entryZone != 0) {
			if (remindIfNoRecord) {
				player.sendMessage(new Text(TextHelper.translatable("gui.mtr.already_entered").data), true);
				return false;
			} else {
				entryZoneScore.setScore(0);
				balanceScore.incrementScore(-EVASION_FINE);
			}
		}

		if (balanceScore.getScore() >= 0) {
			entryZoneScore.setScore(encodeZone((int) station.getZone1()));
			player.sendMessage(new Text(TextHelper.translatable("gui.mtr.enter_barrier", String.format("%s (%s)", station.getName().replace('|', ' '), station.getZone1()), balanceScore.getScore()).data), true);
			return true;
		} else {
			player.sendMessage(new Text(TextHelper.translatable("gui.mtr.insufficient_balance", balanceScore.getScore()).data), true);
			return false;
		}
	}

	private static boolean onExit(Station station, PlayerEntity player, ScoreboardPlayerScore balanceScore, ScoreboardPlayerScore entryZoneScore, boolean remindIfNoRecord) {
		final int entryZone = entryZoneScore.getScore();
		final long fare = BASE_FARE + ZONE_FARE * Math.abs(station.getZone1() - decodeZone(entryZone));
		final int finalFare = entryZone != 0 ? isConcessionary(player) ? (int) Math.ceil(fare / 2F) : (int) fare : EVASION_FINE;

		if (entryZone == 0 && remindIfNoRecord) {
			player.sendMessage(new Text(TextHelper.translatable("gui.mtr.already_exited").data), true);
			return false;
		} else {
			entryZoneScore.setScore(0);
			balanceScore.incrementScore(-finalFare);
			player.sendMessage(new Text(TextHelper.translatable("gui.mtr.exit_barrier", String.format("%s (%s)", station.getName().replace('|', ' '), station.getZone1()), finalFare, balanceScore.getScore()).data), true);
			return true;
		}
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

	public enum EnumTicketBarrierOpen implements StringIdentifiable {

		CLOSED("closed"), OPEN("open"), OPEN_CONCESSIONARY("open_concessionary");
		private final String name;

		EnumTicketBarrierOpen(String nameIn) {
			name = nameIn;
		}

		@Nonnull
		@Override
		public String asString2() {
			return name;
		}

		public boolean isOpen() {
			return this != CLOSED;
		}
	}
}
