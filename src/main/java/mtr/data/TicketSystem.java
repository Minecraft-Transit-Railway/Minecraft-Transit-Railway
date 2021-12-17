package mtr.data;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.scores.Score;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;

public class TicketSystem {

	public static final String BALANCE_OBJECTIVE = "mtr_balance";
	private static final String ENTRY_ZONE_OBJECTIVE = "mtr_entry_zone";
	private static final int BASE_FARE = 2;
	private static final int ZONE_FARE = 1;
	private static final int EVASION_FINE = 500;

	public static EnumTicketBarrierOpen passThrough(Level world, BlockPos pos, Player player, boolean isEntrance, boolean isExit, SoundEvent entrySound, SoundEvent entrySoundConcessionary, SoundEvent exitSound, SoundEvent exitSoundConcessionary, SoundEvent failSound, boolean remindIfNoRecord) {
		final RailwayData railwayData = RailwayData.getInstance(world);
		if (railwayData == null) {
			return EnumTicketBarrierOpen.CLOSED;
		}

		final Station station = RailwayData.getStation(railwayData.stations, pos);
		if (station == null) {
			return EnumTicketBarrierOpen.CLOSED;
		}

		addObjectivesIfMissing(world);

		final Score balanceScore = getPlayerScore(world, player, BALANCE_OBJECTIVE);
		final Score entryZoneScore = getPlayerScore(world, player, ENTRY_ZONE_OBJECTIVE);

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
			world.playSound(null, pos, isConcessionary(player) ? (isEntering ? entrySoundConcessionary : exitSoundConcessionary) : (isEntering ? entrySound : exitSound), SoundSource.BLOCKS, 1, 1);
		} else if (failSound != null) {
			world.playSound(null, pos, failSound, SoundSource.BLOCKS, 1, 1);
		}

		return canOpen ? isConcessionary(player) ? EnumTicketBarrierOpen.OPEN_CONCESSIONARY : EnumTicketBarrierOpen.OPEN : EnumTicketBarrierOpen.CLOSED;
	}

	public static void addObjectivesIfMissing(Level world) {
		try {
			world.getScoreboard().addObjective(BALANCE_OBJECTIVE, ObjectiveCriteria.DUMMY, new TextComponent("Balance"), ObjectiveCriteria.RenderType.INTEGER);
		} catch (Exception ignored) {
		}
		try {
			world.getScoreboard().addObjective(ENTRY_ZONE_OBJECTIVE, ObjectiveCriteria.DUMMY, new TextComponent("Entry Zone"), ObjectiveCriteria.RenderType.INTEGER);
		} catch (Exception ignored) {
		}
	}

	public static Score getPlayerScore(Level world, Player player, String objectiveName) {
		return world.getScoreboard().getOrCreatePlayerScore(player.getGameProfile().getName(), world.getScoreboard().getObjective(objectiveName));
	}

	private static boolean onEnter(Station station, Player player, Score balanceScore, Score entryZoneScore, boolean remindIfNoRecord) {
		final int entryZone = entryZoneScore.getScore();

		if (entryZone != 0) {
			if (remindIfNoRecord) {
				player.displayClientMessage(new TranslatableComponent("gui.mtr.already_entered"), true);
				return false;
			} else {
				entryZoneScore.setScore(0);
				balanceScore.add(-EVASION_FINE);
			}
		}

		if (balanceScore.getScore() >= 0) {
			entryZoneScore.setScore(encodeZone(station.zone));
			player.displayClientMessage(new TranslatableComponent("gui.mtr.enter_barrier", String.format("%s (%s)", station.name.replace('|', ' '), station.zone), balanceScore.getScore()), true);
			return true;
		} else {
			player.displayClientMessage(new TranslatableComponent("gui.mtr.insufficient_balance", balanceScore.getScore()), true);
			return false;
		}
	}

	private static boolean onExit(Station station, Player player, Score balanceScore, Score entryZoneScore, boolean remindIfNoRecord) {
		final int entryZone = entryZoneScore.getScore();
		final int fare = BASE_FARE + ZONE_FARE * Math.abs(station.zone - decodeZone(entryZone));
		final int finalFare = entryZone != 0 ? isConcessionary(player) ? (int) Math.ceil(fare / 2F) : fare : EVASION_FINE;

		if (entryZone == 0 && remindIfNoRecord) {
			player.displayClientMessage(new TranslatableComponent("gui.mtr.already_exited"), true);
			return false;
		} else {
			entryZoneScore.setScore(0);
			balanceScore.add(-finalFare);
			player.displayClientMessage(new TranslatableComponent("gui.mtr.exit_barrier", String.format("%s (%s)", station.name.replace('|', ' '), station.zone), finalFare, balanceScore.getScore()), true);
			return true;
		}
	}

	private static boolean isConcessionary(Player player) {
		return player.isCreative();
	}

	private static int encodeZone(int zone) {
		return zone >= 0 ? zone + 1 : zone;
	}

	private static int decodeZone(int zone) {
		return zone > 0 ? zone - 1 : zone;
	}

	public enum EnumTicketBarrierOpen implements StringRepresentable {

		CLOSED("closed"), OPEN("open"), OPEN_CONCESSIONARY("open_concessionary");
		private final String name;

		EnumTicketBarrierOpen(String nameIn) {
			name = nameIn;
		}

		@Override
		public String getSerializedName() {
			return name;
		}

		public boolean isOpen() {
			return this != CLOSED;
		}
	}
}
