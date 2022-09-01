package mtr.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mtr.mappings.ModelDataWrapper;
import mtr.mappings.ModelMapper;

public class ModelCMStock extends ModelSimpleTrainBase {

	private final ModelMapper window;
	private final ModelMapper upper_wall_r1;
	private final ModelMapper window_handrails;
	private final ModelMapper handrail_12_r1;
	private final ModelMapper handrail_11_r1;
	private final ModelMapper handrail_10_r1;
	private final ModelMapper handrail_9_r1;
	private final ModelMapper handrail_8_r1;
	private final ModelMapper top_handrail_3_r1;
	private final ModelMapper top_handrail_2_r1;
	private final ModelMapper seat;
	private final ModelMapper seat_back_r1;
	private final ModelMapper window_exterior;
	private final ModelMapper upper_wall_r2;
	private final ModelMapper side_panel_translucent;
	private final ModelMapper roof_window;
	private final ModelMapper inner_roof_2_r1;
	private final ModelMapper roof_door;
	private final ModelMapper inner_roof_2_r2;
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
	private final ModelMapper door_handrail;
	private final ModelMapper handrail_2_r1;
	private final ModelMapper door_exterior;
	private final ModelMapper door_track_r1;
	private final ModelMapper door_left_exterior;
	private final ModelMapper door_left_top_r2;
	private final ModelMapper door_right_exterior;
	private final ModelMapper door_right_top_r2;
	private final ModelMapper end;
	private final ModelMapper upper_wall_2_r1;
	private final ModelMapper upper_wall_1_r1;
	private final ModelMapper end_exterior;
	private final ModelMapper upper_wall_2_r2;
	private final ModelMapper upper_wall_1_r2;
	private final ModelMapper roof_end;
	private final ModelMapper handrail_2_r2;
	private final ModelMapper inner_roof_1;
	private final ModelMapper inner_roof_2_r3;
	private final ModelMapper inner_roof_2;
	private final ModelMapper inner_roof_2_r4;
	private final ModelMapper roof_end_exterior;
	private final ModelMapper vent_2_r1;
	private final ModelMapper vent_1_r1;
	private final ModelMapper outer_roof_1;
	private final ModelMapper outer_roof_5_r2;
	private final ModelMapper outer_roof_4_r2;
	private final ModelMapper outer_roof_3_r2;
	private final ModelMapper outer_roof_2_r2;
	private final ModelMapper outer_roof_1_r2;
	private final ModelMapper outer_roof_2;
	private final ModelMapper outer_roof_5_r3;
	private final ModelMapper outer_roof_4_r3;
	private final ModelMapper outer_roof_3_r3;
	private final ModelMapper outer_roof_2_r3;
	private final ModelMapper outer_roof_1_r3;
	private final ModelMapper roof_light;
	private final ModelMapper light_2_r1;
	private final ModelMapper roof_end_light;
	private final ModelMapper light_r1;
	private final ModelMapper roof_door_light;
	private final ModelMapper light_1_r1;
	private final ModelMapper head;
	private final ModelMapper upper_wall_2_r3;
	private final ModelMapper upper_wall_1_r3;
	private final ModelMapper head_exterior;
	private final ModelMapper driver_door_upper_2_r1;
	private final ModelMapper driver_door_upper_1_r1;
	private final ModelMapper front;
	private final ModelMapper bottom_r1;
	private final ModelMapper front_bottom_right_r1;
	private final ModelMapper front_middle_top_r1;
	private final ModelMapper front_panel_r1;
	private final ModelMapper side_1;
	private final ModelMapper front_side_bottom_1_r1;
	private final ModelMapper front_bottom_right_r2;
	private final ModelMapper outer_roof_4_r4;
	private final ModelMapper outer_roof_2_r4;
	private final ModelMapper outer_roof_1_r4;
	private final ModelMapper outer_roof_3_r4;
	private final ModelMapper front_side_lower_1_r1;
	private final ModelMapper front_side_upper_1_r1;
	private final ModelMapper side_2;
	private final ModelMapper front_side_bottom_2_r1;
	private final ModelMapper outer_roof_4_r5;
	private final ModelMapper outer_roof_7_r1;
	private final ModelMapper outer_roof_5_r4;
	private final ModelMapper outer_roof_5_r5;
	private final ModelMapper front_side_upper_2_r1;
	private final ModelMapper front_side_lower_2_r1;
	private final ModelMapper coupler;
	private final ModelMapper headlight_panel_left;
	private final ModelMapper headlight_panel_bottom_r1;
	private final ModelMapper headlight_panel_main_r1;
	private final ModelMapper headlight_panel_right;
	private final ModelMapper headlight_panel_bottom_r2;
	private final ModelMapper headlight_panel_main_r2;
	private final ModelMapper headlights;
	private final ModelMapper tail_lights;
	private final ModelMapper door_light;
	private final ModelMapper outer_roof_1_r5;
	private final ModelMapper door_light_on;
	private final ModelMapper light_r2;
	private final ModelMapper door_light_off;
	private final ModelMapper light_r3;

	public ModelCMStock() {
		final int textureWidth = 320;
		final int textureHeight = 320;

		final ModelDataWrapper modelDataWrapper = new ModelDataWrapper(this, textureWidth, textureHeight);

		window = new ModelMapper(modelDataWrapper);
		window.setPos(0, 24, 0);
		window.texOffs(0, 0).addBox(-20, 0, -24, 20, 1, 48, 0, false);
		window.texOffs(136, 109).addBox(-20, -14, -26, 2, 14, 52, 0, false);

		upper_wall_r1 = new ModelMapper(modelDataWrapper);
		upper_wall_r1.setPos(-20, -14, 0);
		window.addChild(upper_wall_r1);
		setRotationAngle(upper_wall_r1, 0, 0, 0.1107F);
		upper_wall_r1.texOffs(0, 82).addBox(0, -19, -26, 2, 19, 52, 0, false);

		window_handrails = new ModelMapper(modelDataWrapper);
		window_handrails.setPos(0, 24, 0);
		window_handrails.texOffs(317, 0).addBox(-11, -27.2F, -22, 0, 22, 0, 0.2F, false);
		window_handrails.texOffs(317, 0).addBox(-11, -27.2F, 22, 0, 22, 0, 0.2F, false);
		window_handrails.texOffs(317, 0).addBox(-12.461F, -32.504F, 12.2F, 0, 3, 0, 0.2F, false);
		window_handrails.texOffs(317, 0).addBox(-12.461F, -32.504F, -12.2F, 0, 3, 0, 0.2F, false);
		window_handrails.texOffs(317, 0).addBox(0, -33, -9, 0, 33, 0, 0.2F, false);
		window_handrails.texOffs(317, 0).addBox(0, -33, 9, 0, 33, 0, 0.2F, false);
		window_handrails.texOffs(19, 14).addBox(-1, -32, 21, 2, 4, 0, 0, false);
		window_handrails.texOffs(19, 14).addBox(-1, -32, 15, 2, 4, 0, 0, false);
		window_handrails.texOffs(19, 14).addBox(-1, -32, 3, 2, 4, 0, 0, false);
		window_handrails.texOffs(19, 14).addBox(-1, -32, -3, 2, 4, 0, 0, false);
		window_handrails.texOffs(19, 14).addBox(-1, -32, -15, 2, 4, 0, 0, false);
		window_handrails.texOffs(19, 14).addBox(-13.575F, -29.725F, 8.525F, 2, 4, 0, 0, false);
		window_handrails.texOffs(19, 14).addBox(-13.575F, -29.725F, 0.475F, 2, 4, 0, 0, false);
		window_handrails.texOffs(19, 14).addBox(-13.575F, -29.725F, -8.525F, 2, 4, 0, 0, false);
		window_handrails.texOffs(19, 14).addBox(-1, -32, -21, 2, 4, 0, 0, false);

		handrail_12_r1 = new ModelMapper(modelDataWrapper);
		handrail_12_r1.setPos(-14.9052F, -3.8479F, 22);
		window_handrails.addChild(handrail_12_r1);
		setRotationAngle(handrail_12_r1, 0, 0, 1.4835F);
		handrail_12_r1.texOffs(317, 0).addBox(0, -3, 0, 0, 6, 0, 0.2F, false);

		handrail_11_r1 = new ModelMapper(modelDataWrapper);
		handrail_11_r1.setPos(-10.8F, -5, 22.2F);
		window_handrails.addChild(handrail_11_r1);
		setRotationAngle(handrail_11_r1, 0, 0, 0.6981F);
		handrail_11_r1.texOffs(317, 0).addBox(-0.2F, 0.2F, -0.2F, 0, 1, 0, 0.2F, false);

		handrail_10_r1 = new ModelMapper(modelDataWrapper);
		handrail_10_r1.setPos(-11.0106F, -4.9415F, 0);
		window_handrails.addChild(handrail_10_r1);
		setRotationAngle(handrail_10_r1, 0, 0, 1.4835F);
		handrail_10_r1.texOffs(317, 0).addBox(0.75F, 0.975F, -22, 0, 6, 0, 0.2F, false);

		handrail_9_r1 = new ModelMapper(modelDataWrapper);
		handrail_9_r1.setPos(-10.8F, -5, -22.2F);
		window_handrails.addChild(handrail_9_r1);
		setRotationAngle(handrail_9_r1, 0, 0, 0.6981F);
		handrail_9_r1.texOffs(317, 0).addBox(-0.2F, 0.2F, 0.2F, 0, 1, 0, 0.2F, false);

		handrail_8_r1 = new ModelMapper(modelDataWrapper);
		handrail_8_r1.setPos(0, 0, 0);
		window_handrails.addChild(handrail_8_r1);
		setRotationAngle(handrail_8_r1, -1.5708F, 0, 0);
		handrail_8_r1.texOffs(317, 0).addBox(0, -24, -31.5F, 0, 48, 0, 0.2F, false);

		top_handrail_3_r1 = new ModelMapper(modelDataWrapper);
		top_handrail_3_r1.setPos(-10.8F, -27.4F, 22.2F);
		window_handrails.addChild(top_handrail_3_r1);
		setRotationAngle(top_handrail_3_r1, 0, 0, -0.6545F);
		top_handrail_3_r1.texOffs(317, 0).addBox(-0.2F, -2.2F, -0.2F, 0, 2, 0, 0.2F, false);
		top_handrail_3_r1.texOffs(317, 0).addBox(-0.2F, -2.2F, -44.2F, 0, 2, 0, 0.2F, false);

		top_handrail_2_r1 = new ModelMapper(modelDataWrapper);
		top_handrail_2_r1.setPos(-12.461F, -29.104F, 0);
		window_handrails.addChild(top_handrail_2_r1);
		setRotationAngle(top_handrail_2_r1, -1.5708F, 0, 0);
		top_handrail_2_r1.texOffs(317, 0).addBox(0, -22, 0, 0, 44, 0, 0.2F, false);

		seat = new ModelMapper(modelDataWrapper);
		seat.setPos(0, 0, 0);
		window_handrails.addChild(seat);
		seat.texOffs(0, 180).addBox(-18, -6, -22, 7, 1, 44, 0, false);
		seat.texOffs(60, 189).addBox(-18, -5, -21, 6, 5, 42, 0, false);

		seat_back_r1 = new ModelMapper(modelDataWrapper);
		seat_back_r1.setPos(-17, -6, 0);
		seat.addChild(seat_back_r1);
		setRotationAngle(seat_back_r1, 0, 0, -0.0524F);
		seat_back_r1.texOffs(192, 79).addBox(-1, -8, -22, 1, 8, 44, 0, false);

		window_exterior = new ModelMapper(modelDataWrapper);
		window_exterior.setPos(0, 24, 0);
		window_exterior.texOffs(160, 175).addBox(-21, 0, -24, 1, 4, 48, 0, false);
		window_exterior.texOffs(104, 123).addBox(-20, -14, -26, 0, 14, 52, 0, false);

		upper_wall_r2 = new ModelMapper(modelDataWrapper);
		upper_wall_r2.setPos(-20, -14, 0);
		window_exterior.addChild(upper_wall_r2);
		setRotationAngle(upper_wall_r2, 0, 0, 0.1107F);
		upper_wall_r2.texOffs(0, 109).addBox(0, -19, -26, 0, 19, 52, 0, false);

		side_panel_translucent = new ModelMapper(modelDataWrapper);
		side_panel_translucent.setPos(0, 24, 0);
		side_panel_translucent.texOffs(0, 0).addBox(-18, -28, 0, 7, 24, 0, 0, false);

		roof_window = new ModelMapper(modelDataWrapper);
		roof_window.setPos(0, 24, 0);
		roof_window.texOffs(68, 0).addBox(-16, -32, -24, 3, 0, 48, 0, false);
		roof_window.texOffs(40, 0).addBox(-10, -34, -24, 10, 0, 48, 0, false);

		inner_roof_2_r1 = new ModelMapper(modelDataWrapper);
		inner_roof_2_r1.setPos(-13, -32, 0);
		roof_window.addChild(inner_roof_2_r1);
		setRotationAngle(inner_roof_2_r1, 0, 0, -0.5236F);
		inner_roof_2_r1.texOffs(60, 0).addBox(0, 0, -24, 4, 0, 48, 0, false);

		roof_door = new ModelMapper(modelDataWrapper);
		roof_door.setPos(0, 24, 0);
		roof_door.texOffs(108, 111).addBox(-19, -32, -16, 6, 0, 32, 0, false);
		roof_door.texOffs(56, 7).addBox(-10, -34, -16, 10, 0, 32, 0, false);

		inner_roof_2_r2 = new ModelMapper(modelDataWrapper);
		inner_roof_2_r2.setPos(-13, -32, 0);
		roof_door.addChild(inner_roof_2_r2);
		setRotationAngle(inner_roof_2_r2, 0, 0, -0.5236F);
		inner_roof_2_r2.texOffs(0, 0).addBox(0, 0, -16, 4, 0, 32, 0, false);

		roof_exterior = new ModelMapper(modelDataWrapper);
		roof_exterior.setPos(0, 24, 0);
		roof_exterior.texOffs(0, 82).addBox(-6, -42, -20, 6, 0, 40, 0, false);

		outer_roof_5_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_5_r1.setPos(-9.9394F, -41.3064F, 0);
		roof_exterior.addChild(outer_roof_5_r1);
		setRotationAngle(outer_roof_5_r1, 0, 0, -0.1745F);
		outer_roof_5_r1.texOffs(82, 0).addBox(-4, 0, -20, 8, 0, 40, 0, false);

		outer_roof_4_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_4_r1.setPos(-15.1778F, -39.8628F, 0);
		roof_exterior.addChild(outer_roof_4_r1);
		setRotationAngle(outer_roof_4_r1, 0, 0, -0.5236F);
		outer_roof_4_r1.texOffs(0, 0).addBox(-1.5F, 0, -20, 3, 0, 40, 0, false);

		outer_roof_3_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_3_r1.setPos(-16.9769F, -38.2468F, 0);
		roof_exterior.addChild(outer_roof_3_r1);
		setRotationAngle(outer_roof_3_r1, 0, 0, -1.0472F);
		outer_roof_3_r1.texOffs(98, 0).addBox(-1, 0, -20, 2, 0, 40, 0, false);

		outer_roof_2_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_2_r1.setPos(-17.5872F, -36.3872F, 0);
		roof_exterior.addChild(outer_roof_2_r1);
		setRotationAngle(outer_roof_2_r1, 0, 0, 0.1107F);
		outer_roof_2_r1.texOffs(0, 113).addBox(0, -1, -20, 0, 2, 40, 0, false);

		outer_roof_1_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_1_r1.setPos(-20, -14, 0);
		roof_exterior.addChild(outer_roof_1_r1);
		setRotationAngle(outer_roof_1_r1, 0, 0, 0.1107F);
		outer_roof_1_r1.texOffs(116, 196).addBox(-0.075F, -22, -20, 1, 4, 40, 0, false);

		door = new ModelMapper(modelDataWrapper);
		door.setPos(0, 24, 0);
		door.texOffs(16, 16).addBox(-20, 0, -16, 20, 1, 32, 0, false);

		door_left = new ModelMapper(modelDataWrapper);
		door_left.setPos(0, 0, 0);
		door.addChild(door_left);
		door_left.texOffs(56, 236).addBox(-20.8F, -14, 0, 1, 14, 15, 0, false);

		door_left_top_r1 = new ModelMapper(modelDataWrapper);
		door_left_top_r1.setPos(-20.8F, -14, 0);
		door_left.addChild(door_left_top_r1);
		setRotationAngle(door_left_top_r1, 0, 0, 0.1107F);
		door_left_top_r1.texOffs(78, 272).addBox(0, -19, 0, 1, 19, 15, 0, false);

		door_right = new ModelMapper(modelDataWrapper);
		door_right.setPos(0, 0, 0);
		door.addChild(door_right);
		door_right.texOffs(0, 9).addBox(-20.8F, -14, -15, 1, 14, 15, 0, false);

		door_right_top_r1 = new ModelMapper(modelDataWrapper);
		door_right_top_r1.setPos(-20.8F, -14, 0);
		door_right.addChild(door_right_top_r1);
		setRotationAngle(door_right_top_r1, 0, 0, 0.1107F);
		door_right_top_r1.texOffs(0, 49).addBox(0, -19, -15, 1, 19, 15, 0, false);

		door_handrail = new ModelMapper(modelDataWrapper);
		door_handrail.setPos(0, 24, 0);
		door_handrail.texOffs(317, 0).addBox(0, -34, 0, 0, 34, 0, 0.2F, false);

		handrail_2_r1 = new ModelMapper(modelDataWrapper);
		handrail_2_r1.setPos(0, 0, 0);
		door_handrail.addChild(handrail_2_r1);
		setRotationAngle(handrail_2_r1, -1.5708F, 0, 0);
		handrail_2_r1.texOffs(317, 0).addBox(0, -16, -31.5F, 0, 32, 0, 0.2F, false);

		door_exterior = new ModelMapper(modelDataWrapper);
		door_exterior.setPos(0, 24, 0);
		door_exterior.texOffs(56, 236).addBox(-21, 0, -16, 1, 4, 32, 0, false);

		door_track_r1 = new ModelMapper(modelDataWrapper);
		door_track_r1.setPos(-20, -14, 0);
		door_exterior.addChild(door_track_r1);
		setRotationAngle(door_track_r1, 0, 0, 0.1107F);
		door_track_r1.texOffs(59, 49).addBox(-1, -22, -29, 1, 4, 58, 0, false);

		door_left_exterior = new ModelMapper(modelDataWrapper);
		door_left_exterior.setPos(0, 0, 0);
		door_exterior.addChild(door_left_exterior);
		door_left_exterior.texOffs(210, 194).addBox(-20.8F, -14, 0, 0, 14, 15, 0, false);

		door_left_top_r2 = new ModelMapper(modelDataWrapper);
		door_left_top_r2.setPos(-20.8F, -14, 0);
		door_left_exterior.addChild(door_left_top_r2);
		setRotationAngle(door_left_top_r2, 0, 0, 0.1107F);
		door_left_top_r2.texOffs(192, 88).addBox(0, -19, 0, 0, 19, 15, 0, false);

		door_right_exterior = new ModelMapper(modelDataWrapper);
		door_right_exterior.setPos(0, 0, 0);
		door_exterior.addChild(door_right_exterior);
		door_right_exterior.texOffs(104, 146).addBox(-20.8F, -14, -15, 0, 14, 15, 0, false);

		door_right_top_r2 = new ModelMapper(modelDataWrapper);
		door_right_top_r2.setPos(-20.8F, -14, 0);
		door_right_exterior.addChild(door_right_top_r2);
		setRotationAngle(door_right_top_r2, 0, 0, 0.1107F);
		door_right_top_r2.texOffs(0, 68).addBox(0, -19, -15, 0, 19, 15, 0, false);

		end = new ModelMapper(modelDataWrapper);
		end.setPos(0, 24, 0);
		end.texOffs(192, 131).addBox(-20, 0, -12, 40, 1, 20, 0, false);
		end.texOffs(56, 111).addBox(18, -14, 7, 2, 14, 3, 0, false);
		end.texOffs(30, 82).addBox(-20, -14, 7, 2, 14, 3, 0, false);
		end.texOffs(225, 156).addBox(9.5F, -34, -12, 9, 34, 19, 0, false);
		end.texOffs(0, 225).addBox(-18.5F, -34, -12, 9, 34, 19, 0, false);

		upper_wall_2_r1 = new ModelMapper(modelDataWrapper);
		upper_wall_2_r1.setPos(-20, -14, 0);
		end.addChild(upper_wall_2_r1);
		setRotationAngle(upper_wall_2_r1, 0, 0, 0.1107F);
		upper_wall_2_r1.texOffs(152, 111).addBox(0, -19, 7, 2, 19, 3, 0, false);

		upper_wall_1_r1 = new ModelMapper(modelDataWrapper);
		upper_wall_1_r1.setPos(20, -14, 0);
		end.addChild(upper_wall_1_r1);
		setRotationAngle(upper_wall_1_r1, 0, 0, -0.1107F);
		upper_wall_1_r1.texOffs(185, 50).addBox(-2, -19, 7, 2, 19, 3, 0, false);

		end_exterior = new ModelMapper(modelDataWrapper);
		end_exterior.setPos(0, 24, 0);
		end_exterior.texOffs(36, 272).addBox(20, 0, -12, 1, 4, 20, 0, false);
		end_exterior.texOffs(270, 213).addBox(-21, 0, -12, 1, 4, 20, 0, false);
		end_exterior.texOffs(248, 239).addBox(18, -14, -12, 2, 14, 22, 0, false);
		end_exterior.texOffs(140, 121).addBox(-20, -14, -12, 2, 14, 22, 0, false);
		end_exterior.texOffs(0, 278).addBox(9.5F, -34, -12, 9, 34, 0, 0, false);
		end_exterior.texOffs(166, 0).addBox(-18.5F, -34, -12, 9, 34, 0, 0, false);
		end_exterior.texOffs(205, 40).addBox(-18, -41, -12, 36, 7, 0, 0, false);

		upper_wall_2_r2 = new ModelMapper(modelDataWrapper);
		upper_wall_2_r2.setPos(-20, -14, 0);
		end_exterior.addChild(upper_wall_2_r2);
		setRotationAngle(upper_wall_2_r2, 0, 0, 0.1107F);
		upper_wall_2_r2.texOffs(122, 240).addBox(0, -19, -12, 2, 19, 22, 0, false);

		upper_wall_1_r2 = new ModelMapper(modelDataWrapper);
		upper_wall_1_r2.setPos(20, -14, 0);
		end_exterior.addChild(upper_wall_1_r2);
		setRotationAngle(upper_wall_1_r2, 0, 0, -0.1107F);
		upper_wall_1_r2.texOffs(170, 240).addBox(-2, -19, -12, 2, 19, 22, 0, false);

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
		inner_roof_1.texOffs(103, 50).addBox(-17, 1, -12, 6, 0, 36, 0, true);
		inner_roof_1.texOffs(67, 49).addBox(-8, -1, -28, 10, 0, 52, 0, false);

		inner_roof_2_r3 = new ModelMapper(modelDataWrapper);
		inner_roof_2_r3.setPos(-11, 1, -16);
		inner_roof_1.addChild(inner_roof_2_r3);
		setRotationAngle(inner_roof_2_r3, 0, 0, -0.5236F);
		inner_roof_2_r3.texOffs(106, 0).addBox(0, 0, 4, 4, 0, 36, 0, true);

		inner_roof_2 = new ModelMapper(modelDataWrapper);
		inner_roof_2.setPos(-2, -33, 16);
		roof_end.addChild(inner_roof_2);
		inner_roof_2.texOffs(103, 50).addBox(15, 1, -12, 6, 0, 36, 0, false);
		inner_roof_2.texOffs(67, 49).addBox(2, -1, -28, 10, 0, 52, 0, true);

		inner_roof_2_r4 = new ModelMapper(modelDataWrapper);
		inner_roof_2_r4.setPos(15, 1, -16);
		inner_roof_2.addChild(inner_roof_2_r4);
		setRotationAngle(inner_roof_2_r4, 0, 0, 0.5236F);
		inner_roof_2_r4.texOffs(106, 0).addBox(-4, 0, 4, 4, 0, 36, 0, false);

		roof_end_exterior = new ModelMapper(modelDataWrapper);
		roof_end_exterior.setPos(0, 24, 0);
		roof_end_exterior.texOffs(60, 111).addBox(-8, -43, 0, 16, 2, 48, 0, false);

		vent_2_r1 = new ModelMapper(modelDataWrapper);
		vent_2_r1.setPos(-8, -43, 0);
		roof_end_exterior.addChild(vent_2_r1);
		setRotationAngle(vent_2_r1, 0, 0, -0.3491F);
		vent_2_r1.texOffs(119, 53).addBox(-9, 0, 0, 9, 2, 48, 0, false);

		vent_1_r1 = new ModelMapper(modelDataWrapper);
		vent_1_r1.setPos(8, -43, 0);
		roof_end_exterior.addChild(vent_1_r1);
		setRotationAngle(vent_1_r1, 0, 0, 0.3491F);
		vent_1_r1.texOffs(139, 0).addBox(0, 0, 0, 9, 2, 48, 0, false);

		outer_roof_1 = new ModelMapper(modelDataWrapper);
		outer_roof_1.setPos(0, 0, 0);
		roof_end_exterior.addChild(outer_roof_1);
		outer_roof_1.texOffs(56, 111).addBox(-6, -42, -12, 6, 1, 20, 0, false);

		outer_roof_5_r2 = new ModelMapper(modelDataWrapper);
		outer_roof_5_r2.setPos(-9.7656F, -40.3206F, 0);
		outer_roof_1.addChild(outer_roof_5_r2);
		setRotationAngle(outer_roof_5_r2, 0, 0, -0.1745F);
		outer_roof_5_r2.texOffs(130, 20).addBox(-4, -1, -12, 8, 1, 20, 0, false);

		outer_roof_4_r2 = new ModelMapper(modelDataWrapper);
		outer_roof_4_r2.setPos(-14.6775F, -38.9948F, 0);
		outer_roof_1.addChild(outer_roof_4_r2);
		setRotationAngle(outer_roof_4_r2, 0, 0, -0.5236F);
		outer_roof_4_r2.texOffs(158, 194).addBox(-1.5F, -1, -12, 3, 1, 20, 0, false);

		outer_roof_3_r2 = new ModelMapper(modelDataWrapper);
		outer_roof_3_r2.setPos(-16.1105F, -37.7448F, 0);
		outer_roof_1.addChild(outer_roof_3_r2);
		setRotationAngle(outer_roof_3_r2, 0, 0, -1.0472F);
		outer_roof_3_r2.texOffs(261, 27).addBox(-1, -1, -12, 2, 1, 20, 0, false);

		outer_roof_2_r2 = new ModelMapper(modelDataWrapper);
		outer_roof_2_r2.setPos(-17.587F, -36.3849F, 0);
		outer_roof_1.addChild(outer_roof_2_r2);
		setRotationAngle(outer_roof_2_r2, 0, 0, 0.1107F);
		outer_roof_2_r2.texOffs(274, 237).addBox(0, -1, -12, 1, 2, 20, 0, false);

		outer_roof_1_r2 = new ModelMapper(modelDataWrapper);
		outer_roof_1_r2.setPos(-20, -14, 0);
		outer_roof_1.addChild(outer_roof_1_r2);
		setRotationAngle(outer_roof_1_r2, 0, 0, 0.1107F);
		outer_roof_1_r2.texOffs(261, 189).addBox(-0.075F, -22, -12, 1, 4, 20, 0, false);

		outer_roof_2 = new ModelMapper(modelDataWrapper);
		outer_roof_2.setPos(0, 0, 0);
		roof_end_exterior.addChild(outer_roof_2);
		outer_roof_2.texOffs(0, 102).addBox(0, -42, -12, 6, 1, 20, 0, false);

		outer_roof_5_r3 = new ModelMapper(modelDataWrapper);
		outer_roof_5_r3.setPos(9.7656F, -40.3206F, 0);
		outer_roof_2.addChild(outer_roof_5_r3);
		setRotationAngle(outer_roof_5_r3, 0, 0, 0.1745F);
		outer_roof_5_r3.texOffs(56, 82).addBox(-4, -1, -12, 8, 1, 20, 0, false);

		outer_roof_4_r3 = new ModelMapper(modelDataWrapper);
		outer_roof_4_r3.setPos(14.6775F, -38.9948F, 0);
		outer_roof_2.addChild(outer_roof_4_r3);
		setRotationAngle(outer_roof_4_r3, 0, 0, 0.5236F);
		outer_roof_4_r3.texOffs(185, 79).addBox(-1.5F, -1, -12, 3, 1, 20, 0, false);

		outer_roof_3_r3 = new ModelMapper(modelDataWrapper);
		outer_roof_3_r3.setPos(16.1105F, -37.7448F, 0);
		outer_roof_2.addChild(outer_roof_3_r3);
		setRotationAngle(outer_roof_3_r3, 0, 0, 1.0472F);
		outer_roof_3_r3.texOffs(148, 240).addBox(-1, -1, -12, 2, 1, 20, 0, false);

		outer_roof_2_r3 = new ModelMapper(modelDataWrapper);
		outer_roof_2_r3.setPos(17.587F, -36.3849F, 0);
		outer_roof_2.addChild(outer_roof_2_r3);
		setRotationAngle(outer_roof_2_r3, 0, 0, -0.1107F);
		outer_roof_2_r3.texOffs(262, 152).addBox(-1, -1, -12, 1, 2, 20, 0, false);

		outer_roof_1_r3 = new ModelMapper(modelDataWrapper);
		outer_roof_1_r3.setPos(20, -14, 0);
		outer_roof_2.addChild(outer_roof_1_r3);
		setRotationAngle(outer_roof_1_r3, 0, 0, -0.1107F);
		outer_roof_1_r3.texOffs(90, 236).addBox(-0.925F, -22, -12, 1, 4, 20, 0, false);

		roof_light = new ModelMapper(modelDataWrapper);
		roof_light.setPos(0, 24, 0);


		light_2_r1 = new ModelMapper(modelDataWrapper);
		light_2_r1.setPos(0, 0, 0);
		roof_light.addChild(light_2_r1);
		setRotationAngle(light_2_r1, 0, -1.5708F, 0);
		light_2_r1.texOffs(2, 0).addBox(-14.875F, -33.975F, -10, 2, 0, 20, 0, false);
		light_2_r1.texOffs(2, 0).addBox(12.125F, -33.975F, -10, 2, 0, 20, 0, false);

		roof_end_light = new ModelMapper(modelDataWrapper);
		roof_end_light.setPos(0, 24, 0);


		light_r1 = new ModelMapper(modelDataWrapper);
		light_r1.setPos(0, 0, 0);
		roof_end_light.addChild(light_r1);
		setRotationAngle(light_r1, 0, -1.5708F, 0);
		light_r1.texOffs(0, 0).addBox(24, -33.975F, -13.975F, 2, 0, 24, 0, false);

		roof_door_light = new ModelMapper(modelDataWrapper);
		roof_door_light.setPos(0, 24, 0);


		light_1_r1 = new ModelMapper(modelDataWrapper);
		light_1_r1.setPos(0, 0, 0);
		roof_door_light.addChild(light_1_r1);
		setRotationAngle(light_1_r1, 0, -1.5708F, 0);
		light_1_r1.texOffs(2, 0).addBox(-2, -33.975F, -10, 2, 0, 20, 0, false);

		head = new ModelMapper(modelDataWrapper);
		head.setPos(0, 24, 0);
		head.texOffs(56, 82).addBox(18, -14, 4, 2, 14, 6, 0, true);
		head.texOffs(56, 82).addBox(-20, -14, 4, 2, 14, 6, 0, false);
		head.texOffs(238, 79).addBox(-18, -34, 4, 36, 34, 0, 0, false);
		head.texOffs(126, 303).addBox(-20, 0, 4, 40, 1, 4, 0, false);

		upper_wall_2_r3 = new ModelMapper(modelDataWrapper);
		upper_wall_2_r3.setPos(-20, -14, 0);
		head.addChild(upper_wall_2_r3);
		setRotationAngle(upper_wall_2_r3, 0, 0, 0.1107F);
		upper_wall_2_r3.texOffs(151, 50).addBox(0, -19, 4, 2, 19, 6, 0, false);

		upper_wall_1_r3 = new ModelMapper(modelDataWrapper);
		upper_wall_1_r3.setPos(20, -14, 0);
		head.addChild(upper_wall_1_r3);
		setRotationAngle(upper_wall_1_r3, 0, 0, -0.1107F);
		upper_wall_1_r3.texOffs(151, 50).addBox(-2, -19, 4, 2, 19, 6, 0, true);

		head_exterior = new ModelMapper(modelDataWrapper);
		head_exterior.setPos(0, 24, 0);
		head_exterior.texOffs(185, 50).addBox(-21, 0, -18, 42, 7, 22, 0, false);
		head_exterior.texOffs(66, 111).addBox(20, 0, 4, 1, 7, 4, 0, true);
		head_exterior.texOffs(66, 111).addBox(-21, 0, 4, 1, 7, 4, 0, false);
		head_exterior.texOffs(114, 194).addBox(18, -14, -9, 2, 14, 19, 0, true);
		head_exterior.texOffs(114, 194).addBox(-20, -14, -9, 2, 14, 19, 0, false);
		head_exterior.texOffs(142, 77).addBox(18, -14, -18, 1, 14, 9, 0, false);
		head_exterior.texOffs(142, 77).addBox(-19, -14, -18, 1, 14, 9, 0, true);
		head_exterior.texOffs(198, 227).addBox(-18, -34, 3, 36, 34, 0, 0, false);

		driver_door_upper_2_r1 = new ModelMapper(modelDataWrapper);
		driver_door_upper_2_r1.setPos(-20, -14, 0);
		head_exterior.addChild(driver_door_upper_2_r1);
		setRotationAngle(driver_door_upper_2_r1, 0, 0, 0.1107F);
		driver_door_upper_2_r1.texOffs(110, 272).addBox(1, -19, -18, 1, 19, 9, 0, true);
		driver_door_upper_2_r1.texOffs(0, 180).addBox(0, -19, -9, 2, 19, 19, 0, false);

		driver_door_upper_1_r1 = new ModelMapper(modelDataWrapper);
		driver_door_upper_1_r1.setPos(20, -14, 0);
		head_exterior.addChild(driver_door_upper_1_r1);
		setRotationAngle(driver_door_upper_1_r1, 0, 0, -0.1107F);
		driver_door_upper_1_r1.texOffs(110, 272).addBox(-2, -19, -18, 1, 19, 9, 0, false);
		driver_door_upper_1_r1.texOffs(0, 180).addBox(-2, -19, -9, 2, 19, 19, 0, true);

		front = new ModelMapper(modelDataWrapper);
		front.setPos(0, 0, 0);
		head_exterior.addChild(front);


		bottom_r1 = new ModelMapper(modelDataWrapper);
		bottom_r1.setPos(0, 5.4768F, 4.6132F);
		front.addChild(bottom_r1);
		setRotationAngle(bottom_r1, -0.0436F, 0, 0);
		bottom_r1.texOffs(0, 49).addBox(-21, 1.525F, -31.55F, 42, 0, 33, 0, false);

		front_bottom_right_r1 = new ModelMapper(modelDataWrapper);
		front_bottom_right_r1.setPos(0, 0, 0);
		front.addChild(front_bottom_right_r1);
		setRotationAngle(front_bottom_right_r1, 0.3491F, 0, 0);
		front_bottom_right_r1.texOffs(92, 93).addBox(7, -9.625F, -26.3F, 12, 7, 0, 0, false);

		front_middle_top_r1 = new ModelMapper(modelDataWrapper);
		front_middle_top_r1.setPos(0, -42, -12);
		front.addChild(front_middle_top_r1);
		setRotationAngle(front_middle_top_r1, 0.3491F, 0, 0);
		front_middle_top_r1.texOffs(0, 123).addBox(-6, 0, -10, 12, 0, 10, 0, false);

		front_panel_r1 = new ModelMapper(modelDataWrapper);
		front_panel_r1.setPos(0, 0, -28);
		front.addChild(front_panel_r1);
		setRotationAngle(front_panel_r1, -0.1745F, 0, 0);
		front_panel_r1.texOffs(205, 0).addBox(-19, -40, 0, 38, 40, 0, 0, false);

		side_1 = new ModelMapper(modelDataWrapper);
		side_1.setPos(0, 0, 0);
		front.addChild(side_1);
		side_1.texOffs(19, 0).addBox(19, -14, -18, 1, 14, 0, 0, false);

		front_side_bottom_1_r1 = new ModelMapper(modelDataWrapper);
		front_side_bottom_1_r1.setPos(21, 0, -13);
		side_1.addChild(front_side_bottom_1_r1);
		setRotationAngle(front_side_bottom_1_r1, 0, 0.1745F, 0.1745F);
		front_side_bottom_1_r1.texOffs(0, 17).addBox(0, 0, -16, 0, 7, 23, 0, false);

		front_bottom_right_r2 = new ModelMapper(modelDataWrapper);
		front_bottom_right_r2.setPos(0, 0, 0);
		side_1.addChild(front_bottom_right_r2);
		setRotationAngle(front_bottom_right_r2, 0.3491F, 0, 0);
		front_bottom_right_r2.texOffs(92, 93).addBox(-19, -9.625F, -26.3F, 12, 7, 0, 0, true);

		outer_roof_4_r4 = new ModelMapper(modelDataWrapper);
		outer_roof_4_r4.setPos(6, -42, -12);
		side_1.addChild(outer_roof_4_r4);
		setRotationAngle(outer_roof_4_r4, 0.3491F, 0, 0.1745F);
		outer_roof_4_r4.texOffs(81, 82).addBox(0, 0, -11, 11, 0, 11, 0, false);

		outer_roof_2_r4 = new ModelMapper(modelDataWrapper);
		outer_roof_2_r4.setPos(17.587F, -36.3849F, 0);
		side_1.addChild(outer_roof_2_r4);
		setRotationAngle(outer_roof_2_r4, 0, 0, -0.1107F);
		outer_roof_2_r4.texOffs(0, 32).addBox(0, -1, -18, 0, 2, 6, 0, false);

		outer_roof_1_r4 = new ModelMapper(modelDataWrapper);
		outer_roof_1_r4.setPos(20, -14, 0);
		side_1.addChild(outer_roof_1_r4);
		setRotationAngle(outer_roof_1_r4, 0, 0, -0.1107F);
		outer_roof_1_r4.texOffs(37, 225).addBox(0, -22, -18, 1, 4, 14, 0, true);
		outer_roof_1_r4.texOffs(17, 0).addBox(-1, -19, -18, 1, 19, 0, 0, false);

		outer_roof_3_r4 = new ModelMapper(modelDataWrapper);
		outer_roof_3_r4.setPos(15.813F, -37.5414F, -17.4163F);
		side_1.addChild(outer_roof_3_r4);
		setRotationAngle(outer_roof_3_r4, 0.1745F, 0, 0.7418F);
		outer_roof_3_r4.texOffs(6, 49).addBox(-3.5F, 0, -5.5F, 7, 0, 11, 0, false);

		front_side_lower_1_r1 = new ModelMapper(modelDataWrapper);
		front_side_lower_1_r1.setPos(20, 0, -18);
		side_1.addChild(front_side_lower_1_r1);
		setRotationAngle(front_side_lower_1_r1, 0, 0.1745F, 0);
		front_side_lower_1_r1.texOffs(184, 183).addBox(0, -14, -11, 0, 20, 11, 0, false);

		front_side_upper_1_r1 = new ModelMapper(modelDataWrapper);
		front_side_upper_1_r1.setPos(20, -14, -18);
		side_1.addChild(front_side_upper_1_r1);
		setRotationAngle(front_side_upper_1_r1, 0, 0.1745F, -0.1107F);
		front_side_upper_1_r1.texOffs(166, 100).addBox(0, -23, -11, 0, 23, 11, 0, false);

		side_2 = new ModelMapper(modelDataWrapper);
		side_2.setPos(-21, 0, -9);
		front.addChild(side_2);
		side_2.texOffs(17, 0).addBox(1, -14, -9, 1, 14, 0, 0, false);

		front_side_bottom_2_r1 = new ModelMapper(modelDataWrapper);
		front_side_bottom_2_r1.setPos(0, 0, -4);
		side_2.addChild(front_side_bottom_2_r1);
		setRotationAngle(front_side_bottom_2_r1, 0, -0.1745F, -0.1745F);
		front_side_bottom_2_r1.texOffs(0, 17).addBox(0, 0, -16, 0, 7, 23, 0, false);

		outer_roof_4_r5 = new ModelMapper(modelDataWrapper);
		outer_roof_4_r5.setPos(5.187F, -37.5414F, -8.4163F);
		side_2.addChild(outer_roof_4_r5);
		setRotationAngle(outer_roof_4_r5, 0.1745F, 0, -0.7418F);
		outer_roof_4_r5.texOffs(6, 49).addBox(-3.5F, 0, -5.5F, 7, 0, 11, 0, true);

		outer_roof_7_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_7_r1.setPos(3.413F, -36.3849F, 9);
		side_2.addChild(outer_roof_7_r1);
		setRotationAngle(outer_roof_7_r1, 0, 0, 0.1107F);
		outer_roof_7_r1.texOffs(0, 32).addBox(0, -1, -18, 0, 2, 6, 0, false);

		outer_roof_5_r4 = new ModelMapper(modelDataWrapper);
		outer_roof_5_r4.setPos(15, -42, -3);
		side_2.addChild(outer_roof_5_r4);
		setRotationAngle(outer_roof_5_r4, 0.3491F, 0, -0.1745F);
		outer_roof_5_r4.texOffs(81, 82).addBox(-11, 0, -11, 11, 0, 11, 0, true);

		outer_roof_5_r5 = new ModelMapper(modelDataWrapper);
		outer_roof_5_r5.setPos(1, -14, 9);
		side_2.addChild(outer_roof_5_r5);
		setRotationAngle(outer_roof_5_r5, 0, 0, 0.1107F);
		outer_roof_5_r5.texOffs(37, 225).addBox(-1, -22, -18, 1, 4, 14, 0, false);
		outer_roof_5_r5.texOffs(17, 0).addBox(0, -19, -18, 1, 19, 0, 0, false);

		front_side_upper_2_r1 = new ModelMapper(modelDataWrapper);
		front_side_upper_2_r1.setPos(1, -14, -9);
		side_2.addChild(front_side_upper_2_r1);
		setRotationAngle(front_side_upper_2_r1, 0, -0.1745F, 0.1107F);
		front_side_upper_2_r1.texOffs(166, 100).addBox(0, -23, -11, 0, 23, 11, 0, false);

		front_side_lower_2_r1 = new ModelMapper(modelDataWrapper);
		front_side_lower_2_r1.setPos(1, 0, -9);
		side_2.addChild(front_side_lower_2_r1);
		setRotationAngle(front_side_lower_2_r1, 0, -0.1745F, 0);
		front_side_lower_2_r1.texOffs(184, 183).addBox(0, -14, -11, 0, 20, 11, 0, false);

		coupler = new ModelMapper(modelDataWrapper);
		coupler.setPos(0, 0, 0);
		front.addChild(coupler);
		coupler.texOffs(260, 275).addBox(-4, 0.55F, -29, 8, 6, 11, 0, false);

		headlight_panel_left = new ModelMapper(modelDataWrapper);
		headlight_panel_left.setPos(0, 0, 0);
		head_exterior.addChild(headlight_panel_left);


		headlight_panel_bottom_r1 = new ModelMapper(modelDataWrapper);
		headlight_panel_bottom_r1.setPos(0, 0, 0);
		headlight_panel_left.addChild(headlight_panel_bottom_r1);
		setRotationAngle(headlight_panel_bottom_r1, -1.3963F, 0, 0);
		headlight_panel_bottom_r1.texOffs(122, 41).addBox(-17, 21.475F, 1.1F, 10, 6, 1, 0, false);

		headlight_panel_main_r1 = new ModelMapper(modelDataWrapper);
		headlight_panel_main_r1.setPos(-12, 3.849F, -27.2491F);
		headlight_panel_left.addChild(headlight_panel_main_r1);
		setRotationAngle(headlight_panel_main_r1, 0.3491F, 0, 0);
		headlight_panel_main_r1.texOffs(144, 41).addBox(-5, -3, -0.5F, 10, 6, 1, 0, false);

		headlight_panel_right = new ModelMapper(modelDataWrapper);
		headlight_panel_right.setPos(0, 0, 0);
		head_exterior.addChild(headlight_panel_right);


		headlight_panel_bottom_r2 = new ModelMapper(modelDataWrapper);
		headlight_panel_bottom_r2.setPos(0, 0, 0);
		headlight_panel_right.addChild(headlight_panel_bottom_r2);
		setRotationAngle(headlight_panel_bottom_r2, -1.3963F, 0, 0);
		headlight_panel_bottom_r2.texOffs(122, 41).addBox(7, 21.475F, 1.1F, 10, 6, 1, 0, true);

		headlight_panel_main_r2 = new ModelMapper(modelDataWrapper);
		headlight_panel_main_r2.setPos(12, 3.849F, -27.2491F);
		headlight_panel_right.addChild(headlight_panel_main_r2);
		setRotationAngle(headlight_panel_main_r2, 0.3491F, 0, 0);
		headlight_panel_main_r2.texOffs(144, 41).addBox(-5, -3, -0.5F, 10, 6, 1, 0, true);

		headlights = new ModelMapper(modelDataWrapper);
		headlights.setPos(0, 24, 0);
		setRotationAngle(headlights, 0.3491F, 0, 0);
		headlights.texOffs(17, 19).addBox(8.5F, -7.2029F, -27.5223F, 2, 2, 0, 0, true);
		headlights.texOffs(17, 19).addBox(-10.5F, -7.2029F, -27.5223F, 2, 2, 0, 0, false);

		tail_lights = new ModelMapper(modelDataWrapper);
		tail_lights.setPos(0, 24, 0);
		setRotationAngle(tail_lights, 0.3491F, 0, 0);
		tail_lights.texOffs(32, 36).addBox(11.75F, -7.1029F, -27.4723F, 4, 2, 0, 0, true);
		tail_lights.texOffs(32, 36).addBox(-15.775F, -7.0029F, -27.4723F, 4, 2, 0, 0, false);

		door_light = new ModelMapper(modelDataWrapper);
		door_light.setPos(0, 24, 0);


		outer_roof_1_r5 = new ModelMapper(modelDataWrapper);
		outer_roof_1_r5.setPos(-20, -14, 0);
		door_light.addChild(outer_roof_1_r5);
		setRotationAngle(outer_roof_1_r5, 0, 0, 0.1107F);
		outer_roof_1_r5.texOffs(32, 28).addBox(-1.1F, -22, -2, 0, 4, 4, 0, false);

		door_light_on = new ModelMapper(modelDataWrapper);
		door_light_on.setPos(0, 24, 0);


		light_r2 = new ModelMapper(modelDataWrapper);
		light_r2.setPos(-20, -14, 0);
		door_light_on.addChild(light_r2);
		setRotationAngle(light_r2, 0, 0, 0.1107F);
		light_r2.texOffs(0, 50).addBox(-1, -20, 0, 0, 0, 0, 0.4F, false);

		door_light_off = new ModelMapper(modelDataWrapper);
		door_light_off.setPos(0, 24, 0);


		light_r3 = new ModelMapper(modelDataWrapper);
		light_r3.setPos(-20, -14, 0);
		door_light_off.addChild(light_r3);
		setRotationAngle(light_r3, 0, 0, 0.1107F);
		light_r3.texOffs(0, 52).addBox(-1, -20, 0, 0, 0, 0, 0.4F, false);

		modelDataWrapper.setModelPart(textureWidth, textureHeight);
		window.setModelPart();
		window_handrails.setModelPart();
		window_exterior.setModelPart();
		side_panel_translucent.setModelPart();
		roof_window.setModelPart();
		roof_door.setModelPart();
		roof_exterior.setModelPart();
		door.setModelPart();
		door_left.setModelPart(door.name);
		door_right.setModelPart(door.name);
		door_handrail.setModelPart();
		door_exterior.setModelPart();
		door_left_exterior.setModelPart(door_exterior.name);
		door_right_exterior.setModelPart(door_exterior.name);
		end.setModelPart();
		end_exterior.setModelPart();
		roof_end.setModelPart();
		roof_end_exterior.setModelPart();
		roof_light.setModelPart();
		roof_end_light.setModelPart();
		roof_door_light.setModelPart();
		head.setModelPart();
		head_exterior.setModelPart();
		headlights.setModelPart();
		tail_lights.setModelPart();
		door_light.setModelPart();
		door_light_on.setModelPart();
		door_light_off.setModelPart();
	}

	private static final int DOOR_MAX = 14;
	private static final ModelDoorOverlay MODEL_DOOR_OVERLAY = new ModelDoorOverlay(DOOR_MAX, 6.34F, "door_overlay_cm_stock_left.png", "door_overlay_cm_stock_right.png");

	@Override
	protected void renderWindowPositions(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
		switch (renderStage) {
			case LIGHTS:
				renderOnce(roof_light, matrices, vertices, light, position);
				break;
			case INTERIOR:
				renderMirror(window, matrices, vertices, light, position);
				if (renderDetails) {
					renderMirror(window_handrails, matrices, vertices, light, position);
					renderMirror(roof_window, matrices, vertices, light, position);
				}
				break;
			case INTERIOR_TRANSLUCENT:
				renderMirror(side_panel_translucent, matrices, vertices, light, position - 22);
				renderMirror(side_panel_translucent, matrices, vertices, light, position + 22);
				break;
			case EXTERIOR:
				renderMirror(window_exterior, matrices, vertices, light, position);
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
				if (notLastDoor) {
					renderOnce(roof_door_light, matrices, vertices, light, position);
				}
				if (middleDoor && doorOpen) {
					renderMirror(door_light_on, matrices, vertices, light, position - 23);
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
					renderOnce(door_handrail, matrices, vertices, light, position);
					if (notLastDoor) {
						renderMirror(roof_door, matrices, vertices, light, position);
					}
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
					renderMirror(door_light, matrices, vertices, light, position - 23);
					if (!doorOpen) {
						renderMirror(door_light_off, matrices, vertices, light, position - 23);
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
				renderOnce(useHeadlights ? headlights : tail_lights, matrices, vertices, light, position);
				break;
			case INTERIOR:
				renderOnce(head, matrices, vertices, light, position);
				if (renderDetails) {
					renderOnce(roof_end, matrices, vertices, light, position);
				}
				break;
			case EXTERIOR:
				renderOnce(head_exterior, matrices, vertices, light, position);
				renderOnce(roof_end_exterior, matrices, vertices, light, position);
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
			case EXTERIOR:
				renderOnce(end_exterior, matrices, vertices, light, position);
				renderOnce(roof_end_exterior, matrices, vertices, light, position);
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
		return null;
	}

	@Override
	protected int[] getWindowPositions() {
		return new int[]{-120, -40, 40, 120};
	}

	@Override
	protected int[] getDoorPositions() {
		return new int[]{-160, -80, 0, 80, 160};
	}

	@Override
	protected int[] getEndPositions() {
		return new int[]{-184, 184};
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