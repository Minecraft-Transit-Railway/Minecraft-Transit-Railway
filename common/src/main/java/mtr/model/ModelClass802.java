package mtr.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mtr.mappings.ModelDataWrapper;
import mtr.mappings.ModelMapper;

public class ModelClass802 extends ModelSimpleTrainBase {

	private final ModelMapper window;
	private final ModelMapper roof_3_r1;
	private final ModelMapper roof_1_r1;
	private final ModelMapper window_bottom_r1;
	private final ModelMapper window_top_r1;
	private final ModelMapper window_light;
	private final ModelMapper window_exterior_1;
	private final ModelMapper roof_2_r1;
	private final ModelMapper roof_1_r2;
	private final ModelMapper window_bottom_r2;
	private final ModelMapper window_top_r2;
	private final ModelMapper window_exterior_2;
	private final ModelMapper roof_2_r2;
	private final ModelMapper roof_1_r3;
	private final ModelMapper window_bottom_r3;
	private final ModelMapper window_top_r3;
	private final ModelMapper window_exterior_3;
	private final ModelMapper roof_2_r3;
	private final ModelMapper roof_1_r4;
	private final ModelMapper window_bottom_r4;
	private final ModelMapper window_top_r4;
	private final ModelMapper window_exterior_4;
	private final ModelMapper roof_2_r4;
	private final ModelMapper roof_1_r5;
	private final ModelMapper window_bottom_r5;
	private final ModelMapper window_top_r5;
	private final ModelMapper window_exterior_5;
	private final ModelMapper roof_2_r5;
	private final ModelMapper roof_1_r6;
	private final ModelMapper window_bottom_r6;
	private final ModelMapper window_top_r6;
	private final ModelMapper window_exterior_6;
	private final ModelMapper roof_2_r6;
	private final ModelMapper roof_1_r7;
	private final ModelMapper window_bottom_r7;
	private final ModelMapper window_top_r7;
	private final ModelMapper window_exterior_7;
	private final ModelMapper roof_2_r7;
	private final ModelMapper roof_1_r8;
	private final ModelMapper window_bottom_r8;
	private final ModelMapper window_top_r8;
	private final ModelMapper window_exterior_8;
	private final ModelMapper roof_2_r8;
	private final ModelMapper roof_1_r9;
	private final ModelMapper window_bottom_r9;
	private final ModelMapper window_top_r9;
	private final ModelMapper window_exterior_9;
	private final ModelMapper roof_2_r9;
	private final ModelMapper roof_1_r10;
	private final ModelMapper window_bottom_r10;
	private final ModelMapper window_top_r10;
	private final ModelMapper window_end_exterior_1;
	private final ModelMapper window_end_exterior_1_1;
	private final ModelMapper roof_2_r10;
	private final ModelMapper roof_1_r11;
	private final ModelMapper window_bottom_r11;
	private final ModelMapper window_top_r11;
	private final ModelMapper window_end_exterior_1_2;
	private final ModelMapper roof_3_r2;
	private final ModelMapper roof_2_r11;
	private final ModelMapper window_bottom_r12;
	private final ModelMapper window_top_r12;
	private final ModelMapper window_end_exterior_2;
	private final ModelMapper window_end_exterior_2_1;
	private final ModelMapper roof_3_r3;
	private final ModelMapper roof_2_r12;
	private final ModelMapper window_bottom_r13;
	private final ModelMapper window_top_r13;
	private final ModelMapper window_end_exterior_2_2;
	private final ModelMapper roof_4_r1;
	private final ModelMapper roof_3_r4;
	private final ModelMapper window_bottom_r14;
	private final ModelMapper window_top_r14;
	private final ModelMapper window_end_exterior_3;
	private final ModelMapper window_end_exterior_3_1;
	private final ModelMapper roof_4_r2;
	private final ModelMapper roof_3_r5;
	private final ModelMapper window_bottom_r15;
	private final ModelMapper window_top_r15;
	private final ModelMapper window_end_exterior_3_2;
	private final ModelMapper roof_5_r1;
	private final ModelMapper roof_4_r3;
	private final ModelMapper window_bottom_r16;
	private final ModelMapper window_top_r16;
	private final ModelMapper window_end_exterior_4;
	private final ModelMapper window_end_exterior_4_1;
	private final ModelMapper roof_4_r4;
	private final ModelMapper roof_3_r6;
	private final ModelMapper window_bottom_r17;
	private final ModelMapper window_top_r17;
	private final ModelMapper window_end_exterior_4_2;
	private final ModelMapper roof_5_r2;
	private final ModelMapper roof_4_r5;
	private final ModelMapper window_bottom_r18;
	private final ModelMapper window_top_r18;
	private final ModelMapper window_end_exterior_5;
	private final ModelMapper window_end_exterior_5_1;
	private final ModelMapper roof_5_r3;
	private final ModelMapper roof_4_r6;
	private final ModelMapper window_bottom_r19;
	private final ModelMapper window_top_r19;
	private final ModelMapper window_end_exterior_5_2;
	private final ModelMapper roof_6_r1;
	private final ModelMapper roof_5_r4;
	private final ModelMapper window_bottom_r20;
	private final ModelMapper window_top_r20;
	private final ModelMapper window_end_exterior_6;
	private final ModelMapper window_end_exterior_6_1;
	private final ModelMapper roof_6_r2;
	private final ModelMapper roof_5_r5;
	private final ModelMapper window_bottom_r21;
	private final ModelMapper window_top_r21;
	private final ModelMapper window_end_exterior_6_2;
	private final ModelMapper roof_7_r1;
	private final ModelMapper roof_6_r3;
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
	private final ModelMapper end;
	private final ModelMapper end_pillar_4_r1;
	private final ModelMapper end_pillar_3_r1;
	private final ModelMapper end_side_1;
	private final ModelMapper roof_3_r7;
	private final ModelMapper roof_1_r12;
	private final ModelMapper window_bottom_r23;
	private final ModelMapper window_top_r23;
	private final ModelMapper end_side_2;
	private final ModelMapper roof_4_r7;
	private final ModelMapper roof_2_r13;
	private final ModelMapper window_bottom_r24;
	private final ModelMapper window_top_r24;
	private final ModelMapper end_light;
	private final ModelMapper end_translucent;
	private final ModelMapper end_exterior;
	private final ModelMapper end_exterior_side_1;
	private final ModelMapper roof_4_r8;
	private final ModelMapper roof_3_r8;
	private final ModelMapper door_top_r1;
	private final ModelMapper window_bottom_2_r4;
	private final ModelMapper end_exterior_side_2;
	private final ModelMapper roof_5_r6;
	private final ModelMapper roof_4_r9;
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
	private final ModelMapper roof_4_r10;
	private final ModelMapper roof_3_r9;
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
	private final ModelMapper roof_7_r2;
	private final ModelMapper roof_4_r11;
	private final ModelMapper roof_4_r12;
	private final ModelMapper roof_3_r10;
	private final ModelMapper roof_2_r14;
	private final ModelMapper roof_3_r11;
	private final ModelMapper roof_2_r15;
	private final ModelMapper roof_1_r13;
	private final ModelMapper head_side_2;
	private final ModelMapper roof_5_r7;
	private final ModelMapper roof_4_r13;
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
	private final ModelMapper roof_8_r1;
	private final ModelMapper roof_5_r8;
	private final ModelMapper roof_5_r9;
	private final ModelMapper roof_4_r14;
	private final ModelMapper roof_3_r12;
	private final ModelMapper roof_4_r15;
	private final ModelMapper roof_3_r13;
	private final ModelMapper roof_2_r16;
	private final ModelMapper middle;
	private final ModelMapper roof_8_r2;
	private final ModelMapper roof_6_r4;
	private final ModelMapper roof_5_r10;
	private final ModelMapper roof_9_r2;
	private final ModelMapper bottom_middle;
	private final ModelMapper bottom_end;
	private final ModelMapper seat;
	private final ModelMapper seat_2_r1;
	private final ModelMapper headlights;
	private final ModelMapper headlight_2_r1;
	private final ModelMapper headlight_1_r1;
	private final ModelMapper tail_lights;
	private final ModelMapper tail_light_2_r1;
	private final ModelMapper tail_light_1_r1;
	private final ModelMapper door_light_off;
	private final ModelMapper door_light_off_r1;
	private final ModelMapper door_light_on;
	private final ModelMapper door_light_on_r1;

	public ModelClass802() {
		final int textureWidth = 432;
		final int textureHeight = 432;

		final ModelDataWrapper modelDataWrapper = new ModelDataWrapper(this, textureWidth, textureHeight);

		window = new ModelMapper(modelDataWrapper);
		window.setPos(0, 24, 0);
		window.texOffs(230, 139).addBox(-19, 0, -15, 19, 1, 30, 0, false);
		window.texOffs(82, 201).addBox(-20, -13, -15, 0, 6, 30, 0, false);
		window.texOffs(30, 53).addBox(-15, -36, -15, 6, 0, 30, 0, false);
		window.texOffs(0, 0).addBox(-7, -35, -15, 7, 0, 30, 0, false);
		window.texOffs(237, 36).addBox(-18, -31, -15, 6, 1, 30, 0, false);

		roof_3_r1 = new ModelMapper(modelDataWrapper);
		roof_3_r1.setPos(-7, -35, 0);
		window.addChild(roof_3_r1);
		setRotationAngle(roof_3_r1, 0, 0, 0.3491F);
		roof_3_r1.texOffs(14, 0).addBox(-3, 0, -15, 3, 0, 30, 0, false);

		roof_1_r1 = new ModelMapper(modelDataWrapper);
		roof_1_r1.setPos(-15, -36, 0);
		window.addChild(roof_1_r1);
		setRotationAngle(roof_1_r1, 0, 0, -0.7854F);
		roof_1_r1.texOffs(49, 0).addBox(-3, 0, -15, 3, 0, 30, 0, false);

		window_bottom_r1 = new ModelMapper(modelDataWrapper);
		window_bottom_r1.setPos(-21, -7, 0);
		window.addChild(window_bottom_r1);
		setRotationAngle(window_bottom_r1, 0, 0, -0.2094F);
		window_bottom_r1.texOffs(0, 120).addBox(1, 0, -15, 0, 8, 30, 0, false);

		window_top_r1 = new ModelMapper(modelDataWrapper);
		window_top_r1.setPos(-21, -13, 0);
		window.addChild(window_top_r1);
		setRotationAngle(window_top_r1, 0, 0, 0.1396F);
		window_top_r1.texOffs(227, 205).addBox(1, -22, -15, 0, 22, 30, 0, false);

		window_light = new ModelMapper(modelDataWrapper);
		window_light.setPos(0, 24, 0);
		window_light.texOffs(283, 44).addBox(-7, -35.1F, -15, 4, 0, 30, 0, false);

		window_exterior_1 = new ModelMapper(modelDataWrapper);
		window_exterior_1.setPos(0, 24, 0);
		window_exterior_1.texOffs(32, 251).addBox(-21, -13, -15, 0, 6, 30, 0, false);

		roof_2_r1 = new ModelMapper(modelDataWrapper);
		roof_2_r1.setPos(-10.1709F, -40.8501F, 0);
		window_exterior_1.addChild(roof_2_r1);
		setRotationAngle(roof_2_r1, 0, 0, 1.0472F);
		roof_2_r1.texOffs(32, 356).addBox(0, 3, -15, 1, 3, 30, 0, false);

		roof_1_r2 = new ModelMapper(modelDataWrapper);
		roof_1_r2.setPos(-17.9382F, -34.7859F, 0);
		window_exterior_1.addChild(roof_1_r2);
		setRotationAngle(roof_1_r2, 0, 0, 0.6981F);
		roof_1_r2.texOffs(299, 311).addBox(0, -4, -15, 0, 4, 30, 0, false);

		window_bottom_r2 = new ModelMapper(modelDataWrapper);
		window_bottom_r2.setPos(-21, -7, 0);
		window_exterior_1.addChild(window_bottom_r2);
		setRotationAngle(window_bottom_r2, 0, 0, -0.2094F);
		window_bottom_r2.texOffs(203, 313).addBox(0, 0, -15, 1, 8, 30, 0, false);

		window_top_r2 = new ModelMapper(modelDataWrapper);
		window_top_r2.setPos(-21, -13, 0);
		window_exterior_1.addChild(window_top_r2);
		setRotationAngle(window_top_r2, 0, 0, 0.1396F);
		window_top_r2.texOffs(82, 229).addBox(0, -22, -15, 0, 22, 30, 0, false);

		window_exterior_2 = new ModelMapper(modelDataWrapper);
		window_exterior_2.setPos(0, 24, 0);
		window_exterior_2.texOffs(160, 198).addBox(-21, -13, -15, 0, 6, 30, 0, false);

		roof_2_r2 = new ModelMapper(modelDataWrapper);
		roof_2_r2.setPos(-10.1709F, -40.8501F, 0);
		window_exterior_2.addChild(roof_2_r2);
		setRotationAngle(roof_2_r2, 0, 0, 1.0472F);
		roof_2_r2.texOffs(331, 355).addBox(0, 3, -15, 1, 3, 30, 0, false);

		roof_1_r3 = new ModelMapper(modelDataWrapper);
		roof_1_r3.setPos(-17.9382F, -34.7859F, 0);
		window_exterior_2.addChild(roof_1_r3);
		setRotationAngle(roof_1_r3, 0, 0, 0.6981F);
		roof_1_r3.texOffs(299, 307).addBox(0, -4, -15, 0, 4, 30, 0, false);

		window_bottom_r3 = new ModelMapper(modelDataWrapper);
		window_bottom_r3.setPos(-21, -7, 0);
		window_exterior_2.addChild(window_bottom_r3);
		setRotationAngle(window_bottom_r3, 0, 0, -0.2094F);
		window_bottom_r3.texOffs(313, 54).addBox(0, 0, -15, 1, 8, 30, 0, false);

		window_top_r3 = new ModelMapper(modelDataWrapper);
		window_top_r3.setPos(-21, -13, 0);
		window_exterior_2.addChild(window_top_r3);
		setRotationAngle(window_top_r3, 0, 0, 0.1396F);
		window_top_r3.texOffs(160, 164).addBox(0, -22, -15, 0, 22, 30, 0, false);

		window_exterior_3 = new ModelMapper(modelDataWrapper);
		window_exterior_3.setPos(0, 24, 0);
		window_exterior_3.texOffs(160, 192).addBox(-21, -13, -15, 0, 6, 30, 0, false);

		roof_2_r3 = new ModelMapper(modelDataWrapper);
		roof_2_r3.setPos(-10.1709F, -40.8501F, 0);
		window_exterior_3.addChild(roof_2_r3);
		setRotationAngle(roof_2_r3, 0, 0, 1.0472F);
		roof_2_r3.texOffs(221, 354).addBox(0, 3, -15, 1, 3, 30, 0, false);

		roof_1_r4 = new ModelMapper(modelDataWrapper);
		roof_1_r4.setPos(-17.9382F, -34.7859F, 0);
		window_exterior_3.addChild(roof_1_r4);
		setRotationAngle(roof_1_r4, 0, 0, 0.6981F);
		roof_1_r4.texOffs(299, 303).addBox(0, -4, -15, 0, 4, 30, 0, false);

		window_bottom_r4 = new ModelMapper(modelDataWrapper);
		window_bottom_r4.setPos(-21, -7, 0);
		window_exterior_3.addChild(window_bottom_r4);
		setRotationAngle(window_bottom_r4, 0, 0, -0.2094F);
		window_bottom_r4.texOffs(0, 312).addBox(0, 0, -15, 1, 8, 30, 0, false);

		window_top_r4 = new ModelMapper(modelDataWrapper);
		window_top_r4.setPos(-21, -13, 0);
		window_exterior_3.addChild(window_top_r4);
		setRotationAngle(window_top_r4, 0, 0, 0.1396F);
		window_top_r4.texOffs(98, 164).addBox(0, -22, -15, 0, 22, 30, 0, false);

		window_exterior_4 = new ModelMapper(modelDataWrapper);
		window_exterior_4.setPos(0, 24, 0);
		window_exterior_4.texOffs(98, 192).addBox(-21, -13, -15, 0, 6, 30, 0, false);

		roof_2_r4 = new ModelMapper(modelDataWrapper);
		roof_2_r4.setPos(-10.1709F, -40.8501F, 0);
		window_exterior_4.addChild(roof_2_r4);
		setRotationAngle(roof_2_r4, 0, 0, 1.0472F);
		roof_2_r4.texOffs(299, 352).addBox(0, 3, -15, 1, 3, 30, 0, false);

		roof_1_r5 = new ModelMapper(modelDataWrapper);
		roof_1_r5.setPos(-17.9382F, -34.7859F, 0);
		window_exterior_4.addChild(roof_1_r5);
		setRotationAngle(roof_1_r5, 0, 0, 0.6981F);
		roof_1_r5.texOffs(299, 299).addBox(0, -4, -15, 0, 4, 30, 0, false);

		window_bottom_r5 = new ModelMapper(modelDataWrapper);
		window_bottom_r5.setPos(-21, -7, 0);
		window_exterior_4.addChild(window_bottom_r5);
		setRotationAngle(window_bottom_r5, 0, 0, -0.2094F);
		window_bottom_r5.texOffs(94, 310).addBox(0, 0, -15, 1, 8, 30, 0, false);

		window_top_r5 = new ModelMapper(modelDataWrapper);
		window_top_r5.setPos(-21, -13, 0);
		window_exterior_4.addChild(window_top_r5);
		setRotationAngle(window_top_r5, 0, 0, 0.1396F);
		window_top_r5.texOffs(161, 0).addBox(0, -22, -15, 0, 22, 30, 0, false);

		window_exterior_5 = new ModelMapper(modelDataWrapper);
		window_exterior_5.setPos(0, 24, 0);
		window_exterior_5.texOffs(0, 192).addBox(-21, -13, -15, 0, 6, 30, 0, false);

		roof_2_r5 = new ModelMapper(modelDataWrapper);
		roof_2_r5.setPos(-10.1709F, -40.8501F, 0);
		window_exterior_5.addChild(roof_2_r5);
		setRotationAngle(roof_2_r5, 0, 0, 1.0472F);
		roof_2_r5.texOffs(189, 351).addBox(0, 3, -15, 1, 3, 30, 0, false);

		roof_1_r6 = new ModelMapper(modelDataWrapper);
		roof_1_r6.setPos(-17.9382F, -34.7859F, 0);
		window_exterior_5.addChild(roof_1_r6);
		setRotationAngle(roof_1_r6, 0, 0, 0.6981F);
		roof_1_r6.texOffs(297, 256).addBox(0, -4, -15, 0, 4, 30, 0, false);

		window_bottom_r6 = new ModelMapper(modelDataWrapper);
		window_bottom_r6.setPos(-21, -7, 0);
		window_exterior_5.addChild(window_bottom_r6);
		setRotationAngle(window_bottom_r6, 0, 0, -0.2094F);
		window_bottom_r6.texOffs(298, 147).addBox(0, 0, -15, 1, 8, 30, 0, false);

		window_top_r6 = new ModelMapper(modelDataWrapper);
		window_top_r6.setPos(-21, -13, 0);
		window_exterior_5.addChild(window_top_r6);
		setRotationAngle(window_top_r6, 0, 0, 0.1396F);
		window_top_r6.texOffs(160, 142).addBox(0, -22, -15, 0, 22, 30, 0, false);

		window_exterior_6 = new ModelMapper(modelDataWrapper);
		window_exterior_6.setPos(0, 24, 0);
		window_exterior_6.texOffs(160, 186).addBox(-21, -13, -15, 0, 6, 30, 0, false);

		roof_2_r6 = new ModelMapper(modelDataWrapper);
		roof_2_r6.setPos(-10.1709F, -40.8501F, 0);
		window_exterior_6.addChild(roof_2_r6);
		setRotationAngle(roof_2_r6, 0, 0, 1.0472F);
		roof_2_r6.texOffs(0, 350).addBox(0, 3, -15, 1, 3, 30, 0, false);

		roof_1_r7 = new ModelMapper(modelDataWrapper);
		roof_1_r7.setPos(-17.9382F, -34.7859F, 0);
		window_exterior_6.addChild(roof_1_r7);
		setRotationAngle(roof_1_r7, 0, 0, 0.6981F);
		roof_1_r7.texOffs(158, 290).addBox(0, -4, -15, 0, 4, 30, 0, false);

		window_bottom_r7 = new ModelMapper(modelDataWrapper);
		window_bottom_r7.setPos(-21, -7, 0);
		window_exterior_6.addChild(window_bottom_r7);
		setRotationAngle(window_bottom_r7, 0, 0, -0.2094F);
		window_bottom_r7.texOffs(298, 109).addBox(0, 0, -15, 1, 8, 30, 0, false);

		window_top_r7 = new ModelMapper(modelDataWrapper);
		window_top_r7.setPos(-21, -13, 0);
		window_exterior_6.addChild(window_top_r7);
		setRotationAngle(window_top_r7, 0, 0, 0.1396F);
		window_top_r7.texOffs(158, 100).addBox(0, -22, -15, 0, 22, 30, 0, false);

		window_exterior_7 = new ModelMapper(modelDataWrapper);
		window_exterior_7.setPos(0, 24, 0);
		window_exterior_7.texOffs(98, 186).addBox(-21, -13, -15, 0, 6, 30, 0, false);

		roof_2_r7 = new ModelMapper(modelDataWrapper);
		roof_2_r7.setPos(-10.1709F, -40.8501F, 0);
		window_exterior_7.addChild(roof_2_r7);
		setRotationAngle(roof_2_r7, 0, 0, 1.0472F);
		roof_2_r7.texOffs(92, 348).addBox(0, 3, -15, 1, 3, 30, 0, false);

		roof_1_r8 = new ModelMapper(modelDataWrapper);
		roof_1_r8.setPos(-17.9382F, -34.7859F, 0);
		window_exterior_7.addChild(roof_1_r8);
		setRotationAngle(roof_1_r8, 0, 0, 0.6981F);
		roof_1_r8.texOffs(158, 286).addBox(0, -4, -15, 0, 4, 30, 0, false);

		window_bottom_r8 = new ModelMapper(modelDataWrapper);
		window_bottom_r8.setPos(-21, -7, 0);
		window_exterior_7.addChild(window_bottom_r8);
		setRotationAngle(window_bottom_r8, 0, 0, -0.2094F);
		window_bottom_r8.texOffs(297, 291).addBox(0, 0, -15, 1, 8, 30, 0, false);

		window_top_r8 = new ModelMapper(modelDataWrapper);
		window_top_r8.setPos(-21, -13, 0);
		window_exterior_7.addChild(window_top_r8);
		setRotationAngle(window_top_r8, 0, 0, 0.1396F);
		window_top_r8.texOffs(158, 78).addBox(0, -22, -15, 0, 22, 30, 0, false);

		window_exterior_8 = new ModelMapper(modelDataWrapper);
		window_exterior_8.setPos(0, 24, 0);
		window_exterior_8.texOffs(0, 186).addBox(-21, -13, -15, 0, 6, 30, 0, false);

		roof_2_r8 = new ModelMapper(modelDataWrapper);
		roof_2_r8.setPos(-10.1709F, -40.8501F, 0);
		window_exterior_8.addChild(roof_2_r8);
		setRotationAngle(roof_2_r8, 0, 0, 1.0472F);
		roof_2_r8.texOffs(345, 33).addBox(0, 3, -15, 1, 3, 30, 0, false);

		roof_1_r9 = new ModelMapper(modelDataWrapper);
		roof_1_r9.setPos(-17.9382F, -34.7859F, 0);
		window_exterior_8.addChild(roof_1_r9);
		setRotationAngle(roof_1_r9, 0, 0, 0.6981F);
		roof_1_r9.texOffs(281, 70).addBox(0, -4, -15, 0, 4, 30, 0, false);

		window_bottom_r9 = new ModelMapper(modelDataWrapper);
		window_bottom_r9.setPos(-21, -7, 0);
		window_exterior_8.addChild(window_bottom_r9);
		setRotationAngle(window_bottom_r9, 0, 0, -0.2094F);
		window_bottom_r9.texOffs(296, 245).addBox(0, 0, -15, 1, 8, 30, 0, false);

		window_top_r9 = new ModelMapper(modelDataWrapper);
		window_top_r9.setPos(-21, -13, 0);
		window_exterior_8.addChild(window_top_r9);
		setRotationAngle(window_top_r9, 0, 0, 0.1396F);
		window_top_r9.texOffs(158, 56).addBox(0, -22, -15, 0, 22, 30, 0, false);

		window_exterior_9 = new ModelMapper(modelDataWrapper);
		window_exterior_9.setPos(0, 24, 0);
		window_exterior_9.texOffs(0, 180).addBox(-21, -13, -15, 0, 6, 30, 0, false);

		roof_2_r9 = new ModelMapper(modelDataWrapper);
		roof_2_r9.setPos(-10.1709F, -40.8501F, 0);
		window_exterior_9.addChild(roof_2_r9);
		setRotationAngle(roof_2_r9, 0, 0, 1.0472F);
		roof_2_r9.texOffs(330, 125).addBox(0, 3, -15, 1, 3, 30, 0, false);

		roof_1_r10 = new ModelMapper(modelDataWrapper);
		roof_1_r10.setPos(-17.9382F, -34.7859F, 0);
		window_exterior_9.addChild(roof_1_r10);
		setRotationAngle(roof_1_r10, 0, 0, 0.6981F);
		roof_1_r10.texOffs(281, 66).addBox(0, -4, -15, 0, 4, 30, 0, false);

		window_bottom_r10 = new ModelMapper(modelDataWrapper);
		window_bottom_r10.setPos(-21, -7, 0);
		window_exterior_9.addChild(window_bottom_r10);
		setRotationAngle(window_bottom_r10, 0, 0, -0.2094F);
		window_bottom_r10.texOffs(296, 205).addBox(0, 0, -15, 1, 8, 30, 0, false);

		window_top_r10 = new ModelMapper(modelDataWrapper);
		window_top_r10.setPos(-21, -13, 0);
		window_exterior_9.addChild(window_top_r10);
		setRotationAngle(window_top_r10, 0, 0, 0.1396F);
		window_top_r10.texOffs(0, 158).addBox(0, -22, -15, 0, 22, 30, 0, false);

		window_end_exterior_1 = new ModelMapper(modelDataWrapper);
		window_end_exterior_1.setPos(0, 24, 0);


		window_end_exterior_1_1 = new ModelMapper(modelDataWrapper);
		window_end_exterior_1_1.setPos(-10.1709F, -40.8501F, 0);
		window_end_exterior_1.addChild(window_end_exterior_1_1);
		window_end_exterior_1_1.texOffs(161, 46).addBox(-10.8291F, 27.8501F, -15, 0, 6, 30, 0, false);

		roof_2_r10 = new ModelMapper(modelDataWrapper);
		roof_2_r10.setPos(0, 0, 0);
		window_end_exterior_1_1.addChild(roof_2_r10);
		setRotationAngle(roof_2_r10, 0, 0, 1.0472F);
		roof_2_r10.texOffs(330, 92).addBox(0, 3, -15, 1, 3, 30, 0, false);

		roof_1_r11 = new ModelMapper(modelDataWrapper);
		roof_1_r11.setPos(-7.7673F, 6.0642F, 0);
		window_end_exterior_1_1.addChild(roof_1_r11);
		setRotationAngle(roof_1_r11, 0, 0, 0.6981F);
		roof_1_r11.texOffs(281, 62).addBox(0, -4, -15, 0, 4, 30, 0, false);

		window_bottom_r11 = new ModelMapper(modelDataWrapper);
		window_bottom_r11.setPos(-10.8291F, 33.8501F, 0);
		window_end_exterior_1_1.addChild(window_bottom_r11);
		setRotationAngle(window_bottom_r11, 0, 0, -0.2094F);
		window_bottom_r11.texOffs(287, 6).addBox(0, 0, -15, 1, 8, 30, 0, false);

		window_top_r11 = new ModelMapper(modelDataWrapper);
		window_top_r11.setPos(-10.8291F, 27.8501F, 0);
		window_end_exterior_1_1.addChild(window_top_r11);
		setRotationAngle(window_top_r11, 0, 0, 0.1396F);
		window_top_r11.texOffs(98, 142).addBox(0, -22, -15, 0, 22, 30, 0, false);

		window_end_exterior_1_2 = new ModelMapper(modelDataWrapper);
		window_end_exterior_1_2.setPos(10.1709F, -40.8501F, 0);
		window_end_exterior_1.addChild(window_end_exterior_1_2);
		window_end_exterior_1_2.texOffs(161, 46).addBox(10.8291F, 27.8501F, -15, 0, 6, 30, 0, true);

		roof_3_r2 = new ModelMapper(modelDataWrapper);
		roof_3_r2.setPos(0, 0, 0);
		window_end_exterior_1_2.addChild(roof_3_r2);
		setRotationAngle(roof_3_r2, 0, 0, -1.0472F);
		roof_3_r2.texOffs(330, 92).addBox(-1, 3, -15, 1, 3, 30, 0, true);

		roof_2_r11 = new ModelMapper(modelDataWrapper);
		roof_2_r11.setPos(7.7673F, 6.0642F, 0);
		window_end_exterior_1_2.addChild(roof_2_r11);
		setRotationAngle(roof_2_r11, 0, 0, -0.6981F);
		roof_2_r11.texOffs(281, 62).addBox(0, -4, -15, 0, 4, 30, 0, true);

		window_bottom_r12 = new ModelMapper(modelDataWrapper);
		window_bottom_r12.setPos(10.8291F, 33.8501F, 0);
		window_end_exterior_1_2.addChild(window_bottom_r12);
		setRotationAngle(window_bottom_r12, 0, 0, 0.2094F);
		window_bottom_r12.texOffs(287, 6).addBox(-1, 0, -15, 1, 8, 30, 0, true);

		window_top_r12 = new ModelMapper(modelDataWrapper);
		window_top_r12.setPos(10.8291F, 27.8501F, 0);
		window_end_exterior_1_2.addChild(window_top_r12);
		setRotationAngle(window_top_r12, 0, 0, -0.1396F);
		window_top_r12.texOffs(98, 142).addBox(0, -22, -15, 0, 22, 30, 0, true);

		window_end_exterior_2 = new ModelMapper(modelDataWrapper);
		window_end_exterior_2.setPos(0, 24, 0);


		window_end_exterior_2_1 = new ModelMapper(modelDataWrapper);
		window_end_exterior_2_1.setPos(-10.1709F, -40.8501F, 0);
		window_end_exterior_2.addChild(window_end_exterior_2_1);
		window_end_exterior_2_1.texOffs(161, 34).addBox(-10.8291F, 27.8501F, -15, 0, 6, 30, 0, false);

		roof_3_r3 = new ModelMapper(modelDataWrapper);
		roof_3_r3.setPos(0, 0, 0);
		window_end_exterior_2_1.addChild(roof_3_r3);
		setRotationAngle(roof_3_r3, 0, 0, 1.0472F);
		roof_3_r3.texOffs(329, 319).addBox(0, 3, -15, 1, 3, 30, 0, false);

		roof_2_r12 = new ModelMapper(modelDataWrapper);
		roof_2_r12.setPos(-7.7673F, 6.0642F, 0);
		window_end_exterior_2_1.addChild(roof_2_r12);
		setRotationAngle(roof_2_r12, 0, 0, 0.6981F);
		roof_2_r12.texOffs(278, 167).addBox(0, -4, -15, 0, 4, 30, 0, false);

		window_bottom_r13 = new ModelMapper(modelDataWrapper);
		window_bottom_r13.setPos(-10.8291F, 33.8501F, 0);
		window_end_exterior_2_1.addChild(window_bottom_r13);
		setRotationAngle(window_bottom_r13, 0, 0, -0.2094F);
		window_bottom_r13.texOffs(265, 283).addBox(0, 0, -15, 1, 8, 30, 0, false);

		window_top_r13 = new ModelMapper(modelDataWrapper);
		window_top_r13.setPos(-10.8291F, 27.8501F, 0);
		window_end_exterior_2_1.addChild(window_top_r13);
		setRotationAngle(window_top_r13, 0, 0, 0.1396F);
		window_top_r13.texOffs(0, 136).addBox(0, -22, -15, 0, 22, 30, 0, false);

		window_end_exterior_2_2 = new ModelMapper(modelDataWrapper);
		window_end_exterior_2_2.setPos(10.1709F, -40.8501F, 0);
		window_end_exterior_2.addChild(window_end_exterior_2_2);
		window_end_exterior_2_2.texOffs(161, 34).addBox(10.8291F, 27.8501F, -15, 0, 6, 30, 0, true);

		roof_4_r1 = new ModelMapper(modelDataWrapper);
		roof_4_r1.setPos(0, 0, 0);
		window_end_exterior_2_2.addChild(roof_4_r1);
		setRotationAngle(roof_4_r1, 0, 0, -1.0472F);
		roof_4_r1.texOffs(329, 319).addBox(-1, 3, -15, 1, 3, 30, 0, true);

		roof_3_r4 = new ModelMapper(modelDataWrapper);
		roof_3_r4.setPos(7.7673F, 6.0642F, 0);
		window_end_exterior_2_2.addChild(roof_3_r4);
		setRotationAngle(roof_3_r4, 0, 0, -0.6981F);
		roof_3_r4.texOffs(278, 167).addBox(0, -4, -15, 0, 4, 30, 0, true);

		window_bottom_r14 = new ModelMapper(modelDataWrapper);
		window_bottom_r14.setPos(10.8291F, 33.8501F, 0);
		window_end_exterior_2_2.addChild(window_bottom_r14);
		setRotationAngle(window_bottom_r14, 0, 0, 0.2094F);
		window_bottom_r14.texOffs(265, 283).addBox(-1, 0, -15, 1, 8, 30, 0, true);

		window_top_r14 = new ModelMapper(modelDataWrapper);
		window_top_r14.setPos(10.8291F, 27.8501F, 0);
		window_end_exterior_2_2.addChild(window_top_r14);
		setRotationAngle(window_top_r14, 0, 0, -0.1396F);
		window_top_r14.texOffs(0, 136).addBox(0, -22, -15, 0, 22, 30, 0, true);

		window_end_exterior_3 = new ModelMapper(modelDataWrapper);
		window_end_exterior_3.setPos(0, 24, 0);


		window_end_exterior_3_1 = new ModelMapper(modelDataWrapper);
		window_end_exterior_3_1.setPos(-10.1709F, -40.8501F, 0);
		window_end_exterior_3.addChild(window_end_exterior_3_1);
		window_end_exterior_3_1.texOffs(161, 28).addBox(-10.8291F, 27.8501F, -15, 0, 6, 30, 0, false);

		roof_4_r2 = new ModelMapper(modelDataWrapper);
		roof_4_r2.setPos(0, 0, 0);
		window_end_exterior_3_1.addChild(roof_4_r2);
		setRotationAngle(roof_4_r2, 0, 0, 1.0472F);
		roof_4_r2.texOffs(329, 286).addBox(0, 3, -15, 1, 3, 30, 0, false);

		roof_3_r5 = new ModelMapper(modelDataWrapper);
		roof_3_r5.setPos(-7.7673F, 6.0642F, 0);
		window_end_exterior_3_1.addChild(roof_3_r5);
		setRotationAngle(roof_3_r5, 0, 0, 0.6981F);
		roof_3_r5.texOffs(278, 163).addBox(0, -4, -15, 0, 4, 30, 0, false);

		window_bottom_r15 = new ModelMapper(modelDataWrapper);
		window_bottom_r15.setPos(-10.8291F, 33.8501F, 0);
		window_end_exterior_3_1.addChild(window_bottom_r15);
		setRotationAngle(window_bottom_r15, 0, 0, -0.2094F);
		window_bottom_r15.texOffs(62, 281).addBox(0, 0, -15, 1, 8, 30, 0, false);

		window_top_r15 = new ModelMapper(modelDataWrapper);
		window_top_r15.setPos(-10.8291F, 27.8501F, 0);
		window_end_exterior_3_1.addChild(window_top_r15);
		setRotationAngle(window_top_r15, 0, 0, 0.1396F);
		window_top_r15.texOffs(74, 100).addBox(0, -22, -15, 0, 22, 30, 0, false);

		window_end_exterior_3_2 = new ModelMapper(modelDataWrapper);
		window_end_exterior_3_2.setPos(10.1709F, -40.8501F, 0);
		window_end_exterior_3.addChild(window_end_exterior_3_2);
		window_end_exterior_3_2.texOffs(161, 28).addBox(10.8291F, 27.8501F, -15, 0, 6, 30, 0, true);

		roof_5_r1 = new ModelMapper(modelDataWrapper);
		roof_5_r1.setPos(0, 0, 0);
		window_end_exterior_3_2.addChild(roof_5_r1);
		setRotationAngle(roof_5_r1, 0, 0, -1.0472F);
		roof_5_r1.texOffs(329, 286).addBox(-1, 3, -15, 1, 3, 30, 0, true);

		roof_4_r3 = new ModelMapper(modelDataWrapper);
		roof_4_r3.setPos(7.7673F, 6.0642F, 0);
		window_end_exterior_3_2.addChild(roof_4_r3);
		setRotationAngle(roof_4_r3, 0, 0, -0.6981F);
		roof_4_r3.texOffs(278, 163).addBox(0, -4, -15, 0, 4, 30, 0, true);

		window_bottom_r16 = new ModelMapper(modelDataWrapper);
		window_bottom_r16.setPos(10.8291F, 33.8501F, 0);
		window_end_exterior_3_2.addChild(window_bottom_r16);
		setRotationAngle(window_bottom_r16, 0, 0, 0.2094F);
		window_bottom_r16.texOffs(62, 281).addBox(-1, 0, -15, 1, 8, 30, 0, true);

		window_top_r16 = new ModelMapper(modelDataWrapper);
		window_top_r16.setPos(10.8291F, 27.8501F, 0);
		window_end_exterior_3_2.addChild(window_top_r16);
		setRotationAngle(window_top_r16, 0, 0, -0.1396F);
		window_top_r16.texOffs(74, 100).addBox(0, -22, -15, 0, 22, 30, 0, true);

		window_end_exterior_4 = new ModelMapper(modelDataWrapper);
		window_end_exterior_4.setPos(0, 24, 0);


		window_end_exterior_4_1 = new ModelMapper(modelDataWrapper);
		window_end_exterior_4_1.setPos(-10.1709F, -40.8501F, 0);
		window_end_exterior_4.addChild(window_end_exterior_4_1);
		window_end_exterior_4_1.texOffs(161, 22).addBox(-10.8291F, 27.8501F, -15, 0, 6, 30, 0, false);

		roof_4_r4 = new ModelMapper(modelDataWrapper);
		roof_4_r4.setPos(0, 0, 0);
		window_end_exterior_4_1.addChild(roof_4_r4);
		setRotationAngle(roof_4_r4, 0, 0, 1.0472F);
		roof_4_r4.texOffs(267, 329).addBox(0, 3, -15, 1, 3, 30, 0, false);

		roof_3_r6 = new ModelMapper(modelDataWrapper);
		roof_3_r6.setPos(-7.7673F, 6.0642F, 0);
		window_end_exterior_4_1.addChild(roof_3_r6);
		setRotationAngle(roof_3_r6, 0, 0, 0.6981F);
		roof_3_r6.texOffs(278, 159).addBox(0, -4, -15, 0, 4, 30, 0, false);

		window_bottom_r17 = new ModelMapper(modelDataWrapper);
		window_bottom_r17.setPos(-10.8291F, 33.8501F, 0);
		window_end_exterior_4_1.addChild(window_bottom_r17);
		setRotationAngle(window_bottom_r17, 0, 0, -0.2094F);
		window_bottom_r17.texOffs(281, 46).addBox(0, 0, -15, 1, 8, 30, 0, false);

		window_top_r17 = new ModelMapper(modelDataWrapper);
		window_top_r17.setPos(-10.8291F, 27.8501F, 0);
		window_end_exterior_4_1.addChild(window_top_r17);
		setRotationAngle(window_top_r17, 0, 0, 0.1396F);
		window_top_r17.texOffs(0, 86).addBox(0, -22, -15, 0, 22, 30, 0, false);

		window_end_exterior_4_2 = new ModelMapper(modelDataWrapper);
		window_end_exterior_4_2.setPos(10.1709F, -40.8501F, 0);
		window_end_exterior_4.addChild(window_end_exterior_4_2);
		window_end_exterior_4_2.texOffs(161, 22).addBox(10.8291F, 27.8501F, -15, 0, 6, 30, 0, true);

		roof_5_r2 = new ModelMapper(modelDataWrapper);
		roof_5_r2.setPos(0, 0, 0);
		window_end_exterior_4_2.addChild(roof_5_r2);
		setRotationAngle(roof_5_r2, 0, 0, -1.0472F);
		roof_5_r2.texOffs(267, 329).addBox(-1, 3, -15, 1, 3, 30, 0, true);

		roof_4_r5 = new ModelMapper(modelDataWrapper);
		roof_4_r5.setPos(7.7673F, 6.0642F, 0);
		window_end_exterior_4_2.addChild(roof_4_r5);
		setRotationAngle(roof_4_r5, 0, 0, -0.6981F);
		roof_4_r5.texOffs(278, 159).addBox(0, -4, -15, 0, 4, 30, 0, true);

		window_bottom_r18 = new ModelMapper(modelDataWrapper);
		window_bottom_r18.setPos(10.8291F, 33.8501F, 0);
		window_end_exterior_4_2.addChild(window_bottom_r18);
		setRotationAngle(window_bottom_r18, 0, 0, 0.2094F);
		window_bottom_r18.texOffs(281, 46).addBox(-1, 0, -15, 1, 8, 30, 0, true);

		window_top_r18 = new ModelMapper(modelDataWrapper);
		window_top_r18.setPos(10.8291F, 27.8501F, 0);
		window_end_exterior_4_2.addChild(window_top_r18);
		setRotationAngle(window_top_r18, 0, 0, -0.1396F);
		window_top_r18.texOffs(0, 86).addBox(0, -22, -15, 0, 22, 30, 0, true);

		window_end_exterior_5 = new ModelMapper(modelDataWrapper);
		window_end_exterior_5.setPos(0, 24, 0);


		window_end_exterior_5_1 = new ModelMapper(modelDataWrapper);
		window_end_exterior_5_1.setPos(-10.1709F, -40.8501F, 0);
		window_end_exterior_5.addChild(window_end_exterior_5_1);
		window_end_exterior_5_1.texOffs(74, 122).addBox(-10.8291F, 27.8501F, -15, 0, 6, 30, 0, false);

		roof_5_r3 = new ModelMapper(modelDataWrapper);
		roof_5_r3.setPos(0, 0, 0);
		window_end_exterior_5_1.addChild(roof_5_r3);
		setRotationAngle(roof_5_r3, 0, 0, 1.0472F);
		roof_5_r3.texOffs(328, 253).addBox(0, 3, -15, 1, 3, 30, 0, false);

		roof_4_r6 = new ModelMapper(modelDataWrapper);
		roof_4_r6.setPos(-7.7673F, 6.0642F, 0);
		window_end_exterior_5_1.addChild(roof_4_r6);
		setRotationAngle(roof_4_r6, 0, 0, 0.6981F);
		roof_4_r6.texOffs(278, 155).addBox(0, -4, -15, 0, 4, 30, 0, false);

		window_bottom_r19 = new ModelMapper(modelDataWrapper);
		window_bottom_r19.setPos(-10.8291F, 33.8501F, 0);
		window_end_exterior_5_1.addChild(window_bottom_r19);
		setRotationAngle(window_bottom_r19, 0, 0, -0.2094F);
		window_bottom_r19.texOffs(233, 275).addBox(0, 0, -15, 1, 8, 30, 0, false);

		window_top_r19 = new ModelMapper(modelDataWrapper);
		window_top_r19.setPos(-10.8291F, 27.8501F, 0);
		window_end_exterior_5_1.addChild(window_top_r19);
		setRotationAngle(window_top_r19, 0, 0, 0.1396F);
		window_top_r19.texOffs(74, 78).addBox(0, -22, -15, 0, 22, 30, 0, false);

		window_end_exterior_5_2 = new ModelMapper(modelDataWrapper);
		window_end_exterior_5_2.setPos(10.1709F, -40.8501F, 0);
		window_end_exterior_5.addChild(window_end_exterior_5_2);
		window_end_exterior_5_2.texOffs(74, 122).addBox(10.8291F, 27.8501F, -15, 0, 6, 30, 0, true);

		roof_6_r1 = new ModelMapper(modelDataWrapper);
		roof_6_r1.setPos(0, 0, 0);
		window_end_exterior_5_2.addChild(roof_6_r1);
		setRotationAngle(roof_6_r1, 0, 0, -1.0472F);
		roof_6_r1.texOffs(328, 253).addBox(-1, 3, -15, 1, 3, 30, 0, true);

		roof_5_r4 = new ModelMapper(modelDataWrapper);
		roof_5_r4.setPos(7.7673F, 6.0642F, 0);
		window_end_exterior_5_2.addChild(roof_5_r4);
		setRotationAngle(roof_5_r4, 0, 0, -0.6981F);
		roof_5_r4.texOffs(278, 155).addBox(0, -4, -15, 0, 4, 30, 0, true);

		window_bottom_r20 = new ModelMapper(modelDataWrapper);
		window_bottom_r20.setPos(10.8291F, 33.8501F, 0);
		window_end_exterior_5_2.addChild(window_bottom_r20);
		setRotationAngle(window_bottom_r20, 0, 0, 0.2094F);
		window_bottom_r20.texOffs(233, 275).addBox(-1, 0, -15, 1, 8, 30, 0, true);

		window_top_r20 = new ModelMapper(modelDataWrapper);
		window_top_r20.setPos(10.8291F, 27.8501F, 0);
		window_end_exterior_5_2.addChild(window_top_r20);
		setRotationAngle(window_top_r20, 0, 0, -0.1396F);
		window_top_r20.texOffs(74, 78).addBox(0, -22, -15, 0, 22, 30, 0, true);

		window_end_exterior_6 = new ModelMapper(modelDataWrapper);
		window_end_exterior_6.setPos(0, 24, 0);


		window_end_exterior_6_1 = new ModelMapper(modelDataWrapper);
		window_end_exterior_6_1.setPos(-10.1709F, -40.8501F, 0);
		window_end_exterior_6.addChild(window_end_exterior_6_1);
		window_end_exterior_6_1.texOffs(0, 49).addBox(-10.8291F, 27.8501F, -15, 0, 6, 30, 0, false);

		roof_6_r2 = new ModelMapper(modelDataWrapper);
		roof_6_r2.setPos(0, 0, 0);
		window_end_exterior_6_1.addChild(roof_6_r2);
		setRotationAngle(roof_6_r2, 0, 0, 1.0472F);
		roof_6_r2.texOffs(328, 218).addBox(0, 3, -15, 1, 3, 30, 0, false);

		roof_5_r5 = new ModelMapper(modelDataWrapper);
		roof_5_r5.setPos(-7.7673F, 6.0642F, 0);
		window_end_exterior_6_1.addChild(roof_5_r5);
		setRotationAngle(roof_5_r5, 0, 0, 0.6981F);
		roof_5_r5.texOffs(32, 269).addBox(0, -4, -15, 0, 4, 30, 0, false);

		window_bottom_r21 = new ModelMapper(modelDataWrapper);
		window_bottom_r21.setPos(-10.8291F, 33.8501F, 0);
		window_end_exterior_6_1.addChild(window_bottom_r21);
		setRotationAngle(window_bottom_r21, 0, 0, -0.2094F);
		window_bottom_r21.texOffs(0, 274).addBox(0, 0, -15, 1, 8, 30, 0, false);

		window_top_r21 = new ModelMapper(modelDataWrapper);
		window_top_r21.setPos(-10.8291F, 27.8501F, 0);
		window_end_exterior_6_1.addChild(window_top_r21);
		setRotationAngle(window_top_r21, 0, 0, 0.1396F);
		window_top_r21.texOffs(74, 56).addBox(0, -22, -15, 0, 22, 30, 0, false);

		window_end_exterior_6_2 = new ModelMapper(modelDataWrapper);
		window_end_exterior_6_2.setPos(10.1709F, -40.8501F, 0);
		window_end_exterior_6.addChild(window_end_exterior_6_2);
		window_end_exterior_6_2.texOffs(0, 49).addBox(10.8291F, 27.8501F, -15, 0, 6, 30, 0, true);

		roof_7_r1 = new ModelMapper(modelDataWrapper);
		roof_7_r1.setPos(0, 0, 0);
		window_end_exterior_6_2.addChild(roof_7_r1);
		setRotationAngle(roof_7_r1, 0, 0, -1.0472F);
		roof_7_r1.texOffs(328, 218).addBox(-1, 3, -15, 1, 3, 30, 0, true);

		roof_6_r3 = new ModelMapper(modelDataWrapper);
		roof_6_r3.setPos(7.7673F, 6.0642F, 0);
		window_end_exterior_6_2.addChild(roof_6_r3);
		setRotationAngle(roof_6_r3, 0, 0, -0.6981F);
		roof_6_r3.texOffs(32, 269).addBox(0, -4, -15, 0, 4, 30, 0, true);

		window_bottom_r22 = new ModelMapper(modelDataWrapper);
		window_bottom_r22.setPos(10.8291F, 33.8501F, 0);
		window_end_exterior_6_2.addChild(window_bottom_r22);
		setRotationAngle(window_bottom_r22, 0, 0, 0.2094F);
		window_bottom_r22.texOffs(0, 274).addBox(-1, 0, -15, 1, 8, 30, 0, true);

		window_top_r22 = new ModelMapper(modelDataWrapper);
		window_top_r22.setPos(10.8291F, 27.8501F, 0);
		window_end_exterior_6_2.addChild(window_top_r22);
		setRotationAngle(window_top_r22, 0, 0, -0.1396F);
		window_top_r22.texOffs(74, 56).addBox(0, -22, -15, 0, 22, 30, 0, true);

		door = new ModelMapper(modelDataWrapper);
		door.setPos(0, 24, 0);


		door_1 = new ModelMapper(modelDataWrapper);
		door_1.setPos(0, 0, 0);
		door.addChild(door_1);
		door_1.texOffs(230, 86).addBox(-20, -13, 3, 1, 6, 13, 0, false);

		window_bottom_2_r1 = new ModelMapper(modelDataWrapper);
		window_bottom_2_r1.setPos(-20, -7, 0);
		door_1.addChild(window_bottom_2_r1);
		setRotationAngle(window_bottom_2_r1, 0, 0, -0.2094F);
		window_bottom_2_r1.texOffs(199, 0).addBox(0, 0, 3, 1, 8, 13, 0, false);

		window_top_2_r1 = new ModelMapper(modelDataWrapper);
		window_top_2_r1.setPos(-20, -13, 0);
		door_1.addChild(window_top_2_r1);
		setRotationAngle(window_top_2_r1, 0, 0, 0.1396F);
		window_top_2_r1.texOffs(390, 235).addBox(0, -21, 3, 1, 21, 13, 0, false);

		door_2 = new ModelMapper(modelDataWrapper);
		door_2.setPos(0, 0, 0);
		door.addChild(door_2);
		door_2.texOffs(230, 86).addBox(19, -13, 3, 1, 6, 13, 0, true);

		window_bottom_3_r1 = new ModelMapper(modelDataWrapper);
		window_bottom_3_r1.setPos(20, -7, 0);
		door_2.addChild(window_bottom_3_r1);
		setRotationAngle(window_bottom_3_r1, 0, 0, 0.2094F);
		window_bottom_3_r1.texOffs(199, 0).addBox(-1, 0, 3, 1, 8, 13, 0, true);

		window_top_3_r1 = new ModelMapper(modelDataWrapper);
		window_top_3_r1.setPos(20, -13, 0);
		door_2.addChild(window_top_3_r1);
		setRotationAngle(window_top_3_r1, 0, 0, -0.1396F);
		window_top_3_r1.texOffs(390, 235).addBox(-1, -21, 3, 1, 21, 13, 0, true);

		door_exterior = new ModelMapper(modelDataWrapper);
		door_exterior.setPos(0, 24, 0);


		door_exterior_1 = new ModelMapper(modelDataWrapper);
		door_exterior_1.setPos(0, 0, 0);
		door_exterior.addChild(door_exterior_1);
		door_exterior_1.texOffs(221, 63).addBox(-20, -13, 3, 0, 6, 13, 0, false);

		window_bottom_1_r1 = new ModelMapper(modelDataWrapper);
		window_bottom_1_r1.setPos(-20, -7, 0);
		door_exterior_1.addChild(window_bottom_1_r1);
		setRotationAngle(window_bottom_1_r1, 0, 0, -0.2094F);
		window_bottom_1_r1.texOffs(199, 8).addBox(0, 0, 3, 0, 8, 13, 0, false);

		window_top_1_r1 = new ModelMapper(modelDataWrapper);
		window_top_1_r1.setPos(-20, -13, 0);
		door_exterior_1.addChild(window_top_1_r1);
		setRotationAngle(window_top_1_r1, 0, 0, 0.1396F);
		window_top_1_r1.texOffs(227, 159).addBox(0, -21, 3, 0, 21, 13, 0, false);

		door_exterior_2 = new ModelMapper(modelDataWrapper);
		door_exterior_2.setPos(0, 0, 0);
		door_exterior.addChild(door_exterior_2);
		door_exterior_2.texOffs(221, 63).addBox(20, -13, 3, 0, 6, 13, 0, true);

		window_bottom_2_r2 = new ModelMapper(modelDataWrapper);
		window_bottom_2_r2.setPos(20, -7, 0);
		door_exterior_2.addChild(window_bottom_2_r2);
		setRotationAngle(window_bottom_2_r2, 0, 0, 0.2094F);
		window_bottom_2_r2.texOffs(199, 8).addBox(0, 0, 3, 0, 8, 13, 0, true);

		window_top_2_r2 = new ModelMapper(modelDataWrapper);
		window_top_2_r2.setPos(20, -13, 0);
		door_exterior_2.addChild(window_top_2_r2);
		setRotationAngle(window_top_2_r2, 0, 0, -0.1396F);
		window_top_2_r2.texOffs(227, 159).addBox(0, -21, 3, 0, 21, 13, 0, true);

		door_exterior_end = new ModelMapper(modelDataWrapper);
		door_exterior_end.setPos(0, 24, 0);


		door_exterior_end_1 = new ModelMapper(modelDataWrapper);
		door_exterior_end_1.setPos(0, 0, 0);
		door_exterior_end.addChild(door_exterior_end_1);
		door_exterior_end_1.texOffs(0, 11).addBox(-20, -13, 3, 0, 6, 13, 0, false);

		window_bottom_2_r3 = new ModelMapper(modelDataWrapper);
		window_bottom_2_r3.setPos(-20, -7, 0);
		door_exterior_end_1.addChild(window_bottom_2_r3);
		setRotationAngle(window_bottom_2_r3, 0, 0, -0.2094F);
		window_bottom_2_r3.texOffs(0, 94).addBox(0, 0, 3, 0, 8, 13, 0, false);

		window_top_2_r3 = new ModelMapper(modelDataWrapper);
		window_top_2_r3.setPos(-20, -13, 0);
		door_exterior_end_1.addChild(window_top_2_r3);
		setRotationAngle(window_top_2_r3, 0, 0, 0.1396F);
		window_top_2_r3.texOffs(0, 73).addBox(0, -21, 3, 0, 21, 13, 0, false);

		door_exterior_end_2 = new ModelMapper(modelDataWrapper);
		door_exterior_end_2.setPos(0, 0, 0);
		door_exterior_end.addChild(door_exterior_end_2);
		door_exterior_end_2.texOffs(0, 11).addBox(20, -13, 3, 0, 6, 13, 0, true);

		window_bottom_3_r2 = new ModelMapper(modelDataWrapper);
		window_bottom_3_r2.setPos(20, -7, 0);
		door_exterior_end_2.addChild(window_bottom_3_r2);
		setRotationAngle(window_bottom_3_r2, 0, 0, 0.2094F);
		window_bottom_3_r2.texOffs(0, 94).addBox(0, 0, 3, 0, 8, 13, 0, true);

		window_top_3_r2 = new ModelMapper(modelDataWrapper);
		window_top_3_r2.setPos(20, -13, 0);
		door_exterior_end_2.addChild(window_top_3_r2);
		setRotationAngle(window_top_3_r2, 0, 0, -0.1396F);
		window_top_3_r2.texOffs(0, 73).addBox(0, -21, 3, 0, 21, 13, 0, true);

		roof_exterior = new ModelMapper(modelDataWrapper);
		roof_exterior.setPos(0, 24, 0);
		roof_exterior.texOffs(0, 86).addBox(-13, -39, -15, 13, 0, 30, 0, false);
		roof_exterior.texOffs(360, 193).addBox(-19, 0, -15, 1, 2, 30, 0, false);

		end = new ModelMapper(modelDataWrapper);
		end.setPos(0, 24, 0);
		end.texOffs(230, 76).addBox(9.5F, -34, 16.1F, 11, 34, 29, 0, false);
		end.texOffs(310, 104).addBox(11, -34, 2.9F, 9, 34, 0, 0, false);
		end.texOffs(227, 172).addBox(-20.5F, -34, 16.1F, 11, 34, 29, 0, false);
		end.texOffs(307, 201).addBox(-20, -34, 2.9F, 9, 34, 0, 0, false);
		end.texOffs(237, 0).addBox(-20, -36, -8, 40, 36, 0, 0, false);
		end.texOffs(381, 0).addBox(-10, -34, 45, 20, 34, 0, 0, false);

		end_pillar_4_r1 = new ModelMapper(modelDataWrapper);
		end_pillar_4_r1.setPos(-11, 0, 2.9F);
		end.addChild(end_pillar_4_r1);
		setRotationAngle(end_pillar_4_r1, 0, 1.2217F, 0);
		end_pillar_4_r1.texOffs(94, 381).addBox(0, -34, 0, 12, 34, 0, 0, false);

		end_pillar_3_r1 = new ModelMapper(modelDataWrapper);
		end_pillar_3_r1.setPos(11, 0, 2.9F);
		end.addChild(end_pillar_3_r1);
		setRotationAngle(end_pillar_3_r1, 0, -1.2217F, 0);
		end_pillar_3_r1.texOffs(0, 383).addBox(-12, -34, 0, 12, 34, 0, 0, false);

		end_side_1 = new ModelMapper(modelDataWrapper);
		end_side_1.setPos(0, 0, 0);
		end.addChild(end_side_1);
		end_side_1.texOffs(0, 0).addBox(-13, -34, -8, 13, 0, 53, 0, false);
		end_side_1.texOffs(0, 170).addBox(-19, 0, -15, 19, 1, 60, 0, false);
		end_side_1.texOffs(189, 324).addBox(-17, -34, 3, 4, 1, 13, 0, false);
		end_side_1.texOffs(214, 0).addBox(-20, -13, -15, 0, 6, 7, 0, false);
		end_side_1.texOffs(9, 7).addBox(-15, -36, -15, 6, 0, 7, 0, false);
		end_side_1.texOffs(9, 0).addBox(-7, -35, -15, 7, 0, 7, 0, false);
		end_side_1.texOffs(227, 193).addBox(-18, -31, -15, 6, 1, 7, 0, false);

		roof_3_r7 = new ModelMapper(modelDataWrapper);
		roof_3_r7.setPos(-7, -35, 0);
		end_side_1.addChild(roof_3_r7);
		setRotationAngle(roof_3_r7, 0, 0, 0.3491F);
		roof_3_r7.texOffs(0, 0).addBox(-3, 0, -15, 3, 0, 7, 0, false);

		roof_1_r12 = new ModelMapper(modelDataWrapper);
		roof_1_r12.setPos(-15, -36, 0);
		end_side_1.addChild(roof_1_r12);
		setRotationAngle(roof_1_r12, 0, 0, -0.7854F);
		roof_1_r12.texOffs(0, 7).addBox(-3, 0, -15, 3, 0, 7, 0, false);

		window_bottom_r23 = new ModelMapper(modelDataWrapper);
		window_bottom_r23.setPos(-21, -7, 0);
		end_side_1.addChild(window_bottom_r23);
		setRotationAngle(window_bottom_r23, 0, 0, -0.2094F);
		window_bottom_r23.texOffs(56, 101).addBox(1, 0, -15, 0, 8, 7, 0, false);

		window_top_r23 = new ModelMapper(modelDataWrapper);
		window_top_r23.setPos(-21, -13, 0);
		end_side_1.addChild(window_top_r23);
		setRotationAngle(window_top_r23, 0, 0, 0.1396F);
		window_top_r23.texOffs(56, 79).addBox(1, -22, -15, 0, 22, 7, 0, false);
		window_top_r23.texOffs(237, 36).addBox(1, -22, 3, 2, 1, 13, 0, false);

		end_side_2 = new ModelMapper(modelDataWrapper);
		end_side_2.setPos(0, 0, 0);
		end.addChild(end_side_2);
		end_side_2.texOffs(0, 0).addBox(0, -34, -8, 13, 0, 53, 0, true);
		end_side_2.texOffs(0, 170).addBox(0, 0, -15, 19, 1, 60, 0, true);
		end_side_2.texOffs(189, 324).addBox(13, -34, 3, 4, 1, 13, 0, true);
		end_side_2.texOffs(214, 0).addBox(20, -13, -15, 0, 6, 7, 0, true);
		end_side_2.texOffs(9, 7).addBox(9, -36, -15, 6, 0, 7, 0, true);
		end_side_2.texOffs(9, 0).addBox(0, -35, -15, 7, 0, 7, 0, true);
		end_side_2.texOffs(227, 193).addBox(12, -31, -15, 6, 1, 7, 0, true);

		roof_4_r7 = new ModelMapper(modelDataWrapper);
		roof_4_r7.setPos(7, -35, 0);
		end_side_2.addChild(roof_4_r7);
		setRotationAngle(roof_4_r7, 0, 0, -0.3491F);
		roof_4_r7.texOffs(0, 0).addBox(0, 0, -15, 3, 0, 7, 0, true);

		roof_2_r13 = new ModelMapper(modelDataWrapper);
		roof_2_r13.setPos(15, -36, 0);
		end_side_2.addChild(roof_2_r13);
		setRotationAngle(roof_2_r13, 0, 0, 0.7854F);
		roof_2_r13.texOffs(0, 7).addBox(0, 0, -15, 3, 0, 7, 0, true);

		window_bottom_r24 = new ModelMapper(modelDataWrapper);
		window_bottom_r24.setPos(21, -7, 0);
		end_side_2.addChild(window_bottom_r24);
		setRotationAngle(window_bottom_r24, 0, 0, 0.2094F);
		window_bottom_r24.texOffs(56, 101).addBox(-1, 0, -15, 0, 8, 7, 0, true);

		window_top_r24 = new ModelMapper(modelDataWrapper);
		window_top_r24.setPos(21, -13, 0);
		end_side_2.addChild(window_top_r24);
		setRotationAngle(window_top_r24, 0, 0, -0.1396F);
		window_top_r24.texOffs(56, 79).addBox(-1, -22, -15, 0, 22, 7, 0, true);
		window_top_r24.texOffs(237, 36).addBox(-3, -22, 3, 2, 1, 13, 0, true);

		end_light = new ModelMapper(modelDataWrapper);
		end_light.setPos(0, 24, 0);
		end_light.texOffs(317, 44).addBox(-7, -35.1F, -15, 14, 0, 4, 0, false);

		end_translucent = new ModelMapper(modelDataWrapper);
		end_translucent.setPos(0, 24, 0);
		end_translucent.texOffs(331, 388).addBox(-7, -34, -8, 14, 34, 0, 0, false);
		end_translucent.texOffs(359, 388).addBox(-7, -34, 45, 14, 34, 0, 0, false);

		end_exterior = new ModelMapper(modelDataWrapper);
		end_exterior.setPos(0, 24, 0);


		end_exterior_side_1 = new ModelMapper(modelDataWrapper);
		end_exterior_side_1.setPos(0, 0, 0);
		end_exterior.addChild(end_exterior_side_1);
		end_exterior_side_1.texOffs(362, 125).addBox(-21, -13, -15, 1, 6, 18, 0, false);
		end_exterior_side_1.texOffs(360, 158).addBox(-21, -13, 16, 1, 6, 29, 0, false);
		end_exterior_side_1.texOffs(325, 188).addBox(-21, 0, 3, 2, 1, 13, 0, false);
		end_exterior_side_1.texOffs(303, 385).addBox(-20.5F, -34, 45, 11, 34, 0, 0, false);
		end_exterior_side_1.texOffs(32, 274).addBox(-18, -39, 45, 18, 5, 0, 0, false);

		roof_4_r8 = new ModelMapper(modelDataWrapper);
		roof_4_r8.setPos(-10.1709F, -40.8501F, 0);
		end_exterior_side_1.addChild(roof_4_r8);
		setRotationAngle(roof_4_r8, 0, 0, 1.0472F);
		roof_4_r8.texOffs(235, 321).addBox(0, 3, -15, 1, 3, 30, 0, false);
		roof_4_r8.texOffs(319, 0).addBox(0, 3, 15, 1, 3, 30, 0, false);

		roof_3_r8 = new ModelMapper(modelDataWrapper);
		roof_3_r8.setPos(-17.9382F, -34.7859F, 0);
		end_exterior_side_1.addChild(roof_3_r8);
		setRotationAngle(roof_3_r8, 0, 0, 0.6981F);
		roof_3_r8.texOffs(204, 241).addBox(0, -4, -15, 0, 4, 30, 0, false);
		roof_3_r8.texOffs(0, 387).addBox(0, -4, 15, 1, 4, 30, 0, false);

		door_top_r1 = new ModelMapper(modelDataWrapper);
		door_top_r1.setPos(-21, -13, 0);
		end_exterior_side_1.addChild(door_top_r1);
		setRotationAngle(door_top_r1, 0, 0, 0.1396F);
		door_top_r1.texOffs(265, 275).addBox(0, -22, 3, 1, 1, 13, 0, false);
		door_top_r1.texOffs(173, 265).addBox(0, -22, 16, 1, 22, 29, 0, false);
		door_top_r1.texOffs(265, 369).addBox(0, -22, -15, 1, 22, 18, 0, false);

		window_bottom_2_r4 = new ModelMapper(modelDataWrapper);
		window_bottom_2_r4.setPos(-21, -7, 0);
		end_exterior_side_1.addChild(window_bottom_2_r4);
		setRotationAngle(window_bottom_2_r4, 0, 0, -0.2094F);
		window_bottom_2_r4.texOffs(127, 319).addBox(0, 0, 16, 1, 8, 29, 0, false);
		window_bottom_2_r4.texOffs(360, 251).addBox(0, 0, -15, 1, 8, 18, 0, false);

		end_exterior_side_2 = new ModelMapper(modelDataWrapper);
		end_exterior_side_2.setPos(0, 0, 0);
		end_exterior.addChild(end_exterior_side_2);
		end_exterior_side_2.texOffs(362, 125).addBox(20, -13, -15, 1, 6, 18, 0, true);
		end_exterior_side_2.texOffs(360, 158).addBox(20, -13, 16, 1, 6, 29, 0, true);
		end_exterior_side_2.texOffs(325, 188).addBox(19, 0, 3, 2, 1, 13, 0, true);
		end_exterior_side_2.texOffs(303, 385).addBox(9.5F, -34, 45, 11, 34, 0, 0, true);
		end_exterior_side_2.texOffs(32, 274).addBox(0, -39, 45, 18, 5, 0, 0, true);

		roof_5_r6 = new ModelMapper(modelDataWrapper);
		roof_5_r6.setPos(10.1709F, -40.8501F, 0);
		end_exterior_side_2.addChild(roof_5_r6);
		setRotationAngle(roof_5_r6, 0, 0, -1.0472F);
		roof_5_r6.texOffs(235, 321).addBox(-1, 3, -15, 1, 3, 30, 0, true);
		roof_5_r6.texOffs(319, 0).addBox(-1, 3, 15, 1, 3, 30, 0, true);

		roof_4_r9 = new ModelMapper(modelDataWrapper);
		roof_4_r9.setPos(17.9382F, -34.7859F, 0);
		end_exterior_side_2.addChild(roof_4_r9);
		setRotationAngle(roof_4_r9, 0, 0, -0.6981F);
		roof_4_r9.texOffs(204, 241).addBox(0, -4, -15, 0, 4, 30, 0, true);
		roof_4_r9.texOffs(0, 387).addBox(-1, -4, 15, 1, 4, 30, 0, true);

		door_top_r2 = new ModelMapper(modelDataWrapper);
		door_top_r2.setPos(21, -13, 0);
		end_exterior_side_2.addChild(door_top_r2);
		setRotationAngle(door_top_r2, 0, 0, -0.1396F);
		door_top_r2.texOffs(265, 275).addBox(-1, -22, 3, 1, 1, 13, 0, true);
		door_top_r2.texOffs(173, 265).addBox(-1, -22, 16, 1, 22, 29, 0, true);
		door_top_r2.texOffs(265, 369).addBox(-1, -22, -15, 1, 22, 18, 0, true);

		window_bottom_3_r3 = new ModelMapper(modelDataWrapper);
		window_bottom_3_r3.setPos(21, -7, 0);
		end_exterior_side_2.addChild(window_bottom_3_r3);
		setRotationAngle(window_bottom_3_r3, 0, 0, 0.2094F);
		window_bottom_3_r3.texOffs(127, 319).addBox(-1, 0, 16, 1, 8, 29, 0, true);
		window_bottom_3_r3.texOffs(360, 251).addBox(-1, 0, -15, 1, 8, 18, 0, true);

		roof_vent = new ModelMapper(modelDataWrapper);
		roof_vent.setPos(0, 24, 0);


		roof_vent_side_1 = new ModelMapper(modelDataWrapper);
		roof_vent_side_1.setPos(0, -42, 10);
		roof_vent.addChild(roof_vent_side_1);
		roof_vent_side_1.texOffs(76, 96).addBox(-6, 0, -70, 6, 4, 70, 0, false);

		vent_4_r1 = new ModelMapper(modelDataWrapper);
		vent_4_r1.setPos(0, 0, 0);
		roof_vent_side_1.addChild(vent_4_r1);
		setRotationAngle(vent_4_r1, -0.6981F, 0, 0);
		vent_4_r1.texOffs(67, 53).addBox(-6, 0, 0, 6, 0, 5, 0, false);

		vent_3_r1 = new ModelMapper(modelDataWrapper);
		vent_3_r1.setPos(-6, 0, 0);
		roof_vent_side_1.addChild(vent_3_r1);
		setRotationAngle(vent_3_r1, 0, 0.6981F, 1.1345F);
		vent_3_r1.texOffs(72, 58).addBox(0, 0, 0, 1, 6, 5, 0, false);

		vent_1_r1 = new ModelMapper(modelDataWrapper);
		vent_1_r1.setPos(-6, 0, -10);
		roof_vent_side_1.addChild(vent_1_r1);
		setRotationAngle(vent_1_r1, 0, 0, 1.1345F);
		vent_1_r1.texOffs(161, 0).addBox(0, 0, -60, 3, 6, 70, 0, false);

		roof_vent_side_2 = new ModelMapper(modelDataWrapper);
		roof_vent_side_2.setPos(0, -42, 10);
		roof_vent.addChild(roof_vent_side_2);
		roof_vent_side_2.texOffs(76, 96).addBox(0, 0, -70, 6, 4, 70, 0, true);

		vent_5_r1 = new ModelMapper(modelDataWrapper);
		vent_5_r1.setPos(0, 0, 0);
		roof_vent_side_2.addChild(vent_5_r1);
		setRotationAngle(vent_5_r1, -0.6981F, 0, 0);
		vent_5_r1.texOffs(67, 53).addBox(0, 0, 0, 6, 0, 5, 0, true);

		vent_4_r2 = new ModelMapper(modelDataWrapper);
		vent_4_r2.setPos(6, 0, 0);
		roof_vent_side_2.addChild(vent_4_r2);
		setRotationAngle(vent_4_r2, 0, -0.6981F, -1.1345F);
		vent_4_r2.texOffs(72, 58).addBox(-1, 0, 0, 1, 6, 5, 0, true);

		vent_2_r1 = new ModelMapper(modelDataWrapper);
		vent_2_r1.setPos(6, 0, -10);
		roof_vent_side_2.addChild(vent_2_r1);
		setRotationAngle(vent_2_r1, 0, 0, -1.1345F);
		vent_2_r1.texOffs(161, 0).addBox(-3, 0, -60, 3, 6, 70, 0, true);

		head_exterior = new ModelMapper(modelDataWrapper);
		head_exterior.setPos(0, 24, 0);


		head_side_1 = new ModelMapper(modelDataWrapper);
		head_side_1.setPos(0, 0, 0);
		head_exterior.addChild(head_side_1);
		head_side_1.texOffs(155, 97).addBox(-19, 0, -15, 1, 2, 73, 0, false);
		head_side_1.texOffs(158, 192).addBox(-21, -13, -15, 1, 6, 67, 0, false);
		head_side_1.texOffs(279, 44).addBox(-21, 0, -57, 2, 1, 13, 0, false);
		head_side_1.texOffs(125, 356).addBox(-21, -13, -44, 1, 6, 29, 0, false);
		head_side_1.texOffs(361, 286).addBox(-21, -13, -75, 1, 6, 18, 0, false);

		roof_4_r10 = new ModelMapper(modelDataWrapper);
		roof_4_r10.setPos(-10.1709F, -40.8501F, 0);
		head_side_1.addChild(roof_4_r10);
		setRotationAngle(roof_4_r10, 0, 0, 1.0472F);
		roof_4_r10.texOffs(157, 326).addBox(0, 3, -75, 1, 3, 30, 0, false);
		roof_4_r10.texOffs(328, 185).addBox(0, 3, -45, 1, 3, 30, 0, false);

		roof_3_r9 = new ModelMapper(modelDataWrapper);
		roof_3_r9.setPos(-17.9382F, -34.7859F, 0);
		head_side_1.addChild(roof_3_r9);
		setRotationAngle(roof_3_r9, 0, 0, 0.6981F);
		roof_3_r9.texOffs(32, 261).addBox(0, -4, -75, 0, 4, 30, 0, false);
		roof_3_r9.texOffs(32, 265).addBox(0, -4, -45, 0, 4, 30, 0, false);

		window_top_1_r2 = new ModelMapper(modelDataWrapper);
		window_top_1_r2.setPos(-21, -13, 0);
		head_side_1.addChild(window_top_1_r2);
		setRotationAngle(window_top_1_r2, 0, 0, 0.1396F);
		window_top_1_r2.texOffs(362, 74).addBox(0, -22, -75, 1, 22, 18, 0, false);
		window_top_1_r2.texOffs(113, 259).addBox(0, -22, -44, 1, 22, 29, 0, false);
		window_top_1_r2.texOffs(237, 50).addBox(0, -22, -57, 1, 1, 13, 0, false);
		window_top_1_r2.texOffs(93, 172).addBox(0, -22, -15, 1, 22, 65, 0, false);

		window_bottom_1_r2 = new ModelMapper(modelDataWrapper);
		window_bottom_1_r2.setPos(-21, -7, 0);
		head_side_1.addChild(window_bottom_1_r2);
		setRotationAngle(window_bottom_1_r2, 0, 0, -0.2094F);
		window_bottom_1_r2.texOffs(161, 0).addBox(0, 0, -75, 1, 8, 18, 0, false);
		window_bottom_1_r2.texOffs(62, 319).addBox(0, 0, -44, 1, 8, 29, 0, false);
		window_bottom_1_r2.texOffs(0, 86).addBox(0, 0, -15, 1, 8, 72, 0, false);

		end_r1 = new ModelMapper(modelDataWrapper);
		end_r1.setPos(-19, 2, 33);
		head_side_1.addChild(end_r1);
		setRotationAngle(end_r1, 0, 0, -0.2618F);
		end_r1.texOffs(204, 256).addBox(0, 0, 0, 0, 11, 26, 0, false);

		roof_11_r1 = new ModelMapper(modelDataWrapper);
		roof_11_r1.setPos(-16.8732F, -11.8177F, 56.7312F);
		head_side_1.addChild(roof_11_r1);
		setRotationAngle(roof_11_r1, 0.1745F, -1.0472F, 0);
		roof_11_r1.texOffs(361, 328).addBox(-9.5F, -3, 0, 19, 15, 0, 0, false);

		roof_12_r1 = new ModelMapper(modelDataWrapper);
		roof_12_r1.setPos(-15.5128F, 10.8301F, 58.2552F);
		head_side_1.addChild(roof_12_r1);
		setRotationAngle(roof_12_r1, 0, -1.0472F, 0);
		roof_12_r1.texOffs(161, 26).addBox(-3.5F, -1.5F, 0, 7, 3, 0, 0, false);

		roof_11_r2 = new ModelMapper(modelDataWrapper);
		roof_11_r2.setPos(-15.5954F, 7.1646F, 60.6123F);
		head_side_1.addChild(roof_11_r2);
		setRotationAngle(roof_11_r2, -0.5236F, -1.0472F, 0);
		roof_11_r2.texOffs(134, 91).addBox(-5.5F, -2.5F, 0, 11, 5, 0, 0, false);

		roof_10_r1 = new ModelMapper(modelDataWrapper);
		roof_10_r1.setPos(-16.9278F, 3, 60.8042F);
		head_side_1.addChild(roof_10_r1);
		setRotationAngle(roof_10_r1, 0, -1.0472F, 0);
		roof_10_r1.texOffs(134, 86).addBox(-6, -3, 0, 12, 5, 0, 0, false);

		roof_10_r2 = new ModelMapper(modelDataWrapper);
		roof_10_r2.setPos(-7, 0, 70);
		head_side_1.addChild(roof_10_r2);
		setRotationAngle(roof_10_r2, 0.2618F, -0.5236F, 0);
		roof_10_r2.texOffs(218, 86).addBox(-9, -11, 0, 9, 11, 0, 0, false);

		roof_11_r3 = new ModelMapper(modelDataWrapper);
		roof_11_r3.setPos(-10.0747F, 10.7433F, 63.3255F);
		head_side_1.addChild(roof_11_r3);
		setRotationAngle(roof_11_r3, 0, -0.5236F, 0);
		roof_11_r3.texOffs(28, 50).addBox(-5, -1.5F, 0, 10, 3, 0, 0, false);

		roof_10_r3 = new ModelMapper(modelDataWrapper);
		roof_10_r3.setPos(-7, 5, 70);
		head_side_1.addChild(roof_10_r3);
		setRotationAngle(roof_10_r3, -0.7854F, -0.5236F, 0);
		roof_10_r3.texOffs(279, 58).addBox(-11, 0, 0, 11, 6, 0, 0, false);

		roof_9_r1 = new ModelMapper(modelDataWrapper);
		roof_9_r1.setPos(-7, 0, 70);
		head_side_1.addChild(roof_9_r1);
		setRotationAngle(roof_9_r1, 0, -0.5236F, 0);
		roof_9_r1.texOffs(161, 13).addBox(-8, 0, 0, 8, 5, 0, 0, false);

		roof_7_r2 = new ModelMapper(modelDataWrapper);
		roof_7_r2.setPos(-18.5824F, -19.5437F, 43.232F);
		head_side_1.addChild(roof_7_r2);
		setRotationAngle(roof_7_r2, -0.2182F, 0.5236F, 1.0472F);
		roof_7_r2.texOffs(118, 360).addBox(0, -8, -28.5F, 0, 11, 57, 0, false);

		roof_4_r11 = new ModelMapper(modelDataWrapper);
		roof_4_r11.setPos(-2, -42, -9);
		head_side_1.addChild(roof_4_r11);
		setRotationAngle(roof_4_r11, 0, 0, 1.5708F);
		roof_4_r11.texOffs(265, 289).addBox(0, -2, -6, 1, 4, 12, 0, false);

		roof_4_r12 = new ModelMapper(modelDataWrapper);
		roof_4_r12.setPos(-8.1369F, -39.4953F, 9.8171F);
		head_side_1.addChild(roof_4_r12);
		setRotationAngle(roof_4_r12, -0.1309F, 0.1309F, 1.3963F);
		roof_4_r12.texOffs(32, 292).addBox(0, -3.5F, -13.5F, 0, 7, 27, 0, false);

		roof_3_r10 = new ModelMapper(modelDataWrapper);
		roof_3_r10.setPos(-13.6527F, -37.467F, 10.5898F);
		head_side_1.addChild(roof_3_r10);
		setRotationAngle(roof_3_r10, -0.0873F, 0.0873F, 1.0472F);
		roof_3_r10.texOffs(204, 247).addBox(0, -3.5F, -14, 0, 7, 28, 0, false);

		roof_2_r14 = new ModelMapper(modelDataWrapper);
		roof_2_r14.setPos(-17.2178F, -34.6364F, 11.8407F);
		head_side_1.addChild(roof_2_r14);
		setRotationAngle(roof_2_r14, -0.0436F, 0.0436F, 0.6981F);
		roof_2_r14.texOffs(204, 235).addBox(0, -3, -15, 0, 6, 30, 0, false);

		roof_3_r11 = new ModelMapper(modelDataWrapper);
		roof_3_r11.setPos(-6.6372F, -40.9654F, -9);
		head_side_1.addChild(roof_3_r11);
		setRotationAngle(roof_3_r11, 0, 0, 1.3963F);
		roof_3_r11.texOffs(230, 139).addBox(-0.5F, -3.5F, -6, 1, 7, 12, 0, false);

		roof_2_r15 = new ModelMapper(modelDataWrapper);
		roof_2_r15.setPos(-12.519F, -38.9171F, -9);
		head_side_1.addChild(roof_2_r15);
		setRotationAngle(roof_2_r15, 0, 0, 1.0472F);
		roof_2_r15.texOffs(0, 274).addBox(-0.5F, -3, -6, 1, 6, 12, 0, false);

		roof_1_r13 = new ModelMapper(modelDataWrapper);
		roof_1_r13.setPos(-16.2696F, -35.9966F, -9);
		head_side_1.addChild(roof_1_r13);
		setRotationAngle(roof_1_r13, 0, 0, 0.6981F);
		roof_1_r13.texOffs(296, 243).addBox(-0.5F, -2, -6, 1, 4, 12, 0, false);

		head_side_2 = new ModelMapper(modelDataWrapper);
		head_side_2.setPos(0, 0, 0);
		head_exterior.addChild(head_side_2);
		head_side_2.texOffs(155, 97).addBox(18, 0, -15, 1, 2, 73, 0, true);
		head_side_2.texOffs(158, 192).addBox(20, -13, -15, 1, 6, 67, 0, true);
		head_side_2.texOffs(279, 44).addBox(19, 0, -57, 2, 1, 13, 0, true);
		head_side_2.texOffs(125, 356).addBox(20, -13, -44, 1, 6, 29, 0, true);
		head_side_2.texOffs(361, 286).addBox(20, -13, -75, 1, 6, 18, 0, true);

		roof_5_r7 = new ModelMapper(modelDataWrapper);
		roof_5_r7.setPos(10.1709F, -40.8501F, 0);
		head_side_2.addChild(roof_5_r7);
		setRotationAngle(roof_5_r7, 0, 0, -1.0472F);
		roof_5_r7.texOffs(157, 326).addBox(-1, 3, -75, 1, 3, 30, 0, true);
		roof_5_r7.texOffs(328, 185).addBox(-1, 3, -45, 1, 3, 30, 0, true);

		roof_4_r13 = new ModelMapper(modelDataWrapper);
		roof_4_r13.setPos(17.9382F, -34.7859F, 0);
		head_side_2.addChild(roof_4_r13);
		setRotationAngle(roof_4_r13, 0, 0, -0.6981F);
		roof_4_r13.texOffs(32, 261).addBox(0, -4, -75, 0, 4, 30, 0, true);
		roof_4_r13.texOffs(32, 265).addBox(0, -4, -45, 0, 4, 30, 0, true);

		window_top_2_r4 = new ModelMapper(modelDataWrapper);
		window_top_2_r4.setPos(21, -13, 0);
		head_side_2.addChild(window_top_2_r4);
		setRotationAngle(window_top_2_r4, 0, 0, -0.1396F);
		window_top_2_r4.texOffs(362, 74).addBox(-1, -22, -75, 1, 22, 18, 0, true);
		window_top_2_r4.texOffs(113, 259).addBox(-1, -22, -44, 1, 22, 29, 0, true);
		window_top_2_r4.texOffs(237, 50).addBox(-1, -22, -57, 1, 1, 13, 0, true);
		window_top_2_r4.texOffs(93, 172).addBox(-1, -22, -15, 1, 22, 65, 0, true);

		window_bottom_2_r5 = new ModelMapper(modelDataWrapper);
		window_bottom_2_r5.setPos(21, -7, 0);
		head_side_2.addChild(window_bottom_2_r5);
		setRotationAngle(window_bottom_2_r5, 0, 0, 0.2094F);
		window_bottom_2_r5.texOffs(161, 0).addBox(-1, 0, -75, 1, 8, 18, 0, true);
		window_bottom_2_r5.texOffs(62, 319).addBox(-1, 0, -44, 1, 8, 29, 0, true);
		window_bottom_2_r5.texOffs(0, 86).addBox(-1, 0, -15, 1, 8, 72, 0, true);

		end_r2 = new ModelMapper(modelDataWrapper);
		end_r2.setPos(19, 2, 33);
		head_side_2.addChild(end_r2);
		setRotationAngle(end_r2, 0, 0, 0.2618F);
		end_r2.texOffs(204, 256).addBox(0, 0, 0, 0, 11, 26, 0, true);

		roof_12_r2 = new ModelMapper(modelDataWrapper);
		roof_12_r2.setPos(16.8732F, -11.8177F, 56.7312F);
		head_side_2.addChild(roof_12_r2);
		setRotationAngle(roof_12_r2, 0.1745F, 1.0472F, 0);
		roof_12_r2.texOffs(361, 328).addBox(-9.5F, -3, 0, 19, 15, 0, 0, true);

		roof_13_r1 = new ModelMapper(modelDataWrapper);
		roof_13_r1.setPos(15.5128F, 10.8301F, 58.2552F);
		head_side_2.addChild(roof_13_r1);
		setRotationAngle(roof_13_r1, 0, 1.0472F, 0);
		roof_13_r1.texOffs(161, 26).addBox(-3.5F, -1.5F, 0, 7, 3, 0, 0, true);

		roof_12_r3 = new ModelMapper(modelDataWrapper);
		roof_12_r3.setPos(15.5954F, 7.1646F, 60.6123F);
		head_side_2.addChild(roof_12_r3);
		setRotationAngle(roof_12_r3, -0.5236F, 1.0472F, 0);
		roof_12_r3.texOffs(134, 91).addBox(-5.5F, -2.5F, 0, 11, 5, 0, 0, true);

		roof_11_r4 = new ModelMapper(modelDataWrapper);
		roof_11_r4.setPos(16.9278F, 3, 60.8042F);
		head_side_2.addChild(roof_11_r4);
		setRotationAngle(roof_11_r4, 0, 1.0472F, 0);
		roof_11_r4.texOffs(134, 86).addBox(-6, -3, 0, 12, 5, 0, 0, true);

		roof_11_r5 = new ModelMapper(modelDataWrapper);
		roof_11_r5.setPos(7, 0, 70);
		head_side_2.addChild(roof_11_r5);
		setRotationAngle(roof_11_r5, 0.2618F, 0.5236F, 0);
		roof_11_r5.texOffs(218, 86).addBox(0, -11, 0, 9, 11, 0, 0, true);

		roof_12_r4 = new ModelMapper(modelDataWrapper);
		roof_12_r4.setPos(10.0747F, 10.7433F, 63.3255F);
		head_side_2.addChild(roof_12_r4);
		setRotationAngle(roof_12_r4, 0, 0.5236F, 0);
		roof_12_r4.texOffs(28, 50).addBox(-5, -1.5F, 0, 10, 3, 0, 0, true);

		roof_11_r6 = new ModelMapper(modelDataWrapper);
		roof_11_r6.setPos(7, 5, 70);
		head_side_2.addChild(roof_11_r6);
		setRotationAngle(roof_11_r6, -0.7854F, 0.5236F, 0);
		roof_11_r6.texOffs(279, 58).addBox(0, 0, 0, 11, 6, 0, 0, true);

		roof_10_r4 = new ModelMapper(modelDataWrapper);
		roof_10_r4.setPos(7, 0, 70);
		head_side_2.addChild(roof_10_r4);
		setRotationAngle(roof_10_r4, 0, 0.5236F, 0);
		roof_10_r4.texOffs(161, 13).addBox(0, 0, 0, 8, 5, 0, 0, true);

		roof_8_r1 = new ModelMapper(modelDataWrapper);
		roof_8_r1.setPos(18.5824F, -19.5437F, 43.232F);
		head_side_2.addChild(roof_8_r1);
		setRotationAngle(roof_8_r1, -0.2182F, -0.5236F, -1.0472F);
		roof_8_r1.texOffs(118, 360).addBox(0, -8, -28.5F, 0, 11, 57, 0, true);

		roof_5_r8 = new ModelMapper(modelDataWrapper);
		roof_5_r8.setPos(2, -42, -9);
		head_side_2.addChild(roof_5_r8);
		setRotationAngle(roof_5_r8, 0, 0, -1.5708F);
		roof_5_r8.texOffs(265, 289).addBox(-1, -2, -6, 1, 4, 12, 0, true);

		roof_5_r9 = new ModelMapper(modelDataWrapper);
		roof_5_r9.setPos(8.1369F, -39.4953F, 9.8171F);
		head_side_2.addChild(roof_5_r9);
		setRotationAngle(roof_5_r9, -0.1309F, -0.1309F, -1.3963F);
		roof_5_r9.texOffs(32, 292).addBox(0, -3.5F, -13.5F, 0, 7, 27, 0, true);

		roof_4_r14 = new ModelMapper(modelDataWrapper);
		roof_4_r14.setPos(13.6527F, -37.467F, 10.5898F);
		head_side_2.addChild(roof_4_r14);
		setRotationAngle(roof_4_r14, -0.0873F, -0.0873F, -1.0472F);
		roof_4_r14.texOffs(204, 247).addBox(0, -3.5F, -14, 0, 7, 28, 0, true);

		roof_3_r12 = new ModelMapper(modelDataWrapper);
		roof_3_r12.setPos(17.2178F, -34.6364F, 11.8407F);
		head_side_2.addChild(roof_3_r12);
		setRotationAngle(roof_3_r12, -0.0436F, -0.0436F, -0.6981F);
		roof_3_r12.texOffs(204, 235).addBox(0, -3, -15, 0, 6, 30, 0, true);

		roof_4_r15 = new ModelMapper(modelDataWrapper);
		roof_4_r15.setPos(6.6372F, -40.9654F, -9);
		head_side_2.addChild(roof_4_r15);
		setRotationAngle(roof_4_r15, 0, 0, -1.3963F);
		roof_4_r15.texOffs(230, 139).addBox(-0.5F, -3.5F, -6, 1, 7, 12, 0, true);

		roof_3_r13 = new ModelMapper(modelDataWrapper);
		roof_3_r13.setPos(12.519F, -38.9171F, -9);
		head_side_2.addChild(roof_3_r13);
		setRotationAngle(roof_3_r13, 0, 0, -1.0472F);
		roof_3_r13.texOffs(0, 274).addBox(-0.5F, -3, -6, 1, 6, 12, 0, true);

		roof_2_r16 = new ModelMapper(modelDataWrapper);
		roof_2_r16.setPos(16.2696F, -35.9966F, -9);
		head_side_2.addChild(roof_2_r16);
		setRotationAngle(roof_2_r16, 0, 0, -0.6981F);
		roof_2_r16.texOffs(296, 243).addBox(-0.5F, -2, -6, 1, 4, 12, 0, true);

		middle = new ModelMapper(modelDataWrapper);
		middle.setPos(0, 0, 0);
		head_exterior.addChild(middle);
		middle.texOffs(181, 8).addBox(-7, 0, 70, 14, 5, 0, 0, false);
		middle.texOffs(0, 50).addBox(-7, 9.2433F, 65.7571F, 14, 3, 0, 0, false);
		middle.texOffs(0, 0).addBox(-19, 0, -15, 38, 1, 85, 0, false);
		middle.texOffs(0, 231).addBox(-20, -42, -14.5F, 40, 42, 1, 0, false);

		roof_8_r2 = new ModelMapper(modelDataWrapper);
		roof_8_r2.setPos(0, 0, 70);
		middle.addChild(roof_8_r2);
		setRotationAngle(roof_8_r2, 0.3491F, 0, 0);
		roof_8_r2.texOffs(32, 326).addBox(-8, -9, 0, 16, 9, 0, 0, false);

		roof_6_r4 = new ModelMapper(modelDataWrapper);
		roof_6_r4.setPos(-10, -37.8324F, 20.6354F);
		middle.addChild(roof_6_r4);
		setRotationAngle(roof_6_r4, 0, -0.5672F, -1.5708F);
		roof_6_r4.texOffs(118, 336).addBox(0, -3, 0, 0, 26, 55, 0, false);

		roof_5_r10 = new ModelMapper(modelDataWrapper);
		roof_5_r10.setPos(0, -42, -3);
		middle.addChild(roof_5_r10);
		setRotationAngle(roof_5_r10, 0, -0.1745F, -1.5708F);
		roof_5_r10.texOffs(144, 241).addBox(0, -10, 0, 0, 20, 24, 0, false);

		roof_9_r2 = new ModelMapper(modelDataWrapper);
		roof_9_r2.setPos(0, 5, 70);
		middle.addChild(roof_9_r2);
		setRotationAngle(roof_9_r2, -0.7854F, 0, 0);
		roof_9_r2.texOffs(230, 158).addBox(-7, 0, 0, 14, 6, 0, 0, false);

		bottom_middle = new ModelMapper(modelDataWrapper);
		bottom_middle.setPos(0, 24, 0);
		bottom_middle.texOffs(264, 235).addBox(-17, 2, -15, 1, 10, 30, 0, false);

		bottom_end = new ModelMapper(modelDataWrapper);
		bottom_end.setPos(0, 24, 0);
		bottom_end.texOffs(0, 0).addBox(-17, 2, -7, 1, 10, 14, 0, false);

		seat = new ModelMapper(modelDataWrapper);
		seat.setPos(0, 24, 0);
		seat.texOffs(181, 0).addBox(-4, -6, -4, 8, 1, 7, 0, false);
		seat.texOffs(214, 0).addBox(-3.5F, -22.644F, 4.0686F, 7, 5, 1, 0, false);

		seat_2_r1 = new ModelMapper(modelDataWrapper);
		seat_2_r1.setPos(0, -6, 2);
		seat.addChild(seat_2_r1);
		setRotationAngle(seat_2_r1, -0.1745F, 0, 0);
		seat_2_r1.texOffs(161, 0).addBox(-4, -12, 0, 8, 12, 1, 0, false);

		headlights = new ModelMapper(modelDataWrapper);
		headlights.setPos(0, 24, 0);


		headlight_2_r1 = new ModelMapper(modelDataWrapper);
		headlight_2_r1.setPos(18.5824F, -19.5437F, 43.232F);
		headlights.addChild(headlight_2_r1);
		setRotationAngle(headlight_2_r1, -0.2182F, -0.5236F, -1.0472F);
		headlight_2_r1.texOffs(345, 53).addBox(0.1F, -8, 10.5F, 0, 6, 13, 0, true);

		headlight_1_r1 = new ModelMapper(modelDataWrapper);
		headlight_1_r1.setPos(-18.5824F, -19.5437F, 43.232F);
		headlights.addChild(headlight_1_r1);
		setRotationAngle(headlight_1_r1, -0.2182F, 0.5236F, 1.0472F);
		headlight_1_r1.texOffs(345, 53).addBox(-0.1F, -8, 10.5F, 0, 6, 13, 0, false);

		tail_lights = new ModelMapper(modelDataWrapper);
		tail_lights.setPos(0, 24, 0);


		tail_light_2_r1 = new ModelMapper(modelDataWrapper);
		tail_light_2_r1.setPos(18.5824F, -19.5437F, 43.232F);
		tail_lights.addChild(tail_light_2_r1);
		setRotationAngle(tail_light_2_r1, -0.2182F, -0.5236F, -1.0472F);
		tail_light_2_r1.texOffs(345, 59).addBox(0.1F, -8, 10.5F, 0, 6, 13, 0, true);

		tail_light_1_r1 = new ModelMapper(modelDataWrapper);
		tail_light_1_r1.setPos(-18.5824F, -19.5437F, 43.232F);
		tail_lights.addChild(tail_light_1_r1);
		setRotationAngle(tail_light_1_r1, -0.2182F, 0.5236F, 1.0472F);
		tail_light_1_r1.texOffs(345, 59).addBox(-0.1F, -8, 10.5F, 0, 6, 13, 0, false);

		door_light_off = new ModelMapper(modelDataWrapper);
		door_light_off.setPos(0, 24, 0);


		door_light_off_r1 = new ModelMapper(modelDataWrapper);
		door_light_off_r1.setPos(-21, -13, 0);
		door_light_off.addChild(door_light_off_r1);
		setRotationAngle(door_light_off_r1, 0, 0, 0.1396F);
		door_light_off_r1.texOffs(0, 0).addBox(-0.5F, -21.5F, -0.5F, 1, 1, 1, 0, false);

		door_light_on = new ModelMapper(modelDataWrapper);
		door_light_on.setPos(0, 24, 0);


		door_light_on_r1 = new ModelMapper(modelDataWrapper);
		door_light_on_r1.setPos(-21, -13, 0);
		door_light_on.addChild(door_light_on_r1);
		setRotationAngle(door_light_on_r1, 0, 0, 0.1396F);
		door_light_on_r1.texOffs(0, 2).addBox(-0.5F, -21.5F, -0.5F, 1, 1, 1, 0, false);

		modelDataWrapper.setModelPart(textureWidth, textureHeight);
		window.setModelPart();
		window_light.setModelPart();
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
		end_light.setModelPart();
		end_translucent.setModelPart();
		end_exterior.setModelPart();
		roof_vent.setModelPart();
		head_exterior.setModelPart();
		bottom_middle.setModelPart();
		bottom_end.setModelPart();
		seat.setModelPart();
		headlights.setModelPart();
		tail_lights.setModelPart();
		door_light_off.setModelPart();
		door_light_on.setModelPart();
	}

	private static final int DOOR_MAX = 13;

	@Override
	protected void renderWindowPositions(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
		final ModelMapper[] windowParts = isEnd1Head || isEnd2Head ? windowEndParts() : windowParts();
		final int loopStart = getBogiePositions()[0] + (isEnd1Head ? 94 : 4);
		final int loopEnd = getBogiePositions()[1] - (isEnd2Head ? 94 : 4);

		switch (renderStage) {
			case LIGHTS:
				for (int i = loopStart; i <= loopEnd; i += 30) {
					renderMirror(window_light, matrices, vertices, light, i);
				}
				break;
			case INTERIOR:
				int j = 0;
				for (int i = loopStart; i <= loopEnd; i += 30) {
					renderMirror(window, matrices, vertices, light, i);
					if (renderDetails) {
						if (j % 4 < 2) {
							renderOnce(seat, matrices, vertices, light, -16, i - 5);
							renderOnce(seat, matrices, vertices, light, -16, i + 10);
							renderOnce(seat, matrices, vertices, light, -8, i - 5);
							renderOnce(seat, matrices, vertices, light, -8, i + 10);
							renderOnce(seat, matrices, vertices, light, 8, i - 5);
							renderOnce(seat, matrices, vertices, light, 8, i + 10);
							renderOnce(seat, matrices, vertices, light, 16, i - 5);
							renderOnce(seat, matrices, vertices, light, 16, i + 10);
						} else {
							renderOnceFlipped(seat, matrices, vertices, light, -16, i - 10);
							renderOnceFlipped(seat, matrices, vertices, light, -16, i + 5);
							renderOnceFlipped(seat, matrices, vertices, light, -8, i - 10);
							renderOnceFlipped(seat, matrices, vertices, light, -8, i + 5);
							renderOnceFlipped(seat, matrices, vertices, light, 8, i - 10);
							renderOnceFlipped(seat, matrices, vertices, light, 8, i + 5);
							renderOnceFlipped(seat, matrices, vertices, light, 16, i - 10);
							renderOnceFlipped(seat, matrices, vertices, light, 16, i + 5);
						}
					}
					j++;
				}
				break;
			case EXTERIOR:
				int k = 0;
				for (int i = loopStart; i <= loopEnd; i += 30) {
					if (!isEnd2Head) {
						renderOnce(windowParts[windowParts.length - k - 1], matrices, vertices, light, i);
					}
					if (!isEnd1Head || isEnd2Head) {
						renderOnceFlipped(windowParts[k], matrices, vertices, light, i);
					}
					renderMirror(roof_exterior, matrices, vertices, light, i);
					k++;
				}
				final int[] bogiePositions = getBogiePositions();
				renderMirror(bottom_end, matrices, vertices, light, bogiePositions[0] + 42);
				renderMirror(bottom_end, matrices, vertices, light, bogiePositions[1] - 42);
				for (int i = bogiePositions[0] + 64; i <= bogiePositions[1] - 64; i += 30) {
					renderMirror(bottom_middle, matrices, vertices, light, i);
				}
				break;
		}
	}

	@Override
	protected void renderDoorPositions(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
		final boolean firstDoor = isIndex(0, position, getDoorPositions());
		final int doorOffset = (isEnd1Head && firstDoor ? 90 : 0) + (isEnd2Head && !firstDoor ? -90 : 0);
		final boolean doorOpen = doorLeftZ > 0 || doorRightZ > 0;

		switch (renderStage) {
			case LIGHTS:
				if (firstDoor && doorOpen) {
					renderMirror(door_light_on, matrices, vertices, light, 0);
				}
				break;
			case INTERIOR:
				if (firstDoor) {
					door_1.setOffset(0, 0, doorLeftZ);
					door_2.setOffset(0, 0, doorRightZ);
					renderOnceFlipped(door, matrices, vertices, light, position + doorOffset);
				} else {
					door_1.setOffset(0, 0, doorRightZ);
					door_2.setOffset(0, 0, doorLeftZ);
					renderOnce(door, matrices, vertices, light, position + doorOffset);
				}
				break;
			case EXTERIOR:
				if (isEnd1Head && firstDoor || isEnd2Head && !firstDoor) {
					if (firstDoor) {
						door_exterior_end_1.setOffset(0, 0, doorLeftZ);
						door_exterior_end_2.setOffset(0, 0, doorRightZ);
						renderOnceFlipped(door_exterior_end, matrices, vertices, light, position + doorOffset);
					} else {
						door_exterior_end_1.setOffset(0, 0, doorRightZ);
						door_exterior_end_2.setOffset(0, 0, doorLeftZ);
						renderOnce(door_exterior_end, matrices, vertices, light, position + doorOffset);
					}
				} else {
					if (firstDoor) {
						door_exterior_1.setOffset(0, 0, doorLeftZ);
						door_exterior_2.setOffset(0, 0, doorRightZ);
						renderOnceFlipped(door_exterior, matrices, vertices, light, position);
					} else {
						door_exterior_1.setOffset(0, 0, doorRightZ);
						door_exterior_2.setOffset(0, 0, doorLeftZ);
						renderOnce(door_exterior, matrices, vertices, light, position);
					}
				}
				if (firstDoor && !doorOpen) {
					renderMirror(door_light_off, matrices, vertices, light, 0);
				}
				break;
		}
	}

	@Override
	protected void renderHeadPosition1(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean useHeadlights) {
		switch (renderStage) {
			case LIGHTS:
				renderOnceFlipped(end_light, matrices, vertices, light, position + 90);
				break;
			case ALWAYS_ON_LIGHTS:
				renderOnceFlipped(useHeadlights ? headlights : tail_lights, matrices, vertices, light, position + 30);
				break;
			case INTERIOR:
				renderOnceFlipped(end, matrices, vertices, light, position + 90);
				break;
			case INTERIOR_TRANSLUCENT:
				renderOnceFlipped(end_translucent, matrices, vertices, light, position + 90);
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
			case LIGHTS:
				renderOnce(end_light, matrices, vertices, light, position - 90);
				break;
			case ALWAYS_ON_LIGHTS:
				renderOnce(useHeadlights ? headlights : tail_lights, matrices, vertices, light, position - 30);
				break;
			case INTERIOR:
				renderOnce(end, matrices, vertices, light, position - 90);
				break;
			case INTERIOR_TRANSLUCENT:
				renderOnce(end_translucent, matrices, vertices, light, position - 90);
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
				renderOnceFlipped(end_light, matrices, vertices, light, position);
				break;
			case INTERIOR:
				renderOnceFlipped(end, matrices, vertices, light, position);
				break;
			case INTERIOR_TRANSLUCENT:
				renderOnceFlipped(end_translucent, matrices, vertices, light, position);
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
				renderOnce(end_light, matrices, vertices, light, position);
				break;
			case INTERIOR:
				renderOnce(end, matrices, vertices, light, position);
				break;
			case INTERIOR_TRANSLUCENT:
				renderOnce(end_translucent, matrices, vertices, light, position);
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
		return new ModelMapper[]{window_end_exterior_6, window_end_exterior_5, window_end_exterior_4, window_end_exterior_3, window_end_exterior_2, window_end_exterior_1};
	}

	protected final ModelMapper[] windowPartsMini() {
		return new ModelMapper[]{window_exterior_1, window_exterior_2, window_exterior_3, window_exterior_7, window_exterior_8, window_exterior_9};
	}

	protected final ModelMapper[] windowEndPartsMini() {
		return new ModelMapper[]{window_end_exterior_3, window_end_exterior_2, window_end_exterior_1};
	}
}