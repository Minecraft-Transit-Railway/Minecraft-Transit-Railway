package mtr.render;

import com.mojang.blaze3d.vertex.PoseStack;
import mtr.data.IGui;
import mtr.entity.EntityLift;
import mtr.mappings.EntityRendererMapper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;

// TODO temp code
public class RenderLift extends EntityRendererMapper<EntityLift> implements IGui {

	public RenderLift(Object parameter) {
		super(parameter);
	}

	@Override
	public void render(EntityLift entity, float entityYaw, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light) {
	}

	@Override
	public ResourceLocation getTextureLocation(EntityLift entity) {
		return null;
	}
}
