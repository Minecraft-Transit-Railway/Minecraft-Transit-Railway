package org.mtr.mod.model;

import org.mtr.init.MTR;
import org.mtr.mapping.holder.EntityAbstractMapping;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.holder.RenderLayer;
import org.mtr.mapping.mapper.EntityModelExtension;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.ModelPartExtension;
import org.mtr.mod.render.MoreRenderLayers;

public class ModelDoorOverlay extends EntityModelExtension<EntityAbstractMapping> {

	private final ModelPartExtension door_left_overlay_interior;
	private final ModelPartExtension door_left_top_r1;
	private final ModelPartExtension door_right_overlay_interior;
	private final ModelPartExtension door_right_top_r1;
	private final ModelPartExtension door_right_bottom_r1;
	private final ModelPartExtension door_left_overlay_exterior;
	private final ModelPartExtension door_left_top_r2;
	private final ModelPartExtension door_right_overlay_exterior;
	private final ModelPartExtension door_right_top_r2;
	private final ModelPartExtension wall_1;
	private final ModelPartExtension upper_wall_1_r1;
	private final ModelPartExtension wall_2;
	private final ModelPartExtension upper_wall_2_r1;

	private final Identifier doorOverlayTextureLeft;
	private final Identifier doorOverlayTextureRight;
	private final boolean renderLeft;
	private final boolean renderRight;

	public ModelDoorOverlay(int doorMax, float angle, String overlayLeftTextureName, String overlayRightTextureName) {
		this(doorMax, angle, 14, overlayLeftTextureName, overlayRightTextureName, true, true);
	}

	public ModelDoorOverlay(int doorMax, float angle, int pivotY, String overlayLeftTextureName, String overlayRightTextureName) {
		this(doorMax, angle, pivotY, overlayLeftTextureName, overlayRightTextureName, true, true);
	}

	public ModelDoorOverlay(int doorMax, float angle, int pivotY, String overlayLeftTextureName, String overlayRightTextureName, boolean renderLeft, boolean renderRight) {
		super(38, 32);
		float angleRadians = (float) Math.toRadians(angle);
		doorOverlayTextureLeft = new Identifier(MTR.MOD_ID, "textures/block/sign/" + overlayLeftTextureName);
		doorOverlayTextureRight = new Identifier(MTR.MOD_ID, "textures/block/sign/" + overlayRightTextureName);
		this.renderLeft = renderLeft;
		this.renderRight = renderRight;

		door_left_overlay_interior = createModelPart();
		door_left_overlay_interior.setPivot(0, 24, 0);
		door_left_overlay_interior.setTextureUVOffset(3, 3).addCuboid(-19.7F, -pivotY, 0, 0, 13, 16, 0, false);

		door_left_top_r1 = createModelPart();
		door_left_top_r1.setPivot(-19.7F, -pivotY, 0);
		door_left_overlay_interior.addChild(door_left_top_r1);
		ModelTrainBase.setRotationAngle(door_left_top_r1, 0, 0, angleRadians);
		door_left_top_r1.setTextureUVOffset(3, -16).addCuboid(0, -19, 0, 0, 19, 16, 0, false);

		door_right_overlay_interior = createModelPart();
		door_right_overlay_interior.setPivot(0, 24, 0);


		door_right_top_r1 = createModelPart();
		door_right_top_r1.setPivot(-19.7F, -pivotY, 0);
		door_right_overlay_interior.addChild(door_right_top_r1);
		ModelTrainBase.setRotationAngle(door_right_top_r1, 0, 3.1416F, angleRadians);
		door_right_top_r1.setTextureUVOffset(3, -16).addCuboid(0, -19, 0, 0, 19, 16, 0, false);

		door_right_bottom_r1 = createModelPart();
		door_right_bottom_r1.setPivot(0, 0, 0);
		door_right_overlay_interior.addChild(door_right_bottom_r1);
		ModelTrainBase.setRotationAngle(door_right_bottom_r1, 0, 3.1416F, 0);
		door_right_bottom_r1.setTextureUVOffset(3, 3).addCuboid(19.7F, -pivotY, 0, 0, 13, 16, 0, false);

		door_left_overlay_exterior = createModelPart();
		door_left_overlay_exterior.setPivot(0, 24, 0);


		door_left_top_r2 = createModelPart();
		door_left_top_r2.setPivot(-20.7F, -pivotY, 0);
		door_left_overlay_exterior.addChild(door_left_top_r2);
		ModelTrainBase.setRotationAngle(door_left_top_r2, 0, 0, angleRadians);
		door_left_top_r2.setTextureUVOffset(3, -16).addCuboid(0, -19, 0, 0, 19, 16, 0, false);

		door_right_overlay_exterior = createModelPart();
		door_right_overlay_exterior.setPivot(0, 24, 0);


		door_right_top_r2 = createModelPart();
		door_right_top_r2.setPivot(-20.7F, -pivotY, 0);
		door_right_overlay_exterior.addChild(door_right_top_r2);
		ModelTrainBase.setRotationAngle(door_right_top_r2, 0, 3.1416F, angleRadians);
		door_right_top_r2.setTextureUVOffset(3, -16).addCuboid(0, -19, 0, 0, 19, 16, 0, false);

		wall_1 = createModelPart();
		wall_1.setPivot(0, 24, 0);
		wall_1.setTextureUVOffset(32, 19).addCuboid(-20, -pivotY, -doorMax + 0.1F, 3, 13, 0, 0, false);

		upper_wall_1_r1 = createModelPart();
		upper_wall_1_r1.setPivot(-20, -pivotY, 0);
		wall_1.addChild(upper_wall_1_r1);
		ModelTrainBase.setRotationAngle(upper_wall_1_r1, 0, 0, angleRadians);
		upper_wall_1_r1.setTextureUVOffset(32, 0).addCuboid(0, -19, -doorMax + 0.1F, 3, 19, 0, 0, false);

		wall_2 = createModelPart();
		wall_2.setPivot(0, 24, 0);
		wall_2.setTextureUVOffset(0, 19).addCuboid(-20, -pivotY, doorMax - 0.1F, 3, 13, 0, 0, false);

		upper_wall_2_r1 = createModelPart();
		upper_wall_2_r1.setPivot(-20, -pivotY, 0);
		wall_2.addChild(upper_wall_2_r1);
		ModelTrainBase.setRotationAngle(upper_wall_2_r1, 0, 0, angleRadians);
		upper_wall_2_r1.setTextureUVOffset(0, 0).addCuboid(0, -19, doorMax - 0.1F, 3, 19, 0, 0, false);

		buildModel();
	}

	public void render(GraphicsHolder graphicsHolder, ModelTrainBase.RenderStage renderStage, int light, int position, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean lightsOn) {
		switch (renderStage) {
			case INTERIOR:
				final RenderLayer renderLayerInteriorLeft = lightsOn ? MoreRenderLayers.getInterior(doorOverlayTextureLeft) : MoreRenderLayers.getExterior(doorOverlayTextureLeft);
				final RenderLayer renderLayerInteriorRight = lightsOn ? MoreRenderLayers.getInterior(doorOverlayTextureRight) : MoreRenderLayers.getExterior(doorOverlayTextureRight);
				if (renderRight) {
					graphicsHolder.createVertexConsumer(renderLayerInteriorLeft);
					ModelTrainBase.renderOnce(door_right_overlay_interior, graphicsHolder, light, doorRightX, position - doorRightZ);
					ModelTrainBase.renderOnce(wall_1, graphicsHolder, light, position);
					graphicsHolder.createVertexConsumer(renderLayerInteriorRight);
					ModelTrainBase.renderOnce(door_left_overlay_interior, graphicsHolder, light, doorRightX, position + doorRightZ);
					ModelTrainBase.renderOnce(wall_2, graphicsHolder, light, position);
				}
				if (renderLeft) {
					graphicsHolder.createVertexConsumer(renderLayerInteriorLeft);
					ModelTrainBase.renderOnceFlipped(door_right_overlay_interior, graphicsHolder, light, doorLeftX, position + doorLeftZ);
					ModelTrainBase.renderOnceFlipped(wall_1, graphicsHolder, light, position);
					graphicsHolder.createVertexConsumer(renderLayerInteriorRight);
					ModelTrainBase.renderOnceFlipped(door_left_overlay_interior, graphicsHolder, light, doorLeftX, position - doorLeftZ);
					ModelTrainBase.renderOnceFlipped(wall_2, graphicsHolder, light, position);
				}
				break;
			case EXTERIOR:
				if (renderRight) {
					graphicsHolder.createVertexConsumer(MoreRenderLayers.getExterior(doorOverlayTextureLeft));
					ModelTrainBase.renderOnce(door_left_overlay_exterior, graphicsHolder, light / 4 * 3, doorRightX, position + doorRightZ);
					graphicsHolder.createVertexConsumer(MoreRenderLayers.getExterior(doorOverlayTextureRight));
					ModelTrainBase.renderOnce(door_right_overlay_exterior, graphicsHolder, light / 4 * 3, doorRightX, position - doorRightZ);
				}
				if (renderLeft) {
					graphicsHolder.createVertexConsumer(MoreRenderLayers.getExterior(doorOverlayTextureLeft));
					ModelTrainBase.renderOnceFlipped(door_left_overlay_exterior, graphicsHolder, light / 4 * 3, doorLeftX, position - doorLeftZ);
					graphicsHolder.createVertexConsumer(MoreRenderLayers.getExterior(doorOverlayTextureRight));
					ModelTrainBase.renderOnceFlipped(door_right_overlay_exterior, graphicsHolder, light / 4 * 3, doorLeftX, position + doorLeftZ);
				}
				break;
		}
	}

	@Override
	public void setAngles2(EntityAbstractMapping entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
	}

	@Override
	public final void render(GraphicsHolder graphicsHolder, int light, int overlay, float red, float green, float blue, float alpha) {
	}
}
