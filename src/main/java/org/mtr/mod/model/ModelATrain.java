package org.mtr.mod.model;

import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.ModelPartExtension;
import org.mtr.mod.client.DoorAnimationType;

public class ModelATrain extends ModelSimpleTrainBase<ModelATrain> {

	private final ModelPartExtension window_tcl;
	private final ModelPartExtension upper_wall_r1;
	private final ModelPartExtension window_tcl_handrails;
	private final ModelPartExtension handrail_8_r1;
	private final ModelPartExtension handrail_3_r1;
	private final ModelPartExtension top_handrail_3_r1;
	private final ModelPartExtension seat_back_r1;
	private final ModelPartExtension bench;
	private final ModelPartExtension window_ael;
	private final ModelPartExtension upper_wall_r2;
	private final ModelPartExtension window_exterior_tcl;
	private final ModelPartExtension upper_wall_r3;
	private final ModelPartExtension floor_r1;
	private final ModelPartExtension window_exterior_ael;
	private final ModelPartExtension upper_wall_r4;
	private final ModelPartExtension floor_r2;
	private final ModelPartExtension window_exterior_end_tcl;
	private final ModelPartExtension upper_wall_2_r1;
	private final ModelPartExtension floor_2_r1;
	private final ModelPartExtension upper_wall_1_r1;
	private final ModelPartExtension floor_1_r1;
	private final ModelPartExtension window_exterior_end_ael;
	private final ModelPartExtension upper_wall_6_r1;
	private final ModelPartExtension floor_6_r1;
	private final ModelPartExtension upper_wall_5_r1;
	private final ModelPartExtension floor_5_r1;
	private final ModelPartExtension side_panel_tcl;
	private final ModelPartExtension side_panel_tcl_translucent;
	private final ModelPartExtension side_panel_ael;
	private final ModelPartExtension side_panel_ael_translucent;
	private final ModelPartExtension roof_window_tcl;
	private final ModelPartExtension inner_roof_4_r1;
	private final ModelPartExtension inner_roof_3_r1;
	private final ModelPartExtension inner_roof_2_r1;
	private final ModelPartExtension roof_window_ael;
	private final ModelPartExtension inner_roof_5_r1;
	private final ModelPartExtension inner_roof_4_r2;
	private final ModelPartExtension inner_roof_3_r2;
	private final ModelPartExtension roof_door_tcl;
	private final ModelPartExtension inner_roof_4_r3;
	private final ModelPartExtension inner_roof_3_r3;
	private final ModelPartExtension inner_roof_2_r2;
	private final ModelPartExtension handrail_2_r1;
	private final ModelPartExtension roof_door_ael;
	private final ModelPartExtension display_main_r1;
	private final ModelPartExtension display_6_r1;
	private final ModelPartExtension display_5_r1;
	private final ModelPartExtension display_4_r1;
	private final ModelPartExtension display_3_r1;
	private final ModelPartExtension display_2_r1;
	private final ModelPartExtension display_1_r1;
	private final ModelPartExtension inner_roof_4_r4;
	private final ModelPartExtension inner_roof_3_r4;
	private final ModelPartExtension inner_roof_2_r3;
	private final ModelPartExtension roof_exterior;
	private final ModelPartExtension outer_roof_6_r1;
	private final ModelPartExtension outer_roof_5_r1;
	private final ModelPartExtension outer_roof_4_r1;
	private final ModelPartExtension outer_roof_3_r1;
	private final ModelPartExtension door_tcl;
	private final ModelPartExtension door_left_tcl;
	private final ModelPartExtension door_left_top_r1;
	private final ModelPartExtension door_right_tcl;
	private final ModelPartExtension door_right_top_r1;
	private final ModelPartExtension door_tcl_handrail;
	private final ModelPartExtension door_ael;
	private final ModelPartExtension door_left_ael;
	private final ModelPartExtension door_left_top_r2;
	private final ModelPartExtension door_right_ael;
	private final ModelPartExtension door_right_top_r2;
	private final ModelPartExtension door_ael_handrail;
	private final ModelPartExtension upper_wall_right_r1;
	private final ModelPartExtension lower_wall_right_r1;
	private final ModelPartExtension upper_wall_left_r1;
	private final ModelPartExtension lower_wall_left_r1;
	private final ModelPartExtension handrail_left_r1;
	private final ModelPartExtension door_exterior_tcl;
	private final ModelPartExtension upper_wall_r5;
	private final ModelPartExtension floor_r3;
	private final ModelPartExtension door_left_exterior_tcl;
	private final ModelPartExtension door_left_top_r3;
	private final ModelPartExtension door_left_base_r1;
	private final ModelPartExtension door_right_exterior_tcl;
	private final ModelPartExtension door_right_top_r3;
	private final ModelPartExtension door_right_base_r1;
	private final ModelPartExtension door_exterior_ael;
	private final ModelPartExtension upper_wall_2_r2;
	private final ModelPartExtension floor_2_r2;
	private final ModelPartExtension lower_wall_1_r1;
	private final ModelPartExtension upper_wall_1_r2;
	private final ModelPartExtension floor_1_r2;
	private final ModelPartExtension door_left_exterior_ael;
	private final ModelPartExtension door_left_top_r4;
	private final ModelPartExtension door_left_base_r2;
	private final ModelPartExtension door_right_exterior_ael;
	private final ModelPartExtension door_right_top_r4;
	private final ModelPartExtension door_right_base_r2;
	private final ModelPartExtension door_exterior_end;
	private final ModelPartExtension upper_wall_r6;
	private final ModelPartExtension floor_r4;
	private final ModelPartExtension door_left_exterior_end;
	private final ModelPartExtension door_left_top_r5;
	private final ModelPartExtension door_left_base_r3;
	private final ModelPartExtension door_right_exterior_end;
	private final ModelPartExtension door_right_top_r5;
	private final ModelPartExtension door_right_base_r3;
	private final ModelPartExtension luggage_rack;
	private final ModelPartExtension top_3_r1;
	private final ModelPartExtension top_2_r1;
	private final ModelPartExtension top_1_r1;
	private final ModelPartExtension upper_wall_r7;
	private final ModelPartExtension end_tcl;
	private final ModelPartExtension upper_wall_2_r3;
	private final ModelPartExtension upper_wall_1_r3;
	private final ModelPartExtension end_ael;
	private final ModelPartExtension end_exterior_tcl;
	private final ModelPartExtension upper_wall_2_r4;
	private final ModelPartExtension upper_wall_1_r4;
	private final ModelPartExtension floor_2_r3;
	private final ModelPartExtension floor_1_r3;
	private final ModelPartExtension end_exterior_ael;
	private final ModelPartExtension upper_wall_3_r1;
	private final ModelPartExtension upper_wall_2_r5;
	private final ModelPartExtension floor_3_r1;
	private final ModelPartExtension floor_2_r4;
	private final ModelPartExtension end_door_ael;
	private final ModelPartExtension roof_end;
	private final ModelPartExtension handrail_2_r2;
	private final ModelPartExtension inner_roof_1;
	private final ModelPartExtension inner_roof_4_r5;
	private final ModelPartExtension inner_roof_3_r5;
	private final ModelPartExtension inner_roof_2_r4;
	private final ModelPartExtension inner_roof_2;
	private final ModelPartExtension inner_roof_4_r6;
	private final ModelPartExtension inner_roof_3_r6;
	private final ModelPartExtension inner_roof_2_r5;
	private final ModelPartExtension roof_end_exterior;
	private final ModelPartExtension vent_2_r1;
	private final ModelPartExtension vent_1_r1;
	private final ModelPartExtension outer_roof_1;
	private final ModelPartExtension outer_roof_6_r2;
	private final ModelPartExtension outer_roof_5_r2;
	private final ModelPartExtension outer_roof_4_r2;
	private final ModelPartExtension outer_roof_3_r2;
	private final ModelPartExtension outer_roof_2;
	private final ModelPartExtension outer_roof_5_r3;
	private final ModelPartExtension outer_roof_4_r3;
	private final ModelPartExtension outer_roof_3_r3;
	private final ModelPartExtension outer_roof_6_r3;
	private final ModelPartExtension roof_light_tcl;
	private final ModelPartExtension roof_light_r1;
	private final ModelPartExtension roof_light_door_ael;
	private final ModelPartExtension roof_light_window_ael;
	private final ModelPartExtension inner_roof_3_r7;
	private final ModelPartExtension roof_end_light;
	private final ModelPartExtension roof_light_2_r1;
	private final ModelPartExtension roof_light_1_r1;
	private final ModelPartExtension head_tcl;
	private final ModelPartExtension upper_wall_2_r6;
	private final ModelPartExtension upper_wall_1_r5;
	private final ModelPartExtension head_ael;
	private final ModelPartExtension head_exterior;
	private final ModelPartExtension upper_wall_2_r7;
	private final ModelPartExtension upper_wall_1_r6;
	private final ModelPartExtension floor_2_r5;
	private final ModelPartExtension floor_1_r4;
	private final ModelPartExtension front;
	private final ModelPartExtension front_bottom_5_r1;
	private final ModelPartExtension front_bottom_4_r1;
	private final ModelPartExtension front_bottom_2_r1;
	private final ModelPartExtension front_bottom_1_r1;
	private final ModelPartExtension front_panel_3_r1;
	private final ModelPartExtension front_panel_2_r1;
	private final ModelPartExtension front_panel_1_r1;
	private final ModelPartExtension side_1;
	private final ModelPartExtension front_side_bottom_2_r1;
	private final ModelPartExtension front_middle_top_r1;
	private final ModelPartExtension outer_roof_8_r1;
	private final ModelPartExtension outer_roof_7_r1;
	private final ModelPartExtension outer_roof_6_r4;
	private final ModelPartExtension front_panel_8_r1;
	private final ModelPartExtension front_panel_7_r1;
	private final ModelPartExtension front_panel_6_r1;
	private final ModelPartExtension front_panel_5_r1;
	private final ModelPartExtension front_panel_4_r1;
	private final ModelPartExtension front_side_upper_2_r1;
	private final ModelPartExtension front_side_lower_2_r1;
	private final ModelPartExtension side_2;
	private final ModelPartExtension front_side_bottom_2_r2;
	private final ModelPartExtension front_middle_top_r2;
	private final ModelPartExtension outer_roof_8_r2;
	private final ModelPartExtension outer_roof_7_r2;
	private final ModelPartExtension outer_roof_6_r5;
	private final ModelPartExtension front_panel_8_r2;
	private final ModelPartExtension front_panel_7_r2;
	private final ModelPartExtension front_panel_6_r2;
	private final ModelPartExtension front_panel_5_r2;
	private final ModelPartExtension front_panel_4_r2;
	private final ModelPartExtension front_side_upper_2_r2;
	private final ModelPartExtension front_side_lower_2_r2;
	private final ModelPartExtension headlights;
	private final ModelPartExtension headlight_2b_r1;
	private final ModelPartExtension headlight_2a_r1;
	private final ModelPartExtension headlight_1a_r1;
	private final ModelPartExtension tail_lights;
	private final ModelPartExtension tail_light_2a_r1;
	private final ModelPartExtension tail_light_1a_r1;
	private final ModelPartExtension seat;
	private final ModelPartExtension top_right_r1;
	private final ModelPartExtension top_left_r1;
	private final ModelPartExtension back_right_r1;
	private final ModelPartExtension back_left_r1;
	private final ModelPartExtension back_r1;
	private final ModelPartExtension door_light_on;
	private final ModelPartExtension light_r1;
	private final ModelPartExtension door_light_off;
	private final ModelPartExtension light_r2;

	protected final boolean isAel;

	public ModelATrain(boolean isAel) {
		this(isAel, DoorAnimationType.PLUG_FAST, true);
	}

	protected ModelATrain(boolean isAel, DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		super(336, 336, doorAnimationType, renderDoorOverlay);
		this.isAel = isAel;

		window_tcl = createModelPart();
		window_tcl.setPivot(0, 24, 0);
		window_tcl.setTextureUVOffset(0, 0).addCuboid(-20, 0, -24, 20, 1, 48, 0, false);
		window_tcl.setTextureUVOffset(0, 120).addCuboid(-21, -14, -26, 3, 14, 52, 0, false);

		upper_wall_r1 = createModelPart();
		upper_wall_r1.setPivot(-21, -14, 0);
		window_tcl.addChild(upper_wall_r1);
		setRotationAngle(upper_wall_r1, 0, 0, 0.1396F);
		upper_wall_r1.setTextureUVOffset(0, 49).addCuboid(0, -19, -26, 3, 19, 52, 0, false);

		window_tcl_handrails = createModelPart();
		window_tcl_handrails.setPivot(0, 24, 0);
		window_tcl_handrails.setTextureUVOffset(142, 76).addCuboid(-18, -6, -22, 7, 1, 44, 0, false);
		window_tcl_handrails.setTextureUVOffset(180, 121).addCuboid(-18, -5, -21, 5, 5, 42, 0, false);
		window_tcl_handrails.setTextureUVOffset(4, 0).addCuboid(0, -35, -22, 0, 35, 0, 0.2F, false);
		window_tcl_handrails.setTextureUVOffset(4, 0).addCuboid(0, -35, 22, 0, 35, 0, 0.2F, false);
		window_tcl_handrails.setTextureUVOffset(42, 40).addCuboid(-1, -32, -5.5F, 2, 4, 0, 0, false);
		window_tcl_handrails.setTextureUVOffset(42, 40).addCuboid(-1, -32, -16.5F, 2, 4, 0, 0, false);
		window_tcl_handrails.setTextureUVOffset(42, 40).addCuboid(-1, -32, 5.5F, 2, 4, 0, 0, false);
		window_tcl_handrails.setTextureUVOffset(42, 40).addCuboid(-1, -32, 16.5F, 2, 4, 0, 0, false);
		window_tcl_handrails.setTextureUVOffset(4, 0).addCuboid(0, -35, 0, 0, 35, 0, 0.2F, false);

		handrail_8_r1 = createModelPart();
		handrail_8_r1.setPivot(0, 0, 0);
		window_tcl_handrails.addChild(handrail_8_r1);
		setRotationAngle(handrail_8_r1, -1.5708F, 0, 0);
		handrail_8_r1.setTextureUVOffset(0, 0).addCuboid(0, -24, -31.5F, 0, 48, 0, 0.2F, false);

		handrail_3_r1 = createModelPart();
		handrail_3_r1.setPivot(-11, -5, 0);
		window_tcl_handrails.addChild(handrail_3_r1);
		setRotationAngle(handrail_3_r1, 0, 0, -0.0698F);
		handrail_3_r1.setTextureUVOffset(8, 0).addCuboid(0, -27.2F, 22, 0, 27, 0, 0.2F, false);
		handrail_3_r1.setTextureUVOffset(0, 0).addCuboid(0, -27.2F, 0, 0, 4, 0, 0.2F, false);
		handrail_3_r1.setTextureUVOffset(8, 0).addCuboid(0, -27.2F, -22, 0, 27, 0, 0.2F, false);

		top_handrail_3_r1 = createModelPart();
		top_handrail_3_r1.setPivot(-11, -5, 0);
		window_tcl_handrails.addChild(top_handrail_3_r1);
		setRotationAngle(top_handrail_3_r1, -1.5708F, 0, -0.0698F);
		top_handrail_3_r1.setTextureUVOffset(0, 0).addCuboid(0, -22, -23, 0, 44, 0, 0.2F, false);

		seat_back_r1 = createModelPart();
		seat_back_r1.setPivot(-17, -6, 0);
		window_tcl_handrails.addChild(seat_back_r1);
		setRotationAngle(seat_back_r1, 0, 0, -0.0524F);
		seat_back_r1.setTextureUVOffset(116, 175).addCuboid(-1, -8, -22, 1, 8, 44, 0, false);

		bench = createModelPart();
		bench.setPivot(0, 0, 0);
		window_tcl_handrails.addChild(bench);


		window_ael = createModelPart();
		window_ael.setPivot(0, 24, 0);
		window_ael.setTextureUVOffset(22, 22).addCuboid(-20, 0, -13, 20, 1, 26, 0, false);
		window_ael.setTextureUVOffset(28, 146).addCuboid(-21, -14, -13, 3, 14, 26, 0, false);

		upper_wall_r2 = createModelPart();
		upper_wall_r2.setPivot(-21, -14, 0);
		window_ael.addChild(upper_wall_r2);
		setRotationAngle(upper_wall_r2, 0, 0, 0.1396F);
		upper_wall_r2.setTextureUVOffset(26, 75).addCuboid(0, -19, -13, 3, 19, 26, 0, false);

		window_exterior_tcl = createModelPart();
		window_exterior_tcl.setPivot(0, 24, 0);
		window_exterior_tcl.setTextureUVOffset(58, 73).addCuboid(-21, -14, -26, 0, 14, 52, 0, false);

		upper_wall_r3 = createModelPart();
		upper_wall_r3.setPivot(-21, -14, 0);
		window_exterior_tcl.addChild(upper_wall_r3);
		setRotationAngle(upper_wall_r3, 0, 0, 0.1396F);
		upper_wall_r3.setTextureUVOffset(58, 0).addCuboid(0, -23, -26, 0, 23, 52, 0, false);

		floor_r1 = createModelPart();
		floor_r1.setPivot(-21, 0, 0);
		window_exterior_tcl.addChild(floor_r1);
		setRotationAngle(floor_r1, 0, 0, -0.1745F);
		floor_r1.setTextureUVOffset(62, 139).addCuboid(0, 0, -24, 1, 8, 48, 0, false);

		window_exterior_ael = createModelPart();
		window_exterior_ael.setPivot(0, 24, 0);
		window_exterior_ael.setTextureUVOffset(58, 99).addCuboid(-21, -14, -13, 0, 14, 26, 0, false);

		upper_wall_r4 = createModelPart();
		upper_wall_r4.setPivot(-21, -14, 0);
		window_exterior_ael.addChild(upper_wall_r4);
		setRotationAngle(upper_wall_r4, 0, 0, 0.1396F);
		upper_wall_r4.setTextureUVOffset(58, 26).addCuboid(0, -23, -13, 0, 23, 26, 0, false);

		floor_r2 = createModelPart();
		floor_r2.setPivot(-21, 0, 0);
		window_exterior_ael.addChild(floor_r2);
		setRotationAngle(floor_r2, 0, 0, -0.1745F);
		floor_r2.setTextureUVOffset(84, 161).addCuboid(0, 0, -13, 1, 8, 26, 0, false);

		window_exterior_end_tcl = createModelPart();
		window_exterior_end_tcl.setPivot(0, 24, 0);
		window_exterior_end_tcl.setTextureUVOffset(212, 144).addCuboid(21, -14, -26, 0, 14, 52, 0, true);
		window_exterior_end_tcl.setTextureUVOffset(212, 144).addCuboid(-21, -14, -26, 0, 14, 52, 0, false);

		upper_wall_2_r1 = createModelPart();
		upper_wall_2_r1.setPivot(-21, -14, 0);
		window_exterior_end_tcl.addChild(upper_wall_2_r1);
		setRotationAngle(upper_wall_2_r1, 0, 0, 0.1396F);
		upper_wall_2_r1.setTextureUVOffset(58, 0).addCuboid(0, -23, -26, 0, 23, 52, 0, false);

		floor_2_r1 = createModelPart();
		floor_2_r1.setPivot(-21, 0, 0);
		window_exterior_end_tcl.addChild(floor_2_r1);
		setRotationAngle(floor_2_r1, 0, 0, -0.1745F);
		floor_2_r1.setTextureUVOffset(8, 272).addCuboid(0, 0, -24, 1, 8, 48, 0, false);

		upper_wall_1_r1 = createModelPart();
		upper_wall_1_r1.setPivot(21, -14, 0);
		window_exterior_end_tcl.addChild(upper_wall_1_r1);
		setRotationAngle(upper_wall_1_r1, 0, 0, -0.1396F);
		upper_wall_1_r1.setTextureUVOffset(58, 0).addCuboid(0, -23, -26, 0, 23, 52, 0, true);

		floor_1_r1 = createModelPart();
		floor_1_r1.setPivot(21, 0, 0);
		window_exterior_end_tcl.addChild(floor_1_r1);
		setRotationAngle(floor_1_r1, 0, 0, 0.1745F);
		floor_1_r1.setTextureUVOffset(8, 272).addCuboid(-1, 0, -24, 1, 8, 48, 0, true);

		window_exterior_end_ael = createModelPart();
		window_exterior_end_ael.setPivot(0, 24, 0);
		window_exterior_end_ael.setTextureUVOffset(0, 210).addCuboid(21, -14, -2, 0, 14, 26, 0, true);
		window_exterior_end_ael.setTextureUVOffset(0, 210).addCuboid(-21, -14, -2, 0, 14, 26, 0, false);
		window_exterior_end_ael.setTextureUVOffset(0, 224).addCuboid(21, -14, 24, 0, 14, 26, 0, true);
		window_exterior_end_ael.setTextureUVOffset(0, 224).addCuboid(-21, -14, 24, 0, 14, 26, 0, false);
		window_exterior_end_ael.setTextureUVOffset(0, 238).addCuboid(21, -14, 50, 0, 14, 26, 0, true);
		window_exterior_end_ael.setTextureUVOffset(0, 238).addCuboid(-21, -14, 50, 0, 14, 26, 0, false);

		upper_wall_6_r1 = createModelPart();
		upper_wall_6_r1.setPivot(-21, -14, 0);
		window_exterior_end_ael.addChild(upper_wall_6_r1);
		setRotationAngle(upper_wall_6_r1, 0, 0, 0.1396F);
		upper_wall_6_r1.setTextureUVOffset(58, 26).addCuboid(0, -23, 50, 0, 23, 26, 0, false);
		upper_wall_6_r1.setTextureUVOffset(58, 26).addCuboid(0, -23, 24, 0, 23, 26, 0, false);
		upper_wall_6_r1.setTextureUVOffset(58, 26).addCuboid(0, -23, -2, 0, 23, 26, 0, false);

		floor_6_r1 = createModelPart();
		floor_6_r1.setPivot(-21, 0, 0);
		window_exterior_end_ael.addChild(floor_6_r1);
		setRotationAngle(floor_6_r1, 0, 0, -0.1745F);
		floor_6_r1.setTextureUVOffset(181, 264).addCuboid(0, 0, 50, 1, 8, 26, 0, false);
		floor_6_r1.setTextureUVOffset(181, 256).addCuboid(0, 0, 24, 1, 8, 26, 0, false);
		floor_6_r1.setTextureUVOffset(181, 248).addCuboid(0, 0, -2, 1, 8, 26, 0, false);

		upper_wall_5_r1 = createModelPart();
		upper_wall_5_r1.setPivot(21, -14, 0);
		window_exterior_end_ael.addChild(upper_wall_5_r1);
		setRotationAngle(upper_wall_5_r1, 0, 0, -0.1396F);
		upper_wall_5_r1.setTextureUVOffset(58, 26).addCuboid(0, -23, 50, 0, 23, 26, 0, true);
		upper_wall_5_r1.setTextureUVOffset(58, 26).addCuboid(0, -23, 24, 0, 23, 26, 0, true);
		upper_wall_5_r1.setTextureUVOffset(58, 26).addCuboid(0, -23, -2, 0, 23, 26, 0, true);

		floor_5_r1 = createModelPart();
		floor_5_r1.setPivot(21, 0, 0);
		window_exterior_end_ael.addChild(floor_5_r1);
		setRotationAngle(floor_5_r1, 0, 0, 0.1745F);
		floor_5_r1.setTextureUVOffset(181, 264).addCuboid(-1, 0, 50, 1, 8, 26, 0, true);
		floor_5_r1.setTextureUVOffset(181, 256).addCuboid(-1, 0, 24, 1, 8, 26, 0, true);
		floor_5_r1.setTextureUVOffset(181, 248).addCuboid(-1, 0, -2, 1, 8, 26, 0, true);

		side_panel_tcl = createModelPart();
		side_panel_tcl.setPivot(0, 24, 0);
		side_panel_tcl.setTextureUVOffset(90, 139).addCuboid(-18, -35, 0, 7, 30, 0, 0, false);

		side_panel_tcl_translucent = createModelPart();
		side_panel_tcl_translucent.setPivot(0, 24, 0);
		side_panel_tcl_translucent.setTextureUVOffset(76, 139).addCuboid(-18, -35, 0, 7, 30, 0, 0, false);

		side_panel_ael = createModelPart();
		side_panel_ael.setPivot(0, 24, 0);
		side_panel_ael.setTextureUVOffset(26, 281).addCuboid(-18, -34, 0, 12, 34, 0, 0, false);

		side_panel_ael_translucent = createModelPart();
		side_panel_ael_translucent.setPivot(0, 24, 0);
		side_panel_ael_translucent.setTextureUVOffset(294, 108).addCuboid(-18, -34, 0, 12, 34, 0, 0, false);

		roof_window_tcl = createModelPart();
		roof_window_tcl.setPivot(0, 24, 0);
		roof_window_tcl.setTextureUVOffset(62, 0).addCuboid(-16, -32, -24, 3, 0, 48, 0, false);
		roof_window_tcl.setTextureUVOffset(52, 0).addCuboid(-5, -34.5F, -24, 5, 0, 48, 0, false);

		inner_roof_4_r1 = createModelPart();
		inner_roof_4_r1.setPivot(-3.5384F, -34.6286F, 0);
		roof_window_tcl.addChild(inner_roof_4_r1);
		setRotationAngle(inner_roof_4_r1, 0, 0, -0.1396F);
		inner_roof_4_r1.setTextureUVOffset(40, 0).addCuboid(-6, 0, -24, 6, 0, 48, 0, false);

		inner_roof_3_r1 = createModelPart();
		inner_roof_3_r1.setPivot(-10.4309F, -33.4846F, 0);
		roof_window_tcl.addChild(inner_roof_3_r1);
		setRotationAngle(inner_roof_3_r1, 0, 0, -0.3142F);
		inner_roof_3_r1.setTextureUVOffset(0, 49).addCuboid(-1, 0, -24, 2, 0, 48, 0, false);

		inner_roof_2_r1 = createModelPart();
		inner_roof_2_r1.setPivot(-13, -32, 0);
		roof_window_tcl.addChild(inner_roof_2_r1);
		setRotationAngle(inner_roof_2_r1, 0, 0, -0.6283F);
		inner_roof_2_r1.setTextureUVOffset(68, 0).addCuboid(0, 0, -24, 2, 0, 48, 0, false);

		roof_window_ael = createModelPart();
		roof_window_ael.setPivot(0, 24, 0);
		roof_window_ael.setTextureUVOffset(4, 134).addCuboid(-16, -32.3F, -13, 2, 0, 26, 0, false);
		roof_window_ael.setTextureUVOffset(12, 120).addCuboid(-4, -35.6679F, -13, 4, 0, 26, 0, false);

		inner_roof_5_r1 = createModelPart();
		inner_roof_5_r1.setPivot(-5.1202F, -33.4588F, 0);
		roof_window_ael.addChild(inner_roof_5_r1);
		setRotationAngle(inner_roof_5_r1, 0, 0, -0.1047F);
		inner_roof_5_r1.setTextureUVOffset(11, 120).addCuboid(-2.8798F, -2, -13, 5, 0, 26, 0, false);

		inner_roof_4_r2 = createModelPart();
		inner_roof_4_r2.setPivot(-9.0655F, -32.834F, 0);
		roof_window_ael.addChild(inner_roof_4_r2);
		setRotationAngle(inner_roof_4_r2, 0, 0, -0.2094F);
		inner_roof_4_r2.setTextureUVOffset(10, 120).addCuboid(-3, -2, -13, 6, 0, 26, 0, false);

		inner_roof_3_r2 = createModelPart();
		inner_roof_3_r2.setPivot(-15, -32, 0);
		roof_window_ael.addChild(inner_roof_3_r2);
		setRotationAngle(inner_roof_3_r2, 0, 0, -0.1047F);
		inner_roof_3_r2.setTextureUVOffset(0, 134).addCuboid(1, -2, -13, 2, 2, 26, 0, false);

		roof_door_tcl = createModelPart();
		roof_door_tcl.setPivot(0, 24, 0);
		roof_door_tcl.setTextureUVOffset(201, 269).addCuboid(-18, -33, -16, 5, 1, 32, 0, false);
		roof_door_tcl.setTextureUVOffset(68, 8).addCuboid(-5, -34.5F, -16, 5, 0, 32, 0, false);

		inner_roof_4_r3 = createModelPart();
		inner_roof_4_r3.setPivot(-3.5384F, -34.6286F, 0);
		roof_door_tcl.addChild(inner_roof_4_r3);
		setRotationAngle(inner_roof_4_r3, 0, 0, -0.1396F);
		inner_roof_4_r3.setTextureUVOffset(56, 8).addCuboid(-6, 0, -16, 6, 0, 32, 0, false);

		inner_roof_3_r3 = createModelPart();
		inner_roof_3_r3.setPivot(-10.4309F, -33.4846F, 0);
		roof_door_tcl.addChild(inner_roof_3_r3);
		setRotationAngle(inner_roof_3_r3, 0, 0, -0.3142F);
		inner_roof_3_r3.setTextureUVOffset(126, 0).addCuboid(-1, 0, -16, 2, 0, 32, 0, false);

		inner_roof_2_r2 = createModelPart();
		inner_roof_2_r2.setPivot(-13, -32, 0);
		roof_door_tcl.addChild(inner_roof_2_r2);
		setRotationAngle(inner_roof_2_r2, 0, 0, -0.6283F);
		inner_roof_2_r2.setTextureUVOffset(122, 0).addCuboid(0, 0, -16, 2, 0, 32, 0, false);

		handrail_2_r1 = createModelPart();
		handrail_2_r1.setPivot(0, 0, 0);
		roof_door_tcl.addChild(handrail_2_r1);
		setRotationAngle(handrail_2_r1, -1.5708F, 0, 0);
		handrail_2_r1.setTextureUVOffset(0, 0).addCuboid(0, -16, -31.5F, 0, 32, 0, 0.2F, false);

		roof_door_ael = createModelPart();
		roof_door_ael.setPivot(0, 24, 0);
		roof_door_ael.setTextureUVOffset(211, 277).addCuboid(-18, -33, -12, 3, 1, 24, 0, false);
		roof_door_ael.setTextureUVOffset(209, 179).addCuboid(-3.1311F, -33.6679F, -28, 4, 0, 56, 0, false);

		display_main_r1 = createModelPart();
		display_main_r1.setPivot(0, -33.4588F, -28);
		roof_door_ael.addChild(display_main_r1);
		setRotationAngle(display_main_r1, 0.1745F, 0, 0);
		display_main_r1.setTextureUVOffset(60, 156).addCuboid(-10, -2.5412F, -0.1F, 20, 4, 0, 0, false);

		display_6_r1 = createModelPart();
		display_6_r1.setPivot(-1.1311F, -33.6679F, 28);
		roof_door_ael.addChild(display_6_r1);
		setRotationAngle(display_6_r1, -0.1745F, 0, 0);
		display_6_r1.setTextureUVOffset(34, 146).addCuboid(-2.8689F, -3, 0, 4, 3, 0, 0, false);

		display_5_r1 = createModelPart();
		display_5_r1.setPivot(-5.1202F, -33.4588F, 28);
		roof_door_ael.addChild(display_5_r1);
		setRotationAngle(display_5_r1, -0.1745F, 0, -0.1047F);
		display_5_r1.setTextureUVOffset(34, 146).addCuboid(-2.8798F, -3, 0, 5, 3, 0, 0, false);

		display_4_r1 = createModelPart();
		display_4_r1.setPivot(-9.0655F, -32.834F, 28);
		roof_door_ael.addChild(display_4_r1);
		setRotationAngle(display_4_r1, -0.1745F, 0, -0.2094F);
		display_4_r1.setTextureUVOffset(34, 146).addCuboid(-3, -3, 0, 6, 3, 0, 0, false);

		display_3_r1 = createModelPart();
		display_3_r1.setPivot(-1.1311F, -33.6679F, -28);
		roof_door_ael.addChild(display_3_r1);
		setRotationAngle(display_3_r1, 0.1745F, 0, 0);
		display_3_r1.setTextureUVOffset(34, 146).addCuboid(-2.8689F, -3, 0, 4, 3, 0, 0, false);

		display_2_r1 = createModelPart();
		display_2_r1.setPivot(-5.1202F, -33.4588F, -28);
		roof_door_ael.addChild(display_2_r1);
		setRotationAngle(display_2_r1, 0.1745F, 0, -0.1047F);
		display_2_r1.setTextureUVOffset(34, 146).addCuboid(-2.8798F, -3, 0, 5, 3, 0, 0, false);

		display_1_r1 = createModelPart();
		display_1_r1.setPivot(-9.0655F, -32.834F, -28);
		roof_door_ael.addChild(display_1_r1);
		setRotationAngle(display_1_r1, 0.1745F, 0, -0.2094F);
		display_1_r1.setTextureUVOffset(34, 146).addCuboid(-3, -3, 0, 6, 3, 0, 0, false);

		inner_roof_4_r4 = createModelPart();
		inner_roof_4_r4.setPivot(-5.1202F, -33.4588F, 0);
		roof_door_ael.addChild(inner_roof_4_r4);
		setRotationAngle(inner_roof_4_r4, 0, 0, -0.1047F);
		inner_roof_4_r4.setTextureUVOffset(201, 179).addCuboid(-2, 0, -28, 4, 0, 56, 0, false);

		inner_roof_3_r4 = createModelPart();
		inner_roof_3_r4.setPivot(-9.0655F, -32.834F, 0);
		roof_door_ael.addChild(inner_roof_3_r4);
		setRotationAngle(inner_roof_3_r4, 0, 0, -0.2094F);
		inner_roof_3_r4.setTextureUVOffset(193, 179).addCuboid(-2, 0, -28, 4, 0, 56, 0, false);

		inner_roof_2_r3 = createModelPart();
		inner_roof_2_r3.setPivot(-15, -32, 0);
		roof_door_ael.addChild(inner_roof_2_r3);
		setRotationAngle(inner_roof_2_r3, 0, 0, -0.1047F);
		inner_roof_2_r3.setTextureUVOffset(225, 195).addCuboid(0, 0, -12, 4, 0, 24, 0, false);

		roof_exterior = createModelPart();
		roof_exterior.setPivot(0, 24, 0);


		outer_roof_6_r1 = createModelPart();
		outer_roof_6_r1.setPivot(-2.339F, -41.5711F, 0);
		roof_exterior.addChild(outer_roof_6_r1);
		setRotationAngle(outer_roof_6_r1, 0, 0, 1.5708F);
		outer_roof_6_r1.setTextureUVOffset(106, 274).addCuboid(0, -3, -20, 0, 6, 40, 0, false);

		outer_roof_5_r1 = createModelPart();
		outer_roof_5_r1.setPivot(-9.7706F, -40.7897F, 0);
		roof_exterior.addChild(outer_roof_5_r1);
		setRotationAngle(outer_roof_5_r1, 0, 0, 1.3963F);
		outer_roof_5_r1.setTextureUVOffset(106, 280).addCuboid(0, -4.5F, -20, 0, 9, 40, 0, false);

		outer_roof_4_r1 = createModelPart();
		outer_roof_4_r1.setPivot(-15.501F, -39.2584F, 0);
		roof_exterior.addChild(outer_roof_4_r1);
		setRotationAngle(outer_roof_4_r1, 0, 0, 1.0472F);
		outer_roof_4_r1.setTextureUVOffset(106, 289).addCuboid(0, -1.5F, -20, 0, 3, 40, 0, false);

		outer_roof_3_r1 = createModelPart();
		outer_roof_3_r1.setPivot(-18.6652F, -37.2758F, 0);
		roof_exterior.addChild(outer_roof_3_r1);
		setRotationAngle(outer_roof_3_r1, 0, 0, 0.5236F);
		outer_roof_3_r1.setTextureUVOffset(106, 292).addCuboid(1, -2, -20, 0, 2, 40, 0, false);

		door_tcl = createModelPart();
		door_tcl.setPivot(0, 24, 0);
		door_tcl.setTextureUVOffset(0, 195).addCuboid(-20, 0, -16, 20, 1, 32, 0, false);

		door_left_tcl = createModelPart();
		door_left_tcl.setPivot(0, 0, 0);
		door_tcl.addChild(door_left_tcl);
		door_left_tcl.setTextureUVOffset(280, 168).addCuboid(-21, -14, 0, 1, 14, 14, 0, false);

		door_left_top_r1 = createModelPart();
		door_left_top_r1.setPivot(-20.8F, -14, 0);
		door_left_tcl.addChild(door_left_top_r1);
		setRotationAngle(door_left_top_r1, 0, 0, 0.1396F);
		door_left_top_r1.setTextureUVOffset(68, 279).addCuboid(-0.2F, -19, 0, 1, 19, 14, 0, false);

		door_right_tcl = createModelPart();
		door_right_tcl.setPivot(0, 0, 0);
		door_tcl.addChild(door_right_tcl);
		door_right_tcl.setTextureUVOffset(56, 233).addCuboid(-21, -14, -14, 1, 14, 14, 0, false);

		door_right_top_r1 = createModelPart();
		door_right_top_r1.setPivot(-20.8F, -14, 0);
		door_right_tcl.addChild(door_right_top_r1);
		setRotationAngle(door_right_top_r1, 0, 0, 0.1396F);
		door_right_top_r1.setTextureUVOffset(0, 190).addCuboid(-0.2F, -19, -14, 1, 19, 14, 0, false);

		door_tcl_handrail = createModelPart();
		door_tcl_handrail.setPivot(0, 24, 0);
		door_tcl_handrail.setTextureUVOffset(4, 0).addCuboid(0, -35, 0, 0, 35, 0, 0.2F, false);

		door_ael = createModelPart();
		door_ael.setPivot(0, 24, 0);
		door_ael.setTextureUVOffset(8, 203).addCuboid(-20, 0, -12, 20, 1, 24, 0, false);

		door_left_ael = createModelPart();
		door_left_ael.setPivot(0, 0, 0);
		door_ael.addChild(door_left_ael);
		door_left_ael.setTextureUVOffset(283, 171).addCuboid(-21, -14, 0, 1, 14, 11, 0, false);

		door_left_top_r2 = createModelPart();
		door_left_top_r2.setPivot(-20.8F, -14, 0);
		door_left_ael.addChild(door_left_top_r2);
		setRotationAngle(door_left_top_r2, 0, 0, 0.1396F);
		door_left_top_r2.setTextureUVOffset(71, 282).addCuboid(-0.2F, -19, 0, 1, 19, 11, 0, false);

		door_right_ael = createModelPart();
		door_right_ael.setPivot(0, 0, 0);
		door_ael.addChild(door_right_ael);
		door_right_ael.setTextureUVOffset(59, 236).addCuboid(-21, -14, -11, 1, 14, 11, 0, false);

		door_right_top_r2 = createModelPart();
		door_right_top_r2.setPivot(-20.8F, -14, 0);
		door_right_ael.addChild(door_right_top_r2);
		setRotationAngle(door_right_top_r2, 0, 0, 0.1396F);
		door_right_top_r2.setTextureUVOffset(3, 193).addCuboid(-0.2F, -19, -11, 1, 19, 11, 0, false);

		door_ael_handrail = createModelPart();
		door_ael_handrail.setPivot(0, 24, 0);
		door_ael_handrail.setTextureUVOffset(119, 195).addCuboid(-17, -18, -12.4F, 5, 1, 1, 0, false);

		upper_wall_right_r1 = createModelPart();
		upper_wall_right_r1.setPivot(-21, -14, -11);
		door_ael_handrail.addChild(upper_wall_right_r1);
		setRotationAngle(upper_wall_right_r1, 0, 0.2014F, 0.1396F);
		upper_wall_right_r1.setTextureUVOffset(212, 196).addCuboid(0, -20, -1, 5, 20, 1, 0, false);

		lower_wall_right_r1 = createModelPart();
		lower_wall_right_r1.setPivot(-21, 0, -11);
		door_ael_handrail.addChild(lower_wall_right_r1);
		setRotationAngle(lower_wall_right_r1, 0, 0.2014F, 0);
		lower_wall_right_r1.setTextureUVOffset(184, 196).addCuboid(0, -14, -1, 5, 14, 1, 0, false);

		upper_wall_left_r1 = createModelPart();
		upper_wall_left_r1.setPivot(-21, -14, 11);
		door_ael_handrail.addChild(upper_wall_left_r1);
		setRotationAngle(upper_wall_left_r1, 0, 2.9402F, 0.1396F);
		upper_wall_left_r1.setTextureUVOffset(212, 196).addCuboid(-5, -20, -1, 5, 20, 1, 0, true);

		lower_wall_left_r1 = createModelPart();
		lower_wall_left_r1.setPivot(-21, 0, 11);
		door_ael_handrail.addChild(lower_wall_left_r1);
		setRotationAngle(lower_wall_left_r1, 0, 2.9402F, 0);
		lower_wall_left_r1.setTextureUVOffset(184, 196).addCuboid(-5, -14, -1, 5, 14, 1, 0, true);

		handrail_left_r1 = createModelPart();
		handrail_left_r1.setPivot(0, 0, 0);
		door_ael_handrail.addChild(handrail_left_r1);
		setRotationAngle(handrail_left_r1, 0, 3.1416F, 0);
		handrail_left_r1.setTextureUVOffset(119, 195).addCuboid(12, -18, -12.4F, 5, 1, 1, 0, true);

		door_exterior_tcl = createModelPart();
		door_exterior_tcl.setPivot(0, 24, 0);


		upper_wall_r5 = createModelPart();
		upper_wall_r5.setPivot(-21, -14, 0);
		door_exterior_tcl.addChild(upper_wall_r5);
		setRotationAngle(upper_wall_r5, 0, 0, 0.1396F);
		upper_wall_r5.setTextureUVOffset(72, 196).addCuboid(0, -23, -16, 1, 4, 32, 0, false);

		floor_r3 = createModelPart();
		floor_r3.setPivot(-21, 0, 0);
		door_exterior_tcl.addChild(floor_r3);
		setRotationAngle(floor_r3, 0, 0, -0.1745F);
		floor_r3.setTextureUVOffset(56, 232).addCuboid(0, 0, -16, 1, 8, 32, 0, false);

		door_left_exterior_tcl = createModelPart();
		door_left_exterior_tcl.setPivot(0, 0, 0);
		door_exterior_tcl.addChild(door_left_exterior_tcl);
		door_left_exterior_tcl.setTextureUVOffset(0, 287).addCuboid(-21, -14, 0, 0, 14, 16, 0, false);

		door_left_top_r3 = createModelPart();
		door_left_top_r3.setPivot(-21, -14, 0);
		door_left_exterior_tcl.addChild(door_left_top_r3);
		setRotationAngle(door_left_top_r3, 0, 0, 0.1396F);
		door_left_top_r3.setTextureUVOffset(0, 265).addCuboid(0, -22, 0, 0, 22, 16, 0, false);

		door_left_base_r1 = createModelPart();
		door_left_base_r1.setPivot(-21, 0, 0);
		door_left_exterior_tcl.addChild(door_left_base_r1);
		setRotationAngle(door_left_base_r1, 0, 0, -0.1745F);
		door_left_base_r1.setTextureUVOffset(0, 301).addCuboid(0, 0, 0, 0, 2, 16, 0, false);

		door_right_exterior_tcl = createModelPart();
		door_right_exterior_tcl.setPivot(0, 0, 0);
		door_exterior_tcl.addChild(door_right_exterior_tcl);
		door_right_exterior_tcl.setTextureUVOffset(294, 76).addCuboid(-21, -14, -16, 0, 14, 16, 0, false);

		door_right_top_r3 = createModelPart();
		door_right_top_r3.setPivot(-21, -14, 0);
		door_right_exterior_tcl.addChild(door_right_top_r3);
		setRotationAngle(door_right_top_r3, 0, 0, 0.1396F);
		door_right_top_r3.setTextureUVOffset(294, 54).addCuboid(0, -22, -16, 0, 22, 16, 0, false);

		door_right_base_r1 = createModelPart();
		door_right_base_r1.setPivot(-21, 0, 0);
		door_right_exterior_tcl.addChild(door_right_base_r1);
		setRotationAngle(door_right_base_r1, 0, 0, -0.1745F);
		door_right_base_r1.setTextureUVOffset(294, 90).addCuboid(0, 0, -16, 0, 2, 16, 0, false);

		door_exterior_ael = createModelPart();
		door_exterior_ael.setPivot(0, 24, 0);
		door_exterior_ael.setTextureUVOffset(69, 110).addCuboid(-21, -14, -28, 0, 14, 15, 0, false);

		upper_wall_2_r2 = createModelPart();
		upper_wall_2_r2.setPivot(-21, -14, 0);
		door_exterior_ael.addChild(upper_wall_2_r2);
		setRotationAngle(upper_wall_2_r2, 0, 0, 0.1396F);
		upper_wall_2_r2.setTextureUVOffset(110, 35).addCuboid(0, -23, -28, 0, 23, 17, 0, false);
		upper_wall_2_r2.setTextureUVOffset(78, 202).addCuboid(0, -23, -13, 1, 4, 26, 0, false);

		floor_2_r2 = createModelPart();
		floor_2_r2.setPivot(-21, 0, 0);
		door_exterior_ael.addChild(floor_2_r2);
		setRotationAngle(floor_2_r2, 0, 0, -0.1745F);
		floor_2_r2.setTextureUVOffset(95, 172).addCuboid(0, 0, -28, 1, 8, 15, 0, false);
		floor_2_r2.setTextureUVOffset(62, 238).addCuboid(0, 0, -13, 1, 8, 26, 0, false);

		lower_wall_1_r1 = createModelPart();
		lower_wall_1_r1.setPivot(0, 0, 0);
		door_exterior_ael.addChild(lower_wall_1_r1);
		setRotationAngle(lower_wall_1_r1, 0, 3.1416F, 0);
		lower_wall_1_r1.setTextureUVOffset(69, 110).addCuboid(21, -14, -28, 0, 14, 15, 0, true);

		upper_wall_1_r2 = createModelPart();
		upper_wall_1_r2.setPivot(-21, -14, 0);
		door_exterior_ael.addChild(upper_wall_1_r2);
		setRotationAngle(upper_wall_1_r2, 0, 3.1416F, 0.1396F);
		upper_wall_1_r2.setTextureUVOffset(110, 35).addCuboid(0, -23, -28, 0, 23, 17, 0, true);

		floor_1_r2 = createModelPart();
		floor_1_r2.setPivot(-21, 0, 0);
		door_exterior_ael.addChild(floor_1_r2);
		setRotationAngle(floor_1_r2, 0, 3.1416F, -0.1745F);
		floor_1_r2.setTextureUVOffset(95, 172).addCuboid(-1, 0, -28, 1, 8, 15, 0, true);

		door_left_exterior_ael = createModelPart();
		door_left_exterior_ael.setPivot(0, 0, 0);
		door_exterior_ael.addChild(door_left_exterior_ael);
		door_left_exterior_ael.setTextureUVOffset(0, 290).addCuboid(-21, -14, 0, 0, 14, 13, 0, false);

		door_left_top_r4 = createModelPart();
		door_left_top_r4.setPivot(-21, -14, 0);
		door_left_exterior_ael.addChild(door_left_top_r4);
		setRotationAngle(door_left_top_r4, 0, 0, 0.1396F);
		door_left_top_r4.setTextureUVOffset(0, 268).addCuboid(0, -22, 0, 0, 22, 13, 0, false);

		door_left_base_r2 = createModelPart();
		door_left_base_r2.setPivot(-21, 0, 0);
		door_left_exterior_ael.addChild(door_left_base_r2);
		setRotationAngle(door_left_base_r2, 0, 0, -0.1745F);
		door_left_base_r2.setTextureUVOffset(0, 304).addCuboid(0, 0, 0, 0, 2, 13, 0, false);

		door_right_exterior_ael = createModelPart();
		door_right_exterior_ael.setPivot(0, 0, 0);
		door_exterior_ael.addChild(door_right_exterior_ael);
		door_right_exterior_ael.setTextureUVOffset(294, 79).addCuboid(-21, -14, -13, 0, 14, 13, 0, false);

		door_right_top_r4 = createModelPart();
		door_right_top_r4.setPivot(-21, -14, 0);
		door_right_exterior_ael.addChild(door_right_top_r4);
		setRotationAngle(door_right_top_r4, 0, 0, 0.1396F);
		door_right_top_r4.setTextureUVOffset(294, 57).addCuboid(0, -22, -13, 0, 22, 13, 0, false);

		door_right_base_r2 = createModelPart();
		door_right_base_r2.setPivot(-21, 0, 0);
		door_right_exterior_ael.addChild(door_right_base_r2);
		setRotationAngle(door_right_base_r2, 0, 0, -0.1745F);
		door_right_base_r2.setTextureUVOffset(294, 93).addCuboid(0, 0, -13, 0, 2, 13, 0, false);

		door_exterior_end = createModelPart();
		door_exterior_end.setPivot(0, 24, 0);


		upper_wall_r6 = createModelPart();
		upper_wall_r6.setPivot(-21, -14, 0);
		door_exterior_end.addChild(upper_wall_r6);
		setRotationAngle(upper_wall_r6, 0, 0, 0.1396F);
		upper_wall_r6.setTextureUVOffset(72, 196).addCuboid(0, -23, -16, 1, 4, 32, 0, false);

		floor_r4 = createModelPart();
		floor_r4.setPivot(-21, 0, 0);
		door_exterior_end.addChild(floor_r4);
		setRotationAngle(floor_r4, 0, 0, -0.1745F);
		floor_r4.setTextureUVOffset(266, 294).addCuboid(0, 0, -16, 1, 8, 32, 0, false);

		door_left_exterior_end = createModelPart();
		door_left_exterior_end.setPivot(0, 0, 0);
		door_exterior_end.addChild(door_left_exterior_end);
		door_left_exterior_end.setTextureUVOffset(162, 109).addCuboid(-21, -14, 0, 0, 14, 16, 0, false);

		door_left_top_r5 = createModelPart();
		door_left_top_r5.setPivot(-21, -14, 0);
		door_left_exterior_end.addChild(door_left_top_r5);
		setRotationAngle(door_left_top_r5, 0, 0, 0.1396F);
		door_left_top_r5.setTextureUVOffset(0, 265).addCuboid(0, -22, 0, 0, 22, 16, 0, false);

		door_left_base_r3 = createModelPart();
		door_left_base_r3.setPivot(-21, 0, 0);
		door_left_exterior_end.addChild(door_left_base_r3);
		setRotationAngle(door_left_base_r3, 0, 0, -0.1745F);
		door_left_base_r3.setTextureUVOffset(162, 123).addCuboid(0, 0, 0, 0, 2, 16, 0, false);

		door_right_exterior_end = createModelPart();
		door_right_exterior_end.setPivot(0, 0, 0);
		door_exterior_end.addChild(door_right_exterior_end);
		door_right_exterior_end.setTextureUVOffset(162, 125).addCuboid(-21, -14, -16, 0, 14, 16, 0, false);

		door_right_top_r5 = createModelPart();
		door_right_top_r5.setPivot(-21, -14, 0);
		door_right_exterior_end.addChild(door_right_top_r5);
		setRotationAngle(door_right_top_r5, 0, 0, 0.1396F);
		door_right_top_r5.setTextureUVOffset(294, 54).addCuboid(0, -22, -16, 0, 22, 16, 0, false);

		door_right_base_r3 = createModelPart();
		door_right_base_r3.setPivot(-21, 0, 0);
		door_right_exterior_end.addChild(door_right_base_r3);
		setRotationAngle(door_right_base_r3, 0, 0, -0.1745F);
		door_right_base_r3.setTextureUVOffset(162, 139).addCuboid(0, 0, -16, 0, 2, 16, 0, false);

		luggage_rack = createModelPart();
		luggage_rack.setPivot(0, 24, 0);
		luggage_rack.setTextureUVOffset(176, 218).addCuboid(-21, -14, -8, 3, 14, 16, 0, false);
		luggage_rack.setTextureUVOffset(60, 139).addCuboid(-18, -13, -8, 9, 1, 16, 0, false);
		luggage_rack.setTextureUVOffset(32, 32).addCuboid(-20, 0, -8, 20, 1, 16, 0, false);

		top_3_r1 = createModelPart();
		top_3_r1.setPivot(-9.3615F, -29.3127F, -20);
		luggage_rack.addChild(top_3_r1);
		setRotationAngle(top_3_r1, 0, 0, -0.2618F);
		top_3_r1.setTextureUVOffset(134, 125).addCuboid(-3, -4, 12, 3, 7, 16, 0, false);

		top_2_r1 = createModelPart();
		top_2_r1.setPivot(-10, -25, 0);
		luggage_rack.addChild(top_2_r1);
		setRotationAngle(top_2_r1, 0, 0, 0.7854F);
		top_2_r1.setTextureUVOffset(136, 132).addCuboid(-1, -2, -8, 1, 2, 16, 0, false);

		top_1_r1 = createModelPart();
		top_1_r1.setPivot(0, 0, 0);
		luggage_rack.addChild(top_1_r1);
		setRotationAngle(top_1_r1, 0, 0, 1.5708F);
		top_1_r1.setTextureUVOffset(136, 132).addCuboid(-26, 10, -8, 1, 8, 16, 0, false);

		upper_wall_r7 = createModelPart();
		upper_wall_r7.setPivot(-21, -14, 0);
		luggage_rack.addChild(upper_wall_r7);
		setRotationAngle(upper_wall_r7, 0, 0, 0.1396F);
		upper_wall_r7.setTextureUVOffset(142, 78).addCuboid(0, -19, -8, 3, 19, 16, 0, false);

		end_tcl = createModelPart();
		end_tcl.setPivot(0, 24, 0);
		end_tcl.setTextureUVOffset(162, 175).addCuboid(-20, 0, -12, 40, 1, 20, 0, false);
		end_tcl.setTextureUVOffset(110, 195).addCuboid(18, -14, 7, 3, 14, 3, 0, true);
		end_tcl.setTextureUVOffset(110, 195).addCuboid(-21, -14, 7, 3, 14, 3, 0, false);
		end_tcl.setTextureUVOffset(127, 231).addCuboid(9.5F, -34, -12, 9, 34, 19, 0, false);
		end_tcl.setTextureUVOffset(0, 228).addCuboid(-18.5F, -34, -12, 9, 34, 19, 0, false);
		end_tcl.setTextureUVOffset(79, 293).addCuboid(-9.5F, -35, -12, 19, 2, 19, 0, false);

		upper_wall_2_r3 = createModelPart();
		upper_wall_2_r3.setPivot(-21, -14, 0);
		end_tcl.addChild(upper_wall_2_r3);
		setRotationAngle(upper_wall_2_r3, 0, 0, 0.1396F);
		upper_wall_2_r3.setTextureUVOffset(248, 110).addCuboid(0, -19, 7, 3, 19, 3, 0, false);

		upper_wall_1_r3 = createModelPart();
		upper_wall_1_r3.setPivot(21, -14, 0);
		end_tcl.addChild(upper_wall_1_r3);
		setRotationAngle(upper_wall_1_r3, 0, 0, -0.1396F);
		upper_wall_1_r3.setTextureUVOffset(164, 227).addCuboid(-3, -19, 7, 3, 19, 3, 0, true);

		end_ael = createModelPart();
		end_ael.setPivot(0, 24, 0);
		end_ael.setTextureUVOffset(138, 173).addCuboid(-7, 0, -12, 14, 1, 10, 0, false);
		end_ael.setTextureUVOffset(128, 238).addCuboid(7, -36, -12, 11, 36, 10, 0, true);
		end_ael.setTextureUVOffset(128, 238).addCuboid(-18, -36, -12, 11, 36, 10, 0, false);
		end_ael.setTextureUVOffset(24, 186).addCuboid(-7, -36, -12, 14, 4, 10, 0, false);

		end_exterior_tcl = createModelPart();
		end_exterior_tcl.setPivot(0, 24, 0);
		end_exterior_tcl.setTextureUVOffset(0, 134).addCuboid(18, -14, -12, 3, 14, 22, 0, true);
		end_exterior_tcl.setTextureUVOffset(0, 134).addCuboid(-21, -14, -12, 3, 14, 22, 0, false);
		end_exterior_tcl.setTextureUVOffset(182, 0).addCuboid(9.5F, -34, -12, 9, 34, 0, 0, true);
		end_exterior_tcl.setTextureUVOffset(182, 0).addCuboid(-18.5F, -34, -12, 9, 34, 0, 0, false);
		end_exterior_tcl.setTextureUVOffset(240, 236).addCuboid(-18, -41, -12, 36, 8, 0, 0, false);

		upper_wall_2_r4 = createModelPart();
		upper_wall_2_r4.setPivot(-21, -14, 0);
		end_exterior_tcl.addChild(upper_wall_2_r4);
		setRotationAngle(upper_wall_2_r4, 0, 0, 0.1396F);
		upper_wall_2_r4.setTextureUVOffset(183, 251).addCuboid(0, -23, -12, 3, 23, 22, 0, false);

		upper_wall_1_r4 = createModelPart();
		upper_wall_1_r4.setPivot(21, -14, 0);
		end_exterior_tcl.addChild(upper_wall_1_r4);
		setRotationAngle(upper_wall_1_r4, 0, 0, -0.1396F);
		upper_wall_1_r4.setTextureUVOffset(183, 251).addCuboid(-3, -23, -12, 3, 23, 22, 0, true);

		floor_2_r3 = createModelPart();
		floor_2_r3.setPivot(-21, 0, 0);
		end_exterior_tcl.addChild(floor_2_r3);
		setRotationAngle(floor_2_r3, 0, 0, -0.1745F);
		floor_2_r3.setTextureUVOffset(112, 139).addCuboid(0, 0, -12, 1, 8, 20, 0, false);

		floor_1_r3 = createModelPart();
		floor_1_r3.setPivot(21, 0, 0);
		end_exterior_tcl.addChild(floor_1_r3);
		setRotationAngle(floor_1_r3, 0, 0, 0.1745F);
		floor_1_r3.setTextureUVOffset(112, 139).addCuboid(-1, 0, -12, 1, 8, 20, 0, true);

		end_exterior_ael = createModelPart();
		end_exterior_ael.setPivot(0, 24, 0);
		end_exterior_ael.setTextureUVOffset(149, 185).addCuboid(-20, 0, -12, 40, 1, 10, 0, false);
		end_exterior_ael.setTextureUVOffset(0, 134).addCuboid(18, -14, -12, 3, 14, 10, 0, true);
		end_exterior_ael.setTextureUVOffset(0, 134).addCuboid(-21, -14, -12, 3, 14, 10, 0, false);
		end_exterior_ael.setTextureUVOffset(180, 2).addCuboid(7, -32, -12, 11, 32, 0, 0, true);
		end_exterior_ael.setTextureUVOffset(180, 2).addCuboid(-18, -32, -12, 11, 32, 0, 0, false);
		end_exterior_ael.setTextureUVOffset(240, 235).addCuboid(-18, -41, -12, 36, 9, 0, 0, false);

		upper_wall_3_r1 = createModelPart();
		upper_wall_3_r1.setPivot(-21, -14, 0);
		end_exterior_ael.addChild(upper_wall_3_r1);
		setRotationAngle(upper_wall_3_r1, 0, 0, 0.1396F);
		upper_wall_3_r1.setTextureUVOffset(196, 120).addCuboid(0, -23, -12, 3, 23, 10, 0, false);

		upper_wall_2_r5 = createModelPart();
		upper_wall_2_r5.setPivot(21, -14, 0);
		end_exterior_ael.addChild(upper_wall_2_r5);
		setRotationAngle(upper_wall_2_r5, 0, 0, -0.1396F);
		upper_wall_2_r5.setTextureUVOffset(196, 120).addCuboid(-3, -23, -12, 3, 23, 10, 0, true);

		floor_3_r1 = createModelPart();
		floor_3_r1.setPivot(-21, 0, 0);
		end_exterior_ael.addChild(floor_3_r1);
		setRotationAngle(floor_3_r1, 0, 0, -0.1745F);
		floor_3_r1.setTextureUVOffset(122, 149).addCuboid(0, 0, -12, 1, 8, 10, 0, false);

		floor_2_r4 = createModelPart();
		floor_2_r4.setPivot(21, 0, 0);
		end_exterior_ael.addChild(floor_2_r4);
		setRotationAngle(floor_2_r4, 0, 0, 0.1745F);
		floor_2_r4.setTextureUVOffset(122, 149).addCuboid(-1, 0, -12, 1, 8, 10, 0, true);

		end_door_ael = createModelPart();
		end_door_ael.setPivot(0, 24, 0);
		end_door_ael.setTextureUVOffset(280, 0).addCuboid(-7, -32, -2, 14, 32, 0, 0, false);

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
		inner_roof_1.setTextureUVOffset(197, 265).addCuboid(-16, 0, -12, 5, 1, 36, 0, false);
		inner_roof_1.setTextureUVOffset(64, 0).addCuboid(-3, -1.5F, -12, 5, 0, 36, 0, false);

		inner_roof_4_r5 = createModelPart();
		inner_roof_4_r5.setPivot(-1.5384F, -1.6286F, -16);
		inner_roof_1.addChild(inner_roof_4_r5);
		setRotationAngle(inner_roof_4_r5, 0, 0, -0.1396F);
		inner_roof_4_r5.setTextureUVOffset(52, 0).addCuboid(-6, 0, 4, 6, 0, 36, 0, false);

		inner_roof_3_r5 = createModelPart();
		inner_roof_3_r5.setPivot(-8.4309F, -0.4846F, -16);
		inner_roof_1.addChild(inner_roof_3_r5);
		setRotationAngle(inner_roof_3_r5, 0, 0, -0.3142F);
		inner_roof_3_r5.setTextureUVOffset(8, 49).addCuboid(-1, 0, 4, 2, 0, 36, 0, false);

		inner_roof_2_r4 = createModelPart();
		inner_roof_2_r4.setPivot(-11, 1, -16);
		inner_roof_1.addChild(inner_roof_2_r4);
		setRotationAngle(inner_roof_2_r4, 0, 0, -0.6283F);
		inner_roof_2_r4.setTextureUVOffset(96, 0).addCuboid(0, 0, 4, 2, 0, 36, 0, false);

		inner_roof_2 = createModelPart();
		inner_roof_2.setPivot(-2, -33, 16);
		roof_end.addChild(inner_roof_2);
		inner_roof_2.setTextureUVOffset(197, 265).addCuboid(15, 0, -12, 5, 1, 36, 0, true);
		inner_roof_2.setTextureUVOffset(64, 0).addCuboid(2, -1.5F, -12, 5, 0, 36, 0, true);

		inner_roof_4_r6 = createModelPart();
		inner_roof_4_r6.setPivot(5.5384F, -1.6286F, -16);
		inner_roof_2.addChild(inner_roof_4_r6);
		setRotationAngle(inner_roof_4_r6, 0, 0, 0.1396F);
		inner_roof_4_r6.setTextureUVOffset(52, 0).addCuboid(0, 0, 4, 6, 0, 36, 0, true);

		inner_roof_3_r6 = createModelPart();
		inner_roof_3_r6.setPivot(12.4309F, -0.4846F, -16);
		inner_roof_2.addChild(inner_roof_3_r6);
		setRotationAngle(inner_roof_3_r6, 0, 0, 0.3142F);
		inner_roof_3_r6.setTextureUVOffset(8, 49).addCuboid(-1, 0, 4, 2, 0, 36, 0, true);

		inner_roof_2_r5 = createModelPart();
		inner_roof_2_r5.setPivot(15, 1, -16);
		inner_roof_2.addChild(inner_roof_2_r5);
		setRotationAngle(inner_roof_2_r5, 0, 0, 0.6283F);
		inner_roof_2_r5.setTextureUVOffset(96, 0).addCuboid(-2, 0, 4, 2, 0, 36, 0, true);

		roof_end_exterior = createModelPart();
		roof_end_exterior.setPivot(0, 24, 0);
		roof_end_exterior.setTextureUVOffset(62, 75).addCuboid(-8, -42.5F, 0, 16, 2, 48, 0, false);

		vent_2_r1 = createModelPart();
		vent_2_r1.setPivot(-8, -42.5F, 0);
		roof_end_exterior.addChild(vent_2_r1);
		setRotationAngle(vent_2_r1, 0, 0, -0.3491F);
		vent_2_r1.setTextureUVOffset(88, 1).addCuboid(-9, 0, 0, 9, 2, 48, 0, false);

		vent_1_r1 = createModelPart();
		vent_1_r1.setPivot(8, -42.5F, 0);
		roof_end_exterior.addChild(vent_1_r1);
		setRotationAngle(vent_1_r1, 0, 0, 0.3491F);
		vent_1_r1.setTextureUVOffset(88, 1).addCuboid(0, 0, 0, 9, 2, 48, 0, true);

		outer_roof_1 = createModelPart();
		outer_roof_1.setPivot(0, 0, 0);
		roof_end_exterior.addChild(outer_roof_1);


		outer_roof_6_r2 = createModelPart();
		outer_roof_6_r2.setPivot(-2.3377F, -41.071F, 0);
		outer_roof_1.addChild(outer_roof_6_r2);
		setRotationAngle(outer_roof_6_r2, 0, 0, 1.5708F);
		outer_roof_6_r2.setTextureUVOffset(174, 137).addCuboid(-0.5F, -3, -12, 1, 6, 20, 0, false);

		outer_roof_5_r2 = createModelPart();
		outer_roof_5_r2.setPivot(-9.6825F, -40.2972F, 0);
		outer_roof_1.addChild(outer_roof_5_r2);
		setRotationAngle(outer_roof_5_r2, 0, 0, 1.3963F);
		outer_roof_5_r2.setTextureUVOffset(154, 14).addCuboid(-0.5F, -4.5F, -12, 1, 9, 20, 0, false);

		outer_roof_4_r2 = createModelPart();
		outer_roof_4_r2.setPivot(-15.25F, -38.8252F, 0);
		outer_roof_1.addChild(outer_roof_4_r2);
		setRotationAngle(outer_roof_4_r2, 0, 0, 1.0472F);
		outer_roof_4_r2.setTextureUVOffset(106, 195).addCuboid(-0.5F, -1.5F, -12, 1, 3, 20, 0, false);

		outer_roof_3_r2 = createModelPart();
		outer_roof_3_r2.setPivot(-16.866F, -37.3922F, 0);
		outer_roof_1.addChild(outer_roof_3_r2);
		setRotationAngle(outer_roof_3_r2, 0, 0, 0.5236F);
		outer_roof_3_r2.setTextureUVOffset(162, 196).addCuboid(-0.5F, -1, -12, 1, 2, 20, 0, false);

		outer_roof_2 = createModelPart();
		outer_roof_2.setPivot(0, 0, 0);
		roof_end_exterior.addChild(outer_roof_2);


		outer_roof_5_r3 = createModelPart();
		outer_roof_5_r3.setPivot(9.6825F, -40.2972F, 0);
		outer_roof_2.addChild(outer_roof_5_r3);
		setRotationAngle(outer_roof_5_r3, 0, 0, -1.3963F);
		outer_roof_5_r3.setTextureUVOffset(154, 14).addCuboid(-0.5F, -4.5F, -12, 1, 9, 20, 0, true);

		outer_roof_4_r3 = createModelPart();
		outer_roof_4_r3.setPivot(15.25F, -38.8252F, 0);
		outer_roof_2.addChild(outer_roof_4_r3);
		setRotationAngle(outer_roof_4_r3, 0, 0, -1.0472F);
		outer_roof_4_r3.setTextureUVOffset(106, 195).addCuboid(-0.5F, -1.5F, -12, 1, 3, 20, 0, true);

		outer_roof_3_r3 = createModelPart();
		outer_roof_3_r3.setPivot(16.866F, -37.3922F, 0);
		outer_roof_2.addChild(outer_roof_3_r3);
		setRotationAngle(outer_roof_3_r3, 0, 0, -0.5236F);
		outer_roof_3_r3.setTextureUVOffset(162, 196).addCuboid(-0.5F, -1, -12, 1, 2, 20, 0, true);

		outer_roof_6_r3 = createModelPart();
		outer_roof_6_r3.setPivot(2.3377F, -41.071F, 0);
		outer_roof_2.addChild(outer_roof_6_r3);
		setRotationAngle(outer_roof_6_r3, 0, 0, -1.5708F);
		outer_roof_6_r3.setTextureUVOffset(174, 137).addCuboid(-0.5F, -3, -12, 1, 6, 20, 0, true);

		roof_light_tcl = createModelPart();
		roof_light_tcl.setPivot(0, 24, 0);


		roof_light_r1 = createModelPart();
		roof_light_r1.setPivot(-7, -33.5F, 0);
		roof_light_tcl.addChild(roof_light_r1);
		setRotationAngle(roof_light_r1, 0, 0, 0.1745F);
		roof_light_r1.setTextureUVOffset(154, 0).addCuboid(-3, -1, -24, 3, 1, 48, 0, false);

		roof_light_door_ael = createModelPart();
		roof_light_door_ael.setPivot(0, 24, 0);
		roof_light_door_ael.setTextureUVOffset(126, 78).addCuboid(-10, -33.7F, -8, 10, 0, 16, 0, false);

		roof_light_window_ael = createModelPart();
		roof_light_window_ael.setPivot(0, 24, 0);


		inner_roof_3_r7 = createModelPart();
		inner_roof_3_r7.setPivot(-15, -32, 0);
		roof_light_window_ael.addChild(inner_roof_3_r7);
		setRotationAngle(inner_roof_3_r7, 0, 0, -0.1047F);
		inner_roof_3_r7.setTextureUVOffset(176, 22).addCuboid(2.5F, -1.1F, -13, 2, 1, 26, 0, false);

		roof_end_light = createModelPart();
		roof_end_light.setPivot(0, 24, 0);


		roof_light_2_r1 = createModelPart();
		roof_light_2_r1.setPivot(7, -33.5F, 0);
		roof_end_light.addChild(roof_light_2_r1);
		setRotationAngle(roof_light_2_r1, 0, 0, -0.1745F);
		roof_light_2_r1.setTextureUVOffset(166, 12).addCuboid(0, -1, 4, 3, 1, 36, 0, false);

		roof_light_1_r1 = createModelPart();
		roof_light_1_r1.setPivot(-7, -33.5F, 0);
		roof_end_light.addChild(roof_light_1_r1);
		setRotationAngle(roof_light_1_r1, 0, 0, 0.1745F);
		roof_light_1_r1.setTextureUVOffset(166, 12).addCuboid(-3, -1, 4, 3, 1, 36, 0, false);

		head_tcl = createModelPart();
		head_tcl.setPivot(0, 24, 0);
		head_tcl.setTextureUVOffset(180, 168).addCuboid(-20, 0, 4, 40, 1, 4, 0, false);
		head_tcl.setTextureUVOffset(90, 75).addCuboid(18, -14, 4, 3, 14, 6, 0, true);
		head_tcl.setTextureUVOffset(90, 75).addCuboid(-21, -14, 4, 3, 14, 6, 0, false);
		head_tcl.setTextureUVOffset(208, 0).addCuboid(-18, -41, 4, 36, 41, 0, 0, false);

		upper_wall_2_r6 = createModelPart();
		upper_wall_2_r6.setPivot(-21, -14, 0);
		head_tcl.addChild(upper_wall_2_r6);
		setRotationAngle(upper_wall_2_r6, 0, 0, 0.1396F);
		upper_wall_2_r6.setTextureUVOffset(0, 72).addCuboid(0, -19, 4, 3, 19, 6, 0, false);

		upper_wall_1_r5 = createModelPart();
		upper_wall_1_r5.setPivot(21, -14, 0);
		head_tcl.addChild(upper_wall_1_r5);
		setRotationAngle(upper_wall_1_r5, 0, 0, -0.1396F);
		upper_wall_1_r5.setTextureUVOffset(0, 72).addCuboid(-3, -19, 4, 3, 19, 6, 0, true);

		head_ael = createModelPart();
		head_ael.setPivot(0, 24, 0);
		head_ael.setTextureUVOffset(208, 0).addCuboid(-18, -41, -2, 36, 41, 0, 0, false);

		head_exterior = createModelPart();
		head_exterior.setPivot(0, 24, 0);
		head_exterior.setTextureUVOffset(158, 306).addCuboid(-20, 0, -24, 40, 1, 28, 0, false);
		head_exterior.setTextureUVOffset(232, 110).addCuboid(18, -14, -24, 3, 14, 34, 0, true);
		head_exterior.setTextureUVOffset(232, 110).addCuboid(-21, -14, -24, 3, 14, 34, 0, false);
		head_exterior.setTextureUVOffset(200, 69).addCuboid(-18, -41, -3, 36, 41, 0, 0, false);

		upper_wall_2_r7 = createModelPart();
		upper_wall_2_r7.setPivot(-21, -14, 0);
		head_exterior.addChild(upper_wall_2_r7);
		setRotationAngle(upper_wall_2_r7, 0, 0, 0.1396F);
		upper_wall_2_r7.setTextureUVOffset(250, 237).addCuboid(0, -23, -24, 3, 23, 34, 0, false);

		upper_wall_1_r6 = createModelPart();
		upper_wall_1_r6.setPivot(21, -14, 0);
		head_exterior.addChild(upper_wall_1_r6);
		setRotationAngle(upper_wall_1_r6, 0, 0, -0.1396F);
		upper_wall_1_r6.setTextureUVOffset(250, 237).addCuboid(-3, -23, -24, 3, 23, 34, 0, true);

		floor_2_r5 = createModelPart();
		floor_2_r5.setPivot(-21, 0, 0);
		head_exterior.addChild(floor_2_r5);
		setRotationAngle(floor_2_r5, 0, 0, -0.1745F);
		floor_2_r5.setTextureUVOffset(90, 252).addCuboid(0, 0, -24, 1, 8, 32, 0, false);

		floor_1_r4 = createModelPart();
		floor_1_r4.setPivot(21, 0, 0);
		head_exterior.addChild(floor_1_r4);
		setRotationAngle(floor_1_r4, 0, 0, 0.1745F);
		floor_1_r4.setTextureUVOffset(90, 252).addCuboid(-1, 0, -24, 1, 8, 32, 0, true);

		front = createModelPart();
		front.setPivot(0, 0, 0);
		head_exterior.addChild(front);
		front.setTextureUVOffset(142, 76).addCuboid(-9, 0.9884F, -46.7528F, 18, 2, 0, 0, false);
		front.setTextureUVOffset(172, 51).addCuboid(-21, 0, -24, 42, 8, 0, 0, false);

		front_bottom_5_r1 = createModelPart();
		front_bottom_5_r1.setPivot(0, 4.4033F, -45.3383F);
		front.addChild(front_bottom_5_r1);
		setRotationAngle(front_bottom_5_r1, 1.4137F, 0, 0);
		front_bottom_5_r1.setTextureUVOffset(256, 42).addCuboid(-20, 0, -0.001F, 40, 22, 0, 0, false);

		front_bottom_4_r1 = createModelPart();
		front_bottom_4_r1.setPivot(0, 3.6962F, -46.0454F);
		front.addChild(front_bottom_4_r1);
		setRotationAngle(front_bottom_4_r1, 0.7854F, 0, 0);
		front_bottom_4_r1.setTextureUVOffset(0, 98).addCuboid(-12, -1, 0, 24, 2, 0, 0, false);

		front_bottom_2_r1 = createModelPart();
		front_bottom_2_r1.setPivot(0, 0.1009F, -46.292F);
		front.addChild(front_bottom_2_r1);
		setRotationAngle(front_bottom_2_r1, -0.48F, 0, 0);
		front_bottom_2_r1.setTextureUVOffset(58, 120).addCuboid(-11, -1, 0, 22, 2, 0, 0, false);

		front_bottom_1_r1 = createModelPart();
		front_bottom_1_r1.setPivot(0, -4, -42);
		front.addChild(front_bottom_1_r1);
		setRotationAngle(front_bottom_1_r1, -0.8727F, 0, 0);
		front_bottom_1_r1.setTextureUVOffset(154, 43).addCuboid(-12, 0, 0, 24, 5, 0, 0, false);

		front_panel_3_r1 = createModelPart();
		front_panel_3_r1.setPivot(0, -32.2269F, -28.3321F);
		front.addChild(front_panel_3_r1);
		setRotationAngle(front_panel_3_r1, -0.6283F, 0, 0);
		front_panel_3_r1.setTextureUVOffset(211, 250).addCuboid(-12, -6.5F, 0, 24, 12, 0, 0, false);

		front_panel_2_r1 = createModelPart();
		front_panel_2_r1.setPivot(0, -20.5874F, -35.0728F);
		front.addChild(front_panel_2_r1);
		setRotationAngle(front_panel_2_r1, -0.4538F, 0, 0);
		front_panel_2_r1.setTextureUVOffset(90, 232).addCuboid(-12, -8, 0, 24, 16, 0, 0, false);

		front_panel_1_r1 = createModelPart();
		front_panel_1_r1.setPivot(0, -4, -42);
		front.addChild(front_panel_1_r1);
		setRotationAngle(front_panel_1_r1, -0.3491F, 0, 0);
		front_panel_1_r1.setTextureUVOffset(200, 110).addCuboid(-12, -10, 0, 24, 10, 0, 0, false);

		side_1 = createModelPart();
		side_1.setPivot(0, 0, 0);
		front.addChild(side_1);


		front_side_bottom_2_r1 = createModelPart();
		front_side_bottom_2_r1.setPivot(18.3378F, 3.5923F, -35.3251F);
		side_1.addChild(front_side_bottom_2_r1);
		setRotationAngle(front_side_bottom_2_r1, 0, 0.1745F, 0.1745F);
		front_side_bottom_2_r1.setTextureUVOffset(112, 157).addCuboid(0, -4, -6.5F, 0, 8, 18, 0, true);

		front_middle_top_r1 = createModelPart();
		front_middle_top_r1.setPivot(-0.6613F, -41.5702F, -11.9997F);
		side_1.addChild(front_middle_top_r1);
		setRotationAngle(front_middle_top_r1, 0, 0.3491F, -1.5708F);
		front_middle_top_r1.setTextureUVOffset(232, 118).addCuboid(0, 0, -14, 0, 6, 14, 0, true);

		outer_roof_8_r1 = createModelPart();
		outer_roof_8_r1.setPivot(16.824F, -34.5499F, -16.9825F);
		side_1.addChild(outer_roof_8_r1);
		setRotationAngle(outer_roof_8_r1, 0, 0.1745F, -1.0472F);
		outer_roof_8_r1.setTextureUVOffset(70, 66).addCuboid(2.5F, -5, -3.5F, 0, 6, 9, 0, true);

		outer_roof_7_r1 = createModelPart();
		outer_roof_7_r1.setPivot(17.2991F, -37.6418F, 0);
		side_1.addChild(outer_roof_7_r1);
		setRotationAngle(outer_roof_7_r1, 0, 0, -0.5236F);
		outer_roof_7_r1.setTextureUVOffset(22, 25).addCuboid(0, -1, -19, 0, 2, 7, 0, true);

		outer_roof_6_r4 = createModelPart();
		outer_roof_6_r4.setPivot(11.7869F, -37.8295F, -19.0477F);
		side_1.addChild(outer_roof_6_r4);
		setRotationAngle(outer_roof_6_r4, 0, 0.3491F, -1.3963F);
		outer_roof_6_r4.setTextureUVOffset(72, 180).addCuboid(0, -7, -7.5F, 0, 14, 15, 0, true);

		front_panel_8_r1 = createModelPart();
		front_panel_8_r1.setPivot(8.9994F, 2.9884F, -46.7521F);
		side_1.addChild(front_panel_8_r1);
		setRotationAngle(front_panel_8_r1, 0, -0.5672F, 0);
		front_panel_8_r1.setTextureUVOffset(212, 217).addCuboid(0, -5.9884F, -0.001F, 11, 8, 0, 0, true);

		front_panel_7_r1 = createModelPart();
		front_panel_7_r1.setPivot(12, -4, -42);
		side_1.addChild(front_panel_7_r1);
		setRotationAngle(front_panel_7_r1, -0.7418F, -0.3491F, 0);
		front_panel_7_r1.setTextureUVOffset(208, 42).addCuboid(-4, 0, 0, 11, 5, 0, 0, true);

		front_panel_6_r1 = createModelPart();
		front_panel_6_r1.setPivot(12, -4, -42);
		side_1.addChild(front_panel_6_r1);
		setRotationAngle(front_panel_6_r1, -0.3491F, -0.3491F, 0);
		front_panel_6_r1.setTextureUVOffset(162, 196).addCuboid(0, -12, 0, 9, 12, 0, 0, true);

		front_panel_5_r1 = createModelPart();
		front_panel_5_r1.setPivot(12, -13.397F, -38.5798F);
		side_1.addChild(front_panel_5_r1);
		setRotationAngle(front_panel_5_r1, -0.4538F, -0.3491F, 0);
		front_panel_5_r1.setTextureUVOffset(112, 139).addCuboid(0, -18, -0.001F, 10, 18, 0, 0, true);

		front_panel_4_r1 = createModelPart();
		front_panel_4_r1.setPivot(15.1229F, -32.2269F, -26.988F);
		side_1.addChild(front_panel_4_r1);
		setRotationAngle(front_panel_4_r1, -0.6283F, -0.3491F, 0);
		front_panel_4_r1.setTextureUVOffset(0, 228).addCuboid(-4.5F, -4.5F, 0, 9, 8, 0, 0, true);

		front_side_upper_2_r1 = createModelPart();
		front_side_upper_2_r1.setPivot(21, -14, -24);
		side_1.addChild(front_side_upper_2_r1);
		setRotationAngle(front_side_upper_2_r1, 0, 0.1745F, -0.1396F);
		front_side_upper_2_r1.setTextureUVOffset(0, 37).addCuboid(0, -21, -12, 0, 21, 12, 0, true);

		front_side_lower_2_r1 = createModelPart();
		front_side_lower_2_r1.setPivot(21, 0, -24);
		side_1.addChild(front_side_lower_2_r1);
		setRotationAngle(front_side_lower_2_r1, 0, 0.1745F, 0);
		front_side_lower_2_r1.setTextureUVOffset(0, 102).addCuboid(0, -14, -18, 0, 14, 18, 0, true);

		side_2 = createModelPart();
		side_2.setPivot(21, 0, -9);
		front.addChild(side_2);


		front_side_bottom_2_r2 = createModelPart();
		front_side_bottom_2_r2.setPivot(-39.3378F, 3.5923F, -26.3251F);
		side_2.addChild(front_side_bottom_2_r2);
		setRotationAngle(front_side_bottom_2_r2, 0, -0.1745F, -0.1745F);
		front_side_bottom_2_r2.setTextureUVOffset(112, 157).addCuboid(0, -4, -6.5F, 0, 8, 18, 0, false);

		front_middle_top_r2 = createModelPart();
		front_middle_top_r2.setPivot(-20.3387F, -41.5702F, -2.9997F);
		side_2.addChild(front_middle_top_r2);
		setRotationAngle(front_middle_top_r2, 0, -0.3491F, 1.5708F);
		front_middle_top_r2.setTextureUVOffset(232, 118).addCuboid(0, 0, -14, 0, 6, 14, 0, false);

		outer_roof_8_r2 = createModelPart();
		outer_roof_8_r2.setPivot(-37.824F, -34.5499F, -7.9825F);
		side_2.addChild(outer_roof_8_r2);
		setRotationAngle(outer_roof_8_r2, 0, -0.1745F, 1.0472F);
		outer_roof_8_r2.setTextureUVOffset(70, 66).addCuboid(-2.5F, -5, -3.5F, 0, 6, 9, 0, false);

		outer_roof_7_r2 = createModelPart();
		outer_roof_7_r2.setPivot(-38.2991F, -37.6418F, 9);
		side_2.addChild(outer_roof_7_r2);
		setRotationAngle(outer_roof_7_r2, 0, 0, 0.5236F);
		outer_roof_7_r2.setTextureUVOffset(22, 25).addCuboid(0, -1, -19, 0, 2, 7, 0, false);

		outer_roof_6_r5 = createModelPart();
		outer_roof_6_r5.setPivot(-32.7869F, -37.8295F, -10.0477F);
		side_2.addChild(outer_roof_6_r5);
		setRotationAngle(outer_roof_6_r5, 0, -0.3491F, 1.3963F);
		outer_roof_6_r5.setTextureUVOffset(72, 180).addCuboid(0, -7, -7.5F, 0, 14, 15, 0, false);

		front_panel_8_r2 = createModelPart();
		front_panel_8_r2.setPivot(-29.9994F, 2.9884F, -37.7521F);
		side_2.addChild(front_panel_8_r2);
		setRotationAngle(front_panel_8_r2, 0, 0.5672F, 0);
		front_panel_8_r2.setTextureUVOffset(212, 217).addCuboid(-11, -5.9884F, -0.001F, 11, 8, 0, 0, false);

		front_panel_7_r2 = createModelPart();
		front_panel_7_r2.setPivot(-33, -4, -33);
		side_2.addChild(front_panel_7_r2);
		setRotationAngle(front_panel_7_r2, -0.7418F, 0.3491F, 0);
		front_panel_7_r2.setTextureUVOffset(208, 42).addCuboid(-7, 0, 0, 11, 5, 0, 0, false);

		front_panel_6_r2 = createModelPart();
		front_panel_6_r2.setPivot(-33, -4, -33);
		side_2.addChild(front_panel_6_r2);
		setRotationAngle(front_panel_6_r2, -0.3491F, 0.3491F, 0);
		front_panel_6_r2.setTextureUVOffset(162, 196).addCuboid(-9, -12, 0, 9, 12, 0, 0, false);

		front_panel_5_r2 = createModelPart();
		front_panel_5_r2.setPivot(-33, -13.397F, -29.5798F);
		side_2.addChild(front_panel_5_r2);
		setRotationAngle(front_panel_5_r2, -0.4538F, 0.3491F, 0);
		front_panel_5_r2.setTextureUVOffset(112, 139).addCuboid(-10, -18, -0.001F, 10, 18, 0, 0, false);

		front_panel_4_r2 = createModelPart();
		front_panel_4_r2.setPivot(-36.1229F, -32.2269F, -17.988F);
		side_2.addChild(front_panel_4_r2);
		setRotationAngle(front_panel_4_r2, -0.6283F, 0.3491F, 0);
		front_panel_4_r2.setTextureUVOffset(0, 228).addCuboid(-4.5F, -4.5F, 0, 9, 8, 0, 0, false);

		front_side_upper_2_r2 = createModelPart();
		front_side_upper_2_r2.setPivot(-42, -14, -15);
		side_2.addChild(front_side_upper_2_r2);
		setRotationAngle(front_side_upper_2_r2, 0, -0.1745F, 0.1396F);
		front_side_upper_2_r2.setTextureUVOffset(0, 37).addCuboid(0, -21, -12, 0, 21, 12, 0, false);

		front_side_lower_2_r2 = createModelPart();
		front_side_lower_2_r2.setPivot(-42, 0, -15);
		side_2.addChild(front_side_lower_2_r2);
		setRotationAngle(front_side_lower_2_r2, 0, -0.1745F, 0);
		front_side_lower_2_r2.setTextureUVOffset(0, 102).addCuboid(0, -14, -18, 0, 14, 18, 0, false);

		headlights = createModelPart();
		headlights.setPivot(0, 24, 0);


		headlight_2b_r1 = createModelPart();
		headlight_2b_r1.setPivot(0, -4, -42.1F);
		headlights.addChild(headlight_2b_r1);
		setRotationAngle(headlight_2b_r1, -0.3491F, 0, 0);
		headlight_2b_r1.setTextureUVOffset(20, 4).addCuboid(8, -4, 0, 4, 4, 0, 0, true);
		headlight_2b_r1.setTextureUVOffset(20, 4).addCuboid(-12, -4, 0, 4, 4, 0, 0, false);

		headlight_2a_r1 = createModelPart();
		headlight_2a_r1.setPivot(12, -4, -42.1F);
		headlights.addChild(headlight_2a_r1);
		setRotationAngle(headlight_2a_r1, -0.3491F, -0.3491F, 0);
		headlight_2a_r1.setTextureUVOffset(20, 0).addCuboid(0, -4, 0, 6, 4, 0, 0, true);

		headlight_1a_r1 = createModelPart();
		headlight_1a_r1.setPivot(-12, -4, -42.1F);
		headlights.addChild(headlight_1a_r1);
		setRotationAngle(headlight_1a_r1, -0.3491F, 0.3491F, 0);
		headlight_1a_r1.setTextureUVOffset(20, 0).addCuboid(-6, -4, 0, 6, 4, 0, 0, false);

		tail_lights = createModelPart();
		tail_lights.setPivot(0, 24, 0);


		tail_light_2a_r1 = createModelPart();
		tail_light_2a_r1.setPivot(12, -4, -42);
		tail_lights.addChild(tail_light_2a_r1);
		setRotationAngle(tail_light_2a_r1, -0.3491F, -0.3491F, 0);
		tail_light_2a_r1.setTextureUVOffset(20, 8).addCuboid(0, -4, -0.1F, 6, 4, 0, 0, true);

		tail_light_1a_r1 = createModelPart();
		tail_light_1a_r1.setPivot(-12, -4, -42);
		tail_lights.addChild(tail_light_1a_r1);
		setRotationAngle(tail_light_1a_r1, -0.3491F, 0.3491F, 0);
		tail_light_1a_r1.setTextureUVOffset(20, 8).addCuboid(-6, -4, -0.1F, 6, 4, 0, 0, false);

		seat = createModelPart();
		seat.setPivot(0, 24, 0);
		seat.setTextureUVOffset(16, 34).addCuboid(-3, -5, -3, 6, 1, 6, 0, false);
		seat.setTextureUVOffset(24, 52).addCuboid(-1.5F, -16.4F, 4.5F, 3, 2, 1, 0, false);

		top_right_r1 = createModelPart();
		top_right_r1.setPivot(-1.5F, -16.4F, 4.5F);
		seat.addChild(top_right_r1);
		setRotationAngle(top_right_r1, 0.0017F, 0, 0.2618F);
		top_right_r1.setTextureUVOffset(32, 52).addCuboid(0, 0, 0, 1, 2, 1, 0, false);

		top_left_r1 = createModelPart();
		top_left_r1.setPivot(1.5F, -16.4F, 4.5F);
		seat.addChild(top_left_r1);
		setRotationAngle(top_left_r1, 0.0017F, 0, -0.2618F);
		top_left_r1.setTextureUVOffset(32, 52).addCuboid(-1, 0, 0, 1, 2, 1, 0, true);

		back_right_r1 = createModelPart();
		back_right_r1.setPivot(-1.5F, -5, 2);
		seat.addChild(back_right_r1);
		setRotationAngle(back_right_r1, -0.2618F, -0.1745F, 0.0873F);
		back_right_r1.setTextureUVOffset(32, 41).addCuboid(-1.5F, -9.75F, 0, 2, 10, 1, 0, false);

		back_left_r1 = createModelPart();
		back_left_r1.setPivot(1.5F, -5, 2);
		seat.addChild(back_left_r1);
		setRotationAngle(back_left_r1, -0.2618F, 0.1745F, -0.0873F);
		back_left_r1.setTextureUVOffset(32, 41).addCuboid(-0.5F, -9.75F, 0, 2, 10, 1, 0, true);

		back_r1 = createModelPart();
		back_r1.setPivot(3, -5, 2);
		seat.addChild(back_r1);
		setRotationAngle(back_r1, -0.2618F, 0, 0);
		back_r1.setTextureUVOffset(24, 41).addCuboid(-4.5F, -10, 0, 3, 10, 1, 0, true);

		door_light_on = createModelPart();
		door_light_on.setPivot(0, 24, 0);


		light_r1 = createModelPart();
		light_r1.setPivot(-21, -14, 0);
		door_light_on.addChild(light_r1);
		setRotationAngle(light_r1, 0, 0, 0.1396F);
		light_r1.setTextureUVOffset(20, 15).addCuboid(0, -21.5F, -0.5F, 0, 0, 1, 0.4F, false);

		door_light_off = createModelPart();
		door_light_off.setPivot(0, 24, 0);


		light_r2 = createModelPart();
		light_r2.setPivot(-21, -14, 0);
		door_light_off.addChild(light_r2);
		setRotationAngle(light_r2, 0, 0, 0.1396F);
		light_r2.setTextureUVOffset(23, 15).addCuboid(0, -21.5F, -0.5F, 0, 0, 1, 0.4F, false);

		buildModel();
	}

	private static final int DOOR_MAX_TCL = 14;
	private static final int DOOR_MAX_AEL = 11;
	private static final ModelDoorOverlay MODEL_DOOR_OVERLAY = new ModelDoorOverlay(DOOR_MAX_TCL, 8, "door_overlay_a_train_tcl_left.png", "door_overlay_a_train_tcl_right.png");

	@Override
	public ModelATrain createNew(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		return new ModelATrain(isAel, doorAnimationType, renderDoorOverlay);
	}

	@Override
	protected void renderWindowPositions(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
		switch (renderStage) {
			case LIGHTS:
				renderMirror(isAel ? roof_light_window_ael : roof_light_tcl, graphicsHolder, light, position);
				break;
			case INTERIOR:
				renderMirror(isAel ? window_ael : window_tcl, graphicsHolder, light, position);
				if (renderDetails) {
					if (!isAel) {
						renderMirror(window_tcl_handrails, graphicsHolder, light, position);
						renderMirror(side_panel_tcl, graphicsHolder, light, position - 22F);
						renderMirror(side_panel_tcl, graphicsHolder, light, position + 22F);
					}
					renderMirror(isAel ? roof_window_ael : roof_window_tcl, graphicsHolder, light, position);
				}
				break;
			case INTERIOR_TRANSLUCENT:
				if (!isAel) {
					renderMirror(side_panel_tcl_translucent, graphicsHolder, light, position - 22F);
					renderMirror(side_panel_tcl_translucent, graphicsHolder, light, position + 22F);
				}
				break;
			case EXTERIOR:
				if (isAel) {
					final boolean isHeadWindows = isEnd1Head && (isIndex(0, position, getWindowPositions()) || isIndex(1, position, getWindowPositions()) || isIndex(2, position, getWindowPositions()));
					final boolean isEndWindows = isEnd2Head && (isIndex(-1, position, getWindowPositions()) || isIndex(-2, position, getWindowPositions()) || isIndex(-3, position, getWindowPositions()));
					if (!isHeadWindows && !isEndWindows) {
						renderMirror(window_exterior_ael, graphicsHolder, light, position);
					}
				} else {
					if (isIndex(0, position, getWindowPositions()) && isEnd1Head) {
						renderOnceFlipped(window_exterior_end_tcl, graphicsHolder, light, position);
					} else if (isIndex(-1, position, getWindowPositions()) && isEnd2Head) {
						renderOnce(window_exterior_end_tcl, graphicsHolder, light, position);
					} else {
						renderMirror(window_exterior_tcl, graphicsHolder, light, position);
					}
				}
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
				if (isAel) {
					renderMirror(roof_light_door_ael, graphicsHolder, light, position);
				} else if (notLastDoor) {
					renderMirror(roof_light_tcl, graphicsHolder, light, position);
				}
				if (middleDoor && doorOpen && renderDetails) {
					renderMirror(door_light_on, graphicsHolder, light, position - (isAel ? 80 : 40));
				}
				break;
			case INTERIOR:
				if (isAel) {
					door_left_ael.setOffset(doorRightX, 0, doorRightZ);
					door_right_ael.setOffset(doorRightX, 0, -doorRightZ);
					renderOnce(door_ael, graphicsHolder, light, position);
					door_left_ael.setOffset(doorLeftX, 0, doorLeftZ);
					door_right_ael.setOffset(doorLeftX, 0, -doorLeftZ);
					renderOnceFlipped(door_ael, graphicsHolder, light, position);

					if (renderDetails) {
						renderMirror(door_ael_handrail, graphicsHolder, light, position);
						renderMirror(roof_door_ael, graphicsHolder, light, position);
						renderMirror(luggage_rack, graphicsHolder, light, position - 20);
						renderMirror(luggage_rack, graphicsHolder, light, position + 20);
						renderMirror(side_panel_ael, graphicsHolder, light, position - 28.1F);
						renderMirror(side_panel_ael, graphicsHolder, light, position - 11.9F);
						renderMirror(side_panel_ael, graphicsHolder, light, position + 11.9F);
						renderMirror(side_panel_ael, graphicsHolder, light, position + 28.1F);

						for (int z = position + 40; z <= position + 74; z += 17) {
							renderOnce(seat, graphicsHolder, light, 15, z);
							renderOnce(seat, graphicsHolder, light, 8.5F, z);
							renderOnce(seat, graphicsHolder, light, -8.5F, z);
							renderOnce(seat, graphicsHolder, light, -15, z);
						}
						for (int z = position - 74; z <= position - 40; z += 17) {
							renderOnceFlipped(seat, graphicsHolder, light, 15, z);
							renderOnceFlipped(seat, graphicsHolder, light, 8.5F, z);
							renderOnceFlipped(seat, graphicsHolder, light, -8.5F, z);
							renderOnceFlipped(seat, graphicsHolder, light, -15, z);
						}
					}
				} else {
					door_left_tcl.setOffset(doorRightX, 0, doorRightZ);
					door_right_tcl.setOffset(doorRightX, 0, -doorRightZ);
					renderOnce(door_tcl, graphicsHolder, light, position);
					door_left_tcl.setOffset(doorLeftX, 0, doorLeftZ);
					door_right_tcl.setOffset(doorLeftX, 0, -doorLeftZ);
					renderOnceFlipped(door_tcl, graphicsHolder, light, position);

					if (renderDetails) {
						renderOnce(door_tcl_handrail, graphicsHolder, light, position);
						if (notLastDoor) {
							renderMirror(roof_door_tcl, graphicsHolder, light, position);
						}
					}
				}
				break;
			case INTERIOR_TRANSLUCENT:
				if (isAel) {
					renderMirror(side_panel_ael_translucent, graphicsHolder, light, position - 28.1F);
					renderMirror(side_panel_ael_translucent, graphicsHolder, light, position - 11.9F);
					renderMirror(side_panel_ael_translucent, graphicsHolder, light, position + 11.9F);
					renderMirror(side_panel_ael_translucent, graphicsHolder, light, position + 28.1F);
				}
				break;
			case EXTERIOR:
				if (isAel) {
					door_left_exterior_ael.setOffset(doorRightX, 0, doorRightZ);
					door_right_exterior_ael.setOffset(doorRightX, 0, -doorRightZ);
					renderOnce(door_exterior_ael, graphicsHolder, light, position);
					door_left_exterior_ael.setOffset(doorLeftX, 0, doorLeftZ);
					door_right_exterior_ael.setOffset(doorLeftX, 0, -doorLeftZ);
					renderOnceFlipped(door_exterior_ael, graphicsHolder, light, position);
					renderMirror(roof_exterior, graphicsHolder, light, position - 2);
					renderMirror(roof_exterior, graphicsHolder, light, position + 2);
				} else {
					final boolean door1End = isIndex(0, position, getDoorPositions()) && isEnd1Head;
					final boolean door2End = isIndex(-1, position, getDoorPositions()) && isEnd2Head;

					if (door1End || door2End) {
						door_left_exterior_end.setOffset(doorRightX, 0, doorRightZ);
						door_right_exterior_end.setOffset(doorRightX, 0, -doorRightZ);
						renderOnce(door_exterior_end, graphicsHolder, light, position);
					} else {
						door_left_exterior_tcl.setOffset(doorRightX, 0, doorRightZ);
						door_right_exterior_tcl.setOffset(doorRightX, 0, -doorRightZ);
						renderOnce(door_exterior_tcl, graphicsHolder, light, position);
					}

					if (door1End || door2End) {
						door_left_exterior_end.setOffset(doorLeftX, 0, doorLeftZ);
						door_right_exterior_end.setOffset(doorLeftX, 0, -doorLeftZ);
						renderOnceFlipped(door_exterior_end, graphicsHolder, light, position);
					} else {
						door_left_exterior_tcl.setOffset(doorLeftX, 0, doorLeftZ);
						door_right_exterior_tcl.setOffset(doorLeftX, 0, -doorLeftZ);
						renderOnceFlipped(door_exterior_tcl, graphicsHolder, light, position);
					}
					renderMirror(roof_exterior, graphicsHolder, light, position);
				}
				if (middleDoor && !doorOpen && renderDetails) {
					renderMirror(door_light_off, graphicsHolder, light, position - (isAel ? 80 : 40));
				}
				break;
		}
	}

	@Override
	protected void renderHeadPosition1(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean useHeadlights) {
		switch (renderStage) {
			case LIGHTS:
				if (!isAel) {
					renderOnce(roof_end_light, graphicsHolder, light, position);
				}
				break;
			case ALWAYS_ON_LIGHTS:
				renderOnce(useHeadlights ? headlights : tail_lights, graphicsHolder, light, position);
				break;
			case INTERIOR:
				renderOnce(isAel ? head_ael : head_tcl, graphicsHolder, light, position);
				if (renderDetails) {
					if (isAel) {
						renderOnceFlipped(seat, graphicsHolder, light, 15, position + 13);
						renderOnceFlipped(seat, graphicsHolder, light, 8.5F, position + 13);
						renderOnceFlipped(seat, graphicsHolder, light, -15, position + 13);
						renderOnceFlipped(seat, graphicsHolder, light, -8.5F, position + 13);
					} else {
						renderOnce(roof_end, graphicsHolder, light, position);
					}
				}
				break;
			case EXTERIOR:
				renderOnce(head_exterior, graphicsHolder, light, position);
				renderOnce(roof_end_exterior, graphicsHolder, light, position);
				if (isAel) {
					renderOnce(window_exterior_end_ael, graphicsHolder, light, position);
					renderMirror(roof_exterior, graphicsHolder, light, position + 30);
					renderMirror(roof_exterior, graphicsHolder, light, position + 70);
				}
				break;
		}
	}

	@Override
	protected void renderHeadPosition2(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean useHeadlights) {
		switch (renderStage) {
			case LIGHTS:
				if (!isAel) {
					renderOnceFlipped(roof_end_light, graphicsHolder, light, position);
				}
				break;
			case ALWAYS_ON_LIGHTS:
				renderOnceFlipped(useHeadlights ? headlights : tail_lights, graphicsHolder, light, position);
				break;
			case INTERIOR:
				renderOnceFlipped(isAel ? head_ael : head_tcl, graphicsHolder, light, position);
				if (renderDetails) {
					if (isAel) {
						renderOnce(seat, graphicsHolder, light, 15, position - 13);
						renderOnce(seat, graphicsHolder, light, 8.5F, position - 13);
						renderOnce(seat, graphicsHolder, light, -15, position - 13);
						renderOnce(seat, graphicsHolder, light, -8.5F, position - 13);
					} else {
						renderOnceFlipped(roof_end, graphicsHolder, light, position);
					}
				}
				break;
			case EXTERIOR:
				renderOnceFlipped(head_exterior, graphicsHolder, light, position);
				renderOnceFlipped(roof_end_exterior, graphicsHolder, light, position);
				if (isAel) {
					renderOnceFlipped(window_exterior_end_ael, graphicsHolder, light, position);
					renderMirror(roof_exterior, graphicsHolder, light, position - 30);
					renderMirror(roof_exterior, graphicsHolder, light, position - 70);
				}
				break;
		}
	}

	@Override
	protected void renderEndPosition1(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
		switch (renderStage) {
			case LIGHTS:
				if (!isAel) {
					renderOnce(roof_end_light, graphicsHolder, light, position);
				}
				break;
			case INTERIOR:
				if (isAel) {
					renderOnce(end_ael, graphicsHolder, light, position);
					if (renderDetails) {
						renderOnce(end_door_ael, graphicsHolder, light, position);
						renderOnceFlipped(seat, graphicsHolder, light, 15, position + 13);
						renderOnceFlipped(seat, graphicsHolder, light, 8.5F, position + 13);
						renderOnceFlipped(seat, graphicsHolder, light, -15, position + 13);
						renderOnceFlipped(seat, graphicsHolder, light, -8.5F, position + 13);
					}
				} else {
					renderOnce(end_tcl, graphicsHolder, light, position);
					if (renderDetails) {
						renderOnce(roof_end, graphicsHolder, light, position);
					}
				}
				break;
			case EXTERIOR:
				renderOnce(isAel ? end_exterior_ael : end_exterior_tcl, graphicsHolder, light, position);
				renderOnce(roof_end_exterior, graphicsHolder, light, position);
				break;
		}
	}

	@Override
	protected void renderEndPosition2(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
		switch (renderStage) {
			case LIGHTS:
				if (!isAel) {
					renderOnceFlipped(roof_end_light, graphicsHolder, light, position);
				}
				break;
			case INTERIOR:
				if (isAel) {
					renderOnceFlipped(end_ael, graphicsHolder, light, position);
					if (renderDetails) {
						renderOnceFlipped(end_door_ael, graphicsHolder, light, position);
						renderOnce(seat, graphicsHolder, light, 15, position - 13);
						renderOnce(seat, graphicsHolder, light, 8.5F, position - 13);
						renderOnce(seat, graphicsHolder, light, -15, position - 13);
						renderOnce(seat, graphicsHolder, light, -8.5F, position - 13);
					}
				} else {
					renderOnceFlipped(end_tcl, graphicsHolder, light, position);
					if (renderDetails) {
						renderOnceFlipped(roof_end, graphicsHolder, light, position);
					}
				}
				break;
			case EXTERIOR:
				renderOnceFlipped(isAel ? end_exterior_ael : end_exterior_tcl, graphicsHolder, light, position);
				renderOnceFlipped(roof_end_exterior, graphicsHolder, light, position);
				break;
		}
	}

	@Override
	protected ModelDoorOverlay getModelDoorOverlay() {
		return isAel ? null : MODEL_DOOR_OVERLAY;
	}

	@Override
	protected ModelDoorOverlayTopBase getModelDoorOverlayTop() {
		return null;
	}

	@Override
	protected int[] getWindowPositions() {
		return isAel ? new int[]{-173, -147, -121, -39, -13, 13, 39, 121, 147, 173} : new int[]{-120, -40, 40, 120};
	}

	@Override
	protected int[] getDoorPositions() {
		return isAel ? new int[]{-80, 80} : new int[]{-160, -80, 0, 80, 160};
	}

	@Override
	protected int[] getEndPositions() {
		return new int[]{-184, 184};
	}

	@Override
	protected int getDoorMax() {
		return isAel ? DOOR_MAX_AEL : DOOR_MAX_TCL;
	}

	@Override
	protected float getDoorDuration() {
		return 0.5F * (isAel ? (float) DOOR_MAX_AEL / DOOR_MAX_TCL : 1);
	}
}