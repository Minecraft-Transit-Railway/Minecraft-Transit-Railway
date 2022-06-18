package mtr.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mtr.mappings.ModelDataWrapper;
import mtr.mappings.ModelMapper;

public class ModelLondonUndergroundD78 extends ModelSimpleTrainBase {

	private final ModelMapper window;
	private final ModelMapper window_1;
	private final ModelMapper roof_side_r1;
	private final ModelMapper window_bottom_r1;
	private final ModelMapper window_3;
	private final ModelMapper roof_side_r2;
	private final ModelMapper window_bottom_r2;
	private final ModelMapper window_2_partial;
	private final ModelMapper window_13;
	private final ModelMapper window_bottom_r3;
	private final ModelMapper window_4;
	private final ModelMapper window_bottom_r4;
	private final ModelMapper window_exterior;
	private final ModelMapper window_2;
	private final ModelMapper roof_3_r1;
	private final ModelMapper roof_2_r1;
	private final ModelMapper roof_1_r1;
	private final ModelMapper window_bottom_r5;
	private final ModelMapper window_6;
	private final ModelMapper roof_4_r1;
	private final ModelMapper roof_3_r2;
	private final ModelMapper roof_2_r2;
	private final ModelMapper window_bottom_r6;
	private final ModelMapper window_exterior_2_partial;
	private final ModelMapper window_15;
	private final ModelMapper window_bottom_r7;
	private final ModelMapper window_8;
	private final ModelMapper window_bottom_r8;
	private final ModelMapper logo;
	private final ModelMapper logo_r1;
	private final ModelMapper door;
	private final ModelMapper roof_side_r3;
	private final ModelMapper door_top_r1;
	private final ModelMapper door_sliding_1;
	private final ModelMapper door_sliding_1_part;
	private final ModelMapper door_top_r2;
	private final ModelMapper door_sliding_2;
	private final ModelMapper door_sliding_2_part;
	private final ModelMapper door_top_r3;
	private final ModelMapper door_handrails;
	private final ModelMapper handrail_2_r1;
	private final ModelMapper handrail_5_r1;
	private final ModelMapper door_exterior;
	private final ModelMapper roof_3_r3;
	private final ModelMapper roof_2_r3;
	private final ModelMapper roof_1_r2;
	private final ModelMapper door_top_r4;
	private final ModelMapper door_sliding_exterior_1;
	private final ModelMapper door_sliding_exterior_1_part;
	private final ModelMapper door_top_r5;
	private final ModelMapper door_sliding_exterior_2;
	private final ModelMapper door_sliding_exterior_2_part;
	private final ModelMapper door_top_r6;
	private final ModelMapper side_seats;
	private final ModelMapper seat_1;
	private final ModelMapper handrail_5_r2;
	private final ModelMapper handrail_2_r2;
	private final ModelMapper seat_back_r1;
	private final ModelMapper seat_2;
	private final ModelMapper handrail_6_r1;
	private final ModelMapper handrail_3_r1;
	private final ModelMapper seat_back_r2;
	private final ModelMapper middle_seats;
	private final ModelMapper seat_3;
	private final ModelMapper handrail_2_r3;
	private final ModelMapper handrail_5_r3;
	private final ModelMapper seat_back_2_r1;
	private final ModelMapper seat_back_1_r1;
	private final ModelMapper seat_4;
	private final ModelMapper handrail_3_r2;
	private final ModelMapper handrail_6_r2;
	private final ModelMapper seat_back_3_r1;
	private final ModelMapper seat_back_2_r2;
	private final ModelMapper side_panel;
	private final ModelMapper handrail_1_r1;
	private final ModelMapper side_panel_translucent;
	private final ModelMapper light_window;
	private final ModelMapper cube_r1;
	private final ModelMapper cube_r2;
	private final ModelMapper light_door;
	private final ModelMapper cube_r3;
	private final ModelMapper light_end;
	private final ModelMapper cube_r4;
	private final ModelMapper cube_r5;
	private final ModelMapper light_head;
	private final ModelMapper cube_r6;
	private final ModelMapper cube_r7;
	private final ModelMapper side_seats_end;
	private final ModelMapper seat_5;
	private final ModelMapper handrail_6_r3;
	private final ModelMapper handrail_3_r3;
	private final ModelMapper seat_back_r3;
	private final ModelMapper seat_6;
	private final ModelMapper handrail_7_r1;
	private final ModelMapper handrail_4_r1;
	private final ModelMapper seat_back_r4;
	private final ModelMapper side_seats_head;
	private final ModelMapper seat_7;
	private final ModelMapper handrail_7_r2;
	private final ModelMapper handrail_4_r2;
	private final ModelMapper seat_back_r5;
	private final ModelMapper seat_8;
	private final ModelMapper handrail_8_r1;
	private final ModelMapper handrail_5_r4;
	private final ModelMapper seat_back_r6;
	private final ModelMapper end;
	private final ModelMapper window_5;
	private final ModelMapper roof_side_r4;
	private final ModelMapper window_bottom_r9;
	private final ModelMapper window_10;
	private final ModelMapper roof_side_r5;
	private final ModelMapper window_bottom_r10;
	private final ModelMapper end_exterior;
	private final ModelMapper window_7;
	private final ModelMapper roof_3_r4;
	private final ModelMapper roof_2_r4;
	private final ModelMapper roof_1_r3;
	private final ModelMapper back_wall_r1;
	private final ModelMapper window_bottom_r11;
	private final ModelMapper window_12;
	private final ModelMapper roof_4_r2;
	private final ModelMapper roof_3_r5;
	private final ModelMapper roof_2_r5;
	private final ModelMapper back_wall_r2;
	private final ModelMapper window_bottom_r12;
	private final ModelMapper head;
	private final ModelMapper window_11;
	private final ModelMapper roof_side_r6;
	private final ModelMapper window_bottom_r13;
	private final ModelMapper window_14;
	private final ModelMapper roof_side_r7;
	private final ModelMapper window_bottom_r14;
	private final ModelMapper head_exterior;
	private final ModelMapper window_9;
	private final ModelMapper door_top_r7;
	private final ModelMapper roof_4_r3;
	private final ModelMapper roof_3_r6;
	private final ModelMapper roof_2_r6;
	private final ModelMapper back_wall_r3;
	private final ModelMapper window_16;
	private final ModelMapper door_top_r8;
	private final ModelMapper roof_5_r1;
	private final ModelMapper roof_4_r4;
	private final ModelMapper roof_3_r7;
	private final ModelMapper back_wall_r4;
	private final ModelMapper headlights;
	private final ModelMapper tail_lights;

	public ModelLondonUndergroundD78() {
		final int textureWidth = 272;
		final int textureHeight = 272;

		final ModelDataWrapper modelDataWrapper = new ModelDataWrapper(this, textureWidth, textureHeight);

		window = new ModelMapper(modelDataWrapper);
		window.setPos(0, 24, 0);


		window_1 = new ModelMapper(modelDataWrapper);
		window_1.setPos(0, 0, 0);
		window.addChild(window_1);
		window_1.texOffs(88, 0).addBox(-21, 0, 0, 21, 1, 23, 0, false);
		window_1.texOffs(28, 0).addBox(-13, -39, 0, 13, 0, 23, 0, false);

		roof_side_r1 = new ModelMapper(modelDataWrapper);
		roof_side_r1.setPos(-15.5823F, -35.5941F, 11.5F);
		window_1.addChild(roof_side_r1);
		setRotationAngle(roof_side_r1, 0, 0, 0.4363F);
		roof_side_r1.texOffs(188, 80).addBox(0, -3, -11.5F, 0, 6, 23, 0, false);

		window_bottom_r1 = new ModelMapper(modelDataWrapper);
		window_bottom_r1.setPos(-21, 0, 0);
		window_1.addChild(window_bottom_r1);
		setRotationAngle(window_bottom_r1, 0, 0, 0.0349F);
		window_bottom_r1.texOffs(99, 93).addBox(0, -33, 0, 3, 33, 23, 0, false);

		window_3 = new ModelMapper(modelDataWrapper);
		window_3.setPos(0, 0, 0);
		window.addChild(window_3);
		window_3.texOffs(88, 0).addBox(0, 0, 0, 21, 1, 23, 0, true);
		window_3.texOffs(28, 0).addBox(0, -39, 0, 13, 0, 23, 0, true);

		roof_side_r2 = new ModelMapper(modelDataWrapper);
		roof_side_r2.setPos(15.5823F, -35.5941F, 11.5F);
		window_3.addChild(roof_side_r2);
		setRotationAngle(roof_side_r2, 0, 0, -0.4363F);
		roof_side_r2.texOffs(188, 80).addBox(0, -3, -11.5F, 0, 6, 23, 0, true);

		window_bottom_r2 = new ModelMapper(modelDataWrapper);
		window_bottom_r2.setPos(21, 0, 0);
		window_3.addChild(window_bottom_r2);
		setRotationAngle(window_bottom_r2, 0, 0, -0.0349F);
		window_bottom_r2.texOffs(99, 93).addBox(-3, -33, 0, 3, 33, 23, 0, true);

		window_2_partial = new ModelMapper(modelDataWrapper);
		window_2_partial.setPos(0, 24, 0);


		window_13 = new ModelMapper(modelDataWrapper);
		window_13.setPos(0, 0, 0);
		window_2_partial.addChild(window_13);


		window_bottom_r3 = new ModelMapper(modelDataWrapper);
		window_bottom_r3.setPos(-21, 0, 0);
		window_13.addChild(window_bottom_r3);
		setRotationAngle(window_bottom_r3, 0, 0, 0.0349F);
		window_bottom_r3.texOffs(0, 126).addBox(3, -33, 0, 0, 33, 23, 0, false);

		window_4 = new ModelMapper(modelDataWrapper);
		window_4.setPos(0, 0, 0);
		window_2_partial.addChild(window_4);


		window_bottom_r4 = new ModelMapper(modelDataWrapper);
		window_bottom_r4.setPos(21, 0, 0);
		window_4.addChild(window_bottom_r4);
		setRotationAngle(window_bottom_r4, 0, 0, -0.0349F);
		window_bottom_r4.texOffs(0, 126).addBox(-3, -33, 0, 0, 33, 23, 0, true);

		window_exterior = new ModelMapper(modelDataWrapper);
		window_exterior.setPos(0, 24, 0);


		window_2 = new ModelMapper(modelDataWrapper);
		window_2.setPos(0, 0, 0);
		window_exterior.addChild(window_2);
		window_2.texOffs(203, 204).addBox(-21, 0, 0, 1, 1, 23, 0, false);
		window_2.texOffs(189, 27).addBox(-20, 1, 0, 1, 4, 23, 0, false);
		window_2.texOffs(0, 0).addBox(-3.8669F, -42.7901F, 0, 4, 0, 23, 0, false);

		roof_3_r1 = new ModelMapper(modelDataWrapper);
		roof_3_r1.setPos(-8.7907F, -41.9219F, 0);
		window_2.addChild(roof_3_r1);
		setRotationAngle(roof_3_r1, 0, 0, 1.3963F);
		roof_3_r1.texOffs(150, 55).addBox(0, -5, 0, 0, 7, 23, 0, false);

		roof_2_r1 = new ModelMapper(modelDataWrapper);
		roof_2_r1.setPos(-14.2241F, -39.5747F, 0);
		window_2.addChild(roof_2_r1);
		setRotationAngle(roof_2_r1, 0, 0, 1.0472F);
		roof_2_r1.texOffs(72, 87).addBox(0, -4, 0, 0, 6, 23, 0, false);

		roof_1_r1 = new ModelMapper(modelDataWrapper);
		roof_1_r1.setPos(-18.8485F, -35.1277F, 0);
		window_2.addChild(roof_1_r1);
		setRotationAngle(roof_1_r1, 0, 0, 0.6981F);
		roof_1_r1.texOffs(150, 62).addBox(0, -4.5F, 0, 0, 6, 23, 0, false);

		window_bottom_r5 = new ModelMapper(modelDataWrapper);
		window_bottom_r5.setPos(-21, 0, 0);
		window_2.addChild(window_bottom_r5);
		setRotationAngle(window_bottom_r5, 0, 0, 0.0349F);
		window_bottom_r5.texOffs(128, 126).addBox(0, -34, 0, 1, 34, 23, 0, false);

		window_6 = new ModelMapper(modelDataWrapper);
		window_6.setPos(0, 0, 0);
		window_exterior.addChild(window_6);
		window_6.texOffs(203, 204).addBox(20, 0, 0, 1, 1, 23, 0, true);
		window_6.texOffs(189, 27).addBox(19, 1, 0, 1, 4, 23, 0, true);
		window_6.texOffs(0, 0).addBox(-0.1331F, -42.7901F, 0, 4, 0, 23, 0, true);

		roof_4_r1 = new ModelMapper(modelDataWrapper);
		roof_4_r1.setPos(8.7907F, -41.9219F, 0);
		window_6.addChild(roof_4_r1);
		setRotationAngle(roof_4_r1, 0, 0, -1.3963F);
		roof_4_r1.texOffs(150, 55).addBox(0, -5, 0, 0, 7, 23, 0, true);

		roof_3_r2 = new ModelMapper(modelDataWrapper);
		roof_3_r2.setPos(14.2241F, -39.5747F, 0);
		window_6.addChild(roof_3_r2);
		setRotationAngle(roof_3_r2, 0, 0, -1.0472F);
		roof_3_r2.texOffs(72, 87).addBox(0, -4, 0, 0, 6, 23, 0, true);

		roof_2_r2 = new ModelMapper(modelDataWrapper);
		roof_2_r2.setPos(18.8485F, -35.1277F, 0);
		window_6.addChild(roof_2_r2);
		setRotationAngle(roof_2_r2, 0, 0, -0.6981F);
		roof_2_r2.texOffs(150, 62).addBox(0, -4.5F, 0, 0, 6, 23, 0, true);

		window_bottom_r6 = new ModelMapper(modelDataWrapper);
		window_bottom_r6.setPos(21, 0, 0);
		window_6.addChild(window_bottom_r6);
		setRotationAngle(window_bottom_r6, 0, 0, -0.0349F);
		window_bottom_r6.texOffs(128, 126).addBox(-1, -34, 0, 1, 34, 23, 0, true);

		window_exterior_2_partial = new ModelMapper(modelDataWrapper);
		window_exterior_2_partial.setPos(0, 24, 0);


		window_15 = new ModelMapper(modelDataWrapper);
		window_15.setPos(0, 0, 0);
		window_exterior_2_partial.addChild(window_15);


		window_bottom_r7 = new ModelMapper(modelDataWrapper);
		window_bottom_r7.setPos(-21, 0, 0);
		window_15.addChild(window_bottom_r7);
		setRotationAngle(window_bottom_r7, 0, 0, 0.0349F);
		window_bottom_r7.texOffs(88, 1).addBox(0, -34, 0, 0, 34, 23, 0, false);

		window_8 = new ModelMapper(modelDataWrapper);
		window_8.setPos(0, 0, 0);
		window_exterior_2_partial.addChild(window_8);


		window_bottom_r8 = new ModelMapper(modelDataWrapper);
		window_bottom_r8.setPos(21, 0, 0);
		window_8.addChild(window_bottom_r8);
		setRotationAngle(window_bottom_r8, 0, 0, -0.0349F);
		window_bottom_r8.texOffs(88, 1).addBox(0, -34, 0, 0, 34, 23, 0, true);

		logo = new ModelMapper(modelDataWrapper);
		logo.setPos(0, 24, 0);


		logo_r1 = new ModelMapper(modelDataWrapper);
		logo_r1.setPos(-19, 0, 0);
		logo.addChild(logo_r1);
		setRotationAngle(logo_r1, 0, 0, 0.0349F);
		logo_r1.texOffs(238, 15).addBox(-2.1F, -15, -4, 0, 8, 8, 0, false);

		door = new ModelMapper(modelDataWrapper);
		door.setPos(0, 24, 0);
		door.texOffs(150, 59).addBox(-21, 0, -9, 21, 1, 18, 0, false);
		door.texOffs(59, 0).addBox(-13, -39, -9, 13, 0, 18, 0, false);

		roof_side_r3 = new ModelMapper(modelDataWrapper);
		roof_side_r3.setPos(-15.5823F, -35.5941F, 0);
		door.addChild(roof_side_r3);
		setRotationAngle(roof_side_r3, 0, 0, 0.4363F);
		roof_side_r3.texOffs(128, 41).addBox(0, -3, -9, 0, 6, 18, 0, false);

		door_top_r1 = new ModelMapper(modelDataWrapper);
		door_top_r1.setPos(-21, 0, 0);
		door.addChild(door_top_r1);
		setRotationAngle(door_top_r1, 0, 0, 0.0349F);
		door_top_r1.texOffs(168, 242).addBox(1, -33.5F, -9, 2, 2, 18, 0, false);

		door_sliding_1 = new ModelMapper(modelDataWrapper);
		door_sliding_1.setPos(0, 24, 0);


		door_sliding_1_part = new ModelMapper(modelDataWrapper);
		door_sliding_1_part.setPos(0, 0, 0);
		door_sliding_1.addChild(door_sliding_1_part);


		door_top_r2 = new ModelMapper(modelDataWrapper);
		door_top_r2.setPos(-21, 0, 0);
		door_sliding_1_part.addChild(door_top_r2);
		setRotationAngle(door_top_r2, 0, 0, 0.0349F);
		door_top_r2.texOffs(158, 165).addBox(0.5F, -32, -9, 1, 32, 18, 0, false);

		door_sliding_2 = new ModelMapper(modelDataWrapper);
		door_sliding_2.setPos(0, 24, 0);


		door_sliding_2_part = new ModelMapper(modelDataWrapper);
		door_sliding_2_part.setPos(0, 0, 0);
		door_sliding_2.addChild(door_sliding_2_part);


		door_top_r3 = new ModelMapper(modelDataWrapper);
		door_top_r3.setPos(-21, 0, 0);
		door_sliding_2_part.addChild(door_top_r3);
		setRotationAngle(door_top_r3, 0, 0, 0.0349F);
		door_top_r3.texOffs(28, 165).addBox(0.5F, -32, -9, 1, 32, 18, 0, false);

		door_handrails = new ModelMapper(modelDataWrapper);
		door_handrails.setPos(0, 24, 0);


		handrail_2_r1 = new ModelMapper(modelDataWrapper);
		handrail_2_r1.setPos(-8, -9, 0);
		door_handrails.addChild(handrail_2_r1);
		setRotationAngle(handrail_2_r1, 0, 0, -0.0349F);
		handrail_2_r1.texOffs(268, 216).addBox(0, -30, 0, 0, 5, 0, 0.2F, false);

		handrail_5_r1 = new ModelMapper(modelDataWrapper);
		handrail_5_r1.setPos(-8, -9, 0);
		door_handrails.addChild(handrail_5_r1);
		setRotationAngle(handrail_5_r1, -1.5708F, 0, -0.0349F);
		handrail_5_r1.texOffs(268, 216).addBox(0, -14, -25, 0, 28, 0, 0.2F, false);

		door_exterior = new ModelMapper(modelDataWrapper);
		door_exterior.setPos(0, 24, 0);
		door_exterior.texOffs(128, 93).addBox(-21, 0, -9, 1, 1, 18, 0, false);
		door_exterior.texOffs(223, 96).addBox(-20, 1, -9, 1, 4, 18, 0, false);
		door_exterior.texOffs(13, 0).addBox(-3.8669F, -42.7901F, -9, 4, 0, 18, 0, false);

		roof_3_r3 = new ModelMapper(modelDataWrapper);
		roof_3_r3.setPos(-8.7907F, -41.9219F, 0);
		door_exterior.addChild(roof_3_r3);
		setRotationAngle(roof_3_r3, 0, 0, 1.3963F);
		roof_3_r3.texOffs(178, 151).addBox(0, -5, -9, 0, 7, 18, 0, false);

		roof_2_r3 = new ModelMapper(modelDataWrapper);
		roof_2_r3.setPos(-14.2241F, -39.5747F, 0);
		door_exterior.addChild(roof_2_r3);
		setRotationAngle(roof_2_r3, 0, 0, 1.0472F);
		roof_2_r3.texOffs(148, 81).addBox(0, -4, -9, 0, 6, 18, 0, false);

		roof_1_r2 = new ModelMapper(modelDataWrapper);
		roof_1_r2.setPos(-18.8485F, -35.1277F, 0);
		door_exterior.addChild(roof_1_r2);
		setRotationAngle(roof_1_r2, 0, 0, 0.6981F);
		roof_1_r2.texOffs(148, 87).addBox(0, -4.5F, -9, 0, 6, 18, 0, false);

		door_top_r4 = new ModelMapper(modelDataWrapper);
		door_top_r4.setPos(-21, 0, 0);
		door_exterior.addChild(door_top_r4);
		setRotationAngle(door_top_r4, 0, 0, 0.0349F);
		door_top_r4.texOffs(218, 29).addBox(0, -34, -9, 1, 2, 18, 0, false);

		door_sliding_exterior_1 = new ModelMapper(modelDataWrapper);
		door_sliding_exterior_1.setPos(0, 24, 0);


		door_sliding_exterior_1_part = new ModelMapper(modelDataWrapper);
		door_sliding_exterior_1_part.setPos(0, 0, 0);
		door_sliding_exterior_1.addChild(door_sliding_exterior_1_part);


		door_top_r5 = new ModelMapper(modelDataWrapper);
		door_top_r5.setPos(-21, 0, 0);
		door_sliding_exterior_1_part.addChild(door_top_r5);
		setRotationAngle(door_top_r5, 0, 0, 0.0349F);
		door_top_r5.texOffs(176, 0).addBox(0.5F, -32, -9, 0, 32, 18, 0, false);

		door_sliding_exterior_2 = new ModelMapper(modelDataWrapper);
		door_sliding_exterior_2.setPos(0, 24, 0);


		door_sliding_exterior_2_part = new ModelMapper(modelDataWrapper);
		door_sliding_exterior_2_part.setPos(0, 0, 0);
		door_sliding_exterior_2.addChild(door_sliding_exterior_2_part);


		door_top_r6 = new ModelMapper(modelDataWrapper);
		door_top_r6.setPos(-21, 0, 0);
		door_sliding_exterior_2_part.addChild(door_top_r6);
		setRotationAngle(door_top_r6, 0, 0, 0.0349F);
		door_top_r6.texOffs(153, 94).addBox(0.5F, -32, -9, 0, 32, 18, 0, false);

		side_seats = new ModelMapper(modelDataWrapper);
		side_seats.setPos(0, 24, 0);


		seat_1 = new ModelMapper(modelDataWrapper);
		seat_1.setPos(0, 0, -29);
		side_seats.addChild(seat_1);
		seat_1.texOffs(46, 196).addBox(-15, -7, 29, 6, 2, 20, 0, false);
		seat_1.texOffs(82, 73).addBox(-11, -5, 29, 0, 5, 20, 0, false);

		handrail_5_r2 = new ModelMapper(modelDataWrapper);
		handrail_5_r2.setPos(-9, -9, 29);
		seat_1.addChild(handrail_5_r2);
		setRotationAngle(handrail_5_r2, -1.5708F, 0, -0.0349F);
		handrail_5_r2.texOffs(268, 216).addBox(0, -20, -24, 0, 20, 0, 0.2F, false);

		handrail_2_r2 = new ModelMapper(modelDataWrapper);
		handrail_2_r2.setPos(-9, -9, 29);
		seat_1.addChild(handrail_2_r2);
		setRotationAngle(handrail_2_r2, 0, 0, -0.0349F);
		handrail_2_r2.texOffs(268, 216).addBox(0, -30, 7, 0, 6, 0, 0.2F, false);

		seat_back_r1 = new ModelMapper(modelDataWrapper);
		seat_back_r1.setPos(-15, -7, 29);
		seat_1.addChild(seat_back_r1);
		setRotationAngle(seat_back_r1, 0, 0, -0.1745F);
		seat_back_r1.texOffs(205, 137).addBox(-2, -8, 0, 2, 8, 20, 0, false);

		seat_2 = new ModelMapper(modelDataWrapper);
		seat_2.setPos(0, 0, -29);
		side_seats.addChild(seat_2);
		seat_2.texOffs(46, 196).addBox(9, -7, 29, 6, 2, 20, 0, true);
		seat_2.texOffs(82, 73).addBox(11, -5, 29, 0, 5, 20, 0, true);

		handrail_6_r1 = new ModelMapper(modelDataWrapper);
		handrail_6_r1.setPos(9, -9, 29);
		seat_2.addChild(handrail_6_r1);
		setRotationAngle(handrail_6_r1, -1.5708F, 0, 0.0349F);
		handrail_6_r1.texOffs(268, 216).addBox(0, -20, -24, 0, 20, 0, 0.2F, true);

		handrail_3_r1 = new ModelMapper(modelDataWrapper);
		handrail_3_r1.setPos(9, -9, 29);
		seat_2.addChild(handrail_3_r1);
		setRotationAngle(handrail_3_r1, 0, 0, 0.0349F);
		handrail_3_r1.texOffs(268, 216).addBox(0, -30, 7, 0, 6, 0, 0.2F, true);

		seat_back_r2 = new ModelMapper(modelDataWrapper);
		seat_back_r2.setPos(15, -7, 29);
		seat_2.addChild(seat_back_r2);
		setRotationAngle(seat_back_r2, 0, 0, 0.1745F);
		seat_back_r2.texOffs(205, 137).addBox(0, -8, 0, 2, 8, 20, 0, true);

		middle_seats = new ModelMapper(modelDataWrapper);
		middle_seats.setPos(0, 24, 0);


		seat_3 = new ModelMapper(modelDataWrapper);
		seat_3.setPos(0, 0, 0);
		middle_seats.addChild(seat_3);
		seat_3.texOffs(214, 184).addBox(-15, -7, 12, 6, 2, 8, 0, false);
		seat_3.texOffs(236, 10).addBox(-15, -5, 12, 4, 5, 8, 0, false);
		seat_3.texOffs(226, 0).addBox(-18, -7, 4, 14, 2, 8, 0, false);
		seat_3.texOffs(210, 228).addBox(-18, -5, 6, 12, 5, 6, 0, false);
		seat_3.texOffs(122, 234).addBox(-18, -15, 11, 14, 8, 1, 0, false);

		handrail_2_r3 = new ModelMapper(modelDataWrapper);
		handrail_2_r3.setPos(-9, -9, 0);
		seat_3.addChild(handrail_2_r3);
		setRotationAngle(handrail_2_r3, 0, 0, -0.0349F);
		handrail_2_r3.texOffs(268, 216).addBox(0, -30, 7, 0, 6, 0, 0.2F, false);

		handrail_5_r3 = new ModelMapper(modelDataWrapper);
		handrail_5_r3.setPos(-9, -9, 0);
		seat_3.addChild(handrail_5_r3);
		setRotationAngle(handrail_5_r3, -1.5708F, 0, -0.0349F);
		handrail_5_r3.texOffs(268, 216).addBox(0, -20, -24, 0, 20, 0, 0.2F, false);

		seat_back_2_r1 = new ModelMapper(modelDataWrapper);
		seat_back_2_r1.setPos(-11, -7, 10);
		seat_3.addChild(seat_back_2_r1);
		setRotationAngle(seat_back_2_r1, -0.1745F, 0, 0);
		seat_back_2_r1.texOffs(235, 72).addBox(-7, -7, 0, 14, 7, 1, 0, false);

		seat_back_1_r1 = new ModelMapper(modelDataWrapper);
		seat_back_1_r1.setPos(-15, -7, 0);
		seat_3.addChild(seat_back_1_r1);
		setRotationAngle(seat_back_1_r1, 0, 0, -0.1745F);
		seat_back_1_r1.texOffs(0, 7).addBox(-2, -8, 12, 2, 8, 8, 0, false);

		seat_4 = new ModelMapper(modelDataWrapper);
		seat_4.setPos(0, 0, 0);
		middle_seats.addChild(seat_4);
		seat_4.texOffs(214, 184).addBox(9, -7, 12, 6, 2, 8, 0, true);
		seat_4.texOffs(236, 10).addBox(11, -5, 12, 4, 5, 8, 0, true);
		seat_4.texOffs(226, 0).addBox(4, -7, 4, 14, 2, 8, 0, true);
		seat_4.texOffs(210, 228).addBox(6, -5, 6, 12, 5, 6, 0, true);
		seat_4.texOffs(122, 234).addBox(4, -15, 11, 14, 8, 1, 0, true);

		handrail_3_r2 = new ModelMapper(modelDataWrapper);
		handrail_3_r2.setPos(9, -9, 0);
		seat_4.addChild(handrail_3_r2);
		setRotationAngle(handrail_3_r2, 0, 0, 0.0349F);
		handrail_3_r2.texOffs(268, 216).addBox(0, -30, 7, 0, 6, 0, 0.2F, true);

		handrail_6_r2 = new ModelMapper(modelDataWrapper);
		handrail_6_r2.setPos(9, -9, 0);
		seat_4.addChild(handrail_6_r2);
		setRotationAngle(handrail_6_r2, -1.5708F, 0, 0.0349F);
		handrail_6_r2.texOffs(268, 216).addBox(0, -20, -24, 0, 20, 0, 0.2F, true);

		seat_back_3_r1 = new ModelMapper(modelDataWrapper);
		seat_back_3_r1.setPos(11, -7, 10);
		seat_4.addChild(seat_back_3_r1);
		setRotationAngle(seat_back_3_r1, -0.1745F, 0, 0);
		seat_back_3_r1.texOffs(235, 72).addBox(-7, -7, 0, 14, 7, 1, 0, true);

		seat_back_2_r2 = new ModelMapper(modelDataWrapper);
		seat_back_2_r2.setPos(15, -7, 0);
		seat_4.addChild(seat_back_2_r2);
		setRotationAngle(seat_back_2_r2, 0, 0, 0.1745F);
		seat_back_2_r2.texOffs(0, 7).addBox(0, -8, 12, 2, 8, 8, 0, true);

		side_panel = new ModelMapper(modelDataWrapper);
		side_panel.setPos(0, 24, 0);
		side_panel.texOffs(76, 116).addBox(-18, -30, 0, 9, 30, 0, 0, false);

		handrail_1_r1 = new ModelMapper(modelDataWrapper);
		handrail_1_r1.setPos(-9, -9, 0);
		side_panel.addChild(handrail_1_r1);
		setRotationAngle(handrail_1_r1, 0, 0, -0.0349F);
		handrail_1_r1.texOffs(268, 216).addBox(0, -30, 0, 0, 30, 0, 0.2F, false);

		side_panel_translucent = new ModelMapper(modelDataWrapper);
		side_panel_translucent.setPos(0, 24, 0);
		side_panel_translucent.texOffs(0, 245).addBox(-18, -30, 0, 9, 20, 0, 0, false);

		light_window = new ModelMapper(modelDataWrapper);
		light_window.setPos(0, 24, 0);


		cube_r1 = new ModelMapper(modelDataWrapper);
		cube_r1.setPos(-0.9792F, -0.1101F, 0);
		light_window.addChild(cube_r1);
		setRotationAngle(cube_r1, 0, 0, -0.7854F);
		cube_r1.texOffs(188, 78).addBox(17, -37, 0, 2, 1, 23, 0, true);

		cube_r2 = new ModelMapper(modelDataWrapper);
		cube_r2.setPos(0.9792F, -0.1101F, 0);
		light_window.addChild(cube_r2);
		setRotationAngle(cube_r2, 0, 0, 0.7854F);
		cube_r2.texOffs(188, 78).addBox(-19, -37, 0, 2, 1, 23, 0, false);

		light_door = new ModelMapper(modelDataWrapper);
		light_door.setPos(0, 24, 0);


		cube_r3 = new ModelMapper(modelDataWrapper);
		cube_r3.setPos(-0.9792F, -0.1101F, 0);
		light_door.addChild(cube_r3);
		setRotationAngle(cube_r3, 0, 0, -0.7854F);
		cube_r3.texOffs(190, 244).addBox(17, -37, -9, 2, 1, 18, 0, true);

		light_end = new ModelMapper(modelDataWrapper);
		light_end.setPos(0, 24, 0);


		cube_r4 = new ModelMapper(modelDataWrapper);
		cube_r4.setPos(0.9792F, -0.1101F, 0);
		light_end.addChild(cube_r4);
		setRotationAngle(cube_r4, 0, 0, 0.7854F);
		cube_r4.texOffs(243, 84).addBox(-19, -37, 0, 2, 1, 12, 0, true);

		cube_r5 = new ModelMapper(modelDataWrapper);
		cube_r5.setPos(-0.9792F, -0.1101F, 0);
		light_end.addChild(cube_r5);
		setRotationAngle(cube_r5, 0, 0, -0.7854F);
		cube_r5.texOffs(243, 84).addBox(17, -37, 0, 2, 1, 12, 0, false);

		light_head = new ModelMapper(modelDataWrapper);
		light_head.setPos(0, 24, 0);


		cube_r6 = new ModelMapper(modelDataWrapper);
		cube_r6.setPos(0.9792F, -0.1101F, 5);
		light_head.addChild(cube_r6);
		setRotationAngle(cube_r6, 0, 0, 0.7854F);
		cube_r6.texOffs(212, 239).addBox(-19, -37, -5, 2, 1, 19, 0, true);

		cube_r7 = new ModelMapper(modelDataWrapper);
		cube_r7.setPos(-0.9792F, -0.1101F, 5);
		light_head.addChild(cube_r7);
		setRotationAngle(cube_r7, 0, 0, -0.7854F);
		cube_r7.texOffs(212, 239).addBox(17, -37, -5, 2, 1, 19, 0, false);

		side_seats_end = new ModelMapper(modelDataWrapper);
		side_seats_end.setPos(0, 24, 0);


		seat_5 = new ModelMapper(modelDataWrapper);
		seat_5.setPos(0, 0, -29);
		side_seats_end.addChild(seat_5);
		seat_5.texOffs(186, 228).addBox(-15, -7, 29, 6, 2, 12, 0, false);
		seat_5.texOffs(82, 72).addBox(-11, -5, 29, 0, 5, 12, 0, false);

		handrail_6_r3 = new ModelMapper(modelDataWrapper);
		handrail_6_r3.setPos(-9, -9, 29);
		seat_5.addChild(handrail_6_r3);
		setRotationAngle(handrail_6_r3, -1.5708F, 0, -0.0349F);
		handrail_6_r3.texOffs(268, 216).addBox(0, -7, -24, 0, 7, 0, 0.2F, false);

		handrail_3_r3 = new ModelMapper(modelDataWrapper);
		handrail_3_r3.setPos(-9, -9, 29);
		seat_5.addChild(handrail_3_r3);
		setRotationAngle(handrail_3_r3, 0, 0, -0.0349F);
		handrail_3_r3.texOffs(268, 216).addBox(0, -30, 7, 0, 6, 0, 0.2F, false);

		seat_back_r3 = new ModelMapper(modelDataWrapper);
		seat_back_r3.setPos(-15, -7, 29);
		seat_5.addChild(seat_back_r3);
		setRotationAngle(seat_back_r3, 0, 0, -0.1745F);
		seat_back_r3.texOffs(228, 202).addBox(-2, -8, 0, 2, 8, 12, 0, false);

		seat_6 = new ModelMapper(modelDataWrapper);
		seat_6.setPos(0, 0, -29);
		side_seats_end.addChild(seat_6);
		seat_6.texOffs(186, 228).addBox(9, -7, 29, 6, 2, 12, 0, true);
		seat_6.texOffs(82, 72).addBox(11, -5, 29, 0, 5, 12, 0, true);

		handrail_7_r1 = new ModelMapper(modelDataWrapper);
		handrail_7_r1.setPos(9, -9, 29);
		seat_6.addChild(handrail_7_r1);
		setRotationAngle(handrail_7_r1, -1.5708F, 0, 0.0349F);
		handrail_7_r1.texOffs(268, 216).addBox(0, -7, -24, 0, 7, 0, 0.2F, true);

		handrail_4_r1 = new ModelMapper(modelDataWrapper);
		handrail_4_r1.setPos(9, -9, 29);
		seat_6.addChild(handrail_4_r1);
		setRotationAngle(handrail_4_r1, 0, 0, 0.0349F);
		handrail_4_r1.texOffs(268, 216).addBox(0, -30, 7, 0, 6, 0, 0.2F, true);

		seat_back_r4 = new ModelMapper(modelDataWrapper);
		seat_back_r4.setPos(15, -7, 29);
		seat_6.addChild(seat_back_r4);
		setRotationAngle(seat_back_r4, 0, 0, 0.1745F);
		seat_back_r4.texOffs(228, 202).addBox(0, -8, 0, 2, 8, 12, 0, true);

		side_seats_head = new ModelMapper(modelDataWrapper);
		side_seats_head.setPos(0, 24, 0);


		seat_7 = new ModelMapper(modelDataWrapper);
		seat_7.setPos(0, 0, -29);
		side_seats_head.addChild(seat_7);
		seat_7.texOffs(210, 54).addBox(-15, -7, 32, 6, 2, 16, 0, false);
		seat_7.texOffs(134, 8).addBox(-11, -5, 32, 0, 5, 16, 0, false);

		handrail_7_r2 = new ModelMapper(modelDataWrapper);
		handrail_7_r2.setPos(-9, -9, 29);
		seat_7.addChild(handrail_7_r2);
		setRotationAngle(handrail_7_r2, -1.5708F, 0, -0.0349F);
		handrail_7_r2.texOffs(268, 216).addBox(0, -10, -24, 0, 7, 0, 0.2F, false);

		handrail_4_r2 = new ModelMapper(modelDataWrapper);
		handrail_4_r2.setPos(-9, -9, 29);
		seat_7.addChild(handrail_4_r2);
		setRotationAngle(handrail_4_r2, 0, 0, -0.0349F);
		handrail_4_r2.texOffs(268, 216).addBox(0, -30, 10, 0, 6, 0, 0.2F, false);

		seat_back_r5 = new ModelMapper(modelDataWrapper);
		seat_back_r5.setPos(-15, -7, 29);
		seat_7.addChild(seat_back_r5);
		setRotationAngle(seat_back_r5, 0, 0, -0.1745F);
		seat_back_r5.texOffs(215, 72).addBox(-2, -8, 3, 2, 8, 16, 0, false);

		seat_8 = new ModelMapper(modelDataWrapper);
		seat_8.setPos(0, 0, -29);
		side_seats_head.addChild(seat_8);
		seat_8.texOffs(210, 54).addBox(9, -7, 32, 6, 2, 16, 0, true);
		seat_8.texOffs(134, 8).addBox(11, -5, 32, 0, 5, 16, 0, true);

		handrail_8_r1 = new ModelMapper(modelDataWrapper);
		handrail_8_r1.setPos(9, -9, 29);
		seat_8.addChild(handrail_8_r1);
		setRotationAngle(handrail_8_r1, -1.5708F, 0, 0.0349F);
		handrail_8_r1.texOffs(268, 216).addBox(0, -10, -24, 0, 7, 0, 0.2F, true);

		handrail_5_r4 = new ModelMapper(modelDataWrapper);
		handrail_5_r4.setPos(9, -9, 29);
		seat_8.addChild(handrail_5_r4);
		setRotationAngle(handrail_5_r4, 0, 0, 0.0349F);
		handrail_5_r4.texOffs(268, 216).addBox(0, -30, 10, 0, 6, 0, 0.2F, true);

		seat_back_r6 = new ModelMapper(modelDataWrapper);
		seat_back_r6.setPos(15, -7, 29);
		seat_8.addChild(seat_back_r6);
		setRotationAngle(seat_back_r6, 0, 0, 0.1745F);
		seat_back_r6.texOffs(215, 72).addBox(0, -8, 3, 2, 8, 16, 0, true);

		end = new ModelMapper(modelDataWrapper);
		end.setPos(0, 24, 0);
		end.texOffs(72, 218).addBox(-6, -34, 14, 12, 34, 0, 0, false);

		window_5 = new ModelMapper(modelDataWrapper);
		window_5.setPos(0, 0, 0);
		end.addChild(window_5);
		window_5.texOffs(153, 0).addBox(-21, 0, 0, 21, 1, 14, 0, false);
		window_5.texOffs(158, 215).addBox(-18, -39, 12, 12, 39, 2, 0, false);
		window_5.texOffs(128, 93).addBox(-6, -39, 12, 6, 6, 2, 0, false);
		window_5.texOffs(128, 101).addBox(-8, -39, 11, 8, 5, 1, 0, false);
		window_5.texOffs(70, 66).addBox(-13, -39, 0, 13, 0, 12, 0, false);

		roof_side_r4 = new ModelMapper(modelDataWrapper);
		roof_side_r4.setPos(-15.5823F, -35.5941F, 11.5F);
		window_5.addChild(roof_side_r4);
		setRotationAngle(roof_side_r4, 0, 0, 0.4363F);
		roof_side_r4.texOffs(82, 66).addBox(0, -3, -11.5F, 0, 6, 12, 0, false);

		window_bottom_r9 = new ModelMapper(modelDataWrapper);
		window_bottom_r9.setPos(-21, 0, 0);
		window_5.addChild(window_bottom_r9);
		setRotationAngle(window_bottom_r9, 0, 0, 0.0349F);
		window_bottom_r9.texOffs(196, 182).addBox(0, -33, 0, 3, 33, 12, 0, false);

		window_10 = new ModelMapper(modelDataWrapper);
		window_10.setPos(0, 0, 0);
		end.addChild(window_10);
		window_10.texOffs(153, 0).addBox(0, 0, 0, 21, 1, 14, 0, true);
		window_10.texOffs(158, 215).addBox(6, -39, 12, 12, 39, 2, 0, true);
		window_10.texOffs(128, 93).addBox(0, -39, 12, 6, 6, 2, 0, true);
		window_10.texOffs(128, 101).addBox(0, -39, 11, 8, 5, 1, 0, true);
		window_10.texOffs(70, 66).addBox(0, -39, 0, 13, 0, 12, 0, true);

		roof_side_r5 = new ModelMapper(modelDataWrapper);
		roof_side_r5.setPos(15.5823F, -35.5941F, 11.5F);
		window_10.addChild(roof_side_r5);
		setRotationAngle(roof_side_r5, 0, 0, -0.4363F);
		roof_side_r5.texOffs(82, 66).addBox(0, -3, -11.5F, 0, 6, 12, 0, true);

		window_bottom_r10 = new ModelMapper(modelDataWrapper);
		window_bottom_r10.setPos(21, 0, 0);
		window_10.addChild(window_bottom_r10);
		setRotationAngle(window_bottom_r10, 0, 0, -0.0349F);
		window_bottom_r10.texOffs(196, 182).addBox(-3, -33, 0, 3, 33, 12, 0, true);

		end_exterior = new ModelMapper(modelDataWrapper);
		end_exterior.setPos(0, 24, 0);
		end_exterior.texOffs(28, 218).addBox(-6, -43, 15, 12, 43, 0, 0, false);

		window_7 = new ModelMapper(modelDataWrapper);
		window_7.setPos(0, 0, 0);
		end_exterior.addChild(window_7);
		window_7.texOffs(0, 29).addBox(-21, 0, 0, 1, 1, 12, 0, false);
		window_7.texOffs(0, 229).addBox(-20, 1, 0, 1, 4, 12, 0, false);
		window_7.texOffs(82, 89).addBox(-19, 1, 11, 12, 2, 1, 0, false);
		window_7.texOffs(229, 144).addBox(-7, 0, 11, 14, 4, 4, 0, false);
		window_7.texOffs(0, 0).addBox(-3.8669F, -42.7901F, 0, 4, 0, 15, 0, false);

		roof_3_r4 = new ModelMapper(modelDataWrapper);
		roof_3_r4.setPos(-8.7907F, -41.9219F, 11.5F);
		window_7.addChild(roof_3_r4);
		setRotationAngle(roof_3_r4, 0, 0, 1.3963F);
		roof_3_r4.texOffs(134, 14).addBox(0, -5, -11.5F, 0, 7, 15, 0, false);

		roof_2_r4 = new ModelMapper(modelDataWrapper);
		roof_2_r4.setPos(-14.2241F, -39.5747F, 11.5F);
		window_7.addChild(roof_2_r4);
		setRotationAngle(roof_2_r4, 0, 0, 1.0472F);
		roof_2_r4.texOffs(122, 213).addBox(0, -4, -11.5F, 0, 6, 15, 0, false);

		roof_1_r3 = new ModelMapper(modelDataWrapper);
		roof_1_r3.setPos(-18.8485F, -35.1277F, 11.5F);
		window_7.addChild(roof_1_r3);
		setRotationAngle(roof_1_r3, 0, 0, 0.6981F);
		roof_1_r3.texOffs(99, 135).addBox(0, -4.5F, -11.5F, 0, 6, 14, 0, false);

		back_wall_r1 = new ModelMapper(modelDataWrapper);
		back_wall_r1.setPos(-6, 0, 15);
		window_7.addChild(back_wall_r1);
		setRotationAngle(back_wall_r1, 0, -0.1745F, 0);
		back_wall_r1.texOffs(189, 112).addBox(-16, -43, -1, 16, 44, 1, 0, false);

		window_bottom_r11 = new ModelMapper(modelDataWrapper);
		window_bottom_r11.setPos(-21, 0, 0);
		window_7.addChild(window_bottom_r11);
		setRotationAngle(window_bottom_r11, 0, 0, 0.0349F);
		window_bottom_r11.texOffs(0, 182).addBox(0, -34, 0, 1, 34, 13, 0, false);

		window_12 = new ModelMapper(modelDataWrapper);
		window_12.setPos(0, 0, 0);
		end_exterior.addChild(window_12);
		window_12.texOffs(0, 29).addBox(20, 0, 0, 1, 1, 12, 0, true);
		window_12.texOffs(0, 229).addBox(19, 1, 0, 1, 4, 12, 0, true);
		window_12.texOffs(82, 89).addBox(7, 1, 11, 12, 2, 1, 0, true);
		window_12.texOffs(229, 144).addBox(-7, 0, 11, 14, 4, 4, 0, true);
		window_12.texOffs(0, 0).addBox(-0.1331F, -42.7901F, 0, 4, 0, 15, 0, true);

		roof_4_r2 = new ModelMapper(modelDataWrapper);
		roof_4_r2.setPos(8.7907F, -41.9219F, 11.5F);
		window_12.addChild(roof_4_r2);
		setRotationAngle(roof_4_r2, 0, 0, -1.3963F);
		roof_4_r2.texOffs(134, 14).addBox(0, -5, -11.5F, 0, 7, 15, 0, true);

		roof_3_r5 = new ModelMapper(modelDataWrapper);
		roof_3_r5.setPos(14.2241F, -39.5747F, 11.5F);
		window_12.addChild(roof_3_r5);
		setRotationAngle(roof_3_r5, 0, 0, -1.0472F);
		roof_3_r5.texOffs(122, 213).addBox(0, -4, -11.5F, 0, 6, 15, 0, true);

		roof_2_r5 = new ModelMapper(modelDataWrapper);
		roof_2_r5.setPos(18.8485F, -35.1277F, 11.5F);
		window_12.addChild(roof_2_r5);
		setRotationAngle(roof_2_r5, 0, 0, -0.6981F);
		roof_2_r5.texOffs(99, 135).addBox(0, -4.5F, -11.5F, 0, 6, 14, 0, true);

		back_wall_r2 = new ModelMapper(modelDataWrapper);
		back_wall_r2.setPos(6, 0, 15);
		window_12.addChild(back_wall_r2);
		setRotationAngle(back_wall_r2, 0, 0.1745F, 0);
		back_wall_r2.texOffs(189, 112).addBox(0, -43, -1, 16, 44, 1, 0, true);

		window_bottom_r12 = new ModelMapper(modelDataWrapper);
		window_bottom_r12.setPos(21, 0, 0);
		window_12.addChild(window_bottom_r12);
		setRotationAngle(window_bottom_r12, 0, 0, -0.0349F);
		window_bottom_r12.texOffs(0, 182).addBox(-1, -34, 0, 1, 34, 13, 0, true);

		head = new ModelMapper(modelDataWrapper);
		head.setPos(0, 24, 0);
		head.texOffs(0, 110).addBox(-18, -39, 19, 36, 39, 0, 0, false);

		window_11 = new ModelMapper(modelDataWrapper);
		window_11.setPos(0, 0, -29);
		head.addChild(window_11);
		window_11.texOffs(115, 39).addBox(-21, 0, 29, 21, 1, 19, 0, false);
		window_11.texOffs(128, 101).addBox(-8, -39, 47, 8, 5, 1, 0, false);
		window_11.texOffs(32, 23).addBox(-13, -39, 29, 13, 0, 19, 0, false);

		roof_side_r6 = new ModelMapper(modelDataWrapper);
		roof_side_r6.setPos(-15.5823F, -35.5941F, 40.5F);
		window_11.addChild(roof_side_r6);
		setRotationAngle(roof_side_r6, 0, 0, 0.4363F);
		roof_side_r6.texOffs(82, 79).addBox(0, -3, -11.5F, 0, 6, 19, 0, false);

		window_bottom_r13 = new ModelMapper(modelDataWrapper);
		window_bottom_r13.setPos(-21, 0, 29);
		window_11.addChild(window_bottom_r13);
		setRotationAngle(window_bottom_r13, 0, 0, 0.0349F);
		window_bottom_r13.texOffs(80, 164).addBox(0, -33, 0, 3, 33, 19, 0, false);

		window_14 = new ModelMapper(modelDataWrapper);
		window_14.setPos(0, 0, -29);
		head.addChild(window_14);
		window_14.texOffs(115, 39).addBox(0, 0, 29, 21, 1, 19, 0, true);
		window_14.texOffs(128, 101).addBox(0, -39, 47, 8, 5, 1, 0, true);
		window_14.texOffs(32, 23).addBox(0, -39, 29, 13, 0, 19, 0, true);

		roof_side_r7 = new ModelMapper(modelDataWrapper);
		roof_side_r7.setPos(15.5823F, -35.5941F, 40.5F);
		window_14.addChild(roof_side_r7);
		setRotationAngle(roof_side_r7, 0, 0, -0.4363F);
		roof_side_r7.texOffs(82, 79).addBox(0, -3, -11.5F, 0, 6, 19, 0, true);

		window_bottom_r14 = new ModelMapper(modelDataWrapper);
		window_bottom_r14.setPos(21, 0, 29);
		window_14.addChild(window_bottom_r14);
		setRotationAngle(window_bottom_r14, 0, 0, -0.0349F);
		window_bottom_r14.texOffs(80, 164).addBox(-3, -33, 0, 3, 33, 19, 0, true);

		head_exterior = new ModelMapper(modelDataWrapper);
		head_exterior.setPos(0, 24, 0);
		head_exterior.texOffs(98, 216).addBox(-6, -43, 43, 12, 43, 0, 0, false);
		head_exterior.texOffs(0, 26).addBox(-16, 1, 40, 6, 2, 1, 0, false);
		head_exterior.texOffs(0, 23).addBox(10, 1, 40, 6, 2, 1, 0, false);
		head_exterior.texOffs(0, 66).addBox(-20, -43, 20, 40, 43, 0, 0, false);

		window_9 = new ModelMapper(modelDataWrapper);
		window_9.setPos(0, 0, 0);
		head_exterior.addChild(window_9);
		window_9.texOffs(82, 66).addBox(-21, 0, 16, 21, 1, 26, 0, false);
		window_9.texOffs(40, 69).addBox(-21, 0, 0, 1, 1, 40, 0, false);
		window_9.texOffs(46, 22).addBox(-20, 1, 0, 1, 4, 40, 0, false);
		window_9.texOffs(77, 18).addBox(-19, 1, 39, 12, 2, 1, 0, false);
		window_9.texOffs(229, 136).addBox(-7, 0, 39, 14, 4, 4, 0, false);
		window_9.texOffs(0, 0).addBox(-3.8669F, -42.7901F, 0, 4, 0, 43, 0, false);

		door_top_r7 = new ModelMapper(modelDataWrapper);
		door_top_r7.setPos(-21, 0, 32);
		window_9.addChild(door_top_r7);
		setRotationAngle(door_top_r7, 0, 0, 0.0349F);
		door_top_r7.texOffs(105, 155).addBox(0, -34, -9, 1, 2, 10, 0, false);
		door_top_r7.texOffs(214, 5).addBox(0.5F, -32, -9, 1, 32, 10, 0, false);
		door_top_r7.texOffs(52, 218).addBox(0, -34, 1, 2, 34, 8, 0, false);
		door_top_r7.texOffs(49, 126).addBox(0, -34, -32, 2, 34, 23, 0, false);

		roof_4_r3 = new ModelMapper(modelDataWrapper);
		roof_4_r3.setPos(-8.7907F, -41.9219F, 0);
		window_9.addChild(roof_4_r3);
		setRotationAngle(roof_4_r3, 0, 0, 1.3963F);
		roof_4_r3.texOffs(0, 0).addBox(0, -5, 0, 0, 7, 43, 0, false);

		roof_3_r6 = new ModelMapper(modelDataWrapper);
		roof_3_r6.setPos(-14.2241F, -39.5747F, 0);
		window_9.addChild(roof_3_r6);
		setRotationAngle(roof_3_r6, 0, 0, 1.0472F);
		roof_3_r6.texOffs(0, 7).addBox(0, -4, 0, 0, 6, 43, 0, false);

		roof_2_r6 = new ModelMapper(modelDataWrapper);
		roof_2_r6.setPos(-18.8485F, -35.1277F, 0);
		window_9.addChild(roof_2_r6);
		setRotationAngle(roof_2_r6, 0, 0, 0.6981F);
		roof_2_r6.texOffs(0, 14).addBox(0, -4.5F, 0, 0, 6, 42, 0, false);

		back_wall_r3 = new ModelMapper(modelDataWrapper);
		back_wall_r3.setPos(-6, 0, 43);
		window_9.addChild(back_wall_r3);
		setRotationAngle(back_wall_r3, 0, -0.1745F, 0);
		back_wall_r3.texOffs(124, 183).addBox(-16, -43, -1, 16, 44, 1, 0, false);

		window_16 = new ModelMapper(modelDataWrapper);
		window_16.setPos(0, 0, 0);
		head_exterior.addChild(window_16);
		window_16.texOffs(82, 66).addBox(0, 0, 16, 21, 1, 26, 0, true);
		window_16.texOffs(40, 69).addBox(20, 0, 0, 1, 1, 40, 0, true);
		window_16.texOffs(46, 22).addBox(19, 1, 0, 1, 4, 40, 0, true);
		window_16.texOffs(77, 18).addBox(7, 1, 39, 12, 2, 1, 0, true);
		window_16.texOffs(229, 136).addBox(-7, 0, 39, 14, 4, 4, 0, true);
		window_16.texOffs(0, 0).addBox(-0.1331F, -42.7901F, 0, 4, 0, 43, 0, true);

		door_top_r8 = new ModelMapper(modelDataWrapper);
		door_top_r8.setPos(21, 0, 32);
		window_16.addChild(door_top_r8);
		setRotationAngle(door_top_r8, 0, 0, -0.0349F);
		door_top_r8.texOffs(105, 155).addBox(-1, -34, -9, 1, 2, 10, 0, true);
		door_top_r8.texOffs(214, 5).addBox(-1.5F, -32, -9, 1, 32, 10, 0, true);
		door_top_r8.texOffs(52, 218).addBox(-2, -34, 1, 2, 34, 8, 0, true);
		door_top_r8.texOffs(49, 126).addBox(-2, -34, -32, 2, 34, 23, 0, true);

		roof_5_r1 = new ModelMapper(modelDataWrapper);
		roof_5_r1.setPos(8.7907F, -41.9219F, 0);
		window_16.addChild(roof_5_r1);
		setRotationAngle(roof_5_r1, 0, 0, -1.3963F);
		roof_5_r1.texOffs(0, 0).addBox(0, -5, 0, 0, 7, 43, 0, true);

		roof_4_r4 = new ModelMapper(modelDataWrapper);
		roof_4_r4.setPos(14.2241F, -39.5747F, 0);
		window_16.addChild(roof_4_r4);
		setRotationAngle(roof_4_r4, 0, 0, -1.0472F);
		roof_4_r4.texOffs(0, 7).addBox(0, -4, 0, 0, 6, 43, 0, true);

		roof_3_r7 = new ModelMapper(modelDataWrapper);
		roof_3_r7.setPos(18.8485F, -35.1277F, 0);
		window_16.addChild(roof_3_r7);
		setRotationAngle(roof_3_r7, 0, 0, -0.6981F);
		roof_3_r7.texOffs(0, 14).addBox(0, -4.5F, 0, 0, 6, 42, 0, true);

		back_wall_r4 = new ModelMapper(modelDataWrapper);
		back_wall_r4.setPos(6, 0, 43);
		window_16.addChild(back_wall_r4);
		setRotationAngle(back_wall_r4, 0, 0.1745F, 0);
		back_wall_r4.texOffs(124, 183).addBox(0, -43, -1, 16, 44, 1, 0, true);

		headlights = new ModelMapper(modelDataWrapper);
		headlights.setPos(0, 24, 0);
		headlights.texOffs(14, 23).addBox(-14, 1, 41.1F, 2, 2, 0, 0, false);
		headlights.texOffs(14, 23).addBox(12, 1, 41.1F, 2, 2, 0, 0, true);

		tail_lights = new ModelMapper(modelDataWrapper);
		tail_lights.setPos(0, 24, 0);
		tail_lights.texOffs(14, 25).addBox(-16, 1, 41.1F, 2, 2, 0, 0, false);
		tail_lights.texOffs(14, 25).addBox(-12, 1, 41.1F, 2, 2, 0, 0, false);
		tail_lights.texOffs(14, 25).addBox(14, 1, 41.1F, 2, 2, 0, 0, true);

		modelDataWrapper.setModelPart(textureWidth, textureHeight);
		window.setModelPart();
		window_2_partial.setModelPart();
		window_exterior.setModelPart();
		window_exterior_2_partial.setModelPart();
		logo.setModelPart();
		door.setModelPart();
		door_sliding_1.setModelPart();
		door_sliding_1_part.setModelPart(door_sliding_1.name);
		door_sliding_2.setModelPart();
		door_sliding_2_part.setModelPart(door_sliding_2.name);
		door_exterior.setModelPart();
		door_sliding_exterior_1.setModelPart();
		door_sliding_exterior_1_part.setModelPart(door_sliding_exterior_1.name);
		door_sliding_exterior_2.setModelPart();
		door_sliding_exterior_2_part.setModelPart(door_sliding_exterior_2.name);
		side_seats.setModelPart();
		middle_seats.setModelPart();
		side_panel.setModelPart();
		side_panel_translucent.setModelPart();
		light_window.setModelPart();
		light_door.setModelPart();
		light_end.setModelPart();
		light_head.setModelPart();
		side_seats_end.setModelPart();
		side_seats_head.setModelPart();
		end.setModelPart();
		end_exterior.setModelPart();
		head.setModelPart();
		head_exterior.setModelPart();
		headlights.setModelPart();
		tail_lights.setModelPart();
	}

	private static final int DOOR_MAX = 17;

	@Override
	protected void renderWindowPositions(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
		final boolean isEnd1 = isIndex(0, position, getWindowPositions());
		final boolean isEnd2 = isIndex(-1, position, getWindowPositions());

		switch (renderStage) {
			case LIGHTS:
				renderOnceFlipped(light_window, matrices, vertices, light, position);
				renderOnce(light_window, matrices, vertices, light, position);
				break;
			case INTERIOR:
				renderMirror(window, matrices, vertices, light, position);
				if (renderDetails) {
					renderMirror(isEnd1 || isEnd2 ? side_seats : middle_seats, matrices, vertices, light, position);
					if (!isEnd1 && !isEnd2) {
						renderMirror(window_2_partial, matrices, vertices, light, position);
					}
				}
				break;
			case EXTERIOR:
				renderMirror(window_exterior, matrices, vertices, light, position);
				if (renderDetails && !isEnd1 && !isEnd2) {
					renderMirror(window_exterior_2_partial, matrices, vertices, light, position);
					renderMirror(logo, matrices, vertices, light, position);
				}
				break;
		}
	}

	@Override
	protected void renderDoorPositions(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
		final int[] doorPositions = getDoorPositions();
		boolean isOdd = false;
		for (int i = 0; i < doorPositions.length; i++) {
			if (isIndex(i, position, doorPositions)) {
				isOdd = (i % 2) == 1;
				break;
			}
		}

		switch (renderStage) {
			case LIGHTS:
				renderMirror(light_door, matrices, vertices, light, position);
				break;
			case INTERIOR:
				renderMirror(door, matrices, vertices, light, position);

				(isOdd ? door_sliding_1_part : door_sliding_2_part).setOffset(0, 0, (isOdd ? -1 : 1) * doorRightZ);
				renderOnce(isOdd ? door_sliding_1 : door_sliding_2, matrices, vertices, light, position);
				(isOdd ? door_sliding_2_part : door_sliding_1_part).setOffset(0, 0, (isOdd ? 1 : -1) * doorLeftZ);
				renderOnceFlipped(isOdd ? door_sliding_2 : door_sliding_1, matrices, vertices, light, position);

				if (renderDetails) {
					renderMirror(side_panel, matrices, vertices, light, position - 11.9F);
					renderMirror(side_panel, matrices, vertices, light, position + 11.9F);
				}
				break;
			case INTERIOR_TRANSLUCENT:
				if (renderDetails) {
					renderMirror(side_panel_translucent, matrices, vertices, light, position - 11.9F);
					renderMirror(side_panel_translucent, matrices, vertices, light, position + 11.9F);
				}
				break;
			case EXTERIOR:
				renderMirror(door_exterior, matrices, vertices, light, position);

				(isOdd ? door_sliding_exterior_1_part : door_sliding_exterior_2_part).setOffset(0, 0, (isOdd ? -1 : 1) * doorRightZ);
				renderOnce(isOdd ? door_sliding_exterior_1 : door_sliding_exterior_2, matrices, vertices, light, position);
				(isOdd ? door_sliding_exterior_2_part : door_sliding_exterior_1_part).setOffset(0, 0, (isOdd ? 1 : -1) * doorLeftZ);
				renderOnceFlipped(isOdd ? door_sliding_exterior_2 : door_sliding_exterior_1, matrices, vertices, light, position);

				break;
		}
	}

	@Override
	protected void renderHeadPosition1(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean useHeadlights) {
		switch (renderStage) {
			case ALWAYS_ON_LIGHTS:
				renderOnceFlipped(useHeadlights ? headlights : tail_lights, matrices, vertices, light, position + 23);
				break;
			case LIGHTS:
				renderOnceFlipped(light_head, matrices, vertices, light, position + 23);
				break;
			case INTERIOR:
				renderOnceFlipped(head, matrices, vertices, light, position + 23);
				if (renderDetails) {
					renderOnceFlipped(side_seats_head, matrices, vertices, light, position + 23);
				}
				break;
			case EXTERIOR:
				renderOnceFlipped(head_exterior, matrices, vertices, light, position + 23);
				break;
		}
	}

	@Override
	protected void renderHeadPosition2(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean useHeadlights) {
		switch (renderStage) {
			case ALWAYS_ON_LIGHTS:
				renderOnce(useHeadlights ? headlights : tail_lights, matrices, vertices, light, position - 23);
				break;
			case LIGHTS:
				renderOnce(light_head, matrices, vertices, light, position - 23);
				break;
			case INTERIOR:
				renderOnce(head, matrices, vertices, light, position - 23);
				if (renderDetails) {
					renderOnce(side_seats_head, matrices, vertices, light, position - 23);
				}
				break;
			case EXTERIOR:
				renderOnce(head_exterior, matrices, vertices, light, position - 23);
				break;
		}
	}

	@Override
	protected void renderEndPosition1(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
		switch (renderStage) {
			case LIGHTS:
				renderOnce(light_window, matrices, vertices, light, position);
				renderOnceFlipped(light_end, matrices, vertices, light, position);
				break;
			case INTERIOR:
				renderOnce(window, matrices, vertices, light, position);
				renderOnce(window_2_partial, matrices, vertices, light, position);
				renderOnceFlipped(end, matrices, vertices, light, position);
				if (renderDetails) {
					renderOnce(side_seats, matrices, vertices, light, position);
					renderOnceFlipped(side_seats_end, matrices, vertices, light, position);
				}
				break;
			case EXTERIOR:
				renderOnce(window_exterior, matrices, vertices, light, position);
				renderOnce(window_exterior_2_partial, matrices, vertices, light, position);
				renderOnceFlipped(end_exterior, matrices, vertices, light, position);
				break;
		}
	}

	@Override
	protected void renderEndPosition2(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
		switch (renderStage) {
			case LIGHTS:
				renderOnceFlipped(light_window, matrices, vertices, light, position);
				renderOnce(light_end, matrices, vertices, light, position);
				break;
			case INTERIOR:
				renderOnceFlipped(window, matrices, vertices, light, position);
				renderOnceFlipped(window_2_partial, matrices, vertices, light, position);
				renderOnce(end, matrices, vertices, light, position);
				if (renderDetails) {
					renderOnceFlipped(side_seats, matrices, vertices, light, position);
					renderOnce(side_seats_end, matrices, vertices, light, position);
				}
				break;
			case EXTERIOR:
				renderOnceFlipped(window_exterior, matrices, vertices, light, position);
				renderOnceFlipped(window_exterior_2_partial, matrices, vertices, light, position);
				renderOnce(end_exterior, matrices, vertices, light, position);
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
		return new int[]{-64, 0, 64};
	}

	@Override
	protected int[] getDoorPositions() {
		return new int[]{-96, -32, 32, 96};
	}

	@Override
	protected int[] getEndPositions() {
		return new int[]{-128, 128};
	}

	@Override
	protected int[] getBogiePositions() {
		return new int[]{-92, 92};
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