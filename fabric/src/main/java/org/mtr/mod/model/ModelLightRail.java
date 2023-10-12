package org.mtr.mod.model;

import org.mtr.core.data.InterchangeColorsForStationName;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.ModelPartExtension;
import org.mtr.mod.client.DoorAnimationType;
import org.mtr.mod.client.DynamicTextureCache;
import org.mtr.mod.client.RouteMapGenerator;
import org.mtr.mod.client.ScrollingText;
import org.mtr.mod.render.RenderTrains;
import org.mtr.mod.render.StoredMatrixTransformations;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ModelLightRail extends ModelSimpleTrainBase<ModelLightRail> {

	private final ModelPartExtension window;
	private final ModelPartExtension window_exterior;
	private final ModelPartExtension door;
	private final ModelPartExtension door_left;
	private final ModelPartExtension door_right;
	private final ModelPartExtension door_5;
	private final ModelPartExtension door_left_5;
	private final ModelPartExtension door_right_5;
	private final ModelPartExtension door_handrails;
	private final ModelPartExtension handrail_6_r1;
	private final ModelPartExtension door_handrails_4;
	private final ModelPartExtension handrail_6_r2;
	private final ModelPartExtension handrail_5_r1;
	private final ModelPartExtension handrail_3_r1;
	private final ModelPartExtension handrail_2_r1;
	private final ModelPartExtension door_handrails_5;
	private final ModelPartExtension door_edge_8_r1;
	private final ModelPartExtension door_edge_7_r1;
	private final ModelPartExtension door_exterior;
	private final ModelPartExtension door_left_exterior;
	private final ModelPartExtension door_right_exterior;
	private final ModelPartExtension door_exterior_5;
	private final ModelPartExtension door_left_exterior_5;
	private final ModelPartExtension door_right_exterior_5;
	private final ModelPartExtension door_window;
	private final ModelPartExtension door_window_handrails;
	private final ModelPartExtension handrail_6_r3;
	private final ModelPartExtension door_window_handrails_4;
	private final ModelPartExtension handrail_12_r1;
	private final ModelPartExtension handrail_9_r1;
	private final ModelPartExtension handrail_7_r1;
	private final ModelPartExtension door_window_handrails_5;
	private final ModelPartExtension handrail_14_r1;
	private final ModelPartExtension handrail_8_r1;
	private final ModelPartExtension handrail_7_r2;
	private final ModelPartExtension handrail_6_r4;
	private final ModelPartExtension handrail_3_r2;
	private final ModelPartExtension handrail_2_r2;
	private final ModelPartExtension handrail_1_r1;
	private final ModelPartExtension door_window_exterior;
	private final ModelPartExtension roof;
	private final ModelPartExtension inner_roof_2_r1;
	private final ModelPartExtension roof_exterior;
	private final ModelPartExtension outer_roof_3_r1;
	private final ModelPartExtension outer_roof_2_r1;
	private final ModelPartExtension roof_end_exterior;
	private final ModelPartExtension bumper_2_r1;
	private final ModelPartExtension bumper_1_r1;
	private final ModelPartExtension outer_roof_6_r1;
	private final ModelPartExtension outer_roof_5_r1;
	private final ModelPartExtension outer_roof_3_r2;
	private final ModelPartExtension outer_roof_2_r2;
	private final ModelPartExtension roof_light;
	private final ModelPartExtension light_3_r1;
	private final ModelPartExtension light_1_r1;
	private final ModelPartExtension roof_light_5;
	private final ModelPartExtension end;
	private final ModelPartExtension inner_roof_4_r1;
	private final ModelPartExtension inner_roof_2_r2;
	private final ModelPartExtension back_r1;
	private final ModelPartExtension wall_diagonal_2_r1;
	private final ModelPartExtension wall_diagonal_1_r1;
	private final ModelPartExtension end_exterior;
	private final ModelPartExtension back_r2;
	private final ModelPartExtension wall_diagonal_2_r2;
	private final ModelPartExtension wall_diagonal_1_r2;
	private final ModelPartExtension head;
	private final ModelPartExtension head_exterior;
	private final ModelPartExtension wall_diagonal_2_r3;
	private final ModelPartExtension wall_diagonal_1_r3;
	private final ModelPartExtension head_exterior_1;
	private final ModelPartExtension front_r1;
	private final ModelPartExtension head_exterior_3_5;
	private final ModelPartExtension front_r2;
	private final ModelPartExtension head_exterior_4;
	private final ModelPartExtension front_middle_r1;
	private final ModelPartExtension front_top_r1;
	private final ModelPartExtension destination_board_r1;
	private final ModelPartExtension seat;
	private final ModelPartExtension back_right_r1;
	private final ModelPartExtension back_left_r1;
	private final ModelPartExtension seat_green;
	private final ModelPartExtension back_right_r2;
	private final ModelPartExtension seat_purple;
	private final ModelPartExtension back_right_r3;
	private final ModelPartExtension vents_top;
	private final ModelPartExtension headlights;
	private final ModelPartExtension tail_lights;
	private final ModelPartExtension side_display;
	private final ModelPartExtension bottom_r1;

	private final int phase;
	private final boolean isRHT;

	public ModelLightRail(int phase, boolean isRHT) {
		this(phase, isRHT, getDoorAnimationType(phase), true);
	}

	private ModelLightRail(int phase, boolean isRHT, DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		super(368, 368, doorAnimationType, renderDoorOverlay);
		this.phase = phase;
		this.isRHT = isRHT;

		final int textureWidth = 368;
		final int textureHeight = 368;

		window = createModelPart();
		window.setPivot(0, 24, 0);
		window.setTextureUVOffset(98, 0).addCuboid(-20, 0, -32, 20, 1, 64, 0, false);
		window.setTextureUVOffset(134, 134).addCuboid(-20, -32, -34, 2, 32, 68, 0, false);

		window_exterior = createModelPart();
		window_exterior.setPivot(0, 24, 0);
		window_exterior.setTextureUVOffset(0, 192).addCuboid(-20, 0, -32, 1, 7, 64, 0, false);
		window_exterior.setTextureUVOffset(0, 92).addCuboid(-20, -32, -34, 0, 32, 68, 0, false);

		door = createModelPart();
		door.setPivot(0, 24, 0);
		door.setTextureUVOffset(206, 65).addCuboid(-20, 0, -16, 20, 1, 32, 0, false);

		door_left = createModelPart();
		door_left.setPivot(0, 0, 0);
		door.addChild(door_left);
		door_left.setTextureUVOffset(168, 219).addCuboid(-20, -32, 0, 0, 32, 15, 0, false);

		door_right = createModelPart();
		door_right.setPivot(0, 0, 0);
		door.addChild(door_right);
		door_right.setTextureUVOffset(206, 116).addCuboid(-20, -32, -15, 0, 32, 15, 0, false);

		door_5 = createModelPart();
		door_5.setPivot(0, 24, 0);
		door_5.setTextureUVOffset(206, 65).addCuboid(-20, 0, -16, 20, 1, 32, 0, false);

		door_left_5 = createModelPart();
		door_left_5.setPivot(0, 0, 0);
		door_5.addChild(door_left_5);
		door_left_5.setTextureUVOffset(167, 218).addCuboid(-20, -32, 0, 0, 32, 16, 0, false);

		door_right_5 = createModelPart();
		door_right_5.setPivot(0, 0, 0);
		door_5.addChild(door_right_5);
		door_right_5.setTextureUVOffset(205, 115).addCuboid(-20, -32, -16, 0, 32, 16, 0, false);

		door_handrails = createModelPart();
		door_handrails.setPivot(0, 24, 0);
		door_handrails.setTextureUVOffset(120, 0).addCuboid(-18, -15, 16, 5, 13, 0, 0, false);
		door_handrails.setTextureUVOffset(120, 0).addCuboid(-18, -15, -16, 5, 13, 0, 0, false);
		door_handrails.setTextureUVOffset(0, 0).addCuboid(-13, -36, 16, 0, 36, 0, 0.2F, false);
		door_handrails.setTextureUVOffset(0, 0).addCuboid(-13, -36, -16, 0, 36, 0, 0.2F, false);
		door_handrails.setTextureUVOffset(4, 0).addCuboid(-9, -36, 0, 0, 36, 0, 0.2F, false);
		door_handrails.setTextureUVOffset(0, 75).addCuboid(-15, -32, 20, 4, 5, 0, 0, false);
		door_handrails.setTextureUVOffset(0, 75).addCuboid(-15, -32, 28, 4, 5, 0, 0, false);
		door_handrails.setTextureUVOffset(0, 75).addCuboid(-15, -32, 36, 4, 5, 0, 0, false);
		door_handrails.setTextureUVOffset(0, 75).addCuboid(-15, -32, 44, 4, 5, 0, 0, false);
		door_handrails.setTextureUVOffset(0, 75).addCuboid(-15, -32, -44, 4, 5, 0, 0, false);
		door_handrails.setTextureUVOffset(0, 75).addCuboid(-15, -32, -36, 4, 5, 0, 0, false);
		door_handrails.setTextureUVOffset(0, 75).addCuboid(-15, -32, -28, 4, 5, 0, 0, false);
		door_handrails.setTextureUVOffset(0, 75).addCuboid(-15, -32, -20, 4, 5, 0, 0, false);

		handrail_6_r1 = createModelPart();
		handrail_6_r1.setPivot(0, 0, 0);
		door_handrails.addChild(handrail_6_r1);
		setRotationAngle(handrail_6_r1, -1.5708F, 0, 0);
		handrail_6_r1.setTextureUVOffset(8, 0).addCuboid(-13, 16, -32, 0, 32, 0, 0.2F, false);
		handrail_6_r1.setTextureUVOffset(8, 0).addCuboid(-13, -48, -32, 0, 32, 0, 0.2F, false);

		door_handrails_4 = createModelPart();
		door_handrails_4.setPivot(0, 24, 0);
		door_handrails_4.setTextureUVOffset(4, 6).addCuboid(-15.8F, -17, -15, 0, 4, 0, 0.2F, false);
		door_handrails_4.setTextureUVOffset(4, 6).addCuboid(-15.8F, -17, 15, 0, 4, 0, 0.2F, false);
		door_handrails_4.setTextureUVOffset(0, 75).addCuboid(-5, -32, -12, 4, 5, 0, 0, false);
		door_handrails_4.setTextureUVOffset(0, 75).addCuboid(-5, -32, -4, 4, 5, 0, 0, false);
		door_handrails_4.setTextureUVOffset(0, 75).addCuboid(-5, -32, 4, 4, 5, 0, 0, false);
		door_handrails_4.setTextureUVOffset(0, 75).addCuboid(-5, -32, 12, 4, 5, 0, 0, false);
		door_handrails_4.setTextureUVOffset(0, 75).addCuboid(-5, -32, 20, 4, 5, 0, 0, false);
		door_handrails_4.setTextureUVOffset(0, 75).addCuboid(-5, -32, 28, 4, 5, 0, 0, false);
		door_handrails_4.setTextureUVOffset(0, 75).addCuboid(-5, -32, 36, 4, 5, 0, 0, false);
		door_handrails_4.setTextureUVOffset(0, 75).addCuboid(-5, -32, 44, 4, 5, 0, 0, false);
		door_handrails_4.setTextureUVOffset(0, 75).addCuboid(-5, -32, -44, 4, 5, 0, 0, false);
		door_handrails_4.setTextureUVOffset(0, 75).addCuboid(-5, -32, -36, 4, 5, 0, 0, false);
		door_handrails_4.setTextureUVOffset(0, 75).addCuboid(-5, -32, -28, 4, 5, 0, 0, false);
		door_handrails_4.setTextureUVOffset(0, 75).addCuboid(-5, -32, -20, 4, 5, 0, 0, false);
		door_handrails_4.setTextureUVOffset(358, 0).addCuboid(-20, -32, 14, 3, 32, 2, 0, false);
		door_handrails_4.setTextureUVOffset(346, 0).addCuboid(-20, -32, -16, 3, 32, 2, 0, false);

		handrail_6_r2 = createModelPart();
		handrail_6_r2.setPivot(-15.6F, -17.2F, 14.8F);
		door_handrails_4.addChild(handrail_6_r2);
		setRotationAngle(handrail_6_r2, 0, 0, -0.3491F);
		handrail_6_r2.setTextureUVOffset(4, 6).addCuboid(-0.2F, -5.2F, 0.2F, 0, 5, 0, 0.2F, false);

		handrail_5_r1 = createModelPart();
		handrail_5_r1.setPivot(-15.6F, -12.8F, 14.8F);
		door_handrails_4.addChild(handrail_5_r1);
		setRotationAngle(handrail_5_r1, 0, 0, 0.3491F);
		handrail_5_r1.setTextureUVOffset(4, 6).addCuboid(-0.2F, 0.2F, 0.2F, 0, 5, 0, 0.2F, false);

		handrail_3_r1 = createModelPart();
		handrail_3_r1.setPivot(-15.6F, -17.2F, -15.2F);
		door_handrails_4.addChild(handrail_3_r1);
		setRotationAngle(handrail_3_r1, 0, 0, -0.3491F);
		handrail_3_r1.setTextureUVOffset(4, 6).addCuboid(-0.2F, -5.2F, 0.2F, 0, 5, 0, 0.2F, false);

		handrail_2_r1 = createModelPart();
		handrail_2_r1.setPivot(-15.6F, -12.8F, -15.2F);
		door_handrails_4.addChild(handrail_2_r1);
		setRotationAngle(handrail_2_r1, 0, 0, 0.3491F);
		handrail_2_r1.setTextureUVOffset(4, 6).addCuboid(-0.2F, 0.2F, 0.2F, 0, 5, 0, 0.2F, false);

		door_handrails_5 = createModelPart();
		door_handrails_5.setPivot(0, 24, 0);
		door_handrails_5.setTextureUVOffset(0, 3).addCuboid(-17.25F, -26, 14.25F, 0, 19, 0, 0.2F, false);
		door_handrails_5.setTextureUVOffset(0, 3).addCuboid(-17.25F, -26, -14.25F, 0, 19, 0, 0.2F, false);
		door_handrails_5.setTextureUVOffset(0, 75).addCuboid(-5, -32.5F, -12, 4, 5, 0, 0, false);
		door_handrails_5.setTextureUVOffset(0, 75).addCuboid(-5, -32.5F, -4, 4, 5, 0, 0, false);
		door_handrails_5.setTextureUVOffset(0, 75).addCuboid(-5, -32.5F, 4, 4, 5, 0, 0, false);
		door_handrails_5.setTextureUVOffset(0, 75).addCuboid(-5, -32.5F, 12, 4, 5, 0, 0, false);
		door_handrails_5.setTextureUVOffset(0, 75).addCuboid(-5, -32.5F, 20, 4, 5, 0, 0, false);
		door_handrails_5.setTextureUVOffset(0, 75).addCuboid(-5, -32.5F, 28, 4, 5, 0, 0, false);
		door_handrails_5.setTextureUVOffset(0, 75).addCuboid(-5, -32.5F, 36, 4, 5, 0, 0, false);
		door_handrails_5.setTextureUVOffset(0, 75).addCuboid(-5, -32.5F, 44, 4, 5, 0, 0, false);
		door_handrails_5.setTextureUVOffset(0, 75).addCuboid(-5, -32.5F, -44, 4, 5, 0, 0, false);
		door_handrails_5.setTextureUVOffset(0, 75).addCuboid(-5, -32.5F, -36, 4, 5, 0, 0, false);
		door_handrails_5.setTextureUVOffset(0, 75).addCuboid(-5, -32.5F, -28, 4, 5, 0, 0, false);
		door_handrails_5.setTextureUVOffset(0, 75).addCuboid(-5, -32.5F, -20, 4, 5, 0, 0, false);
		door_handrails_5.setTextureUVOffset(358, 0).addCuboid(-20, -32, 14, 3, 32, 2, 0, false);
		door_handrails_5.setTextureUVOffset(346, 0).addCuboid(-20, -32, -16, 3, 32, 2, 0, false);
		door_handrails_5.setTextureUVOffset(361, 38).addCuboid(-19, -7, -16, 2, 0, 2, 0, false);
		door_handrails_5.setTextureUVOffset(363, 64).addCuboid(-18.25F, -26, -15.25F, 2, 19, 0, 0, false);
		door_handrails_5.setTextureUVOffset(361, 38).addCuboid(-19, -7, 13.25F, 2, 0, 2, 0, false);

		door_edge_8_r1 = createModelPart();
		door_edge_8_r1.setPivot(0, 0, 0);
		door_handrails_5.addChild(door_edge_8_r1);
		setRotationAngle(door_edge_8_r1, 0, 3.1416F, 0);
		door_edge_8_r1.setTextureUVOffset(363, 64).addCuboid(16.75F, -26, -15.25F, 2, 19, 0, 0, false);

		door_edge_7_r1 = createModelPart();
		door_edge_7_r1.setPivot(0, 0, 0);
		door_handrails_5.addChild(door_edge_7_r1);
		setRotationAngle(door_edge_7_r1, 0, 0, -3.1416F);
		door_edge_7_r1.setTextureUVOffset(361, 38).addCuboid(16, 26, 13.25F, 2, 0, 2, 0, false);
		door_edge_7_r1.setTextureUVOffset(361, 38).addCuboid(16, 26, -16, 2, 0, 2, 0, false);

		door_exterior = createModelPart();
		door_exterior.setPivot(0, 24, 0);
		door_exterior.setTextureUVOffset(278, 29).addCuboid(-20, 0, -16, 1, 7, 32, 0, false);
		door_exterior.setTextureUVOffset(0, 0).addCuboid(-21, -34, -48, 1, 2, 96, 0, false);

		door_left_exterior = createModelPart();
		door_left_exterior.setPivot(0, 0, 0);
		door_exterior.addChild(door_left_exterior);
		door_left_exterior.setTextureUVOffset(234, 311).addCuboid(-21, -32, 0, 1, 33, 15, 0, false);

		door_right_exterior = createModelPart();
		door_right_exterior.setPivot(0, 0, 0);
		door_exterior.addChild(door_right_exterior);
		door_right_exterior.setTextureUVOffset(202, 300).addCuboid(-21, -32, -15, 1, 33, 15, 0, false);

		door_exterior_5 = createModelPart();
		door_exterior_5.setPivot(0, 24, 0);
		door_exterior_5.setTextureUVOffset(278, 29).addCuboid(-20, 0, -16, 1, 7, 32, 0, false);
		door_exterior_5.setTextureUVOffset(0, 0).addCuboid(-21, -34, -48, 1, 2, 96, 0, false);

		door_left_exterior_5 = createModelPart();
		door_left_exterior_5.setPivot(0, 0, 0);
		door_exterior_5.addChild(door_left_exterior_5);
		door_left_exterior_5.setTextureUVOffset(242, 311).addCuboid(-21, -32, 0, 1, 33, 16, 0, false);

		door_right_exterior_5 = createModelPart();
		door_right_exterior_5.setPivot(0, 0, 0);
		door_exterior_5.addChild(door_right_exterior_5);
		door_right_exterior_5.setTextureUVOffset(202, 300).addCuboid(-21, -32, -16, 1, 33, 16, 0, false);

		door_window = createModelPart();
		door_window.setPivot(0, 24, 0);
		door_window.setTextureUVOffset(202, 28).addCuboid(0, 0, -16, 20, 1, 32, 0, false);
		door_window.setTextureUVOffset(0, 98).addCuboid(18, -32, -14, 2, 32, 28, 0, true);

		door_window_handrails = createModelPart();
		door_window_handrails.setPivot(0, 24, 0);
		door_window_handrails.setTextureUVOffset(32, 98).addCuboid(7, -15, 16, 11, 13, 0, 0, false);
		door_window_handrails.setTextureUVOffset(32, 98).addCuboid(7, -15, -16, 11, 13, 0, 0, false);
		door_window_handrails.setTextureUVOffset(0, 0).addCuboid(7, -36, 16, 0, 36, 0, 0.2F, false);
		door_window_handrails.setTextureUVOffset(0, 0).addCuboid(7, -36, -16, 0, 36, 0, 0.2F, false);
		door_window_handrails.setTextureUVOffset(4, 0).addCuboid(7, -36, 0, 0, 36, 0, 0.2F, false);
		door_window_handrails.setTextureUVOffset(0, 75).addCuboid(5, -32, 12, 4, 5, 0, 0, false);
		door_window_handrails.setTextureUVOffset(0, 75).addCuboid(5, -32, 4, 4, 5, 0, 0, false);
		door_window_handrails.setTextureUVOffset(0, 75).addCuboid(5, -32, -4, 4, 5, 0, 0, false);
		door_window_handrails.setTextureUVOffset(0, 75).addCuboid(5, -32, -12, 4, 5, 0, 0, false);
		door_window_handrails.setTextureUVOffset(0, 75).addCuboid(5, -32, 20, 4, 5, 0, 0, false);
		door_window_handrails.setTextureUVOffset(0, 75).addCuboid(5, -32, 28, 4, 5, 0, 0, false);
		door_window_handrails.setTextureUVOffset(0, 75).addCuboid(5, -32, 36, 4, 5, 0, 0, false);
		door_window_handrails.setTextureUVOffset(0, 75).addCuboid(5, -32, 44, 4, 5, 0, 0, false);
		door_window_handrails.setTextureUVOffset(0, 75).addCuboid(5, -32, -44, 4, 5, 0, 0, false);
		door_window_handrails.setTextureUVOffset(0, 75).addCuboid(5, -32, -36, 4, 5, 0, 0, false);
		door_window_handrails.setTextureUVOffset(0, 75).addCuboid(5, -32, -28, 4, 5, 0, 0, false);
		door_window_handrails.setTextureUVOffset(0, 75).addCuboid(5, -32, -20, 4, 5, 0, 0, false);

		handrail_6_r3 = createModelPart();
		handrail_6_r3.setPivot(0, 0, 0);
		door_window_handrails.addChild(handrail_6_r3);
		setRotationAngle(handrail_6_r3, -1.5708F, 0, 0);
		handrail_6_r3.setTextureUVOffset(8, 0).addCuboid(7, 16, -32, 0, 32, 0, 0.2F, false);
		handrail_6_r3.setTextureUVOffset(8, 0).addCuboid(7, -48, -32, 0, 32, 0, 0.2F, false);
		handrail_6_r3.setTextureUVOffset(8, 0).addCuboid(7, -16, -32, 0, 32, 0, 0.2F, false);

		door_window_handrails_4 = createModelPart();
		door_window_handrails_4.setPivot(0, 24, 0);
		door_window_handrails_4.setTextureUVOffset(0, 98).addCuboid(5, -13, 16, 13, 11, 0, 0, false);
		door_window_handrails_4.setTextureUVOffset(0, 98).addCuboid(5, -13, -16, 13, 11, 0, 0, false);
		door_window_handrails_4.setTextureUVOffset(0, 9).addCuboid(5.6F, -27.25F, -16, 0, 15, 0, 0.2F, false);
		door_window_handrails_4.setTextureUVOffset(0, 9).addCuboid(5.6F, -27.25F, 16, 0, 15, 0, 0.2F, false);
		door_window_handrails_4.setTextureUVOffset(0, 75).addCuboid(4.4F, -32, 8, 4, 5, 0, 0, false);
		door_window_handrails_4.setTextureUVOffset(0, 75).addCuboid(4.4F, -32, 0, 4, 5, 0, 0, false);
		door_window_handrails_4.setTextureUVOffset(0, 75).addCuboid(4.4F, -32, -8, 4, 5, 0, 0, false);

		handrail_12_r1 = createModelPart();
		handrail_12_r1.setPivot(0, 0, 0);
		door_window_handrails_4.addChild(handrail_12_r1);
		setRotationAngle(handrail_12_r1, -1.5708F, 0, 0);
		handrail_12_r1.setTextureUVOffset(8, 0).addCuboid(-3, -48, -32, 0, 32, 0, 0.2F, false);
		handrail_12_r1.setTextureUVOffset(8, 0).addCuboid(-3, -16, -32, 0, 32, 0, 0.2F, false);
		handrail_12_r1.setTextureUVOffset(8, 0).addCuboid(-3, 16, -32, 0, 32, 0, 0.2F, false);
		handrail_12_r1.setTextureUVOffset(8, 0).addCuboid(6.4F, -16, -32, 0, 32, 0, 0.2F, false);

		handrail_9_r1 = createModelPart();
		handrail_9_r1.setPivot(6, -28.25F, -16);
		door_window_handrails_4.addChild(handrail_9_r1);
		setRotationAngle(handrail_9_r1, 0, 0, 0.1745F);
		handrail_9_r1.setTextureUVOffset(0, 30).addCuboid(-0.25F, -8.25F, 32, 0, 9, 0, 0.2F, false);
		handrail_9_r1.setTextureUVOffset(0, 30).addCuboid(-0.25F, -8.25F, 0, 0, 9, 0, 0.2F, false);

		handrail_7_r1 = createModelPart();
		handrail_7_r1.setPivot(6, -3.5F, -16);
		door_window_handrails_4.addChild(handrail_7_r1);
		setRotationAngle(handrail_7_r1, 0, 0, -0.2182F);
		handrail_7_r1.setTextureUVOffset(0, 30).addCuboid(1.5F, -8.25F, 32, 0, 12, 0, 0.2F, false);
		handrail_7_r1.setTextureUVOffset(0, 30).addCuboid(1.5F, -8.25F, 0, 0, 12, 0, 0.2F, false);

		door_window_handrails_5 = createModelPart();
		door_window_handrails_5.setPivot(0, 24, 0);
		door_window_handrails_5.setTextureUVOffset(0, 98).addCuboid(5, -12.75F, 16, 13, 13, 0, 0, false);
		door_window_handrails_5.setTextureUVOffset(0, 98).addCuboid(5, -12.75F, -16, 13, 13, 0, 0, false);
		door_window_handrails_5.setTextureUVOffset(0, 30).addCuboid(5.6F, -13, -16, 0, 11, 0, 0.2F, false);
		door_window_handrails_5.setTextureUVOffset(0, 30).addCuboid(5.6F, -13, 16, 0, 11, 0, 0.2F, false);
		door_window_handrails_5.setTextureUVOffset(0, 30).addCuboid(9.2F, -36.2738F, -11.5F, 0, 5, 0, 0.2F, false);
		door_window_handrails_5.setTextureUVOffset(0, 30).addCuboid(9.2F, -36.2738F, 11.5F, 0, 5, 0, 0.2F, false);
		door_window_handrails_5.setTextureUVOffset(0, 75).addCuboid(6.9F, -31.75F, 10, 4, 5, 0, 0, false);
		door_window_handrails_5.setTextureUVOffset(0, 75).addCuboid(7.15F, -31.75F, 3.5F, 4, 5, 0, 0, false);
		door_window_handrails_5.setTextureUVOffset(0, 75).addCuboid(7.15F, -31.75F, -3.5F, 4, 5, 0, 0, false);
		door_window_handrails_5.setTextureUVOffset(0, 75).addCuboid(7.15F, -31.75F, -10, 4, 5, 0, 0, false);
		door_window_handrails_5.setTextureUVOffset(32, 94).addCuboid(17.99F, -31.5F, -18, 0, 18, 4, 0, false);
		door_window_handrails_5.setTextureUVOffset(32, 94).addCuboid(17.99F, -31.5F, 14, 0, 18, 4, 0, false);

		handrail_14_r1 = createModelPart();
		handrail_14_r1.setPivot(0, 0, 0);
		door_window_handrails_5.addChild(handrail_14_r1);
		setRotationAngle(handrail_14_r1, -1.5708F, 0, 0);
		handrail_14_r1.setTextureUVOffset(8, 0).addCuboid(-3, -48, -32, 0, 32, 0, 0.2F, false);
		handrail_14_r1.setTextureUVOffset(8, 0).addCuboid(-3, -16, -32, 0, 32, 0, 0.2F, false);
		handrail_14_r1.setTextureUVOffset(8, 0).addCuboid(-3, 16, -32, 0, 32, 0, 0.2F, false);
		handrail_14_r1.setTextureUVOffset(8, 0).addCuboid(9.15F, -16, -31.25F, 0, 32, 0, 0.2F, false);

		handrail_8_r1 = createModelPart();
		handrail_8_r1.setPivot(5.9238F, -16.714F, 16);
		door_window_handrails_5.addChild(handrail_8_r1);
		setRotationAngle(handrail_8_r1, 0, 0, 0.0873F);
		handrail_8_r1.setTextureUVOffset(0, 9).addCuboid(0, -3.5F, 0, 0, 7, 0, 0.2F, false);

		handrail_7_r2 = createModelPart();
		handrail_7_r2.setPivot(8.5226F, -29.7676F, 16);
		door_window_handrails_5.addChild(handrail_7_r2);
		setRotationAngle(handrail_7_r2, 0, 0, 0.3491F);
		handrail_7_r2.setTextureUVOffset(0, 3).addCuboid(0, -1.5F, 0, 0, 3, 0, 0.2F, false);

		handrail_6_r4 = createModelPart();
		handrail_6_r4.setPivot(7.0994F, -24.3136F, 16);
		door_window_handrails_5.addChild(handrail_6_r4);
		setRotationAngle(handrail_6_r4, 0, 0, 0.2182F);
		handrail_6_r4.setTextureUVOffset(0, 9).addCuboid(0, -4, 0, 0, 8, 0, 0.2F, false);

		handrail_3_r2 = createModelPart();
		handrail_3_r2.setPivot(8.5935F, -30.2738F, -16);
		door_window_handrails_5.addChild(handrail_3_r2);
		setRotationAngle(handrail_3_r2, 0, 0, 0.3491F);
		handrail_3_r2.setTextureUVOffset(0, 3).addCuboid(0.1065F, -1, 0, 0, 3, 0, 0.2F, false);

		handrail_2_r2 = createModelPart();
		handrail_2_r2.setPivot(5.6F, -18.75F, -16);
		door_window_handrails_5.addChild(handrail_2_r2);
		setRotationAngle(handrail_2_r2, 0, 0, 0.0873F);
		handrail_2_r2.setTextureUVOffset(0, 9).addCuboid(0.5F, -1.5F, 0, 0, 7, 0, 0.2F, false);

		handrail_1_r1 = createModelPart();
		handrail_1_r1.setPivot(7.0761F, -24.3188F, -16);
		door_window_handrails_5.addChild(handrail_1_r1);
		setRotationAngle(handrail_1_r1, 0, 0, 0.2182F);
		handrail_1_r1.setTextureUVOffset(0, 9).addCuboid(0.0239F, -4, 0, 0, 8, 0, 0.2F, false);

		door_window_exterior = createModelPart();
		door_window_exterior.setPivot(0, 24, 0);
		door_window_exterior.setTextureUVOffset(36, 263).addCuboid(19, 0, -16, 1, 7, 32, 0, true);
		door_window_exterior.setTextureUVOffset(98, 0).addCuboid(20, -32, -14, 0, 32, 28, 0, true);

		roof = createModelPart();
		roof.setPivot(0, 24, 0);
		roof.setTextureUVOffset(122, 0).addCuboid(-20, -32, -16, 3, 0, 32, 0, false);
		roof.setTextureUVOffset(36, 36).addCuboid(-14, -36, -16, 14, 0, 32, 0, false);

		inner_roof_2_r1 = createModelPart();
		inner_roof_2_r1.setPivot(-17, -32, 0);
		roof.addChild(inner_roof_2_r1);
		setRotationAngle(inner_roof_2_r1, 0, 0, -0.8727F);
		inner_roof_2_r1.setTextureUVOffset(109, 98).addCuboid(0, 0, -16, 6, 0, 32, 0, false);

		roof_exterior = createModelPart();
		roof_exterior.setPivot(0, 24, 0);
		roof_exterior.setTextureUVOffset(0, 39).addCuboid(-20, -36, -16, 0, 4, 32, 0, false);
		roof_exterior.setTextureUVOffset(242, 242).addCuboid(-17, -39, -16, 17, 1, 32, 0, false);

		outer_roof_3_r1 = createModelPart();
		outer_roof_3_r1.setPivot(-19, -37.7329F, 0);
		roof_exterior.addChild(outer_roof_3_r1);
		setRotationAngle(outer_roof_3_r1, 0, 0, -0.5236F);
		outer_roof_3_r1.setTextureUVOffset(121, 98).addCuboid(0, 0.001F, -16, 3, 0, 32, 0, false);

		outer_roof_2_r1 = createModelPart();
		outer_roof_2_r1.setPivot(-20, -36, 0);
		roof_exterior.addChild(outer_roof_2_r1);
		setRotationAngle(outer_roof_2_r1, 0, 0, -1.0472F);
		outer_roof_2_r1.setTextureUVOffset(60, 0).addCuboid(0, 0, -16, 2, 0, 32, 0, false);

		roof_end_exterior = createModelPart();
		roof_end_exterior.setPivot(0, 24, 0);
		roof_end_exterior.setTextureUVOffset(202, 0).addCuboid(-17, -39, 16, 34, 1, 27, 0, false);
		roof_end_exterior.setTextureUVOffset(266, 138).addCuboid(-10, -2, 43, 20, 3, 5, 0, false);

		bumper_2_r1 = createModelPart();
		bumper_2_r1.setPivot(20, 0, 16);
		roof_end_exterior.addChild(bumper_2_r1);
		setRotationAngle(bumper_2_r1, 0, -0.3491F, 0);
		bumper_2_r1.setTextureUVOffset(202, 28).addCuboid(0, -2, 23, 1, 3, 10, 0, true);
		bumper_2_r1.setTextureUVOffset(0, 225).addCuboid(0, 0, 0, 0, 7, 23, 0, true);

		bumper_1_r1 = createModelPart();
		bumper_1_r1.setPivot(-20, 0, 16);
		roof_end_exterior.addChild(bumper_1_r1);
		setRotationAngle(bumper_1_r1, 0, 0.3491F, 0);
		bumper_1_r1.setTextureUVOffset(202, 28).addCuboid(-1, -2, 23, 1, 3, 10, 0, false);
		bumper_1_r1.setTextureUVOffset(0, 225).addCuboid(0, 0, 0, 0, 7, 23, 0, false);

		outer_roof_6_r1 = createModelPart();
		outer_roof_6_r1.setPivot(20, -36, 16);
		roof_end_exterior.addChild(outer_roof_6_r1);
		setRotationAngle(outer_roof_6_r1, -0.2967F, 0, 1.0472F);
		outer_roof_6_r1.setTextureUVOffset(69, 65).addCuboid(-7, 0, 0, 7, 0, 29, 0, true);

		outer_roof_5_r1 = createModelPart();
		outer_roof_5_r1.setPivot(18.981F, -37.6982F, 16.0159F);
		roof_end_exterior.addChild(outer_roof_5_r1);
		setRotationAngle(outer_roof_5_r1, -0.1745F, 0, 0.5236F);
		outer_roof_5_r1.setTextureUVOffset(70, 0).addCuboid(-11, -0.0354F, -0.0226F, 11, 0, 28, 0, true);

		outer_roof_3_r2 = createModelPart();
		outer_roof_3_r2.setPivot(-18.981F, -37.6982F, 16.0159F);
		roof_end_exterior.addChild(outer_roof_3_r2);
		setRotationAngle(outer_roof_3_r2, -0.1745F, 0, -0.5236F);
		outer_roof_3_r2.setTextureUVOffset(70, 0).addCuboid(0, -0.0354F, -0.0226F, 11, 0, 28, 0, false);

		outer_roof_2_r2 = createModelPart();
		outer_roof_2_r2.setPivot(-20, -36, 16);
		roof_end_exterior.addChild(outer_roof_2_r2);
		setRotationAngle(outer_roof_2_r2, -0.2967F, 0, -1.0472F);
		outer_roof_2_r2.setTextureUVOffset(69, 65).addCuboid(0, 0, 0, 7, 0, 29, 0, false);

		roof_light = createModelPart();
		roof_light.setPivot(0, 24, 0);
		roof_light.setTextureUVOffset(122, 32).addCuboid(-12, -35.5F, -16, 2, 0, 32, 0, false);

		light_3_r1 = createModelPart();
		light_3_r1.setPivot(-10, -35.5F, 0);
		roof_light.addChild(light_3_r1);
		setRotationAngle(light_3_r1, 0, 0, -1.0472F);
		light_3_r1.setTextureUVOffset(126, 32).addCuboid(0, 0, -16, 1, 0, 32, 0, false);

		light_1_r1 = createModelPart();
		light_1_r1.setPivot(-12, -35.5F, 0);
		roof_light.addChild(light_1_r1);
		setRotationAngle(light_1_r1, 0, 0, 1.0472F);
		light_1_r1.setTextureUVOffset(127, 98).addCuboid(-1, 0, -16, 1, 0, 32, 0, false);

		roof_light_5 = createModelPart();
		roof_light_5.setPivot(0, 24, 0);
		roof_light_5.setTextureUVOffset(122, 32).addCuboid(-12, -36.001F, -16, 2, 0, 32, 0, false);

		end = createModelPart();
		end.setPivot(0, 24, 0);
		end.setTextureUVOffset(0, 98).addCuboid(-20, 0, -16, 40, 1, 61, 0, false);
		end.setTextureUVOffset(168, 234).addCuboid(-20, -32, -18, 2, 32, 34, 0, false);
		end.setTextureUVOffset(168, 234).addCuboid(18, -32, -18, 2, 32, 34, 0, true);
		end.setTextureUVOffset(278, 68).addCuboid(-8, -9, 44, 16, 9, 0, 0, false);
		end.setTextureUVOffset(21, 25).addCuboid(-20, -32, 16, 3, 0, 3, 0, false);
		end.setTextureUVOffset(9, 0).addCuboid(-14, -36, 16, 28, 0, 27, 0, false);
		end.setTextureUVOffset(21, 25).addCuboid(17, -32, 16, 3, 0, 3, 0, true);

		inner_roof_4_r1 = createModelPart();
		inner_roof_4_r1.setPivot(17, -32, 0);
		end.addChild(inner_roof_4_r1);
		setRotationAngle(inner_roof_4_r1, 0, 0, 0.8727F);
		inner_roof_4_r1.setTextureUVOffset(0, 0).addCuboid(-6, 0, 16, 6, 0, 12, 0, true);

		inner_roof_2_r2 = createModelPart();
		inner_roof_2_r2.setPivot(-17, -32, 0);
		end.addChild(inner_roof_2_r2);
		setRotationAngle(inner_roof_2_r2, 0, 0, -0.8727F);
		inner_roof_2_r2.setTextureUVOffset(0, 0).addCuboid(0, 0, 16, 6, 0, 12, 0, false);

		back_r1 = createModelPart();
		back_r1.setPivot(0, -9, 46);
		end.addChild(back_r1);
		setRotationAngle(back_r1, 0.0873F, 0, 0);
		back_r1.setTextureUVOffset(274, 28).addCuboid(-9, -30, -2, 18, 30, 0, 0, false);

		wall_diagonal_2_r1 = createModelPart();
		wall_diagonal_2_r1.setPivot(20, 0, 16);
		end.addChild(wall_diagonal_2_r1);
		setRotationAngle(wall_diagonal_2_r1, 0, -0.3491F, 0);
		wall_diagonal_2_r1.setTextureUVOffset(0, 160).addCuboid(-2, -36, 0, 0, 36, 32, 0, true);

		wall_diagonal_1_r1 = createModelPart();
		wall_diagonal_1_r1.setPivot(-20, 0, 16);
		end.addChild(wall_diagonal_1_r1);
		setRotationAngle(wall_diagonal_1_r1, 0, 0.3491F, 0);
		wall_diagonal_1_r1.setTextureUVOffset(0, 160).addCuboid(2, -36, 0, 0, 36, 32, 0, false);

		end_exterior = createModelPart();
		end_exterior.setPivot(0, 24, 0);
		end_exterior.setTextureUVOffset(36, 263).addCuboid(-20, 0, -16, 1, 7, 32, 0, false);
		end_exterior.setTextureUVOffset(36, 263).addCuboid(19, 0, -16, 1, 7, 32, 0, true);
		end_exterior.setTextureUVOffset(66, 158).addCuboid(-20, -32, -18, 0, 32, 34, 0, false);
		end_exterior.setTextureUVOffset(66, 158).addCuboid(20, -32, -18, 0, 32, 34, 0, true);
		end_exterior.setTextureUVOffset(266, 146).addCuboid(-10, -9, 46, 20, 9, 0, 0, false);

		back_r2 = createModelPart();
		back_r2.setPivot(0, -9, 46);
		end_exterior.addChild(back_r2);
		setRotationAngle(back_r2, 0.0873F, 0, 0);
		back_r2.setTextureUVOffset(0, 334).addCuboid(-11, -30, -1, 22, 30, 1, 0, false);

		wall_diagonal_2_r2 = createModelPart();
		wall_diagonal_2_r2.setPivot(20, 0, 16);
		end_exterior.addChild(wall_diagonal_2_r2);
		setRotationAngle(wall_diagonal_2_r2, 0, -0.3491F, 0);
		wall_diagonal_2_r2.setTextureUVOffset(136, 128).addCuboid(0, -37, 0, 0, 37, 32, 0, true);

		wall_diagonal_1_r2 = createModelPart();
		wall_diagonal_1_r2.setPivot(-20, 0, 16);
		end_exterior.addChild(wall_diagonal_1_r2);
		setRotationAngle(wall_diagonal_1_r2, 0, 0.3491F, 0);
		wall_diagonal_1_r2.setTextureUVOffset(136, 128).addCuboid(0, -37, 0, 0, 37, 32, 0, false);

		head = createModelPart();
		head.setPivot(0, 24, 0);
		head.setTextureUVOffset(141, 98).addCuboid(-20, 0, -16, 40, 1, 32, 0, false);
		head.setTextureUVOffset(206, 131).addCuboid(-20, -32, -16, 2, 32, 34, 0, false);
		head.setTextureUVOffset(96, 229).addCuboid(18, -32, -16, 2, 32, 34, 0, true);
		head.setTextureUVOffset(274, 204).addCuboid(-20, -36, -16, 40, 36, 0, 0, false);

		head_exterior = createModelPart();
		head_exterior.setPivot(0, 24, 0);
		head_exterior.setTextureUVOffset(98, 65).addCuboid(-20, 0, -46, 40, 1, 30, 0, false);
		head_exterior.setTextureUVOffset(136, 300).addCuboid(-20, 0, -16, 1, 7, 32, 0, false);
		head_exterior.setTextureUVOffset(136, 300).addCuboid(19, 0, -16, 1, 7, 32, 0, true);
		head_exterior.setTextureUVOffset(0, 229).addCuboid(-20, -32, -16, 0, 32, 34, 0, false);
		head_exterior.setTextureUVOffset(206, 200).addCuboid(20, -32, -16, 0, 32, 34, 0, true);
		head_exterior.setTextureUVOffset(240, 275).addCuboid(-20, -36, -16, 40, 36, 0, 0, false);
		head_exterior.setTextureUVOffset(22, 228).addCuboid(-10, -14, -46, 20, 14, 1, 0, false);
		head_exterior.setTextureUVOffset(0, 302).addCuboid(-18, -36, -38, 36, 0, 22, 0, false);

		wall_diagonal_2_r3 = createModelPart();
		wall_diagonal_2_r3.setPivot(20, 0, -16);
		head_exterior.addChild(wall_diagonal_2_r3);
		setRotationAngle(wall_diagonal_2_r3, 0, 0.3491F, 0);
		wall_diagonal_2_r3.setTextureUVOffset(288, 279).addCuboid(-2, -39, -32, 2, 39, 32, 0, true);

		wall_diagonal_1_r3 = createModelPart();
		wall_diagonal_1_r3.setPivot(-20, 0, -16);
		head_exterior.addChild(wall_diagonal_1_r3);
		setRotationAngle(wall_diagonal_1_r3, 0, -0.3491F, 0);
		wall_diagonal_1_r3.setTextureUVOffset(288, 279).addCuboid(0, -39, -32, 2, 39, 32, 0, false);

		head_exterior_1 = createModelPart();
		head_exterior_1.setPivot(0, 24, 0);
		head_exterior_1.setTextureUVOffset(0, 75).addCuboid(-10, -39, -46, 20, 7, 9, 0, false);

		front_r1 = createModelPart();
		front_r1.setPivot(0, -14, -46);
		head_exterior_1.addChild(front_r1);
		setRotationAngle(front_r1, -0.4189F, 0, 0);
		front_r1.setTextureUVOffset(141, 131).addCuboid(-13, -20, 0, 26, 20, 0, 0, false);

		head_exterior_3_5 = createModelPart();
		head_exterior_3_5.setPivot(0, 24, 0);
		head_exterior_3_5.setTextureUVOffset(0, 75).addCuboid(-10, -39, -44, 20, 7, 9, 0, false);

		front_r2 = createModelPart();
		front_r2.setPivot(0, -14, -46);
		head_exterior_3_5.addChild(front_r2);
		setRotationAngle(front_r2, -0.1134F, 0, 0);
		front_r2.setTextureUVOffset(141, 131).addCuboid(-13, -20, 0, 26, 20, 0, 0, false);

		head_exterior_4 = createModelPart();
		head_exterior_4.setPivot(0, 24, 0);
		head_exterior_4.setTextureUVOffset(0, 75).addCuboid(-10, -38.8073F, -43.2695F, 20, 7, 9, 0, false);

		front_middle_r1 = createModelPart();
		front_middle_r1.setPivot(0, -14, -46);
		head_exterior_4.addChild(front_middle_r1);
		setRotationAngle(front_middle_r1, -0.0262F, 0, 0);
		front_middle_r1.setTextureUVOffset(141, 143).addCuboid(-13, -8, 0, 26, 8, 0, 0, false);

		front_top_r1 = createModelPart();
		front_top_r1.setPivot(0, -13.9405F, -46.0895F);
		head_exterior_4.addChild(front_top_r1);
		setRotationAngle(front_top_r1, -0.1614F, 0, 0);
		front_top_r1.setTextureUVOffset(141, 133).addCuboid(-13, -18, -1, 26, 10, 0, 0, false);

		destination_board_r1 = createModelPart();
		destination_board_r1.setPivot(3, 0.143F, -0.2353F);
		head_exterior_4.addChild(destination_board_r1);
		setRotationAngle(destination_board_r1, -0.1309F, 0, 0);
		destination_board_r1.setTextureUVOffset(28, 57).addCuboid(-13, -33, -47.75F, 20, 7, 0, 0, false);

		seat = createModelPart();
		seat.setPivot(0, 24, 0);
		seat.setTextureUVOffset(72, 27).addCuboid(-3, -5, -3, 6, 1, 6, 0, false);
		seat.setTextureUVOffset(18, 21).addCuboid(-2.5F, -16.5F, 4.5F, 5, 3, 1, 0, false);

		back_right_r1 = createModelPart();
		back_right_r1.setPivot(-3, -5, 2);
		seat.addChild(back_right_r1);
		setRotationAngle(back_right_r1, -0.2618F, 0, 0.0873F);
		back_right_r1.setTextureUVOffset(24, 0).addCuboid(0, -10, 0, 3, 10, 1, 0, false);

		back_left_r1 = createModelPart();
		back_left_r1.setPivot(3, -5, 2);
		seat.addChild(back_left_r1);
		setRotationAngle(back_left_r1, -0.2618F, 0, -0.0873F);
		back_left_r1.setTextureUVOffset(24, 0).addCuboid(-3, -10, 0, 3, 10, 1, 0, true);

		seat_green = createModelPart();
		seat_green.setPivot(0, 24, 0);
		seat_green.setTextureUVOffset(36, 32).addCuboid(-3, -5, -2.75F, 6, 1, 6, 0, false);
		seat_green.setTextureUVOffset(15, 41).addCuboid(-3, -16.4216F, 4.008F, 6, 3, 1, 0, false);
		seat_green.setTextureUVOffset(45, 45).addCuboid(-3, -8, 2.25F, 6, 3, 1, 0, false);

		back_right_r2 = createModelPart();
		back_right_r2.setPivot(-3, -5.1969F, 1.9953F);
		seat_green.addChild(back_right_r2);
		setRotationAngle(back_right_r2, -0.3054F, 0, 0);
		back_right_r2.setTextureUVOffset(34, 43).addCuboid(0, -8.75F, -0.6F, 3, 6, 1, 0, false);
		back_right_r2.setTextureUVOffset(34, 43).addCuboid(3, -8.75F, -0.6F, 3, 6, 1, 0, true);

		seat_purple = createModelPart();
		seat_purple.setPivot(0, 24, 0);
		seat_purple.setTextureUVOffset(72, 27).addCuboid(-3, -5, -2.75F, 6, 1, 6, 0, false);
		seat_purple.setTextureUVOffset(17, 32).addCuboid(-3, -16.4216F, 4.008F, 6, 3, 1, 0, false);
		seat_purple.setTextureUVOffset(18, 21).addCuboid(-3, -8, 2.25F, 6, 3, 1, 0, false);

		back_right_r3 = createModelPart();
		back_right_r3.setPivot(-3, -5.1969F, 1.9953F);
		seat_purple.addChild(back_right_r3);
		setRotationAngle(back_right_r3, -0.3054F, 0, 0);
		back_right_r3.setTextureUVOffset(24, 0).addCuboid(0, -8.75F, -0.6F, 3, 6, 1, 0, false);
		back_right_r3.setTextureUVOffset(24, 0).addCuboid(3, -8.75F, -0.6F, 3, 6, 1, 0, true);

		vents_top = createModelPart();
		vents_top.setPivot(0, 24, 0);
		vents_top.setTextureUVOffset(274, 169).addCuboid(-15, -46, 20, 15, 7, 28, 0, false);
		vents_top.setTextureUVOffset(257, 103).addCuboid(-15, -46, -14, 15, 7, 28, 0, false);
		vents_top.setTextureUVOffset(274, 169).addCuboid(-15, -46, -48, 15, 7, 28, 0, false);

		headlights = createModelPart();
		headlights.setPivot(0, 24, 0);
		headlights.setTextureUVOffset(12, 12).addCuboid(-7, -6, -46.1F, 4, 3, 0, 0, false);
		headlights.setTextureUVOffset(12, 12).addCuboid(3, -6, -46.1F, 4, 3, 0, 0, true);

		tail_lights = createModelPart();
		tail_lights.setPivot(0, 24, 0);
		tail_lights.setTextureUVOffset(20, 12).addCuboid(-8, -7, 46.1F, 5, 4, 0, 0, false);
		tail_lights.setTextureUVOffset(20, 12).addCuboid(3, -7, 46.1F, 5, 4, 0, 0, true);

		side_display = createModelPart();
		side_display.setPivot(0, 24, 0);
		side_display.setTextureUVOffset(72, 315).addCuboid(-19.95F, -32, -14, 3, 6, 28, 0, false);

		bottom_r1 = createModelPart();
		bottom_r1.setPivot(-16.95F, -29, 0);
		side_display.addChild(bottom_r1);
		setRotationAngle(bottom_r1, 0, 0, 0.1745F);
		bottom_r1.setTextureUVOffset(106, 311).addCuboid(-1, 0, -14, 1, 4, 28, 0, false);

		buildModel();
	}

	private static final int DOOR_MAX = 14;
	private static final ModelDoorOverlay MODEL_DOOR_OVERLAY = new ModelDoorOverlay(DOOR_MAX, 0, 14, "door_overlay_light_rail_left.png", "door_overlay_light_rail_right.png", true, false);
	private static final ModelDoorOverlay MODEL_DOOR_OVERLAY_RHT = new ModelDoorOverlay(DOOR_MAX, 0, 14, "door_overlay_light_rail_left.png", "door_overlay_light_rail_right.png", false, true);
	private static final ModelDoorOverlay MODEL_DOOR_OVERLAY_3 = new ModelDoorOverlay(DOOR_MAX, 0, 14, "door_overlay_light_rail_3_left.png", "door_overlay_light_rail_3_right.png", true, false);
	private static final ModelDoorOverlay MODEL_DOOR_OVERLAY_3_RHT = new ModelDoorOverlay(DOOR_MAX, 0, 14, "door_overlay_light_rail_3_left.png", "door_overlay_light_rail_3_right.png", false, true);
	private static final ModelDoorOverlay MODEL_DOOR_OVERLAY_4 = new ModelDoorOverlay(DOOR_MAX, 0, 14, "door_overlay_light_rail_4_left.png", "door_overlay_light_rail_4_right.png", true, false);
	private static final ModelDoorOverlay MODEL_DOOR_OVERLAY_4_RHT = new ModelDoorOverlay(DOOR_MAX, 0, 14, "door_overlay_light_rail_4_left.png", "door_overlay_light_rail_4_right.png", false, true);
	private static final ModelDoorOverlay MODEL_DOOR_OVERLAY_5 = new ModelDoorOverlay(DOOR_MAX, 0, 14, "door_overlay_light_rail_5_left.png", "door_overlay_light_rail_5_right.png", true, false);
	private static final ModelDoorOverlay MODEL_DOOR_OVERLAY_5_RHT = new ModelDoorOverlay(DOOR_MAX, 0, 14, "door_overlay_light_rail_5_left.png", "door_overlay_light_rail_5_right.png", false, true);

	@Override
	public ModelLightRail createNew(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		return new ModelLightRail(phase, isRHT, doorAnimationType, renderDoorOverlay);
	}

	@Override
	protected void renderWindowPositions(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
		switch (renderStage) {
			case INTERIOR:
				renderMirror(window, graphicsHolder, light, position);
				if (renderDetails) {
					boolean flipSeat = false;
					for (int z = position - 24; z <= position + 24; z += 16) {
						renderOnce(phase >= 4 ? flipSeat ? seat_purple : seat_green : seat, graphicsHolder, light, 15, z);
						renderOnce(phase >= 4 ? flipSeat ? seat_green : seat_purple : seat, graphicsHolder, light, -8.5F, z);
						renderOnce(phase >= 4 ? flipSeat ? seat_purple : seat_green : seat, graphicsHolder, light, -15, z);
						flipSeat = !flipSeat;
					}
				}
				break;
			case EXTERIOR:
				renderMirror(window_exterior, graphicsHolder, light, position);
				break;
		}

		for (int i = 0; i < 2; i++) {
			final int roofPosition = position + i * 32 - 16;
			switch (renderStage) {
				case LIGHT:
					renderMirror(phase == 5 ? roof_light_5 : roof_light, graphicsHolder, light, roofPosition);
					break;
				case INTERIOR:
					if (renderDetails) {
						renderMirror(roof, graphicsHolder, light, roofPosition);
					}
					break;
				case EXTERIOR:
					renderMirror(roof_exterior, graphicsHolder, light, roofPosition);
					break;
			}
		}
	}

	@Override
	protected void renderDoorPositions(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
		switch (renderStage) {
			case LIGHT:
				renderMirror(phase == 5 ? roof_light_5 : roof_light, graphicsHolder, light, position);
				break;
			case INTERIOR:
				if (isRHT) {
					(phase == 5 ? door_left_5 : door_left).setOffset(0, 0, doorRightZ);
					(phase == 5 ? door_right_5 : door_right).setOffset(0, 0, -doorRightZ);
					renderOnce(phase == 5 ? door_5 : door, graphicsHolder, light, position);
					renderOnce(door_window, graphicsHolder, light, position);
					if (renderDetails) {
						renderOnce(phase >= 4 ? phase == 4 || phase == 6 ? door_handrails_4 : door_handrails_5 : door_handrails, graphicsHolder, light, position);
						renderOnce(phase >= 4 ? phase == 4 || phase == 6 ? door_window_handrails_4 : door_window_handrails_5 : door_window_handrails, graphicsHolder, light, position);
					}
				} else {
					(phase == 5 ? door_left_5 : door_left).setOffset(0, 0, doorLeftZ);
					(phase == 5 ? door_right_5 : door_right).setOffset(0, 0, -doorLeftZ);
					renderOnceFlipped(phase == 5 ? door_5 : door, graphicsHolder, light, position);
					renderOnceFlipped(door_window, graphicsHolder, light, position);
					if (renderDetails) {
						renderOnceFlipped(phase >= 4 ? phase == 4 || phase == 6 ? door_handrails_4 : door_handrails_5 : door_handrails, graphicsHolder, light, position);
						renderOnceFlipped(phase >= 4 ? phase == 4 || phase == 6 ? door_window_handrails_4 : door_window_handrails_5 : door_window_handrails, graphicsHolder, light, position);
					}
				}
				if (renderDetails) {
					renderMirror(roof, graphicsHolder, light, position);
				}
				break;
			case EXTERIOR:
				if (isRHT) {
					(phase == 5 ? door_left_exterior_5 : door_left_exterior).setOffset(0, 0, doorRightZ);
					(phase == 5 ? door_right_exterior_5 : door_right_exterior).setOffset(0, 0, -doorRightZ);
					renderOnce(phase == 5 ? door_exterior_5 : door_exterior, graphicsHolder, light, position);
					renderOnce(door_window_exterior, graphicsHolder, light, position);
				} else {
					(phase == 5 ? door_left_exterior_5 : door_left_exterior).setOffset(0, 0, doorLeftZ);
					(phase == 5 ? door_right_exterior_5 : door_right_exterior).setOffset(0, 0, -doorLeftZ);
					renderOnceFlipped(phase == 5 ? door_exterior_5 : door_exterior, graphicsHolder, light, position);
					renderOnceFlipped(door_window_exterior, graphicsHolder, light, position);
				}
				renderMirror(roof_exterior, graphicsHolder, light, position);
				if (position == 0) {
					renderMirror(vents_top, graphicsHolder, light, position);
				}
				break;
		}
	}

	@Override
	protected void renderHeadPosition1(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean useHeadlights) {
		switch (renderStage) {
			case LIGHT:
				renderMirror(phase == 5 ? roof_light_5 : roof_light, graphicsHolder, light, position);
				break;
			case ALWAYS_ON_LIGHT:
				renderOnce(headlights, graphicsHolder, light, position);
				break;
			case INTERIOR:
				renderOnce(head, graphicsHolder, light, position);
				if (renderDetails) {
					boolean flipSeat = false;
					for (int z = position - 8; z <= position + 8; z += 16) {
						renderOnce(phase >= 4 ? flipSeat ? seat_purple : seat_green : seat, graphicsHolder, light, 15, z);
						renderOnce(phase >= 4 ? flipSeat ? seat_green : seat_purple : seat, graphicsHolder, light, -8.5F, z);
						renderOnce(phase >= 4 ? flipSeat ? seat_purple : seat_green : seat, graphicsHolder, light, -15, z);
						flipSeat = !flipSeat;
					}
					renderMirror(roof, graphicsHolder, light, position);
				}
				break;
			case EXTERIOR:
				switch (phase) {
					case 1:
					case 2:
						renderOnce(head_exterior_1, graphicsHolder, light, position);
						break;
					case 3:
					case 5:
					case 7:
						renderOnce(head_exterior_3_5, graphicsHolder, light, position);
						break;
					case 4:
					case 6:
						renderOnce(head_exterior_4, graphicsHolder, light, position);
						break;
				}
				renderOnce(head_exterior, graphicsHolder, light, position);
				renderMirror(roof_exterior, graphicsHolder, light, position);
				renderOnceFlipped(roof_end_exterior, graphicsHolder, light, position);
				break;
		}
	}

	@Override
	protected void renderHeadPosition2(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean useHeadlights) {
		switch (renderStage) {
			case LIGHT:
				renderMirror(phase == 5 ? roof_light_5 : roof_light, graphicsHolder, light, position);
				break;
			case ALWAYS_ON_LIGHT:
				renderOnce(tail_lights, graphicsHolder, light, position);
				break;
			case INTERIOR:
				renderOnce(end, graphicsHolder, light, position);
				if (renderDetails) {
					boolean flipSeat = false;
					for (int z = position - 8; z <= position + 8; z += 16) {
						renderOnce(phase >= 4 ? flipSeat ? seat_purple : seat_green : seat, graphicsHolder, light, 15, z);
						renderOnce(phase >= 4 ? flipSeat ? seat_green : seat_purple : seat, graphicsHolder, light, -8.5F, z);
						renderOnce(phase >= 4 ? flipSeat ? seat_purple : seat_green : seat, graphicsHolder, light, -15, z);
						flipSeat = !flipSeat;
					}
					renderMirror(roof, graphicsHolder, light, position);
					if (isRHT) {
						renderOnce(side_display, graphicsHolder, light, position - (phase <= 3 ? 64 : phase == 5 ? 0 : 1));
					} else {
						renderOnceFlipped(side_display, graphicsHolder, light, position - (phase <= 3 ? 64 : phase == 5 ? 0 : 1));
					}
				}
				break;
			case EXTERIOR:
				renderOnce(end_exterior, graphicsHolder, light, position);
				renderMirror(roof_exterior, graphicsHolder, light, position);
				renderOnce(roof_end_exterior, graphicsHolder, light, position);
				break;
		}
	}

	@Override
	protected void renderEndPosition1(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
		renderHeadPosition1(graphicsHolder, renderStage, light, position, renderDetails, doorLeftX, doorRightX, doorLeftZ, doorRightZ, true);
	}

	@Override
	protected void renderEndPosition2(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
		renderHeadPosition2(graphicsHolder, renderStage, light, position, renderDetails, doorLeftX, doorRightX, doorLeftZ, doorRightZ, false);
	}

	@Override
	protected ModelDoorOverlay getModelDoorOverlay() {
		switch (phase) {
			case 1:
			case 2:
				return isRHT ? MODEL_DOOR_OVERLAY_RHT : MODEL_DOOR_OVERLAY;
			case 3:
			case 7:
				return isRHT ? MODEL_DOOR_OVERLAY_3_RHT : MODEL_DOOR_OVERLAY_3;
			case 4:
			case 6:
				return isRHT ? MODEL_DOOR_OVERLAY_4_RHT : MODEL_DOOR_OVERLAY_4;
			case 5:
				return isRHT ? MODEL_DOOR_OVERLAY_5_RHT : MODEL_DOOR_OVERLAY_5;
		}
		return null;
	}

	@Override
	protected ModelDoorOverlayTopBase getModelDoorOverlayTop() {
		return null;
	}

	@Override
	protected int[] getWindowPositions() {
		return new int[]{-48, 48};
	}

	@Override
	protected int[] getDoorPositions() {
		return new int[]{-96, 0, 96};
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
		final float frontOffset = phase == 3 || phase == 5 || phase == 7 ? 2.75F : phase == 4 || phase == 6 ? 3.02F : 2.87F;
		final int color = phase == 3 || phase == 7 ? 0xFFA4FE07 : 0xFFFF9900;

		renderFrontDestination(
				storedMatrixTransformations,
				0, 0, getEndPositions()[0] / 16F - frontOffset, thisRouteNumber.isEmpty() ? 0 : -0.2F, phase == 4 || phase == 6 ? -2.14F : -2.18F, -0.01F,
				phase == 4 || phase == 6 ? -7.5F : 0, 0, 0.56F, 0.26F,
				color, color, 3, getDestinationString(thisRouteDestination, TextSpacingType.SPACE_CJK_FLIPPED, true), true, 0, 2
		);

		if (!thisRouteNumber.isEmpty()) {
			renderFrontDestination(
					storedMatrixTransformations,
					0, 0, getEndPositions()[0] / 16F - frontOffset, 0.31F, phase == 4 || phase == 6 ? -2.14F : -2.15F, -0.01F,
					phase == 4 || phase == 6 ? -7.5F : 0, 0, 0.4F, 0.26F,
					color, color, 3, thisRouteNumber, false, 0, 2
			);
			renderFrontDestination(
					storedMatrixTransformations,
					0, 0, getEndPositions()[0] / 16F - 2.92F, 0, phase == 1 || phase == 6 ? -2.13F : -2.24F, -0.01F,
					-5, 0, 0.38F, 0.2F,
					color, color, 3, thisRouteNumber, false, 1, 2
			);
		}

		final float sideOffset = (128 - (phase <= 3 ? 64 : phase == 5 ? 0 : 1)) / 16F;
		renderFrontDestination(
				storedMatrixTransformations,
				isRHT ? -1.26F : 1.26F, -1.76F, sideOffset - 0.3F, 0, 0, 0,
				0, isRHT ? 90 : -90, 0.56F, 0.26F,
				color, color, 3, getDestinationString(thisRouteDestination, TextSpacingType.SPACE_CJK_FLIPPED, true), true, 0, 2
		);
		renderFrontDestination(
				storedMatrixTransformations,
				isRHT ? -1.26F : 1.26F, -1.73F, sideOffset + 0.42F, 0, 0, 0,
				0, isRHT ? 90 : -90, 0.4F, 0.26F,
				color, color, 3, thisRouteNumber, false, 0, 2
		);
		renderFrontDestination(
				storedMatrixTransformations,
				isRHT ? -1.05F : 1.05F, -1.89F, sideOffset, 0, 0, 0,
				0, isRHT ? -90 : 90, 1.2F, 0.08F,
				color, color, 1, ((thisRouteNumber.isEmpty() ? "" : thisRouteNumber + "|") + getDestinationString(thisRouteDestination, TextSpacingType.SPACE_CJK, true)).replace("|", "  "), false, 0, 2
		);

		final String stationName = atPlatform ? thisStationName : nextStationName;
		if (!stationName.isEmpty()) {
			final String stationString = getDestinationString(stationName, TextSpacingType.SPACE_CJK_LARGE, true);
			final DynamicTextureCache.DynamicResource dynamicResource = DynamicTextureCache.instance.getPixelatedText(stationString, 0xFFFF9900, 300, 0, true);
			final StoredMatrixTransformations storedMatrixTransformationsNew = storedMatrixTransformations.copy();

			storedMatrixTransformationsNew.add(graphicsHolder -> {
				graphicsHolder.rotateYDegrees(180);
				graphicsHolder.translate(-0.35F, -2.2F, 8.99F);
			});

			RenderTrains.scheduleRender(dynamicResource.identifier, false, RenderTrains.QueuedRenderLayer.LIGHT_TRANSLUCENT, (graphicsHolder, offset) -> {
				storedMatrixTransformations.transform(graphicsHolder, offset);
				RouteMapGenerator.scrollTextLightRail(graphicsHolder, stationString.split("\\|").length, 0.7F, 0.07F, dynamicResource.width, dynamicResource.height);
				graphicsHolder.pop();
			});
		}
	}

	@Override
	protected String defaultDestinationString() {
		return "\u4E0D載客|Not in Service";
	}

	private static DoorAnimationType getDoorAnimationType(int phase) {
		switch (phase) {
			case 1:
			case 6:
				return DoorAnimationType.BOUNCY_2;
			case 4:
				return DoorAnimationType.STANDARD_SLOW;
			default:
				return DoorAnimationType.STANDARD;
		}
	}
}
