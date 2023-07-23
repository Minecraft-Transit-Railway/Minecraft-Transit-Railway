package mtr.data;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class RailwayDataDriveTrainModule extends RailwayDataModuleBase {

	private final Set<UUID> acceleratePlayers = new HashSet<>();
	private final Set<UUID> brakePlayers = new HashSet<>();
	private final Set<UUID> doorsPlayers = new HashSet<>();
	private final Set<UUID> primaryHornPlayers = new HashSet<>();
	private final Set<UUID> secondaryHornPlayers = new HashSet<>();
	private final Set<UUID> musicHornPlayers = new HashSet<>();

	public RailwayDataDriveTrainModule(RailwayData railwayData, Level world, Map<BlockPos, Map<BlockPos, Rail>> rails) {
		super(railwayData, world, rails);
	}

	public void tick() {
		acceleratePlayers.clear();
		brakePlayers.clear();
		doorsPlayers.clear();
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
			primaryHornPlayers.add(player.getUUID());
		} else {
			primaryHornPlayers.remove(player.getUUID());
		}
		if (pressingSecondaryHorn) {
			secondaryHornPlayers.add(player.getUUID());
		} else {
			secondaryHornPlayers.remove(player.getUUID());
		}
		if (pressingMusicHorn) {
			musicHornPlayers.add(player.getUUID());
		} else {
			musicHornPlayers.remove(player.getUUID());
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
			if (!primaryHornPlayers.isEmpty()) {
				trainServer.setHornStatus(0, true);
				dirty = true;
			} else if (trainServer.getHornStatus(0)) {
				trainServer.setHornStatus(0, false);
				dirty = true;
			}
			if (!secondaryHornPlayers.isEmpty()) {
				trainServer.setHornStatus(1, true);
				dirty = true;
			} else if (trainServer.getHornStatus(1)) {
				trainServer.setHornStatus(1, false);
				dirty = true;
			}
			if (!musicHornPlayers.isEmpty()) {
				trainServer.setHornStatus(2, true);
				dirty = true;
			} else if (trainServer.getHornStatus(2)) {
				trainServer.setHornStatus(2, false);
				dirty = true;
			}
		}
		return dirty;
	}
}
