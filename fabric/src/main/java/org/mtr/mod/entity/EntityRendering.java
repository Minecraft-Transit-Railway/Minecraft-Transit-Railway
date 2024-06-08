package org.mtr.mod.entity;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.EntityExtension;
import org.mtr.mod.EntityTypes;

public class EntityRendering extends EntityExtension {

	public EntityRendering(EntityType<?> type, World world) {
		super(type, world);
	}

	public EntityRendering(World world) {
		super(EntityTypes.RENDERING.get(), world);
	}

	@Override
	protected void initDataTracker2() {
	}

	@Override
	public void tick2() {
		update(true);
	}

	public void update() {
		update(false);
	}

	private void update(boolean skipDistanceCheck) {
		final ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().getPlayerMapped();
		if (clientPlayerEntity != null) {
			final Vector3d playerPosition = clientPlayerEntity.getPos();
			if (skipDistanceCheck || playerPosition.squaredDistanceTo(getPos2()) > 1 || MinecraftClient.getInstance().isPaused()) {
				setPosition2(playerPosition.getXMapped(), playerPosition.getYMapped(), playerPosition.getZMapped());
			}
		}
	}
}
