package mtr.mixin;

import mtr.EntityTypes;
import mtr.entity.EntitySeat;
import mtr.mappings.Utilities;
import mtr.mappings.UtilitiesClient;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public abstract class EntitySpawnMixin {

	@Shadow
	private ClientLevel level;

	@Inject(method = "handleAddEntity", at = @At("RETURN"))
	private void injectMethod(ClientboundAddEntityPacket packet, CallbackInfo info) {
		final double x = packet.getX();
		final double y = packet.getY();
		final double z = packet.getZ();
		final EntityType<?> entityType = packet.getType();

		final Entity entity;
		if (entityType == EntityTypes.SEAT.get()) {
			entity = new EntitySeat(level, x, y, z);
		} else if (entityType == EntityTypes.LiftType.SIZE_2_2.registryObject.get()) {
			entity = EntityTypes.LiftType.SIZE_2_2.liftSupplier.liftSupplier(level, x, y, z);
		} else if (entityType == EntityTypes.LiftType.SIZE_2_2_DOUBLE_SIDED.registryObject.get()) {
			entity = EntityTypes.LiftType.SIZE_2_2_DOUBLE_SIDED.liftSupplier.liftSupplier(level, x, y, z);
		} else if (entityType == EntityTypes.LiftType.SIZE_3_2.registryObject.get()) {
			entity = EntityTypes.LiftType.SIZE_3_2.liftSupplier.liftSupplier(level, x, y, z);
		} else if (entityType == EntityTypes.LiftType.SIZE_3_2_DOUBLE_SIDED.registryObject.get()) {
			entity = EntityTypes.LiftType.SIZE_3_2_DOUBLE_SIDED.liftSupplier.liftSupplier(level, x, y, z);
		} else if (entityType == EntityTypes.LiftType.SIZE_3_3.registryObject.get()) {
			entity = EntityTypes.LiftType.SIZE_3_3.liftSupplier.liftSupplier(level, x, y, z);
		} else if (entityType == EntityTypes.LiftType.SIZE_3_3_DOUBLE_SIDED.registryObject.get()) {
			entity = EntityTypes.LiftType.SIZE_3_3_DOUBLE_SIDED.liftSupplier.liftSupplier(level, x, y, z);
		} else if (entityType == EntityTypes.LiftType.SIZE_4_3.registryObject.get()) {
			entity = EntityTypes.LiftType.SIZE_4_3.liftSupplier.liftSupplier(level, x, y, z);
		} else if (entityType == EntityTypes.LiftType.SIZE_4_3_DOUBLE_SIDED.registryObject.get()) {
			entity = EntityTypes.LiftType.SIZE_4_3_DOUBLE_SIDED.liftSupplier.liftSupplier(level, x, y, z);
		} else if (entityType == EntityTypes.LiftType.SIZE_4_4.registryObject.get()) {
			entity = EntityTypes.LiftType.SIZE_4_4.liftSupplier.liftSupplier(level, x, y, z);
		} else if (entityType == EntityTypes.LiftType.SIZE_4_4_DOUBLE_SIDED.registryObject.get()) {
			entity = EntityTypes.LiftType.SIZE_4_4_DOUBLE_SIDED.liftSupplier.liftSupplier(level, x, y, z);
		} else {
			entity = null;
		}

		if (entity != null) {
			final int id = packet.getId();
			UtilitiesClient.setPacketCoordinates(entity, x, y, z);
			entity.moveTo(x, y, z);
			entity.setId(id);
			entity.setUUID(packet.getUUID());
			Utilities.setYaw(entity, UtilitiesClient.getPacketYaw(packet));
			level.putNonPlayerEntity(id, entity);
		}
	}
}
