package mtr.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mtr.mappings.ModelDataWrapper;
import mtr.mappings.ModelMapper;

public class ModelClass802 extends ModelTrainBase {

	private final ModelMapper window;
	private final ModelMapper roof_3_r1;
	private final ModelMapper roof_1_r1;
	private final ModelMapper window_bottom_r1;
	private final ModelMapper window_top_r1;
	private final ModelMapper window_exterior_1;
	private final ModelMapper window_bottom_r2;
	private final ModelMapper window_top_r2;
	private final ModelMapper window_exterior_2;
	private final ModelMapper window_bottom_r3;
	private final ModelMapper window_top_r3;
	private final ModelMapper window_exterior_3;
	private final ModelMapper window_bottom_r4;
	private final ModelMapper window_top_r4;
	private final ModelMapper window_exterior_4;
	private final ModelMapper window_bottom_r5;
	private final ModelMapper window_top_r5;
	private final ModelMapper window_exterior_5;
	private final ModelMapper window_bottom_r6;
	private final ModelMapper window_top_r6;
	private final ModelMapper window_exterior_6;
	private final ModelMapper window_bottom_r7;
	private final ModelMapper window_top_r7;
	private final ModelMapper window_exterior_7;
	private final ModelMapper window_bottom_r8;
	private final ModelMapper window_top_r8;
	private final ModelMapper window_exterior_8;
	private final ModelMapper window_bottom_r9;
	private final ModelMapper window_top_r9;
	private final ModelMapper window_exterior_9;
	private final ModelMapper window_bottom_r10;
	private final ModelMapper window_top_r10;
	private final ModelMapper window_end_exterior_1;
	private final ModelMapper window_bottom_r11;
	private final ModelMapper window_top_r11;
	private final ModelMapper window_bottom_r12;
	private final ModelMapper window_top_r12;
	private final ModelMapper window_end_exterior_2;
	private final ModelMapper window_bottom_r13;
	private final ModelMapper window_top_r13;
	private final ModelMapper window_bottom_r14;
	private final ModelMapper window_top_r14;
	private final ModelMapper window_end_exterior_3;
	private final ModelMapper window_bottom_r15;
	private final ModelMapper window_top_r15;
	private final ModelMapper window_bottom_r16;
	private final ModelMapper window_top_r16;
	private final ModelMapper window_end_exterior_4;
	private final ModelMapper window_bottom_r17;
	private final ModelMapper window_top_r17;
	private final ModelMapper window_bottom_r18;
	private final ModelMapper window_top_r18;
	private final ModelMapper window_end_exterior_5;
	private final ModelMapper window_bottom_r19;
	private final ModelMapper window_top_r19;
	private final ModelMapper window_bottom_r20;
	private final ModelMapper window_top_r20;
	private final ModelMapper window_end_exterior_6;
	private final ModelMapper window_bottom_r21;
	private final ModelMapper window_top_r21;
	private final ModelMapper window_bottom_r22;
	private final ModelMapper window_top_r22;
	private final ModelMapper door;
	private final ModelMapper door_1;
	private final ModelMapper window_bottom_2_r1;
	private final ModelMapper window_top_2_r1;
	private final ModelMapper door_2;
	private final ModelMapper window_bottom_3_r1;
	private final ModelMapper window_top_3_r1;
	private final ModelMapper door_exterior;
	private final ModelMapper door_exterior_1;
	private final ModelMapper window_bottom_1_r1;
	private final ModelMapper window_top_1_r1;
	private final ModelMapper door_exterior_2;
	private final ModelMapper window_bottom_2_r2;
	private final ModelMapper window_top_2_r2;
	private final ModelMapper door_exterior_end;
	private final ModelMapper door_exterior_end_1;
	private final ModelMapper window_bottom_2_r3;
	private final ModelMapper window_top_2_r3;
	private final ModelMapper door_exterior_end_2;
	private final ModelMapper window_bottom_3_r2;
	private final ModelMapper window_top_3_r2;
	private final ModelMapper roof_exterior;
	private final ModelMapper roof_2_r1;
	private final ModelMapper roof_1_r2;
	private final ModelMapper end;
	private final ModelMapper end_pillar_4_r1;
	private final ModelMapper end_pillar_3_r1;
	private final ModelMapper end_side_1;
	private final ModelMapper roof_3_r2;
	private final ModelMapper roof_1_r3;
	private final ModelMapper window_bottom_r23;
	private final ModelMapper window_top_r23;
	private final ModelMapper end_side_2;
	private final ModelMapper roof_4_r1;
	private final ModelMapper roof_2_r2;
	private final ModelMapper window_bottom_r24;
	private final ModelMapper window_top_r24;
	private final ModelMapper end_exterior;
	private final ModelMapper end_exterior_side_1;
	private final ModelMapper door_top_r1;
	private final ModelMapper window_bottom_2_r4;
	private final ModelMapper end_exterior_side_2;
	private final ModelMapper door_top_r2;
	private final ModelMapper window_bottom_3_r3;
	private final ModelMapper roof_vent;
	private final ModelMapper roof_vent_side_1;
	private final ModelMapper vent_4_r1;
	private final ModelMapper vent_3_r1;
	private final ModelMapper vent_1_r1;
	private final ModelMapper roof_vent_side_2;
	private final ModelMapper vent_5_r1;
	private final ModelMapper vent_4_r2;
	private final ModelMapper vent_2_r1;
	private final ModelMapper head_exterior;
	private final ModelMapper head_side_1;
	private final ModelMapper window_top_1_r2;
	private final ModelMapper window_bottom_1_r2;
	private final ModelMapper end_r1;
	private final ModelMapper roof_11_r1;
	private final ModelMapper roof_12_r1;
	private final ModelMapper roof_11_r2;
	private final ModelMapper roof_10_r1;
	private final ModelMapper roof_10_r2;
	private final ModelMapper roof_11_r3;
	private final ModelMapper roof_10_r3;
	private final ModelMapper roof_9_r1;
	private final ModelMapper roof_8_r1;
	private final ModelMapper roof_7_r1;
	private final ModelMapper roof_4_r2;
	private final ModelMapper roof_4_r3;
	private final ModelMapper roof_3_r3;
	private final ModelMapper roof_2_r3;
	private final ModelMapper roof_3_r4;
	private final ModelMapper roof_2_r4;
	private final ModelMapper roof_1_r4;
	private final ModelMapper head_side_2;
	private final ModelMapper window_top_2_r4;
	private final ModelMapper window_bottom_2_r5;
	private final ModelMapper end_r2;
	private final ModelMapper roof_12_r2;
	private final ModelMapper roof_13_r1;
	private final ModelMapper roof_12_r3;
	private final ModelMapper roof_11_r4;
	private final ModelMapper roof_11_r5;
	private final ModelMapper roof_12_r4;
	private final ModelMapper roof_11_r6;
	private final ModelMapper roof_10_r4;
	private final ModelMapper roof_9_r2;
	private final ModelMapper roof_8_r2;
	private final ModelMapper roof_5_r1;
	private final ModelMapper roof_5_r2;
	private final ModelMapper roof_4_r4;
	private final ModelMapper roof_3_r5;
	private final ModelMapper roof_4_r5;
	private final ModelMapper roof_3_r6;
	private final ModelMapper roof_2_r5;
	private final ModelMapper middle;
	private final ModelMapper roof_8_r3;
	private final ModelMapper roof_7_r2;
	private final ModelMapper roof_6_r1;
	private final ModelMapper roof_5_r3;
	private final ModelMapper roof_9_r3;
	private final ModelMapper bottom_middle;
	private final ModelMapper bottom_end;

	public ModelClass802() {
		final int textureWidth = 400;
		final int textureHeight = 400;

		final ModelDataWrapper modelDataWrapper = new ModelDataWrapper(this, textureWidth, textureHeight);

		window = new ModelMapper(modelDataWrapper);
		window.setPos(0, 24, 0);
		window.texOffs(0, 0).addBox(-19, 0, -15, 19, 0, 30, 0, false);
		window.texOffs(214, 235).addBox(-20, -13, -15, 0, 6, 30, 0, false);
		window.texOffs(6, 30).addBox(-15, -36, -15, 6, 0, 30, 0, false);
		window.texOffs(24, 30).addBox(-7, -35, -15, 7, 0, 30, 0, false);
		window.texOffs(233, 234).addBox(-18, -31, -15, 6, 1, 30, 0, false);

		roof_3_r1 = new ModelMapper(modelDataWrapper);
		roof_3_r1.setPos(-7, -35, 0);
		window.addChild(roof_3_r1);
		setRotationAngle(roof_3_r1, 0, 0, 0.3491F);
		roof_3_r1.texOffs(18, 30).addBox(-3, 0, -15, 3, 0, 30, 0, false);

		roof_1_r1 = new ModelMapper(modelDataWrapper);
		roof_1_r1.setPos(-15, -36, 0);
		window.addChild(roof_1_r1);
		setRotationAngle(roof_1_r1, 0, 0, -0.7854F);
		roof_1_r1.texOffs(0, 30).addBox(-3, 0, -15, 3, 0, 30, 0, false);

		window_bottom_r1 = new ModelMapper(modelDataWrapper);
		window_bottom_r1.setPos(-21, -7, 0);
		window.addChild(window_bottom_r1);
		setRotationAngle(window_bottom_r1, 0, 0, -0.2094F);
		window_bottom_r1.texOffs(74, 30).addBox(1, 0, -15, 0, 8, 30, 0, false);

		window_top_r1 = new ModelMapper(modelDataWrapper);
		window_top_r1.setPos(-21, -13, 0);
		window.addChild(window_top_r1);
		setRotationAngle(window_top_r1, 0, 0, 0.1396F);
		window_top_r1.texOffs(220, 0).addBox(1, -22, -15, 0, 22, 30, 0, false);

		window_exterior_1 = new ModelMapper(modelDataWrapper);
		window_exterior_1.setPos(0, 24, 0);
		window_exterior_1.texOffs(214, 241).addBox(-21, -13, -15, 0, 6, 30, 0, false);
		window_exterior_1.texOffs(0, 339).addBox(-19, 0, -15, 1, 2, 30, 0, false);

		window_bottom_r2 = new ModelMapper(modelDataWrapper);
		window_bottom_r2.setPos(-21, -7, 0);
		window_exterior_1.addChild(window_bottom_r2);
		setRotationAngle(window_bottom_r2, 0, 0, -0.2094F);
		window_bottom_r2.texOffs(295, 122).addBox(0, 0, -15, 1, 8, 30, 0, false);

		window_top_r2 = new ModelMapper(modelDataWrapper);
		window_top_r2.setPos(-21, -13, 0);
		window_exterior_1.addChild(window_top_r2);
		setRotationAngle(window_top_r2, 0, 0, 0.1396F);
		window_top_r2.texOffs(226, 54).addBox(0, -22, -15, 0, 22, 30, 0, false);

		window_exterior_2 = new ModelMapper(modelDataWrapper);
		window_exterior_2.setPos(0, 24, 0);
		window_exterior_2.texOffs(234, 194).addBox(-21, -13, -15, 0, 6, 30, 0, false);
		window_exterior_2.texOffs(218, 337).addBox(-19, 0, -15, 1, 2, 30, 0, false);

		window_bottom_r3 = new ModelMapper(modelDataWrapper);
		window_bottom_r3.setPos(-21, -7, 0);
		window_exterior_2.addChild(window_bottom_r3);
		setRotationAngle(window_bottom_r3, 0, 0, -0.2094F);
		window_bottom_r3.texOffs(289, 84).addBox(0, 0, -15, 1, 8, 30, 0, false);

		window_top_r3 = new ModelMapper(modelDataWrapper);
		window_top_r3.setPos(-21, -13, 0);
		window_exterior_2.addChild(window_top_r3);
		setRotationAngle(window_top_r3, 0, 0, 0.1396F);
		window_top_r3.texOffs(120, 212).addBox(0, -22, -15, 0, 22, 30, 0, false);

		window_exterior_3 = new ModelMapper(modelDataWrapper);
		window_exterior_3.setPos(0, 24, 0);
		window_exterior_3.texOffs(32, 234).addBox(-21, -13, -15, 0, 6, 30, 0, false);
		window_exterior_3.texOffs(156, 337).addBox(-19, 0, -15, 1, 2, 30, 0, false);

		window_bottom_r4 = new ModelMapper(modelDataWrapper);
		window_bottom_r4.setPos(-21, -7, 0);
		window_exterior_3.addChild(window_bottom_r4);
		setRotationAngle(window_bottom_r4, 0, 0, -0.2094F);
		window_bottom_r4.texOffs(282, 38).addBox(0, 0, -15, 1, 8, 30, 0, false);

		window_top_r4 = new ModelMapper(modelDataWrapper);
		window_top_r4.setPos(-21, -13, 0);
		window_exterior_3.addChild(window_top_r4);
		setRotationAngle(window_top_r4, 0, 0, 0.1396F);
		window_top_r4.texOffs(60, 212).addBox(0, -22, -15, 0, 22, 30, 0, false);

		window_exterior_4 = new ModelMapper(modelDataWrapper);
		window_exterior_4.setPos(0, 24, 0);
		window_exterior_4.texOffs(218, 44).addBox(-21, -13, -15, 0, 6, 30, 0, false);
		window_exterior_4.texOffs(317, 334).addBox(-19, 0, -15, 1, 2, 30, 0, false);

		window_bottom_r5 = new ModelMapper(modelDataWrapper);
		window_bottom_r5.setPos(-21, -7, 0);
		window_exterior_4.addChild(window_bottom_r5);
		setRotationAngle(window_bottom_r5, 0, 0, -0.2094F);
		window_bottom_r5.texOffs(282, 0).addBox(0, 0, -15, 1, 8, 30, 0, false);

		window_top_r5 = new ModelMapper(modelDataWrapper);
		window_top_r5.setPos(-21, -13, 0);
		window_exterior_4.addChild(window_top_r5);
		setRotationAngle(window_top_r5, 0, 0, 0.1396F);
		window_top_r5.texOffs(0, 212).addBox(0, -22, -15, 0, 22, 30, 0, false);

		window_exterior_5 = new ModelMapper(modelDataWrapper);
		window_exterior_5.setPos(0, 24, 0);
		window_exterior_5.texOffs(218, 38).addBox(-21, -13, -15, 0, 6, 30, 0, false);
		window_exterior_5.texOffs(285, 332).addBox(-19, 0, -15, 1, 2, 30, 0, false);

		window_bottom_r6 = new ModelMapper(modelDataWrapper);
		window_bottom_r6.setPos(-21, -7, 0);
		window_exterior_5.addChild(window_bottom_r6);
		setRotationAngle(window_bottom_r6, 0, 0, -0.2094F);
		window_bottom_r6.texOffs(126, 280).addBox(0, 0, -15, 1, 8, 30, 0, false);

		window_top_r6 = new ModelMapper(modelDataWrapper);
		window_top_r6.setPos(-21, -13, 0);
		window_exterior_5.addChild(window_top_r6);
		setRotationAngle(window_top_r6, 0, 0, 0.1396F);
		window_top_r6.texOffs(183, 137).addBox(0, -22, -15, 0, 22, 30, 0, false);

		window_exterior_6 = new ModelMapper(modelDataWrapper);
		window_exterior_6.setPos(0, 24, 0);
		window_exterior_6.texOffs(218, 32).addBox(-21, -13, -15, 0, 6, 30, 0, false);
		window_exterior_6.texOffs(327, 110).addBox(-19, 0, -15, 1, 2, 30, 0, false);

		window_bottom_r7 = new ModelMapper(modelDataWrapper);
		window_bottom_r7.setPos(-21, -7, 0);
		window_exterior_6.addChild(window_bottom_r7);
		setRotationAngle(window_bottom_r7, 0, 0, -0.2094F);
		window_bottom_r7.texOffs(276, 273).addBox(0, 0, -15, 1, 8, 30, 0, false);

		window_top_r7 = new ModelMapper(modelDataWrapper);
		window_top_r7.setPos(-21, -13, 0);
		window_exterior_6.addChild(window_top_r7);
		setRotationAngle(window_top_r7, 0, 0, 0.1396F);
		window_top_r7.texOffs(158, 0).addBox(0, -22, -15, 0, 22, 30, 0, false);

		window_exterior_7 = new ModelMapper(modelDataWrapper);
		window_exterior_7.setPos(0, 24, 0);
		window_exterior_7.texOffs(67, 181).addBox(-21, -13, -15, 0, 6, 30, 0, false);
		window_exterior_7.texOffs(321, 78).addBox(-19, 0, -15, 1, 2, 30, 0, false);

		window_bottom_r8 = new ModelMapper(modelDataWrapper);
		window_bottom_r8.setPos(-21, -7, 0);
		window_exterior_7.addChild(window_bottom_r8);
		setRotationAngle(window_bottom_r8, 0, 0, -0.2094F);
		window_bottom_r8.texOffs(275, 224).addBox(0, 0, -15, 1, 8, 30, 0, false);

		window_top_r8 = new ModelMapper(modelDataWrapper);
		window_top_r8.setPos(-21, -13, 0);
		window_exterior_7.addChild(window_top_r8);
		setRotationAngle(window_top_r8, 0, 0, 0.1396F);
		window_top_r8.texOffs(154, 98).addBox(0, -22, -15, 0, 22, 30, 0, false);

		window_exterior_8 = new ModelMapper(modelDataWrapper);
		window_exterior_8.setPos(0, 24, 0);
		window_exterior_8.texOffs(0, 181).addBox(-21, -13, -15, 0, 6, 30, 0, false);
		window_exterior_8.texOffs(124, 318).addBox(-19, 0, -15, 1, 2, 30, 0, false);

		window_bottom_r9 = new ModelMapper(modelDataWrapper);
		window_bottom_r9.setPos(-21, -7, 0);
		window_exterior_8.addChild(window_bottom_r9);
		setRotationAngle(window_bottom_r9, 0, 0, -0.2094F);
		window_bottom_r9.texOffs(94, 272).addBox(0, 0, -15, 1, 8, 30, 0, false);

		window_top_r9 = new ModelMapper(modelDataWrapper);
		window_top_r9.setPos(-21, -13, 0);
		window_exterior_8.addChild(window_top_r9);
		setRotationAngle(window_top_r9, 0, 0, 0.1396F);
		window_top_r9.texOffs(154, 76).addBox(0, -22, -15, 0, 22, 30, 0, false);

		window_exterior_9 = new ModelMapper(modelDataWrapper);
		window_exterior_9.setPos(0, 24, 0);
		window_exterior_9.texOffs(67, 175).addBox(-21, -13, -15, 0, 6, 30, 0, false);
		window_exterior_9.texOffs(314, 46).addBox(-19, 0, -15, 1, 2, 30, 0, false);

		window_bottom_r10 = new ModelMapper(modelDataWrapper);
		window_bottom_r10.setPos(-21, -7, 0);
		window_exterior_9.addChild(window_bottom_r10);
		setRotationAngle(window_bottom_r10, 0, 0, -0.2094F);
		window_bottom_r10.texOffs(244, 265).addBox(0, 0, -15, 1, 8, 30, 0, false);

		window_top_r10 = new ModelMapper(modelDataWrapper);
		window_top_r10.setPos(-21, -13, 0);
		window_exterior_9.addChild(window_top_r10);
		setRotationAngle(window_top_r10, 0, 0, 0.1396F);
		window_top_r10.texOffs(154, 54).addBox(0, -22, -15, 0, 22, 30, 0, false);

		window_end_exterior_1 = new ModelMapper(modelDataWrapper);
		window_end_exterior_1.setPos(0, 24, 0);
		window_end_exterior_1.texOffs(0, 175).addBox(-21, -13, -15, 0, 6, 30, 0, false);
		window_end_exterior_1.texOffs(314, 8).addBox(-19, 0, -15, 1, 2, 30, 0, false);
		window_end_exterior_1.texOffs(0, 175).addBox(21, -13, -15, 0, 6, 30, 0, true);
		window_end_exterior_1.texOffs(314, 8).addBox(18, 0, -15, 1, 2, 30, 0, true);

		window_bottom_r11 = new ModelMapper(modelDataWrapper);
		window_bottom_r11.setPos(21, -7, 0);
		window_end_exterior_1.addChild(window_bottom_r11);
		setRotationAngle(window_bottom_r11, 0, 0, 0.2094F);
		window_bottom_r11.texOffs(182, 264).addBox(-1, 0, -15, 1, 8, 30, 0, true);

		window_top_r11 = new ModelMapper(modelDataWrapper);
		window_top_r11.setPos(21, -13, 0);
		window_end_exterior_1.addChild(window_top_r11);
		setRotationAngle(window_top_r11, 0, 0, -0.1396F);
		window_top_r11.texOffs(67, 153).addBox(0, -22, -15, 0, 22, 30, 0, true);

		window_bottom_r12 = new ModelMapper(modelDataWrapper);
		window_bottom_r12.setPos(-21, -7, 0);
		window_end_exterior_1.addChild(window_bottom_r12);
		setRotationAngle(window_bottom_r12, 0, 0, -0.2094F);
		window_bottom_r12.texOffs(182, 264).addBox(0, 0, -15, 1, 8, 30, 0, false);

		window_top_r12 = new ModelMapper(modelDataWrapper);
		window_top_r12.setPos(-21, -13, 0);
		window_end_exterior_1.addChild(window_top_r12);
		setRotationAngle(window_top_r12, 0, 0, 0.1396F);
		window_top_r12.texOffs(67, 153).addBox(0, -22, -15, 0, 22, 30, 0, false);

		window_end_exterior_2 = new ModelMapper(modelDataWrapper);
		window_end_exterior_2.setPos(0, 24, 0);
		window_end_exterior_2.texOffs(0, 169).addBox(-21, -13, -15, 0, 6, 30, 0, false);
		window_end_exterior_2.texOffs(253, 311).addBox(-19, 0, -15, 1, 2, 30, 0, false);
		window_end_exterior_2.texOffs(0, 169).addBox(21, -13, -15, 0, 6, 30, 0, true);
		window_end_exterior_2.texOffs(253, 311).addBox(18, 0, -15, 1, 2, 30, 0, true);

		window_bottom_r13 = new ModelMapper(modelDataWrapper);
		window_bottom_r13.setPos(21, -7, 0);
		window_end_exterior_2.addChild(window_bottom_r13);
		setRotationAngle(window_bottom_r13, 0, 0, 0.2094F);
		window_bottom_r13.texOffs(62, 264).addBox(-1, 0, -15, 1, 8, 30, 0, true);

		window_top_r13 = new ModelMapper(modelDataWrapper);
		window_top_r13.setPos(21, -13, 0);
		window_end_exterior_2.addChild(window_top_r13);
		setRotationAngle(window_top_r13, 0, 0, -0.1396F);
		window_top_r13.texOffs(0, 147).addBox(0, -22, -15, 0, 22, 30, 0, true);

		window_bottom_r14 = new ModelMapper(modelDataWrapper);
		window_bottom_r14.setPos(-21, -7, 0);
		window_end_exterior_2.addChild(window_bottom_r14);
		setRotationAngle(window_bottom_r14, 0, 0, -0.2094F);
		window_bottom_r14.texOffs(62, 264).addBox(0, 0, -15, 1, 8, 30, 0, false);

		window_top_r14 = new ModelMapper(modelDataWrapper);
		window_top_r14.setPos(-21, -13, 0);
		window_end_exterior_2.addChild(window_top_r14);
		setRotationAngle(window_top_r14, 0, 0, 0.1396F);
		window_top_r14.texOffs(0, 147).addBox(0, -22, -15, 0, 22, 30, 0, false);

		window_end_exterior_3 = new ModelMapper(modelDataWrapper);
		window_end_exterior_3.setPos(0, 24, 0);
		window_end_exterior_3.texOffs(158, 44).addBox(-21, -13, -15, 0, 6, 30, 0, false);
		window_end_exterior_3.texOffs(92, 310).addBox(-19, 0, -15, 1, 2, 30, 0, false);
		window_end_exterior_3.texOffs(158, 44).addBox(21, -13, -15, 0, 6, 30, 0, true);
		window_end_exterior_3.texOffs(92, 310).addBox(18, 0, -15, 1, 2, 30, 0, true);

		window_bottom_r15 = new ModelMapper(modelDataWrapper);
		window_bottom_r15.setPos(21, -7, 0);
		window_end_exterior_3.addChild(window_bottom_r15);
		setRotationAngle(window_bottom_r15, 0, 0, 0.2094F);
		window_bottom_r15.texOffs(0, 264).addBox(-1, 0, -15, 1, 8, 30, 0, true);

		window_top_r15 = new ModelMapper(modelDataWrapper);
		window_top_r15.setPos(21, -13, 0);
		window_end_exterior_3.addChild(window_top_r15);
		setRotationAngle(window_top_r15, 0, 0, -0.1396F);
		window_top_r15.texOffs(67, 131).addBox(0, -22, -15, 0, 22, 30, 0, true);

		window_bottom_r16 = new ModelMapper(modelDataWrapper);
		window_bottom_r16.setPos(-21, -7, 0);
		window_end_exterior_3.addChild(window_bottom_r16);
		setRotationAngle(window_bottom_r16, 0, 0, -0.2094F);
		window_bottom_r16.texOffs(0, 264).addBox(0, 0, -15, 1, 8, 30, 0, false);

		window_top_r16 = new ModelMapper(modelDataWrapper);
		window_top_r16.setPos(-21, -13, 0);
		window_end_exterior_3.addChild(window_top_r16);
		setRotationAngle(window_top_r16, 0, 0, 0.1396F);
		window_top_r16.texOffs(67, 131).addBox(0, -22, -15, 0, 22, 30, 0, false);

		window_end_exterior_4 = new ModelMapper(modelDataWrapper);
		window_end_exterior_4.setPos(0, 24, 0);
		window_end_exterior_4.texOffs(158, 38).addBox(-21, -13, -15, 0, 6, 30, 0, false);
		window_end_exterior_4.texOffs(308, 300).addBox(-19, 0, -15, 1, 2, 30, 0, false);
		window_end_exterior_4.texOffs(158, 38).addBox(21, -13, -15, 0, 6, 30, 0, true);
		window_end_exterior_4.texOffs(308, 300).addBox(18, 0, -15, 1, 2, 30, 0, true);

		window_bottom_r17 = new ModelMapper(modelDataWrapper);
		window_bottom_r17.setPos(21, -7, 0);
		window_end_exterior_4.addChild(window_bottom_r17);
		setRotationAngle(window_bottom_r17, 0, 0, 0.2094F);
		window_bottom_r17.texOffs(263, 137).addBox(-1, 0, -15, 1, 8, 30, 0, true);

		window_top_r17 = new ModelMapper(modelDataWrapper);
		window_top_r17.setPos(21, -13, 0);
		window_end_exterior_4.addChild(window_top_r17);
		setRotationAngle(window_top_r17, 0, 0, -0.1396F);
		window_top_r17.texOffs(0, 125).addBox(0, -22, -15, 0, 22, 30, 0, true);

		window_bottom_r18 = new ModelMapper(modelDataWrapper);
		window_bottom_r18.setPos(-21, -7, 0);
		window_end_exterior_4.addChild(window_bottom_r18);
		setRotationAngle(window_bottom_r18, 0, 0, -0.2094F);
		window_bottom_r18.texOffs(263, 137).addBox(0, 0, -15, 1, 8, 30, 0, false);

		window_top_r18 = new ModelMapper(modelDataWrapper);
		window_top_r18.setPos(-21, -13, 0);
		window_end_exterior_4.addChild(window_top_r18);
		setRotationAngle(window_top_r18, 0, 0, 0.1396F);
		window_top_r18.texOffs(0, 125).addBox(0, -22, -15, 0, 22, 30, 0, false);

		window_end_exterior_5 = new ModelMapper(modelDataWrapper);
		window_end_exterior_5.setPos(0, 24, 0);
		window_end_exterior_5.texOffs(158, 32).addBox(-21, -13, -15, 0, 6, 30, 0, false);
		window_end_exterior_5.texOffs(308, 268).addBox(-19, 0, -15, 1, 2, 30, 0, false);
		window_end_exterior_5.texOffs(158, 32).addBox(21, -13, -15, 0, 6, 30, 0, true);
		window_end_exterior_5.texOffs(308, 268).addBox(18, 0, -15, 1, 2, 30, 0, true);

		window_bottom_r19 = new ModelMapper(modelDataWrapper);
		window_bottom_r19.setPos(21, -7, 0);
		window_end_exterior_5.addChild(window_bottom_r19);
		setRotationAngle(window_bottom_r19, 0, 0, 0.2094F);
		window_bottom_r19.texOffs(257, 76).addBox(-1, 0, -15, 1, 8, 30, 0, true);

		window_top_r19 = new ModelMapper(modelDataWrapper);
		window_top_r19.setPos(21, -13, 0);
		window_end_exterior_5.addChild(window_top_r19);
		setRotationAngle(window_top_r19, 0, 0, -0.1396F);
		window_top_r19.texOffs(0, 98).addBox(0, -22, -15, 0, 22, 30, 0, true);

		window_bottom_r20 = new ModelMapper(modelDataWrapper);
		window_bottom_r20.setPos(-21, -7, 0);
		window_end_exterior_5.addChild(window_bottom_r20);
		setRotationAngle(window_bottom_r20, 0, 0, -0.2094F);
		window_bottom_r20.texOffs(257, 76).addBox(0, 0, -15, 1, 8, 30, 0, false);

		window_top_r20 = new ModelMapper(modelDataWrapper);
		window_top_r20.setPos(-21, -13, 0);
		window_end_exterior_5.addChild(window_top_r20);
		setRotationAngle(window_top_r20, 0, 0, 0.1396F);
		window_top_r20.texOffs(0, 98).addBox(0, -22, -15, 0, 22, 30, 0, false);

		window_end_exterior_6 = new ModelMapper(modelDataWrapper);
		window_end_exterior_6.setPos(0, 24, 0);
		window_end_exterior_6.texOffs(158, 22).addBox(-21, -13, -15, 0, 6, 30, 0, false);
		window_end_exterior_6.texOffs(250, 343).addBox(-19, 0, -15, 1, 2, 30, 0, false);
		window_end_exterior_6.texOffs(158, 22).addBox(21, -13, -15, 0, 6, 30, 0, true);
		window_end_exterior_6.texOffs(250, 343).addBox(18, 0, -15, 1, 2, 30, 0, true);

		window_bottom_r21 = new ModelMapper(modelDataWrapper);
		window_bottom_r21.setPos(21, -7, 0);
		window_end_exterior_6.addChild(window_bottom_r21);
		setRotationAngle(window_bottom_r21, 0, 0, 0.2094F);
		window_bottom_r21.texOffs(250, 22).addBox(-1, 0, -15, 1, 8, 30, 0, true);

		window_top_r21 = new ModelMapper(modelDataWrapper);
		window_top_r21.setPos(21, -13, 0);
		window_end_exterior_6.addChild(window_top_r21);
		setRotationAngle(window_top_r21, 0, 0, -0.1396F);
		window_top_r21.texOffs(0, 76).addBox(0, -22, -15, 0, 22, 30, 0, true);

		window_bottom_r22 = new ModelMapper(modelDataWrapper);
		window_bottom_r22.setPos(-21, -7, 0);
		window_end_exterior_6.addChild(window_bottom_r22);
		setRotationAngle(window_bottom_r22, 0, 0, -0.2094F);
		window_bottom_r22.texOffs(250, 22).addBox(0, 0, -15, 1, 8, 30, 0, false);

		window_top_r22 = new ModelMapper(modelDataWrapper);
		window_top_r22.setPos(-21, -13, 0);
		window_end_exterior_6.addChild(window_top_r22);
		setRotationAngle(window_top_r22, 0, 0, 0.1396F);
		window_top_r22.texOffs(0, 76).addBox(0, -22, -15, 0, 22, 30, 0, false);

		door = new ModelMapper(modelDataWrapper);
		door.setPos(0, 24, 0);


		door_1 = new ModelMapper(modelDataWrapper);
		door_1.setPos(0, 0, 0);
		door.addChild(door_1);
		door_1.texOffs(257, 114).addBox(-20, -13, 3, 1, 6, 13, 0, false);

		window_bottom_2_r1 = new ModelMapper(modelDataWrapper);
		window_bottom_2_r1.setPos(-20, -7, 0);
		door_1.addChild(window_bottom_2_r1);
		setRotationAngle(window_bottom_2_r1, 0, 0, -0.2094F);
		window_bottom_2_r1.texOffs(226, 106).addBox(0, 0, 3, 1, 8, 13, 0, false);

		window_top_2_r1 = new ModelMapper(modelDataWrapper);
		window_top_2_r1.setPos(-20, -13, 0);
		door_1.addChild(window_top_2_r1);
		setRotationAngle(window_top_2_r1, 0, 0, 0.1396F);
		window_top_2_r1.texOffs(0, 0).addBox(0, -21, 3, 1, 21, 13, 0, false);

		door_2 = new ModelMapper(modelDataWrapper);
		door_2.setPos(0, 0, 0);
		door.addChild(door_2);
		door_2.texOffs(257, 114).addBox(19, -13, 3, 1, 6, 13, 0, true);

		window_bottom_3_r1 = new ModelMapper(modelDataWrapper);
		window_bottom_3_r1.setPos(20, -7, 0);
		door_2.addChild(window_bottom_3_r1);
		setRotationAngle(window_bottom_3_r1, 0, 0, 0.2094F);
		window_bottom_3_r1.texOffs(226, 106).addBox(-1, 0, 3, 1, 8, 13, 0, true);

		window_top_3_r1 = new ModelMapper(modelDataWrapper);
		window_top_3_r1.setPos(20, -13, 0);
		door_2.addChild(window_top_3_r1);
		setRotationAngle(window_top_3_r1, 0, 0, -0.1396F);
		window_top_3_r1.texOffs(0, 0).addBox(-1, -21, 3, 1, 21, 13, 0, true);

		door_exterior = new ModelMapper(modelDataWrapper);
		door_exterior.setPos(0, 24, 0);


		door_exterior_1 = new ModelMapper(modelDataWrapper);
		door_exterior_1.setPos(0, 0, 0);
		door_exterior.addChild(door_exterior_1);
		door_exterior_1.texOffs(183, 176).addBox(-20, -13, 3, 0, 6, 13, 0, false);

		window_bottom_1_r1 = new ModelMapper(modelDataWrapper);
		window_bottom_1_r1.setPos(-20, -7, 0);
		door_exterior_1.addChild(window_bottom_1_r1);
		setRotationAngle(window_bottom_1_r1, 0, 0, -0.2094F);
		window_bottom_1_r1.texOffs(132, 217).addBox(0, 0, 3, 0, 8, 13, 0, false);

		window_top_1_r1 = new ModelMapper(modelDataWrapper);
		window_top_1_r1.setPos(-20, -13, 0);
		door_exterior_1.addChild(window_top_1_r1);
		setRotationAngle(window_top_1_r1, 0, 0, 0.1396F);
		window_top_1_r1.texOffs(0, 21).addBox(0, -21, 3, 0, 21, 13, 0, false);

		door_exterior_2 = new ModelMapper(modelDataWrapper);
		door_exterior_2.setPos(0, 0, 0);
		door_exterior.addChild(door_exterior_2);
		door_exterior_2.texOffs(183, 176).addBox(20, -13, 3, 0, 6, 13, 0, true);

		window_bottom_2_r2 = new ModelMapper(modelDataWrapper);
		window_bottom_2_r2.setPos(20, -7, 0);
		door_exterior_2.addChild(window_bottom_2_r2);
		setRotationAngle(window_bottom_2_r2, 0, 0, 0.2094F);
		window_bottom_2_r2.texOffs(132, 217).addBox(0, 0, 3, 0, 8, 13, 0, true);

		window_top_2_r2 = new ModelMapper(modelDataWrapper);
		window_top_2_r2.setPos(20, -13, 0);
		door_exterior_2.addChild(window_top_2_r2);
		setRotationAngle(window_top_2_r2, 0, 0, -0.1396F);
		window_top_2_r2.texOffs(0, 21).addBox(0, -21, 3, 0, 21, 13, 0, true);

		door_exterior_end = new ModelMapper(modelDataWrapper);
		door_exterior_end.setPos(0, 24, 0);


		door_exterior_end_1 = new ModelMapper(modelDataWrapper);
		door_exterior_end_1.setPos(0, 0, 0);
		door_exterior_end.addChild(door_exterior_end_1);
		door_exterior_end_1.texOffs(101, 101).addBox(-20, -13, 3, 0, 6, 13, 0, false);

		window_bottom_2_r3 = new ModelMapper(modelDataWrapper);
		window_bottom_2_r3.setPos(-20, -7, 0);
		door_exterior_end_1.addChild(window_bottom_2_r3);
		setRotationAngle(window_bottom_2_r3, 0, 0, -0.2094F);
		window_bottom_2_r3.texOffs(101, 107).addBox(0, 0, 3, 0, 8, 13, 0, false);

		window_top_2_r3 = new ModelMapper(modelDataWrapper);
		window_top_2_r3.setPos(-20, -13, 0);
		door_exterior_end_1.addChild(window_top_2_r3);
		setRotationAngle(window_top_2_r3, 0, 0, 0.1396F);
		window_top_2_r3.texOffs(75, 101).addBox(0, -21, 3, 0, 21, 13, 0, false);

		door_exterior_end_2 = new ModelMapper(modelDataWrapper);
		door_exterior_end_2.setPos(0, 0, 0);
		door_exterior_end.addChild(door_exterior_end_2);
		door_exterior_end_2.texOffs(101, 101).addBox(20, -13, 3, 0, 6, 13, 0, true);

		window_bottom_3_r2 = new ModelMapper(modelDataWrapper);
		window_bottom_3_r2.setPos(20, -7, 0);
		door_exterior_end_2.addChild(window_bottom_3_r2);
		setRotationAngle(window_bottom_3_r2, 0, 0, 0.2094F);
		window_bottom_3_r2.texOffs(101, 107).addBox(0, 0, 3, 0, 8, 13, 0, true);

		window_top_3_r2 = new ModelMapper(modelDataWrapper);
		window_top_3_r2.setPos(20, -13, 0);
		door_exterior_end_2.addChild(window_top_3_r2);
		setRotationAngle(window_top_3_r2, 0, 0, -0.1396F);
		window_top_3_r2.texOffs(75, 101).addBox(0, -21, 3, 0, 21, 13, 0, true);

		roof_exterior = new ModelMapper(modelDataWrapper);
		roof_exterior.setPos(0, 24, 0);
		roof_exterior.texOffs(45, 84).addBox(-13, -39, -15, 13, 0, 30, 0, false);

		roof_2_r1 = new ModelMapper(modelDataWrapper);
		roof_2_r1.setPos(-10.1709F, -40.8501F, 0);
		roof_exterior.addChild(roof_2_r1);
		setRotationAngle(roof_2_r1, 0, 0, 1.0472F);
		roof_2_r1.texOffs(307, 194).addBox(0, 3, -15, 1, 3, 30, 0, false);

		roof_1_r2 = new ModelMapper(modelDataWrapper);
		roof_1_r2.setPos(-17.9382F, -34.7859F, 0);
		roof_exterior.addChild(roof_1_r2);
		setRotationAngle(roof_1_r2, 0, 0, 0.6981F);
		roof_1_r2.texOffs(221, 303).addBox(0, -4, -15, 1, 4, 30, 0, false);

		end = new ModelMapper(modelDataWrapper);
		end.setPos(0, 24, 0);
		end.texOffs(183, 201).addBox(9.5F, -34, 16.1F, 11, 34, 29, 0, false);
		end.texOffs(346, 40).addBox(11, -34, 2.9F, 9, 34, 0, 0, false);
		end.texOffs(243, 188).addBox(-20, -36, -8, 40, 36, 0, 0, false);
		end.texOffs(339, 190).addBox(-10, -34, 45, 20, 34, 0, 0, false);
		end.texOffs(132, 167).addBox(-20.5F, -34, 16.1F, 11, 34, 29, 0, false);
		end.texOffs(346, 0).addBox(-20, -34, 2.9F, 9, 34, 0, 0, false);

		end_pillar_4_r1 = new ModelMapper(modelDataWrapper);
		end_pillar_4_r1.setPos(-11, 0, 2.9F);
		end.addChild(end_pillar_4_r1);
		setRotationAngle(end_pillar_4_r1, 0, 1.2217F, 0);
		end_pillar_4_r1.texOffs(127, 161).addBox(0, -34, 0, 12, 34, 0, 0, false);

		end_pillar_3_r1 = new ModelMapper(modelDataWrapper);
		end_pillar_3_r1.setPos(11, 0, 2.9F);
		end.addChild(end_pillar_3_r1);
		setRotationAngle(end_pillar_3_r1, 0, -1.2217F, 0);
		end_pillar_3_r1.texOffs(339, 227).addBox(-12, -34, 0, 12, 34, 0, 0, false);

		end_side_1 = new ModelMapper(modelDataWrapper);
		end_side_1.setPos(0, 0, 0);
		end.addChild(end_side_1);
		end_side_1.texOffs(57, 0).addBox(-13, -34, -8, 13, 0, 53, 0, false);
		end_side_1.texOffs(14, 0).addBox(-18, 0, -15, 18, 0, 60, 0, false);
		end_side_1.texOffs(240, 0).addBox(-17, -34, 3, 4, 1, 13, 0, false);
		end_side_1.texOffs(171, 11).addBox(-20, -13, -15, 0, 6, 7, 0, false);
		end_side_1.texOffs(29, 53).addBox(-15, -36, -15, 6, 0, 7, 0, false);
		end_side_1.texOffs(47, 53).addBox(-7, -35, -15, 7, 0, 7, 0, false);
		end_side_1.texOffs(214, 277).addBox(-18, -31, -15, 6, 1, 7, 0, false);

		roof_3_r2 = new ModelMapper(modelDataWrapper);
		roof_3_r2.setPos(-7, -35, 0);
		end_side_1.addChild(roof_3_r2);
		setRotationAngle(roof_3_r2, 0, 0, 0.3491F);
		roof_3_r2.texOffs(41, 53).addBox(-3, 0, -15, 3, 0, 7, 0, false);

		roof_1_r3 = new ModelMapper(modelDataWrapper);
		roof_1_r3.setPos(-15, -36, 0);
		end_side_1.addChild(roof_1_r3);
		setRotationAngle(roof_1_r3, 0, 0, -0.7854F);
		roof_1_r3.texOffs(23, 53).addBox(-3, 0, -15, 3, 0, 7, 0, false);

		window_bottom_r23 = new ModelMapper(modelDataWrapper);
		window_bottom_r23.setPos(-21, -7, 0);
		end_side_1.addChild(window_bottom_r23);
		setRotationAngle(window_bottom_r23, 0, 0, -0.2094F);
		window_bottom_r23.texOffs(121, 95).addBox(1, 0, -15, 0, 8, 7, 0, false);

		window_top_r23 = new ModelMapper(modelDataWrapper);
		window_top_r23.setPos(-21, -13, 0);
		end_side_1.addChild(window_top_r23);
		setRotationAngle(window_top_r23, 0, 0, 0.1396F);
		window_top_r23.texOffs(75, 350).addBox(1, -22, -15, 0, 22, 7, 0, false);
		window_top_r23.texOffs(118, 136).addBox(1, -22, 3, 2, 1, 13, 0, false);

		end_side_2 = new ModelMapper(modelDataWrapper);
		end_side_2.setPos(0, 0, 0);
		end.addChild(end_side_2);
		end_side_2.texOffs(57, 0).addBox(0, -34, -8, 13, 0, 53, 0, true);
		end_side_2.texOffs(14, 0).addBox(0, 0, -15, 18, 0, 60, 0, true);
		end_side_2.texOffs(240, 0).addBox(13, -34, 3, 4, 1, 13, 0, true);
		end_side_2.texOffs(171, 11).addBox(20, -13, -15, 0, 6, 7, 0, true);
		end_side_2.texOffs(29, 53).addBox(9, -36, -15, 6, 0, 7, 0, true);
		end_side_2.texOffs(47, 53).addBox(0, -35, -15, 7, 0, 7, 0, true);
		end_side_2.texOffs(214, 277).addBox(12, -31, -15, 6, 1, 7, 0, true);

		roof_4_r1 = new ModelMapper(modelDataWrapper);
		roof_4_r1.setPos(7, -35, 0);
		end_side_2.addChild(roof_4_r1);
		setRotationAngle(roof_4_r1, 0, 0, -0.3491F);
		roof_4_r1.texOffs(41, 53).addBox(0, 0, -15, 3, 0, 7, 0, true);

		roof_2_r2 = new ModelMapper(modelDataWrapper);
		roof_2_r2.setPos(15, -36, 0);
		end_side_2.addChild(roof_2_r2);
		setRotationAngle(roof_2_r2, 0, 0, 0.7854F);
		roof_2_r2.texOffs(23, 53).addBox(0, 0, -15, 3, 0, 7, 0, true);

		window_bottom_r24 = new ModelMapper(modelDataWrapper);
		window_bottom_r24.setPos(21, -7, 0);
		end_side_2.addChild(window_bottom_r24);
		setRotationAngle(window_bottom_r24, 0, 0, 0.2094F);
		window_bottom_r24.texOffs(121, 95).addBox(-1, 0, -15, 0, 8, 7, 0, true);

		window_top_r24 = new ModelMapper(modelDataWrapper);
		window_top_r24.setPos(21, -13, 0);
		end_side_2.addChild(window_top_r24);
		setRotationAngle(window_top_r24, 0, 0, -0.1396F);
		window_top_r24.texOffs(75, 350).addBox(-1, -22, -15, 0, 22, 7, 0, true);
		window_top_r24.texOffs(118, 136).addBox(-3, -22, 3, 2, 1, 13, 0, true);

		end_exterior = new ModelMapper(modelDataWrapper);
		end_exterior.setPos(0, 24, 0);


		end_exterior_side_1 = new ModelMapper(modelDataWrapper);
		end_exterior_side_1.setPos(0, 0, 0);
		end_exterior.addChild(end_exterior_side_1);
		end_exterior_side_1.texOffs(180, 6).addBox(-21, -13, -15, 1, 6, 18, 0, false);
		end_exterior_side_1.texOffs(308, 233).addBox(-21, -13, 16, 1, 6, 29, 0, false);
		end_exterior_side_1.texOffs(245, 14).addBox(-21, 0, 3, 2, 1, 13, 0, false);
		end_exterior_side_1.texOffs(102, 342).addBox(-20.5F, -34, 45, 11, 34, 0, 0, false);
		end_exterior_side_1.texOffs(136, 0).addBox(-18, -39, 45, 18, 5, 0, 0, false);
		end_exterior_side_1.texOffs(158, 0).addBox(-19, 0, -15, 1, 2, 60, 0, false);

		door_top_r1 = new ModelMapper(modelDataWrapper);
		door_top_r1.setPos(-21, -13, 0);
		end_exterior_side_1.addChild(door_top_r1);
		setRotationAngle(door_top_r1, 0, 0, 0.1396F);
		door_top_r1.texOffs(126, 271).addBox(0, -22, 3, 1, 1, 13, 0, false);
		door_top_r1.texOffs(226, 106).addBox(0, -22, 16, 1, 22, 29, 0, false);
		door_top_r1.texOffs(340, 142).addBox(0, -22, -15, 1, 22, 18, 0, false);

		window_bottom_2_r4 = new ModelMapper(modelDataWrapper);
		window_bottom_2_r4.setPos(-21, -7, 0);
		end_exterior_side_1.addChild(window_bottom_2_r4);
		setRotationAngle(window_bottom_2_r4, 0, 0, -0.2094F);
		window_bottom_2_r4.texOffs(62, 302).addBox(0, 0, 16, 1, 8, 29, 0, false);
		window_bottom_2_r4.texOffs(220, 0).addBox(0, 0, -15, 1, 8, 18, 0, false);

		end_exterior_side_2 = new ModelMapper(modelDataWrapper);
		end_exterior_side_2.setPos(0, 0, 0);
		end_exterior.addChild(end_exterior_side_2);
		end_exterior_side_2.texOffs(180, 6).addBox(20, -13, -15, 1, 6, 18, 0, true);
		end_exterior_side_2.texOffs(308, 233).addBox(20, -13, 16, 1, 6, 29, 0, true);
		end_exterior_side_2.texOffs(245, 14).addBox(19, 0, 3, 2, 1, 13, 0, true);
		end_exterior_side_2.texOffs(102, 342).addBox(9.5F, -34, 45, 11, 34, 0, 0, true);
		end_exterior_side_2.texOffs(136, 0).addBox(0, -39, 45, 18, 5, 0, 0, true);
		end_exterior_side_2.texOffs(158, 0).addBox(18, 0, -15, 1, 2, 60, 0, true);

		door_top_r2 = new ModelMapper(modelDataWrapper);
		door_top_r2.setPos(21, -13, 0);
		end_exterior_side_2.addChild(door_top_r2);
		setRotationAngle(door_top_r2, 0, 0, -0.1396F);
		door_top_r2.texOffs(126, 271).addBox(-1, -22, 3, 1, 1, 13, 0, true);
		door_top_r2.texOffs(226, 106).addBox(-1, -22, 16, 1, 22, 29, 0, true);
		door_top_r2.texOffs(340, 142).addBox(-1, -22, -15, 1, 22, 18, 0, true);

		window_bottom_3_r3 = new ModelMapper(modelDataWrapper);
		window_bottom_3_r3.setPos(21, -7, 0);
		end_exterior_side_2.addChild(window_bottom_3_r3);
		setRotationAngle(window_bottom_3_r3, 0, 0, 0.2094F);
		window_bottom_3_r3.texOffs(62, 302).addBox(-1, 0, 16, 1, 8, 29, 0, true);
		window_bottom_3_r3.texOffs(220, 0).addBox(-1, 0, -15, 1, 8, 18, 0, true);

		roof_vent = new ModelMapper(modelDataWrapper);
		roof_vent.setPos(0, 24, 0);


		roof_vent_side_1 = new ModelMapper(modelDataWrapper);
		roof_vent_side_1.setPos(0, -42, 10);
		roof_vent.addChild(roof_vent_side_1);
		roof_vent_side_1.texOffs(76, 10).addBox(-6, 0, -70, 6, 4, 70, 0, false);

		vent_4_r1 = new ModelMapper(modelDataWrapper);
		vent_4_r1.setPos(0, 0, 0);
		roof_vent_side_1.addChild(vent_4_r1);
		setRotationAngle(vent_4_r1, -0.6981F, 0, 0);
		vent_4_r1.texOffs(10, 0).addBox(-6, 0, 0, 6, 0, 5, 0, false);

		vent_3_r1 = new ModelMapper(modelDataWrapper);
		vent_3_r1.setPos(-6, 0, 0);
		roof_vent_side_1.addChild(vent_3_r1);
		setRotationAngle(vent_3_r1, 0, 0.6981F, 1.1345F);
		vent_3_r1.texOffs(0, 0).addBox(0, 0, 0, 1, 6, 5, 0, false);

		vent_1_r1 = new ModelMapper(modelDataWrapper);
		vent_1_r1.setPos(-6, 0, -10);
		roof_vent_side_1.addChild(vent_1_r1);
		setRotationAngle(vent_1_r1, 0, 0, 1.1345F);
		vent_1_r1.texOffs(78, 85).addBox(0, 0, -60, 3, 6, 70, 0, false);

		roof_vent_side_2 = new ModelMapper(modelDataWrapper);
		roof_vent_side_2.setPos(0, -42, 10);
		roof_vent.addChild(roof_vent_side_2);
		roof_vent_side_2.texOffs(76, 10).addBox(0, 0, -70, 6, 4, 70, 0, true);

		vent_5_r1 = new ModelMapper(modelDataWrapper);
		vent_5_r1.setPos(0, 0, 0);
		roof_vent_side_2.addChild(vent_5_r1);
		setRotationAngle(vent_5_r1, -0.6981F, 0, 0);
		vent_5_r1.texOffs(10, 0).addBox(0, 0, 0, 6, 0, 5, 0, true);

		vent_4_r2 = new ModelMapper(modelDataWrapper);
		vent_4_r2.setPos(6, 0, 0);
		roof_vent_side_2.addChild(vent_4_r2);
		setRotationAngle(vent_4_r2, 0, -0.6981F, -1.1345F);
		vent_4_r2.texOffs(0, 0).addBox(-1, 0, 0, 1, 6, 5, 0, true);

		vent_2_r1 = new ModelMapper(modelDataWrapper);
		vent_2_r1.setPos(6, 0, -10);
		roof_vent_side_2.addChild(vent_2_r1);
		setRotationAngle(vent_2_r1, 0, 0, -1.1345F);
		vent_2_r1.texOffs(78, 85).addBox(-3, 0, -60, 3, 6, 70, 0, true);

		head_exterior = new ModelMapper(modelDataWrapper);
		head_exterior.setPos(0, 24, 0);


		head_side_1 = new ModelMapper(modelDataWrapper);
		head_side_1.setPos(0, 0, 0);
		head_exterior.addChild(head_side_1);
		head_side_1.texOffs(0, 80).addBox(-19, 0, -15, 1, 2, 73, 0, false);
		head_side_1.texOffs(157, 94).addBox(-21, -13, -15, 1, 6, 67, 0, false);
		head_side_1.texOffs(245, 14).addBox(-21, 0, -57, 2, 1, 13, 0, false);
		head_side_1.texOffs(188, 332).addBox(-21, -13, -44, 1, 6, 29, 0, false);
		head_side_1.texOffs(307, 164).addBox(-21, -13, -75, 1, 6, 18, 0, false);
		head_side_1.texOffs(158, 0).addBox(-19, 0, -75, 1, 2, 60, 0, false);

		window_top_1_r2 = new ModelMapper(modelDataWrapper);
		window_top_1_r2.setPos(-21, -13, 0);
		head_side_1.addChild(window_top_1_r2);
		setRotationAngle(window_top_1_r2, 0, 0, 0.1396F);
		window_top_1_r2.texOffs(352, 290).addBox(0, -22, -75, 1, 22, 18, 0, false);
		window_top_1_r2.texOffs(340, 239).addBox(0, -22, -44, 1, 22, 29, 0, false);
		window_top_1_r2.texOffs(126, 271).addBox(0, -22, -57, 1, 1, 13, 0, false);
		window_top_1_r2.texOffs(0, 155).addBox(0, -22, -15, 1, 22, 65, 0, false);

		window_bottom_1_r2 = new ModelMapper(modelDataWrapper);
		window_bottom_1_r2.setPos(-21, -7, 0);
		head_side_1.addChild(window_bottom_1_r2);
		setRotationAngle(window_bottom_1_r2, 0, 0, -0.2094F);
		window_bottom_1_r2.texOffs(353, 82).addBox(0, 0, -75, 1, 8, 18, 0, false);
		window_bottom_1_r2.texOffs(2, 302).addBox(0, 0, -44, 1, 8, 29, 0, false);
		window_bottom_1_r2.texOffs(0, 0).addBox(0, 0, -15, 1, 8, 72, 0, false);

		end_r1 = new ModelMapper(modelDataWrapper);
		end_r1.setPos(-19, 2, 33);
		head_side_1.addChild(end_r1);
		setRotationAngle(end_r1, 0, 0, -0.2618F);
		end_r1.texOffs(243, 149).addBox(0, 0, 0, 0, 11, 26, 0, false);

		roof_11_r1 = new ModelMapper(modelDataWrapper);
		roof_11_r1.setPos(-16.8732F, -11.8177F, 56.7312F);
		head_side_1.addChild(roof_11_r1);
		setRotationAngle(roof_11_r1, 0.1745F, -1.0472F, 0);
		roof_11_r1.texOffs(192, 302).addBox(-9.5F, -12, 0, 19, 24, 0, 0, false);

		roof_12_r1 = new ModelMapper(modelDataWrapper);
		roof_12_r1.setPos(-15.5128F, 10.8301F, 58.2552F);
		head_side_1.addChild(roof_12_r1);
		setRotationAngle(roof_12_r1, 0, -1.0472F, 0);
		roof_12_r1.texOffs(15, 5).addBox(-3.5F, -1.5F, 0, 7, 3, 0, 0, false);

		roof_11_r2 = new ModelMapper(modelDataWrapper);
		roof_11_r2.setPos(-15.5954F, 7.1646F, 60.6123F);
		head_side_1.addChild(roof_11_r2);
		setRotationAngle(roof_11_r2, -0.5236F, -1.0472F, 0);
		roof_11_r2.texOffs(154, 150).addBox(-5.5F, -2.5F, 0, 11, 5, 0, 0, false);

		roof_10_r1 = new ModelMapper(modelDataWrapper);
		roof_10_r1.setPos(-16.9278F, 3, 60.8042F);
		head_side_1.addChild(roof_10_r1);
		setRotationAngle(roof_10_r1, 0, -1.0472F, 0);
		roof_10_r1.texOffs(136, 5).addBox(-6, -3, 0, 12, 5, 0, 0, false);

		roof_10_r2 = new ModelMapper(modelDataWrapper);
		roof_10_r2.setPos(-7, 0, 70);
		head_side_1.addChild(roof_10_r2);
		setRotationAngle(roof_10_r2, 0.2618F, -0.5236F, 0);
		roof_10_r2.texOffs(108, 84).addBox(-9, -11, 0, 9, 11, 0, 0, false);

		roof_11_r3 = new ModelMapper(modelDataWrapper);
		roof_11_r3.setPos(-10.0747F, 10.7433F, 63.3255F);
		head_side_1.addChild(roof_11_r3);
		setRotationAngle(roof_11_r3, 0, -0.5236F, 0);
		roof_11_r3.texOffs(102, 68).addBox(-5, -1.5F, 0, 10, 3, 0, 0, false);

		roof_10_r3 = new ModelMapper(modelDataWrapper);
		roof_10_r3.setPos(-7, 5, 70);
		head_side_1.addChild(roof_10_r3);
		setRotationAngle(roof_10_r3, -0.7854F, -0.5236F, 0);
		roof_10_r3.texOffs(209, 189).addBox(-11, 0, 0, 11, 6, 0, 0, false);

		roof_9_r1 = new ModelMapper(modelDataWrapper);
		roof_9_r1.setPos(-7, 0, 70);
		head_side_1.addChild(roof_9_r1);
		setRotationAngle(roof_9_r1, 0, -0.5236F, 0);
		roof_9_r1.texOffs(108, 95).addBox(-8, 0, 0, 8, 5, 0, 0, false);

		roof_8_r1 = new ModelMapper(modelDataWrapper);
		roof_8_r1.setPos(-12.533F, -16.0189F, 57.5648F);
		head_side_1.addChild(roof_8_r1);
		setRotationAngle(roof_8_r1, -0.2618F, 0.7854F, 1.0472F);
		roof_8_r1.texOffs(75, 108).addBox(0, -6.5F, -14, 0, 13, 28, 0, false);

		roof_7_r1 = new ModelMapper(modelDataWrapper);
		roof_7_r1.setPos(-15.5298F, -29.8468F, 35.8167F);
		head_side_1.addChild(roof_7_r1);
		setRotationAngle(roof_7_r1, -0.1745F, 0.3491F, 1.0472F);
		roof_7_r1.texOffs(0, 30).addBox(0, -6, -15, 0, 12, 30, 0, false);

		roof_4_r2 = new ModelMapper(modelDataWrapper);
		roof_4_r2.setPos(-2, -42, -9);
		head_side_1.addChild(roof_4_r2);
		setRotationAngle(roof_4_r2, 0, 0, 1.5708F);
		roof_4_r2.texOffs(275, 230).addBox(0, -2, -6, 1, 4, 12, 0, false);

		roof_4_r3 = new ModelMapper(modelDataWrapper);
		roof_4_r3.setPos(-8.1369F, -39.4953F, 9.8171F);
		head_side_1.addChild(roof_4_r3);
		setRotationAngle(roof_4_r3, -0.1309F, 0.1309F, 1.3963F);
		roof_4_r3.texOffs(32, 249).addBox(0, -3.5F, -13.5F, 0, 7, 27, 0, false);

		roof_3_r3 = new ModelMapper(modelDataWrapper);
		roof_3_r3.setPos(-13.6527F, -37.467F, 10.5898F);
		head_side_1.addChild(roof_3_r3);
		setRotationAngle(roof_3_r3, -0.0873F, 0.0873F, 1.0472F);
		roof_3_r3.texOffs(94, 236).addBox(0, -3.5F, -14, 0, 7, 28, 0, false);

		roof_2_r3 = new ModelMapper(modelDataWrapper);
		roof_2_r3.setPos(-17.2178F, -34.6364F, 11.8407F);
		head_side_1.addChild(roof_2_r3);
		setRotationAngle(roof_2_r3, -0.0436F, 0.0436F, 0.6981F);
		roof_2_r3.texOffs(32, 240).addBox(0, -3, -15, 0, 6, 30, 0, false);

		roof_3_r4 = new ModelMapper(modelDataWrapper);
		roof_3_r4.setPos(-6.6372F, -40.9654F, -9);
		head_side_1.addChild(roof_3_r4);
		setRotationAngle(roof_3_r4, 0, 0, 1.3963F);
		roof_3_r4.texOffs(262, 2).addBox(-0.5F, -3.5F, -6, 1, 7, 12, 0, false);

		roof_2_r4 = new ModelMapper(modelDataWrapper);
		roof_2_r4.setPos(-12.519F, -38.9171F, -9);
		head_side_1.addChild(roof_2_r4);
		setRotationAngle(roof_2_r4, 0, 0, 1.0472F);
		roof_2_r4.texOffs(94, 271).addBox(-0.5F, -3, -6, 1, 6, 12, 0, false);

		roof_1_r4 = new ModelMapper(modelDataWrapper);
		roof_1_r4.setPos(-16.2696F, -35.9966F, -9);
		head_side_1.addChild(roof_1_r4);
		setRotationAngle(roof_1_r4, 0, 0, 0.6981F);
		roof_1_r4.texOffs(276, 265).addBox(-0.5F, -2, -6, 1, 4, 12, 0, false);

		head_side_2 = new ModelMapper(modelDataWrapper);
		head_side_2.setPos(0, 0, 0);
		head_exterior.addChild(head_side_2);
		head_side_2.texOffs(0, 80).addBox(18, 0, -15, 1, 2, 73, 0, true);
		head_side_2.texOffs(157, 94).addBox(20, -13, -15, 1, 6, 67, 0, true);
		head_side_2.texOffs(245, 14).addBox(19, 0, -57, 2, 1, 13, 0, true);
		head_side_2.texOffs(188, 332).addBox(20, -13, -44, 1, 6, 29, 0, true);
		head_side_2.texOffs(307, 164).addBox(20, -13, -75, 1, 6, 18, 0, true);
		head_side_2.texOffs(158, 0).addBox(18, 0, -75, 1, 2, 60, 0, true);

		window_top_2_r4 = new ModelMapper(modelDataWrapper);
		window_top_2_r4.setPos(21, -13, 0);
		head_side_2.addChild(window_top_2_r4);
		setRotationAngle(window_top_2_r4, 0, 0, -0.1396F);
		window_top_2_r4.texOffs(352, 290).addBox(-1, -22, -75, 1, 22, 18, 0, true);
		window_top_2_r4.texOffs(340, 239).addBox(-1, -22, -44, 1, 22, 29, 0, true);
		window_top_2_r4.texOffs(126, 271).addBox(-1, -22, -57, 1, 1, 13, 0, true);
		window_top_2_r4.texOffs(0, 155).addBox(-1, -22, -15, 1, 22, 65, 0, true);

		window_bottom_2_r5 = new ModelMapper(modelDataWrapper);
		window_bottom_2_r5.setPos(21, -7, 0);
		head_side_2.addChild(window_bottom_2_r5);
		setRotationAngle(window_bottom_2_r5, 0, 0, 0.2094F);
		window_bottom_2_r5.texOffs(353, 82).addBox(-1, 0, -75, 1, 8, 18, 0, true);
		window_bottom_2_r5.texOffs(2, 302).addBox(-1, 0, -44, 1, 8, 29, 0, true);
		window_bottom_2_r5.texOffs(0, 0).addBox(-1, 0, -15, 1, 8, 72, 0, true);

		end_r2 = new ModelMapper(modelDataWrapper);
		end_r2.setPos(19, 2, 33);
		head_side_2.addChild(end_r2);
		setRotationAngle(end_r2, 0, 0, 0.2618F);
		end_r2.texOffs(243, 149).addBox(0, 0, 0, 0, 11, 26, 0, true);

		roof_12_r2 = new ModelMapper(modelDataWrapper);
		roof_12_r2.setPos(16.8732F, -11.8177F, 56.7312F);
		head_side_2.addChild(roof_12_r2);
		setRotationAngle(roof_12_r2, 0.1745F, 1.0472F, 0);
		roof_12_r2.texOffs(192, 302).addBox(-9.5F, -12, 0, 19, 24, 0, 0, true);

		roof_13_r1 = new ModelMapper(modelDataWrapper);
		roof_13_r1.setPos(15.5128F, 10.8301F, 58.2552F);
		head_side_2.addChild(roof_13_r1);
		setRotationAngle(roof_13_r1, 0, 1.0472F, 0);
		roof_13_r1.texOffs(15, 5).addBox(-3.5F, -1.5F, 0, 7, 3, 0, 0, true);

		roof_12_r3 = new ModelMapper(modelDataWrapper);
		roof_12_r3.setPos(15.5954F, 7.1646F, 60.6123F);
		head_side_2.addChild(roof_12_r3);
		setRotationAngle(roof_12_r3, -0.5236F, 1.0472F, 0);
		roof_12_r3.texOffs(154, 150).addBox(-5.5F, -2.5F, 0, 11, 5, 0, 0, true);

		roof_11_r4 = new ModelMapper(modelDataWrapper);
		roof_11_r4.setPos(16.9278F, 3, 60.8042F);
		head_side_2.addChild(roof_11_r4);
		setRotationAngle(roof_11_r4, 0, 1.0472F, 0);
		roof_11_r4.texOffs(136, 5).addBox(-6, -3, 0, 12, 5, 0, 0, true);

		roof_11_r5 = new ModelMapper(modelDataWrapper);
		roof_11_r5.setPos(7, 0, 70);
		head_side_2.addChild(roof_11_r5);
		setRotationAngle(roof_11_r5, 0.2618F, 0.5236F, 0);
		roof_11_r5.texOffs(108, 84).addBox(0, -11, 0, 9, 11, 0, 0, true);

		roof_12_r4 = new ModelMapper(modelDataWrapper);
		roof_12_r4.setPos(10.0747F, 10.7433F, 63.3255F);
		head_side_2.addChild(roof_12_r4);
		setRotationAngle(roof_12_r4, 0, 0.5236F, 0);
		roof_12_r4.texOffs(102, 68).addBox(-5, -1.5F, 0, 10, 3, 0, 0, true);

		roof_11_r6 = new ModelMapper(modelDataWrapper);
		roof_11_r6.setPos(7, 5, 70);
		head_side_2.addChild(roof_11_r6);
		setRotationAngle(roof_11_r6, -0.7854F, 0.5236F, 0);
		roof_11_r6.texOffs(209, 189).addBox(0, 0, 0, 11, 6, 0, 0, true);

		roof_10_r4 = new ModelMapper(modelDataWrapper);
		roof_10_r4.setPos(7, 0, 70);
		head_side_2.addChild(roof_10_r4);
		setRotationAngle(roof_10_r4, 0, 0.5236F, 0);
		roof_10_r4.texOffs(108, 95).addBox(0, 0, 0, 8, 5, 0, 0, true);

		roof_9_r2 = new ModelMapper(modelDataWrapper);
		roof_9_r2.setPos(12.533F, -16.0189F, 57.5648F);
		head_side_2.addChild(roof_9_r2);
		setRotationAngle(roof_9_r2, -0.2618F, -0.7854F, -1.0472F);
		roof_9_r2.texOffs(75, 108).addBox(0, -6.5F, -14, 0, 13, 28, 0, true);

		roof_8_r2 = new ModelMapper(modelDataWrapper);
		roof_8_r2.setPos(15.5298F, -29.8468F, 35.8167F);
		head_side_2.addChild(roof_8_r2);
		setRotationAngle(roof_8_r2, -0.1745F, -0.3491F, -1.0472F);
		roof_8_r2.texOffs(0, 30).addBox(0, -6, -15, 0, 12, 30, 0, true);

		roof_5_r1 = new ModelMapper(modelDataWrapper);
		roof_5_r1.setPos(2, -42, -9);
		head_side_2.addChild(roof_5_r1);
		setRotationAngle(roof_5_r1, 0, 0, -1.5708F);
		roof_5_r1.texOffs(275, 230).addBox(-1, -2, -6, 1, 4, 12, 0, true);

		roof_5_r2 = new ModelMapper(modelDataWrapper);
		roof_5_r2.setPos(8.1369F, -39.4953F, 9.8171F);
		head_side_2.addChild(roof_5_r2);
		setRotationAngle(roof_5_r2, -0.1309F, -0.1309F, -1.3963F);
		roof_5_r2.texOffs(32, 249).addBox(0, -3.5F, -13.5F, 0, 7, 27, 0, true);

		roof_4_r4 = new ModelMapper(modelDataWrapper);
		roof_4_r4.setPos(13.6527F, -37.467F, 10.5898F);
		head_side_2.addChild(roof_4_r4);
		setRotationAngle(roof_4_r4, -0.0873F, -0.0873F, -1.0472F);
		roof_4_r4.texOffs(94, 236).addBox(0, -3.5F, -14, 0, 7, 28, 0, true);

		roof_3_r5 = new ModelMapper(modelDataWrapper);
		roof_3_r5.setPos(17.2178F, -34.6364F, 11.8407F);
		head_side_2.addChild(roof_3_r5);
		setRotationAngle(roof_3_r5, -0.0436F, -0.0436F, -0.6981F);
		roof_3_r5.texOffs(32, 240).addBox(0, -3, -15, 0, 6, 30, 0, true);

		roof_4_r5 = new ModelMapper(modelDataWrapper);
		roof_4_r5.setPos(6.6372F, -40.9654F, -9);
		head_side_2.addChild(roof_4_r5);
		setRotationAngle(roof_4_r5, 0, 0, -1.3963F);
		roof_4_r5.texOffs(262, 2).addBox(-0.5F, -3.5F, -6, 1, 7, 12, 0, true);

		roof_3_r6 = new ModelMapper(modelDataWrapper);
		roof_3_r6.setPos(12.519F, -38.9171F, -9);
		head_side_2.addChild(roof_3_r6);
		setRotationAngle(roof_3_r6, 0, 0, -1.0472F);
		roof_3_r6.texOffs(94, 271).addBox(-0.5F, -3, -6, 1, 6, 12, 0, true);

		roof_2_r5 = new ModelMapper(modelDataWrapper);
		roof_2_r5.setPos(16.2696F, -35.9966F, -9);
		head_side_2.addChild(roof_2_r5);
		setRotationAngle(roof_2_r5, 0, 0, -0.6981F);
		roof_2_r5.texOffs(276, 265).addBox(-0.5F, -2, -6, 1, 4, 12, 0, true);

		middle = new ModelMapper(modelDataWrapper);
		middle.setPos(0, 0, 0);
		head_exterior.addChild(middle);
		middle.texOffs(0, 55).addBox(-7, 0, 70, 14, 5, 0, 0, false);
		middle.texOffs(74, 68).addBox(-7, 9.2433F, 65.7571F, 14, 3, 0, 0, false);
		middle.texOffs(88, 376).addBox(-19, 0, 56, 38, 1, 14, 0, false);

		roof_8_r3 = new ModelMapper(modelDataWrapper);
		roof_8_r3.setPos(0, 0, 70);
		middle.addChild(roof_8_r3);
		setRotationAngle(roof_8_r3, 0.3491F, 0, 0);
		roof_8_r3.texOffs(276, 0).addBox(-8, -11, 0, 16, 11, 0, 0, false);

		roof_7_r2 = new ModelMapper(modelDataWrapper);
		roof_7_r2.setPos(0, -18.733F, 57.665F);
		middle.addChild(roof_7_r2);
		setRotationAngle(roof_7_r2, 0, -0.7854F, -1.5708F);
		roof_7_r2.texOffs(158, 249).addBox(0, -10, -12.5F, 0, 20, 25, 0, false);

		roof_6_r1 = new ModelMapper(modelDataWrapper);
		roof_6_r1.setPos(0, -32.7021F, 34.7308F);
		middle.addChild(roof_6_r1);
		setRotationAngle(roof_6_r1, 0, -0.3491F, -1.5708F);
		roof_6_r1.texOffs(0, 50).addBox(0, -13, -15, 0, 26, 30, 0, false);

		roof_5_r3 = new ModelMapper(modelDataWrapper);
		roof_5_r3.setPos(0, -42, -3);
		middle.addChild(roof_5_r3);
		setRotationAngle(roof_5_r3, 0, -0.1745F, -1.5708F);
		roof_5_r3.texOffs(33, 278).addBox(0, -10, 0, 0, 20, 24, 0, false);

		roof_9_r3 = new ModelMapper(modelDataWrapper);
		roof_9_r3.setPos(0, 5, 70);
		middle.addChild(roof_9_r3);
		setRotationAngle(roof_9_r3, -0.7854F, 0, 0);
		roof_9_r3.texOffs(180, 0).addBox(-7, 0, 0, 14, 6, 0, 0, false);

		bottom_middle = new ModelMapper(modelDataWrapper);
		bottom_middle.setPos(0, 24, 0);
		bottom_middle.texOffs(150, 234).addBox(-19, 2, -15, 1, 10, 30, 0, false);

		bottom_end = new ModelMapper(modelDataWrapper);
		bottom_end.setPos(0, 24, 0);
		bottom_end.texOffs(0, 264).addBox(-19, 2, -15, 1, 10, 14, 0, false);

		modelDataWrapper.setModelPart(textureWidth, textureHeight);
		window.setModelPart();
		window_exterior_1.setModelPart();
		window_exterior_2.setModelPart();
		window_exterior_3.setModelPart();
		window_exterior_4.setModelPart();
		window_exterior_5.setModelPart();
		window_exterior_6.setModelPart();
		window_exterior_7.setModelPart();
		window_exterior_8.setModelPart();
		window_exterior_9.setModelPart();
		window_end_exterior_1.setModelPart();
		window_end_exterior_2.setModelPart();
		window_end_exterior_3.setModelPart();
		window_end_exterior_4.setModelPart();
		window_end_exterior_5.setModelPart();
		window_end_exterior_6.setModelPart();
		door.setModelPart();
		door_1.setModelPart(door.name);
		door_2.setModelPart(door.name);
		door_exterior.setModelPart();
		door_exterior_1.setModelPart(door_exterior.name);
		door_exterior_2.setModelPart(door_exterior.name);
		door_exterior_end.setModelPart();
		door_exterior_end_1.setModelPart(door_exterior_end.name);
		door_exterior_end_2.setModelPart(door_exterior_end.name);
		roof_exterior.setModelPart();
		end.setModelPart();
		end_exterior.setModelPart();
		roof_vent.setModelPart();
		head_exterior.setModelPart();
	}

	private static final int DOOR_MAX = 13;

	@Override
	protected void renderWindowPositions(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
		final ModelMapper[] windowParts = isEnd1Head || isEnd2Head ? windowEndParts() : windowParts();
		final int windowPartsLength = windowParts.length;
		final int offset = windowParts().length / 2;
		switch (renderStage) {
			case INTERIOR:
				for (int i = 0; i < (isEnd1Head && isEnd2Head ? windowPartsLength - offset + 1 : windowPartsLength); i++) {
					renderMirror(window, matrices, vertices, light, (isEnd2Head ? -1 : 1) * (i - windowPartsLength + offset + 1) * 30);
				}
				if (renderDetails) {
					// TODO seats
				}
				break;
			case EXTERIOR:
				if (isEnd1Head || isEnd2Head) {
					for (int i = 0; i < (isEnd1Head && isEnd2Head ? windowPartsLength - offset + 1 : windowPartsLength); i++) {
						final int newPosition = (isEnd2Head ? -1 : 1) * (i - windowPartsLength + offset + 1) * 30;
						renderOnce(windowParts[i], matrices, vertices, light, newPosition);
						renderMirror(roof_exterior, matrices, vertices, light, newPosition);
					}
				} else {
					for (int i = 0; i < windowPartsLength; i++) {
						renderOnce(windowParts[i], matrices, vertices, light, (i - offset) * 30);
					}
					for (int i = 0; i < windowPartsLength; i++) {
						final int newPosition = (i - offset) * 30;
						renderOnceFlipped(windowParts[windowPartsLength - i - 1], matrices, vertices, light, newPosition);
						renderMirror(roof_exterior, matrices, vertices, light, newPosition);
					}
				}
				break;
		}
	}

	@Override
	protected void renderDoorPositions(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
		final boolean firstDoor = isIndex(0, position, getDoorPositions());
		final int doorOffset = (isEnd1Head && firstDoor ? 90 : 0) + (isEnd2Head && !firstDoor ? -90 : 0);
		final boolean doorOpen = doorLeftZ > 0 || doorRightZ > 0;
		// TODO door lights

		switch (renderStage) {
			case INTERIOR:
				if (firstDoor) {
					door_1.setOffset(0, 0, -doorLeftZ);
					door_2.setOffset(0, 0, doorRightZ);
					renderOnceFlipped(door, matrices, vertices, light, position + doorOffset);
				} else {
					door_1.setOffset(0, 0, doorRightZ);
					door_2.setOffset(0, 0, -doorLeftZ);
					renderOnce(door, matrices, vertices, light, position + doorOffset);
				}
				break;
			case EXTERIOR:
				if (isEnd1Head && firstDoor || isEnd2Head && !firstDoor) {
					if (firstDoor) {
						door_exterior_end_1.setOffset(0, 0, -doorLeftZ);
						door_exterior_end_2.setOffset(0, 0, doorRightZ);
						renderOnceFlipped(door_exterior_end, matrices, vertices, light, position + doorOffset);
					} else {
						door_exterior_end_1.setOffset(0, 0, doorRightZ);
						door_exterior_end_2.setOffset(0, 0, -doorLeftZ);
						renderOnce(door_exterior_end, matrices, vertices, light, position + doorOffset);
					}
				} else {
					if (firstDoor) {
						door_exterior_1.setOffset(0, 0, -doorLeftZ);
						door_exterior_2.setOffset(0, 0, doorRightZ);
						renderOnceFlipped(door_exterior, matrices, vertices, light, position);
					} else {
						door_exterior_1.setOffset(0, 0, doorRightZ);
						door_exterior_2.setOffset(0, 0, -doorLeftZ);
						renderOnce(door_exterior, matrices, vertices, light, position);
					}
				}
				break;
		}
	}

	@Override
	protected void renderHeadPosition1(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean useHeadlights) {
		switch (renderStage) {
			case ALWAYS_ON_LIGHTS:
				// TODO headlights and tail lights
				break;
			case INTERIOR:
				renderOnceFlipped(end, matrices, vertices, light, position + 90);
				break;
			case EXTERIOR:
				renderOnceFlipped(head_exterior, matrices, vertices, light, position + 30);
				renderMirror(roof_exterior, matrices, vertices, light, position + 60);
				renderMirror(roof_exterior, matrices, vertices, light, position + 90);
				break;
		}
	}

	@Override
	protected void renderHeadPosition2(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean useHeadlights) {
		switch (renderStage) {
			case ALWAYS_ON_LIGHTS:
				// TODO headlights and tail lights
				break;
			case INTERIOR:
				renderOnce(end, matrices, vertices, light, position - 90);
				break;
			case EXTERIOR:
				renderOnce(head_exterior, matrices, vertices, light, position - 30);
				renderMirror(roof_exterior, matrices, vertices, light, position - 60);
				renderMirror(roof_exterior, matrices, vertices, light, position - 90);
				break;
		}
	}

	@Override
	protected void renderEndPosition1(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
		switch (renderStage) {
			case LIGHTS:
				// TODO interior lights
				break;
			case INTERIOR:
				renderOnceFlipped(end, matrices, vertices, light, position);
				break;
			case EXTERIOR:
				renderOnceFlipped(end_exterior, matrices, vertices, light, position);
				renderMirror(roof_exterior, matrices, vertices, light, position);
				renderMirror(roof_exterior, matrices, vertices, light, position - 30);
				renderOnceFlipped(roof_vent, matrices, vertices, light, position);
				break;
		}
	}

	@Override
	protected void renderEndPosition2(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
		switch (renderStage) {
			case LIGHTS:
				// TODO interior lights
				break;
			case INTERIOR:
				renderOnce(end, matrices, vertices, light, position);
				break;
			case EXTERIOR:
				renderOnce(end_exterior, matrices, vertices, light, position);
				renderMirror(roof_exterior, matrices, vertices, light, position);
				renderMirror(roof_exterior, matrices, vertices, light, position + 30);
				renderOnce(roof_vent, matrices, vertices, light, position);
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

	@Override
	protected int[] getBogiePositions() {
		return new int[]{-124, 124};
	}

	@Override
	protected float getDoorAnimationX(float value, boolean opening) {
		return 0;
	}

	@Override
	protected float getDoorAnimationZ(float value, boolean opening) {
		return smoothEnds(0, DOOR_MAX, 0, 0.5F, value);
	}

	protected ModelMapper[] windowParts() {
		return new ModelMapper[]{window_exterior_1, window_exterior_2, window_exterior_3, window_exterior_4, window_exterior_5, window_exterior_6, window_exterior_7, window_exterior_8, window_exterior_9};
	}

	protected ModelMapper[] windowEndParts() {
		return new ModelMapper[]{window_end_exterior_1, window_end_exterior_2, window_end_exterior_3, window_end_exterior_4, window_end_exterior_5, window_end_exterior_6};
	}
}