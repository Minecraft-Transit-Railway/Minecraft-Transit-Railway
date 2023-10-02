package org.mtr.mod.model;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.core.data.InterchangeColorsForStationName;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.ModelPartExtension;
import org.mtr.mod.client.DoorAnimationType;
import org.mtr.mod.client.ScrollingText;
import org.mtr.mod.render.StoredMatrixTransformations;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ModelR211 extends ModelSimpleTrainBase<ModelR211> {

	private final ModelPartExtension window_exterior;
	private final ModelPartExtension upper_wall_r1;
	private final ModelPartExtension window_handrails;
	private final ModelPartExtension headrail_right;
	private final ModelPartExtension handrail_turn_1_r1;
	private final ModelPartExtension handrail_middle_4_r1;
	private final ModelPartExtension handrail_middle_3_r1;
	private final ModelPartExtension handrail_middle_2_r1;
	private final ModelPartExtension handrail_turn_1_r2;
	private final ModelPartExtension headrail_left;
	private final ModelPartExtension handrail_turn_1_r3;
	private final ModelPartExtension handrail_middle_4_r2;
	private final ModelPartExtension handrail_middle_3_r2;
	private final ModelPartExtension handrail_middle_2_r2;
	private final ModelPartExtension handrail_turn_1_r4;
	private final ModelPartExtension handrail_mid;
	private final ModelPartExtension handrail_middle_4_r3;
	private final ModelPartExtension handrail_middle_3_r3;
	private final ModelPartExtension handrail_middle_2_r3;
	private final ModelPartExtension handrail_turn_1_r5;
	private final ModelPartExtension headrail_up;
	private final ModelPartExtension handrail_up_3_r1;
	private final ModelPartExtension handrail_top_1_r1;
	private final ModelPartExtension handrail_top_2_r1;
	private final ModelPartExtension seat;
	private final ModelPartExtension seat_bottom_r1;
	private final ModelPartExtension seat_back_3_r1;
	private final ModelPartExtension window_exterior_end;
	private final ModelPartExtension upper_wall_2_r1;
	private final ModelPartExtension upper_wall_1_r1;
	private final ModelPartExtension window;
	private final ModelPartExtension wall_1_r1;
	private final ModelPartExtension side_panel;
	private final ModelPartExtension end;
	private final ModelPartExtension upper_wall_2_r2;
	private final ModelPartExtension upper_wall_1_r2;
	private final ModelPartExtension end_exterior;
	private final ModelPartExtension upper_wall_2_r3;
	private final ModelPartExtension upper_wall_1_r3;
	private final ModelPartExtension end_bottom_out;
	private final ModelPartExtension buttom_panel_right_2_r1;
	private final ModelPartExtension buttom_panel_left_2_r1;
	private final ModelPartExtension end_back;
	private final ModelPartExtension front_right_panel_4_r1;
	private final ModelPartExtension front_right_panel_3_r1;
	private final ModelPartExtension roof_end_exterior;
	private final ModelPartExtension outer_roof_6_r1;
	private final ModelPartExtension outer_roof_5_r1;
	private final ModelPartExtension outer_roof_4_r1;
	private final ModelPartExtension outer_roof_3_r1;
	private final ModelPartExtension outer_roof_2_r1;
	private final ModelPartExtension outer_roof_5_r2;
	private final ModelPartExtension outer_roof_4_r2;
	private final ModelPartExtension outer_roof_3_r2;
	private final ModelPartExtension outer_roof_2_r2;
	private final ModelPartExtension outer_roof_1_r1;
	private final ModelPartExtension end_gangway;
	private final ModelPartExtension end_gangway_exterior;
	private final ModelPartExtension upper_wall_3_r1;
	private final ModelPartExtension upper_wall_2_r4;
	private final ModelPartExtension roof_end_gangway_exterior;
	private final ModelPartExtension outer_roof_7_r1;
	private final ModelPartExtension outer_roof_6_r2;
	private final ModelPartExtension outer_roof_5_r3;
	private final ModelPartExtension outer_roof_4_r3;
	private final ModelPartExtension outer_roof_3_r3;
	private final ModelPartExtension outer_roof_6_r3;
	private final ModelPartExtension outer_roof_5_r4;
	private final ModelPartExtension outer_roof_4_r4;
	private final ModelPartExtension outer_roof_3_r4;
	private final ModelPartExtension outer_roof_2_r3;
	private final ModelPartExtension door;
	private final ModelPartExtension door_edge_4_r1;
	private final ModelPartExtension door_edge_3_r1;
	private final ModelPartExtension door_edge_2_r1;
	private final ModelPartExtension door_right;
	private final ModelPartExtension door_right_top_r1;
	private final ModelPartExtension door_left;
	private final ModelPartExtension door_side_top_r1;
	private final ModelPartExtension door_exterior;
	private final ModelPartExtension door_right_exterior;
	private final ModelPartExtension door_right_top_r2;
	private final ModelPartExtension door_left_exterior;
	private final ModelPartExtension door_left_top_r1;
	private final ModelPartExtension door_sides;
	private final ModelPartExtension door_side_top_2_r1;
	private final ModelPartExtension door_side_top_1_r1;
	private final ModelPartExtension door_end_exterior;
	private final ModelPartExtension door_right_exterior_end;
	private final ModelPartExtension door_right_top_r3;
	private final ModelPartExtension door_left_exterior_end;
	private final ModelPartExtension door_left_top_r2;
	private final ModelPartExtension door_sides_end;
	private final ModelPartExtension door_side_top_3_r1;
	private final ModelPartExtension door_side_top_2_r2;
	private final ModelPartExtension handrail_door;
	private final ModelPartExtension handrail_curve;
	private final ModelPartExtension handrail_curve_12_r1;
	private final ModelPartExtension handrail_curve_10_r1;
	private final ModelPartExtension handrail_curve_9_r1;
	private final ModelPartExtension handrail_curve_7_r1;
	private final ModelPartExtension handrail_curve_6_r1;
	private final ModelPartExtension handrail_curve_5_r1;
	private final ModelPartExtension handrail_curve_3_r1;
	private final ModelPartExtension handrail_curve_11_r1;
	private final ModelPartExtension handrail_curve_9_r2;
	private final ModelPartExtension handrail_curve_8_r1;
	private final ModelPartExtension handrail_curve_5_r2;
	private final ModelPartExtension handrail_curve_4_r1;
	private final ModelPartExtension handrail_curve_2_r1;
	private final ModelPartExtension head;
	private final ModelPartExtension upper_wall_2_r5;
	private final ModelPartExtension upper_wall_1_r4;
	private final ModelPartExtension head_exterior;
	private final ModelPartExtension upper_wall_2_r6;
	private final ModelPartExtension upper_wall_1_r5;
	private final ModelPartExtension bumper;
	private final ModelPartExtension bumper_2_r1;
	private final ModelPartExtension bumper_1_r1;
	private final ModelPartExtension head_back;
	private final ModelPartExtension front_right_panel_3_r2;
	private final ModelPartExtension front_right_panel_2_r1;
	private final ModelPartExtension roof_head_exterior;
	private final ModelPartExtension outer_roof_7_r2;
	private final ModelPartExtension outer_roof_6_r4;
	private final ModelPartExtension outer_roof_5_r5;
	private final ModelPartExtension outer_roof_4_r5;
	private final ModelPartExtension outer_roof_3_r5;
	private final ModelPartExtension outer_roof_6_r5;
	private final ModelPartExtension outer_roof_5_r6;
	private final ModelPartExtension outer_roof_4_r6;
	private final ModelPartExtension outer_roof_3_r6;
	private final ModelPartExtension outer_roof_2_r4;
	private final ModelPartExtension roof_exterior;
	private final ModelPartExtension outer_roof_5_r7;
	private final ModelPartExtension outer_roof_4_r7;
	private final ModelPartExtension outer_roof_3_r7;
	private final ModelPartExtension outer_roof_2_r5;
	private final ModelPartExtension outer_roof_1_r2;
	private final ModelPartExtension roof_door;
	private final ModelPartExtension inner_roof_4_r1;
	private final ModelPartExtension inner_roof_2_r1;
	private final ModelPartExtension roof_window;
	private final ModelPartExtension inner_roof_9_r1;
	private final ModelPartExtension inner_roof_6_r1;
	private final ModelPartExtension inner_roof_4_r2;
	private final ModelPartExtension inner_roof_3_r1;
	private final ModelPartExtension roof_end;
	private final ModelPartExtension side_1;
	private final ModelPartExtension inner_roof_5_r1;
	private final ModelPartExtension inner_roof_3_r2;
	private final ModelPartExtension side_2;
	private final ModelPartExtension inner_roof_7_r1;
	private final ModelPartExtension inner_roof_6_r2;
	private final ModelPartExtension mid_roof;
	private final ModelPartExtension roof_end_gangway;
	private final ModelPartExtension mid_roof_gangway;
	private final ModelPartExtension roof_light;
	private final ModelPartExtension destination_display_end_interior;
	private final ModelPartExtension display_6_r1;
	private final ModelPartExtension display_5_r1;
	private final ModelPartExtension display_4_r1;
	private final ModelPartExtension display_3_r1;
	private final ModelPartExtension roof_head;
	private final ModelPartExtension inner_roof_6_r3;
	private final ModelPartExtension inner_roof_3_r3;
	private final ModelPartExtension destination_display;
	private final ModelPartExtension display_7_r1;
	private final ModelPartExtension display_6_r2;
	private final ModelPartExtension headlights;
	private final ModelPartExtension headlights_2_r1;
	private final ModelPartExtension headlights_1_r1;
	private final ModelPartExtension tail_lights;
	private final ModelPartExtension tail_lights_2_r1;
	private final ModelPartExtension tail_lights_1_r1;
	private final ModelPartExtension door_light_interior_off;
	private final ModelPartExtension light_2_r1;
	private final ModelPartExtension door_light_interior_on;
	private final ModelPartExtension light_3_r1;

	protected final boolean openGangway;

	public ModelR211(boolean openGangway) {
		this(openGangway, DoorAnimationType.R211, true);
	}

	protected ModelR211(boolean openGangway, DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		super(360, 360, doorAnimationType, renderDoorOverlay);
		this.openGangway = openGangway;

		window_exterior = createModelPart();
		window_exterior.setPivot(0, 24, 0);
		window_exterior.setTextureUVOffset(0, 212).addCuboid(-21.5F, 0, -24, 1, 3, 48, 0, false);
		window_exterior.setTextureUVOffset(112, 0).addCuboid(-21.5F, -13, -27, 1, 13, 54, 0, false);

		upper_wall_r1 = createModelPart();
		upper_wall_r1.setPivot(-21.5F, -13, 0);
		window_exterior.addChild(upper_wall_r1);
		setRotationAngle(upper_wall_r1, 0, 0, 0.1047F);
		upper_wall_r1.setTextureUVOffset(56, 23).addCuboid(0, -23, -27, 1, 23, 54, 0, false);

		window_handrails = createModelPart();
		window_handrails.setPivot(0, 24, 0);


		headrail_right = createModelPart();
		headrail_right.setPivot(0, 0, 0);
		window_handrails.addChild(headrail_right);
		headrail_right.setTextureUVOffset(343, 0).addCuboid(-19.5F, -4.5F, 24, 8, 0, 0, 0.2F, false);

		handrail_turn_1_r1 = createModelPart();
		handrail_turn_1_r1.setPivot(-10.9858F, -31.518F, 23.5636F);
		headrail_right.addChild(handrail_turn_1_r1);
		setRotationAngle(handrail_turn_1_r1, -0.7854F, 0, -0.1745F);
		handrail_turn_1_r1.setTextureUVOffset(343, 0).addCuboid(0, 0, -0.5F, 0, 0, 1, 0.2F, false);

		handrail_middle_4_r1 = createModelPart();
		handrail_middle_4_r1.setPivot(-10.1951F, -27.0336F, 24);
		headrail_right.addChild(handrail_middle_4_r1);
		setRotationAngle(handrail_middle_4_r1, 0, 0, -0.1745F);
		handrail_middle_4_r1.setTextureUVOffset(359, 27).addCuboid(0, -4, 0, 0, 8, 0, 0.2F, false);

		handrail_middle_3_r1 = createModelPart();
		handrail_middle_3_r1.setPivot(-9.3199F, -21.2212F, 24);
		headrail_right.addChild(handrail_middle_3_r1);
		setRotationAngle(handrail_middle_3_r1, 0, 0, -0.0873F);
		handrail_middle_3_r1.setTextureUVOffset(342, 0).addCuboid(0, -1.5F, 0, 0, 3, 0, 0.2F, false);

		handrail_middle_2_r1 = createModelPart();
		handrail_middle_2_r1.setPivot(-9.7993F, -12.3899F, 24);
		headrail_right.addChild(handrail_middle_2_r1);
		setRotationAngle(handrail_middle_2_r1, 0, 0, 0.0873F);
		handrail_middle_2_r1.setTextureUVOffset(359, 30).addCuboid(0, -7, 0, 0, 14, 0, 0.2F, false);

		handrail_turn_1_r2 = createModelPart();
		handrail_turn_1_r2.setPivot(-11.3F, -4.3F, 0);
		headrail_right.addChild(handrail_turn_1_r2);
		setRotationAngle(handrail_turn_1_r2, 0, 0, 0.8727F);
		handrail_turn_1_r2.setTextureUVOffset(343, 0).addCuboid(-0.2F, -1.2F, 24, 0, 1, 0, 0.2F, false);

		headrail_left = createModelPart();
		headrail_left.setPivot(0, 0, 0);
		window_handrails.addChild(headrail_left);
		headrail_left.setTextureUVOffset(343, 0).addCuboid(-19.5F, -4.5F, -24, 8, 0, 0, 0.2F, false);

		handrail_turn_1_r3 = createModelPart();
		handrail_turn_1_r3.setPivot(-10.9858F, -31.518F, -23.5636F);
		headrail_left.addChild(handrail_turn_1_r3);
		setRotationAngle(handrail_turn_1_r3, 0.7854F, 0, -0.1745F);
		handrail_turn_1_r3.setTextureUVOffset(343, 0).addCuboid(0, 0, -0.5F, 0, 0, 1, 0.2F, false);

		handrail_middle_4_r2 = createModelPart();
		handrail_middle_4_r2.setPivot(-10.1951F, -27.0336F, -24);
		headrail_left.addChild(handrail_middle_4_r2);
		setRotationAngle(handrail_middle_4_r2, 0, 0, -0.1745F);
		handrail_middle_4_r2.setTextureUVOffset(359, 27).addCuboid(0, -4, 0, 0, 8, 0, 0.2F, false);

		handrail_middle_3_r2 = createModelPart();
		handrail_middle_3_r2.setPivot(-9.3199F, -21.2212F, -24);
		headrail_left.addChild(handrail_middle_3_r2);
		setRotationAngle(handrail_middle_3_r2, 0, 0, -0.0873F);
		handrail_middle_3_r2.setTextureUVOffset(342, 0).addCuboid(0, -1.5F, 0, 0, 3, 0, 0.2F, false);

		handrail_middle_2_r2 = createModelPart();
		handrail_middle_2_r2.setPivot(-9.7993F, -12.3899F, -24);
		headrail_left.addChild(handrail_middle_2_r2);
		setRotationAngle(handrail_middle_2_r2, 0, 0, 0.0873F);
		handrail_middle_2_r2.setTextureUVOffset(359, 30).addCuboid(0, -7, 0, 0, 14, 0, 0.2F, false);

		handrail_turn_1_r4 = createModelPart();
		handrail_turn_1_r4.setPivot(-11.3F, -4.3F, 0);
		headrail_left.addChild(handrail_turn_1_r4);
		setRotationAngle(handrail_turn_1_r4, 0, 0, 0.8727F);
		handrail_turn_1_r4.setTextureUVOffset(343, 0).addCuboid(-0.2F, -1.2F, -24, 0, 1, 0, 0.2F, false);

		handrail_mid = createModelPart();
		handrail_mid.setPivot(0, 0, 0);
		window_handrails.addChild(handrail_mid);
		handrail_mid.setTextureUVOffset(343, 0).addCuboid(-16.5F, -4.5F, -3.5F, 5, 0, 0, 0.2F, false);

		handrail_middle_4_r3 = createModelPart();
		handrail_middle_4_r3.setPivot(-10.5424F, -29.0032F, -3.5F);
		handrail_mid.addChild(handrail_middle_4_r3);
		setRotationAngle(handrail_middle_4_r3, 0, 0, -0.1745F);
		handrail_middle_4_r3.setTextureUVOffset(359, 27).addCuboid(0, -3, 0, 0, 9, 0, 0.2F, false);

		handrail_middle_3_r3 = createModelPart();
		handrail_middle_3_r3.setPivot(-9.3199F, -21.2212F, -3.5F);
		handrail_mid.addChild(handrail_middle_3_r3);
		setRotationAngle(handrail_middle_3_r3, 0, 0, -0.0873F);
		handrail_middle_3_r3.setTextureUVOffset(342, 0).addCuboid(0, -1.5F, 0, 0, 3, 0, 0.2F, false);

		handrail_middle_2_r3 = createModelPart();
		handrail_middle_2_r3.setPivot(-9.7993F, -12.3899F, -3.5F);
		handrail_mid.addChild(handrail_middle_2_r3);
		setRotationAngle(handrail_middle_2_r3, 0, 0, 0.0873F);
		handrail_middle_2_r3.setTextureUVOffset(359, 30).addCuboid(0, -7, 0, 0, 14, 0, 0.2F, false);

		handrail_turn_1_r5 = createModelPart();
		handrail_turn_1_r5.setPivot(-11.3F, -4.3F, 0);
		handrail_mid.addChild(handrail_turn_1_r5);
		setRotationAngle(handrail_turn_1_r5, 0, 0, 0.8727F);
		handrail_turn_1_r5.setTextureUVOffset(343, 0).addCuboid(-0.2F, -1.2F, -3.5F, 0, 1, 0, 0.2F, false);

		headrail_up = createModelPart();
		headrail_up.setPivot(0, 0, 0);
		window_handrails.addChild(headrail_up);
		headrail_up.setTextureUVOffset(343, 0).addCuboid(-10.6132F, -35.3491F, 0, 0, 1, 0, 0.2F, false);
		headrail_up.setTextureUVOffset(343, 0).addCuboid(-10.6132F, -35.3491F, -20, 0, 1, 0, 0.2F, false);
		headrail_up.setTextureUVOffset(343, 0).addCuboid(-10.6132F, -35.3491F, 20, 0, 1, 0, 0.2F, false);

		handrail_up_3_r1 = createModelPart();
		handrail_up_3_r1.setPivot(-10.8185F, -33.002F, 0);
		headrail_up.addChild(handrail_up_3_r1);
		setRotationAngle(handrail_up_3_r1, 0, 0, 0.1745F);
		handrail_up_3_r1.setTextureUVOffset(343, 0).addCuboid(0, -1, 20, 0, 2, 0, 0.2F, false);
		handrail_up_3_r1.setTextureUVOffset(343, 0).addCuboid(0, -1, -20, 0, 2, 0, 0.2F, false);
		handrail_up_3_r1.setTextureUVOffset(343, 0).addCuboid(0, -1, 0, 0, 2, 0, 0.2F, false);

		handrail_top_1_r1 = createModelPart();
		handrail_top_1_r1.setPivot(-11.0616F, -31.9478F, -11.5101F);
		headrail_up.addChild(handrail_top_1_r1);
		setRotationAngle(handrail_top_1_r1, -1.5708F, 0, -0.1745F);
		handrail_top_1_r1.setTextureUVOffset(359, 30).addCuboid(0, -11.5F, 0, 0, 23, 0, 0.2F, false);

		handrail_top_2_r1 = createModelPart();
		handrail_top_2_r1.setPivot(-11.0616F, -31.9478F, 11.5101F);
		headrail_up.addChild(handrail_top_2_r1);
		setRotationAngle(handrail_top_2_r1, -1.5708F, 0, -0.1745F);
		handrail_top_2_r1.setTextureUVOffset(359, 30).addCuboid(0, -11.5F, 0, 0, 23, 0, 0.2F, false);

		seat = createModelPart();
		seat.setPivot(0, 0, 0);
		window_handrails.addChild(seat);
		seat.setTextureUVOffset(122, 168).addCuboid(-19.9F, -10.75F, -24, 2, 5, 48, 0, false);
		seat.setTextureUVOffset(175, 174).addCuboid(-19.55F, -6, -23.5F, 3, 4, 47, 0, false);

		seat_bottom_r1 = createModelPart();
		seat_bottom_r1.setPivot(0, -1.75F, 2.5F);
		seat.addChild(seat_bottom_r1);
		setRotationAngle(seat_bottom_r1, 0, 0, -0.0873F);
		seat_bottom_r1.setTextureUVOffset(56, 167).addCuboid(-19.9F, -6, -26.5F, 9, 1, 48, 0, false);

		seat_back_3_r1 = createModelPart();
		seat_back_3_r1.setPivot(-17.9F, -10.75F, 2.5F);
		seat.addChild(seat_back_3_r1);
		setRotationAngle(seat_back_3_r1, 0, 0, -0.1309F);
		seat_back_3_r1.setTextureUVOffset(168, 0).addCuboid(-2, -5, -26.5F, 2, 5, 48, 0, false);

		window_exterior_end = createModelPart();
		window_exterior_end.setPivot(0, 24, 0);
		window_exterior_end.setTextureUVOffset(192, 53).addCuboid(-21.5F, 0, -24, 1, 3, 48, 0, false);
		window_exterior_end.setTextureUVOffset(192, 53).addCuboid(20.5F, 0, -24, 1, 3, 48, 0, true);
		window_exterior_end.setTextureUVOffset(82, 100).addCuboid(-21.5F, -13, -27, 1, 13, 54, 0, false);
		window_exterior_end.setTextureUVOffset(82, 100).addCuboid(20.5F, -13, -27, 1, 13, 54, 0, true);

		upper_wall_2_r1 = createModelPart();
		upper_wall_2_r1.setPivot(-9.3302F, -9.7596F, 0);
		window_exterior_end.addChild(upper_wall_2_r1);
		setRotationAngle(upper_wall_2_r1, 0, 0, -0.1047F);
		upper_wall_2_r1.setTextureUVOffset(0, 0).addCuboid(30, -23, -27, 1, 23, 54, 0, true);

		upper_wall_1_r1 = createModelPart();
		upper_wall_1_r1.setPivot(-21.5F, -13, 0);
		window_exterior_end.addChild(upper_wall_1_r1);
		setRotationAngle(upper_wall_1_r1, 0, 0, 0.1047F);
		upper_wall_1_r1.setTextureUVOffset(0, 0).addCuboid(0, -23, -27, 1, 23, 54, 0, false);

		window = createModelPart();
		window.setPivot(0, 24, 0);
		window.setTextureUVOffset(0, 100).addCuboid(-20, 0, -24, 20, 1, 48, 0, false);
		window.setTextureUVOffset(0, 149).addCuboid(-21.5F, -13, -25, 2, 13, 50, 0, false);

		wall_1_r1 = createModelPart();
		wall_1_r1.setPivot(-21.5F, -13, 0);
		window.addChild(wall_1_r1);
		setRotationAngle(wall_1_r1, 0, 0, 0.1047F);
		wall_1_r1.setTextureUVOffset(138, 67).addCuboid(0, -21, -25, 2, 21, 50, 0, false);

		side_panel = createModelPart();
		side_panel.setPivot(0, 24, 0);
		side_panel.setTextureUVOffset(0, 314).addCuboid(-20, -32, 0, 11, 29, 0, 0, false);

		end = createModelPart();
		end.setPivot(0, 24, 0);
		end.setTextureUVOffset(149, 225).addCuboid(-20.5F, 0, -9, 41, 1, 17, 0, false);
		end.setTextureUVOffset(285, 296).addCuboid(-7, -33, -9, 14, 33, 0, 0, false);
		end.setTextureUVOffset(114, 294).addCuboid(7, -33, -9, 13, 33, 1, 0, true);
		end.setTextureUVOffset(114, 294).addCuboid(-20, -33, -9, 13, 33, 1, 0, false);
		end.setTextureUVOffset(48, 40).addCuboid(19.5F, -13, 8, 2, 13, 1, 0, false);
		end.setTextureUVOffset(48, 40).addCuboid(-21.5F, -13, 8, 2, 13, 1, 0, true);
		end.setTextureUVOffset(122, 167).addCuboid(-20.5F, -34, -8, 3, 21, 16, 0, false);
		end.setTextureUVOffset(122, 167).addCuboid(17.5F, -34, -8, 3, 21, 16, 0, true);
		end.setTextureUVOffset(168, 0).addCuboid(-19.5F, -13, -8, 5, 13, 16, 0, false);
		end.setTextureUVOffset(168, 0).addCuboid(14.5F, -13, -8, 5, 13, 16, 0, true);

		upper_wall_2_r2 = createModelPart();
		upper_wall_2_r2.setPivot(-21.5F, -13, 0);
		end.addChild(upper_wall_2_r2);
		setRotationAngle(upper_wall_2_r2, 0, 0, 0.1047F);
		upper_wall_2_r2.setTextureUVOffset(14, 90).addCuboid(0, -21, 8, 2, 21, 1, 0, true);

		upper_wall_1_r2 = createModelPart();
		upper_wall_1_r2.setPivot(21.5F, -13, 0);
		end.addChild(upper_wall_1_r2);
		setRotationAngle(upper_wall_1_r2, 0, 0, -0.1047F);
		upper_wall_1_r2.setTextureUVOffset(14, 90).addCuboid(-2, -21, 8, 2, 21, 1, 0, false);

		end_exterior = createModelPart();
		end_exterior.setPivot(0, 24, 0);
		end_exterior.setTextureUVOffset(147, 81).addCuboid(20.5F, 0, -11, 1, 3, 19, 0, false);
		end_exterior.setTextureUVOffset(0, 18).addCuboid(19.5F, -13, -11, 2, 13, 22, 0, false);
		end_exterior.setTextureUVOffset(147, 81).addCuboid(-21.5F, 0, -11, 1, 3, 19, 0, true);
		end_exterior.setTextureUVOffset(0, 18).addCuboid(-21.5F, -13, -11, 2, 13, 22, 0, true);

		upper_wall_2_r3 = createModelPart();
		upper_wall_2_r3.setPivot(-21.5F, -13, 0);
		end_exterior.addChild(upper_wall_2_r3);
		setRotationAngle(upper_wall_2_r3, 0, 0, 0.1047F);
		upper_wall_2_r3.setTextureUVOffset(0, 95).addCuboid(0, -23, -11, 2, 23, 22, 0, true);

		upper_wall_1_r3 = createModelPart();
		upper_wall_1_r3.setPivot(21.5F, -13, 0);
		end_exterior.addChild(upper_wall_1_r3);
		setRotationAngle(upper_wall_1_r3, 0, 0, -0.1047F);
		upper_wall_1_r3.setTextureUVOffset(0, 95).addCuboid(-2, -23, -11, 2, 23, 22, 0, false);

		end_bottom_out = createModelPart();
		end_bottom_out.setPivot(0, 0, 0);
		end_exterior.addChild(end_bottom_out);
		end_bottom_out.setTextureUVOffset(54, 191).addCuboid(-0.057F, 0, -14.3646F, 9, 3, 5, 0, false);
		end_bottom_out.setTextureUVOffset(54, 191).addCuboid(-8.943F, 0, -14.3646F, 9, 3, 5, 0, true);

		buttom_panel_right_2_r1 = createModelPart();
		buttom_panel_right_2_r1.setPivot(-21.5F, 0, -11);
		end_bottom_out.addChild(buttom_panel_right_2_r1);
		setRotationAngle(buttom_panel_right_2_r1, 0, 0.2618F, 0);
		buttom_panel_right_2_r1.setTextureUVOffset(0, 140).addCuboid(0, 0, 0, 13, 3, 3, 0, true);

		buttom_panel_left_2_r1 = createModelPart();
		buttom_panel_left_2_r1.setPivot(21.5F, 0, -11);
		end_bottom_out.addChild(buttom_panel_left_2_r1);
		setRotationAngle(buttom_panel_left_2_r1, 0, -0.2618F, 0);
		buttom_panel_left_2_r1.setTextureUVOffset(0, 140).addCuboid(-13, 0, 0, 13, 3, 3, 0, false);

		end_back = createModelPart();
		end_back.setPivot(0, 0, 0);
		end_exterior.addChild(end_back);
		end_back.setTextureUVOffset(158, 0).addCuboid(-8, -33, -13, 1, 33, 3, 0, false);
		end_back.setTextureUVOffset(158, 0).addCuboid(7, -33, -13, 1, 33, 3, 0, true);
		end_back.setTextureUVOffset(138, 103).addCuboid(-8, -42, -13, 16, 9, 4, 0, false);
		end_back.setTextureUVOffset(86, 291).addCuboid(-7, -33, -11, 14, 33, 0, 0, false);

		front_right_panel_4_r1 = createModelPart();
		front_right_panel_4_r1.setPivot(8, -42, -13);
		end_back.addChild(front_right_panel_4_r1);
		setRotationAngle(front_right_panel_4_r1, 0, -0.192F, 0);
		front_right_panel_4_r1.setTextureUVOffset(58, 291).addCuboid(0, 0, 0, 14, 42, 0, 0, true);

		front_right_panel_3_r1 = createModelPart();
		front_right_panel_3_r1.setPivot(-8, -42, -13);
		end_back.addChild(front_right_panel_3_r1);
		setRotationAngle(front_right_panel_3_r1, 0, 0.192F, 0);
		front_right_panel_3_r1.setTextureUVOffset(58, 291).addCuboid(-14, 0, 0, 14, 42, 0, 0, false);

		roof_end_exterior = createModelPart();
		roof_end_exterior.setPivot(0, 0, 0);
		end_exterior.addChild(roof_end_exterior);
		roof_end_exterior.setTextureUVOffset(105, 100).addCuboid(-4, -41.375F, -15, 4, 0, 23, 0, false);
		roof_end_exterior.setTextureUVOffset(105, 100).addCuboid(0, -41.375F, -15, 4, 0, 23, 0, true);

		outer_roof_6_r1 = createModelPart();
		outer_roof_6_r1.setPivot(4, -41.375F, 0);
		roof_end_exterior.addChild(outer_roof_6_r1);
		setRotationAngle(outer_roof_6_r1, 0, 0, 0.0873F);
		outer_roof_6_r1.setTextureUVOffset(119, 0).addCuboid(0, 0, -15, 6, 0, 23, 0, true);

		outer_roof_5_r1 = createModelPart();
		outer_roof_5_r1.setPivot(12.392F, -40.205F, 0);
		roof_end_exterior.addChild(outer_roof_5_r1);
		setRotationAngle(outer_roof_5_r1, 0, 0, 0.2618F);
		outer_roof_5_r1.setTextureUVOffset(23, 77).addCuboid(-2.5F, 0, -15, 5, 0, 23, 0, true);

		outer_roof_4_r1 = createModelPart();
		outer_roof_4_r1.setPivot(16.2163F, -39.0449F, 0);
		roof_end_exterior.addChild(outer_roof_4_r1);
		setRotationAngle(outer_roof_4_r1, 0, 0, 0.3491F);
		outer_roof_4_r1.setTextureUVOffset(105, 123).addCuboid(-1.5F, 0, -15, 3, 0, 23, 0, true);

		outer_roof_3_r1 = createModelPart();
		outer_roof_3_r1.setPivot(18.2687F, -37.7659F, 0);
		roof_end_exterior.addChild(outer_roof_3_r1);
		setRotationAngle(outer_roof_3_r1, 0, 0, 0.8727F);
		outer_roof_3_r1.setTextureUVOffset(131, 0).addCuboid(-1, 0, -15, 2, 0, 23, 0, true);

		outer_roof_2_r1 = createModelPart();
		outer_roof_2_r1.setPivot(18.6114F, -35.9228F, 0);
		roof_end_exterior.addChild(outer_roof_2_r1);
		setRotationAngle(outer_roof_2_r1, 0, 0, 1.3788F);
		outer_roof_2_r1.setTextureUVOffset(166, 285).addCuboid(-1, -0.5F, -15, 2, 1, 23, 0, true);

		outer_roof_5_r2 = createModelPart();
		outer_roof_5_r2.setPivot(-4, -41.375F, 0);
		roof_end_exterior.addChild(outer_roof_5_r2);
		setRotationAngle(outer_roof_5_r2, 0, 0, -0.0873F);
		outer_roof_5_r2.setTextureUVOffset(119, 0).addCuboid(-6, 0, -15, 6, 0, 23, 0, false);

		outer_roof_4_r2 = createModelPart();
		outer_roof_4_r2.setPivot(-12.392F, -40.205F, 0);
		roof_end_exterior.addChild(outer_roof_4_r2);
		setRotationAngle(outer_roof_4_r2, 0, 0, -0.2618F);
		outer_roof_4_r2.setTextureUVOffset(23, 77).addCuboid(-2.5F, 0, -15, 5, 0, 23, 0, false);

		outer_roof_3_r2 = createModelPart();
		outer_roof_3_r2.setPivot(-16.2163F, -39.0449F, 0);
		roof_end_exterior.addChild(outer_roof_3_r2);
		setRotationAngle(outer_roof_3_r2, 0, 0, -0.3491F);
		outer_roof_3_r2.setTextureUVOffset(105, 123).addCuboid(-1.5F, 0, -15, 3, 0, 23, 0, false);

		outer_roof_2_r2 = createModelPart();
		outer_roof_2_r2.setPivot(-18.2687F, -37.7659F, 0);
		roof_end_exterior.addChild(outer_roof_2_r2);
		setRotationAngle(outer_roof_2_r2, 0, 0, -0.8727F);
		outer_roof_2_r2.setTextureUVOffset(131, 0).addCuboid(-1, 0, -15, 2, 0, 23, 0, false);

		outer_roof_1_r1 = createModelPart();
		outer_roof_1_r1.setPivot(-18.6114F, -35.9228F, 0);
		roof_end_exterior.addChild(outer_roof_1_r1);
		setRotationAngle(outer_roof_1_r1, 0, 0, -1.3788F);
		outer_roof_1_r1.setTextureUVOffset(166, 285).addCuboid(-1, -0.5F, -15, 2, 1, 23, 0, false);

		end_gangway = createModelPart();
		end_gangway.setPivot(0, 24, 0);
		end_gangway.setTextureUVOffset(50, 221).addCuboid(-20.5F, 0, -9, 41, 1, 17, 0, false);
		end_gangway.setTextureUVOffset(0, 263).addCuboid(8.5F, -32.875F, -9, 11, 33, 18, 0, false);
		end_gangway.setTextureUVOffset(131, 243).addCuboid(-19.5F, -32.875F, -9, 11, 33, 18, 0, false);

		end_gangway_exterior = createModelPart();
		end_gangway_exterior.setPivot(0, 24, 0);
		end_gangway_exterior.setTextureUVOffset(303, 211).addCuboid(20.5F, 0, -9, 1, 3, 17, 0, false);
		end_gangway_exterior.setTextureUVOffset(54, 149).addCuboid(19.5F, -13, -9, 2, 13, 20, 0, false);
		end_gangway_exterior.setTextureUVOffset(303, 211).addCuboid(-21.5F, 0, -9, 1, 3, 17, 0, true);
		end_gangway_exterior.setTextureUVOffset(54, 149).addCuboid(-21.5F, -13, -9, 2, 13, 20, 0, true);
		end_gangway_exterior.setTextureUVOffset(231, 301).addCuboid(8.5F, -33, -9, 12, 33, 0, 0, false);
		end_gangway_exterior.setTextureUVOffset(231, 301).addCuboid(-20.5F, -33, -9, 12, 33, 0, 0, true);
		end_gangway_exterior.setTextureUVOffset(192, 104).addCuboid(-20, -41.875F, -9, 20, 9, 0, 0, false);
		end_gangway_exterior.setTextureUVOffset(192, 104).addCuboid(0, -41.875F, -9, 20, 9, 0, 0, true);

		upper_wall_3_r1 = createModelPart();
		upper_wall_3_r1.setPivot(-21.5F, -13, 0);
		end_gangway_exterior.addChild(upper_wall_3_r1);
		setRotationAngle(upper_wall_3_r1, 0, 0, 0.1047F);
		upper_wall_3_r1.setTextureUVOffset(0, 149).addCuboid(0, -23, -9, 2, 23, 20, 0, true);

		upper_wall_2_r4 = createModelPart();
		upper_wall_2_r4.setPivot(21.5F, -13, 0);
		end_gangway_exterior.addChild(upper_wall_2_r4);
		setRotationAngle(upper_wall_2_r4, 0, 0, -0.1047F);
		upper_wall_2_r4.setTextureUVOffset(0, 149).addCuboid(-2, -23, -9, 2, 23, 20, 0, false);

		roof_end_gangway_exterior = createModelPart();
		roof_end_gangway_exterior.setPivot(0, 0, 0);
		end_gangway_exterior.addChild(roof_end_gangway_exterior);
		roof_end_gangway_exterior.setTextureUVOffset(50, 239).addCuboid(-4, -41.375F, -9, 4, 1, 17, 0, false);
		roof_end_gangway_exterior.setTextureUVOffset(50, 239).addCuboid(0, -41.375F, -9, 4, 1, 17, 0, true);

		outer_roof_7_r1 = createModelPart();
		outer_roof_7_r1.setPivot(4, -41.375F, 0);
		roof_end_gangway_exterior.addChild(outer_roof_7_r1);
		setRotationAngle(outer_roof_7_r1, 0, 0, 0.0873F);
		outer_roof_7_r1.setTextureUVOffset(88, 129).addCuboid(0, 0, -9, 6, 1, 17, 0, true);

		outer_roof_6_r2 = createModelPart();
		outer_roof_6_r2.setPivot(12.392F, -40.205F, 0);
		roof_end_gangway_exterior.addChild(outer_roof_6_r2);
		setRotationAngle(outer_roof_6_r2, 0, 0, 0.2618F);
		outer_roof_6_r2.setTextureUVOffset(192, 67).addCuboid(-2.5F, 0, -9, 5, 1, 17, 0, true);

		outer_roof_5_r3 = createModelPart();
		outer_roof_5_r3.setPivot(16.2163F, -39.0449F, 0);
		roof_end_gangway_exterior.addChild(outer_roof_5_r3);
		setRotationAngle(outer_roof_5_r3, 0, 0, 0.3491F);
		outer_roof_5_r3.setTextureUVOffset(294, 0).addCuboid(-1.5F, 0, -9, 3, 1, 17, 0, true);

		outer_roof_4_r3 = createModelPart();
		outer_roof_4_r3.setPivot(18.2687F, -37.7659F, 0);
		roof_end_gangway_exterior.addChild(outer_roof_4_r3);
		setRotationAngle(outer_roof_4_r3, 0, 0, 0.8727F);
		outer_roof_4_r3.setTextureUVOffset(301, 130).addCuboid(-1, 0, -9, 2, 1, 17, 0, true);

		outer_roof_3_r3 = createModelPart();
		outer_roof_3_r3.setPivot(18.6114F, -35.9228F, 0);
		roof_end_gangway_exterior.addChild(outer_roof_3_r3);
		setRotationAngle(outer_roof_3_r3, 0, 0, 1.3788F);
		outer_roof_3_r3.setTextureUVOffset(303, 193).addCuboid(-1, -0.5F, -9, 2, 1, 17, 0, true);

		outer_roof_6_r3 = createModelPart();
		outer_roof_6_r3.setPivot(-4, -41.375F, 0);
		roof_end_gangway_exterior.addChild(outer_roof_6_r3);
		setRotationAngle(outer_roof_6_r3, 0, 0, -0.0873F);
		outer_roof_6_r3.setTextureUVOffset(88, 129).addCuboid(-6, 0, -9, 6, 1, 17, 0, false);

		outer_roof_5_r4 = createModelPart();
		outer_roof_5_r4.setPivot(-12.392F, -40.205F, 0);
		roof_end_gangway_exterior.addChild(outer_roof_5_r4);
		setRotationAngle(outer_roof_5_r4, 0, 0, -0.2618F);
		outer_roof_5_r4.setTextureUVOffset(192, 67).addCuboid(-2.5F, 0, -9, 5, 1, 17, 0, false);

		outer_roof_4_r4 = createModelPart();
		outer_roof_4_r4.setPivot(-16.2163F, -39.0449F, 0);
		roof_end_gangway_exterior.addChild(outer_roof_4_r4);
		setRotationAngle(outer_roof_4_r4, 0, 0, -0.3491F);
		outer_roof_4_r4.setTextureUVOffset(294, 0).addCuboid(-1.5F, 0, -9, 3, 1, 17, 0, false);

		outer_roof_3_r4 = createModelPart();
		outer_roof_3_r4.setPivot(-18.2687F, -37.7659F, 0);
		roof_end_gangway_exterior.addChild(outer_roof_3_r4);
		setRotationAngle(outer_roof_3_r4, 0, 0, -0.8727F);
		outer_roof_3_r4.setTextureUVOffset(301, 130).addCuboid(-1, 0, -9, 2, 1, 17, 0, false);

		outer_roof_2_r3 = createModelPart();
		outer_roof_2_r3.setPivot(-18.6114F, -35.9228F, 0);
		roof_end_gangway_exterior.addChild(outer_roof_2_r3);
		setRotationAngle(outer_roof_2_r3, 0, 0, -1.3788F);
		outer_roof_2_r3.setTextureUVOffset(303, 193).addCuboid(-1, -0.5F, -9, 2, 1, 17, 0, false);

		door = createModelPart();
		door.setPivot(0, 24, 0);
		door.setTextureUVOffset(220, 0).addCuboid(-21, 0, -16, 21, 1, 32, 0, false);

		door_edge_4_r1 = createModelPart();
		door_edge_4_r1.setPivot(-21.5F, -13, 15);
		door.addChild(door_edge_4_r1);
		setRotationAngle(door_edge_4_r1, 0, 1.5708F, 0.1047F);
		door_edge_4_r1.setTextureUVOffset(0, 90).addCuboid(0, -21, 0.5F, 2, 21, 2, 0, false);
		door_edge_4_r1.setTextureUVOffset(0, 90).addCuboid(28, -21, 0.5F, 2, 21, 2, 0, true);

		door_edge_3_r1 = createModelPart();
		door_edge_3_r1.setPivot(-20, -6.5F, 14);
		door.addChild(door_edge_3_r1);
		setRotationAngle(door_edge_3_r1, 0, 1.5708F, 0);
		door_edge_3_r1.setTextureUVOffset(96, 126).addCuboid(-1, -6.5F, -1, 2, 13, 2, 0, false);

		door_edge_2_r1 = createModelPart();
		door_edge_2_r1.setPivot(-20, -6.5F, -14);
		door.addChild(door_edge_2_r1);
		setRotationAngle(door_edge_2_r1, 0, 1.5708F, 0);
		door_edge_2_r1.setTextureUVOffset(96, 126).addCuboid(-1, -6.5F, -1, 2, 13, 2, 0, true);

		door_right = createModelPart();
		door_right.setPivot(0, 0, 0);
		door.addChild(door_right);
		door_right.setTextureUVOffset(269, 212).addCuboid(-21, -13, 0, 1, 13, 13, 0, false);

		door_right_top_r1 = createModelPart();
		door_right_top_r1.setPivot(-21, -13, 0);
		door_right.addChild(door_right_top_r1);
		setRotationAngle(door_right_top_r1, 0, 0, 0.1047F);
		door_right_top_r1.setTextureUVOffset(203, 296).addCuboid(0, -20, 0, 1, 20, 13, 0, false);

		door_left = createModelPart();
		door_left.setPivot(0, 0, 0);
		door.addChild(door_left);
		door_left.setTextureUVOffset(220, 0).addCuboid(-21, -13, -13, 1, 13, 13, 0, false);

		door_side_top_r1 = createModelPart();
		door_side_top_r1.setPivot(-21, -13, -2);
		door_left.addChild(door_side_top_r1);
		setRotationAngle(door_side_top_r1, 0, 0, 0.1047F);
		door_side_top_r1.setTextureUVOffset(142, 296).addCuboid(0, -20, -11, 1, 20, 13, 0, false);

		door_exterior = createModelPart();
		door_exterior.setPivot(0, 24, 0);


		door_right_exterior = createModelPart();
		door_right_exterior.setPivot(0, 0, 0);
		door_exterior.addChild(door_right_exterior);
		door_right_exterior.setTextureUVOffset(164, 125).addCuboid(-21, -13, 0, 0, 13, 13, 0, false);

		door_right_top_r2 = createModelPart();
		door_right_top_r2.setPivot(-21, -13, 0);
		door_right_exterior.addChild(door_right_top_r2);
		setRotationAngle(door_right_top_r2, 0, 0, 0.1047F);
		door_right_top_r2.setTextureUVOffset(222, 230).addCuboid(0, -20, 0, 0, 20, 13, 0, false);

		door_left_exterior = createModelPart();
		door_left_exterior.setPivot(0, 0, 0);
		door_exterior.addChild(door_left_exterior);
		door_left_exterior.setTextureUVOffset(144, 155).addCuboid(-21, -13, -13, 0, 13, 13, 0, false);

		door_left_top_r1 = createModelPart();
		door_left_top_r1.setPivot(-21, -13, 0);
		door_left_exterior.addChild(door_left_top_r1);
		setRotationAngle(door_left_top_r1, 0, 0, 0.1047F);
		door_left_top_r1.setTextureUVOffset(189, 230).addCuboid(0, -20, -13, 0, 20, 13, 0, false);

		door_sides = createModelPart();
		door_sides.setPivot(0, 0, 0);
		door_exterior.addChild(door_sides);
		door_sides.setTextureUVOffset(269, 210).addCuboid(-21.5F, 0, -16, 1, 3, 32, 0, false);
		door_sides.setTextureUVOffset(282, 266).addCuboid(-22, 0, -13, 1, 1, 26, 0, false);

		door_side_top_2_r1 = createModelPart();
		door_side_top_2_r1.setPivot(-18.9095F, -32.8894F, 0);
		door_sides.addChild(door_side_top_2_r1);
		setRotationAngle(door_side_top_2_r1, 0, 0, 0.1047F);
		door_side_top_2_r1.setTextureUVOffset(98, 100).addCuboid(-1, 0, -13, 2, 0, 26, 0, false);

		door_side_top_1_r1 = createModelPart();
		door_side_top_1_r1.setPivot(-21.5F, -13, 0);
		door_sides.addChild(door_side_top_1_r1);
		setRotationAngle(door_side_top_1_r1, 0, 0, 0.1047F);
		door_side_top_1_r1.setTextureUVOffset(112, 17).addCuboid(0, -23, -13, 0, 3, 26, 0, false);

		door_end_exterior = createModelPart();
		door_end_exterior.setPivot(0, 24, 0);


		door_right_exterior_end = createModelPart();
		door_right_exterior_end.setPivot(0, 0, 0);
		door_end_exterior.addChild(door_right_exterior_end);
		door_right_exterior_end.setTextureUVOffset(138, 125).addCuboid(-21, -13, 0, 0, 13, 13, 0, false);

		door_right_top_r3 = createModelPart();
		door_right_top_r3.setPivot(-21, -13, 0);
		door_right_exterior_end.addChild(door_right_top_r3);
		setRotationAngle(door_right_top_r3, 0, 0, 0.1047F);
		door_right_top_r3.setTextureUVOffset(102, 226).addCuboid(0, -20, 0, 0, 20, 13, 0, false);

		door_left_exterior_end = createModelPart();
		door_left_exterior_end.setPivot(0, 0, 0);
		door_end_exterior.addChild(door_left_exterior_end);
		door_left_exterior_end.setTextureUVOffset(0, 64).addCuboid(-21, -13, -13, 0, 13, 13, 0, false);

		door_left_top_r2 = createModelPart();
		door_left_top_r2.setPivot(-21, -13, 0);
		door_left_exterior_end.addChild(door_left_top_r2);
		setRotationAngle(door_left_top_r2, 0, 0, 0.1047F);
		door_left_top_r2.setTextureUVOffset(24, 136).addCuboid(0, -20, -13, 0, 20, 13, 0, false);

		door_sides_end = createModelPart();
		door_sides_end.setPivot(0, 0, 0);
		door_end_exterior.addChild(door_sides_end);
		door_sides_end.setTextureUVOffset(219, 266).addCuboid(-21.5F, 0, -16, 1, 3, 32, 0, false);
		door_sides_end.setTextureUVOffset(282, 266).addCuboid(-22, 0, -13, 1, 1, 26, 0, false);

		door_side_top_3_r1 = createModelPart();
		door_side_top_3_r1.setPivot(-18.9094F, -32.8894F, 0);
		door_sides_end.addChild(door_side_top_3_r1);
		setRotationAngle(door_side_top_3_r1, 0, 0, 0.1047F);
		door_side_top_3_r1.setTextureUVOffset(98, 100).addCuboid(-1, 0, -13, 2, 0, 26, 0, false);

		door_side_top_2_r2 = createModelPart();
		door_side_top_2_r2.setPivot(-21.5F, -13, 0);
		door_sides_end.addChild(door_side_top_2_r2);
		setRotationAngle(door_side_top_2_r2, 0, 0, 0.1047F);
		door_side_top_2_r2.setTextureUVOffset(112, 14).addCuboid(0, -23, -13, 0, 3, 26, 0, false);

		handrail_door = createModelPart();
		handrail_door.setPivot(0, 24, 0);
		handrail_door.setTextureUVOffset(359, 77).addCuboid(0, -14.25F, 0, 0, 15, 0, 0.2F, false);
		handrail_door.setTextureUVOffset(359, 29).addCuboid(0, -37, 0, 0, 6, 0, 0.2F, false);

		handrail_curve = createModelPart();
		handrail_curve.setPivot(0, 0, 0);
		handrail_door.addChild(handrail_curve);
		handrail_curve.setTextureUVOffset(330, 0).addCuboid(0, -30.9085F, -0.5F, 0, 0, 1, 0.2F, false);
		handrail_curve.setTextureUVOffset(330, 0).addCuboid(0, -14.25F, -0.5F, 0, 0, 1, 0.2F, false);

		handrail_curve_12_r1 = createModelPart();
		handrail_curve_12_r1.setPivot(0, -14.5328F, -1.0464F);
		handrail_curve.addChild(handrail_curve_12_r1);
		setRotationAngle(handrail_curve_12_r1, -0.7854F, 0, 0);
		handrail_curve_12_r1.setTextureUVOffset(330, 0).addCuboid(0, 0, 0, 0, 0, 0, 0.2F, false);

		handrail_curve_10_r1 = createModelPart();
		handrail_curve_10_r1.setPivot(0, -14.806F, -1.256F);
		handrail_curve.addChild(handrail_curve_10_r1);
		setRotationAngle(handrail_curve_10_r1, -1.0472F, 0, 0);
		handrail_curve_10_r1.setTextureUVOffset(330, 0).addCuboid(0, 0, 0, 0, 0, 0, 0.2F, false);

		handrail_curve_9_r1 = createModelPart();
		handrail_curve_9_r1.setPivot(0, -14.3232F, -0.7732F);
		handrail_curve.addChild(handrail_curve_9_r1);
		setRotationAngle(handrail_curve_9_r1, -0.5236F, 0, 0);
		handrail_curve_9_r1.setTextureUVOffset(330, 0).addCuboid(0, 0, 0, 0, 0, 0, 0.2F, false);

		handrail_curve_7_r1 = createModelPart();
		handrail_curve_7_r1.setPivot(0, -22.5793F, -1.3293F);
		handrail_curve.addChild(handrail_curve_7_r1);
		setRotationAngle(handrail_curve_7_r1, 0, 0, -1.5708F);
		handrail_curve_7_r1.setTextureUVOffset(328, 0).addCuboid(-7.5F, 0, 0, 15, 0, 0, 0.2F, false);
		handrail_curve_7_r1.setTextureUVOffset(328, 0).addCuboid(-7.5F, 0, 2.6585F, 15, 0, 0, 0.2F, false);

		handrail_curve_6_r1 = createModelPart();
		handrail_curve_6_r1.setPivot(0, -30.3525F, -1.256F);
		handrail_curve.addChild(handrail_curve_6_r1);
		setRotationAngle(handrail_curve_6_r1, 1.0472F, 0, 0);
		handrail_curve_6_r1.setTextureUVOffset(330, 0).addCuboid(0, 0, 0, 0, 0, 0, 0.2F, false);

		handrail_curve_5_r1 = createModelPart();
		handrail_curve_5_r1.setPivot(0, -30.6257F, -1.0464F);
		handrail_curve.addChild(handrail_curve_5_r1);
		setRotationAngle(handrail_curve_5_r1, 0.7854F, 0, 0);
		handrail_curve_5_r1.setTextureUVOffset(330, 0).addCuboid(0, 0, 0, 0, 0, 0, 0.2F, false);

		handrail_curve_3_r1 = createModelPart();
		handrail_curve_3_r1.setPivot(0, -30.8353F, -0.7732F);
		handrail_curve.addChild(handrail_curve_3_r1);
		setRotationAngle(handrail_curve_3_r1, 0.5236F, 0, 0);
		handrail_curve_3_r1.setTextureUVOffset(330, 0).addCuboid(0, 0, 0, 0, 0, 0, 0.2F, false);

		handrail_curve_11_r1 = createModelPart();
		handrail_curve_11_r1.setPivot(0, -14.5328F, 1.0464F);
		handrail_curve.addChild(handrail_curve_11_r1);
		setRotationAngle(handrail_curve_11_r1, 0.7854F, 0, 0);
		handrail_curve_11_r1.setTextureUVOffset(330, 0).addCuboid(0, 0, 0, 0, 0, 0, 0.2F, false);

		handrail_curve_9_r2 = createModelPart();
		handrail_curve_9_r2.setPivot(0, -14.806F, 1.256F);
		handrail_curve.addChild(handrail_curve_9_r2);
		setRotationAngle(handrail_curve_9_r2, 1.0472F, 0, 0);
		handrail_curve_9_r2.setTextureUVOffset(330, 0).addCuboid(0, 0, 0, 0, 0, 0, 0.2F, false);

		handrail_curve_8_r1 = createModelPart();
		handrail_curve_8_r1.setPivot(0, -14.3232F, 0.7732F);
		handrail_curve.addChild(handrail_curve_8_r1);
		setRotationAngle(handrail_curve_8_r1, 0.5236F, 0, 0);
		handrail_curve_8_r1.setTextureUVOffset(330, 0).addCuboid(0, 0, 0, 0, 0, 0, 0.2F, false);

		handrail_curve_5_r2 = createModelPart();
		handrail_curve_5_r2.setPivot(0, -30.3525F, 1.256F);
		handrail_curve.addChild(handrail_curve_5_r2);
		setRotationAngle(handrail_curve_5_r2, -1.0472F, 0, 0);
		handrail_curve_5_r2.setTextureUVOffset(330, 0).addCuboid(0, 0, 0, 0, 0, 0, 0.2F, false);

		handrail_curve_4_r1 = createModelPart();
		handrail_curve_4_r1.setPivot(0, -30.6257F, 1.0464F);
		handrail_curve.addChild(handrail_curve_4_r1);
		setRotationAngle(handrail_curve_4_r1, -0.7854F, 0, 0);
		handrail_curve_4_r1.setTextureUVOffset(330, 0).addCuboid(0, 0, 0, 0, 0, 0, 0.2F, false);

		handrail_curve_2_r1 = createModelPart();
		handrail_curve_2_r1.setPivot(0, -30.8353F, 0.7732F);
		handrail_curve.addChild(handrail_curve_2_r1);
		setRotationAngle(handrail_curve_2_r1, -0.5236F, 0, 0);
		handrail_curve_2_r1.setTextureUVOffset(330, 0).addCuboid(0, 0, 0, 0, 0, 0, 0.2F, false);

		head = createModelPart();
		head.setPivot(0, 24, 0);
		head.setTextureUVOffset(117, 126).addCuboid(-20.5F, -13, 7, 1, 13, 2, 0, false);
		head.setTextureUVOffset(117, 126).addCuboid(19.5F, -13, 7, 1, 13, 2, 0, true);
		head.setTextureUVOffset(192, 113).addCuboid(-21, 0, 7, 42, 1, 1, 0, false);
		head.setTextureUVOffset(228, 174).addCuboid(-20, -36, 7, 40, 36, 0, 0, false);

		upper_wall_2_r5 = createModelPart();
		upper_wall_2_r5.setPivot(21.5F, -13, 0);
		head.addChild(upper_wall_2_r5);
		setRotationAngle(upper_wall_2_r5, 0, 0, -0.1047F);
		upper_wall_2_r5.setTextureUVOffset(8, 90).addCuboid(-2, -21, 7, 1, 21, 2, 0, true);

		upper_wall_1_r4 = createModelPart();
		upper_wall_1_r4.setPivot(-21.5F, -13, 0);
		head.addChild(upper_wall_1_r4);
		setRotationAngle(upper_wall_1_r4, 0, 0, 0.1047F);
		upper_wall_1_r4.setTextureUVOffset(8, 90).addCuboid(1, -21, 7, 1, 21, 2, 0, false);

		head_exterior = createModelPart();
		head_exterior.setPivot(0, 24, 0);
		head_exterior.setTextureUVOffset(272, 130).addCuboid(-21.5F, 0, -19, 1, 3, 27, 0, false);
		head_exterior.setTextureUVOffset(189, 243).addCuboid(-21.5F, -13, -18, 2, 13, 29, 0, false);
		head_exterior.setTextureUVOffset(272, 130).addCuboid(20.5F, 0, -19, 1, 3, 27, 0, true);
		head_exterior.setTextureUVOffset(189, 243).addCuboid(19.5F, -13, -18, 2, 13, 29, 0, true);
		head_exterior.setTextureUVOffset(163, 138).addCuboid(-20, 0, -22, 40, 1, 29, 0, false);
		head_exterior.setTextureUVOffset(242, 53).addCuboid(-19.5F, -42, 7, 39, 42, 0, 0, false);

		upper_wall_2_r6 = createModelPart();
		upper_wall_2_r6.setPivot(21.5F, -13, 1);
		head_exterior.addChild(upper_wall_2_r6);
		setRotationAngle(upper_wall_2_r6, 0, 0, -0.1047F);
		upper_wall_2_r6.setTextureUVOffset(69, 239).addCuboid(-2, -23, -19, 2, 23, 29, 0, true);

		upper_wall_1_r5 = createModelPart();
		upper_wall_1_r5.setPivot(-21.5F, -13, 1);
		head_exterior.addChild(upper_wall_1_r5);
		setRotationAngle(upper_wall_1_r5, 0, 0, 0.1047F);
		upper_wall_1_r5.setTextureUVOffset(69, 239).addCuboid(0, -23, -19, 2, 23, 29, 0, false);

		bumper = createModelPart();
		bumper.setPivot(0, 0.1F, -17);
		head_exterior.addChild(bumper);
		bumper.setTextureUVOffset(54, 155).addCuboid(-4.5855F, -0.1F, -8.1564F, 5, 3, 3, 0, false);
		bumper.setTextureUVOffset(54, 155).addCuboid(-0.4145F, -0.1F, -8.1564F, 5, 3, 3, 0, true);

		bumper_2_r1 = createModelPart();
		bumper_2_r1.setPivot(-21.5F, -0.1F, -2);
		bumper.addChild(bumper_2_r1);
		setRotationAngle(bumper_2_r1, 0, 0.3491F, 0);
		bumper_2_r1.setTextureUVOffset(112, 46).addCuboid(0, 0, 0, 18, 3, 3, 0, true);

		bumper_1_r1 = createModelPart();
		bumper_1_r1.setPivot(21.5F, -0.1F, -2);
		bumper.addChild(bumper_1_r1);
		setRotationAngle(bumper_1_r1, 0, -0.3491F, 0);
		bumper_1_r1.setTextureUVOffset(112, 46).addCuboid(-18, 0, 0, 18, 3, 3, 0, false);

		head_back = createModelPart();
		head_back.setPivot(0, 0, -17);
		head_exterior.addChild(head_back);
		head_back.setTextureUVOffset(52, 0).addCuboid(-8, -33, -5, 1, 33, 0, 0, false);
		head_back.setTextureUVOffset(32, 0).addCuboid(7, -33, -5, 1, 33, 0, 0, false);
		head_back.setTextureUVOffset(54, 182).addCuboid(-8, -42, -5, 16, 9, 0, 0, false);
		head_back.setTextureUVOffset(294, 95).addCuboid(-7, -33, -5, 14, 34, 0, 0, false);

		front_right_panel_3_r2 = createModelPart();
		front_right_panel_3_r2.setPivot(8, -42, -5);
		head_back.addChild(front_right_panel_3_r2);
		setRotationAngle(front_right_panel_3_r2, 0, -0.3491F, 0);
		front_right_panel_3_r2.setTextureUVOffset(174, 168).addCuboid(0, 0, 0, 15, 42, 0, 0, false);

		front_right_panel_2_r1 = createModelPart();
		front_right_panel_2_r1.setPivot(-8, -42, -5);
		head_back.addChild(front_right_panel_2_r1);
		setRotationAngle(front_right_panel_2_r1, 0, 0.3491F, 0);
		front_right_panel_2_r1.setTextureUVOffset(0, 212).addCuboid(-15, 0, 0, 15, 42, 0, 0, false);

		roof_head_exterior = createModelPart();
		roof_head_exterior.setPivot(0, 0, 4);
		head_exterior.addChild(roof_head_exterior);
		roof_head_exterior.setTextureUVOffset(54, 100).addCuboid(-4, -41.375F, -26, 4, 0, 34, 0, false);
		roof_head_exterior.setTextureUVOffset(54, 100).addCuboid(0, -41.375F, -26, 4, 0, 34, 0, true);

		outer_roof_7_r2 = createModelPart();
		outer_roof_7_r2.setPivot(4, -41.375F, -3);
		roof_head_exterior.addChild(outer_roof_7_r2);
		setRotationAngle(outer_roof_7_r2, 0, 0, 0.0873F);
		outer_roof_7_r2.setTextureUVOffset(86, 0).addCuboid(0, 0, -23, 6, 0, 34, 0, false);

		outer_roof_6_r4 = createModelPart();
		outer_roof_6_r4.setPivot(12.392F, -40.205F, -3);
		roof_head_exterior.addChild(outer_roof_6_r4);
		setRotationAngle(outer_roof_6_r4, 0, 0, 0.2618F);
		outer_roof_6_r4.setTextureUVOffset(98, 0).addCuboid(-2.5F, 0, -23, 5, 0, 34, 0, true);

		outer_roof_5_r5 = createModelPart();
		outer_roof_5_r5.setPivot(16.2163F, -39.0449F, -3);
		roof_head_exterior.addChild(outer_roof_5_r5);
		setRotationAngle(outer_roof_5_r5, 0, 0, 0.3491F);
		outer_roof_5_r5.setTextureUVOffset(0, 0).addCuboid(-1.5F, 0, -23, 3, 0, 34, 0, true);

		outer_roof_4_r5 = createModelPart();
		outer_roof_4_r5.setPivot(18.2687F, -37.7659F, -3);
		roof_head_exterior.addChild(outer_roof_4_r5);
		setRotationAngle(outer_roof_4_r5, 0, 0, 0.8727F);
		outer_roof_4_r5.setTextureUVOffset(0, 77).addCuboid(-1, 0, -23, 2, 0, 34, 0, true);

		outer_roof_3_r5 = createModelPart();
		outer_roof_3_r5.setPivot(18.6114F, -35.9228F, -3);
		roof_head_exterior.addChild(outer_roof_3_r5);
		setRotationAngle(outer_roof_3_r5, 0, 0, 1.3788F);
		outer_roof_3_r5.setTextureUVOffset(256, 95).addCuboid(-1, -0.5F, -23, 2, 1, 34, 0, true);

		outer_roof_6_r5 = createModelPart();
		outer_roof_6_r5.setPivot(-4, -41.375F, -3);
		roof_head_exterior.addChild(outer_roof_6_r5);
		setRotationAngle(outer_roof_6_r5, 0, 0, -0.0873F);
		outer_roof_6_r5.setTextureUVOffset(86, 0).addCuboid(-6, 0, -23, 6, 0, 34, 0, true);

		outer_roof_5_r6 = createModelPart();
		outer_roof_5_r6.setPivot(-12.392F, -40.205F, -3);
		roof_head_exterior.addChild(outer_roof_5_r6);
		setRotationAngle(outer_roof_5_r6, 0, 0, -0.2618F);
		outer_roof_5_r6.setTextureUVOffset(98, 0).addCuboid(-2.5F, 0, -23, 5, 0, 34, 0, false);

		outer_roof_4_r6 = createModelPart();
		outer_roof_4_r6.setPivot(-16.2163F, -39.0449F, -3);
		roof_head_exterior.addChild(outer_roof_4_r6);
		setRotationAngle(outer_roof_4_r6, 0, 0, -0.3491F);
		outer_roof_4_r6.setTextureUVOffset(0, 0).addCuboid(-1.5F, 0, -23, 3, 0, 34, 0, false);

		outer_roof_3_r6 = createModelPart();
		outer_roof_3_r6.setPivot(-18.2687F, -37.7659F, -3);
		roof_head_exterior.addChild(outer_roof_3_r6);
		setRotationAngle(outer_roof_3_r6, 0, 0, -0.8727F);
		outer_roof_3_r6.setTextureUVOffset(0, 77).addCuboid(-1, 0, -23, 2, 0, 34, 0, false);

		outer_roof_2_r4 = createModelPart();
		outer_roof_2_r4.setPivot(-18.6114F, -35.9228F, -3);
		roof_head_exterior.addChild(outer_roof_2_r4);
		setRotationAngle(outer_roof_2_r4, 0, 0, -1.3788F);
		outer_roof_2_r4.setTextureUVOffset(256, 95).addCuboid(-1, -0.5F, -23, 2, 1, 34, 0, false);

		roof_exterior = createModelPart();
		roof_exterior.setPivot(0, 24, 0);
		roof_exterior.setTextureUVOffset(72, 0).addCuboid(-4, -41.375F, -20, 4, 0, 40, 0, false);

		outer_roof_5_r7 = createModelPart();
		outer_roof_5_r7.setPivot(-4, -41.375F, 4);
		roof_exterior.addChild(outer_roof_5_r7);
		setRotationAngle(outer_roof_5_r7, 0, 0, -0.0873F);
		outer_roof_5_r7.setTextureUVOffset(0, 0).addCuboid(-6, 0, -24, 6, 0, 40, 0, false);

		outer_roof_4_r7 = createModelPart();
		outer_roof_4_r7.setPivot(-12.392F, -40.205F, 4);
		roof_exterior.addChild(outer_roof_4_r7);
		setRotationAngle(outer_roof_4_r7, 0, 0, -0.2618F);
		outer_roof_4_r7.setTextureUVOffset(56, 0).addCuboid(-2.5F, 0, -24, 5, 0, 40, 0, false);

		outer_roof_3_r7 = createModelPart();
		outer_roof_3_r7.setPivot(-16.2163F, -39.0449F, 4);
		roof_exterior.addChild(outer_roof_3_r7);
		setRotationAngle(outer_roof_3_r7, 0, 0, -0.3491F);
		outer_roof_3_r7.setTextureUVOffset(0, 77).addCuboid(-1.5F, 0, -24, 3, 0, 40, 0, false);

		outer_roof_2_r5 = createModelPart();
		outer_roof_2_r5.setPivot(-18.2687F, -37.7659F, 4);
		roof_exterior.addChild(outer_roof_2_r5);
		setRotationAngle(outer_roof_2_r5, 0, 0, -0.8727F);
		outer_roof_2_r5.setTextureUVOffset(66, 0).addCuboid(-1, 0, -24, 2, 0, 40, 0, false);

		outer_roof_1_r2 = createModelPart();
		outer_roof_1_r2.setPivot(-18.6114F, -35.9228F, 4);
		roof_exterior.addChild(outer_roof_1_r2);
		setRotationAngle(outer_roof_1_r2, 0, 0, -1.3788F);
		outer_roof_1_r2.setTextureUVOffset(225, 225).addCuboid(-1, -0.5F, -24, 2, 1, 40, 0, false);

		roof_door = createModelPart();
		roof_door.setPivot(0, 24, 0);
		roof_door.setTextureUVOffset(94, 100).addCuboid(-17.9149F, -32.7849F, -13, 2, 0, 26, 0, false);
		roof_door.setTextureUVOffset(4, 77).addCuboid(-11.3838F, -34.8979F, -13, 2, 0, 26, 0, false);
		roof_door.setTextureUVOffset(70, 100).addCuboid(-6.6649F, -36.1658F, -13, 7, 0, 26, 0, false);

		inner_roof_4_r1 = createModelPart();
		inner_roof_4_r1.setPivot(-9.3834F, -34.898F, 3);
		roof_door.addChild(inner_roof_4_r1);
		setRotationAngle(inner_roof_4_r1, 0, 0, -0.4363F);
		inner_roof_4_r1.setTextureUVOffset(0, 0).addCuboid(0, 0, -16, 3, 0, 26, 0, false);

		inner_roof_2_r1 = createModelPart();
		inner_roof_2_r1.setPivot(-15.9149F, -32.7849F, 3);
		roof_door.addChild(inner_roof_2_r1);
		setRotationAngle(inner_roof_2_r1, 0, 0, -0.4363F);
		inner_roof_2_r1.setTextureUVOffset(84, 100).addCuboid(0, 0, -16, 5, 0, 26, 0, false);

		roof_window = createModelPart();
		roof_window.setPivot(0, 24, 0);
		roof_window.setTextureUVOffset(38, 0).addCuboid(-17.9149F, -32.7849F, -27, 2, 0, 54, 0, false);
		roof_window.setTextureUVOffset(34, 0).addCuboid(-11.3838F, -34.8979F, -27, 2, 0, 54, 0, false);
		roof_window.setTextureUVOffset(2, 0).addCuboid(-6.6649F, -36.1658F, -27, 7, 0, 54, 0, false);

		inner_roof_9_r1 = createModelPart();
		inner_roof_9_r1.setPivot(-23.3588F, 10.4067F, -26.999F);
		roof_window.addChild(inner_roof_9_r1);
		setRotationAngle(inner_roof_9_r1, 0, -1.5708F, -0.4363F);
		inner_roof_9_r1.setTextureUVOffset(26, 33).addCuboid(54, -37, -30, 0, 1, 5, 0, true);
		inner_roof_9_r1.setTextureUVOffset(26, 33).addCuboid(0, -37, -30, 0, 1, 5, 0, false);

		inner_roof_6_r1 = createModelPart();
		inner_roof_6_r1.setPivot(-9.3834F, -34.898F, 3);
		roof_window.addChild(inner_roof_6_r1);
		setRotationAngle(inner_roof_6_r1, 0, 0, -0.4363F);
		inner_roof_6_r1.setTextureUVOffset(22, 0).addCuboid(0, 0, -30, 3, 0, 54, 0, false);

		inner_roof_4_r2 = createModelPart();
		inner_roof_4_r2.setPivot(-11.2897F, -34.4754F, 0);
		roof_window.addChild(inner_roof_4_r2);
		setRotationAngle(inner_roof_4_r2, 0, 0, -0.2182F);
		inner_roof_4_r2.setTextureUVOffset(16, 0).addCuboid(-2.9063F, -0.4226F, -27, 3, 0, 54, 0, false);

		inner_roof_3_r1 = createModelPart();
		inner_roof_3_r1.setPivot(-15.8212F, -32.3623F, 3);
		roof_window.addChild(inner_roof_3_r1);
		setRotationAngle(inner_roof_3_r1, 0, 0, -0.6109F);
		inner_roof_3_r1.setTextureUVOffset(28, 0).addCuboid(0.0937F, -0.4226F, -30, 3, 0, 54, 0, false);

		roof_end = createModelPart();
		roof_end.setPivot(0, 24, 0);


		side_1 = createModelPart();
		side_1.setPivot(0, 0, 0);
		roof_end.addChild(side_1);
		side_1.setTextureUVOffset(168, 29).addCuboid(-17.9149F, -33.7849F, -7, 4, 1, 18, 0, true);
		side_1.setTextureUVOffset(10, 10).addCuboid(-11.3838F, -34.8979F, 3, 2, 0, 8, 0, true);

		inner_roof_5_r1 = createModelPart();
		inner_roof_5_r1.setPivot(-9.3834F, -34.898F, 3);
		side_1.addChild(inner_roof_5_r1);
		setRotationAngle(inner_roof_5_r1, 0, 0, -0.4363F);
		inner_roof_5_r1.setTextureUVOffset(8, 0).addCuboid(0, 0, -2, 3, 0, 10, 0, true);

		inner_roof_3_r2 = createModelPart();
		inner_roof_3_r2.setPivot(-14.6848F, -32.1477F, 3);
		side_1.addChild(inner_roof_3_r2);
		setRotationAngle(inner_roof_3_r2, 0, 0, -0.6912F);
		inner_roof_3_r2.setTextureUVOffset(78, 183).addCuboid(1, -1, 0, 4, 1, 8, 0, true);

		side_2 = createModelPart();
		side_2.setPivot(0, 0, 0);
		roof_end.addChild(side_2);
		side_2.setTextureUVOffset(168, 29).addCuboid(13.9149F, -33.7849F, -7, 4, 1, 18, 0, false);
		side_2.setTextureUVOffset(10, 10).addCuboid(9.3838F, -34.8979F, 3, 2, 0, 8, 0, false);

		inner_roof_7_r1 = createModelPart();
		inner_roof_7_r1.setPivot(13.9156F, -32.7856F, 11);
		side_2.addChild(inner_roof_7_r1);
		setRotationAngle(inner_roof_7_r1, 0, 0, 0.6912F);
		inner_roof_7_r1.setTextureUVOffset(78, 183).addCuboid(-4, -0.999F, -8, 4, 1, 8, 0, false);

		inner_roof_6_r2 = createModelPart();
		inner_roof_6_r2.setPivot(6.6644F, -36.1659F, 3);
		side_2.addChild(inner_roof_6_r2);
		setRotationAngle(inner_roof_6_r2, 0, 0, 0.4363F);
		inner_roof_6_r2.setTextureUVOffset(8, 0).addCuboid(0, 0, -2, 3, 0, 10, 0, false);

		mid_roof = createModelPart();
		mid_roof.setPivot(0, 0, 0);
		roof_end.addChild(mid_roof);
		mid_roof.setTextureUVOffset(112, 67).addCuboid(-18, -36.7849F, -9, 36, 4, 2, 0, false);
		mid_roof.setTextureUVOffset(54, 149).addCuboid(7.9149F, -36.7849F, -7, 6, 4, 2, 0, false);
		mid_roof.setTextureUVOffset(54, 149).addCuboid(-13.9149F, -36.7849F, -7, 6, 4, 2, 0, true);

		roof_end_gangway = createModelPart();
		roof_end_gangway.setPivot(0, 24, 0);
		roof_end_gangway.setTextureUVOffset(96, 40).addCuboid(-17.9149F, -33.7849F, 9, 4, 1, 2, 0, false);
		roof_end_gangway.setTextureUVOffset(96, 40).addCuboid(13.9149F, -33.7849F, 9, 4, 1, 2, 0, true);

		mid_roof_gangway = createModelPart();
		mid_roof_gangway.setPivot(0, 0, 0);
		roof_end_gangway.addChild(mid_roof_gangway);
		mid_roof_gangway.setTextureUVOffset(268, 33).addCuboid(-8.5F, -34.875F, -9, 17, 2, 16, 0, false);
		mid_roof_gangway.setTextureUVOffset(32, 140).addCuboid(8.5F, -36.875F, 7, 6, 4, 2, 0, false);
		mid_roof_gangway.setTextureUVOffset(32, 140).addCuboid(-14.5F, -36.875F, 7, 6, 4, 2, 0, true);

		roof_light = createModelPart();
		roof_light.setPivot(0, 24, 0);
		roof_light.setTextureUVOffset(0, 77).addCuboid(-11.3838F, -34.9F, -13, 2, 0, 26, 0, false);

		destination_display_end_interior = createModelPart();
		destination_display_end_interior.setPivot(0, 24, 0);
		destination_display_end_interior.setTextureUVOffset(269, 245).addCuboid(-0.5032F, -36.4362F, -7, 9, 2, 18, 0, false);
		destination_display_end_interior.setTextureUVOffset(269, 245).addCuboid(-8.4964F, -36.4363F, -7, 9, 2, 18, 0, true);

		display_6_r1 = createModelPart();
		display_6_r1.setPivot(-24.0533F, -3.2172F, 42.001F);
		destination_display_end_interior.addChild(display_6_r1);
		setRotationAngle(display_6_r1, 0, 0, 0.48F);
		display_6_r1.setTextureUVOffset(0, 149).addCuboid(-1.6162F, -35.875F, -39, 1, 1, 8, 0, true);

		display_5_r1 = createModelPart();
		display_5_r1.setPivot(4.8028F, -41.5322F, 9.501F);
		destination_display_end_interior.addChild(display_5_r1);
		setRotationAngle(display_5_r1, 0, 0, -0.48F);
		display_5_r1.setTextureUVOffset(0, 149).addCuboid(0, 7, -6.5F, 1, 1, 8, 0, false);

		display_4_r1 = createModelPart();
		display_4_r1.setPivot(13.7899F, -31.7933F, 21);
		destination_display_end_interior.addChild(display_4_r1);
		setRotationAngle(display_4_r1, 0, 0, 0.295F);
		display_4_r1.setTextureUVOffset(134, 28).addCuboid(-6.1257F, -2.9925F, -26, 6, 2, 8, 0, false);

		display_3_r1 = createModelPart();
		display_3_r1.setPivot(-7.7652F, -33.617F, 21);
		destination_display_end_interior.addChild(display_3_r1);
		setRotationAngle(display_3_r1, 0, 0, -0.295F);
		display_3_r1.setTextureUVOffset(134, 28).addCuboid(-6.1257F, -2.9925F, -26, 6, 2, 8, 0, true);

		roof_head = createModelPart();
		roof_head.setPivot(0, 24, 0);
		roof_head.setTextureUVOffset(14, 18).addCuboid(-17.9149F, -32.7849F, 7, 2, 0, 4, 0, false);
		roof_head.setTextureUVOffset(18, 10).addCuboid(-11.3838F, -34.8979F, 7, 2, 0, 4, 0, false);
		roof_head.setTextureUVOffset(18, 10).addCuboid(9.3838F, -34.8979F, 7, 2, 0, 4, 0, true);
		roof_head.setTextureUVOffset(14, 18).addCuboid(15.9149F, -32.7849F, 7, 2, 0, 4, 0, true);

		inner_roof_6_r3 = createModelPart();
		inner_roof_6_r3.setPivot(11.3834F, -34.898F, 19);
		roof_head.addChild(inner_roof_6_r3);
		setRotationAngle(inner_roof_6_r3, 0, 0, 0.4363F);
		inner_roof_6_r3.setTextureUVOffset(22, 34).addCuboid(0, 0, -12, 5, 0, 4, 0, true);

		inner_roof_3_r3 = createModelPart();
		inner_roof_3_r3.setPivot(-15.9149F, -32.7849F, 27);
		roof_head.addChild(inner_roof_3_r3);
		setRotationAngle(inner_roof_3_r3, 0, 0, -0.4363F);
		inner_roof_3_r3.setTextureUVOffset(22, 34).addCuboid(0, 0, -20, 5, 0, 4, 0, false);

		destination_display = createModelPart();
		destination_display.setPivot(0, 0, 4);
		roof_head.addChild(destination_display);
		destination_display.setTextureUVOffset(269, 245).addCuboid(-0.5032F, -36.4362F, 3, 9, 2, 18, 0, false);
		destination_display.setTextureUVOffset(269, 245).addCuboid(-8.4964F, -36.4363F, 3, 9, 2, 18, 0, true);

		display_7_r1 = createModelPart();
		display_7_r1.setPivot(-24.0533F, -3.2172F, 52.001F);
		destination_display.addChild(display_7_r1);
		setRotationAngle(display_7_r1, 0, 0, 0.48F);
		display_7_r1.setTextureUVOffset(110, 241).addCuboid(-1.6162F, -35.875F, -49, 1, 1, 18, 0, true);

		display_6_r2 = createModelPart();
		display_6_r2.setPivot(4.8028F, -41.5322F, 19.501F);
		destination_display.addChild(display_6_r2);
		setRotationAngle(display_6_r2, 0, 0, -0.48F);
		display_6_r2.setTextureUVOffset(110, 241).addCuboid(0, 7, -16.5F, 1, 1, 18, 0, false);

		headlights = createModelPart();
		headlights.setPivot(0, 24, 0);


		headlights_2_r1 = createModelPart();
		headlights_2_r1.setPivot(8, -42, -22);
		headlights.addChild(headlights_2_r1);
		setRotationAngle(headlights_2_r1, 0, -0.3491F, 0);
		headlights_2_r1.setTextureUVOffset(0, 29).addCuboid(3, 29, -0.1F, 10, 10, 0, 0, true);

		headlights_1_r1 = createModelPart();
		headlights_1_r1.setPivot(-8, -42, -22);
		headlights.addChild(headlights_1_r1);
		setRotationAngle(headlights_1_r1, 0, 0.3491F, 0);
		headlights_1_r1.setTextureUVOffset(0, 29).addCuboid(-13, 29, -0.1F, 10, 10, 0, 0, false);

		tail_lights = createModelPart();
		tail_lights.setPivot(0, 24, 0);


		tail_lights_2_r1 = createModelPart();
		tail_lights_2_r1.setPivot(8, -42, -22);
		tail_lights.addChild(tail_lights_2_r1);
		setRotationAngle(tail_lights_2_r1, 0, -0.3491F, 0);
		tail_lights_2_r1.setTextureUVOffset(96, 43).addCuboid(9, 28, -0.05F, 5, 5, 0, 0, true);

		tail_lights_1_r1 = createModelPart();
		tail_lights_1_r1.setPivot(-8, -42, -22);
		tail_lights.addChild(tail_lights_1_r1);
		setRotationAngle(tail_lights_1_r1, 0, 0.3491F, 0);
		tail_lights_1_r1.setTextureUVOffset(96, 43).addCuboid(-14, 28, -0.05F, 5, 5, 0, 0, false);

		door_light_interior_off = createModelPart();
		door_light_interior_off.setPivot(0, 24, 0);


		light_2_r1 = createModelPart();
		light_2_r1.setPivot(-9.6762F, -32.7244F, 13.2F);
		door_light_interior_off.addChild(light_2_r1);
		setRotationAngle(light_2_r1, 0, 0, 0.1047F);
		light_2_r1.setTextureUVOffset(351, 10).addCuboid(-7, 1.75F, -27.2F, 0, 0, 0, 0.3F, false);
		light_2_r1.setTextureUVOffset(351, 10).addCuboid(-7, 1.75F, 0.8F, 0, 0, 0, 0.3F, false);

		door_light_interior_on = createModelPart();
		door_light_interior_on.setPivot(0, 24, 0);


		light_3_r1 = createModelPart();
		light_3_r1.setPivot(-9.6762F, -32.7244F, 13.2F);
		door_light_interior_on.addChild(light_3_r1);
		setRotationAngle(light_3_r1, 0, 0, 0.1047F);
		light_3_r1.setTextureUVOffset(351, 15).addCuboid(-7, 1.75F, -27.2F, 0, 0, 0, 0.3F, false);
		light_3_r1.setTextureUVOffset(351, 15).addCuboid(-7, 1.75F, 0.8F, 0, 0, 0, 0.3F, false);

		buildModel();
	}

	private static final int DOOR_MAX = 13;

	@Override
	public ModelR211 createNew(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		return new ModelR211(openGangway, doorAnimationType, renderDoorOverlay);
	}

	@Override
	protected void renderWindowPositions(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
		switch (renderStage) {
			case LIGHT:
				renderMirror(roof_light, graphicsHolder, light, position);
				renderMirror(roof_light, graphicsHolder, light, position - 15);
				renderMirror(roof_light, graphicsHolder, light, position + 15);
				break;
			case INTERIOR:
				renderMirror(window, graphicsHolder, light, position);
				if (renderDetails) {
					renderMirror(roof_window, graphicsHolder, light, position);
					renderMirror(window_handrails, graphicsHolder, light, position);
					renderMirror(side_panel, graphicsHolder, light, position + 24.1F);
					renderMirror(side_panel, graphicsHolder, light, position - 24.1F);
				}
				break;
			case EXTERIOR:
				renderMirror(roof_exterior, graphicsHolder, light, position);
				if (isIndex(0, position, getWindowPositions()) && isEnd1Head) {
					renderOnceFlipped(window_exterior_end, graphicsHolder, light, position);
				} else if (isIndex(-1, position, getWindowPositions()) && isEnd2Head) {
					renderOnce(window_exterior_end, graphicsHolder, light, position);
				} else {
					renderMirror(window_exterior, graphicsHolder, light, position);
				}
		}
	}

	@Override
	protected void renderDoorPositions(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
		final boolean doorOpen = doorLeftZ > 0 || doorRightZ > 0;

		switch (renderStage) {
			case LIGHT:
				renderMirror(roof_light, graphicsHolder, light, position);
				if (doorOpen) {
					renderMirror(door_light_interior_on, graphicsHolder, light, position);
				}
				break;
			case INTERIOR:
				door_right.setOffset(doorRightX, 0, doorRightZ);
				door_left.setOffset(doorRightX, 0, -doorRightZ);
				renderOnce(door, graphicsHolder, light, position);
				door_right.setOffset(doorLeftX, 0, doorLeftZ);
				door_left.setOffset(doorLeftX, 0, -doorLeftZ);
				renderOnceFlipped(door, graphicsHolder, light, position);
				if (!doorOpen) {
					renderMirror(door_light_interior_off, graphicsHolder, light, position);
				}
				if (renderDetails) {
					renderOnce(handrail_door, graphicsHolder, light, position);
					renderMirror(roof_door, graphicsHolder, light, position);
				}
				break;
			case EXTERIOR:
				final boolean door1End = isIndex(0, position, getDoorPositions()) && isEnd1Head;
				final boolean door2End = isIndex(-1, position, getDoorPositions()) && isEnd2Head;

				if (door1End || door2End) {
					door_right_exterior_end.setOffset(doorRightX, 0, doorRightZ);
					door_left_exterior_end.setOffset(doorRightX, 0, -doorRightZ);
					renderOnce(door_end_exterior, graphicsHolder, light, position);
				} else {
					door_right_exterior.setOffset(doorRightX, 0, doorRightZ);
					door_left_exterior.setOffset(doorRightX, 0, -doorRightZ);
					renderOnce(door_exterior, graphicsHolder, light, position);
				}

				if (door1End || door2End) {
					door_right_exterior_end.setOffset(doorLeftX, 0, doorLeftZ);
					door_left_exterior_end.setOffset(doorLeftX, 0, -doorLeftZ);
					renderOnceFlipped(door_end_exterior, graphicsHolder, light, position);
				} else {
					door_right_exterior.setOffset(doorLeftX, 0, doorLeftZ);
					door_left_exterior.setOffset(doorLeftX, 0, -doorLeftZ);
					renderOnceFlipped(door_exterior, graphicsHolder, light, position);
				}
				renderMirror(roof_exterior, graphicsHolder, light, position);
		}
	}

	@Override
	protected void renderHeadPosition1(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean useHeadlights) {
		switch (renderStage) {
			case LIGHT:
				renderOnce(roof_light, graphicsHolder, light, position + 20);
				renderOnceFlipped(roof_light, graphicsHolder, light, position + 20);
				break;
			case ALWAYS_ON_LIGHT:
				renderOnce(useHeadlights ? headlights : tail_lights, graphicsHolder, light, position);
				break;
			case INTERIOR:
				renderOnce(head, graphicsHolder, light, position);
				if (renderDetails) {
					renderOnce(roof_head, graphicsHolder, light, position);
				}
				break;
			case EXTERIOR:
				renderOnce(head_exterior, graphicsHolder, light, position);
				break;
		}
	}

	@Override
	protected void renderHeadPosition2(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean useHeadlights) {
		switch (renderStage) {
			case LIGHT:
				renderOnce(roof_light, graphicsHolder, light, position - 20);
				renderOnceFlipped(roof_light, graphicsHolder, light, position - 20);
				break;
			case ALWAYS_ON_LIGHT:
				renderOnceFlipped(useHeadlights ? headlights : tail_lights, graphicsHolder, light, position);
				break;
			case INTERIOR:
				renderOnceFlipped(head, graphicsHolder, light, position);
				if (renderDetails) {
					renderOnceFlipped(roof_head, graphicsHolder, light, position);
				}
				break;
			case EXTERIOR:
				renderOnceFlipped(head_exterior, graphicsHolder, light, position);
				break;
		}
	}

	@Override
	protected void renderEndPosition1(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
		switch (renderStage) {
			case LIGHT:
				if (!openGangway) {
					renderOnce(roof_light, graphicsHolder, light, position + 16);
					renderOnceFlipped(roof_light, graphicsHolder, light, position + 16);
				}
				break;
			case INTERIOR:
				if (openGangway) {
					renderOnce(end_gangway, graphicsHolder, light, position);
				} else {
					renderOnce(end, graphicsHolder, light, position);
				}
				if (renderDetails) {
					if (openGangway) {
						renderOnce(roof_end_gangway, graphicsHolder, light, position);
						renderOnce(destination_display_end_interior, graphicsHolder, light, position + 8);
					} else {
						renderOnce(roof_end, graphicsHolder, light, position);
						renderOnce(destination_display_end_interior, graphicsHolder, light, position);
					}
				}
				break;
			case EXTERIOR:
				if (openGangway) {
					renderOnce(end_gangway_exterior, graphicsHolder, light, position);
				} else {
					renderOnce(end_exterior, graphicsHolder, light, position);
				}
				break;
		}
	}

	@Override
	protected void renderEndPosition2(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
		switch (renderStage) {
			case LIGHT:
				if (!openGangway) {
					renderOnce(roof_light, graphicsHolder, light, position - 16);
					renderOnceFlipped(roof_light, graphicsHolder, light, position - 16);
				}
				break;
			case INTERIOR:
				if (openGangway) {
					renderOnceFlipped(end_gangway, graphicsHolder, light, position);
				} else {
					renderOnceFlipped(end, graphicsHolder, light, position);
				}
				if (renderDetails) {
					if (openGangway) {
						renderOnceFlipped(roof_end_gangway, graphicsHolder, light, position);
						renderOnceFlipped(destination_display_end_interior, graphicsHolder, light, position - 8);
					} else {
						renderOnceFlipped(roof_end, graphicsHolder, light, position);
						renderOnceFlipped(destination_display_end_interior, graphicsHolder, light, position);
					}
				}
				break;
			case EXTERIOR:
				if (openGangway) {
					renderOnceFlipped(end_gangway_exterior, graphicsHolder, light, position);
				} else {
					renderOnceFlipped(end_exterior, graphicsHolder, light, position);
				}
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
		return new int[]{-80, 0, 80};
	}

	@Override
	protected int[] getDoorPositions() {
		return new int[]{-120, -40, 40, 120};
	}

	@Override
	protected int[] getEndPositions() {
		return new int[]{-144, 144};
	}

	@Override
	protected int getDoorMax() {
		return DOOR_MAX;
	}

	@Override
	protected void renderTextDisplays(StoredMatrixTransformations storedMatrixTransformations, int thisRouteColor, String thisRouteName, String thisRouteNumber, String thisStationName, String thisRouteDestination, String nextStationName, Consumer<BiConsumer<String, InterchangeColorsForStationName>> getInterchanges, int car, int totalCars, boolean atPlatform, boolean isTerminating, ObjectArrayList<ScrollingText> scrollingTexts) {
		renderFrontDestination(
				storedMatrixTransformations,
				0, -2.26F, getEndPositions()[0] / 16F - 1.37F, 0, 0, -0.01F,
				0, 0, 0.44F, 0.12F,
				ARGB_WHITE, ARGB_WHITE, 1, getDestinationString(thisRouteDestination, TextSpacingType.NORMAL, true), true, car, totalCars
		);
		renderFrontDestination(
				storedMatrixTransformations,
				0.5F, 0, getEndPositions()[0] / 16F - 1.37F, 0.35F, -1.57F, -0.01F,
				0, -20, 0.4F, 0.36F,
				ARGB_WHITE, ARGB_WHITE, 1, thisRouteNumber, false, car, totalCars
		);
	}

	@Override
	protected String defaultDestinationString() {
		return "Not in Service";
	}
}
