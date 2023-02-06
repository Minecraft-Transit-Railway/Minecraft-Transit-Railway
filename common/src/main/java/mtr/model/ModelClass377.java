package mtr.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mtr.client.ClientData;
import mtr.client.DoorAnimationType;
import mtr.client.ScrollingText;
import mtr.data.Route;
import mtr.data.Station;
import mtr.mappings.ModelDataWrapper;
import mtr.mappings.ModelMapper;
import mtr.mappings.UtilitiesClient;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;

import java.util.List;

public class ModelClass377 extends ModelSimpleTrainBase<ModelClass377> {

	private final ModelMapper window;
	private final ModelMapper light_edge_r1;
	private final ModelMapper roof_1_r1;
	private final ModelMapper window_bottom_r1;
	private final ModelMapper window_light;
	private final ModelMapper light_r1;
	private final ModelMapper door_light;
	private final ModelMapper light_r2;
	private final ModelMapper window_1;
	private final ModelMapper window_top_r1;
	private final ModelMapper window_2;
	private final ModelMapper window_top_r2;
	private final ModelMapper window_3;
	private final ModelMapper window_top_r3;
	private final ModelMapper window_4;
	private final ModelMapper window_top_r4;
	private final ModelMapper window_5;
	private final ModelMapper window_top_r5;
	private final ModelMapper window_6;
	private final ModelMapper window_top_r6;
	private final ModelMapper window_7;
	private final ModelMapper window_top_r7;
	private final ModelMapper window_8;
	private final ModelMapper window_top_r8;
	private final ModelMapper window_exterior_1;
	private final ModelMapper window_exterior_1_1;
	private final ModelMapper window_bottom_r2;
	private final ModelMapper window_top_r9;
	private final ModelMapper window_exterior_1_2;
	private final ModelMapper window_bottom_r3;
	private final ModelMapper window_top_r10;
	private final ModelMapper window_exterior_2;
	private final ModelMapper window_exterior_1_3;
	private final ModelMapper window_bottom_r4;
	private final ModelMapper window_top_r11;
	private final ModelMapper window_exterior_1_4;
	private final ModelMapper window_bottom_r5;
	private final ModelMapper window_top_r12;
	private final ModelMapper window_exterior_3;
	private final ModelMapper window_exterior_1_5;
	private final ModelMapper window_bottom_r6;
	private final ModelMapper window_top_r13;
	private final ModelMapper window_exterior_1_6;
	private final ModelMapper window_bottom_r7;
	private final ModelMapper window_top_r14;
	private final ModelMapper window_exterior_4;
	private final ModelMapper window_exterior_1_7;
	private final ModelMapper window_bottom_r8;
	private final ModelMapper window_top_r15;
	private final ModelMapper window_exterior_1_8;
	private final ModelMapper window_bottom_r9;
	private final ModelMapper window_top_r16;
	private final ModelMapper window_exterior_5;
	private final ModelMapper window_exterior_1_9;
	private final ModelMapper window_bottom_r10;
	private final ModelMapper window_top_r17;
	private final ModelMapper window_exterior_1_10;
	private final ModelMapper window_bottom_r11;
	private final ModelMapper window_top_r18;
	private final ModelMapper window_exterior_6;
	private final ModelMapper window_exterior_1_11;
	private final ModelMapper window_bottom_r12;
	private final ModelMapper window_top_r19;
	private final ModelMapper window_exterior_1_12;
	private final ModelMapper window_bottom_r13;
	private final ModelMapper window_top_r20;
	private final ModelMapper window_exterior_7;
	private final ModelMapper window_exterior_1_13;
	private final ModelMapper window_bottom_r14;
	private final ModelMapper window_top_r21;
	private final ModelMapper window_exterior_1_14;
	private final ModelMapper window_bottom_r15;
	private final ModelMapper window_top_r22;
	private final ModelMapper window_exterior_8;
	private final ModelMapper window_exterior_1_15;
	private final ModelMapper window_bottom_r16;
	private final ModelMapper window_top_r23;
	private final ModelMapper window_exterior_1_16;
	private final ModelMapper window_bottom_r17;
	private final ModelMapper window_top_r24;
	private final ModelMapper window_end_exterior_1;
	private final ModelMapper window_end_exterior_1_1;
	private final ModelMapper window_bottom_r18;
	private final ModelMapper window_top_r25;
	private final ModelMapper window_end_exterior_1_2;
	private final ModelMapper window_bottom_r19;
	private final ModelMapper window_top_r26;
	private final ModelMapper window_end_exterior_2;
	private final ModelMapper window_end_exterior_1_3;
	private final ModelMapper window_bottom_r20;
	private final ModelMapper window_top_r27;
	private final ModelMapper window_end_exterior_1_4;
	private final ModelMapper window_bottom_r21;
	private final ModelMapper window_top_r28;
	private final ModelMapper window_end_exterior_3;
	private final ModelMapper window_end_exterior_1_5;
	private final ModelMapper window_bottom_r22;
	private final ModelMapper window_top_r29;
	private final ModelMapper window_end_exterior_1_6;
	private final ModelMapper window_bottom_r23;
	private final ModelMapper window_top_r30;
	private final ModelMapper window_end_exterior_4;
	private final ModelMapper window_end_exterior_1_7;
	private final ModelMapper window_bottom_r24;
	private final ModelMapper window_top_r31;
	private final ModelMapper window_end_exterior_1_8;
	private final ModelMapper window_bottom_r25;
	private final ModelMapper window_top_r32;
	private final ModelMapper roof_exterior_1;
	private final ModelMapper roof_3_r1;
	private final ModelMapper roof_2_r1;
	private final ModelMapper roof_1_r2;
	private final ModelMapper roof_exterior_door_edge;
	private final ModelMapper roof_exterior_door_edge_1;
	private final ModelMapper roof_4_r1;
	private final ModelMapper roof_3_r2;
	private final ModelMapper roof_2_r2;
	private final ModelMapper roof_exterior_door_edge_2;
	private final ModelMapper roof_5_r1;
	private final ModelMapper roof_4_r2;
	private final ModelMapper roof_3_r3;
	private final ModelMapper roof_exterior_door;
	private final ModelMapper roof_exterior_door_1;
	private final ModelMapper roof_5_r2;
	private final ModelMapper roof_4_r3;
	private final ModelMapper roof_3_r4;
	private final ModelMapper roof_exterior_door_2;
	private final ModelMapper roof_6_r1;
	private final ModelMapper roof_5_r3;
	private final ModelMapper roof_4_r4;
	private final ModelMapper door;
	private final ModelMapper light_edge_r2;
	private final ModelMapper door_top_3_r1;
	private final ModelMapper door_top_2_r1;
	private final ModelMapper window_top_2_r1;
	private final ModelMapper window_bottom_2_r1;
	private final ModelMapper door_left;
	private final ModelMapper door_top_r1;
	private final ModelMapper door_bottom_r1;
	private final ModelMapper door_right;
	private final ModelMapper door_top_r2;
	private final ModelMapper door_bottom_r2;
	private final ModelMapper door_exterior;
	private final ModelMapper window_top_2_r2;
	private final ModelMapper window_bottom_2_r2;
	private final ModelMapper door_left_exterior;
	private final ModelMapper door_top_r3;
	private final ModelMapper door_bottom_r3;
	private final ModelMapper door_right_exterior;
	private final ModelMapper door_top_r4;
	private final ModelMapper door_bottom_r4;
	private final ModelMapper door_exterior_end;
	private final ModelMapper door_exterior_end_1;
	private final ModelMapper window_top_2_r3;
	private final ModelMapper window_bottom_2_r3;
	private final ModelMapper door_left_exterior_end_1;
	private final ModelMapper door_top_r5;
	private final ModelMapper door_bottom_r5;
	private final ModelMapper door_right_exterior_end_1;
	private final ModelMapper door_top_r6;
	private final ModelMapper door_bottom_r6;
	private final ModelMapper door_exterior_end_2;
	private final ModelMapper window_top_2_r4;
	private final ModelMapper window_bottom_2_r4;
	private final ModelMapper door_left_exterior_end_2;
	private final ModelMapper door_top_r7;
	private final ModelMapper door_bottom_r7;
	private final ModelMapper door_right_exterior_end_2;
	private final ModelMapper door_top_r8;
	private final ModelMapper door_bottom_r8;
	private final ModelMapper end;
	private final ModelMapper end_1;
	private final ModelMapper end_2;
	private final ModelMapper end_exterior;
	private final ModelMapper end_exterior_1;
	private final ModelMapper window_bottom_r26;
	private final ModelMapper window_top_r33;
	private final ModelMapper end_exterior_2;
	private final ModelMapper window_bottom_r27;
	private final ModelMapper window_top_r34;
	private final ModelMapper roof_end_exterior_1;
	private final ModelMapper roof_3_r5;
	private final ModelMapper roof_2_r3;
	private final ModelMapper roof_1_r3;
	private final ModelMapper roof_end_exterior_2;
	private final ModelMapper roof_4_r5;
	private final ModelMapper roof_3_r6;
	private final ModelMapper roof_2_r4;
	private final ModelMapper head;
	private final ModelMapper head_exterior;
	private final ModelMapper front_r1;
	private final ModelMapper head_exterior_1;
	private final ModelMapper front_bottom_r1;
	private final ModelMapper roof_5_r4;
	private final ModelMapper roof_4_r6;
	private final ModelMapper roof_3_r7;
	private final ModelMapper roof_2_r5;
	private final ModelMapper window_bottom_r28;
	private final ModelMapper window_top_r35;
	private final ModelMapper head_exterior_2;
	private final ModelMapper front_bottom_r2;
	private final ModelMapper roof_6_r2;
	private final ModelMapper roof_5_r5;
	private final ModelMapper roof_4_r7;
	private final ModelMapper roof_3_r8;
	private final ModelMapper window_bottom_r29;
	private final ModelMapper window_top_r36;
	private final ModelMapper seat;
	private final ModelMapper seat_2_r1;
	private final ModelMapper door_light_box;
	private final ModelMapper door_light_r1;
	private final ModelMapper door_light_off;
	private final ModelMapper door_light_r2;
	private final ModelMapper door_light_on;
	private final ModelMapper door_light_r3;
	private final ModelMapper headlights;
	private final ModelMapper headlight_2_r1;
	private final ModelMapper tail_lights;
	private final ModelMapper tail_light_2_r1;

	public ModelClass377() {
		this(DoorAnimationType.PLUG_FAST, true);
	}

	private ModelClass377(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		super(doorAnimationType, renderDoorOverlay);
		final int textureWidth = 368;
		final int textureHeight = 368;

		final ModelDataWrapper modelDataWrapper = new ModelDataWrapper(this, textureWidth, textureHeight);

		window = new ModelMapper(modelDataWrapper);
		window.setPos(0, 24, 0);
		window.texOffs(0, 78).addBox(-19, 0, -12.5F, 19, 1, 25, 0, false);
		window.texOffs(208, 221).addBox(-20, -13, -12.5F, 0, 6, 25, 0, false);
		window.texOffs(133, 277).addBox(-18, -31, -12.5F, 2, 4, 25, 0, false);
		window.texOffs(208, 91).addBox(-18, -32, -12.5F, 6, 1, 25, 0, false);
		window.texOffs(183, 117).addBox(-5, -35, -12.5F, 5, 0, 25, 0, false);

		light_edge_r1 = new ModelMapper(modelDataWrapper);
		light_edge_r1.setPos(-5, -35, 0);
		window.addChild(light_edge_r1);
		setRotationAngle(light_edge_r1, 0, 0, 0.2182F);
		light_edge_r1.texOffs(292, 120).addBox(-5, 0, -12.5F, 1, 1, 25, 0, false);
		light_edge_r1.texOffs(38, 78).addBox(-9, 0, -12.5F, 9, 0, 25, 0, false);

		roof_1_r1 = new ModelMapper(modelDataWrapper);
		roof_1_r1.setPos(-18, -32, 0);
		window.addChild(roof_1_r1);
		setRotationAngle(roof_1_r1, 0, 0, -0.7854F);
		roof_1_r1.texOffs(181, 173).addBox(0, 0, -12.5F, 7, 0, 25, 0, false);

		window_bottom_r1 = new ModelMapper(modelDataWrapper);
		window_bottom_r1.setPos(-21, -7, 0);
		window.addChild(window_bottom_r1);
		setRotationAngle(window_bottom_r1, 0, 0, -0.2094F);
		window_bottom_r1.texOffs(208, 206).addBox(1, 0, -12.5F, 0, 8, 25, 0, false);

		window_light = new ModelMapper(modelDataWrapper);
		window_light.setPos(0, 24, 0);
		window_light.texOffs(183, 142).addBox(-4, -35.2F, -12.5F, 4, 0, 25, 0, false);

		light_r1 = new ModelMapper(modelDataWrapper);
		light_r1.setPos(-5, -35, 0);
		window_light.addChild(light_r1);
		setRotationAngle(light_r1, 0, 0, 0.2182F);
		light_r1.texOffs(287, 238).addBox(-5.5F, -0.2F, -12.5F, 2, 1, 25, 0, false);

		door_light = new ModelMapper(modelDataWrapper);
		door_light.setPos(0, 24, 0);
		door_light.texOffs(172, 116).addBox(-4, -35.2F, -14, 4, 0, 28, 0, false);

		light_r2 = new ModelMapper(modelDataWrapper);
		light_r2.setPos(-5, -35, 0);
		door_light.addChild(light_r2);
		setRotationAngle(light_r2, 0, 0, 0.2182F);
		light_r2.texOffs(206, 173).addBox(-5.5F, -0.2F, -14, 2, 1, 28, 0, false);

		window_1 = new ModelMapper(modelDataWrapper);
		window_1.setPos(0, 24, 0);


		window_top_r1 = new ModelMapper(modelDataWrapper);
		window_top_r1.setPos(-21, -13, 0);
		window_1.addChild(window_top_r1);
		setRotationAngle(window_top_r1, 0, 0, 0.1396F);
		window_top_r1.texOffs(100, 107).addBox(1, -21, -12.5F, 0, 21, 25, 0, false);

		window_2 = new ModelMapper(modelDataWrapper);
		window_2.setPos(0, 24, 0);


		window_top_r2 = new ModelMapper(modelDataWrapper);
		window_top_r2.setPos(-21, -13, 0);
		window_2.addChild(window_top_r2);
		setRotationAngle(window_top_r2, 0, 0, 0.1396F);
		window_top_r2.texOffs(50, 107).addBox(1, -21, -12.5F, 0, 21, 25, 0, false);

		window_3 = new ModelMapper(modelDataWrapper);
		window_3.setPos(0, 24, 0);


		window_top_r3 = new ModelMapper(modelDataWrapper);
		window_top_r3.setPos(-21, -13, 0);
		window_3.addChild(window_top_r3);
		setRotationAngle(window_top_r3, 0, 0, 0.1396F);
		window_top_r3.texOffs(100, 86).addBox(1, -21, -12.5F, 0, 21, 25, 0, false);

		window_4 = new ModelMapper(modelDataWrapper);
		window_4.setPos(0, 24, 0);


		window_top_r4 = new ModelMapper(modelDataWrapper);
		window_top_r4.setPos(-21, -13, 0);
		window_4.addChild(window_top_r4);
		setRotationAngle(window_top_r4, 0, 0, 0.1396F);
		window_top_r4.texOffs(0, 100).addBox(1, -21, -12.5F, 0, 21, 25, 0, false);

		window_5 = new ModelMapper(modelDataWrapper);
		window_5.setPos(0, 24, 0);


		window_top_r5 = new ModelMapper(modelDataWrapper);
		window_top_r5.setPos(-21, -13, 0);
		window_5.addChild(window_top_r5);
		setRotationAngle(window_top_r5, 0, 0, 0.1396F);
		window_top_r5.texOffs(98, 53).addBox(1, -21, -12.5F, 0, 21, 25, 0, false);

		window_6 = new ModelMapper(modelDataWrapper);
		window_6.setPos(0, 24, 0);


		window_top_r6 = new ModelMapper(modelDataWrapper);
		window_top_r6.setPos(-21, -13, 0);
		window_6.addChild(window_top_r6);
		setRotationAngle(window_top_r6, 0, 0, 0.1396F);
		window_top_r6.texOffs(88, 3).addBox(1, -21, -12.5F, 0, 21, 25, 0, false);

		window_7 = new ModelMapper(modelDataWrapper);
		window_7.setPos(0, 24, 0);


		window_top_r7 = new ModelMapper(modelDataWrapper);
		window_top_r7.setPos(-21, -13, 0);
		window_7.addChild(window_top_r7);
		setRotationAngle(window_top_r7, 0, 0, 0.1396F);
		window_top_r7.texOffs(50, 86).addBox(1, -21, -12.5F, 0, 21, 25, 0, false);

		window_8 = new ModelMapper(modelDataWrapper);
		window_8.setPos(0, 24, 0);


		window_top_r8 = new ModelMapper(modelDataWrapper);
		window_top_r8.setPos(-21, -13, 0);
		window_8.addChild(window_top_r8);
		setRotationAngle(window_top_r8, 0, 0, 0.1396F);
		window_top_r8.texOffs(0, 79).addBox(1, -21, -12.5F, 0, 21, 25, 0, false);

		window_exterior_1 = new ModelMapper(modelDataWrapper);
		window_exterior_1.setPos(0, 24, 0);


		window_exterior_1_1 = new ModelMapper(modelDataWrapper);
		window_exterior_1_1.setPos(0, 0, 0);
		window_exterior_1.addChild(window_exterior_1_1);
		window_exterior_1_1.texOffs(286, 89).addBox(-21, -13, -12.5F, 0, 6, 25, 0, false);

		window_bottom_r2 = new ModelMapper(modelDataWrapper);
		window_bottom_r2.setPos(-21, -7, 0);
		window_exterior_1_1.addChild(window_bottom_r2);
		setRotationAngle(window_bottom_r2, 0, 0, -0.2094F);
		window_bottom_r2.texOffs(272, 33).addBox(0, 0, -12.5F, 1, 8, 25, 0, false);

		window_top_r9 = new ModelMapper(modelDataWrapper);
		window_top_r9.setPos(-21, -13, 0);
		window_exterior_1_1.addChild(window_top_r9);
		setRotationAngle(window_top_r9, 0, 0, 0.1396F);
		window_top_r9.texOffs(198, 42).addBox(0, -21, -12.5F, 0, 21, 25, 0, false);

		window_exterior_1_2 = new ModelMapper(modelDataWrapper);
		window_exterior_1_2.setPos(0, 0, 0);
		window_exterior_1.addChild(window_exterior_1_2);
		window_exterior_1_2.texOffs(286, 83).addBox(21, -13, -12.5F, 0, 6, 25, 0, true);

		window_bottom_r3 = new ModelMapper(modelDataWrapper);
		window_bottom_r3.setPos(21, -7, 0);
		window_exterior_1_2.addChild(window_bottom_r3);
		setRotationAngle(window_bottom_r3, 0, 0, 0.2094F);
		window_bottom_r3.texOffs(52, 270).addBox(-1, 0, -12.5F, 1, 8, 25, 0, true);

		window_top_r10 = new ModelMapper(modelDataWrapper);
		window_top_r10.setPos(21, -13, 0);
		window_exterior_1_2.addChild(window_top_r10);
		setRotationAngle(window_top_r10, 0, 0, -0.1396F);
		window_top_r10.texOffs(100, 191).addBox(0, -21, -12.5F, 0, 21, 25, 0, true);

		window_exterior_2 = new ModelMapper(modelDataWrapper);
		window_exterior_2.setPos(0, 24, 0);


		window_exterior_1_3 = new ModelMapper(modelDataWrapper);
		window_exterior_1_3.setPos(0, 0, 0);
		window_exterior_2.addChild(window_exterior_1_3);
		window_exterior_1_3.texOffs(38, 278).addBox(-21, -13, -12.5F, 0, 6, 25, 0, false);

		window_bottom_r4 = new ModelMapper(modelDataWrapper);
		window_bottom_r4.setPos(-21, -7, 0);
		window_exterior_1_3.addChild(window_bottom_r4);
		setRotationAngle(window_bottom_r4, 0, 0, -0.2094F);
		window_bottom_r4.texOffs(259, 96).addBox(0, 0, -12.5F, 1, 8, 25, 0, false);

		window_top_r11 = new ModelMapper(modelDataWrapper);
		window_top_r11.setPos(-21, -13, 0);
		window_exterior_1_3.addChild(window_top_r11);
		setRotationAngle(window_top_r11, 0, 0, 0.1396F);
		window_top_r11.texOffs(150, 158).addBox(0, -21, -12.5F, 0, 21, 25, 0, false);

		window_exterior_1_4 = new ModelMapper(modelDataWrapper);
		window_exterior_1_4.setPos(0, 0, 0);
		window_exterior_2.addChild(window_exterior_1_4);
		window_exterior_1_4.texOffs(187, 272).addBox(21, -13, -12.5F, 0, 6, 25, 0, true);

		window_bottom_r5 = new ModelMapper(modelDataWrapper);
		window_bottom_r5.setPos(21, -7, 0);
		window_exterior_1_4.addChild(window_bottom_r5);
		setRotationAngle(window_bottom_r5, 0, 0, 0.2094F);
		window_bottom_r5.texOffs(253, 0).addBox(-1, 0, -12.5F, 1, 8, 25, 0, true);

		window_top_r12 = new ModelMapper(modelDataWrapper);
		window_top_r12.setPos(21, -13, 0);
		window_exterior_1_4.addChild(window_top_r12);
		setRotationAngle(window_top_r12, 0, 0, -0.1396F);
		window_top_r12.texOffs(150, 137).addBox(0, -21, -12.5F, 0, 21, 25, 0, true);

		window_exterior_3 = new ModelMapper(modelDataWrapper);
		window_exterior_3.setPos(0, 24, 0);


		window_exterior_1_5 = new ModelMapper(modelDataWrapper);
		window_exterior_1_5.setPos(0, 0, 0);
		window_exterior_3.addChild(window_exterior_1_5);
		window_exterior_1_5.texOffs(272, 53).addBox(-21, -13, -12.5F, 0, 6, 25, 0, false);

		window_bottom_r6 = new ModelMapper(modelDataWrapper);
		window_bottom_r6.setPos(-21, -7, 0);
		window_exterior_1_5.addChild(window_bottom_r6);
		setRotationAngle(window_bottom_r6, 0, 0, -0.2094F);
		window_bottom_r6.texOffs(79, 245).addBox(0, 0, -12.5F, 1, 8, 25, 0, false);

		window_top_r13 = new ModelMapper(modelDataWrapper);
		window_top_r13.setPos(-21, -13, 0);
		window_exterior_1_5.addChild(window_top_r13);
		setRotationAngle(window_top_r13, 0, 0, 0.1396F);
		window_top_r13.texOffs(150, 116).addBox(0, -21, -12.5F, 0, 21, 25, 0, false);

		window_exterior_1_6 = new ModelMapper(modelDataWrapper);
		window_exterior_1_6.setPos(0, 0, 0);
		window_exterior_3.addChild(window_exterior_1_6);
		window_exterior_1_6.texOffs(272, 47).addBox(21, -13, -12.5F, 0, 6, 25, 0, true);

		window_bottom_r7 = new ModelMapper(modelDataWrapper);
		window_bottom_r7.setPos(21, -7, 0);
		window_exterior_1_6.addChild(window_bottom_r7);
		setRotationAngle(window_bottom_r7, 0, 0, 0.2094F);
		window_bottom_r7.texOffs(245, 63).addBox(-1, 0, -12.5F, 1, 8, 25, 0, true);

		window_top_r14 = new ModelMapper(modelDataWrapper);
		window_top_r14.setPos(21, -13, 0);
		window_exterior_1_6.addChild(window_top_r14);
		setRotationAngle(window_top_r14, 0, 0, -0.1396F);
		window_top_r14.texOffs(150, 95).addBox(0, -21, -12.5F, 0, 21, 25, 0, true);

		window_exterior_4 = new ModelMapper(modelDataWrapper);
		window_exterior_4.setPos(0, 24, 0);


		window_exterior_1_7 = new ModelMapper(modelDataWrapper);
		window_exterior_1_7.setPos(0, 0, 0);
		window_exterior_4.addChild(window_exterior_1_7);
		window_exterior_1_7.texOffs(272, 41).addBox(-21, -13, -12.5F, 0, 6, 25, 0, false);

		window_bottom_r8 = new ModelMapper(modelDataWrapper);
		window_bottom_r8.setPos(-21, -7, 0);
		window_exterior_1_7.addChild(window_bottom_r8);
		setRotationAngle(window_bottom_r8, 0, 0, -0.2094F);
		window_bottom_r8.texOffs(152, 244).addBox(0, 0, -12.5F, 1, 8, 25, 0, false);

		window_top_r15 = new ModelMapper(modelDataWrapper);
		window_top_r15.setPos(-21, -13, 0);
		window_exterior_1_7.addChild(window_top_r15);
		setRotationAngle(window_top_r15, 0, 0, 0.1396F);
		window_top_r15.texOffs(150, 74).addBox(0, -21, -12.5F, 0, 21, 25, 0, false);

		window_exterior_1_8 = new ModelMapper(modelDataWrapper);
		window_exterior_1_8.setPos(0, 0, 0);
		window_exterior_4.addChild(window_exterior_1_8);
		window_exterior_1_8.texOffs(79, 259).addBox(21, -13, -12.5F, 0, 6, 25, 0, true);

		window_bottom_r9 = new ModelMapper(modelDataWrapper);
		window_bottom_r9.setPos(21, -7, 0);
		window_exterior_1_8.addChild(window_bottom_r9);
		setRotationAngle(window_bottom_r9, 0, 0, 0.2094F);
		window_bottom_r9.texOffs(241, 183).addBox(-1, 0, -12.5F, 1, 8, 25, 0, true);

		window_top_r16 = new ModelMapper(modelDataWrapper);
		window_top_r16.setPos(21, -13, 0);
		window_exterior_1_8.addChild(window_top_r16);
		setRotationAngle(window_top_r16, 0, 0, -0.1396F);
		window_top_r16.texOffs(100, 149).addBox(0, -21, -12.5F, 0, 21, 25, 0, true);

		window_exterior_5 = new ModelMapper(modelDataWrapper);
		window_exterior_5.setPos(0, 24, 0);


		window_exterior_1_9 = new ModelMapper(modelDataWrapper);
		window_exterior_1_9.setPos(0, 0, 0);
		window_exterior_5.addChild(window_exterior_1_9);
		window_exterior_1_9.texOffs(206, 257).addBox(-21, -13, -12.5F, 0, 6, 25, 0, false);

		window_bottom_r10 = new ModelMapper(modelDataWrapper);
		window_bottom_r10.setPos(-21, -7, 0);
		window_exterior_1_9.addChild(window_bottom_r10);
		setRotationAngle(window_bottom_r10, 0, 0, -0.2094F);
		window_bottom_r10.texOffs(238, 150).addBox(0, 0, -12.5F, 1, 8, 25, 0, false);

		window_top_r17 = new ModelMapper(modelDataWrapper);
		window_top_r17.setPos(-21, -13, 0);
		window_exterior_1_9.addChild(window_top_r17);
		setRotationAngle(window_top_r17, 0, 0, 0.1396F);
		window_top_r17.texOffs(50, 149).addBox(0, -21, -12.5F, 0, 21, 25, 0, false);

		window_exterior_1_10 = new ModelMapper(modelDataWrapper);
		window_exterior_1_10.setPos(0, 0, 0);
		window_exterior_5.addChild(window_exterior_1_10);
		window_exterior_1_10.texOffs(27, 257).addBox(21, -13, -12.5F, 0, 6, 25, 0, true);

		window_bottom_r11 = new ModelMapper(modelDataWrapper);
		window_bottom_r11.setPos(21, -7, 0);
		window_exterior_1_10.addChild(window_bottom_r11);
		setRotationAngle(window_bottom_r11, 0, 0, 0.2094F);
		window_bottom_r11.texOffs(52, 237).addBox(-1, 0, -12.5F, 1, 8, 25, 0, true);

		window_top_r18 = new ModelMapper(modelDataWrapper);
		window_top_r18.setPos(21, -13, 0);
		window_exterior_1_10.addChild(window_top_r18);
		setRotationAngle(window_top_r18, 0, 0, -0.1396F);
		window_top_r18.texOffs(148, 53).addBox(0, -21, -12.5F, 0, 21, 25, 0, true);

		window_exterior_6 = new ModelMapper(modelDataWrapper);
		window_exterior_6.setPos(0, 24, 0);


		window_exterior_1_11 = new ModelMapper(modelDataWrapper);
		window_exterior_1_11.setPos(0, 0, 0);
		window_exterior_6.addChild(window_exterior_1_11);
		window_exterior_1_11.texOffs(79, 253).addBox(-21, -13, -12.5F, 0, 6, 25, 0, false);

		window_bottom_r12 = new ModelMapper(modelDataWrapper);
		window_bottom_r12.setPos(-21, -7, 0);
		window_exterior_1_11.addChild(window_bottom_r12);
		setRotationAngle(window_bottom_r12, 0, 0, -0.2094F);
		window_bottom_r12.texOffs(125, 236).addBox(0, 0, -12.5F, 1, 8, 25, 0, false);

		window_top_r19 = new ModelMapper(modelDataWrapper);
		window_top_r19.setPos(-21, -13, 0);
		window_exterior_1_11.addChild(window_top_r19);
		setRotationAngle(window_top_r19, 0, 0, 0.1396F);
		window_top_r19.texOffs(0, 142).addBox(0, -21, -12.5F, 0, 21, 25, 0, false);

		window_exterior_1_12 = new ModelMapper(modelDataWrapper);
		window_exterior_1_12.setPos(0, 0, 0);
		window_exterior_6.addChild(window_exterior_1_12);
		window_exterior_1_12.texOffs(206, 251).addBox(21, -13, -12.5F, 0, 6, 25, 0, true);

		window_bottom_r13 = new ModelMapper(modelDataWrapper);
		window_bottom_r13.setPos(21, -7, 0);
		window_exterior_1_12.addChild(window_bottom_r13);
		setRotationAngle(window_bottom_r13, 0, 0, 0.2094F);
		window_bottom_r13.texOffs(233, 231).addBox(-1, 0, -12.5F, 1, 8, 25, 0, true);

		window_top_r20 = new ModelMapper(modelDataWrapper);
		window_top_r20.setPos(21, -13, 0);
		window_exterior_1_12.addChild(window_top_r20);
		setRotationAngle(window_top_r20, 0, 0, -0.1396F);
		window_top_r20.texOffs(138, 0).addBox(0, -21, -12.5F, 0, 21, 25, 0, true);

		window_exterior_7 = new ModelMapper(modelDataWrapper);
		window_exterior_7.setPos(0, 24, 0);


		window_exterior_1_13 = new ModelMapper(modelDataWrapper);
		window_exterior_1_13.setPos(0, 0, 0);
		window_exterior_7.addChild(window_exterior_1_13);
		window_exterior_1_13.texOffs(27, 251).addBox(-21, -13, -12.5F, 0, 6, 25, 0, false);

		window_bottom_r14 = new ModelMapper(modelDataWrapper);
		window_bottom_r14.setPos(-21, -7, 0);
		window_exterior_1_13.addChild(window_bottom_r14);
		setRotationAngle(window_bottom_r14, 0, 0, -0.2094F);
		window_bottom_r14.texOffs(232, 117).addBox(0, 0, -12.5F, 1, 8, 25, 0, false);

		window_top_r21 = new ModelMapper(modelDataWrapper);
		window_top_r21.setPos(-21, -13, 0);
		window_exterior_1_13.addChild(window_top_r21);
		setRotationAngle(window_top_r21, 0, 0, 0.1396F);
		window_top_r21.texOffs(100, 128).addBox(0, -21, -12.5F, 0, 21, 25, 0, false);

		window_exterior_1_14 = new ModelMapper(modelDataWrapper);
		window_exterior_1_14.setPos(0, 0, 0);
		window_exterior_7.addChild(window_exterior_1_14);
		window_exterior_1_14.texOffs(206, 245).addBox(21, -13, -12.5F, 0, 6, 25, 0, true);

		window_bottom_r15 = new ModelMapper(modelDataWrapper);
		window_bottom_r15.setPos(21, -7, 0);
		window_exterior_1_14.addChild(window_bottom_r15);
		setRotationAngle(window_bottom_r15, 0, 0, 0.2094F);
		window_bottom_r15.texOffs(181, 231).addBox(-1, 0, -12.5F, 1, 8, 25, 0, true);

		window_top_r22 = new ModelMapper(modelDataWrapper);
		window_top_r22.setPos(21, -13, 0);
		window_exterior_1_14.addChild(window_top_r22);
		setRotationAngle(window_top_r22, 0, 0, -0.1396F);
		window_top_r22.texOffs(50, 128).addBox(0, -21, -12.5F, 0, 21, 25, 0, true);

		window_exterior_8 = new ModelMapper(modelDataWrapper);
		window_exterior_8.setPos(0, 24, 0);


		window_exterior_1_15 = new ModelMapper(modelDataWrapper);
		window_exterior_1_15.setPos(0, 0, 0);
		window_exterior_8.addChild(window_exterior_1_15);
		window_exterior_1_15.texOffs(27, 245).addBox(-21, -13, -12.5F, 0, 6, 25, 0, false);

		window_bottom_r16 = new ModelMapper(modelDataWrapper);
		window_bottom_r16.setPos(-21, -7, 0);
		window_exterior_1_15.addChild(window_bottom_r16);
		setRotationAngle(window_bottom_r16, 0, 0, -0.2094F);
		window_bottom_r16.texOffs(0, 230).addBox(0, 0, -12.5F, 1, 8, 25, 0, false);

		window_top_r23 = new ModelMapper(modelDataWrapper);
		window_top_r23.setPos(-21, -13, 0);
		window_exterior_1_15.addChild(window_top_r23);
		setRotationAngle(window_top_r23, 0, 0, 0.1396F);
		window_top_r23.texOffs(126, 24).addBox(0, -21, -12.5F, 0, 21, 25, 0, false);

		window_exterior_1_16 = new ModelMapper(modelDataWrapper);
		window_exterior_1_16.setPos(0, 0, 0);
		window_exterior_8.addChild(window_exterior_1_16);
		window_exterior_1_16.texOffs(206, 239).addBox(21, -13, -12.5F, 0, 6, 25, 0, true);

		window_bottom_r17 = new ModelMapper(modelDataWrapper);
		window_bottom_r17.setPos(21, -7, 0);
		window_exterior_1_16.addChild(window_bottom_r17);
		setRotationAngle(window_bottom_r17, 0, 0, 0.2094F);
		window_bottom_r17.texOffs(226, 21).addBox(-1, 0, -12.5F, 1, 8, 25, 0, true);

		window_top_r24 = new ModelMapper(modelDataWrapper);
		window_top_r24.setPos(21, -13, 0);
		window_exterior_1_16.addChild(window_top_r24);
		setRotationAngle(window_top_r24, 0, 0, -0.1396F);
		window_top_r24.texOffs(0, 121).addBox(0, -21, -12.5F, 0, 21, 25, 0, true);

		window_end_exterior_1 = new ModelMapper(modelDataWrapper);
		window_end_exterior_1.setPos(0, 24, 0);


		window_end_exterior_1_1 = new ModelMapper(modelDataWrapper);
		window_end_exterior_1_1.setPos(0, 0, 0);
		window_end_exterior_1.addChild(window_end_exterior_1_1);
		window_end_exterior_1_1.texOffs(286, 77).addBox(-21, -13, -12.5F, 0, 6, 25, 0, false);

		window_bottom_r18 = new ModelMapper(modelDataWrapper);
		window_bottom_r18.setPos(-21, -7, 0);
		window_end_exterior_1_1.addChild(window_bottom_r18);
		setRotationAngle(window_bottom_r18, 0, 0, -0.2094F);
		window_bottom_r18.texOffs(106, 269).addBox(0, 0, -12.5F, 1, 8, 25, 0, false);

		window_top_r25 = new ModelMapper(modelDataWrapper);
		window_top_r25.setPos(-21, -13, 0);
		window_end_exterior_1_1.addChild(window_top_r25);
		setRotationAngle(window_top_r25, 0, 0, 0.1396F);
		window_top_r25.texOffs(50, 191).addBox(0, -21, -12.5F, 0, 21, 25, 0, false);

		window_end_exterior_1_2 = new ModelMapper(modelDataWrapper);
		window_end_exterior_1_2.setPos(0, 0, 0);
		window_end_exterior_1.addChild(window_end_exterior_1_2);
		window_end_exterior_1_2.texOffs(286, 71).addBox(21, -13, -12.5F, 0, 6, 25, 0, true);

		window_bottom_r19 = new ModelMapper(modelDataWrapper);
		window_bottom_r19.setPos(21, -7, 0);
		window_end_exterior_1_2.addChild(window_bottom_r19);
		setRotationAngle(window_bottom_r19, 0, 0, 0.2094F);
		window_bottom_r19.texOffs(268, 162).addBox(-1, 0, -12.5F, 1, 8, 25, 0, true);

		window_top_r26 = new ModelMapper(modelDataWrapper);
		window_top_r26.setPos(21, -13, 0);
		window_end_exterior_1_2.addChild(window_top_r26);
		setRotationAngle(window_top_r26, 0, 0, -0.1396F);
		window_top_r26.texOffs(188, 0).addBox(0, -21, -12.5F, 0, 21, 25, 0, true);

		window_end_exterior_2 = new ModelMapper(modelDataWrapper);
		window_end_exterior_2.setPos(0, 24, 0);


		window_end_exterior_1_3 = new ModelMapper(modelDataWrapper);
		window_end_exterior_1_3.setPos(0, 0, 0);
		window_end_exterior_2.addChild(window_end_exterior_1_3);
		window_end_exterior_1_3.texOffs(88, 285).addBox(-21, -13, -12.5F, 0, 6, 25, 0, false);

		window_bottom_r20 = new ModelMapper(modelDataWrapper);
		window_bottom_r20.setPos(-21, -7, 0);
		window_end_exterior_1_3.addChild(window_bottom_r20);
		setRotationAngle(window_bottom_r20, 0, 0, -0.2094F);
		window_bottom_r20.texOffs(265, 129).addBox(0, 0, -12.5F, 1, 8, 25, 0, false);

		window_top_r27 = new ModelMapper(modelDataWrapper);
		window_top_r27.setPos(-21, -13, 0);
		window_end_exterior_1_3.addChild(window_top_r27);
		setRotationAngle(window_top_r27, 0, 0, 0.1396F);
		window_top_r27.texOffs(0, 184).addBox(0, -21, -12.5F, 0, 21, 25, 0, false);

		window_end_exterior_1_4 = new ModelMapper(modelDataWrapper);
		window_end_exterior_1_4.setPos(0, 0, 0);
		window_end_exterior_2.addChild(window_end_exterior_1_4);
		window_end_exterior_1_4.texOffs(188, 284).addBox(21, -13, -12.5F, 0, 6, 25, 0, true);

		window_bottom_r21 = new ModelMapper(modelDataWrapper);
		window_bottom_r21.setPos(21, -7, 0);
		window_end_exterior_1_4.addChild(window_bottom_r21);
		setRotationAngle(window_bottom_r21, 0, 0, 0.2094F);
		window_bottom_r21.texOffs(231, 264).addBox(-1, 0, -12.5F, 1, 8, 25, 0, true);

		window_top_r28 = new ModelMapper(modelDataWrapper);
		window_top_r28.setPos(21, -13, 0);
		window_end_exterior_1_4.addChild(window_top_r28);
		setRotationAngle(window_top_r28, 0, 0, -0.1396F);
		window_top_r28.texOffs(150, 179).addBox(0, -21, -12.5F, 0, 21, 25, 0, true);

		window_end_exterior_3 = new ModelMapper(modelDataWrapper);
		window_end_exterior_3.setPos(0, 24, 0);


		window_end_exterior_1_5 = new ModelMapper(modelDataWrapper);
		window_end_exterior_1_5.setPos(0, 0, 0);
		window_end_exterior_3.addChild(window_end_exterior_1_5);
		window_end_exterior_1_5.texOffs(138, 284).addBox(-21, -13, -12.5F, 0, 6, 25, 0, false);

		window_bottom_r22 = new ModelMapper(modelDataWrapper);
		window_bottom_r22.setPos(-21, -7, 0);
		window_end_exterior_1_5.addChild(window_bottom_r22);
		setRotationAngle(window_bottom_r22, 0, 0, -0.2094F);
		window_bottom_r22.texOffs(179, 264).addBox(0, 0, -12.5F, 1, 8, 25, 0, false);

		window_top_r29 = new ModelMapper(modelDataWrapper);
		window_top_r29.setPos(-21, -13, 0);
		window_end_exterior_1_5.addChild(window_top_r29);
		setRotationAngle(window_top_r29, 0, 0, 0.1396F);
		window_top_r29.texOffs(176, 21).addBox(0, -21, -12.5F, 0, 21, 25, 0, false);

		window_end_exterior_1_6 = new ModelMapper(modelDataWrapper);
		window_end_exterior_1_6.setPos(0, 0, 0);
		window_end_exterior_3.addChild(window_end_exterior_1_6);
		window_end_exterior_1_6.texOffs(38, 284).addBox(21, -13, -12.5F, 0, 6, 25, 0, true);

		window_bottom_r23 = new ModelMapper(modelDataWrapper);
		window_bottom_r23.setPos(21, -7, 0);
		window_end_exterior_1_6.addChild(window_bottom_r23);
		setRotationAngle(window_bottom_r23, 0, 0, 0.2094F);
		window_bottom_r23.texOffs(0, 263).addBox(-1, 0, -12.5F, 1, 8, 25, 0, true);

		window_top_r30 = new ModelMapper(modelDataWrapper);
		window_top_r30.setPos(21, -13, 0);
		window_end_exterior_1_6.addChild(window_top_r30);
		setRotationAngle(window_top_r30, 0, 0, -0.1396F);
		window_top_r30.texOffs(100, 170).addBox(0, -21, -12.5F, 0, 21, 25, 0, true);

		window_end_exterior_4 = new ModelMapper(modelDataWrapper);
		window_end_exterior_4.setPos(0, 24, 0);


		window_end_exterior_1_7 = new ModelMapper(modelDataWrapper);
		window_end_exterior_1_7.setPos(0, 0, 0);
		window_end_exterior_4.addChild(window_end_exterior_1_7);
		window_end_exterior_1_7.texOffs(88, 279).addBox(-21, -13, -12.5F, 0, 6, 25, 0, false);

		window_bottom_r24 = new ModelMapper(modelDataWrapper);
		window_bottom_r24.setPos(-21, -7, 0);
		window_end_exterior_1_7.addChild(window_bottom_r24);
		setRotationAngle(window_bottom_r24, 0, 0, -0.2094F);
		window_bottom_r24.texOffs(260, 249).addBox(0, 0, -12.5F, 1, 8, 25, 0, false);

		window_top_r31 = new ModelMapper(modelDataWrapper);
		window_top_r31.setPos(-21, -13, 0);
		window_end_exterior_1_7.addChild(window_top_r31);
		setRotationAngle(window_top_r31, 0, 0, 0.1396F);
		window_top_r31.texOffs(50, 170).addBox(0, -21, -12.5F, 0, 21, 25, 0, false);

		window_end_exterior_1_8 = new ModelMapper(modelDataWrapper);
		window_end_exterior_1_8.setPos(0, 0, 0);
		window_end_exterior_4.addChild(window_end_exterior_1_8);
		window_end_exterior_1_8.texOffs(187, 278).addBox(21, -13, -12.5F, 0, 6, 25, 0, true);

		window_bottom_r25 = new ModelMapper(modelDataWrapper);
		window_bottom_r25.setPos(21, -7, 0);
		window_end_exterior_1_8.addChild(window_bottom_r25);
		setRotationAngle(window_bottom_r25, 0, 0, 0.2094F);
		window_bottom_r25.texOffs(260, 216).addBox(-1, 0, -12.5F, 1, 8, 25, 0, true);

		window_top_r32 = new ModelMapper(modelDataWrapper);
		window_top_r32.setPos(21, -13, 0);
		window_end_exterior_1_8.addChild(window_top_r32);
		setRotationAngle(window_top_r32, 0, 0, -0.1396F);
		window_top_r32.texOffs(0, 163).addBox(0, -21, -12.5F, 0, 21, 25, 0, true);

		roof_exterior_1 = new ModelMapper(modelDataWrapper);
		roof_exterior_1.setPos(0, 24, 0);
		roof_exterior_1.texOffs(193, 88).addBox(-3.8369F, -40.0424F, -12.5F, 4, 0, 25, 0, false);

		roof_3_r1 = new ModelMapper(modelDataWrapper);
		roof_3_r1.setPos(-7.2836F, -39.4346F, 0);
		roof_exterior_1.addChild(roof_3_r1);
		setRotationAngle(roof_3_r1, 0, 0, 1.3963F);
		roof_3_r1.texOffs(208, 214).addBox(0, -3.5F, -12.5F, 0, 7, 25, 0, false);

		roof_2_r1 = new ModelMapper(modelDataWrapper);
		roof_2_r1.setPos(-13.7612F, -37.077F, 0);
		roof_exterior_1.addChild(roof_2_r1);
		setRotationAngle(roof_2_r1, 0, 0, 1.0472F);
		roof_2_r1.texOffs(27, 219).addBox(0, -3.5F, -12.5F, 0, 7, 25, 0, false);

		roof_1_r2 = new ModelMapper(modelDataWrapper);
		roof_1_r2.setPos(-16.792F, -35.3272F, 0);
		roof_exterior_1.addChild(roof_1_r2);
		setRotationAngle(roof_1_r2, 0, 0, 0.6981F);
		roof_1_r2.texOffs(226, 31).addBox(0, 0, -12.5F, 0, 2, 25, 0, false);

		roof_exterior_door_edge = new ModelMapper(modelDataWrapper);
		roof_exterior_door_edge.setPos(0, 24, 0);


		roof_exterior_door_edge_1 = new ModelMapper(modelDataWrapper);
		roof_exterior_door_edge_1.setPos(0, 0, 0);
		roof_exterior_door_edge.addChild(roof_exterior_door_edge_1);
		roof_exterior_door_edge_1.texOffs(191, 142).addBox(-3.8369F, -40.0424F, -12.5F, 4, 0, 25, 0, false);

		roof_4_r1 = new ModelMapper(modelDataWrapper);
		roof_4_r1.setPos(-7.2836F, -39.4346F, 0);
		roof_exterior_door_edge_1.addChild(roof_4_r1);
		setRotationAngle(roof_4_r1, 0, 0, 1.3963F);
		roof_4_r1.texOffs(27, 212).addBox(0, -3.5F, -12.5F, 0, 7, 25, 0, false);

		roof_3_r2 = new ModelMapper(modelDataWrapper);
		roof_3_r2.setPos(-13.7612F, -37.077F, 0);
		roof_exterior_door_edge_1.addChild(roof_3_r2);
		setRotationAngle(roof_3_r2, 0, 0, 1.0472F);
		roof_3_r2.texOffs(79, 212).addBox(0, -3.5F, -12.5F, 0, 7, 25, 0, false);

		roof_2_r2 = new ModelMapper(modelDataWrapper);
		roof_2_r2.setPos(-16.792F, -35.3272F, 0);
		roof_exterior_door_edge_1.addChild(roof_2_r2);
		setRotationAngle(roof_2_r2, 0, 0, 0.6981F);
		roof_2_r2.texOffs(226, 29).addBox(0, 0, -12.5F, 0, 2, 25, 0, false);

		roof_exterior_door_edge_2 = new ModelMapper(modelDataWrapper);
		roof_exterior_door_edge_2.setPos(0, 0, 0);
		roof_exterior_door_edge.addChild(roof_exterior_door_edge_2);
		roof_exterior_door_edge_2.texOffs(185, 88).addBox(-0.1631F, -40.0424F, -12.5F, 4, 0, 25, 0, true);

		roof_5_r1 = new ModelMapper(modelDataWrapper);
		roof_5_r1.setPos(7.2836F, -39.4346F, 0);
		roof_exterior_door_edge_2.addChild(roof_5_r1);
		setRotationAngle(roof_5_r1, 0, 0, -1.3963F);
		roof_5_r1.texOffs(98, 74).addBox(0, -3.5F, -12.5F, 0, 7, 25, 0, true);

		roof_4_r2 = new ModelMapper(modelDataWrapper);
		roof_4_r2.setPos(13.7612F, -37.077F, 0);
		roof_exterior_door_edge_2.addChild(roof_4_r2);
		setRotationAngle(roof_4_r2, 0, 0, -1.0472F);
		roof_4_r2.texOffs(152, 211).addBox(0, -3.5F, -12.5F, 0, 7, 25, 0, true);

		roof_3_r3 = new ModelMapper(modelDataWrapper);
		roof_3_r3.setPos(16.792F, -35.3272F, 0);
		roof_exterior_door_edge_2.addChild(roof_3_r3);
		setRotationAngle(roof_3_r3, 0, 0, -0.6981F);
		roof_3_r3.texOffs(27, 226).addBox(0, 0, -12.5F, 0, 2, 25, 0, true);

		roof_exterior_door = new ModelMapper(modelDataWrapper);
		roof_exterior_door.setPos(0, 24, 0);


		roof_exterior_door_1 = new ModelMapper(modelDataWrapper);
		roof_exterior_door_1.setPos(0, 0, 0);
		roof_exterior_door.addChild(roof_exterior_door_1);
		roof_exterior_door_1.texOffs(172, 144).addBox(-3.8369F, -40.0424F, -14, 4, 0, 28, 0, false);

		roof_5_r2 = new ModelMapper(modelDataWrapper);
		roof_5_r2.setPos(-7.2836F, -39.4346F, 0);
		roof_exterior_door_1.addChild(roof_5_r2);
		setRotationAngle(roof_5_r2, 0, 0, 1.3963F);
		roof_5_r2.texOffs(126, 42).addBox(0, -3.5F, -14, 0, 7, 28, 0, false);

		roof_4_r3 = new ModelMapper(modelDataWrapper);
		roof_4_r3.setPos(-13.7612F, -37.077F, 0);
		roof_exterior_door_1.addChild(roof_4_r3);
		setRotationAngle(roof_4_r3, 0, 0, 1.0472F);
		roof_4_r3.texOffs(150, 199).addBox(0, -3.5F, -14, 0, 7, 28, 0, false);

		roof_3_r4 = new ModelMapper(modelDataWrapper);
		roof_3_r4.setPos(-16.792F, -35.3272F, 0);
		roof_exterior_door_1.addChild(roof_3_r4);
		setRotationAngle(roof_3_r4, 0, 0, 0.6981F);
		roof_3_r4.texOffs(150, 206).addBox(0, 0, -14, 0, 2, 28, 0, false);

		roof_exterior_door_2 = new ModelMapper(modelDataWrapper);
		roof_exterior_door_2.setPos(0, 0, 0);
		roof_exterior_door.addChild(roof_exterior_door_2);
		roof_exterior_door_2.texOffs(246, 297).addBox(-0.1631F, -40.0424F, -14, 4, 0, 28, 0, true);

		roof_6_r1 = new ModelMapper(modelDataWrapper);
		roof_6_r1.setPos(7.2836F, -39.4346F, 0);
		roof_exterior_door_2.addChild(roof_6_r1);
		setRotationAngle(roof_6_r1, 0, 0, -1.3963F);
		roof_6_r1.texOffs(208, 322).addBox(0, -3.5F, -14, 0, 7, 28, 0, true);

		roof_5_r3 = new ModelMapper(modelDataWrapper);
		roof_5_r3.setPos(13.7612F, -37.077F, 0);
		roof_exterior_door_2.addChild(roof_5_r3);
		setRotationAngle(roof_5_r3, 0, 0, -1.0472F);
		roof_5_r3.texOffs(208, 313).addBox(0, -3.5F, -14, 0, 7, 28, 0, true);

		roof_4_r4 = new ModelMapper(modelDataWrapper);
		roof_4_r4.setPos(16.792F, -35.3272F, 0);
		roof_exterior_door_2.addChild(roof_4_r4);
		setRotationAngle(roof_4_r4, 0, 0, -0.6981F);
		roof_4_r4.texOffs(208, 320).addBox(0, 0, -14, 0, 2, 28, 0, true);

		door = new ModelMapper(modelDataWrapper);
		door.setPos(0, 24, 0);
		door.texOffs(60, 49).addBox(-19, 0, -14, 19, 1, 28, 0, false);
		door.texOffs(275, 33).addBox(-20, -13, 11, 1, 6, 1, 0, false);
		door.texOffs(279, 33).addBox(-20, -13, -12, 1, 6, 1, 0, false);
		door.texOffs(82, 316).addBox(-20, -32, 11.05F, 6, 32, 3, 0, false);
		door.texOffs(190, 315).addBox(-20, -32, -14.05F, 6, 32, 3, 0, false);
		door.texOffs(60, 78).addBox(-18, -37, -14, 5, 5, 28, 0, false);
		door.texOffs(66, 315).addBox(-14, -37, 14, 8, 37, 0, 0, false);
		door.texOffs(258, 297).addBox(-14, -37, -14, 8, 37, 0, 0, false);
		door.texOffs(172, 88).addBox(-5, -35, -14, 5, 0, 28, 0, false);

		light_edge_r2 = new ModelMapper(modelDataWrapper);
		light_edge_r2.setPos(-5, -35, 0);
		door.addChild(light_edge_r2);
		setRotationAngle(light_edge_r2, 0, 0, 0.2182F);
		light_edge_r2.texOffs(206, 202).addBox(-5, 0, -14, 1, 1, 28, 0, false);
		light_edge_r2.texOffs(60, 0).addBox(-9, 0, -14, 9, 0, 28, 0, false);

		door_top_3_r1 = new ModelMapper(modelDataWrapper);
		door_top_3_r1.setPos(-11.7395F, -35.2093F, 0);
		door.addChild(door_top_3_r1);
		setRotationAngle(door_top_3_r1, 0, 0, -1.3963F);
		door_top_3_r1.texOffs(172, 197).addBox(-1.5F, -2, -14, 3, 2, 28, 0, false);

		door_top_2_r1 = new ModelMapper(modelDataWrapper);
		door_top_2_r1.setPos(-13, -32, 0);
		door.addChild(door_top_2_r1);
		setRotationAngle(door_top_2_r1, 0, 0, -1.0472F);
		door_top_2_r1.texOffs(200, 144).addBox(0, -1, -14, 2, 1, 28, 0, false);

		window_top_2_r1 = new ModelMapper(modelDataWrapper);
		window_top_2_r1.setPos(-21, -13, 12);
		door.addChild(window_top_2_r1);
		setRotationAngle(window_top_2_r1, 0, 0, 0.1396F);
		window_top_2_r1.texOffs(287, 33).addBox(1, -21, -24, 1, 21, 1, 0, false);
		window_top_2_r1.texOffs(283, 33).addBox(1, -21, -1, 1, 21, 1, 0, false);

		window_bottom_2_r1 = new ModelMapper(modelDataWrapper);
		window_bottom_2_r1.setPos(-21, -7, 12);
		door.addChild(window_bottom_2_r1);
		setRotationAngle(window_bottom_2_r1, 0, 0, -0.2094F);
		window_bottom_2_r1.texOffs(271, 33).addBox(1, 0, -24, 1, 8, 1, 0, false);
		window_bottom_2_r1.texOffs(267, 33).addBox(1, 0, -1, 1, 8, 1, 0, false);

		door_left = new ModelMapper(modelDataWrapper);
		door_left.setPos(0, 0, 0);
		door.addChild(door_left);
		door_left.texOffs(222, 7).addBox(-20, -13, 0, 0, 6, 12, 0, false);

		door_top_r1 = new ModelMapper(modelDataWrapper);
		door_top_r1.setPos(-21, -13, 12);
		door_left.addChild(door_top_r1);
		setRotationAngle(door_top_r1, 0, 0, 0.1396F);
		door_top_r1.texOffs(206, 190).addBox(1, -21, -12, 0, 21, 12, 0, false);

		door_bottom_r1 = new ModelMapper(modelDataWrapper);
		door_bottom_r1.setPos(-21, -7, 12);
		door_left.addChild(door_bottom_r1);
		setRotationAngle(door_bottom_r1, 0, 0, -0.2094F);
		door_bottom_r1.texOffs(0, 226).addBox(1, 0, -12, 0, 8, 12, 0, false);

		door_right = new ModelMapper(modelDataWrapper);
		door_right.setPos(0, 0, 0);
		door.addChild(door_right);
		door_right.texOffs(158, 0).addBox(-20, -13, -12, 0, 6, 12, 0, false);

		door_top_r2 = new ModelMapper(modelDataWrapper);
		door_top_r2.setPos(-21, -13, 12);
		door_right.addChild(door_top_r2);
		setRotationAngle(door_top_r2, 0, 0, 0.1396F);
		door_top_r2.texOffs(0, 65).addBox(1, -21, -24, 0, 21, 12, 0, false);

		door_bottom_r2 = new ModelMapper(modelDataWrapper);
		door_bottom_r2.setPos(-21, -7, 12);
		door_right.addChild(door_bottom_r2);
		setRotationAngle(door_bottom_r2, 0, 0, -0.2094F);
		door_bottom_r2.texOffs(0, 218).addBox(1, 0, -24, 0, 8, 12, 0, false);

		door_exterior = new ModelMapper(modelDataWrapper);
		door_exterior.setPos(0, 24, 0);
		door_exterior.texOffs(226, 88).addBox(-21, -13, 12, 1, 6, 2, 0, false);
		door_exterior.texOffs(190, 67).addBox(-21, -13, -14, 1, 6, 2, 0, false);
		door_exterior.texOffs(280, 0).addBox(-21, 0, -12, 2, 1, 24, 0, false);

		window_top_2_r2 = new ModelMapper(modelDataWrapper);
		window_top_2_r2.setPos(-21, -13, 12);
		door_exterior.addChild(window_top_2_r2);
		setRotationAngle(window_top_2_r2, 0, 0, 0.1396F);
		window_top_2_r2.texOffs(81, 78).addBox(0, -21, -26, 1, 21, 2, 0, false);
		window_top_2_r2.texOffs(200, 173).addBox(0, -21, 0, 1, 21, 2, 0, false);

		window_bottom_2_r2 = new ModelMapper(modelDataWrapper);
		window_bottom_2_r2.setPos(-21, -7, 12);
		door_exterior.addChild(window_bottom_2_r2);
		setRotationAngle(window_bottom_2_r2, 0, 0, -0.2094F);
		window_bottom_2_r2.texOffs(182, 7).addBox(0, 0, -26, 1, 8, 2, 0, false);
		window_bottom_2_r2.texOffs(188, 7).addBox(0, 0, 0, 1, 8, 2, 0, false);

		door_left_exterior = new ModelMapper(modelDataWrapper);
		door_left_exterior.setPos(0, 0, 0);
		door_exterior.addChild(door_left_exterior);
		door_left_exterior.texOffs(245, 96).addBox(-21, -13, 0, 1, 6, 12, 0, false);

		door_top_r3 = new ModelMapper(modelDataWrapper);
		door_top_r3.setPos(-21, -13, 12);
		door_left_exterior.addChild(door_top_r3);
		setRotationAngle(door_top_r3, 0, 0, 0.1396F);
		door_top_r3.texOffs(305, 150).addBox(0, -21, -12, 1, 21, 12, 0, false);

		door_bottom_r3 = new ModelMapper(modelDataWrapper);
		door_bottom_r3.setPos(-21, -7, 12);
		door_left_exterior.addChild(door_bottom_r3);
		setRotationAngle(door_bottom_r3, 0, 0, -0.2094F);
		door_bottom_r3.texOffs(117, 237).addBox(0, 0, -12, 1, 8, 12, 0, false);

		door_right_exterior = new ModelMapper(modelDataWrapper);
		door_right_exterior.setPos(0, 0, 0);
		door_exterior.addChild(door_right_exterior);
		door_right_exterior.texOffs(238, 183).addBox(-21, -13, -12, 1, 6, 12, 0, false);

		door_top_r4 = new ModelMapper(modelDataWrapper);
		door_top_r4.setPos(-21, -13, 12);
		door_right_exterior.addChild(door_top_r4);
		setRotationAngle(door_top_r4, 0, 0, 0.1396F);
		door_top_r4.texOffs(299, 25).addBox(0, -21, -24, 1, 21, 12, 0, false);

		door_bottom_r4 = new ModelMapper(modelDataWrapper);
		door_bottom_r4.setPos(-21, -7, 12);
		door_right_exterior.addChild(door_bottom_r4);
		setRotationAngle(door_bottom_r4, 0, 0, -0.2094F);
		door_bottom_r4.texOffs(232, 150).addBox(0, 0, -24, 1, 8, 12, 0, false);

		door_exterior_end = new ModelMapper(modelDataWrapper);
		door_exterior_end.setPos(0, 24, 0);


		door_exterior_end_1 = new ModelMapper(modelDataWrapper);
		door_exterior_end_1.setPos(0, 0, 0);
		door_exterior_end.addChild(door_exterior_end_1);
		door_exterior_end_1.texOffs(226, 96).addBox(-21, -13, 12, 1, 6, 2, 0, false);
		door_exterior_end_1.texOffs(226, 104).addBox(-21, -13, -14, 1, 6, 2, 0, false);
		door_exterior_end_1.texOffs(313, 1).addBox(-21, 0, -12, 2, 1, 24, 0, false);

		window_top_2_r3 = new ModelMapper(modelDataWrapper);
		window_top_2_r3.setPos(-21, -13, 12);
		door_exterior_end_1.addChild(window_top_2_r3);
		setRotationAngle(window_top_2_r3, 0, 0, 0.1396F);
		window_top_2_r3.texOffs(298, 0).addBox(0, -21, -26, 1, 21, 2, 0, false);
		window_top_2_r3.texOffs(292, 0).addBox(0, -21, 0, 1, 21, 2, 0, false);

		window_bottom_2_r3 = new ModelMapper(modelDataWrapper);
		window_bottom_2_r3.setPos(-21, -7, 12);
		door_exterior_end_1.addChild(window_bottom_2_r3);
		setRotationAngle(window_bottom_2_r3, 0, 0, -0.2094F);
		window_bottom_2_r3.texOffs(280, 0).addBox(0, 0, -26, 1, 8, 2, 0, false);
		window_bottom_2_r3.texOffs(286, 0).addBox(0, 0, 0, 1, 8, 2, 0, false);

		door_left_exterior_end_1 = new ModelMapper(modelDataWrapper);
		door_left_exterior_end_1.setPos(0, 0, 0);
		door_exterior_end.addChild(door_left_exterior_end_1);
		door_left_exterior_end_1.texOffs(339, 46).addBox(-21, -13, 0, 1, 6, 12, 0, false);

		door_top_r5 = new ModelMapper(modelDataWrapper);
		door_top_r5.setPos(-21, -13, 12);
		door_left_exterior_end_1.addChild(door_top_r5);
		setRotationAngle(door_top_r5, 0, 0, 0.1396F);
		door_top_r5.texOffs(325, 25).addBox(0, -21, -12, 1, 21, 12, 0, false);

		door_bottom_r5 = new ModelMapper(modelDataWrapper);
		door_bottom_r5.setPos(-21, -7, 12);
		door_left_exterior_end_1.addChild(door_bottom_r5);
		setRotationAngle(door_bottom_r5, 0, 0, -0.2094F);
		door_bottom_r5.texOffs(112, 316).addBox(0, 0, -12, 1, 8, 12, 0, false);

		door_right_exterior_end_1 = new ModelMapper(modelDataWrapper);
		door_right_exterior_end_1.setPos(0, 0, 0);
		door_exterior_end.addChild(door_right_exterior_end_1);
		door_right_exterior_end_1.texOffs(339, 79).addBox(-21, -13, -12, 1, 6, 12, 0, false);

		door_top_r6 = new ModelMapper(modelDataWrapper);
		door_top_r6.setPos(-21, -13, 12);
		door_right_exterior_end_1.addChild(door_top_r6);
		setRotationAngle(door_top_r6, 0, 0, 0.1396F);
		door_top_r6.texOffs(325, 58).addBox(0, -21, -24, 1, 21, 12, 0, false);

		door_bottom_r6 = new ModelMapper(modelDataWrapper);
		door_bottom_r6.setPos(-21, -7, 12);
		door_right_exterior_end_1.addChild(door_bottom_r6);
		setRotationAngle(door_bottom_r6, 0, 0, -0.2094F);
		door_bottom_r6.texOffs(112, 336).addBox(0, 0, -24, 1, 8, 12, 0, false);

		door_exterior_end_2 = new ModelMapper(modelDataWrapper);
		door_exterior_end_2.setPos(0, 0, 0);
		door_exterior_end.addChild(door_exterior_end_2);
		door_exterior_end_2.texOffs(226, 96).addBox(20, -13, 12, 1, 6, 2, 0, true);
		door_exterior_end_2.texOffs(226, 104).addBox(20, -13, -14, 1, 6, 2, 0, true);
		door_exterior_end_2.texOffs(313, 1).addBox(19, 0, -12, 2, 1, 24, 0, true);

		window_top_2_r4 = new ModelMapper(modelDataWrapper);
		window_top_2_r4.setPos(21, -13, 12);
		door_exterior_end_2.addChild(window_top_2_r4);
		setRotationAngle(window_top_2_r4, 0, 0, -0.1396F);
		window_top_2_r4.texOffs(298, 0).addBox(-1, -21, -26, 1, 21, 2, 0, true);
		window_top_2_r4.texOffs(292, 0).addBox(-1, -21, 0, 1, 21, 2, 0, true);

		window_bottom_2_r4 = new ModelMapper(modelDataWrapper);
		window_bottom_2_r4.setPos(21, -7, 12);
		door_exterior_end_2.addChild(window_bottom_2_r4);
		setRotationAngle(window_bottom_2_r4, 0, 0, 0.2094F);
		window_bottom_2_r4.texOffs(280, 0).addBox(-1, 0, -26, 1, 8, 2, 0, true);
		window_bottom_2_r4.texOffs(286, 0).addBox(-1, 0, 0, 1, 8, 2, 0, true);

		door_left_exterior_end_2 = new ModelMapper(modelDataWrapper);
		door_left_exterior_end_2.setPos(0, 0, 0);
		door_exterior_end.addChild(door_left_exterior_end_2);
		door_left_exterior_end_2.texOffs(339, 46).addBox(20, -13, 0, 1, 6, 12, 0, true);

		door_top_r7 = new ModelMapper(modelDataWrapper);
		door_top_r7.setPos(21, -13, 12);
		door_left_exterior_end_2.addChild(door_top_r7);
		setRotationAngle(door_top_r7, 0, 0, -0.1396F);
		door_top_r7.texOffs(325, 25).addBox(-1, -21, -12, 1, 21, 12, 0, true);

		door_bottom_r7 = new ModelMapper(modelDataWrapper);
		door_bottom_r7.setPos(21, -7, 12);
		door_left_exterior_end_2.addChild(door_bottom_r7);
		setRotationAngle(door_bottom_r7, 0, 0, 0.2094F);
		door_bottom_r7.texOffs(112, 316).addBox(-1, 0, -12, 1, 8, 12, 0, true);

		door_right_exterior_end_2 = new ModelMapper(modelDataWrapper);
		door_right_exterior_end_2.setPos(0, 0, 0);
		door_exterior_end.addChild(door_right_exterior_end_2);
		door_right_exterior_end_2.texOffs(339, 79).addBox(20, -13, -12, 1, 6, 12, 0, true);

		door_top_r8 = new ModelMapper(modelDataWrapper);
		door_top_r8.setPos(21, -13, 12);
		door_right_exterior_end_2.addChild(door_top_r8);
		setRotationAngle(door_top_r8, 0, 0, -0.1396F);
		door_top_r8.texOffs(325, 58).addBox(-1, -21, -24, 1, 21, 12, 0, true);

		door_bottom_r8 = new ModelMapper(modelDataWrapper);
		door_bottom_r8.setPos(21, -7, 12);
		door_right_exterior_end_2.addChild(door_bottom_r8);
		setRotationAngle(door_bottom_r8, 0, 0, 0.2094F);
		door_bottom_r8.texOffs(112, 336).addBox(-1, 0, -24, 1, 8, 12, 0, true);

		end = new ModelMapper(modelDataWrapper);
		end.setPos(0, 24, 0);
		end.texOffs(208, 331).addBox(-7, -36, -12.5F, 14, 4, 6, 0, false);
		end.texOffs(106, 0).addBox(-19, 0, -12.5F, 38, 1, 6, 0, false);

		end_1 = new ModelMapper(modelDataWrapper);
		end_1.setPos(0, 0, 0);
		end.addChild(end_1);
		end_1.texOffs(0, 296).addBox(-20, -37, -12.5F, 13, 37, 6, 0, false);

		end_2 = new ModelMapper(modelDataWrapper);
		end_2.setPos(0, 0, 0);
		end.addChild(end_2);
		end_2.texOffs(293, 195).addBox(7, -37, -12.5F, 13, 37, 6, 0, true);

		end_exterior = new ModelMapper(modelDataWrapper);
		end_exterior.setPos(0, 24, 0);
		end_exterior.texOffs(287, 266).addBox(-7, -40, -6.5F, 14, 8, 0, 0, false);

		end_exterior_1 = new ModelMapper(modelDataWrapper);
		end_exterior_1.setPos(0, 0, 0);
		end_exterior.addChild(end_exterior_1);
		end_exterior_1.texOffs(246, 150).addBox(-21, -13, -12.5F, 1, 6, 6, 0, false);
		end_exterior_1.texOffs(164, 315).addBox(-20, -39, -6.5F, 13, 39, 0, 0, false);

		window_bottom_r26 = new ModelMapper(modelDataWrapper);
		window_bottom_r26.setPos(-21, -7, 0);
		end_exterior_1.addChild(window_bottom_r26);
		setRotationAngle(window_bottom_r26, 0, 0, -0.2094F);
		window_bottom_r26.texOffs(106, 251).addBox(0, 0, -12.5F, 1, 8, 6, 0, false);

		window_top_r33 = new ModelMapper(modelDataWrapper);
		window_top_r33.setPos(-21, -13, 0);
		end_exterior_1.addChild(window_top_r33);
		setRotationAngle(window_top_r33, 0, 0, 0.1396F);
		window_top_r33.texOffs(256, 59).addBox(0, -21, -12.5F, 1, 21, 6, 0, false);

		end_exterior_2 = new ModelMapper(modelDataWrapper);
		end_exterior_2.setPos(0, 0, 0);
		end_exterior.addChild(end_exterior_2);
		end_exterior_2.texOffs(131, 237).addBox(20, -13, -12.5F, 1, 6, 6, 0, true);
		end_exterior_2.texOffs(138, 315).addBox(7, -39, -6.5F, 13, 39, 0, 0, true);

		window_bottom_r27 = new ModelMapper(modelDataWrapper);
		window_bottom_r27.setPos(21, -7, 0);
		end_exterior_2.addChild(window_bottom_r27);
		setRotationAngle(window_bottom_r27, 0, 0, 0.2094F);
		window_bottom_r27.texOffs(79, 244).addBox(-1, 0, -12.5F, 1, 8, 6, 0, true);

		window_top_r34 = new ModelMapper(modelDataWrapper);
		window_top_r34.setPos(21, -13, 0);
		end_exterior_2.addChild(window_top_r34);
		setRotationAngle(window_top_r34, 0, 0, -0.1396F);
		window_top_r34.texOffs(220, 173).addBox(-1, -21, -12.5F, 1, 21, 6, 0, true);

		roof_end_exterior_1 = new ModelMapper(modelDataWrapper);
		roof_end_exterior_1.setPos(0, 0, 0);
		end_exterior.addChild(roof_end_exterior_1);
		roof_end_exterior_1.texOffs(27, 230).addBox(-3.8369F, -40.0424F, -12.5F, 4, 1, 6, 0, false);

		roof_3_r5 = new ModelMapper(modelDataWrapper);
		roof_3_r5.setPos(-7.2836F, -39.4346F, 0);
		roof_end_exterior_1.addChild(roof_3_r5);
		setRotationAngle(roof_3_r5, 0, 0, 1.3963F);
		roof_3_r5.texOffs(256, 11).addBox(0, -3.5F, -12.5F, 1, 7, 6, 0, false);

		roof_2_r3 = new ModelMapper(modelDataWrapper);
		roof_2_r3.setPos(-13.7612F, -37.077F, 0);
		roof_end_exterior_1.addChild(roof_2_r3);
		setRotationAngle(roof_2_r3, 0, 0, 1.0472F);
		roof_2_r3.texOffs(258, 217).addBox(0, -3.5F, -12.5F, 1, 7, 6, 0, false);

		roof_1_r3 = new ModelMapper(modelDataWrapper);
		roof_1_r3.setPos(-16.792F, -35.3272F, 0);
		roof_end_exterior_1.addChild(roof_1_r3);
		setRotationAngle(roof_1_r3, 0, 0, 0.6981F);
		roof_1_r3.texOffs(248, 80).addBox(0, 0, -12.5F, 1, 2, 6, 0, false);

		roof_end_exterior_2 = new ModelMapper(modelDataWrapper);
		roof_end_exterior_2.setPos(0, 0, 0);
		end_exterior.addChild(roof_end_exterior_2);
		roof_end_exterior_2.texOffs(176, 71).addBox(-0.1631F, -40.0424F, -12.5F, 4, 1, 6, 0, true);

		roof_4_r5 = new ModelMapper(modelDataWrapper);
		roof_4_r5.setPos(7.2836F, -39.4346F, 0);
		roof_end_exterior_2.addChild(roof_4_r5);
		setRotationAngle(roof_4_r5, 0, 0, -1.3963F);
		roof_4_r5.texOffs(179, 243).addBox(-1, -3.5F, -12.5F, 1, 7, 6, 0, true);

		roof_3_r6 = new ModelMapper(modelDataWrapper);
		roof_3_r6.setPos(13.7612F, -37.077F, 0);
		roof_end_exterior_2.addChild(roof_3_r6);
		setRotationAngle(roof_3_r6, 0, 0, -1.0472F);
		roof_3_r6.texOffs(253, 33).addBox(-1, -3.5F, -12.5F, 1, 7, 6, 0, true);

		roof_2_r4 = new ModelMapper(modelDataWrapper);
		roof_2_r4.setPos(16.792F, -35.3272F, 0);
		roof_end_exterior_2.addChild(roof_2_r4);
		setRotationAngle(roof_2_r4, 0, 0, -0.6981F);
		roof_2_r4.texOffs(0, 246).addBox(-1, 0, -12.5F, 1, 2, 6, 0, true);

		head = new ModelMapper(modelDataWrapper);
		head.setPos(0, 24, 0);
		head.texOffs(0, 40).addBox(-22, -37, -25.5F, 44, 37, 0, 0, false);

		head_exterior = new ModelMapper(modelDataWrapper);
		head_exterior.setPos(0, 24, 0);
		head_exterior.texOffs(283, 323).addBox(-7, -34, 2.5F, 14, 36, 6, 0, false);
		head_exterior.texOffs(0, 0).addBox(-22, -40, -12, 44, 40, 0, 0, false);

		front_r1 = new ModelMapper(modelDataWrapper);
		front_r1.setPos(0, 2, 5.5F);
		head_exterior.addChild(front_r1);
		setRotationAngle(front_r1, 0.0698F, 0, 0);
		front_r1.texOffs(283, 282).addBox(-21, -41, 0, 42, 41, 0, 0, false);

		head_exterior_1 = new ModelMapper(modelDataWrapper);
		head_exterior_1.setPos(0, 0, 0);
		head_exterior.addChild(head_exterior_1);
		head_exterior_1.texOffs(150, 1).addBox(-21, -13, -12.5F, 0, 6, 18, 0, false);
		head_exterior_1.texOffs(176, 0).addBox(-19, 0, -12.5F, 19, 1, 18, 0, false);

		front_bottom_r1 = new ModelMapper(modelDataWrapper);
		front_bottom_r1.setPos(0, 2, 5.5F);
		head_exterior_1.addChild(front_bottom_r1);
		setRotationAngle(front_bottom_r1, -0.5236F, 0, 0);
		front_bottom_r1.texOffs(114, 7).addBox(-21, 0, -1, 21, 8, 1, 0, false);

		roof_5_r4 = new ModelMapper(modelDataWrapper);
		roof_5_r4.setPos(-1.8369F, -39.258F, -3.5343F);
		head_exterior_1.addChild(roof_5_r4);
		setRotationAngle(roof_5_r4, -0.0873F, 0, 0);
		roof_5_r4.texOffs(88, 7).addBox(-2, 0, -9, 4, 0, 18, 0, false);

		roof_4_r6 = new ModelMapper(modelDataWrapper);
		roof_4_r6.setPos(-7.1474F, -38.6622F, -3.5343F);
		head_exterior_1.addChild(roof_4_r6);
		setRotationAngle(roof_4_r6, 0, 0.0873F, 1.3963F);
		roof_4_r6.texOffs(114, 0).addBox(0, -3.5F, -9, 0, 7, 18, 0, false);

		roof_3_r7 = new ModelMapper(modelDataWrapper);
		roof_3_r7.setPos(-13.369F, -36.3977F, -3.5343F);
		head_exterior_1.addChild(roof_3_r7);
		setRotationAngle(roof_3_r7, 0, 0.0873F, 1.0472F);
		roof_3_r7.texOffs(226, 40).addBox(0, -3.5F, -9, 0, 7, 18, 0, false);

		roof_2_r5 = new ModelMapper(modelDataWrapper);
		roof_2_r5.setPos(-17.7981F, -32.9079F, -3.5343F);
		head_exterior_1.addChild(roof_2_r5);
		setRotationAngle(roof_2_r5, 0, 0.0873F, 0.6981F);
		roof_2_r5.texOffs(186, 1).addBox(0, -2.5F, -9, 0, 5, 18, 0, false);

		window_bottom_r28 = new ModelMapper(modelDataWrapper);
		window_bottom_r28.setPos(-21, -7, 0);
		head_exterior_1.addChild(window_bottom_r28);
		setRotationAngle(window_bottom_r28, 0, 0, -0.2094F);
		window_bottom_r28.texOffs(220, 297).addBox(0, 0, -12.5F, 1, 16, 18, 0, false);

		window_top_r35 = new ModelMapper(modelDataWrapper);
		window_top_r35.setPos(-21, -13, 0);
		head_exterior_1.addChild(window_top_r35);
		setRotationAngle(window_top_r35, 0, 0, 0.1396F);
		window_top_r35.texOffs(218, 99).addBox(0, -21, -12.5F, 0, 21, 18, 0, false);

		head_exterior_2 = new ModelMapper(modelDataWrapper);
		head_exterior_2.setPos(0, 0, 0);
		head_exterior.addChild(head_exterior_2);
		head_exterior_2.texOffs(150, 1).addBox(21, -13, -12.5F, 0, 6, 18, 0, true);
		head_exterior_2.texOffs(176, 0).addBox(0, 0, -12.5F, 19, 1, 18, 0, true);

		front_bottom_r2 = new ModelMapper(modelDataWrapper);
		front_bottom_r2.setPos(0, 2, 5.5F);
		head_exterior_2.addChild(front_bottom_r2);
		setRotationAngle(front_bottom_r2, -0.5236F, 0, 0);
		front_bottom_r2.texOffs(114, 7).addBox(0, 0, -1, 21, 8, 1, 0, true);

		roof_6_r2 = new ModelMapper(modelDataWrapper);
		roof_6_r2.setPos(1.8369F, -39.258F, -3.5343F);
		head_exterior_2.addChild(roof_6_r2);
		setRotationAngle(roof_6_r2, -0.0873F, 0, 0);
		roof_6_r2.texOffs(88, 7).addBox(-2, 0, -9, 4, 0, 18, 0, true);

		roof_5_r5 = new ModelMapper(modelDataWrapper);
		roof_5_r5.setPos(7.1474F, -38.6622F, -3.5343F);
		head_exterior_2.addChild(roof_5_r5);
		setRotationAngle(roof_5_r5, 0, -0.0873F, -1.3963F);
		roof_5_r5.texOffs(114, 0).addBox(0, -3.5F, -9, 0, 7, 18, 0, true);

		roof_4_r7 = new ModelMapper(modelDataWrapper);
		roof_4_r7.setPos(13.369F, -36.3977F, -3.5343F);
		head_exterior_2.addChild(roof_4_r7);
		setRotationAngle(roof_4_r7, 0, -0.0873F, -1.0472F);
		roof_4_r7.texOffs(226, 40).addBox(0, -3.5F, -9, 0, 7, 18, 0, true);

		roof_3_r8 = new ModelMapper(modelDataWrapper);
		roof_3_r8.setPos(17.7981F, -32.9079F, -3.5343F);
		head_exterior_2.addChild(roof_3_r8);
		setRotationAngle(roof_3_r8, 0, -0.0873F, -0.6981F);
		roof_3_r8.texOffs(186, 1).addBox(0, -2.5F, -9, 0, 5, 18, 0, true);

		window_bottom_r29 = new ModelMapper(modelDataWrapper);
		window_bottom_r29.setPos(21, -7, 0);
		head_exterior_2.addChild(window_bottom_r29);
		setRotationAngle(window_bottom_r29, 0, 0, 0.2094F);
		window_bottom_r29.texOffs(220, 297).addBox(-1, 0, -12.5F, 1, 16, 18, 0, true);

		window_top_r36 = new ModelMapper(modelDataWrapper);
		window_top_r36.setPos(21, -13, 0);
		head_exterior_2.addChild(window_top_r36);
		setRotationAngle(window_top_r36, 0, 0, -0.1396F);
		window_top_r36.texOffs(218, 99).addBox(0, -21, -12.5F, 0, 21, 18, 0, true);

		seat = new ModelMapper(modelDataWrapper);
		seat.setPos(0, 24, 0);
		seat.texOffs(232, 9).addBox(-4, -6, -4, 8, 1, 7, 0, false);
		seat.texOffs(236, 202).addBox(-3.5F, -22.644F, 4.0686F, 7, 5, 1, 0, false);

		seat_2_r1 = new ModelMapper(modelDataWrapper);
		seat_2_r1.setPos(0, -6, 2);
		seat.addChild(seat_2_r1);
		setRotationAngle(seat_2_r1, -0.1745F, 0, 0);
		seat_2_r1.texOffs(152, 243).addBox(-4, -12, 0, 8, 12, 1, 0, false);

		door_light_box = new ModelMapper(modelDataWrapper);
		door_light_box.setPos(0, 24, 0);


		door_light_r1 = new ModelMapper(modelDataWrapper);
		door_light_r1.setPos(-21, -13, 0);
		door_light_box.addChild(door_light_r1);
		setRotationAngle(door_light_r1, 0, 0, 0.1396F);
		door_light_r1.texOffs(232, 0).addBox(-0.5F, -19, -1, 1, 2, 2, 0, false);

		door_light_off = new ModelMapper(modelDataWrapper);
		door_light_off.setPos(0, 24, 0);


		door_light_r2 = new ModelMapper(modelDataWrapper);
		door_light_r2.setPos(-21, -13, 0);
		door_light_off.addChild(door_light_r2);
		setRotationAngle(door_light_r2, 0, 0, 0.1396F);
		door_light_r2.texOffs(238, 0).addBox(-0.5F, -19, -1, 1, 2, 2, 0, false);

		door_light_on = new ModelMapper(modelDataWrapper);
		door_light_on.setPos(0, 24, 0);


		door_light_r3 = new ModelMapper(modelDataWrapper);
		door_light_r3.setPos(-21, -13, 0);
		door_light_on.addChild(door_light_r3);
		setRotationAngle(door_light_r3, 0, 0, 0.1396F);
		door_light_r3.texOffs(244, 0).addBox(-0.5F, -19, -1, 1, 2, 2, 0, false);

		headlights = new ModelMapper(modelDataWrapper);
		headlights.setPos(0, 24, 0);


		headlight_2_r1 = new ModelMapper(modelDataWrapper);
		headlight_2_r1.setPos(0, 2, 5.5F);
		headlights.addChild(headlight_2_r1);
		setRotationAngle(headlight_2_r1, 0.0698F, 0, 0);
		headlight_2_r1.texOffs(250, 0).addBox(12, -11, 0.1F, 6, 7, 0, 0, true);
		headlight_2_r1.texOffs(250, 0).addBox(-18, -11, 0.1F, 6, 7, 0, 0, false);

		tail_lights = new ModelMapper(modelDataWrapper);
		tail_lights.setPos(0, 24, 0);


		tail_light_2_r1 = new ModelMapper(modelDataWrapper);
		tail_light_2_r1.setPos(0, 2, 5.5F);
		tail_lights.addChild(tail_light_2_r1);
		setRotationAngle(tail_light_2_r1, 0.0698F, 0, 0);
		tail_light_2_r1.texOffs(262, 0).addBox(9, -9, 0.1F, 4, 5, 0, 0, true);
		tail_light_2_r1.texOffs(262, 0).addBox(-13, -9, 0.1F, 4, 5, 0, 0, false);

		modelDataWrapper.setModelPart(textureWidth, textureHeight);
		window.setModelPart();
		window_light.setModelPart();
		door_light.setModelPart();
		window_1.setModelPart();
		window_2.setModelPart();
		window_3.setModelPart();
		window_4.setModelPart();
		window_5.setModelPart();
		window_6.setModelPart();
		window_7.setModelPart();
		window_8.setModelPart();
		window_exterior_1.setModelPart();
		window_exterior_2.setModelPart();
		window_exterior_3.setModelPart();
		window_exterior_4.setModelPart();
		window_exterior_5.setModelPart();
		window_exterior_6.setModelPart();
		window_exterior_7.setModelPart();
		window_exterior_8.setModelPart();
		window_end_exterior_1.setModelPart();
		window_end_exterior_2.setModelPart();
		window_end_exterior_3.setModelPart();
		window_end_exterior_4.setModelPart();
		roof_exterior_1.setModelPart();
		roof_exterior_door_edge.setModelPart();
		roof_exterior_door.setModelPart();
		door.setModelPart();
		door_left.setModelPart(door.name);
		door_right.setModelPart(door.name);
		door_exterior.setModelPart();
		door_left_exterior.setModelPart(door_exterior.name);
		door_right_exterior.setModelPart(door_exterior.name);
		door_exterior_end.setModelPart();
		door_left_exterior_end_1.setModelPart(door_exterior_end.name);
		door_right_exterior_end_1.setModelPart(door_exterior_end.name);
		door_left_exterior_end_2.setModelPart(door_exterior_end.name);
		door_right_exterior_end_2.setModelPart(door_exterior_end.name);
		end.setModelPart();
		end_exterior.setModelPart();
		head.setModelPart();
		head_exterior.setModelPart();
		seat.setModelPart();
		door_light_box.setModelPart();
		door_light_off.setModelPart();
		door_light_on.setModelPart();
		headlights.setModelPart();
		tail_lights.setModelPart();
	}

	private static final int DOOR_MAX = 11;

	@Override
	public ModelClass377 createNew(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		return new ModelClass377(doorAnimationType, renderDoorOverlay);
	}

	@Override
	protected void renderWindowPositions(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
		final float[] windowPositions = getNewWindowPositions();

		switch (renderStage) {
			case LIGHTS:
				for (final float i : windowPositions) {
					renderMirror(window_light, matrices, vertices, light, i);
				}
				break;
			case INTERIOR:
				for (final float i : windowPositions) {
					renderMirror(window, matrices, vertices, light, i);
				}
				renderOnceFlipped(window_1, matrices, vertices, light, windowPositions[0]);
				renderOnceFlipped(window_2, matrices, vertices, light, windowPositions[1]);
				renderOnceFlipped(window_3, matrices, vertices, light, windowPositions[2]);
				renderOnceFlipped(window_4, matrices, vertices, light, windowPositions[3]);
				renderOnceFlipped(window_5, matrices, vertices, light, windowPositions[4]);
				renderOnceFlipped(window_6, matrices, vertices, light, windowPositions[5]);
				renderOnceFlipped(window_7, matrices, vertices, light, windowPositions[6]);
				renderOnceFlipped(window_8, matrices, vertices, light, windowPositions[7]);
				renderOnce(window_8, matrices, vertices, light, windowPositions[0]);
				renderOnce(window_7, matrices, vertices, light, windowPositions[1]);
				renderOnce(window_6, matrices, vertices, light, windowPositions[2]);
				renderOnce(window_5, matrices, vertices, light, windowPositions[3]);
				renderOnce(window_4, matrices, vertices, light, windowPositions[4]);
				renderOnce(window_3, matrices, vertices, light, windowPositions[5]);
				renderOnce(window_2, matrices, vertices, light, windowPositions[6]);
				renderOnce(window_1, matrices, vertices, light, windowPositions[7]);
				if (renderDetails) {
					for (final float i : windowPositions) {
						renderOnceFlipped(seat, matrices, vertices, light, -16, i - 7);
						renderOnceFlipped(seat, matrices, vertices, light, -8, i - 7);
						renderOnceFlipped(seat, matrices, vertices, light, 8, i - 7);
						renderOnceFlipped(seat, matrices, vertices, light, 16, i - 7);
						renderOnce(seat, matrices, vertices, light, -16, i + 7);
						renderOnce(seat, matrices, vertices, light, -8, i + 7);
						renderOnce(seat, matrices, vertices, light, 8, i + 7);
						renderOnce(seat, matrices, vertices, light, 16, i + 7);
					}
				}
				break;
			case EXTERIOR:
				if (isEnd1Head) {
					renderOnceFlipped(window_end_exterior_1, matrices, vertices, light, windowPositions[0]);
					renderOnceFlipped(window_end_exterior_2, matrices, vertices, light, windowPositions[1]);
					renderOnceFlipped(window_end_exterior_3, matrices, vertices, light, windowPositions[2]);
					renderOnceFlipped(window_end_exterior_4, matrices, vertices, light, windowPositions[3]);
				} else {
					renderOnceFlipped(window_exterior_1, matrices, vertices, light, windowPositions[0]);
					renderOnceFlipped(window_exterior_2, matrices, vertices, light, windowPositions[1]);
					renderOnceFlipped(window_exterior_3, matrices, vertices, light, windowPositions[2]);
					renderOnceFlipped(window_exterior_4, matrices, vertices, light, windowPositions[3]);
				}
				if (isEnd2Head) {
					renderOnce(window_end_exterior_4, matrices, vertices, light, windowPositions[4]);
					renderOnce(window_end_exterior_3, matrices, vertices, light, windowPositions[5]);
					renderOnce(window_end_exterior_2, matrices, vertices, light, windowPositions[6]);
					renderOnce(window_end_exterior_1, matrices, vertices, light, windowPositions[7]);
				} else {
					renderOnceFlipped(window_exterior_5, matrices, vertices, light, windowPositions[4]);
					renderOnceFlipped(window_exterior_6, matrices, vertices, light, windowPositions[5]);
					renderOnceFlipped(window_exterior_7, matrices, vertices, light, windowPositions[6]);
					renderOnceFlipped(window_exterior_8, matrices, vertices, light, windowPositions[7]);
				}
				renderMirror(roof_exterior_1, matrices, vertices, light, windowPositions[0]);
				renderOnce(roof_exterior_door_edge, matrices, vertices, light, windowPositions[1]);
				renderOnceFlipped(roof_exterior_door_edge, matrices, vertices, light, windowPositions[2]);
				renderMirror(roof_exterior_1, matrices, vertices, light, windowPositions[3]);
				renderMirror(roof_exterior_1, matrices, vertices, light, windowPositions[4]);
				renderOnce(roof_exterior_door_edge, matrices, vertices, light, windowPositions[5]);
				renderOnceFlipped(roof_exterior_door_edge, matrices, vertices, light, windowPositions[6]);
				renderMirror(roof_exterior_1, matrices, vertices, light, windowPositions[7]);
				break;
		}
	}

	@Override
	protected void renderDoorPositions(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
		final boolean isFirstDoor = isIndex(0, position, getDoorPositions());
		final boolean doorOpen = doorLeftZ > 0 || doorRightZ > 0;

		switch (renderStage) {
			case LIGHTS:
				if (isFirstDoor && doorOpen) {
					renderMirror(door_light_on, matrices, vertices, light, 0);
				}
				renderMirror(door_light, matrices, vertices, light, position);
				break;
			case INTERIOR:
				door_left.setOffset(doorRightX, 0, doorRightZ);
				door_right.setOffset(doorRightX, 0, -doorRightZ);
				renderOnce(door, matrices, vertices, light, position);
				door_left.setOffset(doorLeftX, 0, doorLeftZ);
				door_right.setOffset(doorLeftX, 0, -doorLeftZ);
				renderOnceFlipped(door, matrices, vertices, light, position);
				break;
			case EXTERIOR:
				if (isEnd1Head && isFirstDoor) {
					door_left_exterior_end_2.setOffset(-doorRightX, 0, doorRightZ);
					door_right_exterior_end_2.setOffset(-doorRightX, 0, -doorRightZ);
					door_left_exterior_end_1.setOffset(doorLeftX, 0, doorLeftZ);
					door_right_exterior_end_1.setOffset(doorLeftX, 0, -doorLeftZ);
					renderOnceFlipped(door_exterior_end, matrices, vertices, light, position);
				} else if (isEnd2Head && isIndex(-1, position, getDoorPositions())) {
					door_left_exterior_end_1.setOffset(doorRightX, 0, doorRightZ);
					door_right_exterior_end_1.setOffset(doorRightX, 0, -doorRightZ);
					door_left_exterior_end_2.setOffset(-doorLeftX, 0, doorLeftZ);
					door_right_exterior_end_2.setOffset(-doorLeftX, 0, -doorLeftZ);
					renderOnce(door_exterior_end, matrices, vertices, light, position);
				} else {
					door_left_exterior.setOffset(doorRightX, 0, doorRightZ);
					door_right_exterior.setOffset(doorRightX, 0, -doorRightZ);
					renderOnce(door_exterior, matrices, vertices, light, position);
					door_left_exterior.setOffset(doorLeftX, 0, doorLeftZ);
					door_right_exterior.setOffset(doorLeftX, 0, -doorLeftZ);
					renderOnceFlipped(door_exterior, matrices, vertices, light, position);
				}
				renderOnce(roof_exterior_door, matrices, vertices, light, position);
				if (isFirstDoor) {
					renderMirror(door_light_box, matrices, vertices, light, 0);
					if (!doorOpen) {
						renderMirror(door_light_off, matrices, vertices, light, 0);
					}
				}
				break;
		}
	}

	@Override
	protected void renderHeadPosition1(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean useHeadlights) {
		switch (renderStage) {
			case ALWAYS_ON_LIGHTS:
				renderOnceFlipped(useHeadlights ? headlights : tail_lights, matrices, vertices, light, position - 12.5F);
				break;
			case INTERIOR:
				renderOnceFlipped(head, matrices, vertices, light, position - 12.5F);
				break;
			case EXTERIOR:
				renderOnceFlipped(head_exterior, matrices, vertices, light, position - 12.5F);
				break;
		}
	}

	@Override
	protected void renderHeadPosition2(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean useHeadlights) {
		switch (renderStage) {
			case ALWAYS_ON_LIGHTS:
				renderOnce(useHeadlights ? headlights : tail_lights, matrices, vertices, light, position + 12.5F);
				break;
			case INTERIOR:
				renderOnce(head, matrices, vertices, light, position + 12.5F);
				break;
			case EXTERIOR:
				renderOnce(head_exterior, matrices, vertices, light, position + 12.5F);
				break;
		}
	}

	@Override
	protected void renderEndPosition1(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
		switch (renderStage) {
			case INTERIOR:
				renderOnceFlipped(end, matrices, vertices, light, position - 12.5F);
				break;
			case EXTERIOR:
				renderOnceFlipped(end_exterior, matrices, vertices, light, position - 12.5F);
				break;
		}
	}

	@Override
	protected void renderEndPosition2(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
		switch (renderStage) {
			case INTERIOR:
				renderOnce(end, matrices, vertices, light, position + 12.5F);
				break;
			case EXTERIOR:
				renderOnce(end_exterior, matrices, vertices, light, position + 12.5F);
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
	protected void renderTextDisplays(PoseStack matrices, MultiBufferSource vertexConsumers, Font font, MultiBufferSource.BufferSource immediate, Route thisRoute, Route nextRoute, Station thisStation, Station nextStation, Station lastStation, String customDestination, int car, int totalCars, boolean atPlatform, List<ScrollingText> scrollingTexts) {
		if (scrollingTexts.isEmpty()) {
			scrollingTexts.add(new ScrollingText(0.5F, 0.1F, 4, true));
			scrollingTexts.add(new ScrollingText(1.08F, 0.06F, 8, true));
		}
		final boolean isEnd1Head = car == 0;
		final boolean isEnd2Head = car == totalCars - 1;
		final float offset = 0.23F;
		final float[] positions1 = {-4 + 26.5F / 16 - offset, 4 + 26.5F / 16 - (isEnd2Head ? -1 : 1) * offset};
		final float[] positions2 = {-4 - 26.5F / 16 - offset, 4 - 26.5F / 16 - (isEnd2Head ? -1 : 1) * offset};

		final String destinationString = getDestinationString(lastStation, customDestination, TextSpacingType.NORMAL, false);
		scrollingTexts.get(0).changeImage(destinationString.isEmpty() ? null : ClientData.DATA_CACHE.getPixelatedText(destinationString.replace("|", " "), 0xFFFF9900, Integer.MAX_VALUE, 0, true));
		scrollingTexts.get(0).setVertexConsumer(vertexConsumers);

		for (final float position : positions1) {
			matrices.pushPose();
			matrices.translate(-21F / 16, -13F / 16, position);
			UtilitiesClient.rotateYDegrees(matrices, 90);
			UtilitiesClient.rotateXDegrees(matrices, -8);
			matrices.translate(-0.25F, -0.82F, -0.01F);
			scrollingTexts.get(0).scrollText(matrices);
			matrices.popPose();
		}
		for (final float position : positions2) {
			matrices.pushPose();
			matrices.translate(21F / 16, -13F / 16, position);
			UtilitiesClient.rotateYDegrees(matrices, -90);
			UtilitiesClient.rotateXDegrees(matrices, -8);
			matrices.translate(-0.25F, -0.82F, -0.01F);
			scrollingTexts.get(0).scrollText(matrices);
			matrices.popPose();
		}

		final String nextStationString = getLondonNextStationString(thisRoute, nextRoute, thisStation, nextStation, lastStation, destinationString, atPlatform);
		scrollingTexts.get(1).changeImage(nextStationString.isEmpty() ? null : ClientData.DATA_CACHE.getPixelatedText(nextStationString, 0xFFFF9900, Integer.MAX_VALUE, 0, true));
		scrollingTexts.get(1).setVertexConsumer(vertexConsumers);

		for (int i = 0; i < 2; i++) {
			matrices.pushPose();
			if (i == 1) {
				UtilitiesClient.rotateYDegrees(matrices, 180);
			}
			matrices.translate(-0.54F, -2.14F, (getEndPositions()[1] - (i == 1 && isEnd1Head || i == 0 && isEnd2Head ? 13 : 0)) / 16F - 0.01);
			scrollingTexts.get(1).scrollText(matrices);
			matrices.popPose();
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