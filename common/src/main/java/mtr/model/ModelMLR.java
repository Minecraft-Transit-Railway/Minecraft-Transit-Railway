package mtr.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mtr.mappings.ModelDataWrapper;
import mtr.mappings.ModelMapper;

public class ModelMLR extends ModelSimpleTrainBase {

	private final ModelMapper window_1;
	private final ModelMapper upper_wall_r1;
	private final ModelMapper window_2;
	private final ModelMapper upper_wall_r2;
	private final ModelMapper window_handrails;
	private final ModelMapper handrail_8_r1;
	private final ModelMapper seats;
	private final ModelMapper seat_back_r1;
	private final ModelMapper handrail_3_r1;
	private final ModelMapper handrail_2_r1;
	private final ModelMapper window_1_tv;
	private final ModelMapper tv_r1;
	private final ModelMapper window_exterior_1;
	private final ModelMapper upper_wall_r3;
	private final ModelMapper window_exterior_2;
	private final ModelMapper upper_wall_r4;
	private final ModelMapper side_panel;
	private final ModelMapper side_panel_translucent;
	private final ModelMapper roof_window;
	private final ModelMapper inner_roof_5_r1;
	private final ModelMapper inner_roof_3_r1;
	private final ModelMapper inner_roof_2_r1;
	private final ModelMapper roof_door;
	private final ModelMapper inner_roof_6_r1;
	private final ModelMapper inner_roof_4_r1;
	private final ModelMapper inner_roof_3_r2;
	private final ModelMapper roof_light;
	private final ModelMapper light_r1;
	private final ModelMapper roof_exterior;
	private final ModelMapper outer_roof_5_r1;
	private final ModelMapper outer_roof_4_r1;
	private final ModelMapper outer_roof_3_r1;
	private final ModelMapper outer_roof_2_r1;
	private final ModelMapper outer_roof_1_r1;
	private final ModelMapper door;
	private final ModelMapper door_left;
	private final ModelMapper door_left_top_r1;
	private final ModelMapper door_right;
	private final ModelMapper door_right_top_r1;
	private final ModelMapper door_handrails;
	private final ModelMapper handrail_8_r2;
	private final ModelMapper door_exterior;
	private final ModelMapper door_left_exterior;
	private final ModelMapper door_left_top_r2;
	private final ModelMapper door_right_exterior;
	private final ModelMapper door_right_top_r2;
	private final ModelMapper end;
	private final ModelMapper upper_wall_2_r1;
	private final ModelMapper upper_wall_1_r1;
	private final ModelMapper seat_1;
	private final ModelMapper seat_back_r2;
	private final ModelMapper seat_2;
	private final ModelMapper seat_back_r3;
	private final ModelMapper seat_3;
	private final ModelMapper seat_back_r4;
	private final ModelMapper seat_4;
	private final ModelMapper seat_back_r5;
	private final ModelMapper seat_5;
	private final ModelMapper seat_back_r6;
	private final ModelMapper seat_6;
	private final ModelMapper seat_back_r7;
	private final ModelMapper end_exterior;
	private final ModelMapper upper_wall_2_r2;
	private final ModelMapper upper_wall_1_r2;
	private final ModelMapper roof_end;
	private final ModelMapper handrail_4_r1;
	private final ModelMapper handrail_3_r2;
	private final ModelMapper handrail_2_r2;
	private final ModelMapper handrail_1_r1;
	private final ModelMapper handrail_9_r1;
	private final ModelMapper handrail_8_r3;
	private final ModelMapper inner_roof_right_4_r1;
	private final ModelMapper inner_roof_right_5_r1;
	private final ModelMapper inner_roof_right_7_r1;
	private final ModelMapper inner_roof_left_7_r1;
	private final ModelMapper inner_roof_left_5_r1;
	private final ModelMapper inner_roof_left_4_r1;
	private final ModelMapper roof_end_light;
	private final ModelMapper light_2_r1;
	private final ModelMapper light_1_r1;
	private final ModelMapper roof_end_exterior;
	private final ModelMapper outer_roof_1;
	private final ModelMapper upper_wall_1_r3;
	private final ModelMapper outer_roof_5_r2;
	private final ModelMapper outer_roof_4_r2;
	private final ModelMapper outer_roof_3_r2;
	private final ModelMapper outer_roof_2_r2;
	private final ModelMapper outer_roof_2;
	private final ModelMapper upper_wall_1_r4;
	private final ModelMapper outer_roof_5_r3;
	private final ModelMapper outer_roof_4_r3;
	private final ModelMapper outer_roof_3_r3;
	private final ModelMapper outer_roof_2_r3;
	private final ModelMapper roof_end_vents;
	private final ModelMapper vent_3_r1;
	private final ModelMapper vent_2_r1;
	private final ModelMapper head;
	private final ModelMapper upper_wall_2_r3;
	private final ModelMapper upper_wall_1_r5;
	private final ModelMapper head_exterior;
	private final ModelMapper driver_door_upper_2_r1;
	private final ModelMapper upper_wall_2_r4;
	private final ModelMapper driver_door_upper_1_r1;
	private final ModelMapper upper_wall_1_r6;
	private final ModelMapper front;
	private final ModelMapper front_middle_top_2_r1;
	private final ModelMapper front_middle_top_1_r1;
	private final ModelMapper front_3_r1;
	private final ModelMapper front_1_r1;
	private final ModelMapper side_1;
	private final ModelMapper outer_roof_11_r1;
	private final ModelMapper outer_roof_10_r1;
	private final ModelMapper outer_roof_9_r1;
	private final ModelMapper outer_roof_8_r1;
	private final ModelMapper outer_roof_7_r1;
	private final ModelMapper outer_roof_5_r4;
	private final ModelMapper outer_roof_4_r4;
	private final ModelMapper outer_roof_3_r4;
	private final ModelMapper outer_roof_2_r4;
	private final ModelMapper outer_roof_1_r2;
	private final ModelMapper front_side_lower_5_r1;
	private final ModelMapper front_side_lower_4_r1;
	private final ModelMapper front_side_lower_3_r1;
	private final ModelMapper front_side_lower_2_r1;
	private final ModelMapper front_side_lower_1_r1;
	private final ModelMapper front_side_upper_7_r1;
	private final ModelMapper front_side_upper_6_r1;
	private final ModelMapper front_side_upper_5_r1;
	private final ModelMapper front_side_upper_4_r1;
	private final ModelMapper front_side_upper_3_r1;
	private final ModelMapper front_side_upper_2_r1;
	private final ModelMapper front_side_upper_1_r1;
	private final ModelMapper side_2;
	private final ModelMapper outer_roof_11_r2;
	private final ModelMapper outer_roof_10_r2;
	private final ModelMapper outer_roof_9_r2;
	private final ModelMapper outer_roof_8_r2;
	private final ModelMapper outer_roof_7_r2;
	private final ModelMapper outer_roof_5_r5;
	private final ModelMapper outer_roof_4_r5;
	private final ModelMapper outer_roof_3_r5;
	private final ModelMapper outer_roof_2_r5;
	private final ModelMapper outer_roof_1_r3;
	private final ModelMapper front_side_lower_6_r1;
	private final ModelMapper front_side_lower_5_r2;
	private final ModelMapper front_side_lower_4_r2;
	private final ModelMapper front_side_lower_3_r2;
	private final ModelMapper front_side_lower_2_r2;
	private final ModelMapper front_side_upper_8_r1;
	private final ModelMapper front_side_upper_7_r2;
	private final ModelMapper front_side_upper_6_r2;
	private final ModelMapper front_side_upper_5_r2;
	private final ModelMapper front_side_upper_4_r2;
	private final ModelMapper front_side_upper_3_r2;
	private final ModelMapper front_side_upper_2_r2;
	private final ModelMapper headlights;
	private final ModelMapper headlight_4_r1;
	private final ModelMapper headlight_3_r1;
	private final ModelMapper headlight_2_r1;
	private final ModelMapper headlight_1_r1;
	private final ModelMapper tail_lights;
	private final ModelMapper tail_light_2_r1;
	private final ModelMapper tail_light_1_r1;
	private final ModelMapper door_light;
	private final ModelMapper outer_roof_1_r4;
	private final ModelMapper door_light_on;
	private final ModelMapper light_r2;
	private final ModelMapper door_light_off;
	private final ModelMapper light_r3;

	public ModelMLR() {
		final int textureWidth = 400;
		final int textureHeight = 400;

		final ModelDataWrapper modelDataWrapper = new ModelDataWrapper(this, textureWidth, textureHeight);

		window_1 = new ModelMapper(modelDataWrapper);
		window_1.setPos(0, 24, 0);
		window_1.texOffs(232, 102).addBox(-20, 0, -16, 20, 1, 32, 0, false);
		window_1.texOffs(300, 99).addBox(-20, -13, -18, 2, 13, 36, 0, false);

		upper_wall_r1 = new ModelMapper(modelDataWrapper);
		upper_wall_r1.setPos(-20, -13, 0);
		window_1.addChild(upper_wall_r1);
		setRotationAngle(upper_wall_r1, 0, 0, 0.1107F);
		upper_wall_r1.texOffs(76, 267).addBox(0, -20, -18, 2, 20, 36, 0, false);

		window_2 = new ModelMapper(modelDataWrapper);
		window_2.setPos(0, 24, 0);
		window_2.texOffs(184, 219).addBox(-20, 0, -16, 20, 1, 32, 0, false);
		window_2.texOffs(292, 191).addBox(-20, -13, -18, 2, 13, 36, 0, false);

		upper_wall_r2 = new ModelMapper(modelDataWrapper);
		upper_wall_r2.setPos(-20, -13, 0);
		window_2.addChild(upper_wall_r2);
		setRotationAngle(upper_wall_r2, 0, 0, 0.1107F);
		upper_wall_r2.texOffs(201, 252).addBox(0, -20, -18, 2, 20, 36, 0, false);

		window_handrails = new ModelMapper(modelDataWrapper);
		window_handrails.setPos(0, 24, 0);
		window_handrails.texOffs(8, 1).addBox(0, -33, 0, 0, 33, 0, 0.2F, false);
		window_handrails.texOffs(0, 69).addBox(-1, -32, 15, 2, 4, 0, 0, false);
		window_handrails.texOffs(0, 69).addBox(-1, -32, 9, 2, 4, 0, 0, false);
		window_handrails.texOffs(0, 69).addBox(-1, -32, 3, 2, 4, 0, 0, false);
		window_handrails.texOffs(0, 69).addBox(-1, -32, -3, 2, 4, 0, 0, false);
		window_handrails.texOffs(0, 69).addBox(-1, -32, -9, 2, 4, 0, 0, false);
		window_handrails.texOffs(0, 69).addBox(-1, -32, -15, 2, 4, 0, 0, false);

		handrail_8_r1 = new ModelMapper(modelDataWrapper);
		handrail_8_r1.setPos(0, 0, 0);
		window_handrails.addChild(handrail_8_r1);
		setRotationAngle(handrail_8_r1, -1.5708F, 0, 0);
		handrail_8_r1.texOffs(0, 0).addBox(0, -16, -31.5F, 0, 32, 0, 0.2F, false);

		seats = new ModelMapper(modelDataWrapper);
		seats.setPos(0, 24, 0);
		seats.texOffs(156, 298).addBox(-18, -6, -15.5F, 7, 1, 31, 0, false);

		seat_back_r1 = new ModelMapper(modelDataWrapper);
		seat_back_r1.setPos(-17, -6, 0);
		seats.addChild(seat_back_r1);
		setRotationAngle(seat_back_r1, 0, 0, -0.0524F);
		seat_back_r1.texOffs(328, 325).addBox(-1, -8, -15.5F, 1, 8, 31, 0, false);

		handrail_3_r1 = new ModelMapper(modelDataWrapper);
		handrail_3_r1.setPos(-11, -5, 0);
		seats.addChild(handrail_3_r1);
		setRotationAngle(handrail_3_r1, 0, 0, -0.0436F);
		handrail_3_r1.texOffs(4, 16).addBox(0, -13.2F, 15, 0, 13, 0, 0.2F, false);
		handrail_3_r1.texOffs(4, 16).addBox(0, -13.2F, -15, 0, 13, 0, 0.2F, false);

		handrail_2_r1 = new ModelMapper(modelDataWrapper);
		handrail_2_r1.setPos(-12.2986F, -26.5473F, -14);
		seats.addChild(handrail_2_r1);
		setRotationAngle(handrail_2_r1, 0, 0, -0.0873F);
		handrail_2_r1.texOffs(4, 0).addBox(0, -8, -1, 0, 16, 0, 0.2F, false);
		handrail_2_r1.texOffs(4, 0).addBox(0, -8, 29, 0, 16, 0, 0.2F, false);

		window_1_tv = new ModelMapper(modelDataWrapper);
		window_1_tv.setPos(0, 24, 0);
		window_1_tv.texOffs(232, 135).addBox(-17, -32.5F, -5.5F, 2, 4, 12, 0, false);

		tv_r1 = new ModelMapper(modelDataWrapper);
		tv_r1.setPos(-15, -28.5F, 0);
		window_1_tv.addChild(tv_r1);
		setRotationAngle(tv_r1, 0, 0, 0.5672F);
		tv_r1.texOffs(20, 155).addBox(-2, -8, -5.5F, 2, 8, 12, 0, false);

		window_exterior_1 = new ModelMapper(modelDataWrapper);
		window_exterior_1.setPos(0, 24, 0);
		window_exterior_1.texOffs(234, 326).addBox(-21, 0, -16, 1, 7, 32, 0, false);
		window_exterior_1.texOffs(40, 244).addBox(-20, -13, -18, 0, 13, 36, 0, false);

		upper_wall_r3 = new ModelMapper(modelDataWrapper);
		upper_wall_r3.setPos(-20, -13, 0);
		window_exterior_1.addChild(upper_wall_r3);
		setRotationAngle(upper_wall_r3, 0, 0, 0.1107F);
		upper_wall_r3.texOffs(164, 216).addBox(0, -20, -18, 0, 20, 36, 0, false);

		window_exterior_2 = new ModelMapper(modelDataWrapper);
		window_exterior_2.setPos(0, 24, 0);
		window_exterior_2.texOffs(200, 308).addBox(-21, 0, -16, 1, 7, 32, 0, false);
		window_exterior_2.texOffs(40, 231).addBox(-20, -13, -18, 0, 13, 36, 0, false);

		upper_wall_r4 = new ModelMapper(modelDataWrapper);
		upper_wall_r4.setPos(-20, -13, 0);
		window_exterior_2.addChild(upper_wall_r4);
		setRotationAngle(upper_wall_r4, 0, 0, 0.1107F);
		upper_wall_r4.texOffs(68, 13).addBox(0, -20, -18, 0, 20, 36, 0, false);

		side_panel = new ModelMapper(modelDataWrapper);
		side_panel.setPos(0, 24, 0);
		side_panel.texOffs(206, 123).addBox(-18, -34, 0, 7, 31, 0, 0, false);

		side_panel_translucent = new ModelMapper(modelDataWrapper);
		side_panel_translucent.setPos(0, 24, 0);
		side_panel_translucent.texOffs(304, 103).addBox(-18, -34, 0, 7, 31, 0, 0, false);

		roof_window = new ModelMapper(modelDataWrapper);
		roof_window.setPos(0, 24, 0);
		roof_window.texOffs(4, 123).addBox(-16, -32, -16, 2, 0, 32, 0, false);
		roof_window.texOffs(84, 69).addBox(-10.2292F, -34.8796F, -16, 5, 0, 32, 0, false);
		roof_window.texOffs(122, 0).addBox(-2, -33, -16, 2, 0, 32, 0, false);

		inner_roof_5_r1 = new ModelMapper(modelDataWrapper);
		inner_roof_5_r1.setPos(-2, -33, 0);
		roof_window.addChild(inner_roof_5_r1);
		setRotationAngle(inner_roof_5_r1, 0, 0, 0.5236F);
		inner_roof_5_r1.texOffs(96, 0).addBox(-4, 0, -16, 4, 0, 32, 0, false);

		inner_roof_3_r1 = new ModelMapper(modelDataWrapper);
		inner_roof_3_r1.setPos(-11.6147F, -34.3057F, 0);
		roof_window.addChild(inner_roof_3_r1);
		setRotationAngle(inner_roof_3_r1, 0, 0, -0.3927F);
		inner_roof_3_r1.texOffs(104, 0).addBox(-1.5F, 0, -16, 3, 0, 32, 0, false);

		inner_roof_2_r1 = new ModelMapper(modelDataWrapper);
		inner_roof_2_r1.setPos(-14, -32, 0);
		roof_window.addChild(inner_roof_2_r1);
		setRotationAngle(inner_roof_2_r1, 0, 0, -1.0472F);
		inner_roof_2_r1.texOffs(0, 123).addBox(0, 0, -16, 2, 0, 32, 0, false);

		roof_door = new ModelMapper(modelDataWrapper);
		roof_door.setPos(0, 24, 0);
		roof_door.texOffs(94, 69).addBox(-18, -32, -16, 4, 0, 32, 0, false);
		roof_door.texOffs(64, 69).addBox(-10.2292F, -34.8796F, -16, 5, 0, 32, 0, false);
		roof_door.texOffs(114, 0).addBox(-2, -33, -16, 2, 0, 32, 0, false);

		inner_roof_6_r1 = new ModelMapper(modelDataWrapper);
		inner_roof_6_r1.setPos(-2, -33, 0);
		roof_door.addChild(inner_roof_6_r1);
		setRotationAngle(inner_roof_6_r1, 0, 0, 0.5236F);
		inner_roof_6_r1.texOffs(0, 49).addBox(-4, 0, -16, 4, 0, 32, 0, false);

		inner_roof_4_r1 = new ModelMapper(modelDataWrapper);
		inner_roof_4_r1.setPos(-11.6147F, -34.3057F, 0);
		roof_door.addChild(inner_roof_4_r1);
		setRotationAngle(inner_roof_4_r1, 0, 0, -0.3927F);
		inner_roof_4_r1.texOffs(102, 69).addBox(-1.5F, 0, -16, 3, 0, 32, 0, false);

		inner_roof_3_r2 = new ModelMapper(modelDataWrapper);
		inner_roof_3_r2.setPos(-14, -32, 0);
		roof_door.addChild(inner_roof_3_r2);
		setRotationAngle(inner_roof_3_r2, 0, 0, -1.0472F);
		inner_roof_3_r2.texOffs(118, 0).addBox(0, 0, -16, 2, 0, 32, 0, false);

		roof_light = new ModelMapper(modelDataWrapper);
		roof_light.setPos(0, 24, 0);


		light_r1 = new ModelMapper(modelDataWrapper);
		light_r1.setPos(-2, -33, 0);
		roof_light.addChild(light_r1);
		setRotationAngle(light_r1, 0, 0, 0.5236F);
		light_r1.texOffs(108, 49).addBox(-4, -0.1F, -16, 4, 0, 32, 0, false);

		roof_exterior = new ModelMapper(modelDataWrapper);
		roof_exterior.setPos(0, 24, 0);
		roof_exterior.texOffs(52, 69).addBox(-6, -42, -16, 6, 0, 32, 0, false);

		outer_roof_5_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_5_r1.setPos(-9.9394F, -41.3064F, 0);
		roof_exterior.addChild(outer_roof_5_r1);
		setRotationAngle(outer_roof_5_r1, 0, 0, -0.1745F);
		outer_roof_5_r1.texOffs(36, 69).addBox(-4, 0, -16, 8, 0, 32, 0, false);

		outer_roof_4_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_4_r1.setPos(-15.1778F, -39.8628F, 0);
		roof_exterior.addChild(outer_roof_4_r1);
		setRotationAngle(outer_roof_4_r1, 0, 0, -0.5236F);
		outer_roof_4_r1.texOffs(74, 69).addBox(-1.5F, 0, -16, 3, 0, 32, 0, false);

		outer_roof_3_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_3_r1.setPos(-16.9769F, -38.2468F, 0);
		roof_exterior.addChild(outer_roof_3_r1);
		setRotationAngle(outer_roof_3_r1, 0, 0, -1.0472F);
		outer_roof_3_r1.texOffs(110, 0).addBox(-1, 0, -16, 2, 0, 32, 0, false);

		outer_roof_2_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_2_r1.setPos(-17.5872F, -36.3872F, 0);
		roof_exterior.addChild(outer_roof_2_r1);
		setRotationAngle(outer_roof_2_r1, 0, 0, 0.1107F);
		outer_roof_2_r1.texOffs(66, 178).addBox(0, -1, -16, 0, 2, 32, 0, false);

		outer_roof_1_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_1_r1.setPos(-20, -13, 0);
		roof_exterior.addChild(outer_roof_1_r1);
		setRotationAngle(outer_roof_1_r1, 0, 0, 0.1107F);
		outer_roof_1_r1.texOffs(160, 330).addBox(-1, -23, -16, 1, 4, 32, 0, false);

		door = new ModelMapper(modelDataWrapper);
		door.setPos(0, 24, 0);
		door.texOffs(232, 35).addBox(-20, 0, -16, 20, 1, 32, 0, false);

		door_left = new ModelMapper(modelDataWrapper);
		door_left.setPos(0, 0, 0);
		door.addChild(door_left);
		door_left.texOffs(232, 35).addBox(-20.8F, -13, 0, 1, 13, 15, 0, false);

		door_left_top_r1 = new ModelMapper(modelDataWrapper);
		door_left_top_r1.setPos(-20.8F, -13, 0);
		door_left.addChild(door_left_top_r1);
		setRotationAngle(door_left_top_r1, 0, 0, 0.1107F);
		door_left_top_r1.texOffs(194, 35).addBox(0, -20, 0, 1, 20, 15, 0, false);

		door_right = new ModelMapper(modelDataWrapper);
		door_right.setPos(0, 0, 0);
		door.addChild(door_right);
		door_right.texOffs(174, 123).addBox(-20.8F, -13, -15, 1, 13, 15, 0, false);

		door_right_top_r1 = new ModelMapper(modelDataWrapper);
		door_right_top_r1.setPos(-20.8F, -13, 0);
		door_right.addChild(door_right_top_r1);
		setRotationAngle(door_right_top_r1, 0, 0, 0.1107F);
		door_right_top_r1.texOffs(0, 123).addBox(0, -20, -15, 1, 20, 15, 0, false);

		door_handrails = new ModelMapper(modelDataWrapper);
		door_handrails.setPos(0, 24, 0);
		door_handrails.texOffs(8, 0).addBox(0, -33, 0, 0, 33, 0, 0.2F, false);

		handrail_8_r2 = new ModelMapper(modelDataWrapper);
		handrail_8_r2.setPos(0, 0, 0);
		door_handrails.addChild(handrail_8_r2);
		setRotationAngle(handrail_8_r2, -1.5708F, 0, 0);
		handrail_8_r2.texOffs(0, 0).addBox(0, -16, -31.5F, 0, 32, 0, 0.2F, false);

		door_exterior = new ModelMapper(modelDataWrapper);
		door_exterior.setPos(0, 24, 0);
		door_exterior.texOffs(72, 323).addBox(-21, 0, -16, 1, 7, 32, 0, false);

		door_left_exterior = new ModelMapper(modelDataWrapper);
		door_left_exterior.setPos(0, 0, 0);
		door_exterior.addChild(door_left_exterior);
		door_left_exterior.texOffs(128, 20).addBox(-20.8F, -13, 0, 0, 13, 15, 0, false);

		door_left_top_r2 = new ModelMapper(modelDataWrapper);
		door_left_top_r2.setPos(-20.8F, -13, 0);
		door_left_exterior.addChild(door_left_top_r2);
		setRotationAngle(door_left_top_r2, 0, 0, 0.1107F);
		door_left_top_r2.texOffs(82, 128).addBox(0, -20, 0, 0, 20, 15, 0, false);

		door_right_exterior = new ModelMapper(modelDataWrapper);
		door_right_exterior.setPos(0, 0, 0);
		door_exterior.addChild(door_right_exterior);
		door_right_exterior.texOffs(0, 75).addBox(-20.8F, -13, -15, 0, 13, 15, 0, false);

		door_right_top_r2 = new ModelMapper(modelDataWrapper);
		door_right_top_r2.setPos(-20.8F, -13, 0);
		door_right_exterior.addChild(door_right_top_r2);
		setRotationAngle(door_right_top_r2, 0, 0, 0.1107F);
		door_right_top_r2.texOffs(0, 34).addBox(0, -20, -15, 0, 20, 15, 0, false);

		end = new ModelMapper(modelDataWrapper);
		end.setPos(0, 24, 0);
		end.texOffs(0, 0).addBox(-20, 0, -32, 40, 1, 48, 0, false);
		end.texOffs(174, 102).addBox(18, -13, -36, 2, 13, 54, 0, true);
		end.texOffs(174, 35).addBox(-20, -13, -36, 2, 13, 54, 0, false);
		end.texOffs(0, 197).addBox(6, -32, -36, 12, 32, 12, 0, false);
		end.texOffs(66, 212).addBox(-18, -32, -36, 12, 32, 12, 0, false);
		end.texOffs(242, 14).addBox(-18, -35, -36, 36, 3, 12, 0, false);

		upper_wall_2_r1 = new ModelMapper(modelDataWrapper);
		upper_wall_2_r1.setPos(-20, -13, 0);
		end.addChild(upper_wall_2_r1);
		setRotationAngle(upper_wall_2_r1, 0, 0, 0.1107F);
		upper_wall_2_r1.texOffs(116, 49).addBox(0, -20, -36, 2, 20, 54, 0, false);

		upper_wall_1_r1 = new ModelMapper(modelDataWrapper);
		upper_wall_1_r1.setPos(20, -13, 0);
		end.addChild(upper_wall_1_r1);
		setRotationAngle(upper_wall_1_r1, 0, 0, -0.1107F);
		upper_wall_1_r1.texOffs(0, 123).addBox(-2, -20, -36, 2, 20, 54, 0, true);

		seat_1 = new ModelMapper(modelDataWrapper);
		seat_1.setPos(0, 0, 0);
		end.addChild(seat_1);
		seat_1.texOffs(116, 165).addBox(6, -6, -23, 12, 1, 7, 0, false);

		seat_back_r2 = new ModelMapper(modelDataWrapper);
		seat_back_r2.setPos(0, -6, -22);
		seat_1.addChild(seat_back_r2);
		setRotationAngle(seat_back_r2, 0.0524F, 0, 0);
		seat_back_r2.texOffs(0, 241).addBox(6, -8, -1, 12, 8, 1, 0, false);

		seat_2 = new ModelMapper(modelDataWrapper);
		seat_2.setPos(0, 0, 0);
		end.addChild(seat_2);
		seat_2.texOffs(58, 164).addBox(6, -6, -11, 12, 1, 7, 0, false);

		seat_back_r3 = new ModelMapper(modelDataWrapper);
		seat_back_r3.setPos(0, -6, -10);
		seat_2.addChild(seat_back_r3);
		setRotationAngle(seat_back_r3, 0.0524F, 0, 0);
		seat_back_r3.texOffs(172, 219).addBox(6, -8, -1, 12, 8, 1, 0, false);

		seat_3 = new ModelMapper(modelDataWrapper);
		seat_3.setPos(0, 0, 0);
		end.addChild(seat_3);
		seat_3.texOffs(174, 169).addBox(11, -6, 6, 7, 1, 7, 0, false);

		seat_back_r4 = new ModelMapper(modelDataWrapper);
		seat_back_r4.setPos(0, -6, 12);
		seat_3.addChild(seat_back_r4);
		setRotationAngle(seat_back_r4, -0.0524F, 0, 0);
		seat_back_r4.texOffs(0, 80).addBox(11, -8, 0, 7, 8, 1, 0, false);

		seat_4 = new ModelMapper(modelDataWrapper);
		seat_4.setPos(0, 0, 0);
		end.addChild(seat_4);
		seat_4.texOffs(116, 165).addBox(-18, -6, -23, 12, 1, 7, 0, true);

		seat_back_r5 = new ModelMapper(modelDataWrapper);
		seat_back_r5.setPos(0, -6, -22);
		seat_4.addChild(seat_back_r5);
		setRotationAngle(seat_back_r5, 0.0524F, 0, 0);
		seat_back_r5.texOffs(0, 241).addBox(-18, -8, -1, 12, 8, 1, 0, true);

		seat_5 = new ModelMapper(modelDataWrapper);
		seat_5.setPos(0, 0, 0);
		end.addChild(seat_5);
		seat_5.texOffs(58, 164).addBox(-18, -6, -11, 12, 1, 7, 0, true);

		seat_back_r6 = new ModelMapper(modelDataWrapper);
		seat_back_r6.setPos(0, -6, -10);
		seat_5.addChild(seat_back_r6);
		setRotationAngle(seat_back_r6, 0.0524F, 0, 0);
		seat_back_r6.texOffs(172, 219).addBox(-18, -8, -1, 12, 8, 1, 0, true);

		seat_6 = new ModelMapper(modelDataWrapper);
		seat_6.setPos(0, 0, 0);
		end.addChild(seat_6);
		seat_6.texOffs(174, 169).addBox(-18, -6, 6, 7, 1, 7, 0, true);

		seat_back_r7 = new ModelMapper(modelDataWrapper);
		seat_back_r7.setPos(0, -6, 12);
		seat_6.addChild(seat_back_r7);
		setRotationAngle(seat_back_r7, -0.0524F, 0, 0);
		seat_back_r7.texOffs(0, 80).addBox(-18, -8, 0, 7, 8, 1, 0, true);

		end_exterior = new ModelMapper(modelDataWrapper);
		end_exterior.setPos(0, 24, 0);
		end_exterior.texOffs(70, 216).addBox(20, 0, -28, 1, 7, 44, 0, true);
		end_exterior.texOffs(126, 190).addBox(-21, 0, -28, 1, 7, 44, 0, false);
		end_exterior.texOffs(58, 143).addBox(18, -13, -36, 2, 13, 54, 0, true);
		end_exterior.texOffs(116, 123).addBox(-20, -13, -36, 2, 13, 54, 0, false);
		end_exterior.texOffs(0, 260).addBox(6, -33, -36, 12, 33, 0, 0, true);
		end_exterior.texOffs(0, 260).addBox(-18, -33, -36, 12, 33, 0, 0, false);
		end_exterior.texOffs(300, 148).addBox(-18, -41, -36, 36, 9, 0, 0, false);

		upper_wall_2_r2 = new ModelMapper(modelDataWrapper);
		upper_wall_2_r2.setPos(-20, -13, 0);
		end_exterior.addChild(upper_wall_2_r2);
		setRotationAngle(upper_wall_2_r2, 0, 0, 0.1107F);
		upper_wall_2_r2.texOffs(0, 49).addBox(0, -20, -36, 2, 20, 54, 0, false);

		upper_wall_1_r2 = new ModelMapper(modelDataWrapper);
		upper_wall_1_r2.setPos(20, -13, 0);
		end_exterior.addChild(upper_wall_1_r2);
		setRotationAngle(upper_wall_1_r2, 0, 0, -0.1107F);
		upper_wall_1_r2.texOffs(58, 69).addBox(-2, -20, -36, 2, 20, 54, 0, true);

		roof_end = new ModelMapper(modelDataWrapper);
		roof_end.setPos(0, 24, 0);
		roof_end.texOffs(134, 49).addBox(-18, -32, -24, 4, 0, 40, 0, false);
		roof_end.texOffs(18, 49).addBox(-10.2292F, -34.8796F, -24, 5, 0, 40, 0, false);
		roof_end.texOffs(146, 35).addBox(-2, -33, -24, 2, 0, 40, 0, false);
		roof_end.texOffs(10, 49).addBox(0, -33, -24, 2, 0, 40, 0, true);
		roof_end.texOffs(0, 49).addBox(5.2292F, -34.8796F, -24, 5, 0, 40, 0, true);
		roof_end.texOffs(116, 49).addBox(14, -32, -24, 4, 0, 40, 0, true);
		roof_end.texOffs(0, 0).addBox(0, -33.4899F, -16.9899F, 0, 1, 0, 0.2F, false);
		roof_end.texOffs(0, 69).addBox(-1, -32, 15, 2, 4, 0, 0, false);
		roof_end.texOffs(0, 69).addBox(-1, -32, 9, 2, 4, 0, 0, false);
		roof_end.texOffs(0, 69).addBox(-1, -32, 3, 2, 4, 0, 0, false);

		handrail_4_r1 = new ModelMapper(modelDataWrapper);
		handrail_4_r1.setPos(12.2986F, -26.5473F, 0);
		roof_end.addChild(handrail_4_r1);
		setRotationAngle(handrail_4_r1, 0, 0, 0.0873F);
		handrail_4_r1.texOffs(4, 0).addBox(0, -8, 14.25F, 0, 16, 0, 0.2F, true);

		handrail_3_r2 = new ModelMapper(modelDataWrapper);
		handrail_3_r2.setPos(11, -5, 0);
		roof_end.addChild(handrail_3_r2);
		setRotationAngle(handrail_3_r2, 0, 0, 0.0436F);
		handrail_3_r2.texOffs(4, 16).addBox(0, -13.2F, 14.25F, 0, 13, 0, 0.2F, true);

		handrail_2_r2 = new ModelMapper(modelDataWrapper);
		handrail_2_r2.setPos(-12.2986F, -26.5473F, 0);
		roof_end.addChild(handrail_2_r2);
		setRotationAngle(handrail_2_r2, 0, 0, -0.0873F);
		handrail_2_r2.texOffs(4, 0).addBox(0, -8, 14.25F, 0, 16, 0, 0.2F, false);

		handrail_1_r1 = new ModelMapper(modelDataWrapper);
		handrail_1_r1.setPos(-11, -5, 0);
		roof_end.addChild(handrail_1_r1);
		setRotationAngle(handrail_1_r1, 0, 0, -0.0436F);
		handrail_1_r1.texOffs(4, 16).addBox(0, -13.2F, 14.25F, 0, 13, 0, 0.2F, false);

		handrail_9_r1 = new ModelMapper(modelDataWrapper);
		handrail_9_r1.setPos(0, -31.3F, -16.2F);
		roof_end.addChild(handrail_9_r1);
		setRotationAngle(handrail_9_r1, 0.7854F, 0, 0);
		handrail_9_r1.texOffs(0, 0).addBox(0, -1.2F, 0.2F, 0, 1, 0, 0.2F, false);

		handrail_8_r3 = new ModelMapper(modelDataWrapper);
		handrail_8_r3.setPos(0, 0, 0);
		roof_end.addChild(handrail_8_r3);
		setRotationAngle(handrail_8_r3, -1.5708F, 0, 0);
		handrail_8_r3.texOffs(0, 0).addBox(0, -16, -31.5F, 0, 32, 0, 0.2F, false);

		inner_roof_right_4_r1 = new ModelMapper(modelDataWrapper);
		inner_roof_right_4_r1.setPos(14, -32, 0);
		roof_end.addChild(inner_roof_right_4_r1);
		setRotationAngle(inner_roof_right_4_r1, 0, 0, 1.0472F);
		inner_roof_right_4_r1.texOffs(142, 35).addBox(-2, 0, -24, 2, 0, 40, 0, true);

		inner_roof_right_5_r1 = new ModelMapper(modelDataWrapper);
		inner_roof_right_5_r1.setPos(11.6147F, -34.3057F, 0);
		roof_end.addChild(inner_roof_right_5_r1);
		setRotationAngle(inner_roof_right_5_r1, 0, 0, 0.3927F);
		inner_roof_right_5_r1.texOffs(8, 123).addBox(-1.5F, 0, -24, 3, 0, 40, 0, true);

		inner_roof_right_7_r1 = new ModelMapper(modelDataWrapper);
		inner_roof_right_7_r1.setPos(2, -33, 0);
		roof_end.addChild(inner_roof_right_7_r1);
		setRotationAngle(inner_roof_right_7_r1, 0, 0, -0.5236F);
		inner_roof_right_7_r1.texOffs(108, 49).addBox(0, 0, -24, 4, 0, 40, 0, true);

		inner_roof_left_7_r1 = new ModelMapper(modelDataWrapper);
		inner_roof_left_7_r1.setPos(-2, -33, 0);
		roof_end.addChild(inner_roof_left_7_r1);
		setRotationAngle(inner_roof_left_7_r1, 0, 0, 0.5236F);
		inner_roof_left_7_r1.texOffs(0, 123).addBox(-4, 0, -24, 4, 0, 40, 0, false);

		inner_roof_left_5_r1 = new ModelMapper(modelDataWrapper);
		inner_roof_left_5_r1.setPos(-11.6147F, -34.3057F, 0);
		roof_end.addChild(inner_roof_left_5_r1);
		setRotationAngle(inner_roof_left_5_r1, 0, 0, -0.3927F);
		inner_roof_left_5_r1.texOffs(124, 49).addBox(-1.5F, 0, -24, 3, 0, 40, 0, false);

		inner_roof_left_4_r1 = new ModelMapper(modelDataWrapper);
		inner_roof_left_4_r1.setPos(-14, -32, 0);
		roof_end.addChild(inner_roof_left_4_r1);
		setRotationAngle(inner_roof_left_4_r1, 0, 0, -1.0472F);
		inner_roof_left_4_r1.texOffs(150, 35).addBox(0, 0, -24, 2, 0, 40, 0, false);

		roof_end_light = new ModelMapper(modelDataWrapper);
		roof_end_light.setPos(0, 24, 0);


		light_2_r1 = new ModelMapper(modelDataWrapper);
		light_2_r1.setPos(2, -33, 0);
		roof_end_light.addChild(light_2_r1);
		setRotationAngle(light_2_r1, 0, 0, -0.5236F);
		light_2_r1.texOffs(100, 49).addBox(0, -0.1F, -24, 4, 0, 40, 0, false);

		light_1_r1 = new ModelMapper(modelDataWrapper);
		light_1_r1.setPos(-2, -33, 0);
		roof_end_light.addChild(light_1_r1);
		setRotationAngle(light_1_r1, 0, 0, 0.5236F);
		light_1_r1.texOffs(100, 49).addBox(-4, -0.1F, -24, 4, 0, 40, 0, false);

		roof_end_exterior = new ModelMapper(modelDataWrapper);
		roof_end_exterior.setPos(0, 24, 0);


		outer_roof_1 = new ModelMapper(modelDataWrapper);
		outer_roof_1.setPos(0, 0, 0);
		roof_end_exterior.addChild(outer_roof_1);
		outer_roof_1.texOffs(0, 69).addBox(-6, -42, -36, 6, 1, 20, 0, false);

		upper_wall_1_r3 = new ModelMapper(modelDataWrapper);
		upper_wall_1_r3.setPos(-20, -13, 0);
		outer_roof_1.addChild(upper_wall_1_r3);
		setRotationAngle(upper_wall_1_r3, 0, 0, 0.1107F);
		upper_wall_1_r3.texOffs(0, 69).addBox(0, -23, -36, 1, 4, 7, 0, false);
		upper_wall_1_r3.texOffs(182, 70).addBox(-1, -23, -29, 1, 4, 13, 0, false);

		outer_roof_5_r2 = new ModelMapper(modelDataWrapper);
		outer_roof_5_r2.setPos(-9.7656F, -40.3206F, 0);
		outer_roof_1.addChild(outer_roof_5_r2);
		setRotationAngle(outer_roof_5_r2, 0, 0, -0.1745F);
		outer_roof_5_r2.texOffs(172, 190).addBox(-4, -1, -36, 8, 1, 20, 0, false);

		outer_roof_4_r2 = new ModelMapper(modelDataWrapper);
		outer_roof_4_r2.setPos(-14.6775F, -38.9948F, 0);
		outer_roof_1.addChild(outer_roof_4_r2);
		setRotationAngle(outer_roof_4_r2, 0, 0, -0.5236F);
		outer_roof_4_r2.texOffs(0, 20).addBox(-1.5F, -1, -36, 3, 1, 20, 0, false);

		outer_roof_3_r2 = new ModelMapper(modelDataWrapper);
		outer_roof_3_r2.setPos(-16.1105F, -37.7448F, 0);
		outer_roof_1.addChild(outer_roof_3_r2);
		setRotationAngle(outer_roof_3_r2, 0, 0, -1.0472F);
		outer_roof_3_r2.texOffs(58, 143).addBox(-1, -1, -36, 2, 1, 20, 0, false);

		outer_roof_2_r2 = new ModelMapper(modelDataWrapper);
		outer_roof_2_r2.setPos(-17.587F, -36.3849F, 0);
		outer_roof_1.addChild(outer_roof_2_r2);
		setRotationAngle(outer_roof_2_r2, 0, 0, 0.1107F);
		outer_roof_2_r2.texOffs(116, 143).addBox(0, -1, -36, 1, 2, 20, 0, false);

		outer_roof_2 = new ModelMapper(modelDataWrapper);
		outer_roof_2.setPos(0, 0, 0);
		roof_end_exterior.addChild(outer_roof_2);
		outer_roof_2.texOffs(0, 69).addBox(0, -42, -36, 6, 1, 20, 0, true);

		upper_wall_1_r4 = new ModelMapper(modelDataWrapper);
		upper_wall_1_r4.setPos(20, -13, 0);
		outer_roof_2.addChild(upper_wall_1_r4);
		setRotationAngle(upper_wall_1_r4, 0, 0, -0.1107F);
		upper_wall_1_r4.texOffs(0, 69).addBox(-1, -23, -36, 1, 4, 7, 0, true);
		upper_wall_1_r4.texOffs(182, 70).addBox(0, -23, -29, 1, 4, 13, 0, true);

		outer_roof_5_r3 = new ModelMapper(modelDataWrapper);
		outer_roof_5_r3.setPos(9.7656F, -40.3206F, 0);
		outer_roof_2.addChild(outer_roof_5_r3);
		setRotationAngle(outer_roof_5_r3, 0, 0, 0.1745F);
		outer_roof_5_r3.texOffs(172, 190).addBox(-4, -1, -36, 8, 1, 20, 0, true);

		outer_roof_4_r3 = new ModelMapper(modelDataWrapper);
		outer_roof_4_r3.setPos(14.6775F, -38.9948F, 0);
		outer_roof_2.addChild(outer_roof_4_r3);
		setRotationAngle(outer_roof_4_r3, 0, 0, 0.5236F);
		outer_roof_4_r3.texOffs(0, 20).addBox(-1.5F, -1, -36, 3, 1, 20, 0, true);

		outer_roof_3_r3 = new ModelMapper(modelDataWrapper);
		outer_roof_3_r3.setPos(16.1105F, -37.7448F, 0);
		outer_roof_2.addChild(outer_roof_3_r3);
		setRotationAngle(outer_roof_3_r3, 0, 0, 1.0472F);
		outer_roof_3_r3.texOffs(58, 143).addBox(-1, -1, -36, 2, 1, 20, 0, true);

		outer_roof_2_r3 = new ModelMapper(modelDataWrapper);
		outer_roof_2_r3.setPos(17.587F, -36.3849F, 0);
		outer_roof_2.addChild(outer_roof_2_r3);
		setRotationAngle(outer_roof_2_r3, 0, 0, -0.1107F);
		outer_roof_2_r3.texOffs(116, 143).addBox(-1, -1, -36, 1, 2, 20, 0, true);

		roof_end_vents = new ModelMapper(modelDataWrapper);
		roof_end_vents.setPos(0, 24, 0);
		roof_end_vents.texOffs(180, 169).addBox(-8, -43, -21, 16, 2, 48, 0, false);

		vent_3_r1 = new ModelMapper(modelDataWrapper);
		vent_3_r1.setPos(-8, -43, 12);
		roof_end_vents.addChild(vent_3_r1);
		setRotationAngle(vent_3_r1, 0, 0, -0.3491F);
		vent_3_r1.texOffs(0, 210).addBox(-9, 0, -33, 9, 2, 48, 0, false);

		vent_2_r1 = new ModelMapper(modelDataWrapper);
		vent_2_r1.setPos(8, -43, 12);
		roof_end_vents.addChild(vent_2_r1);
		setRotationAngle(vent_2_r1, 0, 0, 0.3491F);
		vent_2_r1.texOffs(0, 210).addBox(0, 0, -33, 9, 2, 48, 0, true);

		head = new ModelMapper(modelDataWrapper);
		head.setPos(0, 24, 0);
		head.texOffs(128, 0).addBox(-20, 0, -16, 40, 1, 34, 0, false);
		head.texOffs(116, 298).addBox(-20, -13, -18, 2, 13, 36, 0, false);
		head.texOffs(293, 240).addBox(18, -13, -18, 2, 13, 36, 0, true);
		head.texOffs(322, 289).addBox(-18, -36, 18, 36, 36, 0, 0, false);

		upper_wall_2_r3 = new ModelMapper(modelDataWrapper);
		upper_wall_2_r3.setPos(20, -13, 0);
		head.addChild(upper_wall_2_r3);
		setRotationAngle(upper_wall_2_r3, 0, 0, -0.1107F);
		upper_wall_2_r3.texOffs(0, 260).addBox(-2, -20, -18, 2, 20, 36, 0, true);

		upper_wall_1_r5 = new ModelMapper(modelDataWrapper);
		upper_wall_1_r5.setPos(-20, -13, 0);
		head.addChild(upper_wall_1_r5);
		setRotationAngle(upper_wall_1_r5, 0, 0, 0.1107F);
		upper_wall_1_r5.texOffs(260, 135).addBox(0, -20, -18, 2, 20, 36, 0, false);

		head_exterior = new ModelMapper(modelDataWrapper);
		head_exterior.setPos(0, 24, 0);
		head_exterior.texOffs(232, 68).addBox(-21, 0, 19, 42, 7, 12, 0, false);
		head_exterior.texOffs(305, 52).addBox(20, 0, -16, 1, 7, 35, 0, true);
		head_exterior.texOffs(304, 0).addBox(-21, 0, -16, 1, 7, 35, 0, false);
		head_exterior.texOffs(281, 289).addBox(18, -13, -18, 2, 13, 37, 0, true);
		head_exterior.texOffs(256, 219).addBox(20, -13, 19, 1, 13, 12, 0, true);
		head_exterior.texOffs(240, 276).addBox(-20, -13, -18, 2, 13, 37, 0, false);
		head_exterior.texOffs(256, 219).addBox(-21, -13, 19, 1, 13, 12, 0, false);
		head_exterior.texOffs(0, 316).addBox(-18, -42, 19, 36, 42, 0, 0, false);

		driver_door_upper_2_r1 = new ModelMapper(modelDataWrapper);
		driver_door_upper_2_r1.setPos(-21, -13, 0);
		head_exterior.addChild(driver_door_upper_2_r1);
		setRotationAngle(driver_door_upper_2_r1, 0, 0, 0.1107F);
		driver_door_upper_2_r1.texOffs(232, 102).addBox(0, -20, 19, 1, 20, 12, 0, false);

		upper_wall_2_r4 = new ModelMapper(modelDataWrapper);
		upper_wall_2_r4.setPos(-20, -13, 0);
		head_exterior.addChild(upper_wall_2_r4);
		setRotationAngle(upper_wall_2_r4, 0, 0, 0.1107F);
		upper_wall_2_r4.texOffs(123, 241).addBox(0, -20, -18, 2, 20, 37, 0, false);

		driver_door_upper_1_r1 = new ModelMapper(modelDataWrapper);
		driver_door_upper_1_r1.setPos(21, -13, 0);
		head_exterior.addChild(driver_door_upper_1_r1);
		setRotationAngle(driver_door_upper_1_r1, 0, 0, -0.1107F);
		driver_door_upper_1_r1.texOffs(232, 102).addBox(-1, -20, 19, 1, 20, 12, 0, true);

		upper_wall_1_r6 = new ModelMapper(modelDataWrapper);
		upper_wall_1_r6.setPos(20, -13, 0);
		head_exterior.addChild(upper_wall_1_r6);
		setRotationAngle(upper_wall_1_r6, 0, 0, -0.1107F);
		upper_wall_1_r6.texOffs(251, 219).addBox(-2, -20, -18, 2, 20, 37, 0, true);

		front = new ModelMapper(modelDataWrapper);
		front.setPos(0, 0, 0);
		head_exterior.addChild(front);
		front.texOffs(116, 143).addBox(-3, -10, 45, 6, 5, 0, 0, false);
		front.texOffs(228, 0).addBox(-21, 0, 31, 42, 0, 14, 0, false);

		front_middle_top_2_r1 = new ModelMapper(modelDataWrapper);
		front_middle_top_2_r1.setPos(0, -36.1042F, 37.8711F);
		front.addChild(front_middle_top_2_r1);
		setRotationAngle(front_middle_top_2_r1, -0.7854F, 0, 0);
		front_middle_top_2_r1.texOffs(149, 35).addBox(-6, 0, -3.5F, 12, 0, 8, 0, false);

		front_middle_top_1_r1 = new ModelMapper(modelDataWrapper);
		front_middle_top_1_r1.setPos(0, -42, 26);
		front.addChild(front_middle_top_1_r1);
		setRotationAngle(front_middle_top_1_r1, -0.3491F, 0, 0);
		front_middle_top_1_r1.texOffs(20, 90).addBox(-6, 0, 0, 12, 0, 10, 0, false);

		front_3_r1 = new ModelMapper(modelDataWrapper);
		front_3_r1.setPos(0, -5, 45);
		front.addChild(front_3_r1);
		setRotationAngle(front_3_r1, -0.0873F, 0, 0);
		front_3_r1.texOffs(58, 143).addBox(-3, 0, 0, 6, 12, 0, 0, false);

		front_1_r1 = new ModelMapper(modelDataWrapper);
		front_1_r1.setPos(0, -10, 45);
		front.addChild(front_1_r1);
		setRotationAngle(front_1_r1, 0.1745F, 0, 0);
		front_1_r1.texOffs(260, 191).addBox(-3, -24, 0, 6, 24, 0, 0, false);

		side_1 = new ModelMapper(modelDataWrapper);
		side_1.setPos(0, 0, 0);
		front.addChild(side_1);
		side_1.texOffs(201, 277).addBox(0, -42, 16, 6, 1, 10, 0, false);

		outer_roof_11_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_11_r1.setPos(18.7914F, -32.8777F, 31);
		side_1.addChild(outer_roof_11_r1);
		setRotationAngle(outer_roof_11_r1, -1.0472F, 0.9948F, 0);
		outer_roof_11_r1.texOffs(106, 267).addBox(-9.0009F, 0.0014F, -6, 11, 0, 10, 0, false);

		outer_roof_10_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_10_r1.setPos(5.4063F, -38.6327F, 35.3973F);
		side_1.addChild(outer_roof_10_r1);
		setRotationAngle(outer_roof_10_r1, -0.8378F, 0.0873F, 0.2618F);
		outer_roof_10_r1.texOffs(66, 293).addBox(-3, 0.001F, -2, 14, 0, 10, 0, false);

		outer_roof_9_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_9_r1.setPos(6, -42, 26);
		side_1.addChild(outer_roof_9_r1);
		setRotationAngle(outer_roof_9_r1, -0.3491F, 0, 0.1745F);
		outer_roof_9_r1.texOffs(0, 0).addBox(0, 0, 0, 8, 0, 10, 0, false);

		outer_roof_8_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_8_r1.setPos(18.682F, -33.8719F, 31);
		side_1.addChild(outer_roof_8_r1);
		setRotationAngle(outer_roof_8_r1, 0, 0, 1.1345F);
		outer_roof_8_r1.texOffs(145, 89).addBox(-6, 0.001F, -5, 6, 0, 5, 0, false);

		outer_roof_7_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_7_r1.setPos(13.8776F, -40.6102F, 26);
		side_1.addChild(outer_roof_7_r1);
		setRotationAngle(outer_roof_7_r1, -0.3491F, 0, 0.5236F);
		outer_roof_7_r1.texOffs(130, 89).addBox(0, 0, 0, 5, 0, 10, 0, false);

		outer_roof_5_r4 = new ModelMapper(modelDataWrapper);
		outer_roof_5_r4.setPos(9.9394F, -41.3064F, 0);
		side_1.addChild(outer_roof_5_r4);
		setRotationAngle(outer_roof_5_r4, 0, 0, 0.1745F);
		outer_roof_5_r4.texOffs(116, 249).addBox(-4, 0, 16, 8, 1, 10, 0, false);

		outer_roof_4_r4 = new ModelMapper(modelDataWrapper);
		outer_roof_4_r4.setPos(15.1778F, -39.8628F, 0);
		side_1.addChild(outer_roof_4_r4);
		setRotationAngle(outer_roof_4_r4, 0, 0, 0.5236F);
		outer_roof_4_r4.texOffs(156, 309).addBox(-1.5F, 0, 16, 3, 1, 10, 0, false);

		outer_roof_3_r4 = new ModelMapper(modelDataWrapper);
		outer_roof_3_r4.setPos(16.9769F, -38.2468F, 0);
		side_1.addChild(outer_roof_3_r4);
		setRotationAngle(outer_roof_3_r4, 0, 0, 1.0472F);
		outer_roof_3_r4.texOffs(156, 298).addBox(-1, 0, 16, 2, 1, 10, 0, false);

		outer_roof_2_r4 = new ModelMapper(modelDataWrapper);
		outer_roof_2_r4.setPos(17.5872F, -36.3872F, 0);
		side_1.addChild(outer_roof_2_r4);
		setRotationAngle(outer_roof_2_r4, 0, 0, -0.1107F);
		outer_roof_2_r4.texOffs(138, 143).addBox(-1, -1, 16, 1, 2, 10, 0, true);

		outer_roof_1_r2 = new ModelMapper(modelDataWrapper);
		outer_roof_1_r2.setPos(20, -13, 5);
		side_1.addChild(outer_roof_1_r2);
		setRotationAngle(outer_roof_1_r2, 0, 0, -0.1107F);
		outer_roof_1_r2.texOffs(245, 136).addBox(-1, -23, 11, 2, 4, 15, 0, true);

		front_side_lower_5_r1 = new ModelMapper(modelDataWrapper);
		front_side_lower_5_r1.setPos(8.7065F, -8.5F, 43.6502F);
		side_1.addChild(front_side_lower_5_r1);
		setRotationAngle(front_side_lower_5_r1, 0, 0.2182F, 0);
		front_side_lower_5_r1.texOffs(116, 241).addBox(-7, -3.5F, 0, 14, 7, 0, 0, false);

		front_side_lower_4_r1 = new ModelMapper(modelDataWrapper);
		front_side_lower_4_r1.setPos(17.6615F, -8.5F, 40.0141F);
		side_1.addChild(front_side_lower_4_r1);
		setRotationAngle(front_side_lower_4_r1, 0, 0.7854F, 0);
		front_side_lower_4_r1.texOffs(26, 32).addBox(-3, -3.5F, 0, 6, 7, 0, 0, false);

		front_side_lower_3_r1 = new ModelMapper(modelDataWrapper);
		front_side_lower_3_r1.setPos(1.8727F, -5, 45.1663F);
		side_1.addChild(front_side_lower_3_r1);
		setRotationAngle(front_side_lower_3_r1, -0.0873F, 0.2182F, 0);
		front_side_lower_3_r1.texOffs(197, 70).addBox(0, 0, -0.001F, 15, 12, 0, 0, false);

		front_side_lower_2_r1 = new ModelMapper(modelDataWrapper);
		front_side_lower_2_r1.setPos(15.5408F, -5, 42.1361F);
		side_1.addChild(front_side_lower_2_r1);
		setRotationAngle(front_side_lower_2_r1, -0.0436F, 0.7854F, 0);
		front_side_lower_2_r1.texOffs(0, 123).addBox(0, 0, -0.001F, 7, 12, 0, 0, false);

		front_side_lower_1_r1 = new ModelMapper(modelDataWrapper);
		front_side_lower_1_r1.setPos(21, 7, 31);
		side_1.addChild(front_side_lower_1_r1);
		setRotationAngle(front_side_lower_1_r1, 0, -0.1745F, 0);
		front_side_lower_1_r1.texOffs(116, 203).addBox(0, -20, 0, 0, 20, 7, 0, false);

		front_side_upper_7_r1 = new ModelMapper(modelDataWrapper);
		front_side_upper_7_r1.setPos(15.2976F, -12.6432F, 41.454F);
		side_1.addChild(front_side_upper_7_r1);
		setRotationAngle(front_side_upper_7_r1, 0.4102F, 0.2059F, -0.0436F);
		front_side_upper_7_r1.texOffs(58, 172).addBox(-13, -1, -0.001F, 13, 4, 0, 0, false);

		front_side_upper_6_r1 = new ModelMapper(modelDataWrapper);
		front_side_upper_6_r1.setPos(19.1F, -12.95F, 37.7F);
		side_1.addChild(front_side_upper_6_r1);
		setRotationAngle(front_side_upper_6_r1, 0.3491F, 0.7679F, -0.0436F);
		front_side_upper_6_r1.texOffs(28, 43).addBox(-6.35F, -0.85F, 0, 7, 3, 0, 0, false);

		front_side_upper_5_r1 = new ModelMapper(modelDataWrapper);
		front_side_upper_5_r1.setPos(20.4813F, -12.9424F, 33.9542F);
		side_1.addChild(front_side_upper_5_r1);
		setRotationAngle(front_side_upper_5_r1, 0.4363F, 1.2217F, 0);
		front_side_upper_5_r1.texOffs(30, 100).addBox(-4.9998F, -0.9997F, -0.0001F, 5, 3, 0, 0, false);

		front_side_upper_4_r1 = new ModelMapper(modelDataWrapper);
		front_side_upper_4_r1.setPos(3, -10, 45);
		side_1.addChild(front_side_upper_4_r1);
		setRotationAngle(front_side_upper_4_r1, 0.1745F, 0.2443F, 0);
		front_side_upper_4_r1.texOffs(76, 323).addBox(0, -24, 0, 14, 24, 0, 0, false);

		front_side_upper_3_r1 = new ModelMapper(modelDataWrapper);
		front_side_upper_3_r1.setPos(19.1695F, -12.1451F, 37.781F);
		side_1.addChild(front_side_upper_3_r1);
		setRotationAngle(front_side_upper_3_r1, 0.1309F, 0.7679F, 0);
		front_side_upper_3_r1.texOffs(276, 191).addBox(-5.9989F, -21.9999F, 0.0149F, 7, 23, 0, 0, false);

		front_side_upper_2_r1 = new ModelMapper(modelDataWrapper);
		front_side_upper_2_r1.setPos(19.156F, -22.9499F, 34.5398F);
		side_1.addChild(front_side_upper_2_r1);
		setRotationAngle(front_side_upper_2_r1, 0.0873F, -0.3491F, -0.1107F);
		front_side_upper_2_r1.texOffs(136, 206).addBox(0, -9, -1.5F, 0, 20, 4, 0, false);

		front_side_upper_1_r1 = new ModelMapper(modelDataWrapper);
		front_side_upper_1_r1.setPos(21, -13, 31);
		side_1.addChild(front_side_upper_1_r1);
		setRotationAngle(front_side_upper_1_r1, 0, -0.1745F, -0.1107F);
		front_side_upper_1_r1.texOffs(130, 207).addBox(0, -20, 0, 0, 20, 3, 0, false);

		side_2 = new ModelMapper(modelDataWrapper);
		side_2.setPos(0, 0, 0);
		front.addChild(side_2);
		side_2.texOffs(201, 277).addBox(-6, -42, 16, 6, 1, 10, 0, true);

		outer_roof_11_r2 = new ModelMapper(modelDataWrapper);
		outer_roof_11_r2.setPos(-18.7914F, -32.8777F, 31);
		side_2.addChild(outer_roof_11_r2);
		setRotationAngle(outer_roof_11_r2, -1.0472F, -0.9948F, 0);
		outer_roof_11_r2.texOffs(106, 267).addBox(-1.9991F, 0.0014F, -6, 11, 0, 10, 0, true);

		outer_roof_10_r2 = new ModelMapper(modelDataWrapper);
		outer_roof_10_r2.setPos(-5.4063F, -38.6327F, 35.3973F);
		side_2.addChild(outer_roof_10_r2);
		setRotationAngle(outer_roof_10_r2, -0.8378F, -0.0873F, -0.2618F);
		outer_roof_10_r2.texOffs(66, 293).addBox(-11, 0.001F, -2, 14, 0, 10, 0, true);

		outer_roof_9_r2 = new ModelMapper(modelDataWrapper);
		outer_roof_9_r2.setPos(-6, -42, 26);
		side_2.addChild(outer_roof_9_r2);
		setRotationAngle(outer_roof_9_r2, -0.3491F, 0, -0.1745F);
		outer_roof_9_r2.texOffs(0, 0).addBox(-8, 0, 0, 8, 0, 10, 0, true);

		outer_roof_8_r2 = new ModelMapper(modelDataWrapper);
		outer_roof_8_r2.setPos(-18.682F, -33.8719F, 31);
		side_2.addChild(outer_roof_8_r2);
		setRotationAngle(outer_roof_8_r2, 0, 0, -1.1345F);
		outer_roof_8_r2.texOffs(145, 89).addBox(0, 0.001F, -5, 6, 0, 5, 0, true);

		outer_roof_7_r2 = new ModelMapper(modelDataWrapper);
		outer_roof_7_r2.setPos(-13.8776F, -40.6102F, 26);
		side_2.addChild(outer_roof_7_r2);
		setRotationAngle(outer_roof_7_r2, -0.3491F, 0, -0.5236F);
		outer_roof_7_r2.texOffs(130, 89).addBox(-5, 0, 0, 5, 0, 10, 0, true);

		outer_roof_5_r5 = new ModelMapper(modelDataWrapper);
		outer_roof_5_r5.setPos(-9.9394F, -41.3064F, 0);
		side_2.addChild(outer_roof_5_r5);
		setRotationAngle(outer_roof_5_r5, 0, 0, -0.1745F);
		outer_roof_5_r5.texOffs(116, 249).addBox(-4, 0, 16, 8, 1, 10, 0, true);

		outer_roof_4_r5 = new ModelMapper(modelDataWrapper);
		outer_roof_4_r5.setPos(-15.1778F, -39.8628F, 0);
		side_2.addChild(outer_roof_4_r5);
		setRotationAngle(outer_roof_4_r5, 0, 0, -0.5236F);
		outer_roof_4_r5.texOffs(156, 309).addBox(-1.5F, 0, 16, 3, 1, 10, 0, true);

		outer_roof_3_r5 = new ModelMapper(modelDataWrapper);
		outer_roof_3_r5.setPos(-16.9769F, -38.2468F, 0);
		side_2.addChild(outer_roof_3_r5);
		setRotationAngle(outer_roof_3_r5, 0, 0, -1.0472F);
		outer_roof_3_r5.texOffs(156, 298).addBox(-1, 0, 16, 2, 1, 10, 0, true);

		outer_roof_2_r5 = new ModelMapper(modelDataWrapper);
		outer_roof_2_r5.setPos(-17.5872F, -36.3872F, 0);
		side_2.addChild(outer_roof_2_r5);
		setRotationAngle(outer_roof_2_r5, 0, 0, 0.1107F);
		outer_roof_2_r5.texOffs(138, 143).addBox(0, -1, 16, 1, 2, 10, 0, false);

		outer_roof_1_r3 = new ModelMapper(modelDataWrapper);
		outer_roof_1_r3.setPos(-20, -13, 5);
		side_2.addChild(outer_roof_1_r3);
		setRotationAngle(outer_roof_1_r3, 0, 0, 0.1107F);
		outer_roof_1_r3.texOffs(245, 136).addBox(-1, -23, 11, 2, 4, 15, 0, false);

		front_side_lower_6_r1 = new ModelMapper(modelDataWrapper);
		front_side_lower_6_r1.setPos(-8.7065F, -8.5F, 43.6502F);
		side_2.addChild(front_side_lower_6_r1);
		setRotationAngle(front_side_lower_6_r1, 0, -0.2182F, 0);
		front_side_lower_6_r1.texOffs(116, 241).addBox(-7, -3.5F, 0, 14, 7, 0, 0, true);

		front_side_lower_5_r2 = new ModelMapper(modelDataWrapper);
		front_side_lower_5_r2.setPos(-17.6615F, -8.5F, 40.0141F);
		side_2.addChild(front_side_lower_5_r2);
		setRotationAngle(front_side_lower_5_r2, 0, -0.7854F, 0);
		front_side_lower_5_r2.texOffs(26, 32).addBox(-3, -3.5F, 0, 6, 7, 0, 0, true);

		front_side_lower_4_r2 = new ModelMapper(modelDataWrapper);
		front_side_lower_4_r2.setPos(-1.8727F, -5, 45.1663F);
		side_2.addChild(front_side_lower_4_r2);
		setRotationAngle(front_side_lower_4_r2, -0.0873F, -0.2182F, 0);
		front_side_lower_4_r2.texOffs(197, 70).addBox(-15, 0, -0.001F, 15, 12, 0, 0, true);

		front_side_lower_3_r2 = new ModelMapper(modelDataWrapper);
		front_side_lower_3_r2.setPos(-15.5408F, -5, 42.1361F);
		side_2.addChild(front_side_lower_3_r2);
		setRotationAngle(front_side_lower_3_r2, -0.0436F, -0.7854F, 0);
		front_side_lower_3_r2.texOffs(0, 123).addBox(-7, 0, -0.001F, 7, 12, 0, 0, true);

		front_side_lower_2_r2 = new ModelMapper(modelDataWrapper);
		front_side_lower_2_r2.setPos(-21, 7, 31);
		side_2.addChild(front_side_lower_2_r2);
		setRotationAngle(front_side_lower_2_r2, 0, 0.1745F, 0);
		front_side_lower_2_r2.texOffs(116, 203).addBox(0, -20, 0, 0, 20, 7, 0, true);

		front_side_upper_8_r1 = new ModelMapper(modelDataWrapper);
		front_side_upper_8_r1.setPos(-15.2976F, -12.6432F, 41.454F);
		side_2.addChild(front_side_upper_8_r1);
		setRotationAngle(front_side_upper_8_r1, 0.4102F, -0.2059F, 0.0436F);
		front_side_upper_8_r1.texOffs(58, 172).addBox(0, -1, -0.001F, 13, 4, 0, 0, true);

		front_side_upper_7_r2 = new ModelMapper(modelDataWrapper);
		front_side_upper_7_r2.setPos(-19.1F, -12.95F, 37.7F);
		side_2.addChild(front_side_upper_7_r2);
		setRotationAngle(front_side_upper_7_r2, 0.3491F, -0.7679F, 0.0436F);
		front_side_upper_7_r2.texOffs(28, 43).addBox(-0.65F, -0.85F, 0, 7, 3, 0, 0, true);

		front_side_upper_6_r2 = new ModelMapper(modelDataWrapper);
		front_side_upper_6_r2.setPos(-20.4813F, -12.9424F, 33.9542F);
		side_2.addChild(front_side_upper_6_r2);
		setRotationAngle(front_side_upper_6_r2, 0.4363F, -1.2217F, 0);
		front_side_upper_6_r2.texOffs(30, 100).addBox(-0.0002F, -0.9997F, -0.0001F, 5, 3, 0, 0, true);

		front_side_upper_5_r2 = new ModelMapper(modelDataWrapper);
		front_side_upper_5_r2.setPos(-3, -10, 45);
		side_2.addChild(front_side_upper_5_r2);
		setRotationAngle(front_side_upper_5_r2, 0.1745F, -0.2443F, 0);
		front_side_upper_5_r2.texOffs(76, 323).addBox(-14, -24, 0, 14, 24, 0, 0, true);

		front_side_upper_4_r2 = new ModelMapper(modelDataWrapper);
		front_side_upper_4_r2.setPos(-19.1695F, -12.1451F, 37.781F);
		side_2.addChild(front_side_upper_4_r2);
		setRotationAngle(front_side_upper_4_r2, 0.1309F, -0.7679F, 0);
		front_side_upper_4_r2.texOffs(276, 191).addBox(-1.0011F, -21.9999F, 0.0149F, 7, 23, 0, 0, true);

		front_side_upper_3_r2 = new ModelMapper(modelDataWrapper);
		front_side_upper_3_r2.setPos(-19.156F, -22.9499F, 34.5398F);
		side_2.addChild(front_side_upper_3_r2);
		setRotationAngle(front_side_upper_3_r2, 0.0873F, 0.3491F, 0.1107F);
		front_side_upper_3_r2.texOffs(136, 205).addBox(0, -10, -1.5F, 0, 21, 4, 0, true);

		front_side_upper_2_r2 = new ModelMapper(modelDataWrapper);
		front_side_upper_2_r2.setPos(-21, -13, 31);
		side_2.addChild(front_side_upper_2_r2);
		setRotationAngle(front_side_upper_2_r2, 0, 0.1745F, 0.1107F);
		front_side_upper_2_r2.texOffs(130, 207).addBox(0, -20, 0, 0, 20, 3, 0, true);

		headlights = new ModelMapper(modelDataWrapper);
		headlights.setPos(0, 24, 0);


		headlight_4_r1 = new ModelMapper(modelDataWrapper);
		headlight_4_r1.setPos(-14.0978F, -7, 42.5574F);
		headlights.addChild(headlight_4_r1);
		setRotationAngle(headlight_4_r1, 0, -0.2182F, 0);
		headlight_4_r1.texOffs(39, 0).addBox(-1.5F, -2, 0, 3, 4, 0, 0, true);

		headlight_3_r1 = new ModelMapper(modelDataWrapper);
		headlight_3_r1.setPos(-16.976F, -7, 40.8188F);
		headlights.addChild(headlight_3_r1);
		setRotationAngle(headlight_3_r1, 0, -0.7854F, 0);
		headlight_3_r1.texOffs(31, 0).addBox(-2, -2, 0, 4, 4, 0, 0, true);

		headlight_2_r1 = new ModelMapper(modelDataWrapper);
		headlight_2_r1.setPos(14.0978F, -7, 42.5574F);
		headlights.addChild(headlight_2_r1);
		setRotationAngle(headlight_2_r1, 0, 0.2182F, 0);
		headlight_2_r1.texOffs(39, 0).addBox(-1.5F, -2, 0, 3, 4, 0, 0, false);

		headlight_1_r1 = new ModelMapper(modelDataWrapper);
		headlight_1_r1.setPos(16.976F, -7, 40.8188F);
		headlights.addChild(headlight_1_r1);
		setRotationAngle(headlight_1_r1, 0, 0.7854F, 0);
		headlight_1_r1.texOffs(31, 0).addBox(-2, -2, 0, 4, 4, 0, 0, false);

		tail_lights = new ModelMapper(modelDataWrapper);
		tail_lights.setPos(0, 24, 0);


		tail_light_2_r1 = new ModelMapper(modelDataWrapper);
		tail_light_2_r1.setPos(-14.0978F, -7, 42.5574F);
		tail_lights.addChild(tail_light_2_r1);
		setRotationAngle(tail_light_2_r1, 0, -0.2182F, 0);
		tail_light_2_r1.texOffs(31, 4).addBox(0.5F, -2, 0, 5, 4, 0, 0, true);

		tail_light_1_r1 = new ModelMapper(modelDataWrapper);
		tail_light_1_r1.setPos(14.0978F, -7, 42.5574F);
		tail_lights.addChild(tail_light_1_r1);
		setRotationAngle(tail_light_1_r1, 0, 0.2182F, 0);
		tail_light_1_r1.texOffs(31, 4).addBox(-5.5F, -2, 0, 5, 4, 0, 0, false);

		door_light = new ModelMapper(modelDataWrapper);
		door_light.setPos(0, 24, 0);


		outer_roof_1_r4 = new ModelMapper(modelDataWrapper);
		outer_roof_1_r4.setPos(-20, -13, 0);
		door_light.addChild(outer_roof_1_r4);
		setRotationAngle(outer_roof_1_r4, 0, 0, 0.1107F);
		outer_roof_1_r4.texOffs(58, 85).addBox(-1.1F, -23, -2, 0, 4, 4, 0, false);

		door_light_on = new ModelMapper(modelDataWrapper);
		door_light_on.setPos(0, 24, 0);


		light_r2 = new ModelMapper(modelDataWrapper);
		light_r2.setPos(-20, -13, 0);
		door_light_on.addChild(light_r2);
		setRotationAngle(light_r2, 0, 0, 0.1107F);
		light_r2.texOffs(60, 94).addBox(-1, -21, 0, 0, 0, 0, 0.4F, false);

		door_light_off = new ModelMapper(modelDataWrapper);
		door_light_off.setPos(0, 24, 0);


		light_r3 = new ModelMapper(modelDataWrapper);
		light_r3.setPos(-20, -13, 0);
		door_light_off.addChild(light_r3);
		setRotationAngle(light_r3, 0, 0, 0.1107F);
		light_r3.texOffs(60, 96).addBox(-1, -21, 0, 0, 0, 0, 0.4F, false);

		modelDataWrapper.setModelPart(textureWidth, textureHeight);
		window_1.setModelPart();
		window_2.setModelPart();
		window_handrails.setModelPart();
		seats.setModelPart();
		window_1_tv.setModelPart();
		window_exterior_1.setModelPart();
		window_exterior_2.setModelPart();
		side_panel.setModelPart();
		side_panel_translucent.setModelPart();
		roof_window.setModelPart();
		roof_door.setModelPart();
		roof_light.setModelPart();
		roof_exterior.setModelPart();
		door.setModelPart();
		door_left.setModelPart(door.name);
		door_right.setModelPart(door.name);
		door_handrails.setModelPart();
		door_exterior.setModelPart();
		door_left_exterior.setModelPart(door_exterior.name);
		door_right_exterior.setModelPart(door_exterior.name);
		end.setModelPart();
		end_exterior.setModelPart();
		roof_end.setModelPart();
		roof_end_light.setModelPart();
		roof_end_exterior.setModelPart();
		roof_end_vents.setModelPart();
		head.setModelPart();
		head_exterior.setModelPart();
		headlights.setModelPart();
		tail_lights.setModelPart();
		door_light.setModelPart();
		door_light_on.setModelPart();
		door_light_off.setModelPart();
	}

	private static final int DOOR_MAX = 14;
	private static final ModelDoorOverlay MODEL_DOOR_OVERLAY = new ModelDoorOverlay(DOOR_MAX, 6.34F, 13, "door_overlay_mlr_left.png", "door_overlay_mlr_right.png");
	private static final ModelDoorOverlayTopMLR MODEL_DOOR_OVERLAY_TOP = new ModelDoorOverlayTopMLR("mtr:textures/sign/door_overlay_mlr_top.png");

	@Override
	protected void renderWindowPositions(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
		final boolean isEvenWindow = isEvenWindow(position);
		switch (renderStage) {
			case LIGHTS:
				renderMirror(roof_light, matrices, vertices, light, position);
				break;
			case INTERIOR:
				if (isEvenWindow) {
					renderOnceFlipped(window_1_tv, matrices, vertices, light, position);
					renderOnceFlipped(window_1, matrices, vertices, light, position);
					renderOnce(window_2, matrices, vertices, light, position);
				} else {
					renderOnce(window_1_tv, matrices, vertices, light, position);
					renderOnce(window_1, matrices, vertices, light, position);
					renderOnceFlipped(window_2, matrices, vertices, light, position);
				}
				if (renderDetails) {
					renderMirror(roof_window, matrices, vertices, light, position);
					renderMirror(window_handrails, matrices, vertices, light, position);
					renderMirror(seats, matrices, vertices, light, position + (isEvenWindow ? -0.75F : 0.75F));
					renderMirror(side_panel, matrices, vertices, light, position - (isEvenWindow ? 15.75F : 14.25F));
					renderMirror(side_panel, matrices, vertices, light, position + (isEvenWindow ? 14.25F : 15.75F));
				}
				break;
			case INTERIOR_TRANSLUCENT:
				renderMirror(side_panel_translucent, matrices, vertices, light, position - (isEvenWindow ? 15.75F : 14.25F));
				renderMirror(side_panel_translucent, matrices, vertices, light, position + (isEvenWindow ? 14.25F : 15.75F));
				break;
			case EXTERIOR:
				if (isEvenWindow) {
					renderOnceFlipped(window_exterior_1, matrices, vertices, light, position);
					renderOnce(window_exterior_2, matrices, vertices, light, position);
				} else {
					renderOnce(window_exterior_1, matrices, vertices, light, position);
					renderOnceFlipped(window_exterior_2, matrices, vertices, light, position);
				}
				renderMirror(roof_exterior, matrices, vertices, light, position);
				break;
		}
	}

	@Override
	protected void renderDoorPositions(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
		final boolean middleDoor = isIndex(getDoorPositions().length / 2, position, getDoorPositions());
		final boolean doorOpen = doorLeftZ > 0 || doorRightZ > 0;

		switch (renderStage) {
			case LIGHTS:
				renderMirror(roof_light, matrices, vertices, light, position);
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
					renderOnce(door_handrails, matrices, vertices, light, position);
					renderMirror(roof_door, matrices, vertices, light, position);
				}
				break;
			case EXTERIOR:
				door_left_exterior.setOffset(0, 0, doorRightZ);
				door_right_exterior.setOffset(0, 0, -doorRightZ);
				renderOnce(door_exterior, matrices, vertices, light, position);
				door_left_exterior.setOffset(0, 0, doorLeftZ);
				door_right_exterior.setOffset(0, 0, -doorLeftZ);
				renderOnceFlipped(door_exterior, matrices, vertices, light, position);
				renderMirror(roof_exterior, matrices, vertices, light, position);

				if (middleDoor && renderDetails) {
					renderMirror(door_light, matrices, vertices, light, position - 22);
					if (!doorOpen) {
						renderMirror(door_light_off, matrices, vertices, light, position - 22);
					}
				}
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
			case INTERIOR_TRANSLUCENT:
				renderMirror(side_panel, matrices, vertices, light, position + 14.25F);
				break;
			case EXTERIOR:
				renderOnceFlipped(head_exterior, matrices, vertices, light, position);
				renderMirror(roof_exterior, matrices, vertices, light, position);
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
			case INTERIOR_TRANSLUCENT:
				renderMirror(side_panel, matrices, vertices, light, position - 14.25F);
				break;
			case EXTERIOR:
				renderOnce(head_exterior, matrices, vertices, light, position);
				renderMirror(roof_exterior, matrices, vertices, light, position);
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
				}
				break;
			case INTERIOR_TRANSLUCENT:
				renderMirror(side_panel, matrices, vertices, light, position + 14.25F);
				break;
			case EXTERIOR:
				renderOnce(end_exterior, matrices, vertices, light, position);
				renderMirror(roof_exterior, matrices, vertices, light, position);
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
				}
				break;
			case INTERIOR_TRANSLUCENT:
				renderMirror(side_panel, matrices, vertices, light, position - 14.25F);
				break;
			case EXTERIOR:
				renderOnceFlipped(end_exterior, matrices, vertices, light, position);
				renderMirror(roof_exterior, matrices, vertices, light, position);
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
		return new int[]{-96, -32, 32, 96};
	}

	@Override
	protected int[] getDoorPositions() {
		return new int[]{-128, -64, 0, 64, 128};
	}

	@Override
	protected int[] getEndPositions() {
		return new int[]{-160, 160};
	}

	@Override
	protected int[] getBogiePositions() {
		return new int[]{-136, 136};
	}

	@Override
	protected float getDoorAnimationX(float value, boolean opening) {
		return 0;
	}

	@Override
	protected float getDoorAnimationZ(float value, boolean opening) {
		if (opening) {
			if (value < 0.2) {
				return 0;
			} else if (value > 0.7) {
				return DOOR_MAX;
			} else {
				return (value - 0.2F) * 2 * DOOR_MAX;
			}
		} else {
			final float stoppingPoint = 1.5F;
			if (value > 0.25) {
				return Math.min((value - 0.25F) * 2 * DOOR_MAX + stoppingPoint + 1, DOOR_MAX);
			} else if (value > 0.2) {
				return smoothEnds(stoppingPoint, stoppingPoint + 2, 0.2F, 0.3F, value);
			} else if (value < 0.1) {
				return value / 0.1F * stoppingPoint;
			} else {
				return stoppingPoint;
			}
		}
	}

	private boolean isEvenWindow(int position) {
		return isIndex(1, position, getWindowPositions()) || isIndex(3, position, getWindowPositions());
	}
}