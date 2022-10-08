package mtr.data;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.msgpack.value.Value;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LiftServer extends Lift {

	private static final int LIFT_UPDATE_DISTANCE = 64;

	public LiftServer(BlockPos pos, Direction facing) {
		super(pos, facing);
	}

	public LiftServer(Map<String, Value> map) {
		super(map);
	}

	public void tickServer(Level world, Map<Player, Set<LiftServer>> liftsInPlayerRange, Set<LiftServer> liftsToSync) {
		world.players().forEach(player -> {
			if (player.blockPosition().distManhattan(new Vec3i(currentPositionX, currentPositionY, currentPositionZ)) < LIFT_UPDATE_DISTANCE) {
				if (!liftsInPlayerRange.containsKey(player)) {
					liftsInPlayerRange.put(player, new HashSet<>());
				}
				liftsInPlayerRange.get(player).add(this);
			}
		});

		tick(world, 1);

		final int ridingEntitiesCount = ridingEntities.size();
		VehicleRidingServer.mountRider(world, ridingEntities, id, 1, currentPositionX + liftOffsetX, currentPositionY + liftOffsetY, currentPositionZ + liftOffsetZ, liftWidth - 1, liftDepth - 1, getYaw(), 0, doorOpen, true, 0, PACKET_UPDATE_LIFT_PASSENGERS, player -> true, player -> {
		});

		if (liftInstructions.isDirty() || ridingEntitiesCount != ridingEntities.size()) {
			liftsToSync.add(this);
		}
	}
}
