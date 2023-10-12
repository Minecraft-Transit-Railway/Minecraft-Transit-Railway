package org.mtr.mod.model;

import org.mtr.core.data.InterchangeColorsForStationName;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.ModelPartExtension;
import org.mtr.mod.client.DoorAnimationType;
import org.mtr.mod.client.DynamicTextureCache;
import org.mtr.mod.client.ScrollingText;
import org.mtr.mod.render.StoredMatrixTransformations;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ModelClass377 extends ModelSimpleTrainBase<ModelClass377> {

	private final ModelPartExtension window;
	private final ModelPartExtension light_edge_r1;
	private final ModelPartExtension roof_1_r1;
	private final ModelPartExtension window_bottom_r1;
	private final ModelPartExtension window_light;
	private final ModelPartExtension light_r1;
	private final ModelPartExtension door_light;
	private final ModelPartExtension light_r2;
	private final ModelPartExtension window_1;
	private final ModelPartExtension window_top_r1;
	private final ModelPartExtension window_2;
	private final ModelPartExtension window_top_r2;
	private final ModelPartExtension window_3;
	private final ModelPartExtension window_top_r3;
	private final ModelPartExtension window_4;
	private final ModelPartExtension window_top_r4;
	private final ModelPartExtension window_5;
	private final ModelPartExtension window_top_r5;
	private final ModelPartExtension window_6;
	private final ModelPartExtension window_top_r6;
	private final ModelPartExtension window_7;
	private final ModelPartExtension window_top_r7;
	private final ModelPartExtension window_8;
	private final ModelPartExtension window_top_r8;
	private final ModelPartExtension window_exterior_1;
	private final ModelPartExtension window_exterior_1_1;
	private final ModelPartExtension window_bottom_r2;
	private final ModelPartExtension window_top_r9;
	private final ModelPartExtension window_exterior_1_2;
	private final ModelPartExtension window_bottom_r3;
	private final ModelPartExtension window_top_r10;
	private final ModelPartExtension window_exterior_2;
	private final ModelPartExtension window_exterior_1_3;
	private final ModelPartExtension window_bottom_r4;
	private final ModelPartExtension window_top_r11;
	private final ModelPartExtension window_exterior_1_4;
	private final ModelPartExtension window_bottom_r5;
	private final ModelPartExtension window_top_r12;
	private final ModelPartExtension window_exterior_3;
	private final ModelPartExtension window_exterior_1_5;
	private final ModelPartExtension window_bottom_r6;
	private final ModelPartExtension window_top_r13;
	private final ModelPartExtension window_exterior_1_6;
	private final ModelPartExtension window_bottom_r7;
	private final ModelPartExtension window_top_r14;
	private final ModelPartExtension window_exterior_4;
	private final ModelPartExtension window_exterior_1_7;
	private final ModelPartExtension window_bottom_r8;
	private final ModelPartExtension window_top_r15;
	private final ModelPartExtension window_exterior_1_8;
	private final ModelPartExtension window_bottom_r9;
	private final ModelPartExtension window_top_r16;
	private final ModelPartExtension window_exterior_5;
	private final ModelPartExtension window_exterior_1_9;
	private final ModelPartExtension window_bottom_r10;
	private final ModelPartExtension window_top_r17;
	private final ModelPartExtension window_exterior_1_10;
	private final ModelPartExtension window_bottom_r11;
	private final ModelPartExtension window_top_r18;
	private final ModelPartExtension window_exterior_6;
	private final ModelPartExtension window_exterior_1_11;
	private final ModelPartExtension window_bottom_r12;
	private final ModelPartExtension window_top_r19;
	private final ModelPartExtension window_exterior_1_12;
	private final ModelPartExtension window_bottom_r13;
	private final ModelPartExtension window_top_r20;
	private final ModelPartExtension window_exterior_7;
	private final ModelPartExtension window_exterior_1_13;
	private final ModelPartExtension window_bottom_r14;
	private final ModelPartExtension window_top_r21;
	private final ModelPartExtension window_exterior_1_14;
	private final ModelPartExtension window_bottom_r15;
	private final ModelPartExtension window_top_r22;
	private final ModelPartExtension window_exterior_8;
	private final ModelPartExtension window_exterior_1_15;
	private final ModelPartExtension window_bottom_r16;
	private final ModelPartExtension window_top_r23;
	private final ModelPartExtension window_exterior_1_16;
	private final ModelPartExtension window_bottom_r17;
	private final ModelPartExtension window_top_r24;
	private final ModelPartExtension window_end_exterior_1;
	private final ModelPartExtension window_end_exterior_1_1;
	private final ModelPartExtension window_bottom_r18;
	private final ModelPartExtension window_top_r25;
	private final ModelPartExtension window_end_exterior_1_2;
	private final ModelPartExtension window_bottom_r19;
	private final ModelPartExtension window_top_r26;
	private final ModelPartExtension window_end_exterior_2;
	private final ModelPartExtension window_end_exterior_1_3;
	private final ModelPartExtension window_bottom_r20;
	private final ModelPartExtension window_top_r27;
	private final ModelPartExtension window_end_exterior_1_4;
	private final ModelPartExtension window_bottom_r21;
	private final ModelPartExtension window_top_r28;
	private final ModelPartExtension window_end_exterior_3;
	private final ModelPartExtension window_end_exterior_1_5;
	private final ModelPartExtension window_bottom_r22;
	private final ModelPartExtension window_top_r29;
	private final ModelPartExtension window_end_exterior_1_6;
	private final ModelPartExtension window_bottom_r23;
	private final ModelPartExtension window_top_r30;
	private final ModelPartExtension window_end_exterior_4;
	private final ModelPartExtension window_end_exterior_1_7;
	private final ModelPartExtension window_bottom_r24;
	private final ModelPartExtension window_top_r31;
	private final ModelPartExtension window_end_exterior_1_8;
	private final ModelPartExtension window_bottom_r25;
	private final ModelPartExtension window_top_r32;
	private final ModelPartExtension roof_exterior_1;
	private final ModelPartExtension roof_3_r1;
	private final ModelPartExtension roof_2_r1;
	private final ModelPartExtension roof_1_r2;
	private final ModelPartExtension roof_exterior_door_edge;
	private final ModelPartExtension roof_exterior_door_edge_1;
	private final ModelPartExtension roof_4_r1;
	private final ModelPartExtension roof_3_r2;
	private final ModelPartExtension roof_2_r2;
	private final ModelPartExtension roof_exterior_door_edge_2;
	private final ModelPartExtension roof_5_r1;
	private final ModelPartExtension roof_4_r2;
	private final ModelPartExtension roof_3_r3;
	private final ModelPartExtension roof_exterior_door;
	private final ModelPartExtension roof_exterior_door_1;
	private final ModelPartExtension roof_5_r2;
	private final ModelPartExtension roof_4_r3;
	private final ModelPartExtension roof_3_r4;
	private final ModelPartExtension roof_exterior_door_2;
	private final ModelPartExtension roof_6_r1;
	private final ModelPartExtension roof_5_r3;
	private final ModelPartExtension roof_4_r4;
	private final ModelPartExtension door;
	private final ModelPartExtension light_edge_r2;
	private final ModelPartExtension door_top_3_r1;
	private final ModelPartExtension door_top_2_r1;
	private final ModelPartExtension window_top_2_r1;
	private final ModelPartExtension window_bottom_2_r1;
	private final ModelPartExtension door_left;
	private final ModelPartExtension door_top_r1;
	private final ModelPartExtension door_bottom_r1;
	private final ModelPartExtension door_right;
	private final ModelPartExtension door_top_r2;
	private final ModelPartExtension door_bottom_r2;
	private final ModelPartExtension door_exterior;
	private final ModelPartExtension window_top_2_r2;
	private final ModelPartExtension window_bottom_2_r2;
	private final ModelPartExtension door_left_exterior;
	private final ModelPartExtension door_top_r3;
	private final ModelPartExtension door_bottom_r3;
	private final ModelPartExtension door_right_exterior;
	private final ModelPartExtension door_top_r4;
	private final ModelPartExtension door_bottom_r4;
	private final ModelPartExtension door_exterior_end;
	private final ModelPartExtension door_exterior_end_1;
	private final ModelPartExtension window_top_2_r3;
	private final ModelPartExtension window_bottom_2_r3;
	private final ModelPartExtension door_left_exterior_end_1;
	private final ModelPartExtension door_top_r5;
	private final ModelPartExtension door_bottom_r5;
	private final ModelPartExtension door_right_exterior_end_1;
	private final ModelPartExtension door_top_r6;
	private final ModelPartExtension door_bottom_r6;
	private final ModelPartExtension door_exterior_end_2;
	private final ModelPartExtension window_top_2_r4;
	private final ModelPartExtension window_bottom_2_r4;
	private final ModelPartExtension door_left_exterior_end_2;
	private final ModelPartExtension door_top_r7;
	private final ModelPartExtension door_bottom_r7;
	private final ModelPartExtension door_right_exterior_end_2;
	private final ModelPartExtension door_top_r8;
	private final ModelPartExtension door_bottom_r8;
	private final ModelPartExtension end;
	private final ModelPartExtension end_1;
	private final ModelPartExtension end_2;
	private final ModelPartExtension end_exterior;
	private final ModelPartExtension end_exterior_1;
	private final ModelPartExtension window_bottom_r26;
	private final ModelPartExtension window_top_r33;
	private final ModelPartExtension end_exterior_2;
	private final ModelPartExtension window_bottom_r27;
	private final ModelPartExtension window_top_r34;
	private final ModelPartExtension roof_end_exterior_1;
	private final ModelPartExtension roof_3_r5;
	private final ModelPartExtension roof_2_r3;
	private final ModelPartExtension roof_1_r3;
	private final ModelPartExtension roof_end_exterior_2;
	private final ModelPartExtension roof_4_r5;
	private final ModelPartExtension roof_3_r6;
	private final ModelPartExtension roof_2_r4;
	private final ModelPartExtension head;
	private final ModelPartExtension head_exterior;
	private final ModelPartExtension front_r1;
	private final ModelPartExtension head_exterior_1;
	private final ModelPartExtension front_bottom_r1;
	private final ModelPartExtension roof_5_r4;
	private final ModelPartExtension roof_4_r6;
	private final ModelPartExtension roof_3_r7;
	private final ModelPartExtension roof_2_r5;
	private final ModelPartExtension window_bottom_r28;
	private final ModelPartExtension window_top_r35;
	private final ModelPartExtension head_exterior_2;
	private final ModelPartExtension front_bottom_r2;
	private final ModelPartExtension roof_6_r2;
	private final ModelPartExtension roof_5_r5;
	private final ModelPartExtension roof_4_r7;
	private final ModelPartExtension roof_3_r8;
	private final ModelPartExtension window_bottom_r29;
	private final ModelPartExtension window_top_r36;
	private final ModelPartExtension seat;
	private final ModelPartExtension seat_2_r1;
	private final ModelPartExtension door_light_box;
	private final ModelPartExtension door_light_r1;
	private final ModelPartExtension door_light_off;
	private final ModelPartExtension door_light_r2;
	private final ModelPartExtension door_light_on;
	private final ModelPartExtension door_light_r3;
	private final ModelPartExtension headlights;
	private final ModelPartExtension headlight_2_r1;
	private final ModelPartExtension tail_lights;
	private final ModelPartExtension tail_light_2_r1;

	public ModelClass377() {
		this(DoorAnimationType.PLUG_FAST, true);
	}

	private ModelClass377(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		super(368, 368, doorAnimationType, renderDoorOverlay);

		window = createModelPart();
		window.setPivot(0, 24, 0);
		window.setTextureUVOffset(0, 78).addCuboid(-19, 0, -12.5F, 19, 1, 25, 0, false);
		window.setTextureUVOffset(208, 221).addCuboid(-20, -13, -12.5F, 0, 6, 25, 0, false);
		window.setTextureUVOffset(133, 277).addCuboid(-18, -31, -12.5F, 2, 4, 25, 0, false);
		window.setTextureUVOffset(208, 91).addCuboid(-18, -32, -12.5F, 6, 1, 25, 0, false);
		window.setTextureUVOffset(183, 117).addCuboid(-5, -35, -12.5F, 5, 0, 25, 0, false);

		light_edge_r1 = createModelPart();
		light_edge_r1.setPivot(-5, -35, 0);
		window.addChild(light_edge_r1);
		setRotationAngle(light_edge_r1, 0, 0, 0.2182F);
		light_edge_r1.setTextureUVOffset(292, 120).addCuboid(-5, 0, -12.5F, 1, 1, 25, 0, false);
		light_edge_r1.setTextureUVOffset(38, 78).addCuboid(-9, 0, -12.5F, 9, 0, 25, 0, false);

		roof_1_r1 = createModelPart();
		roof_1_r1.setPivot(-18, -32, 0);
		window.addChild(roof_1_r1);
		setRotationAngle(roof_1_r1, 0, 0, -0.7854F);
		roof_1_r1.setTextureUVOffset(181, 173).addCuboid(0, 0, -12.5F, 7, 0, 25, 0, false);

		window_bottom_r1 = createModelPart();
		window_bottom_r1.setPivot(-21, -7, 0);
		window.addChild(window_bottom_r1);
		setRotationAngle(window_bottom_r1, 0, 0, -0.2094F);
		window_bottom_r1.setTextureUVOffset(208, 206).addCuboid(1, 0, -12.5F, 0, 8, 25, 0, false);

		window_light = createModelPart();
		window_light.setPivot(0, 24, 0);
		window_light.setTextureUVOffset(183, 142).addCuboid(-4, -35.2F, -12.5F, 4, 0, 25, 0, false);

		light_r1 = createModelPart();
		light_r1.setPivot(-5, -35, 0);
		window_light.addChild(light_r1);
		setRotationAngle(light_r1, 0, 0, 0.2182F);
		light_r1.setTextureUVOffset(287, 238).addCuboid(-5.5F, -0.2F, -12.5F, 2, 1, 25, 0, false);

		door_light = createModelPart();
		door_light.setPivot(0, 24, 0);
		door_light.setTextureUVOffset(172, 116).addCuboid(-4, -35.2F, -14, 4, 0, 28, 0, false);

		light_r2 = createModelPart();
		light_r2.setPivot(-5, -35, 0);
		door_light.addChild(light_r2);
		setRotationAngle(light_r2, 0, 0, 0.2182F);
		light_r2.setTextureUVOffset(206, 173).addCuboid(-5.5F, -0.2F, -14, 2, 1, 28, 0, false);

		window_1 = createModelPart();
		window_1.setPivot(0, 24, 0);


		window_top_r1 = createModelPart();
		window_top_r1.setPivot(-21, -13, 0);
		window_1.addChild(window_top_r1);
		setRotationAngle(window_top_r1, 0, 0, 0.1396F);
		window_top_r1.setTextureUVOffset(100, 107).addCuboid(1, -21, -12.5F, 0, 21, 25, 0, false);

		window_2 = createModelPart();
		window_2.setPivot(0, 24, 0);


		window_top_r2 = createModelPart();
		window_top_r2.setPivot(-21, -13, 0);
		window_2.addChild(window_top_r2);
		setRotationAngle(window_top_r2, 0, 0, 0.1396F);
		window_top_r2.setTextureUVOffset(50, 107).addCuboid(1, -21, -12.5F, 0, 21, 25, 0, false);

		window_3 = createModelPart();
		window_3.setPivot(0, 24, 0);


		window_top_r3 = createModelPart();
		window_top_r3.setPivot(-21, -13, 0);
		window_3.addChild(window_top_r3);
		setRotationAngle(window_top_r3, 0, 0, 0.1396F);
		window_top_r3.setTextureUVOffset(100, 86).addCuboid(1, -21, -12.5F, 0, 21, 25, 0, false);

		window_4 = createModelPart();
		window_4.setPivot(0, 24, 0);


		window_top_r4 = createModelPart();
		window_top_r4.setPivot(-21, -13, 0);
		window_4.addChild(window_top_r4);
		setRotationAngle(window_top_r4, 0, 0, 0.1396F);
		window_top_r4.setTextureUVOffset(0, 100).addCuboid(1, -21, -12.5F, 0, 21, 25, 0, false);

		window_5 = createModelPart();
		window_5.setPivot(0, 24, 0);


		window_top_r5 = createModelPart();
		window_top_r5.setPivot(-21, -13, 0);
		window_5.addChild(window_top_r5);
		setRotationAngle(window_top_r5, 0, 0, 0.1396F);
		window_top_r5.setTextureUVOffset(98, 53).addCuboid(1, -21, -12.5F, 0, 21, 25, 0, false);

		window_6 = createModelPart();
		window_6.setPivot(0, 24, 0);


		window_top_r6 = createModelPart();
		window_top_r6.setPivot(-21, -13, 0);
		window_6.addChild(window_top_r6);
		setRotationAngle(window_top_r6, 0, 0, 0.1396F);
		window_top_r6.setTextureUVOffset(88, 3).addCuboid(1, -21, -12.5F, 0, 21, 25, 0, false);

		window_7 = createModelPart();
		window_7.setPivot(0, 24, 0);


		window_top_r7 = createModelPart();
		window_top_r7.setPivot(-21, -13, 0);
		window_7.addChild(window_top_r7);
		setRotationAngle(window_top_r7, 0, 0, 0.1396F);
		window_top_r7.setTextureUVOffset(50, 86).addCuboid(1, -21, -12.5F, 0, 21, 25, 0, false);

		window_8 = createModelPart();
		window_8.setPivot(0, 24, 0);


		window_top_r8 = createModelPart();
		window_top_r8.setPivot(-21, -13, 0);
		window_8.addChild(window_top_r8);
		setRotationAngle(window_top_r8, 0, 0, 0.1396F);
		window_top_r8.setTextureUVOffset(0, 79).addCuboid(1, -21, -12.5F, 0, 21, 25, 0, false);

		window_exterior_1 = createModelPart();
		window_exterior_1.setPivot(0, 24, 0);


		window_exterior_1_1 = createModelPart();
		window_exterior_1_1.setPivot(0, 0, 0);
		window_exterior_1.addChild(window_exterior_1_1);
		window_exterior_1_1.setTextureUVOffset(286, 89).addCuboid(-21, -13, -12.5F, 0, 6, 25, 0, false);

		window_bottom_r2 = createModelPart();
		window_bottom_r2.setPivot(-21, -7, 0);
		window_exterior_1_1.addChild(window_bottom_r2);
		setRotationAngle(window_bottom_r2, 0, 0, -0.2094F);
		window_bottom_r2.setTextureUVOffset(272, 33).addCuboid(0, 0, -12.5F, 1, 8, 25, 0, false);

		window_top_r9 = createModelPart();
		window_top_r9.setPivot(-21, -13, 0);
		window_exterior_1_1.addChild(window_top_r9);
		setRotationAngle(window_top_r9, 0, 0, 0.1396F);
		window_top_r9.setTextureUVOffset(198, 42).addCuboid(0, -21, -12.5F, 0, 21, 25, 0, false);

		window_exterior_1_2 = createModelPart();
		window_exterior_1_2.setPivot(0, 0, 0);
		window_exterior_1.addChild(window_exterior_1_2);
		window_exterior_1_2.setTextureUVOffset(286, 83).addCuboid(21, -13, -12.5F, 0, 6, 25, 0, true);

		window_bottom_r3 = createModelPart();
		window_bottom_r3.setPivot(21, -7, 0);
		window_exterior_1_2.addChild(window_bottom_r3);
		setRotationAngle(window_bottom_r3, 0, 0, 0.2094F);
		window_bottom_r3.setTextureUVOffset(52, 270).addCuboid(-1, 0, -12.5F, 1, 8, 25, 0, true);

		window_top_r10 = createModelPart();
		window_top_r10.setPivot(21, -13, 0);
		window_exterior_1_2.addChild(window_top_r10);
		setRotationAngle(window_top_r10, 0, 0, -0.1396F);
		window_top_r10.setTextureUVOffset(100, 191).addCuboid(0, -21, -12.5F, 0, 21, 25, 0, true);

		window_exterior_2 = createModelPart();
		window_exterior_2.setPivot(0, 24, 0);


		window_exterior_1_3 = createModelPart();
		window_exterior_1_3.setPivot(0, 0, 0);
		window_exterior_2.addChild(window_exterior_1_3);
		window_exterior_1_3.setTextureUVOffset(38, 278).addCuboid(-21, -13, -12.5F, 0, 6, 25, 0, false);

		window_bottom_r4 = createModelPart();
		window_bottom_r4.setPivot(-21, -7, 0);
		window_exterior_1_3.addChild(window_bottom_r4);
		setRotationAngle(window_bottom_r4, 0, 0, -0.2094F);
		window_bottom_r4.setTextureUVOffset(259, 96).addCuboid(0, 0, -12.5F, 1, 8, 25, 0, false);

		window_top_r11 = createModelPart();
		window_top_r11.setPivot(-21, -13, 0);
		window_exterior_1_3.addChild(window_top_r11);
		setRotationAngle(window_top_r11, 0, 0, 0.1396F);
		window_top_r11.setTextureUVOffset(150, 158).addCuboid(0, -21, -12.5F, 0, 21, 25, 0, false);

		window_exterior_1_4 = createModelPart();
		window_exterior_1_4.setPivot(0, 0, 0);
		window_exterior_2.addChild(window_exterior_1_4);
		window_exterior_1_4.setTextureUVOffset(187, 272).addCuboid(21, -13, -12.5F, 0, 6, 25, 0, true);

		window_bottom_r5 = createModelPart();
		window_bottom_r5.setPivot(21, -7, 0);
		window_exterior_1_4.addChild(window_bottom_r5);
		setRotationAngle(window_bottom_r5, 0, 0, 0.2094F);
		window_bottom_r5.setTextureUVOffset(253, 0).addCuboid(-1, 0, -12.5F, 1, 8, 25, 0, true);

		window_top_r12 = createModelPart();
		window_top_r12.setPivot(21, -13, 0);
		window_exterior_1_4.addChild(window_top_r12);
		setRotationAngle(window_top_r12, 0, 0, -0.1396F);
		window_top_r12.setTextureUVOffset(150, 137).addCuboid(0, -21, -12.5F, 0, 21, 25, 0, true);

		window_exterior_3 = createModelPart();
		window_exterior_3.setPivot(0, 24, 0);


		window_exterior_1_5 = createModelPart();
		window_exterior_1_5.setPivot(0, 0, 0);
		window_exterior_3.addChild(window_exterior_1_5);
		window_exterior_1_5.setTextureUVOffset(272, 53).addCuboid(-21, -13, -12.5F, 0, 6, 25, 0, false);

		window_bottom_r6 = createModelPart();
		window_bottom_r6.setPivot(-21, -7, 0);
		window_exterior_1_5.addChild(window_bottom_r6);
		setRotationAngle(window_bottom_r6, 0, 0, -0.2094F);
		window_bottom_r6.setTextureUVOffset(79, 245).addCuboid(0, 0, -12.5F, 1, 8, 25, 0, false);

		window_top_r13 = createModelPart();
		window_top_r13.setPivot(-21, -13, 0);
		window_exterior_1_5.addChild(window_top_r13);
		setRotationAngle(window_top_r13, 0, 0, 0.1396F);
		window_top_r13.setTextureUVOffset(150, 116).addCuboid(0, -21, -12.5F, 0, 21, 25, 0, false);

		window_exterior_1_6 = createModelPart();
		window_exterior_1_6.setPivot(0, 0, 0);
		window_exterior_3.addChild(window_exterior_1_6);
		window_exterior_1_6.setTextureUVOffset(272, 47).addCuboid(21, -13, -12.5F, 0, 6, 25, 0, true);

		window_bottom_r7 = createModelPart();
		window_bottom_r7.setPivot(21, -7, 0);
		window_exterior_1_6.addChild(window_bottom_r7);
		setRotationAngle(window_bottom_r7, 0, 0, 0.2094F);
		window_bottom_r7.setTextureUVOffset(245, 63).addCuboid(-1, 0, -12.5F, 1, 8, 25, 0, true);

		window_top_r14 = createModelPart();
		window_top_r14.setPivot(21, -13, 0);
		window_exterior_1_6.addChild(window_top_r14);
		setRotationAngle(window_top_r14, 0, 0, -0.1396F);
		window_top_r14.setTextureUVOffset(150, 95).addCuboid(0, -21, -12.5F, 0, 21, 25, 0, true);

		window_exterior_4 = createModelPart();
		window_exterior_4.setPivot(0, 24, 0);


		window_exterior_1_7 = createModelPart();
		window_exterior_1_7.setPivot(0, 0, 0);
		window_exterior_4.addChild(window_exterior_1_7);
		window_exterior_1_7.setTextureUVOffset(272, 41).addCuboid(-21, -13, -12.5F, 0, 6, 25, 0, false);

		window_bottom_r8 = createModelPart();
		window_bottom_r8.setPivot(-21, -7, 0);
		window_exterior_1_7.addChild(window_bottom_r8);
		setRotationAngle(window_bottom_r8, 0, 0, -0.2094F);
		window_bottom_r8.setTextureUVOffset(152, 244).addCuboid(0, 0, -12.5F, 1, 8, 25, 0, false);

		window_top_r15 = createModelPart();
		window_top_r15.setPivot(-21, -13, 0);
		window_exterior_1_7.addChild(window_top_r15);
		setRotationAngle(window_top_r15, 0, 0, 0.1396F);
		window_top_r15.setTextureUVOffset(150, 74).addCuboid(0, -21, -12.5F, 0, 21, 25, 0, false);

		window_exterior_1_8 = createModelPart();
		window_exterior_1_8.setPivot(0, 0, 0);
		window_exterior_4.addChild(window_exterior_1_8);
		window_exterior_1_8.setTextureUVOffset(79, 259).addCuboid(21, -13, -12.5F, 0, 6, 25, 0, true);

		window_bottom_r9 = createModelPart();
		window_bottom_r9.setPivot(21, -7, 0);
		window_exterior_1_8.addChild(window_bottom_r9);
		setRotationAngle(window_bottom_r9, 0, 0, 0.2094F);
		window_bottom_r9.setTextureUVOffset(241, 183).addCuboid(-1, 0, -12.5F, 1, 8, 25, 0, true);

		window_top_r16 = createModelPart();
		window_top_r16.setPivot(21, -13, 0);
		window_exterior_1_8.addChild(window_top_r16);
		setRotationAngle(window_top_r16, 0, 0, -0.1396F);
		window_top_r16.setTextureUVOffset(100, 149).addCuboid(0, -21, -12.5F, 0, 21, 25, 0, true);

		window_exterior_5 = createModelPart();
		window_exterior_5.setPivot(0, 24, 0);


		window_exterior_1_9 = createModelPart();
		window_exterior_1_9.setPivot(0, 0, 0);
		window_exterior_5.addChild(window_exterior_1_9);
		window_exterior_1_9.setTextureUVOffset(206, 257).addCuboid(-21, -13, -12.5F, 0, 6, 25, 0, false);

		window_bottom_r10 = createModelPart();
		window_bottom_r10.setPivot(-21, -7, 0);
		window_exterior_1_9.addChild(window_bottom_r10);
		setRotationAngle(window_bottom_r10, 0, 0, -0.2094F);
		window_bottom_r10.setTextureUVOffset(238, 150).addCuboid(0, 0, -12.5F, 1, 8, 25, 0, false);

		window_top_r17 = createModelPart();
		window_top_r17.setPivot(-21, -13, 0);
		window_exterior_1_9.addChild(window_top_r17);
		setRotationAngle(window_top_r17, 0, 0, 0.1396F);
		window_top_r17.setTextureUVOffset(50, 149).addCuboid(0, -21, -12.5F, 0, 21, 25, 0, false);

		window_exterior_1_10 = createModelPart();
		window_exterior_1_10.setPivot(0, 0, 0);
		window_exterior_5.addChild(window_exterior_1_10);
		window_exterior_1_10.setTextureUVOffset(27, 257).addCuboid(21, -13, -12.5F, 0, 6, 25, 0, true);

		window_bottom_r11 = createModelPart();
		window_bottom_r11.setPivot(21, -7, 0);
		window_exterior_1_10.addChild(window_bottom_r11);
		setRotationAngle(window_bottom_r11, 0, 0, 0.2094F);
		window_bottom_r11.setTextureUVOffset(52, 237).addCuboid(-1, 0, -12.5F, 1, 8, 25, 0, true);

		window_top_r18 = createModelPart();
		window_top_r18.setPivot(21, -13, 0);
		window_exterior_1_10.addChild(window_top_r18);
		setRotationAngle(window_top_r18, 0, 0, -0.1396F);
		window_top_r18.setTextureUVOffset(148, 53).addCuboid(0, -21, -12.5F, 0, 21, 25, 0, true);

		window_exterior_6 = createModelPart();
		window_exterior_6.setPivot(0, 24, 0);


		window_exterior_1_11 = createModelPart();
		window_exterior_1_11.setPivot(0, 0, 0);
		window_exterior_6.addChild(window_exterior_1_11);
		window_exterior_1_11.setTextureUVOffset(79, 253).addCuboid(-21, -13, -12.5F, 0, 6, 25, 0, false);

		window_bottom_r12 = createModelPart();
		window_bottom_r12.setPivot(-21, -7, 0);
		window_exterior_1_11.addChild(window_bottom_r12);
		setRotationAngle(window_bottom_r12, 0, 0, -0.2094F);
		window_bottom_r12.setTextureUVOffset(125, 236).addCuboid(0, 0, -12.5F, 1, 8, 25, 0, false);

		window_top_r19 = createModelPart();
		window_top_r19.setPivot(-21, -13, 0);
		window_exterior_1_11.addChild(window_top_r19);
		setRotationAngle(window_top_r19, 0, 0, 0.1396F);
		window_top_r19.setTextureUVOffset(0, 142).addCuboid(0, -21, -12.5F, 0, 21, 25, 0, false);

		window_exterior_1_12 = createModelPart();
		window_exterior_1_12.setPivot(0, 0, 0);
		window_exterior_6.addChild(window_exterior_1_12);
		window_exterior_1_12.setTextureUVOffset(206, 251).addCuboid(21, -13, -12.5F, 0, 6, 25, 0, true);

		window_bottom_r13 = createModelPart();
		window_bottom_r13.setPivot(21, -7, 0);
		window_exterior_1_12.addChild(window_bottom_r13);
		setRotationAngle(window_bottom_r13, 0, 0, 0.2094F);
		window_bottom_r13.setTextureUVOffset(233, 231).addCuboid(-1, 0, -12.5F, 1, 8, 25, 0, true);

		window_top_r20 = createModelPart();
		window_top_r20.setPivot(21, -13, 0);
		window_exterior_1_12.addChild(window_top_r20);
		setRotationAngle(window_top_r20, 0, 0, -0.1396F);
		window_top_r20.setTextureUVOffset(138, 0).addCuboid(0, -21, -12.5F, 0, 21, 25, 0, true);

		window_exterior_7 = createModelPart();
		window_exterior_7.setPivot(0, 24, 0);


		window_exterior_1_13 = createModelPart();
		window_exterior_1_13.setPivot(0, 0, 0);
		window_exterior_7.addChild(window_exterior_1_13);
		window_exterior_1_13.setTextureUVOffset(27, 251).addCuboid(-21, -13, -12.5F, 0, 6, 25, 0, false);

		window_bottom_r14 = createModelPart();
		window_bottom_r14.setPivot(-21, -7, 0);
		window_exterior_1_13.addChild(window_bottom_r14);
		setRotationAngle(window_bottom_r14, 0, 0, -0.2094F);
		window_bottom_r14.setTextureUVOffset(232, 117).addCuboid(0, 0, -12.5F, 1, 8, 25, 0, false);

		window_top_r21 = createModelPart();
		window_top_r21.setPivot(-21, -13, 0);
		window_exterior_1_13.addChild(window_top_r21);
		setRotationAngle(window_top_r21, 0, 0, 0.1396F);
		window_top_r21.setTextureUVOffset(100, 128).addCuboid(0, -21, -12.5F, 0, 21, 25, 0, false);

		window_exterior_1_14 = createModelPart();
		window_exterior_1_14.setPivot(0, 0, 0);
		window_exterior_7.addChild(window_exterior_1_14);
		window_exterior_1_14.setTextureUVOffset(206, 245).addCuboid(21, -13, -12.5F, 0, 6, 25, 0, true);

		window_bottom_r15 = createModelPart();
		window_bottom_r15.setPivot(21, -7, 0);
		window_exterior_1_14.addChild(window_bottom_r15);
		setRotationAngle(window_bottom_r15, 0, 0, 0.2094F);
		window_bottom_r15.setTextureUVOffset(181, 231).addCuboid(-1, 0, -12.5F, 1, 8, 25, 0, true);

		window_top_r22 = createModelPart();
		window_top_r22.setPivot(21, -13, 0);
		window_exterior_1_14.addChild(window_top_r22);
		setRotationAngle(window_top_r22, 0, 0, -0.1396F);
		window_top_r22.setTextureUVOffset(50, 128).addCuboid(0, -21, -12.5F, 0, 21, 25, 0, true);

		window_exterior_8 = createModelPart();
		window_exterior_8.setPivot(0, 24, 0);


		window_exterior_1_15 = createModelPart();
		window_exterior_1_15.setPivot(0, 0, 0);
		window_exterior_8.addChild(window_exterior_1_15);
		window_exterior_1_15.setTextureUVOffset(27, 245).addCuboid(-21, -13, -12.5F, 0, 6, 25, 0, false);

		window_bottom_r16 = createModelPart();
		window_bottom_r16.setPivot(-21, -7, 0);
		window_exterior_1_15.addChild(window_bottom_r16);
		setRotationAngle(window_bottom_r16, 0, 0, -0.2094F);
		window_bottom_r16.setTextureUVOffset(0, 230).addCuboid(0, 0, -12.5F, 1, 8, 25, 0, false);

		window_top_r23 = createModelPart();
		window_top_r23.setPivot(-21, -13, 0);
		window_exterior_1_15.addChild(window_top_r23);
		setRotationAngle(window_top_r23, 0, 0, 0.1396F);
		window_top_r23.setTextureUVOffset(126, 24).addCuboid(0, -21, -12.5F, 0, 21, 25, 0, false);

		window_exterior_1_16 = createModelPart();
		window_exterior_1_16.setPivot(0, 0, 0);
		window_exterior_8.addChild(window_exterior_1_16);
		window_exterior_1_16.setTextureUVOffset(206, 239).addCuboid(21, -13, -12.5F, 0, 6, 25, 0, true);

		window_bottom_r17 = createModelPart();
		window_bottom_r17.setPivot(21, -7, 0);
		window_exterior_1_16.addChild(window_bottom_r17);
		setRotationAngle(window_bottom_r17, 0, 0, 0.2094F);
		window_bottom_r17.setTextureUVOffset(226, 21).addCuboid(-1, 0, -12.5F, 1, 8, 25, 0, true);

		window_top_r24 = createModelPart();
		window_top_r24.setPivot(21, -13, 0);
		window_exterior_1_16.addChild(window_top_r24);
		setRotationAngle(window_top_r24, 0, 0, -0.1396F);
		window_top_r24.setTextureUVOffset(0, 121).addCuboid(0, -21, -12.5F, 0, 21, 25, 0, true);

		window_end_exterior_1 = createModelPart();
		window_end_exterior_1.setPivot(0, 24, 0);


		window_end_exterior_1_1 = createModelPart();
		window_end_exterior_1_1.setPivot(0, 0, 0);
		window_end_exterior_1.addChild(window_end_exterior_1_1);
		window_end_exterior_1_1.setTextureUVOffset(286, 77).addCuboid(-21, -13, -12.5F, 0, 6, 25, 0, false);

		window_bottom_r18 = createModelPart();
		window_bottom_r18.setPivot(-21, -7, 0);
		window_end_exterior_1_1.addChild(window_bottom_r18);
		setRotationAngle(window_bottom_r18, 0, 0, -0.2094F);
		window_bottom_r18.setTextureUVOffset(106, 269).addCuboid(0, 0, -12.5F, 1, 8, 25, 0, false);

		window_top_r25 = createModelPart();
		window_top_r25.setPivot(-21, -13, 0);
		window_end_exterior_1_1.addChild(window_top_r25);
		setRotationAngle(window_top_r25, 0, 0, 0.1396F);
		window_top_r25.setTextureUVOffset(50, 191).addCuboid(0, -21, -12.5F, 0, 21, 25, 0, false);

		window_end_exterior_1_2 = createModelPart();
		window_end_exterior_1_2.setPivot(0, 0, 0);
		window_end_exterior_1.addChild(window_end_exterior_1_2);
		window_end_exterior_1_2.setTextureUVOffset(286, 71).addCuboid(21, -13, -12.5F, 0, 6, 25, 0, true);

		window_bottom_r19 = createModelPart();
		window_bottom_r19.setPivot(21, -7, 0);
		window_end_exterior_1_2.addChild(window_bottom_r19);
		setRotationAngle(window_bottom_r19, 0, 0, 0.2094F);
		window_bottom_r19.setTextureUVOffset(268, 162).addCuboid(-1, 0, -12.5F, 1, 8, 25, 0, true);

		window_top_r26 = createModelPart();
		window_top_r26.setPivot(21, -13, 0);
		window_end_exterior_1_2.addChild(window_top_r26);
		setRotationAngle(window_top_r26, 0, 0, -0.1396F);
		window_top_r26.setTextureUVOffset(188, 0).addCuboid(0, -21, -12.5F, 0, 21, 25, 0, true);

		window_end_exterior_2 = createModelPart();
		window_end_exterior_2.setPivot(0, 24, 0);


		window_end_exterior_1_3 = createModelPart();
		window_end_exterior_1_3.setPivot(0, 0, 0);
		window_end_exterior_2.addChild(window_end_exterior_1_3);
		window_end_exterior_1_3.setTextureUVOffset(88, 285).addCuboid(-21, -13, -12.5F, 0, 6, 25, 0, false);

		window_bottom_r20 = createModelPart();
		window_bottom_r20.setPivot(-21, -7, 0);
		window_end_exterior_1_3.addChild(window_bottom_r20);
		setRotationAngle(window_bottom_r20, 0, 0, -0.2094F);
		window_bottom_r20.setTextureUVOffset(265, 129).addCuboid(0, 0, -12.5F, 1, 8, 25, 0, false);

		window_top_r27 = createModelPart();
		window_top_r27.setPivot(-21, -13, 0);
		window_end_exterior_1_3.addChild(window_top_r27);
		setRotationAngle(window_top_r27, 0, 0, 0.1396F);
		window_top_r27.setTextureUVOffset(0, 184).addCuboid(0, -21, -12.5F, 0, 21, 25, 0, false);

		window_end_exterior_1_4 = createModelPart();
		window_end_exterior_1_4.setPivot(0, 0, 0);
		window_end_exterior_2.addChild(window_end_exterior_1_4);
		window_end_exterior_1_4.setTextureUVOffset(188, 284).addCuboid(21, -13, -12.5F, 0, 6, 25, 0, true);

		window_bottom_r21 = createModelPart();
		window_bottom_r21.setPivot(21, -7, 0);
		window_end_exterior_1_4.addChild(window_bottom_r21);
		setRotationAngle(window_bottom_r21, 0, 0, 0.2094F);
		window_bottom_r21.setTextureUVOffset(231, 264).addCuboid(-1, 0, -12.5F, 1, 8, 25, 0, true);

		window_top_r28 = createModelPart();
		window_top_r28.setPivot(21, -13, 0);
		window_end_exterior_1_4.addChild(window_top_r28);
		setRotationAngle(window_top_r28, 0, 0, -0.1396F);
		window_top_r28.setTextureUVOffset(150, 179).addCuboid(0, -21, -12.5F, 0, 21, 25, 0, true);

		window_end_exterior_3 = createModelPart();
		window_end_exterior_3.setPivot(0, 24, 0);


		window_end_exterior_1_5 = createModelPart();
		window_end_exterior_1_5.setPivot(0, 0, 0);
		window_end_exterior_3.addChild(window_end_exterior_1_5);
		window_end_exterior_1_5.setTextureUVOffset(138, 284).addCuboid(-21, -13, -12.5F, 0, 6, 25, 0, false);

		window_bottom_r22 = createModelPart();
		window_bottom_r22.setPivot(-21, -7, 0);
		window_end_exterior_1_5.addChild(window_bottom_r22);
		setRotationAngle(window_bottom_r22, 0, 0, -0.2094F);
		window_bottom_r22.setTextureUVOffset(179, 264).addCuboid(0, 0, -12.5F, 1, 8, 25, 0, false);

		window_top_r29 = createModelPart();
		window_top_r29.setPivot(-21, -13, 0);
		window_end_exterior_1_5.addChild(window_top_r29);
		setRotationAngle(window_top_r29, 0, 0, 0.1396F);
		window_top_r29.setTextureUVOffset(176, 21).addCuboid(0, -21, -12.5F, 0, 21, 25, 0, false);

		window_end_exterior_1_6 = createModelPart();
		window_end_exterior_1_6.setPivot(0, 0, 0);
		window_end_exterior_3.addChild(window_end_exterior_1_6);
		window_end_exterior_1_6.setTextureUVOffset(38, 284).addCuboid(21, -13, -12.5F, 0, 6, 25, 0, true);

		window_bottom_r23 = createModelPart();
		window_bottom_r23.setPivot(21, -7, 0);
		window_end_exterior_1_6.addChild(window_bottom_r23);
		setRotationAngle(window_bottom_r23, 0, 0, 0.2094F);
		window_bottom_r23.setTextureUVOffset(0, 263).addCuboid(-1, 0, -12.5F, 1, 8, 25, 0, true);

		window_top_r30 = createModelPart();
		window_top_r30.setPivot(21, -13, 0);
		window_end_exterior_1_6.addChild(window_top_r30);
		setRotationAngle(window_top_r30, 0, 0, -0.1396F);
		window_top_r30.setTextureUVOffset(100, 170).addCuboid(0, -21, -12.5F, 0, 21, 25, 0, true);

		window_end_exterior_4 = createModelPart();
		window_end_exterior_4.setPivot(0, 24, 0);


		window_end_exterior_1_7 = createModelPart();
		window_end_exterior_1_7.setPivot(0, 0, 0);
		window_end_exterior_4.addChild(window_end_exterior_1_7);
		window_end_exterior_1_7.setTextureUVOffset(88, 279).addCuboid(-21, -13, -12.5F, 0, 6, 25, 0, false);

		window_bottom_r24 = createModelPart();
		window_bottom_r24.setPivot(-21, -7, 0);
		window_end_exterior_1_7.addChild(window_bottom_r24);
		setRotationAngle(window_bottom_r24, 0, 0, -0.2094F);
		window_bottom_r24.setTextureUVOffset(260, 249).addCuboid(0, 0, -12.5F, 1, 8, 25, 0, false);

		window_top_r31 = createModelPart();
		window_top_r31.setPivot(-21, -13, 0);
		window_end_exterior_1_7.addChild(window_top_r31);
		setRotationAngle(window_top_r31, 0, 0, 0.1396F);
		window_top_r31.setTextureUVOffset(50, 170).addCuboid(0, -21, -12.5F, 0, 21, 25, 0, false);

		window_end_exterior_1_8 = createModelPart();
		window_end_exterior_1_8.setPivot(0, 0, 0);
		window_end_exterior_4.addChild(window_end_exterior_1_8);
		window_end_exterior_1_8.setTextureUVOffset(187, 278).addCuboid(21, -13, -12.5F, 0, 6, 25, 0, true);

		window_bottom_r25 = createModelPart();
		window_bottom_r25.setPivot(21, -7, 0);
		window_end_exterior_1_8.addChild(window_bottom_r25);
		setRotationAngle(window_bottom_r25, 0, 0, 0.2094F);
		window_bottom_r25.setTextureUVOffset(260, 216).addCuboid(-1, 0, -12.5F, 1, 8, 25, 0, true);

		window_top_r32 = createModelPart();
		window_top_r32.setPivot(21, -13, 0);
		window_end_exterior_1_8.addChild(window_top_r32);
		setRotationAngle(window_top_r32, 0, 0, -0.1396F);
		window_top_r32.setTextureUVOffset(0, 163).addCuboid(0, -21, -12.5F, 0, 21, 25, 0, true);

		roof_exterior_1 = createModelPart();
		roof_exterior_1.setPivot(0, 24, 0);
		roof_exterior_1.setTextureUVOffset(193, 88).addCuboid(-3.8369F, -40.0424F, -12.5F, 4, 0, 25, 0, false);

		roof_3_r1 = createModelPart();
		roof_3_r1.setPivot(-7.2836F, -39.4346F, 0);
		roof_exterior_1.addChild(roof_3_r1);
		setRotationAngle(roof_3_r1, 0, 0, 1.3963F);
		roof_3_r1.setTextureUVOffset(208, 214).addCuboid(0, -3.5F, -12.5F, 0, 7, 25, 0, false);

		roof_2_r1 = createModelPart();
		roof_2_r1.setPivot(-13.7612F, -37.077F, 0);
		roof_exterior_1.addChild(roof_2_r1);
		setRotationAngle(roof_2_r1, 0, 0, 1.0472F);
		roof_2_r1.setTextureUVOffset(27, 219).addCuboid(0, -3.5F, -12.5F, 0, 7, 25, 0, false);

		roof_1_r2 = createModelPart();
		roof_1_r2.setPivot(-16.792F, -35.3272F, 0);
		roof_exterior_1.addChild(roof_1_r2);
		setRotationAngle(roof_1_r2, 0, 0, 0.6981F);
		roof_1_r2.setTextureUVOffset(226, 31).addCuboid(0, 0, -12.5F, 0, 2, 25, 0, false);

		roof_exterior_door_edge = createModelPart();
		roof_exterior_door_edge.setPivot(0, 24, 0);


		roof_exterior_door_edge_1 = createModelPart();
		roof_exterior_door_edge_1.setPivot(0, 0, 0);
		roof_exterior_door_edge.addChild(roof_exterior_door_edge_1);
		roof_exterior_door_edge_1.setTextureUVOffset(191, 142).addCuboid(-3.8369F, -40.0424F, -12.5F, 4, 0, 25, 0, false);

		roof_4_r1 = createModelPart();
		roof_4_r1.setPivot(-7.2836F, -39.4346F, 0);
		roof_exterior_door_edge_1.addChild(roof_4_r1);
		setRotationAngle(roof_4_r1, 0, 0, 1.3963F);
		roof_4_r1.setTextureUVOffset(27, 212).addCuboid(0, -3.5F, -12.5F, 0, 7, 25, 0, false);

		roof_3_r2 = createModelPart();
		roof_3_r2.setPivot(-13.7612F, -37.077F, 0);
		roof_exterior_door_edge_1.addChild(roof_3_r2);
		setRotationAngle(roof_3_r2, 0, 0, 1.0472F);
		roof_3_r2.setTextureUVOffset(79, 212).addCuboid(0, -3.5F, -12.5F, 0, 7, 25, 0, false);

		roof_2_r2 = createModelPart();
		roof_2_r2.setPivot(-16.792F, -35.3272F, 0);
		roof_exterior_door_edge_1.addChild(roof_2_r2);
		setRotationAngle(roof_2_r2, 0, 0, 0.6981F);
		roof_2_r2.setTextureUVOffset(226, 29).addCuboid(0, 0, -12.5F, 0, 2, 25, 0, false);

		roof_exterior_door_edge_2 = createModelPart();
		roof_exterior_door_edge_2.setPivot(0, 0, 0);
		roof_exterior_door_edge.addChild(roof_exterior_door_edge_2);
		roof_exterior_door_edge_2.setTextureUVOffset(185, 88).addCuboid(-0.1631F, -40.0424F, -12.5F, 4, 0, 25, 0, true);

		roof_5_r1 = createModelPart();
		roof_5_r1.setPivot(7.2836F, -39.4346F, 0);
		roof_exterior_door_edge_2.addChild(roof_5_r1);
		setRotationAngle(roof_5_r1, 0, 0, -1.3963F);
		roof_5_r1.setTextureUVOffset(98, 74).addCuboid(0, -3.5F, -12.5F, 0, 7, 25, 0, true);

		roof_4_r2 = createModelPart();
		roof_4_r2.setPivot(13.7612F, -37.077F, 0);
		roof_exterior_door_edge_2.addChild(roof_4_r2);
		setRotationAngle(roof_4_r2, 0, 0, -1.0472F);
		roof_4_r2.setTextureUVOffset(152, 211).addCuboid(0, -3.5F, -12.5F, 0, 7, 25, 0, true);

		roof_3_r3 = createModelPart();
		roof_3_r3.setPivot(16.792F, -35.3272F, 0);
		roof_exterior_door_edge_2.addChild(roof_3_r3);
		setRotationAngle(roof_3_r3, 0, 0, -0.6981F);
		roof_3_r3.setTextureUVOffset(27, 226).addCuboid(0, 0, -12.5F, 0, 2, 25, 0, true);

		roof_exterior_door = createModelPart();
		roof_exterior_door.setPivot(0, 24, 0);


		roof_exterior_door_1 = createModelPart();
		roof_exterior_door_1.setPivot(0, 0, 0);
		roof_exterior_door.addChild(roof_exterior_door_1);
		roof_exterior_door_1.setTextureUVOffset(172, 144).addCuboid(-3.8369F, -40.0424F, -14, 4, 0, 28, 0, false);

		roof_5_r2 = createModelPart();
		roof_5_r2.setPivot(-7.2836F, -39.4346F, 0);
		roof_exterior_door_1.addChild(roof_5_r2);
		setRotationAngle(roof_5_r2, 0, 0, 1.3963F);
		roof_5_r2.setTextureUVOffset(126, 42).addCuboid(0, -3.5F, -14, 0, 7, 28, 0, false);

		roof_4_r3 = createModelPart();
		roof_4_r3.setPivot(-13.7612F, -37.077F, 0);
		roof_exterior_door_1.addChild(roof_4_r3);
		setRotationAngle(roof_4_r3, 0, 0, 1.0472F);
		roof_4_r3.setTextureUVOffset(150, 199).addCuboid(0, -3.5F, -14, 0, 7, 28, 0, false);

		roof_3_r4 = createModelPart();
		roof_3_r4.setPivot(-16.792F, -35.3272F, 0);
		roof_exterior_door_1.addChild(roof_3_r4);
		setRotationAngle(roof_3_r4, 0, 0, 0.6981F);
		roof_3_r4.setTextureUVOffset(150, 206).addCuboid(0, 0, -14, 0, 2, 28, 0, false);

		roof_exterior_door_2 = createModelPart();
		roof_exterior_door_2.setPivot(0, 0, 0);
		roof_exterior_door.addChild(roof_exterior_door_2);
		roof_exterior_door_2.setTextureUVOffset(246, 297).addCuboid(-0.1631F, -40.0424F, -14, 4, 0, 28, 0, true);

		roof_6_r1 = createModelPart();
		roof_6_r1.setPivot(7.2836F, -39.4346F, 0);
		roof_exterior_door_2.addChild(roof_6_r1);
		setRotationAngle(roof_6_r1, 0, 0, -1.3963F);
		roof_6_r1.setTextureUVOffset(208, 322).addCuboid(0, -3.5F, -14, 0, 7, 28, 0, true);

		roof_5_r3 = createModelPart();
		roof_5_r3.setPivot(13.7612F, -37.077F, 0);
		roof_exterior_door_2.addChild(roof_5_r3);
		setRotationAngle(roof_5_r3, 0, 0, -1.0472F);
		roof_5_r3.setTextureUVOffset(208, 313).addCuboid(0, -3.5F, -14, 0, 7, 28, 0, true);

		roof_4_r4 = createModelPart();
		roof_4_r4.setPivot(16.792F, -35.3272F, 0);
		roof_exterior_door_2.addChild(roof_4_r4);
		setRotationAngle(roof_4_r4, 0, 0, -0.6981F);
		roof_4_r4.setTextureUVOffset(208, 320).addCuboid(0, 0, -14, 0, 2, 28, 0, true);

		door = createModelPart();
		door.setPivot(0, 24, 0);
		door.setTextureUVOffset(60, 49).addCuboid(-19, 0, -14, 19, 1, 28, 0, false);
		door.setTextureUVOffset(275, 33).addCuboid(-20, -13, 11, 1, 6, 1, 0, false);
		door.setTextureUVOffset(279, 33).addCuboid(-20, -13, -12, 1, 6, 1, 0, false);
		door.setTextureUVOffset(82, 316).addCuboid(-20, -32, 11.05F, 6, 32, 3, 0, false);
		door.setTextureUVOffset(190, 315).addCuboid(-20, -32, -14.05F, 6, 32, 3, 0, false);
		door.setTextureUVOffset(60, 78).addCuboid(-18, -37, -14, 5, 5, 28, 0, false);
		door.setTextureUVOffset(66, 315).addCuboid(-14, -37, 14, 8, 37, 0, 0, false);
		door.setTextureUVOffset(258, 297).addCuboid(-14, -37, -14, 8, 37, 0, 0, false);
		door.setTextureUVOffset(172, 88).addCuboid(-5, -35, -14, 5, 0, 28, 0, false);

		light_edge_r2 = createModelPart();
		light_edge_r2.setPivot(-5, -35, 0);
		door.addChild(light_edge_r2);
		setRotationAngle(light_edge_r2, 0, 0, 0.2182F);
		light_edge_r2.setTextureUVOffset(206, 202).addCuboid(-5, 0, -14, 1, 1, 28, 0, false);
		light_edge_r2.setTextureUVOffset(60, 0).addCuboid(-9, 0, -14, 9, 0, 28, 0, false);

		door_top_3_r1 = createModelPart();
		door_top_3_r1.setPivot(-11.7395F, -35.2093F, 0);
		door.addChild(door_top_3_r1);
		setRotationAngle(door_top_3_r1, 0, 0, -1.3963F);
		door_top_3_r1.setTextureUVOffset(172, 197).addCuboid(-1.5F, -2, -14, 3, 2, 28, 0, false);

		door_top_2_r1 = createModelPart();
		door_top_2_r1.setPivot(-13, -32, 0);
		door.addChild(door_top_2_r1);
		setRotationAngle(door_top_2_r1, 0, 0, -1.0472F);
		door_top_2_r1.setTextureUVOffset(200, 144).addCuboid(0, -1, -14, 2, 1, 28, 0, false);

		window_top_2_r1 = createModelPart();
		window_top_2_r1.setPivot(-21, -13, 12);
		door.addChild(window_top_2_r1);
		setRotationAngle(window_top_2_r1, 0, 0, 0.1396F);
		window_top_2_r1.setTextureUVOffset(287, 33).addCuboid(1, -21, -24, 1, 21, 1, 0, false);
		window_top_2_r1.setTextureUVOffset(283, 33).addCuboid(1, -21, -1, 1, 21, 1, 0, false);

		window_bottom_2_r1 = createModelPart();
		window_bottom_2_r1.setPivot(-21, -7, 12);
		door.addChild(window_bottom_2_r1);
		setRotationAngle(window_bottom_2_r1, 0, 0, -0.2094F);
		window_bottom_2_r1.setTextureUVOffset(271, 33).addCuboid(1, 0, -24, 1, 8, 1, 0, false);
		window_bottom_2_r1.setTextureUVOffset(267, 33).addCuboid(1, 0, -1, 1, 8, 1, 0, false);

		door_left = createModelPart();
		door_left.setPivot(0, 0, 0);
		door.addChild(door_left);
		door_left.setTextureUVOffset(222, 7).addCuboid(-20, -13, 0, 0, 6, 12, 0, false);

		door_top_r1 = createModelPart();
		door_top_r1.setPivot(-21, -13, 12);
		door_left.addChild(door_top_r1);
		setRotationAngle(door_top_r1, 0, 0, 0.1396F);
		door_top_r1.setTextureUVOffset(206, 190).addCuboid(1, -21, -12, 0, 21, 12, 0, false);

		door_bottom_r1 = createModelPart();
		door_bottom_r1.setPivot(-21, -7, 12);
		door_left.addChild(door_bottom_r1);
		setRotationAngle(door_bottom_r1, 0, 0, -0.2094F);
		door_bottom_r1.setTextureUVOffset(0, 226).addCuboid(1, 0, -12, 0, 8, 12, 0, false);

		door_right = createModelPart();
		door_right.setPivot(0, 0, 0);
		door.addChild(door_right);
		door_right.setTextureUVOffset(158, 0).addCuboid(-20, -13, -12, 0, 6, 12, 0, false);

		door_top_r2 = createModelPart();
		door_top_r2.setPivot(-21, -13, 12);
		door_right.addChild(door_top_r2);
		setRotationAngle(door_top_r2, 0, 0, 0.1396F);
		door_top_r2.setTextureUVOffset(0, 65).addCuboid(1, -21, -24, 0, 21, 12, 0, false);

		door_bottom_r2 = createModelPart();
		door_bottom_r2.setPivot(-21, -7, 12);
		door_right.addChild(door_bottom_r2);
		setRotationAngle(door_bottom_r2, 0, 0, -0.2094F);
		door_bottom_r2.setTextureUVOffset(0, 218).addCuboid(1, 0, -24, 0, 8, 12, 0, false);

		door_exterior = createModelPart();
		door_exterior.setPivot(0, 24, 0);
		door_exterior.setTextureUVOffset(226, 88).addCuboid(-21, -13, 12, 1, 6, 2, 0, false);
		door_exterior.setTextureUVOffset(190, 67).addCuboid(-21, -13, -14, 1, 6, 2, 0, false);
		door_exterior.setTextureUVOffset(280, 0).addCuboid(-21, 0, -12, 2, 1, 24, 0, false);

		window_top_2_r2 = createModelPart();
		window_top_2_r2.setPivot(-21, -13, 12);
		door_exterior.addChild(window_top_2_r2);
		setRotationAngle(window_top_2_r2, 0, 0, 0.1396F);
		window_top_2_r2.setTextureUVOffset(81, 78).addCuboid(0, -21, -26, 1, 21, 2, 0, false);
		window_top_2_r2.setTextureUVOffset(200, 173).addCuboid(0, -21, 0, 1, 21, 2, 0, false);

		window_bottom_2_r2 = createModelPart();
		window_bottom_2_r2.setPivot(-21, -7, 12);
		door_exterior.addChild(window_bottom_2_r2);
		setRotationAngle(window_bottom_2_r2, 0, 0, -0.2094F);
		window_bottom_2_r2.setTextureUVOffset(182, 7).addCuboid(0, 0, -26, 1, 8, 2, 0, false);
		window_bottom_2_r2.setTextureUVOffset(188, 7).addCuboid(0, 0, 0, 1, 8, 2, 0, false);

		door_left_exterior = createModelPart();
		door_left_exterior.setPivot(0, 0, 0);
		door_exterior.addChild(door_left_exterior);
		door_left_exterior.setTextureUVOffset(245, 96).addCuboid(-21, -13, 0, 1, 6, 12, 0, false);

		door_top_r3 = createModelPart();
		door_top_r3.setPivot(-21, -13, 12);
		door_left_exterior.addChild(door_top_r3);
		setRotationAngle(door_top_r3, 0, 0, 0.1396F);
		door_top_r3.setTextureUVOffset(305, 150).addCuboid(0, -21, -12, 1, 21, 12, 0, false);

		door_bottom_r3 = createModelPart();
		door_bottom_r3.setPivot(-21, -7, 12);
		door_left_exterior.addChild(door_bottom_r3);
		setRotationAngle(door_bottom_r3, 0, 0, -0.2094F);
		door_bottom_r3.setTextureUVOffset(117, 237).addCuboid(0, 0, -12, 1, 8, 12, 0, false);

		door_right_exterior = createModelPart();
		door_right_exterior.setPivot(0, 0, 0);
		door_exterior.addChild(door_right_exterior);
		door_right_exterior.setTextureUVOffset(238, 183).addCuboid(-21, -13, -12, 1, 6, 12, 0, false);

		door_top_r4 = createModelPart();
		door_top_r4.setPivot(-21, -13, 12);
		door_right_exterior.addChild(door_top_r4);
		setRotationAngle(door_top_r4, 0, 0, 0.1396F);
		door_top_r4.setTextureUVOffset(299, 25).addCuboid(0, -21, -24, 1, 21, 12, 0, false);

		door_bottom_r4 = createModelPart();
		door_bottom_r4.setPivot(-21, -7, 12);
		door_right_exterior.addChild(door_bottom_r4);
		setRotationAngle(door_bottom_r4, 0, 0, -0.2094F);
		door_bottom_r4.setTextureUVOffset(232, 150).addCuboid(0, 0, -24, 1, 8, 12, 0, false);

		door_exterior_end = createModelPart();
		door_exterior_end.setPivot(0, 24, 0);


		door_exterior_end_1 = createModelPart();
		door_exterior_end_1.setPivot(0, 0, 0);
		door_exterior_end.addChild(door_exterior_end_1);
		door_exterior_end_1.setTextureUVOffset(226, 96).addCuboid(-21, -13, 12, 1, 6, 2, 0, false);
		door_exterior_end_1.setTextureUVOffset(226, 104).addCuboid(-21, -13, -14, 1, 6, 2, 0, false);
		door_exterior_end_1.setTextureUVOffset(313, 1).addCuboid(-21, 0, -12, 2, 1, 24, 0, false);

		window_top_2_r3 = createModelPart();
		window_top_2_r3.setPivot(-21, -13, 12);
		door_exterior_end_1.addChild(window_top_2_r3);
		setRotationAngle(window_top_2_r3, 0, 0, 0.1396F);
		window_top_2_r3.setTextureUVOffset(298, 0).addCuboid(0, -21, -26, 1, 21, 2, 0, false);
		window_top_2_r3.setTextureUVOffset(292, 0).addCuboid(0, -21, 0, 1, 21, 2, 0, false);

		window_bottom_2_r3 = createModelPart();
		window_bottom_2_r3.setPivot(-21, -7, 12);
		door_exterior_end_1.addChild(window_bottom_2_r3);
		setRotationAngle(window_bottom_2_r3, 0, 0, -0.2094F);
		window_bottom_2_r3.setTextureUVOffset(280, 0).addCuboid(0, 0, -26, 1, 8, 2, 0, false);
		window_bottom_2_r3.setTextureUVOffset(286, 0).addCuboid(0, 0, 0, 1, 8, 2, 0, false);

		door_left_exterior_end_1 = createModelPart();
		door_left_exterior_end_1.setPivot(0, 0, 0);
		door_exterior_end.addChild(door_left_exterior_end_1);
		door_left_exterior_end_1.setTextureUVOffset(339, 46).addCuboid(-21, -13, 0, 1, 6, 12, 0, false);

		door_top_r5 = createModelPart();
		door_top_r5.setPivot(-21, -13, 12);
		door_left_exterior_end_1.addChild(door_top_r5);
		setRotationAngle(door_top_r5, 0, 0, 0.1396F);
		door_top_r5.setTextureUVOffset(325, 25).addCuboid(0, -21, -12, 1, 21, 12, 0, false);

		door_bottom_r5 = createModelPart();
		door_bottom_r5.setPivot(-21, -7, 12);
		door_left_exterior_end_1.addChild(door_bottom_r5);
		setRotationAngle(door_bottom_r5, 0, 0, -0.2094F);
		door_bottom_r5.setTextureUVOffset(112, 316).addCuboid(0, 0, -12, 1, 8, 12, 0, false);

		door_right_exterior_end_1 = createModelPart();
		door_right_exterior_end_1.setPivot(0, 0, 0);
		door_exterior_end.addChild(door_right_exterior_end_1);
		door_right_exterior_end_1.setTextureUVOffset(339, 79).addCuboid(-21, -13, -12, 1, 6, 12, 0, false);

		door_top_r6 = createModelPart();
		door_top_r6.setPivot(-21, -13, 12);
		door_right_exterior_end_1.addChild(door_top_r6);
		setRotationAngle(door_top_r6, 0, 0, 0.1396F);
		door_top_r6.setTextureUVOffset(325, 58).addCuboid(0, -21, -24, 1, 21, 12, 0, false);

		door_bottom_r6 = createModelPart();
		door_bottom_r6.setPivot(-21, -7, 12);
		door_right_exterior_end_1.addChild(door_bottom_r6);
		setRotationAngle(door_bottom_r6, 0, 0, -0.2094F);
		door_bottom_r6.setTextureUVOffset(112, 336).addCuboid(0, 0, -24, 1, 8, 12, 0, false);

		door_exterior_end_2 = createModelPart();
		door_exterior_end_2.setPivot(0, 0, 0);
		door_exterior_end.addChild(door_exterior_end_2);
		door_exterior_end_2.setTextureUVOffset(226, 96).addCuboid(20, -13, 12, 1, 6, 2, 0, true);
		door_exterior_end_2.setTextureUVOffset(226, 104).addCuboid(20, -13, -14, 1, 6, 2, 0, true);
		door_exterior_end_2.setTextureUVOffset(313, 1).addCuboid(19, 0, -12, 2, 1, 24, 0, true);

		window_top_2_r4 = createModelPart();
		window_top_2_r4.setPivot(21, -13, 12);
		door_exterior_end_2.addChild(window_top_2_r4);
		setRotationAngle(window_top_2_r4, 0, 0, -0.1396F);
		window_top_2_r4.setTextureUVOffset(298, 0).addCuboid(-1, -21, -26, 1, 21, 2, 0, true);
		window_top_2_r4.setTextureUVOffset(292, 0).addCuboid(-1, -21, 0, 1, 21, 2, 0, true);

		window_bottom_2_r4 = createModelPart();
		window_bottom_2_r4.setPivot(21, -7, 12);
		door_exterior_end_2.addChild(window_bottom_2_r4);
		setRotationAngle(window_bottom_2_r4, 0, 0, 0.2094F);
		window_bottom_2_r4.setTextureUVOffset(280, 0).addCuboid(-1, 0, -26, 1, 8, 2, 0, true);
		window_bottom_2_r4.setTextureUVOffset(286, 0).addCuboid(-1, 0, 0, 1, 8, 2, 0, true);

		door_left_exterior_end_2 = createModelPart();
		door_left_exterior_end_2.setPivot(0, 0, 0);
		door_exterior_end.addChild(door_left_exterior_end_2);
		door_left_exterior_end_2.setTextureUVOffset(339, 46).addCuboid(20, -13, 0, 1, 6, 12, 0, true);

		door_top_r7 = createModelPart();
		door_top_r7.setPivot(21, -13, 12);
		door_left_exterior_end_2.addChild(door_top_r7);
		setRotationAngle(door_top_r7, 0, 0, -0.1396F);
		door_top_r7.setTextureUVOffset(325, 25).addCuboid(-1, -21, -12, 1, 21, 12, 0, true);

		door_bottom_r7 = createModelPart();
		door_bottom_r7.setPivot(21, -7, 12);
		door_left_exterior_end_2.addChild(door_bottom_r7);
		setRotationAngle(door_bottom_r7, 0, 0, 0.2094F);
		door_bottom_r7.setTextureUVOffset(112, 316).addCuboid(-1, 0, -12, 1, 8, 12, 0, true);

		door_right_exterior_end_2 = createModelPart();
		door_right_exterior_end_2.setPivot(0, 0, 0);
		door_exterior_end.addChild(door_right_exterior_end_2);
		door_right_exterior_end_2.setTextureUVOffset(339, 79).addCuboid(20, -13, -12, 1, 6, 12, 0, true);

		door_top_r8 = createModelPart();
		door_top_r8.setPivot(21, -13, 12);
		door_right_exterior_end_2.addChild(door_top_r8);
		setRotationAngle(door_top_r8, 0, 0, -0.1396F);
		door_top_r8.setTextureUVOffset(325, 58).addCuboid(-1, -21, -24, 1, 21, 12, 0, true);

		door_bottom_r8 = createModelPart();
		door_bottom_r8.setPivot(21, -7, 12);
		door_right_exterior_end_2.addChild(door_bottom_r8);
		setRotationAngle(door_bottom_r8, 0, 0, 0.2094F);
		door_bottom_r8.setTextureUVOffset(112, 336).addCuboid(-1, 0, -24, 1, 8, 12, 0, true);

		end = createModelPart();
		end.setPivot(0, 24, 0);
		end.setTextureUVOffset(208, 331).addCuboid(-7, -36, -12.5F, 14, 4, 6, 0, false);
		end.setTextureUVOffset(106, 0).addCuboid(-19, 0, -12.5F, 38, 1, 6, 0, false);

		end_1 = createModelPart();
		end_1.setPivot(0, 0, 0);
		end.addChild(end_1);
		end_1.setTextureUVOffset(0, 296).addCuboid(-20, -37, -12.5F, 13, 37, 6, 0, false);

		end_2 = createModelPart();
		end_2.setPivot(0, 0, 0);
		end.addChild(end_2);
		end_2.setTextureUVOffset(293, 195).addCuboid(7, -37, -12.5F, 13, 37, 6, 0, true);

		end_exterior = createModelPart();
		end_exterior.setPivot(0, 24, 0);
		end_exterior.setTextureUVOffset(287, 266).addCuboid(-7, -40, -6.5F, 14, 8, 0, 0, false);

		end_exterior_1 = createModelPart();
		end_exterior_1.setPivot(0, 0, 0);
		end_exterior.addChild(end_exterior_1);
		end_exterior_1.setTextureUVOffset(246, 150).addCuboid(-21, -13, -12.5F, 1, 6, 6, 0, false);
		end_exterior_1.setTextureUVOffset(164, 315).addCuboid(-20, -39, -6.5F, 13, 39, 0, 0, false);

		window_bottom_r26 = createModelPart();
		window_bottom_r26.setPivot(-21, -7, 0);
		end_exterior_1.addChild(window_bottom_r26);
		setRotationAngle(window_bottom_r26, 0, 0, -0.2094F);
		window_bottom_r26.setTextureUVOffset(106, 251).addCuboid(0, 0, -12.5F, 1, 8, 6, 0, false);

		window_top_r33 = createModelPart();
		window_top_r33.setPivot(-21, -13, 0);
		end_exterior_1.addChild(window_top_r33);
		setRotationAngle(window_top_r33, 0, 0, 0.1396F);
		window_top_r33.setTextureUVOffset(256, 59).addCuboid(0, -21, -12.5F, 1, 21, 6, 0, false);

		end_exterior_2 = createModelPart();
		end_exterior_2.setPivot(0, 0, 0);
		end_exterior.addChild(end_exterior_2);
		end_exterior_2.setTextureUVOffset(131, 237).addCuboid(20, -13, -12.5F, 1, 6, 6, 0, true);
		end_exterior_2.setTextureUVOffset(138, 315).addCuboid(7, -39, -6.5F, 13, 39, 0, 0, true);

		window_bottom_r27 = createModelPart();
		window_bottom_r27.setPivot(21, -7, 0);
		end_exterior_2.addChild(window_bottom_r27);
		setRotationAngle(window_bottom_r27, 0, 0, 0.2094F);
		window_bottom_r27.setTextureUVOffset(79, 244).addCuboid(-1, 0, -12.5F, 1, 8, 6, 0, true);

		window_top_r34 = createModelPart();
		window_top_r34.setPivot(21, -13, 0);
		end_exterior_2.addChild(window_top_r34);
		setRotationAngle(window_top_r34, 0, 0, -0.1396F);
		window_top_r34.setTextureUVOffset(220, 173).addCuboid(-1, -21, -12.5F, 1, 21, 6, 0, true);

		roof_end_exterior_1 = createModelPart();
		roof_end_exterior_1.setPivot(0, 0, 0);
		end_exterior.addChild(roof_end_exterior_1);
		roof_end_exterior_1.setTextureUVOffset(27, 230).addCuboid(-3.8369F, -40.0424F, -12.5F, 4, 1, 6, 0, false);

		roof_3_r5 = createModelPart();
		roof_3_r5.setPivot(-7.2836F, -39.4346F, 0);
		roof_end_exterior_1.addChild(roof_3_r5);
		setRotationAngle(roof_3_r5, 0, 0, 1.3963F);
		roof_3_r5.setTextureUVOffset(256, 11).addCuboid(0, -3.5F, -12.5F, 1, 7, 6, 0, false);

		roof_2_r3 = createModelPart();
		roof_2_r3.setPivot(-13.7612F, -37.077F, 0);
		roof_end_exterior_1.addChild(roof_2_r3);
		setRotationAngle(roof_2_r3, 0, 0, 1.0472F);
		roof_2_r3.setTextureUVOffset(258, 217).addCuboid(0, -3.5F, -12.5F, 1, 7, 6, 0, false);

		roof_1_r3 = createModelPart();
		roof_1_r3.setPivot(-16.792F, -35.3272F, 0);
		roof_end_exterior_1.addChild(roof_1_r3);
		setRotationAngle(roof_1_r3, 0, 0, 0.6981F);
		roof_1_r3.setTextureUVOffset(248, 80).addCuboid(0, 0, -12.5F, 1, 2, 6, 0, false);

		roof_end_exterior_2 = createModelPart();
		roof_end_exterior_2.setPivot(0, 0, 0);
		end_exterior.addChild(roof_end_exterior_2);
		roof_end_exterior_2.setTextureUVOffset(176, 71).addCuboid(-0.1631F, -40.0424F, -12.5F, 4, 1, 6, 0, true);

		roof_4_r5 = createModelPart();
		roof_4_r5.setPivot(7.2836F, -39.4346F, 0);
		roof_end_exterior_2.addChild(roof_4_r5);
		setRotationAngle(roof_4_r5, 0, 0, -1.3963F);
		roof_4_r5.setTextureUVOffset(179, 243).addCuboid(-1, -3.5F, -12.5F, 1, 7, 6, 0, true);

		roof_3_r6 = createModelPart();
		roof_3_r6.setPivot(13.7612F, -37.077F, 0);
		roof_end_exterior_2.addChild(roof_3_r6);
		setRotationAngle(roof_3_r6, 0, 0, -1.0472F);
		roof_3_r6.setTextureUVOffset(253, 33).addCuboid(-1, -3.5F, -12.5F, 1, 7, 6, 0, true);

		roof_2_r4 = createModelPart();
		roof_2_r4.setPivot(16.792F, -35.3272F, 0);
		roof_end_exterior_2.addChild(roof_2_r4);
		setRotationAngle(roof_2_r4, 0, 0, -0.6981F);
		roof_2_r4.setTextureUVOffset(0, 246).addCuboid(-1, 0, -12.5F, 1, 2, 6, 0, true);

		head = createModelPart();
		head.setPivot(0, 24, 0);
		head.setTextureUVOffset(0, 40).addCuboid(-22, -37, -25.5F, 44, 37, 0, 0, false);

		head_exterior = createModelPart();
		head_exterior.setPivot(0, 24, 0);
		head_exterior.setTextureUVOffset(283, 323).addCuboid(-7, -34, 2.5F, 14, 36, 6, 0, false);
		head_exterior.setTextureUVOffset(0, 0).addCuboid(-22, -40, -12, 44, 40, 0, 0, false);

		front_r1 = createModelPart();
		front_r1.setPivot(0, 2, 5.5F);
		head_exterior.addChild(front_r1);
		setRotationAngle(front_r1, 0.0698F, 0, 0);
		front_r1.setTextureUVOffset(283, 282).addCuboid(-21, -41, 0, 42, 41, 0, 0, false);

		head_exterior_1 = createModelPart();
		head_exterior_1.setPivot(0, 0, 0);
		head_exterior.addChild(head_exterior_1);
		head_exterior_1.setTextureUVOffset(150, 1).addCuboid(-21, -13, -12.5F, 0, 6, 18, 0, false);
		head_exterior_1.setTextureUVOffset(176, 0).addCuboid(-19, 0, -12.5F, 19, 1, 18, 0, false);

		front_bottom_r1 = createModelPart();
		front_bottom_r1.setPivot(0, 2, 5.5F);
		head_exterior_1.addChild(front_bottom_r1);
		setRotationAngle(front_bottom_r1, -0.5236F, 0, 0);
		front_bottom_r1.setTextureUVOffset(114, 7).addCuboid(-21, 0, -1, 21, 8, 1, 0, false);

		roof_5_r4 = createModelPart();
		roof_5_r4.setPivot(-1.8369F, -39.258F, -3.5343F);
		head_exterior_1.addChild(roof_5_r4);
		setRotationAngle(roof_5_r4, -0.0873F, 0, 0);
		roof_5_r4.setTextureUVOffset(88, 7).addCuboid(-2, 0, -9, 4, 0, 18, 0, false);

		roof_4_r6 = createModelPart();
		roof_4_r6.setPivot(-7.1474F, -38.6622F, -3.5343F);
		head_exterior_1.addChild(roof_4_r6);
		setRotationAngle(roof_4_r6, 0, 0.0873F, 1.3963F);
		roof_4_r6.setTextureUVOffset(114, 0).addCuboid(0, -3.5F, -9, 0, 7, 18, 0, false);

		roof_3_r7 = createModelPart();
		roof_3_r7.setPivot(-13.369F, -36.3977F, -3.5343F);
		head_exterior_1.addChild(roof_3_r7);
		setRotationAngle(roof_3_r7, 0, 0.0873F, 1.0472F);
		roof_3_r7.setTextureUVOffset(226, 40).addCuboid(0, -3.5F, -9, 0, 7, 18, 0, false);

		roof_2_r5 = createModelPart();
		roof_2_r5.setPivot(-17.7981F, -32.9079F, -3.5343F);
		head_exterior_1.addChild(roof_2_r5);
		setRotationAngle(roof_2_r5, 0, 0.0873F, 0.6981F);
		roof_2_r5.setTextureUVOffset(186, 1).addCuboid(0, -2.5F, -9, 0, 5, 18, 0, false);

		window_bottom_r28 = createModelPart();
		window_bottom_r28.setPivot(-21, -7, 0);
		head_exterior_1.addChild(window_bottom_r28);
		setRotationAngle(window_bottom_r28, 0, 0, -0.2094F);
		window_bottom_r28.setTextureUVOffset(220, 297).addCuboid(0, 0, -12.5F, 1, 16, 18, 0, false);

		window_top_r35 = createModelPart();
		window_top_r35.setPivot(-21, -13, 0);
		head_exterior_1.addChild(window_top_r35);
		setRotationAngle(window_top_r35, 0, 0, 0.1396F);
		window_top_r35.setTextureUVOffset(218, 99).addCuboid(0, -21, -12.5F, 0, 21, 18, 0, false);

		head_exterior_2 = createModelPart();
		head_exterior_2.setPivot(0, 0, 0);
		head_exterior.addChild(head_exterior_2);
		head_exterior_2.setTextureUVOffset(150, 1).addCuboid(21, -13, -12.5F, 0, 6, 18, 0, true);
		head_exterior_2.setTextureUVOffset(176, 0).addCuboid(0, 0, -12.5F, 19, 1, 18, 0, true);

		front_bottom_r2 = createModelPart();
		front_bottom_r2.setPivot(0, 2, 5.5F);
		head_exterior_2.addChild(front_bottom_r2);
		setRotationAngle(front_bottom_r2, -0.5236F, 0, 0);
		front_bottom_r2.setTextureUVOffset(114, 7).addCuboid(0, 0, -1, 21, 8, 1, 0, true);

		roof_6_r2 = createModelPart();
		roof_6_r2.setPivot(1.8369F, -39.258F, -3.5343F);
		head_exterior_2.addChild(roof_6_r2);
		setRotationAngle(roof_6_r2, -0.0873F, 0, 0);
		roof_6_r2.setTextureUVOffset(88, 7).addCuboid(-2, 0, -9, 4, 0, 18, 0, true);

		roof_5_r5 = createModelPart();
		roof_5_r5.setPivot(7.1474F, -38.6622F, -3.5343F);
		head_exterior_2.addChild(roof_5_r5);
		setRotationAngle(roof_5_r5, 0, -0.0873F, -1.3963F);
		roof_5_r5.setTextureUVOffset(114, 0).addCuboid(0, -3.5F, -9, 0, 7, 18, 0, true);

		roof_4_r7 = createModelPart();
		roof_4_r7.setPivot(13.369F, -36.3977F, -3.5343F);
		head_exterior_2.addChild(roof_4_r7);
		setRotationAngle(roof_4_r7, 0, -0.0873F, -1.0472F);
		roof_4_r7.setTextureUVOffset(226, 40).addCuboid(0, -3.5F, -9, 0, 7, 18, 0, true);

		roof_3_r8 = createModelPart();
		roof_3_r8.setPivot(17.7981F, -32.9079F, -3.5343F);
		head_exterior_2.addChild(roof_3_r8);
		setRotationAngle(roof_3_r8, 0, -0.0873F, -0.6981F);
		roof_3_r8.setTextureUVOffset(186, 1).addCuboid(0, -2.5F, -9, 0, 5, 18, 0, true);

		window_bottom_r29 = createModelPart();
		window_bottom_r29.setPivot(21, -7, 0);
		head_exterior_2.addChild(window_bottom_r29);
		setRotationAngle(window_bottom_r29, 0, 0, 0.2094F);
		window_bottom_r29.setTextureUVOffset(220, 297).addCuboid(-1, 0, -12.5F, 1, 16, 18, 0, true);

		window_top_r36 = createModelPart();
		window_top_r36.setPivot(21, -13, 0);
		head_exterior_2.addChild(window_top_r36);
		setRotationAngle(window_top_r36, 0, 0, -0.1396F);
		window_top_r36.setTextureUVOffset(218, 99).addCuboid(0, -21, -12.5F, 0, 21, 18, 0, true);

		seat = createModelPart();
		seat.setPivot(0, 24, 0);
		seat.setTextureUVOffset(232, 9).addCuboid(-4, -6, -4, 8, 1, 7, 0, false);
		seat.setTextureUVOffset(236, 202).addCuboid(-3.5F, -22.644F, 4.0686F, 7, 5, 1, 0, false);

		seat_2_r1 = createModelPart();
		seat_2_r1.setPivot(0, -6, 2);
		seat.addChild(seat_2_r1);
		setRotationAngle(seat_2_r1, -0.1745F, 0, 0);
		seat_2_r1.setTextureUVOffset(152, 243).addCuboid(-4, -12, 0, 8, 12, 1, 0, false);

		door_light_box = createModelPart();
		door_light_box.setPivot(0, 24, 0);


		door_light_r1 = createModelPart();
		door_light_r1.setPivot(-21, -13, 0);
		door_light_box.addChild(door_light_r1);
		setRotationAngle(door_light_r1, 0, 0, 0.1396F);
		door_light_r1.setTextureUVOffset(232, 0).addCuboid(-0.5F, -19, -1, 1, 2, 2, 0, false);

		door_light_off = createModelPart();
		door_light_off.setPivot(0, 24, 0);


		door_light_r2 = createModelPart();
		door_light_r2.setPivot(-21, -13, 0);
		door_light_off.addChild(door_light_r2);
		setRotationAngle(door_light_r2, 0, 0, 0.1396F);
		door_light_r2.setTextureUVOffset(238, 0).addCuboid(-0.5F, -19, -1, 1, 2, 2, 0, false);

		door_light_on = createModelPart();
		door_light_on.setPivot(0, 24, 0);


		door_light_r3 = createModelPart();
		door_light_r3.setPivot(-21, -13, 0);
		door_light_on.addChild(door_light_r3);
		setRotationAngle(door_light_r3, 0, 0, 0.1396F);
		door_light_r3.setTextureUVOffset(244, 0).addCuboid(-0.5F, -19, -1, 1, 2, 2, 0, false);

		headlights = createModelPart();
		headlights.setPivot(0, 24, 0);


		headlight_2_r1 = createModelPart();
		headlight_2_r1.setPivot(0, 2, 5.5F);
		headlights.addChild(headlight_2_r1);
		setRotationAngle(headlight_2_r1, 0.0698F, 0, 0);
		headlight_2_r1.setTextureUVOffset(250, 0).addCuboid(12, -11, 0.1F, 6, 7, 0, 0, true);
		headlight_2_r1.setTextureUVOffset(250, 0).addCuboid(-18, -11, 0.1F, 6, 7, 0, 0, false);

		tail_lights = createModelPart();
		tail_lights.setPivot(0, 24, 0);


		tail_light_2_r1 = createModelPart();
		tail_light_2_r1.setPivot(0, 2, 5.5F);
		tail_lights.addChild(tail_light_2_r1);
		setRotationAngle(tail_light_2_r1, 0.0698F, 0, 0);
		tail_light_2_r1.setTextureUVOffset(262, 0).addCuboid(9, -9, 0.1F, 4, 5, 0, 0, true);
		tail_light_2_r1.setTextureUVOffset(262, 0).addCuboid(-13, -9, 0.1F, 4, 5, 0, 0, false);

		buildModel();
	}

	private static final int DOOR_MAX = 11;

	@Override
	public ModelClass377 createNew(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		return new ModelClass377(doorAnimationType, renderDoorOverlay);
	}

	@Override
	protected void renderWindowPositions(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
		final float[] windowPositions = getNewWindowPositions();

		switch (renderStage) {
			case LIGHT:
				for (final float i : windowPositions) {
					renderMirror(window_light, graphicsHolder, light, i);
				}
				break;
			case INTERIOR:
				for (final float i : windowPositions) {
					renderMirror(window, graphicsHolder, light, i);
				}
				renderOnceFlipped(window_1, graphicsHolder, light, windowPositions[0]);
				renderOnceFlipped(window_2, graphicsHolder, light, windowPositions[1]);
				renderOnceFlipped(window_3, graphicsHolder, light, windowPositions[2]);
				renderOnceFlipped(window_4, graphicsHolder, light, windowPositions[3]);
				renderOnceFlipped(window_5, graphicsHolder, light, windowPositions[4]);
				renderOnceFlipped(window_6, graphicsHolder, light, windowPositions[5]);
				renderOnceFlipped(window_7, graphicsHolder, light, windowPositions[6]);
				renderOnceFlipped(window_8, graphicsHolder, light, windowPositions[7]);
				renderOnce(window_8, graphicsHolder, light, windowPositions[0]);
				renderOnce(window_7, graphicsHolder, light, windowPositions[1]);
				renderOnce(window_6, graphicsHolder, light, windowPositions[2]);
				renderOnce(window_5, graphicsHolder, light, windowPositions[3]);
				renderOnce(window_4, graphicsHolder, light, windowPositions[4]);
				renderOnce(window_3, graphicsHolder, light, windowPositions[5]);
				renderOnce(window_2, graphicsHolder, light, windowPositions[6]);
				renderOnce(window_1, graphicsHolder, light, windowPositions[7]);
				if (renderDetails) {
					for (final float i : windowPositions) {
						renderOnceFlipped(seat, graphicsHolder, light, -16, i - 7);
						renderOnceFlipped(seat, graphicsHolder, light, -8, i - 7);
						renderOnceFlipped(seat, graphicsHolder, light, 8, i - 7);
						renderOnceFlipped(seat, graphicsHolder, light, 16, i - 7);
						renderOnce(seat, graphicsHolder, light, -16, i + 7);
						renderOnce(seat, graphicsHolder, light, -8, i + 7);
						renderOnce(seat, graphicsHolder, light, 8, i + 7);
						renderOnce(seat, graphicsHolder, light, 16, i + 7);
					}
				}
				break;
			case EXTERIOR:
				if (isEnd1Head) {
					renderOnceFlipped(window_end_exterior_1, graphicsHolder, light, windowPositions[0]);
					renderOnceFlipped(window_end_exterior_2, graphicsHolder, light, windowPositions[1]);
					renderOnceFlipped(window_end_exterior_3, graphicsHolder, light, windowPositions[2]);
					renderOnceFlipped(window_end_exterior_4, graphicsHolder, light, windowPositions[3]);
				} else {
					renderOnceFlipped(window_exterior_1, graphicsHolder, light, windowPositions[0]);
					renderOnceFlipped(window_exterior_2, graphicsHolder, light, windowPositions[1]);
					renderOnceFlipped(window_exterior_3, graphicsHolder, light, windowPositions[2]);
					renderOnceFlipped(window_exterior_4, graphicsHolder, light, windowPositions[3]);
				}
				if (isEnd2Head) {
					renderOnce(window_end_exterior_4, graphicsHolder, light, windowPositions[4]);
					renderOnce(window_end_exterior_3, graphicsHolder, light, windowPositions[5]);
					renderOnce(window_end_exterior_2, graphicsHolder, light, windowPositions[6]);
					renderOnce(window_end_exterior_1, graphicsHolder, light, windowPositions[7]);
				} else {
					renderOnceFlipped(window_exterior_5, graphicsHolder, light, windowPositions[4]);
					renderOnceFlipped(window_exterior_6, graphicsHolder, light, windowPositions[5]);
					renderOnceFlipped(window_exterior_7, graphicsHolder, light, windowPositions[6]);
					renderOnceFlipped(window_exterior_8, graphicsHolder, light, windowPositions[7]);
				}
				renderMirror(roof_exterior_1, graphicsHolder, light, windowPositions[0]);
				renderOnce(roof_exterior_door_edge, graphicsHolder, light, windowPositions[1]);
				renderOnceFlipped(roof_exterior_door_edge, graphicsHolder, light, windowPositions[2]);
				renderMirror(roof_exterior_1, graphicsHolder, light, windowPositions[3]);
				renderMirror(roof_exterior_1, graphicsHolder, light, windowPositions[4]);
				renderOnce(roof_exterior_door_edge, graphicsHolder, light, windowPositions[5]);
				renderOnceFlipped(roof_exterior_door_edge, graphicsHolder, light, windowPositions[6]);
				renderMirror(roof_exterior_1, graphicsHolder, light, windowPositions[7]);
				break;
		}
	}

	@Override
	protected void renderDoorPositions(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
		final boolean isFirstDoor = isIndex(0, position, getDoorPositions());
		final boolean doorOpen = doorLeftZ > 0 || doorRightZ > 0;

		switch (renderStage) {
			case LIGHT:
				if (isFirstDoor && doorOpen) {
					renderMirror(door_light_on, graphicsHolder, light, 0);
				}
				renderMirror(door_light, graphicsHolder, light, position);
				break;
			case INTERIOR:
				door_left.setOffset(doorRightX, 0, doorRightZ);
				door_right.setOffset(doorRightX, 0, -doorRightZ);
				renderOnce(door, graphicsHolder, light, position);
				door_left.setOffset(doorLeftX, 0, doorLeftZ);
				door_right.setOffset(doorLeftX, 0, -doorLeftZ);
				renderOnceFlipped(door, graphicsHolder, light, position);
				break;
			case EXTERIOR:
				if (isEnd1Head && isFirstDoor) {
					door_left_exterior_end_2.setOffset(-doorRightX, 0, doorRightZ);
					door_right_exterior_end_2.setOffset(-doorRightX, 0, -doorRightZ);
					door_left_exterior_end_1.setOffset(doorLeftX, 0, doorLeftZ);
					door_right_exterior_end_1.setOffset(doorLeftX, 0, -doorLeftZ);
					renderOnceFlipped(door_exterior_end, graphicsHolder, light, position);
				} else if (isEnd2Head && isIndex(-1, position, getDoorPositions())) {
					door_left_exterior_end_1.setOffset(doorRightX, 0, doorRightZ);
					door_right_exterior_end_1.setOffset(doorRightX, 0, -doorRightZ);
					door_left_exterior_end_2.setOffset(-doorLeftX, 0, doorLeftZ);
					door_right_exterior_end_2.setOffset(-doorLeftX, 0, -doorLeftZ);
					renderOnce(door_exterior_end, graphicsHolder, light, position);
				} else {
					door_left_exterior.setOffset(doorRightX, 0, doorRightZ);
					door_right_exterior.setOffset(doorRightX, 0, -doorRightZ);
					renderOnce(door_exterior, graphicsHolder, light, position);
					door_left_exterior.setOffset(doorLeftX, 0, doorLeftZ);
					door_right_exterior.setOffset(doorLeftX, 0, -doorLeftZ);
					renderOnceFlipped(door_exterior, graphicsHolder, light, position);
				}
				renderOnce(roof_exterior_door, graphicsHolder, light, position);
				if (isFirstDoor) {
					renderMirror(door_light_box, graphicsHolder, light, 0);
					if (!doorOpen) {
						renderMirror(door_light_off, graphicsHolder, light, 0);
					}
				}
				break;
		}
	}

	@Override
	protected void renderHeadPosition1(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean useHeadlights) {
		switch (renderStage) {
			case ALWAYS_ON_LIGHT:
				renderOnceFlipped(useHeadlights ? headlights : tail_lights, graphicsHolder, light, position - 12.5F);
				break;
			case INTERIOR:
				renderOnceFlipped(head, graphicsHolder, light, position - 12.5F);
				break;
			case EXTERIOR:
				renderOnceFlipped(head_exterior, graphicsHolder, light, position - 12.5F);
				break;
		}
	}

	@Override
	protected void renderHeadPosition2(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean useHeadlights) {
		switch (renderStage) {
			case ALWAYS_ON_LIGHT:
				renderOnce(useHeadlights ? headlights : tail_lights, graphicsHolder, light, position + 12.5F);
				break;
			case INTERIOR:
				renderOnce(head, graphicsHolder, light, position + 12.5F);
				break;
			case EXTERIOR:
				renderOnce(head_exterior, graphicsHolder, light, position + 12.5F);
				break;
		}
	}

	@Override
	protected void renderEndPosition1(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
		switch (renderStage) {
			case INTERIOR:
				renderOnceFlipped(end, graphicsHolder, light, position - 12.5F);
				break;
			case EXTERIOR:
				renderOnceFlipped(end_exterior, graphicsHolder, light, position - 12.5F);
				break;
		}
	}

	@Override
	protected void renderEndPosition2(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
		switch (renderStage) {
			case INTERIOR:
				renderOnce(end, graphicsHolder, light, position + 12.5F);
				break;
			case EXTERIOR:
				renderOnce(end_exterior, graphicsHolder, light, position + 12.5F);
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
		return new int[]{0};
	}

	@Override
	protected int[] getDoorPositions() {
		return new int[]{-64, 64};
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
	protected void renderTextDisplays(StoredMatrixTransformations storedMatrixTransformations, int thisRouteColor, String thisRouteName, String thisRouteNumber, String thisStationName, String thisRouteDestination, String nextStationName, Consumer<BiConsumer<String, InterchangeColorsForStationName>> getInterchanges, int car, int totalCars, boolean atPlatform, boolean isTerminating, ObjectArrayList<ScrollingText> scrollingTexts) {
		if (scrollingTexts.isEmpty()) {
			scrollingTexts.add(new ScrollingText(0.5F, 0.1F, 4, true));
			scrollingTexts.add(new ScrollingText(1.08F, 0.06F, 8, true));
		}
		final boolean isEnd1Head = car == 0;
		final boolean isEnd2Head = car == totalCars - 1;
		final float offset = 0.23F;
		final float[] positions1 = {-4 + 26.5F / 16 - offset, 4 + 26.5F / 16 - (isEnd2Head ? -1 : 1) * offset};
		final float[] positions2 = {-4 - 26.5F / 16 - offset, 4 - 26.5F / 16 - (isEnd2Head ? -1 : 1) * offset};

		final String destinationString = getDestinationString(thisRouteDestination, TextSpacingType.NORMAL, false);
		scrollingTexts.get(0).changeImage(destinationString.isEmpty() ? null : DynamicTextureCache.instance.getPixelatedText(destinationString.replace("|", " "), 0xFFFF9900, Integer.MAX_VALUE, 0, true));

		for (final float position : positions1) {
			final StoredMatrixTransformations storedMatrixTransformationsNew = storedMatrixTransformations.copy();
			storedMatrixTransformationsNew.add(graphicsHolder -> {
				graphicsHolder.translate(-21F / 16, -13F / 16, position);
				graphicsHolder.rotateYDegrees(90);
				graphicsHolder.rotateXDegrees(-8);
				graphicsHolder.translate(-0.25F, -0.82F, -0.01F);
			});
			scrollingTexts.get(0).scrollText(storedMatrixTransformationsNew);
		}
		for (final float position : positions2) {
			final StoredMatrixTransformations storedMatrixTransformationsNew = storedMatrixTransformations.copy();
			storedMatrixTransformationsNew.add(graphicsHolder -> {
				graphicsHolder.translate(21F / 16, -13F / 16, position);
				graphicsHolder.rotateYDegrees(-90);
				graphicsHolder.rotateXDegrees(-8);
				graphicsHolder.translate(-0.25F, -0.82F, -0.01F);
			});
			scrollingTexts.get(0).scrollText(storedMatrixTransformationsNew);
		}

		final String nextStationString = getLondonNextStationString(thisRouteName, thisStationName, nextStationName, getInterchanges, destinationString, atPlatform, isTerminating);
		scrollingTexts.get(1).changeImage(nextStationString.isEmpty() ? null : DynamicTextureCache.instance.getPixelatedText(nextStationString, 0xFFFF9900, Integer.MAX_VALUE, 0, true));

		for (int i = 0; i < 2; i++) {
			final StoredMatrixTransformations storedMatrixTransformationsNew = storedMatrixTransformations.copy();
			final boolean flip = i == 1;
			storedMatrixTransformationsNew.add(graphicsHolder -> {
				if (flip) {
					graphicsHolder.rotateYDegrees(180);
				}
				graphicsHolder.translate(-0.54F, -2.14F, (getEndPositions()[1] - (flip && isEnd1Head || !flip && isEnd2Head ? 13 : 0)) / 16F - 0.01);
			});
			scrollingTexts.get(1).scrollText(storedMatrixTransformationsNew);
		}
	}

	@Override
	protected String defaultDestinationString() {
		return "Not in Service";
	}

	private float[] getNewWindowPositions() {
		return new float[]{-115.5F, -90.5F, -37.5F, -12.5F, 12.5F, 37.5F, 90.5F, 115.5F};
	}
}