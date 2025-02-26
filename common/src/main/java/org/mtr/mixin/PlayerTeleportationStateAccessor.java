package org.mtr.mixin;

import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ServerPlayerEntity.class)
public interface PlayerTeleportationStateAccessor {

	@Accessor("inTeleportationState")
	void setInTeleportationState(boolean inTeleportationState);
}
