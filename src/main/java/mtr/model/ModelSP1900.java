package mtr.model;

import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class ModelSP1900 extends ModelTrainBase {

	private final ModelMapper window;
	private final ModelMapper upper_wall_r1;
	private final ModelMapper seat;
	private final ModelMapper seat_back_c1141a_r1;
	private final ModelMapper window_exterior_1;
	private final ModelMapper upper_wall_r2;
	private final ModelMapper window_exterior_2;
	private final ModelMapper upper_wall_r3;
	private final ModelMapper side_panel_sp1900;
	private final ModelMapper handrail_7_r1;
	private final ModelMapper handrail_6_r1;
	private final ModelMapper handrail_4_r1;
	private final ModelMapper handrail_3_r1;
	private final ModelMapper handrail_2_r1;
	private final ModelMapper side_panel_sp1900_translucent;
	private final ModelMapper side_panel_c1141a;
	private final ModelMapper handrail_5_r1;
	private final ModelMapper handrail_3_r2;
	private final ModelMapper side_panel_c1141a_translucent;
	private final ModelMapper door;
	private final ModelMapper door_left;
	private final ModelMapper door_left_top_r1;
	private final ModelMapper door_right;
	private final ModelMapper door_right_top_r1;
	private final ModelMapper door_light_c1141a;
	private final ModelMapper door_exterior_1;
	private final ModelMapper door_right_exterior_1;
	private final ModelMapper door_left_top_r2;
	private final ModelMapper door_left_exterior_1;
	private final ModelMapper door_right_top_r2_r1;
	private final ModelMapper door_exterior_2;
	private final ModelMapper door_left_exterior_2;
	private final ModelMapper door_left_top_r3;
	private final ModelMapper door_right_exterior_2;
	private final ModelMapper door_right_top_r3_r1;
	private final ModelMapper end;
	private final ModelMapper upper_wall_2_r1;
	private final ModelMapper upper_wall_1_r1;
	private final ModelMapper seat_end_1;
	private final ModelMapper seat_back_1_c1141a_r1;
	private final ModelMapper seat_end_2;
	private final ModelMapper seat_back_2_c1141a_r1;
	private final ModelMapper seat_bottom_2_r1;
	private final ModelMapper end_exterior;
	private final ModelMapper outer_roof_2_r1;
	private final ModelMapper outer_roof_1_r1;
	private final ModelMapper floor_1_r1;
	private final ModelMapper roof_sp1900;
	private final ModelMapper inner_roof_6_r1;
	private final ModelMapper inner_roof_5_r1;
	private final ModelMapper inner_roof_4_r1;
	private final ModelMapper inner_roof_3_r1;
	private final ModelMapper inner_roof_2_r1;
	private final ModelMapper roof_c1141a;
	private final ModelMapper inner_roof_3_r2;
	private final ModelMapper roof_exterior;
	private final ModelMapper outer_roof_4_r1;
	private final ModelMapper outer_roof_3_r1;
	private final ModelMapper outer_roof_2_r2;
	private final ModelMapper outer_roof_1_r2;
	private final ModelMapper roof_light_sp1900;
	private final ModelMapper light_3_r1;
	private final ModelMapper light_1_r1;
	private final ModelMapper roof_light_c1141a;
	private final ModelMapper roof_end_sp1900;
	private final ModelMapper inner_roof_1;
	private final ModelMapper inner_roof_6_r2;
	private final ModelMapper inner_roof_5_r2;
	private final ModelMapper inner_roof_4_r2;
	private final ModelMapper inner_roof_3_r3;
	private final ModelMapper inner_roof_2_r2;
	private final ModelMapper inner_roof_2;
	private final ModelMapper inner_roof_6_r3;
	private final ModelMapper inner_roof_5_r3;
	private final ModelMapper inner_roof_4_r3;
	private final ModelMapper inner_roof_3_r4;
	private final ModelMapper inner_roof_2_r3;
	private final ModelMapper roof_end_c1141a;
	private final ModelMapper inner_roof_3;
	private final ModelMapper inner_roof_3_r5;
	private final ModelMapper inner_roof_4;
	private final ModelMapper inner_roof_3_r6;
	private final ModelMapper roof_end_exterior;
	private final ModelMapper vent_2_r1;
	private final ModelMapper vent_1_r1;
	private final ModelMapper outer_roof_1;
	private final ModelMapper outer_roof_3_r2;
	private final ModelMapper outer_roof_2_r3;
	private final ModelMapper outer_roof_2;
	private final ModelMapper outer_roof_3_r3;
	private final ModelMapper outer_roof_2_r4;
	private final ModelMapper roof_end_light_sp1900;
	private final ModelMapper light_6_r1;
	private final ModelMapper light_4_r1;
	private final ModelMapper light_3_r2;
	private final ModelMapper light_1_r2;
	private final ModelMapper roof_end_light_c1141a;
	private final ModelMapper top_handrail_sp1900;
	private final ModelMapper top_handrail_bottom_right_r1;
	private final ModelMapper top_handrail_bottom_left_r1;
	private final ModelMapper top_handrail_right_3_r1;
	private final ModelMapper top_handrail_right_2_r1;
	private final ModelMapper top_handrail_left_3_r1;
	private final ModelMapper top_handrail_left_2_r1;
	private final ModelMapper handrail_strap_1;
	private final ModelMapper top_handrail_c1141a;
	private final ModelMapper pole_bottom_diagonal_2_r1;
	private final ModelMapper pole_bottom_diagonal_1_r1;
	private final ModelMapper pole_top_diagonal_2_r1;
	private final ModelMapper pole_top_diagonal_1_r1;
	private final ModelMapper top_handrail_connector_bottom_4_r1;
	private final ModelMapper top_handrail_connector_bottom_3_r1;
	private final ModelMapper top_handrail_bottom_right_r2;
	private final ModelMapper top_handrail_bottom_left_r2;
	private final ModelMapper top_handrail_right_4_r1;
	private final ModelMapper top_handrail_right_3_r2;
	private final ModelMapper top_handrail_left_4_r1;
	private final ModelMapper top_handrail_left_3_r2;
	private final ModelMapper handrail_strap_2;
	private final ModelMapper handrail_strap_8_r1;
	private final ModelMapper tv_pole;
	private final ModelMapper tv_right_r1;
	private final ModelMapper tv_left_r1;
	private final ModelMapper pole_5_r1;
	private final ModelMapper pole_4_r1;
	private final ModelMapper pole_2_r1;
	private final ModelMapper pole_1_r1;
	private final ModelMapper head;
	private final ModelMapper upper_wall_2_r2;
	private final ModelMapper upper_wall_1_r2;
	private final ModelMapper seat_head_1;
	private final ModelMapper seat_back_c1141a_r2;
	private final ModelMapper seat_head_2;
	private final ModelMapper seat_back_c1141a_r3;
	private final ModelMapper seat_bottom_r1;
	private final ModelMapper head_exterior;
	private final ModelMapper outer_roof_1_r3;
	private final ModelMapper outer_roof_2_r5;
	private final ModelMapper driver_door_top_2_r1;
	private final ModelMapper driver_door_top_1_r1;
	private final ModelMapper floor_2_r1;
	private final ModelMapper front;
	private final ModelMapper front_middle_r1_r1;
	private final ModelMapper front_roof_r1_r1;
	private final ModelMapper bottom_r1_r1;
	private final ModelMapper front_bottom_r1_r1;
	private final ModelMapper front_side_1_r1_r1;
	private final ModelMapper front_side_2_r1_r1;
	private final ModelMapper bottom_side_1_r1_r1;
	private final ModelMapper bottom_side_2_r1_r1;
	private final ModelMapper top_side_1_r1_r1;
	private final ModelMapper top_side_2_r1_r1;
	private final ModelMapper roof_side_1_r1_r1;
	private final ModelMapper roof_side_2_r1_r1;
	private final ModelMapper roof_middle_corner_1_r1_r1;
	private final ModelMapper roof_middle_corner_2_r1_r1;
	private final ModelMapper roof_corner_1_r1_r1;
	private final ModelMapper roof_corner_2_r1_r1;
	private final ModelMapper bottom_corner_1_r1_r1;
	private final ModelMapper bottom_corner_2_r1_r1;
	private final ModelMapper top_handrail_head_sp1900;
	private final ModelMapper top_handrail_bottom_left_r3;
	private final ModelMapper top_handrail_bottom_right_r3;
	private final ModelMapper top_handrail_right_3_r3;
	private final ModelMapper top_handrail_right_2_r2;
	private final ModelMapper top_handrail_left_3_r3;
	private final ModelMapper top_handrail_left_2_r2;
	private final ModelMapper handrail_strap_head;
	private final ModelMapper top_handrail_head_c1141a;
	private final ModelMapper pole_bottom_diagonal_3_r1;
	private final ModelMapper pole_bottom_diagonal_2_r2;
	private final ModelMapper pole_top_diagonal_3_r1;
	private final ModelMapper pole_top_diagonal_2_r2;
	private final ModelMapper top_handrail_connector_bottom_5_r1;
	private final ModelMapper top_handrail_connector_bottom_right_4_r1;
	private final ModelMapper top_handrail_connector_bottom_left_4_r1;
	private final ModelMapper top_handrail_connector_bottom_right_3_r1;
	private final ModelMapper top_handrail_connector_bottom_left_3_r1;
	private final ModelMapper top_handrail_bottom_right_r4;
	private final ModelMapper top_handrail_right_5_r1;
	private final ModelMapper top_handrail_right_4_r2;
	private final ModelMapper top_handrail_left_5_r1;
	private final ModelMapper top_handrail_left_4_r2;
	private final ModelMapper handrail_strap_3;
	private final ModelMapper handrail_strap_right_8_r1;
	private final ModelMapper handrail_strap_left_8_r1;
	private final ModelMapper headlights;
	private final ModelMapper headlight_4_r1_r1;
	private final ModelMapper headlight_3_r1_r1;
	private final ModelMapper headlight_1_r1_r1;
	private final ModelMapper tail_lights;
	private final ModelMapper tail_light_4_r1_r1;
	private final ModelMapper tail_light_3_r1_r1;
	private final ModelMapper tail_light_1_r1_r1;
	private final ModelMapper door_light_on;
	private final ModelMapper light_r1;
	private final ModelMapper door_light_off;
	private final ModelMapper light_r2;
	private final ModelMapper bb_main;

	private final boolean isC1141A;

	public ModelSP1900(boolean isC1141A) {
		this.isC1141A = isC1141A;

		final int textureWidth = 416;
		final int textureHeight = 416;

		final ModelData modelData = new ModelData();
		final ModelPartData modelPartData = modelData.getRoot();

		window = new ModelMapper(modelPartData);
		window.setPivot(0, 24, 0);
		window.setTextureOffset(58, 115).addCuboid(-20, 0, -16, 20, 1, 32, 0, false);
		window.setTextureOffset(326, 119).addCuboid(-20, -14, -18, 2, 14, 36, 0, false);

		upper_wall_r1 = new ModelMapper(modelPartData);
		upper_wall_r1.setPivot(-20, -14, 0);
		window.addChild(upper_wall_r1);
		setRotationAngle(upper_wall_r1, 0, 0, 0.1107F);
		upper_wall_r1.setTextureOffset(134, 311).addCuboid(0, -19, -18, 2, 19, 36, 0, false);

		seat = new ModelMapper(modelPartData);
		seat.setPivot(-9, 0, 0);
		window.addChild(seat);
		seat.setTextureOffset(228, 192).addCuboid(-9, -6, -16, 7, 1, 32, 0, false);

		seat_back_c1141a_r1 = new ModelMapper(modelPartData);
		seat_back_c1141a_r1.setPivot(-9, -6.5F, 0);
		seat.addChild(seat_back_c1141a_r1);
		setRotationAngle(seat_back_c1141a_r1, 0, 0, -0.0873F);
		seat_back_c1141a_r1.setTextureOffset(254, 71).addCuboid(0, -6, -12, 1, 4, 24, 0, false);
		seat_back_c1141a_r1.setTextureOffset(212, 351).addCuboid(0, -8, -16, 1, 8, 32, 0, false);

		window_exterior_1 = new ModelMapper(modelPartData);
		window_exterior_1.setPivot(0, 24, 0);
		window_exterior_1.setTextureOffset(324, 343).addCuboid(20, 0, -16, 1, 7, 32, 0, true);
		window_exterior_1.setTextureOffset(128, 46).addCuboid(20, -14, -18, 0, 14, 36, 0, true);

		upper_wall_r2 = new ModelMapper(modelPartData);
		upper_wall_r2.setPivot(20, -14, 0);
		window_exterior_1.addChild(upper_wall_r2);
		setRotationAngle(upper_wall_r2, 0, 0, -0.1107F);
		upper_wall_r2.setTextureOffset(58, 112).addCuboid(0, -19, -18, 0, 19, 36, 0, true);

		window_exterior_2 = new ModelMapper(modelPartData);
		window_exterior_2.setPivot(0, 24, 0);
		window_exterior_2.setTextureOffset(324, 343).addCuboid(-21, 0, -16, 1, 7, 32, 0, false);
		window_exterior_2.setTextureOffset(128, 46).addCuboid(-20, -14, -18, 0, 14, 36, 0, false);

		upper_wall_r3 = new ModelMapper(modelPartData);
		upper_wall_r3.setPivot(-20, -14, 0);
		window_exterior_2.addChild(upper_wall_r3);
		setRotationAngle(upper_wall_r3, 0, 0, 0.1107F);
		upper_wall_r3.setTextureOffset(58, 112).addCuboid(0, -19, -18, 0, 19, 36, 0, false);

		side_panel_sp1900 = new ModelMapper(modelPartData);
		side_panel_sp1900.setPivot(0, 24, 0);
		side_panel_sp1900.setTextureOffset(38, 188).addCuboid(-18, -34, 0, 7, 30, 0, 0, false);
		side_panel_sp1900.setTextureOffset(12, 18).addCuboid(-12, -16, 0, 0, 10, 0, 0.2F, false);
		side_panel_sp1900.setTextureOffset(12, 18).addCuboid(-10.3698F, -26.2455F, 0, 0, 3, 0, 0.2F, false);

		handrail_7_r1 = new ModelMapper(modelPartData);
		handrail_7_r1.setPivot(-11.8689F, -31.7477F, 0);
		side_panel_sp1900.addChild(handrail_7_r1);
		setRotationAngle(handrail_7_r1, 0, 0, -0.3491F);
		handrail_7_r1.setTextureOffset(12, 18).addCuboid(0, -3, 0, 0, 6, 0, 0.2F, false);

		handrail_6_r1 = new ModelMapper(modelPartData);
		handrail_6_r1.setPivot(-10.5751F, -27.5926F, 0);
		side_panel_sp1900.addChild(handrail_6_r1);
		setRotationAngle(handrail_6_r1, 0, 0, -0.1745F);
		handrail_6_r1.setTextureOffset(12, 18).addCuboid(0, -1, 0, 0, 2, 0, 0.2F, false);

		handrail_4_r1 = new ModelMapper(modelPartData);
		handrail_4_r1.setPivot(-10.5751F, -21.8985F, 0);
		side_panel_sp1900.addChild(handrail_4_r1);
		setRotationAngle(handrail_4_r1, 0, 0, 0.1745F);
		handrail_4_r1.setTextureOffset(12, 18).addCuboid(0, -1, 0, 0, 2, 0, 0.2F, false);

		handrail_3_r1 = new ModelMapper(modelPartData);
		handrail_3_r1.setPivot(-11.1849F, -19.6228F, 0);
		side_panel_sp1900.addChild(handrail_3_r1);
		setRotationAngle(handrail_3_r1, 0, 0, 0.3491F);
		handrail_3_r1.setTextureOffset(12, 18).addCuboid(0, -1, 0, 0, 2, 0, 0.2F, false);

		handrail_2_r1 = new ModelMapper(modelPartData);
		handrail_2_r1.setPivot(-11.7947F, -17.347F, 0);
		side_panel_sp1900.addChild(handrail_2_r1);
		setRotationAngle(handrail_2_r1, 0, 0, 0.1745F);
		handrail_2_r1.setTextureOffset(12, 18).addCuboid(0, -1, 0, 0, 2, 0, 0.2F, false);

		side_panel_sp1900_translucent = new ModelMapper(modelPartData);
		side_panel_sp1900_translucent.setPivot(0, 24, 0);
		side_panel_sp1900_translucent.setTextureOffset(34, 289).addCuboid(-18, -34, 0, 7, 30, 0, 0, false);

		side_panel_c1141a = new ModelMapper(modelPartData);
		side_panel_c1141a.setPivot(0, 24, 0);
		side_panel_c1141a.setTextureOffset(38, 218).addCuboid(-18, -29, 0, 7, 24, 0, 0, false);
		side_panel_c1141a.setTextureOffset(8, 4).addCuboid(-11, -28, 0, 0, 23, 0, 0.2F, false);
		side_panel_c1141a.setTextureOffset(224, 31).addCuboid(-18.7F, -12.5F, -0.5F, 8, 4, 1, 0, false);

		handrail_5_r1 = new ModelMapper(modelPartData);
		handrail_5_r1.setPivot(-14.4899F, -28.9899F, 0);
		side_panel_c1141a.addChild(handrail_5_r1);
		setRotationAngle(handrail_5_r1, 0, 0, 1.5708F);
		handrail_5_r1.setTextureOffset(8, 24).addCuboid(0, -2.5F, 0, 0, 5, 0, 0.2F, false);

		handrail_3_r2 = new ModelMapper(modelPartData);
		handrail_3_r2.setPivot(-10.0929F, -27.4929F, 0.2F);
		side_panel_c1141a.addChild(handrail_3_r2);
		setRotationAngle(handrail_3_r2, 0, 0, -0.7854F);
		handrail_3_r2.setTextureOffset(8, 24).addCuboid(-0.2F, -2.2F, -0.2F, 0, 1, 0, 0.2F, false);

		side_panel_c1141a_translucent = new ModelMapper(modelPartData);
		side_panel_c1141a_translucent.setPivot(0, 24, 0);
		side_panel_c1141a_translucent.setTextureOffset(34, 295).addCuboid(-18, -29, 0, 7, 24, 0, 0, false);

		door = new ModelMapper(modelPartData);
		door.setPivot(0, 24, 0);
		door.setTextureOffset(128, 49).addCuboid(-20, 0, -16, 20, 1, 32, 0, false);
		door.setTextureOffset(128, 66).addCuboid(-5, -36, -5, 5, 1, 10, 0, false);

		door_left = new ModelMapper(modelPartData);
		door_left.setPivot(0, 0, 0);
		door.addChild(door_left);
		door_left.setTextureOffset(224, 251).addCuboid(-20.8F, -14, 0, 1, 14, 16, 0, false);

		door_left_top_r1 = new ModelMapper(modelPartData);
		door_left_top_r1.setPivot(-20.8F, -14, 0);
		door_left.addChild(door_left_top_r1);
		setRotationAngle(door_left_top_r1, 0, 0, 0.1107F);
		door_left_top_r1.setTextureOffset(236, 122).addCuboid(0, -19, 0, 1, 19, 16, 0, false);

		door_right = new ModelMapper(modelPartData);
		door_right.setPivot(0, 0, 0);
		door.addChild(door_right);
		door_right.setTextureOffset(156, 251).addCuboid(-20.8F, -14, -16, 1, 14, 16, 0, false);

		door_right_top_r1 = new ModelMapper(modelPartData);
		door_right_top_r1.setPivot(-20.8F, -14, 0);
		door_right.addChild(door_right_top_r1);
		setRotationAngle(door_right_top_r1, 0, 0, 0.1107F);
		door_right_top_r1.setTextureOffset(116, 251).addCuboid(0, -19, -16, 1, 19, 16, 0, false);

		door_light_c1141a = new ModelMapper(modelPartData);
		door_light_c1141a.setPivot(0, 24, 0);
		door_light_c1141a.setTextureOffset(96, 40).addCuboid(-4, -36, -3.5F, 4, 1, 0, 0, false);
		door_light_c1141a.setTextureOffset(96, 32).addCuboid(-3.5F, -36, -4, 0, 1, 8, 0, false);
		door_light_c1141a.setTextureOffset(96, 40).addCuboid(-4, -36, 3.5F, 4, 1, 0, 0, false);

		door_exterior_1 = new ModelMapper(modelPartData);
		door_exterior_1.setPivot(0, 24, 0);
		door_exterior_1.setTextureOffset(178, 334).addCuboid(20, 0, -16, 1, 7, 32, 0, true);

		door_right_exterior_1 = new ModelMapper(modelPartData);
		door_right_exterior_1.setPivot(0, 0, 0);
		door_exterior_1.addChild(door_right_exterior_1);
		door_right_exterior_1.setTextureOffset(128, 33).addCuboid(20.8F, -14, 0, 0, 14, 16, 0, true);

		door_left_top_r2 = new ModelMapper(modelPartData);
		door_left_top_r2.setPivot(20.8F, -14, 0);
		door_right_exterior_1.addChild(door_left_top_r2);
		setRotationAngle(door_left_top_r2, 0, 0, -0.1107F);
		door_left_top_r2.setTextureOffset(130, 99).addCuboid(0, -19, 0, 0, 19, 16, 0, true);

		door_left_exterior_1 = new ModelMapper(modelPartData);
		door_left_exterior_1.setPivot(0, 0, 0);
		door_exterior_1.addChild(door_left_exterior_1);
		door_left_exterior_1.setTextureOffset(96, 33).addCuboid(20.8F, -14, -16, 0, 14, 16, 0, true);

		door_right_top_r2_r1 = new ModelMapper(modelPartData);
		door_right_top_r2_r1.setPivot(20.8F, -14, 0);
		door_left_exterior_1.addChild(door_right_top_r2_r1);
		setRotationAngle(door_right_top_r2_r1, 0, 0, -0.1107F);
		door_right_top_r2_r1.setTextureOffset(58, 99).addCuboid(0, -19, -16, 0, 19, 16, 0, true);

		door_exterior_2 = new ModelMapper(modelPartData);
		door_exterior_2.setPivot(0, 24, 0);
		door_exterior_2.setTextureOffset(178, 334).addCuboid(-21, 0, -16, 1, 7, 32, 0, false);

		door_left_exterior_2 = new ModelMapper(modelPartData);
		door_left_exterior_2.setPivot(0, 0, 0);
		door_exterior_2.addChild(door_left_exterior_2);
		door_left_exterior_2.setTextureOffset(128, 33).addCuboid(-20.8F, -14, 0, 0, 14, 16, 0, false);

		door_left_top_r3 = new ModelMapper(modelPartData);
		door_left_top_r3.setPivot(-20.8F, -14, 0);
		door_left_exterior_2.addChild(door_left_top_r3);
		setRotationAngle(door_left_top_r3, 0, 0, 0.1107F);
		door_left_top_r3.setTextureOffset(130, 99).addCuboid(0, -19, 0, 0, 19, 16, 0, false);

		door_right_exterior_2 = new ModelMapper(modelPartData);
		door_right_exterior_2.setPivot(0, 0, 0);
		door_exterior_2.addChild(door_right_exterior_2);
		door_right_exterior_2.setTextureOffset(96, 33).addCuboid(-20.8F, -14, -16, 0, 14, 16, 0, false);

		door_right_top_r3_r1 = new ModelMapper(modelPartData);
		door_right_top_r3_r1.setPivot(-20.8F, -14, 0);
		door_right_exterior_2.addChild(door_right_top_r3_r1);
		setRotationAngle(door_right_top_r3_r1, 0, 0, 0.1107F);
		door_right_top_r3_r1.setTextureOffset(58, 99).addCuboid(0, -19, -16, 0, 19, 16, 0, false);

		end = new ModelMapper(modelPartData);
		end.setPivot(0, 24, 0);
		end.setTextureOffset(96, 0).addCuboid(-20, 0, -32, 40, 1, 48, 0, false);
		end.setTextureOffset(58, 251).addCuboid(18, -14, -36, 2, 14, 54, 0, true);
		end.setTextureOffset(228, 124).addCuboid(-20, -14, -36, 2, 14, 54, 0, false);
		end.setTextureOffset(0, 188).addCuboid(11, -33, -36, 7, 33, 12, 0, false);
		end.setTextureOffset(0, 115).addCuboid(-18, -33, -36, 7, 33, 12, 0, false);
		end.setTextureOffset(320, 320).addCuboid(-18, -44, -36, 36, 11, 12, 0, false);

		upper_wall_2_r1 = new ModelMapper(modelPartData);
		upper_wall_2_r1.setPivot(-20, -14, 0);
		end.addChild(upper_wall_2_r1);
		setRotationAngle(upper_wall_2_r1, 0, 0, 0.1107F);
		upper_wall_2_r1.setTextureOffset(170, 178).addCuboid(0, -19, -36, 2, 19, 54, 0, false);

		upper_wall_1_r1 = new ModelMapper(modelPartData);
		upper_wall_1_r1.setPivot(20, -14, 0);
		end.addChild(upper_wall_1_r1);
		setRotationAngle(upper_wall_1_r1, 0, 0, -0.1107F);
		upper_wall_1_r1.setTextureOffset(194, 49).addCuboid(-2, -19, -36, 2, 19, 54, 0, true);

		seat_end_1 = new ModelMapper(modelPartData);
		seat_end_1.setPivot(0, 0, 0);
		end.addChild(seat_end_1);
		seat_end_1.setTextureOffset(116, 178).addCuboid(11, -6, -24, 7, 1, 40, 0, false);

		seat_back_1_c1141a_r1 = new ModelMapper(modelPartData);
		seat_back_1_c1141a_r1.setPivot(18, -6.5F, 0);
		seat_end_1.addChild(seat_back_1_c1141a_r1);
		setRotationAngle(seat_back_1_c1141a_r1, 0, 0, 0.0873F);
		seat_back_1_c1141a_r1.setTextureOffset(282, 343).addCuboid(-1, -6, -24, 1, 4, 40, 0, false);
		seat_back_1_c1141a_r1.setTextureOffset(194, 122).addCuboid(-1, -8, -24, 1, 8, 40, 0, false);

		seat_end_2 = new ModelMapper(modelPartData);
		seat_end_2.setPivot(0, 0, 0);
		end.addChild(seat_end_2);


		seat_back_2_c1141a_r1 = new ModelMapper(modelPartData);
		seat_back_2_c1141a_r1.setPivot(-18, -6.5F, 0);
		seat_end_2.addChild(seat_back_2_c1141a_r1);
		setRotationAngle(seat_back_2_c1141a_r1, 0, 3.1416F, -0.1047F);
		seat_back_2_c1141a_r1.setTextureOffset(282, 343).addCuboid(-1, -6, -16, 1, 4, 40, 0, false);
		seat_back_2_c1141a_r1.setTextureOffset(194, 122).addCuboid(-1, -8, -16, 1, 8, 40, 0, false);

		seat_bottom_2_r1 = new ModelMapper(modelPartData);
		seat_bottom_2_r1.setPivot(0, 0, 0);
		seat_end_2.addChild(seat_bottom_2_r1);
		setRotationAngle(seat_bottom_2_r1, 0, 3.1416F, 0);
		seat_bottom_2_r1.setTextureOffset(116, 178).addCuboid(11, -6, -16, 7, 1, 40, 0, false);

		end_exterior = new ModelMapper(modelPartData);
		end_exterior.setPivot(0, 24, 0);
		end_exterior.setTextureOffset(0, 271).addCuboid(-21, 0, -32, 1, 7, 48, 0, false);
		end_exterior.setTextureOffset(228, 228).addCuboid(18, -14, -36, 2, 14, 54, 0, true);
		end_exterior.setTextureOffset(0, 197).addCuboid(-20, -14, -36, 2, 14, 54, 0, false);
		end_exterior.setTextureOffset(162, 115).addCuboid(11, -33, -36, 7, 33, 0, 0, false);
		end_exterior.setTextureOffset(38, 115).addCuboid(-18, -33, -36, 7, 33, 0, 0, false);
		end_exterior.setTextureOffset(116, 219).addCuboid(-18, -44, -36, 36, 11, 0, 0, false);

		outer_roof_2_r1 = new ModelMapper(modelPartData);
		outer_roof_2_r1.setPivot(20, -14, 0);
		end_exterior.addChild(outer_roof_2_r1);
		setRotationAngle(outer_roof_2_r1, 0, 0, -0.1107F);
		outer_roof_2_r1.setTextureOffset(170, 251).addCuboid(0, -26, -36, 1, 8, 52, 0, true);
		outer_roof_2_r1.setTextureOffset(58, 178).addCuboid(-2, -19, -36, 2, 19, 54, 0, true);

		outer_roof_1_r1 = new ModelMapper(modelPartData);
		outer_roof_1_r1.setPivot(-20, -14, 0);
		end_exterior.addChild(outer_roof_1_r1);
		setRotationAngle(outer_roof_1_r1, 0, 0, 0.1107F);
		outer_roof_1_r1.setTextureOffset(170, 251).addCuboid(-1, -26, -36, 1, 8, 52, 0, false);
		outer_roof_1_r1.setTextureOffset(0, 115).addCuboid(0, -19, -36, 2, 19, 54, 0, false);

		floor_1_r1 = new ModelMapper(modelPartData);
		floor_1_r1.setPivot(0, 0, 0);
		end_exterior.addChild(floor_1_r1);
		setRotationAngle(floor_1_r1, 0, 3.1416F, 0);
		floor_1_r1.setTextureOffset(0, 271).addCuboid(-21, 0, -16, 1, 7, 48, 0, false);

		roof_sp1900 = new ModelMapper(modelPartData);
		roof_sp1900.setPivot(0, 24, 0);
		roof_sp1900.setTextureOffset(70, 0).addCuboid(-18, -32, -16, 3, 0, 32, 0, false);
		roof_sp1900.setTextureOffset(8, 0).addCuboid(-4, -36, -16, 4, 0, 32, 0, false);

		inner_roof_6_r1 = new ModelMapper(modelPartData);
		inner_roof_6_r1.setPivot(-5.1552F, -35.6379F, 0);
		roof_sp1900.addChild(inner_roof_6_r1);
		setRotationAngle(inner_roof_6_r1, 0, 0, -0.2618F);
		inner_roof_6_r1.setTextureOffset(64, 0).addCuboid(-1.5F, 0, -16, 3, 0, 32, 0, false);

		inner_roof_5_r1 = new ModelMapper(modelPartData);
		inner_roof_5_r1.setPivot(-11.3018F, -34.9909F, 0);
		roof_sp1900.addChild(inner_roof_5_r1);
		setRotationAngle(inner_roof_5_r1, 0, 0, -0.2618F);
		inner_roof_5_r1.setTextureOffset(8, 66).addCuboid(-1, 0, -16, 2, 0, 32, 0, false);

		inner_roof_4_r1 = new ModelMapper(modelPartData);
		inner_roof_4_r1.setPivot(-12.7005F, -34.4822F, 0);
		roof_sp1900.addChild(inner_roof_4_r1);
		setRotationAngle(inner_roof_4_r1, 0, 0, -0.5236F);
		inner_roof_4_r1.setTextureOffset(78, 0).addCuboid(-0.5F, 0, -16, 1, 0, 32, 0, false);

		inner_roof_3_r1 = new ModelMapper(modelPartData);
		inner_roof_3_r1.setPivot(-13.6331F, -33.3665F, 0);
		roof_sp1900.addChild(inner_roof_3_r1);
		setRotationAngle(inner_roof_3_r1, 0, 0, -1.0472F);
		inner_roof_3_r1.setTextureOffset(12, 66).addCuboid(-1, 0, -16, 2, 0, 32, 0, false);

		inner_roof_2_r1 = new ModelMapper(modelPartData);
		inner_roof_2_r1.setPivot(-14.5665F, -32.2501F, 0);
		roof_sp1900.addChild(inner_roof_2_r1);
		setRotationAngle(inner_roof_2_r1, 0, 0, -0.5236F);
		inner_roof_2_r1.setTextureOffset(80, 0).addCuboid(-0.5F, 0, -16, 1, 0, 32, 0, false);

		roof_c1141a = new ModelMapper(modelPartData);
		roof_c1141a.setPivot(0, 24, 0);
		roof_c1141a.setTextureOffset(70, 0).addCuboid(-18, -32, -16, 3, 0, 32, 0, false);
		roof_c1141a.setTextureOffset(8, 376).addCuboid(-13, -36, -16, 13, 0, 32, 0, false);

		inner_roof_3_r2 = new ModelMapper(modelPartData);
		inner_roof_3_r2.setPivot(-15, -32, 16);
		roof_c1141a.addChild(inner_roof_3_r2);
		setRotationAngle(inner_roof_3_r2, 0, 0, -1.0472F);
		inner_roof_3_r2.setTextureOffset(206, 49).addCuboid(0, 0, -32, 5, 0, 32, 0, false);

		roof_exterior = new ModelMapper(modelPartData);
		roof_exterior.setPivot(0, 24, 0);
		roof_exterior.setTextureOffset(98, 0).addCuboid(-6, -44, -16, 6, 0, 32, 0, false);

		outer_roof_4_r1 = new ModelMapper(modelPartData);
		outer_roof_4_r1.setPivot(-9.9391F, -43.3054F, 0);
		roof_exterior.addChild(outer_roof_4_r1);
		setRotationAngle(outer_roof_4_r1, 0, 0, -0.1745F);
		outer_roof_4_r1.setTextureOffset(82, 0).addCuboid(-4, 0, -16, 8, 0, 32, 0, false);

		outer_roof_3_r1 = new ModelMapper(modelPartData);
		outer_roof_3_r1.setPivot(-15.1773F, -41.8608F, 0);
		roof_exterior.addChild(outer_roof_3_r1);
		setRotationAngle(outer_roof_3_r1, 0, 0, -0.5236F);
		outer_roof_3_r1.setTextureOffset(20, 0).addCuboid(-1.5F, 0, -16, 3, 0, 32, 0, false);

		outer_roof_2_r2 = new ModelMapper(modelPartData);
		outer_roof_2_r2.setPivot(-16.9764F, -40.2448F, 0);
		roof_exterior.addChild(outer_roof_2_r2);
		setRotationAngle(outer_roof_2_r2, 0, 0, -1.0472F);
		outer_roof_2_r2.setTextureOffset(26, 6).addCuboid(-1, 0, -16, 2, 0, 32, 0, false);

		outer_roof_1_r2 = new ModelMapper(modelPartData);
		outer_roof_1_r2.setPivot(-20, -14, 0);
		roof_exterior.addChild(outer_roof_1_r2);
		setRotationAngle(outer_roof_1_r2, 0, 0, 0.1107F);
		outer_roof_1_r2.setTextureOffset(344, 228).addCuboid(-1, -26, -16, 1, 8, 32, 0, false);

		roof_light_sp1900 = new ModelMapper(modelPartData);
		roof_light_sp1900.setPivot(0, 24, 0);
		roof_light_sp1900.setTextureOffset(16, 8).addCuboid(-9.4701F, -34.7497F, -16, 2, 0, 32, 0, false);

		light_3_r1 = new ModelMapper(modelPartData);
		light_3_r1.setPivot(-7.0366F, -34.9988F, 0);
		roof_light_sp1900.addChild(light_3_r1);
		setRotationAngle(light_3_r1, 0, 0, -0.5236F);
		light_3_r1.setTextureOffset(30, 22).addCuboid(-0.5F, 0, -16, 1, 0, 32, 0, false);

		light_1_r1 = new ModelMapper(modelPartData);
		light_1_r1.setPivot(-9.9036F, -34.9998F, 0);
		roof_light_sp1900.addChild(light_1_r1);
		setRotationAngle(light_1_r1, 0, 0, 0.5236F);
		light_1_r1.setTextureOffset(76, 0).addCuboid(-0.5F, 0, -16, 1, 0, 32, 0, false);

		roof_light_c1141a = new ModelMapper(modelPartData);
		roof_light_c1141a.setPivot(0, 24, 0);
		roof_light_c1141a.setTextureOffset(274, 69).addCuboid(-10, -36.1F, -16, 4, 0, 32, 0, false);

		roof_end_sp1900 = new ModelMapper(modelPartData);
		roof_end_sp1900.setPivot(0, 24, 0);


		inner_roof_1 = new ModelMapper(modelPartData);
		inner_roof_1.setPivot(0, 0, 0);
		roof_end_sp1900.addChild(inner_roof_1);
		inner_roof_1.setTextureOffset(62, 0).addCuboid(-18, -32, -24, 3, 0, 40, 0, false);
		inner_roof_1.setTextureOffset(0, 0).addCuboid(-4, -36, -24, 4, 0, 40, 0, false);

		inner_roof_6_r2 = new ModelMapper(modelPartData);
		inner_roof_6_r2.setPivot(-5.1551F, -35.6369F, -4);
		inner_roof_1.addChild(inner_roof_6_r2);
		setRotationAngle(inner_roof_6_r2, 0, 0, -0.2618F);
		inner_roof_6_r2.setTextureOffset(56, 0).addCuboid(-1.5F, 0, -20, 3, 0, 40, 0, false);

		inner_roof_5_r2 = new ModelMapper(modelPartData);
		inner_roof_5_r2.setPivot(-11.3023F, -34.9918F, -4);
		inner_roof_1.addChild(inner_roof_5_r2);
		setRotationAngle(inner_roof_5_r2, 0, 0, -0.2618F);
		inner_roof_5_r2.setTextureOffset(0, 66).addCuboid(-1, 0, -20, 2, 0, 40, 0, false);

		inner_roof_4_r2 = new ModelMapper(modelPartData);
		inner_roof_4_r2.setPivot(-12.701F, -34.4831F, -4);
		inner_roof_1.addChild(inner_roof_4_r2);
		setRotationAngle(inner_roof_4_r2, 0, 0, -0.5236F);
		inner_roof_4_r2.setTextureOffset(70, 0).addCuboid(-0.5F, 0, -20, 1, 0, 40, 0, false);

		inner_roof_3_r3 = new ModelMapper(modelPartData);
		inner_roof_3_r3.setPivot(-13.6331F, -33.3665F, -4);
		inner_roof_1.addChild(inner_roof_3_r3);
		setRotationAngle(inner_roof_3_r3, 0, 0, -1.0472F);
		inner_roof_3_r3.setTextureOffset(4, 66).addCuboid(-1, 0, -20, 2, 0, 40, 0, false);

		inner_roof_2_r2 = new ModelMapper(modelPartData);
		inner_roof_2_r2.setPivot(-14.5665F, -32.2501F, -4);
		inner_roof_1.addChild(inner_roof_2_r2);
		setRotationAngle(inner_roof_2_r2, 0, 0, -0.5236F);
		inner_roof_2_r2.setTextureOffset(72, 0).addCuboid(-0.5F, 0, -20, 1, 0, 40, 0, false);

		inner_roof_2 = new ModelMapper(modelPartData);
		inner_roof_2.setPivot(0, 0, 0);
		roof_end_sp1900.addChild(inner_roof_2);
		inner_roof_2.setTextureOffset(62, 0).addCuboid(15, -32, -24, 3, 0, 40, 0, true);
		inner_roof_2.setTextureOffset(0, 0).addCuboid(0, -36, -24, 4, 0, 40, 0, true);

		inner_roof_6_r3 = new ModelMapper(modelPartData);
		inner_roof_6_r3.setPivot(5.1554F, -35.6388F, -4);
		inner_roof_2.addChild(inner_roof_6_r3);
		setRotationAngle(inner_roof_6_r3, 0, 0, 0.2618F);
		inner_roof_6_r3.setTextureOffset(56, 0).addCuboid(-1.5F, 0, -20, 3, 0, 40, 0, true);

		inner_roof_5_r3 = new ModelMapper(modelPartData);
		inner_roof_5_r3.setPivot(11.3023F, -34.9908F, -4);
		inner_roof_2.addChild(inner_roof_5_r3);
		setRotationAngle(inner_roof_5_r3, 0, 0, 0.2618F);
		inner_roof_5_r3.setTextureOffset(0, 66).addCuboid(-1, 0, -20, 2, 0, 40, 0, true);

		inner_roof_4_r3 = new ModelMapper(modelPartData);
		inner_roof_4_r3.setPivot(12.701F, -34.4821F, -4);
		inner_roof_2.addChild(inner_roof_4_r3);
		setRotationAngle(inner_roof_4_r3, 0, 0, 0.5236F);
		inner_roof_4_r3.setTextureOffset(72, 0).addCuboid(-0.5F, 0, -20, 1, 0, 40, 0, true);

		inner_roof_3_r4 = new ModelMapper(modelPartData);
		inner_roof_3_r4.setPivot(13.6336F, -33.3664F, -4);
		inner_roof_2.addChild(inner_roof_3_r4);
		setRotationAngle(inner_roof_3_r4, 0, 0, 1.0472F);
		inner_roof_3_r4.setTextureOffset(4, 66).addCuboid(-1, 0, -20, 2, 0, 40, 0, true);

		inner_roof_2_r3 = new ModelMapper(modelPartData);
		inner_roof_2_r3.setPivot(14.5665F, -32.2491F, -4);
		inner_roof_2.addChild(inner_roof_2_r3);
		setRotationAngle(inner_roof_2_r3, 0, 0, 0.5236F);
		inner_roof_2_r3.setTextureOffset(72, 0).addCuboid(-0.5F, 0, -20, 1, 0, 40, 0, true);

		roof_end_c1141a = new ModelMapper(modelPartData);
		roof_end_c1141a.setPivot(0, 24, 0);


		inner_roof_3 = new ModelMapper(modelPartData);
		inner_roof_3.setPivot(0, 0, 0);
		roof_end_c1141a.addChild(inner_roof_3);
		inner_roof_3.setTextureOffset(62, 0).addCuboid(-18, -32, -24, 3, 0, 40, 0, false);
		inner_roof_3.setTextureOffset(0, 376).addCuboid(-13, -36, -24, 13, 0, 40, 0, false);

		inner_roof_3_r5 = new ModelMapper(modelPartData);
		inner_roof_3_r5.setPivot(-15, -32, 0);
		inner_roof_3.addChild(inner_roof_3_r5);
		setRotationAngle(inner_roof_3_r5, 0, 0, -1.0472F);
		inner_roof_3_r5.setTextureOffset(198, 49).addCuboid(0, 0, -24, 5, 0, 40, 0, false);

		inner_roof_4 = new ModelMapper(modelPartData);
		inner_roof_4.setPivot(0, 0, 0);
		roof_end_c1141a.addChild(inner_roof_4);
		inner_roof_4.setTextureOffset(62, 0).addCuboid(15, -32, -24, 3, 0, 40, 0, true);
		inner_roof_4.setTextureOffset(0, 376).addCuboid(0, -36, -24, 13, 0, 40, 0, true);

		inner_roof_3_r6 = new ModelMapper(modelPartData);
		inner_roof_3_r6.setPivot(15, -32, 0);
		inner_roof_4.addChild(inner_roof_3_r6);
		setRotationAngle(inner_roof_3_r6, 0, 0, 1.0472F);
		inner_roof_3_r6.setTextureOffset(198, 49).addCuboid(-5, 0, -24, 5, 0, 40, 0, true);

		roof_end_exterior = new ModelMapper(modelPartData);
		roof_end_exterior.setPivot(0, 24, 0);
		roof_end_exterior.setTextureOffset(0, 0).addCuboid(-8, -45, -36, 16, 2, 64, 0, false);

		vent_2_r1 = new ModelMapper(modelPartData);
		vent_2_r1.setPivot(-8, -45, 0);
		roof_end_exterior.addChild(vent_2_r1);
		setRotationAngle(vent_2_r1, 0, 0, -0.3491F);
		vent_2_r1.setTextureOffset(112, 112).addCuboid(-9, 0, -36, 9, 2, 64, 0, true);

		vent_1_r1 = new ModelMapper(modelPartData);
		vent_1_r1.setPivot(8, -45, 0);
		roof_end_exterior.addChild(vent_1_r1);
		setRotationAngle(vent_1_r1, 0, 0, 0.3491F);
		vent_1_r1.setTextureOffset(112, 112).addCuboid(0, 0, -36, 9, 2, 64, 0, false);

		outer_roof_1 = new ModelMapper(modelPartData);
		outer_roof_1.setPivot(0, 0, 0);
		roof_end_exterior.addChild(outer_roof_1);


		outer_roof_3_r2 = new ModelMapper(modelPartData);
		outer_roof_3_r2.setPivot(-15.1773F, -41.8608F, -10);
		outer_roof_1.addChild(outer_roof_3_r2);
		setRotationAngle(outer_roof_3_r2, 0, 0, -0.5236F);
		outer_roof_3_r2.setTextureOffset(0, 0).addCuboid(-1.5F, 0, -26, 3, 0, 52, 0, false);

		outer_roof_2_r3 = new ModelMapper(modelPartData);
		outer_roof_2_r3.setPivot(-16.9764F, -40.2448F, -10);
		outer_roof_1.addChild(outer_roof_2_r3);
		setRotationAngle(outer_roof_2_r3, 0, 0, -1.0472F);
		outer_roof_2_r3.setTextureOffset(6, 6).addCuboid(-1, 0, -26, 2, 0, 52, 0, false);

		outer_roof_2 = new ModelMapper(modelPartData);
		outer_roof_2.setPivot(0, 0, 0);
		roof_end_exterior.addChild(outer_roof_2);


		outer_roof_3_r3 = new ModelMapper(modelPartData);
		outer_roof_3_r3.setPivot(15.1773F, -41.8608F, -10);
		outer_roof_2.addChild(outer_roof_3_r3);
		setRotationAngle(outer_roof_3_r3, 0, 0, 0.5236F);
		outer_roof_3_r3.setTextureOffset(0, 0).addCuboid(-1.5F, 0, -26, 3, 0, 52, 0, true);

		outer_roof_2_r4 = new ModelMapper(modelPartData);
		outer_roof_2_r4.setPivot(16.9764F, -40.2448F, -10);
		outer_roof_2.addChild(outer_roof_2_r4);
		setRotationAngle(outer_roof_2_r4, 0, 0, 1.0472F);
		outer_roof_2_r4.setTextureOffset(6, 6).addCuboid(-1, 0, -26, 2, 0, 52, 0, true);

		roof_end_light_sp1900 = new ModelMapper(modelPartData);
		roof_end_light_sp1900.setPivot(0, 24, 0);
		roof_end_light_sp1900.setTextureOffset(8, 8).addCuboid(-9.4703F, -34.7496F, -24, 2, 0, 40, 0, false);
		roof_end_light_sp1900.setTextureOffset(8, 8).addCuboid(7.4706F, -34.7506F, -24, 2, 0, 40, 0, true);

		light_6_r1 = new ModelMapper(modelPartData);
		light_6_r1.setPivot(7.0371F, -35.0007F, -4);
		roof_end_light_sp1900.addChild(light_6_r1);
		setRotationAngle(light_6_r1, 0, 0, 0.5236F);
		light_6_r1.setTextureOffset(22, 22).addCuboid(-0.5F, 0, -20, 1, 0, 40, 0, true);

		light_4_r1 = new ModelMapper(modelPartData);
		light_4_r1.setPivot(9.9041F, -34.9997F, -4);
		roof_end_light_sp1900.addChild(light_4_r1);
		setRotationAngle(light_4_r1, 0, 0, -0.5236F);
		light_4_r1.setTextureOffset(68, 0).addCuboid(-0.5F, 0, -20, 1, 0, 40, 0, true);

		light_3_r2 = new ModelMapper(modelPartData);
		light_3_r2.setPivot(-7.0368F, -34.9987F, -4);
		roof_end_light_sp1900.addChild(light_3_r2);
		setRotationAngle(light_3_r2, 0, 0, -0.5236F);
		light_3_r2.setTextureOffset(22, 22).addCuboid(-0.5F, 0, -20, 1, 0, 40, 0, false);

		light_1_r2 = new ModelMapper(modelPartData);
		light_1_r2.setPivot(-9.9038F, -34.9997F, -4);
		roof_end_light_sp1900.addChild(light_1_r2);
		setRotationAngle(light_1_r2, 0, 0, 0.5236F);
		light_1_r2.setTextureOffset(68, 0).addCuboid(-0.5F, 0, -20, 1, 0, 40, 0, false);

		roof_end_light_c1141a = new ModelMapper(modelPartData);
		roof_end_light_c1141a.setPivot(0, 24, 0);
		roof_end_light_c1141a.setTextureOffset(266, 69).addCuboid(-10, -36.1F, -24, 4, 0, 40, 0, false);
		roof_end_light_c1141a.setTextureOffset(266, 69).addCuboid(6, -36.1F, -24, 4, 0, 40, 0, false);

		top_handrail_sp1900 = new ModelMapper(modelPartData);
		top_handrail_sp1900.setPivot(0, 24, 0);
		top_handrail_sp1900.setTextureOffset(0, 0).addCuboid(-5, -36, 15.8F, 0, 3, 0, 0.2F, false);
		top_handrail_sp1900.setTextureOffset(0, 0).addCuboid(-5, -36, -15.8F, 0, 3, 0, 0.2F, false);

		top_handrail_bottom_right_r1 = new ModelMapper(modelPartData);
		top_handrail_bottom_right_r1.setPivot(-5, -31.0876F, -6.8876F);
		top_handrail_sp1900.addChild(top_handrail_bottom_right_r1);
		setRotationAngle(top_handrail_bottom_right_r1, -1.5708F, 0, 0);
		top_handrail_bottom_right_r1.setTextureOffset(0, 0).addCuboid(0, -7, 0, 0, 14, 0, 0.2F, false);

		top_handrail_bottom_left_r1 = new ModelMapper(modelPartData);
		top_handrail_bottom_left_r1.setPivot(-5, -31.0876F, 6.8876F);
		top_handrail_sp1900.addChild(top_handrail_bottom_left_r1);
		setRotationAngle(top_handrail_bottom_left_r1, -1.5708F, 0, 0);
		top_handrail_bottom_left_r1.setTextureOffset(0, 0).addCuboid(0, -7, 0, 0, 14, 0, 0.2F, false);

		top_handrail_right_3_r1 = new ModelMapper(modelPartData);
		top_handrail_right_3_r1.setPivot(-5, -31.4108F, -14.5938F);
		top_handrail_sp1900.addChild(top_handrail_right_3_r1);
		setRotationAngle(top_handrail_right_3_r1, 1.0472F, 0, 0);
		top_handrail_right_3_r1.setTextureOffset(0, 0).addCuboid(0, -0.5F, 0, 0, 1, 0, 0.2F, false);

		top_handrail_right_2_r1 = new ModelMapper(modelPartData);
		top_handrail_right_2_r1.setPivot(-5, -32.2938F, -15.4768F);
		top_handrail_sp1900.addChild(top_handrail_right_2_r1);
		setRotationAngle(top_handrail_right_2_r1, 0.5236F, 0, 0);
		top_handrail_right_2_r1.setTextureOffset(0, 0).addCuboid(0, -0.5F, 0, 0, 1, 0, 0.2F, false);

		top_handrail_left_3_r1 = new ModelMapper(modelPartData);
		top_handrail_left_3_r1.setPivot(-5, -31.4108F, 14.5938F);
		top_handrail_sp1900.addChild(top_handrail_left_3_r1);
		setRotationAngle(top_handrail_left_3_r1, -1.0472F, 0, 0);
		top_handrail_left_3_r1.setTextureOffset(0, 0).addCuboid(0, -0.5F, 0, 0, 1, 0, 0.2F, false);

		top_handrail_left_2_r1 = new ModelMapper(modelPartData);
		top_handrail_left_2_r1.setPivot(-5, -32.2938F, 15.4768F);
		top_handrail_sp1900.addChild(top_handrail_left_2_r1);
		setRotationAngle(top_handrail_left_2_r1, -0.5236F, 0, 0);
		top_handrail_left_2_r1.setTextureOffset(0, 0).addCuboid(0, -0.5F, 0, 0, 1, 0, 0.2F, false);

		handrail_strap_1 = new ModelMapper(modelPartData);
		handrail_strap_1.setPivot(0, 0, 0);
		top_handrail_sp1900.addChild(handrail_strap_1);
		handrail_strap_1.setTextureOffset(12, 12).addCuboid(-6, -32, -12, 2, 4, 0, 0, false);
		handrail_strap_1.setTextureOffset(12, 12).addCuboid(-6, -32, -6, 2, 4, 0, 0, false);
		handrail_strap_1.setTextureOffset(12, 12).addCuboid(-6, -32, 0, 2, 4, 0, 0, false);
		handrail_strap_1.setTextureOffset(12, 12).addCuboid(-6, -32, 6, 2, 4, 0, 0, false);
		handrail_strap_1.setTextureOffset(12, 12).addCuboid(-6, -32, 12, 2, 4, 0, 0, false);

		top_handrail_c1141a = new ModelMapper(modelPartData);
		top_handrail_c1141a.setPivot(0, 24, 0);
		top_handrail_c1141a.setTextureOffset(0, 50).addCuboid(-5, -31, 15.8F, 5, 0, 0, 0.2F, false);
		top_handrail_c1141a.setTextureOffset(0, 50).addCuboid(-5, -31, -15.8F, 5, 0, 0, 0.2F, false);
		top_handrail_c1141a.setTextureOffset(0, 0).addCuboid(-4.727F, -36.6045F, -11, 0, 3, 0, 0.2F, false);
		top_handrail_c1141a.setTextureOffset(0, 0).addCuboid(-4.727F, -36.6045F, 0, 0, 3, 0, 0.2F, false);
		top_handrail_c1141a.setTextureOffset(0, 0).addCuboid(-4.727F, -36.6045F, 11, 0, 3, 0, 0.2F, false);
		top_handrail_c1141a.setTextureOffset(0, 0).addCuboid(0, -36.6046F, 13.6145F, 0, 3, 0, 0.2F, false);
		top_handrail_c1141a.setTextureOffset(0, 0).addCuboid(0, -36, 11, 0, 7, 0, 0.2F, false);
		top_handrail_c1141a.setTextureOffset(8, 7).addCuboid(0, -23.2645F, 10.437F, 0, 6, 0, 0.2F, false);
		top_handrail_c1141a.setTextureOffset(8, 7).addCuboid(0, -23.2645F, 11.563F, 0, 6, 0, 0.2F, false);
		top_handrail_c1141a.setTextureOffset(0, 0).addCuboid(0, -12, 11, 0, 12, 0, 0.2F, false);

		pole_bottom_diagonal_2_r1 = new ModelMapper(modelPartData);
		pole_bottom_diagonal_2_r1.setPivot(0, -14.4002F, 11.2819F);
		top_handrail_c1141a.addChild(pole_bottom_diagonal_2_r1);
		setRotationAngle(pole_bottom_diagonal_2_r1, -0.1047F, 0, 0);
		pole_bottom_diagonal_2_r1.setTextureOffset(11, 28).addCuboid(0, -2.5F, 0, 0, 5, 0, 0.2F, false);

		pole_bottom_diagonal_1_r1 = new ModelMapper(modelPartData);
		pole_bottom_diagonal_1_r1.setPivot(0, -14.4002F, 10.7181F);
		top_handrail_c1141a.addChild(pole_bottom_diagonal_1_r1);
		setRotationAngle(pole_bottom_diagonal_1_r1, 0.1047F, 0, 0);
		pole_bottom_diagonal_1_r1.setTextureOffset(11, 28).addCuboid(0, -2.5F, 0, 0, 5, 0, 0.2F, false);

		pole_top_diagonal_2_r1 = new ModelMapper(modelPartData);
		pole_top_diagonal_2_r1.setPivot(0.2F, -28.8F, 10.8F);
		top_handrail_c1141a.addChild(pole_top_diagonal_2_r1);
		setRotationAngle(pole_top_diagonal_2_r1, 0.1047F, 0, 0);
		pole_top_diagonal_2_r1.setTextureOffset(11, 11).addCuboid(-0.2F, 0.2069F, 0.2F, 0, 5, 0, 0.2F, false);

		pole_top_diagonal_1_r1 = new ModelMapper(modelPartData);
		pole_top_diagonal_1_r1.setPivot(0.2F, -28.8F, 11.2F);
		top_handrail_c1141a.addChild(pole_top_diagonal_1_r1);
		setRotationAngle(pole_top_diagonal_1_r1, -0.1047F, 0, 0);
		pole_top_diagonal_1_r1.setTextureOffset(11, 11).addCuboid(-0.2F, 0.2069F, -0.2F, 0, 5, 0, 0.2F, false);

		top_handrail_connector_bottom_4_r1 = new ModelMapper(modelPartData);
		top_handrail_connector_bottom_4_r1.setPivot(0, -32.2308F, 14.6605F);
		top_handrail_c1141a.addChild(top_handrail_connector_bottom_4_r1);
		setRotationAngle(top_handrail_connector_bottom_4_r1, 0.6981F, 0, 0);
		top_handrail_connector_bottom_4_r1.setTextureOffset(0, 0).addCuboid(0, -1.5F, 0, 0, 3, 0, 0.2F, false);

		top_handrail_connector_bottom_3_r1 = new ModelMapper(modelPartData);
		top_handrail_connector_bottom_3_r1.setPivot(-5.7729F, -32.2308F, 0);
		top_handrail_c1141a.addChild(top_handrail_connector_bottom_3_r1);
		setRotationAngle(top_handrail_connector_bottom_3_r1, 0, 0, 0.6981F);
		top_handrail_connector_bottom_3_r1.setTextureOffset(0, 0).addCuboid(0, -1.5F, 11, 0, 3, 0, 0.2F, false);
		top_handrail_connector_bottom_3_r1.setTextureOffset(0, 0).addCuboid(0, -1.5F, 0, 0, 3, 0, 0.2F, false);
		top_handrail_connector_bottom_3_r1.setTextureOffset(0, 0).addCuboid(0, -1.5F, -11, 0, 3, 0, 0.2F, false);

		top_handrail_bottom_right_r2 = new ModelMapper(modelPartData);
		top_handrail_bottom_right_r2.setPivot(-6.9124F, -31, -6.8876F);
		top_handrail_c1141a.addChild(top_handrail_bottom_right_r2);
		setRotationAngle(top_handrail_bottom_right_r2, 1.5708F, 0, 0);
		top_handrail_bottom_right_r2.setTextureOffset(0, 0).addCuboid(0, -7, 0, 0, 14, 0, 0.2F, false);

		top_handrail_bottom_left_r2 = new ModelMapper(modelPartData);
		top_handrail_bottom_left_r2.setPivot(-6.9124F, -31, 6.8876F);
		top_handrail_c1141a.addChild(top_handrail_bottom_left_r2);
		setRotationAngle(top_handrail_bottom_left_r2, -1.5708F, 0, 0);
		top_handrail_bottom_left_r2.setTextureOffset(0, 0).addCuboid(0, -7, 0, 0, 14, 0, 0.2F, false);

		top_handrail_right_4_r1 = new ModelMapper(modelPartData);
		top_handrail_right_4_r1.setPivot(-6.5892F, -31, -14.5938F);
		top_handrail_c1141a.addChild(top_handrail_right_4_r1);
		setRotationAngle(top_handrail_right_4_r1, 1.5708F, -0.5236F, 0);
		top_handrail_right_4_r1.setTextureOffset(0, 0).addCuboid(0, -0.5F, 0, 0, 1, 0, 0.2F, false);

		top_handrail_right_3_r2 = new ModelMapper(modelPartData);
		top_handrail_right_3_r2.setPivot(-5.7062F, -31, -15.4768F);
		top_handrail_c1141a.addChild(top_handrail_right_3_r2);
		setRotationAngle(top_handrail_right_3_r2, 1.5708F, -1.0472F, 0);
		top_handrail_right_3_r2.setTextureOffset(0, 0).addCuboid(0, -0.5F, 0, 0, 1, 0, 0.2F, false);

		top_handrail_left_4_r1 = new ModelMapper(modelPartData);
		top_handrail_left_4_r1.setPivot(-6.5892F, -31, 14.5938F);
		top_handrail_c1141a.addChild(top_handrail_left_4_r1);
		setRotationAngle(top_handrail_left_4_r1, -1.5708F, 0.5236F, 0);
		top_handrail_left_4_r1.setTextureOffset(0, 0).addCuboid(0, -0.5F, 0, 0, 1, 0, 0.2F, false);

		top_handrail_left_3_r2 = new ModelMapper(modelPartData);
		top_handrail_left_3_r2.setPivot(-5.7062F, -31, 15.4768F);
		top_handrail_c1141a.addChild(top_handrail_left_3_r2);
		setRotationAngle(top_handrail_left_3_r2, -1.5708F, 1.0472F, 0);
		top_handrail_left_3_r2.setTextureOffset(0, 0).addCuboid(0, -0.5F, 0, 0, 1, 0, 0.2F, false);

		handrail_strap_2 = new ModelMapper(modelPartData);
		handrail_strap_2.setPivot(0, 0, 0);
		top_handrail_c1141a.addChild(handrail_strap_2);
		handrail_strap_2.setTextureOffset(12, 12).addCuboid(-8, -32, -14, 2, 4, 0, 0, false);
		handrail_strap_2.setTextureOffset(12, 12).addCuboid(-8, -32, -9, 2, 4, 0, 0, false);
		handrail_strap_2.setTextureOffset(12, 12).addCuboid(-8, -32, -4, 2, 4, 0, 0, false);
		handrail_strap_2.setTextureOffset(12, 12).addCuboid(-8, -32, 4, 2, 4, 0, 0, false);
		handrail_strap_2.setTextureOffset(12, 12).addCuboid(-8, -32, 9, 2, 4, 0, 0, false);
		handrail_strap_2.setTextureOffset(12, 12).addCuboid(-8, -32, 14, 2, 4, 0, 0, false);

		handrail_strap_8_r1 = new ModelMapper(modelPartData);
		handrail_strap_8_r1.setPivot(0, 0, 0);
		handrail_strap_2.addChild(handrail_strap_8_r1);
		setRotationAngle(handrail_strap_8_r1, 0, -1.5708F, 0);
		handrail_strap_8_r1.setTextureOffset(12, 12).addCuboid(-16.8F, -32, 3, 2, 4, 0, 0, false);
		handrail_strap_8_r1.setTextureOffset(12, 12).addCuboid(14.8F, -32, 3, 2, 4, 0, 0, false);

		tv_pole = new ModelMapper(modelPartData);
		tv_pole.setPivot(0, 24, 0);
		tv_pole.setTextureOffset(18, 0).addCuboid(-4, -36, -1, 8, 6, 2, 0, false);
		tv_pole.setTextureOffset(4, 0).addCuboid(0, -27.5F, 0, 0, 28, 0, 0.2F, false);
		tv_pole.setTextureOffset(4, 0).addCuboid(-1, -27.5F, 0, 2, 0, 0, 0.2F, false);

		tv_right_r1 = new ModelMapper(modelPartData);
		tv_right_r1.setPivot(4, -30, -1);
		tv_pole.addChild(tv_right_r1);
		setRotationAngle(tv_right_r1, -0.1047F, 3.1416F, 0);
		tv_right_r1.setTextureOffset(18, 8).addCuboid(0, -7, -1, 8, 7, 1, 0, false);

		tv_left_r1 = new ModelMapper(modelPartData);
		tv_left_r1.setPivot(4, -30, 1);
		tv_pole.addChild(tv_left_r1);
		setRotationAngle(tv_left_r1, -0.1047F, 0, 0);
		tv_left_r1.setTextureOffset(18, 8).addCuboid(-8, -7, -1, 8, 7, 1, 0, false);

		pole_5_r1 = new ModelMapper(modelPartData);
		pole_5_r1.setPivot(-3.3885F, -29.8726F, 0);
		tv_pole.addChild(pole_5_r1);
		setRotationAngle(pole_5_r1, 0, 0, 1.2217F);
		pole_5_r1.setTextureOffset(4, 0).addCuboid(-1, 0, 0, 2, 0, 0, 0.2F, false);

		pole_4_r1 = new ModelMapper(modelPartData);
		pole_4_r1.setPivot(-1.2F, -27.3F, 0);
		tv_pole.addChild(pole_4_r1);
		setRotationAngle(pole_4_r1, 0, 0, 0.6109F);
		pole_4_r1.setTextureOffset(4, 0).addCuboid(-2.2F, -0.2F, 0, 2, 0, 0, 0.2F, false);

		pole_2_r1 = new ModelMapper(modelPartData);
		pole_2_r1.setPivot(1.2F, -27.3F, 0);
		tv_pole.addChild(pole_2_r1);
		setRotationAngle(pole_2_r1, 0, 0, -0.6109F);
		pole_2_r1.setTextureOffset(4, 0).addCuboid(0.2F, -0.2F, 0, 2, 0, 0, 0.2F, false);

		pole_1_r1 = new ModelMapper(modelPartData);
		pole_1_r1.setPivot(3.3885F, -29.8726F, 0);
		tv_pole.addChild(pole_1_r1);
		setRotationAngle(pole_1_r1, 0, 0, -1.2217F);
		pole_1_r1.setTextureOffset(4, 0).addCuboid(-1, 0, 0, 2, 0, 0, 0.2F, false);

		head = new ModelMapper(modelPartData);
		head.setPivot(0, 24, 0);
		head.setTextureOffset(0, 66).addCuboid(-20, 0, -16, 40, 1, 48, 0, false);
		head.setTextureOffset(326, 69).addCuboid(-20, -14, -18, 2, 14, 36, 0, false);
		head.setTextureOffset(0, 326).addCuboid(18, -14, -18, 2, 14, 36, 0, true);
		head.setTextureOffset(326, 192).addCuboid(-18, -36, 8, 36, 36, 0, 0, false);

		upper_wall_2_r2 = new ModelMapper(modelPartData);
		upper_wall_2_r2.setPivot(20, -14, 0);
		head.addChild(upper_wall_2_r2);
		setRotationAngle(upper_wall_2_r2, 0, 0, -0.1107F);
		upper_wall_2_r2.setTextureOffset(240, 296).addCuboid(-2, -19, -18, 2, 19, 36, 0, true);

		upper_wall_1_r2 = new ModelMapper(modelPartData);
		upper_wall_1_r2.setPivot(-20, -14, 0);
		head.addChild(upper_wall_1_r2);
		setRotationAngle(upper_wall_1_r2, 0, 0, 0.1107F);
		upper_wall_1_r2.setTextureOffset(304, 260).addCuboid(0, -19, -18, 2, 19, 36, 0, false);

		seat_head_1 = new ModelMapper(modelPartData);
		seat_head_1.setPivot(0, 0, 0);
		head.addChild(seat_head_1);
		seat_head_1.setTextureOffset(0, 34).addCuboid(11, -6, -16, 7, 1, 24, 0, false);

		seat_back_c1141a_r2 = new ModelMapper(modelPartData);
		seat_back_c1141a_r2.setPivot(18, -6.5F, 0);
		seat_head_1.addChild(seat_back_c1141a_r2);
		setRotationAngle(seat_back_c1141a_r2, 0, 0, 0.0873F);
		seat_back_c1141a_r2.setTextureOffset(254, 71).addCuboid(-1, -6, -16, 1, 4, 24, 0, false);
		seat_back_c1141a_r2.setTextureOffset(170, 178).addCuboid(-1, -8, -16, 1, 8, 24, 0, false);

		seat_head_2 = new ModelMapper(modelPartData);
		seat_head_2.setPivot(0, 0, 0);
		head.addChild(seat_head_2);


		seat_back_c1141a_r3 = new ModelMapper(modelPartData);
		seat_back_c1141a_r3.setPivot(-18, -6.5F, 0);
		seat_head_2.addChild(seat_back_c1141a_r3);
		setRotationAngle(seat_back_c1141a_r3, 0, 3.1416F, -0.1047F);
		seat_back_c1141a_r3.setTextureOffset(254, 71).addCuboid(-1, -6, -8, 1, 4, 24, 0, false);
		seat_back_c1141a_r3.setTextureOffset(170, 178).addCuboid(-1, -8, -8, 1, 8, 24, 0, false);

		seat_bottom_r1 = new ModelMapper(modelPartData);
		seat_bottom_r1.setPivot(0, 0, 0);
		seat_head_2.addChild(seat_bottom_r1);
		setRotationAngle(seat_bottom_r1, 0, 3.1416F, 0);
		seat_bottom_r1.setTextureOffset(0, 34).addCuboid(11, -6, -8, 7, 1, 24, 0, false);

		head_exterior = new ModelMapper(modelPartData);
		head_exterior.setPivot(0, 24, 0);
		head_exterior.setTextureOffset(224, 0).addCuboid(-21, 0, 8, 42, 7, 24, 0, false);
		head_exterior.setTextureOffset(58, 188).addCuboid(-21, 0, -16, 1, 7, 24, 0, false);
		head_exterior.setTextureOffset(40, 326).addCuboid(-18, -36, 9, 36, 36, 0, 0, false);
		head_exterior.setTextureOffset(116, 251).addCuboid(-20, -14, -18, 2, 14, 36, 0, false);
		head_exterior.setTextureOffset(116, 251).addCuboid(18, -14, -18, 2, 14, 36, 0, true);
		head_exterior.setTextureOffset(200, 49).addCuboid(-20.8F, -14, 16, 1, 14, 16, 0, false);
		head_exterior.setTextureOffset(350, 22).addCuboid(19.8F, -14, 16, 1, 14, 16, 0, true);
		head_exterior.setTextureOffset(252, 31).addCuboid(-18, -43.9F, 8, 36, 12, 26, 0, false);

		outer_roof_1_r3 = new ModelMapper(modelPartData);
		outer_roof_1_r3.setPivot(-20, -14, 0);
		head_exterior.addChild(outer_roof_1_r3);
		setRotationAngle(outer_roof_1_r3, 0, 0, 0.1107F);
		outer_roof_1_r3.setTextureOffset(76, 319).addCuboid(-1, -26, -16, 1, 8, 49, 0, false);
		outer_roof_1_r3.setTextureOffset(286, 86).addCuboid(0, -19, -18, 2, 19, 36, 0, false);

		outer_roof_2_r5 = new ModelMapper(modelPartData);
		outer_roof_2_r5.setPivot(20, -14, 0);
		head_exterior.addChild(outer_roof_2_r5);
		setRotationAngle(outer_roof_2_r5, 0, 0, -0.1107F);
		outer_roof_2_r5.setTextureOffset(76, 319).addCuboid(0, -26, -16, 1, 8, 49, 0, true);
		outer_roof_2_r5.setTextureOffset(286, 192).addCuboid(-2, -19, -18, 2, 19, 36, 0, true);

		driver_door_top_2_r1 = new ModelMapper(modelPartData);
		driver_door_top_2_r1.setPivot(20.8F, -14, 16);
		head_exterior.addChild(driver_door_top_2_r1);
		setRotationAngle(driver_door_top_2_r1, 0, 0, -0.1107F);
		driver_door_top_2_r1.setTextureOffset(116, 178).addCuboid(-1, -19, 0, 1, 19, 16, 0, true);

		driver_door_top_1_r1 = new ModelMapper(modelPartData);
		driver_door_top_1_r1.setPivot(-20.8F, -14, 16);
		head_exterior.addChild(driver_door_top_1_r1);
		setRotationAngle(driver_door_top_1_r1, 0, 0, 0.1107F);
		driver_door_top_1_r1.setTextureOffset(194, 122).addCuboid(0, -19, 0, 1, 19, 16, 0, false);

		floor_2_r1 = new ModelMapper(modelPartData);
		floor_2_r1.setPivot(0, 0, 0);
		head_exterior.addChild(floor_2_r1);
		setRotationAngle(floor_2_r1, 0, 3.1416F, 0);
		floor_2_r1.setTextureOffset(58, 188).addCuboid(-21, 0, -8, 1, 7, 24, 0, false);

		front = new ModelMapper(modelPartData);
		front.setPivot(0, 0, 0);
		head_exterior.addChild(front);


		front_middle_r1_r1 = new ModelMapper(modelPartData);
		front_middle_r1_r1.setPivot(0, 2.5717F, 45.8123F);
		front.addChild(front_middle_r1_r1);
		setRotationAngle(front_middle_r1_r1, 0.3491F, 0, 0);
		front_middle_r1_r1.setTextureOffset(0, 66).addCuboid(-10, -44.5717F, 8.1877F, 20, 44, 0, 0, false);

		front_roof_r1_r1 = new ModelMapper(modelPartData);
		front_roof_r1_r1.setPivot(0, -44, 34);
		front.addChild(front_roof_r1_r1);
		setRotationAngle(front_roof_r1_r1, 1.0472F, 0, 0);
		front_roof_r1_r1.setTextureOffset(224, 12).addCuboid(-6, 0, 0, 12, 6, 0, 0, false);

		bottom_r1_r1 = new ModelMapper(modelPartData);
		bottom_r1_r1.setPivot(0, 7, 32);
		front.addChild(bottom_r1_r1);
		setRotationAngle(bottom_r1_r1, -1.3526F, 0, 0);
		bottom_r1_r1.setTextureOffset(332, 0).addCuboid(-21, -22, 0, 42, 22, 0, 0, false);

		front_bottom_r1_r1 = new ModelMapper(modelPartData);
		front_bottom_r1_r1.setPivot(0, -9.1588F, 1.0997F);
		front.addChild(front_bottom_r1_r1);
		setRotationAngle(front_bottom_r1_r1, -0.1745F, 0, 0);
		front_bottom_r1_r1.setTextureOffset(0, 59).addCuboid(-10, -0.7936F, 52.9026F, 20, 5, 0, 0, false);

		front_side_1_r1_r1 = new ModelMapper(modelPartData);
		front_side_1_r1_r1.setPivot(-13.6605F, -21.1866F, 43.5706F);
		front.addChild(front_side_1_r1_r1);
		setRotationAngle(front_side_1_r1_r1, 0.3491F, -0.5236F, -0.0873F);
		front_side_1_r1_r1.setTextureOffset(0, 265).addCuboid(-6.5F, -22, 0, 13, 44, 0, 0, false);

		front_side_2_r1_r1 = new ModelMapper(modelPartData);
		front_side_2_r1_r1.setPivot(13.6605F, -21.1866F, 43.5706F);
		front.addChild(front_side_2_r1_r1);
		setRotationAngle(front_side_2_r1_r1, 0.3491F, 0.5236F, 0.0873F);
		front_side_2_r1_r1.setTextureOffset(192, 251).addCuboid(-6.5F, -22, 0, 13, 44, 0, 0, false);

		bottom_side_1_r1_r1 = new ModelMapper(modelPartData);
		bottom_side_1_r1_r1.setPivot(-20.8F, -14, 32);
		front.addChild(bottom_side_1_r1_r1);
		setRotationAngle(bottom_side_1_r1_r1, 0, -1.309F, 0);
		bottom_side_1_r1_r1.setTextureOffset(200, 82).addCuboid(0, 0, 0, 19, 21, 0, 0, false);

		bottom_side_2_r1_r1 = new ModelMapper(modelPartData);
		bottom_side_2_r1_r1.setPivot(20.8F, -14, 32);
		front.addChild(bottom_side_2_r1_r1);
		setRotationAngle(bottom_side_2_r1_r1, 0, 1.309F, 0);
		bottom_side_2_r1_r1.setTextureOffset(130, 148).addCuboid(-19, 0, 0, 19, 21, 0, 0, false);

		top_side_1_r1_r1 = new ModelMapper(modelPartData);
		top_side_1_r1_r1.setPivot(-20.8F, -14, 32);
		front.addChild(top_side_1_r1_r1);
		setRotationAngle(top_side_1_r1_r1, 0, -1.309F, 0.1107F);
		top_side_1_r1_r1.setTextureOffset(252, 69).addCuboid(0, -26, 0, 13, 26, 0, 0, false);

		top_side_2_r1_r1 = new ModelMapper(modelPartData);
		top_side_2_r1_r1.setPivot(20.8F, -14, 32);
		front.addChild(top_side_2_r1_r1);
		setRotationAngle(top_side_2_r1_r1, 0, 1.309F, -0.1107F);
		top_side_2_r1_r1.setTextureOffset(228, 192).addCuboid(-13, -26, 0, 13, 26, 0, 0, false);

		roof_side_1_r1_r1 = new ModelMapper(modelPartData);
		roof_side_1_r1_r1.setPivot(-6, -44, 34);
		front.addChild(roof_side_1_r1_r1);
		setRotationAngle(roof_side_1_r1_r1, 1.0472F, 0, -0.1745F);
		roof_side_1_r1_r1.setTextureOffset(224, 6).addCuboid(-8, 0, 0, 8, 6, 0, 0, false);

		roof_side_2_r1_r1 = new ModelMapper(modelPartData);
		roof_side_2_r1_r1.setPivot(6, -44, 34);
		front.addChild(roof_side_2_r1_r1);
		setRotationAngle(roof_side_2_r1_r1, 1.0472F, 0, 0.1745F);
		roof_side_2_r1_r1.setTextureOffset(224, 0).addCuboid(0, 0, 0, 8, 6, 0, 0, false);

		roof_middle_corner_1_r1_r1 = new ModelMapper(modelPartData);
		roof_middle_corner_1_r1_r1.setPivot(-14.8022F, -41.2114F, 35.299F);
		front.addChild(roof_middle_corner_1_r1_r1);
		setRotationAngle(roof_middle_corner_1_r1_r1, 1.0472F, 0, -0.5236F);
		roof_middle_corner_1_r1_r1.setTextureOffset(0, 52).addCuboid(-1.5F, -1.5F, 0, 3, 3, 0, 0, false);

		roof_middle_corner_2_r1_r1 = new ModelMapper(modelPartData);
		roof_middle_corner_2_r1_r1.setPivot(14.8022F, -41.2114F, 35.299F);
		front.addChild(roof_middle_corner_2_r1_r1);
		setRotationAngle(roof_middle_corner_2_r1_r1, 1.0472F, 0, 0.5236F);
		roof_middle_corner_2_r1_r1.setTextureOffset(0, 55).addCuboid(-1.5F, -1.5F, 0, 3, 3, 0, 0, false);

		roof_corner_1_r1_r1 = new ModelMapper(modelPartData);
		roof_corner_1_r1_r1.setPivot(-16.7925F, -39.5614F, 34.8655F);
		front.addChild(roof_corner_1_r1_r1);
		setRotationAngle(roof_corner_1_r1_r1, 1.0472F, 0, -1.0472F);
		roof_corner_1_r1_r1.setTextureOffset(30, 30).addCuboid(-1.5F, -1, 0, 3, 2, 0, 0, false);

		roof_corner_2_r1_r1 = new ModelMapper(modelPartData);
		roof_corner_2_r1_r1.setPivot(16.7929F, -39.5622F, 34.866F);
		front.addChild(roof_corner_2_r1_r1);
		setRotationAngle(roof_corner_2_r1_r1, 1.0472F, 0, 1.0472F);
		roof_corner_2_r1_r1.setTextureOffset(24, 30).addCuboid(-1.5F, -1, 0, 3, 2, 0, 0, false);

		bottom_corner_1_r1_r1 = new ModelMapper(modelPartData);
		bottom_corner_1_r1_r1.setPivot(21.913F, -11.9098F, 14.4897F);
		front.addChild(bottom_corner_1_r1_r1);
		setRotationAngle(bottom_corner_1_r1_r1, -0.1745F, -0.5236F, -0.0873F);
		bottom_corner_1_r1_r1.setTextureOffset(114, 37).addCuboid(-17.951F, -0.4819F, 50.7099F, 9, 5, 0, 0, false);

		bottom_corner_2_r1_r1 = new ModelMapper(modelPartData);
		bottom_corner_2_r1_r1.setPivot(-21.913F, -11.9098F, 14.4897F);
		front.addChild(bottom_corner_2_r1_r1);
		setRotationAngle(bottom_corner_2_r1_r1, -0.1745F, 0.5236F, 0.0873F);
		bottom_corner_2_r1_r1.setTextureOffset(114, 32).addCuboid(8.951F, -0.4819F, 50.7099F, 9, 5, 0, 0, false);

		top_handrail_head_sp1900 = new ModelMapper(modelPartData);
		top_handrail_head_sp1900.setPivot(0, 24, 0);
		top_handrail_head_sp1900.setTextureOffset(0, 0).addCuboid(-5, -36, 9.8F, 0, 3, 0, 0.2F, false);
		top_handrail_head_sp1900.setTextureOffset(0, 0).addCuboid(-5, -36, -9.8F, 0, 3, 0, 0.2F, false);

		top_handrail_bottom_left_r3 = new ModelMapper(modelPartData);
		top_handrail_bottom_left_r3.setPivot(-5, -31.0876F, 3.8875F);
		top_handrail_head_sp1900.addChild(top_handrail_bottom_left_r3);
		setRotationAngle(top_handrail_bottom_left_r3, -1.5708F, 0, 0);
		top_handrail_bottom_left_r3.setTextureOffset(0, 0).addCuboid(0, -4, 0, 0, 8, 0, 0.2F, false);

		top_handrail_bottom_right_r3 = new ModelMapper(modelPartData);
		top_handrail_bottom_right_r3.setPivot(-5, -31.0876F, -3.8876F);
		top_handrail_head_sp1900.addChild(top_handrail_bottom_right_r3);
		setRotationAngle(top_handrail_bottom_right_r3, -1.5708F, 0, 0);
		top_handrail_bottom_right_r3.setTextureOffset(0, 0).addCuboid(0, -4, 0, 0, 8, 0, 0.2F, false);

		top_handrail_right_3_r3 = new ModelMapper(modelPartData);
		top_handrail_right_3_r3.setPivot(-5, -31.4108F, -8.5938F);
		top_handrail_head_sp1900.addChild(top_handrail_right_3_r3);
		setRotationAngle(top_handrail_right_3_r3, 1.0472F, 0, 0);
		top_handrail_right_3_r3.setTextureOffset(0, 0).addCuboid(0, -0.5F, 0, 0, 1, 0, 0.2F, false);

		top_handrail_right_2_r2 = new ModelMapper(modelPartData);
		top_handrail_right_2_r2.setPivot(-4.8F, -32.8F, -10);
		top_handrail_head_sp1900.addChild(top_handrail_right_2_r2);
		setRotationAngle(top_handrail_right_2_r2, 0.5236F, 0, 0);
		top_handrail_right_2_r2.setTextureOffset(0, 0).addCuboid(-0.2F, 0.2F, 0.2F, 0, 1, 0, 0.2F, false);

		top_handrail_left_3_r3 = new ModelMapper(modelPartData);
		top_handrail_left_3_r3.setPivot(-5, -31.4108F, 8.5938F);
		top_handrail_head_sp1900.addChild(top_handrail_left_3_r3);
		setRotationAngle(top_handrail_left_3_r3, -1.0472F, 0, 0);
		top_handrail_left_3_r3.setTextureOffset(0, 0).addCuboid(0, -0.5F, 0, 0, 1, 0, 0.2F, false);

		top_handrail_left_2_r2 = new ModelMapper(modelPartData);
		top_handrail_left_2_r2.setPivot(-4.8F, -32.8F, 10);
		top_handrail_head_sp1900.addChild(top_handrail_left_2_r2);
		setRotationAngle(top_handrail_left_2_r2, -0.5236F, 0, 0);
		top_handrail_left_2_r2.setTextureOffset(0, 0).addCuboid(-0.2F, 0.2F, -0.2F, 0, 1, 0, 0.2F, false);

		handrail_strap_head = new ModelMapper(modelPartData);
		handrail_strap_head.setPivot(0, 0, 0);
		top_handrail_head_sp1900.addChild(handrail_strap_head);
		handrail_strap_head.setTextureOffset(12, 12).addCuboid(-6, -32, -6, 2, 4, 0, 0, false);
		handrail_strap_head.setTextureOffset(12, 12).addCuboid(-6, -32, 0, 2, 4, 0, 0, false);
		handrail_strap_head.setTextureOffset(12, 12).addCuboid(-6, -32, 6, 2, 4, 0, 0, false);

		top_handrail_head_c1141a = new ModelMapper(modelPartData);
		top_handrail_head_c1141a.setPivot(0, 24, 0);
		top_handrail_head_c1141a.setTextureOffset(0, 50).addCuboid(-5, -31, 15.8F, 5, 0, 0, 0.2F, false);
		top_handrail_head_c1141a.setTextureOffset(0, 50).addCuboid(0, -31, 15.8F, 5, 0, 0, 0.2F, true);
		top_handrail_head_c1141a.setTextureOffset(0, 0).addCuboid(-4.727F, -36.6045F, -3.1124F, 0, 3, 0, 0.2F, false);
		top_handrail_head_c1141a.setTextureOffset(0, 0).addCuboid(4.727F, -36.6045F, -3.1124F, 0, 3, 0, 0.2F, true);
		top_handrail_head_c1141a.setTextureOffset(0, 0).addCuboid(-4.727F, -36.6045F, 11, 0, 3, 0, 0.2F, false);
		top_handrail_head_c1141a.setTextureOffset(0, 0).addCuboid(4.727F, -36.6045F, 11, 0, 3, 0, 0.2F, true);
		top_handrail_head_c1141a.setTextureOffset(0, 0).addCuboid(0, -36.6046F, 13.6145F, 0, 3, 0, 0.2F, false);
		top_handrail_head_c1141a.setTextureOffset(0, 0).addCuboid(0, -36, 11, 0, 7, 0, 0.2F, false);
		top_handrail_head_c1141a.setTextureOffset(8, 7).addCuboid(0, -23.2645F, 10.437F, 0, 6, 0, 0.2F, false);
		top_handrail_head_c1141a.setTextureOffset(8, 7).addCuboid(0, -23.2645F, 11.563F, 0, 6, 0, 0.2F, false);
		top_handrail_head_c1141a.setTextureOffset(0, 0).addCuboid(0, -12, 11, 0, 12, 0, 0.2F, false);

		pole_bottom_diagonal_3_r1 = new ModelMapper(modelPartData);
		pole_bottom_diagonal_3_r1.setPivot(0, -14.4002F, 11.2819F);
		top_handrail_head_c1141a.addChild(pole_bottom_diagonal_3_r1);
		setRotationAngle(pole_bottom_diagonal_3_r1, -0.1047F, 0, 0);
		pole_bottom_diagonal_3_r1.setTextureOffset(11, 28).addCuboid(0, -2.5F, 0, 0, 5, 0, 0.2F, false);

		pole_bottom_diagonal_2_r2 = new ModelMapper(modelPartData);
		pole_bottom_diagonal_2_r2.setPivot(0, -14.4002F, 10.7181F);
		top_handrail_head_c1141a.addChild(pole_bottom_diagonal_2_r2);
		setRotationAngle(pole_bottom_diagonal_2_r2, 0.1047F, 0, 0);
		pole_bottom_diagonal_2_r2.setTextureOffset(11, 28).addCuboid(0, -2.5F, 0, 0, 5, 0, 0.2F, false);

		pole_top_diagonal_3_r1 = new ModelMapper(modelPartData);
		pole_top_diagonal_3_r1.setPivot(0.2F, -28.8F, 10.8F);
		top_handrail_head_c1141a.addChild(pole_top_diagonal_3_r1);
		setRotationAngle(pole_top_diagonal_3_r1, 0.1047F, 0, 0);
		pole_top_diagonal_3_r1.setTextureOffset(11, 11).addCuboid(-0.2F, 0.2069F, 0.2F, 0, 5, 0, 0.2F, false);

		pole_top_diagonal_2_r2 = new ModelMapper(modelPartData);
		pole_top_diagonal_2_r2.setPivot(0.2F, -28.8F, 11.2F);
		top_handrail_head_c1141a.addChild(pole_top_diagonal_2_r2);
		setRotationAngle(pole_top_diagonal_2_r2, -0.1047F, 0, 0);
		pole_top_diagonal_2_r2.setTextureOffset(11, 11).addCuboid(-0.2F, 0.2069F, -0.2F, 0, 5, 0, 0.2F, false);

		top_handrail_connector_bottom_5_r1 = new ModelMapper(modelPartData);
		top_handrail_connector_bottom_5_r1.setPivot(0, -32.2308F, 14.6605F);
		top_handrail_head_c1141a.addChild(top_handrail_connector_bottom_5_r1);
		setRotationAngle(top_handrail_connector_bottom_5_r1, 0.6981F, 0, 0);
		top_handrail_connector_bottom_5_r1.setTextureOffset(0, 0).addCuboid(0, -1.5F, 0, 0, 3, 0, 0.2F, false);

		top_handrail_connector_bottom_right_4_r1 = new ModelMapper(modelPartData);
		top_handrail_connector_bottom_right_4_r1.setPivot(5.7729F, -32.2308F, 0);
		top_handrail_head_c1141a.addChild(top_handrail_connector_bottom_right_4_r1);
		setRotationAngle(top_handrail_connector_bottom_right_4_r1, 0, 0, -0.6981F);
		top_handrail_connector_bottom_right_4_r1.setTextureOffset(0, 0).addCuboid(0, -1.5F, 11, 0, 3, 0, 0.2F, false);

		top_handrail_connector_bottom_left_4_r1 = new ModelMapper(modelPartData);
		top_handrail_connector_bottom_left_4_r1.setPivot(-5.7729F, -32.2308F, 0);
		top_handrail_head_c1141a.addChild(top_handrail_connector_bottom_left_4_r1);
		setRotationAngle(top_handrail_connector_bottom_left_4_r1, 0, 0, 0.6981F);
		top_handrail_connector_bottom_left_4_r1.setTextureOffset(0, 0).addCuboid(0, -1.5F, 11, 0, 3, 0, 0.2F, false);

		top_handrail_connector_bottom_right_3_r1 = new ModelMapper(modelPartData);
		top_handrail_connector_bottom_right_3_r1.setPivot(5.7729F, -32.2308F, 0);
		top_handrail_head_c1141a.addChild(top_handrail_connector_bottom_right_3_r1);
		setRotationAngle(top_handrail_connector_bottom_right_3_r1, 0, 0, -0.6981F);
		top_handrail_connector_bottom_right_3_r1.setTextureOffset(0, 0).addCuboid(0, -1.5F, -3.1124F, 0, 3, 0, 0.2F, false);

		top_handrail_connector_bottom_left_3_r1 = new ModelMapper(modelPartData);
		top_handrail_connector_bottom_left_3_r1.setPivot(-5.7729F, -32.2308F, 0);
		top_handrail_head_c1141a.addChild(top_handrail_connector_bottom_left_3_r1);
		setRotationAngle(top_handrail_connector_bottom_left_3_r1, 0, 0, 0.6981F);
		top_handrail_connector_bottom_left_3_r1.setTextureOffset(0, 0).addCuboid(0, -1.5F, -3.1124F, 0, 3, 0, 0.2F, false);

		top_handrail_bottom_right_r4 = new ModelMapper(modelPartData);
		top_handrail_bottom_right_r4.setPivot(6.9124F, -31, 6.8876F);
		top_handrail_head_c1141a.addChild(top_handrail_bottom_right_r4);
		setRotationAngle(top_handrail_bottom_right_r4, -1.5708F, 0, 0);
		top_handrail_bottom_right_r4.setTextureOffset(0, 0).addCuboid(0, -7, 0, 0, 17, 0, 0.2F, true);
		top_handrail_bottom_right_r4.setTextureOffset(0, 0).addCuboid(-13.8249F, -7, 0, 0, 17, 0, 0.2F, false);

		top_handrail_right_5_r1 = new ModelMapper(modelPartData);
		top_handrail_right_5_r1.setPivot(6.5892F, -31, 14.5938F);
		top_handrail_head_c1141a.addChild(top_handrail_right_5_r1);
		setRotationAngle(top_handrail_right_5_r1, -1.5708F, -0.5236F, 0);
		top_handrail_right_5_r1.setTextureOffset(0, 0).addCuboid(0, -0.5F, 0, 0, 1, 0, 0.2F, true);

		top_handrail_right_4_r2 = new ModelMapper(modelPartData);
		top_handrail_right_4_r2.setPivot(5.7062F, -31, 15.4768F);
		top_handrail_head_c1141a.addChild(top_handrail_right_4_r2);
		setRotationAngle(top_handrail_right_4_r2, -1.5708F, -1.0472F, 0);
		top_handrail_right_4_r2.setTextureOffset(0, 0).addCuboid(0, -0.5F, 0, 0, 1, 0, 0.2F, true);

		top_handrail_left_5_r1 = new ModelMapper(modelPartData);
		top_handrail_left_5_r1.setPivot(-6.5892F, -31, 14.5938F);
		top_handrail_head_c1141a.addChild(top_handrail_left_5_r1);
		setRotationAngle(top_handrail_left_5_r1, -1.5708F, 0.5236F, 0);
		top_handrail_left_5_r1.setTextureOffset(0, 0).addCuboid(0, -0.5F, 0, 0, 1, 0, 0.2F, false);

		top_handrail_left_4_r2 = new ModelMapper(modelPartData);
		top_handrail_left_4_r2.setPivot(-5.7062F, -31, 15.4768F);
		top_handrail_head_c1141a.addChild(top_handrail_left_4_r2);
		setRotationAngle(top_handrail_left_4_r2, -1.5708F, 1.0472F, 0);
		top_handrail_left_4_r2.setTextureOffset(0, 0).addCuboid(0, -0.5F, 0, 0, 1, 0, 0.2F, false);

		handrail_strap_3 = new ModelMapper(modelPartData);
		handrail_strap_3.setPivot(0, 0, 0);
		top_handrail_head_c1141a.addChild(handrail_strap_3);
		handrail_strap_3.setTextureOffset(12, 12).addCuboid(-8, -32, -1, 2, 4, 0, 0, false);
		handrail_strap_3.setTextureOffset(12, 12).addCuboid(6, -32, -1, 2, 4, 0, 0, false);
		handrail_strap_3.setTextureOffset(12, 12).addCuboid(-8, -32, 4, 2, 4, 0, 0, false);
		handrail_strap_3.setTextureOffset(12, 12).addCuboid(6, -32, 4, 2, 4, 0, 0, false);
		handrail_strap_3.setTextureOffset(12, 12).addCuboid(-8, -32, 9, 2, 4, 0, 0, false);
		handrail_strap_3.setTextureOffset(12, 12).addCuboid(6, -32, 9, 2, 4, 0, 0, false);
		handrail_strap_3.setTextureOffset(12, 12).addCuboid(-8, -32, 14, 2, 4, 0, 0, false);
		handrail_strap_3.setTextureOffset(12, 12).addCuboid(6, -32, 14, 2, 4, 0, 0, false);

		handrail_strap_right_8_r1 = new ModelMapper(modelPartData);
		handrail_strap_right_8_r1.setPivot(0, 0, 0);
		handrail_strap_3.addChild(handrail_strap_right_8_r1);
		setRotationAngle(handrail_strap_right_8_r1, 0, 1.5708F, 0);
		handrail_strap_right_8_r1.setTextureOffset(12, 12).addCuboid(-16.8F, -32, 3, 2, 4, 0, 0, false);

		handrail_strap_left_8_r1 = new ModelMapper(modelPartData);
		handrail_strap_left_8_r1.setPivot(0, 0, 0);
		handrail_strap_3.addChild(handrail_strap_left_8_r1);
		setRotationAngle(handrail_strap_left_8_r1, 0, -1.5708F, 0);
		handrail_strap_left_8_r1.setTextureOffset(12, 12).addCuboid(14.8F, -32, 3, 2, 4, 0, 0, false);

		headlights = new ModelMapper(modelPartData);
		headlights.setPivot(0, 24, 0);


		headlight_4_r1_r1 = new ModelMapper(modelPartData);
		headlight_4_r1_r1.setPivot(17.943F, 10.9921F, 21.2198F);
		headlights.addChild(headlight_4_r1_r1);
		setRotationAngle(headlight_4_r1_r1, 0.3491F, -0.5236F, -0.0873F);
		headlight_4_r1_r1.setTextureOffset(16, 34).addCuboid(-12, -15.5F, 43.4F, 4, 6, 0, 0, true);

		headlight_3_r1_r1 = new ModelMapper(modelPartData);
		headlight_3_r1_r1.setPivot(0, 14.3777F, 10.093F);
		headlights.addChild(headlight_3_r1_r1);
		setRotationAngle(headlight_3_r1_r1, 0.3491F, 0, 0);
		headlight_3_r1_r1.setTextureOffset(18, 16).addCuboid(-12, -16, 45.7F, 7, 6, 0, 0, true);
		headlight_3_r1_r1.setTextureOffset(18, 16).addCuboid(5, -16, 45.7F, 7, 6, 0, 0, false);

		headlight_1_r1_r1 = new ModelMapper(modelPartData);
		headlight_1_r1_r1.setPivot(-17.943F, 10.9921F, 21.2198F);
		headlights.addChild(headlight_1_r1_r1);
		setRotationAngle(headlight_1_r1_r1, 0.3491F, 0.5236F, 0.0873F);
		headlight_1_r1_r1.setTextureOffset(16, 34).addCuboid(8, -15.5F, 43.4F, 4, 6, 0, 0, false);

		tail_lights = new ModelMapper(modelPartData);
		tail_lights.setPivot(0, 24, 0);


		tail_light_4_r1_r1 = new ModelMapper(modelPartData);
		tail_light_4_r1_r1.setPivot(17.943F, 10.9921F, 21.2198F);
		tail_lights.addChild(tail_light_4_r1_r1);
		setRotationAngle(tail_light_4_r1_r1, 0.3491F, -0.5236F, -0.0873F);
		tail_light_4_r1_r1.setTextureOffset(16, 40).addCuboid(-12, -15.5F, 43.4F, 4, 6, 0, 0, true);

		tail_light_3_r1_r1 = new ModelMapper(modelPartData);
		tail_light_3_r1_r1.setPivot(0, 14.3777F, 10.093F);
		tail_lights.addChild(tail_light_3_r1_r1);
		setRotationAngle(tail_light_3_r1_r1, 0.3491F, 0, 0);
		tail_light_3_r1_r1.setTextureOffset(18, 22).addCuboid(-12, -16, 45.7F, 7, 6, 0, 0, true);
		tail_light_3_r1_r1.setTextureOffset(18, 22).addCuboid(5, -16, 45.7F, 7, 6, 0, 0, false);

		tail_light_1_r1_r1 = new ModelMapper(modelPartData);
		tail_light_1_r1_r1.setPivot(-17.943F, 10.9921F, 21.2198F);
		tail_lights.addChild(tail_light_1_r1_r1);
		setRotationAngle(tail_light_1_r1_r1, 0.3491F, 0.5236F, 0.0873F);
		tail_light_1_r1_r1.setTextureOffset(16, 40).addCuboid(8, -15.5F, 43.4F, 4, 6, 0, 0, false);

		door_light_on = new ModelMapper(modelPartData);
		door_light_on.setPivot(0, 24, 0);


		light_r1 = new ModelMapper(modelPartData);
		light_r1.setPivot(-20, -14, 0);
		door_light_on.addChild(light_r1);
		setRotationAngle(light_r1, 0, 0, 0.1107F);
		light_r1.setTextureOffset(82, 0).addCuboid(-1, -21.5F, 0, 0, 0, 0, 0.5F, false);

		door_light_off = new ModelMapper(modelPartData);
		door_light_off.setPivot(0, 24, 0);


		light_r2 = new ModelMapper(modelPartData);
		light_r2.setPivot(-20, -14, 0);
		door_light_off.addChild(light_r2);
		setRotationAngle(light_r2, 0, 0, 0.1107F);
		light_r2.setTextureOffset(86, 0).addCuboid(-1, -21.5F, 0, 0, 0, 0, 0.5F, false);

		bb_main = new ModelMapper(modelPartData);
		bb_main.setPivot(0, 24, 0);
		bb_main.setTextureOffset(4, 0).addCuboid(0, -36, 0, 0, 36, 0, 0.2F, false);

		final ModelPart modelPart = TexturedModelData.of(modelData, textureWidth, textureHeight).createModel();
		window.setModelPart(modelPart);
		window_exterior_1.setModelPart(modelPart);
		window_exterior_2.setModelPart(modelPart);
		side_panel_sp1900.setModelPart(modelPart);
		side_panel_c1141a.setModelPart(modelPart);
		door.setModelPart(modelPart);
		door_left.setModelPart(modelPart.getChild(door.name));
		door_right.setModelPart(modelPart.getChild(door.name));
		door_light_c1141a.setModelPart(modelPart);
		door_exterior_1.setModelPart(modelPart);
		door_left_exterior_1.setModelPart(modelPart.getChild(door_exterior_1.name));
		door_right_exterior_1.setModelPart(modelPart.getChild(door_exterior_1.name));
		door_exterior_2.setModelPart(modelPart);
		door_left_exterior_2.setModelPart(modelPart.getChild(door_exterior_2.name));
		door_right_exterior_2.setModelPart(modelPart.getChild(door_exterior_2.name));
		end.setModelPart(modelPart);
		end_exterior.setModelPart(modelPart);
		roof_sp1900.setModelPart(modelPart);
		roof_c1141a.setModelPart(modelPart);
		roof_exterior.setModelPart(modelPart);
		roof_light_sp1900.setModelPart(modelPart);
		roof_light_c1141a.setModelPart(modelPart);
		roof_end_sp1900.setModelPart(modelPart);
		roof_end_c1141a.setModelPart(modelPart);
		roof_end_exterior.setModelPart(modelPart);
		roof_end_light_sp1900.setModelPart(modelPart);
		roof_end_light_c1141a.setModelPart(modelPart);
		top_handrail_sp1900.setModelPart(modelPart);
		top_handrail_c1141a.setModelPart(modelPart);
		tv_pole.setModelPart(modelPart);
		head.setModelPart(modelPart);
		head_exterior.setModelPart(modelPart);
		top_handrail_head_sp1900.setModelPart(modelPart);
		top_handrail_head_c1141a.setModelPart(modelPart);
		headlights.setModelPart(modelPart);
		tail_lights.setModelPart(modelPart);
		door_light_on.setModelPart(modelPart);
		door_light_off.setModelPart(modelPart);
		bb_main.setModelPart(modelPart);
	}

	private static final int DOOR_MAX = 14;
	private static final ModelDoorOverlay MODEL_DOOR_OVERLAY = new ModelDoorOverlay(DOOR_MAX, 6.34F, "door_overlay_sp1900_left.png", "door_overlay_sp1900_right.png");
	private static final ModelDoorOverlayTopSP1900 MODEL_DOOR_OVERLAY_TOP = new ModelDoorOverlayTopSP1900();

	@Override
	protected void renderWindowPositions(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
		switch (renderStage) {
			case LIGHTS:
				renderMirror(isC1141A ? roof_light_c1141a : roof_light_sp1900, matrices, vertices, light, position);
				break;
			case INTERIOR:
				renderMirror(window, matrices, vertices, light, position);
				if (renderDetails) {
					renderMirror(isC1141A ? roof_c1141a : roof_sp1900, matrices, vertices, light, position);
					renderMirror(isC1141A ? top_handrail_c1141a : top_handrail_sp1900, matrices, vertices, light, position);
					if (isC1141A) {
						renderMirror(side_panel_c1141a, matrices, vertices, light, position - 12);
						renderMirror(side_panel_c1141a, matrices, vertices, light, position + 12);
					} else {
						renderMirror(side_panel_sp1900, matrices, vertices, light, position - 15.9F);
						renderMirror(side_panel_sp1900, matrices, vertices, light, position + 15.9F);
					}
				}
				break;
			case INTERIOR_TRANSLUCENT:
				if (isC1141A) {
					renderMirror(side_panel_c1141a_translucent, matrices, vertices, light, position - 12);
					renderMirror(side_panel_c1141a_translucent, matrices, vertices, light, position + 12);
				} else {
					renderMirror(side_panel_sp1900_translucent, matrices, vertices, light, position - 15.9F);
					renderMirror(side_panel_sp1900_translucent, matrices, vertices, light, position + 15.9F);
				}
				break;
			case EXTERIOR:
				if (isEnd2Head) {
					renderOnceFlipped(window_exterior_1, matrices, vertices, light, position);
					renderOnceFlipped(window_exterior_2, matrices, vertices, light, position);
				} else {
					renderOnce(window_exterior_1, matrices, vertices, light, position);
					renderOnce(window_exterior_2, matrices, vertices, light, position);
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
				renderMirror(isC1141A ? roof_light_c1141a : roof_light_sp1900, matrices, vertices, light, position);
				if (isC1141A) {
					renderMirror(door_light_c1141a, matrices, vertices, light, position);
				}
				if (middleDoor && doorOpen && renderDetails) {
					renderMirror(door_light_on, matrices, vertices, light, position - 32);
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
					renderMirror(isC1141A ? roof_c1141a : roof_sp1900, matrices, vertices, light, position);
					if (!isC1141A) {
						if (getDoorPositions().length > 3 && (isIndex(1, position, getDoorPositions()) || isIndex(3, position, getDoorPositions()))) {
							renderOnce(bb_main, matrices, vertices, light, position);
						} else {
							renderOnce(tv_pole, matrices, vertices, light, position);
						}
					}
				}

				break;
			case EXTERIOR:
				if (isEnd2Head) {
					door_left_exterior_2.setOffset(0, 0, doorLeftZ);
					door_right_exterior_2.setOffset(0, 0, -doorLeftZ);
					renderOnceFlipped(door_exterior_2, matrices, vertices, light, position);
					door_left_exterior_1.setOffset(0, 0, -doorRightZ);
					door_right_exterior_1.setOffset(0, 0, doorRightZ);
					renderOnceFlipped(door_exterior_1, matrices, vertices, light, position);
				} else {
					door_left_exterior_1.setOffset(0, 0, -doorLeftZ);
					door_right_exterior_1.setOffset(0, 0, doorLeftZ);
					renderOnce(door_exterior_1, matrices, vertices, light, position);
					door_left_exterior_2.setOffset(0, 0, doorRightZ);
					door_right_exterior_2.setOffset(0, 0, -doorRightZ);
					renderOnce(door_exterior_2, matrices, vertices, light, position);
				}
				renderMirror(roof_exterior, matrices, vertices, light, position);
				if (middleDoor && !doorOpen && renderDetails) {
					renderMirror(door_light_off, matrices, vertices, light, position - 32);
				}
				break;
		}
	}

	@Override
	protected void renderHeadPosition1(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean useHeadlights) {
		switch (renderStage) {
			case LIGHTS:
				renderOnce(isC1141A ? roof_end_light_c1141a : roof_end_light_sp1900, matrices, vertices, light, position);
				break;
			case ALWAYS_ON_LIGHTS:
				renderOnceFlipped(useHeadlights ? headlights : tail_lights, matrices, vertices, light, position);
				break;
			case INTERIOR:
				renderOnceFlipped(head, matrices, vertices, light, position);
				if (renderDetails) {
					renderOnce(isC1141A ? roof_end_c1141a : roof_end_sp1900, matrices, vertices, light, position);
					if (isC1141A) {
						renderMirror(side_panel_c1141a, matrices, vertices, light, position + 16);
					} else {
						renderMirror(top_handrail_head_sp1900, matrices, vertices, light, position + 6);
						renderMirror(side_panel_sp1900, matrices, vertices, light, position + 15.9F);
					}
				}
				break;
			case INTERIOR_TRANSLUCENT:
				if (isC1141A) {
					renderMirror(side_panel_c1141a_translucent, matrices, vertices, light, position + 16);
				} else {
					renderMirror(side_panel_sp1900_translucent, matrices, vertices, light, position + 15.9F);
				}
				break;
			case EXTERIOR:
				renderOnceFlipped(head_exterior, matrices, vertices, light, position);
				renderOnce(roof_end_exterior, matrices, vertices, light, position + 2);
				break;
		}
	}

	@Override
	protected void renderHeadPosition2(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean useHeadlights) {
		switch (renderStage) {
			case LIGHTS:
				renderOnce(isC1141A ? roof_end_light_c1141a : roof_end_light_sp1900, matrices, vertices, light, position);
				break;
			case ALWAYS_ON_LIGHTS:
				renderOnce(useHeadlights ? headlights : tail_lights, matrices, vertices, light, position);
				break;
			case INTERIOR:
				renderOnce(head, matrices, vertices, light, position);
				if (renderDetails) {
					renderOnceFlipped(isC1141A ? roof_end_c1141a : roof_end_sp1900, matrices, vertices, light, position);
					if (isC1141A) {
						renderMirror(side_panel_c1141a, matrices, vertices, light, position - 16);
					} else {
						renderMirror(top_handrail_head_sp1900, matrices, vertices, light, position - 6);
						renderMirror(side_panel_sp1900, matrices, vertices, light, position - 15.9F);
					}
				}
				break;
			case INTERIOR_TRANSLUCENT:
				if (isC1141A) {
					renderMirror(side_panel_c1141a_translucent, matrices, vertices, light, position - 16);
				} else {
					renderMirror(side_panel_sp1900_translucent, matrices, vertices, light, position - 15.9F);
				}
				break;
			case EXTERIOR:
				renderOnce(head_exterior, matrices, vertices, light, position);
				renderOnceFlipped(roof_end_exterior, matrices, vertices, light, position - 2);
				break;
		}
	}

	@Override
	protected void renderEndPosition1(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
		switch (renderStage) {
			case LIGHTS:
				renderOnce(isC1141A ? roof_end_light_c1141a : roof_end_light_sp1900, matrices, vertices, light, position);
				break;
			case INTERIOR:
				renderOnce(end, matrices, vertices, light, position);
				if (renderDetails) {
					renderOnce(isC1141A ? roof_end_c1141a : roof_end_sp1900, matrices, vertices, light, position);
					if (isC1141A) {
						renderOnce(top_handrail_head_c1141a, matrices, vertices, light, position);
						renderMirror(side_panel_c1141a, matrices, vertices, light, position + 16);
					} else {
						renderMirror(top_handrail_sp1900, matrices, vertices, light, position);
						renderMirror(side_panel_sp1900, matrices, vertices, light, position + 15.9F);
					}
				}
				break;
			case INTERIOR_TRANSLUCENT:
				if (isC1141A) {
					renderMirror(side_panel_c1141a_translucent, matrices, vertices, light, position + 16);
				} else {
					renderMirror(side_panel_sp1900_translucent, matrices, vertices, light, position + 15.9F);
				}
				break;
			case EXTERIOR:
				renderOnce(end_exterior, matrices, vertices, light, position);
				renderOnce(roof_end_exterior, matrices, vertices, light, position);
				break;
		}
	}

	@Override
	protected void renderEndPosition2(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
		switch (renderStage) {
			case LIGHTS:
				renderOnceFlipped(isC1141A ? roof_end_light_c1141a : roof_end_light_sp1900, matrices, vertices, light, position);
				break;
			case INTERIOR:
				renderOnceFlipped(end, matrices, vertices, light, position);
				if (renderDetails) {
					renderOnceFlipped(isC1141A ? roof_end_c1141a : roof_end_sp1900, matrices, vertices, light, position);
					if (isC1141A) {
						renderOnceFlipped(top_handrail_head_c1141a, matrices, vertices, light, position);
						renderMirror(side_panel_c1141a, matrices, vertices, light, position - 16);
					} else {
						renderMirror(top_handrail_sp1900, matrices, vertices, light, position);
						renderMirror(side_panel_sp1900, matrices, vertices, light, position - 15.9F);
					}
				}
				break;
			case INTERIOR_TRANSLUCENT:
				if (isC1141A) {
					renderMirror(side_panel_c1141a_translucent, matrices, vertices, light, position - 16);
				} else {
					renderMirror(side_panel_sp1900_translucent, matrices, vertices, light, position - 15.9F);
				}
				break;
			case EXTERIOR:
				renderOnceFlipped(end_exterior, matrices, vertices, light, position);
				renderOnceFlipped(roof_end_exterior, matrices, vertices, light, position);
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
		return smoothEnds(0, DOOR_MAX, 0, 0.5F, value);
	}
}