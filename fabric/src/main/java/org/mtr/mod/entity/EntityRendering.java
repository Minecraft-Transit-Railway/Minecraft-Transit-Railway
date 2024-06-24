package org.mtr.mod.entity;

import org.mtr.mapping.holder.EntityType;
import org.mtr.mapping.holder.MinecraftClient;
import org.mtr.mapping.holder.Vector3d;
import org.mtr.mapping.holder.World;
import org.mtr.mapping.mapper.EntityExtension;
import org.mtr.mod.EntityTypes;

public class EntityRendering extends EntityExtension {

	public final Vector3d offset;

	public EntityRendering(EntityType<?> type, World world) {
		super(type, world);
		offset = Vector3d.getZeroMapped();
	}

	public EntityRendering(World world) {
		super(EntityTypes.RENDERING.get(), world);
		offset = MinecraftClient.getInstance().getGameRendererMapped().getCamera().getPos();
		setPosition2(offset.getXMapped(), offset.getYMapped(), offset.getZMapped());
	}

	@Override
	protected void initDataTracker2() {
	}
}
