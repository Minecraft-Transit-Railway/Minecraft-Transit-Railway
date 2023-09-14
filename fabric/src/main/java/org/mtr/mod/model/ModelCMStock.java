package org.mtr.mod.model;

import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.ModelPartExtension;
import org.mtr.mod.client.DoorAnimationType;

public class ModelCMStock extends ModelSimpleTrainBase<ModelCMStock> {

	private final ModelPartExtension window;
	private final ModelPartExtension upper_wall_r1;
	private final ModelPartExtension window_handrails;
	private final ModelPartExtension handrail_12_r1;
	private final ModelPartExtension handrail_11_r1;
	private final ModelPartExtension handrail_10_r1;
	private final ModelPartExtension handrail_9_r1;
	private final ModelPartExtension handrail_8_r1;
	private final ModelPartExtension top_handrail_3_r1;
	private final ModelPartExtension top_handrail_2_r1;
	private final ModelPartExtension seat;
	private final ModelPartExtension seat_back_r1;
	private final ModelPartExtension window_exterior;
	private final ModelPartExtension upper_wall_r2;
	private final ModelPartExtension side_panel_translucent;
	private final ModelPartExtension roof_window;
	private final ModelPartExtension inner_roof_2_r1;
	private final ModelPartExtension roof_door;
	private final ModelPartExtension inner_roof_2_r2;
	private final ModelPartExtension roof_exterior;
	private final ModelPartExtension outer_roof_5_r1;
	private final ModelPartExtension outer_roof_4_r1;
	private final ModelPartExtension outer_roof_3_r1;
	private final ModelPartExtension outer_roof_2_r1;
	private final ModelPartExtension outer_roof_1_r1;
	private final ModelPartExtension door;
	private final ModelPartExtension door_left;
	private final ModelPartExtension door_left_top_r1;
	private final ModelPartExtension door_right;
	private final ModelPartExtension door_right_top_r1;
	private final ModelPartExtension door_handrail;
	private final ModelPartExtension handrail_2_r1;
	private final ModelPartExtension door_exterior;
	private final ModelPartExtension door_track_r1;
	private final ModelPartExtension door_left_exterior;
	private final ModelPartExtension door_left_top_r2;
	private final ModelPartExtension door_right_exterior;
	private final ModelPartExtension door_right_top_r2;
	private final ModelPartExtension end;
	private final ModelPartExtension upper_wall_2_r1;
	private final ModelPartExtension upper_wall_1_r1;
	private final ModelPartExtension end_exterior;
	private final ModelPartExtension upper_wall_2_r2;
	private final ModelPartExtension upper_wall_1_r2;
	private final ModelPartExtension roof_end;
	private final ModelPartExtension handrail_2_r2;
	private final ModelPartExtension inner_roof_1;
	private final ModelPartExtension inner_roof_2_r3;
	private final ModelPartExtension inner_roof_2;
	private final ModelPartExtension inner_roof_2_r4;
	private final ModelPartExtension roof_end_exterior;
	private final ModelPartExtension vent_2_r1;
	private final ModelPartExtension vent_1_r1;
	private final ModelPartExtension outer_roof_1;
	private final ModelPartExtension outer_roof_5_r2;
	private final ModelPartExtension outer_roof_4_r2;
	private final ModelPartExtension outer_roof_3_r2;
	private final ModelPartExtension outer_roof_2_r2;
	private final ModelPartExtension outer_roof_1_r2;
	private final ModelPartExtension outer_roof_2;
	private final ModelPartExtension outer_roof_5_r3;
	private final ModelPartExtension outer_roof_4_r3;
	private final ModelPartExtension outer_roof_3_r3;
	private final ModelPartExtension outer_roof_2_r3;
	private final ModelPartExtension outer_roof_1_r3;
	private final ModelPartExtension roof_light;
	private final ModelPartExtension light_2_r1;
	private final ModelPartExtension roof_end_light;
	private final ModelPartExtension light_r1;
	private final ModelPartExtension roof_door_light;
	private final ModelPartExtension light_1_r1;
	private final ModelPartExtension head;
	private final ModelPartExtension upper_wall_2_r3;
	private final ModelPartExtension upper_wall_1_r3;
	private final ModelPartExtension head_exterior;
	private final ModelPartExtension driver_door_upper_2_r1;
	private final ModelPartExtension driver_door_upper_1_r1;
	private final ModelPartExtension front;
	private final ModelPartExtension bottom_r1;
	private final ModelPartExtension front_bottom_right_r1;
	private final ModelPartExtension front_middle_top_r1;
	private final ModelPartExtension front_panel_r1;
	private final ModelPartExtension side_1;
	private final ModelPartExtension front_side_bottom_1_r1;
	private final ModelPartExtension front_bottom_right_r2;
	private final ModelPartExtension outer_roof_4_r4;
	private final ModelPartExtension outer_roof_2_r4;
	private final ModelPartExtension outer_roof_1_r4;
	private final ModelPartExtension outer_roof_3_r4;
	private final ModelPartExtension front_side_lower_1_r1;
	private final ModelPartExtension front_side_upper_1_r1;
	private final ModelPartExtension side_2;
	private final ModelPartExtension front_side_bottom_2_r1;
	private final ModelPartExtension outer_roof_4_r5;
	private final ModelPartExtension outer_roof_7_r1;
	private final ModelPartExtension outer_roof_5_r4;
	private final ModelPartExtension outer_roof_5_r5;
	private final ModelPartExtension front_side_upper_2_r1;
	private final ModelPartExtension front_side_lower_2_r1;
	private final ModelPartExtension coupler;
	private final ModelPartExtension headlight_panel_left;
	private final ModelPartExtension headlight_panel_bottom_r1;
	private final ModelPartExtension headlight_panel_main_r1;
	private final ModelPartExtension headlight_panel_right;
	private final ModelPartExtension headlight_panel_bottom_r2;
	private final ModelPartExtension headlight_panel_main_r2;
	private final ModelPartExtension headlights;
	private final ModelPartExtension tail_lights;
	private final ModelPartExtension door_light;
	private final ModelPartExtension outer_roof_1_r5;
	private final ModelPartExtension door_light_on;
	private final ModelPartExtension light_r2;
	private final ModelPartExtension door_light_off;
	private final ModelPartExtension light_r3;

	public ModelCMStock() {
		this(DoorAnimationType.BOUNCY_1, true);
	}

	protected ModelCMStock(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		super(320, 320, doorAnimationType, renderDoorOverlay);

		window = createModelPart();
		window.setPivot(0, 24, 0);
		window.setTextureUVOffset(0, 0).addCuboid(-20, 0, -24, 20, 1, 48, 0, false);
		window.setTextureUVOffset(136, 109).addCuboid(-20, -14, -26, 2, 14, 52, 0, false);

		upper_wall_r1 = createModelPart();
		upper_wall_r1.setPivot(-20, -14, 0);
		window.addChild(upper_wall_r1);
		setRotationAngle(upper_wall_r1, 0, 0, 0.1107F);
		upper_wall_r1.setTextureUVOffset(0, 82).addCuboid(0, -19, -26, 2, 19, 52, 0, false);

		window_handrails = createModelPart();
		window_handrails.setPivot(0, 24, 0);
		window_handrails.setTextureUVOffset(317, 0).addCuboid(-11, -27.2F, -22, 0, 22, 0, 0.2F, false);
		window_handrails.setTextureUVOffset(317, 0).addCuboid(-11, -27.2F, 22, 0, 22, 0, 0.2F, false);
		window_handrails.setTextureUVOffset(317, 0).addCuboid(-12.461F, -32.504F, 12.2F, 0, 3, 0, 0.2F, false);
		window_handrails.setTextureUVOffset(317, 0).addCuboid(-12.461F, -32.504F, -12.2F, 0, 3, 0, 0.2F, false);
		window_handrails.setTextureUVOffset(317, 0).addCuboid(0, -33, -9, 0, 33, 0, 0.2F, false);
		window_handrails.setTextureUVOffset(317, 0).addCuboid(0, -33, 9, 0, 33, 0, 0.2F, false);
		window_handrails.setTextureUVOffset(19, 14).addCuboid(-1, -32, 21, 2, 4, 0, 0, false);
		window_handrails.setTextureUVOffset(19, 14).addCuboid(-1, -32, 15, 2, 4, 0, 0, false);
		window_handrails.setTextureUVOffset(19, 14).addCuboid(-1, -32, 3, 2, 4, 0, 0, false);
		window_handrails.setTextureUVOffset(19, 14).addCuboid(-1, -32, -3, 2, 4, 0, 0, false);
		window_handrails.setTextureUVOffset(19, 14).addCuboid(-1, -32, -15, 2, 4, 0, 0, false);
		window_handrails.setTextureUVOffset(19, 14).addCuboid(-13.575F, -29.725F, 8.525F, 2, 4, 0, 0, false);
		window_handrails.setTextureUVOffset(19, 14).addCuboid(-13.575F, -29.725F, 0.475F, 2, 4, 0, 0, false);
		window_handrails.setTextureUVOffset(19, 14).addCuboid(-13.575F, -29.725F, -8.525F, 2, 4, 0, 0, false);
		window_handrails.setTextureUVOffset(19, 14).addCuboid(-1, -32, -21, 2, 4, 0, 0, false);

		handrail_12_r1 = createModelPart();
		handrail_12_r1.setPivot(-14.9052F, -3.8479F, 22);
		window_handrails.addChild(handrail_12_r1);
		setRotationAngle(handrail_12_r1, 0, 0, 1.4835F);
		handrail_12_r1.setTextureUVOffset(317, 0).addCuboid(0, -3, 0, 0, 6, 0, 0.2F, false);

		handrail_11_r1 = createModelPart();
		handrail_11_r1.setPivot(-10.8F, -5, 22.2F);
		window_handrails.addChild(handrail_11_r1);
		setRotationAngle(handrail_11_r1, 0, 0, 0.6981F);
		handrail_11_r1.setTextureUVOffset(317, 0).addCuboid(-0.2F, 0.2F, -0.2F, 0, 1, 0, 0.2F, false);

		handrail_10_r1 = createModelPart();
		handrail_10_r1.setPivot(-11.0106F, -4.9415F, 0);
		window_handrails.addChild(handrail_10_r1);
		setRotationAngle(handrail_10_r1, 0, 0, 1.4835F);
		handrail_10_r1.setTextureUVOffset(317, 0).addCuboid(0.75F, 0.975F, -22, 0, 6, 0, 0.2F, false);

		handrail_9_r1 = createModelPart();
		handrail_9_r1.setPivot(-10.8F, -5, -22.2F);
		window_handrails.addChild(handrail_9_r1);
		setRotationAngle(handrail_9_r1, 0, 0, 0.6981F);
		handrail_9_r1.setTextureUVOffset(317, 0).addCuboid(-0.2F, 0.2F, 0.2F, 0, 1, 0, 0.2F, false);

		handrail_8_r1 = createModelPart();
		handrail_8_r1.setPivot(0, 0, 0);
		window_handrails.addChild(handrail_8_r1);
		setRotationAngle(handrail_8_r1, -1.5708F, 0, 0);
		handrail_8_r1.setTextureUVOffset(317, 0).addCuboid(0, -24, -31.5F, 0, 48, 0, 0.2F, false);

		top_handrail_3_r1 = createModelPart();
		top_handrail_3_r1.setPivot(-10.8F, -27.4F, 22.2F);
		window_handrails.addChild(top_handrail_3_r1);
		setRotationAngle(top_handrail_3_r1, 0, 0, -0.6545F);
		top_handrail_3_r1.setTextureUVOffset(317, 0).addCuboid(-0.2F, -2.2F, -0.2F, 0, 2, 0, 0.2F, false);
		top_handrail_3_r1.setTextureUVOffset(317, 0).addCuboid(-0.2F, -2.2F, -44.2F, 0, 2, 0, 0.2F, false);

		top_handrail_2_r1 = createModelPart();
		top_handrail_2_r1.setPivot(-12.461F, -29.104F, 0);
		window_handrails.addChild(top_handrail_2_r1);
		setRotationAngle(top_handrail_2_r1, -1.5708F, 0, 0);
		top_handrail_2_r1.setTextureUVOffset(317, 0).addCuboid(0, -22, 0, 0, 44, 0, 0.2F, false);

		seat = createModelPart();
		seat.setPivot(0, 0, 0);
		window_handrails.addChild(seat);
		seat.setTextureUVOffset(0, 180).addCuboid(-18, -6, -22, 7, 1, 44, 0, false);
		seat.setTextureUVOffset(60, 189).addCuboid(-18, -5, -21, 6, 5, 42, 0, false);

		seat_back_r1 = createModelPart();
		seat_back_r1.setPivot(-17, -6, 0);
		seat.addChild(seat_back_r1);
		setRotationAngle(seat_back_r1, 0, 0, -0.0524F);
		seat_back_r1.setTextureUVOffset(192, 79).addCuboid(-1, -8, -22, 1, 8, 44, 0, false);

		window_exterior = createModelPart();
		window_exterior.setPivot(0, 24, 0);
		window_exterior.setTextureUVOffset(160, 175).addCuboid(-21, 0, -24, 1, 4, 48, 0, false);
		window_exterior.setTextureUVOffset(104, 123).addCuboid(-20, -14, -26, 0, 14, 52, 0, false);

		upper_wall_r2 = createModelPart();
		upper_wall_r2.setPivot(-20, -14, 0);
		window_exterior.addChild(upper_wall_r2);
		setRotationAngle(upper_wall_r2, 0, 0, 0.1107F);
		upper_wall_r2.setTextureUVOffset(0, 109).addCuboid(0, -19, -26, 0, 19, 52, 0, false);

		side_panel_translucent = createModelPart();
		side_panel_translucent.setPivot(0, 24, 0);
		side_panel_translucent.setTextureUVOffset(0, 0).addCuboid(-18, -28, 0, 7, 24, 0, 0, false);

		roof_window = createModelPart();
		roof_window.setPivot(0, 24, 0);
		roof_window.setTextureUVOffset(68, 0).addCuboid(-16, -32, -24, 3, 0, 48, 0, false);
		roof_window.setTextureUVOffset(40, 0).addCuboid(-10, -34, -24, 10, 0, 48, 0, false);

		inner_roof_2_r1 = createModelPart();
		inner_roof_2_r1.setPivot(-13, -32, 0);
		roof_window.addChild(inner_roof_2_r1);
		setRotationAngle(inner_roof_2_r1, 0, 0, -0.5236F);
		inner_roof_2_r1.setTextureUVOffset(60, 0).addCuboid(0, 0, -24, 4, 0, 48, 0, false);

		roof_door = createModelPart();
		roof_door.setPivot(0, 24, 0);
		roof_door.setTextureUVOffset(108, 111).addCuboid(-19, -32, -16, 6, 0, 32, 0, false);
		roof_door.setTextureUVOffset(56, 7).addCuboid(-10, -34, -16, 10, 0, 32, 0, false);

		inner_roof_2_r2 = createModelPart();
		inner_roof_2_r2.setPivot(-13, -32, 0);
		roof_door.addChild(inner_roof_2_r2);
		setRotationAngle(inner_roof_2_r2, 0, 0, -0.5236F);
		inner_roof_2_r2.setTextureUVOffset(0, 0).addCuboid(0, 0, -16, 4, 0, 32, 0, false);

		roof_exterior = createModelPart();
		roof_exterior.setPivot(0, 24, 0);
		roof_exterior.setTextureUVOffset(0, 82).addCuboid(-6, -42, -20, 6, 0, 40, 0, false);

		outer_roof_5_r1 = createModelPart();
		outer_roof_5_r1.setPivot(-9.9394F, -41.3064F, 0);
		roof_exterior.addChild(outer_roof_5_r1);
		setRotationAngle(outer_roof_5_r1, 0, 0, -0.1745F);
		outer_roof_5_r1.setTextureUVOffset(82, 0).addCuboid(-4, 0, -20, 8, 0, 40, 0, false);

		outer_roof_4_r1 = createModelPart();
		outer_roof_4_r1.setPivot(-15.1778F, -39.8628F, 0);
		roof_exterior.addChild(outer_roof_4_r1);
		setRotationAngle(outer_roof_4_r1, 0, 0, -0.5236F);
		outer_roof_4_r1.setTextureUVOffset(0, 0).addCuboid(-1.5F, 0, -20, 3, 0, 40, 0, false);

		outer_roof_3_r1 = createModelPart();
		outer_roof_3_r1.setPivot(-16.9769F, -38.2468F, 0);
		roof_exterior.addChild(outer_roof_3_r1);
		setRotationAngle(outer_roof_3_r1, 0, 0, -1.0472F);
		outer_roof_3_r1.setTextureUVOffset(98, 0).addCuboid(-1, 0, -20, 2, 0, 40, 0, false);

		outer_roof_2_r1 = createModelPart();
		outer_roof_2_r1.setPivot(-17.5872F, -36.3872F, 0);
		roof_exterior.addChild(outer_roof_2_r1);
		setRotationAngle(outer_roof_2_r1, 0, 0, 0.1107F);
		outer_roof_2_r1.setTextureUVOffset(0, 113).addCuboid(0, -1, -20, 0, 2, 40, 0, false);

		outer_roof_1_r1 = createModelPart();
		outer_roof_1_r1.setPivot(-20, -14, 0);
		roof_exterior.addChild(outer_roof_1_r1);
		setRotationAngle(outer_roof_1_r1, 0, 0, 0.1107F);
		outer_roof_1_r1.setTextureUVOffset(116, 196).addCuboid(-0.075F, -22, -20, 1, 4, 40, 0, false);

		door = createModelPart();
		door.setPivot(0, 24, 0);
		door.setTextureUVOffset(16, 16).addCuboid(-20, 0, -16, 20, 1, 32, 0, false);

		door_left = createModelPart();
		door_left.setPivot(0, 0, 0);
		door.addChild(door_left);
		door_left.setTextureUVOffset(56, 236).addCuboid(-20.8F, -14, 0, 1, 14, 15, 0, false);

		door_left_top_r1 = createModelPart();
		door_left_top_r1.setPivot(-20.8F, -14, 0);
		door_left.addChild(door_left_top_r1);
		setRotationAngle(door_left_top_r1, 0, 0, 0.1107F);
		door_left_top_r1.setTextureUVOffset(78, 272).addCuboid(0, -19, 0, 1, 19, 15, 0, false);

		door_right = createModelPart();
		door_right.setPivot(0, 0, 0);
		door.addChild(door_right);
		door_right.setTextureUVOffset(0, 9).addCuboid(-20.8F, -14, -15, 1, 14, 15, 0, false);

		door_right_top_r1 = createModelPart();
		door_right_top_r1.setPivot(-20.8F, -14, 0);
		door_right.addChild(door_right_top_r1);
		setRotationAngle(door_right_top_r1, 0, 0, 0.1107F);
		door_right_top_r1.setTextureUVOffset(0, 49).addCuboid(0, -19, -15, 1, 19, 15, 0, false);

		door_handrail = createModelPart();
		door_handrail.setPivot(0, 24, 0);
		door_handrail.setTextureUVOffset(317, 0).addCuboid(0, -34, 0, 0, 34, 0, 0.2F, false);

		handrail_2_r1 = createModelPart();
		handrail_2_r1.setPivot(0, 0, 0);
		door_handrail.addChild(handrail_2_r1);
		setRotationAngle(handrail_2_r1, -1.5708F, 0, 0);
		handrail_2_r1.setTextureUVOffset(317, 0).addCuboid(0, -16, -31.5F, 0, 32, 0, 0.2F, false);

		door_exterior = createModelPart();
		door_exterior.setPivot(0, 24, 0);
		door_exterior.setTextureUVOffset(56, 236).addCuboid(-21, 0, -16, 1, 4, 32, 0, false);

		door_track_r1 = createModelPart();
		door_track_r1.setPivot(-20, -14, 0);
		door_exterior.addChild(door_track_r1);
		setRotationAngle(door_track_r1, 0, 0, 0.1107F);
		door_track_r1.setTextureUVOffset(59, 49).addCuboid(-1, -22, -29, 1, 4, 58, 0, false);

		door_left_exterior = createModelPart();
		door_left_exterior.setPivot(0, 0, 0);
		door_exterior.addChild(door_left_exterior);
		door_left_exterior.setTextureUVOffset(210, 194).addCuboid(-20.8F, -14, 0, 0, 14, 15, 0, false);

		door_left_top_r2 = createModelPart();
		door_left_top_r2.setPivot(-20.8F, -14, 0);
		door_left_exterior.addChild(door_left_top_r2);
		setRotationAngle(door_left_top_r2, 0, 0, 0.1107F);
		door_left_top_r2.setTextureUVOffset(192, 88).addCuboid(0, -19, 0, 0, 19, 15, 0, false);

		door_right_exterior = createModelPart();
		door_right_exterior.setPivot(0, 0, 0);
		door_exterior.addChild(door_right_exterior);
		door_right_exterior.setTextureUVOffset(104, 146).addCuboid(-20.8F, -14, -15, 0, 14, 15, 0, false);

		door_right_top_r2 = createModelPart();
		door_right_top_r2.setPivot(-20.8F, -14, 0);
		door_right_exterior.addChild(door_right_top_r2);
		setRotationAngle(door_right_top_r2, 0, 0, 0.1107F);
		door_right_top_r2.setTextureUVOffset(0, 68).addCuboid(0, -19, -15, 0, 19, 15, 0, false);

		end = createModelPart();
		end.setPivot(0, 24, 0);
		end.setTextureUVOffset(192, 131).addCuboid(-20, 0, -12, 40, 1, 20, 0, false);
		end.setTextureUVOffset(56, 111).addCuboid(18, -14, 7, 2, 14, 3, 0, false);
		end.setTextureUVOffset(30, 82).addCuboid(-20, -14, 7, 2, 14, 3, 0, false);
		end.setTextureUVOffset(225, 156).addCuboid(9.5F, -34, -12, 9, 34, 19, 0, false);
		end.setTextureUVOffset(0, 225).addCuboid(-18.5F, -34, -12, 9, 34, 19, 0, false);

		upper_wall_2_r1 = createModelPart();
		upper_wall_2_r1.setPivot(-20, -14, 0);
		end.addChild(upper_wall_2_r1);
		setRotationAngle(upper_wall_2_r1, 0, 0, 0.1107F);
		upper_wall_2_r1.setTextureUVOffset(152, 111).addCuboid(0, -19, 7, 2, 19, 3, 0, false);

		upper_wall_1_r1 = createModelPart();
		upper_wall_1_r1.setPivot(20, -14, 0);
		end.addChild(upper_wall_1_r1);
		setRotationAngle(upper_wall_1_r1, 0, 0, -0.1107F);
		upper_wall_1_r1.setTextureUVOffset(185, 50).addCuboid(-2, -19, 7, 2, 19, 3, 0, false);

		end_exterior = createModelPart();
		end_exterior.setPivot(0, 24, 0);
		end_exterior.setTextureUVOffset(36, 272).addCuboid(20, 0, -12, 1, 4, 20, 0, false);
		end_exterior.setTextureUVOffset(270, 213).addCuboid(-21, 0, -12, 1, 4, 20, 0, false);
		end_exterior.setTextureUVOffset(248, 239).addCuboid(18, -14, -12, 2, 14, 22, 0, false);
		end_exterior.setTextureUVOffset(140, 121).addCuboid(-20, -14, -12, 2, 14, 22, 0, false);
		end_exterior.setTextureUVOffset(0, 278).addCuboid(9.5F, -34, -12, 9, 34, 0, 0, false);
		end_exterior.setTextureUVOffset(166, 0).addCuboid(-18.5F, -34, -12, 9, 34, 0, 0, false);
		end_exterior.setTextureUVOffset(205, 40).addCuboid(-18, -41, -12, 36, 7, 0, 0, false);

		upper_wall_2_r2 = createModelPart();
		upper_wall_2_r2.setPivot(-20, -14, 0);
		end_exterior.addChild(upper_wall_2_r2);
		setRotationAngle(upper_wall_2_r2, 0, 0, 0.1107F);
		upper_wall_2_r2.setTextureUVOffset(122, 240).addCuboid(0, -19, -12, 2, 19, 22, 0, false);

		upper_wall_1_r2 = createModelPart();
		upper_wall_1_r2.setPivot(20, -14, 0);
		end_exterior.addChild(upper_wall_1_r2);
		setRotationAngle(upper_wall_1_r2, 0, 0, -0.1107F);
		upper_wall_1_r2.setTextureUVOffset(170, 240).addCuboid(-2, -19, -12, 2, 19, 22, 0, false);

		roof_end = createModelPart();
		roof_end.setPivot(0, 24, 0);


		handrail_2_r2 = createModelPart();
		handrail_2_r2.setPivot(0, 0, 0);
		roof_end.addChild(handrail_2_r2);
		setRotationAngle(handrail_2_r2, -1.5708F, 0, 0);
		handrail_2_r2.setTextureUVOffset(0, 0).addCuboid(0, -40, -31.5F, 0, 16, 0, 0.2F, false);

		inner_roof_1 = createModelPart();
		inner_roof_1.setPivot(-2, -33, 16);
		roof_end.addChild(inner_roof_1);
		inner_roof_1.setTextureUVOffset(103, 50).addCuboid(-17, 1, -12, 6, 0, 36, 0, true);
		inner_roof_1.setTextureUVOffset(67, 49).addCuboid(-8, -1, -28, 10, 0, 52, 0, false);

		inner_roof_2_r3 = createModelPart();
		inner_roof_2_r3.setPivot(-11, 1, -16);
		inner_roof_1.addChild(inner_roof_2_r3);
		setRotationAngle(inner_roof_2_r3, 0, 0, -0.5236F);
		inner_roof_2_r3.setTextureUVOffset(106, 0).addCuboid(0, 0, 4, 4, 0, 36, 0, true);

		inner_roof_2 = createModelPart();
		inner_roof_2.setPivot(-2, -33, 16);
		roof_end.addChild(inner_roof_2);
		inner_roof_2.setTextureUVOffset(103, 50).addCuboid(15, 1, -12, 6, 0, 36, 0, false);
		inner_roof_2.setTextureUVOffset(67, 49).addCuboid(2, -1, -28, 10, 0, 52, 0, true);

		inner_roof_2_r4 = createModelPart();
		inner_roof_2_r4.setPivot(15, 1, -16);
		inner_roof_2.addChild(inner_roof_2_r4);
		setRotationAngle(inner_roof_2_r4, 0, 0, 0.5236F);
		inner_roof_2_r4.setTextureUVOffset(106, 0).addCuboid(-4, 0, 4, 4, 0, 36, 0, false);

		roof_end_exterior = createModelPart();
		roof_end_exterior.setPivot(0, 24, 0);
		roof_end_exterior.setTextureUVOffset(60, 111).addCuboid(-8, -43, 0, 16, 2, 48, 0, false);

		vent_2_r1 = createModelPart();
		vent_2_r1.setPivot(-8, -43, 0);
		roof_end_exterior.addChild(vent_2_r1);
		setRotationAngle(vent_2_r1, 0, 0, -0.3491F);
		vent_2_r1.setTextureUVOffset(119, 53).addCuboid(-9, 0, 0, 9, 2, 48, 0, false);

		vent_1_r1 = createModelPart();
		vent_1_r1.setPivot(8, -43, 0);
		roof_end_exterior.addChild(vent_1_r1);
		setRotationAngle(vent_1_r1, 0, 0, 0.3491F);
		vent_1_r1.setTextureUVOffset(139, 0).addCuboid(0, 0, 0, 9, 2, 48, 0, false);

		outer_roof_1 = createModelPart();
		outer_roof_1.setPivot(0, 0, 0);
		roof_end_exterior.addChild(outer_roof_1);
		outer_roof_1.setTextureUVOffset(56, 111).addCuboid(-6, -42, -12, 6, 1, 20, 0, false);

		outer_roof_5_r2 = createModelPart();
		outer_roof_5_r2.setPivot(-9.7656F, -40.3206F, 0);
		outer_roof_1.addChild(outer_roof_5_r2);
		setRotationAngle(outer_roof_5_r2, 0, 0, -0.1745F);
		outer_roof_5_r2.setTextureUVOffset(130, 20).addCuboid(-4, -1, -12, 8, 1, 20, 0, false);

		outer_roof_4_r2 = createModelPart();
		outer_roof_4_r2.setPivot(-14.6775F, -38.9948F, 0);
		outer_roof_1.addChild(outer_roof_4_r2);
		setRotationAngle(outer_roof_4_r2, 0, 0, -0.5236F);
		outer_roof_4_r2.setTextureUVOffset(158, 194).addCuboid(-1.5F, -1, -12, 3, 1, 20, 0, false);

		outer_roof_3_r2 = createModelPart();
		outer_roof_3_r2.setPivot(-16.1105F, -37.7448F, 0);
		outer_roof_1.addChild(outer_roof_3_r2);
		setRotationAngle(outer_roof_3_r2, 0, 0, -1.0472F);
		outer_roof_3_r2.setTextureUVOffset(261, 27).addCuboid(-1, -1, -12, 2, 1, 20, 0, false);

		outer_roof_2_r2 = createModelPart();
		outer_roof_2_r2.setPivot(-17.587F, -36.3849F, 0);
		outer_roof_1.addChild(outer_roof_2_r2);
		setRotationAngle(outer_roof_2_r2, 0, 0, 0.1107F);
		outer_roof_2_r2.setTextureUVOffset(274, 237).addCuboid(0, -1, -12, 1, 2, 20, 0, false);

		outer_roof_1_r2 = createModelPart();
		outer_roof_1_r2.setPivot(-20, -14, 0);
		outer_roof_1.addChild(outer_roof_1_r2);
		setRotationAngle(outer_roof_1_r2, 0, 0, 0.1107F);
		outer_roof_1_r2.setTextureUVOffset(261, 189).addCuboid(-0.075F, -22, -12, 1, 4, 20, 0, false);

		outer_roof_2 = createModelPart();
		outer_roof_2.setPivot(0, 0, 0);
		roof_end_exterior.addChild(outer_roof_2);
		outer_roof_2.setTextureUVOffset(0, 102).addCuboid(0, -42, -12, 6, 1, 20, 0, false);

		outer_roof_5_r3 = createModelPart();
		outer_roof_5_r3.setPivot(9.7656F, -40.3206F, 0);
		outer_roof_2.addChild(outer_roof_5_r3);
		setRotationAngle(outer_roof_5_r3, 0, 0, 0.1745F);
		outer_roof_5_r3.setTextureUVOffset(56, 82).addCuboid(-4, -1, -12, 8, 1, 20, 0, false);

		outer_roof_4_r3 = createModelPart();
		outer_roof_4_r3.setPivot(14.6775F, -38.9948F, 0);
		outer_roof_2.addChild(outer_roof_4_r3);
		setRotationAngle(outer_roof_4_r3, 0, 0, 0.5236F);
		outer_roof_4_r3.setTextureUVOffset(185, 79).addCuboid(-1.5F, -1, -12, 3, 1, 20, 0, false);

		outer_roof_3_r3 = createModelPart();
		outer_roof_3_r3.setPivot(16.1105F, -37.7448F, 0);
		outer_roof_2.addChild(outer_roof_3_r3);
		setRotationAngle(outer_roof_3_r3, 0, 0, 1.0472F);
		outer_roof_3_r3.setTextureUVOffset(148, 240).addCuboid(-1, -1, -12, 2, 1, 20, 0, false);

		outer_roof_2_r3 = createModelPart();
		outer_roof_2_r3.setPivot(17.587F, -36.3849F, 0);
		outer_roof_2.addChild(outer_roof_2_r3);
		setRotationAngle(outer_roof_2_r3, 0, 0, -0.1107F);
		outer_roof_2_r3.setTextureUVOffset(262, 152).addCuboid(-1, -1, -12, 1, 2, 20, 0, false);

		outer_roof_1_r3 = createModelPart();
		outer_roof_1_r3.setPivot(20, -14, 0);
		outer_roof_2.addChild(outer_roof_1_r3);
		setRotationAngle(outer_roof_1_r3, 0, 0, -0.1107F);
		outer_roof_1_r3.setTextureUVOffset(90, 236).addCuboid(-0.925F, -22, -12, 1, 4, 20, 0, false);

		roof_light = createModelPart();
		roof_light.setPivot(0, 24, 0);


		light_2_r1 = createModelPart();
		light_2_r1.setPivot(0, 0, 0);
		roof_light.addChild(light_2_r1);
		setRotationAngle(light_2_r1, 0, -1.5708F, 0);
		light_2_r1.setTextureUVOffset(2, 0).addCuboid(-14.875F, -33.975F, -10, 2, 0, 20, 0, false);
		light_2_r1.setTextureUVOffset(2, 0).addCuboid(12.125F, -33.975F, -10, 2, 0, 20, 0, false);

		roof_end_light = createModelPart();
		roof_end_light.setPivot(0, 24, 0);


		light_r1 = createModelPart();
		light_r1.setPivot(0, 0, 0);
		roof_end_light.addChild(light_r1);
		setRotationAngle(light_r1, 0, -1.5708F, 0);
		light_r1.setTextureUVOffset(0, 0).addCuboid(24, -33.975F, -13.975F, 2, 0, 24, 0, false);

		roof_door_light = createModelPart();
		roof_door_light.setPivot(0, 24, 0);


		light_1_r1 = createModelPart();
		light_1_r1.setPivot(0, 0, 0);
		roof_door_light.addChild(light_1_r1);
		setRotationAngle(light_1_r1, 0, -1.5708F, 0);
		light_1_r1.setTextureUVOffset(2, 0).addCuboid(-2, -33.975F, -10, 2, 0, 20, 0, false);

		head = createModelPart();
		head.setPivot(0, 24, 0);
		head.setTextureUVOffset(56, 82).addCuboid(18, -14, 4, 2, 14, 6, 0, true);
		head.setTextureUVOffset(56, 82).addCuboid(-20, -14, 4, 2, 14, 6, 0, false);
		head.setTextureUVOffset(238, 79).addCuboid(-18, -34, 4, 36, 34, 0, 0, false);
		head.setTextureUVOffset(126, 303).addCuboid(-20, 0, 4, 40, 1, 4, 0, false);

		upper_wall_2_r3 = createModelPart();
		upper_wall_2_r3.setPivot(-20, -14, 0);
		head.addChild(upper_wall_2_r3);
		setRotationAngle(upper_wall_2_r3, 0, 0, 0.1107F);
		upper_wall_2_r3.setTextureUVOffset(151, 50).addCuboid(0, -19, 4, 2, 19, 6, 0, false);

		upper_wall_1_r3 = createModelPart();
		upper_wall_1_r3.setPivot(20, -14, 0);
		head.addChild(upper_wall_1_r3);
		setRotationAngle(upper_wall_1_r3, 0, 0, -0.1107F);
		upper_wall_1_r3.setTextureUVOffset(151, 50).addCuboid(-2, -19, 4, 2, 19, 6, 0, true);

		head_exterior = createModelPart();
		head_exterior.setPivot(0, 24, 0);
		head_exterior.setTextureUVOffset(185, 50).addCuboid(-21, 0, -18, 42, 7, 22, 0, false);
		head_exterior.setTextureUVOffset(66, 111).addCuboid(20, 0, 4, 1, 7, 4, 0, true);
		head_exterior.setTextureUVOffset(66, 111).addCuboid(-21, 0, 4, 1, 7, 4, 0, false);
		head_exterior.setTextureUVOffset(114, 194).addCuboid(18, -14, -9, 2, 14, 19, 0, true);
		head_exterior.setTextureUVOffset(114, 194).addCuboid(-20, -14, -9, 2, 14, 19, 0, false);
		head_exterior.setTextureUVOffset(142, 77).addCuboid(18, -14, -18, 1, 14, 9, 0, false);
		head_exterior.setTextureUVOffset(142, 77).addCuboid(-19, -14, -18, 1, 14, 9, 0, true);
		head_exterior.setTextureUVOffset(198, 227).addCuboid(-18, -34, 3, 36, 34, 0, 0, false);

		driver_door_upper_2_r1 = createModelPart();
		driver_door_upper_2_r1.setPivot(-20, -14, 0);
		head_exterior.addChild(driver_door_upper_2_r1);
		setRotationAngle(driver_door_upper_2_r1, 0, 0, 0.1107F);
		driver_door_upper_2_r1.setTextureUVOffset(110, 272).addCuboid(1, -19, -18, 1, 19, 9, 0, true);
		driver_door_upper_2_r1.setTextureUVOffset(0, 180).addCuboid(0, -19, -9, 2, 19, 19, 0, false);

		driver_door_upper_1_r1 = createModelPart();
		driver_door_upper_1_r1.setPivot(20, -14, 0);
		head_exterior.addChild(driver_door_upper_1_r1);
		setRotationAngle(driver_door_upper_1_r1, 0, 0, -0.1107F);
		driver_door_upper_1_r1.setTextureUVOffset(110, 272).addCuboid(-2, -19, -18, 1, 19, 9, 0, false);
		driver_door_upper_1_r1.setTextureUVOffset(0, 180).addCuboid(-2, -19, -9, 2, 19, 19, 0, true);

		front = createModelPart();
		front.setPivot(0, 0, 0);
		head_exterior.addChild(front);


		bottom_r1 = createModelPart();
		bottom_r1.setPivot(0, 5.4768F, 4.6132F);
		front.addChild(bottom_r1);
		setRotationAngle(bottom_r1, -0.0436F, 0, 0);
		bottom_r1.setTextureUVOffset(0, 49).addCuboid(-21, 1.525F, -31.55F, 42, 0, 33, 0, false);

		front_bottom_right_r1 = createModelPart();
		front_bottom_right_r1.setPivot(0, 0, 0);
		front.addChild(front_bottom_right_r1);
		setRotationAngle(front_bottom_right_r1, 0.3491F, 0, 0);
		front_bottom_right_r1.setTextureUVOffset(92, 93).addCuboid(7, -9.625F, -26.3F, 12, 7, 0, 0, false);

		front_middle_top_r1 = createModelPart();
		front_middle_top_r1.setPivot(0, -42, -12);
		front.addChild(front_middle_top_r1);
		setRotationAngle(front_middle_top_r1, 0.3491F, 0, 0);
		front_middle_top_r1.setTextureUVOffset(0, 123).addCuboid(-6, 0, -10, 12, 0, 10, 0, false);

		front_panel_r1 = createModelPart();
		front_panel_r1.setPivot(0, 0, -28);
		front.addChild(front_panel_r1);
		setRotationAngle(front_panel_r1, -0.1745F, 0, 0);
		front_panel_r1.setTextureUVOffset(205, 0).addCuboid(-19, -40, 0, 38, 40, 0, 0, false);

		side_1 = createModelPart();
		side_1.setPivot(0, 0, 0);
		front.addChild(side_1);
		side_1.setTextureUVOffset(19, 0).addCuboid(19, -14, -18, 1, 14, 0, 0, false);

		front_side_bottom_1_r1 = createModelPart();
		front_side_bottom_1_r1.setPivot(21, 0, -13);
		side_1.addChild(front_side_bottom_1_r1);
		setRotationAngle(front_side_bottom_1_r1, 0, 0.1745F, 0.1745F);
		front_side_bottom_1_r1.setTextureUVOffset(0, 17).addCuboid(0, 0, -16, 0, 7, 23, 0, false);

		front_bottom_right_r2 = createModelPart();
		front_bottom_right_r2.setPivot(0, 0, 0);
		side_1.addChild(front_bottom_right_r2);
		setRotationAngle(front_bottom_right_r2, 0.3491F, 0, 0);
		front_bottom_right_r2.setTextureUVOffset(92, 93).addCuboid(-19, -9.625F, -26.3F, 12, 7, 0, 0, true);

		outer_roof_4_r4 = createModelPart();
		outer_roof_4_r4.setPivot(6, -42, -12);
		side_1.addChild(outer_roof_4_r4);
		setRotationAngle(outer_roof_4_r4, 0.3491F, 0, 0.1745F);
		outer_roof_4_r4.setTextureUVOffset(81, 82).addCuboid(0, 0, -11, 11, 0, 11, 0, false);

		outer_roof_2_r4 = createModelPart();
		outer_roof_2_r4.setPivot(17.587F, -36.3849F, 0);
		side_1.addChild(outer_roof_2_r4);
		setRotationAngle(outer_roof_2_r4, 0, 0, -0.1107F);
		outer_roof_2_r4.setTextureUVOffset(0, 32).addCuboid(0, -1, -18, 0, 2, 6, 0, false);

		outer_roof_1_r4 = createModelPart();
		outer_roof_1_r4.setPivot(20, -14, 0);
		side_1.addChild(outer_roof_1_r4);
		setRotationAngle(outer_roof_1_r4, 0, 0, -0.1107F);
		outer_roof_1_r4.setTextureUVOffset(37, 225).addCuboid(0, -22, -18, 1, 4, 14, 0, true);
		outer_roof_1_r4.setTextureUVOffset(17, 0).addCuboid(-1, -19, -18, 1, 19, 0, 0, false);

		outer_roof_3_r4 = createModelPart();
		outer_roof_3_r4.setPivot(15.813F, -37.5414F, -17.4163F);
		side_1.addChild(outer_roof_3_r4);
		setRotationAngle(outer_roof_3_r4, 0.1745F, 0, 0.7418F);
		outer_roof_3_r4.setTextureUVOffset(6, 49).addCuboid(-3.5F, 0, -5.5F, 7, 0, 11, 0, false);

		front_side_lower_1_r1 = createModelPart();
		front_side_lower_1_r1.setPivot(20, 0, -18);
		side_1.addChild(front_side_lower_1_r1);
		setRotationAngle(front_side_lower_1_r1, 0, 0.1745F, 0);
		front_side_lower_1_r1.setTextureUVOffset(184, 183).addCuboid(0, -14, -11, 0, 20, 11, 0, false);

		front_side_upper_1_r1 = createModelPart();
		front_side_upper_1_r1.setPivot(20, -14, -18);
		side_1.addChild(front_side_upper_1_r1);
		setRotationAngle(front_side_upper_1_r1, 0, 0.1745F, -0.1107F);
		front_side_upper_1_r1.setTextureUVOffset(166, 100).addCuboid(0, -23, -11, 0, 23, 11, 0, false);

		side_2 = createModelPart();
		side_2.setPivot(-21, 0, -9);
		front.addChild(side_2);
		side_2.setTextureUVOffset(17, 0).addCuboid(1, -14, -9, 1, 14, 0, 0, false);

		front_side_bottom_2_r1 = createModelPart();
		front_side_bottom_2_r1.setPivot(0, 0, -4);
		side_2.addChild(front_side_bottom_2_r1);
		setRotationAngle(front_side_bottom_2_r1, 0, -0.1745F, -0.1745F);
		front_side_bottom_2_r1.setTextureUVOffset(0, 17).addCuboid(0, 0, -16, 0, 7, 23, 0, false);

		outer_roof_4_r5 = createModelPart();
		outer_roof_4_r5.setPivot(5.187F, -37.5414F, -8.4163F);
		side_2.addChild(outer_roof_4_r5);
		setRotationAngle(outer_roof_4_r5, 0.1745F, 0, -0.7418F);
		outer_roof_4_r5.setTextureUVOffset(6, 49).addCuboid(-3.5F, 0, -5.5F, 7, 0, 11, 0, true);

		outer_roof_7_r1 = createModelPart();
		outer_roof_7_r1.setPivot(3.413F, -36.3849F, 9);
		side_2.addChild(outer_roof_7_r1);
		setRotationAngle(outer_roof_7_r1, 0, 0, 0.1107F);
		outer_roof_7_r1.setTextureUVOffset(0, 32).addCuboid(0, -1, -18, 0, 2, 6, 0, false);

		outer_roof_5_r4 = createModelPart();
		outer_roof_5_r4.setPivot(15, -42, -3);
		side_2.addChild(outer_roof_5_r4);
		setRotationAngle(outer_roof_5_r4, 0.3491F, 0, -0.1745F);
		outer_roof_5_r4.setTextureUVOffset(81, 82).addCuboid(-11, 0, -11, 11, 0, 11, 0, true);

		outer_roof_5_r5 = createModelPart();
		outer_roof_5_r5.setPivot(1, -14, 9);
		side_2.addChild(outer_roof_5_r5);
		setRotationAngle(outer_roof_5_r5, 0, 0, 0.1107F);
		outer_roof_5_r5.setTextureUVOffset(37, 225).addCuboid(-1, -22, -18, 1, 4, 14, 0, false);
		outer_roof_5_r5.setTextureUVOffset(17, 0).addCuboid(0, -19, -18, 1, 19, 0, 0, false);

		front_side_upper_2_r1 = createModelPart();
		front_side_upper_2_r1.setPivot(1, -14, -9);
		side_2.addChild(front_side_upper_2_r1);
		setRotationAngle(front_side_upper_2_r1, 0, -0.1745F, 0.1107F);
		front_side_upper_2_r1.setTextureUVOffset(166, 100).addCuboid(0, -23, -11, 0, 23, 11, 0, false);

		front_side_lower_2_r1 = createModelPart();
		front_side_lower_2_r1.setPivot(1, 0, -9);
		side_2.addChild(front_side_lower_2_r1);
		setRotationAngle(front_side_lower_2_r1, 0, -0.1745F, 0);
		front_side_lower_2_r1.setTextureUVOffset(184, 183).addCuboid(0, -14, -11, 0, 20, 11, 0, false);

		coupler = createModelPart();
		coupler.setPivot(0, 0, 0);
		front.addChild(coupler);
		coupler.setTextureUVOffset(260, 275).addCuboid(-4, 0.55F, -29, 8, 6, 11, 0, false);

		headlight_panel_left = createModelPart();
		headlight_panel_left.setPivot(0, 0, 0);
		head_exterior.addChild(headlight_panel_left);


		headlight_panel_bottom_r1 = createModelPart();
		headlight_panel_bottom_r1.setPivot(0, 0, 0);
		headlight_panel_left.addChild(headlight_panel_bottom_r1);
		setRotationAngle(headlight_panel_bottom_r1, -1.3963F, 0, 0);
		headlight_panel_bottom_r1.setTextureUVOffset(122, 41).addCuboid(-17, 21.475F, 1.1F, 10, 6, 1, 0, false);

		headlight_panel_main_r1 = createModelPart();
		headlight_panel_main_r1.setPivot(-12, 3.849F, -27.2491F);
		headlight_panel_left.addChild(headlight_panel_main_r1);
		setRotationAngle(headlight_panel_main_r1, 0.3491F, 0, 0);
		headlight_panel_main_r1.setTextureUVOffset(144, 41).addCuboid(-5, -3, -0.5F, 10, 6, 1, 0, false);

		headlight_panel_right = createModelPart();
		headlight_panel_right.setPivot(0, 0, 0);
		head_exterior.addChild(headlight_panel_right);


		headlight_panel_bottom_r2 = createModelPart();
		headlight_panel_bottom_r2.setPivot(0, 0, 0);
		headlight_panel_right.addChild(headlight_panel_bottom_r2);
		setRotationAngle(headlight_panel_bottom_r2, -1.3963F, 0, 0);
		headlight_panel_bottom_r2.setTextureUVOffset(122, 41).addCuboid(7, 21.475F, 1.1F, 10, 6, 1, 0, true);

		headlight_panel_main_r2 = createModelPart();
		headlight_panel_main_r2.setPivot(12, 3.849F, -27.2491F);
		headlight_panel_right.addChild(headlight_panel_main_r2);
		setRotationAngle(headlight_panel_main_r2, 0.3491F, 0, 0);
		headlight_panel_main_r2.setTextureUVOffset(144, 41).addCuboid(-5, -3, -0.5F, 10, 6, 1, 0, true);

		headlights = createModelPart();
		headlights.setPivot(0, 24, 0);
		setRotationAngle(headlights, 0.3491F, 0, 0);
		headlights.setTextureUVOffset(17, 19).addCuboid(8.5F, -7.2029F, -27.5223F, 2, 2, 0, 0, true);
		headlights.setTextureUVOffset(17, 19).addCuboid(-10.5F, -7.2029F, -27.5223F, 2, 2, 0, 0, false);

		tail_lights = createModelPart();
		tail_lights.setPivot(0, 24, 0);
		setRotationAngle(tail_lights, 0.3491F, 0, 0);
		tail_lights.setTextureUVOffset(32, 36).addCuboid(11.75F, -7.1029F, -27.4723F, 4, 2, 0, 0, true);
		tail_lights.setTextureUVOffset(32, 36).addCuboid(-15.775F, -7.0029F, -27.4723F, 4, 2, 0, 0, false);

		door_light = createModelPart();
		door_light.setPivot(0, 24, 0);


		outer_roof_1_r5 = createModelPart();
		outer_roof_1_r5.setPivot(-20, -14, 0);
		door_light.addChild(outer_roof_1_r5);
		setRotationAngle(outer_roof_1_r5, 0, 0, 0.1107F);
		outer_roof_1_r5.setTextureUVOffset(32, 28).addCuboid(-1.1F, -22, -2, 0, 4, 4, 0, false);

		door_light_on = createModelPart();
		door_light_on.setPivot(0, 24, 0);


		light_r2 = createModelPart();
		light_r2.setPivot(-20, -14, 0);
		door_light_on.addChild(light_r2);
		setRotationAngle(light_r2, 0, 0, 0.1107F);
		light_r2.setTextureUVOffset(0, 50).addCuboid(-1, -20, 0, 0, 0, 0, 0.4F, false);

		door_light_off = createModelPart();
		door_light_off.setPivot(0, 24, 0);


		light_r3 = createModelPart();
		light_r3.setPivot(-20, -14, 0);
		door_light_off.addChild(light_r3);
		setRotationAngle(light_r3, 0, 0, 0.1107F);
		light_r3.setTextureUVOffset(0, 52).addCuboid(-1, -20, 0, 0, 0, 0, 0.4F, false);

		buildModel();
	}

	private static final int DOOR_MAX = 14;
	private static final ModelDoorOverlay MODEL_DOOR_OVERLAY = new ModelDoorOverlay(DOOR_MAX, 6.34F, "door_overlay_cm_stock_left.png", "door_overlay_cm_stock_right.png");

	@Override
	public ModelCMStock createNew(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		return new ModelCMStock(doorAnimationType, renderDoorOverlay);
	}

	@Override
	protected void renderWindowPositions(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
		switch (renderStage) {
			case LIGHTS:
				renderOnce(roof_light, graphicsHolder, light, position);
				break;
			case INTERIOR:
				renderMirror(window, graphicsHolder, light, position);
				if (renderDetails) {
					renderMirror(window_handrails, graphicsHolder, light, position);
					renderMirror(roof_window, graphicsHolder, light, position);
				}
				break;
			case INTERIOR_TRANSLUCENT:
				renderMirror(side_panel_translucent, graphicsHolder, light, position - 22);
				renderMirror(side_panel_translucent, graphicsHolder, light, position + 22);
				break;
			case EXTERIOR:
				renderMirror(window_exterior, graphicsHolder, light, position);
				renderMirror(roof_exterior, graphicsHolder, light, position);
				break;
		}
	}

	@Override
	protected void renderDoorPositions(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
		final boolean middleDoor = isIndex(getDoorPositions().length / 2, position, getDoorPositions());
		final boolean doorOpen = doorLeftZ > 0 || doorRightZ > 0;
		final boolean notLastDoor = !isIndex(0, position, getDoorPositions()) && !isIndex(-1, position, getDoorPositions());

		switch (renderStage) {
			case LIGHTS:
				if (notLastDoor) {
					renderOnce(roof_door_light, graphicsHolder, light, position);
				}
				if (middleDoor && doorOpen) {
					renderMirror(door_light_on, graphicsHolder, light, position - 23);
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
					renderOnce(door_handrail, graphicsHolder, light, position);
					if (notLastDoor) {
						renderMirror(roof_door, graphicsHolder, light, position);
					}
				}
				break;
			case EXTERIOR:
				door_left_exterior.setOffset(0, 0, doorRightZ);
				door_right_exterior.setOffset(0, 0, -doorRightZ);
				renderOnce(door_exterior, graphicsHolder, light, position);
				door_left_exterior.setOffset(0, 0, doorLeftZ);
				door_right_exterior.setOffset(0, 0, -doorLeftZ);
				renderOnceFlipped(door_exterior, graphicsHolder, light, position);
				renderMirror(roof_exterior, graphicsHolder, light, position);
				if (middleDoor && renderDetails) {
					renderMirror(door_light, graphicsHolder, light, position - 23);
					if (!doorOpen) {
						renderMirror(door_light_off, graphicsHolder, light, position - 23);
					}
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
				if (renderDetails) {
					renderOnce(roof_end, graphicsHolder, light, position);
				}
				break;
			case EXTERIOR:
				renderOnce(head_exterior, graphicsHolder, light, position);
				renderOnce(roof_end_exterior, graphicsHolder, light, position);
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
				if (renderDetails) {
					renderOnceFlipped(roof_end, graphicsHolder, light, position);
				}
				break;
			case EXTERIOR:
				renderOnceFlipped(head_exterior, graphicsHolder, light, position);
				renderOnceFlipped(roof_end_exterior, graphicsHolder, light, position);
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