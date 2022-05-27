package mtr.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import mtr.entity.EntityLift;
import mtr.mappings.EntityRendererMapper;
import mtr.mappings.UtilitiesClient;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class RenderLift extends EntityRendererMapper<EntityLift> {

	public RenderLift(Object parameter) {
		super(parameter);
	}

	@Override
	public void render(EntityLift entity, float entityYaw, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light) {
		matrices.pushPose();
		matrices.mulPose(Vector3f.YP.rotationDegrees(entityYaw));
		UtilitiesClient.getMinecartModel().renderToBuffer(matrices, vertexConsumers.getBuffer(RenderType.entityCutout(new ResourceLocation("textures/entity/minecart.png"))), light, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
		matrices.popPose();
	}

	@Override
	public ResourceLocation getTextureLocation(EntityLift entity) {
		return null;
	}
}
