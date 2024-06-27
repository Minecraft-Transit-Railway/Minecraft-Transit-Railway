package org.mtr.mod.entity;

import org.mtr.mapping.holder.EntityType;
import org.mtr.mapping.holder.MinecraftClient;
import org.mtr.mapping.holder.Vector3d;
import org.mtr.mapping.holder.World;
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
		final Vector3d position = MinecraftClient.getInstance().getGameRendererMapped().getCamera().getPos();
		if (skipDistanceCheck || position.squaredDistanceTo(getPos2()) > 1 || MinecraftClient.getInstance().isPaused()) {
			setPosition2(position.getXMapped(), position.getYMapped(), position.getZMapped());
		}
	}
}
