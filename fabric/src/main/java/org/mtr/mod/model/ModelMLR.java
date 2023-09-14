package org.mtr.mod.model;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.core.data.InterchangeColorsForStationName;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.ModelPartExtension;
import org.mtr.mod.client.DoorAnimationType;
import org.mtr.mod.client.ScrollingText;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ModelMLR extends ModelSimpleTrainBase<ModelMLR> {

	private final ModelPartExtension window_1;
	private final ModelPartExtension upper_wall_r1;
	private final ModelPartExtension window_2;
	private final ModelPartExtension upper_wall_r2;
	private final ModelPartExtension window_handrails;
	private final ModelPartExtension handrail_8_r1;
	private final ModelPartExtension seats;
	private final ModelPartExtension seat_back_r1;
	private final ModelPartExtension handrail_3_r1;
	private final ModelPartExtension handrail_2_r1;
	private final ModelPartExtension window_1_tv;
	private final ModelPartExtension tv_r1;
	private final ModelPartExtension window_exterior_1;
	private final ModelPartExtension upper_wall_r3;
	private final ModelPartExtension window_exterior_2;
	private final ModelPartExtension upper_wall_r4;
	private final ModelPartExtension side_panel;
	private final ModelPartExtension side_panel_translucent;
	private final ModelPartExtension roof_window;
	private final ModelPartExtension inner_roof_5_r1;
	private final ModelPartExtension inner_roof_3_r1;
	private final ModelPartExtension inner_roof_2_r1;
	private final ModelPartExtension roof_door;
	private final ModelPartExtension inner_roof_6_r1;
	private final ModelPartExtension inner_roof_4_r1;
	private final ModelPartExtension inner_roof_3_r2;
	private final ModelPartExtension roof_light;
	private final ModelPartExtension light_r1;
	private final ModelPartExtension roof_exterior;
	private final ModelPartExtension outer_roof_5_r1;
	private final ModelPartExtension outer_roof_4_r1;
	private final ModelPartExtension outer_roof_3_r1;
	private final ModelPartExtension outer_roof_2_r1;
	private final ModelPartExtension outer_roof_1_r1;
	private final ModelPartExtension door;
	private final ModelPartExtension door_left;
	private final ModelPartExtension door_left_top_r1;
	private final ModelPartExtension door_right;
	private final ModelPartExtension door_right_top_r1;
	private final ModelPartExtension door_handrails;
	private final ModelPartExtension handrail_8_r2;
	private final ModelPartExtension door_exterior;
	private final ModelPartExtension door_left_exterior;
	private final ModelPartExtension door_left_top_r2;
	private final ModelPartExtension door_right_exterior;
	private final ModelPartExtension door_right_top_r2;
	private final ModelPartExtension end;
	private final ModelPartExtension upper_wall_2_r1;
	private final ModelPartExtension upper_wall_1_r1;
	private final ModelPartExtension seat_1;
	private final ModelPartExtension seat_back_r2;
	private final ModelPartExtension seat_2;
	private final ModelPartExtension seat_back_r3;
	private final ModelPartExtension seat_3;
	private final ModelPartExtension seat_back_r4;
	private final ModelPartExtension seat_4;
	private final ModelPartExtension seat_back_r5;
	private final ModelPartExtension seat_5;
	private final ModelPartExtension seat_back_r6;
	private final ModelPartExtension seat_6;
	private final ModelPartExtension seat_back_r7;
	private final ModelPartExtension end_exterior;
	private final ModelPartExtension upper_wall_2_r2;
	private final ModelPartExtension upper_wall_1_r2;
	private final ModelPartExtension roof_end;
	private final ModelPartExtension handrail_4_r1;
	private final ModelPartExtension handrail_3_r2;
	private final ModelPartExtension handrail_2_r2;
	private final ModelPartExtension handrail_1_r1;
	private final ModelPartExtension handrail_9_r1;
	private final ModelPartExtension handrail_8_r3;
	private final ModelPartExtension inner_roof_right_4_r1;
	private final ModelPartExtension inner_roof_right_5_r1;
	private final ModelPartExtension inner_roof_right_7_r1;
	private final ModelPartExtension inner_roof_left_7_r1;
	private final ModelPartExtension inner_roof_left_5_r1;
	private final ModelPartExtension inner_roof_left_4_r1;
	private final ModelPartExtension roof_end_light;
	private final ModelPartExtension light_2_r1;
	private final ModelPartExtension light_1_r1;
	private final ModelPartExtension roof_end_exterior;
	private final ModelPartExtension outer_roof_1;
	private final ModelPartExtension upper_wall_1_r3;
	private final ModelPartExtension outer_roof_5_r2;
	private final ModelPartExtension outer_roof_4_r2;
	private final ModelPartExtension outer_roof_3_r2;
	private final ModelPartExtension outer_roof_2_r2;
	private final ModelPartExtension outer_roof_2;
	private final ModelPartExtension upper_wall_1_r4;
	private final ModelPartExtension outer_roof_5_r3;
	private final ModelPartExtension outer_roof_4_r3;
	private final ModelPartExtension outer_roof_3_r3;
	private final ModelPartExtension outer_roof_2_r3;
	private final ModelPartExtension roof_end_vents;
	private final ModelPartExtension vent_3_r1;
	private final ModelPartExtension vent_2_r1;
	private final ModelPartExtension head;
	private final ModelPartExtension upper_wall_2_r3;
	private final ModelPartExtension upper_wall_1_r5;
	private final ModelPartExtension head_exterior;
	private final ModelPartExtension driver_door_upper_2_r1;
	private final ModelPartExtension upper_wall_2_r4;
	private final ModelPartExtension driver_door_upper_1_r1;
	private final ModelPartExtension upper_wall_1_r6;
	private final ModelPartExtension front;
	private final ModelPartExtension front_middle_top_2_r1;
	private final ModelPartExtension front_middle_top_1_r1;
	private final ModelPartExtension front_3_r1;
	private final ModelPartExtension front_1_r1;
	private final ModelPartExtension side_1;
	private final ModelPartExtension outer_roof_11_r1;
	private final ModelPartExtension outer_roof_10_r1;
	private final ModelPartExtension outer_roof_9_r1;
	private final ModelPartExtension outer_roof_8_r1;
	private final ModelPartExtension outer_roof_7_r1;
	private final ModelPartExtension outer_roof_5_r4;
	private final ModelPartExtension outer_roof_4_r4;
	private final ModelPartExtension outer_roof_3_r4;
	private final ModelPartExtension outer_roof_2_r4;
	private final ModelPartExtension outer_roof_1_r2;
	private final ModelPartExtension front_side_lower_5_r1;
	private final ModelPartExtension front_side_lower_4_r1;
	private final ModelPartExtension front_side_lower_3_r1;
	private final ModelPartExtension front_side_lower_2_r1;
	private final ModelPartExtension front_side_lower_1_r1;
	private final ModelPartExtension front_side_upper_7_r1;
	private final ModelPartExtension front_side_upper_6_r1;
	private final ModelPartExtension front_side_upper_5_r1;
	private final ModelPartExtension front_side_upper_4_r1;
	private final ModelPartExtension front_side_upper_3_r1;
	private final ModelPartExtension front_side_upper_2_r1;
	private final ModelPartExtension front_side_upper_1_r1;
	private final ModelPartExtension side_2;
	private final ModelPartExtension outer_roof_11_r2;
	private final ModelPartExtension outer_roof_10_r2;
	private final ModelPartExtension outer_roof_9_r2;
	private final ModelPartExtension outer_roof_8_r2;
	private final ModelPartExtension outer_roof_7_r2;
	private final ModelPartExtension outer_roof_5_r5;
	private final ModelPartExtension outer_roof_4_r5;
	private final ModelPartExtension outer_roof_3_r5;
	private final ModelPartExtension outer_roof_2_r5;
	private final ModelPartExtension outer_roof_1_r3;
	private final ModelPartExtension front_side_lower_6_r1;
	private final ModelPartExtension front_side_lower_5_r2;
	private final ModelPartExtension front_side_lower_4_r2;
	private final ModelPartExtension front_side_lower_3_r2;
	private final ModelPartExtension front_side_lower_2_r2;
	private final ModelPartExtension front_side_upper_8_r1;
	private final ModelPartExtension front_side_upper_7_r2;
	private final ModelPartExtension front_side_upper_6_r2;
	private final ModelPartExtension front_side_upper_5_r2;
	private final ModelPartExtension front_side_upper_4_r2;
	private final ModelPartExtension front_side_upper_3_r2;
	private final ModelPartExtension front_side_upper_2_r2;
	private final ModelPartExtension headlights;
	private final ModelPartExtension headlight_4_r1;
	private final ModelPartExtension headlight_3_r1;
	private final ModelPartExtension headlight_2_r1;
	private final ModelPartExtension headlight_1_r1;
	private final ModelPartExtension tail_lights;
	private final ModelPartExtension tail_light_2_r1;
	private final ModelPartExtension tail_light_1_r1;
	private final ModelPartExtension door_light;
	private final ModelPartExtension outer_roof_1_r4;
	private final ModelPartExtension door_light_on;
	private final ModelPartExtension light_r2;
	private final ModelPartExtension door_light_off;
	private final ModelPartExtension light_r3;
	private final ModelPartExtension christmas_tree;
	private final ModelPartExtension present_6_r1;
	private final ModelPartExtension present_5_r1;
	private final ModelPartExtension present_2_r1;
	private final ModelPartExtension christmas_antler;
	private final ModelPartExtension antler_2_r1;
	private final ModelPartExtension antler_base_r1;
	private final ModelPartExtension christmas_light_head;
	private final ModelPartExtension christmas_light_holder;
	private final ModelPartExtension c_light_pole_5_r1;
	private final ModelPartExtension c_light_pole_4_r1;
	private final ModelPartExtension c_light_pole_3_r1;
	private final ModelPartExtension c_light_pole_2_r1;
	private final ModelPartExtension c_light_pole_1_r1;
	private final ModelPartExtension christmas_light_red;
	private final ModelPartExtension c_light_9_r1;
	private final ModelPartExtension c_light_5_r1;
	private final ModelPartExtension c_light_1_r1;
	private final ModelPartExtension christmas_light_yellow;
	private final ModelPartExtension c_light_10_r1;
	private final ModelPartExtension c_light_6_r1;
	private final ModelPartExtension c_light_2_r1;
	private final ModelPartExtension christmas_light_green;
	private final ModelPartExtension c_light_11_r1;
	private final ModelPartExtension c_light_7_r1;
	private final ModelPartExtension c_light_3_r1;
	private final ModelPartExtension christmas_light_blue;
	private final ModelPartExtension c_light_12_r1;
	private final ModelPartExtension c_light_8_r1;
	private final ModelPartExtension c_light_4_r1;
	private final ModelPartExtension christmas_light_tree_red;
	private final ModelPartExtension ct_light_13_r1;
	private final ModelPartExtension christmas_light_tree_yellow;
	private final ModelPartExtension ct_light_20_r1;
	private final ModelPartExtension christmas_light_tree_green;
	private final ModelPartExtension ct_light_36_r1;
	private final ModelPartExtension christmas_light_tree_blue;
	private final ModelPartExtension ct_light_48_r1;

	protected boolean isChristmas;

	public ModelMLR(boolean isChristmas) {
		this(isChristmas, DoorAnimationType.MLR, true);
	}

	protected ModelMLR(boolean isChristmas, DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		super(400, 400, doorAnimationType, renderDoorOverlay);
		this.isChristmas = isChristmas;

		window_1 = createModelPart();
		window_1.setPivot(0, 24, 0);
		window_1.setTextureUVOffset(232, 102).addCuboid(-20, 0, -16, 20, 1, 32, 0, false);
		window_1.setTextureUVOffset(300, 99).addCuboid(-20, -13, -18, 2, 13, 36, 0, false);

		upper_wall_r1 = createModelPart();
		upper_wall_r1.setPivot(-20, -13, 0);
		window_1.addChild(upper_wall_r1);
		setRotationAngle(upper_wall_r1, 0, 0, 0.1107F);
		upper_wall_r1.setTextureUVOffset(76, 267).addCuboid(0, -20, -18, 2, 20, 36, 0, false);

		window_2 = createModelPart();
		window_2.setPivot(0, 24, 0);
		window_2.setTextureUVOffset(184, 219).addCuboid(-20, 0, -16, 20, 1, 32, 0, false);
		window_2.setTextureUVOffset(292, 191).addCuboid(-20, -13, -18, 2, 13, 36, 0, false);

		upper_wall_r2 = createModelPart();
		upper_wall_r2.setPivot(-20, -13, 0);
		window_2.addChild(upper_wall_r2);
		setRotationAngle(upper_wall_r2, 0, 0, 0.1107F);
		upper_wall_r2.setTextureUVOffset(201, 252).addCuboid(0, -20, -18, 2, 20, 36, 0, false);

		window_handrails = createModelPart();
		window_handrails.setPivot(0, 24, 0);
		window_handrails.setTextureUVOffset(8, 1).addCuboid(0, -33, 0, 0, 33, 0, 0.2F, false);
		window_handrails.setTextureUVOffset(0, 69).addCuboid(-1, -32, 15, 2, 4, 0, 0, false);
		window_handrails.setTextureUVOffset(0, 69).addCuboid(-1, -32, 9, 2, 4, 0, 0, false);
		window_handrails.setTextureUVOffset(0, 69).addCuboid(-1, -32, 3, 2, 4, 0, 0, false);
		window_handrails.setTextureUVOffset(0, 69).addCuboid(-1, -32, -3, 2, 4, 0, 0, false);
		window_handrails.setTextureUVOffset(0, 69).addCuboid(-1, -32, -9, 2, 4, 0, 0, false);
		window_handrails.setTextureUVOffset(0, 69).addCuboid(-1, -32, -15, 2, 4, 0, 0, false);

		handrail_8_r1 = createModelPart();
		handrail_8_r1.setPivot(0, 0, 0);
		window_handrails.addChild(handrail_8_r1);
		setRotationAngle(handrail_8_r1, -1.5708F, 0, 0);
		handrail_8_r1.setTextureUVOffset(0, 0).addCuboid(0, -16, -31.5F, 0, 32, 0, 0.2F, false);

		seats = createModelPart();
		seats.setPivot(0, 24, 0);
		seats.setTextureUVOffset(156, 298).addCuboid(-18, -6, -15.5F, 7, 1, 31, 0, false);

		seat_back_r1 = createModelPart();
		seat_back_r1.setPivot(-17, -6, 0);
		seats.addChild(seat_back_r1);
		setRotationAngle(seat_back_r1, 0, 0, -0.0524F);
		seat_back_r1.setTextureUVOffset(328, 325).addCuboid(-1, -8, -15.5F, 1, 8, 31, 0, false);

		handrail_3_r1 = createModelPart();
		handrail_3_r1.setPivot(-11, -5, 0);
		seats.addChild(handrail_3_r1);
		setRotationAngle(handrail_3_r1, 0, 0, -0.0436F);
		handrail_3_r1.setTextureUVOffset(4, 16).addCuboid(0, -13.2F, 15, 0, 13, 0, 0.2F, false);
		handrail_3_r1.setTextureUVOffset(4, 16).addCuboid(0, -13.2F, -15, 0, 13, 0, 0.2F, false);

		handrail_2_r1 = createModelPart();
		handrail_2_r1.setPivot(-12.2986F, -26.5473F, -14);
		seats.addChild(handrail_2_r1);
		setRotationAngle(handrail_2_r1, 0, 0, -0.0873F);
		handrail_2_r1.setTextureUVOffset(4, 0).addCuboid(0, -8, -1, 0, 16, 0, 0.2F, false);
		handrail_2_r1.setTextureUVOffset(4, 0).addCuboid(0, -8, 29, 0, 16, 0, 0.2F, false);

		window_1_tv = createModelPart();
		window_1_tv.setPivot(0, 24, 0);
		window_1_tv.setTextureUVOffset(232, 135).addCuboid(-17, -32.5F, -5.5F, 2, 4, 12, 0, false);

		tv_r1 = createModelPart();
		tv_r1.setPivot(-15, -28.5F, 0);
		window_1_tv.addChild(tv_r1);
		setRotationAngle(tv_r1, 0, 0, 0.5672F);
		tv_r1.setTextureUVOffset(20, 155).addCuboid(-2, -8, -5.5F, 2, 8, 12, 0, false);

		window_exterior_1 = createModelPart();
		window_exterior_1.setPivot(0, 24, 0);
		window_exterior_1.setTextureUVOffset(234, 326).addCuboid(-21, 0, -16, 1, 7, 32, 0, false);
		window_exterior_1.setTextureUVOffset(40, 244).addCuboid(-20, -13, -18, 0, 13, 36, 0, false);

		upper_wall_r3 = createModelPart();
		upper_wall_r3.setPivot(-20, -13, 0);
		window_exterior_1.addChild(upper_wall_r3);
		setRotationAngle(upper_wall_r3, 0, 0, 0.1107F);
		upper_wall_r3.setTextureUVOffset(164, 216).addCuboid(0, -20, -18, 0, 20, 36, 0, false);

		window_exterior_2 = createModelPart();
		window_exterior_2.setPivot(0, 24, 0);
		window_exterior_2.setTextureUVOffset(200, 308).addCuboid(-21, 0, -16, 1, 7, 32, 0, false);
		window_exterior_2.setTextureUVOffset(40, 231).addCuboid(-20, -13, -18, 0, 13, 36, 0, false);

		upper_wall_r4 = createModelPart();
		upper_wall_r4.setPivot(-20, -13, 0);
		window_exterior_2.addChild(upper_wall_r4);
		setRotationAngle(upper_wall_r4, 0, 0, 0.1107F);
		upper_wall_r4.setTextureUVOffset(68, 13).addCuboid(0, -20, -18, 0, 20, 36, 0, false);

		side_panel = createModelPart();
		side_panel.setPivot(0, 24, 0);
		side_panel.setTextureUVOffset(206, 123).addCuboid(-18, -34, 0, 7, 31, 0, 0, false);

		side_panel_translucent = createModelPart();
		side_panel_translucent.setPivot(0, 24, 0);
		side_panel_translucent.setTextureUVOffset(304, 103).addCuboid(-18, -34, 0, 7, 31, 0, 0, false);

		roof_window = createModelPart();
		roof_window.setPivot(0, 24, 0);
		roof_window.setTextureUVOffset(4, 123).addCuboid(-16, -32, -16, 2, 0, 32, 0, false);
		roof_window.setTextureUVOffset(84, 69).addCuboid(-10.2292F, -34.8796F, -16, 5, 0, 32, 0, false);
		roof_window.setTextureUVOffset(122, 0).addCuboid(-2, -33, -16, 2, 0, 32, 0, false);

		inner_roof_5_r1 = createModelPart();
		inner_roof_5_r1.setPivot(-2, -33, 0);
		roof_window.addChild(inner_roof_5_r1);
		setRotationAngle(inner_roof_5_r1, 0, 0, 0.5236F);
		inner_roof_5_r1.setTextureUVOffset(96, 0).addCuboid(-4, 0, -16, 4, 0, 32, 0, false);

		inner_roof_3_r1 = createModelPart();
		inner_roof_3_r1.setPivot(-11.6147F, -34.3057F, 0);
		roof_window.addChild(inner_roof_3_r1);
		setRotationAngle(inner_roof_3_r1, 0, 0, -0.3927F);
		inner_roof_3_r1.setTextureUVOffset(104, 0).addCuboid(-1.5F, 0, -16, 3, 0, 32, 0, false);

		inner_roof_2_r1 = createModelPart();
		inner_roof_2_r1.setPivot(-14, -32, 0);
		roof_window.addChild(inner_roof_2_r1);
		setRotationAngle(inner_roof_2_r1, 0, 0, -1.0472F);
		inner_roof_2_r1.setTextureUVOffset(0, 123).addCuboid(0, 0, -16, 2, 0, 32, 0, false);

		roof_door = createModelPart();
		roof_door.setPivot(0, 24, 0);
		roof_door.setTextureUVOffset(94, 69).addCuboid(-18, -32, -16, 4, 0, 32, 0, false);
		roof_door.setTextureUVOffset(64, 69).addCuboid(-10.2292F, -34.8796F, -16, 5, 0, 32, 0, false);
		roof_door.setTextureUVOffset(114, 0).addCuboid(-2, -33, -16, 2, 0, 32, 0, false);

		inner_roof_6_r1 = createModelPart();
		inner_roof_6_r1.setPivot(-2, -33, 0);
		roof_door.addChild(inner_roof_6_r1);
		setRotationAngle(inner_roof_6_r1, 0, 0, 0.5236F);
		inner_roof_6_r1.setTextureUVOffset(0, 49).addCuboid(-4, 0, -16, 4, 0, 32, 0, false);

		inner_roof_4_r1 = createModelPart();
		inner_roof_4_r1.setPivot(-11.6147F, -34.3057F, 0);
		roof_door.addChild(inner_roof_4_r1);
		setRotationAngle(inner_roof_4_r1, 0, 0, -0.3927F);
		inner_roof_4_r1.setTextureUVOffset(102, 69).addCuboid(-1.5F, 0, -16, 3, 0, 32, 0, false);

		inner_roof_3_r2 = createModelPart();
		inner_roof_3_r2.setPivot(-14, -32, 0);
		roof_door.addChild(inner_roof_3_r2);
		setRotationAngle(inner_roof_3_r2, 0, 0, -1.0472F);
		inner_roof_3_r2.setTextureUVOffset(118, 0).addCuboid(0, 0, -16, 2, 0, 32, 0, false);

		roof_light = createModelPart();
		roof_light.setPivot(0, 24, 0);


		light_r1 = createModelPart();
		light_r1.setPivot(-2, -33, 0);
		roof_light.addChild(light_r1);
		setRotationAngle(light_r1, 0, 0, 0.5236F);
		light_r1.setTextureUVOffset(108, 49).addCuboid(-4, -0.1F, -16, 4, 0, 32, 0, false);

		roof_exterior = createModelPart();
		roof_exterior.setPivot(0, 24, 0);
		roof_exterior.setTextureUVOffset(52, 69).addCuboid(-6, -42, -16, 6, 0, 32, 0, false);

		outer_roof_5_r1 = createModelPart();
		outer_roof_5_r1.setPivot(-9.9394F, -41.3064F, 0);
		roof_exterior.addChild(outer_roof_5_r1);
		setRotationAngle(outer_roof_5_r1, 0, 0, -0.1745F);
		outer_roof_5_r1.setTextureUVOffset(36, 69).addCuboid(-4, 0, -16, 8, 0, 32, 0, false);

		outer_roof_4_r1 = createModelPart();
		outer_roof_4_r1.setPivot(-15.1778F, -39.8628F, 0);
		roof_exterior.addChild(outer_roof_4_r1);
		setRotationAngle(outer_roof_4_r1, 0, 0, -0.5236F);
		outer_roof_4_r1.setTextureUVOffset(74, 69).addCuboid(-1.5F, 0, -16, 3, 0, 32, 0, false);

		outer_roof_3_r1 = createModelPart();
		outer_roof_3_r1.setPivot(-16.9769F, -38.2468F, 0);
		roof_exterior.addChild(outer_roof_3_r1);
		setRotationAngle(outer_roof_3_r1, 0, 0, -1.0472F);
		outer_roof_3_r1.setTextureUVOffset(110, 0).addCuboid(-1, 0, -16, 2, 0, 32, 0, false);

		outer_roof_2_r1 = createModelPart();
		outer_roof_2_r1.setPivot(-17.5872F, -36.3872F, 0);
		roof_exterior.addChild(outer_roof_2_r1);
		setRotationAngle(outer_roof_2_r1, 0, 0, 0.1107F);
		outer_roof_2_r1.setTextureUVOffset(66, 178).addCuboid(0, -1, -16, 0, 2, 32, 0, false);

		outer_roof_1_r1 = createModelPart();
		outer_roof_1_r1.setPivot(-20, -13, 0);
		roof_exterior.addChild(outer_roof_1_r1);
		setRotationAngle(outer_roof_1_r1, 0, 0, 0.1107F);
		outer_roof_1_r1.setTextureUVOffset(160, 330).addCuboid(-1, -23, -16, 1, 4, 32, 0, false);

		door = createModelPart();
		door.setPivot(0, 24, 0);
		door.setTextureUVOffset(232, 35).addCuboid(-20, 0, -16, 20, 1, 32, 0, false);

		door_left = createModelPart();
		door_left.setPivot(0, 0, 0);
		door.addChild(door_left);
		door_left.setTextureUVOffset(232, 35).addCuboid(-20.8F, -13, 0, 1, 13, 15, 0, false);

		door_left_top_r1 = createModelPart();
		door_left_top_r1.setPivot(-20.8F, -13, 0);
		door_left.addChild(door_left_top_r1);
		setRotationAngle(door_left_top_r1, 0, 0, 0.1107F);
		door_left_top_r1.setTextureUVOffset(194, 35).addCuboid(0, -20, 0, 1, 20, 15, 0, false);

		door_right = createModelPart();
		door_right.setPivot(0, 0, 0);
		door.addChild(door_right);
		door_right.setTextureUVOffset(174, 123).addCuboid(-20.8F, -13, -15, 1, 13, 15, 0, false);

		door_right_top_r1 = createModelPart();
		door_right_top_r1.setPivot(-20.8F, -13, 0);
		door_right.addChild(door_right_top_r1);
		setRotationAngle(door_right_top_r1, 0, 0, 0.1107F);
		door_right_top_r1.setTextureUVOffset(0, 123).addCuboid(0, -20, -15, 1, 20, 15, 0, false);

		door_handrails = createModelPart();
		door_handrails.setPivot(0, 24, 0);
		door_handrails.setTextureUVOffset(8, 0).addCuboid(0, -33, 0, 0, 33, 0, 0.2F, false);

		handrail_8_r2 = createModelPart();
		handrail_8_r2.setPivot(0, 0, 0);
		door_handrails.addChild(handrail_8_r2);
		setRotationAngle(handrail_8_r2, -1.5708F, 0, 0);
		handrail_8_r2.setTextureUVOffset(0, 0).addCuboid(0, -16, -31.5F, 0, 32, 0, 0.2F, false);

		door_exterior = createModelPart();
		door_exterior.setPivot(0, 24, 0);
		door_exterior.setTextureUVOffset(72, 323).addCuboid(-21, 0, -16, 1, 7, 32, 0, false);

		door_left_exterior = createModelPart();
		door_left_exterior.setPivot(0, 0, 0);
		door_exterior.addChild(door_left_exterior);
		door_left_exterior.setTextureUVOffset(128, 20).addCuboid(-20.8F, -13, 0, 0, 13, 15, 0, false);

		door_left_top_r2 = createModelPart();
		door_left_top_r2.setPivot(-20.8F, -13, 0);
		door_left_exterior.addChild(door_left_top_r2);
		setRotationAngle(door_left_top_r2, 0, 0, 0.1107F);
		door_left_top_r2.setTextureUVOffset(82, 128).addCuboid(0, -20, 0, 0, 20, 15, 0, false);

		door_right_exterior = createModelPart();
		door_right_exterior.setPivot(0, 0, 0);
		door_exterior.addChild(door_right_exterior);
		door_right_exterior.setTextureUVOffset(0, 75).addCuboid(-20.8F, -13, -15, 0, 13, 15, 0, false);

		door_right_top_r2 = createModelPart();
		door_right_top_r2.setPivot(-20.8F, -13, 0);
		door_right_exterior.addChild(door_right_top_r2);
		setRotationAngle(door_right_top_r2, 0, 0, 0.1107F);
		door_right_top_r2.setTextureUVOffset(0, 34).addCuboid(0, -20, -15, 0, 20, 15, 0, false);

		end = createModelPart();
		end.setPivot(0, 24, 0);
		end.setTextureUVOffset(0, 0).addCuboid(-20, 0, -32, 40, 1, 48, 0, false);
		end.setTextureUVOffset(174, 102).addCuboid(18, -13, -36, 2, 13, 54, 0, true);
		end.setTextureUVOffset(174, 35).addCuboid(-20, -13, -36, 2, 13, 54, 0, false);
		end.setTextureUVOffset(0, 197).addCuboid(6, -32, -36, 12, 32, 12, 0, false);
		end.setTextureUVOffset(66, 212).addCuboid(-18, -32, -36, 12, 32, 12, 0, false);
		end.setTextureUVOffset(242, 14).addCuboid(-18, -35, -36, 36, 3, 12, 0, false);

		upper_wall_2_r1 = createModelPart();
		upper_wall_2_r1.setPivot(-20, -13, 0);
		end.addChild(upper_wall_2_r1);
		setRotationAngle(upper_wall_2_r1, 0, 0, 0.1107F);
		upper_wall_2_r1.setTextureUVOffset(116, 49).addCuboid(0, -20, -36, 2, 20, 54, 0, false);

		upper_wall_1_r1 = createModelPart();
		upper_wall_1_r1.setPivot(20, -13, 0);
		end.addChild(upper_wall_1_r1);
		setRotationAngle(upper_wall_1_r1, 0, 0, -0.1107F);
		upper_wall_1_r1.setTextureUVOffset(0, 123).addCuboid(-2, -20, -36, 2, 20, 54, 0, true);

		seat_1 = createModelPart();
		seat_1.setPivot(0, 0, 0);
		end.addChild(seat_1);
		seat_1.setTextureUVOffset(116, 165).addCuboid(6, -6, -23, 12, 1, 7, 0, false);

		seat_back_r2 = createModelPart();
		seat_back_r2.setPivot(0, -6, -22);
		seat_1.addChild(seat_back_r2);
		setRotationAngle(seat_back_r2, 0.0524F, 0, 0);
		seat_back_r2.setTextureUVOffset(0, 241).addCuboid(6, -8, -1, 12, 8, 1, 0, false);

		seat_2 = createModelPart();
		seat_2.setPivot(0, 0, 0);
		end.addChild(seat_2);
		seat_2.setTextureUVOffset(58, 164).addCuboid(6, -6, -11, 12, 1, 7, 0, false);

		seat_back_r3 = createModelPart();
		seat_back_r3.setPivot(0, -6, -10);
		seat_2.addChild(seat_back_r3);
		setRotationAngle(seat_back_r3, 0.0524F, 0, 0);
		seat_back_r3.setTextureUVOffset(172, 219).addCuboid(6, -8, -1, 12, 8, 1, 0, false);

		seat_3 = createModelPart();
		seat_3.setPivot(0, 0, 0);
		end.addChild(seat_3);
		seat_3.setTextureUVOffset(174, 169).addCuboid(11, -6, 6, 7, 1, 7, 0, false);

		seat_back_r4 = createModelPart();
		seat_back_r4.setPivot(0, -6, 12);
		seat_3.addChild(seat_back_r4);
		setRotationAngle(seat_back_r4, -0.0524F, 0, 0);
		seat_back_r4.setTextureUVOffset(0, 80).addCuboid(11, -8, 0, 7, 8, 1, 0, false);

		seat_4 = createModelPart();
		seat_4.setPivot(0, 0, 0);
		end.addChild(seat_4);
		seat_4.setTextureUVOffset(116, 165).addCuboid(-18, -6, -23, 12, 1, 7, 0, true);

		seat_back_r5 = createModelPart();
		seat_back_r5.setPivot(0, -6, -22);
		seat_4.addChild(seat_back_r5);
		setRotationAngle(seat_back_r5, 0.0524F, 0, 0);
		seat_back_r5.setTextureUVOffset(0, 241).addCuboid(-18, -8, -1, 12, 8, 1, 0, true);

		seat_5 = createModelPart();
		seat_5.setPivot(0, 0, 0);
		end.addChild(seat_5);
		seat_5.setTextureUVOffset(58, 164).addCuboid(-18, -6, -11, 12, 1, 7, 0, true);

		seat_back_r6 = createModelPart();
		seat_back_r6.setPivot(0, -6, -10);
		seat_5.addChild(seat_back_r6);
		setRotationAngle(seat_back_r6, 0.0524F, 0, 0);
		seat_back_r6.setTextureUVOffset(172, 219).addCuboid(-18, -8, -1, 12, 8, 1, 0, true);

		seat_6 = createModelPart();
		seat_6.setPivot(0, 0, 0);
		end.addChild(seat_6);
		seat_6.setTextureUVOffset(174, 169).addCuboid(-18, -6, 6, 7, 1, 7, 0, true);

		seat_back_r7 = createModelPart();
		seat_back_r7.setPivot(0, -6, 12);
		seat_6.addChild(seat_back_r7);
		setRotationAngle(seat_back_r7, -0.0524F, 0, 0);
		seat_back_r7.setTextureUVOffset(0, 80).addCuboid(-18, -8, 0, 7, 8, 1, 0, true);

		end_exterior = createModelPart();
		end_exterior.setPivot(0, 24, 0);
		end_exterior.setTextureUVOffset(70, 216).addCuboid(20, 0, -28, 1, 7, 44, 0, true);
		end_exterior.setTextureUVOffset(126, 190).addCuboid(-21, 0, -28, 1, 7, 44, 0, false);
		end_exterior.setTextureUVOffset(58, 143).addCuboid(18, -13, -36, 2, 13, 54, 0, true);
		end_exterior.setTextureUVOffset(116, 123).addCuboid(-20, -13, -36, 2, 13, 54, 0, false);
		end_exterior.setTextureUVOffset(0, 260).addCuboid(6, -33, -36, 12, 33, 0, 0, true);
		end_exterior.setTextureUVOffset(0, 260).addCuboid(-18, -33, -36, 12, 33, 0, 0, false);
		end_exterior.setTextureUVOffset(300, 148).addCuboid(-18, -41, -36, 36, 9, 0, 0, false);

		upper_wall_2_r2 = createModelPart();
		upper_wall_2_r2.setPivot(-20, -13, 0);
		end_exterior.addChild(upper_wall_2_r2);
		setRotationAngle(upper_wall_2_r2, 0, 0, 0.1107F);
		upper_wall_2_r2.setTextureUVOffset(0, 49).addCuboid(0, -20, -36, 2, 20, 54, 0, false);

		upper_wall_1_r2 = createModelPart();
		upper_wall_1_r2.setPivot(20, -13, 0);
		end_exterior.addChild(upper_wall_1_r2);
		setRotationAngle(upper_wall_1_r2, 0, 0, -0.1107F);
		upper_wall_1_r2.setTextureUVOffset(58, 69).addCuboid(-2, -20, -36, 2, 20, 54, 0, true);

		roof_end = createModelPart();
		roof_end.setPivot(0, 24, 0);
		roof_end.setTextureUVOffset(134, 49).addCuboid(-18, -32, -24, 4, 0, 40, 0, false);
		roof_end.setTextureUVOffset(18, 49).addCuboid(-10.2292F, -34.8796F, -24, 5, 0, 40, 0, false);
		roof_end.setTextureUVOffset(146, 35).addCuboid(-2, -33, -24, 2, 0, 40, 0, false);
		roof_end.setTextureUVOffset(10, 49).addCuboid(0, -33, -24, 2, 0, 40, 0, true);
		roof_end.setTextureUVOffset(0, 49).addCuboid(5.2292F, -34.8796F, -24, 5, 0, 40, 0, true);
		roof_end.setTextureUVOffset(116, 49).addCuboid(14, -32, -24, 4, 0, 40, 0, true);
		roof_end.setTextureUVOffset(0, 0).addCuboid(0, -33.4899F, -16.9899F, 0, 1, 0, 0.2F, false);
		roof_end.setTextureUVOffset(0, 69).addCuboid(-1, -32, 15, 2, 4, 0, 0, false);
		roof_end.setTextureUVOffset(0, 69).addCuboid(-1, -32, 9, 2, 4, 0, 0, false);
		roof_end.setTextureUVOffset(0, 69).addCuboid(-1, -32, 3, 2, 4, 0, 0, false);

		handrail_4_r1 = createModelPart();
		handrail_4_r1.setPivot(12.2986F, -26.5473F, 0);
		roof_end.addChild(handrail_4_r1);
		setRotationAngle(handrail_4_r1, 0, 0, 0.0873F);
		handrail_4_r1.setTextureUVOffset(4, 0).addCuboid(0, -8, 14.25F, 0, 16, 0, 0.2F, true);

		handrail_3_r2 = createModelPart();
		handrail_3_r2.setPivot(11, -5, 0);
		roof_end.addChild(handrail_3_r2);
		setRotationAngle(handrail_3_r2, 0, 0, 0.0436F);
		handrail_3_r2.setTextureUVOffset(4, 16).addCuboid(0, -13.2F, 14.25F, 0, 13, 0, 0.2F, true);

		handrail_2_r2 = createModelPart();
		handrail_2_r2.setPivot(-12.2986F, -26.5473F, 0);
		roof_end.addChild(handrail_2_r2);
		setRotationAngle(handrail_2_r2, 0, 0, -0.0873F);
		handrail_2_r2.setTextureUVOffset(4, 0).addCuboid(0, -8, 14.25F, 0, 16, 0, 0.2F, false);

		handrail_1_r1 = createModelPart();
		handrail_1_r1.setPivot(-11, -5, 0);
		roof_end.addChild(handrail_1_r1);
		setRotationAngle(handrail_1_r1, 0, 0, -0.0436F);
		handrail_1_r1.setTextureUVOffset(4, 16).addCuboid(0, -13.2F, 14.25F, 0, 13, 0, 0.2F, false);

		handrail_9_r1 = createModelPart();
		handrail_9_r1.setPivot(0, -31.3F, -16.2F);
		roof_end.addChild(handrail_9_r1);
		setRotationAngle(handrail_9_r1, 0.7854F, 0, 0);
		handrail_9_r1.setTextureUVOffset(0, 0).addCuboid(0, -1.2F, 0.2F, 0, 1, 0, 0.2F, false);

		handrail_8_r3 = createModelPart();
		handrail_8_r3.setPivot(0, 0, 0);
		roof_end.addChild(handrail_8_r3);
		setRotationAngle(handrail_8_r3, -1.5708F, 0, 0);
		handrail_8_r3.setTextureUVOffset(0, 0).addCuboid(0, -16, -31.5F, 0, 32, 0, 0.2F, false);

		inner_roof_right_4_r1 = createModelPart();
		inner_roof_right_4_r1.setPivot(14, -32, 0);
		roof_end.addChild(inner_roof_right_4_r1);
		setRotationAngle(inner_roof_right_4_r1, 0, 0, 1.0472F);
		inner_roof_right_4_r1.setTextureUVOffset(142, 35).addCuboid(-2, 0, -24, 2, 0, 40, 0, true);

		inner_roof_right_5_r1 = createModelPart();
		inner_roof_right_5_r1.setPivot(11.6147F, -34.3057F, 0);
		roof_end.addChild(inner_roof_right_5_r1);
		setRotationAngle(inner_roof_right_5_r1, 0, 0, 0.3927F);
		inner_roof_right_5_r1.setTextureUVOffset(8, 123).addCuboid(-1.5F, 0, -24, 3, 0, 40, 0, true);

		inner_roof_right_7_r1 = createModelPart();
		inner_roof_right_7_r1.setPivot(2, -33, 0);
		roof_end.addChild(inner_roof_right_7_r1);
		setRotationAngle(inner_roof_right_7_r1, 0, 0, -0.5236F);
		inner_roof_right_7_r1.setTextureUVOffset(108, 49).addCuboid(0, 0, -24, 4, 0, 40, 0, true);

		inner_roof_left_7_r1 = createModelPart();
		inner_roof_left_7_r1.setPivot(-2, -33, 0);
		roof_end.addChild(inner_roof_left_7_r1);
		setRotationAngle(inner_roof_left_7_r1, 0, 0, 0.5236F);
		inner_roof_left_7_r1.setTextureUVOffset(0, 123).addCuboid(-4, 0, -24, 4, 0, 40, 0, false);

		inner_roof_left_5_r1 = createModelPart();
		inner_roof_left_5_r1.setPivot(-11.6147F, -34.3057F, 0);
		roof_end.addChild(inner_roof_left_5_r1);
		setRotationAngle(inner_roof_left_5_r1, 0, 0, -0.3927F);
		inner_roof_left_5_r1.setTextureUVOffset(124, 49).addCuboid(-1.5F, 0, -24, 3, 0, 40, 0, false);

		inner_roof_left_4_r1 = createModelPart();
		inner_roof_left_4_r1.setPivot(-14, -32, 0);
		roof_end.addChild(inner_roof_left_4_r1);
		setRotationAngle(inner_roof_left_4_r1, 0, 0, -1.0472F);
		inner_roof_left_4_r1.setTextureUVOffset(150, 35).addCuboid(0, 0, -24, 2, 0, 40, 0, false);

		roof_end_light = createModelPart();
		roof_end_light.setPivot(0, 24, 0);


		light_2_r1 = createModelPart();
		light_2_r1.setPivot(2, -33, 0);
		roof_end_light.addChild(light_2_r1);
		setRotationAngle(light_2_r1, 0, 0, -0.5236F);
		light_2_r1.setTextureUVOffset(100, 49).addCuboid(0, -0.1F, -24, 4, 0, 40, 0, false);

		light_1_r1 = createModelPart();
		light_1_r1.setPivot(-2, -33, 0);
		roof_end_light.addChild(light_1_r1);
		setRotationAngle(light_1_r1, 0, 0, 0.5236F);
		light_1_r1.setTextureUVOffset(100, 49).addCuboid(-4, -0.1F, -24, 4, 0, 40, 0, false);

		roof_end_exterior = createModelPart();
		roof_end_exterior.setPivot(0, 24, 0);


		outer_roof_1 = createModelPart();
		outer_roof_1.setPivot(0, 0, 0);
		roof_end_exterior.addChild(outer_roof_1);
		outer_roof_1.setTextureUVOffset(0, 69).addCuboid(-6, -42, -36, 6, 1, 20, 0, false);

		upper_wall_1_r3 = createModelPart();
		upper_wall_1_r3.setPivot(-20, -13, 0);
		outer_roof_1.addChild(upper_wall_1_r3);
		setRotationAngle(upper_wall_1_r3, 0, 0, 0.1107F);
		upper_wall_1_r3.setTextureUVOffset(0, 69).addCuboid(0, -23, -36, 1, 4, 7, 0, false);
		upper_wall_1_r3.setTextureUVOffset(182, 70).addCuboid(-1, -23, -29, 1, 4, 13, 0, false);

		outer_roof_5_r2 = createModelPart();
		outer_roof_5_r2.setPivot(-9.7656F, -40.3206F, 0);
		outer_roof_1.addChild(outer_roof_5_r2);
		setRotationAngle(outer_roof_5_r2, 0, 0, -0.1745F);
		outer_roof_5_r2.setTextureUVOffset(172, 190).addCuboid(-4, -1, -36, 8, 1, 20, 0, false);

		outer_roof_4_r2 = createModelPart();
		outer_roof_4_r2.setPivot(-14.6775F, -38.9948F, 0);
		outer_roof_1.addChild(outer_roof_4_r2);
		setRotationAngle(outer_roof_4_r2, 0, 0, -0.5236F);
		outer_roof_4_r2.setTextureUVOffset(0, 20).addCuboid(-1.5F, -1, -36, 3, 1, 20, 0, false);

		outer_roof_3_r2 = createModelPart();
		outer_roof_3_r2.setPivot(-16.1105F, -37.7448F, 0);
		outer_roof_1.addChild(outer_roof_3_r2);
		setRotationAngle(outer_roof_3_r2, 0, 0, -1.0472F);
		outer_roof_3_r2.setTextureUVOffset(58, 143).addCuboid(-1, -1, -36, 2, 1, 20, 0, false);

		outer_roof_2_r2 = createModelPart();
		outer_roof_2_r2.setPivot(-17.587F, -36.3849F, 0);
		outer_roof_1.addChild(outer_roof_2_r2);
		setRotationAngle(outer_roof_2_r2, 0, 0, 0.1107F);
		outer_roof_2_r2.setTextureUVOffset(116, 143).addCuboid(0, -1, -36, 1, 2, 20, 0, false);

		outer_roof_2 = createModelPart();
		outer_roof_2.setPivot(0, 0, 0);
		roof_end_exterior.addChild(outer_roof_2);
		outer_roof_2.setTextureUVOffset(0, 69).addCuboid(0, -42, -36, 6, 1, 20, 0, true);

		upper_wall_1_r4 = createModelPart();
		upper_wall_1_r4.setPivot(20, -13, 0);
		outer_roof_2.addChild(upper_wall_1_r4);
		setRotationAngle(upper_wall_1_r4, 0, 0, -0.1107F);
		upper_wall_1_r4.setTextureUVOffset(0, 69).addCuboid(-1, -23, -36, 1, 4, 7, 0, true);
		upper_wall_1_r4.setTextureUVOffset(182, 70).addCuboid(0, -23, -29, 1, 4, 13, 0, true);

		outer_roof_5_r3 = createModelPart();
		outer_roof_5_r3.setPivot(9.7656F, -40.3206F, 0);
		outer_roof_2.addChild(outer_roof_5_r3);
		setRotationAngle(outer_roof_5_r3, 0, 0, 0.1745F);
		outer_roof_5_r3.setTextureUVOffset(172, 190).addCuboid(-4, -1, -36, 8, 1, 20, 0, true);

		outer_roof_4_r3 = createModelPart();
		outer_roof_4_r3.setPivot(14.6775F, -38.9948F, 0);
		outer_roof_2.addChild(outer_roof_4_r3);
		setRotationAngle(outer_roof_4_r3, 0, 0, 0.5236F);
		outer_roof_4_r3.setTextureUVOffset(0, 20).addCuboid(-1.5F, -1, -36, 3, 1, 20, 0, true);

		outer_roof_3_r3 = createModelPart();
		outer_roof_3_r3.setPivot(16.1105F, -37.7448F, 0);
		outer_roof_2.addChild(outer_roof_3_r3);
		setRotationAngle(outer_roof_3_r3, 0, 0, 1.0472F);
		outer_roof_3_r3.setTextureUVOffset(58, 143).addCuboid(-1, -1, -36, 2, 1, 20, 0, true);

		outer_roof_2_r3 = createModelPart();
		outer_roof_2_r3.setPivot(17.587F, -36.3849F, 0);
		outer_roof_2.addChild(outer_roof_2_r3);
		setRotationAngle(outer_roof_2_r3, 0, 0, -0.1107F);
		outer_roof_2_r3.setTextureUVOffset(116, 143).addCuboid(-1, -1, -36, 1, 2, 20, 0, true);

		roof_end_vents = createModelPart();
		roof_end_vents.setPivot(0, 24, 0);
		roof_end_vents.setTextureUVOffset(180, 169).addCuboid(-8, -43, -21, 16, 2, 48, 0, false);

		vent_3_r1 = createModelPart();
		vent_3_r1.setPivot(-8, -43, 12);
		roof_end_vents.addChild(vent_3_r1);
		setRotationAngle(vent_3_r1, 0, 0, -0.3491F);
		vent_3_r1.setTextureUVOffset(0, 210).addCuboid(-9, 0, -33, 9, 2, 48, 0, false);

		vent_2_r1 = createModelPart();
		vent_2_r1.setPivot(8, -43, 12);
		roof_end_vents.addChild(vent_2_r1);
		setRotationAngle(vent_2_r1, 0, 0, 0.3491F);
		vent_2_r1.setTextureUVOffset(0, 210).addCuboid(0, 0, -33, 9, 2, 48, 0, true);

		head = createModelPart();
		head.setPivot(0, 24, 0);
		head.setTextureUVOffset(128, 0).addCuboid(-20, 0, -16, 40, 1, 34, 0, false);
		head.setTextureUVOffset(116, 298).addCuboid(-20, -13, -18, 2, 13, 36, 0, false);
		head.setTextureUVOffset(293, 240).addCuboid(18, -13, -18, 2, 13, 36, 0, true);
		head.setTextureUVOffset(322, 289).addCuboid(-18, -36, 18, 36, 36, 0, 0, false);

		upper_wall_2_r3 = createModelPart();
		upper_wall_2_r3.setPivot(20, -13, 0);
		head.addChild(upper_wall_2_r3);
		setRotationAngle(upper_wall_2_r3, 0, 0, -0.1107F);
		upper_wall_2_r3.setTextureUVOffset(0, 260).addCuboid(-2, -20, -18, 2, 20, 36, 0, true);

		upper_wall_1_r5 = createModelPart();
		upper_wall_1_r5.setPivot(-20, -13, 0);
		head.addChild(upper_wall_1_r5);
		setRotationAngle(upper_wall_1_r5, 0, 0, 0.1107F);
		upper_wall_1_r5.setTextureUVOffset(260, 135).addCuboid(0, -20, -18, 2, 20, 36, 0, false);

		head_exterior = createModelPart();
		head_exterior.setPivot(0, 24, 0);
		head_exterior.setTextureUVOffset(232, 68).addCuboid(-21, 0, 19, 42, 7, 12, 0, false);
		head_exterior.setTextureUVOffset(305, 52).addCuboid(20, 0, -16, 1, 7, 35, 0, true);
		head_exterior.setTextureUVOffset(304, 0).addCuboid(-21, 0, -16, 1, 7, 35, 0, false);
		head_exterior.setTextureUVOffset(281, 289).addCuboid(18, -13, -18, 2, 13, 37, 0, true);
		head_exterior.setTextureUVOffset(256, 219).addCuboid(20, -13, 19, 1, 13, 12, 0, true);
		head_exterior.setTextureUVOffset(240, 276).addCuboid(-20, -13, -18, 2, 13, 37, 0, false);
		head_exterior.setTextureUVOffset(256, 219).addCuboid(-21, -13, 19, 1, 13, 12, 0, false);
		head_exterior.setTextureUVOffset(0, 316).addCuboid(-18, -42, 19, 36, 42, 0, 0, false);

		driver_door_upper_2_r1 = createModelPart();
		driver_door_upper_2_r1.setPivot(-21, -13, 0);
		head_exterior.addChild(driver_door_upper_2_r1);
		setRotationAngle(driver_door_upper_2_r1, 0, 0, 0.1107F);
		driver_door_upper_2_r1.setTextureUVOffset(232, 102).addCuboid(0, -20, 19, 1, 20, 12, 0, false);

		upper_wall_2_r4 = createModelPart();
		upper_wall_2_r4.setPivot(-20, -13, 0);
		head_exterior.addChild(upper_wall_2_r4);
		setRotationAngle(upper_wall_2_r4, 0, 0, 0.1107F);
		upper_wall_2_r4.setTextureUVOffset(123, 241).addCuboid(0, -20, -18, 2, 20, 37, 0, false);

		driver_door_upper_1_r1 = createModelPart();
		driver_door_upper_1_r1.setPivot(21, -13, 0);
		head_exterior.addChild(driver_door_upper_1_r1);
		setRotationAngle(driver_door_upper_1_r1, 0, 0, -0.1107F);
		driver_door_upper_1_r1.setTextureUVOffset(232, 102).addCuboid(-1, -20, 19, 1, 20, 12, 0, true);

		upper_wall_1_r6 = createModelPart();
		upper_wall_1_r6.setPivot(20, -13, 0);
		head_exterior.addChild(upper_wall_1_r6);
		setRotationAngle(upper_wall_1_r6, 0, 0, -0.1107F);
		upper_wall_1_r6.setTextureUVOffset(251, 219).addCuboid(-2, -20, -18, 2, 20, 37, 0, true);

		front = createModelPart();
		front.setPivot(0, 0, 0);
		head_exterior.addChild(front);
		front.setTextureUVOffset(116, 143).addCuboid(-3, -10, 45, 6, 5, 0, 0, false);
		front.setTextureUVOffset(228, 0).addCuboid(-21, 0, 31, 42, 0, 14, 0, false);

		front_middle_top_2_r1 = createModelPart();
		front_middle_top_2_r1.setPivot(0, -36.1042F, 37.8711F);
		front.addChild(front_middle_top_2_r1);
		setRotationAngle(front_middle_top_2_r1, -0.7854F, 0, 0);
		front_middle_top_2_r1.setTextureUVOffset(149, 35).addCuboid(-6, 0, -3.5F, 12, 0, 8, 0, false);

		front_middle_top_1_r1 = createModelPart();
		front_middle_top_1_r1.setPivot(0, -42, 26);
		front.addChild(front_middle_top_1_r1);
		setRotationAngle(front_middle_top_1_r1, -0.3491F, 0, 0);
		front_middle_top_1_r1.setTextureUVOffset(20, 90).addCuboid(-6, 0, 0, 12, 0, 10, 0, false);

		front_3_r1 = createModelPart();
		front_3_r1.setPivot(0, -5, 45);
		front.addChild(front_3_r1);
		setRotationAngle(front_3_r1, -0.0873F, 0, 0);
		front_3_r1.setTextureUVOffset(58, 143).addCuboid(-3, 0, 0, 6, 12, 0, 0, false);

		front_1_r1 = createModelPart();
		front_1_r1.setPivot(0, -10, 45);
		front.addChild(front_1_r1);
		setRotationAngle(front_1_r1, 0.1745F, 0, 0);
		front_1_r1.setTextureUVOffset(260, 191).addCuboid(-3, -24, 0, 6, 24, 0, 0, false);

		side_1 = createModelPart();
		side_1.setPivot(0, 0, 0);
		front.addChild(side_1);
		side_1.setTextureUVOffset(201, 277).addCuboid(0, -42, 16, 6, 1, 10, 0, false);

		outer_roof_11_r1 = createModelPart();
		outer_roof_11_r1.setPivot(18.7914F, -32.8777F, 31);
		side_1.addChild(outer_roof_11_r1);
		setRotationAngle(outer_roof_11_r1, -1.0472F, 0.9948F, 0);
		outer_roof_11_r1.setTextureUVOffset(106, 267).addCuboid(-9.0009F, 0.0014F, -6, 11, 0, 10, 0, false);

		outer_roof_10_r1 = createModelPart();
		outer_roof_10_r1.setPivot(5.4063F, -38.6327F, 35.3973F);
		side_1.addChild(outer_roof_10_r1);
		setRotationAngle(outer_roof_10_r1, -0.8378F, 0.0873F, 0.2618F);
		outer_roof_10_r1.setTextureUVOffset(66, 293).addCuboid(-3, 0.001F, -2, 14, 0, 10, 0, false);

		outer_roof_9_r1 = createModelPart();
		outer_roof_9_r1.setPivot(6, -42, 26);
		side_1.addChild(outer_roof_9_r1);
		setRotationAngle(outer_roof_9_r1, -0.3491F, 0, 0.1745F);
		outer_roof_9_r1.setTextureUVOffset(0, 0).addCuboid(0, 0, 0, 8, 0, 10, 0, false);

		outer_roof_8_r1 = createModelPart();
		outer_roof_8_r1.setPivot(18.682F, -33.8719F, 31);
		side_1.addChild(outer_roof_8_r1);
		setRotationAngle(outer_roof_8_r1, 0, 0, 1.1345F);
		outer_roof_8_r1.setTextureUVOffset(145, 89).addCuboid(-6, 0.001F, -5, 6, 0, 5, 0, false);

		outer_roof_7_r1 = createModelPart();
		outer_roof_7_r1.setPivot(13.8776F, -40.6102F, 26);
		side_1.addChild(outer_roof_7_r1);
		setRotationAngle(outer_roof_7_r1, -0.3491F, 0, 0.5236F);
		outer_roof_7_r1.setTextureUVOffset(130, 89).addCuboid(0, 0, 0, 5, 0, 10, 0, false);

		outer_roof_5_r4 = createModelPart();
		outer_roof_5_r4.setPivot(9.9394F, -41.3064F, 0);
		side_1.addChild(outer_roof_5_r4);
		setRotationAngle(outer_roof_5_r4, 0, 0, 0.1745F);
		outer_roof_5_r4.setTextureUVOffset(116, 249).addCuboid(-4, 0, 16, 8, 1, 10, 0, false);

		outer_roof_4_r4 = createModelPart();
		outer_roof_4_r4.setPivot(15.1778F, -39.8628F, 0);
		side_1.addChild(outer_roof_4_r4);
		setRotationAngle(outer_roof_4_r4, 0, 0, 0.5236F);
		outer_roof_4_r4.setTextureUVOffset(156, 309).addCuboid(-1.5F, 0, 16, 3, 1, 10, 0, false);

		outer_roof_3_r4 = createModelPart();
		outer_roof_3_r4.setPivot(16.9769F, -38.2468F, 0);
		side_1.addChild(outer_roof_3_r4);
		setRotationAngle(outer_roof_3_r4, 0, 0, 1.0472F);
		outer_roof_3_r4.setTextureUVOffset(156, 298).addCuboid(-1, 0, 16, 2, 1, 10, 0, false);

		outer_roof_2_r4 = createModelPart();
		outer_roof_2_r4.setPivot(17.5872F, -36.3872F, 0);
		side_1.addChild(outer_roof_2_r4);
		setRotationAngle(outer_roof_2_r4, 0, 0, -0.1107F);
		outer_roof_2_r4.setTextureUVOffset(138, 143).addCuboid(-1, -1, 16, 1, 2, 10, 0, true);

		outer_roof_1_r2 = createModelPart();
		outer_roof_1_r2.setPivot(20, -13, 5);
		side_1.addChild(outer_roof_1_r2);
		setRotationAngle(outer_roof_1_r2, 0, 0, -0.1107F);
		outer_roof_1_r2.setTextureUVOffset(245, 136).addCuboid(-1, -23, 11, 2, 4, 15, 0, true);

		front_side_lower_5_r1 = createModelPart();
		front_side_lower_5_r1.setPivot(8.7065F, -8.5F, 43.6502F);
		side_1.addChild(front_side_lower_5_r1);
		setRotationAngle(front_side_lower_5_r1, 0, 0.2182F, 0);
		front_side_lower_5_r1.setTextureUVOffset(116, 241).addCuboid(-7, -3.5F, 0, 14, 7, 0, 0, false);

		front_side_lower_4_r1 = createModelPart();
		front_side_lower_4_r1.setPivot(17.6615F, -8.5F, 40.0141F);
		side_1.addChild(front_side_lower_4_r1);
		setRotationAngle(front_side_lower_4_r1, 0, 0.7854F, 0);
		front_side_lower_4_r1.setTextureUVOffset(26, 32).addCuboid(-3, -3.5F, 0, 6, 7, 0, 0, false);

		front_side_lower_3_r1 = createModelPart();
		front_side_lower_3_r1.setPivot(1.8727F, -5, 45.1663F);
		side_1.addChild(front_side_lower_3_r1);
		setRotationAngle(front_side_lower_3_r1, -0.0873F, 0.2182F, 0);
		front_side_lower_3_r1.setTextureUVOffset(197, 70).addCuboid(0, 0, -0.001F, 15, 12, 0, 0, false);

		front_side_lower_2_r1 = createModelPart();
		front_side_lower_2_r1.setPivot(15.5408F, -5, 42.1361F);
		side_1.addChild(front_side_lower_2_r1);
		setRotationAngle(front_side_lower_2_r1, -0.0436F, 0.7854F, 0);
		front_side_lower_2_r1.setTextureUVOffset(0, 123).addCuboid(0, 0, -0.001F, 7, 12, 0, 0, false);

		front_side_lower_1_r1 = createModelPart();
		front_side_lower_1_r1.setPivot(21, 7, 31);
		side_1.addChild(front_side_lower_1_r1);
		setRotationAngle(front_side_lower_1_r1, 0, -0.1745F, 0);
		front_side_lower_1_r1.setTextureUVOffset(116, 203).addCuboid(0, -20, 0, 0, 20, 7, 0, false);

		front_side_upper_7_r1 = createModelPart();
		front_side_upper_7_r1.setPivot(15.2976F, -12.6432F, 41.454F);
		side_1.addChild(front_side_upper_7_r1);
		setRotationAngle(front_side_upper_7_r1, 0.4102F, 0.2059F, -0.0436F);
		front_side_upper_7_r1.setTextureUVOffset(58, 172).addCuboid(-13, -1, -0.001F, 13, 4, 0, 0, false);

		front_side_upper_6_r1 = createModelPart();
		front_side_upper_6_r1.setPivot(19.1F, -12.95F, 37.7F);
		side_1.addChild(front_side_upper_6_r1);
		setRotationAngle(front_side_upper_6_r1, 0.3491F, 0.7679F, -0.0436F);
		front_side_upper_6_r1.setTextureUVOffset(28, 43).addCuboid(-6.35F, -0.85F, 0, 7, 3, 0, 0, false);

		front_side_upper_5_r1 = createModelPart();
		front_side_upper_5_r1.setPivot(20.4813F, -12.9424F, 33.9542F);
		side_1.addChild(front_side_upper_5_r1);
		setRotationAngle(front_side_upper_5_r1, 0.4363F, 1.2217F, 0);
		front_side_upper_5_r1.setTextureUVOffset(30, 100).addCuboid(-4.9998F, -0.9997F, -0.0001F, 5, 3, 0, 0, false);

		front_side_upper_4_r1 = createModelPart();
		front_side_upper_4_r1.setPivot(3, -10, 45);
		side_1.addChild(front_side_upper_4_r1);
		setRotationAngle(front_side_upper_4_r1, 0.1745F, 0.2443F, 0);
		front_side_upper_4_r1.setTextureUVOffset(76, 323).addCuboid(0, -24, 0, 14, 24, 0, 0, false);

		front_side_upper_3_r1 = createModelPart();
		front_side_upper_3_r1.setPivot(19.1695F, -12.1451F, 37.781F);
		side_1.addChild(front_side_upper_3_r1);
		setRotationAngle(front_side_upper_3_r1, 0.1309F, 0.7679F, 0);
		front_side_upper_3_r1.setTextureUVOffset(276, 191).addCuboid(-5.9989F, -21.9999F, 0.0149F, 7, 23, 0, 0, false);

		front_side_upper_2_r1 = createModelPart();
		front_side_upper_2_r1.setPivot(19.156F, -22.9499F, 34.5398F);
		side_1.addChild(front_side_upper_2_r1);
		setRotationAngle(front_side_upper_2_r1, 0.0873F, -0.3491F, -0.1107F);
		front_side_upper_2_r1.setTextureUVOffset(136, 206).addCuboid(0, -9, -1.5F, 0, 20, 4, 0, false);

		front_side_upper_1_r1 = createModelPart();
		front_side_upper_1_r1.setPivot(21, -13, 31);
		side_1.addChild(front_side_upper_1_r1);
		setRotationAngle(front_side_upper_1_r1, 0, -0.1745F, -0.1107F);
		front_side_upper_1_r1.setTextureUVOffset(130, 207).addCuboid(0, -20, 0, 0, 20, 3, 0, false);

		side_2 = createModelPart();
		side_2.setPivot(0, 0, 0);
		front.addChild(side_2);
		side_2.setTextureUVOffset(201, 277).addCuboid(-6, -42, 16, 6, 1, 10, 0, true);

		outer_roof_11_r2 = createModelPart();
		outer_roof_11_r2.setPivot(-18.7914F, -32.8777F, 31);
		side_2.addChild(outer_roof_11_r2);
		setRotationAngle(outer_roof_11_r2, -1.0472F, -0.9948F, 0);
		outer_roof_11_r2.setTextureUVOffset(106, 267).addCuboid(-1.9991F, 0.0014F, -6, 11, 0, 10, 0, true);

		outer_roof_10_r2 = createModelPart();
		outer_roof_10_r2.setPivot(-5.4063F, -38.6327F, 35.3973F);
		side_2.addChild(outer_roof_10_r2);
		setRotationAngle(outer_roof_10_r2, -0.8378F, -0.0873F, -0.2618F);
		outer_roof_10_r2.setTextureUVOffset(66, 293).addCuboid(-11, 0.001F, -2, 14, 0, 10, 0, true);

		outer_roof_9_r2 = createModelPart();
		outer_roof_9_r2.setPivot(-6, -42, 26);
		side_2.addChild(outer_roof_9_r2);
		setRotationAngle(outer_roof_9_r2, -0.3491F, 0, -0.1745F);
		outer_roof_9_r2.setTextureUVOffset(0, 0).addCuboid(-8, 0, 0, 8, 0, 10, 0, true);

		outer_roof_8_r2 = createModelPart();
		outer_roof_8_r2.setPivot(-18.682F, -33.8719F, 31);
		side_2.addChild(outer_roof_8_r2);
		setRotationAngle(outer_roof_8_r2, 0, 0, -1.1345F);
		outer_roof_8_r2.setTextureUVOffset(145, 89).addCuboid(0, 0.001F, -5, 6, 0, 5, 0, true);

		outer_roof_7_r2 = createModelPart();
		outer_roof_7_r2.setPivot(-13.8776F, -40.6102F, 26);
		side_2.addChild(outer_roof_7_r2);
		setRotationAngle(outer_roof_7_r2, -0.3491F, 0, -0.5236F);
		outer_roof_7_r2.setTextureUVOffset(130, 89).addCuboid(-5, 0, 0, 5, 0, 10, 0, true);

		outer_roof_5_r5 = createModelPart();
		outer_roof_5_r5.setPivot(-9.9394F, -41.3064F, 0);
		side_2.addChild(outer_roof_5_r5);
		setRotationAngle(outer_roof_5_r5, 0, 0, -0.1745F);
		outer_roof_5_r5.setTextureUVOffset(116, 249).addCuboid(-4, 0, 16, 8, 1, 10, 0, true);

		outer_roof_4_r5 = createModelPart();
		outer_roof_4_r5.setPivot(-15.1778F, -39.8628F, 0);
		side_2.addChild(outer_roof_4_r5);
		setRotationAngle(outer_roof_4_r5, 0, 0, -0.5236F);
		outer_roof_4_r5.setTextureUVOffset(156, 309).addCuboid(-1.5F, 0, 16, 3, 1, 10, 0, true);

		outer_roof_3_r5 = createModelPart();
		outer_roof_3_r5.setPivot(-16.9769F, -38.2468F, 0);
		side_2.addChild(outer_roof_3_r5);
		setRotationAngle(outer_roof_3_r5, 0, 0, -1.0472F);
		outer_roof_3_r5.setTextureUVOffset(156, 298).addCuboid(-1, 0, 16, 2, 1, 10, 0, true);

		outer_roof_2_r5 = createModelPart();
		outer_roof_2_r5.setPivot(-17.5872F, -36.3872F, 0);
		side_2.addChild(outer_roof_2_r5);
		setRotationAngle(outer_roof_2_r5, 0, 0, 0.1107F);
		outer_roof_2_r5.setTextureUVOffset(138, 143).addCuboid(0, -1, 16, 1, 2, 10, 0, false);

		outer_roof_1_r3 = createModelPart();
		outer_roof_1_r3.setPivot(-20, -13, 5);
		side_2.addChild(outer_roof_1_r3);
		setRotationAngle(outer_roof_1_r3, 0, 0, 0.1107F);
		outer_roof_1_r3.setTextureUVOffset(245, 136).addCuboid(-1, -23, 11, 2, 4, 15, 0, false);

		front_side_lower_6_r1 = createModelPart();
		front_side_lower_6_r1.setPivot(-8.7065F, -8.5F, 43.6502F);
		side_2.addChild(front_side_lower_6_r1);
		setRotationAngle(front_side_lower_6_r1, 0, -0.2182F, 0);
		front_side_lower_6_r1.setTextureUVOffset(116, 241).addCuboid(-7, -3.5F, 0, 14, 7, 0, 0, true);

		front_side_lower_5_r2 = createModelPart();
		front_side_lower_5_r2.setPivot(-17.6615F, -8.5F, 40.0141F);
		side_2.addChild(front_side_lower_5_r2);
		setRotationAngle(front_side_lower_5_r2, 0, -0.7854F, 0);
		front_side_lower_5_r2.setTextureUVOffset(26, 32).addCuboid(-3, -3.5F, 0, 6, 7, 0, 0, true);

		front_side_lower_4_r2 = createModelPart();
		front_side_lower_4_r2.setPivot(-1.8727F, -5, 45.1663F);
		side_2.addChild(front_side_lower_4_r2);
		setRotationAngle(front_side_lower_4_r2, -0.0873F, -0.2182F, 0);
		front_side_lower_4_r2.setTextureUVOffset(197, 70).addCuboid(-15, 0, -0.001F, 15, 12, 0, 0, true);

		front_side_lower_3_r2 = createModelPart();
		front_side_lower_3_r2.setPivot(-15.5408F, -5, 42.1361F);
		side_2.addChild(front_side_lower_3_r2);
		setRotationAngle(front_side_lower_3_r2, -0.0436F, -0.7854F, 0);
		front_side_lower_3_r2.setTextureUVOffset(0, 123).addCuboid(-7, 0, -0.001F, 7, 12, 0, 0, true);

		front_side_lower_2_r2 = createModelPart();
		front_side_lower_2_r2.setPivot(-21, 7, 31);
		side_2.addChild(front_side_lower_2_r2);
		setRotationAngle(front_side_lower_2_r2, 0, 0.1745F, 0);
		front_side_lower_2_r2.setTextureUVOffset(116, 203).addCuboid(0, -20, 0, 0, 20, 7, 0, true);

		front_side_upper_8_r1 = createModelPart();
		front_side_upper_8_r1.setPivot(-15.2976F, -12.6432F, 41.454F);
		side_2.addChild(front_side_upper_8_r1);
		setRotationAngle(front_side_upper_8_r1, 0.4102F, -0.2059F, 0.0436F);
		front_side_upper_8_r1.setTextureUVOffset(58, 172).addCuboid(0, -1, -0.001F, 13, 4, 0, 0, true);

		front_side_upper_7_r2 = createModelPart();
		front_side_upper_7_r2.setPivot(-19.1F, -12.95F, 37.7F);
		side_2.addChild(front_side_upper_7_r2);
		setRotationAngle(front_side_upper_7_r2, 0.3491F, -0.7679F, 0.0436F);
		front_side_upper_7_r2.setTextureUVOffset(28, 43).addCuboid(-0.65F, -0.85F, 0, 7, 3, 0, 0, true);

		front_side_upper_6_r2 = createModelPart();
		front_side_upper_6_r2.setPivot(-20.4813F, -12.9424F, 33.9542F);
		side_2.addChild(front_side_upper_6_r2);
		setRotationAngle(front_side_upper_6_r2, 0.4363F, -1.2217F, 0);
		front_side_upper_6_r2.setTextureUVOffset(30, 100).addCuboid(-0.0002F, -0.9997F, -0.0001F, 5, 3, 0, 0, true);

		front_side_upper_5_r2 = createModelPart();
		front_side_upper_5_r2.setPivot(-3, -10, 45);
		side_2.addChild(front_side_upper_5_r2);
		setRotationAngle(front_side_upper_5_r2, 0.1745F, -0.2443F, 0);
		front_side_upper_5_r2.setTextureUVOffset(76, 323).addCuboid(-14, -24, 0, 14, 24, 0, 0, true);

		front_side_upper_4_r2 = createModelPart();
		front_side_upper_4_r2.setPivot(-19.1695F, -12.1451F, 37.781F);
		side_2.addChild(front_side_upper_4_r2);
		setRotationAngle(front_side_upper_4_r2, 0.1309F, -0.7679F, 0);
		front_side_upper_4_r2.setTextureUVOffset(276, 191).addCuboid(-1.0011F, -21.9999F, 0.0149F, 7, 23, 0, 0, true);

		front_side_upper_3_r2 = createModelPart();
		front_side_upper_3_r2.setPivot(-19.156F, -22.9499F, 34.5398F);
		side_2.addChild(front_side_upper_3_r2);
		setRotationAngle(front_side_upper_3_r2, 0.0873F, 0.3491F, 0.1107F);
		front_side_upper_3_r2.setTextureUVOffset(136, 205).addCuboid(0, -10, -1.5F, 0, 21, 4, 0, true);

		front_side_upper_2_r2 = createModelPart();
		front_side_upper_2_r2.setPivot(-21, -13, 31);
		side_2.addChild(front_side_upper_2_r2);
		setRotationAngle(front_side_upper_2_r2, 0, 0.1745F, 0.1107F);
		front_side_upper_2_r2.setTextureUVOffset(130, 207).addCuboid(0, -20, 0, 0, 20, 3, 0, true);

		headlights = createModelPart();
		headlights.setPivot(0, 24, 0);


		headlight_4_r1 = createModelPart();
		headlight_4_r1.setPivot(-14.0978F, -7, 42.5574F);
		headlights.addChild(headlight_4_r1);
		setRotationAngle(headlight_4_r1, 0, -0.2182F, 0);
		headlight_4_r1.setTextureUVOffset(39, 0).addCuboid(-1.5F, -2, 0, 3, 4, 0, 0, true);

		headlight_3_r1 = createModelPart();
		headlight_3_r1.setPivot(-16.976F, -7, 40.8188F);
		headlights.addChild(headlight_3_r1);
		setRotationAngle(headlight_3_r1, 0, -0.7854F, 0);
		headlight_3_r1.setTextureUVOffset(31, 0).addCuboid(-2, -2, 0, 4, 4, 0, 0, true);

		headlight_2_r1 = createModelPart();
		headlight_2_r1.setPivot(14.0978F, -7, 42.5574F);
		headlights.addChild(headlight_2_r1);
		setRotationAngle(headlight_2_r1, 0, 0.2182F, 0);
		headlight_2_r1.setTextureUVOffset(39, 0).addCuboid(-1.5F, -2, 0, 3, 4, 0, 0, false);

		headlight_1_r1 = createModelPart();
		headlight_1_r1.setPivot(16.976F, -7, 40.8188F);
		headlights.addChild(headlight_1_r1);
		setRotationAngle(headlight_1_r1, 0, 0.7854F, 0);
		headlight_1_r1.setTextureUVOffset(31, 0).addCuboid(-2, -2, 0, 4, 4, 0, 0, false);

		tail_lights = createModelPart();
		tail_lights.setPivot(0, 24, 0);


		tail_light_2_r1 = createModelPart();
		tail_light_2_r1.setPivot(-14.0978F, -7, 42.5574F);
		tail_lights.addChild(tail_light_2_r1);
		setRotationAngle(tail_light_2_r1, 0, -0.2182F, 0);
		tail_light_2_r1.setTextureUVOffset(31, 4).addCuboid(0.5F, -2, 0, 5, 4, 0, 0, true);

		tail_light_1_r1 = createModelPart();
		tail_light_1_r1.setPivot(14.0978F, -7, 42.5574F);
		tail_lights.addChild(tail_light_1_r1);
		setRotationAngle(tail_light_1_r1, 0, 0.2182F, 0);
		tail_light_1_r1.setTextureUVOffset(31, 4).addCuboid(-5.5F, -2, 0, 5, 4, 0, 0, false);

		door_light = createModelPart();
		door_light.setPivot(0, 24, 0);


		outer_roof_1_r4 = createModelPart();
		outer_roof_1_r4.setPivot(-20, -13, 0);
		door_light.addChild(outer_roof_1_r4);
		setRotationAngle(outer_roof_1_r4, 0, 0, 0.1107F);
		outer_roof_1_r4.setTextureUVOffset(58, 85).addCuboid(-1.1F, -23, -2, 0, 4, 4, 0, false);

		door_light_on = createModelPart();
		door_light_on.setPivot(0, 24, 0);


		light_r2 = createModelPart();
		light_r2.setPivot(-20, -13, 0);
		door_light_on.addChild(light_r2);
		setRotationAngle(light_r2, 0, 0, 0.1107F);
		light_r2.setTextureUVOffset(60, 94).addCuboid(-1, -21, 0, 0, 0, 0, 0.4F, false);

		door_light_off = createModelPart();
		door_light_off.setPivot(0, 24, 0);


		light_r3 = createModelPart();
		light_r3.setPivot(-20, -13, 0);
		door_light_off.addChild(light_r3);
		setRotationAngle(light_r3, 0, 0, 0.1107F);
		light_r3.setTextureUVOffset(60, 96).addCuboid(-1, -21, 0, 0, 0, 0, 0.4F, false);

		christmas_tree = createModelPart();
		christmas_tree.setPivot(0, 24, 0);
		christmas_tree.setTextureUVOffset(0, 379).addCuboid(-1.5F, -17.9F, -1.5F, 3, 18, 3, 0, false);
		christmas_tree.setTextureUVOffset(12, 380).addCuboid(-8, -10, -8, 16, 4, 16, 0, false);
		christmas_tree.setTextureUVOffset(20, 384).addCuboid(-6, -14, -6, 12, 4, 12, 0, false);
		christmas_tree.setTextureUVOffset(28, 388).addCuboid(-4, -18, -4, 8, 4, 8, 0, false);
		christmas_tree.setTextureUVOffset(36, 392).addCuboid(-2, -22, -2, 4, 4, 4, 0, false);
		christmas_tree.setTextureUVOffset(40, 396).addCuboid(-1, -24, -1, 2, 2, 2, 0, false);

		present_6_r1 = createModelPart();
		present_6_r1.setPivot(0, 0, 0);
		christmas_tree.addChild(present_6_r1);
		setRotationAngle(present_6_r1, 0, -0.0873F, 0);
		present_6_r1.setTextureUVOffset(72, 390).addCuboid(-2, -3, 3.75F, 3, 3, 3, 0, false);
		present_6_r1.setTextureUVOffset(60, 390).addCuboid(-5, -3, -0.25F, 3, 3, 3, 0, false);
		present_6_r1.setTextureUVOffset(60, 384).addCuboid(-2, -3, -5.25F, 3, 3, 3, 0, false);

		present_5_r1 = createModelPart();
		present_5_r1.setPivot(0, 0, 0);
		christmas_tree.addChild(present_5_r1);
		setRotationAngle(present_5_r1, 0, -0.3927F, 0);
		present_5_r1.setTextureUVOffset(60, 384).addCuboid(2.5F, -3, -0.5F, 3, 3, 3, 0, false);
		present_5_r1.setTextureUVOffset(72, 390).addCuboid(-6.5F, -3, -3.5F, 3, 3, 3, 0, false);

		present_2_r1 = createModelPart();
		present_2_r1.setPivot(0, 0, 0);
		christmas_tree.addChild(present_2_r1);
		setRotationAngle(present_2_r1, 0, 0.0873F, 0);
		present_2_r1.setTextureUVOffset(72, 384).addCuboid(1.75F, -3, -3.5F, 3, 3, 3, 0, false);

		christmas_antler = createModelPart();
		christmas_antler.setPivot(0, 24, 0);


		antler_2_r1 = createModelPart();
		antler_2_r1.setPivot(-10, -7, 0);
		christmas_antler.addChild(antler_2_r1);
		setRotationAngle(antler_2_r1, 0, 0, 1.1781F);
		antler_2_r1.setTextureUVOffset(22, 383).addCuboid(-18, -42.75F, 0, 1, 4, 1, 0, false);
		antler_2_r1.setTextureUVOffset(22, 383).addCuboid(-20, -44.75F, 0, 1, 4, 1, 0, false);

		antler_base_r1 = createModelPart();
		antler_base_r1.setPivot(-10, -7, 0);
		christmas_antler.addChild(antler_base_r1);
		setRotationAngle(antler_base_r1, 0, 0, 0.5236F);
		antler_base_r1.setTextureUVOffset(22, 383).addCuboid(9, -50, 0, 1, 12, 1, 0, false);

		christmas_light_head = createModelPart();
		christmas_light_head.setPivot(0, 24, 0);
		christmas_light_head.setTextureUVOffset(12, 392).addCuboid(-2, -28, 0, 4, 4, 0, 0, false);
		christmas_light_head.setTextureUVOffset(12, 387).addCuboid(-1.5F, -8.25F, 45, 3, 3, 2, 0, false);

		christmas_light_holder = createModelPart();
		christmas_light_holder.setPivot(0, 24, 0);


		c_light_pole_5_r1 = createModelPart();
		c_light_pole_5_r1.setPivot(0, -31.8F, 5.2F);
		christmas_light_holder.addChild(c_light_pole_5_r1);
		setRotationAngle(c_light_pole_5_r1, 1.7453F, 0, 0);
		c_light_pole_5_r1.setTextureUVOffset(85, 385).addCuboid(-12, 0.2F, 0.2F, 0, 5, 0, 0.2F, false);

		c_light_pole_4_r1 = createModelPart();
		c_light_pole_4_r1.setPivot(0, -33.8491F, 12.9867F);
		christmas_light_holder.addChild(c_light_pole_4_r1);
		setRotationAngle(c_light_pole_4_r1, 1.9199F, 0, 0);
		c_light_pole_4_r1.setTextureUVOffset(85, 385).addCuboid(-12, -2.5F, 0, 0, 5, 0, 0.2F, false);

		c_light_pole_3_r1 = createModelPart();
		c_light_pole_3_r1.setPivot(0, -32, 0);
		christmas_light_holder.addChild(c_light_pole_3_r1);
		setRotationAngle(c_light_pole_3_r1, -1.5708F, 0, 0);
		c_light_pole_3_r1.setTextureUVOffset(85, 385).addCuboid(-12, -5, 0, 0, 10, 0, 0.2F, false);

		c_light_pole_2_r1 = createModelPart();
		c_light_pole_2_r1.setPivot(0, -31.8F, -5.2F);
		christmas_light_holder.addChild(c_light_pole_2_r1);
		setRotationAngle(c_light_pole_2_r1, -1.7453F, 0, 0);
		c_light_pole_2_r1.setTextureUVOffset(85, 385).addCuboid(-12, 0.2F, -0.2F, 0, 5, 0, 0.2F, false);

		c_light_pole_1_r1 = createModelPart();
		c_light_pole_1_r1.setPivot(0, -33.8491F, -12.9867F);
		christmas_light_holder.addChild(c_light_pole_1_r1);
		setRotationAngle(c_light_pole_1_r1, -1.9199F, 0, 0);
		c_light_pole_1_r1.setTextureUVOffset(85, 385).addCuboid(-12, -2.5F, 0, 0, 5, 0, 0.2F, false);

		christmas_light_red = createModelPart();
		christmas_light_red.setPivot(0, 24, 0);


		c_light_9_r1 = createModelPart();
		c_light_9_r1.setPivot(0, -31.8F, 5.2F);
		christmas_light_red.addChild(c_light_9_r1);
		setRotationAngle(c_light_9_r1, 1.7453F, 0, 0);
		c_light_9_r1.setTextureUVOffset(61, 385).addCuboid(-12, 1.05F, -0.2F, 0, 0, 0, 0.2F, false);

		c_light_5_r1 = createModelPart();
		c_light_5_r1.setPivot(0, -32, 0);
		christmas_light_red.addChild(c_light_5_r1);
		setRotationAngle(c_light_5_r1, -1.5708F, 0, 0);
		c_light_5_r1.setTextureUVOffset(61, 385).addCuboid(-12, 3.75F, 0.4F, 0, 0, 0, 0.2F, false);

		c_light_1_r1 = createModelPart();
		c_light_1_r1.setPivot(0, -33.5402F, -10.625F);
		christmas_light_red.addChild(c_light_1_r1);
		setRotationAngle(c_light_1_r1, -1.9199F, 0, 0);
		c_light_1_r1.setTextureUVOffset(61, 385).addCuboid(-12, 2.8249F, 0.9175F, 0, 0, 0, 0.2F, false);

		christmas_light_yellow = createModelPart();
		christmas_light_yellow.setPivot(0, 24, 0);


		c_light_10_r1 = createModelPart();
		c_light_10_r1.setPivot(0, -31.8F, 5.2F);
		christmas_light_yellow.addChild(c_light_10_r1);
		setRotationAngle(c_light_10_r1, 1.7453F, 0, 0);
		c_light_10_r1.setTextureUVOffset(73, 385).addCuboid(-12, 3.55F, -0.2F, 0, 0, 0, 0.2F, false);

		c_light_6_r1 = createModelPart();
		c_light_6_r1.setPivot(0, -32, 0);
		christmas_light_yellow.addChild(c_light_6_r1);
		setRotationAngle(c_light_6_r1, -1.5708F, 0, 0);
		c_light_6_r1.setTextureUVOffset(73, 385).addCuboid(-12, 1.25F, 0.4F, 0, 0, 0, 0.2F, false);

		c_light_2_r1 = createModelPart();
		c_light_2_r1.setPivot(0, -33.5402F, -10.625F);
		christmas_light_yellow.addChild(c_light_2_r1);
		setRotationAngle(c_light_2_r1, -1.9199F, 0, 0);
		c_light_2_r1.setTextureUVOffset(73, 385).addCuboid(-12, 0.3249F, 0.9175F, 0, 0, 0, 0.2F, false);

		christmas_light_green = createModelPart();
		christmas_light_green.setPivot(0, 24, 0);


		c_light_11_r1 = createModelPart();
		c_light_11_r1.setPivot(0, -33.5402F, 10.625F);
		christmas_light_green.addChild(c_light_11_r1);
		setRotationAngle(c_light_11_r1, 1.9199F, 0, 0);
		c_light_11_r1.setTextureUVOffset(61, 391).addCuboid(-12, 0.3249F, -0.9175F, 0, 0, 0, 0.2F, false);

		c_light_7_r1 = createModelPart();
		c_light_7_r1.setPivot(0, -32, 0);
		christmas_light_green.addChild(c_light_7_r1);
		setRotationAngle(c_light_7_r1, -1.5708F, 0, 0);
		c_light_7_r1.setTextureUVOffset(61, 391).addCuboid(-12, -1.25F, 0.4F, 0, 0, 0, 0.2F, false);

		c_light_3_r1 = createModelPart();
		c_light_3_r1.setPivot(0, -31.8F, -5.2F);
		christmas_light_green.addChild(c_light_3_r1);
		setRotationAngle(c_light_3_r1, -1.7453F, 0, 0);
		c_light_3_r1.setTextureUVOffset(61, 391).addCuboid(-12, 3.55F, 0.2F, 0, 0, 0, 0.2F, false);

		christmas_light_blue = createModelPart();
		christmas_light_blue.setPivot(0, 24, 0);


		c_light_12_r1 = createModelPart();
		c_light_12_r1.setPivot(0, -33.5402F, 10.625F);
		christmas_light_blue.addChild(c_light_12_r1);
		setRotationAngle(c_light_12_r1, 1.9199F, 0, 0);
		c_light_12_r1.setTextureUVOffset(73, 391).addCuboid(-12, 2.8249F, -0.9175F, 0, 0, 0, 0.2F, false);

		c_light_8_r1 = createModelPart();
		c_light_8_r1.setPivot(0, -32, 0);
		christmas_light_blue.addChild(c_light_8_r1);
		setRotationAngle(c_light_8_r1, -1.5708F, 0, 0);
		c_light_8_r1.setTextureUVOffset(73, 391).addCuboid(-12, -3.75F, 0.4F, 0, 0, 0, 0.2F, false);

		c_light_4_r1 = createModelPart();
		c_light_4_r1.setPivot(0, -31.8F, -5.2F);
		christmas_light_blue.addChild(c_light_4_r1);
		setRotationAngle(c_light_4_r1, -1.7453F, 0, 0);
		c_light_4_r1.setTextureUVOffset(73, 391).addCuboid(-12, 1.05F, 0.2F, 0, 0, 0, 0.2F, false);

		christmas_light_tree_red = createModelPart();
		christmas_light_tree_red.setPivot(0, 24, 0);
		christmas_light_tree_red.setTextureUVOffset(70, 385).addCuboid(1, -21.2888F, -2.177F, 0, 0, 0, 0.2F, false);
		christmas_light_tree_red.setTextureUVOffset(70, 385).addCuboid(0, -17.2888F, -4.177F, 0, 0, 0, 0.2F, false);
		christmas_light_tree_red.setTextureUVOffset(70, 385).addCuboid(-1, -13.2888F, -6.177F, 0, 0, 0, 0.2F, false);
		christmas_light_tree_red.setTextureUVOffset(70, 385).addCuboid(-5, -8.0388F, -8.177F, 0, 0, 0, 0.2F, false);
		christmas_light_tree_red.setTextureUVOffset(70, 385).addCuboid(-3, -6.0388F, -8.177F, 0, 0, 0, 0.2F, false);
		christmas_light_tree_red.setTextureUVOffset(70, 385).addCuboid(6, -7.2888F, -8.177F, 0, 0, 0, 0.2F, false);
		christmas_light_tree_red.setTextureUVOffset(70, 385).addCuboid(-0.2F, -0.2F, 0.2F, 0, 0, 0, 0.2F, false);

		ct_light_13_r1 = createModelPart();
		ct_light_13_r1.setPivot(0, 0, 0);
		christmas_light_tree_red.addChild(ct_light_13_r1);
		setRotationAngle(ct_light_13_r1, 0, -1.5708F, 0);
		ct_light_13_r1.setTextureUVOffset(70, 385).addCuboid(6, -7.2888F, -8.177F, 0, 0, 0, 0.2F, false);
		ct_light_13_r1.setTextureUVOffset(70, 385).addCuboid(-3, -6.0388F, -8.177F, 0, 0, 0, 0.2F, false);
		ct_light_13_r1.setTextureUVOffset(70, 385).addCuboid(-5, -8.0388F, -8.177F, 0, 0, 0, 0.2F, false);
		ct_light_13_r1.setTextureUVOffset(70, 385).addCuboid(-1, -13.2888F, -6.177F, 0, 0, 0, 0.2F, false);
		ct_light_13_r1.setTextureUVOffset(70, 385).addCuboid(0, -17.2888F, -4.177F, 0, 0, 0, 0.2F, false);
		ct_light_13_r1.setTextureUVOffset(70, 385).addCuboid(1, -21.2888F, -2.177F, 0, 0, 0, 0.2F, false);
		ct_light_13_r1.setTextureUVOffset(70, 385).addCuboid(-0.2F, -0.2F, 0.2F, 0, 0, 0, 0.2F, false);

		christmas_light_tree_yellow = createModelPart();
		christmas_light_tree_yellow.setPivot(0, 24, 0);
		christmas_light_tree_yellow.setTextureUVOffset(82, 385).addCuboid(-1, -18.2888F, -2.177F, 0, 0, 0, 0.2F, false);
		christmas_light_tree_yellow.setTextureUVOffset(82, 385).addCuboid(2, -15.2888F, -4.177F, 0, 0, 0, 0.2F, false);
		christmas_light_tree_yellow.setTextureUVOffset(82, 385).addCuboid(1, -11.2888F, -6.177F, 0, 0, 0, 0.2F, false);
		christmas_light_tree_yellow.setTextureUVOffset(82, 385).addCuboid(-3, -12.2888F, -6.177F, 0, 0, 0, 0.2F, false);
		christmas_light_tree_yellow.setTextureUVOffset(82, 385).addCuboid(-2, -9.5388F, -8.177F, 0, 0, 0, 0.2F, false);
		christmas_light_tree_yellow.setTextureUVOffset(82, 385).addCuboid(-6, -7.0388F, -8.177F, 0, 0, 0, 0.2F, false);

		ct_light_20_r1 = createModelPart();
		ct_light_20_r1.setPivot(0, 0, 0);
		christmas_light_tree_yellow.addChild(ct_light_20_r1);
		setRotationAngle(ct_light_20_r1, 0, -1.5708F, 0);
		ct_light_20_r1.setTextureUVOffset(82, 385).addCuboid(-6, -7.0388F, -8.177F, 0, 0, 0, 0.2F, false);
		ct_light_20_r1.setTextureUVOffset(82, 385).addCuboid(-2, -9.5388F, -8.177F, 0, 0, 0, 0.2F, false);
		ct_light_20_r1.setTextureUVOffset(82, 385).addCuboid(-3, -12.2888F, -6.177F, 0, 0, 0, 0.2F, false);
		ct_light_20_r1.setTextureUVOffset(82, 385).addCuboid(1, -11.2888F, -6.177F, 0, 0, 0, 0.2F, false);
		ct_light_20_r1.setTextureUVOffset(82, 385).addCuboid(2, -15.2888F, -4.177F, 0, 0, 0, 0.2F, false);
		ct_light_20_r1.setTextureUVOffset(82, 385).addCuboid(-1, -18.2888F, -2.177F, 0, 0, 0, 0.2F, false);

		christmas_light_tree_green = createModelPart();
		christmas_light_tree_green.setPivot(0, 24, 0);
		christmas_light_tree_green.setTextureUVOffset(70, 391).addCuboid(1.75F, -19.2888F, -2.177F, 0, 0, 0, 0.2F, false);
		christmas_light_tree_green.setTextureUVOffset(70, 391).addCuboid(-5, -10.2888F, -6.177F, 0, 0, 0, 0.2F, false);
		christmas_light_tree_green.setTextureUVOffset(70, 391).addCuboid(0, -7.0388F, -8.177F, 0, 0, 0, 0.2F, false);
		christmas_light_tree_green.setTextureUVOffset(70, 391).addCuboid(7.5F, -9.0388F, -8.177F, 0, 0, 0, 0.2F, false);
		christmas_light_tree_green.setTextureUVOffset(70, 391).addCuboid(4, -14.2888F, -4.177F, 0, 0, 0, 0.2F, false);

		ct_light_36_r1 = createModelPart();
		ct_light_36_r1.setPivot(0, 0, 0);
		christmas_light_tree_green.addChild(ct_light_36_r1);
		setRotationAngle(ct_light_36_r1, 0, -1.5708F, 0);
		ct_light_36_r1.setTextureUVOffset(70, 391).addCuboid(4, -14.2888F, -4.177F, 0, 0, 0, 0.2F, false);
		ct_light_36_r1.setTextureUVOffset(70, 391).addCuboid(7.5F, -9.0388F, -8.177F, 0, 0, 0, 0.2F, false);
		ct_light_36_r1.setTextureUVOffset(70, 391).addCuboid(0, -7.0388F, -8.177F, 0, 0, 0, 0.2F, false);
		ct_light_36_r1.setTextureUVOffset(70, 391).addCuboid(-5, -10.2888F, -6.177F, 0, 0, 0, 0.2F, false);
		ct_light_36_r1.setTextureUVOffset(70, 391).addCuboid(1.75F, -19.2888F, -2.177F, 0, 0, 0, 0.2F, false);

		christmas_light_tree_blue = createModelPart();
		christmas_light_tree_blue.setPivot(0, 24, 0);
		christmas_light_tree_blue.setTextureUVOffset(82, 391).addCuboid(4, -12.2888F, -6.177F, 0, 0, 0, 0.2F, false);
		christmas_light_tree_blue.setTextureUVOffset(82, 391).addCuboid(3, -8.5388F, -8.177F, 0, 0, 0, 0.2F, false);
		christmas_light_tree_blue.setTextureUVOffset(82, 391).addCuboid(-7, -9.0388F, -8.177F, 0, 0, 0, 0.2F, false);
		christmas_light_tree_blue.setTextureUVOffset(82, 391).addCuboid(-2, -11.0388F, -6.177F, 0, 0, 0, 0.2F, false);
		christmas_light_tree_blue.setTextureUVOffset(82, 391).addCuboid(-3, -15.2888F, -4.177F, 0, 0, 0, 0.2F, false);
		christmas_light_tree_blue.setTextureUVOffset(82, 391).addCuboid(-1, -21.7888F, -2.177F, 0, 0, 0, 0.2F, false);

		ct_light_48_r1 = createModelPart();
		ct_light_48_r1.setPivot(0, 0, 0);
		christmas_light_tree_blue.addChild(ct_light_48_r1);
		setRotationAngle(ct_light_48_r1, 0, -1.5708F, 0);
		ct_light_48_r1.setTextureUVOffset(82, 391).addCuboid(4, -12.2888F, -6.177F, 0, 0, 0, 0.2F, false);
		ct_light_48_r1.setTextureUVOffset(82, 391).addCuboid(-1, -21.7888F, -2.177F, 0, 0, 0, 0.2F, false);
		ct_light_48_r1.setTextureUVOffset(82, 391).addCuboid(-3, -15.2888F, -4.177F, 0, 0, 0, 0.2F, false);
		ct_light_48_r1.setTextureUVOffset(82, 391).addCuboid(-2, -11.0388F, -6.177F, 0, 0, 0, 0.2F, false);
		ct_light_48_r1.setTextureUVOffset(82, 391).addCuboid(-7, -9.0388F, -8.177F, 0, 0, 0, 0.2F, false);
		ct_light_48_r1.setTextureUVOffset(82, 391).addCuboid(3, -8.5388F, -8.177F, 0, 0, 0, 0.2F, false);

		buildModel();
	}

	private static final int DOOR_MAX = 14;
	private static final ModelDoorOverlay MODEL_DOOR_OVERLAY = new ModelDoorOverlay(DOOR_MAX, 6.34F, 13, "door_overlay_mlr_left.png", "door_overlay_mlr_right.png");
	private static final ModelDoorOverlayTopMLR MODEL_DOOR_OVERLAY_TOP = new ModelDoorOverlayTopMLR("mtr:textures/block/sign/door_overlay_mlr_top.png");
	private static final boolean[][] CHRISTMAS_LIGHT_STAGES = {
			{true, false, false, false},
			{false, true, false, false},
			{false, false, true, false},
			{false, false, false, true},
			{true, false, false, false},
			{false, true, false, false},
			{false, false, true, false},
			{false, false, false, true},

			{true, true, false, false},
			{false, true, true, false},
			{false, false, true, true},
			{true, false, false, true},
			{true, true, false, false},
			{false, true, true, false},
			{false, false, true, true},
			{true, false, false, true},

			{true, false, true, false},
			{false, true, false, true},
			{true, false, true, false},
			{false, true, false, true},
			{true, false, true, false},
			{false, true, false, true},
			{true, false, true, false},
			{false, true, false, true},

			{true, false, false, false},
			{true, true, false, false},
			{true, true, true, false},
			{true, true, true, true},
			{false, true, false, false},
			{false, true, true, false},
			{false, true, true, true},
			{true, true, true, true},
			{false, false, true, false},
			{false, false, true, true},
			{true, false, true, true},
			{true, true, true, true},
			{false, false, false, true},
			{true, false, false, true},
			{true, true, false, true},
			{true, true, true, true},

			{false, false, false, false},
			{true, true, true, true},
			{true, true, true, true},
			{true, true, true, true},
			{false, false, false, false},
			{true, true, true, true},
			{true, true, true, true},
			{true, true, true, true},
	};

	@Override
	public ModelMLR createNew(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		return new ModelMLR(isChristmas, doorAnimationType, renderDoorOverlay);
	}

	@Override
	protected void renderWindowPositions(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
		final boolean isEvenWindow = isEvenWindow(position);
		switch (renderStage) {
			case LIGHTS:
				renderMirror(roof_light, graphicsHolder, light, position);
				break;
			case INTERIOR:
				if (isEvenWindow) {
					renderOnceFlipped(window_1_tv, graphicsHolder, light, position);
					renderOnceFlipped(window_1, graphicsHolder, light, position);
					renderOnce(window_2, graphicsHolder, light, position);
				} else {
					renderOnce(window_1_tv, graphicsHolder, light, position);
					renderOnce(window_1, graphicsHolder, light, position);
					renderOnceFlipped(window_2, graphicsHolder, light, position);
				}
				if (renderDetails) {
					renderMirror(roof_window, graphicsHolder, light, position);
					renderMirror(window_handrails, graphicsHolder, light, position);
					renderMirror(seats, graphicsHolder, light, position + (isEvenWindow ? -0.75F : 0.75F));
					renderMirror(side_panel, graphicsHolder, light, position - (isEvenWindow ? 15.75F : 14.25F));
					renderMirror(side_panel, graphicsHolder, light, position + (isEvenWindow ? 14.25F : 15.75F));
					if (isChristmas) {
						renderMirror(christmas_light_holder, graphicsHolder, light, position);
					}
				}
				break;
			case INTERIOR_TRANSLUCENT:
				renderMirror(side_panel_translucent, graphicsHolder, light, position - (isEvenWindow ? 15.75F : 14.25F));
				renderMirror(side_panel_translucent, graphicsHolder, light, position + (isEvenWindow ? 14.25F : 15.75F));
				break;
			case EXTERIOR:
				if (isEvenWindow) {
					renderOnceFlipped(window_exterior_1, graphicsHolder, light, position);
					renderOnce(window_exterior_2, graphicsHolder, light, position);
				} else {
					renderOnce(window_exterior_1, graphicsHolder, light, position);
					renderOnceFlipped(window_exterior_2, graphicsHolder, light, position);
				}
				renderMirror(roof_exterior, graphicsHolder, light, position);
				break;
		}

		if (renderDetails && isChristmas) {
			renderChristmasLights(graphicsHolder, renderStage, christmas_light_red, christmas_light_yellow, christmas_light_green, christmas_light_blue, light, position);
		}
	}

	@Override
	protected void renderDoorPositions(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
		final boolean middleDoor = isIndex(getDoorPositions().length / 2, position, getDoorPositions());
		final boolean doorOpen = doorLeftZ > 0 || doorRightZ > 0;

		switch (renderStage) {
			case LIGHTS:
				renderMirror(roof_light, graphicsHolder, light, position);
				if (middleDoor && doorOpen) {
					renderMirror(door_light_on, graphicsHolder, light, position - 22);
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
					renderOnce(door_handrails, graphicsHolder, light, position);
					renderMirror(roof_door, graphicsHolder, light, position);
					if (isChristmas) {
						renderMirror(christmas_light_holder, graphicsHolder, light, position);
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
					renderMirror(door_light, graphicsHolder, light, position - 22);
					if (!doorOpen) {
						renderMirror(door_light_off, graphicsHolder, light, position - 22);
					}
				}
				break;
		}

		if (renderDetails && isChristmas) {
			renderChristmasLights(graphicsHolder, renderStage, christmas_light_red, christmas_light_yellow, christmas_light_green, christmas_light_blue, light, position);
		}
	}

	@Override
	protected void renderHeadPosition1(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean useHeadlights) {
		switch (renderStage) {
			case LIGHTS:
				renderOnce(roof_end_light, graphicsHolder, light, position);
				break;
			case ALWAYS_ON_LIGHTS:
				renderOnceFlipped(useHeadlights ? headlights : tail_lights, graphicsHolder, light, position);
				if (renderDetails && isChristmas) {
					renderOnceFlipped(christmas_light_head, graphicsHolder, light, position);
				}
				break;
			case INTERIOR:
				renderOnceFlipped(head, graphicsHolder, light, position);
				if (renderDetails) {
					renderOnce(roof_end, graphicsHolder, light, position);
					if (isChristmas) {
						renderMirror(christmas_light_holder, graphicsHolder, light, position);
						renderOnce(christmas_tree, graphicsHolder, light, position);
						renderMirror(christmas_antler, graphicsHolder, light, position - 30);
					}
				}
				break;
			case INTERIOR_TRANSLUCENT:
				renderMirror(side_panel, graphicsHolder, light, position + 14.25F);
				break;
			case EXTERIOR:
				renderOnceFlipped(head_exterior, graphicsHolder, light, position);
				renderMirror(roof_exterior, graphicsHolder, light, position);
				renderOnceFlipped(roof_end_vents, graphicsHolder, light, position + 2);
				break;
		}

		if (renderDetails && isChristmas) {
			renderChristmasLights(graphicsHolder, renderStage, christmas_light_red, christmas_light_yellow, christmas_light_green, christmas_light_blue, light, position);
			renderChristmasLights(graphicsHolder, renderStage, christmas_light_tree_red, christmas_light_tree_yellow, christmas_light_tree_green, christmas_light_tree_blue, light, position);
		}
	}

	@Override
	protected void renderHeadPosition2(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean useHeadlights) {
		switch (renderStage) {
			case LIGHTS:
				renderOnceFlipped(roof_end_light, graphicsHolder, light, position);
				break;
			case ALWAYS_ON_LIGHTS:
				renderOnce(useHeadlights ? headlights : tail_lights, graphicsHolder, light, position);
				if (renderDetails && isChristmas) {
					renderOnce(christmas_light_head, graphicsHolder, light, position);
				}
				break;
			case INTERIOR:
				renderOnce(head, graphicsHolder, light, position);
				if (renderDetails) {
					renderOnceFlipped(roof_end, graphicsHolder, light, position);
					if (isChristmas) {
						renderMirror(christmas_light_holder, graphicsHolder, light, position);
						renderOnce(christmas_tree, graphicsHolder, light, position);
						renderMirror(christmas_antler, graphicsHolder, light, position + 30);
					}
				}
				break;
			case INTERIOR_TRANSLUCENT:
				renderMirror(side_panel, graphicsHolder, light, position - 14.25F);
				break;
			case EXTERIOR:
				renderOnce(head_exterior, graphicsHolder, light, position);
				renderMirror(roof_exterior, graphicsHolder, light, position);
				renderOnce(roof_end_vents, graphicsHolder, light, position - 2);
				break;
		}

		if (renderDetails && isChristmas) {
			renderChristmasLights(graphicsHolder, renderStage, christmas_light_red, christmas_light_yellow, christmas_light_green, christmas_light_blue, light, position);
			renderChristmasLights(graphicsHolder, renderStage, christmas_light_tree_red, christmas_light_tree_yellow, christmas_light_tree_green, christmas_light_tree_blue, light, position);
		}
	}

	@Override
	protected void renderEndPosition1(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
		switch (renderStage) {
			case LIGHTS:
				renderOnce(roof_end_light, graphicsHolder, light, position);
				break;
			case INTERIOR:
				renderOnce(end, graphicsHolder, light, position);
				if (renderDetails) {
					renderOnce(roof_end, graphicsHolder, light, position);
					if (isChristmas) {
						renderMirror(christmas_light_holder, graphicsHolder, light, position);
					}
				}
				break;
			case INTERIOR_TRANSLUCENT:
				renderMirror(side_panel, graphicsHolder, light, position + 14.25F);
				break;
			case EXTERIOR:
				renderOnce(end_exterior, graphicsHolder, light, position);
				renderMirror(roof_exterior, graphicsHolder, light, position);
				renderOnce(roof_end_exterior, graphicsHolder, light, position);
				renderOnceFlipped(roof_end_vents, graphicsHolder, light, position);
				break;
		}

		if (renderDetails && isChristmas) {
			renderChristmasLights(graphicsHolder, renderStage, christmas_light_red, christmas_light_yellow, christmas_light_green, christmas_light_blue, light, position);
		}
	}

	@Override
	protected void renderEndPosition2(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
		switch (renderStage) {
			case LIGHTS:
				renderOnceFlipped(roof_end_light, graphicsHolder, light, position);
				break;
			case INTERIOR:
				renderOnceFlipped(end, graphicsHolder, light, position);
				if (renderDetails) {
					renderOnceFlipped(roof_end, graphicsHolder, light, position);
					if (isChristmas) {
						renderMirror(christmas_light_holder, graphicsHolder, light, position);
					}
				}
				break;
			case INTERIOR_TRANSLUCENT:
				renderMirror(side_panel, graphicsHolder, light, position - 14.25F);
				break;
			case EXTERIOR:
				renderOnceFlipped(end_exterior, graphicsHolder, light, position);
				renderMirror(roof_exterior, graphicsHolder, light, position);
				renderOnceFlipped(roof_end_exterior, graphicsHolder, light, position);
				renderOnce(roof_end_vents, graphicsHolder, light, position);
				break;
		}

		if (renderDetails && isChristmas) {
			renderChristmasLights(graphicsHolder, renderStage, christmas_light_red, christmas_light_yellow, christmas_light_green, christmas_light_blue, light, position);
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
		return new int[]{-96, -32, 32, 96};
	}

	@Override
	protected int[] getDoorPositions() {
		return new int[]{-128, -64, 0, 64, 128};
	}

	@Override
	protected int[] getEndPositions() {
		return new int[]{-160, 160};
	}

	@Override
	protected int getDoorMax() {
		return DOOR_MAX;
	}

	private boolean isEvenWindow(int position) {
		return isIndex(1, position, getWindowPositions()) || isIndex(3, position, getWindowPositions());
	}

	@Override
	protected void renderTextDisplays(GraphicsHolder graphicsHolder, int thisRouteColor, String thisRouteName, String thisRouteNumber, String thisStationName, String thisRouteDestination, String nextStationName, Consumer<BiConsumer<String, InterchangeColorsForStationName>> getInterchanges, int car, int totalCars, boolean atPlatform, boolean isTerminating, ObjectArrayList<ScrollingText> scrollingTexts) {
		renderFrontDestination(
				graphicsHolder,
				0, 0, getEndPositions()[0] / 16F - 2.92F, 0, -1.81F, -0.01F,
				-10, 0, 0.5F, 0.26F,
				0xFFFF0000, 0xFFFF9900, 2, getDestinationString(thisRouteDestination, TextSpacingType.MLR_SPACING, true), false, car, totalCars
		);
	}

	@Override
	protected String defaultDestinationString() {
		return "East Rail";
	}

	private static void renderChristmasLights(GraphicsHolder graphicsHolder, RenderStage renderStage, ModelPartExtension lightRed, ModelPartExtension lightYellow, ModelPartExtension lightGreen, ModelPartExtension lightBlue, int light, int position) {
		if (renderStage == RenderStage.INTERIOR || renderStage == RenderStage.ALWAYS_ON_LIGHTS) {
			final boolean[] lights = CHRISTMAS_LIGHT_STAGES[(int) ((System.currentTimeMillis() / 500) % CHRISTMAS_LIGHT_STAGES.length)];
			if (renderStage == RenderStage.ALWAYS_ON_LIGHTS == lights[0]) {
				renderMirror(lightRed, graphicsHolder, light / 2, position);
			}
			if (renderStage == RenderStage.ALWAYS_ON_LIGHTS == lights[1]) {
				renderMirror(lightYellow, graphicsHolder, light / 2, position);
			}
			if (renderStage == RenderStage.ALWAYS_ON_LIGHTS == lights[2]) {
				renderMirror(lightGreen, graphicsHolder, light / 2, position);
			}
			if (renderStage == RenderStage.ALWAYS_ON_LIGHTS == lights[3]) {
				renderMirror(lightBlue, graphicsHolder, light / 2, position);
			}
		}
	}
}