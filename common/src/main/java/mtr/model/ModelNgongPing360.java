package mtr.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mtr.mappings.ModelDataWrapper;
import mtr.mappings.ModelMapper;

public class ModelNgongPing360 extends ModelSimpleTrainBase {

	private final ModelMapper body;
	private final ModelMapper pole_2_r1;
	private final ModelMapper wall_1;
	private final ModelMapper upper_pole_2_r1;
	private final ModelMapper upper_pole_1_r1;
	private final ModelMapper upper_wall_r1;
	private final ModelMapper lower_wall_r1;
	private final ModelMapper wall_2;
	private final ModelMapper upper_wall_r2;
	private final ModelMapper lower_wall_r2;
	private final ModelMapper wall_3;
	private final ModelMapper door_bottom_4_r1;
	private final ModelMapper door_bottom_3_r1;
	private final ModelMapper door_top_r1;
	private final ModelMapper lower_wall_right_r1;
	private final ModelMapper doors;
	private final ModelMapper door_left;
	private final ModelMapper upper_wall_left_r1;
	private final ModelMapper lower_wall_left_r1;
	private final ModelMapper door_right;
	private final ModelMapper upper_wall_right_r1;
	private final ModelMapper lower_wall_right_r2;

	private final boolean isRHT;

	public ModelNgongPing360(boolean isRHT) {
		this.isRHT = isRHT;

		final int textureWidth = 192;
		final int textureHeight = 192;

		final ModelDataWrapper modelDataWrapper = new ModelDataWrapper(this, textureWidth, textureHeight);

		body = new ModelMapper(modelDataWrapper);
		body.setPos(0, 24, 0);
		body.texOffs(78, 78).addBox(-13, 0, -13, 26, 2, 26, 0, false);
		body.texOffs(0, 76).addBox(-13, -34, -13, 26, 2, 26, 0, false);
		body.texOffs(62, 0).addBox(-1, -44, -1, 2, 10, 2, 0, false);
		body.texOffs(158, 0).addBox(8, -88, -1.5F, 3, 36, 3, 0, false);

		pole_2_r1 = new ModelMapper(modelDataWrapper);
		pole_2_r1.setPos(11, -52, 0);
		body.addChild(pole_2_r1);
		setRotationAngle(pole_2_r1, 0, 0, 0.7854F);
		pole_2_r1.texOffs(22, 57).addBox(-3, 0, -1.5F, 3, 15, 3, -0.1F, false);

		wall_1 = new ModelMapper(modelDataWrapper);
		wall_1.setPos(0, 24, 0);
		wall_1.texOffs(126, 67).addBox(-14, -6, 10, 28, 1, 4, 0, false);

		upper_pole_2_r1 = new ModelMapper(modelDataWrapper);
		upper_pole_2_r1.setPos(-13, -34, 13);
		wall_1.addChild(upper_pole_2_r1);
		setRotationAngle(upper_pole_2_r1, 0.2793F, -0.0436F, 0.2793F);
		upper_pole_2_r1.texOffs(0, 0).addBox(0, -6, -2, 2, 6, 2, 0, false);

		upper_pole_1_r1 = new ModelMapper(modelDataWrapper);
		upper_pole_1_r1.setPos(13, -34, 13);
		wall_1.addChild(upper_pole_1_r1);
		setRotationAngle(upper_pole_1_r1, 0.2793F, 0.0436F, -0.2793F);
		upper_pole_1_r1.texOffs(12, 0).addBox(-2, -6, -2, 2, 6, 2, 0, false);

		upper_wall_r1 = new ModelMapper(modelDataWrapper);
		upper_wall_r1.setPos(0, -34, 13);
		wall_1.addChild(upper_wall_r1);
		setRotationAngle(upper_wall_r1, 0.2793F, 0, 0);
		upper_wall_r1.texOffs(80, 0).addBox(-19, 0, -1, 38, 19, 1, 0, false);

		lower_wall_r1 = new ModelMapper(modelDataWrapper);
		lower_wall_r1.setPos(0, 2, 13);
		wall_1.addChild(lower_wall_r1);
		setRotationAngle(lower_wall_r1, -0.2793F, 0, 0);
		lower_wall_r1.texOffs(80, 20).addBox(-19, -19, -1, 38, 19, 1, 0, false);

		wall_2 = new ModelMapper(modelDataWrapper);
		wall_2.setPos(0, 24, 0);


		upper_wall_r2 = new ModelMapper(modelDataWrapper);
		upper_wall_r2.setPos(-13, -34, 0);
		wall_2.addChild(upper_wall_r2);
		setRotationAngle(upper_wall_r2, 0, 0, 0.2793F);
		upper_wall_r2.texOffs(0, 0).addBox(0, 0, -19, 1, 19, 38, 0, false);

		lower_wall_r2 = new ModelMapper(modelDataWrapper);
		lower_wall_r2.setPos(-13, 2, 0);
		wall_2.addChild(lower_wall_r2);
		setRotationAngle(lower_wall_r2, 0, 0, -0.2793F);
		lower_wall_r2.texOffs(40, 19).addBox(0, -19, -19, 1, 19, 38, 0, false);

		wall_3 = new ModelMapper(modelDataWrapper);
		wall_3.setPos(0, 24, 0);
		wall_3.texOffs(94, 52).addBox(13, 0, -12, 4, 1, 24, 0, false);
		wall_3.texOffs(80, 40).addBox(17, 0, -7, 2, 1, 14, 0, false);

		door_bottom_4_r1 = new ModelMapper(modelDataWrapper);
		door_bottom_4_r1.setPos(17, 0, -12);
		wall_3.addChild(door_bottom_4_r1);
		setRotationAngle(door_bottom_4_r1, 0, 0.3491F, 0);
		door_bottom_4_r1.texOffs(0, 29).addBox(-2, 0.05F, 0, 2, 1, 6, 0, false);

		door_bottom_3_r1 = new ModelMapper(modelDataWrapper);
		door_bottom_3_r1.setPos(17, 0, 12);
		wall_3.addChild(door_bottom_3_r1);
		setRotationAngle(door_bottom_3_r1, 0, -0.3491F, 0);
		door_bottom_3_r1.texOffs(10, 30).addBox(-2, 0.05F, -6, 2, 1, 6, 0, false);

		door_top_r1 = new ModelMapper(modelDataWrapper);
		door_top_r1.setPos(13, -34, 0);
		wall_3.addChild(door_top_r1);
		setRotationAngle(door_top_r1, 0, 0, -0.2793F);
		door_top_r1.texOffs(0, 104).addBox(-1.5F, 0, -11, 3, 2, 22, 0, false);
		door_top_r1.texOffs(40, 0).addBox(-1, 0, 9, 1, 19, 10, 0, false);
		door_top_r1.texOffs(0, 57).addBox(-1, 0, -19, 1, 19, 10, 0, false);

		lower_wall_right_r1 = new ModelMapper(modelDataWrapper);
		lower_wall_right_r1.setPos(13, 2, 28);
		wall_3.addChild(lower_wall_right_r1);
		setRotationAngle(lower_wall_right_r1, 0, 0, 0.2793F);
		lower_wall_right_r1.texOffs(0, 0).addBox(-1, -19, -19, 1, 19, 10, 0, false);
		lower_wall_right_r1.texOffs(50, 104).addBox(-1, -19, -47, 1, 19, 10, 0, false);

		doors = new ModelMapper(modelDataWrapper);
		doors.setPos(0, 24, 0);


		door_left = new ModelMapper(modelDataWrapper);
		door_left.setPos(0, 0, 0);
		doors.addChild(door_left);


		upper_wall_left_r1 = new ModelMapper(modelDataWrapper);
		upper_wall_left_r1.setPos(14, -34, 9);
		door_left.addChild(upper_wall_left_r1);
		setRotationAngle(upper_wall_left_r1, 0, 0, -0.2793F);
		upper_wall_left_r1.texOffs(94, 106).addBox(-1, 1, -19, 1, 18, 10, 0, false);

		lower_wall_left_r1 = new ModelMapper(modelDataWrapper);
		lower_wall_left_r1.setPos(13, 2, 9);
		door_left.addChild(lower_wall_left_r1);
		setRotationAngle(lower_wall_left_r1, 0, 0, 0.2793F);
		lower_wall_left_r1.texOffs(126, 40).addBox(0, -19, -19, 1, 17, 10, 0, false);

		door_right = new ModelMapper(modelDataWrapper);
		door_right.setPos(0, 0, 0);
		doors.addChild(door_right);


		upper_wall_right_r1 = new ModelMapper(modelDataWrapper);
		upper_wall_right_r1.setPos(14, -34, 19);
		door_right.addChild(upper_wall_right_r1);
		setRotationAngle(upper_wall_right_r1, 0, 0, -0.2793F);
		upper_wall_right_r1.texOffs(72, 106).addBox(-1, 1, -19, 1, 18, 10, 0, false);

		lower_wall_right_r2 = new ModelMapper(modelDataWrapper);
		lower_wall_right_r2.setPos(13, 2, 19);
		door_right.addChild(lower_wall_right_r2);
		setRotationAngle(lower_wall_right_r2, 0, 0, 0.2793F);
		lower_wall_right_r2.texOffs(116, 106).addBox(0, -19, -19, 1, 17, 10, 0, false);

		modelDataWrapper.setModelPart(textureWidth, textureHeight);
		body.setModelPart();
		wall_1.setModelPart();
		wall_2.setModelPart();
		wall_3.setModelPart();
		doors.setModelPart();
		door_left.setModelPart(doors.name);
		door_right.setModelPart(doors.name);
	}

	private static final int DOOR_MAX = 9;

	@Override
	protected void renderWindowPositions(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
		if (renderStage == RenderStage.EXTERIOR) {
			renderMirror(wall_1, matrices, vertices, light, position);
			if (isRHT) {
				renderOnceFlipped(body, matrices, vertices, light, position);
				renderOnceFlipped(wall_2, matrices, vertices, light, position);
				renderOnceFlipped(wall_3, matrices, vertices, light, position);
			} else {
				renderOnce(body, matrices, vertices, light, position);
				renderOnce(wall_2, matrices, vertices, light, position);
				renderOnce(wall_3, matrices, vertices, light, position);
			}
		}
	}

	@Override
	protected void renderDoorPositions(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
		if (renderStage == RenderStage.EXTERIOR) {
			door_left.setOffset(0, 0, isRHT ? -doorRightZ : -doorLeftZ);
			door_right.setOffset(0, 0, isRHT ? doorRightZ : doorLeftZ);
			if (isRHT) {
				renderOnceFlipped(doors, matrices, vertices, light, position);
			} else {
				renderOnce(doors, matrices, vertices, light, position);
			}
		}
	}

	@Override
	protected void renderHeadPosition1(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean useHeadlights) {
	}

	@Override
	protected void renderHeadPosition2(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean useHeadlights) {
	}

	@Override
	protected void renderEndPosition1(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
	}

	@Override
	protected void renderEndPosition2(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
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
	protected float getDoorAnimationX(float value, boolean opening) {
		return 0;
	}

	@Override
	protected float getDoorAnimationZ(float value, boolean opening) {
		return smoothEnds(0, DOOR_MAX, 0, 0.5F, value);
	}
}
