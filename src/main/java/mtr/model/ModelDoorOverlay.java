package mtr.model;

import minecraftmappings.ModelDataWrapper;
import minecraftmappings.ModelMapper;
import mtr.render.MoreRenderLayers;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

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
		float angleRadians = (float) Math.toRadians(angle);
		doorOverlayTextureLeft = new Identifier("mtr:textures/sign/" + overlayLeftTextureName);
		doorOverlayTextureRight = new Identifier("mtr:textures/sign/" + overlayRightTextureName);
		this.renderLeft = renderLeft;
		this.renderRight = renderRight;

		final int textureWidth = 38;
		final int textureHeight = 32;

		final ModelDataWrapper modelDataWrapper = new ModelDataWrapper(this, textureWidth, textureHeight);

		door_left_overlay_interior = new ModelMapper(modelDataWrapper);
		door_left_overlay_interior.setPivot(0, 24, 0);
		door_left_overlay_interior.setTextureOffset(3, 3).addCuboid(-19.7F, -pivotY, 0, 0, 13, 16, 0, false);

		door_left_top_r1 = new ModelMapper(modelDataWrapper);
		door_left_top_r1.setPivot(-19.7F, -pivotY, 0);
		door_left_overlay_interior.addChild(door_left_top_r1);
		ModelTrainBase.setRotationAngle(door_left_top_r1, 0, 0, angleRadians);
		door_left_top_r1.setTextureOffset(3, -16).addCuboid(0, -19, 0, 0, 19, 16, 0, false);

		door_right_overlay_interior = new ModelMapper(modelDataWrapper);
		door_right_overlay_interior.setPivot(0, 24, 0);


		door_right_top_r1 = new ModelMapper(modelDataWrapper);
		door_right_top_r1.setPivot(-19.7F, -pivotY, 0);
		door_right_overlay_interior.addChild(door_right_top_r1);
		ModelTrainBase.setRotationAngle(door_right_top_r1, 0, 3.1416F, angleRadians);
		door_right_top_r1.setTextureOffset(3, -16).addCuboid(0, -19, 0, 0, 19, 16, 0, false);

		door_right_bottom_r1 = new ModelMapper(modelDataWrapper);
		door_right_bottom_r1.setPivot(0, 0, 0);
		door_right_overlay_interior.addChild(door_right_bottom_r1);
		ModelTrainBase.setRotationAngle(door_right_bottom_r1, 0, 3.1416F, 0);
		door_right_bottom_r1.setTextureOffset(3, 3).addCuboid(19.7F, -pivotY, 0, 0, 13, 16, 0, false);

		door_left_overlay_exterior = new ModelMapper(modelDataWrapper);
		door_left_overlay_exterior.setPivot(0, 24, 0);


		door_left_top_r2 = new ModelMapper(modelDataWrapper);
		door_left_top_r2.setPivot(-20.7F, -pivotY, 0);
		door_left_overlay_exterior.addChild(door_left_top_r2);
		ModelTrainBase.setRotationAngle(door_left_top_r2, 0, 0, angleRadians);
		door_left_top_r2.setTextureOffset(3, -16).addCuboid(0, -19, 0, 0, 19, 16, 0, false);

		door_right_overlay_exterior = new ModelMapper(modelDataWrapper);
		door_right_overlay_exterior.setPivot(0, 24, 0);


		door_right_top_r2 = new ModelMapper(modelDataWrapper);
		door_right_top_r2.setPivot(-20.7F, -pivotY, 0);
		door_right_overlay_exterior.addChild(door_right_top_r2);
		ModelTrainBase.setRotationAngle(door_right_top_r2, 0, 3.1416F, angleRadians);
		door_right_top_r2.setTextureOffset(3, -16).addCuboid(0, -19, 0, 0, 19, 16, 0, false);

		wall_1 = new ModelMapper(modelDataWrapper);
		wall_1.setPivot(0, 24, 0);
		wall_1.setTextureOffset(32, 19).addCuboid(-20, -pivotY, -doorMax + 0.1F, 3, 13, 0, 0, false);

		upper_wall_1_r1 = new ModelMapper(modelDataWrapper);
		upper_wall_1_r1.setPivot(-20, -pivotY, 0);
		wall_1.addChild(upper_wall_1_r1);
		ModelTrainBase.setRotationAngle(upper_wall_1_r1, 0, 0, angleRadians);
		upper_wall_1_r1.setTextureOffset(32, 0).addCuboid(0, -19, -doorMax + 0.1F, 3, 19, 0, 0, false);

		wall_2 = new ModelMapper(modelDataWrapper);
		wall_2.setPivot(0, 24, 0);
		wall_2.setTextureOffset(0, 19).addCuboid(-20, -pivotY, doorMax - 0.1F, 3, 13, 0, 0, false);

		upper_wall_2_r1 = new ModelMapper(modelDataWrapper);
		upper_wall_2_r1.setPivot(-20, -pivotY, 0);
		wall_2.addChild(upper_wall_2_r1);
		ModelTrainBase.setRotationAngle(upper_wall_2_r1, 0, 0, angleRadians);
		upper_wall_2_r1.setTextureOffset(0, 0).addCuboid(0, -19, doorMax - 0.1F, 3, 19, 0, 0, false);

		modelDataWrapper.setModelPart(textureWidth, textureHeight);
		door_left_overlay_interior.setModelPart();
		door_right_overlay_interior.setModelPart();
		door_left_overlay_exterior.setModelPart();
		door_right_overlay_exterior.setModelPart();
		wall_1.setModelPart();
		wall_2.setModelPart();
	}

	@Override
	public final void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
	}

	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, ModelTrainBase.RenderStage renderStage, int light, int position, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean lightsOn) {
		switch (renderStage) {
			case INTERIOR:
				final RenderLayer renderLayerInteriorLeft = lightsOn ? MoreRenderLayers.getInterior(doorOverlayTextureLeft) : MoreRenderLayers.getExterior(doorOverlayTextureLeft);
				final RenderLayer renderLayerInteriorRight = lightsOn ? MoreRenderLayers.getInterior(doorOverlayTextureRight) : MoreRenderLayers.getExterior(doorOverlayTextureRight);
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
	public void setAngles(Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
	}
}
