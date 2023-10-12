package org.mtr.mod.model;

import org.mtr.core.data.InterchangeColorsForStationName;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.ModelPartExtension;
import org.mtr.mod.client.DoorAnimationType;
import org.mtr.mod.client.ScrollingText;
import org.mtr.mod.render.StoredMatrixTransformations;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ModelBR423 extends ModelSimpleTrainBase<ModelBR423> {

	private final ModelPartExtension window_1_interior;
	private final ModelPartExtension curve_top_r1;
	private final ModelPartExtension curve_middle_r1;
	private final ModelPartExtension curve_bottom_r1;
	private final ModelPartExtension window_2_interior;
	private final ModelPartExtension curve_top_r2;
	private final ModelPartExtension curve_middle_r2;
	private final ModelPartExtension curve_bottom_r2;
	private final ModelPartExtension window_3_interior;
	private final ModelPartExtension curve_top_r3;
	private final ModelPartExtension curve_middle_r3;
	private final ModelPartExtension curve_bottom_r3;
	private final ModelPartExtension window_1_exterior;
	private final ModelPartExtension window_2_exterior;
	private final ModelPartExtension window_3_exterior;
	private final ModelPartExtension door_interior;
	private final ModelPartExtension curve_top_r4;
	private final ModelPartExtension curve_middle_r4;
	private final ModelPartExtension right_curve_2_r1;
	private final ModelPartExtension right_curve_1_r1;
	private final ModelPartExtension door_left_interior;
	private final ModelPartExtension door_right_interior;
	private final ModelPartExtension door_exterior;
	private final ModelPartExtension door_left_exterior;
	private final ModelPartExtension door_right_exterior;
	private final ModelPartExtension end_interior;
	private final ModelPartExtension end_exterior;
	private final ModelPartExtension end_exterior_left;
	private final ModelPartExtension roof_2_r1;
	private final ModelPartExtension roof_1_r1;
	private final ModelPartExtension end_exterior_right;
	private final ModelPartExtension roof_3_r1;
	private final ModelPartExtension roof_2_r2;
	private final ModelPartExtension roof_window_interior;
	private final ModelPartExtension roof_2_r3;
	private final ModelPartExtension roof_door_interior;
	private final ModelPartExtension roof_2_r4;
	private final ModelPartExtension roof_window_exterior_1;
	private final ModelPartExtension roof_2_r5;
	private final ModelPartExtension roof_1_r2;
	private final ModelPartExtension roof_window_exterior_2;
	private final ModelPartExtension roof_2_r6;
	private final ModelPartExtension roof_1_r3;
	private final ModelPartExtension roof_door_exterior;
	private final ModelPartExtension roof_2_r7;
	private final ModelPartExtension roof_1_r4;
	private final ModelPartExtension vent;
	private final ModelPartExtension light_window;
	private final ModelPartExtension light_door;
	private final ModelPartExtension light_head;
	private final ModelPartExtension roof_right_r1;
	private final ModelPartExtension roof_left_r1;
	private final ModelPartExtension head_exterior;
	private final ModelPartExtension front_5_r1;
	private final ModelPartExtension front_4_r1;
	private final ModelPartExtension front_3_r1;
	private final ModelPartExtension front_1_r1;
	private final ModelPartExtension head_exterior_left;
	private final ModelPartExtension side_roof_2_r1;
	private final ModelPartExtension side_roof_1_r1;
	private final ModelPartExtension side_4_r1;
	private final ModelPartExtension side_3_r1;
	private final ModelPartExtension side_2_r1;
	private final ModelPartExtension side_1_r1;
	private final ModelPartExtension roof_2_r8;
	private final ModelPartExtension roof_1_r5;
	private final ModelPartExtension head_exterior_right;
	private final ModelPartExtension side_roof_2_r2;
	private final ModelPartExtension side_roof_1_r2;
	private final ModelPartExtension side_4_r2;
	private final ModelPartExtension side_3_r2;
	private final ModelPartExtension side_2_r2;
	private final ModelPartExtension side_1_r2;
	private final ModelPartExtension roof_2_r9;
	private final ModelPartExtension roof_1_r6;
	private final ModelPartExtension head_interior;
	private final ModelPartExtension head_interior_left;
	private final ModelPartExtension roof_2_r10;
	private final ModelPartExtension head_interior_right;
	private final ModelPartExtension roof_2_r11;
	private final ModelPartExtension seat;
	private final ModelPartExtension seat_2_r1;
	private final ModelPartExtension headlights;
	private final ModelPartExtension side_2_r3;
	private final ModelPartExtension side_1_r3;
	private final ModelPartExtension front_5_r2;
	private final ModelPartExtension tail_lights;
	private final ModelPartExtension side_2_r4;
	private final ModelPartExtension side_1_r4;

	public ModelBR423() {
		this(DoorAnimationType.PLUG_FAST, true);
	}

	private ModelBR423(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		super(288, 288, doorAnimationType, renderDoorOverlay);

		window_1_interior = createModelPart();
		window_1_interior.setPivot(0, 24, 0);
		window_1_interior.setTextureUVOffset(105, 36).addCuboid(0, 0, -13.5F, 20, 1, 27, 0, false);
		window_1_interior.setTextureUVOffset(138, 150).addCuboid(20, -33, -13.5F, 0, 33, 27, 0, false);

		curve_top_r1 = createModelPart();
		curve_top_r1.setPivot(13, -37, 0);
		window_1_interior.addChild(curve_top_r1);
		setRotationAngle(curve_top_r1, 0, 0, 0.0873F);
		curve_top_r1.setTextureUVOffset(0, 27).addCuboid(-3, 0, -13.5F, 6, 0, 27, 0, false);

		curve_middle_r1 = createModelPart();
		curve_middle_r1.setPivot(19, -35, 0);
		window_1_interior.addChild(curve_middle_r1);
		setRotationAngle(curve_middle_r1, 0, 0, -1.0472F);
		curve_middle_r1.setTextureUVOffset(158, 73).addCuboid(0, -4, -13.5F, 0, 4, 27, 0, false);

		curve_bottom_r1 = createModelPart();
		curve_bottom_r1.setPivot(20, -33, 0);
		window_1_interior.addChild(curve_bottom_r1);
		setRotationAngle(curve_bottom_r1, 0, 0, -0.5236F);
		curve_bottom_r1.setTextureUVOffset(168, 81).addCuboid(0, -3, -13.5F, 0, 3, 27, 0, false);

		window_2_interior = createModelPart();
		window_2_interior.setPivot(0, 24, 0);
		window_2_interior.setTextureUVOffset(91, 77).addCuboid(0, 0, -13.5F, 20, 1, 27, 0, false);
		window_2_interior.setTextureUVOffset(138, 117).addCuboid(20, -33, -13.5F, 0, 33, 27, 0, false);

		curve_top_r2 = createModelPart();
		curve_top_r2.setPivot(13, -37, 0);
		window_2_interior.addChild(curve_top_r2);
		setRotationAngle(curve_top_r2, 0, 0, 0.0873F);
		curve_top_r2.setTextureUVOffset(0, 0).addCuboid(-3, 0, -13.5F, 6, 0, 27, 0, false);

		curve_middle_r2 = createModelPart();
		curve_middle_r2.setPivot(19, -35, 0);
		window_2_interior.addChild(curve_middle_r2);
		setRotationAngle(curve_middle_r2, 0, 0, -1.0472F);
		curve_middle_r2.setTextureUVOffset(158, 69).addCuboid(0, -4, -13.5F, 0, 4, 27, 0, false);

		curve_bottom_r2 = createModelPart();
		curve_bottom_r2.setPivot(20, -33, 0);
		window_2_interior.addChild(curve_bottom_r2);
		setRotationAngle(curve_bottom_r2, 0, 0, -0.5236F);
		curve_bottom_r2.setTextureUVOffset(168, 78).addCuboid(0, -3, -13.5F, 0, 3, 27, 0, false);

		window_3_interior = createModelPart();
		window_3_interior.setPivot(0, 24, 0);
		window_3_interior.setTextureUVOffset(194, 260).addCuboid(0, 0, -13.5F, 20, 1, 27, 0, false);
		window_3_interior.setTextureUVOffset(166, 213).addCuboid(20, -33, -13.5F, 0, 33, 27, 0, false);

		curve_top_r3 = createModelPart();
		curve_top_r3.setPivot(13, -37, 0);
		window_3_interior.addChild(curve_top_r3);
		setRotationAngle(curve_top_r3, 0, 0, 0.0873F);
		curve_top_r3.setTextureUVOffset(243, 0).addCuboid(-3, 0, -13.5F, 6, 0, 27, 0, false);

		curve_middle_r3 = createModelPart();
		curve_middle_r3.setPivot(19, -35, 0);
		window_3_interior.addChild(curve_middle_r3);
		setRotationAngle(curve_middle_r3, 0, 0, -1.0472F);
		curve_middle_r3.setTextureUVOffset(208, 104).addCuboid(0, -4, -13.5F, 0, 4, 27, 0, false);

		curve_bottom_r3 = createModelPart();
		curve_bottom_r3.setPivot(20, -33, 0);
		window_3_interior.addChild(curve_bottom_r3);
		setRotationAngle(curve_bottom_r3, 0, 0, -0.5236F);
		curve_bottom_r3.setTextureUVOffset(208, 101).addCuboid(0, -3, -13.5F, 0, 3, 27, 0, false);

		window_1_exterior = createModelPart();
		window_1_exterior.setPivot(0, 24, 0);
		window_1_exterior.setTextureUVOffset(192, 173).addCuboid(20, 0, -13.5F, 1, 4, 27, 0, false);
		window_1_exterior.setTextureUVOffset(0, 120).addCuboid(21, -36, -13.5F, 0, 36, 27, 0, false);

		window_2_exterior = createModelPart();
		window_2_exterior.setPivot(0, 24, 0);
		window_2_exterior.setTextureUVOffset(192, 37).addCuboid(20, 0, -13.5F, 1, 4, 27, 0, false);
		window_2_exterior.setTextureUVOffset(84, 116).addCuboid(21, -36, -13.5F, 0, 36, 27, 0, false);

		window_3_exterior = createModelPart();
		window_3_exterior.setPivot(0, 24, 0);
		window_3_exterior.setTextureUVOffset(224, 142).addCuboid(20, 0, -13.5F, 1, 4, 27, 0, false);
		window_3_exterior.setTextureUVOffset(192, 177).addCuboid(21, -36, -13.5F, 0, 36, 27, 0, false);

		door_interior = createModelPart();
		door_interior.setPivot(0, 24, 0);
		door_interior.setTextureUVOffset(142, 117).addCuboid(0, 0, -13, 20, 1, 26, 0, false);
		door_interior.setTextureUVOffset(273, 27).addCuboid(16, -32, -13, 4, 32, 2, 0, false);
		door_interior.setTextureUVOffset(206, 17).addCuboid(16, -32, 11, 4, 32, 2, 0, false);
		door_interior.setTextureUVOffset(40, 231).addCuboid(6, -38, -12.9F, 10, 38, 0, 0, false);
		door_interior.setTextureUVOffset(40, 231).addCuboid(6, -38, 12.9F, 10, 38, 0, 0, false);
		door_interior.setTextureUVOffset(220, 229).addCuboid(16, -37, -13, 4, 5, 26, 0, false);

		curve_top_r4 = createModelPart();
		curve_top_r4.setPivot(12, -34, 0);
		door_interior.addChild(curve_top_r4);
		setRotationAngle(curve_top_r4, 0, 0, -0.5236F);
		curve_top_r4.setTextureUVOffset(8, 201).addCuboid(0, -4, -13, 1, 4, 26, 0, false);

		curve_middle_r4 = createModelPart();
		curve_middle_r4.setPivot(17, -32, 0);
		door_interior.addChild(curve_middle_r4);
		setRotationAngle(curve_middle_r4, 0, 0, 0.3927F);
		curve_middle_r4.setTextureUVOffset(82, 36).addCuboid(-6, 0, -13, 6, 0, 26, 0, false);

		right_curve_2_r1 = createModelPart();
		right_curve_2_r1.setPivot(12, -34, 0);
		door_interior.addChild(right_curve_2_r1);
		setRotationAngle(right_curve_2_r1, 0, 0, -0.7854F);
		right_curve_2_r1.setTextureUVOffset(0, 0).addCuboid(0, 0, 11, 2, 4, 2, 0, false);
		right_curve_2_r1.setTextureUVOffset(0, 6).addCuboid(0, 0, -13, 2, 4, 2, 0, false);

		right_curve_1_r1 = createModelPart();
		right_curve_1_r1.setPivot(16, -29, 0);
		door_interior.addChild(right_curve_1_r1);
		setRotationAngle(right_curve_1_r1, 0, 0, -0.5236F);
		right_curve_1_r1.setTextureUVOffset(105, 90).addCuboid(0, -3, 11, 3, 3, 2, 0, false);
		right_curve_1_r1.setTextureUVOffset(212, 96).addCuboid(0, -3, -13, 3, 3, 2, 0, false);

		door_left_interior = createModelPart();
		door_left_interior.setPivot(0, 24, 0);
		door_left_interior.setTextureUVOffset(248, 182).addCuboid(20, -35, -12, 1, 35, 12, 0, false);

		door_right_interior = createModelPart();
		door_right_interior.setPivot(0, 24, 0);
		door_right_interior.setTextureUVOffset(0, 0).addCuboid(20, -35, 0, 1, 35, 12, 0, false);

		door_exterior = createModelPart();
		door_exterior.setPivot(0, 24, 0);
		door_exterior.setTextureUVOffset(208, 90).addCuboid(20, 0, -13, 1, 4, 26, 0, false);
		door_exterior.setTextureUVOffset(262, 0).addCuboid(20, -35, -13, 1, 35, 1, 0, false);
		door_exterior.setTextureUVOffset(266, 0).addCuboid(20, -35, 12, 1, 35, 1, 0, false);
		door_exterior.setTextureUVOffset(60, 228).addCuboid(20, -36, -13, 1, 1, 26, 0, false);

		door_left_exterior = createModelPart();
		door_left_exterior.setPivot(0, 24, 0);
		door_left_exterior.setTextureUVOffset(62, 207).addCuboid(21, -35, -12, 0, 35, 12, 0, false);

		door_right_exterior = createModelPart();
		door_right_exterior.setPivot(0, 24, 0);
		door_right_exterior.setTextureUVOffset(88, 203).addCuboid(21, -35, 0, 0, 35, 12, 0, false);

		end_interior = createModelPart();
		end_interior.setPivot(0, 24, 0);
		end_interior.setTextureUVOffset(88, 33).addCuboid(-20, 0, 0, 40, 1, 2, 0, false);
		end_interior.setTextureUVOffset(20, 231).addCuboid(12, -35, 0, 8, 35, 2, 0, false);
		end_interior.setTextureUVOffset(0, 231).addCuboid(-20, -35, 0, 8, 35, 2, 0, true);
		end_interior.setTextureUVOffset(165, 144).addCuboid(12, -31, -27, 8, 0, 27, 0, false);
		end_interior.setTextureUVOffset(181, 144).addCuboid(-20, -31, -27, 8, 0, 27, 0, true);
		end_interior.setTextureUVOffset(156, 0).addCuboid(-20, -39, 0, 40, 4, 2, 0, false);

		end_exterior = createModelPart();
		end_exterior.setPivot(0, 24, 0);
		end_exterior.setTextureUVOffset(156, 7).addCuboid(-21, -43, 2, 42, 8, 0, 0, false);
		end_exterior.setTextureUVOffset(154, 15).addCuboid(-17, -43, 0, 34, 0, 2, 0, false);

		end_exterior_left = createModelPart();
		end_exterior_left.setPivot(0, 0, 0);
		end_exterior.addChild(end_exterior_left);
		end_exterior_left.setTextureUVOffset(164, 17).addCuboid(20, 0, 0, 1, 4, 2, 0, false);
		end_exterior_left.setTextureUVOffset(266, 104).addCuboid(21, -36, 0, 0, 36, 2, 0, false);
		end_exterior_left.setTextureUVOffset(268, 61).addCuboid(12, -35, 2, 9, 35, 0, 0, false);

		roof_2_r1 = createModelPart();
		roof_2_r1.setPivot(17, -43, 0);
		end_exterior_left.addChild(roof_2_r1);
		setRotationAngle(roof_2_r1, 0, 0, -0.6981F);
		roof_2_r1.setTextureUVOffset(78, 147).addCuboid(-1, 0, 0, 1, 4, 2, 0, false);

		roof_1_r1 = createModelPart();
		roof_1_r1.setPivot(21, -36, 0);
		end_exterior_left.addChild(roof_1_r1);
		setRotationAngle(roof_1_r1, 0, 0, -0.3491F);
		roof_1_r1.setTextureUVOffset(18, 5).addCuboid(-1, -5, 0, 1, 5, 2, 0, false);

		end_exterior_right = createModelPart();
		end_exterior_right.setPivot(0, 0, 0);
		end_exterior.addChild(end_exterior_right);
		end_exterior_right.setTextureUVOffset(164, 23).addCuboid(-21, 0, 0, 1, 4, 2, 0, true);
		end_exterior_right.setTextureUVOffset(262, 104).addCuboid(-21, -36, 0, 0, 36, 2, 0, true);
		end_exterior_right.setTextureUVOffset(0, 63).addCuboid(-21, -35, 2, 9, 35, 0, 0, true);

		roof_3_r1 = createModelPart();
		roof_3_r1.setPivot(-17, -43, 0);
		end_exterior_right.addChild(roof_3_r1);
		setRotationAngle(roof_3_r1, 0, 0, 0.6981F);
		roof_3_r1.setTextureUVOffset(78, 153).addCuboid(0, 0, 0, 1, 4, 2, 0, true);

		roof_2_r2 = createModelPart();
		roof_2_r2.setPivot(-21, -36, 0);
		end_exterior_right.addChild(roof_2_r2);
		setRotationAngle(roof_2_r2, 0, 0, 0.3491F);
		roof_2_r2.setTextureUVOffset(14, 0).addCuboid(0, -5, 0, 1, 5, 2, 0, true);

		roof_window_interior = createModelPart();
		roof_window_interior.setPivot(0, 24, 0);
		roof_window_interior.setTextureUVOffset(12, 0).addCuboid(0, -38, -13.5F, 6, 0, 27, 0, false);

		roof_2_r3 = createModelPart();
		roof_2_r3.setPivot(6, -38, 0);
		roof_window_interior.addChild(roof_2_r3);
		setRotationAngle(roof_2_r3, 0, 0, 0.1745F);
		roof_2_r3.setTextureUVOffset(12, 27).addCuboid(0, 0, -13.5F, 5, 0, 27, 0, false);

		roof_door_interior = createModelPart();
		roof_door_interior.setPivot(0, 24, 0);
		roof_door_interior.setTextureUVOffset(79, 64).addCuboid(0, -38, -13, 6, 0, 26, 0, false);

		roof_2_r4 = createModelPart();
		roof_2_r4.setPivot(6, -38, 0);
		roof_door_interior.addChild(roof_2_r4);
		setRotationAngle(roof_2_r4, 0, 0, 0.1745F);
		roof_2_r4.setTextureUVOffset(94, 36).addCuboid(0, 0, -13, 5, 0, 26, 0, false);

		roof_window_exterior_1 = createModelPart();
		roof_window_exterior_1.setPivot(0, 24, 0);
		roof_window_exterior_1.setTextureUVOffset(27, 0).addCuboid(0, -43, -13.5F, 17, 0, 27, 0, false);

		roof_2_r5 = createModelPart();
		roof_2_r5.setPivot(17, -43, 0);
		roof_window_exterior_1.addChild(roof_2_r5);
		setRotationAngle(roof_2_r5, 0, 0, -0.6981F);
		roof_2_r5.setTextureUVOffset(0, 32).addCuboid(0, 0, -13.5F, 0, 4, 27, 0, false);

		roof_1_r2 = createModelPart();
		roof_1_r2.setPivot(21, -36, 0);
		roof_window_exterior_1.addChild(roof_1_r2);
		setRotationAngle(roof_1_r2, 0, 0, -0.3491F);
		roof_1_r2.setTextureUVOffset(0, 27).addCuboid(0, -5, -13.5F, 0, 5, 27, 0, false);

		roof_window_exterior_2 = createModelPart();
		roof_window_exterior_2.setPivot(0, 24, 0);
		roof_window_exterior_2.setTextureUVOffset(27, 27).addCuboid(0, -43, -13.5F, 17, 0, 27, 0, false);

		roof_2_r6 = createModelPart();
		roof_2_r6.setPivot(17, -43, 0);
		roof_window_exterior_2.addChild(roof_2_r6);
		setRotationAngle(roof_2_r6, 0, 0, -0.6981F);
		roof_2_r6.setTextureUVOffset(54, 32).addCuboid(0, 0, -13.5F, 0, 4, 27, 0, false);

		roof_1_r3 = createModelPart();
		roof_1_r3.setPivot(21, -36, 0);
		roof_window_exterior_2.addChild(roof_1_r3);
		setRotationAngle(roof_1_r3, 0, 0, -0.3491F);
		roof_1_r3.setTextureUVOffset(54, 27).addCuboid(0, -5, -13.5F, 0, 5, 27, 0, false);

		roof_door_exterior = createModelPart();
		roof_door_exterior.setPivot(0, 24, 0);
		roof_door_exterior.setTextureUVOffset(132, 64).addCuboid(0, -43, -13, 17, 0, 26, 0, false);

		roof_2_r7 = createModelPart();
		roof_2_r7.setPivot(17, -43, 0);
		roof_door_exterior.addChild(roof_2_r7);
		setRotationAngle(roof_2_r7, 0, 0, -0.6981F);
		roof_2_r7.setTextureUVOffset(36, 189).addCuboid(0, 0, -13, 0, 4, 26, 0, false);

		roof_1_r4 = createModelPart();
		roof_1_r4.setPivot(21, -36, 0);
		roof_door_exterior.addChild(roof_1_r4);
		setRotationAngle(roof_1_r4, 0, 0, -0.3491F);
		roof_1_r4.setTextureUVOffset(168, 85).addCuboid(0, -5, -13, 0, 5, 26, 0, false);

		vent = createModelPart();
		vent.setPivot(0, 24, 0);
		vent.setTextureUVOffset(74, 237).addCuboid(-13, -49, -40, 26, 6, 40, 0, false);

		light_window = createModelPart();
		light_window.setPivot(0, 24, 0);
		light_window.setTextureUVOffset(217, 0).addCuboid(2, -38.2F, -13.5F, 4, 0, 27, 0, false);

		light_door = createModelPart();
		light_door.setPivot(0, 24, 0);
		light_door.setTextureUVOffset(218, 0).addCuboid(2, -38.2F, -13, 4, 0, 26, 0, false);

		light_head = createModelPart();
		light_head.setPivot(0, 24, 0);
		light_head.setTextureUVOffset(127, 210).addCuboid(-6, -38.2F, -13.5F, 12, 0, 27, 0, false);

		roof_right_r1 = createModelPart();
		roof_right_r1.setPivot(-6, -38.2F, 5);
		light_head.addChild(roof_right_r1);
		setRotationAngle(roof_right_r1, 0, 0, -0.1745F);
		roof_right_r1.setTextureUVOffset(151, 210).addCuboid(-5, 0, -18.5F, 5, 0, 27, 0, true);

		roof_left_r1 = createModelPart();
		roof_left_r1.setPivot(6, -38.2F, 5);
		light_head.addChild(roof_left_r1);
		setRotationAngle(roof_left_r1, 0, 0, 0.1745F);
		roof_left_r1.setTextureUVOffset(117, 210).addCuboid(0, 0, -18.5F, 5, 0, 27, 0, false);

		head_exterior = createModelPart();
		head_exterior.setPivot(0, 24, 0);
		head_exterior.setTextureUVOffset(-22, 63).addCuboid(-21, 0, -40, 42, 0, 40, 0, false);
		head_exterior.setTextureUVOffset(221, 173).addCuboid(-12, -11, -40, 24, 9, 0, 0, false);
		head_exterior.setTextureUVOffset(68, 0).addCuboid(-17, -43, -20, 34, 0, 20, 0, false);
		head_exterior.setTextureUVOffset(0, 104).addCuboid(-21, -43, -1, 42, 43, 0, 0, false);

		front_5_r1 = createModelPart();
		front_5_r1.setPivot(0, -43, -20);
		head_exterior.addChild(front_5_r1);
		setRotationAngle(front_5_r1, 0.5236F, 0, 0);
		front_5_r1.setTextureUVOffset(80, 20).addCuboid(-18, 0, -8, 36, 0, 8, 0, false);

		front_4_r1 = createModelPart();
		front_4_r1.setPivot(0, -43, -20);
		head_exterior.addChild(front_4_r1);
		setRotationAngle(front_4_r1, 0.9599F, 0, 0);
		front_4_r1.setTextureUVOffset(-19, 269).addCuboid(-17, -3, -25, 34, 0, 19, 0, false);

		front_3_r1 = createModelPart();
		front_3_r1.setPivot(0, -11, -40);
		head_exterior.addChild(front_3_r1);
		setRotationAngle(front_3_r1, -0.2618F, 0, 0);
		front_3_r1.setTextureUVOffset(192, 68).addCuboid(-14, -16, 0, 28, 16, 0, 0, false);

		front_1_r1 = createModelPart();
		front_1_r1.setPivot(0, -2, -40);
		head_exterior.addChild(front_1_r1);
		setRotationAngle(front_1_r1, 0.0873F, 0, 0);
		front_1_r1.setTextureUVOffset(221, 55).addCuboid(-13, 0, 0, 26, 6, 0, 0, false);

		head_exterior_left = createModelPart();
		head_exterior_left.setPivot(0, 0, 0);
		head_exterior.addChild(head_exterior_left);
		head_exterior_left.setTextureUVOffset(221, 31).addCuboid(20, 0, -20, 1, 4, 20, 0, false);
		head_exterior_left.setTextureUVOffset(94, 159).addCuboid(21, -36, -20, 0, 36, 20, 0, false);

		side_roof_2_r1 = createModelPart();
		side_roof_2_r1.setPivot(17, -43, -20);
		head_exterior_left.addChild(side_roof_2_r1);
		setRotationAngle(side_roof_2_r1, 0, 0.2618F, -0.6981F);
		side_roof_2_r1.setTextureUVOffset(0, 178).addCuboid(0, 0, -5, 0, 4, 5, 0, false);

		side_roof_1_r1 = createModelPart();
		side_roof_1_r1.setPivot(21, -36, -20);
		head_exterior_left.addChild(side_roof_1_r1);
		setRotationAngle(side_roof_1_r1, 0, 0.2618F, -0.3491F);
		side_roof_1_r1.setTextureUVOffset(0, 41).addCuboid(0, -5, -6, 0, 7, 6, 0, false);

		side_4_r1 = createModelPart();
		side_4_r1.setPivot(17, -43, -20);
		head_exterior_left.addChild(side_4_r1);
		setRotationAngle(side_4_r1, 1.1781F, -0.6981F, 0);
		side_4_r1.setTextureUVOffset(204, 182).addCuboid(-13, -2.5F, -20, 12, 0, 17, 0, false);

		side_3_r1 = createModelPart();
		side_3_r1.setPivot(12, -11, -40);
		head_exterior_left.addChild(side_3_r1);
		setRotationAngle(side_3_r1, -0.1309F, -0.7854F, 0);
		side_3_r1.setTextureUVOffset(224, 144).addCuboid(0, -18, 0, 13, 18, 0, 0, false);

		side_2_r1 = createModelPart();
		side_2_r1.setPivot(12, 0, -40);
		head_exterior_left.addChild(side_2_r1);
		setRotationAngle(side_2_r1, 0, -0.7854F, 0);
		side_2_r1.setTextureUVOffset(224, 15).addCuboid(0, -11, 0, 10, 15, 0, 0, false);

		side_1_r1 = createModelPart();
		side_1_r1.setPivot(21, 0, -20);
		head_exterior_left.addChild(side_1_r1);
		setRotationAngle(side_1_r1, 0, 0.1745F, 0);
		side_1_r1.setTextureUVOffset(172, 7).addCuboid(0, -36, -14, 0, 40, 14, 0, false);

		roof_2_r8 = createModelPart();
		roof_2_r8.setPivot(17, -43, 0);
		head_exterior_left.addChild(roof_2_r8);
		setRotationAngle(roof_2_r8, 0, 0, -0.6981F);
		roof_2_r8.setTextureUVOffset(208, 104).addCuboid(0, 0, -20, 0, 4, 20, 0, false);

		roof_1_r5 = createModelPart();
		roof_1_r5.setPivot(21, -36, 0);
		head_exterior_left.addChild(roof_1_r5);
		setRotationAngle(roof_1_r5, 0, 0, -0.3491F);
		roof_1_r5.setTextureUVOffset(117, 44).addCuboid(0, -5, -20, 0, 5, 20, 0, false);

		head_exterior_right = createModelPart();
		head_exterior_right.setPivot(0, 0, 0);
		head_exterior.addChild(head_exterior_right);
		head_exterior_right.setTextureUVOffset(242, 82).addCuboid(-21, 0, -20, 1, 4, 20, 0, true);
		head_exterior_right.setTextureUVOffset(54, 159).addCuboid(-21, -36, -20, 0, 36, 20, 0, true);

		side_roof_2_r2 = createModelPart();
		side_roof_2_r2.setPivot(-17, -43, -20);
		head_exterior_right.addChild(side_roof_2_r2);
		setRotationAngle(side_roof_2_r2, 0, -0.2618F, 0.6981F);
		side_roof_2_r2.setTextureUVOffset(10, 178).addCuboid(0, 0, -5, 0, 4, 5, 0, true);

		side_roof_1_r2 = createModelPart();
		side_roof_1_r2.setPivot(-21, -36, -20);
		head_exterior_right.addChild(side_roof_1_r2);
		setRotationAngle(side_roof_1_r2, 0, -0.2618F, 0.3491F);
		side_roof_1_r2.setTextureUVOffset(12, 41).addCuboid(0, -5, -6, 0, 7, 6, 0, true);

		side_4_r2 = createModelPart();
		side_4_r2.setPivot(-17, -43, -20);
		head_exterior_right.addChild(side_4_r2);
		setRotationAngle(side_4_r2, 1.1781F, 0.6981F, 0);
		side_4_r2.setTextureUVOffset(37, 147).addCuboid(1, -2.5F, -20, 12, 0, 17, 0, true);

		side_3_r2 = createModelPart();
		side_3_r2.setPivot(-12, -11, -40);
		head_exterior_right.addChild(side_3_r2);
		setRotationAngle(side_3_r2, -0.1309F, 0.7854F, 0);
		side_3_r2.setTextureUVOffset(236, 84).addCuboid(-13, -18, 0, 13, 18, 0, 0, true);

		side_2_r2 = createModelPart();
		side_2_r2.setPivot(-12, 0, -40);
		head_exterior_right.addChild(side_2_r2);
		setRotationAngle(side_2_r2, 0, 0.7854F, 0);
		side_2_r2.setTextureUVOffset(248, 61).addCuboid(-10, -11, 0, 10, 15, 0, 0, true);

		side_1_r2 = createModelPart();
		side_1_r2.setPivot(-21, 0, -20);
		head_exterior_right.addChild(side_1_r2);
		setRotationAngle(side_1_r2, 0, -0.1745F, 0);
		side_1_r2.setTextureUVOffset(0, 173).addCuboid(0, -36, -14, 0, 40, 14, 0, true);

		roof_2_r9 = createModelPart();
		roof_2_r9.setPivot(-17, -43, 0);
		head_exterior_right.addChild(roof_2_r9);
		setRotationAngle(roof_2_r9, 0, 0, 0.6981F);
		roof_2_r9.setTextureUVOffset(208, 100).addCuboid(0, 0, -20, 0, 4, 20, 0, true);

		roof_1_r6 = createModelPart();
		roof_1_r6.setPivot(-21, -36, 0);
		head_exterior_right.addChild(roof_1_r6);
		setRotationAngle(roof_1_r6, 0, 0, 0.3491F);
		roof_1_r6.setTextureUVOffset(117, 49).addCuboid(0, -5, -20, 0, 5, 20, 0, true);

		head_interior = createModelPart();
		head_interior.setPivot(0, 24, 0);
		head_interior.setTextureUVOffset(84, 105).addCuboid(-21, -38, -13.5F, 42, 38, 0, 0, false);
		head_interior.setTextureUVOffset(165, 173).addCuboid(-6, -38, -13.5F, 12, 0, 27, 0, false);

		head_interior_left = createModelPart();
		head_interior_left.setPivot(0, 0, 0);
		head_interior.addChild(head_interior_left);


		roof_2_r10 = createModelPart();
		roof_2_r10.setPivot(6, -38, 5);
		head_interior_left.addChild(roof_2_r10);
		setRotationAngle(roof_2_r10, 0, 0, 0.1745F);
		roof_2_r10.setTextureUVOffset(9, 183).addCuboid(0, 0, -18.5F, 5, 0, 27, 0, false);

		head_interior_right = createModelPart();
		head_interior_right.setPivot(0, 0, 0);
		head_interior.addChild(head_interior_right);


		roof_2_r11 = createModelPart();
		roof_2_r11.setPivot(-6, -38, 5);
		head_interior_right.addChild(roof_2_r11);
		setRotationAngle(roof_2_r11, 0, 0, -0.1745F);
		roof_2_r11.setTextureUVOffset(107, 210).addCuboid(-5, 0, -18.5F, 5, 0, 27, 0, true);

		seat = createModelPart();
		seat.setPivot(0, 24, 0);
		seat.setTextureUVOffset(54, 166).addCuboid(-4, -6, -4, 8, 1, 7, 0, false);
		seat.setTextureUVOffset(199, 61).addCuboid(-3.5F, -19.644F, 4.0686F, 7, 2, 1, 0, false);

		seat_2_r1 = createModelPart();
		seat_2_r1.setPivot(0, -6, 2);
		seat.addChild(seat_2_r1);
		setRotationAngle(seat_2_r1, -0.1745F, 0, 0);
		seat_2_r1.setTextureUVOffset(88, 36).addCuboid(-4, -12, 0, 8, 12, 1, 0, false);

		headlights = createModelPart();
		headlights.setPivot(0, 24, 0);


		side_2_r3 = createModelPart();
		side_2_r3.setPivot(-12, 0, -40);
		headlights.addChild(side_2_r3);
		setRotationAngle(side_2_r3, 0, 0.7854F, 0);
		side_2_r3.setTextureUVOffset(221, 34).addCuboid(-8.1F, -11, -0.1F, 7, 4, 0, 0, true);

		side_1_r3 = createModelPart();
		side_1_r3.setPivot(12, 0, -40);
		headlights.addChild(side_1_r3);
		setRotationAngle(side_1_r3, 0, -0.7854F, 0);
		side_1_r3.setTextureUVOffset(221, 30).addCuboid(1.1F, -11, -0.1F, 7, 4, 0, 0, false);

		front_5_r2 = createModelPart();
		front_5_r2.setPivot(0, -43, -20);
		headlights.addChild(front_5_r2);
		setRotationAngle(front_5_r2, 0.5236F, 0, 0);
		front_5_r2.setTextureUVOffset(216, 43).addCuboid(-2, -0.2F, -7, 4, 0, 5, 0, false);

		tail_lights = createModelPart();
		tail_lights.setPivot(0, 24, 0);


		side_2_r4 = createModelPart();
		side_2_r4.setPivot(-12, 0, -40);
		tail_lights.addChild(side_2_r4);
		setRotationAngle(side_2_r4, 0, 0.7854F, 0);
		side_2_r4.setTextureUVOffset(243, 34).addCuboid(-8.1F, -11, -0.1F, 7, 4, 0, 0, true);

		side_1_r4 = createModelPart();
		side_1_r4.setPivot(12, 0, -40);
		tail_lights.addChild(side_1_r4);
		setRotationAngle(side_1_r4, 0, -0.7854F, 0);
		side_1_r4.setTextureUVOffset(243, 30).addCuboid(1.1F, -11, -0.1F, 7, 4, 0, 0, false);

		buildModel();
	}

	private static final int DOOR_MAX = 12;

	@Override
	public ModelBR423 createNew(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		return new ModelBR423(doorAnimationType, renderDoorOverlay);
	}

	@Override
	protected void renderWindowPositions(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
		final float[] windowPositions = {-106.5F, -53.5F, -26.5F, 26.5F, 53.5F, 106.5F};

		for (int i = 0; i < windowPositions.length; i++) {
			final float windowPosition = windowPositions[i];
			final boolean isEnd1 = isEnd1Head && i == 0;
			final boolean isEnd2 = isEnd2Head && i == windowPositions.length - 1;

			switch (renderStage) {
				case LIGHT:
					if (isEnd1) {
						renderOnce(light_head, graphicsHolder, light, windowPosition);
					} else if (isEnd2) {
						renderOnceFlipped(light_head, graphicsHolder, light, windowPosition);
					} else {
						renderMirror(light_window, graphicsHolder, light, windowPosition);
					}
					break;
				case INTERIOR:
					if (i == 2) {
						renderOnce(window_3_interior, graphicsHolder, light, windowPosition);
						renderOnceFlipped(window_1_interior, graphicsHolder, light, windowPosition);
					} else if (i == 3) {
						renderOnce(window_1_interior, graphicsHolder, light, windowPosition);
						renderOnceFlipped(window_3_interior, graphicsHolder, light, windowPosition);
					} else {
						renderMirror(isEnd1 || isEnd2 || i == 1 || i == 4 ? window_2_interior : window_1_interior, graphicsHolder, light, windowPosition);
					}
					if (isEnd1) {
						renderOnce(head_interior, graphicsHolder, light, windowPosition);
					} else if (isEnd2) {
						renderOnceFlipped(head_interior, graphicsHolder, light, windowPosition);
					} else {
						renderMirror(roof_window_interior, graphicsHolder, light, windowPosition);
					}
					if (renderDetails) {
						renderOnceFlipped(seat, graphicsHolder, light, -16, windowPosition - 7);
						renderOnceFlipped(seat, graphicsHolder, light, 16, windowPosition - 7);
						renderOnce(seat, graphicsHolder, light, -16, windowPosition + 7);
						renderOnce(seat, graphicsHolder, light, 16, windowPosition + 7);
						if (i != 1 && i != 3) {
							renderOnceFlipped(seat, graphicsHolder, light, -8, windowPosition - 7);
							renderOnceFlipped(seat, graphicsHolder, light, 8, windowPosition - 7);
						}
						if (i != 2 && i != 4) {
							renderOnce(seat, graphicsHolder, light, -8, windowPosition + 7);
							renderOnce(seat, graphicsHolder, light, 8, windowPosition + 7);
						}
					}
					break;
				case EXTERIOR:
					if (i == 2) {
						renderOnce(window_3_exterior, graphicsHolder, light, windowPosition);
						renderOnceFlipped(window_1_exterior, graphicsHolder, light, windowPosition);
					} else if (i == 3) {
						renderOnce(window_1_exterior, graphicsHolder, light, windowPosition);
						renderOnceFlipped(window_3_exterior, graphicsHolder, light, windowPosition);
					} else {
						renderMirror(isEnd1 || isEnd2 || i == 1 || i == 4 ? window_2_exterior : window_1_exterior, graphicsHolder, light, windowPosition);
					}
					renderOnce(i % 2 == 0 ? roof_window_exterior_2 : roof_window_exterior_1, graphicsHolder, light, windowPosition);
					renderOnceFlipped(i % 2 == 0 ? roof_window_exterior_1 : roof_window_exterior_2, graphicsHolder, light, windowPosition);
					break;
			}
		}

		if (renderStage == RenderStage.EXTERIOR) {
			renderMirror(vent, graphicsHolder, light, position);
		}
	}

	@Override
	protected void renderDoorPositions(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
		switch (renderStage) {
			case LIGHT:
				renderMirror(light_door, graphicsHolder, light, position);
				break;
			case INTERIOR:
				renderMirror(door_interior, graphicsHolder, light, position);
				renderMirror(roof_door_interior, graphicsHolder, light, position);
				renderOnce(door_left_interior, graphicsHolder, light, -doorLeftX, position - doorLeftZ);
				renderOnce(door_right_interior, graphicsHolder, light, -doorLeftX, position + doorLeftZ);
				renderOnceFlipped(door_left_interior, graphicsHolder, light, -doorRightX, position + doorRightZ);
				renderOnceFlipped(door_right_interior, graphicsHolder, light, -doorRightX, position - doorRightZ);
				break;
			case EXTERIOR:
				renderMirror(door_exterior, graphicsHolder, light, position);
				renderMirror(roof_door_exterior, graphicsHolder, light, position);
				renderOnce(door_left_exterior, graphicsHolder, light, -doorLeftX, position - doorLeftZ);
				renderOnce(door_right_exterior, graphicsHolder, light, -doorLeftX, position + doorLeftZ);
				renderOnceFlipped(door_left_exterior, graphicsHolder, light, -doorRightX, position + doorRightZ);
				renderOnceFlipped(door_right_exterior, graphicsHolder, light, -doorRightX, position - doorRightZ);
				break;
		}
	}

	@Override
	protected void renderHeadPosition1(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean useHeadlights) {
		switch (renderStage) {
			case ALWAYS_ON_LIGHT:
				renderOnce(useHeadlights ? headlights : tail_lights, graphicsHolder, light, position);
				break;
			case EXTERIOR:
				renderOnce(head_exterior, graphicsHolder, light, position);
				break;
		}
	}

	@Override
	protected void renderHeadPosition2(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean useHeadlights) {
		switch (renderStage) {
			case ALWAYS_ON_LIGHT:
				renderOnceFlipped(useHeadlights ? headlights : tail_lights, graphicsHolder, light, position);
				break;
			case EXTERIOR:
				renderOnceFlipped(head_exterior, graphicsHolder, light, position);
				break;
		}
	}

	@Override
	protected void renderEndPosition1(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
		switch (renderStage) {
			case INTERIOR:
				renderOnceFlipped(end_interior, graphicsHolder, light, position);
				break;
			case EXTERIOR:
				renderOnceFlipped(end_exterior, graphicsHolder, light, position);
				break;
		}
	}

	@Override
	protected void renderEndPosition2(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
		switch (renderStage) {
			case INTERIOR:
				renderOnce(end_interior, graphicsHolder, light, position);
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
		return new int[]{0};
	}

	@Override
	protected int[] getDoorPositions() {
		return new int[]{-80, 0, 80};
	}

	@Override
	protected int[] getEndPositions() {
		return new int[]{-120, 120};
	}

	@Override
	protected int getDoorMax() {
		return DOOR_MAX;
	}

	@Override
	protected void renderTextDisplays(StoredMatrixTransformations storedMatrixTransformations, int thisRouteColor, String thisRouteName, String thisRouteNumber, String thisStationName, String thisRouteDestination, String nextStationName, Consumer<BiConsumer<String, InterchangeColorsForStationName>> getInterchanges, int car, int totalCars, boolean atPlatform, boolean isTerminating, ObjectArrayList<ScrollingText> scrollingTexts) {
		final boolean noRoute = thisRouteNumber.isEmpty();
		final String destinationString = getAlternatingString(getDestinationString(thisRouteDestination, TextSpacingType.NORMAL, false));
		final float center = 26.5F / 16;
		final float widthBig = 28F / 16;
		final float widthSmall = 19F / 16;
		final float routeWidthSmall = 0.2F;
		final float routeWidthBig = 0.3F;
		final float margin = 0.03F;

		renderFrontDestination(
				storedMatrixTransformations,
				0, -43F / 16, -8.75F, noRoute ? 0 : (routeWidthBig + margin) / 2, 0.66F, -0.01F - 3F / 16,
				-35, 0, widthBig - (noRoute ? margin * 2 : margin * 3 + routeWidthBig), 0.16F,
				ARGB_WHITE, ARGB_WHITE, 1, destinationString, false, car, totalCars
		);
		renderFrontDestination(
				storedMatrixTransformations,
				0, -43F / 16, -8.75F, -widthBig / 2 + margin + routeWidthBig / 2, 0.66F, -0.01F - 3F / 16,
				-35, 0, routeWidthBig, 0.16F,
				ARGB_WHITE, ARGB_WHITE, 1, thisRouteNumber, false, car, totalCars
		);

		renderFrontDestination(
				storedMatrixTransformations,
				-1.31F, -1.68F, center + (noRoute ? 0 : (-routeWidthBig - margin) / 2), 0, 0, -0.01F,
				0, 90, widthSmall - (noRoute ? margin * 2 : margin * 3 + routeWidthSmall), 0.1F,
				ARGB_WHITE, ARGB_WHITE, 1, destinationString, false, 0, 1
		);
		renderFrontDestination(
				storedMatrixTransformations,
				-1.31F, -1.68F, center + widthSmall / 2 - margin - routeWidthSmall / 2, 0, 0, -0.01F,
				0, 90, routeWidthSmall, 0.1F,
				ARGB_WHITE, ARGB_WHITE, 1, thisRouteNumber, false, 0, 1
		);
	}

	@Override
	protected String defaultDestinationString() {
		return "Nicht Einsteigen";
	}
}