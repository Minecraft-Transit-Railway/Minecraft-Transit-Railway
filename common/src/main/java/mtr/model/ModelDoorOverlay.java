package mtr.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mtr.mappings.ModelDataWrapper;
import mtr.mappings.ModelMapper;
import mtr.render.MoreRenderLayers;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class ModelDoorOverlay extends EntityModel<Entity> {

	private final ModelMapper door_left_overlay_interior;
	private final ModelMapper door_left_top_r1;
	private final ModelMapper door_right_overlay_interior;
	private final ModelMapper door_right_top_r1;
	private final ModelMapper door_right_bottom_r1;
	private final ModelMapper door_left_overlay_exterior;
	private final ModelMapper door_left_top_r2;
	private final ModelMapper door_right_overlay_exterior;
	private final ModelMapper door_right_top_r2;
	private final ModelMapper wall_1;
	private final ModelMapper upper_wall_1_r1;
	private final ModelMapper wall_2;
	private final ModelMapper upper_wall_2_r1;

	private final ResourceLocation doorOverlayTextureLeft;
	private final ResourceLocation doorOverlayTextureRight;
	private final boolean renderLeft;
	private final boolean renderRight;

	public ModelDoorOverlay(int doorMax, float angle, String overlayLeftTextureName, String overlayRightTextureName) {
		this(doorMax, angle, 14, overlayLeftTextureName, overlayRightTextureName, true, true);
	}

	public ModelDoorOverlay(int doorMax, float angle, int pivotY, String overlayLeftTextureName, String overlayRightTextureName) {
		this(doorMax, angle, pivotY, overlayLeftTextureName, overlayRightTextureName, true, true);
	}

	public ModelDoorOverlay(int doorMax, float angle, int pivotY, String overlayLeftTextureName, String overlayRightTextureName, boolean renderLeft, boolean renderRight) {
		float angleRadians = (float) Math.toRadians(angle);
		doorOverlayTextureLeft = new ResourceLocation("mtr:textures/block/sign/" + overlayLeftTextureName);
		doorOverlayTextureRight = new ResourceLocation("mtr:textures/block/sign/" + overlayRightTextureName);
		this.renderLeft = renderLeft;
		this.renderRight = renderRight;

		final int textureWidth = 38;
		final int textureHeight = 32;

		final ModelDataWrapper modelDataWrapper = new ModelDataWrapper(this, textureWidth, textureHeight);

		door_left_overlay_interior = new ModelMapper(modelDataWrapper);
		door_left_overlay_interior.setPos(0, 24, 0);
		door_left_overlay_interior.texOffs(3, 3).addBox(-19.7F, -pivotY, 0, 0, 13, 16, 0, false);

		door_left_top_r1 = new ModelMapper(modelDataWrapper);
		door_left_top_r1.setPos(-19.7F, -pivotY, 0);
		door_left_overlay_interior.addChild(door_left_top_r1);
		ModelTrainBase.setRotationAngle(door_left_top_r1, 0, 0, angleRadians);
		door_left_top_r1.texOffs(3, -16).addBox(0, -19, 0, 0, 19, 16, 0, false);

		door_right_overlay_interior = new ModelMapper(modelDataWrapper);
		door_right_overlay_interior.setPos(0, 24, 0);


		door_right_top_r1 = new ModelMapper(modelDataWrapper);
		door_right_top_r1.setPos(-19.7F, -pivotY, 0);
		door_right_overlay_interior.addChild(door_right_top_r1);
		ModelTrainBase.setRotationAngle(door_right_top_r1, 0, 3.1416F, angleRadians);
		door_right_top_r1.texOffs(3, -16).addBox(0, -19, 0, 0, 19, 16, 0, false);

		door_right_bottom_r1 = new ModelMapper(modelDataWrapper);
		door_right_bottom_r1.setPos(0, 0, 0);
		door_right_overlay_interior.addChild(door_right_bottom_r1);
		ModelTrainBase.setRotationAngle(door_right_bottom_r1, 0, 3.1416F, 0);
		door_right_bottom_r1.texOffs(3, 3).addBox(19.7F, -pivotY, 0, 0, 13, 16, 0, false);

		door_left_overlay_exterior = new ModelMapper(modelDataWrapper);
		door_left_overlay_exterior.setPos(0, 24, 0);


		door_left_top_r2 = new ModelMapper(modelDataWrapper);
		door_left_top_r2.setPos(-20.7F, -pivotY, 0);
		door_left_overlay_exterior.addChild(door_left_top_r2);
		ModelTrainBase.setRotationAngle(door_left_top_r2, 0, 0, angleRadians);
		door_left_top_r2.texOffs(3, -16).addBox(0, -19, 0, 0, 19, 16, 0, false);

		door_right_overlay_exterior = new ModelMapper(modelDataWrapper);
		door_right_overlay_exterior.setPos(0, 24, 0);


		door_right_top_r2 = new ModelMapper(modelDataWrapper);
		door_right_top_r2.setPos(-20.7F, -pivotY, 0);
		door_right_overlay_exterior.addChild(door_right_top_r2);
		ModelTrainBase.setRotationAngle(door_right_top_r2, 0, 3.1416F, angleRadians);
		door_right_top_r2.texOffs(3, -16).addBox(0, -19, 0, 0, 19, 16, 0, false);

		wall_1 = new ModelMapper(modelDataWrapper);
		wall_1.setPos(0, 24, 0);
		wall_1.texOffs(32, 19).addBox(-20, -pivotY, -doorMax + 0.1F, 3, 13, 0, 0, false);

		upper_wall_1_r1 = new ModelMapper(modelDataWrapper);
		upper_wall_1_r1.setPos(-20, -pivotY, 0);
		wall_1.addChild(upper_wall_1_r1);
		ModelTrainBase.setRotationAngle(upper_wall_1_r1, 0, 0, angleRadians);
		upper_wall_1_r1.texOffs(32, 0).addBox(0, -19, -doorMax + 0.1F, 3, 19, 0, 0, false);

		wall_2 = new ModelMapper(modelDataWrapper);
		wall_2.setPos(0, 24, 0);
		wall_2.texOffs(0, 19).addBox(-20, -pivotY, doorMax - 0.1F, 3, 13, 0, 0, false);

		upper_wall_2_r1 = new ModelMapper(modelDataWrapper);
		upper_wall_2_r1.setPos(-20, -pivotY, 0);
		wall_2.addChild(upper_wall_2_r1);
		ModelTrainBase.setRotationAngle(upper_wall_2_r1, 0, 0, angleRadians);
		upper_wall_2_r1.texOffs(0, 0).addBox(0, -19, doorMax - 0.1F, 3, 19, 0, 0, false);

		modelDataWrapper.setModelPart(textureWidth, textureHeight);
		door_left_overlay_interior.setModelPart();
		door_right_overlay_interior.setModelPart();
		door_left_overlay_exterior.setModelPart();
		door_right_overlay_exterior.setModelPart();
		wall_1.setModelPart();
		wall_2.setModelPart();
	}

	public void render(PoseStack matrices, MultiBufferSource vertexConsumers, ModelTrainBase.RenderStage renderStage, int light, int position, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean lightsOn) {
		switch (renderStage) {
			case INTERIOR:
				final RenderType renderLayerInteriorLeft = lightsOn ? MoreRenderLayers.getInterior(doorOverlayTextureLeft) : MoreRenderLayers.getExterior(doorOverlayTextureLeft);
				final RenderType renderLayerInteriorRight = lightsOn ? MoreRenderLayers.getInterior(doorOverlayTextureRight) : MoreRenderLayers.getExterior(doorOverlayTextureRight);
				if (renderRight) {
					ModelTrainBase.renderOnce(door_left_overlay_interior, matrices, vertexConsumers.getBuffer(renderLayerInteriorRight), light, doorRightX, position + doorRightZ);
					ModelTrainBase.renderOnce(door_right_overlay_interior, matrices, vertexConsumers.getBuffer(renderLayerInteriorLeft), light, doorRightX, position - doorRightZ);
					ModelTrainBase.renderOnce(wall_1, matrices, vertexConsumers.getBuffer(renderLayerInteriorLeft), light, position);
					ModelTrainBase.renderOnce(wall_2, matrices, vertexConsumers.getBuffer(renderLayerInteriorRight), light, position);
				}
				if (renderLeft) {
					ModelTrainBase.renderOnceFlipped(door_left_overlay_interior, matrices, vertexConsumers.getBuffer(renderLayerInteriorRight), light, doorLeftX, position - doorLeftZ);
					ModelTrainBase.renderOnceFlipped(door_right_overlay_interior, matrices, vertexConsumers.getBuffer(renderLayerInteriorLeft), light, doorLeftX, position + doorLeftZ);
					ModelTrainBase.renderOnceFlipped(wall_1, matrices, vertexConsumers.getBuffer(renderLayerInteriorLeft), light, position);
					ModelTrainBase.renderOnceFlipped(wall_2, matrices, vertexConsumers.getBuffer(renderLayerInteriorRight), light, position);
				}
				break;
			case EXTERIOR:
				if (renderRight) {
					ModelTrainBase.renderOnce(door_left_overlay_exterior, matrices, vertexConsumers.getBuffer(MoreRenderLayers.getExterior(doorOverlayTextureLeft)), light / 4 * 3, doorRightX, position + doorRightZ);
					ModelTrainBase.renderOnce(door_right_overlay_exterior, matrices, vertexConsumers.getBuffer(MoreRenderLayers.getExterior(doorOverlayTextureRight)), light / 4 * 3, doorRightX, position - doorRightZ);
				}
				if (renderLeft) {
					ModelTrainBase.renderOnceFlipped(door_left_overlay_exterior, matrices, vertexConsumers.getBuffer(MoreRenderLayers.getExterior(doorOverlayTextureLeft)), light / 4 * 3, doorLeftX, position - doorLeftZ);
					ModelTrainBase.renderOnceFlipped(door_right_overlay_exterior, matrices, vertexConsumers.getBuffer(MoreRenderLayers.getExterior(doorOverlayTextureRight)), light / 4 * 3, doorLeftX, position + doorLeftZ);
				}
				break;
		}
	}

	@Override
	public void setupAnim(Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
	}

	@Override
	public final void renderToBuffer(PoseStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
	}
}
