package org.mtr.mod.model;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.core.data.InterchangeColorsForStationName;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.ModelPartExtension;
import org.mtr.mod.client.DoorAnimationType;
import org.mtr.mod.client.DynamicTextureCache;
import org.mtr.mod.client.ScrollingText;
import org.mtr.mod.render.StoredMatrixTransformations;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ModelClass802 extends ModelSimpleTrainBase<ModelClass802> {

	private final ModelPartExtension window;
	private final ModelPartExtension roof_3_r1;
	private final ModelPartExtension roof_1_r1;
	private final ModelPartExtension window_bottom_r1;
	private final ModelPartExtension window_top_r1;
	private final ModelPartExtension window_light;
	private final ModelPartExtension window_exterior_1;
	private final ModelPartExtension roof_2_r1;
	private final ModelPartExtension roof_1_r2;
	private final ModelPartExtension window_bottom_r2;
	private final ModelPartExtension window_top_r2;
	private final ModelPartExtension window_exterior_2;
	private final ModelPartExtension roof_2_r2;
	private final ModelPartExtension roof_1_r3;
	private final ModelPartExtension window_bottom_r3;
	private final ModelPartExtension window_top_r3;
	private final ModelPartExtension window_exterior_3;
	private final ModelPartExtension roof_2_r3;
	private final ModelPartExtension roof_1_r4;
	private final ModelPartExtension window_bottom_r4;
	private final ModelPartExtension window_top_r4;
	private final ModelPartExtension window_exterior_4;
	private final ModelPartExtension roof_2_r4;
	private final ModelPartExtension roof_1_r5;
	private final ModelPartExtension window_bottom_r5;
	private final ModelPartExtension window_top_r5;
	private final ModelPartExtension window_exterior_5;
	private final ModelPartExtension roof_2_r5;
	private final ModelPartExtension roof_1_r6;
	private final ModelPartExtension window_bottom_r6;
	private final ModelPartExtension window_top_r6;
	private final ModelPartExtension window_exterior_6;
	private final ModelPartExtension roof_2_r6;
	private final ModelPartExtension roof_1_r7;
	private final ModelPartExtension window_bottom_r7;
	private final ModelPartExtension window_top_r7;
	private final ModelPartExtension window_exterior_7;
	private final ModelPartExtension roof_2_r7;
	private final ModelPartExtension roof_1_r8;
	private final ModelPartExtension window_bottom_r8;
	private final ModelPartExtension window_top_r8;
	private final ModelPartExtension window_exterior_8;
	private final ModelPartExtension roof_2_r8;
	private final ModelPartExtension roof_1_r9;
	private final ModelPartExtension window_bottom_r9;
	private final ModelPartExtension window_top_r9;
	private final ModelPartExtension window_exterior_9;
	private final ModelPartExtension roof_2_r9;
	private final ModelPartExtension roof_1_r10;
	private final ModelPartExtension window_bottom_r10;
	private final ModelPartExtension window_top_r10;
	private final ModelPartExtension window_end_exterior_1;
	private final ModelPartExtension window_end_exterior_1_1;
	private final ModelPartExtension roof_2_r10;
	private final ModelPartExtension roof_1_r11;
	private final ModelPartExtension window_bottom_r11;
	private final ModelPartExtension window_top_r11;
	private final ModelPartExtension window_end_exterior_1_2;
	private final ModelPartExtension roof_3_r2;
	private final ModelPartExtension roof_2_r11;
	private final ModelPartExtension window_bottom_r12;
	private final ModelPartExtension window_top_r12;
	private final ModelPartExtension window_end_exterior_2;
	private final ModelPartExtension window_end_exterior_2_1;
	private final ModelPartExtension roof_3_r3;
	private final ModelPartExtension roof_2_r12;
	private final ModelPartExtension window_bottom_r13;
	private final ModelPartExtension window_top_r13;
	private final ModelPartExtension window_end_exterior_2_2;
	private final ModelPartExtension roof_4_r1;
	private final ModelPartExtension roof_3_r4;
	private final ModelPartExtension window_bottom_r14;
	private final ModelPartExtension window_top_r14;
	private final ModelPartExtension window_end_exterior_3;
	private final ModelPartExtension window_end_exterior_3_1;
	private final ModelPartExtension roof_4_r2;
	private final ModelPartExtension roof_3_r5;
	private final ModelPartExtension window_bottom_r15;
	private final ModelPartExtension window_top_r15;
	private final ModelPartExtension window_end_exterior_3_2;
	private final ModelPartExtension roof_5_r1;
	private final ModelPartExtension roof_4_r3;
	private final ModelPartExtension window_bottom_r16;
	private final ModelPartExtension window_top_r16;
	private final ModelPartExtension window_end_exterior_4;
	private final ModelPartExtension window_end_exterior_4_1;
	private final ModelPartExtension roof_4_r4;
	private final ModelPartExtension roof_3_r6;
	private final ModelPartExtension window_bottom_r17;
	private final ModelPartExtension window_top_r17;
	private final ModelPartExtension window_end_exterior_4_2;
	private final ModelPartExtension roof_5_r2;
	private final ModelPartExtension roof_4_r5;
	private final ModelPartExtension window_bottom_r18;
	private final ModelPartExtension window_top_r18;
	private final ModelPartExtension window_end_exterior_5;
	private final ModelPartExtension window_end_exterior_5_1;
	private final ModelPartExtension roof_5_r3;
	private final ModelPartExtension roof_4_r6;
	private final ModelPartExtension window_bottom_r19;
	private final ModelPartExtension window_top_r19;
	private final ModelPartExtension window_end_exterior_5_2;
	private final ModelPartExtension roof_6_r1;
	private final ModelPartExtension roof_5_r4;
	private final ModelPartExtension window_bottom_r20;
	private final ModelPartExtension window_top_r20;
	private final ModelPartExtension window_end_exterior_6;
	private final ModelPartExtension window_end_exterior_6_1;
	private final ModelPartExtension roof_6_r2;
	private final ModelPartExtension roof_5_r5;
	private final ModelPartExtension window_bottom_r21;
	private final ModelPartExtension window_top_r21;
	private final ModelPartExtension window_end_exterior_6_2;
	private final ModelPartExtension roof_7_r1;
	private final ModelPartExtension roof_6_r3;
	private final ModelPartExtension window_bottom_r22;
	private final ModelPartExtension window_top_r22;
	private final ModelPartExtension door;
	private final ModelPartExtension door_1;
	private final ModelPartExtension window_bottom_2_r1;
	private final ModelPartExtension window_top_2_r1;
	private final ModelPartExtension door_2;
	private final ModelPartExtension window_bottom_3_r1;
	private final ModelPartExtension window_top_3_r1;
	private final ModelPartExtension door_exterior;
	private final ModelPartExtension door_exterior_1;
	private final ModelPartExtension window_bottom_1_r1;
	private final ModelPartExtension window_top_1_r1;
	private final ModelPartExtension door_exterior_2;
	private final ModelPartExtension window_bottom_2_r2;
	private final ModelPartExtension window_top_2_r2;
	private final ModelPartExtension door_exterior_end;
	private final ModelPartExtension door_exterior_end_1;
	private final ModelPartExtension window_bottom_2_r3;
	private final ModelPartExtension window_top_2_r3;
	private final ModelPartExtension door_exterior_end_2;
	private final ModelPartExtension window_bottom_3_r2;
	private final ModelPartExtension window_top_3_r2;
	private final ModelPartExtension roof_exterior;
	private final ModelPartExtension end;
	private final ModelPartExtension end_pillar_4_r1;
	private final ModelPartExtension end_pillar_3_r1;
	private final ModelPartExtension end_side_1;
	private final ModelPartExtension roof_3_r7;
	private final ModelPartExtension roof_1_r12;
	private final ModelPartExtension window_bottom_r23;
	private final ModelPartExtension window_top_r23;
	private final ModelPartExtension end_side_2;
	private final ModelPartExtension roof_4_r7;
	private final ModelPartExtension roof_2_r13;
	private final ModelPartExtension window_bottom_r24;
	private final ModelPartExtension window_top_r24;
	private final ModelPartExtension end_light;
	private final ModelPartExtension end_translucent;
	private final ModelPartExtension end_exterior;
	private final ModelPartExtension end_exterior_side_1;
	private final ModelPartExtension roof_4_r8;
	private final ModelPartExtension roof_3_r8;
	private final ModelPartExtension door_top_r1;
	private final ModelPartExtension window_bottom_2_r4;
	private final ModelPartExtension end_exterior_side_2;
	private final ModelPartExtension roof_5_r6;
	private final ModelPartExtension roof_4_r9;
	private final ModelPartExtension door_top_r2;
	private final ModelPartExtension window_bottom_3_r3;
	private final ModelPartExtension roof_vent;
	private final ModelPartExtension roof_vent_side_1;
	private final ModelPartExtension vent_4_r1;
	private final ModelPartExtension vent_3_r1;
	private final ModelPartExtension vent_1_r1;
	private final ModelPartExtension roof_vent_side_2;
	private final ModelPartExtension vent_5_r1;
	private final ModelPartExtension vent_4_r2;
	private final ModelPartExtension vent_2_r1;
	private final ModelPartExtension head_exterior;
	private final ModelPartExtension head_side_1;
	private final ModelPartExtension roof_4_r10;
	private final ModelPartExtension roof_3_r9;
	private final ModelPartExtension window_top_1_r2;
	private final ModelPartExtension window_bottom_1_r2;
	private final ModelPartExtension end_r1;
	private final ModelPartExtension roof_11_r1;
	private final ModelPartExtension roof_12_r1;
	private final ModelPartExtension roof_11_r2;
	private final ModelPartExtension roof_10_r1;
	private final ModelPartExtension roof_10_r2;
	private final ModelPartExtension roof_11_r3;
	private final ModelPartExtension roof_10_r3;
	private final ModelPartExtension roof_9_r1;
	private final ModelPartExtension roof_7_r2;
	private final ModelPartExtension roof_4_r11;
	private final ModelPartExtension roof_4_r12;
	private final ModelPartExtension roof_3_r10;
	private final ModelPartExtension roof_2_r14;
	private final ModelPartExtension roof_3_r11;
	private final ModelPartExtension roof_2_r15;
	private final ModelPartExtension roof_1_r13;
	private final ModelPartExtension head_side_2;
	private final ModelPartExtension roof_5_r7;
	private final ModelPartExtension roof_4_r13;
	private final ModelPartExtension window_top_2_r4;
	private final ModelPartExtension window_bottom_2_r5;
	private final ModelPartExtension end_r2;
	private final ModelPartExtension roof_12_r2;
	private final ModelPartExtension roof_13_r1;
	private final ModelPartExtension roof_12_r3;
	private final ModelPartExtension roof_11_r4;
	private final ModelPartExtension roof_11_r5;
	private final ModelPartExtension roof_12_r4;
	private final ModelPartExtension roof_11_r6;
	private final ModelPartExtension roof_10_r4;
	private final ModelPartExtension roof_8_r1;
	private final ModelPartExtension roof_5_r8;
	private final ModelPartExtension roof_5_r9;
	private final ModelPartExtension roof_4_r14;
	private final ModelPartExtension roof_3_r12;
	private final ModelPartExtension roof_4_r15;
	private final ModelPartExtension roof_3_r13;
	private final ModelPartExtension roof_2_r16;
	private final ModelPartExtension middle;
	private final ModelPartExtension roof_8_r2;
	private final ModelPartExtension roof_6_r4;
	private final ModelPartExtension roof_5_r10;
	private final ModelPartExtension roof_9_r2;
	private final ModelPartExtension bottom_middle;
	private final ModelPartExtension bottom_end;
	private final ModelPartExtension seat;
	private final ModelPartExtension seat_2_r1;
	private final ModelPartExtension headlights;
	private final ModelPartExtension headlight_2_r1;
	private final ModelPartExtension headlight_1_r1;
	private final ModelPartExtension tail_lights;
	private final ModelPartExtension tail_light_2_r1;
	private final ModelPartExtension tail_light_1_r1;
	private final ModelPartExtension door_light_off;
	private final ModelPartExtension door_light_off_r1;
	private final ModelPartExtension door_light_on;
	private final ModelPartExtension door_light_on_r1;

	public ModelClass802() {
		this(DoorAnimationType.STANDARD, true);
	}

	protected ModelClass802(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		super(432, 432, doorAnimationType, renderDoorOverlay);

		window = createModelPart();
		window.setPivot(0, 24, 0);
		window.setTextureUVOffset(230, 139).addCuboid(-19, 0, -15, 19, 1, 30, 0, false);
		window.setTextureUVOffset(82, 201).addCuboid(-20, -13, -15, 0, 6, 30, 0, false);
		window.setTextureUVOffset(30, 53).addCuboid(-15, -36, -15, 6, 0, 30, 0, false);
		window.setTextureUVOffset(0, 0).addCuboid(-7, -35, -15, 7, 0, 30, 0, false);
		window.setTextureUVOffset(237, 36).addCuboid(-18, -31, -15, 6, 1, 30, 0, false);

		roof_3_r1 = createModelPart();
		roof_3_r1.setPivot(-7, -35, 0);
		window.addChild(roof_3_r1);
		setRotationAngle(roof_3_r1, 0, 0, 0.3491F);
		roof_3_r1.setTextureUVOffset(14, 0).addCuboid(-3, 0, -15, 3, 0, 30, 0, false);

		roof_1_r1 = createModelPart();
		roof_1_r1.setPivot(-15, -36, 0);
		window.addChild(roof_1_r1);
		setRotationAngle(roof_1_r1, 0, 0, -0.7854F);
		roof_1_r1.setTextureUVOffset(49, 0).addCuboid(-3, 0, -15, 3, 0, 30, 0, false);

		window_bottom_r1 = createModelPart();
		window_bottom_r1.setPivot(-21, -7, 0);
		window.addChild(window_bottom_r1);
		setRotationAngle(window_bottom_r1, 0, 0, -0.2094F);
		window_bottom_r1.setTextureUVOffset(0, 120).addCuboid(1, 0, -15, 0, 8, 30, 0, false);

		window_top_r1 = createModelPart();
		window_top_r1.setPivot(-21, -13, 0);
		window.addChild(window_top_r1);
		setRotationAngle(window_top_r1, 0, 0, 0.1396F);
		window_top_r1.setTextureUVOffset(227, 205).addCuboid(1, -22, -15, 0, 22, 30, 0, false);

		window_light = createModelPart();
		window_light.setPivot(0, 24, 0);
		window_light.setTextureUVOffset(283, 44).addCuboid(-7, -35.1F, -15, 4, 0, 30, 0, false);

		window_exterior_1 = createModelPart();
		window_exterior_1.setPivot(0, 24, 0);
		window_exterior_1.setTextureUVOffset(32, 251).addCuboid(-21, -13, -15, 0, 6, 30, 0, false);

		roof_2_r1 = createModelPart();
		roof_2_r1.setPivot(-10.1709F, -40.8501F, 0);
		window_exterior_1.addChild(roof_2_r1);
		setRotationAngle(roof_2_r1, 0, 0, 1.0472F);
		roof_2_r1.setTextureUVOffset(32, 356).addCuboid(0, 3, -15, 1, 3, 30, 0, false);

		roof_1_r2 = createModelPart();
		roof_1_r2.setPivot(-17.9382F, -34.7859F, 0);
		window_exterior_1.addChild(roof_1_r2);
		setRotationAngle(roof_1_r2, 0, 0, 0.6981F);
		roof_1_r2.setTextureUVOffset(299, 311).addCuboid(0, -4, -15, 0, 4, 30, 0, false);

		window_bottom_r2 = createModelPart();
		window_bottom_r2.setPivot(-21, -7, 0);
		window_exterior_1.addChild(window_bottom_r2);
		setRotationAngle(window_bottom_r2, 0, 0, -0.2094F);
		window_bottom_r2.setTextureUVOffset(203, 313).addCuboid(0, 0, -15, 1, 8, 30, 0, false);

		window_top_r2 = createModelPart();
		window_top_r2.setPivot(-21, -13, 0);
		window_exterior_1.addChild(window_top_r2);
		setRotationAngle(window_top_r2, 0, 0, 0.1396F);
		window_top_r2.setTextureUVOffset(82, 229).addCuboid(0, -22, -15, 0, 22, 30, 0, false);

		window_exterior_2 = createModelPart();
		window_exterior_2.setPivot(0, 24, 0);
		window_exterior_2.setTextureUVOffset(160, 198).addCuboid(-21, -13, -15, 0, 6, 30, 0, false);

		roof_2_r2 = createModelPart();
		roof_2_r2.setPivot(-10.1709F, -40.8501F, 0);
		window_exterior_2.addChild(roof_2_r2);
		setRotationAngle(roof_2_r2, 0, 0, 1.0472F);
		roof_2_r2.setTextureUVOffset(331, 355).addCuboid(0, 3, -15, 1, 3, 30, 0, false);

		roof_1_r3 = createModelPart();
		roof_1_r3.setPivot(-17.9382F, -34.7859F, 0);
		window_exterior_2.addChild(roof_1_r3);
		setRotationAngle(roof_1_r3, 0, 0, 0.6981F);
		roof_1_r3.setTextureUVOffset(299, 307).addCuboid(0, -4, -15, 0, 4, 30, 0, false);

		window_bottom_r3 = createModelPart();
		window_bottom_r3.setPivot(-21, -7, 0);
		window_exterior_2.addChild(window_bottom_r3);
		setRotationAngle(window_bottom_r3, 0, 0, -0.2094F);
		window_bottom_r3.setTextureUVOffset(313, 54).addCuboid(0, 0, -15, 1, 8, 30, 0, false);

		window_top_r3 = createModelPart();
		window_top_r3.setPivot(-21, -13, 0);
		window_exterior_2.addChild(window_top_r3);
		setRotationAngle(window_top_r3, 0, 0, 0.1396F);
		window_top_r3.setTextureUVOffset(160, 164).addCuboid(0, -22, -15, 0, 22, 30, 0, false);

		window_exterior_3 = createModelPart();
		window_exterior_3.setPivot(0, 24, 0);
		window_exterior_3.setTextureUVOffset(160, 192).addCuboid(-21, -13, -15, 0, 6, 30, 0, false);

		roof_2_r3 = createModelPart();
		roof_2_r3.setPivot(-10.1709F, -40.8501F, 0);
		window_exterior_3.addChild(roof_2_r3);
		setRotationAngle(roof_2_r3, 0, 0, 1.0472F);
		roof_2_r3.setTextureUVOffset(221, 354).addCuboid(0, 3, -15, 1, 3, 30, 0, false);

		roof_1_r4 = createModelPart();
		roof_1_r4.setPivot(-17.9382F, -34.7859F, 0);
		window_exterior_3.addChild(roof_1_r4);
		setRotationAngle(roof_1_r4, 0, 0, 0.6981F);
		roof_1_r4.setTextureUVOffset(299, 303).addCuboid(0, -4, -15, 0, 4, 30, 0, false);

		window_bottom_r4 = createModelPart();
		window_bottom_r4.setPivot(-21, -7, 0);
		window_exterior_3.addChild(window_bottom_r4);
		setRotationAngle(window_bottom_r4, 0, 0, -0.2094F);
		window_bottom_r4.setTextureUVOffset(0, 312).addCuboid(0, 0, -15, 1, 8, 30, 0, false);

		window_top_r4 = createModelPart();
		window_top_r4.setPivot(-21, -13, 0);
		window_exterior_3.addChild(window_top_r4);
		setRotationAngle(window_top_r4, 0, 0, 0.1396F);
		window_top_r4.setTextureUVOffset(98, 164).addCuboid(0, -22, -15, 0, 22, 30, 0, false);

		window_exterior_4 = createModelPart();
		window_exterior_4.setPivot(0, 24, 0);
		window_exterior_4.setTextureUVOffset(98, 192).addCuboid(-21, -13, -15, 0, 6, 30, 0, false);

		roof_2_r4 = createModelPart();
		roof_2_r4.setPivot(-10.1709F, -40.8501F, 0);
		window_exterior_4.addChild(roof_2_r4);
		setRotationAngle(roof_2_r4, 0, 0, 1.0472F);
		roof_2_r4.setTextureUVOffset(299, 352).addCuboid(0, 3, -15, 1, 3, 30, 0, false);

		roof_1_r5 = createModelPart();
		roof_1_r5.setPivot(-17.9382F, -34.7859F, 0);
		window_exterior_4.addChild(roof_1_r5);
		setRotationAngle(roof_1_r5, 0, 0, 0.6981F);
		roof_1_r5.setTextureUVOffset(299, 299).addCuboid(0, -4, -15, 0, 4, 30, 0, false);

		window_bottom_r5 = createModelPart();
		window_bottom_r5.setPivot(-21, -7, 0);
		window_exterior_4.addChild(window_bottom_r5);
		setRotationAngle(window_bottom_r5, 0, 0, -0.2094F);
		window_bottom_r5.setTextureUVOffset(94, 310).addCuboid(0, 0, -15, 1, 8, 30, 0, false);

		window_top_r5 = createModelPart();
		window_top_r5.setPivot(-21, -13, 0);
		window_exterior_4.addChild(window_top_r5);
		setRotationAngle(window_top_r5, 0, 0, 0.1396F);
		window_top_r5.setTextureUVOffset(161, 0).addCuboid(0, -22, -15, 0, 22, 30, 0, false);

		window_exterior_5 = createModelPart();
		window_exterior_5.setPivot(0, 24, 0);
		window_exterior_5.setTextureUVOffset(0, 192).addCuboid(-21, -13, -15, 0, 6, 30, 0, false);

		roof_2_r5 = createModelPart();
		roof_2_r5.setPivot(-10.1709F, -40.8501F, 0);
		window_exterior_5.addChild(roof_2_r5);
		setRotationAngle(roof_2_r5, 0, 0, 1.0472F);
		roof_2_r5.setTextureUVOffset(189, 351).addCuboid(0, 3, -15, 1, 3, 30, 0, false);

		roof_1_r6 = createModelPart();
		roof_1_r6.setPivot(-17.9382F, -34.7859F, 0);
		window_exterior_5.addChild(roof_1_r6);
		setRotationAngle(roof_1_r6, 0, 0, 0.6981F);
		roof_1_r6.setTextureUVOffset(297, 256).addCuboid(0, -4, -15, 0, 4, 30, 0, false);

		window_bottom_r6 = createModelPart();
		window_bottom_r6.setPivot(-21, -7, 0);
		window_exterior_5.addChild(window_bottom_r6);
		setRotationAngle(window_bottom_r6, 0, 0, -0.2094F);
		window_bottom_r6.setTextureUVOffset(298, 147).addCuboid(0, 0, -15, 1, 8, 30, 0, false);

		window_top_r6 = createModelPart();
		window_top_r6.setPivot(-21, -13, 0);
		window_exterior_5.addChild(window_top_r6);
		setRotationAngle(window_top_r6, 0, 0, 0.1396F);
		window_top_r6.setTextureUVOffset(160, 142).addCuboid(0, -22, -15, 0, 22, 30, 0, false);

		window_exterior_6 = createModelPart();
		window_exterior_6.setPivot(0, 24, 0);
		window_exterior_6.setTextureUVOffset(160, 186).addCuboid(-21, -13, -15, 0, 6, 30, 0, false);

		roof_2_r6 = createModelPart();
		roof_2_r6.setPivot(-10.1709F, -40.8501F, 0);
		window_exterior_6.addChild(roof_2_r6);
		setRotationAngle(roof_2_r6, 0, 0, 1.0472F);
		roof_2_r6.setTextureUVOffset(0, 350).addCuboid(0, 3, -15, 1, 3, 30, 0, false);

		roof_1_r7 = createModelPart();
		roof_1_r7.setPivot(-17.9382F, -34.7859F, 0);
		window_exterior_6.addChild(roof_1_r7);
		setRotationAngle(roof_1_r7, 0, 0, 0.6981F);
		roof_1_r7.setTextureUVOffset(158, 290).addCuboid(0, -4, -15, 0, 4, 30, 0, false);

		window_bottom_r7 = createModelPart();
		window_bottom_r7.setPivot(-21, -7, 0);
		window_exterior_6.addChild(window_bottom_r7);
		setRotationAngle(window_bottom_r7, 0, 0, -0.2094F);
		window_bottom_r7.setTextureUVOffset(298, 109).addCuboid(0, 0, -15, 1, 8, 30, 0, false);

		window_top_r7 = createModelPart();
		window_top_r7.setPivot(-21, -13, 0);
		window_exterior_6.addChild(window_top_r7);
		setRotationAngle(window_top_r7, 0, 0, 0.1396F);
		window_top_r7.setTextureUVOffset(158, 100).addCuboid(0, -22, -15, 0, 22, 30, 0, false);

		window_exterior_7 = createModelPart();
		window_exterior_7.setPivot(0, 24, 0);
		window_exterior_7.setTextureUVOffset(98, 186).addCuboid(-21, -13, -15, 0, 6, 30, 0, false);

		roof_2_r7 = createModelPart();
		roof_2_r7.setPivot(-10.1709F, -40.8501F, 0);
		window_exterior_7.addChild(roof_2_r7);
		setRotationAngle(roof_2_r7, 0, 0, 1.0472F);
		roof_2_r7.setTextureUVOffset(92, 348).addCuboid(0, 3, -15, 1, 3, 30, 0, false);

		roof_1_r8 = createModelPart();
		roof_1_r8.setPivot(-17.9382F, -34.7859F, 0);
		window_exterior_7.addChild(roof_1_r8);
		setRotationAngle(roof_1_r8, 0, 0, 0.6981F);
		roof_1_r8.setTextureUVOffset(158, 286).addCuboid(0, -4, -15, 0, 4, 30, 0, false);

		window_bottom_r8 = createModelPart();
		window_bottom_r8.setPivot(-21, -7, 0);
		window_exterior_7.addChild(window_bottom_r8);
		setRotationAngle(window_bottom_r8, 0, 0, -0.2094F);
		window_bottom_r8.setTextureUVOffset(297, 291).addCuboid(0, 0, -15, 1, 8, 30, 0, false);

		window_top_r8 = createModelPart();
		window_top_r8.setPivot(-21, -13, 0);
		window_exterior_7.addChild(window_top_r8);
		setRotationAngle(window_top_r8, 0, 0, 0.1396F);
		window_top_r8.setTextureUVOffset(158, 78).addCuboid(0, -22, -15, 0, 22, 30, 0, false);

		window_exterior_8 = createModelPart();
		window_exterior_8.setPivot(0, 24, 0);
		window_exterior_8.setTextureUVOffset(0, 186).addCuboid(-21, -13, -15, 0, 6, 30, 0, false);

		roof_2_r8 = createModelPart();
		roof_2_r8.setPivot(-10.1709F, -40.8501F, 0);
		window_exterior_8.addChild(roof_2_r8);
		setRotationAngle(roof_2_r8, 0, 0, 1.0472F);
		roof_2_r8.setTextureUVOffset(345, 33).addCuboid(0, 3, -15, 1, 3, 30, 0, false);

		roof_1_r9 = createModelPart();
		roof_1_r9.setPivot(-17.9382F, -34.7859F, 0);
		window_exterior_8.addChild(roof_1_r9);
		setRotationAngle(roof_1_r9, 0, 0, 0.6981F);
		roof_1_r9.setTextureUVOffset(281, 70).addCuboid(0, -4, -15, 0, 4, 30, 0, false);

		window_bottom_r9 = createModelPart();
		window_bottom_r9.setPivot(-21, -7, 0);
		window_exterior_8.addChild(window_bottom_r9);
		setRotationAngle(window_bottom_r9, 0, 0, -0.2094F);
		window_bottom_r9.setTextureUVOffset(296, 245).addCuboid(0, 0, -15, 1, 8, 30, 0, false);

		window_top_r9 = createModelPart();
		window_top_r9.setPivot(-21, -13, 0);
		window_exterior_8.addChild(window_top_r9);
		setRotationAngle(window_top_r9, 0, 0, 0.1396F);
		window_top_r9.setTextureUVOffset(158, 56).addCuboid(0, -22, -15, 0, 22, 30, 0, false);

		window_exterior_9 = createModelPart();
		window_exterior_9.setPivot(0, 24, 0);
		window_exterior_9.setTextureUVOffset(0, 180).addCuboid(-21, -13, -15, 0, 6, 30, 0, false);

		roof_2_r9 = createModelPart();
		roof_2_r9.setPivot(-10.1709F, -40.8501F, 0);
		window_exterior_9.addChild(roof_2_r9);
		setRotationAngle(roof_2_r9, 0, 0, 1.0472F);
		roof_2_r9.setTextureUVOffset(330, 125).addCuboid(0, 3, -15, 1, 3, 30, 0, false);

		roof_1_r10 = createModelPart();
		roof_1_r10.setPivot(-17.9382F, -34.7859F, 0);
		window_exterior_9.addChild(roof_1_r10);
		setRotationAngle(roof_1_r10, 0, 0, 0.6981F);
		roof_1_r10.setTextureUVOffset(281, 66).addCuboid(0, -4, -15, 0, 4, 30, 0, false);

		window_bottom_r10 = createModelPart();
		window_bottom_r10.setPivot(-21, -7, 0);
		window_exterior_9.addChild(window_bottom_r10);
		setRotationAngle(window_bottom_r10, 0, 0, -0.2094F);
		window_bottom_r10.setTextureUVOffset(296, 205).addCuboid(0, 0, -15, 1, 8, 30, 0, false);

		window_top_r10 = createModelPart();
		window_top_r10.setPivot(-21, -13, 0);
		window_exterior_9.addChild(window_top_r10);
		setRotationAngle(window_top_r10, 0, 0, 0.1396F);
		window_top_r10.setTextureUVOffset(0, 158).addCuboid(0, -22, -15, 0, 22, 30, 0, false);

		window_end_exterior_1 = createModelPart();
		window_end_exterior_1.setPivot(0, 24, 0);


		window_end_exterior_1_1 = createModelPart();
		window_end_exterior_1_1.setPivot(-10.1709F, -40.8501F, 0);
		window_end_exterior_1.addChild(window_end_exterior_1_1);
		window_end_exterior_1_1.setTextureUVOffset(161, 46).addCuboid(-10.8291F, 27.8501F, -15, 0, 6, 30, 0, false);

		roof_2_r10 = createModelPart();
		roof_2_r10.setPivot(0, 0, 0);
		window_end_exterior_1_1.addChild(roof_2_r10);
		setRotationAngle(roof_2_r10, 0, 0, 1.0472F);
		roof_2_r10.setTextureUVOffset(330, 92).addCuboid(0, 3, -15, 1, 3, 30, 0, false);

		roof_1_r11 = createModelPart();
		roof_1_r11.setPivot(-7.7673F, 6.0642F, 0);
		window_end_exterior_1_1.addChild(roof_1_r11);
		setRotationAngle(roof_1_r11, 0, 0, 0.6981F);
		roof_1_r11.setTextureUVOffset(281, 62).addCuboid(0, -4, -15, 0, 4, 30, 0, false);

		window_bottom_r11 = createModelPart();
		window_bottom_r11.setPivot(-10.8291F, 33.8501F, 0);
		window_end_exterior_1_1.addChild(window_bottom_r11);
		setRotationAngle(window_bottom_r11, 0, 0, -0.2094F);
		window_bottom_r11.setTextureUVOffset(287, 6).addCuboid(0, 0, -15, 1, 8, 30, 0, false);

		window_top_r11 = createModelPart();
		window_top_r11.setPivot(-10.8291F, 27.8501F, 0);
		window_end_exterior_1_1.addChild(window_top_r11);
		setRotationAngle(window_top_r11, 0, 0, 0.1396F);
		window_top_r11.setTextureUVOffset(98, 142).addCuboid(0, -22, -15, 0, 22, 30, 0, false);

		window_end_exterior_1_2 = createModelPart();
		window_end_exterior_1_2.setPivot(10.1709F, -40.8501F, 0);
		window_end_exterior_1.addChild(window_end_exterior_1_2);
		window_end_exterior_1_2.setTextureUVOffset(161, 46).addCuboid(10.8291F, 27.8501F, -15, 0, 6, 30, 0, true);

		roof_3_r2 = createModelPart();
		roof_3_r2.setPivot(0, 0, 0);
		window_end_exterior_1_2.addChild(roof_3_r2);
		setRotationAngle(roof_3_r2, 0, 0, -1.0472F);
		roof_3_r2.setTextureUVOffset(330, 92).addCuboid(-1, 3, -15, 1, 3, 30, 0, true);

		roof_2_r11 = createModelPart();
		roof_2_r11.setPivot(7.7673F, 6.0642F, 0);
		window_end_exterior_1_2.addChild(roof_2_r11);
		setRotationAngle(roof_2_r11, 0, 0, -0.6981F);
		roof_2_r11.setTextureUVOffset(281, 62).addCuboid(0, -4, -15, 0, 4, 30, 0, true);

		window_bottom_r12 = createModelPart();
		window_bottom_r12.setPivot(10.8291F, 33.8501F, 0);
		window_end_exterior_1_2.addChild(window_bottom_r12);
		setRotationAngle(window_bottom_r12, 0, 0, 0.2094F);
		window_bottom_r12.setTextureUVOffset(287, 6).addCuboid(-1, 0, -15, 1, 8, 30, 0, true);

		window_top_r12 = createModelPart();
		window_top_r12.setPivot(10.8291F, 27.8501F, 0);
		window_end_exterior_1_2.addChild(window_top_r12);
		setRotationAngle(window_top_r12, 0, 0, -0.1396F);
		window_top_r12.setTextureUVOffset(98, 142).addCuboid(0, -22, -15, 0, 22, 30, 0, true);

		window_end_exterior_2 = createModelPart();
		window_end_exterior_2.setPivot(0, 24, 0);


		window_end_exterior_2_1 = createModelPart();
		window_end_exterior_2_1.setPivot(-10.1709F, -40.8501F, 0);
		window_end_exterior_2.addChild(window_end_exterior_2_1);
		window_end_exterior_2_1.setTextureUVOffset(161, 34).addCuboid(-10.8291F, 27.8501F, -15, 0, 6, 30, 0, false);

		roof_3_r3 = createModelPart();
		roof_3_r3.setPivot(0, 0, 0);
		window_end_exterior_2_1.addChild(roof_3_r3);
		setRotationAngle(roof_3_r3, 0, 0, 1.0472F);
		roof_3_r3.setTextureUVOffset(329, 319).addCuboid(0, 3, -15, 1, 3, 30, 0, false);

		roof_2_r12 = createModelPart();
		roof_2_r12.setPivot(-7.7673F, 6.0642F, 0);
		window_end_exterior_2_1.addChild(roof_2_r12);
		setRotationAngle(roof_2_r12, 0, 0, 0.6981F);
		roof_2_r12.setTextureUVOffset(278, 167).addCuboid(0, -4, -15, 0, 4, 30, 0, false);

		window_bottom_r13 = createModelPart();
		window_bottom_r13.setPivot(-10.8291F, 33.8501F, 0);
		window_end_exterior_2_1.addChild(window_bottom_r13);
		setRotationAngle(window_bottom_r13, 0, 0, -0.2094F);
		window_bottom_r13.setTextureUVOffset(265, 283).addCuboid(0, 0, -15, 1, 8, 30, 0, false);

		window_top_r13 = createModelPart();
		window_top_r13.setPivot(-10.8291F, 27.8501F, 0);
		window_end_exterior_2_1.addChild(window_top_r13);
		setRotationAngle(window_top_r13, 0, 0, 0.1396F);
		window_top_r13.setTextureUVOffset(0, 136).addCuboid(0, -22, -15, 0, 22, 30, 0, false);

		window_end_exterior_2_2 = createModelPart();
		window_end_exterior_2_2.setPivot(10.1709F, -40.8501F, 0);
		window_end_exterior_2.addChild(window_end_exterior_2_2);
		window_end_exterior_2_2.setTextureUVOffset(161, 34).addCuboid(10.8291F, 27.8501F, -15, 0, 6, 30, 0, true);

		roof_4_r1 = createModelPart();
		roof_4_r1.setPivot(0, 0, 0);
		window_end_exterior_2_2.addChild(roof_4_r1);
		setRotationAngle(roof_4_r1, 0, 0, -1.0472F);
		roof_4_r1.setTextureUVOffset(329, 319).addCuboid(-1, 3, -15, 1, 3, 30, 0, true);

		roof_3_r4 = createModelPart();
		roof_3_r4.setPivot(7.7673F, 6.0642F, 0);
		window_end_exterior_2_2.addChild(roof_3_r4);
		setRotationAngle(roof_3_r4, 0, 0, -0.6981F);
		roof_3_r4.setTextureUVOffset(278, 167).addCuboid(0, -4, -15, 0, 4, 30, 0, true);

		window_bottom_r14 = createModelPart();
		window_bottom_r14.setPivot(10.8291F, 33.8501F, 0);
		window_end_exterior_2_2.addChild(window_bottom_r14);
		setRotationAngle(window_bottom_r14, 0, 0, 0.2094F);
		window_bottom_r14.setTextureUVOffset(265, 283).addCuboid(-1, 0, -15, 1, 8, 30, 0, true);

		window_top_r14 = createModelPart();
		window_top_r14.setPivot(10.8291F, 27.8501F, 0);
		window_end_exterior_2_2.addChild(window_top_r14);
		setRotationAngle(window_top_r14, 0, 0, -0.1396F);
		window_top_r14.setTextureUVOffset(0, 136).addCuboid(0, -22, -15, 0, 22, 30, 0, true);

		window_end_exterior_3 = createModelPart();
		window_end_exterior_3.setPivot(0, 24, 0);


		window_end_exterior_3_1 = createModelPart();
		window_end_exterior_3_1.setPivot(-10.1709F, -40.8501F, 0);
		window_end_exterior_3.addChild(window_end_exterior_3_1);
		window_end_exterior_3_1.setTextureUVOffset(161, 28).addCuboid(-10.8291F, 27.8501F, -15, 0, 6, 30, 0, false);

		roof_4_r2 = createModelPart();
		roof_4_r2.setPivot(0, 0, 0);
		window_end_exterior_3_1.addChild(roof_4_r2);
		setRotationAngle(roof_4_r2, 0, 0, 1.0472F);
		roof_4_r2.setTextureUVOffset(329, 286).addCuboid(0, 3, -15, 1, 3, 30, 0, false);

		roof_3_r5 = createModelPart();
		roof_3_r5.setPivot(-7.7673F, 6.0642F, 0);
		window_end_exterior_3_1.addChild(roof_3_r5);
		setRotationAngle(roof_3_r5, 0, 0, 0.6981F);
		roof_3_r5.setTextureUVOffset(278, 163).addCuboid(0, -4, -15, 0, 4, 30, 0, false);

		window_bottom_r15 = createModelPart();
		window_bottom_r15.setPivot(-10.8291F, 33.8501F, 0);
		window_end_exterior_3_1.addChild(window_bottom_r15);
		setRotationAngle(window_bottom_r15, 0, 0, -0.2094F);
		window_bottom_r15.setTextureUVOffset(62, 281).addCuboid(0, 0, -15, 1, 8, 30, 0, false);

		window_top_r15 = createModelPart();
		window_top_r15.setPivot(-10.8291F, 27.8501F, 0);
		window_end_exterior_3_1.addChild(window_top_r15);
		setRotationAngle(window_top_r15, 0, 0, 0.1396F);
		window_top_r15.setTextureUVOffset(74, 100).addCuboid(0, -22, -15, 0, 22, 30, 0, false);

		window_end_exterior_3_2 = createModelPart();
		window_end_exterior_3_2.setPivot(10.1709F, -40.8501F, 0);
		window_end_exterior_3.addChild(window_end_exterior_3_2);
		window_end_exterior_3_2.setTextureUVOffset(161, 28).addCuboid(10.8291F, 27.8501F, -15, 0, 6, 30, 0, true);

		roof_5_r1 = createModelPart();
		roof_5_r1.setPivot(0, 0, 0);
		window_end_exterior_3_2.addChild(roof_5_r1);
		setRotationAngle(roof_5_r1, 0, 0, -1.0472F);
		roof_5_r1.setTextureUVOffset(329, 286).addCuboid(-1, 3, -15, 1, 3, 30, 0, true);

		roof_4_r3 = createModelPart();
		roof_4_r3.setPivot(7.7673F, 6.0642F, 0);
		window_end_exterior_3_2.addChild(roof_4_r3);
		setRotationAngle(roof_4_r3, 0, 0, -0.6981F);
		roof_4_r3.setTextureUVOffset(278, 163).addCuboid(0, -4, -15, 0, 4, 30, 0, true);

		window_bottom_r16 = createModelPart();
		window_bottom_r16.setPivot(10.8291F, 33.8501F, 0);
		window_end_exterior_3_2.addChild(window_bottom_r16);
		setRotationAngle(window_bottom_r16, 0, 0, 0.2094F);
		window_bottom_r16.setTextureUVOffset(62, 281).addCuboid(-1, 0, -15, 1, 8, 30, 0, true);

		window_top_r16 = createModelPart();
		window_top_r16.setPivot(10.8291F, 27.8501F, 0);
		window_end_exterior_3_2.addChild(window_top_r16);
		setRotationAngle(window_top_r16, 0, 0, -0.1396F);
		window_top_r16.setTextureUVOffset(74, 100).addCuboid(0, -22, -15, 0, 22, 30, 0, true);

		window_end_exterior_4 = createModelPart();
		window_end_exterior_4.setPivot(0, 24, 0);


		window_end_exterior_4_1 = createModelPart();
		window_end_exterior_4_1.setPivot(-10.1709F, -40.8501F, 0);
		window_end_exterior_4.addChild(window_end_exterior_4_1);
		window_end_exterior_4_1.setTextureUVOffset(161, 22).addCuboid(-10.8291F, 27.8501F, -15, 0, 6, 30, 0, false);

		roof_4_r4 = createModelPart();
		roof_4_r4.setPivot(0, 0, 0);
		window_end_exterior_4_1.addChild(roof_4_r4);
		setRotationAngle(roof_4_r4, 0, 0, 1.0472F);
		roof_4_r4.setTextureUVOffset(267, 329).addCuboid(0, 3, -15, 1, 3, 30, 0, false);

		roof_3_r6 = createModelPart();
		roof_3_r6.setPivot(-7.7673F, 6.0642F, 0);
		window_end_exterior_4_1.addChild(roof_3_r6);
		setRotationAngle(roof_3_r6, 0, 0, 0.6981F);
		roof_3_r6.setTextureUVOffset(278, 159).addCuboid(0, -4, -15, 0, 4, 30, 0, false);

		window_bottom_r17 = createModelPart();
		window_bottom_r17.setPivot(-10.8291F, 33.8501F, 0);
		window_end_exterior_4_1.addChild(window_bottom_r17);
		setRotationAngle(window_bottom_r17, 0, 0, -0.2094F);
		window_bottom_r17.setTextureUVOffset(281, 46).addCuboid(0, 0, -15, 1, 8, 30, 0, false);

		window_top_r17 = createModelPart();
		window_top_r17.setPivot(-10.8291F, 27.8501F, 0);
		window_end_exterior_4_1.addChild(window_top_r17);
		setRotationAngle(window_top_r17, 0, 0, 0.1396F);
		window_top_r17.setTextureUVOffset(0, 86).addCuboid(0, -22, -15, 0, 22, 30, 0, false);

		window_end_exterior_4_2 = createModelPart();
		window_end_exterior_4_2.setPivot(10.1709F, -40.8501F, 0);
		window_end_exterior_4.addChild(window_end_exterior_4_2);
		window_end_exterior_4_2.setTextureUVOffset(161, 22).addCuboid(10.8291F, 27.8501F, -15, 0, 6, 30, 0, true);

		roof_5_r2 = createModelPart();
		roof_5_r2.setPivot(0, 0, 0);
		window_end_exterior_4_2.addChild(roof_5_r2);
		setRotationAngle(roof_5_r2, 0, 0, -1.0472F);
		roof_5_r2.setTextureUVOffset(267, 329).addCuboid(-1, 3, -15, 1, 3, 30, 0, true);

		roof_4_r5 = createModelPart();
		roof_4_r5.setPivot(7.7673F, 6.0642F, 0);
		window_end_exterior_4_2.addChild(roof_4_r5);
		setRotationAngle(roof_4_r5, 0, 0, -0.6981F);
		roof_4_r5.setTextureUVOffset(278, 159).addCuboid(0, -4, -15, 0, 4, 30, 0, true);

		window_bottom_r18 = createModelPart();
		window_bottom_r18.setPivot(10.8291F, 33.8501F, 0);
		window_end_exterior_4_2.addChild(window_bottom_r18);
		setRotationAngle(window_bottom_r18, 0, 0, 0.2094F);
		window_bottom_r18.setTextureUVOffset(281, 46).addCuboid(-1, 0, -15, 1, 8, 30, 0, true);

		window_top_r18 = createModelPart();
		window_top_r18.setPivot(10.8291F, 27.8501F, 0);
		window_end_exterior_4_2.addChild(window_top_r18);
		setRotationAngle(window_top_r18, 0, 0, -0.1396F);
		window_top_r18.setTextureUVOffset(0, 86).addCuboid(0, -22, -15, 0, 22, 30, 0, true);

		window_end_exterior_5 = createModelPart();
		window_end_exterior_5.setPivot(0, 24, 0);


		window_end_exterior_5_1 = createModelPart();
		window_end_exterior_5_1.setPivot(-10.1709F, -40.8501F, 0);
		window_end_exterior_5.addChild(window_end_exterior_5_1);
		window_end_exterior_5_1.setTextureUVOffset(74, 122).addCuboid(-10.8291F, 27.8501F, -15, 0, 6, 30, 0, false);

		roof_5_r3 = createModelPart();
		roof_5_r3.setPivot(0, 0, 0);
		window_end_exterior_5_1.addChild(roof_5_r3);
		setRotationAngle(roof_5_r3, 0, 0, 1.0472F);
		roof_5_r3.setTextureUVOffset(328, 253).addCuboid(0, 3, -15, 1, 3, 30, 0, false);

		roof_4_r6 = createModelPart();
		roof_4_r6.setPivot(-7.7673F, 6.0642F, 0);
		window_end_exterior_5_1.addChild(roof_4_r6);
		setRotationAngle(roof_4_r6, 0, 0, 0.6981F);
		roof_4_r6.setTextureUVOffset(278, 155).addCuboid(0, -4, -15, 0, 4, 30, 0, false);

		window_bottom_r19 = createModelPart();
		window_bottom_r19.setPivot(-10.8291F, 33.8501F, 0);
		window_end_exterior_5_1.addChild(window_bottom_r19);
		setRotationAngle(window_bottom_r19, 0, 0, -0.2094F);
		window_bottom_r19.setTextureUVOffset(233, 275).addCuboid(0, 0, -15, 1, 8, 30, 0, false);

		window_top_r19 = createModelPart();
		window_top_r19.setPivot(-10.8291F, 27.8501F, 0);
		window_end_exterior_5_1.addChild(window_top_r19);
		setRotationAngle(window_top_r19, 0, 0, 0.1396F);
		window_top_r19.setTextureUVOffset(74, 78).addCuboid(0, -22, -15, 0, 22, 30, 0, false);

		window_end_exterior_5_2 = createModelPart();
		window_end_exterior_5_2.setPivot(10.1709F, -40.8501F, 0);
		window_end_exterior_5.addChild(window_end_exterior_5_2);
		window_end_exterior_5_2.setTextureUVOffset(74, 122).addCuboid(10.8291F, 27.8501F, -15, 0, 6, 30, 0, true);

		roof_6_r1 = createModelPart();
		roof_6_r1.setPivot(0, 0, 0);
		window_end_exterior_5_2.addChild(roof_6_r1);
		setRotationAngle(roof_6_r1, 0, 0, -1.0472F);
		roof_6_r1.setTextureUVOffset(328, 253).addCuboid(-1, 3, -15, 1, 3, 30, 0, true);

		roof_5_r4 = createModelPart();
		roof_5_r4.setPivot(7.7673F, 6.0642F, 0);
		window_end_exterior_5_2.addChild(roof_5_r4);
		setRotationAngle(roof_5_r4, 0, 0, -0.6981F);
		roof_5_r4.setTextureUVOffset(278, 155).addCuboid(0, -4, -15, 0, 4, 30, 0, true);

		window_bottom_r20 = createModelPart();
		window_bottom_r20.setPivot(10.8291F, 33.8501F, 0);
		window_end_exterior_5_2.addChild(window_bottom_r20);
		setRotationAngle(window_bottom_r20, 0, 0, 0.2094F);
		window_bottom_r20.setTextureUVOffset(233, 275).addCuboid(-1, 0, -15, 1, 8, 30, 0, true);

		window_top_r20 = createModelPart();
		window_top_r20.setPivot(10.8291F, 27.8501F, 0);
		window_end_exterior_5_2.addChild(window_top_r20);
		setRotationAngle(window_top_r20, 0, 0, -0.1396F);
		window_top_r20.setTextureUVOffset(74, 78).addCuboid(0, -22, -15, 0, 22, 30, 0, true);

		window_end_exterior_6 = createModelPart();
		window_end_exterior_6.setPivot(0, 24, 0);


		window_end_exterior_6_1 = createModelPart();
		window_end_exterior_6_1.setPivot(-10.1709F, -40.8501F, 0);
		window_end_exterior_6.addChild(window_end_exterior_6_1);
		window_end_exterior_6_1.setTextureUVOffset(0, 49).addCuboid(-10.8291F, 27.8501F, -15, 0, 6, 30, 0, false);

		roof_6_r2 = createModelPart();
		roof_6_r2.setPivot(0, 0, 0);
		window_end_exterior_6_1.addChild(roof_6_r2);
		setRotationAngle(roof_6_r2, 0, 0, 1.0472F);
		roof_6_r2.setTextureUVOffset(328, 218).addCuboid(0, 3, -15, 1, 3, 30, 0, false);

		roof_5_r5 = createModelPart();
		roof_5_r5.setPivot(-7.7673F, 6.0642F, 0);
		window_end_exterior_6_1.addChild(roof_5_r5);
		setRotationAngle(roof_5_r5, 0, 0, 0.6981F);
		roof_5_r5.setTextureUVOffset(32, 269).addCuboid(0, -4, -15, 0, 4, 30, 0, false);

		window_bottom_r21 = createModelPart();
		window_bottom_r21.setPivot(-10.8291F, 33.8501F, 0);
		window_end_exterior_6_1.addChild(window_bottom_r21);
		setRotationAngle(window_bottom_r21, 0, 0, -0.2094F);
		window_bottom_r21.setTextureUVOffset(0, 274).addCuboid(0, 0, -15, 1, 8, 30, 0, false);

		window_top_r21 = createModelPart();
		window_top_r21.setPivot(-10.8291F, 27.8501F, 0);
		window_end_exterior_6_1.addChild(window_top_r21);
		setRotationAngle(window_top_r21, 0, 0, 0.1396F);
		window_top_r21.setTextureUVOffset(74, 56).addCuboid(0, -22, -15, 0, 22, 30, 0, false);

		window_end_exterior_6_2 = createModelPart();
		window_end_exterior_6_2.setPivot(10.1709F, -40.8501F, 0);
		window_end_exterior_6.addChild(window_end_exterior_6_2);
		window_end_exterior_6_2.setTextureUVOffset(0, 49).addCuboid(10.8291F, 27.8501F, -15, 0, 6, 30, 0, true);

		roof_7_r1 = createModelPart();
		roof_7_r1.setPivot(0, 0, 0);
		window_end_exterior_6_2.addChild(roof_7_r1);
		setRotationAngle(roof_7_r1, 0, 0, -1.0472F);
		roof_7_r1.setTextureUVOffset(328, 218).addCuboid(-1, 3, -15, 1, 3, 30, 0, true);

		roof_6_r3 = createModelPart();
		roof_6_r3.setPivot(7.7673F, 6.0642F, 0);
		window_end_exterior_6_2.addChild(roof_6_r3);
		setRotationAngle(roof_6_r3, 0, 0, -0.6981F);
		roof_6_r3.setTextureUVOffset(32, 269).addCuboid(0, -4, -15, 0, 4, 30, 0, true);

		window_bottom_r22 = createModelPart();
		window_bottom_r22.setPivot(10.8291F, 33.8501F, 0);
		window_end_exterior_6_2.addChild(window_bottom_r22);
		setRotationAngle(window_bottom_r22, 0, 0, 0.2094F);
		window_bottom_r22.setTextureUVOffset(0, 274).addCuboid(-1, 0, -15, 1, 8, 30, 0, true);

		window_top_r22 = createModelPart();
		window_top_r22.setPivot(10.8291F, 27.8501F, 0);
		window_end_exterior_6_2.addChild(window_top_r22);
		setRotationAngle(window_top_r22, 0, 0, -0.1396F);
		window_top_r22.setTextureUVOffset(74, 56).addCuboid(0, -22, -15, 0, 22, 30, 0, true);

		door = createModelPart();
		door.setPivot(0, 24, 0);


		door_1 = createModelPart();
		door_1.setPivot(0, 0, 0);
		door.addChild(door_1);
		door_1.setTextureUVOffset(230, 86).addCuboid(-20, -13, 3, 1, 6, 13, 0, false);

		window_bottom_2_r1 = createModelPart();
		window_bottom_2_r1.setPivot(-20, -7, 0);
		door_1.addChild(window_bottom_2_r1);
		setRotationAngle(window_bottom_2_r1, 0, 0, -0.2094F);
		window_bottom_2_r1.setTextureUVOffset(199, 0).addCuboid(0, 0, 3, 1, 8, 13, 0, false);

		window_top_2_r1 = createModelPart();
		window_top_2_r1.setPivot(-20, -13, 0);
		door_1.addChild(window_top_2_r1);
		setRotationAngle(window_top_2_r1, 0, 0, 0.1396F);
		window_top_2_r1.setTextureUVOffset(390, 235).addCuboid(0, -21, 3, 1, 21, 13, 0, false);

		door_2 = createModelPart();
		door_2.setPivot(0, 0, 0);
		door.addChild(door_2);
		door_2.setTextureUVOffset(230, 86).addCuboid(19, -13, 3, 1, 6, 13, 0, true);

		window_bottom_3_r1 = createModelPart();
		window_bottom_3_r1.setPivot(20, -7, 0);
		door_2.addChild(window_bottom_3_r1);
		setRotationAngle(window_bottom_3_r1, 0, 0, 0.2094F);
		window_bottom_3_r1.setTextureUVOffset(199, 0).addCuboid(-1, 0, 3, 1, 8, 13, 0, true);

		window_top_3_r1 = createModelPart();
		window_top_3_r1.setPivot(20, -13, 0);
		door_2.addChild(window_top_3_r1);
		setRotationAngle(window_top_3_r1, 0, 0, -0.1396F);
		window_top_3_r1.setTextureUVOffset(390, 235).addCuboid(-1, -21, 3, 1, 21, 13, 0, true);

		door_exterior = createModelPart();
		door_exterior.setPivot(0, 24, 0);


		door_exterior_1 = createModelPart();
		door_exterior_1.setPivot(0, 0, 0);
		door_exterior.addChild(door_exterior_1);
		door_exterior_1.setTextureUVOffset(221, 63).addCuboid(-20, -13, 3, 0, 6, 13, 0, false);

		window_bottom_1_r1 = createModelPart();
		window_bottom_1_r1.setPivot(-20, -7, 0);
		door_exterior_1.addChild(window_bottom_1_r1);
		setRotationAngle(window_bottom_1_r1, 0, 0, -0.2094F);
		window_bottom_1_r1.setTextureUVOffset(199, 8).addCuboid(0, 0, 3, 0, 8, 13, 0, false);

		window_top_1_r1 = createModelPart();
		window_top_1_r1.setPivot(-20, -13, 0);
		door_exterior_1.addChild(window_top_1_r1);
		setRotationAngle(window_top_1_r1, 0, 0, 0.1396F);
		window_top_1_r1.setTextureUVOffset(227, 159).addCuboid(0, -21, 3, 0, 21, 13, 0, false);

		door_exterior_2 = createModelPart();
		door_exterior_2.setPivot(0, 0, 0);
		door_exterior.addChild(door_exterior_2);
		door_exterior_2.setTextureUVOffset(221, 63).addCuboid(20, -13, 3, 0, 6, 13, 0, true);

		window_bottom_2_r2 = createModelPart();
		window_bottom_2_r2.setPivot(20, -7, 0);
		door_exterior_2.addChild(window_bottom_2_r2);
		setRotationAngle(window_bottom_2_r2, 0, 0, 0.2094F);
		window_bottom_2_r2.setTextureUVOffset(199, 8).addCuboid(0, 0, 3, 0, 8, 13, 0, true);

		window_top_2_r2 = createModelPart();
		window_top_2_r2.setPivot(20, -13, 0);
		door_exterior_2.addChild(window_top_2_r2);
		setRotationAngle(window_top_2_r2, 0, 0, -0.1396F);
		window_top_2_r2.setTextureUVOffset(227, 159).addCuboid(0, -21, 3, 0, 21, 13, 0, true);

		door_exterior_end = createModelPart();
		door_exterior_end.setPivot(0, 24, 0);


		door_exterior_end_1 = createModelPart();
		door_exterior_end_1.setPivot(0, 0, 0);
		door_exterior_end.addChild(door_exterior_end_1);
		door_exterior_end_1.setTextureUVOffset(0, 11).addCuboid(-20, -13, 3, 0, 6, 13, 0, false);

		window_bottom_2_r3 = createModelPart();
		window_bottom_2_r3.setPivot(-20, -7, 0);
		door_exterior_end_1.addChild(window_bottom_2_r3);
		setRotationAngle(window_bottom_2_r3, 0, 0, -0.2094F);
		window_bottom_2_r3.setTextureUVOffset(0, 94).addCuboid(0, 0, 3, 0, 8, 13, 0, false);

		window_top_2_r3 = createModelPart();
		window_top_2_r3.setPivot(-20, -13, 0);
		door_exterior_end_1.addChild(window_top_2_r3);
		setRotationAngle(window_top_2_r3, 0, 0, 0.1396F);
		window_top_2_r3.setTextureUVOffset(0, 73).addCuboid(0, -21, 3, 0, 21, 13, 0, false);

		door_exterior_end_2 = createModelPart();
		door_exterior_end_2.setPivot(0, 0, 0);
		door_exterior_end.addChild(door_exterior_end_2);
		door_exterior_end_2.setTextureUVOffset(0, 11).addCuboid(20, -13, 3, 0, 6, 13, 0, true);

		window_bottom_3_r2 = createModelPart();
		window_bottom_3_r2.setPivot(20, -7, 0);
		door_exterior_end_2.addChild(window_bottom_3_r2);
		setRotationAngle(window_bottom_3_r2, 0, 0, 0.2094F);
		window_bottom_3_r2.setTextureUVOffset(0, 94).addCuboid(0, 0, 3, 0, 8, 13, 0, true);

		window_top_3_r2 = createModelPart();
		window_top_3_r2.setPivot(20, -13, 0);
		door_exterior_end_2.addChild(window_top_3_r2);
		setRotationAngle(window_top_3_r2, 0, 0, -0.1396F);
		window_top_3_r2.setTextureUVOffset(0, 73).addCuboid(0, -21, 3, 0, 21, 13, 0, true);

		roof_exterior = createModelPart();
		roof_exterior.setPivot(0, 24, 0);
		roof_exterior.setTextureUVOffset(0, 86).addCuboid(-13, -39, -15, 13, 0, 30, 0, false);
		roof_exterior.setTextureUVOffset(360, 193).addCuboid(-19, 0, -15, 1, 2, 30, 0, false);

		end = createModelPart();
		end.setPivot(0, 24, 0);
		end.setTextureUVOffset(230, 76).addCuboid(9.5F, -34, 16.1F, 11, 34, 29, 0, false);
		end.setTextureUVOffset(310, 104).addCuboid(11, -34, 2.9F, 9, 34, 0, 0, false);
		end.setTextureUVOffset(227, 172).addCuboid(-20.5F, -34, 16.1F, 11, 34, 29, 0, false);
		end.setTextureUVOffset(307, 201).addCuboid(-20, -34, 2.9F, 9, 34, 0, 0, false);
		end.setTextureUVOffset(237, 0).addCuboid(-20, -36, -8, 40, 36, 0, 0, false);
		end.setTextureUVOffset(381, 0).addCuboid(-10, -34, 45, 20, 34, 0, 0, false);

		end_pillar_4_r1 = createModelPart();
		end_pillar_4_r1.setPivot(-11, 0, 2.9F);
		end.addChild(end_pillar_4_r1);
		setRotationAngle(end_pillar_4_r1, 0, 1.2217F, 0);
		end_pillar_4_r1.setTextureUVOffset(94, 381).addCuboid(0, -34, 0, 12, 34, 0, 0, false);

		end_pillar_3_r1 = createModelPart();
		end_pillar_3_r1.setPivot(11, 0, 2.9F);
		end.addChild(end_pillar_3_r1);
		setRotationAngle(end_pillar_3_r1, 0, -1.2217F, 0);
		end_pillar_3_r1.setTextureUVOffset(0, 383).addCuboid(-12, -34, 0, 12, 34, 0, 0, false);

		end_side_1 = createModelPart();
		end_side_1.setPivot(0, 0, 0);
		end.addChild(end_side_1);
		end_side_1.setTextureUVOffset(0, 0).addCuboid(-13, -34, -8, 13, 0, 53, 0, false);
		end_side_1.setTextureUVOffset(0, 170).addCuboid(-19, 0, -15, 19, 1, 60, 0, false);
		end_side_1.setTextureUVOffset(189, 324).addCuboid(-17, -34, 3, 4, 1, 13, 0, false);
		end_side_1.setTextureUVOffset(214, 0).addCuboid(-20, -13, -15, 0, 6, 7, 0, false);
		end_side_1.setTextureUVOffset(9, 7).addCuboid(-15, -36, -15, 6, 0, 7, 0, false);
		end_side_1.setTextureUVOffset(9, 0).addCuboid(-7, -35, -15, 7, 0, 7, 0, false);
		end_side_1.setTextureUVOffset(227, 193).addCuboid(-18, -31, -15, 6, 1, 7, 0, false);

		roof_3_r7 = createModelPart();
		roof_3_r7.setPivot(-7, -35, 0);
		end_side_1.addChild(roof_3_r7);
		setRotationAngle(roof_3_r7, 0, 0, 0.3491F);
		roof_3_r7.setTextureUVOffset(0, 0).addCuboid(-3, 0, -15, 3, 0, 7, 0, false);

		roof_1_r12 = createModelPart();
		roof_1_r12.setPivot(-15, -36, 0);
		end_side_1.addChild(roof_1_r12);
		setRotationAngle(roof_1_r12, 0, 0, -0.7854F);
		roof_1_r12.setTextureUVOffset(0, 7).addCuboid(-3, 0, -15, 3, 0, 7, 0, false);

		window_bottom_r23 = createModelPart();
		window_bottom_r23.setPivot(-21, -7, 0);
		end_side_1.addChild(window_bottom_r23);
		setRotationAngle(window_bottom_r23, 0, 0, -0.2094F);
		window_bottom_r23.setTextureUVOffset(56, 101).addCuboid(1, 0, -15, 0, 8, 7, 0, false);

		window_top_r23 = createModelPart();
		window_top_r23.setPivot(-21, -13, 0);
		end_side_1.addChild(window_top_r23);
		setRotationAngle(window_top_r23, 0, 0, 0.1396F);
		window_top_r23.setTextureUVOffset(56, 79).addCuboid(1, -22, -15, 0, 22, 7, 0, false);
		window_top_r23.setTextureUVOffset(237, 36).addCuboid(1, -22, 3, 2, 1, 13, 0, false);

		end_side_2 = createModelPart();
		end_side_2.setPivot(0, 0, 0);
		end.addChild(end_side_2);
		end_side_2.setTextureUVOffset(0, 0).addCuboid(0, -34, -8, 13, 0, 53, 0, true);
		end_side_2.setTextureUVOffset(0, 170).addCuboid(0, 0, -15, 19, 1, 60, 0, true);
		end_side_2.setTextureUVOffset(189, 324).addCuboid(13, -34, 3, 4, 1, 13, 0, true);
		end_side_2.setTextureUVOffset(214, 0).addCuboid(20, -13, -15, 0, 6, 7, 0, true);
		end_side_2.setTextureUVOffset(9, 7).addCuboid(9, -36, -15, 6, 0, 7, 0, true);
		end_side_2.setTextureUVOffset(9, 0).addCuboid(0, -35, -15, 7, 0, 7, 0, true);
		end_side_2.setTextureUVOffset(227, 193).addCuboid(12, -31, -15, 6, 1, 7, 0, true);

		roof_4_r7 = createModelPart();
		roof_4_r7.setPivot(7, -35, 0);
		end_side_2.addChild(roof_4_r7);
		setRotationAngle(roof_4_r7, 0, 0, -0.3491F);
		roof_4_r7.setTextureUVOffset(0, 0).addCuboid(0, 0, -15, 3, 0, 7, 0, true);

		roof_2_r13 = createModelPart();
		roof_2_r13.setPivot(15, -36, 0);
		end_side_2.addChild(roof_2_r13);
		setRotationAngle(roof_2_r13, 0, 0, 0.7854F);
		roof_2_r13.setTextureUVOffset(0, 7).addCuboid(0, 0, -15, 3, 0, 7, 0, true);

		window_bottom_r24 = createModelPart();
		window_bottom_r24.setPivot(21, -7, 0);
		end_side_2.addChild(window_bottom_r24);
		setRotationAngle(window_bottom_r24, 0, 0, 0.2094F);
		window_bottom_r24.setTextureUVOffset(56, 101).addCuboid(-1, 0, -15, 0, 8, 7, 0, true);

		window_top_r24 = createModelPart();
		window_top_r24.setPivot(21, -13, 0);
		end_side_2.addChild(window_top_r24);
		setRotationAngle(window_top_r24, 0, 0, -0.1396F);
		window_top_r24.setTextureUVOffset(56, 79).addCuboid(-1, -22, -15, 0, 22, 7, 0, true);
		window_top_r24.setTextureUVOffset(237, 36).addCuboid(-3, -22, 3, 2, 1, 13, 0, true);

		end_light = createModelPart();
		end_light.setPivot(0, 24, 0);
		end_light.setTextureUVOffset(317, 44).addCuboid(-7, -35.1F, -15, 14, 0, 4, 0, false);

		end_translucent = createModelPart();
		end_translucent.setPivot(0, 24, 0);
		end_translucent.setTextureUVOffset(331, 388).addCuboid(-7, -34, -8, 14, 34, 0, 0, false);
		end_translucent.setTextureUVOffset(359, 388).addCuboid(-7, -34, 45, 14, 34, 0, 0, false);

		end_exterior = createModelPart();
		end_exterior.setPivot(0, 24, 0);


		end_exterior_side_1 = createModelPart();
		end_exterior_side_1.setPivot(0, 0, 0);
		end_exterior.addChild(end_exterior_side_1);
		end_exterior_side_1.setTextureUVOffset(362, 125).addCuboid(-21, -13, -15, 1, 6, 18, 0, false);
		end_exterior_side_1.setTextureUVOffset(360, 158).addCuboid(-21, -13, 16, 1, 6, 29, 0, false);
		end_exterior_side_1.setTextureUVOffset(325, 188).addCuboid(-21, 0, 3, 2, 1, 13, 0, false);
		end_exterior_side_1.setTextureUVOffset(303, 385).addCuboid(-20.5F, -34, 45, 11, 34, 0, 0, false);
		end_exterior_side_1.setTextureUVOffset(32, 274).addCuboid(-18, -39, 45, 18, 5, 0, 0, false);

		roof_4_r8 = createModelPart();
		roof_4_r8.setPivot(-10.1709F, -40.8501F, 0);
		end_exterior_side_1.addChild(roof_4_r8);
		setRotationAngle(roof_4_r8, 0, 0, 1.0472F);
		roof_4_r8.setTextureUVOffset(235, 321).addCuboid(0, 3, -15, 1, 3, 30, 0, false);
		roof_4_r8.setTextureUVOffset(319, 0).addCuboid(0, 3, 15, 1, 3, 30, 0, false);

		roof_3_r8 = createModelPart();
		roof_3_r8.setPivot(-17.9382F, -34.7859F, 0);
		end_exterior_side_1.addChild(roof_3_r8);
		setRotationAngle(roof_3_r8, 0, 0, 0.6981F);
		roof_3_r8.setTextureUVOffset(204, 241).addCuboid(0, -4, -15, 0, 4, 30, 0, false);
		roof_3_r8.setTextureUVOffset(0, 387).addCuboid(0, -4, 15, 1, 4, 30, 0, false);

		door_top_r1 = createModelPart();
		door_top_r1.setPivot(-21, -13, 0);
		end_exterior_side_1.addChild(door_top_r1);
		setRotationAngle(door_top_r1, 0, 0, 0.1396F);
		door_top_r1.setTextureUVOffset(265, 275).addCuboid(0, -22, 3, 1, 1, 13, 0, false);
		door_top_r1.setTextureUVOffset(173, 265).addCuboid(0, -22, 16, 1, 22, 29, 0, false);
		door_top_r1.setTextureUVOffset(265, 369).addCuboid(0, -22, -15, 1, 22, 18, 0, false);

		window_bottom_2_r4 = createModelPart();
		window_bottom_2_r4.setPivot(-21, -7, 0);
		end_exterior_side_1.addChild(window_bottom_2_r4);
		setRotationAngle(window_bottom_2_r4, 0, 0, -0.2094F);
		window_bottom_2_r4.setTextureUVOffset(127, 319).addCuboid(0, 0, 16, 1, 8, 29, 0, false);
		window_bottom_2_r4.setTextureUVOffset(360, 251).addCuboid(0, 0, -15, 1, 8, 18, 0, false);

		end_exterior_side_2 = createModelPart();
		end_exterior_side_2.setPivot(0, 0, 0);
		end_exterior.addChild(end_exterior_side_2);
		end_exterior_side_2.setTextureUVOffset(362, 125).addCuboid(20, -13, -15, 1, 6, 18, 0, true);
		end_exterior_side_2.setTextureUVOffset(360, 158).addCuboid(20, -13, 16, 1, 6, 29, 0, true);
		end_exterior_side_2.setTextureUVOffset(325, 188).addCuboid(19, 0, 3, 2, 1, 13, 0, true);
		end_exterior_side_2.setTextureUVOffset(303, 385).addCuboid(9.5F, -34, 45, 11, 34, 0, 0, true);
		end_exterior_side_2.setTextureUVOffset(32, 274).addCuboid(0, -39, 45, 18, 5, 0, 0, true);

		roof_5_r6 = createModelPart();
		roof_5_r6.setPivot(10.1709F, -40.8501F, 0);
		end_exterior_side_2.addChild(roof_5_r6);
		setRotationAngle(roof_5_r6, 0, 0, -1.0472F);
		roof_5_r6.setTextureUVOffset(235, 321).addCuboid(-1, 3, -15, 1, 3, 30, 0, true);
		roof_5_r6.setTextureUVOffset(319, 0).addCuboid(-1, 3, 15, 1, 3, 30, 0, true);

		roof_4_r9 = createModelPart();
		roof_4_r9.setPivot(17.9382F, -34.7859F, 0);
		end_exterior_side_2.addChild(roof_4_r9);
		setRotationAngle(roof_4_r9, 0, 0, -0.6981F);
		roof_4_r9.setTextureUVOffset(204, 241).addCuboid(0, -4, -15, 0, 4, 30, 0, true);
		roof_4_r9.setTextureUVOffset(0, 387).addCuboid(-1, -4, 15, 1, 4, 30, 0, true);

		door_top_r2 = createModelPart();
		door_top_r2.setPivot(21, -13, 0);
		end_exterior_side_2.addChild(door_top_r2);
		setRotationAngle(door_top_r2, 0, 0, -0.1396F);
		door_top_r2.setTextureUVOffset(265, 275).addCuboid(-1, -22, 3, 1, 1, 13, 0, true);
		door_top_r2.setTextureUVOffset(173, 265).addCuboid(-1, -22, 16, 1, 22, 29, 0, true);
		door_top_r2.setTextureUVOffset(265, 369).addCuboid(-1, -22, -15, 1, 22, 18, 0, true);

		window_bottom_3_r3 = createModelPart();
		window_bottom_3_r3.setPivot(21, -7, 0);
		end_exterior_side_2.addChild(window_bottom_3_r3);
		setRotationAngle(window_bottom_3_r3, 0, 0, 0.2094F);
		window_bottom_3_r3.setTextureUVOffset(127, 319).addCuboid(-1, 0, 16, 1, 8, 29, 0, true);
		window_bottom_3_r3.setTextureUVOffset(360, 251).addCuboid(-1, 0, -15, 1, 8, 18, 0, true);

		roof_vent = createModelPart();
		roof_vent.setPivot(0, 24, 0);


		roof_vent_side_1 = createModelPart();
		roof_vent_side_1.setPivot(0, -42, 10);
		roof_vent.addChild(roof_vent_side_1);
		roof_vent_side_1.setTextureUVOffset(76, 96).addCuboid(-6, 0, -70, 6, 4, 70, 0, false);

		vent_4_r1 = createModelPart();
		vent_4_r1.setPivot(0, 0, 0);
		roof_vent_side_1.addChild(vent_4_r1);
		setRotationAngle(vent_4_r1, -0.6981F, 0, 0);
		vent_4_r1.setTextureUVOffset(67, 53).addCuboid(-6, 0, 0, 6, 0, 5, 0, false);

		vent_3_r1 = createModelPart();
		vent_3_r1.setPivot(-6, 0, 0);
		roof_vent_side_1.addChild(vent_3_r1);
		setRotationAngle(vent_3_r1, 0, 0.6981F, 1.1345F);
		vent_3_r1.setTextureUVOffset(72, 58).addCuboid(0, 0, 0, 1, 6, 5, 0, false);

		vent_1_r1 = createModelPart();
		vent_1_r1.setPivot(-6, 0, -10);
		roof_vent_side_1.addChild(vent_1_r1);
		setRotationAngle(vent_1_r1, 0, 0, 1.1345F);
		vent_1_r1.setTextureUVOffset(161, 0).addCuboid(0, 0, -60, 3, 6, 70, 0, false);

		roof_vent_side_2 = createModelPart();
		roof_vent_side_2.setPivot(0, -42, 10);
		roof_vent.addChild(roof_vent_side_2);
		roof_vent_side_2.setTextureUVOffset(76, 96).addCuboid(0, 0, -70, 6, 4, 70, 0, true);

		vent_5_r1 = createModelPart();
		vent_5_r1.setPivot(0, 0, 0);
		roof_vent_side_2.addChild(vent_5_r1);
		setRotationAngle(vent_5_r1, -0.6981F, 0, 0);
		vent_5_r1.setTextureUVOffset(67, 53).addCuboid(0, 0, 0, 6, 0, 5, 0, true);

		vent_4_r2 = createModelPart();
		vent_4_r2.setPivot(6, 0, 0);
		roof_vent_side_2.addChild(vent_4_r2);
		setRotationAngle(vent_4_r2, 0, -0.6981F, -1.1345F);
		vent_4_r2.setTextureUVOffset(72, 58).addCuboid(-1, 0, 0, 1, 6, 5, 0, true);

		vent_2_r1 = createModelPart();
		vent_2_r1.setPivot(6, 0, -10);
		roof_vent_side_2.addChild(vent_2_r1);
		setRotationAngle(vent_2_r1, 0, 0, -1.1345F);
		vent_2_r1.setTextureUVOffset(161, 0).addCuboid(-3, 0, -60, 3, 6, 70, 0, true);

		head_exterior = createModelPart();
		head_exterior.setPivot(0, 24, 0);


		head_side_1 = createModelPart();
		head_side_1.setPivot(0, 0, 0);
		head_exterior.addChild(head_side_1);
		head_side_1.setTextureUVOffset(155, 97).addCuboid(-19, 0, -15, 1, 2, 73, 0, false);
		head_side_1.setTextureUVOffset(158, 192).addCuboid(-21, -13, -15, 1, 6, 67, 0, false);
		head_side_1.setTextureUVOffset(279, 44).addCuboid(-21, 0, -57, 2, 1, 13, 0, false);
		head_side_1.setTextureUVOffset(125, 356).addCuboid(-21, -13, -44, 1, 6, 29, 0, false);
		head_side_1.setTextureUVOffset(361, 286).addCuboid(-21, -13, -75, 1, 6, 18, 0, false);

		roof_4_r10 = createModelPart();
		roof_4_r10.setPivot(-10.1709F, -40.8501F, 0);
		head_side_1.addChild(roof_4_r10);
		setRotationAngle(roof_4_r10, 0, 0, 1.0472F);
		roof_4_r10.setTextureUVOffset(157, 326).addCuboid(0, 3, -75, 1, 3, 30, 0, false);
		roof_4_r10.setTextureUVOffset(328, 185).addCuboid(0, 3, -45, 1, 3, 30, 0, false);

		roof_3_r9 = createModelPart();
		roof_3_r9.setPivot(-17.9382F, -34.7859F, 0);
		head_side_1.addChild(roof_3_r9);
		setRotationAngle(roof_3_r9, 0, 0, 0.6981F);
		roof_3_r9.setTextureUVOffset(32, 261).addCuboid(0, -4, -75, 0, 4, 30, 0, false);
		roof_3_r9.setTextureUVOffset(32, 265).addCuboid(0, -4, -45, 0, 4, 30, 0, false);

		window_top_1_r2 = createModelPart();
		window_top_1_r2.setPivot(-21, -13, 0);
		head_side_1.addChild(window_top_1_r2);
		setRotationAngle(window_top_1_r2, 0, 0, 0.1396F);
		window_top_1_r2.setTextureUVOffset(362, 74).addCuboid(0, -22, -75, 1, 22, 18, 0, false);
		window_top_1_r2.setTextureUVOffset(113, 259).addCuboid(0, -22, -44, 1, 22, 29, 0, false);
		window_top_1_r2.setTextureUVOffset(237, 50).addCuboid(0, -22, -57, 1, 1, 13, 0, false);
		window_top_1_r2.setTextureUVOffset(93, 172).addCuboid(0, -22, -15, 1, 22, 65, 0, false);

		window_bottom_1_r2 = createModelPart();
		window_bottom_1_r2.setPivot(-21, -7, 0);
		head_side_1.addChild(window_bottom_1_r2);
		setRotationAngle(window_bottom_1_r2, 0, 0, -0.2094F);
		window_bottom_1_r2.setTextureUVOffset(161, 0).addCuboid(0, 0, -75, 1, 8, 18, 0, false);
		window_bottom_1_r2.setTextureUVOffset(62, 319).addCuboid(0, 0, -44, 1, 8, 29, 0, false);
		window_bottom_1_r2.setTextureUVOffset(0, 86).addCuboid(0, 0, -15, 1, 8, 72, 0, false);

		end_r1 = createModelPart();
		end_r1.setPivot(-19, 2, 33);
		head_side_1.addChild(end_r1);
		setRotationAngle(end_r1, 0, 0, -0.2618F);
		end_r1.setTextureUVOffset(204, 256).addCuboid(0, 0, 0, 0, 11, 26, 0, false);

		roof_11_r1 = createModelPart();
		roof_11_r1.setPivot(-16.8732F, -11.8177F, 56.7312F);
		head_side_1.addChild(roof_11_r1);
		setRotationAngle(roof_11_r1, 0.1745F, -1.0472F, 0);
		roof_11_r1.setTextureUVOffset(361, 328).addCuboid(-9.5F, -3, 0, 19, 15, 0, 0, false);

		roof_12_r1 = createModelPart();
		roof_12_r1.setPivot(-15.5128F, 10.8301F, 58.2552F);
		head_side_1.addChild(roof_12_r1);
		setRotationAngle(roof_12_r1, 0, -1.0472F, 0);
		roof_12_r1.setTextureUVOffset(161, 26).addCuboid(-3.5F, -1.5F, 0, 7, 3, 0, 0, false);

		roof_11_r2 = createModelPart();
		roof_11_r2.setPivot(-15.5954F, 7.1646F, 60.6123F);
		head_side_1.addChild(roof_11_r2);
		setRotationAngle(roof_11_r2, -0.5236F, -1.0472F, 0);
		roof_11_r2.setTextureUVOffset(134, 91).addCuboid(-5.5F, -2.5F, 0, 11, 5, 0, 0, false);

		roof_10_r1 = createModelPart();
		roof_10_r1.setPivot(-16.9278F, 3, 60.8042F);
		head_side_1.addChild(roof_10_r1);
		setRotationAngle(roof_10_r1, 0, -1.0472F, 0);
		roof_10_r1.setTextureUVOffset(134, 86).addCuboid(-6, -3, 0, 12, 5, 0, 0, false);

		roof_10_r2 = createModelPart();
		roof_10_r2.setPivot(-7, 0, 70);
		head_side_1.addChild(roof_10_r2);
		setRotationAngle(roof_10_r2, 0.2618F, -0.5236F, 0);
		roof_10_r2.setTextureUVOffset(218, 86).addCuboid(-9, -11, 0, 9, 11, 0, 0, false);

		roof_11_r3 = createModelPart();
		roof_11_r3.setPivot(-10.0747F, 10.7433F, 63.3255F);
		head_side_1.addChild(roof_11_r3);
		setRotationAngle(roof_11_r3, 0, -0.5236F, 0);
		roof_11_r3.setTextureUVOffset(28, 50).addCuboid(-5, -1.5F, 0, 10, 3, 0, 0, false);

		roof_10_r3 = createModelPart();
		roof_10_r3.setPivot(-7, 5, 70);
		head_side_1.addChild(roof_10_r3);
		setRotationAngle(roof_10_r3, -0.7854F, -0.5236F, 0);
		roof_10_r3.setTextureUVOffset(279, 58).addCuboid(-11, 0, 0, 11, 6, 0, 0, false);

		roof_9_r1 = createModelPart();
		roof_9_r1.setPivot(-7, 0, 70);
		head_side_1.addChild(roof_9_r1);
		setRotationAngle(roof_9_r1, 0, -0.5236F, 0);
		roof_9_r1.setTextureUVOffset(161, 13).addCuboid(-8, 0, 0, 8, 5, 0, 0, false);

		roof_7_r2 = createModelPart();
		roof_7_r2.setPivot(-18.5824F, -19.5437F, 43.232F);
		head_side_1.addChild(roof_7_r2);
		setRotationAngle(roof_7_r2, -0.2182F, 0.5236F, 1.0472F);
		roof_7_r2.setTextureUVOffset(118, 360).addCuboid(0, -8, -28.5F, 0, 11, 57, 0, false);

		roof_4_r11 = createModelPart();
		roof_4_r11.setPivot(-2, -42, -9);
		head_side_1.addChild(roof_4_r11);
		setRotationAngle(roof_4_r11, 0, 0, 1.5708F);
		roof_4_r11.setTextureUVOffset(265, 289).addCuboid(0, -2, -6, 1, 4, 12, 0, false);

		roof_4_r12 = createModelPart();
		roof_4_r12.setPivot(-8.1369F, -39.4953F, 9.8171F);
		head_side_1.addChild(roof_4_r12);
		setRotationAngle(roof_4_r12, -0.1309F, 0.1309F, 1.3963F);
		roof_4_r12.setTextureUVOffset(32, 292).addCuboid(0, -3.5F, -13.5F, 0, 7, 27, 0, false);

		roof_3_r10 = createModelPart();
		roof_3_r10.setPivot(-13.6527F, -37.467F, 10.5898F);
		head_side_1.addChild(roof_3_r10);
		setRotationAngle(roof_3_r10, -0.0873F, 0.0873F, 1.0472F);
		roof_3_r10.setTextureUVOffset(204, 247).addCuboid(0, -3.5F, -14, 0, 7, 28, 0, false);

		roof_2_r14 = createModelPart();
		roof_2_r14.setPivot(-17.2178F, -34.6364F, 11.8407F);
		head_side_1.addChild(roof_2_r14);
		setRotationAngle(roof_2_r14, -0.0436F, 0.0436F, 0.6981F);
		roof_2_r14.setTextureUVOffset(204, 235).addCuboid(0, -3, -15, 0, 6, 30, 0, false);

		roof_3_r11 = createModelPart();
		roof_3_r11.setPivot(-6.6372F, -40.9654F, -9);
		head_side_1.addChild(roof_3_r11);
		setRotationAngle(roof_3_r11, 0, 0, 1.3963F);
		roof_3_r11.setTextureUVOffset(230, 139).addCuboid(-0.5F, -3.5F, -6, 1, 7, 12, 0, false);

		roof_2_r15 = createModelPart();
		roof_2_r15.setPivot(-12.519F, -38.9171F, -9);
		head_side_1.addChild(roof_2_r15);
		setRotationAngle(roof_2_r15, 0, 0, 1.0472F);
		roof_2_r15.setTextureUVOffset(0, 274).addCuboid(-0.5F, -3, -6, 1, 6, 12, 0, false);

		roof_1_r13 = createModelPart();
		roof_1_r13.setPivot(-16.2696F, -35.9966F, -9);
		head_side_1.addChild(roof_1_r13);
		setRotationAngle(roof_1_r13, 0, 0, 0.6981F);
		roof_1_r13.setTextureUVOffset(296, 243).addCuboid(-0.5F, -2, -6, 1, 4, 12, 0, false);

		head_side_2 = createModelPart();
		head_side_2.setPivot(0, 0, 0);
		head_exterior.addChild(head_side_2);
		head_side_2.setTextureUVOffset(155, 97).addCuboid(18, 0, -15, 1, 2, 73, 0, true);
		head_side_2.setTextureUVOffset(158, 192).addCuboid(20, -13, -15, 1, 6, 67, 0, true);
		head_side_2.setTextureUVOffset(279, 44).addCuboid(19, 0, -57, 2, 1, 13, 0, true);
		head_side_2.setTextureUVOffset(125, 356).addCuboid(20, -13, -44, 1, 6, 29, 0, true);
		head_side_2.setTextureUVOffset(361, 286).addCuboid(20, -13, -75, 1, 6, 18, 0, true);

		roof_5_r7 = createModelPart();
		roof_5_r7.setPivot(10.1709F, -40.8501F, 0);
		head_side_2.addChild(roof_5_r7);
		setRotationAngle(roof_5_r7, 0, 0, -1.0472F);
		roof_5_r7.setTextureUVOffset(157, 326).addCuboid(-1, 3, -75, 1, 3, 30, 0, true);
		roof_5_r7.setTextureUVOffset(328, 185).addCuboid(-1, 3, -45, 1, 3, 30, 0, true);

		roof_4_r13 = createModelPart();
		roof_4_r13.setPivot(17.9382F, -34.7859F, 0);
		head_side_2.addChild(roof_4_r13);
		setRotationAngle(roof_4_r13, 0, 0, -0.6981F);
		roof_4_r13.setTextureUVOffset(32, 261).addCuboid(0, -4, -75, 0, 4, 30, 0, true);
		roof_4_r13.setTextureUVOffset(32, 265).addCuboid(0, -4, -45, 0, 4, 30, 0, true);

		window_top_2_r4 = createModelPart();
		window_top_2_r4.setPivot(21, -13, 0);
		head_side_2.addChild(window_top_2_r4);
		setRotationAngle(window_top_2_r4, 0, 0, -0.1396F);
		window_top_2_r4.setTextureUVOffset(362, 74).addCuboid(-1, -22, -75, 1, 22, 18, 0, true);
		window_top_2_r4.setTextureUVOffset(113, 259).addCuboid(-1, -22, -44, 1, 22, 29, 0, true);
		window_top_2_r4.setTextureUVOffset(237, 50).addCuboid(-1, -22, -57, 1, 1, 13, 0, true);
		window_top_2_r4.setTextureUVOffset(93, 172).addCuboid(-1, -22, -15, 1, 22, 65, 0, true);

		window_bottom_2_r5 = createModelPart();
		window_bottom_2_r5.setPivot(21, -7, 0);
		head_side_2.addChild(window_bottom_2_r5);
		setRotationAngle(window_bottom_2_r5, 0, 0, 0.2094F);
		window_bottom_2_r5.setTextureUVOffset(161, 0).addCuboid(-1, 0, -75, 1, 8, 18, 0, true);
		window_bottom_2_r5.setTextureUVOffset(62, 319).addCuboid(-1, 0, -44, 1, 8, 29, 0, true);
		window_bottom_2_r5.setTextureUVOffset(0, 86).addCuboid(-1, 0, -15, 1, 8, 72, 0, true);

		end_r2 = createModelPart();
		end_r2.setPivot(19, 2, 33);
		head_side_2.addChild(end_r2);
		setRotationAngle(end_r2, 0, 0, 0.2618F);
		end_r2.setTextureUVOffset(204, 256).addCuboid(0, 0, 0, 0, 11, 26, 0, true);

		roof_12_r2 = createModelPart();
		roof_12_r2.setPivot(16.8732F, -11.8177F, 56.7312F);
		head_side_2.addChild(roof_12_r2);
		setRotationAngle(roof_12_r2, 0.1745F, 1.0472F, 0);
		roof_12_r2.setTextureUVOffset(361, 328).addCuboid(-9.5F, -3, 0, 19, 15, 0, 0, true);

		roof_13_r1 = createModelPart();
		roof_13_r1.setPivot(15.5128F, 10.8301F, 58.2552F);
		head_side_2.addChild(roof_13_r1);
		setRotationAngle(roof_13_r1, 0, 1.0472F, 0);
		roof_13_r1.setTextureUVOffset(161, 26).addCuboid(-3.5F, -1.5F, 0, 7, 3, 0, 0, true);

		roof_12_r3 = createModelPart();
		roof_12_r3.setPivot(15.5954F, 7.1646F, 60.6123F);
		head_side_2.addChild(roof_12_r3);
		setRotationAngle(roof_12_r3, -0.5236F, 1.0472F, 0);
		roof_12_r3.setTextureUVOffset(134, 91).addCuboid(-5.5F, -2.5F, 0, 11, 5, 0, 0, true);

		roof_11_r4 = createModelPart();
		roof_11_r4.setPivot(16.9278F, 3, 60.8042F);
		head_side_2.addChild(roof_11_r4);
		setRotationAngle(roof_11_r4, 0, 1.0472F, 0);
		roof_11_r4.setTextureUVOffset(134, 86).addCuboid(-6, -3, 0, 12, 5, 0, 0, true);

		roof_11_r5 = createModelPart();
		roof_11_r5.setPivot(7, 0, 70);
		head_side_2.addChild(roof_11_r5);
		setRotationAngle(roof_11_r5, 0.2618F, 0.5236F, 0);
		roof_11_r5.setTextureUVOffset(218, 86).addCuboid(0, -11, 0, 9, 11, 0, 0, true);

		roof_12_r4 = createModelPart();
		roof_12_r4.setPivot(10.0747F, 10.7433F, 63.3255F);
		head_side_2.addChild(roof_12_r4);
		setRotationAngle(roof_12_r4, 0, 0.5236F, 0);
		roof_12_r4.setTextureUVOffset(28, 50).addCuboid(-5, -1.5F, 0, 10, 3, 0, 0, true);

		roof_11_r6 = createModelPart();
		roof_11_r6.setPivot(7, 5, 70);
		head_side_2.addChild(roof_11_r6);
		setRotationAngle(roof_11_r6, -0.7854F, 0.5236F, 0);
		roof_11_r6.setTextureUVOffset(279, 58).addCuboid(0, 0, 0, 11, 6, 0, 0, true);

		roof_10_r4 = createModelPart();
		roof_10_r4.setPivot(7, 0, 70);
		head_side_2.addChild(roof_10_r4);
		setRotationAngle(roof_10_r4, 0, 0.5236F, 0);
		roof_10_r4.setTextureUVOffset(161, 13).addCuboid(0, 0, 0, 8, 5, 0, 0, true);

		roof_8_r1 = createModelPart();
		roof_8_r1.setPivot(18.5824F, -19.5437F, 43.232F);
		head_side_2.addChild(roof_8_r1);
		setRotationAngle(roof_8_r1, -0.2182F, -0.5236F, -1.0472F);
		roof_8_r1.setTextureUVOffset(118, 360).addCuboid(0, -8, -28.5F, 0, 11, 57, 0, true);

		roof_5_r8 = createModelPart();
		roof_5_r8.setPivot(2, -42, -9);
		head_side_2.addChild(roof_5_r8);
		setRotationAngle(roof_5_r8, 0, 0, -1.5708F);
		roof_5_r8.setTextureUVOffset(265, 289).addCuboid(-1, -2, -6, 1, 4, 12, 0, true);

		roof_5_r9 = createModelPart();
		roof_5_r9.setPivot(8.1369F, -39.4953F, 9.8171F);
		head_side_2.addChild(roof_5_r9);
		setRotationAngle(roof_5_r9, -0.1309F, -0.1309F, -1.3963F);
		roof_5_r9.setTextureUVOffset(32, 292).addCuboid(0, -3.5F, -13.5F, 0, 7, 27, 0, true);

		roof_4_r14 = createModelPart();
		roof_4_r14.setPivot(13.6527F, -37.467F, 10.5898F);
		head_side_2.addChild(roof_4_r14);
		setRotationAngle(roof_4_r14, -0.0873F, -0.0873F, -1.0472F);
		roof_4_r14.setTextureUVOffset(204, 247).addCuboid(0, -3.5F, -14, 0, 7, 28, 0, true);

		roof_3_r12 = createModelPart();
		roof_3_r12.setPivot(17.2178F, -34.6364F, 11.8407F);
		head_side_2.addChild(roof_3_r12);
		setRotationAngle(roof_3_r12, -0.0436F, -0.0436F, -0.6981F);
		roof_3_r12.setTextureUVOffset(204, 235).addCuboid(0, -3, -15, 0, 6, 30, 0, true);

		roof_4_r15 = createModelPart();
		roof_4_r15.setPivot(6.6372F, -40.9654F, -9);
		head_side_2.addChild(roof_4_r15);
		setRotationAngle(roof_4_r15, 0, 0, -1.3963F);
		roof_4_r15.setTextureUVOffset(230, 139).addCuboid(-0.5F, -3.5F, -6, 1, 7, 12, 0, true);

		roof_3_r13 = createModelPart();
		roof_3_r13.setPivot(12.519F, -38.9171F, -9);
		head_side_2.addChild(roof_3_r13);
		setRotationAngle(roof_3_r13, 0, 0, -1.0472F);
		roof_3_r13.setTextureUVOffset(0, 274).addCuboid(-0.5F, -3, -6, 1, 6, 12, 0, true);

		roof_2_r16 = createModelPart();
		roof_2_r16.setPivot(16.2696F, -35.9966F, -9);
		head_side_2.addChild(roof_2_r16);
		setRotationAngle(roof_2_r16, 0, 0, -0.6981F);
		roof_2_r16.setTextureUVOffset(296, 243).addCuboid(-0.5F, -2, -6, 1, 4, 12, 0, true);

		middle = createModelPart();
		middle.setPivot(0, 0, 0);
		head_exterior.addChild(middle);
		middle.setTextureUVOffset(181, 8).addCuboid(-7, 0, 70, 14, 5, 0, 0, false);
		middle.setTextureUVOffset(0, 50).addCuboid(-7, 9.2433F, 65.7571F, 14, 3, 0, 0, false);
		middle.setTextureUVOffset(0, 0).addCuboid(-19, 0, -15, 38, 1, 85, 0, false);
		middle.setTextureUVOffset(0, 231).addCuboid(-20, -42, -14.5F, 40, 42, 1, 0, false);

		roof_8_r2 = createModelPart();
		roof_8_r2.setPivot(0, 0, 70);
		middle.addChild(roof_8_r2);
		setRotationAngle(roof_8_r2, 0.3491F, 0, 0);
		roof_8_r2.setTextureUVOffset(32, 326).addCuboid(-8, -9, 0, 16, 9, 0, 0, false);

		roof_6_r4 = createModelPart();
		roof_6_r4.setPivot(-10, -37.8324F, 20.6354F);
		middle.addChild(roof_6_r4);
		setRotationAngle(roof_6_r4, 0, -0.5672F, -1.5708F);
		roof_6_r4.setTextureUVOffset(118, 336).addCuboid(0, -3, 0, 0, 26, 55, 0, false);

		roof_5_r10 = createModelPart();
		roof_5_r10.setPivot(0, -42, -3);
		middle.addChild(roof_5_r10);
		setRotationAngle(roof_5_r10, 0, -0.1745F, -1.5708F);
		roof_5_r10.setTextureUVOffset(144, 241).addCuboid(0, -10, 0, 0, 20, 24, 0, false);

		roof_9_r2 = createModelPart();
		roof_9_r2.setPivot(0, 5, 70);
		middle.addChild(roof_9_r2);
		setRotationAngle(roof_9_r2, -0.7854F, 0, 0);
		roof_9_r2.setTextureUVOffset(230, 158).addCuboid(-7, 0, 0, 14, 6, 0, 0, false);

		bottom_middle = createModelPart();
		bottom_middle.setPivot(0, 24, 0);
		bottom_middle.setTextureUVOffset(264, 235).addCuboid(-17, 2, -15, 1, 10, 30, 0, false);

		bottom_end = createModelPart();
		bottom_end.setPivot(0, 24, 0);
		bottom_end.setTextureUVOffset(0, 0).addCuboid(-17, 2, -7, 1, 10, 14, 0, false);

		seat = createModelPart();
		seat.setPivot(0, 24, 0);
		seat.setTextureUVOffset(181, 0).addCuboid(-4, -6, -4, 8, 1, 7, 0, false);
		seat.setTextureUVOffset(214, 0).addCuboid(-3.5F, -22.644F, 4.0686F, 7, 5, 1, 0, false);

		seat_2_r1 = createModelPart();
		seat_2_r1.setPivot(0, -6, 2);
		seat.addChild(seat_2_r1);
		setRotationAngle(seat_2_r1, -0.1745F, 0, 0);
		seat_2_r1.setTextureUVOffset(161, 0).addCuboid(-4, -12, 0, 8, 12, 1, 0, false);

		headlights = createModelPart();
		headlights.setPivot(0, 24, 0);


		headlight_2_r1 = createModelPart();
		headlight_2_r1.setPivot(18.5824F, -19.5437F, 43.232F);
		headlights.addChild(headlight_2_r1);
		setRotationAngle(headlight_2_r1, -0.2182F, -0.5236F, -1.0472F);
		headlight_2_r1.setTextureUVOffset(345, 53).addCuboid(0.1F, -8, 10.5F, 0, 6, 13, 0, true);

		headlight_1_r1 = createModelPart();
		headlight_1_r1.setPivot(-18.5824F, -19.5437F, 43.232F);
		headlights.addChild(headlight_1_r1);
		setRotationAngle(headlight_1_r1, -0.2182F, 0.5236F, 1.0472F);
		headlight_1_r1.setTextureUVOffset(345, 53).addCuboid(-0.1F, -8, 10.5F, 0, 6, 13, 0, false);

		tail_lights = createModelPart();
		tail_lights.setPivot(0, 24, 0);


		tail_light_2_r1 = createModelPart();
		tail_light_2_r1.setPivot(18.5824F, -19.5437F, 43.232F);
		tail_lights.addChild(tail_light_2_r1);
		setRotationAngle(tail_light_2_r1, -0.2182F, -0.5236F, -1.0472F);
		tail_light_2_r1.setTextureUVOffset(345, 59).addCuboid(0.1F, -8, 10.5F, 0, 6, 13, 0, true);

		tail_light_1_r1 = createModelPart();
		tail_light_1_r1.setPivot(-18.5824F, -19.5437F, 43.232F);
		tail_lights.addChild(tail_light_1_r1);
		setRotationAngle(tail_light_1_r1, -0.2182F, 0.5236F, 1.0472F);
		tail_light_1_r1.setTextureUVOffset(345, 59).addCuboid(-0.1F, -8, 10.5F, 0, 6, 13, 0, false);

		door_light_off = createModelPart();
		door_light_off.setPivot(0, 24, 0);


		door_light_off_r1 = createModelPart();
		door_light_off_r1.setPivot(-21, -13, 0);
		door_light_off.addChild(door_light_off_r1);
		setRotationAngle(door_light_off_r1, 0, 0, 0.1396F);
		door_light_off_r1.setTextureUVOffset(0, 0).addCuboid(-0.5F, -21.5F, -0.5F, 1, 1, 1, 0, false);

		door_light_on = createModelPart();
		door_light_on.setPivot(0, 24, 0);


		door_light_on_r1 = createModelPart();
		door_light_on_r1.setPivot(-21, -13, 0);
		door_light_on.addChild(door_light_on_r1);
		setRotationAngle(door_light_on_r1, 0, 0, 0.1396F);
		door_light_on_r1.setTextureUVOffset(0, 2).addCuboid(-0.5F, -21.5F, -0.5F, 1, 1, 1, 0, false);

		buildModel();
	}

	private static final int DOOR_MAX = 13;

	@Override
	public ModelClass802 createNew(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		return new ModelClass802(doorAnimationType, renderDoorOverlay);
	}

	@Override
	protected void renderWindowPositions(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
		final ModelPartExtension[] windowParts = isEnd1Head || isEnd2Head ? windowEndParts() : windowParts();
		final int loopStart = getBogiePositions()[0] + (isEnd1Head ? 94 : 4);
		final int loopEnd = getBogiePositions()[1] - (isEnd2Head ? 94 : 4);

		switch (renderStage) {
			case LIGHT:
				for (int i = loopStart; i <= loopEnd; i += 30) {
					renderMirror(window_light, graphicsHolder, light, i);
				}
				break;
			case INTERIOR:
				int j = 0;
				for (int i = loopStart; i <= loopEnd; i += 30) {
					renderMirror(window, graphicsHolder, light, i);
					if (renderDetails) {
						if (j % 4 < 2) {
							renderOnce(seat, graphicsHolder, light, -16, i - 5);
							renderOnce(seat, graphicsHolder, light, -16, i + 10);
							renderOnce(seat, graphicsHolder, light, -8, i - 5);
							renderOnce(seat, graphicsHolder, light, -8, i + 10);
							renderOnce(seat, graphicsHolder, light, 8, i - 5);
							renderOnce(seat, graphicsHolder, light, 8, i + 10);
							renderOnce(seat, graphicsHolder, light, 16, i - 5);
							renderOnce(seat, graphicsHolder, light, 16, i + 10);
						} else {
							renderOnceFlipped(seat, graphicsHolder, light, -16, i - 10);
							renderOnceFlipped(seat, graphicsHolder, light, -16, i + 5);
							renderOnceFlipped(seat, graphicsHolder, light, -8, i - 10);
							renderOnceFlipped(seat, graphicsHolder, light, -8, i + 5);
							renderOnceFlipped(seat, graphicsHolder, light, 8, i - 10);
							renderOnceFlipped(seat, graphicsHolder, light, 8, i + 5);
							renderOnceFlipped(seat, graphicsHolder, light, 16, i - 10);
							renderOnceFlipped(seat, graphicsHolder, light, 16, i + 5);
						}
					}
					j++;
				}
				break;
			case EXTERIOR:
				int k = 0;
				for (int i = loopStart; i <= loopEnd; i += 30) {
					if (!isEnd2Head) {
						renderOnce(windowParts[windowParts.length - k - 1], graphicsHolder, light, i);
					}
					if (!isEnd1Head || isEnd2Head) {
						renderOnceFlipped(windowParts[k], graphicsHolder, light, i);
					}
					renderMirror(roof_exterior, graphicsHolder, light, i);
					k++;
				}
				final int[] bogiePositions = getBogiePositions();
				renderMirror(bottom_end, graphicsHolder, light, bogiePositions[0] + 42);
				renderMirror(bottom_end, graphicsHolder, light, bogiePositions[1] - 42);
				for (int i = bogiePositions[0] + 64; i <= bogiePositions[1] - 64; i += 30) {
					renderMirror(bottom_middle, graphicsHolder, light, i);
				}
				break;
		}
	}

	@Override
	protected void renderDoorPositions(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
		final boolean firstDoor = isIndex(0, position, getDoorPositions());
		final int doorOffset = (isEnd1Head && firstDoor ? 90 : 0) + (isEnd2Head && !firstDoor ? -90 : 0);
		final boolean doorOpen = doorLeftZ > 0 || doorRightZ > 0;

		switch (renderStage) {
			case LIGHT:
				if (firstDoor && doorOpen) {
					renderMirror(door_light_on, graphicsHolder, light, 0);
				}
				break;
			case INTERIOR:
				if (firstDoor) {
					door_1.setOffset(0, 0, doorLeftZ);
					door_2.setOffset(0, 0, doorRightZ);
					renderOnceFlipped(door, graphicsHolder, light, position + doorOffset);
				} else {
					door_1.setOffset(0, 0, doorRightZ);
					door_2.setOffset(0, 0, doorLeftZ);
					renderOnce(door, graphicsHolder, light, position + doorOffset);
				}
				break;
			case EXTERIOR:
				if (isEnd1Head && firstDoor || isEnd2Head && !firstDoor) {
					if (firstDoor) {
						door_exterior_end_1.setOffset(0, 0, doorLeftZ);
						door_exterior_end_2.setOffset(0, 0, doorRightZ);
						renderOnceFlipped(door_exterior_end, graphicsHolder, light, position + doorOffset);
					} else {
						door_exterior_end_1.setOffset(0, 0, doorRightZ);
						door_exterior_end_2.setOffset(0, 0, doorLeftZ);
						renderOnce(door_exterior_end, graphicsHolder, light, position + doorOffset);
					}
				} else {
					if (firstDoor) {
						door_exterior_1.setOffset(0, 0, doorLeftZ);
						door_exterior_2.setOffset(0, 0, doorRightZ);
						renderOnceFlipped(door_exterior, graphicsHolder, light, position);
					} else {
						door_exterior_1.setOffset(0, 0, doorRightZ);
						door_exterior_2.setOffset(0, 0, doorLeftZ);
						renderOnce(door_exterior, graphicsHolder, light, position);
					}
				}
				if (firstDoor && !doorOpen) {
					renderMirror(door_light_off, graphicsHolder, light, 0);
				}
				break;
		}
	}

	@Override
	protected void renderHeadPosition1(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean useHeadlights) {
		switch (renderStage) {
			case LIGHT:
				renderOnceFlipped(end_light, graphicsHolder, light, position + 90);
				break;
			case ALWAYS_ON_LIGHT:
				renderOnceFlipped(useHeadlights ? headlights : tail_lights, graphicsHolder, light, position + 30);
				break;
			case INTERIOR:
				renderOnceFlipped(end, graphicsHolder, light, position + 90);
				break;
			case INTERIOR_TRANSLUCENT:
				renderOnceFlipped(end_translucent, graphicsHolder, light, position + 90);
				break;
			case EXTERIOR:
				renderOnceFlipped(head_exterior, graphicsHolder, light, position + 30);
				renderMirror(roof_exterior, graphicsHolder, light, position + 60);
				renderMirror(roof_exterior, graphicsHolder, light, position + 90);
				break;
		}
	}

	@Override
	protected void renderHeadPosition2(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean useHeadlights) {
		switch (renderStage) {
			case LIGHT:
				renderOnce(end_light, graphicsHolder, light, position - 90);
				break;
			case ALWAYS_ON_LIGHT:
				renderOnce(useHeadlights ? headlights : tail_lights, graphicsHolder, light, position - 30);
				break;
			case INTERIOR:
				renderOnce(end, graphicsHolder, light, position - 90);
				break;
			case INTERIOR_TRANSLUCENT:
				renderOnce(end_translucent, graphicsHolder, light, position - 90);
				break;
			case EXTERIOR:
				renderOnce(head_exterior, graphicsHolder, light, position - 30);
				renderMirror(roof_exterior, graphicsHolder, light, position - 60);
				renderMirror(roof_exterior, graphicsHolder, light, position - 90);
				break;
		}
	}

	@Override
	protected void renderEndPosition1(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
		switch (renderStage) {
			case LIGHT:
				renderOnceFlipped(end_light, graphicsHolder, light, position);
				break;
			case INTERIOR:
				renderOnceFlipped(end, graphicsHolder, light, position);
				break;
			case INTERIOR_TRANSLUCENT:
				renderOnceFlipped(end_translucent, graphicsHolder, light, position);
				break;
			case EXTERIOR:
				renderOnceFlipped(end_exterior, graphicsHolder, light, position);
				renderMirror(roof_exterior, graphicsHolder, light, position);
				renderMirror(roof_exterior, graphicsHolder, light, position - 30);
				renderOnceFlipped(roof_vent, graphicsHolder, light, position);
				break;
		}
	}

	@Override
	protected void renderEndPosition2(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
		switch (renderStage) {
			case LIGHT:
				renderOnce(end_light, graphicsHolder, light, position);
				break;
			case INTERIOR:
				renderOnce(end, graphicsHolder, light, position);
				break;
			case INTERIOR_TRANSLUCENT:
				renderOnce(end_translucent, graphicsHolder, light, position);
				break;
			case EXTERIOR:
				renderOnce(end_exterior, graphicsHolder, light, position);
				renderMirror(roof_exterior, graphicsHolder, light, position);
				renderMirror(roof_exterior, graphicsHolder, light, position + 30);
				renderOnce(roof_vent, graphicsHolder, light, position);
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
		return new int[]{-150, 150};
	}

	@Override
	protected int[] getEndPositions() {
		return new int[]{-150, 150};
	}

	protected int[] getBogiePositions() {
		return new int[]{-124, 124};
	}

	@Override
	protected int getDoorMax() {
		return DOOR_MAX;
	}

	@Override
	protected void renderTextDisplays(StoredMatrixTransformations storedMatrixTransformations, int thisRouteColor, String thisRouteName, String thisRouteNumber, String thisStationName, String thisRouteDestination, String nextStationName, Consumer<BiConsumer<String, InterchangeColorsForStationName>> getInterchanges, int car, int totalCars, boolean atPlatform, boolean isTerminating, ObjectArrayList<ScrollingText> scrollingTexts) {
		if (scrollingTexts.isEmpty()) {
			scrollingTexts.add(new ScrollingText(0.46F, 0.16F, 4, false));
			scrollingTexts.add(new ScrollingText(0.7F, 0.06F, 8, true));
		}
		final boolean isEnd1Head = car == 0;
		final boolean isEnd2Head = car == totalCars - 1;
		final boolean renderFirstDestination = renderFirstDestination(isEnd1Head, isEnd2Head);
		final boolean renderSecondDestination = renderSecondDestination(isEnd1Head, isEnd2Head);
		final String destinationString = getDestinationString(thisRouteDestination, TextSpacingType.NORMAL, false);

		if (renderFirstDestination || renderSecondDestination) {
			scrollingTexts.get(0).changeImage(destinationString.isEmpty() ? null : DynamicTextureCache.instance.getPixelatedText(destinationString.replace("|", " "), 0xFFFF9900, Integer.MAX_VALUE, 0, false));

			for (int i = 0; i < 2; i++) {
				if (i == 0 && renderFirstDestination || i == 1 && renderSecondDestination) {
					for (int j = 0; j < 2; j++) {
						final StoredMatrixTransformations storedMatrixTransformationsNew = storedMatrixTransformations.copy();
						final boolean flip = i == 1;
						final int sign = j == 1 ? -1 : 1;
						final int endPosition = getEndPositions()[i];
						storedMatrixTransformationsNew.add(graphicsHolder -> {
							graphicsHolder.translate(-21F / 16 * sign, -13F / 16, endPosition / 16F + (!flip ? 1 : -1) * (1.28F + (!flip && isEnd1Head || flip && isEnd2Head ? 5.63F : 0)));
							graphicsHolder.rotateYDegrees(90 * sign);
							graphicsHolder.rotateXDegrees(-8);
							graphicsHolder.translate(-0.23F, -1.38F, -0.01F);
						});
						scrollingTexts.get(0).scrollText(storedMatrixTransformationsNew);
					}
				}
			}
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
				graphicsHolder.translate(-0.35F, -2.19F, (getEndPositions()[1] - (flip && isEnd1Head || !flip && isEnd2Head ? 90 : 0)) / 16F - 0.51F);
			});
			scrollingTexts.get(1).scrollText(storedMatrixTransformationsNew);
		}
	}

	@Override
	protected String defaultDestinationString() {
		return "Not in Service";
	}

	protected boolean renderFirstDestination(boolean isEnd1Head, boolean isEnd2Head) {
		return true;
	}

	protected boolean renderSecondDestination(boolean isEnd1Head, boolean isEnd2Head) {
		return !isEnd1Head || !isEnd2Head;
	}

	protected ModelPartExtension[] windowParts() {
		return new ModelPartExtension[]{window_exterior_1, window_exterior_2, window_exterior_3, window_exterior_4, window_exterior_5, window_exterior_6, window_exterior_7, window_exterior_8, window_exterior_9};
	}

	protected ModelPartExtension[] windowEndParts() {
		return new ModelPartExtension[]{window_end_exterior_6, window_end_exterior_5, window_end_exterior_4, window_end_exterior_3, window_end_exterior_2, window_end_exterior_1};
	}

	protected final ModelPartExtension[] windowPartsMini() {
		return new ModelPartExtension[]{window_exterior_1, window_exterior_2, window_exterior_3, window_exterior_7, window_exterior_8, window_exterior_9};
	}

	protected final ModelPartExtension[] windowEndPartsMini() {
		return new ModelPartExtension[]{window_end_exterior_3, window_end_exterior_2, window_end_exterior_1};
	}
}