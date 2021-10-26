package mtr.model;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class ModelR179 extends ModelTrainBase {

	private final ModelPart window;
	private final ModelPart wall_1_r1;
	private final ModelPart window_handrails;
	private final ModelPart seat;
	private final ModelPart seat_bottom_r1;
	private final ModelPart seat_back_3_r1;
	private final ModelPart handrail_window;
	private final ModelPart handrail_top_1_r1;
	private final ModelPart handrail_mid;
	private final ModelPart handrail_middle_4_r1;
	private final ModelPart handrail_middle_3_r1;
	private final ModelPart handrail_middle_2_r1;
	private final ModelPart handrail_turn_1_r1;
	private final ModelPart headrail_right;
	private final ModelPart handrail_right_3_r1;
	private final ModelPart handrail_right_1_r1;
	private final ModelPart handrail_turn_4_r1;
	private final ModelPart handrail_turn_3_r1;
	private final ModelPart handrail_turn_2_r1;
	private final ModelPart handrail_turn_1_r2;
	private final ModelPart headrail_left;
	private final ModelPart handrail_left_3_r1;
	private final ModelPart handrail_left_1_r1;
	private final ModelPart handrail_turn_4_r2;
	private final ModelPart handrail_turn_3_r2;
	private final ModelPart handrail_turn_2_r2;
	private final ModelPart handrail_turn_1_r3;
	private final ModelPart headrail_up;
	private final ModelPart handrail_up_11_r1;
	private final ModelPart handrail_up_10_r1;
	private final ModelPart window_display;
	private final ModelPart display_window_r1;
	private final ModelPart window_exterior;
	private final ModelPart upper_wall_r1;
	private final ModelPart side_panel;
	private final ModelPart door;
	private final ModelPart door_right;
	private final ModelPart door_right_top_r1;
	private final ModelPart door_left;
	private final ModelPart door_side_top_r1;
	private final ModelPart door_exterior;
	private final ModelPart door_right_exterior;
	private final ModelPart door_right_top_r2;
	private final ModelPart door_left_exterior;
	private final ModelPart door_left_top_r1;
	private final ModelPart door_sides;
	private final ModelPart door_side_top_1_r1;
	private final ModelPart end;
	private final ModelPart upper_wall_2_r1;
	private final ModelPart upper_wall_1_r1;
	private final ModelPart end_handrails;
	private final ModelPart end_mid_roof_3_r1;
	private final ModelPart end_mid_roof_4_r1;
	private final ModelPart end_side_1;
	private final ModelPart seat_bottom_r2;
	private final ModelPart seat_back_4_r1;
	private final ModelPart end_side_2;
	private final ModelPart seat_bottom_r3;
	private final ModelPart seat_back_3_r2;
	private final ModelPart handrail_end_1;
	private final ModelPart handrail_top_6_r1;
	private final ModelPart handrail_top_5_r1;
	private final ModelPart handrail_top_4_r1;
	private final ModelPart handrail_right_4_r1;
	private final ModelPart handrail_right_2_r1;
	private final ModelPart handrail_turn_5_r1;
	private final ModelPart handrail_turn_4_r3;
	private final ModelPart handrail_turn_3_r3;
	private final ModelPart handrail_turn_2_r3;
	private final ModelPart handrail_end_2;
	private final ModelPart handrail_top_5_r2;
	private final ModelPart handrail_top_4_r2;
	private final ModelPart handrail_top_3_r1;
	private final ModelPart handrail_right_3_r2;
	private final ModelPart handrail_right_1_r2;
	private final ModelPart handrail_turn_4_r4;
	private final ModelPart handrail_turn_3_r4;
	private final ModelPart handrail_turn_2_r4;
	private final ModelPart handrail_turn_1_r4;
	private final ModelPart end_exterior;
	private final ModelPart upper_wall_2_r2;
	private final ModelPart upper_wall_1_r2;
	private final ModelPart end_bottom_out;
	private final ModelPart buttom_panel_right_4_r1;
	private final ModelPart buttom_panel_right_3_r1;
	private final ModelPart buttom_panel_right_2_r1;
	private final ModelPart buttom_panel_left_4_r1;
	private final ModelPart buttom_panel_left_3_r1;
	private final ModelPart buttom_panel_left_2_r1;
	private final ModelPart end_back;
	private final ModelPart front_right_panel_4_r1;
	private final ModelPart front_right_panel_3_r1;
	private final ModelPart front_right_panel_2_r1;
	private final ModelPart front_right_panel_3_r2;
	private final ModelPart front_right_panel_2_r2;
	private final ModelPart front_right_panel_1_r1;
	private final ModelPart roof_end_exterior;
	private final ModelPart outer_roof_6_r1;
	private final ModelPart outer_roof_5_r1;
	private final ModelPart outer_roof_4_r1;
	private final ModelPart outer_roof_3_r1;
	private final ModelPart outer_roof_2_r1;
	private final ModelPart outer_roof_5_r2;
	private final ModelPart outer_roof_4_r2;
	private final ModelPart outer_roof_3_r2;
	private final ModelPart outer_roof_2_r2;
	private final ModelPart outer_roof_1_r1;
	private final ModelPart head;
	private final ModelPart upper_wall_2_r3;
	private final ModelPart upper_wall_1_r3;
	private final ModelPart head_exterior;
	private final ModelPart upper_wall_2_r4;
	private final ModelPart upper_wall_1_r4;
	private final ModelPart head_bottom_out;
	private final ModelPart buttom_panel_right_5_r1;
	private final ModelPart buttom_panel_right_4_r2;
	private final ModelPart buttom_panel_right_3_r2;
	private final ModelPart buttom_panel_left_5_r1;
	private final ModelPart buttom_panel_left_4_r2;
	private final ModelPart buttom_panel_left_3_r2;
	private final ModelPart head_back;
	private final ModelPart front_right_panel_5_r1;
	private final ModelPart front_right_panel_4_r2;
	private final ModelPart front_right_panel_3_r3;
	private final ModelPart front_right_panel_4_r3;
	private final ModelPart front_right_panel_3_r4;
	private final ModelPart front_right_panel_2_r3;
	private final ModelPart roof_head_exterior;
	private final ModelPart outer_roof_7_r1;
	private final ModelPart outer_roof_6_r2;
	private final ModelPart outer_roof_5_r3;
	private final ModelPart outer_roof_4_r3;
	private final ModelPart outer_roof_3_r3;
	private final ModelPart outer_roof_6_r3;
	private final ModelPart outer_roof_5_r4;
	private final ModelPart outer_roof_4_r4;
	private final ModelPart outer_roof_3_r4;
	private final ModelPart outer_roof_2_r3;
	private final ModelPart roof_window;
	private final ModelPart inner_roof_4_r1;
	private final ModelPart inner_roof_2_r1;
	private final ModelPart roof_door;
	private final ModelPart inner_roof_4_r2;
	private final ModelPart inner_roof_2_r2;
	private final ModelPart roof_end;
	private final ModelPart inner_roof_9_r1;
	private final ModelPart inner_roof_7_r1;
	private final ModelPart inner_roof_4_r3;
	private final ModelPart inner_roof_2_r3;
	private final ModelPart roof_exterior_window;
	private final ModelPart outer_roof_5_r5;
	private final ModelPart outer_roof_4_r5;
	private final ModelPart outer_roof_3_r5;
	private final ModelPart outer_roof_2_r4;
	private final ModelPart outer_roof_1_r2;
	private final ModelPart roof_exterior_door;
	private final ModelPart outer_roof_6_r4;
	private final ModelPart outer_roof_5_r6;
	private final ModelPart outer_roof_4_r6;
	private final ModelPart outer_roof_3_r6;
	private final ModelPart outer_roof_2_r5;
	private final ModelPart roof_head;
	private final ModelPart inner_roof_9_r2;
	private final ModelPart inner_roof_7_r2;
	private final ModelPart inner_roof_4_r4;
	private final ModelPart inner_roof_2_r4;
	private final ModelPart roof_window_light;
	private final ModelPart roof_door_light;
	private final ModelPart roof_end_light;
	private final ModelPart roof_head_light;
	private final ModelPart handrail_door_type_1;
	private final ModelPart handrail_door_type_2;
	private final ModelPart handrail_curve;
	private final ModelPart handrail_curve_12_r1;
	private final ModelPart handrail_curve_10_r1;
	private final ModelPart handrail_curve_9_r1;
	private final ModelPart handrail_curve_7_r1;
	private final ModelPart handrail_curve_6_r1;
	private final ModelPart handrail_curve_5_r1;
	private final ModelPart handrail_curve_3_r1;
	private final ModelPart handrail_curve_11_r1;
	private final ModelPart handrail_curve_9_r2;
	private final ModelPart handrail_curve_8_r1;
	private final ModelPart handrail_curve_5_r2;
	private final ModelPart handrail_curve_4_r1;
	private final ModelPart handrail_curve_2_r1;
	private final ModelPart roof_handle;
	private final ModelPart roof_handrail_curve_19_r1;
	private final ModelPart roof_handrail_curve_22_r1;
	private final ModelPart roof_handrail_curve_23_r1;
	private final ModelPart roof_handrail_curve_18_r1;
	private final ModelPart roof_handrail_curve_21_r1;
	private final ModelPart roof_handrail_curve_22_r2;
	private final ModelPart roof_handrail_curve_18_r2;
	private final ModelPart roof_handrail_curve_21_r2;
	private final ModelPart roof_handrail_curve_22_r3;
	private final ModelPart roof_handrail_curve_17_r1;
	private final ModelPart roof_handrail_curve_20_r1;
	private final ModelPart roof_handrail_curve_21_r3;
	private final ModelPart roof_handrail_2_r1;
	private final ModelPart headlights;
	private final ModelPart front_right_panel_6_r1;
	private final ModelPart front_right_panel_5_r2;
	private final ModelPart front_right_panel_4_r4;
	private final ModelPart front_right_panel_3_r5;
	private final ModelPart tail_lights;
	private final ModelPart front_right_panel_5_r3;
	private final ModelPart front_right_panel_4_r5;
	private final ModelPart front_right_panel_3_r6;
	private final ModelPart front_right_panel_2_r4;
	private final ModelPart door_light;
	private final ModelPart light_plate_1_r1;
	private final ModelPart door_light_on;
	private final ModelPart light_r1;
	private final ModelPart door_light_off;
	private final ModelPart light_r2;

	public ModelR179() {
		textureWidth = 368;
		textureHeight = 368;
		window = new ModelPart(this);
		window.setPivot(0.0F, 24.0F, 0.0F);
		window.setTextureOffset(120, 0).addCuboid(-20.0F, 0.0F, -24.0F, 20.0F, 1.0F, 48.0F, 0.0F, false);
		window.setTextureOffset(0, 78).addCuboid(-21.5F, -13.0F, -28.5F, 2.0F, 13.0F, 57.0F, 0.0F, false);

		wall_1_r1 = new ModelPart(this);
		wall_1_r1.setPivot(-21.5F, -13.0F, 0.0F);
		window.addChild(wall_1_r1);
		setRotationAngle(wall_1_r1, 0.0F, 0.0F, 0.1047F);
		wall_1_r1.setTextureOffset(0, 0).addCuboid(0.0F, -21.0F, -28.5F, 2.0F, 21.0F, 57.0F, 0.0F, false);

		window_handrails = new ModelPart(this);
		window_handrails.setPivot(0.0F, 24.0F, 0.0F);


		seat = new ModelPart(this);
		seat.setPivot(0.0F, 0.0F, 0.0F);
		window_handrails.addChild(seat);
		seat.setTextureOffset(124, 155).addCuboid(-19.9F, -10.75F, -26.5F, 2.0F, 5.0F, 53.0F, 0.0F, false);
		seat.setTextureOffset(58, 171).addCuboid(-19.55F, -6.0F, -26.0F, 3.0F, 4.0F, 52.0F, 0.0F, false);

		seat_bottom_r1 = new ModelPart(this);
		seat_bottom_r1.setPivot(0.0F, -1.75F, 0.0F);
		seat.addChild(seat_bottom_r1);
		setRotationAngle(seat_bottom_r1, 0.0F, 0.0F, -0.0873F);
		seat_bottom_r1.setTextureOffset(120, 101).addCuboid(-19.9F, -6.0F, -26.5F, 9.0F, 1.0F, 53.0F, 0.0F, false);

		seat_back_3_r1 = new ModelPart(this);
		seat_back_3_r1.setPivot(-17.9F, -10.75F, 0.0F);
		seat.addChild(seat_back_3_r1);
		setRotationAngle(seat_back_3_r1, 0.0F, 0.0F, -0.1309F);
		seat_back_3_r1.setTextureOffset(0, 148).addCuboid(-2.0F, -5.0F, -26.5F, 2.0F, 5.0F, 53.0F, 0.0F, false);

		handrail_window = new ModelPart(this);
		handrail_window.setPivot(0.0F, 0.0F, 0.0F);
		window_handrails.addChild(handrail_window);


		handrail_top_1_r1 = new ModelPart(this);
		handrail_top_1_r1.setPivot(-12.8F, -31.75F, 0.0F);
		handrail_window.addChild(handrail_top_1_r1);
		setRotationAngle(handrail_top_1_r1, -1.5708F, 0.0F, 0.0F);
		handrail_top_1_r1.setTextureOffset(0, 0).addCuboid(0.0F, -24.5F, 0.0F, 0.0F, 49.0F, 0.0F, 0.2F, false);

		handrail_mid = new ModelPart(this);
		handrail_mid.setPivot(-39.95F, -9.875F, 38.1F);
		handrail_window.addChild(handrail_mid);
		handrail_mid.setTextureOffset(0, 0).addCuboid(23.5101F, 5.4649F, -38.1F, 5.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_middle_4_r1 = new ModelPart(this);
		handrail_middle_4_r1.setPivot(27.8083F, -20.505F, -38.1F);
		handrail_mid.addChild(handrail_middle_4_r1);
		setRotationAngle(handrail_middle_4_r1, 0.0F, 0.0F, 1.1606F);
		handrail_middle_4_r1.setTextureOffset(0, 0).addCuboid(-1.5F, 0.0F, 0.0F, 3.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_middle_3_r1 = new ModelPart(this);
		handrail_middle_3_r1.setPivot(29.7F, -13.725F, -38.1F);
		handrail_mid.addChild(handrail_middle_3_r1);
		setRotationAngle(handrail_middle_3_r1, 0.0F, 0.0F, 1.3788F);
		handrail_middle_3_r1.setTextureOffset(0, 0).addCuboid(-5.2F, 0.2F, 0.0F, 5.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_middle_2_r1 = new ModelPart(this);
		handrail_middle_2_r1.setPivot(29.5F, -4.525F, -38.1F);
		handrail_mid.addChild(handrail_middle_2_r1);
		setRotationAngle(handrail_middle_2_r1, 0.0F, 0.0F, 1.5708F);
		handrail_middle_2_r1.setTextureOffset(0, 0).addCuboid(-9.0F, 0.0F, 0.0F, 18.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_turn_1_r1 = new ModelPart(this);
		handrail_turn_1_r1.setPivot(29.7F, 4.675F, -38.3F);
		handrail_mid.addChild(handrail_turn_1_r1);
		setRotationAngle(handrail_turn_1_r1, 0.0F, -1.5708F, 2.3562F);
		handrail_turn_1_r1.setTextureOffset(0, 0).addCuboid(0.2F, 0.2F, -1.2F, 0.0F, 0.0F, 1.0F, 0.2F, false);

		headrail_right = new ModelPart(this);
		headrail_right.setPivot(-39.95F, -9.875F, 37.1F);
		handrail_window.addChild(headrail_right);


		handrail_right_3_r1 = new ModelPart(this);
		handrail_right_3_r1.setPivot(23.3F, -0.225F, -12.2F);
		headrail_right.addChild(handrail_right_3_r1);
		setRotationAngle(handrail_right_3_r1, 0.0F, 0.0F, 1.5708F);
		handrail_right_3_r1.setTextureOffset(0, 0).addCuboid(4.0F, -5.0F, 1.6F, 0.0F, 0.0F, 0.0F, 0.2F, false);
		handrail_right_3_r1.setTextureOffset(0, 0).addCuboid(-15.0F, -5.0F, 2.0F, 19.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_right_1_r1 = new ModelPart(this);
		handrail_right_1_r1.setPivot(28.06F, -17.5269F, -10.2F);
		headrail_right.addChild(handrail_right_1_r1);
		setRotationAngle(handrail_right_1_r1, 0.0F, 0.0F, 1.4573F);
		handrail_right_1_r1.setTextureOffset(0, 0).addCuboid(-1.0F, 0.0F, 0.0F, 3.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_turn_4_r1 = new ModelPart(this);
		handrail_turn_4_r1.setPivot(27.9F, -18.7556F, -10.237F);
		headrail_right.addChild(handrail_turn_4_r1);
		setRotationAngle(handrail_turn_4_r1, -1.3099F, 0.0441F, -0.1412F);
		handrail_turn_4_r1.setTextureOffset(0, 0).addCuboid(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_turn_3_r1 = new ModelPart(this);
		handrail_turn_3_r1.setPivot(27.65F, -19.8179F, -10.7262F);
		headrail_right.addChild(handrail_turn_3_r1);
		setRotationAngle(handrail_turn_3_r1, -1.1436F, 0.0916F, -0.1983F);
		handrail_turn_3_r1.setTextureOffset(0, 0).addCuboid(0.0F, 0.0F, -1.0F, 0.0F, 0.0F, 2.0F, 0.2F, false);

		handrail_turn_2_r1 = new ModelPart(this);
		handrail_turn_2_r1.setPivot(27.375F, -20.9556F, -11.312F);
		headrail_right.addChild(handrail_turn_2_r1);
		setRotationAngle(handrail_turn_2_r1, -0.7494F, 0.1284F, -0.1186F);
		handrail_turn_2_r1.setTextureOffset(0, 0).addCuboid(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_turn_1_r2 = new ModelPart(this);
		handrail_turn_1_r2.setPivot(27.25F, -21.4912F, -11.9782F);
		headrail_right.addChild(handrail_turn_1_r2);
		setRotationAngle(handrail_turn_1_r2, -0.6584F, 0.1103F, -0.0706F);
		handrail_turn_1_r2.setTextureOffset(0, 0).addCuboid(0.0F, 0.0F, -0.5F, 0.0F, 0.0F, 1.0F, 0.2F, false);

		headrail_left = new ModelPart(this);
		headrail_left.setPivot(-39.95F, -9.875F, 39.1F);
		handrail_window.addChild(headrail_left);


		handrail_left_3_r1 = new ModelPart(this);
		handrail_left_3_r1.setPivot(23.3F, -0.225F, -64.0F);
		headrail_left.addChild(handrail_left_3_r1);
		setRotationAngle(handrail_left_3_r1, 0.0F, 0.0F, 1.5708F);
		handrail_left_3_r1.setTextureOffset(0, 0).addCuboid(4.0F, -5.0F, -1.6F, 0.0F, 0.0F, 0.0F, 0.2F, false);
		handrail_left_3_r1.setTextureOffset(0, 0).addCuboid(-15.0F, -5.0F, -2.0F, 19.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_left_1_r1 = new ModelPart(this);
		handrail_left_1_r1.setPivot(28.06F, -17.5269F, -66.0F);
		headrail_left.addChild(handrail_left_1_r1);
		setRotationAngle(handrail_left_1_r1, 0.0F, 0.0F, 1.4573F);
		handrail_left_1_r1.setTextureOffset(0, 0).addCuboid(-1.0F, 0.0F, 0.0F, 3.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_turn_4_r2 = new ModelPart(this);
		handrail_turn_4_r2.setPivot(27.9F, -18.7556F, -65.963F);
		headrail_left.addChild(handrail_turn_4_r2);
		setRotationAngle(handrail_turn_4_r2, 1.3099F, -0.0441F, -0.1412F);
		handrail_turn_4_r2.setTextureOffset(0, 0).addCuboid(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_turn_3_r2 = new ModelPart(this);
		handrail_turn_3_r2.setPivot(27.65F, -19.8179F, -65.4738F);
		headrail_left.addChild(handrail_turn_3_r2);
		setRotationAngle(handrail_turn_3_r2, 1.1436F, -0.0916F, -0.1983F);
		handrail_turn_3_r2.setTextureOffset(0, 0).addCuboid(0.0F, 0.0F, -1.0F, 0.0F, 0.0F, 2.0F, 0.2F, false);

		handrail_turn_2_r2 = new ModelPart(this);
		handrail_turn_2_r2.setPivot(27.375F, -20.9556F, -64.888F);
		headrail_left.addChild(handrail_turn_2_r2);
		setRotationAngle(handrail_turn_2_r2, 0.7494F, -0.1284F, -0.1186F);
		handrail_turn_2_r2.setTextureOffset(0, 0).addCuboid(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_turn_1_r3 = new ModelPart(this);
		handrail_turn_1_r3.setPivot(27.25F, -21.4912F, -64.2218F);
		headrail_left.addChild(handrail_turn_1_r3);
		setRotationAngle(handrail_turn_1_r3, 0.6584F, -0.1103F, -0.0706F);
		handrail_turn_1_r3.setTextureOffset(0, 0).addCuboid(0.0F, 0.0F, -0.5F, 0.0F, 0.0F, 1.0F, 0.2F, false);

		headrail_up = new ModelPart(this);
		headrail_up.setPivot(-40.95F, -12.875F, 38.1F);
		handrail_window.addChild(headrail_up);
		headrail_up.setTextureOffset(0, 0).addCuboid(30.35F, -22.375F, -15.8F, 0.0F, 1.0F, 0.0F, 0.2F, false);
		headrail_up.setTextureOffset(0, 0).addCuboid(30.35F, -22.375F, -60.4F, 0.0F, 1.0F, 0.0F, 0.2F, false);
		headrail_up.setTextureOffset(0, 0).addCuboid(30.35F, -22.375F, -32.4F, 0.0F, 1.0F, 0.0F, 0.2F, false);
		headrail_up.setTextureOffset(0, 0).addCuboid(30.35F, -22.375F, -43.8F, 0.0F, 1.0F, 0.0F, 0.2F, false);

		handrail_up_11_r1 = new ModelPart(this);
		handrail_up_11_r1.setPivot(37.479F, -8.7545F, -46.8F);
		headrail_up.addChild(handrail_up_11_r1);
		setRotationAngle(handrail_up_11_r1, 0.0F, 0.0F, 0.6109F);
		handrail_up_11_r1.setTextureOffset(0, 0).addCuboid(-13.0F, -6.0F, 3.0F, 0.0F, 1.0F, 0.0F, 0.2F, false);
		handrail_up_11_r1.setTextureOffset(0, 0).addCuboid(-13.0F, -6.0F, 14.4F, 0.0F, 1.0F, 0.0F, 0.2F, false);
		handrail_up_11_r1.setTextureOffset(0, 0).addCuboid(-13.0F, -6.0F, -13.6F, 0.0F, 1.0F, 0.0F, 0.2F, false);
		handrail_up_11_r1.setTextureOffset(0, 0).addCuboid(-13.0F, -6.0F, 31.0F, 0.0F, 1.0F, 0.0F, 0.2F, false);

		handrail_up_10_r1 = new ModelPart(this);
		handrail_up_10_r1.setPivot(34.65F, -6.875F, -46.8F);
		headrail_up.addChild(handrail_up_10_r1);
		setRotationAngle(handrail_up_10_r1, 0.0F, 0.0F, 0.7854F);
		handrail_up_10_r1.setTextureOffset(0, 0).addCuboid(-13.0F, -6.0F, 3.0F, 0.0F, 2.0F, 0.0F, 0.2F, false);
		handrail_up_10_r1.setTextureOffset(0, 0).addCuboid(-13.0F, -6.0F, 14.4F, 0.0F, 2.0F, 0.0F, 0.2F, false);
		handrail_up_10_r1.setTextureOffset(0, 0).addCuboid(-13.0F, -6.0F, -13.6F, 0.0F, 2.0F, 0.0F, 0.2F, false);
		handrail_up_10_r1.setTextureOffset(0, 0).addCuboid(-13.0F, -6.0F, 31.0F, 0.0F, 2.0F, 0.0F, 0.2F, false);

		window_display = new ModelPart(this);
		window_display.setPivot(0.0F, 24.0F, 0.0F);


		display_window_r1 = new ModelPart(this);
		display_window_r1.setPivot(-21.5F, -13.0F, 0.0F);
		window_display.addChild(display_window_r1);
		setRotationAngle(display_window_r1, 0.0F, 0.0F, 0.1047F);
		display_window_r1.setTextureOffset(116, 171).addCuboid(0.1F, -19.5F, -14.0F, 2.0F, 6.0F, 28.0F, 0.0F, false);

		window_exterior = new ModelPart(this);
		window_exterior.setPivot(0.0F, 24.0F, 0.0F);
		window_exterior.setTextureOffset(181, 155).addCuboid(-21.5F, 0.0F, -24.0F, 1.0F, 4.0F, 48.0F, 0.0F, false);
		window_exterior.setTextureOffset(61, 101).addCuboid(-21.5F, -13.0F, -28.5F, 1.0F, 13.0F, 57.0F, 0.0F, false);

		upper_wall_r1 = new ModelPart(this);
		upper_wall_r1.setPivot(-21.5F, -13.0F, 0.0F);
		window_exterior.addChild(upper_wall_r1);
		setRotationAngle(upper_wall_r1, 0.0F, 0.0F, 0.1047F);
		upper_wall_r1.setTextureOffset(61, 21).addCuboid(0.0F, -23.0F, -28.5F, 1.0F, 23.0F, 57.0F, 0.0F, false);

		side_panel = new ModelPart(this);
		side_panel.setPivot(0.0F, 24.0F, 0.0F);
		side_panel.setTextureOffset(302, 137).addCuboid(-20.0F, -32.0F, 0.0F, 9.0F, 29.0F, 0.0F, 0.0F, false);

		door = new ModelPart(this);
		door.setPivot(0.0F, 24.0F, 0.0F);
		door.setTextureOffset(202, 207).addCuboid(-21.0F, 0.0F, -16.0F, 21.0F, 1.0F, 32.0F, 0.0F, false);

		door_right = new ModelPart(this);
		door_right.setPivot(0.0F, 0.0F, 0.0F);
		door.addChild(door_right);
		door_right.setTextureOffset(116, 171).addCuboid(-21.0F, -13.0F, 0.0F, 1.0F, 13.0F, 12.0F, 0.0F, false);

		door_right_top_r1 = new ModelPart(this);
		door_right_top_r1.setPivot(-21.0F, -13.0F, 0.0F);
		door_right.addChild(door_right_top_r1);
		setRotationAngle(door_right_top_r1, 0.0F, 0.0F, 0.1047F);
		door_right_top_r1.setTextureOffset(232, 149).addCuboid(0.0F, -21.0F, 0.0F, 1.0F, 21.0F, 12.0F, 0.0F, false);

		door_left = new ModelPart(this);
		door_left.setPivot(0.0F, 0.0F, 0.0F);
		door.addChild(door_left);
		door_left.setTextureOffset(148, 171).addCuboid(-21.0F, -13.0F, -12.0F, 1.0F, 13.0F, 12.0F, 0.0F, false);

		door_side_top_r1 = new ModelPart(this);
		door_side_top_r1.setPivot(-21.0F, -13.0F, 0.0F);
		door_left.addChild(door_side_top_r1);
		setRotationAngle(door_side_top_r1, 0.0F, 0.0F, 0.1047F);
		door_side_top_r1.setTextureOffset(52, 294).addCuboid(0.0F, -21.0F, -12.0F, 1.0F, 21.0F, 12.0F, 0.0F, false);

		door_exterior = new ModelPart(this);
		door_exterior.setPivot(0.0F, 24.0F, 0.0F);


		door_right_exterior = new ModelPart(this);
		door_right_exterior.setPivot(0.0F, 0.0F, 0.0F);
		door_exterior.addChild(door_right_exterior);
		door_right_exterior.setTextureOffset(191, 123).addCuboid(-21.0F, -13.0F, 0.0F, 0.0F, 13.0F, 12.0F, 0.0F, false);

		door_right_top_r2 = new ModelPart(this);
		door_right_top_r2.setPivot(-21.0F, -13.0F, 0.0F);
		door_right_exterior.addChild(door_right_top_r2);
		setRotationAngle(door_right_top_r2, 0.0F, 0.0F, 0.1047F);
		door_right_top_r2.setTextureOffset(84, 159).addCuboid(0.0F, -21.0F, 0.0F, 0.0F, 21.0F, 12.0F, 0.0F, false);

		door_left_exterior = new ModelPart(this);
		door_left_exterior.setPivot(0.0F, 0.0F, 0.0F);
		door_exterior.addChild(door_left_exterior);
		door_left_exterior.setTextureOffset(181, 178).addCuboid(-21.0F, -13.0F, -12.0F, 0.0F, 13.0F, 12.0F, 0.0F, false);

		door_left_top_r1 = new ModelPart(this);
		door_left_top_r1.setPivot(-21.0F, -13.0F, 0.0F);
		door_left_exterior.addChild(door_left_top_r1);
		setRotationAngle(door_left_top_r1, 0.0F, 0.0F, 0.1047F);
		door_left_top_r1.setTextureOffset(0, 136).addCuboid(0.0F, -21.0F, -12.0F, 0.0F, 21.0F, 12.0F, 0.0F, false);

		door_sides = new ModelPart(this);
		door_sides.setPivot(0.0F, 0.0F, 0.0F);
		door_exterior.addChild(door_sides);
		door_sides.setTextureOffset(245, 240).addCuboid(-21.5F, 0.0F, -17.0F, 1.0F, 4.0F, 34.0F, 0.0F, false);
		door_sides.setTextureOffset(0, 338).addCuboid(-22.0F, 0.0F, -11.0F, 1.0F, 1.0F, 22.0F, 0.0F, false);
		door_sides.setTextureOffset(235, 149).addCuboid(-20.0F, -33.0F, -11.5F, 2.0F, 0.0F, 23.0F, 0.0F, false);

		door_side_top_1_r1 = new ModelPart(this);
		door_side_top_1_r1.setPivot(-21.5F, -13.0F, 0.0F);
		door_sides.addChild(door_side_top_1_r1);
		setRotationAngle(door_side_top_1_r1, 0.0F, 0.0F, 0.1047F);
		door_side_top_1_r1.setTextureOffset(2, 30).addCuboid(0.0F, -23.0F, -11.5F, 0.0F, 3.0F, 23.0F, 0.0F, false);

		end = new ModelPart(this);
		end.setPivot(0.0F, 24.0F, 0.0F);
		end.setTextureOffset(208, 22).addCuboid(-20.0F, 0.0F, -10.0F, 40.0F, 1.0F, 18.0F, 0.0F, false);
		end.setTextureOffset(191, 287).addCuboid(-7.0F, -33.0F, -10.0F, 14.0F, 33.0F, 0.0F, 0.0F, false);
		end.setTextureOffset(0, 255).addCuboid(7.0F, -33.0F, -10.0F, 13.0F, 33.0F, 1.0F, 0.0F, true);
		end.setTextureOffset(33, 255).addCuboid(-20.0F, -33.0F, -10.0F, 13.0F, 33.0F, 1.0F, 0.0F, false);
		end.setTextureOffset(191, 100).addCuboid(19.5F, -13.0F, -9.5F, 2.0F, 13.0F, 22.0F, 0.0F, true);
		end.setTextureOffset(191, 100).addCuboid(-21.5F, -13.0F, -9.5F, 2.0F, 13.0F, 22.0F, 0.0F, false);

		upper_wall_2_r1 = new ModelPart(this);
		upper_wall_2_r1.setPivot(-21.5F, -13.0F, 0.0F);
		end.addChild(upper_wall_2_r1);
		setRotationAngle(upper_wall_2_r1, 0.0F, 0.0F, 0.1047F);
		upper_wall_2_r1.setTextureOffset(0, 87).addCuboid(0.0F, -21.0F, -9.5F, 2.0F, 21.0F, 22.0F, 0.0F, false);

		upper_wall_1_r1 = new ModelPart(this);
		upper_wall_1_r1.setPivot(21.5F, -13.0F, 0.0F);
		end.addChild(upper_wall_1_r1);
		setRotationAngle(upper_wall_1_r1, 0.0F, 0.0F, -0.1047F);
		upper_wall_1_r1.setTextureOffset(0, 87).addCuboid(-2.0F, -21.0F, -9.5F, 2.0F, 21.0F, 22.0F, 0.0F, true);

		end_handrails = new ModelPart(this);
		end_handrails.setPivot(0.0F, 24.0F, 0.0F);
		end_handrails.setTextureOffset(120, 69).addCuboid(-17.0F, -37.875F, -8.0F, 34.0F, 3.0F, 3.0F, 0.0F, false);
		end_handrails.setTextureOffset(120, 64).addCuboid(-18.0F, -35.875F, -10.0F, 36.0F, 3.0F, 2.0F, 0.0F, false);

		end_mid_roof_3_r1 = new ModelPart(this);
		end_mid_roof_3_r1.setPivot(16.05F, -32.95F, 0.0F);
		end_handrails.addChild(end_mid_roof_3_r1);
		setRotationAngle(end_mid_roof_3_r1, 0.0F, 0.0F, 0.2967F);
		end_mid_roof_3_r1.setTextureOffset(0, 130).addCuboid(-8.05F, -2.0F, -8.0F, 8.0F, 2.0F, 3.0F, 0.0F, true);

		end_mid_roof_4_r1 = new ModelPart(this);
		end_mid_roof_4_r1.setPivot(-16.05F, -32.95F, 0.0F);
		end_handrails.addChild(end_mid_roof_4_r1);
		setRotationAngle(end_mid_roof_4_r1, 0.0F, 0.0F, -0.2967F);
		end_mid_roof_4_r1.setTextureOffset(0, 130).addCuboid(0.05F, -2.0F, -8.0F, 8.0F, 2.0F, 3.0F, 0.0F, false);

		end_side_1 = new ModelPart(this);
		end_side_1.setPivot(0.0F, 0.0F, 0.0F);
		end_handrails.addChild(end_side_1);
		end_side_1.setTextureOffset(0, 206).addCuboid(17.9F, -10.75F, -9.5F, 2.0F, 5.0F, 20.0F, 0.0F, true);
		end_side_1.setTextureOffset(50, 227).addCuboid(16.55F, -6.0F, -9.0F, 3.0F, 4.0F, 19.0F, 0.0F, true);

		seat_bottom_r2 = new ModelPart(this);
		seat_bottom_r2.setPivot(0.0F, -1.75F, 16.0F);
		end_side_1.addChild(seat_bottom_r2);
		setRotationAngle(seat_bottom_r2, 0.0F, 0.0F, 0.0873F);
		seat_bottom_r2.setTextureOffset(172, 213).addCuboid(10.9F, -6.0F, -25.5F, 9.0F, 1.0F, 20.0F, 0.0F, true);

		seat_back_4_r1 = new ModelPart(this);
		seat_back_4_r1.setPivot(17.9F, -10.75F, 16.0F);
		end_side_1.addChild(seat_back_4_r1);
		setRotationAngle(seat_back_4_r1, 0.0F, 0.0F, 0.1309F);
		seat_back_4_r1.setTextureOffset(177, 64).addCuboid(0.0F, -5.0F, -25.5F, 2.0F, 5.0F, 20.0F, 0.0F, true);

		end_side_2 = new ModelPart(this);
		end_side_2.setPivot(0.0F, 0.0F, 0.0F);
		end_handrails.addChild(end_side_2);
		end_side_2.setTextureOffset(0, 206).addCuboid(-19.9F, -10.75F, -9.5F, 2.0F, 5.0F, 20.0F, 0.0F, false);
		end_side_2.setTextureOffset(50, 227).addCuboid(-19.55F, -6.0F, -9.0F, 3.0F, 4.0F, 19.0F, 0.0F, false);

		seat_bottom_r3 = new ModelPart(this);
		seat_bottom_r3.setPivot(0.0F, -1.75F, 16.0F);
		end_side_2.addChild(seat_bottom_r3);
		setRotationAngle(seat_bottom_r3, 0.0F, 0.0F, -0.0873F);
		seat_bottom_r3.setTextureOffset(172, 213).addCuboid(-19.9F, -6.0F, -25.5F, 9.0F, 1.0F, 20.0F, 0.0F, false);

		seat_back_3_r2 = new ModelPart(this);
		seat_back_3_r2.setPivot(-17.9F, -10.75F, 16.0F);
		end_side_2.addChild(seat_back_3_r2);
		setRotationAngle(seat_back_3_r2, 0.0F, 0.0F, -0.1309F);
		seat_back_3_r2.setTextureOffset(177, 64).addCuboid(-2.0F, -5.0F, -25.5F, 2.0F, 5.0F, 20.0F, 0.0F, false);

		handrail_end_1 = new ModelPart(this);
		handrail_end_1.setPivot(-1.0F, 4.0F, -12.0F);
		end_handrails.addChild(handrail_end_1);
		handrail_end_1.setTextureOffset(0, 0).addCuboid(13.8F, -35.75F, 11.5F, 0.0F, 0.0F, 9.0F, 0.2F, false);

		handrail_top_6_r1 = new ModelPart(this);
		handrail_top_6_r1.setPivot(11.7943F, -38.3087F, 9.5766F);
		handrail_end_1.addChild(handrail_top_6_r1);
		setRotationAngle(handrail_top_6_r1, 0.0F, 0.7854F, 0.0F);
		handrail_top_6_r1.setTextureOffset(0, 0).addCuboid(0.0F, -0.5F, 0.0F, 0.0F, 1.0F, 0.0F, 0.2F, false);

		handrail_top_5_r1 = new ModelPart(this);
		handrail_top_5_r1.setPivot(12.1924F, -37.3779F, 9.807F);
		handrail_end_1.addChild(handrail_top_5_r1);
		setRotationAngle(handrail_top_5_r1, -2.7699F, 0.6484F, -0.6107F);
		handrail_top_5_r1.setTextureOffset(0, 0).addCuboid(0.0F, -0.5F, 0.0F, 0.0F, 1.0F, 0.0F, 0.2F, false);

		handrail_top_4_r1 = new ModelPart(this);
		handrail_top_4_r1.setPivot(13.2474F, -36.3462F, 10.7346F);
		handrail_end_1.addChild(handrail_top_4_r1);
		setRotationAngle(handrail_top_4_r1, -0.7247F, 0.5197F, -0.2271F);
		handrail_top_4_r1.setTextureOffset(0, 0).addCuboid(0.0F, 0.0F, -1.0F, 0.0F, 0.0F, 2.0F, 0.2F, false);

		handrail_right_4_r1 = new ModelPart(this);
		handrail_right_4_r1.setPivot(17.65F, -14.1F, 20.9F);
		handrail_end_1.addChild(handrail_right_4_r1);
		setRotationAngle(handrail_right_4_r1, 0.0F, 0.0F, -1.5708F);
		handrail_right_4_r1.setTextureOffset(0, 0).addCuboid(-4.0F, -5.0F, 1.6F, 0.0F, 0.0F, 0.0F, 0.2F, false);
		handrail_right_4_r1.setTextureOffset(0, 0).addCuboid(-4.0F, -5.0F, 2.0F, 19.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_right_2_r1 = new ModelPart(this);
		handrail_right_2_r1.setPivot(12.89F, -31.4019F, 22.9F);
		handrail_end_1.addChild(handrail_right_2_r1);
		setRotationAngle(handrail_right_2_r1, 0.0F, 0.0F, -1.4573F);
		handrail_right_2_r1.setTextureOffset(0, 0).addCuboid(-2.0F, 0.0F, 0.0F, 3.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_turn_5_r1 = new ModelPart(this);
		handrail_turn_5_r1.setPivot(13.05F, -32.6306F, 22.863F);
		handrail_end_1.addChild(handrail_turn_5_r1);
		setRotationAngle(handrail_turn_5_r1, -1.3099F, -0.0441F, 0.1412F);
		handrail_turn_5_r1.setTextureOffset(0, 0).addCuboid(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_turn_4_r3 = new ModelPart(this);
		handrail_turn_4_r3.setPivot(13.3F, -33.6929F, 22.3738F);
		handrail_end_1.addChild(handrail_turn_4_r3);
		setRotationAngle(handrail_turn_4_r3, -1.1436F, -0.0916F, 0.1983F);
		handrail_turn_4_r3.setTextureOffset(0, 0).addCuboid(0.0F, 0.0F, -1.0F, 0.0F, 0.0F, 2.0F, 0.2F, false);

		handrail_turn_3_r3 = new ModelPart(this);
		handrail_turn_3_r3.setPivot(13.575F, -34.8306F, 21.788F);
		handrail_end_1.addChild(handrail_turn_3_r3);
		setRotationAngle(handrail_turn_3_r3, -0.7494F, -0.1284F, 0.1186F);
		handrail_turn_3_r3.setTextureOffset(0, 0).addCuboid(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_turn_2_r3 = new ModelPart(this);
		handrail_turn_2_r3.setPivot(13.7F, -35.3662F, 21.1218F);
		handrail_end_1.addChild(handrail_turn_2_r3);
		setRotationAngle(handrail_turn_2_r3, -0.6584F, -0.1103F, 0.0706F);
		handrail_turn_2_r3.setTextureOffset(0, 0).addCuboid(0.0F, 0.0F, -0.5F, 0.0F, 0.0F, 1.0F, 0.2F, false);

		handrail_end_2 = new ModelPart(this);
		handrail_end_2.setPivot(1.0F, 4.0F, -12.0F);
		end_handrails.addChild(handrail_end_2);
		handrail_end_2.setTextureOffset(0, 0).addCuboid(-13.8F, -35.75F, 11.5F, 0.0F, 0.0F, 9.0F, 0.2F, false);

		handrail_top_5_r2 = new ModelPart(this);
		handrail_top_5_r2.setPivot(-11.7943F, -38.3087F, 9.5766F);
		handrail_end_2.addChild(handrail_top_5_r2);
		setRotationAngle(handrail_top_5_r2, 0.0F, -0.7854F, 0.0F);
		handrail_top_5_r2.setTextureOffset(0, 0).addCuboid(0.0F, -0.5F, 0.0F, 0.0F, 1.0F, 0.0F, 0.2F, false);

		handrail_top_4_r2 = new ModelPart(this);
		handrail_top_4_r2.setPivot(-12.1924F, -37.3779F, 9.807F);
		handrail_end_2.addChild(handrail_top_4_r2);
		setRotationAngle(handrail_top_4_r2, -2.7699F, -0.6484F, 0.6107F);
		handrail_top_4_r2.setTextureOffset(0, 0).addCuboid(0.0F, -0.5F, 0.0F, 0.0F, 1.0F, 0.0F, 0.2F, false);

		handrail_top_3_r1 = new ModelPart(this);
		handrail_top_3_r1.setPivot(-13.2474F, -36.3462F, 10.7346F);
		handrail_end_2.addChild(handrail_top_3_r1);
		setRotationAngle(handrail_top_3_r1, -0.7247F, -0.5197F, 0.2271F);
		handrail_top_3_r1.setTextureOffset(0, 0).addCuboid(0.0F, 0.0F, -1.0F, 0.0F, 0.0F, 2.0F, 0.2F, false);

		handrail_right_3_r2 = new ModelPart(this);
		handrail_right_3_r2.setPivot(-17.65F, -14.1F, 20.9F);
		handrail_end_2.addChild(handrail_right_3_r2);
		setRotationAngle(handrail_right_3_r2, 0.0F, 0.0F, 1.5708F);
		handrail_right_3_r2.setTextureOffset(0, 0).addCuboid(4.0F, -5.0F, 1.6F, 0.0F, 0.0F, 0.0F, 0.2F, false);
		handrail_right_3_r2.setTextureOffset(0, 0).addCuboid(-15.0F, -5.0F, 2.0F, 19.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_right_1_r2 = new ModelPart(this);
		handrail_right_1_r2.setPivot(-12.89F, -31.4019F, 22.9F);
		handrail_end_2.addChild(handrail_right_1_r2);
		setRotationAngle(handrail_right_1_r2, 0.0F, 0.0F, 1.4573F);
		handrail_right_1_r2.setTextureOffset(0, 0).addCuboid(-1.0F, 0.0F, 0.0F, 3.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_turn_4_r4 = new ModelPart(this);
		handrail_turn_4_r4.setPivot(-13.05F, -32.6306F, 22.863F);
		handrail_end_2.addChild(handrail_turn_4_r4);
		setRotationAngle(handrail_turn_4_r4, -1.3099F, 0.0441F, -0.1412F);
		handrail_turn_4_r4.setTextureOffset(0, 0).addCuboid(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_turn_3_r4 = new ModelPart(this);
		handrail_turn_3_r4.setPivot(-13.3F, -33.6929F, 22.3738F);
		handrail_end_2.addChild(handrail_turn_3_r4);
		setRotationAngle(handrail_turn_3_r4, -1.1436F, 0.0916F, -0.1983F);
		handrail_turn_3_r4.setTextureOffset(0, 0).addCuboid(0.0F, 0.0F, -1.0F, 0.0F, 0.0F, 2.0F, 0.2F, false);

		handrail_turn_2_r4 = new ModelPart(this);
		handrail_turn_2_r4.setPivot(-13.575F, -34.8306F, 21.788F);
		handrail_end_2.addChild(handrail_turn_2_r4);
		setRotationAngle(handrail_turn_2_r4, -0.7494F, 0.1284F, -0.1186F);
		handrail_turn_2_r4.setTextureOffset(0, 0).addCuboid(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_turn_1_r4 = new ModelPart(this);
		handrail_turn_1_r4.setPivot(-13.7F, -35.3662F, 21.1218F);
		handrail_end_2.addChild(handrail_turn_1_r4);
		setRotationAngle(handrail_turn_1_r4, -0.6584F, 0.1103F, -0.0706F);
		handrail_turn_1_r4.setTextureOffset(0, 0).addCuboid(0.0F, 0.0F, -0.5F, 0.0F, 0.0F, 1.0F, 0.2F, false);

		end_exterior = new ModelPart(this);
		end_exterior.setPivot(0.0F, 24.0F, 0.0F);
		end_exterior.setTextureOffset(276, 208).addCuboid(20.5F, 0.0F, -12.0F, 1.0F, 4.0F, 20.0F, 0.0F, true);
		end_exterior.setTextureOffset(120, 109).addCuboid(19.5F, -13.0F, -10.5F, 2.0F, 13.0F, 23.0F, 0.0F, true);
		end_exterior.setTextureOffset(276, 208).addCuboid(-21.5F, 0.0F, -12.0F, 1.0F, 4.0F, 20.0F, 0.0F, false);
		end_exterior.setTextureOffset(222, 278).addCuboid(-21.5F, -13.0F, -10.5F, 2.0F, 13.0F, 23.0F, 0.0F, false);

		upper_wall_2_r2 = new ModelPart(this);
		upper_wall_2_r2.setPivot(-21.5F, -13.0F, 0.0F);
		end_exterior.addChild(upper_wall_2_r2);
		setRotationAngle(upper_wall_2_r2, 0.0F, 0.0F, 0.1047F);
		upper_wall_2_r2.setTextureOffset(113, 271).addCuboid(0.0F, -23.0F, -11.5F, 2.0F, 23.0F, 24.0F, 0.0F, false);

		upper_wall_1_r2 = new ModelPart(this);
		upper_wall_1_r2.setPivot(21.5F, -13.0F, 0.0F);
		end_exterior.addChild(upper_wall_1_r2);
		setRotationAngle(upper_wall_1_r2, 0.0F, 0.0F, -0.1047F);
		upper_wall_1_r2.setTextureOffset(0, 148).addCuboid(-2.0F, -23.0F, -11.5F, 2.0F, 23.0F, 24.0F, 0.0F, true);

		end_bottom_out = new ModelPart(this);
		end_bottom_out.setPivot(0.0F, 0.1F, -21.0F);
		end_exterior.addChild(end_bottom_out);
		end_bottom_out.setTextureOffset(120, 56).addCuboid(-19.5F, -0.1F, 5.0F, 39.0F, 3.0F, 5.0F, 0.0F, false);

		buttom_panel_right_4_r1 = new ModelPart(this);
		buttom_panel_right_4_r1.setPivot(-12.7131F, 1.4F, 5.8639F);
		end_bottom_out.addChild(buttom_panel_right_4_r1);
		setRotationAngle(buttom_panel_right_4_r1, 0.0F, 0.1745F, 0.0F);
		buttom_panel_right_4_r1.setTextureOffset(32, 130).addCuboid(-1.5F, -1.5F, -0.5F, 4.0F, 3.0F, 1.0F, 0.0F, false);

		buttom_panel_right_3_r1 = new ModelPart(this);
		buttom_panel_right_3_r1.setPivot(-15.9855F, 1.4F, 6.7858F);
		end_bottom_out.addChild(buttom_panel_right_3_r1);
		setRotationAngle(buttom_panel_right_3_r1, 0.0F, 0.3491F, 0.0F);
		buttom_panel_right_3_r1.setTextureOffset(134, 145).addCuboid(-2.0F, -1.5F, -0.5F, 4.0F, 3.0F, 1.0F, 0.0F, false);

		buttom_panel_right_2_r1 = new ModelPart(this);
		buttom_panel_right_2_r1.setPivot(-21.5F, -0.1F, 9.0F);
		end_bottom_out.addChild(buttom_panel_right_2_r1);
		setRotationAngle(buttom_panel_right_2_r1, 0.0F, 0.5236F, 0.0F);
		buttom_panel_right_2_r1.setTextureOffset(120, 145).addCuboid(0.0F, 0.0F, 0.0F, 4.0F, 3.0F, 3.0F, 0.0F, false);

		buttom_panel_left_4_r1 = new ModelPart(this);
		buttom_panel_left_4_r1.setPivot(12.7131F, 1.4F, 5.8639F);
		end_bottom_out.addChild(buttom_panel_left_4_r1);
		setRotationAngle(buttom_panel_left_4_r1, 0.0F, -0.1745F, 0.0F);
		buttom_panel_left_4_r1.setTextureOffset(32, 130).addCuboid(-2.5F, -1.5F, -0.5F, 4.0F, 3.0F, 1.0F, 0.0F, true);

		buttom_panel_left_3_r1 = new ModelPart(this);
		buttom_panel_left_3_r1.setPivot(15.9855F, 1.4F, 6.7858F);
		end_bottom_out.addChild(buttom_panel_left_3_r1);
		setRotationAngle(buttom_panel_left_3_r1, 0.0F, -0.3491F, 0.0F);
		buttom_panel_left_3_r1.setTextureOffset(134, 145).addCuboid(-2.0F, -1.5F, -0.5F, 4.0F, 3.0F, 1.0F, 0.0F, true);

		buttom_panel_left_2_r1 = new ModelPart(this);
		buttom_panel_left_2_r1.setPivot(21.5F, -0.1F, 9.0F);
		end_bottom_out.addChild(buttom_panel_left_2_r1);
		setRotationAngle(buttom_panel_left_2_r1, 0.0F, -0.5236F, 0.0F);
		buttom_panel_left_2_r1.setTextureOffset(120, 145).addCuboid(-4.0F, 0.0F, 0.0F, 4.0F, 3.0F, 3.0F, 0.0F, true);

		end_back = new ModelPart(this);
		end_back.setPivot(0.0F, 0.0F, -21.0F);
		end_exterior.addChild(end_back);
		end_back.setTextureOffset(308, 302).addCuboid(-8.0F, -33.0F, 6.0F, 1.0F, 33.0F, 5.0F, 0.0F, false);
		end_back.setTextureOffset(296, 302).addCuboid(7.0F, -33.0F, 6.0F, 1.0F, 33.0F, 5.0F, 0.0F, false);
		end_back.setTextureOffset(0, 231).addCuboid(-8.0F, -42.0F, 6.0F, 16.0F, 9.0F, 6.0F, 0.0F, false);
		end_back.setTextureOffset(249, 278).addCuboid(-7.0F, -25.0F, 6.0F, 14.0F, 19.0F, 0.0F, 0.0F, false);
		end_back.setTextureOffset(281, 240).addCuboid(-7.0F, -34.0F, 10.0F, 14.0F, 34.0F, 0.0F, 0.0F, false);

		front_right_panel_4_r1 = new ModelPart(this);
		front_right_panel_4_r1.setPivot(9.4774F, -21.0F, 6.2595F);
		end_back.addChild(front_right_panel_4_r1);
		setRotationAngle(front_right_panel_4_r1, 0.0F, -0.1745F, 0.0F);
		front_right_panel_4_r1.setTextureOffset(311, 41).addCuboid(-1.5F, -21.0F, 0.0F, 3.0F, 42.0F, 0.0F, 0.0F, false);

		front_right_panel_3_r1 = new ModelPart(this);
		front_right_panel_3_r1.setPivot(13.7738F, -20.5F, 7.5461F);
		end_back.addChild(front_right_panel_3_r1);
		setRotationAngle(front_right_panel_3_r1, 0.0F, -0.3491F, 0.0F);
		front_right_panel_3_r1.setTextureOffset(165, 295).addCuboid(-3.0F, -20.5F, 0.0F, 6.0F, 41.0F, 0.0F, 0.0F, false);

		front_right_panel_2_r1 = new ModelPart(this);
		front_right_panel_2_r1.setPivot(19.1907F, -20.0F, 10.0731F);
		end_back.addChild(front_right_panel_2_r1);
		setRotationAngle(front_right_panel_2_r1, 0.0F, -0.5236F, 0.0F);
		front_right_panel_2_r1.setTextureOffset(24, 298).addCuboid(-3.0F, -20.0F, 0.0F, 6.0F, 40.0F, 0.0F, 0.0F, false);

		front_right_panel_3_r2 = new ModelPart(this);
		front_right_panel_3_r2.setPivot(-9.4774F, -21.0F, 6.2595F);
		end_back.addChild(front_right_panel_3_r2);
		setRotationAngle(front_right_panel_3_r2, 0.0F, 0.1745F, 0.0F);
		front_right_panel_3_r2.setTextureOffset(219, 314).addCuboid(-1.5F, -21.0F, 0.0F, 3.0F, 42.0F, 0.0F, 0.0F, false);

		front_right_panel_2_r2 = new ModelPart(this);
		front_right_panel_2_r2.setPivot(-13.7738F, -20.5F, 7.5461F);
		end_back.addChild(front_right_panel_2_r2);
		setRotationAngle(front_right_panel_2_r2, 0.0F, 0.3491F, 0.0F);
		front_right_panel_2_r2.setTextureOffset(177, 295).addCuboid(-3.0F, -20.5F, 0.0F, 6.0F, 41.0F, 0.0F, 0.0F, false);

		front_right_panel_1_r1 = new ModelPart(this);
		front_right_panel_1_r1.setPivot(-19.1907F, -20.0F, 10.0731F);
		end_back.addChild(front_right_panel_1_r1);
		setRotationAngle(front_right_panel_1_r1, 0.0F, 0.5236F, 0.0F);
		front_right_panel_1_r1.setTextureOffset(36, 298).addCuboid(-3.0F, -20.0F, 0.0F, 6.0F, 40.0F, 0.0F, 0.0F, false);

		roof_end_exterior = new ModelPart(this);
		roof_end_exterior.setPivot(0.0F, 0.0F, 0.0F);
		end_exterior.addChild(roof_end_exterior);
		roof_end_exterior.setTextureOffset(137, 0).addCuboid(-4.0F, -41.375F, -15.0F, 4.0F, 0.0F, 23.0F, 0.0F, false);
		roof_end_exterior.setTextureOffset(137, 0).addCuboid(0.0F, -41.375F, -15.0F, 4.0F, 0.0F, 23.0F, 0.0F, true);

		outer_roof_6_r1 = new ModelPart(this);
		outer_roof_6_r1.setPivot(4.0F, -41.375F, 0.0F);
		roof_end_exterior.addChild(outer_roof_6_r1);
		setRotationAngle(outer_roof_6_r1, 0.0F, 0.0F, 0.0873F);
		outer_roof_6_r1.setTextureOffset(125, 0).addCuboid(0.0F, 0.0F, -15.0F, 6.0F, 0.0F, 23.0F, 0.0F, true);

		outer_roof_5_r1 = new ModelPart(this);
		outer_roof_5_r1.setPivot(12.392F, -40.205F, 0.0F);
		roof_end_exterior.addChild(outer_roof_5_r1);
		setRotationAngle(outer_roof_5_r1, 0.0F, 0.0F, 0.2618F);
		outer_roof_5_r1.setTextureOffset(125, 23).addCuboid(-2.5F, 0.0F, -15.0F, 5.0F, 0.0F, 23.0F, 0.0F, true);

		outer_roof_4_r1 = new ModelPart(this);
		outer_roof_4_r1.setPivot(16.2163F, -39.0449F, 0.0F);
		roof_end_exterior.addChild(outer_roof_4_r1);
		setRotationAngle(outer_roof_4_r1, 0.0F, 0.0F, 0.3491F);
		outer_roof_4_r1.setTextureOffset(113, 101).addCuboid(-1.5F, 0.0F, -15.0F, 3.0F, 0.0F, 23.0F, 0.0F, true);

		outer_roof_3_r1 = new ModelPart(this);
		outer_roof_3_r1.setPivot(18.2687F, -37.7659F, 0.0F);
		roof_end_exterior.addChild(outer_roof_3_r1);
		setRotationAngle(outer_roof_3_r1, 0.0F, 0.0F, 0.8727F);
		outer_roof_3_r1.setTextureOffset(3, 78).addCuboid(-1.0F, 0.0F, -15.0F, 2.0F, 0.0F, 23.0F, 0.0F, true);

		outer_roof_2_r1 = new ModelPart(this);
		outer_roof_2_r1.setPivot(18.6114F, -35.9228F, 0.0F);
		roof_end_exterior.addChild(outer_roof_2_r1);
		setRotationAngle(outer_roof_2_r1, 0.0F, 0.0F, 1.3788F);
		outer_roof_2_r1.setTextureOffset(57, 171).addCuboid(-1.0F, -0.5F, -15.0F, 2.0F, 1.0F, 23.0F, 0.0F, true);

		outer_roof_5_r2 = new ModelPart(this);
		outer_roof_5_r2.setPivot(-4.0F, -41.375F, 0.0F);
		roof_end_exterior.addChild(outer_roof_5_r2);
		setRotationAngle(outer_roof_5_r2, 0.0F, 0.0F, -0.0873F);
		outer_roof_5_r2.setTextureOffset(125, 0).addCuboid(-6.0F, 0.0F, -15.0F, 6.0F, 0.0F, 23.0F, 0.0F, false);

		outer_roof_4_r2 = new ModelPart(this);
		outer_roof_4_r2.setPivot(-12.392F, -40.205F, 0.0F);
		roof_end_exterior.addChild(outer_roof_4_r2);
		setRotationAngle(outer_roof_4_r2, 0.0F, 0.0F, -0.2618F);
		outer_roof_4_r2.setTextureOffset(125, 23).addCuboid(-2.5F, 0.0F, -15.0F, 5.0F, 0.0F, 23.0F, 0.0F, false);

		outer_roof_3_r2 = new ModelPart(this);
		outer_roof_3_r2.setPivot(-16.2163F, -39.0449F, 0.0F);
		roof_end_exterior.addChild(outer_roof_3_r2);
		setRotationAngle(outer_roof_3_r2, 0.0F, 0.0F, -0.3491F);
		outer_roof_3_r2.setTextureOffset(113, 101).addCuboid(-1.5F, 0.0F, -15.0F, 3.0F, 0.0F, 23.0F, 0.0F, false);

		outer_roof_2_r2 = new ModelPart(this);
		outer_roof_2_r2.setPivot(-18.2687F, -37.7659F, 0.0F);
		roof_end_exterior.addChild(outer_roof_2_r2);
		setRotationAngle(outer_roof_2_r2, 0.0F, 0.0F, -0.8727F);
		outer_roof_2_r2.setTextureOffset(3, 78).addCuboid(-1.0F, 0.0F, -15.0F, 2.0F, 0.0F, 23.0F, 0.0F, false);

		outer_roof_1_r1 = new ModelPart(this);
		outer_roof_1_r1.setPivot(-18.6114F, -35.9228F, 0.0F);
		roof_end_exterior.addChild(outer_roof_1_r1);
		setRotationAngle(outer_roof_1_r1, 0.0F, 0.0F, -1.3788F);
		outer_roof_1_r1.setTextureOffset(57, 171).addCuboid(-1.0F, -0.5F, -15.0F, 2.0F, 1.0F, 23.0F, 0.0F, false);

		head = new ModelPart(this);
		head.setPivot(0.0F, 24.0F, 0.0F);
		head.setTextureOffset(120, 49).addCuboid(-20.0F, 0.0F, 4.0F, 40.0F, 1.0F, 4.0F, 0.0F, false);
		head.setTextureOffset(201, 56).addCuboid(19.5F, -13.0F, 3.5F, 2.0F, 13.0F, 9.0F, 0.0F, true);
		head.setTextureOffset(201, 56).addCuboid(-21.5F, -13.0F, 3.5F, 2.0F, 13.0F, 9.0F, 0.0F, false);
		head.setTextureOffset(243, 100).addCuboid(-19.5F, -37.0F, 4.0F, 39.0F, 37.0F, 0.0F, 0.0F, false);

		upper_wall_2_r3 = new ModelPart(this);
		upper_wall_2_r3.setPivot(-21.5F, -13.0F, 0.0F);
		head.addChild(upper_wall_2_r3);
		setRotationAngle(upper_wall_2_r3, 0.0F, 0.0F, 0.1047F);
		upper_wall_2_r3.setTextureOffset(0, 78).addCuboid(0.0F, -21.0F, 3.5F, 2.0F, 21.0F, 9.0F, 0.0F, false);

		upper_wall_1_r3 = new ModelPart(this);
		upper_wall_1_r3.setPivot(21.5F, -13.0F, 0.0F);
		head.addChild(upper_wall_1_r3);
		setRotationAngle(upper_wall_1_r3, 0.0F, 0.0F, -0.1047F);
		upper_wall_1_r3.setTextureOffset(0, 78).addCuboid(-2.0F, -21.0F, 3.5F, 2.0F, 21.0F, 9.0F, 0.0F, true);

		head_exterior = new ModelPart(this);
		head_exterior.setPivot(0.0F, 24.0F, 0.0F);
		head_exterior.setTextureOffset(0, 263).addCuboid(-21.5F, 0.0F, -23.0F, 1.0F, 4.0F, 31.0F, 0.0F, false);
		head_exterior.setTextureOffset(231, 149).addCuboid(-21.5F, -13.0F, -21.5F, 2.0F, 13.0F, 34.0F, 0.0F, false);
		head_exterior.setTextureOffset(0, 263).addCuboid(20.5F, 0.0F, -23.0F, 1.0F, 4.0F, 31.0F, 0.0F, true);
		head_exterior.setTextureOffset(173, 240).addCuboid(19.5F, -13.0F, -21.5F, 2.0F, 13.0F, 34.0F, 0.0F, true);
		head_exterior.setTextureOffset(22, 336).addCuboid(-20.0F, 0.0F, -22.0F, 40.0F, 1.0F, 26.0F, 0.0F, false);
		head_exterior.setTextureOffset(229, 49).addCuboid(-19.5F, -42.0F, 3.0F, 39.0F, 42.0F, 0.0F, 0.0F, false);

		upper_wall_2_r4 = new ModelPart(this);
		upper_wall_2_r4.setPivot(21.5F, -13.0F, -3.0F);
		head_exterior.addChild(upper_wall_2_r4);
		setRotationAngle(upper_wall_2_r4, 0.0F, 0.0F, -0.1047F);
		upper_wall_2_r4.setTextureOffset(63, 236).addCuboid(-2.0F, -23.0F, -19.5F, 2.0F, 23.0F, 35.0F, 0.0F, true);

		upper_wall_1_r4 = new ModelPart(this);
		upper_wall_1_r4.setPivot(-21.5F, -13.0F, -3.0F);
		head_exterior.addChild(upper_wall_1_r4);
		setRotationAngle(upper_wall_1_r4, 0.0F, 0.0F, 0.1047F);
		upper_wall_1_r4.setTextureOffset(133, 213).addCuboid(0.0F, -23.0F, -19.5F, 2.0F, 23.0F, 35.0F, 0.0F, false);

		head_bottom_out = new ModelPart(this);
		head_bottom_out.setPivot(0.0F, 0.1F, -21.0F);
		head_exterior.addChild(head_bottom_out);
		head_bottom_out.setTextureOffset(120, 56).addCuboid(-19.5F, -0.1F, -6.0F, 39.0F, 3.0F, 5.0F, 0.0F, false);

		buttom_panel_right_5_r1 = new ModelPart(this);
		buttom_panel_right_5_r1.setPivot(-12.7131F, 1.4F, -5.1361F);
		head_bottom_out.addChild(buttom_panel_right_5_r1);
		setRotationAngle(buttom_panel_right_5_r1, 0.0F, 0.1745F, 0.0F);
		buttom_panel_right_5_r1.setTextureOffset(32, 130).addCuboid(-1.5F, -1.5F, -0.5F, 4.0F, 3.0F, 1.0F, 0.0F, false);

		buttom_panel_right_4_r2 = new ModelPart(this);
		buttom_panel_right_4_r2.setPivot(-15.9855F, 1.4F, -4.2142F);
		head_bottom_out.addChild(buttom_panel_right_4_r2);
		setRotationAngle(buttom_panel_right_4_r2, 0.0F, 0.3491F, 0.0F);
		buttom_panel_right_4_r2.setTextureOffset(134, 145).addCuboid(-2.0F, -1.5F, -0.5F, 4.0F, 3.0F, 1.0F, 0.0F, false);

		buttom_panel_right_3_r2 = new ModelPart(this);
		buttom_panel_right_3_r2.setPivot(-21.5F, -0.1F, -2.0F);
		head_bottom_out.addChild(buttom_panel_right_3_r2);
		setRotationAngle(buttom_panel_right_3_r2, 0.0F, 0.5236F, 0.0F);
		buttom_panel_right_3_r2.setTextureOffset(120, 145).addCuboid(0.0F, 0.0F, 0.0F, 4.0F, 3.0F, 3.0F, 0.0F, false);

		buttom_panel_left_5_r1 = new ModelPart(this);
		buttom_panel_left_5_r1.setPivot(12.7131F, 1.4F, -5.1361F);
		head_bottom_out.addChild(buttom_panel_left_5_r1);
		setRotationAngle(buttom_panel_left_5_r1, 0.0F, -0.1745F, 0.0F);
		buttom_panel_left_5_r1.setTextureOffset(32, 130).addCuboid(-2.5F, -1.5F, -0.5F, 4.0F, 3.0F, 1.0F, 0.0F, true);

		buttom_panel_left_4_r2 = new ModelPart(this);
		buttom_panel_left_4_r2.setPivot(15.9855F, 1.4F, -4.2142F);
		head_bottom_out.addChild(buttom_panel_left_4_r2);
		setRotationAngle(buttom_panel_left_4_r2, 0.0F, -0.3491F, 0.0F);
		buttom_panel_left_4_r2.setTextureOffset(32, 130).addCuboid(-2.0F, -1.5F, -0.5F, 4.0F, 3.0F, 1.0F, 0.0F, true);

		buttom_panel_left_3_r2 = new ModelPart(this);
		buttom_panel_left_3_r2.setPivot(21.5F, -0.1F, -2.0F);
		head_bottom_out.addChild(buttom_panel_left_3_r2);
		setRotationAngle(buttom_panel_left_3_r2, 0.0F, -0.5236F, 0.0F);
		buttom_panel_left_3_r2.setTextureOffset(120, 145).addCuboid(-4.0F, 0.0F, 0.0F, 4.0F, 3.0F, 3.0F, 0.0F, true);

		head_back = new ModelPart(this);
		head_back.setPivot(0.0F, 0.0F, -21.0F);
		head_exterior.addChild(head_back);
		head_back.setTextureOffset(284, 302).addCuboid(-8.0F, -33.0F, -5.0F, 1.0F, 33.0F, 5.0F, 0.0F, false);
		head_back.setTextureOffset(272, 302).addCuboid(7.0F, -33.0F, -5.0F, 1.0F, 33.0F, 5.0F, 0.0F, false);
		head_back.setTextureOffset(50, 206).addCuboid(-8.0F, -42.0F, -5.0F, 16.0F, 9.0F, 6.0F, 0.0F, false);
		head_back.setTextureOffset(269, 149).addCuboid(-7.0F, -25.0F, -5.0F, 14.0F, 19.0F, 0.0F, 0.0F, false);
		head_back.setTextureOffset(102, 227).addCuboid(-7.0F, -33.0F, -0.1F, 14.0F, 34.0F, 0.0F, 0.0F, false);

		front_right_panel_5_r1 = new ModelPart(this);
		front_right_panel_5_r1.setPivot(9.4774F, -21.0F, -4.7405F);
		head_back.addChild(front_right_panel_5_r1);
		setRotationAngle(front_right_panel_5_r1, 0.0F, -0.1745F, 0.0F);
		front_right_panel_5_r1.setTextureOffset(102, 294).addCuboid(-1.5F, -21.0F, 0.0F, 3.0F, 42.0F, 0.0F, 0.0F, false);

		front_right_panel_4_r2 = new ModelPart(this);
		front_right_panel_4_r2.setPivot(13.7738F, -20.5F, -3.4539F);
		head_back.addChild(front_right_panel_4_r2);
		setRotationAngle(front_right_panel_4_r2, 0.0F, -0.3491F, 0.0F);
		front_right_panel_4_r2.setTextureOffset(78, 294).addCuboid(-3.0F, -20.5F, 0.0F, 6.0F, 41.0F, 0.0F, 0.0F, false);

		front_right_panel_3_r3 = new ModelPart(this);
		front_right_panel_3_r3.setPivot(19.1907F, -20.0F, -0.9269F);
		head_back.addChild(front_right_panel_3_r3);
		setRotationAngle(front_right_panel_3_r3, 0.0F, -0.5236F, 0.0F);
		front_right_panel_3_r3.setTextureOffset(0, 298).addCuboid(-3.0F, -20.0F, 0.0F, 6.0F, 40.0F, 0.0F, 0.0F, false);

		front_right_panel_4_r3 = new ModelPart(this);
		front_right_panel_4_r3.setPivot(-9.4774F, -21.0F, -4.7405F);
		head_back.addChild(front_right_panel_4_r3);
		setRotationAngle(front_right_panel_4_r3, 0.0F, 0.1745F, 0.0F);
		front_right_panel_4_r3.setTextureOffset(309, 232).addCuboid(-1.5F, -21.0F, 0.0F, 3.0F, 42.0F, 0.0F, 0.0F, false);

		front_right_panel_3_r4 = new ModelPart(this);
		front_right_panel_3_r4.setPivot(-13.7738F, -20.5F, -3.4539F);
		head_back.addChild(front_right_panel_3_r4);
		setRotationAngle(front_right_panel_3_r4, 0.0F, 0.3491F, 0.0F);
		front_right_panel_3_r4.setTextureOffset(90, 294).addCuboid(-3.0F, -20.5F, 0.0F, 6.0F, 41.0F, 0.0F, 0.0F, false);

		front_right_panel_2_r3 = new ModelPart(this);
		front_right_panel_2_r3.setPivot(-19.1907F, -20.0F, -0.9269F);
		head_back.addChild(front_right_panel_2_r3);
		setRotationAngle(front_right_panel_2_r3, 0.0F, 0.5236F, 0.0F);
		front_right_panel_2_r3.setTextureOffset(12, 298).addCuboid(-3.0F, -20.0F, 0.0F, 6.0F, 40.0F, 0.0F, 0.0F, false);

		roof_head_exterior = new ModelPart(this);
		roof_head_exterior.setPivot(0.0F, 0.0F, 0.0F);
		head_exterior.addChild(roof_head_exterior);
		roof_head_exterior.setTextureOffset(65, 101).addCuboid(-4.0F, -41.375F, -26.0F, 4.0F, 0.0F, 34.0F, 0.0F, false);
		roof_head_exterior.setTextureOffset(65, 101).addCuboid(0.0F, -41.375F, -26.0F, 4.0F, 0.0F, 34.0F, 0.0F, true);

		outer_roof_7_r1 = new ModelPart(this);
		outer_roof_7_r1.setPivot(4.0F, -41.375F, -3.0F);
		roof_head_exterior.addChild(outer_roof_7_r1);
		setRotationAngle(outer_roof_7_r1, 0.0F, 0.0F, 0.0873F);
		outer_roof_7_r1.setTextureOffset(27, 101).addCuboid(0.0F, 0.0F, -23.0F, 6.0F, 0.0F, 34.0F, 0.0F, true);

		outer_roof_6_r2 = new ModelPart(this);
		outer_roof_6_r2.setPivot(12.392F, -40.205F, -3.0F);
		roof_head_exterior.addChild(outer_roof_6_r2);
		setRotationAngle(outer_roof_6_r2, 0.0F, 0.0F, 0.2618F);
		outer_roof_6_r2.setTextureOffset(39, 101).addCuboid(-2.5F, 0.0F, -23.0F, 5.0F, 0.0F, 34.0F, 0.0F, true);

		outer_roof_5_r3 = new ModelPart(this);
		outer_roof_5_r3.setPivot(16.2163F, -39.0449F, -3.0F);
		roof_head_exterior.addChild(outer_roof_5_r3);
		setRotationAngle(outer_roof_5_r3, 0.0F, 0.0F, 0.3491F);
		outer_roof_5_r3.setTextureOffset(53, 101).addCuboid(-1.5F, 0.0F, -23.0F, 3.0F, 0.0F, 34.0F, 0.0F, true);

		outer_roof_4_r3 = new ModelPart(this);
		outer_roof_4_r3.setPivot(18.2687F, -37.7659F, -3.0F);
		roof_head_exterior.addChild(outer_roof_4_r3);
		setRotationAngle(outer_roof_4_r3, 0.0F, 0.0F, 0.8727F);
		outer_roof_4_r3.setTextureOffset(49, 101).addCuboid(-1.0F, 0.0F, -23.0F, 2.0F, 0.0F, 34.0F, 0.0F, true);

		outer_roof_3_r3 = new ModelPart(this);
		outer_roof_3_r3.setPivot(18.6114F, -35.9228F, -3.0F);
		roof_head_exterior.addChild(outer_roof_3_r3);
		setRotationAngle(outer_roof_3_r3, 0.0F, 0.0F, 1.3788F);
		outer_roof_3_r3.setTextureOffset(273, 57).addCuboid(-1.0F, -0.5F, -23.0F, 2.0F, 1.0F, 34.0F, 0.0F, true);

		outer_roof_6_r3 = new ModelPart(this);
		outer_roof_6_r3.setPivot(-4.0F, -41.375F, -3.0F);
		roof_head_exterior.addChild(outer_roof_6_r3);
		setRotationAngle(outer_roof_6_r3, 0.0F, 0.0F, -0.0873F);
		outer_roof_6_r3.setTextureOffset(27, 101).addCuboid(-6.0F, 0.0F, -23.0F, 6.0F, 0.0F, 34.0F, 0.0F, false);

		outer_roof_5_r4 = new ModelPart(this);
		outer_roof_5_r4.setPivot(-12.392F, -40.205F, -3.0F);
		roof_head_exterior.addChild(outer_roof_5_r4);
		setRotationAngle(outer_roof_5_r4, 0.0F, 0.0F, -0.2618F);
		outer_roof_5_r4.setTextureOffset(39, 101).addCuboid(-2.5F, 0.0F, -23.0F, 5.0F, 0.0F, 34.0F, 0.0F, false);

		outer_roof_4_r4 = new ModelPart(this);
		outer_roof_4_r4.setPivot(-16.2163F, -39.0449F, -3.0F);
		roof_head_exterior.addChild(outer_roof_4_r4);
		setRotationAngle(outer_roof_4_r4, 0.0F, 0.0F, -0.3491F);
		outer_roof_4_r4.setTextureOffset(53, 101).addCuboid(-1.5F, 0.0F, -23.0F, 3.0F, 0.0F, 34.0F, 0.0F, false);

		outer_roof_3_r4 = new ModelPart(this);
		outer_roof_3_r4.setPivot(-18.2687F, -37.7659F, -3.0F);
		roof_head_exterior.addChild(outer_roof_3_r4);
		setRotationAngle(outer_roof_3_r4, 0.0F, 0.0F, -0.8727F);
		outer_roof_3_r4.setTextureOffset(49, 101).addCuboid(-1.0F, 0.0F, -23.0F, 2.0F, 0.0F, 34.0F, 0.0F, false);

		outer_roof_2_r3 = new ModelPart(this);
		outer_roof_2_r3.setPivot(-18.6114F, -35.9228F, -3.0F);
		roof_head_exterior.addChild(outer_roof_2_r3);
		setRotationAngle(outer_roof_2_r3, 0.0F, 0.0F, -1.3788F);
		outer_roof_2_r3.setTextureOffset(273, 57).addCuboid(-1.0F, -0.5F, -23.0F, 2.0F, 1.0F, 34.0F, 0.0F, false);

		roof_window = new ModelPart(this);
		roof_window.setPivot(0.0F, 24.0F, 0.0F);
		roof_window.setTextureOffset(78, 0).addCuboid(-18.0F, -33.0F, -24.0F, 2.0F, 0.0F, 48.0F, 0.0F, false);
		roof_window.setTextureOffset(0, 206).addCuboid(-11.0F, -36.0F, -24.0F, 1.0F, 1.0F, 48.0F, 0.0F, false);
		roof_window.setTextureOffset(13, 0).addCuboid(-9.0F, -37.0F, -24.0F, 9.0F, 0.0F, 48.0F, 0.0F, false);

		inner_roof_4_r1 = new ModelPart(this);
		inner_roof_4_r1.setPivot(-10.0F, -35.0F, 0.0F);
		roof_window.addChild(inner_roof_4_r1);
		setRotationAngle(inner_roof_4_r1, 0.0F, 0.0F, -0.829F);
		inner_roof_4_r1.setTextureOffset(0, 78).addCuboid(0.0F, 0.0F, -24.0F, 3.0F, 0.0F, 48.0F, 0.0F, false);

		inner_roof_2_r1 = new ModelPart(this);
		inner_roof_2_r1.setPivot(-16.0F, -33.0F, 0.0F);
		roof_window.addChild(inner_roof_2_r1);
		setRotationAngle(inner_roof_2_r1, 0.0F, 0.0F, -0.9163F);
		inner_roof_2_r1.setTextureOffset(43, 0).addCuboid(0.0F, 0.0F, -24.0F, 6.0F, 0.0F, 48.0F, 0.0F, false);

		roof_door = new ModelPart(this);
		roof_door.setPivot(0.0F, 24.0F, 0.0F);
		roof_door.setTextureOffset(12, 0).addCuboid(-18.0F, -33.0F, -16.0F, 2.0F, 0.0F, 32.0F, 0.0F, false);
		roof_door.setTextureOffset(211, 240).addCuboid(-11.0F, -36.0F, -16.0F, 1.0F, 1.0F, 32.0F, 0.0F, false);
		roof_door.setTextureOffset(98, 0).addCuboid(-9.0F, -37.0F, -16.0F, 9.0F, 0.0F, 32.0F, 0.0F, false);

		inner_roof_4_r2 = new ModelPart(this);
		inner_roof_4_r2.setPivot(-10.0F, -35.0F, 0.0F);
		roof_door.addChild(inner_roof_4_r2);
		setRotationAngle(inner_roof_4_r2, 0.0F, 0.0F, -0.829F);
		inner_roof_4_r2.setTextureOffset(61, 101).addCuboid(0.0F, 0.0F, -16.0F, 3.0F, 0.0F, 32.0F, 0.0F, false);

		inner_roof_2_r2 = new ModelPart(this);
		inner_roof_2_r2.setPivot(-16.0F, -33.0F, 0.0F);
		roof_door.addChild(inner_roof_2_r2);
		setRotationAngle(inner_roof_2_r2, 0.0F, 0.0F, -0.9163F);
		inner_roof_2_r2.setTextureOffset(0, 4).addCuboid(0.0F, 0.0F, -16.0F, 6.0F, 0.0F, 32.0F, 0.0F, false);

		roof_end = new ModelPart(this);
		roof_end.setPivot(0.0F, 24.0F, 0.0F);
		roof_end.setTextureOffset(41, 148).addCuboid(-18.0F, -33.0F, -8.0F, 2.0F, 0.0F, 16.0F, 0.0F, false);
		roof_end.setTextureOffset(130, 227).addCuboid(-11.0F, -36.0F, -8.0F, 1.0F, 1.0F, 16.0F, 0.0F, false);
		roof_end.setTextureOffset(114, 32).addCuboid(-9.0F, -37.0F, -8.0F, 9.0F, 0.0F, 16.0F, 0.0F, false);
		roof_end.setTextureOffset(114, 32).addCuboid(0.0F, -37.0F, -8.0F, 9.0F, 0.0F, 16.0F, 0.0F, true);
		roof_end.setTextureOffset(130, 227).addCuboid(10.0F, -36.0F, -8.0F, 1.0F, 1.0F, 16.0F, 0.0F, true);
		roof_end.setTextureOffset(41, 148).addCuboid(16.0F, -33.0F, -8.0F, 2.0F, 0.0F, 16.0F, 0.0F, true);

		inner_roof_9_r1 = new ModelPart(this);
		inner_roof_9_r1.setPivot(16.0F, -33.0F, 0.0F);
		roof_end.addChild(inner_roof_9_r1);
		setRotationAngle(inner_roof_9_r1, 0.0F, 0.0F, 0.9163F);
		inner_roof_9_r1.setTextureOffset(41, 171).addCuboid(-6.0F, 0.0F, -8.0F, 6.0F, 0.0F, 16.0F, 0.0F, true);

		inner_roof_7_r1 = new ModelPart(this);
		inner_roof_7_r1.setPivot(10.0F, -35.0F, 0.0F);
		roof_end.addChild(inner_roof_7_r1);
		setRotationAngle(inner_roof_7_r1, 0.0F, 0.0F, 0.829F);
		inner_roof_7_r1.setTextureOffset(0, 16).addCuboid(-3.0F, 0.0F, -8.0F, 3.0F, 0.0F, 16.0F, 0.0F, true);

		inner_roof_4_r3 = new ModelPart(this);
		inner_roof_4_r3.setPivot(-10.0F, -35.0F, 0.0F);
		roof_end.addChild(inner_roof_4_r3);
		setRotationAngle(inner_roof_4_r3, 0.0F, 0.0F, -0.829F);
		inner_roof_4_r3.setTextureOffset(0, 16).addCuboid(0.0F, 0.0F, -8.0F, 3.0F, 0.0F, 16.0F, 0.0F, false);

		inner_roof_2_r3 = new ModelPart(this);
		inner_roof_2_r3.setPivot(-16.0F, -33.0F, 0.0F);
		roof_end.addChild(inner_roof_2_r3);
		setRotationAngle(inner_roof_2_r3, 0.0F, 0.0F, -0.9163F);
		inner_roof_2_r3.setTextureOffset(41, 171).addCuboid(0.0F, 0.0F, -8.0F, 6.0F, 0.0F, 16.0F, 0.0F, false);

		roof_exterior_window = new ModelPart(this);
		roof_exterior_window.setPivot(0.0F, 24.0F, 0.0F);
		roof_exterior_window.setTextureOffset(0, 0).addCuboid(-4.0F, -41.375F, -24.0F, 4.0F, 0.0F, 48.0F, 0.0F, false);

		outer_roof_5_r5 = new ModelPart(this);
		outer_roof_5_r5.setPivot(-4.0F, -41.375F, 0.0F);
		roof_exterior_window.addChild(outer_roof_5_r5);
		setRotationAngle(outer_roof_5_r5, 0.0F, 0.0F, -0.0873F);
		outer_roof_5_r5.setTextureOffset(31, 0).addCuboid(-6.0F, 0.0F, -24.0F, 6.0F, 0.0F, 48.0F, 0.0F, false);

		outer_roof_4_r5 = new ModelPart(this);
		outer_roof_4_r5.setPivot(-12.392F, -40.205F, 0.0F);
		roof_exterior_window.addChild(outer_roof_4_r5);
		setRotationAngle(outer_roof_4_r5, 0.0F, 0.0F, -0.2618F);
		outer_roof_4_r5.setTextureOffset(55, 0).addCuboid(-2.5F, 0.0F, -24.0F, 5.0F, 0.0F, 48.0F, 0.0F, false);

		outer_roof_3_r5 = new ModelPart(this);
		outer_roof_3_r5.setPivot(-16.2163F, -39.0449F, 0.0F);
		roof_exterior_window.addChild(outer_roof_3_r5);
		setRotationAngle(outer_roof_3_r5, 0.0F, 0.0F, -0.3491F);
		outer_roof_3_r5.setTextureOffset(72, 0).addCuboid(-1.5F, 0.0F, -24.0F, 3.0F, 0.0F, 48.0F, 0.0F, false);

		outer_roof_2_r4 = new ModelPart(this);
		outer_roof_2_r4.setPivot(-18.2687F, -37.7659F, 0.0F);
		roof_exterior_window.addChild(outer_roof_2_r4);
		setRotationAngle(outer_roof_2_r4, 0.0F, 0.0F, -0.8727F);
		outer_roof_2_r4.setTextureOffset(65, 0).addCuboid(-1.0F, 0.0F, -24.0F, 2.0F, 0.0F, 48.0F, 0.0F, false);

		outer_roof_1_r2 = new ModelPart(this);
		outer_roof_1_r2.setPivot(-18.6114F, -35.9228F, 0.0F);
		roof_exterior_window.addChild(outer_roof_1_r2);
		setRotationAngle(outer_roof_1_r2, 0.0F, 0.0F, -1.3788F);
		outer_roof_1_r2.setTextureOffset(191, 100).addCuboid(-1.0F, -0.5F, -24.0F, 2.0F, 1.0F, 48.0F, 0.0F, false);

		roof_exterior_door = new ModelPart(this);
		roof_exterior_door.setPivot(0.0F, 24.0F, 0.0F);
		roof_exterior_door.setTextureOffset(16, 0).addCuboid(-4.0F, -41.375F, -16.0F, 4.0F, 0.0F, 32.0F, 0.0F, false);

		outer_roof_6_r4 = new ModelPart(this);
		outer_roof_6_r4.setPivot(-4.0F, -41.375F, 0.0F);
		roof_exterior_door.addChild(outer_roof_6_r4);
		setRotationAngle(outer_roof_6_r4, 0.0F, 0.0F, -0.0873F);
		outer_roof_6_r4.setTextureOffset(47, 0).addCuboid(-6.0F, 0.0F, -16.0F, 6.0F, 0.0F, 32.0F, 0.0F, false);

		outer_roof_5_r6 = new ModelPart(this);
		outer_roof_5_r6.setPivot(-12.392F, -40.205F, 0.0F);
		roof_exterior_door.addChild(outer_roof_5_r6);
		setRotationAngle(outer_roof_5_r6, 0.0F, 0.0F, -0.2618F);
		outer_roof_5_r6.setTextureOffset(71, 0).addCuboid(-2.5F, 0.0F, -16.0F, 5.0F, 0.0F, 32.0F, 0.0F, false);

		outer_roof_4_r6 = new ModelPart(this);
		outer_roof_4_r6.setPivot(-16.2163F, -39.0449F, 0.0F);
		roof_exterior_door.addChild(outer_roof_4_r6);
		setRotationAngle(outer_roof_4_r6, 0.0F, 0.0F, -0.3491F);
		outer_roof_4_r6.setTextureOffset(88, 0).addCuboid(-1.5F, 0.0F, -16.0F, 3.0F, 0.0F, 32.0F, 0.0F, false);

		outer_roof_3_r6 = new ModelPart(this);
		outer_roof_3_r6.setPivot(-18.2687F, -37.7659F, 0.0F);
		roof_exterior_door.addChild(outer_roof_3_r6);
		setRotationAngle(outer_roof_3_r6, 0.0F, 0.0F, -0.8727F);
		outer_roof_3_r6.setTextureOffset(81, 0).addCuboid(-1.0F, 0.0F, -16.0F, 2.0F, 0.0F, 32.0F, 0.0F, false);

		outer_roof_2_r5 = new ModelPart(this);
		outer_roof_2_r5.setPivot(-18.6114F, -35.9228F, 0.0F);
		roof_exterior_door.addChild(outer_roof_2_r5);
		setRotationAngle(outer_roof_2_r5, 0.0F, 0.0F, -1.3788F);
		outer_roof_2_r5.setTextureOffset(207, 116).addCuboid(-1.0F, -0.5F, -16.0F, 2.0F, 1.0F, 32.0F, 0.0F, false);

		roof_head = new ModelPart(this);
		roof_head.setPivot(0.0F, 24.0F, 0.0F);
		roof_head.setTextureOffset(6, 16).addCuboid(-18.0F, -33.0F, 2.0F, 2.0F, 0.0F, 6.0F, 0.0F, false);
		roof_head.setTextureOffset(8, 26).addCuboid(-11.0F, -36.0F, 2.0F, 1.0F, 1.0F, 6.0F, 0.0F, false);
		roof_head.setTextureOffset(55, 50).addCuboid(-9.0F, -37.0F, 2.0F, 9.0F, 0.0F, 6.0F, 0.0F, false);
		roof_head.setTextureOffset(55, 50).addCuboid(0.0F, -37.0F, 2.0F, 9.0F, 0.0F, 6.0F, 0.0F, true);
		roof_head.setTextureOffset(8, 26).addCuboid(10.0F, -36.0F, 2.0F, 1.0F, 1.0F, 6.0F, 0.0F, true);
		roof_head.setTextureOffset(6, 16).addCuboid(16.0F, -33.0F, 2.0F, 2.0F, 0.0F, 6.0F, 0.0F, true);

		inner_roof_9_r2 = new ModelPart(this);
		inner_roof_9_r2.setPivot(16.0F, -33.0F, 0.0F);
		roof_head.addChild(inner_roof_9_r2);
		setRotationAngle(inner_roof_9_r2, 0.0F, 0.0F, 0.9163F);
		inner_roof_9_r2.setTextureOffset(91, 50).addCuboid(-6.0F, 0.0F, 2.0F, 6.0F, 0.0F, 6.0F, 0.0F, true);

		inner_roof_7_r2 = new ModelPart(this);
		inner_roof_7_r2.setPivot(10.0F, -35.0F, 0.0F);
		roof_head.addChild(inner_roof_7_r2);
		setRotationAngle(inner_roof_7_r2, 0.0F, 0.0F, 0.829F);
		inner_roof_7_r2.setTextureOffset(0, 16).addCuboid(-3.0F, 0.0F, 2.0F, 3.0F, 0.0F, 6.0F, 0.0F, true);

		inner_roof_4_r4 = new ModelPart(this);
		inner_roof_4_r4.setPivot(-10.0F, -35.0F, 0.0F);
		roof_head.addChild(inner_roof_4_r4);
		setRotationAngle(inner_roof_4_r4, 0.0F, 0.0F, -0.829F);
		inner_roof_4_r4.setTextureOffset(0, 16).addCuboid(0.0F, 0.0F, 2.0F, 3.0F, 0.0F, 6.0F, 0.0F, false);

		inner_roof_2_r4 = new ModelPart(this);
		inner_roof_2_r4.setPivot(-16.0F, -33.0F, 0.0F);
		roof_head.addChild(inner_roof_2_r4);
		setRotationAngle(inner_roof_2_r4, 0.0F, 0.0F, -0.9163F);
		inner_roof_2_r4.setTextureOffset(91, 50).addCuboid(0.0F, 0.0F, 2.0F, 6.0F, 0.0F, 6.0F, 0.0F, false);

		roof_window_light = new ModelPart(this);
		roof_window_light.setPivot(0.0F, 24.0F, 0.0F);
		roof_window_light.setTextureOffset(177, 49).addCuboid(-12.4F, -38.5F, -24.0F, 2.0F, 3.0F, 48.0F, 0.0F, false);

		roof_door_light = new ModelPart(this);
		roof_door_light.setPivot(0.0F, 24.0F, 0.0F);
		roof_door_light.setTextureOffset(193, 65).addCuboid(-12.4F, -38.5F, -16.0F, 2.0F, 3.0F, 32.0F, 0.0F, false);

		roof_end_light = new ModelPart(this);
		roof_end_light.setPivot(0.0F, 24.0F, 0.0F);
		roof_end_light.setTextureOffset(206, 97).addCuboid(-9.0F, -34.75F, -8.1F, 18.0F, 0.0F, 3.0F, 0.0F, false);
		roof_end_light.setTextureOffset(212, 84).addCuboid(-12.4F, -38.5F, -5.0F, 2.0F, 3.0F, 13.0F, 0.0F, false);
		roof_end_light.setTextureOffset(212, 84).addCuboid(10.4F, -38.5F, -5.0F, 2.0F, 3.0F, 13.0F, 0.0F, true);

		roof_head_light = new ModelPart(this);
		roof_head_light.setPivot(0.0F, 24.0F, 0.0F);
		roof_head_light.setTextureOffset(219, 91).addCuboid(-12.4F, -38.5F, 2.0F, 2.0F, 3.0F, 6.0F, 0.0F, false);
		roof_head_light.setTextureOffset(219, 91).addCuboid(10.4F, -38.5F, 2.0F, 2.0F, 3.0F, 6.0F, 0.0F, true);

		handrail_door_type_1 = new ModelPart(this);
		handrail_door_type_1.setPivot(0.0F, 24.0F, 0.0F);
		handrail_door_type_1.setTextureOffset(0, 0).addCuboid(0.0F, -37.0F, 0.0F, 0.0F, 37.0F, 0.0F, 0.2F, false);

		handrail_door_type_2 = new ModelPart(this);
		handrail_door_type_2.setPivot(0.0F, 24.0F, 0.0F);
		handrail_door_type_2.setTextureOffset(0, 0).addCuboid(0.0F, -14.25F, 0.0F, 0.0F, 15.0F, 0.0F, 0.2F, false);
		handrail_door_type_2.setTextureOffset(0, 0).addCuboid(0.0F, -37.0F, 0.0F, 0.0F, 6.0F, 0.0F, 0.2F, false);

		handrail_curve = new ModelPart(this);
		handrail_curve.setPivot(0.175F, 3.25F, 15.1F);
		handrail_door_type_2.addChild(handrail_curve);
		handrail_curve.setTextureOffset(0, 0).addCuboid(-0.175F, -34.1585F, -16.6F, 0.0F, 0.0F, 3.0F, 0.2F, false);
		handrail_curve.setTextureOffset(0, 0).addCuboid(-0.175F, -17.5F, -16.6F, 0.0F, 0.0F, 3.0F, 0.2F, false);

		handrail_curve_12_r1 = new ModelPart(this);
		handrail_curve_12_r1.setPivot(-0.175F, -17.7828F, -17.1464F);
		handrail_curve.addChild(handrail_curve_12_r1);
		setRotationAngle(handrail_curve_12_r1, -0.7854F, 0.0F, 0.0F);
		handrail_curve_12_r1.setTextureOffset(0, 0).addCuboid(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_curve_10_r1 = new ModelPart(this);
		handrail_curve_10_r1.setPivot(-0.175F, -18.056F, -17.356F);
		handrail_curve.addChild(handrail_curve_10_r1);
		setRotationAngle(handrail_curve_10_r1, -1.0472F, 0.0F, 0.0F);
		handrail_curve_10_r1.setTextureOffset(0, 0).addCuboid(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_curve_9_r1 = new ModelPart(this);
		handrail_curve_9_r1.setPivot(-0.175F, -17.5732F, -16.8732F);
		handrail_curve.addChild(handrail_curve_9_r1);
		setRotationAngle(handrail_curve_9_r1, -0.5236F, 0.0F, 0.0F);
		handrail_curve_9_r1.setTextureOffset(0, 0).addCuboid(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_curve_7_r1 = new ModelPart(this);
		handrail_curve_7_r1.setPivot(-0.175F, -25.8043F, -17.4293F);
		handrail_curve.addChild(handrail_curve_7_r1);
		setRotationAngle(handrail_curve_7_r1, 0.0F, 0.0F, -1.5708F);
		handrail_curve_7_r1.setTextureOffset(0, 0).addCuboid(-7.5F, 0.0F, 0.0F, 15.0F, 0.0F, 0.0F, 0.2F, false);
		handrail_curve_7_r1.setTextureOffset(0, 0).addCuboid(-7.5F, 0.0F, 4.6585F, 15.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_curve_6_r1 = new ModelPart(this);
		handrail_curve_6_r1.setPivot(-0.175F, -33.6025F, -17.356F);
		handrail_curve.addChild(handrail_curve_6_r1);
		setRotationAngle(handrail_curve_6_r1, 1.0472F, 0.0F, 0.0F);
		handrail_curve_6_r1.setTextureOffset(0, 0).addCuboid(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_curve_5_r1 = new ModelPart(this);
		handrail_curve_5_r1.setPivot(-0.175F, -33.8757F, -17.1464F);
		handrail_curve.addChild(handrail_curve_5_r1);
		setRotationAngle(handrail_curve_5_r1, 0.7854F, 0.0F, 0.0F);
		handrail_curve_5_r1.setTextureOffset(0, 0).addCuboid(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_curve_3_r1 = new ModelPart(this);
		handrail_curve_3_r1.setPivot(-0.175F, -34.0853F, -16.8732F);
		handrail_curve.addChild(handrail_curve_3_r1);
		setRotationAngle(handrail_curve_3_r1, 0.5236F, 0.0F, 0.0F);
		handrail_curve_3_r1.setTextureOffset(0, 0).addCuboid(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_curve_11_r1 = new ModelPart(this);
		handrail_curve_11_r1.setPivot(-0.175F, -17.7828F, -13.0536F);
		handrail_curve.addChild(handrail_curve_11_r1);
		setRotationAngle(handrail_curve_11_r1, 0.7854F, 0.0F, 0.0F);
		handrail_curve_11_r1.setTextureOffset(0, 0).addCuboid(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_curve_9_r2 = new ModelPart(this);
		handrail_curve_9_r2.setPivot(-0.175F, -18.056F, -12.844F);
		handrail_curve.addChild(handrail_curve_9_r2);
		setRotationAngle(handrail_curve_9_r2, 1.0472F, 0.0F, 0.0F);
		handrail_curve_9_r2.setTextureOffset(0, 0).addCuboid(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_curve_8_r1 = new ModelPart(this);
		handrail_curve_8_r1.setPivot(-0.175F, -17.5732F, -13.3268F);
		handrail_curve.addChild(handrail_curve_8_r1);
		setRotationAngle(handrail_curve_8_r1, 0.5236F, 0.0F, 0.0F);
		handrail_curve_8_r1.setTextureOffset(0, 0).addCuboid(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_curve_5_r2 = new ModelPart(this);
		handrail_curve_5_r2.setPivot(-0.175F, -33.6025F, -12.844F);
		handrail_curve.addChild(handrail_curve_5_r2);
		setRotationAngle(handrail_curve_5_r2, -1.0472F, 0.0F, 0.0F);
		handrail_curve_5_r2.setTextureOffset(0, 0).addCuboid(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_curve_4_r1 = new ModelPart(this);
		handrail_curve_4_r1.setPivot(-0.175F, -33.8757F, -13.0536F);
		handrail_curve.addChild(handrail_curve_4_r1);
		setRotationAngle(handrail_curve_4_r1, -0.7854F, 0.0F, 0.0F);
		handrail_curve_4_r1.setTextureOffset(0, 0).addCuboid(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_curve_2_r1 = new ModelPart(this);
		handrail_curve_2_r1.setPivot(-0.175F, -34.0853F, -13.3268F);
		handrail_curve.addChild(handrail_curve_2_r1);
		setRotationAngle(handrail_curve_2_r1, -0.5236F, 0.0F, 0.0F);
		handrail_curve_2_r1.setTextureOffset(0, 0).addCuboid(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2F, false);

		roof_handle = new ModelPart(this);
		roof_handle.setPivot(0.0F, 24.0F, 0.0F);
		roof_handle.setTextureOffset(0, 0).addCuboid(-4.0F, -37.2F, -24.0F, 0.0F, 2.0F, 0.0F, 0.2F, false);
		roof_handle.setTextureOffset(0, 0).addCuboid(-4.0F, -37.2F, -8.0F, 0.0F, 2.0F, 0.0F, 0.2F, false);
		roof_handle.setTextureOffset(0, 0).addCuboid(-4.0F, -37.2F, 8.0F, 0.0F, 2.0F, 0.0F, 0.2F, false);
		roof_handle.setTextureOffset(0, 0).addCuboid(-4.0F, -37.2F, 24.0F, 0.0F, 2.0F, 0.0F, 0.2F, false);
		roof_handle.setTextureOffset(0, 0).addCuboid(4.0F, -37.2F, -24.0F, 0.0F, 2.0F, 0.0F, 0.2F, false);
		roof_handle.setTextureOffset(0, 0).addCuboid(4.0F, -37.2F, -8.0F, 0.0F, 2.0F, 0.0F, 0.2F, false);
		roof_handle.setTextureOffset(0, 0).addCuboid(4.0F, -37.2F, 8.0F, 0.0F, 2.0F, 0.0F, 0.2F, false);
		roof_handle.setTextureOffset(0, 0).addCuboid(4.0F, -37.2F, 24.0F, 0.0F, 2.0F, 0.0F, 0.2F, false);

		roof_handrail_curve_19_r1 = new ModelPart(this);
		roof_handrail_curve_19_r1.setPivot(0.2215F, -35.0F, -31.2785F);
		roof_handle.addChild(roof_handrail_curve_19_r1);
		setRotationAngle(roof_handrail_curve_19_r1, 0.0F, 1.5708F, 0.0F);
		roof_handrail_curve_19_r1.setTextureOffset(0, 0).addCuboid(0.0F, 0.0F, -0.5F, 0.0F, 0.0F, 1.0F, 0.2F, false);

		roof_handrail_curve_22_r1 = new ModelPart(this);
		roof_handrail_curve_22_r1.setPivot(1.8608F, -35.0F, -30.7053F);
		roof_handle.addChild(roof_handrail_curve_22_r1);
		setRotationAngle(roof_handrail_curve_22_r1, 0.0F, 1.0472F, 0.0F);
		roof_handrail_curve_22_r1.setTextureOffset(0, 0).addCuboid(0.0F, 0.0F, -1.0F, 0.0F, 0.0F, 2.0F, 0.2F, false);

		roof_handrail_curve_23_r1 = new ModelPart(this);
		roof_handrail_curve_23_r1.setPivot(3.4268F, -35.0F, -29.1392F);
		roof_handle.addChild(roof_handrail_curve_23_r1);
		setRotationAngle(roof_handrail_curve_23_r1, 0.0F, 0.5236F, 0.0F);
		roof_handrail_curve_23_r1.setTextureOffset(0, 0).addCuboid(0.0F, 0.0F, -1.0F, 0.0F, 0.0F, 2.0F, 0.2F, false);

		roof_handrail_curve_18_r1 = new ModelPart(this);
		roof_handrail_curve_18_r1.setPivot(-0.2215F, -35.0F, -31.2785F);
		roof_handle.addChild(roof_handrail_curve_18_r1);
		setRotationAngle(roof_handrail_curve_18_r1, 0.0F, -1.5708F, 0.0F);
		roof_handrail_curve_18_r1.setTextureOffset(0, 0).addCuboid(0.0F, 0.0F, -0.5F, 0.0F, 0.0F, 1.0F, 0.2F, false);

		roof_handrail_curve_21_r1 = new ModelPart(this);
		roof_handrail_curve_21_r1.setPivot(-1.8608F, -35.0F, -30.7053F);
		roof_handle.addChild(roof_handrail_curve_21_r1);
		setRotationAngle(roof_handrail_curve_21_r1, 0.0F, -1.0472F, 0.0F);
		roof_handrail_curve_21_r1.setTextureOffset(0, 0).addCuboid(0.0F, 0.0F, -1.0F, 0.0F, 0.0F, 2.0F, 0.2F, false);

		roof_handrail_curve_22_r2 = new ModelPart(this);
		roof_handrail_curve_22_r2.setPivot(-3.4268F, -35.0F, -29.1392F);
		roof_handle.addChild(roof_handrail_curve_22_r2);
		setRotationAngle(roof_handrail_curve_22_r2, 0.0F, -0.5236F, 0.0F);
		roof_handrail_curve_22_r2.setTextureOffset(0, 0).addCuboid(0.0F, 0.0F, -1.0F, 0.0F, 0.0F, 2.0F, 0.2F, false);

		roof_handrail_curve_18_r2 = new ModelPart(this);
		roof_handrail_curve_18_r2.setPivot(0.2215F, -35.0F, 31.2785F);
		roof_handle.addChild(roof_handrail_curve_18_r2);
		setRotationAngle(roof_handrail_curve_18_r2, 0.0F, -1.5708F, 0.0F);
		roof_handrail_curve_18_r2.setTextureOffset(0, 0).addCuboid(0.0F, 0.0F, -0.5F, 0.0F, 0.0F, 1.0F, 0.2F, false);

		roof_handrail_curve_21_r2 = new ModelPart(this);
		roof_handrail_curve_21_r2.setPivot(1.8608F, -35.0F, 30.7053F);
		roof_handle.addChild(roof_handrail_curve_21_r2);
		setRotationAngle(roof_handrail_curve_21_r2, 0.0F, -1.0472F, 0.0F);
		roof_handrail_curve_21_r2.setTextureOffset(0, 0).addCuboid(0.0F, 0.0F, -1.0F, 0.0F, 0.0F, 2.0F, 0.2F, false);

		roof_handrail_curve_22_r3 = new ModelPart(this);
		roof_handrail_curve_22_r3.setPivot(3.4268F, -35.0F, 29.1392F);
		roof_handle.addChild(roof_handrail_curve_22_r3);
		setRotationAngle(roof_handrail_curve_22_r3, 0.0F, -0.5236F, 0.0F);
		roof_handrail_curve_22_r3.setTextureOffset(0, 0).addCuboid(0.0F, 0.0F, -1.0F, 0.0F, 0.0F, 2.0F, 0.2F, false);

		roof_handrail_curve_17_r1 = new ModelPart(this);
		roof_handrail_curve_17_r1.setPivot(-0.2215F, -35.0F, 31.2785F);
		roof_handle.addChild(roof_handrail_curve_17_r1);
		setRotationAngle(roof_handrail_curve_17_r1, 0.0F, 1.5708F, 0.0F);
		roof_handrail_curve_17_r1.setTextureOffset(0, 0).addCuboid(0.0F, 0.0F, -0.5F, 0.0F, 0.0F, 1.0F, 0.2F, false);

		roof_handrail_curve_20_r1 = new ModelPart(this);
		roof_handrail_curve_20_r1.setPivot(-1.8608F, -35.0F, 30.7053F);
		roof_handle.addChild(roof_handrail_curve_20_r1);
		setRotationAngle(roof_handrail_curve_20_r1, 0.0F, 1.0472F, 0.0F);
		roof_handrail_curve_20_r1.setTextureOffset(0, 0).addCuboid(0.0F, 0.0F, -1.0F, 0.0F, 0.0F, 2.0F, 0.2F, false);

		roof_handrail_curve_21_r3 = new ModelPart(this);
		roof_handrail_curve_21_r3.setPivot(-3.4268F, -35.0F, 29.1392F);
		roof_handle.addChild(roof_handrail_curve_21_r3);
		setRotationAngle(roof_handrail_curve_21_r3, 0.0F, 0.5236F, 0.0F);
		roof_handrail_curve_21_r3.setTextureOffset(0, 0).addCuboid(0.0F, 0.0F, -1.0F, 0.0F, 0.0F, 2.0F, 0.2F, false);

		roof_handrail_2_r1 = new ModelPart(this);
		roof_handrail_2_r1.setPivot(4.0F, -35.0F, 0.0F);
		roof_handle.addChild(roof_handrail_2_r1);
		setRotationAngle(roof_handrail_2_r1, -1.5708F, 0.0F, 0.0F);
		roof_handrail_2_r1.setTextureOffset(0, 0).addCuboid(0.0F, -28.0F, 0.0F, 0.0F, 56.0F, 0.0F, 0.2F, false);
		roof_handrail_2_r1.setTextureOffset(0, 0).addCuboid(-8.0F, -28.0F, 0.0F, 0.0F, 56.0F, 0.0F, 0.2F, false);

		headlights = new ModelPart(this);
		headlights.setPivot(0.0F, 24.0F, 0.0F);


		front_right_panel_6_r1 = new ModelPart(this);
		front_right_panel_6_r1.setPivot(9.4774F, -21.0F, -25.7655F);
		headlights.addChild(front_right_panel_6_r1);
		setRotationAngle(front_right_panel_6_r1, 0.0F, -0.1745F, 0.0F);
		front_right_panel_6_r1.setTextureOffset(18, 33).addCuboid(-1.5F, 10.75F, 0.0F, 3.0F, 6.0F, 0.0F, 0.0F, true);

		front_right_panel_5_r2 = new ModelPart(this);
		front_right_panel_5_r2.setPivot(13.7738F, -20.5F, -24.4789F);
		headlights.addChild(front_right_panel_5_r2);
		setRotationAngle(front_right_panel_5_r2, 0.0F, -0.3491F, 0.0F);
		front_right_panel_5_r2.setTextureOffset(6, 33).addCuboid(-3.0F, 10.25F, 0.0F, 6.0F, 6.0F, 0.0F, 0.0F, true);

		front_right_panel_4_r4 = new ModelPart(this);
		front_right_panel_4_r4.setPivot(-9.4774F, -21.0F, -25.7655F);
		headlights.addChild(front_right_panel_4_r4);
		setRotationAngle(front_right_panel_4_r4, 0.0F, 0.1745F, 0.0F);
		front_right_panel_4_r4.setTextureOffset(18, 33).addCuboid(-1.5F, 10.75F, 0.0F, 3.0F, 6.0F, 0.0F, 0.0F, false);

		front_right_panel_3_r5 = new ModelPart(this);
		front_right_panel_3_r5.setPivot(-13.7738F, -20.5F, -24.4789F);
		headlights.addChild(front_right_panel_3_r5);
		setRotationAngle(front_right_panel_3_r5, 0.0F, 0.3491F, 0.0F);
		front_right_panel_3_r5.setTextureOffset(6, 33).addCuboid(-3.0F, 10.25F, 0.0F, 6.0F, 6.0F, 0.0F, 0.0F, false);

		tail_lights = new ModelPart(this);
		tail_lights.setPivot(0.0F, 24.0F, 0.0F);


		front_right_panel_5_r3 = new ModelPart(this);
		front_right_panel_5_r3.setPivot(13.7738F, -20.5F, -24.4789F);
		tail_lights.addChild(front_right_panel_5_r3);
		setRotationAngle(front_right_panel_5_r3, 0.0F, -0.3491F, 0.0F);
		front_right_panel_5_r3.setTextureOffset(18, 39).addCuboid(-3.0F, 9.5F, 0.0F, 6.0F, 6.0F, 0.0F, 0.0F, true);

		front_right_panel_4_r5 = new ModelPart(this);
		front_right_panel_4_r5.setPivot(19.1907F, -20.0F, -21.9519F);
		tail_lights.addChild(front_right_panel_4_r5);
		setRotationAngle(front_right_panel_4_r5, 0.0F, -0.5236F, 0.0F);
		front_right_panel_4_r5.setTextureOffset(6, 39).addCuboid(-3.0F, 9.0F, 0.0F, 6.0F, 6.0F, 0.0F, 0.0F, true);

		front_right_panel_3_r6 = new ModelPart(this);
		front_right_panel_3_r6.setPivot(-13.7738F, -20.5F, -24.4789F);
		tail_lights.addChild(front_right_panel_3_r6);
		setRotationAngle(front_right_panel_3_r6, 0.0F, 0.3491F, 0.0F);
		front_right_panel_3_r6.setTextureOffset(18, 39).addCuboid(-3.0F, 9.5F, 0.0F, 6.0F, 6.0F, 0.0F, 0.0F, false);

		front_right_panel_2_r4 = new ModelPart(this);
		front_right_panel_2_r4.setPivot(-19.1907F, -20.0F, -21.9519F);
		tail_lights.addChild(front_right_panel_2_r4);
		setRotationAngle(front_right_panel_2_r4, 0.0F, 0.5236F, 0.0F);
		front_right_panel_2_r4.setTextureOffset(6, 39).addCuboid(-3.0F, 9.0F, 0.0F, 6.0F, 6.0F, 0.0F, 0.0F, false);

		door_light = new ModelPart(this);
		door_light.setPivot(0.0F, 24.0F, 0.0F);


		light_plate_1_r1 = new ModelPart(this);
		light_plate_1_r1.setPivot(-20.4294F, -31.1645F, 0.0F);
		door_light.addChild(light_plate_1_r1);
		setRotationAngle(light_plate_1_r1, 0.0F, 0.0F, 1.5708F);
		light_plate_1_r1.setTextureOffset(0, 45).addCuboid(-1.5F, -1.1F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);

		door_light_on = new ModelPart(this);
		door_light_on.setPivot(0.0F, 24.0F, 0.0F);


		light_r1 = new ModelPart(this);
		light_r1.setPivot(-20.4294F, -31.1645F, -1.025F);
		door_light_on.addChild(light_r1);
		setRotationAngle(light_r1, 0.0F, 0.0F, 1.5708F);
		light_r1.setTextureOffset(48, 12).addCuboid(-1.5F, -1.1F, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, false);

		door_light_off = new ModelPart(this);
		door_light_off.setPivot(0.0F, 24.0F, 0.0F);


		light_r2 = new ModelPart(this);
		light_r2.setPivot(-20.4294F, -31.1645F, -1.025F);
		door_light_off.addChild(light_r2);
		setRotationAngle(light_r2, 0.0F, 0.0F, 1.5708F);
		light_r2.setTextureOffset(48, 10).addCuboid(-1.5F, -1.1F, 0.01F, 1.0F, 1.0F, 0.0F, 0.0F, false);
	}

	private static final int DOOR_MAX = 12;

	@Override
	protected void renderWindowPositions(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, boolean isEnd1Head, boolean isEnd2Head) {
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
			case INTERIOR_TRANSLUCENT:
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
				door_right.setPivot(doorRightX, 0, doorRightZ);
				door_left.setPivot(doorRightX, 0, -doorRightZ);
				renderOnce(door, matrices, vertices, light, position);
				door_right.setPivot(doorLeftX, 0, doorLeftZ);
				door_left.setPivot(doorLeftX, 0, -doorLeftZ);
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
				door_right_exterior.setPivot(doorRightX, 0, doorRightZ);
				door_left_exterior.setPivot(doorRightX, 0, -doorRightZ);
				renderOnce(door_exterior, matrices, vertices, light, position);
				door_right_exterior.setPivot(doorLeftX, 0, doorLeftZ);
				door_left_exterior.setPivot(doorLeftX, 0, -doorLeftZ);
				renderOnceFlipped(door_exterior, matrices, vertices, light, position);
				renderMirror(roof_exterior_door, matrices, vertices, light, position);
				break;
		}
	}

	@Override
	protected void renderHeadPosition1(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, boolean useHeadlights) {
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
	protected void renderHeadPosition2(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, boolean useHeadlights) {
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
	protected void renderEndPosition1(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails) {
		switch (renderStage) {
			case LIGHTS:
				renderOnce(roof_end_light, matrices, vertices, light, position);
				break;
			case INTERIOR:
				renderOnce(end, matrices, vertices, light, position);
				if (renderDetails) {
					renderOnce(roof_end, matrices, vertices, light, position);
					renderOnce(end_handrails, matrices, vertices, light, position);
				}
				break;
			case INTERIOR_TRANSLUCENT:
				renderMirror(side_panel, matrices, vertices, light, position + 11);
				break;
			case EXTERIOR:
				renderOnce(end_exterior, matrices, vertices, light, position);
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
					renderOnceFlipped(end_handrails, matrices, vertices, light, position);
				}
				break;
			case INTERIOR_TRANSLUCENT:
				renderMirror(side_panel, matrices, vertices, light, position - 11);
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
