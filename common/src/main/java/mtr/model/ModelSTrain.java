package mtr.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mtr.client.DoorAnimationType;
import mtr.mappings.ModelDataWrapper;
import mtr.mappings.ModelMapper;

public class ModelSTrain extends ModelSimpleTrainBase<ModelSTrain> {

	private final ModelMapper window;
	private final ModelMapper upper_wall_2_r1;
	private final ModelMapper window_handrails;
	private final ModelMapper top_handrail_5_r1;
	private final ModelMapper top_handrail_4_r1;
	private final ModelMapper top_handrail_3_r1;
	private final ModelMapper top_handrail_2_r1;
	private final ModelMapper top_handrail_1_r1;
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
	private final ModelMapper pole_bottom_diagonal_3_r1;
	private final ModelMapper pole_bottom_diagonal_2_r1;
	private final ModelMapper pole_bottom_diagonal_1_r1;
	private final ModelMapper pole_middle_3_r1;
	private final ModelMapper pole_middle_2_r1;
	private final ModelMapper pole_middle_1_r1;
	private final ModelMapper pole_top_diagonal_3_r1;
	private final ModelMapper pole_top_diagonal_2_r1;
	private final ModelMapper pole_top_diagonal_1_r1;
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
	private final ModelMapper roof_end_exterior;
	private final ModelMapper vent_2_r1;
	private final ModelMapper vent_1_r1;
	private final ModelMapper outer_roof_1;
	private final ModelMapper outer_roof_5_r2;
	private final ModelMapper outer_roof_4_r2;
	private final ModelMapper outer_roof_3_r2;
	private final ModelMapper outer_roof_2;
	private final ModelMapper outer_roof_6_r1;
	private final ModelMapper outer_roof_5_r3;
	private final ModelMapper outer_roof_4_r3;
	private final ModelMapper roof_window_light;
	private final ModelMapper light_2_r1;
	private final ModelMapper light_1_r1;
	private final ModelMapper roof_door_light;
	private final ModelMapper light_3_r1;
	private final ModelMapper light_2_r2;
	private final ModelMapper roof_end_light;
	private final ModelMapper light_3_r2;
	private final ModelMapper light_2_r3;
	private final ModelMapper light_3_r3;
	private final ModelMapper light_2_r4;
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
	private final ModelMapper ceiling3;
	private final ModelMapper handrail_8_r1;
	private final ModelMapper handrail_5_r3;
	private final ModelMapper handrail_6_r2;
	private final ModelMapper handrail_7_r2;
	private final ModelMapper handrail_4_r3;
	private final ModelMapper head_exterior;
	private final ModelMapper upper_wall_2_r5;
	private final ModelMapper upper_wall_1_r4;
	private final ModelMapper door_leaf_4_r1;
	private final ModelMapper door_leaf_1_r2;
	private final ModelMapper door_leaf_5_r1;
	private final ModelMapper door_leaf_2_r2;
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
	private final ModelMapper outer_roof_6_r2;
	private final ModelMapper outer_roof_5_r4;
	private final ModelMapper outer_roof_4_r4;
	private final ModelMapper outer_roof_5_r5;
	private final ModelMapper outer_roof_4_r5;
	private final ModelMapper outer_roof_3_r3;
	private final ModelMapper vent_top_r1;
	private final ModelMapper vent_2_r2;
	private final ModelMapper vent_1_r2;
	private final ModelMapper outer_roof_6_r3;
	private final ModelMapper outer_roof_7_r1;
	private final ModelMapper outer_roof_7_r2;
	private final ModelMapper outer_roof_8_r1;
	private final ModelMapper outer_roof_5_r6;
	private final ModelMapper outer_roof_6_r4;
	private final ModelMapper outer_roof_6_r5;
	private final ModelMapper outer_roof_7_r3;
	private final ModelMapper headlights;
	private final ModelMapper tail_lights;
	private final ModelMapper door_light_on;
	private final ModelMapper light_r1;
	private final ModelMapper door_light_off;
	private final ModelMapper light_r2;

	public ModelSTrain() {
		this(DoorAnimationType.STANDARD, true);
	}

	protected ModelSTrain(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		super(doorAnimationType, renderDoorOverlay);
		final int textureWidth = 320;
		final int textureHeight = 320;

		final ModelDataWrapper modelDataWrapper = new ModelDataWrapper(this, textureWidth, textureHeight);

		window = new ModelMapper(modelDataWrapper);
		window.setPos(0, 24, 0);
		window.texOffs(0, 0).addBox(-20, 0, -24, 20, 1, 48, 0, false);
		window.texOffs(80, 43).addBox(-18, -5, -21, 0, 5, 42, 0, false);
		window.texOffs(124, 204).addBox(-20, -14, 21, 3, 14, 6, 0, false);
		window.texOffs(130, 164).addBox(-20, -14, -27, 3, 14, 6, 0, false);

		upper_wall_2_r1 = new ModelMapper(modelDataWrapper);
		upper_wall_2_r1.setPos(-20, -14, 0);
		window.addChild(upper_wall_2_r1);
		setRotationAngle(upper_wall_2_r1, 0, 0, 0.1107F);
		upper_wall_2_r1.texOffs(72, 196).addBox(0, -19, -27, 3, 19, 6, 0, false);
		upper_wall_2_r1.texOffs(245, 205).addBox(0, -19, 21, 3, 19, 6, 0, false);
		upper_wall_2_r1.texOffs(106, 141).addBox(0, -19, -22, 2, 19, 44, 0, false);

		window_handrails = new ModelMapper(modelDataWrapper);
		window_handrails.setPos(0, 24, 0);
		window_handrails.texOffs(156, 162).addBox(-18, -6, -21, 7, 1, 42, 0, false);
		window_handrails.texOffs(16, 55).addBox(-18, -11, -22, 7, 6, 1, 0, false);
		window_handrails.texOffs(16, 55).addBox(-18, -11, 21, 7, 6, 1, 0, false);
		window_handrails.texOffs(319, 1).addBox(0, -35.2F, -13, 0, 35, 0, 0.2F, false);
		window_handrails.texOffs(319, 1).addBox(0, -35.2F, 13, 0, 35, 0, 0.2F, false);
		window_handrails.texOffs(0, 0).addBox(-8, -33, 18, 2, 4, 0, 0, false);
		window_handrails.texOffs(0, 0).addBox(-8, -33, 12, 2, 4, 0, 0, false);
		window_handrails.texOffs(0, 0).addBox(-8, -33, 6, 2, 4, 0, 0, false);
		window_handrails.texOffs(0, 0).addBox(-8, -33, -6, 2, 4, 0, 0, false);
		window_handrails.texOffs(0, 0).addBox(-8, -33, -12, 2, 4, 0, 0, false);
		window_handrails.texOffs(0, 0).addBox(-8, -33, -18, 2, 4, 0, 0, false);
		window_handrails.texOffs(319, 0).addBox(-7, -34.5F, -10, 0, 2, 0, 0.2F, true);
		window_handrails.texOffs(319, 0).addBox(-7, -34.5F, 10, 0, 2, 0, 0.2F, true);
		window_handrails.texOffs(319, 2).addBox(-11, -34.2F, -22, 0, 29, 0, 0.2F, false);
		window_handrails.texOffs(319, 0).addBox(-11, -34.2F, 22, 0, 29, 0, 0.2F, false);

		top_handrail_5_r1 = new ModelMapper(modelDataWrapper);
		top_handrail_5_r1.setPos(0, 0, 0);
		window_handrails.addChild(top_handrail_5_r1);
		setRotationAngle(top_handrail_5_r1, 1.5708F, 0, 0);
		top_handrail_5_r1.texOffs(319, 22).addBox(-7, -20, 32.5F, 0, 40, 0, 0.2F, false);

		top_handrail_4_r1 = new ModelMapper(modelDataWrapper);
		top_handrail_4_r1.setPos(-7, -33.7062F, 21.5892F);
		window_handrails.addChild(top_handrail_4_r1);
		setRotationAngle(top_handrail_4_r1, -0.5236F, 0, 0);
		top_handrail_4_r1.texOffs(319, 0).addBox(0, -1.5F, 0, 0, 2, 0, 0.2F, true);

		top_handrail_3_r1 = new ModelMapper(modelDataWrapper);
		top_handrail_3_r1.setPos(-6.8F, -32.3F, 20.2F);
		window_handrails.addChild(top_handrail_3_r1);
		setRotationAngle(top_handrail_3_r1, -1.0472F, 0, 0);
		top_handrail_3_r1.texOffs(319, 0).addBox(-0.2F, -1.2F, -0.2F, 0, 1, 0, 0.2F, true);

		top_handrail_2_r1 = new ModelMapper(modelDataWrapper);
		top_handrail_2_r1.setPos(-6.8F, -32.3F, -20.2F);
		window_handrails.addChild(top_handrail_2_r1);
		setRotationAngle(top_handrail_2_r1, 1.0472F, 0, 0);
		top_handrail_2_r1.texOffs(319, 0).addBox(-0.2F, -1.2F, 0.2F, 0, 1, 0, 0.2F, true);

		top_handrail_1_r1 = new ModelMapper(modelDataWrapper);
		top_handrail_1_r1.setPos(-7, -34.1392F, -21.8392F);
		window_handrails.addChild(top_handrail_1_r1);
		setRotationAngle(top_handrail_1_r1, 0.5236F, 0, 0);
		top_handrail_1_r1.texOffs(319, 0).addBox(0, -1, 0, 0, 2, 0, 0.2F, true);

		seat_back_r1 = new ModelMapper(modelDataWrapper);
		seat_back_r1.setPos(-17, -6, 0);
		window_handrails.addChild(seat_back_r1);
		setRotationAngle(seat_back_r1, 0, 0, -0.0524F);
		seat_back_r1.texOffs(146, 85).addBox(-1, -8, -22, 1, 8, 44, 0, false);

		window_exterior_1 = new ModelMapper(modelDataWrapper);
		window_exterior_1.setPos(0, 24, 0);
		window_exterior_1.texOffs(0, 69).addBox(-20, -14, -26, 0, 18, 52, 0, false);

		door_leaf_r1 = new ModelMapper(modelDataWrapper);
		door_leaf_r1.setPos(-21, -14, 0);
		window_exterior_1.addChild(door_leaf_r1);
		setRotationAngle(door_leaf_r1, 0, 0, 0.1107F);
		door_leaf_r1.texOffs(0, 139).addBox(0, -23, -26, 1, 5, 52, 0, false);

		upper_wall_r1 = new ModelMapper(modelDataWrapper);
		upper_wall_r1.setPos(-20, -14, 0);
		window_exterior_1.addChild(upper_wall_r1);
		setRotationAngle(upper_wall_r1, 0, 0, 0.1107F);
		upper_wall_r1.texOffs(0, 47).addBox(0, -22, -26, 0, 22, 52, 0, false);

		window_exterior_2 = new ModelMapper(modelDataWrapper);
		window_exterior_2.setPos(0, 24, 0);
		window_exterior_2.texOffs(0, 69).addBox(20, -14, -26, 0, 18, 52, 0, true);

		door_leaf_r2 = new ModelMapper(modelDataWrapper);
		door_leaf_r2.setPos(21, -14, 0);
		window_exterior_2.addChild(door_leaf_r2);
		setRotationAngle(door_leaf_r2, 0, 0, -0.1107F);
		door_leaf_r2.texOffs(0, 139).addBox(-1, -23, -26, 1, 5, 52, 0, true);

		upper_wall_r2 = new ModelMapper(modelDataWrapper);
		upper_wall_r2.setPos(20, -14, 0);
		window_exterior_2.addChild(upper_wall_r2);
		setRotationAngle(upper_wall_r2, 0, 0, -0.1107F);
		upper_wall_r2.texOffs(0, 47).addBox(0, -22, -26, 0, 22, 52, 0, true);

		side_panel_translucent = new ModelMapper(modelDataWrapper);
		side_panel_translucent.setPos(0, 24, 0);
		side_panel_translucent.texOffs(152, 90).addBox(-18, -25, 0, 7, 15, 0, 0, false);

		roof_window = new ModelMapper(modelDataWrapper);
		roof_window.setPos(0, 24, 0);
		roof_window.texOffs(60, 0).addBox(-16, -32, -24, 3, 0, 48, 0, false);
		roof_window.texOffs(55, 0).addBox(-11.0724F, -34.2978F, -24, 1, 0, 48, 0, false);
		roof_window.texOffs(40, 0).addBox(-4, -34.5F, -24, 4, 0, 48, 0, false);

		inner_roof_5_r1 = new ModelMapper(modelDataWrapper);
		inner_roof_5_r1.setPos(-3, -34.5F, 0);
		roof_window.addChild(inner_roof_5_r1);
		setRotationAngle(inner_roof_5_r1, 0, 0, -0.0873F);
		inner_roof_5_r1.texOffs(54, 0).addBox(-3, 0, -24, 3, 0, 48, 0, false);

		inner_roof_4_r1 = new ModelMapper(modelDataWrapper);
		inner_roof_4_r1.setPos(-6.9734F, -34.0649F, 0);
		roof_window.addChild(inner_roof_4_r1);
		setRotationAngle(inner_roof_4_r1, 0, 0, -0.1745F);
		inner_roof_4_r1.texOffs(66, 0).addBox(-1, 0, -24, 2, 0, 48, 0, false);

		inner_roof_2_r1 = new ModelMapper(modelDataWrapper);
		inner_roof_2_r1.setPos(-13, -32, 0);
		roof_window.addChild(inner_roof_2_r1);
		setRotationAngle(inner_roof_2_r1, 0, 0, -0.8727F);
		inner_roof_2_r1.texOffs(60, 0).addBox(0, 0, -24, 3, 0, 48, 0, false);

		roof_door = new ModelMapper(modelDataWrapper);
		roof_door.setPos(0, 24, 0);
		roof_door.texOffs(204, 0).addBox(-18, -33, -16, 5, 1, 32, 0, false);
		roof_door.texOffs(80, 16).addBox(-11.0724F, -34.2978F, -16, 1, 0, 32, 0, false);
		roof_door.texOffs(56, 0).addBox(-4, -34.5F, -16, 4, 0, 32, 0, false);

		inner_roof_5_r2 = new ModelMapper(modelDataWrapper);
		inner_roof_5_r2.setPos(-3, -34.5F, 0);
		roof_door.addChild(inner_roof_5_r2);
		setRotationAngle(inner_roof_5_r2, 0, 0, -0.0873F);
		inner_roof_5_r2.texOffs(70, 0).addBox(-3, 0, -16, 3, 0, 32, 0, false);

		inner_roof_4_r2 = new ModelMapper(modelDataWrapper);
		inner_roof_4_r2.setPos(-6.9733F, -34.0649F, 0);
		roof_door.addChild(inner_roof_4_r2);
		setRotationAngle(inner_roof_4_r2, 0, 0, -0.1745F);
		inner_roof_4_r2.texOffs(82, 0).addBox(-1, 0, -16, 2, 0, 32, 0, false);

		inner_roof_2_r2 = new ModelMapper(modelDataWrapper);
		inner_roof_2_r2.setPos(-13, -32, 0);
		roof_door.addChild(inner_roof_2_r2);
		setRotationAngle(inner_roof_2_r2, 0, 0, -0.8727F);
		inner_roof_2_r2.texOffs(114, 90).addBox(0, 0, -16, 3, 0, 32, 0, false);

		roof_exterior = new ModelMapper(modelDataWrapper);
		roof_exterior.setPos(0, 24, 0);
		roof_exterior.texOffs(64, 99).addBox(-6, -41, -20, 6, 0, 40, 0, false);

		outer_roof_5_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_5_r1.setPos(-6, -41, 0);
		roof_exterior.addChild(outer_roof_5_r1);
		setRotationAngle(outer_roof_5_r1, 0, 0, -0.1745F);
		outer_roof_5_r1.texOffs(98, 0).addBox(-8, 0, -20, 8, 0, 40, 0, false);

		outer_roof_4_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_4_r1.setPos(-15.6102F, -38.6109F, 0);
		roof_exterior.addChild(outer_roof_4_r1);
		setRotationAngle(outer_roof_4_r1, 0, 0, -0.5236F);
		outer_roof_4_r1.texOffs(0, 0).addBox(-2, 0, -20, 4, 0, 40, 0, false);

		outer_roof_3_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_3_r1.setPos(-17.8419F, -36.7453F, 0);
		roof_exterior.addChild(outer_roof_3_r1);
		setRotationAngle(outer_roof_3_r1, 0, 0, -1.0472F);
		outer_roof_3_r1.texOffs(0, 49).addBox(-1, 0, -20, 2, 0, 40, 0, false);

		door = new ModelMapper(modelDataWrapper);
		door.setPos(0, 24, 0);
		door.texOffs(178, 50).addBox(-20, 0, -16, 20, 1, 32, 0, false);

		door_left = new ModelMapper(modelDataWrapper);
		door_left.setPos(0, 0, 0);
		door.addChild(door_left);
		door_left.texOffs(94, 265).addBox(-21, -14, 0, 1, 14, 14, 0, false);

		door_left_top_r1 = new ModelMapper(modelDataWrapper);
		door_left_top_r1.setPos(-21, -14, 0);
		door_left.addChild(door_left_top_r1);
		setRotationAngle(door_left_top_r1, 0, 0, 0.1107F);
		door_left_top_r1.texOffs(192, 83).addBox(0, -19, 0, 1, 19, 14, 0, false);

		door_right = new ModelMapper(modelDataWrapper);
		door_right.setPos(0, 0, 0);
		door.addChild(door_right);
		door_right.texOffs(204, 0).addBox(-21, -14, -14, 1, 14, 14, 0, false);

		door_right_top_r1 = new ModelMapper(modelDataWrapper);
		door_right_top_r1.setPos(-21, -14, 0);
		door_right.addChild(door_right_top_r1);
		setRotationAngle(door_right_top_r1, 0, 0, 0.1107F);
		door_right_top_r1.texOffs(0, 49).addBox(0, -19, -14, 1, 19, 14, 0, false);

		door_handrail = new ModelMapper(modelDataWrapper);
		door_handrail.setPos(0, 24, 0);
		door_handrail.texOffs(319, 0).addBox(0, -34.6F, 0, 0, 5, 0, 0.2F, false);
		door_handrail.texOffs(319, 24).addBox(0, -11.2F, 0, 0, 11, 0, 0.2F, false);

		pole_bottom_diagonal_3_r1 = new ModelMapper(modelDataWrapper);
		pole_bottom_diagonal_3_r1.setPos(-0.1712F, -12.5316F, 0.1712F);
		door_handrail.addChild(pole_bottom_diagonal_3_r1);
		setRotationAngle(pole_bottom_diagonal_3_r1, -0.096F, -0.7854F, 0);
		pole_bottom_diagonal_3_r1.texOffs(319, 0).addBox(0, -2.5F, 0, 0, 5, 0, 0.2F, false);

		pole_bottom_diagonal_2_r1 = new ModelMapper(modelDataWrapper);
		pole_bottom_diagonal_2_r1.setPos(0.2339F, -12.5316F, 0.0627F);
		door_handrail.addChild(pole_bottom_diagonal_2_r1);
		setRotationAngle(pole_bottom_diagonal_2_r1, -0.096F, 1.309F, 0);
		pole_bottom_diagonal_2_r1.texOffs(319, 0).addBox(0, -2.5F, 0, 0, 5, 0, 0.2F, false);

		pole_bottom_diagonal_1_r1 = new ModelMapper(modelDataWrapper);
		pole_bottom_diagonal_1_r1.setPos(-0.0627F, -12.5316F, -0.2339F);
		door_handrail.addChild(pole_bottom_diagonal_1_r1);
		setRotationAngle(pole_bottom_diagonal_1_r1, -0.096F, -2.8798F, 0);
		pole_bottom_diagonal_1_r1.texOffs(319, 0).addBox(0, -2.5F, 0, 0, 5, 0, 0.2F, false);

		pole_middle_3_r1 = new ModelMapper(modelDataWrapper);
		pole_middle_3_r1.setPos(0, 0, 0);
		door_handrail.addChild(pole_middle_3_r1);
		setRotationAngle(pole_middle_3_r1, 0, 1.309F, 0);
		pole_middle_3_r1.texOffs(319, 11).addBox(0, -25.4F, 0.5F, 0, 10, 0, 0.2F, false);

		pole_middle_2_r1 = new ModelMapper(modelDataWrapper);
		pole_middle_2_r1.setPos(0, 0, 0);
		door_handrail.addChild(pole_middle_2_r1);
		setRotationAngle(pole_middle_2_r1, 0, -2.8798F, 0);
		pole_middle_2_r1.texOffs(319, 11).addBox(0, -25.4F, 0.5F, 0, 10, 0, 0.2F, false);

		pole_middle_1_r1 = new ModelMapper(modelDataWrapper);
		pole_middle_1_r1.setPos(0, 0, 0);
		door_handrail.addChild(pole_middle_1_r1);
		setRotationAngle(pole_middle_1_r1, 0, -0.7854F, 0);
		pole_middle_1_r1.texOffs(319, 11).addBox(0, -25.4F, 0.5F, 0, 10, 0, 0.2F, false);

		pole_top_diagonal_3_r1 = new ModelMapper(modelDataWrapper);
		pole_top_diagonal_3_r1.setPos(-0.1712F, -28.2684F, 0.1712F);
		door_handrail.addChild(pole_top_diagonal_3_r1);
		setRotationAngle(pole_top_diagonal_3_r1, 0.096F, -0.7854F, 0);
		pole_top_diagonal_3_r1.texOffs(319, 0).addBox(0, -2.5F, 0, 0, 5, 0, 0.2F, false);

		pole_top_diagonal_2_r1 = new ModelMapper(modelDataWrapper);
		pole_top_diagonal_2_r1.setPos(0.2339F, -28.2684F, 0.0627F);
		door_handrail.addChild(pole_top_diagonal_2_r1);
		setRotationAngle(pole_top_diagonal_2_r1, 0.096F, 1.309F, 0);
		pole_top_diagonal_2_r1.texOffs(319, 0).addBox(0, -2.5F, 0, 0, 5, 0, 0.2F, false);

		pole_top_diagonal_1_r1 = new ModelMapper(modelDataWrapper);
		pole_top_diagonal_1_r1.setPos(-0.0627F, -28.2684F, -0.2339F);
		door_handrail.addChild(pole_top_diagonal_1_r1);
		setRotationAngle(pole_top_diagonal_1_r1, 0.096F, -2.8798F, 0);
		pole_top_diagonal_1_r1.texOffs(319, 0).addBox(0, -2.5F, 0, 0, 5, 0, 0.2F, false);

		door_exterior_1 = new ModelMapper(modelDataWrapper);
		door_exterior_1.setPos(0, 24, 0);
		door_exterior_1.texOffs(100, 18).addBox(-21, 0, -18, 1, 4, 36, 0, false);

		door_leaf_r3 = new ModelMapper(modelDataWrapper);
		door_leaf_r3.setPos(-21, -14, 0);
		door_exterior_1.addChild(door_leaf_r3);
		setRotationAngle(door_leaf_r3, 0, 0, 0.1107F);
		door_leaf_r3.texOffs(0, 243).addBox(0, -23, -14, 1, 5, 28, 0, false);

		door_left_exterior_1 = new ModelMapper(modelDataWrapper);
		door_left_exterior_1.setPos(0, 0, 0);
		door_exterior_1.addChild(door_left_exterior_1);
		door_left_exterior_1.texOffs(159, 191).addBox(-21, -14, 0, 0, 14, 14, 0, false);

		door_left_top_r2 = new ModelMapper(modelDataWrapper);
		door_left_top_r2.setPos(-21, -14, 0);
		door_left_exterior_1.addChild(door_left_top_r2);
		setRotationAngle(door_left_top_r2, 0, 0, 0.1107F);
		door_left_top_r2.texOffs(0, 182).addBox(0, -18, 0, 0, 18, 14, 0, false);

		door_right_exterior_1 = new ModelMapper(modelDataWrapper);
		door_right_exterior_1.setPos(0, 0, 0);
		door_exterior_1.addChild(door_right_exterior_1);
		door_right_exterior_1.texOffs(96, 190).addBox(-21, -14, -14, 0, 14, 14, 0, false);

		door_right_top_r2 = new ModelMapper(modelDataWrapper);
		door_right_top_r2.setPos(-21, -14, 0);
		door_right_exterior_1.addChild(door_right_top_r2);
		setRotationAngle(door_right_top_r2, 0, 0, 0.1107F);
		door_right_top_r2.texOffs(178, 44).addBox(0, -18, -14, 0, 18, 14, 0, false);

		door_exterior_2 = new ModelMapper(modelDataWrapper);
		door_exterior_2.setPos(0, 24, 0);
		door_exterior_2.texOffs(100, 18).addBox(20, 0, -18, 1, 4, 36, 0, true);

		door_leaf_r4 = new ModelMapper(modelDataWrapper);
		door_leaf_r4.setPos(21, -14, 0);
		door_exterior_2.addChild(door_leaf_r4);
		setRotationAngle(door_leaf_r4, 0, 0, -0.1107F);
		door_leaf_r4.texOffs(0, 243).addBox(-1, -23, -14, 1, 5, 28, 0, true);

		door_left_exterior_2 = new ModelMapper(modelDataWrapper);
		door_left_exterior_2.setPos(0, 0, 0);
		door_exterior_2.addChild(door_left_exterior_2);
		door_left_exterior_2.texOffs(159, 191).addBox(21, -14, 0, 0, 14, 14, 0, true);

		door_left_top_r3 = new ModelMapper(modelDataWrapper);
		door_left_top_r3.setPos(21, -14, 0);
		door_left_exterior_2.addChild(door_left_top_r3);
		setRotationAngle(door_left_top_r3, 0, 0, -0.1107F);
		door_left_top_r3.texOffs(0, 182).addBox(0, -18, 0, 0, 18, 14, 0, true);

		door_right_exterior_2 = new ModelMapper(modelDataWrapper);
		door_right_exterior_2.setPos(0, 0, 0);
		door_exterior_2.addChild(door_right_exterior_2);
		door_right_exterior_2.texOffs(96, 190).addBox(21, -14, -14, 0, 14, 14, 0, true);

		door_right_top_r3 = new ModelMapper(modelDataWrapper);
		door_right_top_r3.setPos(21, -14, 0);
		door_right_exterior_2.addChild(door_right_top_r3);
		setRotationAngle(door_right_top_r3, 0, 0, -0.1107F);
		door_right_top_r3.texOffs(178, 44).addBox(0, -18, -14, 0, 18, 14, 0, true);

		end = new ModelMapper(modelDataWrapper);
		end.setPos(0, 24, 0);
		end.texOffs(154, 141).addBox(-20, 0, -12, 40, 1, 20, 0, false);
		end.texOffs(28, 139).addBox(-20, -14, 5, 3, 14, 6, 0, false);
		end.texOffs(178, 205).addBox(9.5F, -35, -12, 8, 35, 19, 0, false);
		end.texOffs(124, 205).addBox(-17.5F, -35, -12, 8, 35, 19, 0, false);
		end.texOffs(217, 118).addBox(-9.5F, -35, -12, 19, 3, 19, 0, false);

		upper_wall_2_r2 = new ModelMapper(modelDataWrapper);
		upper_wall_2_r2.setPos(-20, -14, 0);
		end.addChild(upper_wall_2_r2);
		setRotationAngle(upper_wall_2_r2, 0, 0, 0.1107F);
		upper_wall_2_r2.texOffs(54, 139).addBox(0, -19, 6, 3, 19, 5, 0, false);

		upper_wall_1_r1 = new ModelMapper(modelDataWrapper);
		upper_wall_1_r1.setPos(20, -14, 0);
		end.addChild(upper_wall_1_r1);
		setRotationAngle(upper_wall_1_r1, 0, 3.1416F, -0.1107F);
		upper_wall_1_r1.texOffs(236, 83).addBox(0, -19, -11, 3, 19, 5, 0, false);

		lower_wall_1_r1 = new ModelMapper(modelDataWrapper);
		lower_wall_1_r1.setPos(0, 0, 0);
		end.addChild(lower_wall_1_r1);
		setRotationAngle(lower_wall_1_r1, 0, 3.1416F, 0);
		lower_wall_1_r1.texOffs(154, 141).addBox(-20, -14, -11, 3, 14, 6, 0, false);

		end_exterior = new ModelMapper(modelDataWrapper);
		end_exterior.setPos(0, 24, 0);
		end_exterior.texOffs(156, 259).addBox(17, -14, -12, 3, 18, 22, 0, true);
		end_exterior.texOffs(156, 259).addBox(-20, -14, -12, 3, 18, 22, 0, false);
		end_exterior.texOffs(168, 85).addBox(9.5F, -34, -12, 10, 34, 0, 0, false);
		end_exterior.texOffs(168, 85).addBox(-19.5F, -34, -12, 10, 34, 0, 0, true);
		end_exterior.texOffs(246, 11).addBox(-18, -41, -12, 36, 7, 0, 0, false);

		door_leaf_2_r1 = new ModelMapper(modelDataWrapper);
		door_leaf_2_r1.setPos(21, -14, 0);
		end_exterior.addChild(door_leaf_2_r1);
		setRotationAngle(door_leaf_2_r1, 0, 3.1416F, -0.1107F);
		door_leaf_2_r1.texOffs(236, 83).addBox(0, -23, -16, 1, 5, 28, 0, false);

		door_leaf_1_r1 = new ModelMapper(modelDataWrapper);
		door_leaf_1_r1.setPos(-21, -14, 0);
		end_exterior.addChild(door_leaf_1_r1);
		setRotationAngle(door_leaf_1_r1, 0, 0, 0.1107F);
		door_leaf_1_r1.texOffs(238, 204).addBox(0, -23, -12, 1, 5, 28, 0, false);

		upper_wall_2_r3 = new ModelMapper(modelDataWrapper);
		upper_wall_2_r3.setPos(-20, -14, 0);
		end_exterior.addChild(upper_wall_2_r3);
		setRotationAngle(upper_wall_2_r3, 0, 0, 0.1107F);
		upper_wall_2_r3.texOffs(0, 139).addBox(0, -22, -12, 3, 22, 22, 0, false);

		upper_wall_1_r2 = new ModelMapper(modelDataWrapper);
		upper_wall_1_r2.setPos(20, -14, 0);
		end_exterior.addChild(upper_wall_1_r2);
		setRotationAngle(upper_wall_1_r2, 0, 0, -0.1107F);
		upper_wall_1_r2.texOffs(0, 139).addBox(-3, -22, -12, 3, 22, 22, 0, true);

		roof_end = new ModelMapper(modelDataWrapper);
		roof_end.setPos(0, 24, 0);


		inner_roof_1 = new ModelMapper(modelDataWrapper);
		inner_roof_1.setPos(-2, -33, 38);
		roof_end.addChild(inner_roof_1);
		inner_roof_1.texOffs(225, 116).addBox(-16, 0, -31, 5, 1, 1, 0, false);
		inner_roof_1.texOffs(79, 82).addBox(-9.0724F, -1.2978F, -31, 1, 0, 1, 0, false);
		inner_roof_1.texOffs(115, 99).addBox(-2, -1.5F, -31, 4, 0, 1, 0, false);

		inner_roof_5_r3 = new ModelMapper(modelDataWrapper);
		inner_roof_5_r3.setPos(-1, -1.5F, -38);
		inner_roof_1.addChild(inner_roof_5_r3);
		setRotationAngle(inner_roof_5_r3, 0, 0, -0.0873F);
		inner_roof_5_r3.texOffs(33, 0).addBox(-3, 0, 7, 3, 0, 1, 0, false);

		inner_roof_4_r3 = new ModelMapper(modelDataWrapper);
		inner_roof_4_r3.setPos(-4.9733F, -1.0649F, -38);
		inner_roof_1.addChild(inner_roof_4_r3);
		setRotationAngle(inner_roof_4_r3, 0, 0, -0.1745F);
		inner_roof_4_r3.texOffs(123, 99).addBox(-1, 0, 7, 2, 0, 1, 0, false);

		inner_roof_2_r3 = new ModelMapper(modelDataWrapper);
		inner_roof_2_r3.setPos(-11, 1, -38);
		inner_roof_1.addChild(inner_roof_2_r3);
		setRotationAngle(inner_roof_2_r3, 0, 0, -0.8727F);
		inner_roof_2_r3.texOffs(33, 49).addBox(0, 0, 7, 3, 0, 1, 0, false);

		inner_roof_2 = new ModelMapper(modelDataWrapper);
		inner_roof_2.setPos(2, -33, 38);
		roof_end.addChild(inner_roof_2);
		inner_roof_2.texOffs(225, 116).addBox(11, 0, -31, 5, 1, 1, 0, true);
		inner_roof_2.texOffs(79, 82).addBox(8.0724F, -1.2978F, -31, 1, 0, 1, 0, true);
		inner_roof_2.texOffs(115, 99).addBox(-2, -1.5F, -31, 4, 0, 1, 0, true);

		inner_roof_6_r1 = new ModelMapper(modelDataWrapper);
		inner_roof_6_r1.setPos(1, -1.5F, -38);
		inner_roof_2.addChild(inner_roof_6_r1);
		setRotationAngle(inner_roof_6_r1, 0, 0, 0.0873F);
		inner_roof_6_r1.texOffs(33, 0).addBox(0, 0, 7, 3, 0, 1, 0, true);

		inner_roof_5_r4 = new ModelMapper(modelDataWrapper);
		inner_roof_5_r4.setPos(4.9733F, -1.0649F, -38);
		inner_roof_2.addChild(inner_roof_5_r4);
		setRotationAngle(inner_roof_5_r4, 0, 0, 0.1745F);
		inner_roof_5_r4.texOffs(123, 99).addBox(-1, 0, 7, 2, 0, 1, 0, true);

		inner_roof_3_r1 = new ModelMapper(modelDataWrapper);
		inner_roof_3_r1.setPos(11, 1, -38);
		inner_roof_2.addChild(inner_roof_3_r1);
		setRotationAngle(inner_roof_3_r1, 0, 0, 0.8727F);
		inner_roof_3_r1.texOffs(33, 49).addBox(-3, 0, 7, 3, 0, 1, 0, true);

		roof_end_exterior = new ModelMapper(modelDataWrapper);
		roof_end_exterior.setPos(0, 24, 0);
		roof_end_exterior.texOffs(0, 49).addBox(-8, -42, 0, 16, 2, 48, 0, false);

		vent_2_r1 = new ModelMapper(modelDataWrapper);
		vent_2_r1.setPos(-8, -42, 0);
		roof_end_exterior.addChild(vent_2_r1);
		setRotationAngle(vent_2_r1, 0, 0, -0.3491F);
		vent_2_r1.texOffs(80, 91).addBox(-9, 0, 0, 9, 2, 48, 0, false);

		vent_1_r1 = new ModelMapper(modelDataWrapper);
		vent_1_r1.setPos(8, -42, 0);
		roof_end_exterior.addChild(vent_1_r1);
		setRotationAngle(vent_1_r1, 0, 0, 0.3491F);
		vent_1_r1.texOffs(138, 0).addBox(0, 0, 0, 9, 2, 48, 0, false);

		outer_roof_1 = new ModelMapper(modelDataWrapper);
		outer_roof_1.setPos(0, 0, 0);
		roof_end_exterior.addChild(outer_roof_1);
		outer_roof_1.texOffs(247, 162).addBox(-6, -41, -12, 6, 1, 20, 0, false);

		outer_roof_5_r2 = new ModelMapper(modelDataWrapper);
		outer_roof_5_r2.setPos(-6, -41, 0);
		outer_roof_1.addChild(outer_roof_5_r2);
		setRotationAngle(outer_roof_5_r2, 0, 0, -0.1745F);
		outer_roof_5_r2.texOffs(87, 243).addBox(-8, 0, -12, 8, 1, 20, 0, false);

		outer_roof_4_r2 = new ModelMapper(modelDataWrapper);
		outer_roof_4_r2.setPos(-15.3605F, -38.1778F, -2);
		outer_roof_1.addChild(outer_roof_4_r2);
		setRotationAngle(outer_roof_4_r2, 0, 0, -0.5236F);
		outer_roof_4_r2.texOffs(31, 243).addBox(-2, -0.5F, -10, 4, 1, 20, 0, false);

		outer_roof_3_r2 = new ModelMapper(modelDataWrapper);
		outer_roof_3_r2.setPos(-17.4096F, -36.4948F, -2);
		outer_roof_1.addChild(outer_roof_3_r2);
		setRotationAngle(outer_roof_3_r2, 0, 0, -1.0472F);
		outer_roof_3_r2.texOffs(246, 253).addBox(-1, -0.5F, -10, 2, 1, 20, 0, false);

		outer_roof_2 = new ModelMapper(modelDataWrapper);
		outer_roof_2.setPos(0, 0, 0);
		roof_end_exterior.addChild(outer_roof_2);
		outer_roof_2.texOffs(247, 162).addBox(0, -41, -12, 6, 1, 20, 0, true);

		outer_roof_6_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_6_r1.setPos(6, -41, 0);
		outer_roof_2.addChild(outer_roof_6_r1);
		setRotationAngle(outer_roof_6_r1, 0, 0, 0.1745F);
		outer_roof_6_r1.texOffs(87, 243).addBox(0, 0, -12, 8, 1, 20, 0, true);

		outer_roof_5_r3 = new ModelMapper(modelDataWrapper);
		outer_roof_5_r3.setPos(15.3605F, -38.1778F, -2);
		outer_roof_2.addChild(outer_roof_5_r3);
		setRotationAngle(outer_roof_5_r3, 0, 0, 0.5236F);
		outer_roof_5_r3.texOffs(31, 243).addBox(-2, -0.5F, -10, 4, 1, 20, 0, true);

		outer_roof_4_r3 = new ModelMapper(modelDataWrapper);
		outer_roof_4_r3.setPos(17.4096F, -36.4948F, -2);
		outer_roof_2.addChild(outer_roof_4_r3);
		setRotationAngle(outer_roof_4_r3, 0, 0, 1.0472F);
		outer_roof_4_r3.texOffs(246, 253).addBox(-1, -0.5F, -10, 2, 1, 20, 0, true);

		roof_window_light = new ModelMapper(modelDataWrapper);
		roof_window_light.setPos(0, 24, 0);


		light_2_r1 = new ModelMapper(modelDataWrapper);
		light_2_r1.setPos(-10.1225F, -34.1864F, 0);
		roof_window_light.addChild(light_2_r1);
		setRotationAngle(light_2_r1, 0, 0, 1.2217F);
		light_2_r1.texOffs(80, 0).addBox(-0.5F, 0, -24, 1, 0, 48, 0, false);

		light_1_r1 = new ModelMapper(modelDataWrapper);
		light_1_r1.setPos(-8.9544F, -33.8041F, 0);
		roof_window_light.addChild(light_1_r1);
		setRotationAngle(light_1_r1, 0, 0, -0.0873F);
		light_1_r1.texOffs(74, 0).addBox(-1, 0, -24, 2, 0, 48, 0, false);

		roof_door_light = new ModelMapper(modelDataWrapper);
		roof_door_light.setPos(0, 24, 0);


		light_3_r1 = new ModelMapper(modelDataWrapper);
		light_3_r1.setPos(-10.1225F, -34.1864F, 0);
		roof_door_light.addChild(light_3_r1);
		setRotationAngle(light_3_r1, 0, 0, 1.2217F);
		light_3_r1.texOffs(96, 8).addBox(-0.5F, 0, -16, 1, 0, 32, 0, false);

		light_2_r2 = new ModelMapper(modelDataWrapper);
		light_2_r2.setPos(-8.9544F, -33.8041F, 0);
		roof_door_light.addChild(light_2_r2);
		setRotationAngle(light_2_r2, 0, 0, -0.0873F);
		light_2_r2.texOffs(90, 8).addBox(-1, 0, -16, 2, 0, 32, 0, false);

		roof_end_light = new ModelMapper(modelDataWrapper);
		roof_end_light.setPos(0, 24, 0);


		light_3_r2 = new ModelMapper(modelDataWrapper);
		light_3_r2.setPos(10.1225F, -34.1864F, 0);
		roof_end_light.addChild(light_3_r2);
		setRotationAngle(light_3_r2, 0, 0, -1.2217F);
		light_3_r2.texOffs(127, 35).addBox(-0.5F, 0, 7, 1, 0, 1, 0, true);

		light_2_r3 = new ModelMapper(modelDataWrapper);
		light_2_r3.setPos(8.9544F, -33.8041F, 0);
		roof_end_light.addChild(light_2_r3);
		setRotationAngle(light_2_r3, 0, 0, 0.0873F);
		light_2_r3.texOffs(121, 35).addBox(-1, 0, 7, 2, 0, 1, 0, true);

		light_3_r3 = new ModelMapper(modelDataWrapper);
		light_3_r3.setPos(-10.1225F, -34.1864F, 0);
		roof_end_light.addChild(light_3_r3);
		setRotationAngle(light_3_r3, 0, 0, 1.2217F);
		light_3_r3.texOffs(127, 35).addBox(-0.5F, 0, 7, 1, 0, 1, 0, false);

		light_2_r4 = new ModelMapper(modelDataWrapper);
		light_2_r4.setPos(-8.9544F, -33.8041F, 0);
		roof_end_light.addChild(light_2_r4);
		setRotationAngle(light_2_r4, 0, 0, -0.0873F);
		light_2_r4.texOffs(121, 35).addBox(-1, 0, 7, 2, 0, 1, 0, false);

		head = new ModelMapper(modelDataWrapper);
		head.setPos(0, 24, 0);
		head.texOffs(80, 58).addBox(-18, 0, -18, 36, 1, 26, 0, false);
		head.texOffs(212, 162).addBox(-20, -14, -17, 3, 14, 28, 0, false);
		head.texOffs(266, 83).addBox(-17, -14, -2, 4, 14, 13, 0, false);

		upper_wall_2_r4 = new ModelMapper(modelDataWrapper);
		upper_wall_2_r4.setPos(-20, -14, 0);
		head.addChild(upper_wall_2_r4);
		setRotationAngle(upper_wall_2_r4, 0, 0, 0.1107F);
		upper_wall_2_r4.texOffs(0, 196).addBox(0, -19, -17, 3, 19, 28, 0, false);
		upper_wall_2_r4.texOffs(0, 0).addBox(3, -21, -2, 4, 21, 13, 0, false);

		upper_wall_1_r3 = new ModelMapper(modelDataWrapper);
		upper_wall_1_r3.setPos(20, -14, 0);
		head.addChild(upper_wall_1_r3);
		setRotationAngle(upper_wall_1_r3, 0, 3.1416F, -0.1107F);
		upper_wall_1_r3.texOffs(62, 196).addBox(0, -19, -11, 3, 19, 28, 0, false);

		lower_wall_1_r2 = new ModelMapper(modelDataWrapper);
		lower_wall_1_r2.setPos(0, 0, 0);
		head.addChild(lower_wall_1_r2);
		setRotationAngle(lower_wall_1_r2, 0, 3.1416F, 0);
		lower_wall_1_r2.texOffs(204, 231).addBox(-20, -14, -11, 3, 14, 28, 0, false);

		ceiling = new ModelMapper(modelDataWrapper);
		ceiling.setPos(0, 0, 0);
		head.addChild(ceiling);
		ceiling.texOffs(0, 183).addBox(-15.165F, -34.3F, -15, 8, 4, 4, 0, true);
		ceiling.texOffs(208, 273).addBox(-7.5F, -32, -17, 2, 32, 2, 0, true);
		ceiling.texOffs(208, 273).addBox(5.5F, -32, -17, 2, 32, 2, 0, true);

		panel_9_r1 = new ModelMapper(modelDataWrapper);
		panel_9_r1.setPos(7.165F, -30.3F, -11);
		ceiling.addChild(panel_9_r1);
		setRotationAngle(panel_9_r1, 0, 0, 0.6109F);
		panel_9_r1.texOffs(0, 49).addBox(-2, -4, -4, 2, 4, 4, 0, true);

		panel_8_r1 = new ModelMapper(modelDataWrapper);
		panel_8_r1.setPos(22.165F, 0.7F, 0);
		ceiling.addChild(panel_8_r1);
		setRotationAngle(panel_8_r1, 0, 0, 0);
		panel_8_r1.texOffs(0, 183).addBox(-15, -35, -15, 8, 4, 4, 0, true);

		panel_7_r1 = new ModelMapper(modelDataWrapper);
		panel_7_r1.setPos(0, 0.7F, 2);
		ceiling.addChild(panel_7_r1);
		setRotationAngle(panel_7_r1, 0, 0, 0);
		panel_7_r1.texOffs(80, 90).addBox(-7, -34, -17, 14, 2, 4, 0, true);

		panel_6_r1 = new ModelMapper(modelDataWrapper);
		panel_6_r1.setPos(-7.165F, -30.3F, 0);
		ceiling.addChild(panel_6_r1);
		setRotationAngle(panel_6_r1, 0, 0, -0.6109F);
		panel_6_r1.texOffs(0, 49).addBox(0, -4, -15, 2, 4, 4, 0, true);

		panel_4_r1 = new ModelMapper(modelDataWrapper);
		panel_4_r1.setPos(-13.5F, -31, 0);
		ceiling.addChild(panel_4_r1);
		setRotationAngle(panel_4_r1, 0, 0, -0.7418F);
		panel_4_r1.texOffs(0, 214).addBox(0, -1, -11, 3, 1, 9, 0, false);

		panel_3_r1 = new ModelMapper(modelDataWrapper);
		panel_3_r1.setPos(-1.0805F, 1.0256F, 2);
		ceiling.addChild(panel_3_r1);
		setRotationAngle(panel_3_r1, 0, 0, 0);
		panel_3_r1.texOffs(250, 42).addBox(-14.4195F, -33.0256F, -13, 2, 1, 9, 0, false);

		panel_2_r1 = new ModelMapper(modelDataWrapper);
		panel_2_r1.setPos(13.5F, -31, 0);
		ceiling.addChild(panel_2_r1);
		setRotationAngle(panel_2_r1, 0, 0, 0.7418F);
		panel_2_r1.texOffs(250, 42).addBox(-3, -1, -11, 3, 1, 22, 0, false);

		panel_1_r1 = new ModelMapper(modelDataWrapper);
		panel_1_r1.setPos(0.1555F, 1.0256F, -2);
		ceiling.addChild(panel_1_r1);
		setRotationAngle(panel_1_r1, 0, 0, 0);
		panel_1_r1.texOffs(138, 18).addBox(13.3445F, -33.0256F, -9, 2, 1, 22, 0, false);

		main_r1 = new ModelMapper(modelDataWrapper);
		main_r1.setPos(0, 1, -2);
		ceiling.addChild(main_r1);
		setRotationAngle(main_r1, 0, 0, 0);
		main_r1.texOffs(54, 141).addBox(-12, -35, -9, 24, 1, 22, 0, true);

		emergency_door = new ModelMapper(modelDataWrapper);
		emergency_door.setPos(0, 0, 0);
		head.addChild(emergency_door);
		emergency_door.texOffs(238, 246).addBox(-5.5F, -11, -18, 11, 11, 1, 0, false);

		upper_r1 = new ModelMapper(modelDataWrapper);
		upper_r1.setPos(0, -11, -17);
		emergency_door.addChild(upper_r1);
		setRotationAngle(upper_r1, -0.0873F, 0, 0);
		upper_r1.texOffs(212, 162).addBox(-5.5F, -22, -1, 11, 22, 1, 0, false);

		left_c_panel = new ModelMapper(modelDataWrapper);
		left_c_panel.setPos(0, 0, 0);
		head.addChild(left_c_panel);


		panel_r1 = new ModelMapper(modelDataWrapper);
		panel_r1.setPos(0, 0, 0);
		left_c_panel.addChild(panel_r1);
		setRotationAngle(panel_r1, -2.5744F, 0, 3.1416F);
		panel_r1.texOffs(246, 18).addBox(-17, -4.15F, 15.65F, 10, 3, 6, 0, false);

		base_r1 = new ModelMapper(modelDataWrapper);
		base_r1.setPos(0, 0, 0);
		left_c_panel.addChild(base_r1);
		setRotationAngle(base_r1, 0, 3.1416F, 0);
		base_r1.texOffs(146, 259).addBox(-17, -12, 11, 10, 12, 6, 0, false);

		right_c_panel = new ModelMapper(modelDataWrapper);
		right_c_panel.setPos(0, 0, 0);
		head.addChild(right_c_panel);


		panel_r2 = new ModelMapper(modelDataWrapper);
		panel_r2.setPos(0, 0, 0);
		right_c_panel.addChild(panel_r2);
		setRotationAngle(panel_r2, -2.5744F, 0, -3.1416F);
		panel_r2.texOffs(192, 118).addBox(7, -4.15F, 15.65F, 10, 3, 6, 0, false);

		base_r2 = new ModelMapper(modelDataWrapper);
		base_r2.setPos(0, 0, 0);
		right_c_panel.addChild(base_r2);
		setRotationAngle(base_r2, 0, -3.1416F, 0);
		base_r2.texOffs(213, 205).addBox(7, -12, 11, 10, 12, 6, 0, false);

		handrail = new ModelMapper(modelDataWrapper);
		handrail.setPos(0, 0, 0);
		head.addChild(handrail);


		wall = new ModelMapper(modelDataWrapper);
		wall.setPos(0, 0, 2);
		handrail.addChild(wall);


		handrail_4_r1 = new ModelMapper(modelDataWrapper);
		handrail_4_r1.setPos(14.8F, -11.2F, -7.2F);
		wall.addChild(handrail_4_r1);
		setRotationAngle(handrail_4_r1, 1.5708F, -0.4363F, 0);
		handrail_4_r1.texOffs(319, 0).addBox(0.2F, -1.2F, -0.2F, 0, 1, 0, 0.2F, false);

		handrail_3_r1 = new ModelMapper(modelDataWrapper);
		handrail_3_r1.setPos(16.5638F, -11, 2.7947F);
		wall.addChild(handrail_3_r1);
		setRotationAngle(handrail_3_r1, -1.5708F, 1.1345F, 0);
		handrail_3_r1.texOffs(319, 0).addBox(0, -1, 0, 0, 2, 0, 0.2F, false);

		handrail_2_r1 = new ModelMapper(modelDataWrapper);
		handrail_2_r1.setPos(14.8F, -11.2F, 1.2F);
		wall.addChild(handrail_2_r1);
		setRotationAngle(handrail_2_r1, -1.5708F, 0.4363F, 0);
		handrail_2_r1.texOffs(319, 0).addBox(0.2F, -1.2F, 0.2F, 0, 1, 0, 0.2F, false);

		handrail_5_r1 = new ModelMapper(modelDataWrapper);
		handrail_5_r1.setPos(16.5638F, -11, -8.7947F);
		wall.addChild(handrail_5_r1);
		setRotationAngle(handrail_5_r1, 1.5708F, -1.1345F, 0);
		handrail_5_r1.texOffs(319, 0).addBox(0, -1, 0, 0, 2, 0, 0.2F, false);

		handrail_1_r1 = new ModelMapper(modelDataWrapper);
		handrail_1_r1.setPos(0, 0, -2);
		wall.addChild(handrail_1_r1);
		setRotationAngle(handrail_1_r1, -1.5708F, 0, 0);
		handrail_1_r1.texOffs(319, 11).addBox(15, -3, -11, 0, 8, 0, 0.2F, false);

		ceiling2 = new ModelMapper(modelDataWrapper);
		ceiling2.setPos(1, 0, 2);
		handrail.addChild(ceiling2);
		ceiling2.texOffs(0, 0).addBox(-9, -31.5F, 0, 2, 4, 0, 0, false);
		ceiling2.texOffs(0, 0).addBox(-9, -31.5F, -6, 2, 4, 0, 0, false);

		handrail_7_r1 = new ModelMapper(modelDataWrapper);
		handrail_7_r1.setPos(-19, -16.0275F, -4.0905F);
		ceiling2.addChild(handrail_7_r1);
		setRotationAngle(handrail_7_r1, 1.5708F, 1.1345F, 1.5708F);
		handrail_7_r1.texOffs(319, 0).addBox(-2.725F, -17.975F, 11, 0, 2, 0, 0.2F, false);

		handrail_4_r2 = new ModelMapper(modelDataWrapper);
		handrail_4_r2.setPos(-7.8F, -30.8F, 1.2F);
		ceiling2.addChild(handrail_4_r2);
		setRotationAngle(handrail_4_r2, -1.5708F, -0.4363F, 1.5708F);
		handrail_4_r2.texOffs(319, 0).addBox(-0.2F, -1.2F, 0.2F, 0, 1, 0, 0.2F, false);

		handrail_5_r2 = new ModelMapper(modelDataWrapper);
		handrail_5_r2.setPos(-19, -16.0275F, -1.9095F);
		ceiling2.addChild(handrail_5_r2);
		setRotationAngle(handrail_5_r2, -1.5708F, -1.1345F, 1.5708F);
		handrail_5_r2.texOffs(319, 0).addBox(-2.725F, -17.975F, -11, 0, 2, 0, 0.2F, false);

		handrail_6_r1 = new ModelMapper(modelDataWrapper);
		handrail_6_r1.setPos(-7.8F, -30.8F, -7.2F);
		ceiling2.addChild(handrail_6_r1);
		setRotationAngle(handrail_6_r1, 1.5708F, 0.4363F, 1.5708F);
		handrail_6_r1.texOffs(319, 0).addBox(-0.2F, -1.2F, -0.2F, 0, 1, 0, 0.2F, false);

		handrail_3_r2 = new ModelMapper(modelDataWrapper);
		handrail_3_r2.setPos(-1, 0, -2);
		ceiling2.addChild(handrail_3_r2);
		setRotationAngle(handrail_3_r2, -1.5708F, 0, 0);
		handrail_3_r2.texOffs(319, 0).addBox(-7, -3, -31, 0, 8, 0, 0.2F, false);

		ceiling3 = new ModelMapper(modelDataWrapper);
		ceiling3.setPos(-1, 0, 2);
		handrail.addChild(ceiling3);
		ceiling3.texOffs(0, 0).addBox(7, -31.5F, 0, 2, 4, 0, 0, true);
		ceiling3.texOffs(0, 0).addBox(7, -31.5F, -6, 2, 4, 0, 0, true);

		handrail_8_r1 = new ModelMapper(modelDataWrapper);
		handrail_8_r1.setPos(19, -16.0275F, -4.0905F);
		ceiling3.addChild(handrail_8_r1);
		setRotationAngle(handrail_8_r1, 1.5708F, -1.1345F, -1.5708F);
		handrail_8_r1.texOffs(319, 0).addBox(2.725F, -17.975F, 11, 0, 2, 0, 0.2F, true);

		handrail_5_r3 = new ModelMapper(modelDataWrapper);
		handrail_5_r3.setPos(7.8F, -30.8F, 1.2F);
		ceiling3.addChild(handrail_5_r3);
		setRotationAngle(handrail_5_r3, -1.5708F, 0.4363F, -1.5708F);
		handrail_5_r3.texOffs(319, 0).addBox(0.2F, -1.2F, 0.2F, 0, 1, 0, 0.2F, true);

		handrail_6_r2 = new ModelMapper(modelDataWrapper);
		handrail_6_r2.setPos(19, -16.0275F, -1.9095F);
		ceiling3.addChild(handrail_6_r2);
		setRotationAngle(handrail_6_r2, -1.5708F, 1.1345F, -1.5708F);
		handrail_6_r2.texOffs(319, 0).addBox(2.725F, -17.975F, -11, 0, 2, 0, 0.2F, true);

		handrail_7_r2 = new ModelMapper(modelDataWrapper);
		handrail_7_r2.setPos(7.8F, -30.8F, -7.2F);
		ceiling3.addChild(handrail_7_r2);
		setRotationAngle(handrail_7_r2, 1.5708F, -0.4363F, -1.5708F);
		handrail_7_r2.texOffs(319, 0).addBox(0.2F, -1.2F, -0.2F, 0, 1, 0, 0.2F, true);

		handrail_4_r3 = new ModelMapper(modelDataWrapper);
		handrail_4_r3.setPos(1, 0, -2);
		ceiling3.addChild(handrail_4_r3);
		setRotationAngle(handrail_4_r3, -1.5708F, 0, 0);
		handrail_4_r3.texOffs(319, 0).addBox(7, -3, -31, 0, 8, 0, 0.2F, true);

		head_exterior = new ModelMapper(modelDataWrapper);
		head_exterior.setPos(0, 24, 0);
		head_exterior.texOffs(0, 14).addBox(20, -14, -10, 0, 14, 20, 0, false);
		head_exterior.texOffs(0, 14).addBox(-20, -14, -10, 0, 14, 20, 0, false);

		upper_wall_2_r5 = new ModelMapper(modelDataWrapper);
		upper_wall_2_r5.setPos(-20, -14, 0);
		head_exterior.addChild(upper_wall_2_r5);
		setRotationAngle(upper_wall_2_r5, 0, 0, 0.1107F);
		upper_wall_2_r5.texOffs(154, 142).addBox(0, -22, -10, 0, 22, 20, 0, false);

		upper_wall_1_r4 = new ModelMapper(modelDataWrapper);
		upper_wall_1_r4.setPos(20, -14, 0);
		head_exterior.addChild(upper_wall_1_r4);
		setRotationAngle(upper_wall_1_r4, 0, 0, -0.1107F);
		upper_wall_1_r4.texOffs(154, 142).addBox(0, -22, -10, 0, 22, 20, 0, false);

		door_leaf_4_r1 = new ModelMapper(modelDataWrapper);
		door_leaf_4_r1.setPos(21, -14, -10);
		head_exterior.addChild(door_leaf_4_r1);
		setRotationAngle(door_leaf_4_r1, 0, 0.3316F, -0.1107F);
		door_leaf_4_r1.texOffs(0, 82).addBox(-1, -23, -5, 1, 5, 5, 0, true);

		door_leaf_1_r2 = new ModelMapper(modelDataWrapper);
		door_leaf_1_r2.setPos(21, -14, 0);
		head_exterior.addChild(door_leaf_1_r2);
		setRotationAngle(door_leaf_1_r2, 0, 3.1416F, -0.1107F);
		door_leaf_1_r2.texOffs(146, 102).addBox(0, -23, -10, 1, 5, 20, 0, true);

		door_leaf_5_r1 = new ModelMapper(modelDataWrapper);
		door_leaf_5_r1.setPos(-21, -14, -10);
		head_exterior.addChild(door_leaf_5_r1);
		setRotationAngle(door_leaf_5_r1, 0, -0.3316F, 0.1107F);
		door_leaf_5_r1.texOffs(0, 82).addBox(0, -23, -5, 1, 5, 5, 0, false);

		door_leaf_2_r2 = new ModelMapper(modelDataWrapper);
		door_leaf_2_r2.setPos(-21, -14, 0);
		head_exterior.addChild(door_leaf_2_r2);
		setRotationAngle(door_leaf_2_r2, 0, -3.1416F, 0.1107F);
		door_leaf_2_r2.texOffs(146, 102).addBox(-1, -23, -10, 1, 5, 20, 0, false);

		front = new ModelMapper(modelDataWrapper);
		front.setPos(0, 0, 0);
		head_exterior.addChild(front);
		front.texOffs(238, 237).addBox(-19, -10, -19.5F, 38, 9, 0, 0, false);

		front_panel_4_r1 = new ModelMapper(modelDataWrapper);
		front_panel_4_r1.setPos(0, -35.2365F, -16.3334F);
		front.addChild(front_panel_4_r1);
		setRotationAngle(front_panel_4_r1, -0.2618F, 0, 0);
		front_panel_4_r1.texOffs(246, 0).addBox(-17.5F, -5.5F, 0, 35, 11, 0, 0, false);

		front_panel_3_r1 = new ModelMapper(modelDataWrapper);
		front_panel_3_r1.setPos(0, -10, -19.5F);
		front.addChild(front_panel_3_r1);
		setRotationAngle(front_panel_3_r1, -0.0873F, 0, 0);
		front_panel_3_r1.texOffs(54, 164).addBox(-19, -20, 0, 38, 20, 0, 0, false);

		front_panel_1_r1 = new ModelMapper(modelDataWrapper);
		front_panel_1_r1.setPos(0, -1, -19.5F);
		front.addChild(front_panel_1_r1);
		setRotationAngle(front_panel_1_r1, 0.3054F, 0, 0);
		front_panel_1_r1.texOffs(204, 33).addBox(-19, 0, 0, 38, 9, 0, 0, false);

		side_1 = new ModelMapper(modelDataWrapper);
		side_1.setPos(0, 0, 0);
		front.addChild(side_1);


		front_side_bottom_3_r1 = new ModelMapper(modelDataWrapper);
		front_side_bottom_3_r1.setPos(20, 0, -10);
		side_1.addChild(front_side_bottom_3_r1);
		setRotationAngle(front_side_bottom_3_r1, 0, 0, 0.1745F);
		front_side_bottom_3_r1.texOffs(178, 60).addBox(0, 0, 0, 0, 8, 16, 0, true);

		front_side_bottom_1_r1 = new ModelMapper(modelDataWrapper);
		front_side_bottom_1_r1.setPos(-20, 0, -10);
		side_1.addChild(front_side_bottom_1_r1);
		setRotationAngle(front_side_bottom_1_r1, 0, -0.1309F, -0.1745F);
		front_side_bottom_1_r1.texOffs(124, 136).addBox(0, 0, -13, 0, 8, 13, 0, false);

		front_side_lower_1_r1 = new ModelMapper(modelDataWrapper);
		front_side_lower_1_r1.setPos(-20, 0, -10);
		side_1.addChild(front_side_lower_1_r1);
		setRotationAngle(front_side_lower_1_r1, 0, -0.1309F, 0);
		front_side_lower_1_r1.texOffs(0, 128).addBox(0, -14, -11, 0, 14, 11, 0, false);

		front_side_upper_1_r1 = new ModelMapper(modelDataWrapper);
		front_side_upper_1_r1.setPos(-20, -14, -10);
		side_1.addChild(front_side_upper_1_r1);
		setRotationAngle(front_side_upper_1_r1, 0, -0.1309F, 0.1107F);
		front_side_upper_1_r1.texOffs(82, 47).addBox(0, -23, -11, 0, 23, 11, 0, false);

		side_2 = new ModelMapper(modelDataWrapper);
		side_2.setPos(-21, 0, 9);
		front.addChild(side_2);


		front_side_upper_2_r1 = new ModelMapper(modelDataWrapper);
		front_side_upper_2_r1.setPos(41, -14, -19);
		side_2.addChild(front_side_upper_2_r1);
		setRotationAngle(front_side_upper_2_r1, 0, 0.1309F, -0.1107F);
		front_side_upper_2_r1.texOffs(82, 47).addBox(0, -23, -11, 0, 23, 11, 0, true);

		front_side_lower_2_r1 = new ModelMapper(modelDataWrapper);
		front_side_lower_2_r1.setPos(41, 0, -19);
		side_2.addChild(front_side_lower_2_r1);
		setRotationAngle(front_side_lower_2_r1, 0, 0.1309F, 0);
		front_side_lower_2_r1.texOffs(0, 128).addBox(0, -14, -11, 0, 14, 11, 0, true);

		front_side_bottom_4_r1 = new ModelMapper(modelDataWrapper);
		front_side_bottom_4_r1.setPos(1, 0, -19);
		side_2.addChild(front_side_bottom_4_r1);
		setRotationAngle(front_side_bottom_4_r1, 0, 0, -0.1745F);
		front_side_bottom_4_r1.texOffs(178, 60).addBox(0, 0, 0, 0, 8, 16, 0, false);

		front_side_bottom_2_r1 = new ModelMapper(modelDataWrapper);
		front_side_bottom_2_r1.setPos(41, 0, -19);
		side_2.addChild(front_side_bottom_2_r1);
		setRotationAngle(front_side_bottom_2_r1, 0, 0.1309F, 0.1745F);
		front_side_bottom_2_r1.texOffs(124, 136).addBox(0, 0, -13, 0, 8, 13, 0, true);

		roof = new ModelMapper(modelDataWrapper);
		roof.setPos(-16.7054F, -37.098F, 5);
		head_exterior.addChild(roof);
		roof.texOffs(246, 162).addBox(10.7054F, -3.902F, -16, 6, 1, 21, 0, false);
		roof.texOffs(246, 162).addBox(16.7054F, -3.902F, -16, 6, 1, 21, 0, true);

		outer_roof_6_r2 = new ModelMapper(modelDataWrapper);
		outer_roof_6_r2.setPos(22.7054F, -3.902F, -5);
		roof.addChild(outer_roof_6_r2);
		setRotationAngle(outer_roof_6_r2, 0, 0, 0.1745F);
		outer_roof_6_r2.texOffs(86, 243).addBox(0, 0, -11, 8, 1, 21, 0, true);

		outer_roof_5_r4 = new ModelMapper(modelDataWrapper);
		outer_roof_5_r4.setPos(32.0659F, -1.0798F, -5.5F);
		roof.addChild(outer_roof_5_r4);
		setRotationAngle(outer_roof_5_r4, 0, 0, 0.5236F);
		outer_roof_5_r4.texOffs(30, 243).addBox(-2, -0.5F, -10.5F, 4, 1, 21, 0, true);

		outer_roof_4_r4 = new ModelMapper(modelDataWrapper);
		outer_roof_4_r4.setPos(34.115F, 0.6032F, -5.5F);
		roof.addChild(outer_roof_4_r4);
		setRotationAngle(outer_roof_4_r4, 0, 0, 1.0472F);
		outer_roof_4_r4.texOffs(245, 274).addBox(-1, -0.5F, -10.5F, 2, 1, 21, 0, true);

		outer_roof_5_r5 = new ModelMapper(modelDataWrapper);
		outer_roof_5_r5.setPos(10.7054F, -3.902F, -5);
		roof.addChild(outer_roof_5_r5);
		setRotationAngle(outer_roof_5_r5, 0, 0, -0.1745F);
		outer_roof_5_r5.texOffs(86, 243).addBox(-8, 0, -11, 8, 1, 21, 0, false);

		outer_roof_4_r5 = new ModelMapper(modelDataWrapper);
		outer_roof_4_r5.setPos(1.3449F, -1.0798F, -5.5F);
		roof.addChild(outer_roof_4_r5);
		setRotationAngle(outer_roof_4_r5, 0, 0, -0.5236F);
		outer_roof_4_r5.texOffs(30, 243).addBox(-2, -0.5F, -10.5F, 4, 1, 21, 0, false);

		outer_roof_3_r3 = new ModelMapper(modelDataWrapper);
		outer_roof_3_r3.setPos(-0.7041F, 0.6032F, -5.5F);
		roof.addChild(outer_roof_3_r3);
		setRotationAngle(outer_roof_3_r3, 0, 0, -1.0472F);
		outer_roof_3_r3.texOffs(245, 274).addBox(-1, -0.5F, -10.5F, 2, 1, 21, 0, false);

		vent_top_r1 = new ModelMapper(modelDataWrapper);
		vent_top_r1.setPos(16.7054F, 37.098F, 48);
		roof.addChild(vent_top_r1);
		setRotationAngle(vent_top_r1, -3.1416F, 0, 3.1416F);
		vent_top_r1.texOffs(0, 49).addBox(-8, -42, 0, 16, 2, 48, 0, false);

		vent_2_r2 = new ModelMapper(modelDataWrapper);
		vent_2_r2.setPos(24.7054F, -4.902F, 48);
		roof.addChild(vent_2_r2);
		setRotationAngle(vent_2_r2, -3.1416F, 0, -2.7925F);
		vent_2_r2.texOffs(80, 91).addBox(-9, 0, 0, 9, 2, 48, 0, false);

		vent_1_r2 = new ModelMapper(modelDataWrapper);
		vent_1_r2.setPos(8.7054F, -4.902F, 48);
		roof.addChild(vent_1_r2);
		setRotationAngle(vent_1_r2, 3.1416F, 0, 2.7925F);
		vent_1_r2.texOffs(138, 0).addBox(0, 0, 0, 9, 2, 48, 0, false);

		outer_roof_6_r3 = new ModelMapper(modelDataWrapper);
		outer_roof_6_r3.setPos(34.1586F, 1.7327F, -18.8187F);
		roof.addChild(outer_roof_6_r3);
		setRotationAngle(outer_roof_6_r3, 2.7925F, 0, -2.0944F);
		outer_roof_6_r3.texOffs(33, 40).addBox(-1, 0, -3, 4, 0, 7, 0, true);

		outer_roof_7_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_7_r1.setPos(31.9919F, -0.9516F, -18.4146F);
		roof.addChild(outer_roof_7_r1);
		setRotationAngle(outer_roof_7_r1, 2.8798F, 0, -2.618F);
		outer_roof_7_r1.texOffs(-1, 5).addBox(-2, 0, -2.5F, 4, 0, 6, 0, false);

		outer_roof_7_r2 = new ModelMapper(modelDataWrapper);
		outer_roof_7_r2.setPos(22.7054F, -3.902F, -16);
		roof.addChild(outer_roof_7_r2);
		setRotationAngle(outer_roof_7_r2, 0.3054F, 0, 0.1745F);
		outer_roof_7_r2.texOffs(10, 49).addBox(0, 0, -6, 9, 0, 6, 0, true);

		outer_roof_8_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_8_r1.setPos(16.7054F, 79.2472F, 0.0461F);
		roof.addChild(outer_roof_8_r1);
		setRotationAngle(outer_roof_8_r1, 0.3054F, 0, 0);
		outer_roof_8_r1.texOffs(16, 0).addBox(0, -84.125F, 4.7F, 6, 0, 5, 0, true);

		outer_roof_5_r6 = new ModelMapper(modelDataWrapper);
		outer_roof_5_r6.setPos(-0.7477F, 1.7327F, -18.8187F);
		roof.addChild(outer_roof_5_r6);
		setRotationAngle(outer_roof_5_r6, 2.7925F, 0, 2.0944F);
		outer_roof_5_r6.texOffs(33, 40).addBox(-3, 0, -3, 4, 0, 7, 0, false);

		outer_roof_6_r4 = new ModelMapper(modelDataWrapper);
		outer_roof_6_r4.setPos(1.4189F, -0.9516F, -18.4146F);
		roof.addChild(outer_roof_6_r4);
		setRotationAngle(outer_roof_6_r4, 2.8798F, 0, 2.618F);
		outer_roof_6_r4.texOffs(-1, 5).addBox(-2, 0, -2.5F, 4, 0, 6, 0, true);

		outer_roof_6_r5 = new ModelMapper(modelDataWrapper);
		outer_roof_6_r5.setPos(10.7054F, -3.902F, -16);
		roof.addChild(outer_roof_6_r5);
		setRotationAngle(outer_roof_6_r5, 0.3054F, 0, -0.1745F);
		outer_roof_6_r5.texOffs(10, 49).addBox(-9, 0, -6, 9, 0, 6, 0, false);

		outer_roof_7_r3 = new ModelMapper(modelDataWrapper);
		outer_roof_7_r3.setPos(16.7054F, 79.2472F, 0.0461F);
		roof.addChild(outer_roof_7_r3);
		setRotationAngle(outer_roof_7_r3, 0.3054F, 0, 0);
		outer_roof_7_r3.texOffs(16, 0).addBox(-6, -84.125F, 4.7F, 6, 0, 5, 0, false);

		headlights = new ModelMapper(modelDataWrapper);
		headlights.setPos(0, 24, 0);
		headlights.texOffs(0, 57).addBox(7.75F, -11, -19.6F, 7, 4, 0, 0, false);
		headlights.texOffs(0, 57).addBox(-14.75F, -11, -19.6F, 7, 4, 0, 0, true);

		tail_lights = new ModelMapper(modelDataWrapper);
		tail_lights.setPos(0, 24, 0);
		tail_lights.texOffs(0, 243).addBox(7.75F, -15, -19.6F, 14, 13, 0, 0, false);
		tail_lights.texOffs(0, 243).addBox(-21.75F, -15, -19.6F, 14, 13, 0, 0, true);

		door_light_on = new ModelMapper(modelDataWrapper);
		door_light_on.setPos(0, 24, 0);


		light_r1 = new ModelMapper(modelDataWrapper);
		light_r1.setPos(-21, 0, 0);
		door_light_on.addChild(light_r1);
		setRotationAngle(light_r1, 0, 0, 0.1107F);
		light_r1.texOffs(6, 3).addBox(-1.5F, -33.5F, 0, 0, 0, 0, 0.3F, false);

		door_light_off = new ModelMapper(modelDataWrapper);
		door_light_off.setPos(0, 24, 0);


		light_r2 = new ModelMapper(modelDataWrapper);
		light_r2.setPos(-21, 0, 0);
		door_light_off.addChild(light_r2);
		setRotationAngle(light_r2, 0, 0, 0.1107F);
		light_r2.texOffs(6, 0).addBox(-1.5F, -33.5F, 0, 0, 0, 0, 0.3F, false);

		modelDataWrapper.setModelPart(textureWidth, textureHeight);
		window.setModelPart();
		window_handrails.setModelPart();
		window_exterior_1.setModelPart();
		window_exterior_2.setModelPart();
		side_panel_translucent.setModelPart();
		roof_window.setModelPart();
		roof_door.setModelPart();
		roof_end.setModelPart();
		roof_exterior.setModelPart();
		door.setModelPart();
		door_left.setModelPart(door.name);
		door_right.setModelPart(door.name);
		door_handrail.setModelPart();
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
		roof_door_light.setModelPart();
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
	public ModelSTrain createNew(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		return new ModelSTrain(doorAnimationType, renderDoorOverlay);
	}

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
					renderMirror(window_handrails, matrices, vertices, light, position);
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

		switch (renderStage) {
			case LIGHTS:
				renderMirror(roof_door_light, matrices, vertices, light, position);
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
					door_left_exterior_1.setOffset(0, 0, doorLeftZ);
					door_right_exterior_1.setOffset(0, 0, -doorLeftZ);
					renderOnceFlipped(door_exterior_1, matrices, vertices, light, position);
					door_left_exterior_2.setOffset(0, 0, doorRightZ);
					door_right_exterior_2.setOffset(0, 0, -doorRightZ);
					renderOnceFlipped(door_exterior_2, matrices, vertices, light, position);
				} else {
					door_left_exterior_1.setOffset(0, 0, doorRightZ);
					door_right_exterior_1.setOffset(0, 0, -doorRightZ);
					renderOnce(door_exterior_1, matrices, vertices, light, position);
					door_left_exterior_2.setOffset(0, 0, doorLeftZ);
					door_right_exterior_2.setOffset(0, 0, -doorLeftZ);
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
}