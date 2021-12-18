package mtr;

import com.mojang.blaze3d.vertex.PoseStack;
import mtr.render.RenderTrains;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;

public class MTRFabricClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		MTRClient.init();
		WorldRenderEvents.AFTER_ENTITIES.register(context -> {
			final PoseStack matrices = context.matrixStack();
			matrices.pushPose();
			RenderTrains.render(context.world(), matrices, context.consumers(), context.camera());
			matrices.popPose();
		});
	}
}
