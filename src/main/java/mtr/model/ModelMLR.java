package mtr.model;

import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class ModelMLR extends ModelTrainBase {

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

		final ModelData modelData = new ModelData();
		final ModelPartData modelPartData = modelData.getRoot();

		window_1 = new ModelMapper(modelPartData);
		window_1.setPivot(0, 24, 0);
		window_1.setTextureOffset(232, 102).addCuboid(-20, 0, -16, 20, 1, 32, 0, false);
		window_1.setTextureOffset(300, 99).addCuboid(-20, -13, -18, 2, 13, 36, 0, false);

		upper_wall_r1 = new ModelMapper(modelPartData);
		upper_wall_r1.setPivot(-20, -13, 0);
		window_1.addChild(upper_wall_r1);
		setRotationAngle(upper_wall_r1, 0, 0, 0.1107F);
		upper_wall_r1.setTextureOffset(76, 267).addCuboid(0, -20, -18, 2, 20, 36, 0, false);

		window_2 = new ModelMapper(modelPartData);
		window_2.setPivot(0, 24, 0);
		window_2.setTextureOffset(184, 219).addCuboid(-20, 0, -16, 20, 1, 32, 0, false);
		window_2.setTextureOffset(292, 191).addCuboid(-20, -13, -18, 2, 13, 36, 0, false);

		upper_wall_r2 = new ModelMapper(modelPartData);
		upper_wall_r2.setPivot(-20, -13, 0);
		window_2.addChild(upper_wall_r2);
		setRotationAngle(upper_wall_r2, 0, 0, 0.1107F);
		upper_wall_r2.setTextureOffset(201, 252).addCuboid(0, -20, -18, 2, 20, 36, 0, false);

		window_handrails = new ModelMapper(modelPartData);
		window_handrails.setPivot(0, 24, 0);
		window_handrails.setTextureOffset(8, 1).addCuboid(0, -33, 0, 0, 33, 0, 0.2F, false);
		window_handrails.setTextureOffset(0, 69).addCuboid(-1, -32, 15, 2, 4, 0, 0, false);
		window_handrails.setTextureOffset(0, 69).addCuboid(-1, -32, 9, 2, 4, 0, 0, false);
		window_handrails.setTextureOffset(0, 69).addCuboid(-1, -32, 3, 2, 4, 0, 0, false);
		window_handrails.setTextureOffset(0, 69).addCuboid(-1, -32, -3, 2, 4, 0, 0, false);
		window_handrails.setTextureOffset(0, 69).addCuboid(-1, -32, -9, 2, 4, 0, 0, false);
		window_handrails.setTextureOffset(0, 69).addCuboid(-1, -32, -15, 2, 4, 0, 0, false);

		handrail_8_r1 = new ModelMapper(modelPartData);
		handrail_8_r1.setPivot(0, 0, 0);
		window_handrails.addChild(handrail_8_r1);
		setRotationAngle(handrail_8_r1, -1.5708F, 0, 0);
		handrail_8_r1.setTextureOffset(0, 0).addCuboid(0, -16, -31.5F, 0, 32, 0, 0.2F, false);

		seats = new ModelMapper(modelPartData);
		seats.setPivot(0, 24, 0);
		seats.setTextureOffset(156, 298).addCuboid(-18, -6, -15.5F, 7, 1, 31, 0, false);

		seat_back_r1 = new ModelMapper(modelPartData);
		seat_back_r1.setPivot(-17, -6, 0);
		seats.addChild(seat_back_r1);
		setRotationAngle(seat_back_r1, 0, 0, -0.0524F);
		seat_back_r1.setTextureOffset(328, 325).addCuboid(-1, -8, -15.5F, 1, 8, 31, 0, false);

		handrail_3_r1 = new ModelMapper(modelPartData);
		handrail_3_r1.setPivot(-11, -5, 0);
		seats.addChild(handrail_3_r1);
		setRotationAngle(handrail_3_r1, 0, 0, -0.0436F);
		handrail_3_r1.setTextureOffset(4, 16).addCuboid(0, -13.2F, 15, 0, 13, 0, 0.2F, false);
		handrail_3_r1.setTextureOffset(4, 16).addCuboid(0, -13.2F, -15, 0, 13, 0, 0.2F, false);

		handrail_2_r1 = new ModelMapper(modelPartData);
		handrail_2_r1.setPivot(-12.2986F, -26.5473F, -14);
		seats.addChild(handrail_2_r1);
		setRotationAngle(handrail_2_r1, 0, 0, -0.0873F);
		handrail_2_r1.setTextureOffset(4, 0).addCuboid(0, -8, -1, 0, 16, 0, 0.2F, false);
		handrail_2_r1.setTextureOffset(4, 0).addCuboid(0, -8, 29, 0, 16, 0, 0.2F, false);

		window_1_tv = new ModelMapper(modelPartData);
		window_1_tv.setPivot(0, 24, 0);
		window_1_tv.setTextureOffset(232, 135).addCuboid(-17, -32.5F, -5.5F, 2, 4, 12, 0, false);

		tv_r1 = new ModelMapper(modelPartData);
		tv_r1.setPivot(-15, -28.5F, 0);
		window_1_tv.addChild(tv_r1);
		setRotationAngle(tv_r1, 0, 0, 0.5672F);
		tv_r1.setTextureOffset(20, 155).addCuboid(-2, -8, -5.5F, 2, 8, 12, 0, false);

		window_exterior_1 = new ModelMapper(modelPartData);
		window_exterior_1.setPivot(0, 24, 0);
		window_exterior_1.setTextureOffset(234, 326).addCuboid(-21, 0, -16, 1, 7, 32, 0, false);
		window_exterior_1.setTextureOffset(40, 244).addCuboid(-20, -13, -18, 0, 13, 36, 0, false);

		upper_wall_r3 = new ModelMapper(modelPartData);
		upper_wall_r3.setPivot(-20, -13, 0);
		window_exterior_1.addChild(upper_wall_r3);
		setRotationAngle(upper_wall_r3, 0, 0, 0.1107F);
		upper_wall_r3.setTextureOffset(164, 216).addCuboid(0, -20, -18, 0, 20, 36, 0, false);

		window_exterior_2 = new ModelMapper(modelPartData);
		window_exterior_2.setPivot(0, 24, 0);
		window_exterior_2.setTextureOffset(200, 308).addCuboid(-21, 0, -16, 1, 7, 32, 0, false);
		window_exterior_2.setTextureOffset(40, 231).addCuboid(-20, -13, -18, 0, 13, 36, 0, false);

		upper_wall_r4 = new ModelMapper(modelPartData);
		upper_wall_r4.setPivot(-20, -13, 0);
		window_exterior_2.addChild(upper_wall_r4);
		setRotationAngle(upper_wall_r4, 0, 0, 0.1107F);
		upper_wall_r4.setTextureOffset(68, 13).addCuboid(0, -20, -18, 0, 20, 36, 0, false);

		side_panel = new ModelMapper(modelPartData);
		side_panel.setPivot(0, 24, 0);
		side_panel.setTextureOffset(206, 123).addCuboid(-18, -34, 0, 7, 31, 0, 0, false);

		roof_window = new ModelMapper(modelPartData);
		roof_window.setPivot(0, 24, 0);
		roof_window.setTextureOffset(4, 123).addCuboid(-16, -32, -16, 2, 0, 32, 0, false);
		roof_window.setTextureOffset(84, 69).addCuboid(-10.2292F, -34.8796F, -16, 5, 0, 32, 0, false);
		roof_window.setTextureOffset(122, 0).addCuboid(-2, -33, -16, 2, 0, 32, 0, false);

		inner_roof_5_r1 = new ModelMapper(modelPartData);
		inner_roof_5_r1.setPivot(-2, -33, 0);
		roof_window.addChild(inner_roof_5_r1);
		setRotationAngle(inner_roof_5_r1, 0, 0, 0.5236F);
		inner_roof_5_r1.setTextureOffset(96, 0).addCuboid(-4, 0, -16, 4, 0, 32, 0, false);

		inner_roof_3_r1 = new ModelMapper(modelPartData);
		inner_roof_3_r1.setPivot(-11.6147F, -34.3057F, 0);
		roof_window.addChild(inner_roof_3_r1);
		setRotationAngle(inner_roof_3_r1, 0, 0, -0.3927F);
		inner_roof_3_r1.setTextureOffset(104, 0).addCuboid(-1.5F, 0, -16, 3, 0, 32, 0, false);

		inner_roof_2_r1 = new ModelMapper(modelPartData);
		inner_roof_2_r1.setPivot(-14, -32, 0);
		roof_window.addChild(inner_roof_2_r1);
		setRotationAngle(inner_roof_2_r1, 0, 0, -1.0472F);
		inner_roof_2_r1.setTextureOffset(0, 123).addCuboid(0, 0, -16, 2, 0, 32, 0, false);

		roof_door = new ModelMapper(modelPartData);
		roof_door.setPivot(0, 24, 0);
		roof_door.setTextureOffset(94, 69).addCuboid(-18, -32, -16, 4, 0, 32, 0, false);
		roof_door.setTextureOffset(64, 69).addCuboid(-10.2292F, -34.8796F, -16, 5, 0, 32, 0, false);
		roof_door.setTextureOffset(114, 0).addCuboid(-2, -33, -16, 2, 0, 32, 0, false);

		inner_roof_6_r1 = new ModelMapper(modelPartData);
		inner_roof_6_r1.setPivot(-2, -33, 0);
		roof_door.addChild(inner_roof_6_r1);
		setRotationAngle(inner_roof_6_r1, 0, 0, 0.5236F);
		inner_roof_6_r1.setTextureOffset(0, 49).addCuboid(-4, 0, -16, 4, 0, 32, 0, false);

		inner_roof_4_r1 = new ModelMapper(modelPartData);
		inner_roof_4_r1.setPivot(-11.6147F, -34.3057F, 0);
		roof_door.addChild(inner_roof_4_r1);
		setRotationAngle(inner_roof_4_r1, 0, 0, -0.3927F);
		inner_roof_4_r1.setTextureOffset(102, 69).addCuboid(-1.5F, 0, -16, 3, 0, 32, 0, false);

		inner_roof_3_r2 = new ModelMapper(modelPartData);
		inner_roof_3_r2.setPivot(-14, -32, 0);
		roof_door.addChild(inner_roof_3_r2);
		setRotationAngle(inner_roof_3_r2, 0, 0, -1.0472F);
		inner_roof_3_r2.setTextureOffset(118, 0).addCuboid(0, 0, -16, 2, 0, 32, 0, false);

		roof_light = new ModelMapper(modelPartData);
		roof_light.setPivot(0, 24, 0);


		light_r1 = new ModelMapper(modelPartData);
		light_r1.setPivot(-2, -33, 0);
		roof_light.addChild(light_r1);
		setRotationAngle(light_r1, 0, 0, 0.5236F);
		light_r1.setTextureOffset(108, 49).addCuboid(-4, -0.1F, -16, 4, 0, 32, 0, false);

		roof_exterior = new ModelMapper(modelPartData);
		roof_exterior.setPivot(0, 24, 0);
		roof_exterior.setTextureOffset(52, 69).addCuboid(-6, -42, -16, 6, 0, 32, 0, false);

		outer_roof_5_r1 = new ModelMapper(modelPartData);
		outer_roof_5_r1.setPivot(-9.9394F, -41.3064F, 0);
		roof_exterior.addChild(outer_roof_5_r1);
		setRotationAngle(outer_roof_5_r1, 0, 0, -0.1745F);
		outer_roof_5_r1.setTextureOffset(36, 69).addCuboid(-4, 0, -16, 8, 0, 32, 0, false);

		outer_roof_4_r1 = new ModelMapper(modelPartData);
		outer_roof_4_r1.setPivot(-15.1778F, -39.8628F, 0);
		roof_exterior.addChild(outer_roof_4_r1);
		setRotationAngle(outer_roof_4_r1, 0, 0, -0.5236F);
		outer_roof_4_r1.setTextureOffset(74, 69).addCuboid(-1.5F, 0, -16, 3, 0, 32, 0, false);

		outer_roof_3_r1 = new ModelMapper(modelPartData);
		outer_roof_3_r1.setPivot(-16.9769F, -38.2468F, 0);
		roof_exterior.addChild(outer_roof_3_r1);
		setRotationAngle(outer_roof_3_r1, 0, 0, -1.0472F);
		outer_roof_3_r1.setTextureOffset(110, 0).addCuboid(-1, 0, -16, 2, 0, 32, 0, false);

		outer_roof_2_r1 = new ModelMapper(modelPartData);
		outer_roof_2_r1.setPivot(-17.5872F, -36.3872F, 0);
		roof_exterior.addChild(outer_roof_2_r1);
		setRotationAngle(outer_roof_2_r1, 0, 0, 0.1107F);
		outer_roof_2_r1.setTextureOffset(66, 178).addCuboid(0, -1, -16, 0, 2, 32, 0, false);

		outer_roof_1_r1 = new ModelMapper(modelPartData);
		outer_roof_1_r1.setPivot(-20, -13, 0);
		roof_exterior.addChild(outer_roof_1_r1);
		setRotationAngle(outer_roof_1_r1, 0, 0, 0.1107F);
		outer_roof_1_r1.setTextureOffset(160, 330).addCuboid(-1, -23, -16, 1, 4, 32, 0, false);

		door = new ModelMapper(modelPartData);
		door.setPivot(0, 24, 0);
		door.setTextureOffset(232, 35).addCuboid(-20, 0, -16, 20, 1, 32, 0, false);

		door_left = new ModelMapper(modelPartData);
		door_left.setPivot(0, 0, 0);
		door.addChild(door_left);
		door_left.setTextureOffset(232, 35).addCuboid(-20.8F, -13, 0, 1, 13, 15, 0, false);

		door_left_top_r1 = new ModelMapper(modelPartData);
		door_left_top_r1.setPivot(-20.8F, -13, 0);
		door_left.addChild(door_left_top_r1);
		setRotationAngle(door_left_top_r1, 0, 0, 0.1107F);
		door_left_top_r1.setTextureOffset(194, 35).addCuboid(0, -20, 0, 1, 20, 15, 0, false);

		door_right = new ModelMapper(modelPartData);
		door_right.setPivot(0, 0, 0);
		door.addChild(door_right);
		door_right.setTextureOffset(174, 123).addCuboid(-20.8F, -13, -15, 1, 13, 15, 0, false);

		door_right_top_r1 = new ModelMapper(modelPartData);
		door_right_top_r1.setPivot(-20.8F, -13, 0);
		door_right.addChild(door_right_top_r1);
		setRotationAngle(door_right_top_r1, 0, 0, 0.1107F);
		door_right_top_r1.setTextureOffset(0, 123).addCuboid(0, -20, -15, 1, 20, 15, 0, false);

		door_handrails = new ModelMapper(modelPartData);
		door_handrails.setPivot(0, 24, 0);
		door_handrails.setTextureOffset(8, 0).addCuboid(0, -33, 0, 0, 33, 0, 0.2F, false);

		handrail_8_r2 = new ModelMapper(modelPartData);
		handrail_8_r2.setPivot(0, 0, 0);
		door_handrails.addChild(handrail_8_r2);
		setRotationAngle(handrail_8_r2, -1.5708F, 0, 0);
		handrail_8_r2.setTextureOffset(0, 0).addCuboid(0, -16, -31.5F, 0, 32, 0, 0.2F, false);

		door_exterior = new ModelMapper(modelPartData);
		door_exterior.setPivot(0, 24, 0);
		door_exterior.setTextureOffset(72, 323).addCuboid(-21, 0, -16, 1, 7, 32, 0, false);

		door_left_exterior = new ModelMapper(modelPartData);
		door_left_exterior.setPivot(0, 0, 0);
		door_exterior.addChild(door_left_exterior);
		door_left_exterior.setTextureOffset(128, 20).addCuboid(-20.8F, -13, 0, 0, 13, 15, 0, false);

		door_left_top_r2 = new ModelMapper(modelPartData);
		door_left_top_r2.setPivot(-20.8F, -13, 0);
		door_left_exterior.addChild(door_left_top_r2);
		setRotationAngle(door_left_top_r2, 0, 0, 0.1107F);
		door_left_top_r2.setTextureOffset(82, 128).addCuboid(0, -20, 0, 0, 20, 15, 0, false);

		door_right_exterior = new ModelMapper(modelPartData);
		door_right_exterior.setPivot(0, 0, 0);
		door_exterior.addChild(door_right_exterior);
		door_right_exterior.setTextureOffset(0, 75).addCuboid(-20.8F, -13, -15, 0, 13, 15, 0, false);

		door_right_top_r2 = new ModelMapper(modelPartData);
		door_right_top_r2.setPivot(-20.8F, -13, 0);
		door_right_exterior.addChild(door_right_top_r2);
		setRotationAngle(door_right_top_r2, 0, 0, 0.1107F);
		door_right_top_r2.setTextureOffset(0, 34).addCuboid(0, -20, -15, 0, 20, 15, 0, false);

		end = new ModelMapper(modelPartData);
		end.setPivot(0, 24, 0);
		end.setTextureOffset(0, 0).addCuboid(-20, 0, -32, 40, 1, 48, 0, false);
		end.setTextureOffset(174, 102).addCuboid(18, -13, -36, 2, 13, 54, 0, true);
		end.setTextureOffset(174, 35).addCuboid(-20, -13, -36, 2, 13, 54, 0, false);
		end.setTextureOffset(0, 197).addCuboid(6, -32, -36, 12, 32, 12, 0, false);
		end.setTextureOffset(66, 212).addCuboid(-18, -32, -36, 12, 32, 12, 0, false);
		end.setTextureOffset(242, 14).addCuboid(-18, -35, -36, 36, 3, 12, 0, false);

		upper_wall_2_r1 = new ModelMapper(modelPartData);
		upper_wall_2_r1.setPivot(-20, -13, 0);
		end.addChild(upper_wall_2_r1);
		setRotationAngle(upper_wall_2_r1, 0, 0, 0.1107F);
		upper_wall_2_r1.setTextureOffset(116, 49).addCuboid(0, -20, -36, 2, 20, 54, 0, false);

		upper_wall_1_r1 = new ModelMapper(modelPartData);
		upper_wall_1_r1.setPivot(20, -13, 0);
		end.addChild(upper_wall_1_r1);
		setRotationAngle(upper_wall_1_r1, 0, 0, -0.1107F);
		upper_wall_1_r1.setTextureOffset(0, 123).addCuboid(-2, -20, -36, 2, 20, 54, 0, true);

		seat_1 = new ModelMapper(modelPartData);
		seat_1.setPivot(0, 0, 0);
		end.addChild(seat_1);
		seat_1.setTextureOffset(116, 165).addCuboid(6, -6, -23, 12, 1, 7, 0, false);

		seat_back_r2 = new ModelMapper(modelPartData);
		seat_back_r2.setPivot(0, -6, -22);
		seat_1.addChild(seat_back_r2);
		setRotationAngle(seat_back_r2, 0.0524F, 0, 0);
		seat_back_r2.setTextureOffset(0, 241).addCuboid(6, -8, -1, 12, 8, 1, 0, false);

		seat_2 = new ModelMapper(modelPartData);
		seat_2.setPivot(0, 0, 0);
		end.addChild(seat_2);
		seat_2.setTextureOffset(58, 164).addCuboid(6, -6, -11, 12, 1, 7, 0, false);

		seat_back_r3 = new ModelMapper(modelPartData);
		seat_back_r3.setPivot(0, -6, -10);
		seat_2.addChild(seat_back_r3);
		setRotationAngle(seat_back_r3, 0.0524F, 0, 0);
		seat_back_r3.setTextureOffset(172, 219).addCuboid(6, -8, -1, 12, 8, 1, 0, false);

		seat_3 = new ModelMapper(modelPartData);
		seat_3.setPivot(0, 0, 0);
		end.addChild(seat_3);
		seat_3.setTextureOffset(174, 169).addCuboid(11, -6, 6, 7, 1, 7, 0, false);

		seat_back_r4 = new ModelMapper(modelPartData);
		seat_back_r4.setPivot(0, -6, 12);
		seat_3.addChild(seat_back_r4);
		setRotationAngle(seat_back_r4, -0.0524F, 0, 0);
		seat_back_r4.setTextureOffset(0, 80).addCuboid(11, -8, 0, 7, 8, 1, 0, false);

		seat_4 = new ModelMapper(modelPartData);
		seat_4.setPivot(0, 0, 0);
		end.addChild(seat_4);
		seat_4.setTextureOffset(116, 165).addCuboid(-18, -6, -23, 12, 1, 7, 0, true);

		seat_back_r5 = new ModelMapper(modelPartData);
		seat_back_r5.setPivot(0, -6, -22);
		seat_4.addChild(seat_back_r5);
		setRotationAngle(seat_back_r5, 0.0524F, 0, 0);
		seat_back_r5.setTextureOffset(0, 241).addCuboid(-18, -8, -1, 12, 8, 1, 0, true);

		seat_5 = new ModelMapper(modelPartData);
		seat_5.setPivot(0, 0, 0);
		end.addChild(seat_5);
		seat_5.setTextureOffset(58, 164).addCuboid(-18, -6, -11, 12, 1, 7, 0, true);

		seat_back_r6 = new ModelMapper(modelPartData);
		seat_back_r6.setPivot(0, -6, -10);
		seat_5.addChild(seat_back_r6);
		setRotationAngle(seat_back_r6, 0.0524F, 0, 0);
		seat_back_r6.setTextureOffset(172, 219).addCuboid(-18, -8, -1, 12, 8, 1, 0, true);

		seat_6 = new ModelMapper(modelPartData);
		seat_6.setPivot(0, 0, 0);
		end.addChild(seat_6);
		seat_6.setTextureOffset(174, 169).addCuboid(-18, -6, 6, 7, 1, 7, 0, true);

		seat_back_r7 = new ModelMapper(modelPartData);
		seat_back_r7.setPivot(0, -6, 12);
		seat_6.addChild(seat_back_r7);
		setRotationAngle(seat_back_r7, -0.0524F, 0, 0);
		seat_back_r7.setTextureOffset(0, 80).addCuboid(-18, -8, 0, 7, 8, 1, 0, true);

		end_exterior = new ModelMapper(modelPartData);
		end_exterior.setPivot(0, 24, 0);
		end_exterior.setTextureOffset(70, 216).addCuboid(20, 0, -28, 1, 7, 44, 0, true);
		end_exterior.setTextureOffset(126, 190).addCuboid(-21, 0, -28, 1, 7, 44, 0, false);
		end_exterior.setTextureOffset(58, 143).addCuboid(18, -13, -36, 2, 13, 54, 0, true);
		end_exterior.setTextureOffset(116, 123).addCuboid(-20, -13, -36, 2, 13, 54, 0, false);
		end_exterior.setTextureOffset(0, 260).addCuboid(6, -33, -36, 12, 33, 0, 0, true);
		end_exterior.setTextureOffset(0, 260).addCuboid(-18, -33, -36, 12, 33, 0, 0, false);
		end_exterior.setTextureOffset(300, 148).addCuboid(-18, -41, -36, 36, 9, 0, 0, false);

		upper_wall_2_r2 = new ModelMapper(modelPartData);
		upper_wall_2_r2.setPivot(-20, -13, 0);
		end_exterior.addChild(upper_wall_2_r2);
		setRotationAngle(upper_wall_2_r2, 0, 0, 0.1107F);
		upper_wall_2_r2.setTextureOffset(0, 49).addCuboid(0, -20, -36, 2, 20, 54, 0, false);

		upper_wall_1_r2 = new ModelMapper(modelPartData);
		upper_wall_1_r2.setPivot(20, -13, 0);
		end_exterior.addChild(upper_wall_1_r2);
		setRotationAngle(upper_wall_1_r2, 0, 0, -0.1107F);
		upper_wall_1_r2.setTextureOffset(58, 69).addCuboid(-2, -20, -36, 2, 20, 54, 0, true);

		roof_end = new ModelMapper(modelPartData);
		roof_end.setPivot(0, 24, 0);
		roof_end.setTextureOffset(134, 49).addCuboid(-18, -32, -24, 4, 0, 40, 0, false);
		roof_end.setTextureOffset(18, 49).addCuboid(-10.2292F, -34.8796F, -24, 5, 0, 40, 0, false);
		roof_end.setTextureOffset(146, 35).addCuboid(-2, -33, -24, 2, 0, 40, 0, false);
		roof_end.setTextureOffset(10, 49).addCuboid(0, -33, -24, 2, 0, 40, 0, true);
		roof_end.setTextureOffset(0, 49).addCuboid(5.2292F, -34.8796F, -24, 5, 0, 40, 0, true);
		roof_end.setTextureOffset(116, 49).addCuboid(14, -32, -24, 4, 0, 40, 0, true);
		roof_end.setTextureOffset(0, 0).addCuboid(0, -33.4899F, -16.9899F, 0, 1, 0, 0.2F, false);
		roof_end.setTextureOffset(0, 69).addCuboid(-1, -32, 15, 2, 4, 0, 0, false);
		roof_end.setTextureOffset(0, 69).addCuboid(-1, -32, 9, 2, 4, 0, 0, false);
		roof_end.setTextureOffset(0, 69).addCuboid(-1, -32, 3, 2, 4, 0, 0, false);

		handrail_4_r1 = new ModelMapper(modelPartData);
		handrail_4_r1.setPivot(12.2986F, -26.5473F, 0);
		roof_end.addChild(handrail_4_r1);
		setRotationAngle(handrail_4_r1, 0, 0, 0.0873F);
		handrail_4_r1.setTextureOffset(4, 0).addCuboid(0, -8, 14.25F, 0, 16, 0, 0.2F, true);

		handrail_3_r2 = new ModelMapper(modelPartData);
		handrail_3_r2.setPivot(11, -5, 0);
		roof_end.addChild(handrail_3_r2);
		setRotationAngle(handrail_3_r2, 0, 0, 0.0436F);
		handrail_3_r2.setTextureOffset(4, 16).addCuboid(0, -13.2F, 14.25F, 0, 13, 0, 0.2F, true);

		handrail_2_r2 = new ModelMapper(modelPartData);
		handrail_2_r2.setPivot(-12.2986F, -26.5473F, 0);
		roof_end.addChild(handrail_2_r2);
		setRotationAngle(handrail_2_r2, 0, 0, -0.0873F);
		handrail_2_r2.setTextureOffset(4, 0).addCuboid(0, -8, 14.25F, 0, 16, 0, 0.2F, false);

		handrail_1_r1 = new ModelMapper(modelPartData);
		handrail_1_r1.setPivot(-11, -5, 0);
		roof_end.addChild(handrail_1_r1);
		setRotationAngle(handrail_1_r1, 0, 0, -0.0436F);
		handrail_1_r1.setTextureOffset(4, 16).addCuboid(0, -13.2F, 14.25F, 0, 13, 0, 0.2F, false);

		handrail_9_r1 = new ModelMapper(modelPartData);
		handrail_9_r1.setPivot(0, -31.3F, -16.2F);
		roof_end.addChild(handrail_9_r1);
		setRotationAngle(handrail_9_r1, 0.7854F, 0, 0);
		handrail_9_r1.setTextureOffset(0, 0).addCuboid(0, -1.2F, 0.2F, 0, 1, 0, 0.2F, false);

		handrail_8_r3 = new ModelMapper(modelPartData);
		handrail_8_r3.setPivot(0, 0, 0);
		roof_end.addChild(handrail_8_r3);
		setRotationAngle(handrail_8_r3, -1.5708F, 0, 0);
		handrail_8_r3.setTextureOffset(0, 0).addCuboid(0, -16, -31.5F, 0, 32, 0, 0.2F, false);

		inner_roof_right_4_r1 = new ModelMapper(modelPartData);
		inner_roof_right_4_r1.setPivot(14, -32, 0);
		roof_end.addChild(inner_roof_right_4_r1);
		setRotationAngle(inner_roof_right_4_r1, 0, 0, 1.0472F);
		inner_roof_right_4_r1.setTextureOffset(142, 35).addCuboid(-2, 0, -24, 2, 0, 40, 0, true);

		inner_roof_right_5_r1 = new ModelMapper(modelPartData);
		inner_roof_right_5_r1.setPivot(11.6147F, -34.3057F, 0);
		roof_end.addChild(inner_roof_right_5_r1);
		setRotationAngle(inner_roof_right_5_r1, 0, 0, 0.3927F);
		inner_roof_right_5_r1.setTextureOffset(8, 123).addCuboid(-1.5F, 0, -24, 3, 0, 40, 0, true);

		inner_roof_right_7_r1 = new ModelMapper(modelPartData);
		inner_roof_right_7_r1.setPivot(2, -33, 0);
		roof_end.addChild(inner_roof_right_7_r1);
		setRotationAngle(inner_roof_right_7_r1, 0, 0, -0.5236F);
		inner_roof_right_7_r1.setTextureOffset(108, 49).addCuboid(0, 0, -24, 4, 0, 40, 0, true);

		inner_roof_left_7_r1 = new ModelMapper(modelPartData);
		inner_roof_left_7_r1.setPivot(-2, -33, 0);
		roof_end.addChild(inner_roof_left_7_r1);
		setRotationAngle(inner_roof_left_7_r1, 0, 0, 0.5236F);
		inner_roof_left_7_r1.setTextureOffset(0, 123).addCuboid(-4, 0, -24, 4, 0, 40, 0, false);

		inner_roof_left_5_r1 = new ModelMapper(modelPartData);
		inner_roof_left_5_r1.setPivot(-11.6147F, -34.3057F, 0);
		roof_end.addChild(inner_roof_left_5_r1);
		setRotationAngle(inner_roof_left_5_r1, 0, 0, -0.3927F);
		inner_roof_left_5_r1.setTextureOffset(124, 49).addCuboid(-1.5F, 0, -24, 3, 0, 40, 0, false);

		inner_roof_left_4_r1 = new ModelMapper(modelPartData);
		inner_roof_left_4_r1.setPivot(-14, -32, 0);
		roof_end.addChild(inner_roof_left_4_r1);
		setRotationAngle(inner_roof_left_4_r1, 0, 0, -1.0472F);
		inner_roof_left_4_r1.setTextureOffset(150, 35).addCuboid(0, 0, -24, 2, 0, 40, 0, false);

		roof_end_light = new ModelMapper(modelPartData);
		roof_end_light.setPivot(0, 24, 0);


		light_2_r1 = new ModelMapper(modelPartData);
		light_2_r1.setPivot(2, -33, 0);
		roof_end_light.addChild(light_2_r1);
		setRotationAngle(light_2_r1, 0, 0, -0.5236F);
		light_2_r1.setTextureOffset(100, 49).addCuboid(0, -0.1F, -24, 4, 0, 40, 0, false);

		light_1_r1 = new ModelMapper(modelPartData);
		light_1_r1.setPivot(-2, -33, 0);
		roof_end_light.addChild(light_1_r1);
		setRotationAngle(light_1_r1, 0, 0, 0.5236F);
		light_1_r1.setTextureOffset(100, 49).addCuboid(-4, -0.1F, -24, 4, 0, 40, 0, false);

		roof_end_exterior = new ModelMapper(modelPartData);
		roof_end_exterior.setPivot(0, 24, 0);


		outer_roof_1 = new ModelMapper(modelPartData);
		outer_roof_1.setPivot(0, 0, 0);
		roof_end_exterior.addChild(outer_roof_1);
		outer_roof_1.setTextureOffset(0, 69).addCuboid(-6, -42, -36, 6, 1, 20, 0, false);

		upper_wall_1_r3 = new ModelMapper(modelPartData);
		upper_wall_1_r3.setPivot(-20, -13, 0);
		outer_roof_1.addChild(upper_wall_1_r3);
		setRotationAngle(upper_wall_1_r3, 0, 0, 0.1107F);
		upper_wall_1_r3.setTextureOffset(0, 69).addCuboid(0, -23, -36, 1, 4, 7, 0, false);
		upper_wall_1_r3.setTextureOffset(182, 70).addCuboid(-1, -23, -29, 1, 4, 13, 0, false);

		outer_roof_5_r2 = new ModelMapper(modelPartData);
		outer_roof_5_r2.setPivot(-9.7656F, -40.3206F, 0);
		outer_roof_1.addChild(outer_roof_5_r2);
		setRotationAngle(outer_roof_5_r2, 0, 0, -0.1745F);
		outer_roof_5_r2.setTextureOffset(172, 190).addCuboid(-4, -1, -36, 8, 1, 20, 0, false);

		outer_roof_4_r2 = new ModelMapper(modelPartData);
		outer_roof_4_r2.setPivot(-14.6775F, -38.9948F, 0);
		outer_roof_1.addChild(outer_roof_4_r2);
		setRotationAngle(outer_roof_4_r2, 0, 0, -0.5236F);
		outer_roof_4_r2.setTextureOffset(0, 20).addCuboid(-1.5F, -1, -36, 3, 1, 20, 0, false);

		outer_roof_3_r2 = new ModelMapper(modelPartData);
		outer_roof_3_r2.setPivot(-16.1105F, -37.7448F, 0);
		outer_roof_1.addChild(outer_roof_3_r2);
		setRotationAngle(outer_roof_3_r2, 0, 0, -1.0472F);
		outer_roof_3_r2.setTextureOffset(58, 143).addCuboid(-1, -1, -36, 2, 1, 20, 0, false);

		outer_roof_2_r2 = new ModelMapper(modelPartData);
		outer_roof_2_r2.setPivot(-17.587F, -36.3849F, 0);
		outer_roof_1.addChild(outer_roof_2_r2);
		setRotationAngle(outer_roof_2_r2, 0, 0, 0.1107F);
		outer_roof_2_r2.setTextureOffset(116, 143).addCuboid(0, -1, -36, 1, 2, 20, 0, false);

		outer_roof_2 = new ModelMapper(modelPartData);
		outer_roof_2.setPivot(0, 0, 0);
		roof_end_exterior.addChild(outer_roof_2);
		outer_roof_2.setTextureOffset(0, 69).addCuboid(0, -42, -36, 6, 1, 20, 0, true);

		upper_wall_1_r4 = new ModelMapper(modelPartData);
		upper_wall_1_r4.setPivot(20, -13, 0);
		outer_roof_2.addChild(upper_wall_1_r4);
		setRotationAngle(upper_wall_1_r4, 0, 0, -0.1107F);
		upper_wall_1_r4.setTextureOffset(0, 69).addCuboid(-1, -23, -36, 1, 4, 7, 0, true);
		upper_wall_1_r4.setTextureOffset(182, 70).addCuboid(0, -23, -29, 1, 4, 13, 0, true);

		outer_roof_5_r3 = new ModelMapper(modelPartData);
		outer_roof_5_r3.setPivot(9.7656F, -40.3206F, 0);
		outer_roof_2.addChild(outer_roof_5_r3);
		setRotationAngle(outer_roof_5_r3, 0, 0, 0.1745F);
		outer_roof_5_r3.setTextureOffset(172, 190).addCuboid(-4, -1, -36, 8, 1, 20, 0, true);

		outer_roof_4_r3 = new ModelMapper(modelPartData);
		outer_roof_4_r3.setPivot(14.6775F, -38.9948F, 0);
		outer_roof_2.addChild(outer_roof_4_r3);
		setRotationAngle(outer_roof_4_r3, 0, 0, 0.5236F);
		outer_roof_4_r3.setTextureOffset(0, 20).addCuboid(-1.5F, -1, -36, 3, 1, 20, 0, true);

		outer_roof_3_r3 = new ModelMapper(modelPartData);
		outer_roof_3_r3.setPivot(16.1105F, -37.7448F, 0);
		outer_roof_2.addChild(outer_roof_3_r3);
		setRotationAngle(outer_roof_3_r3, 0, 0, 1.0472F);
		outer_roof_3_r3.setTextureOffset(58, 143).addCuboid(-1, -1, -36, 2, 1, 20, 0, true);

		outer_roof_2_r3 = new ModelMapper(modelPartData);
		outer_roof_2_r3.setPivot(17.587F, -36.3849F, 0);
		outer_roof_2.addChild(outer_roof_2_r3);
		setRotationAngle(outer_roof_2_r3, 0, 0, -0.1107F);
		outer_roof_2_r3.setTextureOffset(116, 143).addCuboid(-1, -1, -36, 1, 2, 20, 0, true);

		roof_end_vents = new ModelMapper(modelPartData);
		roof_end_vents.setPivot(0, 24, 0);
		roof_end_vents.setTextureOffset(180, 169).addCuboid(-8, -43, -21, 16, 2, 48, 0, false);

		vent_3_r1 = new ModelMapper(modelPartData);
		vent_3_r1.setPivot(-8, -43, 12);
		roof_end_vents.addChild(vent_3_r1);
		setRotationAngle(vent_3_r1, 0, 0, -0.3491F);
		vent_3_r1.setTextureOffset(0, 210).addCuboid(-9, 0, -33, 9, 2, 48, 0, false);

		vent_2_r1 = new ModelMapper(modelPartData);
		vent_2_r1.setPivot(8, -43, 12);
		roof_end_vents.addChild(vent_2_r1);
		setRotationAngle(vent_2_r1, 0, 0, 0.3491F);
		vent_2_r1.setTextureOffset(0, 210).addCuboid(0, 0, -33, 9, 2, 48, 0, true);

		head = new ModelMapper(modelPartData);
		head.setPivot(0, 24, 0);
		head.setTextureOffset(128, 0).addCuboid(-20, 0, -16, 40, 1, 34, 0, false);
		head.setTextureOffset(116, 298).addCuboid(-20, -13, -18, 2, 13, 36, 0, false);
		head.setTextureOffset(293, 240).addCuboid(18, -13, -18, 2, 13, 36, 0, true);
		head.setTextureOffset(322, 289).addCuboid(-18, -36, 18, 36, 36, 0, 0, false);

		upper_wall_2_r3 = new ModelMapper(modelPartData);
		upper_wall_2_r3.setPivot(20, -13, 0);
		head.addChild(upper_wall_2_r3);
		setRotationAngle(upper_wall_2_r3, 0, 0, -0.1107F);
		upper_wall_2_r3.setTextureOffset(0, 260).addCuboid(-2, -20, -18, 2, 20, 36, 0, true);

		upper_wall_1_r5 = new ModelMapper(modelPartData);
		upper_wall_1_r5.setPivot(-20, -13, 0);
		head.addChild(upper_wall_1_r5);
		setRotationAngle(upper_wall_1_r5, 0, 0, 0.1107F);
		upper_wall_1_r5.setTextureOffset(260, 135).addCuboid(0, -20, -18, 2, 20, 36, 0, false);

		head_exterior = new ModelMapper(modelPartData);
		head_exterior.setPivot(0, 24, 0);
		head_exterior.setTextureOffset(232, 68).addCuboid(-21, 0, 19, 42, 7, 12, 0, false);
		head_exterior.setTextureOffset(305, 52).addCuboid(20, 0, -16, 1, 7, 35, 0, true);
		head_exterior.setTextureOffset(304, 0).addCuboid(-21, 0, -16, 1, 7, 35, 0, false);
		head_exterior.setTextureOffset(281, 289).addCuboid(18, -13, -18, 2, 13, 37, 0, true);
		head_exterior.setTextureOffset(256, 219).addCuboid(20, -13, 19, 1, 13, 12, 0, true);
		head_exterior.setTextureOffset(240, 276).addCuboid(-20, -13, -18, 2, 13, 37, 0, false);
		head_exterior.setTextureOffset(256, 219).addCuboid(-21, -13, 19, 1, 13, 12, 0, false);
		head_exterior.setTextureOffset(0, 316).addCuboid(-18, -42, 19, 36, 42, 0, 0, false);

		driver_door_upper_2_r1 = new ModelMapper(modelPartData);
		driver_door_upper_2_r1.setPivot(-21, -13, 0);
		head_exterior.addChild(driver_door_upper_2_r1);
		setRotationAngle(driver_door_upper_2_r1, 0, 0, 0.1107F);
		driver_door_upper_2_r1.setTextureOffset(232, 102).addCuboid(0, -20, 19, 1, 20, 12, 0, false);

		upper_wall_2_r4 = new ModelMapper(modelPartData);
		upper_wall_2_r4.setPivot(-20, -13, 0);
		head_exterior.addChild(upper_wall_2_r4);
		setRotationAngle(upper_wall_2_r4, 0, 0, 0.1107F);
		upper_wall_2_r4.setTextureOffset(123, 241).addCuboid(0, -20, -18, 2, 20, 37, 0, false);

		driver_door_upper_1_r1 = new ModelMapper(modelPartData);
		driver_door_upper_1_r1.setPivot(21, -13, 0);
		head_exterior.addChild(driver_door_upper_1_r1);
		setRotationAngle(driver_door_upper_1_r1, 0, 0, -0.1107F);
		driver_door_upper_1_r1.setTextureOffset(232, 102).addCuboid(-1, -20, 19, 1, 20, 12, 0, true);

		upper_wall_1_r6 = new ModelMapper(modelPartData);
		upper_wall_1_r6.setPivot(20, -13, 0);
		head_exterior.addChild(upper_wall_1_r6);
		setRotationAngle(upper_wall_1_r6, 0, 0, -0.1107F);
		upper_wall_1_r6.setTextureOffset(251, 219).addCuboid(-2, -20, -18, 2, 20, 37, 0, true);

		front = new ModelMapper(modelPartData);
		front.setPivot(0, 0, 0);
		head_exterior.addChild(front);
		front.setTextureOffset(116, 143).addCuboid(-3, -10, 45, 6, 5, 0, 0, false);
		front.setTextureOffset(228, 0).addCuboid(-21, 0, 31, 42, 0, 14, 0, false);

		front_middle_top_2_r1 = new ModelMapper(modelPartData);
		front_middle_top_2_r1.setPivot(0, -36.1042F, 37.8711F);
		front.addChild(front_middle_top_2_r1);
		setRotationAngle(front_middle_top_2_r1, -0.7854F, 0, 0);
		front_middle_top_2_r1.setTextureOffset(149, 35).addCuboid(-6, 0, -3.5F, 12, 0, 8, 0, false);

		front_middle_top_1_r1 = new ModelMapper(modelPartData);
		front_middle_top_1_r1.setPivot(0, -42, 26);
		front.addChild(front_middle_top_1_r1);
		setRotationAngle(front_middle_top_1_r1, -0.3491F, 0, 0);
		front_middle_top_1_r1.setTextureOffset(20, 90).addCuboid(-6, 0, 0, 12, 0, 10, 0, false);

		front_3_r1 = new ModelMapper(modelPartData);
		front_3_r1.setPivot(0, -5, 45);
		front.addChild(front_3_r1);
		setRotationAngle(front_3_r1, -0.0873F, 0, 0);
		front_3_r1.setTextureOffset(58, 143).addCuboid(-3, 0, 0, 6, 12, 0, 0, false);

		front_1_r1 = new ModelMapper(modelPartData);
		front_1_r1.setPivot(0, -10, 45);
		front.addChild(front_1_r1);
		setRotationAngle(front_1_r1, 0.1745F, 0, 0);
		front_1_r1.setTextureOffset(260, 191).addCuboid(-3, -24, 0, 6, 24, 0, 0, false);

		side_1 = new ModelMapper(modelPartData);
		side_1.setPivot(0, 0, 0);
		front.addChild(side_1);
		side_1.setTextureOffset(201, 277).addCuboid(0, -42, 16, 6, 1, 10, 0, false);

		outer_roof_11_r1 = new ModelMapper(modelPartData);
		outer_roof_11_r1.setPivot(18.7914F, -32.8777F, 31);
		side_1.addChild(outer_roof_11_r1);
		setRotationAngle(outer_roof_11_r1, -1.0472F, 0.9948F, 0);
		outer_roof_11_r1.setTextureOffset(106, 267).addCuboid(-9.0009F, 0.0014F, -6, 11, 0, 10, 0, false);

		outer_roof_10_r1 = new ModelMapper(modelPartData);
		outer_roof_10_r1.setPivot(5.4063F, -38.6327F, 35.3973F);
		side_1.addChild(outer_roof_10_r1);
		setRotationAngle(outer_roof_10_r1, -0.8378F, 0.0873F, 0.2618F);
		outer_roof_10_r1.setTextureOffset(66, 293).addCuboid(-3, 0.001F, -2, 14, 0, 10, 0, false);

		outer_roof_9_r1 = new ModelMapper(modelPartData);
		outer_roof_9_r1.setPivot(6, -42, 26);
		side_1.addChild(outer_roof_9_r1);
		setRotationAngle(outer_roof_9_r1, -0.3491F, 0, 0.1745F);
		outer_roof_9_r1.setTextureOffset(0, 0).addCuboid(0, 0, 0, 8, 0, 10, 0, false);

		outer_roof_8_r1 = new ModelMapper(modelPartData);
		outer_roof_8_r1.setPivot(18.682F, -33.8719F, 31);
		side_1.addChild(outer_roof_8_r1);
		setRotationAngle(outer_roof_8_r1, 0, 0, 1.1345F);
		outer_roof_8_r1.setTextureOffset(145, 89).addCuboid(-6, 0.001F, -5, 6, 0, 5, 0, false);

		outer_roof_7_r1 = new ModelMapper(modelPartData);
		outer_roof_7_r1.setPivot(13.8776F, -40.6102F, 26);
		side_1.addChild(outer_roof_7_r1);
		setRotationAngle(outer_roof_7_r1, -0.3491F, 0, 0.5236F);
		outer_roof_7_r1.setTextureOffset(130, 89).addCuboid(0, 0, 0, 5, 0, 10, 0, false);

		outer_roof_5_r4 = new ModelMapper(modelPartData);
		outer_roof_5_r4.setPivot(9.9394F, -41.3064F, 0);
		side_1.addChild(outer_roof_5_r4);
		setRotationAngle(outer_roof_5_r4, 0, 0, 0.1745F);
		outer_roof_5_r4.setTextureOffset(116, 249).addCuboid(-4, 0, 16, 8, 1, 10, 0, false);

		outer_roof_4_r4 = new ModelMapper(modelPartData);
		outer_roof_4_r4.setPivot(15.1778F, -39.8628F, 0);
		side_1.addChild(outer_roof_4_r4);
		setRotationAngle(outer_roof_4_r4, 0, 0, 0.5236F);
		outer_roof_4_r4.setTextureOffset(156, 309).addCuboid(-1.5F, 0, 16, 3, 1, 10, 0, false);

		outer_roof_3_r4 = new ModelMapper(modelPartData);
		outer_roof_3_r4.setPivot(16.9769F, -38.2468F, 0);
		side_1.addChild(outer_roof_3_r4);
		setRotationAngle(outer_roof_3_r4, 0, 0, 1.0472F);
		outer_roof_3_r4.setTextureOffset(156, 298).addCuboid(-1, 0, 16, 2, 1, 10, 0, false);

		outer_roof_2_r4 = new ModelMapper(modelPartData);
		outer_roof_2_r4.setPivot(17.5872F, -36.3872F, 0);
		side_1.addChild(outer_roof_2_r4);
		setRotationAngle(outer_roof_2_r4, 0, 0, -0.1107F);
		outer_roof_2_r4.setTextureOffset(138, 143).addCuboid(-1, -1, 16, 1, 2, 10, 0, true);

		outer_roof_1_r2 = new ModelMapper(modelPartData);
		outer_roof_1_r2.setPivot(20, -13, 5);
		side_1.addChild(outer_roof_1_r2);
		setRotationAngle(outer_roof_1_r2, 0, 0, -0.1107F);
		outer_roof_1_r2.setTextureOffset(245, 136).addCuboid(-1, -23, 11, 2, 4, 15, 0, true);

		front_side_lower_5_r1 = new ModelMapper(modelPartData);
		front_side_lower_5_r1.setPivot(8.7065F, -8.5F, 43.6502F);
		side_1.addChild(front_side_lower_5_r1);
		setRotationAngle(front_side_lower_5_r1, 0, 0.2182F, 0);
		front_side_lower_5_r1.setTextureOffset(116, 241).addCuboid(-7, -3.5F, 0, 14, 7, 0, 0, false);

		front_side_lower_4_r1 = new ModelMapper(modelPartData);
		front_side_lower_4_r1.setPivot(17.6615F, -8.5F, 40.0141F);
		side_1.addChild(front_side_lower_4_r1);
		setRotationAngle(front_side_lower_4_r1, 0, 0.7854F, 0);
		front_side_lower_4_r1.setTextureOffset(26, 32).addCuboid(-3, -3.5F, 0, 6, 7, 0, 0, false);

		front_side_lower_3_r1 = new ModelMapper(modelPartData);
		front_side_lower_3_r1.setPivot(1.8727F, -5, 45.1663F);
		side_1.addChild(front_side_lower_3_r1);
		setRotationAngle(front_side_lower_3_r1, -0.0873F, 0.2182F, 0);
		front_side_lower_3_r1.setTextureOffset(197, 70).addCuboid(0, 0, -0.001F, 15, 12, 0, 0, false);

		front_side_lower_2_r1 = new ModelMapper(modelPartData);
		front_side_lower_2_r1.setPivot(15.5408F, -5, 42.1361F);
		side_1.addChild(front_side_lower_2_r1);
		setRotationAngle(front_side_lower_2_r1, -0.0436F, 0.7854F, 0);
		front_side_lower_2_r1.setTextureOffset(0, 123).addCuboid(0, 0, -0.001F, 7, 12, 0, 0, false);

		front_side_lower_1_r1 = new ModelMapper(modelPartData);
		front_side_lower_1_r1.setPivot(21, 7, 31);
		side_1.addChild(front_side_lower_1_r1);
		setRotationAngle(front_side_lower_1_r1, 0, -0.1745F, 0);
		front_side_lower_1_r1.setTextureOffset(116, 203).addCuboid(0, -20, 0, 0, 20, 7, 0, false);

		front_side_upper_7_r1 = new ModelMapper(modelPartData);
		front_side_upper_7_r1.setPivot(15.2976F, -12.6432F, 41.454F);
		side_1.addChild(front_side_upper_7_r1);
		setRotationAngle(front_side_upper_7_r1, 0.4102F, 0.2059F, -0.0436F);
		front_side_upper_7_r1.setTextureOffset(58, 172).addCuboid(-13, -1, -0.001F, 13, 4, 0, 0, false);

		front_side_upper_6_r1 = new ModelMapper(modelPartData);
		front_side_upper_6_r1.setPivot(19.1F, -12.95F, 37.7F);
		side_1.addChild(front_side_upper_6_r1);
		setRotationAngle(front_side_upper_6_r1, 0.3491F, 0.7679F, -0.0436F);
		front_side_upper_6_r1.setTextureOffset(28, 43).addCuboid(-6.35F, -0.85F, 0, 7, 3, 0, 0, false);

		front_side_upper_5_r1 = new ModelMapper(modelPartData);
		front_side_upper_5_r1.setPivot(20.4813F, -12.9424F, 33.9542F);
		side_1.addChild(front_side_upper_5_r1);
		setRotationAngle(front_side_upper_5_r1, 0.4363F, 1.2217F, 0);
		front_side_upper_5_r1.setTextureOffset(30, 100).addCuboid(-4.9998F, -0.9997F, -0.0001F, 5, 3, 0, 0, false);

		front_side_upper_4_r1 = new ModelMapper(modelPartData);
		front_side_upper_4_r1.setPivot(3, -10, 45);
		side_1.addChild(front_side_upper_4_r1);
		setRotationAngle(front_side_upper_4_r1, 0.1745F, 0.2443F, 0);
		front_side_upper_4_r1.setTextureOffset(76, 323).addCuboid(0, -24, 0, 14, 24, 0, 0, false);

		front_side_upper_3_r1 = new ModelMapper(modelPartData);
		front_side_upper_3_r1.setPivot(19.1695F, -12.1451F, 37.781F);
		side_1.addChild(front_side_upper_3_r1);
		setRotationAngle(front_side_upper_3_r1, 0.1309F, 0.7679F, 0);
		front_side_upper_3_r1.setTextureOffset(276, 191).addCuboid(-5.9989F, -21.9999F, 0.0149F, 7, 23, 0, 0, false);

		front_side_upper_2_r1 = new ModelMapper(modelPartData);
		front_side_upper_2_r1.setPivot(19.156F, -22.9499F, 34.5398F);
		side_1.addChild(front_side_upper_2_r1);
		setRotationAngle(front_side_upper_2_r1, 0.0873F, -0.3491F, -0.1107F);
		front_side_upper_2_r1.setTextureOffset(136, 206).addCuboid(0, -9, -1.5F, 0, 20, 4, 0, false);

		front_side_upper_1_r1 = new ModelMapper(modelPartData);
		front_side_upper_1_r1.setPivot(21, -13, 31);
		side_1.addChild(front_side_upper_1_r1);
		setRotationAngle(front_side_upper_1_r1, 0, -0.1745F, -0.1107F);
		front_side_upper_1_r1.setTextureOffset(130, 207).addCuboid(0, -20, 0, 0, 20, 3, 0, false);

		side_2 = new ModelMapper(modelPartData);
		side_2.setPivot(0, 0, 0);
		front.addChild(side_2);
		side_2.setTextureOffset(201, 277).addCuboid(-6, -42, 16, 6, 1, 10, 0, true);

		outer_roof_11_r2 = new ModelMapper(modelPartData);
		outer_roof_11_r2.setPivot(-18.7914F, -32.8777F, 31);
		side_2.addChild(outer_roof_11_r2);
		setRotationAngle(outer_roof_11_r2, -1.0472F, -0.9948F, 0);
		outer_roof_11_r2.setTextureOffset(106, 267).addCuboid(-1.9991F, 0.0014F, -6, 11, 0, 10, 0, true);

		outer_roof_10_r2 = new ModelMapper(modelPartData);
		outer_roof_10_r2.setPivot(-5.4063F, -38.6327F, 35.3973F);
		side_2.addChild(outer_roof_10_r2);
		setRotationAngle(outer_roof_10_r2, -0.8378F, -0.0873F, -0.2618F);
		outer_roof_10_r2.setTextureOffset(66, 293).addCuboid(-11, 0.001F, -2, 14, 0, 10, 0, true);

		outer_roof_9_r2 = new ModelMapper(modelPartData);
		outer_roof_9_r2.setPivot(-6, -42, 26);
		side_2.addChild(outer_roof_9_r2);
		setRotationAngle(outer_roof_9_r2, -0.3491F, 0, -0.1745F);
		outer_roof_9_r2.setTextureOffset(0, 0).addCuboid(-8, 0, 0, 8, 0, 10, 0, true);

		outer_roof_8_r2 = new ModelMapper(modelPartData);
		outer_roof_8_r2.setPivot(-18.682F, -33.8719F, 31);
		side_2.addChild(outer_roof_8_r2);
		setRotationAngle(outer_roof_8_r2, 0, 0, -1.1345F);
		outer_roof_8_r2.setTextureOffset(145, 89).addCuboid(0, 0.001F, -5, 6, 0, 5, 0, true);

		outer_roof_7_r2 = new ModelMapper(modelPartData);
		outer_roof_7_r2.setPivot(-13.8776F, -40.6102F, 26);
		side_2.addChild(outer_roof_7_r2);
		setRotationAngle(outer_roof_7_r2, -0.3491F, 0, -0.5236F);
		outer_roof_7_r2.setTextureOffset(130, 89).addCuboid(-5, 0, 0, 5, 0, 10, 0, true);

		outer_roof_5_r5 = new ModelMapper(modelPartData);
		outer_roof_5_r5.setPivot(-9.9394F, -41.3064F, 0);
		side_2.addChild(outer_roof_5_r5);
		setRotationAngle(outer_roof_5_r5, 0, 0, -0.1745F);
		outer_roof_5_r5.setTextureOffset(116, 249).addCuboid(-4, 0, 16, 8, 1, 10, 0, true);

		outer_roof_4_r5 = new ModelMapper(modelPartData);
		outer_roof_4_r5.setPivot(-15.1778F, -39.8628F, 0);
		side_2.addChild(outer_roof_4_r5);
		setRotationAngle(outer_roof_4_r5, 0, 0, -0.5236F);
		outer_roof_4_r5.setTextureOffset(156, 309).addCuboid(-1.5F, 0, 16, 3, 1, 10, 0, true);

		outer_roof_3_r5 = new ModelMapper(modelPartData);
		outer_roof_3_r5.setPivot(-16.9769F, -38.2468F, 0);
		side_2.addChild(outer_roof_3_r5);
		setRotationAngle(outer_roof_3_r5, 0, 0, -1.0472F);
		outer_roof_3_r5.setTextureOffset(156, 298).addCuboid(-1, 0, 16, 2, 1, 10, 0, true);

		outer_roof_2_r5 = new ModelMapper(modelPartData);
		outer_roof_2_r5.setPivot(-17.5872F, -36.3872F, 0);
		side_2.addChild(outer_roof_2_r5);
		setRotationAngle(outer_roof_2_r5, 0, 0, 0.1107F);
		outer_roof_2_r5.setTextureOffset(138, 143).addCuboid(0, -1, 16, 1, 2, 10, 0, false);

		outer_roof_1_r3 = new ModelMapper(modelPartData);
		outer_roof_1_r3.setPivot(-20, -13, 5);
		side_2.addChild(outer_roof_1_r3);
		setRotationAngle(outer_roof_1_r3, 0, 0, 0.1107F);
		outer_roof_1_r3.setTextureOffset(245, 136).addCuboid(-1, -23, 11, 2, 4, 15, 0, false);

		front_side_lower_6_r1 = new ModelMapper(modelPartData);
		front_side_lower_6_r1.setPivot(-8.7065F, -8.5F, 43.6502F);
		side_2.addChild(front_side_lower_6_r1);
		setRotationAngle(front_side_lower_6_r1, 0, -0.2182F, 0);
		front_side_lower_6_r1.setTextureOffset(116, 241).addCuboid(-7, -3.5F, 0, 14, 7, 0, 0, true);

		front_side_lower_5_r2 = new ModelMapper(modelPartData);
		front_side_lower_5_r2.setPivot(-17.6615F, -8.5F, 40.0141F);
		side_2.addChild(front_side_lower_5_r2);
		setRotationAngle(front_side_lower_5_r2, 0, -0.7854F, 0);
		front_side_lower_5_r2.setTextureOffset(26, 32).addCuboid(-3, -3.5F, 0, 6, 7, 0, 0, true);

		front_side_lower_4_r2 = new ModelMapper(modelPartData);
		front_side_lower_4_r2.setPivot(-1.8727F, -5, 45.1663F);
		side_2.addChild(front_side_lower_4_r2);
		setRotationAngle(front_side_lower_4_r2, -0.0873F, -0.2182F, 0);
		front_side_lower_4_r2.setTextureOffset(197, 70).addCuboid(-15, 0, -0.001F, 15, 12, 0, 0, true);

		front_side_lower_3_r2 = new ModelMapper(modelPartData);
		front_side_lower_3_r2.setPivot(-15.5408F, -5, 42.1361F);
		side_2.addChild(front_side_lower_3_r2);
		setRotationAngle(front_side_lower_3_r2, -0.0436F, -0.7854F, 0);
		front_side_lower_3_r2.setTextureOffset(0, 123).addCuboid(-7, 0, -0.001F, 7, 12, 0, 0, true);

		front_side_lower_2_r2 = new ModelMapper(modelPartData);
		front_side_lower_2_r2.setPivot(-21, 7, 31);
		side_2.addChild(front_side_lower_2_r2);
		setRotationAngle(front_side_lower_2_r2, 0, 0.1745F, 0);
		front_side_lower_2_r2.setTextureOffset(116, 203).addCuboid(0, -20, 0, 0, 20, 7, 0, true);

		front_side_upper_8_r1 = new ModelMapper(modelPartData);
		front_side_upper_8_r1.setPivot(-15.2976F, -12.6432F, 41.454F);
		side_2.addChild(front_side_upper_8_r1);
		setRotationAngle(front_side_upper_8_r1, 0.4102F, -0.2059F, 0.0436F);
		front_side_upper_8_r1.setTextureOffset(58, 172).addCuboid(0, -1, -0.001F, 13, 4, 0, 0, true);

		front_side_upper_7_r2 = new ModelMapper(modelPartData);
		front_side_upper_7_r2.setPivot(-19.1F, -12.95F, 37.7F);
		side_2.addChild(front_side_upper_7_r2);
		setRotationAngle(front_side_upper_7_r2, 0.3491F, -0.7679F, 0.0436F);
		front_side_upper_7_r2.setTextureOffset(28, 43).addCuboid(-0.65F, -0.85F, 0, 7, 3, 0, 0, true);

		front_side_upper_6_r2 = new ModelMapper(modelPartData);
		front_side_upper_6_r2.setPivot(-20.4813F, -12.9424F, 33.9542F);
		side_2.addChild(front_side_upper_6_r2);
		setRotationAngle(front_side_upper_6_r2, 0.4363F, -1.2217F, 0);
		front_side_upper_6_r2.setTextureOffset(30, 100).addCuboid(-0.0002F, -0.9997F, -0.0001F, 5, 3, 0, 0, true);

		front_side_upper_5_r2 = new ModelMapper(modelPartData);
		front_side_upper_5_r2.setPivot(-3, -10, 45);
		side_2.addChild(front_side_upper_5_r2);
		setRotationAngle(front_side_upper_5_r2, 0.1745F, -0.2443F, 0);
		front_side_upper_5_r2.setTextureOffset(76, 323).addCuboid(-14, -24, 0, 14, 24, 0, 0, true);

		front_side_upper_4_r2 = new ModelMapper(modelPartData);
		front_side_upper_4_r2.setPivot(-19.1695F, -12.1451F, 37.781F);
		side_2.addChild(front_side_upper_4_r2);
		setRotationAngle(front_side_upper_4_r2, 0.1309F, -0.7679F, 0);
		front_side_upper_4_r2.setTextureOffset(276, 191).addCuboid(-1.0011F, -21.9999F, 0.0149F, 7, 23, 0, 0, true);

		front_side_upper_3_r2 = new ModelMapper(modelPartData);
		front_side_upper_3_r2.setPivot(-19.156F, -22.9499F, 34.5398F);
		side_2.addChild(front_side_upper_3_r2);
		setRotationAngle(front_side_upper_3_r2, 0.0873F, 0.3491F, 0.1107F);
		front_side_upper_3_r2.setTextureOffset(136, 205).addCuboid(0, -10, -1.5F, 0, 21, 4, 0, true);

		front_side_upper_2_r2 = new ModelMapper(modelPartData);
		front_side_upper_2_r2.setPivot(-21, -13, 31);
		side_2.addChild(front_side_upper_2_r2);
		setRotationAngle(front_side_upper_2_r2, 0, 0.1745F, 0.1107F);
		front_side_upper_2_r2.setTextureOffset(130, 207).addCuboid(0, -20, 0, 0, 20, 3, 0, true);

		headlights = new ModelMapper(modelPartData);
		headlights.setPivot(0, 24, 0);


		headlight_4_r1 = new ModelMapper(modelPartData);
		headlight_4_r1.setPivot(-14.0978F, -7, 42.5574F);
		headlights.addChild(headlight_4_r1);
		setRotationAngle(headlight_4_r1, 0, -0.2182F, 0);
		headlight_4_r1.setTextureOffset(39, 0).addCuboid(-1.5F, -2, 0, 3, 4, 0, 0, true);

		headlight_3_r1 = new ModelMapper(modelPartData);
		headlight_3_r1.setPivot(-16.976F, -7, 40.8188F);
		headlights.addChild(headlight_3_r1);
		setRotationAngle(headlight_3_r1, 0, -0.7854F, 0);
		headlight_3_r1.setTextureOffset(31, 0).addCuboid(-2, -2, 0, 4, 4, 0, 0, true);

		headlight_2_r1 = new ModelMapper(modelPartData);
		headlight_2_r1.setPivot(14.0978F, -7, 42.5574F);
		headlights.addChild(headlight_2_r1);
		setRotationAngle(headlight_2_r1, 0, 0.2182F, 0);
		headlight_2_r1.setTextureOffset(39, 0).addCuboid(-1.5F, -2, 0, 3, 4, 0, 0, false);

		headlight_1_r1 = new ModelMapper(modelPartData);
		headlight_1_r1.setPivot(16.976F, -7, 40.8188F);
		headlights.addChild(headlight_1_r1);
		setRotationAngle(headlight_1_r1, 0, 0.7854F, 0);
		headlight_1_r1.setTextureOffset(31, 0).addCuboid(-2, -2, 0, 4, 4, 0, 0, false);

		tail_lights = new ModelMapper(modelPartData);
		tail_lights.setPivot(0, 24, 0);


		tail_light_2_r1 = new ModelMapper(modelPartData);
		tail_light_2_r1.setPivot(-14.0978F, -7, 42.5574F);
		tail_lights.addChild(tail_light_2_r1);
		setRotationAngle(tail_light_2_r1, 0, -0.2182F, 0);
		tail_light_2_r1.setTextureOffset(31, 4).addCuboid(0.5F, -2, 0, 5, 4, 0, 0, true);

		tail_light_1_r1 = new ModelMapper(modelPartData);
		tail_light_1_r1.setPivot(14.0978F, -7, 42.5574F);
		tail_lights.addChild(tail_light_1_r1);
		setRotationAngle(tail_light_1_r1, 0, 0.2182F, 0);
		tail_light_1_r1.setTextureOffset(31, 4).addCuboid(-5.5F, -2, 0, 5, 4, 0, 0, false);

		door_light = new ModelMapper(modelPartData);
		door_light.setPivot(0, 24, 0);


		outer_roof_1_r4 = new ModelMapper(modelPartData);
		outer_roof_1_r4.setPivot(-20, -13, 0);
		door_light.addChild(outer_roof_1_r4);
		setRotationAngle(outer_roof_1_r4, 0, 0, 0.1107F);
		outer_roof_1_r4.setTextureOffset(58, 85).addCuboid(-1.1F, -23, -2, 0, 4, 4, 0, false);

		door_light_on = new ModelMapper(modelPartData);
		door_light_on.setPivot(0, 24, 0);


		light_r2 = new ModelMapper(modelPartData);
		light_r2.setPivot(-20, -13, 0);
		door_light_on.addChild(light_r2);
		setRotationAngle(light_r2, 0, 0, 0.1107F);
		light_r2.setTextureOffset(60, 94).addCuboid(-1, -21, 0, 0, 0, 0, 0.4F, false);

		door_light_off = new ModelMapper(modelPartData);
		door_light_off.setPivot(0, 24, 0);


		light_r3 = new ModelMapper(modelPartData);
		light_r3.setPivot(-20, -13, 0);
		door_light_off.addChild(light_r3);
		setRotationAngle(light_r3, 0, 0, 0.1107F);
		light_r3.setTextureOffset(60, 96).addCuboid(-1, -21, 0, 0, 0, 0, 0.4F, false);

		final ModelPart modelPart = TexturedModelData.of(modelData, textureWidth, textureHeight).createModel();
		window_1.setModelPart(modelPart);
		window_2.setModelPart(modelPart);
		window_handrails.setModelPart(modelPart);
		seats.setModelPart(modelPart);
		window_1_tv.setModelPart(modelPart);
		window_exterior_1.setModelPart(modelPart);
		window_exterior_2.setModelPart(modelPart);
		side_panel.setModelPart(modelPart);
		roof_window.setModelPart(modelPart);
		roof_door.setModelPart(modelPart);
		roof_light.setModelPart(modelPart);
		roof_exterior.setModelPart(modelPart);
		door.setModelPart(modelPart);
		door_left.setModelPart(modelPart.getChild(door.name));
		door_right.setModelPart(modelPart.getChild(door.name));
		door_handrails.setModelPart(modelPart);
		door_exterior.setModelPart(modelPart);
		door_left_exterior.setModelPart(modelPart.getChild(door_exterior.name));
		door_right_exterior.setModelPart(modelPart.getChild(door_exterior.name));
		end.setModelPart(modelPart);
		end_exterior.setModelPart(modelPart);
		roof_end.setModelPart(modelPart);
		roof_end_light.setModelPart(modelPart);
		roof_end_exterior.setModelPart(modelPart);
		roof_end_vents.setModelPart(modelPart);
		head.setModelPart(modelPart);
		head_exterior.setModelPart(modelPart);
		headlights.setModelPart(modelPart);
		tail_lights.setModelPart(modelPart);
		door_light.setModelPart(modelPart);
		door_light_on.setModelPart(modelPart);
		door_light_off.setModelPart(modelPart);
	}

	private static final int DOOR_MAX = 14;
	private static final ModelDoorOverlay MODEL_DOOR_OVERLAY = new ModelDoorOverlay(DOOR_MAX, 6.34F, 13, "door_overlay_mlr_left.png", "door_overlay_mlr_right.png");
	private static final ModelDoorOverlayTopMLR MODEL_DOOR_OVERLAY_TOP = new ModelDoorOverlayTopMLR();

	@Override
	protected void renderWindowPositions(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, boolean isEnd1Head, boolean isEnd2Head) {
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
				}
				break;
			case INTERIOR_TRANSLUCENT:
				renderMirror(side_panel, matrices, vertices, light, position - (isEvenWindow ? 15.75F : 14.25F));
				renderMirror(side_panel, matrices, vertices, light, position + (isEvenWindow ? 14.25F : 15.75F));
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
	protected void renderDoorPositions(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
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
	protected void renderHeadPosition1(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, boolean useHeadlights) {
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
	protected void renderHeadPosition2(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, boolean useHeadlights) {
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
	protected void renderEndPosition1(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails) {
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
	protected void renderEndPosition2(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails) {
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