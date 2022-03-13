package mtr.data;

import mtr.Registry;
import mtr.entity.EntitySeat;
import mtr.mappings.Utilities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RailwayDataCoolDownModule extends RailwayDataModuleBase {

	private final Map<Player, Integer> playerRidingCoolDown = new HashMap<>();
	private final Map<Player, EntitySeat> playerSeats = new HashMap<>();
	private final Map<Player, Integer> playerSeatCoolDowns = new HashMap<>();

	public RailwayDataCoolDownModule(RailwayData railwayData, Level world, Map<BlockPos, Map<BlockPos, Rail>> rails) {
		super(railwayData, world, rails);
	}

	public void tick() {
		world.players().forEach(player -> {
			final Integer seatCoolDownOld = playerSeatCoolDowns.get(player);
			final EntitySeat seatOld = playerSeats.get(player);
			final EntitySeat seat;
			if (seatCoolDownOld == null || seatCoolDownOld <= 0 || Utilities.entityRemoved(seatOld)) {
				seat = new EntitySeat(world, player.getX(), player.getY(), player.getZ());
				world.addFreshEntity(seat);
				seat.initialize(player);
				playerSeats.put(player, seat);
				playerSeatCoolDowns.put(player, 3);
			} else {
				seat = playerSeats.get(player);
				playerSeatCoolDowns.put(player, playerSeatCoolDowns.get(player) - 1);
			}
			seat.updateSeatByRailwayData(player);
		});

		final Set<Player> playersToRemove = new HashSet<>();
		playerRidingCoolDown.forEach((player, coolDown) -> {
			if (coolDown <= 0) {
				updatePlayerRiding(player, false);
				playersToRemove.add(player);
			}
			playerRidingCoolDown.put(player, coolDown - 1);
		});
		playersToRemove.forEach(playerRidingCoolDown::remove);
	}

	public void onPlayerJoin(ServerPlayer serverPlayer) {
		playerRidingCoolDown.put(serverPlayer, 2);
	}

	public void onPlayerDisconnect(Player player) {
		playerSeats.remove(player);
		playerSeatCoolDowns.remove(player);
	}

	public void updatePlayerRiding(Player player) {
		updatePlayerRiding(player, true);
		playerRidingCoolDown.put(player, 2);
	}

	public void updatePlayerSeatCoolDown(Player player) {
		playerSeatCoolDowns.put(player, 3);
	}

	private static void updatePlayerRiding(Player player, boolean isRiding) {
		player.fallDistance = 0;
		player.setNoGravity(isRiding);
		player.noPhysics = isRiding;
		if (isRiding) {
			Utilities.getAbilities(player).mayfly = true;
		} else {
			((ServerPlayer) player).gameMode.getGameModeForPlayer().updatePlayerAbilities(Utilities.getAbilities(player));
		}
		Registry.setInTeleportationState(player, isRiding);
	}
}
