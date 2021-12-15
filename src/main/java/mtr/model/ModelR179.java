package mtr.model;

import minecraftmappings.ModelDataWrapper;
import minecraftmappings.ModelMapper;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class ModelR179 extends ModelTrainBase {

	private final ModelMapper window;
	private final ModelMapper wall_1_r1;
	private final ModelMapper window_handrails;
	private final ModelMapper seat;
	private final ModelMapper seat_bottom_r1;
	private final ModelMapper seat_back_3_r1;
	private final ModelMapper handrail_window;
	private final ModelMapper handrail_top_1_r1;
	private final ModelMapper handrail_mid;
	private final ModelMapper handrail_middle_4_r1;
	private final ModelMapper handrail_middle_3_r1;
	private final ModelMapper handrail_middle_2_r1;
	private final ModelMapper handrail_turn_1_r1;
	private final ModelMapper headrail_right;
	private final ModelMapper handrail_right_3_r1;
	private final ModelMapper handrail_right_1_r1;
	private final ModelMapper handrail_turn_4_r1;
	private final ModelMapper handrail_turn_3_r1;
	private final ModelMapper handrail_turn_2_r1;
	private final ModelMapper handrail_turn_1_r2;
	private final ModelMapper headrail_left;
	private final ModelMapper handrail_left_3_r1;
	private final ModelMapper handrail_left_1_r1;
	private final ModelMapper handrail_turn_4_r2;
	private final ModelMapper handrail_turn_3_r2;
	private final ModelMapper handrail_turn_2_r2;
	private final ModelMapper handrail_turn_1_r3;
	private final ModelMapper headrail_up;
	private final ModelMapper handrail_up_11_r1;
	private final ModelMapper handrail_up_10_r1;
	private final ModelMapper window_display;
	private final ModelMapper display_window_r1;
	private final ModelMapper window_exterior;
	private final ModelMapper upper_wall_r1;
	private final ModelMapper side_panel;
	private final ModelMapper door;
	private final ModelMapper door_right;
	private final ModelMapper door_right_top_r1;
	private final ModelMapper door_left;
	private final ModelMapper door_side_top_r1;
	private final ModelMapper door_exterior;
	private final ModelMapper door_right_exterior;
	private final ModelMapper door_right_top_r2;
	private final ModelMapper door_left_exterior;
	private final ModelMapper door_left_top_r1;
	private final ModelMapper door_sides;
	private final ModelMapper door_side_top_1_r1;
	private final ModelMapper end;
	private final ModelMapper upper_wall_2_r1;
	private final ModelMapper upper_wall_1_r1;
	private final ModelMapper end_handrails;
	private final ModelMapper end_mid_roof_3_r1;
	private final ModelMapper end_mid_roof_4_r1;
	private final ModelMapper end_side_1;
	private final ModelMapper seat_bottom_r2;
	private final ModelMapper seat_back_4_r1;
	private final ModelMapper end_side_2;
	private final ModelMapper seat_bottom_r3;
	private final ModelMapper seat_back_3_r2;
	private final ModelMapper handrail_end_1;
	private final ModelMapper handrail_top_6_r1;
	private final ModelMapper handrail_top_5_r1;
	private final ModelMapper handrail_top_4_r1;
	private final ModelMapper handrail_right_4_r1;
	private final ModelMapper handrail_right_2_r1;
	private final ModelMapper handrail_turn_5_r1;
	private final ModelMapper handrail_turn_4_r3;
	private final ModelMapper handrail_turn_3_r3;
	private final ModelMapper handrail_turn_2_r3;
	private final ModelMapper handrail_end_2;
	private final ModelMapper handrail_top_5_r2;
	private final ModelMapper handrail_top_4_r2;
	private final ModelMapper handrail_top_3_r1;
	private final ModelMapper handrail_right_3_r2;
	private final ModelMapper handrail_right_1_r2;
	private final ModelMapper handrail_turn_4_r4;
	private final ModelMapper handrail_turn_3_r4;
	private final ModelMapper handrail_turn_2_r4;
	private final ModelMapper handrail_turn_1_r4;
	private final ModelMapper end_exterior;
	private final ModelMapper upper_wall_2_r2;
	private final ModelMapper upper_wall_1_r2;
	private final ModelMapper end_bottom_out;
	private final ModelMapper buttom_panel_right_4_r1;
	private final ModelMapper buttom_panel_right_3_r1;
	private final ModelMapper buttom_panel_right_2_r1;
	private final ModelMapper buttom_panel_left_4_r1;
	private final ModelMapper buttom_panel_left_3_r1;
	private final ModelMapper buttom_panel_left_2_r1;
	private final ModelMapper end_back;
	private final ModelMapper front_right_panel_4_r1;
	private final ModelMapper front_right_panel_3_r1;
	private final ModelMapper front_right_panel_2_r1;
	private final ModelMapper front_right_panel_3_r2;
	private final ModelMapper front_right_panel_2_r2;
	private final ModelMapper front_right_panel_1_r1;
	private final ModelMapper roof_end_exterior;
	private final ModelMapper outer_roof_6_r1;
	private final ModelMapper outer_roof_5_r1;
	private final ModelMapper outer_roof_4_r1;
	private final ModelMapper outer_roof_3_r1;
	private final ModelMapper outer_roof_2_r1;
	private final ModelMapper outer_roof_5_r2;
	private final ModelMapper outer_roof_4_r2;
	private final ModelMapper outer_roof_3_r2;
	private final ModelMapper outer_roof_2_r2;
	private final ModelMapper outer_roof_1_r1;
	private final ModelMapper head;
	private final ModelMapper upper_wall_2_r3;
	private final ModelMapper upper_wall_1_r3;
	private final ModelMapper head_exterior;
	private final ModelMapper upper_wall_2_r4;
	private final ModelMapper upper_wall_1_r4;
	private final ModelMapper head_bottom_out;
	private final ModelMapper buttom_panel_right_5_r1;
	private final ModelMapper buttom_panel_right_4_r2;
	private final ModelMapper buttom_panel_right_3_r2;
	private final ModelMapper buttom_panel_left_5_r1;
	private final ModelMapper buttom_panel_left_4_r2;
	private final ModelMapper buttom_panel_left_3_r2;
	private final ModelMapper head_back;
	private final ModelMapper front_right_panel_5_r1;
	private final ModelMapper front_right_panel_4_r2;
	private final ModelMapper front_right_panel_3_r3;
	private final ModelMapper front_right_panel_4_r3;
	private final ModelMapper front_right_panel_3_r4;
	private final ModelMapper front_right_panel_2_r3;
	private final ModelMapper roof_head_exterior;
	private final ModelMapper outer_roof_7_r1;
	private final ModelMapper outer_roof_6_r2;
	private final ModelMapper outer_roof_5_r3;
	private final ModelMapper outer_roof_4_r3;
	private final ModelMapper outer_roof_3_r3;
	private final ModelMapper outer_roof_6_r3;
	private final ModelMapper outer_roof_5_r4;
	private final ModelMapper outer_roof_4_r4;
	private final ModelMapper outer_roof_3_r4;
	private final ModelMapper outer_roof_2_r3;
	private final ModelMapper roof_window;
	private final ModelMapper inner_roof_4_r1;
	private final ModelMapper inner_roof_2_r1;
	private final ModelMapper roof_door;
	private final ModelMapper inner_roof_4_r2;
	private final ModelMapper inner_roof_2_r2;
	private final ModelMapper roof_end;
	private final ModelMapper inner_roof_9_r1;
	private final ModelMapper inner_roof_7_r1;
	private final ModelMapper inner_roof_4_r3;
	private final ModelMapper inner_roof_2_r3;
	private final ModelMapper roof_exterior_window;
	private final ModelMapper outer_roof_5_r5;
	private final ModelMapper outer_roof_4_r5;
	private final ModelMapper outer_roof_3_r5;
	private final ModelMapper outer_roof_2_r4;
	private final ModelMapper outer_roof_1_r2;
	private final ModelMapper roof_exterior_door;
	private final ModelMapper outer_roof_6_r4;
	private final ModelMapper outer_roof_5_r6;
	private final ModelMapper outer_roof_4_r6;
	private final ModelMapper outer_roof_3_r6;
	private final ModelMapper outer_roof_2_r5;
	private final ModelMapper roof_head;
	private final ModelMapper inner_roof_9_r2;
	private final ModelMapper inner_roof_7_r2;
	private final ModelMapper inner_roof_4_r4;
	private final ModelMapper inner_roof_2_r4;
	private final ModelMapper roof_window_light;
	private final ModelMapper roof_door_light;
	private final ModelMapper roof_end_light;
	private final ModelMapper roof_head_light;
	private final ModelMapper handrail_door_type_1;
	private final ModelMapper handrail_door_type_2;
	private final ModelMapper handrail_curve;
	private final ModelMapper handrail_curve_12_r1;
	private final ModelMapper handrail_curve_10_r1;
	private final ModelMapper handrail_curve_9_r1;
	private final ModelMapper handrail_curve_7_r1;
	private final ModelMapper handrail_curve_6_r1;
	private final ModelMapper handrail_curve_5_r1;
	private final ModelMapper handrail_curve_3_r1;
	private final ModelMapper handrail_curve_11_r1;
	private final ModelMapper handrail_curve_9_r2;
	private final ModelMapper handrail_curve_8_r1;
	private final ModelMapper handrail_curve_5_r2;
	private final ModelMapper handrail_curve_4_r1;
	private final ModelMapper handrail_curve_2_r1;
	private final ModelMapper roof_handle;
	private final ModelMapper roof_handrail_curve_19_r1;
	private final ModelMapper roof_handrail_curve_22_r1;
	private final ModelMapper roof_handrail_curve_23_r1;
	private final ModelMapper roof_handrail_curve_18_r1;
	private final ModelMapper roof_handrail_curve_21_r1;
	private final ModelMapper roof_handrail_curve_22_r2;
	private final ModelMapper roof_handrail_curve_18_r2;
	private final ModelMapper roof_handrail_curve_21_r2;
	private final ModelMapper roof_handrail_curve_22_r3;
	private final ModelMapper roof_handrail_curve_17_r1;
	private final ModelMapper roof_handrail_curve_20_r1;
	private final ModelMapper roof_handrail_curve_21_r3;
	private final ModelMapper roof_handrail_2_r1;
	private final ModelMapper headlights;
	private final ModelMapper front_right_panel_6_r1;
	private final ModelMapper front_right_panel_5_r2;
	private final ModelMapper front_right_panel_4_r4;
	private final ModelMapper front_right_panel_3_r5;
	private final ModelMapper tail_lights;
	private final ModelMapper front_right_panel_5_r3;
	private final ModelMapper front_right_panel_4_r5;
	private final ModelMapper front_right_panel_3_r6;
	private final ModelMapper front_right_panel_2_r4;
	private final ModelMapper door_light;
	private final ModelMapper light_plate_1_r1;
	private final ModelMapper door_light_on;
	private final ModelMapper light_r1;
	private final ModelMapper door_light_off;
	private final ModelMapper light_r2;

	public ModelR179() {
		final int textureWidth = 368;
		final int textureHeight = 368;

		final ModelDataWrapper modelDataWrapper = new ModelDataWrapper(this, textureWidth, textureHeight);

		window = new ModelMapper(modelDataWrapper);
		window.setPivot(0, 24, 0);
		window.setTextureOffset(120, 0).addCuboid(-20, 0, -24, 20, 1, 48, 0, false);
		window.setTextureOffset(0, 78).addCuboid(-21.5F, -13, -28.5F, 2, 13, 57, 0, false);

		wall_1_r1 = new ModelMapper(modelDataWrapper);
		wall_1_r1.setPivot(-21.5F, -13, 0);
		window.addChild(wall_1_r1);
		setRotationAngle(wall_1_r1, 0, 0, 0.1047F);
		wall_1_r1.setTextureOffset(0, 0).addCuboid(0, -21, -28.5F, 2, 21, 57, 0, false);

		window_handrails = new ModelMapper(modelDataWrapper);
		window_handrails.setPivot(0, 24, 0);


		seat = new ModelMapper(modelDataWrapper);
		seat.setPivot(0, 0, 0);
		window_handrails.addChild(seat);
		seat.setTextureOffset(124, 155).addCuboid(-19.9F, -10.75F, -26.5F, 2, 5, 53, 0, false);
		seat.setTextureOffset(58, 171).addCuboid(-19.55F, -6, -26, 3, 4, 52, 0, false);

		seat_bottom_r1 = new ModelMapper(modelDataWrapper);
		seat_bottom_r1.setPivot(0, -1.75F, 0);
		seat.addChild(seat_bottom_r1);
		setRotationAngle(seat_bottom_r1, 0, 0, -0.0873F);
		seat_bottom_r1.setTextureOffset(120, 101).addCuboid(-19.9F, -6, -26.5F, 9, 1, 53, 0, false);

		seat_back_3_r1 = new ModelMapper(modelDataWrapper);
		seat_back_3_r1.setPivot(-17.9F, -10.75F, 0);
		seat.addChild(seat_back_3_r1);
		setRotationAngle(seat_back_3_r1, 0, 0, -0.1309F);
		seat_back_3_r1.setTextureOffset(0, 148).addCuboid(-2, -5, -26.5F, 2, 5, 53, 0, false);

		handrail_window = new ModelMapper(modelDataWrapper);
		handrail_window.setPivot(0, 0, 0);
		window_handrails.addChild(handrail_window);


		handrail_top_1_r1 = new ModelMapper(modelDataWrapper);
		handrail_top_1_r1.setPivot(-12.8F, -31.75F, 0);
		handrail_window.addChild(handrail_top_1_r1);
		setRotationAngle(handrail_top_1_r1, -1.5708F, 0, 0);
		handrail_top_1_r1.setTextureOffset(0, 0).addCuboid(0, -24.5F, 0, 0, 49, 0, 0.2F, false);

		handrail_mid = new ModelMapper(modelDataWrapper);
		handrail_mid.setPivot(-39.95F, -9.875F, 38.1F);
		handrail_window.addChild(handrail_mid);
		handrail_mid.setTextureOffset(0, 0).addCuboid(23.5101F, 5.4649F, -38.1F, 5, 0, 0, 0.2F, false);

		handrail_middle_4_r1 = new ModelMapper(modelDataWrapper);
		handrail_middle_4_r1.setPivot(27.8083F, -20.505F, -38.1F);
		handrail_mid.addChild(handrail_middle_4_r1);
		setRotationAngle(handrail_middle_4_r1, 0, 0, 1.1606F);
		handrail_middle_4_r1.setTextureOffset(0, 0).addCuboid(-1.5F, 0, 0, 3, 0, 0, 0.2F, false);

		handrail_middle_3_r1 = new ModelMapper(modelDataWrapper);
		handrail_middle_3_r1.setPivot(29.7F, -13.725F, -38.1F);
		handrail_mid.addChild(handrail_middle_3_r1);
		setRotationAngle(handrail_middle_3_r1, 0, 0, 1.3788F);
		handrail_middle_3_r1.setTextureOffset(0, 0).addCuboid(-5.2F, 0.2F, 0, 5, 0, 0, 0.2F, false);

		handrail_middle_2_r1 = new ModelMapper(modelDataWrapper);
		handrail_middle_2_r1.setPivot(29.5F, -4.525F, -38.1F);
		handrail_mid.addChild(handrail_middle_2_r1);
		setRotationAngle(handrail_middle_2_r1, 0, 0, 1.5708F);
		handrail_middle_2_r1.setTextureOffset(0, 0).addCuboid(-9, 0, 0, 18, 0, 0, 0.2F, false);

		handrail_turn_1_r1 = new ModelMapper(modelDataWrapper);
		handrail_turn_1_r1.setPivot(29.7F, 4.675F, -38.3F);
		handrail_mid.addChild(handrail_turn_1_r1);
		setRotationAngle(handrail_turn_1_r1, 0, -1.5708F, 2.3562F);
		handrail_turn_1_r1.setTextureOffset(0, 0).addCuboid(0.2F, 0.2F, -1.2F, 0, 0, 1, 0.2F, false);

		headrail_right = new ModelMapper(modelDataWrapper);
		headrail_right.setPivot(-39.95F, -9.875F, 37.1F);
		handrail_window.addChild(headrail_right);


		handrail_right_3_r1 = new ModelMapper(modelDataWrapper);
		handrail_right_3_r1.setPivot(23.3F, -0.225F, -12.2F);
		headrail_right.addChild(handrail_right_3_r1);
		setRotationAngle(handrail_right_3_r1, 0, 0, 1.5708F);
		handrail_right_3_r1.setTextureOffset(0, 0).addCuboid(4, -5, 1.6F, 0, 0, 0, 0.2F, false);
		handrail_right_3_r1.setTextureOffset(0, 0).addCuboid(-15, -5, 2, 19, 0, 0, 0.2F, false);

		handrail_right_1_r1 = new ModelMapper(modelDataWrapper);
		handrail_right_1_r1.setPivot(28.06F, -17.5269F, -10.2F);
		headrail_right.addChild(handrail_right_1_r1);
		setRotationAngle(handrail_right_1_r1, 0, 0, 1.4573F);
		handrail_right_1_r1.setTextureOffset(0, 0).addCuboid(-1, 0, 0, 3, 0, 0, 0.2F, false);

		handrail_turn_4_r1 = new ModelMapper(modelDataWrapper);
		handrail_turn_4_r1.setPivot(27.9F, -18.7556F, -10.237F);
		headrail_right.addChild(handrail_turn_4_r1);
		setRotationAngle(handrail_turn_4_r1, -1.3099F, 0.0441F, -0.1412F);
		handrail_turn_4_r1.setTextureOffset(0, 0).addCuboid(0, 0, 0, 0, 0, 0, 0.2F, false);

		handrail_turn_3_r1 = new ModelMapper(modelDataWrapper);
		handrail_turn_3_r1.setPivot(27.65F, -19.8179F, -10.7262F);
		headrail_right.addChild(handrail_turn_3_r1);
		setRotationAngle(handrail_turn_3_r1, -1.1436F, 0.0916F, -0.1983F);
		handrail_turn_3_r1.setTextureOffset(0, 0).addCuboid(0, 0, -1, 0, 0, 2, 0.2F, false);

		handrail_turn_2_r1 = new ModelMapper(modelDataWrapper);
		handrail_turn_2_r1.setPivot(27.375F, -20.9556F, -11.312F);
		headrail_right.addChild(handrail_turn_2_r1);
		setRotationAngle(handrail_turn_2_r1, -0.7494F, 0.1284F, -0.1186F);
		handrail_turn_2_r1.setTextureOffset(0, 0).addCuboid(0, 0, 0, 0, 0, 0, 0.2F, false);

		handrail_turn_1_r2 = new ModelMapper(modelDataWrapper);
		handrail_turn_1_r2.setPivot(27.25F, -21.4912F, -11.9782F);
		headrail_right.addChild(handrail_turn_1_r2);
		setRotationAngle(handrail_turn_1_r2, -0.6584F, 0.1103F, -0.0706F);
		handrail_turn_1_r2.setTextureOffset(0, 0).addCuboid(0, 0, -0.5F, 0, 0, 1, 0.2F, false);

		headrail_left = new ModelMapper(modelDataWrapper);
		headrail_left.setPivot(-39.95F, -9.875F, 39.1F);
		handrail_window.addChild(headrail_left);


		handrail_left_3_r1 = new ModelMapper(modelDataWrapper);
		handrail_left_3_r1.setPivot(23.3F, -0.225F, -64);
		headrail_left.addChild(handrail_left_3_r1);
		setRotationAngle(handrail_left_3_r1, 0, 0, 1.5708F);
		handrail_left_3_r1.setTextureOffset(0, 0).addCuboid(4, -5, -1.6F, 0, 0, 0, 0.2F, false);
		handrail_left_3_r1.setTextureOffset(0, 0).addCuboid(-15, -5, -2, 19, 0, 0, 0.2F, false);

		handrail_left_1_r1 = new ModelMapper(modelDataWrapper);
		handrail_left_1_r1.setPivot(28.06F, -17.5269F, -66);
		headrail_left.addChild(handrail_left_1_r1);
		setRotationAngle(handrail_left_1_r1, 0, 0, 1.4573F);
		handrail_left_1_r1.setTextureOffset(0, 0).addCuboid(-1, 0, 0, 3, 0, 0, 0.2F, false);

		handrail_turn_4_r2 = new ModelMapper(modelDataWrapper);
		handrail_turn_4_r2.setPivot(27.9F, -18.7556F, -65.963F);
		headrail_left.addChild(handrail_turn_4_r2);
		setRotationAngle(handrail_turn_4_r2, 1.3099F, -0.0441F, -0.1412F);
		handrail_turn_4_r2.setTextureOffset(0, 0).addCuboid(0, 0, 0, 0, 0, 0, 0.2F, false);

		handrail_turn_3_r2 = new ModelMapper(modelDataWrapper);
		handrail_turn_3_r2.setPivot(27.65F, -19.8179F, -65.4738F);
		headrail_left.addChild(handrail_turn_3_r2);
		setRotationAngle(handrail_turn_3_r2, 1.1436F, -0.0916F, -0.1983F);
		handrail_turn_3_r2.setTextureOffset(0, 0).addCuboid(0, 0, -1, 0, 0, 2, 0.2F, false);

		handrail_turn_2_r2 = new ModelMapper(modelDataWrapper);
		handrail_turn_2_r2.setPivot(27.375F, -20.9556F, -64.888F);
		headrail_left.addChild(handrail_turn_2_r2);
		setRotationAngle(handrail_turn_2_r2, 0.7494F, -0.1284F, -0.1186F);
		handrail_turn_2_r2.setTextureOffset(0, 0).addCuboid(0, 0, 0, 0, 0, 0, 0.2F, false);

		handrail_turn_1_r3 = new ModelMapper(modelDataWrapper);
		handrail_turn_1_r3.setPivot(27.25F, -21.4912F, -64.2218F);
		headrail_left.addChild(handrail_turn_1_r3);
		setRotationAngle(handrail_turn_1_r3, 0.6584F, -0.1103F, -0.0706F);
		handrail_turn_1_r3.setTextureOffset(0, 0).addCuboid(0, 0, -0.5F, 0, 0, 1, 0.2F, false);

		headrail_up = new ModelMapper(modelDataWrapper);
		headrail_up.setPivot(-40.95F, -12.875F, 38.1F);
		handrail_window.addChild(headrail_up);
		headrail_up.setTextureOffset(0, 0).addCuboid(30.35F, -22.375F, -15.8F, 0, 1, 0, 0.2F, false);
		headrail_up.setTextureOffset(0, 0).addCuboid(30.35F, -22.375F, -60.4F, 0, 1, 0, 0.2F, false);
		headrail_up.setTextureOffset(0, 0).addCuboid(30.35F, -22.375F, -32.4F, 0, 1, 0, 0.2F, false);
		headrail_up.setTextureOffset(0, 0).addCuboid(30.35F, -22.375F, -43.8F, 0, 1, 0, 0.2F, false);

		handrail_up_11_r1 = new ModelMapper(modelDataWrapper);
		handrail_up_11_r1.setPivot(37.479F, -8.7545F, -46.8F);
		headrail_up.addChild(handrail_up_11_r1);
		setRotationAngle(handrail_up_11_r1, 0, 0, 0.6109F);
		handrail_up_11_r1.setTextureOffset(0, 0).addCuboid(-13, -6, 3, 0, 1, 0, 0.2F, false);
		handrail_up_11_r1.setTextureOffset(0, 0).addCuboid(-13, -6, 14.4F, 0, 1, 0, 0.2F, false);
		handrail_up_11_r1.setTextureOffset(0, 0).addCuboid(-13, -6, -13.6F, 0, 1, 0, 0.2F, false);
		handrail_up_11_r1.setTextureOffset(0, 0).addCuboid(-13, -6, 31, 0, 1, 0, 0.2F, false);

		handrail_up_10_r1 = new ModelMapper(modelDataWrapper);
		handrail_up_10_r1.setPivot(34.65F, -6.875F, -46.8F);
		headrail_up.addChild(handrail_up_10_r1);
		setRotationAngle(handrail_up_10_r1, 0, 0, 0.7854F);
		handrail_up_10_r1.setTextureOffset(0, 0).addCuboid(-13, -6, 3, 0, 2, 0, 0.2F, false);
		handrail_up_10_r1.setTextureOffset(0, 0).addCuboid(-13, -6, 14.4F, 0, 2, 0, 0.2F, false);
		handrail_up_10_r1.setTextureOffset(0, 0).addCuboid(-13, -6, -13.6F, 0, 2, 0, 0.2F, false);
		handrail_up_10_r1.setTextureOffset(0, 0).addCuboid(-13, -6, 31, 0, 2, 0, 0.2F, false);

		window_display = new ModelMapper(modelDataWrapper);
		window_display.setPivot(0, 24, 0);


		display_window_r1 = new ModelMapper(modelDataWrapper);
		display_window_r1.setPivot(-21.5F, -13, 0);
		window_display.addChild(display_window_r1);
		setRotationAngle(display_window_r1, 0, 0, 0.1047F);
		display_window_r1.setTextureOffset(116, 171).addCuboid(0.1F, -19.5F, -14, 2, 6, 28, 0, false);

		window_exterior = new ModelMapper(modelDataWrapper);
		window_exterior.setPivot(0, 24, 0);
		window_exterior.setTextureOffset(181, 155).addCuboid(-21.5F, 0, -24, 1, 4, 48, 0, false);
		window_exterior.setTextureOffset(61, 101).addCuboid(-21.5F, -13, -28.5F, 1, 13, 57, 0, false);

		upper_wall_r1 = new ModelMapper(modelDataWrapper);
		upper_wall_r1.setPivot(-21.5F, -13, 0);
		window_exterior.addChild(upper_wall_r1);
		setRotationAngle(upper_wall_r1, 0, 0, 0.1047F);
		upper_wall_r1.setTextureOffset(61, 21).addCuboid(0, -23, -28.5F, 1, 23, 57, 0, false);

		side_panel = new ModelMapper(modelDataWrapper);
		side_panel.setPivot(0, 24, 0);
		side_panel.setTextureOffset(302, 137).addCuboid(-20, -32, 0, 9, 29, 0, 0, false);

		door = new ModelMapper(modelDataWrapper);
		door.setPivot(0, 24, 0);
		door.setTextureOffset(202, 207).addCuboid(-21, 0, -16, 21, 1, 32, 0, false);

		door_right = new ModelMapper(modelDataWrapper);
		door_right.setPivot(0, 0, 0);
		door.addChild(door_right);
		door_right.setTextureOffset(116, 171).addCuboid(-21, -13, 0, 1, 13, 12, 0, false);

		door_right_top_r1 = new ModelMapper(modelDataWrapper);
		door_right_top_r1.setPivot(-21, -13, 0);
		door_right.addChild(door_right_top_r1);
		setRotationAngle(door_right_top_r1, 0, 0, 0.1047F);
		door_right_top_r1.setTextureOffset(232, 149).addCuboid(0, -21, 0, 1, 21, 12, 0, false);

		door_left = new ModelMapper(modelDataWrapper);
		door_left.setPivot(0, 0, 0);
		door.addChild(door_left);
		door_left.setTextureOffset(148, 171).addCuboid(-21, -13, -12, 1, 13, 12, 0, false);

		door_side_top_r1 = new ModelMapper(modelDataWrapper);
		door_side_top_r1.setPivot(-21, -13, 0);
		door_left.addChild(door_side_top_r1);
		setRotationAngle(door_side_top_r1, 0, 0, 0.1047F);
		door_side_top_r1.setTextureOffset(52, 294).addCuboid(0, -21, -12, 1, 21, 12, 0, false);

		door_exterior = new ModelMapper(modelDataWrapper);
		door_exterior.setPivot(0, 24, 0);


		door_right_exterior = new ModelMapper(modelDataWrapper);
		door_right_exterior.setPivot(0, 0, 0);
		door_exterior.addChild(door_right_exterior);
		door_right_exterior.setTextureOffset(191, 123).addCuboid(-21, -13, 0, 0, 13, 12, 0, false);

		door_right_top_r2 = new ModelMapper(modelDataWrapper);
		door_right_top_r2.setPivot(-21, -13, 0);
		door_right_exterior.addChild(door_right_top_r2);
		setRotationAngle(door_right_top_r2, 0, 0, 0.1047F);
		door_right_top_r2.setTextureOffset(84, 159).addCuboid(0, -21, 0, 0, 21, 12, 0, false);

		door_left_exterior = new ModelMapper(modelDataWrapper);
		door_left_exterior.setPivot(0, 0, 0);
		door_exterior.addChild(door_left_exterior);
		door_left_exterior.setTextureOffset(181, 178).addCuboid(-21, -13, -12, 0, 13, 12, 0, false);

		door_left_top_r1 = new ModelMapper(modelDataWrapper);
		door_left_top_r1.setPivot(-21, -13, 0);
		door_left_exterior.addChild(door_left_top_r1);
		setRotationAngle(door_left_top_r1, 0, 0, 0.1047F);
		door_left_top_r1.setTextureOffset(0, 136).addCuboid(0, -21, -12, 0, 21, 12, 0, false);

		door_sides = new ModelMapper(modelDataWrapper);
		door_sides.setPivot(0, 0, 0);
		door_exterior.addChild(door_sides);
		door_sides.setTextureOffset(245, 240).addCuboid(-21.5F, 0, -17, 1, 4, 34, 0, false);
		door_sides.setTextureOffset(0, 338).addCuboid(-22, 0, -11, 1, 1, 22, 0, false);
		door_sides.setTextureOffset(235, 149).addCuboid(-20, -33, -11.5F, 2, 0, 23, 0, false);

		door_side_top_1_r1 = new ModelMapper(modelDataWrapper);
		door_side_top_1_r1.setPivot(-21.5F, -13, 0);
		door_sides.addChild(door_side_top_1_r1);
		setRotationAngle(door_side_top_1_r1, 0, 0, 0.1047F);
		door_side_top_1_r1.setTextureOffset(2, 30).addCuboid(0, -23, -11.5F, 0, 3, 23, 0, false);

		end = new ModelMapper(modelDataWrapper);
		end.setPivot(0, 24, 0);
		end.setTextureOffset(208, 22).addCuboid(-20, 0, -10, 40, 1, 18, 0, false);
		end.setTextureOffset(191, 287).addCuboid(-7, -33, -10, 14, 33, 0, 0, false);
		end.setTextureOffset(0, 255).addCuboid(7, -33, -10, 13, 33, 1, 0, true);
		end.setTextureOffset(33, 255).addCuboid(-20, -33, -10, 13, 33, 1, 0, false);
		end.setTextureOffset(191, 100).addCuboid(19.5F, -13, -9.5F, 2, 13, 22, 0, true);
		end.setTextureOffset(191, 100).addCuboid(-21.5F, -13, -9.5F, 2, 13, 22, 0, false);

		upper_wall_2_r1 = new ModelMapper(modelDataWrapper);
		upper_wall_2_r1.setPivot(-21.5F, -13, 0);
		end.addChild(upper_wall_2_r1);
		setRotationAngle(upper_wall_2_r1, 0, 0, 0.1047F);
		upper_wall_2_r1.setTextureOffset(0, 87).addCuboid(0, -21, -9.5F, 2, 21, 22, 0, false);

		upper_wall_1_r1 = new ModelMapper(modelDataWrapper);
		upper_wall_1_r1.setPivot(21.5F, -13, 0);
		end.addChild(upper_wall_1_r1);
		setRotationAngle(upper_wall_1_r1, 0, 0, -0.1047F);
		upper_wall_1_r1.setTextureOffset(0, 87).addCuboid(-2, -21, -9.5F, 2, 21, 22, 0, true);

		end_handrails = new ModelMapper(modelDataWrapper);
		end_handrails.setPivot(0, 24, 0);
		end_handrails.setTextureOffset(120, 69).addCuboid(-17, -37.875F, -8, 34, 3, 3, 0, false);
		end_handrails.setTextureOffset(120, 64).addCuboid(-18, -35.875F, -10, 36, 3, 2, 0, false);

		end_mid_roof_3_r1 = new ModelMapper(modelDataWrapper);
		end_mid_roof_3_r1.setPivot(16.05F, -32.95F, 0);
		end_handrails.addChild(end_mid_roof_3_r1);
		setRotationAngle(end_mid_roof_3_r1, 0, 0, 0.2967F);
		end_mid_roof_3_r1.setTextureOffset(0, 130).addCuboid(-8.05F, -2, -8, 8, 2, 3, 0, true);

		end_mid_roof_4_r1 = new ModelMapper(modelDataWrapper);
		end_mid_roof_4_r1.setPivot(-16.05F, -32.95F, 0);
		end_handrails.addChild(end_mid_roof_4_r1);
		setRotationAngle(end_mid_roof_4_r1, 0, 0, -0.2967F);
		end_mid_roof_4_r1.setTextureOffset(0, 130).addCuboid(0.05F, -2, -8, 8, 2, 3, 0, false);

		end_side_1 = new ModelMapper(modelDataWrapper);
		end_side_1.setPivot(0, 0, 0);
		end_handrails.addChild(end_side_1);
		end_side_1.setTextureOffset(0, 206).addCuboid(17.9F, -10.75F, -9.5F, 2, 5, 20, 0, true);
		end_side_1.setTextureOffset(50, 227).addCuboid(16.55F, -6, -9, 3, 4, 19, 0, true);

		seat_bottom_r2 = new ModelMapper(modelDataWrapper);
		seat_bottom_r2.setPivot(0, -1.75F, 16);
		end_side_1.addChild(seat_bottom_r2);
		setRotationAngle(seat_bottom_r2, 0, 0, 0.0873F);
		seat_bottom_r2.setTextureOffset(172, 213).addCuboid(10.9F, -6, -25.5F, 9, 1, 20, 0, true);

		seat_back_4_r1 = new ModelMapper(modelDataWrapper);
		seat_back_4_r1.setPivot(17.9F, -10.75F, 16);
		end_side_1.addChild(seat_back_4_r1);
		setRotationAngle(seat_back_4_r1, 0, 0, 0.1309F);
		seat_back_4_r1.setTextureOffset(177, 64).addCuboid(0, -5, -25.5F, 2, 5, 20, 0, true);

		end_side_2 = new ModelMapper(modelDataWrapper);
		end_side_2.setPivot(0, 0, 0);
		end_handrails.addChild(end_side_2);
		end_side_2.setTextureOffset(0, 206).addCuboid(-19.9F, -10.75F, -9.5F, 2, 5, 20, 0, false);
		end_side_2.setTextureOffset(50, 227).addCuboid(-19.55F, -6, -9, 3, 4, 19, 0, false);

		seat_bottom_r3 = new ModelMapper(modelDataWrapper);
		seat_bottom_r3.setPivot(0, -1.75F, 16);
		end_side_2.addChild(seat_bottom_r3);
		setRotationAngle(seat_bottom_r3, 0, 0, -0.0873F);
		seat_bottom_r3.setTextureOffset(172, 213).addCuboid(-19.9F, -6, -25.5F, 9, 1, 20, 0, false);

		seat_back_3_r2 = new ModelMapper(modelDataWrapper);
		seat_back_3_r2.setPivot(-17.9F, -10.75F, 16);
		end_side_2.addChild(seat_back_3_r2);
		setRotationAngle(seat_back_3_r2, 0, 0, -0.1309F);
		seat_back_3_r2.setTextureOffset(177, 64).addCuboid(-2, -5, -25.5F, 2, 5, 20, 0, false);

		handrail_end_1 = new ModelMapper(modelDataWrapper);
		handrail_end_1.setPivot(-1, 4, -12);
		end_handrails.addChild(handrail_end_1);
		handrail_end_1.setTextureOffset(0, 0).addCuboid(13.8F, -35.75F, 11.5F, 0, 0, 9, 0.2F, false);

		handrail_top_6_r1 = new ModelMapper(modelDataWrapper);
		handrail_top_6_r1.setPivot(11.7943F, -38.3087F, 9.5766F);
		handrail_end_1.addChild(handrail_top_6_r1);
		setRotationAngle(handrail_top_6_r1, 0, 0.7854F, 0);
		handrail_top_6_r1.setTextureOffset(0, 0).addCuboid(0, -0.5F, 0, 0, 1, 0, 0.2F, false);

		handrail_top_5_r1 = new ModelMapper(modelDataWrapper);
		handrail_top_5_r1.setPivot(12.1924F, -37.3779F, 9.807F);
		handrail_end_1.addChild(handrail_top_5_r1);
		setRotationAngle(handrail_top_5_r1, -2.7699F, 0.6484F, -0.6107F);
		handrail_top_5_r1.setTextureOffset(0, 0).addCuboid(0, -0.5F, 0, 0, 1, 0, 0.2F, false);

		handrail_top_4_r1 = new ModelMapper(modelDataWrapper);
		handrail_top_4_r1.setPivot(13.2474F, -36.3462F, 10.7346F);
		handrail_end_1.addChild(handrail_top_4_r1);
		setRotationAngle(handrail_top_4_r1, -0.7247F, 0.5197F, -0.2271F);
		handrail_top_4_r1.setTextureOffset(0, 0).addCuboid(0, 0, -1, 0, 0, 2, 0.2F, false);

		handrail_right_4_r1 = new ModelMapper(modelDataWrapper);
		handrail_right_4_r1.setPivot(17.65F, -14.1F, 20.9F);
		handrail_end_1.addChild(handrail_right_4_r1);
		setRotationAngle(handrail_right_4_r1, 0, 0, -1.5708F);
		handrail_right_4_r1.setTextureOffset(0, 0).addCuboid(-4, -5, 1.6F, 0, 0, 0, 0.2F, false);
		handrail_right_4_r1.setTextureOffset(0, 0).addCuboid(-4, -5, 2, 19, 0, 0, 0.2F, false);

		handrail_right_2_r1 = new ModelMapper(modelDataWrapper);
		handrail_right_2_r1.setPivot(12.89F, -31.4019F, 22.9F);
		handrail_end_1.addChild(handrail_right_2_r1);
		setRotationAngle(handrail_right_2_r1, 0, 0, -1.4573F);
		handrail_right_2_r1.setTextureOffset(0, 0).addCuboid(-2, 0, 0, 3, 0, 0, 0.2F, false);

		handrail_turn_5_r1 = new ModelMapper(modelDataWrapper);
		handrail_turn_5_r1.setPivot(13.05F, -32.6306F, 22.863F);
		handrail_end_1.addChild(handrail_turn_5_r1);
		setRotationAngle(handrail_turn_5_r1, -1.3099F, -0.0441F, 0.1412F);
		handrail_turn_5_r1.setTextureOffset(0, 0).addCuboid(0, 0, 0, 0, 0, 0, 0.2F, false);

		handrail_turn_4_r3 = new ModelMapper(modelDataWrapper);
		handrail_turn_4_r3.setPivot(13.3F, -33.6929F, 22.3738F);
		handrail_end_1.addChild(handrail_turn_4_r3);
		setRotationAngle(handrail_turn_4_r3, -1.1436F, -0.0916F, 0.1983F);
		handrail_turn_4_r3.setTextureOffset(0, 0).addCuboid(0, 0, -1, 0, 0, 2, 0.2F, false);

		handrail_turn_3_r3 = new ModelMapper(modelDataWrapper);
		handrail_turn_3_r3.setPivot(13.575F, -34.8306F, 21.788F);
		handrail_end_1.addChild(handrail_turn_3_r3);
		setRotationAngle(handrail_turn_3_r3, -0.7494F, -0.1284F, 0.1186F);
		handrail_turn_3_r3.setTextureOffset(0, 0).addCuboid(0, 0, 0, 0, 0, 0, 0.2F, false);

		handrail_turn_2_r3 = new ModelMapper(modelDataWrapper);
		handrail_turn_2_r3.setPivot(13.7F, -35.3662F, 21.1218F);
		handrail_end_1.addChild(handrail_turn_2_r3);
		setRotationAngle(handrail_turn_2_r3, -0.6584F, -0.1103F, 0.0706F);
		handrail_turn_2_r3.setTextureOffset(0, 0).addCuboid(0, 0, -0.5F, 0, 0, 1, 0.2F, false);

		handrail_end_2 = new ModelMapper(modelDataWrapper);
		handrail_end_2.setPivot(1, 4, -12);
		end_handrails.addChild(handrail_end_2);
		handrail_end_2.setTextureOffset(0, 0).addCuboid(-13.8F, -35.75F, 11.5F, 0, 0, 9, 0.2F, false);

		handrail_top_5_r2 = new ModelMapper(modelDataWrapper);
		handrail_top_5_r2.setPivot(-11.7943F, -38.3087F, 9.5766F);
		handrail_end_2.addChild(handrail_top_5_r2);
		setRotationAngle(handrail_top_5_r2, 0, -0.7854F, 0);
		handrail_top_5_r2.setTextureOffset(0, 0).addCuboid(0, -0.5F, 0, 0, 1, 0, 0.2F, false);

		handrail_top_4_r2 = new ModelMapper(modelDataWrapper);
		handrail_top_4_r2.setPivot(-12.1924F, -37.3779F, 9.807F);
		handrail_end_2.addChild(handrail_top_4_r2);
		setRotationAngle(handrail_top_4_r2, -2.7699F, -0.6484F, 0.6107F);
		handrail_top_4_r2.setTextureOffset(0, 0).addCuboid(0, -0.5F, 0, 0, 1, 0, 0.2F, false);

		handrail_top_3_r1 = new ModelMapper(modelDataWrapper);
		handrail_top_3_r1.setPivot(-13.2474F, -36.3462F, 10.7346F);
		handrail_end_2.addChild(handrail_top_3_r1);
		setRotationAngle(handrail_top_3_r1, -0.7247F, -0.5197F, 0.2271F);
		handrail_top_3_r1.setTextureOffset(0, 0).addCuboid(0, 0, -1, 0, 0, 2, 0.2F, false);

		handrail_right_3_r2 = new ModelMapper(modelDataWrapper);
		handrail_right_3_r2.setPivot(-17.65F, -14.1F, 20.9F);
		handrail_end_2.addChild(handrail_right_3_r2);
		setRotationAngle(handrail_right_3_r2, 0, 0, 1.5708F);
		handrail_right_3_r2.setTextureOffset(0, 0).addCuboid(4, -5, 1.6F, 0, 0, 0, 0.2F, false);
		handrail_right_3_r2.setTextureOffset(0, 0).addCuboid(-15, -5, 2, 19, 0, 0, 0.2F, false);

		handrail_right_1_r2 = new ModelMapper(modelDataWrapper);
		handrail_right_1_r2.setPivot(-12.89F, -31.4019F, 22.9F);
		handrail_end_2.addChild(handrail_right_1_r2);
		setRotationAngle(handrail_right_1_r2, 0, 0, 1.4573F);
		handrail_right_1_r2.setTextureOffset(0, 0).addCuboid(-1, 0, 0, 3, 0, 0, 0.2F, false);

		handrail_turn_4_r4 = new ModelMapper(modelDataWrapper);
		handrail_turn_4_r4.setPivot(-13.05F, -32.6306F, 22.863F);
		handrail_end_2.addChild(handrail_turn_4_r4);
		setRotationAngle(handrail_turn_4_r4, -1.3099F, 0.0441F, -0.1412F);
		handrail_turn_4_r4.setTextureOffset(0, 0).addCuboid(0, 0, 0, 0, 0, 0, 0.2F, false);

		handrail_turn_3_r4 = new ModelMapper(modelDataWrapper);
		handrail_turn_3_r4.setPivot(-13.3F, -33.6929F, 22.3738F);
		handrail_end_2.addChild(handrail_turn_3_r4);
		setRotationAngle(handrail_turn_3_r4, -1.1436F, 0.0916F, -0.1983F);
		handrail_turn_3_r4.setTextureOffset(0, 0).addCuboid(0, 0, -1, 0, 0, 2, 0.2F, false);

		handrail_turn_2_r4 = new ModelMapper(modelDataWrapper);
		handrail_turn_2_r4.setPivot(-13.575F, -34.8306F, 21.788F);
		handrail_end_2.addChild(handrail_turn_2_r4);
		setRotationAngle(handrail_turn_2_r4, -0.7494F, 0.1284F, -0.1186F);
		handrail_turn_2_r4.setTextureOffset(0, 0).addCuboid(0, 0, 0, 0, 0, 0, 0.2F, false);

		handrail_turn_1_r4 = new ModelMapper(modelDataWrapper);
		handrail_turn_1_r4.setPivot(-13.7F, -35.3662F, 21.1218F);
		handrail_end_2.addChild(handrail_turn_1_r4);
		setRotationAngle(handrail_turn_1_r4, -0.6584F, 0.1103F, -0.0706F);
		handrail_turn_1_r4.setTextureOffset(0, 0).addCuboid(0, 0, -0.5F, 0, 0, 1, 0.2F, false);

		end_exterior = new ModelMapper(modelDataWrapper);
		end_exterior.setPivot(0, 24, 0);
		end_exterior.setTextureOffset(276, 208).addCuboid(20.5F, 0, -12, 1, 4, 20, 0, true);
		end_exterior.setTextureOffset(120, 109).addCuboid(19.5F, -13, -10.5F, 2, 13, 23, 0, true);
		end_exterior.setTextureOffset(276, 208).addCuboid(-21.5F, 0, -12, 1, 4, 20, 0, false);
		end_exterior.setTextureOffset(222, 278).addCuboid(-21.5F, -13, -10.5F, 2, 13, 23, 0, false);

		upper_wall_2_r2 = new ModelMapper(modelDataWrapper);
		upper_wall_2_r2.setPivot(-21.5F, -13, 0);
		end_exterior.addChild(upper_wall_2_r2);
		setRotationAngle(upper_wall_2_r2, 0, 0, 0.1047F);
		upper_wall_2_r2.setTextureOffset(113, 271).addCuboid(0, -23, -11.5F, 2, 23, 24, 0, false);

		upper_wall_1_r2 = new ModelMapper(modelDataWrapper);
		upper_wall_1_r2.setPivot(21.5F, -13, 0);
		end_exterior.addChild(upper_wall_1_r2);
		setRotationAngle(upper_wall_1_r2, 0, 0, -0.1047F);
		upper_wall_1_r2.setTextureOffset(0, 148).addCuboid(-2, -23, -11.5F, 2, 23, 24, 0, true);

		end_bottom_out = new ModelMapper(modelDataWrapper);
		end_bottom_out.setPivot(0, 0.1F, -21);
		end_exterior.addChild(end_bottom_out);
		end_bottom_out.setTextureOffset(120, 56).addCuboid(-19.5F, -0.1F, 5, 39, 3, 5, 0, false);

		buttom_panel_right_4_r1 = new ModelMapper(modelDataWrapper);
		buttom_panel_right_4_r1.setPivot(-12.7131F, 1.4F, 5.8639F);
		end_bottom_out.addChild(buttom_panel_right_4_r1);
		setRotationAngle(buttom_panel_right_4_r1, 0, 0.1745F, 0);
		buttom_panel_right_4_r1.setTextureOffset(32, 130).addCuboid(-1.5F, -1.5F, -0.5F, 4, 3, 1, 0, false);

		buttom_panel_right_3_r1 = new ModelMapper(modelDataWrapper);
		buttom_panel_right_3_r1.setPivot(-15.9855F, 1.4F, 6.7858F);
		end_bottom_out.addChild(buttom_panel_right_3_r1);
		setRotationAngle(buttom_panel_right_3_r1, 0, 0.3491F, 0);
		buttom_panel_right_3_r1.setTextureOffset(134, 145).addCuboid(-2, -1.5F, -0.5F, 4, 3, 1, 0, false);

		buttom_panel_right_2_r1 = new ModelMapper(modelDataWrapper);
		buttom_panel_right_2_r1.setPivot(-21.5F, -0.1F, 9);
		end_bottom_out.addChild(buttom_panel_right_2_r1);
		setRotationAngle(buttom_panel_right_2_r1, 0, 0.5236F, 0);
		buttom_panel_right_2_r1.setTextureOffset(120, 145).addCuboid(0, 0, 0, 4, 3, 3, 0, false);

		buttom_panel_left_4_r1 = new ModelMapper(modelDataWrapper);
		buttom_panel_left_4_r1.setPivot(12.7131F, 1.4F, 5.8639F);
		end_bottom_out.addChild(buttom_panel_left_4_r1);
		setRotationAngle(buttom_panel_left_4_r1, 0, -0.1745F, 0);
		buttom_panel_left_4_r1.setTextureOffset(32, 130).addCuboid(-2.5F, -1.5F, -0.5F, 4, 3, 1, 0, true);

		buttom_panel_left_3_r1 = new ModelMapper(modelDataWrapper);
		buttom_panel_left_3_r1.setPivot(15.9855F, 1.4F, 6.7858F);
		end_bottom_out.addChild(buttom_panel_left_3_r1);
		setRotationAngle(buttom_panel_left_3_r1, 0, -0.3491F, 0);
		buttom_panel_left_3_r1.setTextureOffset(134, 145).addCuboid(-2, -1.5F, -0.5F, 4, 3, 1, 0, true);

		buttom_panel_left_2_r1 = new ModelMapper(modelDataWrapper);
		buttom_panel_left_2_r1.setPivot(21.5F, -0.1F, 9);
		end_bottom_out.addChild(buttom_panel_left_2_r1);
		setRotationAngle(buttom_panel_left_2_r1, 0, -0.5236F, 0);
		buttom_panel_left_2_r1.setTextureOffset(120, 145).addCuboid(-4, 0, 0, 4, 3, 3, 0, true);

		end_back = new ModelMapper(modelDataWrapper);
		end_back.setPivot(0, 0, -21);
		end_exterior.addChild(end_back);
		end_back.setTextureOffset(308, 302).addCuboid(-8, -33, 6, 1, 33, 5, 0, false);
		end_back.setTextureOffset(296, 302).addCuboid(7, -33, 6, 1, 33, 5, 0, false);
		end_back.setTextureOffset(0, 231).addCuboid(-8, -42, 6, 16, 9, 6, 0, false);
		end_back.setTextureOffset(249, 278).addCuboid(-7, -25, 6, 14, 19, 0, 0, false);
		end_back.setTextureOffset(281, 240).addCuboid(-7, -34, 10, 14, 34, 0, 0, false);

		front_right_panel_4_r1 = new ModelMapper(modelDataWrapper);
		front_right_panel_4_r1.setPivot(9.4774F, -21, 6.2595F);
		end_back.addChild(front_right_panel_4_r1);
		setRotationAngle(front_right_panel_4_r1, 0, -0.1745F, 0);
		front_right_panel_4_r1.setTextureOffset(311, 41).addCuboid(-1.5F, -21, 0, 3, 42, 0, 0, false);

		front_right_panel_3_r1 = new ModelMapper(modelDataWrapper);
		front_right_panel_3_r1.setPivot(13.7738F, -20.5F, 7.5461F);
		end_back.addChild(front_right_panel_3_r1);
		setRotationAngle(front_right_panel_3_r1, 0, -0.3491F, 0);
		front_right_panel_3_r1.setTextureOffset(165, 295).addCuboid(-3, -20.5F, 0, 6, 41, 0, 0, false);

		front_right_panel_2_r1 = new ModelMapper(modelDataWrapper);
		front_right_panel_2_r1.setPivot(19.1907F, -20, 10.0731F);
		end_back.addChild(front_right_panel_2_r1);
		setRotationAngle(front_right_panel_2_r1, 0, -0.5236F, 0);
		front_right_panel_2_r1.setTextureOffset(24, 298).addCuboid(-3, -20, 0, 6, 40, 0, 0, false);

		front_right_panel_3_r2 = new ModelMapper(modelDataWrapper);
		front_right_panel_3_r2.setPivot(-9.4774F, -21, 6.2595F);
		end_back.addChild(front_right_panel_3_r2);
		setRotationAngle(front_right_panel_3_r2, 0, 0.1745F, 0);
		front_right_panel_3_r2.setTextureOffset(219, 314).addCuboid(-1.5F, -21, 0, 3, 42, 0, 0, false);

		front_right_panel_2_r2 = new ModelMapper(modelDataWrapper);
		front_right_panel_2_r2.setPivot(-13.7738F, -20.5F, 7.5461F);
		end_back.addChild(front_right_panel_2_r2);
		setRotationAngle(front_right_panel_2_r2, 0, 0.3491F, 0);
		front_right_panel_2_r2.setTextureOffset(177, 295).addCuboid(-3, -20.5F, 0, 6, 41, 0, 0, false);

		front_right_panel_1_r1 = new ModelMapper(modelDataWrapper);
		front_right_panel_1_r1.setPivot(-19.1907F, -20, 10.0731F);
		end_back.addChild(front_right_panel_1_r1);
		setRotationAngle(front_right_panel_1_r1, 0, 0.5236F, 0);
		front_right_panel_1_r1.setTextureOffset(36, 298).addCuboid(-3, -20, 0, 6, 40, 0, 0, false);

		roof_end_exterior = new ModelMapper(modelDataWrapper);
		roof_end_exterior.setPivot(0, 0, 0);
		end_exterior.addChild(roof_end_exterior);
		roof_end_exterior.setTextureOffset(137, 0).addCuboid(-4, -41.375F, -15, 4, 0, 23, 0, false);
		roof_end_exterior.setTextureOffset(137, 0).addCuboid(0, -41.375F, -15, 4, 0, 23, 0, true);

		outer_roof_6_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_6_r1.setPivot(4, -41.375F, 0);
		roof_end_exterior.addChild(outer_roof_6_r1);
		setRotationAngle(outer_roof_6_r1, 0, 0, 0.0873F);
		outer_roof_6_r1.setTextureOffset(125, 0).addCuboid(0, 0, -15, 6, 0, 23, 0, true);

		outer_roof_5_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_5_r1.setPivot(12.392F, -40.205F, 0);
		roof_end_exterior.addChild(outer_roof_5_r1);
		setRotationAngle(outer_roof_5_r1, 0, 0, 0.2618F);
		outer_roof_5_r1.setTextureOffset(125, 23).addCuboid(-2.5F, 0, -15, 5, 0, 23, 0, true);

		outer_roof_4_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_4_r1.setPivot(16.2163F, -39.0449F, 0);
		roof_end_exterior.addChild(outer_roof_4_r1);
		setRotationAngle(outer_roof_4_r1, 0, 0, 0.3491F);
		outer_roof_4_r1.setTextureOffset(113, 101).addCuboid(-1.5F, 0, -15, 3, 0, 23, 0, true);

		outer_roof_3_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_3_r1.setPivot(18.2687F, -37.7659F, 0);
		roof_end_exterior.addChild(outer_roof_3_r1);
		setRotationAngle(outer_roof_3_r1, 0, 0, 0.8727F);
		outer_roof_3_r1.setTextureOffset(3, 78).addCuboid(-1, 0, -15, 2, 0, 23, 0, true);

		outer_roof_2_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_2_r1.setPivot(18.6114F, -35.9228F, 0);
		roof_end_exterior.addChild(outer_roof_2_r1);
		setRotationAngle(outer_roof_2_r1, 0, 0, 1.3788F);
		outer_roof_2_r1.setTextureOffset(57, 171).addCuboid(-1, -0.5F, -15, 2, 1, 23, 0, true);

		outer_roof_5_r2 = new ModelMapper(modelDataWrapper);
		outer_roof_5_r2.setPivot(-4, -41.375F, 0);
		roof_end_exterior.addChild(outer_roof_5_r2);
		setRotationAngle(outer_roof_5_r2, 0, 0, -0.0873F);
		outer_roof_5_r2.setTextureOffset(125, 0).addCuboid(-6, 0, -15, 6, 0, 23, 0, false);

		outer_roof_4_r2 = new ModelMapper(modelDataWrapper);
		outer_roof_4_r2.setPivot(-12.392F, -40.205F, 0);
		roof_end_exterior.addChild(outer_roof_4_r2);
		setRotationAngle(outer_roof_4_r2, 0, 0, -0.2618F);
		outer_roof_4_r2.setTextureOffset(125, 23).addCuboid(-2.5F, 0, -15, 5, 0, 23, 0, false);

		outer_roof_3_r2 = new ModelMapper(modelDataWrapper);
		outer_roof_3_r2.setPivot(-16.2163F, -39.0449F, 0);
		roof_end_exterior.addChild(outer_roof_3_r2);
		setRotationAngle(outer_roof_3_r2, 0, 0, -0.3491F);
		outer_roof_3_r2.setTextureOffset(113, 101).addCuboid(-1.5F, 0, -15, 3, 0, 23, 0, false);

		outer_roof_2_r2 = new ModelMapper(modelDataWrapper);
		outer_roof_2_r2.setPivot(-18.2687F, -37.7659F, 0);
		roof_end_exterior.addChild(outer_roof_2_r2);
		setRotationAngle(outer_roof_2_r2, 0, 0, -0.8727F);
		outer_roof_2_r2.setTextureOffset(3, 78).addCuboid(-1, 0, -15, 2, 0, 23, 0, false);

		outer_roof_1_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_1_r1.setPivot(-18.6114F, -35.9228F, 0);
		roof_end_exterior.addChild(outer_roof_1_r1);
		setRotationAngle(outer_roof_1_r1, 0, 0, -1.3788F);
		outer_roof_1_r1.setTextureOffset(57, 171).addCuboid(-1, -0.5F, -15, 2, 1, 23, 0, false);

		head = new ModelMapper(modelDataWrapper);
		head.setPivot(0, 24, 0);
		head.setTextureOffset(120, 49).addCuboid(-20, 0, 4, 40, 1, 4, 0, false);
		head.setTextureOffset(201, 56).addCuboid(19.5F, -13, 3.5F, 2, 13, 9, 0, true);
		head.setTextureOffset(201, 56).addCuboid(-21.5F, -13, 3.5F, 2, 13, 9, 0, false);
		head.setTextureOffset(243, 100).addCuboid(-19.5F, -37, 4, 39, 37, 0, 0, false);

		upper_wall_2_r3 = new ModelMapper(modelDataWrapper);
		upper_wall_2_r3.setPivot(-21.5F, -13, 0);
		head.addChild(upper_wall_2_r3);
		setRotationAngle(upper_wall_2_r3, 0, 0, 0.1047F);
		upper_wall_2_r3.setTextureOffset(0, 78).addCuboid(0, -21, 3.5F, 2, 21, 9, 0, false);

		upper_wall_1_r3 = new ModelMapper(modelDataWrapper);
		upper_wall_1_r3.setPivot(21.5F, -13, 0);
		head.addChild(upper_wall_1_r3);
		setRotationAngle(upper_wall_1_r3, 0, 0, -0.1047F);
		upper_wall_1_r3.setTextureOffset(0, 78).addCuboid(-2, -21, 3.5F, 2, 21, 9, 0, true);

		head_exterior = new ModelMapper(modelDataWrapper);
		head_exterior.setPivot(0, 24, 0);
		head_exterior.setTextureOffset(0, 263).addCuboid(-21.5F, 0, -23, 1, 4, 31, 0, false);
		head_exterior.setTextureOffset(231, 149).addCuboid(-21.5F, -13, -21.5F, 2, 13, 34, 0, false);
		head_exterior.setTextureOffset(0, 263).addCuboid(20.5F, 0, -23, 1, 4, 31, 0, true);
		head_exterior.setTextureOffset(173, 240).addCuboid(19.5F, -13, -21.5F, 2, 13, 34, 0, true);
		head_exterior.setTextureOffset(22, 336).addCuboid(-20, 0, -22, 40, 1, 26, 0, false);
		head_exterior.setTextureOffset(229, 49).addCuboid(-19.5F, -42, 3, 39, 42, 0, 0, false);

		upper_wall_2_r4 = new ModelMapper(modelDataWrapper);
		upper_wall_2_r4.setPivot(21.5F, -13, -3);
		head_exterior.addChild(upper_wall_2_r4);
		setRotationAngle(upper_wall_2_r4, 0, 0, -0.1047F);
		upper_wall_2_r4.setTextureOffset(63, 236).addCuboid(-2, -23, -19.5F, 2, 23, 35, 0, true);

		upper_wall_1_r4 = new ModelMapper(modelDataWrapper);
		upper_wall_1_r4.setPivot(-21.5F, -13, -3);
		head_exterior.addChild(upper_wall_1_r4);
		setRotationAngle(upper_wall_1_r4, 0, 0, 0.1047F);
		upper_wall_1_r4.setTextureOffset(133, 213).addCuboid(0, -23, -19.5F, 2, 23, 35, 0, false);

		head_bottom_out = new ModelMapper(modelDataWrapper);
		head_bottom_out.setPivot(0, 0.1F, -21);
		head_exterior.addChild(head_bottom_out);
		head_bottom_out.setTextureOffset(120, 56).addCuboid(-19.5F, -0.1F, -6, 39, 3, 5, 0, false);

		buttom_panel_right_5_r1 = new ModelMapper(modelDataWrapper);
		buttom_panel_right_5_r1.setPivot(-12.7131F, 1.4F, -5.1361F);
		head_bottom_out.addChild(buttom_panel_right_5_r1);
		setRotationAngle(buttom_panel_right_5_r1, 0, 0.1745F, 0);
		buttom_panel_right_5_r1.setTextureOffset(32, 130).addCuboid(-1.5F, -1.5F, -0.5F, 4, 3, 1, 0, false);

		buttom_panel_right_4_r2 = new ModelMapper(modelDataWrapper);
		buttom_panel_right_4_r2.setPivot(-15.9855F, 1.4F, -4.2142F);
		head_bottom_out.addChild(buttom_panel_right_4_r2);
		setRotationAngle(buttom_panel_right_4_r2, 0, 0.3491F, 0);
		buttom_panel_right_4_r2.setTextureOffset(134, 145).addCuboid(-2, -1.5F, -0.5F, 4, 3, 1, 0, false);

		buttom_panel_right_3_r2 = new ModelMapper(modelDataWrapper);
		buttom_panel_right_3_r2.setPivot(-21.5F, -0.1F, -2);
		head_bottom_out.addChild(buttom_panel_right_3_r2);
		setRotationAngle(buttom_panel_right_3_r2, 0, 0.5236F, 0);
		buttom_panel_right_3_r2.setTextureOffset(120, 145).addCuboid(0, 0, 0, 4, 3, 3, 0, false);

		buttom_panel_left_5_r1 = new ModelMapper(modelDataWrapper);
		buttom_panel_left_5_r1.setPivot(12.7131F, 1.4F, -5.1361F);
		head_bottom_out.addChild(buttom_panel_left_5_r1);
		setRotationAngle(buttom_panel_left_5_r1, 0, -0.1745F, 0);
		buttom_panel_left_5_r1.setTextureOffset(32, 130).addCuboid(-2.5F, -1.5F, -0.5F, 4, 3, 1, 0, true);

		buttom_panel_left_4_r2 = new ModelMapper(modelDataWrapper);
		buttom_panel_left_4_r2.setPivot(15.9855F, 1.4F, -4.2142F);
		head_bottom_out.addChild(buttom_panel_left_4_r2);
		setRotationAngle(buttom_panel_left_4_r2, 0, -0.3491F, 0);
		buttom_panel_left_4_r2.setTextureOffset(32, 130).addCuboid(-2, -1.5F, -0.5F, 4, 3, 1, 0, true);

		buttom_panel_left_3_r2 = new ModelMapper(modelDataWrapper);
		buttom_panel_left_3_r2.setPivot(21.5F, -0.1F, -2);
		head_bottom_out.addChild(buttom_panel_left_3_r2);
		setRotationAngle(buttom_panel_left_3_r2, 0, -0.5236F, 0);
		buttom_panel_left_3_r2.setTextureOffset(120, 145).addCuboid(-4, 0, 0, 4, 3, 3, 0, true);

		head_back = new ModelMapper(modelDataWrapper);
		head_back.setPivot(0, 0, -21);
		head_exterior.addChild(head_back);
		head_back.setTextureOffset(284, 302).addCuboid(-8, -33, -5, 1, 33, 5, 0, false);
		head_back.setTextureOffset(272, 302).addCuboid(7, -33, -5, 1, 33, 5, 0, false);
		head_back.setTextureOffset(50, 206).addCuboid(-8, -42, -5, 16, 9, 6, 0, false);
		head_back.setTextureOffset(269, 149).addCuboid(-7, -25, -5, 14, 19, 0, 0, false);
		head_back.setTextureOffset(102, 227).addCuboid(-7, -33, -0.1F, 14, 34, 0, 0, false);

		front_right_panel_5_r1 = new ModelMapper(modelDataWrapper);
		front_right_panel_5_r1.setPivot(9.4774F, -21, -4.7405F);
		head_back.addChild(front_right_panel_5_r1);
		setRotationAngle(front_right_panel_5_r1, 0, -0.1745F, 0);
		front_right_panel_5_r1.setTextureOffset(102, 294).addCuboid(-1.5F, -21, 0, 3, 42, 0, 0, false);

		front_right_panel_4_r2 = new ModelMapper(modelDataWrapper);
		front_right_panel_4_r2.setPivot(13.7738F, -20.5F, -3.4539F);
		head_back.addChild(front_right_panel_4_r2);
		setRotationAngle(front_right_panel_4_r2, 0, -0.3491F, 0);
		front_right_panel_4_r2.setTextureOffset(78, 294).addCuboid(-3, -20.5F, 0, 6, 41, 0, 0, false);

		front_right_panel_3_r3 = new ModelMapper(modelDataWrapper);
		front_right_panel_3_r3.setPivot(19.1907F, -20, -0.9269F);
		head_back.addChild(front_right_panel_3_r3);
		setRotationAngle(front_right_panel_3_r3, 0, -0.5236F, 0);
		front_right_panel_3_r3.setTextureOffset(0, 298).addCuboid(-3, -20, 0, 6, 40, 0, 0, false);

		front_right_panel_4_r3 = new ModelMapper(modelDataWrapper);
		front_right_panel_4_r3.setPivot(-9.4774F, -21, -4.7405F);
		head_back.addChild(front_right_panel_4_r3);
		setRotationAngle(front_right_panel_4_r3, 0, 0.1745F, 0);
		front_right_panel_4_r3.setTextureOffset(309, 232).addCuboid(-1.5F, -21, 0, 3, 42, 0, 0, false);

		front_right_panel_3_r4 = new ModelMapper(modelDataWrapper);
		front_right_panel_3_r4.setPivot(-13.7738F, -20.5F, -3.4539F);
		head_back.addChild(front_right_panel_3_r4);
		setRotationAngle(front_right_panel_3_r4, 0, 0.3491F, 0);
		front_right_panel_3_r4.setTextureOffset(90, 294).addCuboid(-3, -20.5F, 0, 6, 41, 0, 0, false);

		front_right_panel_2_r3 = new ModelMapper(modelDataWrapper);
		front_right_panel_2_r3.setPivot(-19.1907F, -20, -0.9269F);
		head_back.addChild(front_right_panel_2_r3);
		setRotationAngle(front_right_panel_2_r3, 0, 0.5236F, 0);
		front_right_panel_2_r3.setTextureOffset(12, 298).addCuboid(-3, -20, 0, 6, 40, 0, 0, false);

		roof_head_exterior = new ModelMapper(modelDataWrapper);
		roof_head_exterior.setPivot(0, 0, 0);
		head_exterior.addChild(roof_head_exterior);
		roof_head_exterior.setTextureOffset(65, 101).addCuboid(-4, -41.375F, -26, 4, 0, 34, 0, false);
		roof_head_exterior.setTextureOffset(65, 101).addCuboid(0, -41.375F, -26, 4, 0, 34, 0, true);

		outer_roof_7_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_7_r1.setPivot(4, -41.375F, -3);
		roof_head_exterior.addChild(outer_roof_7_r1);
		setRotationAngle(outer_roof_7_r1, 0, 0, 0.0873F);
		outer_roof_7_r1.setTextureOffset(27, 101).addCuboid(0, 0, -23, 6, 0, 34, 0, true);

		outer_roof_6_r2 = new ModelMapper(modelDataWrapper);
		outer_roof_6_r2.setPivot(12.392F, -40.205F, -3);
		roof_head_exterior.addChild(outer_roof_6_r2);
		setRotationAngle(outer_roof_6_r2, 0, 0, 0.2618F);
		outer_roof_6_r2.setTextureOffset(39, 101).addCuboid(-2.5F, 0, -23, 5, 0, 34, 0, true);

		outer_roof_5_r3 = new ModelMapper(modelDataWrapper);
		outer_roof_5_r3.setPivot(16.2163F, -39.0449F, -3);
		roof_head_exterior.addChild(outer_roof_5_r3);
		setRotationAngle(outer_roof_5_r3, 0, 0, 0.3491F);
		outer_roof_5_r3.setTextureOffset(53, 101).addCuboid(-1.5F, 0, -23, 3, 0, 34, 0, true);

		outer_roof_4_r3 = new ModelMapper(modelDataWrapper);
		outer_roof_4_r3.setPivot(18.2687F, -37.7659F, -3);
		roof_head_exterior.addChild(outer_roof_4_r3);
		setRotationAngle(outer_roof_4_r3, 0, 0, 0.8727F);
		outer_roof_4_r3.setTextureOffset(49, 101).addCuboid(-1, 0, -23, 2, 0, 34, 0, true);

		outer_roof_3_r3 = new ModelMapper(modelDataWrapper);
		outer_roof_3_r3.setPivot(18.6114F, -35.9228F, -3);
		roof_head_exterior.addChild(outer_roof_3_r3);
		setRotationAngle(outer_roof_3_r3, 0, 0, 1.3788F);
		outer_roof_3_r3.setTextureOffset(273, 57).addCuboid(-1, -0.5F, -23, 2, 1, 34, 0, true);

		outer_roof_6_r3 = new ModelMapper(modelDataWrapper);
		outer_roof_6_r3.setPivot(-4, -41.375F, -3);
		roof_head_exterior.addChild(outer_roof_6_r3);
		setRotationAngle(outer_roof_6_r3, 0, 0, -0.0873F);
		outer_roof_6_r3.setTextureOffset(27, 101).addCuboid(-6, 0, -23, 6, 0, 34, 0, false);

		outer_roof_5_r4 = new ModelMapper(modelDataWrapper);
		outer_roof_5_r4.setPivot(-12.392F, -40.205F, -3);
		roof_head_exterior.addChild(outer_roof_5_r4);
		setRotationAngle(outer_roof_5_r4, 0, 0, -0.2618F);
		outer_roof_5_r4.setTextureOffset(39, 101).addCuboid(-2.5F, 0, -23, 5, 0, 34, 0, false);

		outer_roof_4_r4 = new ModelMapper(modelDataWrapper);
		outer_roof_4_r4.setPivot(-16.2163F, -39.0449F, -3);
		roof_head_exterior.addChild(outer_roof_4_r4);
		setRotationAngle(outer_roof_4_r4, 0, 0, -0.3491F);
		outer_roof_4_r4.setTextureOffset(53, 101).addCuboid(-1.5F, 0, -23, 3, 0, 34, 0, false);

		outer_roof_3_r4 = new ModelMapper(modelDataWrapper);
		outer_roof_3_r4.setPivot(-18.2687F, -37.7659F, -3);
		roof_head_exterior.addChild(outer_roof_3_r4);
		setRotationAngle(outer_roof_3_r4, 0, 0, -0.8727F);
		outer_roof_3_r4.setTextureOffset(49, 101).addCuboid(-1, 0, -23, 2, 0, 34, 0, false);

		outer_roof_2_r3 = new ModelMapper(modelDataWrapper);
		outer_roof_2_r3.setPivot(-18.6114F, -35.9228F, -3);
		roof_head_exterior.addChild(outer_roof_2_r3);
		setRotationAngle(outer_roof_2_r3, 0, 0, -1.3788F);
		outer_roof_2_r3.setTextureOffset(273, 57).addCuboid(-1, -0.5F, -23, 2, 1, 34, 0, false);

		roof_window = new ModelMapper(modelDataWrapper);
		roof_window.setPivot(0, 24, 0);
		roof_window.setTextureOffset(78, 0).addCuboid(-18, -33, -24, 2, 0, 48, 0, false);
		roof_window.setTextureOffset(0, 206).addCuboid(-11, -36, -24, 1, 1, 48, 0, false);
		roof_window.setTextureOffset(13, 0).addCuboid(-9, -37, -24, 9, 0, 48, 0, false);

		inner_roof_4_r1 = new ModelMapper(modelDataWrapper);
		inner_roof_4_r1.setPivot(-10, -35, 0);
		roof_window.addChild(inner_roof_4_r1);
		setRotationAngle(inner_roof_4_r1, 0, 0, -0.829F);
		inner_roof_4_r1.setTextureOffset(0, 78).addCuboid(0, 0, -24, 3, 0, 48, 0, false);

		inner_roof_2_r1 = new ModelMapper(modelDataWrapper);
		inner_roof_2_r1.setPivot(-16, -33, 0);
		roof_window.addChild(inner_roof_2_r1);
		setRotationAngle(inner_roof_2_r1, 0, 0, -0.9163F);
		inner_roof_2_r1.setTextureOffset(43, 0).addCuboid(0, 0, -24, 6, 0, 48, 0, false);

		roof_door = new ModelMapper(modelDataWrapper);
		roof_door.setPivot(0, 24, 0);
		roof_door.setTextureOffset(12, 0).addCuboid(-18, -33, -16, 2, 0, 32, 0, false);
		roof_door.setTextureOffset(211, 240).addCuboid(-11, -36, -16, 1, 1, 32, 0, false);
		roof_door.setTextureOffset(98, 0).addCuboid(-9, -37, -16, 9, 0, 32, 0, false);

		inner_roof_4_r2 = new ModelMapper(modelDataWrapper);
		inner_roof_4_r2.setPivot(-10, -35, 0);
		roof_door.addChild(inner_roof_4_r2);
		setRotationAngle(inner_roof_4_r2, 0, 0, -0.829F);
		inner_roof_4_r2.setTextureOffset(61, 101).addCuboid(0, 0, -16, 3, 0, 32, 0, false);

		inner_roof_2_r2 = new ModelMapper(modelDataWrapper);
		inner_roof_2_r2.setPivot(-16, -33, 0);
		roof_door.addChild(inner_roof_2_r2);
		setRotationAngle(inner_roof_2_r2, 0, 0, -0.9163F);
		inner_roof_2_r2.setTextureOffset(0, 4).addCuboid(0, 0, -16, 6, 0, 32, 0, false);

		roof_end = new ModelMapper(modelDataWrapper);
		roof_end.setPivot(0, 24, 0);
		roof_end.setTextureOffset(41, 148).addCuboid(-18, -33, -8, 2, 0, 16, 0, false);
		roof_end.setTextureOffset(130, 227).addCuboid(-11, -36, -8, 1, 1, 16, 0, false);
		roof_end.setTextureOffset(114, 32).addCuboid(-9, -37, -8, 9, 0, 16, 0, false);
		roof_end.setTextureOffset(114, 32).addCuboid(0, -37, -8, 9, 0, 16, 0, true);
		roof_end.setTextureOffset(130, 227).addCuboid(10, -36, -8, 1, 1, 16, 0, true);
		roof_end.setTextureOffset(41, 148).addCuboid(16, -33, -8, 2, 0, 16, 0, true);

		inner_roof_9_r1 = new ModelMapper(modelDataWrapper);
		inner_roof_9_r1.setPivot(16, -33, 0);
		roof_end.addChild(inner_roof_9_r1);
		setRotationAngle(inner_roof_9_r1, 0, 0, 0.9163F);
		inner_roof_9_r1.setTextureOffset(41, 171).addCuboid(-6, 0, -8, 6, 0, 16, 0, true);

		inner_roof_7_r1 = new ModelMapper(modelDataWrapper);
		inner_roof_7_r1.setPivot(10, -35, 0);
		roof_end.addChild(inner_roof_7_r1);
		setRotationAngle(inner_roof_7_r1, 0, 0, 0.829F);
		inner_roof_7_r1.setTextureOffset(0, 16).addCuboid(-3, 0, -8, 3, 0, 16, 0, true);

		inner_roof_4_r3 = new ModelMapper(modelDataWrapper);
		inner_roof_4_r3.setPivot(-10, -35, 0);
		roof_end.addChild(inner_roof_4_r3);
		setRotationAngle(inner_roof_4_r3, 0, 0, -0.829F);
		inner_roof_4_r3.setTextureOffset(0, 16).addCuboid(0, 0, -8, 3, 0, 16, 0, false);

		inner_roof_2_r3 = new ModelMapper(modelDataWrapper);
		inner_roof_2_r3.setPivot(-16, -33, 0);
		roof_end.addChild(inner_roof_2_r3);
		setRotationAngle(inner_roof_2_r3, 0, 0, -0.9163F);
		inner_roof_2_r3.setTextureOffset(41, 171).addCuboid(0, 0, -8, 6, 0, 16, 0, false);

		roof_exterior_window = new ModelMapper(modelDataWrapper);
		roof_exterior_window.setPivot(0, 24, 0);
		roof_exterior_window.setTextureOffset(0, 0).addCuboid(-4, -41.375F, -24, 4, 0, 48, 0, false);

		outer_roof_5_r5 = new ModelMapper(modelDataWrapper);
		outer_roof_5_r5.setPivot(-4, -41.375F, 0);
		roof_exterior_window.addChild(outer_roof_5_r5);
		setRotationAngle(outer_roof_5_r5, 0, 0, -0.0873F);
		outer_roof_5_r5.setTextureOffset(31, 0).addCuboid(-6, 0, -24, 6, 0, 48, 0, false);

		outer_roof_4_r5 = new ModelMapper(modelDataWrapper);
		outer_roof_4_r5.setPivot(-12.392F, -40.205F, 0);
		roof_exterior_window.addChild(outer_roof_4_r5);
		setRotationAngle(outer_roof_4_r5, 0, 0, -0.2618F);
		outer_roof_4_r5.setTextureOffset(55, 0).addCuboid(-2.5F, 0, -24, 5, 0, 48, 0, false);

		outer_roof_3_r5 = new ModelMapper(modelDataWrapper);
		outer_roof_3_r5.setPivot(-16.2163F, -39.0449F, 0);
		roof_exterior_window.addChild(outer_roof_3_r5);
		setRotationAngle(outer_roof_3_r5, 0, 0, -0.3491F);
		outer_roof_3_r5.setTextureOffset(72, 0).addCuboid(-1.5F, 0, -24, 3, 0, 48, 0, false);

		outer_roof_2_r4 = new ModelMapper(modelDataWrapper);
		outer_roof_2_r4.setPivot(-18.2687F, -37.7659F, 0);
		roof_exterior_window.addChild(outer_roof_2_r4);
		setRotationAngle(outer_roof_2_r4, 0, 0, -0.8727F);
		outer_roof_2_r4.setTextureOffset(65, 0).addCuboid(-1, 0, -24, 2, 0, 48, 0, false);

		outer_roof_1_r2 = new ModelMapper(modelDataWrapper);
		outer_roof_1_r2.setPivot(-18.6114F, -35.9228F, 0);
		roof_exterior_window.addChild(outer_roof_1_r2);
		setRotationAngle(outer_roof_1_r2, 0, 0, -1.3788F);
		outer_roof_1_r2.setTextureOffset(191, 100).addCuboid(-1, -0.5F, -24, 2, 1, 48, 0, false);

		roof_exterior_door = new ModelMapper(modelDataWrapper);
		roof_exterior_door.setPivot(0, 24, 0);
		roof_exterior_door.setTextureOffset(16, 0).addCuboid(-4, -41.375F, -16, 4, 0, 32, 0, false);

		outer_roof_6_r4 = new ModelMapper(modelDataWrapper);
		outer_roof_6_r4.setPivot(-4, -41.375F, 0);
		roof_exterior_door.addChild(outer_roof_6_r4);
		setRotationAngle(outer_roof_6_r4, 0, 0, -0.0873F);
		outer_roof_6_r4.setTextureOffset(47, 0).addCuboid(-6, 0, -16, 6, 0, 32, 0, false);

		outer_roof_5_r6 = new ModelMapper(modelDataWrapper);
		outer_roof_5_r6.setPivot(-12.392F, -40.205F, 0);
		roof_exterior_door.addChild(outer_roof_5_r6);
		setRotationAngle(outer_roof_5_r6, 0, 0, -0.2618F);
		outer_roof_5_r6.setTextureOffset(71, 0).addCuboid(-2.5F, 0, -16, 5, 0, 32, 0, false);

		outer_roof_4_r6 = new ModelMapper(modelDataWrapper);
		outer_roof_4_r6.setPivot(-16.2163F, -39.0449F, 0);
		roof_exterior_door.addChild(outer_roof_4_r6);
		setRotationAngle(outer_roof_4_r6, 0, 0, -0.3491F);
		outer_roof_4_r6.setTextureOffset(88, 0).addCuboid(-1.5F, 0, -16, 3, 0, 32, 0, false);

		outer_roof_3_r6 = new ModelMapper(modelDataWrapper);
		outer_roof_3_r6.setPivot(-18.2687F, -37.7659F, 0);
		roof_exterior_door.addChild(outer_roof_3_r6);
		setRotationAngle(outer_roof_3_r6, 0, 0, -0.8727F);
		outer_roof_3_r6.setTextureOffset(81, 0).addCuboid(-1, 0, -16, 2, 0, 32, 0, false);

		outer_roof_2_r5 = new ModelMapper(modelDataWrapper);
		outer_roof_2_r5.setPivot(-18.6114F, -35.9228F, 0);
		roof_exterior_door.addChild(outer_roof_2_r5);
		setRotationAngle(outer_roof_2_r5, 0, 0, -1.3788F);
		outer_roof_2_r5.setTextureOffset(207, 116).addCuboid(-1, -0.5F, -16, 2, 1, 32, 0, false);

		roof_head = new ModelMapper(modelDataWrapper);
		roof_head.setPivot(0, 24, 0);
		roof_head.setTextureOffset(6, 16).addCuboid(-18, -33, 2, 2, 0, 6, 0, false);
		roof_head.setTextureOffset(8, 26).addCuboid(-11, -36, 2, 1, 1, 6, 0, false);
		roof_head.setTextureOffset(55, 50).addCuboid(-9, -37, 2, 9, 0, 6, 0, false);
		roof_head.setTextureOffset(55, 50).addCuboid(0, -37, 2, 9, 0, 6, 0, true);
		roof_head.setTextureOffset(8, 26).addCuboid(10, -36, 2, 1, 1, 6, 0, true);
		roof_head.setTextureOffset(6, 16).addCuboid(16, -33, 2, 2, 0, 6, 0, true);

		inner_roof_9_r2 = new ModelMapper(modelDataWrapper);
		inner_roof_9_r2.setPivot(16, -33, 0);
		roof_head.addChild(inner_roof_9_r2);
		setRotationAngle(inner_roof_9_r2, 0, 0, 0.9163F);
		inner_roof_9_r2.setTextureOffset(91, 50).addCuboid(-6, 0, 2, 6, 0, 6, 0, true);

		inner_roof_7_r2 = new ModelMapper(modelDataWrapper);
		inner_roof_7_r2.setPivot(10, -35, 0);
		roof_head.addChild(inner_roof_7_r2);
		setRotationAngle(inner_roof_7_r2, 0, 0, 0.829F);
		inner_roof_7_r2.setTextureOffset(0, 16).addCuboid(-3, 0, 2, 3, 0, 6, 0, true);

		inner_roof_4_r4 = new ModelMapper(modelDataWrapper);
		inner_roof_4_r4.setPivot(-10, -35, 0);
		roof_head.addChild(inner_roof_4_r4);
		setRotationAngle(inner_roof_4_r4, 0, 0, -0.829F);
		inner_roof_4_r4.setTextureOffset(0, 16).addCuboid(0, 0, 2, 3, 0, 6, 0, false);

		inner_roof_2_r4 = new ModelMapper(modelDataWrapper);
		inner_roof_2_r4.setPivot(-16, -33, 0);
		roof_head.addChild(inner_roof_2_r4);
		setRotationAngle(inner_roof_2_r4, 0, 0, -0.9163F);
		inner_roof_2_r4.setTextureOffset(91, 50).addCuboid(0, 0, 2, 6, 0, 6, 0, false);

		roof_window_light = new ModelMapper(modelDataWrapper);
		roof_window_light.setPivot(0, 24, 0);
		roof_window_light.setTextureOffset(177, 49).addCuboid(-12.4F, -38.5F, -24, 2, 3, 48, 0, false);

		roof_door_light = new ModelMapper(modelDataWrapper);
		roof_door_light.setPivot(0, 24, 0);
		roof_door_light.setTextureOffset(193, 65).addCuboid(-12.4F, -38.5F, -16, 2, 3, 32, 0, false);

		roof_end_light = new ModelMapper(modelDataWrapper);
		roof_end_light.setPivot(0, 24, 0);
		roof_end_light.setTextureOffset(206, 97).addCuboid(-9, -34.75F, -8.1F, 18, 0, 3, 0, false);
		roof_end_light.setTextureOffset(212, 84).addCuboid(-12.4F, -38.5F, -5, 2, 3, 13, 0, false);
		roof_end_light.setTextureOffset(212, 84).addCuboid(10.4F, -38.5F, -5, 2, 3, 13, 0, true);

		roof_head_light = new ModelMapper(modelDataWrapper);
		roof_head_light.setPivot(0, 24, 0);
		roof_head_light.setTextureOffset(219, 91).addCuboid(-12.4F, -38.5F, 2, 2, 3, 6, 0, false);
		roof_head_light.setTextureOffset(219, 91).addCuboid(10.4F, -38.5F, 2, 2, 3, 6, 0, true);

		handrail_door_type_1 = new ModelMapper(modelDataWrapper);
		handrail_door_type_1.setPivot(0, 24, 0);
		handrail_door_type_1.setTextureOffset(0, 0).addCuboid(0, -37, 0, 0, 37, 0, 0.2F, false);

		handrail_door_type_2 = new ModelMapper(modelDataWrapper);
		handrail_door_type_2.setPivot(0, 24, 0);
		handrail_door_type_2.setTextureOffset(0, 0).addCuboid(0, -14.25F, 0, 0, 15, 0, 0.2F, false);
		handrail_door_type_2.setTextureOffset(0, 0).addCuboid(0, -37, 0, 0, 6, 0, 0.2F, false);

		handrail_curve = new ModelMapper(modelDataWrapper);
		handrail_curve.setPivot(0.175F, 3.25F, 15.1F);
		handrail_door_type_2.addChild(handrail_curve);
		handrail_curve.setTextureOffset(0, 0).addCuboid(-0.175F, -34.1585F, -16.6F, 0, 0, 3, 0.2F, false);
		handrail_curve.setTextureOffset(0, 0).addCuboid(-0.175F, -17.5F, -16.6F, 0, 0, 3, 0.2F, false);

		handrail_curve_12_r1 = new ModelMapper(modelDataWrapper);
		handrail_curve_12_r1.setPivot(-0.175F, -17.7828F, -17.1464F);
		handrail_curve.addChild(handrail_curve_12_r1);
		setRotationAngle(handrail_curve_12_r1, -0.7854F, 0, 0);
		handrail_curve_12_r1.setTextureOffset(0, 0).addCuboid(0, 0, 0, 0, 0, 0, 0.2F, false);

		setRotationAngle(handrail_curve_12_r1, -0.7854F, 0, 0);
		handrail_curve_12_r1.setTextureOffset(0, 0).addCuboid(0, 0, 0, 0, 0, 0, 0.2F, false);

		handrail_curve_10_r1 = new ModelMapper(modelDataWrapper);
		handrail_curve_10_r1.setPivot(-0.175F, -18.056F, -17.356F);
		handrail_curve.addChild(handrail_curve_10_r1);
		setRotationAngle(handrail_curve_10_r1, -1.0472F, 0, 0);
		handrail_curve_10_r1.setTextureOffset(0, 0).addCuboid(0, 0, 0, 0, 0, 0, 0.2F, false);

		setRotationAngle(handrail_curve_10_r1, -1.0472F, 0, 0);
		handrail_curve_10_r1.setTextureOffset(0, 0).addCuboid(0, 0, 0, 0, 0, 0, 0.2F, false);

		handrail_curve_9_r1 = new ModelMapper(modelDataWrapper);
		handrail_curve_9_r1.setPivot(-0.175F, -17.5732F, -16.8732F);
		handrail_curve.addChild(handrail_curve_9_r1);
		setRotationAngle(handrail_curve_9_r1, -0.5236F, 0, 0);
		handrail_curve_9_r1.setTextureOffset(0, 0).addCuboid(0, 0, 0, 0, 0, 0, 0.2F, false);

		setRotationAngle(handrail_curve_9_r1, -0.5236F, 0, 0);
		handrail_curve_9_r1.setTextureOffset(0, 0).addCuboid(0, 0, 0, 0, 0, 0, 0.2F, false);

		handrail_curve_7_r1 = new ModelMapper(modelDataWrapper);
		handrail_curve_7_r1.setPivot(-0.175F, -25.8043F, -17.4293F);
		handrail_curve.addChild(handrail_curve_7_r1);
		setRotationAngle(handrail_curve_7_r1, 0, 0, -1.5708F);
		handrail_curve_7_r1.setTextureOffset(0, 0).addCuboid(-7.5F, 0, 0, 15, 0, 0, 0.2F, false);
		handrail_curve_7_r1.setTextureOffset(0, 0).addCuboid(-7.5F, 0, 4.6585F, 15, 0, 0, 0.2F, false);

		handrail_curve_6_r1 = new ModelMapper(modelDataWrapper);
		handrail_curve_6_r1.setPivot(-0.175F, -33.6025F, -17.356F);
		handrail_curve.addChild(handrail_curve_6_r1);
		setRotationAngle(handrail_curve_6_r1, 1.0472F, 0, 0);
		handrail_curve_6_r1.setTextureOffset(0, 0).addCuboid(0, 0, 0, 0, 0, 0, 0.2F, false);

		setRotationAngle(handrail_curve_6_r1, 1.0472F, 0, 0);
		handrail_curve_6_r1.setTextureOffset(0, 0).addCuboid(0, 0, 0, 0, 0, 0, 0.2F, false);

		handrail_curve_5_r1 = new ModelMapper(modelDataWrapper);
		handrail_curve_5_r1.setPivot(-0.175F, -33.8757F, -17.1464F);
		handrail_curve.addChild(handrail_curve_5_r1);
		setRotationAngle(handrail_curve_5_r1, 0.7854F, 0, 0);
		handrail_curve_5_r1.setTextureOffset(0, 0).addCuboid(0, 0, 0, 0, 0, 0, 0.2F, false);

		setRotationAngle(handrail_curve_5_r1, 0.7854F, 0, 0);
		handrail_curve_5_r1.setTextureOffset(0, 0).addCuboid(0, 0, 0, 0, 0, 0, 0.2F, false);

		handrail_curve_3_r1 = new ModelMapper(modelDataWrapper);
		handrail_curve_3_r1.setPivot(-0.175F, -34.0853F, -16.8732F);
		handrail_curve.addChild(handrail_curve_3_r1);
		setRotationAngle(handrail_curve_3_r1, 0.5236F, 0, 0);
		handrail_curve_3_r1.setTextureOffset(0, 0).addCuboid(0, 0, 0, 0, 0, 0, 0.2F, false);

		setRotationAngle(handrail_curve_3_r1, 0.5236F, 0, 0);
		handrail_curve_3_r1.setTextureOffset(0, 0).addCuboid(0, 0, 0, 0, 0, 0, 0.2F, false);

		handrail_curve_11_r1 = new ModelMapper(modelDataWrapper);
		handrail_curve_11_r1.setPivot(-0.175F, -17.7828F, -13.0536F);
		handrail_curve.addChild(handrail_curve_11_r1);
		setRotationAngle(handrail_curve_11_r1, 0.7854F, 0, 0);
		handrail_curve_11_r1.setTextureOffset(0, 0).addCuboid(0, 0, 0, 0, 0, 0, 0.2F, false);

		setRotationAngle(handrail_curve_11_r1, 0.7854F, 0, 0);
		handrail_curve_11_r1.setTextureOffset(0, 0).addCuboid(0, 0, 0, 0, 0, 0, 0.2F, false);

		handrail_curve_9_r2 = new ModelMapper(modelDataWrapper);
		handrail_curve_9_r2.setPivot(-0.175F, -18.056F, -12.844F);
		handrail_curve.addChild(handrail_curve_9_r2);
		setRotationAngle(handrail_curve_9_r2, 1.0472F, 0, 0);
		handrail_curve_9_r2.setTextureOffset(0, 0).addCuboid(0, 0, 0, 0, 0, 0, 0.2F, false);

		setRotationAngle(handrail_curve_9_r2, 1.0472F, 0, 0);
		handrail_curve_9_r2.setTextureOffset(0, 0).addCuboid(0, 0, 0, 0, 0, 0, 0.2F, false);

		handrail_curve_8_r1 = new ModelMapper(modelDataWrapper);
		handrail_curve_8_r1.setPivot(-0.175F, -17.5732F, -13.3268F);
		handrail_curve.addChild(handrail_curve_8_r1);
		setRotationAngle(handrail_curve_8_r1, 0.5236F, 0, 0);
		handrail_curve_8_r1.setTextureOffset(0, 0).addCuboid(0, 0, 0, 0, 0, 0, 0.2F, false);

		setRotationAngle(handrail_curve_8_r1, 0.5236F, 0, 0);
		handrail_curve_8_r1.setTextureOffset(0, 0).addCuboid(0, 0, 0, 0, 0, 0, 0.2F, false);

		handrail_curve_5_r2 = new ModelMapper(modelDataWrapper);
		handrail_curve_5_r2.setPivot(-0.175F, -33.6025F, -12.844F);
		handrail_curve.addChild(handrail_curve_5_r2);
		setRotationAngle(handrail_curve_5_r2, -1.0472F, 0, 0);
		handrail_curve_5_r2.setTextureOffset(0, 0).addCuboid(0, 0, 0, 0, 0, 0, 0.2F, false);

		setRotationAngle(handrail_curve_5_r2, -1.0472F, 0, 0);
		handrail_curve_5_r2.setTextureOffset(0, 0).addCuboid(0, 0, 0, 0, 0, 0, 0.2F, false);

		handrail_curve_4_r1 = new ModelMapper(modelDataWrapper);
		handrail_curve_4_r1.setPivot(-0.175F, -33.8757F, -13.0536F);
		handrail_curve.addChild(handrail_curve_4_r1);
		setRotationAngle(handrail_curve_4_r1, -0.7854F, 0, 0);
		handrail_curve_4_r1.setTextureOffset(0, 0).addCuboid(0, 0, 0, 0, 0, 0, 0.2F, false);

		setRotationAngle(handrail_curve_4_r1, -0.7854F, 0, 0);
		handrail_curve_4_r1.setTextureOffset(0, 0).addCuboid(0, 0, 0, 0, 0, 0, 0.2F, false);

		handrail_curve_2_r1 = new ModelMapper(modelDataWrapper);
		handrail_curve_2_r1.setPivot(-0.175F, -34.0853F, -13.3268F);
		handrail_curve.addChild(handrail_curve_2_r1);
		setRotationAngle(handrail_curve_2_r1, -0.5236F, 0, 0);
		handrail_curve_2_r1.setTextureOffset(0, 0).addCuboid(0, 0, 0, 0, 0, 0, 0.2F, false);

		roof_handle = new ModelMapper(modelDataWrapper);
		roof_handle.setPivot(0, 24, 0);
		roof_handle.setTextureOffset(0, 0).addCuboid(-4, -37.2F, -24, 0, 2, 0, 0.2F, false);
		roof_handle.setTextureOffset(0, 0).addCuboid(-4, -37.2F, -8, 0, 2, 0, 0.2F, false);
		roof_handle.setTextureOffset(0, 0).addCuboid(-4, -37.2F, 8, 0, 2, 0, 0.2F, false);
		roof_handle.setTextureOffset(0, 0).addCuboid(-4, -37.2F, 24, 0, 2, 0, 0.2F, false);
		roof_handle.setTextureOffset(0, 0).addCuboid(4, -37.2F, -24, 0, 2, 0, 0.2F, false);
		roof_handle.setTextureOffset(0, 0).addCuboid(4, -37.2F, -8, 0, 2, 0, 0.2F, false);
		roof_handle.setTextureOffset(0, 0).addCuboid(4, -37.2F, 8, 0, 2, 0, 0.2F, false);
		roof_handle.setTextureOffset(0, 0).addCuboid(4, -37.2F, 24, 0, 2, 0, 0.2F, false);

		roof_handrail_curve_19_r1 = new ModelMapper(modelDataWrapper);
		roof_handrail_curve_19_r1.setPivot(0.2215F, -35, -31.2785F);
		roof_handle.addChild(roof_handrail_curve_19_r1);
		setRotationAngle(roof_handrail_curve_19_r1, 0, 1.5708F, 0);
		roof_handrail_curve_19_r1.setTextureOffset(0, 0).addCuboid(0, 0, -0.5F, 0, 0, 1, 0.2F, false);

		roof_handrail_curve_22_r1 = new ModelMapper(modelDataWrapper);
		roof_handrail_curve_22_r1.setPivot(1.8608F, -35, -30.7053F);
		roof_handle.addChild(roof_handrail_curve_22_r1);
		setRotationAngle(roof_handrail_curve_22_r1, 0, 1.0472F, 0);
		roof_handrail_curve_22_r1.setTextureOffset(0, 0).addCuboid(0, 0, -1, 0, 0, 2, 0.2F, false);

		roof_handrail_curve_23_r1 = new ModelMapper(modelDataWrapper);
		roof_handrail_curve_23_r1.setPivot(3.4268F, -35, -29.1392F);
		roof_handle.addChild(roof_handrail_curve_23_r1);
		setRotationAngle(roof_handrail_curve_23_r1, 0, 0.5236F, 0);
		roof_handrail_curve_23_r1.setTextureOffset(0, 0).addCuboid(0, 0, -1, 0, 0, 2, 0.2F, false);

		roof_handrail_curve_18_r1 = new ModelMapper(modelDataWrapper);
		roof_handrail_curve_18_r1.setPivot(-0.2215F, -35, -31.2785F);
		roof_handle.addChild(roof_handrail_curve_18_r1);
		setRotationAngle(roof_handrail_curve_18_r1, 0, -1.5708F, 0);
		roof_handrail_curve_18_r1.setTextureOffset(0, 0).addCuboid(0, 0, -0.5F, 0, 0, 1, 0.2F, false);

		roof_handrail_curve_21_r1 = new ModelMapper(modelDataWrapper);
		roof_handrail_curve_21_r1.setPivot(-1.8608F, -35, -30.7053F);
		roof_handle.addChild(roof_handrail_curve_21_r1);
		setRotationAngle(roof_handrail_curve_21_r1, 0, -1.0472F, 0);
		roof_handrail_curve_21_r1.setTextureOffset(0, 0).addCuboid(0, 0, -1, 0, 0, 2, 0.2F, false);

		roof_handrail_curve_22_r2 = new ModelMapper(modelDataWrapper);
		roof_handrail_curve_22_r2.setPivot(-3.4268F, -35, -29.1392F);
		roof_handle.addChild(roof_handrail_curve_22_r2);
		setRotationAngle(roof_handrail_curve_22_r2, 0, -0.5236F, 0);
		roof_handrail_curve_22_r2.setTextureOffset(0, 0).addCuboid(0, 0, -1, 0, 0, 2, 0.2F, false);

		roof_handrail_curve_18_r2 = new ModelMapper(modelDataWrapper);
		roof_handrail_curve_18_r2.setPivot(0.2215F, -35, 31.2785F);
		roof_handle.addChild(roof_handrail_curve_18_r2);
		setRotationAngle(roof_handrail_curve_18_r2, 0, -1.5708F, 0);
		roof_handrail_curve_18_r2.setTextureOffset(0, 0).addCuboid(0, 0, -0.5F, 0, 0, 1, 0.2F, false);

		roof_handrail_curve_21_r2 = new ModelMapper(modelDataWrapper);
		roof_handrail_curve_21_r2.setPivot(1.8608F, -35, 30.7053F);
		roof_handle.addChild(roof_handrail_curve_21_r2);
		setRotationAngle(roof_handrail_curve_21_r2, 0, -1.0472F, 0);
		roof_handrail_curve_21_r2.setTextureOffset(0, 0).addCuboid(0, 0, -1, 0, 0, 2, 0.2F, false);

		roof_handrail_curve_22_r3 = new ModelMapper(modelDataWrapper);
		roof_handrail_curve_22_r3.setPivot(3.4268F, -35, 29.1392F);
		roof_handle.addChild(roof_handrail_curve_22_r3);
		setRotationAngle(roof_handrail_curve_22_r3, 0, -0.5236F, 0);
		roof_handrail_curve_22_r3.setTextureOffset(0, 0).addCuboid(0, 0, -1, 0, 0, 2, 0.2F, false);

		roof_handrail_curve_17_r1 = new ModelMapper(modelDataWrapper);
		roof_handrail_curve_17_r1.setPivot(-0.2215F, -35, 31.2785F);
		roof_handle.addChild(roof_handrail_curve_17_r1);
		setRotationAngle(roof_handrail_curve_17_r1, 0, 1.5708F, 0);
		roof_handrail_curve_17_r1.setTextureOffset(0, 0).addCuboid(0, 0, -0.5F, 0, 0, 1, 0.2F, false);

		roof_handrail_curve_20_r1 = new ModelMapper(modelDataWrapper);
		roof_handrail_curve_20_r1.setPivot(-1.8608F, -35, 30.7053F);
		roof_handle.addChild(roof_handrail_curve_20_r1);
		setRotationAngle(roof_handrail_curve_20_r1, 0, 1.0472F, 0);
		roof_handrail_curve_20_r1.setTextureOffset(0, 0).addCuboid(0, 0, -1, 0, 0, 2, 0.2F, false);

		roof_handrail_curve_21_r3 = new ModelMapper(modelDataWrapper);
		roof_handrail_curve_21_r3.setPivot(-3.4268F, -35, 29.1392F);
		roof_handle.addChild(roof_handrail_curve_21_r3);
		setRotationAngle(roof_handrail_curve_21_r3, 0, 0.5236F, 0);
		roof_handrail_curve_21_r3.setTextureOffset(0, 0).addCuboid(0, 0, -1, 0, 0, 2, 0.2F, false);

		roof_handrail_2_r1 = new ModelMapper(modelDataWrapper);
		roof_handrail_2_r1.setPivot(4, -35, 0);
		roof_handle.addChild(roof_handrail_2_r1);
		setRotationAngle(roof_handrail_2_r1, -1.5708F, 0, 0);
		roof_handrail_2_r1.setTextureOffset(0, 0).addCuboid(0, -28, 0, 0, 56, 0, 0.2F, false);
		roof_handrail_2_r1.setTextureOffset(0, 0).addCuboid(-8, -28, 0, 0, 56, 0, 0.2F, false);

		headlights = new ModelMapper(modelDataWrapper);
		headlights.setPivot(0, 24, 0);


		front_right_panel_6_r1 = new ModelMapper(modelDataWrapper);
		front_right_panel_6_r1.setPivot(9.4774F, -21, -25.7655F);
		headlights.addChild(front_right_panel_6_r1);
		setRotationAngle(front_right_panel_6_r1, 0, -0.1745F, 0);
		front_right_panel_6_r1.setTextureOffset(18, 33).addCuboid(-1.5F, 10.75F, 0, 3, 6, 0, 0, true);

		front_right_panel_5_r2 = new ModelMapper(modelDataWrapper);
		front_right_panel_5_r2.setPivot(13.7738F, -20.5F, -24.4789F);
		headlights.addChild(front_right_panel_5_r2);
		setRotationAngle(front_right_panel_5_r2, 0, -0.3491F, 0);
		front_right_panel_5_r2.setTextureOffset(6, 33).addCuboid(-3, 10.25F, 0, 6, 6, 0, 0, true);

		front_right_panel_4_r4 = new ModelMapper(modelDataWrapper);
		front_right_panel_4_r4.setPivot(-9.4774F, -21, -25.7655F);
		headlights.addChild(front_right_panel_4_r4);
		setRotationAngle(front_right_panel_4_r4, 0, 0.1745F, 0);
		front_right_panel_4_r4.setTextureOffset(18, 33).addCuboid(-1.5F, 10.75F, 0, 3, 6, 0, 0, false);

		front_right_panel_3_r5 = new ModelMapper(modelDataWrapper);
		front_right_panel_3_r5.setPivot(-13.7738F, -20.5F, -24.4789F);
		headlights.addChild(front_right_panel_3_r5);
		setRotationAngle(front_right_panel_3_r5, 0, 0.3491F, 0);
		front_right_panel_3_r5.setTextureOffset(6, 33).addCuboid(-3, 10.25F, 0, 6, 6, 0, 0, false);

		tail_lights = new ModelMapper(modelDataWrapper);
		tail_lights.setPivot(0, 24, 0);


		front_right_panel_5_r3 = new ModelMapper(modelDataWrapper);
		front_right_panel_5_r3.setPivot(13.7738F, -20.5F, -24.4789F);
		tail_lights.addChild(front_right_panel_5_r3);
		setRotationAngle(front_right_panel_5_r3, 0, -0.3491F, 0);
		front_right_panel_5_r3.setTextureOffset(18, 39).addCuboid(-3, 9.5F, 0, 6, 6, 0, 0, true);

		front_right_panel_4_r5 = new ModelMapper(modelDataWrapper);
		front_right_panel_4_r5.setPivot(19.1907F, -20, -21.9519F);
		tail_lights.addChild(front_right_panel_4_r5);
		setRotationAngle(front_right_panel_4_r5, 0, -0.5236F, 0);
		front_right_panel_4_r5.setTextureOffset(6, 39).addCuboid(-3, 9, 0, 6, 6, 0, 0, true);

		front_right_panel_3_r6 = new ModelMapper(modelDataWrapper);
		front_right_panel_3_r6.setPivot(-13.7738F, -20.5F, -24.4789F);
		tail_lights.addChild(front_right_panel_3_r6);
		setRotationAngle(front_right_panel_3_r6, 0, 0.3491F, 0);
		front_right_panel_3_r6.setTextureOffset(18, 39).addCuboid(-3, 9.5F, 0, 6, 6, 0, 0, false);

		front_right_panel_2_r4 = new ModelMapper(modelDataWrapper);
		front_right_panel_2_r4.setPivot(-19.1907F, -20, -21.9519F);
		tail_lights.addChild(front_right_panel_2_r4);
		setRotationAngle(front_right_panel_2_r4, 0, 0.5236F, 0);
		front_right_panel_2_r4.setTextureOffset(6, 39).addCuboid(-3, 9, 0, 6, 6, 0, 0, false);

		door_light = new ModelMapper(modelDataWrapper);
		door_light.setPivot(0, 24, 0);


		light_plate_1_r1 = new ModelMapper(modelDataWrapper);
		light_plate_1_r1.setPivot(-20.4294F, -31.1645F, 0);
		door_light.addChild(light_plate_1_r1);
		setRotationAngle(light_plate_1_r1, 0, 0, 1.5708F);
		light_plate_1_r1.setTextureOffset(0, 45).addCuboid(-1.5F, -1.1F, -1, 1, 1, 1, 0, false);

		door_light_on = new ModelMapper(modelDataWrapper);
		door_light_on.setPivot(0, 24, 0);


		light_r1 = new ModelMapper(modelDataWrapper);
		light_r1.setPivot(-20.4294F, -31.1645F, -1.025F);
		door_light_on.addChild(light_r1);
		setRotationAngle(light_r1, 0, 0, 1.5708F);
		light_r1.setTextureOffset(48, 12).addCuboid(-1.5F, -1.1F, 0, 1, 1, 0, 0, false);

		door_light_off = new ModelMapper(modelDataWrapper);
		door_light_off.setPivot(0, 24, 0);


		light_r2 = new ModelMapper(modelDataWrapper);
		light_r2.setPivot(-20.4294F, -31.1645F, -1.025F);
		door_light_off.addChild(light_r2);
		setRotationAngle(light_r2, 0, 0, 1.5708F);
		light_r2.setTextureOffset(48, 10).addCuboid(-1.5F, -1.1F, 0.01F, 1, 1, 0, 0, false);

		modelDataWrapper.setModelPart(textureWidth, textureHeight);
		window.setModelPart();
		window_handrails.setModelPart();
		window_display.setModelPart();
		window_exterior.setModelPart();
		side_panel.setModelPart();
		door.setModelPart();
		door_left.setModelPart(door.name);
		door_right.setModelPart(door.name);
		door_exterior.setModelPart();
		door_left_exterior.setModelPart(door_exterior.name);
		door_right_exterior.setModelPart(door_exterior.name);
		end.setModelPart();
		end_handrails.setModelPart();
		end_exterior.setModelPart();
		head.setModelPart();
		head_exterior.setModelPart();
		roof_window.setModelPart();
		roof_door.setModelPart();
		roof_end.setModelPart();
		roof_exterior_window.setModelPart();
		roof_exterior_door.setModelPart();
		roof_head.setModelPart();
		roof_window_light.setModelPart();
		roof_door_light.setModelPart();
		roof_end_light.setModelPart();
		roof_head_light.setModelPart();
		handrail_door_type_1.setModelPart();
		handrail_door_type_2.setModelPart();
		roof_handle.setModelPart();
		headlights.setModelPart();
		tail_lights.setModelPart();
	}

	private static final int DOOR_MAX = 12;

	@Override
	protected void renderWindowPositions(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
		final boolean frontWindow = isIndex(0, position, getWindowPositions());
		final boolean endWindow = isIndex(-1, position, getWindowPositions());

		switch (renderStage) {
			case LIGHTS:
				renderMirror(roof_window_light, matrices, vertices, light, position);
				break;
			case INTERIOR:
				renderMirror(window, matrices, vertices, light, position);
				if (renderDetails) {
					renderMirror(roof_window, matrices, vertices, light, position);
					renderMirror(window_handrails, matrices, vertices, light, position);
					if (frontWindow) {
						renderOnceFlipped(window_display, matrices, vertices, light, position);
					}
					if (!frontWindow && !endWindow) {
						renderOnce(roof_handle, matrices, vertices, light, position);
					}
					if (endWindow) {
						renderOnce(window_display, matrices, vertices, light, position);
					}
				}
				renderMirror(side_panel, matrices, vertices, light, position - 27);
				renderMirror(side_panel, matrices, vertices, light, position + 27);
				break;
			case EXTERIOR:
				renderMirror(window_exterior, matrices, vertices, light, position);
				renderMirror(roof_exterior_window, matrices, vertices, light, position);
				break;
		}
	}

	@Override
	protected void renderDoorPositions(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
		final boolean firstDoor = isIndex(0, position, getDoorPositions());
		final boolean lastDoor = isIndex(-1, position, getDoorPositions());

		switch (renderStage) {
			case LIGHTS:
				renderMirror(roof_door_light, matrices, vertices, light, position);
				break;
			case INTERIOR:
				door_right.setOffset(doorRightX, 0, doorRightZ);
				door_left.setOffset(doorRightX, 0, -doorRightZ);
				renderOnce(door, matrices, vertices, light, position);
				door_right.setOffset(doorLeftX, 0, doorLeftZ);
				door_left.setOffset(doorLeftX, 0, -doorLeftZ);
				renderOnceFlipped(door, matrices, vertices, light, position);

				if (renderDetails) {
					renderMirror(roof_door, matrices, vertices, light, position);
					if (getDoorPositions().length > 2 && !firstDoor && !lastDoor) {
						renderOnce(handrail_door_type_2, matrices, vertices, light, position);
					} else {
						renderOnce(handrail_door_type_1, matrices, vertices, light, position);
					}
				}
				break;
			case EXTERIOR:
				door_right_exterior.setOffset(doorRightX, 0, doorRightZ);
				door_left_exterior.setOffset(doorRightX, 0, -doorRightZ);
				renderOnce(door_exterior, matrices, vertices, light, position);
				door_right_exterior.setOffset(doorLeftX, 0, doorLeftZ);
				door_left_exterior.setOffset(doorLeftX, 0, -doorLeftZ);
				renderOnceFlipped(door_exterior, matrices, vertices, light, position);
				renderMirror(roof_exterior_door, matrices, vertices, light, position);
				break;
		}
	}

	@Override
	protected void renderHeadPosition1(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean useHeadlights) {
		switch (renderStage) {
			case LIGHTS:
				renderOnce(roof_head_light, matrices, vertices, light, position);
				break;
			case ALWAYS_ON_LIGHTS:
				renderOnce(useHeadlights ? headlights : tail_lights, matrices, vertices, light, position);
				break;
			case INTERIOR:
				renderOnce(head, matrices, vertices, light, position);
				if (renderDetails) {
					renderOnce(roof_head, matrices, vertices, light, position);
				}
				break;
			case EXTERIOR:
				renderOnce(head_exterior, matrices, vertices, light, position);
				break;

		}
	}

	@Override
	protected void renderHeadPosition2(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean useHeadlights) {
		switch (renderStage) {
			case LIGHTS:
				renderOnceFlipped(roof_head_light, matrices, vertices, light, position);
				break;
			case ALWAYS_ON_LIGHTS:
				renderOnceFlipped(useHeadlights ? headlights : tail_lights, matrices, vertices, light, position);
				break;
			case INTERIOR:
				renderOnceFlipped(head, matrices, vertices, light, position);
				if (renderDetails) {
					renderOnceFlipped(roof_head, matrices, vertices, light, position);
				}
				break;
			case EXTERIOR:
				renderOnceFlipped(head_exterior, matrices, vertices, light, position);
				break;
		}
	}

	@Override
	protected void renderEndPosition1(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
		switch (renderStage) {
			case LIGHTS:
				renderOnce(roof_end_light, matrices, vertices, light, position);
				break;
			case INTERIOR:
				renderOnce(end, matrices, vertices, light, position);
				if (renderDetails) {
					renderOnce(roof_end, matrices, vertices, light, position);
					renderOnce(end_handrails, matrices, vertices, light, position);
					renderMirror(side_panel, matrices, vertices, light, position + 11);
				}
				break;
			case EXTERIOR:
				renderOnce(end_exterior, matrices, vertices, light, position);
				break;
		}
	}

	@Override
	protected void renderEndPosition2(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
		switch (renderStage) {
			case LIGHTS:
				renderOnceFlipped(roof_end_light, matrices, vertices, light, position);
				break;
			case INTERIOR:
				renderOnceFlipped(end, matrices, vertices, light, position);
				if (renderDetails) {
					renderOnceFlipped(roof_end, matrices, vertices, light, position);
					renderOnceFlipped(end_handrails, matrices, vertices, light, position);
					renderMirror(side_panel, matrices, vertices, light, position - 11);
				}
				break;
			case EXTERIOR:
				renderOnceFlipped(end_exterior, matrices, vertices, light, position);
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
		return new int[]{-80, 0, 80};
	}

	@Override
	protected int[] getDoorPositions() {
		return new int[]{-120, -40, 40, 120};
	}

	@Override
	protected int[] getEndPositions() {
		return new int[]{-144, 144};
	}

	@Override
	protected float getDoorAnimationX(float value, boolean opening) {
		return 0;
	}

	@Override
	protected float getDoorAnimationZ(float value, boolean opening) {
		if (opening) {
			if (value > 0.4) {
				return smoothEnds(DOOR_MAX - 1, DOOR_MAX - 0.5F, 0.4F, 0.6F, value);
			} else {
				return smoothEnds(-DOOR_MAX + 1, DOOR_MAX - 1, -0.4F, 0.4F, value);
			}
		} else {
			if (value > 0.2) {
				return smoothEnds(1, DOOR_MAX - 0.5F, 0.2F, 0.6F, value);
			} else {
				return smoothEnds(-1.5F, 1.5F, -0.4F, 0.4F, value);
			}
		}
	}
}
