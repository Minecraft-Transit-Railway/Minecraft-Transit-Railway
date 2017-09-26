package MTR;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MTREventHandler {

	@SubscribeEvent
	public void entityMount(EntityMountEvent event) {
		if (event.getEntityBeingMounted() instanceof EntityTrainBase
				&& event.getEntityMounting() instanceof EntityPlayer && event.isDismounting()) {
			EntityTrainBase entityTrain = (EntityTrainBase) event.getEntityBeingMounted();
			if (entityTrain.leftDoor > 0 || entityTrain.rightDoor > 0) {
				EntityPlayer entityPlayer = (EntityPlayer) event.getEntityMounting();
				double a = Math.toRadians(entityTrain.rotationYaw
						+ (entityTrain.rightDoor > 0 && !(entityTrain instanceof EntityLightRail1) ? 0 : 180));
				entityPlayer.posX = entityPlayer.posX + 3.5D * Math.sin(a);
				entityPlayer.posZ = entityPlayer.posZ + 3.5D * Math.cos(a);
				if (entityTrain instanceof EntityMinecartSpecial)
					entityPlayer.posY += 1;
			}
		}
	}
}
