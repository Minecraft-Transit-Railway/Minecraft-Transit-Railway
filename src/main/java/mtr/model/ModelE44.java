package mtr.model;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class ModelE44 extends ModelTrainBase {

	private final ModelPart window;
	private final ModelPart upper_wall_r1;
	private final ModelPart window_exterior;
	private final ModelPart upper_wall_r2;
	private final ModelPart window_handrails;
	private final ModelPart handrail_4_r1;
	private final ModelPart handrail_1_r1;
	private final ModelPart seat_7;
	private final ModelPart seat_back_r1;
	private final ModelPart seat_8;
	private final ModelPart seat_back_r2;
	private final ModelPart door;
	private final ModelPart upper_wall_2_r1;
	private final ModelPart door_left;
	private final ModelPart door_left_top_r1;
	private final ModelPart door_right;
	private final ModelPart door_right_top_r1;
	private final ModelPart door_exterior;
	private final ModelPart upper_wall_2_r2;
	private final ModelPart door_left_exterior;
	private final ModelPart door_left_top_r2;
	private final ModelPart door_right_exterior;
	private final ModelPart door_right_top_r2;
	private final ModelPart door_handrails;
	private final ModelPart handrail_1_r2;
	private final ModelPart side_panel;
	private final ModelPart handrail_11_r1;
	private final ModelPart handrail_5_r1;
	private final ModelPart roof_window;
	private final ModelPart inner_roof_4_r1;
	private final ModelPart inner_roof_2_r1;
	private final ModelPart inner_roof_1_r1;
	private final ModelPart roof_door;
	private final ModelPart inner_roof_6_r1;
	private final ModelPart inner_roof_4_r2;
	private final ModelPart inner_roof_3_r1;
	private final ModelPart inner_roof_2_r2;
	private final ModelPart roof_window_light;
	private final ModelPart roof_door_light;
	private final ModelPart roof_exterior_window;
	private final ModelPart outer_roof_5_r1;
	private final ModelPart outer_roof_4_r1;
	private final ModelPart outer_roof_3_r1;
	private final ModelPart outer_roof_2_r1;
	private final ModelPart outer_roof_1_r1;
	private final ModelPart roof_exterior_door;
	private final ModelPart outer_roof_6_r1;
	private final ModelPart outer_roof_5_r2;
	private final ModelPart outer_roof_4_r2;
	private final ModelPart outer_roof_3_r2;
	private final ModelPart outer_roof_2_r2;
	private final ModelPart end;
	private final ModelPart upper_wall_2_r3;
	private final ModelPart upper_wall_1_r1;
	private final ModelPart seat_1;
	private final ModelPart seat_back_r3;
	private final ModelPart seat_2;
	private final ModelPart seat_back_r4;
	private final ModelPart seat_3;
	private final ModelPart seat_back_r5;
	private final ModelPart seat_4;
	private final ModelPart seat_back_r6;
	private final ModelPart seat_5;
	private final ModelPart seat_back_r7;
	private final ModelPart seat_6;
	private final ModelPart seat_back_r8;
	private final ModelPart end_exterior;
	private final ModelPart upper_wall_2_r4;
	private final ModelPart upper_wall_1_r2;
	private final ModelPart roof_end;
	private final ModelPart inner_roof_5_r1;
	private final ModelPart inner_roof_3_r2;
	private final ModelPart inner_roof_2_r3;
	private final ModelPart inner_roof_4_r3;
	private final ModelPart inner_roof_2_r4;
	private final ModelPart inner_roof_1_r2;
	private final ModelPart roof_end_light;
	private final ModelPart roof_end_handrails;
	private final ModelPart handrail_11_r2;
	private final ModelPart handrail_10_r1;
	private final ModelPart roof_end_exterior;
	private final ModelPart outer_roof_1;
	private final ModelPart upper_wall_1_r3;
	private final ModelPart outer_roof_5_r3;
	private final ModelPart outer_roof_4_r3;
	private final ModelPart outer_roof_3_r3;
	private final ModelPart outer_roof_2_r3;
	private final ModelPart outer_roof_2;
	private final ModelPart outer_roof_5_r4;
	private final ModelPart outer_roof_4_r4;
	private final ModelPart outer_roof_3_r4;
	private final ModelPart outer_roof_2_r4;
	private final ModelPart outer_roof_1_r2;
	private final ModelPart roof_end_vents;
	private final ModelPart vent_3_r1;
	private final ModelPart vent_2_r1;
	private final ModelPart head;
	private final ModelPart upper_wall_2_r5;
	private final ModelPart upper_wall_1_r4;
	private final ModelPart seat_9;
	private final ModelPart seat_back_r9;
	private final ModelPart seat_10;
	private final ModelPart seat_back_r10;
	private final ModelPart seat_11;
	private final ModelPart seat_back_r11;
	private final ModelPart seat_12;
	private final ModelPart seat_back_r12;
	private final ModelPart head_exterior;
	private final ModelPart driver_door_upper_2_r1;
	private final ModelPart upper_wall_2_r6;
	private final ModelPart driver_door_upper_1_r1;
	private final ModelPart upper_wall_1_r5;
	private final ModelPart front;
	private final ModelPart head_roof_r1;
	private final ModelPart head_top_r1;
	private final ModelPart head_bottom_1_r1;
	private final ModelPart side_1;
	private final ModelPart outer_head_4_r1;
	private final ModelPart outer_head_2_r1;
	private final ModelPart outer_head_1_r1;
	private final ModelPart outer_roof_1_r3;
	private final ModelPart outer_roof_3_r5;
	private final ModelPart outer_roof_2_r5;
	private final ModelPart outer_roof_4_r5;
	private final ModelPart outer_roof_11_r1;
	private final ModelPart outer_head_6_r1;
	private final ModelPart outer_roof_5_r5;
	private final ModelPart outer_head_5_r1;
	private final ModelPart side_2;
	private final ModelPart outer_head_6_r2;
	private final ModelPart outer_head_5_r2;
	private final ModelPart outer_head_4_r2;
	private final ModelPart outer_head_2_r2;
	private final ModelPart outer_head_1_r2;
	private final ModelPart outer_roof_11_r2;
	private final ModelPart outer_roof_5_r6;
	private final ModelPart outer_roof_4_r6;
	private final ModelPart outer_roof_3_r6;
	private final ModelPart outer_roof_2_r6;
	private final ModelPart outer_roof_1_r4;
	private final ModelPart emergency_door;
	private final ModelPart pipe;
	private final ModelPart valve_8_r1;
	private final ModelPart valve_7_r1;
	private final ModelPart valve_6_r1;
	private final ModelPart valve_5_r1;
	private final ModelPart valve_4_r1;
	private final ModelPart valve_3_r1;
	private final ModelPart valve_2_r1;
	private final ModelPart valve_1_r1;
	private final ModelPart headlights;
	private final ModelPart outer_head_5_r3;
	private final ModelPart tail_lights;
	private final ModelPart tail_light_r1;
	private final ModelPart door_light;
	private final ModelPart outer_roof_1_r5;
	private final ModelPart door_light_off;
	private final ModelPart light_r1;
	private final ModelPart door_light_on;
	private final ModelPart light_r2;

	public ModelE44() {
		textureWidth = 336;
		textureHeight = 336;
		window = new ModelPart(this);
		window.setPivot(0, 24, 0);
		window.setTextureOffset(191, 39).addCuboid(-20, 0, -15, 20, 1, 30, 0, false);
		window.setTextureOffset(36, 271).addCuboid(-20, -13, -15, 2, 13, 30, 0, false);

		upper_wall_r1 = new ModelPart(this);
		upper_wall_r1.setPivot(-20, -13, 0);
		window.addChild(upper_wall_r1);
		setRotationAngle(upper_wall_r1, 0, 0, 0.1107F);
		upper_wall_r1.setTextureOffset(245, 247).addCuboid(0, -20, -15, 2, 20, 30, 0, false);

		window_exterior = new ModelPart(this);
		window_exterior.setPivot(0, 24, 0);
		window_exterior.setTextureOffset(100, 271).addCuboid(-21, 0, -15, 1, 2, 30, 0, false);
		window_exterior.setTextureOffset(94, 221).addCuboid(-20, -13, -15, 0, 13, 30, 0, false);

		upper_wall_r2 = new ModelPart(this);
		upper_wall_r2.setPivot(-20, -13, 0);
		window_exterior.addChild(upper_wall_r2);
		setRotationAngle(upper_wall_r2, 0, 0, 0.1107F);
		upper_wall_r2.setTextureOffset(214, 217).addCuboid(0, -20, -15, 0, 20, 30, 0, false);

		window_handrails = new ModelPart(this);
		window_handrails.setPivot(0, 24, 0);
		window_handrails.setTextureOffset(4, 41).addCuboid(-9, -31, 7, 2, 4, 0, 0, false);
		window_handrails.setTextureOffset(4, 41).addCuboid(-9, -31, 0, 2, 4, 0, 0, false);
		window_handrails.setTextureOffset(4, 41).addCuboid(-9, -31, -7, 2, 4, 0, 0, false);

		handrail_4_r1 = new ModelPart(this);
		handrail_4_r1.setPivot(-7.8F, -30.7F, 16.2F);
		window_handrails.addChild(handrail_4_r1);
		setRotationAngle(handrail_4_r1, 0, 0, -0.0873F);
		handrail_4_r1.setTextureOffset(0, 0).addCuboid(-0.2F, 0.2F, -1.2F, 0, 18, 0, 0.2F, false);
		handrail_4_r1.setTextureOffset(0, 0).addCuboid(-0.2F, 0.2F, -31.2F, 0, 18, 0, 0.2F, false);

		handrail_1_r1 = new ModelPart(this);
		handrail_1_r1.setPivot(0, 0, 0);
		window_handrails.addChild(handrail_1_r1);
		setRotationAngle(handrail_1_r1, -1.5708F, 0, 0);
		handrail_1_r1.setTextureOffset(0, 0).addCuboid(-8, -15, -30.5F, 0, 30, 0, 0.2F, false);

		seat_7 = new ModelPart(this);
		seat_7.setPivot(0, 0, 0);
		window_handrails.addChild(seat_7);
		seat_7.setTextureOffset(0, 85).addCuboid(-18, -6, -14.5F, 12, 1, 7, 0, false);

		seat_back_r1 = new ModelPart(this);
		seat_back_r1.setPivot(0, -6, -13.5F);
		seat_7.addChild(seat_back_r1);
		setRotationAngle(seat_back_r1, 0.0524F, 0, 0);
		seat_back_r1.setTextureOffset(53, 138).addCuboid(-18, -8, -1, 12, 8, 1, 0, false);

		seat_8 = new ModelPart(this);
		seat_8.setPivot(-24, 0, 2);
		window_handrails.addChild(seat_8);
		seat_8.setTextureOffset(0, 85).addCuboid(6, -6, 5.5F, 12, 1, 7, 0, false);

		seat_back_r2 = new ModelPart(this);
		seat_back_r2.setPivot(0, -6, 11.5F);
		seat_8.addChild(seat_back_r2);
		setRotationAngle(seat_back_r2, -0.0524F, 0, 0);
		seat_back_r2.setTextureOffset(53, 138).addCuboid(6, -8, 0, 12, 8, 1, 0, false);

		door = new ModelPart(this);
		door.setPivot(0, 24, 0);
		door.setTextureOffset(125, 0).addCuboid(-20, 0, -19, 20, 1, 38, 0, false);
		door.setTextureOffset(154, 246).addCuboid(-20, -13, -19, 2, 13, 5, 0, false);
		door.setTextureOffset(239, 15).addCuboid(-20, -13, 14, 2, 13, 5, 0, false);

		upper_wall_2_r1 = new ModelPart(this);
		upper_wall_2_r1.setPivot(-20, -13, 0);
		door.addChild(upper_wall_2_r1);
		setRotationAngle(upper_wall_2_r1, 0, 0, 0.1107F);
		upper_wall_2_r1.setTextureOffset(196, 240).addCuboid(0, -20, 14, 2, 20, 5, 0, false);
		upper_wall_2_r1.setTextureOffset(114, 271).addCuboid(0, -20, -19, 2, 20, 5, 0, false);

		door_left = new ModelPart(this);
		door_left.setPivot(0, 0, 0);
		door.addChild(door_left);
		door_left.setTextureOffset(47, 187).addCuboid(-20.8F, -13, 0, 1, 13, 15, 0, false);

		door_left_top_r1 = new ModelPart(this);
		door_left_top_r1.setPivot(-20.8F, -13, 0);
		door_left.addChild(door_left_top_r1);
		setRotationAngle(door_left_top_r1, 0, 0, 0.1107F);
		door_left_top_r1.setTextureOffset(132, 91).addCuboid(0, -20, 0, 1, 20, 15, 0, false);

		door_right = new ModelPart(this);
		door_right.setPivot(0, 0, 0);
		door.addChild(door_right);
		door_right.setTextureOffset(0, 187).addCuboid(-20.8F, -13, -15, 1, 13, 15, 0, false);

		door_right_top_r1 = new ModelPart(this);
		door_right_top_r1.setPivot(-20.8F, -13, 0);
		door_right.addChild(door_right_top_r1);
		setRotationAngle(door_right_top_r1, 0, 0, 0.1107F);
		door_right_top_r1.setTextureOffset(0, 96).addCuboid(0, -20, -15, 1, 20, 15, 0, false);

		door_exterior = new ModelPart(this);
		door_exterior.setPivot(0, 24, 0);
		door_exterior.setTextureOffset(132, 231).addCuboid(-21, 0, -19, 1, 2, 38, 0, false);
		door_exterior.setTextureOffset(0, 91).addCuboid(-20, -13, -19, 0, 13, 5, 0, false);
		door_exterior.setTextureOffset(0, 182).addCuboid(-20, -13, 14, 0, 13, 5, 0, false);

		upper_wall_2_r2 = new ModelPart(this);
		upper_wall_2_r2.setPivot(-20, -13, 0);
		door_exterior.addChild(upper_wall_2_r2);
		setRotationAngle(upper_wall_2_r2, 0, 0, 0.1107F);
		upper_wall_2_r2.setTextureOffset(32, 197).addCuboid(0, -20, 14, 0, 20, 5, 0, false);
		upper_wall_2_r2.setTextureOffset(164, 99).addCuboid(0, -20, -19, 0, 20, 5, 0, false);

		door_left_exterior = new ModelPart(this);
		door_left_exterior.setPivot(0, 0, 0);
		door_exterior.addChild(door_left_exterior);
		door_left_exterior.setTextureOffset(149, 76).addCuboid(-20.8F, -13, 0, 0, 13, 15, 0, false);

		door_left_top_r2 = new ModelPart(this);
		door_left_top_r2.setPivot(-20.8F, -13, 0);
		door_left_exterior.addChild(door_left_top_r2);
		setRotationAngle(door_left_top_r2, 0, 0, 0.1107F);
		door_left_top_r2.setTextureOffset(166, 167).addCuboid(0, -20, 0, 0, 20, 15, 0, false);

		door_right_exterior = new ModelPart(this);
		door_right_exterior.setPivot(0, 0, 0);
		door_exterior.addChild(door_right_exterior);
		door_right_exterior.setTextureOffset(0, 116).addCuboid(-20.8F, -13, -15, 0, 13, 15, 0, false);

		door_right_top_r2 = new ModelPart(this);
		door_right_top_r2.setPivot(-20.8F, -13, 0);
		door_right_exterior.addChild(door_right_top_r2);
		setRotationAngle(door_right_top_r2, 0, 0, 0.1107F);
		door_right_top_r2.setTextureOffset(77, 81).addCuboid(0, -20, -15, 0, 20, 15, 0, false);

		door_handrails = new ModelPart(this);
		door_handrails.setPivot(0, 24, 0);


		handrail_1_r2 = new ModelPart(this);
		handrail_1_r2.setPivot(0, 0, 0);
		door_handrails.addChild(handrail_1_r2);
		setRotationAngle(handrail_1_r2, -1.5708F, 0, 0);
		handrail_1_r2.setTextureOffset(0, 0).addCuboid(-8, -19, -30.5F, 0, 38, 0, 0.2F, false);

		side_panel = new ModelPart(this);
		side_panel.setPivot(0, 24, 0);
		side_panel.setTextureOffset(287, 148).addCuboid(-18, -31, 0, 13, 31, 0, 0, false);

		handrail_11_r1 = new ModelPart(this);
		handrail_11_r1.setPivot(0, 0, 0);
		side_panel.addChild(handrail_11_r1);
		setRotationAngle(handrail_11_r1, 0, 0, 0);
		handrail_11_r1.setTextureOffset(0, 0).addCuboid(-6.3963F, -12.17F, 0, 0, 13, 0, 0.2F, false);

		handrail_5_r1 = new ModelPart(this);
		handrail_5_r1.setPivot(-7.8F, -30.7F, 0);
		side_panel.addChild(handrail_5_r1);
		setRotationAngle(handrail_5_r1, 0, 0, -0.0873F);
		handrail_5_r1.setTextureOffset(0, 0).addCuboid(-0.2F, 0.2F, 0, 0, 18, 0, 0.2F, false);

		roof_window = new ModelPart(this);
		roof_window.setPivot(0, 24, 0);
		roof_window.setTextureOffset(50, 46).addCuboid(-9.775F, -32.575F, -15, 7, 0, 30, 0, false);
		roof_window.setTextureOffset(0, 0).addCuboid(-3, -33.575F, -15, 3, 0, 30, 0, false);

		inner_roof_4_r1 = new ModelPart(this);
		inner_roof_4_r1.setPivot(-2.775F, -33.076F, 0);
		roof_window.addChild(inner_roof_4_r1);
		setRotationAngle(inner_roof_4_r1, 0, 0, 1.5708F);
		inner_roof_4_r1.setTextureOffset(6, 0).addCuboid(-0.5F, 0, -15, 1, 0, 30, 0, false);

		inner_roof_2_r1 = new ModelPart(this);
		inner_roof_2_r1.setPivot(-9.775F, -32.575F, 0);
		roof_window.addChild(inner_roof_2_r1);
		setRotationAngle(inner_roof_2_r1, 0, 0, 1.309F);
		inner_roof_2_r1.setTextureOffset(0, 46).addCuboid(-2, 0, -15, 2, 0, 30, 0, false);

		inner_roof_1_r1 = new ModelPart(this);
		inner_roof_1_r1.setPivot(0, 0, 0);
		roof_window.addChild(inner_roof_1_r1);
		setRotationAngle(inner_roof_1_r1, 0, 0, -0.3491F);
		inner_roof_1_r1.setTextureOffset(64, 46).addCuboid(-4.75F, -35, -15, 7, 0, 30, 0, false);

		roof_door = new ModelPart(this);
		roof_door.setPivot(0, 24, 0);
		roof_door.setTextureOffset(215, 208).addCuboid(-18, -32, -19, 3, 1, 38, 0, false);
		roof_door.setTextureOffset(103, 0).addCuboid(-9.775F, -32.575F, -19, 7, 0, 38, 0, false);
		roof_door.setTextureOffset(166, 169).addCuboid(-3, -33.575F, -19, 3, 0, 38, 0, false);

		inner_roof_6_r1 = new ModelPart(this);
		inner_roof_6_r1.setPivot(-2.775F, -33.076F, 0);
		roof_door.addChild(inner_roof_6_r1);
		setRotationAngle(inner_roof_6_r1, 0, 0, 1.5708F);
		inner_roof_6_r1.setTextureOffset(8, 96).addCuboid(-0.5F, 0, -19, 1, 0, 38, 0, false);

		inner_roof_4_r2 = new ModelPart(this);
		inner_roof_4_r2.setPivot(-9.775F, -32.575F, 0);
		roof_door.addChild(inner_roof_4_r2);
		setRotationAngle(inner_roof_4_r2, 0, 0, 1.309F);
		inner_roof_4_r2.setTextureOffset(0, 96).addCuboid(-2, 0, -19, 2, 0, 38, 0, false);

		inner_roof_3_r1 = new ModelPart(this);
		inner_roof_3_r1.setPivot(0, 0, 0);
		roof_door.addChild(inner_roof_3_r1);
		setRotationAngle(inner_roof_3_r1, 0, 0, -0.3491F);
		inner_roof_3_r1.setTextureOffset(0, 46).addCuboid(0.25F, -35, -19, 2, 0, 38, 0, false);

		inner_roof_2_r2 = new ModelPart(this);
		inner_roof_2_r2.setPivot(-13.863F, -32.0306F, 0);
		roof_door.addChild(inner_roof_2_r2);
		setRotationAngle(inner_roof_2_r2, 0, 0, -0.5236F);
		inner_roof_2_r2.setTextureOffset(204, 169).addCuboid(-1.5F, -0.675F, -19, 5, 1, 38, 0, false);

		roof_window_light = new ModelPart(this);
		roof_window_light.setPivot(0, 24, 0);
		roof_window_light.setTextureOffset(37, 102).addCuboid(-2.5F, -33.1F, -15, 5, 0, 30, 0, false);

		roof_door_light = new ModelPart(this);
		roof_door_light.setPivot(0, 24, 0);
		roof_door_light.setTextureOffset(29, 98).addCuboid(-2.5F, -33.1F, -19, 5, 0, 38, 0, false);

		roof_exterior_window = new ModelPart(this);
		roof_exterior_window.setPivot(0, 24, 0);
		roof_exterior_window.setTextureOffset(173, 0).addCuboid(-6, -42, -15, 6, 0, 30, 0, false);

		outer_roof_5_r1 = new ModelPart(this);
		outer_roof_5_r1.setPivot(-9.9394F, -41.3064F, 0);
		roof_exterior_window.addChild(outer_roof_5_r1);
		setRotationAngle(outer_roof_5_r1, 0, 0, -0.1745F);
		outer_roof_5_r1.setTextureOffset(95, 0).addCuboid(-4, 0, -15, 8, 0, 30, 0, false);

		outer_roof_4_r1 = new ModelPart(this);
		outer_roof_4_r1.setPivot(-15.1778F, -39.8628F, 0);
		roof_exterior_window.addChild(outer_roof_4_r1);
		setRotationAngle(outer_roof_4_r1, 0, 0, -0.5236F);
		outer_roof_4_r1.setTextureOffset(125, 0).addCuboid(-1.5F, 0, -15, 3, 0, 30, 0, false);

		outer_roof_3_r1 = new ModelPart(this);
		outer_roof_3_r1.setPivot(-16.9769F, -38.2468F, 0);
		roof_exterior_window.addChild(outer_roof_3_r1);
		setRotationAngle(outer_roof_3_r1, 0, 0, -1.0472F);
		outer_roof_3_r1.setTextureOffset(8, 0).addCuboid(-1, 0, -15, 2, 0, 30, 0, false);

		outer_roof_2_r1 = new ModelPart(this);
		outer_roof_2_r1.setPivot(-17.5872F, -36.3872F, 0);
		roof_exterior_window.addChild(outer_roof_2_r1);
		setRotationAngle(outer_roof_2_r1, 0, 0, 0.1107F);
		outer_roof_2_r1.setTextureOffset(133, 9).addCuboid(0, -1, -15, 0, 2, 30, 0, false);

		outer_roof_1_r1 = new ModelPart(this);
		outer_roof_1_r1.setPivot(-20, -13, 0);
		roof_exterior_window.addChild(outer_roof_1_r1);
		setRotationAngle(outer_roof_1_r1, 0, 0, 0.1107F);
		outer_roof_1_r1.setTextureOffset(62, 237).addCuboid(-1, -23, -15, 1, 4, 30, 0, false);

		roof_exterior_door = new ModelPart(this);
		roof_exterior_door.setPivot(0, 24, 0);
		roof_exterior_door.setTextureOffset(165, 0).addCuboid(-6, -42, -19, 6, 0, 38, 0, false);

		outer_roof_6_r1 = new ModelPart(this);
		outer_roof_6_r1.setPivot(-9.9394F, -41.3064F, 0);
		roof_exterior_door.addChild(outer_roof_6_r1);
		setRotationAngle(outer_roof_6_r1, 0, 0, -0.1745F);
		outer_roof_6_r1.setTextureOffset(87, 0).addCuboid(-4, 0, -19, 8, 0, 38, 0, false);

		outer_roof_5_r2 = new ModelPart(this);
		outer_roof_5_r2.setPivot(-15.1778F, -39.8628F, 0);
		roof_exterior_door.addChild(outer_roof_5_r2);
		setRotationAngle(outer_roof_5_r2, 0, 0, -0.5236F);
		outer_roof_5_r2.setTextureOffset(117, 0).addCuboid(-1.5F, 0, -19, 3, 0, 38, 0, false);

		outer_roof_4_r2 = new ModelPart(this);
		outer_roof_4_r2.setPivot(-16.9769F, -38.2468F, 0);
		roof_exterior_door.addChild(outer_roof_4_r2);
		setRotationAngle(outer_roof_4_r2, 0, 0, -1.0472F);
		outer_roof_4_r2.setTextureOffset(0, 0).addCuboid(-1, 0, -19, 2, 0, 38, 0, false);

		outer_roof_3_r2 = new ModelPart(this);
		outer_roof_3_r2.setPivot(-17.5872F, -36.3872F, 0);
		roof_exterior_door.addChild(outer_roof_3_r2);
		setRotationAngle(outer_roof_3_r2, 0, 0, 0.1107F);
		outer_roof_3_r2.setTextureOffset(125, 1).addCuboid(0, -1, -19, 0, 2, 38, 0, false);

		outer_roof_2_r2 = new ModelPart(this);
		outer_roof_2_r2.setPivot(-20, -13, 0);
		roof_exterior_door.addChild(outer_roof_2_r2);
		setRotationAngle(outer_roof_2_r2, 0, 0, 0.1107F);
		outer_roof_2_r2.setTextureOffset(54, 229).addCuboid(-1, -23, -19, 1, 4, 38, 0, false);

		end = new ModelPart(this);
		end.setPivot(0, 24, 0);
		end.setTextureOffset(0, 0).addCuboid(-20, 0, -32, 40, 1, 45, 0, false);
		end.setTextureOffset(102, 120).addCuboid(-18, -13, -36, 0, 13, 49, 0, false);
		end.setTextureOffset(132, 29).addCuboid(18, -13, -36, 0, 13, 49, 0, true);
		end.setTextureOffset(150, 291).addCuboid(6, -32, -36, 12, 32, 12, 0, true);
		end.setTextureOffset(198, 291).addCuboid(-18, -32, -36, 12, 32, 12, 0, false);
		end.setTextureOffset(215, 0).addCuboid(-18, -35, -36, 36, 3, 12, 0, false);
		end.setTextureOffset(0, 291).addCuboid(-6, -32, -24, 12, 32, 0, 0, false);

		upper_wall_2_r3 = new ModelPart(this);
		upper_wall_2_r3.setPivot(-20, -13, 0);
		end.addChild(upper_wall_2_r3);
		setRotationAngle(upper_wall_2_r3, 0, 0, 0.1107F);
		upper_wall_2_r3.setTextureOffset(102, 98).addCuboid(2, -20, -36, 0, 20, 49, 0, false);

		upper_wall_1_r1 = new ModelPart(this);
		upper_wall_1_r1.setPivot(20, -13, 0);
		end.addChild(upper_wall_1_r1);
		setRotationAngle(upper_wall_1_r1, 0, 0, -0.1107F);
		upper_wall_1_r1.setTextureOffset(0, 118).addCuboid(-2, -20, -36, 0, 20, 49, 0, true);

		seat_1 = new ModelPart(this);
		seat_1.setPivot(0, 0, 0);
		end.addChild(seat_1);
		seat_1.setTextureOffset(0, 85).addCuboid(6, -6, -23, 12, 1, 7, 0, false);

		seat_back_r3 = new ModelPart(this);
		seat_back_r3.setPivot(0, -6, -22);
		seat_1.addChild(seat_back_r3);
		setRotationAngle(seat_back_r3, 0.0524F, 0, 0);
		seat_back_r3.setTextureOffset(53, 138).addCuboid(6, -8, -1, 12, 8, 1, 0, false);

		seat_2 = new ModelPart(this);
		seat_2.setPivot(0, 0, 0);
		end.addChild(seat_2);
		seat_2.setTextureOffset(0, 85).addCuboid(6, -6, -11, 12, 1, 7, 0, false);

		seat_back_r4 = new ModelPart(this);
		seat_back_r4.setPivot(0, -6, -10);
		seat_2.addChild(seat_back_r4);
		setRotationAngle(seat_back_r4, 0.0524F, 0, 0);
		seat_back_r4.setTextureOffset(53, 138).addCuboid(6, -8, -1, 12, 8, 1, 0, false);

		seat_3 = new ModelPart(this);
		seat_3.setPivot(0, 0, 0);
		end.addChild(seat_3);
		seat_3.setTextureOffset(0, 85).addCuboid(6, -6, 5, 12, 1, 7, 0, false);

		seat_back_r5 = new ModelPart(this);
		seat_back_r5.setPivot(0, -6, 11);
		seat_3.addChild(seat_back_r5);
		setRotationAngle(seat_back_r5, -0.0524F, 0, 0);
		seat_back_r5.setTextureOffset(53, 138).addCuboid(6, -8, 0, 12, 8, 1, 0, false);

		seat_4 = new ModelPart(this);
		seat_4.setPivot(0, 0, 0);
		end.addChild(seat_4);
		seat_4.setTextureOffset(0, 85).addCuboid(-18, -6, -23, 12, 1, 7, 0, true);

		seat_back_r6 = new ModelPart(this);
		seat_back_r6.setPivot(0, -6, -22);
		seat_4.addChild(seat_back_r6);
		setRotationAngle(seat_back_r6, 0.0524F, 0, 0);
		seat_back_r6.setTextureOffset(53, 138).addCuboid(-18, -8, -1, 12, 8, 1, 0, true);

		seat_5 = new ModelPart(this);
		seat_5.setPivot(0, 0, 0);
		end.addChild(seat_5);
		seat_5.setTextureOffset(0, 85).addCuboid(-18, -6, -11, 12, 1, 7, 0, true);

		seat_back_r7 = new ModelPart(this);
		seat_back_r7.setPivot(0, -6, -10);
		seat_5.addChild(seat_back_r7);
		setRotationAngle(seat_back_r7, 0.0524F, 0, 0);
		seat_back_r7.setTextureOffset(53, 138).addCuboid(-18, -8, -1, 12, 8, 1, 0, true);

		seat_6 = new ModelPart(this);
		seat_6.setPivot(0, 0, 0);
		end.addChild(seat_6);
		seat_6.setTextureOffset(0, 85).addCuboid(-18, -6, 5, 12, 1, 7, 0, true);

		seat_back_r8 = new ModelPart(this);
		seat_back_r8.setPivot(0, -6, 11);
		seat_6.addChild(seat_back_r8);
		setRotationAngle(seat_back_r8, -0.0524F, 0, 0);
		seat_back_r8.setTextureOffset(53, 138).addCuboid(-18, -8, 0, 12, 8, 1, 0, true);

		end_exterior = new ModelPart(this);
		end_exterior.setPivot(0, 24, 0);
		end_exterior.setTextureOffset(0, 187).addCuboid(20, 0, -32, 1, 2, 45, 0, true);
		end_exterior.setTextureOffset(0, 187).addCuboid(-21, 0, -32, 1, 2, 45, 0, false);
		end_exterior.setTextureOffset(151, 169).addCuboid(18, -13, -36, 2, 13, 49, 0, true);
		end_exterior.setTextureOffset(49, 167).addCuboid(-20, -13, -36, 2, 13, 49, 0, false);
		end_exterior.setTextureOffset(59, 234).addCuboid(6, -33, -36, 12, 33, 0, 0, true);
		end_exterior.setTextureOffset(35, 234).addCuboid(-18, -33, -36, 12, 33, 0, 0, false);
		end_exterior.setTextureOffset(172, 231).addCuboid(-18, -41, -36, 36, 9, 0, 0, false);

		upper_wall_2_r4 = new ModelPart(this);
		upper_wall_2_r4.setPivot(-20, -13, 0);
		end_exterior.addChild(upper_wall_2_r4);
		setRotationAngle(upper_wall_2_r4, 0, 0, 0.1107F);
		upper_wall_2_r4.setTextureOffset(79, 78).addCuboid(0, -20, -36, 2, 20, 49, 0, false);

		upper_wall_1_r2 = new ModelPart(this);
		upper_wall_1_r2.setPivot(20, -13, 0);
		end_exterior.addChild(upper_wall_1_r2);
		setRotationAngle(upper_wall_1_r2, 0, 0, -0.1107F);
		upper_wall_1_r2.setTextureOffset(0, 98).addCuboid(-2, -20, -36, 2, 20, 49, 0, true);

		roof_end = new ModelPart(this);
		roof_end.setPivot(0, 24, 0);
		roof_end.setTextureOffset(16, 96).addCuboid(-9.775F, -32.575F, -24, 7, 0, 37, 0, false);
		roof_end.setTextureOffset(5, 46).addCuboid(-3, -33.575F, -24, 3, 0, 37, 0, false);
		roof_end.setTextureOffset(16, 96).addCuboid(2.775F, -32.575F, -24, 7, 0, 37, 0, true);
		roof_end.setTextureOffset(5, 46).addCuboid(0, -33.575F, -24, 3, 0, 37, 0, true);

		inner_roof_5_r1 = new ModelPart(this);
		inner_roof_5_r1.setPivot(2.775F, -33.076F, 0);
		roof_end.addChild(inner_roof_5_r1);
		setRotationAngle(inner_roof_5_r1, 0, 0, -1.5708F);
		inner_roof_5_r1.setTextureOffset(5, 0).addCuboid(-0.5F, 0, -24, 1, 0, 37, 0, true);

		inner_roof_3_r2 = new ModelPart(this);
		inner_roof_3_r2.setPivot(9.775F, -32.575F, 0);
		roof_end.addChild(inner_roof_3_r2);
		setRotationAngle(inner_roof_3_r2, 0, 0, -1.309F);
		inner_roof_3_r2.setTextureOffset(5, 96).addCuboid(0, 0, -24, 2, 0, 37, 0, true);

		inner_roof_2_r3 = new ModelPart(this);
		inner_roof_2_r3.setPivot(0, 0, 0);
		roof_end.addChild(inner_roof_2_r3);
		setRotationAngle(inner_roof_2_r3, 0, 0, 0.3491F);
		inner_roof_2_r3.setTextureOffset(144, 91).addCuboid(-2.25F, -35, -24, 7, 0, 37, 0, true);

		inner_roof_4_r3 = new ModelPart(this);
		inner_roof_4_r3.setPivot(-2.775F, -33.076F, 0);
		roof_end.addChild(inner_roof_4_r3);
		setRotationAngle(inner_roof_4_r3, 0, 0, 1.5708F);
		inner_roof_4_r3.setTextureOffset(5, 0).addCuboid(-0.5F, 0, -24, 1, 0, 37, 0, false);

		inner_roof_2_r4 = new ModelPart(this);
		inner_roof_2_r4.setPivot(-9.775F, -32.575F, 0);
		roof_end.addChild(inner_roof_2_r4);
		setRotationAngle(inner_roof_2_r4, 0, 0, 1.309F);
		inner_roof_2_r4.setTextureOffset(5, 96).addCuboid(-2, 0, -24, 2, 0, 37, 0, false);

		inner_roof_1_r2 = new ModelPart(this);
		inner_roof_1_r2.setPivot(0, 0, 0);
		roof_end.addChild(inner_roof_1_r2);
		setRotationAngle(inner_roof_1_r2, 0, 0, -0.3491F);
		inner_roof_1_r2.setTextureOffset(144, 91).addCuboid(-4.75F, -35, -24, 7, 0, 37, 0, false);

		roof_end_light = new ModelPart(this);
		roof_end_light.setPivot(0, 24, 0);
		roof_end_light.setTextureOffset(25, 96).addCuboid(-2.5F, -33.1F, -24, 5, 0, 42, 0, false);

		roof_end_handrails = new ModelPart(this);
		roof_end_handrails.setPivot(0, 24, 0);
		roof_end_handrails.setTextureOffset(4, 41).addCuboid(-9, -31, 7, 2, 4, 0, 0, false);
		roof_end_handrails.setTextureOffset(4, 41).addCuboid(-9, -31, 0, 2, 4, 0, 0, false);
		roof_end_handrails.setTextureOffset(4, 41).addCuboid(-9, -31, -7, 2, 4, 0, 0, false);
		roof_end_handrails.setTextureOffset(0, 0).addCuboid(-8, -33.4899F, -16.9899F, 0, 2, 0, 0.2F, false);
		roof_end_handrails.setTextureOffset(4, 41).addCuboid(7, -31, 7, 2, 4, 0, 0, true);
		roof_end_handrails.setTextureOffset(4, 41).addCuboid(7, -31, 0, 2, 4, 0, 0, true);
		roof_end_handrails.setTextureOffset(4, 41).addCuboid(7, -31, -7, 2, 4, 0, 0, true);
		roof_end_handrails.setTextureOffset(0, 0).addCuboid(8, -33.4899F, -16.9899F, 0, 2, 0, 0.2F, true);

		handrail_11_r2 = new ModelPart(this);
		handrail_11_r2.setPivot(10, -30.9364F, -16.5536F);
		roof_end_handrails.addChild(handrail_11_r2);
		setRotationAngle(handrail_11_r2, 0.7854F, 0, 0);
		handrail_11_r2.setTextureOffset(0, 0).addCuboid(-2, -0.5F, 0, 0, 1, 0, 0.2F, true);
		handrail_11_r2.setTextureOffset(0, 0).addCuboid(-18, -0.5F, 0, 0, 1, 0, 0.2F, false);

		handrail_10_r1 = new ModelPart(this);
		handrail_10_r1.setPivot(0, 0, 0);
		roof_end_handrails.addChild(handrail_10_r1);
		setRotationAngle(handrail_10_r1, -1.5708F, 0, 0);
		handrail_10_r1.setTextureOffset(0, 0).addCuboid(8, -16, -30.5F, 0, 32, 0, 0.2F, true);
		handrail_10_r1.setTextureOffset(0, 0).addCuboid(-8, -16, -30.5F, 0, 32, 0, 0.2F, false);

		roof_end_exterior = new ModelPart(this);
		roof_end_exterior.setPivot(0, 24, 0);


		outer_roof_1 = new ModelPart(this);
		outer_roof_1.setPivot(0, 0, 0);
		roof_end_exterior.addChild(outer_roof_1);
		outer_roof_1.setTextureOffset(93, 79).addCuboid(-6, -42, -36, 6, 1, 11, 0, false);

		upper_wall_1_r3 = new ModelPart(this);
		upper_wall_1_r3.setPivot(-20, -13, 0);
		outer_roof_1.addChild(upper_wall_1_r3);
		setRotationAngle(upper_wall_1_r3, 0, 0, 0.1107F);
		upper_wall_1_r3.setTextureOffset(181, 133).addCuboid(0, -23, -36, 1, 4, 7, 0, false);
		upper_wall_1_r3.setTextureOffset(34, 134).addCuboid(-1, -23, -29, 1, 4, 4, 0, false);

		outer_roof_5_r3 = new ModelPart(this);
		outer_roof_5_r3.setPivot(-9.7656F, -40.3206F, 0);
		outer_roof_1.addChild(outer_roof_5_r3);
		setRotationAngle(outer_roof_5_r3, 0, 0, -0.1745F);
		outer_roof_5_r3.setTextureOffset(155, 204).addCuboid(-4, -1, -36, 8, 1, 11, 0, false);

		outer_roof_4_r3 = new ModelPart(this);
		outer_roof_4_r3.setPivot(-14.6775F, -38.9948F, 0);
		outer_roof_1.addChild(outer_roof_4_r3);
		setRotationAngle(outer_roof_4_r3, 0, 0, -0.5236F);
		outer_roof_4_r3.setTextureOffset(17, 31).addCuboid(-1.5F, -1, -36, 3, 1, 11, 0, false);

		outer_roof_3_r3 = new ModelPart(this);
		outer_roof_3_r3.setPivot(-16.1105F, -37.7448F, 0);
		outer_roof_1.addChild(outer_roof_3_r3);
		setRotationAngle(outer_roof_3_r3, 0, 0, -1.0472F);
		outer_roof_3_r3.setTextureOffset(19, 133).addCuboid(-1, -1, -36, 2, 1, 11, 0, false);

		outer_roof_2_r3 = new ModelPart(this);
		outer_roof_2_r3.setPivot(-17.587F, -36.3849F, 0);
		outer_roof_1.addChild(outer_roof_2_r3);
		setRotationAngle(outer_roof_2_r3, 0, 0, 0.1107F);
		outer_roof_2_r3.setTextureOffset(215, 15).addCuboid(0, -1, -36, 1, 2, 11, 0, false);

		outer_roof_2 = new ModelPart(this);
		outer_roof_2.setPivot(0, 0, 0);
		roof_end_exterior.addChild(outer_roof_2);
		outer_roof_2.setTextureOffset(93, 79).addCuboid(0, -42, -36, 6, 1, 11, 0, true);

		outer_roof_5_r4 = new ModelPart(this);
		outer_roof_5_r4.setPivot(9.7656F, -40.3206F, 0);
		outer_roof_2.addChild(outer_roof_5_r4);
		setRotationAngle(outer_roof_5_r4, 0, 0, 0.1745F);
		outer_roof_5_r4.setTextureOffset(155, 204).addCuboid(-4, -1, -36, 8, 1, 11, 0, true);

		outer_roof_4_r4 = new ModelPart(this);
		outer_roof_4_r4.setPivot(14.6775F, -38.9948F, 0);
		outer_roof_2.addChild(outer_roof_4_r4);
		setRotationAngle(outer_roof_4_r4, 0, 0, 0.5236F);
		outer_roof_4_r4.setTextureOffset(17, 31).addCuboid(-1.5F, -1, -36, 3, 1, 11, 0, true);

		outer_roof_3_r4 = new ModelPart(this);
		outer_roof_3_r4.setPivot(16.1105F, -37.7448F, 0);
		outer_roof_2.addChild(outer_roof_3_r4);
		setRotationAngle(outer_roof_3_r4, 0, 0, 1.0472F);
		outer_roof_3_r4.setTextureOffset(19, 133).addCuboid(-1, -1, -36, 2, 1, 11, 0, true);

		outer_roof_2_r4 = new ModelPart(this);
		outer_roof_2_r4.setPivot(17.587F, -36.3849F, 0);
		outer_roof_2.addChild(outer_roof_2_r4);
		setRotationAngle(outer_roof_2_r4, 0, 0, -0.1107F);
		outer_roof_2_r4.setTextureOffset(215, 15).addCuboid(-1, -1, -36, 1, 2, 11, 0, true);

		outer_roof_1_r2 = new ModelPart(this);
		outer_roof_1_r2.setPivot(20, -13, 0);
		outer_roof_2.addChild(outer_roof_1_r2);
		setRotationAngle(outer_roof_1_r2, 0, 0, -0.1107F);
		outer_roof_1_r2.setTextureOffset(34, 134).addCuboid(0, -23, -29, 1, 4, 4, 0, true);
		outer_roof_1_r2.setTextureOffset(181, 133).addCuboid(-1, -23, -36, 1, 4, 7, 0, true);

		roof_end_vents = new ModelPart(this);
		roof_end_vents.setPivot(0, 24, 0);
		roof_end_vents.setTextureOffset(0, 46).addCuboid(-8, -43, -21, 16, 2, 48, 0, false);

		vent_3_r1 = new ModelPart(this);
		vent_3_r1.setPivot(-8, -43, 12);
		roof_end_vents.addChild(vent_3_r1);
		setRotationAngle(vent_3_r1, 0, 0, -0.3491F);
		vent_3_r1.setTextureOffset(152, 119).addCuboid(-9, 0, -33, 9, 2, 48, 0, true);

		vent_2_r1 = new ModelPart(this);
		vent_2_r1.setPivot(8, -43, 12);
		roof_end_vents.addChild(vent_2_r1);
		setRotationAngle(vent_2_r1, 0, 0, 0.3491F);
		vent_2_r1.setTextureOffset(152, 119).addCuboid(0, 0, -33, 9, 2, 48, 0, false);

		head = new ModelPart(this);
		head.setPivot(0, 24, 0);
		head.setTextureOffset(80, 46).addCuboid(-20, 0, -13, 40, 1, 31, 0, false);
		head.setTextureOffset(259, 192).addCuboid(-20, -13, -13, 2, 13, 31, 0, false);
		head.setTextureOffset(252, 148).addCuboid(18, -13, -13, 2, 13, 31, 0, true);
		head.setTextureOffset(261, 15).addCuboid(-18, -36, 18, 36, 36, 0, 0, false);

		upper_wall_2_r5 = new ModelPart(this);
		upper_wall_2_r5.setPivot(20, -13, 0);
		head.addChild(upper_wall_2_r5);
		setRotationAngle(upper_wall_2_r5, 0, 0, -0.1107F);
		upper_wall_2_r5.setTextureOffset(0, 240).addCuboid(-2, -20, -13, 2, 20, 31, 0, true);

		upper_wall_1_r4 = new ModelPart(this);
		upper_wall_1_r4.setPivot(-20, -13, 0);
		head.addChild(upper_wall_1_r4);
		setRotationAngle(upper_wall_1_r4, 0, 0, 0.1107F);
		upper_wall_1_r4.setTextureOffset(179, 240).addCuboid(0, -20, -13, 2, 20, 31, 0, false);

		seat_9 = new ModelPart(this);
		seat_9.setPivot(0, 0, 2);
		head.addChild(seat_9);
		seat_9.setTextureOffset(0, 85).addCuboid(-18, -6, 8, 12, 1, 7, 0, true);

		seat_back_r9 = new ModelPart(this);
		seat_back_r9.setPivot(0, -6, 14);
		seat_9.addChild(seat_back_r9);
		setRotationAngle(seat_back_r9, -0.0524F, 0, 0);
		seat_back_r9.setTextureOffset(53, 138).addCuboid(-18, -8, 0, 12, 8, 1, 0, true);

		seat_10 = new ModelPart(this);
		seat_10.setPivot(0, 0, -2);
		head.addChild(seat_10);
		setRotationAngle(seat_10, 0, 3.1416F, 0);
		seat_10.setTextureOffset(0, 85).addCuboid(6, -6, 3, 12, 1, 7, 0, false);

		seat_back_r10 = new ModelPart(this);
		seat_back_r10.setPivot(0, -6, 9);
		seat_10.addChild(seat_back_r10);
		setRotationAngle(seat_back_r10, -0.0524F, 0, 0);
		seat_back_r10.setTextureOffset(53, 138).addCuboid(6, -8, 0, 12, 8, 1, 0, false);

		seat_11 = new ModelPart(this);
		seat_11.setPivot(24, 0, -2);
		head.addChild(seat_11);
		setRotationAngle(seat_11, 0, 3.1416F, 0);
		seat_11.setTextureOffset(0, 85).addCuboid(6, -6, 3, 12, 1, 7, 0, false);

		seat_back_r11 = new ModelPart(this);
		seat_back_r11.setPivot(0, -6, 9);
		seat_11.addChild(seat_back_r11);
		setRotationAngle(seat_back_r11, -0.0524F, 0, 0);
		seat_back_r11.setTextureOffset(53, 138).addCuboid(6, -8, 0, 12, 8, 1, 0, false);

		seat_12 = new ModelPart(this);
		seat_12.setPivot(0, 0, 2);
		head.addChild(seat_12);
		seat_12.setTextureOffset(0, 85).addCuboid(6, -6, 8, 12, 1, 7, 0, true);

		seat_back_r12 = new ModelPart(this);
		seat_back_r12.setPivot(24, -6, 14);
		seat_12.addChild(seat_back_r12);
		setRotationAngle(seat_back_r12, -0.0524F, 0, 0);
		seat_back_r12.setTextureOffset(53, 138).addCuboid(-18, -8, 0, 12, 8, 1, 0, true);

		head_exterior = new ModelPart(this);
		head_exterior.setPivot(0, 24, 0);
		head_exterior.setTextureOffset(195, 91).addCuboid(-21, 0, 18, 42, 2, 13, 0, false);
		head_exterior.setTextureOffset(18, 58).addCuboid(21, 2, 22, 0, 5, 6, 0, true);
		head_exterior.setTextureOffset(18, 58).addCuboid(-21, 2, 22, 0, 5, 6, 0, false);
		head_exterior.setTextureOffset(218, 116).addCuboid(20, -13, -13, 0, 13, 32, 0, true);
		head_exterior.setTextureOffset(88, 271).addCuboid(20, -13, 19, 1, 13, 12, 0, true);
		head_exterior.setTextureOffset(102, 170).addCuboid(-20, -13, -13, 0, 13, 32, 0, false);
		head_exterior.setTextureOffset(88, 271).addCuboid(-21, -13, 19, 1, 13, 12, 0, false);
		head_exterior.setTextureOffset(271, 51).addCuboid(20, 0, -13, 1, 2, 31, 0, false);
		head_exterior.setTextureOffset(271, 51).addCuboid(-21, 0, -13, 1, 2, 31, 0, false);
		head_exterior.setTextureOffset(218, 106).addCuboid(-20, -42, 19, 40, 42, 0, 0, false);

		driver_door_upper_2_r1 = new ModelPart(this);
		driver_door_upper_2_r1.setPivot(-21, -13, 0);
		head_exterior.addChild(driver_door_upper_2_r1);
		setRotationAngle(driver_door_upper_2_r1, 0, 0, 0.1107F);
		driver_door_upper_2_r1.setTextureOffset(0, 234).addCuboid(0, -20, 19, 1, 20, 12, 0, false);

		upper_wall_2_r6 = new ModelPart(this);
		upper_wall_2_r6.setPivot(-20, -13, 0);
		head_exterior.addChild(upper_wall_2_r6);
		setRotationAngle(upper_wall_2_r6, 0, 0, 0.1107F);
		upper_wall_2_r6.setTextureOffset(102, 150).addCuboid(0, -20, -13, 0, 20, 32, 0, false);

		driver_door_upper_1_r1 = new ModelPart(this);
		driver_door_upper_1_r1.setPivot(21, -13, 0);
		head_exterior.addChild(driver_door_upper_1_r1);
		setRotationAngle(driver_door_upper_1_r1, 0, 0, -0.1107F);
		driver_door_upper_1_r1.setTextureOffset(0, 234).addCuboid(-1, -20, 19, 1, 20, 12, 0, true);

		upper_wall_1_r5 = new ModelPart(this);
		upper_wall_1_r5.setPivot(20, -13, 0);
		head_exterior.addChild(upper_wall_1_r5);
		setRotationAngle(upper_wall_1_r5, 0, 0, -0.1107F);
		upper_wall_1_r5.setTextureOffset(94, 199).addCuboid(0, -20, -13, 0, 20, 32, 0, true);

		front = new ModelPart(this);
		front.setPivot(0, 0, 0);
		head_exterior.addChild(front);
		front.setTextureOffset(214, 70).addCuboid(-20, 2, 31, 40, 0, 8, 0, false);

		head_roof_r1 = new ModelPart(this);
		head_roof_r1.setPivot(0, -41.2432F, 30.9176F);
		front.addChild(head_roof_r1);
		setRotationAngle(head_roof_r1, -0.1309F, 0, 0);
		head_roof_r1.setTextureOffset(73, 116).addCuboid(-6, -0.5F, -2, 12, 0, 4, 0, false);

		head_top_r1 = new ModelPart(this);
		head_top_r1.setPivot(0, -35.7151F, 31.9514F);
		front.addChild(head_top_r1);
		setRotationAngle(head_top_r1, 0.1745F, 0, 0);
		head_top_r1.setTextureOffset(0, 215).addCuboid(-6, -5.5F, -2, 12, 11, 4, 0, false);

		head_bottom_1_r1 = new ModelPart(this);
		head_bottom_1_r1.setPivot(0, 2, 39);
		front.addChild(head_bottom_1_r1);
		setRotationAngle(head_bottom_1_r1, -0.3054F, 0, 0);
		head_bottom_1_r1.setTextureOffset(230, 84).addCuboid(-20, -4, 0, 40, 4, 0, 0, false);

		side_1 = new ModelPart(this);
		side_1.setPivot(0, 0, 0);
		front.addChild(side_1);
		side_1.setTextureOffset(76, 90).addCuboid(0, -42, 25, 6, 0, 4, 0, true);

		outer_head_4_r1 = new ModelPart(this);
		outer_head_4_r1.setPivot(13, -22.275F, 36.3511F);
		side_1.addChild(outer_head_4_r1);
		setRotationAngle(outer_head_4_r1, 0.1745F, 0, 0);
		outer_head_4_r1.setTextureOffset(297, 236).addCuboid(-7, -19.5F, 0, 14, 41, 0, 0, false);

		outer_head_2_r1 = new ModelPart(this);
		outer_head_2_r1.setPivot(0, 0, 0);
		side_1.addChild(outer_head_2_r1);
		setRotationAngle(outer_head_2_r1, 0, -0.0873F, -0.1107F);
		outer_head_2_r1.setTextureOffset(79, 178).addCuboid(24, -35.5F, 28.75F, 0, 25, 9, 0, false);

		outer_head_1_r1 = new ModelPart(this);
		outer_head_1_r1.setPivot(0, 0, 0);
		side_1.addChild(outer_head_1_r1);
		setRotationAngle(outer_head_1_r1, 0, -0.0873F, 0);
		outer_head_1_r1.setTextureOffset(17, 177).addCuboid(22.7F, -13, 29, 0, 15, 10, 0, false);

		outer_roof_1_r3 = new ModelPart(this);
		outer_roof_1_r3.setPivot(20, -13, 5);
		side_1.addChild(outer_roof_1_r3);
		setRotationAngle(outer_roof_1_r3, 0, 0, -0.1107F);
		outer_roof_1_r3.setTextureOffset(195, 106).addCuboid(-1, -23, 20, 2, 4, 6, 0, true);

		outer_roof_3_r5 = new ModelPart(this);
		outer_roof_3_r5.setPivot(16.9769F, -38.2468F, 23.5F);
		side_1.addChild(outer_roof_3_r5);
		setRotationAngle(outer_roof_3_r5, 0, 0, 1.0472F);
		outer_roof_3_r5.setTextureOffset(28, 36).addCuboid(-1, 0, 1.5F, 2, 0, 6, 0, true);

		outer_roof_2_r5 = new ModelPart(this);
		outer_roof_2_r5.setPivot(17.0902F, -36.332F, 23.5F);
		side_1.addChild(outer_roof_2_r5);
		setRotationAngle(outer_roof_2_r5, 0, 0, -0.1107F);
		outer_roof_2_r5.setTextureOffset(132, 91).addCuboid(-0.5F, -1, 1.5F, 1, 2, 6, 0, true);

		outer_roof_4_r5 = new ModelPart(this);
		outer_roof_4_r5.setPivot(15.1778F, -39.8628F, 0);
		side_1.addChild(outer_roof_4_r5);
		setRotationAngle(outer_roof_4_r5, 0, 0, 0.5236F);
		outer_roof_4_r5.setTextureOffset(112, 83).addCuboid(-1.5F, 0, 25, 3, 0, 4, 0, true);

		outer_roof_11_r1 = new ModelPart(this);
		outer_roof_11_r1.setPivot(16.4774F, -39.1136F, 29.0001F);
		side_1.addChild(outer_roof_11_r1);
		setRotationAngle(outer_roof_11_r1, -0.2182F, -0.829F, 1.0472F);
		outer_roof_11_r1.setTextureOffset(24, 80).addCuboid(0, 0.001F, 0, 5, 0, 4, 0, true);

		outer_head_6_r1 = new ModelPart(this);
		outer_head_6_r1.setPivot(14.7995F, -39.2074F, 30.9176F);
		side_1.addChild(outer_head_6_r1);
		setRotationAngle(outer_head_6_r1, -0.1309F, 0, 0.5236F);
		outer_head_6_r1.setTextureOffset(111, 78).addCuboid(-1.5F, -0.5F, -2, 3, 0, 5, 0, true);

		outer_roof_5_r5 = new ModelPart(this);
		outer_roof_5_r5.setPivot(9.9394F, -41.3064F, 0);
		side_1.addChild(outer_roof_5_r5);
		setRotationAngle(outer_roof_5_r5, 0, 0, 0.1745F);
		outer_roof_5_r5.setTextureOffset(121, 41).addCuboid(-4, 0, 25, 8, 0, 4, 0, true);

		outer_head_5_r1 = new ModelPart(this);
		outer_head_5_r1.setPivot(9.808F, -40.5611F, 30.9176F);
		side_1.addChild(outer_head_5_r1);
		setRotationAngle(outer_head_5_r1, -0.1309F, 0, 0.1745F);
		outer_head_5_r1.setTextureOffset(72, 120).addCuboid(-4, -0.5F, -2, 8, 0, 5, 0, true);

		side_2 = new ModelPart(this);
		side_2.setPivot(0, 0, 0);
		front.addChild(side_2);
		side_2.setTextureOffset(76, 90).addCuboid(-6, -42, 25, 6, 0, 4, 0, false);

		outer_head_6_r2 = new ModelPart(this);
		outer_head_6_r2.setPivot(-14.7995F, -39.2074F, 30.9176F);
		side_2.addChild(outer_head_6_r2);
		setRotationAngle(outer_head_6_r2, -0.1309F, 0, -0.5236F);
		outer_head_6_r2.setTextureOffset(111, 78).addCuboid(-1.5F, -0.5F, -2, 3, 0, 5, 0, false);

		outer_head_5_r2 = new ModelPart(this);
		outer_head_5_r2.setPivot(-9.808F, -40.5611F, 30.9176F);
		side_2.addChild(outer_head_5_r2);
		setRotationAngle(outer_head_5_r2, -0.1309F, 0, -0.1745F);
		outer_head_5_r2.setTextureOffset(72, 120).addCuboid(-4, -0.5F, -2, 8, 0, 5, 0, false);

		outer_head_4_r2 = new ModelPart(this);
		outer_head_4_r2.setPivot(-13, -22.275F, 36.3511F);
		side_2.addChild(outer_head_4_r2);
		setRotationAngle(outer_head_4_r2, 0.1745F, 0, 0);
		outer_head_4_r2.setTextureOffset(298, 106).addCuboid(-7, -19.5F, 0, 14, 41, 0, 0, false);

		outer_head_2_r2 = new ModelPart(this);
		outer_head_2_r2.setPivot(0, 0, 0);
		side_2.addChild(outer_head_2_r2);
		setRotationAngle(outer_head_2_r2, 0, 0.0873F, 0.1107F);
		outer_head_2_r2.setTextureOffset(79, 178).addCuboid(-24, -35.5F, 28.75F, 0, 25, 9, 0, false);

		outer_head_1_r2 = new ModelPart(this);
		outer_head_1_r2.setPivot(0, 0, 0);
		side_2.addChild(outer_head_1_r2);
		setRotationAngle(outer_head_1_r2, 0, 0.0873F, 0);
		outer_head_1_r2.setTextureOffset(17, 86).addCuboid(-22.7F, -13, 29, 0, 15, 10, 0, false);

		outer_roof_11_r2 = new ModelPart(this);
		outer_roof_11_r2.setPivot(-16.4774F, -39.1136F, 29.0001F);
		side_2.addChild(outer_roof_11_r2);
		setRotationAngle(outer_roof_11_r2, -0.2182F, 0.829F, -1.0472F);
		outer_roof_11_r2.setTextureOffset(24, 80).addCuboid(-5, 0.001F, 0, 5, 0, 4, 0, false);

		outer_roof_5_r6 = new ModelPart(this);
		outer_roof_5_r6.setPivot(-9.9394F, -41.3064F, 0);
		side_2.addChild(outer_roof_5_r6);
		setRotationAngle(outer_roof_5_r6, 0, 0, -0.1745F);
		outer_roof_5_r6.setTextureOffset(121, 41).addCuboid(-4, 0, 25, 8, 0, 4, 0, false);

		outer_roof_4_r6 = new ModelPart(this);
		outer_roof_4_r6.setPivot(-15.1778F, -39.8628F, 0);
		side_2.addChild(outer_roof_4_r6);
		setRotationAngle(outer_roof_4_r6, 0, 0, -0.5236F);
		outer_roof_4_r6.setTextureOffset(112, 83).addCuboid(-1.5F, 0, 25, 3, 0, 4, 0, false);

		outer_roof_3_r6 = new ModelPart(this);
		outer_roof_3_r6.setPivot(-16.9769F, -38.2468F, 23.5F);
		side_2.addChild(outer_roof_3_r6);
		setRotationAngle(outer_roof_3_r6, 0, 0, -1.0472F);
		outer_roof_3_r6.setTextureOffset(28, 36).addCuboid(-1, 0, 1.5F, 2, 0, 6, 0, false);

		outer_roof_2_r6 = new ModelPart(this);
		outer_roof_2_r6.setPivot(-17.0902F, -36.332F, 23.5F);
		side_2.addChild(outer_roof_2_r6);
		setRotationAngle(outer_roof_2_r6, 0, 0, 0.1107F);
		outer_roof_2_r6.setTextureOffset(132, 91).addCuboid(-0.5F, -1, 1.5F, 1, 2, 6, 0, false);

		outer_roof_1_r4 = new ModelPart(this);
		outer_roof_1_r4.setPivot(-20, -13, 5);
		side_2.addChild(outer_roof_1_r4);
		setRotationAngle(outer_roof_1_r4, 0, 0, 0.1107F);
		outer_roof_1_r4.setTextureOffset(195, 106).addCuboid(-1, -23, 20, 2, 4, 6, 0, false);

		emergency_door = new ModelPart(this);
		emergency_door.setPivot(0, 0, 0);
		front.addChild(emergency_door);
		emergency_door.setTextureOffset(294, 192).addCuboid(-6, -31, 34, 12, 30, 0, 0, false);
		emergency_door.setTextureOffset(100, 293).addCuboid(6, -31, 31, 0, 30, 10, 0, false);
		emergency_door.setTextureOffset(120, 293).addCuboid(-6, -31, 31, 0, 30, 10, 0, false);
		emergency_door.setTextureOffset(68, 78).addCuboid(-6, -1, 30, 12, 0, 12, 0, false);

		pipe = new ModelPart(this);
		pipe.setPivot(0, -3.05F, -0.5F);
		head_exterior.addChild(pipe);


		valve_8_r1 = new ModelPart(this);
		valve_8_r1.setPivot(0, 0, 0);
		pipe.addChild(valve_8_r1);
		setRotationAngle(valve_8_r1, 0.1745F, 0, 0);
		valve_8_r1.setTextureOffset(34, 46).addCuboid(-10.175F, 2.125F, 39.55F, 1, 8, 1, 0, false);
		valve_8_r1.setTextureOffset(0, 85).addCuboid(-10.775F, 9.3F, 39.6F, 2, 3, 1, 0, false);
		valve_8_r1.setTextureOffset(32, 116).addCuboid(-10.775F, -0.7F, 39.6F, 2, 3, 1, 0, false);
		valve_8_r1.setTextureOffset(32, 120).addCuboid(8.675F, -0.7F, 39.6F, 2, 3, 1, 0, false);
		valve_8_r1.setTextureOffset(93, 120).addCuboid(15.325F, -0.7F, 39.6F, 2, 3, 1, 0, false);

		valve_7_r1 = new ModelPart(this);
		valve_7_r1.setPivot(0, 0, 0);
		pipe.addChild(valve_7_r1);
		setRotationAngle(valve_7_r1, 0, 0, -0.1745F);
		valve_7_r1.setTextureOffset(0, 39).addCuboid(8.65F, 2.375F, 40.35F, 1, 5, 1, 0, false);

		valve_6_r1 = new ModelPart(this);
		valve_6_r1.setPivot(0, 0, 0);
		pipe.addChild(valve_6_r1);
		setRotationAngle(valve_6_r1, 0, 0, -1.309F);
		valve_6_r1.setTextureOffset(38, 38).addCuboid(-3, 10.975F, 40.35F, 1, 3, 1, 0, false);

		valve_5_r1 = new ModelPart(this);
		valve_5_r1.setPivot(0, 0, 0);
		pipe.addChild(valve_5_r1);
		setRotationAngle(valve_5_r1, 0, 0, 1.5708F);
		valve_5_r1.setTextureOffset(16, 39).addCuboid(5.375F, -13.325F, 40.3F, 1, 1, 1, 0, false);

		valve_4_r1 = new ModelPart(this);
		valve_4_r1.setPivot(0, 0, 0);
		pipe.addChild(valve_4_r1);
		setRotationAngle(valve_4_r1, 0, 0, 1.309F);
		valve_4_r1.setTextureOffset(37, 84).addCuboid(8.55F, -13.85F, 40.3F, 1, 3, 1, 0, false);

		valve_3_r1 = new ModelPart(this);
		valve_3_r1.setPivot(0, 0, 0);
		pipe.addChild(valve_3_r1);
		setRotationAngle(valve_3_r1, 0, 0, 0.2618F);
		valve_3_r1.setTextureOffset(34, 69).addCuboid(15.8F, -3.625F, 40.3F, 1, 5, 1, 0, false);

		valve_2_r1 = new ModelPart(this);
		valve_2_r1.setPivot(0, 0, 0);
		pipe.addChild(valve_2_r1);
		setRotationAngle(valve_2_r1, 0.1745F, 0, 0.0436F);
		valve_2_r1.setTextureOffset(34, 55).addCuboid(8.95F, 1.775F, 39.65F, 1, 6, 1, 0, false);

		valve_1_r1 = new ModelPart(this);
		valve_1_r1.setPivot(0, 0, 0);
		pipe.addChild(valve_1_r1);
		setRotationAngle(valve_1_r1, 0.1745F, 0, -0.0436F);
		valve_1_r1.setTextureOffset(34, 62).addCuboid(16.1F, 2.8F, 39.4F, 1, 6, 1, 0, false);

		headlights = new ModelPart(this);
		headlights.setPivot(0, 24, 0);


		outer_head_5_r3 = new ModelPart(this);
		outer_head_5_r3.setPivot(-13, -22.275F, 36.4511F);
		headlights.addChild(outer_head_5_r3);
		setRotationAngle(outer_head_5_r3, 0.1745F, 0, 0);
		outer_head_5_r3.setTextureOffset(18, 46).addCuboid(-3, 15.5F, 0, 6, 5, 0, 0, true);
		outer_head_5_r3.setTextureOffset(18, 46).addCuboid(23, 15.5F, 0, 6, 5, 0, 0, false);

		tail_lights = new ModelPart(this);
		tail_lights.setPivot(0, 24, 0);


		tail_light_r1 = new ModelPart(this);
		tail_light_r1.setPivot(4, -40.0015F, 33.2254F);
		tail_lights.addChild(tail_light_r1);
		setRotationAngle(tail_light_r1, 0.1745F, 0, 0);
		tail_light_r1.setTextureOffset(18, 51).addCuboid(-4, -2, 0.1F, 6, 5, 0, 0, false);

		door_light = new ModelPart(this);
		door_light.setPivot(0, 24, 0);


		outer_roof_1_r5 = new ModelPart(this);
		outer_roof_1_r5.setPivot(-20, -13, 0);
		door_light.addChild(outer_roof_1_r5);
		setRotationAngle(outer_roof_1_r5, 0, 0, 0.1107F);
		outer_roof_1_r5.setTextureOffset(18, 52).addCuboid(-1.1F, -23, -2, 0, 4, 4, 0, false);

		door_light_off = new ModelPart(this);
		door_light_off.setPivot(0, 24, 0);


		light_r1 = new ModelPart(this);
		light_r1.setPivot(-20, -13, 0);
		door_light_off.addChild(light_r1);
		setRotationAngle(light_r1, 0, 0, 0.1107F);
		light_r1.setTextureOffset(19, 62).addCuboid(-1, -21, 0, 0, 0, 0, 0.4F, false);

		door_light_on = new ModelPart(this);
		door_light_on.setPivot(0, 24, 0);


		light_r2 = new ModelPart(this);
		light_r2.setPivot(-20, -13, 0);
		door_light_on.addChild(light_r2);
		setRotationAngle(light_r2, 0, 0, 0.1107F);
		light_r2.setTextureOffset(21, 62).addCuboid(-1, -21, 0, 0, 0, 0, 0.4F, false);
	}

	private static final int DOOR_MAX = 14;

	@Override
	protected void renderWindowPositions(MatrixStack matrices, VertexConsumer vertices, ModelTrainBase.RenderStage renderStage, int light, int position, boolean renderDetails, boolean isEnd1Head, boolean isEnd2Head) {
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
	protected void renderDoorPositions(MatrixStack matrices, VertexConsumer vertices, ModelTrainBase.RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
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
				door_left.setPivot(0, 0, doorRightZ);
				door_right.setPivot(0, 0, -doorRightZ);
				renderOnce(door, matrices, vertices, light, position);
				door_left.setPivot(0, 0, doorLeftZ);
				door_right.setPivot(0, 0, -doorLeftZ);
				renderOnceFlipped(door, matrices, vertices, light, position);

				if (renderDetails) {
					renderMirror(roof_door, matrices, vertices, light, position);
					renderMirror(door_handrails, matrices, vertices, light, position);
				}
				break;
			case EXTERIOR:
				door_left_exterior.setPivot(0, 0, doorRightZ);
				door_right_exterior.setPivot(0, 0, -doorRightZ);
				renderOnce(door_exterior, matrices, vertices, light, position);
				door_left_exterior.setPivot(0, 0, doorLeftZ);
				door_right_exterior.setPivot(0, 0, -doorLeftZ);
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
				renderMirror(side_panel, matrices, vertices, light, position - 19);
				renderMirror(side_panel, matrices, vertices, light, position + 19);
				break;
		}
	}

	@Override
	protected void renderHeadPosition1(MatrixStack matrices, VertexConsumer vertices, ModelTrainBase.RenderStage renderStage, int light, int position, boolean renderDetails, boolean useHeadlights) {
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
	protected void renderHeadPosition2(MatrixStack matrices, VertexConsumer vertices, ModelTrainBase.RenderStage renderStage, int light, int position, boolean renderDetails, boolean useHeadlights) {
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
	protected void renderEndPosition1(MatrixStack matrices, VertexConsumer vertices, ModelTrainBase.RenderStage renderStage, int light, int position, boolean renderDetails) {
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
	protected void renderEndPosition2(MatrixStack matrices, VertexConsumer vertices, ModelTrainBase.RenderStage renderStage, int light, int position, boolean renderDetails) {
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
		return null;
	}

	@Override
	protected ModelDoorOverlayTopBase getModelDoorOverlayTop() {
		return null;
	}

	@Override
	protected int[] getWindowPositions() {
		return new int[]{-94, -64, -34, 34, 64, 94};
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
