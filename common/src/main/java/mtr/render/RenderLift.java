package mtr.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.math.Vector3f;
import mtr.EntityTypes;
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

	private final int depth;
	private final ModelLift1 modelLift1;

	private static final int LIGHT_COLOR = 0xFFFF0000;
	private static final ResourceLocation LIFT_TEXTURE = new ResourceLocation("mtr:textures/entity/lift_1.png");
	private static final ResourceLocation ARROW_TEXTURE = new ResourceLocation("mtr:textures/sign/lift_arrow.png");

	public RenderLift(Object parameter, EntityTypes.LiftType liftType) {
		super(parameter);
		depth = liftType.depth;
		modelLift1 = new ModelLift1(liftType.width, liftType.depth);
	}

	@Override
	public void render(EntityLift entity, float entityYaw, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light) {
		matrices.pushPose();
		matrices.mulPose(Vector3f.XP.rotationDegrees(180));
		matrices.mulPose(Vector3f.YP.rotationDegrees(180 - entityYaw));
		modelLift1.render(matrices, vertexConsumers, LIFT_TEXTURE, light, entity.getDoorValueClient(), 0, false, 0, 1, false, true, false, false);

		matrices.mulPose(Vector3f.YP.rotationDegrees(180));
		matrices.translate(0.875F, -1.25, depth / 2F - 0.25 - SMALL_OFFSET);
		final MultiBufferSource.BufferSource immediate = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
		IDrawing.drawStringWithFont(matrices, Minecraft.getInstance().font, immediate, entity.getCurrentFloorDisplay()[0], IGui.HorizontalAlignment.CENTER, IGui.VerticalAlignment.CENTER, 0, 0, 0.15625F, -1, 96, LIGHT_COLOR, false, MAX_LIGHT_GLOWING, null);
		immediate.endBatch();

		final EntityLift.LiftDirection liftDirection = entity.getLiftDirectionClient();
		if (liftDirection != EntityLift.LiftDirection.NONE) {
			IDrawing.drawTexture(matrices, vertexConsumers.getBuffer(MoreRenderLayers.getLight(ARROW_TEXTURE, true)), -0.03125F, -0.25F, 0.0625F, 0.0625F, 0, liftDirection == EntityLift.LiftDirection.UP ? 0 : 1, 1, liftDirection == EntityLift.LiftDirection.UP ? 1 : 0, Direction.UP, LIGHT_COLOR, MAX_LIGHT_GLOWING);
		}

		matrices.popPose();
	}

	@Override
	public ResourceLocation getTextureLocation(EntityLift entity) {
		return null;
	}
}
