package mtr.data;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

import java.util.*;

public class RailwayDataDriveTrainModule extends RailwayDataModuleBase {

	private final Set<UUID> acceleratePlayers = new HashSet<>();
	private final Set<UUID> brakePlayers = new HashSet<>();
	private final Set<UUID> doorsPlayers = new HashSet<>();
	private final Map<UUID, Integer> honkingPlayers = new HashMap<>();
	private final Set<UUID> notHonkingPlayers = new HashSet<>();

	public RailwayDataDriveTrainModule(RailwayData railwayData, Level world, Map<BlockPos, Map<BlockPos, Rail>> rails) {
		super(railwayData, world, rails);
	}

	public void tick() {
		acceleratePlayers.clear();
		brakePlayers.clear();
		doorsPlayers.clear();
		honkingPlayers.clear();
		notHonkingPlayers.clear();
	}

	public void drive(ServerPlayer player, boolean pressingAccelerate, boolean pressingBrake, boolean pressingDoors) {
		if (pressingAccelerate) {
			acceleratePlayers.add(player.getUUID());
		}
		if (pressingBrake) {
			brakePlayers.add(player.getUUID());
		}
		if (pressingDoors) {
			doorsPlayers.add(player.getUUID());
		}
	}

	public void honk(ServerPlayer player, boolean pressingPrimaryHorn, boolean pressingSecondaryHorn, boolean pressingMusicHorn) {
		if (pressingPrimaryHorn) {
			honkingPlayers.put(player.getUUID(), 0);
		} else if (pressingSecondaryHorn) {
			honkingPlayers.put(player.getUUID(), 1);
		} else if (pressingMusicHorn) {
			honkingPlayers.put(player.getUUID(), 2);
		} else {
			notHonkingPlayers.add(player.getUUID());
			honkingPlayers.remove(player.getUUID());
		}
	}

	public boolean drive(TrainServer trainServer) {
		boolean dirty = false;
		for (final UUID ridingEntity : trainServer.ridingEntities) {
			if (acceleratePlayers.contains(ridingEntity) && trainServer.changeManualSpeed(true)) {
				dirty = true;
			} else if (brakePlayers.contains(ridingEntity) && trainServer.changeManualSpeed(false)) {
				dirty = true;
			}
			if (doorsPlayers.contains(ridingEntity) && trainServer.toggleDoors()) {
				dirty = true;
			}
			if (honkingPlayers.containsKey(ridingEntity) && trainServer.honk(true, honkingPlayers.get(ridingEntity))) {
				dirty = true;
			} else if (notHonkingPlayers.contains(ridingEntity) && trainServer.honk(false, -1)) {
				dirty = true;
			}
		}
		return dirty;
	}
}
