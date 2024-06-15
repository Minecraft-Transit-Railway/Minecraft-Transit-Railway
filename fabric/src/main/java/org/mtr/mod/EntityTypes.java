package org.mtr.mod;

import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.registry.EntityTypeRegistryObject;
import org.mtr.mod.entity.EntityRendering;

public final class EntityTypes {

	static {
		RENDERING = Init.REGISTRY.registerEntityType(new Identifier(Init.MOD_ID, "rendering"), EntityRendering::new, Float.MIN_VALUE, Float.MIN_VALUE);
	}

	public static final EntityTypeRegistryObject<EntityRendering> RENDERING;

	public static void init() {
		Init.LOGGER.info("Registering Minecraft Transit Railway entity types");
	}
}
