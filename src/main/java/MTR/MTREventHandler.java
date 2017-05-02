package MTR;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MTREventHandler {

	@SubscribeEvent
	public void entityMount(EntityMountEvent event) {
		if (event.entityBeingMounted instanceof EntityTrainBase && event.entityMounting instanceof EntityPlayer
				&& event.isDismounting()) {
			EntityTrainBase entityTrain = (EntityTrainBase) event.entityBeingMounted;
			if (entityTrain.getLeftDoor() > 0 || entityTrain.getRightDoor() > 0) {
				EntityPlayer entityPlayer = (EntityPlayer) event.entityMounting;
				double a = Math.toRadians(entityTrain.rotationYaw
						+ (entityTrain.getRightDoor() > 0 && !(entityTrain instanceof EntityLightRail1) ? 0 : 180));
				entityPlayer.posX = entityPlayer.posX + 3.5D * Math.sin(a);
				entityPlayer.posZ = entityPlayer.posZ + 3.5D * Math.cos(a);
				if (entityTrain instanceof EntityMinecartSpecial)
					entityPlayer.posY += 1;
			}
		}
	}
}
