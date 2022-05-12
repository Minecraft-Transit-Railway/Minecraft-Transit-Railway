package mtr.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mtr.mappings.ModelDataWrapper;
import mtr.mappings.ModelMapper;

public class ModelATrain extends ModelSimpleTrainBase {

	private final ModelMapper window_tcl;
	private final ModelMapper upper_wall_r1;
	private final ModelMapper window_tcl_handrails;
	private final ModelMapper handrail_8_r1;
	private final ModelMapper handrail_3_r1;
	private final ModelMapper top_handrail_3_r1;
	private final ModelMapper seat_back_r1;
	private final ModelMapper bench;
	private final ModelMapper window_ael;
	private final ModelMapper upper_wall_r2;
	private final ModelMapper window_exterior_tcl;
	private final ModelMapper upper_wall_r3;
	private final ModelMapper floor_r1;
	private final ModelMapper window_exterior_ael;
	private final ModelMapper upper_wall_r4;
	private final ModelMapper floor_r2;
	private final ModelMapper window_exterior_end_tcl;
	private final ModelMapper upper_wall_2_r1;
	private final ModelMapper floor_2_r1;
	private final ModelMapper upper_wall_1_r1;
	private final ModelMapper floor_1_r1;
	private final ModelMapper window_exterior_end_ael;
	private final ModelMapper upper_wall_6_r1;
	private final ModelMapper floor_6_r1;
	private final ModelMapper upper_wall_5_r1;
	private final ModelMapper floor_5_r1;
	private final ModelMapper side_panel_tcl;
	private final ModelMapper side_panel_tcl_translucent;
	private final ModelMapper side_panel_ael;
	private final ModelMapper side_panel_ael_translucent;
	private final ModelMapper roof_window_tcl;
	private final ModelMapper inner_roof_4_r1;
	private final ModelMapper inner_roof_3_r1;
	private final ModelMapper inner_roof_2_r1;
	private final ModelMapper roof_window_ael;
	private final ModelMapper inner_roof_5_r1;
	private final ModelMapper inner_roof_4_r2;
	private final ModelMapper inner_roof_3_r2;
	private final ModelMapper roof_door_tcl;
	private final ModelMapper inner_roof_4_r3;
	private final ModelMapper inner_roof_3_r3;
	private final ModelMapper inner_roof_2_r2;
	private final ModelMapper handrail_2_r1;
	private final ModelMapper roof_door_ael;
	private final ModelMapper display_main_r1;
	private final ModelMapper display_6_r1;
	private final ModelMapper display_5_r1;
	private final ModelMapper display_4_r1;
	private final ModelMapper display_3_r1;
	private final ModelMapper display_2_r1;
	private final ModelMapper display_1_r1;
	private final ModelMapper inner_roof_4_r4;
	private final ModelMapper inner_roof_3_r4;
	private final ModelMapper inner_roof_2_r3;
	private final ModelMapper roof_exterior;
	private final ModelMapper outer_roof_6_r1;
	private final ModelMapper outer_roof_5_r1;
	private final ModelMapper outer_roof_4_r1;
	private final ModelMapper outer_roof_3_r1;
	private final ModelMapper door_tcl;
	private final ModelMapper door_left_tcl;
	private final ModelMapper door_left_top_r1;
	private final ModelMapper door_right_tcl;
	private final ModelMapper door_right_top_r1;
	private final ModelMapper door_tcl_handrail;
	private final ModelMapper door_ael;
	private final ModelMapper door_left_ael;
	private final ModelMapper door_left_top_r2;
	private final ModelMapper door_right_ael;
	private final ModelMapper door_right_top_r2;
	private final ModelMapper door_ael_handrail;
	private final ModelMapper upper_wall_right_r1;
	private final ModelMapper lower_wall_right_r1;
	private final ModelMapper upper_wall_left_r1;
	private final ModelMapper lower_wall_left_r1;
	private final ModelMapper handrail_left_r1;
	private final ModelMapper door_exterior_tcl;
	private final ModelMapper upper_wall_r5;
	private final ModelMapper floor_r3;
	private final ModelMapper door_left_exterior_tcl;
	private final ModelMapper door_left_top_r3;
	private final ModelMapper door_left_base_r1;
	private final ModelMapper door_right_exterior_tcl;
	private final ModelMapper door_right_top_r3;
	private final ModelMapper door_right_base_r1;
	private final ModelMapper door_exterior_ael;
	private final ModelMapper upper_wall_2_r2;
	private final ModelMapper floor_2_r2;
	private final ModelMapper lower_wall_1_r1;
	private final ModelMapper upper_wall_1_r2;
	private final ModelMapper floor_1_r2;
	private final ModelMapper door_left_exterior_ael;
	private final ModelMapper door_left_top_r4;
	private final ModelMapper door_left_base_r2;
	private final ModelMapper door_right_exterior_ael;
	private final ModelMapper door_right_top_r4;
	private final ModelMapper door_right_base_r2;
	private final ModelMapper door_exterior_end;
	private final ModelMapper upper_wall_r6;
	private final ModelMapper floor_r4;
	private final ModelMapper door_left_exterior_end;
	private final ModelMapper door_left_top_r5;
	private final ModelMapper door_left_base_r3;
	private final ModelMapper door_right_exterior_end;
	private final ModelMapper door_right_top_r5;
	private final ModelMapper door_right_base_r3;
	private final ModelMapper luggage_rack;
	private final ModelMapper top_3_r1;
	private final ModelMapper top_2_r1;
	private final ModelMapper top_1_r1;
	private final ModelMapper upper_wall_r7;
	private final ModelMapper end_tcl;
	private final ModelMapper upper_wall_2_r3;
	private final ModelMapper upper_wall_1_r3;
	private final ModelMapper end_ael;
	private final ModelMapper end_exterior_tcl;
	private final ModelMapper upper_wall_2_r4;
	private final ModelMapper upper_wall_1_r4;
	private final ModelMapper floor_2_r3;
	private final ModelMapper floor_1_r3;
	private final ModelMapper end_exterior_ael;
	private final ModelMapper upper_wall_3_r1;
	private final ModelMapper upper_wall_2_r5;
	private final ModelMapper floor_3_r1;
	private final ModelMapper floor_2_r4;
	private final ModelMapper end_door_ael;
	private final ModelMapper roof_end;
	private final ModelMapper handrail_2_r2;
	private final ModelMapper inner_roof_1;
	private final ModelMapper inner_roof_4_r5;
	private final ModelMapper inner_roof_3_r5;
	private final ModelMapper inner_roof_2_r4;
	private final ModelMapper inner_roof_2;
	private final ModelMapper inner_roof_4_r6;
	private final ModelMapper inner_roof_3_r6;
	private final ModelMapper inner_roof_2_r5;
	private final ModelMapper roof_end_exterior;
	private final ModelMapper vent_2_r1;
	private final ModelMapper vent_1_r1;
	private final ModelMapper outer_roof_1;
	private final ModelMapper outer_roof_6_r2;
	private final ModelMapper outer_roof_5_r2;
	private final ModelMapper outer_roof_4_r2;
	private final ModelMapper outer_roof_3_r2;
	private final ModelMapper outer_roof_2;
	private final ModelMapper outer_roof_5_r3;
	private final ModelMapper outer_roof_4_r3;
	private final ModelMapper outer_roof_3_r3;
	private final ModelMapper outer_roof_6_r3;
	private final ModelMapper roof_light_tcl;
	private final ModelMapper roof_light_r1;
	private final ModelMapper roof_light_door_ael;
	private final ModelMapper roof_light_window_ael;
	private final ModelMapper inner_roof_3_r7;
	private final ModelMapper roof_end_light;
	private final ModelMapper roof_light_2_r1;
	private final ModelMapper roof_light_1_r1;
	private final ModelMapper head_tcl;
	private final ModelMapper upper_wall_2_r6;
	private final ModelMapper upper_wall_1_r5;
	private final ModelMapper head_ael;
	private final ModelMapper head_exterior;
	private final ModelMapper upper_wall_2_r7;
	private final ModelMapper upper_wall_1_r6;
	private final ModelMapper floor_2_r5;
	private final ModelMapper floor_1_r4;
	private final ModelMapper front;
	private final ModelMapper front_bottom_5_r1;
	private final ModelMapper front_bottom_4_r1;
	private final ModelMapper front_bottom_2_r1;
	private final ModelMapper front_bottom_1_r1;
	private final ModelMapper front_panel_3_r1;
	private final ModelMapper front_panel_2_r1;
	private final ModelMapper front_panel_1_r1;
	private final ModelMapper side_1;
	private final ModelMapper front_side_bottom_2_r1;
	private final ModelMapper front_middle_top_r1;
	private final ModelMapper outer_roof_8_r1;
	private final ModelMapper outer_roof_7_r1;
	private final ModelMapper outer_roof_6_r4;
	private final ModelMapper front_panel_8_r1;
	private final ModelMapper front_panel_7_r1;
	private final ModelMapper front_panel_6_r1;
	private final ModelMapper front_panel_5_r1;
	private final ModelMapper front_panel_4_r1;
	private final ModelMapper front_side_upper_2_r1;
	private final ModelMapper front_side_lower_2_r1;
	private final ModelMapper side_2;
	private final ModelMapper front_side_bottom_2_r2;
	private final ModelMapper front_middle_top_r2;
	private final ModelMapper outer_roof_8_r2;
	private final ModelMapper outer_roof_7_r2;
	private final ModelMapper outer_roof_6_r5;
	private final ModelMapper front_panel_8_r2;
	private final ModelMapper front_panel_7_r2;
	private final ModelMapper front_panel_6_r2;
	private final ModelMapper front_panel_5_r2;
	private final ModelMapper front_panel_4_r2;
	private final ModelMapper front_side_upper_2_r2;
	private final ModelMapper front_side_lower_2_r2;
	private final ModelMapper headlights;
	private final ModelMapper headlight_2b_r1;
	private final ModelMapper headlight_2a_r1;
	private final ModelMapper headlight_1a_r1;
	private final ModelMapper tail_lights;
	private final ModelMapper tail_light_2a_r1;
	private final ModelMapper tail_light_1a_r1;
	private final ModelMapper seat;
	private final ModelMapper top_right_r1;
	private final ModelMapper top_left_r1;
	private final ModelMapper back_right_r1;
	private final ModelMapper back_left_r1;
	private final ModelMapper back_r1;
	private final ModelMapper door_light_on;
	private final ModelMapper light_r1;
	private final ModelMapper door_light_off;
	private final ModelMapper light_r2;

	protected final boolean isAel;

	public ModelATrain(boolean isAel) {
		this.isAel = isAel;

		final int textureWidth = 336;
		final int textureHeight = 336;

		final ModelDataWrapper modelDataWrapper = new ModelDataWrapper(this, textureWidth, textureHeight);

		window_tcl = new ModelMapper(modelDataWrapper);
		window_tcl.setPos(0, 24, 0);
		window_tcl.texOffs(0, 0).addBox(-20, 0, -24, 20, 1, 48, 0, false);
		window_tcl.texOffs(0, 120).addBox(-21, -14, -26, 3, 14, 52, 0, false);

		upper_wall_r1 = new ModelMapper(modelDataWrapper);
		upper_wall_r1.setPos(-21, -14, 0);
		window_tcl.addChild(upper_wall_r1);
		setRotationAngle(upper_wall_r1, 0, 0, 0.1396F);
		upper_wall_r1.texOffs(0, 49).addBox(0, -19, -26, 3, 19, 52, 0, false);

		window_tcl_handrails = new ModelMapper(modelDataWrapper);
		window_tcl_handrails.setPos(0, 24, 0);
		window_tcl_handrails.texOffs(142, 76).addBox(-18, -6, -22, 7, 1, 44, 0, false);
		window_tcl_handrails.texOffs(180, 121).addBox(-18, -5, -21, 5, 5, 42, 0, false);
		window_tcl_handrails.texOffs(4, 0).addBox(0, -35, -22, 0, 35, 0, 0.2F, false);
		window_tcl_handrails.texOffs(4, 0).addBox(0, -35, 22, 0, 35, 0, 0.2F, false);
		window_tcl_handrails.texOffs(42, 40).addBox(-1, -32, -5.5F, 2, 4, 0, 0, false);
		window_tcl_handrails.texOffs(42, 40).addBox(-1, -32, -16.5F, 2, 4, 0, 0, false);
		window_tcl_handrails.texOffs(42, 40).addBox(-1, -32, 5.5F, 2, 4, 0, 0, false);
		window_tcl_handrails.texOffs(42, 40).addBox(-1, -32, 16.5F, 2, 4, 0, 0, false);
		window_tcl_handrails.texOffs(4, 0).addBox(0, -35, 0, 0, 35, 0, 0.2F, false);

		handrail_8_r1 = new ModelMapper(modelDataWrapper);
		handrail_8_r1.setPos(0, 0, 0);
		window_tcl_handrails.addChild(handrail_8_r1);
		setRotationAngle(handrail_8_r1, -1.5708F, 0, 0);
		handrail_8_r1.texOffs(0, 0).addBox(0, -24, -31.5F, 0, 48, 0, 0.2F, false);

		handrail_3_r1 = new ModelMapper(modelDataWrapper);
		handrail_3_r1.setPos(-11, -5, 0);
		window_tcl_handrails.addChild(handrail_3_r1);
		setRotationAngle(handrail_3_r1, 0, 0, -0.0698F);
		handrail_3_r1.texOffs(8, 0).addBox(0, -27.2F, 22, 0, 27, 0, 0.2F, false);
		handrail_3_r1.texOffs(0, 0).addBox(0, -27.2F, 0, 0, 4, 0, 0.2F, false);
		handrail_3_r1.texOffs(8, 0).addBox(0, -27.2F, -22, 0, 27, 0, 0.2F, false);

		top_handrail_3_r1 = new ModelMapper(modelDataWrapper);
		top_handrail_3_r1.setPos(-11, -5, 0);
		window_tcl_handrails.addChild(top_handrail_3_r1);
		setRotationAngle(top_handrail_3_r1, -1.5708F, 0, -0.0698F);
		top_handrail_3_r1.texOffs(0, 0).addBox(0, -22, -23, 0, 44, 0, 0.2F, false);

		seat_back_r1 = new ModelMapper(modelDataWrapper);
		seat_back_r1.setPos(-17, -6, 0);
		window_tcl_handrails.addChild(seat_back_r1);
		setRotationAngle(seat_back_r1, 0, 0, -0.0524F);
		seat_back_r1.texOffs(116, 175).addBox(-1, -8, -22, 1, 8, 44, 0, false);

		bench = new ModelMapper(modelDataWrapper);
		bench.setPos(0, 0, 0);
		window_tcl_handrails.addChild(bench);


		window_ael = new ModelMapper(modelDataWrapper);
		window_ael.setPos(0, 24, 0);
		window_ael.texOffs(22, 22).addBox(-20, 0, -13, 20, 1, 26, 0, false);
		window_ael.texOffs(28, 146).addBox(-21, -14, -13, 3, 14, 26, 0, false);

		upper_wall_r2 = new ModelMapper(modelDataWrapper);
		upper_wall_r2.setPos(-21, -14, 0);
		window_ael.addChild(upper_wall_r2);
		setRotationAngle(upper_wall_r2, 0, 0, 0.1396F);
		upper_wall_r2.texOffs(26, 75).addBox(0, -19, -13, 3, 19, 26, 0, false);

		window_exterior_tcl = new ModelMapper(modelDataWrapper);
		window_exterior_tcl.setPos(0, 24, 0);
		window_exterior_tcl.texOffs(58, 73).addBox(-21, -14, -26, 0, 14, 52, 0, false);

		upper_wall_r3 = new ModelMapper(modelDataWrapper);
		upper_wall_r3.setPos(-21, -14, 0);
		window_exterior_tcl.addChild(upper_wall_r3);
		setRotationAngle(upper_wall_r3, 0, 0, 0.1396F);
		upper_wall_r3.texOffs(58, 0).addBox(0, -23, -26, 0, 23, 52, 0, false);

		floor_r1 = new ModelMapper(modelDataWrapper);
		floor_r1.setPos(-21, 0, 0);
		window_exterior_tcl.addChild(floor_r1);
		setRotationAngle(floor_r1, 0, 0, -0.1745F);
		floor_r1.texOffs(62, 139).addBox(0, 0, -24, 1, 8, 48, 0, false);

		window_exterior_ael = new ModelMapper(modelDataWrapper);
		window_exterior_ael.setPos(0, 24, 0);
		window_exterior_ael.texOffs(58, 99).addBox(-21, -14, -13, 0, 14, 26, 0, false);

		upper_wall_r4 = new ModelMapper(modelDataWrapper);
		upper_wall_r4.setPos(-21, -14, 0);
		window_exterior_ael.addChild(upper_wall_r4);
		setRotationAngle(upper_wall_r4, 0, 0, 0.1396F);
		upper_wall_r4.texOffs(58, 26).addBox(0, -23, -13, 0, 23, 26, 0, false);

		floor_r2 = new ModelMapper(modelDataWrapper);
		floor_r2.setPos(-21, 0, 0);
		window_exterior_ael.addChild(floor_r2);
		setRotationAngle(floor_r2, 0, 0, -0.1745F);
		floor_r2.texOffs(84, 161).addBox(0, 0, -13, 1, 8, 26, 0, false);

		window_exterior_end_tcl = new ModelMapper(modelDataWrapper);
		window_exterior_end_tcl.setPos(0, 24, 0);
		window_exterior_end_tcl.texOffs(212, 144).addBox(21, -14, -26, 0, 14, 52, 0, true);
		window_exterior_end_tcl.texOffs(212, 144).addBox(-21, -14, -26, 0, 14, 52, 0, false);

		upper_wall_2_r1 = new ModelMapper(modelDataWrapper);
		upper_wall_2_r1.setPos(-21, -14, 0);
		window_exterior_end_tcl.addChild(upper_wall_2_r1);
		setRotationAngle(upper_wall_2_r1, 0, 0, 0.1396F);
		upper_wall_2_r1.texOffs(58, 0).addBox(0, -23, -26, 0, 23, 52, 0, false);

		floor_2_r1 = new ModelMapper(modelDataWrapper);
		floor_2_r1.setPos(-21, 0, 0);
		window_exterior_end_tcl.addChild(floor_2_r1);
		setRotationAngle(floor_2_r1, 0, 0, -0.1745F);
		floor_2_r1.texOffs(8, 272).addBox(0, 0, -24, 1, 8, 48, 0, false);

		upper_wall_1_r1 = new ModelMapper(modelDataWrapper);
		upper_wall_1_r1.setPos(21, -14, 0);
		window_exterior_end_tcl.addChild(upper_wall_1_r1);
		setRotationAngle(upper_wall_1_r1, 0, 0, -0.1396F);
		upper_wall_1_r1.texOffs(58, 0).addBox(0, -23, -26, 0, 23, 52, 0, true);

		floor_1_r1 = new ModelMapper(modelDataWrapper);
		floor_1_r1.setPos(21, 0, 0);
		window_exterior_end_tcl.addChild(floor_1_r1);
		setRotationAngle(floor_1_r1, 0, 0, 0.1745F);
		floor_1_r1.texOffs(8, 272).addBox(-1, 0, -24, 1, 8, 48, 0, true);

		window_exterior_end_ael = new ModelMapper(modelDataWrapper);
		window_exterior_end_ael.setPos(0, 24, 0);
		window_exterior_end_ael.texOffs(0, 210).addBox(21, -14, -2, 0, 14, 26, 0, true);
		window_exterior_end_ael.texOffs(0, 210).addBox(-21, -14, -2, 0, 14, 26, 0, false);
		window_exterior_end_ael.texOffs(0, 224).addBox(21, -14, 24, 0, 14, 26, 0, true);
		window_exterior_end_ael.texOffs(0, 224).addBox(-21, -14, 24, 0, 14, 26, 0, false);
		window_exterior_end_ael.texOffs(0, 238).addBox(21, -14, 50, 0, 14, 26, 0, true);
		window_exterior_end_ael.texOffs(0, 238).addBox(-21, -14, 50, 0, 14, 26, 0, false);

		upper_wall_6_r1 = new ModelMapper(modelDataWrapper);
		upper_wall_6_r1.setPos(-21, -14, 0);
		window_exterior_end_ael.addChild(upper_wall_6_r1);
		setRotationAngle(upper_wall_6_r1, 0, 0, 0.1396F);
		upper_wall_6_r1.texOffs(58, 26).addBox(0, -23, 50, 0, 23, 26, 0, false);
		upper_wall_6_r1.texOffs(58, 26).addBox(0, -23, 24, 0, 23, 26, 0, false);
		upper_wall_6_r1.texOffs(58, 26).addBox(0, -23, -2, 0, 23, 26, 0, false);

		floor_6_r1 = new ModelMapper(modelDataWrapper);
		floor_6_r1.setPos(-21, 0, 0);
		window_exterior_end_ael.addChild(floor_6_r1);
		setRotationAngle(floor_6_r1, 0, 0, -0.1745F);
		floor_6_r1.texOffs(181, 264).addBox(0, 0, 50, 1, 8, 26, 0, false);
		floor_6_r1.texOffs(181, 256).addBox(0, 0, 24, 1, 8, 26, 0, false);
		floor_6_r1.texOffs(181, 248).addBox(0, 0, -2, 1, 8, 26, 0, false);

		upper_wall_5_r1 = new ModelMapper(modelDataWrapper);
		upper_wall_5_r1.setPos(21, -14, 0);
		window_exterior_end_ael.addChild(upper_wall_5_r1);
		setRotationAngle(upper_wall_5_r1, 0, 0, -0.1396F);
		upper_wall_5_r1.texOffs(58, 26).addBox(0, -23, 50, 0, 23, 26, 0, true);
		upper_wall_5_r1.texOffs(58, 26).addBox(0, -23, 24, 0, 23, 26, 0, true);
		upper_wall_5_r1.texOffs(58, 26).addBox(0, -23, -2, 0, 23, 26, 0, true);

		floor_5_r1 = new ModelMapper(modelDataWrapper);
		floor_5_r1.setPos(21, 0, 0);
		window_exterior_end_ael.addChild(floor_5_r1);
		setRotationAngle(floor_5_r1, 0, 0, 0.1745F);
		floor_5_r1.texOffs(181, 264).addBox(-1, 0, 50, 1, 8, 26, 0, true);
		floor_5_r1.texOffs(181, 256).addBox(-1, 0, 24, 1, 8, 26, 0, true);
		floor_5_r1.texOffs(181, 248).addBox(-1, 0, -2, 1, 8, 26, 0, true);

		side_panel_tcl = new ModelMapper(modelDataWrapper);
		side_panel_tcl.setPos(0, 24, 0);
		side_panel_tcl.texOffs(90, 139).addBox(-18, -35, 0, 7, 30, 0, 0, false);

		side_panel_tcl_translucent = new ModelMapper(modelDataWrapper);
		side_panel_tcl_translucent.setPos(0, 24, 0);
		side_panel_tcl_translucent.texOffs(76, 139).addBox(-18, -35, 0, 7, 30, 0, 0, false);

		side_panel_ael = new ModelMapper(modelDataWrapper);
		side_panel_ael.setPos(0, 24, 0);
		side_panel_ael.texOffs(26, 281).addBox(-18, -34, 0, 12, 34, 0, 0, false);

		side_panel_ael_translucent = new ModelMapper(modelDataWrapper);
		side_panel_ael_translucent.setPos(0, 24, 0);
		side_panel_ael_translucent.texOffs(294, 108).addBox(-18, -34, 0, 12, 34, 0, 0, false);

		roof_window_tcl = new ModelMapper(modelDataWrapper);
		roof_window_tcl.setPos(0, 24, 0);
		roof_window_tcl.texOffs(62, 0).addBox(-16, -32, -24, 3, 0, 48, 0, false);
		roof_window_tcl.texOffs(52, 0).addBox(-5, -34.5F, -24, 5, 0, 48, 0, false);

		inner_roof_4_r1 = new ModelMapper(modelDataWrapper);
		inner_roof_4_r1.setPos(-3.5384F, -34.6286F, 0);
		roof_window_tcl.addChild(inner_roof_4_r1);
		setRotationAngle(inner_roof_4_r1, 0, 0, -0.1396F);
		inner_roof_4_r1.texOffs(40, 0).addBox(-6, 0, -24, 6, 0, 48, 0, false);

		inner_roof_3_r1 = new ModelMapper(modelDataWrapper);
		inner_roof_3_r1.setPos(-10.4309F, -33.4846F, 0);
		roof_window_tcl.addChild(inner_roof_3_r1);
		setRotationAngle(inner_roof_3_r1, 0, 0, -0.3142F);
		inner_roof_3_r1.texOffs(0, 49).addBox(-1, 0, -24, 2, 0, 48, 0, false);

		inner_roof_2_r1 = new ModelMapper(modelDataWrapper);
		inner_roof_2_r1.setPos(-13, -32, 0);
		roof_window_tcl.addChild(inner_roof_2_r1);
		setRotationAngle(inner_roof_2_r1, 0, 0, -0.6283F);
		inner_roof_2_r1.texOffs(68, 0).addBox(0, 0, -24, 2, 0, 48, 0, false);

		roof_window_ael = new ModelMapper(modelDataWrapper);
		roof_window_ael.setPos(0, 24, 0);
		roof_window_ael.texOffs(4, 134).addBox(-16, -32.3F, -13, 2, 0, 26, 0, false);
		roof_window_ael.texOffs(12, 120).addBox(-4, -35.6679F, -13, 4, 0, 26, 0, false);

		inner_roof_5_r1 = new ModelMapper(modelDataWrapper);
		inner_roof_5_r1.setPos(-5.1202F, -33.4588F, 0);
		roof_window_ael.addChild(inner_roof_5_r1);
		setRotationAngle(inner_roof_5_r1, 0, 0, -0.1047F);
		inner_roof_5_r1.texOffs(11, 120).addBox(-2.8798F, -2, -13, 5, 0, 26, 0, false);

		inner_roof_4_r2 = new ModelMapper(modelDataWrapper);
		inner_roof_4_r2.setPos(-9.0655F, -32.834F, 0);
		roof_window_ael.addChild(inner_roof_4_r2);
		setRotationAngle(inner_roof_4_r2, 0, 0, -0.2094F);
		inner_roof_4_r2.texOffs(10, 120).addBox(-3, -2, -13, 6, 0, 26, 0, false);

		inner_roof_3_r2 = new ModelMapper(modelDataWrapper);
		inner_roof_3_r2.setPos(-15, -32, 0);
		roof_window_ael.addChild(inner_roof_3_r2);
		setRotationAngle(inner_roof_3_r2, 0, 0, -0.1047F);
		inner_roof_3_r2.texOffs(0, 134).addBox(1, -2, -13, 2, 2, 26, 0, false);

		roof_door_tcl = new ModelMapper(modelDataWrapper);
		roof_door_tcl.setPos(0, 24, 0);
		roof_door_tcl.texOffs(201, 269).addBox(-18, -33, -16, 5, 1, 32, 0, false);
		roof_door_tcl.texOffs(68, 8).addBox(-5, -34.5F, -16, 5, 0, 32, 0, false);

		inner_roof_4_r3 = new ModelMapper(modelDataWrapper);
		inner_roof_4_r3.setPos(-3.5384F, -34.6286F, 0);
		roof_door_tcl.addChild(inner_roof_4_r3);
		setRotationAngle(inner_roof_4_r3, 0, 0, -0.1396F);
		inner_roof_4_r3.texOffs(56, 8).addBox(-6, 0, -16, 6, 0, 32, 0, false);

		inner_roof_3_r3 = new ModelMapper(modelDataWrapper);
		inner_roof_3_r3.setPos(-10.4309F, -33.4846F, 0);
		roof_door_tcl.addChild(inner_roof_3_r3);
		setRotationAngle(inner_roof_3_r3, 0, 0, -0.3142F);
		inner_roof_3_r3.texOffs(126, 0).addBox(-1, 0, -16, 2, 0, 32, 0, false);

		inner_roof_2_r2 = new ModelMapper(modelDataWrapper);
		inner_roof_2_r2.setPos(-13, -32, 0);
		roof_door_tcl.addChild(inner_roof_2_r2);
		setRotationAngle(inner_roof_2_r2, 0, 0, -0.6283F);
		inner_roof_2_r2.texOffs(122, 0).addBox(0, 0, -16, 2, 0, 32, 0, false);

		handrail_2_r1 = new ModelMapper(modelDataWrapper);
		handrail_2_r1.setPos(0, 0, 0);
		roof_door_tcl.addChild(handrail_2_r1);
		setRotationAngle(handrail_2_r1, -1.5708F, 0, 0);
		handrail_2_r1.texOffs(0, 0).addBox(0, -16, -31.5F, 0, 32, 0, 0.2F, false);

		roof_door_ael = new ModelMapper(modelDataWrapper);
		roof_door_ael.setPos(0, 24, 0);
		roof_door_ael.texOffs(211, 277).addBox(-18, -33, -12, 3, 1, 24, 0, false);
		roof_door_ael.texOffs(209, 179).addBox(-3.1311F, -33.6679F, -28, 4, 0, 56, 0, false);

		display_main_r1 = new ModelMapper(modelDataWrapper);
		display_main_r1.setPos(0, -33.4588F, -28);
		roof_door_ael.addChild(display_main_r1);
		setRotationAngle(display_main_r1, 0.1745F, 0, 0);
		display_main_r1.texOffs(60, 156).addBox(-10, -2.5412F, -0.1F, 20, 4, 0, 0, false);

		display_6_r1 = new ModelMapper(modelDataWrapper);
		display_6_r1.setPos(-1.1311F, -33.6679F, 28);
		roof_door_ael.addChild(display_6_r1);
		setRotationAngle(display_6_r1, -0.1745F, 0, 0);
		display_6_r1.texOffs(34, 146).addBox(-2.8689F, -3, 0, 4, 3, 0, 0, false);

		display_5_r1 = new ModelMapper(modelDataWrapper);
		display_5_r1.setPos(-5.1202F, -33.4588F, 28);
		roof_door_ael.addChild(display_5_r1);
		setRotationAngle(display_5_r1, -0.1745F, 0, -0.1047F);
		display_5_r1.texOffs(34, 146).addBox(-2.8798F, -3, 0, 5, 3, 0, 0, false);

		display_4_r1 = new ModelMapper(modelDataWrapper);
		display_4_r1.setPos(-9.0655F, -32.834F, 28);
		roof_door_ael.addChild(display_4_r1);
		setRotationAngle(display_4_r1, -0.1745F, 0, -0.2094F);
		display_4_r1.texOffs(34, 146).addBox(-3, -3, 0, 6, 3, 0, 0, false);

		display_3_r1 = new ModelMapper(modelDataWrapper);
		display_3_r1.setPos(-1.1311F, -33.6679F, -28);
		roof_door_ael.addChild(display_3_r1);
		setRotationAngle(display_3_r1, 0.1745F, 0, 0);
		display_3_r1.texOffs(34, 146).addBox(-2.8689F, -3, 0, 4, 3, 0, 0, false);

		display_2_r1 = new ModelMapper(modelDataWrapper);
		display_2_r1.setPos(-5.1202F, -33.4588F, -28);
		roof_door_ael.addChild(display_2_r1);
		setRotationAngle(display_2_r1, 0.1745F, 0, -0.1047F);
		display_2_r1.texOffs(34, 146).addBox(-2.8798F, -3, 0, 5, 3, 0, 0, false);

		display_1_r1 = new ModelMapper(modelDataWrapper);
		display_1_r1.setPos(-9.0655F, -32.834F, -28);
		roof_door_ael.addChild(display_1_r1);
		setRotationAngle(display_1_r1, 0.1745F, 0, -0.2094F);
		display_1_r1.texOffs(34, 146).addBox(-3, -3, 0, 6, 3, 0, 0, false);

		inner_roof_4_r4 = new ModelMapper(modelDataWrapper);
		inner_roof_4_r4.setPos(-5.1202F, -33.4588F, 0);
		roof_door_ael.addChild(inner_roof_4_r4);
		setRotationAngle(inner_roof_4_r4, 0, 0, -0.1047F);
		inner_roof_4_r4.texOffs(201, 179).addBox(-2, 0, -28, 4, 0, 56, 0, false);

		inner_roof_3_r4 = new ModelMapper(modelDataWrapper);
		inner_roof_3_r4.setPos(-9.0655F, -32.834F, 0);
		roof_door_ael.addChild(inner_roof_3_r4);
		setRotationAngle(inner_roof_3_r4, 0, 0, -0.2094F);
		inner_roof_3_r4.texOffs(193, 179).addBox(-2, 0, -28, 4, 0, 56, 0, false);

		inner_roof_2_r3 = new ModelMapper(modelDataWrapper);
		inner_roof_2_r3.setPos(-15, -32, 0);
		roof_door_ael.addChild(inner_roof_2_r3);
		setRotationAngle(inner_roof_2_r3, 0, 0, -0.1047F);
		inner_roof_2_r3.texOffs(225, 195).addBox(0, 0, -12, 4, 0, 24, 0, false);

		roof_exterior = new ModelMapper(modelDataWrapper);
		roof_exterior.setPos(0, 24, 0);


		outer_roof_6_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_6_r1.setPos(-2.339F, -41.5711F, 0);
		roof_exterior.addChild(outer_roof_6_r1);
		setRotationAngle(outer_roof_6_r1, 0, 0, 1.5708F);
		outer_roof_6_r1.texOffs(106, 274).addBox(0, -3, -20, 0, 6, 40, 0, false);

		outer_roof_5_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_5_r1.setPos(-9.7706F, -40.7897F, 0);
		roof_exterior.addChild(outer_roof_5_r1);
		setRotationAngle(outer_roof_5_r1, 0, 0, 1.3963F);
		outer_roof_5_r1.texOffs(106, 280).addBox(0, -4.5F, -20, 0, 9, 40, 0, false);

		outer_roof_4_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_4_r1.setPos(-15.501F, -39.2584F, 0);
		roof_exterior.addChild(outer_roof_4_r1);
		setRotationAngle(outer_roof_4_r1, 0, 0, 1.0472F);
		outer_roof_4_r1.texOffs(106, 289).addBox(0, -1.5F, -20, 0, 3, 40, 0, false);

		outer_roof_3_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_3_r1.setPos(-18.6652F, -37.2758F, 0);
		roof_exterior.addChild(outer_roof_3_r1);
		setRotationAngle(outer_roof_3_r1, 0, 0, 0.5236F);
		outer_roof_3_r1.texOffs(106, 292).addBox(1, -2, -20, 0, 2, 40, 0, false);

		door_tcl = new ModelMapper(modelDataWrapper);
		door_tcl.setPos(0, 24, 0);
		door_tcl.texOffs(0, 195).addBox(-20, 0, -16, 20, 1, 32, 0, false);

		door_left_tcl = new ModelMapper(modelDataWrapper);
		door_left_tcl.setPos(0, 0, 0);
		door_tcl.addChild(door_left_tcl);
		door_left_tcl.texOffs(280, 168).addBox(-21, -14, 0, 1, 14, 14, 0, false);

		door_left_top_r1 = new ModelMapper(modelDataWrapper);
		door_left_top_r1.setPos(-20.8F, -14, 0);
		door_left_tcl.addChild(door_left_top_r1);
		setRotationAngle(door_left_top_r1, 0, 0, 0.1396F);
		door_left_top_r1.texOffs(68, 279).addBox(-0.2F, -19, 0, 1, 19, 14, 0, false);

		door_right_tcl = new ModelMapper(modelDataWrapper);
		door_right_tcl.setPos(0, 0, 0);
		door_tcl.addChild(door_right_tcl);
		door_right_tcl.texOffs(56, 233).addBox(-21, -14, -14, 1, 14, 14, 0, false);

		door_right_top_r1 = new ModelMapper(modelDataWrapper);
		door_right_top_r1.setPos(-20.8F, -14, 0);
		door_right_tcl.addChild(door_right_top_r1);
		setRotationAngle(door_right_top_r1, 0, 0, 0.1396F);
		door_right_top_r1.texOffs(0, 190).addBox(-0.2F, -19, -14, 1, 19, 14, 0, false);

		door_tcl_handrail = new ModelMapper(modelDataWrapper);
		door_tcl_handrail.setPos(0, 24, 0);
		door_tcl_handrail.texOffs(4, 0).addBox(0, -35, 0, 0, 35, 0, 0.2F, false);

		door_ael = new ModelMapper(modelDataWrapper);
		door_ael.setPos(0, 24, 0);
		door_ael.texOffs(8, 203).addBox(-20, 0, -12, 20, 1, 24, 0, false);

		door_left_ael = new ModelMapper(modelDataWrapper);
		door_left_ael.setPos(0, 0, 0);
		door_ael.addChild(door_left_ael);
		door_left_ael.texOffs(283, 171).addBox(-21, -14, 0, 1, 14, 11, 0, false);

		door_left_top_r2 = new ModelMapper(modelDataWrapper);
		door_left_top_r2.setPos(-20.8F, -14, 0);
		door_left_ael.addChild(door_left_top_r2);
		setRotationAngle(door_left_top_r2, 0, 0, 0.1396F);
		door_left_top_r2.texOffs(71, 282).addBox(-0.2F, -19, 0, 1, 19, 11, 0, false);

		door_right_ael = new ModelMapper(modelDataWrapper);
		door_right_ael.setPos(0, 0, 0);
		door_ael.addChild(door_right_ael);
		door_right_ael.texOffs(59, 236).addBox(-21, -14, -11, 1, 14, 11, 0, false);

		door_right_top_r2 = new ModelMapper(modelDataWrapper);
		door_right_top_r2.setPos(-20.8F, -14, 0);
		door_right_ael.addChild(door_right_top_r2);
		setRotationAngle(door_right_top_r2, 0, 0, 0.1396F);
		door_right_top_r2.texOffs(3, 193).addBox(-0.2F, -19, -11, 1, 19, 11, 0, false);

		door_ael_handrail = new ModelMapper(modelDataWrapper);
		door_ael_handrail.setPos(0, 24, 0);
		door_ael_handrail.texOffs(119, 195).addBox(-17, -18, -12.4F, 5, 1, 1, 0, false);

		upper_wall_right_r1 = new ModelMapper(modelDataWrapper);
		upper_wall_right_r1.setPos(-21, -14, -11);
		door_ael_handrail.addChild(upper_wall_right_r1);
		setRotationAngle(upper_wall_right_r1, 0, 0.2014F, 0.1396F);
		upper_wall_right_r1.texOffs(212, 196).addBox(0, -20, -1, 5, 20, 1, 0, false);

		lower_wall_right_r1 = new ModelMapper(modelDataWrapper);
		lower_wall_right_r1.setPos(-21, 0, -11);
		door_ael_handrail.addChild(lower_wall_right_r1);
		setRotationAngle(lower_wall_right_r1, 0, 0.2014F, 0);
		lower_wall_right_r1.texOffs(184, 196).addBox(0, -14, -1, 5, 14, 1, 0, false);

		upper_wall_left_r1 = new ModelMapper(modelDataWrapper);
		upper_wall_left_r1.setPos(-21, -14, 11);
		door_ael_handrail.addChild(upper_wall_left_r1);
		setRotationAngle(upper_wall_left_r1, 0, 2.9402F, 0.1396F);
		upper_wall_left_r1.texOffs(212, 196).addBox(-5, -20, -1, 5, 20, 1, 0, true);

		lower_wall_left_r1 = new ModelMapper(modelDataWrapper);
		lower_wall_left_r1.setPos(-21, 0, 11);
		door_ael_handrail.addChild(lower_wall_left_r1);
		setRotationAngle(lower_wall_left_r1, 0, 2.9402F, 0);
		lower_wall_left_r1.texOffs(184, 196).addBox(-5, -14, -1, 5, 14, 1, 0, true);

		handrail_left_r1 = new ModelMapper(modelDataWrapper);
		handrail_left_r1.setPos(0, 0, 0);
		door_ael_handrail.addChild(handrail_left_r1);
		setRotationAngle(handrail_left_r1, 0, 3.1416F, 0);
		handrail_left_r1.texOffs(119, 195).addBox(12, -18, -12.4F, 5, 1, 1, 0, true);

		door_exterior_tcl = new ModelMapper(modelDataWrapper);
		door_exterior_tcl.setPos(0, 24, 0);


		upper_wall_r5 = new ModelMapper(modelDataWrapper);
		upper_wall_r5.setPos(-21, -14, 0);
		door_exterior_tcl.addChild(upper_wall_r5);
		setRotationAngle(upper_wall_r5, 0, 0, 0.1396F);
		upper_wall_r5.texOffs(72, 196).addBox(0, -23, -16, 1, 4, 32, 0, false);

		floor_r3 = new ModelMapper(modelDataWrapper);
		floor_r3.setPos(-21, 0, 0);
		door_exterior_tcl.addChild(floor_r3);
		setRotationAngle(floor_r3, 0, 0, -0.1745F);
		floor_r3.texOffs(56, 232).addBox(0, 0, -16, 1, 8, 32, 0, false);

		door_left_exterior_tcl = new ModelMapper(modelDataWrapper);
		door_left_exterior_tcl.setPos(0, 0, 0);
		door_exterior_tcl.addChild(door_left_exterior_tcl);
		door_left_exterior_tcl.texOffs(0, 287).addBox(-21, -14, 0, 0, 14, 16, 0, false);

		door_left_top_r3 = new ModelMapper(modelDataWrapper);
		door_left_top_r3.setPos(-21, -14, 0);
		door_left_exterior_tcl.addChild(door_left_top_r3);
		setRotationAngle(door_left_top_r3, 0, 0, 0.1396F);
		door_left_top_r3.texOffs(0, 265).addBox(0, -22, 0, 0, 22, 16, 0, false);

		door_left_base_r1 = new ModelMapper(modelDataWrapper);
		door_left_base_r1.setPos(-21, 0, 0);
		door_left_exterior_tcl.addChild(door_left_base_r1);
		setRotationAngle(door_left_base_r1, 0, 0, -0.1745F);
		door_left_base_r1.texOffs(0, 301).addBox(0, 0, 0, 0, 2, 16, 0, false);

		door_right_exterior_tcl = new ModelMapper(modelDataWrapper);
		door_right_exterior_tcl.setPos(0, 0, 0);
		door_exterior_tcl.addChild(door_right_exterior_tcl);
		door_right_exterior_tcl.texOffs(294, 76).addBox(-21, -14, -16, 0, 14, 16, 0, false);

		door_right_top_r3 = new ModelMapper(modelDataWrapper);
		door_right_top_r3.setPos(-21, -14, 0);
		door_right_exterior_tcl.addChild(door_right_top_r3);
		setRotationAngle(door_right_top_r3, 0, 0, 0.1396F);
		door_right_top_r3.texOffs(294, 54).addBox(0, -22, -16, 0, 22, 16, 0, false);

		door_right_base_r1 = new ModelMapper(modelDataWrapper);
		door_right_base_r1.setPos(-21, 0, 0);
		door_right_exterior_tcl.addChild(door_right_base_r1);
		setRotationAngle(door_right_base_r1, 0, 0, -0.1745F);
		door_right_base_r1.texOffs(294, 90).addBox(0, 0, -16, 0, 2, 16, 0, false);

		door_exterior_ael = new ModelMapper(modelDataWrapper);
		door_exterior_ael.setPos(0, 24, 0);
		door_exterior_ael.texOffs(69, 110).addBox(-21, -14, -28, 0, 14, 15, 0, false);

		upper_wall_2_r2 = new ModelMapper(modelDataWrapper);
		upper_wall_2_r2.setPos(-21, -14, 0);
		door_exterior_ael.addChild(upper_wall_2_r2);
		setRotationAngle(upper_wall_2_r2, 0, 0, 0.1396F);
		upper_wall_2_r2.texOffs(110, 35).addBox(0, -23, -28, 0, 23, 17, 0, false);
		upper_wall_2_r2.texOffs(78, 202).addBox(0, -23, -13, 1, 4, 26, 0, false);

		floor_2_r2 = new ModelMapper(modelDataWrapper);
		floor_2_r2.setPos(-21, 0, 0);
		door_exterior_ael.addChild(floor_2_r2);
		setRotationAngle(floor_2_r2, 0, 0, -0.1745F);
		floor_2_r2.texOffs(95, 172).addBox(0, 0, -28, 1, 8, 15, 0, false);
		floor_2_r2.texOffs(62, 238).addBox(0, 0, -13, 1, 8, 26, 0, false);

		lower_wall_1_r1 = new ModelMapper(modelDataWrapper);
		lower_wall_1_r1.setPos(0, 0, 0);
		door_exterior_ael.addChild(lower_wall_1_r1);
		setRotationAngle(lower_wall_1_r1, 0, 3.1416F, 0);
		lower_wall_1_r1.texOffs(69, 110).addBox(21, -14, -28, 0, 14, 15, 0, true);

		upper_wall_1_r2 = new ModelMapper(modelDataWrapper);
		upper_wall_1_r2.setPos(-21, -14, 0);
		door_exterior_ael.addChild(upper_wall_1_r2);
		setRotationAngle(upper_wall_1_r2, 0, 3.1416F, 0.1396F);
		upper_wall_1_r2.texOffs(110, 35).addBox(0, -23, -28, 0, 23, 17, 0, true);

		floor_1_r2 = new ModelMapper(modelDataWrapper);
		floor_1_r2.setPos(-21, 0, 0);
		door_exterior_ael.addChild(floor_1_r2);
		setRotationAngle(floor_1_r2, 0, 3.1416F, -0.1745F);
		floor_1_r2.texOffs(95, 172).addBox(-1, 0, -28, 1, 8, 15, 0, true);

		door_left_exterior_ael = new ModelMapper(modelDataWrapper);
		door_left_exterior_ael.setPos(0, 0, 0);
		door_exterior_ael.addChild(door_left_exterior_ael);
		door_left_exterior_ael.texOffs(0, 290).addBox(-21, -14, 0, 0, 14, 13, 0, false);

		door_left_top_r4 = new ModelMapper(modelDataWrapper);
		door_left_top_r4.setPos(-21, -14, 0);
		door_left_exterior_ael.addChild(door_left_top_r4);
		setRotationAngle(door_left_top_r4, 0, 0, 0.1396F);
		door_left_top_r4.texOffs(0, 268).addBox(0, -22, 0, 0, 22, 13, 0, false);

		door_left_base_r2 = new ModelMapper(modelDataWrapper);
		door_left_base_r2.setPos(-21, 0, 0);
		door_left_exterior_ael.addChild(door_left_base_r2);
		setRotationAngle(door_left_base_r2, 0, 0, -0.1745F);
		door_left_base_r2.texOffs(0, 304).addBox(0, 0, 0, 0, 2, 13, 0, false);

		door_right_exterior_ael = new ModelMapper(modelDataWrapper);
		door_right_exterior_ael.setPos(0, 0, 0);
		door_exterior_ael.addChild(door_right_exterior_ael);
		door_right_exterior_ael.texOffs(294, 79).addBox(-21, -14, -13, 0, 14, 13, 0, false);

		door_right_top_r4 = new ModelMapper(modelDataWrapper);
		door_right_top_r4.setPos(-21, -14, 0);
		door_right_exterior_ael.addChild(door_right_top_r4);
		setRotationAngle(door_right_top_r4, 0, 0, 0.1396F);
		door_right_top_r4.texOffs(294, 57).addBox(0, -22, -13, 0, 22, 13, 0, false);

		door_right_base_r2 = new ModelMapper(modelDataWrapper);
		door_right_base_r2.setPos(-21, 0, 0);
		door_right_exterior_ael.addChild(door_right_base_r2);
		setRotationAngle(door_right_base_r2, 0, 0, -0.1745F);
		door_right_base_r2.texOffs(294, 93).addBox(0, 0, -13, 0, 2, 13, 0, false);

		door_exterior_end = new ModelMapper(modelDataWrapper);
		door_exterior_end.setPos(0, 24, 0);


		upper_wall_r6 = new ModelMapper(modelDataWrapper);
		upper_wall_r6.setPos(-21, -14, 0);
		door_exterior_end.addChild(upper_wall_r6);
		setRotationAngle(upper_wall_r6, 0, 0, 0.1396F);
		upper_wall_r6.texOffs(72, 196).addBox(0, -23, -16, 1, 4, 32, 0, false);

		floor_r4 = new ModelMapper(modelDataWrapper);
		floor_r4.setPos(-21, 0, 0);
		door_exterior_end.addChild(floor_r4);
		setRotationAngle(floor_r4, 0, 0, -0.1745F);
		floor_r4.texOffs(266, 294).addBox(0, 0, -16, 1, 8, 32, 0, false);

		door_left_exterior_end = new ModelMapper(modelDataWrapper);
		door_left_exterior_end.setPos(0, 0, 0);
		door_exterior_end.addChild(door_left_exterior_end);
		door_left_exterior_end.texOffs(162, 109).addBox(-21, -14, 0, 0, 14, 16, 0, false);

		door_left_top_r5 = new ModelMapper(modelDataWrapper);
		door_left_top_r5.setPos(-21, -14, 0);
		door_left_exterior_end.addChild(door_left_top_r5);
		setRotationAngle(door_left_top_r5, 0, 0, 0.1396F);
		door_left_top_r5.texOffs(0, 265).addBox(0, -22, 0, 0, 22, 16, 0, false);

		door_left_base_r3 = new ModelMapper(modelDataWrapper);
		door_left_base_r3.setPos(-21, 0, 0);
		door_left_exterior_end.addChild(door_left_base_r3);
		setRotationAngle(door_left_base_r3, 0, 0, -0.1745F);
		door_left_base_r3.texOffs(162, 123).addBox(0, 0, 0, 0, 2, 16, 0, false);

		door_right_exterior_end = new ModelMapper(modelDataWrapper);
		door_right_exterior_end.setPos(0, 0, 0);
		door_exterior_end.addChild(door_right_exterior_end);
		door_right_exterior_end.texOffs(162, 125).addBox(-21, -14, -16, 0, 14, 16, 0, false);

		door_right_top_r5 = new ModelMapper(modelDataWrapper);
		door_right_top_r5.setPos(-21, -14, 0);
		door_right_exterior_end.addChild(door_right_top_r5);
		setRotationAngle(door_right_top_r5, 0, 0, 0.1396F);
		door_right_top_r5.texOffs(294, 54).addBox(0, -22, -16, 0, 22, 16, 0, false);

		door_right_base_r3 = new ModelMapper(modelDataWrapper);
		door_right_base_r3.setPos(-21, 0, 0);
		door_right_exterior_end.addChild(door_right_base_r3);
		setRotationAngle(door_right_base_r3, 0, 0, -0.1745F);
		door_right_base_r3.texOffs(162, 139).addBox(0, 0, -16, 0, 2, 16, 0, false);

		luggage_rack = new ModelMapper(modelDataWrapper);
		luggage_rack.setPos(0, 24, 0);
		luggage_rack.texOffs(176, 218).addBox(-21, -14, -8, 3, 14, 16, 0, false);
		luggage_rack.texOffs(60, 139).addBox(-18, -13, -8, 9, 1, 16, 0, false);
		luggage_rack.texOffs(32, 32).addBox(-20, 0, -8, 20, 1, 16, 0, false);

		top_3_r1 = new ModelMapper(modelDataWrapper);
		top_3_r1.setPos(-9.3615F, -29.3127F, -20);
		luggage_rack.addChild(top_3_r1);
		setRotationAngle(top_3_r1, 0, 0, -0.2618F);
		top_3_r1.texOffs(134, 125).addBox(-3, -4, 12, 3, 7, 16, 0, false);

		top_2_r1 = new ModelMapper(modelDataWrapper);
		top_2_r1.setPos(-10, -25, 0);
		luggage_rack.addChild(top_2_r1);
		setRotationAngle(top_2_r1, 0, 0, 0.7854F);
		top_2_r1.texOffs(136, 132).addBox(-1, -2, -8, 1, 2, 16, 0, false);

		top_1_r1 = new ModelMapper(modelDataWrapper);
		top_1_r1.setPos(0, 0, 0);
		luggage_rack.addChild(top_1_r1);
		setRotationAngle(top_1_r1, 0, 0, 1.5708F);
		top_1_r1.texOffs(136, 132).addBox(-26, 10, -8, 1, 8, 16, 0, false);

		upper_wall_r7 = new ModelMapper(modelDataWrapper);
		upper_wall_r7.setPos(-21, -14, 0);
		luggage_rack.addChild(upper_wall_r7);
		setRotationAngle(upper_wall_r7, 0, 0, 0.1396F);
		upper_wall_r7.texOffs(142, 78).addBox(0, -19, -8, 3, 19, 16, 0, false);

		end_tcl = new ModelMapper(modelDataWrapper);
		end_tcl.setPos(0, 24, 0);
		end_tcl.texOffs(162, 175).addBox(-20, 0, -12, 40, 1, 20, 0, false);
		end_tcl.texOffs(110, 195).addBox(18, -14, 7, 3, 14, 3, 0, true);
		end_tcl.texOffs(110, 195).addBox(-21, -14, 7, 3, 14, 3, 0, false);
		end_tcl.texOffs(127, 231).addBox(9.5F, -34, -12, 9, 34, 19, 0, false);
		end_tcl.texOffs(0, 228).addBox(-18.5F, -34, -12, 9, 34, 19, 0, false);
		end_tcl.texOffs(79, 293).addBox(-9.5F, -35, -12, 19, 2, 19, 0, false);

		upper_wall_2_r3 = new ModelMapper(modelDataWrapper);
		upper_wall_2_r3.setPos(-21, -14, 0);
		end_tcl.addChild(upper_wall_2_r3);
		setRotationAngle(upper_wall_2_r3, 0, 0, 0.1396F);
		upper_wall_2_r3.texOffs(248, 110).addBox(0, -19, 7, 3, 19, 3, 0, false);

		upper_wall_1_r3 = new ModelMapper(modelDataWrapper);
		upper_wall_1_r3.setPos(21, -14, 0);
		end_tcl.addChild(upper_wall_1_r3);
		setRotationAngle(upper_wall_1_r3, 0, 0, -0.1396F);
		upper_wall_1_r3.texOffs(164, 227).addBox(-3, -19, 7, 3, 19, 3, 0, true);

		end_ael = new ModelMapper(modelDataWrapper);
		end_ael.setPos(0, 24, 0);
		end_ael.texOffs(138, 173).addBox(-7, 0, -12, 14, 1, 10, 0, false);
		end_ael.texOffs(128, 238).addBox(7, -36, -12, 11, 36, 10, 0, true);
		end_ael.texOffs(128, 238).addBox(-18, -36, -12, 11, 36, 10, 0, false);
		end_ael.texOffs(24, 186).addBox(-7, -36, -12, 14, 4, 10, 0, false);

		end_exterior_tcl = new ModelMapper(modelDataWrapper);
		end_exterior_tcl.setPos(0, 24, 0);
		end_exterior_tcl.texOffs(0, 134).addBox(18, -14, -12, 3, 14, 22, 0, true);
		end_exterior_tcl.texOffs(0, 134).addBox(-21, -14, -12, 3, 14, 22, 0, false);
		end_exterior_tcl.texOffs(182, 0).addBox(9.5F, -34, -12, 9, 34, 0, 0, true);
		end_exterior_tcl.texOffs(182, 0).addBox(-18.5F, -34, -12, 9, 34, 0, 0, false);
		end_exterior_tcl.texOffs(240, 236).addBox(-18, -41, -12, 36, 8, 0, 0, false);

		upper_wall_2_r4 = new ModelMapper(modelDataWrapper);
		upper_wall_2_r4.setPos(-21, -14, 0);
		end_exterior_tcl.addChild(upper_wall_2_r4);
		setRotationAngle(upper_wall_2_r4, 0, 0, 0.1396F);
		upper_wall_2_r4.texOffs(183, 251).addBox(0, -23, -12, 3, 23, 22, 0, false);

		upper_wall_1_r4 = new ModelMapper(modelDataWrapper);
		upper_wall_1_r4.setPos(21, -14, 0);
		end_exterior_tcl.addChild(upper_wall_1_r4);
		setRotationAngle(upper_wall_1_r4, 0, 0, -0.1396F);
		upper_wall_1_r4.texOffs(183, 251).addBox(-3, -23, -12, 3, 23, 22, 0, true);

		floor_2_r3 = new ModelMapper(modelDataWrapper);
		floor_2_r3.setPos(-21, 0, 0);
		end_exterior_tcl.addChild(floor_2_r3);
		setRotationAngle(floor_2_r3, 0, 0, -0.1745F);
		floor_2_r3.texOffs(112, 139).addBox(0, 0, -12, 1, 8, 20, 0, false);

		floor_1_r3 = new ModelMapper(modelDataWrapper);
		floor_1_r3.setPos(21, 0, 0);
		end_exterior_tcl.addChild(floor_1_r3);
		setRotationAngle(floor_1_r3, 0, 0, 0.1745F);
		floor_1_r3.texOffs(112, 139).addBox(-1, 0, -12, 1, 8, 20, 0, true);

		end_exterior_ael = new ModelMapper(modelDataWrapper);
		end_exterior_ael.setPos(0, 24, 0);
		end_exterior_ael.texOffs(149, 185).addBox(-20, 0, -12, 40, 1, 10, 0, false);
		end_exterior_ael.texOffs(0, 134).addBox(18, -14, -12, 3, 14, 10, 0, true);
		end_exterior_ael.texOffs(0, 134).addBox(-21, -14, -12, 3, 14, 10, 0, false);
		end_exterior_ael.texOffs(180, 2).addBox(7, -32, -12, 11, 32, 0, 0, true);
		end_exterior_ael.texOffs(180, 2).addBox(-18, -32, -12, 11, 32, 0, 0, false);
		end_exterior_ael.texOffs(240, 235).addBox(-18, -41, -12, 36, 9, 0, 0, false);

		upper_wall_3_r1 = new ModelMapper(modelDataWrapper);
		upper_wall_3_r1.setPos(-21, -14, 0);
		end_exterior_ael.addChild(upper_wall_3_r1);
		setRotationAngle(upper_wall_3_r1, 0, 0, 0.1396F);
		upper_wall_3_r1.texOffs(196, 120).addBox(0, -23, -12, 3, 23, 10, 0, false);

		upper_wall_2_r5 = new ModelMapper(modelDataWrapper);
		upper_wall_2_r5.setPos(21, -14, 0);
		end_exterior_ael.addChild(upper_wall_2_r5);
		setRotationAngle(upper_wall_2_r5, 0, 0, -0.1396F);
		upper_wall_2_r5.texOffs(196, 120).addBox(-3, -23, -12, 3, 23, 10, 0, true);

		floor_3_r1 = new ModelMapper(modelDataWrapper);
		floor_3_r1.setPos(-21, 0, 0);
		end_exterior_ael.addChild(floor_3_r1);
		setRotationAngle(floor_3_r1, 0, 0, -0.1745F);
		floor_3_r1.texOffs(122, 149).addBox(0, 0, -12, 1, 8, 10, 0, false);

		floor_2_r4 = new ModelMapper(modelDataWrapper);
		floor_2_r4.setPos(21, 0, 0);
		end_exterior_ael.addChild(floor_2_r4);
		setRotationAngle(floor_2_r4, 0, 0, 0.1745F);
		floor_2_r4.texOffs(122, 149).addBox(-1, 0, -12, 1, 8, 10, 0, true);

		end_door_ael = new ModelMapper(modelDataWrapper);
		end_door_ael.setPos(0, 24, 0);
		end_door_ael.texOffs(280, 0).addBox(-7, -32, -2, 14, 32, 0, 0, false);

		roof_end = new ModelMapper(modelDataWrapper);
		roof_end.setPos(0, 24, 0);


		handrail_2_r2 = new ModelMapper(modelDataWrapper);
		handrail_2_r2.setPos(0, 0, 0);
		roof_end.addChild(handrail_2_r2);
		setRotationAngle(handrail_2_r2, -1.5708F, 0, 0);
		handrail_2_r2.texOffs(0, 0).addBox(0, -40, -31.5F, 0, 16, 0, 0.2F, false);

		inner_roof_1 = new ModelMapper(modelDataWrapper);
		inner_roof_1.setPos(-2, -33, 16);
		roof_end.addChild(inner_roof_1);
		inner_roof_1.texOffs(197, 265).addBox(-16, 0, -12, 5, 1, 36, 0, false);
		inner_roof_1.texOffs(64, 0).addBox(-3, -1.5F, -12, 5, 0, 36, 0, false);

		inner_roof_4_r5 = new ModelMapper(modelDataWrapper);
		inner_roof_4_r5.setPos(-1.5384F, -1.6286F, -16);
		inner_roof_1.addChild(inner_roof_4_r5);
		setRotationAngle(inner_roof_4_r5, 0, 0, -0.1396F);
		inner_roof_4_r5.texOffs(52, 0).addBox(-6, 0, 4, 6, 0, 36, 0, false);

		inner_roof_3_r5 = new ModelMapper(modelDataWrapper);
		inner_roof_3_r5.setPos(-8.4309F, -0.4846F, -16);
		inner_roof_1.addChild(inner_roof_3_r5);
		setRotationAngle(inner_roof_3_r5, 0, 0, -0.3142F);
		inner_roof_3_r5.texOffs(8, 49).addBox(-1, 0, 4, 2, 0, 36, 0, false);

		inner_roof_2_r4 = new ModelMapper(modelDataWrapper);
		inner_roof_2_r4.setPos(-11, 1, -16);
		inner_roof_1.addChild(inner_roof_2_r4);
		setRotationAngle(inner_roof_2_r4, 0, 0, -0.6283F);
		inner_roof_2_r4.texOffs(96, 0).addBox(0, 0, 4, 2, 0, 36, 0, false);

		inner_roof_2 = new ModelMapper(modelDataWrapper);
		inner_roof_2.setPos(-2, -33, 16);
		roof_end.addChild(inner_roof_2);
		inner_roof_2.texOffs(197, 265).addBox(15, 0, -12, 5, 1, 36, 0, true);
		inner_roof_2.texOffs(64, 0).addBox(2, -1.5F, -12, 5, 0, 36, 0, true);

		inner_roof_4_r6 = new ModelMapper(modelDataWrapper);
		inner_roof_4_r6.setPos(5.5384F, -1.6286F, -16);
		inner_roof_2.addChild(inner_roof_4_r6);
		setRotationAngle(inner_roof_4_r6, 0, 0, 0.1396F);
		inner_roof_4_r6.texOffs(52, 0).addBox(0, 0, 4, 6, 0, 36, 0, true);

		inner_roof_3_r6 = new ModelMapper(modelDataWrapper);
		inner_roof_3_r6.setPos(12.4309F, -0.4846F, -16);
		inner_roof_2.addChild(inner_roof_3_r6);
		setRotationAngle(inner_roof_3_r6, 0, 0, 0.3142F);
		inner_roof_3_r6.texOffs(8, 49).addBox(-1, 0, 4, 2, 0, 36, 0, true);

		inner_roof_2_r5 = new ModelMapper(modelDataWrapper);
		inner_roof_2_r5.setPos(15, 1, -16);
		inner_roof_2.addChild(inner_roof_2_r5);
		setRotationAngle(inner_roof_2_r5, 0, 0, 0.6283F);
		inner_roof_2_r5.texOffs(96, 0).addBox(-2, 0, 4, 2, 0, 36, 0, true);

		roof_end_exterior = new ModelMapper(modelDataWrapper);
		roof_end_exterior.setPos(0, 24, 0);
		roof_end_exterior.texOffs(62, 75).addBox(-8, -42.5F, 0, 16, 2, 48, 0, false);

		vent_2_r1 = new ModelMapper(modelDataWrapper);
		vent_2_r1.setPos(-8, -42.5F, 0);
		roof_end_exterior.addChild(vent_2_r1);
		setRotationAngle(vent_2_r1, 0, 0, -0.3491F);
		vent_2_r1.texOffs(88, 1).addBox(-9, 0, 0, 9, 2, 48, 0, false);

		vent_1_r1 = new ModelMapper(modelDataWrapper);
		vent_1_r1.setPos(8, -42.5F, 0);
		roof_end_exterior.addChild(vent_1_r1);
		setRotationAngle(vent_1_r1, 0, 0, 0.3491F);
		vent_1_r1.texOffs(88, 1).addBox(0, 0, 0, 9, 2, 48, 0, true);

		outer_roof_1 = new ModelMapper(modelDataWrapper);
		outer_roof_1.setPos(0, 0, 0);
		roof_end_exterior.addChild(outer_roof_1);


		outer_roof_6_r2 = new ModelMapper(modelDataWrapper);
		outer_roof_6_r2.setPos(-2.3377F, -41.071F, 0);
		outer_roof_1.addChild(outer_roof_6_r2);
		setRotationAngle(outer_roof_6_r2, 0, 0, 1.5708F);
		outer_roof_6_r2.texOffs(174, 137).addBox(-0.5F, -3, -12, 1, 6, 20, 0, false);

		outer_roof_5_r2 = new ModelMapper(modelDataWrapper);
		outer_roof_5_r2.setPos(-9.6825F, -40.2972F, 0);
		outer_roof_1.addChild(outer_roof_5_r2);
		setRotationAngle(outer_roof_5_r2, 0, 0, 1.3963F);
		outer_roof_5_r2.texOffs(154, 14).addBox(-0.5F, -4.5F, -12, 1, 9, 20, 0, false);

		outer_roof_4_r2 = new ModelMapper(modelDataWrapper);
		outer_roof_4_r2.setPos(-15.25F, -38.8252F, 0);
		outer_roof_1.addChild(outer_roof_4_r2);
		setRotationAngle(outer_roof_4_r2, 0, 0, 1.0472F);
		outer_roof_4_r2.texOffs(106, 195).addBox(-0.5F, -1.5F, -12, 1, 3, 20, 0, false);

		outer_roof_3_r2 = new ModelMapper(modelDataWrapper);
		outer_roof_3_r2.setPos(-16.866F, -37.3922F, 0);
		outer_roof_1.addChild(outer_roof_3_r2);
		setRotationAngle(outer_roof_3_r2, 0, 0, 0.5236F);
		outer_roof_3_r2.texOffs(162, 196).addBox(-0.5F, -1, -12, 1, 2, 20, 0, false);

		outer_roof_2 = new ModelMapper(modelDataWrapper);
		outer_roof_2.setPos(0, 0, 0);
		roof_end_exterior.addChild(outer_roof_2);


		outer_roof_5_r3 = new ModelMapper(modelDataWrapper);
		outer_roof_5_r3.setPos(9.6825F, -40.2972F, 0);
		outer_roof_2.addChild(outer_roof_5_r3);
		setRotationAngle(outer_roof_5_r3, 0, 0, -1.3963F);
		outer_roof_5_r3.texOffs(154, 14).addBox(-0.5F, -4.5F, -12, 1, 9, 20, 0, true);

		outer_roof_4_r3 = new ModelMapper(modelDataWrapper);
		outer_roof_4_r3.setPos(15.25F, -38.8252F, 0);
		outer_roof_2.addChild(outer_roof_4_r3);
		setRotationAngle(outer_roof_4_r3, 0, 0, -1.0472F);
		outer_roof_4_r3.texOffs(106, 195).addBox(-0.5F, -1.5F, -12, 1, 3, 20, 0, true);

		outer_roof_3_r3 = new ModelMapper(modelDataWrapper);
		outer_roof_3_r3.setPos(16.866F, -37.3922F, 0);
		outer_roof_2.addChild(outer_roof_3_r3);
		setRotationAngle(outer_roof_3_r3, 0, 0, -0.5236F);
		outer_roof_3_r3.texOffs(162, 196).addBox(-0.5F, -1, -12, 1, 2, 20, 0, true);

		outer_roof_6_r3 = new ModelMapper(modelDataWrapper);
		outer_roof_6_r3.setPos(2.3377F, -41.071F, 0);
		outer_roof_2.addChild(outer_roof_6_r3);
		setRotationAngle(outer_roof_6_r3, 0, 0, -1.5708F);
		outer_roof_6_r3.texOffs(174, 137).addBox(-0.5F, -3, -12, 1, 6, 20, 0, true);

		roof_light_tcl = new ModelMapper(modelDataWrapper);
		roof_light_tcl.setPos(0, 24, 0);


		roof_light_r1 = new ModelMapper(modelDataWrapper);
		roof_light_r1.setPos(-7, -33.5F, 0);
		roof_light_tcl.addChild(roof_light_r1);
		setRotationAngle(roof_light_r1, 0, 0, 0.1745F);
		roof_light_r1.texOffs(154, 0).addBox(-3, -1, -24, 3, 1, 48, 0, false);

		roof_light_door_ael = new ModelMapper(modelDataWrapper);
		roof_light_door_ael.setPos(0, 24, 0);
		roof_light_door_ael.texOffs(126, 78).addBox(-10, -33.7F, -8, 10, 0, 16, 0, false);

		roof_light_window_ael = new ModelMapper(modelDataWrapper);
		roof_light_window_ael.setPos(0, 24, 0);


		inner_roof_3_r7 = new ModelMapper(modelDataWrapper);
		inner_roof_3_r7.setPos(-15, -32, 0);
		roof_light_window_ael.addChild(inner_roof_3_r7);
		setRotationAngle(inner_roof_3_r7, 0, 0, -0.1047F);
		inner_roof_3_r7.texOffs(176, 22).addBox(2.5F, -1.1F, -13, 2, 1, 26, 0, false);

		roof_end_light = new ModelMapper(modelDataWrapper);
		roof_end_light.setPos(0, 24, 0);


		roof_light_2_r1 = new ModelMapper(modelDataWrapper);
		roof_light_2_r1.setPos(7, -33.5F, 0);
		roof_end_light.addChild(roof_light_2_r1);
		setRotationAngle(roof_light_2_r1, 0, 0, -0.1745F);
		roof_light_2_r1.texOffs(166, 12).addBox(0, -1, 4, 3, 1, 36, 0, false);

		roof_light_1_r1 = new ModelMapper(modelDataWrapper);
		roof_light_1_r1.setPos(-7, -33.5F, 0);
		roof_end_light.addChild(roof_light_1_r1);
		setRotationAngle(roof_light_1_r1, 0, 0, 0.1745F);
		roof_light_1_r1.texOffs(166, 12).addBox(-3, -1, 4, 3, 1, 36, 0, false);

		head_tcl = new ModelMapper(modelDataWrapper);
		head_tcl.setPos(0, 24, 0);
		head_tcl.texOffs(180, 168).addBox(-20, 0, 4, 40, 1, 4, 0, false);
		head_tcl.texOffs(90, 75).addBox(18, -14, 4, 3, 14, 6, 0, true);
		head_tcl.texOffs(90, 75).addBox(-21, -14, 4, 3, 14, 6, 0, false);
		head_tcl.texOffs(208, 0).addBox(-18, -41, 4, 36, 41, 0, 0, false);

		upper_wall_2_r6 = new ModelMapper(modelDataWrapper);
		upper_wall_2_r6.setPos(-21, -14, 0);
		head_tcl.addChild(upper_wall_2_r6);
		setRotationAngle(upper_wall_2_r6, 0, 0, 0.1396F);
		upper_wall_2_r6.texOffs(0, 72).addBox(0, -19, 4, 3, 19, 6, 0, false);

		upper_wall_1_r5 = new ModelMapper(modelDataWrapper);
		upper_wall_1_r5.setPos(21, -14, 0);
		head_tcl.addChild(upper_wall_1_r5);
		setRotationAngle(upper_wall_1_r5, 0, 0, -0.1396F);
		upper_wall_1_r5.texOffs(0, 72).addBox(-3, -19, 4, 3, 19, 6, 0, true);

		head_ael = new ModelMapper(modelDataWrapper);
		head_ael.setPos(0, 24, 0);
		head_ael.texOffs(208, 0).addBox(-18, -41, -2, 36, 41, 0, 0, false);

		head_exterior = new ModelMapper(modelDataWrapper);
		head_exterior.setPos(0, 24, 0);
		head_exterior.texOffs(158, 306).addBox(-20, 0, -24, 40, 1, 28, 0, false);
		head_exterior.texOffs(232, 110).addBox(18, -14, -24, 3, 14, 34, 0, true);
		head_exterior.texOffs(232, 110).addBox(-21, -14, -24, 3, 14, 34, 0, false);
		head_exterior.texOffs(200, 69).addBox(-18, -41, -3, 36, 41, 0, 0, false);

		upper_wall_2_r7 = new ModelMapper(modelDataWrapper);
		upper_wall_2_r7.setPos(-21, -14, 0);
		head_exterior.addChild(upper_wall_2_r7);
		setRotationAngle(upper_wall_2_r7, 0, 0, 0.1396F);
		upper_wall_2_r7.texOffs(250, 237).addBox(0, -23, -24, 3, 23, 34, 0, false);

		upper_wall_1_r6 = new ModelMapper(modelDataWrapper);
		upper_wall_1_r6.setPos(21, -14, 0);
		head_exterior.addChild(upper_wall_1_r6);
		setRotationAngle(upper_wall_1_r6, 0, 0, -0.1396F);
		upper_wall_1_r6.texOffs(250, 237).addBox(-3, -23, -24, 3, 23, 34, 0, true);

		floor_2_r5 = new ModelMapper(modelDataWrapper);
		floor_2_r5.setPos(-21, 0, 0);
		head_exterior.addChild(floor_2_r5);
		setRotationAngle(floor_2_r5, 0, 0, -0.1745F);
		floor_2_r5.texOffs(90, 252).addBox(0, 0, -24, 1, 8, 32, 0, false);

		floor_1_r4 = new ModelMapper(modelDataWrapper);
		floor_1_r4.setPos(21, 0, 0);
		head_exterior.addChild(floor_1_r4);
		setRotationAngle(floor_1_r4, 0, 0, 0.1745F);
		floor_1_r4.texOffs(90, 252).addBox(-1, 0, -24, 1, 8, 32, 0, true);

		front = new ModelMapper(modelDataWrapper);
		front.setPos(0, 0, 0);
		head_exterior.addChild(front);
		front.texOffs(142, 76).addBox(-9, 0.9884F, -46.7528F, 18, 2, 0, 0, false);
		front.texOffs(172, 51).addBox(-21, 0, -24, 42, 8, 0, 0, false);

		front_bottom_5_r1 = new ModelMapper(modelDataWrapper);
		front_bottom_5_r1.setPos(0, 4.4033F, -45.3383F);
		front.addChild(front_bottom_5_r1);
		setRotationAngle(front_bottom_5_r1, 1.4137F, 0, 0);
		front_bottom_5_r1.texOffs(256, 42).addBox(-20, 0, -0.001F, 40, 22, 0, 0, false);

		front_bottom_4_r1 = new ModelMapper(modelDataWrapper);
		front_bottom_4_r1.setPos(0, 3.6962F, -46.0454F);
		front.addChild(front_bottom_4_r1);
		setRotationAngle(front_bottom_4_r1, 0.7854F, 0, 0);
		front_bottom_4_r1.texOffs(0, 98).addBox(-12, -1, 0, 24, 2, 0, 0, false);

		front_bottom_2_r1 = new ModelMapper(modelDataWrapper);
		front_bottom_2_r1.setPos(0, 0.1009F, -46.292F);
		front.addChild(front_bottom_2_r1);
		setRotationAngle(front_bottom_2_r1, -0.48F, 0, 0);
		front_bottom_2_r1.texOffs(58, 120).addBox(-11, -1, 0, 22, 2, 0, 0, false);

		front_bottom_1_r1 = new ModelMapper(modelDataWrapper);
		front_bottom_1_r1.setPos(0, -4, -42);
		front.addChild(front_bottom_1_r1);
		setRotationAngle(front_bottom_1_r1, -0.8727F, 0, 0);
		front_bottom_1_r1.texOffs(154, 43).addBox(-12, 0, 0, 24, 5, 0, 0, false);

		front_panel_3_r1 = new ModelMapper(modelDataWrapper);
		front_panel_3_r1.setPos(0, -32.2269F, -28.3321F);
		front.addChild(front_panel_3_r1);
		setRotationAngle(front_panel_3_r1, -0.6283F, 0, 0);
		front_panel_3_r1.texOffs(211, 250).addBox(-12, -6.5F, 0, 24, 12, 0, 0, false);

		front_panel_2_r1 = new ModelMapper(modelDataWrapper);
		front_panel_2_r1.setPos(0, -20.5874F, -35.0728F);
		front.addChild(front_panel_2_r1);
		setRotationAngle(front_panel_2_r1, -0.4538F, 0, 0);
		front_panel_2_r1.texOffs(90, 232).addBox(-12, -8, 0, 24, 16, 0, 0, false);

		front_panel_1_r1 = new ModelMapper(modelDataWrapper);
		front_panel_1_r1.setPos(0, -4, -42);
		front.addChild(front_panel_1_r1);
		setRotationAngle(front_panel_1_r1, -0.3491F, 0, 0);
		front_panel_1_r1.texOffs(200, 110).addBox(-12, -10, 0, 24, 10, 0, 0, false);

		side_1 = new ModelMapper(modelDataWrapper);
		side_1.setPos(0, 0, 0);
		front.addChild(side_1);


		front_side_bottom_2_r1 = new ModelMapper(modelDataWrapper);
		front_side_bottom_2_r1.setPos(18.3378F, 3.5923F, -35.3251F);
		side_1.addChild(front_side_bottom_2_r1);
		setRotationAngle(front_side_bottom_2_r1, 0, 0.1745F, 0.1745F);
		front_side_bottom_2_r1.texOffs(112, 157).addBox(0, -4, -6.5F, 0, 8, 18, 0, true);

		front_middle_top_r1 = new ModelMapper(modelDataWrapper);
		front_middle_top_r1.setPos(-0.6613F, -41.5702F, -11.9997F);
		side_1.addChild(front_middle_top_r1);
		setRotationAngle(front_middle_top_r1, 0, 0.3491F, -1.5708F);
		front_middle_top_r1.texOffs(232, 118).addBox(0, 0, -14, 0, 6, 14, 0, true);

		outer_roof_8_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_8_r1.setPos(16.824F, -34.5499F, -16.9825F);
		side_1.addChild(outer_roof_8_r1);
		setRotationAngle(outer_roof_8_r1, 0, 0.1745F, -1.0472F);
		outer_roof_8_r1.texOffs(70, 66).addBox(2.5F, -5, -3.5F, 0, 6, 9, 0, true);

		outer_roof_7_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_7_r1.setPos(17.2991F, -37.6418F, 0);
		side_1.addChild(outer_roof_7_r1);
		setRotationAngle(outer_roof_7_r1, 0, 0, -0.5236F);
		outer_roof_7_r1.texOffs(22, 25).addBox(0, -1, -19, 0, 2, 7, 0, true);

		outer_roof_6_r4 = new ModelMapper(modelDataWrapper);
		outer_roof_6_r4.setPos(11.7869F, -37.8295F, -19.0477F);
		side_1.addChild(outer_roof_6_r4);
		setRotationAngle(outer_roof_6_r4, 0, 0.3491F, -1.3963F);
		outer_roof_6_r4.texOffs(72, 180).addBox(0, -7, -7.5F, 0, 14, 15, 0, true);

		front_panel_8_r1 = new ModelMapper(modelDataWrapper);
		front_panel_8_r1.setPos(8.9994F, 2.9884F, -46.7521F);
		side_1.addChild(front_panel_8_r1);
		setRotationAngle(front_panel_8_r1, 0, -0.5672F, 0);
		front_panel_8_r1.texOffs(212, 217).addBox(0, -5.9884F, -0.001F, 11, 8, 0, 0, true);

		front_panel_7_r1 = new ModelMapper(modelDataWrapper);
		front_panel_7_r1.setPos(12, -4, -42);
		side_1.addChild(front_panel_7_r1);
		setRotationAngle(front_panel_7_r1, -0.7418F, -0.3491F, 0);
		front_panel_7_r1.texOffs(208, 42).addBox(-4, 0, 0, 11, 5, 0, 0, true);

		front_panel_6_r1 = new ModelMapper(modelDataWrapper);
		front_panel_6_r1.setPos(12, -4, -42);
		side_1.addChild(front_panel_6_r1);
		setRotationAngle(front_panel_6_r1, -0.3491F, -0.3491F, 0);
		front_panel_6_r1.texOffs(162, 196).addBox(0, -12, 0, 9, 12, 0, 0, true);

		front_panel_5_r1 = new ModelMapper(modelDataWrapper);
		front_panel_5_r1.setPos(12, -13.397F, -38.5798F);
		side_1.addChild(front_panel_5_r1);
		setRotationAngle(front_panel_5_r1, -0.4538F, -0.3491F, 0);
		front_panel_5_r1.texOffs(112, 139).addBox(0, -18, -0.001F, 10, 18, 0, 0, true);

		front_panel_4_r1 = new ModelMapper(modelDataWrapper);
		front_panel_4_r1.setPos(15.1229F, -32.2269F, -26.988F);
		side_1.addChild(front_panel_4_r1);
		setRotationAngle(front_panel_4_r1, -0.6283F, -0.3491F, 0);
		front_panel_4_r1.texOffs(0, 228).addBox(-4.5F, -4.5F, 0, 9, 8, 0, 0, true);

		front_side_upper_2_r1 = new ModelMapper(modelDataWrapper);
		front_side_upper_2_r1.setPos(21, -14, -24);
		side_1.addChild(front_side_upper_2_r1);
		setRotationAngle(front_side_upper_2_r1, 0, 0.1745F, -0.1396F);
		front_side_upper_2_r1.texOffs(0, 37).addBox(0, -21, -12, 0, 21, 12, 0, true);

		front_side_lower_2_r1 = new ModelMapper(modelDataWrapper);
		front_side_lower_2_r1.setPos(21, 0, -24);
		side_1.addChild(front_side_lower_2_r1);
		setRotationAngle(front_side_lower_2_r1, 0, 0.1745F, 0);
		front_side_lower_2_r1.texOffs(0, 102).addBox(0, -14, -18, 0, 14, 18, 0, true);

		side_2 = new ModelMapper(modelDataWrapper);
		side_2.setPos(21, 0, -9);
		front.addChild(side_2);


		front_side_bottom_2_r2 = new ModelMapper(modelDataWrapper);
		front_side_bottom_2_r2.setPos(-39.3378F, 3.5923F, -26.3251F);
		side_2.addChild(front_side_bottom_2_r2);
		setRotationAngle(front_side_bottom_2_r2, 0, -0.1745F, -0.1745F);
		front_side_bottom_2_r2.texOffs(112, 157).addBox(0, -4, -6.5F, 0, 8, 18, 0, false);

		front_middle_top_r2 = new ModelMapper(modelDataWrapper);
		front_middle_top_r2.setPos(-20.3387F, -41.5702F, -2.9997F);
		side_2.addChild(front_middle_top_r2);
		setRotationAngle(front_middle_top_r2, 0, -0.3491F, 1.5708F);
		front_middle_top_r2.texOffs(232, 118).addBox(0, 0, -14, 0, 6, 14, 0, false);

		outer_roof_8_r2 = new ModelMapper(modelDataWrapper);
		outer_roof_8_r2.setPos(-37.824F, -34.5499F, -7.9825F);
		side_2.addChild(outer_roof_8_r2);
		setRotationAngle(outer_roof_8_r2, 0, -0.1745F, 1.0472F);
		outer_roof_8_r2.texOffs(70, 66).addBox(-2.5F, -5, -3.5F, 0, 6, 9, 0, false);

		outer_roof_7_r2 = new ModelMapper(modelDataWrapper);
		outer_roof_7_r2.setPos(-38.2991F, -37.6418F, 9);
		side_2.addChild(outer_roof_7_r2);
		setRotationAngle(outer_roof_7_r2, 0, 0, 0.5236F);
		outer_roof_7_r2.texOffs(22, 25).addBox(0, -1, -19, 0, 2, 7, 0, false);

		outer_roof_6_r5 = new ModelMapper(modelDataWrapper);
		outer_roof_6_r5.setPos(-32.7869F, -37.8295F, -10.0477F);
		side_2.addChild(outer_roof_6_r5);
		setRotationAngle(outer_roof_6_r5, 0, -0.3491F, 1.3963F);
		outer_roof_6_r5.texOffs(72, 180).addBox(0, -7, -7.5F, 0, 14, 15, 0, false);

		front_panel_8_r2 = new ModelMapper(modelDataWrapper);
		front_panel_8_r2.setPos(-29.9994F, 2.9884F, -37.7521F);
		side_2.addChild(front_panel_8_r2);
		setRotationAngle(front_panel_8_r2, 0, 0.5672F, 0);
		front_panel_8_r2.texOffs(212, 217).addBox(-11, -5.9884F, -0.001F, 11, 8, 0, 0, false);

		front_panel_7_r2 = new ModelMapper(modelDataWrapper);
		front_panel_7_r2.setPos(-33, -4, -33);
		side_2.addChild(front_panel_7_r2);
		setRotationAngle(front_panel_7_r2, -0.7418F, 0.3491F, 0);
		front_panel_7_r2.texOffs(208, 42).addBox(-7, 0, 0, 11, 5, 0, 0, false);

		front_panel_6_r2 = new ModelMapper(modelDataWrapper);
		front_panel_6_r2.setPos(-33, -4, -33);
		side_2.addChild(front_panel_6_r2);
		setRotationAngle(front_panel_6_r2, -0.3491F, 0.3491F, 0);
		front_panel_6_r2.texOffs(162, 196).addBox(-9, -12, 0, 9, 12, 0, 0, false);

		front_panel_5_r2 = new ModelMapper(modelDataWrapper);
		front_panel_5_r2.setPos(-33, -13.397F, -29.5798F);
		side_2.addChild(front_panel_5_r2);
		setRotationAngle(front_panel_5_r2, -0.4538F, 0.3491F, 0);
		front_panel_5_r2.texOffs(112, 139).addBox(-10, -18, -0.001F, 10, 18, 0, 0, false);

		front_panel_4_r2 = new ModelMapper(modelDataWrapper);
		front_panel_4_r2.setPos(-36.1229F, -32.2269F, -17.988F);
		side_2.addChild(front_panel_4_r2);
		setRotationAngle(front_panel_4_r2, -0.6283F, 0.3491F, 0);
		front_panel_4_r2.texOffs(0, 228).addBox(-4.5F, -4.5F, 0, 9, 8, 0, 0, false);

		front_side_upper_2_r2 = new ModelMapper(modelDataWrapper);
		front_side_upper_2_r2.setPos(-42, -14, -15);
		side_2.addChild(front_side_upper_2_r2);
		setRotationAngle(front_side_upper_2_r2, 0, -0.1745F, 0.1396F);
		front_side_upper_2_r2.texOffs(0, 37).addBox(0, -21, -12, 0, 21, 12, 0, false);

		front_side_lower_2_r2 = new ModelMapper(modelDataWrapper);
		front_side_lower_2_r2.setPos(-42, 0, -15);
		side_2.addChild(front_side_lower_2_r2);
		setRotationAngle(front_side_lower_2_r2, 0, -0.1745F, 0);
		front_side_lower_2_r2.texOffs(0, 102).addBox(0, -14, -18, 0, 14, 18, 0, false);

		headlights = new ModelMapper(modelDataWrapper);
		headlights.setPos(0, 24, 0);


		headlight_2b_r1 = new ModelMapper(modelDataWrapper);
		headlight_2b_r1.setPos(0, -4, -42.1F);
		headlights.addChild(headlight_2b_r1);
		setRotationAngle(headlight_2b_r1, -0.3491F, 0, 0);
		headlight_2b_r1.texOffs(20, 4).addBox(8, -4, 0, 4, 4, 0, 0, true);
		headlight_2b_r1.texOffs(20, 4).addBox(-12, -4, 0, 4, 4, 0, 0, false);

		headlight_2a_r1 = new ModelMapper(modelDataWrapper);
		headlight_2a_r1.setPos(12, -4, -42.1F);
		headlights.addChild(headlight_2a_r1);
		setRotationAngle(headlight_2a_r1, -0.3491F, -0.3491F, 0);
		headlight_2a_r1.texOffs(20, 0).addBox(0, -4, 0, 6, 4, 0, 0, true);

		headlight_1a_r1 = new ModelMapper(modelDataWrapper);
		headlight_1a_r1.setPos(-12, -4, -42.1F);
		headlights.addChild(headlight_1a_r1);
		setRotationAngle(headlight_1a_r1, -0.3491F, 0.3491F, 0);
		headlight_1a_r1.texOffs(20, 0).addBox(-6, -4, 0, 6, 4, 0, 0, false);

		tail_lights = new ModelMapper(modelDataWrapper);
		tail_lights.setPos(0, 24, 0);


		tail_light_2a_r1 = new ModelMapper(modelDataWrapper);
		tail_light_2a_r1.setPos(12, -4, -42);
		tail_lights.addChild(tail_light_2a_r1);
		setRotationAngle(tail_light_2a_r1, -0.3491F, -0.3491F, 0);
		tail_light_2a_r1.texOffs(20, 8).addBox(0, -4, -0.1F, 6, 4, 0, 0, true);

		tail_light_1a_r1 = new ModelMapper(modelDataWrapper);
		tail_light_1a_r1.setPos(-12, -4, -42);
		tail_lights.addChild(tail_light_1a_r1);
		setRotationAngle(tail_light_1a_r1, -0.3491F, 0.3491F, 0);
		tail_light_1a_r1.texOffs(20, 8).addBox(-6, -4, -0.1F, 6, 4, 0, 0, false);

		seat = new ModelMapper(modelDataWrapper);
		seat.setPos(0, 24, 0);
		seat.texOffs(16, 34).addBox(-3, -5, -3, 6, 1, 6, 0, false);
		seat.texOffs(24, 52).addBox(-1.5F, -16.4F, 4.5F, 3, 2, 1, 0, false);

		top_right_r1 = new ModelMapper(modelDataWrapper);
		top_right_r1.setPos(-1.5F, -16.4F, 4.5F);
		seat.addChild(top_right_r1);
		setRotationAngle(top_right_r1, 0.0017F, 0, 0.2618F);
		top_right_r1.texOffs(32, 52).addBox(0, 0, 0, 1, 2, 1, 0, false);

		top_left_r1 = new ModelMapper(modelDataWrapper);
		top_left_r1.setPos(1.5F, -16.4F, 4.5F);
		seat.addChild(top_left_r1);
		setRotationAngle(top_left_r1, 0.0017F, 0, -0.2618F);
		top_left_r1.texOffs(32, 52).addBox(-1, 0, 0, 1, 2, 1, 0, true);

		back_right_r1 = new ModelMapper(modelDataWrapper);
		back_right_r1.setPos(-1.5F, -5, 2);
		seat.addChild(back_right_r1);
		setRotationAngle(back_right_r1, -0.2618F, -0.1745F, 0.0873F);
		back_right_r1.texOffs(32, 41).addBox(-1.5F, -9.75F, 0, 2, 10, 1, 0, false);

		back_left_r1 = new ModelMapper(modelDataWrapper);
		back_left_r1.setPos(1.5F, -5, 2);
		seat.addChild(back_left_r1);
		setRotationAngle(back_left_r1, -0.2618F, 0.1745F, -0.0873F);
		back_left_r1.texOffs(32, 41).addBox(-0.5F, -9.75F, 0, 2, 10, 1, 0, true);

		back_r1 = new ModelMapper(modelDataWrapper);
		back_r1.setPos(3, -5, 2);
		seat.addChild(back_r1);
		setRotationAngle(back_r1, -0.2618F, 0, 0);
		back_r1.texOffs(24, 41).addBox(-4.5F, -10, 0, 3, 10, 1, 0, true);

		door_light_on = new ModelMapper(modelDataWrapper);
		door_light_on.setPos(0, 24, 0);


		light_r1 = new ModelMapper(modelDataWrapper);
		light_r1.setPos(-21, -14, 0);
		door_light_on.addChild(light_r1);
		setRotationAngle(light_r1, 0, 0, 0.1396F);
		light_r1.texOffs(20, 15).addBox(0, -21.5F, -0.5F, 0, 0, 1, 0.4F, false);

		door_light_off = new ModelMapper(modelDataWrapper);
		door_light_off.setPos(0, 24, 0);


		light_r2 = new ModelMapper(modelDataWrapper);
		light_r2.setPos(-21, -14, 0);
		door_light_off.addChild(light_r2);
		setRotationAngle(light_r2, 0, 0, 0.1396F);
		light_r2.texOffs(23, 15).addBox(0, -21.5F, -0.5F, 0, 0, 1, 0.4F, false);

		modelDataWrapper.setModelPart(textureWidth, textureHeight);
		window_tcl.setModelPart();
		window_tcl_handrails.setModelPart();
		window_ael.setModelPart();
		window_exterior_tcl.setModelPart();
		window_exterior_ael.setModelPart();
		window_exterior_end_tcl.setModelPart();
		window_exterior_end_ael.setModelPart();
		side_panel_tcl.setModelPart();
		side_panel_tcl_translucent.setModelPart();
		side_panel_ael.setModelPart();
		side_panel_ael_translucent.setModelPart();
		roof_window_tcl.setModelPart();
		roof_window_ael.setModelPart();
		roof_door_tcl.setModelPart();
		roof_door_ael.setModelPart();
		roof_exterior.setModelPart();
		door_tcl.setModelPart();
		door_left_tcl.setModelPart(door_tcl.name);
		door_right_tcl.setModelPart(door_tcl.name);
		door_tcl_handrail.setModelPart();
		door_ael.setModelPart();
		door_left_ael.setModelPart(door_ael.name);
		door_right_ael.setModelPart(door_ael.name);
		door_ael_handrail.setModelPart();
		door_exterior_tcl.setModelPart();
		door_left_exterior_tcl.setModelPart(door_exterior_tcl.name);
		door_right_exterior_tcl.setModelPart(door_exterior_tcl.name);
		door_exterior_ael.setModelPart();
		door_left_exterior_ael.setModelPart(door_exterior_ael.name);
		door_right_exterior_ael.setModelPart(door_exterior_ael.name);
		door_exterior_end.setModelPart();
		door_left_exterior_end.setModelPart(door_exterior_end.name);
		door_right_exterior_end.setModelPart(door_exterior_end.name);
		luggage_rack.setModelPart();
		end_tcl.setModelPart();
		end_ael.setModelPart();
		end_exterior_tcl.setModelPart();
		end_exterior_ael.setModelPart();
		end_door_ael.setModelPart();
		roof_end.setModelPart();
		roof_end_exterior.setModelPart();
		roof_light_tcl.setModelPart();
		roof_light_door_ael.setModelPart();
		roof_light_window_ael.setModelPart();
		roof_end_light.setModelPart();
		head_tcl.setModelPart();
		head_ael.setModelPart();
		head_exterior.setModelPart();
		headlights.setModelPart();
		tail_lights.setModelPart();
		seat.setModelPart();
		door_light_on.setModelPart();
		door_light_off.setModelPart();
	}

	private static final int DOOR_MAX_TCL = 14;
	private static final int DOOR_MAX_AEL = 11;
	private static final ModelDoorOverlay MODEL_DOOR_OVERLAY = new ModelDoorOverlay(DOOR_MAX_TCL, 8, "door_overlay_a_train_tcl_left.png", "door_overlay_a_train_tcl_right.png");

	@Override
	protected void renderWindowPositions(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
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
	protected void renderDoorPositions(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
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
					door_left_ael.setOffset(doorRightX, 0, doorRightZ);
					door_right_ael.setOffset(doorRightX, 0, -doorRightZ);
					renderOnce(door_ael, matrices, vertices, light, position);
					door_left_ael.setOffset(doorLeftX, 0, doorLeftZ);
					door_right_ael.setOffset(doorLeftX, 0, -doorLeftZ);
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
					door_left_tcl.setOffset(doorRightX, 0, doorRightZ);
					door_right_tcl.setOffset(doorRightX, 0, -doorRightZ);
					renderOnce(door_tcl, matrices, vertices, light, position);
					door_left_tcl.setOffset(doorLeftX, 0, doorLeftZ);
					door_right_tcl.setOffset(doorLeftX, 0, -doorLeftZ);
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
					door_left_exterior_ael.setOffset(doorRightX, 0, doorRightZ);
					door_right_exterior_ael.setOffset(doorRightX, 0, -doorRightZ);
					renderOnce(door_exterior_ael, matrices, vertices, light, position);
					door_left_exterior_ael.setOffset(doorLeftX, 0, doorLeftZ);
					door_right_exterior_ael.setOffset(doorLeftX, 0, -doorLeftZ);
					renderOnceFlipped(door_exterior_ael, matrices, vertices, light, position);
					renderMirror(roof_exterior, matrices, vertices, light, position - 2);
					renderMirror(roof_exterior, matrices, vertices, light, position + 2);
				} else {
					final boolean door1End = isIndex(0, position, getDoorPositions()) && isEnd1Head;
					final boolean door2End = isIndex(-1, position, getDoorPositions()) && isEnd2Head;

					if (door1End || door2End) {
						door_left_exterior_end.setOffset(doorRightX, 0, doorRightZ);
						door_right_exterior_end.setOffset(doorRightX, 0, -doorRightZ);
						renderOnce(door_exterior_end, matrices, vertices, light, position);
					} else {
						door_left_exterior_tcl.setOffset(doorRightX, 0, doorRightZ);
						door_right_exterior_tcl.setOffset(doorRightX, 0, -doorRightZ);
						renderOnce(door_exterior_tcl, matrices, vertices, light, position);
					}

					if (door1End || door2End) {
						door_left_exterior_end.setOffset(doorLeftX, 0, doorLeftZ);
						door_right_exterior_end.setOffset(doorLeftX, 0, -doorLeftZ);
						renderOnceFlipped(door_exterior_end, matrices, vertices, light, position);
					} else {
						door_left_exterior_tcl.setOffset(doorLeftX, 0, doorLeftZ);
						door_right_exterior_tcl.setOffset(doorLeftX, 0, -doorLeftZ);
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
	protected void renderHeadPosition1(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean useHeadlights) {
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
	protected void renderHeadPosition2(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean useHeadlights) {
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
	protected void renderEndPosition1(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
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
	protected void renderEndPosition2(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
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
	protected int[] getBogiePositions() {
		return new int[]{-136, 136};
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