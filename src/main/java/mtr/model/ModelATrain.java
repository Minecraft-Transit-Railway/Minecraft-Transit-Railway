package mtr.model;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class ModelATrain extends ModelTrainBase {

	private final ModelPart window_tcl;
	private final ModelPart upper_wall_r1;
	private final ModelPart window_tcl_handrails;
	private final ModelPart handrail_8_r1;
	private final ModelPart handrail_3_r1;
	private final ModelPart top_handrail_3_r1;
	private final ModelPart seat_back_r1;
	private final ModelPart bench;
	private final ModelPart window_ael;
	private final ModelPart upper_wall_r2;
	private final ModelPart window_exterior_tcl;
	private final ModelPart upper_wall_r3;
	private final ModelPart floor_r1;
	private final ModelPart window_exterior_ael;
	private final ModelPart upper_wall_r4;
	private final ModelPart floor_r2;
	private final ModelPart window_exterior_end_tcl;
	private final ModelPart upper_wall_2_r1;
	private final ModelPart floor_2_r1;
	private final ModelPart upper_wall_1_r1;
	private final ModelPart floor_1_r1;
	private final ModelPart window_exterior_end_ael;
	private final ModelPart upper_wall_6_r1;
	private final ModelPart floor_6_r1;
	private final ModelPart upper_wall_5_r1;
	private final ModelPart floor_5_r1;
	private final ModelPart side_panel_tcl;
	private final ModelPart side_panel_tcl_translucent;
	private final ModelPart side_panel_ael;
	private final ModelPart side_panel_ael_translucent;
	private final ModelPart roof_window_tcl;
	private final ModelPart inner_roof_4_r1;
	private final ModelPart inner_roof_3_r1;
	private final ModelPart inner_roof_2_r1;
	private final ModelPart roof_window_ael;
	private final ModelPart inner_roof_5_r1;
	private final ModelPart inner_roof_4_r2;
	private final ModelPart inner_roof_3_r2;
	private final ModelPart roof_door_tcl;
	private final ModelPart inner_roof_4_r3;
	private final ModelPart inner_roof_3_r3;
	private final ModelPart inner_roof_2_r2;
	private final ModelPart handrail_2_r1;
	private final ModelPart roof_door_ael;
	private final ModelPart display_main_r1;
	private final ModelPart display_6_r1;
	private final ModelPart display_5_r1;
	private final ModelPart display_4_r1;
	private final ModelPart display_3_r1;
	private final ModelPart display_2_r1;
	private final ModelPart display_1_r1;
	private final ModelPart inner_roof_4_r4;
	private final ModelPart inner_roof_3_r4;
	private final ModelPart inner_roof_2_r3;
	private final ModelPart roof_exterior;
	private final ModelPart outer_roof_6_r1;
	private final ModelPart outer_roof_5_r1;
	private final ModelPart outer_roof_4_r1;
	private final ModelPart outer_roof_3_r1;
	private final ModelPart door_tcl;
	private final ModelPart door_left_tcl;
	private final ModelPart door_left_top_r1;
	private final ModelPart door_right_tcl;
	private final ModelPart door_right_top_r1;
	private final ModelPart door_tcl_handrail;
	private final ModelPart door_ael;
	private final ModelPart door_left_ael;
	private final ModelPart door_left_top_r2;
	private final ModelPart door_right_ael;
	private final ModelPart door_right_top_r2;
	private final ModelPart door_ael_handrail;
	private final ModelPart upper_wall_right_r1;
	private final ModelPart lower_wall_right_r1;
	private final ModelPart upper_wall_left_r1;
	private final ModelPart lower_wall_left_r1;
	private final ModelPart handrail_left_r1;
	private final ModelPart door_exterior_tcl;
	private final ModelPart upper_wall_r5;
	private final ModelPart floor_r3;
	private final ModelPart door_left_exterior_tcl;
	private final ModelPart door_left_top_r3;
	private final ModelPart door_left_base_r1;
	private final ModelPart door_right_exterior_tcl;
	private final ModelPart door_right_top_r3;
	private final ModelPart door_right_base_r1;
	private final ModelPart door_exterior_ael;
	private final ModelPart upper_wall_2_r2;
	private final ModelPart floor_2_r2;
	private final ModelPart lower_wall_1_r1;
	private final ModelPart upper_wall_1_r2;
	private final ModelPart floor_1_r2;
	private final ModelPart door_left_exterior_ael;
	private final ModelPart door_left_top_r4;
	private final ModelPart door_left_base_r2;
	private final ModelPart door_right_exterior_ael;
	private final ModelPart door_right_top_r4;
	private final ModelPart door_right_base_r2;
	private final ModelPart door_exterior_end;
	private final ModelPart upper_wall_r6;
	private final ModelPart floor_r4;
	private final ModelPart door_left_exterior_end;
	private final ModelPart door_left_top_r5;
	private final ModelPart door_left_base_r3;
	private final ModelPart door_right_exterior_end;
	private final ModelPart door_right_top_r5;
	private final ModelPart door_right_base_r3;
	private final ModelPart luggage_rack;
	private final ModelPart top_3_r1;
	private final ModelPart top_2_r1;
	private final ModelPart top_1_r1;
	private final ModelPart upper_wall_r7;
	private final ModelPart end_tcl;
	private final ModelPart upper_wall_2_r3;
	private final ModelPart upper_wall_1_r3;
	private final ModelPart end_ael;
	private final ModelPart end_exterior_tcl;
	private final ModelPart upper_wall_2_r4;
	private final ModelPart upper_wall_1_r4;
	private final ModelPart floor_2_r3;
	private final ModelPart floor_1_r3;
	private final ModelPart end_exterior_ael;
	private final ModelPart upper_wall_3_r1;
	private final ModelPart upper_wall_2_r5;
	private final ModelPart floor_3_r1;
	private final ModelPart floor_2_r4;
	private final ModelPart end_door_ael;
	private final ModelPart roof_end;
	private final ModelPart handrail_2_r2;
	private final ModelPart inner_roof_1;
	private final ModelPart inner_roof_4_r5;
	private final ModelPart inner_roof_3_r5;
	private final ModelPart inner_roof_2_r4;
	private final ModelPart inner_roof_2;
	private final ModelPart inner_roof_4_r6;
	private final ModelPart inner_roof_3_r6;
	private final ModelPart inner_roof_2_r5;
	private final ModelPart roof_end_exterior;
	private final ModelPart vent_2_r1;
	private final ModelPart vent_1_r1;
	private final ModelPart outer_roof_1;
	private final ModelPart outer_roof_6_r2;
	private final ModelPart outer_roof_5_r2;
	private final ModelPart outer_roof_4_r2;
	private final ModelPart outer_roof_3_r2;
	private final ModelPart outer_roof_2;
	private final ModelPart outer_roof_5_r3;
	private final ModelPart outer_roof_4_r3;
	private final ModelPart outer_roof_3_r3;
	private final ModelPart outer_roof_6_r3;
	private final ModelPart roof_light_tcl;
	private final ModelPart roof_light_r1;
	private final ModelPart roof_light_door_ael;
	private final ModelPart roof_light_window_ael;
	private final ModelPart inner_roof_3_r7;
	private final ModelPart roof_end_light;
	private final ModelPart roof_light_2_r1;
	private final ModelPart roof_light_1_r1;
	private final ModelPart head_tcl;
	private final ModelPart upper_wall_2_r6;
	private final ModelPart upper_wall_1_r5;
	private final ModelPart head_ael;
	private final ModelPart head_exterior;
	private final ModelPart upper_wall_2_r7;
	private final ModelPart upper_wall_1_r6;
	private final ModelPart floor_2_r5;
	private final ModelPart floor_1_r4;
	private final ModelPart front;
	private final ModelPart front_bottom_5_r1;
	private final ModelPart front_bottom_4_r1;
	private final ModelPart front_bottom_2_r1;
	private final ModelPart front_bottom_1_r1;
	private final ModelPart front_panel_3_r1;
	private final ModelPart front_panel_2_r1;
	private final ModelPart front_panel_1_r1;
	private final ModelPart side_1;
	private final ModelPart front_side_bottom_2_r1;
	private final ModelPart front_middle_top_r1;
	private final ModelPart outer_roof_8_r1;
	private final ModelPart outer_roof_7_r1;
	private final ModelPart outer_roof_6_r4;
	private final ModelPart front_panel_8_r1;
	private final ModelPart front_panel_7_r1;
	private final ModelPart front_panel_6_r1;
	private final ModelPart front_panel_5_r1;
	private final ModelPart front_panel_4_r1;
	private final ModelPart front_side_upper_2_r1;
	private final ModelPart front_side_lower_2_r1;
	private final ModelPart side_2;
	private final ModelPart front_side_bottom_2_r2;
	private final ModelPart front_middle_top_r2;
	private final ModelPart outer_roof_8_r2;
	private final ModelPart outer_roof_7_r2;
	private final ModelPart outer_roof_6_r5;
	private final ModelPart front_panel_8_r2;
	private final ModelPart front_panel_7_r2;
	private final ModelPart front_panel_6_r2;
	private final ModelPart front_panel_5_r2;
	private final ModelPart front_panel_4_r2;
	private final ModelPart front_side_upper_2_r2;
	private final ModelPart front_side_lower_2_r2;
	private final ModelPart headlights;
	private final ModelPart headlight_2b_r1;
	private final ModelPart headlight_2a_r1;
	private final ModelPart headlight_1a_r1;
	private final ModelPart tail_lights;
	private final ModelPart tail_light_2a_r1;
	private final ModelPart tail_light_1a_r1;
	private final ModelPart seat;
	private final ModelPart top_right_r1;
	private final ModelPart top_left_r1;
	private final ModelPart back_right_r1;
	private final ModelPart back_left_r1;
	private final ModelPart back_r1;
	private final ModelPart door_light_on;
	private final ModelPart light_r1;
	private final ModelPart door_light_off;
	private final ModelPart light_r2;

	protected final boolean isAel;

	public ModelATrain(boolean isAel) {
		this.isAel = isAel;

		textureWidth = 336;
		textureHeight = 336;
		window_tcl = new ModelPart(this);
		window_tcl.setPivot(0, 24, 0);
		window_tcl.setTextureOffset(0, 0).addCuboid(-20, 0, -24, 20, 1, 48, 0, false);
		window_tcl.setTextureOffset(0, 120).addCuboid(-21, -14, -26, 3, 14, 52, 0, false);

		upper_wall_r1 = new ModelPart(this);
		upper_wall_r1.setPivot(-21, -14, 0);
		window_tcl.addChild(upper_wall_r1);
		setRotationAngle(upper_wall_r1, 0, 0, 0.1396F);
		upper_wall_r1.setTextureOffset(0, 49).addCuboid(0, -19, -26, 3, 19, 52, 0, false);

		window_tcl_handrails = new ModelPart(this);
		window_tcl_handrails.setPivot(0, 24, 0);
		window_tcl_handrails.setTextureOffset(142, 76).addCuboid(-18, -6, -22, 7, 1, 44, 0, false);
		window_tcl_handrails.setTextureOffset(180, 121).addCuboid(-18, -5, -21, 5, 5, 42, 0, false);
		window_tcl_handrails.setTextureOffset(4, 0).addCuboid(0, -35, -22, 0, 35, 0, 0.2F, false);
		window_tcl_handrails.setTextureOffset(4, 0).addCuboid(0, -35, 22, 0, 35, 0, 0.2F, false);
		window_tcl_handrails.setTextureOffset(42, 40).addCuboid(-1, -32, -5.5F, 2, 4, 0, 0, false);
		window_tcl_handrails.setTextureOffset(42, 40).addCuboid(-1, -32, -16.5F, 2, 4, 0, 0, false);
		window_tcl_handrails.setTextureOffset(42, 40).addCuboid(-1, -32, 5.5F, 2, 4, 0, 0, false);
		window_tcl_handrails.setTextureOffset(42, 40).addCuboid(-1, -32, 16.5F, 2, 4, 0, 0, false);
		window_tcl_handrails.setTextureOffset(4, 0).addCuboid(0, -35, 0, 0, 35, 0, 0.2F, false);

		handrail_8_r1 = new ModelPart(this);
		handrail_8_r1.setPivot(0, 0, 0);
		window_tcl_handrails.addChild(handrail_8_r1);
		setRotationAngle(handrail_8_r1, -1.5708F, 0, 0);
		handrail_8_r1.setTextureOffset(0, 0).addCuboid(0, -24, -31.5F, 0, 48, 0, 0.2F, false);

		handrail_3_r1 = new ModelPart(this);
		handrail_3_r1.setPivot(-11, -5, 0);
		window_tcl_handrails.addChild(handrail_3_r1);
		setRotationAngle(handrail_3_r1, 0, 0, -0.0698F);
		handrail_3_r1.setTextureOffset(8, 0).addCuboid(0, -27.2F, 22, 0, 27, 0, 0.2F, false);
		handrail_3_r1.setTextureOffset(0, 0).addCuboid(0, -27.2F, 0, 0, 4, 0, 0.2F, false);
		handrail_3_r1.setTextureOffset(8, 0).addCuboid(0, -27.2F, -22, 0, 27, 0, 0.2F, false);

		top_handrail_3_r1 = new ModelPart(this);
		top_handrail_3_r1.setPivot(-11, -5, 0);
		window_tcl_handrails.addChild(top_handrail_3_r1);
		setRotationAngle(top_handrail_3_r1, -1.5708F, 0, -0.0698F);
		top_handrail_3_r1.setTextureOffset(0, 0).addCuboid(0, -22, -23, 0, 44, 0, 0.2F, false);

		seat_back_r1 = new ModelPart(this);
		seat_back_r1.setPivot(-17, -6, 0);
		window_tcl_handrails.addChild(seat_back_r1);
		setRotationAngle(seat_back_r1, 0, 0, -0.0524F);
		seat_back_r1.setTextureOffset(116, 175).addCuboid(-1, -8, -22, 1, 8, 44, 0, false);

		bench = new ModelPart(this);
		bench.setPivot(0, 0, 0);
		window_tcl_handrails.addChild(bench);


		window_ael = new ModelPart(this);
		window_ael.setPivot(0, 24, 0);
		window_ael.setTextureOffset(22, 22).addCuboid(-20, 0, -13, 20, 1, 26, 0, false);
		window_ael.setTextureOffset(28, 146).addCuboid(-21, -14, -13, 3, 14, 26, 0, false);

		upper_wall_r2 = new ModelPart(this);
		upper_wall_r2.setPivot(-21, -14, 0);
		window_ael.addChild(upper_wall_r2);
		setRotationAngle(upper_wall_r2, 0, 0, 0.1396F);
		upper_wall_r2.setTextureOffset(26, 75).addCuboid(0, -19, -13, 3, 19, 26, 0, false);

		window_exterior_tcl = new ModelPart(this);
		window_exterior_tcl.setPivot(0, 24, 0);
		window_exterior_tcl.setTextureOffset(58, 73).addCuboid(-21, -14, -26, 0, 14, 52, 0, false);

		upper_wall_r3 = new ModelPart(this);
		upper_wall_r3.setPivot(-21, -14, 0);
		window_exterior_tcl.addChild(upper_wall_r3);
		setRotationAngle(upper_wall_r3, 0, 0, 0.1396F);
		upper_wall_r3.setTextureOffset(58, 0).addCuboid(0, -23, -26, 0, 23, 52, 0, false);

		floor_r1 = new ModelPart(this);
		floor_r1.setPivot(-21, 0, 0);
		window_exterior_tcl.addChild(floor_r1);
		setRotationAngle(floor_r1, 0, 0, -0.1745F);
		floor_r1.setTextureOffset(62, 139).addCuboid(0, 0, -24, 1, 8, 48, 0, false);

		window_exterior_ael = new ModelPart(this);
		window_exterior_ael.setPivot(0, 24, 0);
		window_exterior_ael.setTextureOffset(58, 99).addCuboid(-21, -14, -13, 0, 14, 26, 0, false);

		upper_wall_r4 = new ModelPart(this);
		upper_wall_r4.setPivot(-21, -14, 0);
		window_exterior_ael.addChild(upper_wall_r4);
		setRotationAngle(upper_wall_r4, 0, 0, 0.1396F);
		upper_wall_r4.setTextureOffset(58, 26).addCuboid(0, -23, -13, 0, 23, 26, 0, false);

		floor_r2 = new ModelPart(this);
		floor_r2.setPivot(-21, 0, 0);
		window_exterior_ael.addChild(floor_r2);
		setRotationAngle(floor_r2, 0, 0, -0.1745F);
		floor_r2.setTextureOffset(84, 161).addCuboid(0, 0, -13, 1, 8, 26, 0, false);

		window_exterior_end_tcl = new ModelPart(this);
		window_exterior_end_tcl.setPivot(0, 24, 0);
		window_exterior_end_tcl.setTextureOffset(212, 144).addCuboid(21, -14, -26, 0, 14, 52, 0, true);
		window_exterior_end_tcl.setTextureOffset(212, 144).addCuboid(-21, -14, -26, 0, 14, 52, 0, false);

		upper_wall_2_r1 = new ModelPart(this);
		upper_wall_2_r1.setPivot(-21, -14, 0);
		window_exterior_end_tcl.addChild(upper_wall_2_r1);
		setRotationAngle(upper_wall_2_r1, 0, 0, 0.1396F);
		upper_wall_2_r1.setTextureOffset(58, 0).addCuboid(0, -23, -26, 0, 23, 52, 0, false);

		floor_2_r1 = new ModelPart(this);
		floor_2_r1.setPivot(-21, 0, 0);
		window_exterior_end_tcl.addChild(floor_2_r1);
		setRotationAngle(floor_2_r1, 0, 0, -0.1745F);
		floor_2_r1.setTextureOffset(8, 272).addCuboid(0, 0, -24, 1, 8, 48, 0, false);

		upper_wall_1_r1 = new ModelPart(this);
		upper_wall_1_r1.setPivot(21, -14, 0);
		window_exterior_end_tcl.addChild(upper_wall_1_r1);
		setRotationAngle(upper_wall_1_r1, 0, 0, -0.1396F);
		upper_wall_1_r1.setTextureOffset(58, 0).addCuboid(0, -23, -26, 0, 23, 52, 0, true);

		floor_1_r1 = new ModelPart(this);
		floor_1_r1.setPivot(21, 0, 0);
		window_exterior_end_tcl.addChild(floor_1_r1);
		setRotationAngle(floor_1_r1, 0, 0, 0.1745F);
		floor_1_r1.setTextureOffset(8, 272).addCuboid(-1, 0, -24, 1, 8, 48, 0, true);

		window_exterior_end_ael = new ModelPart(this);
		window_exterior_end_ael.setPivot(0, 24, 0);
		window_exterior_end_ael.setTextureOffset(0, 210).addCuboid(21, -14, -2, 0, 14, 26, 0, true);
		window_exterior_end_ael.setTextureOffset(0, 210).addCuboid(-21, -14, -2, 0, 14, 26, 0, false);
		window_exterior_end_ael.setTextureOffset(0, 224).addCuboid(21, -14, 24, 0, 14, 26, 0, true);
		window_exterior_end_ael.setTextureOffset(0, 224).addCuboid(-21, -14, 24, 0, 14, 26, 0, false);
		window_exterior_end_ael.setTextureOffset(0, 238).addCuboid(21, -14, 50, 0, 14, 26, 0, true);
		window_exterior_end_ael.setTextureOffset(0, 238).addCuboid(-21, -14, 50, 0, 14, 26, 0, false);

		upper_wall_6_r1 = new ModelPart(this);
		upper_wall_6_r1.setPivot(-21, -14, 0);
		window_exterior_end_ael.addChild(upper_wall_6_r1);
		setRotationAngle(upper_wall_6_r1, 0, 0, 0.1396F);
		upper_wall_6_r1.setTextureOffset(58, 26).addCuboid(0, -23, 50, 0, 23, 26, 0, false);
		upper_wall_6_r1.setTextureOffset(58, 26).addCuboid(0, -23, 24, 0, 23, 26, 0, false);
		upper_wall_6_r1.setTextureOffset(58, 26).addCuboid(0, -23, -2, 0, 23, 26, 0, false);

		floor_6_r1 = new ModelPart(this);
		floor_6_r1.setPivot(-21, 0, 0);
		window_exterior_end_ael.addChild(floor_6_r1);
		setRotationAngle(floor_6_r1, 0, 0, -0.1745F);
		floor_6_r1.setTextureOffset(181, 264).addCuboid(0, 0, 50, 1, 8, 26, 0, false);
		floor_6_r1.setTextureOffset(181, 256).addCuboid(0, 0, 24, 1, 8, 26, 0, false);
		floor_6_r1.setTextureOffset(181, 248).addCuboid(0, 0, -2, 1, 8, 26, 0, false);

		upper_wall_5_r1 = new ModelPart(this);
		upper_wall_5_r1.setPivot(21, -14, 0);
		window_exterior_end_ael.addChild(upper_wall_5_r1);
		setRotationAngle(upper_wall_5_r1, 0, 0, -0.1396F);
		upper_wall_5_r1.setTextureOffset(58, 26).addCuboid(0, -23, 50, 0, 23, 26, 0, true);
		upper_wall_5_r1.setTextureOffset(58, 26).addCuboid(0, -23, 24, 0, 23, 26, 0, true);
		upper_wall_5_r1.setTextureOffset(58, 26).addCuboid(0, -23, -2, 0, 23, 26, 0, true);

		floor_5_r1 = new ModelPart(this);
		floor_5_r1.setPivot(21, 0, 0);
		window_exterior_end_ael.addChild(floor_5_r1);
		setRotationAngle(floor_5_r1, 0, 0, 0.1745F);
		floor_5_r1.setTextureOffset(181, 264).addCuboid(-1, 0, 50, 1, 8, 26, 0, true);
		floor_5_r1.setTextureOffset(181, 256).addCuboid(-1, 0, 24, 1, 8, 26, 0, true);
		floor_5_r1.setTextureOffset(181, 248).addCuboid(-1, 0, -2, 1, 8, 26, 0, true);

		side_panel_tcl = new ModelPart(this);
		side_panel_tcl.setPivot(0, 24, 0);
		side_panel_tcl.setTextureOffset(90, 139).addCuboid(-18, -35, 0, 7, 30, 0, 0, false);

		side_panel_tcl_translucent = new ModelPart(this);
		side_panel_tcl_translucent.setPivot(0, 24, 0);
		side_panel_tcl_translucent.setTextureOffset(76, 139).addCuboid(-18, -35, 0, 7, 30, 0, 0, false);

		side_panel_ael = new ModelPart(this);
		side_panel_ael.setPivot(0, 24, 0);
		side_panel_ael.setTextureOffset(26, 281).addCuboid(-18, -34, 0, 12, 34, 0, 0, false);

		side_panel_ael_translucent = new ModelPart(this);
		side_panel_ael_translucent.setPivot(0, 24, 0);
		side_panel_ael_translucent.setTextureOffset(294, 108).addCuboid(-18, -34, 0, 12, 34, 0, 0, false);

		roof_window_tcl = new ModelPart(this);
		roof_window_tcl.setPivot(0, 24, 0);
		roof_window_tcl.setTextureOffset(62, 0).addCuboid(-16, -32, -24, 3, 0, 48, 0, false);
		roof_window_tcl.setTextureOffset(52, 0).addCuboid(-5, -34.5F, -24, 5, 0, 48, 0, false);

		inner_roof_4_r1 = new ModelPart(this);
		inner_roof_4_r1.setPivot(-3.5384F, -34.6286F, 0);
		roof_window_tcl.addChild(inner_roof_4_r1);
		setRotationAngle(inner_roof_4_r1, 0, 0, -0.1396F);
		inner_roof_4_r1.setTextureOffset(40, 0).addCuboid(-6, 0, -24, 6, 0, 48, 0, false);

		inner_roof_3_r1 = new ModelPart(this);
		inner_roof_3_r1.setPivot(-10.4309F, -33.4846F, 0);
		roof_window_tcl.addChild(inner_roof_3_r1);
		setRotationAngle(inner_roof_3_r1, 0, 0, -0.3142F);
		inner_roof_3_r1.setTextureOffset(0, 49).addCuboid(-1, 0, -24, 2, 0, 48, 0, false);

		inner_roof_2_r1 = new ModelPart(this);
		inner_roof_2_r1.setPivot(-13, -32, 0);
		roof_window_tcl.addChild(inner_roof_2_r1);
		setRotationAngle(inner_roof_2_r1, 0, 0, -0.6283F);
		inner_roof_2_r1.setTextureOffset(68, 0).addCuboid(0, 0, -24, 2, 0, 48, 0, false);

		roof_window_ael = new ModelPart(this);
		roof_window_ael.setPivot(0, 24, 0);
		roof_window_ael.setTextureOffset(4, 134).addCuboid(-16, -32.3F, -13, 2, 0, 26, 0, false);
		roof_window_ael.setTextureOffset(12, 120).addCuboid(-4, -35.6679F, -13, 4, 0, 26, 0, false);

		inner_roof_5_r1 = new ModelPart(this);
		inner_roof_5_r1.setPivot(-5.1202F, -33.4588F, 0);
		roof_window_ael.addChild(inner_roof_5_r1);
		setRotationAngle(inner_roof_5_r1, 0, 0, -0.1047F);
		inner_roof_5_r1.setTextureOffset(11, 120).addCuboid(-2.8798F, -2, -13, 5, 0, 26, 0, false);

		inner_roof_4_r2 = new ModelPart(this);
		inner_roof_4_r2.setPivot(-9.0655F, -32.834F, 0);
		roof_window_ael.addChild(inner_roof_4_r2);
		setRotationAngle(inner_roof_4_r2, 0, 0, -0.2094F);
		inner_roof_4_r2.setTextureOffset(10, 120).addCuboid(-3, -2, -13, 6, 0, 26, 0, false);

		inner_roof_3_r2 = new ModelPart(this);
		inner_roof_3_r2.setPivot(-15, -32, 0);
		roof_window_ael.addChild(inner_roof_3_r2);
		setRotationAngle(inner_roof_3_r2, 0, 0, -0.1047F);
		inner_roof_3_r2.setTextureOffset(0, 134).addCuboid(1, -2, -13, 2, 2, 26, 0, false);

		roof_door_tcl = new ModelPart(this);
		roof_door_tcl.setPivot(0, 24, 0);
		roof_door_tcl.setTextureOffset(201, 269).addCuboid(-18, -33, -16, 5, 1, 32, 0, false);
		roof_door_tcl.setTextureOffset(68, 8).addCuboid(-5, -34.5F, -16, 5, 0, 32, 0, false);

		inner_roof_4_r3 = new ModelPart(this);
		inner_roof_4_r3.setPivot(-3.5384F, -34.6286F, 0);
		roof_door_tcl.addChild(inner_roof_4_r3);
		setRotationAngle(inner_roof_4_r3, 0, 0, -0.1396F);
		inner_roof_4_r3.setTextureOffset(56, 8).addCuboid(-6, 0, -16, 6, 0, 32, 0, false);

		inner_roof_3_r3 = new ModelPart(this);
		inner_roof_3_r3.setPivot(-10.4309F, -33.4846F, 0);
		roof_door_tcl.addChild(inner_roof_3_r3);
		setRotationAngle(inner_roof_3_r3, 0, 0, -0.3142F);
		inner_roof_3_r3.setTextureOffset(126, 0).addCuboid(-1, 0, -16, 2, 0, 32, 0, false);

		inner_roof_2_r2 = new ModelPart(this);
		inner_roof_2_r2.setPivot(-13, -32, 0);
		roof_door_tcl.addChild(inner_roof_2_r2);
		setRotationAngle(inner_roof_2_r2, 0, 0, -0.6283F);
		inner_roof_2_r2.setTextureOffset(122, 0).addCuboid(0, 0, -16, 2, 0, 32, 0, false);

		handrail_2_r1 = new ModelPart(this);
		handrail_2_r1.setPivot(0, 0, 0);
		roof_door_tcl.addChild(handrail_2_r1);
		setRotationAngle(handrail_2_r1, -1.5708F, 0, 0);
		handrail_2_r1.setTextureOffset(0, 0).addCuboid(0, -16, -31.5F, 0, 32, 0, 0.2F, false);

		roof_door_ael = new ModelPart(this);
		roof_door_ael.setPivot(0, 24, 0);
		roof_door_ael.setTextureOffset(211, 277).addCuboid(-18, -33, -12, 3, 1, 24, 0, false);
		roof_door_ael.setTextureOffset(209, 179).addCuboid(-3.1311F, -33.6679F, -28, 4, 0, 56, 0, false);

		display_main_r1 = new ModelPart(this);
		display_main_r1.setPivot(0, -33.4588F, -28);
		roof_door_ael.addChild(display_main_r1);
		setRotationAngle(display_main_r1, 0.1745F, 0, 0);
		display_main_r1.setTextureOffset(60, 156).addCuboid(-10, -2.5412F, -0.1F, 20, 4, 0, 0, false);

		display_6_r1 = new ModelPart(this);
		display_6_r1.setPivot(-1.1311F, -33.6679F, 28);
		roof_door_ael.addChild(display_6_r1);
		setRotationAngle(display_6_r1, -0.1745F, 0, 0);
		display_6_r1.setTextureOffset(34, 146).addCuboid(-2.8689F, -3, 0, 4, 3, 0, 0, false);

		display_5_r1 = new ModelPart(this);
		display_5_r1.setPivot(-5.1202F, -33.4588F, 28);
		roof_door_ael.addChild(display_5_r1);
		setRotationAngle(display_5_r1, -0.1745F, 0, -0.1047F);
		display_5_r1.setTextureOffset(34, 146).addCuboid(-2.8798F, -3, 0, 5, 3, 0, 0, false);

		display_4_r1 = new ModelPart(this);
		display_4_r1.setPivot(-9.0655F, -32.834F, 28);
		roof_door_ael.addChild(display_4_r1);
		setRotationAngle(display_4_r1, -0.1745F, 0, -0.2094F);
		display_4_r1.setTextureOffset(34, 146).addCuboid(-3, -3, 0, 6, 3, 0, 0, false);

		display_3_r1 = new ModelPart(this);
		display_3_r1.setPivot(-1.1311F, -33.6679F, -28);
		roof_door_ael.addChild(display_3_r1);
		setRotationAngle(display_3_r1, 0.1745F, 0, 0);
		display_3_r1.setTextureOffset(34, 146).addCuboid(-2.8689F, -3, 0, 4, 3, 0, 0, false);

		display_2_r1 = new ModelPart(this);
		display_2_r1.setPivot(-5.1202F, -33.4588F, -28);
		roof_door_ael.addChild(display_2_r1);
		setRotationAngle(display_2_r1, 0.1745F, 0, -0.1047F);
		display_2_r1.setTextureOffset(34, 146).addCuboid(-2.8798F, -3, 0, 5, 3, 0, 0, false);

		display_1_r1 = new ModelPart(this);
		display_1_r1.setPivot(-9.0655F, -32.834F, -28);
		roof_door_ael.addChild(display_1_r1);
		setRotationAngle(display_1_r1, 0.1745F, 0, -0.2094F);
		display_1_r1.setTextureOffset(34, 146).addCuboid(-3, -3, 0, 6, 3, 0, 0, false);

		inner_roof_4_r4 = new ModelPart(this);
		inner_roof_4_r4.setPivot(-5.1202F, -33.4588F, 0);
		roof_door_ael.addChild(inner_roof_4_r4);
		setRotationAngle(inner_roof_4_r4, 0, 0, -0.1047F);
		inner_roof_4_r4.setTextureOffset(201, 179).addCuboid(-2, 0, -28, 4, 0, 56, 0, false);

		inner_roof_3_r4 = new ModelPart(this);
		inner_roof_3_r4.setPivot(-9.0655F, -32.834F, 0);
		roof_door_ael.addChild(inner_roof_3_r4);
		setRotationAngle(inner_roof_3_r4, 0, 0, -0.2094F);
		inner_roof_3_r4.setTextureOffset(193, 179).addCuboid(-2, 0, -28, 4, 0, 56, 0, false);

		inner_roof_2_r3 = new ModelPart(this);
		inner_roof_2_r3.setPivot(-15, -32, 0);
		roof_door_ael.addChild(inner_roof_2_r3);
		setRotationAngle(inner_roof_2_r3, 0, 0, -0.1047F);
		inner_roof_2_r3.setTextureOffset(225, 195).addCuboid(0, 0, -12, 4, 0, 24, 0, false);

		roof_exterior = new ModelPart(this);
		roof_exterior.setPivot(0, 24, 0);


		outer_roof_6_r1 = new ModelPart(this);
		outer_roof_6_r1.setPivot(-2.339F, -41.5711F, 0);
		roof_exterior.addChild(outer_roof_6_r1);
		setRotationAngle(outer_roof_6_r1, 0, 0, 1.5708F);
		outer_roof_6_r1.setTextureOffset(106, 274).addCuboid(0, -3, -20, 0, 6, 40, 0, false);

		outer_roof_5_r1 = new ModelPart(this);
		outer_roof_5_r1.setPivot(-9.7706F, -40.7897F, 0);
		roof_exterior.addChild(outer_roof_5_r1);
		setRotationAngle(outer_roof_5_r1, 0, 0, 1.3963F);
		outer_roof_5_r1.setTextureOffset(106, 280).addCuboid(0, -4.5F, -20, 0, 9, 40, 0, false);

		outer_roof_4_r1 = new ModelPart(this);
		outer_roof_4_r1.setPivot(-15.501F, -39.2584F, 0);
		roof_exterior.addChild(outer_roof_4_r1);
		setRotationAngle(outer_roof_4_r1, 0, 0, 1.0472F);
		outer_roof_4_r1.setTextureOffset(106, 289).addCuboid(0, -1.5F, -20, 0, 3, 40, 0, false);

		outer_roof_3_r1 = new ModelPart(this);
		outer_roof_3_r1.setPivot(-18.6652F, -37.2758F, 0);
		roof_exterior.addChild(outer_roof_3_r1);
		setRotationAngle(outer_roof_3_r1, 0, 0, 0.5236F);
		outer_roof_3_r1.setTextureOffset(106, 292).addCuboid(1, -2, -20, 0, 2, 40, 0, false);

		door_tcl = new ModelPart(this);
		door_tcl.setPivot(0, 24, 0);
		door_tcl.setTextureOffset(0, 195).addCuboid(-20, 0, -16, 20, 1, 32, 0, false);

		door_left_tcl = new ModelPart(this);
		door_left_tcl.setPivot(0, 0, 0);
		door_tcl.addChild(door_left_tcl);
		door_left_tcl.setTextureOffset(280, 168).addCuboid(-21, -14, 0, 1, 14, 14, 0, false);

		door_left_top_r1 = new ModelPart(this);
		door_left_top_r1.setPivot(-20.8F, -14, 0);
		door_left_tcl.addChild(door_left_top_r1);
		setRotationAngle(door_left_top_r1, 0, 0, 0.1396F);
		door_left_top_r1.setTextureOffset(68, 279).addCuboid(-0.2F, -19, 0, 1, 19, 14, 0, false);

		door_right_tcl = new ModelPart(this);
		door_right_tcl.setPivot(0, 0, 0);
		door_tcl.addChild(door_right_tcl);
		door_right_tcl.setTextureOffset(56, 233).addCuboid(-21, -14, -14, 1, 14, 14, 0, false);

		door_right_top_r1 = new ModelPart(this);
		door_right_top_r1.setPivot(-20.8F, -14, 0);
		door_right_tcl.addChild(door_right_top_r1);
		setRotationAngle(door_right_top_r1, 0, 0, 0.1396F);
		door_right_top_r1.setTextureOffset(0, 190).addCuboid(-0.2F, -19, -14, 1, 19, 14, 0, false);

		door_tcl_handrail = new ModelPart(this);
		door_tcl_handrail.setPivot(0, 24, 0);
		door_tcl_handrail.setTextureOffset(4, 0).addCuboid(0, -35, 0, 0, 35, 0, 0.2F, false);

		door_ael = new ModelPart(this);
		door_ael.setPivot(0, 24, 0);
		door_ael.setTextureOffset(8, 203).addCuboid(-20, 0, -12, 20, 1, 24, 0, false);

		door_left_ael = new ModelPart(this);
		door_left_ael.setPivot(0, 0, 0);
		door_ael.addChild(door_left_ael);
		door_left_ael.setTextureOffset(283, 171).addCuboid(-21, -14, 0, 1, 14, 11, 0, false);

		door_left_top_r2 = new ModelPart(this);
		door_left_top_r2.setPivot(-20.8F, -14, 0);
		door_left_ael.addChild(door_left_top_r2);
		setRotationAngle(door_left_top_r2, 0, 0, 0.1396F);
		door_left_top_r2.setTextureOffset(71, 282).addCuboid(-0.2F, -19, 0, 1, 19, 11, 0, false);

		door_right_ael = new ModelPart(this);
		door_right_ael.setPivot(0, 0, 0);
		door_ael.addChild(door_right_ael);
		door_right_ael.setTextureOffset(59, 236).addCuboid(-21, -14, -11, 1, 14, 11, 0, false);

		door_right_top_r2 = new ModelPart(this);
		door_right_top_r2.setPivot(-20.8F, -14, 0);
		door_right_ael.addChild(door_right_top_r2);
		setRotationAngle(door_right_top_r2, 0, 0, 0.1396F);
		door_right_top_r2.setTextureOffset(3, 193).addCuboid(-0.2F, -19, -11, 1, 19, 11, 0, false);

		door_ael_handrail = new ModelPart(this);
		door_ael_handrail.setPivot(0, 24, 0);
		door_ael_handrail.setTextureOffset(119, 195).addCuboid(-17, -18, -12.4F, 5, 1, 1, 0, false);

		upper_wall_right_r1 = new ModelPart(this);
		upper_wall_right_r1.setPivot(-21, -14, -11);
		door_ael_handrail.addChild(upper_wall_right_r1);
		setRotationAngle(upper_wall_right_r1, 0, 0.2014F, 0.1396F);
		upper_wall_right_r1.setTextureOffset(212, 196).addCuboid(0, -20, -1, 5, 20, 1, 0, false);

		lower_wall_right_r1 = new ModelPart(this);
		lower_wall_right_r1.setPivot(-21, 0, -11);
		door_ael_handrail.addChild(lower_wall_right_r1);
		setRotationAngle(lower_wall_right_r1, 0, 0.2014F, 0);
		lower_wall_right_r1.setTextureOffset(184, 196).addCuboid(0, -14, -1, 5, 14, 1, 0, false);

		upper_wall_left_r1 = new ModelPart(this);
		upper_wall_left_r1.setPivot(-21, -14, 11);
		door_ael_handrail.addChild(upper_wall_left_r1);
		setRotationAngle(upper_wall_left_r1, 0, 2.9402F, 0.1396F);
		upper_wall_left_r1.setTextureOffset(212, 196).addCuboid(-5, -20, -1, 5, 20, 1, 0, true);

		lower_wall_left_r1 = new ModelPart(this);
		lower_wall_left_r1.setPivot(-21, 0, 11);
		door_ael_handrail.addChild(lower_wall_left_r1);
		setRotationAngle(lower_wall_left_r1, 0, 2.9402F, 0);
		lower_wall_left_r1.setTextureOffset(184, 196).addCuboid(-5, -14, -1, 5, 14, 1, 0, true);

		handrail_left_r1 = new ModelPart(this);
		handrail_left_r1.setPivot(0, 0, 0);
		door_ael_handrail.addChild(handrail_left_r1);
		setRotationAngle(handrail_left_r1, 0, 3.1416F, 0);
		handrail_left_r1.setTextureOffset(119, 195).addCuboid(12, -18, -12.4F, 5, 1, 1, 0, true);

		door_exterior_tcl = new ModelPart(this);
		door_exterior_tcl.setPivot(0, 24, 0);


		upper_wall_r5 = new ModelPart(this);
		upper_wall_r5.setPivot(-21, -14, 0);
		door_exterior_tcl.addChild(upper_wall_r5);
		setRotationAngle(upper_wall_r5, 0, 0, 0.1396F);
		upper_wall_r5.setTextureOffset(72, 196).addCuboid(0, -23, -16, 1, 4, 32, 0, false);

		floor_r3 = new ModelPart(this);
		floor_r3.setPivot(-21, 0, 0);
		door_exterior_tcl.addChild(floor_r3);
		setRotationAngle(floor_r3, 0, 0, -0.1745F);
		floor_r3.setTextureOffset(56, 232).addCuboid(0, 0, -16, 1, 8, 32, 0, false);

		door_left_exterior_tcl = new ModelPart(this);
		door_left_exterior_tcl.setPivot(0, 0, 0);
		door_exterior_tcl.addChild(door_left_exterior_tcl);
		door_left_exterior_tcl.setTextureOffset(0, 287).addCuboid(-21, -14, 0, 0, 14, 16, 0, false);

		door_left_top_r3 = new ModelPart(this);
		door_left_top_r3.setPivot(-21, -14, 0);
		door_left_exterior_tcl.addChild(door_left_top_r3);
		setRotationAngle(door_left_top_r3, 0, 0, 0.1396F);
		door_left_top_r3.setTextureOffset(0, 265).addCuboid(0, -22, 0, 0, 22, 16, 0, false);

		door_left_base_r1 = new ModelPart(this);
		door_left_base_r1.setPivot(-21, 0, 0);
		door_left_exterior_tcl.addChild(door_left_base_r1);
		setRotationAngle(door_left_base_r1, 0, 0, -0.1745F);
		door_left_base_r1.setTextureOffset(0, 301).addCuboid(0, 0, 0, 0, 2, 16, 0, false);

		door_right_exterior_tcl = new ModelPart(this);
		door_right_exterior_tcl.setPivot(0, 0, 0);
		door_exterior_tcl.addChild(door_right_exterior_tcl);
		door_right_exterior_tcl.setTextureOffset(294, 76).addCuboid(-21, -14, -16, 0, 14, 16, 0, false);

		door_right_top_r3 = new ModelPart(this);
		door_right_top_r3.setPivot(-21, -14, 0);
		door_right_exterior_tcl.addChild(door_right_top_r3);
		setRotationAngle(door_right_top_r3, 0, 0, 0.1396F);
		door_right_top_r3.setTextureOffset(294, 54).addCuboid(0, -22, -16, 0, 22, 16, 0, false);

		door_right_base_r1 = new ModelPart(this);
		door_right_base_r1.setPivot(-21, 0, 0);
		door_right_exterior_tcl.addChild(door_right_base_r1);
		setRotationAngle(door_right_base_r1, 0, 0, -0.1745F);
		door_right_base_r1.setTextureOffset(294, 90).addCuboid(0, 0, -16, 0, 2, 16, 0, false);

		door_exterior_ael = new ModelPart(this);
		door_exterior_ael.setPivot(0, 24, 0);
		door_exterior_ael.setTextureOffset(69, 110).addCuboid(-21, -14, -28, 0, 14, 15, 0, false);

		upper_wall_2_r2 = new ModelPart(this);
		upper_wall_2_r2.setPivot(-21, -14, 0);
		door_exterior_ael.addChild(upper_wall_2_r2);
		setRotationAngle(upper_wall_2_r2, 0, 0, 0.1396F);
		upper_wall_2_r2.setTextureOffset(110, 35).addCuboid(0, -23, -28, 0, 23, 17, 0, false);
		upper_wall_2_r2.setTextureOffset(78, 202).addCuboid(0, -23, -13, 1, 4, 26, 0, false);

		floor_2_r2 = new ModelPart(this);
		floor_2_r2.setPivot(-21, 0, 0);
		door_exterior_ael.addChild(floor_2_r2);
		setRotationAngle(floor_2_r2, 0, 0, -0.1745F);
		floor_2_r2.setTextureOffset(95, 172).addCuboid(0, 0, -28, 1, 8, 15, 0, false);
		floor_2_r2.setTextureOffset(62, 238).addCuboid(0, 0, -13, 1, 8, 26, 0, false);

		lower_wall_1_r1 = new ModelPart(this);
		lower_wall_1_r1.setPivot(0, 0, 0);
		door_exterior_ael.addChild(lower_wall_1_r1);
		setRotationAngle(lower_wall_1_r1, 0, 3.1416F, 0);
		lower_wall_1_r1.setTextureOffset(69, 110).addCuboid(21, -14, -28, 0, 14, 15, 0, true);

		upper_wall_1_r2 = new ModelPart(this);
		upper_wall_1_r2.setPivot(-21, -14, 0);
		door_exterior_ael.addChild(upper_wall_1_r2);
		setRotationAngle(upper_wall_1_r2, 0, 3.1416F, 0.1396F);
		upper_wall_1_r2.setTextureOffset(110, 35).addCuboid(0, -23, -28, 0, 23, 17, 0, true);

		floor_1_r2 = new ModelPart(this);
		floor_1_r2.setPivot(-21, 0, 0);
		door_exterior_ael.addChild(floor_1_r2);
		setRotationAngle(floor_1_r2, 0, 3.1416F, -0.1745F);
		floor_1_r2.setTextureOffset(95, 172).addCuboid(-1, 0, -28, 1, 8, 15, 0, true);

		door_left_exterior_ael = new ModelPart(this);
		door_left_exterior_ael.setPivot(0, 0, 0);
		door_exterior_ael.addChild(door_left_exterior_ael);
		door_left_exterior_ael.setTextureOffset(0, 290).addCuboid(-21, -14, 0, 0, 14, 13, 0, false);

		door_left_top_r4 = new ModelPart(this);
		door_left_top_r4.setPivot(-21, -14, 0);
		door_left_exterior_ael.addChild(door_left_top_r4);
		setRotationAngle(door_left_top_r4, 0, 0, 0.1396F);
		door_left_top_r4.setTextureOffset(0, 268).addCuboid(0, -22, 0, 0, 22, 13, 0, false);

		door_left_base_r2 = new ModelPart(this);
		door_left_base_r2.setPivot(-21, 0, 0);
		door_left_exterior_ael.addChild(door_left_base_r2);
		setRotationAngle(door_left_base_r2, 0, 0, -0.1745F);
		door_left_base_r2.setTextureOffset(0, 304).addCuboid(0, 0, 0, 0, 2, 13, 0, false);

		door_right_exterior_ael = new ModelPart(this);
		door_right_exterior_ael.setPivot(0, 0, 0);
		door_exterior_ael.addChild(door_right_exterior_ael);
		door_right_exterior_ael.setTextureOffset(294, 79).addCuboid(-21, -14, -13, 0, 14, 13, 0, false);

		door_right_top_r4 = new ModelPart(this);
		door_right_top_r4.setPivot(-21, -14, 0);
		door_right_exterior_ael.addChild(door_right_top_r4);
		setRotationAngle(door_right_top_r4, 0, 0, 0.1396F);
		door_right_top_r4.setTextureOffset(294, 57).addCuboid(0, -22, -13, 0, 22, 13, 0, false);

		door_right_base_r2 = new ModelPart(this);
		door_right_base_r2.setPivot(-21, 0, 0);
		door_right_exterior_ael.addChild(door_right_base_r2);
		setRotationAngle(door_right_base_r2, 0, 0, -0.1745F);
		door_right_base_r2.setTextureOffset(294, 93).addCuboid(0, 0, -13, 0, 2, 13, 0, false);

		door_exterior_end = new ModelPart(this);
		door_exterior_end.setPivot(0, 24, 0);


		upper_wall_r6 = new ModelPart(this);
		upper_wall_r6.setPivot(-21, -14, 0);
		door_exterior_end.addChild(upper_wall_r6);
		setRotationAngle(upper_wall_r6, 0, 0, 0.1396F);
		upper_wall_r6.setTextureOffset(72, 196).addCuboid(0, -23, -16, 1, 4, 32, 0, false);

		floor_r4 = new ModelPart(this);
		floor_r4.setPivot(-21, 0, 0);
		door_exterior_end.addChild(floor_r4);
		setRotationAngle(floor_r4, 0, 0, -0.1745F);
		floor_r4.setTextureOffset(266, 294).addCuboid(0, 0, -16, 1, 8, 32, 0, false);

		door_left_exterior_end = new ModelPart(this);
		door_left_exterior_end.setPivot(0, 0, 0);
		door_exterior_end.addChild(door_left_exterior_end);
		door_left_exterior_end.setTextureOffset(162, 109).addCuboid(-21, -14, 0, 0, 14, 16, 0, false);

		door_left_top_r5 = new ModelPart(this);
		door_left_top_r5.setPivot(-21, -14, 0);
		door_left_exterior_end.addChild(door_left_top_r5);
		setRotationAngle(door_left_top_r5, 0, 0, 0.1396F);
		door_left_top_r5.setTextureOffset(0, 265).addCuboid(0, -22, 0, 0, 22, 16, 0, false);

		door_left_base_r3 = new ModelPart(this);
		door_left_base_r3.setPivot(-21, 0, 0);
		door_left_exterior_end.addChild(door_left_base_r3);
		setRotationAngle(door_left_base_r3, 0, 0, -0.1745F);
		door_left_base_r3.setTextureOffset(162, 123).addCuboid(0, 0, 0, 0, 2, 16, 0, false);

		door_right_exterior_end = new ModelPart(this);
		door_right_exterior_end.setPivot(0, 0, 0);
		door_exterior_end.addChild(door_right_exterior_end);
		door_right_exterior_end.setTextureOffset(162, 125).addCuboid(-21, -14, -16, 0, 14, 16, 0, false);

		door_right_top_r5 = new ModelPart(this);
		door_right_top_r5.setPivot(-21, -14, 0);
		door_right_exterior_end.addChild(door_right_top_r5);
		setRotationAngle(door_right_top_r5, 0, 0, 0.1396F);
		door_right_top_r5.setTextureOffset(294, 54).addCuboid(0, -22, -16, 0, 22, 16, 0, false);

		door_right_base_r3 = new ModelPart(this);
		door_right_base_r3.setPivot(-21, 0, 0);
		door_right_exterior_end.addChild(door_right_base_r3);
		setRotationAngle(door_right_base_r3, 0, 0, -0.1745F);
		door_right_base_r3.setTextureOffset(162, 139).addCuboid(0, 0, -16, 0, 2, 16, 0, false);

		luggage_rack = new ModelPart(this);
		luggage_rack.setPivot(0, 24, 0);
		luggage_rack.setTextureOffset(176, 218).addCuboid(-21, -14, -8, 3, 14, 16, 0, false);
		luggage_rack.setTextureOffset(60, 139).addCuboid(-18, -13, -8, 9, 1, 16, 0, false);
		luggage_rack.setTextureOffset(32, 32).addCuboid(-20, 0, -8, 20, 1, 16, 0, false);

		top_3_r1 = new ModelPart(this);
		top_3_r1.setPivot(-9.3615F, -29.3127F, -20);
		luggage_rack.addChild(top_3_r1);
		setRotationAngle(top_3_r1, 0, 0, -0.2618F);
		top_3_r1.setTextureOffset(134, 125).addCuboid(-3, -4, 12, 3, 7, 16, 0, false);

		top_2_r1 = new ModelPart(this);
		top_2_r1.setPivot(-10, -25, 0);
		luggage_rack.addChild(top_2_r1);
		setRotationAngle(top_2_r1, 0, 0, 0.7854F);
		top_2_r1.setTextureOffset(136, 132).addCuboid(-1, -2, -8, 1, 2, 16, 0, false);

		top_1_r1 = new ModelPart(this);
		top_1_r1.setPivot(0, 0, 0);
		luggage_rack.addChild(top_1_r1);
		setRotationAngle(top_1_r1, 0, 0, 1.5708F);
		top_1_r1.setTextureOffset(136, 132).addCuboid(-26, 10, -8, 1, 8, 16, 0, false);

		upper_wall_r7 = new ModelPart(this);
		upper_wall_r7.setPivot(-21, -14, 0);
		luggage_rack.addChild(upper_wall_r7);
		setRotationAngle(upper_wall_r7, 0, 0, 0.1396F);
		upper_wall_r7.setTextureOffset(142, 78).addCuboid(0, -19, -8, 3, 19, 16, 0, false);

		end_tcl = new ModelPart(this);
		end_tcl.setPivot(0, 24, 0);
		end_tcl.setTextureOffset(162, 175).addCuboid(-20, 0, -12, 40, 1, 20, 0, false);
		end_tcl.setTextureOffset(110, 195).addCuboid(18, -14, 7, 3, 14, 3, 0, true);
		end_tcl.setTextureOffset(110, 195).addCuboid(-21, -14, 7, 3, 14, 3, 0, false);
		end_tcl.setTextureOffset(127, 231).addCuboid(9.5F, -34, -12, 9, 34, 19, 0, false);
		end_tcl.setTextureOffset(0, 228).addCuboid(-18.5F, -34, -12, 9, 34, 19, 0, false);
		end_tcl.setTextureOffset(79, 293).addCuboid(-9.5F, -35, -12, 19, 2, 19, 0, false);

		upper_wall_2_r3 = new ModelPart(this);
		upper_wall_2_r3.setPivot(-21, -14, 0);
		end_tcl.addChild(upper_wall_2_r3);
		setRotationAngle(upper_wall_2_r3, 0, 0, 0.1396F);
		upper_wall_2_r3.setTextureOffset(248, 110).addCuboid(0, -19, 7, 3, 19, 3, 0, false);

		upper_wall_1_r3 = new ModelPart(this);
		upper_wall_1_r3.setPivot(21, -14, 0);
		end_tcl.addChild(upper_wall_1_r3);
		setRotationAngle(upper_wall_1_r3, 0, 0, -0.1396F);
		upper_wall_1_r3.setTextureOffset(164, 227).addCuboid(-3, -19, 7, 3, 19, 3, 0, true);

		end_ael = new ModelPart(this);
		end_ael.setPivot(0, 24, 0);
		end_ael.setTextureOffset(138, 173).addCuboid(-7, 0, -12, 14, 1, 10, 0, false);
		end_ael.setTextureOffset(128, 238).addCuboid(7, -36, -12, 11, 36, 10, 0, true);
		end_ael.setTextureOffset(128, 238).addCuboid(-18, -36, -12, 11, 36, 10, 0, false);
		end_ael.setTextureOffset(24, 186).addCuboid(-7, -36, -12, 14, 4, 10, 0, false);

		end_exterior_tcl = new ModelPart(this);
		end_exterior_tcl.setPivot(0, 24, 0);
		end_exterior_tcl.setTextureOffset(0, 134).addCuboid(18, -14, -12, 3, 14, 22, 0, true);
		end_exterior_tcl.setTextureOffset(0, 134).addCuboid(-21, -14, -12, 3, 14, 22, 0, false);
		end_exterior_tcl.setTextureOffset(182, 0).addCuboid(9.5F, -34, -12, 9, 34, 0, 0, true);
		end_exterior_tcl.setTextureOffset(182, 0).addCuboid(-18.5F, -34, -12, 9, 34, 0, 0, false);
		end_exterior_tcl.setTextureOffset(240, 236).addCuboid(-18, -41, -12, 36, 8, 0, 0, false);

		upper_wall_2_r4 = new ModelPart(this);
		upper_wall_2_r4.setPivot(-21, -14, 0);
		end_exterior_tcl.addChild(upper_wall_2_r4);
		setRotationAngle(upper_wall_2_r4, 0, 0, 0.1396F);
		upper_wall_2_r4.setTextureOffset(183, 251).addCuboid(0, -23, -12, 3, 23, 22, 0, false);

		upper_wall_1_r4 = new ModelPart(this);
		upper_wall_1_r4.setPivot(21, -14, 0);
		end_exterior_tcl.addChild(upper_wall_1_r4);
		setRotationAngle(upper_wall_1_r4, 0, 0, -0.1396F);
		upper_wall_1_r4.setTextureOffset(183, 251).addCuboid(-3, -23, -12, 3, 23, 22, 0, true);

		floor_2_r3 = new ModelPart(this);
		floor_2_r3.setPivot(-21, 0, 0);
		end_exterior_tcl.addChild(floor_2_r3);
		setRotationAngle(floor_2_r3, 0, 0, -0.1745F);
		floor_2_r3.setTextureOffset(112, 139).addCuboid(0, 0, -12, 1, 8, 20, 0, false);

		floor_1_r3 = new ModelPart(this);
		floor_1_r3.setPivot(21, 0, 0);
		end_exterior_tcl.addChild(floor_1_r3);
		setRotationAngle(floor_1_r3, 0, 0, 0.1745F);
		floor_1_r3.setTextureOffset(112, 139).addCuboid(-1, 0, -12, 1, 8, 20, 0, true);

		end_exterior_ael = new ModelPart(this);
		end_exterior_ael.setPivot(0, 24, 0);
		end_exterior_ael.setTextureOffset(149, 185).addCuboid(-20, 0, -12, 40, 1, 10, 0, false);
		end_exterior_ael.setTextureOffset(0, 134).addCuboid(18, -14, -12, 3, 14, 10, 0, true);
		end_exterior_ael.setTextureOffset(0, 134).addCuboid(-21, -14, -12, 3, 14, 10, 0, false);
		end_exterior_ael.setTextureOffset(180, 2).addCuboid(7, -32, -12, 11, 32, 0, 0, true);
		end_exterior_ael.setTextureOffset(180, 2).addCuboid(-18, -32, -12, 11, 32, 0, 0, false);
		end_exterior_ael.setTextureOffset(240, 235).addCuboid(-18, -41, -12, 36, 9, 0, 0, false);

		upper_wall_3_r1 = new ModelPart(this);
		upper_wall_3_r1.setPivot(-21, -14, 0);
		end_exterior_ael.addChild(upper_wall_3_r1);
		setRotationAngle(upper_wall_3_r1, 0, 0, 0.1396F);
		upper_wall_3_r1.setTextureOffset(196, 120).addCuboid(0, -23, -12, 3, 23, 10, 0, false);

		upper_wall_2_r5 = new ModelPart(this);
		upper_wall_2_r5.setPivot(21, -14, 0);
		end_exterior_ael.addChild(upper_wall_2_r5);
		setRotationAngle(upper_wall_2_r5, 0, 0, -0.1396F);
		upper_wall_2_r5.setTextureOffset(196, 120).addCuboid(-3, -23, -12, 3, 23, 10, 0, true);

		floor_3_r1 = new ModelPart(this);
		floor_3_r1.setPivot(-21, 0, 0);
		end_exterior_ael.addChild(floor_3_r1);
		setRotationAngle(floor_3_r1, 0, 0, -0.1745F);
		floor_3_r1.setTextureOffset(122, 149).addCuboid(0, 0, -12, 1, 8, 10, 0, false);

		floor_2_r4 = new ModelPart(this);
		floor_2_r4.setPivot(21, 0, 0);
		end_exterior_ael.addChild(floor_2_r4);
		setRotationAngle(floor_2_r4, 0, 0, 0.1745F);
		floor_2_r4.setTextureOffset(122, 149).addCuboid(-1, 0, -12, 1, 8, 10, 0, true);

		end_door_ael = new ModelPart(this);
		end_door_ael.setPivot(0, 24, 0);
		end_door_ael.setTextureOffset(280, 0).addCuboid(-7, -32, -2, 14, 32, 0, 0, false);

		roof_end = new ModelPart(this);
		roof_end.setPivot(0, 24, 0);


		handrail_2_r2 = new ModelPart(this);
		handrail_2_r2.setPivot(0, 0, 0);
		roof_end.addChild(handrail_2_r2);
		setRotationAngle(handrail_2_r2, -1.5708F, 0, 0);
		handrail_2_r2.setTextureOffset(0, 0).addCuboid(0, -40, -31.5F, 0, 16, 0, 0.2F, false);

		inner_roof_1 = new ModelPart(this);
		inner_roof_1.setPivot(-2, -33, 16);
		roof_end.addChild(inner_roof_1);
		inner_roof_1.setTextureOffset(197, 265).addCuboid(-16, 0, -12, 5, 1, 36, 0, false);
		inner_roof_1.setTextureOffset(64, 0).addCuboid(-3, -1.5F, -12, 5, 0, 36, 0, false);

		inner_roof_4_r5 = new ModelPart(this);
		inner_roof_4_r5.setPivot(-1.5384F, -1.6286F, -16);
		inner_roof_1.addChild(inner_roof_4_r5);
		setRotationAngle(inner_roof_4_r5, 0, 0, -0.1396F);
		inner_roof_4_r5.setTextureOffset(52, 0).addCuboid(-6, 0, 4, 6, 0, 36, 0, false);

		inner_roof_3_r5 = new ModelPart(this);
		inner_roof_3_r5.setPivot(-8.4309F, -0.4846F, -16);
		inner_roof_1.addChild(inner_roof_3_r5);
		setRotationAngle(inner_roof_3_r5, 0, 0, -0.3142F);
		inner_roof_3_r5.setTextureOffset(8, 49).addCuboid(-1, 0, 4, 2, 0, 36, 0, false);

		inner_roof_2_r4 = new ModelPart(this);
		inner_roof_2_r4.setPivot(-11, 1, -16);
		inner_roof_1.addChild(inner_roof_2_r4);
		setRotationAngle(inner_roof_2_r4, 0, 0, -0.6283F);
		inner_roof_2_r4.setTextureOffset(96, 0).addCuboid(0, 0, 4, 2, 0, 36, 0, false);

		inner_roof_2 = new ModelPart(this);
		inner_roof_2.setPivot(-2, -33, 16);
		roof_end.addChild(inner_roof_2);
		inner_roof_2.setTextureOffset(197, 265).addCuboid(15, 0, -12, 5, 1, 36, 0, true);
		inner_roof_2.setTextureOffset(64, 0).addCuboid(2, -1.5F, -12, 5, 0, 36, 0, true);

		inner_roof_4_r6 = new ModelPart(this);
		inner_roof_4_r6.setPivot(5.5384F, -1.6286F, -16);
		inner_roof_2.addChild(inner_roof_4_r6);
		setRotationAngle(inner_roof_4_r6, 0, 0, 0.1396F);
		inner_roof_4_r6.setTextureOffset(52, 0).addCuboid(0, 0, 4, 6, 0, 36, 0, true);

		inner_roof_3_r6 = new ModelPart(this);
		inner_roof_3_r6.setPivot(12.4309F, -0.4846F, -16);
		inner_roof_2.addChild(inner_roof_3_r6);
		setRotationAngle(inner_roof_3_r6, 0, 0, 0.3142F);
		inner_roof_3_r6.setTextureOffset(8, 49).addCuboid(-1, 0, 4, 2, 0, 36, 0, true);

		inner_roof_2_r5 = new ModelPart(this);
		inner_roof_2_r5.setPivot(15, 1, -16);
		inner_roof_2.addChild(inner_roof_2_r5);
		setRotationAngle(inner_roof_2_r5, 0, 0, 0.6283F);
		inner_roof_2_r5.setTextureOffset(96, 0).addCuboid(-2, 0, 4, 2, 0, 36, 0, true);

		roof_end_exterior = new ModelPart(this);
		roof_end_exterior.setPivot(0, 24, 0);
		roof_end_exterior.setTextureOffset(62, 75).addCuboid(-8, -42.5F, 0, 16, 2, 48, 0, false);

		vent_2_r1 = new ModelPart(this);
		vent_2_r1.setPivot(-8, -42.5F, 0);
		roof_end_exterior.addChild(vent_2_r1);
		setRotationAngle(vent_2_r1, 0, 0, -0.3491F);
		vent_2_r1.setTextureOffset(88, 1).addCuboid(-9, 0, 0, 9, 2, 48, 0, false);

		vent_1_r1 = new ModelPart(this);
		vent_1_r1.setPivot(8, -42.5F, 0);
		roof_end_exterior.addChild(vent_1_r1);
		setRotationAngle(vent_1_r1, 0, 0, 0.3491F);
		vent_1_r1.setTextureOffset(88, 1).addCuboid(0, 0, 0, 9, 2, 48, 0, true);

		outer_roof_1 = new ModelPart(this);
		outer_roof_1.setPivot(0, 0, 0);
		roof_end_exterior.addChild(outer_roof_1);


		outer_roof_6_r2 = new ModelPart(this);
		outer_roof_6_r2.setPivot(-2.3377F, -41.071F, 0);
		outer_roof_1.addChild(outer_roof_6_r2);
		setRotationAngle(outer_roof_6_r2, 0, 0, 1.5708F);
		outer_roof_6_r2.setTextureOffset(174, 137).addCuboid(-0.5F, -3, -12, 1, 6, 20, 0, false);

		outer_roof_5_r2 = new ModelPart(this);
		outer_roof_5_r2.setPivot(-9.6825F, -40.2972F, 0);
		outer_roof_1.addChild(outer_roof_5_r2);
		setRotationAngle(outer_roof_5_r2, 0, 0, 1.3963F);
		outer_roof_5_r2.setTextureOffset(154, 14).addCuboid(-0.5F, -4.5F, -12, 1, 9, 20, 0, false);

		outer_roof_4_r2 = new ModelPart(this);
		outer_roof_4_r2.setPivot(-15.25F, -38.8252F, 0);
		outer_roof_1.addChild(outer_roof_4_r2);
		setRotationAngle(outer_roof_4_r2, 0, 0, 1.0472F);
		outer_roof_4_r2.setTextureOffset(106, 195).addCuboid(-0.5F, -1.5F, -12, 1, 3, 20, 0, false);

		outer_roof_3_r2 = new ModelPart(this);
		outer_roof_3_r2.setPivot(-16.866F, -37.3922F, 0);
		outer_roof_1.addChild(outer_roof_3_r2);
		setRotationAngle(outer_roof_3_r2, 0, 0, 0.5236F);
		outer_roof_3_r2.setTextureOffset(162, 196).addCuboid(-0.5F, -1, -12, 1, 2, 20, 0, false);

		outer_roof_2 = new ModelPart(this);
		outer_roof_2.setPivot(0, 0, 0);
		roof_end_exterior.addChild(outer_roof_2);


		outer_roof_5_r3 = new ModelPart(this);
		outer_roof_5_r3.setPivot(9.6825F, -40.2972F, 0);
		outer_roof_2.addChild(outer_roof_5_r3);
		setRotationAngle(outer_roof_5_r3, 0, 0, -1.3963F);
		outer_roof_5_r3.setTextureOffset(154, 14).addCuboid(-0.5F, -4.5F, -12, 1, 9, 20, 0, true);

		outer_roof_4_r3 = new ModelPart(this);
		outer_roof_4_r3.setPivot(15.25F, -38.8252F, 0);
		outer_roof_2.addChild(outer_roof_4_r3);
		setRotationAngle(outer_roof_4_r3, 0, 0, -1.0472F);
		outer_roof_4_r3.setTextureOffset(106, 195).addCuboid(-0.5F, -1.5F, -12, 1, 3, 20, 0, true);

		outer_roof_3_r3 = new ModelPart(this);
		outer_roof_3_r3.setPivot(16.866F, -37.3922F, 0);
		outer_roof_2.addChild(outer_roof_3_r3);
		setRotationAngle(outer_roof_3_r3, 0, 0, -0.5236F);
		outer_roof_3_r3.setTextureOffset(162, 196).addCuboid(-0.5F, -1, -12, 1, 2, 20, 0, true);

		outer_roof_6_r3 = new ModelPart(this);
		outer_roof_6_r3.setPivot(2.3377F, -41.071F, 0);
		outer_roof_2.addChild(outer_roof_6_r3);
		setRotationAngle(outer_roof_6_r3, 0, 0, -1.5708F);
		outer_roof_6_r3.setTextureOffset(174, 137).addCuboid(-0.5F, -3, -12, 1, 6, 20, 0, true);

		roof_light_tcl = new ModelPart(this);
		roof_light_tcl.setPivot(0, 24, 0);


		roof_light_r1 = new ModelPart(this);
		roof_light_r1.setPivot(-7, -33.5F, 0);
		roof_light_tcl.addChild(roof_light_r1);
		setRotationAngle(roof_light_r1, 0, 0, 0.1745F);
		roof_light_r1.setTextureOffset(154, 0).addCuboid(-3, -1, -24, 3, 1, 48, 0, false);

		roof_light_door_ael = new ModelPart(this);
		roof_light_door_ael.setPivot(0, 24, 0);
		roof_light_door_ael.setTextureOffset(126, 78).addCuboid(-10, -33.7F, -8, 10, 0, 16, 0, false);

		roof_light_window_ael = new ModelPart(this);
		roof_light_window_ael.setPivot(0, 24, 0);


		inner_roof_3_r7 = new ModelPart(this);
		inner_roof_3_r7.setPivot(-15, -32, 0);
		roof_light_window_ael.addChild(inner_roof_3_r7);
		setRotationAngle(inner_roof_3_r7, 0, 0, -0.1047F);
		inner_roof_3_r7.setTextureOffset(176, 22).addCuboid(2.5F, -1.1F, -13, 2, 1, 26, 0, false);

		roof_end_light = new ModelPart(this);
		roof_end_light.setPivot(0, 24, 0);


		roof_light_2_r1 = new ModelPart(this);
		roof_light_2_r1.setPivot(7, -33.5F, 0);
		roof_end_light.addChild(roof_light_2_r1);
		setRotationAngle(roof_light_2_r1, 0, 0, -0.1745F);
		roof_light_2_r1.setTextureOffset(166, 12).addCuboid(0, -1, 4, 3, 1, 36, 0, false);

		roof_light_1_r1 = new ModelPart(this);
		roof_light_1_r1.setPivot(-7, -33.5F, 0);
		roof_end_light.addChild(roof_light_1_r1);
		setRotationAngle(roof_light_1_r1, 0, 0, 0.1745F);
		roof_light_1_r1.setTextureOffset(166, 12).addCuboid(-3, -1, 4, 3, 1, 36, 0, false);

		head_tcl = new ModelPart(this);
		head_tcl.setPivot(0, 24, 0);
		head_tcl.setTextureOffset(180, 168).addCuboid(-20, 0, 4, 40, 1, 4, 0, false);
		head_tcl.setTextureOffset(90, 75).addCuboid(18, -14, 4, 3, 14, 6, 0, true);
		head_tcl.setTextureOffset(90, 75).addCuboid(-21, -14, 4, 3, 14, 6, 0, false);
		head_tcl.setTextureOffset(208, 0).addCuboid(-18, -41, 4, 36, 41, 0, 0, false);

		upper_wall_2_r6 = new ModelPart(this);
		upper_wall_2_r6.setPivot(-21, -14, 0);
		head_tcl.addChild(upper_wall_2_r6);
		setRotationAngle(upper_wall_2_r6, 0, 0, 0.1396F);
		upper_wall_2_r6.setTextureOffset(0, 72).addCuboid(0, -19, 4, 3, 19, 6, 0, false);

		upper_wall_1_r5 = new ModelPart(this);
		upper_wall_1_r5.setPivot(21, -14, 0);
		head_tcl.addChild(upper_wall_1_r5);
		setRotationAngle(upper_wall_1_r5, 0, 0, -0.1396F);
		upper_wall_1_r5.setTextureOffset(0, 72).addCuboid(-3, -19, 4, 3, 19, 6, 0, true);

		head_ael = new ModelPart(this);
		head_ael.setPivot(0, 24, 0);
		head_ael.setTextureOffset(208, 0).addCuboid(-18, -41, -2, 36, 41, 0, 0, false);

		head_exterior = new ModelPart(this);
		head_exterior.setPivot(0, 24, 0);
		head_exterior.setTextureOffset(158, 306).addCuboid(-20, 0, -24, 40, 1, 28, 0, false);
		head_exterior.setTextureOffset(232, 110).addCuboid(18, -14, -24, 3, 14, 34, 0, true);
		head_exterior.setTextureOffset(232, 110).addCuboid(-21, -14, -24, 3, 14, 34, 0, false);
		head_exterior.setTextureOffset(200, 69).addCuboid(-18, -41, -3, 36, 41, 0, 0, false);

		upper_wall_2_r7 = new ModelPart(this);
		upper_wall_2_r7.setPivot(-21, -14, 0);
		head_exterior.addChild(upper_wall_2_r7);
		setRotationAngle(upper_wall_2_r7, 0, 0, 0.1396F);
		upper_wall_2_r7.setTextureOffset(250, 237).addCuboid(0, -23, -24, 3, 23, 34, 0, false);

		upper_wall_1_r6 = new ModelPart(this);
		upper_wall_1_r6.setPivot(21, -14, 0);
		head_exterior.addChild(upper_wall_1_r6);
		setRotationAngle(upper_wall_1_r6, 0, 0, -0.1396F);
		upper_wall_1_r6.setTextureOffset(250, 237).addCuboid(-3, -23, -24, 3, 23, 34, 0, true);

		floor_2_r5 = new ModelPart(this);
		floor_2_r5.setPivot(-21, 0, 0);
		head_exterior.addChild(floor_2_r5);
		setRotationAngle(floor_2_r5, 0, 0, -0.1745F);
		floor_2_r5.setTextureOffset(90, 252).addCuboid(0, 0, -24, 1, 8, 32, 0, false);

		floor_1_r4 = new ModelPart(this);
		floor_1_r4.setPivot(21, 0, 0);
		head_exterior.addChild(floor_1_r4);
		setRotationAngle(floor_1_r4, 0, 0, 0.1745F);
		floor_1_r4.setTextureOffset(90, 252).addCuboid(-1, 0, -24, 1, 8, 32, 0, true);

		front = new ModelPart(this);
		front.setPivot(0, 0, 0);
		head_exterior.addChild(front);
		front.setTextureOffset(142, 76).addCuboid(-9, 0.9884F, -46.7528F, 18, 2, 0, 0, false);
		front.setTextureOffset(172, 51).addCuboid(-21, 0, -24, 42, 8, 0, 0, false);

		front_bottom_5_r1 = new ModelPart(this);
		front_bottom_5_r1.setPivot(0, 4.4033F, -45.3383F);
		front.addChild(front_bottom_5_r1);
		setRotationAngle(front_bottom_5_r1, 1.4137F, 0, 0);
		front_bottom_5_r1.setTextureOffset(256, 42).addCuboid(-20, 0, -0.001F, 40, 22, 0, 0, false);

		front_bottom_4_r1 = new ModelPart(this);
		front_bottom_4_r1.setPivot(0, 3.6962F, -46.0454F);
		front.addChild(front_bottom_4_r1);
		setRotationAngle(front_bottom_4_r1, 0.7854F, 0, 0);
		front_bottom_4_r1.setTextureOffset(0, 98).addCuboid(-12, -1, 0, 24, 2, 0, 0, false);

		front_bottom_2_r1 = new ModelPart(this);
		front_bottom_2_r1.setPivot(0, 0.1009F, -46.292F);
		front.addChild(front_bottom_2_r1);
		setRotationAngle(front_bottom_2_r1, -0.48F, 0, 0);
		front_bottom_2_r1.setTextureOffset(58, 120).addCuboid(-11, -1, 0, 22, 2, 0, 0, false);

		front_bottom_1_r1 = new ModelPart(this);
		front_bottom_1_r1.setPivot(0, -4, -42);
		front.addChild(front_bottom_1_r1);
		setRotationAngle(front_bottom_1_r1, -0.8727F, 0, 0);
		front_bottom_1_r1.setTextureOffset(154, 43).addCuboid(-12, 0, 0, 24, 5, 0, 0, false);

		front_panel_3_r1 = new ModelPart(this);
		front_panel_3_r1.setPivot(0, -32.2269F, -28.3321F);
		front.addChild(front_panel_3_r1);
		setRotationAngle(front_panel_3_r1, -0.6283F, 0, 0);
		front_panel_3_r1.setTextureOffset(211, 250).addCuboid(-12, -6.5F, 0, 24, 12, 0, 0, false);

		front_panel_2_r1 = new ModelPart(this);
		front_panel_2_r1.setPivot(0, -20.5874F, -35.0728F);
		front.addChild(front_panel_2_r1);
		setRotationAngle(front_panel_2_r1, -0.4538F, 0, 0);
		front_panel_2_r1.setTextureOffset(90, 232).addCuboid(-12, -8, 0, 24, 16, 0, 0, false);

		front_panel_1_r1 = new ModelPart(this);
		front_panel_1_r1.setPivot(0, -4, -42);
		front.addChild(front_panel_1_r1);
		setRotationAngle(front_panel_1_r1, -0.3491F, 0, 0);
		front_panel_1_r1.setTextureOffset(200, 110).addCuboid(-12, -10, 0, 24, 10, 0, 0, false);

		side_1 = new ModelPart(this);
		side_1.setPivot(0, 0, 0);
		front.addChild(side_1);


		front_side_bottom_2_r1 = new ModelPart(this);
		front_side_bottom_2_r1.setPivot(18.3378F, 3.5923F, -35.3251F);
		side_1.addChild(front_side_bottom_2_r1);
		setRotationAngle(front_side_bottom_2_r1, 0, 0.1745F, 0.1745F);
		front_side_bottom_2_r1.setTextureOffset(112, 157).addCuboid(0, -4, -6.5F, 0, 8, 18, 0, true);

		front_middle_top_r1 = new ModelPart(this);
		front_middle_top_r1.setPivot(-0.6613F, -41.5702F, -11.9997F);
		side_1.addChild(front_middle_top_r1);
		setRotationAngle(front_middle_top_r1, 0, 0.3491F, -1.5708F);
		front_middle_top_r1.setTextureOffset(232, 118).addCuboid(0, 0, -14, 0, 6, 14, 0, true);

		outer_roof_8_r1 = new ModelPart(this);
		outer_roof_8_r1.setPivot(16.824F, -34.5499F, -16.9825F);
		side_1.addChild(outer_roof_8_r1);
		setRotationAngle(outer_roof_8_r1, 0, 0.1745F, -1.0472F);
		outer_roof_8_r1.setTextureOffset(70, 66).addCuboid(2.5F, -5, -3.5F, 0, 6, 9, 0, true);

		outer_roof_7_r1 = new ModelPart(this);
		outer_roof_7_r1.setPivot(17.2991F, -37.6418F, 0);
		side_1.addChild(outer_roof_7_r1);
		setRotationAngle(outer_roof_7_r1, 0, 0, -0.5236F);
		outer_roof_7_r1.setTextureOffset(22, 25).addCuboid(0, -1, -19, 0, 2, 7, 0, true);

		outer_roof_6_r4 = new ModelPart(this);
		outer_roof_6_r4.setPivot(11.7869F, -37.8295F, -19.0477F);
		side_1.addChild(outer_roof_6_r4);
		setRotationAngle(outer_roof_6_r4, 0, 0.3491F, -1.3963F);
		outer_roof_6_r4.setTextureOffset(72, 180).addCuboid(0, -7, -7.5F, 0, 14, 15, 0, true);

		front_panel_8_r1 = new ModelPart(this);
		front_panel_8_r1.setPivot(8.9994F, 2.9884F, -46.7521F);
		side_1.addChild(front_panel_8_r1);
		setRotationAngle(front_panel_8_r1, 0, -0.5672F, 0);
		front_panel_8_r1.setTextureOffset(212, 217).addCuboid(0, -5.9884F, -0.001F, 11, 8, 0, 0, true);

		front_panel_7_r1 = new ModelPart(this);
		front_panel_7_r1.setPivot(12, -4, -42);
		side_1.addChild(front_panel_7_r1);
		setRotationAngle(front_panel_7_r1, -0.7418F, -0.3491F, 0);
		front_panel_7_r1.setTextureOffset(208, 42).addCuboid(-4, 0, 0, 11, 5, 0, 0, true);

		front_panel_6_r1 = new ModelPart(this);
		front_panel_6_r1.setPivot(12, -4, -42);
		side_1.addChild(front_panel_6_r1);
		setRotationAngle(front_panel_6_r1, -0.3491F, -0.3491F, 0);
		front_panel_6_r1.setTextureOffset(162, 196).addCuboid(0, -12, 0, 9, 12, 0, 0, true);

		front_panel_5_r1 = new ModelPart(this);
		front_panel_5_r1.setPivot(12, -13.397F, -38.5798F);
		side_1.addChild(front_panel_5_r1);
		setRotationAngle(front_panel_5_r1, -0.4538F, -0.3491F, 0);
		front_panel_5_r1.setTextureOffset(112, 139).addCuboid(0, -18, -0.001F, 10, 18, 0, 0, true);

		front_panel_4_r1 = new ModelPart(this);
		front_panel_4_r1.setPivot(15.1229F, -32.2269F, -26.988F);
		side_1.addChild(front_panel_4_r1);
		setRotationAngle(front_panel_4_r1, -0.6283F, -0.3491F, 0);
		front_panel_4_r1.setTextureOffset(0, 228).addCuboid(-4.5F, -4.5F, 0, 9, 8, 0, 0, true);

		front_side_upper_2_r1 = new ModelPart(this);
		front_side_upper_2_r1.setPivot(21, -14, -24);
		side_1.addChild(front_side_upper_2_r1);
		setRotationAngle(front_side_upper_2_r1, 0, 0.1745F, -0.1396F);
		front_side_upper_2_r1.setTextureOffset(0, 37).addCuboid(0, -21, -12, 0, 21, 12, 0, true);

		front_side_lower_2_r1 = new ModelPart(this);
		front_side_lower_2_r1.setPivot(21, 0, -24);
		side_1.addChild(front_side_lower_2_r1);
		setRotationAngle(front_side_lower_2_r1, 0, 0.1745F, 0);
		front_side_lower_2_r1.setTextureOffset(0, 102).addCuboid(0, -14, -18, 0, 14, 18, 0, true);

		side_2 = new ModelPart(this);
		side_2.setPivot(21, 0, -9);
		front.addChild(side_2);


		front_side_bottom_2_r2 = new ModelPart(this);
		front_side_bottom_2_r2.setPivot(-39.3378F, 3.5923F, -26.3251F);
		side_2.addChild(front_side_bottom_2_r2);
		setRotationAngle(front_side_bottom_2_r2, 0, -0.1745F, -0.1745F);
		front_side_bottom_2_r2.setTextureOffset(112, 157).addCuboid(0, -4, -6.5F, 0, 8, 18, 0, false);

		front_middle_top_r2 = new ModelPart(this);
		front_middle_top_r2.setPivot(-20.3387F, -41.5702F, -2.9997F);
		side_2.addChild(front_middle_top_r2);
		setRotationAngle(front_middle_top_r2, 0, -0.3491F, 1.5708F);
		front_middle_top_r2.setTextureOffset(232, 118).addCuboid(0, 0, -14, 0, 6, 14, 0, false);

		outer_roof_8_r2 = new ModelPart(this);
		outer_roof_8_r2.setPivot(-37.824F, -34.5499F, -7.9825F);
		side_2.addChild(outer_roof_8_r2);
		setRotationAngle(outer_roof_8_r2, 0, -0.1745F, 1.0472F);
		outer_roof_8_r2.setTextureOffset(70, 66).addCuboid(-2.5F, -5, -3.5F, 0, 6, 9, 0, false);

		outer_roof_7_r2 = new ModelPart(this);
		outer_roof_7_r2.setPivot(-38.2991F, -37.6418F, 9);
		side_2.addChild(outer_roof_7_r2);
		setRotationAngle(outer_roof_7_r2, 0, 0, 0.5236F);
		outer_roof_7_r2.setTextureOffset(22, 25).addCuboid(0, -1, -19, 0, 2, 7, 0, false);

		outer_roof_6_r5 = new ModelPart(this);
		outer_roof_6_r5.setPivot(-32.7869F, -37.8295F, -10.0477F);
		side_2.addChild(outer_roof_6_r5);
		setRotationAngle(outer_roof_6_r5, 0, -0.3491F, 1.3963F);
		outer_roof_6_r5.setTextureOffset(72, 180).addCuboid(0, -7, -7.5F, 0, 14, 15, 0, false);

		front_panel_8_r2 = new ModelPart(this);
		front_panel_8_r2.setPivot(-29.9994F, 2.9884F, -37.7521F);
		side_2.addChild(front_panel_8_r2);
		setRotationAngle(front_panel_8_r2, 0, 0.5672F, 0);
		front_panel_8_r2.setTextureOffset(212, 217).addCuboid(-11, -5.9884F, -0.001F, 11, 8, 0, 0, false);

		front_panel_7_r2 = new ModelPart(this);
		front_panel_7_r2.setPivot(-33, -4, -33);
		side_2.addChild(front_panel_7_r2);
		setRotationAngle(front_panel_7_r2, -0.7418F, 0.3491F, 0);
		front_panel_7_r2.setTextureOffset(208, 42).addCuboid(-7, 0, 0, 11, 5, 0, 0, false);

		front_panel_6_r2 = new ModelPart(this);
		front_panel_6_r2.setPivot(-33, -4, -33);
		side_2.addChild(front_panel_6_r2);
		setRotationAngle(front_panel_6_r2, -0.3491F, 0.3491F, 0);
		front_panel_6_r2.setTextureOffset(162, 196).addCuboid(-9, -12, 0, 9, 12, 0, 0, false);

		front_panel_5_r2 = new ModelPart(this);
		front_panel_5_r2.setPivot(-33, -13.397F, -29.5798F);
		side_2.addChild(front_panel_5_r2);
		setRotationAngle(front_panel_5_r2, -0.4538F, 0.3491F, 0);
		front_panel_5_r2.setTextureOffset(112, 139).addCuboid(-10, -18, -0.001F, 10, 18, 0, 0, false);

		front_panel_4_r2 = new ModelPart(this);
		front_panel_4_r2.setPivot(-36.1229F, -32.2269F, -17.988F);
		side_2.addChild(front_panel_4_r2);
		setRotationAngle(front_panel_4_r2, -0.6283F, 0.3491F, 0);
		front_panel_4_r2.setTextureOffset(0, 228).addCuboid(-4.5F, -4.5F, 0, 9, 8, 0, 0, false);

		front_side_upper_2_r2 = new ModelPart(this);
		front_side_upper_2_r2.setPivot(-42, -14, -15);
		side_2.addChild(front_side_upper_2_r2);
		setRotationAngle(front_side_upper_2_r2, 0, -0.1745F, 0.1396F);
		front_side_upper_2_r2.setTextureOffset(0, 37).addCuboid(0, -21, -12, 0, 21, 12, 0, false);

		front_side_lower_2_r2 = new ModelPart(this);
		front_side_lower_2_r2.setPivot(-42, 0, -15);
		side_2.addChild(front_side_lower_2_r2);
		setRotationAngle(front_side_lower_2_r2, 0, -0.1745F, 0);
		front_side_lower_2_r2.setTextureOffset(0, 102).addCuboid(0, -14, -18, 0, 14, 18, 0, false);

		headlights = new ModelPart(this);
		headlights.setPivot(0, 24, 0);


		headlight_2b_r1 = new ModelPart(this);
		headlight_2b_r1.setPivot(0, -4, -42.1F);
		headlights.addChild(headlight_2b_r1);
		setRotationAngle(headlight_2b_r1, -0.3491F, 0, 0);
		headlight_2b_r1.setTextureOffset(20, 4).addCuboid(8, -4, 0, 4, 4, 0, 0, true);
		headlight_2b_r1.setTextureOffset(20, 4).addCuboid(-12, -4, 0, 4, 4, 0, 0, false);

		headlight_2a_r1 = new ModelPart(this);
		headlight_2a_r1.setPivot(12, -4, -42.1F);
		headlights.addChild(headlight_2a_r1);
		setRotationAngle(headlight_2a_r1, -0.3491F, -0.3491F, 0);
		headlight_2a_r1.setTextureOffset(20, 0).addCuboid(0, -4, 0, 6, 4, 0, 0, true);

		headlight_1a_r1 = new ModelPart(this);
		headlight_1a_r1.setPivot(-12, -4, -42.1F);
		headlights.addChild(headlight_1a_r1);
		setRotationAngle(headlight_1a_r1, -0.3491F, 0.3491F, 0);
		headlight_1a_r1.setTextureOffset(20, 0).addCuboid(-6, -4, 0, 6, 4, 0, 0, false);

		tail_lights = new ModelPart(this);
		tail_lights.setPivot(0, 24, 0);


		tail_light_2a_r1 = new ModelPart(this);
		tail_light_2a_r1.setPivot(12, -4, -42);
		tail_lights.addChild(tail_light_2a_r1);
		setRotationAngle(tail_light_2a_r1, -0.3491F, -0.3491F, 0);
		tail_light_2a_r1.setTextureOffset(20, 8).addCuboid(0, -4, -0.1F, 6, 4, 0, 0, true);

		tail_light_1a_r1 = new ModelPart(this);
		tail_light_1a_r1.setPivot(-12, -4, -42);
		tail_lights.addChild(tail_light_1a_r1);
		setRotationAngle(tail_light_1a_r1, -0.3491F, 0.3491F, 0);
		tail_light_1a_r1.setTextureOffset(20, 8).addCuboid(-6, -4, -0.1F, 6, 4, 0, 0, false);

		seat = new ModelPart(this);
		seat.setPivot(0, 24, 0);
		seat.setTextureOffset(16, 34).addCuboid(-3, -5, -3, 6, 1, 6, 0, false);
		seat.setTextureOffset(24, 52).addCuboid(-1.5F, -16.4F, 4.5F, 3, 2, 1, 0, false);

		top_right_r1 = new ModelPart(this);
		top_right_r1.setPivot(-1.5F, -16.4F, 4.5F);
		seat.addChild(top_right_r1);
		setRotationAngle(top_right_r1, 0.0017F, 0, 0.2618F);
		top_right_r1.setTextureOffset(32, 52).addCuboid(0, 0, 0, 1, 2, 1, 0, false);

		top_left_r1 = new ModelPart(this);
		top_left_r1.setPivot(1.5F, -16.4F, 4.5F);
		seat.addChild(top_left_r1);
		setRotationAngle(top_left_r1, 0.0017F, 0, -0.2618F);
		top_left_r1.setTextureOffset(32, 52).addCuboid(-1, 0, 0, 1, 2, 1, 0, true);

		back_right_r1 = new ModelPart(this);
		back_right_r1.setPivot(-1.5F, -5, 2);
		seat.addChild(back_right_r1);
		setRotationAngle(back_right_r1, -0.2618F, -0.1745F, 0.0873F);
		back_right_r1.setTextureOffset(32, 41).addCuboid(-1.5F, -9.75F, 0, 2, 10, 1, 0, false);

		back_left_r1 = new ModelPart(this);
		back_left_r1.setPivot(1.5F, -5, 2);
		seat.addChild(back_left_r1);
		setRotationAngle(back_left_r1, -0.2618F, 0.1745F, -0.0873F);
		back_left_r1.setTextureOffset(32, 41).addCuboid(-0.5F, -9.75F, 0, 2, 10, 1, 0, true);

		back_r1 = new ModelPart(this);
		back_r1.setPivot(3, -5, 2);
		seat.addChild(back_r1);
		setRotationAngle(back_r1, -0.2618F, 0, 0);
		back_r1.setTextureOffset(24, 41).addCuboid(-4.5F, -10, 0, 3, 10, 1, 0, true);

		door_light_on = new ModelPart(this);
		door_light_on.setPivot(0, 24, 0);


		light_r1 = new ModelPart(this);
		light_r1.setPivot(-21, -14, 0);
		door_light_on.addChild(light_r1);
		setRotationAngle(light_r1, 0, 0, 0.1396F);
		light_r1.setTextureOffset(20, 15).addCuboid(0, -21.5F, -0.5F, 0, 0, 1, 0.4F, false);

		door_light_off = new ModelPart(this);
		door_light_off.setPivot(0, 24, 0);


		light_r2 = new ModelPart(this);
		light_r2.setPivot(-21, -14, 0);
		door_light_off.addChild(light_r2);
		setRotationAngle(light_r2, 0, 0, 0.1396F);
		light_r2.setTextureOffset(23, 15).addCuboid(0, -21.5F, -0.5F, 0, 0, 1, 0.4F, false);
	}

	private static final int DOOR_MAX_TCL = 14;
	private static final int DOOR_MAX_AEL = 11;
	private static final ModelDoorOverlay MODEL_DOOR_OVERLAY = new ModelDoorOverlay(DOOR_MAX_TCL, 8, "door_overlay_a_train_tcl_left.png", "door_overlay_a_train_tcl_right.png");

	@Override
	protected void renderWindowPositions(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
		switch (renderStage) {
			case LIGHTS:
				renderMirror(isAel ? roof_light_window_ael : roof_light_tcl, matrices, vertices, light, position);
				break;
			case INTERIOR:
				renderMirror(isAel ? window_ael : window_tcl, matrices, vertices, light, position);
				if (renderDetails) {
					if (!isAel) {
						renderMirror(window_tcl_handrails, matrices, vertices, light, position);
						renderMirror(side_panel_tcl, matrices, vertices, light, position - 22F);
						renderMirror(side_panel_tcl, matrices, vertices, light, position + 22F);
					}
					renderMirror(isAel ? roof_window_ael : roof_window_tcl, matrices, vertices, light, position);
				}
				break;
			case INTERIOR_TRANSLUCENT:
				if (!isAel) {
					renderMirror(side_panel_tcl_translucent, matrices, vertices, light, position - 22F);
					renderMirror(side_panel_tcl_translucent, matrices, vertices, light, position + 22F);
				}
				break;
			case EXTERIOR:
				if (isAel) {
					final boolean isHeadWindows = isEnd1Head && (isIndex(0, position, getWindowPositions()) || isIndex(1, position, getWindowPositions()) || isIndex(2, position, getWindowPositions()));
					final boolean isEndWindows = isEnd2Head && (isIndex(-1, position, getWindowPositions()) || isIndex(-2, position, getWindowPositions()) || isIndex(-3, position, getWindowPositions()));
					if (!isHeadWindows && !isEndWindows) {
						renderMirror(window_exterior_ael, matrices, vertices, light, position);
					}
				} else {
					if (isIndex(0, position, getWindowPositions()) && isEnd1Head) {
						renderOnceFlipped(window_exterior_end_tcl, matrices, vertices, light, position);
					} else if (isIndex(-1, position, getWindowPositions()) && isEnd2Head) {
						renderOnce(window_exterior_end_tcl, matrices, vertices, light, position);
					} else {
						renderMirror(window_exterior_tcl, matrices, vertices, light, position);
					}
				}
				renderMirror(roof_exterior, matrices, vertices, light, position);
				break;
		}
	}

	@Override
	protected void renderDoorPositions(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
		final boolean middleDoor = isIndex(getDoorPositions().length / 2, position, getDoorPositions());
		final boolean doorOpen = doorLeftZ > 0 || doorRightZ > 0;
		final boolean notLastDoor = !isIndex(0, position, getDoorPositions()) && !isIndex(-1, position, getDoorPositions());

		switch (renderStage) {
			case LIGHTS:
				if (isAel) {
					renderMirror(roof_light_door_ael, matrices, vertices, light, position);
				} else if (notLastDoor) {
					renderMirror(roof_light_tcl, matrices, vertices, light, position);
				}
				if (middleDoor && doorOpen && renderDetails) {
					renderMirror(door_light_on, matrices, vertices, light, position - (isAel ? 80 : 40));
				}
				break;
			case INTERIOR:
				if (isAel) {
					door_left_ael.setPivot(doorRightX, 0, doorRightZ);
					door_right_ael.setPivot(doorRightX, 0, -doorRightZ);
					renderOnce(door_ael, matrices, vertices, light, position);
					door_left_ael.setPivot(doorLeftX, 0, doorLeftZ);
					door_right_ael.setPivot(doorLeftX, 0, -doorLeftZ);
					renderOnceFlipped(door_ael, matrices, vertices, light, position);

					if (renderDetails) {
						renderMirror(door_ael_handrail, matrices, vertices, light, position);
						renderMirror(roof_door_ael, matrices, vertices, light, position);
						renderMirror(luggage_rack, matrices, vertices, light, position - 20);
						renderMirror(luggage_rack, matrices, vertices, light, position + 20);
						renderMirror(side_panel_ael, matrices, vertices, light, position - 28.1F);
						renderMirror(side_panel_ael, matrices, vertices, light, position - 11.9F);
						renderMirror(side_panel_ael, matrices, vertices, light, position + 11.9F);
						renderMirror(side_panel_ael, matrices, vertices, light, position + 28.1F);

						for (int z = position + 40; z <= position + 74; z += 17) {
							renderOnce(seat, matrices, vertices, light, 15, z);
							renderOnce(seat, matrices, vertices, light, 8.5F, z);
							renderOnce(seat, matrices, vertices, light, -8.5F, z);
							renderOnce(seat, matrices, vertices, light, -15, z);
						}
						for (int z = position - 74; z <= position - 40; z += 17) {
							renderOnceFlipped(seat, matrices, vertices, light, 15, z);
							renderOnceFlipped(seat, matrices, vertices, light, 8.5F, z);
							renderOnceFlipped(seat, matrices, vertices, light, -8.5F, z);
							renderOnceFlipped(seat, matrices, vertices, light, -15, z);
						}
					}
				} else {
					door_left_tcl.setPivot(doorRightX, 0, doorRightZ);
					door_right_tcl.setPivot(doorRightX, 0, -doorRightZ);
					renderOnce(door_tcl, matrices, vertices, light, position);
					door_left_tcl.setPivot(doorLeftX, 0, doorLeftZ);
					door_right_tcl.setPivot(doorLeftX, 0, -doorLeftZ);
					renderOnceFlipped(door_tcl, matrices, vertices, light, position);

					if (renderDetails) {
						renderOnce(door_tcl_handrail, matrices, vertices, light, position);
						if (notLastDoor) {
							renderMirror(roof_door_tcl, matrices, vertices, light, position);
						}
					}
				}
				break;
			case INTERIOR_TRANSLUCENT:
				if (isAel) {
					renderMirror(side_panel_ael_translucent, matrices, vertices, light, position - 28.1F);
					renderMirror(side_panel_ael_translucent, matrices, vertices, light, position - 11.9F);
					renderMirror(side_panel_ael_translucent, matrices, vertices, light, position + 11.9F);
					renderMirror(side_panel_ael_translucent, matrices, vertices, light, position + 28.1F);
				}
				break;
			case EXTERIOR:
				if (isAel) {
					door_left_exterior_ael.setPivot(doorRightX, 0, doorRightZ);
					door_right_exterior_ael.setPivot(doorRightX, 0, -doorRightZ);
					renderOnce(door_exterior_ael, matrices, vertices, light, position);
					door_left_exterior_ael.setPivot(doorLeftX, 0, doorLeftZ);
					door_right_exterior_ael.setPivot(doorLeftX, 0, -doorLeftZ);
					renderOnceFlipped(door_exterior_ael, matrices, vertices, light, position);
					renderMirror(roof_exterior, matrices, vertices, light, position - 2);
					renderMirror(roof_exterior, matrices, vertices, light, position + 2);
				} else {
					final boolean door1End = isIndex(0, position, getDoorPositions()) && isEnd1Head;
					final boolean door2End = isIndex(-1, position, getDoorPositions()) && isEnd2Head;

					if (door1End || door2End) {
						door_left_exterior_end.setPivot(doorRightX, 0, doorRightZ);
						door_right_exterior_end.setPivot(doorRightX, 0, -doorRightZ);
						renderOnce(door_exterior_end, matrices, vertices, light, position);
					} else {
						door_left_exterior_tcl.setPivot(doorRightX, 0, doorRightZ);
						door_right_exterior_tcl.setPivot(doorRightX, 0, -doorRightZ);
						renderOnce(door_exterior_tcl, matrices, vertices, light, position);
					}

					if (door1End || door2End) {
						door_left_exterior_end.setPivot(doorLeftX, 0, doorLeftZ);
						door_right_exterior_end.setPivot(doorLeftX, 0, -doorLeftZ);
						renderOnceFlipped(door_exterior_end, matrices, vertices, light, position);
					} else {
						door_left_exterior_tcl.setPivot(doorLeftX, 0, doorLeftZ);
						door_right_exterior_tcl.setPivot(doorLeftX, 0, -doorLeftZ);
						renderOnceFlipped(door_exterior_tcl, matrices, vertices, light, position);
					}
					renderMirror(roof_exterior, matrices, vertices, light, position);
				}
				if (middleDoor && !doorOpen && renderDetails) {
					renderMirror(door_light_off, matrices, vertices, light, position - (isAel ? 80 : 40));
				}
				break;
		}
	}

	@Override
	protected void renderHeadPosition1(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean useHeadlights) {
		switch (renderStage) {
			case LIGHTS:
				if (!isAel) {
					renderOnce(roof_end_light, matrices, vertices, light, position);
				}
				break;
			case ALWAYS_ON_LIGHTS:
				renderOnce(useHeadlights ? headlights : tail_lights, matrices, vertices, light, position);
				break;
			case INTERIOR:
				renderOnce(isAel ? head_ael : head_tcl, matrices, vertices, light, position);
				if (renderDetails) {
					if (isAel) {
						renderOnceFlipped(seat, matrices, vertices, light, 15, position + 13);
						renderOnceFlipped(seat, matrices, vertices, light, 8.5F, position + 13);
						renderOnceFlipped(seat, matrices, vertices, light, -15, position + 13);
						renderOnceFlipped(seat, matrices, vertices, light, -8.5F, position + 13);
					} else {
						renderOnce(roof_end, matrices, vertices, light, position);
					}
				}
				break;
			case EXTERIOR:
				renderOnce(head_exterior, matrices, vertices, light, position);
				renderOnce(roof_end_exterior, matrices, vertices, light, position);
				if (isAel) {
					renderOnce(window_exterior_end_ael, matrices, vertices, light, position);
					renderMirror(roof_exterior, matrices, vertices, light, position + 30);
					renderMirror(roof_exterior, matrices, vertices, light, position + 70);
				}
				break;
		}
	}

	@Override
	protected void renderHeadPosition2(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean useHeadlights) {
		switch (renderStage) {
			case LIGHTS:
				if (!isAel) {
					renderOnceFlipped(roof_end_light, matrices, vertices, light, position);
				}
				break;
			case ALWAYS_ON_LIGHTS:
				renderOnceFlipped(useHeadlights ? headlights : tail_lights, matrices, vertices, light, position);
				break;
			case INTERIOR:
				renderOnceFlipped(isAel ? head_ael : head_tcl, matrices, vertices, light, position);
				if (renderDetails) {
					if (isAel) {
						renderOnce(seat, matrices, vertices, light, 15, position - 13);
						renderOnce(seat, matrices, vertices, light, 8.5F, position - 13);
						renderOnce(seat, matrices, vertices, light, -15, position - 13);
						renderOnce(seat, matrices, vertices, light, -8.5F, position - 13);
					} else {
						renderOnceFlipped(roof_end, matrices, vertices, light, position);
					}
				}
				break;
			case EXTERIOR:
				renderOnceFlipped(head_exterior, matrices, vertices, light, position);
				renderOnceFlipped(roof_end_exterior, matrices, vertices, light, position);
				if (isAel) {
					renderOnceFlipped(window_exterior_end_ael, matrices, vertices, light, position);
					renderMirror(roof_exterior, matrices, vertices, light, position - 30);
					renderMirror(roof_exterior, matrices, vertices, light, position - 70);
				}
				break;
		}
	}

	@Override
	protected void renderEndPosition1(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
		switch (renderStage) {
			case LIGHTS:
				if (!isAel) {
					renderOnce(roof_end_light, matrices, vertices, light, position);
				}
				break;
			case INTERIOR:
				if (isAel) {
					renderOnce(end_ael, matrices, vertices, light, position);
					if (renderDetails) {
						renderOnce(end_door_ael, matrices, vertices, light, position);
						renderOnceFlipped(seat, matrices, vertices, light, 15, position + 13);
						renderOnceFlipped(seat, matrices, vertices, light, 8.5F, position + 13);
						renderOnceFlipped(seat, matrices, vertices, light, -15, position + 13);
						renderOnceFlipped(seat, matrices, vertices, light, -8.5F, position + 13);
					}
				} else {
					renderOnce(end_tcl, matrices, vertices, light, position);
					if (renderDetails) {
						renderOnce(roof_end, matrices, vertices, light, position);
					}
				}
				break;
			case EXTERIOR:
				renderOnce(isAel ? end_exterior_ael : end_exterior_tcl, matrices, vertices, light, position);
				renderOnce(roof_end_exterior, matrices, vertices, light, position);
				break;
		}
	}

	@Override
	protected void renderEndPosition2(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
		switch (renderStage) {
			case LIGHTS:
				if (!isAel) {
					renderOnceFlipped(roof_end_light, matrices, vertices, light, position);
				}
				break;
			case INTERIOR:
				if (isAel) {
					renderOnceFlipped(end_ael, matrices, vertices, light, position);
					if (renderDetails) {
						renderOnceFlipped(end_door_ael, matrices, vertices, light, position);
						renderOnce(seat, matrices, vertices, light, 15, position - 13);
						renderOnce(seat, matrices, vertices, light, 8.5F, position - 13);
						renderOnce(seat, matrices, vertices, light, -15, position - 13);
						renderOnce(seat, matrices, vertices, light, -8.5F, position - 13);
					}
				} else {
					renderOnceFlipped(end_tcl, matrices, vertices, light, position);
					if (renderDetails) {
						renderOnceFlipped(roof_end, matrices, vertices, light, position);
					}
				}
				break;
			case EXTERIOR:
				renderOnceFlipped(isAel ? end_exterior_ael : end_exterior_tcl, matrices, vertices, light, position);
				renderOnceFlipped(roof_end_exterior, matrices, vertices, light, position);
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
	protected float getDoorAnimationX(float value, boolean opening) {
		return value < 0.05 ? -value * 20 - 0.01F : -1.01F;
	}

	@Override
	protected float getDoorAnimationZ(float value, boolean opening) {
		final int doorMax = isAel ? DOOR_MAX_AEL : DOOR_MAX_TCL;
		final float time = 0.5F * (isAel ? (float) DOOR_MAX_AEL / DOOR_MAX_TCL : 1);
		return smoothEnds(-doorMax, doorMax, -time, time, value);
	}
}