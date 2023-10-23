package org.mtr.mod.model;

import org.mtr.core.data.RenderStage;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.ModelPartExtension;
import org.mtr.mod.client.DoorAnimationType;

public class ModelDRL extends ModelSimpleTrainBase<ModelDRL> {
	private final ModelPartExtension window;
	private final ModelPartExtension upper_wall_r1;
	private final ModelPartExtension window_handrails_1;
	private final ModelPartExtension handrail_3_r1;
	private final ModelPartExtension handrail_2_r1;
	private final ModelPartExtension handrail_1_r1;
	private final ModelPartExtension window_handrails_2;
	private final ModelPartExtension handrail_4_r1;
	private final ModelPartExtension handrail_3_r2;
	private final ModelPartExtension handrail_2_r2;
	private final ModelPartExtension window_exterior;
	private final ModelPartExtension upper_wall_5_r1;
	private final ModelPartExtension upper_wall_3_r1;
	private final ModelPartExtension floor_4_r1;
	private final ModelPartExtension floor_3_r1;
	private final ModelPartExtension roof_window_1;
	private final ModelPartExtension inner_roof_4_r1;
	private final ModelPartExtension inner_roof_2_r1;
	private final ModelPartExtension roof_window_2;
	private final ModelPartExtension inner_roof_5_r1;
	private final ModelPartExtension inner_roof_3_r1;
	private final ModelPartExtension roof_window_3;
	private final ModelPartExtension inner_roof_5_r2;
	private final ModelPartExtension inner_roof_3_r2;
	private final ModelPartExtension roof_window_light;
	private final ModelPartExtension window_light_r1;
	private final ModelPartExtension roof_door;
	private final ModelPartExtension inner_roof_4_r2;
	private final ModelPartExtension inner_roof_2_r2;
	private final ModelPartExtension roof_exterior;
	private final ModelPartExtension outer_roof_5_r1;
	private final ModelPartExtension outer_roof_4_r1;
	private final ModelPartExtension outer_roof_3_r1;
	private final ModelPartExtension outer_roof_2_r1;
	private final ModelPartExtension outer_roof_1_r1;
	private final ModelPartExtension door;
	private final ModelPartExtension door_right;
	private final ModelPartExtension door_right_top_r1;
	private final ModelPartExtension door_left;
	private final ModelPartExtension door_left_top_r1;
	private final ModelPartExtension door_handrail_1;
	private final ModelPartExtension handrail_11_r1;
	private final ModelPartExtension door_handrail_2;
	private final ModelPartExtension door_exterior;
	private final ModelPartExtension door_left_exterior;
	private final ModelPartExtension door_left_top_r2;
	private final ModelPartExtension door_right_exterior;
	private final ModelPartExtension door_right_top_r2;
	private final ModelPartExtension end;
	private final ModelPartExtension upper_wall_2_r1;
	private final ModelPartExtension upper_wall_1_r1;
	private final ModelPartExtension end_exterior;
	private final ModelPartExtension upper_wall_2_r2;
	private final ModelPartExtension upper_wall_1_r2;
	private final ModelPartExtension roof_end;
	private final ModelPartExtension handrail_2_r3;
	private final ModelPartExtension inner_roof_7_r1;
	private final ModelPartExtension inner_roof_1;
	private final ModelPartExtension inner_roof_6_r1;
	private final ModelPartExtension inner_roof_4_r3;
	private final ModelPartExtension inner_roof_2_r3;
	private final ModelPartExtension inner_roof_2;
	private final ModelPartExtension inner_roof_6_r2;
	private final ModelPartExtension inner_roof_4_r4;
	private final ModelPartExtension inner_roof_2_r4;
	private final ModelPartExtension roof_end_exterior;
	private final ModelPartExtension vent_2_r1;
	private final ModelPartExtension vent_1_r1;
	private final ModelPartExtension outer_roof_1;
	private final ModelPartExtension outer_roof_5_r2;
	private final ModelPartExtension outer_roof_4_r2;
	private final ModelPartExtension outer_roof_3_r2;
	private final ModelPartExtension outer_roof_2_r2;
	private final ModelPartExtension outer_roof_1_r2;
	private final ModelPartExtension outer_roof_2;
	private final ModelPartExtension outer_roof_5_r3;
	private final ModelPartExtension outer_roof_4_r3;
	private final ModelPartExtension outer_roof_3_r3;
	private final ModelPartExtension outer_roof_2_r3;
	private final ModelPartExtension outer_roof_1_r3;
	private final ModelPartExtension roof_light;
	private final ModelPartExtension roof_light_r1;
	private final ModelPartExtension roof_end_light;
	private final ModelPartExtension light_5_r1;
	private final ModelPartExtension light_4_r1;
	private final ModelPartExtension light_3_r1;
	private final ModelPartExtension light_2_r1;
	private final ModelPartExtension light_1_r1;
	private final ModelPartExtension head;
	private final ModelPartExtension upper_wall_2_r3;
	private final ModelPartExtension upper_wall_1_r3;
	private final ModelPartExtension head_exterior;
	private final ModelPartExtension driver_door_upper_2_r1;
	private final ModelPartExtension driver_door_upper_1_r1;
	private final ModelPartExtension front;
	private final ModelPartExtension bottom_r1;
	private final ModelPartExtension front_middle_top_r1;
	private final ModelPartExtension front_panel_2_r1;
	private final ModelPartExtension front_panel_1_r1;
	private final ModelPartExtension side_1;
	private final ModelPartExtension front_side_bottom_1_r1;
	private final ModelPartExtension outer_roof_4_r4;
	private final ModelPartExtension outer_roof_1_r4;
	private final ModelPartExtension outer_roof_2_r4;
	private final ModelPartExtension outer_roof_3_r4;
	private final ModelPartExtension front_side_lower_1_r1;
	private final ModelPartExtension front_side_upper_1_r1;
	private final ModelPartExtension side_2;
	private final ModelPartExtension front_side_bottom_2_r1;
	private final ModelPartExtension outer_roof_8_r1;
	private final ModelPartExtension outer_roof_7_r1;
	private final ModelPartExtension outer_roof_6_r1;
	private final ModelPartExtension outer_roof_5_r4;
	private final ModelPartExtension front_side_upper_2_r1;
	private final ModelPartExtension front_side_lower_2_r1;
	private final ModelPartExtension nose;
	private final ModelPartExtension center_nose;
	private final ModelPartExtension nose_5_r1;
	private final ModelPartExtension nose_4_r1;
	private final ModelPartExtension nose_2_r1;
	private final ModelPartExtension nose_side_1;
	private final ModelPartExtension nose_18_r1;
	private final ModelPartExtension nose_17_r1;
	private final ModelPartExtension nose_16_r1;
	private final ModelPartExtension nose_15_r1;
	private final ModelPartExtension nose_14_r1;
	private final ModelPartExtension nose_13_r1;
	private final ModelPartExtension nose_12_r1;
	private final ModelPartExtension nose_11_r1;
	private final ModelPartExtension nose_10_r1;
	private final ModelPartExtension nose_9_r1;
	private final ModelPartExtension nose_8_r1;
	private final ModelPartExtension nose_7_r1;
	private final ModelPartExtension nose_6_r1;
	private final ModelPartExtension nose_5_r2;
	private final ModelPartExtension nose_4_r2;
	private final ModelPartExtension nose_3_r1;
	private final ModelPartExtension nose_2_r2;
	private final ModelPartExtension nose_side_2;
	private final ModelPartExtension nose_19_r1;
	private final ModelPartExtension nose_18_r2;
	private final ModelPartExtension nose_17_r2;
	private final ModelPartExtension nose_16_r2;
	private final ModelPartExtension nose_15_r2;
	private final ModelPartExtension nose_14_r2;
	private final ModelPartExtension nose_13_r2;
	private final ModelPartExtension nose_12_r2;
	private final ModelPartExtension nose_11_r2;
	private final ModelPartExtension nose_10_r2;
	private final ModelPartExtension nose_9_r2;
	private final ModelPartExtension nose_8_r2;
	private final ModelPartExtension nose_7_r2;
	private final ModelPartExtension nose_6_r2;
	private final ModelPartExtension nose_5_r3;
	private final ModelPartExtension nose_4_r3;
	private final ModelPartExtension nose_3_r2;
	private final ModelPartExtension roof_head_exterior;
	private final ModelPartExtension vent_3_r1;
	private final ModelPartExtension vent_2_r2;
	private final ModelPartExtension outer_roof_3;
	private final ModelPartExtension outer_roof_6_r2;
	private final ModelPartExtension outer_roof_5_r5;
	private final ModelPartExtension outer_roof_4_r5;
	private final ModelPartExtension outer_roof_3_r5;
	private final ModelPartExtension outer_roof_2_r5;
	private final ModelPartExtension outer_roof_4;
	private final ModelPartExtension outer_roof_6_r3;
	private final ModelPartExtension outer_roof_5_r6;
	private final ModelPartExtension outer_roof_4_r6;
	private final ModelPartExtension outer_roof_3_r6;
	private final ModelPartExtension outer_roof_2_r6;
	private final ModelPartExtension headlights;
	private final ModelPartExtension headlight_2_r1;
	private final ModelPartExtension tail_lights;
	private final ModelPartExtension headlight_4_r1;
	private final ModelPartExtension headlight_3_r1;
	private final ModelPartExtension headlight_2_r2;
	private final ModelPartExtension headlight_1_r1;
	private final ModelPartExtension door_light;
	private final ModelPartExtension outer_roof_1_r5;
	private final ModelPartExtension door_light_on;
	private final ModelPartExtension light_r1;
	private final ModelPartExtension door_light_off;
	private final ModelPartExtension light_r2;
	private final ModelPartExtension side_panel_1;
	private final ModelPartExtension handrail_r1;
	private final ModelPartExtension side_panel_2;
	private final ModelPartExtension side_panel_translucent;
	private final ModelPartExtension seat_1;
	private final ModelPartExtension seat_back_2_r1;
	private final ModelPartExtension seat_2;
	private final ModelPartExtension seat_back_3_r1;
	private final ModelPartExtension seat_curve;
	private final ModelPartExtension seat_panel_2_r1;
	private final ModelPartExtension statue_box;
	private final ModelPartExtension statue_box_3_r1;
	private final ModelPartExtension statue_box_1_r1;
	private final ModelPartExtension seat_side_1;
	private final ModelPartExtension seat_back_6_r1;
	private final ModelPartExtension seat_top_5_r1;
	private final ModelPartExtension seat_top_7_r1;
	private final ModelPartExtension seat_back_3_r2;
	private final ModelPartExtension seat_bottom_4_r1;
	private final ModelPartExtension seat_bottom_5_r1;
	private final ModelPartExtension seat_side_2;
	private final ModelPartExtension seat_top_8_r1;
	private final ModelPartExtension seat_top_6_r1;
	private final ModelPartExtension seat_back_5_r1;
	private final ModelPartExtension seat_bottom_6_r1;
	private final ModelPartExtension seat_back_4_r1;
	private final ModelPartExtension seat_bottom_3_r1;
	private final ModelPartExtension window_edge;
	private final ModelPartExtension edge_side_1;
	private final ModelPartExtension window_edge_2_r1;
	private final ModelPartExtension window_edge_1_r1;
	private final ModelPartExtension edge_side_2;
	private final ModelPartExtension window_edge_3_r1;
	private final ModelPartExtension window_edge_2_r2;
	private final ModelPartExtension statue_box_translucent;
	private final ModelPartExtension statue_box_translucent_3_r1;
	private final ModelPartExtension statue_box_translucent_1_r1;

	public ModelDRL() {
		this(DoorAnimationType.STANDARD_SLOW, true);
	}

	private ModelDRL(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		super(360, 360, doorAnimationType, renderDoorOverlay);

		window = createModelPart();
		window.setPivot(0, 24, 0);
		window.setTextureUVOffset(0, 83).addCuboid(-20, 0, -20, 20, 1, 40, 0, false);
		window.setTextureUVOffset(202, 206).addCuboid(-20, -14, -20, 2, 14, 40, 0, false);

		upper_wall_r1 = createModelPart();
		upper_wall_r1.setPivot(-20, -14, 0);
		window.addChild(upper_wall_r1);
		setRotationAngle(upper_wall_r1, 0, 0, 0.1107F);
		upper_wall_r1.setTextureUVOffset(134, 12).addCuboid(0, -19, -20, 2, 19, 40, 0, false);

		window_handrails_1 = createModelPart();
		window_handrails_1.setPivot(0, 24, 0);
		window_handrails_1.setTextureUVOffset(359, 3).addCuboid(0, -33, -40, 0, 33, 0, 0.2F, false);
		window_handrails_1.setTextureUVOffset(359, 3).addCuboid(0, -33, 20, 0, 33, 0, 0.2F, false);
		window_handrails_1.setTextureUVOffset(359, 3).addCuboid(0, -33, 40, 0, 33, 0, 0.2F, false);
		window_handrails_1.setTextureUVOffset(17, 44).addCuboid(-1.5F, -32, 34, 3, 4, 0, 0, false);
		window_handrails_1.setTextureUVOffset(17, 44).addCuboid(-1.5F, -32, 26, 3, 4, 0, 0, false);
		window_handrails_1.setTextureUVOffset(17, 44).addCuboid(-1.5F, -32, 14, 3, 4, 0, 0, false);
		window_handrails_1.setTextureUVOffset(17, 44).addCuboid(-1.5F, -32, 8, 3, 4, 0, 0, false);
		window_handrails_1.setTextureUVOffset(17, 44).addCuboid(-1.5F, -32, 2, 3, 4, 0, 0, false);
		window_handrails_1.setTextureUVOffset(17, 44).addCuboid(-1.5F, -32, 46, 3, 4, 0, 0, false);
		window_handrails_1.setTextureUVOffset(17, 44).addCuboid(-1.5F, -32, 52, 3, 4, 0, 0, false);
		window_handrails_1.setTextureUVOffset(17, 44).addCuboid(-1.5F, -32, -46, 3, 4, 0, 0, false);
		window_handrails_1.setTextureUVOffset(17, 44).addCuboid(-1.5F, -32, -52, 3, 4, 0, 0, false);
		window_handrails_1.setTextureUVOffset(17, 44).addCuboid(-1.5F, -32, -58, 3, 4, 0, 0, false);
		window_handrails_1.setTextureUVOffset(17, 44).addCuboid(-1.5F, -32, -34, 3, 4, 0, 0, false);
		window_handrails_1.setTextureUVOffset(17, 44).addCuboid(-1.5F, -32, -28, 3, 4, 0, 0, false);
		window_handrails_1.setTextureUVOffset(17, 44).addCuboid(-1.5F, -32, -22, 3, 4, 0, 0, false);

		handrail_3_r1 = createModelPart();
		handrail_3_r1.setPivot(0, 0, 0);
		window_handrails_1.addChild(handrail_3_r1);
		setRotationAngle(handrail_3_r1, -1.5708F, 0, 0);
		handrail_3_r1.setTextureUVOffset(352, 0).addCuboid(0, -16, -31.5F, 0, 32, 0, 0.2F, false);

		handrail_2_r1 = createModelPart();
		handrail_2_r1.setPivot(0, 0, 40);
		window_handrails_1.addChild(handrail_2_r1);
		setRotationAngle(handrail_2_r1, -1.5708F, 0, 0);
		handrail_2_r1.setTextureUVOffset(352, 0).addCuboid(0, -24, -31.5F, 0, 48, 0, 0.2F, false);

		handrail_1_r1 = createModelPart();
		handrail_1_r1.setPivot(0, 0, -40);
		window_handrails_1.addChild(handrail_1_r1);
		setRotationAngle(handrail_1_r1, -1.5708F, 0, 0);
		handrail_1_r1.setTextureUVOffset(352, 0).addCuboid(0, -24, -31.5F, 0, 48, 0, 0.2F, false);

		window_handrails_2 = createModelPart();
		window_handrails_2.setPivot(0, 24, 0);
		window_handrails_2.setTextureUVOffset(359, 3).addCuboid(0, -33, -40, 0, 33, 0, 0.2F, false);
		window_handrails_2.setTextureUVOffset(359, 3).addCuboid(0, -33, -20, 0, 33, 0, 0.2F, false);
		window_handrails_2.setTextureUVOffset(359, 3).addCuboid(0, -33, 40, 0, 33, 0, 0.2F, false);
		window_handrails_2.setTextureUVOffset(17, 44).addCuboid(-1.5F, -32, 34, 3, 4, 0, 0, false);
		window_handrails_2.setTextureUVOffset(17, 44).addCuboid(-1.5F, -32, 28, 3, 4, 0, 0, false);
		window_handrails_2.setTextureUVOffset(17, 44).addCuboid(-1.5F, -32, 22, 3, 4, 0, 0, false);
		window_handrails_2.setTextureUVOffset(17, 44).addCuboid(-1.5F, -32, -2, 3, 4, 0, 0, false);
		window_handrails_2.setTextureUVOffset(17, 44).addCuboid(-1.5F, -32, -8, 3, 4, 0, 0, false);
		window_handrails_2.setTextureUVOffset(17, 44).addCuboid(-1.5F, -32, 46, 3, 4, 0, 0, false);
		window_handrails_2.setTextureUVOffset(17, 44).addCuboid(-1.5F, -32, 52, 3, 4, 0, 0, false);
		window_handrails_2.setTextureUVOffset(17, 44).addCuboid(-1.5F, -32, -46, 3, 4, 0, 0, false);
		window_handrails_2.setTextureUVOffset(17, 44).addCuboid(-1.5F, -32, -52, 3, 4, 0, 0, false);
		window_handrails_2.setTextureUVOffset(17, 44).addCuboid(-1.5F, -32, 58, 3, 4, 0, 0, false);
		window_handrails_2.setTextureUVOffset(17, 44).addCuboid(-1.5F, -32, -34, 3, 4, 0, 0, false);
		window_handrails_2.setTextureUVOffset(17, 44).addCuboid(-1.5F, -32, -26, 3, 4, 0, 0, false);
		window_handrails_2.setTextureUVOffset(17, 44).addCuboid(-1.5F, -32, -14, 3, 4, 0, 0, false);

		handrail_4_r1 = createModelPart();
		handrail_4_r1.setPivot(0, 0, 0);
		window_handrails_2.addChild(handrail_4_r1);
		setRotationAngle(handrail_4_r1, -1.5708F, 0, 0);
		handrail_4_r1.setTextureUVOffset(352, 0).addCuboid(0, -16, -31.5F, 0, 32, 0, 0.2F, false);

		handrail_3_r2 = createModelPart();
		handrail_3_r2.setPivot(0, 0, 40);
		window_handrails_2.addChild(handrail_3_r2);
		setRotationAngle(handrail_3_r2, -1.5708F, 0, 0);
		handrail_3_r2.setTextureUVOffset(352, 0).addCuboid(0, -24, -31.5F, 0, 48, 0, 0.2F, false);

		handrail_2_r2 = createModelPart();
		handrail_2_r2.setPivot(0, 0, -40);
		window_handrails_2.addChild(handrail_2_r2);
		setRotationAngle(handrail_2_r2, -1.5708F, 0, 0);
		handrail_2_r2.setTextureUVOffset(352, 0).addCuboid(0, -24, -31.5F, 0, 48, 0, 0.2F, false);

		window_exterior = createModelPart();
		window_exterior.setPivot(0, 24, 0);
		window_exterior.setTextureUVOffset(0, 0).addCuboid(-20, -14, -66, 1, 14, 132, 0, false);

		upper_wall_5_r1 = createModelPart();
		upper_wall_5_r1.setPivot(40.6269F, -7.2639F, 65);
		window_exterior.addChild(upper_wall_5_r1);
		setRotationAngle(upper_wall_5_r1, 0, -1.5708F, 0.1107F);
		upper_wall_5_r1.setTextureUVOffset(131, 146).addCuboid(-5, -19, 60, 6, 19, 1, 0, false);
		upper_wall_5_r1.setTextureUVOffset(131, 146).addCuboid(-131, -19, 60, 6, 19, 1, 0, true);

		upper_wall_3_r1 = createModelPart();
		upper_wall_3_r1.setPivot(-20, -14, 0);
		window_exterior.addChild(upper_wall_3_r1);
		setRotationAngle(upper_wall_3_r1, 0, 0, 0.1107F);
		upper_wall_3_r1.setTextureUVOffset(120, 198).addCuboid(0, -19, -60, 1, 19, 40, 0, false);
		upper_wall_3_r1.setTextureUVOffset(120, 198).addCuboid(0, -19, -20, 1, 19, 40, 0, false);
		upper_wall_3_r1.setTextureUVOffset(120, 198).addCuboid(0, -19, 20, 1, 19, 40, 0, false);

		floor_4_r1 = createModelPart();
		floor_4_r1.setPivot(30, 0, 84);
		window_exterior.addChild(floor_4_r1);
		setRotationAngle(floor_4_r1, 0, -1.5708F, 0);
		floor_4_r1.setTextureUVOffset(0, 67).addCuboid(-148, 0, 50, 14, 4, 1, 0, true);
		floor_4_r1.setTextureUVOffset(0, 67).addCuboid(-34, 0, 50, 14, 4, 1, 0, false);

		floor_3_r1 = createModelPart();
		floor_3_r1.setPivot(-20, 0, 70);
		window_exterior.addChild(floor_3_r1);
		setRotationAngle(floor_3_r1, 0, -1.5708F, 0);
		floor_3_r1.setTextureUVOffset(0, 126).addCuboid(-120, 0, 0, 50, 4, 1, 0, true);
		floor_3_r1.setTextureUVOffset(0, 126).addCuboid(-70, 0, 0, 50, 4, 1, 0, false);

		roof_window_1 = createModelPart();
		roof_window_1.setPivot(0, 24, 0);
		roof_window_1.setTextureUVOffset(54, 33).addCuboid(-16, -32, -24, 3, 0, 48, 0, false);
		roof_window_1.setTextureUVOffset(32, 33).addCuboid(-10, -34, -24, 7, 0, 48, 0, false);
		roof_window_1.setTextureUVOffset(68, 33).addCuboid(-2, -33, -24, 2, 0, 48, 0, false);

		inner_roof_4_r1 = createModelPart();
		inner_roof_4_r1.setPivot(-2, -33, 0);
		roof_window_1.addChild(inner_roof_4_r1);
		setRotationAngle(inner_roof_4_r1, 0, 0, 0.5236F);
		inner_roof_4_r1.setTextureUVOffset(72, 0).addCuboid(-2, 0, -24, 2, 0, 48, 0, false);

		inner_roof_2_r1 = createModelPart();
		inner_roof_2_r1.setPivot(-13, -32, 0);
		roof_window_1.addChild(inner_roof_2_r1);
		setRotationAngle(inner_roof_2_r1, 0, 0, -0.5236F);
		inner_roof_2_r1.setTextureUVOffset(46, 33).addCuboid(0, 0, -24, 4, 0, 48, 0, false);

		roof_window_2 = createModelPart();
		roof_window_2.setPivot(0, 24, 0);
		roof_window_2.setTextureUVOffset(92, 83).addCuboid(-16, -32, -16, 3, 0, 32, 0, false);
		roof_window_2.setTextureUVOffset(122, 0).addCuboid(-10, -34, -16, 7, 0, 32, 0, false);
		roof_window_2.setTextureUVOffset(136, 0).addCuboid(-2, -33, -16, 2, 0, 32, 0, false);

		inner_roof_5_r1 = createModelPart();
		inner_roof_5_r1.setPivot(-2, -33, 8);
		roof_window_2.addChild(inner_roof_5_r1);
		setRotationAngle(inner_roof_5_r1, 0, 0, 0.5236F);
		inner_roof_5_r1.setTextureUVOffset(0, 146).addCuboid(-2, 0, -24, 2, 0, 32, 0, false);

		inner_roof_3_r1 = createModelPart();
		inner_roof_3_r1.setPivot(-13, -32, 8);
		roof_window_2.addChild(inner_roof_3_r1);
		setRotationAngle(inner_roof_3_r1, 0, 0, -0.5236F);
		inner_roof_3_r1.setTextureUVOffset(0, 33).addCuboid(0, 0, -24, 4, 0, 32, 0, false);

		roof_window_3 = createModelPart();
		roof_window_3.setPivot(0, 24, 0);
		roof_window_3.setTextureUVOffset(54, 33).addCuboid(-16, -32, -24, 3, 0, 48, 0, false);
		roof_window_3.setTextureUVOffset(32, 33).addCuboid(-10, -34, -24, 7, 0, 48, 0, false);
		roof_window_3.setTextureUVOffset(68, 33).addCuboid(-2, -33, -24, 2, 0, 48, 0, false);

		inner_roof_5_r2 = createModelPart();
		inner_roof_5_r2.setPivot(-2, -33, 0);
		roof_window_3.addChild(inner_roof_5_r2);
		setRotationAngle(inner_roof_5_r2, 0, 0, 0.5236F);
		inner_roof_5_r2.setTextureUVOffset(72, 0).addCuboid(-2, 0, -24, 2, 0, 48, 0, false);

		inner_roof_3_r2 = createModelPart();
		inner_roof_3_r2.setPivot(-13, -32, 0);
		roof_window_3.addChild(inner_roof_3_r2);
		setRotationAngle(inner_roof_3_r2, 0, 0, -0.5236F);
		inner_roof_3_r2.setTextureUVOffset(252, 0).addCuboid(0, 0, -24, 4, 0, 48, 0, false);

		roof_window_light = createModelPart();
		roof_window_light.setPivot(0, 24, 0);


		window_light_r1 = createModelPart();
		window_light_r1.setPivot(-13, -32, 0);
		roof_window_light.addChild(window_light_r1);
		setRotationAngle(window_light_r1, 0, 0, -0.5236F);
		window_light_r1.setTextureUVOffset(60, 33).addCuboid(1, -0.001F, -24, 2, 0, 48, 0, false);

		roof_door = createModelPart();
		roof_door.setPivot(0, 24, 0);
		roof_door.setTextureUVOffset(88, 48).addCuboid(-18, -32, -16, 5, 0, 32, 0, false);
		roof_door.setTextureUVOffset(122, 0).addCuboid(-10, -34, -16, 7, 0, 32, 0, false);
		roof_door.setTextureUVOffset(136, 0).addCuboid(-2, -33, -16, 2, 0, 32, 0, false);

		inner_roof_4_r2 = createModelPart();
		inner_roof_4_r2.setPivot(-2, -33, 0);
		roof_door.addChild(inner_roof_4_r2);
		setRotationAngle(inner_roof_4_r2, 0, 0, 0.5236F);
		inner_roof_4_r2.setTextureUVOffset(0, 146).addCuboid(-2, 0, -16, 2, 0, 32, 0, false);

		inner_roof_2_r2 = createModelPart();
		inner_roof_2_r2.setPivot(-13, -32, 0);
		roof_door.addChild(inner_roof_2_r2);
		setRotationAngle(inner_roof_2_r2, 0, 0, -0.5236F);
		inner_roof_2_r2.setTextureUVOffset(0, 83).addCuboid(0, 0, -16, 4, 0, 32, 0, true);

		roof_exterior = createModelPart();
		roof_exterior.setPivot(0, 24, 0);
		roof_exterior.setTextureUVOffset(56, 83).addCuboid(-6, -42, -20, 6, 0, 40, 0, false);

		outer_roof_5_r1 = createModelPart();
		outer_roof_5_r1.setPivot(-9.9394F, -41.3064F, 0);
		roof_exterior.addChild(outer_roof_5_r1);
		setRotationAngle(outer_roof_5_r1, 0, 0, -0.1745F);
		outer_roof_5_r1.setTextureUVOffset(40, 83).addCuboid(-4, 0, -20, 8, 0, 40, 0, false);

		outer_roof_4_r1 = createModelPart();
		outer_roof_4_r1.setPivot(-15.1778F, -39.8628F, 0);
		roof_exterior.addChild(outer_roof_4_r1);
		setRotationAngle(outer_roof_4_r1, 0, 0, -0.5236F);
		outer_roof_4_r1.setTextureUVOffset(0, 33).addCuboid(-1.5F, 0, -20, 3, 0, 40, 0, false);

		outer_roof_3_r1 = createModelPart();
		outer_roof_3_r1.setPivot(-16.9769F, -38.2468F, 0);
		roof_exterior.addChild(outer_roof_3_r1);
		setRotationAngle(outer_roof_3_r1, 0, 0, -1.0472F);
		outer_roof_3_r1.setTextureUVOffset(68, 83).addCuboid(-1, 0, -20, 2, 0, 40, 0, false);

		outer_roof_2_r1 = createModelPart();
		outer_roof_2_r1.setPivot(-17.5872F, -36.3872F, 0);
		roof_exterior.addChild(outer_roof_2_r1);
		setRotationAngle(outer_roof_2_r1, 0, 0, 0.1107F);
		outer_roof_2_r1.setTextureUVOffset(0, 84).addCuboid(0, -1, -20, 0, 2, 40, 0, false);

		outer_roof_1_r1 = createModelPart();
		outer_roof_1_r1.setPivot(-20, -14, 0);
		roof_exterior.addChild(outer_roof_1_r1);
		setRotationAngle(outer_roof_1_r1, 0, 0, 0.1107F);
		outer_roof_1_r1.setTextureUVOffset(72, 239).addCuboid(-1, -22, -20, 1, 4, 40, 0, false);

		door = createModelPart();
		door.setPivot(0, 24, 0);
		door.setTextureUVOffset(0, 198).addCuboid(-20, 0, -20, 20, 1, 40, 0, false);

		door_right = createModelPart();
		door_right.setPivot(0, 0, 0);
		door.addChild(door_right);
		door_right.setTextureUVOffset(125, 198).addCuboid(-20.8F, -14, -15, 1, 14, 15, 0, false);

		door_right_top_r1 = createModelPart();
		door_right_top_r1.setPivot(-20.8F, -14, 0);
		door_right.addChild(door_right_top_r1);
		setRotationAngle(door_right_top_r1, 0, 0, 0.1107F);
		door_right_top_r1.setTextureUVOffset(0, 33).addCuboid(0, -19, -15, 1, 19, 15, 0, false);

		door_left = createModelPart();
		door_left.setPivot(0, 0, 0);
		door.addChild(door_left);
		door_left.setTextureUVOffset(0, 256).addCuboid(-20.8F, -14, 0, 1, 14, 15, 0, false);

		door_left_top_r1 = createModelPart();
		door_left_top_r1.setPivot(-20.8F, -14, 0);
		door_left.addChild(door_left_top_r1);
		setRotationAngle(door_left_top_r1, 0, 0, 0.1107F);
		door_left_top_r1.setTextureUVOffset(0, 83).addCuboid(0, -19, 0, 1, 19, 15, 0, false);

		door_handrail_1 = createModelPart();
		door_handrail_1.setPivot(0, 24, 0);
		door_handrail_1.setTextureUVOffset(352, 0).addCuboid(0, -33.75F, 0, 0, 2, 0, 0.2F, false);
		door_handrail_1.setTextureUVOffset(359, 4).addCuboid(0, -31.5F, -14, 0, 32, 0, 0.2F, false);
		door_handrail_1.setTextureUVOffset(359, 4).addCuboid(0, -31.5F, 14, 0, 32, 0, 0.2F, false);

		handrail_11_r1 = createModelPart();
		handrail_11_r1.setPivot(0, 0, -30);
		door_handrail_1.addChild(handrail_11_r1);
		setRotationAngle(handrail_11_r1, -1.5708F, 0, 0);
		handrail_11_r1.setTextureUVOffset(352, 0).addCuboid(0, -46, -31.5F, 0, 32, 0, 0.2F, false);

		door_handrail_2 = createModelPart();
		door_handrail_2.setPivot(0, 24, 0);
		door_handrail_2.setTextureUVOffset(359, 3).addCuboid(0, -33, 0, 0, 33, 0, 0.2F, false);

		door_exterior = createModelPart();
		door_exterior.setPivot(0, 24, 0);
		door_exterior.setTextureUVOffset(0, 256).addCuboid(-21, 0, -16, 1, 4, 32, 0, false);

		door_left_exterior = createModelPart();
		door_left_exterior.setPivot(0, 0, 0);
		door_exterior.addChild(door_left_exterior);
		door_left_exterior.setTextureUVOffset(197, 164).addCuboid(-20.799F, -14, 0, 0, 14, 15, 0, false);

		door_left_top_r2 = createModelPart();
		door_left_top_r2.setPivot(-14.8357F, -13.3373F, 0);
		door_left_exterior.addChild(door_left_top_r2);
		setRotationAngle(door_left_top_r2, 0, 0, 0.1107F);
		door_left_top_r2.setTextureUVOffset(80, 183).addCuboid(-6, -19, 0, 0, 19, 15, 0, false);

		door_right_exterior = createModelPart();
		door_right_exterior.setPivot(0, 0, 0);
		door_exterior.addChild(door_right_exterior);
		door_right_exterior.setTextureUVOffset(178, 23).addCuboid(-20.799F, -14, -15, 0, 14, 15, 0, false);

		door_right_top_r2 = createModelPart();
		door_right_top_r2.setPivot(-20.799F, -13.9999F, 0);
		door_right_exterior.addChild(door_right_top_r2);
		setRotationAngle(door_right_top_r2, 0, 0, 0.1107F);
		door_right_top_r2.setTextureUVOffset(0, 183).addCuboid(0, -19, -15, 0, 19, 15, 0, false);

		end = createModelPart();
		end.setPivot(0, 24, 0);
		end.setTextureUVOffset(0, 239).addCuboid(-20, 0, -12, 40, 1, 16, 0, false);
		end.setTextureUVOffset(220, 17).addCuboid(18, -14, 7, 2, 14, 3, 0, true);
		end.setTextureUVOffset(220, 17).addCuboid(-20, -14, 7, 2, 14, 3, 0, false);
		end.setTextureUVOffset(247, 127).addCuboid(9.5F, -34, -12, 9, 34, 19, 0, true);
		end.setTextureUVOffset(244, 0).addCuboid(-18.5F, -34, -12, 9, 34, 19, 0, false);

		upper_wall_2_r1 = createModelPart();
		upper_wall_2_r1.setPivot(-20, -14, 0);
		end.addChild(upper_wall_2_r1);
		setRotationAngle(upper_wall_2_r1, 0, 0, 0.1107F);
		upper_wall_2_r1.setTextureUVOffset(167, 146).addCuboid(0, -19, 7, 2, 19, 3, 0, false);

		upper_wall_1_r1 = createModelPart();
		upper_wall_1_r1.setPivot(20, -14, 0);
		end.addChild(upper_wall_1_r1);
		setRotationAngle(upper_wall_1_r1, 0, 0, -0.1107F);
		upper_wall_1_r1.setTextureUVOffset(167, 146).addCuboid(-2, -19, 7, 2, 19, 3, 0, true);

		end_exterior = createModelPart();
		end_exterior.setPivot(0, 24, 0);
		end_exterior.setTextureUVOffset(302, 279).addCuboid(20, 0, -12, 1, 4, 20, 0, true);
		end_exterior.setTextureUVOffset(302, 279).addCuboid(-21, 0, -12, 1, 4, 20, 0, false);
		end_exterior.setTextureUVOffset(0, 156).addCuboid(18, -14, -12, 2, 14, 22, 0, true);
		end_exterior.setTextureUVOffset(131, 146).addCuboid(-20, -14, -12, 2, 14, 22, 0, false);
		end_exterior.setTextureUVOffset(218, 282).addCuboid(9.5F, -34, -12, 9, 34, 0, 0, false);
		end_exterior.setTextureUVOffset(218, 282).addCuboid(-18.5F, -34, -12, 9, 34, 0, 0, true);
		end_exterior.setTextureUVOffset(80, 230).addCuboid(-18, -41, -12, 36, 7, 0, 0, false);

		upper_wall_2_r2 = createModelPart();
		upper_wall_2_r2.setPivot(-20, -14, 0);
		end_exterior.addChild(upper_wall_2_r2);
		setRotationAngle(upper_wall_2_r2, 0, 0, 0.1107F);
		upper_wall_2_r2.setTextureUVOffset(65, 146).addCuboid(0, -19, -12, 2, 19, 22, 0, false);

		upper_wall_1_r2 = createModelPart();
		upper_wall_1_r2.setPivot(20, -14, 0);
		end_exterior.addChild(upper_wall_1_r2);
		setRotationAngle(upper_wall_1_r2, 0, 0, -0.1107F);
		upper_wall_1_r2.setTextureUVOffset(65, 146).addCuboid(-2, -19, -12, 2, 19, 22, 0, true);

		roof_end = createModelPart();
		roof_end.setPivot(0, 24, 0);


		handrail_2_r3 = createModelPart();
		handrail_2_r3.setPivot(0, 0, 0);
		roof_end.addChild(handrail_2_r3);
		setRotationAngle(handrail_2_r3, -1.5708F, 0, 0);
		handrail_2_r3.setTextureUVOffset(352, 0).addCuboid(0, -40, -31.5F, 0, 16, 0, 0.2F, false);

		inner_roof_7_r1 = createModelPart();
		inner_roof_7_r1.setPivot(0, -33, 16);
		roof_end.addChild(inner_roof_7_r1);
		setRotationAngle(inner_roof_7_r1, -0.5236F, 0, 0);
		inner_roof_7_r1.setTextureUVOffset(24, 120).addCuboid(-2, 0, -2, 4, 0, 2, 0, false);

		inner_roof_1 = createModelPart();
		inner_roof_1.setPivot(-2, -33, 16);
		roof_end.addChild(inner_roof_1);
		inner_roof_1.setTextureUVOffset(76, 83).addCuboid(-17, 1, -12, 6, 0, 36, 0, false);
		inner_roof_1.setTextureUVOffset(82, 0).addCuboid(-8, -1, -28, 10, 0, 52, 0, false);
		inner_roof_1.setTextureUVOffset(4, 0).addCuboid(0, 0, 0, 2, 0, 24, 0, false);

		inner_roof_6_r1 = createModelPart();
		inner_roof_6_r1.setPivot(0, 0, 0);
		inner_roof_1.addChild(inner_roof_6_r1);
		setRotationAngle(inner_roof_6_r1, -0.5236F, 0, 0.5236F);
		inner_roof_6_r1.setTextureUVOffset(15, 33).addCuboid(-2, 0, -2, 2, 0, 2, 0, false);

		inner_roof_4_r3 = createModelPart();
		inner_roof_4_r3.setPivot(0, 0, -16);
		inner_roof_1.addChild(inner_roof_4_r3);
		setRotationAngle(inner_roof_4_r3, 0, 0, 0.5236F);
		inner_roof_4_r3.setTextureUVOffset(0, 24).addCuboid(-2, 0, 16, 2, 0, 24, 0, false);

		inner_roof_2_r3 = createModelPart();
		inner_roof_2_r3.setPivot(-11, 1, -16);
		inner_roof_1.addChild(inner_roof_2_r3);
		setRotationAngle(inner_roof_2_r3, 0, 0, -0.5236F);
		inner_roof_2_r3.setTextureUVOffset(270, 142).addCuboid(0, 0, 4, 4, 0, 36, 0, true);

		inner_roof_2 = createModelPart();
		inner_roof_2.setPivot(-2, -33, 16);
		roof_end.addChild(inner_roof_2);
		inner_roof_2.setTextureUVOffset(76, 83).addCuboid(15, 1, -12, 6, 0, 36, 0, true);
		inner_roof_2.setTextureUVOffset(82, 0).addCuboid(2, -1, -28, 10, 0, 52, 0, true);
		inner_roof_2.setTextureUVOffset(4, 0).addCuboid(2, 0, 0, 2, 0, 24, 0, true);

		inner_roof_6_r2 = createModelPart();
		inner_roof_6_r2.setPivot(4, 0, 0);
		inner_roof_2.addChild(inner_roof_6_r2);
		setRotationAngle(inner_roof_6_r2, -0.5236F, 0, -0.5236F);
		inner_roof_6_r2.setTextureUVOffset(15, 33).addCuboid(0, 0, -2, 2, 0, 2, 0, true);

		inner_roof_4_r4 = createModelPart();
		inner_roof_4_r4.setPivot(4, 0, -16);
		inner_roof_2.addChild(inner_roof_4_r4);
		setRotationAngle(inner_roof_4_r4, 0, 0, -0.5236F);
		inner_roof_4_r4.setTextureUVOffset(0, 24).addCuboid(0, 0, 16, 2, 0, 24, 0, true);

		inner_roof_2_r4 = createModelPart();
		inner_roof_2_r4.setPivot(15, 1, -16);
		inner_roof_2.addChild(inner_roof_2_r4);
		setRotationAngle(inner_roof_2_r4, 0, 0, 0.5236F);
		inner_roof_2_r4.setTextureUVOffset(88, 0).addCuboid(-4, 0, 4, 4, 0, 36, 0, true);

		roof_end_exterior = createModelPart();
		roof_end_exterior.setPivot(0, 24, 0);
		roof_end_exterior.setTextureUVOffset(0, 33).addCuboid(-8, -43, 0, 16, 2, 48, 0, false);

		vent_2_r1 = createModelPart();
		vent_2_r1.setPivot(-8, -43, 0);
		roof_end_exterior.addChild(vent_2_r1);
		setRotationAngle(vent_2_r1, 0, 0, -0.3491F);
		vent_2_r1.setTextureUVOffset(131, 148).addCuboid(-9, 0, 0, 9, 2, 48, 0, false);

		vent_1_r1 = createModelPart();
		vent_1_r1.setPivot(8, -43, 0);
		roof_end_exterior.addChild(vent_1_r1);
		setRotationAngle(vent_1_r1, 0, 0, 0.3491F);
		vent_1_r1.setTextureUVOffset(131, 148).addCuboid(0, 0, 0, 9, 2, 48, 0, true);

		outer_roof_1 = createModelPart();
		outer_roof_1.setPivot(0, 0, 0);
		roof_end_exterior.addChild(outer_roof_1);
		outer_roof_1.setTextureUVOffset(276, 104).addCuboid(-6, -42, -12, 6, 1, 20, 0, false);

		outer_roof_5_r2 = createModelPart();
		outer_roof_5_r2.setPivot(-9.7656F, -40.3206F, 0);
		outer_roof_1.addChild(outer_roof_5_r2);
		setRotationAngle(outer_roof_5_r2, 0, 0, -0.1745F);
		outer_roof_5_r2.setTextureUVOffset(34, 256).addCuboid(-4, -1, -12, 8, 1, 20, 0, false);

		outer_roof_4_r2 = createModelPart();
		outer_roof_4_r2.setPivot(-14.6775F, -38.9948F, 0);
		outer_roof_1.addChild(outer_roof_4_r2);
		setRotationAngle(outer_roof_4_r2, 0, 0, -0.5236F);
		outer_roof_4_r2.setTextureUVOffset(46, 277).addCuboid(-1.5F, -1, -12, 3, 1, 20, 0, false);

		outer_roof_3_r2 = createModelPart();
		outer_roof_3_r2.setPivot(-16.1105F, -37.7448F, 0);
		outer_roof_1.addChild(outer_roof_3_r2);
		setRotationAngle(outer_roof_3_r2, 0, 0, -1.0472F);
		outer_roof_3_r2.setTextureUVOffset(116, 283).addCuboid(-1, -1, -12, 2, 1, 20, 0, false);

		outer_roof_2_r2 = createModelPart();
		outer_roof_2_r2.setPivot(-17.587F, -36.3849F, 0);
		outer_roof_1.addChild(outer_roof_2_r2);
		setRotationAngle(outer_roof_2_r2, 0, 0, 0.1107F);
		outer_roof_2_r2.setTextureUVOffset(283, 160).addCuboid(0, -1, -12, 1, 2, 20, 0, false);

		outer_roof_1_r2 = createModelPart();
		outer_roof_1_r2.setPivot(-20, -14, 0);
		outer_roof_1.addChild(outer_roof_1_r2);
		setRotationAngle(outer_roof_1_r2, 0, 0, 0.1107F);
		outer_roof_1_r2.setTextureUVOffset(176, 281).addCuboid(-1, -22, -12, 1, 4, 20, 0, false);

		outer_roof_2 = createModelPart();
		outer_roof_2.setPivot(0, 0, 0);
		roof_end_exterior.addChild(outer_roof_2);
		outer_roof_2.setTextureUVOffset(276, 104).addCuboid(0, -42, -12, 6, 1, 20, 0, false);

		outer_roof_5_r3 = createModelPart();
		outer_roof_5_r3.setPivot(9.7656F, -40.3206F, 0);
		outer_roof_2.addChild(outer_roof_5_r3);
		setRotationAngle(outer_roof_5_r3, 0, 0, 0.1745F);
		outer_roof_5_r3.setTextureUVOffset(34, 256).addCuboid(-4, -1, -12, 8, 1, 20, 0, true);

		outer_roof_4_r3 = createModelPart();
		outer_roof_4_r3.setPivot(14.6775F, -38.9948F, 0);
		outer_roof_2.addChild(outer_roof_4_r3);
		setRotationAngle(outer_roof_4_r3, 0, 0, 0.5236F);
		outer_roof_4_r3.setTextureUVOffset(46, 277).addCuboid(-1.5F, -1, -12, 3, 1, 20, 0, true);

		outer_roof_3_r3 = createModelPart();
		outer_roof_3_r3.setPivot(16.1105F, -37.7448F, 0);
		outer_roof_2.addChild(outer_roof_3_r3);
		setRotationAngle(outer_roof_3_r3, 0, 0, 1.0472F);
		outer_roof_3_r3.setTextureUVOffset(116, 283).addCuboid(-1, -1, -12, 2, 1, 20, 0, true);

		outer_roof_2_r3 = createModelPart();
		outer_roof_2_r3.setPivot(17.587F, -36.3849F, 0);
		outer_roof_2.addChild(outer_roof_2_r3);
		setRotationAngle(outer_roof_2_r3, 0, 0, -0.1107F);
		outer_roof_2_r3.setTextureUVOffset(283, 160).addCuboid(-1, -1, -12, 1, 2, 20, 0, true);

		outer_roof_1_r3 = createModelPart();
		outer_roof_1_r3.setPivot(20, -14, 0);
		outer_roof_2.addChild(outer_roof_1_r3);
		setRotationAngle(outer_roof_1_r3, 0, 0, -0.1107F);
		outer_roof_1_r3.setTextureUVOffset(176, 281).addCuboid(0, -22, -12, 1, 4, 20, 0, true);

		roof_light = createModelPart();
		roof_light.setPivot(0, 24, 0);


		roof_light_r1 = createModelPart();
		roof_light_r1.setPivot(-2, -33, 0);
		roof_light.addChild(roof_light_r1);
		setRotationAngle(roof_light_r1, 0, 0, 0.5236F);
		roof_light_r1.setTextureUVOffset(64, 33).addCuboid(-2, -0.1F, -24, 2, 0, 48, 0, false);

		roof_end_light = createModelPart();
		roof_end_light.setPivot(0, 24, 0);


		light_5_r1 = createModelPart();
		light_5_r1.setPivot(2, -33, 0);
		roof_end_light.addChild(light_5_r1);
		setRotationAngle(light_5_r1, 0, 0, -0.5236F);
		light_5_r1.setTextureUVOffset(0, 0).addCuboid(0, -0.1F, 16, 2, 0, 24, 0, false);

		light_4_r1 = createModelPart();
		light_4_r1.setPivot(2, -33, 16);
		roof_end_light.addChild(light_4_r1);
		setRotationAngle(light_4_r1, -0.5236F, 0, -0.5236F);
		light_4_r1.setTextureUVOffset(0, 2).addCuboid(0, -0.1F, -2, 2, 0, 2, 0, true);

		light_3_r1 = createModelPart();
		light_3_r1.setPivot(0, -33, 16);
		roof_end_light.addChild(light_3_r1);
		setRotationAngle(light_3_r1, -0.5236F, 0, 0);
		light_3_r1.setTextureUVOffset(110, 119).addCuboid(-2, -0.1F, -2, 4, 0, 2, 0, false);

		light_2_r1 = createModelPart();
		light_2_r1.setPivot(-2, -33, 16);
		roof_end_light.addChild(light_2_r1);
		setRotationAngle(light_2_r1, -0.5236F, 0, 0.5236F);
		light_2_r1.setTextureUVOffset(0, 2).addCuboid(-2, -0.1F, -2, 2, 0, 2, 0, false);

		light_1_r1 = createModelPart();
		light_1_r1.setPivot(-2, -33, 0);
		roof_end_light.addChild(light_1_r1);
		setRotationAngle(light_1_r1, 0, 0, 0.5236F);
		light_1_r1.setTextureUVOffset(0, 0).addCuboid(-2, -0.1F, 16, 2, 0, 24, 0, false);

		head = createModelPart();
		head.setPivot(0, 24, 0);
		head.setTextureUVOffset(91, 146).addCuboid(18, -14, 4, 2, 14, 6, 0, false);
		head.setTextureUVOffset(91, 146).addCuboid(-20, -14, 4, 2, 14, 6, 0, true);
		head.setTextureUVOffset(246, 206).addCuboid(-18, -34, 4, 36, 34, 0, 0, false);

		upper_wall_2_r3 = createModelPart();
		upper_wall_2_r3.setPivot(-20, -14, 0);
		head.addChild(upper_wall_2_r3);
		setRotationAngle(upper_wall_2_r3, 0, 0, 0.1107F);
		upper_wall_2_r3.setTextureUVOffset(24, 211).addCuboid(0, -19, 4, 2, 19, 6, 0, true);

		upper_wall_1_r3 = createModelPart();
		upper_wall_1_r3.setPivot(20, -14, 0);
		head.addChild(upper_wall_1_r3);
		setRotationAngle(upper_wall_1_r3, 0, 0, -0.1107F);
		upper_wall_1_r3.setTextureUVOffset(24, 211).addCuboid(-2, -19, 4, 2, 19, 6, 0, false);

		head_exterior = createModelPart();
		head_exterior.setPivot(0, 24, 0);
		head_exterior.setTextureUVOffset(134, 103).addCuboid(-21, 0, -18, 42, 7, 22, 0, false);
		head_exterior.setTextureUVOffset(125, 196).addCuboid(20, 0, 4, 1, 7, 4, 0, true);
		head_exterior.setTextureUVOffset(125, 196).addCuboid(-21, 0, 4, 1, 7, 4, 0, false);
		head_exterior.setTextureUVOffset(154, 257).addCuboid(18, -14, -9, 2, 14, 19, 0, false);
		head_exterior.setTextureUVOffset(197, 146).addCuboid(-20, -14, -9, 2, 14, 19, 0, true);
		head_exterior.setTextureUVOffset(90, 256).addCuboid(18, -14, -18, 1, 14, 9, 0, false);
		head_exterior.setTextureUVOffset(90, 256).addCuboid(-19, -14, -18, 1, 14, 9, 0, true);
		head_exterior.setTextureUVOffset(244, 53).addCuboid(-18, -34, 3, 36, 34, 0, 0, false);

		driver_door_upper_2_r1 = createModelPart();
		driver_door_upper_2_r1.setPivot(-20, -14, 0);
		head_exterior.addChild(driver_door_upper_2_r1);
		setRotationAngle(driver_door_upper_2_r1, 0, 0, 0.1107F);
		driver_door_upper_2_r1.setTextureUVOffset(288, 240).addCuboid(1, -19, -18, 1, 19, 9, 0, true);
		driver_door_upper_2_r1.setTextureUVOffset(178, 0).addCuboid(0, -19, -9, 2, 19, 19, 0, true);

		driver_door_upper_1_r1 = createModelPart();
		driver_door_upper_1_r1.setPivot(20, -14, 0);
		head_exterior.addChild(driver_door_upper_1_r1);
		setRotationAngle(driver_door_upper_1_r1, 0, 0, -0.1107F);
		driver_door_upper_1_r1.setTextureUVOffset(288, 240).addCuboid(-2, -19, -18, 1, 19, 9, 0, false);
		driver_door_upper_1_r1.setTextureUVOffset(178, 0).addCuboid(-2, -19, -9, 2, 19, 19, 0, false);

		front = createModelPart();
		front.setPivot(0, 0, 0);
		head_exterior.addChild(front);
		front.setTextureUVOffset(134, 88).addCuboid(-19, 0.2966F, -26.5318F, 38, 5, 0, 0, false);

		bottom_r1 = createModelPart();
		bottom_r1.setPivot(0, 7, 4);
		front.addChild(bottom_r1);
		setRotationAngle(bottom_r1, -0.0698F, 0, 0);
		bottom_r1.setTextureUVOffset(0, 0).addCuboid(-21, 0, -33, 42, 0, 33, 0, false);

		front_middle_top_r1 = createModelPart();
		front_middle_top_r1.setPivot(0, -42, -12);
		front.addChild(front_middle_top_r1);
		setRotationAngle(front_middle_top_r1, 0.3491F, 0, 0);
		front_middle_top_r1.setTextureUVOffset(161, 0).addCuboid(-6, 0, -11, 12, 0, 11, 0, false);

		front_panel_2_r1 = createModelPart();
		front_panel_2_r1.setPivot(0, 41.314F, -36.3702F);
		front.addChild(front_panel_2_r1);
		setRotationAngle(front_panel_2_r1, -0.1745F, 0, 0);
		front_panel_2_r1.setTextureUVOffset(162, 206).addCuboid(-19, -82, 1, 38, 28, 0, 0, false);

		front_panel_1_r1 = createModelPart();
		front_panel_1_r1.setPivot(0, 0.2966F, -26.5318F);
		front.addChild(front_panel_1_r1);
		setRotationAngle(front_panel_1_r1, -0.0436F, 0, 0);
		front_panel_1_r1.setTextureUVOffset(134, 76).addCuboid(-19, -12, 0, 38, 12, 0, 0, false);

		side_1 = createModelPart();
		side_1.setPivot(0, 0, 0);
		front.addChild(side_1);
		side_1.setTextureUVOffset(22, 30).addCuboid(19, -14, -18, 1, 14, 0, 0, false);

		front_side_bottom_1_r1 = createModelPart();
		front_side_bottom_1_r1.setPivot(21, 0, -13);
		side_1.addChild(front_side_bottom_1_r1);
		setRotationAngle(front_side_bottom_1_r1, 0, 0.1745F, 0.1745F);
		front_side_bottom_1_r1.setTextureUVOffset(0, 50).addCuboid(0, 0, -16, 0, 7, 23, 0, false);

		outer_roof_4_r4 = createModelPart();
		outer_roof_4_r4.setPivot(6, -42, -12);
		side_1.addChild(outer_roof_4_r4);
		setRotationAngle(outer_roof_4_r4, 0.3491F, 0, 0.1745F);
		outer_roof_4_r4.setTextureUVOffset(123, 103).addCuboid(0, 0, -11, 11, 0, 11, 0, true);

		outer_roof_1_r4 = createModelPart();
		outer_roof_1_r4.setPivot(20, -14, 0);
		side_1.addChild(outer_roof_1_r4);
		setRotationAngle(outer_roof_1_r4, 0, 0, -0.1107F);
		outer_roof_1_r4.setTextureUVOffset(118, 118).addCuboid(0, -22, -18, 1, 4, 6, 0, true);
		outer_roof_1_r4.setTextureUVOffset(22, 11).addCuboid(-1, -19, -18, 1, 19, 0, 0, false);

		outer_roof_2_r4 = createModelPart();
		outer_roof_2_r4.setPivot(17.587F, -36.3849F, 0);
		side_1.addChild(outer_roof_2_r4);
		setRotationAngle(outer_roof_2_r4, 0, 0, -0.1107F);
		outer_roof_2_r4.setTextureUVOffset(0, 88).addCuboid(0, -1, -18, 0, 2, 6, 0, false);

		outer_roof_3_r4 = createModelPart();
		outer_roof_3_r4.setPivot(15.813F, -37.5414F, -17.4163F);
		side_1.addChild(outer_roof_3_r4);
		setRotationAngle(outer_roof_3_r4, 0.1745F, 0, 0.7418F);
		outer_roof_3_r4.setTextureUVOffset(6, 83).addCuboid(-3.5F, 0, -5.5F, 7, 0, 11, 0, true);

		front_side_lower_1_r1 = createModelPart();
		front_side_lower_1_r1.setPivot(20, 0, -18);
		side_1.addChild(front_side_lower_1_r1);
		setRotationAngle(front_side_lower_1_r1, 0, 0.1745F, 0);
		front_side_lower_1_r1.setTextureUVOffset(0, 0).addCuboid(0, -14, -11, 0, 20, 11, 0, true);

		front_side_upper_1_r1 = createModelPart();
		front_side_upper_1_r1.setPivot(20, -14, -18);
		side_1.addChild(front_side_upper_1_r1);
		setRotationAngle(front_side_upper_1_r1, 0, 0.1745F, -0.1107F);
		front_side_upper_1_r1.setTextureUVOffset(0, 135).addCuboid(0, -23, -11, 0, 23, 11, 0, false);

		side_2 = createModelPart();
		side_2.setPivot(-21, 0, -9);
		front.addChild(side_2);
		side_2.setTextureUVOffset(22, 30).addCuboid(1, -14, -9, 1, 14, 0, 0, false);

		front_side_bottom_2_r1 = createModelPart();
		front_side_bottom_2_r1.setPivot(0, 0, -4);
		side_2.addChild(front_side_bottom_2_r1);
		setRotationAngle(front_side_bottom_2_r1, 0, -0.1745F, -0.1745F);
		front_side_bottom_2_r1.setTextureUVOffset(0, 50).addCuboid(0, 0, -16, 0, 7, 23, 0, false);

		outer_roof_8_r1 = createModelPart();
		outer_roof_8_r1.setPivot(5.187F, -37.5414F, -8.4163F);
		side_2.addChild(outer_roof_8_r1);
		setRotationAngle(outer_roof_8_r1, 0.1745F, 0, -0.7418F);
		outer_roof_8_r1.setTextureUVOffset(6, 83).addCuboid(-3.5F, 0, -5.5F, 7, 0, 11, 0, false);

		outer_roof_7_r1 = createModelPart();
		outer_roof_7_r1.setPivot(3.413F, -36.3849F, 9);
		side_2.addChild(outer_roof_7_r1);
		setRotationAngle(outer_roof_7_r1, 0, 0, 0.1107F);
		outer_roof_7_r1.setTextureUVOffset(0, 88).addCuboid(0, -1, -18, 0, 2, 6, 0, false);

		outer_roof_6_r1 = createModelPart();
		outer_roof_6_r1.setPivot(15, -42, -3);
		side_2.addChild(outer_roof_6_r1);
		setRotationAngle(outer_roof_6_r1, 0.3491F, 0, -0.1745F);
		outer_roof_6_r1.setTextureUVOffset(123, 103).addCuboid(-11, 0, -11, 11, 0, 11, 0, false);

		outer_roof_5_r4 = createModelPart();
		outer_roof_5_r4.setPivot(1, -14, 9);
		side_2.addChild(outer_roof_5_r4);
		setRotationAngle(outer_roof_5_r4, 0, 0, 0.1107F);
		outer_roof_5_r4.setTextureUVOffset(118, 118).addCuboid(-1, -22, -18, 1, 4, 6, 0, false);
		outer_roof_5_r4.setTextureUVOffset(22, 11).addCuboid(0, -19, -18, 1, 19, 0, 0, false);

		front_side_upper_2_r1 = createModelPart();
		front_side_upper_2_r1.setPivot(1, -14, -9);
		side_2.addChild(front_side_upper_2_r1);
		setRotationAngle(front_side_upper_2_r1, 0, -0.1745F, 0.1107F);
		front_side_upper_2_r1.setTextureUVOffset(0, 135).addCuboid(0, -23, -11, 0, 23, 11, 0, false);

		front_side_lower_2_r1 = createModelPart();
		front_side_lower_2_r1.setPivot(1, 0, -9);
		side_2.addChild(front_side_lower_2_r1);
		setRotationAngle(front_side_lower_2_r1, 0, -0.1745F, 0);
		front_side_lower_2_r1.setTextureUVOffset(0, 0).addCuboid(0, -14, -11, 0, 20, 11, 0, false);

		nose = createModelPart();
		nose.setPivot(0, -1.25F, 0);
		front.addChild(nose);


		center_nose = createModelPart();
		center_nose.setPivot(0, 1.25F, 0);
		nose.addChild(center_nose);
		center_nose.setTextureUVOffset(156, 93).addCuboid(-5.5F, -0.25F, -31.25F, 11, 3, 0, 0, false);
		center_nose.setTextureUVOffset(131, 93).addCuboid(-5.5F, -0.7666F, -29.3181F, 11, 0, 3, 0, false);
		center_nose.setTextureUVOffset(0, 117).addCuboid(-5.5F, 3.0089F, -30.2838F, 11, 0, 4, 0, false);

		nose_5_r1 = createModelPart();
		nose_5_r1.setPivot(0, 3.4918F, -30.4135F);
		center_nose.addChild(nose_5_r1);
		setRotationAngle(nose_5_r1, -1.8326F, 0, 0);
		nose_5_r1.setTextureUVOffset(0, 10).addCuboid(-5.5F, 0, -0.5F, 11, 0, 1, 0, false);

		nose_4_r1 = createModelPart();
		nose_4_r1.setPivot(0.5F, -5.6845F, -27.9547F);
		center_nose.addChild(nose_4_r1);
		setRotationAngle(nose_4_r1, -0.2618F, 0, 0);
		nose_4_r1.setTextureUVOffset(0, 10).addCuboid(-6, 9, -1, 11, 0, 1, 0, false);

		nose_2_r1 = createModelPart();
		nose_2_r1.setPivot(0.5F, -9.3031F, -1.3234F);
		center_nose.addChild(nose_2_r1);
		setRotationAngle(nose_2_r1, 0.2618F, 0, 0);
		nose_2_r1.setTextureUVOffset(0, 8).addCuboid(-6, 1, -31.25F, 11, 0, 2, 0, false);

		nose_side_1 = createModelPart();
		nose_side_1.setPivot(0, 0.25F, 0);
		nose.addChild(nose_side_1);


		nose_18_r1 = createModelPart();
		nose_18_r1.setPivot(-13.2048F, 4.0101F, -25.7633F);
		nose_side_1.addChild(nose_18_r1);
		setRotationAngle(nose_18_r1, 0, 0.7854F, 0);
		nose_18_r1.setTextureUVOffset(30, 80).addCuboid(-1, 0, -0.5F, 3, 0, 1, 0, false);

		nose_17_r1 = createModelPart();
		nose_17_r1.setPivot(-10.3623F, 4.0101F, -27.9444F);
		nose_side_1.addChild(nose_17_r1);
		setRotationAngle(nose_17_r1, 0, 0.5236F, 0);
		nose_17_r1.setTextureUVOffset(14, 94).addCuboid(-2, 0, -0.5F, 4, 0, 3, 0, false);

		nose_16_r1 = createModelPart();
		nose_16_r1.setPivot(-10.9576F, -43.4921F, -9.1171F);
		nose_side_1.addChild(nose_16_r1);
		setRotationAngle(nose_16_r1, 0, 0.2618F, 0);
		nose_16_r1.setTextureUVOffset(28, 65).addCuboid(7, 47.5F, -19, 4, 0, 4, 0, false);

		nose_15_r1 = createModelPart();
		nose_15_r1.setPivot(-0.3159F, -36.8232F, 30.5981F);
		nose_side_1.addChild(nose_15_r1);
		setRotationAngle(nose_15_r1, -1.8326F, 0.2618F, 0);
		nose_15_r1.setTextureUVOffset(8, 80).addCuboid(7, 47.5F, 55, 4, 0, 1, 0, false);

		nose_14_r1 = createModelPart();
		nose_14_r1.setPivot(-10.5481F, 3.5259F, -28.2661F);
		nose_side_1.addChild(nose_14_r1);
		setRotationAngle(nose_14_r1, -1.8326F, 0.5236F, 0);
		nose_14_r1.setTextureUVOffset(8, 80).addCuboid(-2, 0, 0.5F, 4, 0, 1, 0, false);

		nose_13_r1 = createModelPart();
		nose_13_r1.setPivot(-13.297F, 4.4918F, -26.5626F);
		nose_side_1.addChild(nose_13_r1);
		setRotationAngle(nose_13_r1, -1.8326F, 0.7854F, 0);
		nose_13_r1.setTextureUVOffset(36, 80).addCuboid(-1.5F, 0, -0.5F, 3, 0, 1, 0, false);

		nose_12_r1 = createModelPart();
		nose_12_r1.setPivot(-40.9283F, -41.8727F, 18.6381F);
		nose_side_1.addChild(nose_12_r1);
		setRotationAngle(nose_12_r1, -0.2618F, 0.7854F, 0);
		nose_12_r1.setTextureUVOffset(36, 80).addCuboid(50, 47.5F, -1, 3, 0, 1, 0, false);

		nose_11_r1 = createModelPart();
		nose_11_r1.setPivot(-27.8485F, -41.8727F, -4.2314F);
		nose_side_1.addChild(nose_11_r1);
		setRotationAngle(nose_11_r1, -0.2618F, 0.5236F, 0);
		nose_11_r1.setTextureUVOffset(8, 80).addCuboid(25, 47.5F, -1, 4, 0, 1, 0, false);

		nose_10_r1 = createModelPart();
		nose_10_r1.setPivot(-12.6933F, -41.8727F, -15.595F);
		nose_side_1.addChild(nose_10_r1);
		setRotationAngle(nose_10_r1, -0.2618F, 0.2618F, 0);
		nose_10_r1.setTextureUVOffset(8, 80).addCuboid(7, 47.5F, -1, 4, 0, 1, 0, false);

		nose_9_r1 = createModelPart();
		nose_9_r1.setPivot(-20.8882F, 1.7334F, -20.1757F);
		nose_side_1.addChild(nose_9_r1);
		setRotationAngle(nose_9_r1, 0, 0.5236F, 0);
		nose_9_r1.setTextureUVOffset(27, 69).addCuboid(10, -1.5F, -1, 5, 0, 3, 0, false);

		nose_8_r1 = createModelPart();
		nose_8_r1.setPivot(-19.2301F, 1.7334F, -24.5358F);
		nose_side_1.addChild(nose_8_r1);
		setRotationAngle(nose_8_r1, 0, 0.2618F, 0);
		nose_8_r1.setTextureUVOffset(29, 115).addCuboid(11, -1.5F, -1, 4, 0, 3, 0, false);

		nose_7_r1 = createModelPart();
		nose_7_r1.setPivot(-19.6383F, 1.941F, -26.0595F);
		nose_side_1.addChild(nose_7_r1);
		setRotationAngle(nose_7_r1, 0.2618F, 0.2618F, 0);
		nose_7_r1.setTextureUVOffset(24, 118).addCuboid(11, -1.5F, -1, 4, 0, 2, 0, false);

		nose_6_r1 = createModelPart();
		nose_6_r1.setPivot(-18.2128F, 1.941F, -23.5418F);
		nose_side_1.addChild(nose_6_r1);
		setRotationAngle(nose_6_r1, 0.2618F, 0.5236F, 0);
		nose_6_r1.setTextureUVOffset(24, 118).addCuboid(7, -1.5F, -1, 4, 0, 2, 0, false);

		nose_5_r2 = createModelPart();
		nose_5_r2.setPivot(-10.4559F, 1.941F, -28.6712F);
		nose_side_1.addChild(nose_5_r2);
		setRotationAngle(nose_5_r2, 0.2618F, 0.7854F, 0);
		nose_5_r2.setTextureUVOffset(0, 0).addCuboid(-5, -1.5F, -1, 3, 0, 2, 0, false);

		nose_4_r2 = createModelPart();
		nose_4_r2.setPivot(-10.7065F, 2.25F, -28.9218F);
		nose_side_1.addChild(nose_4_r2);
		setRotationAngle(nose_4_r2, 0, 0.7854F, 0);
		nose_4_r2.setTextureUVOffset(25, 94).addCuboid(-5, -1.5F, -1, 3, 3, 0, 0, false);

		nose_3_r1 = createModelPart();
		nose_3_r1.setPivot(-18.39F, 2.25F, -23.8487F);
		nose_side_1.addChild(nose_3_r1);
		setRotationAngle(nose_3_r1, 0, 0.5236F, 0);
		nose_3_r1.setTextureUVOffset(124, 43).addCuboid(7, -1.5F, -1, 4, 3, 0, 0, false);

		nose_2_r2 = createModelPart();
		nose_2_r2.setPivot(0.5544F, 2.25F, -31.837F);
		nose_side_1.addChild(nose_2_r2);
		setRotationAngle(nose_2_r2, 0, 0.2618F, 0);
		nose_2_r2.setTextureUVOffset(124, 40).addCuboid(-10, -1.5F, -1, 4, 3, 0, 0, false);

		nose_side_2 = createModelPart();
		nose_side_2.setPivot(0, 0.25F, 0);
		nose.addChild(nose_side_2);


		nose_19_r1 = createModelPart();
		nose_19_r1.setPivot(12.4983F, 4.0098F, -26.4711F);
		nose_side_2.addChild(nose_19_r1);
		setRotationAngle(nose_19_r1, 0, -0.7854F, 0);
		nose_19_r1.setTextureUVOffset(0, 0).addCuboid(-1, 0, -0.5F, 3, 0, 1, 0, false);

		nose_18_r2 = createModelPart();
		nose_18_r2.setPivot(9.6127F, 4.0089F, -26.6459F);
		nose_side_2.addChild(nose_18_r2);
		setRotationAngle(nose_18_r2, 0, -0.5236F, 0);
		nose_18_r2.setTextureUVOffset(14, 94).addCuboid(-2, 0, -2, 4, 0, 3, 0, false);

		nose_17_r2 = createModelPart();
		nose_17_r2.setPivot(6.6643F, 4.0098F, -27.8677F);
		nose_side_2.addChild(nose_17_r2);
		setRotationAngle(nose_17_r2, 0, -0.2618F, 0);
		nose_17_r2.setTextureUVOffset(28, 65).addCuboid(-2, 0, -2, 4, 0, 4, 0, false);

		nose_16_r2 = createModelPart();
		nose_16_r2.setPivot(13.297F, 4.4918F, -26.5626F);
		nose_side_2.addChild(nose_16_r2);
		setRotationAngle(nose_16_r2, -1.8326F, -0.7854F, 0);
		nose_16_r2.setTextureUVOffset(36, 80).addCuboid(-1.5F, 0, -0.5F, 3, 0, 1, 0, false);

		nose_15_r2 = createModelPart();
		nose_15_r2.setPivot(10.6775F, 4.4918F, -28.4903F);
		nose_side_2.addChild(nose_15_r2);
		setRotationAngle(nose_15_r2, -1.8326F, -0.5236F, 0);
		nose_15_r2.setTextureUVOffset(8, 80).addCuboid(-2, 0, -0.5F, 4, 0, 1, 0, false);

		nose_14_r2 = createModelPart();
		nose_14_r2.setPivot(7.2154F, 4.4927F, -29.9246F);
		nose_side_2.addChild(nose_14_r2);
		setRotationAngle(nose_14_r2, -1.8326F, -0.2618F, 0);
		nose_14_r2.setTextureUVOffset(8, 80).addCuboid(-2, 0, -0.5F, 4, 0, 1, 0, false);

		nose_13_r2 = createModelPart();
		nose_13_r2.setPivot(10.5228F, -41.8727F, -11.7675F);
		nose_side_2.addChild(nose_13_r2);
		setRotationAngle(nose_13_r2, -0.2618F, -0.7854F, 0);
		nose_13_r2.setTextureUVOffset(36, 80).addCuboid(-10, 47.5F, -1, 3, 0, 1, 0, false);

		nose_12_r2 = createModelPart();
		nose_12_r2.setPivot(12.2601F, -41.8727F, -13.2314F);
		nose_side_2.addChild(nose_12_r2);
		setRotationAngle(nose_12_r2, -0.2618F, -0.5236F, 0);
		nose_12_r2.setTextureUVOffset(8, 80).addCuboid(-11, 47.5F, -1, 4, 0, 1, 0, false);

		nose_11_r2 = createModelPart();
		nose_11_r2.setPivot(12.6934F, -41.8717F, -15.5952F);
		nose_side_2.addChild(nose_11_r2);
		setRotationAngle(nose_11_r2, -0.2618F, -0.2618F, 0);
		nose_11_r2.setTextureUVOffset(8, 80).addCuboid(-11, 47.5F, -1, 4, 0, 1, 0, false);

		nose_10_r2 = createModelPart();
		nose_10_r2.setPivot(16.558F, 0.2334F, -22.6757F);
		nose_side_2.addChild(nose_10_r2);
		setRotationAngle(nose_10_r2, 0, -0.5236F, 0);
		nose_10_r2.setTextureUVOffset(27, 69).addCuboid(-10, 0, -1, 5, 0, 3, 0, false);

		nose_9_r2 = createModelPart();
		nose_9_r2.setPivot(6.673F, 0.2334F, -27.9004F);
		nose_side_2.addChild(nose_9_r2);
		setRotationAngle(nose_9_r2, 0, -0.2618F, 0);
		nose_9_r2.setTextureUVOffset(29, 115).addCuboid(-2, 0, -1, 4, 0, 3, 0, false);

		nose_8_r2 = createModelPart();
		nose_8_r2.setPivot(19.057F, -45.3893F, -39.3447F);
		nose_side_2.addChild(nose_8_r2);
		setRotationAngle(nose_8_r2, 0.2618F, -0.2618F, 0);
		nose_8_r2.setTextureUVOffset(24, 118).addCuboid(-11, 47.5F, -1, 4, 0, 2, 0, false);

		nose_7_r2 = createModelPart();
		nose_7_r2.setPivot(21.9558F, -45.3893F, -36.0248F);
		nose_side_2.addChild(nose_7_r2);
		setRotationAngle(nose_7_r2, 0.2618F, -0.5236F, 0);
		nose_7_r2.setTextureUVOffset(24, 118).addCuboid(-8, 47.5F, -1, 4, 0, 2, 0, false);

		nose_6_r2 = createModelPart();
		nose_6_r2.setPivot(23.6661F, -45.3893F, -33.3962F);
		nose_side_2.addChild(nose_6_r2);
		setRotationAngle(nose_6_r2, 0.2618F, -0.7854F, 0);
		nose_6_r2.setTextureUVOffset(0, 0).addCuboid(-4, 47.5F, -1, 3, 0, 2, 0, false);

		nose_5_r3 = createModelPart();
		nose_5_r3.setPivot(14.9491F, -46.75F, -24.6792F);
		nose_side_2.addChild(nose_5_r3);
		setRotationAngle(nose_5_r3, 0, -0.7854F, 0);
		nose_5_r3.setTextureUVOffset(25, 94).addCuboid(-4, 47.5F, -1, 3, 3, 0, 0, true);

		nose_4_r3 = createModelPart();
		nose_4_r3.setPivot(-9.1985F, 2.25F, -12.0641F);
		nose_side_2.addChild(nose_4_r3);
		setRotationAngle(nose_4_r3, 0, -0.5236F, 0);
		nose_4_r3.setTextureUVOffset(124, 43).addCuboid(7, -1.5F, -25, 4, 3, 0, 0, true);

		nose_3_r2 = createModelPart();
		nose_3_r2.setPivot(5.2412F, 2.25F, -30.2841F);
		nose_side_2.addChild(nose_3_r2);
		setRotationAngle(nose_3_r2, 0, -0.2618F, 0);
		nose_3_r2.setTextureUVOffset(124, 40).addCuboid(0, -1.5F, -1, 4, 3, 0, 0, false);

		roof_head_exterior = createModelPart();
		roof_head_exterior.setPivot(0, 24, 0);
		roof_head_exterior.setTextureUVOffset(0, 33).addCuboid(-8, -43, 0, 16, 2, 48, 0, false);

		vent_3_r1 = createModelPart();
		vent_3_r1.setPivot(-8, -43, 0);
		roof_head_exterior.addChild(vent_3_r1);
		setRotationAngle(vent_3_r1, 0, 0, -0.3491F);
		vent_3_r1.setTextureUVOffset(131, 148).addCuboid(-9, 0, 0, 9, 2, 48, 0, false);

		vent_2_r2 = createModelPart();
		vent_2_r2.setPivot(8, -43, 0);
		roof_head_exterior.addChild(vent_2_r2);
		setRotationAngle(vent_2_r2, 0, 0, 0.3491F);
		vent_2_r2.setTextureUVOffset(131, 148).addCuboid(0, 0, 0, 9, 2, 48, 0, true);

		outer_roof_3 = createModelPart();
		outer_roof_3.setPivot(0, 0, 0);
		roof_head_exterior.addChild(outer_roof_3);
		outer_roof_3.setTextureUVOffset(196, 260).addCuboid(-6, -42, -12, 6, 1, 20, 0, false);

		outer_roof_6_r2 = createModelPart();
		outer_roof_6_r2.setPivot(-9.7656F, -40.3206F, 0);
		outer_roof_3.addChild(outer_roof_6_r2);
		setRotationAngle(outer_roof_6_r2, 0, 0, -0.1745F);
		outer_roof_6_r2.setTextureUVOffset(240, 103).addCuboid(-4, -1, -12, 8, 1, 20, 0, false);

		outer_roof_5_r5 = createModelPart();
		outer_roof_5_r5.setPivot(-14.6775F, -38.9948F, 0);
		outer_roof_3.addChild(outer_roof_5_r5);
		setRotationAngle(outer_roof_5_r5, 0, 0, -0.5236F);
		outer_roof_5_r5.setTextureUVOffset(228, 261).addCuboid(-1.5F, -1, -12, 3, 1, 20, 0, false);

		outer_roof_4_r5 = createModelPart();
		outer_roof_4_r5.setPivot(-16.1105F, -37.7448F, 0);
		outer_roof_3.addChild(outer_roof_4_r5);
		setRotationAngle(outer_roof_4_r5, 0, 0, -1.0472F);
		outer_roof_4_r5.setTextureUVOffset(72, 283).addCuboid(-1, -1, -12, 2, 1, 20, 0, false);

		outer_roof_3_r5 = createModelPart();
		outer_roof_3_r5.setPivot(-17.587F, -36.3849F, 0);
		outer_roof_3.addChild(outer_roof_3_r5);
		setRotationAngle(outer_roof_3_r5, 0, 0, 0.1107F);
		outer_roof_3_r5.setTextureUVOffset(190, 76).addCuboid(0, -1, -12, 1, 2, 20, 0, false);

		outer_roof_2_r5 = createModelPart();
		outer_roof_2_r5.setPivot(-20, -14, 0);
		outer_roof_3.addChild(outer_roof_2_r5);
		setRotationAngle(outer_roof_2_r5, 0, 0, 0.1107F);
		outer_roof_2_r5.setTextureUVOffset(254, 279).addCuboid(-1, -22, -12, 1, 4, 20, 0, false);

		outer_roof_4 = createModelPart();
		outer_roof_4.setPivot(0, 0, 0);
		roof_head_exterior.addChild(outer_roof_4);
		outer_roof_4.setTextureUVOffset(196, 260).addCuboid(0, -42, -12, 6, 1, 20, 0, false);

		outer_roof_6_r3 = createModelPart();
		outer_roof_6_r3.setPivot(9.7656F, -40.3206F, 0);
		outer_roof_4.addChild(outer_roof_6_r3);
		setRotationAngle(outer_roof_6_r3, 0, 0, 0.1745F);
		outer_roof_6_r3.setTextureUVOffset(240, 103).addCuboid(-4, -1, -12, 8, 1, 20, 0, false);

		outer_roof_5_r6 = createModelPart();
		outer_roof_5_r6.setPivot(14.6775F, -38.9948F, 0);
		outer_roof_4.addChild(outer_roof_5_r6);
		setRotationAngle(outer_roof_5_r6, 0, 0, 0.5236F);
		outer_roof_5_r6.setTextureUVOffset(228, 261).addCuboid(-1.5F, -1, -12, 3, 1, 20, 0, true);

		outer_roof_4_r6 = createModelPart();
		outer_roof_4_r6.setPivot(16.1105F, -37.7448F, 0);
		outer_roof_4.addChild(outer_roof_4_r6);
		setRotationAngle(outer_roof_4_r6, 0, 0, 1.0472F);
		outer_roof_4_r6.setTextureUVOffset(72, 283).addCuboid(-1, -1, -12, 2, 1, 20, 0, true);

		outer_roof_3_r6 = createModelPart();
		outer_roof_3_r6.setPivot(17.587F, -36.3849F, 0);
		outer_roof_4.addChild(outer_roof_3_r6);
		setRotationAngle(outer_roof_3_r6, 0, 0, -0.1107F);
		outer_roof_3_r6.setTextureUVOffset(190, 76).addCuboid(-1, -1, -12, 1, 2, 20, 0, true);

		outer_roof_2_r6 = createModelPart();
		outer_roof_2_r6.setPivot(20, -14, 0);
		outer_roof_4.addChild(outer_roof_2_r6);
		setRotationAngle(outer_roof_2_r6, 0, 0, -0.1107F);
		outer_roof_2_r6.setTextureUVOffset(254, 279).addCuboid(0, -22, -12, 1, 4, 20, 0, true);

		headlights = createModelPart();
		headlights.setPivot(0, 24, 0);


		headlight_2_r1 = createModelPart();
		headlight_2_r1.setPivot(-1, -3.4772F, 1.663F);
		headlights.addChild(headlight_2_r1);
		setRotationAngle(headlight_2_r1, -0.0436F, 0, 0);
		headlight_2_r1.setTextureUVOffset(124, 36).addCuboid(13.75F, -3, -28.013F, 4, 4, 0, 0, true);
		headlight_2_r1.setTextureUVOffset(124, 36).addCuboid(-15.75F, -3, -28.013F, 4, 4, 0, 0, false);

		tail_lights = createModelPart();
		tail_lights.setPivot(0, 24, 0);


		headlight_4_r1 = createModelPart();
		headlight_4_r1.setPivot(-4.8579F, 1.25F, 0.7838F);
		tail_lights.addChild(headlight_4_r1);
		setRotationAngle(headlight_4_r1, 0, -0.7854F, 0);
		headlight_4_r1.setTextureUVOffset(5, 44).addCuboid(-8, -2, -33.013F, 2, 3, 0, 0, true);

		headlight_3_r1 = createModelPart();
		headlight_3_r1.setPivot(-11.4729F, 1.25F, -4.1246F);
		tail_lights.addChild(headlight_3_r1);
		setRotationAngle(headlight_3_r1, 0, -0.5236F, 0);
		headlight_3_r1.setTextureUVOffset(9, 44).addCuboid(5, -2, -33.0154F, 4, 3, 0, 0, true);

		headlight_2_r2 = createModelPart();
		headlight_2_r2.setPivot(7.2409F, 1.75F, -7.4547F);
		tail_lights.addChild(headlight_2_r2);
		setRotationAngle(headlight_2_r2, 0, 0.5236F, 0);
		headlight_2_r2.setTextureUVOffset(9, 44).addCuboid(-7, -2.5F, -28.0153F, 4, 3, 0, 0, false);

		headlight_1_r1 = createModelPart();
		headlight_1_r1.setPivot(11.929F, 1.75F, -13.3582F);
		tail_lights.addChild(headlight_1_r1);
		setRotationAngle(headlight_1_r1, 0, 0.7854F, 0);
		headlight_1_r1.setTextureUVOffset(5, 44).addCuboid(-9, -2.5F, -28.013F, 2, 3, 0, 0, false);

		door_light = createModelPart();
		door_light.setPivot(0, 24, 0);


		outer_roof_1_r5 = createModelPart();
		outer_roof_1_r5.setPivot(-20, -14, 0);
		door_light.addChild(outer_roof_1_r5);
		setRotationAngle(outer_roof_1_r5, 0, 0, 0.1107F);
		outer_roof_1_r5.setTextureUVOffset(0, 0).addCuboid(-1.1F, -22, -2, 0, 4, 4, 0, false);

		door_light_on = createModelPart();
		door_light_on.setPivot(0, 24, 0);


		light_r1 = createModelPart();
		light_r1.setPivot(-20, -14, 0);
		door_light_on.addChild(light_r1);
		setRotationAngle(light_r1, 0, 0, 0.1107F);
		light_r1.setTextureUVOffset(204, 0).addCuboid(-1, -20, 0, 0, 0, 0, 0.4F, false);

		door_light_off = createModelPart();
		door_light_off.setPivot(0, 24, 0);


		light_r2 = createModelPart();
		light_r2.setPivot(-20, -14, 0);
		door_light_off.addChild(light_r2);
		setRotationAngle(light_r2, 0, 0, 0.1107F);
		light_r2.setTextureUVOffset(204, 3).addCuboid(-1, -20, 0, 0, 0, 0, 0.4F, false);

		side_panel_1 = createModelPart();
		side_panel_1.setPivot(0, 24, 0);
		side_panel_1.setTextureUVOffset(218, 38).addCuboid(-18, -34, 0, 7, 30, 0, 0, false);

		handrail_r1 = createModelPart();
		handrail_r1.setPivot(-11, -5, 22);
		side_panel_1.addChild(handrail_r1);
		setRotationAngle(handrail_r1, 0, 0, -0.0436F);
		handrail_r1.setTextureUVOffset(355, 0).addCuboid(0, -28.2F, -22, 0, 28, 0, 0.2F, false);

		side_panel_2 = createModelPart();
		side_panel_2.setPivot(0, 24, 0);
		side_panel_2.setTextureUVOffset(0, 83).addCuboid(-18, -15, 0, 7, 11, 0, 0, false);

		side_panel_translucent = createModelPart();
		side_panel_translucent.setPivot(0, 24, 0);
		side_panel_translucent.setTextureUVOffset(36, 146).addCuboid(-18, -34, 0, 7, 30, 0, 0, false);

		seat_1 = createModelPart();
		seat_1.setPivot(0, 24, 0);
		seat_1.setTextureUVOffset(0, 146).addCuboid(-18, -6, -40, 7, 1, 51, 0, false);
		seat_1.setTextureUVOffset(80, 198).addCuboid(-18, -6, 29, 7, 1, 31, 0, false);
		seat_1.setTextureUVOffset(134, 0).addCuboid(-18, -5, -39, 6, 5, 98, 0, false);

		seat_back_2_r1 = createModelPart();
		seat_back_2_r1.setPivot(-17, -6, 0);
		seat_1.addChild(seat_back_2_r1);
		setRotationAngle(seat_back_2_r1, 0, 0, -0.0524F);
		seat_back_2_r1.setTextureUVOffset(255, 240).addCuboid(-1, -8, 29, 1, 8, 31, 0, false);
		seat_back_2_r1.setTextureUVOffset(194, 147).addCuboid(-1, -8, -40, 1, 8, 51, 0, false);

		seat_2 = createModelPart();
		seat_2.setPivot(0, 24, 0);
		seat_2.setTextureUVOffset(80, 198).addCuboid(-18, -6, -60, 7, 1, 31, 0, false);
		seat_2.setTextureUVOffset(0, 146).addCuboid(-18, -6, -11, 7, 1, 51, 0, false);
		seat_2.setTextureUVOffset(134, 0).addCuboid(-18, -5, -59, 6, 5, 98, 0, false);

		seat_back_3_r1 = createModelPart();
		seat_back_3_r1.setPivot(-17, -6, 0);
		seat_2.addChild(seat_back_3_r1);
		setRotationAngle(seat_back_3_r1, 0, 0, -0.0524F);
		seat_back_3_r1.setTextureUVOffset(194, 147).addCuboid(-1, -8, -11, 1, 8, 51, 0, false);
		seat_back_3_r1.setTextureUVOffset(255, 240).addCuboid(-1, -8, -60, 1, 8, 31, 0, false);

		seat_curve = createModelPart();
		seat_curve.setPivot(0, 24, 0);
		seat_curve.setTextureUVOffset(94, 124).addCuboid(-12.9289F, -5, -3.6538F, 8, 0, 8, 0, false);

		seat_panel_2_r1 = createModelPart();
		seat_panel_2_r1.setPivot(-4.9289F, -9.4367F, -0.9289F);
		seat_curve.addChild(seat_panel_2_r1);
		setRotationAngle(seat_panel_2_r1, 0, -1.5708F, 0);
		seat_panel_2_r1.setTextureUVOffset(134, 114).addCuboid(1.0003F, -4.5633F, -0.0001F, 9, 9, 0, 0, false);
		seat_panel_2_r1.setTextureUVOffset(134, 114).addCuboid(-7.9997F, -4.5633F, -0.0001F, 9, 9, 0, 0, true);

		statue_box = createModelPart();
		statue_box.setPivot(0, 0, 0);
		seat_curve.addChild(statue_box);
		statue_box.setTextureUVOffset(145, 142).addCuboid(-13, -32, -2, 0, 18, 4, 0, false);

		statue_box_3_r1 = createModelPart();
		statue_box_3_r1.setPivot(-16.3807F, -23, 2.9059F);
		statue_box.addChild(statue_box_3_r1);
		setRotationAngle(statue_box_3_r1, 0, 0.2618F, 0);
		statue_box_3_r1.setTextureUVOffset(212, 76).addCuboid(-2.5F, -9, 0, 6, 18, 0, 0, false);

		statue_box_1_r1 = createModelPart();
		statue_box_1_r1.setPivot(-15.4148F, -23, -2.647F);
		statue_box.addChild(statue_box_1_r1);
		setRotationAngle(statue_box_1_r1, 0, -0.2618F, 0);
		statue_box_1_r1.setTextureUVOffset(212, 76).addCuboid(-3.5F, -9, 0, 6, 18, 0, 0, false);

		seat_side_1 = createModelPart();
		seat_side_1.setPivot(0, 0, 0);
		seat_curve.addChild(seat_side_1);
		seat_side_1.setTextureUVOffset(22, 72).addCuboid(-10.9289F, -13.9357F, -0.9289F, 6, 0, 1, 0, false);

		seat_back_6_r1 = createModelPart();
		seat_back_6_r1.setPivot(-7.9289F, -9.9684F, -1.6376F);
		seat_side_1.addChild(seat_back_6_r1);
		setRotationAngle(seat_back_6_r1, -0.0524F, 0, 0);
		seat_back_6_r1.setTextureUVOffset(0, 169).addCuboid(-3, -4, -0.5F, 6, 8, 1, 0, true);

		seat_top_5_r1 = createModelPart();
		seat_top_5_r1.setPivot(-10.2218F, -12.9367F, -5.4645F);
		seat_side_1.addChild(seat_top_5_r1);
		setRotationAngle(seat_top_5_r1, 0, 0.7854F, 0);
		seat_top_5_r1.setTextureUVOffset(18, 24).addCuboid(-5, -1, -8, 2, 0, 10, 0, false);

		seat_top_7_r1 = createModelPart();
		seat_top_7_r1.setPivot(-14.9289F, -13.9367F, -3.9289F);
		seat_side_1.addChild(seat_top_7_r1);
		setRotationAngle(seat_top_7_r1, 0, 1.5708F, 0);
		seat_top_7_r1.setTextureUVOffset(0, 0).addCuboid(-4, 0, -4, 8, 0, 8, 0, false);

		seat_back_3_r2 = createModelPart();
		seat_back_3_r2.setPivot(-14.1109F, -6, -5.818F);
		seat_side_1.addChild(seat_back_3_r2);
		setRotationAngle(seat_back_3_r2, 0, 0.7854F, 0);
		seat_back_3_r2.setTextureUVOffset(0, 217).addCuboid(-0.5F, -8, -5, 1, 8, 11, 0, false);

		seat_bottom_4_r1 = createModelPart();
		seat_bottom_4_r1.setPivot(-12.6967F, -5.5F, -7.2322F);
		seat_side_1.addChild(seat_bottom_4_r1);
		setRotationAngle(seat_bottom_4_r1, 0, 0.7854F, 0);
		seat_bottom_4_r1.setTextureUVOffset(131, 182).addCuboid(-2.5F, -0.6F, -5, 6, 1, 10, 0, false);

		seat_bottom_5_r1 = createModelPart();
		seat_bottom_5_r1.setPivot(-7.9289F, -5.5F, -4.8462F);
		seat_side_1.addChild(seat_bottom_5_r1);
		setRotationAngle(seat_bottom_5_r1, 0, 1.5708F, 0);
		seat_bottom_5_r1.setTextureUVOffset(153, 182).addCuboid(-3, -0.5F, -4, 6, 1, 7, 0, false);

		seat_side_2 = createModelPart();
		seat_side_2.setPivot(0, 0, 0);
		seat_curve.addChild(seat_side_2);
		seat_side_2.setTextureUVOffset(22, 72).addCuboid(-10.9289F, -13.9357F, -0.0711F, 6, 0, 1, 0, false);

		seat_top_8_r1 = createModelPart();
		seat_top_8_r1.setPivot(-14.9289F, -13.9367F, 4.0711F);
		seat_side_2.addChild(seat_top_8_r1);
		setRotationAngle(seat_top_8_r1, 0, 1.5708F, 0);
		seat_top_8_r1.setTextureUVOffset(0, 0).addCuboid(-4, 0, -4, 8, 0, 8, 0, true);

		seat_top_6_r1 = createModelPart();
		seat_top_6_r1.setPivot(-3.1508F, -12.9367F, -1.4645F);
		seat_side_2.addChild(seat_top_6_r1);
		setRotationAngle(seat_top_6_r1, 0, -0.7854F, 0);
		seat_top_6_r1.setTextureUVOffset(18, 24).addCuboid(-5, -1, 8, 2, 0, 10, 0, false);

		seat_back_5_r1 = createModelPart();
		seat_back_5_r1.setPivot(-7.9289F, -9.9684F, 1.6376F);
		seat_side_2.addChild(seat_back_5_r1);
		setRotationAngle(seat_back_5_r1, -0.0524F, 3.1416F, 0);
		seat_back_5_r1.setTextureUVOffset(0, 169).addCuboid(-3, -4, -0.5F, 6, 8, 1, 0, false);

		seat_bottom_6_r1 = createModelPart();
		seat_bottom_6_r1.setPivot(-10.4289F, -5.5F, 5.3462F);
		seat_side_2.addChild(seat_bottom_6_r1);
		setRotationAngle(seat_bottom_6_r1, 0, 1.5708F, 0);
		seat_bottom_6_r1.setTextureUVOffset(153, 182).addCuboid(-3, -0.5F, -1.5F, 6, 1, 7, 0, true);

		seat_back_4_r1 = createModelPart();
		seat_back_4_r1.setPivot(-30.0208F, -9, 21.7279F);
		seat_side_2.addChild(seat_back_4_r1);
		setRotationAngle(seat_back_4_r1, 0, -0.7854F, 0);
		seat_back_4_r1.setTextureUVOffset(0, 217).addCuboid(-0.5F, -5, -28.5F, 1, 8, 11, 0, false);

		seat_bottom_3_r1 = createModelPart();
		seat_bottom_3_r1.setPivot(-12.6967F, -5.5F, 7.2322F);
		seat_side_2.addChild(seat_bottom_3_r1);
		setRotationAngle(seat_bottom_3_r1, 0, -0.7854F, 0);
		seat_bottom_3_r1.setTextureUVOffset(131, 182).addCuboid(-2.5F, -0.6F, -5, 6, 1, 10, 0, false);

		window_edge = createModelPart();
		window_edge.setPivot(0, 24, 0);


		edge_side_1 = createModelPart();
		edge_side_1.setPivot(0, 0, 0);
		window_edge.addChild(edge_side_1);


		window_edge_2_r1 = createModelPart();
		window_edge_2_r1.setPivot(41.6208F, -7.1535F, 64);
		edge_side_1.addChild(window_edge_2_r1);
		setRotationAngle(window_edge_2_r1, 0, -1.5708F, 0.1107F);
		window_edge_2_r1.setTextureUVOffset(65, 146).addCuboid(-4, -19, 60, 6, 19, 2, 0, false);

		window_edge_1_r1 = createModelPart();
		window_edge_1_r1.setPivot(-17, -7, 65);
		edge_side_1.addChild(window_edge_1_r1);
		setRotationAngle(window_edge_1_r1, 0, -1.5708F, 0);
		window_edge_1_r1.setTextureUVOffset(154, 32).addCuboid(-5, -7, 1, 6, 14, 2, 0, false);

		edge_side_2 = createModelPart();
		edge_side_2.setPivot(0, 0, 6);
		window_edge.addChild(edge_side_2);


		window_edge_3_r1 = createModelPart();
		window_edge_3_r1.setPivot(41.6208F, -7.1535F, -68);
		edge_side_2.addChild(window_edge_3_r1);
		setRotationAngle(window_edge_3_r1, 0, -1.5708F, 0.1107F);
		window_edge_3_r1.setTextureUVOffset(65, 146).addCuboid(-4, -19, 60, 6, 19, 2, 0, true);

		window_edge_2_r2 = createModelPart();
		window_edge_2_r2.setPivot(-17, -7, -67);
		edge_side_2.addChild(window_edge_2_r2);
		setRotationAngle(window_edge_2_r2, 0, -1.5708F, 0);
		window_edge_2_r2.setTextureUVOffset(154, 32).addCuboid(-5, -7, 1, 6, 14, 2, 0, true);

		statue_box_translucent = createModelPart();
		statue_box_translucent.setPivot(0, 24, 0);
		statue_box_translucent.setTextureUVOffset(351, 78).addCuboid(-13, -32, -2, 0, 18, 4, 0, false);

		statue_box_translucent_3_r1 = createModelPart();
		statue_box_translucent_3_r1.setPivot(-16.3807F, -23, 2.9059F);
		statue_box_translucent.addChild(statue_box_translucent_3_r1);
		setRotationAngle(statue_box_translucent_3_r1, 0, 0.2618F, 0);
		statue_box_translucent_3_r1.setTextureUVOffset(349, 57).addCuboid(-1.5F, -9, 0, 5, 18, 0, 0, false);

		statue_box_translucent_1_r1 = createModelPart();
		statue_box_translucent_1_r1.setPivot(-15.4148F, -23, -2.647F);
		statue_box_translucent.addChild(statue_box_translucent_1_r1);
		setRotationAngle(statue_box_translucent_1_r1, 0, -0.2618F, 0);
		statue_box_translucent_1_r1.setTextureUVOffset(349, 57).addCuboid(-2.5F, -9, 0, 5, 18, 0, 0, false);

		buildModel();
	}

	private static final int DOOR_MAX = 14;
	private static final ModelDoorOverlay MODEL_DOOR_OVERLAY = new ModelDoorOverlay(DOOR_MAX, 6.34F, "door_overlay_drl_left.png", "door_overlay_drl_right.png");

	@Override
	public ModelDRL createNew(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		return new ModelDRL(doorAnimationType, renderDoorOverlay);
	}

	@Override
	protected void renderWindowPositions(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
		final boolean isEvenWindow = isEvenWindow(position);
		switch (renderStage) {
			case LIGHT:
				renderMirror(roof_light, graphicsHolder, light, position);
				renderMirror(roof_light, graphicsHolder, light, position + 40);
				renderMirror(roof_light, graphicsHolder, light, position - 40);
				renderMirror(roof_window_light, graphicsHolder, light, position);
				renderMirror(roof_window_light, graphicsHolder, light, position + 40);
				renderMirror(roof_window_light, graphicsHolder, light, position - 40);
				break;
			case INTERIOR:
				renderMirror(window, graphicsHolder, light, position);
				renderMirror(window_edge, graphicsHolder, light, position);
				renderMirror(window, graphicsHolder, light, position + 40);
				renderMirror(window, graphicsHolder, light, position - 40);
				if (renderDetails) {
					renderMirror(roof_window_2, graphicsHolder, light, position);
					renderOnce(roof_window_1, graphicsHolder, light, position - 40);
					renderOnceFlipped(roof_window_1, graphicsHolder, light, position + 40);
					renderOnce(roof_window_3, graphicsHolder, light, position + 40);
					renderOnceFlipped(roof_window_3, graphicsHolder, light, position - 40);
					renderMirror(side_panel_2, graphicsHolder, light, position + (isEvenWindow ? -40 : 40));
					renderMirror(side_panel_1, graphicsHolder, light, position + (isEvenWindow ? 60 : -60));
					renderMirror(seat_curve, graphicsHolder, light, position + (isEvenWindow ? 20 : -20));
					if (isEvenWindow) {
						renderOnce(seat_1, graphicsHolder, light, position);
						renderOnceFlipped(seat_2, graphicsHolder, light, position);
						renderOnce(window_handrails_2, graphicsHolder, light, position);
					} else {
						renderOnceFlipped(seat_1, graphicsHolder, light, position);
						renderOnce(seat_2, graphicsHolder, light, position);
						renderOnce(window_handrails_1, graphicsHolder, light, position);
					}
				}
				break;
			case INTERIOR_TRANSLUCENT:
				renderMirror(side_panel_translucent, graphicsHolder, light, position + (isEvenWindow ? 60 : -60));
				renderMirror(statue_box_translucent, graphicsHolder, light, position + (isEvenWindow ? 20 : -20));
				break;
			case EXTERIOR:
				renderMirror(roof_exterior, graphicsHolder, light, position);
				renderMirror(roof_exterior, graphicsHolder, light, position - 40);
				renderMirror(roof_exterior, graphicsHolder, light, position + 40);
				renderMirror(window_exterior, graphicsHolder, light, position);
				break;
		}
	}

	@Override
	protected void renderDoorPositions(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
		final boolean middleDoor = isIndex(getDoorPositions().length / 2, position, getDoorPositions());
		final boolean doorOpen = doorLeftZ > 0 || doorRightZ > 0;
		final boolean notLastDoor = !isIndex(0, position, getDoorPositions()) && !isIndex(-1, position, getDoorPositions());

		switch (renderStage) {
			case LIGHT:
				if (notLastDoor) {
					renderMirror(roof_light, graphicsHolder, light, position);
				}
				if (middleDoor && doorOpen) {
					renderMirror(door_light_on, graphicsHolder, light, position - 30);
				}
				break;
			case INTERIOR:
				door_left.setOffset(0, 0, doorRightZ);
				door_right.setOffset(0, 0, -doorRightZ);
				renderOnce(door, graphicsHolder, light, position);
				door_left.setOffset(0, 0, doorLeftZ);
				door_right.setOffset(0, 0, -doorLeftZ);
				renderOnceFlipped(door, graphicsHolder, light, position);

				if (renderDetails) {
					if (notLastDoor) {
						renderOnce(roof_door, graphicsHolder, light, position);
						renderOnceFlipped(roof_door, graphicsHolder, light, position);
						renderOnce(door_handrail_1, graphicsHolder, light, position);
					} else {
						renderOnce(door_handrail_2, graphicsHolder, light, position);
					}
				}
				break;
			case EXTERIOR:
				door_left_exterior.setOffset(0, 0, doorRightZ);
				door_right_exterior.setOffset(0, 0, -doorRightZ);
				renderOnce(door_exterior, graphicsHolder, light, position);
				door_left_exterior.setOffset(0, 0, doorLeftZ);
				door_right_exterior.setOffset(0, 0, -doorLeftZ);
				renderOnceFlipped(door_exterior, graphicsHolder, light, position);
				renderMirror(roof_exterior, graphicsHolder, light, position);
				if (middleDoor && renderDetails) {
					renderMirror(door_light, graphicsHolder, light, position - 30);
					if (!doorOpen) {
						renderMirror(door_light_off, graphicsHolder, light, position - 30);
					}
				}
				break;
		}
	}

	@Override
	protected void renderHeadPosition1(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean useHeadlights) {
		switch (renderStage) {
			case LIGHT:
				renderOnce(roof_end_light, graphicsHolder, light, position);
				break;
			case ALWAYS_ON_LIGHT:
				renderOnce(useHeadlights ? headlights : tail_lights, graphicsHolder, light, position);
				break;
			case INTERIOR:
				renderOnce(head, graphicsHolder, light, position);
				if (renderDetails) {
					renderOnce(roof_end, graphicsHolder, light, position);
				}
				break;
			case EXTERIOR:
				renderOnce(head_exterior, graphicsHolder, light, position);
				renderOnce(roof_head_exterior, graphicsHolder, light, position);
				break;
		}
	}

	@Override
	protected void renderHeadPosition2(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean useHeadlights) {
		switch (renderStage) {
			case LIGHT:
				renderOnceFlipped(roof_end_light, graphicsHolder, light, position);
				break;
			case ALWAYS_ON_LIGHT:
				renderOnceFlipped(useHeadlights ? headlights : tail_lights, graphicsHolder, light, position);
				break;
			case INTERIOR:
				renderOnceFlipped(head, graphicsHolder, light, position);
				if (renderDetails) {
					renderOnceFlipped(roof_end, graphicsHolder, light, position);
				}
				break;
			case EXTERIOR:
				renderOnceFlipped(head_exterior, graphicsHolder, light, position);
				renderOnceFlipped(roof_head_exterior, graphicsHolder, light, position);
				break;
		}
	}

	@Override
	protected void renderEndPosition1(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
		switch (renderStage) {
			case LIGHT:
				renderOnce(roof_end_light, graphicsHolder, light, position);
				break;
			case INTERIOR:
				renderOnce(end, graphicsHolder, light, position);
				if (renderDetails) {
					renderOnce(roof_end, graphicsHolder, light, position);
				}
				break;
			case EXTERIOR:
				renderOnce(end_exterior, graphicsHolder, light, position);
				renderOnce(roof_end_exterior, graphicsHolder, light, position);
				break;
		}
	}

	@Override
	protected void renderEndPosition2(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
		switch (renderStage) {
			case LIGHT:
				renderOnceFlipped(roof_end_light, graphicsHolder, light, position);
				break;
			case INTERIOR:
				renderOnceFlipped(end, graphicsHolder, light, position);
				if (renderDetails) {
					renderOnceFlipped(roof_end, graphicsHolder, light, position);
				}
				break;
			case EXTERIOR:
				renderOnceFlipped(end_exterior, graphicsHolder, light, position);
				renderOnceFlipped(roof_end_exterior, graphicsHolder, light, position);
				break;
		}
	}

	@Override
	protected ModelDoorOverlay getModelDoorOverlay() {
		return MODEL_DOOR_OVERLAY;
	}

	@Override
	protected ModelDoorOverlayTopBase getModelDoorOverlayTop() {
		return null;
	}

	@Override
	protected int[] getWindowPositions() {
		return new int[]{-80, 80};
	}

	@Override
	protected int[] getDoorPositions() {
		return new int[]{-160, 0, 160};
	}

	@Override
	protected int[] getEndPositions() {
		return new int[]{-184, 184};
	}

	@Override
	protected int getDoorMax() {
		return DOOR_MAX;
	}

	private boolean isEvenWindow(int position) {
		return isIndex(1, position, getWindowPositions()) || isIndex(3, position, getWindowPositions());
	}
}