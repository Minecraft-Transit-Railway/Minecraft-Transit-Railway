        package mtr.model;

        import net.minecraft.client.model.ModelPart;
	import net.minecraft.client.render.VertexConsumer;
	import net.minecraft.client.util.math.MatrixStack;

	public class ModelR179Alpha6 extends ModelTrainBase {
        
        private final ModelPart window;
	private final ModelPart wall;
	private final ModelPart wall_10_r1;
	private final ModelPart wall_9_r1;
	private final ModelPart wall_8_r1;
	private final ModelPart wall_7_r1;
	private final ModelPart wall_6_r1;
	private final ModelPart wall_5_r1;
	private final ModelPart wall_4_r1;
	private final ModelPart wall_2_r1;
	private final ModelPart Turns;
	private final ModelPart window_curved_1;
	private final ModelPart window_curved_side_2_r1;
	private final ModelPart window_curved_side_1_r1;
	private final ModelPart window_curved_2;
	private final ModelPart window_curved_side_2_r2;
	private final ModelPart window_curved_side_1_r2;
	private final ModelPart window_curved_3;
	private final ModelPart window_curved_side_2_r3;
	private final ModelPart window_curved_side_1_r3;
	private final ModelPart window_curved_4;
	private final ModelPart window_curved_side_2_r4;
	private final ModelPart window_curved_side_1_r4;
	private final ModelPart seat;
	private final ModelPart seat_bottom_r1;
	private final ModelPart seat_back_3_r1;
	private final ModelPart seat_back_2_r1;
	private final ModelPart handrail_window;
	private final ModelPart handrail_mid;
	private final ModelPart handrail_middle_4_r1;
	private final ModelPart handrail_middle_3_r1;
	private final ModelPart handrail_middle_2_r1;
	private final ModelPart handrail_middle_1_r1;
	private final ModelPart handrail_turn_5_r1;
	private final ModelPart handrail_turn_4_r1;
	private final ModelPart handrail_turn_3_r1;
	private final ModelPart handrail_turn_2_r1;
	private final ModelPart handrail_turn_1_r1;
	private final ModelPart headrail_right;
	private final ModelPart handrail_left_3_r1;
	private final ModelPart handrail_right_1_r1;
	private final ModelPart handrail_turn_4_r2;
	private final ModelPart handrail_turn_3_r2;
	private final ModelPart handrail_turn_2_r2;
	private final ModelPart handrail_turn_1_r2;
	private final ModelPart headrail_left;
	private final ModelPart handrail_right_3_r1;
	private final ModelPart handrail_right_1_r2;
	private final ModelPart handrail_turn_4_r3;
	private final ModelPart handrail_turn_3_r3;
	private final ModelPart handrail_turn_2_r3;
	private final ModelPart handrail_turn_1_r3;
	private final ModelPart headrail_up;
	private final ModelPart handrail_up_11_r1;
	private final ModelPart handrail_up_10_r1;
	private final ModelPart window_display;
	private final ModelPart display_panel_r1;
	private final ModelPart display_window_r1;
	private final ModelPart window_exterior;
	private final ModelPart upper_wall_r1;
	private final ModelPart window_exterior_side;
	private final ModelPart left_side_2_r1;
	private final ModelPart under_seatbox;
	private final ModelPart seatbox_2_r1;
	private final ModelPart side_panel;
	private final ModelPart roof_window;
	private final ModelPart inner_roof_4_r1;
	private final ModelPart inner_roof_2_r1;
	private final ModelPart roof_door;
	private final ModelPart inner_roof_4_r2;
	private final ModelPart inner_roof_2_r2;
	private final ModelPart roof_exterior;
	private final ModelPart outer_roof_5_r1;
	private final ModelPart outer_roof_4_r1;
	private final ModelPart outer_roof_3_r1;
	private final ModelPart outer_roof_2_r1;
	private final ModelPart outer_roof_1_r1;
	private final ModelPart roof_head;
	private final ModelPart head_roof_right;
	private final ModelPart inner_roof_4_r3;
	private final ModelPart inner_roof_2_r3;
	private final ModelPart head_roof_left;
	private final ModelPart inner_roof_4_r4;
	private final ModelPart inner_roof_2_r4;
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
	private final ModelPart door_side_top_2_r1;
	private final ModelPart door_side_top_1_r1;
	private final ModelPart door_side_bottom_2_r1;
	private final ModelPart handrail_door_type_1;
	private final ModelPart handrail_door_type_2;
	private final ModelPart handrail_curve;
	private final ModelPart handrail_curve_22_r1;
	private final ModelPart handrail_curve_21_r1;
	private final ModelPart handrail_curve_20_r1;
	private final ModelPart handrail_curve_19_r1;
	private final ModelPart handrail_curve_17_r1;
	private final ModelPart handrail_curve_16_r1;
	private final ModelPart handrail_curve_15_r1;
	private final ModelPart handrail_curve_14_r1;
	private final ModelPart handrail_curve_13_r1;
	private final ModelPart handrail_curve_11_r1;
	private final ModelPart handrail_curve_10_r1;
	private final ModelPart handrail_curve_9_r1;
	private final ModelPart handrail_curve_8_r1;
	private final ModelPart handrail_curve_5_r1;
	private final ModelPart handrail_curve_4_r1;
	private final ModelPart handrail_curve_3_r1;
	private final ModelPart handrail_curve_2_r1;
	private final ModelPart end;
	private final ModelPart end_door;
	private final ModelPart end_side_1;
	private final ModelPart upper_door_wall_r1;
	private final ModelPart upper_wall_r2;
	private final ModelPart end_side_2;
	private final ModelPart upper_door_wall_r2;
	private final ModelPart upper_wall_r3;
	private final ModelPart seat_end_right;
	private final ModelPart seat_bottom_r2;
	private final ModelPart seat_back_3_r2;
	private final ModelPart seat_back_2_r2;
	private final ModelPart handrail_end2;
	private final ModelPart handrail_top_4_r1;
	private final ModelPart handrail_top_3_r1;
	private final ModelPart handrail_top_2_r1;
	private final ModelPart handrail_left_3_r2;
	private final ModelPart handrail_left_1_r1;
	private final ModelPart handrail_turn_3_r4;
	private final ModelPart handrail_turn_2_r4;
	private final ModelPart handrail_turn_1_r4;
	private final ModelPart seat_end_left;
	private final ModelPart seat_bottom_r3;
	private final ModelPart seat_back_3_r3;
	private final ModelPart seat_back_2_r3;
	private final ModelPart handrail_end;
	private final ModelPart handrail_top_8_r1;
	private final ModelPart handrail_top_3_r2;
	private final ModelPart handrail_top_2_r2;
	private final ModelPart handrail_left_3_r3;
	private final ModelPart handrail_left_1_r2;
	private final ModelPart handrail_turn_3_r5;
	private final ModelPart handrail_turn_2_r5;
	private final ModelPart handrail_turn_1_r5;
	private final ModelPart end_light_right;
	private final ModelPart light_r1;
	private final ModelPart end_light_off_right;
	private final ModelPart light_r2;
	private final ModelPart end_light_on_right;
	private final ModelPart light_r3;
	private final ModelPart end_light_left;
	private final ModelPart light_r4;
	private final ModelPart end_light_off_left;
	private final ModelPart light_r5;
	private final ModelPart end_light_on_left;
	private final ModelPart light_r6;
	private final ModelPart end_exterior;
	private final ModelPart end_bottom_out;
	private final ModelPart buttom_panel_left_9_r1;
	private final ModelPart buttom_panel_left_8_r1;
	private final ModelPart buttom_panel_left_7_r1;
	private final ModelPart buttom_panel_left_6_r1;
	private final ModelPart buttom_panel_left_5_r1;
	private final ModelPart buttom_panel_left_4_r1;
	private final ModelPart buttom_panel_left_3_r1;
	private final ModelPart buttom_panel_left_2_r1;
	private final ModelPart buttom_panel_right_9_r1;
	private final ModelPart buttom_panel_right_8_r1;
	private final ModelPart buttom_panel_right_7_r1;
	private final ModelPart buttom_panel_right_6_r1;
	private final ModelPart buttom_panel_right_5_r1;
	private final ModelPart buttom_panel_right_4_r1;
	private final ModelPart buttom_panel_right_3_r1;
	private final ModelPart buttom_panel_right_2_r1;
	private final ModelPart end_back;
	private final ModelPart front_left_panel_3_r1;
	private final ModelPart front_left_panel_2_r1;
	private final ModelPart front_left_panel_1_r1;
	private final ModelPart front_right_panel_3_r1;
	private final ModelPart front_right_panel_2_r1;
	private final ModelPart front_right_panel_1_r1;
	private final ModelPart end_door_exterior;
	private final ModelPart end_handle_exterior_1;
	private final ModelPart handle_1_r1;
	private final ModelPart end_handle_exterior_2;
	private final ModelPart handle_2_r1;
	private final ModelPart end_handle_exterior_3;
	private final ModelPart handle_3_r1;
	private final ModelPart end_handle_exterior_4;
	private final ModelPart end_side_right;
	private final ModelPart outer_right_roof_5_r1;
	private final ModelPart outer_right_roof_4_r1;
	private final ModelPart outer_right_roof_3_r1;
	private final ModelPart outer_right_roof_2_r1;
	private final ModelPart outer_right_roof_1_r1;
	private final ModelPart side_right_panel_3_r1;
	private final ModelPart right_side_2_r1;
	private final ModelPart end_side_left;
	private final ModelPart outer_left_roof_5_r1;
	private final ModelPart outer_left_roof_4_r1;
	private final ModelPart outer_left_roof_3_r1;
	private final ModelPart outer_left_roof_2_r1;
	private final ModelPart outer_left_roof_1_r1;
	private final ModelPart side_left_panel_3_r1;
	private final ModelPart left_side_2_r2;
	private final ModelPart end_bottom_panel;
	private final ModelPart roof_end;
	private final ModelPart end_mid_roof_4_r1;
	private final ModelPart end_mid_roof_3_r1;
	private final ModelPart end_roof_left;
	private final ModelPart inner_roof_4_r5;
	private final ModelPart inner_roof_2_r5;
	private final ModelPart end_roof_right;
	private final ModelPart inner_roof_4_r6;
	private final ModelPart inner_roof_2_r6;
	private final ModelPart roof_end_exterior;
	private final ModelPart outer_roof_right;
	private final ModelPart outer_left_roof_5_r2;
	private final ModelPart outer_left_roof_4_r2;
	private final ModelPart outer_left_roof_3_r2;
	private final ModelPart outer_left_roof_2_r2;
	private final ModelPart outer_left_roof_1_r2;
	private final ModelPart outer_roof_left;
	private final ModelPart outer_left_roof_5_r3;
	private final ModelPart outer_left_roof_4_r3;
	private final ModelPart outer_left_roof_3_r3;
	private final ModelPart outer_left_roof_2_r3;
	private final ModelPart outer_left_roof_1_r3;
	private final ModelPart roof_door_light;
	private final ModelPart door_light_2_r1;
	private final ModelPart door_light_1_r1;
	private final ModelPart roof_window_light;
	private final ModelPart window_light_2_r1;
	private final ModelPart window_light_1_r1;
	private final ModelPart roof_end_light;
	private final ModelPart end_light_roof_right;
	private final ModelPart inner_light_2_r1;
	private final ModelPart inner_light_1_r1;
	private final ModelPart end_light_roof_left;
	private final ModelPart inner_light_2_r2;
	private final ModelPart inner_light_1_r2;
	private final ModelPart roof_head_light;
	private final ModelPart head_light_roof_right;
	private final ModelPart inner_light_2_r3;
	private final ModelPart inner_light_1_r3;
	private final ModelPart head_light_roof_left;
	private final ModelPart inner_light_2_r4;
	private final ModelPart inner_light_1_r4;
	private final ModelPart roof_handle;
	private final ModelPart roof_handrail_curve;
	private final ModelPart roof_handrail_curve_22_r1;
	private final ModelPart roof_handrail_curve_21_r1;
	private final ModelPart roof_handrail_curve_20_r1;
	private final ModelPart roof_handrail_curve_19_r1;
	private final ModelPart roof_handrail_curve_18_r1;
	private final ModelPart roof_handrail_curve_17_r1;
	private final ModelPart roof_handrail_curve_16_r1;
	private final ModelPart roof_handrail_curve_15_r1;
	private final ModelPart roof_handrail_curve_14_r1;
	private final ModelPart roof_handrail_curve_13_r1;
	private final ModelPart roof_handrail_curve_12_r1;
	private final ModelPart roof_handrail_curve_11_r1;
	private final ModelPart roof_handrail_curve_10_r1;
	private final ModelPart roof_handrail_curve_9_r1;
	private final ModelPart roof_handrail_curve_8_r1;
	private final ModelPart roof_handrail_curve_7_r1;
	private final ModelPart roof_handrail_curve_6_r1;
	private final ModelPart roof_handrail_curve_4_r1;
	private final ModelPart roof_handrail_curve_3_r1;
	private final ModelPart roof_handrail_curve_2_r1;
	private final ModelPart roof_handrail_curve_1_r1;
	private final ModelPart head;
	private final ModelPart front_cab_left_1_r1;
	private final ModelPart front_cab_right_2_r1;
	private final ModelPart cab_door_back;
	private final ModelPart end_wall_4_r1;
	private final ModelPart end_wall_2_r1;
	private final ModelPart head_exterior;
	private final ModelPart bottom;
	private final ModelPart buttom_panel_left_9_r2;
	private final ModelPart buttom_panel_left_8_r2;
	private final ModelPart buttom_panel_left_7_r2;
	private final ModelPart buttom_panel_left_6_r2;
	private final ModelPart buttom_panel_left_5_r2;
	private final ModelPart buttom_panel_left_4_r2;
	private final ModelPart buttom_panel_left_3_r2;
	private final ModelPart buttom_panel_left_2_r2;
	private final ModelPart buttom_panel_right_9_r2;
	private final ModelPart buttom_panel_right_8_r2;
	private final ModelPart buttom_panel_right_7_r2;
	private final ModelPart buttom_panel_right_6_r2;
	private final ModelPart buttom_panel_right_5_r2;
	private final ModelPart buttom_panel_right_4_r2;
	private final ModelPart buttom_panel_right_3_r2;
	private final ModelPart buttom_panel_right_2_r2;
	private final ModelPart front;
	private final ModelPart front_left_panel_4_r1;
	private final ModelPart front_left_panel_3_r2;
	private final ModelPart front_left_panel_2_r2;
	private final ModelPart front_left_panel_1_r2;
	private final ModelPart front_right_panel_4_r1;
	private final ModelPart front_right_panel_3_r2;
	private final ModelPart front_right_panel_2_r2;
	private final ModelPart front_right_panel_1_r2;
	private final ModelPart cab_door;
	private final ModelPart front_handle_exterior_1;
	private final ModelPart handle_1_r2;
	private final ModelPart front_handle_exterior_2;
	private final ModelPart handle_2_r2;
	private final ModelPart front_handle_exterior_3;
	private final ModelPart handle_3_r2;
	private final ModelPart front_handle_exterior_4;
	private final ModelPart front_handle_exterior_5;
	private final ModelPart front_handle_exterior_6;
	private final ModelPart front_handle_exterior_7;
	private final ModelPart side_right;
	private final ModelPart outer_right_roof_5_r2;
	private final ModelPart outer_right_roof_4_r2;
	private final ModelPart outer_right_roof_3_r2;
	private final ModelPart outer_right_roof_2_r2;
	private final ModelPart outer_right_roof_1_r2;
	private final ModelPart side_right_panel_3_r2;
	private final ModelPart right_side_2_r2;
	private final ModelPart side_left;
	private final ModelPart outer_left_roof_5_r4;
	private final ModelPart outer_left_roof_4_r4;
	private final ModelPart outer_left_roof_3_r4;
	private final ModelPart outer_left_roof_2_r4;
	private final ModelPart outer_left_roof_1_r4;
	private final ModelPart side_left_panel_3_r2;
	private final ModelPart left_side_2_r3;
	private final ModelPart bottom_panel;
	private final ModelPart headlights;
	private final ModelPart headlight_2_r1;
	private final ModelPart headlight_1_r1;
	private final ModelPart tail_lights;
	private final ModelPart tail_light_2_r1;
	private final ModelPart tail_light_1_r1;
	private final ModelPart door_light;
	private final ModelPart light_plate_1_r1;
	private final ModelPart door_light_on;
	private final ModelPart light_r7;
	private final ModelPart door_light_off;
	private final ModelPart light_r8;
	private final ModelPart coupler_fence;
	private final ModelPart front_display;
	private final ModelPart letter_display;
	private final ModelPart end_display;
	private final ModelPart system_map;
	private final ModelPart display_map_r1;
public ModelR179Alpha6() {
		textureWidth = 608;
		textureHeight = 608;
		window = new ModelPart(this);
		window.setPivot(0.0F, 24.0F, 0.0F);
		window.setTextureOffset(271, 71).addCuboid(-20.0F, 0.0F, -24.0F, 21.0F, 1.0F, 48.0F, 0.0F, false);

		wall = new ModelPart(this);
		wall.setPivot(0.0F, 0.0F, 0.0F);
		window.addChild(wall);
		wall.setTextureOffset(198, 302).addCuboid(-21.3F, -13.0F, -27.0F, 2.0F, 15.0F, 54.0F, 0.0F, false);
		wall.setTextureOffset(115, 234).addCuboid(-19.8F, -13.0F, 25.0F, 1.0F, 13.0F, 2.0F, 0.0F, false);
		wall.setTextureOffset(100, 234).addCuboid(-19.8F, -13.0F, -27.0F, 1.0F, 13.0F, 2.0F, 0.0F, false);

		wall_10_r1 = new ModelPart(this);
		wall_10_r1.setPivot(-17.3248F, -31.7566F, 0.0F);
		wall.addChild(wall_10_r1);
		setRotationAngle(wall_10_r1, 0.0F, 0.0F, -0.0262F);
		wall_10_r1.setTextureOffset(260, 372).addCuboid(-1.0F, -1.0F, -12.0F, 1.0F, 1.0F, 24.0F, 0.0F, false);

		wall_9_r1 = new ModelPart(this);
		wall_9_r1.setPivot(-18.2748F, -31.7566F, -0.1F);
		wall.addChild(wall_9_r1);
		setRotationAngle(wall_9_r1, 0.0F, 0.0F, -0.0262F);
		wall_9_r1.setTextureOffset(74, 269).addCuboid(-1.0F, -0.9764F, -12.9F, 1.0F, 1.0F, 26.0F, 0.0F, false);

		wall_8_r1 = new ModelPart(this);
		wall_8_r1.setPivot(-18.9748F, -16.0934F, 0.0F);
		wall.addChild(wall_8_r1);
		setRotationAngle(wall_8_r1, 0.0F, 0.0F, 0.8988F);
		wall_8_r1.setTextureOffset(315, 405).addCuboid(-2.0F, 0.0F, -12.0F, 2.0F, 1.0F, 24.0F, 0.0F, false);

		wall_7_r1 = new ModelPart(this);
		wall_7_r1.setPivot(-20.8248F, -17.8934F, -0.1F);
		wall.addChild(wall_7_r1);
		setRotationAngle(wall_7_r1, 0.0F, 0.0F, 0.0698F);
		wall_7_r1.setTextureOffset(61, 385).addCuboid(0.125F, 0.0134F, -12.9F, 0.0F, 1.0F, 26.0F, 0.2F, false);

		wall_6_r1 = new ModelPart(this);
		wall_6_r1.setPivot(-19.9498F, -17.2684F, -0.1F);
		wall.addChild(wall_6_r1);
		setRotationAngle(wall_6_r1, 0.0F, 0.0F, 0.9425F);
		wall_6_r1.setTextureOffset(406, 0).addCuboid(-1.0F, -0.0116F, -11.9F, 1.0F, 0.0F, 24.0F, 0.0F, false);

		wall_5_r1 = new ModelPart(this);
		wall_5_r1.setPivot(-19.1F, -10.875F, -82.1F);
		wall.addChild(wall_5_r1);
		setRotationAngle(wall_5_r1, 0.0F, 0.0F, 0.1047F);
		wall_5_r1.setTextureOffset(218, 72).addCuboid(-0.925F, -23.0F, 55.1F, 1.0F, 21.0F, 2.0F, 0.0F, false);
		wall_5_r1.setTextureOffset(221, 160).addCuboid(-0.925F, -23.0F, 107.1F, 1.0F, 21.0F, 2.0F, 0.0F, false);

		wall_4_r1 = new ModelPart(this);
		wall_4_r1.setPivot(-19.6F, -10.875F, -31.1F);
		wall.addChild(wall_4_r1);
		setRotationAngle(wall_4_r1, 0.0F, 0.0F, 0.1047F);
		wall_4_r1.setTextureOffset(0, 41).addCuboid(-1.925F, -23.0F, 43.1F, 2.0F, 21.0F, 15.0F, 0.0F, false);
		wall_4_r1.setTextureOffset(194, 199).addCuboid(-1.925F, -23.0F, 18.1F, 2.0F, 2.0F, 26.0F, 0.0F, false);
		wall_4_r1.setTextureOffset(0, 0).addCuboid(-1.925F, -23.0F, 4.1F, 2.0F, 21.0F, 15.0F, 0.0F, false);

		wall_2_r1 = new ModelPart(this);
		wall_2_r1.setPivot(-19.775F, -9.125F, -69.1F);
		wall.addChild(wall_2_r1);
		setRotationAngle(wall_2_r1, 0.0F, 0.0F, 0.1047F);
		wall_2_r1.setTextureOffset(234, 231).addCuboid(-1.925F, -7.0F, 56.1F, 2.0F, 5.0F, 26.0F, 0.0F, false);

		Turns = new ModelPart(this);
		Turns.setPivot(0.0F, 0.0F, 0.0F);
		wall.addChild(Turns);
		

		window_curved_1 = new ModelPart(this);
		window_curved_1.setPivot(-20.469F, -18.7084F, -12.075F);
		Turns.addChild(window_curved_1);
		

		window_curved_side_2_r1 = new ModelPart(this);
		window_curved_side_2_r1.setPivot(0.075F, 1.625F, -0.25F);
		window_curved_1.addChild(window_curved_side_2_r1);
		setRotationAngle(window_curved_side_2_r1, -0.5411F, 0.0F, 0.1047F);
		window_curved_side_2_r1.setTextureOffset(145, 90).addCuboid(-0.5F, -0.5F, 0.5F, 2.0F, 2.0F, 2.0F, 0.0F, false);

		window_curved_side_1_r1 = new ModelPart(this);
		window_curved_side_1_r1.setPivot(0.325F, -0.7F, -1.225F);
		window_curved_1.addChild(window_curved_side_1_r1);
		setRotationAngle(window_curved_side_1_r1, -1.021F, 0.0F, 0.1047F);
		window_curved_side_1_r1.setTextureOffset(146, 28).addCuboid(-0.5F, -0.5F, 1.5F, 2.0F, 2.0F, 2.0F, 0.0F, false);

		window_curved_2 = new ModelPart(this);
		window_curved_2.setPivot(-17.856F, -29.2916F, -12.075F);
		Turns.addChild(window_curved_2);
		

		window_curved_side_2_r2 = new ModelPart(this);
		window_curved_side_2_r2.setPivot(-0.075F, -1.625F, -0.25F);
		window_curved_2.addChild(window_curved_side_2_r2);
		setRotationAngle(window_curved_side_2_r2, 0.5411F, 0.0F, 0.1047F);
		window_curved_side_2_r2.setTextureOffset(130, 118).addCuboid(-1.5F, -1.5F, 0.5F, 2.0F, 2.0F, 2.0F, 0.0F, false);

		window_curved_side_1_r2 = new ModelPart(this);
		window_curved_side_1_r2.setPivot(-0.325F, 0.7F, -1.225F);
		window_curved_2.addChild(window_curved_side_1_r2);
		setRotationAngle(window_curved_side_1_r2, 1.021F, 0.0F, 0.1047F);
		window_curved_side_1_r2.setTextureOffset(142, 8).addCuboid(-1.5F, -1.5F, 1.5F, 2.0F, 2.0F, 2.0F, 0.0F, false);

		window_curved_3 = new ModelPart(this);
		window_curved_3.setPivot(-17.856F, -29.2916F, -12.075F);
		Turns.addChild(window_curved_3);
		

		window_curved_side_2_r3 = new ModelPart(this);
		window_curved_side_2_r3.setPivot(-0.075F, -1.625F, 24.4F);
		window_curved_3.addChild(window_curved_side_2_r3);
		setRotationAngle(window_curved_side_2_r3, -0.5411F, 0.0F, 0.1047F);
		window_curved_side_2_r3.setTextureOffset(77, 41).addCuboid(-1.5F, -1.5F, -2.5F, 2.0F, 2.0F, 2.0F, 0.0F, false);

		window_curved_side_1_r3 = new ModelPart(this);
		window_curved_side_1_r3.setPivot(-0.325F, 0.7F, 25.375F);
		window_curved_3.addChild(window_curved_side_1_r3);
		setRotationAngle(window_curved_side_1_r3, -1.021F, 0.0F, 0.1047F);
		window_curved_side_1_r3.setTextureOffset(53, 118).addCuboid(-1.5F, -1.5F, -3.5F, 2.0F, 2.0F, 2.0F, 0.0F, false);

		window_curved_4 = new ModelPart(this);
		window_curved_4.setPivot(-2.6129F, 10.5831F, 0.0F);
		window_curved_3.addChild(window_curved_4);
		

		window_curved_side_2_r4 = new ModelPart(this);
		window_curved_side_2_r4.setPivot(0.075F, 1.625F, 24.4F);
		window_curved_4.addChild(window_curved_side_2_r4);
		setRotationAngle(window_curved_side_2_r4, 0.5411F, 0.0F, 0.1047F);
		window_curved_side_2_r4.setTextureOffset(20, 51).addCuboid(-0.5F, -0.5F, -2.5F, 2.0F, 2.0F, 2.0F, 0.0F, false);

		window_curved_side_1_r4 = new ModelPart(this);
		window_curved_side_1_r4.setPivot(0.325F, -0.7F, 25.375F);
		window_curved_4.addChild(window_curved_side_1_r4);
		setRotationAngle(window_curved_side_1_r4, 1.021F, 0.0F, 0.1047F);
		window_curved_side_1_r4.setTextureOffset(76, 34).addCuboid(-0.5F, -0.5F, -3.5F, 2.0F, 2.0F, 2.0F, 0.0F, false);

		seat = new ModelPart(this);
		seat.setPivot(0.0F, 0.0F, 0.0F);
		window.addChild(seat);
		seat.setTextureOffset(148, 372).addCuboid(-19.9F, -10.75F, -25.0F, 2.0F, 5.0F, 50.0F, 0.0F, false);
		seat.setTextureOffset(362, 51).addCuboid(-19.55F, -5.0F, -24.9F, 3.0F, 3.0F, 50.0F, 0.0F, false);

		seat_bottom_r1 = new ModelPart(this);
		seat_bottom_r1.setPivot(0.1F, -1.75F, 0.0F);
		seat.addChild(seat_bottom_r1);
		setRotationAngle(seat_bottom_r1, 0.0F, 0.0F, -0.0873F);
		seat_bottom_r1.setTextureOffset(257, 302).addCuboid(-20.0F, -6.0F, -25.0F, 9.0F, 1.0F, 50.0F, 0.0F, false);

		seat_back_3_r1 = new ModelPart(this);
		seat_back_3_r1.setPivot(-16.725F, -9.75F, 0.0F);
		seat.addChild(seat_back_3_r1);
		setRotationAngle(seat_back_3_r1, 0.0F, 0.0F, -0.1309F);
		seat_back_3_r1.setTextureOffset(377, 172).addCuboid(-3.0F, -6.0F, -25.0F, 2.0F, 4.0F, 50.0F, 0.0F, false);

		seat_back_2_r1 = new ModelPart(this);
		seat_back_2_r1.setPivot(-16.725F, -8.75F, 0.0F);
		seat.addChild(seat_back_2_r1);
		setRotationAngle(seat_back_2_r1, 0.0F, 0.0F, -0.0873F);
		seat_back_2_r1.setTextureOffset(395, 289).addCuboid(-3.0F, -3.0F, -25.0F, 2.0F, 1.0F, 50.0F, 0.0F, false);

		handrail_window = new ModelPart(this);
		handrail_window.setPivot(0.0F, 0.0F, 0.0F);
		window.addChild(handrail_window);
		handrail_window.setTextureOffset(116, 479).addCuboid(-12.8F, -31.75F, -23.0F, 0.0F, 0.0F, 46.0F, 0.2F, false);

		handrail_mid = new ModelPart(this);
		handrail_mid.setPivot(-39.95F, -9.875F, 38.1F);
		handrail_window.addChild(handrail_mid);
		

		handrail_middle_4_r1 = new ModelPart(this);
		handrail_middle_4_r1.setPivot(21.875F, -21.65F, -38.1F);
		handrail_mid.addChild(handrail_middle_4_r1);
		setRotationAngle(handrail_middle_4_r1, 0.0F, 0.0F, 1.1606F);
		handrail_middle_4_r1.setTextureOffset(20, 14).addCuboid(2.0F, -5.0F, 0.0F, 3.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_middle_3_r1 = new ModelPart(this);
		handrail_middle_3_r1.setPivot(23.6F, -17.95F, -38.1F);
		handrail_mid.addChild(handrail_middle_3_r1);
		setRotationAngle(handrail_middle_3_r1, 0.0F, 0.0F, 1.3788F);
		handrail_middle_3_r1.setTextureOffset(20, 13).addCuboid(0.0F, -5.0F, 0.0F, 5.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_middle_2_r1 = new ModelPart(this);
		handrail_middle_2_r1.setPivot(24.5F, -0.725F, -38.1F);
		handrail_mid.addChild(handrail_middle_2_r1);
		setRotationAngle(handrail_middle_2_r1, 0.0F, 0.0F, 1.5708F);
		handrail_middle_2_r1.setTextureOffset(45, 33).addCuboid(-13.0F, -5.0F, 0.0F, 18.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_middle_1_r1 = new ModelPart(this);
		handrail_middle_1_r1.setPivot(25.2F, -0.725F, -38.1F);
		handrail_mid.addChild(handrail_middle_1_r1);
		setRotationAngle(handrail_middle_1_r1, 0.0F, 0.0F, 1.5708F);
		handrail_middle_1_r1.setTextureOffset(35, 29).addCuboid(6.0F, -3.0F, 0.0F, 0.0F, 7.0F, 0.0F, 0.2F, false);

		handrail_turn_5_r1 = new ModelPart(this);
		handrail_turn_5_r1.setPivot(29.35F, 5.0679F, -38.1238F);
		handrail_mid.addChild(handrail_turn_5_r1);
		setRotationAngle(handrail_turn_5_r1, 0.0F, -1.5708F, -0.1833F);
		handrail_turn_5_r1.setTextureOffset(38, 33).addCuboid(0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_turn_4_r1 = new ModelPart(this);
		handrail_turn_4_r1.setPivot(29.55F, 4.7429F, -38.1238F);
		handrail_mid.addChild(handrail_turn_4_r1);
		setRotationAngle(handrail_turn_4_r1, 0.0F, -1.5708F, -0.4451F);
		handrail_turn_4_r1.setTextureOffset(11, 9).addCuboid(0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_turn_3_r1 = new ModelPart(this);
		handrail_turn_3_r1.setPivot(29.85F, 4.5429F, -38.1238F);
		handrail_mid.addChild(handrail_turn_3_r1);
		setRotationAngle(handrail_turn_3_r1, 0.0F, -1.5708F, -0.4887F);
		handrail_turn_3_r1.setTextureOffset(4, 0).addCuboid(0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_turn_2_r1 = new ModelPart(this);
		handrail_turn_2_r1.setPivot(29.95F, 4.1429F, -38.1238F);
		handrail_mid.addChild(handrail_turn_2_r1);
		setRotationAngle(handrail_turn_2_r1, 0.0F, -1.5708F, -0.7505F);
		handrail_turn_2_r1.setTextureOffset(0, 0).addCuboid(0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_turn_1_r1 = new ModelPart(this);
		handrail_turn_1_r1.setPivot(29.85F, 3.6429F, -38.1238F);
		handrail_mid.addChild(handrail_turn_1_r1);
		setRotationAngle(handrail_turn_1_r1, 0.0F, -1.5708F, -1.1345F);
		handrail_turn_1_r1.setTextureOffset(38, 35).addCuboid(0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.2F, false);

		headrail_right = new ModelPart(this);
		headrail_right.setPivot(-39.95F, -9.875F, 37.1F);
		handrail_window.addChild(headrail_right);
		

		handrail_left_3_r1 = new ModelPart(this);
		handrail_left_3_r1.setPivot(23.3F, -0.225F, -13.7F);
		headrail_right.addChild(handrail_left_3_r1);
		setRotationAngle(handrail_left_3_r1, 0.0F, 0.0F, 1.5708F);
		handrail_left_3_r1.setTextureOffset(47, 42).addCuboid(4.0F, -5.0F, 1.0F, 0.0F, 0.0F, 1.0F, 0.2F, false);
		handrail_left_3_r1.setTextureOffset(0, 38).addCuboid(-15.0F, -5.0F, 2.0F, 19.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_right_1_r1 = new ModelPart(this);
		handrail_right_1_r1.setPivot(28.06F, -17.5269F, -11.7F);
		headrail_right.addChild(handrail_right_1_r1);
		setRotationAngle(handrail_right_1_r1, 0.0F, 0.0F, 1.4573F);
		handrail_right_1_r1.setTextureOffset(27, 14).addCuboid(-1.0F, 0.0F, 0.0F, 3.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_turn_4_r2 = new ModelPart(this);
		handrail_turn_4_r2.setPivot(27.9F, -18.7556F, -11.737F);
		headrail_right.addChild(handrail_turn_4_r2);
		setRotationAngle(handrail_turn_4_r2, -1.3099F, 0.0441F, -0.1412F);
		handrail_turn_4_r2.setTextureOffset(14, 2).addCuboid(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_turn_3_r2 = new ModelPart(this);
		handrail_turn_3_r2.setPivot(27.65F, -19.8179F, -12.2262F);
		headrail_right.addChild(handrail_turn_3_r2);
		setRotationAngle(handrail_turn_3_r2, -1.1436F, 0.0916F, -0.1983F);
		handrail_turn_3_r2.setTextureOffset(33, 3).addCuboid(0.0F, 0.0F, -1.0F, 0.0F, 0.0F, 2.0F, 0.2F, false);

		handrail_turn_2_r2 = new ModelPart(this);
		handrail_turn_2_r2.setPivot(27.375F, -20.9556F, -12.812F);
		headrail_right.addChild(handrail_turn_2_r2);
		setRotationAngle(handrail_turn_2_r2, -0.7494F, 0.1284F, -0.1186F);
		handrail_turn_2_r2.setTextureOffset(14, 5).addCuboid(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_turn_1_r2 = new ModelPart(this);
		handrail_turn_1_r2.setPivot(27.25F, -21.4912F, -13.4782F);
		headrail_right.addChild(handrail_turn_1_r2);
		setRotationAngle(handrail_turn_1_r2, -0.6584F, 0.1103F, -0.0706F);
		handrail_turn_1_r2.setTextureOffset(36, 3).addCuboid(0.0F, 0.0F, -0.5F, 0.0F, 0.0F, 1.0F, 0.2F, false);

		headrail_left = new ModelPart(this);
		headrail_left.setPivot(-39.95F, -9.875F, 39.1F);
		handrail_window.addChild(headrail_left);
		

		handrail_right_3_r1 = new ModelPart(this);
		handrail_right_3_r1.setPivot(23.3F, -0.225F, -62.5F);
		headrail_left.addChild(handrail_right_3_r1);
		setRotationAngle(handrail_right_3_r1, 0.0F, 0.0F, 1.5708F);
		handrail_right_3_r1.setTextureOffset(47, 28).addCuboid(4.0F, -5.0F, -2.0F, 0.0F, 0.0F, 1.0F, 0.2F, false);
		handrail_right_3_r1.setTextureOffset(0, 37).addCuboid(-15.0F, -5.0F, -2.0F, 19.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_right_1_r2 = new ModelPart(this);
		handrail_right_1_r2.setPivot(28.06F, -17.5269F, -64.5F);
		headrail_left.addChild(handrail_right_1_r2);
		setRotationAngle(handrail_right_1_r2, 0.0F, 0.0F, 1.4573F);
		handrail_right_1_r2.setTextureOffset(8, 45).addCuboid(-1.0F, 0.0F, 0.0F, 3.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_turn_4_r3 = new ModelPart(this);
		handrail_turn_4_r3.setPivot(27.9F, -18.7556F, -64.463F);
		headrail_left.addChild(handrail_turn_4_r3);
		setRotationAngle(handrail_turn_4_r3, 1.3099F, -0.0441F, -0.1412F);
		handrail_turn_4_r3.setTextureOffset(31, 13).addCuboid(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_turn_3_r3 = new ModelPart(this);
		handrail_turn_3_r3.setPivot(27.65F, -19.8179F, -63.9738F);
		headrail_left.addChild(handrail_turn_3_r3);
		setRotationAngle(handrail_turn_3_r3, 1.1436F, -0.0916F, -0.1983F);
		handrail_turn_3_r3.setTextureOffset(20, 44).addCuboid(0.0F, 0.0F, -1.0F, 0.0F, 0.0F, 2.0F, 0.2F, false);

		handrail_turn_2_r3 = new ModelPart(this);
		handrail_turn_2_r3.setPivot(27.375F, -20.9556F, -63.388F);
		headrail_left.addChild(handrail_turn_2_r3);
		setRotationAngle(handrail_turn_2_r3, 0.7494F, -0.1284F, -0.1186F);
		handrail_turn_2_r3.setTextureOffset(37, 39).addCuboid(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_turn_1_r3 = new ModelPart(this);
		handrail_turn_1_r3.setPivot(27.25F, -21.4912F, -62.7218F);
		headrail_left.addChild(handrail_turn_1_r3);
		setRotationAngle(handrail_turn_1_r3, 0.6584F, -0.1103F, -0.0706F);
		handrail_turn_1_r3.setTextureOffset(47, 3).addCuboid(0.0F, 0.0F, -0.5F, 0.0F, 0.0F, 1.0F, 0.2F, false);

		headrail_up = new ModelPart(this);
		headrail_up.setPivot(-40.95F, -12.875F, 38.1F);
		handrail_window.addChild(headrail_up);
		headrail_up.setTextureOffset(34, 13).addCuboid(30.35F, -22.375F, -15.8F, 0.0F, 1.0F, 0.0F, 0.2F, false);
		headrail_up.setTextureOffset(34, 3).addCuboid(30.35F, -22.375F, -60.4F, 0.0F, 1.0F, 0.0F, 0.2F, false);
		headrail_up.setTextureOffset(33, 3).addCuboid(30.35F, -22.375F, -32.4F, 0.0F, 1.0F, 0.0F, 0.2F, false);
		headrail_up.setTextureOffset(21, 10).addCuboid(30.35F, -22.375F, -43.8F, 0.0F, 1.0F, 0.0F, 0.2F, false);

		handrail_up_11_r1 = new ModelPart(this);
		handrail_up_11_r1.setPivot(37.5F, -8.775F, -46.8F);
		headrail_up.addChild(handrail_up_11_r1);
		setRotationAngle(handrail_up_11_r1, 0.0F, 0.0F, 0.6109F);
		handrail_up_11_r1.setTextureOffset(20, 10).addCuboid(-13.0F, -6.0F, 3.0F, 0.0F, 1.0F, 0.0F, 0.2F, false);
		handrail_up_11_r1.setTextureOffset(33, 0).addCuboid(-13.0F, -6.0F, 14.4F, 0.0F, 1.0F, 0.0F, 0.2F, false);
		handrail_up_11_r1.setTextureOffset(34, 0).addCuboid(-13.0F, -6.0F, -13.6F, 0.0F, 1.0F, 0.0F, 0.2F, false);
		handrail_up_11_r1.setTextureOffset(37, 10).addCuboid(-13.0F, -6.0F, 31.0F, 0.0F, 1.0F, 0.0F, 0.2F, false);

		handrail_up_10_r1 = new ModelPart(this);
		handrail_up_10_r1.setPivot(34.65F, -6.875F, -46.8F);
		headrail_up.addChild(handrail_up_10_r1);
		setRotationAngle(handrail_up_10_r1, 0.0F, 0.0F, 0.7854F);
		handrail_up_10_r1.setTextureOffset(39, 45).addCuboid(-13.0F, -6.0F, 3.0F, 0.0F, 2.0F, 0.0F, 0.2F, false);
		handrail_up_10_r1.setTextureOffset(39, 48).addCuboid(-13.0F, -6.0F, 14.4F, 0.0F, 2.0F, 0.0F, 0.2F, false);
		handrail_up_10_r1.setTextureOffset(48, 44).addCuboid(-13.0F, -6.0F, -13.6F, 0.0F, 2.0F, 0.0F, 0.2F, false);
		handrail_up_10_r1.setTextureOffset(49, 44).addCuboid(-13.0F, -6.0F, 31.0F, 0.0F, 2.0F, 0.0F, 0.2F, false);

		window_display = new ModelPart(this);
		window_display.setPivot(0.0F, 24.0F, 0.0F);
		

		display_panel_r1 = new ModelPart(this);
		display_panel_r1.setPivot(-19.2406F, -24.081F, 0.025F);
		window_display.addChild(display_panel_r1);
		setRotationAngle(display_panel_r1, 0.0F, 0.0F, 0.1047F);
		display_panel_r1.setTextureOffset(270, 171).addCuboid(-1.0F, -8.0F, -13.5F, 0.0F, 4.0F, 27.0F, 0.0F, false);

		display_window_r1 = new ModelPart(this);
		display_window_r1.setPivot(-19.0656F, -25.081F, 0.025F);
		window_display.addChild(display_window_r1);
		setRotationAngle(display_window_r1, 0.0F, 0.0F, 0.1047F);
		display_window_r1.setTextureOffset(195, 107).addCuboid(-1.0F, -7.0F, -13.5F, 2.0F, 4.0F, 27.0F, 0.0F, false);

		window_exterior = new ModelPart(this);
		window_exterior.setPivot(0.0F, 24.0F, 0.0F);
		window_exterior.setTextureOffset(82, 164).addCuboid(-21.5F, 0.0F, -24.0F, 0.0F, 4.0F, 48.0F, 0.0F, false);
		window_exterior.setTextureOffset(211, 0).addCuboid(-21.5F, -13.0F, -27.0F, 0.0F, 13.0F, 54.0F, 0.0F, false);

		upper_wall_r1 = new ModelPart(this);
		upper_wall_r1.setPivot(-20.95F, -9.875F, 24.1F);
		window_exterior.addChild(upper_wall_r1);
		setRotationAngle(upper_wall_r1, 0.0F, 0.0F, 0.1047F);
		upper_wall_r1.setTextureOffset(89, 300).addCuboid(-0.85F, -26.0F, -51.1F, 0.0F, 23.0F, 54.0F, 0.0F, false);

		window_exterior_side = new ModelPart(this);
		window_exterior_side.setPivot(0.0F, 0.0F, 0.0F);
		window_exterior.addChild(window_exterior_side);
		window_exterior_side.setTextureOffset(59, 219).addCuboid(-21.5F, -13.0F, -27.025F, 2.0F, 13.0F, 0.0F, 0.0F, false);
		window_exterior_side.setTextureOffset(132, 0).addCuboid(-21.5F, -13.0F, 27.025F, 2.0F, 13.0F, 0.0F, 0.0F, false);

		left_side_2_r1 = new ModelPart(this);
		left_side_2_r1.setPivot(-20.95F, -9.875F, -25.075F);
		window_exterior_side.addChild(left_side_2_r1);
		setRotationAngle(left_side_2_r1, 0.0F, 0.0F, 0.1047F);
		left_side_2_r1.setTextureOffset(210, 231).addCuboid(-0.85F, -24.0F, 52.1F, 2.0F, 21.0F, 0.0F, 0.0F, false);
		left_side_2_r1.setTextureOffset(95, 234).addCuboid(-0.85F, -24.0F, -1.95F, 2.0F, 21.0F, 0.0F, 0.0F, false);

		under_seatbox = new ModelPart(this);
		under_seatbox.setPivot(0.0F, 24.0F, 0.0F);
		under_seatbox.setTextureOffset(41, 147).addCuboid(-19.3F, -5.075F, 20.2F, 6.0F, 6.0F, 6.0F, 0.0F, false);
		under_seatbox.setTextureOffset(66, 41).addCuboid(-14.15F, -3.5F, 20.2F, 2.0F, 4.0F, 6.0F, 0.0F, false);

		seatbox_2_r1 = new ModelPart(this);
		seatbox_2_r1.setPivot(-12.25F, -2.775F, 23.2F);
		under_seatbox.addChild(seatbox_2_r1);
		setRotationAngle(seatbox_2_r1, 0.0F, 0.0F, -0.6109F);
		seatbox_2_r1.setTextureOffset(0, 0).addCuboid(-0.5F, -2.5F, -3.0F, 1.0F, 2.0F, 6.0F, 0.0F, false);

		side_panel = new ModelPart(this);
		side_panel.setPivot(0.0F, 24.0F, 0.0F);
		side_panel.setTextureOffset(34, 164).addCuboid(-20.0F, -32.0F, 0.0F, 9.0F, 29.0F, 0.0F, 0.0F, false);

		roof_window = new ModelPart(this);
		roof_window.setPivot(0.0F, 24.0F, 0.0F);
		roof_window.setTextureOffset(446, 341).addCuboid(-18.05F, -32.95F, -24.0F, 2.0F, 0.0F, 48.0F, 0.0F, false);
		roof_window.setTextureOffset(368, 457).addCuboid(-11.0F, -34.8F, -24.0F, 1.0F, 0.0F, 48.0F, 0.0F, false);
		roof_window.setTextureOffset(324, 355).addCuboid(-8.8F, -37.0F, -24.0F, 9.0F, 0.0F, 48.0F, 0.0F, false);

		inner_roof_4_r1 = new ModelPart(this);
		inner_roof_4_r1.setPivot(-11.6F, -33.1F, -25.0F);
		roof_window.addChild(inner_roof_4_r1);
		setRotationAngle(inner_roof_4_r1, 0.0F, 0.0F, -0.829F);
		inner_roof_4_r1.setTextureOffset(419, 52).addCuboid(2.3F, 0.0F, 1.0F, 3.0F, 0.0F, 48.0F, 0.0F, false);

		inner_roof_2_r1 = new ModelPart(this);
		inner_roof_2_r1.setPivot(-14.9F, -34.45F, 15.0F);
		roof_window.addChild(inner_roof_2_r1);
		setRotationAngle(inner_roof_2_r1, 0.0F, 0.0F, -0.9163F);
		inner_roof_2_r1.setTextureOffset(0, 385).addCuboid(-1.9F, 0.0F, -39.0F, 6.0F, 0.0F, 48.0F, 0.0F, false);

		roof_door = new ModelPart(this);
		roof_door.setPivot(0.0F, 24.0F, 0.0F);
		roof_door.setTextureOffset(266, 0).addCuboid(-18.05F, -32.95F, -17.0F, 2.0F, 0.0F, 34.0F, 0.0F, false);
		roof_door.setTextureOffset(122, 378).addCuboid(-11.0F, -34.8F, -17.075F, 1.0F, 0.0F, 34.0F, 0.0F, false);
		roof_door.setTextureOffset(515, 499).addCuboid(-8.8F, -37.0F, -17.075F, 9.0F, 0.0F, 34.0F, 0.0F, false);

		inner_roof_4_r2 = new ModelPart(this);
		inner_roof_4_r2.setPivot(-11.6F, -33.1F, -25.075F);
		roof_door.addChild(inner_roof_4_r2);
		setRotationAngle(inner_roof_4_r2, 0.0F, 0.0F, -0.829F);
		inner_roof_4_r2.setTextureOffset(74, 234).addCuboid(2.3F, 0.0F, 8.0F, 3.0F, 0.0F, 34.0F, 0.0F, false);

		inner_roof_2_r2 = new ModelPart(this);
		inner_roof_2_r2.setPivot(-14.9F, -34.45F, 32.0F);
		roof_door.addChild(inner_roof_2_r2);
		setRotationAngle(inner_roof_2_r2, 0.0F, 0.0F, -0.9163F);
		inner_roof_2_r2.setTextureOffset(342, 172).addCuboid(-1.9F, 0.0F, -49.0F, 6.0F, 0.0F, 34.0F, 0.0F, false);

		roof_exterior = new ModelPart(this);
		roof_exterior.setPivot(0.0F, 24.0F, 0.0F);
		roof_exterior.setTextureOffset(144, 301).addCuboid(-3.0F, -41.375F, -25.0F, 3.0F, 0.0F, 50.0F, 0.0F, false);

		outer_roof_5_r1 = new ModelPart(this);
		outer_roof_5_r1.setPivot(-8.978F, -39.8555F, -5.5F);
		roof_exterior.addChild(outer_roof_5_r1);
		setRotationAngle(outer_roof_5_r1, 0.0F, 0.0F, -0.0698F);
		outer_roof_5_r1.setTextureOffset(0, 334).addCuboid(-0.7355F, -1.1097F, -19.5F, 7.0F, 0.0F, 50.0F, 0.0F, false);

		outer_roof_4_r1 = new ModelPart(this);
		outer_roof_4_r1.setPivot(-9.578F, -39.7555F, -5.5F);
		roof_exterior.addChild(outer_roof_4_r1);
		setRotationAngle(outer_roof_4_r1, 0.0F, 0.0F, -0.288F);
		outer_roof_4_r1.setTextureOffset(342, 121).addCuboid(-5.503F, -1.1228F, -19.5F, 6.0F, 0.0F, 50.0F, 0.0F, false);

		outer_roof_3_r1 = new ModelPart(this);
		outer_roof_3_r1.setPivot(-9.578F, -39.7555F, -5.5F);
		roof_exterior.addChild(outer_roof_3_r1);
		setRotationAngle(outer_roof_3_r1, 0.0F, 0.0F, -0.4014F);
		outer_roof_3_r1.setTextureOffset(65, 378).addCuboid(-8.3154F, -1.7351F, -19.5F, 3.0F, 0.0F, 50.0F, 0.0F, false);

		outer_roof_2_r1 = new ModelPart(this);
		outer_roof_2_r1.setPivot(-9.578F, -39.7555F, -5.5F);
		roof_exterior.addChild(outer_roof_2_r1);
		setRotationAngle(outer_roof_2_r1, 0.0F, 0.0F, -0.9599F);
		outer_roof_2_r1.setTextureOffset(324, 404).addCuboid(-8.1006F, -5.8625F, -19.5F, 2.0F, 0.0F, 50.0F, 0.0F, false);

		outer_roof_1_r1 = new ModelPart(this);
		outer_roof_1_r1.setPivot(-9.578F, -39.7555F, -20.5F);
		roof_exterior.addChild(outer_roof_1_r1);
		setRotationAngle(outer_roof_1_r1, 0.0F, 0.0F, -1.3788F);
		outer_roof_1_r1.setTextureOffset(422, 227).addCuboid(-5.9967F, -8.6386F, -4.5F, 1.0F, 0.0F, 50.0F, 0.0F, false);

		roof_head = new ModelPart(this);
		roof_head.setPivot(0.0F, 24.0F, 0.0F);
		

		head_roof_right = new ModelPart(this);
		head_roof_right.setPivot(16.95F, -12.875F, -6.1F);
		roof_head.addChild(head_roof_right);
		head_roof_right.setTextureOffset(0, 0).addCuboid(-0.9F, -20.075F, 8.1F, 2.0F, 0.0F, 40.0F, 0.0F, false);
		head_roof_right.setTextureOffset(503, 254).addCuboid(-6.95F, -21.925F, 8.1F, 1.0F, 0.0F, 40.0F, 0.0F, false);
		head_roof_right.setTextureOffset(63, 480).addCuboid(-17.15F, -24.125F, 8.1F, 9.0F, 0.0F, 40.0F, 0.0F, false);

		inner_roof_4_r3 = new ModelPart(this);
		inner_roof_4_r3.setPivot(-5.35F, -20.225F, 38.1F);
		head_roof_right.addChild(inner_roof_4_r3);
		setRotationAngle(inner_roof_4_r3, 0.0F, 0.0F, 0.829F);
		inner_roof_4_r3.setTextureOffset(487, 390).addCuboid(-5.3F, 0.0F, -30.0F, 3.0F, 0.0F, 40.0F, 0.0F, false);

		inner_roof_2_r3 = new ModelPart(this);
		inner_roof_2_r3.setPivot(-2.05F, -21.575F, 38.1F);
		head_roof_right.addChild(inner_roof_2_r3);
		setRotationAngle(inner_roof_2_r3, 0.0F, 0.0F, 0.9163F);
		inner_roof_2_r3.setTextureOffset(0, 485).addCuboid(-4.1F, 0.0F, -30.0F, 6.0F, 0.0F, 40.0F, 0.0F, false);

		head_roof_left = new ModelPart(this);
		head_roof_left.setPivot(-16.95F, -12.875F, -6.1F);
		roof_head.addChild(head_roof_left);
		head_roof_left.setTextureOffset(427, 494).addCuboid(-1.1F, -20.075F, 8.1F, 2.0F, 0.0F, 40.0F, 0.0F, false);
		head_roof_left.setTextureOffset(289, 504).addCuboid(5.95F, -21.925F, 8.1F, 1.0F, 0.0F, 40.0F, 0.0F, false);
		head_roof_left.setTextureOffset(163, 480).addCuboid(8.15F, -24.125F, 8.1F, 9.0F, 0.0F, 40.0F, 0.0F, false);

		inner_roof_4_r4 = new ModelPart(this);
		inner_roof_4_r4.setPivot(5.35F, -20.225F, 38.1F);
		head_roof_left.addChild(inner_roof_4_r4);
		setRotationAngle(inner_roof_4_r4, 0.0F, 0.0F, -0.829F);
		inner_roof_4_r4.setTextureOffset(488, 458).addCuboid(2.3F, 0.0F, -30.0F, 3.0F, 0.0F, 40.0F, 0.0F, false);

		inner_roof_2_r4 = new ModelPart(this);
		inner_roof_2_r4.setPivot(2.05F, -21.575F, 38.1F);
		head_roof_left.addChild(inner_roof_2_r4);
		setRotationAngle(inner_roof_2_r4, 0.0F, 0.0F, -0.9163F);
		inner_roof_2_r4.setTextureOffset(485, 141).addCuboid(-1.9F, 0.0F, -30.0F, 6.0F, 0.0F, 40.0F, 0.0F, false);

		door = new ModelPart(this);
		door.setPivot(0.0F, 24.0F, 0.0F);
		door.setTextureOffset(419, 458).addCuboid(-19.975F, 0.0F, -17.0F, 20.0F, 1.0F, 34.0F, 0.0F, false);

		door_right = new ModelPart(this);
		door_right.setPivot(0.0F, 0.0F, 0.0F);
		door.addChild(door_right);
		door_right.setTextureOffset(0, 90).addCuboid(-19.925F, -13.0F, 0.0F, 0.0F, 13.0F, 14.0F, 0.0F, false);

		door_right_top_r1 = new ModelPart(this);
		door_right_top_r1.setPivot(0.0F, 0.0F, 0.0F);
		door_right.addChild(door_right_top_r1);
		setRotationAngle(door_right_top_r1, 0.0F, 0.0F, 0.1047F);
		door_right_top_r1.setTextureOffset(324, 355).addCuboid(-21.1835F, -31.5961F, 0.0F, 0.0F, 21.0F, 14.0F, 0.0F, false);

		door_left = new ModelPart(this);
		door_left.setPivot(0.0F, 0.0F, 0.0F);
		door.addChild(door_left);
		door_left.setTextureOffset(34, 90).addCuboid(-19.925F, -13.0F, -14.0F, 0.0F, 13.0F, 14.0F, 0.0F, false);

		door_side_top_r1 = new ModelPart(this);
		door_side_top_r1.setPivot(0.0F, 0.0F, 0.0F);
		door_left.addChild(door_side_top_r1);
		setRotationAngle(door_side_top_r1, 0.0F, 0.0F, 0.1047F);
		door_side_top_r1.setTextureOffset(203, 372).addCuboid(-21.1705F, -31.5968F, -14.0F, 0.0F, 21.0F, 14.0F, 0.0F, false);

		door_exterior = new ModelPart(this);
		door_exterior.setPivot(0.0F, 24.0F, 0.0F);
		

		door_right_exterior = new ModelPart(this);
		door_right_exterior.setPivot(0.0F, 0.0F, 0.0F);
		door_exterior.addChild(door_right_exterior);
		door_right_exterior.setTextureOffset(127, 0).addCuboid(-20.2F, -13.0F, 0.0F, 0.0F, 13.0F, 14.0F, 0.0F, false);

		door_right_top_r2 = new ModelPart(this);
		door_right_top_r2.setPivot(0.0011F, -0.002F, 0.0F);
		door_right_exterior.addChild(door_right_top_r2);
		setRotationAngle(door_right_top_r2, 0.0F, 0.0F, 0.1047F);
		door_right_top_r2.setTextureOffset(395, 289).addCuboid(-21.4702F, -31.5712F, 0.0F, 0.0F, 21.0F, 14.0F, 0.0F, false);

		door_left_exterior = new ModelPart(this);
		door_left_exterior.setPivot(0.0F, 0.0F, 0.0F);
		door_exterior.addChild(door_left_exterior);
		door_left_exterior.setTextureOffset(111, 90).addCuboid(-20.2F, -13.0F, -14.0F, 0.0F, 13.0F, 14.0F, 0.0F, false);

		door_left_top_r1 = new ModelPart(this);
		door_left_top_r1.setPivot(-0.0005F, 0.0044F, 0.0F);
		door_left_exterior.addChild(door_left_top_r1);
		setRotationAngle(door_left_top_r1, 0.0F, 0.0F, 0.1047F);
		door_left_top_r1.setTextureOffset(391, 355).addCuboid(-21.4675F, -31.5463F, -14.0F, 0.0F, 21.0F, 14.0F, 0.0F, false);

		door_sides = new ModelPart(this);
		door_sides.setPivot(0.0F, 0.0F, 0.0F);
		door_exterior.addChild(door_sides);
		door_sides.setTextureOffset(194, 160).addCuboid(-21.5F, 0.0F, -17.0F, 0.0F, 4.0F, 34.0F, 0.0F, false);

		door_side_top_2_r1 = new ModelPart(this);
		door_side_top_2_r1.setPivot(-20.5F, 20.8F, 16.0F);
		door_sides.addChild(door_side_top_2_r1);
		setRotationAngle(door_side_top_2_r1, 0.0F, 0.0F, -1.5708F);
		door_side_top_2_r1.setTextureOffset(270, 140).addCuboid(53.75F, 1.0F, -30.0F, 0.0F, 2.0F, 28.0F, 0.0F, false);

		door_side_top_1_r1 = new ModelPart(this);
		door_side_top_1_r1.setPivot(-21.575F, 22.8F, 16.0F);
		door_sides.addChild(door_side_top_1_r1);
		setRotationAngle(door_side_top_1_r1, 0.0F, 0.0F, 0.1047F);
		door_side_top_1_r1.setTextureOffset(234, 263).addCuboid(-3.65F, -58.675F, -30.0F, 0.0F, 3.0F, 28.0F, 0.0F, false);

		door_side_bottom_2_r1 = new ModelPart(this);
		door_side_bottom_2_r1.setPivot(-19.9F, 24.0F, 16.0F);
		door_sides.addChild(door_side_bottom_2_r1);
		setRotationAngle(door_side_bottom_2_r1, 0.0F, 0.0F, -1.5708F);
		door_side_bottom_2_r1.setTextureOffset(158, 265).addCuboid(24.0F, -3.0F, -30.0F, 0.0F, 3.0F, 28.0F, 0.0F, false);

		handrail_door_type_1 = new ModelPart(this);
		handrail_door_type_1.setPivot(0.0F, 24.0F, 0.0F);
		handrail_door_type_1.setTextureOffset(39, 0).addCuboid(0.0F, -37.0F, 0.0F, 0.0F, 37.0F, 0.0F, 0.2F, false);

		handrail_door_type_2 = new ModelPart(this);
		handrail_door_type_2.setPivot(0.0F, 24.0F, 0.0F);
		handrail_door_type_2.setTextureOffset(35, 13).addCuboid(0.0F, -14.25F, 0.0F, 0.0F, 15.0F, 0.0F, 0.2F, false);
		handrail_door_type_2.setTextureOffset(64, 0).addCuboid(0.0F, -37.5F, 0.0F, 0.0F, 6.0F, 0.0F, 0.2F, false);

		handrail_curve = new ModelPart(this);
		handrail_curve.setPivot(0.175F, 3.25F, 15.1F);
		handrail_door_type_2.addChild(handrail_curve);
		handrail_curve.setTextureOffset(47, 1).addCuboid(-0.2F, -34.75F, -14.7F, 0.0F, 0.0F, 1.0F, 0.2F, false);
		handrail_curve.setTextureOffset(11, 46).addCuboid(-0.2F, -17.5F, -14.7F, 0.0F, 0.0F, 1.0F, 0.2F, false);
		handrail_curve.setTextureOffset(45, 45).addCuboid(-0.2F, -34.75F, -16.5F, 0.0F, 0.0F, 1.0F, 0.2F, false);
		handrail_curve.setTextureOffset(8, 46).addCuboid(-0.2F, -17.5F, -16.5F, 0.0F, 0.0F, 1.0F, 0.2F, false);

		handrail_curve_22_r1 = new ModelPart(this);
		handrail_curve_22_r1.setPivot(-0.2F, -17.8248F, -17.1709F);
		handrail_curve.addChild(handrail_curve_22_r1);
		setRotationAngle(handrail_curve_22_r1, -1.4835F, 0.0F, 0.0F);
		handrail_curve_22_r1.setTextureOffset(36, 35).addCuboid(0.0F, 0.0F, -0.5F, 0.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_curve_21_r1 = new ModelPart(this);
		handrail_curve_21_r1.setPivot(-0.2F, -17.6498F, -16.9709F);
		handrail_curve.addChild(handrail_curve_21_r1);
		setRotationAngle(handrail_curve_21_r1, -1.1781F, 0.0F, 0.0F);
		handrail_curve_21_r1.setTextureOffset(36, 33).addCuboid(0.0F, 0.0F, -0.5F, 0.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_curve_20_r1 = new ModelPart(this);
		handrail_curve_20_r1.setPivot(-0.2F, -17.4498F, -16.6709F);
		handrail_curve.addChild(handrail_curve_20_r1);
		setRotationAngle(handrail_curve_20_r1, -0.8727F, 0.0F, 0.0F);
		handrail_curve_20_r1.setTextureOffset(36, 23).addCuboid(0.0F, 0.0F, -0.5F, 0.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_curve_19_r1 = new ModelPart(this);
		handrail_curve_19_r1.setPivot(-0.2F, -17.8748F, -17.1209F);
		handrail_curve.addChild(handrail_curve_19_r1);
		setRotationAngle(handrail_curve_19_r1, -0.6545F, 0.0F, 0.0F);
		handrail_curve_19_r1.setTextureOffset(36, 13).addCuboid(0.0F, 0.0F, 0.5F, 0.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_curve_17_r1 = new ModelPart(this);
		handrail_curve_17_r1.setPivot(-0.2F, -31.1F, -17.225F);
		handrail_curve.addChild(handrail_curve_17_r1);
		setRotationAngle(handrail_curve_17_r1, 0.0F, 0.0F, -1.5708F);
		handrail_curve_17_r1.setTextureOffset(45, 38).addCuboid(-12.5F, 0.0F, 0.0F, 15.0F, 0.0F, 0.0F, 0.2F, false);
		handrail_curve_17_r1.setTextureOffset(45, 39).addCuboid(-12.5F, 0.0F, 4.25F, 15.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_curve_16_r1 = new ModelPart(this);
		handrail_curve_16_r1.setPivot(-0.2F, -34.4252F, -17.1709F);
		handrail_curve.addChild(handrail_curve_16_r1);
		setRotationAngle(handrail_curve_16_r1, 1.4835F, 0.0F, 0.0F);
		handrail_curve_16_r1.setTextureOffset(36, 3).addCuboid(0.0F, 0.0F, -0.5F, 0.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_curve_15_r1 = new ModelPart(this);
		handrail_curve_15_r1.setPivot(-0.2F, -34.6002F, -16.9709F);
		handrail_curve.addChild(handrail_curve_15_r1);
		setRotationAngle(handrail_curve_15_r1, 1.1781F, 0.0F, 0.0F);
		handrail_curve_15_r1.setTextureOffset(36, 0).addCuboid(0.0F, 0.0F, -0.5F, 0.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_curve_14_r1 = new ModelPart(this);
		handrail_curve_14_r1.setPivot(-0.2F, -34.8002F, -16.6709F);
		handrail_curve.addChild(handrail_curve_14_r1);
		setRotationAngle(handrail_curve_14_r1, 0.8727F, 0.0F, 0.0F);
		handrail_curve_14_r1.setTextureOffset(33, 13).addCuboid(0.0F, 0.0F, -0.5F, 0.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_curve_13_r1 = new ModelPart(this);
		handrail_curve_13_r1.setPivot(-0.2F, -34.3752F, -17.1209F);
		handrail_curve.addChild(handrail_curve_13_r1);
		setRotationAngle(handrail_curve_13_r1, 0.6545F, 0.0F, 0.0F);
		handrail_curve_13_r1.setTextureOffset(32, 13).addCuboid(0.0F, 0.0F, 0.5F, 0.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_curve_11_r1 = new ModelPart(this);
		handrail_curve_11_r1.setPivot(-0.2F, -17.8248F, -13.0291F);
		handrail_curve.addChild(handrail_curve_11_r1);
		setRotationAngle(handrail_curve_11_r1, 1.4835F, 0.0F, 0.0F);
		handrail_curve_11_r1.setTextureOffset(38, 0).addCuboid(0.0F, 0.0F, 0.5F, 0.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_curve_10_r1 = new ModelPart(this);
		handrail_curve_10_r1.setPivot(-0.2F, -17.6498F, -13.2291F);
		handrail_curve.addChild(handrail_curve_10_r1);
		setRotationAngle(handrail_curve_10_r1, 1.1781F, 0.0F, 0.0F);
		handrail_curve_10_r1.setTextureOffset(38, 2).addCuboid(0.0F, 0.0F, 0.5F, 0.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_curve_9_r1 = new ModelPart(this);
		handrail_curve_9_r1.setPivot(-0.2F, -17.4498F, -13.5291F);
		handrail_curve.addChild(handrail_curve_9_r1);
		setRotationAngle(handrail_curve_9_r1, 0.8727F, 0.0F, 0.0F);
		handrail_curve_9_r1.setTextureOffset(38, 3).addCuboid(0.0F, 0.0F, 0.5F, 0.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_curve_8_r1 = new ModelPart(this);
		handrail_curve_8_r1.setPivot(-0.2F, -17.8748F, -13.0791F);
		handrail_curve.addChild(handrail_curve_8_r1);
		setRotationAngle(handrail_curve_8_r1, 0.6545F, 0.0F, 0.0F);
		handrail_curve_8_r1.setTextureOffset(38, 5).addCuboid(0.0F, 0.0F, -0.5F, 0.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_curve_5_r1 = new ModelPart(this);
		handrail_curve_5_r1.setPivot(-0.2F, -34.4252F, -13.0291F);
		handrail_curve.addChild(handrail_curve_5_r1);
		setRotationAngle(handrail_curve_5_r1, -1.4835F, 0.0F, 0.0F);
		handrail_curve_5_r1.setTextureOffset(38, 10).addCuboid(0.0F, 0.0F, 0.5F, 0.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_curve_4_r1 = new ModelPart(this);
		handrail_curve_4_r1.setPivot(-0.2F, -34.6002F, -13.2291F);
		handrail_curve.addChild(handrail_curve_4_r1);
		setRotationAngle(handrail_curve_4_r1, -1.1781F, 0.0F, 0.0F);
		handrail_curve_4_r1.setTextureOffset(38, 11).addCuboid(0.0F, 0.0F, 0.5F, 0.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_curve_3_r1 = new ModelPart(this);
		handrail_curve_3_r1.setPivot(-0.2F, -34.8002F, -13.5291F);
		handrail_curve.addChild(handrail_curve_3_r1);
		setRotationAngle(handrail_curve_3_r1, -0.8727F, 0.0F, 0.0F);
		handrail_curve_3_r1.setTextureOffset(38, 13).addCuboid(0.0F, 0.0F, 0.5F, 0.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_curve_2_r1 = new ModelPart(this);
		handrail_curve_2_r1.setPivot(-0.2F, -34.3752F, -13.0791F);
		handrail_curve.addChild(handrail_curve_2_r1);
		setRotationAngle(handrail_curve_2_r1, -0.6545F, 0.0F, 0.0F);
		handrail_curve_2_r1.setTextureOffset(38, 23).addCuboid(0.0F, 0.0F, -0.5F, 0.0F, 0.0F, 0.0F, 0.2F, false);

		end = new ModelPart(this);
		end.setPivot(0.0F, 24.0F, 0.0F);
		end.setTextureOffset(460, 121).addCuboid(-21.0F, 0.0F, -10.0F, 42.0F, 1.0F, 18.0F, 0.0F, false);

		end_door = new ModelPart(this);
		end_door.setPivot(0.0F, 4.0F, 2.2F);
		end.addChild(end_door);
		end_door.setTextureOffset(320, 35).addCuboid(0.0F, -37.0F, -12.2F, 7.0F, 34.0F, 0.0F, 0.0F, false);
		end_door.setTextureOffset(112, 429).addCuboid(-7.0F, -37.0F, -12.2F, 7.0F, 34.0F, 0.0F, 0.0F, false);
		end_door.setTextureOffset(0, 0).addCuboid(0.0F, -20.0F, -12.1F, 1.0F, 2.0F, 1.0F, 0.0F, false);
		end_door.setTextureOffset(33, 41).addCuboid(-2.0F, -20.0F, -12.1F, 2.0F, 2.0F, 1.0F, 0.0F, false);

		end_side_1 = new ModelPart(this);
		end_side_1.setPivot(0.0F, 4.0F, 2.1F);
		end.addChild(end_side_1);
		end_side_1.setTextureOffset(144, 301).addCuboid(19.3F, -17.0F, -12.1F, 2.0F, 15.0F, 21.0F, 0.0F, false);
		end_side_1.setTextureOffset(409, 120).addCuboid(7.0F, -37.0F, -12.1F, 12.0F, 33.0F, 1.0F, 0.0F, false);
		end_side_1.setTextureOffset(57, 234).addCuboid(-19.3F, -17.0F, 7.0F, 1.0F, 13.0F, 2.0F, 0.0F, false);

		upper_door_wall_r1 = new ModelPart(this);
		upper_door_wall_r1.setPivot(-18.6F, -14.875F, -48.1F);
		end_side_1.addChild(upper_door_wall_r1);
		setRotationAngle(upper_door_wall_r1, 0.0F, 0.0F, 0.1047F);
		upper_door_wall_r1.setTextureOffset(184, 0).addCuboid(-0.925F, -23.0F, 55.1F, 1.0F, 21.0F, 2.0F, 0.0F, false);

		upper_wall_r2 = new ModelPart(this);
		upper_wall_r2.setPivot(19.6F, -14.875F, -26.1F);
		end_side_1.addChild(upper_wall_r2);
		setRotationAngle(upper_wall_r2, 0.0F, 0.0F, -0.1047F);
		upper_wall_r2.setTextureOffset(0, 234).addCuboid(-0.075F, -25.0F, 14.0F, 2.0F, 23.0F, 21.0F, 0.0F, false);

		end_side_2 = new ModelPart(this);
		end_side_2.setPivot(0.0F, 4.0F, 2.1F);
		end.addChild(end_side_2);
		end_side_2.setTextureOffset(271, 71).addCuboid(-21.3F, -17.0F, -12.1F, 2.0F, 15.0F, 21.0F, 0.0F, false);
		end_side_2.setTextureOffset(419, 52).addCuboid(-19.0F, -37.0F, -12.1F, 12.0F, 33.0F, 1.0F, 0.0F, false);
		end_side_2.setTextureOffset(50, 234).addCuboid(18.3F, -17.0F, 7.0F, 1.0F, 13.0F, 2.0F, 0.0F, false);

		upper_door_wall_r2 = new ModelPart(this);
		upper_door_wall_r2.setPivot(18.6F, -14.875F, -48.1F);
		end_side_2.addChild(upper_door_wall_r2);
		setRotationAngle(upper_door_wall_r2, 0.0F, 0.0F, -0.1047F);
		upper_door_wall_r2.setTextureOffset(168, 90).addCuboid(-0.075F, -23.0F, 55.1F, 1.0F, 21.0F, 2.0F, 0.0F, false);

		upper_wall_r3 = new ModelPart(this);
		upper_wall_r3.setPivot(-19.6F, -14.875F, -26.1F);
		end_side_2.addChild(upper_wall_r3);
		setRotationAngle(upper_wall_r3, 0.0F, 0.0F, 0.1047F);
		upper_wall_r3.setTextureOffset(211, 0).addCuboid(-1.925F, -25.0F, 14.0F, 2.0F, 23.0F, 21.0F, 0.0F, false);

		seat_end_right = new ModelPart(this);
		seat_end_right.setPivot(-1.0F, 1.25F, -4.0F);
		end.addChild(seat_end_right);
		seat_end_right.setTextureOffset(326, 327).addCuboid(-18.9F, -12.0F, -5.9F, 2.0F, 5.0F, 19.0F, 0.0F, false);
		seat_end_right.setTextureOffset(103, 269).addCuboid(17.55F, -6.25F, -5.925F, 3.0F, 3.0F, 19.0F, 0.0F, false);

		seat_bottom_r2 = new ModelPart(this);
		seat_bottom_r2.setPivot(1.1F, -3.0F, 3.1F);
		seat_end_right.addChild(seat_bottom_r2);
		setRotationAngle(seat_bottom_r2, 0.0F, 0.0F, -0.0873F);
		seat_bottom_r2.setTextureOffset(127, 47).addCuboid(-20.0F, -6.0F, -9.0F, 9.0F, 1.0F, 19.0F, 0.0F, false);

		seat_back_3_r2 = new ModelPart(this);
		seat_back_3_r2.setPivot(-15.725F, -11.0F, 3.1F);
		seat_end_right.addChild(seat_back_3_r2);
		setRotationAngle(seat_back_3_r2, 0.0F, 0.0F, -0.1309F);
		seat_back_3_r2.setTextureOffset(342, 121).addCuboid(-3.0F, -6.0F, -9.0F, 2.0F, 4.0F, 19.0F, 0.0F, false);

		seat_back_2_r2 = new ModelPart(this);
		seat_back_2_r2.setPivot(-15.725F, -10.0F, 3.1F);
		seat_end_right.addChild(seat_back_2_r2);
		setRotationAngle(seat_back_2_r2, 0.0F, 0.0F, -0.0873F);
		seat_back_2_r2.setTextureOffset(342, 145).addCuboid(-3.0F, -3.0F, -9.0F, 2.0F, 1.0F, 19.0F, 0.0F, false);

		handrail_end2 = new ModelPart(this);
		handrail_end2.setPivot(2.0F, 2.75F, -8.0F);
		seat_end_right.addChild(handrail_end2);
		handrail_end2.setTextureOffset(0, 41).addCuboid(-13.8F, -35.75F, 10.5F, 0.0F, 0.0F, 7.0F, 0.2F, false);
		handrail_end2.setTextureOffset(9, 3).addCuboid(-11.7F, -38.95F, 8.2F, 0.0F, 1.0F, 0.0F, 0.2F, false);

		handrail_top_4_r1 = new ModelPart(this);
		handrail_top_4_r1.setPivot(-11.8922F, -38.1422F, 8.6421F);
		handrail_end2.addChild(handrail_top_4_r1);
		setRotationAngle(handrail_top_4_r1, -2.7699F, -0.6484F, 0.6107F);
		handrail_top_4_r1.setTextureOffset(9, 0).addCuboid(0.0F, -1.0F, 0.5F, 0.0F, 1.0F, 0.0F, 0.2F, false);

		handrail_top_3_r1 = new ModelPart(this);
		handrail_top_3_r1.setPivot(-13.3922F, -36.1922F, 9.6671F);
		handrail_end2.addChild(handrail_top_3_r1);
		setRotationAngle(handrail_top_3_r1, -0.7247F, -0.5197F, 0.2271F);
		handrail_top_3_r1.setTextureOffset(9, 0).addCuboid(0.0F, 0.0F, -1.5F, 0.0F, 0.0F, 2.0F, 0.2F, false);

		handrail_top_2_r1 = new ModelPart(this);
		handrail_top_2_r1.setPivot(-13.8422F, -35.6672F, 10.6921F);
		handrail_end2.addChild(handrail_top_2_r1);
		setRotationAngle(handrail_top_2_r1, -0.2552F, -0.1375F, 0.0202F);
		handrail_top_2_r1.setTextureOffset(13, 9).addCuboid(0.0F, 0.0F, -0.5F, 0.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_left_3_r2 = new ModelPart(this);
		handrail_left_3_r2.setPivot(-17.65F, -14.1F, 20.4F);
		handrail_end2.addChild(handrail_left_3_r2);
		setRotationAngle(handrail_left_3_r2, 0.0F, 0.0F, 1.5708F);
		handrail_left_3_r2.setTextureOffset(36, 33).addCuboid(4.0F, -5.0F, 0.1F, 0.0F, 0.0F, 1.0F, 0.2F, false);
		handrail_left_3_r2.setTextureOffset(0, 39).addCuboid(-14.0F, -5.0F, 1.1F, 18.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_left_1_r1 = new ModelPart(this);
		handrail_left_1_r1.setPivot(-12.6742F, -30.728F, 21.5F);
		handrail_end2.addChild(handrail_left_1_r1);
		setRotationAngle(handrail_left_1_r1, 0.0F, 0.0F, 1.5621F);
		handrail_left_1_r1.setTextureOffset(0, 4).addCuboid(0.5F, 0.0F, 0.0F, 2.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_turn_3_r4 = new ModelPart(this);
		handrail_turn_3_r4.setPivot(-13.6833F, -35.4121F, 18.1583F);
		handrail_end2.addChild(handrail_turn_3_r4);
		setRotationAngle(handrail_turn_3_r4, -0.574F, 0.1527F, -0.0846F);
		handrail_turn_3_r4.setTextureOffset(11, 9).addCuboid(0.0F, 0.0F, -0.5F, 0.0F, 0.0F, 1.0F, 0.2F, false);

		handrail_turn_2_r4 = new ModelPart(this);
		handrail_turn_2_r4.setPivot(-12.7F, -30.5556F, 21.463F);
		handrail_end2.addChild(handrail_turn_2_r4);
		setRotationAngle(handrail_turn_2_r4, -1.3106F, 0.0562F, -0.1369F);
		handrail_turn_2_r4.setTextureOffset(9, 3).addCuboid(0.0F, 0.0F, -2.0F, 0.0F, 0.0F, 2.0F, 0.2F, false);

		handrail_turn_1_r4 = new ModelPart(this);
		handrail_turn_1_r4.setPivot(-13.55F, -34.9556F, 18.813F);
		handrail_end2.addChild(handrail_turn_1_r4);
		setRotationAngle(handrail_turn_1_r4, -0.8197F, 0.2347F, -0.0169F);
		handrail_turn_1_r4.setTextureOffset(0, 41).addCuboid(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 3.0F, 0.2F, false);

		seat_end_left = new ModelPart(this);
		seat_end_left.setPivot(1.0F, 1.25F, -4.0F);
		end.addChild(seat_end_left);
		seat_end_left.setTextureOffset(326, 302).addCuboid(16.9F, -12.0F, -5.9F, 2.0F, 5.0F, 19.0F, 0.0F, false);
		seat_end_left.setTextureOffset(0, 279).addCuboid(-20.55F, -6.25F, -5.925F, 3.0F, 3.0F, 19.0F, 0.0F, false);

		seat_bottom_r3 = new ModelPart(this);
		seat_bottom_r3.setPivot(-1.1F, -3.0F, 3.1F);
		seat_end_left.addChild(seat_bottom_r3);
		setRotationAngle(seat_bottom_r3, 0.0F, 0.0F, 0.0873F);
		seat_bottom_r3.setTextureOffset(0, 211).addCuboid(11.0F, -6.0F, -9.0F, 9.0F, 1.0F, 19.0F, 0.0F, false);

		seat_back_3_r3 = new ModelPart(this);
		seat_back_3_r3.setPivot(15.725F, -11.0F, 3.1F);
		seat_end_left.addChild(seat_back_3_r3);
		setRotationAngle(seat_back_3_r3, 0.0F, 0.0F, 0.1309F);
		seat_back_3_r3.setTextureOffset(0, 334).addCuboid(1.0F, -6.0F, -9.0F, 2.0F, 4.0F, 19.0F, 0.0F, false);

		seat_back_2_r3 = new ModelPart(this);
		seat_back_2_r3.setPivot(15.725F, -10.0F, 3.1F);
		seat_end_left.addChild(seat_back_2_r3);
		setRotationAngle(seat_back_2_r3, 0.0F, 0.0F, 0.0873F);
		seat_back_2_r3.setTextureOffset(26, 234).addCuboid(1.0F, -3.0F, -9.0F, 2.0F, 1.0F, 19.0F, 0.0F, false);

		handrail_end = new ModelPart(this);
		handrail_end.setPivot(0.0F, 2.75F, -8.0F);
		seat_end_left.addChild(handrail_end);
		handrail_end.setTextureOffset(71, 73).addCuboid(11.8F, -35.75F, 10.5F, 0.0F, 0.0F, 7.0F, 0.2F, false);
		handrail_end.setTextureOffset(10, 3).addCuboid(9.7F, -38.95F, 8.2F, 0.0F, 1.0F, 0.0F, 0.2F, false);

		handrail_top_8_r1 = new ModelPart(this);
		handrail_top_8_r1.setPivot(9.8922F, -38.1422F, 8.6421F);
		handrail_end.addChild(handrail_top_8_r1);
		setRotationAngle(handrail_top_8_r1, -2.7699F, 0.6484F, -0.6107F);
		handrail_top_8_r1.setTextureOffset(10, 0).addCuboid(0.0F, -1.0F, 0.5F, 0.0F, 1.0F, 0.0F, 0.2F, false);

		handrail_top_3_r2 = new ModelPart(this);
		handrail_top_3_r2.setPivot(11.3922F, -36.1922F, 9.6671F);
		handrail_end.addChild(handrail_top_3_r2);
		setRotationAngle(handrail_top_3_r2, -0.7247F, 0.5197F, -0.2271F);
		handrail_top_3_r2.setTextureOffset(20, 41).addCuboid(0.0F, 0.0F, -1.5F, 0.0F, 0.0F, 2.0F, 0.2F, false);

		handrail_top_2_r2 = new ModelPart(this);
		handrail_top_2_r2.setPivot(11.8422F, -35.6672F, 10.6921F);
		handrail_end.addChild(handrail_top_2_r2);
		setRotationAngle(handrail_top_2_r2, -0.2552F, 0.1375F, -0.0202F);
		handrail_top_2_r2.setTextureOffset(38, 39).addCuboid(0.0F, 0.0F, -0.5F, 0.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_left_3_r3 = new ModelPart(this);
		handrail_left_3_r3.setPivot(15.65F, -14.1F, 20.4F);
		handrail_end.addChild(handrail_left_3_r3);
		setRotationAngle(handrail_left_3_r3, 0.0F, 0.0F, -1.5708F);
		handrail_left_3_r3.setTextureOffset(47, 26).addCuboid(-4.0F, -5.0F, 0.1F, 0.0F, 0.0F, 1.0F, 0.2F, false);
		handrail_left_3_r3.setTextureOffset(45, 32).addCuboid(-4.0F, -5.0F, 1.1F, 18.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_left_1_r2 = new ModelPart(this);
		handrail_left_1_r2.setPivot(10.6742F, -30.728F, 21.5F);
		handrail_end.addChild(handrail_left_1_r2);
		setRotationAngle(handrail_left_1_r2, 0.0F, 0.0F, -1.5621F);
		handrail_left_1_r2.setTextureOffset(0, 5).addCuboid(-2.5F, 0.0F, 0.0F, 2.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_turn_3_r5 = new ModelPart(this);
		handrail_turn_3_r5.setPivot(10.7F, -30.5556F, 21.463F);
		handrail_end.addChild(handrail_turn_3_r5);
		setRotationAngle(handrail_turn_3_r5, -1.3106F, -0.0562F, 0.1369F);
		handrail_turn_3_r5.setTextureOffset(33, 0).addCuboid(0.0F, 0.0F, -2.0F, 0.0F, 0.0F, 2.0F, 0.2F, false);

		handrail_turn_2_r5 = new ModelPart(this);
		handrail_turn_2_r5.setPivot(11.55F, -34.9556F, 18.813F);
		handrail_end.addChild(handrail_turn_2_r5);
		setRotationAngle(handrail_turn_2_r5, -0.8197F, -0.2347F, 0.0169F);
		handrail_turn_2_r5.setTextureOffset(8, 41).addCuboid(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 3.0F, 0.2F, false);

		handrail_turn_1_r5 = new ModelPart(this);
		handrail_turn_1_r5.setPivot(11.6833F, -35.4121F, 18.1583F);
		handrail_end.addChild(handrail_turn_1_r5);
		setRotationAngle(handrail_turn_1_r5, -0.574F, -0.1527F, 0.0846F);
		handrail_turn_1_r5.setTextureOffset(36, 0).addCuboid(0.0F, 0.0F, -0.5F, 0.0F, 0.0F, 1.0F, 0.2F, false);

		end_light_right = new ModelPart(this);
		end_light_right.setPivot(-0.6F, -0.3F, -0.025F);
		end.addChild(end_light_right);
		

		light_r1 = new ModelPart(this);
		light_r1.setPivot(-8.4206F, -31.1645F, -8.95F);
		end_light_right.addChild(light_r1);
		setRotationAngle(light_r1, 0.0F, 0.0F, -1.5708F);
		light_r1.setTextureOffset(20, 0).addCuboid(-0.5F, -1.1F, 0.0F, 2.0F, 2.0F, 0.0F, 0.0F, false);

		end_light_off_right = new ModelPart(this);
		end_light_off_right.setPivot(-0.2F, 0.1F, 0.025F);
		end.addChild(end_light_off_right);
		

		light_r2 = new ModelPart(this);
		light_r2.setPivot(-8.3206F, -31.1145F, -8.975F);
		end_light_off_right.addChild(light_r2);
		setRotationAngle(light_r2, 0.0F, 0.0F, -1.5708F);
		light_r2.setTextureOffset(11, 11).addCuboid(0.5F, -1.1F, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, false);

		end_light_on_right = new ModelPart(this);
		end_light_on_right.setPivot(0.0F, 0.0F, 0.025F);
		end.addChild(end_light_on_right);
		

		light_r3 = new ModelPart(this);
		light_r3.setPivot(-8.5206F, -31.0145F, -8.975F);
		end_light_on_right.addChild(light_r3);
		setRotationAngle(light_r3, 0.0F, 0.0F, -1.5708F);
		light_r3.setTextureOffset(12, 0).addCuboid(0.5F, -1.1F, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, false);

		end_light_left = new ModelPart(this);
		end_light_left.setPivot(0.6F, -0.3F, -0.025F);
		end.addChild(end_light_left);
		

		light_r4 = new ModelPart(this);
		light_r4.setPivot(8.4206F, -31.1645F, -8.95F);
		end_light_left.addChild(light_r4);
		setRotationAngle(light_r4, 0.0F, 0.0F, 1.5708F);
		light_r4.setTextureOffset(20, 3).addCuboid(-1.5F, -1.1F, 0.0F, 2.0F, 2.0F, 0.0F, 0.0F, false);

		end_light_off_left = new ModelPart(this);
		end_light_off_left.setPivot(0.2F, 0.1F, 0.025F);
		end.addChild(end_light_off_left);
		

		light_r5 = new ModelPart(this);
		light_r5.setPivot(8.3206F, -31.1145F, -8.975F);
		end_light_off_left.addChild(light_r5);
		setRotationAngle(light_r5, 0.0F, 0.0F, 1.5708F);
		light_r5.setTextureOffset(12, 3).addCuboid(-1.5F, -1.1F, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, false);

		end_light_on_left = new ModelPart(this);
		end_light_on_left.setPivot(0.0F, 0.0F, 0.025F);
		end.addChild(end_light_on_left);
		

		light_r6 = new ModelPart(this);
		light_r6.setPivot(8.5206F, -31.0145F, -8.975F);
		end_light_on_left.addChild(light_r6);
		setRotationAngle(light_r6, 0.0F, 0.0F, 1.5708F);
		light_r6.setTextureOffset(11, 13).addCuboid(-1.5F, -1.1F, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, false);

		end_exterior = new ModelPart(this);
		end_exterior.setPivot(0.0F, 24.0F, 0.0F);
		

		end_bottom_out = new ModelPart(this);
		end_bottom_out.setPivot(0.0F, 0.1F, -21.0F);
		end_exterior.addChild(end_bottom_out);
		end_bottom_out.setTextureOffset(127, 72).addCuboid(-21.5F, 0.0F, 10.0F, 43.0F, 3.0F, 2.0F, 0.0F, false);

		buttom_panel_left_9_r1 = new ModelPart(this);
		buttom_panel_left_9_r1.setPivot(-15.625F, 4.0F, 14.925F);
		end_bottom_out.addChild(buttom_panel_left_9_r1);
		setRotationAngle(buttom_panel_left_9_r1, 0.0F, -0.0436F, 0.0F);
		buttom_panel_left_9_r1.setTextureOffset(82, 175).addCuboid(15.0F, -4.0F, -12.5F, 3.0F, 3.0F, 7.0F, 0.0F, false);

		buttom_panel_left_8_r1 = new ModelPart(this);
		buttom_panel_left_8_r1.setPivot(-13.2F, 4.0F, 15.075F);
		end_bottom_out.addChild(buttom_panel_left_8_r1);
		setRotationAngle(buttom_panel_left_8_r1, 0.0F, -0.1309F, 0.0F);
		buttom_panel_left_8_r1.setTextureOffset(106, 175).addCuboid(14.3F, -4.0F, -13.875F, 3.0F, 3.0F, 7.0F, 0.0F, false);

		buttom_panel_left_7_r1 = new ModelPart(this);
		buttom_panel_left_7_r1.setPivot(-11.625F, 4.0F, 12.55F);
		end_bottom_out.addChild(buttom_panel_left_7_r1);
		setRotationAngle(buttom_panel_left_7_r1, 0.0F, -0.2182F, 0.0F);
		buttom_panel_left_7_r1.setTextureOffset(131, 175).addCuboid(15.0F, -4.0F, -12.5F, 3.0F, 3.0F, 7.0F, 0.0F, false);

		buttom_panel_left_6_r1 = new ModelPart(this);
		buttom_panel_left_6_r1.setPivot(-9.3F, 4.0F, 13.125F);
		end_bottom_out.addChild(buttom_panel_left_6_r1);
		setRotationAngle(buttom_panel_left_6_r1, 0.0F, -0.3054F, 0.0F);
		buttom_panel_left_6_r1.setTextureOffset(155, 175).addCuboid(14.3F, -4.0F, -13.875F, 3.0F, 3.0F, 7.0F, 0.0F, false);

		buttom_panel_left_5_r1 = new ModelPart(this);
		buttom_panel_left_5_r1.setPivot(-7.125F, 4.0F, 11.65F);
		end_bottom_out.addChild(buttom_panel_left_5_r1);
		setRotationAngle(buttom_panel_left_5_r1, 0.0F, -0.3491F, 0.0F);
		buttom_panel_left_5_r1.setTextureOffset(211, 0).addCuboid(15.0F, -4.0F, -12.5F, 3.0F, 3.0F, 7.0F, 0.0F, false);

		buttom_panel_left_4_r1 = new ModelPart(this);
		buttom_panel_left_4_r1.setPivot(-5.125F, 4.0F, 12.45F);
		end_bottom_out.addChild(buttom_panel_left_4_r1);
		setRotationAngle(buttom_panel_left_4_r1, 0.0F, -0.4363F, 0.0F);
		buttom_panel_left_4_r1.setTextureOffset(215, 147).addCuboid(14.3F, -4.0F, -13.875F, 3.0F, 3.0F, 7.0F, 0.0F, false);

		buttom_panel_left_3_r1 = new ModelPart(this);
		buttom_panel_left_3_r1.setPivot(-3.125F, 4.0F, 11.125F);
		end_bottom_out.addChild(buttom_panel_left_3_r1);
		setRotationAngle(buttom_panel_left_3_r1, 0.0F, -0.48F, 0.0F);
		buttom_panel_left_3_r1.setTextureOffset(82, 217).addCuboid(15.0F, -4.0F, -12.5F, 3.0F, 3.0F, 7.0F, 0.0F, false);

		buttom_panel_left_2_r1 = new ModelPart(this);
		buttom_panel_left_2_r1.setPivot(-1.125F, 4.0F, 12.375F);
		end_bottom_out.addChild(buttom_panel_left_2_r1);
		setRotationAngle(buttom_panel_left_2_r1, 0.0F, -0.5672F, 0.0F);
		buttom_panel_left_2_r1.setTextureOffset(103, 217).addCuboid(14.3F, -4.0F, -14.0F, 3.0F, 3.0F, 7.0F, 0.0F, false);

		buttom_panel_right_9_r1 = new ModelPart(this);
		buttom_panel_right_9_r1.setPivot(15.625F, 4.0F, 14.925F);
		end_bottom_out.addChild(buttom_panel_right_9_r1);
		setRotationAngle(buttom_panel_right_9_r1, 0.0F, 0.0436F, 0.0F);
		buttom_panel_right_9_r1.setTextureOffset(124, 217).addCuboid(-18.0F, -4.0F, -12.5F, 3.0F, 3.0F, 7.0F, 0.0F, false);

		buttom_panel_right_8_r1 = new ModelPart(this);
		buttom_panel_right_8_r1.setPivot(13.2F, 4.0F, 15.075F);
		end_bottom_out.addChild(buttom_panel_right_8_r1);
		setRotationAngle(buttom_panel_right_8_r1, 0.0F, 0.1309F, 0.0F);
		buttom_panel_right_8_r1.setTextureOffset(145, 217).addCuboid(-17.3F, -4.0F, -13.875F, 3.0F, 3.0F, 7.0F, 0.0F, false);

		buttom_panel_right_7_r1 = new ModelPart(this);
		buttom_panel_right_7_r1.setPivot(11.625F, 4.0F, 12.55F);
		end_bottom_out.addChild(buttom_panel_right_7_r1);
		setRotationAngle(buttom_panel_right_7_r1, 0.0F, 0.2182F, 0.0F);
		buttom_panel_right_7_r1.setTextureOffset(38, 219).addCuboid(-18.0F, -4.0F, -12.5F, 3.0F, 3.0F, 7.0F, 0.0F, false);

		buttom_panel_right_6_r1 = new ModelPart(this);
		buttom_panel_right_6_r1.setPivot(9.3F, 4.0F, 13.125F);
		end_bottom_out.addChild(buttom_panel_right_6_r1);
		setRotationAngle(buttom_panel_right_6_r1, 0.0F, 0.3054F, 0.0F);
		buttom_panel_right_6_r1.setTextureOffset(226, 70).addCuboid(-17.3F, -4.0F, -13.875F, 3.0F, 3.0F, 7.0F, 0.0F, false);

		buttom_panel_right_5_r1 = new ModelPart(this);
		buttom_panel_right_5_r1.setPivot(7.125F, 4.0F, 11.65F);
		end_bottom_out.addChild(buttom_panel_right_5_r1);
		setRotationAngle(buttom_panel_right_5_r1, 0.0F, 0.3491F, 0.0F);
		buttom_panel_right_5_r1.setTextureOffset(226, 81).addCuboid(-18.0F, -4.0F, -12.5F, 3.0F, 3.0F, 7.0F, 0.0F, false);

		buttom_panel_right_4_r1 = new ModelPart(this);
		buttom_panel_right_4_r1.setPivot(5.125F, 4.0F, 12.45F);
		end_bottom_out.addChild(buttom_panel_right_4_r1);
		setRotationAngle(buttom_panel_right_4_r1, 0.0F, 0.4363F, 0.0F);
		buttom_panel_right_4_r1.setTextureOffset(227, 107).addCuboid(-17.3F, -4.0F, -13.875F, 3.0F, 3.0F, 7.0F, 0.0F, false);

		buttom_panel_right_3_r1 = new ModelPart(this);
		buttom_panel_right_3_r1.setPivot(3.125F, 4.0F, 11.125F);
		end_bottom_out.addChild(buttom_panel_right_3_r1);
		setRotationAngle(buttom_panel_right_3_r1, 0.0F, 0.48F, 0.0F);
		buttom_panel_right_3_r1.setTextureOffset(227, 118).addCuboid(-18.0F, -4.0F, -12.5F, 3.0F, 3.0F, 7.0F, 0.0F, false);

		buttom_panel_right_2_r1 = new ModelPart(this);
		buttom_panel_right_2_r1.setPivot(1.125F, 4.0F, 12.375F);
		end_bottom_out.addChild(buttom_panel_right_2_r1);
		setRotationAngle(buttom_panel_right_2_r1, 0.0F, 0.5672F, 0.0F);
		buttom_panel_right_2_r1.setTextureOffset(229, 160).addCuboid(-17.3F, -4.0F, -14.0F, 3.0F, 3.0F, 7.0F, 0.0F, false);

		end_back = new ModelPart(this);
		end_back.setPivot(0.0F, 0.0F, -21.0F);
		end_exterior.addChild(end_back);
		end_back.setTextureOffset(67, 90).addCuboid(-7.8F, -42.0F, 6.125F, 1.0F, 44.0F, 0.0F, 0.0F, false);
		end_back.setTextureOffset(178, 164).addCuboid(6.8F, -42.0F, 6.125F, 1.0F, 44.0F, 0.0F, 0.0F, false);
		end_back.setTextureOffset(45, 66).addCuboid(-8.2F, -42.0F, 5.975F, 16.0F, 9.0F, 0.0F, 0.0F, false);
		end_back.setTextureOffset(47, 255).addCuboid(7.05F, -34.0F, 5.975F, 0.0F, 36.0F, 6.0F, 0.0F, false);
		end_back.setTextureOffset(420, 355).addCuboid(-7.05F, -34.0F, 5.975F, 0.0F, 35.0F, 6.0F, 0.0F, false);
		end_back.setTextureOffset(111, 147).addCuboid(-7.975F, -33.0F, 5.975F, 16.0F, 0.0F, 6.0F, 0.0F, false);

		front_left_panel_3_r1 = new ModelPart(this);
		front_left_panel_3_r1.setPivot(-9.2F, 4.0F, 4.15F);
		end_back.addChild(front_left_panel_3_r1);
		setRotationAngle(front_left_panel_3_r1, 0.0F, -0.1745F, 0.0F);
		front_left_panel_3_r1.setTextureOffset(291, 231).addCuboid(17.0F, -46.0F, -1.0F, 5.0F, 44.0F, 0.0F, 0.0F, false);

		front_left_panel_2_r1 = new ModelPart(this);
		front_left_panel_2_r1.setPivot(-3.7F, 4.0F, 2.1F);
		end_back.addChild(front_left_panel_2_r1);
		setRotationAngle(front_left_panel_2_r1, 0.0F, -0.3491F, 0.0F);
		front_left_panel_2_r1.setTextureOffset(327, 140).addCuboid(17.0F, -46.0F, -1.0F, 5.0F, 44.0F, 0.0F, 0.0F, false);

		front_left_panel_1_r1 = new ModelPart(this);
		front_left_panel_1_r1.setPivot(1.5F, 4.0F, 1.6F);
		end_back.addChild(front_left_panel_1_r1);
		setRotationAngle(front_left_panel_1_r1, 0.0F, -0.48F, 0.0F);
		front_left_panel_1_r1.setTextureOffset(65, 334).addCuboid(17.0F, -46.0F, -1.0F, 5.0F, 44.0F, 0.0F, 0.0F, false);

		front_right_panel_3_r1 = new ModelPart(this);
		front_right_panel_3_r1.setPivot(9.2F, 4.0F, 4.15F);
		end_back.addChild(front_right_panel_3_r1);
		setRotationAngle(front_right_panel_3_r1, 0.0F, 0.1745F, 0.0F);
		front_right_panel_3_r1.setTextureOffset(76, 334).addCuboid(-22.0F, -46.0F, -1.0F, 5.0F, 44.0F, 0.0F, 0.0F, false);

		front_right_panel_2_r1 = new ModelPart(this);
		front_right_panel_2_r1.setPivot(3.7F, 4.0F, 2.1F);
		end_back.addChild(front_right_panel_2_r1);
		setRotationAngle(front_right_panel_2_r1, 0.0F, 0.3491F, 0.0F);
		front_right_panel_2_r1.setTextureOffset(353, 355).addCuboid(-22.0F, -46.0F, -1.0F, 5.0F, 44.0F, 0.0F, 0.0F, false);

		front_right_panel_1_r1 = new ModelPart(this);
		front_right_panel_1_r1.setPivot(-1.5F, 4.0F, 1.6F);
		end_back.addChild(front_right_panel_1_r1);
		setRotationAngle(front_right_panel_1_r1, 0.0F, 0.48F, 0.0F);
		front_right_panel_1_r1.setTextureOffset(370, 0).addCuboid(-22.0F, -46.0F, -1.0F, 5.0F, 44.0F, 0.0F, 0.0F, false);

		end_door_exterior = new ModelPart(this);
		end_door_exterior.setPivot(0.0F, 0.0F, 0.0F);
		end_back.addChild(end_door_exterior);
		end_door_exterior.setTextureOffset(142, 429).addCuboid(0.0F, -33.0F, 10.9F, 7.0F, 34.0F, 0.0F, 0.0F, false);
		end_door_exterior.setTextureOffset(127, 429).addCuboid(-7.0F, -33.0F, 10.9F, 7.0F, 34.0F, 0.0F, 0.0F, false);
		end_door_exterior.setTextureOffset(225, 199).addCuboid(-9.0F, -26.0F, 6.1F, 18.0F, 19.0F, 0.0F, 0.0F, false);

		end_handle_exterior_1 = new ModelPart(this);
		end_handle_exterior_1.setPivot(6.825F, 5.45F, 44.85F);
		end_back.addChild(end_handle_exterior_1);
		setRotationAngle(end_handle_exterior_1, -3.1416F, 0.0F, 3.1416F);
		

		handle_1_r1 = new ModelPart(this);
		handle_1_r1.setPivot(-6.3F, -33.1F, -60.725F);
		end_handle_exterior_1.addChild(handle_1_r1);
		setRotationAngle(handle_1_r1, 0.0F, 0.0F, 1.5708F);
		handle_1_r1.setTextureOffset(19, 78).addCuboid(-4.75F, -21.3769F, 98.3736F, 7.0F, 0.0F, 2.0F, 0.1F, false);

		end_handle_exterior_2 = new ModelPart(this);
		end_handle_exterior_2.setPivot(6.825F, 23.45F, 44.85F);
		end_back.addChild(end_handle_exterior_2);
		setRotationAngle(end_handle_exterior_2, -3.1416F, 0.0F, 3.1416F);
		

		handle_2_r1 = new ModelPart(this);
		handle_2_r1.setPivot(-6.3F, -51.1F, -60.725F);
		end_handle_exterior_2.addChild(handle_2_r1);
		setRotationAngle(handle_2_r1, 0.0F, 0.0F, 1.5708F);
		handle_2_r1.setTextureOffset(0, 78).addCuboid(13.25F, -21.3769F, 98.3736F, 7.0F, 0.0F, 2.0F, 0.1F, false);

		end_handle_exterior_3 = new ModelPart(this);
		end_handle_exterior_3.setPivot(-6.825F, 5.45F, 44.85F);
		end_back.addChild(end_handle_exterior_3);
		setRotationAngle(end_handle_exterior_3, -3.1416F, 0.0F, -3.1416F);
		

		handle_3_r1 = new ModelPart(this);
		handle_3_r1.setPivot(7.35F, -33.1F, -60.725F);
		end_handle_exterior_3.addChild(handle_3_r1);
		setRotationAngle(handle_3_r1, 0.0F, 0.0F, 1.5708F);
		handle_3_r1.setTextureOffset(20, 10).addCuboid(-4.75F, 22.4231F, 98.3736F, 7.0F, 0.0F, 2.0F, 0.1F, false);

		end_handle_exterior_4 = new ModelPart(this);
		end_handle_exterior_4.setPivot(-28.1789F, -17.1496F, 16.3939F);
		end_back.addChild(end_handle_exterior_4);
		setRotationAngle(end_handle_exterior_4, -2.8362F, 0.0F, 1.5708F);
		end_handle_exterior_4.setTextureOffset(0, 90).addCuboid(2.0F, 11.818F, 3.0282F, 3.0F, 8.0F, 2.0F, 0.0F, false);

		end_side_right = new ModelPart(this);
		end_side_right.setPivot(0.0F, 0.0F, 25.0F);
		end_exterior.addChild(end_side_right);
		end_side_right.setTextureOffset(127, 0).addCuboid(-21.5F, -13.0F, -13.975F, 2.0F, 13.0F, 0.0F, 0.0F, false);
		end_side_right.setTextureOffset(45, 41).addCuboid(21.5F, 0.0F, -37.0F, 0.0F, 4.0F, 20.0F, 0.0F, false);
		end_side_right.setTextureOffset(257, 302).addCuboid(21.5F, -13.0F, -37.0F, 0.0F, 13.0F, 23.0F, 0.0F, false);
		end_side_right.setTextureOffset(159, 221).addCuboid(0.0F, -41.375F, -40.0F, 3.0F, 0.0F, 7.0F, 0.0F, false);

		outer_right_roof_5_r1 = new ModelPart(this);
		outer_right_roof_5_r1.setPivot(8.978F, -39.8555F, -27.5F);
		end_side_right.addChild(outer_right_roof_5_r1);
		setRotationAngle(outer_right_roof_5_r1, 0.0F, 0.0F, 0.0698F);
		outer_right_roof_5_r1.setTextureOffset(0, 152).addCuboid(-6.2645F, -1.1097F, -12.5F, 7.0F, 0.0F, 7.0F, 0.0F, false);

		outer_right_roof_4_r1 = new ModelPart(this);
		outer_right_roof_4_r1.setPivot(9.578F, -39.7555F, -27.5F);
		end_side_right.addChild(outer_right_roof_4_r1);
		setRotationAngle(outer_right_roof_4_r1, 0.0F, 0.0F, 0.288F);
		outer_right_roof_4_r1.setTextureOffset(195, 146).addCuboid(-0.497F, -1.1228F, -12.5F, 6.0F, 0.0F, 7.0F, 0.0F, false);

		outer_right_roof_3_r1 = new ModelPart(this);
		outer_right_roof_3_r1.setPivot(9.578F, -39.7555F, -27.5F);
		end_side_right.addChild(outer_right_roof_3_r1);
		setRotationAngle(outer_right_roof_3_r1, 0.0F, 0.0F, 0.4014F);
		outer_right_roof_3_r1.setTextureOffset(226, 92).addCuboid(5.3154F, -1.7351F, -12.5F, 3.0F, 0.0F, 7.0F, 0.0F, false);

		outer_right_roof_2_r1 = new ModelPart(this);
		outer_right_roof_2_r1.setPivot(9.578F, -39.7555F, -27.5F);
		end_side_right.addChild(outer_right_roof_2_r1);
		setRotationAngle(outer_right_roof_2_r1, 0.0F, 0.0F, 0.9599F);
		outer_right_roof_2_r1.setTextureOffset(127, 47).addCuboid(6.1006F, -5.8625F, -12.5F, 2.0F, 0.0F, 7.0F, 0.0F, false);

		outer_right_roof_1_r1 = new ModelPart(this);
		outer_right_roof_1_r1.setPivot(9.578F, -39.7555F, -27.5F);
		end_side_right.addChild(outer_right_roof_1_r1);
		setRotationAngle(outer_right_roof_1_r1, 0.0F, 0.0F, 1.3788F);
		outer_right_roof_1_r1.setTextureOffset(49, 90).addCuboid(4.9967F, -8.6386F, -12.5F, 1.0F, 0.0F, 7.0F, 0.0F, false);

		side_right_panel_3_r1 = new ModelPart(this);
		side_right_panel_3_r1.setPivot(23.55F, -10.875F, -20.1F);
		end_side_right.addChild(side_right_panel_3_r1);
		setRotationAngle(side_right_panel_3_r1, 0.0F, 0.0F, -0.1047F);
		side_right_panel_3_r1.setTextureOffset(131, 164).addCuboid(-1.85F, -25.0F, -16.9F, 0.0F, 23.0F, 23.0F, 0.0F, false);

		right_side_2_r1 = new ModelPart(this);
		right_side_2_r1.setPivot(-20.95F, -9.875F, 38.125F);
		end_side_right.addChild(right_side_2_r1);
		setRotationAngle(right_side_2_r1, 0.0F, 0.0F, 0.1047F);
		right_side_2_r1.setTextureOffset(179, 230).addCuboid(-0.85F, -24.0F, -52.1F, 2.0F, 21.0F, 0.0F, 0.0F, false);

		end_side_left = new ModelPart(this);
		end_side_left.setPivot(0.0F, 0.0F, 25.0F);
		end_exterior.addChild(end_side_left);
		end_side_left.setTextureOffset(116, 90).addCuboid(19.5F, -13.0F, -13.975F, 2.0F, 13.0F, 0.0F, 0.0F, false);
		end_side_left.setTextureOffset(45, 0).addCuboid(-21.5F, 0.0F, -37.0F, 0.0F, 4.0F, 20.0F, 0.0F, false);
		end_side_left.setTextureOffset(201, 301).addCuboid(-21.5F, -13.0F, -37.0F, 0.0F, 13.0F, 23.0F, 0.0F, false);
		end_side_left.setTextureOffset(46, 188).addCuboid(-3.0F, -41.375F, -40.0F, 3.0F, 0.0F, 7.0F, 0.0F, false);

		outer_left_roof_5_r1 = new ModelPart(this);
		outer_left_roof_5_r1.setPivot(-8.978F, -39.8555F, -27.5F);
		end_side_left.addChild(outer_left_roof_5_r1);
		setRotationAngle(outer_left_roof_5_r1, 0.0F, 0.0F, -0.0698F);
		outer_left_roof_5_r1.setTextureOffset(149, 147).addCuboid(-0.7355F, -1.1097F, -12.5F, 7.0F, 0.0F, 7.0F, 0.0F, false);

		outer_left_roof_4_r1 = new ModelPart(this);
		outer_left_roof_4_r1.setPivot(-9.578F, -39.7555F, -27.5F);
		end_side_left.addChild(outer_left_roof_4_r1);
		setRotationAngle(outer_left_roof_4_r1, 0.0F, 0.0F, -0.288F);
		outer_left_roof_4_r1.setTextureOffset(195, 90).addCuboid(-5.503F, -1.1228F, -12.5F, 6.0F, 0.0F, 7.0F, 0.0F, false);

		outer_left_roof_3_r1 = new ModelPart(this);
		outer_left_roof_3_r1.setPivot(-9.578F, -39.7555F, -27.5F);
		end_side_left.addChild(outer_left_roof_3_r1);
		setRotationAngle(outer_left_roof_3_r1, 0.0F, 0.0F, -0.4014F);
		outer_left_roof_3_r1.setTextureOffset(211, 11).addCuboid(-8.3154F, -1.7351F, -12.5F, 3.0F, 0.0F, 7.0F, 0.0F, false);

		outer_left_roof_2_r1 = new ModelPart(this);
		outer_left_roof_2_r1.setPivot(-9.578F, -39.7555F, -27.5F);
		end_side_left.addChild(outer_left_roof_2_r1);
		setRotationAngle(outer_left_roof_2_r1, 0.0F, 0.0F, -0.9599F);
		outer_left_roof_2_r1.setTextureOffset(66, 52).addCuboid(-8.1006F, -5.8625F, -12.5F, 2.0F, 0.0F, 7.0F, 0.0F, false);

		outer_left_roof_1_r1 = new ModelPart(this);
		outer_left_roof_1_r1.setPivot(-9.578F, -39.7555F, -27.5F);
		end_side_left.addChild(outer_left_roof_1_r1);
		setRotationAngle(outer_left_roof_1_r1, 0.0F, 0.0F, -1.3788F);
		outer_left_roof_1_r1.setTextureOffset(15, 90).addCuboid(-5.9967F, -8.6386F, -12.5F, 1.0F, 0.0F, 7.0F, 0.0F, false);

		side_left_panel_3_r1 = new ModelPart(this);
		side_left_panel_3_r1.setPivot(-23.55F, -10.875F, -20.1F);
		end_side_left.addChild(side_left_panel_3_r1);
		setRotationAngle(side_left_panel_3_r1, 0.0F, 0.0F, 0.1047F);
		side_left_panel_3_r1.setTextureOffset(82, 164).addCuboid(1.85F, -25.0F, -16.9F, 0.0F, 23.0F, 23.0F, 0.0F, false);

		left_side_2_r2 = new ModelPart(this);
		left_side_2_r2.setPivot(20.95F, -9.875F, 38.125F);
		end_side_left.addChild(left_side_2_r2);
		setRotationAngle(left_side_2_r2, 0.0F, 0.0F, -0.1047F);
		left_side_2_r2.setTextureOffset(191, 0).addCuboid(-1.15F, -24.0F, -52.1F, 2.0F, 21.0F, 0.0F, 0.0F, false);

		end_bottom_panel = new ModelPart(this);
		end_bottom_panel.setPivot(0.0F, 0.0F, -21.0F);
		end_exterior.addChild(end_bottom_panel);
		end_bottom_panel.setTextureOffset(460, 101).addCuboid(-21.5F, 3.0F, 10.0F, 43.0F, 0.0F, 19.0F, 0.0F, false);

		roof_end = new ModelPart(this);
		roof_end.setPivot(0.0F, 24.0F, 0.0F);
		roof_end.setTextureOffset(195, 140).addCuboid(-17.05F, -35.875F, -10.1F, 34.0F, 3.0F, 2.0F, 0.0F, false);
		roof_end.setTextureOffset(0, 82).addCuboid(-17.05F, -37.875F, -5.0F, 34.0F, 3.0F, 0.0F, 0.0F, false);

		end_mid_roof_4_r1 = new ModelPart(this);
		end_mid_roof_4_r1.setPivot(-9.65F, -10.875F, -14.1F);
		roof_end.addChild(end_mid_roof_4_r1);
		setRotationAngle(end_mid_roof_4_r1, 0.0F, 0.0F, -0.3054F);
		end_mid_roof_4_r1.setTextureOffset(225, 219).addCuboid(-1.0F, -25.0F, 6.0F, 8.0F, 2.0F, 3.0F, 0.0F, false);

		end_mid_roof_3_r1 = new ModelPart(this);
		end_mid_roof_3_r1.setPivot(9.65F, -10.875F, -14.1F);
		roof_end.addChild(end_mid_roof_3_r1);
		setRotationAngle(end_mid_roof_3_r1, 0.0F, 0.0F, 0.3054F);
		end_mid_roof_3_r1.setTextureOffset(0, 191).addCuboid(-7.0F, -25.0F, 6.0F, 8.0F, 2.0F, 3.0F, 0.0F, false);

		end_roof_left = new ModelPart(this);
		end_roof_left.setPivot(16.95F, -12.875F, -17.1F);
		roof_end.addChild(end_roof_left);
		end_roof_left.setTextureOffset(405, 105).addCuboid(-0.9F, -20.075F, 12.1F, 2.0F, 0.0F, 50.0F, 0.0F, false);
		end_roof_left.setTextureOffset(434, 407).addCuboid(-6.95F, -21.925F, 12.1F, 1.0F, 0.0F, 50.0F, 0.0F, false);
		end_roof_left.setTextureOffset(326, 304).addCuboid(-17.15F, -24.125F, 12.1F, 9.0F, 0.0F, 50.0F, 0.0F, false);

		inner_roof_4_r5 = new ModelPart(this);
		inner_roof_4_r5.setPivot(-5.35F, -20.225F, 41.1F);
		end_roof_left.addChild(inner_roof_4_r5);
		setRotationAngle(inner_roof_4_r5, 0.0F, 0.0F, 0.829F);
		inner_roof_4_r5.setTextureOffset(203, 378).addCuboid(-5.3F, 0.0F, -29.0F, 3.0F, 0.0F, 50.0F, 0.0F, false);

		inner_roof_2_r5 = new ModelPart(this);
		inner_roof_2_r5.setPivot(-2.05F, -21.575F, 52.1F);
		end_roof_left.addChild(inner_roof_2_r5);
		setRotationAngle(inner_roof_2_r5, 0.0F, 0.0F, 0.9163F);
		inner_roof_2_r5.setTextureOffset(343, 0).addCuboid(-4.1F, 0.0F, -40.0F, 6.0F, 0.0F, 50.0F, 0.0F, false);

		end_roof_right = new ModelPart(this);
		end_roof_right.setPivot(-16.95F, -12.875F, -17.1F);
		roof_end.addChild(end_roof_right);
		end_roof_right.setTextureOffset(260, 405).addCuboid(-1.1F, -20.075F, 12.1F, 2.0F, 0.0F, 50.0F, 0.0F, false);
		end_roof_right.setTextureOffset(0, 434).addCuboid(5.95F, -21.925F, 12.1F, 1.0F, 0.0F, 50.0F, 0.0F, false);
		end_roof_right.setTextureOffset(308, 210).addCuboid(8.15F, -24.125F, 12.1F, 9.0F, 0.0F, 50.0F, 0.0F, false);

		inner_roof_4_r6 = new ModelPart(this);
		inner_roof_4_r6.setPivot(5.35F, -20.225F, 41.1F);
		end_roof_right.addChild(inner_roof_4_r6);
		setRotationAngle(inner_roof_4_r6, 0.0F, 0.0F, -0.829F);
		inner_roof_4_r6.setTextureOffset(389, 355).addCuboid(2.3F, 0.0F, -29.0F, 3.0F, 0.0F, 50.0F, 0.0F, false);

		inner_roof_2_r6 = new ModelPart(this);
		inner_roof_2_r6.setPivot(2.05F, -21.575F, 52.1F);
		end_roof_right.addChild(inner_roof_2_r6);
		setRotationAngle(inner_roof_2_r6, 0.0F, 0.0F, -0.9163F);
		inner_roof_2_r6.setTextureOffset(261, 354).addCuboid(-1.9F, 0.0F, -40.0F, 6.0F, 0.0F, 50.0F, 0.0F, false);

		roof_end_exterior = new ModelPart(this);
		roof_end_exterior.setPivot(0.0F, 24.0F, 0.0F);
		

		outer_roof_right = new ModelPart(this);
		outer_roof_right.setPivot(-0.1F, -2.7F, -35.0F);
		roof_end_exterior.addChild(outer_roof_right);
		outer_roof_right.setTextureOffset(194, 161).addCuboid(0.1F, -38.675F, 17.0F, 3.0F, 0.0F, 69.0F, 0.0F, false);

		outer_left_roof_5_r2 = new ModelPart(this);
		outer_left_roof_5_r2.setPivot(9.078F, -37.1555F, 29.5F);
		outer_roof_right.addChild(outer_left_roof_5_r2);
		setRotationAngle(outer_left_roof_5_r2, 0.0F, 0.0F, 0.0698F);
		outer_left_roof_5_r2.setTextureOffset(111, 90).addCuboid(-6.2645F, -1.1097F, -12.5F, 7.0F, 0.0F, 69.0F, 0.0F, false);

		outer_left_roof_4_r2 = new ModelPart(this);
		outer_left_roof_4_r2.setPivot(9.678F, -37.0555F, 29.5F);
		outer_roof_right.addChild(outer_left_roof_4_r2);
		setRotationAngle(outer_left_roof_4_r2, 0.0F, 0.0F, 0.288F);
		outer_left_roof_4_r2.setTextureOffset(112, 160).addCuboid(-0.497F, -1.1228F, -12.5F, 6.0F, 0.0F, 69.0F, 0.0F, false);

		outer_left_roof_3_r2 = new ModelPart(this);
		outer_left_roof_3_r2.setPivot(9.678F, -37.0555F, 29.5F);
		outer_roof_right.addChild(outer_left_roof_3_r2);
		setRotationAngle(outer_left_roof_3_r2, 0.0F, 0.0F, 0.4014F);
		outer_left_roof_3_r2.setTextureOffset(195, 70).addCuboid(5.3154F, -1.7351F, -12.5F, 3.0F, 0.0F, 69.0F, 0.0F, false);

		outer_left_roof_2_r2 = new ModelPart(this);
		outer_left_roof_2_r2.setPivot(9.678F, -37.0555F, 29.5F);
		outer_roof_right.addChild(outer_left_roof_2_r2);
		setRotationAngle(outer_left_roof_2_r2, 0.0F, 0.0F, 0.9599F);
		outer_left_roof_2_r2.setTextureOffset(0, 234).addCuboid(6.1006F, -5.8625F, -12.5F, 2.0F, 0.0F, 69.0F, 0.0F, false);

		outer_left_roof_1_r2 = new ModelPart(this);
		outer_left_roof_1_r2.setPivot(9.678F, -37.0555F, 29.5F);
		outer_roof_right.addChild(outer_left_roof_1_r2);
		setRotationAngle(outer_left_roof_1_r2, 0.0F, 0.0F, 1.3788F);
		outer_left_roof_1_r2.setTextureOffset(270, 140).addCuboid(4.9967F, -8.6386F, -12.5F, 1.0F, 0.0F, 69.0F, 0.0F, false);

		outer_roof_left = new ModelPart(this);
		outer_roof_left.setPivot(0.1F, -2.7F, -35.0F);
		roof_end_exterior.addChild(outer_roof_left);
		outer_roof_left.setTextureOffset(82, 230).addCuboid(-3.1F, -38.675F, 17.0F, 3.0F, 0.0F, 69.0F, 0.0F, false);

		outer_left_roof_5_r3 = new ModelPart(this);
		outer_left_roof_5_r3.setPivot(-9.078F, -37.1555F, 29.5F);
		outer_roof_left.addChild(outer_left_roof_5_r3);
		setRotationAngle(outer_left_roof_5_r3, 0.0F, 0.0F, -0.0698F);
		outer_left_roof_5_r3.setTextureOffset(127, 0).addCuboid(-0.7355F, -1.1097F, -12.5F, 7.0F, 0.0F, 69.0F, 0.0F, false);

		outer_left_roof_4_r3 = new ModelPart(this);
		outer_left_roof_4_r3.setPivot(-9.678F, -37.0555F, 29.5F);
		outer_roof_left.addChild(outer_left_roof_4_r3);
		setRotationAngle(outer_left_roof_4_r3, 0.0F, 0.0F, -0.288F);
		outer_left_roof_4_r3.setTextureOffset(0, 164).addCuboid(-5.503F, -1.1228F, -12.5F, 6.0F, 0.0F, 69.0F, 0.0F, false);

		outer_left_roof_3_r3 = new ModelPart(this);
		outer_left_roof_3_r3.setPivot(-9.678F, -37.0555F, 29.5F);
		outer_roof_left.addChild(outer_left_roof_3_r3);
		setRotationAngle(outer_left_roof_3_r3, 0.0F, 0.0F, -0.4014F);
		outer_left_roof_3_r3.setTextureOffset(158, 231).addCuboid(-8.3154F, -1.7351F, -12.5F, 3.0F, 0.0F, 69.0F, 0.0F, false);

		outer_left_roof_2_r3 = new ModelPart(this);
		outer_left_roof_2_r3.setPivot(-9.678F, -37.0555F, 29.5F);
		outer_roof_left.addChild(outer_left_roof_2_r3);
		setRotationAngle(outer_left_roof_2_r3, 0.0F, 0.0F, -0.9599F);
		outer_left_roof_2_r3.setTextureOffset(234, 232).addCuboid(-8.1006F, -5.8625F, -12.5F, 2.0F, 0.0F, 69.0F, 0.0F, false);

		outer_left_roof_1_r3 = new ModelPart(this);
		outer_left_roof_1_r3.setPivot(-9.678F, -37.0555F, 29.5F);
		outer_roof_left.addChild(outer_left_roof_1_r3);
		setRotationAngle(outer_left_roof_1_r3, 0.0F, 0.0F, -1.3788F);
		outer_left_roof_1_r3.setTextureOffset(271, 1).addCuboid(-5.9967F, -8.6386F, -12.5F, 1.0F, 0.0F, 69.0F, 0.0F, false);

		roof_door_light = new ModelPart(this);
		roof_door_light.setPivot(0.0F, 24.0F, 0.0F);
		roof_door_light.setTextureOffset(474, 52).addCuboid(-12.0F, -34.8F, -23.075F, 1.0F, 0.0F, 46.0F, 0.0F, false);

		door_light_2_r1 = new ModelPart(this);
		door_light_2_r1.setPivot(-12.95F, -38.85F, -0.075F);
		roof_door_light.addChild(door_light_2_r1);
		setRotationAngle(door_light_2_r1, 0.0F, 0.0F, 0.6807F);
		door_light_2_r1.setTextureOffset(475, 207).addCuboid(2.3F, 2.575F, -23.0F, 1.0F, 0.0F, 46.0F, 0.0F, false);

		door_light_1_r1 = new ModelPart(this);
		door_light_1_r1.setPivot(-12.8F, -38.7F, -0.075F);
		roof_door_light.addChild(door_light_1_r1);
		setRotationAngle(door_light_1_r1, 0.0F, 0.0F, 1.5708F);
		door_light_1_r1.setTextureOffset(474, 0).addCuboid(1.3F, 0.0F, -23.0F, 2.0F, 0.0F, 46.0F, 0.0F, false);

		roof_window_light = new ModelPart(this);
		roof_window_light.setPivot(0.0F, 24.0F, 0.0F);
		roof_window_light.setTextureOffset(317, 455).addCuboid(-12.0F, -34.8F, -24.0F, 1.0F, 0.0F, 48.0F, 0.0F, false);

		window_light_2_r1 = new ModelPart(this);
		window_light_2_r1.setPivot(-12.95F, -38.85F, 0.0F);
		roof_window_light.addChild(window_light_2_r1);
		setRotationAngle(window_light_2_r1, 0.0F, 0.0F, 0.6807F);
		window_light_2_r1.setTextureOffset(230, 456).addCuboid(2.3F, 2.575F, -24.0F, 1.0F, 0.0F, 48.0F, 0.0F, false);

		window_light_1_r1 = new ModelPart(this);
		window_light_1_r1.setPivot(-12.8F, -38.7F, 0.0F);
		roof_window_light.addChild(window_light_1_r1);
		setRotationAngle(window_light_1_r1, 0.0F, 0.0F, 1.5708F);
		window_light_1_r1.setTextureOffset(450, 278).addCuboid(1.3F, 0.0F, -24.0F, 2.0F, 0.0F, 48.0F, 0.0F, false);

		roof_end_light = new ModelPart(this);
		roof_end_light.setPivot(0.0F, 24.0F, 0.0F);
		roof_end_light.setTextureOffset(0, 147).addCuboid(-10.05F, -35.875F, -8.1F, 20.0F, 1.0F, 3.0F, 0.0F, false);

		end_light_roof_right = new ModelPart(this);
		end_light_roof_right.setPivot(1.0F, -3.0F, -1.0F);
		roof_end_light.addChild(end_light_roof_right);
		end_light_roof_right.setTextureOffset(122, 428).addCuboid(10.0F, -31.8F, -4.0F, 1.0F, 0.0F, 50.0F, 0.0F, false);

		inner_light_2_r1 = new ModelPart(this);
		inner_light_2_r1.setPivot(11.95F, -35.85F, 0.0F);
		end_light_roof_right.addChild(inner_light_2_r1);
		setRotationAngle(inner_light_2_r1, 0.0F, 0.0F, -0.6807F);
		inner_light_2_r1.setTextureOffset(59, 429).addCuboid(-3.3F, 2.575F, -4.0F, 1.0F, 0.0F, 50.0F, 0.0F, false);

		inner_light_1_r1 = new ModelPart(this);
		inner_light_1_r1.setPivot(11.8F, -35.7F, 0.0F);
		end_light_roof_right.addChild(inner_light_1_r1);
		setRotationAngle(inner_light_1_r1, 0.0F, 0.0F, -1.5708F);
		inner_light_1_r1.setTextureOffset(379, 406).addCuboid(-3.3F, 0.0F, -4.0F, 2.0F, 0.0F, 50.0F, 0.0F, false);

		end_light_roof_left = new ModelPart(this);
		end_light_roof_left.setPivot(-1.0F, -3.0F, -1.0F);
		roof_end_light.addChild(end_light_roof_left);
		end_light_roof_left.setTextureOffset(175, 429).addCuboid(-11.0F, -31.8F, -4.0F, 1.0F, 0.0F, 50.0F, 0.0F, false);

		inner_light_2_r2 = new ModelPart(this);
		inner_light_2_r2.setPivot(-11.95F, -35.85F, 0.0F);
		end_light_roof_left.addChild(inner_light_2_r2);
		setRotationAngle(inner_light_2_r2, 0.0F, 0.0F, 0.6807F);
		inner_light_2_r2.setTextureOffset(432, 156).addCuboid(2.3F, 2.575F, -4.0F, 1.0F, 0.0F, 50.0F, 0.0F, false);

		inner_light_1_r2 = new ModelPart(this);
		inner_light_1_r2.setPivot(-11.8F, -35.7F, 0.0F);
		end_light_roof_left.addChild(inner_light_1_r2);
		setRotationAngle(inner_light_1_r2, 0.0F, 0.0F, 1.5708F);
		inner_light_1_r2.setTextureOffset(419, 1).addCuboid(1.3F, 0.0F, -4.0F, 2.0F, 0.0F, 50.0F, 0.0F, false);

		roof_head_light = new ModelPart(this);
		roof_head_light.setPivot(0.0F, 24.0F, 0.0F);
		

		head_light_roof_right = new ModelPart(this);
		head_light_roof_right.setPivot(1.0F, -3.0F, 8.0F);
		roof_head_light.addChild(head_light_roof_right);
		head_light_roof_right.setTextureOffset(472, 499).addCuboid(10.0F, -31.8F, -6.0F, 1.0F, 0.0F, 40.0F, 0.0F, false);

		inner_light_2_r3 = new ModelPart(this);
		inner_light_2_r3.setPivot(11.95F, -35.875F, 0.0F);
		head_light_roof_right.addChild(inner_light_2_r3);
		setRotationAngle(inner_light_2_r3, 0.0F, 0.0F, -0.6807F);
		inner_light_2_r3.setTextureOffset(281, 456).addCuboid(-3.3F, 2.575F, -6.0F, 1.0F, 0.0F, 40.0F, 0.0F, false);

		inner_light_1_r3 = new ModelPart(this);
		inner_light_1_r3.setPivot(11.8F, -35.7F, 0.0F);
		head_light_roof_right.addChild(inner_light_1_r3);
		setRotationAngle(inner_light_1_r3, 0.0F, 0.0F, -1.5708F);
		inner_light_1_r3.setTextureOffset(0, 41).addCuboid(-3.3F, 0.0F, -6.0F, 2.0F, 0.0F, 40.0F, 0.0F, false);

		head_light_roof_left = new ModelPart(this);
		head_light_roof_left.setPivot(0.0F, -3.0F, 8.0F);
		roof_head_light.addChild(head_light_roof_left);
		head_light_roof_left.setTextureOffset(222, 505).addCuboid(-12.0F, -31.8F, -6.0F, 1.0F, 0.0F, 40.0F, 0.0F, false);

		inner_light_2_r4 = new ModelPart(this);
		inner_light_2_r4.setPivot(-12.95F, -35.875F, 0.0F);
		head_light_roof_left.addChild(inner_light_2_r4);
		setRotationAngle(inner_light_2_r4, 0.0F, 0.0F, 0.6807F);
		inner_light_2_r4.setTextureOffset(332, 506).addCuboid(2.3F, 2.575F, -6.0F, 1.0F, 0.0F, 40.0F, 0.0F, false);

		inner_light_1_r4 = new ModelPart(this);
		inner_light_1_r4.setPivot(-12.8F, -35.7F, 0.0F);
		head_light_roof_left.addChild(inner_light_1_r4);
		setRotationAngle(inner_light_1_r4, 0.0F, 0.0F, 1.5708F);
		inner_light_1_r4.setTextureOffset(499, 327).addCuboid(1.3F, 0.0F, -6.0F, 2.0F, 0.0F, 40.0F, 0.0F, false);

		roof_handle = new ModelPart(this);
		roof_handle.setPivot(0.0F, 24.0F, 0.0F);
		

		roof_handrail_curve = new ModelPart(this);
		roof_handrail_curve.setPivot(0.2343F, -40.1584F, 7.6535F);
		roof_handle.addChild(roof_handrail_curve);
		setRotationAngle(roof_handrail_curve, 1.5708F, 0.0F, 1.5708F);
		

		roof_handrail_curve_22_r1 = new ModelPart(this);
		roof_handrail_curve_22_r1.setPivot(4.7407F, -21.7138F, 3.0172F);
		roof_handrail_curve.addChild(roof_handrail_curve_22_r1);
		setRotationAngle(roof_handrail_curve_22_r1, -0.1745F, 0.0F, 0.0F);
		roof_handrail_curve_22_r1.setTextureOffset(45, 29).addCuboid(0.0F, -14.2435F, -5.903F, 0.0F, 0.0F, 1.0F, 0.2F, false);

		roof_handrail_curve_21_r1 = new ModelPart(this);
		roof_handrail_curve_21_r1.setPivot(4.7407F, 96.0306F, -0.8742F);
		roof_handrail_curve.addChild(roof_handrail_curve_21_r1);
		setRotationAngle(roof_handrail_curve_21_r1, -1.4835F, 0.0F, 0.0F);
		roof_handrail_curve_21_r1.setTextureOffset(45, 4).addCuboid(0.0F, -3.7108F, -78.7714F, 0.0F, 0.0F, 1.0F, 0.2F, false);

		roof_handrail_curve_20_r1 = new ModelPart(this);
		roof_handrail_curve_20_r1.setPivot(4.7407F, 96.0306F, -0.8742F);
		roof_handrail_curve.addChild(roof_handrail_curve_20_r1);
		setRotationAngle(roof_handrail_curve_20_r1, -1.1781F, 0.0F, 0.0F);
		roof_handrail_curve_20_r1.setTextureOffset(45, 2).addCuboid(0.0F, -26.9032F, -72.8326F, 0.0F, 0.0F, 1.0F, 0.2F, false);

		roof_handrail_curve_19_r1 = new ModelPart(this);
		roof_handrail_curve_19_r1.setPivot(4.7407F, 96.0306F, -0.8742F);
		roof_handrail_curve.addChild(roof_handrail_curve_19_r1);
		setRotationAngle(roof_handrail_curve_19_r1, -0.8727F, 0.0F, 0.0F);
		roof_handrail_curve_19_r1.setTextureOffset(45, 0).addCuboid(0.0F, -47.1737F, -60.0257F, 0.0F, 0.0F, 1.0F, 0.2F, false);

		roof_handrail_curve_18_r1 = new ModelPart(this);
		roof_handrail_curve_18_r1.setPivot(4.7407F, 96.0306F, -0.8742F);
		roof_handrail_curve.addChild(roof_handrail_curve_18_r1);
		setRotationAngle(roof_handrail_curve_18_r1, -0.6545F, 0.0F, 0.0F);
		roof_handrail_curve_18_r1.setTextureOffset(23, 44).addCuboid(0.0F, -58.8199F, -47.2581F, 0.0F, 0.0F, 1.0F, 0.2F, false);

		roof_handrail_curve_17_r1 = new ModelPart(this);
		roof_handrail_curve_17_r1.setPivot(4.7407F, 96.0306F, -0.8742F);
		roof_handrail_curve.addChild(roof_handrail_curve_17_r1);
		setRotationAngle(roof_handrail_curve_17_r1, -0.1745F, 0.0F, 0.0F);
		roof_handrail_curve_17_r1.setTextureOffset(23, 41).addCuboid(0.0F, -73.4743F, -13.6103F, 0.0F, 0.0F, 1.0F, 0.2F, false);

		roof_handrail_curve_16_r1 = new ModelPart(this);
		roof_handrail_curve_16_r1.setPivot(4.7407F, -21.7138F, -0.8742F);
		roof_handrail_curve.addChild(roof_handrail_curve_16_r1);
		setRotationAngle(roof_handrail_curve_16_r1, 0.0F, 0.0F, -1.5708F);
		roof_handrail_curve_16_r1.setTextureOffset(127, 70).addCuboid(-38.5222F, 0.0F, -3.1793F, 49.0F, 0.0F, 0.0F, 0.2F, false);
		roof_handrail_curve_16_r1.setTextureOffset(127, 71).addCuboid(-38.5222F, 0.0F, 4.0707F, 49.0F, 0.0F, 0.0F, 0.2F, false);
		roof_handrail_curve_16_r1.setTextureOffset(5, 3).addCuboid(-38.5222F, -2.0F, -3.1293F, 0.0F, 2.0F, 0.0F, 0.2F, false);
		roof_handrail_curve_16_r1.setTextureOffset(5, 0).addCuboid(-38.5222F, -2.0F, 4.0707F, 0.0F, 2.0F, 0.0F, 0.2F, false);
		roof_handrail_curve_16_r1.setTextureOffset(14, 12).addCuboid(-23.5222F, -2.0F, -3.1293F, 0.0F, 2.0F, 0.0F, 0.2F, false);
		roof_handrail_curve_16_r1.setTextureOffset(14, 9).addCuboid(-23.5222F, -2.0F, 4.0707F, 0.0F, 2.0F, 0.0F, 0.2F, false);
		roof_handrail_curve_16_r1.setTextureOffset(25, 3).addCuboid(-5.5222F, -2.0F, -3.1293F, 0.0F, 2.0F, 0.0F, 0.2F, false);
		roof_handrail_curve_16_r1.setTextureOffset(25, 0).addCuboid(-5.5222F, -2.0F, 4.0707F, 0.0F, 2.0F, 0.0F, 0.2F, false);
		roof_handrail_curve_16_r1.setTextureOffset(5, 45).addCuboid(10.4778F, -2.0F, -3.1293F, 0.0F, 2.0F, 0.0F, 0.2F, false);
		roof_handrail_curve_16_r1.setTextureOffset(6, 45).addCuboid(10.4778F, -2.0F, 4.0707F, 0.0F, 2.0F, 0.0F, 0.2F, false);

		roof_handrail_curve_15_r1 = new ModelPart(this);
		roof_handrail_curve_15_r1.setPivot(4.7407F, -21.7138F, -0.8742F);
		roof_handrail_curve.addChild(roof_handrail_curve_15_r1);
		setRotationAngle(roof_handrail_curve_15_r1, 1.4835F, 0.0F, 0.0F);
		roof_handrail_curve_15_r1.setTextureOffset(12, 41).addCuboid(0.0F, -4.0984F, 10.4876F, 0.0F, 0.0F, 1.0F, 0.2F, false);

		roof_handrail_curve_14_r1 = new ModelPart(this);
		roof_handrail_curve_14_r1.setPivot(4.7407F, -21.7138F, -0.8742F);
		roof_handrail_curve.addChild(roof_handrail_curve_14_r1);
		setRotationAngle(roof_handrail_curve_14_r1, 1.1781F, 0.0F, 0.0F);
		roof_handrail_curve_14_r1.setTextureOffset(8, 41).addCuboid(0.0F, -7.3852F, 9.947F, 0.0F, 0.0F, 1.0F, 0.2F, false);

		roof_handrail_curve_13_r1 = new ModelPart(this);
		roof_handrail_curve_13_r1.setPivot(4.7407F, -21.7138F, -0.8742F);
		roof_handrail_curve.addChild(roof_handrail_curve_13_r1);
		setRotationAngle(roof_handrail_curve_13_r1, 0.8727F, 0.0F, 0.0F);
		roof_handrail_curve_13_r1.setTextureOffset(4, 41).addCuboid(0.0F, -10.42F, 8.6119F, 0.0F, 0.0F, 1.0F, 0.2F, false);

		roof_handrail_curve_12_r1 = new ModelPart(this);
		roof_handrail_curve_12_r1.setPivot(4.7407F, -21.7138F, -0.8742F);
		roof_handrail_curve.addChild(roof_handrail_curve_12_r1);
		setRotationAngle(roof_handrail_curve_12_r1, 0.6545F, 0.0F, 0.0F);
		roof_handrail_curve_12_r1.setTextureOffset(0, 41).addCuboid(0.0F, -12.2646F, 7.2869F, 0.0F, 0.0F, 1.0F, 0.2F, false);

		roof_handrail_curve_11_r1 = new ModelPart(this);
		roof_handrail_curve_11_r1.setPivot(4.7407F, -21.7138F, -0.8742F);
		roof_handrail_curve.addChild(roof_handrail_curve_11_r1);
		setRotationAngle(roof_handrail_curve_11_r1, 0.1745F, 0.0F, 0.0F);
		roof_handrail_curve_11_r1.setTextureOffset(36, 35).addCuboid(0.0F, -14.7644F, 1.9486F, 0.0F, 0.0F, 1.0F, 0.2F, false);

		roof_handrail_curve_10_r1 = new ModelPart(this);
		roof_handrail_curve_10_r1.setPivot(4.7407F, 96.0306F, 3.0172F);
		roof_handrail_curve.addChild(roof_handrail_curve_10_r1);
		setRotationAngle(roof_handrail_curve_10_r1, 1.4835F, 0.0F, 0.0F);
		roof_handrail_curve_10_r1.setTextureOffset(45, 27).addCuboid(0.0F, -6.6993F, 77.51F, 0.0F, 0.0F, 1.0F, 0.2F, false);

		roof_handrail_curve_9_r1 = new ModelPart(this);
		roof_handrail_curve_9_r1.setPivot(4.7407F, 96.0306F, 3.0172F);
		roof_handrail_curve.addChild(roof_handrail_curve_9_r1);
		setRotationAngle(roof_handrail_curve_9_r1, 1.1781F, 0.0F, 0.0F);
		roof_handrail_curve_9_r1.setTextureOffset(45, 25).addCuboid(0.0F, -29.6748F, 70.6845F, 0.0F, 0.0F, 1.0F, 0.2F, false);

		roof_handrail_curve_8_r1 = new ModelPart(this);
		roof_handrail_curve_8_r1.setPivot(4.7407F, 96.0306F, 3.0172F);
		roof_handrail_curve.addChild(roof_handrail_curve_8_r1);
		setRotationAngle(roof_handrail_curve_8_r1, 0.8727F, 0.0F, 0.0F);
		roof_handrail_curve_8_r1.setTextureOffset(45, 14).addCuboid(0.0F, -49.4719F, 57.0973F, 0.0F, 0.0F, 1.0F, 0.2F, false);

		roof_handrail_curve_7_r1 = new ModelPart(this);
		roof_handrail_curve_7_r1.setPivot(4.7407F, 96.0306F, 3.0172F);
		roof_handrail_curve.addChild(roof_handrail_curve_7_r1);
		setRotationAngle(roof_handrail_curve_7_r1, 0.6545F, 0.0F, 0.0F);
		roof_handrail_curve_7_r1.setTextureOffset(45, 12).addCuboid(0.0F, -60.6461F, 43.8781F, 0.0F, 0.0F, 1.0F, 0.2F, false);

		roof_handrail_curve_6_r1 = new ModelPart(this);
		roof_handrail_curve_6_r1.setPivot(4.7407F, 96.0306F, 3.0172F);
		roof_handrail_curve.addChild(roof_handrail_curve_6_r1);
		setRotationAngle(roof_handrail_curve_6_r1, 0.1745F, 0.0F, 0.0F);
		roof_handrail_curve_6_r1.setTextureOffset(45, 10).addCuboid(0.0F, -73.9953F, 9.6558F, 0.0F, 0.0F, 1.0F, 0.2F, false);

		roof_handrail_curve_4_r1 = new ModelPart(this);
		roof_handrail_curve_4_r1.setPivot(4.7407F, -21.7138F, 3.0172F);
		roof_handrail_curve.addChild(roof_handrail_curve_4_r1);
		setRotationAngle(roof_handrail_curve_4_r1, -1.4835F, 0.0F, 0.0F);
		roof_handrail_curve_4_r1.setTextureOffset(33, 45).addCuboid(0.0F, -1.1098F, -11.7491F, 0.0F, 0.0F, 1.0F, 0.2F, false);

		roof_handrail_curve_3_r1 = new ModelPart(this);
		roof_handrail_curve_3_r1.setPivot(4.7407F, -21.7138F, 3.0172F);
		roof_handrail_curve.addChild(roof_handrail_curve_3_r1);
		setRotationAngle(roof_handrail_curve_3_r1, -1.1781F, 0.0F, 0.0F);
		roof_handrail_curve_3_r1.setTextureOffset(36, 45).addCuboid(0.0F, -4.6136F, -12.0951F, 0.0F, 0.0F, 1.0F, 0.2F, false);

		roof_handrail_curve_2_r1 = new ModelPart(this);
		roof_handrail_curve_2_r1.setPivot(4.7407F, -21.7138F, 3.0172F);
		roof_handrail_curve.addChild(roof_handrail_curve_2_r1);
		setRotationAngle(roof_handrail_curve_2_r1, -0.8727F, 0.0F, 0.0F);
		roof_handrail_curve_2_r1.setTextureOffset(45, 41).addCuboid(0.0F, -8.1219F, -11.5403F, 0.0F, 0.0F, 1.0F, 0.2F, false);

		roof_handrail_curve_1_r1 = new ModelPart(this);
		roof_handrail_curve_1_r1.setPivot(4.7407F, -21.7138F, 3.0172F);
		roof_handrail_curve.addChild(roof_handrail_curve_1_r1);
		setRotationAngle(roof_handrail_curve_1_r1, -0.6545F, 0.0F, 0.0F);
		roof_handrail_curve_1_r1.setTextureOffset(45, 43).addCuboid(0.0F, -10.4383F, -10.6669F, 0.0F, 0.0F, 1.0F, 0.2F, false);

		head = new ModelPart(this);
		head.setPivot(0.0F, 24.0F, 0.0F);
		head.setTextureOffset(194, 199).addCuboid(19.3F, -13.0F, 2.0F, 2.0F, 15.0F, 9.0F, 0.0F, false);
		head.setTextureOffset(195, 107).addCuboid(-21.3F, -13.0F, 2.0F, 2.0F, 15.0F, 9.0F, 0.0F, false);
		head.setTextureOffset(0, 385).addCuboid(-19.5F, -37.0F, 2.1F, 13.0F, 37.0F, 0.0F, 0.0F, false);
		head.setTextureOffset(343, 0).addCuboid(6.5F, -37.0F, 2.1F, 13.0F, 37.0F, 0.0F, 0.0F, false);
		head.setTextureOffset(0, 118).addCuboid(-6.5F, -37.0F, 2.1F, 13.0F, 3.0F, 0.0F, 0.0F, false);
		head.setTextureOffset(0, 304).addCuboid(-21.0F, 0.0F, -20.0F, 42.0F, 1.0F, 28.0F, 0.0F, false);

		front_cab_left_1_r1 = new ModelPart(this);
		front_cab_left_1_r1.setPivot(-19.6F, -10.875F, -26.0F);
		head.addChild(front_cab_left_1_r1);
		setRotationAngle(front_cab_left_1_r1, 0.0F, 0.0F, 0.1047F);
		front_cab_left_1_r1.setTextureOffset(145, 90).addCuboid(-1.925F, -25.0F, 28.0F, 2.0F, 23.0F, 9.0F, 0.0F, false);

		front_cab_right_2_r1 = new ModelPart(this);
		front_cab_right_2_r1.setPivot(19.6F, -10.875F, -26.0F);
		head.addChild(front_cab_right_2_r1);
		setRotationAngle(front_cab_right_2_r1, 0.0F, 0.0F, -0.1047F);
		front_cab_right_2_r1.setTextureOffset(161, 0).addCuboid(-0.075F, -25.0F, 28.0F, 2.0F, 23.0F, 9.0F, 0.0F, false);

		cab_door_back = new ModelPart(this);
		cab_door_back.setPivot(-6.5F, 0.0F, -2.5F);
		head.addChild(cab_door_back);
		cab_door_back.setTextureOffset(379, 406).addCuboid(0.0F, -34.0F, 4.6F, 13.0F, 34.0F, 0.0F, 0.0F, false);
		cab_door_back.setTextureOffset(33, 234).addCuboid(25.3F, -13.0F, 11.5F, 1.0F, 13.0F, 2.0F, 0.0F, false);
		cab_door_back.setTextureOffset(26, 234).addCuboid(-13.3F, -13.0F, 11.5F, 1.0F, 13.0F, 2.0F, 0.0F, false);

		end_wall_4_r1 = new ModelPart(this);
		end_wall_4_r1.setPivot(-12.6F, -10.875F, -45.6F);
		cab_door_back.addChild(end_wall_4_r1);
		setRotationAngle(end_wall_4_r1, 0.0F, 0.0F, 0.1047F);
		end_wall_4_r1.setTextureOffset(53, 164).addCuboid(-0.925F, -23.0F, 57.1F, 1.0F, 21.0F, 2.0F, 0.0F, false);

		end_wall_2_r1 = new ModelPart(this);
		end_wall_2_r1.setPivot(25.6F, -10.875F, -45.6F);
		cab_door_back.addChild(end_wall_2_r1);
		setRotationAngle(end_wall_2_r1, 0.0F, 0.0F, -0.1047F);
		end_wall_2_r1.setTextureOffset(60, 164).addCuboid(-0.075F, -23.0F, 57.1F, 1.0F, 21.0F, 2.0F, 0.0F, false);

		head_exterior = new ModelPart(this);
		head_exterior.setPivot(0.0F, 24.0F, 0.0F);
		

		bottom = new ModelPart(this);
		bottom.setPivot(0.0F, 0.1F, -3.0F);
		head_exterior.addChild(bottom);
		bottom.setTextureOffset(127, 78).addCuboid(-21.5F, 0.0F, -18.0F, 43.0F, 3.0F, 2.0F, 0.0F, false);

		buttom_panel_left_9_r2 = new ModelPart(this);
		buttom_panel_left_9_r2.setPivot(-15.625F, 4.0F, -13.075F);
		bottom.addChild(buttom_panel_left_9_r2);
		setRotationAngle(buttom_panel_left_9_r2, 0.0F, -0.0436F, 0.0F);
		buttom_panel_left_9_r2.setTextureOffset(158, 230).addCuboid(15.0F, -4.0F, -12.5F, 3.0F, 3.0F, 7.0F, 0.0F, false);

		buttom_panel_left_8_r2 = new ModelPart(this);
		buttom_panel_left_8_r2.setPivot(-13.2F, 4.0F, -12.925F);
		bottom.addChild(buttom_panel_left_8_r2);
		setRotationAngle(buttom_panel_left_8_r2, 0.0F, -0.1309F, 0.0F);
		buttom_panel_left_8_r2.setTextureOffset(229, 171).addCuboid(14.3F, -4.0F, -13.875F, 3.0F, 3.0F, 7.0F, 0.0F, false);

		buttom_panel_left_7_r2 = new ModelPart(this);
		buttom_panel_left_7_r2.setPivot(-11.625F, 4.0F, -15.45F);
		bottom.addChild(buttom_panel_left_7_r2);
		setRotationAngle(buttom_panel_left_7_r2, 0.0F, -0.2182F, 0.0F);
		buttom_panel_left_7_r2.setTextureOffset(229, 182).addCuboid(15.0F, -4.0F, -12.5F, 3.0F, 3.0F, 7.0F, 0.0F, false);

		buttom_panel_left_6_r2 = new ModelPart(this);
		buttom_panel_left_6_r2.setPivot(-9.3F, 4.0F, -14.875F);
		bottom.addChild(buttom_panel_left_6_r2);
		setRotationAngle(buttom_panel_left_6_r2, 0.0F, -0.3054F, 0.0F);
		buttom_panel_left_6_r2.setTextureOffset(20, 0).addCuboid(14.3F, -4.0F, -13.875F, 3.0F, 3.0F, 6.0F, 0.0F, false);

		buttom_panel_left_5_r2 = new ModelPart(this);
		buttom_panel_left_5_r2.setPivot(-7.125F, 4.0F, -16.35F);
		bottom.addChild(buttom_panel_left_5_r2);
		setRotationAngle(buttom_panel_left_5_r2, 0.0F, -0.3491F, 0.0F);
		buttom_panel_left_5_r2.setTextureOffset(20, 41).addCuboid(15.0F, -4.0F, -12.5F, 3.0F, 3.0F, 6.0F, 0.0F, false);

		buttom_panel_left_4_r2 = new ModelPart(this);
		buttom_panel_left_4_r2.setPivot(-5.125F, 4.0F, -15.55F);
		bottom.addChild(buttom_panel_left_4_r2);
		setRotationAngle(buttom_panel_left_4_r2, 0.0F, -0.4363F, 0.0F);
		buttom_panel_left_4_r2.setTextureOffset(45, 0).addCuboid(14.3F, -4.0F, -13.875F, 3.0F, 3.0F, 6.0F, 0.0F, false);

		buttom_panel_left_3_r2 = new ModelPart(this);
		buttom_panel_left_3_r2.setPivot(-3.125F, 4.0F, -16.875F);
		bottom.addChild(buttom_panel_left_3_r2);
		setRotationAngle(buttom_panel_left_3_r2, 0.0F, -0.48F, 0.0F);
		buttom_panel_left_3_r2.setTextureOffset(45, 10).addCuboid(15.0F, -4.0F, -12.5F, 3.0F, 3.0F, 6.0F, 0.0F, false);

		buttom_panel_left_2_r2 = new ModelPart(this);
		buttom_panel_left_2_r2.setPivot(-1.125F, 4.0F, -15.625F);
		bottom.addChild(buttom_panel_left_2_r2);
		setRotationAngle(buttom_panel_left_2_r2, 0.0F, -0.5672F, 0.0F);
		buttom_panel_left_2_r2.setTextureOffset(45, 41).addCuboid(14.3F, -4.0F, -14.0F, 3.0F, 3.0F, 6.0F, 0.0F, false);

		buttom_panel_right_9_r2 = new ModelPart(this);
		buttom_panel_right_9_r2.setPivot(15.625F, 4.0F, -13.075F);
		bottom.addChild(buttom_panel_right_9_r2);
		setRotationAngle(buttom_panel_right_9_r2, 0.0F, 0.0436F, 0.0F);
		buttom_panel_right_9_r2.setTextureOffset(155, 164).addCuboid(-18.0F, -4.0F, -12.5F, 3.0F, 3.0F, 7.0F, 0.0F, false);

		buttom_panel_right_8_r2 = new ModelPart(this);
		buttom_panel_right_8_r2.setPivot(13.2F, 4.0F, -12.925F);
		bottom.addChild(buttom_panel_right_8_r2);
		setRotationAngle(buttom_panel_right_8_r2, 0.0F, 0.1309F, 0.0F);
		buttom_panel_right_8_r2.setTextureOffset(106, 164).addCuboid(-17.3F, -4.0F, -13.875F, 3.0F, 3.0F, 7.0F, 0.0F, false);

		buttom_panel_right_7_r2 = new ModelPart(this);
		buttom_panel_right_7_r2.setPivot(11.625F, 4.0F, -15.45F);
		bottom.addChild(buttom_panel_right_7_r2);
		setRotationAngle(buttom_panel_right_7_r2, 0.0F, 0.2182F, 0.0F);
		buttom_panel_right_7_r2.setTextureOffset(131, 164).addCuboid(-18.0F, -4.0F, -12.5F, 3.0F, 3.0F, 7.0F, 0.0F, false);

		buttom_panel_right_6_r2 = new ModelPart(this);
		buttom_panel_right_6_r2.setPivot(9.3F, 4.0F, -14.875F);
		bottom.addChild(buttom_panel_right_6_r2);
		setRotationAngle(buttom_panel_right_6_r2, 0.0F, 0.3054F, 0.0F);
		buttom_panel_right_6_r2.setTextureOffset(82, 164).addCuboid(-17.3F, -4.0F, -13.875F, 3.0F, 3.0F, 7.0F, 0.0F, false);

		buttom_panel_right_5_r2 = new ModelPart(this);
		buttom_panel_right_5_r2.setPivot(7.125F, 4.0F, -16.35F);
		bottom.addChild(buttom_panel_right_5_r2);
		setRotationAngle(buttom_panel_right_5_r2, 0.0F, 0.3491F, 0.0F);
		buttom_panel_right_5_r2.setTextureOffset(189, 231).addCuboid(-18.0F, -4.0F, -12.5F, 3.0F, 3.0F, 7.0F, 0.0F, false);

		buttom_panel_right_4_r2 = new ModelPart(this);
		buttom_panel_right_4_r2.setPivot(5.125F, 4.0F, -15.55F);
		bottom.addChild(buttom_panel_right_4_r2);
		setRotationAngle(buttom_panel_right_4_r2, 0.0F, 0.4363F, 0.0F);
		buttom_panel_right_4_r2.setTextureOffset(45, 51).addCuboid(-17.3F, -4.0F, -13.875F, 3.0F, 3.0F, 6.0F, 0.0F, false);

		buttom_panel_right_3_r2 = new ModelPart(this);
		buttom_panel_right_3_r2.setPivot(3.125F, 4.0F, -16.875F);
		bottom.addChild(buttom_panel_right_3_r2);
		setRotationAngle(buttom_panel_right_3_r2, 0.0F, 0.48F, 0.0F);
		buttom_panel_right_3_r2.setTextureOffset(66, 0).addCuboid(-18.0F, -4.0F, -12.5F, 3.0F, 3.0F, 6.0F, 0.0F, false);

		buttom_panel_right_2_r2 = new ModelPart(this);
		buttom_panel_right_2_r2.setPivot(1.125F, 4.0F, -15.625F);
		bottom.addChild(buttom_panel_right_2_r2);
		setRotationAngle(buttom_panel_right_2_r2, 0.0F, 0.5672F, 0.0F);
		buttom_panel_right_2_r2.setTextureOffset(66, 10).addCuboid(-17.3F, -4.0F, -14.0F, 3.0F, 3.0F, 6.0F, 0.0F, false);

		front = new ModelPart(this);
		front.setPivot(0.0F, 0.0F, -3.0F);
		head_exterior.addChild(front);
		front.setTextureOffset(165, 47).addCuboid(-7.2F, -42.0F, -22.025F, 14.0F, 9.0F, 0.0F, 0.0F, false);
		front.setTextureOffset(175, 428).addCuboid(6.05F, -33.0F, -22.275F, 0.0F, 35.0F, 6.0F, 0.0F, false);
		front.setTextureOffset(424, 289).addCuboid(-6.3F, -33.0F, -22.275F, 0.0F, 35.0F, 6.0F, 0.0F, false);
		front.setTextureOffset(45, 25).addCuboid(-6.975F, -33.0F, -22.025F, 14.0F, 0.0F, 6.0F, 0.0F, false);

		front_left_panel_4_r1 = new ModelPart(this);
		front_left_panel_4_r1.setPivot(-14.2F, 4.0F, -22.85F);
		front.addChild(front_left_panel_4_r1);
		setRotationAngle(front_left_panel_4_r1, 0.0F, -0.0873F, 0.0F);
		front_left_panel_4_r1.setTextureOffset(219, 231).addCuboid(20.25F, -46.0F, -1.0F, 2.0F, 44.0F, 0.0F, 0.0F, false);

		front_left_panel_3_r2 = new ModelPart(this);
		front_left_panel_3_r2.setPivot(-9.2F, 4.0F, -23.9F);
		front.addChild(front_left_panel_3_r2);
		setRotationAngle(front_left_panel_3_r2, 0.0F, -0.1745F, 0.0F);
		front_left_panel_3_r2.setTextureOffset(232, 372).addCuboid(17.0F, -46.0F, -1.0F, 5.0F, 44.0F, 0.0F, 0.0F, false);

		front_left_panel_2_r2 = new ModelPart(this);
		front_left_panel_2_r2.setPivot(-3.7F, 4.0F, -25.925F);
		front.addChild(front_left_panel_2_r2);
		setRotationAngle(front_left_panel_2_r2, 0.0F, -0.3491F, 0.0F);
		front_left_panel_2_r2.setTextureOffset(188, 428).addCuboid(17.0F, -46.0F, -1.0F, 5.0F, 42.0F, 0.0F, 0.0F, false);

		front_left_panel_1_r2 = new ModelPart(this);
		front_left_panel_1_r2.setPivot(1.5F, 4.0F, -26.4F);
		front.addChild(front_left_panel_1_r2);
		setRotationAngle(front_left_panel_1_r2, 0.0F, -0.48F, 0.0F);
		front_left_panel_1_r2.setTextureOffset(381, 0).addCuboid(17.0F, -46.0F, -1.0F, 5.0F, 44.0F, 0.0F, 0.0F, false);

		front_right_panel_4_r1 = new ModelPart(this);
		front_right_panel_4_r1.setPivot(14.2F, 4.0F, -22.85F);
		front.addChild(front_right_panel_4_r1);
		setRotationAngle(front_right_panel_4_r1, 0.0F, 0.0873F, 0.0F);
		front_right_panel_4_r1.setTextureOffset(60, 255).addCuboid(-22.5F, -46.0F, -1.0F, 2.0F, 44.0F, 0.0F, 0.0F, false);

		front_right_panel_3_r2 = new ModelPart(this);
		front_right_panel_3_r2.setPivot(9.2F, 4.0F, -23.9F);
		front.addChild(front_right_panel_3_r2);
		setRotationAngle(front_right_panel_3_r2, 0.0F, 0.1745F, 0.0F);
		front_right_panel_3_r2.setTextureOffset(406, 406).addCuboid(-22.0F, -46.0F, -1.0F, 5.0F, 44.0F, 0.0F, 0.0F, false);

		front_right_panel_2_r2 = new ModelPart(this);
		front_right_panel_2_r2.setPivot(3.7F, 4.0F, -25.925F);
		front.addChild(front_right_panel_2_r2);
		setRotationAngle(front_right_panel_2_r2, 0.0F, 0.3491F, 0.0F);
		front_right_panel_2_r2.setTextureOffset(27, 385).addCuboid(-22.0F, -46.0F, -1.0F, 5.0F, 44.0F, 0.0F, 0.0F, false);

		front_right_panel_1_r2 = new ModelPart(this);
		front_right_panel_1_r2.setPivot(-1.5F, 4.0F, -26.4F);
		front.addChild(front_right_panel_1_r2);
		setRotationAngle(front_right_panel_1_r2, 0.0F, 0.48F, 0.0F);
		front_right_panel_1_r2.setTextureOffset(417, 406).addCuboid(-22.0F, -46.0F, -1.0F, 5.0F, 44.0F, 0.0F, 0.0F, false);

		cab_door = new ModelPart(this);
		cab_door.setPivot(0.0F, -1.0F, 0.0F);
		front.addChild(cab_door);
		cab_door.setTextureOffset(194, 160).addCuboid(-6.5F, -32.0F, -16.9F, 13.0F, 33.0F, 0.0F, 0.0F, false);
		cab_door.setTextureOffset(0, 164).addCuboid(-8.0F, -25.0F, -22.4F, 16.0F, 18.0F, 0.0F, 0.0F, false);

		front_handle_exterior_1 = new ModelPart(this);
		front_handle_exterior_1.setPivot(7.825F, 3.45F, 13.1F);
		front.addChild(front_handle_exterior_1);
		setRotationAngle(front_handle_exterior_1, -3.1416F, 0.0F, 3.1416F);
		

		handle_1_r2 = new ModelPart(this);
		handle_1_r2.setPivot(-7.3F, -31.1F, -46.975F);
		front_handle_exterior_1.addChild(handle_1_r2);
		setRotationAngle(handle_1_r2, 0.0F, 0.0F, 1.5708F);
		handle_1_r2.setTextureOffset(127, 28).addCuboid(-4.75F, -22.3769F, 81.3736F, 7.0F, 0.0F, 2.0F, 0.1F, false);

		front_handle_exterior_2 = new ModelPart(this);
		front_handle_exterior_2.setPivot(7.825F, 21.45F, 13.1F);
		front.addChild(front_handle_exterior_2);
		setRotationAngle(front_handle_exterior_2, -3.1416F, 0.0F, 3.1416F);
		

		handle_2_r2 = new ModelPart(this);
		handle_2_r2.setPivot(-7.3F, -49.1F, -46.975F);
		front_handle_exterior_2.addChild(handle_2_r2);
		setRotationAngle(handle_2_r2, 0.0F, 0.0F, 1.5708F);
		handle_2_r2.setTextureOffset(111, 118).addCuboid(13.25F, -22.3769F, 81.3736F, 7.0F, 0.0F, 2.0F, 0.1F, false);

		front_handle_exterior_3 = new ModelPart(this);
		front_handle_exterior_3.setPivot(22.075F, 3.45F, 13.1F);
		front.addChild(front_handle_exterior_3);
		setRotationAngle(front_handle_exterior_3, -3.1416F, 0.0F, 3.1416F);
		

		handle_3_r2 = new ModelPart(this);
		handle_3_r2.setPivot(-21.55F, -31.1F, -46.975F);
		front_handle_exterior_3.addChild(handle_3_r2);
		setRotationAngle(handle_3_r2, 0.0F, 0.0F, 1.5708F);
		handle_3_r2.setTextureOffset(34, 118).addCuboid(-4.75F, -36.6269F, 81.3736F, 7.0F, 0.0F, 2.0F, 0.1F, false);

		front_handle_exterior_4 = new ModelPart(this);
		front_handle_exterior_4.setPivot(38.075F, 3.95F, 17.1F);
		front.addChild(front_handle_exterior_4);
		setRotationAngle(front_handle_exterior_4, -3.1416F, 0.0F, 3.0369F);
		front_handle_exterior_4.setTextureOffset(36, 23).addCuboid(15.3166F, -38.9798F, 35.2707F, 0.0F, 8.0F, 1.0F, 0.1F, false);

		front_handle_exterior_5 = new ModelPart(this);
		front_handle_exterior_5.setPivot(39.275F, 13.85F, 19.1F);
		front.addChild(front_handle_exterior_5);
		setRotationAngle(front_handle_exterior_5, 3.098F, 0.0F, 3.0369F);
		front_handle_exterior_5.setTextureOffset(36, 13).addCuboid(15.3162F, -38.0211F, 35.2462F, 0.0F, 8.0F, 1.0F, 0.1F, false);

		front_handle_exterior_6 = new ModelPart(this);
		front_handle_exterior_6.setPivot(-105.7F, 18.5F, -13.9F);
		front.addChild(front_handle_exterior_6);
		setRotationAngle(front_handle_exterior_6, -3.1416F, 0.0F, -3.0369F);
		front_handle_exterior_6.setTextureOffset(83, 46).addCuboid(-81.1F, -60.0F, 4.1F, 0.0F, 8.0F, 1.0F, 0.1F, false);

		front_handle_exterior_7 = new ModelPart(this);
		front_handle_exterior_7.setPivot(-28.1789F, -17.1496F, -14.8561F);
		front.addChild(front_handle_exterior_7);
		setRotationAngle(front_handle_exterior_7, -2.8362F, 0.0F, 1.5708F);
		front_handle_exterior_7.setTextureOffset(34, 90).addCuboid(2.0F, 11.1F, 0.2F, 3.0F, 8.0F, 2.0F, 0.0F, false);

		side_right = new ModelPart(this);
		side_right.setPivot(0.0F, 0.0F, 0.0F);
		head_exterior.addChild(side_right);
		side_right.setTextureOffset(79, 66).addCuboid(19.5F, -13.0F, 9.025F, 2.0F, 13.0F, 0.0F, 0.0F, false);
		side_right.setTextureOffset(158, 230).addCuboid(21.5F, 0.0F, -22.0F, 0.0F, 4.0F, 30.0F, 0.0F, false);
		side_right.setTextureOffset(0, 164).addCuboid(21.5F, -13.0F, -22.0F, 0.0F, 13.0F, 33.0F, 0.0F, false);
		side_right.setTextureOffset(0, 234).addCuboid(0.0F, -41.375F, -25.0F, 3.0F, 0.0F, 7.0F, 0.0F, false);

		outer_right_roof_5_r2 = new ModelPart(this);
		outer_right_roof_5_r2.setPivot(8.978F, -39.8555F, -12.5F);
		side_right.addChild(outer_right_roof_5_r2);
		setRotationAngle(outer_right_roof_5_r2, 0.0F, 0.0F, 0.0698F);
		outer_right_roof_5_r2.setTextureOffset(0, 183).addCuboid(-6.2645F, -1.1097F, -12.5F, 7.0F, 0.0F, 7.0F, 0.0F, false);

		outer_right_roof_4_r2 = new ModelPart(this);
		outer_right_roof_4_r2.setPivot(9.578F, -39.7555F, -12.5F);
		side_right.addChild(outer_right_roof_4_r2);
		setRotationAngle(outer_right_roof_4_r2, 0.0F, 0.0F, 0.288F);
		outer_right_roof_4_r2.setTextureOffset(211, 45).addCuboid(-0.497F, -1.1228F, -12.5F, 6.0F, 0.0F, 7.0F, 0.0F, false);

		outer_right_roof_3_r2 = new ModelPart(this);
		outer_right_roof_3_r2.setPivot(9.578F, -39.7555F, -12.5F);
		side_right.addChild(outer_right_roof_3_r2);
		setRotationAngle(outer_right_roof_3_r2, 0.0F, 0.0F, 0.4014F);
		outer_right_roof_3_r2.setTextureOffset(74, 234).addCuboid(5.3154F, -1.7351F, -12.5F, 3.0F, 0.0F, 7.0F, 0.0F, false);

		outer_right_roof_2_r2 = new ModelPart(this);
		outer_right_roof_2_r2.setPivot(9.578F, -39.7555F, -12.5F);
		side_right.addChild(outer_right_roof_2_r2);
		setRotationAngle(outer_right_roof_2_r2, 0.0F, 0.0F, 0.9599F);
		outer_right_roof_2_r2.setTextureOffset(0, 211).addCuboid(6.1006F, -5.8625F, -12.5F, 2.0F, 0.0F, 7.0F, 0.0F, false);

		outer_right_roof_1_r2 = new ModelPart(this);
		outer_right_roof_1_r2.setPivot(9.578F, -39.7555F, -12.5F);
		side_right.addChild(outer_right_roof_1_r2);
		setRotationAngle(outer_right_roof_1_r2, 0.0F, 0.0F, 1.3788F);
		outer_right_roof_1_r2.setTextureOffset(142, 0).addCuboid(4.9967F, -8.6386F, -12.5F, 1.0F, 0.0F, 7.0F, 0.0F, false);

		side_right_panel_3_r2 = new ModelPart(this);
		side_right_panel_3_r2.setPivot(23.55F, -10.875F, -5.1F);
		side_right.addChild(side_right_panel_3_r2);
		setRotationAngle(side_right_panel_3_r2, 0.0F, 0.0F, -0.1047F);
		side_right_panel_3_r2.setTextureOffset(111, 90).addCuboid(-1.85F, -25.0F, -16.9F, 0.0F, 23.0F, 33.0F, 0.0F, false);

		right_side_2_r2 = new ModelPart(this);
		right_side_2_r2.setPivot(20.95F, -9.875F, 61.125F);
		side_right.addChild(right_side_2_r2);
		setRotationAngle(right_side_2_r2, 0.0F, 0.0F, -0.1047F);
		right_side_2_r2.setTextureOffset(35, 51).addCuboid(-1.15F, -24.0F, -52.1F, 2.0F, 21.0F, 0.0F, 0.0F, false);

		side_left = new ModelPart(this);
		side_left.setPivot(0.0F, 0.0F, 0.0F);
		head_exterior.addChild(side_left);
		side_left.setTextureOffset(111, 90).addCuboid(-21.5F, -13.0F, 9.025F, 2.0F, 13.0F, 0.0F, 0.0F, false);
		side_left.setTextureOffset(195, 72).addCuboid(-21.5F, 0.0F, -22.0F, 0.0F, 4.0F, 30.0F, 0.0F, false);
		side_left.setTextureOffset(127, 0).addCuboid(-21.5F, -13.0F, -22.0F, 0.0F, 13.0F, 33.0F, 0.0F, false);
		side_left.setTextureOffset(231, 46).addCuboid(-3.0F, -41.375F, -25.0F, 3.0F, 0.0F, 7.0F, 0.0F, false);

		outer_left_roof_5_r4 = new ModelPart(this);
		outer_left_roof_5_r4.setPivot(-8.978F, -39.8555F, -12.5F);
		side_left.addChild(outer_left_roof_5_r4);
		setRotationAngle(outer_left_roof_5_r4, 0.0F, 0.0F, -0.0698F);
		outer_left_roof_5_r4.setTextureOffset(165, 57).addCuboid(-0.7355F, -1.1097F, -12.5F, 7.0F, 0.0F, 7.0F, 0.0F, false);

		outer_left_roof_4_r4 = new ModelPart(this);
		outer_left_roof_4_r4.setPivot(-9.578F, -39.7555F, -12.5F);
		side_left.addChild(outer_left_roof_4_r4);
		setRotationAngle(outer_left_roof_4_r4, 0.0F, 0.0F, -0.288F);
		outer_left_roof_4_r4.setTextureOffset(38, 211).addCuboid(-5.503F, -1.1228F, -12.5F, 6.0F, 0.0F, 7.0F, 0.0F, false);

		outer_left_roof_3_r4 = new ModelPart(this);
		outer_left_roof_3_r4.setPivot(-9.578F, -39.7555F, -12.5F);
		side_left.addChild(outer_left_roof_3_r4);
		setRotationAngle(outer_left_roof_3_r4, 0.0F, 0.0F, -0.4014F);
		outer_left_roof_3_r4.setTextureOffset(229, 146).addCuboid(-8.3154F, -1.7351F, -12.5F, 3.0F, 0.0F, 7.0F, 0.0F, false);

		outer_left_roof_2_r4 = new ModelPart(this);
		outer_left_roof_2_r4.setPivot(-9.578F, -39.7555F, -12.5F);
		side_left.addChild(outer_left_roof_2_r4);
		setRotationAngle(outer_left_roof_2_r4, 0.0F, 0.0F, -0.9599F);
		outer_left_roof_2_r4.setTextureOffset(127, 55).addCuboid(-8.1006F, -5.8625F, -12.5F, 2.0F, 0.0F, 7.0F, 0.0F, false);

		outer_left_roof_1_r4 = new ModelPart(this);
		outer_left_roof_1_r4.setPivot(-9.578F, -39.7555F, -12.5F);
		side_left.addChild(outer_left_roof_1_r4);
		setRotationAngle(outer_left_roof_1_r4, 0.0F, 0.0F, -1.3788F);
		outer_left_roof_1_r4.setTextureOffset(126, 90).addCuboid(-5.9967F, -8.6386F, -12.5F, 1.0F, 0.0F, 7.0F, 0.0F, false);

		side_left_panel_3_r2 = new ModelPart(this);
		side_left_panel_3_r2.setPivot(-23.55F, -10.875F, -5.1F);
		side_left.addChild(side_left_panel_3_r2);
		setRotationAngle(side_left_panel_3_r2, 0.0F, 0.0F, 0.1047F);
		side_left_panel_3_r2.setTextureOffset(0, 90).addCuboid(1.85F, -25.0F, -16.9F, 0.0F, 23.0F, 33.0F, 0.0F, false);

		left_side_2_r3 = new ModelPart(this);
		left_side_2_r3.setPivot(-20.95F, -9.875F, 61.125F);
		side_left.addChild(left_side_2_r3);
		setRotationAngle(left_side_2_r3, 0.0F, 0.0F, 0.1047F);
		left_side_2_r3.setTextureOffset(175, 90).addCuboid(-0.85F, -24.0F, -52.1F, 2.0F, 21.0F, 0.0F, 0.0F, false);

		bottom_panel = new ModelPart(this);
		bottom_panel.setPivot(0.0F, 0.0F, 0.0F);
		head_exterior.addChild(bottom_panel);
		bottom_panel.setTextureOffset(308, 261).addCuboid(-21.5F, 3.0F, -21.0F, 43.0F, 0.0F, 27.0F, 0.0F, false);

		headlights = new ModelPart(this);
		headlights.setPivot(0.0F, 24.0F, 0.0F);
		

		headlight_2_r1 = new ModelPart(this);
		headlight_2_r1.setPivot(-11.5F, -6.3978F, -24.4F);
		headlights.addChild(headlight_2_r1);
		setRotationAngle(headlight_2_r1, 0.0F, 0.1745F, 0.0F);
		headlight_2_r1.setTextureOffset(15, 98).addCuboid(-2.0F, -4.0F, 0.0F, 5.0F, 5.0F, 0.0F, 0.0F, false);

		headlight_1_r1 = new ModelPart(this);
		headlight_1_r1.setPivot(11.5F, -7.3978F, -24.4F);
		headlights.addChild(headlight_1_r1);
		setRotationAngle(headlight_1_r1, 0.0F, -0.1745F, 0.0F);
		headlight_1_r1.setTextureOffset(126, 98).addCuboid(-3.1F, -3.1F, 0.0F, 5.0F, 5.0F, 0.0F, 0.0F, false);

		tail_lights = new ModelPart(this);
		tail_lights.setPivot(0.0F, 24.0F, 0.0F);
		

		tail_light_2_r1 = new ModelPart(this);
		tail_light_2_r1.setPivot(-17.35F, -7.9978F, -22.8F);
		tail_lights.addChild(tail_light_2_r1);
		setRotationAngle(tail_light_2_r1, 0.0F, 0.3622F, 0.0F);
		tail_light_2_r1.setTextureOffset(0, 9).addCuboid(-1.25F, -3.75F, 0.2F, 5.0F, 5.0F, 0.0F, 0.0F, false);

		tail_light_1_r1 = new ModelPart(this);
		tail_light_1_r1.setPivot(17.35F, -8.9978F, -22.8F);
		tail_lights.addChild(tail_light_1_r1);
		setRotationAngle(tail_light_1_r1, 0.0F, -0.3622F, 0.0F);
		tail_light_1_r1.setTextureOffset(49, 98).addCuboid(-3.75F, -2.75F, 0.2F, 5.0F, 5.0F, 0.0F, 0.0F, false);

		door_light = new ModelPart(this);
		door_light.setPivot(0.0F, 24.0F, 0.0F);
		

		light_plate_1_r1 = new ModelPart(this);
		light_plate_1_r1.setPivot(-20.9294F, -27.1645F, 1.0F);
		door_light.addChild(light_plate_1_r1);
		setRotationAngle(light_plate_1_r1, 0.0F, 0.0F, 1.5708F);
		light_plate_1_r1.setTextureOffset(0, 45).addCuboid(-1.5F, -1.1F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);

		door_light_on = new ModelPart(this);
		door_light_on.setPivot(0.0F, 24.0F, 0.0F);
		

		light_r7 = new ModelPart(this);
		light_r7.setPivot(-20.9294F, -27.1645F, 0.0F);
		door_light_on.addChild(light_r7);
		setRotationAngle(light_r7, 0.0F, 0.0F, 1.5708F);
		light_r7.setTextureOffset(48, 12).addCuboid(-1.5F, -1.1F, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, false);

		door_light_off = new ModelPart(this);
		door_light_off.setPivot(0.0F, 24.0F, 0.0F);
		

		light_r8 = new ModelPart(this);
		light_r8.setPivot(-20.9294F, -27.1645F, 0.0F);
		door_light_off.addChild(light_r8);
		setRotationAngle(light_r8, 0.0F, 0.0F, 1.5708F);
		light_r8.setTextureOffset(48, 10).addCuboid(-1.5F, -1.1F, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, false);

		coupler_fence = new ModelPart(this);
		coupler_fence.setPivot(0.0F, 24.0F, 0.0F);
		coupler_fence.setTextureOffset(321, 213).addCuboid(-20.0F, -27.0F, -9.0F, 0.0F, 25.0F, 18.0F, 0.0F, false);

		front_display = new ModelPart(this);
		front_display.setPivot(0.0F, 24.0F, 0.0F);
		front_display.setTextureOffset(0, 90).addCuboid(-10.0F, -36.9F, -10.0F, 20.0F, 3.0F, 70.0F, 0.0F, false);
		front_display.setTextureOffset(45, 34).addCuboid(-7.0F, -36.9F, 60.1F, 14.0F, 3.0F, 0.0F, 0.0F, false);

		letter_display = new ModelPart(this);
		letter_display.setPivot(0.0F, 24.0F, 0.0F);
		letter_display.setTextureOffset(0, 49).addCuboid(-3.5F, -40.75F, -25.175F, 7.0F, 6.0F, 0.0F, 0.0F, false);

		end_display = new ModelPart(this);
		end_display.setPivot(0.0F, 24.0F, 0.0F);
		end_display.setTextureOffset(0, 0).addCuboid(-10.0F, -36.9F, -10.0F, 20.0F, 3.0F, 86.0F, 0.0F, false);
		end_display.setTextureOffset(45, 76).addCuboid(-7.0F, -36.9F, -10.1F, 14.0F, 3.0F, 0.0F, 0.0F, false);

		system_map = new ModelPart(this);
		system_map.setPivot(0.0F, 24.0F, 0.0F);
		

		display_map_r1 = new ModelPart(this);
		display_map_r1.setPivot(-8.775F, -34.3F, -25.0F);
		system_map.addChild(display_map_r1);
		setRotationAngle(display_map_r1, 0.0F, 0.0F, -0.7418F);
		display_map_r1.setTextureOffset(377, 227).addCuboid(-0.9F, 0.0F, 10.0F, 5.0F, 0.0F, 30.0F, 0.0F, false);
}
private static final int DOOR_MAX = 14;
		private static final ModelDoorOverlay MODEL_DOOR_OVERLAY = new ModelDoorOverlay(DOOR_MAX, 6.34F, "door_overlay_r179_alpha_6_left.png", "door_overlay_r179_alpha_6_right.png");

		@Override
		protected void renderWindowPositions(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, boolean isEnd1Head, boolean isEnd2Head) {
			switch (renderStage) {
				case LIGHTS:
					renderMirror(roof_window_light, matrices, vertices, light, position);
					break;
				case INTERIOR:
					renderMirror(window, matrices, vertices, light, position);
					if (renderDetails) {
						renderMirror(roof_window, matrices, vertices, light, position);
					}
					break;
				case INTERIOR_TRANSLUCENT:
					renderMirror(side_panel, matrices, vertices, light, position - 25.5F);
					renderMirror(side_panel, matrices, vertices, light, position + 25.5F);
					break;
				case EXTERIOR:
					renderMirror(window_exterior, matrices, vertices, light, position);
					renderMirror(roof_exterior, matrices, vertices, light, position);
					break;
			}
		}

		@Override
		protected void renderDoorPositions(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
			final boolean firstDoor = isIndex(0, position, getDoorPositions());
			final boolean notLastDoor = !firstDoor && !isIndex(-1, position, getDoorPositions());
			final boolean doorOpen = doorLeftZ > 0 || doorRightZ > 0;

			switch (renderStage) {
				case LIGHTS:
					if (notLastDoor) {
						renderMirror(roof_window_light, matrices, vertices, light, position);
					}
					if (firstDoor && doorOpen) {
						renderMirror(door_light_on, matrices, vertices, light, -138);
					}
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
						if (getDoorPositions().length > 2 && (position == getDoorPositions()[1] || position == getDoorPositions()[2])) {
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
					renderMirror(roof_exterior, matrices, vertices, light, position);
					if (firstDoor && renderDetails) {
						renderMirror(door_light, matrices, vertices, light, -138);
						if (!doorOpen) {
							renderMirror(door_light_off, matrices, vertices, light, -138);
						}
					}
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
					renderOnce(roof_end_exterior, matrices, vertices, light, position);
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
					renderOnceFlipped(useHeadlights ? headlights : tail_lights, matrices, vertices, light, position);
					break;
				case INTERIOR:
					renderOnceFlipped(head, matrices, vertices, light, position);
					if (renderDetails) {
						renderOnceFlipped(roof_end, matrices, vertices, light, position);
					}
					break;
				case EXTERIOR:
					renderOnceFlipped(head_exterior, matrices, vertices, light, position);
					renderOnceFlipped(roof_end_exterior, matrices, vertices, light, position);
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
					renderMirror(side_panel, matrices, vertices, light, position + 9.5F);
					break;
				case EXTERIOR:
					renderOnce(end_exterior, matrices, vertices, light, position);
					renderOnce(roof_end_exterior, matrices, vertices, light, position);
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
					renderMirror(side_panel, matrices, vertices, light, position - 9.5F);
					break;
				case EXTERIOR:
					renderOnceFlipped(end_exterior, matrices, vertices, light, position);
					renderOnceFlipped(roof_end_exterior, matrices, vertices, light, position);
					break;
			}
		}

		@Override
		protected ModelDoorOverlay getModelDoorOverlay() {
			return null;
		}

		@Override
		protected ModelDoorOverlayTop getModelDoorOverlayTop() {
			return null;
		}

		@Override
		protected int[] getWindowPositions() {
			return new int[]{-78, 4, 86};
		}

		@Override
		protected int[] getDoorPositions() {
			return new int[]{-119, -37, 45, 127};
		}


		@Override
		protected int[] getEndPositions() {
			return new int[]{-144, 152};
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


