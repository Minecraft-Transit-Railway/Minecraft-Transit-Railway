package org.mtr.mod.model;

import org.mtr.core.data.InterchangeColorsForStationName;
import org.mtr.core.data.RenderStage;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.ModelPartExtension;
import org.mtr.mod.client.DoorAnimationType;
import org.mtr.mod.client.DynamicTextureCache;
import org.mtr.mod.client.ScrollingText;
import org.mtr.mod.render.StoredMatrixTransformations;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ModelLondonUnderground1995 extends ModelSimpleTrainBase<ModelLondonUnderground1995> {

	private final ModelPartExtension window;
	private final ModelPartExtension window_1;
	private final ModelPartExtension roof_2_r1;
	private final ModelPartExtension roof_1_r1;
	private final ModelPartExtension window_side_pole_r1;
	private final ModelPartExtension window_top_r1;
	private final ModelPartExtension window_2;
	private final ModelPartExtension roof_3_r1;
	private final ModelPartExtension roof_2_r2;
	private final ModelPartExtension window_side_pole_r2;
	private final ModelPartExtension window_top_r2;
	private final ModelPartExtension window_exterior;
	private final ModelPartExtension window_exterior_1;
	private final ModelPartExtension roof_2_r3;
	private final ModelPartExtension roof_1_r2;
	private final ModelPartExtension window_top_r3;
	private final ModelPartExtension window_middle_r1;
	private final ModelPartExtension window_exterior_2;
	private final ModelPartExtension roof_3_r2;
	private final ModelPartExtension roof_2_r4;
	private final ModelPartExtension window_top_r4;
	private final ModelPartExtension window_middle_r2;
	private final ModelPartExtension door_left;
	private final ModelPartExtension door_left_part;
	private final ModelPartExtension door_left_top_r1;
	private final ModelPartExtension door_left_middle_r1;
	private final ModelPartExtension door_right;
	private final ModelPartExtension door_right_part;
	private final ModelPartExtension door_right_top_r1;
	private final ModelPartExtension door_right_middle_r1;
	private final ModelPartExtension door_left_exterior;
	private final ModelPartExtension door_left_exterior_part;
	private final ModelPartExtension door_left_top_r2;
	private final ModelPartExtension door_left_middle_r2;
	private final ModelPartExtension door_right_exterior;
	private final ModelPartExtension door_right_exterior_part;
	private final ModelPartExtension door_right_top_r2;
	private final ModelPartExtension door_right_middle_r2;
	private final ModelPartExtension seat;
	private final ModelPartExtension seat_1;
	private final ModelPartExtension handrail_3_r1;
	private final ModelPartExtension handrail_2_r1;
	private final ModelPartExtension seat_back_r1;
	private final ModelPartExtension seat_2;
	private final ModelPartExtension handrail_4_r1;
	private final ModelPartExtension handrail_3_r2;
	private final ModelPartExtension seat_back_r2;
	private final ModelPartExtension seat_wheelchair;
	private final ModelPartExtension seat_wheelchair_1;
	private final ModelPartExtension handrail_4_r2;
	private final ModelPartExtension handrail_2_r2;
	private final ModelPartExtension seat_back_r3;
	private final ModelPartExtension seat_wheelchair_2;
	private final ModelPartExtension handrail_5_r1;
	private final ModelPartExtension handrail_3_r3;
	private final ModelPartExtension seat_back_r4;
	private final ModelPartExtension window_light;
	private final ModelPartExtension light_2_r1;
	private final ModelPartExtension light_1_r1;
	private final ModelPartExtension window_light_end;
	private final ModelPartExtension light_3_r1;
	private final ModelPartExtension light_2_r2;
	private final ModelPartExtension window_light_head;
	private final ModelPartExtension light_2_r3;
	private final ModelPartExtension light_1_r2;
	private final ModelPartExtension side_panel;
	private final ModelPartExtension side_panel_r1;
	private final ModelPartExtension end;
	private final ModelPartExtension end_1;
	private final ModelPartExtension roof_2_r5;
	private final ModelPartExtension roof_1_r3;
	private final ModelPartExtension window_top_r5;
	private final ModelPartExtension window_middle_r3;
	private final ModelPartExtension front_box_top_r1;
	private final ModelPartExtension front_box_r1;
	private final ModelPartExtension front_side_r1;
	private final ModelPartExtension end_2;
	private final ModelPartExtension roof_3_r3;
	private final ModelPartExtension roof_2_r6;
	private final ModelPartExtension window_top_r6;
	private final ModelPartExtension window_middle_r4;
	private final ModelPartExtension front_box_top_r2;
	private final ModelPartExtension front_box_r2;
	private final ModelPartExtension front_side_r2;
	private final ModelPartExtension end_exterior;
	private final ModelPartExtension end_exterior_1;
	private final ModelPartExtension front_side_bottom_r1;
	private final ModelPartExtension front_side_6_r1;
	private final ModelPartExtension front_side_5_r1;
	private final ModelPartExtension front_side_4_r1;
	private final ModelPartExtension front_side_3_r1;
	private final ModelPartExtension front_side_2_r1;
	private final ModelPartExtension end_exterior_2;
	private final ModelPartExtension front_side_bottom_r2;
	private final ModelPartExtension front_side_7_r1;
	private final ModelPartExtension front_side_6_r2;
	private final ModelPartExtension front_side_5_r2;
	private final ModelPartExtension front_side_4_r2;
	private final ModelPartExtension front_side_3_r2;
	private final ModelPartExtension head;
	private final ModelPartExtension head_1;
	private final ModelPartExtension roof_2_r7;
	private final ModelPartExtension roof_1_r4;
	private final ModelPartExtension window_top_r7;
	private final ModelPartExtension window_middle_r5;
	private final ModelPartExtension head_2;
	private final ModelPartExtension roof_3_r4;
	private final ModelPartExtension roof_2_r8;
	private final ModelPartExtension window_top_r8;
	private final ModelPartExtension window_middle_r6;
	private final ModelPartExtension head_exterior;
	private final ModelPartExtension small_light_r1;
	private final ModelPartExtension front_side_right_r1;
	private final ModelPartExtension front_side_left_r1;
	private final ModelPartExtension head_exterior_1;
	private final ModelPartExtension front_side_bottom_r3;
	private final ModelPartExtension front_side_5_r3;
	private final ModelPartExtension front_side_4_r3;
	private final ModelPartExtension front_side_3_r3;
	private final ModelPartExtension front_side_1_r1;
	private final ModelPartExtension door_right_top_r3;
	private final ModelPartExtension door_right_middle_r3;
	private final ModelPartExtension roof_1_r5;
	private final ModelPartExtension window_top_r9;
	private final ModelPartExtension window_middle_r7;
	private final ModelPartExtension head_exterior_2;
	private final ModelPartExtension front_side_bottom_r4;
	private final ModelPartExtension front_side_6_r3;
	private final ModelPartExtension front_side_5_r4;
	private final ModelPartExtension front_side_4_r4;
	private final ModelPartExtension front_side_2_r2;
	private final ModelPartExtension door_right_top_r4;
	private final ModelPartExtension door_right_middle_r4;
	private final ModelPartExtension roof_2_r9;
	private final ModelPartExtension window_top_r10;
	private final ModelPartExtension window_middle_r8;
	private final ModelPartExtension logo;
	private final ModelPartExtension door_light_on;
	private final ModelPartExtension light_r1;
	private final ModelPartExtension door_light_off;
	private final ModelPartExtension light_r2;
	private final ModelPartExtension headlights;
	private final ModelPartExtension light_2_r4;
	private final ModelPartExtension light_1_r3;
	private final ModelPartExtension tail_lights;
	private final ModelPartExtension light_3_r2;
	private final ModelPartExtension light_2_r5;

	private final boolean is1995;

	public ModelLondonUnderground1995(boolean is1995) {
		this(is1995, DoorAnimationType.STANDARD, true);
	}

	private ModelLondonUnderground1995(boolean is1995, DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		super(288, 288, doorAnimationType, renderDoorOverlay);
		this.is1995 = is1995;
		final int textureWidth = 288;
		final int textureHeight = 288;

		window = createModelPart();
		window.setPivot(0, 24, 0);


		window_1 = createModelPart();
		window_1.setPivot(0, 0, 0);
		window.addChild(window_1);
		window_1.setTextureUVOffset(88, 0).addCuboid(-20, 0, 0, 20, 1, 32, 0, false);
		window_1.setTextureUVOffset(64, 128).addCuboid(-20, -6, 0, 3, 6, 36, 0, false);
		window_1.setTextureUVOffset(30, 0).addCuboid(-3, -35, 0, 3, 0, 48, 0, false);
		window_1.setTextureUVOffset(0, 112).addCuboid(-14.369F, -32.0747F, 36, 2, 1, 12, 0, false);

		roof_2_r1 = createModelPart();
		roof_2_r1.setPivot(-3, -35, 0);
		window_1.addChild(roof_2_r1);
		setRotationAngle(roof_2_r1, 0, 0, -0.5236F);
		roof_2_r1.setTextureUVOffset(36, 0).addCuboid(-2, 0, 0, 2, 0, 48, 0, false);

		roof_1_r1 = createModelPart();
		roof_1_r1.setPivot(-9.771F, -32.5747F, 24);
		window_1.addChild(roof_1_r1);
		setRotationAngle(roof_1_r1, 0, 0, 1.0472F);
		roof_1_r1.setTextureUVOffset(0, 2).addCuboid(0, -3, -24, 0, 6, 48, 0, false);

		window_side_pole_r1 = createModelPart();
		window_side_pole_r1.setPivot(-20, -12, 0);
		window_1.addChild(window_side_pole_r1);
		setRotationAngle(window_side_pole_r1, 0, 0, 0.1571F);
		window_side_pole_r1.setTextureUVOffset(216, 237).addCuboid(0, -20, 26, 4, 27, 10, 0, false);
		window_side_pole_r1.setTextureUVOffset(108, 176).addCuboid(0, -16, 0, 1, 16, 26, 0, false);

		window_top_r1 = createModelPart();
		window_top_r1.setPivot(-14.933F, -29.4388F, 0);
		window_1.addChild(window_top_r1);
		setRotationAngle(window_top_r1, 0, 0, 0.8378F);
		window_top_r1.setTextureUVOffset(0, 147).addCuboid(-0.5F, -3, 0, 1, 6, 36, 0, false);

		window_2 = createModelPart();
		window_2.setPivot(0, 0, 0);
		window.addChild(window_2);
		window_2.setTextureUVOffset(88, 0).addCuboid(0, 0, 0, 20, 1, 32, 0, true);
		window_2.setTextureUVOffset(64, 128).addCuboid(17, -6, 0, 3, 6, 36, 0, true);
		window_2.setTextureUVOffset(30, 0).addCuboid(0, -35, 0, 3, 0, 48, 0, true);
		window_2.setTextureUVOffset(0, 112).addCuboid(12.369F, -32.0747F, 36, 2, 1, 12, 0, true);

		roof_3_r1 = createModelPart();
		roof_3_r1.setPivot(3, -35, 0);
		window_2.addChild(roof_3_r1);
		setRotationAngle(roof_3_r1, 0, 0, 0.5236F);
		roof_3_r1.setTextureUVOffset(36, 0).addCuboid(0, 0, 0, 2, 0, 48, 0, true);

		roof_2_r2 = createModelPart();
		roof_2_r2.setPivot(9.771F, -32.5747F, 24);
		window_2.addChild(roof_2_r2);
		setRotationAngle(roof_2_r2, 0, 0, -1.0472F);
		roof_2_r2.setTextureUVOffset(0, 2).addCuboid(0, -3, -24, 0, 6, 48, 0, true);

		window_side_pole_r2 = createModelPart();
		window_side_pole_r2.setPivot(20, -12, 0);
		window_2.addChild(window_side_pole_r2);
		setRotationAngle(window_side_pole_r2, 0, 0, -0.1571F);
		window_side_pole_r2.setTextureUVOffset(216, 237).addCuboid(-4, -20, 26, 4, 27, 10, 0, true);
		window_side_pole_r2.setTextureUVOffset(108, 176).addCuboid(-1, -16, 0, 1, 16, 26, 0, true);

		window_top_r2 = createModelPart();
		window_top_r2.setPivot(14.933F, -29.4388F, 0);
		window_2.addChild(window_top_r2);
		setRotationAngle(window_top_r2, 0, 0, -0.8378F);
		window_top_r2.setTextureUVOffset(0, 147).addCuboid(-0.5F, -3, 0, 1, 6, 36, 0, true);

		window_exterior = createModelPart();
		window_exterior.setPivot(0, 24, 0);


		window_exterior_1 = createModelPart();
		window_exterior_1.setPivot(0, 0, 0);
		window_exterior.addChild(window_exterior_1);
		window_exterior_1.setTextureUVOffset(42, 176).addCuboid(-21, 0, 0, 1, 4, 32, 0, false);
		window_exterior_1.setTextureUVOffset(100, 41).addCuboid(-20, -12, 0, 0, 12, 36, 0, false);
		window_exterior_1.setTextureUVOffset(22, 0).addCuboid(-4, -36, 0, 4, 0, 48, 0, false);

		roof_2_r3 = createModelPart();
		roof_2_r3.setPivot(-4, -36, 0);
		window_exterior_1.addChild(roof_2_r3);
		setRotationAngle(roof_2_r3, 0, 0, -0.1745F);
		roof_2_r3.setTextureUVOffset(12, 0).addCuboid(-5, 0, 0, 5, 0, 48, 0, false);

		roof_1_r2 = createModelPart();
		roof_1_r2.setPivot(-11.0223F, -32.7667F, 18);
		window_exterior_1.addChild(roof_1_r2);
		setRotationAngle(roof_1_r2, 0, 0, -0.5236F);
		roof_1_r2.setTextureUVOffset(0, 0).addCuboid(-3, -1, -18, 6, 2, 48, 0, false);

		window_top_r3 = createModelPart();
		window_top_r3.setPivot(-14.933F, -29.4388F, 0);
		window_exterior_1.addChild(window_top_r3);
		setRotationAngle(window_top_r3, 0, 0, 0.8378F);
		window_top_r3.setTextureUVOffset(110, 134).addCuboid(-0.5F, -3, 0, 0, 6, 36, 0, false);

		window_middle_r1 = createModelPart();
		window_middle_r1.setPivot(-20, -12, 0);
		window_exterior_1.addChild(window_middle_r1);
		setRotationAngle(window_middle_r1, 0, 0, 0.1571F);
		window_middle_r1.setTextureUVOffset(0, 95).addCuboid(0, -16, 0, 0, 16, 36, 0, false);

		window_exterior_2 = createModelPart();
		window_exterior_2.setPivot(0, 0, 0);
		window_exterior.addChild(window_exterior_2);
		window_exterior_2.setTextureUVOffset(42, 176).addCuboid(20, 0, 0, 1, 4, 32, 0, true);
		window_exterior_2.setTextureUVOffset(100, 41).addCuboid(20, -12, 0, 0, 12, 36, 0, true);
		window_exterior_2.setTextureUVOffset(22, 0).addCuboid(0, -36, 0, 4, 0, 48, 0, true);

		roof_3_r2 = createModelPart();
		roof_3_r2.setPivot(4, -36, 0);
		window_exterior_2.addChild(roof_3_r2);
		setRotationAngle(roof_3_r2, 0, 0, 0.1745F);
		roof_3_r2.setTextureUVOffset(12, 0).addCuboid(0, 0, 0, 5, 0, 48, 0, true);

		roof_2_r4 = createModelPart();
		roof_2_r4.setPivot(11.0223F, -32.7667F, 18);
		window_exterior_2.addChild(roof_2_r4);
		setRotationAngle(roof_2_r4, 0, 0, 0.5236F);
		roof_2_r4.setTextureUVOffset(0, 0).addCuboid(-3, -1, -18, 6, 2, 48, 0, true);

		window_top_r4 = createModelPart();
		window_top_r4.setPivot(14.933F, -29.4388F, 0);
		window_exterior_2.addChild(window_top_r4);
		setRotationAngle(window_top_r4, 0, 0, -0.8378F);
		window_top_r4.setTextureUVOffset(110, 134).addCuboid(0.5F, -3, 0, 0, 6, 36, 0, true);

		window_middle_r2 = createModelPart();
		window_middle_r2.setPivot(20, -12, 0);
		window_exterior_2.addChild(window_middle_r2);
		setRotationAngle(window_middle_r2, 0, 0, -0.1571F);
		window_middle_r2.setTextureUVOffset(0, 95).addCuboid(0, -16, 0, 0, 16, 36, 0, true);

		door_left = createModelPart();
		door_left.setPivot(0, 24, 0);
		door_left.setTextureUVOffset(0, 189).addCuboid(-20, 0, 0, 20, 1, 16, 0, false);

		door_left_part = createModelPart();
		door_left_part.setPivot(0, 0, 0);
		door_left.addChild(door_left_part);
		door_left_part.setTextureUVOffset(70, 196).addCuboid(-19.8F, -12, 0, 0, 12, 22, 0, false);

		door_left_top_r1 = createModelPart();
		door_left_top_r1.setPivot(-15.6548F, -29.9327F, 11);
		door_left_part.addChild(door_left_top_r1);
		setRotationAngle(door_left_top_r1, 0, 0, 0.8378F);
		door_left_top_r1.setTextureUVOffset(100, 67).addCuboid(0.5F, -3, -11, 0, 6, 22, 0, false);

		door_left_middle_r1 = createModelPart();
		door_left_middle_r1.setPivot(-20.8F, -12, 0);
		door_left_part.addChild(door_left_middle_r1);
		setRotationAngle(door_left_middle_r1, 0, 0, 0.1571F);
		door_left_middle_r1.setTextureUVOffset(136, 162).addCuboid(1, -17, 0, 0, 17, 22, 0, false);

		door_right = createModelPart();
		door_right.setPivot(0, 24, 0);
		door_right.setTextureUVOffset(160, 0).addCuboid(-20, 0, -16, 20, 1, 16, 0, false);

		door_right_part = createModelPart();
		door_right_part.setPivot(0, 0, 0);
		door_right.addChild(door_right_part);
		door_right_part.setTextureUVOffset(24, 190).addCuboid(-19.8F, -12, -22, 0, 12, 22, 0, false);

		door_right_top_r1 = createModelPart();
		door_right_top_r1.setPivot(-15.6548F, -29.9327F, -11);
		door_right_part.addChild(door_right_top_r1);
		setRotationAngle(door_right_top_r1, 0, 0, 0.8378F);
		door_right_top_r1.setTextureUVOffset(88, 17).addCuboid(0.5F, -3, -11, 0, 6, 22, 0, false);

		door_right_middle_r1 = createModelPart();
		door_right_middle_r1.setPivot(-20.8F, -12, 0);
		door_right_part.addChild(door_right_middle_r1);
		setRotationAngle(door_right_middle_r1, 0, 0, 0.1571F);
		door_right_middle_r1.setTextureUVOffset(76, 154).addCuboid(1, -17, -22, 0, 17, 22, 0, false);

		door_left_exterior = createModelPart();
		door_left_exterior.setPivot(0, 24, 0);
		door_left_exterior.setTextureUVOffset(238, 86).addCuboid(-21, 0, 0, 1, 4, 16, 0, false);

		door_left_exterior_part = createModelPart();
		door_left_exterior_part.setPivot(0, 0, 0);
		door_left_exterior.addChild(door_left_exterior_part);
		door_left_exterior_part.setTextureUVOffset(220, 169).addCuboid(-20.8F, -12, 0, 1, 12, 22, 0, false);

		door_left_top_r2 = createModelPart();
		door_left_top_r2.setPivot(-15.6548F, -29.9327F, 11);
		door_left_exterior_part.addChild(door_left_top_r2);
		setRotationAngle(door_left_top_r2, 0, 0, 0.8378F);
		door_left_top_r2.setTextureUVOffset(223, 209).addCuboid(-0.5F, -3, -11, 1, 6, 22, 0, false);

		door_left_middle_r2 = createModelPart();
		door_left_middle_r2.setPivot(-20.8F, -12, 0);
		door_left_exterior_part.addChild(door_left_middle_r2);
		setRotationAngle(door_left_middle_r2, 0, 0, 0.1571F);
		door_left_middle_r2.setTextureUVOffset(46, 212).addCuboid(0, -17, 0, 1, 17, 22, 0, false);

		door_right_exterior = createModelPart();
		door_right_exterior.setPivot(0, 24, 0);
		door_right_exterior.setTextureUVOffset(236, 0).addCuboid(-21, 0, -16, 1, 4, 16, 0, false);

		door_right_exterior_part = createModelPart();
		door_right_exterior_part.setPivot(0, 0, 0);
		door_right_exterior.addChild(door_right_exterior_part);
		door_right_exterior_part.setTextureUVOffset(138, 218).addCuboid(-20.8F, -12, -22, 1, 12, 22, 0, false);

		door_right_top_r2 = createModelPart();
		door_right_top_r2.setPivot(-15.6548F, -29.9327F, -11);
		door_right_exterior_part.addChild(door_right_top_r2);
		setRotationAngle(door_right_top_r2, 0, 0, 0.8378F);
		door_right_top_r2.setTextureUVOffset(222, 58).addCuboid(-0.5F, -3, -11, 1, 6, 22, 0, false);

		door_right_middle_r2 = createModelPart();
		door_right_middle_r2.setPivot(-20.8F, -12, 0);
		door_right_exterior_part.addChild(door_right_middle_r2);
		setRotationAngle(door_right_middle_r2, 0, 0, 0.1571F);
		door_right_middle_r2.setTextureUVOffset(0, 206).addCuboid(0, -17, -22, 1, 17, 22, 0, false);

		seat = createModelPart();
		seat.setPivot(0, 24, 0);


		seat_1 = createModelPart();
		seat_1.setPivot(0, 0, 0);
		seat.addChild(seat_1);
		seat_1.setTextureUVOffset(110, 100).addCuboid(-17, -6, 0, 10, 6, 32, 0, false);

		handrail_3_r1 = createModelPart();
		handrail_3_r1.setPivot(-7, -4, 0);
		seat_1.addChild(handrail_3_r1);
		setRotationAngle(handrail_3_r1, -1.5708F, 0, -0.0349F);
		handrail_3_r1.setTextureUVOffset(0, 0).addCuboid(0, -31.5F, -28, 0, 32, 0, 0.2F, false);

		handrail_2_r1 = createModelPart();
		handrail_2_r1.setPivot(-7, -4, 0);
		seat_1.addChild(handrail_2_r1);
		setRotationAngle(handrail_2_r1, 0, 0, -0.0349F);
		handrail_2_r1.setTextureUVOffset(0, 0).addCuboid(0, -30, 31.5F, 0, 30, 0, 0.2F, false);
		handrail_2_r1.setTextureUVOffset(0, 0).addCuboid(0, -30, 13.5F, 0, 30, 0, 0.2F, false);

		seat_back_r1 = createModelPart();
		seat_back_r1.setPivot(-14, -6, 0);
		seat_1.addChild(seat_back_r1);
		setRotationAngle(seat_back_r1, 0, 0, -0.1745F);
		seat_back_r1.setTextureUVOffset(164, 144).addCuboid(-4, -8, 0, 4, 8, 32, 0, false);

		seat_2 = createModelPart();
		seat_2.setPivot(0, 0, 0);
		seat.addChild(seat_2);
		seat_2.setTextureUVOffset(110, 100).addCuboid(7, -6, 0, 10, 6, 32, 0, true);

		handrail_4_r1 = createModelPart();
		handrail_4_r1.setPivot(7, -4, 0);
		seat_2.addChild(handrail_4_r1);
		setRotationAngle(handrail_4_r1, -1.5708F, 0, 0.0349F);
		handrail_4_r1.setTextureUVOffset(0, 0).addCuboid(0, -31.5F, -28, 0, 32, 0, 0.2F, true);

		handrail_3_r2 = createModelPart();
		handrail_3_r2.setPivot(7, -4, 0);
		seat_2.addChild(handrail_3_r2);
		setRotationAngle(handrail_3_r2, 0, 0, 0.0349F);
		handrail_3_r2.setTextureUVOffset(0, 0).addCuboid(0, -30, 31.5F, 0, 30, 0, 0.2F, true);
		handrail_3_r2.setTextureUVOffset(0, 0).addCuboid(0, -30, 13.5F, 0, 30, 0, 0.2F, true);

		seat_back_r2 = createModelPart();
		seat_back_r2.setPivot(14, -6, 0);
		seat_2.addChild(seat_back_r2);
		setRotationAngle(seat_back_r2, 0, 0, 0.1745F);
		seat_back_r2.setTextureUVOffset(164, 144).addCuboid(0, -8, 0, 4, 8, 32, 0, true);

		seat_wheelchair = createModelPart();
		seat_wheelchair.setPivot(0, 24, 0);


		seat_wheelchair_1 = createModelPart();
		seat_wheelchair_1.setPivot(-7, -4, 0);
		seat_wheelchair.addChild(seat_wheelchair_1);
		seat_wheelchair_1.setTextureUVOffset(228, 116).addCuboid(-10, -2, 0, 10, 6, 14, 0, false);
		seat_wheelchair_1.setTextureUVOffset(222, 27).addCuboid(-12, -9, 14, 5, 13, 18, 0, false);

		handrail_4_r2 = createModelPart();
		handrail_4_r2.setPivot(0, 0, 0);
		seat_wheelchair_1.addChild(handrail_4_r2);
		setRotationAngle(handrail_4_r2, -1.5708F, 0, -0.0349F);
		handrail_4_r2.setTextureUVOffset(0, 0).addCuboid(0, -13.5F, -28, 0, 14, 0, 0.2F, false);

		handrail_2_r2 = createModelPart();
		handrail_2_r2.setPivot(0, 0, 0);
		seat_wheelchair_1.addChild(handrail_2_r2);
		setRotationAngle(handrail_2_r2, 0, 0, -0.0349F);
		handrail_2_r2.setTextureUVOffset(0, 0).addCuboid(0, -30, 13.5F, 0, 30, 0, 0.2F, false);

		seat_back_r3 = createModelPart();
		seat_back_r3.setPivot(-7, -2, 0);
		seat_wheelchair_1.addChild(seat_back_r3);
		setRotationAngle(seat_back_r3, 0, 0, -0.1745F);
		seat_back_r3.setTextureUVOffset(116, 218).addCuboid(-4, -8, 0, 4, 8, 14, 0, false);

		seat_wheelchair_2 = createModelPart();
		seat_wheelchair_2.setPivot(7, -4, 0);
		seat_wheelchair.addChild(seat_wheelchair_2);
		seat_wheelchair_2.setTextureUVOffset(228, 116).addCuboid(0, -2, 0, 10, 6, 14, 0, true);
		seat_wheelchair_2.setTextureUVOffset(222, 27).addCuboid(7, -9, 14, 5, 13, 18, 0, true);

		handrail_5_r1 = createModelPart();
		handrail_5_r1.setPivot(0, 0, 0);
		seat_wheelchair_2.addChild(handrail_5_r1);
		setRotationAngle(handrail_5_r1, -1.5708F, 0, 0.0349F);
		handrail_5_r1.setTextureUVOffset(0, 0).addCuboid(0, -13.5F, -28, 0, 14, 0, 0.2F, true);

		handrail_3_r3 = createModelPart();
		handrail_3_r3.setPivot(0, 0, 0);
		seat_wheelchair_2.addChild(handrail_3_r3);
		setRotationAngle(handrail_3_r3, 0, 0, 0.0349F);
		handrail_3_r3.setTextureUVOffset(0, 0).addCuboid(0, -30, 13.5F, 0, 30, 0, 0.2F, true);

		seat_back_r4 = createModelPart();
		seat_back_r4.setPivot(7, -2, 0);
		seat_wheelchair_2.addChild(seat_back_r4);
		setRotationAngle(seat_back_r4, 0, 0, 0.1745F);
		seat_back_r4.setTextureUVOffset(116, 218).addCuboid(0, -8, 0, 4, 8, 14, 0, true);

		window_light = createModelPart();
		window_light.setPivot(0, 24, 0);


		light_2_r1 = createModelPart();
		light_2_r1.setPivot(6.1463F, -34, 0);
		window_light.addChild(light_2_r1);
		setRotationAngle(light_2_r1, 0, 0, 0.7854F);
		light_2_r1.setTextureUVOffset(48, 50).addCuboid(-1, -1, 0, 2, 2, 48, 0, true);

		light_1_r1 = createModelPart();
		light_1_r1.setPivot(-6.1463F, -34, 0);
		window_light.addChild(light_1_r1);
		setRotationAngle(light_1_r1, 0, 0, -0.7854F);
		light_1_r1.setTextureUVOffset(48, 50).addCuboid(-1, -1, 0, 2, 2, 48, 0, false);

		window_light_end = createModelPart();
		window_light_end.setPivot(0, 24, 0);


		light_3_r1 = createModelPart();
		light_3_r1.setPivot(6.1463F, -34, 48);
		window_light_end.addChild(light_3_r1);
		setRotationAngle(light_3_r1, 0, 0, 0.7854F);
		light_3_r1.setTextureUVOffset(88, 90).addCuboid(-1, -1, 0, 2, 2, 8, 0, true);

		light_2_r2 = createModelPart();
		light_2_r2.setPivot(-6.1463F, -34, 48);
		window_light_end.addChild(light_2_r2);
		setRotationAngle(light_2_r2, 0, 0, -0.7854F);
		light_2_r2.setTextureUVOffset(88, 90).addCuboid(-1, -1, 0, 2, 2, 8, 0, false);

		window_light_head = createModelPart();
		window_light_head.setPivot(0, 24, 0);


		light_2_r3 = createModelPart();
		light_2_r3.setPivot(6.1463F, -34, 0);
		window_light_head.addChild(light_2_r3);
		setRotationAngle(light_2_r3, 0, 0, 0.7854F);
		light_2_r3.setTextureUVOffset(71, 73).addCuboid(-1, -1, 0, 2, 2, 25, 0, true);

		light_1_r2 = createModelPart();
		light_1_r2.setPivot(-6.1463F, -34, 0);
		window_light_head.addChild(light_1_r2);
		setRotationAngle(light_1_r2, 0, 0, -0.7854F);
		light_1_r2.setTextureUVOffset(71, 73).addCuboid(-1, -1, 0, 2, 2, 25, 0, false);

		side_panel = createModelPart();
		side_panel.setPivot(0, 24, 0);


		side_panel_r1 = createModelPart();
		side_panel_r1.setPivot(-8, -6, 0);
		side_panel.addChild(side_panel_r1);
		setRotationAngle(side_panel_r1, 0, 0, -0.0349F);
		side_panel_r1.setTextureUVOffset(168, 252).addCuboid(-8, -28, 0, 8, 28, 0, 0, false);

		end = createModelPart();
		end.setPivot(0, 24, 0);
		end.setTextureUVOffset(244, 237).addCuboid(-5, -36, 56, 10, 36, 0, 0, false);

		end_1 = createModelPart();
		end_1.setPivot(0, 0, 0);
		end.addChild(end_1);
		end_1.setTextureUVOffset(122, 154).addCuboid(-20, 0, 48, 20, 1, 9, 0, false);
		end_1.setTextureUVOffset(106, 132).addCuboid(-5.5F, -14, 50, 0, 14, 6, 0, false);
		end_1.setTextureUVOffset(171, 138).addCuboid(-20, -12, 48, 1, 12, 6, 0, false);
		end_1.setTextureUVOffset(8, 17).addCuboid(-3, -35, 48, 3, 0, 8, 0, false);

		roof_2_r5 = createModelPart();
		roof_2_r5.setPivot(-3, -35, 49);
		end_1.addChild(roof_2_r5);
		setRotationAngle(roof_2_r5, 0, 0, -0.5236F);
		roof_2_r5.setTextureUVOffset(27, 0).addCuboid(-2, 0, -1, 2, 0, 8, 0, false);

		roof_1_r3 = createModelPart();
		roof_1_r3.setPivot(-17.8152F, -28.5077F, 49);
		end_1.addChild(roof_1_r3);
		setRotationAngle(roof_1_r3, 0, 0, 1.0472F);
		roof_1_r3.setTextureUVOffset(24, 26).addCuboid(0.5F, -12, -1, 0, 6, 8, 0, false);

		window_top_r5 = createModelPart();
		window_top_r5.setPivot(-14.933F, -29.4388F, 49);
		end_1.addChild(window_top_r5);
		setRotationAngle(window_top_r5, 0, 0, 0.8378F);
		window_top_r5.setTextureUVOffset(38, 156).addCuboid(-0.5F, -3, -1, 1, 6, 7, 0, false);

		window_middle_r3 = createModelPart();
		window_middle_r3.setPivot(-20, -12, 49);
		end_1.addChild(window_middle_r3);
		setRotationAngle(window_middle_r3, 0, 0, 0.1571F);
		window_middle_r3.setTextureUVOffset(118, 45).addCuboid(0, -16, -1, 1, 16, 6, 0, false);

		front_box_top_r1 = createModelPart();
		front_box_top_r1.setPivot(-5.5F, -14, 56);
		end_1.addChild(front_box_top_r1);
		setRotationAngle(front_box_top_r1, 0.1745F, -0.1745F, 0);
		front_box_top_r1.setTextureUVOffset(82, 22).addCuboid(-15, 0, -6, 15, 0, 6, 0, false);

		front_box_r1 = createModelPart();
		front_box_r1.setPivot(-5.5F, 0, 56);
		end_1.addChild(front_box_r1);
		setRotationAngle(front_box_r1, 0, -0.1745F, 0);
		front_box_r1.setTextureUVOffset(0, 98).addCuboid(-15, -14, -5, 15, 14, 0, 0, false);

		front_side_r1 = createModelPart();
		front_side_r1.setPivot(-5, -36, 56);
		end_1.addChild(front_side_r1);
		setRotationAngle(front_side_r1, 0, -0.1745F, 0);
		front_side_r1.setTextureUVOffset(0, 56).addCuboid(-16, 0, 0, 16, 36, 0, 0, false);

		end_2 = createModelPart();
		end_2.setPivot(0, 0, 0);
		end.addChild(end_2);
		end_2.setTextureUVOffset(122, 154).addCuboid(0, 0, 48, 20, 1, 9, 0, true);
		end_2.setTextureUVOffset(106, 132).addCuboid(5.5F, -14, 50, 0, 14, 6, 0, true);
		end_2.setTextureUVOffset(171, 138).addCuboid(19, -12, 48, 1, 12, 6, 0, true);
		end_2.setTextureUVOffset(8, 17).addCuboid(0, -35, 48, 3, 0, 8, 0, true);

		roof_3_r3 = createModelPart();
		roof_3_r3.setPivot(3, -35, 49);
		end_2.addChild(roof_3_r3);
		setRotationAngle(roof_3_r3, 0, 0, 0.5236F);
		roof_3_r3.setTextureUVOffset(27, 0).addCuboid(0, 0, -1, 2, 0, 8, 0, true);

		roof_2_r6 = createModelPart();
		roof_2_r6.setPivot(17.8152F, -28.5077F, 49);
		end_2.addChild(roof_2_r6);
		setRotationAngle(roof_2_r6, 0, 0, -1.0472F);
		roof_2_r6.setTextureUVOffset(24, 26).addCuboid(-0.5F, -12, -1, 0, 6, 8, 0, true);

		window_top_r6 = createModelPart();
		window_top_r6.setPivot(14.933F, -29.4388F, 49);
		end_2.addChild(window_top_r6);
		setRotationAngle(window_top_r6, 0, 0, -0.8378F);
		window_top_r6.setTextureUVOffset(38, 156).addCuboid(-0.5F, -3, -1, 1, 6, 7, 0, true);

		window_middle_r4 = createModelPart();
		window_middle_r4.setPivot(20, -12, 49);
		end_2.addChild(window_middle_r4);
		setRotationAngle(window_middle_r4, 0, 0, -0.1571F);
		window_middle_r4.setTextureUVOffset(118, 45).addCuboid(-1, -16, -1, 1, 16, 6, 0, true);

		front_box_top_r2 = createModelPart();
		front_box_top_r2.setPivot(5.5F, -14, 56);
		end_2.addChild(front_box_top_r2);
		setRotationAngle(front_box_top_r2, 0.1745F, 0.1745F, 0);
		front_box_top_r2.setTextureUVOffset(82, 22).addCuboid(0, 0, -6, 15, 0, 6, 0, true);

		front_box_r2 = createModelPart();
		front_box_r2.setPivot(5.5F, 0, 56);
		end_2.addChild(front_box_r2);
		setRotationAngle(front_box_r2, 0, 0.1745F, 0);
		front_box_r2.setTextureUVOffset(0, 98).addCuboid(0, -14, -5, 15, 14, 0, 0, true);

		front_side_r2 = createModelPart();
		front_side_r2.setPivot(5, -36, 56);
		end_2.addChild(front_side_r2);
		setRotationAngle(front_side_r2, 0, 0.1745F, 0);
		front_side_r2.setTextureUVOffset(0, 56).addCuboid(0, 0, 0, 16, 36, 0, 0, true);

		end_exterior = createModelPart();
		end_exterior.setPivot(0, 24, 0);
		end_exterior.setTextureUVOffset(0, 245).addCuboid(-5, -36, 57, 10, 36, 0, 0, false);
		end_exterior.setTextureUVOffset(38, 147).addCuboid(-5, 0, 52, 10, 4, 5, 0, false);

		end_exterior_1 = createModelPart();
		end_exterior_1.setPivot(0, 0, 0);
		end_exterior.addChild(end_exterior_1);
		end_exterior_1.setTextureUVOffset(100, 51).addCuboid(-21, 0, 32, 1, 4, 16, 0, false);
		end_exterior_1.setTextureUVOffset(23, 56).addCuboid(-4, -36, 48, 4, 0, 9, 0, false);

		front_side_bottom_r1 = createModelPart();
		front_side_bottom_r1.setPivot(-5, -36, 57);
		end_exterior_1.addChild(front_side_bottom_r1);
		setRotationAngle(front_side_bottom_r1, 0, -0.1745F, 0);
		front_side_bottom_r1.setTextureUVOffset(160, 17).addCuboid(-16, 36, -5, 16, 4, 5, 0, false);
		front_side_bottom_r1.setTextureUVOffset(62, 56).addCuboid(-16, 0, 0, 16, 36, 0, 0, false);

		front_side_6_r1 = createModelPart();
		front_side_6_r1.setPivot(-4, -36, 40);
		end_exterior_1.addChild(front_side_6_r1);
		setRotationAngle(front_side_6_r1, 0, 0, -0.1745F);
		front_side_6_r1.setTextureUVOffset(0, 28).addCuboid(-5, 0, 8, 5, 0, 9, 0, false);

		front_side_5_r1 = createModelPart();
		front_side_5_r1.setPivot(-11.0218F, -32.7659F, 58);
		end_exterior_1.addChild(front_side_5_r1);
		setRotationAngle(front_side_5_r1, 0, 0, -0.5236F);
		front_side_5_r1.setTextureUVOffset(228, 136).addCuboid(-4, -1, -10, 7, 1, 9, 0, false);

		front_side_4_r1 = createModelPart();
		front_side_4_r1.setPivot(-15.8157F, -30.1796F, 11);
		end_exterior_1.addChild(front_side_4_r1);
		setRotationAngle(front_side_4_r1, 0, 0, 0.8378F);
		front_side_4_r1.setTextureUVOffset(110, 144).addCuboid(-0.5F, -2, 37, 1, 5, 8, 0, false);

		front_side_3_r1 = createModelPart();
		front_side_3_r1.setPivot(-21, -12, 48);
		end_exterior_1.addChild(front_side_3_r1);
		setRotationAngle(front_side_3_r1, 0, 0.0873F, 0.1571F);
		front_side_3_r1.setTextureUVOffset(247, 203).addCuboid(0, -19, 0, 1, 19, 7, 0, false);

		front_side_2_r1 = createModelPart();
		front_side_2_r1.setPivot(-21, 4, 48);
		end_exterior_1.addChild(front_side_2_r1);
		setRotationAngle(front_side_2_r1, 0, 0.0873F, 0);
		front_side_2_r1.setTextureUVOffset(74, 131).addCuboid(0, -16, 0, 1, 16, 7, 0, false);

		end_exterior_2 = createModelPart();
		end_exterior_2.setPivot(0, 0, 0);
		end_exterior.addChild(end_exterior_2);
		end_exterior_2.setTextureUVOffset(100, 51).addCuboid(20, 0, 32, 1, 4, 16, 0, true);
		end_exterior_2.setTextureUVOffset(23, 56).addCuboid(0, -36, 48, 4, 0, 9, 0, true);

		front_side_bottom_r2 = createModelPart();
		front_side_bottom_r2.setPivot(5, -36, 57);
		end_exterior_2.addChild(front_side_bottom_r2);
		setRotationAngle(front_side_bottom_r2, 0, 0.1745F, 0);
		front_side_bottom_r2.setTextureUVOffset(160, 17).addCuboid(0, 36, -5, 16, 4, 5, 0, true);
		front_side_bottom_r2.setTextureUVOffset(62, 56).addCuboid(0, 0, 0, 16, 36, 0, 0, true);

		front_side_7_r1 = createModelPart();
		front_side_7_r1.setPivot(4, -36, 40);
		end_exterior_2.addChild(front_side_7_r1);
		setRotationAngle(front_side_7_r1, 0, 0, 0.1745F);
		front_side_7_r1.setTextureUVOffset(0, 28).addCuboid(0, 0, 8, 5, 0, 9, 0, true);

		front_side_6_r2 = createModelPart();
		front_side_6_r2.setPivot(11.0218F, -32.7659F, 58);
		end_exterior_2.addChild(front_side_6_r2);
		setRotationAngle(front_side_6_r2, 0, 0, 0.5236F);
		front_side_6_r2.setTextureUVOffset(228, 136).addCuboid(-3, -1, -10, 7, 1, 9, 0, true);

		front_side_5_r2 = createModelPart();
		front_side_5_r2.setPivot(15.8157F, -30.1796F, 11);
		end_exterior_2.addChild(front_side_5_r2);
		setRotationAngle(front_side_5_r2, 0, 0, -0.8378F);
		front_side_5_r2.setTextureUVOffset(110, 144).addCuboid(-0.5F, -2, 37, 1, 5, 8, 0, true);

		front_side_4_r2 = createModelPart();
		front_side_4_r2.setPivot(21, -12, 48);
		end_exterior_2.addChild(front_side_4_r2);
		setRotationAngle(front_side_4_r2, 0, -0.0873F, -0.1571F);
		front_side_4_r2.setTextureUVOffset(247, 203).addCuboid(-1, -19, 0, 1, 19, 7, 0, true);

		front_side_3_r2 = createModelPart();
		front_side_3_r2.setPivot(21, 4, 48);
		end_exterior_2.addChild(front_side_3_r2);
		setRotationAngle(front_side_3_r2, 0, -0.0873F, 0);
		front_side_3_r2.setTextureUVOffset(74, 131).addCuboid(-1, -16, 0, 1, 16, 7, 0, true);

		head = createModelPart();
		head.setPivot(0, 24, 0);
		head.setTextureUVOffset(162, 95).addCuboid(-19, -35, 25, 38, 35, 0, 0, false);

		head_1 = createModelPart();
		head_1.setPivot(0, 0, 0);
		head.addChild(head_1);
		head_1.setTextureUVOffset(157, 69).addCuboid(-20, 0, 0, 20, 1, 25, 0, false);
		head_1.setTextureUVOffset(189, 200).addCuboid(-20, -6, 0, 3, 6, 25, 0, false);
		head_1.setTextureUVOffset(0, 0).addCuboid(-3, -35, 0, 3, 0, 25, 0, false);

		roof_2_r7 = createModelPart();
		roof_2_r7.setPivot(-3, -35, 0);
		head_1.addChild(roof_2_r7);
		setRotationAngle(roof_2_r7, 0, 0, -0.5236F);
		roof_2_r7.setTextureUVOffset(6, 0).addCuboid(-2, 0, 0, 2, 0, 25, 0, false);

		roof_1_r4 = createModelPart();
		roof_1_r4.setPivot(-17.8152F, -28.5077F, 0);
		head_1.addChild(roof_1_r4);
		setRotationAngle(roof_1_r4, 0, 0, 1.0472F);
		roof_1_r4.setTextureUVOffset(88, 8).addCuboid(0.5F, -12, 0, 0, 6, 25, 0, false);

		window_top_r7 = createModelPart();
		window_top_r7.setPivot(-14.933F, -29.4388F, 0);
		head_1.addChild(window_top_r7);
		setRotationAngle(window_top_r7, 0, 0, 0.8378F);
		window_top_r7.setTextureUVOffset(47, 131).addCuboid(-0.5F, -3, 0, 1, 6, 25, 0, false);

		window_middle_r5 = createModelPart();
		window_middle_r5.setPivot(-20, -12, 0);
		head_1.addChild(window_middle_r5);
		setRotationAngle(window_middle_r5, 0, 0, 0.1571F);
		window_middle_r5.setTextureUVOffset(162, 184).addCuboid(0, -16, 0, 1, 16, 25, 0, false);

		head_2 = createModelPart();
		head_2.setPivot(0, 0, 0);
		head.addChild(head_2);
		head_2.setTextureUVOffset(157, 69).addCuboid(0, 0, 0, 20, 1, 25, 0, true);
		head_2.setTextureUVOffset(189, 200).addCuboid(17, -6, 0, 3, 6, 25, 0, true);
		head_2.setTextureUVOffset(0, 0).addCuboid(0, -35, 0, 3, 0, 25, 0, true);

		roof_3_r4 = createModelPart();
		roof_3_r4.setPivot(3, -35, 0);
		head_2.addChild(roof_3_r4);
		setRotationAngle(roof_3_r4, 0, 0, 0.5236F);
		roof_3_r4.setTextureUVOffset(6, 0).addCuboid(0, 0, 0, 2, 0, 25, 0, true);

		roof_2_r8 = createModelPart();
		roof_2_r8.setPivot(17.8152F, -28.5077F, 0);
		head_2.addChild(roof_2_r8);
		setRotationAngle(roof_2_r8, 0, 0, -1.0472F);
		roof_2_r8.setTextureUVOffset(88, 8).addCuboid(-0.5F, -12, 0, 0, 6, 25, 0, true);

		window_top_r8 = createModelPart();
		window_top_r8.setPivot(14.933F, -29.4388F, 0);
		head_2.addChild(window_top_r8);
		setRotationAngle(window_top_r8, 0, 0, -0.8378F);
		window_top_r8.setTextureUVOffset(47, 131).addCuboid(-0.5F, -3, 0, 1, 6, 25, 0, true);

		window_middle_r6 = createModelPart();
		window_middle_r6.setPivot(20, -12, 0);
		head_2.addChild(window_middle_r6);
		setRotationAngle(window_middle_r6, 0, 0, -0.1571F);
		window_middle_r6.setTextureUVOffset(162, 184).addCuboid(-1, -16, 0, 1, 16, 25, 0, true);

		head_exterior = createModelPart();
		head_exterior.setPivot(0, 24, 0);
		head_exterior.setTextureUVOffset(142, 33).addCuboid(-20, -36, 26, 40, 36, 0, 0, false);
		head_exterior.setTextureUVOffset(20, 245).addCuboid(-5, -36, 57, 10, 36, 0, 0, false);
		head_exterior.setTextureUVOffset(189, 184).addCuboid(-5, 0, 52, 10, 4, 5, 0, false);

		small_light_r1 = createModelPart();
		small_light_r1.setPivot(5, -36, 57);
		head_exterior.addChild(small_light_r1);
		setRotationAngle(small_light_r1, 0, 0.1745F, 0);
		small_light_r1.setTextureUVOffset(0, 189).addCuboid(2, 30, 0.05F, 5, 4, 0, 0, true);

		front_side_right_r1 = createModelPart();
		front_side_right_r1.setPivot(21, -12, 40);
		head_exterior.addChild(front_side_right_r1);
		setRotationAngle(front_side_right_r1, 0, -0.0873F, -0.1571F);
		front_side_right_r1.setTextureUVOffset(136, 252).addCuboid(-1, -19, 0, 1, 19, 15, 0, true);

		front_side_left_r1 = createModelPart();
		front_side_left_r1.setPivot(-21, -12, 40);
		head_exterior.addChild(front_side_left_r1);
		setRotationAngle(front_side_left_r1, 0, 0.0873F, 0.1571F);
		front_side_left_r1.setTextureUVOffset(184, 231).addCuboid(0, -19, 0, 1, 19, 15, 0, false);

		head_exterior_1 = createModelPart();
		head_exterior_1.setPivot(0, 0, 0);
		head_exterior.addChild(head_exterior_1);
		head_exterior_1.setTextureUVOffset(100, 33).addCuboid(-21, 0, 0, 1, 4, 40, 0, false);
		head_exterior_1.setTextureUVOffset(0, 100).addCuboid(-20, 0, 25, 20, 1, 30, 0, false);
		head_exterior_1.setTextureUVOffset(70, 80).addCuboid(-20, -12, 0, 0, 12, 36, 0, false);
		head_exterior_1.setTextureUVOffset(0, 0).addCuboid(-4, -36, 0, 4, 0, 40, 0, false);
		head_exterior_1.setTextureUVOffset(92, 218).addCuboid(-20.8F, -12, 18, 1, 12, 22, 0, false);
		head_exterior_1.setTextureUVOffset(0, 0).addCuboid(-4, -36, 40, 4, 0, 17, 0, false);

		front_side_bottom_r3 = createModelPart();
		front_side_bottom_r3.setPivot(-5, -36, 57);
		head_exterior_1.addChild(front_side_bottom_r3);
		setRotationAngle(front_side_bottom_r3, 0, -0.1745F, 0);
		front_side_bottom_r3.setTextureUVOffset(111, 252).addCuboid(-15, 36, -5, 15, 4, 5, 0, false);
		front_side_bottom_r3.setTextureUVOffset(0, 147).addCuboid(-15, 0, 0, 15, 36, 0, 0, false);

		front_side_5_r3 = createModelPart();
		front_side_5_r3.setPivot(-4, -36, 40);
		head_exterior_1.addChild(front_side_5_r3);
		setRotationAngle(front_side_5_r3, 0, 0, -0.1745F);
		front_side_5_r3.setTextureUVOffset(83, 50).addCuboid(-5, 0, 0, 5, 0, 17, 0, false);
		front_side_5_r3.setTextureUVOffset(12, 56).addCuboid(-5, 0, -40, 5, 0, 40, 0, false);

		front_side_4_r3 = createModelPart();
		front_side_4_r3.setPivot(-11.0218F, -32.7659F, 58);
		head_exterior_1.addChild(front_side_4_r3);
		setRotationAngle(front_side_4_r3, 0, 0, -0.5236F);
		front_side_4_r3.setTextureUVOffset(61, 251).addCuboid(-4, -1, -18, 7, 1, 17, 0, false);

		front_side_3_r3 = createModelPart();
		front_side_3_r3.setPivot(-15.8157F, -30.1796F, 11);
		head_exterior_1.addChild(front_side_3_r3);
		setRotationAngle(front_side_3_r3, 0, 0, 0.8378F);
		front_side_3_r3.setTextureUVOffset(93, 253).addCuboid(-0.5F, -2, 29, 1, 5, 16, 0, false);

		front_side_1_r1 = createModelPart();
		front_side_1_r1.setPivot(-21, 4, 40);
		head_exterior_1.addChild(front_side_1_r1);
		setRotationAngle(front_side_1_r1, 0, 0.0873F, 0);
		front_side_1_r1.setTextureUVOffset(244, 154).addCuboid(0, -16, 0, 1, 16, 15, 0, false);

		door_right_top_r3 = createModelPart();
		door_right_top_r3.setPivot(-15.6548F, -29.9327F, 11);
		head_exterior_1.addChild(door_right_top_r3);
		setRotationAngle(door_right_top_r3, 0, 0, 0.8378F);
		door_right_top_r3.setTextureUVOffset(0, 18).addCuboid(-0.5F, -3, 7, 1, 6, 22, 0, false);

		door_right_middle_r3 = createModelPart();
		door_right_middle_r3.setPivot(-20.8F, -12, 22);
		head_exterior_1.addChild(door_right_middle_r3);
		setRotationAngle(door_right_middle_r3, 0, 0, 0.1571F);
		door_right_middle_r3.setTextureUVOffset(204, 130).addCuboid(0, -17, -4, 1, 17, 22, 0, false);

		roof_1_r5 = createModelPart();
		roof_1_r5.setPivot(-11.0223F, -32.7667F, 18);
		head_exterior_1.addChild(roof_1_r5);
		setRotationAngle(roof_1_r5, 0, 0, -0.5236F);
		roof_1_r5.setTextureUVOffset(0, 56).addCuboid(-3, -1, -18, 6, 2, 40, 0, false);

		window_top_r9 = createModelPart();
		window_top_r9.setPivot(-14.933F, -29.4388F, 0);
		head_exterior_1.addChild(window_top_r9);
		setRotationAngle(window_top_r9, 0, 0, 0.8378F);
		window_top_r9.setTextureUVOffset(38, 134).addCuboid(-0.5F, -3, 0, 0, 6, 36, 0, false);

		window_middle_r7 = createModelPart();
		window_middle_r7.setPivot(-20, -12, 0);
		head_exterior_1.addChild(window_middle_r7);
		setRotationAngle(window_middle_r7, 0, 0, 0.1571F);
		window_middle_r7.setTextureUVOffset(70, 64).addCuboid(0, -16, 0, 0, 16, 36, 0, false);

		head_exterior_2 = createModelPart();
		head_exterior_2.setPivot(0, 0, 0);
		head_exterior.addChild(head_exterior_2);
		head_exterior_2.setTextureUVOffset(100, 33).addCuboid(20, 0, 0, 1, 4, 40, 0, true);
		head_exterior_2.setTextureUVOffset(0, 100).addCuboid(0, 0, 25, 20, 1, 30, 0, true);
		head_exterior_2.setTextureUVOffset(70, 80).addCuboid(20, -12, 0, 0, 12, 36, 0, true);
		head_exterior_2.setTextureUVOffset(0, 0).addCuboid(0, -36, 0, 4, 0, 40, 0, true);
		head_exterior_2.setTextureUVOffset(92, 218).addCuboid(19.8F, -12, 18, 1, 12, 22, 0, true);
		head_exterior_2.setTextureUVOffset(0, 0).addCuboid(0, -36, 40, 4, 0, 17, 0, true);

		front_side_bottom_r4 = createModelPart();
		front_side_bottom_r4.setPivot(5, -36, 57);
		head_exterior_2.addChild(front_side_bottom_r4);
		setRotationAngle(front_side_bottom_r4, 0, 0.1745F, 0);
		front_side_bottom_r4.setTextureUVOffset(111, 252).addCuboid(0, 36, -5, 15, 4, 5, 0, true);
		front_side_bottom_r4.setTextureUVOffset(0, 147).addCuboid(0, 0, 0, 15, 36, 0, 0, true);

		front_side_6_r3 = createModelPart();
		front_side_6_r3.setPivot(4, -36, 40);
		head_exterior_2.addChild(front_side_6_r3);
		setRotationAngle(front_side_6_r3, 0, 0, 0.1745F);
		front_side_6_r3.setTextureUVOffset(83, 50).addCuboid(0, 0, 0, 5, 0, 17, 0, true);
		front_side_6_r3.setTextureUVOffset(12, 56).addCuboid(0, 0, -40, 5, 0, 40, 0, true);

		front_side_5_r4 = createModelPart();
		front_side_5_r4.setPivot(11.0218F, -32.7659F, 58);
		head_exterior_2.addChild(front_side_5_r4);
		setRotationAngle(front_side_5_r4, 0, 0, 0.5236F);
		front_side_5_r4.setTextureUVOffset(61, 251).addCuboid(-3, -1, -18, 7, 1, 17, 0, true);

		front_side_4_r4 = createModelPart();
		front_side_4_r4.setPivot(15.8157F, -30.1796F, 11);
		head_exterior_2.addChild(front_side_4_r4);
		setRotationAngle(front_side_4_r4, 0, 0, -0.8378F);
		front_side_4_r4.setTextureUVOffset(93, 253).addCuboid(-0.5F, -2, 29, 1, 5, 16, 0, true);

		front_side_2_r2 = createModelPart();
		front_side_2_r2.setPivot(21, 4, 40);
		head_exterior_2.addChild(front_side_2_r2);
		setRotationAngle(front_side_2_r2, 0, -0.0873F, 0);
		front_side_2_r2.setTextureUVOffset(244, 154).addCuboid(-1, -16, 0, 1, 16, 15, 0, true);

		door_right_top_r4 = createModelPart();
		door_right_top_r4.setPivot(15.6548F, -29.9327F, 11);
		head_exterior_2.addChild(door_right_top_r4);
		setRotationAngle(door_right_top_r4, 0, 0, -0.8378F);
		door_right_top_r4.setTextureUVOffset(0, 18).addCuboid(-0.5F, -3, 7, 1, 6, 22, 0, true);

		door_right_middle_r4 = createModelPart();
		door_right_middle_r4.setPivot(20.8F, -12, 22);
		head_exterior_2.addChild(door_right_middle_r4);
		setRotationAngle(door_right_middle_r4, 0, 0, -0.1571F);
		door_right_middle_r4.setTextureUVOffset(204, 130).addCuboid(-1, -17, -4, 1, 17, 22, 0, true);

		roof_2_r9 = createModelPart();
		roof_2_r9.setPivot(11.0223F, -32.7667F, 18);
		head_exterior_2.addChild(roof_2_r9);
		setRotationAngle(roof_2_r9, 0, 0, 0.5236F);
		roof_2_r9.setTextureUVOffset(0, 56).addCuboid(-3, -1, -18, 6, 2, 40, 0, true);

		window_top_r10 = createModelPart();
		window_top_r10.setPivot(14.933F, -29.4388F, 0);
		head_exterior_2.addChild(window_top_r10);
		setRotationAngle(window_top_r10, 0, 0, -0.8378F);
		window_top_r10.setTextureUVOffset(38, 134).addCuboid(0.5F, -3, 0, 0, 6, 36, 0, true);

		window_middle_r8 = createModelPart();
		window_middle_r8.setPivot(20, -12, 0);
		head_exterior_2.addChild(window_middle_r8);
		setRotationAngle(window_middle_r8, 0, 0, -0.1571F);
		window_middle_r8.setTextureUVOffset(70, 64).addCuboid(0, -16, 0, 0, 16, 36, 0, true);

		logo = createModelPart();
		logo.setPivot(0, 24, 0);
		logo.setTextureUVOffset(202, 9).addCuboid(-20.1F, -12, -4, 0, 8, 8, 0, false);

		door_light_on = createModelPart();
		door_light_on.setPivot(0, 24, 0);


		light_r1 = createModelPart();
		light_r1.setPivot(-14.933F, -29.4388F, 0);
		door_light_on.addChild(light_r1);
		setRotationAngle(light_r1, 0, 0, 0.8378F);
		light_r1.setTextureUVOffset(3, 0).addCuboid(-1, -1, -1, 1, 1, 2, 0, false);

		door_light_off = createModelPart();
		door_light_off.setPivot(0, 24, 0);


		light_r2 = createModelPart();
		light_r2.setPivot(-14.933F, -29.4388F, 0);
		door_light_off.addChild(light_r2);
		setRotationAngle(light_r2, 0, 0, 0.8378F);
		light_r2.setTextureUVOffset(3, 3).addCuboid(-1, -1, -1, 1, 1, 2, 0, false);

		headlights = createModelPart();
		headlights.setPivot(0, 24, 0);


		light_2_r4 = createModelPart();
		light_2_r4.setPivot(5, -36, 57);
		headlights.addChild(light_2_r4);
		setRotationAngle(light_2_r4, 0, 0.1745F, 0);
		light_2_r4.setTextureUVOffset(0, 206).addCuboid(2, 26, 0.1F, 9, 4, 0, 0, true);

		light_1_r3 = createModelPart();
		light_1_r3.setPivot(-5, -36, 57);
		headlights.addChild(light_1_r3);
		setRotationAngle(light_1_r3, 0, -0.1745F, 0);
		light_1_r3.setTextureUVOffset(0, 206).addCuboid(-11, 26, 0.1F, 9, 4, 0, 0, false);

		tail_lights = createModelPart();
		tail_lights.setPivot(0, 24, 0);


		light_3_r2 = createModelPart();
		light_3_r2.setPivot(5, -36, 57);
		tail_lights.addChild(light_3_r2);
		setRotationAngle(light_3_r2, 0, 0.1745F, 0);
		light_3_r2.setTextureUVOffset(0, 210).addCuboid(2, 26, 0.1F, 9, 4, 0, 0, true);

		light_2_r5 = createModelPart();
		light_2_r5.setPivot(-5, -36, 57);
		tail_lights.addChild(light_2_r5);
		setRotationAngle(light_2_r5, 0, -0.1745F, 0);
		light_2_r5.setTextureUVOffset(0, 210).addCuboid(-11, 26, 0.1F, 9, 4, 0, 0, false);

		buildModel();
	}

	private static final int DOOR_MAX = 12;

	@Override
	public ModelLondonUnderground1995 createNew(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		return new ModelLondonUnderground1995(is1995, doorAnimationType, renderDoorOverlay);
	}

	@Override
	protected void renderWindowPositions(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
		final boolean isEnd1 = isIndex(0, position, getWindowPositions());
		final boolean isEnd2 = isIndex(-1, position, getWindowPositions());
		final boolean useHead1 = isEnd1Head && isEnd1;
		final boolean useHead2 = isEnd2Head && isEnd2;

		switch (renderStage) {
			case LIGHT:
				if (useHead1) {
					renderOnceFlipped(window_light_head, graphicsHolder, light, position);
					renderOnce(window_light, graphicsHolder, light, position);
				} else if (useHead2) {
					renderOnce(window_light_head, graphicsHolder, light, position);
					renderOnceFlipped(window_light, graphicsHolder, light, position);
				} else {
					renderMirror(window_light, graphicsHolder, light, position);
				}
				break;
			case INTERIOR:
				if (useHead1) {
					renderOnceFlipped(head, graphicsHolder, light, position);
					renderOnce(window, graphicsHolder, light, position);
				} else if (useHead2) {
					renderOnce(head, graphicsHolder, light, position);
					renderOnceFlipped(window, graphicsHolder, light, position);
				} else {
					renderMirror(window, graphicsHolder, light, position);
				}
				if (renderDetails) {
					renderOnceFlipped(isEnd1 || isEnd2 ? seat : seat_wheelchair, graphicsHolder, light, position + (useHead1 ? 9 : 0));
					renderOnce(isEnd1 || isEnd2 ? seat : seat_wheelchair, graphicsHolder, light, position - (useHead2 ? 9 : 0));
				}
				break;
			case INTERIOR_TRANSLUCENT:
				if (isEnd1 || isEnd2) {
					if (!useHead1) {
						renderMirror(side_panel, graphicsHolder, light, position - 31.5F);
					}
					if (!useHead2) {
						renderMirror(side_panel, graphicsHolder, light, position + 31.5F);
					}
				}
				break;
			case EXTERIOR:
				if (useHead1) {
					renderOnceFlipped(head_exterior, graphicsHolder, light, position);
					renderOnce(window_exterior, graphicsHolder, light, position);
				} else if (useHead2) {
					renderOnce(head_exterior, graphicsHolder, light, position);
					renderOnceFlipped(window_exterior, graphicsHolder, light, position);
				} else {
					renderMirror(window_exterior, graphicsHolder, light, position);
				}
				if (renderDetails && position == 0) {
					renderMirror(logo, graphicsHolder, light, position);
				}
				break;
		}
	}

	@Override
	protected void renderDoorPositions(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
		if (isEnd1Head && isIndex(0, position, getDoorPositions()) || isEnd2Head && isIndex(-1, position, getDoorPositions())) {
			return;
		}

		final boolean useEnd1 = isIndex(0, position, getDoorPositions());
		final boolean useEnd2 = isIndex(-1, position, getDoorPositions());
		final boolean isDoorLight1 = isIndex(1, position, getDoorPositions());
		final boolean isDoorLight2 = isIndex(-2, position, getDoorPositions());
		final boolean doorOpen = doorLeftZ > 0 || doorRightZ > 0;

		switch (renderStage) {
			case LIGHT:
				if (doorOpen && renderDetails) {
					if (isDoorLight1) {
						renderMirror(door_light_on, graphicsHolder, light, position - 48);
					} else if (isDoorLight2) {
						renderMirror(door_light_on, graphicsHolder, light, position + 48);
					}
				}
				break;
			case INTERIOR:
				if (!useEnd1) {
					door_right_part.setOffset(0, 0, -doorRightZ);
					renderOnce(door_right, graphicsHolder, light, position);
					door_left_part.setOffset(0, 0, doorLeftZ);
					renderOnceFlipped(door_left, graphicsHolder, light, position);
				}
				if (!useEnd2) {
					door_left_part.setOffset(0, 0, doorRightZ);
					renderOnce(door_left, graphicsHolder, light, position);
					door_right_part.setOffset(0, 0, -doorLeftZ);
					renderOnceFlipped(door_right, graphicsHolder, light, position);
				}
				break;
			case EXTERIOR:
				if (!useEnd1) {
					door_right_exterior_part.setOffset(0, 0, -doorRightZ);
					renderOnce(door_right_exterior, graphicsHolder, light, position);
					door_left_exterior_part.setOffset(0, 0, doorLeftZ);
					renderOnceFlipped(door_left_exterior, graphicsHolder, light, position);
				}
				if (!useEnd2) {
					door_left_exterior_part.setOffset(0, 0, doorRightZ);
					renderOnce(door_left_exterior, graphicsHolder, light, position);
					door_right_exterior_part.setOffset(0, 0, -doorLeftZ);
					renderOnceFlipped(door_right_exterior, graphicsHolder, light, position);
				}
				if (!doorOpen && renderDetails) {
					if (isDoorLight1) {
						renderMirror(door_light_off, graphicsHolder, light, position - 48);
					} else if (isDoorLight2) {
						renderMirror(door_light_off, graphicsHolder, light, position + 48);
					}
				}
				break;
		}
	}

	@Override
	protected void renderHeadPosition1(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean useHeadlights) {
		if (renderStage == RenderStage.ALWAYS_ON_LIGHT) {
			renderOnceFlipped(useHeadlights ? headlights : tail_lights, graphicsHolder, light, position);
		}
	}

	@Override
	protected void renderHeadPosition2(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean useHeadlights) {
		if (renderStage == RenderStage.ALWAYS_ON_LIGHT) {
			renderOnce(useHeadlights ? headlights : tail_lights, graphicsHolder, light, position);
		}
	}

	@Override
	protected void renderEndPosition1(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
		switch (renderStage) {
			case LIGHT:
				renderOnceFlipped(window_light_end, graphicsHolder, light, position);
				break;
			case INTERIOR:
				renderOnceFlipped(end, graphicsHolder, light, position);
				break;
			case EXTERIOR:
				renderOnceFlipped(end_exterior, graphicsHolder, light, position);
				break;
		}
	}

	@Override
	protected void renderEndPosition2(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
		switch (renderStage) {
			case LIGHT:
				renderOnce(window_light_end, graphicsHolder, light, position);
				break;
			case INTERIOR:
				renderOnce(end, graphicsHolder, light, position);
				break;
			case EXTERIOR:
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
		return new int[]{-96, 0, 96};
	}

	@Override
	protected int[] getDoorPositions() {
		return new int[]{-144, -48, 48, 144};
	}

	@Override
	protected int[] getEndPositions() {
		return new int[]{-96, 96};
	}

	@Override
	protected int getDoorMax() {
		return DOOR_MAX;
	}

	@Override
	protected void renderTextDisplays(StoredMatrixTransformations storedMatrixTransformations, int thisRouteColor, String thisRouteName, String thisRouteNumber, String thisStationName, String thisRouteDestination, String nextStationName, Consumer<BiConsumer<String, InterchangeColorsForStationName>> getInterchanges, int car, int totalCars, boolean atPlatform, boolean isTerminating, ObjectArrayList<ScrollingText> scrollingTexts) {
		if (scrollingTexts.isEmpty()) {
			scrollingTexts.add(new ScrollingText(0.46F, 0.09F, 6, false));
		}

		final String destinationString = getDestinationString(thisRouteDestination, TextSpacingType.NORMAL, false);
		renderFrontDestination(
				storedMatrixTransformations,
				0, -2.08F, getEndPositions()[0] / 16F - 3.56F, 0, 0, -0.01F,
				0, 0, 0.66F, 0.08F,
				0xFFFF9900, 0xFFFF9900, 1, getAlternatingString(destinationString), false, car, totalCars
		);

		final String nextStationString = getLondonNextStationString(thisRouteName, thisStationName, nextStationName, getInterchanges, destinationString, atPlatform, isTerminating);
		scrollingTexts.get(0).changeImage(nextStationString.isEmpty() ? null : DynamicTextureCache.instance.getPixelatedText(nextStationString, is1995 ? 0xFFFF9900 : 0xFFFF0000, Integer.MAX_VALUE, 0, false));

		final int[] positions = {-6, 0, 6};
		for (final int position : positions) {
			for (int i = 0; i < 2; i++) {
				final StoredMatrixTransformations storedMatrixTransformationsNew = storedMatrixTransformations.copy();
				final boolean flip = i == 1;
				storedMatrixTransformationsNew.add(graphicsHolder -> {
					graphicsHolder.rotateYDegrees(flip ? -90 : 90);
					graphicsHolder.rotateXDegrees(48);
					graphicsHolder.translate(position - 0.23F, -0.55F, 1.96F);
				});
				scrollingTexts.get(0).scrollText(storedMatrixTransformationsNew);
			}
		}
	}

	@Override
	protected String defaultDestinationString() {
		return "Not in Service";
	}
}