package org.mtr.mod.model;

import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.ModelPartExtension;
import org.mtr.mod.client.DoorAnimationType;
import org.mtr.mod.resource.RenderStage;

public class ModelMPL85 extends ModelSimpleTrainBase<ModelMPL85> {

	private final ModelPartExtension window;
	private final ModelPartExtension roof_4_r1;
	private final ModelPartExtension roof_3_r1;
	private final ModelPartExtension roof_2_r1;
	private final ModelPartExtension wall_top_r1;
	private final ModelPartExtension wall_bottom_r1;
	private final ModelPartExtension window_exterior;
	private final ModelPartExtension roof_3_r2;
	private final ModelPartExtension roof_2_r2;
	private final ModelPartExtension roof_1_r1;
	private final ModelPartExtension wall_top_r2;
	private final ModelPartExtension wall_bottom_r2;
	private final ModelPartExtension door;
	private final ModelPartExtension roof_4_r2;
	private final ModelPartExtension roof_3_r3;
	private final ModelPartExtension roof_2_r3;
	private final ModelPartExtension wall_top_2_r1;
	private final ModelPartExtension wall_bottom_2_r1;
	private final ModelPartExtension door_left;
	private final ModelPartExtension door_top_r1;
	private final ModelPartExtension door_bottom_r1;
	private final ModelPartExtension door_right;
	private final ModelPartExtension door_top_r2;
	private final ModelPartExtension door_bottom_r2;
	private final ModelPartExtension door_exterior;
	private final ModelPartExtension wall_top_3_r1;
	private final ModelPartExtension wall_bottom_3_r1;
	private final ModelPartExtension door_left_exterior;
	private final ModelPartExtension door_top_r3;
	private final ModelPartExtension door_bottom_r3;
	private final ModelPartExtension door_right_exterior;
	private final ModelPartExtension door_top_r4;
	private final ModelPartExtension door_bottom_r4;
	private final ModelPartExtension roof_door_exterior_1;
	private final ModelPartExtension roof_3_r4;
	private final ModelPartExtension roof_2_r4;
	private final ModelPartExtension roof_1_r2;
	private final ModelPartExtension roof_door_exterior_2;
	private final ModelPartExtension roof_4_r3;
	private final ModelPartExtension roof_3_r5;
	private final ModelPartExtension roof_2_r5;
	private final ModelPartExtension roof_door_exterior_3;
	private final ModelPartExtension roof_5_r1;
	private final ModelPartExtension roof_4_r4;
	private final ModelPartExtension roof_3_r6;
	private final ModelPartExtension roof_window_exterior;
	private final ModelPartExtension roof_6_r1;
	private final ModelPartExtension roof_5_r2;
	private final ModelPartExtension roof_4_r5;
	private final ModelPartExtension seat_1;
	private final ModelPartExtension handrail_3_r1;
	private final ModelPartExtension handrail_2_r1;
	private final ModelPartExtension seat_2;
	private final ModelPartExtension handrail_3_r2;
	private final ModelPartExtension handrail_2_r2;
	private final ModelPartExtension seat_3;
	private final ModelPartExtension back_upper_diagonal_2_r1;
	private final ModelPartExtension back_upper_diagonal_1_r1;
	private final ModelPartExtension seat_diagonal_2_r1;
	private final ModelPartExtension seat_diagonal_1_r1;
	private final ModelPartExtension light_window;
	private final ModelPartExtension light_r1;
	private final ModelPartExtension light_door;
	private final ModelPartExtension light_r2;
	private final ModelPartExtension middle_handrail;
	private final ModelPartExtension top_handrail_5_r1;
	private final ModelPartExtension top_handrail_4_r1;
	private final ModelPartExtension top_handrail_3_r1;
	private final ModelPartExtension top_handrail_2_r1;
	private final ModelPartExtension handrail_16_r1;
	private final ModelPartExtension handrail_15_r1;
	private final ModelPartExtension handrail_14_r1;
	private final ModelPartExtension handrail_13_r1;
	private final ModelPartExtension handrail_12_r1;
	private final ModelPartExtension handrail_11_r1;
	private final ModelPartExtension handrail_10_r1;
	private final ModelPartExtension handrail_9_r1;
	private final ModelPartExtension handrail_7_r1;
	private final ModelPartExtension handrail_6_r1;
	private final ModelPartExtension handrail_5_r1;
	private final ModelPartExtension handrail_4_r1;
	private final ModelPartExtension handrail_3_r3;
	private final ModelPartExtension handrail_2_r3;
	private final ModelPartExtension head;
	private final ModelPartExtension side_1;
	private final ModelPartExtension bar_2_r1;
	private final ModelPartExtension front_11_r1;
	private final ModelPartExtension front_10_r1;
	private final ModelPartExtension front_9_r1;
	private final ModelPartExtension front_8_r1;
	private final ModelPartExtension front_4_r1;
	private final ModelPartExtension front_3_r1;
	private final ModelPartExtension front_2_r1;
	private final ModelPartExtension roof_8_r1;
	private final ModelPartExtension roof_7_r1;
	private final ModelPartExtension roof_4_r6;
	private final ModelPartExtension roof_3_r7;
	private final ModelPartExtension roof_2_r6;
	private final ModelPartExtension wall_top_r3;
	private final ModelPartExtension wall_bottom_r3;
	private final ModelPartExtension side_2;
	private final ModelPartExtension bar_3_r1;
	private final ModelPartExtension front_12_r1;
	private final ModelPartExtension front_11_r2;
	private final ModelPartExtension front_10_r2;
	private final ModelPartExtension front_9_r2;
	private final ModelPartExtension front_5_r1;
	private final ModelPartExtension front_4_r2;
	private final ModelPartExtension front_3_r2;
	private final ModelPartExtension roof_9_r1;
	private final ModelPartExtension roof_8_r2;
	private final ModelPartExtension roof_5_r3;
	private final ModelPartExtension roof_4_r7;
	private final ModelPartExtension roof_3_r8;
	private final ModelPartExtension wall_top_r4;
	private final ModelPartExtension wall_bottom_r4;
	private final ModelPartExtension head_exterior;
	private final ModelPartExtension side_1_exterior;
	private final ModelPartExtension roof_8_r3;
	private final ModelPartExtension roof_7_r2;
	private final ModelPartExtension roof_6_r2;
	private final ModelPartExtension roof_5_r4;
	private final ModelPartExtension roof_3_r9;
	private final ModelPartExtension roof_2_r7;
	private final ModelPartExtension roof_1_r3;
	private final ModelPartExtension front_14_r1;
	private final ModelPartExtension front_13_r1;
	private final ModelPartExtension front_12_r2;
	private final ModelPartExtension front_11_r3;
	private final ModelPartExtension front_10_r3;
	private final ModelPartExtension front_9_r3;
	private final ModelPartExtension front_8_r2;
	private final ModelPartExtension front_7_r1;
	private final ModelPartExtension front_6_r1;
	private final ModelPartExtension front_5_r2;
	private final ModelPartExtension front_4_r3;
	private final ModelPartExtension front_3_r3;
	private final ModelPartExtension wall_top_r5;
	private final ModelPartExtension wall_bottom_r5;
	private final ModelPartExtension side_2_exterior;
	private final ModelPartExtension roof_9_r2;
	private final ModelPartExtension roof_8_r4;
	private final ModelPartExtension roof_7_r3;
	private final ModelPartExtension roof_6_r3;
	private final ModelPartExtension roof_4_r8;
	private final ModelPartExtension roof_3_r10;
	private final ModelPartExtension roof_2_r8;
	private final ModelPartExtension front_15_r1;
	private final ModelPartExtension front_14_r2;
	private final ModelPartExtension front_13_r2;
	private final ModelPartExtension front_12_r3;
	private final ModelPartExtension front_11_r4;
	private final ModelPartExtension front_10_r4;
	private final ModelPartExtension front_9_r4;
	private final ModelPartExtension front_8_r3;
	private final ModelPartExtension front_7_r2;
	private final ModelPartExtension front_6_r2;
	private final ModelPartExtension front_5_r3;
	private final ModelPartExtension front_4_r4;
	private final ModelPartExtension wall_top_r6;
	private final ModelPartExtension wall_bottom_r6;
	private final ModelPartExtension door_light_on;
	private final ModelPartExtension light_r3;
	private final ModelPartExtension door_light_off;
	private final ModelPartExtension light_r4;
	private final ModelPartExtension headlights;
	private final ModelPartExtension headlight_4_r1;
	private final ModelPartExtension headlight_3_r1;
	private final ModelPartExtension headlight_2_r1;
	private final ModelPartExtension headlight_1_r1;
	private final ModelPartExtension tail_lights;
	private final ModelPartExtension headlight_4_r2;
	private final ModelPartExtension headlight_3_r2;
	private final ModelPartExtension headlight_2_r2;
	private final ModelPartExtension headlight_1_r2;

	public ModelMPL85() {
		this(DoorAnimationType.PLUG_FAST, true);
	}

	protected ModelMPL85(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		super(256, 256, doorAnimationType, renderDoorOverlay);

		window = createModelPart();
		window.setPivot(0, 24, 0);
		window.setTextureUVOffset(32, 94).addCuboid(0, 0, -14, 19, 2, 28, 0, false);
		window.setTextureUVOffset(130, 132).addCuboid(20, -10, -14, 1, 5, 28, 0, false);
		window.setTextureUVOffset(4, 42).addCuboid(16.734F, -33.9011F, -14, 2, 0, 28, 0, false);
		window.setTextureUVOffset(126, 0).addCuboid(0, -37.3304F, -14, 7, 0, 28, 0, false);

		roof_4_r1 = createModelPart();
		roof_4_r1.setPivot(10.0171F, -36.9817F, 0);
		window.addChild(roof_4_r1);
		setRotationAngle(roof_4_r1, 0, 0, 0.0873F);
		roof_4_r1.setTextureUVOffset(0, 126).addCuboid(-4, 0, -14, 8, 0, 28, 0, false);

		roof_3_r1 = createModelPart();
		roof_3_r1.setPivot(14.5019F, -35.7671F, 0);
		window.addChild(roof_3_r1);
		setRotationAngle(roof_3_r1, 0, 0, 1.0472F);
		roof_3_r1.setTextureUVOffset(8, 0).addCuboid(-1, 0, -14, 2, 0, 28, 0, false);

		roof_2_r1 = createModelPart();
		roof_2_r1.setPivot(15.8679F, -34.4011F, 0);
		window.addChild(roof_2_r1);
		setRotationAngle(roof_2_r1, 0, 0, 0.5236F);
		roof_2_r1.setTextureUVOffset(0, 42).addCuboid(-1, 0, -14, 2, 0, 28, 0, false);

		wall_top_r1 = createModelPart();
		wall_top_r1.setPivot(21, -10, 0);
		window.addChild(wall_top_r1);
		setRotationAngle(wall_top_r1, 0, 0, -0.0873F);
		wall_top_r1.setTextureUVOffset(124, 12).addCuboid(-1, -27, -14, 1, 27, 28, 0, false);

		wall_bottom_r1 = createModelPart();
		wall_bottom_r1.setPivot(21, -5, 0);
		window.addChild(wall_bottom_r1);
		setRotationAngle(wall_bottom_r1, 0, 0, 0.1745F);
		wall_bottom_r1.setTextureUVOffset(44, 126).addCuboid(-1, 0, -14, 1, 7, 28, 0, false);

		window_exterior = createModelPart();
		window_exterior.setPivot(0, 24, 0);
		window_exterior.setTextureUVOffset(0, 133).addCuboid(21, -10, -14, 0, 5, 28, 0, false);
		window_exterior.setTextureUVOffset(30, 126).addCuboid(-0.0293F, -41.2781F, -14, 7, 0, 28, 0, false);

		roof_3_r2 = createModelPart();
		roof_3_r2.setPivot(11.9516F, -40.8423F, 0);
		window_exterior.addChild(roof_3_r2);
		setRotationAngle(roof_3_r2, 0, 0, 0.0873F);
		roof_3_r2.setTextureUVOffset(108, 125).addCuboid(-5, 0, -14, 10, 0, 28, 0, false);

		roof_2_r2 = createModelPart();
		roof_2_r2.setPivot(17.3157F, -40.0862F, 0);
		window_exterior.addChild(roof_2_r2);
		setRotationAngle(roof_2_r2, 0, 0, -0.8727F);
		roof_2_r2.setTextureUVOffset(74, 124).addCuboid(0, -0.5F, -14, 0, 1, 28, 0, false);

		roof_1_r1 = createModelPart();
		roof_1_r1.setPivot(18.2163F, -37.8329F, 0);
		window_exterior.addChild(roof_1_r1);
		setRotationAngle(roof_1_r1, 0, 0, -0.2618F);
		roof_1_r1.setTextureUVOffset(56, 137).addCuboid(0, -2, -14, 0, 4, 28, 0, false);

		wall_top_r2 = createModelPart();
		wall_top_r2.setPivot(21, -10, 0);
		window_exterior.addChild(wall_top_r2);
		setRotationAngle(wall_top_r2, 0, 0, -0.0873F);
		wall_top_r2.setTextureUVOffset(80, 97).addCuboid(0, -26, -14, 0, 26, 28, 0, false);

		wall_bottom_r2 = createModelPart();
		wall_bottom_r2.setPivot(21, -5, 0);
		window_exterior.addChild(wall_bottom_r2);
		setRotationAngle(wall_bottom_r2, 0, 0, 0.1745F);
		wall_bottom_r2.setTextureUVOffset(102, 125).addCuboid(0, 0, -14, 0, 7, 28, 0, false);

		door = createModelPart();
		door.setPivot(0, 24, 0);
		door.setTextureUVOffset(0, 0).addCuboid(0, 0, -20, 19, 2, 40, 0, false);
		door.setTextureUVOffset(162, 165).addCuboid(20, -10, -20, 1, 5, 4, 0, false);
		door.setTextureUVOffset(162, 93).addCuboid(20, -10, 16, 1, 5, 4, 0, false);
		door.setTextureUVOffset(104, 203).addCuboid(17, -36, -20, 3, 36, 4, 0, false);
		door.setTextureUVOffset(0, 192).addCuboid(17, -36, 16, 3, 36, 4, 0, false);
		door.setTextureUVOffset(6, 209).addCuboid(16.734F, -35.9011F, -20, 2, 2, 40, 0, false);
		door.setTextureUVOffset(56, 42).addCuboid(0, -37.3304F, -20, 7, 0, 40, 0, false);

		roof_4_r2 = createModelPart();
		roof_4_r2.setPivot(10.0171F, -36.9817F, 0);
		door.addChild(roof_4_r2);
		setRotationAngle(roof_4_r2, 0, 0, 0.0873F);
		roof_4_r2.setTextureUVOffset(40, 42).addCuboid(-4, 0, -20, 8, 0, 40, 0, false);

		roof_3_r3 = createModelPart();
		roof_3_r3.setPivot(14.5019F, -35.7671F, 0);
		door.addChild(roof_3_r3);
		setRotationAngle(roof_3_r3, 0, 0, 1.0472F);
		roof_3_r3.setTextureUVOffset(8, 82).addCuboid(-1, 0, -20, 2, 0, 40, 0, false);

		roof_2_r3 = createModelPart();
		roof_2_r3.setPivot(15.8679F, -34.4011F, 0);
		door.addChild(roof_2_r3);
		setRotationAngle(roof_2_r3, 0, 0, 0.5236F);
		roof_2_r3.setTextureUVOffset(12, 82).addCuboid(-1, 0, -20, 2, 0, 40, 0, false);

		wall_top_2_r1 = createModelPart();
		wall_top_2_r1.setPivot(21, -10, 0);
		door.addChild(wall_top_2_r1);
		setRotationAngle(wall_top_2_r1, 0, 0, -0.0873F);
		wall_top_2_r1.setTextureUVOffset(14, 204).addCuboid(-1, -26, 16, 1, 26, 4, 0, false);
		wall_top_2_r1.setTextureUVOffset(24, 204).addCuboid(-1, -26, -20, 1, 26, 4, 0, false);

		wall_bottom_2_r1 = createModelPart();
		wall_bottom_2_r1.setPivot(21, -5, 0);
		door.addChild(wall_bottom_2_r1);
		setRotationAngle(wall_bottom_2_r1, 0, 0, 0.1745F);
		wall_bottom_2_r1.setTextureUVOffset(114, 160).addCuboid(-1, 0, 16, 1, 7, 4, 0, false);
		wall_bottom_2_r1.setTextureUVOffset(160, 139).addCuboid(-1, 0, -20, 1, 7, 4, 0, false);

		door_left = createModelPart();
		door_left.setPivot(0, 0, 0);
		door.addChild(door_left);
		door_left.setTextureUVOffset(188, 52).addCuboid(20, -10, -16, 1, 5, 16, 0, false);

		door_top_r1 = createModelPart();
		door_top_r1.setPivot(21, -10, 0);
		door_left.addChild(door_top_r1);
		setRotationAngle(door_top_r1, 0, 0, -0.0873F);
		door_top_r1.setTextureUVOffset(96, 160).addCuboid(-1, -26, -16, 1, 26, 16, 0, false);

		door_bottom_r1 = createModelPart();
		door_bottom_r1.setPivot(21, -5, 0);
		door_left.addChild(door_bottom_r1);
		setRotationAngle(door_bottom_r1, 0, 0, 0.1745F);
		door_bottom_r1.setTextureUVOffset(182, 29).addCuboid(-1, 0, -16, 1, 7, 16, 0, false);

		door_right = createModelPart();
		door_right.setPivot(0, 0, 0);
		door.addChild(door_right);
		door_right.setTextureUVOffset(36, 188).addCuboid(20, -10, 0, 1, 5, 16, 0, false);

		door_top_r2 = createModelPart();
		door_top_r2.setPivot(21, -10, 0);
		door_right.addChild(door_top_r2);
		setRotationAngle(door_top_r2, 0, 0, -0.0873F);
		door_top_r2.setTextureUVOffset(0, 73).addCuboid(-1, -26, 0, 1, 26, 16, 0, false);

		door_bottom_r2 = createModelPart();
		door_bottom_r2.setPivot(21, -5, 0);
		door_right.addChild(door_bottom_r2);
		setRotationAngle(door_bottom_r2, 0, 0, 0.1745F);
		door_bottom_r2.setTextureUVOffset(54, 169).addCuboid(-1, 0, 0, 1, 7, 16, 0, false);

		door_exterior = createModelPart();
		door_exterior.setPivot(0, 24, 0);
		door_exterior.setTextureUVOffset(20, 126).addCuboid(21, -10, -20, 0, 5, 4, 0, false);
		door_exterior.setTextureUVOffset(32, 112).addCuboid(21, -10, 16, 0, 5, 4, 0, false);

		wall_top_3_r1 = createModelPart();
		wall_top_3_r1.setPivot(21, -10, 0);
		door_exterior.addChild(wall_top_3_r1);
		setRotationAngle(wall_top_3_r1, 0, 0, -0.0873F);
		wall_top_3_r1.setTextureUVOffset(70, 202).addCuboid(0, -26, 16, 0, 26, 4, 0, false);
		wall_top_3_r1.setTextureUVOffset(78, 202).addCuboid(0, -26, -20, 0, 26, 4, 0, false);

		wall_bottom_3_r1 = createModelPart();
		wall_bottom_3_r1.setPivot(21, -5, 0);
		door_exterior.addChild(wall_bottom_3_r1);
		setRotationAngle(wall_bottom_3_r1, 0, 0, 0.1745F);
		wall_bottom_3_r1.setTextureUVOffset(112, 100).addCuboid(0, 0, 16, 0, 7, 4, 0, false);
		wall_bottom_3_r1.setTextureUVOffset(146, 0).addCuboid(0, 0, -20, 0, 7, 4, 0, false);

		door_left_exterior = createModelPart();
		door_left_exterior.setPivot(0, 0, 0);
		door_exterior.addChild(door_left_exterior);
		door_left_exterior.setTextureUVOffset(154, 18).addCuboid(21, -10, -16, 0, 5, 16, 0, false);

		door_top_r3 = createModelPart();
		door_top_r3.setPivot(21, -10, 0);
		door_left_exterior.addChild(door_top_r3);
		setRotationAngle(door_top_r3, 0, 0, -0.0873F);
		door_top_r3.setTextureUVOffset(0, 150).addCuboid(0, -26, -16, 0, 26, 16, 0, false);

		door_bottom_r3 = createModelPart();
		door_bottom_r3.setPivot(21, -5, 0);
		door_left_exterior.addChild(door_bottom_r3);
		setRotationAngle(door_bottom_r3, 0, 0, 0.1745F);
		door_bottom_r3.setTextureUVOffset(72, 153).addCuboid(0, 0, -16, 0, 7, 16, 0, false);

		door_right_exterior = createModelPart();
		door_right_exterior.setPivot(0, 0, 0);
		door_exterior.addChild(door_right_exterior);
		door_right_exterior.setTextureUVOffset(154, 13).addCuboid(21, -10, 0, 0, 5, 16, 0, false);

		door_top_r4 = createModelPart();
		door_top_r4.setPivot(21, -10, 0);
		door_right_exterior.addChild(door_top_r4);
		setRotationAngle(door_top_r4, 0, 0, -0.0873F);
		door_top_r4.setTextureUVOffset(130, 149).addCuboid(0, -26, 0, 0, 26, 16, 0, false);

		door_bottom_r4 = createModelPart();
		door_bottom_r4.setPivot(21, -5, 0);
		door_right_exterior.addChild(door_bottom_r4);
		setRotationAngle(door_bottom_r4, 0, 0, 0.1745F);
		door_bottom_r4.setTextureUVOffset(0, 100).addCuboid(0, 0, 0, 0, 7, 16, 0, false);

		roof_door_exterior_1 = createModelPart();
		roof_door_exterior_1.setPivot(0, 24, 0);
		roof_door_exterior_1.setTextureUVOffset(72, 0).addCuboid(-0.0293F, -41.2781F, -20, 7, 0, 40, 0, false);

		roof_3_r4 = createModelPart();
		roof_3_r4.setPivot(11.9516F, -40.8423F, 0);
		roof_door_exterior_1.addChild(roof_3_r4);
		setRotationAngle(roof_3_r4, 0, 0, 0.0873F);
		roof_3_r4.setTextureUVOffset(20, 42).addCuboid(-5, 0, -20, 10, 0, 40, 0, false);

		roof_2_r4 = createModelPart();
		roof_2_r4.setPivot(17.3157F, -40.0862F, 0);
		roof_door_exterior_1.addChild(roof_2_r4);
		setRotationAngle(roof_2_r4, 0, 0, -0.8727F);
		roof_2_r4.setTextureUVOffset(0, 85).addCuboid(0, -0.5F, -20, 0, 1, 40, 0, false);

		roof_1_r2 = createModelPart();
		roof_1_r2.setPivot(18.2163F, -37.8329F, 0);
		roof_door_exterior_1.addChild(roof_1_r2);
		setRotationAngle(roof_1_r2, 0, 0, -0.2618F);
		roof_1_r2.setTextureUVOffset(60, 50).addCuboid(0, -2, -20, 0, 4, 40, 0, false);

		roof_door_exterior_2 = createModelPart();
		roof_door_exterior_2.setPivot(0, 24, 0);
		roof_door_exterior_2.setTextureUVOffset(70, 42).addCuboid(-0.0293F, -41.2781F, -20, 7, 0, 40, 0, false);

		roof_4_r3 = createModelPart();
		roof_4_r3.setPivot(11.9516F, -40.8423F, 0);
		roof_door_exterior_2.addChild(roof_4_r3);
		setRotationAngle(roof_4_r3, 0, 0, 0.0873F);
		roof_4_r3.setTextureUVOffset(0, 42).addCuboid(-5, 0, -20, 10, 0, 40, 0, false);

		roof_3_r5 = createModelPart();
		roof_3_r5.setPivot(17.3157F, -40.0862F, 0);
		roof_door_exterior_2.addChild(roof_3_r5);
		setRotationAngle(roof_3_r5, 0, 0, -0.8727F);
		roof_3_r5.setTextureUVOffset(80, 84).addCuboid(0, -0.5F, -20, 0, 1, 40, 0, false);

		roof_2_r5 = createModelPart();
		roof_2_r5.setPivot(18.2163F, -37.8329F, 0);
		roof_door_exterior_2.addChild(roof_2_r5);
		setRotationAngle(roof_2_r5, 0, 0, -0.2618F);
		roof_2_r5.setTextureUVOffset(60, 46).addCuboid(0, -2, -20, 0, 4, 40, 0, false);

		roof_door_exterior_3 = createModelPart();
		roof_door_exterior_3.setPivot(0, 24, 0);
		roof_door_exterior_3.setTextureUVOffset(58, 0).addCuboid(-0.0293F, -41.2781F, -20, 7, 0, 40, 0, false);

		roof_5_r1 = createModelPart();
		roof_5_r1.setPivot(11.9516F, -40.8423F, 0);
		roof_door_exterior_3.addChild(roof_5_r1);
		setRotationAngle(roof_5_r1, 0, 0, 0.0873F);
		roof_5_r1.setTextureUVOffset(38, 0).addCuboid(-5, 0, -20, 10, 0, 40, 0, false);

		roof_4_r4 = createModelPart();
		roof_4_r4.setPivot(17.3157F, -40.0862F, 0);
		roof_door_exterior_3.addChild(roof_4_r4);
		setRotationAngle(roof_4_r4, 0, 0, -0.8727F);
		roof_4_r4.setTextureUVOffset(0, 84).addCuboid(0, -0.5F, -20, 0, 1, 40, 0, false);

		roof_3_r6 = createModelPart();
		roof_3_r6.setPivot(18.2163F, -37.8329F, 0);
		roof_door_exterior_3.addChild(roof_3_r6);
		setRotationAngle(roof_3_r6, 0, 0, -0.2618F);
		roof_3_r6.setTextureUVOffset(60, 42).addCuboid(0, -2, -20, 0, 4, 40, 0, false);

		roof_window_exterior = createModelPart();
		roof_window_exterior.setPivot(0, 24, 0);
		roof_window_exterior.setTextureUVOffset(16, 126).addCuboid(-0.0293F, -41.2781F, -14, 7, 0, 28, 0, false);

		roof_6_r1 = createModelPart();
		roof_6_r1.setPivot(11.9516F, -40.8423F, 0);
		roof_window_exterior.addChild(roof_6_r1);
		setRotationAngle(roof_6_r1, 0, 0, 0.0873F);
		roof_6_r1.setTextureUVOffset(98, 0).addCuboid(-5, 0, -14, 10, 0, 28, 0, false);

		roof_5_r2 = createModelPart();
		roof_5_r2.setPivot(17.3157F, -40.0862F, 0);
		roof_window_exterior.addChild(roof_5_r2);
		setRotationAngle(roof_5_r2, 0, 0, -0.8727F);
		roof_5_r2.setTextureUVOffset(74, 123).addCuboid(0, -0.5F, -14, 0, 1, 28, 0, false);

		roof_4_r5 = createModelPart();
		roof_4_r5.setPivot(18.2163F, -37.8329F, 0);
		roof_window_exterior.addChild(roof_4_r5);
		setRotationAngle(roof_4_r5, 0, 0, -0.2618F);
		roof_4_r5.setTextureUVOffset(56, 133).addCuboid(0, -2, -14, 0, 4, 28, 0, false);

		seat_1 = createModelPart();
		seat_1.setPivot(0, 24, 0);
		seat_1.setTextureUVOffset(162, 93).addCuboid(15, -8, -11, 5, 8, 17, 0, false);
		seat_1.setTextureUVOffset(160, 139).addCuboid(11, -9, -10, 9, 1, 16, 0, false);
		seat_1.setTextureUVOffset(72, 187).addCuboid(18, -12, -10, 2, 3, 16, 0, false);
		seat_1.setTextureUVOffset(16, 185).addCuboid(18, -19, -10, 2, 3, 16, 0, false);
		seat_1.setTextureUVOffset(194, 126).addCuboid(11, -19, -11, 9, 11, 1, 0, false);
		seat_1.setTextureUVOffset(250, 0).addCuboid(11.5F, -21, -10.5F, 0, 6, 0, 0.2F, false);

		handrail_3_r1 = createModelPart();
		handrail_3_r1.setPivot(10.0471F, -35.6984F, -10.5F);
		seat_1.addChild(handrail_3_r1);
		setRotationAngle(handrail_3_r1, 0, 0, -0.1309F);
		handrail_3_r1.setTextureUVOffset(250, 0).addCuboid(0, -2, 0, 0, 6, 0, 0.2F, false);

		handrail_2_r1 = createModelPart();
		handrail_2_r1.setPivot(11.7F, -21.2F, 0);
		seat_1.addChild(handrail_2_r1);
		setRotationAngle(handrail_2_r1, 0, 0, -0.0873F);
		handrail_2_r1.setTextureUVOffset(250, 0).addCuboid(-0.2F, -10.2F, -10.5F, 0, 10, 0, 0.2F, false);

		seat_2 = createModelPart();
		seat_2.setPivot(0, 24, 0);
		seat_2.setTextureUVOffset(161, 68).addCuboid(15, -8, -6, 5, 8, 17, 0, false);
		seat_2.setTextureUVOffset(160, 122).addCuboid(11, -9, -6, 9, 1, 16, 0, false);
		seat_2.setTextureUVOffset(173, 184).addCuboid(18, -12, -6, 2, 3, 16, 0, false);
		seat_2.setTextureUVOffset(184, 156).addCuboid(18, -19, -6, 2, 3, 16, 0, false);
		seat_2.setTextureUVOffset(144, 220).addCuboid(11, -19, 10, 9, 11, 1, 0, false);
		seat_2.setTextureUVOffset(250, 0).addCuboid(11.5F, -21, 10.5F, 0, 6, 0, 0.2F, false);

		handrail_3_r2 = createModelPart();
		handrail_3_r2.setPivot(10.0471F, -35.6984F, -10.5F);
		seat_2.addChild(handrail_3_r2);
		setRotationAngle(handrail_3_r2, 0, 0, -0.1309F);
		handrail_3_r2.setTextureUVOffset(250, 0).addCuboid(0, -4, 21, 0, 8, 0, 0.2F, false);

		handrail_2_r2 = createModelPart();
		handrail_2_r2.setPivot(11.7F, -21.2F, 0);
		seat_2.addChild(handrail_2_r2);
		setRotationAngle(handrail_2_r2, 0, 0, -0.0873F);
		handrail_2_r2.setTextureUVOffset(250, 0).addCuboid(-0.2F, -10.2F, 10.5F, 0, 10, 0, 0.2F, false);

		seat_3 = createModelPart();
		seat_3.setPivot(0, 24, 0);
		seat_3.setTextureUVOffset(118, 218).addCuboid(15, -8, -8, 5, 8, 16, 0, false);
		seat_3.setTextureUVOffset(154, 12).addCuboid(10, -9, -8, 10, 1, 16, 0, false);
		seat_3.setTextureUVOffset(32, 166).addCuboid(17, -12, -8, 3, 3, 16, 0, false);
		seat_3.setTextureUVOffset(162, 165).addCuboid(17, -19, -8, 3, 3, 16, 0, false);
		seat_3.setTextureUVOffset(162, 107).addCuboid(11, -12, 8, 7, 3, 0, 0, false);
		seat_3.setTextureUVOffset(162, 107).addCuboid(11, -12, -8, 7, 3, 0, 0, false);

		back_upper_diagonal_2_r1 = createModelPart();
		back_upper_diagonal_2_r1.setPivot(18, 0, -8);
		seat_3.addChild(back_upper_diagonal_2_r1);
		setRotationAngle(back_upper_diagonal_2_r1, 0, -0.1745F, 0);
		back_upper_diagonal_2_r1.setTextureUVOffset(124, 71).addCuboid(0, -19, 0, 1, 3, 6, 0, false);
		back_upper_diagonal_2_r1.setTextureUVOffset(0, 135).addCuboid(0, -12, 0, 1, 3, 6, 0, false);

		back_upper_diagonal_1_r1 = createModelPart();
		back_upper_diagonal_1_r1.setPivot(18, 0, 8);
		seat_3.addChild(back_upper_diagonal_1_r1);
		setRotationAngle(back_upper_diagonal_1_r1, 0, 0.1745F, 0);
		back_upper_diagonal_1_r1.setTextureUVOffset(0, 72).addCuboid(0, -19, -6, 1, 3, 6, 0, false);
		back_upper_diagonal_1_r1.setTextureUVOffset(0, 144).addCuboid(0, -12, -6, 1, 3, 6, 0, false);

		seat_diagonal_2_r1 = createModelPart();
		seat_diagonal_2_r1.setPivot(11, 0, -8);
		seat_3.addChild(seat_diagonal_2_r1);
		setRotationAngle(seat_diagonal_2_r1, 0, -0.1745F, 0);
		seat_diagonal_2_r1.setTextureUVOffset(152, 87).addCuboid(0, -9, 0, 1, 1, 6, 0, false);

		seat_diagonal_1_r1 = createModelPart();
		seat_diagonal_1_r1.setPivot(11, 0, 8);
		seat_3.addChild(seat_diagonal_1_r1);
		setRotationAngle(seat_diagonal_1_r1, 0, 0.1745F, 0);
		seat_diagonal_1_r1.setTextureUVOffset(12, 130).addCuboid(0, -9, -6, 1, 1, 6, 0, false);

		light_window = createModelPart();
		light_window.setPivot(0, 24, 0);


		light_r1 = createModelPart();
		light_r1.setPivot(10, -37, 0);
		light_window.addChild(light_r1);
		setRotationAngle(light_r1, 0, 0, 0.0873F);
		light_r1.setTextureUVOffset(6, 209).addCuboid(-5, -0.1F, -14, 6, 0, 28, 0, false);

		light_door = createModelPart();
		light_door.setPivot(0, 24, 0);


		light_r2 = createModelPart();
		light_r2.setPivot(10, -37, 0);
		light_door.addChild(light_r2);
		setRotationAngle(light_r2, 0, 0, 0.0873F);
		light_r2.setTextureUVOffset(10, 209).addCuboid(-5, -0.1F, -20, 6, 0, 40, 0, false);

		middle_handrail = createModelPart();
		middle_handrail.setPivot(0, 24, 0);
		middle_handrail.setTextureUVOffset(250, 0).addCuboid(0, -12, 0, 0, 12, 0, 0.2F, false);
		middle_handrail.setTextureUVOffset(250, 0).addCuboid(0, -22.0809F, -1.1969F, 0, 1, 0, 0.2F, false);
		middle_handrail.setTextureUVOffset(250, 0).addCuboid(0, -37, 0, 0, 5, 0, 0.2F, false);
		middle_handrail.setTextureUVOffset(183, 219).addCuboid(0, -35, -18, 0, 0, 36, 0.2F, false);
		middle_handrail.setTextureUVOffset(36, 52).addCuboid(-1, -35.5F, -16, 2, 4, 0, 0, false);
		middle_handrail.setTextureUVOffset(36, 48).addCuboid(-1, -35.5F, 16, 2, 4, 0, 0, false);

		top_handrail_5_r1 = createModelPart();
		top_handrail_5_r1.setPivot(0, -36.6392F, 19.8392F);
		middle_handrail.addChild(top_handrail_5_r1);
		setRotationAngle(top_handrail_5_r1, 1.0472F, 0, 0);
		top_handrail_5_r1.setTextureUVOffset(217, 253).addCuboid(0, 0, -1, 0, 0, 2, 0.2F, false);

		top_handrail_4_r1 = createModelPart();
		top_handrail_4_r1.setPivot(0, -34.8F, 18.2F);
		middle_handrail.addChild(top_handrail_4_r1);
		setRotationAngle(top_handrail_4_r1, 0.5236F, 0, 0);
		top_handrail_4_r1.setTextureUVOffset(218, 254).addCuboid(0, -0.2F, 0.2F, 0, 0, 1, 0.2F, false);

		top_handrail_3_r1 = createModelPart();
		top_handrail_3_r1.setPivot(0, -36.6392F, -19.8392F);
		middle_handrail.addChild(top_handrail_3_r1);
		setRotationAngle(top_handrail_3_r1, -1.0472F, 0, 0);
		top_handrail_3_r1.setTextureUVOffset(217, 253).addCuboid(0, 0, -1, 0, 0, 2, 0.2F, false);

		top_handrail_2_r1 = createModelPart();
		top_handrail_2_r1.setPivot(0, -34.8F, -18.2F);
		middle_handrail.addChild(top_handrail_2_r1);
		setRotationAngle(top_handrail_2_r1, -0.5236F, 0, 0);
		top_handrail_2_r1.setTextureUVOffset(218, 254).addCuboid(0, -0.2F, -1.2F, 0, 0, 1, 0.2F, false);

		handrail_16_r1 = createModelPart();
		handrail_16_r1.setPivot(0, -21.5809F, -0.2992F);
		middle_handrail.addChild(handrail_16_r1);
		setRotationAngle(handrail_16_r1, -0.1745F, 2.0944F, 0);
		handrail_16_r1.setTextureUVOffset(250, 0).addCuboid(-0.2591F, -10.0003F, -1.9152F, 0, 4, 0, 0.2F, false);

		handrail_15_r1 = createModelPart();
		handrail_15_r1.setPivot(0, -21.5809F, -0.2992F);
		middle_handrail.addChild(handrail_15_r1);
		setRotationAngle(handrail_15_r1, -0.1745F, -2.0944F, 0);
		handrail_15_r1.setTextureUVOffset(250, 0).addCuboid(0.2591F, -10.0003F, -1.9152F, 0, 4, 0, 0.2F, false);

		handrail_14_r1 = createModelPart();
		handrail_14_r1.setPivot(0, -21.5809F, -0.2992F);
		middle_handrail.addChild(handrail_14_r1);
		setRotationAngle(handrail_14_r1, -0.1745F, 0, 0);
		handrail_14_r1.setTextureUVOffset(250, 0).addCuboid(0, -10.0782F, -1.4732F, 0, 4, 0, 0.2F, false);

		handrail_13_r1 = createModelPart();
		handrail_13_r1.setPivot(0, -21.5809F, -0.2992F);
		middle_handrail.addChild(handrail_13_r1);
		setRotationAngle(handrail_13_r1, -0.0873F, 2.0944F, 0);
		handrail_13_r1.setTextureUVOffset(250, 0).addCuboid(-0.2591F, -5.7625F, -1.4017F, 0, 5, 0, 0.2F, false);

		handrail_12_r1 = createModelPart();
		handrail_12_r1.setPivot(0, -21.5809F, -0.2992F);
		middle_handrail.addChild(handrail_12_r1);
		setRotationAngle(handrail_12_r1, -0.0873F, -2.0944F, 0);
		handrail_12_r1.setTextureUVOffset(250, 0).addCuboid(0.2591F, -5.7625F, -1.4017F, 0, 5, 0, 0.2F, false);

		handrail_11_r1 = createModelPart();
		handrail_11_r1.setPivot(0, -21.5809F, -0.2992F);
		middle_handrail.addChild(handrail_11_r1);
		setRotationAngle(handrail_11_r1, -0.0873F, 0, 0);
		handrail_11_r1.setTextureUVOffset(250, 0).addCuboid(0, -5.8017F, -0.9545F, 0, 5, 0, 0.2F, false);

		handrail_10_r1 = createModelPart();
		handrail_10_r1.setPivot(0, -21.5809F, -0.2992F);
		middle_handrail.addChild(handrail_10_r1);
		setRotationAngle(handrail_10_r1, 0, 2.0944F, 0);
		handrail_10_r1.setTextureUVOffset(250, 0).addCuboid(-0.2591F, -0.5F, -1.3465F, 0, 1, 0, 0.2F, false);

		handrail_9_r1 = createModelPart();
		handrail_9_r1.setPivot(0, -21.5809F, -0.2992F);
		middle_handrail.addChild(handrail_9_r1);
		setRotationAngle(handrail_9_r1, 0, -2.0944F, 0);
		handrail_9_r1.setTextureUVOffset(250, 0).addCuboid(0.2591F, -0.5F, -1.3465F, 0, 1, 0, 0.2F, false);

		handrail_7_r1 = createModelPart();
		handrail_7_r1.setPivot(0, -21.5809F, -0.2992F);
		middle_handrail.addChild(handrail_7_r1);
		setRotationAngle(handrail_7_r1, 0.0873F, 2.0944F, 0);
		handrail_7_r1.setTextureUVOffset(250, 0).addCuboid(-0.2591F, 0.7625F, -1.4017F, 0, 5, 0, 0.2F, false);

		handrail_6_r1 = createModelPart();
		handrail_6_r1.setPivot(0, -21.5809F, -0.2992F);
		middle_handrail.addChild(handrail_6_r1);
		setRotationAngle(handrail_6_r1, 0.0873F, -2.0944F, 0);
		handrail_6_r1.setTextureUVOffset(250, 0).addCuboid(0.2591F, 0.7625F, -1.4017F, 0, 5, 0, 0.2F, false);

		handrail_5_r1 = createModelPart();
		handrail_5_r1.setPivot(0, -21.5809F, -0.2992F);
		middle_handrail.addChild(handrail_5_r1);
		setRotationAngle(handrail_5_r1, 0.0873F, 0, 0);
		handrail_5_r1.setTextureUVOffset(250, 0).addCuboid(0, 0.8017F, -0.9545F, 0, 5, 0, 0.2F, false);

		handrail_4_r1 = createModelPart();
		handrail_4_r1.setPivot(0, -21.5809F, -0.2992F);
		middle_handrail.addChild(handrail_4_r1);
		setRotationAngle(handrail_4_r1, 0.1745F, 2.0944F, 0);
		handrail_4_r1.setTextureUVOffset(250, 0).addCuboid(-0.2591F, 6.0003F, -1.9152F, 0, 4, 0, 0.2F, false);

		handrail_3_r3 = createModelPart();
		handrail_3_r3.setPivot(0, -21.5809F, -0.2992F);
		middle_handrail.addChild(handrail_3_r3);
		setRotationAngle(handrail_3_r3, 0.1745F, -2.0944F, 0);
		handrail_3_r3.setTextureUVOffset(250, 0).addCuboid(0.2591F, 6.0003F, -1.9152F, 0, 4, 0, 0.2F, false);

		handrail_2_r3 = createModelPart();
		handrail_2_r3.setPivot(0, -21.5809F, -0.2992F);
		middle_handrail.addChild(handrail_2_r3);
		setRotationAngle(handrail_2_r3, 0.1745F, 0, 0);
		handrail_2_r3.setTextureUVOffset(250, 0).addCuboid(0, 6.0782F, -1.4732F, 0, 4, 0, 0.2F, false);

		head = createModelPart();
		head.setPivot(0, 24, 0);


		side_1 = createModelPart();
		side_1.setPivot(0, 0, 0);
		head.addChild(side_1);
		side_1.setTextureUVOffset(98, 94).addCuboid(0, 0, -14, 19, 2, 26, 0, false);
		side_1.setTextureUVOffset(194, 109).addCuboid(20, -10, 0, 1, 5, 12, 0, false);
		side_1.setTextureUVOffset(30, 42).addCuboid(16.734F, -33.9011F, 6, 2, 0, 6, 0, false);
		side_1.setTextureUVOffset(0, 69).addCuboid(0, -37.3304F, 9, 7, 0, 3, 0, false);
		side_1.setTextureUVOffset(124, 67).addCuboid(0, -33.9011F, -10, 19, 0, 16, 0, false);
		side_1.setTextureUVOffset(14, 0).addCuboid(0, -10, -14, 6, 10, 1, 0, false);
		side_1.setTextureUVOffset(157, 122).addCuboid(0, -20, -10, 6, 5, 3, 0, false);

		bar_2_r1 = createModelPart();
		bar_2_r1.setPivot(6, 0, -10);
		side_1.addChild(bar_2_r1);
		setRotationAngle(bar_2_r1, 0, -0.3491F, 0);
		bar_2_r1.setTextureUVOffset(188, 73).addCuboid(0, -19, 0, 15, 3, 2, 0, false);

		front_11_r1 = createModelPart();
		front_11_r1.setPivot(15.9636F, -23.2081F, -8.1364F);
		side_1.addChild(front_11_r1);
		setRotationAngle(front_11_r1, -0.1745F, -0.7854F, 0);
		front_11_r1.setTextureUVOffset(190, 0).addCuboid(-3, -13.5F, -0.5F, 8, 27, 1, 0, false);

		front_10_r1 = createModelPart();
		front_10_r1.setPivot(21, -10, 0);
		side_1.addChild(front_10_r1);
		setRotationAngle(front_10_r1, 0, 0.1745F, -0.0873F);
		front_10_r1.setTextureUVOffset(153, 184).addCuboid(-1, -27, -9, 1, 27, 9, 0, false);

		front_9_r1 = createModelPart();
		front_9_r1.setPivot(21, 0, 0);
		side_1.addChild(front_9_r1);
		setRotationAngle(front_9_r1, 0, 0.1745F, 0);
		front_9_r1.setTextureUVOffset(200, 20).addCuboid(-1, -10, -9, 1, 5, 9, 0, false);

		front_8_r1 = createModelPart();
		front_8_r1.setPivot(17.6158F, -7.5F, -9.7886F);
		side_1.addChild(front_8_r1);
		setRotationAngle(front_8_r1, 0, -0.7854F, 0);
		front_8_r1.setTextureUVOffset(160, 130).addCuboid(-3, -2.5F, -0.5F, 6, 5, 1, 0, false);

		front_4_r1 = createModelPart();
		front_4_r1.setPivot(21, -5, 0);
		side_1.addChild(front_4_r1);
		setRotationAngle(front_4_r1, 0, 0.1745F, 0.1745F);
		front_4_r1.setTextureUVOffset(194, 139).addCuboid(-1, 0, -10, 1, 6, 10, 0, false);

		front_3_r1 = createModelPart();
		front_3_r1.setPivot(17.1096F, -2.5531F, -9.9895F);
		side_1.addChild(front_3_r1);
		setRotationAngle(front_3_r1, 0.0873F, -0.7854F, 0);
		front_3_r1.setTextureUVOffset(0, 81).addCuboid(-3.5F, -2.5F, -0.5F, 7, 6, 1, 0, false);

		front_2_r1 = createModelPart();
		front_2_r1.setPivot(6, 0, -14);
		side_1.addChild(front_2_r1);
		setRotationAngle(front_2_r1, 0, -0.1745F, 0);
		front_2_r1.setTextureUVOffset(168, 0).addCuboid(0, -10, 0, 10, 10, 1, 0, false);

		roof_8_r1 = createModelPart();
		roof_8_r1.setPivot(7.5F, -36.2001F, 8.4821F);
		side_1.addChild(roof_8_r1);
		setRotationAngle(roof_8_r1, 1.0472F, 0, 0);
		roof_8_r1.setTextureUVOffset(157, 156).addCuboid(-7.5F, 0, -1.5F, 15, 0, 3, 0, false);

		roof_7_r1 = createModelPart();
		roof_7_r1.setPivot(8.5F, -34.4011F, 6.866F);
		side_1.addChild(roof_7_r1);
		setRotationAngle(roof_7_r1, 0.5236F, 0, 0);
		roof_7_r1.setTextureUVOffset(124, 122).addCuboid(-8.5F, 0, -1, 17, 0, 2, 0, false);

		roof_4_r6 = createModelPart();
		roof_4_r6.setPivot(10.0171F, -36.9817F, 10);
		side_1.addChild(roof_4_r6);
		setRotationAngle(roof_4_r6, 0, 0, 0.0873F);
		roof_4_r6.setTextureUVOffset(120, 67).addCuboid(-4, 0, -2, 8, 0, 4, 0, false);

		roof_3_r7 = createModelPart();
		roof_3_r7.setPivot(14.5019F, -35.7671F, 9.5F);
		side_1.addChild(roof_3_r7);
		setRotationAngle(roof_3_r7, 0, 0, 1.0472F);
		roof_3_r7.setTextureUVOffset(19, 48).addCuboid(-1, 0, -2.5F, 2, 0, 5, 0, false);

		roof_2_r6 = createModelPart();
		roof_2_r6.setPivot(15.8679F, -34.4011F, 9);
		side_1.addChild(roof_2_r6);
		setRotationAngle(roof_2_r6, 0, 0, 0.5236F);
		roof_2_r6.setTextureUVOffset(18, 42).addCuboid(-1, 0, -3, 2, 0, 6, 0, false);

		wall_top_r3 = createModelPart();
		wall_top_r3.setPivot(21, -10, 0);
		side_1.addChild(wall_top_r3);
		setRotationAngle(wall_top_r3, 0, 0, -0.0873F);
		wall_top_r3.setTextureUVOffset(0, 0).addCuboid(-1, -27, 0, 1, 27, 12, 0, false);

		wall_bottom_r3 = createModelPart();
		wall_bottom_r3.setPivot(21, -5, 0);
		side_1.addChild(wall_bottom_r3);
		setRotationAngle(wall_bottom_r3, 0, 0, 0.1745F);
		wall_bottom_r3.setTextureUVOffset(193, 175).addCuboid(-1, 0, 0, 1, 6, 12, 0, false);

		side_2 = createModelPart();
		side_2.setPivot(0, 0, 0);
		head.addChild(side_2);
		side_2.setTextureUVOffset(98, 94).addCuboid(-19, 0, -14, 19, 2, 26, 0, true);
		side_2.setTextureUVOffset(194, 109).addCuboid(-21, -10, 0, 1, 5, 12, 0, true);
		side_2.setTextureUVOffset(30, 42).addCuboid(-18.734F, -33.9011F, 6, 2, 0, 6, 0, true);
		side_2.setTextureUVOffset(0, 69).addCuboid(-7, -37.3304F, 9, 7, 0, 3, 0, true);
		side_2.setTextureUVOffset(124, 67).addCuboid(-19, -33.9011F, -10, 19, 0, 16, 0, true);
		side_2.setTextureUVOffset(14, 0).addCuboid(-6, -10, -14, 6, 10, 1, 0, true);
		side_2.setTextureUVOffset(157, 122).addCuboid(-6, -20, -10, 6, 5, 3, 0, true);

		bar_3_r1 = createModelPart();
		bar_3_r1.setPivot(-6, 0, -10);
		side_2.addChild(bar_3_r1);
		setRotationAngle(bar_3_r1, 0, 0.3491F, 0);
		bar_3_r1.setTextureUVOffset(188, 73).addCuboid(-15, -19, 0, 15, 3, 2, 0, true);

		front_12_r1 = createModelPart();
		front_12_r1.setPivot(-15.9636F, -23.2081F, -8.1364F);
		side_2.addChild(front_12_r1);
		setRotationAngle(front_12_r1, -0.1745F, 0.7854F, 0);
		front_12_r1.setTextureUVOffset(190, 0).addCuboid(-5, -13.5F, -0.5F, 8, 27, 1, 0, true);

		front_11_r2 = createModelPart();
		front_11_r2.setPivot(-21, -10, 0);
		side_2.addChild(front_11_r2);
		setRotationAngle(front_11_r2, 0, -0.1745F, 0.0873F);
		front_11_r2.setTextureUVOffset(153, 184).addCuboid(0, -27, -9, 1, 27, 9, 0, true);

		front_10_r2 = createModelPart();
		front_10_r2.setPivot(-21, 0, 0);
		side_2.addChild(front_10_r2);
		setRotationAngle(front_10_r2, 0, -0.1745F, 0);
		front_10_r2.setTextureUVOffset(200, 20).addCuboid(0, -10, -9, 1, 5, 9, 0, true);

		front_9_r2 = createModelPart();
		front_9_r2.setPivot(-17.6158F, -7.5F, -9.7886F);
		side_2.addChild(front_9_r2);
		setRotationAngle(front_9_r2, 0, 0.7854F, 0);
		front_9_r2.setTextureUVOffset(160, 130).addCuboid(-3, -2.5F, -0.5F, 6, 5, 1, 0, true);

		front_5_r1 = createModelPart();
		front_5_r1.setPivot(-21, -5, 0);
		side_2.addChild(front_5_r1);
		setRotationAngle(front_5_r1, 0, -0.1745F, -0.1745F);
		front_5_r1.setTextureUVOffset(194, 139).addCuboid(0, 0, -10, 1, 6, 10, 0, true);

		front_4_r2 = createModelPart();
		front_4_r2.setPivot(-17.1096F, -2.5531F, -9.9895F);
		side_2.addChild(front_4_r2);
		setRotationAngle(front_4_r2, 0.0873F, 0.7854F, 0);
		front_4_r2.setTextureUVOffset(0, 81).addCuboid(-3.5F, -2.5F, -0.5F, 7, 6, 1, 0, true);

		front_3_r2 = createModelPart();
		front_3_r2.setPivot(-6, 0, -14);
		side_2.addChild(front_3_r2);
		setRotationAngle(front_3_r2, 0, 0.1745F, 0);
		front_3_r2.setTextureUVOffset(168, 0).addCuboid(-10, -10, 0, 10, 10, 1, 0, true);

		roof_9_r1 = createModelPart();
		roof_9_r1.setPivot(-7.5F, -36.2001F, 8.4821F);
		side_2.addChild(roof_9_r1);
		setRotationAngle(roof_9_r1, 1.0472F, 0, 0);
		roof_9_r1.setTextureUVOffset(157, 156).addCuboid(-7.5F, 0, -1.5F, 15, 0, 3, 0, true);

		roof_8_r2 = createModelPart();
		roof_8_r2.setPivot(-8.5F, -34.4011F, 6.866F);
		side_2.addChild(roof_8_r2);
		setRotationAngle(roof_8_r2, 0.5236F, 0, 0);
		roof_8_r2.setTextureUVOffset(124, 122).addCuboid(-8.5F, 0, -1, 17, 0, 2, 0, true);

		roof_5_r3 = createModelPart();
		roof_5_r3.setPivot(-10.0171F, -36.9817F, 10);
		side_2.addChild(roof_5_r3);
		setRotationAngle(roof_5_r3, 0, 0, -0.0873F);
		roof_5_r3.setTextureUVOffset(120, 67).addCuboid(-4, 0, -2, 8, 0, 4, 0, true);

		roof_4_r7 = createModelPart();
		roof_4_r7.setPivot(-14.5019F, -35.7671F, 9.5F);
		side_2.addChild(roof_4_r7);
		setRotationAngle(roof_4_r7, 0, 0, -1.0472F);
		roof_4_r7.setTextureUVOffset(19, 48).addCuboid(-1, 0, -2.5F, 2, 0, 5, 0, true);

		roof_3_r8 = createModelPart();
		roof_3_r8.setPivot(-15.8679F, -34.4011F, 9);
		side_2.addChild(roof_3_r8);
		setRotationAngle(roof_3_r8, 0, 0, -0.5236F);
		roof_3_r8.setTextureUVOffset(18, 42).addCuboid(-1, 0, -3, 2, 0, 6, 0, true);

		wall_top_r4 = createModelPart();
		wall_top_r4.setPivot(-21, -10, 0);
		side_2.addChild(wall_top_r4);
		setRotationAngle(wall_top_r4, 0, 0, 0.0873F);
		wall_top_r4.setTextureUVOffset(0, 0).addCuboid(0, -27, 0, 1, 27, 12, 0, true);

		wall_bottom_r4 = createModelPart();
		wall_bottom_r4.setPivot(-21, -5, 0);
		side_2.addChild(wall_bottom_r4);
		setRotationAngle(wall_bottom_r4, 0, 0, -0.1745F);
		wall_bottom_r4.setTextureUVOffset(193, 175).addCuboid(0, 0, 0, 1, 6, 12, 0, true);

		head_exterior = createModelPart();
		head_exterior.setPivot(0, 24, 0);


		side_1_exterior = createModelPart();
		side_1_exterior.setPivot(0, 0, 0);
		head_exterior.addChild(side_1_exterior);
		side_1_exterior.setTextureUVOffset(98, 101).addCuboid(21, -10, 0, 0, 5, 12, 0, false);
		side_1_exterior.setTextureUVOffset(112, 94).addCuboid(0, -10, -14, 6, 10, 0, 0, false);
		side_1_exterior.setTextureUVOffset(79, 94).addCuboid(-0.0293F, -41.2781F, -7, 7, 0, 19, 0, false);

		roof_8_r3 = createModelPart();
		roof_8_r3.setPivot(3, -40.5084F, -7.6415F);
		side_1_exterior.addChild(roof_8_r3);
		setRotationAngle(roof_8_r3, -0.8727F, 0, 0);
		roof_8_r3.setTextureUVOffset(32, 166).addCuboid(-3, -1.5F, 0, 6, 3, 0, 0, false);

		roof_7_r2 = createModelPart();
		roof_7_r2.setPivot(9.3275F, -40.5084F, -6.9567F);
		side_1_exterior.addChild(roof_7_r2);
		setRotationAngle(roof_7_r2, -0.8727F, -0.1745F, 0);
		roof_7_r2.setTextureUVOffset(140, 87).addCuboid(-4.5F, -1.5F, 0, 9, 3, 0, 0, false);

		roof_6_r2 = createModelPart();
		roof_6_r2.setPivot(14.1804F, -40.5084F, -4.9389F);
		side_1_exterior.addChild(roof_6_r2);
		setRotationAngle(roof_6_r2, -0.8727F, -0.7854F, 0);
		roof_6_r2.setTextureUVOffset(140, 90).addCuboid(-4, -1.5F, 0, 8, 3, 0, 0, false);

		roof_5_r4 = createModelPart();
		roof_5_r4.setPivot(17.7514F, -38.226F, -1.9696F);
		side_1_exterior.addChild(roof_5_r4);
		setRotationAngle(roof_5_r4, 0, 0.1745F, -0.2618F);
		roof_5_r4.setTextureUVOffset(8, 68).addCuboid(0, -2.5F, -2, 0, 5, 4, 0, false);

		roof_3_r9 = createModelPart();
		roof_3_r9.setPivot(11.9516F, -40.8423F, 0);
		side_1_exterior.addChild(roof_3_r9);
		setRotationAngle(roof_3_r9, 0, 0, 0.0873F);
		roof_3_r9.setTextureUVOffset(0, 70).addCuboid(-5, 0, -7, 10, 0, 19, 0, false);

		roof_2_r7 = createModelPart();
		roof_2_r7.setPivot(17.3157F, -40.0862F, 0);
		side_1_exterior.addChild(roof_2_r7);
		setRotationAngle(roof_2_r7, 0, 0, -0.8727F);
		roof_2_r7.setTextureUVOffset(0, 23).addCuboid(0, -0.5F, -4, 0, 1, 16, 0, false);

		roof_1_r3 = createModelPart();
		roof_1_r3.setPivot(18.2163F, -37.8329F, 0);
		side_1_exterior.addChild(roof_1_r3);
		setRotationAngle(roof_1_r3, 0, 0, -0.2618F);
		roof_1_r3.setTextureUVOffset(0, 114).addCuboid(0, -2, 0, 0, 4, 12, 0, false);

		front_14_r1 = createModelPart();
		front_14_r1.setPivot(0, -10, -14);
		side_1_exterior.addChild(front_14_r1);
		setRotationAngle(front_14_r1, -0.1745F, 0, 0);
		front_14_r1.setTextureUVOffset(118, 203).addCuboid(0, -30, 0, 6, 30, 0, 0, false);

		front_13_r1 = createModelPart();
		front_13_r1.setPivot(6, -10, -14);
		side_1_exterior.addChild(front_13_r1);
		setRotationAngle(front_13_r1, -0.1745F, -0.1745F, 0);
		front_13_r1.setTextureUVOffset(173, 203).addCuboid(0, -30, 0, 10, 30, 0, 0, false);

		front_12_r2 = createModelPart();
		front_12_r2.setPivot(16.6707F, -23.2081F, -7.4293F);
		side_1_exterior.addChild(front_12_r2);
		setRotationAngle(front_12_r2, -0.1745F, -0.7854F, 0);
		front_12_r2.setTextureUVOffset(193, 203).addCuboid(-4, -16.5F, -0.5F, 8, 30, 0, 0, false);

		front_11_r3 = createModelPart();
		front_11_r3.setPivot(21, -10, 0);
		side_1_exterior.addChild(front_11_r3);
		setRotationAngle(front_11_r3, 0, 0.1745F, -0.0873F);
		front_11_r3.setTextureUVOffset(130, 182).addCuboid(0, -27, -9, 0, 27, 9, 0, false);

		front_10_r3 = createModelPart();
		front_10_r3.setPivot(21, 0, 0);
		side_1_exterior.addChild(front_10_r3);
		setRotationAngle(front_10_r3, 0, 0.1745F, 0);
		front_10_r3.setTextureUVOffset(0, 121).addCuboid(0, -10, -9, 0, 5, 9, 0, false);

		front_9_r3 = createModelPart();
		front_9_r3.setPivot(17.6158F, -7.5F, -9.7886F);
		side_1_exterior.addChild(front_9_r3);
		setRotationAngle(front_9_r3, 0, -0.7854F, 0);
		front_9_r3.setTextureUVOffset(162, 102).addCuboid(-3, -2.5F, -0.5F, 6, 5, 0, 0, false);

		front_8_r2 = createModelPart();
		front_8_r2.setPivot(15.9306F, 5.3106F, -9.5176F);
		side_1_exterior.addChild(front_8_r2);
		setRotationAngle(front_8_r2, 0.1745F, -0.7854F, 0);
		front_8_r2.setTextureUVOffset(0, 0).addCuboid(-3, -5.5F, -0.5F, 6, 11, 0, 0, false);

		front_7_r1 = createModelPart();
		front_7_r1.setPivot(6, 0, -14);
		side_1_exterior.addChild(front_7_r1);
		setRotationAngle(front_7_r1, 0.2618F, -0.1745F, 0);
		front_7_r1.setTextureUVOffset(126, 28).addCuboid(0, 0, 0, 11, 11, 0, 0, false);

		front_6_r1 = createModelPart();
		front_6_r1.setPivot(0, 0, -14);
		side_1_exterior.addChild(front_6_r1);
		setRotationAngle(front_6_r1, 0.2618F, 0, 0);
		front_6_r1.setTextureUVOffset(26, 28).addCuboid(0, 0, 0, 6, 11, 0, 0, false);

		front_5_r2 = createModelPart();
		front_5_r2.setPivot(21, -5, 0);
		side_1_exterior.addChild(front_5_r2);
		setRotationAngle(front_5_r2, 0, 0.1745F, 0.1745F);
		front_5_r2.setTextureUVOffset(182, 42).addCuboid(0, 0, -10, 0, 16, 10, 0, false);

		front_4_r3 = createModelPart();
		front_4_r3.setPivot(17.1096F, -2.5531F, -9.9895F);
		side_1_exterior.addChild(front_4_r3);
		setRotationAngle(front_4_r3, 0.0873F, -0.7854F, 0);
		front_4_r3.setTextureUVOffset(160, 150).addCuboid(-3.5F, -2.5F, -0.5F, 7, 5, 0, 0, false);

		front_3_r3 = createModelPart();
		front_3_r3.setPivot(6, 0, -14);
		side_1_exterior.addChild(front_3_r3);
		setRotationAngle(front_3_r3, 0, -0.1745F, 0);
		front_3_r3.setTextureUVOffset(200, 34).addCuboid(0, -10, 0, 10, 10, 0, 0, false);

		wall_top_r5 = createModelPart();
		wall_top_r5.setPivot(21, -10, 0);
		side_1_exterior.addChild(wall_top_r5);
		setRotationAngle(wall_top_r5, 0, 0, -0.0873F);
		wall_top_r5.setTextureUVOffset(0, 30).addCuboid(0, -27, 0, 0, 27, 12, 0, false);

		wall_bottom_r5 = createModelPart();
		wall_bottom_r5.setPivot(21, -5, 0);
		side_1_exterior.addChild(wall_bottom_r5);
		setRotationAngle(wall_bottom_r5, 0, 0, 0.1745F);
		wall_bottom_r5.setTextureUVOffset(189, 81).addCuboid(0, 0, 0, 0, 16, 12, 0, false);

		side_2_exterior = createModelPart();
		side_2_exterior.setPivot(0, 0, 0);
		head_exterior.addChild(side_2_exterior);
		side_2_exterior.setTextureUVOffset(98, 101).addCuboid(-21, -10, 0, 0, 5, 12, 0, true);
		side_2_exterior.setTextureUVOffset(112, 94).addCuboid(-6, -10, -14, 6, 10, 0, 0, true);
		side_2_exterior.setTextureUVOffset(79, 94).addCuboid(-6.9707F, -41.2781F, -7, 7, 0, 19, 0, true);

		roof_9_r2 = createModelPart();
		roof_9_r2.setPivot(-3, -40.5084F, -7.6415F);
		side_2_exterior.addChild(roof_9_r2);
		setRotationAngle(roof_9_r2, -0.8727F, 0, 0);
		roof_9_r2.setTextureUVOffset(32, 166).addCuboid(-3, -1.5F, 0, 6, 3, 0, 0, true);

		roof_8_r4 = createModelPart();
		roof_8_r4.setPivot(-9.3275F, -40.5084F, -6.9567F);
		side_2_exterior.addChild(roof_8_r4);
		setRotationAngle(roof_8_r4, -0.8727F, 0.1745F, 0);
		roof_8_r4.setTextureUVOffset(140, 87).addCuboid(-4.5F, -1.5F, 0, 9, 3, 0, 0, true);

		roof_7_r3 = createModelPart();
		roof_7_r3.setPivot(-14.1804F, -40.5084F, -4.9389F);
		side_2_exterior.addChild(roof_7_r3);
		setRotationAngle(roof_7_r3, -0.8727F, 0.7854F, 0);
		roof_7_r3.setTextureUVOffset(140, 90).addCuboid(-4, -1.5F, 0, 8, 3, 0, 0, true);

		roof_6_r3 = createModelPart();
		roof_6_r3.setPivot(-17.7514F, -38.226F, -1.9696F);
		side_2_exterior.addChild(roof_6_r3);
		setRotationAngle(roof_6_r3, 0, -0.1745F, 0.2618F);
		roof_6_r3.setTextureUVOffset(8, 68).addCuboid(0, -2.5F, -2, 0, 5, 4, 0, true);

		roof_4_r8 = createModelPart();
		roof_4_r8.setPivot(-11.9516F, -40.8423F, 0);
		side_2_exterior.addChild(roof_4_r8);
		setRotationAngle(roof_4_r8, 0, 0, -0.0873F);
		roof_4_r8.setTextureUVOffset(0, 70).addCuboid(-5, 0, -7, 10, 0, 19, 0, true);

		roof_3_r10 = createModelPart();
		roof_3_r10.setPivot(-17.3157F, -40.0862F, 0);
		side_2_exterior.addChild(roof_3_r10);
		setRotationAngle(roof_3_r10, 0, 0, 0.8727F);
		roof_3_r10.setTextureUVOffset(0, 23).addCuboid(0, -0.5F, -4, 0, 1, 16, 0, true);

		roof_2_r8 = createModelPart();
		roof_2_r8.setPivot(-18.2163F, -37.8329F, 0);
		side_2_exterior.addChild(roof_2_r8);
		setRotationAngle(roof_2_r8, 0, 0, 0.2618F);
		roof_2_r8.setTextureUVOffset(0, 114).addCuboid(0, -2, 0, 0, 4, 12, 0, true);

		front_15_r1 = createModelPart();
		front_15_r1.setPivot(0, -10, -14);
		side_2_exterior.addChild(front_15_r1);
		setRotationAngle(front_15_r1, -0.1745F, 0, 0);
		front_15_r1.setTextureUVOffset(118, 203).addCuboid(-6, -30, 0, 6, 30, 0, 0, true);

		front_14_r2 = createModelPart();
		front_14_r2.setPivot(-6, -10, -14);
		side_2_exterior.addChild(front_14_r2);
		setRotationAngle(front_14_r2, -0.1745F, 0.1745F, 0);
		front_14_r2.setTextureUVOffset(173, 203).addCuboid(-10, -30, 0, 10, 30, 0, 0, true);

		front_13_r2 = createModelPart();
		front_13_r2.setPivot(-16.6707F, -23.2081F, -7.4293F);
		side_2_exterior.addChild(front_13_r2);
		setRotationAngle(front_13_r2, -0.1745F, 0.7854F, 0);
		front_13_r2.setTextureUVOffset(193, 203).addCuboid(-4, -16.5F, -0.5F, 8, 30, 0, 0, true);

		front_12_r3 = createModelPart();
		front_12_r3.setPivot(-21, -10, 0);
		side_2_exterior.addChild(front_12_r3);
		setRotationAngle(front_12_r3, 0, -0.1745F, 0.0873F);
		front_12_r3.setTextureUVOffset(130, 182).addCuboid(0, -27, -9, 0, 27, 9, 0, true);

		front_11_r4 = createModelPart();
		front_11_r4.setPivot(-21, 0, 0);
		side_2_exterior.addChild(front_11_r4);
		setRotationAngle(front_11_r4, 0, -0.1745F, 0);
		front_11_r4.setTextureUVOffset(0, 121).addCuboid(0, -10, -9, 0, 5, 9, 0, true);

		front_10_r4 = createModelPart();
		front_10_r4.setPivot(-17.6158F, -7.5F, -9.7886F);
		side_2_exterior.addChild(front_10_r4);
		setRotationAngle(front_10_r4, 0, 0.7854F, 0);
		front_10_r4.setTextureUVOffset(162, 102).addCuboid(-3, -2.5F, -0.5F, 6, 5, 0, 0, true);

		front_9_r4 = createModelPart();
		front_9_r4.setPivot(-15.9306F, 5.3106F, -9.5176F);
		side_2_exterior.addChild(front_9_r4);
		setRotationAngle(front_9_r4, 0.1745F, 0.7854F, 0);
		front_9_r4.setTextureUVOffset(0, 0).addCuboid(-3, -5.5F, -0.5F, 6, 11, 0, 0, true);

		front_8_r3 = createModelPart();
		front_8_r3.setPivot(-6, 0, -14);
		side_2_exterior.addChild(front_8_r3);
		setRotationAngle(front_8_r3, 0.2618F, 0.1745F, 0);
		front_8_r3.setTextureUVOffset(126, 28).addCuboid(-11, 0, 0, 11, 11, 0, 0, true);

		front_7_r2 = createModelPart();
		front_7_r2.setPivot(0, 0, -14);
		side_2_exterior.addChild(front_7_r2);
		setRotationAngle(front_7_r2, 0.2618F, 0, 0);
		front_7_r2.setTextureUVOffset(26, 28).addCuboid(-6, 0, 0, 6, 11, 0, 0, true);

		front_6_r2 = createModelPart();
		front_6_r2.setPivot(-21, -5, 0);
		side_2_exterior.addChild(front_6_r2);
		setRotationAngle(front_6_r2, 0, -0.1745F, -0.1745F);
		front_6_r2.setTextureUVOffset(182, 42).addCuboid(0, 0, -10, 0, 16, 10, 0, true);

		front_5_r3 = createModelPart();
		front_5_r3.setPivot(-17.1096F, -2.5531F, -9.9895F);
		side_2_exterior.addChild(front_5_r3);
		setRotationAngle(front_5_r3, 0.0873F, 0.7854F, 0);
		front_5_r3.setTextureUVOffset(160, 150).addCuboid(-3.5F, -2.5F, -0.5F, 7, 5, 0, 0, true);

		front_4_r4 = createModelPart();
		front_4_r4.setPivot(-6, 0, -14);
		side_2_exterior.addChild(front_4_r4);
		setRotationAngle(front_4_r4, 0, 0.1745F, 0);
		front_4_r4.setTextureUVOffset(200, 34).addCuboid(-10, -10, 0, 10, 10, 0, 0, true);

		wall_top_r6 = createModelPart();
		wall_top_r6.setPivot(-21, -10, 0);
		side_2_exterior.addChild(wall_top_r6);
		setRotationAngle(wall_top_r6, 0, 0, 0.0873F);
		wall_top_r6.setTextureUVOffset(0, 30).addCuboid(0, -27, 0, 0, 27, 12, 0, true);

		wall_bottom_r6 = createModelPart();
		wall_bottom_r6.setPivot(-21, -5, 0);
		side_2_exterior.addChild(wall_bottom_r6);
		setRotationAngle(wall_bottom_r6, 0, 0, -0.1745F);
		wall_bottom_r6.setTextureUVOffset(189, 81).addCuboid(0, 0, 0, 0, 16, 12, 0, true);

		door_light_on = createModelPart();
		door_light_on.setPivot(0, 24, 0);


		light_r3 = createModelPart();
		light_r3.setPivot(18.5F, -38, 0);
		door_light_on.addChild(light_r3);
		setRotationAngle(light_r3, 0, 0, -0.2618F);
		light_r3.setTextureUVOffset(28, 2).addCuboid(-1, -1, -0.5F, 1, 1, 1, 0, false);

		door_light_off = createModelPart();
		door_light_off.setPivot(0, 24, 0);


		light_r4 = createModelPart();
		light_r4.setPivot(18.5F, -38, 0);
		door_light_off.addChild(light_r4);
		setRotationAngle(light_r4, 0, 0, -0.2618F);
		light_r4.setTextureUVOffset(28, 0).addCuboid(-1, -1, -0.5F, 1, 1, 1, 0, false);

		headlights = createModelPart();
		headlights.setPivot(0, 24, 0);


		headlight_4_r1 = createModelPart();
		headlight_4_r1.setPivot(-6, 0, -14);
		headlights.addChild(headlight_4_r1);
		setRotationAngle(headlight_4_r1, 0.2618F, 0.1745F, 0);
		headlight_4_r1.setTextureUVOffset(208, 0).addCuboid(-11, 0, -0.05F, 11, 11, 0, 0, true);

		headlight_3_r1 = createModelPart();
		headlight_3_r1.setPivot(-15.9306F, 5.3106F, -9.5176F);
		headlights.addChild(headlight_3_r1);
		setRotationAngle(headlight_3_r1, 0.1745F, 0.7854F, 0);
		headlight_3_r1.setTextureUVOffset(230, 0).addCuboid(-3, -5.5F, -0.55F, 6, 11, 0, 0, true);

		headlight_2_r1 = createModelPart();
		headlight_2_r1.setPivot(15.9306F, 5.3106F, -9.5176F);
		headlights.addChild(headlight_2_r1);
		setRotationAngle(headlight_2_r1, 0.1745F, -0.7854F, 0);
		headlight_2_r1.setTextureUVOffset(230, 0).addCuboid(-3, -5.5F, -0.55F, 6, 11, 0, 0, false);

		headlight_1_r1 = createModelPart();
		headlight_1_r1.setPivot(6, 0, -14);
		headlights.addChild(headlight_1_r1);
		setRotationAngle(headlight_1_r1, 0.2618F, -0.1745F, 0);
		headlight_1_r1.setTextureUVOffset(208, 0).addCuboid(0, 0, -0.05F, 11, 11, 0, 0, false);

		tail_lights = createModelPart();
		tail_lights.setPivot(0, 24, 0);


		headlight_4_r2 = createModelPart();
		headlight_4_r2.setPivot(-6, 0, -14);
		tail_lights.addChild(headlight_4_r2);
		setRotationAngle(headlight_4_r2, 0.2618F, 0.1745F, 0);
		headlight_4_r2.setTextureUVOffset(211, 11).addCuboid(-11, 0, -0.05F, 11, 11, 0, 0, true);

		headlight_3_r2 = createModelPart();
		headlight_3_r2.setPivot(-15.9306F, 5.3106F, -9.5176F);
		tail_lights.addChild(headlight_3_r2);
		setRotationAngle(headlight_3_r2, 0.1745F, 0.7854F, 0);
		headlight_3_r2.setTextureUVOffset(233, 11).addCuboid(-3, -5.5F, -0.55F, 6, 11, 0, 0, true);

		headlight_2_r2 = createModelPart();
		headlight_2_r2.setPivot(15.9306F, 5.3106F, -9.5176F);
		tail_lights.addChild(headlight_2_r2);
		setRotationAngle(headlight_2_r2, 0.1745F, -0.7854F, 0);
		headlight_2_r2.setTextureUVOffset(233, 11).addCuboid(-3, -5.5F, -0.55F, 6, 11, 0, 0, false);

		headlight_1_r2 = createModelPart();
		headlight_1_r2.setPivot(6, 0, -14);
		tail_lights.addChild(headlight_1_r2);
		setRotationAngle(headlight_1_r2, 0.2618F, -0.1745F, 0);
		headlight_1_r2.setTextureUVOffset(211, 11).addCuboid(0, 0, -0.05F, 11, 11, 0, 0, false);

		buildModel();
	}

	private static final int DOOR_MAX = 16;

	@Override
	public ModelMPL85 createNew(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		return new ModelMPL85(doorAnimationType, renderDoorOverlay);
	}

	@Override
	protected void renderWindowPositions(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
		switch (renderStage) {
			case LIGHT:
				renderMirror(light_window, graphicsHolder, light, position);
				break;
			case INTERIOR:
				renderMirror(window, graphicsHolder, light, position);
				break;
			case EXTERIOR:
				renderMirror(window_exterior, graphicsHolder, light, position);
				break;
		}
	}

	@Override
	protected void renderDoorPositions(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
		switch (renderStage) {
			case LIGHT:
				renderMirror(light_door, graphicsHolder, light, position);
				if (renderDetails) {
					if (doorLeftZ > 0) {
						renderOnce(door_light_on, graphicsHolder, light, position);
					}
					if (doorRightZ > 0) {
						renderOnceFlipped(door_light_on, graphicsHolder, light, position);
					}
				}
				break;
			case INTERIOR:
				door_left.setOffset(-doorLeftX, 0, -doorLeftZ);
				door_right.setOffset(-doorLeftX, 0, doorLeftZ);
				renderOnce(door, graphicsHolder, light, position);
				door_left.setOffset(-doorRightX, 0, -doorRightZ);
				door_right.setOffset(-doorRightX, 0, doorRightZ);
				renderOnceFlipped(door, graphicsHolder, light, position);
				if (renderDetails) {
					renderOnce(seat_1, graphicsHolder, light, position + 34);
					renderOnceFlipped(seat_2, graphicsHolder, light, position + 34);
					renderMirror(seat_3, graphicsHolder, light, position + 48);
					renderOnceFlipped(seat_1, graphicsHolder, light, position - 34);
					renderOnce(seat_2, graphicsHolder, light, position - 34);
					renderOnce(middle_handrail, graphicsHolder, light, position);
					if (!isIndex(-1, position, getDoorPositions())) {
						renderOnce(middle_handrail, graphicsHolder, light, position + 48);
					}
				}
				break;
			case EXTERIOR:
				door_left_exterior.setOffset(-doorLeftX, 0, -doorLeftZ);
				door_right_exterior.setOffset(-doorLeftX, 0, doorLeftZ);
				renderOnce(door_exterior, graphicsHolder, light, position);
				door_left_exterior.setOffset(-doorRightX, 0, -doorRightZ);
				door_right_exterior.setOffset(-doorRightX, 0, doorRightZ);
				renderOnceFlipped(door_exterior, graphicsHolder, light, position);
				renderMirror(isIndex(0, position, getDoorPositions()) ? roof_door_exterior_1 : isIndex(1, position, getDoorPositions()) ? roof_door_exterior_2 : roof_door_exterior_3, graphicsHolder, light, position);
				if (renderDetails) {
					if (doorLeftZ == 0) {
						renderOnce(door_light_off, graphicsHolder, light, position);
					}
					if (doorRightZ == 0) {
						renderOnceFlipped(door_light_off, graphicsHolder, light, position);
					}
				}
				break;
		}
	}

	@Override
	protected void renderHeadPosition1(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean useHeadlights) {
		renderEndPosition1(graphicsHolder, renderStage, light, position, renderDetails, useHeadlights, true);
	}

	@Override
	protected void renderHeadPosition2(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean useHeadlights) {
		renderEndPosition2(graphicsHolder, renderStage, light, position, useHeadlights, true);
	}

	@Override
	protected void renderEndPosition1(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
		renderEndPosition1(graphicsHolder, renderStage, light, position, renderDetails, true, false);
	}

	@Override
	protected void renderEndPosition2(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
		renderEndPosition2(graphicsHolder, renderStage, light, position, true, false);
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
		return new int[]{-130, -62, -34, 34, 62, 130};
	}

	@Override
	protected int[] getDoorPositions() {
		return new int[]{-96, 0, 96};
	}

	@Override
	protected int[] getEndPositions() {
		return new int[]{-156, 156};
	}

	@Override
	protected int getDoorMax() {
		return DOOR_MAX;
	}

	private void renderEndPosition1(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, boolean useHeadlights, boolean isHead) {
		switch (renderStage) {
			case ALWAYS_ON_LIGHT:
				if (isHead) {
					renderOnce(useHeadlights ? headlights : tail_lights, graphicsHolder, light, position);
				}
				break;
			case INTERIOR:
				renderOnce(head, graphicsHolder, light, position);
				if (renderDetails) {
					renderMirror(seat_3, graphicsHolder, light, position + 12);
				}
				break;
			case EXTERIOR:
				renderOnce(head_exterior, graphicsHolder, light, position);
				break;
		}
	}

	private void renderEndPosition2(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean useHeadlights, boolean isHead) {
		switch (renderStage) {
			case ALWAYS_ON_LIGHT:
				if (isHead) {
					renderOnceFlipped(useHeadlights ? headlights : tail_lights, graphicsHolder, light, position);
				}
				break;
			case INTERIOR:
				renderOnceFlipped(head, graphicsHolder, light, position);
				break;
			case EXTERIOR:
				renderOnceFlipped(head_exterior, graphicsHolder, light, position);
				break;
		}
	}
}