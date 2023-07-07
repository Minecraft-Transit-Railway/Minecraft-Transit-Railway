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
	private final Set<UUID> honkingPlayers = new HashSet<>();
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

	public void horn(ServerPlayer player, boolean pressingHorn) {
		if (pressingHorn) {
			honkingPlayers.add(player.getUUID());
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
			if (honkingPlayers.contains(ridingEntity) && trainServer.honk(true)) {
				dirty = true;
			} else if (notHonkingPlayers.contains(ridingEntity) && trainServer.honk(false)) {
				dirty = true;
			}
		}
		return dirty;
	}
}
