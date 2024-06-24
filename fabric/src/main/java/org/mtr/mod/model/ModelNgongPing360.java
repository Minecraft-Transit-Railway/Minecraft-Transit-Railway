package org.mtr.mod.model;

import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.ModelPartExtension;
import org.mtr.mod.client.DoorAnimationType;
import org.mtr.mod.resource.RenderStage;

public class ModelNgongPing360 extends ModelSimpleTrainBase<ModelNgongPing360> {

	private final ModelPartExtension body;
	private final ModelPartExtension pole_2_r1;
	private final ModelPartExtension wall_1;
	private final ModelPartExtension upper_pole_2_r1;
	private final ModelPartExtension upper_pole_1_r1;
	private final ModelPartExtension upper_wall_r1;
	private final ModelPartExtension lower_wall_r1;
	private final ModelPartExtension wall_2;
	private final ModelPartExtension upper_wall_r2;
	private final ModelPartExtension lower_wall_r2;
	private final ModelPartExtension wall_3;
	private final ModelPartExtension door_bottom_4_r1;
	private final ModelPartExtension door_bottom_3_r1;
	private final ModelPartExtension door_top_r1;
	private final ModelPartExtension lower_wall_right_r1;
	private final ModelPartExtension doors;
	private final ModelPartExtension door_left;
	private final ModelPartExtension upper_wall_left_r1;
	private final ModelPartExtension lower_wall_left_r1;
	private final ModelPartExtension door_right;
	private final ModelPartExtension upper_wall_right_r1;
	private final ModelPartExtension lower_wall_right_r2;

	private final boolean isRHT;

	public ModelNgongPing360(boolean isRHT) {
		this(isRHT, DoorAnimationType.STANDARD, true);
	}

	private ModelNgongPing360(boolean isRHT, DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		super(192, 192, doorAnimationType, renderDoorOverlay);
		this.isRHT = isRHT;

		body = createModelPart();
		body.setPivot(0, 24, 0);
		body.setTextureUVOffset(78, 78).addCuboid(-13, 0, -13, 26, 2, 26, 0, false);
		body.setTextureUVOffset(0, 76).addCuboid(-13, -34, -13, 26, 2, 26, 0, false);
		body.setTextureUVOffset(62, 0).addCuboid(-1, -44, -1, 2, 10, 2, 0, false);
		body.setTextureUVOffset(158, 0).addCuboid(8, -88, -1.5F, 3, 36, 3, 0, false);

		pole_2_r1 = createModelPart();
		pole_2_r1.setPivot(11, -52, 0);
		body.addChild(pole_2_r1);
		setRotationAngle(pole_2_r1, 0, 0, 0.7854F);
		pole_2_r1.setTextureUVOffset(22, 57).addCuboid(-3, 0, -1.5F, 3, 15, 3, -0.1F, false);

		wall_1 = createModelPart();
		wall_1.setPivot(0, 24, 0);
		wall_1.setTextureUVOffset(126, 67).addCuboid(-14, -6, 10, 28, 1, 4, 0, false);

		upper_pole_2_r1 = createModelPart();
		upper_pole_2_r1.setPivot(-13, -34, 13);
		wall_1.addChild(upper_pole_2_r1);
		setRotationAngle(upper_pole_2_r1, 0.2793F, -0.0436F, 0.2793F);
		upper_pole_2_r1.setTextureUVOffset(0, 0).addCuboid(0, -6, -2, 2, 6, 2, 0, false);

		upper_pole_1_r1 = createModelPart();
		upper_pole_1_r1.setPivot(13, -34, 13);
		wall_1.addChild(upper_pole_1_r1);
		setRotationAngle(upper_pole_1_r1, 0.2793F, 0.0436F, -0.2793F);
		upper_pole_1_r1.setTextureUVOffset(12, 0).addCuboid(-2, -6, -2, 2, 6, 2, 0, false);

		upper_wall_r1 = createModelPart();
		upper_wall_r1.setPivot(0, -34, 13);
		wall_1.addChild(upper_wall_r1);
		setRotationAngle(upper_wall_r1, 0.2793F, 0, 0);
		upper_wall_r1.setTextureUVOffset(80, 0).addCuboid(-19, 0, -1, 38, 19, 1, 0, false);

		lower_wall_r1 = createModelPart();
		lower_wall_r1.setPivot(0, 2, 13);
		wall_1.addChild(lower_wall_r1);
		setRotationAngle(lower_wall_r1, -0.2793F, 0, 0);
		lower_wall_r1.setTextureUVOffset(80, 20).addCuboid(-19, -19, -1, 38, 19, 1, 0, false);

		wall_2 = createModelPart();
		wall_2.setPivot(0, 24, 0);


		upper_wall_r2 = createModelPart();
		upper_wall_r2.setPivot(-13, -34, 0);
		wall_2.addChild(upper_wall_r2);
		setRotationAngle(upper_wall_r2, 0, 0, 0.2793F);
		upper_wall_r2.setTextureUVOffset(0, 0).addCuboid(0, 0, -19, 1, 19, 38, 0, false);

		lower_wall_r2 = createModelPart();
		lower_wall_r2.setPivot(-13, 2, 0);
		wall_2.addChild(lower_wall_r2);
		setRotationAngle(lower_wall_r2, 0, 0, -0.2793F);
		lower_wall_r2.setTextureUVOffset(40, 19).addCuboid(0, -19, -19, 1, 19, 38, 0, false);

		wall_3 = createModelPart();
		wall_3.setPivot(0, 24, 0);
		wall_3.setTextureUVOffset(94, 52).addCuboid(13, 0, -12, 4, 1, 24, 0, false);
		wall_3.setTextureUVOffset(80, 40).addCuboid(17, 0, -7, 2, 1, 14, 0, false);

		door_bottom_4_r1 = createModelPart();
		door_bottom_4_r1.setPivot(17, 0, -12);
		wall_3.addChild(door_bottom_4_r1);
		setRotationAngle(door_bottom_4_r1, 0, 0.3491F, 0);
		door_bottom_4_r1.setTextureUVOffset(0, 29).addCuboid(-2, 0.05F, 0, 2, 1, 6, 0, false);

		door_bottom_3_r1 = createModelPart();
		door_bottom_3_r1.setPivot(17, 0, 12);
		wall_3.addChild(door_bottom_3_r1);
		setRotationAngle(door_bottom_3_r1, 0, -0.3491F, 0);
		door_bottom_3_r1.setTextureUVOffset(10, 30).addCuboid(-2, 0.05F, -6, 2, 1, 6, 0, false);

		door_top_r1 = createModelPart();
		door_top_r1.setPivot(13, -34, 0);
		wall_3.addChild(door_top_r1);
		setRotationAngle(door_top_r1, 0, 0, -0.2793F);
		door_top_r1.setTextureUVOffset(0, 104).addCuboid(-1.5F, 0, -11, 3, 2, 22, 0, false);
		door_top_r1.setTextureUVOffset(40, 0).addCuboid(-1, 0, 9, 1, 19, 10, 0, false);
		door_top_r1.setTextureUVOffset(0, 57).addCuboid(-1, 0, -19, 1, 19, 10, 0, false);

		lower_wall_right_r1 = createModelPart();
		lower_wall_right_r1.setPivot(13, 2, 28);
		wall_3.addChild(lower_wall_right_r1);
		setRotationAngle(lower_wall_right_r1, 0, 0, 0.2793F);
		lower_wall_right_r1.setTextureUVOffset(0, 0).addCuboid(-1, -19, -19, 1, 19, 10, 0, false);
		lower_wall_right_r1.setTextureUVOffset(50, 104).addCuboid(-1, -19, -47, 1, 19, 10, 0, false);

		doors = createModelPart();
		doors.setPivot(0, 24, 0);


		door_left = createModelPart();
		door_left.setPivot(0, 0, 0);
		doors.addChild(door_left);


		upper_wall_left_r1 = createModelPart();
		upper_wall_left_r1.setPivot(14, -34, 9);
		door_left.addChild(upper_wall_left_r1);
		setRotationAngle(upper_wall_left_r1, 0, 0, -0.2793F);
		upper_wall_left_r1.setTextureUVOffset(94, 106).addCuboid(-1, 1, -19, 1, 18, 10, 0, false);

		lower_wall_left_r1 = createModelPart();
		lower_wall_left_r1.setPivot(13, 2, 9);
		door_left.addChild(lower_wall_left_r1);
		setRotationAngle(lower_wall_left_r1, 0, 0, 0.2793F);
		lower_wall_left_r1.setTextureUVOffset(126, 40).addCuboid(0, -19, -19, 1, 17, 10, 0, false);

		door_right = createModelPart();
		door_right.setPivot(0, 0, 0);
		doors.addChild(door_right);


		upper_wall_right_r1 = createModelPart();
		upper_wall_right_r1.setPivot(14, -34, 19);
		door_right.addChild(upper_wall_right_r1);
		setRotationAngle(upper_wall_right_r1, 0, 0, -0.2793F);
		upper_wall_right_r1.setTextureUVOffset(72, 106).addCuboid(-1, 1, -19, 1, 18, 10, 0, false);

		lower_wall_right_r2 = createModelPart();
		lower_wall_right_r2.setPivot(13, 2, 19);
		door_right.addChild(lower_wall_right_r2);
		setRotationAngle(lower_wall_right_r2, 0, 0, 0.2793F);
		lower_wall_right_r2.setTextureUVOffset(116, 106).addCuboid(0, -19, -19, 1, 17, 10, 0, false);

		buildModel();
	}

	private static final int DOOR_MAX = 9;

	@Override
	public ModelNgongPing360 createNew(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		return new ModelNgongPing360(isRHT, doorAnimationType, renderDoorOverlay);
	}

	@Override
	protected void renderWindowPositions(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
		if (renderStage == RenderStage.EXTERIOR) {
			renderMirror(wall_1, graphicsHolder, light, position);
			if (isRHT) {
				renderOnceFlipped(body, graphicsHolder, light, position);
				renderOnceFlipped(wall_2, graphicsHolder, light, position);
				renderOnceFlipped(wall_3, graphicsHolder, light, position);
			} else {
				renderOnce(body, graphicsHolder, light, position);
				renderOnce(wall_2, graphicsHolder, light, position);
				renderOnce(wall_3, graphicsHolder, light, position);
			}
		}
	}

	@Override
	protected void renderDoorPositions(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
		if (renderStage == RenderStage.EXTERIOR) {
			door_left.setOffset(0, 0, isRHT ? -doorRightZ : -doorLeftZ);
			door_right.setOffset(0, 0, isRHT ? doorRightZ : doorLeftZ);
			if (isRHT) {
				renderOnceFlipped(doors, graphicsHolder, light, position);
			} else {
				renderOnce(doors, graphicsHolder, light, position);
			}
		}
	}

	@Override
	protected void renderHeadPosition1(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean useHeadlights) {
	}

	@Override
	protected void renderHeadPosition2(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean useHeadlights) {
	}

	@Override
	protected void renderEndPosition1(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
	}

	@Override
	protected void renderEndPosition2(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
	}


	@Override
	protected ModelDoorOverlay getModelDoorOverlay() {
		return null;
	}

	@Override
	protected ModelDoorOverlayTopBase getModelDoorOverlayTop() {
		return null;
	}

	@Override
	protected int[] getWindowPositions() {
		return new int[]{0};
	}

	@Override
	protected int[] getDoorPositions() {
		return new int[]{0};
	}

	@Override
	protected int[] getEndPositions() {
		return new int[]{0, 0};
	}

	@Override
	protected int getDoorMax() {
		return DOOR_MAX;
	}
}
