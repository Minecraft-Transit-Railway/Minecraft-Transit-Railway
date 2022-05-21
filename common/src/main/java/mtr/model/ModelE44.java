package mtr.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mtr.mappings.ModelDataWrapper;
import mtr.mappings.ModelMapper;

public class ModelE44 extends ModelSimpleTrainBase {

	private final ModelMapper window;
	private final ModelMapper upper_wall_r1;
	private final ModelMapper window_exterior;
	private final ModelMapper upper_wall_r2;
	private final ModelMapper window_handrails;
	private final ModelMapper handrail_4_r1;
	private final ModelMapper handrail_1_r1;
	private final ModelMapper seat_7;
	private final ModelMapper seat_back_r1;
	private final ModelMapper seat_bottom_r1;
	private final ModelMapper seat_8;
	private final ModelMapper seat_back_r2;
	private final ModelMapper door;
	private final ModelMapper upper_wall_2_r1;
	private final ModelMapper door_left;
	private final ModelMapper door_left_top_r1;
	private final ModelMapper door_right;
	private final ModelMapper door_right_top_r1;
	private final ModelMapper door_exterior;
	private final ModelMapper upper_wall_2_r2;
	private final ModelMapper door_left_exterior;
	private final ModelMapper door_left_top_r2;
	private final ModelMapper door_right_exterior;
	private final ModelMapper door_right_top_r2;
	private final ModelMapper door_handrails;
	private final ModelMapper handrail_1_r2;
	private final ModelMapper side_panel;
	private final ModelMapper handrail_11_r1;
	private final ModelMapper handrail_5_r1;
	private final ModelMapper side_panel_translucent;
	private final ModelMapper roof_window;
	private final ModelMapper inner_roof_4_r1;
	private final ModelMapper inner_roof_2_r1;
	private final ModelMapper inner_roof_1_r1;
	private final ModelMapper roof_door;
	private final ModelMapper inner_roof_6_r1;
	private final ModelMapper inner_roof_4_r2;
	private final ModelMapper inner_roof_3_r1;
	private final ModelMapper inner_roof_2_r2;
	private final ModelMapper roof_window_light;
	private final ModelMapper roof_door_light;
	private final ModelMapper roof_exterior_window;
	private final ModelMapper outer_roof_5_r1;
	private final ModelMapper outer_roof_4_r1;
	private final ModelMapper outer_roof_3_r1;
	private final ModelMapper outer_roof_2_r1;
	private final ModelMapper outer_roof_1_r1;
	private final ModelMapper roof_exterior_door;
	private final ModelMapper outer_roof_6_r1;
	private final ModelMapper outer_roof_5_r2;
	private final ModelMapper outer_roof_4_r2;
	private final ModelMapper outer_roof_3_r2;
	private final ModelMapper outer_roof_2_r2;
	private final ModelMapper end;
	private final ModelMapper upper_wall_2_r3;
	private final ModelMapper upper_wall_1_r1;
	private final ModelMapper seat_1;
	private final ModelMapper seat_back_r3;
	private final ModelMapper seat_bottom_r2;
	private final ModelMapper seat_2;
	private final ModelMapper seat_back_r4;
	private final ModelMapper seat_bottom_r3;
	private final ModelMapper seat_3;
	private final ModelMapper seat_back_r5;
	private final ModelMapper seat_4;
	private final ModelMapper seat_back_r6;
	private final ModelMapper seat_bottom_r4;
	private final ModelMapper seat_5;
	private final ModelMapper seat_back_r7;
	private final ModelMapper seat_bottom_r5;
	private final ModelMapper seat_6;
	private final ModelMapper seat_back_r8;
	private final ModelMapper end_exterior;
	private final ModelMapper upper_wall_2_r4;
	private final ModelMapper upper_wall_1_r2;
	private final ModelMapper roof_end;
	private final ModelMapper inner_roof_5_r1;
	private final ModelMapper inner_roof_3_r2;
	private final ModelMapper inner_roof_2_r3;
	private final ModelMapper inner_roof_4_r3;
	private final ModelMapper inner_roof_2_r4;
	private final ModelMapper inner_roof_1_r2;
	private final ModelMapper roof_end_light;
	private final ModelMapper roof_end_handrails;
	private final ModelMapper handrail_11_r2;
	private final ModelMapper handrail_10_r1;
	private final ModelMapper roof_end_exterior;
	private final ModelMapper outer_roof_1;
	private final ModelMapper upper_wall_1_r3;
	private final ModelMapper outer_roof_5_r3;
	private final ModelMapper outer_roof_4_r3;
	private final ModelMapper outer_roof_3_r3;
	private final ModelMapper outer_roof_2_r3;
	private final ModelMapper outer_roof_2;
	private final ModelMapper outer_roof_5_r4;
	private final ModelMapper outer_roof_4_r4;
	private final ModelMapper outer_roof_3_r4;
	private final ModelMapper outer_roof_2_r4;
	private final ModelMapper outer_roof_1_r2;
	private final ModelMapper roof_end_vents;
	private final ModelMapper vent_3_r1;
	private final ModelMapper vent_2_r1;
	private final ModelMapper head;
	private final ModelMapper upper_wall_2_r5;
	private final ModelMapper upper_wall_1_r4;
	private final ModelMapper seat_9;
	private final ModelMapper seat_back_r9;
	private final ModelMapper seat_10;
	private final ModelMapper seat_back_r10;
	private final ModelMapper seat_11;
	private final ModelMapper seat_back_r11;
	private final ModelMapper seat_12;
	private final ModelMapper seat_back_r12;
	private final ModelMapper head_exterior;
	private final ModelMapper driver_door_upper_2_r1;
	private final ModelMapper upper_wall_2_r6;
	private final ModelMapper driver_door_upper_1_r1;
	private final ModelMapper upper_wall_1_r5;
	private final ModelMapper front;
	private final ModelMapper head_roof_r1;
	private final ModelMapper head_top_r1;
	private final ModelMapper head_bottom_1_r1;
	private final ModelMapper side_1;
	private final ModelMapper outer_head_4_r1;
	private final ModelMapper outer_head_2_r1;
	private final ModelMapper outer_head_1_r1;
	private final ModelMapper outer_roof_1_r3;
	private final ModelMapper outer_roof_3_r5;
	private final ModelMapper outer_roof_2_r5;
	private final ModelMapper outer_roof_4_r5;
	private final ModelMapper outer_roof_11_r1;
	private final ModelMapper outer_head_6_r1;
	private final ModelMapper outer_roof_5_r5;
	private final ModelMapper outer_head_5_r1;
	private final ModelMapper side_2;
	private final ModelMapper outer_head_6_r2;
	private final ModelMapper outer_head_5_r2;
	private final ModelMapper outer_head_4_r2;
	private final ModelMapper outer_head_2_r2;
	private final ModelMapper outer_head_1_r2;
	private final ModelMapper outer_roof_11_r2;
	private final ModelMapper outer_roof_5_r6;
	private final ModelMapper outer_roof_4_r6;
	private final ModelMapper outer_roof_3_r6;
	private final ModelMapper outer_roof_2_r6;
	private final ModelMapper outer_roof_1_r4;
	private final ModelMapper emergency_door;
	private final ModelMapper pipe;
	private final ModelMapper valve_8_r1;
	private final ModelMapper valve_7_r1;
	private final ModelMapper valve_6_r1;
	private final ModelMapper valve_5_r1;
	private final ModelMapper valve_4_r1;
	private final ModelMapper valve_3_r1;
	private final ModelMapper valve_2_r1;
	private final ModelMapper valve_1_r1;
	private final ModelMapper headlights;
	private final ModelMapper outer_head_5_r3;
	private final ModelMapper tail_lights;
	private final ModelMapper tail_light_r1;
	private final ModelMapper door_light;
	private final ModelMapper outer_roof_1_r5;
	private final ModelMapper door_light_off;
	private final ModelMapper light_r1;
	private final ModelMapper door_light_on;
	private final ModelMapper light_r2;

	public ModelE44() {
		final int textureWidth = 336;
		final int textureHeight = 336;

		final ModelDataWrapper modelDataWrapper = new ModelDataWrapper(this, textureWidth, textureHeight);

		window = new ModelMapper(modelDataWrapper);
		window.setPos(0, 24, 0);
		window.texOffs(191, 39).addBox(-20, 0, -15, 20, 1, 30, 0, false);
		window.texOffs(36, 271).addBox(-20, -13, -15, 2, 13, 30, 0, false);

		upper_wall_r1 = new ModelMapper(modelDataWrapper);
		upper_wall_r1.setPos(-20, -13, 0);
		window.addChild(upper_wall_r1);
		setRotationAngle(upper_wall_r1, 0, 0, 0.1107F);
		upper_wall_r1.texOffs(245, 247).addBox(0, -20, -15, 2, 20, 30, 0, false);

		window_exterior = new ModelMapper(modelDataWrapper);
		window_exterior.setPos(0, 24, 0);
		window_exterior.texOffs(100, 271).addBox(-21, 0, -15, 1, 2, 30, 0, false);
		window_exterior.texOffs(94, 221).addBox(-20, -13, -15, 0, 13, 30, 0, false);

		upper_wall_r2 = new ModelMapper(modelDataWrapper);
		upper_wall_r2.setPos(-20, -13, 0);
		window_exterior.addChild(upper_wall_r2);
		setRotationAngle(upper_wall_r2, 0, 0, 0.1107F);
		upper_wall_r2.texOffs(214, 217).addBox(0, -20, -15, 0, 20, 30, 0, false);

		window_handrails = new ModelMapper(modelDataWrapper);
		window_handrails.setPos(0, 24, 0);
		window_handrails.texOffs(4, 41).addBox(-9, -31, 7, 2, 4, 0, 0, false);
		window_handrails.texOffs(4, 41).addBox(-9, -31, 0, 2, 4, 0, 0, false);
		window_handrails.texOffs(4, 41).addBox(-9, -31, -7, 2, 4, 0, 0, false);

		handrail_4_r1 = new ModelMapper(modelDataWrapper);
		handrail_4_r1.setPos(-7.8F, -30.7F, 16.2F);
		window_handrails.addChild(handrail_4_r1);
		setRotationAngle(handrail_4_r1, 0, 0, -0.0873F);
		handrail_4_r1.texOffs(0, 0).addBox(-0.2F, 0.2F, -1.2F, 0, 18, 0, 0.2F, false);
		handrail_4_r1.texOffs(0, 0).addBox(-0.2F, 0.2F, -31.2F, 0, 18, 0, 0.2F, false);

		handrail_1_r1 = new ModelMapper(modelDataWrapper);
		handrail_1_r1.setPos(0, 0, 0);
		window_handrails.addChild(handrail_1_r1);
		setRotationAngle(handrail_1_r1, -1.5708F, 0, 0);
		handrail_1_r1.texOffs(0, 0).addBox(-8, -15, -30.5F, 0, 30, 0, 0.2F, false);

		seat_7 = new ModelMapper(modelDataWrapper);
		seat_7.setPos(0, 0, 0);
		window_handrails.addChild(seat_7);


		seat_back_r1 = new ModelMapper(modelDataWrapper);
		seat_back_r1.setPos(0, -6, -13.5F);
		seat_7.addChild(seat_back_r1);
		setRotationAngle(seat_back_r1, -0.0524F, 3.1416F, 0);
		seat_back_r1.texOffs(53, 138).addBox(6, -8, 0, 12, 8, 1, 0, false);

		seat_bottom_r1 = new ModelMapper(modelDataWrapper);
		seat_bottom_r1.setPos(0, 0, 8);
		seat_7.addChild(seat_bottom_r1);
		setRotationAngle(seat_bottom_r1, 0, 3.1416F, 0);
		seat_bottom_r1.texOffs(0, 85).addBox(6, -6, 15.5F, 12, 1, 7, 0, false);

		seat_8 = new ModelMapper(modelDataWrapper);
		seat_8.setPos(-24, 0, 2);
		window_handrails.addChild(seat_8);
		seat_8.texOffs(0, 85).addBox(6, -6, 5.5F, 12, 1, 7, 0, false);

		seat_back_r2 = new ModelMapper(modelDataWrapper);
		seat_back_r2.setPos(0, -6, 11.5F);
		seat_8.addChild(seat_back_r2);
		setRotationAngle(seat_back_r2, -0.0524F, 0, 0);
		seat_back_r2.texOffs(53, 138).addBox(6, -8, 0, 12, 8, 1, 0, false);

		door = new ModelMapper(modelDataWrapper);
		door.setPos(0, 24, 0);
		door.texOffs(125, 0).addBox(-20, 0, -19, 20, 1, 38, 0, false);
		door.texOffs(154, 246).addBox(-20, -13, -19, 2, 13, 5, 0, false);
		door.texOffs(239, 15).addBox(-20, -13, 14, 2, 13, 5, 0, false);

		upper_wall_2_r1 = new ModelMapper(modelDataWrapper);
		upper_wall_2_r1.setPos(-20, -13, 0);
		door.addChild(upper_wall_2_r1);
		setRotationAngle(upper_wall_2_r1, 0, 0, 0.1107F);
		upper_wall_2_r1.texOffs(196, 240).addBox(0, -20, 14, 2, 20, 5, 0, false);
		upper_wall_2_r1.texOffs(114, 271).addBox(0, -20, -19, 2, 20, 5, 0, false);

		door_left = new ModelMapper(modelDataWrapper);
		door_left.setPos(0, 0, 0);
		door.addChild(door_left);
		door_left.texOffs(47, 187).addBox(-20.8F, -13, 0, 1, 13, 15, 0, false);

		door_left_top_r1 = new ModelMapper(modelDataWrapper);
		door_left_top_r1.setPos(-20.8F, -13, 0);
		door_left.addChild(door_left_top_r1);
		setRotationAngle(door_left_top_r1, 0, 0, 0.1107F);
		door_left_top_r1.texOffs(132, 91).addBox(0, -20, 0, 1, 20, 15, 0, false);

		door_right = new ModelMapper(modelDataWrapper);
		door_right.setPos(0, 0, 0);
		door.addChild(door_right);
		door_right.texOffs(0, 187).addBox(-20.8F, -13, -15, 1, 13, 15, 0, false);

		door_right_top_r1 = new ModelMapper(modelDataWrapper);
		door_right_top_r1.setPos(-20.8F, -13, 0);
		door_right.addChild(door_right_top_r1);
		setRotationAngle(door_right_top_r1, 0, 0, 0.1107F);
		door_right_top_r1.texOffs(0, 96).addBox(0, -20, -15, 1, 20, 15, 0, false);

		door_exterior = new ModelMapper(modelDataWrapper);
		door_exterior.setPos(0, 24, 0);
		door_exterior.texOffs(132, 231).addBox(-21, 0, -19, 1, 2, 38, 0, false);
		door_exterior.texOffs(0, 91).addBox(-20, -13, -19, 0, 13, 5, 0, false);
		door_exterior.texOffs(0, 182).addBox(-20, -13, 14, 0, 13, 5, 0, false);

		upper_wall_2_r2 = new ModelMapper(modelDataWrapper);
		upper_wall_2_r2.setPos(-20, -13, 0);
		door_exterior.addChild(upper_wall_2_r2);
		setRotationAngle(upper_wall_2_r2, 0, 0, 0.1107F);
		upper_wall_2_r2.texOffs(32, 197).addBox(0, -20, 14, 0, 20, 5, 0, false);
		upper_wall_2_r2.texOffs(164, 99).addBox(0, -20, -19, 0, 20, 5, 0, false);

		door_left_exterior = new ModelMapper(modelDataWrapper);
		door_left_exterior.setPos(0, 0, 0);
		door_exterior.addChild(door_left_exterior);
		door_left_exterior.texOffs(149, 76).addBox(-20.8F, -13, 0, 0, 13, 15, 0, false);

		door_left_top_r2 = new ModelMapper(modelDataWrapper);
		door_left_top_r2.setPos(-20.8F, -13, 0);
		door_left_exterior.addChild(door_left_top_r2);
		setRotationAngle(door_left_top_r2, 0, 0, 0.1107F);
		door_left_top_r2.texOffs(166, 167).addBox(0, -20, 0, 0, 20, 15, 0, false);

		door_right_exterior = new ModelMapper(modelDataWrapper);
		door_right_exterior.setPos(0, 0, 0);
		door_exterior.addChild(door_right_exterior);
		door_right_exterior.texOffs(0, 116).addBox(-20.8F, -13, -15, 0, 13, 15, 0, false);

		door_right_top_r2 = new ModelMapper(modelDataWrapper);
		door_right_top_r2.setPos(-20.8F, -13, 0);
		door_right_exterior.addChild(door_right_top_r2);
		setRotationAngle(door_right_top_r2, 0, 0, 0.1107F);
		door_right_top_r2.texOffs(77, 81).addBox(0, -20, -15, 0, 20, 15, 0, false);

		door_handrails = new ModelMapper(modelDataWrapper);
		door_handrails.setPos(0, 24, 0);


		handrail_1_r2 = new ModelMapper(modelDataWrapper);
		handrail_1_r2.setPos(0, 0, 0);
		door_handrails.addChild(handrail_1_r2);
		setRotationAngle(handrail_1_r2, -1.5708F, 0, 0);
		handrail_1_r2.texOffs(0, 0).addBox(-8, -19, -30.5F, 0, 38, 0, 0.2F, false);

		side_panel = new ModelMapper(modelDataWrapper);
		side_panel.setPos(0, 24, 0);
		side_panel.texOffs(287, 164).addBox(-18, -15, 0, 13, 15, 0, 0, false);

		handrail_11_r1 = new ModelMapper(modelDataWrapper);
		handrail_11_r1.setPos(0, 0, 0);
		side_panel.addChild(handrail_11_r1);
		setRotationAngle(handrail_11_r1, 0, 0, 0);
		handrail_11_r1.texOffs(0, 0).addBox(-6.3963F, -12.17F, 0, 0, 13, 0, 0.2F, false);

		handrail_5_r1 = new ModelMapper(modelDataWrapper);
		handrail_5_r1.setPos(-7.8F, -30.7F, 0);
		side_panel.addChild(handrail_5_r1);
		setRotationAngle(handrail_5_r1, 0, 0, -0.0873F);
		handrail_5_r1.texOffs(0, 0).addBox(-0.2F, 0.2F, 0, 0, 18, 0, 0.2F, false);

		side_panel_translucent = new ModelMapper(modelDataWrapper);
		side_panel_translucent.setPos(0, 24, 0);
		side_panel_translucent.texOffs(287, 148).addBox(-18, -31, 0, 13, 16, 0, 0, false);

		roof_window = new ModelMapper(modelDataWrapper);
		roof_window.setPos(0, 24, 0);
		roof_window.texOffs(50, 46).addBox(-9.775F, -32.575F, -15, 7, 0, 30, 0, false);
		roof_window.texOffs(0, 0).addBox(-3, -33.575F, -15, 3, 0, 30, 0, false);

		inner_roof_4_r1 = new ModelMapper(modelDataWrapper);
		inner_roof_4_r1.setPos(-2.775F, -33.076F, 0);
		roof_window.addChild(inner_roof_4_r1);
		setRotationAngle(inner_roof_4_r1, 0, 0, 1.5708F);
		inner_roof_4_r1.texOffs(6, 0).addBox(-0.5F, 0, -15, 1, 0, 30, 0, false);

		inner_roof_2_r1 = new ModelMapper(modelDataWrapper);
		inner_roof_2_r1.setPos(-9.775F, -32.575F, 0);
		roof_window.addChild(inner_roof_2_r1);
		setRotationAngle(inner_roof_2_r1, 0, 0, 1.309F);
		inner_roof_2_r1.texOffs(0, 46).addBox(-2, 0, -15, 2, 0, 30, 0, false);

		inner_roof_1_r1 = new ModelMapper(modelDataWrapper);
		inner_roof_1_r1.setPos(0, 0, 0);
		roof_window.addChild(inner_roof_1_r1);
		setRotationAngle(inner_roof_1_r1, 0, 0, -0.3491F);
		inner_roof_1_r1.texOffs(64, 46).addBox(-4.75F, -35, -15, 7, 0, 30, 0, false);

		roof_door = new ModelMapper(modelDataWrapper);
		roof_door.setPos(0, 24, 0);
		roof_door.texOffs(215, 208).addBox(-18, -32, -19, 3, 1, 38, 0, false);
		roof_door.texOffs(103, 0).addBox(-9.775F, -32.575F, -19, 7, 0, 38, 0, false);
		roof_door.texOffs(166, 169).addBox(-3, -33.575F, -19, 3, 0, 38, 0, false);

		inner_roof_6_r1 = new ModelMapper(modelDataWrapper);
		inner_roof_6_r1.setPos(-2.775F, -33.076F, 0);
		roof_door.addChild(inner_roof_6_r1);
		setRotationAngle(inner_roof_6_r1, 0, 0, 1.5708F);
		inner_roof_6_r1.texOffs(8, 96).addBox(-0.5F, 0, -19, 1, 0, 38, 0, false);

		inner_roof_4_r2 = new ModelMapper(modelDataWrapper);
		inner_roof_4_r2.setPos(-9.775F, -32.575F, 0);
		roof_door.addChild(inner_roof_4_r2);
		setRotationAngle(inner_roof_4_r2, 0, 0, 1.309F);
		inner_roof_4_r2.texOffs(0, 96).addBox(-2, 0, -19, 2, 0, 38, 0, false);

		inner_roof_3_r1 = new ModelMapper(modelDataWrapper);
		inner_roof_3_r1.setPos(0, 0, 0);
		roof_door.addChild(inner_roof_3_r1);
		setRotationAngle(inner_roof_3_r1, 0, 0, -0.3491F);
		inner_roof_3_r1.texOffs(0, 46).addBox(0.25F, -35, -19, 2, 0, 38, 0, false);

		inner_roof_2_r2 = new ModelMapper(modelDataWrapper);
		inner_roof_2_r2.setPos(-13.863F, -32.0306F, 0);
		roof_door.addChild(inner_roof_2_r2);
		setRotationAngle(inner_roof_2_r2, 0, 0, -0.5236F);
		inner_roof_2_r2.texOffs(204, 169).addBox(-1.5F, -0.675F, -19, 5, 1, 38, 0, false);

		roof_window_light = new ModelMapper(modelDataWrapper);
		roof_window_light.setPos(0, 24, 0);
		roof_window_light.texOffs(37, 102).addBox(-2.5F, -33.1F, -15, 5, 0, 30, 0, false);

		roof_door_light = new ModelMapper(modelDataWrapper);
		roof_door_light.setPos(0, 24, 0);
		roof_door_light.texOffs(29, 98).addBox(-2.5F, -33.1F, -19, 5, 0, 38, 0, false);

		roof_exterior_window = new ModelMapper(modelDataWrapper);
		roof_exterior_window.setPos(0, 24, 0);
		roof_exterior_window.texOffs(173, 0).addBox(-6, -42, -15, 6, 0, 30, 0, false);

		outer_roof_5_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_5_r1.setPos(-9.9394F, -41.3064F, 0);
		roof_exterior_window.addChild(outer_roof_5_r1);
		setRotationAngle(outer_roof_5_r1, 0, 0, -0.1745F);
		outer_roof_5_r1.texOffs(95, 0).addBox(-4, 0, -15, 8, 0, 30, 0, false);

		outer_roof_4_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_4_r1.setPos(-15.1778F, -39.8628F, 0);
		roof_exterior_window.addChild(outer_roof_4_r1);
		setRotationAngle(outer_roof_4_r1, 0, 0, -0.5236F);
		outer_roof_4_r1.texOffs(125, 0).addBox(-1.5F, 0, -15, 3, 0, 30, 0, false);

		outer_roof_3_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_3_r1.setPos(-16.9769F, -38.2468F, 0);
		roof_exterior_window.addChild(outer_roof_3_r1);
		setRotationAngle(outer_roof_3_r1, 0, 0, -1.0472F);
		outer_roof_3_r1.texOffs(8, 0).addBox(-1, 0, -15, 2, 0, 30, 0, false);

		outer_roof_2_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_2_r1.setPos(-17.5872F, -36.3872F, 0);
		roof_exterior_window.addChild(outer_roof_2_r1);
		setRotationAngle(outer_roof_2_r1, 0, 0, 0.1107F);
		outer_roof_2_r1.texOffs(133, 9).addBox(0, -1, -15, 0, 2, 30, 0, false);

		outer_roof_1_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_1_r1.setPos(-20, -13, 0);
		roof_exterior_window.addChild(outer_roof_1_r1);
		setRotationAngle(outer_roof_1_r1, 0, 0, 0.1107F);
		outer_roof_1_r1.texOffs(62, 237).addBox(-1, -23, -15, 1, 4, 30, 0, false);

		roof_exterior_door = new ModelMapper(modelDataWrapper);
		roof_exterior_door.setPos(0, 24, 0);
		roof_exterior_door.texOffs(165, 0).addBox(-6, -42, -19, 6, 0, 38, 0, false);

		outer_roof_6_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_6_r1.setPos(-9.9394F, -41.3064F, 0);
		roof_exterior_door.addChild(outer_roof_6_r1);
		setRotationAngle(outer_roof_6_r1, 0, 0, -0.1745F);
		outer_roof_6_r1.texOffs(87, 0).addBox(-4, 0, -19, 8, 0, 38, 0, false);

		outer_roof_5_r2 = new ModelMapper(modelDataWrapper);
		outer_roof_5_r2.setPos(-15.1778F, -39.8628F, 0);
		roof_exterior_door.addChild(outer_roof_5_r2);
		setRotationAngle(outer_roof_5_r2, 0, 0, -0.5236F);
		outer_roof_5_r2.texOffs(117, 0).addBox(-1.5F, 0, -19, 3, 0, 38, 0, false);

		outer_roof_4_r2 = new ModelMapper(modelDataWrapper);
		outer_roof_4_r2.setPos(-16.9769F, -38.2468F, 0);
		roof_exterior_door.addChild(outer_roof_4_r2);
		setRotationAngle(outer_roof_4_r2, 0, 0, -1.0472F);
		outer_roof_4_r2.texOffs(0, 0).addBox(-1, 0, -19, 2, 0, 38, 0, false);

		outer_roof_3_r2 = new ModelMapper(modelDataWrapper);
		outer_roof_3_r2.setPos(-17.5872F, -36.3872F, 0);
		roof_exterior_door.addChild(outer_roof_3_r2);
		setRotationAngle(outer_roof_3_r2, 0, 0, 0.1107F);
		outer_roof_3_r2.texOffs(125, 1).addBox(0, -1, -19, 0, 2, 38, 0, false);

		outer_roof_2_r2 = new ModelMapper(modelDataWrapper);
		outer_roof_2_r2.setPos(-20, -13, 0);
		roof_exterior_door.addChild(outer_roof_2_r2);
		setRotationAngle(outer_roof_2_r2, 0, 0, 0.1107F);
		outer_roof_2_r2.texOffs(54, 229).addBox(-1, -23, -19, 1, 4, 38, 0, false);

		end = new ModelMapper(modelDataWrapper);
		end.setPos(0, 24, 0);
		end.texOffs(0, 0).addBox(-20, 0, -32, 40, 1, 45, 0, false);
		end.texOffs(102, 120).addBox(-18, -13, -36, 0, 13, 49, 0, false);
		end.texOffs(132, 29).addBox(18, -13, -36, 0, 13, 49, 0, true);
		end.texOffs(150, 291).addBox(6, -32, -36, 12, 32, 12, 0, true);
		end.texOffs(198, 291).addBox(-18, -32, -36, 12, 32, 12, 0, false);
		end.texOffs(215, 0).addBox(-18, -35, -36, 36, 3, 12, 0, false);
		end.texOffs(0, 291).addBox(-6, -32, -24, 12, 32, 0, 0, false);

		upper_wall_2_r3 = new ModelMapper(modelDataWrapper);
		upper_wall_2_r3.setPos(-20, -13, 0);
		end.addChild(upper_wall_2_r3);
		setRotationAngle(upper_wall_2_r3, 0, 0, 0.1107F);
		upper_wall_2_r3.texOffs(102, 98).addBox(2, -20, -36, 0, 20, 49, 0, false);

		upper_wall_1_r1 = new ModelMapper(modelDataWrapper);
		upper_wall_1_r1.setPos(20, -13, 0);
		end.addChild(upper_wall_1_r1);
		setRotationAngle(upper_wall_1_r1, 0, 0, -0.1107F);
		upper_wall_1_r1.texOffs(0, 118).addBox(-2, -20, -36, 0, 20, 49, 0, true);

		seat_1 = new ModelMapper(modelDataWrapper);
		seat_1.setPos(0, 0, 0);
		end.addChild(seat_1);


		seat_back_r3 = new ModelMapper(modelDataWrapper);
		seat_back_r3.setPos(0, -6, -22);
		seat_1.addChild(seat_back_r3);
		setRotationAngle(seat_back_r3, -0.0524F, 3.1416F, 0);
		seat_back_r3.texOffs(53, 138).addBox(6, -8, 0, 12, 8, 1, 0, false);

		seat_bottom_r2 = new ModelMapper(modelDataWrapper);
		seat_bottom_r2.setPos(0, 0, 0);
		seat_1.addChild(seat_bottom_r2);
		setRotationAngle(seat_bottom_r2, 0, 3.1416F, 0);
		seat_bottom_r2.texOffs(0, 85).addBox(-18, -6, 16, 12, 1, 7, 0, false);

		seat_2 = new ModelMapper(modelDataWrapper);
		seat_2.setPos(0, 0, 0);
		end.addChild(seat_2);


		seat_back_r4 = new ModelMapper(modelDataWrapper);
		seat_back_r4.setPos(0, -6, -10);
		seat_2.addChild(seat_back_r4);
		setRotationAngle(seat_back_r4, -0.0524F, 3.1416F, 0);
		seat_back_r4.texOffs(53, 138).addBox(6, -8, 0, 12, 8, 1, 0, false);

		seat_bottom_r3 = new ModelMapper(modelDataWrapper);
		seat_bottom_r3.setPos(0, 0, 0);
		seat_2.addChild(seat_bottom_r3);
		setRotationAngle(seat_bottom_r3, 0, 3.1416F, 0);
		seat_bottom_r3.texOffs(0, 85).addBox(-18, -6, 4, 12, 1, 7, 0, false);

		seat_3 = new ModelMapper(modelDataWrapper);
		seat_3.setPos(0, 0, 0);
		end.addChild(seat_3);
		seat_3.texOffs(0, 85).addBox(6, -6, 5, 12, 1, 7, 0, false);

		seat_back_r5 = new ModelMapper(modelDataWrapper);
		seat_back_r5.setPos(0, -6, 11);
		seat_3.addChild(seat_back_r5);
		setRotationAngle(seat_back_r5, -0.0524F, 0, 0);
		seat_back_r5.texOffs(53, 138).addBox(6, -8, 0, 12, 8, 1, 0, false);

		seat_4 = new ModelMapper(modelDataWrapper);
		seat_4.setPos(0, 0, 0);
		end.addChild(seat_4);


		seat_back_r6 = new ModelMapper(modelDataWrapper);
		seat_back_r6.setPos(0, -6, -22);
		seat_4.addChild(seat_back_r6);
		setRotationAngle(seat_back_r6, -0.0524F, 3.1416F, 0);
		seat_back_r6.texOffs(53, 138).addBox(-18, -8, 0, 12, 8, 1, 0, true);

		seat_bottom_r4 = new ModelMapper(modelDataWrapper);
		seat_bottom_r4.setPos(0, 0, 0);
		seat_4.addChild(seat_bottom_r4);
		setRotationAngle(seat_bottom_r4, 0, 3.1416F, 0);
		seat_bottom_r4.texOffs(0, 85).addBox(6, -6, 16, 12, 1, 7, 0, true);

		seat_5 = new ModelMapper(modelDataWrapper);
		seat_5.setPos(0, 0, 0);
		end.addChild(seat_5);


		seat_back_r7 = new ModelMapper(modelDataWrapper);
		seat_back_r7.setPos(0, -6, -10);
		seat_5.addChild(seat_back_r7);
		setRotationAngle(seat_back_r7, -0.0524F, 3.1416F, 0);
		seat_back_r7.texOffs(53, 138).addBox(-18, -8, 0, 12, 8, 1, 0, true);

		seat_bottom_r5 = new ModelMapper(modelDataWrapper);
		seat_bottom_r5.setPos(0, 0, 0);
		seat_5.addChild(seat_bottom_r5);
		setRotationAngle(seat_bottom_r5, 0, 3.1416F, 0);
		seat_bottom_r5.texOffs(0, 85).addBox(6, -6, 4, 12, 1, 7, 0, true);

		seat_6 = new ModelMapper(modelDataWrapper);
		seat_6.setPos(0, 0, 0);
		end.addChild(seat_6);
		seat_6.texOffs(0, 85).addBox(-18, -6, 5, 12, 1, 7, 0, true);

		seat_back_r8 = new ModelMapper(modelDataWrapper);
		seat_back_r8.setPos(0, -6, 11);
		seat_6.addChild(seat_back_r8);
		setRotationAngle(seat_back_r8, -0.0524F, 0, 0);
		seat_back_r8.texOffs(53, 138).addBox(-18, -8, 0, 12, 8, 1, 0, true);

		end_exterior = new ModelMapper(modelDataWrapper);
		end_exterior.setPos(0, 24, 0);
		end_exterior.texOffs(0, 187).addBox(20, 0, -32, 1, 2, 45, 0, true);
		end_exterior.texOffs(0, 187).addBox(-21, 0, -32, 1, 2, 45, 0, false);
		end_exterior.texOffs(151, 169).addBox(18, -13, -36, 2, 13, 49, 0, true);
		end_exterior.texOffs(49, 167).addBox(-20, -13, -36, 2, 13, 49, 0, false);
		end_exterior.texOffs(59, 234).addBox(6, -33, -36, 12, 33, 0, 0, true);
		end_exterior.texOffs(35, 234).addBox(-18, -33, -36, 12, 33, 0, 0, false);
		end_exterior.texOffs(172, 231).addBox(-18, -41, -36, 36, 9, 0, 0, false);

		upper_wall_2_r4 = new ModelMapper(modelDataWrapper);
		upper_wall_2_r4.setPos(-20, -13, 0);
		end_exterior.addChild(upper_wall_2_r4);
		setRotationAngle(upper_wall_2_r4, 0, 0, 0.1107F);
		upper_wall_2_r4.texOffs(79, 78).addBox(0, -20, -36, 2, 20, 49, 0, false);

		upper_wall_1_r2 = new ModelMapper(modelDataWrapper);
		upper_wall_1_r2.setPos(20, -13, 0);
		end_exterior.addChild(upper_wall_1_r2);
		setRotationAngle(upper_wall_1_r2, 0, 0, -0.1107F);
		upper_wall_1_r2.texOffs(0, 98).addBox(-2, -20, -36, 2, 20, 49, 0, true);

		roof_end = new ModelMapper(modelDataWrapper);
		roof_end.setPos(0, 24, 0);
		roof_end.texOffs(16, 96).addBox(-9.775F, -32.575F, -24, 7, 0, 37, 0, false);
		roof_end.texOffs(5, 46).addBox(-3, -33.575F, -24, 3, 0, 37, 0, false);
		roof_end.texOffs(16, 96).addBox(2.775F, -32.575F, -24, 7, 0, 37, 0, true);
		roof_end.texOffs(5, 46).addBox(0, -33.575F, -24, 3, 0, 37, 0, true);

		inner_roof_5_r1 = new ModelMapper(modelDataWrapper);
		inner_roof_5_r1.setPos(2.775F, -33.076F, 0);
		roof_end.addChild(inner_roof_5_r1);
		setRotationAngle(inner_roof_5_r1, 0, 0, -1.5708F);
		inner_roof_5_r1.texOffs(5, 0).addBox(-0.5F, 0, -24, 1, 0, 37, 0, true);

		inner_roof_3_r2 = new ModelMapper(modelDataWrapper);
		inner_roof_3_r2.setPos(9.775F, -32.575F, 0);
		roof_end.addChild(inner_roof_3_r2);
		setRotationAngle(inner_roof_3_r2, 0, 0, -1.309F);
		inner_roof_3_r2.texOffs(5, 96).addBox(0, 0, -24, 2, 0, 37, 0, true);

		inner_roof_2_r3 = new ModelMapper(modelDataWrapper);
		inner_roof_2_r3.setPos(0, 0, 0);
		roof_end.addChild(inner_roof_2_r3);
		setRotationAngle(inner_roof_2_r3, 0, 0, 0.3491F);
		inner_roof_2_r3.texOffs(144, 91).addBox(-2.25F, -35, -24, 7, 0, 37, 0, true);

		inner_roof_4_r3 = new ModelMapper(modelDataWrapper);
		inner_roof_4_r3.setPos(-2.775F, -33.076F, 0);
		roof_end.addChild(inner_roof_4_r3);
		setRotationAngle(inner_roof_4_r3, 0, 0, 1.5708F);
		inner_roof_4_r3.texOffs(5, 0).addBox(-0.5F, 0, -24, 1, 0, 37, 0, false);

		inner_roof_2_r4 = new ModelMapper(modelDataWrapper);
		inner_roof_2_r4.setPos(-9.775F, -32.575F, 0);
		roof_end.addChild(inner_roof_2_r4);
		setRotationAngle(inner_roof_2_r4, 0, 0, 1.309F);
		inner_roof_2_r4.texOffs(5, 96).addBox(-2, 0, -24, 2, 0, 37, 0, false);

		inner_roof_1_r2 = new ModelMapper(modelDataWrapper);
		inner_roof_1_r2.setPos(0, 0, 0);
		roof_end.addChild(inner_roof_1_r2);
		setRotationAngle(inner_roof_1_r2, 0, 0, -0.3491F);
		inner_roof_1_r2.texOffs(144, 91).addBox(-4.75F, -35, -24, 7, 0, 37, 0, false);

		roof_end_light = new ModelMapper(modelDataWrapper);
		roof_end_light.setPos(0, 24, 0);
		roof_end_light.texOffs(25, 96).addBox(-2.5F, -33.1F, -24, 5, 0, 42, 0, false);

		roof_end_handrails = new ModelMapper(modelDataWrapper);
		roof_end_handrails.setPos(0, 24, 0);
		roof_end_handrails.texOffs(4, 41).addBox(-9, -31, 7, 2, 4, 0, 0, false);
		roof_end_handrails.texOffs(4, 41).addBox(-9, -31, 0, 2, 4, 0, 0, false);
		roof_end_handrails.texOffs(4, 41).addBox(-9, -31, -7, 2, 4, 0, 0, false);
		roof_end_handrails.texOffs(0, 0).addBox(-8, -33.4899F, -16.9899F, 0, 2, 0, 0.2F, false);
		roof_end_handrails.texOffs(4, 41).addBox(7, -31, 7, 2, 4, 0, 0, true);
		roof_end_handrails.texOffs(4, 41).addBox(7, -31, 0, 2, 4, 0, 0, true);
		roof_end_handrails.texOffs(4, 41).addBox(7, -31, -7, 2, 4, 0, 0, true);
		roof_end_handrails.texOffs(0, 0).addBox(8, -33.4899F, -16.9899F, 0, 2, 0, 0.2F, true);

		handrail_11_r2 = new ModelMapper(modelDataWrapper);
		handrail_11_r2.setPos(10, -30.9364F, -16.5536F);
		roof_end_handrails.addChild(handrail_11_r2);
		setRotationAngle(handrail_11_r2, 0.7854F, 0, 0);
		handrail_11_r2.texOffs(0, 0).addBox(-2, -0.5F, 0, 0, 1, 0, 0.2F, true);
		handrail_11_r2.texOffs(0, 0).addBox(-18, -0.5F, 0, 0, 1, 0, 0.2F, false);

		handrail_10_r1 = new ModelMapper(modelDataWrapper);
		handrail_10_r1.setPos(0, 0, 0);
		roof_end_handrails.addChild(handrail_10_r1);
		setRotationAngle(handrail_10_r1, -1.5708F, 0, 0);
		handrail_10_r1.texOffs(0, 0).addBox(8, -16, -30.5F, 0, 32, 0, 0.2F, true);
		handrail_10_r1.texOffs(0, 0).addBox(-8, -16, -30.5F, 0, 32, 0, 0.2F, false);

		roof_end_exterior = new ModelMapper(modelDataWrapper);
		roof_end_exterior.setPos(0, 24, 0);


		outer_roof_1 = new ModelMapper(modelDataWrapper);
		outer_roof_1.setPos(0, 0, 0);
		roof_end_exterior.addChild(outer_roof_1);
		outer_roof_1.texOffs(93, 79).addBox(-6, -42, -36, 6, 1, 11, 0, false);

		upper_wall_1_r3 = new ModelMapper(modelDataWrapper);
		upper_wall_1_r3.setPos(-20, -13, 0);
		outer_roof_1.addChild(upper_wall_1_r3);
		setRotationAngle(upper_wall_1_r3, 0, 0, 0.1107F);
		upper_wall_1_r3.texOffs(181, 133).addBox(0, -23, -36, 1, 4, 7, 0, false);
		upper_wall_1_r3.texOffs(34, 134).addBox(-1, -23, -29, 1, 4, 4, 0, false);

		outer_roof_5_r3 = new ModelMapper(modelDataWrapper);
		outer_roof_5_r3.setPos(-9.7656F, -40.3206F, 0);
		outer_roof_1.addChild(outer_roof_5_r3);
		setRotationAngle(outer_roof_5_r3, 0, 0, -0.1745F);
		outer_roof_5_r3.texOffs(155, 204).addBox(-4, -1, -36, 8, 1, 11, 0, false);

		outer_roof_4_r3 = new ModelMapper(modelDataWrapper);
		outer_roof_4_r3.setPos(-14.6775F, -38.9948F, 0);
		outer_roof_1.addChild(outer_roof_4_r3);
		setRotationAngle(outer_roof_4_r3, 0, 0, -0.5236F);
		outer_roof_4_r3.texOffs(17, 31).addBox(-1.5F, -1, -36, 3, 1, 11, 0, false);

		outer_roof_3_r3 = new ModelMapper(modelDataWrapper);
		outer_roof_3_r3.setPos(-16.1105F, -37.7448F, 0);
		outer_roof_1.addChild(outer_roof_3_r3);
		setRotationAngle(outer_roof_3_r3, 0, 0, -1.0472F);
		outer_roof_3_r3.texOffs(19, 133).addBox(-1, -1, -36, 2, 1, 11, 0, false);

		outer_roof_2_r3 = new ModelMapper(modelDataWrapper);
		outer_roof_2_r3.setPos(-17.587F, -36.3849F, 0);
		outer_roof_1.addChild(outer_roof_2_r3);
		setRotationAngle(outer_roof_2_r3, 0, 0, 0.1107F);
		outer_roof_2_r3.texOffs(215, 15).addBox(0, -1, -36, 1, 2, 11, 0, false);

		outer_roof_2 = new ModelMapper(modelDataWrapper);
		outer_roof_2.setPos(0, 0, 0);
		roof_end_exterior.addChild(outer_roof_2);
		outer_roof_2.texOffs(93, 79).addBox(0, -42, -36, 6, 1, 11, 0, true);

		outer_roof_5_r4 = new ModelMapper(modelDataWrapper);
		outer_roof_5_r4.setPos(9.7656F, -40.3206F, 0);
		outer_roof_2.addChild(outer_roof_5_r4);
		setRotationAngle(outer_roof_5_r4, 0, 0, 0.1745F);
		outer_roof_5_r4.texOffs(155, 204).addBox(-4, -1, -36, 8, 1, 11, 0, true);

		outer_roof_4_r4 = new ModelMapper(modelDataWrapper);
		outer_roof_4_r4.setPos(14.6775F, -38.9948F, 0);
		outer_roof_2.addChild(outer_roof_4_r4);
		setRotationAngle(outer_roof_4_r4, 0, 0, 0.5236F);
		outer_roof_4_r4.texOffs(17, 31).addBox(-1.5F, -1, -36, 3, 1, 11, 0, true);

		outer_roof_3_r4 = new ModelMapper(modelDataWrapper);
		outer_roof_3_r4.setPos(16.1105F, -37.7448F, 0);
		outer_roof_2.addChild(outer_roof_3_r4);
		setRotationAngle(outer_roof_3_r4, 0, 0, 1.0472F);
		outer_roof_3_r4.texOffs(19, 133).addBox(-1, -1, -36, 2, 1, 11, 0, true);

		outer_roof_2_r4 = new ModelMapper(modelDataWrapper);
		outer_roof_2_r4.setPos(17.587F, -36.3849F, 0);
		outer_roof_2.addChild(outer_roof_2_r4);
		setRotationAngle(outer_roof_2_r4, 0, 0, -0.1107F);
		outer_roof_2_r4.texOffs(215, 15).addBox(-1, -1, -36, 1, 2, 11, 0, true);

		outer_roof_1_r2 = new ModelMapper(modelDataWrapper);
		outer_roof_1_r2.setPos(20, -13, 0);
		outer_roof_2.addChild(outer_roof_1_r2);
		setRotationAngle(outer_roof_1_r2, 0, 0, -0.1107F);
		outer_roof_1_r2.texOffs(34, 134).addBox(0, -23, -29, 1, 4, 4, 0, true);
		outer_roof_1_r2.texOffs(181, 133).addBox(-1, -23, -36, 1, 4, 7, 0, true);

		roof_end_vents = new ModelMapper(modelDataWrapper);
		roof_end_vents.setPos(0, 24, 0);
		roof_end_vents.texOffs(0, 46).addBox(-8, -43, -21, 16, 2, 48, 0, false);

		vent_3_r1 = new ModelMapper(modelDataWrapper);
		vent_3_r1.setPos(-8, -43, 12);
		roof_end_vents.addChild(vent_3_r1);
		setRotationAngle(vent_3_r1, 0, 0, -0.3491F);
		vent_3_r1.texOffs(152, 119).addBox(-9, 0, -33, 9, 2, 48, 0, true);

		vent_2_r1 = new ModelMapper(modelDataWrapper);
		vent_2_r1.setPos(8, -43, 12);
		roof_end_vents.addChild(vent_2_r1);
		setRotationAngle(vent_2_r1, 0, 0, 0.3491F);
		vent_2_r1.texOffs(152, 119).addBox(0, 0, -33, 9, 2, 48, 0, false);

		head = new ModelMapper(modelDataWrapper);
		head.setPos(0, 24, 0);
		head.texOffs(80, 46).addBox(-20, 0, -13, 40, 1, 31, 0, false);
		head.texOffs(259, 192).addBox(-20, -13, -13, 2, 13, 31, 0, false);
		head.texOffs(252, 148).addBox(18, -13, -13, 2, 13, 31, 0, true);
		head.texOffs(261, 15).addBox(-18, -36, 18, 36, 36, 0, 0, false);

		upper_wall_2_r5 = new ModelMapper(modelDataWrapper);
		upper_wall_2_r5.setPos(20, -13, 0);
		head.addChild(upper_wall_2_r5);
		setRotationAngle(upper_wall_2_r5, 0, 0, -0.1107F);
		upper_wall_2_r5.texOffs(0, 240).addBox(-2, -20, -13, 2, 20, 31, 0, true);

		upper_wall_1_r4 = new ModelMapper(modelDataWrapper);
		upper_wall_1_r4.setPos(-20, -13, 0);
		head.addChild(upper_wall_1_r4);
		setRotationAngle(upper_wall_1_r4, 0, 0, 0.1107F);
		upper_wall_1_r4.texOffs(179, 240).addBox(0, -20, -13, 2, 20, 31, 0, false);

		seat_9 = new ModelMapper(modelDataWrapper);
		seat_9.setPos(0, 0, 2);
		head.addChild(seat_9);
		seat_9.texOffs(0, 85).addBox(-18, -6, 8, 12, 1, 7, 0, true);

		seat_back_r9 = new ModelMapper(modelDataWrapper);
		seat_back_r9.setPos(0, -6, 14);
		seat_9.addChild(seat_back_r9);
		setRotationAngle(seat_back_r9, -0.0524F, 0, 0);
		seat_back_r9.texOffs(53, 138).addBox(-18, -8, 0, 12, 8, 1, 0, true);

		seat_10 = new ModelMapper(modelDataWrapper);
		seat_10.setPos(0, 0, -2);
		head.addChild(seat_10);
		setRotationAngle(seat_10, 0, 3.1416F, 0);
		seat_10.texOffs(0, 85).addBox(6, -6, 3, 12, 1, 7, 0, false);

		seat_back_r10 = new ModelMapper(modelDataWrapper);
		seat_back_r10.setPos(0, -6, 9);
		seat_10.addChild(seat_back_r10);
		setRotationAngle(seat_back_r10, -0.0524F, 0, 0);
		seat_back_r10.texOffs(53, 138).addBox(6, -8, 0, 12, 8, 1, 0, false);

		seat_11 = new ModelMapper(modelDataWrapper);
		seat_11.setPos(24, 0, -2);
		head.addChild(seat_11);
		setRotationAngle(seat_11, 0, 3.1416F, 0);
		seat_11.texOffs(0, 85).addBox(6, -6, 3, 12, 1, 7, 0, false);

		seat_back_r11 = new ModelMapper(modelDataWrapper);
		seat_back_r11.setPos(0, -6, 9);
		seat_11.addChild(seat_back_r11);
		setRotationAngle(seat_back_r11, -0.0524F, 0, 0);
		seat_back_r11.texOffs(53, 138).addBox(6, -8, 0, 12, 8, 1, 0, false);

		seat_12 = new ModelMapper(modelDataWrapper);
		seat_12.setPos(0, 0, 2);
		head.addChild(seat_12);
		seat_12.texOffs(0, 85).addBox(6, -6, 8, 12, 1, 7, 0, true);

		seat_back_r12 = new ModelMapper(modelDataWrapper);
		seat_back_r12.setPos(24, -6, 14);
		seat_12.addChild(seat_back_r12);
		setRotationAngle(seat_back_r12, -0.0524F, 0, 0);
		seat_back_r12.texOffs(53, 138).addBox(-18, -8, 0, 12, 8, 1, 0, true);

		head_exterior = new ModelMapper(modelDataWrapper);
		head_exterior.setPos(0, 24, 0);
		head_exterior.texOffs(195, 91).addBox(-21, 0, 18, 42, 2, 13, 0, false);
		head_exterior.texOffs(18, 58).addBox(21, 2, 22, 0, 5, 6, 0, true);
		head_exterior.texOffs(18, 58).addBox(-21, 2, 22, 0, 5, 6, 0, false);
		head_exterior.texOffs(218, 116).addBox(20, -13, -13, 0, 13, 32, 0, true);
		head_exterior.texOffs(88, 271).addBox(20, -13, 19, 1, 13, 12, 0, true);
		head_exterior.texOffs(102, 170).addBox(-20, -13, -13, 0, 13, 32, 0, false);
		head_exterior.texOffs(88, 271).addBox(-21, -13, 19, 1, 13, 12, 0, false);
		head_exterior.texOffs(271, 51).addBox(20, 0, -13, 1, 2, 31, 0, false);
		head_exterior.texOffs(271, 51).addBox(-21, 0, -13, 1, 2, 31, 0, false);
		head_exterior.texOffs(218, 106).addBox(-20, -42, 19, 40, 42, 0, 0, false);

		driver_door_upper_2_r1 = new ModelMapper(modelDataWrapper);
		driver_door_upper_2_r1.setPos(-21, -13, 0);
		head_exterior.addChild(driver_door_upper_2_r1);
		setRotationAngle(driver_door_upper_2_r1, 0, 0, 0.1107F);
		driver_door_upper_2_r1.texOffs(0, 234).addBox(0, -20, 19, 1, 20, 12, 0, false);

		upper_wall_2_r6 = new ModelMapper(modelDataWrapper);
		upper_wall_2_r6.setPos(-20, -13, 0);
		head_exterior.addChild(upper_wall_2_r6);
		setRotationAngle(upper_wall_2_r6, 0, 0, 0.1107F);
		upper_wall_2_r6.texOffs(102, 150).addBox(0, -20, -13, 0, 20, 32, 0, false);

		driver_door_upper_1_r1 = new ModelMapper(modelDataWrapper);
		driver_door_upper_1_r1.setPos(21, -13, 0);
		head_exterior.addChild(driver_door_upper_1_r1);
		setRotationAngle(driver_door_upper_1_r1, 0, 0, -0.1107F);
		driver_door_upper_1_r1.texOffs(0, 234).addBox(-1, -20, 19, 1, 20, 12, 0, true);

		upper_wall_1_r5 = new ModelMapper(modelDataWrapper);
		upper_wall_1_r5.setPos(20, -13, 0);
		head_exterior.addChild(upper_wall_1_r5);
		setRotationAngle(upper_wall_1_r5, 0, 0, -0.1107F);
		upper_wall_1_r5.texOffs(94, 199).addBox(0, -20, -13, 0, 20, 32, 0, true);

		front = new ModelMapper(modelDataWrapper);
		front.setPos(0, 0, 0);
		head_exterior.addChild(front);
		front.texOffs(214, 70).addBox(-20, 2, 31, 40, 0, 8, 0, false);

		head_roof_r1 = new ModelMapper(modelDataWrapper);
		head_roof_r1.setPos(0, -41.2432F, 30.9176F);
		front.addChild(head_roof_r1);
		setRotationAngle(head_roof_r1, -0.1309F, 0, 0);
		head_roof_r1.texOffs(73, 116).addBox(-6, -0.5F, -2, 12, 0, 4, 0, false);

		head_top_r1 = new ModelMapper(modelDataWrapper);
		head_top_r1.setPos(0, -35.7151F, 31.9514F);
		front.addChild(head_top_r1);
		setRotationAngle(head_top_r1, 0.1745F, 0, 0);
		head_top_r1.texOffs(0, 215).addBox(-6, -5.5F, -2, 12, 11, 4, 0, false);

		head_bottom_1_r1 = new ModelMapper(modelDataWrapper);
		head_bottom_1_r1.setPos(0, 2, 39);
		front.addChild(head_bottom_1_r1);
		setRotationAngle(head_bottom_1_r1, -0.3054F, 0, 0);
		head_bottom_1_r1.texOffs(230, 84).addBox(-20, -4, 0, 40, 4, 0, 0, false);

		side_1 = new ModelMapper(modelDataWrapper);
		side_1.setPos(0, 0, 0);
		front.addChild(side_1);
		side_1.texOffs(76, 90).addBox(0, -42, 25, 6, 0, 4, 0, true);

		outer_head_4_r1 = new ModelMapper(modelDataWrapper);
		outer_head_4_r1.setPos(13, -22.275F, 36.3511F);
		side_1.addChild(outer_head_4_r1);
		setRotationAngle(outer_head_4_r1, 0.1745F, 0, 0);
		outer_head_4_r1.texOffs(297, 236).addBox(-7, -19.5F, 0, 14, 41, 0, 0, false);

		outer_head_2_r1 = new ModelMapper(modelDataWrapper);
		outer_head_2_r1.setPos(0, 0, 0);
		side_1.addChild(outer_head_2_r1);
		setRotationAngle(outer_head_2_r1, 0, -0.0873F, -0.1107F);
		outer_head_2_r1.texOffs(79, 178).addBox(24, -35.5F, 28.75F, 0, 25, 9, 0, false);

		outer_head_1_r1 = new ModelMapper(modelDataWrapper);
		outer_head_1_r1.setPos(0, 0, 0);
		side_1.addChild(outer_head_1_r1);
		setRotationAngle(outer_head_1_r1, 0, -0.0873F, 0);
		outer_head_1_r1.texOffs(17, 177).addBox(22.7F, -13, 29, 0, 15, 10, 0, false);

		outer_roof_1_r3 = new ModelMapper(modelDataWrapper);
		outer_roof_1_r3.setPos(20, -13, 5);
		side_1.addChild(outer_roof_1_r3);
		setRotationAngle(outer_roof_1_r3, 0, 0, -0.1107F);
		outer_roof_1_r3.texOffs(195, 106).addBox(-1, -23, 20, 2, 4, 6, 0, true);

		outer_roof_3_r5 = new ModelMapper(modelDataWrapper);
		outer_roof_3_r5.setPos(16.9769F, -38.2468F, 23.5F);
		side_1.addChild(outer_roof_3_r5);
		setRotationAngle(outer_roof_3_r5, 0, 0, 1.0472F);
		outer_roof_3_r5.texOffs(28, 36).addBox(-1, 0, 1.5F, 2, 0, 6, 0, true);

		outer_roof_2_r5 = new ModelMapper(modelDataWrapper);
		outer_roof_2_r5.setPos(17.0902F, -36.332F, 23.5F);
		side_1.addChild(outer_roof_2_r5);
		setRotationAngle(outer_roof_2_r5, 0, 0, -0.1107F);
		outer_roof_2_r5.texOffs(132, 91).addBox(-0.5F, -1, 1.5F, 1, 2, 6, 0, true);

		outer_roof_4_r5 = new ModelMapper(modelDataWrapper);
		outer_roof_4_r5.setPos(15.1778F, -39.8628F, 0);
		side_1.addChild(outer_roof_4_r5);
		setRotationAngle(outer_roof_4_r5, 0, 0, 0.5236F);
		outer_roof_4_r5.texOffs(112, 83).addBox(-1.5F, 0, 25, 3, 0, 4, 0, true);

		outer_roof_11_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_11_r1.setPos(16.4774F, -39.1136F, 29.0001F);
		side_1.addChild(outer_roof_11_r1);
		setRotationAngle(outer_roof_11_r1, -0.2182F, -0.829F, 1.0472F);
		outer_roof_11_r1.texOffs(24, 80).addBox(0, 0.001F, 0, 5, 0, 4, 0, true);

		outer_head_6_r1 = new ModelMapper(modelDataWrapper);
		outer_head_6_r1.setPos(14.7995F, -39.2074F, 30.9176F);
		side_1.addChild(outer_head_6_r1);
		setRotationAngle(outer_head_6_r1, -0.1309F, 0, 0.5236F);
		outer_head_6_r1.texOffs(111, 78).addBox(-1.5F, -0.5F, -2, 3, 0, 5, 0, true);

		outer_roof_5_r5 = new ModelMapper(modelDataWrapper);
		outer_roof_5_r5.setPos(9.9394F, -41.3064F, 0);
		side_1.addChild(outer_roof_5_r5);
		setRotationAngle(outer_roof_5_r5, 0, 0, 0.1745F);
		outer_roof_5_r5.texOffs(121, 41).addBox(-4, 0, 25, 8, 0, 4, 0, true);

		outer_head_5_r1 = new ModelMapper(modelDataWrapper);
		outer_head_5_r1.setPos(9.808F, -40.5611F, 30.9176F);
		side_1.addChild(outer_head_5_r1);
		setRotationAngle(outer_head_5_r1, -0.1309F, 0, 0.1745F);
		outer_head_5_r1.texOffs(72, 120).addBox(-4, -0.5F, -2, 8, 0, 5, 0, true);

		side_2 = new ModelMapper(modelDataWrapper);
		side_2.setPos(0, 0, 0);
		front.addChild(side_2);
		side_2.texOffs(76, 90).addBox(-6, -42, 25, 6, 0, 4, 0, false);

		outer_head_6_r2 = new ModelMapper(modelDataWrapper);
		outer_head_6_r2.setPos(-14.7995F, -39.2074F, 30.9176F);
		side_2.addChild(outer_head_6_r2);
		setRotationAngle(outer_head_6_r2, -0.1309F, 0, -0.5236F);
		outer_head_6_r2.texOffs(111, 78).addBox(-1.5F, -0.5F, -2, 3, 0, 5, 0, false);

		outer_head_5_r2 = new ModelMapper(modelDataWrapper);
		outer_head_5_r2.setPos(-9.808F, -40.5611F, 30.9176F);
		side_2.addChild(outer_head_5_r2);
		setRotationAngle(outer_head_5_r2, -0.1309F, 0, -0.1745F);
		outer_head_5_r2.texOffs(72, 120).addBox(-4, -0.5F, -2, 8, 0, 5, 0, false);

		outer_head_4_r2 = new ModelMapper(modelDataWrapper);
		outer_head_4_r2.setPos(-13, -22.275F, 36.3511F);
		side_2.addChild(outer_head_4_r2);
		setRotationAngle(outer_head_4_r2, 0.1745F, 0, 0);
		outer_head_4_r2.texOffs(298, 106).addBox(-7, -19.5F, 0, 14, 41, 0, 0, false);

		outer_head_2_r2 = new ModelMapper(modelDataWrapper);
		outer_head_2_r2.setPos(0, 0, 0);
		side_2.addChild(outer_head_2_r2);
		setRotationAngle(outer_head_2_r2, 0, 0.0873F, 0.1107F);
		outer_head_2_r2.texOffs(79, 178).addBox(-24, -35.5F, 28.75F, 0, 25, 9, 0, false);

		outer_head_1_r2 = new ModelMapper(modelDataWrapper);
		outer_head_1_r2.setPos(0, 0, 0);
		side_2.addChild(outer_head_1_r2);
		setRotationAngle(outer_head_1_r2, 0, 0.0873F, 0);
		outer_head_1_r2.texOffs(17, 86).addBox(-22.7F, -13, 29, 0, 15, 10, 0, false);

		outer_roof_11_r2 = new ModelMapper(modelDataWrapper);
		outer_roof_11_r2.setPos(-16.4774F, -39.1136F, 29.0001F);
		side_2.addChild(outer_roof_11_r2);
		setRotationAngle(outer_roof_11_r2, -0.2182F, 0.829F, -1.0472F);
		outer_roof_11_r2.texOffs(24, 80).addBox(-5, 0.001F, 0, 5, 0, 4, 0, false);

		outer_roof_5_r6 = new ModelMapper(modelDataWrapper);
		outer_roof_5_r6.setPos(-9.9394F, -41.3064F, 0);
		side_2.addChild(outer_roof_5_r6);
		setRotationAngle(outer_roof_5_r6, 0, 0, -0.1745F);
		outer_roof_5_r6.texOffs(121, 41).addBox(-4, 0, 25, 8, 0, 4, 0, false);

		outer_roof_4_r6 = new ModelMapper(modelDataWrapper);
		outer_roof_4_r6.setPos(-15.1778F, -39.8628F, 0);
		side_2.addChild(outer_roof_4_r6);
		setRotationAngle(outer_roof_4_r6, 0, 0, -0.5236F);
		outer_roof_4_r6.texOffs(112, 83).addBox(-1.5F, 0, 25, 3, 0, 4, 0, false);

		outer_roof_3_r6 = new ModelMapper(modelDataWrapper);
		outer_roof_3_r6.setPos(-16.9769F, -38.2468F, 23.5F);
		side_2.addChild(outer_roof_3_r6);
		setRotationAngle(outer_roof_3_r6, 0, 0, -1.0472F);
		outer_roof_3_r6.texOffs(28, 36).addBox(-1, 0, 1.5F, 2, 0, 6, 0, false);

		outer_roof_2_r6 = new ModelMapper(modelDataWrapper);
		outer_roof_2_r6.setPos(-17.0902F, -36.332F, 23.5F);
		side_2.addChild(outer_roof_2_r6);
		setRotationAngle(outer_roof_2_r6, 0, 0, 0.1107F);
		outer_roof_2_r6.texOffs(132, 91).addBox(-0.5F, -1, 1.5F, 1, 2, 6, 0, false);

		outer_roof_1_r4 = new ModelMapper(modelDataWrapper);
		outer_roof_1_r4.setPos(-20, -13, 5);
		side_2.addChild(outer_roof_1_r4);
		setRotationAngle(outer_roof_1_r4, 0, 0, 0.1107F);
		outer_roof_1_r4.texOffs(195, 106).addBox(-1, -23, 20, 2, 4, 6, 0, false);

		emergency_door = new ModelMapper(modelDataWrapper);
		emergency_door.setPos(0, 0, 0);
		front.addChild(emergency_door);
		emergency_door.texOffs(294, 192).addBox(-6, -31, 34, 12, 30, 0, 0, false);
		emergency_door.texOffs(100, 293).addBox(6, -31, 31, 0, 30, 10, 0, false);
		emergency_door.texOffs(120, 293).addBox(-6, -31, 31, 0, 30, 10, 0, false);
		emergency_door.texOffs(68, 78).addBox(-6, -1, 30, 12, 0, 12, 0, false);

		pipe = new ModelMapper(modelDataWrapper);
		pipe.setPos(0, -3.05F, -0.5F);
		head_exterior.addChild(pipe);


		valve_8_r1 = new ModelMapper(modelDataWrapper);
		valve_8_r1.setPos(0, 0, 0);
		pipe.addChild(valve_8_r1);
		setRotationAngle(valve_8_r1, 0.1745F, 0, 0);
		valve_8_r1.texOffs(34, 46).addBox(-10.175F, 2.125F, 39.55F, 1, 8, 1, 0, false);
		valve_8_r1.texOffs(0, 85).addBox(-10.775F, 9.3F, 39.6F, 2, 3, 1, 0, false);
		valve_8_r1.texOffs(32, 116).addBox(-10.775F, -0.7F, 39.6F, 2, 3, 1, 0, false);
		valve_8_r1.texOffs(32, 120).addBox(8.675F, -0.7F, 39.6F, 2, 3, 1, 0, false);
		valve_8_r1.texOffs(93, 120).addBox(15.325F, -0.7F, 39.6F, 2, 3, 1, 0, false);

		valve_7_r1 = new ModelMapper(modelDataWrapper);
		valve_7_r1.setPos(0, 0, 0);
		pipe.addChild(valve_7_r1);
		setRotationAngle(valve_7_r1, 0, 0, -0.1745F);
		valve_7_r1.texOffs(0, 39).addBox(8.65F, 2.375F, 40.35F, 1, 5, 1, 0, false);

		valve_6_r1 = new ModelMapper(modelDataWrapper);
		valve_6_r1.setPos(0, 0, 0);
		pipe.addChild(valve_6_r1);
		setRotationAngle(valve_6_r1, 0, 0, -1.309F);
		valve_6_r1.texOffs(38, 38).addBox(-3, 10.975F, 40.35F, 1, 3, 1, 0, false);

		valve_5_r1 = new ModelMapper(modelDataWrapper);
		valve_5_r1.setPos(0, 0, 0);
		pipe.addChild(valve_5_r1);
		setRotationAngle(valve_5_r1, 0, 0, 1.5708F);
		valve_5_r1.texOffs(16, 39).addBox(5.375F, -13.325F, 40.3F, 1, 1, 1, 0, false);

		valve_4_r1 = new ModelMapper(modelDataWrapper);
		valve_4_r1.setPos(0, 0, 0);
		pipe.addChild(valve_4_r1);
		setRotationAngle(valve_4_r1, 0, 0, 1.309F);
		valve_4_r1.texOffs(37, 84).addBox(8.55F, -13.85F, 40.3F, 1, 3, 1, 0, false);

		valve_3_r1 = new ModelMapper(modelDataWrapper);
		valve_3_r1.setPos(0, 0, 0);
		pipe.addChild(valve_3_r1);
		setRotationAngle(valve_3_r1, 0, 0, 0.2618F);
		valve_3_r1.texOffs(34, 69).addBox(15.8F, -3.625F, 40.3F, 1, 5, 1, 0, false);

		valve_2_r1 = new ModelMapper(modelDataWrapper);
		valve_2_r1.setPos(0, 0, 0);
		pipe.addChild(valve_2_r1);
		setRotationAngle(valve_2_r1, 0.1745F, 0, 0.0436F);
		valve_2_r1.texOffs(34, 55).addBox(8.95F, 1.775F, 39.65F, 1, 6, 1, 0, false);

		valve_1_r1 = new ModelMapper(modelDataWrapper);
		valve_1_r1.setPos(0, 0, 0);
		pipe.addChild(valve_1_r1);
		setRotationAngle(valve_1_r1, 0.1745F, 0, -0.0436F);
		valve_1_r1.texOffs(34, 62).addBox(16.1F, 2.8F, 39.4F, 1, 6, 1, 0, false);

		headlights = new ModelMapper(modelDataWrapper);
		headlights.setPos(0, 24, 0);


		outer_head_5_r3 = new ModelMapper(modelDataWrapper);
		outer_head_5_r3.setPos(-13, -22.275F, 36.4511F);
		headlights.addChild(outer_head_5_r3);
		setRotationAngle(outer_head_5_r3, 0.1745F, 0, 0);
		outer_head_5_r3.texOffs(18, 46).addBox(-3, 15.5F, 0, 6, 5, 0, 0, true);
		outer_head_5_r3.texOffs(18, 46).addBox(23, 15.5F, 0, 6, 5, 0, 0, false);

		tail_lights = new ModelMapper(modelDataWrapper);
		tail_lights.setPos(0, 24, 0);


		tail_light_r1 = new ModelMapper(modelDataWrapper);
		tail_light_r1.setPos(4, -40.0015F, 33.2254F);
		tail_lights.addChild(tail_light_r1);
		setRotationAngle(tail_light_r1, 0.1745F, 0, 0);
		tail_light_r1.texOffs(18, 51).addBox(-4, -2, 0.1F, 6, 5, 0, 0, false);

		door_light = new ModelMapper(modelDataWrapper);
		door_light.setPos(0, 24, 0);


		outer_roof_1_r5 = new ModelMapper(modelDataWrapper);
		outer_roof_1_r5.setPos(-20, -13, 0);
		door_light.addChild(outer_roof_1_r5);
		setRotationAngle(outer_roof_1_r5, 0, 0, 0.1107F);
		outer_roof_1_r5.texOffs(18, 52).addBox(-1.1F, -23, -2, 0, 4, 4, 0, false);

		door_light_off = new ModelMapper(modelDataWrapper);
		door_light_off.setPos(0, 24, 0);


		light_r1 = new ModelMapper(modelDataWrapper);
		light_r1.setPos(-20, -13, 0);
		door_light_off.addChild(light_r1);
		setRotationAngle(light_r1, 0, 0, 0.1107F);
		light_r1.texOffs(19, 62).addBox(-1, -21, 0, 0, 0, 0, 0.4F, false);

		door_light_on = new ModelMapper(modelDataWrapper);
		door_light_on.setPos(0, 24, 0);


		light_r2 = new ModelMapper(modelDataWrapper);
		light_r2.setPos(-20, -13, 0);
		door_light_on.addChild(light_r2);
		setRotationAngle(light_r2, 0, 0, 0.1107F);
		light_r2.texOffs(21, 62).addBox(-1, -21, 0, 0, 0, 0, 0.4F, false);

		modelDataWrapper.setModelPart(textureWidth, textureHeight);
		window.setModelPart();
		window_exterior.setModelPart();
		window_handrails.setModelPart();
		door.setModelPart();
		door_left.setModelPart(door.name);
		door_right.setModelPart(door.name);
		door_exterior.setModelPart();
		door_left_exterior.setModelPart(door_exterior.name);
		door_right_exterior.setModelPart(door_exterior.name);
		door_handrails.setModelPart();
		side_panel.setModelPart();
		side_panel_translucent.setModelPart();
		roof_window.setModelPart();
		roof_door.setModelPart();
		roof_window_light.setModelPart();
		roof_door_light.setModelPart();
		roof_exterior_window.setModelPart();
		roof_exterior_door.setModelPart();
		end.setModelPart();
		end_exterior.setModelPart();
		roof_end.setModelPart();
		roof_end_light.setModelPart();
		roof_end_handrails.setModelPart();
		roof_end_exterior.setModelPart();
		roof_end_vents.setModelPart();
		head.setModelPart();
		head_exterior.setModelPart();
		headlights.setModelPart();
		tail_lights.setModelPart();
		door_light.setModelPart();
		door_light_off.setModelPart();
		door_light_on.setModelPart();
	}

	private static final int DOOR_MAX = 14;
	private static final ModelDoorOverlay MODEL_DOOR_OVERLAY = new ModelDoorOverlay(DOOR_MAX, 6.34F, 13, "door_overlay_e44_left.png", "door_overlay_e44_right.png");
	private static final ModelDoorOverlayTopMLR MODEL_DOOR_OVERLAY_TOP = new ModelDoorOverlayTopMLR("mtr:textures/sign/door_overlay_e44_top.png");

	@Override
	protected void renderWindowPositions(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
		switch (renderStage) {
			case LIGHTS:
				renderMirror(roof_window_light, matrices, vertices, light, position);
				break;
			case INTERIOR:
				renderOnce(window, matrices, vertices, light, position);
				renderOnceFlipped(window, matrices, vertices, light, position);

				if (renderDetails) {
					renderMirror(roof_window, matrices, vertices, light, position);
					renderMirror(window_handrails, matrices, vertices, light, position);
				}
				break;
			case EXTERIOR:
				renderOnce(window_exterior, matrices, vertices, light, position);
				renderOnceFlipped(window_exterior, matrices, vertices, light, position);
				renderMirror(roof_exterior_window, matrices, vertices, light, position);
				break;
		}
	}

	@Override
	protected void renderDoorPositions(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
		final boolean middleDoor = isIndex(getDoorPositions().length / 2, position, getDoorPositions());
		final boolean doorOpen = doorLeftZ > 0 || doorRightZ > 0;

		switch (renderStage) {
			case LIGHTS:
				renderMirror(roof_door_light, matrices, vertices, light, position);
				if (middleDoor && doorOpen) {
					renderMirror(door_light_on, matrices, vertices, light, position - 22);
				}
				break;
			case INTERIOR:
				door_left.setOffset(0, 0, doorRightZ);
				door_right.setOffset(0, 0, -doorRightZ);
				renderOnce(door, matrices, vertices, light, position);
				door_left.setOffset(0, 0, doorLeftZ);
				door_right.setOffset(0, 0, -doorLeftZ);
				renderOnceFlipped(door, matrices, vertices, light, position);

				if (renderDetails) {
					renderMirror(roof_door, matrices, vertices, light, position);
					renderMirror(door_handrails, matrices, vertices, light, position);
					renderMirror(side_panel, matrices, vertices, light, position - 19);
					renderMirror(side_panel, matrices, vertices, light, position + 19);
				}
				break;
			case EXTERIOR:
				door_left_exterior.setOffset(0, 0, doorRightZ);
				door_right_exterior.setOffset(0, 0, -doorRightZ);
				renderOnce(door_exterior, matrices, vertices, light, position);
				door_left_exterior.setOffset(0, 0, doorLeftZ);
				door_right_exterior.setOffset(0, 0, -doorLeftZ);
				renderOnceFlipped(door_exterior, matrices, vertices, light, position);
				renderMirror(roof_exterior_door, matrices, vertices, light, position);

				if (middleDoor && renderDetails) {
					renderMirror(door_light, matrices, vertices, light, position - 22);
					if (!doorOpen) {
						renderMirror(door_light_off, matrices, vertices, light, position - 22);
					}
				}
				break;
			case INTERIOR_TRANSLUCENT:
				renderMirror(side_panel_translucent, matrices, vertices, light, position - 19);
				renderMirror(side_panel_translucent, matrices, vertices, light, position + 19);
				break;
		}
	}

	@Override
	protected void renderHeadPosition1(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean useHeadlights) {
		switch (renderStage) {
			case LIGHTS:
				renderOnce(roof_end_light, matrices, vertices, light, position);
				break;
			case ALWAYS_ON_LIGHTS:
				renderOnceFlipped(useHeadlights ? headlights : tail_lights, matrices, vertices, light, position);
				break;
			case INTERIOR:
				renderOnceFlipped(head, matrices, vertices, light, position);
				if (renderDetails) {
					renderOnce(roof_end, matrices, vertices, light, position);
				}
				break;
			case EXTERIOR:
				renderOnceFlipped(head_exterior, matrices, vertices, light, position);
				renderMirror(roof_exterior_door, matrices, vertices, light, position - 6);
				renderOnceFlipped(roof_end_vents, matrices, vertices, light, position + 2);
				break;
		}
	}

	@Override
	protected void renderHeadPosition2(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean useHeadlights) {
		switch (renderStage) {
			case LIGHTS:
				renderOnceFlipped(roof_end_light, matrices, vertices, light, position);
				break;
			case ALWAYS_ON_LIGHTS:
				renderOnce(useHeadlights ? headlights : tail_lights, matrices, vertices, light, position);
				break;
			case INTERIOR:
				renderOnce(head, matrices, vertices, light, position);
				if (renderDetails) {
					renderOnceFlipped(roof_end, matrices, vertices, light, position);
				}
				break;
			case EXTERIOR:
				renderOnce(head_exterior, matrices, vertices, light, position);
				renderMirror(roof_exterior_door, matrices, vertices, light, position + 6);
				renderOnce(roof_end_vents, matrices, vertices, light, position - 2);
				break;
		}
	}

	@Override
	protected void renderEndPosition1(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
		switch (renderStage) {
			case LIGHTS:
				renderOnce(roof_end_light, matrices, vertices, light, position);
				break;
			case INTERIOR:
				renderOnce(end, matrices, vertices, light, position);
				if (renderDetails) {
					renderOnce(roof_end, matrices, vertices, light, position);
					renderOnce(roof_end_handrails, matrices, vertices, light, position);
				}
				break;
			case EXTERIOR:
				renderOnce(end_exterior, matrices, vertices, light, position);
				renderMirror(roof_exterior_door, matrices, vertices, light, position - 6);
				renderOnce(roof_end_exterior, matrices, vertices, light, position);
				renderOnceFlipped(roof_end_vents, matrices, vertices, light, position);
				break;
		}
	}

	@Override
	protected void renderEndPosition2(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
		switch (renderStage) {
			case LIGHTS:
				renderOnceFlipped(roof_end_light, matrices, vertices, light, position);
				break;
			case INTERIOR:
				renderOnceFlipped(end, matrices, vertices, light, position);
				if (renderDetails) {
					renderOnceFlipped(roof_end, matrices, vertices, light, position);
					renderOnceFlipped(roof_end_handrails, matrices, vertices, light, position);
				}
				break;
			case EXTERIOR:
				renderOnceFlipped(end_exterior, matrices, vertices, light, position);
				renderMirror(roof_exterior_door, matrices, vertices, light, position + 6);
				renderOnceFlipped(roof_end_exterior, matrices, vertices, light, position);
				renderOnce(roof_end_vents, matrices, vertices, light, position);
				break;
		}
	}


	@Override
	protected ModelDoorOverlay getModelDoorOverlay() {
		return MODEL_DOOR_OVERLAY;
	}

	@Override
	protected ModelDoorOverlayTopBase getModelDoorOverlayTop() {
		return MODEL_DOOR_OVERLAY_TOP;
	}

	@Override
	protected int[] getWindowPositions() {
		return new int[]{-94, -64, -34, 34, 64, 94};
	}

	@Override
	protected int[] getBogiePositions() {
		return new int[]{-136, 136};
	}

	@Override
	protected int[] getDoorPositions() {
		return new int[]{-128, 0, 128};
	}

	@Override
	protected int[] getEndPositions() {
		return new int[]{-160, 160};
	}

	@Override
	protected float getDoorAnimationX(float value, boolean opening) {
		return 0;
	}

	@Override
	protected float getDoorAnimationZ(float value, boolean opening) {
		if (opening) {
			if (value > 0.4) {
				return smoothEnds(DOOR_MAX - 1, DOOR_MAX - 0.5F, 0.4F, 0.5F, value);
			} else {
				return smoothEnds(-DOOR_MAX + 1, DOOR_MAX - 1, -0.4F, 0.4F, value);
			}
		} else {
			if (value > 0.2) {
				return smoothEnds(1, DOOR_MAX - 0.5F, 0.2F, 0.5F, value);
			} else if (value > 0.1) {
				return smoothEnds(1.5F, 1, 0.1F, 0.2F, value);
			} else {
				return smoothEnds(-1.5F, 1.5F, -0.1F, 0.1F, value);
			}
		}
	}
}
