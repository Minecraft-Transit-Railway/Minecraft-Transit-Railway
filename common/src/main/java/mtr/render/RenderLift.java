package mtr.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.math.Vector3f;
import mtr.client.IDrawing;
import mtr.data.IGui;
import mtr.entity.EntityLift;
import mtr.mappings.EntityRendererMapper;
import mtr.model.ModelLift1;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;

public class RenderLift extends EntityRendererMapper<EntityLift> implements IGui {

	private static final int LIGHT_COLOR = 0xFFFF0000;
	private static final ResourceLocation LIFT_TEXTURE = new ResourceLocation("mtr:textures/entity/lift_1.png");
	private static final ResourceLocation ARROW_TEXTURE = new ResourceLocation("mtr:textures/sign/lift_arrow.png");

	public RenderLift(Object parameter) {
		super(parameter);
	}

	@Override
	public void render(EntityLift entity, float entityYaw, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light) {
		matrices.pushPose();
		matrices.mulPose(Vector3f.XP.rotationDegrees(180));
		matrices.mulPose(Vector3f.YP.rotationDegrees(180 - entityYaw));
		new ModelLift1(entity.liftType.width, entity.liftType.depth).render(matrices, vertexConsumers, LIFT_TEXTURE, light, entity.getDoorValueClient(), 0, false, 0, 1, false, true, false, false);

		matrices.mulPose(Vector3f.YP.rotationDegrees(180));
		matrices.translate(0.875F, -1.5, entity.liftType.depth / 2F - 0.25 - SMALL_OFFSET);
		renderLiftDisplay(matrices, vertexConsumers, entity.getCurrentFloorDisplay()[0], entity.getLiftDirectionClient(), 0.1875F, 0.3125F);

		matrices.popPose();
	}

	@Override
	public ResourceLocation getTextureLocation(EntityLift entity) {
		return null;
	}

	public static void renderLiftDisplay(PoseStack matrices, MultiBufferSource vertexConsumers, String floorNumber, EntityLift.LiftDirection liftDirection, float maxWidth, float height) {
		final MultiBufferSource.BufferSource immediate = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
		IDrawing.drawStringWithFont(matrices, Minecraft.getInstance().font, immediate, floorNumber, IGui.HorizontalAlignment.CENTER, VerticalAlignment.BOTTOM, 0, height, maxWidth, -1, 18 / maxWidth, LIGHT_COLOR, false, MAX_LIGHT_GLOWING, null);
		immediate.endBatch();

		if (liftDirection != EntityLift.LiftDirection.NONE) {
			IDrawing.drawTexture(matrices, vertexConsumers.getBuffer(MoreRenderLayers.getLight(ARROW_TEXTURE, true)), -maxWidth / 6, 0, maxWidth / 3, maxWidth / 3, 0, liftDirection == EntityLift.LiftDirection.UP ? 0 : 1, 1, liftDirection == EntityLift.LiftDirection.UP ? 1 : 0, Direction.UP, LIGHT_COLOR, MAX_LIGHT_GLOWING);
		}
	}
}
