package mtr.model;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class ModelE44 extends ModelTrainBase {

	private final ModelPart window;
	private final ModelPart upper_wall_r1;
	private final ModelPart window_exterior;
	private final ModelPart upper_wall_r2;
	private final ModelPart seats;
	private final ModelPart handrail_3_r1;
	private final ModelPart seat_7;
	private final ModelPart seat_back_r1;
	private final ModelPart seat_8;
	private final ModelPart seat_back_r2;
	private final ModelPart roof_window;
	private final ModelPart inner_roof_4_r1;
	private final ModelPart inner_roof_2_r1;
	private final ModelPart inner_roof_1_r1;
	private final ModelPart roof_door;
	private final ModelPart inner_roof_9_r1;
	private final ModelPart inner_roof_6_r1;
	private final ModelPart inner_roof_4_r2;
	private final ModelPart inner_roof_3_r1;
	private final ModelPart inner_roof_2_r2;
	private final ModelPart roof_light;
	private final ModelPart roof_exterior;
	private final ModelPart outer_roof_5_r1;
	private final ModelPart outer_roof_4_r1;
	private final ModelPart outer_roof_3_r1;
	private final ModelPart outer_roof_2_r1;
	private final ModelPart outer_roof_1_r1;
	private final ModelPart door_exterior;
	private final ModelPart door_left_exterior;
	private final ModelPart door_left_top_r1;
	private final ModelPart door_right_exterior;
	private final ModelPart door_right_top_r1;
	private final ModelPart door_handrails;
	private final ModelPart side_panel;
	private final ModelPart door;
	private final ModelPart door_left;
	private final ModelPart door_left_top_r2;
	private final ModelPart door_right;
	private final ModelPart door_right_top_r2;
	private final ModelPart end;
	private final ModelPart upper_wall_2_r1;
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
	private final ModelPart upper_wall_2_r2;
	private final ModelPart upper_wall_1_r2;
	private final ModelPart roof_end;
	private final ModelPart handrail_12_r1;
	private final ModelPart handrail_9_r1;
	private final ModelPart inner_roof_right_6_r1;
	private final ModelPart inner_roof_right_4_r1;
	private final ModelPart inner_roof_right_3_r1;
	private final ModelPart inner_roof_left_4_r1;
	private final ModelPart inner_roof_left_3_r1;
	private final ModelPart roof_end_light;
	private final ModelPart roof_end_exterior;
	private final ModelPart outer_roof_1;
	private final ModelPart upper_wall_1_r3;
	private final ModelPart outer_roof_5_r2;
	private final ModelPart outer_roof_4_r2;
	private final ModelPart outer_roof_3_r2;
	private final ModelPart outer_roof_2_r2;
	private final ModelPart outer_roof_2;
	private final ModelPart upper_wall_1_r4;
	private final ModelPart outer_roof_5_r3;
	private final ModelPart outer_roof_4_r3;
	private final ModelPart outer_roof_3_r3;
	private final ModelPart outer_roof_2_r3;
	private final ModelPart roof_end_vents;
	private final ModelPart vent_3_r1;
	private final ModelPart vent_2_r1;
	private final ModelPart head;
	private final ModelPart upper_wall_2_r3;
	private final ModelPart upper_wall_1_r5;
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
	private final ModelPart upper_wall_2_r4;
	private final ModelPart driver_door_upper_1_r1;
	private final ModelPart upper_wall_1_r6;
	private final ModelPart front;
	private final ModelPart head_roof_r1;
	private final ModelPart head_top_r1;
	private final ModelPart head_bottom_3_r1;
	private final ModelPart head_bottom_2_r1;
	private final ModelPart head_bottom_1_r1;
	private final ModelPart side_1;
	private final ModelPart outer_head_7_r1;
	private final ModelPart outer_head_6_r1;
	private final ModelPart outer_head_5_r1;
	private final ModelPart outer_head_4_r1;
	private final ModelPart outer_head_2_r1;
	private final ModelPart outer_head_1_r1;
	private final ModelPart outer_roof_11_r1;
	private final ModelPart outer_roof_10_r1;
	private final ModelPart outer_roof_8_r1;
	private final ModelPart outer_roof_7_r1;
	private final ModelPart outer_roof_3_r4;
	private final ModelPart outer_roof_2_r4;
	private final ModelPart outer_roof_1_r2;
	private final ModelPart side_2;
	private final ModelPart outer_head_7_r2;
	private final ModelPart outer_head_6_r2;
	private final ModelPart outer_head_5_r2;
	private final ModelPart outer_head_4_r2;
	private final ModelPart outer_head_2_r2;
	private final ModelPart outer_head_1_r2;
	private final ModelPart outer_roof_11_r2;
	private final ModelPart outer_roof_10_r2;
	private final ModelPart outer_roof_8_r2;
	private final ModelPart outer_roof_7_r2;
	private final ModelPart outer_roof_3_r5;
	private final ModelPart outer_roof_2_r5;
	private final ModelPart outer_roof_1_r3;
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
	private final ModelPart headlight_2_r1;
	private final ModelPart tail_lights;
	private final ModelPart tail_light_r1;
	private final ModelPart door_light;
	private final ModelPart outer_roof_1_r4;
	private final ModelPart door_light_off;
	private final ModelPart light_r1;
	private final ModelPart door_light_on;
	private final ModelPart light_r2;

	public ModelE44() {
		textureWidth = 352;
		textureHeight = 352;
		window = new ModelPart(this);
		window.setPivot(0.0F, 24.0F, 0.0F);
		window.setTextureOffset(190, 183).addCuboid(-20.0F, 0.0F, -16.0F, 20.0F, 1.0F, 32.0F, 0.0F, false);
		window.setTextureOffset(240, 0).addCuboid(-20.0F, -13.0F, -18.0F, 2.0F, 13.0F, 36.0F, 0.0F, false);

		upper_wall_r1 = new ModelPart(this);
		upper_wall_r1.setPivot(-20.0F, -13.0F, 0.0F);
		window.addChild(upper_wall_r1);
		setRotationAngle(upper_wall_r1, 0.0F, 0.0F, 0.1107F);
		upper_wall_r1.setTextureOffset(198, 216).addCuboid(0.0F, -20.0F, -18.0F, 2.0F, 20.0F, 36.0F, 0.0F, false);

		window_exterior = new ModelPart(this);
		window_exterior.setPivot(0.0F, 24.0F, 0.0F);
		window_exterior.setTextureOffset(176, 272).addCuboid(-21.0F, 0.0F, -16.0F, 1.0F, 4.0F, 32.0F, 0.0F, false);
		window_exterior.setTextureOffset(228, 65).addCuboid(-20.0F, -13.0F, -18.0F, 0.0F, 13.0F, 36.0F, 0.0F, false);

		upper_wall_r2 = new ModelPart(this);
		upper_wall_r2.setPivot(-20.0F, -13.0F, 0.0F);
		window_exterior.addChild(upper_wall_r2);
		setRotationAngle(upper_wall_r2, 0.0F, 0.0F, 0.1107F);
		upper_wall_r2.setTextureOffset(220, 80).addCuboid(0.0F, -20.0F, -18.0F, 0.0F, 20.0F, 36.0F, 0.0F, false);

		seats = new ModelPart(this);
		seats.setPivot(0.0F, 24.0F, 0.0F);
		seats.setTextureOffset(6, 0).addCuboid(-11.0F, -30.35F, 7.0F, 2.0F, 4.0F, 0.0F, 0.0F, false);
		seats.setTextureOffset(6, 0).addCuboid(-11.0F, -30.35F, 0.0F, 2.0F, 4.0F, 0.0F, 0.0F, false);
		seats.setTextureOffset(6, 0).addCuboid(-11.0F, -30.35F, -7.0F, 2.0F, 4.0F, 0.0F, 0.0F, false);

		handrail_3_r1 = new ModelPart(this);
		handrail_3_r1.setPivot(0.0F, 0.0F, 0.0F);
		seats.addChild(handrail_3_r1);
		setRotationAngle(handrail_3_r1, 0.0F, 0.0F, -0.1745F);
		handrail_3_r1.setTextureOffset(1, 0).addCuboid(-4.3486F, -31.2973F, -15.0F, 0.0F, 18.0F, 0.0F, 0.2F, false);
		handrail_3_r1.setTextureOffset(1, 0).addCuboid(-4.3236F, -31.2973F, 15.0F, 0.0F, 18.0F, 0.0F, 0.2F, false);

		seat_7 = new ModelPart(this);
		seat_7.setPivot(0.0F, 0.0F, 0.0F);
		seats.addChild(seat_7);
		seat_7.setTextureOffset(190, 216).addCuboid(-18.0F, -6.0F, -15.0F, 12.0F, 1.0F, 7.0F, 0.0F, false);

		seat_back_r1 = new ModelPart(this);
		seat_back_r1.setPivot(0.0F, -6.0F, -14.0F);
		seat_7.addChild(seat_back_r1);
		setRotationAngle(seat_back_r1, 0.0524F, 0.0F, 0.0F);
		seat_back_r1.setTextureOffset(132, 217).addCuboid(-18.0F, -8.0F, -1.0F, 12.0F, 8.0F, 1.0F, 0.0F, false);

		seat_8 = new ModelPart(this);
		seat_8.setPivot(-24.0F, 0.0F, 2.0F);
		seats.addChild(seat_8);
		seat_8.setTextureOffset(190, 216).addCuboid(6.0F, -6.0F, 6.0F, 12.0F, 1.0F, 7.0F, 0.0F, false);

		seat_back_r2 = new ModelPart(this);
		seat_back_r2.setPivot(0.0F, -6.0F, 12.0F);
		seat_8.addChild(seat_back_r2);
		setRotationAngle(seat_back_r2, -0.0524F, 0.0F, 0.0F);
		seat_back_r2.setTextureOffset(132, 217).addCuboid(6.0F, -8.0F, 0.0F, 12.0F, 8.0F, 1.0F, 0.0F, false);

		roof_window = new ModelPart(this);
		roof_window.setPivot(0.0F, 24.0F, 0.0F);
		roof_window.setTextureOffset(8, 50).addCuboid(-9.775F, -32.575F, -16.0F, 7.0F, 0.0F, 32.0F, 0.0F, false);
		roof_window.setTextureOffset(8, 0).addCuboid(-3.025F, -33.575F, -16.0F, 3.0F, 0.0F, 32.0F, 0.0F, false);

		inner_roof_4_r1 = new ModelPart(this);
		inner_roof_4_r1.setPivot(0.0F, 0.0F, 0.0F);
		roof_window.addChild(inner_roof_4_r1);
		setRotationAngle(inner_roof_4_r1, 0.0F, 0.0F, 1.5708F);
		inner_roof_4_r1.setTextureOffset(14, 0).addCuboid(-33.525F, 2.775F, -16.0F, 1.0F, 0.0F, 32.0F, 0.0F, false);

		inner_roof_2_r1 = new ModelPart(this);
		inner_roof_2_r1.setPivot(0.0F, 0.0F, 0.0F);
		roof_window.addChild(inner_roof_2_r1);
		setRotationAngle(inner_roof_2_r1, 0.0F, 0.0F, 1.309F);
		inner_roof_2_r1.setTextureOffset(120, 0).addCuboid(-36.0F, 1.0F, -16.0F, 2.0F, 0.0F, 32.0F, 0.0F, false);

		inner_roof_1_r1 = new ModelPart(this);
		inner_roof_1_r1.setPivot(0.0F, 0.0F, 0.0F);
		roof_window.addChild(inner_roof_1_r1);
		setRotationAngle(inner_roof_1_r1, 0.0F, 0.0F, -0.3491F);
		inner_roof_1_r1.setTextureOffset(96, 0).addCuboid(-4.75F, -35.0F, -16.0F, 7.0F, 0.0F, 32.0F, 0.0F, false);

		roof_door = new ModelPart(this);
		roof_door.setPivot(0.0F, 24.0F, 0.0F);
		roof_door.setTextureOffset(0, 49).addCuboid(-18.0F, -31.0F, -16.0F, 3.0F, 0.0F, 32.0F, 0.0F, false);
		roof_door.setTextureOffset(8, 50).addCuboid(-9.775F, -32.575F, -16.0F, 7.0F, 0.0F, 32.0F, 0.0F, false);
		roof_door.setTextureOffset(11, 54).addCuboid(-3.525F, -33.575F, -16.0F, 4.0F, 0.0F, 32.0F, 0.0F, false);

		inner_roof_9_r1 = new ModelPart(this);
		inner_roof_9_r1.setPivot(0.0F, 0.0F, 0.0F);
		roof_door.addChild(inner_roof_9_r1);
		setRotationAngle(inner_roof_9_r1, -1.5708F, 0.0F, 0.0F);
		inner_roof_9_r1.setTextureOffset(74, 346).addCuboid(-18.075F, 16.0F, -34.0F, 8.0F, 0.0F, 3.0F, 0.0F, false);
		inner_roof_9_r1.setTextureOffset(74, 346).addCuboid(-17.075F, -16.0F, -34.0F, 8.0F, 0.0F, 3.0F, 0.0F, false);

		inner_roof_6_r1 = new ModelPart(this);
		inner_roof_6_r1.setPivot(0.0F, 0.0F, 0.0F);
		roof_door.addChild(inner_roof_6_r1);
		setRotationAngle(inner_roof_6_r1, 0.0F, 0.0F, 1.5708F);
		inner_roof_6_r1.setTextureOffset(14, 0).addCuboid(-33.525F, 2.775F, -16.0F, 1.0F, 0.0F, 32.0F, 0.0F, false);

		inner_roof_4_r2 = new ModelPart(this);
		inner_roof_4_r2.setPivot(0.0F, 0.0F, 0.0F);
		roof_door.addChild(inner_roof_4_r2);
		setRotationAngle(inner_roof_4_r2, 0.0F, 0.0F, 1.309F);
		inner_roof_4_r2.setTextureOffset(120, 0).addCuboid(-36.0F, 1.0F, -16.0F, 2.0F, 0.0F, 32.0F, 0.0F, false);

		inner_roof_3_r1 = new ModelPart(this);
		inner_roof_3_r1.setPivot(0.0F, 0.0F, 0.0F);
		roof_door.addChild(inner_roof_3_r1);
		setRotationAngle(inner_roof_3_r1, 0.0F, 0.0F, -0.3491F);
		inner_roof_3_r1.setTextureOffset(26, 67).addCuboid(0.25F, -35.0F, -16.0F, 2.0F, 0.0F, 32.0F, 0.0F, false);

		inner_roof_2_r2 = new ModelPart(this);
		inner_roof_2_r2.setPivot(-14.0F, -32.0F, 0.0F);
		roof_door.addChild(inner_roof_2_r2);
		setRotationAngle(inner_roof_2_r2, 0.0F, 0.0F, -0.5236F);
		inner_roof_2_r2.setTextureOffset(0, 0).addCuboid(-1.5F, 0.325F, -16.0F, 4.0F, 0.0F, 32.0F, 0.0F, false);

		roof_light = new ModelPart(this);
		roof_light.setPivot(0.0F, 24.0F, 0.0F);
		roof_light.setTextureOffset(110, 0).addCuboid(-2.5F, -33.1F, -16.0F, 5.0F, 0.0F, 32.0F, 0.0F, false);

		roof_exterior = new ModelPart(this);
		roof_exterior.setPivot(0.0F, 24.0F, 0.0F);
		roof_exterior.setTextureOffset(276, 320).addCuboid(-6.0F, -42.0F, -16.0F, 6.0F, 0.0F, 32.0F, 0.0F, false);

		outer_roof_5_r1 = new ModelPart(this);
		outer_roof_5_r1.setPivot(-9.9394F, -41.3064F, 0.0F);
		roof_exterior.addChild(outer_roof_5_r1);
		setRotationAngle(outer_roof_5_r1, 0.0F, 0.0F, -0.1745F);
		outer_roof_5_r1.setTextureOffset(272, 320).addCuboid(-4.0F, 0.0F, -16.0F, 8.0F, 0.0F, 32.0F, 0.0F, false);

		outer_roof_4_r1 = new ModelPart(this);
		outer_roof_4_r1.setPivot(-15.1778F, -39.8628F, 0.0F);
		roof_exterior.addChild(outer_roof_4_r1);
		setRotationAngle(outer_roof_4_r1, 0.0F, 0.0F, -0.5236F);
		outer_roof_4_r1.setTextureOffset(282, 320).addCuboid(-1.5F, 0.0F, -16.0F, 3.0F, 0.0F, 32.0F, 0.0F, false);

		outer_roof_3_r1 = new ModelPart(this);
		outer_roof_3_r1.setPivot(-16.9769F, -38.2468F, 0.0F);
		roof_exterior.addChild(outer_roof_3_r1);
		setRotationAngle(outer_roof_3_r1, 0.0F, 0.0F, -1.0472F);
		outer_roof_3_r1.setTextureOffset(284, 320).addCuboid(-1.0F, 0.0F, -16.0F, 2.0F, 0.0F, 32.0F, 0.0F, false);

		outer_roof_2_r1 = new ModelPart(this);
		outer_roof_2_r1.setPivot(-17.5872F, -36.3872F, 0.0F);
		roof_exterior.addChild(outer_roof_2_r1);
		setRotationAngle(outer_roof_2_r1, 0.0F, 0.0F, 0.1107F);
		outer_roof_2_r1.setTextureOffset(288, 318).addCuboid(0.0F, -1.0F, -16.0F, 0.0F, 2.0F, 32.0F, 0.0F, false);

		outer_roof_1_r1 = new ModelPart(this);
		outer_roof_1_r1.setPivot(-20.0F, -13.0F, 0.0F);
		roof_exterior.addChild(outer_roof_1_r1);
		setRotationAngle(outer_roof_1_r1, 0.0F, 0.0F, 0.1107F);
		outer_roof_1_r1.setTextureOffset(277, 49).addCuboid(-1.0F, -23.0F, -16.0F, 1.0F, 4.0F, 32.0F, 0.0F, false);

		door_exterior = new ModelPart(this);
		door_exterior.setPivot(0.0F, 24.0F, 0.0F);
		door_exterior.setTextureOffset(72, 272).addCuboid(-21.0F, 0.0F, -16.0F, 1.0F, 4.0F, 32.0F, 0.0F, false);

		door_left_exterior = new ModelPart(this);
		door_left_exterior.setPivot(0.0F, 0.0F, 0.0F);
		door_exterior.addChild(door_left_exterior);
		door_left_exterior.setTextureOffset(62, 72).addCuboid(-20.8F, -13.0F, 0.0F, 0.0F, 13.0F, 15.0F, 0.0F, false);

		door_left_top_r1 = new ModelPart(this);
		door_left_top_r1.setPivot(-20.8F, -13.0F, 0.0F);
		door_left_exterior.addChild(door_left_top_r1);
		setRotationAngle(door_left_top_r1, 0.0F, 0.0F, 0.1107F);
		door_left_top_r1.setTextureOffset(0, 108).addCuboid(0.0F, -20.0F, 0.0F, 0.0F, 20.0F, 15.0F, 0.0F, false);

		door_right_exterior = new ModelPart(this);
		door_right_exterior.setPivot(0.0F, 0.0F, 0.0F);
		door_exterior.addChild(door_right_exterior);
		door_right_exterior.setTextureOffset(0, 69).addCuboid(-20.8F, -13.0F, -15.0F, 0.0F, 13.0F, 15.0F, 0.0F, false);

		door_right_top_r1 = new ModelPart(this);
		door_right_top_r1.setPivot(-20.8F, -13.0F, 0.0F);
		door_right_exterior.addChild(door_right_top_r1);
		setRotationAngle(door_right_top_r1, 0.0F, 0.0F, 0.1107F);
		door_right_top_r1.setTextureOffset(62, 52).addCuboid(0.0F, -20.0F, -15.0F, 0.0F, 20.0F, 15.0F, 0.0F, false);

		door_handrails = new ModelPart(this);
		door_handrails.setPivot(0.0F, 24.0F, 0.0F);
		

		side_panel = new ModelPart(this);
		side_panel.setPivot(0.0F, 24.0F, 0.0F);
		side_panel.setTextureOffset(262, 183).addCuboid(-18.0F, -31.0F, -16.0F, 13.0F, 31.0F, 0.0F, 0.0F, false);
		side_panel.setTextureOffset(262, 183).addCuboid(-18.0F, -31.0F, 16.0F, 13.0F, 31.0F, 0.0F, 0.0F, false);

		door = new ModelPart(this);
		door.setPivot(0.0F, 24.0F, 0.0F);
		door.setTextureOffset(190, 183).addCuboid(-20.0F, 0.0F, -16.0F, 20.0F, 1.0F, 32.0F, 0.0F, false);

		door_left = new ModelPart(this);
		door_left.setPivot(0.0F, 0.0F, 0.0F);
		door.addChild(door_left);
		door_left.setTextureOffset(190, 183).addCuboid(-20.8F, -13.0F, 0.0F, 1.0F, 13.0F, 15.0F, 0.0F, false);

		door_left_top_r2 = new ModelPart(this);
		door_left_top_r2.setPivot(-20.8F, -13.0F, 0.0F);
		door_left.addChild(door_left_top_r2);
		setRotationAngle(door_left_top_r2, 0.0F, 0.0F, 0.1107F);
		door_left_top_r2.setTextureOffset(0, 49).addCuboid(0.0F, -20.0F, 0.0F, 1.0F, 20.0F, 15.0F, 0.0F, false);

		door_right = new ModelPart(this);
		door_right.setPivot(0.0F, 0.0F, 0.0F);
		door.addChild(door_right);
		door_right.setTextureOffset(187, 69).addCuboid(-20.8F, -13.0F, -15.0F, 1.0F, 13.0F, 15.0F, 0.0F, false);

		door_right_top_r2 = new ModelPart(this);
		door_right_top_r2.setPivot(-20.8F, -13.0F, 0.0F);
		door_right.addChild(door_right_top_r2);
		setRotationAngle(door_right_top_r2, 0.0F, 0.0F, 0.1107F);
		door_right_top_r2.setTextureOffset(0, 0).addCuboid(0.0F, -20.0F, -15.0F, 1.0F, 20.0F, 15.0F, 0.0F, false);

		end = new ModelPart(this);
		end.setPivot(0.0F, 24.0F, 0.0F);
		end.setTextureOffset(0, 0).addCuboid(-20.0F, 0.0F, -32.0F, 40.0F, 1.0F, 48.0F, 0.0F, false);
		end.setTextureOffset(162, 116).addCuboid(18.0F, -13.0F, -36.0F, 2.0F, 13.0F, 54.0F, 0.0F, false);
		end.setTextureOffset(116, 49).addCuboid(-20.0F, -13.0F, -36.0F, 2.0F, 13.0F, 54.0F, 0.0F, false);
		end.setTextureOffset(0, 163).addCuboid(6.0F, -32.0F, -36.0F, 12.0F, 32.0F, 12.0F, 0.0F, false);
		end.setTextureOffset(174, 0).addCuboid(-18.0F, -32.0F, -36.0F, 12.0F, 32.0F, 12.0F, 0.0F, false);
		end.setTextureOffset(238, 220).addCuboid(-18.0F, -35.0F, -36.0F, 36.0F, 3.0F, 12.0F, 0.0F, false);
		end.setTextureOffset(72, 272).addCuboid(-6.0F, -32.0F, -24.0F, 12.0F, 32.0F, 0.0F, 0.0F, false);

		upper_wall_2_r1 = new ModelPart(this);
		upper_wall_2_r1.setPivot(-20.0F, -13.0F, 0.0F);
		end.addChild(upper_wall_2_r1);
		setRotationAngle(upper_wall_2_r1, 0.0F, 0.0F, 0.1107F);
		upper_wall_2_r1.setTextureOffset(0, 49).addCuboid(0.0F, -20.0F, -36.0F, 2.0F, 20.0F, 54.0F, 0.0F, false);

		upper_wall_1_r1 = new ModelPart(this);
		upper_wall_1_r1.setPivot(20.0F, -13.0F, 0.0F);
		end.addChild(upper_wall_1_r1);
		setRotationAngle(upper_wall_1_r1, 0.0F, 0.0F, -0.1107F);
		upper_wall_1_r1.setTextureOffset(58, 69).addCuboid(-2.0F, -20.0F, -36.0F, 2.0F, 20.0F, 54.0F, 0.0F, false);

		seat_1 = new ModelPart(this);
		seat_1.setPivot(0.0F, 0.0F, 0.0F);
		end.addChild(seat_1);
		seat_1.setTextureOffset(190, 216).addCuboid(6.0F, -6.0F, -23.0F, 12.0F, 1.0F, 7.0F, 0.0F, false);

		seat_back_r3 = new ModelPart(this);
		seat_back_r3.setPivot(0.0F, -6.0F, -22.0F);
		seat_1.addChild(seat_back_r3);
		setRotationAngle(seat_back_r3, 0.0524F, 0.0F, 0.0F);
		seat_back_r3.setTextureOffset(132, 217).addCuboid(6.0F, -8.0F, -1.0F, 12.0F, 8.0F, 1.0F, 0.0F, false);

		seat_2 = new ModelPart(this);
		seat_2.setPivot(0.0F, 0.0F, 0.0F);
		end.addChild(seat_2);
		seat_2.setTextureOffset(190, 216).addCuboid(6.0F, -6.0F, -11.0F, 12.0F, 1.0F, 7.0F, 0.0F, false);

		seat_back_r4 = new ModelPart(this);
		seat_back_r4.setPivot(0.0F, -6.0F, -10.0F);
		seat_2.addChild(seat_back_r4);
		setRotationAngle(seat_back_r4, 0.0524F, 0.0F, 0.0F);
		seat_back_r4.setTextureOffset(132, 217).addCuboid(6.0F, -8.0F, -1.0F, 12.0F, 8.0F, 1.0F, 0.0F, false);

		seat_3 = new ModelPart(this);
		seat_3.setPivot(0.0F, 0.0F, 0.0F);
		end.addChild(seat_3);
		seat_3.setTextureOffset(190, 216).addCuboid(6.0F, -6.0F, 6.0F, 12.0F, 1.0F, 7.0F, 0.0F, false);

		seat_back_r5 = new ModelPart(this);
		seat_back_r5.setPivot(0.0F, -6.0F, 12.0F);
		seat_3.addChild(seat_back_r5);
		setRotationAngle(seat_back_r5, -0.0524F, 0.0F, 0.0F);
		seat_back_r5.setTextureOffset(132, 217).addCuboid(6.0F, -8.0F, 0.0F, 12.0F, 8.0F, 1.0F, 0.0F, false);

		seat_4 = new ModelPart(this);
		seat_4.setPivot(0.0F, 0.0F, 0.0F);
		end.addChild(seat_4);
		seat_4.setTextureOffset(190, 216).addCuboid(-18.0F, -6.0F, -23.0F, 12.0F, 1.0F, 7.0F, 0.0F, true);

		seat_back_r6 = new ModelPart(this);
		seat_back_r6.setPivot(0.0F, -6.0F, -22.0F);
		seat_4.addChild(seat_back_r6);
		setRotationAngle(seat_back_r6, 0.0524F, 0.0F, 0.0F);
		seat_back_r6.setTextureOffset(132, 217).addCuboid(-18.0F, -8.0F, -1.0F, 12.0F, 8.0F, 1.0F, 0.0F, true);

		seat_5 = new ModelPart(this);
		seat_5.setPivot(0.0F, 0.0F, 0.0F);
		end.addChild(seat_5);
		seat_5.setTextureOffset(190, 216).addCuboid(-18.0F, -6.0F, -11.0F, 12.0F, 1.0F, 7.0F, 0.0F, true);

		seat_back_r7 = new ModelPart(this);
		seat_back_r7.setPivot(0.0F, -6.0F, -10.0F);
		seat_5.addChild(seat_back_r7);
		setRotationAngle(seat_back_r7, 0.0524F, 0.0F, 0.0F);
		seat_back_r7.setTextureOffset(132, 217).addCuboid(-18.0F, -8.0F, -1.0F, 12.0F, 8.0F, 1.0F, 0.0F, true);

		seat_6 = new ModelPart(this);
		seat_6.setPivot(0.0F, 0.0F, 0.0F);
		end.addChild(seat_6);
		seat_6.setTextureOffset(190, 216).addCuboid(-18.0F, -6.0F, 6.0F, 12.0F, 1.0F, 7.0F, 0.0F, true);

		seat_back_r8 = new ModelPart(this);
		seat_back_r8.setPivot(0.0F, -6.0F, 12.0F);
		seat_6.addChild(seat_back_r8);
		setRotationAngle(seat_back_r8, -0.0524F, 0.0F, 0.0F);
		seat_back_r8.setTextureOffset(132, 217).addCuboid(-18.0F, -8.0F, 0.0F, 12.0F, 8.0F, 1.0F, 0.0F, true);

		end_exterior = new ModelPart(this);
		end_exterior.setPivot(0.0F, 24.0F, 0.0F);
		end_exterior.setTextureOffset(144, 183).addCuboid(20.0F, 0.0F, -28.0F, 1.0F, 4.0F, 44.0F, 0.0F, false);
		end_exterior.setTextureOffset(128, 0).addCuboid(-21.0F, 0.0F, -28.0F, 1.0F, 4.0F, 44.0F, 0.0F, false);
		end_exterior.setTextureOffset(80, 129).addCuboid(20.0F, -13.0F, -36.0F, 0.0F, 13.0F, 54.0F, 0.0F, false);
		end_exterior.setTextureOffset(58, 0).addCuboid(-20.0F, -13.0F, -36.0F, 0.0F, 13.0F, 54.0F, 0.0F, false);
		end_exterior.setTextureOffset(240, 51).addCuboid(6.0F, -33.0F, -36.0F, 12.0F, 33.0F, 0.0F, 0.0F, false);
		end_exterior.setTextureOffset(240, 0).addCuboid(-18.0F, -33.0F, -36.0F, 12.0F, 33.0F, 0.0F, 0.0F, false);
		end_exterior.setTextureOffset(240, 90).addCuboid(-18.0F, -41.0F, -36.0F, 36.0F, 9.0F, 0.0F, 0.0F, false);

		upper_wall_2_r2 = new ModelPart(this);
		upper_wall_2_r2.setPivot(-20.0F, -13.0F, 0.0F);
		end_exterior.addChild(upper_wall_2_r2);
		setRotationAngle(upper_wall_2_r2, 0.0F, 0.0F, 0.1107F);
		upper_wall_2_r2.setTextureOffset(0, 89).addCuboid(0.0F, -20.0F, -36.0F, 0.0F, 20.0F, 54.0F, 0.0F, false);

		upper_wall_1_r2 = new ModelPart(this);
		upper_wall_1_r2.setPivot(20.0F, -13.0F, 0.0F);
		end_exterior.addChild(upper_wall_1_r2);
		setRotationAngle(upper_wall_1_r2, 0.0F, 0.0F, -0.1107F);
		upper_wall_1_r2.setTextureOffset(108, 89).addCuboid(0.0F, -20.0F, -36.0F, 0.0F, 20.0F, 54.0F, 0.0F, false);

		roof_end = new ModelPart(this);
		roof_end.setPivot(0.0F, 24.0F, 0.0F);
		roof_end.setTextureOffset(0, 49).addCuboid(-9.775F, -32.575F, -24.0F, 7.0F, 0.0F, 40.0F, 0.0F, false);
		roof_end.setTextureOffset(0, 0).addCuboid(-3.025F, -33.575F, -24.0F, 3.0F, 0.0F, 40.0F, 0.0F, false);
		roof_end.setTextureOffset(0, 49).addCuboid(2.775F, -32.575F, -24.0F, 7.0F, 0.0F, 40.0F, 0.0F, false);
		roof_end.setTextureOffset(0, 0).addCuboid(-0.025F, -33.575F, -24.0F, 3.0F, 0.0F, 40.0F, 0.0F, false);
		roof_end.setTextureOffset(6, 0).addCuboid(9.0F, -31.0F, 5.4F, 2.0F, 4.0F, 0.0F, 0.0F, false);
		roof_end.setTextureOffset(6, 0).addCuboid(9.0F, -31.0F, -0.6F, 2.0F, 4.0F, 0.0F, 0.0F, false);
		roof_end.setTextureOffset(6, 0).addCuboid(9.0F, -31.0F, -6.6F, 2.0F, 4.0F, 0.0F, 0.0F, false);
		roof_end.setTextureOffset(6, 0).addCuboid(-11.0F, -31.0F, 5.4F, 2.0F, 4.0F, 0.0F, 0.0F, false);
		roof_end.setTextureOffset(6, 0).addCuboid(-11.0F, -31.0F, -0.6F, 2.0F, 4.0F, 0.0F, 0.0F, false);
		roof_end.setTextureOffset(6, 0).addCuboid(-11.0F, -31.0F, -6.6F, 2.0F, 4.0F, 0.0F, 0.0F, false);
		roof_end.setTextureOffset(1, 0).addCuboid(-10.0F, -33.4649F, -16.8899F, 0.0F, 2.0F, 0.0F, 0.2F, false);
		roof_end.setTextureOffset(1, 0).addCuboid(10.0F, -33.4649F, -16.8899F, 0.0F, 2.0F, 0.0F, 0.2F, false);

		handrail_12_r1 = new ModelPart(this);
		handrail_12_r1.setPivot(10.0F, -30.275F, -16.1F);
		roof_end.addChild(handrail_12_r1);
		setRotationAngle(handrail_12_r1, 0.7854F, 0.0F, 0.0F);
		handrail_12_r1.setTextureOffset(1, 0).addCuboid(0.0F, -1.2F, 0.2F, 0.0F, 1.0F, 0.0F, 0.2F, false);
		handrail_12_r1.setTextureOffset(1, 0).addCuboid(-20.0F, -1.2F, 0.2F, 0.0F, 1.0F, 0.0F, 0.2F, false);

		handrail_9_r1 = new ModelPart(this);
		handrail_9_r1.setPivot(-10.0F, 1.0F, 0.0F);
		roof_end.addChild(handrail_9_r1);
		setRotationAngle(handrail_9_r1, -1.5708F, 0.0F, 0.0F);
		handrail_9_r1.setTextureOffset(1, 0).addCuboid(0.0F, -16.0F, -31.5F, 0.0F, 32.0F, 0.0F, 0.2F, false);
		handrail_9_r1.setTextureOffset(1, 0).addCuboid(20.0F, -16.0F, -31.5F, 0.0F, 32.0F, 0.0F, 0.2F, false);

		inner_roof_right_6_r1 = new ModelPart(this);
		inner_roof_right_6_r1.setPivot(0.0F, 0.0F, 0.0F);
		roof_end.addChild(inner_roof_right_6_r1);
		setRotationAngle(inner_roof_right_6_r1, 0.0F, 0.0F, 1.5708F);
		inner_roof_right_6_r1.setTextureOffset(6, 0).addCuboid(-33.525F, -2.775F, -24.0F, 1.0F, 0.0F, 40.0F, 0.0F, false);
		inner_roof_right_6_r1.setTextureOffset(6, 0).addCuboid(-33.525F, 2.775F, -24.0F, 1.0F, 0.0F, 40.0F, 0.0F, false);

		inner_roof_right_4_r1 = new ModelPart(this);
		inner_roof_right_4_r1.setPivot(0.0F, 0.0F, 0.0F);
		roof_end.addChild(inner_roof_right_4_r1);
		setRotationAngle(inner_roof_right_4_r1, 0.0F, 0.0F, -1.309F);
		inner_roof_right_4_r1.setTextureOffset(112, 0).addCuboid(34.0F, 1.0F, -24.0F, 2.0F, 0.0F, 40.0F, 0.0F, false);

		inner_roof_right_3_r1 = new ModelPart(this);
		inner_roof_right_3_r1.setPivot(0.0F, 0.0F, 0.0F);
		roof_end.addChild(inner_roof_right_3_r1);
		setRotationAngle(inner_roof_right_3_r1, 0.0F, 0.0F, 0.3491F);
		inner_roof_right_3_r1.setTextureOffset(88, 0).addCuboid(-2.25F, -35.0F, -24.0F, 7.0F, 0.0F, 40.0F, 0.0F, false);

		inner_roof_left_4_r1 = new ModelPart(this);
		inner_roof_left_4_r1.setPivot(0.0F, 0.0F, 0.0F);
		roof_end.addChild(inner_roof_left_4_r1);
		setRotationAngle(inner_roof_left_4_r1, 0.0F, 0.0F, 1.309F);
		inner_roof_left_4_r1.setTextureOffset(112, 0).addCuboid(-36.0F, 1.0F, -24.0F, 2.0F, 0.0F, 40.0F, 0.0F, false);

		inner_roof_left_3_r1 = new ModelPart(this);
		inner_roof_left_3_r1.setPivot(0.0F, 0.0F, 0.0F);
		roof_end.addChild(inner_roof_left_3_r1);
		setRotationAngle(inner_roof_left_3_r1, 0.0F, 0.0F, -0.3491F);
		inner_roof_left_3_r1.setTextureOffset(88, 0).addCuboid(-4.75F, -35.0F, -24.0F, 7.0F, 0.0F, 40.0F, 0.0F, false);

		roof_end_light = new ModelPart(this);
		roof_end_light.setPivot(0.0F, 24.0F, 0.0F);
		roof_end_light.setTextureOffset(102, 0).addCuboid(-2.5F, -33.1F, -24.0F, 5.0F, 0.0F, 40.0F, 0.0F, false);

		roof_end_exterior = new ModelPart(this);
		roof_end_exterior.setPivot(0.0F, 24.0F, 0.0F);
		

		outer_roof_1 = new ModelPart(this);
		outer_roof_1.setPivot(0.0F, 0.0F, 0.0F);
		roof_end_exterior.addChild(outer_roof_1);
		outer_roof_1.setTextureOffset(300, 331).addCuboid(-6.0F, -42.0F, -36.0F, 6.0F, 1.0F, 20.0F, 0.0F, false);

		upper_wall_1_r3 = new ModelPart(this);
		upper_wall_1_r3.setPivot(-20.0F, -13.0F, 0.0F);
		outer_roof_1.addChild(upper_wall_1_r3);
		setRotationAngle(upper_wall_1_r3, 0.0F, 0.0F, 0.1107F);
		upper_wall_1_r3.setTextureOffset(156, 0).addCuboid(0.0F, -23.0F, -36.0F, 1.0F, 4.0F, 7.0F, 0.0F, false);
		upper_wall_1_r3.setTextureOffset(143, 27).addCuboid(-1.0F, -23.0F, -29.0F, 1.0F, 4.0F, 13.0F, 0.0F, false);

		outer_roof_5_r2 = new ModelPart(this);
		outer_roof_5_r2.setPivot(-9.7656F, -40.3206F, 0.0F);
		outer_roof_1.addChild(outer_roof_5_r2);
		setRotationAngle(outer_roof_5_r2, 0.0F, 0.0F, -0.1745F);
		outer_roof_5_r2.setTextureOffset(296, 331).addCuboid(-4.0F, -1.0F, -36.0F, 8.0F, 1.0F, 20.0F, 0.0F, false);

		outer_roof_4_r2 = new ModelPart(this);
		outer_roof_4_r2.setPivot(-14.6775F, -38.9948F, 0.0F);
		outer_roof_1.addChild(outer_roof_4_r2);
		setRotationAngle(outer_roof_4_r2, 0.0F, 0.0F, -0.5236F);
		outer_roof_4_r2.setTextureOffset(306, 331).addCuboid(-1.5F, -1.0F, -36.0F, 3.0F, 1.0F, 20.0F, 0.0F, false);

		outer_roof_3_r2 = new ModelPart(this);
		outer_roof_3_r2.setPivot(-16.1105F, -37.7448F, 0.0F);
		outer_roof_1.addChild(outer_roof_3_r2);
		setRotationAngle(outer_roof_3_r2, 0.0F, 0.0F, -1.0472F);
		outer_roof_3_r2.setTextureOffset(308, 331).addCuboid(-1.0F, -1.0F, -36.0F, 2.0F, 1.0F, 20.0F, 0.0F, false);

		outer_roof_2_r2 = new ModelPart(this);
		outer_roof_2_r2.setPivot(-17.587F, -36.3849F, 0.0F);
		outer_roof_1.addChild(outer_roof_2_r2);
		setRotationAngle(outer_roof_2_r2, 0.0F, 0.0F, 0.1107F);
		outer_roof_2_r2.setTextureOffset(310, 330).addCuboid(0.0F, -1.0F, -36.0F, 1.0F, 2.0F, 20.0F, 0.0F, false);

		outer_roof_2 = new ModelPart(this);
		outer_roof_2.setPivot(0.0F, 0.0F, 0.0F);
		roof_end_exterior.addChild(outer_roof_2);
		outer_roof_2.setTextureOffset(300, 331).addCuboid(0.0F, -42.0F, -36.0F, 6.0F, 1.0F, 20.0F, 0.0F, false);

		upper_wall_1_r4 = new ModelPart(this);
		upper_wall_1_r4.setPivot(20.0F, -13.0F, 0.0F);
		outer_roof_2.addChild(upper_wall_1_r4);
		setRotationAngle(upper_wall_1_r4, 0.0F, 0.0F, -0.1107F);
		upper_wall_1_r4.setTextureOffset(156, 11).addCuboid(-1.0F, -23.0F, -36.0F, 1.0F, 4.0F, 7.0F, 0.0F, false);
		upper_wall_1_r4.setTextureOffset(30, 123).addCuboid(0.0F, -23.0F, -29.0F, 1.0F, 4.0F, 13.0F, 0.0F, false);

		outer_roof_5_r3 = new ModelPart(this);
		outer_roof_5_r3.setPivot(9.7656F, -40.3206F, 0.0F);
		outer_roof_2.addChild(outer_roof_5_r3);
		setRotationAngle(outer_roof_5_r3, 0.0F, 0.0F, 0.1745F);
		outer_roof_5_r3.setTextureOffset(296, 331).addCuboid(-4.0F, -1.0F, -36.0F, 8.0F, 1.0F, 20.0F, 0.0F, false);

		outer_roof_4_r3 = new ModelPart(this);
		outer_roof_4_r3.setPivot(14.6775F, -38.9948F, 0.0F);
		outer_roof_2.addChild(outer_roof_4_r3);
		setRotationAngle(outer_roof_4_r3, 0.0F, 0.0F, 0.5236F);
		outer_roof_4_r3.setTextureOffset(306, 331).addCuboid(-1.5F, -1.0F, -36.0F, 3.0F, 1.0F, 20.0F, 0.0F, false);

		outer_roof_3_r3 = new ModelPart(this);
		outer_roof_3_r3.setPivot(16.1105F, -37.7448F, 0.0F);
		outer_roof_2.addChild(outer_roof_3_r3);
		setRotationAngle(outer_roof_3_r3, 0.0F, 0.0F, 1.0472F);
		outer_roof_3_r3.setTextureOffset(308, 331).addCuboid(-1.0F, -1.0F, -36.0F, 2.0F, 1.0F, 20.0F, 0.0F, false);

		outer_roof_2_r3 = new ModelPart(this);
		outer_roof_2_r3.setPivot(17.587F, -36.3849F, 0.0F);
		outer_roof_2.addChild(outer_roof_2_r3);
		setRotationAngle(outer_roof_2_r3, 0.0F, 0.0F, -0.1107F);
		outer_roof_2_r3.setTextureOffset(310, 330).addCuboid(-1.0F, -1.0F, -36.0F, 1.0F, 2.0F, 20.0F, 0.0F, false);

		roof_end_vents = new ModelPart(this);
		roof_end_vents.setPivot(0.0F, 24.0F, 0.0F);
		roof_end_vents.setTextureOffset(0, 163).addCuboid(-8.0F, -43.0F, -21.0F, 16.0F, 2.0F, 48.0F, 0.0F, false);

		vent_3_r1 = new ModelPart(this);
		vent_3_r1.setPivot(-8.0F, -43.0F, 12.0F);
		roof_end_vents.addChild(vent_3_r1);
		setRotationAngle(vent_3_r1, 0.0F, 0.0F, -0.3491F);
		vent_3_r1.setTextureOffset(174, 1).addCuboid(-9.0F, 0.0F, -33.0F, 9.0F, 2.0F, 48.0F, 0.0F, false);

		vent_2_r1 = new ModelPart(this);
		vent_2_r1.setPivot(8.0F, -43.0F, 12.0F);
		roof_end_vents.addChild(vent_2_r1);
		setRotationAngle(vent_2_r1, 0.0F, 0.0F, 0.3491F);
		vent_2_r1.setTextureOffset(174, 51).addCuboid(0.0F, 0.0F, -33.0F, 9.0F, 2.0F, 48.0F, 0.0F, false);

		head = new ModelPart(this);
		head.setPivot(0.0F, 24.0F, 0.0F);
		head.setTextureOffset(14, 13).addCuboid(-20.0F, 0.0F, -16.0F, 40.0F, 1.0F, 34.0F, 0.0F, false);
		head.setTextureOffset(238, 236).addCuboid(-20.0F, -13.0F, -18.0F, 2.0F, 13.0F, 36.0F, 0.0F, false);
		head.setTextureOffset(132, 236).addCuboid(18.0F, -13.0F, -18.0F, 2.0F, 13.0F, 36.0F, 0.0F, false);
		head.setTextureOffset(0, 269).addCuboid(-18.0F, -36.0F, 18.0F, 36.0F, 36.0F, 0.0F, 0.0F, false);

		upper_wall_2_r3 = new ModelPart(this);
		upper_wall_2_r3.setPivot(20.0F, -13.0F, 0.0F);
		head.addChild(upper_wall_2_r3);
		setRotationAngle(upper_wall_2_r3, 0.0F, 0.0F, -0.1107F);
		upper_wall_2_r3.setTextureOffset(92, 196).addCuboid(-2.0F, -20.0F, -18.0F, 2.0F, 20.0F, 36.0F, 0.0F, false);

		upper_wall_1_r5 = new ModelPart(this);
		upper_wall_1_r5.setPivot(-20.0F, -13.0F, 0.0F);
		head.addChild(upper_wall_1_r5);
		setRotationAngle(upper_wall_1_r5, 0.0F, 0.0F, 0.1107F);
		upper_wall_1_r5.setTextureOffset(0, 213).addCuboid(0.0F, -20.0F, -18.0F, 2.0F, 20.0F, 36.0F, 0.0F, false);

		seat_9 = new ModelPart(this);
		seat_9.setPivot(0.0F, 0.0F, 2.0F);
		head.addChild(seat_9);
		seat_9.setTextureOffset(190, 216).addCuboid(-18.0F, -6.0F, 8.0F, 12.0F, 1.0F, 7.0F, 0.0F, true);

		seat_back_r9 = new ModelPart(this);
		seat_back_r9.setPivot(0.0F, -6.0F, 14.0F);
		seat_9.addChild(seat_back_r9);
		setRotationAngle(seat_back_r9, -0.0524F, 0.0F, 0.0F);
		seat_back_r9.setTextureOffset(132, 217).addCuboid(-18.0F, -8.0F, 0.0F, 12.0F, 8.0F, 1.0F, 0.0F, true);

		seat_10 = new ModelPart(this);
		seat_10.setPivot(0.0F, 0.0F, -2.0F);
		head.addChild(seat_10);
		setRotationAngle(seat_10, 0.0F, 3.1416F, 0.0F);
		seat_10.setTextureOffset(190, 216).addCuboid(6.0F, -6.0F, 4.0F, 12.0F, 1.0F, 7.0F, 0.0F, false);

		seat_back_r10 = new ModelPart(this);
		seat_back_r10.setPivot(0.0F, -6.0F, 12.0F);
		seat_10.addChild(seat_back_r10);
		setRotationAngle(seat_back_r10, -0.0524F, 0.0F, 0.0F);
		seat_back_r10.setTextureOffset(132, 217).addCuboid(6.0F, -7.8953F, -1.9973F, 12.0F, 8.0F, 1.0F, 0.0F, false);

		seat_11 = new ModelPart(this);
		seat_11.setPivot(24.0F, 0.0F, -2.0F);
		head.addChild(seat_11);
		setRotationAngle(seat_11, 0.0F, 3.1416F, 0.0F);
		seat_11.setTextureOffset(190, 216).addCuboid(6.0F, -6.0F, 4.0F, 12.0F, 1.0F, 7.0F, 0.0F, false);

		seat_back_r11 = new ModelPart(this);
		seat_back_r11.setPivot(0.0F, -6.0F, 12.0F);
		seat_11.addChild(seat_back_r11);
		setRotationAngle(seat_back_r11, -0.0524F, 0.0F, 0.0F);
		seat_back_r11.setTextureOffset(132, 217).addCuboid(6.0F, -7.8953F, -1.9973F, 12.0F, 8.0F, 1.0F, 0.0F, false);

		seat_12 = new ModelPart(this);
		seat_12.setPivot(0.0F, 0.0F, 2.0F);
		head.addChild(seat_12);
		seat_12.setTextureOffset(190, 216).addCuboid(6.0F, -6.0F, 8.0F, 12.0F, 1.0F, 7.0F, 0.0F, true);

		seat_back_r12 = new ModelPart(this);
		seat_back_r12.setPivot(24.0F, -6.0F, 14.0F);
		seat_12.addChild(seat_back_r12);
		setRotationAngle(seat_back_r12, -0.0524F, 0.0F, 0.0F);
		seat_back_r12.setTextureOffset(132, 217).addCuboid(-18.0F, -8.0F, 0.0F, 12.0F, 8.0F, 1.0F, 0.0F, true);

		head_exterior = new ModelPart(this);
		head_exterior.setPivot(0.0F, 24.0F, 0.0F);
		head_exterior.setTextureOffset(220, 136).addCuboid(-21.0F, 0.0F, 19.0F, 42.0F, 7.0F, 12.0F, 0.0F, false);
		head_exterior.setTextureOffset(220, 118).addCuboid(20.0F, -13.0F, -18.0F, 0.0F, 13.0F, 37.0F, 0.0F, false);
		head_exterior.setTextureOffset(277, 49).addCuboid(20.0F, -13.0F, 19.0F, 1.0F, 13.0F, 12.0F, 0.0F, false);
		head_exterior.setTextureOffset(40, 176).addCuboid(-20.0F, -13.0F, -18.0F, 0.0F, 13.0F, 37.0F, 0.0F, false);
		head_exterior.setTextureOffset(210, 272).addCuboid(-21.0F, -13.0F, 19.0F, 1.0F, 13.0F, 12.0F, 0.0F, false);
		head_exterior.setTextureOffset(259, 181).addCuboid(20.0F, 0.0F, -16.0F, 1.0F, 4.0F, 35.0F, 0.0F, false);
		head_exterior.setTextureOffset(240, 51).addCuboid(-21.0F, 0.0F, -16.0F, 1.0F, 4.0F, 35.0F, 0.0F, false);

		driver_door_upper_2_r1 = new ModelPart(this);
		driver_door_upper_2_r1.setPivot(-21.0F, -13.0F, 0.0F);
		head_exterior.addChild(driver_door_upper_2_r1);
		setRotationAngle(driver_door_upper_2_r1, 0.0F, 0.0F, 0.1107F);
		driver_door_upper_2_r1.setTextureOffset(0, 213).addCuboid(0.0F, -20.0F, 19.0F, 1.0F, 20.0F, 12.0F, 0.0F, false);

		upper_wall_2_r4 = new ModelPart(this);
		upper_wall_2_r4.setPivot(-20.0F, -13.0F, 0.0F);
		head_exterior.addChild(upper_wall_2_r4);
		setRotationAngle(upper_wall_2_r4, 0.0F, 0.0F, 0.1107F);
		upper_wall_2_r4.setTextureOffset(80, 126).addCuboid(0.0F, -20.0F, -18.0F, 0.0F, 20.0F, 37.0F, 0.0F, false);

		driver_door_upper_1_r1 = new ModelPart(this);
		driver_door_upper_1_r1.setPivot(21.0F, -13.0F, 0.0F);
		head_exterior.addChild(driver_door_upper_1_r1);
		setRotationAngle(driver_door_upper_1_r1, 0.0F, 0.0F, -0.1107F);
		driver_door_upper_1_r1.setTextureOffset(172, 231).addCuboid(-1.0F, -20.0F, 19.0F, 1.0F, 20.0F, 12.0F, 0.0F, false);

		upper_wall_1_r6 = new ModelPart(this);
		upper_wall_1_r6.setPivot(20.0F, -13.0F, 0.0F);
		head_exterior.addChild(upper_wall_1_r6);
		setRotationAngle(upper_wall_1_r6, 0.0F, 0.0F, -0.1107F);
		upper_wall_1_r6.setTextureOffset(76, 215).addCuboid(0.0F, -20.0F, -18.0F, 0.0F, 20.0F, 37.0F, 0.0F, false);

		front = new ModelPart(this);
		front.setPivot(0.0F, 0.0F, 0.0F);
		head_exterior.addChild(front);
		front.setTextureOffset(170, 127).addCuboid(6.0F, -1.75F, 30.25F, 13.0F, 3.0F, 8.0F, 0.0F, false);
		front.setTextureOffset(170, 116).addCuboid(-19.0F, -1.75F, 30.25F, 13.0F, 3.0F, 8.0F, 0.0F, false);

		head_roof_r1 = new ModelPart(this);
		head_roof_r1.setPivot(0.0F, -0.175F, -3.75F);
		front.addChild(head_roof_r1);
		setRotationAngle(head_roof_r1, -0.1309F, 0.0F, 0.0F);
		head_roof_r1.setTextureOffset(116, 116).addCuboid(-6.0F, -45.75F, 27.0F, 12.0F, 1.0F, 4.0F, 0.0F, false);

		head_top_r1 = new ModelPart(this);
		head_top_r1.setPivot(0.0F, 0.0F, 0.0F);
		front.addChild(head_top_r1);
		setRotationAngle(head_top_r1, 0.1745F, 0.0F, 0.0F);
		head_top_r1.setTextureOffset(80, 196).addCuboid(-6.0F, -35.175F, 35.675F, 12.0F, 11.0F, 4.0F, 0.0F, false);

		head_bottom_3_r1 = new ModelPart(this);
		head_bottom_3_r1.setPivot(0.0F, 0.0F, 0.0F);
		front.addChild(head_bottom_3_r1);
		setRotationAngle(head_bottom_3_r1, 0.0F, 1.4486F, 0.0F);
		head_bottom_3_r1.setTextureOffset(170, 138).addCuboid(-37.625F, -3.55F, 23.6F, 10.0F, 5.0F, 0.0F, 0.0F, false);

		head_bottom_2_r1 = new ModelPart(this);
		head_bottom_2_r1.setPivot(0.0F, 0.0F, 0.0F);
		front.addChild(head_bottom_2_r1);
		setRotationAngle(head_bottom_2_r1, 0.0F, -1.4486F, 0.0F);
		head_bottom_2_r1.setTextureOffset(182, 163).addCuboid(27.375F, -3.5F, 23.675F, 10.0F, 5.0F, 0.0F, 0.0F, false);

		head_bottom_1_r1 = new ModelPart(this);
		head_bottom_1_r1.setPivot(0.0F, 0.0F, 0.0F);
		front.addChild(head_bottom_1_r1);
		setRotationAngle(head_bottom_1_r1, -0.3054F, 0.0F, 0.0F);
		head_bottom_1_r1.setTextureOffset(58, 49).addCuboid(-19.05F, -15.075F, 36.9F, 38.0F, 5.0F, 0.0F, 0.0F, false);

		side_1 = new ModelPart(this);
		side_1.setPivot(0.0F, 0.0F, 0.0F);
		front.addChild(side_1);
		side_1.setTextureOffset(320, 341).addCuboid(0.0F, -42.0F, 16.0F, 6.0F, 1.0F, 10.0F, 0.0F, false);
		side_1.setTextureOffset(138, 51).addCuboid(0.0F, -42.0F, 26.0F, 6.0F, 1.0F, 3.0F, 0.0F, false);

		outer_head_7_r1 = new ModelPart(this);
		outer_head_7_r1.setPivot(0.0F, -3.05F, -0.5F);
		side_1.addChild(outer_head_7_r1);
		setRotationAngle(outer_head_7_r1, 0.1745F, 0.0F, 0.0F);
		outer_head_7_r1.setTextureOffset(154, 163).addCuboid(6.0F, 0.825F, 39.675F, 14.0F, 6.0F, 0.0F, 0.0F, false);

		outer_head_6_r1 = new ModelPart(this);
		outer_head_6_r1.setPivot(-15.1778F, -40.0378F, -3.825F);
		side_1.addChild(outer_head_6_r1);
		setRotationAngle(outer_head_6_r1, -0.1309F, 0.0F, 0.4887F);
		outer_head_6_r1.setTextureOffset(139, 50).addCuboid(25.375F, -18.325F, 30.7F, 3.0F, 1.0F, 4.0F, 0.0F, false);

		outer_head_5_r1 = new ModelPart(this);
		outer_head_5_r1.setPivot(9.9394F, -41.4814F, -3.825F);
		side_1.addChild(outer_head_5_r1);
		setRotationAngle(outer_head_5_r1, -0.1309F, 0.0F, 0.1745F);
		outer_head_5_r1.setTextureOffset(134, 49).addCuboid(-3.975F, -4.1F, 32.575F, 8.0F, 1.0F, 4.0F, 0.0F, false);

		outer_head_4_r1 = new ModelPart(this);
		outer_head_4_r1.setPivot(0.0F, 0.0F, 0.0F);
		side_1.addChild(outer_head_4_r1);
		setRotationAngle(outer_head_4_r1, 0.1745F, 0.0F, 0.0F);
		outer_head_4_r1.setTextureOffset(186, 231).addCuboid(6.0F, -35.175F, 39.675F, 12.0F, 9.0F, 0.0F, 0.0F, false);
		outer_head_4_r1.setTextureOffset(136, 67).addCuboid(6.0F, -26.175F, 39.675F, 14.0F, 27.0F, 0.0F, 0.0F, false);

		outer_head_2_r1 = new ModelPart(this);
		outer_head_2_r1.setPivot(0.0F, 0.0F, 0.0F);
		side_1.addChild(outer_head_2_r1);
		setRotationAngle(outer_head_2_r1, 0.0F, -0.0873F, -0.1107F);
		outer_head_2_r1.setTextureOffset(74, 217).addCuboid(24.0F, -33.5F, 28.75F, 0.0F, 23.0F, 9.0F, 0.0F, false);

		outer_head_1_r1 = new ModelPart(this);
		outer_head_1_r1.setPivot(0.0F, 0.0F, 0.0F);
		side_1.addChild(outer_head_1_r1);
		setRotationAngle(outer_head_1_r1, 0.0F, -0.0873F, 0.0F);
		outer_head_1_r1.setTextureOffset(168, 190).addCuboid(22.75F, -13.25F, 29.0F, 0.0F, 12.0F, 9.0F, 0.0F, false);

		outer_roof_11_r1 = new ModelPart(this);
		outer_roof_11_r1.setPivot(16.9769F, -38.2468F, -0.05F);
		side_1.addChild(outer_roof_11_r1);
		setRotationAngle(outer_roof_11_r1, -0.0553F, -0.1376F, 0.8717F);
		outer_roof_11_r1.setTextureOffset(198, 236).addCuboid(2.2F, -1.1F, 30.375F, 3.0F, 0.0F, 3.0F, 0.0F, true);

		outer_roof_10_r1 = new ModelPart(this);
		outer_roof_10_r1.setPivot(16.9769F, -38.2468F, -0.05F);
		side_1.addChild(outer_roof_10_r1);
		setRotationAngle(outer_roof_10_r1, -0.1048F, -0.1051F, 1.2748F);
		outer_roof_10_r1.setTextureOffset(198, 236).addCuboid(4.175F, -3.025F, 30.425F, 2.0F, 0.0F, 3.0F, 0.0F, true);

		outer_roof_8_r1 = new ModelPart(this);
		outer_roof_8_r1.setPivot(9.9394F, -41.3064F, 0.0F);
		side_1.addChild(outer_roof_8_r1);
		setRotationAngle(outer_roof_8_r1, 0.0F, 0.0F, 0.1745F);
		outer_roof_8_r1.setTextureOffset(135, 51).addCuboid(-4.0F, 0.0F, 26.0F, 8.0F, 1.0F, 3.0F, 0.0F, false);
		outer_roof_8_r1.setTextureOffset(316, 341).addCuboid(-4.0F, 0.0F, 16.0F, 8.0F, 1.0F, 10.0F, 0.0F, false);

		outer_roof_7_r1 = new ModelPart(this);
		outer_roof_7_r1.setPivot(15.1778F, -39.8628F, 0.0F);
		side_1.addChild(outer_roof_7_r1);
		setRotationAngle(outer_roof_7_r1, 0.0F, 0.0F, 0.5236F);
		outer_roof_7_r1.setTextureOffset(140, 51).addCuboid(-1.5F, 0.0F, 26.0F, 3.0F, 1.0F, 3.0F, 0.0F, false);
		outer_roof_7_r1.setTextureOffset(326, 341).addCuboid(-1.5F, 0.0F, 16.0F, 3.0F, 1.0F, 10.0F, 0.0F, false);

		outer_roof_3_r4 = new ModelPart(this);
		outer_roof_3_r4.setPivot(16.9769F, -38.2468F, 0.0F);
		side_1.addChild(outer_roof_3_r4);
		setRotationAngle(outer_roof_3_r4, 0.0F, 0.0F, 1.0472F);
		outer_roof_3_r4.setTextureOffset(318, 336).addCuboid(-1.0F, 0.0F, 16.0F, 2.0F, 1.0F, 15.0F, 0.0F, false);

		outer_roof_2_r4 = new ModelPart(this);
		outer_roof_2_r4.setPivot(17.5872F, -36.3872F, 0.0F);
		side_1.addChild(outer_roof_2_r4);
		setRotationAngle(outer_roof_2_r4, 0.0F, 0.0F, -0.1107F);
		outer_roof_2_r4.setTextureOffset(320, 335).addCuboid(-1.0F, -1.0F, 16.0F, 1.0F, 2.0F, 15.0F, 0.0F, false);

		outer_roof_1_r2 = new ModelPart(this);
		outer_roof_1_r2.setPivot(20.0F, -13.0F, 5.0F);
		side_1.addChild(outer_roof_1_r2);
		setRotationAngle(outer_roof_1_r2, 0.0F, 0.0F, -0.1107F);
		outer_roof_1_r2.setTextureOffset(17, 82).addCuboid(-1.0F, -23.0F, 11.0F, 2.0F, 4.0F, 15.0F, 0.0F, false);

		side_2 = new ModelPart(this);
		side_2.setPivot(0.0F, 0.0F, 0.0F);
		front.addChild(side_2);
		side_2.setTextureOffset(320, 341).addCuboid(-6.0F, -42.0F, 16.0F, 6.0F, 1.0F, 10.0F, 0.0F, false);
		side_2.setTextureOffset(136, 50).addCuboid(-6.0F, -42.0F, 26.0F, 6.0F, 1.0F, 3.0F, 0.0F, false);

		outer_head_7_r2 = new ModelPart(this);
		outer_head_7_r2.setPivot(0.0F, -3.05F, -0.5F);
		side_2.addChild(outer_head_7_r2);
		setRotationAngle(outer_head_7_r2, 0.1745F, 0.0F, 0.0F);
		outer_head_7_r2.setTextureOffset(148, 116).addCuboid(-20.0F, 0.825F, 39.675F, 14.0F, 6.0F, 0.0F, 0.0F, false);

		outer_head_6_r2 = new ModelPart(this);
		outer_head_6_r2.setPivot(-15.1778F, -40.0378F, -3.825F);
		side_2.addChild(outer_head_6_r2);
		setRotationAngle(outer_head_6_r2, -0.1309F, 0.0F, -0.4712F);
		outer_head_6_r2.setTextureOffset(141, 50).addCuboid(-1.55F, -4.175F, 31.575F, 3.0F, 1.0F, 5.0F, 0.0F, false);

		outer_head_5_r2 = new ModelPart(this);
		outer_head_5_r2.setPivot(9.9394F, -41.4814F, -3.825F);
		side_2.addChild(outer_head_5_r2);
		setRotationAngle(outer_head_5_r2, -0.1309F, 0.0F, -0.1745F);
		outer_head_5_r2.setTextureOffset(134, 50).addCuboid(-23.575F, -7.525F, 32.075F, 8.0F, 1.0F, 4.0F, 0.0F, false);

		outer_head_4_r2 = new ModelPart(this);
		outer_head_4_r2.setPivot(0.0F, 0.0F, 0.0F);
		side_2.addChild(outer_head_4_r2);
		setRotationAngle(outer_head_4_r2, 0.1745F, 0.0F, 0.0F);
		outer_head_4_r2.setTextureOffset(158, 217).addCuboid(-18.0F, -35.175F, 39.675F, 12.0F, 9.0F, 0.0F, 0.0F, false);
		outer_head_4_r2.setTextureOffset(174, 51).addCuboid(-20.0F, -26.175F, 39.675F, 14.0F, 27.0F, 0.0F, 0.0F, false);

		outer_head_2_r2 = new ModelPart(this);
		outer_head_2_r2.setPivot(0.0F, 0.0F, 0.0F);
		side_2.addChild(outer_head_2_r2);
		setRotationAngle(outer_head_2_r2, 0.0F, 0.0873F, 0.1107F);
		outer_head_2_r2.setTextureOffset(204, 42).addCuboid(-24.0F, -33.5F, 28.75F, 0.0F, 23.0F, 9.0F, 0.0F, false);

		outer_head_1_r2 = new ModelPart(this);
		outer_head_1_r2.setPivot(0.0F, 0.0F, 0.0F);
		side_2.addChild(outer_head_1_r2);
		setRotationAngle(outer_head_1_r2, 0.0F, 0.0873F, 0.0F);
		outer_head_1_r2.setTextureOffset(132, 189).addCuboid(-22.75F, -14.0F, 29.0F, 0.0F, 11.0F, 9.0F, 0.0F, false);

		outer_roof_11_r2 = new ModelPart(this);
		outer_roof_11_r2.setPivot(-16.9769F, -38.2468F, -0.05F);
		side_2.addChild(outer_roof_11_r2);
		setRotationAngle(outer_roof_11_r2, -0.0553F, 0.1376F, -0.8717F);
		outer_roof_11_r2.setTextureOffset(198, 236).addCuboid(-5.2F, -1.1F, 30.375F, 3.0F, 0.0F, 3.0F, 0.0F, false);

		outer_roof_10_r2 = new ModelPart(this);
		outer_roof_10_r2.setPivot(-16.9769F, -38.2468F, -0.05F);
		side_2.addChild(outer_roof_10_r2);
		setRotationAngle(outer_roof_10_r2, -0.1048F, 0.1051F, -1.2748F);
		outer_roof_10_r2.setTextureOffset(198, 236).addCuboid(-6.175F, -3.025F, 30.425F, 2.0F, 0.0F, 3.0F, 0.0F, false);

		outer_roof_8_r2 = new ModelPart(this);
		outer_roof_8_r2.setPivot(-9.9394F, -41.3064F, 0.0F);
		side_2.addChild(outer_roof_8_r2);
		setRotationAngle(outer_roof_8_r2, 0.0F, 0.0F, -0.1745F);
		outer_roof_8_r2.setTextureOffset(134, 50).addCuboid(-4.0F, 0.0F, 26.0F, 8.0F, 1.0F, 3.0F, 0.0F, false);
		outer_roof_8_r2.setTextureOffset(316, 341).addCuboid(-4.0F, 0.0F, 16.0F, 8.0F, 1.0F, 10.0F, 0.0F, false);

		outer_roof_7_r2 = new ModelPart(this);
		outer_roof_7_r2.setPivot(-15.1778F, -39.8628F, 0.0F);
		side_2.addChild(outer_roof_7_r2);
		setRotationAngle(outer_roof_7_r2, 0.0F, 0.0F, -0.5236F);
		outer_roof_7_r2.setTextureOffset(139, 51).addCuboid(-1.5F, 0.0F, 26.0F, 3.0F, 1.0F, 3.0F, 0.0F, false);
		outer_roof_7_r2.setTextureOffset(326, 341).addCuboid(-1.5F, 0.0F, 16.0F, 3.0F, 1.0F, 10.0F, 0.0F, false);

		outer_roof_3_r5 = new ModelPart(this);
		outer_roof_3_r5.setPivot(-16.9769F, -38.2468F, 0.0F);
		side_2.addChild(outer_roof_3_r5);
		setRotationAngle(outer_roof_3_r5, 0.0F, 0.0F, -1.0472F);
		outer_roof_3_r5.setTextureOffset(318, 336).addCuboid(-1.0F, 0.0F, 16.0F, 2.0F, 1.0F, 15.0F, 0.0F, false);

		outer_roof_2_r5 = new ModelPart(this);
		outer_roof_2_r5.setPivot(-17.5872F, -36.3872F, 0.0F);
		side_2.addChild(outer_roof_2_r5);
		setRotationAngle(outer_roof_2_r5, 0.0F, 0.0F, 0.1107F);
		outer_roof_2_r5.setTextureOffset(320, 335).addCuboid(0.0F, -1.0F, 16.0F, 1.0F, 2.0F, 15.0F, 0.0F, false);

		outer_roof_1_r3 = new ModelPart(this);
		outer_roof_1_r3.setPivot(-20.0F, -13.0F, 5.0F);
		side_2.addChild(outer_roof_1_r3);
		setRotationAngle(outer_roof_1_r3, 0.0F, 0.0F, 0.1107F);
		outer_roof_1_r3.setTextureOffset(40, 226).addCuboid(-1.0F, -23.0F, 11.0F, 2.0F, 4.0F, 15.0F, 0.0F, false);

		emergency_door = new ModelPart(this);
		emergency_door.setPivot(0.0F, 0.0F, 0.0F);
		front.addChild(emergency_door);
		emergency_door.setTextureOffset(106, 272).addCuboid(-6.0F, -31.25F, 34.0F, 12.0F, 28.0F, 0.0F, 0.0F, false);
		emergency_door.setTextureOffset(116, 57).addCuboid(6.0F, -30.775F, 31.0F, 0.0F, 28.0F, 10.0F, 0.0F, false);
		emergency_door.setTextureOffset(92, 57).addCuboid(-6.0F, -30.75F, 31.0F, 0.0F, 28.0F, 10.0F, 0.0F, false);
		emergency_door.setTextureOffset(0, 35).addCuboid(-6.0F, -3.25F, 30.0F, 12.0F, 0.0F, 12.0F, 0.0F, false);

		pipe = new ModelPart(this);
		pipe.setPivot(0.0F, -3.05F, -0.5F);
		head_exterior.addChild(pipe);
		

		valve_8_r1 = new ModelPart(this);
		valve_8_r1.setPivot(0.0F, 0.0F, 0.0F);
		pipe.addChild(valve_8_r1);
		setRotationAngle(valve_8_r1, 0.1745F, 0.0F, 0.0F);
		valve_8_r1.setTextureOffset(7, 342).addCuboid(-10.175F, 2.125F, 39.55F, 1.0F, 8.0F, 1.0F, 0.0F, false);
		valve_8_r1.setTextureOffset(2, 334).addCuboid(-10.775F, 9.3F, 39.6F, 2.0F, 3.0F, 1.0F, 0.0F, false);
		valve_8_r1.setTextureOffset(6, 335).addCuboid(-10.775F, -0.7F, 39.6F, 2.0F, 3.0F, 1.0F, 0.0F, false);
		valve_8_r1.setTextureOffset(6, 335).addCuboid(8.675F, -0.7F, 39.6F, 2.0F, 3.0F, 1.0F, 0.0F, false);
		valve_8_r1.setTextureOffset(2, 333).addCuboid(15.325F, -0.7F, 39.6F, 2.0F, 3.0F, 1.0F, 0.0F, false);

		valve_7_r1 = new ModelPart(this);
		valve_7_r1.setPivot(0.0F, 0.0F, 0.0F);
		pipe.addChild(valve_7_r1);
		setRotationAngle(valve_7_r1, 0.0F, 0.0F, -0.1745F);
		valve_7_r1.setTextureOffset(5, 344).addCuboid(8.65F, 2.375F, 40.35F, 1.0F, 5.0F, 1.0F, 0.0F, false);

		valve_6_r1 = new ModelPart(this);
		valve_6_r1.setPivot(0.0F, 0.0F, 0.0F);
		pipe.addChild(valve_6_r1);
		setRotationAngle(valve_6_r1, 0.0F, 0.0F, -1.309F);
		valve_6_r1.setTextureOffset(8, 346).addCuboid(-3.0F, 10.975F, 40.35F, 1.0F, 3.0F, 1.0F, 0.0F, false);

		valve_5_r1 = new ModelPart(this);
		valve_5_r1.setPivot(0.0F, 0.0F, 0.0F);
		pipe.addChild(valve_5_r1);
		setRotationAngle(valve_5_r1, 0.0F, 0.0F, 1.5708F);
		valve_5_r1.setTextureOffset(0, 349).addCuboid(5.375F, -13.325F, 40.3F, 1.0F, 1.0F, 1.0F, 0.0F, false);

		valve_4_r1 = new ModelPart(this);
		valve_4_r1.setPivot(0.0F, 0.0F, 0.0F);
		pipe.addChild(valve_4_r1);
		setRotationAngle(valve_4_r1, 0.0F, 0.0F, 1.309F);
		valve_4_r1.setTextureOffset(4, 343).addCuboid(8.55F, -13.85F, 40.3F, 1.0F, 3.0F, 1.0F, 0.0F, false);

		valve_3_r1 = new ModelPart(this);
		valve_3_r1.setPivot(0.0F, 0.0F, 0.0F);
		pipe.addChild(valve_3_r1);
		setRotationAngle(valve_3_r1, 0.0F, 0.0F, 0.2618F);
		valve_3_r1.setTextureOffset(29, 343).addCuboid(15.8F, -3.625F, 40.3F, 1.0F, 5.0F, 1.0F, 0.0F, false);

		valve_2_r1 = new ModelPart(this);
		valve_2_r1.setPivot(0.0F, 0.0F, 0.0F);
		pipe.addChild(valve_2_r1);
		setRotationAngle(valve_2_r1, 0.1745F, 0.0F, 0.0436F);
		valve_2_r1.setTextureOffset(2, 342).addCuboid(8.95F, 1.775F, 39.65F, 1.0F, 6.0F, 1.0F, 0.0F, false);

		valve_1_r1 = new ModelPart(this);
		valve_1_r1.setPivot(0.0F, 0.0F, 0.0F);
		pipe.addChild(valve_1_r1);
		setRotationAngle(valve_1_r1, 0.1745F, 0.0F, -0.0436F);
		valve_1_r1.setTextureOffset(10, 343).addCuboid(16.1F, 2.8F, 39.4F, 1.0F, 6.0F, 1.0F, 0.0F, false);

		headlights = new ModelPart(this);
		headlights.setPivot(0.0F, 21.0F, -3.975F);


		headlight_2_r1 = new ModelPart(this);
		headlight_2_r1.setPivot(-14.0978F, -10F, 38.6F);
		headlights.addChild(headlight_2_r1);
		setRotationAngle(headlight_2_r1, 0.1745F, 0.0F, 0.0F);
		headlight_2_r1.setTextureOffset(0, 0).addCuboid(-0.5F, 2.5F, 0.0F, 3.0F, 2.0F, 0.0F, 0.0F, false);
		headlight_2_r1.setTextureOffset(0, 0).addCuboid(25.6956F, 2.5F, 0.0F, 3.0F, 2.0F, 0.0F, 0.0F, false);

		tail_lights = new ModelPart(this);
		tail_lights.setPivot(0.0F, 24.0F, 0.0F);


		tail_light_r1 = new ModelPart(this);
		tail_light_r1.setPivot(-14.0978F, -10.0F, 38.5824F);
		tail_lights.addChild(tail_light_r1);
		setRotationAngle(tail_light_r1, 0.1745F, 0.0F, 0.0F);
		tail_light_r1.setTextureOffset(97, 200).addCuboid(15.1F, -31.325F, 0.0F, 4.0F, 3.0F, 0.0F, 0.0F, false);

		door_light = new ModelPart(this);
		door_light.setPivot(0.0F, 24.0F, 0.0F);
		

		outer_roof_1_r4 = new ModelPart(this);
		outer_roof_1_r4.setPivot(-20.0F, -13.0F, 0.0F);
		door_light.addChild(outer_roof_1_r4);
		setRotationAngle(outer_roof_1_r4, 0.0F, 0.0F, 0.1107F);
		outer_roof_1_r4.setTextureOffset(17, 0).addCuboid(-1.1F, -23.0F, -2.0F, 0.0F, 4.0F, 4.0F, 0.0F, false);

		door_light_off = new ModelPart(this);
		door_light_off.setPivot(0.0F, 24.0F, 0.0F);
		

		light_r1 = new ModelPart(this);
		light_r1.setPivot(-20.0F, -13.0F, 0.0F);
		door_light_off.addChild(light_r1);
		setRotationAngle(light_r1, 0.0F, 0.0F, 0.1107F);
		light_r1.setTextureOffset(19, 9).addCuboid(-1.0F, -21.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.4F, false);

		door_light_on = new ModelPart(this);
		door_light_on.setPivot(0.0F, 24.0F, 0.0F);
		

		light_r2 = new ModelPart(this);
		light_r2.setPivot(-20.0F, -13.0F, 0.0F);
		door_light_on.addChild(light_r2);
		setRotationAngle(light_r2, 0.0F, 0.0F, 0.1107F);
		light_r2.setTextureOffset(19, 9).addCuboid(-1.0F, -21.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.4F, false);
}
	private static final int DOOR_MAX = 14;

	@Override
	protected void renderWindowPositions(MatrixStack matrices, VertexConsumer vertices, ModelTrainBase.RenderStage renderStage, int light, int position, boolean renderDetails, boolean isEnd1Head, boolean isEnd2Head) {
		switch (renderStage) {
			case LIGHTS:
				renderMirror(roof_light, matrices, vertices, light, position);
				break;
			case INTERIOR:
				renderOnce(window, matrices, vertices, light, position);
				renderOnceFlipped(window, matrices, vertices, light, position);

				if (renderDetails) {
					renderMirror(roof_window, matrices, vertices, light, position);
					renderMirror(seats, matrices, vertices, light, position);
				}
				break;
			case EXTERIOR:
				renderOnce(window_exterior, matrices, vertices, light, position);
				renderOnceFlipped(window_exterior, matrices, vertices, light, position);
				renderMirror(roof_exterior, matrices, vertices, light, position);
				break;
		}
	}

	@Override
	protected void renderDoorPositions(MatrixStack matrices, VertexConsumer vertices, ModelTrainBase.RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
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
				renderMirror(roof_exterior, matrices, vertices, light, position);

				if (middleDoor && renderDetails) {
					renderMirror(door_light, matrices, vertices, light, position - 22);
					if (!doorOpen) {
						renderMirror(door_light_off, matrices, vertices, light, position - 22);
					}
				}
				break;
			case INTERIOR_TRANSLUCENT:
				renderMirror(side_panel, matrices, vertices, light, position);
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
				renderMirror(roof_exterior, matrices, vertices, light, position);
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
				renderMirror(roof_exterior, matrices, vertices, light, position);
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
				}
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
	protected void renderEndPosition2(MatrixStack matrices, VertexConsumer vertices, ModelTrainBase.RenderStage renderStage, int light, int position, boolean renderDetails) {
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
		return null;
	}

	@Override
	protected ModelDoorOverlayTopBase getModelDoorOverlayTop() {
		return null;
	}

	@Override
	protected int[] getWindowPositions() {
		return new int []{-96, -64, -32, 64, 32, 96};
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
