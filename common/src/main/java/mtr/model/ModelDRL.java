package mtr.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mtr.client.DoorAnimationType;
import mtr.mappings.ModelDataWrapper;
import mtr.mappings.ModelMapper;

public class ModelDRL extends ModelSimpleTrainBase<ModelDRL> {
	private final ModelMapper window;
	private final ModelMapper upper_wall_r1;
	private final ModelMapper window_handrails_1;
	private final ModelMapper handrail_3_r1;
	private final ModelMapper handrail_2_r1;
	private final ModelMapper handrail_1_r1;
	private final ModelMapper window_handrails_2;
	private final ModelMapper handrail_4_r1;
	private final ModelMapper handrail_3_r2;
	private final ModelMapper handrail_2_r2;
	private final ModelMapper window_exterior;
	private final ModelMapper upper_wall_5_r1;
	private final ModelMapper upper_wall_3_r1;
	private final ModelMapper floor_4_r1;
	private final ModelMapper floor_3_r1;
	private final ModelMapper roof_window_1;
	private final ModelMapper inner_roof_4_r1;
	private final ModelMapper inner_roof_2_r1;
	private final ModelMapper roof_window_2;
	private final ModelMapper inner_roof_5_r1;
	private final ModelMapper inner_roof_3_r1;
	private final ModelMapper roof_window_3;
	private final ModelMapper inner_roof_5_r2;
	private final ModelMapper inner_roof_3_r2;
	private final ModelMapper roof_window_light;
	private final ModelMapper window_light_r1;
	private final ModelMapper roof_door;
	private final ModelMapper inner_roof_4_r2;
	private final ModelMapper inner_roof_2_r2;
	private final ModelMapper roof_exterior;
	private final ModelMapper outer_roof_5_r1;
	private final ModelMapper outer_roof_4_r1;
	private final ModelMapper outer_roof_3_r1;
	private final ModelMapper outer_roof_2_r1;
	private final ModelMapper outer_roof_1_r1;
	private final ModelMapper door;
	private final ModelMapper door_right;
	private final ModelMapper door_right_top_r1;
	private final ModelMapper door_left;
	private final ModelMapper door_left_top_r1;
	private final ModelMapper door_handrail_1;
	private final ModelMapper handrail_11_r1;
	private final ModelMapper door_handrail_2;
	private final ModelMapper door_exterior;
	private final ModelMapper door_left_exterior;
	private final ModelMapper door_left_top_r2;
	private final ModelMapper door_right_exterior;
	private final ModelMapper door_right_top_r2;
	private final ModelMapper end;
	private final ModelMapper upper_wall_2_r1;
	private final ModelMapper upper_wall_1_r1;
	private final ModelMapper end_exterior;
	private final ModelMapper upper_wall_2_r2;
	private final ModelMapper upper_wall_1_r2;
	private final ModelMapper roof_end;
	private final ModelMapper handrail_2_r3;
	private final ModelMapper inner_roof_7_r1;
	private final ModelMapper inner_roof_1;
	private final ModelMapper inner_roof_6_r1;
	private final ModelMapper inner_roof_4_r3;
	private final ModelMapper inner_roof_2_r3;
	private final ModelMapper inner_roof_2;
	private final ModelMapper inner_roof_6_r2;
	private final ModelMapper inner_roof_4_r4;
	private final ModelMapper inner_roof_2_r4;
	private final ModelMapper roof_end_exterior;
	private final ModelMapper vent_2_r1;
	private final ModelMapper vent_1_r1;
	private final ModelMapper outer_roof_1;
	private final ModelMapper outer_roof_5_r2;
	private final ModelMapper outer_roof_4_r2;
	private final ModelMapper outer_roof_3_r2;
	private final ModelMapper outer_roof_2_r2;
	private final ModelMapper outer_roof_1_r2;
	private final ModelMapper outer_roof_2;
	private final ModelMapper outer_roof_5_r3;
	private final ModelMapper outer_roof_4_r3;
	private final ModelMapper outer_roof_3_r3;
	private final ModelMapper outer_roof_2_r3;
	private final ModelMapper outer_roof_1_r3;
	private final ModelMapper roof_light;
	private final ModelMapper roof_light_r1;
	private final ModelMapper roof_end_light;
	private final ModelMapper light_5_r1;
	private final ModelMapper light_4_r1;
	private final ModelMapper light_3_r1;
	private final ModelMapper light_2_r1;
	private final ModelMapper light_1_r1;
	private final ModelMapper head;
	private final ModelMapper upper_wall_2_r3;
	private final ModelMapper upper_wall_1_r3;
	private final ModelMapper head_exterior;
	private final ModelMapper driver_door_upper_2_r1;
	private final ModelMapper driver_door_upper_1_r1;
	private final ModelMapper front;
	private final ModelMapper bottom_r1;
	private final ModelMapper front_middle_top_r1;
	private final ModelMapper front_panel_2_r1;
	private final ModelMapper front_panel_1_r1;
	private final ModelMapper side_1;
	private final ModelMapper front_side_bottom_1_r1;
	private final ModelMapper outer_roof_4_r4;
	private final ModelMapper outer_roof_1_r4;
	private final ModelMapper outer_roof_2_r4;
	private final ModelMapper outer_roof_3_r4;
	private final ModelMapper front_side_lower_1_r1;
	private final ModelMapper front_side_upper_1_r1;
	private final ModelMapper side_2;
	private final ModelMapper front_side_bottom_2_r1;
	private final ModelMapper outer_roof_8_r1;
	private final ModelMapper outer_roof_7_r1;
	private final ModelMapper outer_roof_6_r1;
	private final ModelMapper outer_roof_5_r4;
	private final ModelMapper front_side_upper_2_r1;
	private final ModelMapper front_side_lower_2_r1;
	private final ModelMapper nose;
	private final ModelMapper center_nose;
	private final ModelMapper nose_5_r1;
	private final ModelMapper nose_4_r1;
	private final ModelMapper nose_2_r1;
	private final ModelMapper nose_side_1;
	private final ModelMapper nose_18_r1;
	private final ModelMapper nose_17_r1;
	private final ModelMapper nose_16_r1;
	private final ModelMapper nose_15_r1;
	private final ModelMapper nose_14_r1;
	private final ModelMapper nose_13_r1;
	private final ModelMapper nose_12_r1;
	private final ModelMapper nose_11_r1;
	private final ModelMapper nose_10_r1;
	private final ModelMapper nose_9_r1;
	private final ModelMapper nose_8_r1;
	private final ModelMapper nose_7_r1;
	private final ModelMapper nose_6_r1;
	private final ModelMapper nose_5_r2;
	private final ModelMapper nose_4_r2;
	private final ModelMapper nose_3_r1;
	private final ModelMapper nose_2_r2;
	private final ModelMapper nose_side_2;
	private final ModelMapper nose_19_r1;
	private final ModelMapper nose_18_r2;
	private final ModelMapper nose_17_r2;
	private final ModelMapper nose_16_r2;
	private final ModelMapper nose_15_r2;
	private final ModelMapper nose_14_r2;
	private final ModelMapper nose_13_r2;
	private final ModelMapper nose_12_r2;
	private final ModelMapper nose_11_r2;
	private final ModelMapper nose_10_r2;
	private final ModelMapper nose_9_r2;
	private final ModelMapper nose_8_r2;
	private final ModelMapper nose_7_r2;
	private final ModelMapper nose_6_r2;
	private final ModelMapper nose_5_r3;
	private final ModelMapper nose_4_r3;
	private final ModelMapper nose_3_r2;
	private final ModelMapper roof_head_exterior;
	private final ModelMapper vent_3_r1;
	private final ModelMapper vent_2_r2;
	private final ModelMapper outer_roof_3;
	private final ModelMapper outer_roof_6_r2;
	private final ModelMapper outer_roof_5_r5;
	private final ModelMapper outer_roof_4_r5;
	private final ModelMapper outer_roof_3_r5;
	private final ModelMapper outer_roof_2_r5;
	private final ModelMapper outer_roof_4;
	private final ModelMapper outer_roof_6_r3;
	private final ModelMapper outer_roof_5_r6;
	private final ModelMapper outer_roof_4_r6;
	private final ModelMapper outer_roof_3_r6;
	private final ModelMapper outer_roof_2_r6;
	private final ModelMapper headlights;
	private final ModelMapper headlight_2_r1;
	private final ModelMapper tail_lights;
	private final ModelMapper headlight_4_r1;
	private final ModelMapper headlight_3_r1;
	private final ModelMapper headlight_2_r2;
	private final ModelMapper headlight_1_r1;
	private final ModelMapper door_light;
	private final ModelMapper outer_roof_1_r5;
	private final ModelMapper door_light_on;
	private final ModelMapper light_r1;
	private final ModelMapper door_light_off;
	private final ModelMapper light_r2;
	private final ModelMapper side_panel_1;
	private final ModelMapper handrail_r1;
	private final ModelMapper side_panel_2;
	private final ModelMapper side_panel_translucent;
	private final ModelMapper seat_1;
	private final ModelMapper seat_back_2_r1;
	private final ModelMapper seat_2;
	private final ModelMapper seat_back_3_r1;
	private final ModelMapper seat_curve;
	private final ModelMapper seat_panel_2_r1;
	private final ModelMapper statue_box;
	private final ModelMapper statue_box_3_r1;
	private final ModelMapper statue_box_1_r1;
	private final ModelMapper seat_side_1;
	private final ModelMapper seat_back_6_r1;
	private final ModelMapper seat_top_5_r1;
	private final ModelMapper seat_top_7_r1;
	private final ModelMapper seat_back_3_r2;
	private final ModelMapper seat_bottom_4_r1;
	private final ModelMapper seat_bottom_5_r1;
	private final ModelMapper seat_side_2;
	private final ModelMapper seat_top_8_r1;
	private final ModelMapper seat_top_6_r1;
	private final ModelMapper seat_back_5_r1;
	private final ModelMapper seat_bottom_6_r1;
	private final ModelMapper seat_back_4_r1;
	private final ModelMapper seat_bottom_3_r1;
	private final ModelMapper window_edge;
	private final ModelMapper edge_side_1;
	private final ModelMapper window_edge_2_r1;
	private final ModelMapper window_edge_1_r1;
	private final ModelMapper edge_side_2;
	private final ModelMapper window_edge_3_r1;
	private final ModelMapper window_edge_2_r2;
	private final ModelMapper statue_box_translucent;
	private final ModelMapper statue_box_translucent_3_r1;
	private final ModelMapper statue_box_translucent_1_r1;

	public ModelDRL() {
		this(DoorAnimationType.STANDARD_SLOW, true);
	}

	private ModelDRL(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		super(doorAnimationType, renderDoorOverlay);
		final int textureWidth = 360;
		final int textureHeight = 360;

		final ModelDataWrapper modelDataWrapper = new ModelDataWrapper(this, textureWidth, textureHeight);

		window = new ModelMapper(modelDataWrapper);
		window.setPos(0, 24, 0);
		window.texOffs(0, 83).addBox(-20, 0, -20, 20, 1, 40, 0, false);
		window.texOffs(202, 206).addBox(-20, -14, -20, 2, 14, 40, 0, false);

		upper_wall_r1 = new ModelMapper(modelDataWrapper);
		upper_wall_r1.setPos(-20, -14, 0);
		window.addChild(upper_wall_r1);
		setRotationAngle(upper_wall_r1, 0, 0, 0.1107F);
		upper_wall_r1.texOffs(134, 12).addBox(0, -19, -20, 2, 19, 40, 0, false);

		window_handrails_1 = new ModelMapper(modelDataWrapper);
		window_handrails_1.setPos(0, 24, 0);
		window_handrails_1.texOffs(359, 3).addBox(0, -33, -40, 0, 33, 0, 0.2F, false);
		window_handrails_1.texOffs(359, 3).addBox(0, -33, 20, 0, 33, 0, 0.2F, false);
		window_handrails_1.texOffs(359, 3).addBox(0, -33, 40, 0, 33, 0, 0.2F, false);
		window_handrails_1.texOffs(17, 44).addBox(-1.5F, -32, 34, 3, 4, 0, 0, false);
		window_handrails_1.texOffs(17, 44).addBox(-1.5F, -32, 26, 3, 4, 0, 0, false);
		window_handrails_1.texOffs(17, 44).addBox(-1.5F, -32, 14, 3, 4, 0, 0, false);
		window_handrails_1.texOffs(17, 44).addBox(-1.5F, -32, 8, 3, 4, 0, 0, false);
		window_handrails_1.texOffs(17, 44).addBox(-1.5F, -32, 2, 3, 4, 0, 0, false);
		window_handrails_1.texOffs(17, 44).addBox(-1.5F, -32, 46, 3, 4, 0, 0, false);
		window_handrails_1.texOffs(17, 44).addBox(-1.5F, -32, 52, 3, 4, 0, 0, false);
		window_handrails_1.texOffs(17, 44).addBox(-1.5F, -32, -46, 3, 4, 0, 0, false);
		window_handrails_1.texOffs(17, 44).addBox(-1.5F, -32, -52, 3, 4, 0, 0, false);
		window_handrails_1.texOffs(17, 44).addBox(-1.5F, -32, -58, 3, 4, 0, 0, false);
		window_handrails_1.texOffs(17, 44).addBox(-1.5F, -32, -34, 3, 4, 0, 0, false);
		window_handrails_1.texOffs(17, 44).addBox(-1.5F, -32, -28, 3, 4, 0, 0, false);
		window_handrails_1.texOffs(17, 44).addBox(-1.5F, -32, -22, 3, 4, 0, 0, false);

		handrail_3_r1 = new ModelMapper(modelDataWrapper);
		handrail_3_r1.setPos(0, 0, 0);
		window_handrails_1.addChild(handrail_3_r1);
		setRotationAngle(handrail_3_r1, -1.5708F, 0, 0);
		handrail_3_r1.texOffs(352, 0).addBox(0, -16, -31.5F, 0, 32, 0, 0.2F, false);

		handrail_2_r1 = new ModelMapper(modelDataWrapper);
		handrail_2_r1.setPos(0, 0, 40);
		window_handrails_1.addChild(handrail_2_r1);
		setRotationAngle(handrail_2_r1, -1.5708F, 0, 0);
		handrail_2_r1.texOffs(352, 0).addBox(0, -24, -31.5F, 0, 48, 0, 0.2F, false);

		handrail_1_r1 = new ModelMapper(modelDataWrapper);
		handrail_1_r1.setPos(0, 0, -40);
		window_handrails_1.addChild(handrail_1_r1);
		setRotationAngle(handrail_1_r1, -1.5708F, 0, 0);
		handrail_1_r1.texOffs(352, 0).addBox(0, -24, -31.5F, 0, 48, 0, 0.2F, false);

		window_handrails_2 = new ModelMapper(modelDataWrapper);
		window_handrails_2.setPos(0, 24, 0);
		window_handrails_2.texOffs(359, 3).addBox(0, -33, -40, 0, 33, 0, 0.2F, false);
		window_handrails_2.texOffs(359, 3).addBox(0, -33, -20, 0, 33, 0, 0.2F, false);
		window_handrails_2.texOffs(359, 3).addBox(0, -33, 40, 0, 33, 0, 0.2F, false);
		window_handrails_2.texOffs(17, 44).addBox(-1.5F, -32, 34, 3, 4, 0, 0, false);
		window_handrails_2.texOffs(17, 44).addBox(-1.5F, -32, 28, 3, 4, 0, 0, false);
		window_handrails_2.texOffs(17, 44).addBox(-1.5F, -32, 22, 3, 4, 0, 0, false);
		window_handrails_2.texOffs(17, 44).addBox(-1.5F, -32, -2, 3, 4, 0, 0, false);
		window_handrails_2.texOffs(17, 44).addBox(-1.5F, -32, -8, 3, 4, 0, 0, false);
		window_handrails_2.texOffs(17, 44).addBox(-1.5F, -32, 46, 3, 4, 0, 0, false);
		window_handrails_2.texOffs(17, 44).addBox(-1.5F, -32, 52, 3, 4, 0, 0, false);
		window_handrails_2.texOffs(17, 44).addBox(-1.5F, -32, -46, 3, 4, 0, 0, false);
		window_handrails_2.texOffs(17, 44).addBox(-1.5F, -32, -52, 3, 4, 0, 0, false);
		window_handrails_2.texOffs(17, 44).addBox(-1.5F, -32, 58, 3, 4, 0, 0, false);
		window_handrails_2.texOffs(17, 44).addBox(-1.5F, -32, -34, 3, 4, 0, 0, false);
		window_handrails_2.texOffs(17, 44).addBox(-1.5F, -32, -26, 3, 4, 0, 0, false);
		window_handrails_2.texOffs(17, 44).addBox(-1.5F, -32, -14, 3, 4, 0, 0, false);

		handrail_4_r1 = new ModelMapper(modelDataWrapper);
		handrail_4_r1.setPos(0, 0, 0);
		window_handrails_2.addChild(handrail_4_r1);
		setRotationAngle(handrail_4_r1, -1.5708F, 0, 0);
		handrail_4_r1.texOffs(352, 0).addBox(0, -16, -31.5F, 0, 32, 0, 0.2F, false);

		handrail_3_r2 = new ModelMapper(modelDataWrapper);
		handrail_3_r2.setPos(0, 0, 40);
		window_handrails_2.addChild(handrail_3_r2);
		setRotationAngle(handrail_3_r2, -1.5708F, 0, 0);
		handrail_3_r2.texOffs(352, 0).addBox(0, -24, -31.5F, 0, 48, 0, 0.2F, false);

		handrail_2_r2 = new ModelMapper(modelDataWrapper);
		handrail_2_r2.setPos(0, 0, -40);
		window_handrails_2.addChild(handrail_2_r2);
		setRotationAngle(handrail_2_r2, -1.5708F, 0, 0);
		handrail_2_r2.texOffs(352, 0).addBox(0, -24, -31.5F, 0, 48, 0, 0.2F, false);

		window_exterior = new ModelMapper(modelDataWrapper);
		window_exterior.setPos(0, 24, 0);
		window_exterior.texOffs(0, 0).addBox(-20, -14, -66, 1, 14, 132, 0, false);

		upper_wall_5_r1 = new ModelMapper(modelDataWrapper);
		upper_wall_5_r1.setPos(40.6269F, -7.2639F, 65);
		window_exterior.addChild(upper_wall_5_r1);
		setRotationAngle(upper_wall_5_r1, 0, -1.5708F, 0.1107F);
		upper_wall_5_r1.texOffs(131, 146).addBox(-5, -19, 60, 6, 19, 1, 0, false);
		upper_wall_5_r1.texOffs(131, 146).addBox(-131, -19, 60, 6, 19, 1, 0, true);

		upper_wall_3_r1 = new ModelMapper(modelDataWrapper);
		upper_wall_3_r1.setPos(-20, -14, 0);
		window_exterior.addChild(upper_wall_3_r1);
		setRotationAngle(upper_wall_3_r1, 0, 0, 0.1107F);
		upper_wall_3_r1.texOffs(120, 198).addBox(0, -19, -60, 1, 19, 40, 0, false);
		upper_wall_3_r1.texOffs(120, 198).addBox(0, -19, -20, 1, 19, 40, 0, false);
		upper_wall_3_r1.texOffs(120, 198).addBox(0, -19, 20, 1, 19, 40, 0, false);

		floor_4_r1 = new ModelMapper(modelDataWrapper);
		floor_4_r1.setPos(30, 0, 84);
		window_exterior.addChild(floor_4_r1);
		setRotationAngle(floor_4_r1, 0, -1.5708F, 0);
		floor_4_r1.texOffs(0, 67).addBox(-148, 0, 50, 14, 4, 1, 0, true);
		floor_4_r1.texOffs(0, 67).addBox(-34, 0, 50, 14, 4, 1, 0, false);

		floor_3_r1 = new ModelMapper(modelDataWrapper);
		floor_3_r1.setPos(-20, 0, 70);
		window_exterior.addChild(floor_3_r1);
		setRotationAngle(floor_3_r1, 0, -1.5708F, 0);
		floor_3_r1.texOffs(0, 126).addBox(-120, 0, 0, 50, 4, 1, 0, true);
		floor_3_r1.texOffs(0, 126).addBox(-70, 0, 0, 50, 4, 1, 0, false);

		roof_window_1 = new ModelMapper(modelDataWrapper);
		roof_window_1.setPos(0, 24, 0);
		roof_window_1.texOffs(54, 33).addBox(-16, -32, -24, 3, 0, 48, 0, false);
		roof_window_1.texOffs(32, 33).addBox(-10, -34, -24, 7, 0, 48, 0, false);
		roof_window_1.texOffs(68, 33).addBox(-2, -33, -24, 2, 0, 48, 0, false);

		inner_roof_4_r1 = new ModelMapper(modelDataWrapper);
		inner_roof_4_r1.setPos(-2, -33, 0);
		roof_window_1.addChild(inner_roof_4_r1);
		setRotationAngle(inner_roof_4_r1, 0, 0, 0.5236F);
		inner_roof_4_r1.texOffs(72, 0).addBox(-2, 0, -24, 2, 0, 48, 0, false);

		inner_roof_2_r1 = new ModelMapper(modelDataWrapper);
		inner_roof_2_r1.setPos(-13, -32, 0);
		roof_window_1.addChild(inner_roof_2_r1);
		setRotationAngle(inner_roof_2_r1, 0, 0, -0.5236F);
		inner_roof_2_r1.texOffs(46, 33).addBox(0, 0, -24, 4, 0, 48, 0, false);

		roof_window_2 = new ModelMapper(modelDataWrapper);
		roof_window_2.setPos(0, 24, 0);
		roof_window_2.texOffs(92, 83).addBox(-16, -32, -16, 3, 0, 32, 0, false);
		roof_window_2.texOffs(122, 0).addBox(-10, -34, -16, 7, 0, 32, 0, false);
		roof_window_2.texOffs(136, 0).addBox(-2, -33, -16, 2, 0, 32, 0, false);

		inner_roof_5_r1 = new ModelMapper(modelDataWrapper);
		inner_roof_5_r1.setPos(-2, -33, 8);
		roof_window_2.addChild(inner_roof_5_r1);
		setRotationAngle(inner_roof_5_r1, 0, 0, 0.5236F);
		inner_roof_5_r1.texOffs(0, 146).addBox(-2, 0, -24, 2, 0, 32, 0, false);

		inner_roof_3_r1 = new ModelMapper(modelDataWrapper);
		inner_roof_3_r1.setPos(-13, -32, 8);
		roof_window_2.addChild(inner_roof_3_r1);
		setRotationAngle(inner_roof_3_r1, 0, 0, -0.5236F);
		inner_roof_3_r1.texOffs(0, 33).addBox(0, 0, -24, 4, 0, 32, 0, false);

		roof_window_3 = new ModelMapper(modelDataWrapper);
		roof_window_3.setPos(0, 24, 0);
		roof_window_3.texOffs(54, 33).addBox(-16, -32, -24, 3, 0, 48, 0, false);
		roof_window_3.texOffs(32, 33).addBox(-10, -34, -24, 7, 0, 48, 0, false);
		roof_window_3.texOffs(68, 33).addBox(-2, -33, -24, 2, 0, 48, 0, false);

		inner_roof_5_r2 = new ModelMapper(modelDataWrapper);
		inner_roof_5_r2.setPos(-2, -33, 0);
		roof_window_3.addChild(inner_roof_5_r2);
		setRotationAngle(inner_roof_5_r2, 0, 0, 0.5236F);
		inner_roof_5_r2.texOffs(72, 0).addBox(-2, 0, -24, 2, 0, 48, 0, false);

		inner_roof_3_r2 = new ModelMapper(modelDataWrapper);
		inner_roof_3_r2.setPos(-13, -32, 0);
		roof_window_3.addChild(inner_roof_3_r2);
		setRotationAngle(inner_roof_3_r2, 0, 0, -0.5236F);
		inner_roof_3_r2.texOffs(252, 0).addBox(0, 0, -24, 4, 0, 48, 0, false);

		roof_window_light = new ModelMapper(modelDataWrapper);
		roof_window_light.setPos(0, 24, 0);


		window_light_r1 = new ModelMapper(modelDataWrapper);
		window_light_r1.setPos(-13, -32, 0);
		roof_window_light.addChild(window_light_r1);
		setRotationAngle(window_light_r1, 0, 0, -0.5236F);
		window_light_r1.texOffs(60, 33).addBox(1, -0.001F, -24, 2, 0, 48, 0, false);

		roof_door = new ModelMapper(modelDataWrapper);
		roof_door.setPos(0, 24, 0);
		roof_door.texOffs(88, 48).addBox(-18, -32, -16, 5, 0, 32, 0, false);
		roof_door.texOffs(122, 0).addBox(-10, -34, -16, 7, 0, 32, 0, false);
		roof_door.texOffs(136, 0).addBox(-2, -33, -16, 2, 0, 32, 0, false);

		inner_roof_4_r2 = new ModelMapper(modelDataWrapper);
		inner_roof_4_r2.setPos(-2, -33, 0);
		roof_door.addChild(inner_roof_4_r2);
		setRotationAngle(inner_roof_4_r2, 0, 0, 0.5236F);
		inner_roof_4_r2.texOffs(0, 146).addBox(-2, 0, -16, 2, 0, 32, 0, false);

		inner_roof_2_r2 = new ModelMapper(modelDataWrapper);
		inner_roof_2_r2.setPos(-13, -32, 0);
		roof_door.addChild(inner_roof_2_r2);
		setRotationAngle(inner_roof_2_r2, 0, 0, -0.5236F);
		inner_roof_2_r2.texOffs(0, 83).addBox(0, 0, -16, 4, 0, 32, 0, true);

		roof_exterior = new ModelMapper(modelDataWrapper);
		roof_exterior.setPos(0, 24, 0);
		roof_exterior.texOffs(56, 83).addBox(-6, -42, -20, 6, 0, 40, 0, false);

		outer_roof_5_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_5_r1.setPos(-9.9394F, -41.3064F, 0);
		roof_exterior.addChild(outer_roof_5_r1);
		setRotationAngle(outer_roof_5_r1, 0, 0, -0.1745F);
		outer_roof_5_r1.texOffs(40, 83).addBox(-4, 0, -20, 8, 0, 40, 0, false);

		outer_roof_4_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_4_r1.setPos(-15.1778F, -39.8628F, 0);
		roof_exterior.addChild(outer_roof_4_r1);
		setRotationAngle(outer_roof_4_r1, 0, 0, -0.5236F);
		outer_roof_4_r1.texOffs(0, 33).addBox(-1.5F, 0, -20, 3, 0, 40, 0, false);

		outer_roof_3_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_3_r1.setPos(-16.9769F, -38.2468F, 0);
		roof_exterior.addChild(outer_roof_3_r1);
		setRotationAngle(outer_roof_3_r1, 0, 0, -1.0472F);
		outer_roof_3_r1.texOffs(68, 83).addBox(-1, 0, -20, 2, 0, 40, 0, false);

		outer_roof_2_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_2_r1.setPos(-17.5872F, -36.3872F, 0);
		roof_exterior.addChild(outer_roof_2_r1);
		setRotationAngle(outer_roof_2_r1, 0, 0, 0.1107F);
		outer_roof_2_r1.texOffs(0, 84).addBox(0, -1, -20, 0, 2, 40, 0, false);

		outer_roof_1_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_1_r1.setPos(-20, -14, 0);
		roof_exterior.addChild(outer_roof_1_r1);
		setRotationAngle(outer_roof_1_r1, 0, 0, 0.1107F);
		outer_roof_1_r1.texOffs(72, 239).addBox(-1, -22, -20, 1, 4, 40, 0, false);

		door = new ModelMapper(modelDataWrapper);
		door.setPos(0, 24, 0);
		door.texOffs(0, 198).addBox(-20, 0, -20, 20, 1, 40, 0, false);

		door_right = new ModelMapper(modelDataWrapper);
		door_right.setPos(0, 0, 0);
		door.addChild(door_right);
		door_right.texOffs(125, 198).addBox(-20.8F, -14, -15, 1, 14, 15, 0, false);

		door_right_top_r1 = new ModelMapper(modelDataWrapper);
		door_right_top_r1.setPos(-20.8F, -14, 0);
		door_right.addChild(door_right_top_r1);
		setRotationAngle(door_right_top_r1, 0, 0, 0.1107F);
		door_right_top_r1.texOffs(0, 33).addBox(0, -19, -15, 1, 19, 15, 0, false);

		door_left = new ModelMapper(modelDataWrapper);
		door_left.setPos(0, 0, 0);
		door.addChild(door_left);
		door_left.texOffs(0, 256).addBox(-20.8F, -14, 0, 1, 14, 15, 0, false);

		door_left_top_r1 = new ModelMapper(modelDataWrapper);
		door_left_top_r1.setPos(-20.8F, -14, 0);
		door_left.addChild(door_left_top_r1);
		setRotationAngle(door_left_top_r1, 0, 0, 0.1107F);
		door_left_top_r1.texOffs(0, 83).addBox(0, -19, 0, 1, 19, 15, 0, false);

		door_handrail_1 = new ModelMapper(modelDataWrapper);
		door_handrail_1.setPos(0, 24, 0);
		door_handrail_1.texOffs(352, 0).addBox(0, -33.75F, 0, 0, 2, 0, 0.2F, false);
		door_handrail_1.texOffs(359, 4).addBox(0, -31.5F, -14, 0, 32, 0, 0.2F, false);
		door_handrail_1.texOffs(359, 4).addBox(0, -31.5F, 14, 0, 32, 0, 0.2F, false);

		handrail_11_r1 = new ModelMapper(modelDataWrapper);
		handrail_11_r1.setPos(0, 0, -30);
		door_handrail_1.addChild(handrail_11_r1);
		setRotationAngle(handrail_11_r1, -1.5708F, 0, 0);
		handrail_11_r1.texOffs(352, 0).addBox(0, -46, -31.5F, 0, 32, 0, 0.2F, false);

		door_handrail_2 = new ModelMapper(modelDataWrapper);
		door_handrail_2.setPos(0, 24, 0);
		door_handrail_2.texOffs(359, 3).addBox(0, -33, 0, 0, 33, 0, 0.2F, false);

		door_exterior = new ModelMapper(modelDataWrapper);
		door_exterior.setPos(0, 24, 0);
		door_exterior.texOffs(0, 256).addBox(-21, 0, -16, 1, 4, 32, 0, false);

		door_left_exterior = new ModelMapper(modelDataWrapper);
		door_left_exterior.setPos(0, 0, 0);
		door_exterior.addChild(door_left_exterior);
		door_left_exterior.texOffs(197, 164).addBox(-20.799F, -14, 0, 0, 14, 15, 0, false);

		door_left_top_r2 = new ModelMapper(modelDataWrapper);
		door_left_top_r2.setPos(-14.8357F, -13.3373F, 0);
		door_left_exterior.addChild(door_left_top_r2);
		setRotationAngle(door_left_top_r2, 0, 0, 0.1107F);
		door_left_top_r2.texOffs(80, 183).addBox(-6, -19, 0, 0, 19, 15, 0, false);

		door_right_exterior = new ModelMapper(modelDataWrapper);
		door_right_exterior.setPos(0, 0, 0);
		door_exterior.addChild(door_right_exterior);
		door_right_exterior.texOffs(178, 23).addBox(-20.799F, -14, -15, 0, 14, 15, 0, false);

		door_right_top_r2 = new ModelMapper(modelDataWrapper);
		door_right_top_r2.setPos(-20.799F, -13.9999F, 0);
		door_right_exterior.addChild(door_right_top_r2);
		setRotationAngle(door_right_top_r2, 0, 0, 0.1107F);
		door_right_top_r2.texOffs(0, 183).addBox(0, -19, -15, 0, 19, 15, 0, false);

		end = new ModelMapper(modelDataWrapper);
		end.setPos(0, 24, 0);
		end.texOffs(0, 239).addBox(-20, 0, -12, 40, 1, 16, 0, false);
		end.texOffs(220, 17).addBox(18, -14, 7, 2, 14, 3, 0, true);
		end.texOffs(220, 17).addBox(-20, -14, 7, 2, 14, 3, 0, false);
		end.texOffs(247, 127).addBox(9.5F, -34, -12, 9, 34, 19, 0, true);
		end.texOffs(244, 0).addBox(-18.5F, -34, -12, 9, 34, 19, 0, false);

		upper_wall_2_r1 = new ModelMapper(modelDataWrapper);
		upper_wall_2_r1.setPos(-20, -14, 0);
		end.addChild(upper_wall_2_r1);
		setRotationAngle(upper_wall_2_r1, 0, 0, 0.1107F);
		upper_wall_2_r1.texOffs(167, 146).addBox(0, -19, 7, 2, 19, 3, 0, false);

		upper_wall_1_r1 = new ModelMapper(modelDataWrapper);
		upper_wall_1_r1.setPos(20, -14, 0);
		end.addChild(upper_wall_1_r1);
		setRotationAngle(upper_wall_1_r1, 0, 0, -0.1107F);
		upper_wall_1_r1.texOffs(167, 146).addBox(-2, -19, 7, 2, 19, 3, 0, true);

		end_exterior = new ModelMapper(modelDataWrapper);
		end_exterior.setPos(0, 24, 0);
		end_exterior.texOffs(302, 279).addBox(20, 0, -12, 1, 4, 20, 0, true);
		end_exterior.texOffs(302, 279).addBox(-21, 0, -12, 1, 4, 20, 0, false);
		end_exterior.texOffs(0, 156).addBox(18, -14, -12, 2, 14, 22, 0, true);
		end_exterior.texOffs(131, 146).addBox(-20, -14, -12, 2, 14, 22, 0, false);
		end_exterior.texOffs(218, 282).addBox(9.5F, -34, -12, 9, 34, 0, 0, false);
		end_exterior.texOffs(218, 282).addBox(-18.5F, -34, -12, 9, 34, 0, 0, true);
		end_exterior.texOffs(80, 230).addBox(-18, -41, -12, 36, 7, 0, 0, false);

		upper_wall_2_r2 = new ModelMapper(modelDataWrapper);
		upper_wall_2_r2.setPos(-20, -14, 0);
		end_exterior.addChild(upper_wall_2_r2);
		setRotationAngle(upper_wall_2_r2, 0, 0, 0.1107F);
		upper_wall_2_r2.texOffs(65, 146).addBox(0, -19, -12, 2, 19, 22, 0, false);

		upper_wall_1_r2 = new ModelMapper(modelDataWrapper);
		upper_wall_1_r2.setPos(20, -14, 0);
		end_exterior.addChild(upper_wall_1_r2);
		setRotationAngle(upper_wall_1_r2, 0, 0, -0.1107F);
		upper_wall_1_r2.texOffs(65, 146).addBox(-2, -19, -12, 2, 19, 22, 0, true);

		roof_end = new ModelMapper(modelDataWrapper);
		roof_end.setPos(0, 24, 0);


		handrail_2_r3 = new ModelMapper(modelDataWrapper);
		handrail_2_r3.setPos(0, 0, 0);
		roof_end.addChild(handrail_2_r3);
		setRotationAngle(handrail_2_r3, -1.5708F, 0, 0);
		handrail_2_r3.texOffs(352, 0).addBox(0, -40, -31.5F, 0, 16, 0, 0.2F, false);

		inner_roof_7_r1 = new ModelMapper(modelDataWrapper);
		inner_roof_7_r1.setPos(0, -33, 16);
		roof_end.addChild(inner_roof_7_r1);
		setRotationAngle(inner_roof_7_r1, -0.5236F, 0, 0);
		inner_roof_7_r1.texOffs(24, 120).addBox(-2, 0, -2, 4, 0, 2, 0, false);

		inner_roof_1 = new ModelMapper(modelDataWrapper);
		inner_roof_1.setPos(-2, -33, 16);
		roof_end.addChild(inner_roof_1);
		inner_roof_1.texOffs(76, 83).addBox(-17, 1, -12, 6, 0, 36, 0, false);
		inner_roof_1.texOffs(82, 0).addBox(-8, -1, -28, 10, 0, 52, 0, false);
		inner_roof_1.texOffs(4, 0).addBox(0, 0, 0, 2, 0, 24, 0, false);

		inner_roof_6_r1 = new ModelMapper(modelDataWrapper);
		inner_roof_6_r1.setPos(0, 0, 0);
		inner_roof_1.addChild(inner_roof_6_r1);
		setRotationAngle(inner_roof_6_r1, -0.5236F, 0, 0.5236F);
		inner_roof_6_r1.texOffs(15, 33).addBox(-2, 0, -2, 2, 0, 2, 0, false);

		inner_roof_4_r3 = new ModelMapper(modelDataWrapper);
		inner_roof_4_r3.setPos(0, 0, -16);
		inner_roof_1.addChild(inner_roof_4_r3);
		setRotationAngle(inner_roof_4_r3, 0, 0, 0.5236F);
		inner_roof_4_r3.texOffs(0, 24).addBox(-2, 0, 16, 2, 0, 24, 0, false);

		inner_roof_2_r3 = new ModelMapper(modelDataWrapper);
		inner_roof_2_r3.setPos(-11, 1, -16);
		inner_roof_1.addChild(inner_roof_2_r3);
		setRotationAngle(inner_roof_2_r3, 0, 0, -0.5236F);
		inner_roof_2_r3.texOffs(270, 142).addBox(0, 0, 4, 4, 0, 36, 0, true);

		inner_roof_2 = new ModelMapper(modelDataWrapper);
		inner_roof_2.setPos(-2, -33, 16);
		roof_end.addChild(inner_roof_2);
		inner_roof_2.texOffs(76, 83).addBox(15, 1, -12, 6, 0, 36, 0, true);
		inner_roof_2.texOffs(82, 0).addBox(2, -1, -28, 10, 0, 52, 0, true);
		inner_roof_2.texOffs(4, 0).addBox(2, 0, 0, 2, 0, 24, 0, true);

		inner_roof_6_r2 = new ModelMapper(modelDataWrapper);
		inner_roof_6_r2.setPos(4, 0, 0);
		inner_roof_2.addChild(inner_roof_6_r2);
		setRotationAngle(inner_roof_6_r2, -0.5236F, 0, -0.5236F);
		inner_roof_6_r2.texOffs(15, 33).addBox(0, 0, -2, 2, 0, 2, 0, true);

		inner_roof_4_r4 = new ModelMapper(modelDataWrapper);
		inner_roof_4_r4.setPos(4, 0, -16);
		inner_roof_2.addChild(inner_roof_4_r4);
		setRotationAngle(inner_roof_4_r4, 0, 0, -0.5236F);
		inner_roof_4_r4.texOffs(0, 24).addBox(0, 0, 16, 2, 0, 24, 0, true);

		inner_roof_2_r4 = new ModelMapper(modelDataWrapper);
		inner_roof_2_r4.setPos(15, 1, -16);
		inner_roof_2.addChild(inner_roof_2_r4);
		setRotationAngle(inner_roof_2_r4, 0, 0, 0.5236F);
		inner_roof_2_r4.texOffs(88, 0).addBox(-4, 0, 4, 4, 0, 36, 0, true);

		roof_end_exterior = new ModelMapper(modelDataWrapper);
		roof_end_exterior.setPos(0, 24, 0);
		roof_end_exterior.texOffs(0, 33).addBox(-8, -43, 0, 16, 2, 48, 0, false);

		vent_2_r1 = new ModelMapper(modelDataWrapper);
		vent_2_r1.setPos(-8, -43, 0);
		roof_end_exterior.addChild(vent_2_r1);
		setRotationAngle(vent_2_r1, 0, 0, -0.3491F);
		vent_2_r1.texOffs(131, 148).addBox(-9, 0, 0, 9, 2, 48, 0, false);

		vent_1_r1 = new ModelMapper(modelDataWrapper);
		vent_1_r1.setPos(8, -43, 0);
		roof_end_exterior.addChild(vent_1_r1);
		setRotationAngle(vent_1_r1, 0, 0, 0.3491F);
		vent_1_r1.texOffs(131, 148).addBox(0, 0, 0, 9, 2, 48, 0, true);

		outer_roof_1 = new ModelMapper(modelDataWrapper);
		outer_roof_1.setPos(0, 0, 0);
		roof_end_exterior.addChild(outer_roof_1);
		outer_roof_1.texOffs(276, 104).addBox(-6, -42, -12, 6, 1, 20, 0, false);

		outer_roof_5_r2 = new ModelMapper(modelDataWrapper);
		outer_roof_5_r2.setPos(-9.7656F, -40.3206F, 0);
		outer_roof_1.addChild(outer_roof_5_r2);
		setRotationAngle(outer_roof_5_r2, 0, 0, -0.1745F);
		outer_roof_5_r2.texOffs(34, 256).addBox(-4, -1, -12, 8, 1, 20, 0, false);

		outer_roof_4_r2 = new ModelMapper(modelDataWrapper);
		outer_roof_4_r2.setPos(-14.6775F, -38.9948F, 0);
		outer_roof_1.addChild(outer_roof_4_r2);
		setRotationAngle(outer_roof_4_r2, 0, 0, -0.5236F);
		outer_roof_4_r2.texOffs(46, 277).addBox(-1.5F, -1, -12, 3, 1, 20, 0, false);

		outer_roof_3_r2 = new ModelMapper(modelDataWrapper);
		outer_roof_3_r2.setPos(-16.1105F, -37.7448F, 0);
		outer_roof_1.addChild(outer_roof_3_r2);
		setRotationAngle(outer_roof_3_r2, 0, 0, -1.0472F);
		outer_roof_3_r2.texOffs(116, 283).addBox(-1, -1, -12, 2, 1, 20, 0, false);

		outer_roof_2_r2 = new ModelMapper(modelDataWrapper);
		outer_roof_2_r2.setPos(-17.587F, -36.3849F, 0);
		outer_roof_1.addChild(outer_roof_2_r2);
		setRotationAngle(outer_roof_2_r2, 0, 0, 0.1107F);
		outer_roof_2_r2.texOffs(283, 160).addBox(0, -1, -12, 1, 2, 20, 0, false);

		outer_roof_1_r2 = new ModelMapper(modelDataWrapper);
		outer_roof_1_r2.setPos(-20, -14, 0);
		outer_roof_1.addChild(outer_roof_1_r2);
		setRotationAngle(outer_roof_1_r2, 0, 0, 0.1107F);
		outer_roof_1_r2.texOffs(176, 281).addBox(-1, -22, -12, 1, 4, 20, 0, false);

		outer_roof_2 = new ModelMapper(modelDataWrapper);
		outer_roof_2.setPos(0, 0, 0);
		roof_end_exterior.addChild(outer_roof_2);
		outer_roof_2.texOffs(276, 104).addBox(0, -42, -12, 6, 1, 20, 0, false);

		outer_roof_5_r3 = new ModelMapper(modelDataWrapper);
		outer_roof_5_r3.setPos(9.7656F, -40.3206F, 0);
		outer_roof_2.addChild(outer_roof_5_r3);
		setRotationAngle(outer_roof_5_r3, 0, 0, 0.1745F);
		outer_roof_5_r3.texOffs(34, 256).addBox(-4, -1, -12, 8, 1, 20, 0, true);

		outer_roof_4_r3 = new ModelMapper(modelDataWrapper);
		outer_roof_4_r3.setPos(14.6775F, -38.9948F, 0);
		outer_roof_2.addChild(outer_roof_4_r3);
		setRotationAngle(outer_roof_4_r3, 0, 0, 0.5236F);
		outer_roof_4_r3.texOffs(46, 277).addBox(-1.5F, -1, -12, 3, 1, 20, 0, true);

		outer_roof_3_r3 = new ModelMapper(modelDataWrapper);
		outer_roof_3_r3.setPos(16.1105F, -37.7448F, 0);
		outer_roof_2.addChild(outer_roof_3_r3);
		setRotationAngle(outer_roof_3_r3, 0, 0, 1.0472F);
		outer_roof_3_r3.texOffs(116, 283).addBox(-1, -1, -12, 2, 1, 20, 0, true);

		outer_roof_2_r3 = new ModelMapper(modelDataWrapper);
		outer_roof_2_r3.setPos(17.587F, -36.3849F, 0);
		outer_roof_2.addChild(outer_roof_2_r3);
		setRotationAngle(outer_roof_2_r3, 0, 0, -0.1107F);
		outer_roof_2_r3.texOffs(283, 160).addBox(-1, -1, -12, 1, 2, 20, 0, true);

		outer_roof_1_r3 = new ModelMapper(modelDataWrapper);
		outer_roof_1_r3.setPos(20, -14, 0);
		outer_roof_2.addChild(outer_roof_1_r3);
		setRotationAngle(outer_roof_1_r3, 0, 0, -0.1107F);
		outer_roof_1_r3.texOffs(176, 281).addBox(0, -22, -12, 1, 4, 20, 0, true);

		roof_light = new ModelMapper(modelDataWrapper);
		roof_light.setPos(0, 24, 0);


		roof_light_r1 = new ModelMapper(modelDataWrapper);
		roof_light_r1.setPos(-2, -33, 0);
		roof_light.addChild(roof_light_r1);
		setRotationAngle(roof_light_r1, 0, 0, 0.5236F);
		roof_light_r1.texOffs(64, 33).addBox(-2, -0.1F, -24, 2, 0, 48, 0, false);

		roof_end_light = new ModelMapper(modelDataWrapper);
		roof_end_light.setPos(0, 24, 0);


		light_5_r1 = new ModelMapper(modelDataWrapper);
		light_5_r1.setPos(2, -33, 0);
		roof_end_light.addChild(light_5_r1);
		setRotationAngle(light_5_r1, 0, 0, -0.5236F);
		light_5_r1.texOffs(0, 0).addBox(0, -0.1F, 16, 2, 0, 24, 0, false);

		light_4_r1 = new ModelMapper(modelDataWrapper);
		light_4_r1.setPos(2, -33, 16);
		roof_end_light.addChild(light_4_r1);
		setRotationAngle(light_4_r1, -0.5236F, 0, -0.5236F);
		light_4_r1.texOffs(0, 2).addBox(0, -0.1F, -2, 2, 0, 2, 0, true);

		light_3_r1 = new ModelMapper(modelDataWrapper);
		light_3_r1.setPos(0, -33, 16);
		roof_end_light.addChild(light_3_r1);
		setRotationAngle(light_3_r1, -0.5236F, 0, 0);
		light_3_r1.texOffs(110, 119).addBox(-2, -0.1F, -2, 4, 0, 2, 0, false);

		light_2_r1 = new ModelMapper(modelDataWrapper);
		light_2_r1.setPos(-2, -33, 16);
		roof_end_light.addChild(light_2_r1);
		setRotationAngle(light_2_r1, -0.5236F, 0, 0.5236F);
		light_2_r1.texOffs(0, 2).addBox(-2, -0.1F, -2, 2, 0, 2, 0, false);

		light_1_r1 = new ModelMapper(modelDataWrapper);
		light_1_r1.setPos(-2, -33, 0);
		roof_end_light.addChild(light_1_r1);
		setRotationAngle(light_1_r1, 0, 0, 0.5236F);
		light_1_r1.texOffs(0, 0).addBox(-2, -0.1F, 16, 2, 0, 24, 0, false);

		head = new ModelMapper(modelDataWrapper);
		head.setPos(0, 24, 0);
		head.texOffs(91, 146).addBox(18, -14, 4, 2, 14, 6, 0, false);
		head.texOffs(91, 146).addBox(-20, -14, 4, 2, 14, 6, 0, true);
		head.texOffs(246, 206).addBox(-18, -34, 4, 36, 34, 0, 0, false);

		upper_wall_2_r3 = new ModelMapper(modelDataWrapper);
		upper_wall_2_r3.setPos(-20, -14, 0);
		head.addChild(upper_wall_2_r3);
		setRotationAngle(upper_wall_2_r3, 0, 0, 0.1107F);
		upper_wall_2_r3.texOffs(24, 211).addBox(0, -19, 4, 2, 19, 6, 0, true);

		upper_wall_1_r3 = new ModelMapper(modelDataWrapper);
		upper_wall_1_r3.setPos(20, -14, 0);
		head.addChild(upper_wall_1_r3);
		setRotationAngle(upper_wall_1_r3, 0, 0, -0.1107F);
		upper_wall_1_r3.texOffs(24, 211).addBox(-2, -19, 4, 2, 19, 6, 0, false);

		head_exterior = new ModelMapper(modelDataWrapper);
		head_exterior.setPos(0, 24, 0);
		head_exterior.texOffs(134, 103).addBox(-21, 0, -18, 42, 7, 22, 0, false);
		head_exterior.texOffs(125, 196).addBox(20, 0, 4, 1, 7, 4, 0, true);
		head_exterior.texOffs(125, 196).addBox(-21, 0, 4, 1, 7, 4, 0, false);
		head_exterior.texOffs(154, 257).addBox(18, -14, -9, 2, 14, 19, 0, false);
		head_exterior.texOffs(197, 146).addBox(-20, -14, -9, 2, 14, 19, 0, true);
		head_exterior.texOffs(90, 256).addBox(18, -14, -18, 1, 14, 9, 0, false);
		head_exterior.texOffs(90, 256).addBox(-19, -14, -18, 1, 14, 9, 0, true);
		head_exterior.texOffs(244, 53).addBox(-18, -34, 3, 36, 34, 0, 0, false);

		driver_door_upper_2_r1 = new ModelMapper(modelDataWrapper);
		driver_door_upper_2_r1.setPos(-20, -14, 0);
		head_exterior.addChild(driver_door_upper_2_r1);
		setRotationAngle(driver_door_upper_2_r1, 0, 0, 0.1107F);
		driver_door_upper_2_r1.texOffs(288, 240).addBox(1, -19, -18, 1, 19, 9, 0, true);
		driver_door_upper_2_r1.texOffs(178, 0).addBox(0, -19, -9, 2, 19, 19, 0, true);

		driver_door_upper_1_r1 = new ModelMapper(modelDataWrapper);
		driver_door_upper_1_r1.setPos(20, -14, 0);
		head_exterior.addChild(driver_door_upper_1_r1);
		setRotationAngle(driver_door_upper_1_r1, 0, 0, -0.1107F);
		driver_door_upper_1_r1.texOffs(288, 240).addBox(-2, -19, -18, 1, 19, 9, 0, false);
		driver_door_upper_1_r1.texOffs(178, 0).addBox(-2, -19, -9, 2, 19, 19, 0, false);

		front = new ModelMapper(modelDataWrapper);
		front.setPos(0, 0, 0);
		head_exterior.addChild(front);
		front.texOffs(134, 88).addBox(-19, 0.2966F, -26.5318F, 38, 5, 0, 0, false);

		bottom_r1 = new ModelMapper(modelDataWrapper);
		bottom_r1.setPos(0, 7, 4);
		front.addChild(bottom_r1);
		setRotationAngle(bottom_r1, -0.0698F, 0, 0);
		bottom_r1.texOffs(0, 0).addBox(-21, 0, -33, 42, 0, 33, 0, false);

		front_middle_top_r1 = new ModelMapper(modelDataWrapper);
		front_middle_top_r1.setPos(0, -42, -12);
		front.addChild(front_middle_top_r1);
		setRotationAngle(front_middle_top_r1, 0.3491F, 0, 0);
		front_middle_top_r1.texOffs(161, 0).addBox(-6, 0, -11, 12, 0, 11, 0, false);

		front_panel_2_r1 = new ModelMapper(modelDataWrapper);
		front_panel_2_r1.setPos(0, 41.314F, -36.3702F);
		front.addChild(front_panel_2_r1);
		setRotationAngle(front_panel_2_r1, -0.1745F, 0, 0);
		front_panel_2_r1.texOffs(162, 206).addBox(-19, -82, 1, 38, 28, 0, 0, false);

		front_panel_1_r1 = new ModelMapper(modelDataWrapper);
		front_panel_1_r1.setPos(0, 0.2966F, -26.5318F);
		front.addChild(front_panel_1_r1);
		setRotationAngle(front_panel_1_r1, -0.0436F, 0, 0);
		front_panel_1_r1.texOffs(134, 76).addBox(-19, -12, 0, 38, 12, 0, 0, false);

		side_1 = new ModelMapper(modelDataWrapper);
		side_1.setPos(0, 0, 0);
		front.addChild(side_1);
		side_1.texOffs(22, 30).addBox(19, -14, -18, 1, 14, 0, 0, false);

		front_side_bottom_1_r1 = new ModelMapper(modelDataWrapper);
		front_side_bottom_1_r1.setPos(21, 0, -13);
		side_1.addChild(front_side_bottom_1_r1);
		setRotationAngle(front_side_bottom_1_r1, 0, 0.1745F, 0.1745F);
		front_side_bottom_1_r1.texOffs(0, 50).addBox(0, 0, -16, 0, 7, 23, 0, false);

		outer_roof_4_r4 = new ModelMapper(modelDataWrapper);
		outer_roof_4_r4.setPos(6, -42, -12);
		side_1.addChild(outer_roof_4_r4);
		setRotationAngle(outer_roof_4_r4, 0.3491F, 0, 0.1745F);
		outer_roof_4_r4.texOffs(123, 103).addBox(0, 0, -11, 11, 0, 11, 0, true);

		outer_roof_1_r4 = new ModelMapper(modelDataWrapper);
		outer_roof_1_r4.setPos(20, -14, 0);
		side_1.addChild(outer_roof_1_r4);
		setRotationAngle(outer_roof_1_r4, 0, 0, -0.1107F);
		outer_roof_1_r4.texOffs(118, 118).addBox(0, -22, -18, 1, 4, 6, 0, true);
		outer_roof_1_r4.texOffs(22, 11).addBox(-1, -19, -18, 1, 19, 0, 0, false);

		outer_roof_2_r4 = new ModelMapper(modelDataWrapper);
		outer_roof_2_r4.setPos(17.587F, -36.3849F, 0);
		side_1.addChild(outer_roof_2_r4);
		setRotationAngle(outer_roof_2_r4, 0, 0, -0.1107F);
		outer_roof_2_r4.texOffs(0, 88).addBox(0, -1, -18, 0, 2, 6, 0, false);

		outer_roof_3_r4 = new ModelMapper(modelDataWrapper);
		outer_roof_3_r4.setPos(15.813F, -37.5414F, -17.4163F);
		side_1.addChild(outer_roof_3_r4);
		setRotationAngle(outer_roof_3_r4, 0.1745F, 0, 0.7418F);
		outer_roof_3_r4.texOffs(6, 83).addBox(-3.5F, 0, -5.5F, 7, 0, 11, 0, true);

		front_side_lower_1_r1 = new ModelMapper(modelDataWrapper);
		front_side_lower_1_r1.setPos(20, 0, -18);
		side_1.addChild(front_side_lower_1_r1);
		setRotationAngle(front_side_lower_1_r1, 0, 0.1745F, 0);
		front_side_lower_1_r1.texOffs(0, 0).addBox(0, -14, -11, 0, 20, 11, 0, true);

		front_side_upper_1_r1 = new ModelMapper(modelDataWrapper);
		front_side_upper_1_r1.setPos(20, -14, -18);
		side_1.addChild(front_side_upper_1_r1);
		setRotationAngle(front_side_upper_1_r1, 0, 0.1745F, -0.1107F);
		front_side_upper_1_r1.texOffs(0, 135).addBox(0, -23, -11, 0, 23, 11, 0, false);

		side_2 = new ModelMapper(modelDataWrapper);
		side_2.setPos(-21, 0, -9);
		front.addChild(side_2);
		side_2.texOffs(22, 30).addBox(1, -14, -9, 1, 14, 0, 0, false);

		front_side_bottom_2_r1 = new ModelMapper(modelDataWrapper);
		front_side_bottom_2_r1.setPos(0, 0, -4);
		side_2.addChild(front_side_bottom_2_r1);
		setRotationAngle(front_side_bottom_2_r1, 0, -0.1745F, -0.1745F);
		front_side_bottom_2_r1.texOffs(0, 50).addBox(0, 0, -16, 0, 7, 23, 0, false);

		outer_roof_8_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_8_r1.setPos(5.187F, -37.5414F, -8.4163F);
		side_2.addChild(outer_roof_8_r1);
		setRotationAngle(outer_roof_8_r1, 0.1745F, 0, -0.7418F);
		outer_roof_8_r1.texOffs(6, 83).addBox(-3.5F, 0, -5.5F, 7, 0, 11, 0, false);

		outer_roof_7_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_7_r1.setPos(3.413F, -36.3849F, 9);
		side_2.addChild(outer_roof_7_r1);
		setRotationAngle(outer_roof_7_r1, 0, 0, 0.1107F);
		outer_roof_7_r1.texOffs(0, 88).addBox(0, -1, -18, 0, 2, 6, 0, false);

		outer_roof_6_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_6_r1.setPos(15, -42, -3);
		side_2.addChild(outer_roof_6_r1);
		setRotationAngle(outer_roof_6_r1, 0.3491F, 0, -0.1745F);
		outer_roof_6_r1.texOffs(123, 103).addBox(-11, 0, -11, 11, 0, 11, 0, false);

		outer_roof_5_r4 = new ModelMapper(modelDataWrapper);
		outer_roof_5_r4.setPos(1, -14, 9);
		side_2.addChild(outer_roof_5_r4);
		setRotationAngle(outer_roof_5_r4, 0, 0, 0.1107F);
		outer_roof_5_r4.texOffs(118, 118).addBox(-1, -22, -18, 1, 4, 6, 0, false);
		outer_roof_5_r4.texOffs(22, 11).addBox(0, -19, -18, 1, 19, 0, 0, false);

		front_side_upper_2_r1 = new ModelMapper(modelDataWrapper);
		front_side_upper_2_r1.setPos(1, -14, -9);
		side_2.addChild(front_side_upper_2_r1);
		setRotationAngle(front_side_upper_2_r1, 0, -0.1745F, 0.1107F);
		front_side_upper_2_r1.texOffs(0, 135).addBox(0, -23, -11, 0, 23, 11, 0, false);

		front_side_lower_2_r1 = new ModelMapper(modelDataWrapper);
		front_side_lower_2_r1.setPos(1, 0, -9);
		side_2.addChild(front_side_lower_2_r1);
		setRotationAngle(front_side_lower_2_r1, 0, -0.1745F, 0);
		front_side_lower_2_r1.texOffs(0, 0).addBox(0, -14, -11, 0, 20, 11, 0, false);

		nose = new ModelMapper(modelDataWrapper);
		nose.setPos(0, -1.25F, 0);
		front.addChild(nose);


		center_nose = new ModelMapper(modelDataWrapper);
		center_nose.setPos(0, 1.25F, 0);
		nose.addChild(center_nose);
		center_nose.texOffs(156, 93).addBox(-5.5F, -0.25F, -31.25F, 11, 3, 0, 0, false);
		center_nose.texOffs(131, 93).addBox(-5.5F, -0.7666F, -29.3181F, 11, 0, 3, 0, false);
		center_nose.texOffs(0, 117).addBox(-5.5F, 3.0089F, -30.2838F, 11, 0, 4, 0, false);

		nose_5_r1 = new ModelMapper(modelDataWrapper);
		nose_5_r1.setPos(0, 3.4918F, -30.4135F);
		center_nose.addChild(nose_5_r1);
		setRotationAngle(nose_5_r1, -1.8326F, 0, 0);
		nose_5_r1.texOffs(0, 10).addBox(-5.5F, 0, -0.5F, 11, 0, 1, 0, false);

		nose_4_r1 = new ModelMapper(modelDataWrapper);
		nose_4_r1.setPos(0.5F, -5.6845F, -27.9547F);
		center_nose.addChild(nose_4_r1);
		setRotationAngle(nose_4_r1, -0.2618F, 0, 0);
		nose_4_r1.texOffs(0, 10).addBox(-6, 9, -1, 11, 0, 1, 0, false);

		nose_2_r1 = new ModelMapper(modelDataWrapper);
		nose_2_r1.setPos(0.5F, -9.3031F, -1.3234F);
		center_nose.addChild(nose_2_r1);
		setRotationAngle(nose_2_r1, 0.2618F, 0, 0);
		nose_2_r1.texOffs(0, 8).addBox(-6, 1, -31.25F, 11, 0, 2, 0, false);

		nose_side_1 = new ModelMapper(modelDataWrapper);
		nose_side_1.setPos(0, 0.25F, 0);
		nose.addChild(nose_side_1);


		nose_18_r1 = new ModelMapper(modelDataWrapper);
		nose_18_r1.setPos(-13.2048F, 4.0101F, -25.7633F);
		nose_side_1.addChild(nose_18_r1);
		setRotationAngle(nose_18_r1, 0, 0.7854F, 0);
		nose_18_r1.texOffs(30, 80).addBox(-1, 0, -0.5F, 3, 0, 1, 0, false);

		nose_17_r1 = new ModelMapper(modelDataWrapper);
		nose_17_r1.setPos(-10.3623F, 4.0101F, -27.9444F);
		nose_side_1.addChild(nose_17_r1);
		setRotationAngle(nose_17_r1, 0, 0.5236F, 0);
		nose_17_r1.texOffs(14, 94).addBox(-2, 0, -0.5F, 4, 0, 3, 0, false);

		nose_16_r1 = new ModelMapper(modelDataWrapper);
		nose_16_r1.setPos(-10.9576F, -43.4921F, -9.1171F);
		nose_side_1.addChild(nose_16_r1);
		setRotationAngle(nose_16_r1, 0, 0.2618F, 0);
		nose_16_r1.texOffs(28, 65).addBox(7, 47.5F, -19, 4, 0, 4, 0, false);

		nose_15_r1 = new ModelMapper(modelDataWrapper);
		nose_15_r1.setPos(-0.3159F, -36.8232F, 30.5981F);
		nose_side_1.addChild(nose_15_r1);
		setRotationAngle(nose_15_r1, -1.8326F, 0.2618F, 0);
		nose_15_r1.texOffs(8, 80).addBox(7, 47.5F, 55, 4, 0, 1, 0, false);

		nose_14_r1 = new ModelMapper(modelDataWrapper);
		nose_14_r1.setPos(-10.5481F, 3.5259F, -28.2661F);
		nose_side_1.addChild(nose_14_r1);
		setRotationAngle(nose_14_r1, -1.8326F, 0.5236F, 0);
		nose_14_r1.texOffs(8, 80).addBox(-2, 0, 0.5F, 4, 0, 1, 0, false);

		nose_13_r1 = new ModelMapper(modelDataWrapper);
		nose_13_r1.setPos(-13.297F, 4.4918F, -26.5626F);
		nose_side_1.addChild(nose_13_r1);
		setRotationAngle(nose_13_r1, -1.8326F, 0.7854F, 0);
		nose_13_r1.texOffs(36, 80).addBox(-1.5F, 0, -0.5F, 3, 0, 1, 0, false);

		nose_12_r1 = new ModelMapper(modelDataWrapper);
		nose_12_r1.setPos(-40.9283F, -41.8727F, 18.6381F);
		nose_side_1.addChild(nose_12_r1);
		setRotationAngle(nose_12_r1, -0.2618F, 0.7854F, 0);
		nose_12_r1.texOffs(36, 80).addBox(50, 47.5F, -1, 3, 0, 1, 0, false);

		nose_11_r1 = new ModelMapper(modelDataWrapper);
		nose_11_r1.setPos(-27.8485F, -41.8727F, -4.2314F);
		nose_side_1.addChild(nose_11_r1);
		setRotationAngle(nose_11_r1, -0.2618F, 0.5236F, 0);
		nose_11_r1.texOffs(8, 80).addBox(25, 47.5F, -1, 4, 0, 1, 0, false);

		nose_10_r1 = new ModelMapper(modelDataWrapper);
		nose_10_r1.setPos(-12.6933F, -41.8727F, -15.595F);
		nose_side_1.addChild(nose_10_r1);
		setRotationAngle(nose_10_r1, -0.2618F, 0.2618F, 0);
		nose_10_r1.texOffs(8, 80).addBox(7, 47.5F, -1, 4, 0, 1, 0, false);

		nose_9_r1 = new ModelMapper(modelDataWrapper);
		nose_9_r1.setPos(-20.8882F, 1.7334F, -20.1757F);
		nose_side_1.addChild(nose_9_r1);
		setRotationAngle(nose_9_r1, 0, 0.5236F, 0);
		nose_9_r1.texOffs(27, 69).addBox(10, -1.5F, -1, 5, 0, 3, 0, false);

		nose_8_r1 = new ModelMapper(modelDataWrapper);
		nose_8_r1.setPos(-19.2301F, 1.7334F, -24.5358F);
		nose_side_1.addChild(nose_8_r1);
		setRotationAngle(nose_8_r1, 0, 0.2618F, 0);
		nose_8_r1.texOffs(29, 115).addBox(11, -1.5F, -1, 4, 0, 3, 0, false);

		nose_7_r1 = new ModelMapper(modelDataWrapper);
		nose_7_r1.setPos(-19.6383F, 1.941F, -26.0595F);
		nose_side_1.addChild(nose_7_r1);
		setRotationAngle(nose_7_r1, 0.2618F, 0.2618F, 0);
		nose_7_r1.texOffs(24, 118).addBox(11, -1.5F, -1, 4, 0, 2, 0, false);

		nose_6_r1 = new ModelMapper(modelDataWrapper);
		nose_6_r1.setPos(-18.2128F, 1.941F, -23.5418F);
		nose_side_1.addChild(nose_6_r1);
		setRotationAngle(nose_6_r1, 0.2618F, 0.5236F, 0);
		nose_6_r1.texOffs(24, 118).addBox(7, -1.5F, -1, 4, 0, 2, 0, false);

		nose_5_r2 = new ModelMapper(modelDataWrapper);
		nose_5_r2.setPos(-10.4559F, 1.941F, -28.6712F);
		nose_side_1.addChild(nose_5_r2);
		setRotationAngle(nose_5_r2, 0.2618F, 0.7854F, 0);
		nose_5_r2.texOffs(0, 0).addBox(-5, -1.5F, -1, 3, 0, 2, 0, false);

		nose_4_r2 = new ModelMapper(modelDataWrapper);
		nose_4_r2.setPos(-10.7065F, 2.25F, -28.9218F);
		nose_side_1.addChild(nose_4_r2);
		setRotationAngle(nose_4_r2, 0, 0.7854F, 0);
		nose_4_r2.texOffs(25, 94).addBox(-5, -1.5F, -1, 3, 3, 0, 0, false);

		nose_3_r1 = new ModelMapper(modelDataWrapper);
		nose_3_r1.setPos(-18.39F, 2.25F, -23.8487F);
		nose_side_1.addChild(nose_3_r1);
		setRotationAngle(nose_3_r1, 0, 0.5236F, 0);
		nose_3_r1.texOffs(124, 43).addBox(7, -1.5F, -1, 4, 3, 0, 0, false);

		nose_2_r2 = new ModelMapper(modelDataWrapper);
		nose_2_r2.setPos(0.5544F, 2.25F, -31.837F);
		nose_side_1.addChild(nose_2_r2);
		setRotationAngle(nose_2_r2, 0, 0.2618F, 0);
		nose_2_r2.texOffs(124, 40).addBox(-10, -1.5F, -1, 4, 3, 0, 0, false);

		nose_side_2 = new ModelMapper(modelDataWrapper);
		nose_side_2.setPos(0, 0.25F, 0);
		nose.addChild(nose_side_2);


		nose_19_r1 = new ModelMapper(modelDataWrapper);
		nose_19_r1.setPos(12.4983F, 4.0098F, -26.4711F);
		nose_side_2.addChild(nose_19_r1);
		setRotationAngle(nose_19_r1, 0, -0.7854F, 0);
		nose_19_r1.texOffs(0, 0).addBox(-1, 0, -0.5F, 3, 0, 1, 0, false);

		nose_18_r2 = new ModelMapper(modelDataWrapper);
		nose_18_r2.setPos(9.6127F, 4.0089F, -26.6459F);
		nose_side_2.addChild(nose_18_r2);
		setRotationAngle(nose_18_r2, 0, -0.5236F, 0);
		nose_18_r2.texOffs(14, 94).addBox(-2, 0, -2, 4, 0, 3, 0, false);

		nose_17_r2 = new ModelMapper(modelDataWrapper);
		nose_17_r2.setPos(6.6643F, 4.0098F, -27.8677F);
		nose_side_2.addChild(nose_17_r2);
		setRotationAngle(nose_17_r2, 0, -0.2618F, 0);
		nose_17_r2.texOffs(28, 65).addBox(-2, 0, -2, 4, 0, 4, 0, false);

		nose_16_r2 = new ModelMapper(modelDataWrapper);
		nose_16_r2.setPos(13.297F, 4.4918F, -26.5626F);
		nose_side_2.addChild(nose_16_r2);
		setRotationAngle(nose_16_r2, -1.8326F, -0.7854F, 0);
		nose_16_r2.texOffs(36, 80).addBox(-1.5F, 0, -0.5F, 3, 0, 1, 0, false);

		nose_15_r2 = new ModelMapper(modelDataWrapper);
		nose_15_r2.setPos(10.6775F, 4.4918F, -28.4903F);
		nose_side_2.addChild(nose_15_r2);
		setRotationAngle(nose_15_r2, -1.8326F, -0.5236F, 0);
		nose_15_r2.texOffs(8, 80).addBox(-2, 0, -0.5F, 4, 0, 1, 0, false);

		nose_14_r2 = new ModelMapper(modelDataWrapper);
		nose_14_r2.setPos(7.2154F, 4.4927F, -29.9246F);
		nose_side_2.addChild(nose_14_r2);
		setRotationAngle(nose_14_r2, -1.8326F, -0.2618F, 0);
		nose_14_r2.texOffs(8, 80).addBox(-2, 0, -0.5F, 4, 0, 1, 0, false);

		nose_13_r2 = new ModelMapper(modelDataWrapper);
		nose_13_r2.setPos(10.5228F, -41.8727F, -11.7675F);
		nose_side_2.addChild(nose_13_r2);
		setRotationAngle(nose_13_r2, -0.2618F, -0.7854F, 0);
		nose_13_r2.texOffs(36, 80).addBox(-10, 47.5F, -1, 3, 0, 1, 0, false);

		nose_12_r2 = new ModelMapper(modelDataWrapper);
		nose_12_r2.setPos(12.2601F, -41.8727F, -13.2314F);
		nose_side_2.addChild(nose_12_r2);
		setRotationAngle(nose_12_r2, -0.2618F, -0.5236F, 0);
		nose_12_r2.texOffs(8, 80).addBox(-11, 47.5F, -1, 4, 0, 1, 0, false);

		nose_11_r2 = new ModelMapper(modelDataWrapper);
		nose_11_r2.setPos(12.6934F, -41.8717F, -15.5952F);
		nose_side_2.addChild(nose_11_r2);
		setRotationAngle(nose_11_r2, -0.2618F, -0.2618F, 0);
		nose_11_r2.texOffs(8, 80).addBox(-11, 47.5F, -1, 4, 0, 1, 0, false);

		nose_10_r2 = new ModelMapper(modelDataWrapper);
		nose_10_r2.setPos(16.558F, 0.2334F, -22.6757F);
		nose_side_2.addChild(nose_10_r2);
		setRotationAngle(nose_10_r2, 0, -0.5236F, 0);
		nose_10_r2.texOffs(27, 69).addBox(-10, 0, -1, 5, 0, 3, 0, false);

		nose_9_r2 = new ModelMapper(modelDataWrapper);
		nose_9_r2.setPos(6.673F, 0.2334F, -27.9004F);
		nose_side_2.addChild(nose_9_r2);
		setRotationAngle(nose_9_r2, 0, -0.2618F, 0);
		nose_9_r2.texOffs(29, 115).addBox(-2, 0, -1, 4, 0, 3, 0, false);

		nose_8_r2 = new ModelMapper(modelDataWrapper);
		nose_8_r2.setPos(19.057F, -45.3893F, -39.3447F);
		nose_side_2.addChild(nose_8_r2);
		setRotationAngle(nose_8_r2, 0.2618F, -0.2618F, 0);
		nose_8_r2.texOffs(24, 118).addBox(-11, 47.5F, -1, 4, 0, 2, 0, false);

		nose_7_r2 = new ModelMapper(modelDataWrapper);
		nose_7_r2.setPos(21.9558F, -45.3893F, -36.0248F);
		nose_side_2.addChild(nose_7_r2);
		setRotationAngle(nose_7_r2, 0.2618F, -0.5236F, 0);
		nose_7_r2.texOffs(24, 118).addBox(-8, 47.5F, -1, 4, 0, 2, 0, false);

		nose_6_r2 = new ModelMapper(modelDataWrapper);
		nose_6_r2.setPos(23.6661F, -45.3893F, -33.3962F);
		nose_side_2.addChild(nose_6_r2);
		setRotationAngle(nose_6_r2, 0.2618F, -0.7854F, 0);
		nose_6_r2.texOffs(0, 0).addBox(-4, 47.5F, -1, 3, 0, 2, 0, false);

		nose_5_r3 = new ModelMapper(modelDataWrapper);
		nose_5_r3.setPos(14.9491F, -46.75F, -24.6792F);
		nose_side_2.addChild(nose_5_r3);
		setRotationAngle(nose_5_r3, 0, -0.7854F, 0);
		nose_5_r3.texOffs(25, 94).addBox(-4, 47.5F, -1, 3, 3, 0, 0, true);

		nose_4_r3 = new ModelMapper(modelDataWrapper);
		nose_4_r3.setPos(-9.1985F, 2.25F, -12.0641F);
		nose_side_2.addChild(nose_4_r3);
		setRotationAngle(nose_4_r3, 0, -0.5236F, 0);
		nose_4_r3.texOffs(124, 43).addBox(7, -1.5F, -25, 4, 3, 0, 0, true);

		nose_3_r2 = new ModelMapper(modelDataWrapper);
		nose_3_r2.setPos(5.2412F, 2.25F, -30.2841F);
		nose_side_2.addChild(nose_3_r2);
		setRotationAngle(nose_3_r2, 0, -0.2618F, 0);
		nose_3_r2.texOffs(124, 40).addBox(0, -1.5F, -1, 4, 3, 0, 0, false);

		roof_head_exterior = new ModelMapper(modelDataWrapper);
		roof_head_exterior.setPos(0, 24, 0);
		roof_head_exterior.texOffs(0, 33).addBox(-8, -43, 0, 16, 2, 48, 0, false);

		vent_3_r1 = new ModelMapper(modelDataWrapper);
		vent_3_r1.setPos(-8, -43, 0);
		roof_head_exterior.addChild(vent_3_r1);
		setRotationAngle(vent_3_r1, 0, 0, -0.3491F);
		vent_3_r1.texOffs(131, 148).addBox(-9, 0, 0, 9, 2, 48, 0, false);

		vent_2_r2 = new ModelMapper(modelDataWrapper);
		vent_2_r2.setPos(8, -43, 0);
		roof_head_exterior.addChild(vent_2_r2);
		setRotationAngle(vent_2_r2, 0, 0, 0.3491F);
		vent_2_r2.texOffs(131, 148).addBox(0, 0, 0, 9, 2, 48, 0, true);

		outer_roof_3 = new ModelMapper(modelDataWrapper);
		outer_roof_3.setPos(0, 0, 0);
		roof_head_exterior.addChild(outer_roof_3);
		outer_roof_3.texOffs(196, 260).addBox(-6, -42, -12, 6, 1, 20, 0, false);

		outer_roof_6_r2 = new ModelMapper(modelDataWrapper);
		outer_roof_6_r2.setPos(-9.7656F, -40.3206F, 0);
		outer_roof_3.addChild(outer_roof_6_r2);
		setRotationAngle(outer_roof_6_r2, 0, 0, -0.1745F);
		outer_roof_6_r2.texOffs(240, 103).addBox(-4, -1, -12, 8, 1, 20, 0, false);

		outer_roof_5_r5 = new ModelMapper(modelDataWrapper);
		outer_roof_5_r5.setPos(-14.6775F, -38.9948F, 0);
		outer_roof_3.addChild(outer_roof_5_r5);
		setRotationAngle(outer_roof_5_r5, 0, 0, -0.5236F);
		outer_roof_5_r5.texOffs(228, 261).addBox(-1.5F, -1, -12, 3, 1, 20, 0, false);

		outer_roof_4_r5 = new ModelMapper(modelDataWrapper);
		outer_roof_4_r5.setPos(-16.1105F, -37.7448F, 0);
		outer_roof_3.addChild(outer_roof_4_r5);
		setRotationAngle(outer_roof_4_r5, 0, 0, -1.0472F);
		outer_roof_4_r5.texOffs(72, 283).addBox(-1, -1, -12, 2, 1, 20, 0, false);

		outer_roof_3_r5 = new ModelMapper(modelDataWrapper);
		outer_roof_3_r5.setPos(-17.587F, -36.3849F, 0);
		outer_roof_3.addChild(outer_roof_3_r5);
		setRotationAngle(outer_roof_3_r5, 0, 0, 0.1107F);
		outer_roof_3_r5.texOffs(190, 76).addBox(0, -1, -12, 1, 2, 20, 0, false);

		outer_roof_2_r5 = new ModelMapper(modelDataWrapper);
		outer_roof_2_r5.setPos(-20, -14, 0);
		outer_roof_3.addChild(outer_roof_2_r5);
		setRotationAngle(outer_roof_2_r5, 0, 0, 0.1107F);
		outer_roof_2_r5.texOffs(254, 279).addBox(-1, -22, -12, 1, 4, 20, 0, false);

		outer_roof_4 = new ModelMapper(modelDataWrapper);
		outer_roof_4.setPos(0, 0, 0);
		roof_head_exterior.addChild(outer_roof_4);
		outer_roof_4.texOffs(196, 260).addBox(0, -42, -12, 6, 1, 20, 0, false);

		outer_roof_6_r3 = new ModelMapper(modelDataWrapper);
		outer_roof_6_r3.setPos(9.7656F, -40.3206F, 0);
		outer_roof_4.addChild(outer_roof_6_r3);
		setRotationAngle(outer_roof_6_r3, 0, 0, 0.1745F);
		outer_roof_6_r3.texOffs(240, 103).addBox(-4, -1, -12, 8, 1, 20, 0, false);

		outer_roof_5_r6 = new ModelMapper(modelDataWrapper);
		outer_roof_5_r6.setPos(14.6775F, -38.9948F, 0);
		outer_roof_4.addChild(outer_roof_5_r6);
		setRotationAngle(outer_roof_5_r6, 0, 0, 0.5236F);
		outer_roof_5_r6.texOffs(228, 261).addBox(-1.5F, -1, -12, 3, 1, 20, 0, true);

		outer_roof_4_r6 = new ModelMapper(modelDataWrapper);
		outer_roof_4_r6.setPos(16.1105F, -37.7448F, 0);
		outer_roof_4.addChild(outer_roof_4_r6);
		setRotationAngle(outer_roof_4_r6, 0, 0, 1.0472F);
		outer_roof_4_r6.texOffs(72, 283).addBox(-1, -1, -12, 2, 1, 20, 0, true);

		outer_roof_3_r6 = new ModelMapper(modelDataWrapper);
		outer_roof_3_r6.setPos(17.587F, -36.3849F, 0);
		outer_roof_4.addChild(outer_roof_3_r6);
		setRotationAngle(outer_roof_3_r6, 0, 0, -0.1107F);
		outer_roof_3_r6.texOffs(190, 76).addBox(-1, -1, -12, 1, 2, 20, 0, true);

		outer_roof_2_r6 = new ModelMapper(modelDataWrapper);
		outer_roof_2_r6.setPos(20, -14, 0);
		outer_roof_4.addChild(outer_roof_2_r6);
		setRotationAngle(outer_roof_2_r6, 0, 0, -0.1107F);
		outer_roof_2_r6.texOffs(254, 279).addBox(0, -22, -12, 1, 4, 20, 0, true);

		headlights = new ModelMapper(modelDataWrapper);
		headlights.setPos(0, 24, 0);


		headlight_2_r1 = new ModelMapper(modelDataWrapper);
		headlight_2_r1.setPos(-1, -3.4772F, 1.663F);
		headlights.addChild(headlight_2_r1);
		setRotationAngle(headlight_2_r1, -0.0436F, 0, 0);
		headlight_2_r1.texOffs(124, 36).addBox(13.75F, -3, -28.013F, 4, 4, 0, 0, true);
		headlight_2_r1.texOffs(124, 36).addBox(-15.75F, -3, -28.013F, 4, 4, 0, 0, false);

		tail_lights = new ModelMapper(modelDataWrapper);
		tail_lights.setPos(0, 24, 0);


		headlight_4_r1 = new ModelMapper(modelDataWrapper);
		headlight_4_r1.setPos(-4.8579F, 1.25F, 0.7838F);
		tail_lights.addChild(headlight_4_r1);
		setRotationAngle(headlight_4_r1, 0, -0.7854F, 0);
		headlight_4_r1.texOffs(5, 44).addBox(-8, -2, -33.013F, 2, 3, 0, 0, true);

		headlight_3_r1 = new ModelMapper(modelDataWrapper);
		headlight_3_r1.setPos(-11.4729F, 1.25F, -4.1246F);
		tail_lights.addChild(headlight_3_r1);
		setRotationAngle(headlight_3_r1, 0, -0.5236F, 0);
		headlight_3_r1.texOffs(9, 44).addBox(5, -2, -33.0154F, 4, 3, 0, 0, true);

		headlight_2_r2 = new ModelMapper(modelDataWrapper);
		headlight_2_r2.setPos(7.2409F, 1.75F, -7.4547F);
		tail_lights.addChild(headlight_2_r2);
		setRotationAngle(headlight_2_r2, 0, 0.5236F, 0);
		headlight_2_r2.texOffs(9, 44).addBox(-7, -2.5F, -28.0153F, 4, 3, 0, 0, false);

		headlight_1_r1 = new ModelMapper(modelDataWrapper);
		headlight_1_r1.setPos(11.929F, 1.75F, -13.3582F);
		tail_lights.addChild(headlight_1_r1);
		setRotationAngle(headlight_1_r1, 0, 0.7854F, 0);
		headlight_1_r1.texOffs(5, 44).addBox(-9, -2.5F, -28.013F, 2, 3, 0, 0, false);

		door_light = new ModelMapper(modelDataWrapper);
		door_light.setPos(0, 24, 0);


		outer_roof_1_r5 = new ModelMapper(modelDataWrapper);
		outer_roof_1_r5.setPos(-20, -14, 0);
		door_light.addChild(outer_roof_1_r5);
		setRotationAngle(outer_roof_1_r5, 0, 0, 0.1107F);
		outer_roof_1_r5.texOffs(0, 0).addBox(-1.1F, -22, -2, 0, 4, 4, 0, false);

		door_light_on = new ModelMapper(modelDataWrapper);
		door_light_on.setPos(0, 24, 0);


		light_r1 = new ModelMapper(modelDataWrapper);
		light_r1.setPos(-20, -14, 0);
		door_light_on.addChild(light_r1);
		setRotationAngle(light_r1, 0, 0, 0.1107F);
		light_r1.texOffs(204, 0).addBox(-1, -20, 0, 0, 0, 0, 0.4F, false);

		door_light_off = new ModelMapper(modelDataWrapper);
		door_light_off.setPos(0, 24, 0);


		light_r2 = new ModelMapper(modelDataWrapper);
		light_r2.setPos(-20, -14, 0);
		door_light_off.addChild(light_r2);
		setRotationAngle(light_r2, 0, 0, 0.1107F);
		light_r2.texOffs(204, 3).addBox(-1, -20, 0, 0, 0, 0, 0.4F, false);

		side_panel_1 = new ModelMapper(modelDataWrapper);
		side_panel_1.setPos(0, 24, 0);
		side_panel_1.texOffs(218, 38).addBox(-18, -34, 0, 7, 30, 0, 0, false);

		handrail_r1 = new ModelMapper(modelDataWrapper);
		handrail_r1.setPos(-11, -5, 22);
		side_panel_1.addChild(handrail_r1);
		setRotationAngle(handrail_r1, 0, 0, -0.0436F);
		handrail_r1.texOffs(355, 0).addBox(0, -28.2F, -22, 0, 28, 0, 0.2F, false);

		side_panel_2 = new ModelMapper(modelDataWrapper);
		side_panel_2.setPos(0, 24, 0);
		side_panel_2.texOffs(0, 83).addBox(-18, -15, 0, 7, 11, 0, 0, false);

		side_panel_translucent = new ModelMapper(modelDataWrapper);
		side_panel_translucent.setPos(0, 24, 0);
		side_panel_translucent.texOffs(36, 146).addBox(-18, -34, 0, 7, 30, 0, 0, false);

		seat_1 = new ModelMapper(modelDataWrapper);
		seat_1.setPos(0, 24, 0);
		seat_1.texOffs(0, 146).addBox(-18, -6, -40, 7, 1, 51, 0, false);
		seat_1.texOffs(80, 198).addBox(-18, -6, 29, 7, 1, 31, 0, false);
		seat_1.texOffs(134, 0).addBox(-18, -5, -39, 6, 5, 98, 0, false);

		seat_back_2_r1 = new ModelMapper(modelDataWrapper);
		seat_back_2_r1.setPos(-17, -6, 0);
		seat_1.addChild(seat_back_2_r1);
		setRotationAngle(seat_back_2_r1, 0, 0, -0.0524F);
		seat_back_2_r1.texOffs(255, 240).addBox(-1, -8, 29, 1, 8, 31, 0, false);
		seat_back_2_r1.texOffs(194, 147).addBox(-1, -8, -40, 1, 8, 51, 0, false);

		seat_2 = new ModelMapper(modelDataWrapper);
		seat_2.setPos(0, 24, 0);
		seat_2.texOffs(80, 198).addBox(-18, -6, -60, 7, 1, 31, 0, false);
		seat_2.texOffs(0, 146).addBox(-18, -6, -11, 7, 1, 51, 0, false);
		seat_2.texOffs(134, 0).addBox(-18, -5, -59, 6, 5, 98, 0, false);

		seat_back_3_r1 = new ModelMapper(modelDataWrapper);
		seat_back_3_r1.setPos(-17, -6, 0);
		seat_2.addChild(seat_back_3_r1);
		setRotationAngle(seat_back_3_r1, 0, 0, -0.0524F);
		seat_back_3_r1.texOffs(194, 147).addBox(-1, -8, -11, 1, 8, 51, 0, false);
		seat_back_3_r1.texOffs(255, 240).addBox(-1, -8, -60, 1, 8, 31, 0, false);

		seat_curve = new ModelMapper(modelDataWrapper);
		seat_curve.setPos(0, 24, 0);
		seat_curve.texOffs(94, 124).addBox(-12.9289F, -5, -3.6538F, 8, 0, 8, 0, false);

		seat_panel_2_r1 = new ModelMapper(modelDataWrapper);
		seat_panel_2_r1.setPos(-4.9289F, -9.4367F, -0.9289F);
		seat_curve.addChild(seat_panel_2_r1);
		setRotationAngle(seat_panel_2_r1, 0, -1.5708F, 0);
		seat_panel_2_r1.texOffs(134, 114).addBox(1.0003F, -4.5633F, -0.0001F, 9, 9, 0, 0, false);
		seat_panel_2_r1.texOffs(134, 114).addBox(-7.9997F, -4.5633F, -0.0001F, 9, 9, 0, 0, true);

		statue_box = new ModelMapper(modelDataWrapper);
		statue_box.setPos(0, 0, 0);
		seat_curve.addChild(statue_box);
		statue_box.texOffs(145, 142).addBox(-13, -32, -2, 0, 18, 4, 0, false);

		statue_box_3_r1 = new ModelMapper(modelDataWrapper);
		statue_box_3_r1.setPos(-16.3807F, -23, 2.9059F);
		statue_box.addChild(statue_box_3_r1);
		setRotationAngle(statue_box_3_r1, 0, 0.2618F, 0);
		statue_box_3_r1.texOffs(212, 76).addBox(-2.5F, -9, 0, 6, 18, 0, 0, false);

		statue_box_1_r1 = new ModelMapper(modelDataWrapper);
		statue_box_1_r1.setPos(-15.4148F, -23, -2.647F);
		statue_box.addChild(statue_box_1_r1);
		setRotationAngle(statue_box_1_r1, 0, -0.2618F, 0);
		statue_box_1_r1.texOffs(212, 76).addBox(-3.5F, -9, 0, 6, 18, 0, 0, false);

		seat_side_1 = new ModelMapper(modelDataWrapper);
		seat_side_1.setPos(0, 0, 0);
		seat_curve.addChild(seat_side_1);
		seat_side_1.texOffs(22, 72).addBox(-10.9289F, -13.9357F, -0.9289F, 6, 0, 1, 0, false);

		seat_back_6_r1 = new ModelMapper(modelDataWrapper);
		seat_back_6_r1.setPos(-7.9289F, -9.9684F, -1.6376F);
		seat_side_1.addChild(seat_back_6_r1);
		setRotationAngle(seat_back_6_r1, -0.0524F, 0, 0);
		seat_back_6_r1.texOffs(0, 169).addBox(-3, -4, -0.5F, 6, 8, 1, 0, true);

		seat_top_5_r1 = new ModelMapper(modelDataWrapper);
		seat_top_5_r1.setPos(-10.2218F, -12.9367F, -5.4645F);
		seat_side_1.addChild(seat_top_5_r1);
		setRotationAngle(seat_top_5_r1, 0, 0.7854F, 0);
		seat_top_5_r1.texOffs(18, 24).addBox(-5, -1, -8, 2, 0, 10, 0, false);

		seat_top_7_r1 = new ModelMapper(modelDataWrapper);
		seat_top_7_r1.setPos(-14.9289F, -13.9367F, -3.9289F);
		seat_side_1.addChild(seat_top_7_r1);
		setRotationAngle(seat_top_7_r1, 0, 1.5708F, 0);
		seat_top_7_r1.texOffs(0, 0).addBox(-4, 0, -4, 8, 0, 8, 0, false);

		seat_back_3_r2 = new ModelMapper(modelDataWrapper);
		seat_back_3_r2.setPos(-14.1109F, -6, -5.818F);
		seat_side_1.addChild(seat_back_3_r2);
		setRotationAngle(seat_back_3_r2, 0, 0.7854F, 0);
		seat_back_3_r2.texOffs(0, 217).addBox(-0.5F, -8, -5, 1, 8, 11, 0, false);

		seat_bottom_4_r1 = new ModelMapper(modelDataWrapper);
		seat_bottom_4_r1.setPos(-12.6967F, -5.5F, -7.2322F);
		seat_side_1.addChild(seat_bottom_4_r1);
		setRotationAngle(seat_bottom_4_r1, 0, 0.7854F, 0);
		seat_bottom_4_r1.texOffs(131, 182).addBox(-2.5F, -0.6F, -5, 6, 1, 10, 0, false);

		seat_bottom_5_r1 = new ModelMapper(modelDataWrapper);
		seat_bottom_5_r1.setPos(-7.9289F, -5.5F, -4.8462F);
		seat_side_1.addChild(seat_bottom_5_r1);
		setRotationAngle(seat_bottom_5_r1, 0, 1.5708F, 0);
		seat_bottom_5_r1.texOffs(153, 182).addBox(-3, -0.5F, -4, 6, 1, 7, 0, false);

		seat_side_2 = new ModelMapper(modelDataWrapper);
		seat_side_2.setPos(0, 0, 0);
		seat_curve.addChild(seat_side_2);
		seat_side_2.texOffs(22, 72).addBox(-10.9289F, -13.9357F, -0.0711F, 6, 0, 1, 0, false);

		seat_top_8_r1 = new ModelMapper(modelDataWrapper);
		seat_top_8_r1.setPos(-14.9289F, -13.9367F, 4.0711F);
		seat_side_2.addChild(seat_top_8_r1);
		setRotationAngle(seat_top_8_r1, 0, 1.5708F, 0);
		seat_top_8_r1.texOffs(0, 0).addBox(-4, 0, -4, 8, 0, 8, 0, true);

		seat_top_6_r1 = new ModelMapper(modelDataWrapper);
		seat_top_6_r1.setPos(-3.1508F, -12.9367F, -1.4645F);
		seat_side_2.addChild(seat_top_6_r1);
		setRotationAngle(seat_top_6_r1, 0, -0.7854F, 0);
		seat_top_6_r1.texOffs(18, 24).addBox(-5, -1, 8, 2, 0, 10, 0, false);

		seat_back_5_r1 = new ModelMapper(modelDataWrapper);
		seat_back_5_r1.setPos(-7.9289F, -9.9684F, 1.6376F);
		seat_side_2.addChild(seat_back_5_r1);
		setRotationAngle(seat_back_5_r1, -0.0524F, 3.1416F, 0);
		seat_back_5_r1.texOffs(0, 169).addBox(-3, -4, -0.5F, 6, 8, 1, 0, false);

		seat_bottom_6_r1 = new ModelMapper(modelDataWrapper);
		seat_bottom_6_r1.setPos(-10.4289F, -5.5F, 5.3462F);
		seat_side_2.addChild(seat_bottom_6_r1);
		setRotationAngle(seat_bottom_6_r1, 0, 1.5708F, 0);
		seat_bottom_6_r1.texOffs(153, 182).addBox(-3, -0.5F, -1.5F, 6, 1, 7, 0, true);

		seat_back_4_r1 = new ModelMapper(modelDataWrapper);
		seat_back_4_r1.setPos(-30.0208F, -9, 21.7279F);
		seat_side_2.addChild(seat_back_4_r1);
		setRotationAngle(seat_back_4_r1, 0, -0.7854F, 0);
		seat_back_4_r1.texOffs(0, 217).addBox(-0.5F, -5, -28.5F, 1, 8, 11, 0, false);

		seat_bottom_3_r1 = new ModelMapper(modelDataWrapper);
		seat_bottom_3_r1.setPos(-12.6967F, -5.5F, 7.2322F);
		seat_side_2.addChild(seat_bottom_3_r1);
		setRotationAngle(seat_bottom_3_r1, 0, -0.7854F, 0);
		seat_bottom_3_r1.texOffs(131, 182).addBox(-2.5F, -0.6F, -5, 6, 1, 10, 0, false);

		window_edge = new ModelMapper(modelDataWrapper);
		window_edge.setPos(0, 24, 0);


		edge_side_1 = new ModelMapper(modelDataWrapper);
		edge_side_1.setPos(0, 0, 0);
		window_edge.addChild(edge_side_1);


		window_edge_2_r1 = new ModelMapper(modelDataWrapper);
		window_edge_2_r1.setPos(41.6208F, -7.1535F, 64);
		edge_side_1.addChild(window_edge_2_r1);
		setRotationAngle(window_edge_2_r1, 0, -1.5708F, 0.1107F);
		window_edge_2_r1.texOffs(65, 146).addBox(-4, -19, 60, 6, 19, 2, 0, false);

		window_edge_1_r1 = new ModelMapper(modelDataWrapper);
		window_edge_1_r1.setPos(-17, -7, 65);
		edge_side_1.addChild(window_edge_1_r1);
		setRotationAngle(window_edge_1_r1, 0, -1.5708F, 0);
		window_edge_1_r1.texOffs(154, 32).addBox(-5, -7, 1, 6, 14, 2, 0, false);

		edge_side_2 = new ModelMapper(modelDataWrapper);
		edge_side_2.setPos(0, 0, 6);
		window_edge.addChild(edge_side_2);


		window_edge_3_r1 = new ModelMapper(modelDataWrapper);
		window_edge_3_r1.setPos(41.6208F, -7.1535F, -68);
		edge_side_2.addChild(window_edge_3_r1);
		setRotationAngle(window_edge_3_r1, 0, -1.5708F, 0.1107F);
		window_edge_3_r1.texOffs(65, 146).addBox(-4, -19, 60, 6, 19, 2, 0, true);

		window_edge_2_r2 = new ModelMapper(modelDataWrapper);
		window_edge_2_r2.setPos(-17, -7, -67);
		edge_side_2.addChild(window_edge_2_r2);
		setRotationAngle(window_edge_2_r2, 0, -1.5708F, 0);
		window_edge_2_r2.texOffs(154, 32).addBox(-5, -7, 1, 6, 14, 2, 0, true);

		statue_box_translucent = new ModelMapper(modelDataWrapper);
		statue_box_translucent.setPos(0, 24, 0);
		statue_box_translucent.texOffs(351, 78).addBox(-13, -32, -2, 0, 18, 4, 0, false);

		statue_box_translucent_3_r1 = new ModelMapper(modelDataWrapper);
		statue_box_translucent_3_r1.setPos(-16.3807F, -23, 2.9059F);
		statue_box_translucent.addChild(statue_box_translucent_3_r1);
		setRotationAngle(statue_box_translucent_3_r1, 0, 0.2618F, 0);
		statue_box_translucent_3_r1.texOffs(349, 57).addBox(-1.5F, -9, 0, 5, 18, 0, 0, false);

		statue_box_translucent_1_r1 = new ModelMapper(modelDataWrapper);
		statue_box_translucent_1_r1.setPos(-15.4148F, -23, -2.647F);
		statue_box_translucent.addChild(statue_box_translucent_1_r1);
		setRotationAngle(statue_box_translucent_1_r1, 0, -0.2618F, 0);
		statue_box_translucent_1_r1.texOffs(349, 57).addBox(-2.5F, -9, 0, 5, 18, 0, 0, false);

		modelDataWrapper.setModelPart(textureWidth, textureHeight);
		window.setModelPart();
		window_handrails_1.setModelPart();
		window_handrails_2.setModelPart();
		window_exterior.setModelPart();
		roof_window_1.setModelPart();
		roof_window_2.setModelPart();
		roof_window_3.setModelPart();
		roof_window_light.setModelPart();
		side_panel_1.setModelPart();
		side_panel_2.setModelPart();
		side_panel_translucent.setModelPart();
		roof_door.setModelPart();
		roof_exterior.setModelPart();
		door.setModelPart();
		door_left.setModelPart(door.name);
		door_right.setModelPart(door.name);
		door_handrail_1.setModelPart();
		door_handrail_2.setModelPart();
		door_exterior.setModelPart();
		door_left_exterior.setModelPart(door_exterior.name);
		door_right_exterior.setModelPart(door_exterior.name);
		end.setModelPart();
		end_exterior.setModelPart();
		roof_end.setModelPart();
		roof_end_exterior.setModelPart();
		roof_head_exterior.setModelPart();
		roof_light.setModelPart();
		roof_end_light.setModelPart();
		head.setModelPart();
		head_exterior.setModelPart();
		headlights.setModelPart();
		tail_lights.setModelPart();
		door_light.setModelPart();
		door_light_on.setModelPart();
		door_light_off.setModelPart();
		seat_1.setModelPart();
		seat_2.setModelPart();
		window_edge.setModelPart();
		seat_curve.setModelPart();
		statue_box_translucent.setModelPart();
	}

	private static final int DOOR_MAX = 14;
	private static final ModelDoorOverlay MODEL_DOOR_OVERLAY = new ModelDoorOverlay(DOOR_MAX, 6.34F, "door_overlay_drl_left.png", "door_overlay_drl_right.png");

	@Override
	public ModelDRL createNew(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		return new ModelDRL(doorAnimationType, renderDoorOverlay);
	}

	@Override
	protected void renderWindowPositions(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
		final boolean isEvenWindow = isEvenWindow(position);
		switch (renderStage) {
			case LIGHTS:
				renderMirror(roof_light, matrices, vertices, light, position);
				renderMirror(roof_light, matrices, vertices, light, position + 40);
				renderMirror(roof_light, matrices, vertices, light, position - 40);
				renderMirror(roof_window_light, matrices, vertices, light, position);
				renderMirror(roof_window_light, matrices, vertices, light, position + 40);
				renderMirror(roof_window_light, matrices, vertices, light, position - 40);
				break;
			case INTERIOR:
				renderMirror(window, matrices, vertices, light, position);
				renderMirror(window_edge, matrices, vertices, light, position);
				renderMirror(window, matrices, vertices, light, position + 40);
				renderMirror(window, matrices, vertices, light, position - 40);
				if (renderDetails) {
					renderMirror(roof_window_2, matrices, vertices, light, position);
					renderOnce(roof_window_1, matrices, vertices, light, position - 40);
					renderOnceFlipped(roof_window_1, matrices, vertices, light, position + 40);
					renderOnce(roof_window_3, matrices, vertices, light, position + 40);
					renderOnceFlipped(roof_window_3, matrices, vertices, light, position - 40);
					renderMirror(side_panel_2, matrices, vertices, light, position + (isEvenWindow ? -40 : 40));
					renderMirror(side_panel_1, matrices, vertices, light, position + (isEvenWindow ? 60 : -60));
					renderMirror(seat_curve, matrices, vertices, light, position + (isEvenWindow ? 20 : -20));
					if (isEvenWindow) {
						renderOnce(seat_1, matrices, vertices, light, position);
						renderOnceFlipped(seat_2, matrices, vertices, light, position);
						renderOnce(window_handrails_2, matrices, vertices, light, position);
					} else {
						renderOnceFlipped(seat_1, matrices, vertices, light, position);
						renderOnce(seat_2, matrices, vertices, light, position);
						renderOnce(window_handrails_1, matrices, vertices, light, position);
					}
				}
				break;
			case INTERIOR_TRANSLUCENT:
				renderMirror(side_panel_translucent, matrices, vertices, light, position + (isEvenWindow ? 60 : -60));
				renderMirror(statue_box_translucent, matrices, vertices, light, position + (isEvenWindow ? 20 : -20));
				break;
			case EXTERIOR:
				renderMirror(roof_exterior, matrices, vertices, light, position);
				renderMirror(roof_exterior, matrices, vertices, light, position - 40);
				renderMirror(roof_exterior, matrices, vertices, light, position + 40);
				renderMirror(window_exterior, matrices, vertices, light, position);
				break;
		}
	}

	@Override
	protected void renderDoorPositions(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
		final boolean middleDoor = isIndex(getDoorPositions().length / 2, position, getDoorPositions());
		final boolean doorOpen = doorLeftZ > 0 || doorRightZ > 0;
		final boolean notLastDoor = !isIndex(0, position, getDoorPositions()) && !isIndex(-1, position, getDoorPositions());

		switch (renderStage) {
			case LIGHTS:
				if (notLastDoor) {
					renderMirror(roof_light, matrices, vertices, light, position);
				}
				if (middleDoor && doorOpen) {
					renderMirror(door_light_on, matrices, vertices, light, position - 30);
				}
				break;
			case INTERIOR:
				door_left.setOffset(0, 0, doorRightZ);
				door_right.setOffset(0, 0, -doorRightZ);
				renderOnce(door, matrices, vertices, light, position);
				door_left.setOffset(0, 0, doorLeftZ);
				door_right.setOffset(0, 0, -doorLeftZ);
				renderOnceFlipped(door, matrices, vertices, light, position);

				if (renderDetails) {
					if (notLastDoor) {
						renderOnce(roof_door, matrices, vertices, light, position);
						renderOnceFlipped(roof_door, matrices, vertices, light, position);
						renderOnce(door_handrail_1, matrices, vertices, light, position);
					} else {
						renderOnce(door_handrail_2, matrices, vertices, light, position);
					}
				}
				break;
			case EXTERIOR:
				door_left_exterior.setOffset(0, 0, doorRightZ);
				door_right_exterior.setOffset(0, 0, -doorRightZ);
				renderOnce(door_exterior, matrices, vertices, light, position);
				door_left_exterior.setOffset(0, 0, doorLeftZ);
				door_right_exterior.setOffset(0, 0, -doorLeftZ);
				renderOnceFlipped(door_exterior, matrices, vertices, light, position);
				renderMirror(roof_exterior, matrices, vertices, light, position);
				if (middleDoor && renderDetails) {
					renderMirror(door_light, matrices, vertices, light, position - 30);
					if (!doorOpen) {
						renderMirror(door_light_off, matrices, vertices, light, position - 30);
					}
				}
				break;
		}
	}

	@Override
	protected void renderHeadPosition1(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean useHeadlights) {
		switch (renderStage) {
			case LIGHTS:
				renderOnce(roof_end_light, matrices, vertices, light, position);
				break;
			case ALWAYS_ON_LIGHTS:
				renderOnce(useHeadlights ? headlights : tail_lights, matrices, vertices, light, position);
				break;
			case INTERIOR:
				renderOnce(head, matrices, vertices, light, position);
				if (renderDetails) {
					renderOnce(roof_end, matrices, vertices, light, position);
				}
				break;
			case EXTERIOR:
				renderOnce(head_exterior, matrices, vertices, light, position);
				renderOnce(roof_head_exterior, matrices, vertices, light, position);
				break;
		}
	}

	@Override
	protected void renderHeadPosition2(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean useHeadlights) {
		switch (renderStage) {
			case LIGHTS:
				renderOnceFlipped(roof_end_light, matrices, vertices, light, position);
				break;
			case ALWAYS_ON_LIGHTS:
				renderOnceFlipped(useHeadlights ? headlights : tail_lights, matrices, vertices, light, position);
				break;
			case INTERIOR:
				renderOnceFlipped(head, matrices, vertices, light, position);
				if (renderDetails) {
					renderOnceFlipped(roof_end, matrices, vertices, light, position);
				}
				break;
			case EXTERIOR:
				renderOnceFlipped(head_exterior, matrices, vertices, light, position);
				renderOnceFlipped(roof_head_exterior, matrices, vertices, light, position);
				break;
		}
	}

	@Override
	protected void renderEndPosition1(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
		switch (renderStage) {
			case LIGHTS:
				renderOnce(roof_end_light, matrices, vertices, light, position);
				break;
			case INTERIOR:
				renderOnce(end, matrices, vertices, light, position);
				if (renderDetails) {
					renderOnce(roof_end, matrices, vertices, light, position);
				}
				break;
			case EXTERIOR:
				renderOnce(end_exterior, matrices, vertices, light, position);
				renderOnce(roof_end_exterior, matrices, vertices, light, position);
				break;
		}
	}

	@Override
	protected void renderEndPosition2(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
		switch (renderStage) {
			case LIGHTS:
				renderOnceFlipped(roof_end_light, matrices, vertices, light, position);
				break;
			case INTERIOR:
				renderOnceFlipped(end, matrices, vertices, light, position);
				if (renderDetails) {
					renderOnceFlipped(roof_end, matrices, vertices, light, position);
				}
				break;
			case EXTERIOR:
				renderOnceFlipped(end_exterior, matrices, vertices, light, position);
				renderOnceFlipped(roof_end_exterior, matrices, vertices, light, position);
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