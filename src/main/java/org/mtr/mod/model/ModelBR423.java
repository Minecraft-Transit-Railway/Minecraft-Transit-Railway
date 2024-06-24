package mtr.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mtr.client.DoorAnimationType;
import mtr.client.ScrollingText;
import mtr.data.Route;
import mtr.data.Station;
import mtr.mappings.ModelDataWrapper;
import mtr.mappings.ModelMapper;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;

import java.util.List;

public class ModelBR423 extends ModelSimpleTrainBase<ModelBR423> {

	private final ModelMapper window_1_interior;
	private final ModelMapper curve_top_r1;
	private final ModelMapper curve_middle_r1;
	private final ModelMapper curve_bottom_r1;
	private final ModelMapper window_2_interior;
	private final ModelMapper curve_top_r2;
	private final ModelMapper curve_middle_r2;
	private final ModelMapper curve_bottom_r2;
	private final ModelMapper window_3_interior;
	private final ModelMapper curve_top_r3;
	private final ModelMapper curve_middle_r3;
	private final ModelMapper curve_bottom_r3;
	private final ModelMapper window_1_exterior;
	private final ModelMapper window_2_exterior;
	private final ModelMapper window_3_exterior;
	private final ModelMapper door_interior;
	private final ModelMapper curve_top_r4;
	private final ModelMapper curve_middle_r4;
	private final ModelMapper right_curve_2_r1;
	private final ModelMapper right_curve_1_r1;
	private final ModelMapper door_left_interior;
	private final ModelMapper door_right_interior;
	private final ModelMapper door_exterior;
	private final ModelMapper door_left_exterior;
	private final ModelMapper door_right_exterior;
	private final ModelMapper end_interior;
	private final ModelMapper end_exterior;
	private final ModelMapper end_exterior_left;
	private final ModelMapper roof_2_r1;
	private final ModelMapper roof_1_r1;
	private final ModelMapper end_exterior_right;
	private final ModelMapper roof_3_r1;
	private final ModelMapper roof_2_r2;
	private final ModelMapper roof_window_interior;
	private final ModelMapper roof_2_r3;
	private final ModelMapper roof_door_interior;
	private final ModelMapper roof_2_r4;
	private final ModelMapper roof_window_exterior_1;
	private final ModelMapper roof_2_r5;
	private final ModelMapper roof_1_r2;
	private final ModelMapper roof_window_exterior_2;
	private final ModelMapper roof_2_r6;
	private final ModelMapper roof_1_r3;
	private final ModelMapper roof_door_exterior;
	private final ModelMapper roof_2_r7;
	private final ModelMapper roof_1_r4;
	private final ModelMapper vent;
	private final ModelMapper light_window;
	private final ModelMapper light_door;
	private final ModelMapper light_head;
	private final ModelMapper roof_right_r1;
	private final ModelMapper roof_left_r1;
	private final ModelMapper head_exterior;
	private final ModelMapper front_5_r1;
	private final ModelMapper front_4_r1;
	private final ModelMapper front_3_r1;
	private final ModelMapper front_1_r1;
	private final ModelMapper head_exterior_left;
	private final ModelMapper side_roof_2_r1;
	private final ModelMapper side_roof_1_r1;
	private final ModelMapper side_4_r1;
	private final ModelMapper side_3_r1;
	private final ModelMapper side_2_r1;
	private final ModelMapper side_1_r1;
	private final ModelMapper roof_2_r8;
	private final ModelMapper roof_1_r5;
	private final ModelMapper head_exterior_right;
	private final ModelMapper side_roof_2_r2;
	private final ModelMapper side_roof_1_r2;
	private final ModelMapper side_4_r2;
	private final ModelMapper side_3_r2;
	private final ModelMapper side_2_r2;
	private final ModelMapper side_1_r2;
	private final ModelMapper roof_2_r9;
	private final ModelMapper roof_1_r6;
	private final ModelMapper head_interior;
	private final ModelMapper head_interior_left;
	private final ModelMapper roof_2_r10;
	private final ModelMapper head_interior_right;
	private final ModelMapper roof_2_r11;
	private final ModelMapper seat;
	private final ModelMapper seat_2_r1;
	private final ModelMapper headlights;
	private final ModelMapper side_2_r3;
	private final ModelMapper side_1_r3;
	private final ModelMapper front_5_r2;
	private final ModelMapper tail_lights;
	private final ModelMapper side_2_r4;
	private final ModelMapper side_1_r4;

	public ModelBR423() {
		this(DoorAnimationType.PLUG_FAST, true);
	}

	private ModelBR423(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		super(doorAnimationType, renderDoorOverlay);
		final int textureWidth = 288;
		final int textureHeight = 288;

		final ModelDataWrapper modelDataWrapper = new ModelDataWrapper(this, textureWidth, textureHeight);

		window_1_interior = new ModelMapper(modelDataWrapper);
		window_1_interior.setPos(0, 24, 0);
		window_1_interior.texOffs(105, 36).addBox(0, 0, -13.5F, 20, 1, 27, 0, false);
		window_1_interior.texOffs(138, 150).addBox(20, -33, -13.5F, 0, 33, 27, 0, false);

		curve_top_r1 = new ModelMapper(modelDataWrapper);
		curve_top_r1.setPos(13, -37, 0);
		window_1_interior.addChild(curve_top_r1);
		setRotationAngle(curve_top_r1, 0, 0, 0.0873F);
		curve_top_r1.texOffs(0, 27).addBox(-3, 0, -13.5F, 6, 0, 27, 0, false);

		curve_middle_r1 = new ModelMapper(modelDataWrapper);
		curve_middle_r1.setPos(19, -35, 0);
		window_1_interior.addChild(curve_middle_r1);
		setRotationAngle(curve_middle_r1, 0, 0, -1.0472F);
		curve_middle_r1.texOffs(158, 73).addBox(0, -4, -13.5F, 0, 4, 27, 0, false);

		curve_bottom_r1 = new ModelMapper(modelDataWrapper);
		curve_bottom_r1.setPos(20, -33, 0);
		window_1_interior.addChild(curve_bottom_r1);
		setRotationAngle(curve_bottom_r1, 0, 0, -0.5236F);
		curve_bottom_r1.texOffs(168, 81).addBox(0, -3, -13.5F, 0, 3, 27, 0, false);

		window_2_interior = new ModelMapper(modelDataWrapper);
		window_2_interior.setPos(0, 24, 0);
		window_2_interior.texOffs(91, 77).addBox(0, 0, -13.5F, 20, 1, 27, 0, false);
		window_2_interior.texOffs(138, 117).addBox(20, -33, -13.5F, 0, 33, 27, 0, false);

		curve_top_r2 = new ModelMapper(modelDataWrapper);
		curve_top_r2.setPos(13, -37, 0);
		window_2_interior.addChild(curve_top_r2);
		setRotationAngle(curve_top_r2, 0, 0, 0.0873F);
		curve_top_r2.texOffs(0, 0).addBox(-3, 0, -13.5F, 6, 0, 27, 0, false);

		curve_middle_r2 = new ModelMapper(modelDataWrapper);
		curve_middle_r2.setPos(19, -35, 0);
		window_2_interior.addChild(curve_middle_r2);
		setRotationAngle(curve_middle_r2, 0, 0, -1.0472F);
		curve_middle_r2.texOffs(158, 69).addBox(0, -4, -13.5F, 0, 4, 27, 0, false);

		curve_bottom_r2 = new ModelMapper(modelDataWrapper);
		curve_bottom_r2.setPos(20, -33, 0);
		window_2_interior.addChild(curve_bottom_r2);
		setRotationAngle(curve_bottom_r2, 0, 0, -0.5236F);
		curve_bottom_r2.texOffs(168, 78).addBox(0, -3, -13.5F, 0, 3, 27, 0, false);

		window_3_interior = new ModelMapper(modelDataWrapper);
		window_3_interior.setPos(0, 24, 0);
		window_3_interior.texOffs(194, 260).addBox(0, 0, -13.5F, 20, 1, 27, 0, false);
		window_3_interior.texOffs(166, 213).addBox(20, -33, -13.5F, 0, 33, 27, 0, false);

		curve_top_r3 = new ModelMapper(modelDataWrapper);
		curve_top_r3.setPos(13, -37, 0);
		window_3_interior.addChild(curve_top_r3);
		setRotationAngle(curve_top_r3, 0, 0, 0.0873F);
		curve_top_r3.texOffs(243, 0).addBox(-3, 0, -13.5F, 6, 0, 27, 0, false);

		curve_middle_r3 = new ModelMapper(modelDataWrapper);
		curve_middle_r3.setPos(19, -35, 0);
		window_3_interior.addChild(curve_middle_r3);
		setRotationAngle(curve_middle_r3, 0, 0, -1.0472F);
		curve_middle_r3.texOffs(208, 104).addBox(0, -4, -13.5F, 0, 4, 27, 0, false);

		curve_bottom_r3 = new ModelMapper(modelDataWrapper);
		curve_bottom_r3.setPos(20, -33, 0);
		window_3_interior.addChild(curve_bottom_r3);
		setRotationAngle(curve_bottom_r3, 0, 0, -0.5236F);
		curve_bottom_r3.texOffs(208, 101).addBox(0, -3, -13.5F, 0, 3, 27, 0, false);

		window_1_exterior = new ModelMapper(modelDataWrapper);
		window_1_exterior.setPos(0, 24, 0);
		window_1_exterior.texOffs(192, 173).addBox(20, 0, -13.5F, 1, 4, 27, 0, false);
		window_1_exterior.texOffs(0, 120).addBox(21, -36, -13.5F, 0, 36, 27, 0, false);

		window_2_exterior = new ModelMapper(modelDataWrapper);
		window_2_exterior.setPos(0, 24, 0);
		window_2_exterior.texOffs(192, 37).addBox(20, 0, -13.5F, 1, 4, 27, 0, false);
		window_2_exterior.texOffs(84, 116).addBox(21, -36, -13.5F, 0, 36, 27, 0, false);

		window_3_exterior = new ModelMapper(modelDataWrapper);
		window_3_exterior.setPos(0, 24, 0);
		window_3_exterior.texOffs(224, 142).addBox(20, 0, -13.5F, 1, 4, 27, 0, false);
		window_3_exterior.texOffs(192, 177).addBox(21, -36, -13.5F, 0, 36, 27, 0, false);

		door_interior = new ModelMapper(modelDataWrapper);
		door_interior.setPos(0, 24, 0);
		door_interior.texOffs(142, 117).addBox(0, 0, -13, 20, 1, 26, 0, false);
		door_interior.texOffs(273, 27).addBox(16, -32, -13, 4, 32, 2, 0, false);
		door_interior.texOffs(206, 17).addBox(16, -32, 11, 4, 32, 2, 0, false);
		door_interior.texOffs(40, 231).addBox(6, -38, -12.9F, 10, 38, 0, 0, false);
		door_interior.texOffs(40, 231).addBox(6, -38, 12.9F, 10, 38, 0, 0, false);
		door_interior.texOffs(220, 229).addBox(16, -37, -13, 4, 5, 26, 0, false);

		curve_top_r4 = new ModelMapper(modelDataWrapper);
		curve_top_r4.setPos(12, -34, 0);
		door_interior.addChild(curve_top_r4);
		setRotationAngle(curve_top_r4, 0, 0, -0.5236F);
		curve_top_r4.texOffs(8, 201).addBox(0, -4, -13, 1, 4, 26, 0, false);

		curve_middle_r4 = new ModelMapper(modelDataWrapper);
		curve_middle_r4.setPos(17, -32, 0);
		door_interior.addChild(curve_middle_r4);
		setRotationAngle(curve_middle_r4, 0, 0, 0.3927F);
		curve_middle_r4.texOffs(82, 36).addBox(-6, 0, -13, 6, 0, 26, 0, false);

		right_curve_2_r1 = new ModelMapper(modelDataWrapper);
		right_curve_2_r1.setPos(12, -34, 0);
		door_interior.addChild(right_curve_2_r1);
		setRotationAngle(right_curve_2_r1, 0, 0, -0.7854F);
		right_curve_2_r1.texOffs(0, 0).addBox(0, 0, 11, 2, 4, 2, 0, false);
		right_curve_2_r1.texOffs(0, 6).addBox(0, 0, -13, 2, 4, 2, 0, false);

		right_curve_1_r1 = new ModelMapper(modelDataWrapper);
		right_curve_1_r1.setPos(16, -29, 0);
		door_interior.addChild(right_curve_1_r1);
		setRotationAngle(right_curve_1_r1, 0, 0, -0.5236F);
		right_curve_1_r1.texOffs(105, 90).addBox(0, -3, 11, 3, 3, 2, 0, false);
		right_curve_1_r1.texOffs(212, 96).addBox(0, -3, -13, 3, 3, 2, 0, false);

		door_left_interior = new ModelMapper(modelDataWrapper);
		door_left_interior.setPos(0, 24, 0);
		door_left_interior.texOffs(248, 182).addBox(20, -35, -12, 1, 35, 12, 0, false);

		door_right_interior = new ModelMapper(modelDataWrapper);
		door_right_interior.setPos(0, 24, 0);
		door_right_interior.texOffs(0, 0).addBox(20, -35, 0, 1, 35, 12, 0, false);

		door_exterior = new ModelMapper(modelDataWrapper);
		door_exterior.setPos(0, 24, 0);
		door_exterior.texOffs(208, 90).addBox(20, 0, -13, 1, 4, 26, 0, false);
		door_exterior.texOffs(262, 0).addBox(20, -35, -13, 1, 35, 1, 0, false);
		door_exterior.texOffs(266, 0).addBox(20, -35, 12, 1, 35, 1, 0, false);
		door_exterior.texOffs(60, 228).addBox(20, -36, -13, 1, 1, 26, 0, false);

		door_left_exterior = new ModelMapper(modelDataWrapper);
		door_left_exterior.setPos(0, 24, 0);
		door_left_exterior.texOffs(62, 207).addBox(21, -35, -12, 0, 35, 12, 0, false);

		door_right_exterior = new ModelMapper(modelDataWrapper);
		door_right_exterior.setPos(0, 24, 0);
		door_right_exterior.texOffs(88, 203).addBox(21, -35, 0, 0, 35, 12, 0, false);

		end_interior = new ModelMapper(modelDataWrapper);
		end_interior.setPos(0, 24, 0);
		end_interior.texOffs(88, 33).addBox(-20, 0, 0, 40, 1, 2, 0, false);
		end_interior.texOffs(20, 231).addBox(12, -35, 0, 8, 35, 2, 0, false);
		end_interior.texOffs(0, 231).addBox(-20, -35, 0, 8, 35, 2, 0, true);
		end_interior.texOffs(165, 144).addBox(12, -31, -27, 8, 0, 27, 0, false);
		end_interior.texOffs(181, 144).addBox(-20, -31, -27, 8, 0, 27, 0, true);
		end_interior.texOffs(156, 0).addBox(-20, -39, 0, 40, 4, 2, 0, false);

		end_exterior = new ModelMapper(modelDataWrapper);
		end_exterior.setPos(0, 24, 0);
		end_exterior.texOffs(156, 7).addBox(-21, -43, 2, 42, 8, 0, 0, false);
		end_exterior.texOffs(154, 15).addBox(-17, -43, 0, 34, 0, 2, 0, false);

		end_exterior_left = new ModelMapper(modelDataWrapper);
		end_exterior_left.setPos(0, 0, 0);
		end_exterior.addChild(end_exterior_left);
		end_exterior_left.texOffs(164, 17).addBox(20, 0, 0, 1, 4, 2, 0, false);
		end_exterior_left.texOffs(266, 104).addBox(21, -36, 0, 0, 36, 2, 0, false);
		end_exterior_left.texOffs(268, 61).addBox(12, -35, 2, 9, 35, 0, 0, false);

		roof_2_r1 = new ModelMapper(modelDataWrapper);
		roof_2_r1.setPos(17, -43, 0);
		end_exterior_left.addChild(roof_2_r1);
		setRotationAngle(roof_2_r1, 0, 0, -0.6981F);
		roof_2_r1.texOffs(78, 147).addBox(-1, 0, 0, 1, 4, 2, 0, false);

		roof_1_r1 = new ModelMapper(modelDataWrapper);
		roof_1_r1.setPos(21, -36, 0);
		end_exterior_left.addChild(roof_1_r1);
		setRotationAngle(roof_1_r1, 0, 0, -0.3491F);
		roof_1_r1.texOffs(18, 5).addBox(-1, -5, 0, 1, 5, 2, 0, false);

		end_exterior_right = new ModelMapper(modelDataWrapper);
		end_exterior_right.setPos(0, 0, 0);
		end_exterior.addChild(end_exterior_right);
		end_exterior_right.texOffs(164, 23).addBox(-21, 0, 0, 1, 4, 2, 0, true);
		end_exterior_right.texOffs(262, 104).addBox(-21, -36, 0, 0, 36, 2, 0, true);
		end_exterior_right.texOffs(0, 63).addBox(-21, -35, 2, 9, 35, 0, 0, true);

		roof_3_r1 = new ModelMapper(modelDataWrapper);
		roof_3_r1.setPos(-17, -43, 0);
		end_exterior_right.addChild(roof_3_r1);
		setRotationAngle(roof_3_r1, 0, 0, 0.6981F);
		roof_3_r1.texOffs(78, 153).addBox(0, 0, 0, 1, 4, 2, 0, true);

		roof_2_r2 = new ModelMapper(modelDataWrapper);
		roof_2_r2.setPos(-21, -36, 0);
		end_exterior_right.addChild(roof_2_r2);
		setRotationAngle(roof_2_r2, 0, 0, 0.3491F);
		roof_2_r2.texOffs(14, 0).addBox(0, -5, 0, 1, 5, 2, 0, true);

		roof_window_interior = new ModelMapper(modelDataWrapper);
		roof_window_interior.setPos(0, 24, 0);
		roof_window_interior.texOffs(12, 0).addBox(0, -38, -13.5F, 6, 0, 27, 0, false);

		roof_2_r3 = new ModelMapper(modelDataWrapper);
		roof_2_r3.setPos(6, -38, 0);
		roof_window_interior.addChild(roof_2_r3);
		setRotationAngle(roof_2_r3, 0, 0, 0.1745F);
		roof_2_r3.texOffs(12, 27).addBox(0, 0, -13.5F, 5, 0, 27, 0, false);

		roof_door_interior = new ModelMapper(modelDataWrapper);
		roof_door_interior.setPos(0, 24, 0);
		roof_door_interior.texOffs(79, 64).addBox(0, -38, -13, 6, 0, 26, 0, false);

		roof_2_r4 = new ModelMapper(modelDataWrapper);
		roof_2_r4.setPos(6, -38, 0);
		roof_door_interior.addChild(roof_2_r4);
		setRotationAngle(roof_2_r4, 0, 0, 0.1745F);
		roof_2_r4.texOffs(94, 36).addBox(0, 0, -13, 5, 0, 26, 0, false);

		roof_window_exterior_1 = new ModelMapper(modelDataWrapper);
		roof_window_exterior_1.setPos(0, 24, 0);
		roof_window_exterior_1.texOffs(27, 0).addBox(0, -43, -13.5F, 17, 0, 27, 0, false);

		roof_2_r5 = new ModelMapper(modelDataWrapper);
		roof_2_r5.setPos(17, -43, 0);
		roof_window_exterior_1.addChild(roof_2_r5);
		setRotationAngle(roof_2_r5, 0, 0, -0.6981F);
		roof_2_r5.texOffs(0, 32).addBox(0, 0, -13.5F, 0, 4, 27, 0, false);

		roof_1_r2 = new ModelMapper(modelDataWrapper);
		roof_1_r2.setPos(21, -36, 0);
		roof_window_exterior_1.addChild(roof_1_r2);
		setRotationAngle(roof_1_r2, 0, 0, -0.3491F);
		roof_1_r2.texOffs(0, 27).addBox(0, -5, -13.5F, 0, 5, 27, 0, false);

		roof_window_exterior_2 = new ModelMapper(modelDataWrapper);
		roof_window_exterior_2.setPos(0, 24, 0);
		roof_window_exterior_2.texOffs(27, 27).addBox(0, -43, -13.5F, 17, 0, 27, 0, false);

		roof_2_r6 = new ModelMapper(modelDataWrapper);
		roof_2_r6.setPos(17, -43, 0);
		roof_window_exterior_2.addChild(roof_2_r6);
		setRotationAngle(roof_2_r6, 0, 0, -0.6981F);
		roof_2_r6.texOffs(54, 32).addBox(0, 0, -13.5F, 0, 4, 27, 0, false);

		roof_1_r3 = new ModelMapper(modelDataWrapper);
		roof_1_r3.setPos(21, -36, 0);
		roof_window_exterior_2.addChild(roof_1_r3);
		setRotationAngle(roof_1_r3, 0, 0, -0.3491F);
		roof_1_r3.texOffs(54, 27).addBox(0, -5, -13.5F, 0, 5, 27, 0, false);

		roof_door_exterior = new ModelMapper(modelDataWrapper);
		roof_door_exterior.setPos(0, 24, 0);
		roof_door_exterior.texOffs(132, 64).addBox(0, -43, -13, 17, 0, 26, 0, false);

		roof_2_r7 = new ModelMapper(modelDataWrapper);
		roof_2_r7.setPos(17, -43, 0);
		roof_door_exterior.addChild(roof_2_r7);
		setRotationAngle(roof_2_r7, 0, 0, -0.6981F);
		roof_2_r7.texOffs(36, 189).addBox(0, 0, -13, 0, 4, 26, 0, false);

		roof_1_r4 = new ModelMapper(modelDataWrapper);
		roof_1_r4.setPos(21, -36, 0);
		roof_door_exterior.addChild(roof_1_r4);
		setRotationAngle(roof_1_r4, 0, 0, -0.3491F);
		roof_1_r4.texOffs(168, 85).addBox(0, -5, -13, 0, 5, 26, 0, false);

		vent = new ModelMapper(modelDataWrapper);
		vent.setPos(0, 24, 0);
		vent.texOffs(74, 237).addBox(-13, -49, -40, 26, 6, 40, 0, false);

		light_window = new ModelMapper(modelDataWrapper);
		light_window.setPos(0, 24, 0);
		light_window.texOffs(217, 0).addBox(2, -38.2F, -13.5F, 4, 0, 27, 0, false);

		light_door = new ModelMapper(modelDataWrapper);
		light_door.setPos(0, 24, 0);
		light_door.texOffs(218, 0).addBox(2, -38.2F, -13, 4, 0, 26, 0, false);

		light_head = new ModelMapper(modelDataWrapper);
		light_head.setPos(0, 24, 0);
		light_head.texOffs(127, 210).addBox(-6, -38.2F, -13.5F, 12, 0, 27, 0, false);

		roof_right_r1 = new ModelMapper(modelDataWrapper);
		roof_right_r1.setPos(-6, -38.2F, 5);
		light_head.addChild(roof_right_r1);
		setRotationAngle(roof_right_r1, 0, 0, -0.1745F);
		roof_right_r1.texOffs(151, 210).addBox(-5, 0, -18.5F, 5, 0, 27, 0, true);

		roof_left_r1 = new ModelMapper(modelDataWrapper);
		roof_left_r1.setPos(6, -38.2F, 5);
		light_head.addChild(roof_left_r1);
		setRotationAngle(roof_left_r1, 0, 0, 0.1745F);
		roof_left_r1.texOffs(117, 210).addBox(0, 0, -18.5F, 5, 0, 27, 0, false);

		head_exterior = new ModelMapper(modelDataWrapper);
		head_exterior.setPos(0, 24, 0);
		head_exterior.texOffs(-22, 63).addBox(-21, 0, -40, 42, 0, 40, 0, false);
		head_exterior.texOffs(221, 173).addBox(-12, -11, -40, 24, 9, 0, 0, false);
		head_exterior.texOffs(68, 0).addBox(-17, -43, -20, 34, 0, 20, 0, false);
		head_exterior.texOffs(0, 104).addBox(-21, -43, -1, 42, 43, 0, 0, false);

		front_5_r1 = new ModelMapper(modelDataWrapper);
		front_5_r1.setPos(0, -43, -20);
		head_exterior.addChild(front_5_r1);
		setRotationAngle(front_5_r1, 0.5236F, 0, 0);
		front_5_r1.texOffs(80, 20).addBox(-18, 0, -8, 36, 0, 8, 0, false);

		front_4_r1 = new ModelMapper(modelDataWrapper);
		front_4_r1.setPos(0, -43, -20);
		head_exterior.addChild(front_4_r1);
		setRotationAngle(front_4_r1, 0.9599F, 0, 0);
		front_4_r1.texOffs(-19, 269).addBox(-17, -3, -25, 34, 0, 19, 0, false);

		front_3_r1 = new ModelMapper(modelDataWrapper);
		front_3_r1.setPos(0, -11, -40);
		head_exterior.addChild(front_3_r1);
		setRotationAngle(front_3_r1, -0.2618F, 0, 0);
		front_3_r1.texOffs(192, 68).addBox(-14, -16, 0, 28, 16, 0, 0, false);

		front_1_r1 = new ModelMapper(modelDataWrapper);
		front_1_r1.setPos(0, -2, -40);
		head_exterior.addChild(front_1_r1);
		setRotationAngle(front_1_r1, 0.0873F, 0, 0);
		front_1_r1.texOffs(221, 55).addBox(-13, 0, 0, 26, 6, 0, 0, false);

		head_exterior_left = new ModelMapper(modelDataWrapper);
		head_exterior_left.setPos(0, 0, 0);
		head_exterior.addChild(head_exterior_left);
		head_exterior_left.texOffs(221, 31).addBox(20, 0, -20, 1, 4, 20, 0, false);
		head_exterior_left.texOffs(94, 159).addBox(21, -36, -20, 0, 36, 20, 0, false);

		side_roof_2_r1 = new ModelMapper(modelDataWrapper);
		side_roof_2_r1.setPos(17, -43, -20);
		head_exterior_left.addChild(side_roof_2_r1);
		setRotationAngle(side_roof_2_r1, 0, 0.2618F, -0.6981F);
		side_roof_2_r1.texOffs(0, 178).addBox(0, 0, -5, 0, 4, 5, 0, false);

		side_roof_1_r1 = new ModelMapper(modelDataWrapper);
		side_roof_1_r1.setPos(21, -36, -20);
		head_exterior_left.addChild(side_roof_1_r1);
		setRotationAngle(side_roof_1_r1, 0, 0.2618F, -0.3491F);
		side_roof_1_r1.texOffs(0, 41).addBox(0, -5, -6, 0, 7, 6, 0, false);

		side_4_r1 = new ModelMapper(modelDataWrapper);
		side_4_r1.setPos(17, -43, -20);
		head_exterior_left.addChild(side_4_r1);
		setRotationAngle(side_4_r1, 1.1781F, -0.6981F, 0);
		side_4_r1.texOffs(204, 182).addBox(-13, -2.5F, -20, 12, 0, 17, 0, false);

		side_3_r1 = new ModelMapper(modelDataWrapper);
		side_3_r1.setPos(12, -11, -40);
		head_exterior_left.addChild(side_3_r1);
		setRotationAngle(side_3_r1, -0.1309F, -0.7854F, 0);
		side_3_r1.texOffs(224, 144).addBox(0, -18, 0, 13, 18, 0, 0, false);

		side_2_r1 = new ModelMapper(modelDataWrapper);
		side_2_r1.setPos(12, 0, -40);
		head_exterior_left.addChild(side_2_r1);
		setRotationAngle(side_2_r1, 0, -0.7854F, 0);
		side_2_r1.texOffs(224, 15).addBox(0, -11, 0, 10, 15, 0, 0, false);

		side_1_r1 = new ModelMapper(modelDataWrapper);
		side_1_r1.setPos(21, 0, -20);
		head_exterior_left.addChild(side_1_r1);
		setRotationAngle(side_1_r1, 0, 0.1745F, 0);
		side_1_r1.texOffs(172, 7).addBox(0, -36, -14, 0, 40, 14, 0, false);

		roof_2_r8 = new ModelMapper(modelDataWrapper);
		roof_2_r8.setPos(17, -43, 0);
		head_exterior_left.addChild(roof_2_r8);
		setRotationAngle(roof_2_r8, 0, 0, -0.6981F);
		roof_2_r8.texOffs(208, 104).addBox(0, 0, -20, 0, 4, 20, 0, false);

		roof_1_r5 = new ModelMapper(modelDataWrapper);
		roof_1_r5.setPos(21, -36, 0);
		head_exterior_left.addChild(roof_1_r5);
		setRotationAngle(roof_1_r5, 0, 0, -0.3491F);
		roof_1_r5.texOffs(117, 44).addBox(0, -5, -20, 0, 5, 20, 0, false);

		head_exterior_right = new ModelMapper(modelDataWrapper);
		head_exterior_right.setPos(0, 0, 0);
		head_exterior.addChild(head_exterior_right);
		head_exterior_right.texOffs(242, 82).addBox(-21, 0, -20, 1, 4, 20, 0, true);
		head_exterior_right.texOffs(54, 159).addBox(-21, -36, -20, 0, 36, 20, 0, true);

		side_roof_2_r2 = new ModelMapper(modelDataWrapper);
		side_roof_2_r2.setPos(-17, -43, -20);
		head_exterior_right.addChild(side_roof_2_r2);
		setRotationAngle(side_roof_2_r2, 0, -0.2618F, 0.6981F);
		side_roof_2_r2.texOffs(10, 178).addBox(0, 0, -5, 0, 4, 5, 0, true);

		side_roof_1_r2 = new ModelMapper(modelDataWrapper);
		side_roof_1_r2.setPos(-21, -36, -20);
		head_exterior_right.addChild(side_roof_1_r2);
		setRotationAngle(side_roof_1_r2, 0, -0.2618F, 0.3491F);
		side_roof_1_r2.texOffs(12, 41).addBox(0, -5, -6, 0, 7, 6, 0, true);

		side_4_r2 = new ModelMapper(modelDataWrapper);
		side_4_r2.setPos(-17, -43, -20);
		head_exterior_right.addChild(side_4_r2);
		setRotationAngle(side_4_r2, 1.1781F, 0.6981F, 0);
		side_4_r2.texOffs(37, 147).addBox(1, -2.5F, -20, 12, 0, 17, 0, true);

		side_3_r2 = new ModelMapper(modelDataWrapper);
		side_3_r2.setPos(-12, -11, -40);
		head_exterior_right.addChild(side_3_r2);
		setRotationAngle(side_3_r2, -0.1309F, 0.7854F, 0);
		side_3_r2.texOffs(236, 84).addBox(-13, -18, 0, 13, 18, 0, 0, true);

		side_2_r2 = new ModelMapper(modelDataWrapper);
		side_2_r2.setPos(-12, 0, -40);
		head_exterior_right.addChild(side_2_r2);
		setRotationAngle(side_2_r2, 0, 0.7854F, 0);
		side_2_r2.texOffs(248, 61).addBox(-10, -11, 0, 10, 15, 0, 0, true);

		side_1_r2 = new ModelMapper(modelDataWrapper);
		side_1_r2.setPos(-21, 0, -20);
		head_exterior_right.addChild(side_1_r2);
		setRotationAngle(side_1_r2, 0, -0.1745F, 0);
		side_1_r2.texOffs(0, 173).addBox(0, -36, -14, 0, 40, 14, 0, true);

		roof_2_r9 = new ModelMapper(modelDataWrapper);
		roof_2_r9.setPos(-17, -43, 0);
		head_exterior_right.addChild(roof_2_r9);
		setRotationAngle(roof_2_r9, 0, 0, 0.6981F);
		roof_2_r9.texOffs(208, 100).addBox(0, 0, -20, 0, 4, 20, 0, true);

		roof_1_r6 = new ModelMapper(modelDataWrapper);
		roof_1_r6.setPos(-21, -36, 0);
		head_exterior_right.addChild(roof_1_r6);
		setRotationAngle(roof_1_r6, 0, 0, 0.3491F);
		roof_1_r6.texOffs(117, 49).addBox(0, -5, -20, 0, 5, 20, 0, true);

		head_interior = new ModelMapper(modelDataWrapper);
		head_interior.setPos(0, 24, 0);
		head_interior.texOffs(84, 105).addBox(-21, -38, -13.5F, 42, 38, 0, 0, false);
		head_interior.texOffs(165, 173).addBox(-6, -38, -13.5F, 12, 0, 27, 0, false);

		head_interior_left = new ModelMapper(modelDataWrapper);
		head_interior_left.setPos(0, 0, 0);
		head_interior.addChild(head_interior_left);


		roof_2_r10 = new ModelMapper(modelDataWrapper);
		roof_2_r10.setPos(6, -38, 5);
		head_interior_left.addChild(roof_2_r10);
		setRotationAngle(roof_2_r10, 0, 0, 0.1745F);
		roof_2_r10.texOffs(9, 183).addBox(0, 0, -18.5F, 5, 0, 27, 0, false);

		head_interior_right = new ModelMapper(modelDataWrapper);
		head_interior_right.setPos(0, 0, 0);
		head_interior.addChild(head_interior_right);


		roof_2_r11 = new ModelMapper(modelDataWrapper);
		roof_2_r11.setPos(-6, -38, 5);
		head_interior_right.addChild(roof_2_r11);
		setRotationAngle(roof_2_r11, 0, 0, -0.1745F);
		roof_2_r11.texOffs(107, 210).addBox(-5, 0, -18.5F, 5, 0, 27, 0, true);

		seat = new ModelMapper(modelDataWrapper);
		seat.setPos(0, 24, 0);
		seat.texOffs(54, 166).addBox(-4, -6, -4, 8, 1, 7, 0, false);
		seat.texOffs(199, 61).addBox(-3.5F, -19.644F, 4.0686F, 7, 2, 1, 0, false);

		seat_2_r1 = new ModelMapper(modelDataWrapper);
		seat_2_r1.setPos(0, -6, 2);
		seat.addChild(seat_2_r1);
		setRotationAngle(seat_2_r1, -0.1745F, 0, 0);
		seat_2_r1.texOffs(88, 36).addBox(-4, -12, 0, 8, 12, 1, 0, false);

		headlights = new ModelMapper(modelDataWrapper);
		headlights.setPos(0, 24, 0);


		side_2_r3 = new ModelMapper(modelDataWrapper);
		side_2_r3.setPos(-12, 0, -40);
		headlights.addChild(side_2_r3);
		setRotationAngle(side_2_r3, 0, 0.7854F, 0);
		side_2_r3.texOffs(221, 34).addBox(-8.1F, -11, -0.1F, 7, 4, 0, 0, true);

		side_1_r3 = new ModelMapper(modelDataWrapper);
		side_1_r3.setPos(12, 0, -40);
		headlights.addChild(side_1_r3);
		setRotationAngle(side_1_r3, 0, -0.7854F, 0);
		side_1_r3.texOffs(221, 30).addBox(1.1F, -11, -0.1F, 7, 4, 0, 0, false);

		front_5_r2 = new ModelMapper(modelDataWrapper);
		front_5_r2.setPos(0, -43, -20);
		headlights.addChild(front_5_r2);
		setRotationAngle(front_5_r2, 0.5236F, 0, 0);
		front_5_r2.texOffs(216, 43).addBox(-2, -0.2F, -7, 4, 0, 5, 0, false);

		tail_lights = new ModelMapper(modelDataWrapper);
		tail_lights.setPos(0, 24, 0);


		side_2_r4 = new ModelMapper(modelDataWrapper);
		side_2_r4.setPos(-12, 0, -40);
		tail_lights.addChild(side_2_r4);
		setRotationAngle(side_2_r4, 0, 0.7854F, 0);
		side_2_r4.texOffs(243, 34).addBox(-8.1F, -11, -0.1F, 7, 4, 0, 0, true);

		side_1_r4 = new ModelMapper(modelDataWrapper);
		side_1_r4.setPos(12, 0, -40);
		tail_lights.addChild(side_1_r4);
		setRotationAngle(side_1_r4, 0, -0.7854F, 0);
		side_1_r4.texOffs(243, 30).addBox(1.1F, -11, -0.1F, 7, 4, 0, 0, false);

		modelDataWrapper.setModelPart(textureWidth, textureHeight);
		window_1_interior.setModelPart();
		window_2_interior.setModelPart();
		window_3_interior.setModelPart();
		window_1_exterior.setModelPart();
		window_2_exterior.setModelPart();
		window_3_exterior.setModelPart();
		door_interior.setModelPart();
		door_left_interior.setModelPart();
		door_right_interior.setModelPart();
		door_exterior.setModelPart();
		door_left_exterior.setModelPart();
		door_right_exterior.setModelPart();
		end_interior.setModelPart();
		end_exterior.setModelPart();
		roof_window_interior.setModelPart();
		roof_door_interior.setModelPart();
		roof_window_exterior_1.setModelPart();
		roof_window_exterior_2.setModelPart();
		roof_door_exterior.setModelPart();
		vent.setModelPart();
		light_window.setModelPart();
		light_door.setModelPart();
		light_head.setModelPart();
		head_exterior.setModelPart();
		head_interior.setModelPart();
		seat.setModelPart();
		headlights.setModelPart();
		tail_lights.setModelPart();
	}

	private static final int DOOR_MAX = 12;

	@Override
	public ModelBR423 createNew(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		return new ModelBR423(doorAnimationType, renderDoorOverlay);
	}

	@Override
	protected void renderWindowPositions(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
		final float[] windowPositions = {-106.5F, -53.5F, -26.5F, 26.5F, 53.5F, 106.5F};

		for (int i = 0; i < windowPositions.length; i++) {
			final float windowPosition = windowPositions[i];
			final boolean isEnd1 = isEnd1Head && i == 0;
			final boolean isEnd2 = isEnd2Head && i == windowPositions.length - 1;

			switch (renderStage) {
				case LIGHTS:
					if (isEnd1) {
						renderOnce(light_head, matrices, vertices, light, windowPosition);
					} else if (isEnd2) {
						renderOnceFlipped(light_head, matrices, vertices, light, windowPosition);
					} else {
						renderMirror(light_window, matrices, vertices, light, windowPosition);
					}
					break;
				case INTERIOR:
					if (i == 2) {
						renderOnce(window_3_interior, matrices, vertices, light, windowPosition);
						renderOnceFlipped(window_1_interior, matrices, vertices, light, windowPosition);
					} else if (i == 3) {
						renderOnce(window_1_interior, matrices, vertices, light, windowPosition);
						renderOnceFlipped(window_3_interior, matrices, vertices, light, windowPosition);
					} else {
						renderMirror(isEnd1 || isEnd2 || i == 1 || i == 4 ? window_2_interior : window_1_interior, matrices, vertices, light, windowPosition);
					}
					if (isEnd1) {
						renderOnce(head_interior, matrices, vertices, light, windowPosition);
					} else if (isEnd2) {
						renderOnceFlipped(head_interior, matrices, vertices, light, windowPosition);
					} else {
						renderMirror(roof_window_interior, matrices, vertices, light, windowPosition);
					}
					if (renderDetails) {
						renderOnceFlipped(seat, matrices, vertices, light, -16, windowPosition - 7);
						renderOnceFlipped(seat, matrices, vertices, light, 16, windowPosition - 7);
						renderOnce(seat, matrices, vertices, light, -16, windowPosition + 7);
						renderOnce(seat, matrices, vertices, light, 16, windowPosition + 7);
						if (i != 1 && i != 3) {
							renderOnceFlipped(seat, matrices, vertices, light, -8, windowPosition - 7);
							renderOnceFlipped(seat, matrices, vertices, light, 8, windowPosition - 7);
						}
						if (i != 2 && i != 4) {
							renderOnce(seat, matrices, vertices, light, -8, windowPosition + 7);
							renderOnce(seat, matrices, vertices, light, 8, windowPosition + 7);
						}
					}
					break;
				case EXTERIOR:
					if (i == 2) {
						renderOnce(window_3_exterior, matrices, vertices, light, windowPosition);
						renderOnceFlipped(window_1_exterior, matrices, vertices, light, windowPosition);
					} else if (i == 3) {
						renderOnce(window_1_exterior, matrices, vertices, light, windowPosition);
						renderOnceFlipped(window_3_exterior, matrices, vertices, light, windowPosition);
					} else {
						renderMirror(isEnd1 || isEnd2 || i == 1 || i == 4 ? window_2_exterior : window_1_exterior, matrices, vertices, light, windowPosition);
					}
					renderOnce(i % 2 == 0 ? roof_window_exterior_2 : roof_window_exterior_1, matrices, vertices, light, windowPosition);
					renderOnceFlipped(i % 2 == 0 ? roof_window_exterior_1 : roof_window_exterior_2, matrices, vertices, light, windowPosition);
					break;
			}
		}

		if (renderStage == RenderStage.EXTERIOR) {
			renderMirror(vent, matrices, vertices, light, position);
		}
	}

	@Override
	protected void renderDoorPositions(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
		switch (renderStage) {
			case LIGHTS:
				renderMirror(light_door, matrices, vertices, light, position);
				break;
			case INTERIOR:
				renderMirror(door_interior, matrices, vertices, light, position);
				renderMirror(roof_door_interior, matrices, vertices, light, position);
				renderOnce(door_left_interior, matrices, vertices, light, -doorLeftX, position - doorLeftZ);
				renderOnce(door_right_interior, matrices, vertices, light, -doorLeftX, position + doorLeftZ);
				renderOnceFlipped(door_left_interior, matrices, vertices, light, -doorRightX, position + doorRightZ);
				renderOnceFlipped(door_right_interior, matrices, vertices, light, -doorRightX, position - doorRightZ);
				break;
			case EXTERIOR:
				renderMirror(door_exterior, matrices, vertices, light, position);
				renderMirror(roof_door_exterior, matrices, vertices, light, position);
				renderOnce(door_left_exterior, matrices, vertices, light, -doorLeftX, position - doorLeftZ);
				renderOnce(door_right_exterior, matrices, vertices, light, -doorLeftX, position + doorLeftZ);
				renderOnceFlipped(door_left_exterior, matrices, vertices, light, -doorRightX, position + doorRightZ);
				renderOnceFlipped(door_right_exterior, matrices, vertices, light, -doorRightX, position - doorRightZ);
				break;
		}
	}

	@Override
	protected void renderHeadPosition1(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean useHeadlights) {
		switch (renderStage) {
			case ALWAYS_ON_LIGHTS:
				renderOnce(useHeadlights ? headlights : tail_lights, matrices, vertices, light, position);
				break;
			case EXTERIOR:
				renderOnce(head_exterior, matrices, vertices, light, position);
				break;
		}
	}

	@Override
	protected void renderHeadPosition2(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean useHeadlights) {
		switch (renderStage) {
			case ALWAYS_ON_LIGHTS:
				renderOnceFlipped(useHeadlights ? headlights : tail_lights, matrices, vertices, light, position);
				break;
			case EXTERIOR:
				renderOnceFlipped(head_exterior, matrices, vertices, light, position);
				break;
		}
	}

	@Override
	protected void renderEndPosition1(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
		switch (renderStage) {
			case INTERIOR:
				renderOnceFlipped(end_interior, matrices, vertices, light, position);
				break;
			case EXTERIOR:
				renderOnceFlipped(end_exterior, matrices, vertices, light, position);
				break;
		}
	}

	@Override
	protected void renderEndPosition2(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
		switch (renderStage) {
			case INTERIOR:
				renderOnce(end_interior, matrices, vertices, light, position);
				break;
			case EXTERIOR:
				renderOnce(end_exterior, matrices, vertices, light, position);
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
	protected void renderTextDisplays(PoseStack matrices, MultiBufferSource vertexConsumers, Font font, MultiBufferSource.BufferSource immediate, Route thisRoute, Route nextRoute, Station thisStation, Station nextStation, Station lastStation, String customDestination, int car, int totalCars, boolean atPlatform, List<ScrollingText> scrollingTexts) {
		final String routeNumber = thisRoute == null ? "" : thisRoute.lightRailRouteNumber;
		final boolean noRoute = routeNumber.isEmpty();
		final String destinationString = getAlternatingString(getDestinationString(lastStation, customDestination, TextSpacingType.NORMAL, false));
		final float center = 26.5F / 16;
		final float widthBig = 28F / 16;
		final float widthSmall = 19F / 16;
		final float routeWidthSmall = 0.2F;
		final float routeWidthBig = 0.3F;
		final float margin = 0.03F;

		renderFrontDestination(
				matrices, font, immediate,
				0, -43F / 16, -8.75F, noRoute ? 0 : (routeWidthBig + margin) / 2, 0.66F, -0.01F - 3F / 16,
				-35, 0, widthBig - (noRoute ? margin * 2 : margin * 3 + routeWidthBig), 0.16F,
				ARGB_WHITE, ARGB_WHITE, 1, destinationString, false, car, totalCars
		);
		renderFrontDestination(
				matrices, font, immediate,
				0, -43F / 16, -8.75F, -widthBig / 2 + margin + routeWidthBig / 2, 0.66F, -0.01F - 3F / 16,
				-35, 0, routeWidthBig, 0.16F,
				ARGB_WHITE, ARGB_WHITE, 1, routeNumber, false, car, totalCars
		);

		renderFrontDestination(
				matrices, font, immediate,
				-1.31F, -1.68F, center + (noRoute ? 0 : (-routeWidthBig - margin) / 2), 0, 0, -0.01F,
				0, 90, widthSmall - (noRoute ? margin * 2 : margin * 3 + routeWidthSmall), 0.1F,
				ARGB_WHITE, ARGB_WHITE, 1, destinationString, false, 0, 1
		);
		renderFrontDestination(
				matrices, font, immediate,
				-1.31F, -1.68F, center + widthSmall / 2 - margin - routeWidthSmall / 2, 0, 0, -0.01F,
				0, 90, routeWidthSmall, 0.1F,
				ARGB_WHITE, ARGB_WHITE, 1, routeNumber, false, 0, 1
		);
	}

	@Override
	protected String defaultDestinationString() {
		return "Nicht Einsteigen";
	}
}