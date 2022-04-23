package mtr.mixin;

import mtr.client.ClientData;
import mtr.render.RenderTrains;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerRenderer.class)
public class PlayerRendererOffsetMixin {

	@Inject(method = "getRenderOffset(Lnet/minecraft/client/player/AbstractClientPlayer;F)Lnet/minecraft/world/phys/Vec3;", at = @At(value = "RETURN"), cancellable = true)
	public void getRenderOffset(AbstractClientPlayer abstractClientPlayer, float f, CallbackInfoReturnable<Vec3> callbackInfoReturnable) {
		if (ClientData.isRiding(abstractClientPlayer.getUUID())) {
			callbackInfoReturnable.setReturnValue(new Vec3(0, -RenderTrains.PLAYER_RENDER_OFFSET, 0));
		}
	}
}
