package org.mtr.mod.model;

import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.ModelPartExtension;
import org.mtr.mod.client.DoorAnimationType;

public class ModelSTrain extends ModelSimpleTrainBase<ModelSTrain> {

	private final ModelPartExtension window;
	private final ModelPartExtension upper_wall_2_r1;
	private final ModelPartExtension window_handrails;
	private final ModelPartExtension top_handrail_5_r1;
	private final ModelPartExtension top_handrail_4_r1;
	private final ModelPartExtension top_handrail_3_r1;
	private final ModelPartExtension top_handrail_2_r1;
	private final ModelPartExtension top_handrail_1_r1;
	private final ModelPartExtension seat_back_r1;
	private final ModelPartExtension window_exterior_1;
	private final ModelPartExtension door_leaf_r1;
	private final ModelPartExtension upper_wall_r1;
	private final ModelPartExtension window_exterior_2;
	private final ModelPartExtension door_leaf_r2;
	private final ModelPartExtension upper_wall_r2;
	private final ModelPartExtension side_panel_translucent;
	private final ModelPartExtension roof_window;
	private final ModelPartExtension inner_roof_5_r1;
	private final ModelPartExtension inner_roof_4_r1;
	private final ModelPartExtension inner_roof_2_r1;
	private final ModelPartExtension roof_door;
	private final ModelPartExtension inner_roof_5_r2;
	private final ModelPartExtension inner_roof_4_r2;
	private final ModelPartExtension inner_roof_2_r2;
	private final ModelPartExtension roof_exterior;
	private final ModelPartExtension outer_roof_5_r1;
	private final ModelPartExtension outer_roof_4_r1;
	private final ModelPartExtension outer_roof_3_r1;
	private final ModelPartExtension door;
	private final ModelPartExtension door_left;
	private final ModelPartExtension door_left_top_r1;
	private final ModelPartExtension door_right;
	private final ModelPartExtension door_right_top_r1;
	private final ModelPartExtension door_handrail;
	private final ModelPartExtension pole_bottom_diagonal_3_r1;
	private final ModelPartExtension pole_bottom_diagonal_2_r1;
	private final ModelPartExtension pole_bottom_diagonal_1_r1;
	private final ModelPartExtension pole_middle_3_r1;
	private final ModelPartExtension pole_middle_2_r1;
	private final ModelPartExtension pole_middle_1_r1;
	private final ModelPartExtension pole_top_diagonal_3_r1;
	private final ModelPartExtension pole_top_diagonal_2_r1;
	private final ModelPartExtension pole_top_diagonal_1_r1;
	private final ModelPartExtension door_exterior_1;
	private final ModelPartExtension door_leaf_r3;
	private final ModelPartExtension door_left_exterior_1;
	private final ModelPartExtension door_left_top_r2;
	private final ModelPartExtension door_right_exterior_1;
	private final ModelPartExtension door_right_top_r2;
	private final ModelPartExtension door_exterior_2;
	private final ModelPartExtension door_leaf_r4;
	private final ModelPartExtension door_left_exterior_2;
	private final ModelPartExtension door_left_top_r3;
	private final ModelPartExtension door_right_exterior_2;
	private final ModelPartExtension door_right_top_r3;
	private final ModelPartExtension end;
	private final ModelPartExtension upper_wall_2_r2;
	private final ModelPartExtension upper_wall_1_r1;
	private final ModelPartExtension lower_wall_1_r1;
	private final ModelPartExtension end_exterior;
	private final ModelPartExtension door_leaf_2_r1;
	private final ModelPartExtension door_leaf_1_r1;
	private final ModelPartExtension upper_wall_2_r3;
	private final ModelPartExtension upper_wall_1_r2;
	private final ModelPartExtension roof_end;
	private final ModelPartExtension inner_roof_1;
	private final ModelPartExtension inner_roof_5_r3;
	private final ModelPartExtension inner_roof_4_r3;
	private final ModelPartExtension inner_roof_2_r3;
	private final ModelPartExtension inner_roof_2;
	private final ModelPartExtension inner_roof_6_r1;
	private final ModelPartExtension inner_roof_5_r4;
	private final ModelPartExtension inner_roof_3_r1;
	private final ModelPartExtension roof_end_exterior;
	private final ModelPartExtension vent_2_r1;
	private final ModelPartExtension vent_1_r1;
	private final ModelPartExtension outer_roof_1;
	private final ModelPartExtension outer_roof_5_r2;
	private final ModelPartExtension outer_roof_4_r2;
	private final ModelPartExtension outer_roof_3_r2;
	private final ModelPartExtension outer_roof_2;
	private final ModelPartExtension outer_roof_6_r1;
	private final ModelPartExtension outer_roof_5_r3;
	private final ModelPartExtension outer_roof_4_r3;
	private final ModelPartExtension roof_window_light;
	private final ModelPartExtension light_2_r1;
	private final ModelPartExtension light_1_r1;
	private final ModelPartExtension roof_door_light;
	private final ModelPartExtension light_3_r1;
	private final ModelPartExtension light_2_r2;
	private final ModelPartExtension roof_end_light;
	private final ModelPartExtension light_3_r2;
	private final ModelPartExtension light_2_r3;
	private final ModelPartExtension light_3_r3;
	private final ModelPartExtension light_2_r4;
	private final ModelPartExtension head;
	private final ModelPartExtension upper_wall_2_r4;
	private final ModelPartExtension upper_wall_1_r3;
	private final ModelPartExtension lower_wall_1_r2;
	private final ModelPartExtension ceiling;
	private final ModelPartExtension panel_9_r1;
	private final ModelPartExtension panel_8_r1;
	private final ModelPartExtension panel_7_r1;
	private final ModelPartExtension panel_6_r1;
	private final ModelPartExtension panel_4_r1;
	private final ModelPartExtension panel_3_r1;
	private final ModelPartExtension panel_2_r1;
	private final ModelPartExtension panel_1_r1;
	private final ModelPartExtension main_r1;
	private final ModelPartExtension emergency_door;
	private final ModelPartExtension upper_r1;
	private final ModelPartExtension left_c_panel;
	private final ModelPartExtension panel_r1;
	private final ModelPartExtension base_r1;
	private final ModelPartExtension right_c_panel;
	private final ModelPartExtension panel_r2;
	private final ModelPartExtension base_r2;
	private final ModelPartExtension handrail;
	private final ModelPartExtension wall;
	private final ModelPartExtension handrail_4_r1;
	private final ModelPartExtension handrail_3_r1;
	private final ModelPartExtension handrail_2_r1;
	private final ModelPartExtension handrail_5_r1;
	private final ModelPartExtension handrail_1_r1;
	private final ModelPartExtension ceiling2;
	private final ModelPartExtension handrail_7_r1;
	private final ModelPartExtension handrail_4_r2;
	private final ModelPartExtension handrail_5_r2;
	private final ModelPartExtension handrail_6_r1;
	private final ModelPartExtension handrail_3_r2;
	private final ModelPartExtension ceiling3;
	private final ModelPartExtension handrail_8_r1;
	private final ModelPartExtension handrail_5_r3;
	private final ModelPartExtension handrail_6_r2;
	private final ModelPartExtension handrail_7_r2;
	private final ModelPartExtension handrail_4_r3;
	private final ModelPartExtension head_exterior;
	private final ModelPartExtension upper_wall_2_r5;
	private final ModelPartExtension upper_wall_1_r4;
	private final ModelPartExtension door_leaf_4_r1;
	private final ModelPartExtension door_leaf_1_r2;
	private final ModelPartExtension door_leaf_5_r1;
	private final ModelPartExtension door_leaf_2_r2;
	private final ModelPartExtension front;
	private final ModelPartExtension front_panel_4_r1;
	private final ModelPartExtension front_panel_3_r1;
	private final ModelPartExtension front_panel_1_r1;
	private final ModelPartExtension side_1;
	private final ModelPartExtension front_side_bottom_3_r1;
	private final ModelPartExtension front_side_bottom_1_r1;
	private final ModelPartExtension front_side_lower_1_r1;
	private final ModelPartExtension front_side_upper_1_r1;
	private final ModelPartExtension side_2;
	private final ModelPartExtension front_side_upper_2_r1;
	private final ModelPartExtension front_side_lower_2_r1;
	private final ModelPartExtension front_side_bottom_4_r1;
	private final ModelPartExtension front_side_bottom_2_r1;
	private final ModelPartExtension roof;
	private final ModelPartExtension outer_roof_6_r2;
	private final ModelPartExtension outer_roof_5_r4;
	private final ModelPartExtension outer_roof_4_r4;
	private final ModelPartExtension outer_roof_5_r5;
	private final ModelPartExtension outer_roof_4_r5;
	private final ModelPartExtension outer_roof_3_r3;
	private final ModelPartExtension vent_top_r1;
	private final ModelPartExtension vent_2_r2;
	private final ModelPartExtension vent_1_r2;
	private final ModelPartExtension outer_roof_6_r3;
	private final ModelPartExtension outer_roof_7_r1;
	private final ModelPartExtension outer_roof_7_r2;
	private final ModelPartExtension outer_roof_8_r1;
	private final ModelPartExtension outer_roof_5_r6;
	private final ModelPartExtension outer_roof_6_r4;
	private final ModelPartExtension outer_roof_6_r5;
	private final ModelPartExtension outer_roof_7_r3;
	private final ModelPartExtension headlights;
	private final ModelPartExtension tail_lights;
	private final ModelPartExtension door_light_on;
	private final ModelPartExtension light_r1;
	private final ModelPartExtension door_light_off;
	private final ModelPartExtension light_r2;

	public ModelSTrain() {
		this(DoorAnimationType.STANDARD, true);
	}

	protected ModelSTrain(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		super(320, 320, doorAnimationType, renderDoorOverlay);

		window = createModelPart();
		window.setPivot(0, 24, 0);
		window.setTextureUVOffset(0, 0).addCuboid(-20, 0, -24, 20, 1, 48, 0, false);
		window.setTextureUVOffset(80, 43).addCuboid(-18, -5, -21, 0, 5, 42, 0, false);
		window.setTextureUVOffset(124, 204).addCuboid(-20, -14, 21, 3, 14, 6, 0, false);
		window.setTextureUVOffset(130, 164).addCuboid(-20, -14, -27, 3, 14, 6, 0, false);

		upper_wall_2_r1 = createModelPart();
		upper_wall_2_r1.setPivot(-20, -14, 0);
		window.addChild(upper_wall_2_r1);
		setRotationAngle(upper_wall_2_r1, 0, 0, 0.1107F);
		upper_wall_2_r1.setTextureUVOffset(72, 196).addCuboid(0, -19, -27, 3, 19, 6, 0, false);
		upper_wall_2_r1.setTextureUVOffset(245, 205).addCuboid(0, -19, 21, 3, 19, 6, 0, false);
		upper_wall_2_r1.setTextureUVOffset(106, 141).addCuboid(0, -19, -22, 2, 19, 44, 0, false);

		window_handrails = createModelPart();
		window_handrails.setPivot(0, 24, 0);
		window_handrails.setTextureUVOffset(156, 162).addCuboid(-18, -6, -21, 7, 1, 42, 0, false);
		window_handrails.setTextureUVOffset(16, 55).addCuboid(-18, -11, -22, 7, 6, 1, 0, false);
		window_handrails.setTextureUVOffset(16, 55).addCuboid(-18, -11, 21, 7, 6, 1, 0, false);
		window_handrails.setTextureUVOffset(319, 1).addCuboid(0, -35.2F, -13, 0, 35, 0, 0.2F, false);
		window_handrails.setTextureUVOffset(319, 1).addCuboid(0, -35.2F, 13, 0, 35, 0, 0.2F, false);
		window_handrails.setTextureUVOffset(0, 0).addCuboid(-8, -33, 18, 2, 4, 0, 0, false);
		window_handrails.setTextureUVOffset(0, 0).addCuboid(-8, -33, 12, 2, 4, 0, 0, false);
		window_handrails.setTextureUVOffset(0, 0).addCuboid(-8, -33, 6, 2, 4, 0, 0, false);
		window_handrails.setTextureUVOffset(0, 0).addCuboid(-8, -33, -6, 2, 4, 0, 0, false);
		window_handrails.setTextureUVOffset(0, 0).addCuboid(-8, -33, -12, 2, 4, 0, 0, false);
		window_handrails.setTextureUVOffset(0, 0).addCuboid(-8, -33, -18, 2, 4, 0, 0, false);
		window_handrails.setTextureUVOffset(319, 0).addCuboid(-7, -34.5F, -10, 0, 2, 0, 0.2F, true);
		window_handrails.setTextureUVOffset(319, 0).addCuboid(-7, -34.5F, 10, 0, 2, 0, 0.2F, true);
		window_handrails.setTextureUVOffset(319, 2).addCuboid(-11, -34.2F, -22, 0, 29, 0, 0.2F, false);
		window_handrails.setTextureUVOffset(319, 0).addCuboid(-11, -34.2F, 22, 0, 29, 0, 0.2F, false);

		top_handrail_5_r1 = createModelPart();
		top_handrail_5_r1.setPivot(0, 0, 0);
		window_handrails.addChild(top_handrail_5_r1);
		setRotationAngle(top_handrail_5_r1, 1.5708F, 0, 0);
		top_handrail_5_r1.setTextureUVOffset(319, 22).addCuboid(-7, -20, 32.5F, 0, 40, 0, 0.2F, false);

		top_handrail_4_r1 = createModelPart();
		top_handrail_4_r1.setPivot(-7, -33.7062F, 21.5892F);
		window_handrails.addChild(top_handrail_4_r1);
		setRotationAngle(top_handrail_4_r1, -0.5236F, 0, 0);
		top_handrail_4_r1.setTextureUVOffset(319, 0).addCuboid(0, -1.5F, 0, 0, 2, 0, 0.2F, true);

		top_handrail_3_r1 = createModelPart();
		top_handrail_3_r1.setPivot(-6.8F, -32.3F, 20.2F);
		window_handrails.addChild(top_handrail_3_r1);
		setRotationAngle(top_handrail_3_r1, -1.0472F, 0, 0);
		top_handrail_3_r1.setTextureUVOffset(319, 0).addCuboid(-0.2F, -1.2F, -0.2F, 0, 1, 0, 0.2F, true);

		top_handrail_2_r1 = createModelPart();
		top_handrail_2_r1.setPivot(-6.8F, -32.3F, -20.2F);
		window_handrails.addChild(top_handrail_2_r1);
		setRotationAngle(top_handrail_2_r1, 1.0472F, 0, 0);
		top_handrail_2_r1.setTextureUVOffset(319, 0).addCuboid(-0.2F, -1.2F, 0.2F, 0, 1, 0, 0.2F, true);

		top_handrail_1_r1 = createModelPart();
		top_handrail_1_r1.setPivot(-7, -34.1392F, -21.8392F);
		window_handrails.addChild(top_handrail_1_r1);
		setRotationAngle(top_handrail_1_r1, 0.5236F, 0, 0);
		top_handrail_1_r1.setTextureUVOffset(319, 0).addCuboid(0, -1, 0, 0, 2, 0, 0.2F, true);

		seat_back_r1 = createModelPart();
		seat_back_r1.setPivot(-17, -6, 0);
		window_handrails.addChild(seat_back_r1);
		setRotationAngle(seat_back_r1, 0, 0, -0.0524F);
		seat_back_r1.setTextureUVOffset(146, 85).addCuboid(-1, -8, -22, 1, 8, 44, 0, false);

		window_exterior_1 = createModelPart();
		window_exterior_1.setPivot(0, 24, 0);
		window_exterior_1.setTextureUVOffset(0, 69).addCuboid(-20, -14, -26, 0, 18, 52, 0, false);

		door_leaf_r1 = createModelPart();
		door_leaf_r1.setPivot(-21, -14, 0);
		window_exterior_1.addChild(door_leaf_r1);
		setRotationAngle(door_leaf_r1, 0, 0, 0.1107F);
		door_leaf_r1.setTextureUVOffset(0, 139).addCuboid(0, -23, -26, 1, 5, 52, 0, false);

		upper_wall_r1 = createModelPart();
		upper_wall_r1.setPivot(-20, -14, 0);
		window_exterior_1.addChild(upper_wall_r1);
		setRotationAngle(upper_wall_r1, 0, 0, 0.1107F);
		upper_wall_r1.setTextureUVOffset(0, 47).addCuboid(0, -22, -26, 0, 22, 52, 0, false);

		window_exterior_2 = createModelPart();
		window_exterior_2.setPivot(0, 24, 0);
		window_exterior_2.setTextureUVOffset(0, 69).addCuboid(20, -14, -26, 0, 18, 52, 0, true);

		door_leaf_r2 = createModelPart();
		door_leaf_r2.setPivot(21, -14, 0);
		window_exterior_2.addChild(door_leaf_r2);
		setRotationAngle(door_leaf_r2, 0, 0, -0.1107F);
		door_leaf_r2.setTextureUVOffset(0, 139).addCuboid(-1, -23, -26, 1, 5, 52, 0, true);

		upper_wall_r2 = createModelPart();
		upper_wall_r2.setPivot(20, -14, 0);
		window_exterior_2.addChild(upper_wall_r2);
		setRotationAngle(upper_wall_r2, 0, 0, -0.1107F);
		upper_wall_r2.setTextureUVOffset(0, 47).addCuboid(0, -22, -26, 0, 22, 52, 0, true);

		side_panel_translucent = createModelPart();
		side_panel_translucent.setPivot(0, 24, 0);
		side_panel_translucent.setTextureUVOffset(152, 90).addCuboid(-18, -25, 0, 7, 15, 0, 0, false);

		roof_window = createModelPart();
		roof_window.setPivot(0, 24, 0);
		roof_window.setTextureUVOffset(60, 0).addCuboid(-16, -32, -24, 3, 0, 48, 0, false);
		roof_window.setTextureUVOffset(55, 0).addCuboid(-11.0724F, -34.2978F, -24, 1, 0, 48, 0, false);
		roof_window.setTextureUVOffset(40, 0).addCuboid(-4, -34.5F, -24, 4, 0, 48, 0, false);

		inner_roof_5_r1 = createModelPart();
		inner_roof_5_r1.setPivot(-3, -34.5F, 0);
		roof_window.addChild(inner_roof_5_r1);
		setRotationAngle(inner_roof_5_r1, 0, 0, -0.0873F);
		inner_roof_5_r1.setTextureUVOffset(54, 0).addCuboid(-3, 0, -24, 3, 0, 48, 0, false);

		inner_roof_4_r1 = createModelPart();
		inner_roof_4_r1.setPivot(-6.9734F, -34.0649F, 0);
		roof_window.addChild(inner_roof_4_r1);
		setRotationAngle(inner_roof_4_r1, 0, 0, -0.1745F);
		inner_roof_4_r1.setTextureUVOffset(66, 0).addCuboid(-1, 0, -24, 2, 0, 48, 0, false);

		inner_roof_2_r1 = createModelPart();
		inner_roof_2_r1.setPivot(-13, -32, 0);
		roof_window.addChild(inner_roof_2_r1);
		setRotationAngle(inner_roof_2_r1, 0, 0, -0.8727F);
		inner_roof_2_r1.setTextureUVOffset(60, 0).addCuboid(0, 0, -24, 3, 0, 48, 0, false);

		roof_door = createModelPart();
		roof_door.setPivot(0, 24, 0);
		roof_door.setTextureUVOffset(204, 0).addCuboid(-18, -33, -16, 5, 1, 32, 0, false);
		roof_door.setTextureUVOffset(80, 16).addCuboid(-11.0724F, -34.2978F, -16, 1, 0, 32, 0, false);
		roof_door.setTextureUVOffset(56, 0).addCuboid(-4, -34.5F, -16, 4, 0, 32, 0, false);

		inner_roof_5_r2 = createModelPart();
		inner_roof_5_r2.setPivot(-3, -34.5F, 0);
		roof_door.addChild(inner_roof_5_r2);
		setRotationAngle(inner_roof_5_r2, 0, 0, -0.0873F);
		inner_roof_5_r2.setTextureUVOffset(70, 0).addCuboid(-3, 0, -16, 3, 0, 32, 0, false);

		inner_roof_4_r2 = createModelPart();
		inner_roof_4_r2.setPivot(-6.9733F, -34.0649F, 0);
		roof_door.addChild(inner_roof_4_r2);
		setRotationAngle(inner_roof_4_r2, 0, 0, -0.1745F);
		inner_roof_4_r2.setTextureUVOffset(82, 0).addCuboid(-1, 0, -16, 2, 0, 32, 0, false);

		inner_roof_2_r2 = createModelPart();
		inner_roof_2_r2.setPivot(-13, -32, 0);
		roof_door.addChild(inner_roof_2_r2);
		setRotationAngle(inner_roof_2_r2, 0, 0, -0.8727F);
		inner_roof_2_r2.setTextureUVOffset(114, 90).addCuboid(0, 0, -16, 3, 0, 32, 0, false);

		roof_exterior = createModelPart();
		roof_exterior.setPivot(0, 24, 0);
		roof_exterior.setTextureUVOffset(64, 99).addCuboid(-6, -41, -20, 6, 0, 40, 0, false);

		outer_roof_5_r1 = createModelPart();
		outer_roof_5_r1.setPivot(-6, -41, 0);
		roof_exterior.addChild(outer_roof_5_r1);
		setRotationAngle(outer_roof_5_r1, 0, 0, -0.1745F);
		outer_roof_5_r1.setTextureUVOffset(98, 0).addCuboid(-8, 0, -20, 8, 0, 40, 0, false);

		outer_roof_4_r1 = createModelPart();
		outer_roof_4_r1.setPivot(-15.6102F, -38.6109F, 0);
		roof_exterior.addChild(outer_roof_4_r1);
		setRotationAngle(outer_roof_4_r1, 0, 0, -0.5236F);
		outer_roof_4_r1.setTextureUVOffset(0, 0).addCuboid(-2, 0, -20, 4, 0, 40, 0, false);

		outer_roof_3_r1 = createModelPart();
		outer_roof_3_r1.setPivot(-17.8419F, -36.7453F, 0);
		roof_exterior.addChild(outer_roof_3_r1);
		setRotationAngle(outer_roof_3_r1, 0, 0, -1.0472F);
		outer_roof_3_r1.setTextureUVOffset(0, 49).addCuboid(-1, 0, -20, 2, 0, 40, 0, false);

		door = createModelPart();
		door.setPivot(0, 24, 0);
		door.setTextureUVOffset(178, 50).addCuboid(-20, 0, -16, 20, 1, 32, 0, false);

		door_left = createModelPart();
		door_left.setPivot(0, 0, 0);
		door.addChild(door_left);
		door_left.setTextureUVOffset(94, 265).addCuboid(-21, -14, 0, 1, 14, 14, 0, false);

		door_left_top_r1 = createModelPart();
		door_left_top_r1.setPivot(-21, -14, 0);
		door_left.addChild(door_left_top_r1);
		setRotationAngle(door_left_top_r1, 0, 0, 0.1107F);
		door_left_top_r1.setTextureUVOffset(192, 83).addCuboid(0, -19, 0, 1, 19, 14, 0, false);

		door_right = createModelPart();
		door_right.setPivot(0, 0, 0);
		door.addChild(door_right);
		door_right.setTextureUVOffset(204, 0).addCuboid(-21, -14, -14, 1, 14, 14, 0, false);

		door_right_top_r1 = createModelPart();
		door_right_top_r1.setPivot(-21, -14, 0);
		door_right.addChild(door_right_top_r1);
		setRotationAngle(door_right_top_r1, 0, 0, 0.1107F);
		door_right_top_r1.setTextureUVOffset(0, 49).addCuboid(0, -19, -14, 1, 19, 14, 0, false);

		door_handrail = createModelPart();
		door_handrail.setPivot(0, 24, 0);
		door_handrail.setTextureUVOffset(319, 0).addCuboid(0, -34.6F, 0, 0, 5, 0, 0.2F, false);
		door_handrail.setTextureUVOffset(319, 24).addCuboid(0, -11.2F, 0, 0, 11, 0, 0.2F, false);

		pole_bottom_diagonal_3_r1 = createModelPart();
		pole_bottom_diagonal_3_r1.setPivot(-0.1712F, -12.5316F, 0.1712F);
		door_handrail.addChild(pole_bottom_diagonal_3_r1);
		setRotationAngle(pole_bottom_diagonal_3_r1, -0.096F, -0.7854F, 0);
		pole_bottom_diagonal_3_r1.setTextureUVOffset(319, 0).addCuboid(0, -2.5F, 0, 0, 5, 0, 0.2F, false);

		pole_bottom_diagonal_2_r1 = createModelPart();
		pole_bottom_diagonal_2_r1.setPivot(0.2339F, -12.5316F, 0.0627F);
		door_handrail.addChild(pole_bottom_diagonal_2_r1);
		setRotationAngle(pole_bottom_diagonal_2_r1, -0.096F, 1.309F, 0);
		pole_bottom_diagonal_2_r1.setTextureUVOffset(319, 0).addCuboid(0, -2.5F, 0, 0, 5, 0, 0.2F, false);

		pole_bottom_diagonal_1_r1 = createModelPart();
		pole_bottom_diagonal_1_r1.setPivot(-0.0627F, -12.5316F, -0.2339F);
		door_handrail.addChild(pole_bottom_diagonal_1_r1);
		setRotationAngle(pole_bottom_diagonal_1_r1, -0.096F, -2.8798F, 0);
		pole_bottom_diagonal_1_r1.setTextureUVOffset(319, 0).addCuboid(0, -2.5F, 0, 0, 5, 0, 0.2F, false);

		pole_middle_3_r1 = createModelPart();
		pole_middle_3_r1.setPivot(0, 0, 0);
		door_handrail.addChild(pole_middle_3_r1);
		setRotationAngle(pole_middle_3_r1, 0, 1.309F, 0);
		pole_middle_3_r1.setTextureUVOffset(319, 11).addCuboid(0, -25.4F, 0.5F, 0, 10, 0, 0.2F, false);

		pole_middle_2_r1 = createModelPart();
		pole_middle_2_r1.setPivot(0, 0, 0);
		door_handrail.addChild(pole_middle_2_r1);
		setRotationAngle(pole_middle_2_r1, 0, -2.8798F, 0);
		pole_middle_2_r1.setTextureUVOffset(319, 11).addCuboid(0, -25.4F, 0.5F, 0, 10, 0, 0.2F, false);

		pole_middle_1_r1 = createModelPart();
		pole_middle_1_r1.setPivot(0, 0, 0);
		door_handrail.addChild(pole_middle_1_r1);
		setRotationAngle(pole_middle_1_r1, 0, -0.7854F, 0);
		pole_middle_1_r1.setTextureUVOffset(319, 11).addCuboid(0, -25.4F, 0.5F, 0, 10, 0, 0.2F, false);

		pole_top_diagonal_3_r1 = createModelPart();
		pole_top_diagonal_3_r1.setPivot(-0.1712F, -28.2684F, 0.1712F);
		door_handrail.addChild(pole_top_diagonal_3_r1);
		setRotationAngle(pole_top_diagonal_3_r1, 0.096F, -0.7854F, 0);
		pole_top_diagonal_3_r1.setTextureUVOffset(319, 0).addCuboid(0, -2.5F, 0, 0, 5, 0, 0.2F, false);

		pole_top_diagonal_2_r1 = createModelPart();
		pole_top_diagonal_2_r1.setPivot(0.2339F, -28.2684F, 0.0627F);
		door_handrail.addChild(pole_top_diagonal_2_r1);
		setRotationAngle(pole_top_diagonal_2_r1, 0.096F, 1.309F, 0);
		pole_top_diagonal_2_r1.setTextureUVOffset(319, 0).addCuboid(0, -2.5F, 0, 0, 5, 0, 0.2F, false);

		pole_top_diagonal_1_r1 = createModelPart();
		pole_top_diagonal_1_r1.setPivot(-0.0627F, -28.2684F, -0.2339F);
		door_handrail.addChild(pole_top_diagonal_1_r1);
		setRotationAngle(pole_top_diagonal_1_r1, 0.096F, -2.8798F, 0);
		pole_top_diagonal_1_r1.setTextureUVOffset(319, 0).addCuboid(0, -2.5F, 0, 0, 5, 0, 0.2F, false);

		door_exterior_1 = createModelPart();
		door_exterior_1.setPivot(0, 24, 0);
		door_exterior_1.setTextureUVOffset(100, 18).addCuboid(-21, 0, -18, 1, 4, 36, 0, false);

		door_leaf_r3 = createModelPart();
		door_leaf_r3.setPivot(-21, -14, 0);
		door_exterior_1.addChild(door_leaf_r3);
		setRotationAngle(door_leaf_r3, 0, 0, 0.1107F);
		door_leaf_r3.setTextureUVOffset(0, 243).addCuboid(0, -23, -14, 1, 5, 28, 0, false);

		door_left_exterior_1 = createModelPart();
		door_left_exterior_1.setPivot(0, 0, 0);
		door_exterior_1.addChild(door_left_exterior_1);
		door_left_exterior_1.setTextureUVOffset(159, 191).addCuboid(-21, -14, 0, 0, 14, 14, 0, false);

		door_left_top_r2 = createModelPart();
		door_left_top_r2.setPivot(-21, -14, 0);
		door_left_exterior_1.addChild(door_left_top_r2);
		setRotationAngle(door_left_top_r2, 0, 0, 0.1107F);
		door_left_top_r2.setTextureUVOffset(0, 182).addCuboid(0, -18, 0, 0, 18, 14, 0, false);

		door_right_exterior_1 = createModelPart();
		door_right_exterior_1.setPivot(0, 0, 0);
		door_exterior_1.addChild(door_right_exterior_1);
		door_right_exterior_1.setTextureUVOffset(96, 190).addCuboid(-21, -14, -14, 0, 14, 14, 0, false);

		door_right_top_r2 = createModelPart();
		door_right_top_r2.setPivot(-21, -14, 0);
		door_right_exterior_1.addChild(door_right_top_r2);
		setRotationAngle(door_right_top_r2, 0, 0, 0.1107F);
		door_right_top_r2.setTextureUVOffset(178, 44).addCuboid(0, -18, -14, 0, 18, 14, 0, false);

		door_exterior_2 = createModelPart();
		door_exterior_2.setPivot(0, 24, 0);
		door_exterior_2.setTextureUVOffset(100, 18).addCuboid(20, 0, -18, 1, 4, 36, 0, true);

		door_leaf_r4 = createModelPart();
		door_leaf_r4.setPivot(21, -14, 0);
		door_exterior_2.addChild(door_leaf_r4);
		setRotationAngle(door_leaf_r4, 0, 0, -0.1107F);
		door_leaf_r4.setTextureUVOffset(0, 243).addCuboid(-1, -23, -14, 1, 5, 28, 0, true);

		door_left_exterior_2 = createModelPart();
		door_left_exterior_2.setPivot(0, 0, 0);
		door_exterior_2.addChild(door_left_exterior_2);
		door_left_exterior_2.setTextureUVOffset(159, 191).addCuboid(21, -14, 0, 0, 14, 14, 0, true);

		door_left_top_r3 = createModelPart();
		door_left_top_r3.setPivot(21, -14, 0);
		door_left_exterior_2.addChild(door_left_top_r3);
		setRotationAngle(door_left_top_r3, 0, 0, -0.1107F);
		door_left_top_r3.setTextureUVOffset(0, 182).addCuboid(0, -18, 0, 0, 18, 14, 0, true);

		door_right_exterior_2 = createModelPart();
		door_right_exterior_2.setPivot(0, 0, 0);
		door_exterior_2.addChild(door_right_exterior_2);
		door_right_exterior_2.setTextureUVOffset(96, 190).addCuboid(21, -14, -14, 0, 14, 14, 0, true);

		door_right_top_r3 = createModelPart();
		door_right_top_r3.setPivot(21, -14, 0);
		door_right_exterior_2.addChild(door_right_top_r3);
		setRotationAngle(door_right_top_r3, 0, 0, -0.1107F);
		door_right_top_r3.setTextureUVOffset(178, 44).addCuboid(0, -18, -14, 0, 18, 14, 0, true);

		end = createModelPart();
		end.setPivot(0, 24, 0);
		end.setTextureUVOffset(154, 141).addCuboid(-20, 0, -12, 40, 1, 20, 0, false);
		end.setTextureUVOffset(28, 139).addCuboid(-20, -14, 5, 3, 14, 6, 0, false);
		end.setTextureUVOffset(178, 205).addCuboid(9.5F, -35, -12, 8, 35, 19, 0, false);
		end.setTextureUVOffset(124, 205).addCuboid(-17.5F, -35, -12, 8, 35, 19, 0, false);
		end.setTextureUVOffset(217, 118).addCuboid(-9.5F, -35, -12, 19, 3, 19, 0, false);

		upper_wall_2_r2 = createModelPart();
		upper_wall_2_r2.setPivot(-20, -14, 0);
		end.addChild(upper_wall_2_r2);
		setRotationAngle(upper_wall_2_r2, 0, 0, 0.1107F);
		upper_wall_2_r2.setTextureUVOffset(54, 139).addCuboid(0, -19, 6, 3, 19, 5, 0, false);

		upper_wall_1_r1 = createModelPart();
		upper_wall_1_r1.setPivot(20, -14, 0);
		end.addChild(upper_wall_1_r1);
		setRotationAngle(upper_wall_1_r1, 0, 3.1416F, -0.1107F);
		upper_wall_1_r1.setTextureUVOffset(236, 83).addCuboid(0, -19, -11, 3, 19, 5, 0, false);

		lower_wall_1_r1 = createModelPart();
		lower_wall_1_r1.setPivot(0, 0, 0);
		end.addChild(lower_wall_1_r1);
		setRotationAngle(lower_wall_1_r1, 0, 3.1416F, 0);
		lower_wall_1_r1.setTextureUVOffset(154, 141).addCuboid(-20, -14, -11, 3, 14, 6, 0, false);

		end_exterior = createModelPart();
		end_exterior.setPivot(0, 24, 0);
		end_exterior.setTextureUVOffset(156, 259).addCuboid(17, -14, -12, 3, 18, 22, 0, true);
		end_exterior.setTextureUVOffset(156, 259).addCuboid(-20, -14, -12, 3, 18, 22, 0, false);
		end_exterior.setTextureUVOffset(168, 85).addCuboid(9.5F, -34, -12, 10, 34, 0, 0, false);
		end_exterior.setTextureUVOffset(168, 85).addCuboid(-19.5F, -34, -12, 10, 34, 0, 0, true);
		end_exterior.setTextureUVOffset(246, 11).addCuboid(-18, -41, -12, 36, 7, 0, 0, false);

		door_leaf_2_r1 = createModelPart();
		door_leaf_2_r1.setPivot(21, -14, 0);
		end_exterior.addChild(door_leaf_2_r1);
		setRotationAngle(door_leaf_2_r1, 0, 3.1416F, -0.1107F);
		door_leaf_2_r1.setTextureUVOffset(236, 83).addCuboid(0, -23, -16, 1, 5, 28, 0, false);

		door_leaf_1_r1 = createModelPart();
		door_leaf_1_r1.setPivot(-21, -14, 0);
		end_exterior.addChild(door_leaf_1_r1);
		setRotationAngle(door_leaf_1_r1, 0, 0, 0.1107F);
		door_leaf_1_r1.setTextureUVOffset(238, 204).addCuboid(0, -23, -12, 1, 5, 28, 0, false);

		upper_wall_2_r3 = createModelPart();
		upper_wall_2_r3.setPivot(-20, -14, 0);
		end_exterior.addChild(upper_wall_2_r3);
		setRotationAngle(upper_wall_2_r3, 0, 0, 0.1107F);
		upper_wall_2_r3.setTextureUVOffset(0, 139).addCuboid(0, -22, -12, 3, 22, 22, 0, false);

		upper_wall_1_r2 = createModelPart();
		upper_wall_1_r2.setPivot(20, -14, 0);
		end_exterior.addChild(upper_wall_1_r2);
		setRotationAngle(upper_wall_1_r2, 0, 0, -0.1107F);
		upper_wall_1_r2.setTextureUVOffset(0, 139).addCuboid(-3, -22, -12, 3, 22, 22, 0, true);

		roof_end = createModelPart();
		roof_end.setPivot(0, 24, 0);


		inner_roof_1 = createModelPart();
		inner_roof_1.setPivot(-2, -33, 38);
		roof_end.addChild(inner_roof_1);
		inner_roof_1.setTextureUVOffset(225, 116).addCuboid(-16, 0, -31, 5, 1, 1, 0, false);
		inner_roof_1.setTextureUVOffset(79, 82).addCuboid(-9.0724F, -1.2978F, -31, 1, 0, 1, 0, false);
		inner_roof_1.setTextureUVOffset(115, 99).addCuboid(-2, -1.5F, -31, 4, 0, 1, 0, false);

		inner_roof_5_r3 = createModelPart();
		inner_roof_5_r3.setPivot(-1, -1.5F, -38);
		inner_roof_1.addChild(inner_roof_5_r3);
		setRotationAngle(inner_roof_5_r3, 0, 0, -0.0873F);
		inner_roof_5_r3.setTextureUVOffset(33, 0).addCuboid(-3, 0, 7, 3, 0, 1, 0, false);

		inner_roof_4_r3 = createModelPart();
		inner_roof_4_r3.setPivot(-4.9733F, -1.0649F, -38);
		inner_roof_1.addChild(inner_roof_4_r3);
		setRotationAngle(inner_roof_4_r3, 0, 0, -0.1745F);
		inner_roof_4_r3.setTextureUVOffset(123, 99).addCuboid(-1, 0, 7, 2, 0, 1, 0, false);

		inner_roof_2_r3 = createModelPart();
		inner_roof_2_r3.setPivot(-11, 1, -38);
		inner_roof_1.addChild(inner_roof_2_r3);
		setRotationAngle(inner_roof_2_r3, 0, 0, -0.8727F);
		inner_roof_2_r3.setTextureUVOffset(33, 49).addCuboid(0, 0, 7, 3, 0, 1, 0, false);

		inner_roof_2 = createModelPart();
		inner_roof_2.setPivot(2, -33, 38);
		roof_end.addChild(inner_roof_2);
		inner_roof_2.setTextureUVOffset(225, 116).addCuboid(11, 0, -31, 5, 1, 1, 0, true);
		inner_roof_2.setTextureUVOffset(79, 82).addCuboid(8.0724F, -1.2978F, -31, 1, 0, 1, 0, true);
		inner_roof_2.setTextureUVOffset(115, 99).addCuboid(-2, -1.5F, -31, 4, 0, 1, 0, true);

		inner_roof_6_r1 = createModelPart();
		inner_roof_6_r1.setPivot(1, -1.5F, -38);
		inner_roof_2.addChild(inner_roof_6_r1);
		setRotationAngle(inner_roof_6_r1, 0, 0, 0.0873F);
		inner_roof_6_r1.setTextureUVOffset(33, 0).addCuboid(0, 0, 7, 3, 0, 1, 0, true);

		inner_roof_5_r4 = createModelPart();
		inner_roof_5_r4.setPivot(4.9733F, -1.0649F, -38);
		inner_roof_2.addChild(inner_roof_5_r4);
		setRotationAngle(inner_roof_5_r4, 0, 0, 0.1745F);
		inner_roof_5_r4.setTextureUVOffset(123, 99).addCuboid(-1, 0, 7, 2, 0, 1, 0, true);

		inner_roof_3_r1 = createModelPart();
		inner_roof_3_r1.setPivot(11, 1, -38);
		inner_roof_2.addChild(inner_roof_3_r1);
		setRotationAngle(inner_roof_3_r1, 0, 0, 0.8727F);
		inner_roof_3_r1.setTextureUVOffset(33, 49).addCuboid(-3, 0, 7, 3, 0, 1, 0, true);

		roof_end_exterior = createModelPart();
		roof_end_exterior.setPivot(0, 24, 0);
		roof_end_exterior.setTextureUVOffset(0, 49).addCuboid(-8, -42, 0, 16, 2, 48, 0, false);

		vent_2_r1 = createModelPart();
		vent_2_r1.setPivot(-8, -42, 0);
		roof_end_exterior.addChild(vent_2_r1);
		setRotationAngle(vent_2_r1, 0, 0, -0.3491F);
		vent_2_r1.setTextureUVOffset(80, 91).addCuboid(-9, 0, 0, 9, 2, 48, 0, false);

		vent_1_r1 = createModelPart();
		vent_1_r1.setPivot(8, -42, 0);
		roof_end_exterior.addChild(vent_1_r1);
		setRotationAngle(vent_1_r1, 0, 0, 0.3491F);
		vent_1_r1.setTextureUVOffset(138, 0).addCuboid(0, 0, 0, 9, 2, 48, 0, false);

		outer_roof_1 = createModelPart();
		outer_roof_1.setPivot(0, 0, 0);
		roof_end_exterior.addChild(outer_roof_1);
		outer_roof_1.setTextureUVOffset(247, 162).addCuboid(-6, -41, -12, 6, 1, 20, 0, false);

		outer_roof_5_r2 = createModelPart();
		outer_roof_5_r2.setPivot(-6, -41, 0);
		outer_roof_1.addChild(outer_roof_5_r2);
		setRotationAngle(outer_roof_5_r2, 0, 0, -0.1745F);
		outer_roof_5_r2.setTextureUVOffset(87, 243).addCuboid(-8, 0, -12, 8, 1, 20, 0, false);

		outer_roof_4_r2 = createModelPart();
		outer_roof_4_r2.setPivot(-15.3605F, -38.1778F, -2);
		outer_roof_1.addChild(outer_roof_4_r2);
		setRotationAngle(outer_roof_4_r2, 0, 0, -0.5236F);
		outer_roof_4_r2.setTextureUVOffset(31, 243).addCuboid(-2, -0.5F, -10, 4, 1, 20, 0, false);

		outer_roof_3_r2 = createModelPart();
		outer_roof_3_r2.setPivot(-17.4096F, -36.4948F, -2);
		outer_roof_1.addChild(outer_roof_3_r2);
		setRotationAngle(outer_roof_3_r2, 0, 0, -1.0472F);
		outer_roof_3_r2.setTextureUVOffset(246, 253).addCuboid(-1, -0.5F, -10, 2, 1, 20, 0, false);

		outer_roof_2 = createModelPart();
		outer_roof_2.setPivot(0, 0, 0);
		roof_end_exterior.addChild(outer_roof_2);
		outer_roof_2.setTextureUVOffset(247, 162).addCuboid(0, -41, -12, 6, 1, 20, 0, true);

		outer_roof_6_r1 = createModelPart();
		outer_roof_6_r1.setPivot(6, -41, 0);
		outer_roof_2.addChild(outer_roof_6_r1);
		setRotationAngle(outer_roof_6_r1, 0, 0, 0.1745F);
		outer_roof_6_r1.setTextureUVOffset(87, 243).addCuboid(0, 0, -12, 8, 1, 20, 0, true);

		outer_roof_5_r3 = createModelPart();
		outer_roof_5_r3.setPivot(15.3605F, -38.1778F, -2);
		outer_roof_2.addChild(outer_roof_5_r3);
		setRotationAngle(outer_roof_5_r3, 0, 0, 0.5236F);
		outer_roof_5_r3.setTextureUVOffset(31, 243).addCuboid(-2, -0.5F, -10, 4, 1, 20, 0, true);

		outer_roof_4_r3 = createModelPart();
		outer_roof_4_r3.setPivot(17.4096F, -36.4948F, -2);
		outer_roof_2.addChild(outer_roof_4_r3);
		setRotationAngle(outer_roof_4_r3, 0, 0, 1.0472F);
		outer_roof_4_r3.setTextureUVOffset(246, 253).addCuboid(-1, -0.5F, -10, 2, 1, 20, 0, true);

		roof_window_light = createModelPart();
		roof_window_light.setPivot(0, 24, 0);


		light_2_r1 = createModelPart();
		light_2_r1.setPivot(-10.1225F, -34.1864F, 0);
		roof_window_light.addChild(light_2_r1);
		setRotationAngle(light_2_r1, 0, 0, 1.2217F);
		light_2_r1.setTextureUVOffset(80, 0).addCuboid(-0.5F, 0, -24, 1, 0, 48, 0, false);

		light_1_r1 = createModelPart();
		light_1_r1.setPivot(-8.9544F, -33.8041F, 0);
		roof_window_light.addChild(light_1_r1);
		setRotationAngle(light_1_r1, 0, 0, -0.0873F);
		light_1_r1.setTextureUVOffset(74, 0).addCuboid(-1, 0, -24, 2, 0, 48, 0, false);

		roof_door_light = createModelPart();
		roof_door_light.setPivot(0, 24, 0);


		light_3_r1 = createModelPart();
		light_3_r1.setPivot(-10.1225F, -34.1864F, 0);
		roof_door_light.addChild(light_3_r1);
		setRotationAngle(light_3_r1, 0, 0, 1.2217F);
		light_3_r1.setTextureUVOffset(96, 8).addCuboid(-0.5F, 0, -16, 1, 0, 32, 0, false);

		light_2_r2 = createModelPart();
		light_2_r2.setPivot(-8.9544F, -33.8041F, 0);
		roof_door_light.addChild(light_2_r2);
		setRotationAngle(light_2_r2, 0, 0, -0.0873F);
		light_2_r2.setTextureUVOffset(90, 8).addCuboid(-1, 0, -16, 2, 0, 32, 0, false);

		roof_end_light = createModelPart();
		roof_end_light.setPivot(0, 24, 0);


		light_3_r2 = createModelPart();
		light_3_r2.setPivot(10.1225F, -34.1864F, 0);
		roof_end_light.addChild(light_3_r2);
		setRotationAngle(light_3_r2, 0, 0, -1.2217F);
		light_3_r2.setTextureUVOffset(127, 35).addCuboid(-0.5F, 0, 7, 1, 0, 1, 0, true);

		light_2_r3 = createModelPart();
		light_2_r3.setPivot(8.9544F, -33.8041F, 0);
		roof_end_light.addChild(light_2_r3);
		setRotationAngle(light_2_r3, 0, 0, 0.0873F);
		light_2_r3.setTextureUVOffset(121, 35).addCuboid(-1, 0, 7, 2, 0, 1, 0, true);

		light_3_r3 = createModelPart();
		light_3_r3.setPivot(-10.1225F, -34.1864F, 0);
		roof_end_light.addChild(light_3_r3);
		setRotationAngle(light_3_r3, 0, 0, 1.2217F);
		light_3_r3.setTextureUVOffset(127, 35).addCuboid(-0.5F, 0, 7, 1, 0, 1, 0, false);

		light_2_r4 = createModelPart();
		light_2_r4.setPivot(-8.9544F, -33.8041F, 0);
		roof_end_light.addChild(light_2_r4);
		setRotationAngle(light_2_r4, 0, 0, -0.0873F);
		light_2_r4.setTextureUVOffset(121, 35).addCuboid(-1, 0, 7, 2, 0, 1, 0, false);

		head = createModelPart();
		head.setPivot(0, 24, 0);
		head.setTextureUVOffset(80, 58).addCuboid(-18, 0, -18, 36, 1, 26, 0, false);
		head.setTextureUVOffset(212, 162).addCuboid(-20, -14, -17, 3, 14, 28, 0, false);
		head.setTextureUVOffset(266, 83).addCuboid(-17, -14, -2, 4, 14, 13, 0, false);

		upper_wall_2_r4 = createModelPart();
		upper_wall_2_r4.setPivot(-20, -14, 0);
		head.addChild(upper_wall_2_r4);
		setRotationAngle(upper_wall_2_r4, 0, 0, 0.1107F);
		upper_wall_2_r4.setTextureUVOffset(0, 196).addCuboid(0, -19, -17, 3, 19, 28, 0, false);
		upper_wall_2_r4.setTextureUVOffset(0, 0).addCuboid(3, -21, -2, 4, 21, 13, 0, false);

		upper_wall_1_r3 = createModelPart();
		upper_wall_1_r3.setPivot(20, -14, 0);
		head.addChild(upper_wall_1_r3);
		setRotationAngle(upper_wall_1_r3, 0, 3.1416F, -0.1107F);
		upper_wall_1_r3.setTextureUVOffset(62, 196).addCuboid(0, -19, -11, 3, 19, 28, 0, false);

		lower_wall_1_r2 = createModelPart();
		lower_wall_1_r2.setPivot(0, 0, 0);
		head.addChild(lower_wall_1_r2);
		setRotationAngle(lower_wall_1_r2, 0, 3.1416F, 0);
		lower_wall_1_r2.setTextureUVOffset(204, 231).addCuboid(-20, -14, -11, 3, 14, 28, 0, false);

		ceiling = createModelPart();
		ceiling.setPivot(0, 0, 0);
		head.addChild(ceiling);
		ceiling.setTextureUVOffset(0, 183).addCuboid(-15.165F, -34.3F, -15, 8, 4, 4, 0, true);
		ceiling.setTextureUVOffset(208, 273).addCuboid(-7.5F, -32, -17, 2, 32, 2, 0, true);
		ceiling.setTextureUVOffset(208, 273).addCuboid(5.5F, -32, -17, 2, 32, 2, 0, true);

		panel_9_r1 = createModelPart();
		panel_9_r1.setPivot(7.165F, -30.3F, -11);
		ceiling.addChild(panel_9_r1);
		setRotationAngle(panel_9_r1, 0, 0, 0.6109F);
		panel_9_r1.setTextureUVOffset(0, 49).addCuboid(-2, -4, -4, 2, 4, 4, 0, true);

		panel_8_r1 = createModelPart();
		panel_8_r1.setPivot(22.165F, 0.7F, 0);
		ceiling.addChild(panel_8_r1);
		setRotationAngle(panel_8_r1, 0, 0, 0);
		panel_8_r1.setTextureUVOffset(0, 183).addCuboid(-15, -35, -15, 8, 4, 4, 0, true);

		panel_7_r1 = createModelPart();
		panel_7_r1.setPivot(0, 0.7F, 2);
		ceiling.addChild(panel_7_r1);
		setRotationAngle(panel_7_r1, 0, 0, 0);
		panel_7_r1.setTextureUVOffset(80, 90).addCuboid(-7, -34, -17, 14, 2, 4, 0, true);

		panel_6_r1 = createModelPart();
		panel_6_r1.setPivot(-7.165F, -30.3F, 0);
		ceiling.addChild(panel_6_r1);
		setRotationAngle(panel_6_r1, 0, 0, -0.6109F);
		panel_6_r1.setTextureUVOffset(0, 49).addCuboid(0, -4, -15, 2, 4, 4, 0, true);

		panel_4_r1 = createModelPart();
		panel_4_r1.setPivot(-13.5F, -31, 0);
		ceiling.addChild(panel_4_r1);
		setRotationAngle(panel_4_r1, 0, 0, -0.7418F);
		panel_4_r1.setTextureUVOffset(0, 214).addCuboid(0, -1, -11, 3, 1, 9, 0, false);

		panel_3_r1 = createModelPart();
		panel_3_r1.setPivot(-1.0805F, 1.0256F, 2);
		ceiling.addChild(panel_3_r1);
		setRotationAngle(panel_3_r1, 0, 0, 0);
		panel_3_r1.setTextureUVOffset(250, 42).addCuboid(-14.4195F, -33.0256F, -13, 2, 1, 9, 0, false);

		panel_2_r1 = createModelPart();
		panel_2_r1.setPivot(13.5F, -31, 0);
		ceiling.addChild(panel_2_r1);
		setRotationAngle(panel_2_r1, 0, 0, 0.7418F);
		panel_2_r1.setTextureUVOffset(250, 42).addCuboid(-3, -1, -11, 3, 1, 22, 0, false);

		panel_1_r1 = createModelPart();
		panel_1_r1.setPivot(0.1555F, 1.0256F, -2);
		ceiling.addChild(panel_1_r1);
		setRotationAngle(panel_1_r1, 0, 0, 0);
		panel_1_r1.setTextureUVOffset(138, 18).addCuboid(13.3445F, -33.0256F, -9, 2, 1, 22, 0, false);

		main_r1 = createModelPart();
		main_r1.setPivot(0, 1, -2);
		ceiling.addChild(main_r1);
		setRotationAngle(main_r1, 0, 0, 0);
		main_r1.setTextureUVOffset(54, 141).addCuboid(-12, -35, -9, 24, 1, 22, 0, true);

		emergency_door = createModelPart();
		emergency_door.setPivot(0, 0, 0);
		head.addChild(emergency_door);
		emergency_door.setTextureUVOffset(238, 246).addCuboid(-5.5F, -11, -18, 11, 11, 1, 0, false);

		upper_r1 = createModelPart();
		upper_r1.setPivot(0, -11, -17);
		emergency_door.addChild(upper_r1);
		setRotationAngle(upper_r1, -0.0873F, 0, 0);
		upper_r1.setTextureUVOffset(212, 162).addCuboid(-5.5F, -22, -1, 11, 22, 1, 0, false);

		left_c_panel = createModelPart();
		left_c_panel.setPivot(0, 0, 0);
		head.addChild(left_c_panel);


		panel_r1 = createModelPart();
		panel_r1.setPivot(0, 0, 0);
		left_c_panel.addChild(panel_r1);
		setRotationAngle(panel_r1, -2.5744F, 0, 3.1416F);
		panel_r1.setTextureUVOffset(246, 18).addCuboid(-17, -4.15F, 15.65F, 10, 3, 6, 0, false);

		base_r1 = createModelPart();
		base_r1.setPivot(0, 0, 0);
		left_c_panel.addChild(base_r1);
		setRotationAngle(base_r1, 0, 3.1416F, 0);
		base_r1.setTextureUVOffset(146, 259).addCuboid(-17, -12, 11, 10, 12, 6, 0, false);

		right_c_panel = createModelPart();
		right_c_panel.setPivot(0, 0, 0);
		head.addChild(right_c_panel);


		panel_r2 = createModelPart();
		panel_r2.setPivot(0, 0, 0);
		right_c_panel.addChild(panel_r2);
		setRotationAngle(panel_r2, -2.5744F, 0, -3.1416F);
		panel_r2.setTextureUVOffset(192, 118).addCuboid(7, -4.15F, 15.65F, 10, 3, 6, 0, false);

		base_r2 = createModelPart();
		base_r2.setPivot(0, 0, 0);
		right_c_panel.addChild(base_r2);
		setRotationAngle(base_r2, 0, -3.1416F, 0);
		base_r2.setTextureUVOffset(213, 205).addCuboid(7, -12, 11, 10, 12, 6, 0, false);

		handrail = createModelPart();
		handrail.setPivot(0, 0, 0);
		head.addChild(handrail);


		wall = createModelPart();
		wall.setPivot(0, 0, 2);
		handrail.addChild(wall);


		handrail_4_r1 = createModelPart();
		handrail_4_r1.setPivot(14.8F, -11.2F, -7.2F);
		wall.addChild(handrail_4_r1);
		setRotationAngle(handrail_4_r1, 1.5708F, -0.4363F, 0);
		handrail_4_r1.setTextureUVOffset(319, 0).addCuboid(0.2F, -1.2F, -0.2F, 0, 1, 0, 0.2F, false);

		handrail_3_r1 = createModelPart();
		handrail_3_r1.setPivot(16.5638F, -11, 2.7947F);
		wall.addChild(handrail_3_r1);
		setRotationAngle(handrail_3_r1, -1.5708F, 1.1345F, 0);
		handrail_3_r1.setTextureUVOffset(319, 0).addCuboid(0, -1, 0, 0, 2, 0, 0.2F, false);

		handrail_2_r1 = createModelPart();
		handrail_2_r1.setPivot(14.8F, -11.2F, 1.2F);
		wall.addChild(handrail_2_r1);
		setRotationAngle(handrail_2_r1, -1.5708F, 0.4363F, 0);
		handrail_2_r1.setTextureUVOffset(319, 0).addCuboid(0.2F, -1.2F, 0.2F, 0, 1, 0, 0.2F, false);

		handrail_5_r1 = createModelPart();
		handrail_5_r1.setPivot(16.5638F, -11, -8.7947F);
		wall.addChild(handrail_5_r1);
		setRotationAngle(handrail_5_r1, 1.5708F, -1.1345F, 0);
		handrail_5_r1.setTextureUVOffset(319, 0).addCuboid(0, -1, 0, 0, 2, 0, 0.2F, false);

		handrail_1_r1 = createModelPart();
		handrail_1_r1.setPivot(0, 0, -2);
		wall.addChild(handrail_1_r1);
		setRotationAngle(handrail_1_r1, -1.5708F, 0, 0);
		handrail_1_r1.setTextureUVOffset(319, 11).addCuboid(15, -3, -11, 0, 8, 0, 0.2F, false);

		ceiling2 = createModelPart();
		ceiling2.setPivot(1, 0, 2);
		handrail.addChild(ceiling2);
		ceiling2.setTextureUVOffset(0, 0).addCuboid(-9, -31.5F, 0, 2, 4, 0, 0, false);
		ceiling2.setTextureUVOffset(0, 0).addCuboid(-9, -31.5F, -6, 2, 4, 0, 0, false);

		handrail_7_r1 = createModelPart();
		handrail_7_r1.setPivot(-19, -16.0275F, -4.0905F);
		ceiling2.addChild(handrail_7_r1);
		setRotationAngle(handrail_7_r1, 1.5708F, 1.1345F, 1.5708F);
		handrail_7_r1.setTextureUVOffset(319, 0).addCuboid(-2.725F, -17.975F, 11, 0, 2, 0, 0.2F, false);

		handrail_4_r2 = createModelPart();
		handrail_4_r2.setPivot(-7.8F, -30.8F, 1.2F);
		ceiling2.addChild(handrail_4_r2);
		setRotationAngle(handrail_4_r2, -1.5708F, -0.4363F, 1.5708F);
		handrail_4_r2.setTextureUVOffset(319, 0).addCuboid(-0.2F, -1.2F, 0.2F, 0, 1, 0, 0.2F, false);

		handrail_5_r2 = createModelPart();
		handrail_5_r2.setPivot(-19, -16.0275F, -1.9095F);
		ceiling2.addChild(handrail_5_r2);
		setRotationAngle(handrail_5_r2, -1.5708F, -1.1345F, 1.5708F);
		handrail_5_r2.setTextureUVOffset(319, 0).addCuboid(-2.725F, -17.975F, -11, 0, 2, 0, 0.2F, false);

		handrail_6_r1 = createModelPart();
		handrail_6_r1.setPivot(-7.8F, -30.8F, -7.2F);
		ceiling2.addChild(handrail_6_r1);
		setRotationAngle(handrail_6_r1, 1.5708F, 0.4363F, 1.5708F);
		handrail_6_r1.setTextureUVOffset(319, 0).addCuboid(-0.2F, -1.2F, -0.2F, 0, 1, 0, 0.2F, false);

		handrail_3_r2 = createModelPart();
		handrail_3_r2.setPivot(-1, 0, -2);
		ceiling2.addChild(handrail_3_r2);
		setRotationAngle(handrail_3_r2, -1.5708F, 0, 0);
		handrail_3_r2.setTextureUVOffset(319, 0).addCuboid(-7, -3, -31, 0, 8, 0, 0.2F, false);

		ceiling3 = createModelPart();
		ceiling3.setPivot(-1, 0, 2);
		handrail.addChild(ceiling3);
		ceiling3.setTextureUVOffset(0, 0).addCuboid(7, -31.5F, 0, 2, 4, 0, 0, true);
		ceiling3.setTextureUVOffset(0, 0).addCuboid(7, -31.5F, -6, 2, 4, 0, 0, true);

		handrail_8_r1 = createModelPart();
		handrail_8_r1.setPivot(19, -16.0275F, -4.0905F);
		ceiling3.addChild(handrail_8_r1);
		setRotationAngle(handrail_8_r1, 1.5708F, -1.1345F, -1.5708F);
		handrail_8_r1.setTextureUVOffset(319, 0).addCuboid(2.725F, -17.975F, 11, 0, 2, 0, 0.2F, true);

		handrail_5_r3 = createModelPart();
		handrail_5_r3.setPivot(7.8F, -30.8F, 1.2F);
		ceiling3.addChild(handrail_5_r3);
		setRotationAngle(handrail_5_r3, -1.5708F, 0.4363F, -1.5708F);
		handrail_5_r3.setTextureUVOffset(319, 0).addCuboid(0.2F, -1.2F, 0.2F, 0, 1, 0, 0.2F, true);

		handrail_6_r2 = createModelPart();
		handrail_6_r2.setPivot(19, -16.0275F, -1.9095F);
		ceiling3.addChild(handrail_6_r2);
		setRotationAngle(handrail_6_r2, -1.5708F, 1.1345F, -1.5708F);
		handrail_6_r2.setTextureUVOffset(319, 0).addCuboid(2.725F, -17.975F, -11, 0, 2, 0, 0.2F, true);

		handrail_7_r2 = createModelPart();
		handrail_7_r2.setPivot(7.8F, -30.8F, -7.2F);
		ceiling3.addChild(handrail_7_r2);
		setRotationAngle(handrail_7_r2, 1.5708F, -0.4363F, -1.5708F);
		handrail_7_r2.setTextureUVOffset(319, 0).addCuboid(0.2F, -1.2F, -0.2F, 0, 1, 0, 0.2F, true);

		handrail_4_r3 = createModelPart();
		handrail_4_r3.setPivot(1, 0, -2);
		ceiling3.addChild(handrail_4_r3);
		setRotationAngle(handrail_4_r3, -1.5708F, 0, 0);
		handrail_4_r3.setTextureUVOffset(319, 0).addCuboid(7, -3, -31, 0, 8, 0, 0.2F, true);

		head_exterior = createModelPart();
		head_exterior.setPivot(0, 24, 0);
		head_exterior.setTextureUVOffset(0, 14).addCuboid(20, -14, -10, 0, 14, 20, 0, false);
		head_exterior.setTextureUVOffset(0, 14).addCuboid(-20, -14, -10, 0, 14, 20, 0, false);

		upper_wall_2_r5 = createModelPart();
		upper_wall_2_r5.setPivot(-20, -14, 0);
		head_exterior.addChild(upper_wall_2_r5);
		setRotationAngle(upper_wall_2_r5, 0, 0, 0.1107F);
		upper_wall_2_r5.setTextureUVOffset(154, 142).addCuboid(0, -22, -10, 0, 22, 20, 0, false);

		upper_wall_1_r4 = createModelPart();
		upper_wall_1_r4.setPivot(20, -14, 0);
		head_exterior.addChild(upper_wall_1_r4);
		setRotationAngle(upper_wall_1_r4, 0, 0, -0.1107F);
		upper_wall_1_r4.setTextureUVOffset(154, 142).addCuboid(0, -22, -10, 0, 22, 20, 0, false);

		door_leaf_4_r1 = createModelPart();
		door_leaf_4_r1.setPivot(21, -14, -10);
		head_exterior.addChild(door_leaf_4_r1);
		setRotationAngle(door_leaf_4_r1, 0, 0.3316F, -0.1107F);
		door_leaf_4_r1.setTextureUVOffset(0, 82).addCuboid(-1, -23, -5, 1, 5, 5, 0, true);

		door_leaf_1_r2 = createModelPart();
		door_leaf_1_r2.setPivot(21, -14, 0);
		head_exterior.addChild(door_leaf_1_r2);
		setRotationAngle(door_leaf_1_r2, 0, 3.1416F, -0.1107F);
		door_leaf_1_r2.setTextureUVOffset(146, 102).addCuboid(0, -23, -10, 1, 5, 20, 0, true);

		door_leaf_5_r1 = createModelPart();
		door_leaf_5_r1.setPivot(-21, -14, -10);
		head_exterior.addChild(door_leaf_5_r1);
		setRotationAngle(door_leaf_5_r1, 0, -0.3316F, 0.1107F);
		door_leaf_5_r1.setTextureUVOffset(0, 82).addCuboid(0, -23, -5, 1, 5, 5, 0, false);

		door_leaf_2_r2 = createModelPart();
		door_leaf_2_r2.setPivot(-21, -14, 0);
		head_exterior.addChild(door_leaf_2_r2);
		setRotationAngle(door_leaf_2_r2, 0, -3.1416F, 0.1107F);
		door_leaf_2_r2.setTextureUVOffset(146, 102).addCuboid(-1, -23, -10, 1, 5, 20, 0, false);

		front = createModelPart();
		front.setPivot(0, 0, 0);
		head_exterior.addChild(front);
		front.setTextureUVOffset(238, 237).addCuboid(-19, -10, -19.5F, 38, 9, 0, 0, false);

		front_panel_4_r1 = createModelPart();
		front_panel_4_r1.setPivot(0, -35.2365F, -16.3334F);
		front.addChild(front_panel_4_r1);
		setRotationAngle(front_panel_4_r1, -0.2618F, 0, 0);
		front_panel_4_r1.setTextureUVOffset(246, 0).addCuboid(-17.5F, -5.5F, 0, 35, 11, 0, 0, false);

		front_panel_3_r1 = createModelPart();
		front_panel_3_r1.setPivot(0, -10, -19.5F);
		front.addChild(front_panel_3_r1);
		setRotationAngle(front_panel_3_r1, -0.0873F, 0, 0);
		front_panel_3_r1.setTextureUVOffset(54, 164).addCuboid(-19, -20, 0, 38, 20, 0, 0, false);

		front_panel_1_r1 = createModelPart();
		front_panel_1_r1.setPivot(0, -1, -19.5F);
		front.addChild(front_panel_1_r1);
		setRotationAngle(front_panel_1_r1, 0.3054F, 0, 0);
		front_panel_1_r1.setTextureUVOffset(204, 33).addCuboid(-19, 0, 0, 38, 9, 0, 0, false);

		side_1 = createModelPart();
		side_1.setPivot(0, 0, 0);
		front.addChild(side_1);


		front_side_bottom_3_r1 = createModelPart();
		front_side_bottom_3_r1.setPivot(20, 0, -10);
		side_1.addChild(front_side_bottom_3_r1);
		setRotationAngle(front_side_bottom_3_r1, 0, 0, 0.1745F);
		front_side_bottom_3_r1.setTextureUVOffset(178, 60).addCuboid(0, 0, 0, 0, 8, 16, 0, true);

		front_side_bottom_1_r1 = createModelPart();
		front_side_bottom_1_r1.setPivot(-20, 0, -10);
		side_1.addChild(front_side_bottom_1_r1);
		setRotationAngle(front_side_bottom_1_r1, 0, -0.1309F, -0.1745F);
		front_side_bottom_1_r1.setTextureUVOffset(124, 136).addCuboid(0, 0, -13, 0, 8, 13, 0, false);

		front_side_lower_1_r1 = createModelPart();
		front_side_lower_1_r1.setPivot(-20, 0, -10);
		side_1.addChild(front_side_lower_1_r1);
		setRotationAngle(front_side_lower_1_r1, 0, -0.1309F, 0);
		front_side_lower_1_r1.setTextureUVOffset(0, 128).addCuboid(0, -14, -11, 0, 14, 11, 0, false);

		front_side_upper_1_r1 = createModelPart();
		front_side_upper_1_r1.setPivot(-20, -14, -10);
		side_1.addChild(front_side_upper_1_r1);
		setRotationAngle(front_side_upper_1_r1, 0, -0.1309F, 0.1107F);
		front_side_upper_1_r1.setTextureUVOffset(82, 47).addCuboid(0, -23, -11, 0, 23, 11, 0, false);

		side_2 = createModelPart();
		side_2.setPivot(-21, 0, 9);
		front.addChild(side_2);


		front_side_upper_2_r1 = createModelPart();
		front_side_upper_2_r1.setPivot(41, -14, -19);
		side_2.addChild(front_side_upper_2_r1);
		setRotationAngle(front_side_upper_2_r1, 0, 0.1309F, -0.1107F);
		front_side_upper_2_r1.setTextureUVOffset(82, 47).addCuboid(0, -23, -11, 0, 23, 11, 0, true);

		front_side_lower_2_r1 = createModelPart();
		front_side_lower_2_r1.setPivot(41, 0, -19);
		side_2.addChild(front_side_lower_2_r1);
		setRotationAngle(front_side_lower_2_r1, 0, 0.1309F, 0);
		front_side_lower_2_r1.setTextureUVOffset(0, 128).addCuboid(0, -14, -11, 0, 14, 11, 0, true);

		front_side_bottom_4_r1 = createModelPart();
		front_side_bottom_4_r1.setPivot(1, 0, -19);
		side_2.addChild(front_side_bottom_4_r1);
		setRotationAngle(front_side_bottom_4_r1, 0, 0, -0.1745F);
		front_side_bottom_4_r1.setTextureUVOffset(178, 60).addCuboid(0, 0, 0, 0, 8, 16, 0, false);

		front_side_bottom_2_r1 = createModelPart();
		front_side_bottom_2_r1.setPivot(41, 0, -19);
		side_2.addChild(front_side_bottom_2_r1);
		setRotationAngle(front_side_bottom_2_r1, 0, 0.1309F, 0.1745F);
		front_side_bottom_2_r1.setTextureUVOffset(124, 136).addCuboid(0, 0, -13, 0, 8, 13, 0, true);

		roof = createModelPart();
		roof.setPivot(-16.7054F, -37.098F, 5);
		head_exterior.addChild(roof);
		roof.setTextureUVOffset(246, 162).addCuboid(10.7054F, -3.902F, -16, 6, 1, 21, 0, false);
		roof.setTextureUVOffset(246, 162).addCuboid(16.7054F, -3.902F, -16, 6, 1, 21, 0, true);

		outer_roof_6_r2 = createModelPart();
		outer_roof_6_r2.setPivot(22.7054F, -3.902F, -5);
		roof.addChild(outer_roof_6_r2);
		setRotationAngle(outer_roof_6_r2, 0, 0, 0.1745F);
		outer_roof_6_r2.setTextureUVOffset(86, 243).addCuboid(0, 0, -11, 8, 1, 21, 0, true);

		outer_roof_5_r4 = createModelPart();
		outer_roof_5_r4.setPivot(32.0659F, -1.0798F, -5.5F);
		roof.addChild(outer_roof_5_r4);
		setRotationAngle(outer_roof_5_r4, 0, 0, 0.5236F);
		outer_roof_5_r4.setTextureUVOffset(30, 243).addCuboid(-2, -0.5F, -10.5F, 4, 1, 21, 0, true);

		outer_roof_4_r4 = createModelPart();
		outer_roof_4_r4.setPivot(34.115F, 0.6032F, -5.5F);
		roof.addChild(outer_roof_4_r4);
		setRotationAngle(outer_roof_4_r4, 0, 0, 1.0472F);
		outer_roof_4_r4.setTextureUVOffset(245, 274).addCuboid(-1, -0.5F, -10.5F, 2, 1, 21, 0, true);

		outer_roof_5_r5 = createModelPart();
		outer_roof_5_r5.setPivot(10.7054F, -3.902F, -5);
		roof.addChild(outer_roof_5_r5);
		setRotationAngle(outer_roof_5_r5, 0, 0, -0.1745F);
		outer_roof_5_r5.setTextureUVOffset(86, 243).addCuboid(-8, 0, -11, 8, 1, 21, 0, false);

		outer_roof_4_r5 = createModelPart();
		outer_roof_4_r5.setPivot(1.3449F, -1.0798F, -5.5F);
		roof.addChild(outer_roof_4_r5);
		setRotationAngle(outer_roof_4_r5, 0, 0, -0.5236F);
		outer_roof_4_r5.setTextureUVOffset(30, 243).addCuboid(-2, -0.5F, -10.5F, 4, 1, 21, 0, false);

		outer_roof_3_r3 = createModelPart();
		outer_roof_3_r3.setPivot(-0.7041F, 0.6032F, -5.5F);
		roof.addChild(outer_roof_3_r3);
		setRotationAngle(outer_roof_3_r3, 0, 0, -1.0472F);
		outer_roof_3_r3.setTextureUVOffset(245, 274).addCuboid(-1, -0.5F, -10.5F, 2, 1, 21, 0, false);

		vent_top_r1 = createModelPart();
		vent_top_r1.setPivot(16.7054F, 37.098F, 48);
		roof.addChild(vent_top_r1);
		setRotationAngle(vent_top_r1, -3.1416F, 0, 3.1416F);
		vent_top_r1.setTextureUVOffset(0, 49).addCuboid(-8, -42, 0, 16, 2, 48, 0, false);

		vent_2_r2 = createModelPart();
		vent_2_r2.setPivot(24.7054F, -4.902F, 48);
		roof.addChild(vent_2_r2);
		setRotationAngle(vent_2_r2, -3.1416F, 0, -2.7925F);
		vent_2_r2.setTextureUVOffset(80, 91).addCuboid(-9, 0, 0, 9, 2, 48, 0, false);

		vent_1_r2 = createModelPart();
		vent_1_r2.setPivot(8.7054F, -4.902F, 48);
		roof.addChild(vent_1_r2);
		setRotationAngle(vent_1_r2, 3.1416F, 0, 2.7925F);
		vent_1_r2.setTextureUVOffset(138, 0).addCuboid(0, 0, 0, 9, 2, 48, 0, false);

		outer_roof_6_r3 = createModelPart();
		outer_roof_6_r3.setPivot(34.1586F, 1.7327F, -18.8187F);
		roof.addChild(outer_roof_6_r3);
		setRotationAngle(outer_roof_6_r3, 2.7925F, 0, -2.0944F);
		outer_roof_6_r3.setTextureUVOffset(33, 40).addCuboid(-1, 0, -3, 4, 0, 7, 0, true);

		outer_roof_7_r1 = createModelPart();
		outer_roof_7_r1.setPivot(31.9919F, -0.9516F, -18.4146F);
		roof.addChild(outer_roof_7_r1);
		setRotationAngle(outer_roof_7_r1, 2.8798F, 0, -2.618F);
		outer_roof_7_r1.setTextureUVOffset(-1, 5).addCuboid(-2, 0, -2.5F, 4, 0, 6, 0, false);

		outer_roof_7_r2 = createModelPart();
		outer_roof_7_r2.setPivot(22.7054F, -3.902F, -16);
		roof.addChild(outer_roof_7_r2);
		setRotationAngle(outer_roof_7_r2, 0.3054F, 0, 0.1745F);
		outer_roof_7_r2.setTextureUVOffset(10, 49).addCuboid(0, 0, -6, 9, 0, 6, 0, true);

		outer_roof_8_r1 = createModelPart();
		outer_roof_8_r1.setPivot(16.7054F, 79.2472F, 0.0461F);
		roof.addChild(outer_roof_8_r1);
		setRotationAngle(outer_roof_8_r1, 0.3054F, 0, 0);
		outer_roof_8_r1.setTextureUVOffset(16, 0).addCuboid(0, -84.125F, 4.7F, 6, 0, 5, 0, true);

		outer_roof_5_r6 = createModelPart();
		outer_roof_5_r6.setPivot(-0.7477F, 1.7327F, -18.8187F);
		roof.addChild(outer_roof_5_r6);
		setRotationAngle(outer_roof_5_r6, 2.7925F, 0, 2.0944F);
		outer_roof_5_r6.setTextureUVOffset(33, 40).addCuboid(-3, 0, -3, 4, 0, 7, 0, false);

		outer_roof_6_r4 = createModelPart();
		outer_roof_6_r4.setPivot(1.4189F, -0.9516F, -18.4146F);
		roof.addChild(outer_roof_6_r4);
		setRotationAngle(outer_roof_6_r4, 2.8798F, 0, 2.618F);
		outer_roof_6_r4.setTextureUVOffset(-1, 5).addCuboid(-2, 0, -2.5F, 4, 0, 6, 0, true);

		outer_roof_6_r5 = createModelPart();
		outer_roof_6_r5.setPivot(10.7054F, -3.902F, -16);
		roof.addChild(outer_roof_6_r5);
		setRotationAngle(outer_roof_6_r5, 0.3054F, 0, -0.1745F);
		outer_roof_6_r5.setTextureUVOffset(10, 49).addCuboid(-9, 0, -6, 9, 0, 6, 0, false);

		outer_roof_7_r3 = createModelPart();
		outer_roof_7_r3.setPivot(16.7054F, 79.2472F, 0.0461F);
		roof.addChild(outer_roof_7_r3);
		setRotationAngle(outer_roof_7_r3, 0.3054F, 0, 0);
		outer_roof_7_r3.setTextureUVOffset(16, 0).addCuboid(-6, -84.125F, 4.7F, 6, 0, 5, 0, false);

		headlights = createModelPart();
		headlights.setPivot(0, 24, 0);
		headlights.setTextureUVOffset(0, 57).addCuboid(7.75F, -11, -19.6F, 7, 4, 0, 0, false);
		headlights.setTextureUVOffset(0, 57).addCuboid(-14.75F, -11, -19.6F, 7, 4, 0, 0, true);

		tail_lights = createModelPart();
		tail_lights.setPivot(0, 24, 0);
		tail_lights.setTextureUVOffset(0, 243).addCuboid(7.75F, -15, -19.6F, 14, 13, 0, 0, false);
		tail_lights.setTextureUVOffset(0, 243).addCuboid(-21.75F, -15, -19.6F, 14, 13, 0, 0, true);

		door_light_on = createModelPart();
		door_light_on.setPivot(0, 24, 0);


		light_r1 = createModelPart();
		light_r1.setPivot(-21, 0, 0);
		door_light_on.addChild(light_r1);
		setRotationAngle(light_r1, 0, 0, 0.1107F);
		light_r1.setTextureUVOffset(6, 3).addCuboid(-1.5F, -33.5F, 0, 0, 0, 0, 0.3F, false);

		door_light_off = createModelPart();
		door_light_off.setPivot(0, 24, 0);


		light_r2 = createModelPart();
		light_r2.setPivot(-21, 0, 0);
		door_light_off.addChild(light_r2);
		setRotationAngle(light_r2, 0, 0, 0.1107F);
		light_r2.setTextureUVOffset(6, 0).addCuboid(-1.5F, -33.5F, 0, 0, 0, 0, 0.3F, false);

		buildModel();
	}

	private static final int DOOR_MAX = 13;
	private static final ModelDoorOverlay MODEL_DOOR_OVERLAY = new ModelDoorOverlay(DOOR_MAX, 6.34F, "door_overlay_c_train_left.png", "door_overlay_c_train_right.png");

	@Override
	public ModelSTrain createNew(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		return new ModelSTrain(doorAnimationType, renderDoorOverlay);
	}

	@Override
	protected void renderWindowPositions(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
		switch (renderStage) {
			case LIGHTS:
				renderMirror(roof_window_light, graphicsHolder, light, position);
				break;
			case INTERIOR:
				renderMirror(window, graphicsHolder, light, position);
				if (renderDetails) {
					renderMirror(roof_window, graphicsHolder, light, position);
					renderMirror(window_handrails, graphicsHolder, light, position);
				}
				break;
			case INTERIOR_TRANSLUCENT:
				renderMirror(side_panel_translucent, graphicsHolder, light, position - 21.5F);
				renderMirror(side_panel_translucent, graphicsHolder, light, position + 21.5F);
				break;
			case EXTERIOR:
				if (isEnd2Head) {
					renderOnceFlipped(window_exterior_1, graphicsHolder, light, position);
					renderOnceFlipped(window_exterior_2, graphicsHolder, light, position);
				} else {
					renderOnce(window_exterior_1, graphicsHolder, light, position);
					renderOnce(window_exterior_2, graphicsHolder, light, position);
				}
				renderMirror(roof_exterior, graphicsHolder, light, position);
				break;
		}
	}

	@Override
	protected void renderDoorPositions(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
		final boolean middleDoor = isIndex(getDoorPositions().length / 2, position, getDoorPositions());
		final boolean doorOpen = doorLeftZ > 0 || doorRightZ > 0;

		switch (renderStage) {
			case LIGHTS:
				renderMirror(roof_door_light, graphicsHolder, light, position);
				if (middleDoor && doorOpen && renderDetails) {
					renderMirror(door_light_on, graphicsHolder, light, position - 40);
				}
				break;
			case INTERIOR:
				door_left.setOffset(0, 0, doorRightZ);
				door_right.setOffset(0, 0, -doorRightZ);
				renderOnce(door, graphicsHolder, light, position);
				door_left.setOffset(0, 0, doorLeftZ);
				door_right.setOffset(0, 0, -doorLeftZ);
				renderOnceFlipped(door, graphicsHolder, light, position);

				if (renderDetails) {
					renderMirror(roof_door, graphicsHolder, light, position);
					renderOnce(door_handrail, graphicsHolder, light, position);
				}
				break;
			case EXTERIOR:
				if (isEnd2Head) {
					door_left_exterior_1.setOffset(0, 0, doorLeftZ);
					door_right_exterior_1.setOffset(0, 0, -doorLeftZ);
					renderOnceFlipped(door_exterior_1, graphicsHolder, light, position);
					door_left_exterior_2.setOffset(0, 0, doorRightZ);
					door_right_exterior_2.setOffset(0, 0, -doorRightZ);
					renderOnceFlipped(door_exterior_2, graphicsHolder, light, position);
				} else {
					door_left_exterior_1.setOffset(0, 0, doorRightZ);
					door_right_exterior_1.setOffset(0, 0, -doorRightZ);
					renderOnce(door_exterior_1, graphicsHolder, light, position);
					door_left_exterior_2.setOffset(0, 0, doorLeftZ);
					door_right_exterior_2.setOffset(0, 0, -doorLeftZ);
					renderOnce(door_exterior_2, graphicsHolder, light, position);
				}
				renderMirror(roof_exterior, graphicsHolder, light, position);
				if (middleDoor && !doorOpen && renderDetails) {
					renderMirror(door_light_off, graphicsHolder, light, position - 40);
				}
				break;
		}
	}

	@Override
	protected void renderHeadPosition1(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean useHeadlights) {
		switch (renderStage) {
			case LIGHTS:
				renderOnce(roof_end_light, graphicsHolder, light, position);
				break;
			case ALWAYS_ON_LIGHTS:
				renderOnce(useHeadlights ? headlights : tail_lights, graphicsHolder, light, position);
				break;
			case INTERIOR:
				renderOnce(head, graphicsHolder, light, position);
				break;
			case EXTERIOR:
				renderOnce(head_exterior, graphicsHolder, light, position);
				break;
		}
	}

	@Override
	protected void renderHeadPosition2(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean useHeadlights) {
		switch (renderStage) {
			case LIGHTS:
				renderOnceFlipped(roof_end_light, graphicsHolder, light, position);
				break;
			case ALWAYS_ON_LIGHTS:
				renderOnceFlipped(useHeadlights ? headlights : tail_lights, graphicsHolder, light, position);
				break;
			case INTERIOR:
				renderOnceFlipped(head, graphicsHolder, light, position);
				break;
			case EXTERIOR:
				renderOnceFlipped(head_exterior, graphicsHolder, light, position);
				break;
		}
	}

	@Override
	protected void renderEndPosition1(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
		switch (renderStage) {
			case LIGHTS:
				renderOnce(roof_end_light, graphicsHolder, light, position);
				break;
			case INTERIOR:
				renderOnce(end, graphicsHolder, light, position);
				if (renderDetails) {
					renderOnce(roof_end, graphicsHolder, light, position);
				}
				break;
			case EXTERIOR:
				renderOnce(end_exterior, graphicsHolder, light, position);
				renderOnce(roof_end_exterior, graphicsHolder, light, position);
				break;
		}
	}

	@Override
	protected void renderEndPosition2(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
		switch (renderStage) {
			case LIGHTS:
				renderOnceFlipped(roof_end_light, graphicsHolder, light, position);
				break;
			case INTERIOR:
				renderOnceFlipped(end, graphicsHolder, light, position);
				if (renderDetails) {
					renderOnceFlipped(roof_end, graphicsHolder, light, position);
				}
				break;
			case EXTERIOR:
				renderOnceFlipped(end_exterior, graphicsHolder, light, position);
				renderOnceFlipped(roof_end_exterior, graphicsHolder, light, position);
				break;
		}
	}

	@Override
	protected ModelDoorOverlay getModelDoorOverlay() {
		return MODEL_DOOR_OVERLAY;
	}

	@Override
	protected ModelDoorOverlayTopBase getModelDoorOverlayTop() {
		return null;
	}

	@Override
	protected int[] getWindowPositions() {
		return new int[]{-120, -40, 40, 120};
	}

	@Override
	protected int[] getDoorPositions() {
		return new int[]{-160, -80, 0, 80, 160};
	}

	@Override
	protected int[] getEndPositions() {
		return new int[]{-184, 184};
	}

	@Override
	protected int getDoorMax() {
		return DOOR_MAX;
	}
}