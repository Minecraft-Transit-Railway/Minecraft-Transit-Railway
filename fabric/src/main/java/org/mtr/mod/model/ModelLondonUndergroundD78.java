package org.mtr.mod.model;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.core.data.InterchangeColorsForStationName;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.ModelPartExtension;
import org.mtr.mod.client.DoorAnimationType;
import org.mtr.mod.client.DynamicTextureCache;
import org.mtr.mod.client.ScrollingText;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ModelLondonUndergroundD78 extends ModelSimpleTrainBase<ModelLondonUndergroundD78> {

	private final ModelPartExtension window;
	private final ModelPartExtension window_1;
	private final ModelPartExtension roof_side_r1;
	private final ModelPartExtension window_bottom_r1;
	private final ModelPartExtension window_3;
	private final ModelPartExtension roof_side_r2;
	private final ModelPartExtension window_bottom_r2;
	private final ModelPartExtension window_2_partial;
	private final ModelPartExtension window_13;
	private final ModelPartExtension window_bottom_r3;
	private final ModelPartExtension window_4;
	private final ModelPartExtension window_bottom_r4;
	private final ModelPartExtension window_exterior;
	private final ModelPartExtension window_2;
	private final ModelPartExtension roof_3_r1;
	private final ModelPartExtension roof_2_r1;
	private final ModelPartExtension roof_1_r1;
	private final ModelPartExtension window_bottom_r5;
	private final ModelPartExtension window_6;
	private final ModelPartExtension roof_4_r1;
	private final ModelPartExtension roof_3_r2;
	private final ModelPartExtension roof_2_r2;
	private final ModelPartExtension window_bottom_r6;
	private final ModelPartExtension window_exterior_2_partial;
	private final ModelPartExtension window_15;
	private final ModelPartExtension window_bottom_r7;
	private final ModelPartExtension window_8;
	private final ModelPartExtension window_bottom_r8;
	private final ModelPartExtension logo;
	private final ModelPartExtension logo_r1;
	private final ModelPartExtension door;
	private final ModelPartExtension roof_side_r3;
	private final ModelPartExtension door_top_r1;
	private final ModelPartExtension door_sliding_1;
	private final ModelPartExtension door_sliding_1_part;
	private final ModelPartExtension door_top_r2;
	private final ModelPartExtension door_sliding_2;
	private final ModelPartExtension door_sliding_2_part;
	private final ModelPartExtension door_top_r3;
	private final ModelPartExtension door_handrails;
	private final ModelPartExtension handrail_2_r1;
	private final ModelPartExtension handrail_5_r1;
	private final ModelPartExtension door_exterior;
	private final ModelPartExtension roof_3_r3;
	private final ModelPartExtension roof_2_r3;
	private final ModelPartExtension roof_1_r2;
	private final ModelPartExtension door_top_r4;
	private final ModelPartExtension door_sliding_exterior_1;
	private final ModelPartExtension door_sliding_exterior_1_part;
	private final ModelPartExtension door_top_r5;
	private final ModelPartExtension door_sliding_exterior_2;
	private final ModelPartExtension door_sliding_exterior_2_part;
	private final ModelPartExtension door_top_r6;
	private final ModelPartExtension side_seats;
	private final ModelPartExtension seat_1;
	private final ModelPartExtension handrail_5_r2;
	private final ModelPartExtension handrail_2_r2;
	private final ModelPartExtension seat_back_r1;
	private final ModelPartExtension seat_2;
	private final ModelPartExtension handrail_6_r1;
	private final ModelPartExtension handrail_3_r1;
	private final ModelPartExtension seat_back_r2;
	private final ModelPartExtension middle_seats;
	private final ModelPartExtension seat_3;
	private final ModelPartExtension handrail_2_r3;
	private final ModelPartExtension handrail_5_r3;
	private final ModelPartExtension seat_back_2_r1;
	private final ModelPartExtension seat_back_1_r1;
	private final ModelPartExtension seat_4;
	private final ModelPartExtension handrail_3_r2;
	private final ModelPartExtension handrail_6_r2;
	private final ModelPartExtension seat_back_3_r1;
	private final ModelPartExtension seat_back_2_r2;
	private final ModelPartExtension side_panel;
	private final ModelPartExtension handrail_1_r1;
	private final ModelPartExtension side_panel_translucent;
	private final ModelPartExtension light_window;
	private final ModelPartExtension cube_r1;
	private final ModelPartExtension cube_r2;
	private final ModelPartExtension light_door;
	private final ModelPartExtension cube_r3;
	private final ModelPartExtension light_end;
	private final ModelPartExtension cube_r4;
	private final ModelPartExtension cube_r5;
	private final ModelPartExtension light_head;
	private final ModelPartExtension cube_r6;
	private final ModelPartExtension cube_r7;
	private final ModelPartExtension side_seats_end;
	private final ModelPartExtension seat_5;
	private final ModelPartExtension handrail_6_r3;
	private final ModelPartExtension handrail_3_r3;
	private final ModelPartExtension seat_back_r3;
	private final ModelPartExtension seat_6;
	private final ModelPartExtension handrail_7_r1;
	private final ModelPartExtension handrail_4_r1;
	private final ModelPartExtension seat_back_r4;
	private final ModelPartExtension side_seats_head;
	private final ModelPartExtension seat_7;
	private final ModelPartExtension handrail_7_r2;
	private final ModelPartExtension handrail_4_r2;
	private final ModelPartExtension seat_back_r5;
	private final ModelPartExtension seat_8;
	private final ModelPartExtension handrail_8_r1;
	private final ModelPartExtension handrail_5_r4;
	private final ModelPartExtension seat_back_r6;
	private final ModelPartExtension end;
	private final ModelPartExtension window_5;
	private final ModelPartExtension roof_side_r4;
	private final ModelPartExtension window_bottom_r9;
	private final ModelPartExtension window_10;
	private final ModelPartExtension roof_side_r5;
	private final ModelPartExtension window_bottom_r10;
	private final ModelPartExtension end_exterior;
	private final ModelPartExtension window_7;
	private final ModelPartExtension roof_3_r4;
	private final ModelPartExtension roof_2_r4;
	private final ModelPartExtension roof_1_r3;
	private final ModelPartExtension back_wall_r1;
	private final ModelPartExtension window_bottom_r11;
	private final ModelPartExtension window_12;
	private final ModelPartExtension roof_4_r2;
	private final ModelPartExtension roof_3_r5;
	private final ModelPartExtension roof_2_r5;
	private final ModelPartExtension back_wall_r2;
	private final ModelPartExtension window_bottom_r12;
	private final ModelPartExtension head;
	private final ModelPartExtension window_11;
	private final ModelPartExtension roof_side_r6;
	private final ModelPartExtension window_bottom_r13;
	private final ModelPartExtension window_14;
	private final ModelPartExtension roof_side_r7;
	private final ModelPartExtension window_bottom_r14;
	private final ModelPartExtension head_exterior;
	private final ModelPartExtension window_9;
	private final ModelPartExtension door_top_r7;
	private final ModelPartExtension roof_4_r3;
	private final ModelPartExtension roof_3_r6;
	private final ModelPartExtension roof_2_r6;
	private final ModelPartExtension back_wall_r3;
	private final ModelPartExtension window_16;
	private final ModelPartExtension door_top_r8;
	private final ModelPartExtension roof_5_r1;
	private final ModelPartExtension roof_4_r4;
	private final ModelPartExtension roof_3_r7;
	private final ModelPartExtension back_wall_r4;
	private final ModelPartExtension headlights;
	private final ModelPartExtension tail_lights;

	public ModelLondonUndergroundD78() {
		this(DoorAnimationType.STANDARD, true);
	}

	protected ModelLondonUndergroundD78(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		super(272, 272, doorAnimationType, renderDoorOverlay);

		window = createModelPart();
		window.setPivot(0, 24, 0);


		window_1 = createModelPart();
		window_1.setPivot(0, 0, 0);
		window.addChild(window_1);
		window_1.setTextureUVOffset(88, 0).addCuboid(-21, 0, 0, 21, 1, 23, 0, false);
		window_1.setTextureUVOffset(28, 0).addCuboid(-13, -39, 0, 13, 0, 23, 0, false);

		roof_side_r1 = createModelPart();
		roof_side_r1.setPivot(-15.5823F, -35.5941F, 11.5F);
		window_1.addChild(roof_side_r1);
		setRotationAngle(roof_side_r1, 0, 0, 0.4363F);
		roof_side_r1.setTextureUVOffset(188, 80).addCuboid(0, -3, -11.5F, 0, 6, 23, 0, false);

		window_bottom_r1 = createModelPart();
		window_bottom_r1.setPivot(-21, 0, 0);
		window_1.addChild(window_bottom_r1);
		setRotationAngle(window_bottom_r1, 0, 0, 0.0349F);
		window_bottom_r1.setTextureUVOffset(99, 93).addCuboid(0, -33, 0, 3, 33, 23, 0, false);

		window_3 = createModelPart();
		window_3.setPivot(0, 0, 0);
		window.addChild(window_3);
		window_3.setTextureUVOffset(88, 0).addCuboid(0, 0, 0, 21, 1, 23, 0, true);
		window_3.setTextureUVOffset(28, 0).addCuboid(0, -39, 0, 13, 0, 23, 0, true);

		roof_side_r2 = createModelPart();
		roof_side_r2.setPivot(15.5823F, -35.5941F, 11.5F);
		window_3.addChild(roof_side_r2);
		setRotationAngle(roof_side_r2, 0, 0, -0.4363F);
		roof_side_r2.setTextureUVOffset(188, 80).addCuboid(0, -3, -11.5F, 0, 6, 23, 0, true);

		window_bottom_r2 = createModelPart();
		window_bottom_r2.setPivot(21, 0, 0);
		window_3.addChild(window_bottom_r2);
		setRotationAngle(window_bottom_r2, 0, 0, -0.0349F);
		window_bottom_r2.setTextureUVOffset(99, 93).addCuboid(-3, -33, 0, 3, 33, 23, 0, true);

		window_2_partial = createModelPart();
		window_2_partial.setPivot(0, 24, 0);


		window_13 = createModelPart();
		window_13.setPivot(0, 0, 0);
		window_2_partial.addChild(window_13);


		window_bottom_r3 = createModelPart();
		window_bottom_r3.setPivot(-21, 0, 0);
		window_13.addChild(window_bottom_r3);
		setRotationAngle(window_bottom_r3, 0, 0, 0.0349F);
		window_bottom_r3.setTextureUVOffset(0, 126).addCuboid(3, -33, 0, 0, 33, 23, 0, false);

		window_4 = createModelPart();
		window_4.setPivot(0, 0, 0);
		window_2_partial.addChild(window_4);


		window_bottom_r4 = createModelPart();
		window_bottom_r4.setPivot(21, 0, 0);
		window_4.addChild(window_bottom_r4);
		setRotationAngle(window_bottom_r4, 0, 0, -0.0349F);
		window_bottom_r4.setTextureUVOffset(0, 126).addCuboid(-3, -33, 0, 0, 33, 23, 0, true);

		window_exterior = createModelPart();
		window_exterior.setPivot(0, 24, 0);


		window_2 = createModelPart();
		window_2.setPivot(0, 0, 0);
		window_exterior.addChild(window_2);
		window_2.setTextureUVOffset(203, 204).addCuboid(-21, 0, 0, 1, 1, 23, 0, false);
		window_2.setTextureUVOffset(189, 27).addCuboid(-20, 1, 0, 1, 4, 23, 0, false);
		window_2.setTextureUVOffset(0, 0).addCuboid(-3.8669F, -42.7901F, 0, 4, 0, 23, 0, false);

		roof_3_r1 = createModelPart();
		roof_3_r1.setPivot(-8.7907F, -41.9219F, 0);
		window_2.addChild(roof_3_r1);
		setRotationAngle(roof_3_r1, 0, 0, 1.3963F);
		roof_3_r1.setTextureUVOffset(150, 55).addCuboid(0, -5, 0, 0, 7, 23, 0, false);

		roof_2_r1 = createModelPart();
		roof_2_r1.setPivot(-14.2241F, -39.5747F, 0);
		window_2.addChild(roof_2_r1);
		setRotationAngle(roof_2_r1, 0, 0, 1.0472F);
		roof_2_r1.setTextureUVOffset(72, 87).addCuboid(0, -4, 0, 0, 6, 23, 0, false);

		roof_1_r1 = createModelPart();
		roof_1_r1.setPivot(-18.8485F, -35.1277F, 0);
		window_2.addChild(roof_1_r1);
		setRotationAngle(roof_1_r1, 0, 0, 0.6981F);
		roof_1_r1.setTextureUVOffset(150, 62).addCuboid(0, -4.5F, 0, 0, 6, 23, 0, false);

		window_bottom_r5 = createModelPart();
		window_bottom_r5.setPivot(-21, 0, 0);
		window_2.addChild(window_bottom_r5);
		setRotationAngle(window_bottom_r5, 0, 0, 0.0349F);
		window_bottom_r5.setTextureUVOffset(128, 126).addCuboid(0, -34, 0, 1, 34, 23, 0, false);

		window_6 = createModelPart();
		window_6.setPivot(0, 0, 0);
		window_exterior.addChild(window_6);
		window_6.setTextureUVOffset(203, 204).addCuboid(20, 0, 0, 1, 1, 23, 0, true);
		window_6.setTextureUVOffset(189, 27).addCuboid(19, 1, 0, 1, 4, 23, 0, true);
		window_6.setTextureUVOffset(0, 0).addCuboid(-0.1331F, -42.7901F, 0, 4, 0, 23, 0, true);

		roof_4_r1 = createModelPart();
		roof_4_r1.setPivot(8.7907F, -41.9219F, 0);
		window_6.addChild(roof_4_r1);
		setRotationAngle(roof_4_r1, 0, 0, -1.3963F);
		roof_4_r1.setTextureUVOffset(150, 55).addCuboid(0, -5, 0, 0, 7, 23, 0, true);

		roof_3_r2 = createModelPart();
		roof_3_r2.setPivot(14.2241F, -39.5747F, 0);
		window_6.addChild(roof_3_r2);
		setRotationAngle(roof_3_r2, 0, 0, -1.0472F);
		roof_3_r2.setTextureUVOffset(72, 87).addCuboid(0, -4, 0, 0, 6, 23, 0, true);

		roof_2_r2 = createModelPart();
		roof_2_r2.setPivot(18.8485F, -35.1277F, 0);
		window_6.addChild(roof_2_r2);
		setRotationAngle(roof_2_r2, 0, 0, -0.6981F);
		roof_2_r2.setTextureUVOffset(150, 62).addCuboid(0, -4.5F, 0, 0, 6, 23, 0, true);

		window_bottom_r6 = createModelPart();
		window_bottom_r6.setPivot(21, 0, 0);
		window_6.addChild(window_bottom_r6);
		setRotationAngle(window_bottom_r6, 0, 0, -0.0349F);
		window_bottom_r6.setTextureUVOffset(128, 126).addCuboid(-1, -34, 0, 1, 34, 23, 0, true);

		window_exterior_2_partial = createModelPart();
		window_exterior_2_partial.setPivot(0, 24, 0);


		window_15 = createModelPart();
		window_15.setPivot(0, 0, 0);
		window_exterior_2_partial.addChild(window_15);


		window_bottom_r7 = createModelPart();
		window_bottom_r7.setPivot(-21, 0, 0);
		window_15.addChild(window_bottom_r7);
		setRotationAngle(window_bottom_r7, 0, 0, 0.0349F);
		window_bottom_r7.setTextureUVOffset(88, 1).addCuboid(0, -34, 0, 0, 34, 23, 0, false);

		window_8 = createModelPart();
		window_8.setPivot(0, 0, 0);
		window_exterior_2_partial.addChild(window_8);


		window_bottom_r8 = createModelPart();
		window_bottom_r8.setPivot(21, 0, 0);
		window_8.addChild(window_bottom_r8);
		setRotationAngle(window_bottom_r8, 0, 0, -0.0349F);
		window_bottom_r8.setTextureUVOffset(88, 1).addCuboid(0, -34, 0, 0, 34, 23, 0, true);

		logo = createModelPart();
		logo.setPivot(0, 24, 0);


		logo_r1 = createModelPart();
		logo_r1.setPivot(-19, 0, 0);
		logo.addChild(logo_r1);
		setRotationAngle(logo_r1, 0, 0, 0.0349F);
		logo_r1.setTextureUVOffset(238, 15).addCuboid(-2.1F, -15, -4, 0, 8, 8, 0, false);

		door = createModelPart();
		door.setPivot(0, 24, 0);
		door.setTextureUVOffset(150, 59).addCuboid(-21, 0, -9, 21, 1, 18, 0, false);
		door.setTextureUVOffset(59, 0).addCuboid(-13, -39, -9, 13, 0, 18, 0, false);

		roof_side_r3 = createModelPart();
		roof_side_r3.setPivot(-15.5823F, -35.5941F, 0);
		door.addChild(roof_side_r3);
		setRotationAngle(roof_side_r3, 0, 0, 0.4363F);
		roof_side_r3.setTextureUVOffset(128, 41).addCuboid(0, -3, -9, 0, 6, 18, 0, false);

		door_top_r1 = createModelPart();
		door_top_r1.setPivot(-21, 0, 0);
		door.addChild(door_top_r1);
		setRotationAngle(door_top_r1, 0, 0, 0.0349F);
		door_top_r1.setTextureUVOffset(168, 242).addCuboid(1, -33.5F, -9, 2, 2, 18, 0, false);

		door_sliding_1 = createModelPart();
		door_sliding_1.setPivot(0, 24, 0);


		door_sliding_1_part = createModelPart();
		door_sliding_1_part.setPivot(0, 0, 0);
		door_sliding_1.addChild(door_sliding_1_part);


		door_top_r2 = createModelPart();
		door_top_r2.setPivot(-21, 0, 0);
		door_sliding_1_part.addChild(door_top_r2);
		setRotationAngle(door_top_r2, 0, 0, 0.0349F);
		door_top_r2.setTextureUVOffset(158, 165).addCuboid(0.5F, -32, -9, 1, 32, 18, 0, false);

		door_sliding_2 = createModelPart();
		door_sliding_2.setPivot(0, 24, 0);


		door_sliding_2_part = createModelPart();
		door_sliding_2_part.setPivot(0, 0, 0);
		door_sliding_2.addChild(door_sliding_2_part);


		door_top_r3 = createModelPart();
		door_top_r3.setPivot(-21, 0, 0);
		door_sliding_2_part.addChild(door_top_r3);
		setRotationAngle(door_top_r3, 0, 0, 0.0349F);
		door_top_r3.setTextureUVOffset(28, 165).addCuboid(0.5F, -32, -9, 1, 32, 18, 0, false);

		door_handrails = createModelPart();
		door_handrails.setPivot(0, 24, 0);


		handrail_2_r1 = createModelPart();
		handrail_2_r1.setPivot(-8, -9, 0);
		door_handrails.addChild(handrail_2_r1);
		setRotationAngle(handrail_2_r1, 0, 0, -0.0349F);
		handrail_2_r1.setTextureUVOffset(268, 216).addCuboid(0, -30, 0, 0, 5, 0, 0.2F, false);

		handrail_5_r1 = createModelPart();
		handrail_5_r1.setPivot(-8, -9, 0);
		door_handrails.addChild(handrail_5_r1);
		setRotationAngle(handrail_5_r1, -1.5708F, 0, -0.0349F);
		handrail_5_r1.setTextureUVOffset(268, 216).addCuboid(0, -14, -25, 0, 28, 0, 0.2F, false);

		door_exterior = createModelPart();
		door_exterior.setPivot(0, 24, 0);
		door_exterior.setTextureUVOffset(128, 93).addCuboid(-21, 0, -9, 1, 1, 18, 0, false);
		door_exterior.setTextureUVOffset(223, 96).addCuboid(-20, 1, -9, 1, 4, 18, 0, false);
		door_exterior.setTextureUVOffset(13, 0).addCuboid(-3.8669F, -42.7901F, -9, 4, 0, 18, 0, false);

		roof_3_r3 = createModelPart();
		roof_3_r3.setPivot(-8.7907F, -41.9219F, 0);
		door_exterior.addChild(roof_3_r3);
		setRotationAngle(roof_3_r3, 0, 0, 1.3963F);
		roof_3_r3.setTextureUVOffset(178, 151).addCuboid(0, -5, -9, 0, 7, 18, 0, false);

		roof_2_r3 = createModelPart();
		roof_2_r3.setPivot(-14.2241F, -39.5747F, 0);
		door_exterior.addChild(roof_2_r3);
		setRotationAngle(roof_2_r3, 0, 0, 1.0472F);
		roof_2_r3.setTextureUVOffset(148, 81).addCuboid(0, -4, -9, 0, 6, 18, 0, false);

		roof_1_r2 = createModelPart();
		roof_1_r2.setPivot(-18.8485F, -35.1277F, 0);
		door_exterior.addChild(roof_1_r2);
		setRotationAngle(roof_1_r2, 0, 0, 0.6981F);
		roof_1_r2.setTextureUVOffset(148, 87).addCuboid(0, -4.5F, -9, 0, 6, 18, 0, false);

		door_top_r4 = createModelPart();
		door_top_r4.setPivot(-21, 0, 0);
		door_exterior.addChild(door_top_r4);
		setRotationAngle(door_top_r4, 0, 0, 0.0349F);
		door_top_r4.setTextureUVOffset(218, 29).addCuboid(0, -34, -9, 1, 2, 18, 0, false);

		door_sliding_exterior_1 = createModelPart();
		door_sliding_exterior_1.setPivot(0, 24, 0);


		door_sliding_exterior_1_part = createModelPart();
		door_sliding_exterior_1_part.setPivot(0, 0, 0);
		door_sliding_exterior_1.addChild(door_sliding_exterior_1_part);


		door_top_r5 = createModelPart();
		door_top_r5.setPivot(-21, 0, 0);
		door_sliding_exterior_1_part.addChild(door_top_r5);
		setRotationAngle(door_top_r5, 0, 0, 0.0349F);
		door_top_r5.setTextureUVOffset(176, 0).addCuboid(0.5F, -32, -9, 0, 32, 18, 0, false);

		door_sliding_exterior_2 = createModelPart();
		door_sliding_exterior_2.setPivot(0, 24, 0);


		door_sliding_exterior_2_part = createModelPart();
		door_sliding_exterior_2_part.setPivot(0, 0, 0);
		door_sliding_exterior_2.addChild(door_sliding_exterior_2_part);


		door_top_r6 = createModelPart();
		door_top_r6.setPivot(-21, 0, 0);
		door_sliding_exterior_2_part.addChild(door_top_r6);
		setRotationAngle(door_top_r6, 0, 0, 0.0349F);
		door_top_r6.setTextureUVOffset(153, 94).addCuboid(0.5F, -32, -9, 0, 32, 18, 0, false);

		side_seats = createModelPart();
		side_seats.setPivot(0, 24, 0);


		seat_1 = createModelPart();
		seat_1.setPivot(0, 0, -29);
		side_seats.addChild(seat_1);
		seat_1.setTextureUVOffset(46, 196).addCuboid(-15, -7, 29, 6, 2, 20, 0, false);
		seat_1.setTextureUVOffset(82, 73).addCuboid(-11, -5, 29, 0, 5, 20, 0, false);

		handrail_5_r2 = createModelPart();
		handrail_5_r2.setPivot(-9, -9, 29);
		seat_1.addChild(handrail_5_r2);
		setRotationAngle(handrail_5_r2, -1.5708F, 0, -0.0349F);
		handrail_5_r2.setTextureUVOffset(268, 216).addCuboid(0, -20, -24, 0, 20, 0, 0.2F, false);

		handrail_2_r2 = createModelPart();
		handrail_2_r2.setPivot(-9, -9, 29);
		seat_1.addChild(handrail_2_r2);
		setRotationAngle(handrail_2_r2, 0, 0, -0.0349F);
		handrail_2_r2.setTextureUVOffset(268, 216).addCuboid(0, -30, 7, 0, 6, 0, 0.2F, false);

		seat_back_r1 = createModelPart();
		seat_back_r1.setPivot(-15, -7, 29);
		seat_1.addChild(seat_back_r1);
		setRotationAngle(seat_back_r1, 0, 0, -0.1745F);
		seat_back_r1.setTextureUVOffset(205, 137).addCuboid(-2, -8, 0, 2, 8, 20, 0, false);

		seat_2 = createModelPart();
		seat_2.setPivot(0, 0, -29);
		side_seats.addChild(seat_2);
		seat_2.setTextureUVOffset(46, 196).addCuboid(9, -7, 29, 6, 2, 20, 0, true);
		seat_2.setTextureUVOffset(82, 73).addCuboid(11, -5, 29, 0, 5, 20, 0, true);

		handrail_6_r1 = createModelPart();
		handrail_6_r1.setPivot(9, -9, 29);
		seat_2.addChild(handrail_6_r1);
		setRotationAngle(handrail_6_r1, -1.5708F, 0, 0.0349F);
		handrail_6_r1.setTextureUVOffset(268, 216).addCuboid(0, -20, -24, 0, 20, 0, 0.2F, true);

		handrail_3_r1 = createModelPart();
		handrail_3_r1.setPivot(9, -9, 29);
		seat_2.addChild(handrail_3_r1);
		setRotationAngle(handrail_3_r1, 0, 0, 0.0349F);
		handrail_3_r1.setTextureUVOffset(268, 216).addCuboid(0, -30, 7, 0, 6, 0, 0.2F, true);

		seat_back_r2 = createModelPart();
		seat_back_r2.setPivot(15, -7, 29);
		seat_2.addChild(seat_back_r2);
		setRotationAngle(seat_back_r2, 0, 0, 0.1745F);
		seat_back_r2.setTextureUVOffset(205, 137).addCuboid(0, -8, 0, 2, 8, 20, 0, true);

		middle_seats = createModelPart();
		middle_seats.setPivot(0, 24, 0);


		seat_3 = createModelPart();
		seat_3.setPivot(0, 0, 0);
		middle_seats.addChild(seat_3);
		seat_3.setTextureUVOffset(214, 184).addCuboid(-15, -7, 12, 6, 2, 8, 0, false);
		seat_3.setTextureUVOffset(236, 10).addCuboid(-15, -5, 12, 4, 5, 8, 0, false);
		seat_3.setTextureUVOffset(226, 0).addCuboid(-18, -7, 4, 14, 2, 8, 0, false);
		seat_3.setTextureUVOffset(210, 228).addCuboid(-18, -5, 6, 12, 5, 6, 0, false);
		seat_3.setTextureUVOffset(122, 234).addCuboid(-18, -15, 11, 14, 8, 1, 0, false);

		handrail_2_r3 = createModelPart();
		handrail_2_r3.setPivot(-9, -9, 0);
		seat_3.addChild(handrail_2_r3);
		setRotationAngle(handrail_2_r3, 0, 0, -0.0349F);
		handrail_2_r3.setTextureUVOffset(268, 216).addCuboid(0, -30, 7, 0, 6, 0, 0.2F, false);

		handrail_5_r3 = createModelPart();
		handrail_5_r3.setPivot(-9, -9, 0);
		seat_3.addChild(handrail_5_r3);
		setRotationAngle(handrail_5_r3, -1.5708F, 0, -0.0349F);
		handrail_5_r3.setTextureUVOffset(268, 216).addCuboid(0, -20, -24, 0, 20, 0, 0.2F, false);

		seat_back_2_r1 = createModelPart();
		seat_back_2_r1.setPivot(-11, -7, 10);
		seat_3.addChild(seat_back_2_r1);
		setRotationAngle(seat_back_2_r1, -0.1745F, 0, 0);
		seat_back_2_r1.setTextureUVOffset(235, 72).addCuboid(-7, -7, 0, 14, 7, 1, 0, false);

		seat_back_1_r1 = createModelPart();
		seat_back_1_r1.setPivot(-15, -7, 0);
		seat_3.addChild(seat_back_1_r1);
		setRotationAngle(seat_back_1_r1, 0, 0, -0.1745F);
		seat_back_1_r1.setTextureUVOffset(0, 7).addCuboid(-2, -8, 12, 2, 8, 8, 0, false);

		seat_4 = createModelPart();
		seat_4.setPivot(0, 0, 0);
		middle_seats.addChild(seat_4);
		seat_4.setTextureUVOffset(214, 184).addCuboid(9, -7, 12, 6, 2, 8, 0, true);
		seat_4.setTextureUVOffset(236, 10).addCuboid(11, -5, 12, 4, 5, 8, 0, true);
		seat_4.setTextureUVOffset(226, 0).addCuboid(4, -7, 4, 14, 2, 8, 0, true);
		seat_4.setTextureUVOffset(210, 228).addCuboid(6, -5, 6, 12, 5, 6, 0, true);
		seat_4.setTextureUVOffset(122, 234).addCuboid(4, -15, 11, 14, 8, 1, 0, true);

		handrail_3_r2 = createModelPart();
		handrail_3_r2.setPivot(9, -9, 0);
		seat_4.addChild(handrail_3_r2);
		setRotationAngle(handrail_3_r2, 0, 0, 0.0349F);
		handrail_3_r2.setTextureUVOffset(268, 216).addCuboid(0, -30, 7, 0, 6, 0, 0.2F, true);

		handrail_6_r2 = createModelPart();
		handrail_6_r2.setPivot(9, -9, 0);
		seat_4.addChild(handrail_6_r2);
		setRotationAngle(handrail_6_r2, -1.5708F, 0, 0.0349F);
		handrail_6_r2.setTextureUVOffset(268, 216).addCuboid(0, -20, -24, 0, 20, 0, 0.2F, true);

		seat_back_3_r1 = createModelPart();
		seat_back_3_r1.setPivot(11, -7, 10);
		seat_4.addChild(seat_back_3_r1);
		setRotationAngle(seat_back_3_r1, -0.1745F, 0, 0);
		seat_back_3_r1.setTextureUVOffset(235, 72).addCuboid(-7, -7, 0, 14, 7, 1, 0, true);

		seat_back_2_r2 = createModelPart();
		seat_back_2_r2.setPivot(15, -7, 0);
		seat_4.addChild(seat_back_2_r2);
		setRotationAngle(seat_back_2_r2, 0, 0, 0.1745F);
		seat_back_2_r2.setTextureUVOffset(0, 7).addCuboid(0, -8, 12, 2, 8, 8, 0, true);

		side_panel = createModelPart();
		side_panel.setPivot(0, 24, 0);
		side_panel.setTextureUVOffset(76, 116).addCuboid(-18, -30, 0, 9, 30, 0, 0, false);

		handrail_1_r1 = createModelPart();
		handrail_1_r1.setPivot(-9, -9, 0);
		side_panel.addChild(handrail_1_r1);
		setRotationAngle(handrail_1_r1, 0, 0, -0.0349F);
		handrail_1_r1.setTextureUVOffset(268, 216).addCuboid(0, -30, 0, 0, 30, 0, 0.2F, false);

		side_panel_translucent = createModelPart();
		side_panel_translucent.setPivot(0, 24, 0);
		side_panel_translucent.setTextureUVOffset(0, 245).addCuboid(-18, -30, 0, 9, 20, 0, 0, false);

		light_window = createModelPart();
		light_window.setPivot(0, 24, 0);


		cube_r1 = createModelPart();
		cube_r1.setPivot(-0.9792F, -0.1101F, 0);
		light_window.addChild(cube_r1);
		setRotationAngle(cube_r1, 0, 0, -0.7854F);
		cube_r1.setTextureUVOffset(188, 78).addCuboid(17, -37, 0, 2, 1, 23, 0, true);

		cube_r2 = createModelPart();
		cube_r2.setPivot(0.9792F, -0.1101F, 0);
		light_window.addChild(cube_r2);
		setRotationAngle(cube_r2, 0, 0, 0.7854F);
		cube_r2.setTextureUVOffset(188, 78).addCuboid(-19, -37, 0, 2, 1, 23, 0, false);

		light_door = createModelPart();
		light_door.setPivot(0, 24, 0);


		cube_r3 = createModelPart();
		cube_r3.setPivot(-0.9792F, -0.1101F, 0);
		light_door.addChild(cube_r3);
		setRotationAngle(cube_r3, 0, 0, -0.7854F);
		cube_r3.setTextureUVOffset(190, 244).addCuboid(17, -37, -9, 2, 1, 18, 0, true);

		light_end = createModelPart();
		light_end.setPivot(0, 24, 0);


		cube_r4 = createModelPart();
		cube_r4.setPivot(0.9792F, -0.1101F, 0);
		light_end.addChild(cube_r4);
		setRotationAngle(cube_r4, 0, 0, 0.7854F);
		cube_r4.setTextureUVOffset(243, 84).addCuboid(-19, -37, 0, 2, 1, 12, 0, true);

		cube_r5 = createModelPart();
		cube_r5.setPivot(-0.9792F, -0.1101F, 0);
		light_end.addChild(cube_r5);
		setRotationAngle(cube_r5, 0, 0, -0.7854F);
		cube_r5.setTextureUVOffset(243, 84).addCuboid(17, -37, 0, 2, 1, 12, 0, false);

		light_head = createModelPart();
		light_head.setPivot(0, 24, 0);


		cube_r6 = createModelPart();
		cube_r6.setPivot(0.9792F, -0.1101F, 5);
		light_head.addChild(cube_r6);
		setRotationAngle(cube_r6, 0, 0, 0.7854F);
		cube_r6.setTextureUVOffset(212, 239).addCuboid(-19, -37, -5, 2, 1, 19, 0, true);

		cube_r7 = createModelPart();
		cube_r7.setPivot(-0.9792F, -0.1101F, 5);
		light_head.addChild(cube_r7);
		setRotationAngle(cube_r7, 0, 0, -0.7854F);
		cube_r7.setTextureUVOffset(212, 239).addCuboid(17, -37, -5, 2, 1, 19, 0, false);

		side_seats_end = createModelPart();
		side_seats_end.setPivot(0, 24, 0);


		seat_5 = createModelPart();
		seat_5.setPivot(0, 0, -29);
		side_seats_end.addChild(seat_5);
		seat_5.setTextureUVOffset(186, 228).addCuboid(-15, -7, 29, 6, 2, 12, 0, false);
		seat_5.setTextureUVOffset(82, 72).addCuboid(-11, -5, 29, 0, 5, 12, 0, false);

		handrail_6_r3 = createModelPart();
		handrail_6_r3.setPivot(-9, -9, 29);
		seat_5.addChild(handrail_6_r3);
		setRotationAngle(handrail_6_r3, -1.5708F, 0, -0.0349F);
		handrail_6_r3.setTextureUVOffset(268, 216).addCuboid(0, -7, -24, 0, 7, 0, 0.2F, false);

		handrail_3_r3 = createModelPart();
		handrail_3_r3.setPivot(-9, -9, 29);
		seat_5.addChild(handrail_3_r3);
		setRotationAngle(handrail_3_r3, 0, 0, -0.0349F);
		handrail_3_r3.setTextureUVOffset(268, 216).addCuboid(0, -30, 7, 0, 6, 0, 0.2F, false);

		seat_back_r3 = createModelPart();
		seat_back_r3.setPivot(-15, -7, 29);
		seat_5.addChild(seat_back_r3);
		setRotationAngle(seat_back_r3, 0, 0, -0.1745F);
		seat_back_r3.setTextureUVOffset(228, 202).addCuboid(-2, -8, 0, 2, 8, 12, 0, false);

		seat_6 = createModelPart();
		seat_6.setPivot(0, 0, -29);
		side_seats_end.addChild(seat_6);
		seat_6.setTextureUVOffset(186, 228).addCuboid(9, -7, 29, 6, 2, 12, 0, true);
		seat_6.setTextureUVOffset(82, 72).addCuboid(11, -5, 29, 0, 5, 12, 0, true);

		handrail_7_r1 = createModelPart();
		handrail_7_r1.setPivot(9, -9, 29);
		seat_6.addChild(handrail_7_r1);
		setRotationAngle(handrail_7_r1, -1.5708F, 0, 0.0349F);
		handrail_7_r1.setTextureUVOffset(268, 216).addCuboid(0, -7, -24, 0, 7, 0, 0.2F, true);

		handrail_4_r1 = createModelPart();
		handrail_4_r1.setPivot(9, -9, 29);
		seat_6.addChild(handrail_4_r1);
		setRotationAngle(handrail_4_r1, 0, 0, 0.0349F);
		handrail_4_r1.setTextureUVOffset(268, 216).addCuboid(0, -30, 7, 0, 6, 0, 0.2F, true);

		seat_back_r4 = createModelPart();
		seat_back_r4.setPivot(15, -7, 29);
		seat_6.addChild(seat_back_r4);
		setRotationAngle(seat_back_r4, 0, 0, 0.1745F);
		seat_back_r4.setTextureUVOffset(228, 202).addCuboid(0, -8, 0, 2, 8, 12, 0, true);

		side_seats_head = createModelPart();
		side_seats_head.setPivot(0, 24, 0);


		seat_7 = createModelPart();
		seat_7.setPivot(0, 0, -29);
		side_seats_head.addChild(seat_7);
		seat_7.setTextureUVOffset(210, 54).addCuboid(-15, -7, 32, 6, 2, 16, 0, false);
		seat_7.setTextureUVOffset(134, 8).addCuboid(-11, -5, 32, 0, 5, 16, 0, false);

		handrail_7_r2 = createModelPart();
		handrail_7_r2.setPivot(-9, -9, 29);
		seat_7.addChild(handrail_7_r2);
		setRotationAngle(handrail_7_r2, -1.5708F, 0, -0.0349F);
		handrail_7_r2.setTextureUVOffset(268, 216).addCuboid(0, -10, -24, 0, 7, 0, 0.2F, false);

		handrail_4_r2 = createModelPart();
		handrail_4_r2.setPivot(-9, -9, 29);
		seat_7.addChild(handrail_4_r2);
		setRotationAngle(handrail_4_r2, 0, 0, -0.0349F);
		handrail_4_r2.setTextureUVOffset(268, 216).addCuboid(0, -30, 10, 0, 6, 0, 0.2F, false);

		seat_back_r5 = createModelPart();
		seat_back_r5.setPivot(-15, -7, 29);
		seat_7.addChild(seat_back_r5);
		setRotationAngle(seat_back_r5, 0, 0, -0.1745F);
		seat_back_r5.setTextureUVOffset(215, 72).addCuboid(-2, -8, 3, 2, 8, 16, 0, false);

		seat_8 = createModelPart();
		seat_8.setPivot(0, 0, -29);
		side_seats_head.addChild(seat_8);
		seat_8.setTextureUVOffset(210, 54).addCuboid(9, -7, 32, 6, 2, 16, 0, true);
		seat_8.setTextureUVOffset(134, 8).addCuboid(11, -5, 32, 0, 5, 16, 0, true);

		handrail_8_r1 = createModelPart();
		handrail_8_r1.setPivot(9, -9, 29);
		seat_8.addChild(handrail_8_r1);
		setRotationAngle(handrail_8_r1, -1.5708F, 0, 0.0349F);
		handrail_8_r1.setTextureUVOffset(268, 216).addCuboid(0, -10, -24, 0, 7, 0, 0.2F, true);

		handrail_5_r4 = createModelPart();
		handrail_5_r4.setPivot(9, -9, 29);
		seat_8.addChild(handrail_5_r4);
		setRotationAngle(handrail_5_r4, 0, 0, 0.0349F);
		handrail_5_r4.setTextureUVOffset(268, 216).addCuboid(0, -30, 10, 0, 6, 0, 0.2F, true);

		seat_back_r6 = createModelPart();
		seat_back_r6.setPivot(15, -7, 29);
		seat_8.addChild(seat_back_r6);
		setRotationAngle(seat_back_r6, 0, 0, 0.1745F);
		seat_back_r6.setTextureUVOffset(215, 72).addCuboid(0, -8, 3, 2, 8, 16, 0, true);

		end = createModelPart();
		end.setPivot(0, 24, 0);
		end.setTextureUVOffset(72, 218).addCuboid(-6, -34, 14, 12, 34, 0, 0, false);

		window_5 = createModelPart();
		window_5.setPivot(0, 0, 0);
		end.addChild(window_5);
		window_5.setTextureUVOffset(153, 0).addCuboid(-21, 0, 0, 21, 1, 14, 0, false);
		window_5.setTextureUVOffset(158, 215).addCuboid(-18, -39, 12, 12, 39, 2, 0, false);
		window_5.setTextureUVOffset(128, 93).addCuboid(-6, -39, 12, 6, 6, 2, 0, false);
		window_5.setTextureUVOffset(128, 101).addCuboid(-8, -39, 11, 8, 5, 1, 0, false);
		window_5.setTextureUVOffset(70, 66).addCuboid(-13, -39, 0, 13, 0, 12, 0, false);

		roof_side_r4 = createModelPart();
		roof_side_r4.setPivot(-15.5823F, -35.5941F, 11.5F);
		window_5.addChild(roof_side_r4);
		setRotationAngle(roof_side_r4, 0, 0, 0.4363F);
		roof_side_r4.setTextureUVOffset(82, 66).addCuboid(0, -3, -11.5F, 0, 6, 12, 0, false);

		window_bottom_r9 = createModelPart();
		window_bottom_r9.setPivot(-21, 0, 0);
		window_5.addChild(window_bottom_r9);
		setRotationAngle(window_bottom_r9, 0, 0, 0.0349F);
		window_bottom_r9.setTextureUVOffset(196, 182).addCuboid(0, -33, 0, 3, 33, 12, 0, false);

		window_10 = createModelPart();
		window_10.setPivot(0, 0, 0);
		end.addChild(window_10);
		window_10.setTextureUVOffset(153, 0).addCuboid(0, 0, 0, 21, 1, 14, 0, true);
		window_10.setTextureUVOffset(158, 215).addCuboid(6, -39, 12, 12, 39, 2, 0, true);
		window_10.setTextureUVOffset(128, 93).addCuboid(0, -39, 12, 6, 6, 2, 0, true);
		window_10.setTextureUVOffset(128, 101).addCuboid(0, -39, 11, 8, 5, 1, 0, true);
		window_10.setTextureUVOffset(70, 66).addCuboid(0, -39, 0, 13, 0, 12, 0, true);

		roof_side_r5 = createModelPart();
		roof_side_r5.setPivot(15.5823F, -35.5941F, 11.5F);
		window_10.addChild(roof_side_r5);
		setRotationAngle(roof_side_r5, 0, 0, -0.4363F);
		roof_side_r5.setTextureUVOffset(82, 66).addCuboid(0, -3, -11.5F, 0, 6, 12, 0, true);

		window_bottom_r10 = createModelPart();
		window_bottom_r10.setPivot(21, 0, 0);
		window_10.addChild(window_bottom_r10);
		setRotationAngle(window_bottom_r10, 0, 0, -0.0349F);
		window_bottom_r10.setTextureUVOffset(196, 182).addCuboid(-3, -33, 0, 3, 33, 12, 0, true);

		end_exterior = createModelPart();
		end_exterior.setPivot(0, 24, 0);
		end_exterior.setTextureUVOffset(28, 218).addCuboid(-6, -43, 15, 12, 43, 0, 0, false);

		window_7 = createModelPart();
		window_7.setPivot(0, 0, 0);
		end_exterior.addChild(window_7);
		window_7.setTextureUVOffset(0, 29).addCuboid(-21, 0, 0, 1, 1, 12, 0, false);
		window_7.setTextureUVOffset(0, 229).addCuboid(-20, 1, 0, 1, 4, 12, 0, false);
		window_7.setTextureUVOffset(82, 89).addCuboid(-19, 1, 11, 12, 2, 1, 0, false);
		window_7.setTextureUVOffset(229, 144).addCuboid(-7, 0, 11, 14, 4, 4, 0, false);
		window_7.setTextureUVOffset(0, 0).addCuboid(-3.8669F, -42.7901F, 0, 4, 0, 15, 0, false);

		roof_3_r4 = createModelPart();
		roof_3_r4.setPivot(-8.7907F, -41.9219F, 11.5F);
		window_7.addChild(roof_3_r4);
		setRotationAngle(roof_3_r4, 0, 0, 1.3963F);
		roof_3_r4.setTextureUVOffset(134, 14).addCuboid(0, -5, -11.5F, 0, 7, 15, 0, false);

		roof_2_r4 = createModelPart();
		roof_2_r4.setPivot(-14.2241F, -39.5747F, 11.5F);
		window_7.addChild(roof_2_r4);
		setRotationAngle(roof_2_r4, 0, 0, 1.0472F);
		roof_2_r4.setTextureUVOffset(122, 213).addCuboid(0, -4, -11.5F, 0, 6, 15, 0, false);

		roof_1_r3 = createModelPart();
		roof_1_r3.setPivot(-18.8485F, -35.1277F, 11.5F);
		window_7.addChild(roof_1_r3);
		setRotationAngle(roof_1_r3, 0, 0, 0.6981F);
		roof_1_r3.setTextureUVOffset(99, 135).addCuboid(0, -4.5F, -11.5F, 0, 6, 14, 0, false);

		back_wall_r1 = createModelPart();
		back_wall_r1.setPivot(-6, 0, 15);
		window_7.addChild(back_wall_r1);
		setRotationAngle(back_wall_r1, 0, -0.1745F, 0);
		back_wall_r1.setTextureUVOffset(189, 112).addCuboid(-16, -43, -1, 16, 44, 1, 0, false);

		window_bottom_r11 = createModelPart();
		window_bottom_r11.setPivot(-21, 0, 0);
		window_7.addChild(window_bottom_r11);
		setRotationAngle(window_bottom_r11, 0, 0, 0.0349F);
		window_bottom_r11.setTextureUVOffset(0, 182).addCuboid(0, -34, 0, 1, 34, 13, 0, false);

		window_12 = createModelPart();
		window_12.setPivot(0, 0, 0);
		end_exterior.addChild(window_12);
		window_12.setTextureUVOffset(0, 29).addCuboid(20, 0, 0, 1, 1, 12, 0, true);
		window_12.setTextureUVOffset(0, 229).addCuboid(19, 1, 0, 1, 4, 12, 0, true);
		window_12.setTextureUVOffset(82, 89).addCuboid(7, 1, 11, 12, 2, 1, 0, true);
		window_12.setTextureUVOffset(229, 144).addCuboid(-7, 0, 11, 14, 4, 4, 0, true);
		window_12.setTextureUVOffset(0, 0).addCuboid(-0.1331F, -42.7901F, 0, 4, 0, 15, 0, true);

		roof_4_r2 = createModelPart();
		roof_4_r2.setPivot(8.7907F, -41.9219F, 11.5F);
		window_12.addChild(roof_4_r2);
		setRotationAngle(roof_4_r2, 0, 0, -1.3963F);
		roof_4_r2.setTextureUVOffset(134, 14).addCuboid(0, -5, -11.5F, 0, 7, 15, 0, true);

		roof_3_r5 = createModelPart();
		roof_3_r5.setPivot(14.2241F, -39.5747F, 11.5F);
		window_12.addChild(roof_3_r5);
		setRotationAngle(roof_3_r5, 0, 0, -1.0472F);
		roof_3_r5.setTextureUVOffset(122, 213).addCuboid(0, -4, -11.5F, 0, 6, 15, 0, true);

		roof_2_r5 = createModelPart();
		roof_2_r5.setPivot(18.8485F, -35.1277F, 11.5F);
		window_12.addChild(roof_2_r5);
		setRotationAngle(roof_2_r5, 0, 0, -0.6981F);
		roof_2_r5.setTextureUVOffset(99, 135).addCuboid(0, -4.5F, -11.5F, 0, 6, 14, 0, true);

		back_wall_r2 = createModelPart();
		back_wall_r2.setPivot(6, 0, 15);
		window_12.addChild(back_wall_r2);
		setRotationAngle(back_wall_r2, 0, 0.1745F, 0);
		back_wall_r2.setTextureUVOffset(189, 112).addCuboid(0, -43, -1, 16, 44, 1, 0, true);

		window_bottom_r12 = createModelPart();
		window_bottom_r12.setPivot(21, 0, 0);
		window_12.addChild(window_bottom_r12);
		setRotationAngle(window_bottom_r12, 0, 0, -0.0349F);
		window_bottom_r12.setTextureUVOffset(0, 182).addCuboid(-1, -34, 0, 1, 34, 13, 0, true);

		head = createModelPart();
		head.setPivot(0, 24, 0);
		head.setTextureUVOffset(0, 110).addCuboid(-18, -39, 19, 36, 39, 0, 0, false);

		window_11 = createModelPart();
		window_11.setPivot(0, 0, -29);
		head.addChild(window_11);
		window_11.setTextureUVOffset(115, 39).addCuboid(-21, 0, 29, 21, 1, 19, 0, false);
		window_11.setTextureUVOffset(128, 101).addCuboid(-8, -39, 47, 8, 5, 1, 0, false);
		window_11.setTextureUVOffset(32, 23).addCuboid(-13, -39, 29, 13, 0, 19, 0, false);

		roof_side_r6 = createModelPart();
		roof_side_r6.setPivot(-15.5823F, -35.5941F, 40.5F);
		window_11.addChild(roof_side_r6);
		setRotationAngle(roof_side_r6, 0, 0, 0.4363F);
		roof_side_r6.setTextureUVOffset(82, 79).addCuboid(0, -3, -11.5F, 0, 6, 19, 0, false);

		window_bottom_r13 = createModelPart();
		window_bottom_r13.setPivot(-21, 0, 29);
		window_11.addChild(window_bottom_r13);
		setRotationAngle(window_bottom_r13, 0, 0, 0.0349F);
		window_bottom_r13.setTextureUVOffset(80, 164).addCuboid(0, -33, 0, 3, 33, 19, 0, false);

		window_14 = createModelPart();
		window_14.setPivot(0, 0, -29);
		head.addChild(window_14);
		window_14.setTextureUVOffset(115, 39).addCuboid(0, 0, 29, 21, 1, 19, 0, true);
		window_14.setTextureUVOffset(128, 101).addCuboid(0, -39, 47, 8, 5, 1, 0, true);
		window_14.setTextureUVOffset(32, 23).addCuboid(0, -39, 29, 13, 0, 19, 0, true);

		roof_side_r7 = createModelPart();
		roof_side_r7.setPivot(15.5823F, -35.5941F, 40.5F);
		window_14.addChild(roof_side_r7);
		setRotationAngle(roof_side_r7, 0, 0, -0.4363F);
		roof_side_r7.setTextureUVOffset(82, 79).addCuboid(0, -3, -11.5F, 0, 6, 19, 0, true);

		window_bottom_r14 = createModelPart();
		window_bottom_r14.setPivot(21, 0, 29);
		window_14.addChild(window_bottom_r14);
		setRotationAngle(window_bottom_r14, 0, 0, -0.0349F);
		window_bottom_r14.setTextureUVOffset(80, 164).addCuboid(-3, -33, 0, 3, 33, 19, 0, true);

		head_exterior = createModelPart();
		head_exterior.setPivot(0, 24, 0);
		head_exterior.setTextureUVOffset(98, 216).addCuboid(-6, -43, 43, 12, 43, 0, 0, false);
		head_exterior.setTextureUVOffset(0, 26).addCuboid(-16, 1, 40, 6, 2, 1, 0, false);
		head_exterior.setTextureUVOffset(0, 23).addCuboid(10, 1, 40, 6, 2, 1, 0, false);
		head_exterior.setTextureUVOffset(0, 66).addCuboid(-20, -43, 20, 40, 43, 0, 0, false);

		window_9 = createModelPart();
		window_9.setPivot(0, 0, 0);
		head_exterior.addChild(window_9);
		window_9.setTextureUVOffset(82, 66).addCuboid(-21, 0, 16, 21, 1, 26, 0, false);
		window_9.setTextureUVOffset(40, 69).addCuboid(-21, 0, 0, 1, 1, 40, 0, false);
		window_9.setTextureUVOffset(46, 22).addCuboid(-20, 1, 0, 1, 4, 40, 0, false);
		window_9.setTextureUVOffset(77, 18).addCuboid(-19, 1, 39, 12, 2, 1, 0, false);
		window_9.setTextureUVOffset(229, 136).addCuboid(-7, 0, 39, 14, 4, 4, 0, false);
		window_9.setTextureUVOffset(0, 0).addCuboid(-3.8669F, -42.7901F, 0, 4, 0, 43, 0, false);

		door_top_r7 = createModelPart();
		door_top_r7.setPivot(-21, 0, 32);
		window_9.addChild(door_top_r7);
		setRotationAngle(door_top_r7, 0, 0, 0.0349F);
		door_top_r7.setTextureUVOffset(105, 155).addCuboid(0, -34, -9, 1, 2, 10, 0, false);
		door_top_r7.setTextureUVOffset(214, 5).addCuboid(0.5F, -32, -9, 1, 32, 10, 0, false);
		door_top_r7.setTextureUVOffset(52, 218).addCuboid(0, -34, 1, 2, 34, 8, 0, false);
		door_top_r7.setTextureUVOffset(49, 126).addCuboid(0, -34, -32, 2, 34, 23, 0, false);

		roof_4_r3 = createModelPart();
		roof_4_r3.setPivot(-8.7907F, -41.9219F, 0);
		window_9.addChild(roof_4_r3);
		setRotationAngle(roof_4_r3, 0, 0, 1.3963F);
		roof_4_r3.setTextureUVOffset(0, 0).addCuboid(0, -5, 0, 0, 7, 43, 0, false);

		roof_3_r6 = createModelPart();
		roof_3_r6.setPivot(-14.2241F, -39.5747F, 0);
		window_9.addChild(roof_3_r6);
		setRotationAngle(roof_3_r6, 0, 0, 1.0472F);
		roof_3_r6.setTextureUVOffset(0, 7).addCuboid(0, -4, 0, 0, 6, 43, 0, false);

		roof_2_r6 = createModelPart();
		roof_2_r6.setPivot(-18.8485F, -35.1277F, 0);
		window_9.addChild(roof_2_r6);
		setRotationAngle(roof_2_r6, 0, 0, 0.6981F);
		roof_2_r6.setTextureUVOffset(0, 14).addCuboid(0, -4.5F, 0, 0, 6, 42, 0, false);

		back_wall_r3 = createModelPart();
		back_wall_r3.setPivot(-6, 0, 43);
		window_9.addChild(back_wall_r3);
		setRotationAngle(back_wall_r3, 0, -0.1745F, 0);
		back_wall_r3.setTextureUVOffset(124, 183).addCuboid(-16, -43, -1, 16, 44, 1, 0, false);

		window_16 = createModelPart();
		window_16.setPivot(0, 0, 0);
		head_exterior.addChild(window_16);
		window_16.setTextureUVOffset(82, 66).addCuboid(0, 0, 16, 21, 1, 26, 0, true);
		window_16.setTextureUVOffset(40, 69).addCuboid(20, 0, 0, 1, 1, 40, 0, true);
		window_16.setTextureUVOffset(46, 22).addCuboid(19, 1, 0, 1, 4, 40, 0, true);
		window_16.setTextureUVOffset(77, 18).addCuboid(7, 1, 39, 12, 2, 1, 0, true);
		window_16.setTextureUVOffset(229, 136).addCuboid(-7, 0, 39, 14, 4, 4, 0, true);
		window_16.setTextureUVOffset(0, 0).addCuboid(-0.1331F, -42.7901F, 0, 4, 0, 43, 0, true);

		door_top_r8 = createModelPart();
		door_top_r8.setPivot(21, 0, 32);
		window_16.addChild(door_top_r8);
		setRotationAngle(door_top_r8, 0, 0, -0.0349F);
		door_top_r8.setTextureUVOffset(105, 155).addCuboid(-1, -34, -9, 1, 2, 10, 0, true);
		door_top_r8.setTextureUVOffset(214, 5).addCuboid(-1.5F, -32, -9, 1, 32, 10, 0, true);
		door_top_r8.setTextureUVOffset(52, 218).addCuboid(-2, -34, 1, 2, 34, 8, 0, true);
		door_top_r8.setTextureUVOffset(49, 126).addCuboid(-2, -34, -32, 2, 34, 23, 0, true);

		roof_5_r1 = createModelPart();
		roof_5_r1.setPivot(8.7907F, -41.9219F, 0);
		window_16.addChild(roof_5_r1);
		setRotationAngle(roof_5_r1, 0, 0, -1.3963F);
		roof_5_r1.setTextureUVOffset(0, 0).addCuboid(0, -5, 0, 0, 7, 43, 0, true);

		roof_4_r4 = createModelPart();
		roof_4_r4.setPivot(14.2241F, -39.5747F, 0);
		window_16.addChild(roof_4_r4);
		setRotationAngle(roof_4_r4, 0, 0, -1.0472F);
		roof_4_r4.setTextureUVOffset(0, 7).addCuboid(0, -4, 0, 0, 6, 43, 0, true);

		roof_3_r7 = createModelPart();
		roof_3_r7.setPivot(18.8485F, -35.1277F, 0);
		window_16.addChild(roof_3_r7);
		setRotationAngle(roof_3_r7, 0, 0, -0.6981F);
		roof_3_r7.setTextureUVOffset(0, 14).addCuboid(0, -4.5F, 0, 0, 6, 42, 0, true);

		back_wall_r4 = createModelPart();
		back_wall_r4.setPivot(6, 0, 43);
		window_16.addChild(back_wall_r4);
		setRotationAngle(back_wall_r4, 0, 0.1745F, 0);
		back_wall_r4.setTextureUVOffset(124, 183).addCuboid(0, -43, -1, 16, 44, 1, 0, true);

		headlights = createModelPart();
		headlights.setPivot(0, 24, 0);
		headlights.setTextureUVOffset(14, 23).addCuboid(-14, 1, 41.1F, 2, 2, 0, 0, false);
		headlights.setTextureUVOffset(14, 23).addCuboid(12, 1, 41.1F, 2, 2, 0, 0, true);

		tail_lights = createModelPart();
		tail_lights.setPivot(0, 24, 0);
		tail_lights.setTextureUVOffset(14, 25).addCuboid(-16, 1, 41.1F, 2, 2, 0, 0, false);
		tail_lights.setTextureUVOffset(14, 25).addCuboid(-12, 1, 41.1F, 2, 2, 0, 0, false);
		tail_lights.setTextureUVOffset(14, 25).addCuboid(14, 1, 41.1F, 2, 2, 0, 0, true);

		buildModel();
	}

	private static final int DOOR_MAX = 17;

	@Override
	public ModelLondonUndergroundD78 createNew(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		return new ModelLondonUndergroundD78(doorAnimationType, renderDoorOverlay);
	}

	@Override
	protected void renderWindowPositions(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
		final boolean isEnd1 = isIndex(0, position, getWindowPositions());
		final boolean isEnd2 = isIndex(-1, position, getWindowPositions());

		switch (renderStage) {
			case LIGHT:
				renderOnceFlipped(light_window, graphicsHolder, light, position);
				renderOnce(light_window, graphicsHolder, light, position);
				break;
			case INTERIOR:
				renderMirror(window, graphicsHolder, light, position);
				if (renderDetails) {
					renderMirror(isEnd1 || isEnd2 ? side_seats : middle_seats, graphicsHolder, light, position);
					if (!isEnd1 && !isEnd2) {
						renderMirror(window_2_partial, graphicsHolder, light, position);
					}
				}
				break;
			case EXTERIOR:
				renderMirror(window_exterior, graphicsHolder, light, position);
				if (renderDetails && !isEnd1 && !isEnd2) {
					renderMirror(window_exterior_2_partial, graphicsHolder, light, position);
					renderMirror(logo, graphicsHolder, light, position);
				}
				break;
		}
	}

	@Override
	protected void renderDoorPositions(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
		final int[] doorPositions = getDoorPositions();
		boolean isOdd = false;
		for (int i = 0; i < doorPositions.length; i++) {
			if (isIndex(i, position, doorPositions)) {
				isOdd = (i % 2) == 1;
				break;
			}
		}

		switch (renderStage) {
			case LIGHT:
				renderMirror(light_door, graphicsHolder, light, position);
				break;
			case INTERIOR:
				renderMirror(door, graphicsHolder, light, position);

				(isOdd ? door_sliding_1_part : door_sliding_2_part).setOffset(0, 0, (isOdd ? -1 : 1) * doorRightZ);
				renderOnce(isOdd ? door_sliding_1 : door_sliding_2, graphicsHolder, light, position);
				(isOdd ? door_sliding_2_part : door_sliding_1_part).setOffset(0, 0, (isOdd ? 1 : -1) * doorLeftZ);
				renderOnceFlipped(isOdd ? door_sliding_2 : door_sliding_1, graphicsHolder, light, position);

				if (renderDetails) {
					renderMirror(side_panel, graphicsHolder, light, position - 11.9F);
					renderMirror(side_panel, graphicsHolder, light, position + 11.9F);
				}
				break;
			case INTERIOR_TRANSLUCENT:
				if (renderDetails) {
					renderMirror(side_panel_translucent, graphicsHolder, light, position - 11.9F);
					renderMirror(side_panel_translucent, graphicsHolder, light, position + 11.9F);
				}
				break;
			case EXTERIOR:
				renderMirror(door_exterior, graphicsHolder, light, position);

				(isOdd ? door_sliding_exterior_1_part : door_sliding_exterior_2_part).setOffset(0, 0, (isOdd ? -1 : 1) * doorRightZ);
				renderOnce(isOdd ? door_sliding_exterior_1 : door_sliding_exterior_2, graphicsHolder, light, position);
				(isOdd ? door_sliding_exterior_2_part : door_sliding_exterior_1_part).setOffset(0, 0, (isOdd ? 1 : -1) * doorLeftZ);
				renderOnceFlipped(isOdd ? door_sliding_exterior_2 : door_sliding_exterior_1, graphicsHolder, light, position);

				break;
		}
	}

	@Override
	protected void renderHeadPosition1(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean useHeadlights) {
		switch (renderStage) {
			case ALWAYS_ON_LIGHT:
				renderOnceFlipped(useHeadlights ? headlights : tail_lights, graphicsHolder, light, position + 23);
				break;
			case LIGHT:
				renderOnceFlipped(light_head, graphicsHolder, light, position + 23);
				break;
			case INTERIOR:
				renderOnceFlipped(head, graphicsHolder, light, position + 23);
				if (renderDetails) {
					renderOnceFlipped(side_seats_head, graphicsHolder, light, position + 23);
				}
				break;
			case EXTERIOR:
				renderOnceFlipped(head_exterior, graphicsHolder, light, position + 23);
				break;
		}
	}

	@Override
	protected void renderHeadPosition2(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean useHeadlights) {
		switch (renderStage) {
			case ALWAYS_ON_LIGHT:
				renderOnce(useHeadlights ? headlights : tail_lights, graphicsHolder, light, position - 23);
				break;
			case LIGHT:
				renderOnce(light_head, graphicsHolder, light, position - 23);
				break;
			case INTERIOR:
				renderOnce(head, graphicsHolder, light, position - 23);
				if (renderDetails) {
					renderOnce(side_seats_head, graphicsHolder, light, position - 23);
				}
				break;
			case EXTERIOR:
				renderOnce(head_exterior, graphicsHolder, light, position - 23);
				break;
		}
	}

	@Override
	protected void renderEndPosition1(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
		switch (renderStage) {
			case LIGHT:
				renderOnce(light_window, graphicsHolder, light, position);
				renderOnceFlipped(light_end, graphicsHolder, light, position);
				break;
			case INTERIOR:
				renderOnce(window, graphicsHolder, light, position);
				renderOnce(window_2_partial, graphicsHolder, light, position);
				renderOnceFlipped(end, graphicsHolder, light, position);
				if (renderDetails) {
					renderOnce(side_seats, graphicsHolder, light, position);
					renderOnceFlipped(side_seats_end, graphicsHolder, light, position);
				}
				break;
			case EXTERIOR:
				renderOnce(window_exterior, graphicsHolder, light, position);
				renderOnce(window_exterior_2_partial, graphicsHolder, light, position);
				renderOnceFlipped(end_exterior, graphicsHolder, light, position);
				break;
		}
	}

	@Override
	protected void renderEndPosition2(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
		switch (renderStage) {
			case LIGHT:
				renderOnceFlipped(light_window, graphicsHolder, light, position);
				renderOnce(light_end, graphicsHolder, light, position);
				break;
			case INTERIOR:
				renderOnceFlipped(window, graphicsHolder, light, position);
				renderOnceFlipped(window_2_partial, graphicsHolder, light, position);
				renderOnce(end, graphicsHolder, light, position);
				if (renderDetails) {
					renderOnceFlipped(side_seats, graphicsHolder, light, position);
					renderOnce(side_seats_end, graphicsHolder, light, position);
				}
				break;
			case EXTERIOR:
				renderOnceFlipped(window_exterior, graphicsHolder, light, position);
				renderOnceFlipped(window_exterior_2_partial, graphicsHolder, light, position);
				renderOnce(end_exterior, graphicsHolder, light, position);
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
	protected int getDoorMax() {
		return DOOR_MAX;
	}

	@Override
	protected void renderTextDisplays(GraphicsHolder graphicsHolder, int thisRouteColor, String thisRouteName, String thisRouteNumber, String thisStationName, String thisRouteDestination, String nextStationName, Consumer<BiConsumer<String, InterchangeColorsForStationName>> getInterchanges, int car, int totalCars, boolean atPlatform, boolean isTerminating, ObjectArrayList<ScrollingText> scrollingTexts) {
		if (scrollingTexts.isEmpty()) {
			scrollingTexts.add(new ScrollingText(0.72F, 0.08F, 8, true));
		}

		final String destinationString = getDestinationString(thisRouteDestination, TextSpacingType.NORMAL, false);
		renderFrontDestination(
				graphicsHolder,
				0, -2.18F, getEndPositions()[0] / 16F - 1.25F, 0, 0, -0.01F,
				0, 0, 0.8F, 0.14F,
				0xFFFF9900, 0xFFFF9900, 1, getAlternatingString(destinationString), false, car, totalCars
		);

		final boolean isEnd1Head = car == 0;
		final boolean isEnd2Head = car == totalCars - 1;
		final String nextStationString = getLondonNextStationString(thisRouteName, thisStationName, nextStationName, getInterchanges, destinationString, atPlatform, isTerminating);
		scrollingTexts.get(0).changeImage(nextStationString.isEmpty() ? null : DynamicTextureCache.instance.getPixelatedText(nextStationString, 0xFFFF9900, Integer.MAX_VALUE, 0, true));
		scrollingTexts.get(0).createVertexConsumer(graphicsHolder);

		for (int i = 0; i < 2; i++) {
			graphicsHolder.push();
			if (i == 1) {
				graphicsHolder.rotateYDegrees(180);
			}
			graphicsHolder.translate(-0.36F, -2.32F, (getEndPositions()[1] + 11 - (i == 1 && isEnd1Head || i == 0 && isEnd2Head ? 16 : 0)) / 16F - 0.01);
			scrollingTexts.get(0).scrollText(graphicsHolder);
			graphicsHolder.pop();
		}
	}

	@Override
	protected String defaultDestinationString() {
		return "Not in Service";
	}
}