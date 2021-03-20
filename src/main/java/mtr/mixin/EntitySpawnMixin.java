package mtr.mixin;

import mtr.MTR;
import mtr.entity.EntitySeat;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class EntitySpawnMixin {

	@Shadow
	private ClientWorld world;

	@Inject(method = "onEntitySpawn", at = @At("RETURN"))
	private void injectMethod(EntitySpawnS2CPacket packet, CallbackInfo info) {
		final double x = packet.getX();
		final double y = packet.getY();
		final double z = packet.getZ();
		final EntityType<?> entityType = packet.getEntityTypeId();

		final Entity entity;
		if (entityType == MTR.SEAT) {
			entity = new EntitySeat(world, x, y, z);
		} else {
			entity = null;
		}

		if (entity != null) {
			final int id = packet.getId();
			entity.updateTrackedPosition(x, y, z);
			entity.refreshPositionAfterTeleport(x, y, z);
			entity.setEntityId(id);
			entity.setUuid(packet.getUuid());
			world.addEntity(id, entity);
		}
	}
}
