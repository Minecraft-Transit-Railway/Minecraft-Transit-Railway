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

public class ModelRTrain extends ModelSimpleTrainBase<ModelRTrain> {

	private final ModelPartExtension window;
	private final ModelPartExtension upper_wall_r1;
	private final ModelPartExtension window_exterior_1;
	private final ModelPartExtension upper_wall_r2;
	private final ModelPartExtension window_exterior_2;
	private final ModelPartExtension upper_wall_r3;
	private final ModelPartExtension door_exterior_1;
	private final ModelPartExtension door_left_exterior_1;
	private final ModelPartExtension door_left_top_r1;
	private final ModelPartExtension door_right_exterior_1;
	private final ModelPartExtension door_right_top_r1;
	private final ModelPartExtension door_exterior_2;
	private final ModelPartExtension door_right_exterior_2;
	private final ModelPartExtension door_left_top_r2;
	private final ModelPartExtension door_left_exterior_2;
	private final ModelPartExtension door_right_top_r2;
	private final ModelPartExtension door;
	private final ModelPartExtension door_left;
	private final ModelPartExtension door_left_top_r3;
	private final ModelPartExtension door_right;
	private final ModelPartExtension door_right_top_r3;
	private final ModelPartExtension end;
	private final ModelPartExtension upper_wall_3_r1;
	private final ModelPartExtension upper_wall_2_r1;
	private final ModelPartExtension seat_end_2;
	private final ModelPartExtension seat_bottom_3_r1;
	private final ModelPartExtension seat_back_3_r1;
	private final ModelPartExtension seat_back_2_r1;
	private final ModelPartExtension seat_bottom_2_r1;
	private final ModelPartExtension end_exterior;
	private final ModelPartExtension upper_wall_2_r2;
	private final ModelPartExtension upper_wall_1_r1;
	private final ModelPartExtension seat;
	private final ModelPartExtension seat_back_2_r2;
	private final ModelPartExtension door_light;
	private final ModelPartExtension light_3_r1;
	private final ModelPartExtension side_panel;
	private final ModelPartExtension side_panel_translucent;
	private final ModelPartExtension roof_window_1;
	private final ModelPartExtension inner_roof_2_r1;
	private final ModelPartExtension roof_window_2;
	private final ModelPartExtension inner_roof_3_r1;
	private final ModelPartExtension roof_light;
	private final ModelPartExtension roof_light_r1;
	private final ModelPartExtension roof_door;
	private final ModelPartExtension inner_roof_3_r2;
	private final ModelPartExtension roof_end;
	private final ModelPartExtension side_1;
	private final ModelPartExtension inner_roof_4_r1;
	private final ModelPartExtension side_2;
	private final ModelPartExtension inner_roof_5_r1;
	private final ModelPartExtension roof_exterior;
	private final ModelPartExtension outer_roof_4_r1;
	private final ModelPartExtension outer_roof_3_r1;
	private final ModelPartExtension outer_roof_2_r1;
	private final ModelPartExtension outer_roof_1_r1;
	private final ModelPartExtension roof_end_exterior;
	private final ModelPartExtension side_3;
	private final ModelPartExtension outer_roof_8_r1;
	private final ModelPartExtension outer_roof_10_r1;
	private final ModelPartExtension outer_roof_11_r1;
	private final ModelPartExtension outer_roof_9_r1;
	private final ModelPartExtension side_4;
	private final ModelPartExtension outer_roof_7_r1;
	private final ModelPartExtension outer_roof_9_r2;
	private final ModelPartExtension outer_roof_10_r2;
	private final ModelPartExtension outer_roof_8_r2;
	private final ModelPartExtension top_handrail;
	private final ModelPartExtension pole_bottom_diagonal_2_r1;
	private final ModelPartExtension pole_bottom_diagonal_1_r1;
	private final ModelPartExtension pole_top_diagonal_2_r1;
	private final ModelPartExtension pole_top_diagonal_1_r1;
	private final ModelPartExtension top_handrail_connector_bottom_3_r1;
	private final ModelPartExtension top_handrail_bottom_right_r1;
	private final ModelPartExtension top_handrail_bottom_left_r1;
	private final ModelPartExtension top_handrail_right_4_r1;
	private final ModelPartExtension top_handrail_right_3_r1;
	private final ModelPartExtension top_handrail_left_4_r1;
	private final ModelPartExtension top_handrail_left_3_r1;
	private final ModelPartExtension handrail_straps;
	private final ModelPartExtension handrail_strap_8_r1;
	private final ModelPartExtension head;
	private final ModelPartExtension upper_wall_2_r3;
	private final ModelPartExtension upper_wall_1_r2;
	private final ModelPartExtension head_exterior;
	private final ModelPartExtension upper_wall_2_r4;
	private final ModelPartExtension upper_wall_1_r3;
	private final ModelPartExtension bottom_r1;
	private final ModelPartExtension floor_8_r1;
	private final ModelPartExtension floor_7_r1;
	private final ModelPartExtension front_side_1;
	private final ModelPartExtension front_side_lower_3_r1;
	private final ModelPartExtension front_side_upper_2_r1;
	private final ModelPartExtension front_side_upper_3_r1;
	private final ModelPartExtension front_side_upper_1_r1;
	private final ModelPartExtension front_side_2;
	private final ModelPartExtension front_side_lower_4_r1;
	private final ModelPartExtension front_side_upper_4_r1;
	private final ModelPartExtension front_panel;
	private final ModelPartExtension panel_6_r1;
	private final ModelPartExtension panel_5_r1;
	private final ModelPartExtension panel_4_r1;
	private final ModelPartExtension panel_4_r2;
	private final ModelPartExtension panel_3_r1;
	private final ModelPartExtension panel_3_r2;
	private final ModelPartExtension panel_2_r1;
	private final ModelPartExtension panel_2_r2;
	private final ModelPartExtension panel_1_r1;
	private final ModelPartExtension nose;
	private final ModelPartExtension nose_edge;
	private final ModelPartExtension edge_6_r1;
	private final ModelPartExtension edge_5_r1;
	private final ModelPartExtension edge_4_r1;
	private final ModelPartExtension edge_3_r1;
	private final ModelPartExtension edge_2_r1;
	private final ModelPartExtension edge_1_r1;
	private final ModelPartExtension nose_top;
	private final ModelPartExtension nose_top_3_r1;
	private final ModelPartExtension nose_top_2_r1;
	private final ModelPartExtension nose_top_1_r1;
	private final ModelPartExtension driver_door;
	private final ModelPartExtension driver_door_edge_upper_2_r1;
	private final ModelPartExtension driver_door_edge_roof_3_r1;
	private final ModelPartExtension roof_vent;
	private final ModelPartExtension vent_1_r1;
	private final ModelPartExtension vent_2_r1;
	private final ModelPartExtension roof_head_exterior;
	private final ModelPartExtension side_7;
	private final ModelPartExtension outer_roof_18_r1;
	private final ModelPartExtension outer_roof_17_r1;
	private final ModelPartExtension outer_roof_16_r1;
	private final ModelPartExtension outer_roof_15_r1;
	private final ModelPartExtension outer_roof_14_r1;
	private final ModelPartExtension outer_roof_9_r3;
	private final ModelPartExtension outer_roof_11_r2;
	private final ModelPartExtension outer_roof_12_r1;
	private final ModelPartExtension outer_roof_10_r3;
	private final ModelPartExtension side_8;
	private final ModelPartExtension outer_roof_19_r1;
	private final ModelPartExtension outer_roof_18_r2;
	private final ModelPartExtension outer_roof_17_r2;
	private final ModelPartExtension outer_roof_16_r2;
	private final ModelPartExtension outer_roof_15_r2;
	private final ModelPartExtension outer_roof_10_r4;
	private final ModelPartExtension outer_roof_12_r2;
	private final ModelPartExtension outer_roof_13_r1;
	private final ModelPartExtension outer_roof_11_r3;
	private final ModelPartExtension headlights;
	private final ModelPartExtension headlight_2_r1;
	private final ModelPartExtension headlight_1_r1;
	private final ModelPartExtension tail_lights;
	private final ModelPartExtension tail_light_2_r1;
	private final ModelPartExtension tail_light_1_r1;
	private final ModelPartExtension roof_head;
	private final ModelPartExtension side_5;
	private final ModelPartExtension inner_roof_5_r2;
	private final ModelPartExtension side_6;
	private final ModelPartExtension inner_roof_2_r2;
	private final ModelPartExtension door_light_on;
	private final ModelPartExtension light_r1;
	private final ModelPartExtension door_light_off;
	private final ModelPartExtension light_r2;
	private final ModelPartExtension end_handrail;
	private final ModelPartExtension pole_top_diagonal_1_r2;
	private final ModelPartExtension pole_top_diagonal_2_r2;
	private final ModelPartExtension pole_bottom_diagonal_1_r2;
	private final ModelPartExtension pole_bottom_diagonal_2_r2;

	public ModelRTrain() {
		this(DoorAnimationType.STANDARD, true);
	}

	protected ModelRTrain(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		super(336, 336, doorAnimationType, renderDoorOverlay);

		window = createModelPart();
		window.setPivot(0, 24, 0);
		window.setTextureUVOffset(0, 54).addCuboid(-20, 0, -24, 20, 1, 48, 0, false);
		window.setTextureUVOffset(140, 0).addCuboid(-20, -14, -26, 2, 14, 52, 0, false);

		upper_wall_r1 = createModelPart();
		upper_wall_r1.setPivot(-20, -14, 0);
		window.addChild(upper_wall_r1);
		setRotationAngle(upper_wall_r1, 0, 0, 0.1107F);
		upper_wall_r1.setTextureUVOffset(84, 51).addCuboid(0, -19, -26, 2, 19, 52, 0, false);

		window_exterior_1 = createModelPart();
		window_exterior_1.setPivot(0, 24, 0);
		window_exterior_1.setTextureUVOffset(144, 80).addCuboid(-21, 0, -24, 1, 9, 48, 0, false);
		window_exterior_1.setTextureUVOffset(140, 14).addCuboid(-19.999F, -14, -26, 0, 14, 52, 0, false);

		upper_wall_r2 = createModelPart();
		upper_wall_r2.setPivot(-34.4103F, -15.6011F, -7.8F);
		window_exterior_1.addChild(upper_wall_r2);
		setRotationAngle(upper_wall_r2, 0, 0, 0.1107F);
		upper_wall_r2.setTextureUVOffset(104, 124).addCuboid(14.5F, -18, -18.2F, 0, 18, 52, 0, false);

		window_exterior_2 = createModelPart();
		window_exterior_2.setPivot(0, 24, 0);
		window_exterior_2.setTextureUVOffset(144, 80).addCuboid(20, 0, -24, 1, 9, 48, 0, true);
		window_exterior_2.setTextureUVOffset(140, 14).addCuboid(19.999F, -14, -26, 0, 14, 52, 0, true);

		upper_wall_r3 = createModelPart();
		upper_wall_r3.setPivot(34.4103F, -15.6011F, -7.8F);
		window_exterior_2.addChild(upper_wall_r3);
		setRotationAngle(upper_wall_r3, 0, 0, -0.1107F);
		upper_wall_r3.setTextureUVOffset(104, 124).addCuboid(-14.5F, -18, -18.2F, 0, 18, 52, 0, true);

		door_exterior_1 = createModelPart();
		door_exterior_1.setPivot(0, 24, 0);
		door_exterior_1.setTextureUVOffset(196, 195).addCuboid(-21, 0, -16, 1, 9, 32, 0, false);

		door_left_exterior_1 = createModelPart();
		door_left_exterior_1.setPivot(0, 0, 0);
		door_exterior_1.addChild(door_left_exterior_1);
		door_left_exterior_1.setTextureUVOffset(283, 1).addCuboid(-20.8F, -14, 0, 1, 14, 15, 0, false);

		door_left_top_r1 = createModelPart();
		door_left_top_r1.setPivot(-20.8F, -14, 0);
		door_left_exterior_1.addChild(door_left_top_r1);
		setRotationAngle(door_left_top_r1, 0, 0, 0.1107F);
		door_left_top_r1.setTextureUVOffset(215, 273).addCuboid(0, -19, 0, 1, 19, 15, 0, false);

		door_right_exterior_1 = createModelPart();
		door_right_exterior_1.setPivot(0, 0, 0);
		door_exterior_1.addChild(door_right_exterior_1);
		door_right_exterior_1.setTextureUVOffset(1, 278).addCuboid(-20.8F, -14, -15, 1, 14, 15, 0, false);

		door_right_top_r1 = createModelPart();
		door_right_top_r1.setPivot(-20.8F, -14, 0);
		door_right_exterior_1.addChild(door_right_top_r1);
		setRotationAngle(door_right_top_r1, 0, 0, 0.1107F);
		door_right_top_r1.setTextureUVOffset(181, 273).addCuboid(0, -19, -15, 1, 19, 15, 0, false);

		door_exterior_2 = createModelPart();
		door_exterior_2.setPivot(0, 24, 0);
		door_exterior_2.setTextureUVOffset(196, 195).addCuboid(20, 0, -16, 1, 9, 32, 0, true);

		door_right_exterior_2 = createModelPart();
		door_right_exterior_2.setPivot(0, 0, 0);
		door_exterior_2.addChild(door_right_exterior_2);
		door_right_exterior_2.setTextureUVOffset(283, 1).addCuboid(19.8F, -14, 0, 1, 14, 15, 0, true);

		door_left_top_r2 = createModelPart();
		door_left_top_r2.setPivot(20.8F, -14, 0);
		door_right_exterior_2.addChild(door_left_top_r2);
		setRotationAngle(door_left_top_r2, 0, 0, -0.1107F);
		door_left_top_r2.setTextureUVOffset(215, 273).addCuboid(-1, -19, 0, 1, 19, 15, 0, true);

		door_left_exterior_2 = createModelPart();
		door_left_exterior_2.setPivot(0, 0, 0);
		door_exterior_2.addChild(door_left_exterior_2);
		door_left_exterior_2.setTextureUVOffset(1, 278).addCuboid(19.8F, -14, -15, 1, 14, 15, 0, true);

		door_right_top_r2 = createModelPart();
		door_right_top_r2.setPivot(20.8F, -14, 0);
		door_left_exterior_2.addChild(door_right_top_r2);
		setRotationAngle(door_right_top_r2, 0, 0, -0.1107F);
		door_right_top_r2.setTextureUVOffset(181, 273).addCuboid(-1, -19, -15, 1, 19, 15, 0, true);

		door = createModelPart();
		door.setPivot(0, 24, 0);
		door.setTextureUVOffset(0, 194).addCuboid(-20, 0, -16, 20, 1, 32, 0, false);
		door.setTextureUVOffset(96, 163).addCuboid(-4, -37.25F, -4, 4, 1, 8, 0, false);
		door.setTextureUVOffset(69, 164).addCuboid(-3, -37.25F, -3, 3, 0, 6, 0, false);

		door_left = createModelPart();
		door_left.setPivot(0, 0, 0);
		door.addChild(door_left);
		door_left.setTextureUVOffset(173, 196).addCuboid(-19.8F, -14, 0, 0, 14, 15, 0, false);

		door_left_top_r3 = createModelPart();
		door_left_top_r3.setPivot(-20.8F, -14, 0);
		door_left.addChild(door_left_top_r3);
		setRotationAngle(door_left_top_r3, 0, 0, 0.1107F);
		door_left_top_r3.setTextureUVOffset(73, 179).addCuboid(1, -19, 0, 0, 19, 15, 0, false);

		door_right = createModelPart();
		door_right.setPivot(0, 0, 0);
		door.addChild(door_right);
		door_right.setTextureUVOffset(119, 196).addCuboid(-19.8F, -14, -15, 0, 14, 15, 0, false);

		door_right_top_r3 = createModelPart();
		door_right_top_r3.setPivot(-20.8F, -14, 0);
		door_right.addChild(door_right_top_r3);
		setRotationAngle(door_right_top_r3, 0, 0, 0.1107F);
		door_right_top_r3.setTextureUVOffset(1, 179).addCuboid(1, -19, -15, 0, 19, 15, 0, false);

		end = createModelPart();
		end.setPivot(0, 24, 0);
		end.setTextureUVOffset(188, 174).addCuboid(-20, 0, -12, 40, 1, 20, 0, false);
		end.setTextureUVOffset(224, 236).addCuboid(-20, -14, -12, 2, 14, 22, 0, false);
		end.setTextureUVOffset(47, 275).addCuboid(-19, -33, -12, 9, 33, 5, 0, false);
		end.setTextureUVOffset(47, 275).addCuboid(10, -33, -12, 9, 33, 5, 0, true);
		end.setTextureUVOffset(224, 236).addCuboid(18, -14, -12, 2, 14, 22, 0, true);
		end.setTextureUVOffset(144, 164).addCuboid(-13, -38, -12, 26, 5, 5, 0, false);

		upper_wall_3_r1 = createModelPart();
		upper_wall_3_r1.setPivot(20, -14, 24);
		end.addChild(upper_wall_3_r1);
		setRotationAngle(upper_wall_3_r1, 0, 0, -0.1107F);
		upper_wall_3_r1.setTextureUVOffset(0, 103).addCuboid(-2, -19, -36, 2, 19, 22, 0, true);

		upper_wall_2_r1 = createModelPart();
		upper_wall_2_r1.setPivot(-20, -14, 24);
		end.addChild(upper_wall_2_r1);
		setRotationAngle(upper_wall_2_r1, 0, 0, 0.1107F);
		upper_wall_2_r1.setTextureUVOffset(0, 103).addCuboid(0, -19, -36, 2, 19, 22, 0, false);


		seat_end_2 = createModelPart();
		seat_end_2.setPivot(0, 0, 24);
		end.addChild(seat_end_2);


		seat_bottom_3_r1 = createModelPart();
		seat_bottom_3_r1.setPivot(0, 0, 0);
		seat_end_2.addChild(seat_bottom_3_r1);
		setRotationAngle(seat_bottom_3_r1, 0, -3.1416F, 0);
		seat_bottom_3_r1.setTextureUVOffset(280, 68).addCuboid(-18, -6, 15, 7, 1, 16, 0, true);

		seat_back_3_r1 = createModelPart();
		seat_back_3_r1.setPivot(18, -6.5F, 0);
		seat_end_2.addChild(seat_back_3_r1);
		setRotationAngle(seat_back_3_r1, 0, -3.1416F, 0.1047F);
		seat_back_3_r1.setTextureUVOffset(68, 235).addCuboid(0, -6, 15, 1, 4, 16, 0, true);

		seat_back_2_r1 = createModelPart();
		seat_back_2_r1.setPivot(-18, -6.5F, 0);
		seat_end_2.addChild(seat_back_2_r1);
		setRotationAngle(seat_back_2_r1, 0, 3.1416F, -0.1047F);
		seat_back_2_r1.setTextureUVOffset(68, 235).addCuboid(-1, -6, 15, 1, 4, 16, 0, false);

		seat_bottom_2_r1 = createModelPart();
		seat_bottom_2_r1.setPivot(0, 0, 0);
		seat_end_2.addChild(seat_bottom_2_r1);
		setRotationAngle(seat_bottom_2_r1, 0, 3.1416F, 0);
		seat_bottom_2_r1.setTextureUVOffset(280, 68).addCuboid(11, -6, 15, 7, 1, 16, 0, false);

		end_exterior = createModelPart();
		end_exterior.setPivot(0, 24, 0);
		end_exterior.setTextureUVOffset(270, 39).addCuboid(20, 0, -12, 1, 9, 20, 0, false);
		end_exterior.setTextureUVOffset(270, 39).addCuboid(-21, 0, -12, 1, 9, 20, 0, true);
		end_exterior.setTextureUVOffset(176, 236).addCuboid(18, -14, -12, 2, 14, 22, 0, false);
		end_exterior.setTextureUVOffset(0, 0).addCuboid(10, -33, -12, 8, 33, 0, 0, false);
		end_exterior.setTextureUVOffset(0, 0).addCuboid(-18, -33, -12, 8, 33, 0, 0, true);
		end_exterior.setTextureUVOffset(84, 34).addCuboid(-18, -45, -12, 36, 12, 0, 0, false);
		end_exterior.setTextureUVOffset(176, 236).addCuboid(-20, -14, -12, 2, 14, 22, 0, true);

		upper_wall_2_r2 = createModelPart();
		upper_wall_2_r2.setPivot(-20, -14, 24);
		end_exterior.addChild(upper_wall_2_r2);
		setRotationAngle(upper_wall_2_r2, 0, 0, 0.1107F);
		upper_wall_2_r2.setTextureUVOffset(90, 235).addCuboid(0, -18, -36, 2, 18, 22, 0, true);

		upper_wall_1_r1 = createModelPart();
		upper_wall_1_r1.setPivot(20, -14, 24);
		end_exterior.addChild(upper_wall_1_r1);
		setRotationAngle(upper_wall_1_r1, 0, 0, -0.1107F);
		upper_wall_1_r1.setTextureUVOffset(90, 235).addCuboid(-2, -18, -36, 2, 18, 22, 0, false);

		seat = createModelPart();
		seat.setPivot(0, 24, 0);
		seat.setTextureUVOffset(64, 194).addCuboid(-18, -6, -20, 7, 1, 40, 0, false);

		seat_back_2_r2 = createModelPart();
		seat_back_2_r2.setPivot(-18, -6.5F, 0.5F);
		seat.addChild(seat_back_2_r2);
		setRotationAngle(seat_back_2_r2, 0, 0, -0.0873F);
		seat_back_2_r2.setTextureUVOffset(269, 150).addCuboid(0, -6, -0.5F, 1, 4, 20, 0, false);
		seat_back_2_r2.setTextureUVOffset(269, 150).addCuboid(0, -6, -20.5F, 1, 4, 20, 0, false);

		door_light = createModelPart();
		door_light.setPivot(0, 24, 0);
		door_light.setTextureUVOffset(30, 45).addCuboid(-3, -37.25F, -3, 3, 1, 0, 0, false);
		door_light.setTextureUVOffset(0, 41).addCuboid(-3, -37.25F, -3, 0, 1, 6, 0, false);

		light_3_r1 = createModelPart();
		light_3_r1.setPivot(-1.5F, -36.75F, 3);
		door_light.addChild(light_3_r1);
		setRotationAngle(light_3_r1, 0, 3.1416F, 0);
		light_3_r1.setTextureUVOffset(21, 27).addCuboid(-1.5F, -0.5F, 0, 3, 1, 0, 0, false);

		side_panel = createModelPart();
		side_panel.setPivot(0, 24, 0);
		side_panel.setTextureUVOffset(242, 126).addCuboid(-18, -29, 0, 7, 24, 0, 0, false);

		side_panel_translucent = createModelPart();
		side_panel_translucent.setPivot(0, 23, 0);
		side_panel_translucent.setTextureUVOffset(140, 80).addCuboid(-18, -28, 0, 6, 22, 0, 0, false);

		roof_window_1 = createModelPart();
		roof_window_1.setPivot(0, 24, 0);
		roof_window_1.setTextureUVOffset(122, 0).addCuboid(-16.0123F, -32.1399F, -26, 3, 0, 52, 0, false);
		roof_window_1.setTextureUVOffset(88, 122).addCuboid(-10.1444F, -36.2357F, -26, 2, 0, 52, 0, false);
		roof_window_1.setTextureUVOffset(18, 103).addCuboid(-6, -37.25F, -26, 6, 0, 52, 0, false);

		inner_roof_2_r1 = createModelPart();
		inner_roof_2_r1.setPivot(-13.0123F, -32.1409F, 26);
		roof_window_1.addChild(inner_roof_2_r1);
		setRotationAngle(inner_roof_2_r1, 0, 0, -0.9599F);
		inner_roof_2_r1.setTextureUVOffset(112, 0).addCuboid(0, 0.001F, -52, 5, 0, 52, 0, false);

		roof_window_2 = createModelPart();
		roof_window_2.setPivot(0, 24, 0);
		roof_window_2.setTextureUVOffset(122, 0).addCuboid(13.0123F, -32.1399F, -26, 3, 0, 52, 0, true);
		roof_window_2.setTextureUVOffset(88, 122).addCuboid(8.1444F, -36.2357F, -26, 2, 0, 52, 0, true);
		roof_window_2.setTextureUVOffset(18, 103).addCuboid(0, -37.25F, -26, 6, 0, 52, 0, true);

		inner_roof_3_r1 = createModelPart();
		inner_roof_3_r1.setPivot(13.0123F, -32.1409F, 26);
		roof_window_2.addChild(inner_roof_3_r1);
		setRotationAngle(inner_roof_3_r1, 0, 0, 0.9599F);
		inner_roof_3_r1.setTextureUVOffset(128, 0).addCuboid(-5, 0.001F, -52, 5, 0, 52, 0, true);

		roof_light = createModelPart();
		roof_light.setPivot(0, 24, 0);


		roof_light_r1 = createModelPart();
		roof_light_r1.setPivot(-8.1444F, -36.2367F, 26);
		roof_light.addChild(roof_light_r1);
		setRotationAngle(roof_light_r1, 0, 0, -0.4102F);
		roof_light_r1.setTextureUVOffset(40, 54).addCuboid(0, 0.001F, -50, 3, 0, 48, 0, false);

		roof_door = createModelPart();
		roof_door.setPivot(0, 24, 0);
		roof_door.setTextureUVOffset(88, 54).addCuboid(-18.0123F, -32.1399F, -14, 5, 0, 28, 0, false);
		roof_door.setTextureUVOffset(98, 54).addCuboid(-10.1444F, -36.2357F, -14, 2, 0, 28, 0, false);
		roof_door.setTextureUVOffset(0, 0).addCuboid(-6, -37.25F, -14, 6, 0, 28, 0, false);

		inner_roof_3_r2 = createModelPart();
		inner_roof_3_r2.setPivot(-13.0123F, -32.1409F, 26);
		roof_door.addChild(inner_roof_3_r2);
		setRotationAngle(inner_roof_3_r2, 0, 0, -0.9599F);
		inner_roof_3_r2.setTextureUVOffset(0, 54).addCuboid(0, 0.001F, -40, 5, 0, 28, 0, false);

		roof_end = createModelPart();
		roof_end.setPivot(0, 24, 0);


		side_1 = createModelPart();
		side_1.setPivot(0, 0, 0);
		roof_end.addChild(side_1);
		side_1.setTextureUVOffset(0, 17).addCuboid(-10.1444F, -36.2357F, -7, 2, 0, 17, 0, false);
		side_1.setTextureUVOffset(13, 28).addCuboid(-16.0123F, -32.1399F, -7, 3, 0, 17, 0, false);
		side_1.setTextureUVOffset(99, 82).addCuboid(-6, -37.25F, -7, 6, 0, 17, 0, false);

		inner_roof_4_r1 = createModelPart();
		inner_roof_4_r1.setPivot(-13.0123F, -32.1409F, 26);
		side_1.addChild(inner_roof_4_r1);
		setRotationAngle(inner_roof_4_r1, 0, 0, -0.9599F);
		inner_roof_4_r1.setTextureUVOffset(0, 0).addCuboid(0, 0.001F, -33, 5, 0, 17, 0, false);

		side_2 = createModelPart();
		side_2.setPivot(0, 0, 0);
		roof_end.addChild(side_2);
		side_2.setTextureUVOffset(0, 17).addCuboid(8.1444F, -36.2357F, -7, 2, 0, 17, 0, true);
		side_2.setTextureUVOffset(13, 28).addCuboid(13.0123F, -32.1399F, -7, 3, 0, 17, 0, true);
		side_2.setTextureUVOffset(99, 82).addCuboid(0, -37.25F, -7, 6, 0, 17, 0, true);

		inner_roof_5_r1 = createModelPart();
		inner_roof_5_r1.setPivot(10.1444F, -36.2367F, 26);
		side_2.addChild(inner_roof_5_r1);
		setRotationAngle(inner_roof_5_r1, 0, 0, 0.9599F);
		inner_roof_5_r1.setTextureUVOffset(0, 0).addCuboid(0, 0.001F, -33, 5, 0, 17, 0, true);

		roof_exterior = createModelPart();
		roof_exterior.setPivot(0, 24, 0);
		roof_exterior.setTextureUVOffset(0, 0).addCuboid(-5.9859F, -44.6423F, -20, 6, 0, 40, 0, false);

		outer_roof_4_r1 = createModelPart();
		outer_roof_4_r1.setPivot(-8.9401F, -44.1214F, -4);
		roof_exterior.addChild(outer_roof_4_r1);
		setRotationAngle(outer_roof_4_r1, 0, 0, -0.1745F);
		outer_roof_4_r1.setTextureUVOffset(54, 54).addCuboid(-4, 0, -16, 7, 0, 40, 0, false);

		outer_roof_3_r1 = createModelPart();
		outer_roof_3_r1.setPivot(-14.178F, -42.6769F, -4);
		roof_exterior.addChild(outer_roof_3_r1);
		setRotationAngle(outer_roof_3_r1, 0, 0, -0.5236F);
		outer_roof_3_r1.setTextureUVOffset(0, 54).addCuboid(-2.5F, 0, -16, 4, 0, 40, 0, false);

		outer_roof_2_r1 = createModelPart();
		outer_roof_2_r1.setPivot(-17.3427F, -39.6952F, -4);
		roof_exterior.addChild(outer_roof_2_r1);
		setRotationAngle(outer_roof_2_r1, 0, 0, -1.0472F);
		outer_roof_2_r1.setTextureUVOffset(68, 54).addCuboid(-2, 0, -16, 4, 0, 40, 0, false);

		outer_roof_1_r1 = createModelPart();
		outer_roof_1_r1.setPivot(-20, -14, -4);
		roof_exterior.addChild(outer_roof_1_r1);
		setRotationAngle(outer_roof_1_r1, 0, 0, 0.1107F);
		outer_roof_1_r1.setTextureUVOffset(194, 80).addCuboid(-1, -24, -16, 1, 6, 40, 0, false);

		roof_end_exterior = createModelPart();
		roof_end_exterior.setPivot(0, 24, 0);


		side_3 = createModelPart();
		side_3.setPivot(0, 0, 0);
		roof_end_exterior.addChild(side_3);
		side_3.setTextureUVOffset(250, 222).addCuboid(-5.9859F, -44.6423F, -12, 6, 1, 22, 0, true);

		outer_roof_8_r1 = createModelPart();
		outer_roof_8_r1.setPivot(-20.9939F, -14.1104F, -4);
		side_3.addChild(outer_roof_8_r1);
		setRotationAngle(outer_roof_8_r1, 0, 0, 0.1107F);
		outer_roof_8_r1.setTextureUVOffset(0, 72).addCuboid(0, -24, -8, 1, 6, 22, 0, true);

		outer_roof_10_r1 = createModelPart();
		outer_roof_10_r1.setPivot(-15.0444F, -42.1768F, -4);
		side_3.addChild(outer_roof_10_r1);
		setRotationAngle(outer_roof_10_r1, 0, 0, -0.5236F);
		outer_roof_10_r1.setTextureUVOffset(0, 28).addCuboid(-1.5F, 0, -8, 4, 1, 22, 0, true);

		outer_roof_11_r1 = createModelPart();
		outer_roof_11_r1.setPivot(-9.9249F, -43.9477F, -4);
		side_3.addChild(outer_roof_11_r1);
		setRotationAngle(outer_roof_11_r1, 0, 0, -0.1745F);
		outer_roof_11_r1.setTextureUVOffset(246, 16).addCuboid(-3, 0, -8, 7, 1, 22, 0, true);

		outer_roof_9_r1 = createModelPart();
		outer_roof_9_r1.setPivot(-17.3427F, -39.6952F, -4);
		side_3.addChild(outer_roof_9_r1);
		setRotationAngle(outer_roof_9_r1, 0, 0, -1.0472F);
		outer_roof_9_r1.setTextureUVOffset(140, 80).addCuboid(-2, 0, -8, 4, 1, 22, 0, true);

		side_4 = createModelPart();
		side_4.setPivot(0, 0, 0);
		roof_end_exterior.addChild(side_4);
		side_4.setTextureUVOffset(250, 222).addCuboid(-0.0141F, -44.6423F, -12, 6, 1, 22, 0, false);

		outer_roof_7_r1 = createModelPart();
		outer_roof_7_r1.setPivot(20.9939F, -14.1104F, -4);
		side_4.addChild(outer_roof_7_r1);
		setRotationAngle(outer_roof_7_r1, 0, 0, -0.1107F);
		outer_roof_7_r1.setTextureUVOffset(0, 72).addCuboid(-1, -24, -8, 1, 6, 22, 0, false);

		outer_roof_9_r2 = createModelPart();
		outer_roof_9_r2.setPivot(15.0444F, -42.1768F, -4);
		side_4.addChild(outer_roof_9_r2);
		setRotationAngle(outer_roof_9_r2, 0, 0, 0.5236F);
		outer_roof_9_r2.setTextureUVOffset(0, 28).addCuboid(-2.5F, 0, -8, 4, 1, 22, 0, false);

		outer_roof_10_r2 = createModelPart();
		outer_roof_10_r2.setPivot(9.9249F, -43.9477F, -4);
		side_4.addChild(outer_roof_10_r2);
		setRotationAngle(outer_roof_10_r2, 0, 0, 0.1745F);
		outer_roof_10_r2.setTextureUVOffset(246, 16).addCuboid(-4, 0, -8, 7, 1, 22, 0, false);

		outer_roof_8_r2 = createModelPart();
		outer_roof_8_r2.setPivot(17.3427F, -39.6952F, -4);
		side_4.addChild(outer_roof_8_r2);
		setRotationAngle(outer_roof_8_r2, 0, 0, 1.0472F);
		outer_roof_8_r2.setTextureUVOffset(140, 80).addCuboid(-2, 0, -8, 4, 1, 22, 0, false);

		top_handrail = createModelPart();
		top_handrail.setPivot(0, 24, 0);
		top_handrail.setTextureUVOffset(330, 0).addCuboid(-3, -33, 33.166F, 3, 0, 0, 0.2F, false);
		top_handrail.setTextureUVOffset(330, 0).addCuboid(-3, -33, -33.166F, 3, 0, 0, 0.2F, false);
		top_handrail.setTextureUVOffset(319, 0).addCuboid(-5.3453F, -37.249F, -22, 0, 2, 0, 0.2F, false);
		top_handrail.setTextureUVOffset(319, 0).addCuboid(-5.3453F, -37.249F, 0, 0, 2, 0, 0.2F, false);
		top_handrail.setTextureUVOffset(319, 0).addCuboid(-5.3453F, -37.249F, 22, 0, 2, 0, 0.2F, false);
		top_handrail.setTextureUVOffset(335, 0).addCuboid(0, -38, 33.166F, 0, 5, 0, 0.2F, false);
		top_handrail.setTextureUVOffset(335, 0).addCuboid(0, -38, 11, 0, 9, 0, 0.2F, false);
		top_handrail.setTextureUVOffset(332, 13).addCuboid(0, -23.2645F, 10.437F, 0, 6, 0, 0.2F, false);
		top_handrail.setTextureUVOffset(332, 13).addCuboid(0, -23.2645F, 11.563F, 0, 6, 0, 0.2F, false);
		top_handrail.setTextureUVOffset(335, 0).addCuboid(0, -12, 11, 0, 12, 0, 0.2F, false);

		pole_bottom_diagonal_2_r1 = createModelPart();
		pole_bottom_diagonal_2_r1.setPivot(0, -14.4002F, 11.2819F);
		top_handrail.addChild(pole_bottom_diagonal_2_r1);
		setRotationAngle(pole_bottom_diagonal_2_r1, -0.1047F, 0, 0);
		pole_bottom_diagonal_2_r1.setTextureUVOffset(335, 0).addCuboid(0, -2.5F, 0, 0, 5, 0, 0.2F, false);

		pole_bottom_diagonal_1_r1 = createModelPart();
		pole_bottom_diagonal_1_r1.setPivot(0, -14.4002F, 10.7181F);
		top_handrail.addChild(pole_bottom_diagonal_1_r1);
		setRotationAngle(pole_bottom_diagonal_1_r1, 0.1047F, 0, 0);
		pole_bottom_diagonal_1_r1.setTextureUVOffset(335, 0).addCuboid(0, -2.5F, 0, 0, 5, 0, 0.2F, false);

		pole_top_diagonal_2_r1 = createModelPart();
		pole_top_diagonal_2_r1.setPivot(0.2F, -28.8F, 10.8F);
		top_handrail.addChild(pole_top_diagonal_2_r1);
		setRotationAngle(pole_top_diagonal_2_r1, 0.1047F, 0, 0);
		pole_top_diagonal_2_r1.setTextureUVOffset(335, 0).addCuboid(-0.2F, 0.2069F, 0.2F, 0, 5, 0, 0.2F, false);

		pole_top_diagonal_1_r1 = createModelPart();
		pole_top_diagonal_1_r1.setPivot(0.2F, -28.8F, 11.2F);
		top_handrail.addChild(pole_top_diagonal_1_r1);
		setRotationAngle(pole_top_diagonal_1_r1, -0.1047F, 0, 0);
		pole_top_diagonal_1_r1.setTextureUVOffset(335, 0).addCuboid(-0.2F, 0.2069F, -0.2F, 0, 5, 0, 0.2F, false);

		top_handrail_connector_bottom_3_r1 = createModelPart();
		top_handrail_connector_bottom_3_r1.setPivot(-6.4333F, -32.9847F, -17.7F);
		top_handrail.addChild(top_handrail_connector_bottom_3_r1);
		setRotationAngle(top_handrail_connector_bottom_3_r1, 0, 0, 0.3927F);
		top_handrail_connector_bottom_3_r1.setTextureUVOffset(319, 0).addCuboid(0.2F, -2.2F, 39.7F, 0, 2, 0, 0.2F, false);
		top_handrail_connector_bottom_3_r1.setTextureUVOffset(319, 0).addCuboid(0.2F, -2.2F, 17.7F, 0, 2, 0, 0.2F, false);
		top_handrail_connector_bottom_3_r1.setTextureUVOffset(319, 0).addCuboid(0.2F, -2.2F, -4.3F, 0, 2, 0, 0.2F, false);

		top_handrail_bottom_right_r1 = createModelPart();
		top_handrail_bottom_right_r1.setPivot(-6.9124F, -31, 7.1125F);
		top_handrail.addChild(top_handrail_bottom_right_r1);
		setRotationAngle(top_handrail_bottom_right_r1, 1.5708F, 0, 0);
		top_handrail_bottom_right_r1.setTextureUVOffset(335, 0).addCuboid(0.6339F, -37, 2, 0, 30, 0, 0.2F, false);

		top_handrail_bottom_left_r1 = createModelPart();
		top_handrail_bottom_left_r1.setPivot(-6.9124F, -31, 6.8876F);
		top_handrail.addChild(top_handrail_bottom_left_r1);
		setRotationAngle(top_handrail_bottom_left_r1, -1.5708F, 0, 0);
		top_handrail_bottom_left_r1.setTextureUVOffset(335, 0).addCuboid(0.6339F, -23, -2, 0, 30, 0, 0.2F, false);

		top_handrail_right_4_r1 = createModelPart();
		top_handrail_right_4_r1.setPivot(-5.9553F, -31, -30.5938F);
		top_handrail.addChild(top_handrail_right_4_r1);
		setRotationAngle(top_handrail_right_4_r1, 1.5708F, -0.5236F, 0);
		top_handrail_right_4_r1.setTextureUVOffset(319, 0).addCuboid(0, -1.5F, 2, 0, 2, 0, 0.2F, false);

		top_handrail_right_3_r1 = createModelPart();
		top_handrail_right_3_r1.setPivot(-4.5723F, -31, -32.3428F);
		top_handrail.addChild(top_handrail_right_3_r1);
		setRotationAngle(top_handrail_right_3_r1, 1.5708F, -1.0472F, 0);
		top_handrail_right_3_r1.setTextureUVOffset(319, 0).addCuboid(0, -1.5F, 2, 0, 2, 0, 0.2F, false);

		top_handrail_left_4_r1 = createModelPart();
		top_handrail_left_4_r1.setPivot(-5.9553F, -31, 30.5938F);
		top_handrail.addChild(top_handrail_left_4_r1);
		setRotationAngle(top_handrail_left_4_r1, -1.5708F, 0.5236F, 0);
		top_handrail_left_4_r1.setTextureUVOffset(319, 0).addCuboid(0, -1.5F, -2, 0, 2, 0, 0.2F, false);

		top_handrail_left_3_r1 = createModelPart();
		top_handrail_left_3_r1.setPivot(-4.5723F, -31, 32.3428F);
		top_handrail.addChild(top_handrail_left_3_r1);
		setRotationAngle(top_handrail_left_3_r1, -1.5708F, 1.0472F, 0);
		top_handrail_left_3_r1.setTextureUVOffset(319, 0).addCuboid(0, -1.5F, -2, 0, 2, 0, 0.2F, false);

		handrail_straps = createModelPart();
		handrail_straps.setPivot(0, 0, 0);
		top_handrail.addChild(handrail_straps);
		handrail_straps.setTextureUVOffset(36, 42).addCuboid(-7.25F, -34, -20, 2, 4, 0, 0, false);
		handrail_straps.setTextureUVOffset(36, 42).addCuboid(-7.25F, -34, -11, 2, 4, 0, 0, false);
		handrail_straps.setTextureUVOffset(36, 42).addCuboid(-7.25F, -34, -2, 2, 4, 0, 0, false);
		handrail_straps.setTextureUVOffset(36, 42).addCuboid(-7.25F, -34, 2, 2, 4, 0, 0, false);
		handrail_straps.setTextureUVOffset(36, 42).addCuboid(-7.25F, -34, 11, 2, 4, 0, 0, false);
		handrail_straps.setTextureUVOffset(36, 42).addCuboid(-7.25F, -34, 20, 2, 4, 0, 0, false);

		handrail_strap_8_r1 = createModelPart();
		handrail_strap_8_r1.setPivot(0, 0, 0);
		handrail_straps.addChild(handrail_strap_8_r1);
		setRotationAngle(handrail_strap_8_r1, 0, -1.5708F, 0);
		handrail_strap_8_r1.setTextureUVOffset(36, 42).addCuboid(-34.166F, -34, 3, 2, 4, 0, 0, false);
		handrail_strap_8_r1.setTextureUVOffset(36, 42).addCuboid(32.166F, -34, 3, 2, 4, 0, 0, false);

		head = createModelPart();
		head.setPivot(0, 24, 0);
		head.setTextureUVOffset(118, 195).addCuboid(-20, 0, -7, 40, 1, 15, 0, false);
		head.setTextureUVOffset(292, 30).addCuboid(18, -14, -4, 2, 14, 14, 0, false);
		head.setTextureUVOffset(292, 30).addCuboid(-20, -14, -4, 2, 14, 14, 0, true);
		head.setTextureUVOffset(196, 0).addCuboid(-18, -38, -4, 36, 38, 0, 0, false);

		upper_wall_2_r3 = createModelPart();
		upper_wall_2_r3.setPivot(-20, -14, 0);
		head.addChild(upper_wall_2_r3);
		setRotationAngle(upper_wall_2_r3, 0, 0, 0.1107F);
		upper_wall_2_r3.setTextureUVOffset(75, 275).addCuboid(0, -19, -4, 2, 19, 14, 0, true);

		upper_wall_1_r2 = createModelPart();
		upper_wall_1_r2.setPivot(20, -14, 0);
		head.addChild(upper_wall_1_r2);
		setRotationAngle(upper_wall_1_r2, 0, 0, -0.1107F);
		upper_wall_1_r2.setTextureUVOffset(75, 275).addCuboid(-2, -19, -4, 2, 19, 14, 0, false);

		head_exterior = createModelPart();
		head_exterior.setPivot(0, 24, 0);
		head_exterior.setTextureUVOffset(230, 195).addCuboid(20, 0, -4, 1, 9, 12, 0, false);
		head_exterior.setTextureUVOffset(144, 137).addCuboid(-20, 0, -25, 40, 9, 18, 0, false);
		head_exterior.setTextureUVOffset(230, 195).addCuboid(-21, 0, -4, 1, 9, 12, 0, true);
		head_exterior.setTextureUVOffset(247, 272).addCuboid(-18, -44, -7, 36, 44, 0, 0, false);
		head_exterior.setTextureUVOffset(82, 122).addCuboid(18, -14, -8, 2, 14, 18, 0, false);
		head_exterior.setTextureUVOffset(82, 122).addCuboid(-20, -14, -8, 2, 14, 18, 0, true);

		upper_wall_2_r4 = createModelPart();
		upper_wall_2_r4.setPivot(-20, -14, 24);
		head_exterior.addChild(upper_wall_2_r4);
		setRotationAngle(upper_wall_2_r4, 0, 0, 0.1107F);
		upper_wall_2_r4.setTextureUVOffset(194, 80).addCuboid(0, -18, -32, 2, 18, 18, 0, true);

		upper_wall_1_r3 = createModelPart();
		upper_wall_1_r3.setPivot(20, -14, 24);
		head_exterior.addChild(upper_wall_1_r3);
		setRotationAngle(upper_wall_1_r3, 0, 0, -0.1107F);
		upper_wall_1_r3.setTextureUVOffset(194, 80).addCuboid(-2, -18, -32, 2, 18, 18, 0, false);

		bottom_r1 = createModelPart();
		bottom_r1.setPivot(-20, 7.263F, -15.3805F);
		head_exterior.addChild(bottom_r1);
		setRotationAngle(bottom_r1, -0.0873F, 0, 0);
		bottom_r1.setTextureUVOffset(50, 0).addCuboid(0, 1, -25.5F, 40, 0, 34, 0, false);

		floor_8_r1 = createModelPart();
		floor_8_r1.setPivot(-23.4373F, 3.5F, 4.1584F);
		head_exterior.addChild(floor_8_r1);
		setRotationAngle(floor_8_r1, 0, -0.3491F, 0);
		floor_8_r1.setTextureUVOffset(112, 157).addCuboid(-0.5F, -3.5F, -12.5F, 1, 9, 4, 0, true);

		floor_7_r1 = createModelPart();
		floor_7_r1.setPivot(23.4373F, 3.5F, 4.1584F);
		head_exterior.addChild(floor_7_r1);
		setRotationAngle(floor_7_r1, 0, 0.3491F, 0);
		floor_7_r1.setTextureUVOffset(112, 157).addCuboid(-0.5F, -3.5F, -12.5F, 1, 9, 4, 0, false);

		front_side_1 = createModelPart();
		front_side_1.setPivot(0, 0, 0);
		head_exterior.addChild(front_side_1);
		front_side_1.setTextureUVOffset(0, 49).addCuboid(20, -14, -25, 0, 14, 5, 0, false);
		front_side_1.setTextureUVOffset(0, 49).addCuboid(-20, -14, -25, 0, 14, 5, 0, true);

		front_side_lower_3_r1 = createModelPart();
		front_side_lower_3_r1.setPivot(20, 0, -25);
		front_side_1.addChild(front_side_lower_3_r1);
		setRotationAngle(front_side_lower_3_r1, 0, 0.2182F, 0);
		front_side_lower_3_r1.setTextureUVOffset(0, 59).addCuboid(0, -14, -9, 0, 22, 9, 0, false);

		front_side_upper_2_r1 = createModelPart();
		front_side_upper_2_r1.setPivot(-19.8274F, -13.9808F, -16.0152F);
		front_side_1.addChild(front_side_upper_2_r1);
		setRotationAngle(front_side_upper_2_r1, 0, 0, 0.1107F);
		front_side_upper_2_r1.setTextureUVOffset(152, 75).addCuboid(-0.1736F, -18, -8.9848F, 0, 18, 5, 0, true);

		front_side_upper_3_r1 = createModelPart();
		front_side_upper_3_r1.setPivot(20, -14, -25);
		front_side_1.addChild(front_side_upper_3_r1);
		setRotationAngle(front_side_upper_3_r1, 0, 0.2182F, -0.1107F);
		front_side_upper_3_r1.setTextureUVOffset(138, 231).addCuboid(0, -24, -7, 0, 24, 7, 0, false);

		front_side_upper_1_r1 = createModelPart();
		front_side_upper_1_r1.setPivot(19.8274F, -13.9808F, -16.0152F);
		front_side_1.addChild(front_side_upper_1_r1);
		setRotationAngle(front_side_upper_1_r1, 0, 0, -0.1107F);
		front_side_upper_1_r1.setTextureUVOffset(152, 75).addCuboid(0.1736F, -18, -8.9848F, 0, 18, 5, 0, false);

		front_side_2 = createModelPart();
		front_side_2.setPivot(0, 0, 0);
		head_exterior.addChild(front_side_2);


		front_side_lower_4_r1 = createModelPart();
		front_side_lower_4_r1.setPivot(-20, 7, -24.9988F);
		front_side_2.addChild(front_side_lower_4_r1);
		setRotationAngle(front_side_lower_4_r1, 0, -0.2182F, 0);
		front_side_lower_4_r1.setTextureUVOffset(0, 59).addCuboid(0.001F, -21, -9, 0, 22, 9, 0, false);

		front_side_upper_4_r1 = createModelPart();
		front_side_upper_4_r1.setPivot(-19.999F, -13.9999F, -24.9988F);
		front_side_2.addChild(front_side_upper_4_r1);
		setRotationAngle(front_side_upper_4_r1, 0, -0.2182F, 0.1107F);
		front_side_upper_4_r1.setTextureUVOffset(138, 231).addCuboid(0, -24, -7, 0, 24, 7, 0, false);

		front_panel = createModelPart();
		front_panel.setPivot(0, 0, 0);
		head_exterior.addChild(front_panel);


		panel_6_r1 = createModelPart();
		panel_6_r1.setPivot(-7.5573F, -13.881F, -33.7427F);
		front_panel.addChild(panel_6_r1);
		setRotationAngle(panel_6_r1, -0.1309F, 0, 0);
		panel_6_r1.setTextureUVOffset(0, 144).addCuboid(0, 0, 0, 15, 11, 0, 0, false);

		panel_5_r1 = createModelPart();
		panel_5_r1.setPivot(-7.2465F, -13.881F, -33.7767F);
		front_panel.addChild(panel_5_r1);
		setRotationAngle(panel_5_r1, -0.1309F, 0.2182F, 0);
		panel_5_r1.setTextureUVOffset(72, 213).addCuboid(-12, 0, 0, 12, 11, 0, 0, false);

		panel_4_r1 = createModelPart();
		panel_4_r1.setPivot(7.132F, -13.881F, -33.7767F);
		front_panel.addChild(panel_4_r1);
		setRotationAngle(panel_4_r1, -0.1309F, -0.2182F, 0);
		panel_4_r1.setTextureUVOffset(172, 225).addCuboid(0, 0, 0, 12, 11, 0, 0, false);

		panel_4_r2 = createModelPart();
		panel_4_r2.setPivot(4.7969F, -43.6878F, -23.2439F);
		front_panel.addChild(panel_4_r2);
		setRotationAngle(panel_4_r2, -0.5672F, -0.2182F, 0);
		panel_4_r2.setTextureUVOffset(206, 164).addCuboid(0, 1, 0, 12, 8, 0, 0, false);

		panel_3_r1 = createModelPart();
		panel_3_r1.setPivot(5.3394F, -44.7906F, -25.6908F);
		front_panel.addChild(panel_3_r1);
		setRotationAngle(panel_3_r1, -0.2618F, -0.2182F, 0);
		panel_3_r1.setTextureUVOffset(0, 227).addCuboid(0, 9, 0, 12, 23, 0, 0, false);

		panel_3_r2 = createModelPart();
		panel_3_r2.setPivot(-4.9115F, -43.6878F, -23.2439F);
		front_panel.addChild(panel_3_r2);
		setRotationAngle(panel_3_r2, -0.5672F, 0.2182F, 0);
		panel_3_r2.setTextureUVOffset(118, 225).addCuboid(-12, 1, 0, 12, 8, 0, 0, false);

		panel_2_r1 = createModelPart();
		panel_2_r1.setPivot(-5.4539F, -44.7906F, -25.6908F);
		front_panel.addChild(panel_2_r1);
		setRotationAngle(panel_2_r1, -0.2618F, 0.2182F, 0);
		panel_2_r1.setTextureUVOffset(38, 227).addCuboid(-12, 9, 0, 12, 23, 0, 0, false);

		panel_2_r2 = createModelPart();
		panel_2_r2.setPivot(-7.5573F, -43.6878F, -22.9541F);
		front_panel.addChild(panel_2_r2);
		setRotationAngle(panel_2_r2, -0.5672F, 0, 0);
		panel_2_r2.setTextureUVOffset(196, 38).addCuboid(0, 1, 0, 15, 8, 0, 0, false);

		panel_1_r1 = createModelPart();
		panel_1_r1.setPivot(-7.5573F, -44.7906F, -25.4605F);
		front_panel.addChild(panel_1_r1);
		setRotationAngle(panel_1_r1, -0.2618F, 0, 0);
		panel_1_r1.setTextureUVOffset(291, 146).addCuboid(0, 9, 0, 15, 23, 0, 0, false);

		nose = createModelPart();
		nose.setPivot(0, 0, 0);
		head_exterior.addChild(nose);


		nose_edge = createModelPart();
		nose_edge.setPivot(0, 0, 0);
		nose.addChild(nose_edge);


		edge_6_r1 = createModelPart();
		edge_6_r1.setPivot(7.7367F, 6, -40.4098F);
		nose_edge.addChild(edge_6_r1);
		setRotationAngle(edge_6_r1, 0, 0, 0);
		edge_6_r1.setTextureUVOffset(82, 122).addCuboid(-8, -9, 0, 8, 9, 0, 0, false);

		edge_5_r1 = createModelPart();
		edge_5_r1.setPivot(-7.7358F, 6, -40.4081F);
		nose_edge.addChild(edge_5_r1);
		setRotationAngle(edge_5_r1, 0, 0, 0);
		edge_5_r1.setTextureUVOffset(82, 122).addCuboid(0, -9, 0, 8, 9, 0, 0, false);

		edge_4_r1 = createModelPart();
		edge_4_r1.setPivot(-15.3656F, 13, -38.0024F);
		nose_edge.addChild(edge_4_r1);
		setRotationAngle(edge_4_r1, 0, 0.3054F, 0);
		edge_4_r1.setTextureUVOffset(104, 122).addCuboid(0, -15.8F, 0, 8, 9, 0, 0, true);

		edge_3_r1 = createModelPart();
		edge_3_r1.setPivot(15.3664F, -2, -38.0042F);
		nose_edge.addChild(edge_3_r1);
		setRotationAngle(edge_3_r1, 0, -0.3054F, 0);
		edge_3_r1.setTextureUVOffset(104, 122).addCuboid(-8, -0.8F, 0, 8, 9, 0, 0, false);

		edge_2_r1 = createModelPart();
		edge_2_r1.setPivot(18.052F, 17, -33.7867F);
		nose_edge.addChild(edge_2_r1);
		setRotationAngle(edge_2_r1, 0, 0.5672F, 0);
		edge_2_r1.setTextureUVOffset(30, 139).addCuboid(0.001F, -20, -5, 0, 10, 5, 0, false);

		edge_1_r1 = createModelPart();
		edge_1_r1.setPivot(-18.0521F, 7, -33.7855F);
		nose_edge.addChild(edge_1_r1);
		setRotationAngle(edge_1_r1, 0, -0.5672F, 0);
		edge_1_r1.setTextureUVOffset(30, 139).addCuboid(0.001F, -10, -5, 0, 10, 5, 0, false);

		nose_top = createModelPart();
		nose_top.setPivot(0, 0, 0);
		nose.addChild(nose_top);


		nose_top_3_r1 = createModelPart();
		nose_top_3_r1.setPivot(7.4428F, -2.9742F, -35.178F);
		nose_top.addChild(nose_top_3_r1);
		setRotationAngle(nose_top_3_r1, 0.1745F, 0, 0);
		nose_top_3_r1.setTextureUVOffset(78, 46).addCuboid(-16, 0, -6, 17, 0, 6, 0, false);

		nose_top_2_r1 = createModelPart();
		nose_top_2_r1.setPivot(20.2491F, -2.9751F, -32.3648F);
		nose_top.addChild(nose_top_2_r1);
		setRotationAngle(nose_top_2_r1, 0.1745F, -0.2182F, 0);
		nose_top_2_r1.setTextureUVOffset(20, 117).addCuboid(-14, 0, -6, 13, 0, 6, 0, true);

		nose_top_1_r1 = createModelPart();
		nose_top_1_r1.setPivot(-20.2491F, -2.9751F, -32.3648F);
		nose_top.addChild(nose_top_1_r1);
		setRotationAngle(nose_top_1_r1, 0.1745F, 0.2182F, 0);
		nose_top_1_r1.setTextureUVOffset(20, 117).addCuboid(1, 0, -6, 13, 0, 6, 0, false);

		driver_door = createModelPart();
		driver_door.setPivot(0, 0, 0);
		head_exterior.addChild(driver_door);
		driver_door.setTextureUVOffset(285, 246).addCuboid(18, -14, -20, 1, 14, 12, 0, false);
		driver_door.setTextureUVOffset(10, 33).addCuboid(19, -14, -20, 1, 14, 0, 0, false);
		driver_door.setTextureUVOffset(285, 246).addCuboid(-19, -14, -20, 1, 14, 12, 0, true);
		driver_door.setTextureUVOffset(10, 33).addCuboid(-20, -14, -20, 1, 14, 0, 0, true);

		driver_door_edge_upper_2_r1 = createModelPart();
		driver_door_edge_upper_2_r1.setPivot(-20, -14, -2);
		driver_door.addChild(driver_door_edge_upper_2_r1);
		setRotationAngle(driver_door_edge_upper_2_r1, 0, 0, 0.1107F);
		driver_door_edge_upper_2_r1.setTextureUVOffset(26, 54).addCuboid(0, -18, -18, 1, 18, 0, 0, true);
		driver_door_edge_upper_2_r1.setTextureUVOffset(11, 17).addCuboid(0, -18, -15, 1, 0, 10, 0, true);
		driver_door_edge_upper_2_r1.setTextureUVOffset(293, 86).addCuboid(1, -18, -18, 1, 18, 12, 0, true);
		driver_door_edge_upper_2_r1.setTextureUVOffset(15, 17).addCuboid(-1, -18, -23, 2, 0, 8, 0, true);

		driver_door_edge_roof_3_r1 = createModelPart();
		driver_door_edge_roof_3_r1.setPivot(20, -14, 1);
		driver_door.addChild(driver_door_edge_roof_3_r1);
		setRotationAngle(driver_door_edge_roof_3_r1, 0, 0, -0.1107F);
		driver_door_edge_roof_3_r1.setTextureUVOffset(15, 17).addCuboid(-1, -18, -26, 2, 0, 8, 0, false);
		driver_door_edge_roof_3_r1.setTextureUVOffset(11, 17).addCuboid(-1, -18, -18, 1, 0, 10, 0, false);
		driver_door_edge_roof_3_r1.setTextureUVOffset(26, 54).addCuboid(-1, -18, -21, 1, 18, 0, 0, false);
		driver_door_edge_roof_3_r1.setTextureUVOffset(293, 86).addCuboid(-2, -18, -21, 1, 18, 12, 0, false);

		roof_vent = createModelPart();
		roof_vent.setPivot(0, 24, 0);
		roof_vent.setTextureUVOffset(0, 0).addCuboid(-8, -45.25F, -4, 16, 2, 52, 0, false);

		vent_1_r1 = createModelPart();
		vent_1_r1.setPivot(8, -45.25F, 0);
		roof_vent.addChild(vent_1_r1);
		setRotationAngle(vent_1_r1, 0, 0, 0.3491F);
		vent_1_r1.setTextureUVOffset(70, 122).addCuboid(0, 0, -4, 9, 2, 52, 0, false);

		vent_2_r1 = createModelPart();
		vent_2_r1.setPivot(-8, -45.25F, 0);
		roof_vent.addChild(vent_2_r1);
		setRotationAngle(vent_2_r1, 0, 0, -0.3491F);
		vent_2_r1.setTextureUVOffset(0, 103).addCuboid(-9, 0, -4, 9, 2, 52, 0, false);

		roof_head_exterior = createModelPart();
		roof_head_exterior.setPivot(0, 24, 0);


		side_7 = createModelPart();
		side_7.setPivot(0, 0, 0);
		roof_head_exterior.addChild(side_7);
		side_7.setTextureUVOffset(0, 227).addCuboid(-5.9859F, -44.6423F, -16, 6, 1, 26, 0, false);

		outer_roof_18_r1 = createModelPart();
		outer_roof_18_r1.setPivot(-1.9859F, -44.6413F, -15.9999F);
		side_7.addChild(outer_roof_18_r1);
		setRotationAngle(outer_roof_18_r1, 0.2618F, 0, 0);
		outer_roof_18_r1.setTextureUVOffset(31, 40).addCuboid(-4, 0, -9, 6, 0, 9, 0, false);

		outer_roof_17_r1 = createModelPart();
		outer_roof_17_r1.setPivot(-8.9399F, -44.1204F, -15.9999F);
		side_7.addChild(outer_roof_17_r1);
		setRotationAngle(outer_roof_17_r1, 0.2618F, 0, -0.1745F);
		outer_roof_17_r1.setTextureUVOffset(15, 82).addCuboid(-4, 0, -9, 7, 0, 9, 0, false);

		outer_roof_16_r1 = createModelPart();
		outer_roof_16_r1.setPivot(-12.8788F, -43.4259F, -15.9999F);
		side_7.addChild(outer_roof_16_r1);
		setRotationAngle(outer_roof_16_r1, 0.2618F, 0, -0.5236F);
		outer_roof_16_r1.setTextureUVOffset(0, 112).addCuboid(-4, 0, -9, 4, 0, 9, 0, false);

		outer_roof_15_r1 = createModelPart();
		outer_roof_15_r1.setPivot(-16.566F, -41.5562F, -15.0338F);
		side_7.addChild(outer_roof_15_r1);
		setRotationAngle(outer_roof_15_r1, 0.2618F, 0, -1.0472F);
		outer_roof_15_r1.setTextureUVOffset(25, 103).addCuboid(-8, 0, -12, 8, 0, 11, 0, false);

		outer_roof_14_r1 = createModelPart();
		outer_roof_14_r1.setPivot(-18.3436F, -37.9636F, -17);
		side_7.addChild(outer_roof_14_r1);
		setRotationAngle(outer_roof_14_r1, 0, -0.1309F, 0.1107F);
		outer_roof_14_r1.setTextureUVOffset(94, 86).addCuboid(0, 0, -8, 0, 6, 8, 0, false);

		outer_roof_9_r3 = createModelPart();
		outer_roof_9_r3.setPivot(-20.9939F, -14.1104F, -4);
		side_7.addChild(outer_roof_9_r3);
		setRotationAngle(outer_roof_9_r3, 0, 0, 0.1107F);
		outer_roof_9_r3.setTextureUVOffset(233, 137).addCuboid(0, -24, -13, 1, 6, 27, 0, false);

		outer_roof_11_r2 = createModelPart();
		outer_roof_11_r2.setPivot(-15.0444F, -42.1768F, -4);
		side_7.addChild(outer_roof_11_r2);
		setRotationAngle(outer_roof_11_r2, 0, 0, -0.5236F);
		outer_roof_11_r2.setTextureUVOffset(230, 195).addCuboid(-1.5F, 0, -12, 4, 1, 26, 0, false);

		outer_roof_12_r1 = createModelPart();
		outer_roof_12_r1.setPivot(-9.9249F, -43.9477F, -4);
		side_7.addChild(outer_roof_12_r1);
		setRotationAngle(outer_roof_12_r1, 0, 0, -0.1745F);
		outer_roof_12_r1.setTextureUVOffset(132, 211).addCuboid(-3, 0, -12, 7, 1, 26, 0, false);

		outer_roof_10_r3 = createModelPart();
		outer_roof_10_r3.setPivot(-17.3427F, -39.6952F, -4);
		side_7.addChild(outer_roof_10_r3);
		setRotationAngle(outer_roof_10_r3, 0, 0, -1.0472F);
		outer_roof_10_r3.setTextureUVOffset(236, 54).addCuboid(-2, -0.1F, -12, 4, 1, 26, 0, false);

		side_8 = createModelPart();
		side_8.setPivot(0, 0, 0);
		roof_head_exterior.addChild(side_8);
		side_8.setTextureUVOffset(0, 227).addCuboid(-0.0141F, -44.6423F, -16, 6, 1, 26, 0, true);

		outer_roof_19_r1 = createModelPart();
		outer_roof_19_r1.setPivot(1.9859F, -44.6413F, -15.9999F);
		side_8.addChild(outer_roof_19_r1);
		setRotationAngle(outer_roof_19_r1, 0.2618F, 0, 0);
		outer_roof_19_r1.setTextureUVOffset(31, 40).addCuboid(-2, 0, -9, 6, 0, 9, 0, true);

		outer_roof_18_r2 = createModelPart();
		outer_roof_18_r2.setPivot(8.9399F, -44.1204F, -15.9999F);
		side_8.addChild(outer_roof_18_r2);
		setRotationAngle(outer_roof_18_r2, 0.2618F, 0, 0.1745F);
		outer_roof_18_r2.setTextureUVOffset(15, 82).addCuboid(-3, 0, -9, 7, 0, 9, 0, true);

		outer_roof_17_r2 = createModelPart();
		outer_roof_17_r2.setPivot(12.8788F, -43.4259F, -15.9999F);
		side_8.addChild(outer_roof_17_r2);
		setRotationAngle(outer_roof_17_r2, 0.2618F, 0, 0.5236F);
		outer_roof_17_r2.setTextureUVOffset(0, 112).addCuboid(0, 0, -9, 4, 0, 9, 0, true);

		outer_roof_16_r2 = createModelPart();
		outer_roof_16_r2.setPivot(16.566F, -41.5562F, -15.0338F);
		side_8.addChild(outer_roof_16_r2);
		setRotationAngle(outer_roof_16_r2, 0.2618F, 0, 1.0472F);
		outer_roof_16_r2.setTextureUVOffset(25, 103).addCuboid(0, 0, -12, 8, 0, 11, 0, true);

		outer_roof_15_r2 = createModelPart();
		outer_roof_15_r2.setPivot(18.3436F, -37.9636F, -17);
		side_8.addChild(outer_roof_15_r2);
		setRotationAngle(outer_roof_15_r2, 0, 0.1309F, -0.1107F);
		outer_roof_15_r2.setTextureUVOffset(94, 86).addCuboid(0, 0, -8, 0, 6, 8, 0, true);

		outer_roof_10_r4 = createModelPart();
		outer_roof_10_r4.setPivot(20.9939F, -14.1104F, -4);
		side_8.addChild(outer_roof_10_r4);
		setRotationAngle(outer_roof_10_r4, 0, 0, -0.1107F);
		outer_roof_10_r4.setTextureUVOffset(233, 137).addCuboid(-1, -24, -13, 1, 6, 27, 0, true);

		outer_roof_12_r2 = createModelPart();
		outer_roof_12_r2.setPivot(15.0444F, -42.1768F, -4);
		side_8.addChild(outer_roof_12_r2);
		setRotationAngle(outer_roof_12_r2, 0, 0, 0.5236F);
		outer_roof_12_r2.setTextureUVOffset(230, 195).addCuboid(-2.5F, 0, -12, 4, 1, 26, 0, true);

		outer_roof_13_r1 = createModelPart();
		outer_roof_13_r1.setPivot(9.9249F, -43.9477F, -4);
		side_8.addChild(outer_roof_13_r1);
		setRotationAngle(outer_roof_13_r1, 0, 0, 0.1745F);
		outer_roof_13_r1.setTextureUVOffset(132, 211).addCuboid(-4, 0, -12, 7, 1, 26, 0, true);

		outer_roof_11_r3 = createModelPart();
		outer_roof_11_r3.setPivot(17.3427F, -39.6952F, -4);
		side_8.addChild(outer_roof_11_r3);
		setRotationAngle(outer_roof_11_r3, 0, 0, 1.0472F);
		outer_roof_11_r3.setTextureUVOffset(236, 54).addCuboid(-2, -0.1F, -12, 4, 1, 26, 0, true);

		headlights = createModelPart();
		headlights.setPivot(0, 24, 0);


		headlight_2_r1 = createModelPart();
		headlight_2_r1.setPivot(7.132F, -13.881F, -33.7767F);
		headlights.addChild(headlight_2_r1);
		setRotationAngle(headlight_2_r1, -0.1309F, -0.2182F, 0);
		headlight_2_r1.setTextureUVOffset(0, 213).addCuboid(0.1F, -0.1F, -0.01F, 12, 11, 0, 0, true);

		headlight_1_r1 = createModelPart();
		headlight_1_r1.setPivot(-7.2465F, -13.881F, -33.7767F);
		headlights.addChild(headlight_1_r1);
		setRotationAngle(headlight_1_r1, -0.1309F, 0.2182F, 0);
		headlight_1_r1.setTextureUVOffset(0, 213).addCuboid(-12.1F, -0.1F, -0.01F, 12, 11, 0, 0, false);

		tail_lights = createModelPart();
		tail_lights.setPivot(0, 24, 0);


		tail_light_2_r1 = createModelPart();
		tail_light_2_r1.setPivot(7.132F, -13.881F, -33.7767F);
		tail_lights.addChild(tail_light_2_r1);
		setRotationAngle(tail_light_2_r1, -0.1309F, -0.2182F, 0);
		tail_light_2_r1.setTextureUVOffset(204, 211).addCuboid(0, 0, -0.01F, 12, 11, 0, 0, true);

		tail_light_1_r1 = createModelPart();
		tail_light_1_r1.setPivot(-7.2465F, -13.881F, -33.7767F);
		tail_lights.addChild(tail_light_1_r1);
		setRotationAngle(tail_light_1_r1, -0.1309F, 0.2182F, 0);
		tail_light_1_r1.setTextureUVOffset(204, 211).addCuboid(-12, 0, -0.01F, 12, 11, 0, 0, false);

		roof_head = createModelPart();
		roof_head.setPivot(0, 24, 0);


		side_5 = createModelPart();
		side_5.setPivot(0, 0, 0);
		roof_head.addChild(side_5);
		side_5.setTextureUVOffset(0, 34).addCuboid(-16.0123F, -32.1399F, -4, 3, 0, 14, 0, false);
		side_5.setTextureUVOffset(22, 28).addCuboid(-10.1444F, -36.2357F, -4, 2, 0, 14, 0, false);
		side_5.setTextureUVOffset(0, 54).addCuboid(-6, -37.25F, -4, 6, 0, 14, 0, false);

		inner_roof_5_r2 = createModelPart();
		inner_roof_5_r2.setPivot(-13.0123F, -32.1409F, 26);
		side_5.addChild(inner_roof_5_r2);
		setRotationAngle(inner_roof_5_r2, 0, 0, -0.9599F);
		inner_roof_5_r2.setTextureUVOffset(12, 103).addCuboid(0, 0.001F, -30, 5, 0, 14, 0, false);

		side_6 = createModelPart();
		side_6.setPivot(0, 0, 0);
		roof_head.addChild(side_6);
		side_6.setTextureUVOffset(0, 34).addCuboid(13.0123F, -32.1399F, -4, 3, 0, 14, 0, true);
		side_6.setTextureUVOffset(22, 28).addCuboid(8.1444F, -36.2357F, -4, 2, 0, 14, 0, true);
		side_6.setTextureUVOffset(0, 54).addCuboid(0, -37.25F, -4, 6, 0, 14, 0, true);

		inner_roof_2_r2 = createModelPart();
		inner_roof_2_r2.setPivot(13.0123F, -32.1409F, 26);
		side_6.addChild(inner_roof_2_r2);
		setRotationAngle(inner_roof_2_r2, 0, 0, 0.9599F);
		inner_roof_2_r2.setTextureUVOffset(12, 103).addCuboid(-5, 0.001F, -30, 5, 0, 14, 0, true);

		door_light_on = createModelPart();
		door_light_on.setPivot(0, 24, 0);


		light_r1 = createModelPart();
		light_r1.setPivot(-20, -14, 0);
		door_light_on.addChild(light_r1);
		setRotationAngle(light_r1, 0, 0, 0.1107F);
		light_r1.setTextureUVOffset(33, 334).addCuboid(-1, -19.25F, 0, 0, 0, 0, 0.4F, false);

		door_light_off = createModelPart();
		door_light_off.setPivot(0, 24, 0);


		light_r2 = createModelPart();
		light_r2.setPivot(-20, -14, 0);
		door_light_off.addChild(light_r2);
		setRotationAngle(light_r2, 0, 0, 0.1107F);
		light_r2.setTextureUVOffset(29, 334).addCuboid(-1, -19.25F, 0, 0, 0, 0, 0.4F, false);

		end_handrail = createModelPart();
		end_handrail.setPivot(0, 24, 0);
		end_handrail.setTextureUVOffset(335, 0).addCuboid(0, -12, 0, 0, 12, 0, 0.2F, false);
		end_handrail.setTextureUVOffset(332, 13).addCuboid(0, -23.2645F, 0.563F, 0, 6, 0, 0.2F, false);
		end_handrail.setTextureUVOffset(332, 13).addCuboid(0, -23.2645F, -0.563F, 0, 6, 0, 0.2F, false);
		end_handrail.setTextureUVOffset(335, 0).addCuboid(0, -38, 0, 0, 9, 0, 0.2F, false);

		pole_top_diagonal_1_r2 = createModelPart();
		pole_top_diagonal_1_r2.setPivot(0.2F, -28.8F, 0.2F);
		end_handrail.addChild(pole_top_diagonal_1_r2);
		setRotationAngle(pole_top_diagonal_1_r2, -0.1047F, 0, 0);
		pole_top_diagonal_1_r2.setTextureUVOffset(335, 0).addCuboid(-0.2F, 0.2069F, -0.2F, 0, 5, 0, 0.2F, false);

		pole_top_diagonal_2_r2 = createModelPart();
		pole_top_diagonal_2_r2.setPivot(0.2F, -28.8F, -0.2F);
		end_handrail.addChild(pole_top_diagonal_2_r2);
		setRotationAngle(pole_top_diagonal_2_r2, 0.1047F, 0, 0);
		pole_top_diagonal_2_r2.setTextureUVOffset(335, 0).addCuboid(-0.2F, 0.2069F, 0.2F, 0, 5, 0, 0.2F, false);

		pole_bottom_diagonal_1_r2 = createModelPart();
		pole_bottom_diagonal_1_r2.setPivot(0, -14.4002F, -0.2819F);
		end_handrail.addChild(pole_bottom_diagonal_1_r2);
		setRotationAngle(pole_bottom_diagonal_1_r2, 0.1047F, 0, 0);
		pole_bottom_diagonal_1_r2.setTextureUVOffset(335, 0).addCuboid(0, -2.5F, 0, 0, 5, 0, 0.2F, false);

		pole_bottom_diagonal_2_r2 = createModelPart();
		pole_bottom_diagonal_2_r2.setPivot(0, -14.4002F, 0.2819F);
		end_handrail.addChild(pole_bottom_diagonal_2_r2);
		setRotationAngle(pole_bottom_diagonal_2_r2, -0.1047F, 0, 0);
		pole_bottom_diagonal_2_r2.setTextureUVOffset(335, 0).addCuboid(0, -2.5F, 0, 0, 5, 0, 0.2F, false);

		buildModel();
	}

	private static final int DOOR_MAX = 14;
	private static final ModelDoorOverlay MODEL_DOOR_OVERLAY = new ModelDoorOverlay(DOOR_MAX, 6.34F, "door_overlay_r_train_left.png", "door_overlay_r_train_right.png");
	private static final ModelDoorOverlayTopSP1900 MODEL_DOOR_OVERLAY_TOP = new ModelDoorOverlayTopSP1900();

	@Override
	public ModelRTrain createNew(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		return new ModelRTrain(doorAnimationType, renderDoorOverlay);
	}

	@Override
	protected void renderWindowPositions(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
		final boolean isEvenWindow = isEvenWindow(position);
		switch (renderStage) {
			case LIGHT:
				renderMirror(roof_light, graphicsHolder, light, position);
				break;
			case INTERIOR:
				if (isEvenWindow) {
					renderMirror(window, graphicsHolder, light, position);
					renderOnce(roof_window_1, graphicsHolder, light, position);
					renderOnce(roof_window_2, graphicsHolder, light, position);
				} else {
					renderMirror(window, graphicsHolder, light, position);
					renderOnceFlipped(roof_window_1, graphicsHolder, light, position);
					renderOnceFlipped(roof_window_2, graphicsHolder, light, position);
				}
				if (renderDetails) {
					renderMirror(top_handrail, graphicsHolder, light, position);
					renderMirror(seat, graphicsHolder, light, position);
					renderMirror(side_panel, graphicsHolder, light, position - 20);
					renderMirror(side_panel, graphicsHolder, light, position + 20);
				}
				break;
			case INTERIOR_TRANSLUCENT:
				renderMirror(side_panel_translucent, graphicsHolder, light, position - 20);
				renderMirror(side_panel_translucent, graphicsHolder, light, position + 20);
				break;
			case EXTERIOR:
				if (isEnd2Head) {
					renderOnceFlipped(window_exterior_1, graphicsHolder, light, position);
					renderOnceFlipped(window_exterior_2, graphicsHolder, light, position);
				} else {
					renderOnce(window_exterior_1, graphicsHolder, light, position);
					renderOnce(window_exterior_2, graphicsHolder, light, position);
				}
				renderMirror(roof_exterior, graphicsHolder, light, position);
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
				renderMirror(door_light, graphicsHolder, light, position);
				if (notLastDoor) {
					renderMirror(roof_light, graphicsHolder, light, position);
				}
				if (middleDoor && doorOpen && renderDetails) {
					renderMirror(door_light_on, graphicsHolder, light, position - 40);
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
					renderMirror(roof_door, graphicsHolder, light, position);
				}
				break;
			case EXTERIOR:
				if (isEnd2Head) {
					door_left_exterior_1.setOffset(0, 0, doorLeftZ);
					door_right_exterior_1.setOffset(0, 0, -doorLeftZ);
					renderOnceFlipped(door_exterior_1, graphicsHolder, light, position);
					door_left_exterior_2.setOffset(0, 0, -doorRightZ);
					door_right_exterior_2.setOffset(0, 0, doorRightZ);
					renderOnceFlipped(door_exterior_2, graphicsHolder, light, position);
				} else {
					door_left_exterior_1.setOffset(0, 0, doorRightZ);
					door_right_exterior_1.setOffset(0, 0, -doorRightZ);
					renderOnce(door_exterior_1, graphicsHolder, light, position);
					door_left_exterior_2.setOffset(0, 0, -doorLeftZ);
					door_right_exterior_2.setOffset(0, 0, doorLeftZ);
					renderOnce(door_exterior_2, graphicsHolder, light, position);
				}
				renderMirror(roof_exterior, graphicsHolder, light, position);
				if (middleDoor && !doorOpen && renderDetails) {
					renderMirror(door_light_off, graphicsHolder, light, position - 40);
				}
				break;
		}
	}

	@Override
	protected void renderHeadPosition1(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean useHeadlights) {
		switch (renderStage) {
			case LIGHT:
				renderMirror(roof_light, graphicsHolder, light, position + 20);
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
				renderOnce(roof_head_exterior, graphicsHolder, light, position);
				renderOnce(roof_vent, graphicsHolder, light, position);
				break;
		}
	}

	@Override
	protected void renderHeadPosition2(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean useHeadlights) {
		switch (renderStage) {
			case LIGHT:
				renderMirror(roof_light, graphicsHolder, light, position - 20);
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
				renderOnceFlipped(roof_head_exterior, graphicsHolder, light, position);
				renderOnceFlipped(roof_vent, graphicsHolder, light, position);
				break;
		}
	}

	@Override
	protected void renderEndPosition1(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
		switch (renderStage) {
			case LIGHT:
				renderMirror(roof_light, graphicsHolder, light, position + 17);
				break;
			case INTERIOR:
				renderOnce(end, graphicsHolder, light, position);
				if (renderDetails) {
					renderOnce(roof_end, graphicsHolder, light, position);
					renderMirror(side_panel, graphicsHolder, light, position + 9);
					renderOnce(end_handrail, graphicsHolder, light, position);
				}
				break;
			case INTERIOR_TRANSLUCENT:
				renderMirror(side_panel_translucent, graphicsHolder, light, position + 9);
				break;
			case EXTERIOR:
				renderOnce(end_exterior, graphicsHolder, light, position);
				renderOnce(roof_end_exterior, graphicsHolder, light, position);
				renderOnce(roof_vent, graphicsHolder, light, position);
				break;
		}
	}

	@Override
	protected void renderEndPosition2(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
		switch (renderStage) {
			case LIGHT:
				renderMirror(roof_light, graphicsHolder, light, position - 17);
				break;
			case INTERIOR:
				renderOnceFlipped(end, graphicsHolder, light, position);
				if (renderDetails) {
					renderOnceFlipped(roof_end, graphicsHolder, light, position);
					renderMirror(side_panel, graphicsHolder, light, position - 9);
					renderOnceFlipped(end_handrail, graphicsHolder, light, position);
				}
				break;
			case INTERIOR_TRANSLUCENT:
				renderMirror(side_panel_translucent, graphicsHolder, light, position - 9);
				break;
			case EXTERIOR:
				renderOnceFlipped(end_exterior, graphicsHolder, light, position);
				renderOnceFlipped(roof_end_exterior, graphicsHolder, light, position);
				renderOnceFlipped(roof_vent, graphicsHolder, light, position);
				break;
		}
	}

	@Override
	protected ModelDoorOverlay getModelDoorOverlay() {
		return MODEL_DOOR_OVERLAY;
	}

	@Override
	protected ModelDoorOverlayTopBase getModelDoorOverlayTop() {
		return MODEL_DOOR_OVERLAY_TOP;
	}

	@Override
	protected int[] getWindowPositions() {
		return new int[]{-120, -40, 40, 120};
	}

	@Override
	protected int[] getDoorPositions() {
		return new int[]{-160, -80, 0, 80, 160};
	}

	@Override
	protected int[] getEndPositions() {
		return new int[]{-184, 184};
	}

	@Override
	protected int getDoorMax() {
		return DOOR_MAX;
	}

	@Override
	protected void renderTextDisplays(StoredMatrixTransformations storedMatrixTransformations, int thisRouteColor, String thisRouteName, String thisRouteNumber, String thisStationName, String thisRouteDestination, String nextStationName, Consumer<BiConsumer<String, InterchangeColorsForStationName>> getInterchanges, int car, int totalCars, boolean atPlatform, boolean isTerminating, ObjectArrayList<ScrollingText> scrollingTexts) {
		renderFrontDestination(
				storedMatrixTransformations,
				0.79F, 0, getEndPositions()[0] / 16F - 2.27F, 0, -2.25F, -0.01F,
				-15, -12.5F, 0.4F, 0.14F,
				0xFFFF0000, 0xFFFF9900, 1.25F, getDestinationString(thisRouteDestination, TextSpacingType.NORMAL, false), true, car, totalCars
		);
	}

	@Override
	protected String defaultDestinationString() {
		return "回廠|Depot";
	}

	private boolean isEvenWindow(int position) {
		return isIndex(1, position, getWindowPositions()) || isIndex(3, position, getWindowPositions());
	}
}