package org.mtr.mod.data;

import org.mtr.core.data.Station;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.ScoreboardCriteria;
import org.mtr.mapping.mapper.ScoreboardHelper;
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

		final boolean isEntering;
		if (isEntrance && isExit) {
			isEntering = getPlayerScore(world, player, ENTRY_ZONE_OBJECTIVE, ENTRY_ZONE_TITLE) == 0;
		} else {
			isEntering = isEntrance;
		}

		final boolean canOpen;
		if (isEntering) {
			canOpen = onEnter(world, station, player, remindIfNoRecord);
		} else {
			canOpen = onExit(world, station, player, remindIfNoRecord);
		}

		if (canOpen) {
			world.playSound((PlayerEntity) null, pos, isConcessionary(player) ? (isEntering ? entrySoundConcessionary : exitSoundConcessionary) : (isEntering ? entrySound : exitSound), SoundCategory.BLOCKS, 1, 1);
		} else if (failSound != null) {
			world.playSound((PlayerEntity) null, pos, failSound, SoundCategory.BLOCKS, 1, 1);
		}

		return canOpen ? isConcessionary(player) ? EnumTicketBarrierOpen.OPEN_CONCESSIONARY : EnumTicketBarrierOpen.OPEN : EnumTicketBarrierOpen.CLOSED;
	}

	public static int getBalance(World world, PlayerEntity player) {
		return getPlayerScore(world, player, BALANCE_OBJECTIVE, BALANCE_OBJECTIVE_TITLE);
	}

	public static void addBalance(World world, PlayerEntity player, int amount) {
		incrementPlayerScore(world, player, BALANCE_OBJECTIVE, BALANCE_OBJECTIVE_TITLE, amount);
	}

	private static int getPlayerScore(World world, PlayerEntity player, String objective, String title) {
		final ScoreboardObjective scoreboardObjective = tryAddObjective(world, objective, title);
		return scoreboardObjective == null ? 0 : ScoreboardHelper.getPlayerScore(world.getScoreboard(), player.getGameProfile().getName(), scoreboardObjective);
	}

	private static void setPlayerScore(World world, PlayerEntity player, String objective, String title, int value) {
		final ScoreboardObjective scoreboardObjective = tryAddObjective(world, objective, title);
		if (scoreboardObjective != null) {
			ScoreboardHelper.setPlayerScore(world.getScoreboard(), player.getGameProfile().getName(), scoreboardObjective, value);
		}
	}

	private static void incrementPlayerScore(World world, PlayerEntity player, String objective, String title, int value) {
		final ScoreboardObjective scoreboardObjective = tryAddObjective(world, objective, title);
		if (scoreboardObjective != null) {
			ScoreboardHelper.incrementPlayerScore(world.getScoreboard(), player.getGameProfile().getName(), scoreboardObjective, value);
		}
	}

	@Nullable
	private static ScoreboardObjective tryAddObjective(World world, String objective, String title) {
		try {
			return ScoreboardHelper.addObjective(world.getScoreboard(), objective, ScoreboardCriteria.DUMMY, new Text(TextHelper.literal(title).data), ScoreboardCriterionRenderType.INTEGER);
		} catch (Exception ignored) {
		}
		return null;
	}

	private static boolean onEnter(World world, Station station, PlayerEntity player, boolean remindIfNoRecord) {
		final int entryZone = getPlayerScore(world, player, ENTRY_ZONE_OBJECTIVE, ENTRY_ZONE_TITLE);

		if (entryZone != 0) {
			if (remindIfNoRecord) {
				player.sendMessage(new Text(TextHelper.translatable("gui.mtr.already_entered").data), true);
				return false;
			} else {
				setPlayerScore(world, player, ENTRY_ZONE_OBJECTIVE, ENTRY_ZONE_TITLE, 0);
				incrementPlayerScore(world, player, BALANCE_OBJECTIVE, BALANCE_OBJECTIVE_TITLE, -EVASION_FINE);
			}
		}

		final int balance = getPlayerScore(world, player, BALANCE_OBJECTIVE, BALANCE_OBJECTIVE_TITLE);

		if (balance >= 0) {
			setPlayerScore(world, player, ENTRY_ZONE_OBJECTIVE, ENTRY_ZONE_TITLE, encodeZone((int) station.getZone1()));
			player.sendMessage(new Text(TextHelper.translatable("gui.mtr.enter_barrier", String.format("%s (%s)", station.getName().replace('|', ' '), station.getZone1()), balance).data), true);
			return true;
		} else {
			player.sendMessage(new Text(TextHelper.translatable("gui.mtr.insufficient_balance", balance).data), true);
			return false;
		}
	}

	private static boolean onExit(World world, Station station, PlayerEntity player, boolean remindIfNoRecord) {
		final int entryZone = getPlayerScore(world, player, ENTRY_ZONE_OBJECTIVE, ENTRY_ZONE_TITLE);
		final long fare = BASE_FARE + ZONE_FARE * Math.abs(station.getZone1() - decodeZone(entryZone));
		final int finalFare = entryZone != 0 ? isConcessionary(player) ? (int) Math.ceil(fare / 2F) : (int) fare : EVASION_FINE;

		if (entryZone == 0 && remindIfNoRecord) {
			player.sendMessage(new Text(TextHelper.translatable("gui.mtr.already_exited").data), true);
			return false;
		} else {
			setPlayerScore(world, player, ENTRY_ZONE_OBJECTIVE, ENTRY_ZONE_TITLE, 0);
			incrementPlayerScore(world, player, BALANCE_OBJECTIVE, BALANCE_OBJECTIVE_TITLE, -finalFare);
			final int balance = getPlayerScore(world, player, BALANCE_OBJECTIVE, BALANCE_OBJECTIVE_TITLE);
			player.sendMessage(new Text(TextHelper.translatable("gui.mtr.exit_barrier", String.format("%s (%s)", station.getName().replace('|', ' '), station.getZone1()), finalFare, balance).data), true);
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
