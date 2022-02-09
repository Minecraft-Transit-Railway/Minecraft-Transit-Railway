package mtr.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mtr.mappings.ModelDataWrapper;
import mtr.mappings.ModelMapper;

public class ModelSTrain extends ModelTrainBase {
	private final ModelMapper window;
	private final ModelMapper top_handrail_7_r1;
	private final ModelMapper top_handrail_5_r1;
	private final ModelMapper top_handrail_4_r1;
	private final ModelMapper top_handrail_3_r1;
	private final ModelMapper top_handrail_2_r1;
	private final ModelMapper top_handrail_1_r1;
	private final ModelMapper upper_wall_2_r1;
	private final ModelMapper seat;
	private final ModelMapper seat_back_r1;
	private final ModelMapper window_exterior_1;
	private final ModelMapper door_leaf_r1;
	private final ModelMapper upper_wall_r1;
	private final ModelMapper window_exterior_2;
	private final ModelMapper door_leaf_r2;
	private final ModelMapper upper_wall_r2;
	private final ModelMapper side_panel_translucent;
	private final ModelMapper roof_window;
	private final ModelMapper inner_roof_5_r1;
	private final ModelMapper inner_roof_4_r1;
	private final ModelMapper inner_roof_2_r1;
	private final ModelMapper roof_door;
	private final ModelMapper inner_roof_5_r2;
	private final ModelMapper inner_roof_4_r2;
	private final ModelMapper inner_roof_2_r2;
	private final ModelMapper roof_exterior;
	private final ModelMapper outer_roof_5_r1;
	private final ModelMapper outer_roof_4_r1;
	private final ModelMapper outer_roof_3_r1;
	private final ModelMapper door;
	private final ModelMapper door_left;
	private final ModelMapper door_left_top_r1;
	private final ModelMapper door_right;
	private final ModelMapper door_right_top_r1;
	private final ModelMapper door_handrail;
	private final ModelMapper pole_bottom_diagonal_1_r1;
	private final ModelMapper pole_bottom_diagonal_3_r1;
	private final ModelMapper pole_middle_3_r1;
	private final ModelMapper pole_top_diagonal_3_r1;
	private final ModelMapper pole_bottom_diagonal_2_r1;
	private final ModelMapper pole_middle_2_r1;
	private final ModelMapper pole_middle_1_r1;
	private final ModelMapper pole_top_diagonal_1_r1;
	private final ModelMapper pole_top_diagonal_2_r1;
	private final ModelMapper door_exterior_1;
	private final ModelMapper door_leaf_r3;
	private final ModelMapper door_left_exterior_1;
	private final ModelMapper door_left_top_r2;
	private final ModelMapper door_right_exterior_1;
	private final ModelMapper door_right_top_r2;
	private final ModelMapper door_exterior_2;
	private final ModelMapper door_leaf_r4;
	private final ModelMapper door_left_exterior_2;
	private final ModelMapper door_left_top_r3;
	private final ModelMapper door_right_exterior_2;
	private final ModelMapper door_right_top_r3;
	private final ModelMapper end;
	private final ModelMapper upper_wall_2_r2;
	private final ModelMapper upper_wall_1_r1;
	private final ModelMapper lower_wall_1_r1;
	private final ModelMapper end_exterior;
	private final ModelMapper door_leaf_2_r1;
	private final ModelMapper door_leaf_1_r1;
	private final ModelMapper upper_wall_2_r3;
	private final ModelMapper upper_wall_1_r2;
	private final ModelMapper roof_end;
	private final ModelMapper inner_roof_1;
	private final ModelMapper inner_roof_5_r3;
	private final ModelMapper inner_roof_4_r3;
	private final ModelMapper inner_roof_2_r3;
	private final ModelMapper inner_roof_2;
	private final ModelMapper inner_roof_6_r1;
	private final ModelMapper inner_roof_5_r4;
	private final ModelMapper inner_roof_3_r1;
	private final ModelMapper handrail_end;
	private final ModelMapper pole_bottom_diagonal_1_r2;
	private final ModelMapper pole_bottom_diagonal_3_r2;
	private final ModelMapper pole_middle_3_r2;
	private final ModelMapper pole_top_diagonal_3_r2;
	private final ModelMapper pole_bottom_diagonal_2_r2;
	private final ModelMapper pole_middle_2_r2;
	private final ModelMapper pole_middle_1_r2;
	private final ModelMapper pole_top_diagonal_1_r2;
	private final ModelMapper pole_top_diagonal_2_r2;
	private final ModelMapper roof_end_exterior;
	private final ModelMapper vent_2_r1;
	private final ModelMapper vent_1_r1;
	private final ModelMapper outer_roof_1;
	private final ModelMapper outer_roof_5_r2;
	private final ModelMapper outer_roof_4_r2;
	private final ModelMapper outer_roof_3_r2;
	private final ModelMapper outer_roof_2;
	private final ModelMapper outer_roof_5_r3;
	private final ModelMapper outer_roof_4_r3;
	private final ModelMapper outer_roof_3_r3;
	private final ModelMapper roof_window_light;
	private final ModelMapper light_2_r1;
	private final ModelMapper light_1_r1;
	private final ModelMapper roof_door_light;
	private final ModelMapper light_2_r2;
	private final ModelMapper light_1_r2;
	private final ModelMapper roof_end_light;
	private final ModelMapper light_4_r1;
	private final ModelMapper light_3_r1;
	private final ModelMapper light_2_r3;
	private final ModelMapper light_1_r3;
	private final ModelMapper head;
	private final ModelMapper upper_wall_2_r4;
	private final ModelMapper upper_wall_1_r3;
	private final ModelMapper lower_wall_1_r2;
	private final ModelMapper ceiling;
	private final ModelMapper panel_9_r1;
	private final ModelMapper panel_8_r1;
	private final ModelMapper panel_7_r1;
	private final ModelMapper panel_6_r1;
	private final ModelMapper panel_4_r1;
	private final ModelMapper panel_3_r1;
	private final ModelMapper panel_2_r1;
	private final ModelMapper panel_1_r1;
	private final ModelMapper main_r1;
	private final ModelMapper emergency_door;
	private final ModelMapper upper_r1;
	private final ModelMapper left_c_panel;
	private final ModelMapper panel_r1;
	private final ModelMapper base_r1;
	private final ModelMapper right_c_panel;
	private final ModelMapper panel_r2;
	private final ModelMapper base_r2;
	private final ModelMapper handrail;
	private final ModelMapper wall;
	private final ModelMapper handrail_4_r1;
	private final ModelMapper handrail_3_r1;
	private final ModelMapper handrail_2_r1;
	private final ModelMapper handrail_5_r1;
	private final ModelMapper handrail_1_r1;
	private final ModelMapper ceiling2;
	private final ModelMapper handrail_7_r1;
	private final ModelMapper handrail_4_r2;
	private final ModelMapper handrail_5_r2;
	private final ModelMapper handrail_6_r1;
	private final ModelMapper handrail_3_r2;
	private final ModelMapper handrail_5_r3;
	private final ModelMapper handrail_4_r3;
	private final ModelMapper handrail_3_r3;
	private final ModelMapper handrail_6_r2;
	private final ModelMapper handrail_2_r2;
	private final ModelMapper head_exterior;
	private final ModelMapper upper_wall_2_r5;
	private final ModelMapper upper_wall_1_r4;
	private final ModelMapper door_leaf_left;
	private final ModelMapper door_leaf_2_r2;
	private final ModelMapper door_leaf_3_r1;
	private final ModelMapper door_leaf_5_r1;
	private final ModelMapper door_leaf_4_r1;
	private final ModelMapper door_leaf_1_r2;
	private final ModelMapper door_leaf_right;
	private final ModelMapper door_leaf_5_r2;
	private final ModelMapper door_leaf_4_r2;
	private final ModelMapper door_leaf_3_r2;
	private final ModelMapper door_leaf_2_r3;
	private final ModelMapper door_leaf_1_r3;
	private final ModelMapper front;
	private final ModelMapper front_panel_4_r1;
	private final ModelMapper front_panel_3_r1;
	private final ModelMapper front_panel_1_r1;
	private final ModelMapper side_1;
	private final ModelMapper front_side_bottom_3_r1;
	private final ModelMapper front_side_bottom_1_r1;
	private final ModelMapper front_side_lower_1_r1;
	private final ModelMapper front_side_upper_1_r1;
	private final ModelMapper side_2;
	private final ModelMapper front_side_upper_2_r1;
	private final ModelMapper front_side_lower_2_r1;
	private final ModelMapper front_side_bottom_4_r1;
	private final ModelMapper front_side_bottom_2_r1;
	private final ModelMapper roof;
	private final ModelMapper outer_roof_3_r4;
	private final ModelMapper outer_roof_5_r4;
	private final ModelMapper outer_roof_4_r4;
	private final ModelMapper outer_roof_4_r5;
	private final ModelMapper outer_roof_5_r5;
	private final ModelMapper outer_roof_3_r5;
	private final ModelMapper vent_top_r1;
	private final ModelMapper vent_2_r2;
	private final ModelMapper vent_1_r2;
	private final ModelMapper outer_roof_5_r6;
	private final ModelMapper outer_roof_6_r1;
	private final ModelMapper outer_roof_6_r2;
	private final ModelMapper outer_roof_7_r1;
	private final ModelMapper outer_roof_8_r1;
	private final ModelMapper outer_roof_7_r2;
	private final ModelMapper outer_roof_5_r7;
	private final ModelMapper outer_roof_6_r3;
	private final ModelMapper headlights;
	private final ModelMapper tail_lights;
	private final ModelMapper door_light_on;
	private final ModelMapper light_r1;
	private final ModelMapper door_light_off;
	private final ModelMapper light_r2;

	public ModelSTrain() {
		final int textureWidth = 320;
		final int textureHeight = 320;

		final ModelDataWrapper modelDataWrapper = new ModelDataWrapper(this, textureWidth, textureHeight);

		window = new ModelMapper(modelDataWrapper);
		window.setPos(0.0F, 24.0F, 0.0F);
		window.texOffs(0, 0).addBox(-20.0F, 0.0F, -24.0F, 20.0F, 1.0F, 48.0F, 0.0F, false);
		window.texOffs(124, 204).addBox(-20.0F, -14.0F, 21.0F, 3.0F, 14.0F, 6.0F, 0.0F, false);
		window.texOffs(130, 164).addBox(-20.0F, -14.0F, -27.0F, 3.0F, 14.0F, 6.0F, 0.0F, false);
		window.texOffs(319, 0).addBox(-11.0F, -34.2F, -22.0F, 0.0F, 29.0F, 0.0F, 0.2F, false);
		window.texOffs(319, 0).addBox(-11.0F, -34.2F, 22.0F, 0.0F, 29.0F, 0.0F, 0.2F, false);
		window.texOffs(319, 0).addBox(0.0F, -35.0F, -13.0F, 0.0F, 35.0F, 0.0F, 0.2F, false);
		window.texOffs(319, 0).addBox(0.0F, -35.0F, 13.0F, 0.0F, 35.0F, 0.0F, 0.2F, false);
		window.texOffs(0, 0).addBox(-8.0F, -32.7F, 18.0F, 2.0F, 4.0F, 0.0F, 0.0F, false);
		window.texOffs(0, 0).addBox(-8.0F, -32.7F, 12.0F, 2.0F, 4.0F, 0.0F, 0.0F, false);
		window.texOffs(0, 0).addBox(-8.0F, -32.7F, 6.0F, 2.0F, 4.0F, 0.0F, 0.0F, false);
		window.texOffs(0, 0).addBox(-8.0F, -32.7F, -6.0F, 2.0F, 4.0F, 0.0F, 0.0F, false);
		window.texOffs(0, 0).addBox(-8.0F, -32.7F, -12.0F, 2.0F, 4.0F, 0.0F, 0.0F, false);
		window.texOffs(0, 0).addBox(-8.0F, -32.7F, -18.0F, 2.0F, 4.0F, 0.0F, 0.0F, false);

		top_handrail_7_r1 = new ModelMapper(modelDataWrapper);
		top_handrail_7_r1.setPos(-6.9482F, -32.6105F, 9.5876F);
		window.addChild(top_handrail_7_r1);
		setRotationAngle(top_handrail_7_r1, 0.0F, 0.0F, -0.0436F);
		top_handrail_7_r1.texOffs(0, 0).addBox(0.0F, -2.3F, -1.0F, 0.0F, 2.0F, 0.0F, 0.2F, true);
		top_handrail_7_r1.texOffs(0, 0).addBox(0.0F, -2.3F, -18.1751F, 0.0F, 2.0F, 0.0F, 0.2F, true);

		top_handrail_5_r1 = new ModelMapper(modelDataWrapper);
		top_handrail_5_r1.setPos(-6.9482F, -32.6105F, -9.5876F);
		window.addChild(top_handrail_5_r1);
		setRotationAngle(top_handrail_5_r1, 1.5708F, 0.0F, -0.0436F);
		top_handrail_5_r1.texOffs(319, 20).addBox(0.0F, -10.5F, 0.0F, 0.0F, 40.0F, 0.0F, 0.2F, false);

		top_handrail_4_r1 = new ModelMapper(modelDataWrapper);
		top_handrail_4_r1.setPos(-7.0008F, -33.8156F, 21.4518F);
		window.addChild(top_handrail_4_r1);
		setRotationAngle(top_handrail_4_r1, -0.5236F, 0.0F, -0.0436F);
		top_handrail_4_r1.texOffs(319, 0).addBox(0.0F, -0.5F, 0.0F, 0.0F, 1.0F, 0.0F, 0.2F, true);

		top_handrail_3_r1 = new ModelMapper(modelDataWrapper);
		top_handrail_3_r1.setPos(-6.9623F, -32.9334F, 20.5688F);
		window.addChild(top_handrail_3_r1);
		setRotationAngle(top_handrail_3_r1, -1.0472F, 0.0F, -0.0436F);
		top_handrail_3_r1.texOffs(319, 0).addBox(0.0F, -0.5F, 0.0F, 0.0F, 1.0F, 0.0F, 0.2F, true);

		top_handrail_2_r1 = new ModelMapper(modelDataWrapper);
		top_handrail_2_r1.setPos(-6.9623F, -32.9334F, -20.7938F);
		window.addChild(top_handrail_2_r1);
		setRotationAngle(top_handrail_2_r1, 1.0472F, 0.0F, -0.0436F);
		top_handrail_2_r1.texOffs(319, 0).addBox(0.0F, -0.5F, 0.0F, 0.0F, 1.0F, 0.0F, 0.2F, true);

		top_handrail_1_r1 = new ModelMapper(modelDataWrapper);
		top_handrail_1_r1.setPos(-7.0008F, -33.8156F, -21.6768F);
		window.addChild(top_handrail_1_r1);
		setRotationAngle(top_handrail_1_r1, 0.5236F, 0.0F, -0.0436F);
		top_handrail_1_r1.texOffs(319, 0).addBox(0.0F, -0.5F, 0.0F, 0.0F, 1.0F, 0.0F, 0.2F, true);

		upper_wall_2_r1 = new ModelMapper(modelDataWrapper);
		upper_wall_2_r1.setPos(-21.0F, -14.0F, 0.0F);
		window.addChild(upper_wall_2_r1);
		setRotationAngle(upper_wall_2_r1, 0.0F, 0.0F, 0.1107F);
		upper_wall_2_r1.texOffs(74, 196).addBox(1.0F, -19.0F, -27.0F, 3.0F, 19.0F, 5.0F, 0.0F, false);
		upper_wall_2_r1.texOffs(245, 205).addBox(1.0F, -19.0F, 22.0F, 3.0F, 19.0F, 5.0F, 0.0F, false);
		upper_wall_2_r1.texOffs(106, 141).addBox(1.0F, -19.0F, -22.0F, 2.0F, 19.0F, 44.0F, 0.0F, false);

		seat = new ModelMapper(modelDataWrapper);
		seat.setPos(0.0F, 0.0F, 0.0F);
		window.addChild(seat);
		seat.texOffs(156, 162).addBox(-18.0F, -6.0F, -21.0F, 7.0F, 1.0F, 42.0F, 0.0F, false);
		seat.texOffs(16, 55).addBox(-18.0F, -11.0F, -22.0F, 7.0F, 6.0F, 1.0F, 0.0F, false);
		seat.texOffs(16, 55).addBox(-18.0F, -11.0F, 21.0F, 7.0F, 6.0F, 1.0F, 0.0F, false);
		seat.texOffs(80, 43).addBox(-18.0F, -5.0F, -21.0F, 0.0F, 5.0F, 42.0F, 0.0F, false);

		seat_back_r1 = new ModelMapper(modelDataWrapper);
		seat_back_r1.setPos(-17.0F, -6.0F, 0.0F);
		seat.addChild(seat_back_r1);
		setRotationAngle(seat_back_r1, 0.0F, 0.0F, -0.0524F);
		seat_back_r1.texOffs(146, 85).addBox(-1.0F, -8.0F, -22.0F, 1.0F, 8.0F, 44.0F, 0.0F, false);

		window_exterior_1 = new ModelMapper(modelDataWrapper);
		window_exterior_1.setPos(0.0F, 24.0F, 0.0F);
		window_exterior_1.texOffs(0, 69).addBox(-20.0F, -14.0F, -26.0F, 0.0F, 18.0F, 52.0F, 0.0F, false);

		door_leaf_r1 = new ModelMapper(modelDataWrapper);
		door_leaf_r1.setPos(0.0F, 0.0F, 0.0F);
		window_exterior_1.addChild(door_leaf_r1);
		setRotationAngle(door_leaf_r1, 0.0F, 0.0F, 0.1107F);
		door_leaf_r1.texOffs(0, 139).addBox(-22.5F, -34.0F, -26.0F, 1.0F, 5.0F, 52.0F, 0.0F, false);

		upper_wall_r1 = new ModelMapper(modelDataWrapper);
		upper_wall_r1.setPos(-21.0F, -14.0F, 0.0F);
		window_exterior_1.addChild(upper_wall_r1);
		setRotationAngle(upper_wall_r1, 0.0F, 0.0F, 0.1107F);
		upper_wall_r1.texOffs(0, 47).addBox(1.0F, -22.0F, -26.0F, 0.0F, 22.0F, 52.0F, 0.0F, false);

		window_exterior_2 = new ModelMapper(modelDataWrapper);
		window_exterior_2.setPos(0.0F, 24.0F, 0.0F);
		window_exterior_2.texOffs(0, 69).addBox(20.0F, -14.0F, -26.0F, 0.0F, 18.0F, 52.0F, 0.0F, true);

		door_leaf_r2 = new ModelMapper(modelDataWrapper);
		door_leaf_r2.setPos(0.0F, 0.0F, 0.0F);
		window_exterior_2.addChild(door_leaf_r2);
		setRotationAngle(door_leaf_r2, 0.0F, 0.0F, -0.1107F);
		door_leaf_r2.texOffs(0, 139).addBox(21.5F, -34.0F, -26.0F, 1.0F, 5.0F, 52.0F, 0.0F, true);

		upper_wall_r2 = new ModelMapper(modelDataWrapper);
		upper_wall_r2.setPos(21.0F, -14.0F, 0.0F);
		window_exterior_2.addChild(upper_wall_r2);
		setRotationAngle(upper_wall_r2, 0.0F, 0.0F, -0.1107F);
		upper_wall_r2.texOffs(0, 47).addBox(-1.0F, -22.0F, -26.0F, 0.0F, 22.0F, 52.0F, 0.0F, true);

		side_panel_translucent = new ModelMapper(modelDataWrapper);
		side_panel_translucent.setPos(0.0F, 24.0F, 0.0F);
		side_panel_translucent.texOffs(152, 90).addBox(-18.0F, -25.0F, 0.0F, 7.0F, 15.0F, 0.0F, 0.0F, false);

		roof_window = new ModelMapper(modelDataWrapper);
		roof_window.setPos(0.0F, 24.0F, 0.0F);
		roof_window.texOffs(60, 0).addBox(-16.0F, -32.0F, -24.0F, 3.0F, 0.0F, 48.0F, 0.0F, false);
		roof_window.texOffs(55, 0).addBox(-11.075F, -34.275F, -24.0F, 1.0F, 0.0F, 48.0F, 0.0F, false);
		roof_window.texOffs(40, 0).addBox(-3.975F, -34.725F, -24.0F, 4.0F, 0.0F, 48.0F, 0.0F, false);

		inner_roof_5_r1 = new ModelMapper(modelDataWrapper);
		inner_roof_5_r1.setPos(-12.0F, -32.0F, 0.0F);
		roof_window.addChild(inner_roof_5_r1);
		setRotationAngle(inner_roof_5_r1, 0.0F, 0.0F, -0.0873F);
		inner_roof_5_r1.texOffs(54, 0).addBox(6.075F, -1.95F, -24.0F, 3.0F, 0.0F, 48.0F, 0.0F, false);

		inner_roof_4_r1 = new ModelMapper(modelDataWrapper);
		inner_roof_4_r1.setPos(-12.0F, -32.0F, 0.0F);
		roof_window.addChild(inner_roof_4_r1);
		setRotationAngle(inner_roof_4_r1, 0.0F, 0.0F, -0.1745F);
		inner_roof_4_r1.texOffs(66, 0).addBox(4.3F, -1.411F, -24.0F, 2.0F, 0.0F, 48.0F, 0.0F, false);

		inner_roof_2_r1 = new ModelMapper(modelDataWrapper);
		inner_roof_2_r1.setPos(-12.0F, -32.0F, 0.0F);
		roof_window.addChild(inner_roof_2_r1);
		setRotationAngle(inner_roof_2_r1, 0.0F, 0.0F, -0.8727F);
		inner_roof_2_r1.texOffs(60, 0).addBox(-0.65F, -0.775F, -24.0F, 3.0F, 0.0F, 48.0F, 0.0F, false);

		roof_door = new ModelMapper(modelDataWrapper);
		roof_door.setPos(0.0F, 24.0F, 0.0F);
		roof_door.texOffs(204, 0).addBox(-18.0034F, -32.9779F, -16.0F, 5.0F, 1.0F, 32.0F, 0.0F, false);
		roof_door.texOffs(80, 16).addBox(-11.075F, -34.275F, -16.0F, 1.0F, 0.0F, 32.0F, 0.0F, false);
		roof_door.texOffs(56, 0).addBox(-3.975F, -34.725F, -16.0F, 4.0F, 0.0F, 32.0F, 0.0F, false);

		inner_roof_5_r2 = new ModelMapper(modelDataWrapper);
		inner_roof_5_r2.setPos(-12.0F, -32.0F, 0.0F);
		roof_door.addChild(inner_roof_5_r2);
		setRotationAngle(inner_roof_5_r2, 0.0F, 0.0F, -0.0873F);
		inner_roof_5_r2.texOffs(70, 0).addBox(6.075F, -1.95F, -16.0F, 3.0F, 0.0F, 32.0F, 0.0F, false);

		inner_roof_4_r2 = new ModelMapper(modelDataWrapper);
		inner_roof_4_r2.setPos(-12.0F, -32.0F, 0.0F);
		roof_door.addChild(inner_roof_4_r2);
		setRotationAngle(inner_roof_4_r2, 0.0F, 0.0F, -0.1745F);
		inner_roof_4_r2.texOffs(82, 0).addBox(4.3F, -1.411F, -16.0F, 2.0F, 0.0F, 32.0F, 0.0F, false);

		inner_roof_2_r2 = new ModelMapper(modelDataWrapper);
		inner_roof_2_r2.setPos(-11.9911F, -31.977F, 0.0F);
		roof_door.addChild(inner_roof_2_r2);
		setRotationAngle(inner_roof_2_r2, 0.0F, 0.0F, -0.8727F);
		inner_roof_2_r2.texOffs(114, 90).addBox(-0.65F, -0.775F, -16.0F, 3.0F, 0.0F, 32.0F, 0.0F, false);

		roof_exterior = new ModelMapper(modelDataWrapper);
		roof_exterior.setPos(0.0F, 24.0F, 0.0F);
		roof_exterior.texOffs(64, 99).addBox(-5.728F, -41.8527F, -20.0F, 6.0F, 0.0F, 40.0F, 0.0F, false);

		outer_roof_5_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_5_r1.setPos(-9.6672F, -41.1581F, 0.0F);
		roof_exterior.addChild(outer_roof_5_r1);
		setRotationAngle(outer_roof_5_r1, 0.0F, 0.0F, -0.1745F);
		outer_roof_5_r1.texOffs(98, 0).addBox(-4.0F, 0.0F, -20.0F, 8.0F, 0.0F, 40.0F, 0.0F, false);

		outer_roof_4_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_4_r1.setPos(-15.3385F, -39.4635F, 0.0F);
		roof_exterior.addChild(outer_roof_4_r1);
		setRotationAngle(outer_roof_4_r1, 0.0F, 0.0F, -0.5236F);
		outer_roof_4_r1.texOffs(0, 0).addBox(-2.0F, 0.0F, -20.0F, 4.0F, 0.0F, 40.0F, 0.0F, false);

		outer_roof_3_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_3_r1.setPos(-17.8206F, -37.1645F, 0.0F);
		roof_exterior.addChild(outer_roof_3_r1);
		setRotationAngle(outer_roof_3_r1, 0.0F, 0.0F, -1.0472F);
		outer_roof_3_r1.texOffs(0, 49).addBox(-1.5F, 0.0F, -20.0F, 3.0F, 0.0F, 40.0F, 0.0F, false);

		door = new ModelMapper(modelDataWrapper);
		door.setPos(0.0F, 24.0F, 0.0F);
		door.texOffs(178, 50).addBox(-20.0F, 0.0F, -16.0F, 20.0F, 1.0F, 32.0F, 0.0F, false);

		door_left = new ModelMapper(modelDataWrapper);
		door_left.setPos(0.0F, 0.0F, 0.0F);
		door.addChild(door_left);
		door_left.texOffs(94, 265).addBox(-21.0F, -14.0F, 0.0F, 1.0F, 14.0F, 14.0F, 0.0F, false);

		door_left_top_r1 = new ModelMapper(modelDataWrapper);
		door_left_top_r1.setPos(-21.0F, -14.0F, 0.0F);
		door_left.addChild(door_left_top_r1);
		setRotationAngle(door_left_top_r1, 0.0F, 0.0F, 0.1107F);
		door_left_top_r1.texOffs(192, 83).addBox(0.0F, -19.0F, 0.0F, 1.0F, 19.0F, 14.0F, 0.0F, false);

		door_right = new ModelMapper(modelDataWrapper);
		door_right.setPos(0.0F, 0.0F, 0.0F);
		door.addChild(door_right);
		door_right.texOffs(204, 0).addBox(-21.0F, -14.0F, -14.0F, 1.0F, 14.0F, 14.0F, 0.0F, false);

		door_right_top_r1 = new ModelMapper(modelDataWrapper);
		door_right_top_r1.setPos(-21.0F, -14.0F, 0.0F);
		door_right.addChild(door_right_top_r1);
		setRotationAngle(door_right_top_r1, 0.0F, 0.0F, 0.1107F);
		door_right_top_r1.texOffs(0, 49).addBox(0.0F, -19.0F, -14.0F, 1.0F, 19.0F, 14.0F, 0.0F, false);

		door_handrail = new ModelMapper(modelDataWrapper);
		door_handrail.setPos(0.0F, 24.0F, 0.0F);
		door_handrail.texOffs(319, 0).addBox(0.0F, -35.0F, 0.0F, 0.0F, 6.0F, 0.0F, 0.2F, false);
		door_handrail.texOffs(319, 20).addBox(0.0F, -12.0F, 0.0F, 0.0F, 12.0F, 0.0F, 0.2F, false);

		pole_bottom_diagonal_1_r1 = new ModelMapper(modelDataWrapper);
		pole_bottom_diagonal_1_r1.setPos(-0.025F, -20.1859F, 0.0F);
		door_handrail.addChild(pole_bottom_diagonal_1_r1);
		setRotationAngle(pole_bottom_diagonal_1_r1, 0.1047F, 0.2618F, 0.0F);
		pole_bottom_diagonal_1_r1.texOffs(319, 0).addBox(0.0F, 3.0505F, -0.8668F, 0.0F, 5.0F, 0.0F, 0.2F, false);

		pole_bottom_diagonal_3_r1 = new ModelMapper(modelDataWrapper);
		pole_bottom_diagonal_3_r1.setPos(0.0F, -20.0109F, -0.025F);
		door_handrail.addChild(pole_bottom_diagonal_3_r1);
		setRotationAngle(pole_bottom_diagonal_3_r1, -0.1047F, -0.829F, 0.0F);
		pole_bottom_diagonal_3_r1.texOffs(319, 0).addBox(0.0F, 3.0505F, 0.8668F, 0.0F, 5.0F, 0.0F, 0.2F, false);

		pole_middle_3_r1 = new ModelMapper(modelDataWrapper);
		pole_middle_3_r1.setPos(0.0F, -20.0109F, -0.025F);
		door_handrail.addChild(pole_middle_3_r1);
		setRotationAngle(pole_middle_3_r1, 0.0F, -0.829F, 0.0F);
		pole_middle_3_r1.texOffs(319, 11).addBox(0.0F, -3.2536F, 0.563F, 0.0F, 6.0F, 0.0F, 0.2F, false);

		pole_top_diagonal_3_r1 = new ModelMapper(modelDataWrapper);
		pole_top_diagonal_3_r1.setPos(0.0F, -20.0109F, -0.025F);
		door_handrail.addChild(pole_top_diagonal_3_r1);
		setRotationAngle(pole_top_diagonal_3_r1, 0.1047F, -0.829F, 0.0F);
		pole_top_diagonal_3_r1.texOffs(319, 0).addBox(0.0F, -8.5549F, 0.9198F, 0.0F, 5.0F, 0.0F, 0.2F, false);

		pole_bottom_diagonal_2_r1 = new ModelMapper(modelDataWrapper);
		pole_bottom_diagonal_2_r1.setPos(0.0F, -20.0609F, 0.0F);
		door_handrail.addChild(pole_bottom_diagonal_2_r1);
		setRotationAngle(pole_bottom_diagonal_2_r1, -0.1047F, 1.1345F, 0.0F);
		pole_bottom_diagonal_2_r1.texOffs(319, 0).addBox(0.0F, 3.0505F, 0.8668F, 0.0F, 5.0F, 0.0F, 0.2F, false);

		pole_middle_2_r1 = new ModelMapper(modelDataWrapper);
		pole_middle_2_r1.setPos(0.0F, -20.0609F, 0.0F);
		door_handrail.addChild(pole_middle_2_r1);
		setRotationAngle(pole_middle_2_r1, 0.0F, 1.1345F, 0.0F);
		pole_middle_2_r1.texOffs(319, 11).addBox(0.0F, -3.2536F, 0.563F, 0.0F, 6.0F, 0.0F, 0.2F, false);

		pole_middle_1_r1 = new ModelMapper(modelDataWrapper);
		pole_middle_1_r1.setPos(-0.025F, -20.1859F, 0.0F);
		door_handrail.addChild(pole_middle_1_r1);
		setRotationAngle(pole_middle_1_r1, 0.0F, 0.2618F, 0.0F);
		pole_middle_1_r1.texOffs(319, 11).addBox(0.0F, -3.2536F, -0.563F, 0.0F, 6.0F, 0.0F, 0.2F, false);

		pole_top_diagonal_1_r1 = new ModelMapper(modelDataWrapper);
		pole_top_diagonal_1_r1.setPos(-0.025F, -20.1859F, 0.0F);
		door_handrail.addChild(pole_top_diagonal_1_r1);
		setRotationAngle(pole_top_diagonal_1_r1, -0.1047F, 0.2618F, 0.0F);
		pole_top_diagonal_1_r1.texOffs(319, 0).addBox(0.0F, -8.5549F, -0.9198F, 0.0F, 5.0F, 0.0F, 0.2F, false);

		pole_top_diagonal_2_r1 = new ModelMapper(modelDataWrapper);
		pole_top_diagonal_2_r1.setPos(0.0F, -20.0609F, 0.0F);
		door_handrail.addChild(pole_top_diagonal_2_r1);
		setRotationAngle(pole_top_diagonal_2_r1, 0.1047F, 1.1345F, 0.0F);
		pole_top_diagonal_2_r1.texOffs(319, 0).addBox(0.0F, -8.5549F, 0.9198F, 0.0F, 5.0F, 0.0F, 0.2F, false);

		door_exterior_1 = new ModelMapper(modelDataWrapper);
		door_exterior_1.setPos(0.0F, 24.0F, 0.0F);
		door_exterior_1.texOffs(99, 17).addBox(-21.0F, 0.0F, -19.0F, 1.0F, 4.0F, 37.0F, 0.0F, false);

		door_leaf_r3 = new ModelMapper(modelDataWrapper);
		door_leaf_r3.setPos(0.0F, 0.0F, 0.0F);
		door_exterior_1.addChild(door_leaf_r3);
		setRotationAngle(door_leaf_r3, 0.0F, 0.0F, 0.1107F);
		door_leaf_r3.texOffs(0, 243).addBox(-22.5F, -34.0F, -14.0F, 1.0F, 5.0F, 28.0F, 0.0F, false);

		door_left_exterior_1 = new ModelMapper(modelDataWrapper);
		door_left_exterior_1.setPos(0.0F, 0.0F, 0.0F);
		door_exterior_1.addChild(door_left_exterior_1);
		door_left_exterior_1.texOffs(159, 191).addBox(-22.0F, -14.0F, 0.0F, 0.0F, 14.0F, 14.0F, 0.0F, false);

		door_left_top_r2 = new ModelMapper(modelDataWrapper);
		door_left_top_r2.setPos(-20.8F, -14.0F, 0.0F);
		door_left_exterior_1.addChild(door_left_top_r2);
		setRotationAngle(door_left_top_r2, 0.0F, 0.0F, 0.1107F);
		door_left_top_r2.texOffs(0, 182).addBox(-1.0F, -17.75F, 0.0F, 0.0F, 18.0F, 14.0F, 0.0F, false);

		door_right_exterior_1 = new ModelMapper(modelDataWrapper);
		door_right_exterior_1.setPos(0.0F, 0.0F, 0.0F);
		door_exterior_1.addChild(door_left_exterior_1);
		door_right_exterior_1.texOffs(96, 190).addBox(-22.0F, -14.0F, -14.0F, 0.0F, 14.0F, 14.0F, 0.0F, false);

		door_right_top_r2 = new ModelMapper(modelDataWrapper);
		door_right_top_r2.setPos(-20.8F, -14.0F, 0.0F);
		door_right_exterior_1.addChild(door_right_top_r2);
		setRotationAngle(door_right_top_r2, 0.0F, 0.0F, 0.1107F);
		door_right_top_r2.texOffs(178, 44).addBox(-1.0F, -17.75F, -14.0F, 0.0F, 18.0F, 14.0F, 0.0F, false);

		door_exterior_2 = new ModelMapper(modelDataWrapper);
		door_exterior_2.setPos(0.0F, 24.0F, 0.0F);
		door_exterior_2.texOffs(99, 17).addBox(20.0F, 0.0F, -19.0F, 1.0F, 4.0F, 37.0F, 0.0F, true);

		door_leaf_r4 = new ModelMapper(modelDataWrapper);
		door_leaf_r4.setPos(0.0F, 0.0F, 0.0F);
		door_exterior_2.addChild(door_leaf_r4);
		setRotationAngle(door_leaf_r4, 0.0F, 0.0F, -0.1107F);
		door_leaf_r4.texOffs(0, 243).addBox(21.5F, -34.0F, -14.0F, 1.0F, 5.0F, 28.0F, 0.0F, true);

		door_left_exterior_2 = new ModelMapper(modelDataWrapper);
		door_left_exterior_2.setPos(0.0F, 0.0F, 0.0F);
		door_exterior_2.addChild(door_left_exterior_2);
		door_left_exterior_2.texOffs(159, 191).addBox(22.0F, -14.0F, 0.0F, 0.0F, 14.0F, 14.0F, 0.0F, true);

		door_left_top_r3 = new ModelMapper(modelDataWrapper);
		door_left_top_r3.setPos(20.8F, -14.0F, 0.0F);
		door_left_exterior_2.addChild(door_left_top_r3);
		setRotationAngle(door_left_top_r3, 0.0F, 0.0F, -0.1107F);
		door_left_top_r3.texOffs(0, 182).addBox(1.0F, -17.75F, 0.0F, 0.0F, 18.0F, 14.0F, 0.0F, true);

		door_right_exterior_2 = new ModelMapper(modelDataWrapper);
		door_right_exterior_2.setPos(0.0F, 0.0F, 0.0F);
		door_exterior_2.addChild(door_right_exterior_2);
		door_right_exterior_2.texOffs(96, 190).addBox(22.0F, -14.0F, -14.0F, 0.0F, 14.0F, 14.0F, 0.0F, true);

		door_right_top_r3 = new ModelMapper(modelDataWrapper);
		door_right_top_r3.setPos(20.8F, -14.0F, 0.0F);
		door_right_exterior_2.addChild(door_right_top_r3);
		setRotationAngle(door_right_top_r3, 0.0F, 0.0F, -0.1107F);
		door_right_top_r3.texOffs(178, 44).addBox(1.0F, -17.75F, -14.0F, 0.0F, 18.0F, 14.0F, 0.0F, true);

		end = new ModelMapper(modelDataWrapper);
		end.setPos(0.0F, 24.0F, 0.0F);
		end.texOffs(154, 141).addBox(-20.0F, 0.0F, -12.0F, 40.0F, 1.0F, 20.0F, 0.0F, false);
		end.texOffs(28, 139).addBox(-21.0F, -14.0F, 5.0F, 4.0F, 14.0F, 6.0F, 0.0F, false);
		end.texOffs(178, 205).addBox(9.5F, -35.0F, -12.0F, 8.0F, 35.0F, 19.0F, 0.0F, false);
		end.texOffs(124, 205).addBox(-17.5F, -35.0F, -12.0F, 8.0F, 35.0F, 19.0F, 0.0F, false);
		end.texOffs(217, 118).addBox(-9.5F, -35.0F, -12.0F, 19.0F, 3.0F, 19.0F, 0.0F, false);

		upper_wall_2_r2 = new ModelMapper(modelDataWrapper);
		upper_wall_2_r2.setPos(-21.0F, -14.0F, 0.0F);
		end.addChild(upper_wall_2_r2);
		setRotationAngle(upper_wall_2_r2, 0.0F, 0.0F, 0.1107F);
		upper_wall_2_r2.texOffs(54, 139).addBox(0.0F, -19.0F, 6.0F, 4.0F, 19.0F, 5.0F, 0.0F, false);

		upper_wall_1_r1 = new ModelMapper(modelDataWrapper);
		upper_wall_1_r1.setPos(21.0F, -14.0F, 0.0F);
		end.addChild(upper_wall_1_r1);
		setRotationAngle(upper_wall_1_r1, 0.0F, 3.1416F, -0.1107F);
		upper_wall_1_r1.texOffs(236, 83).addBox(0.0F, -19.0F, -11.0F, 4.0F, 19.0F, 5.0F, 0.0F, false);

		lower_wall_1_r1 = new ModelMapper(modelDataWrapper);
		lower_wall_1_r1.setPos(0.0F, 0.0F, 0.0F);
		end.addChild(lower_wall_1_r1);
		setRotationAngle(lower_wall_1_r1, 0.0F, 3.1416F, 0.0F);
		lower_wall_1_r1.texOffs(154, 141).addBox(-21.0F, -14.0F, -11.0F, 4.0F, 14.0F, 6.0F, 0.0F, false);

		end_exterior = new ModelMapper(modelDataWrapper);
		end_exterior.setPos(0.0F, 24.0F, 0.0F);
		end_exterior.texOffs(58, 243).addBox(17.0F, -14.0F, -12.0F, 3.0F, 14.0F, 22.0F, 0.0F, true);
		end_exterior.texOffs(58, 243).addBox(-20.0F, -14.0F, -12.0F, 3.0F, 14.0F, 22.0F, 0.0F, false);
		end_exterior.texOffs(168, 85).addBox(9.5F, -34.0F, -12.0F, 10.0F, 34.0F, 0.0F, 0.0F, false);
		end_exterior.texOffs(164, 0).addBox(-19.5F, -34.0F, -12.0F, 10.0F, 34.0F, 0.0F, 0.0F, false);
		end_exterior.texOffs(246, 11).addBox(-18.0F, -41.0F, -12.0F, 36.0F, 7.0F, 0.0F, 0.0F, false);

		door_leaf_2_r1 = new ModelMapper(modelDataWrapper);
		door_leaf_2_r1.setPos(0.0F, 0.0F, 0.0F);
		end_exterior.addChild(door_leaf_2_r1);
		setRotationAngle(door_leaf_2_r1, 0.0F, 3.1416F, -0.1107F);
		door_leaf_2_r1.texOffs(236, 83).addBox(-22.5F, -34.0F, -16.0F, 1.0F, 5.0F, 28.0F, 0.0F, false);

		door_leaf_1_r1 = new ModelMapper(modelDataWrapper);
		door_leaf_1_r1.setPos(0.0F, 0.0F, 0.0F);
		end_exterior.addChild(door_leaf_1_r1);
		setRotationAngle(door_leaf_1_r1, 0.0F, 0.0F, 0.1107F);
		door_leaf_1_r1.texOffs(238, 204).addBox(-22.5F, -34.0F, -12.0F, 1.0F, 5.0F, 28.0F, 0.0F, false);

		upper_wall_2_r3 = new ModelMapper(modelDataWrapper);
		upper_wall_2_r3.setPos(-21.0F, -14.0F, 0.0F);
		end_exterior.addChild(upper_wall_2_r3);
		setRotationAngle(upper_wall_2_r3, 0.0F, 0.0F, 0.1107F);
		upper_wall_2_r3.texOffs(0, 139).addBox(1.0F, -22.0F, -12.0F, 3.0F, 22.0F, 22.0F, 0.0F, false);

		upper_wall_1_r2 = new ModelMapper(modelDataWrapper);
		upper_wall_1_r2.setPos(21.0F, -14.0F, 0.0F);
		end_exterior.addChild(upper_wall_1_r2);
		setRotationAngle(upper_wall_1_r2, 0.0F, 0.0F, -0.1107F);
		upper_wall_1_r2.texOffs(0, 139).addBox(-4.0F, -22.0F, -12.0F, 3.0F, 22.0F, 22.0F, 0.0F, true);

		roof_end = new ModelMapper(modelDataWrapper);
		roof_end.setPos(0.0F, 24.0F, 0.0F);
		

		inner_roof_1 = new ModelMapper(modelDataWrapper);
		inner_roof_1.setPos(-2.0F, -33.0F, 38.0F);
		roof_end.addChild(inner_roof_1);
		inner_roof_1.texOffs(192, 83).addBox(-16.0034F, 0.0221F, -32.0F, 5.0F, 1.0F, 34.0F, 0.0F, false);
		inner_roof_1.texOffs(46, 49).addBox(-9.075F, -1.275F, -32.0F, 1.0F, 0.0F, 34.0F, 0.0F, false);
		inner_roof_1.texOffs(82, 99).addBox(-1.975F, -1.725F, -32.0F, 4.0F, 0.0F, 34.0F, 0.0F, false);

		inner_roof_5_r3 = new ModelMapper(modelDataWrapper);
		inner_roof_5_r3.setPos(-10.0F, 1.0F, -16.0F);
		inner_roof_1.addChild(inner_roof_5_r3);
		setRotationAngle(inner_roof_5_r3, 0.0F, 0.0F, -0.0873F);
		inner_roof_5_r3.texOffs(0, 0).addBox(6.075F, -1.95F, -16.0F, 3.0F, 0.0F, 34.0F, 0.0F, false);

		inner_roof_4_r3 = new ModelMapper(modelDataWrapper);
		inner_roof_4_r3.setPos(-10.0F, 1.0F, -16.0F);
		inner_roof_1.addChild(inner_roof_4_r3);
		setRotationAngle(inner_roof_4_r3, 0.0F, 0.0F, -0.1745F);
		inner_roof_4_r3.texOffs(90, 99).addBox(4.3F, -1.411F, -16.0F, 2.0F, 0.0F, 34.0F, 0.0F, false);

		inner_roof_2_r3 = new ModelMapper(modelDataWrapper);
		inner_roof_2_r3.setPos(-9.9911F, 1.023F, -16.0F);
		inner_roof_1.addChild(inner_roof_2_r3);
		setRotationAngle(inner_roof_2_r3, 0.0F, 0.0F, -0.8727F);
		inner_roof_2_r3.texOffs(0, 49).addBox(-0.65F, -0.775F, -16.0F, 3.0F, 0.0F, 34.0F, 0.0F, false);

		inner_roof_2 = new ModelMapper(modelDataWrapper);
		inner_roof_2.setPos(2.0F, -33.0F, 38.0F);
		roof_end.addChild(inner_roof_2);
		inner_roof_2.texOffs(192, 83).addBox(11.0034F, 0.0221F, -32.0F, 5.0F, 1.0F, 34.0F, 0.0F, true);
		inner_roof_2.texOffs(46, 49).addBox(8.075F, -1.275F, -32.0F, 1.0F, 0.0F, 34.0F, 0.0F, true);
		inner_roof_2.texOffs(82, 99).addBox(-2.025F, -1.725F, -32.0F, 4.0F, 0.0F, 34.0F, 0.0F, true);

		inner_roof_6_r1 = new ModelMapper(modelDataWrapper);
		inner_roof_6_r1.setPos(10.0F, 1.0F, -16.0F);
		inner_roof_2.addChild(inner_roof_6_r1);
		setRotationAngle(inner_roof_6_r1, 0.0F, 0.0F, 0.0873F);
		inner_roof_6_r1.texOffs(0, 0).addBox(-9.075F, -1.95F, -16.0F, 3.0F, 0.0F, 34.0F, 0.0F, true);

		inner_roof_5_r4 = new ModelMapper(modelDataWrapper);
		inner_roof_5_r4.setPos(10.0F, 1.0F, -16.0F);
		inner_roof_2.addChild(inner_roof_5_r4);
		setRotationAngle(inner_roof_5_r4, 0.0F, 0.0F, 0.1745F);
		inner_roof_5_r4.texOffs(90, 99).addBox(-6.3F, -1.411F, -16.0F, 2.0F, 0.0F, 34.0F, 0.0F, true);

		inner_roof_3_r1 = new ModelMapper(modelDataWrapper);
		inner_roof_3_r1.setPos(9.9911F, 1.023F, -16.0F);
		inner_roof_2.addChild(inner_roof_3_r1);
		setRotationAngle(inner_roof_3_r1, 0.0F, 0.0F, 0.8727F);
		inner_roof_3_r1.texOffs(0, 49).addBox(-2.35F, -0.775F, -16.0F, 3.0F, 0.0F, 34.0F, 0.0F, true);

		handrail_end = new ModelMapper(modelDataWrapper);
		handrail_end.setPos(0.0F, 0.0F, 24.0F);
		roof_end.addChild(handrail_end);
		handrail_end.texOffs(319, 0).addBox(0.0F, -35.0F, 0.0F, 0.0F, 6.0F, 0.0F, 0.2F, false);
		handrail_end.texOffs(319, 0).addBox(0.0F, -12.0F, 0.0F, 0.0F, 12.0F, 0.0F, 0.2F, false);

		pole_bottom_diagonal_1_r2 = new ModelMapper(modelDataWrapper);
		pole_bottom_diagonal_1_r2.setPos(-0.025F, -20.1859F, 0.0F);
		handrail_end.addChild(pole_bottom_diagonal_1_r2);
		setRotationAngle(pole_bottom_diagonal_1_r2, 0.1047F, 0.2618F, 0.0F);
		pole_bottom_diagonal_1_r2.texOffs(319, 0).addBox(0.0F, 3.0505F, -0.8668F, 0.0F, 5.0F, 0.0F, 0.2F, false);

		pole_bottom_diagonal_3_r2 = new ModelMapper(modelDataWrapper);
		pole_bottom_diagonal_3_r2.setPos(0.0F, -20.0109F, -0.025F);
		handrail_end.addChild(pole_bottom_diagonal_3_r2);
		setRotationAngle(pole_bottom_diagonal_3_r2, -0.1047F, -0.829F, 0.0F);
		pole_bottom_diagonal_3_r2.texOffs(319, 0).addBox(0.0F, 3.0505F, 0.8668F, 0.0F, 5.0F, 0.0F, 0.2F, false);

		pole_middle_3_r2 = new ModelMapper(modelDataWrapper);
		pole_middle_3_r2.setPos(0.0F, -20.0109F, -0.025F);
		handrail_end.addChild(pole_middle_3_r2);
		setRotationAngle(pole_middle_3_r2, 0.0F, -0.829F, 0.0F);
		pole_middle_3_r2.texOffs(319, 11).addBox(0.0F, -3.2536F, 0.563F, 0.0F, 6.0F, 0.0F, 0.2F, false);

		pole_top_diagonal_3_r2 = new ModelMapper(modelDataWrapper);
		pole_top_diagonal_3_r2.setPos(0.0F, -20.0109F, -0.025F);
		handrail_end.addChild(pole_top_diagonal_3_r2);
		setRotationAngle(pole_top_diagonal_3_r2, 0.1047F, -0.829F, 0.0F);
		pole_top_diagonal_3_r2.texOffs(319, 0).addBox(0.0F, -8.5549F, 0.9198F, 0.0F, 5.0F, 0.0F, 0.2F, false);

		pole_bottom_diagonal_2_r2 = new ModelMapper(modelDataWrapper);
		pole_bottom_diagonal_2_r2.setPos(0.0F, -20.0609F, 0.0F);
		handrail_end.addChild(pole_bottom_diagonal_2_r2);
		setRotationAngle(pole_bottom_diagonal_2_r2, -0.1047F, 1.1345F, 0.0F);
		pole_bottom_diagonal_2_r2.texOffs(319, 0).addBox(0.0F, 3.0505F, 0.8668F, 0.0F, 5.0F, 0.0F, 0.2F, false);

		pole_middle_2_r2 = new ModelMapper(modelDataWrapper);
		pole_middle_2_r2.setPos(0.0F, -20.0609F, 0.0F);
		handrail_end.addChild(pole_middle_2_r2);
		setRotationAngle(pole_middle_2_r2, 0.0F, 1.1345F, 0.0F);
		pole_middle_2_r2.texOffs(319, 11).addBox(0.0F, -3.2536F, 0.563F, 0.0F, 6.0F, 0.0F, 0.2F, false);

		pole_middle_1_r2 = new ModelMapper(modelDataWrapper);
		pole_middle_1_r2.setPos(-0.025F, -20.1859F, 0.0F);
		handrail_end.addChild(pole_middle_1_r2);
		setRotationAngle(pole_middle_1_r2, 0.0F, 0.2618F, 0.0F);
		pole_middle_1_r2.texOffs(319, 11).addBox(0.0F, -3.2536F, -0.563F, 0.0F, 6.0F, 0.0F, 0.2F, false);

		pole_top_diagonal_1_r2 = new ModelMapper(modelDataWrapper);
		pole_top_diagonal_1_r2.setPos(-0.025F, -20.1859F, 0.0F);
		handrail_end.addChild(pole_top_diagonal_1_r2);
		setRotationAngle(pole_top_diagonal_1_r2, -0.1047F, 0.2618F, 0.0F);
		pole_top_diagonal_1_r2.texOffs(319, 0).addBox(0.0F, -8.5549F, -0.9198F, 0.0F, 5.0F, 0.0F, 0.2F, false);

		pole_top_diagonal_2_r2 = new ModelMapper(modelDataWrapper);
		pole_top_diagonal_2_r2.setPos(0.0F, -20.0609F, 0.0F);
		handrail_end.addChild(pole_top_diagonal_2_r2);
		setRotationAngle(pole_top_diagonal_2_r2, 0.1047F, 1.1345F, 0.0F);
		pole_top_diagonal_2_r2.texOffs(319, 0).addBox(0.0F, -8.5549F, 0.9198F, 0.0F, 5.0F, 0.0F, 0.2F, false);

		roof_end_exterior = new ModelMapper(modelDataWrapper);
		roof_end_exterior.setPos(0.0F, 24.0F, 0.0F);
		roof_end_exterior.texOffs(0, 49).addBox(-8.0F, -43.0F, 0.0F, 16.0F, 2.0F, 48.0F, 0.0F, false);

		vent_2_r1 = new ModelMapper(modelDataWrapper);
		vent_2_r1.setPos(-8.0F, -43.0F, 0.0F);
		roof_end_exterior.addChild(vent_2_r1);
		setRotationAngle(vent_2_r1, 0.0F, 0.0F, -0.3491F);
		vent_2_r1.texOffs(80, 91).addBox(-9.0F, 0.0F, 0.0F, 9.0F, 2.0F, 48.0F, 0.0F, false);

		vent_1_r1 = new ModelMapper(modelDataWrapper);
		vent_1_r1.setPos(8.0F, -43.0F, 0.0F);
		roof_end_exterior.addChild(vent_1_r1);
		setRotationAngle(vent_1_r1, 0.0F, 0.0F, 0.3491F);
		vent_1_r1.texOffs(138, 0).addBox(0.0F, 0.0F, 0.0F, 9.0F, 2.0F, 48.0F, 0.0F, false);

		outer_roof_1 = new ModelMapper(modelDataWrapper);
		outer_roof_1.setPos(0.0F, 0.0F, 0.0F);
		roof_end_exterior.addChild(outer_roof_1);
		outer_roof_1.texOffs(247, 162).addBox(-5.7289F, -41.8532F, -12.0F, 6.0F, 1.0F, 20.0F, 0.0F, false);

		outer_roof_5_r2 = new ModelMapper(modelDataWrapper);
		outer_roof_5_r2.setPos(-9.4945F, -40.1738F, -8.0F);
		outer_roof_1.addChild(outer_roof_5_r2);
		setRotationAngle(outer_roof_5_r2, 0.0F, 0.0F, -0.1745F);
		outer_roof_5_r2.texOffs(87, 243).addBox(-4.0F, -1.0F, -4.0F, 8.0F, 1.0F, 20.0F, 0.0F, false);

		outer_roof_4_r2 = new ModelMapper(modelDataWrapper);
		outer_roof_4_r2.setPos(-14.4064F, -38.848F, -8.0F);
		outer_roof_1.addChild(outer_roof_4_r2);
		setRotationAngle(outer_roof_4_r2, 0.0F, 0.0F, -0.5236F);
		outer_roof_4_r2.texOffs(31, 243).addBox(-2.5F, -1.0F, -4.0F, 4.0F, 1.0F, 20.0F, 0.0F, false);

		outer_roof_3_r2 = new ModelMapper(modelDataWrapper);
		outer_roof_3_r2.setPos(-16.7054F, -37.098F, -8.0F);
		outer_roof_1.addChild(outer_roof_3_r2);
		setRotationAngle(outer_roof_3_r2, 0.0F, 0.0F, -1.0472F);
		outer_roof_3_r2.texOffs(246, 253).addBox(-2.0F, -1.0F, -4.0F, 3.0F, 1.0F, 20.0F, 0.0F, false);

		outer_roof_2 = new ModelMapper(modelDataWrapper);
		outer_roof_2.setPos(0.0F, 0.0F, 0.0F);
		roof_end_exterior.addChild(outer_roof_2);
		outer_roof_2.texOffs(247, 162).addBox(-0.2711F, -41.8532F, -12.0F, 6.0F, 1.0F, 20.0F, 0.0F, true);

		outer_roof_5_r3 = new ModelMapper(modelDataWrapper);
		outer_roof_5_r3.setPos(9.4945F, -40.1738F, -8.0F);
		outer_roof_2.addChild(outer_roof_5_r3);
		setRotationAngle(outer_roof_5_r3, 0.0F, 0.0F, 0.1745F);
		outer_roof_5_r3.texOffs(87, 243).addBox(-4.0F, -1.0F, -4.0F, 8.0F, 1.0F, 20.0F, 0.0F, true);

		outer_roof_4_r3 = new ModelMapper(modelDataWrapper);
		outer_roof_4_r3.setPos(15.2724F, -38.348F, -8.0F);
		outer_roof_2.addChild(outer_roof_4_r3);
		setRotationAngle(outer_roof_4_r3, 0.0F, 0.0F, 0.5236F);
		outer_roof_4_r3.texOffs(31, 243).addBox(-2.5F, -1.0F, -4.0F, 4.0F, 1.0F, 20.0F, 0.0F, true);

		outer_roof_3_r3 = new ModelMapper(modelDataWrapper);
		outer_roof_3_r3.setPos(16.7054F, -37.098F, -8.0F);
		outer_roof_2.addChild(outer_roof_3_r3);
		setRotationAngle(outer_roof_3_r3, 0.0F, 0.0F, 1.0472F);
		outer_roof_3_r3.texOffs(246, 253).addBox(-1.0F, -1.0F, -4.0F, 3.0F, 1.0F, 20.0F, 0.0F, true);

		roof_window_light = new ModelMapper(modelDataWrapper);
		roof_window_light.setPos(0.0F, 24.0F, 0.0F);
		

		light_2_r1 = new ModelMapper(modelDataWrapper);
		light_2_r1.setPos(-12.075F, -32.2F, 0.0F);
		roof_window_light.addChild(light_2_r1);
		setRotationAngle(light_2_r1, 0.0F, 0.0F, 1.2217F);
		light_2_r1.texOffs(80, 0).addBox(-1.925F, -2.575F, -24.0F, 1.0F, 0.0F, 48.0F, 0.0F, false);

		light_1_r1 = new ModelMapper(modelDataWrapper);
		light_1_r1.setPos(-12.075F, -32.2F, 0.0F);
		roof_window_light.addChild(light_1_r1);
		setRotationAngle(light_1_r1, 0.0F, 0.0F, -0.0873F);
		light_1_r1.texOffs(74, 0).addBox(2.25F, -1.575F, -24.0F, 2.0F, 0.0F, 48.0F, 0.0F, false);

		roof_door_light = new ModelMapper(modelDataWrapper);
		roof_door_light.setPos(0.0F, 24.0F, 0.0F);
		

		light_2_r2 = new ModelMapper(modelDataWrapper);
		light_2_r2.setPos(-12.075F, -32.2F, 0.0F);
		roof_door_light.addChild(light_2_r2);
		setRotationAngle(light_2_r2, 0.0F, 0.0F, 1.2217F);
		light_2_r2.texOffs(96, 8).addBox(-1.925F, -2.575F, -16.0F, 1.0F, 0.0F, 32.0F, 0.0F, false);

		light_1_r2 = new ModelMapper(modelDataWrapper);
		light_1_r2.setPos(-12.075F, -32.2F, 0.0F);
		roof_door_light.addChild(light_1_r2);
		setRotationAngle(light_1_r2, 0.0F, 0.0F, -0.0873F);
		light_1_r2.texOffs(90, 8).addBox(2.25F, -1.575F, -16.0F, 2.0F, 0.0F, 32.0F, 0.0F, false);

		roof_end_light = new ModelMapper(modelDataWrapper);
		roof_end_light.setPos(0.0F, 24.0F, 16.0F);
		

		light_4_r1 = new ModelMapper(modelDataWrapper);
		light_4_r1.setPos(12.075F, -32.2F, 0.0F);
		roof_end_light.addChild(light_4_r1);
		setRotationAngle(light_4_r1, 0.0F, 0.0F, -1.2217F);
		light_4_r1.texOffs(94, 0).addBox(0.925F, -2.575F, -9.0F, 1.0F, 0.0F, 33.0F, 0.0F, true);

		light_3_r1 = new ModelMapper(modelDataWrapper);
		light_3_r1.setPos(12.075F, -32.2F, 0.0F);
		roof_end_light.addChild(light_3_r1);
		setRotationAngle(light_3_r1, 0.0F, 0.0F, 0.0873F);
		light_3_r1.texOffs(89, 0).addBox(-4.25F, -1.575F, -9.0F, 2.0F, 0.0F, 33.0F, 0.0F, true);

		light_2_r3 = new ModelMapper(modelDataWrapper);
		light_2_r3.setPos(-12.075F, -32.2F, 0.0F);
		roof_end_light.addChild(light_2_r3);
		setRotationAngle(light_2_r3, 0.0F, 0.0F, 1.2217F);
		light_2_r3.texOffs(94, 0).addBox(-1.925F, -2.575F, -9.0F, 1.0F, 0.0F, 33.0F, 0.0F, false);

		light_1_r3 = new ModelMapper(modelDataWrapper);
		light_1_r3.setPos(-12.075F, -32.2F, 0.0F);
		roof_end_light.addChild(light_1_r3);
		setRotationAngle(light_1_r3, 0.0F, 0.0F, -0.0873F);
		light_1_r3.texOffs(87, 0).addBox(2.25F, -1.575F, -9.0F, 2.0F, 0.0F, 33.0F, 0.0F, false);

		head = new ModelMapper(modelDataWrapper);
		head.setPos(0.0F, 24.0F, 0.0F);
		head.texOffs(80, 58).addBox(-18.0F, 0.0F, -18.0F, 36.0F, 1.0F, 26.0F, 0.0F, false);
		head.texOffs(212, 162).addBox(-20.0F, -14.0F, -17.0F, 3.0F, 14.0F, 28.0F, 0.0F, false);
		head.texOffs(266, 83).addBox(-17.0F, -14.0F, -2.0F, 4.0F, 14.0F, 13.0F, 0.0F, false);

		upper_wall_2_r4 = new ModelMapper(modelDataWrapper);
		upper_wall_2_r4.setPos(-21.0F, -14.0F, 0.0F);
		head.addChild(upper_wall_2_r4);
		setRotationAngle(upper_wall_2_r4, 0.0F, 0.0F, 0.1107F);
		upper_wall_2_r4.texOffs(0, 196).addBox(1.0F, -19.0F, -17.0F, 3.0F, 19.0F, 28.0F, 0.0F, false);
		upper_wall_2_r4.texOffs(0, 0).addBox(4.0F, -21.0F, -2.0F, 4.0F, 21.0F, 13.0F, 0.0F, false);

		upper_wall_1_r3 = new ModelMapper(modelDataWrapper);
		upper_wall_1_r3.setPos(21.0F, -14.0F, 0.0F);
		head.addChild(upper_wall_1_r3);
		setRotationAngle(upper_wall_1_r3, 0.0F, 3.1416F, -0.1107F);
		upper_wall_1_r3.texOffs(62, 196).addBox(1.0F, -19.0F, -11.0F, 3.0F, 19.0F, 28.0F, 0.0F, false);

		lower_wall_1_r2 = new ModelMapper(modelDataWrapper);
		lower_wall_1_r2.setPos(0.0F, 0.0F, 0.0F);
		head.addChild(lower_wall_1_r2);
		setRotationAngle(lower_wall_1_r2, 0.0F, 3.1416F, 0.0F);
		lower_wall_1_r2.texOffs(204, 231).addBox(-20.0F, -14.0F, -11.0F, 3.0F, 14.0F, 28.0F, 0.0F, false);

		ceiling = new ModelMapper(modelDataWrapper);
		ceiling.setPos(0.0F, 0.0F, 0.0F);
		head.addChild(ceiling);
		ceiling.texOffs(0, 183).addBox(-15.165F, -34.3F, -15.0F, 8.0F, 4.0F, 4.0F, 0.0F, true);
		ceiling.texOffs(208, 273).addBox(-7.5F, -32.0F, -17.0F, 2.0F, 32.0F, 2.0F, 0.0F, true);
		ceiling.texOffs(208, 273).addBox(5.5F, -32.0F, -17.0F, 2.0F, 32.0F, 2.0F, 0.0F, true);

		panel_9_r1 = new ModelMapper(modelDataWrapper);
		panel_9_r1.setPos(-21.1745F, -14.1917F, -28.0F);
		ceiling.addChild(panel_9_r1);
		setRotationAngle(panel_9_r1, 0.0F, 0.0F, 0.6109F);
		panel_9_r1.texOffs(0, 49).addBox(11.975F, -33.45F, 13.0F, 2.0F, 4.0F, 4.0F, 0.0F, true);

		panel_8_r1 = new ModelMapper(modelDataWrapper);
		panel_8_r1.setPos(22.165F, 0.7F, -28.0F);
		ceiling.addChild(panel_8_r1);
		setRotationAngle(panel_8_r1, 0.0F, 0.0F, 0.0F);
		panel_8_r1.texOffs(0, 183).addBox(-15.0F, -35.0F, 13.0F, 8.0F, 4.0F, 4.0F, 0.0F, true);

		panel_7_r1 = new ModelMapper(modelDataWrapper);
		panel_7_r1.setPos(0.0F, 0.7F, 2.0F);
		ceiling.addChild(panel_7_r1);
		setRotationAngle(panel_7_r1, 0.0F, 0.0F, 0.0F);
		panel_7_r1.texOffs(80, 90).addBox(-7.0F, -34.0F, -17.0F, 14.0F, 2.0F, 4.0F, 0.0F, true);

		panel_6_r1 = new ModelMapper(modelDataWrapper);
		panel_6_r1.setPos(21.1745F, -14.1917F, -28.0F);
		ceiling.addChild(panel_6_r1);
		setRotationAngle(panel_6_r1, 0.0F, 0.0F, -0.6109F);
		panel_6_r1.texOffs(0, 49).addBox(-13.975F, -33.45F, 13.0F, 2.0F, 4.0F, 4.0F, 0.0F, true);

		panel_4_r1 = new ModelMapper(modelDataWrapper);
		panel_4_r1.setPos(0.075F, 1.0F, 2.0F);
		ceiling.addChild(panel_4_r1);
		setRotationAngle(panel_4_r1, 0.0F, 0.0F, -0.6981F);
		panel_4_r1.texOffs(0, 214).addBox(10.475F, -33.95F, -13.0F, 3.0F, 1.0F, 9.0F, 0.0F, false);

		panel_3_r1 = new ModelMapper(modelDataWrapper);
		panel_3_r1.setPos(-1.0805F, 1.0256F, 2.0F);
		ceiling.addChild(panel_3_r1);
		setRotationAngle(panel_3_r1, 0.0F, 0.0F, 0.0F);
		panel_3_r1.texOffs(250, 42).addBox(-14.0F, -33.0F, -13.0F, 2.0F, 1.0F, 9.0F, 0.0F, false);

		panel_2_r1 = new ModelMapper(modelDataWrapper);
		panel_2_r1.setPos(0.0F, 1.0F, -2.0F);
		ceiling.addChild(panel_2_r1);
		setRotationAngle(panel_2_r1, 0.0F, 0.0F, 0.6981F);
		panel_2_r1.texOffs(250, 42).addBox(-13.475F, -33.95F, -9.0F, 3.0F, 1.0F, 22.0F, 0.0F, false);

		panel_1_r1 = new ModelMapper(modelDataWrapper);
		panel_1_r1.setPos(0.1555F, 1.0256F, -2.0F);
		ceiling.addChild(panel_1_r1);
		setRotationAngle(panel_1_r1, 0.0F, 0.0F, 0.0F);
		panel_1_r1.texOffs(138, 18).addBox(13.0F, -33.0F, -9.0F, 2.0F, 1.0F, 22.0F, 0.0F, false);

		main_r1 = new ModelMapper(modelDataWrapper);
		main_r1.setPos(0.0F, 1.0F, -2.0F);
		ceiling.addChild(main_r1);
		setRotationAngle(main_r1, 0.0F, 0.0F, 0.0F);
		main_r1.texOffs(54, 141).addBox(-12.0F, -35.0F, -9.0F, 24.0F, 1.0F, 22.0F, 0.0F, true);

		emergency_door = new ModelMapper(modelDataWrapper);
		emergency_door.setPos(0.0F, 0.0F, 0.0F);
		head.addChild(emergency_door);
		emergency_door.texOffs(238, 246).addBox(-5.5F, -11.0F, -18.0F, 11.0F, 11.0F, 1.0F, 0.0F, false);

		upper_r1 = new ModelMapper(modelDataWrapper);
		upper_r1.setPos(0.0F, -0.2911F, 2.1395F);
		emergency_door.addChild(upper_r1);
		setRotationAngle(upper_r1, -0.0873F, 0.0F, 0.0F);
		upper_r1.texOffs(212, 162).addBox(-5.5F, -31.0F, -21.0F, 11.0F, 22.0F, 1.0F, 0.0F, false);

		left_c_panel = new ModelMapper(modelDataWrapper);
		left_c_panel.setPos(0.0F, 0.0F, 0.0F);
		head.addChild(left_c_panel);
		

		panel_r1 = new ModelMapper(modelDataWrapper);
		panel_r1.setPos(0.0F, 0.0F, 0.0F);
		left_c_panel.addChild(panel_r1);
		setRotationAngle(panel_r1, -2.5744F, 0.0F, 3.1416F);
		panel_r1.texOffs(246, 18).addBox(-17.0F, -4.15F, 15.65F, 10.0F, 3.0F, 6.0F, 0.0F, false);

		base_r1 = new ModelMapper(modelDataWrapper);
		base_r1.setPos(0.0F, 0.0F, 0.0F);
		left_c_panel.addChild(base_r1);
		setRotationAngle(base_r1, 0.0F, 3.1416F, 0.0F);
		base_r1.texOffs(146, 259).addBox(-17.0F, -12.0F, 11.0F, 10.0F, 12.0F, 6.0F, 0.0F, false);

		right_c_panel = new ModelMapper(modelDataWrapper);
		right_c_panel.setPos(0.0F, 0.0F, 0.0F);
		head.addChild(right_c_panel);
		

		panel_r2 = new ModelMapper(modelDataWrapper);
		panel_r2.setPos(0.0F, 0.0F, 0.0F);
		right_c_panel.addChild(panel_r2);
		setRotationAngle(panel_r2, -2.5744F, 0.0F, -3.1416F);
		panel_r2.texOffs(192, 118).addBox(7.0F, -4.15F, 15.65F, 10.0F, 3.0F, 6.0F, 0.0F, false);

		base_r2 = new ModelMapper(modelDataWrapper);
		base_r2.setPos(0.0F, 0.0F, 0.0F);
		right_c_panel.addChild(base_r2);
		setRotationAngle(base_r2, 0.0F, -3.1416F, 0.0F);
		base_r2.texOffs(213, 205).addBox(7.0F, -12.0F, 11.0F, 10.0F, 12.0F, 6.0F, 0.0F, false);

		handrail = new ModelMapper(modelDataWrapper);
		handrail.setPos(0.0F, 0.0F, 0.0F);
		head.addChild(handrail);
		

		wall = new ModelMapper(modelDataWrapper);
		wall.setPos(0.0F, 0.0F, 2.0F);
		handrail.addChild(wall);
		

		handrail_4_r1 = new ModelMapper(modelDataWrapper);
		handrail_4_r1.setPos(0.0F, 0.0F, -4.0F);
		wall.addChild(handrail_4_r1);
		setRotationAngle(handrail_4_r1, 1.5708F, -0.4363F, 0.0F);
		handrail_4_r1.texOffs(319, 0).addBox(12.3F, -10.275F, 11.0F, 0.0F, 1.0F, 0.0F, 0.2F, false);

		handrail_3_r1 = new ModelMapper(modelDataWrapper);
		handrail_3_r1.setPos(0.0F, 0.0F, -2.0F);
		wall.addChild(handrail_3_r1);
		setRotationAngle(handrail_3_r1, -1.5708F, 1.1345F, 0.0F);
		handrail_3_r1.texOffs(319, 0).addBox(2.725F, -17.975F, -11.0F, 0.0F, 2.0F, 0.0F, 0.2F, false);

		handrail_2_r1 = new ModelMapper(modelDataWrapper);
		handrail_2_r1.setPos(0.0F, 0.0F, -2.0F);
		wall.addChild(handrail_2_r1);
		setRotationAngle(handrail_2_r1, -1.5708F, 0.4363F, 0.0F);
		handrail_2_r1.texOffs(319, 0).addBox(12.3F, -10.275F, -11.0F, 0.0F, 1.0F, 0.0F, 0.2F, false);

		handrail_5_r1 = new ModelMapper(modelDataWrapper);
		handrail_5_r1.setPos(0.0F, 0.0F, -4.0F);
		wall.addChild(handrail_5_r1);
		setRotationAngle(handrail_5_r1, 1.5708F, -1.1345F, 0.0F);
		handrail_5_r1.texOffs(319, 0).addBox(2.725F, -17.975F, 11.0F, 0.0F, 2.0F, 0.0F, 0.2F, false);

		handrail_1_r1 = new ModelMapper(modelDataWrapper);
		handrail_1_r1.setPos(0.0F, 0.0F, -2.0F);
		wall.addChild(handrail_1_r1);
		setRotationAngle(handrail_1_r1, -1.5708F, 0.0F, 0.0F);
		handrail_1_r1.texOffs(319, 11).addBox(15.0F, -3.0F, -11.0F, 0.0F, 8.0F, 0.0F, 0.2F, false);

		ceiling2 = new ModelMapper(modelDataWrapper);
		ceiling2.setPos(1.0F, 0.0F, 2.0F);
		handrail.addChild(ceiling2);
		ceiling2.texOffs(0, 0).addBox(-9.0F, -31.7F, 0.0F, 2.0F, 4.0F, 0.0F, 0.0F, false);
		ceiling2.texOffs(0, 0).addBox(-9.0F, -31.7F, -6.0F, 2.0F, 4.0F, 0.0F, 0.0F, false);
		ceiling2.texOffs(0, 0).addBox(5.0F, -31.7F, 0.0F, 2.0F, 4.0F, 0.0F, 0.0F, true);
		ceiling2.texOffs(0, 0).addBox(5.0F, -31.7F, -6.0F, 2.0F, 4.0F, 0.0F, 0.0F, true);

		handrail_7_r1 = new ModelMapper(modelDataWrapper);
		handrail_7_r1.setPos(-19.0F, -16.0F, -4.0F);
		ceiling2.addChild(handrail_7_r1);
		setRotationAngle(handrail_7_r1, 1.5708F, 1.1345F, 1.5708F);
		handrail_7_r1.texOffs(319, 0).addBox(-2.725F, -17.975F, 11.0F, 0.0F, 2.0F, 0.0F, 0.2F, false);

		handrail_4_r2 = new ModelMapper(modelDataWrapper);
		handrail_4_r2.setPos(-19.0F, -16.0F, -2.0F);
		ceiling2.addChild(handrail_4_r2);
		setRotationAngle(handrail_4_r2, -1.5708F, -0.4363F, 1.5708F);
		handrail_4_r2.texOffs(319, 0).addBox(-12.3F, -10.275F, -11.0F, 0.0F, 1.0F, 0.0F, 0.2F, false);

		handrail_5_r2 = new ModelMapper(modelDataWrapper);
		handrail_5_r2.setPos(-19.0F, -16.0F, -2.0F);
		ceiling2.addChild(handrail_5_r2);
		setRotationAngle(handrail_5_r2, -1.5708F, -1.1345F, 1.5708F);
		handrail_5_r2.texOffs(319, 0).addBox(-2.725F, -17.975F, -11.0F, 0.0F, 2.0F, 0.0F, 0.2F, false);

		handrail_6_r1 = new ModelMapper(modelDataWrapper);
		handrail_6_r1.setPos(-19.0F, -16.0F, -4.0F);
		ceiling2.addChild(handrail_6_r1);
		setRotationAngle(handrail_6_r1, 1.5708F, 0.4363F, 1.5708F);
		handrail_6_r1.texOffs(319, 0).addBox(-12.3F, -10.275F, 11.0F, 0.0F, 1.0F, 0.0F, 0.2F, false);

		handrail_3_r2 = new ModelMapper(modelDataWrapper);
		handrail_3_r2.setPos(-19.0F, -16.0F, -2.0F);
		ceiling2.addChild(handrail_3_r2);
		setRotationAngle(handrail_3_r2, -1.5708F, 0.0F, 1.5708F);
		handrail_3_r2.texOffs(319, 0).addBox(-15.0F, -3.0F, -11.0F, 0.0F, 8.0F, 0.0F, 0.2F, false);

		handrail_5_r3 = new ModelMapper(modelDataWrapper);
		handrail_5_r3.setPos(16.75F, -16.0F, -4.0F);
		ceiling2.addChild(handrail_5_r3);
		setRotationAngle(handrail_5_r3, 1.5708F, -0.4363F, -1.5708F);
		handrail_5_r3.texOffs(319, 0).addBox(12.3F, -10.275F, 11.0F, 0.0F, 1.0F, 0.0F, 0.2F, false);

		handrail_4_r3 = new ModelMapper(modelDataWrapper);
		handrail_4_r3.setPos(16.75F, -16.0F, -2.0F);
		ceiling2.addChild(handrail_4_r3);
		setRotationAngle(handrail_4_r3, -1.5708F, 1.1345F, -1.5708F);
		handrail_4_r3.texOffs(319, 0).addBox(2.725F, -17.975F, -11.0F, 0.0F, 2.0F, 0.0F, 0.2F, false);

		handrail_3_r3 = new ModelMapper(modelDataWrapper);
		handrail_3_r3.setPos(16.75F, -16.0F, -2.0F);
		ceiling2.addChild(handrail_3_r3);
		setRotationAngle(handrail_3_r3, -1.5708F, 0.4363F, -1.5708F);
		handrail_3_r3.texOffs(319, 0).addBox(12.3F, -10.275F, -11.0F, 0.0F, 1.0F, 0.0F, 0.2F, false);

		handrail_6_r2 = new ModelMapper(modelDataWrapper);
		handrail_6_r2.setPos(16.75F, -16.0F, -4.0F);
		ceiling2.addChild(handrail_6_r2);
		setRotationAngle(handrail_6_r2, 1.5708F, -1.1345F, -1.5708F);
		handrail_6_r2.texOffs(319, 0).addBox(2.725F, -17.975F, 11.0F, 0.0F, 2.0F, 0.0F, 0.2F, false);

		handrail_2_r2 = new ModelMapper(modelDataWrapper);
		handrail_2_r2.setPos(16.75F, -16.0F, -2.0F);
		ceiling2.addChild(handrail_2_r2);
		setRotationAngle(handrail_2_r2, -1.5708F, 0.0F, -1.5708F);
		handrail_2_r2.texOffs(319, 0).addBox(15.0F, -3.0F, -11.0F, 0.0F, 8.0F, 0.0F, 0.2F, false);

		head_exterior = new ModelMapper(modelDataWrapper);
		head_exterior.setPos(0.0F, 24.0F, 0.0F);
		head_exterior.texOffs(0, 14).addBox(20.0F, -14.0F, -10.0F, 0.0F, 14.0F, 20.0F, 0.0F, false);
		head_exterior.texOffs(0, 14).addBox(-20.0F, -14.0F, -10.0F, 0.0F, 14.0F, 20.0F, 0.0F, false);

		upper_wall_2_r5 = new ModelMapper(modelDataWrapper);
		upper_wall_2_r5.setPos(-21.0F, -14.0F, 0.0F);
		head_exterior.addChild(upper_wall_2_r5);
		setRotationAngle(upper_wall_2_r5, 0.0F, 0.0F, 0.1107F);
		upper_wall_2_r5.texOffs(154, 142).addBox(1.0F, -22.0F, -10.0F, 0.0F, 22.0F, 20.0F, 0.0F, false);

		upper_wall_1_r4 = new ModelMapper(modelDataWrapper);
		upper_wall_1_r4.setPos(21.0F, -14.0F, 0.0F);
		head_exterior.addChild(upper_wall_1_r4);
		setRotationAngle(upper_wall_1_r4, 0.0F, 0.0F, -0.1107F);
		upper_wall_1_r4.texOffs(154, 142).addBox(-1.0F, -22.0F, -10.0F, 0.0F, 22.0F, 20.0F, 0.0F, false);

		door_leaf_left = new ModelMapper(modelDataWrapper);
		door_leaf_left.setPos(0.0F, 0.0F, 0.0F);
		head_exterior.addChild(door_leaf_left);
		

		door_leaf_2_r2 = new ModelMapper(modelDataWrapper);
		door_leaf_2_r2.setPos(0.0F, 0.0F, -4.0F);
		door_leaf_left.addChild(door_leaf_2_r2);
		setRotationAngle(door_leaf_2_r2, -1.1345F, 0.0873F, -0.1107F);
		door_leaf_2_r2.texOffs(0, 7).addBox(21.9F, -10.725F, -32.525F, 1.0F, 2.0F, 1.0F, 0.0F, true);

		door_leaf_3_r1 = new ModelMapper(modelDataWrapper);
		door_leaf_3_r1.setPos(-0.1363F, 0.1932F, -3.8948F);
		door_leaf_left.addChild(door_leaf_3_r1);
		setRotationAngle(door_leaf_3_r1, -0.5236F, 0.1745F, -0.1107F);
		door_leaf_3_r1.texOffs(0, 4).addBox(22.25F, -26.775F, -19.95F, 1.0F, 2.0F, 1.0F, 0.0F, true);

		door_leaf_5_r1 = new ModelMapper(modelDataWrapper);
		door_leaf_5_r1.setPos(0.0F, 0.0F, -4.0F);
		door_leaf_left.addChild(door_leaf_5_r1);
		setRotationAngle(door_leaf_5_r1, -0.0873F, 0.1745F, -0.1107F);
		door_leaf_5_r1.texOffs(8, 49).addBox(22.275F, -32.675F, -6.65F, 1.0F, 2.0F, 2.0F, 0.0F, true);

		door_leaf_4_r1 = new ModelMapper(modelDataWrapper);
		door_leaf_4_r1.setPos(-0.0976F, 0.1654F, -3.8974F);
		door_leaf_left.addChild(door_leaf_4_r1);
		setRotationAngle(door_leaf_4_r1, -0.0873F, 0.2182F, -0.1107F);
		door_leaf_4_r1.texOffs(26, 8).addBox(22.4F, -30.95F, -6.6F, 1.0F, 2.0F, 3.0F, 0.0F, true);

		door_leaf_1_r2 = new ModelMapper(modelDataWrapper);
		door_leaf_1_r2.setPos(0.0F, 0.0F, 0.0F);
		door_leaf_left.addChild(door_leaf_1_r2);
		setRotationAngle(door_leaf_1_r2, 0.0F, -3.1416F, 0.1107F);
		door_leaf_1_r2.texOffs(146, 102).addBox(21.5F, -34.0F, -10.0F, 1.0F, 5.0F, 20.0F, 0.0F, true);

		door_leaf_right = new ModelMapper(modelDataWrapper);
		door_leaf_right.setPos(0.0F, 0.0F, 0.0F);
		head_exterior.addChild(door_leaf_right);
		

		door_leaf_5_r2 = new ModelMapper(modelDataWrapper);
		door_leaf_5_r2.setPos(0.0F, 0.0F, -4.0F);
		door_leaf_right.addChild(door_leaf_5_r2);
		setRotationAngle(door_leaf_5_r2, -0.0873F, -0.1745F, 0.1107F);
		door_leaf_5_r2.texOffs(8, 49).addBox(-23.275F, -32.675F, -6.65F, 1.0F, 2.0F, 2.0F, 0.0F, false);

		door_leaf_4_r2 = new ModelMapper(modelDataWrapper);
		door_leaf_4_r2.setPos(0.0976F, 0.1654F, -3.8974F);
		door_leaf_right.addChild(door_leaf_4_r2);
		setRotationAngle(door_leaf_4_r2, -0.0873F, -0.2182F, 0.1107F);
		door_leaf_4_r2.texOffs(26, 8).addBox(-23.4F, -30.95F, -6.6F, 1.0F, 2.0F, 3.0F, 0.0F, false);

		door_leaf_3_r2 = new ModelMapper(modelDataWrapper);
		door_leaf_3_r2.setPos(0.1363F, 0.1932F, -3.8948F);
		door_leaf_right.addChild(door_leaf_3_r2);
		setRotationAngle(door_leaf_3_r2, -0.5236F, -0.1745F, 0.1107F);
		door_leaf_3_r2.texOffs(0, 4).addBox(-23.25F, -26.775F, -19.95F, 1.0F, 2.0F, 1.0F, 0.0F, false);

		door_leaf_2_r3 = new ModelMapper(modelDataWrapper);
		door_leaf_2_r3.setPos(0.0F, 0.0F, -4.0F);
		door_leaf_right.addChild(door_leaf_2_r3);
		setRotationAngle(door_leaf_2_r3, -1.1345F, -0.0873F, 0.1107F);
		door_leaf_2_r3.texOffs(0, 7).addBox(-22.9F, -10.725F, -32.525F, 1.0F, 2.0F, 1.0F, 0.0F, false);

		door_leaf_1_r3 = new ModelMapper(modelDataWrapper);
		door_leaf_1_r3.setPos(0.0F, 0.0F, 0.0F);
		door_leaf_right.addChild(door_leaf_1_r3);
		setRotationAngle(door_leaf_1_r3, 0.0F, 3.1416F, -0.1107F);
		door_leaf_1_r3.texOffs(146, 102).addBox(-22.5F, -34.0F, -10.0F, 1.0F, 5.0F, 20.0F, 0.0F, false);

		front = new ModelMapper(modelDataWrapper);
		front.setPos(0.0F, 0.0F, 0.0F);
		head_exterior.addChild(front);
		front.texOffs(238, 237).addBox(-19.0F, -10.2433F, -19.4779F, 38.0F, 9.0F, 0.0F, 0.0F, false);

		front_panel_4_r1 = new ModelMapper(modelDataWrapper);
		front_panel_4_r1.setPos(2.0F, -29.8634F, -15.5386F);
		front.addChild(front_panel_4_r1);
		setRotationAngle(front_panel_4_r1, -0.2618F, 0.0F, 0.0F);
		front_panel_4_r1.texOffs(246, 0).addBox(-19.0F, -10.725F, -2.2F, 34.0F, 11.0F, 0.0F, 0.0F, false);

		front_panel_3_r1 = new ModelMapper(modelDataWrapper);
		front_panel_3_r1.setPos(0.0F, -15.8379F, -18.3109F);
		front.addChild(front_panel_3_r1);
		setRotationAngle(front_panel_3_r1, -0.0873F, 0.0F, 0.0F);
		front_panel_3_r1.texOffs(54, 164).addBox(-19.0F, -14.325F, -0.675F, 38.0F, 20.0F, 0.0F, 0.0F, false);

		front_panel_1_r1 = new ModelMapper(modelDataWrapper);
		front_panel_1_r1.setPos(0.0F, 2.2985F, -19.9864F);
		front.addChild(front_panel_1_r1);
		setRotationAngle(front_panel_1_r1, 0.3054F, 0.0F, 0.0F);
		front_panel_1_r1.texOffs(204, 33).addBox(-19.0F, -3.225F, 1.55F, 38.0F, 9.0F, 0.0F, 0.0F, false);

		side_1 = new ModelMapper(modelDataWrapper);
		side_1.setPos(0.0F, 0.0F, 0.0F);
		front.addChild(side_1);
		

		front_side_bottom_3_r1 = new ModelMapper(modelDataWrapper);
		front_side_bottom_3_r1.setPos(21.231F, 0.2171F, -25.0F);
		side_1.addChild(front_side_bottom_3_r1);
		setRotationAngle(front_side_bottom_3_r1, 0.0F, 0.0F, 0.1745F);
		front_side_bottom_3_r1.texOffs(124, 126).addBox(-1.25F, 0.0F, 15.0F, 0.0F, 8.0F, 15.0F, 0.0F, true);

		front_side_bottom_1_r1 = new ModelMapper(modelDataWrapper);
		front_side_bottom_1_r1.setPos(-21.2537F, 0.2195F, -25.1654F);
		side_1.addChild(front_side_bottom_1_r1);
		setRotationAngle(front_side_bottom_1_r1, 0.0F, -0.1309F, -0.1745F);
		front_side_bottom_1_r1.texOffs(124, 136).addBox(3.25F, 0.0F, 2.0F, 0.0F, 8.0F, 13.0F, 0.0F, false);

		front_side_lower_1_r1 = new ModelMapper(modelDataWrapper);
		front_side_lower_1_r1.setPos(-21.0F, 0.0F, -10.0F);
		side_1.addChild(front_side_lower_1_r1);
		setRotationAngle(front_side_lower_1_r1, 0.0F, -0.1309F, 0.0F);
		front_side_lower_1_r1.texOffs(0, 128).addBox(1.0F, -14.0F, -11.0F, 0.0F, 14.0F, 11.0F, 0.0F, false);

		front_side_upper_1_r1 = new ModelMapper(modelDataWrapper);
		front_side_upper_1_r1.setPos(-21.0F, -14.0F, -10.0F);
		side_1.addChild(front_side_upper_1_r1);
		setRotationAngle(front_side_upper_1_r1, 0.0F, -0.1309F, 0.1107F);
		front_side_upper_1_r1.texOffs(82, 47).addBox(1.0F, -23.0F, -11.0F, 0.0F, 23.0F, 11.0F, 0.0F, false);

		side_2 = new ModelMapper(modelDataWrapper);
		side_2.setPos(-21.0F, 0.0F, 9.0F);
		front.addChild(side_2);
		

		front_side_upper_2_r1 = new ModelMapper(modelDataWrapper);
		front_side_upper_2_r1.setPos(42.0F, -14.0F, -19.0F);
		side_2.addChild(front_side_upper_2_r1);
		setRotationAngle(front_side_upper_2_r1, 0.0F, 0.1309F, -0.1107F);
		front_side_upper_2_r1.texOffs(82, 47).addBox(-1.0F, -23.0F, -11.0F, 0.0F, 23.0F, 11.0F, 0.0F, true);

		front_side_lower_2_r1 = new ModelMapper(modelDataWrapper);
		front_side_lower_2_r1.setPos(42.0F, 0.0F, -19.0F);
		side_2.addChild(front_side_lower_2_r1);
		setRotationAngle(front_side_lower_2_r1, 0.0F, 0.1309F, 0.0F);
		front_side_lower_2_r1.texOffs(0, 128).addBox(-1.0F, -14.0F, -11.0F, 0.0F, 14.0F, 11.0F, 0.0F, true);

		front_side_bottom_4_r1 = new ModelMapper(modelDataWrapper);
		front_side_bottom_4_r1.setPos(-0.231F, 0.2169F, -34.0F);
		side_2.addChild(front_side_bottom_4_r1);
		setRotationAngle(front_side_bottom_4_r1, 0.0F, 0.0F, -0.1745F);
		front_side_bottom_4_r1.texOffs(124, 126).addBox(1.25F, 0.0F, 15.0F, 0.0F, 8.0F, 15.0F, 0.0F, false);

		front_side_bottom_2_r1 = new ModelMapper(modelDataWrapper);
		front_side_bottom_2_r1.setPos(42.2537F, 0.2194F, -34.1654F);
		side_2.addChild(front_side_bottom_2_r1);
		setRotationAngle(front_side_bottom_2_r1, 0.0F, 0.1309F, 0.1745F);
		front_side_bottom_2_r1.texOffs(124, 136).addBox(-3.25F, 0.0F, 2.0F, 0.0F, 8.0F, 13.0F, 0.0F, true);

		roof = new ModelMapper(modelDataWrapper);
		roof.setPos(-16.7054F, -37.098F, 5.0F);
		head_exterior.addChild(roof);
		roof.texOffs(246, 162).addBox(16.4343F, -4.7552F, -16.0F, 6.0F, 1.0F, 21.0F, 0.0F, true);
		roof.texOffs(246, 162).addBox(10.9765F, -4.7552F, -16.0F, 6.0F, 1.0F, 21.0F, 0.0F, false);

		outer_roof_3_r4 = new ModelMapper(modelDataWrapper);
		outer_roof_3_r4.setPos(0.0F, 0.0F, 0.0F);
		roof.addChild(outer_roof_3_r4);
		setRotationAngle(outer_roof_3_r4, 0.0F, 0.0F, -1.0472F);
		outer_roof_3_r4.texOffs(245, 252).addBox(-2.0F, -1.0F, -16.0F, 3.0F, 1.0F, 21.0F, 0.0F, false);

		outer_roof_5_r4 = new ModelMapper(modelDataWrapper);
		outer_roof_5_r4.setPos(7.211F, -3.0758F, 0.0F);
		roof.addChild(outer_roof_5_r4);
		setRotationAngle(outer_roof_5_r4, 0.0F, 0.0F, -0.1745F);
		outer_roof_5_r4.texOffs(86, 243).addBox(-4.0F, -1.0F, -16.0F, 8.0F, 1.0F, 21.0F, 0.0F, false);

		outer_roof_4_r4 = new ModelMapper(modelDataWrapper);
		outer_roof_4_r4.setPos(2.299F, -1.75F, 0.0F);
		roof.addChild(outer_roof_4_r4);
		setRotationAngle(outer_roof_4_r4, 0.0F, 0.0F, -0.5236F);
		outer_roof_4_r4.texOffs(30, 243).addBox(-2.5F, -1.0F, -16.0F, 4.0F, 1.0F, 21.0F, 0.0F, false);

		outer_roof_4_r5 = new ModelMapper(modelDataWrapper);
		outer_roof_4_r5.setPos(31.9778F, -1.25F, 0.0F);
		roof.addChild(outer_roof_4_r5);
		setRotationAngle(outer_roof_4_r5, 0.0F, 0.0F, 0.5236F);
		outer_roof_4_r5.texOffs(30, 243).addBox(-2.5F, -1.0F, -16.0F, 4.0F, 1.0F, 21.0F, 0.0F, true);

		outer_roof_5_r5 = new ModelMapper(modelDataWrapper);
		outer_roof_5_r5.setPos(26.1999F, -3.0758F, 0.0F);
		roof.addChild(outer_roof_5_r5);
		setRotationAngle(outer_roof_5_r5, 0.0F, 0.0F, 0.1745F);
		outer_roof_5_r5.texOffs(86, 243).addBox(-4.0F, -1.0F, -16.0F, 8.0F, 1.0F, 21.0F, 0.0F, true);

		outer_roof_3_r5 = new ModelMapper(modelDataWrapper);
		outer_roof_3_r5.setPos(33.4108F, 0.0F, 0.0F);
		roof.addChild(outer_roof_3_r5);
		setRotationAngle(outer_roof_3_r5, 0.0F, 0.0F, 1.0472F);
		outer_roof_3_r5.texOffs(245, 252).addBox(-1.0F, -1.0F, -16.0F, 3.0F, 1.0F, 21.0F, 0.0F, true);

		vent_top_r1 = new ModelMapper(modelDataWrapper);
		vent_top_r1.setPos(16.7054F, 37.098F, 48.0F);
		roof.addChild(vent_top_r1);
		setRotationAngle(vent_top_r1, -3.1416F, 0.0F, 3.1416F);
		vent_top_r1.texOffs(0, 49).addBox(-8.0F, -43.0F, 0.0F, 16.0F, 2.0F, 48.0F, 0.0F, false);

		vent_2_r2 = new ModelMapper(modelDataWrapper);
		vent_2_r2.setPos(24.7054F, -5.902F, 48.0F);
		roof.addChild(vent_2_r2);
		setRotationAngle(vent_2_r2, -3.1416F, 0.0F, -2.7925F);
		vent_2_r2.texOffs(80, 91).addBox(-9.0F, 0.0F, 0.0F, 9.0F, 2.0F, 48.0F, 0.0F, false);

		vent_1_r2 = new ModelMapper(modelDataWrapper);
		vent_1_r2.setPos(8.7054F, -5.902F, 48.0F);
		roof.addChild(vent_1_r2);
		setRotationAngle(vent_1_r2, 3.1416F, 0.0F, 2.7925F);
		vent_1_r2.texOffs(138, 0).addBox(0.0F, 0.0F, 0.0F, 9.0F, 2.0F, 48.0F, 0.0F, false);

		outer_roof_5_r6 = new ModelMapper(modelDataWrapper);
		outer_roof_5_r6.setPos(4.2219F, 2.4375F, -24.6099F);
		roof.addChild(outer_roof_5_r6);
		setRotationAngle(outer_roof_5_r6, 2.7925F, 0.0F, 2.0944F);
		outer_roof_5_r6.texOffs(34, 40).addBox(-2.0F, -2.575F, -10.1F, 4.0F, 0.0F, 6.0F, 0.0F, false);

		outer_roof_6_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_6_r1.setPos(4.1132F, 1.3922F, -24.6726F);
		roof.addChild(outer_roof_6_r1);
		setRotationAngle(outer_roof_6_r1, 2.8798F, 0.0F, 2.618F);
		outer_roof_6_r1.texOffs(0, 5).addBox(-1.5F, -2.225F, -9.575F, 4.0F, 0.0F, 5.0F, 0.0F, true);

		outer_roof_6_r2 = new ModelMapper(modelDataWrapper);
		outer_roof_6_r2.setPos(8.7024F, 5.3828F, -8.4155F);
		roof.addChild(outer_roof_6_r2);
		setRotationAngle(outer_roof_6_r2, 0.3054F, 0.0F, -0.1745F);
		outer_roof_6_r2.texOffs(10, 49).addBox(-5.0F, -11.425F, -10.35F, 9.0F, 0.0F, 6.0F, 0.0F, false);

		outer_roof_7_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_7_r1.setPos(16.9765F, 78.394F, 0.0461F);
		roof.addChild(outer_roof_7_r1);
		setRotationAngle(outer_roof_7_r1, 0.3054F, 0.0F, 0.0F);
		outer_roof_7_r1.texOffs(16, 0).addBox(-6.0F, -84.125F, 4.7F, 6.0F, 0.0F, 5.0F, 0.0F, false);

		outer_roof_8_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_8_r1.setPos(16.4343F, 78.394F, 0.0461F);
		roof.addChild(outer_roof_8_r1);
		setRotationAngle(outer_roof_8_r1, 0.3054F, 0.0F, 0.0F);
		outer_roof_8_r1.texOffs(16, 0).addBox(0.0F, -84.125F, 4.7F, 6.0F, 0.0F, 5.0F, 0.0F, true);

		outer_roof_7_r2 = new ModelMapper(modelDataWrapper);
		outer_roof_7_r2.setPos(24.7084F, 5.3828F, -8.4155F);
		roof.addChild(outer_roof_7_r2);
		setRotationAngle(outer_roof_7_r2, 0.3054F, 0.0F, 0.1745F);
		outer_roof_7_r2.texOffs(10, 49).addBox(-4.0F, -11.425F, -10.35F, 9.0F, 0.0F, 6.0F, 0.0F, true);

		outer_roof_5_r7 = new ModelMapper(modelDataWrapper);
		outer_roof_5_r7.setPos(29.2976F, 1.3922F, -24.6726F);
		roof.addChild(outer_roof_5_r7);
		setRotationAngle(outer_roof_5_r7, 2.8798F, 0.0F, -2.618F);
		outer_roof_5_r7.texOffs(0, 5).addBox(-2.5F, -2.225F, -9.575F, 4.0F, 0.0F, 5.0F, 0.0F, false);

		outer_roof_6_r3 = new ModelMapper(modelDataWrapper);
		outer_roof_6_r3.setPos(29.1889F, 2.4375F, -24.6099F);
		roof.addChild(outer_roof_6_r3);
		setRotationAngle(outer_roof_6_r3, 2.7925F, 0.0F, -2.0944F);
		outer_roof_6_r3.texOffs(34, 40).addBox(-2.0F, -2.575F, -10.1F, 4.0F, 0.0F, 6.0F, 0.0F, true);

		headlights = new ModelMapper(modelDataWrapper);
		headlights.setPos(0.0F, 24.0F, 0.0F);
		setRotationAngle(headlights, 0.0175F, 0.0F, 0.0F);
		headlights.texOffs(0, 57).addBox(7.75F, -11.2727F, -19.3395F, 7.0F, 4.0F, 0.0F, 0.0F, false);
		headlights.texOffs(0, 57).addBox(-14.75F, -11.2727F, -19.3395F, 7.0F, 4.0F, 0.0F, 0.0F, true);

		tail_lights = new ModelMapper(modelDataWrapper);
		tail_lights.setPos(0.0F, 24.0F, 0.0F);
		setRotationAngle(tail_lights, 0.0175F, 0.0F, 0.0F);
		tail_lights.texOffs(0, 243).addBox(7.75F, -15.1477F, -19.4395F, 14.0F, 13.0F, 0.0F, 0.0F, false);
		tail_lights.texOffs(0, 243).addBox(-21.75F, -15.1477F, -19.4395F, 14.0F, 13.0F, 0.0F, 0.0F, true);

		door_light_on = new ModelMapper(modelDataWrapper);
		door_light_on.setPos(0.0F, 24.0F, 0.0F);
		setRotationAngle(door_light_on, 0.0F, 0.0F, -0.3491F);
		

		light_r1 = new ModelMapper(modelDataWrapper);
		light_r1.setPos(-8.3706F, -42.5895F, 0.0F);
		door_light_on.addChild(light_r1);
		setRotationAngle(light_r1, 0.0F, 0.0F, -1.0472F);
		light_r1.texOffs(6, 0).addBox(-1.3618F, 3.7941F, 0.0F, 0.0F, 0.0F, 0.0F, 0.3F, false);

		door_light_off = new ModelMapper(modelDataWrapper);
		door_light_off.setPos(0.0F, 24.0F, 0.0F);
		setRotationAngle(door_light_off, 0.0F, 0.0F, -0.3491F);
		

		light_r2 = new ModelMapper(modelDataWrapper);
		light_r2.setPos(-8.3706F, -42.5895F, 0.0F);
		door_light_off.addChild(light_r2);
		setRotationAngle(light_r2, 0.0F, 0.0F, -1.0472F);
		light_r2.texOffs(6, 3).addBox(-1.3618F, 3.7941F, 0.0F, 0.0F, 0.0F, 0.0F, 0.3F, false);

		modelDataWrapper.setModelPart(textureWidth, textureHeight);
		window.setModelPart();
		window_exterior_1.setModelPart();
		window_exterior_2.setModelPart();
		side_panel_translucent.setModelPart();
		roof_window.setModelPart();
		roof_door.setModelPart();
		roof_exterior.setModelPart();
		door.setModelPart();
		door_left.setModelPart(door.name);
		door_right.setModelPart(door.name);
		door_exterior_1.setModelPart();
		door_left_exterior_1.setModelPart(door_exterior_1.name);
		door_right_exterior_1.setModelPart(door_exterior_1.name);
		door_exterior_2.setModelPart();
		door_left_exterior_2.setModelPart(door_exterior_2.name);
		door_right_exterior_2.setModelPart(door_exterior_2.name);
		end.setModelPart();
		end_exterior.setModelPart();
		roof_end_exterior.setModelPart();
		roof_window_light.setModelPart();
		roof_end_light.setModelPart();
		head.setModelPart();
		head_exterior.setModelPart();
		headlights.setModelPart();
		tail_lights.setModelPart();
		door_light_on.setModelPart();
		door_light_off.setModelPart();
	}

	private static final int DOOR_MAX = 13;
	private static final ModelDoorOverlay MODEL_DOOR_OVERLAY = new ModelDoorOverlay(DOOR_MAX, 6.34F, "door_overlay_c_train_left.png", "door_overlay_c_train_right.png");

	@Override
	protected void renderWindowPositions(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
		switch (renderStage) {
			case LIGHTS:
				renderMirror(roof_window_light, matrices, vertices, light, position);
				break;
			case INTERIOR:
				renderMirror(window, matrices, vertices, light, position);
				if (renderDetails) {
					renderMirror(roof_window, matrices, vertices, light, position);
				}
				break;
			case INTERIOR_TRANSLUCENT:
				renderMirror(side_panel_translucent, matrices, vertices, light, position - 21.5F);
				renderMirror(side_panel_translucent, matrices, vertices, light, position + 21.5F);
				break;
			case EXTERIOR:
				if (isEnd2Head) {
					renderOnceFlipped(window_exterior_1, matrices, vertices, light, position);
					renderOnceFlipped(window_exterior_2, matrices, vertices, light, position);
				} else {
					renderOnce(window_exterior_1, matrices, vertices, light, position);
					renderOnce(window_exterior_2, matrices, vertices, light, position);
				}
				renderMirror(roof_exterior, matrices, vertices, light, position);
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
					renderMirror(roof_window_light, matrices, vertices, light, position);
				}
				if (middleDoor && doorOpen && renderDetails) {
					renderMirror(door_light_on, matrices, vertices, light, position - 40);
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
					renderMirror(roof_door, matrices, vertices, light, position);
					renderOnce(door_handrail, matrices, vertices, light, position);
				}
				break;
			case EXTERIOR:
				if (isEnd2Head) {
					door_left_exterior_2.setOffset(0, 0, doorLeftZ);
					door_right_exterior_2.setOffset(0, 0, -doorLeftZ);
					renderOnceFlipped(door_exterior_2, matrices, vertices, light, position);
					door_left_exterior_1.setOffset(0, 0, -doorRightZ);
					door_right_exterior_1.setOffset(0, 0, doorRightZ);
					renderOnceFlipped(door_exterior_1, matrices, vertices, light, position);
				} else {
					door_left_exterior_1.setOffset(0, 0, -doorLeftZ);
					door_right_exterior_1.setOffset(0, 0, doorLeftZ);
					renderOnce(door_exterior_1, matrices, vertices, light, position);
					door_left_exterior_2.setOffset(0, 0, doorRightZ);
					door_right_exterior_2.setOffset(0, 0, -doorRightZ);
					renderOnce(door_exterior_2, matrices, vertices, light, position);
				}
				renderMirror(roof_exterior, matrices, vertices, light, position);
				if (middleDoor && !doorOpen && renderDetails) {
					renderMirror(door_light_off, matrices, vertices, light, position - 40);
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
				break;
			case EXTERIOR:
				renderOnce(head_exterior, matrices, vertices, light, position);
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
				break;
			case EXTERIOR:
				renderOnceFlipped(head_exterior, matrices, vertices, light, position);
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
	protected float getDoorAnimationX(float value, boolean opening) {
		return 0;
	}

	@Override
	protected float getDoorAnimationZ(float value, boolean opening) {
		return smoothEnds(0, DOOR_MAX, 0, 0.5F, value);
	}
}