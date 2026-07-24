package org.mtr.mixin;

import net.minecraft.client.world.ClientChunkManager;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.LightType;
import org.mtr.mod.render.DefaultRailMeshCache;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientChunkManager.class)
public abstract class ClientChunkLightUpdateMixin {

	@Inject(method = "onLightUpdate", at = @At("TAIL"))
	private void invalidateDefaultRailLight(LightType lightType, ChunkSectionPos chunkSectionPos, CallbackInfo callbackInfo) {
		DefaultRailMeshCache.invalidateLightSection(chunkSectionPos.getSectionX(), chunkSectionPos.getSectionY(), chunkSectionPos.getSectionZ());
	}
}
