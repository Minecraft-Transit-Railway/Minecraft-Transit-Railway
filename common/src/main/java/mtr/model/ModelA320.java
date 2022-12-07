package mtr.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mtr.client.DoorAnimationType;
import mtr.mappings.ModelDataWrapper;
import mtr.mappings.ModelMapper;

public class ModelA320 extends ModelSimpleTrainBase<ModelA320> {

	private final ModelMapper exterior;
	private final ModelMapper main;
	private final ModelMapper right_roof_r1;
	private final ModelMapper left_roof_r1;
	private final ModelMapper right_top_wall_r1;
	private final ModelMapper left_top_wall_r1;
	private final ModelMapper right_bottom_wall_r1;
	private final ModelMapper left_bottom_wall_r1;
	private final ModelMapper right_floor_r1;
	private final ModelMapper left_floor_r1;
	private final ModelMapper front;
	private final ModelMapper top_right_back_r1;
	private final ModelMapper top_left_back_r1;
	private final ModelMapper bottom_right_r1;
	private final ModelMapper bottom_left_r1;
	private final ModelMapper bottom_front_right_r1;
	private final ModelMapper bottom_front_left_r1;
	private final ModelMapper windscreen_lower_right_r1;
	private final ModelMapper windscreen_lower_left_r1;
	private final ModelMapper windscreen_right_r1;
	private final ModelMapper windscreen_left_r1;
	private final ModelMapper right_wall_front_r1;
	private final ModelMapper left_wall_front_r1;
	private final ModelMapper right_wall_top_r1;
	private final ModelMapper left_wall_top_r1;
	private final ModelMapper right_wall_lower_r1;
	private final ModelMapper left_wall_lower_r1;
	private final ModelMapper right_wall_r1;
	private final ModelMapper left_wall_r1;
	private final ModelMapper top_back_r1;
	private final ModelMapper top_front_r1;
	private final ModelMapper bottom_r1;
	private final ModelMapper bottom_front_r1;
	private final ModelMapper windscreen_main_r1;
	private final ModelMapper windscreen_lower_r1;
	private final ModelMapper front_lower_r1;
	private final ModelMapper front_upper_r1;
	private final ModelMapper tail;
	private final ModelMapper top_r1;
	private final ModelMapper bottom_back_r1;
	private final ModelMapper bottom_r2;
	private final ModelMapper bottom_right_r2;
	private final ModelMapper bottom_left_r2;
	private final ModelMapper side_right_r1;
	private final ModelMapper side_left_r1;
	private final ModelMapper right_roof_top_r1;
	private final ModelMapper left_roof_top_r1;
	private final ModelMapper right_wall_top_r2;
	private final ModelMapper left_wall_top_r2;
	private final ModelMapper right_wall_back_r1;
	private final ModelMapper left_wall_back_r1;
	private final ModelMapper right_wall_r2;
	private final ModelMapper left_wall_r2;
	private final ModelMapper left_wing;
	private final ModelMapper edge_wing_bottom_r1;
	private final ModelMapper edge_wing_middle_r1;
	private final ModelMapper outer_bottom_cover_r1;
	private final ModelMapper outer_top_cover_r1;
	private final ModelMapper middle_top_cover_r1;
	private final ModelMapper back_flat_bottom_edge_r1;
	private final ModelMapper back_flat_top_edge_r1;
	private final ModelMapper back_outer_bottom_edge_r1;
	private final ModelMapper back_outer_top_edge_r1;
	private final ModelMapper back_middle_bottom_edge_r1;
	private final ModelMapper back_middle_top_edge_r1;
	private final ModelMapper front_outer_top_edge_r1;
	private final ModelMapper front_outer_top_edge_r2;
	private final ModelMapper front_middle_bottom_edge_r1;
	private final ModelMapper front_middle_top_edge_r1;
	private final ModelMapper right_wing;
	private final ModelMapper edge_wing_bottom_r2;
	private final ModelMapper edge_wing_middle_r2;
	private final ModelMapper outer_bottom_cover_r2;
	private final ModelMapper outer_top_cover_r2;
	private final ModelMapper middle_top_cover_r2;
	private final ModelMapper back_flat_bottom_edge_r2;
	private final ModelMapper back_flat_top_edge_r2;
	private final ModelMapper back_outer_bottom_edge_r2;
	private final ModelMapper back_outer_top_edge_r2;
	private final ModelMapper back_middle_bottom_edge_r2;
	private final ModelMapper back_middle_top_edge_r2;
	private final ModelMapper front_outer_top_edge_r3;
	private final ModelMapper front_outer_top_edge_r4;
	private final ModelMapper front_middle_bottom_edge_r2;
	private final ModelMapper front_middle_top_edge_r2;
	private final ModelMapper back_top_wing;
	private final ModelMapper back_r1;
	private final ModelMapper front_bottom_r1;
	private final ModelMapper front_top_r1;
	private final ModelMapper back_left_wing;
	private final ModelMapper back_r2;
	private final ModelMapper side_r1;
	private final ModelMapper front_r1;
	private final ModelMapper back_right_wing;
	private final ModelMapper back_r3;
	private final ModelMapper side_r2;
	private final ModelMapper front_r2;
	private final ModelMapper bottom;
	private final ModelMapper back_top_right_r1;
	private final ModelMapper back_top_left_r1;
	private final ModelMapper back_side_right_r1;
	private final ModelMapper back_side_left_r1;
	private final ModelMapper back_bottom_right_r1;
	private final ModelMapper back_bottom_left_r1;
	private final ModelMapper front_top_right_r1;
	private final ModelMapper front_top_left_r1;
	private final ModelMapper front_side_right_r1;
	private final ModelMapper front_side_left_r1;
	private final ModelMapper front_bottom_right_r1;
	private final ModelMapper front_bottom_left_r1;
	private final ModelMapper side_right_r2;
	private final ModelMapper side_left_r2;
	private final ModelMapper bottom_right_r3;
	private final ModelMapper bottom_left_r3;
	private final ModelMapper back_side_r1;
	private final ModelMapper back_bottom_r1;
	private final ModelMapper front_side_r1;
	private final ModelMapper front_bottom_r2;
	private final ModelMapper left_engine;
	private final ModelMapper tail_right_roof_r1;
	private final ModelMapper tail_left_roof_r1;
	private final ModelMapper tail_roof_r1;
	private final ModelMapper tail_right_wall_bottom_r1;
	private final ModelMapper tail_right_wall_top_r1;
	private final ModelMapper tail_right_wall_r1;
	private final ModelMapper tail_left_wall_bottom_r1;
	private final ModelMapper tail_left_wall_top_r1;
	private final ModelMapper tail_left_wall_r1;
	private final ModelMapper tail_right_floor_r1;
	private final ModelMapper tail_left_floor_r1;
	private final ModelMapper tail_floor_r1;
	private final ModelMapper back_right_roof_r1;
	private final ModelMapper back_left_roof_r1;
	private final ModelMapper back_roof_r1;
	private final ModelMapper back_right_wall_bottom_r1;
	private final ModelMapper back_right_wall_top_r1;
	private final ModelMapper back_right_wall_r1;
	private final ModelMapper back_left_wall_bottom_r1;
	private final ModelMapper back_left_wall_top_r1;
	private final ModelMapper back_left_wall_r1;
	private final ModelMapper back_right_floor_r1;
	private final ModelMapper back_left_floor_r1;
	private final ModelMapper back_floor_r1;
	private final ModelMapper front_right_roof_r1;
	private final ModelMapper front_left_roof_r1;
	private final ModelMapper front_roof_r1;
	private final ModelMapper front_right_wall_bottom_r1;
	private final ModelMapper front_right_wall_top_r1;
	private final ModelMapper front_right_wall_r1;
	private final ModelMapper front_left_wall_bottom_r1;
	private final ModelMapper front_left_wall_top_r1;
	private final ModelMapper front_left_wall_r1;
	private final ModelMapper front_right_floor_r1;
	private final ModelMapper front_left_floor_r1;
	private final ModelMapper front_floor_r1;
	private final ModelMapper right_roof_r2;
	private final ModelMapper left_roof_r2;
	private final ModelMapper right_wall_bottom_r1;
	private final ModelMapper right_wall_top_r3;
	private final ModelMapper left_wall_bottom_r1;
	private final ModelMapper left_wall_top_r3;
	private final ModelMapper right_floor_r2;
	private final ModelMapper left_floor_r2;
	private final ModelMapper support_back_r1;
	private final ModelMapper support_top_r1;
	private final ModelMapper right_engine;
	private final ModelMapper tail_right_roof_r2;
	private final ModelMapper tail_left_roof_r2;
	private final ModelMapper tail_roof_r2;
	private final ModelMapper tail_right_wall_bottom_r2;
	private final ModelMapper tail_right_wall_top_r2;
	private final ModelMapper tail_right_wall_r2;
	private final ModelMapper tail_left_wall_bottom_r2;
	private final ModelMapper tail_left_wall_top_r2;
	private final ModelMapper tail_left_wall_r2;
	private final ModelMapper tail_right_floor_r2;
	private final ModelMapper tail_left_floor_r2;
	private final ModelMapper tail_floor_r2;
	private final ModelMapper back_right_roof_r2;
	private final ModelMapper back_left_roof_r2;
	private final ModelMapper back_roof_r2;
	private final ModelMapper back_right_wall_bottom_r2;
	private final ModelMapper back_right_wall_top_r2;
	private final ModelMapper back_right_wall_r2;
	private final ModelMapper back_left_wall_bottom_r2;
	private final ModelMapper back_left_wall_top_r2;
	private final ModelMapper back_left_wall_r2;
	private final ModelMapper back_right_floor_r2;
	private final ModelMapper back_left_floor_r2;
	private final ModelMapper back_floor_r2;
	private final ModelMapper front_right_roof_r2;
	private final ModelMapper front_left_roof_r2;
	private final ModelMapper front_roof_r2;
	private final ModelMapper front_right_wall_bottom_r2;
	private final ModelMapper front_right_wall_top_r2;
	private final ModelMapper front_right_wall_r2;
	private final ModelMapper front_left_wall_bottom_r2;
	private final ModelMapper front_left_wall_top_r2;
	private final ModelMapper front_left_wall_r2;
	private final ModelMapper front_right_floor_r2;
	private final ModelMapper front_left_floor_r2;
	private final ModelMapper front_floor_r2;
	private final ModelMapper right_roof_r3;
	private final ModelMapper left_roof_r3;
	private final ModelMapper right_wall_bottom_r2;
	private final ModelMapper right_wall_top_r4;
	private final ModelMapper left_wall_bottom_r2;
	private final ModelMapper left_wall_top_r4;
	private final ModelMapper right_floor_r3;
	private final ModelMapper left_floor_r3;
	private final ModelMapper support_back_r2;
	private final ModelMapper support_top_r2;
	private final ModelMapper window_interior;
	private final ModelMapper roof_side_r1;
	private final ModelMapper luggage_rack_top_r1;
	private final ModelMapper luggage_rack_front_r1;
	private final ModelMapper luggage_rack_bottom_front_r1;
	private final ModelMapper right_top_wall_r2;
	private final ModelMapper window_interior_wall;
	private final ModelMapper right_upper_wall_r1;
	private final ModelMapper right_lower_wall_r1;
	private final ModelMapper window_interior_light;
	private final ModelMapper light_r1;
	private final ModelMapper window_interior_blank;
	private final ModelMapper roof_side_r2;
	private final ModelMapper luggage_rack_top_r2;
	private final ModelMapper luggage_rack_front_r2;
	private final ModelMapper luggage_rack_bottom_front_r2;
	private final ModelMapper right_top_wall_r3;
	private final ModelMapper window_interior_blank_wall;
	private final ModelMapper right_upper_wall_r2;
	private final ModelMapper right_lower_wall_r2;
	private final ModelMapper window_interior_blank_light;
	private final ModelMapper light_r2;
	private final ModelMapper door_left_exterior;
	private final ModelMapper right_wall_front_r2;
	private final ModelMapper right_top_wall_front_r1;
	private final ModelMapper right_top_wall_r4;
	private final ModelMapper top_back_r2;
	private final ModelMapper door_left_interior;
	private final ModelMapper right_wall_front_r3;
	private final ModelMapper right_top_wall_front_r2;
	private final ModelMapper right_top_wall_r5;
	private final ModelMapper top_back_r3;
	private final ModelMapper door_right_exterior;
	private final ModelMapper right_wall_front_r4;
	private final ModelMapper right_top_wall_front_r3;
	private final ModelMapper right_top_wall_r6;
	private final ModelMapper top_back_r4;
	private final ModelMapper door_right_interior;
	private final ModelMapper right_wall_front_r5;
	private final ModelMapper right_top_wall_front_r4;
	private final ModelMapper right_top_wall_r7;
	private final ModelMapper top_back_r5;
	private final ModelMapper head_interior;
	private final ModelMapper right_door_top_r1;
	private final ModelMapper left_door_top_r1;
	private final ModelMapper roof_front_r1;
	private final ModelMapper right_roof_side_r1;
	private final ModelMapper left_roof_side_r1;
	private final ModelMapper right_upper_wall_r3;
	private final ModelMapper left_upper_wall_r1;
	private final ModelMapper right_wall_r3;
	private final ModelMapper left_wall_r3;
	private final ModelMapper right_lower_wall_r3;
	private final ModelMapper left_lower_wall_r1;
	private final ModelMapper end_interior;
	private final ModelMapper right_luggage_rack_top_r1;
	private final ModelMapper left_luggage_rack_top_r1;
	private final ModelMapper right_luggage_rack_front_r1;
	private final ModelMapper left_luggage_rack_front_r1;
	private final ModelMapper right_luggage_rack_bottom_front_r1;
	private final ModelMapper left_luggage_rack_bottom_front_r1;
	private final ModelMapper right_top_wall_r8;
	private final ModelMapper left_top_wall_r2;
	private final ModelMapper right_roof_side_r2;
	private final ModelMapper left_roof_side_r2;
	private final ModelMapper right_upper_wall_r4;
	private final ModelMapper left_upper_wall_r2;
	private final ModelMapper right_wall_r4;
	private final ModelMapper left_wall_r4;
	private final ModelMapper right_lower_wall_r4;
	private final ModelMapper left_lower_wall_r2;
	private final ModelMapper end_light;
	private final ModelMapper left_light_r1;
	private final ModelMapper right_light_r1;
	private final ModelMapper emergency_exit;
	private final ModelMapper right_upper_wall_r5;
	private final ModelMapper right_lower_wall_r5;
	private final ModelMapper left_upper_wall_r3;
	private final ModelMapper left_lower_wall_r3;
	private final ModelMapper seat_nice;
	private final ModelMapper back_bottom_right_r2;
	private final ModelMapper seat_normal;
	private final ModelMapper back_bottom_right_r3;

	public ModelA320() {
		this(DoorAnimationType.PLUG_SLOW, true);
	}

	protected ModelA320(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		super(doorAnimationType, renderDoorOverlay);

		final int textureWidth = 1024;
		final int textureHeight = 1024;

		final ModelDataWrapper modelDataWrapper = new ModelDataWrapper(this, textureWidth, textureHeight);

		exterior = new ModelMapper(modelDataWrapper);
		exterior.setPos(0, 24, 0);


		main = new ModelMapper(modelDataWrapper);
		main.setPos(0, 0, 0);
		exterior.addChild(main);
		main.texOffs(0, 0).addBox(-9, -33, -191, 18, 0, 367, 0, false);
		main.texOffs(43, 0).addBox(-9, 33, -251, 18, 0, 360, 0, false);
		main.texOffs(0, 20).addBox(32, -10, -225, 0, 20, 380, 0, false);
		main.texOffs(0, 0).addBox(-32, -10, -225, 0, 20, 380, 0, false);

		right_roof_r1 = new ModelMapper(modelDataWrapper);
		right_roof_r1.setPos(-9, -33, 0);
		main.addChild(right_roof_r1);
		setRotationAngle(right_roof_r1, 0, 0, -0.5236F);
		right_roof_r1.texOffs(161, 0).addBox(-17, 0, -191, 17, 0, 346, 0, false);

		left_roof_r1 = new ModelMapper(modelDataWrapper);
		left_roof_r1.setPos(9, -33, 0);
		main.addChild(left_roof_r1);
		setRotationAngle(left_roof_r1, 0, 0, 0.5236F);
		left_roof_r1.texOffs(195, 0).addBox(0, 0, -191, 17, 0, 346, 0, false);

		right_top_wall_r1 = new ModelMapper(modelDataWrapper);
		right_top_wall_r1.setPos(-32, -10, 0);
		main.addChild(right_top_wall_r1);
		setRotationAngle(right_top_wall_r1, 0, 0, 0.5236F);
		right_top_wall_r1.texOffs(0, 40).addBox(0, -17, -225, 0, 17, 380, 0, false);

		left_top_wall_r1 = new ModelMapper(modelDataWrapper);
		left_top_wall_r1.setPos(32, -10, 0);
		main.addChild(left_top_wall_r1);
		setRotationAngle(left_top_wall_r1, 0, 0, -0.5236F);
		left_top_wall_r1.texOffs(0, 57).addBox(0, -17, -225, 0, 17, 380, 0, false);

		right_bottom_wall_r1 = new ModelMapper(modelDataWrapper);
		right_bottom_wall_r1.setPos(-32, 10, 0);
		main.addChild(right_bottom_wall_r1);
		setRotationAngle(right_bottom_wall_r1, 0, 0, -0.5236F);
		right_bottom_wall_r1.texOffs(0, 111).addBox(0, 0, -251, 0, 17, 360, 0, false);

		left_bottom_wall_r1 = new ModelMapper(modelDataWrapper);
		left_bottom_wall_r1.setPos(32, 10, 0);
		main.addChild(left_bottom_wall_r1);
		setRotationAngle(left_bottom_wall_r1, 0, 0, 0.5236F);
		left_bottom_wall_r1.texOffs(0, 94).addBox(0, 0, -251, 0, 17, 360, 0, false);

		right_floor_r1 = new ModelMapper(modelDataWrapper);
		right_floor_r1.setPos(-9, 33, 0);
		main.addChild(right_floor_r1);
		setRotationAngle(right_floor_r1, 0, 0, 0.5236F);
		right_floor_r1.texOffs(113, 0).addBox(-17, 0, -251, 17, 0, 360, 0, false);

		left_floor_r1 = new ModelMapper(modelDataWrapper);
		left_floor_r1.setPos(9, 33, 0);
		main.addChild(left_floor_r1);
		setRotationAngle(left_floor_r1, 0, 0, -0.5236F);
		left_floor_r1.texOffs(79, 0).addBox(0, 0, -251, 17, 0, 360, 0, false);

		front = new ModelMapper(modelDataWrapper);
		front.setPos(0, 0, 0);
		exterior.addChild(front);
		front.texOffs(52, 110).addBox(-3, 8, -301, 6, 6, 0, 0, false);
		front.texOffs(124, 897).addBox(17, -24, -229, 15, 32, 0, 0, false);
		front.texOffs(124, 897).addBox(-32, -24, -229, 15, 32, 0, 0, true);
		front.texOffs(154, 900).addBox(18, -25, -213, 14, 33, 0, 0, false);
		front.texOffs(154, 900).addBox(-32, -25, -213, 14, 33, 0, 0, true);

		top_right_back_r1 = new ModelMapper(modelDataWrapper);
		top_right_back_r1.setPos(-9, -33, -191);
		front.addChild(top_right_back_r1);
		setRotationAngle(top_right_back_r1, 0.0873F, 0, -0.5236F);
		top_right_back_r1.texOffs(0, 0).addBox(-22, 0, -64, 22, 0, 64, 0, false);

		top_left_back_r1 = new ModelMapper(modelDataWrapper);
		top_left_back_r1.setPos(9, -33, -191);
		front.addChild(top_left_back_r1);
		setRotationAngle(top_left_back_r1, 0.0873F, 0, 0.5236F);
		top_left_back_r1.texOffs(0, 64).addBox(0, 0, -64, 22, 0, 64, 0, false);

		bottom_right_r1 = new ModelMapper(modelDataWrapper);
		bottom_right_r1.setPos(-9, 33, -251);
		front.addChild(bottom_right_r1);
		setRotationAngle(bottom_right_r1, -0.1396F, 0, 0.5236F);
		bottom_right_r1.texOffs(72, 581).addBox(-17, 0, -22, 17, 0, 22, 0, false);

		bottom_left_r1 = new ModelMapper(modelDataWrapper);
		bottom_left_r1.setPos(9, 33, -251);
		front.addChild(bottom_left_r1);
		setRotationAngle(bottom_left_r1, -0.1396F, 0, -0.5236F);
		bottom_left_r1.texOffs(305, 606).addBox(0, 0, -22, 17, 0, 22, 0, false);

		bottom_front_right_r1 = new ModelMapper(modelDataWrapper);
		bottom_front_right_r1.setPos(-3, 29, -280);
		front.addChild(bottom_front_right_r1);
		setRotationAngle(bottom_front_right_r1, -0.4363F, 0, 0.5236F);
		bottom_front_right_r1.texOffs(78, 109).addBox(-18, 0, -21, 18, 0, 30, 0, false);

		bottom_front_left_r1 = new ModelMapper(modelDataWrapper);
		bottom_front_left_r1.setPos(3, 29, -280);
		front.addChild(bottom_front_left_r1);
		setRotationAngle(bottom_front_left_r1, -0.4363F, 0, -0.5236F);
		bottom_front_left_r1.texOffs(201, 109).addBox(0, 0, -21, 18, 0, 30, 0, false);

		windscreen_lower_right_r1 = new ModelMapper(modelDataWrapper);
		windscreen_lower_right_r1.setPos(-9, -7, -280);
		front.addChild(windscreen_lower_right_r1);
		setRotationAngle(windscreen_lower_right_r1, -0.6109F, 1.0472F, 0);
		windscreen_lower_right_r1.texOffs(758, 593).addBox(-39, 0, 0, 56, 10, 0, 0, false);

		windscreen_lower_left_r1 = new ModelMapper(modelDataWrapper);
		windscreen_lower_left_r1.setPos(9, -7, -280);
		front.addChild(windscreen_lower_left_r1);
		setRotationAngle(windscreen_lower_left_r1, -0.6109F, -1.0472F, 0);
		windscreen_lower_left_r1.texOffs(760, 375).addBox(-17, 0, 0, 56, 10, 0, 0, false);

		windscreen_right_r1 = new ModelMapper(modelDataWrapper);
		windscreen_right_r1.setPos(-9, -7, -280);
		front.addChild(windscreen_right_r1);
		setRotationAngle(windscreen_right_r1, -0.5236F, 1.0472F, 0);
		windscreen_right_r1.texOffs(68, 766).addBox(-42, -21, 0, 42, 21, 0, 0, false);

		windscreen_left_r1 = new ModelMapper(modelDataWrapper);
		windscreen_left_r1.setPos(9, -7, -280);
		front.addChild(windscreen_left_r1);
		setRotationAngle(windscreen_left_r1, -0.5236F, -1.0472F, 0);
		windscreen_left_r1.texOffs(767, 754).addBox(0, -21, 0, 42, 21, 0, 0, false);

		right_wall_front_r1 = new ModelMapper(modelDataWrapper);
		right_wall_front_r1.setPos(-3, -1, -301);
		front.addChild(right_wall_front_r1);
		setRotationAngle(right_wall_front_r1, 0, -0.5236F, 0);
		right_wall_front_r1.texOffs(0, 511).addBox(0, -2, 0, 0, 27, 45, 0, false);

		left_wall_front_r1 = new ModelMapper(modelDataWrapper);
		left_wall_front_r1.setPos(3, -1, -301);
		front.addChild(left_wall_front_r1);
		setRotationAngle(left_wall_front_r1, 0, 0.5236F, 0);
		left_wall_front_r1.texOffs(0, 538).addBox(0, -2, 0, 0, 27, 45, 0, false);

		right_wall_top_r1 = new ModelMapper(modelDataWrapper);
		right_wall_top_r1.setPos(-32, -10, -225);
		front.addChild(right_wall_top_r1);
		setRotationAngle(right_wall_top_r1, 0, -0.1745F, 0.5236F);
		right_wall_top_r1.texOffs(577, 714).addBox(0, -13, -19, 0, 13, 19, 0, false);

		left_wall_top_r1 = new ModelMapper(modelDataWrapper);
		left_wall_top_r1.setPos(32, -10, -225);
		front.addChild(left_wall_top_r1);
		setRotationAngle(left_wall_top_r1, 0, 0.1745F, -0.5236F);
		left_wall_top_r1.texOffs(577, 727).addBox(0, -13, -19, 0, 13, 19, 0, false);

		right_wall_lower_r1 = new ModelMapper(modelDataWrapper);
		right_wall_lower_r1.setPos(-32, 10, -251);
		front.addChild(right_wall_lower_r1);
		setRotationAngle(right_wall_lower_r1, 0, -0.1745F, -0.5236F);
		right_wall_lower_r1.texOffs(220, 680).addBox(0, 9, -22, 0, 9, 22, 0, false);

		left_wall_lower_r1 = new ModelMapper(modelDataWrapper);
		left_wall_lower_r1.setPos(32, 10, -251);
		front.addChild(left_wall_lower_r1);
		setRotationAngle(left_wall_lower_r1, 0, 0.1745F, 0.5236F);
		left_wall_lower_r1.texOffs(627, 721).addBox(0, 9, -22, 0, 9, 22, 0, false);

		right_wall_r1 = new ModelMapper(modelDataWrapper);
		right_wall_r1.setPos(-32, 0, -225);
		front.addChild(right_wall_r1);
		setRotationAngle(right_wall_r1, 0, -0.1745F, 0);
		right_wall_r1.texOffs(0, 137).addBox(0, -10, -38, 0, 28, 38, 0, false);

		left_wall_r1 = new ModelMapper(modelDataWrapper);
		left_wall_r1.setPos(32, 0, -225);
		front.addChild(left_wall_r1);
		setRotationAngle(left_wall_r1, 0, 0.1745F, 0);
		left_wall_r1.texOffs(0, 165).addBox(0, -10, -38, 0, 28, 38, 0, false);

		top_back_r1 = new ModelMapper(modelDataWrapper);
		top_back_r1.setPos(0, -33, -191);
		front.addChild(top_back_r1);
		setRotationAngle(top_back_r1, 0.0873F, 0, 0);
		top_back_r1.texOffs(128, 685).addBox(-9, 0, -56, 18, 0, 56, 0, false);

		top_front_r1 = new ModelMapper(modelDataWrapper);
		top_front_r1.setPos(0, -24.5002F, -254.5527F);
		front.addChild(top_front_r1);
		setRotationAngle(top_front_r1, 0.3491F, 0, 0);
		top_front_r1.texOffs(556, 109).addBox(-12, -0.5F, -9.5F, 24, 0, 19, 0, false);

		bottom_r1 = new ModelMapper(modelDataWrapper);
		bottom_r1.setPos(0, 33, -251);
		front.addChild(bottom_r1);
		setRotationAngle(bottom_r1, -0.1396F, 0, 0);
		bottom_r1.texOffs(545, 79).addBox(-9, 0, -30, 18, 0, 30, 0, false);

		bottom_front_r1 = new ModelMapper(modelDataWrapper);
		bottom_front_r1.setPos(0, 29, -280);
		front.addChild(bottom_front_r1);
		setRotationAngle(bottom_front_r1, -0.4363F, 0, 0);
		bottom_front_r1.texOffs(114, 86).addBox(-3, 0, -19, 6, 0, 19, 0, false);

		windscreen_main_r1 = new ModelMapper(modelDataWrapper);
		windscreen_main_r1.setPos(0, -7, -280);
		front.addChild(windscreen_main_r1);
		setRotationAngle(windscreen_main_r1, 0.733F, 0, 0);
		windscreen_main_r1.texOffs(304, 317).addBox(-9, 0, 0, 18, 0, 22, 0, false);

		windscreen_lower_r1 = new ModelMapper(modelDataWrapper);
		windscreen_lower_r1.setPos(0, -7, -280);
		front.addChild(windscreen_lower_r1);
		setRotationAngle(windscreen_lower_r1, 0.4363F, 0, 0);
		windscreen_lower_r1.texOffs(558, 714).addBox(-9, 0, -19, 18, 0, 19, 0, false);

		front_lower_r1 = new ModelMapper(modelDataWrapper);
		front_lower_r1.setPos(0, 14, -301);
		front.addChild(front_lower_r1);
		setRotationAngle(front_lower_r1, -1.0472F, 0, 0);
		front_lower_r1.texOffs(127, 307).addBox(-5, 0, 0, 10, 0, 9, 0, false);

		front_upper_r1 = new ModelMapper(modelDataWrapper);
		front_upper_r1.setPos(0, 8, -301);
		front.addChild(front_upper_r1);
		setRotationAngle(front_upper_r1, 1.0472F, 0, 0);
		front_upper_r1.texOffs(327, 258).addBox(-6, 0, 0, 12, 0, 9, 0, false);

		tail = new ModelMapper(modelDataWrapper);
		tail.setPos(0, 0, 0);
		exterior.addChild(tail);
		tail.texOffs(100, 256).addBox(-3, -22, 301, 6, 11, 0, 0, false);

		top_r1 = new ModelMapper(modelDataWrapper);
		top_r1.setPos(0, -22, 301);
		tail.addChild(top_r1);
		setRotationAngle(top_r1, -0.0873F, 0, 0);
		top_r1.texOffs(155, 109).addBox(-6, 0, -127, 12, 0, 127, 0, false);

		bottom_back_r1 = new ModelMapper(modelDataWrapper);
		bottom_back_r1.setPos(0, -11, 301);
		tail.addChild(bottom_back_r1);
		setRotationAngle(bottom_back_r1, 0.2618F, 0, 0);
		bottom_back_r1.texOffs(0, 175).addBox(-11, 0, -115, 22, 0, 115, 0, false);

		bottom_r2 = new ModelMapper(modelDataWrapper);
		bottom_r2.setPos(0, 33, 109);
		tail.addChild(bottom_r2);
		setRotationAngle(bottom_r2, 0.1745F, 0, 0);
		bottom_r2.texOffs(223, 109).addBox(-11, 0, 0, 22, 0, 83, 0, false);

		bottom_right_r2 = new ModelMapper(modelDataWrapper);
		bottom_right_r2.setPos(-9, 33, 109);
		tail.addChild(bottom_right_r2);
		setRotationAngle(bottom_right_r2, 0.1396F, 0, 0.5236F);
		bottom_right_r2.texOffs(497, 165).addBox(-17, 0, 0, 17, 0, 78, 0, false);

		bottom_left_r2 = new ModelMapper(modelDataWrapper);
		bottom_left_r2.setPos(9, 33, 109);
		tail.addChild(bottom_left_r2);
		setRotationAngle(bottom_left_r2, 0.1396F, 0, -0.5236F);
		bottom_left_r2.texOffs(531, 165).addBox(0, 0, 0, 17, 0, 78, 0, false);

		side_right_r1 = new ModelMapper(modelDataWrapper);
		side_right_r1.setPos(-32, 10, 109);
		tail.addChild(side_right_r1);
		setRotationAngle(side_right_r1, 0, 0.1745F, -0.5236F);
		side_right_r1.texOffs(0, 297).addBox(0, -15, 0, 0, 34, 191, 0, false);

		side_left_r1 = new ModelMapper(modelDataWrapper);
		side_left_r1.setPos(32, 10, 109);
		tail.addChild(side_left_r1);
		setRotationAngle(side_left_r1, 0, -0.1745F, 0.5236F);
		side_left_r1.texOffs(0, 331).addBox(0, -15, 0, 0, 34, 191, 0, false);

		right_roof_top_r1 = new ModelMapper(modelDataWrapper);
		right_roof_top_r1.setPos(-9, -33, 155);
		tail.addChild(right_roof_top_r1);
		setRotationAngle(right_roof_top_r1, -0.0873F, 0, -0.5236F);
		right_roof_top_r1.texOffs(0, 0).addBox(-17, 0, 0, 21, 0, 147, 0, false);

		left_roof_top_r1 = new ModelMapper(modelDataWrapper);
		left_roof_top_r1.setPos(9, -33, 155);
		tail.addChild(left_roof_top_r1);
		setRotationAngle(left_roof_top_r1, -0.0873F, 0, 0.5236F);
		left_roof_top_r1.texOffs(42, 0).addBox(-4, 0, 0, 21, 0, 147, 0, false);

		right_wall_top_r2 = new ModelMapper(modelDataWrapper);
		right_wall_top_r2.setPos(-32, -10, 155);
		tail.addChild(right_wall_top_r2);
		setRotationAngle(right_wall_top_r2, 0, 0.0873F, 0.5236F);
		right_wall_top_r2.texOffs(0, 214).addBox(0, -17, 0, 0, 17, 76, 0, false);

		left_wall_top_r2 = new ModelMapper(modelDataWrapper);
		left_wall_top_r2.setPos(32, -10, 155);
		tail.addChild(left_wall_top_r2);
		setRotationAngle(left_wall_top_r2, 0, -0.0873F, -0.5236F);
		left_wall_top_r2.texOffs(184, 592).addBox(0, -17, 0, 0, 17, 76, 0, false);

		right_wall_back_r1 = new ModelMapper(modelDataWrapper);
		right_wall_back_r1.setPos(-3, 0, 301);
		tail.addChild(right_wall_back_r1);
		setRotationAngle(right_wall_back_r1, 0, 0.5236F, 0);
		right_wall_back_r1.texOffs(342, 329).addBox(0, -22, -10, 0, 13, 10, 0, false);

		left_wall_back_r1 = new ModelMapper(modelDataWrapper);
		left_wall_back_r1.setPos(3, 0, 301);
		tail.addChild(left_wall_back_r1);
		setRotationAngle(left_wall_back_r1, 0, -0.5236F, 0);
		left_wall_back_r1.texOffs(865, 205).addBox(0, -22, -10, 0, 13, 10, 0, false);

		right_wall_r2 = new ModelMapper(modelDataWrapper);
		right_wall_r2.setPos(-32, 0, 155);
		tail.addChild(right_wall_r2);
		setRotationAngle(right_wall_r2, 0, 0.1745F, 0);
		right_wall_r2.texOffs(0, 6).addBox(0, -20, 0, 0, 14, 141, 0, false);

		left_wall_r2 = new ModelMapper(modelDataWrapper);
		left_wall_r2.setPos(32, 0, 155);
		tail.addChild(left_wall_r2);
		setRotationAngle(left_wall_r2, 0, -0.1745F, 0);
		left_wall_r2.texOffs(0, 20).addBox(0, -20, 0, 0, 14, 141, 0, false);

		left_wing = new ModelMapper(modelDataWrapper);
		left_wing.setPos(0, 0, 0);
		exterior.addChild(left_wing);
		left_wing.texOffs(410, 678).addBox(271, -58, 31, 0, 24, 36, 0, false);
		left_wing.texOffs(633, 725).addBox(76, 8, -42, 5, 7, 56, 0, false);
		left_wing.texOffs(511, 721).addBox(131, 3, -32, 5, 7, 56, 0, false);
		left_wing.texOffs(758, 502).addBox(189, -3, -6, 5, 7, 50, 0, false);

		edge_wing_bottom_r1 = new ModelMapper(modelDataWrapper);
		edge_wing_bottom_r1.setPos(255, -18, 39);
		left_wing.addChild(edge_wing_bottom_r1);
		setRotationAngle(edge_wing_bottom_r1, 0, 0, 1.0472F);
		edge_wing_bottom_r1.texOffs(0, 574).addBox(0, -12, -24, 0, 24, 36, 0, false);

		edge_wing_middle_r1 = new ModelMapper(modelDataWrapper);
		edge_wing_middle_r1.setPos(271, -34, 39);
		left_wing.addChild(edge_wing_middle_r1);
		setRotationAngle(edge_wing_middle_r1, 0, 0, 0.5236F);
		edge_wing_middle_r1.texOffs(0, 598).addBox(0, 0, -18, 0, 12, 36, 0, false);

		outer_bottom_cover_r1 = new ModelMapper(modelDataWrapper);
		outer_bottom_cover_r1.setPos(255, -14, 35);
		left_wing.addChild(outer_bottom_cover_r1);
		setRotationAngle(outer_bottom_cover_r1, 0, -0.4712F, -0.1745F);
		outer_bottom_cover_r1.texOffs(557, 147).addBox(-135, 0, 0, 125, 0, 18, 0, false);

		outer_top_cover_r1 = new ModelMapper(modelDataWrapper);
		outer_top_cover_r1.setPos(255, -18, 35);
		left_wing.addChild(outer_top_cover_r1);
		setRotationAngle(outer_top_cover_r1, 0, -0.4712F, -0.1745F);
		outer_top_cover_r1.texOffs(685, 644).addBox(-72, 0, 0, 79, 0, 13, 0, false);

		middle_top_cover_r1 = new ModelMapper(modelDataWrapper);
		middle_top_cover_r1.setPos(32, 7, -80);
		left_wing.addChild(middle_top_cover_r1);
		setRotationAngle(middle_top_cover_r1, 0, -0.4712F, -0.0873F);
		middle_top_cover_r1.texOffs(511, 0).addBox(0, 0, 0, 189, 0, 64, 0, false);

		back_flat_bottom_edge_r1 = new ModelMapper(modelDataWrapper);
		back_flat_bottom_edge_r1.setPos(32, 13, -9);
		left_wing.addChild(back_flat_bottom_edge_r1);
		setRotationAngle(back_flat_bottom_edge_r1, 0.2618F, 0, -0.0873F);
		back_flat_bottom_edge_r1.texOffs(575, 79).addBox(-9, -1, -37, 85, 1, 49, 0, false);

		back_flat_top_edge_r1 = new ModelMapper(modelDataWrapper);
		back_flat_top_edge_r1.setPos(32, 7, -9);
		left_wing.addChild(back_flat_top_edge_r1);
		setRotationAngle(back_flat_top_edge_r1, -0.0873F, 0, -0.0873F);
		back_flat_top_edge_r1.texOffs(720, 474).addBox(-1, 0, 0, 74, 1, 12, 0, false);

		back_outer_bottom_edge_r1 = new ModelMapper(modelDataWrapper);
		back_outer_bottom_edge_r1.setPos(255, -14, 35);
		left_wing.addChild(back_outer_bottom_edge_r1);
		setRotationAngle(back_outer_bottom_edge_r1, 0.0873F, -0.2793F, -0.1745F);
		back_outer_bottom_edge_r1.texOffs(652, 697).addBox(-88, -1, 0, 85, 1, 12, 0, false);

		back_outer_top_edge_r1 = new ModelMapper(modelDataWrapper);
		back_outer_top_edge_r1.setPos(255, -18, 35);
		left_wing.addChild(back_outer_top_edge_r1);
		setRotationAngle(back_outer_top_edge_r1, -0.1745F, -0.2793F, -0.1745F);
		back_outer_top_edge_r1.texOffs(725, 281).addBox(-66, 0, 0, 66, 1, 12, 0, false);

		back_middle_bottom_edge_r1 = new ModelMapper(modelDataWrapper);
		back_middle_bottom_edge_r1.setPos(32, 13, -30);
		left_wing.addChild(back_middle_bottom_edge_r1);
		setRotationAngle(back_middle_bottom_edge_r1, 0.2618F, -0.2793F, -0.0873F);
		back_middle_bottom_edge_r1.texOffs(94, 606).addBox(73, -1, -17, 102, 1, 29, 0, false);

		back_middle_top_edge_r1 = new ModelMapper(modelDataWrapper);
		back_middle_top_edge_r1.setPos(32, 7, -30);
		left_wing.addChild(back_middle_top_edge_r1);
		setRotationAngle(back_middle_top_edge_r1, -0.0873F, -0.2793F, -0.0873F);
		back_middle_top_edge_r1.texOffs(507, 347).addBox(-10, 0, 0, 194, 1, 12, 0, false);

		front_outer_top_edge_r1 = new ModelMapper(modelDataWrapper);
		front_outer_top_edge_r1.setPos(255, -14, 35);
		left_wing.addChild(front_outer_top_edge_r1);
		setRotationAngle(front_outer_top_edge_r1, -0.0873F, -0.4712F, -0.1745F);
		front_outer_top_edge_r1.texOffs(460, 684).addBox(-98, -1, -14, 88, 1, 14, 0, false);

		front_outer_top_edge_r2 = new ModelMapper(modelDataWrapper);
		front_outer_top_edge_r2.setPos(255, -18, 35);
		left_wing.addChild(front_outer_top_edge_r2);
		setRotationAngle(front_outer_top_edge_r2, 0.0873F, -0.4712F, -0.1745F);
		front_outer_top_edge_r2.texOffs(614, 710).addBox(-77, 0, -14, 77, 1, 14, 0, false);

		front_middle_bottom_edge_r1 = new ModelMapper(modelDataWrapper);
		front_middle_bottom_edge_r1.setPos(32, 13, -80);
		left_wing.addChild(front_middle_bottom_edge_r1);
		setRotationAngle(front_middle_bottom_edge_r1, -0.2618F, -0.4712F, -0.0873F);
		front_middle_bottom_edge_r1.texOffs(331, 552).addBox(-7, -1, -14, 188, 1, 51, 0, false);

		front_middle_top_edge_r1 = new ModelMapper(modelDataWrapper);
		front_middle_top_edge_r1.setPos(32, 7, -80);
		left_wing.addChild(front_middle_top_edge_r1);
		setRotationAngle(front_middle_top_edge_r1, 0.0873F, -0.4712F, -0.0873F);
		front_middle_top_edge_r1.texOffs(575, 64).addBox(-8, 0, -14, 191, 1, 14, 0, false);

		right_wing = new ModelMapper(modelDataWrapper);
		right_wing.setPos(0, 0, 0);
		exterior.addChild(right_wing);
		right_wing.texOffs(287, 237).addBox(-271, -58, 31, 0, 24, 36, 0, false);
		right_wing.texOffs(720, 398).addBox(-81, 8, -42, 5, 7, 56, 0, false);
		right_wing.texOffs(445, 714).addBox(-136, 3, -32, 5, 7, 56, 0, false);
		right_wing.texOffs(124, 749).addBox(-194, -3, -6, 5, 7, 50, 0, false);

		edge_wing_bottom_r2 = new ModelMapper(modelDataWrapper);
		edge_wing_bottom_r2.setPos(-255, -18, 39);
		right_wing.addChild(edge_wing_bottom_r2);
		setRotationAngle(edge_wing_bottom_r2, 0, 0, -1.0472F);
		edge_wing_bottom_r2.texOffs(0, 195).addBox(0, -12, -24, 0, 24, 36, 0, false);

		edge_wing_middle_r2 = new ModelMapper(modelDataWrapper);
		edge_wing_middle_r2.setPos(-271, -34, 39);
		right_wing.addChild(edge_wing_middle_r2);
		setRotationAngle(edge_wing_middle_r2, 0, 0, -0.5236F);
		edge_wing_middle_r2.texOffs(287, 268).addBox(0, 0, -18, 0, 12, 36, 0, false);

		outer_bottom_cover_r2 = new ModelMapper(modelDataWrapper);
		outer_bottom_cover_r2.setPos(-255, -14, 35);
		right_wing.addChild(outer_bottom_cover_r2);
		setRotationAngle(outer_bottom_cover_r2, 0, 0.4712F, 0.1745F);
		outer_bottom_cover_r2.texOffs(557, 129).addBox(10, 0, 0, 125, 0, 18, 0, false);

		outer_top_cover_r2 = new ModelMapper(modelDataWrapper);
		outer_top_cover_r2.setPos(-255, -18, 35);
		right_wing.addChild(outer_top_cover_r2);
		setRotationAngle(outer_top_cover_r2, 0, 0.4712F, 0.1745F);
		outer_top_cover_r2.texOffs(685, 631).addBox(-7, 0, 0, 79, 0, 13, 0, false);

		middle_top_cover_r2 = new ModelMapper(modelDataWrapper);
		middle_top_cover_r2.setPos(-32, 7, -80);
		right_wing.addChild(middle_top_cover_r2);
		setRotationAngle(middle_top_cover_r2, 0, 0.4712F, 0.0873F);
		middle_top_cover_r2.texOffs(318, 488).addBox(-189, 0, 0, 189, 0, 64, 0, false);

		back_flat_bottom_edge_r2 = new ModelMapper(modelDataWrapper);
		back_flat_bottom_edge_r2.setPos(-32, 13, -9);
		right_wing.addChild(back_flat_bottom_edge_r2);
		setRotationAngle(back_flat_bottom_edge_r2, 0.2618F, 0, 0.0873F);
		back_flat_bottom_edge_r2.texOffs(94, 556).addBox(-76, -1, -37, 85, 1, 49, 0, false);

		back_flat_top_edge_r2 = new ModelMapper(modelDataWrapper);
		back_flat_top_edge_r2.setPos(-32, 7, -9);
		right_wing.addChild(back_flat_top_edge_r2);
		setRotationAngle(back_flat_top_edge_r2, -0.0873F, 0, 0.0873F);
		back_flat_top_edge_r2.texOffs(720, 461).addBox(-73, 0, 0, 74, 1, 12, 0, false);

		back_outer_bottom_edge_r2 = new ModelMapper(modelDataWrapper);
		back_outer_bottom_edge_r2.setPos(-255, -14, 35);
		right_wing.addChild(back_outer_bottom_edge_r2);
		setRotationAngle(back_outer_bottom_edge_r2, 0.0873F, 0.2793F, 0.1745F);
		back_outer_bottom_edge_r2.texOffs(650, 684).addBox(3, -1, 0, 85, 1, 12, 0, false);

		back_outer_top_edge_r2 = new ModelMapper(modelDataWrapper);
		back_outer_top_edge_r2.setPos(-255, -18, 35);
		right_wing.addChild(back_outer_top_edge_r2);
		setRotationAngle(back_outer_top_edge_r2, -0.1745F, 0.2793F, 0.1745F);
		back_outer_top_edge_r2.texOffs(206, 353).addBox(0, 0, 0, 66, 1, 12, 0, false);

		back_middle_bottom_edge_r2 = new ModelMapper(modelDataWrapper);
		back_middle_bottom_edge_r2.setPos(-32, 13, -30);
		right_wing.addChild(back_middle_bottom_edge_r2);
		setRotationAngle(back_middle_bottom_edge_r2, 0.2618F, 0.2793F, 0.0873F);
		back_middle_bottom_edge_r2.texOffs(374, 604).addBox(-175, -1, -17, 102, 1, 29, 0, false);

		back_middle_top_edge_r2 = new ModelMapper(modelDataWrapper);
		back_middle_top_edge_r2.setPos(-32, 7, -30);
		right_wing.addChild(back_middle_top_edge_r2);
		setRotationAngle(back_middle_top_edge_r2, -0.0873F, 0.2793F, 0.0873F);
		back_middle_top_edge_r2.texOffs(0, 367).addBox(-184, 0, 0, 194, 1, 12, 0, false);

		front_outer_top_edge_r3 = new ModelMapper(modelDataWrapper);
		front_outer_top_edge_r3.setPos(-255, -14, 35);
		right_wing.addChild(front_outer_top_edge_r3);
		setRotationAngle(front_outer_top_edge_r3, -0.0873F, 0.4712F, 0.1745F);
		front_outer_top_edge_r3.texOffs(673, 225).addBox(10, -1, -14, 88, 1, 14, 0, false);

		front_outer_top_edge_r4 = new ModelMapper(modelDataWrapper);
		front_outer_top_edge_r4.setPos(-255, -18, 35);
		right_wing.addChild(front_outer_top_edge_r4);
		setRotationAngle(front_outer_top_edge_r4, 0.0873F, 0.4712F, 0.1745F);
		front_outer_top_edge_r4.texOffs(446, 699).addBox(0, 0, -14, 77, 1, 14, 0, false);

		front_middle_bottom_edge_r2 = new ModelMapper(modelDataWrapper);
		front_middle_bottom_edge_r2.setPos(-32, 13, -80);
		right_wing.addChild(front_middle_bottom_edge_r2);
		setRotationAngle(front_middle_bottom_edge_r2, -0.2618F, 0.4712F, 0.0873F);
		front_middle_bottom_edge_r2.texOffs(524, 295).addBox(-181, -1, -14, 188, 1, 51, 0, false);

		front_middle_top_edge_r2 = new ModelMapper(modelDataWrapper);
		front_middle_top_edge_r2.setPos(-32, 7, -80);
		right_wing.addChild(front_middle_top_edge_r2);
		setRotationAngle(front_middle_top_edge_r2, 0.0873F, 0.4712F, 0.0873F);
		front_middle_top_edge_r2.texOffs(400, 360).addBox(-183, 0, -14, 191, 1, 14, 0, false);

		back_top_wing = new ModelMapper(modelDataWrapper);
		back_top_wing.setPos(0, 0, 0);
		exterior.addChild(back_top_wing);
		back_top_wing.texOffs(0, 556).addBox(-1, -128, 194, 2, 103, 90, -0.1F, false);

		back_r1 = new ModelMapper(modelDataWrapper);
		back_r1.setPos(0, -128, 285);
		back_top_wing.addChild(back_r1);
		setRotationAngle(back_r1, -0.2269F, 0, 0);
		back_r1.texOffs(28, 0).addBox(-1, 0, -1, 2, 106, 1, 0, false);

		front_bottom_r1 = new ModelMapper(modelDataWrapper);
		front_bottom_r1.setPos(0, -33, 162);
		back_top_wing.addChild(front_bottom_r1);
		setRotationAngle(front_bottom_r1, -1.1345F, 0, 0);
		front_bottom_r1.texOffs(0, 749).addBox(-1, -30, 0, 2, 30, 16, 0.1F, false);

		front_top_r1 = new ModelMapper(modelDataWrapper);
		front_top_r1.setPos(0, -128, 258);
		back_top_wing.addChild(front_top_r1);
		setRotationAngle(front_top_r1, -0.6981F, 0, 0);
		front_top_r1.texOffs(0, 0).addBox(-1, 0, 0, 2, 113, 12, 0, false);

		back_left_wing = new ModelMapper(modelDataWrapper);
		back_left_wing.setPos(0, 0, 0);
		exterior.addChild(back_left_wing);


		back_r2 = new ModelMapper(modelDataWrapper);
		back_r2.setPos(0, -15, 264);
		back_left_wing.addChild(back_r2);
		setRotationAngle(back_r2, 0, -0.2269F, -0.0873F);
		back_r2.texOffs(673, 165).addBox(11, -1, -25, 93, 2, 25, 0, false);

		side_r1 = new ModelMapper(modelDataWrapper);
		side_r1.setPos(0, -15, 0);
		back_left_wing.addChild(side_r1);
		setRotationAngle(side_r1, 0, 0, -0.0873F);
		side_r1.texOffs(511, 743).addBox(99, -1, 264, 2, 2, 24, 0.1F, false);

		front_r1 = new ModelMapper(modelDataWrapper);
		front_r1.setPos(0, -15, 204);
		back_left_wing.addChild(front_r1);
		setRotationAngle(front_r1, 0, -0.5411F, -0.0873F);
		front_r1.texOffs(374, 657).addBox(25, -1, 0, 94, 2, 25, -0.1F, false);

		back_right_wing = new ModelMapper(modelDataWrapper);
		back_right_wing.setPos(0, 0, 0);
		exterior.addChild(back_right_wing);


		back_r3 = new ModelMapper(modelDataWrapper);
		back_r3.setPos(0, -15, 264);
		back_right_wing.addChild(back_r3);
		setRotationAngle(back_r3, 0, 0.2269F, 0.0873F);
		back_r3.texOffs(612, 657).addBox(-104, -1, -25, 93, 2, 25, 0, false);

		side_r2 = new ModelMapper(modelDataWrapper);
		side_r2.setPos(0, -15, 0);
		back_right_wing.addChild(side_r2);
		setRotationAngle(side_r2, 0, 0, 0.0873F);
		side_r2.texOffs(10, 101).addBox(-101, -1, 264, 2, 2, 24, 0.1F, false);

		front_r2 = new ModelMapper(modelDataWrapper);
		front_r2.setPos(0, -15, 204);
		back_right_wing.addChild(front_r2);
		setRotationAngle(front_r2, 0, 0.5411F, 0.0873F);
		front_r2.texOffs(607, 604).addBox(-119, -1, 0, 94, 2, 25, -0.1F, false);

		bottom = new ModelMapper(modelDataWrapper);
		bottom.setPos(0, 0, 0);
		exterior.addChild(bottom);
		bottom.texOffs(122, 0).addBox(-24, 36, -107, 48, 0, 109, 0, false);
		bottom.texOffs(218, 341).addBox(-31, 13, -131, 62, 12, 0, 0, false);
		bottom.texOffs(0, 284).addBox(-24, 24, 51, 48, 6, 0, 0, false);
		bottom.texOffs(0, 226).addBox(32, 10, -107, 0, 18, 109, 0, false);
		bottom.texOffs(0, 208).addBox(-32, 10, -107, 0, 18, 109, 0, false);

		back_top_right_r1 = new ModelMapper(modelDataWrapper);
		back_top_right_r1.setPos(-32, 28, 2);
		bottom.addChild(back_top_right_r1);
		setRotationAngle(back_top_right_r1, 0, 0.1745F, 0);
		back_top_right_r1.texOffs(0, 78).addBox(0, -18, 0, 0, 18, 50, 0, false);

		back_top_left_r1 = new ModelMapper(modelDataWrapper);
		back_top_left_r1.setPos(32, 28, 2);
		bottom.addChild(back_top_left_r1);
		setRotationAngle(back_top_left_r1, 0, -0.1745F, 0);
		back_top_left_r1.texOffs(0, 206).addBox(0, -18, 0, 0, 18, 50, 0, false);

		back_side_right_r1 = new ModelMapper(modelDataWrapper);
		back_side_right_r1.setPos(-32, 28, 2);
		bottom.addChild(back_side_right_r1);
		setRotationAngle(back_side_right_r1, 0, 0.1745F, -0.5236F);
		back_side_right_r1.texOffs(159, 172).addBox(0, 0, 0, 0, 10, 50, 0, false);

		back_side_left_r1 = new ModelMapper(modelDataWrapper);
		back_side_left_r1.setPos(32, 28, 2);
		bottom.addChild(back_side_left_r1);
		setRotationAngle(back_side_left_r1, 0, -0.1745F, 0.5236F);
		back_side_left_r1.texOffs(0, 224).addBox(0, 0, 0, 0, 10, 50, 0, false);

		back_bottom_right_r1 = new ModelMapper(modelDataWrapper);
		back_bottom_right_r1.setPos(-24, 36, 2);
		bottom.addChild(back_bottom_right_r1);
		setRotationAngle(back_bottom_right_r1, 0.0873F, 0, 0.5236F);
		back_bottom_right_r1.texOffs(22, 80).addBox(-6, 0, 0, 6, 0, 30, 0, false);

		back_bottom_left_r1 = new ModelMapper(modelDataWrapper);
		back_bottom_left_r1.setPos(24, 36, 2);
		bottom.addChild(back_bottom_left_r1);
		setRotationAngle(back_bottom_left_r1, 0.0873F, 0, -0.5236F);
		back_bottom_left_r1.texOffs(103, 0).addBox(0, 0, 0, 6, 0, 30, 0, false);

		front_top_right_r1 = new ModelMapper(modelDataWrapper);
		front_top_right_r1.setPos(-32, 28, -107);
		bottom.addChild(front_top_right_r1);
		setRotationAngle(front_top_right_r1, 0, -0.0873F, 0);
		front_top_right_r1.texOffs(587, 632).addBox(0, -18, -25, 0, 18, 25, 0, false);

		front_top_left_r1 = new ModelMapper(modelDataWrapper);
		front_top_left_r1.setPos(32, 28, -107);
		bottom.addChild(front_top_left_r1);
		setRotationAngle(front_top_left_r1, 0, 0.0873F, 0);
		front_top_left_r1.texOffs(627, 700).addBox(0, -18, -25, 0, 18, 25, 0, false);

		front_side_right_r1 = new ModelMapper(modelDataWrapper);
		front_side_right_r1.setPos(-32, 28, -107);
		bottom.addChild(front_side_right_r1);
		setRotationAngle(front_side_right_r1, 0, -0.1745F, -0.5236F);
		front_side_right_r1.texOffs(673, 215).addBox(0, -4, -25, 0, 10, 25, 0, false);

		front_side_left_r1 = new ModelMapper(modelDataWrapper);
		front_side_left_r1.setPos(32, 28, -107);
		bottom.addChild(front_side_left_r1);
		setRotationAngle(front_side_left_r1, 0, 0.1745F, 0.5236F);
		front_side_left_r1.texOffs(614, 674).addBox(0, -4, -25, 0, 10, 25, 0, false);

		front_bottom_right_r1 = new ModelMapper(modelDataWrapper);
		front_bottom_right_r1.setPos(-24, 36, -107);
		bottom.addChild(front_bottom_right_r1);
		setRotationAngle(front_bottom_right_r1, -0.2618F, 0, 0.5236F);
		front_bottom_right_r1.texOffs(234, 203).addBox(-9, 0, -25, 9, 0, 25, 0, false);

		front_bottom_left_r1 = new ModelMapper(modelDataWrapper);
		front_bottom_left_r1.setPos(24, 36, -107);
		bottom.addChild(front_bottom_left_r1);
		setRotationAngle(front_bottom_left_r1, -0.2618F, 0, -0.5236F);
		front_bottom_left_r1.texOffs(262, 236).addBox(0, 0, -25, 9, 0, 25, 0, false);

		side_right_r2 = new ModelMapper(modelDataWrapper);
		side_right_r2.setPos(-32, 28, 0);
		bottom.addChild(side_right_r2);
		setRotationAngle(side_right_r2, 0, 0, -0.5236F);
		side_right_r2.texOffs(0, 244).addBox(0, 0, -107, 0, 6, 109, 0, false);

		side_left_r2 = new ModelMapper(modelDataWrapper);
		side_left_r2.setPos(32, 28, 0);
		bottom.addChild(side_left_r2);
		setRotationAngle(side_left_r2, 0, 0, 0.5236F);
		side_left_r2.texOffs(0, 250).addBox(0, 0, -107, 0, 6, 109, 0, false);

		bottom_right_r3 = new ModelMapper(modelDataWrapper);
		bottom_right_r3.setPos(-24, 36, 0);
		bottom.addChild(bottom_right_r3);
		setRotationAngle(bottom_right_r3, 0, 0, 0.5236F);
		bottom_right_r3.texOffs(0, 0).addBox(-6, 0, -107, 6, 0, 109, 0, false);

		bottom_left_r3 = new ModelMapper(modelDataWrapper);
		bottom_left_r3.setPos(24, 36, 0);
		bottom.addChild(bottom_left_r3);
		setRotationAngle(bottom_left_r3, 0, 0, -0.5236F);
		bottom_left_r3.texOffs(12, 0).addBox(0, 0, -107, 6, 0, 109, 0, false);

		back_side_r1 = new ModelMapper(modelDataWrapper);
		back_side_r1.setPos(0, 30, 51);
		bottom.addChild(back_side_r1);
		setRotationAngle(back_side_r1, 0.6981F, 0, 0);
		back_side_r1.texOffs(397, 375).addBox(-21, 0, -3, 42, 0, 3, 0, false);

		back_bottom_r1 = new ModelMapper(modelDataWrapper);
		back_bottom_r1.setPos(0, 36, 2);
		bottom.addChild(back_bottom_r1);
		setRotationAngle(back_bottom_r1, 0.0873F, 0, 0);
		back_bottom_r1.texOffs(112, 175).addBox(-24, 0, 0, 48, 0, 47, 0, false);

		front_side_r1 = new ModelMapper(modelDataWrapper);
		front_side_r1.setPos(31, 25, -131);
		bottom.addChild(front_side_r1);
		setRotationAngle(front_side_r1, 0.5236F, 0, 0);
		front_side_r1.texOffs(159, 232).addBox(-60, 0, 0, 58, 4, 0, 0, false);

		front_bottom_r2 = new ModelMapper(modelDataWrapper);
		front_bottom_r2.setPos(24, 36, -107);
		bottom.addChild(front_bottom_r2);
		setRotationAngle(front_bottom_r2, -0.3491F, 0, 0);
		front_bottom_r2.texOffs(194, 317).addBox(-51, 0, -24, 54, 0, 24, 0, false);

		left_engine = new ModelMapper(modelDataWrapper);
		left_engine.setPos(0, 0, 0);
		exterior.addChild(left_engine);
		left_engine.texOffs(315, 712).addBox(77, 17, -99, 30, 30, 35, 0, false);
		left_engine.texOffs(276, 604).addBox(89, 6, -106, 6, 22, 86, 0, false);
		left_engine.texOffs(618, 823).addBox(87, 47, -99, 10, 3, 16, 0, false);
		left_engine.texOffs(861, 868).addBox(107, 27, -99, 3, 10, 16, 0, false);
		left_engine.texOffs(248, 874).addBox(74, 27, -99, 3, 10, 16, 0, false);
		left_engine.texOffs(682, 823).addBox(87, 14, -99, 10, 3, 16, 0, false);

		tail_right_roof_r1 = new ModelMapper(modelDataWrapper);
		tail_right_roof_r1.setPos(89, 22, -64);
		left_engine.addChild(tail_right_roof_r1);
		setRotationAngle(tail_right_roof_r1, -0.2094F, 0, -0.5236F);
		tail_right_roof_r1.texOffs(636, 791).addBox(-6, 0, 0, 6, 3, 29, 0, false);

		tail_left_roof_r1 = new ModelMapper(modelDataWrapper);
		tail_left_roof_r1.setPos(95, 22, -64);
		left_engine.addChild(tail_left_roof_r1);
		setRotationAngle(tail_left_roof_r1, -0.2094F, 0, 0.5236F);
		tail_left_roof_r1.texOffs(0, 808).addBox(0, 0, 0, 6, 3, 29, 0, false);

		tail_roof_r1 = new ModelMapper(modelDataWrapper);
		tail_roof_r1.setPos(0, 22, -64);
		left_engine.addChild(tail_roof_r1);
		setRotationAngle(tail_roof_r1, -0.2094F, 0, 0);
		tail_roof_r1.texOffs(95, 806).addBox(89, 0, 0, 6, 3, 29, 0, false);

		tail_right_wall_bottom_r1 = new ModelMapper(modelDataWrapper);
		tail_right_wall_bottom_r1.setPos(82, 35, -64);
		left_engine.addChild(tail_right_wall_bottom_r1);
		setRotationAngle(tail_right_wall_bottom_r1, 0, 0.2094F, -0.5236F);
		tail_right_wall_bottom_r1.texOffs(711, 823).addBox(0, 0, 0, 3, 6, 29, 0, false);

		tail_right_wall_top_r1 = new ModelMapper(modelDataWrapper);
		tail_right_wall_top_r1.setPos(82, 29, -64);
		left_engine.addChild(tail_right_wall_top_r1);
		setRotationAngle(tail_right_wall_top_r1, 0, 0.2094F, 0.5236F);
		tail_right_wall_top_r1.texOffs(647, 823).addBox(0, -6, 0, 3, 6, 29, 0, false);

		tail_right_wall_r1 = new ModelMapper(modelDataWrapper);
		tail_right_wall_r1.setPos(82, 0, -64);
		left_engine.addChild(tail_right_wall_r1);
		setRotationAngle(tail_right_wall_r1, 0, 0.2094F, 0);
		tail_right_wall_r1.texOffs(395, 822).addBox(0, 29, 0, 3, 6, 29, 0, false);

		tail_left_wall_bottom_r1 = new ModelMapper(modelDataWrapper);
		tail_left_wall_bottom_r1.setPos(102, 35, -64);
		left_engine.addChild(tail_left_wall_bottom_r1);
		setRotationAngle(tail_left_wall_bottom_r1, 0, -0.2094F, 0.5236F);
		tail_left_wall_bottom_r1.texOffs(583, 820).addBox(-3, 0, 0, 3, 6, 29, 0, false);

		tail_left_wall_top_r1 = new ModelMapper(modelDataWrapper);
		tail_left_wall_top_r1.setPos(102, 29, -64);
		left_engine.addChild(tail_left_wall_top_r1);
		setRotationAngle(tail_left_wall_top_r1, 0, -0.2094F, -0.5236F);
		tail_left_wall_top_r1.texOffs(818, 487).addBox(-3, -6, 0, 3, 6, 29, 0, false);

		tail_left_wall_r1 = new ModelMapper(modelDataWrapper);
		tail_left_wall_r1.setPos(102, 0, -64);
		left_engine.addChild(tail_left_wall_r1);
		setRotationAngle(tail_left_wall_r1, 0, -0.2094F, 0);
		tail_left_wall_r1.texOffs(817, 810).addBox(-3, 29, 0, 3, 6, 29, 0, false);

		tail_right_floor_r1 = new ModelMapper(modelDataWrapper);
		tail_right_floor_r1.setPos(89, 42, -64);
		left_engine.addChild(tail_right_floor_r1);
		setRotationAngle(tail_right_floor_r1, 0.2094F, 0, 0.5236F);
		tail_right_floor_r1.texOffs(794, 79).addBox(-6, -3, 0, 6, 3, 29, 0, false);

		tail_left_floor_r1 = new ModelMapper(modelDataWrapper);
		tail_left_floor_r1.setPos(95, 42, -64);
		left_engine.addChild(tail_left_floor_r1);
		setRotationAngle(tail_left_floor_r1, 0.2094F, 0, -0.5236F);
		tail_left_floor_r1.texOffs(776, 791).addBox(0, -3, 0, 6, 3, 29, 0, false);

		tail_floor_r1 = new ModelMapper(modelDataWrapper);
		tail_floor_r1.setPos(0, 42, -64);
		left_engine.addChild(tail_floor_r1);
		setRotationAngle(tail_floor_r1, 0.2094F, 0, 0);
		tail_floor_r1.texOffs(706, 791).addBox(89, -3, 0, 6, 3, 29, 0, false);

		back_right_roof_r1 = new ModelMapper(modelDataWrapper);
		back_right_roof_r1.setPos(87, 14, -83);
		left_engine.addChild(back_right_roof_r1);
		setRotationAngle(back_right_roof_r1, -0.1745F, 0, -0.5236F);
		back_right_roof_r1.texOffs(835, 79).addBox(-10, 0, 0, 10, 3, 20, 0, false);

		back_left_roof_r1 = new ModelMapper(modelDataWrapper);
		back_left_roof_r1.setPos(97, 14, -83);
		left_engine.addChild(back_left_roof_r1);
		setRotationAngle(back_left_roof_r1, -0.1745F, 0, 0.5236F);
		back_left_roof_r1.texOffs(786, 845).addBox(0, 0, 0, 10, 3, 20, 0, false);

		back_roof_r1 = new ModelMapper(modelDataWrapper);
		back_roof_r1.setPos(0, 14, -83);
		left_engine.addChild(back_roof_r1);
		setRotationAngle(back_roof_r1, -0.1745F, 0, 0);
		back_roof_r1.texOffs(125, 844).addBox(87, 0, 0, 10, 3, 20, 0, false);

		back_right_wall_bottom_r1 = new ModelMapper(modelDataWrapper);
		back_right_wall_bottom_r1.setPos(74, 37, -83);
		left_engine.addChild(back_right_wall_bottom_r1);
		setRotationAngle(back_right_wall_bottom_r1, 0, 0.1745F, -0.5236F);
		back_right_wall_bottom_r1.texOffs(861, 790).addBox(0, 0, 0, 3, 10, 20, 0, false);

		back_right_wall_top_r1 = new ModelMapper(modelDataWrapper);
		back_right_wall_top_r1.setPos(74, 27, -83);
		left_engine.addChild(back_right_wall_top_r1);
		setRotationAngle(back_right_wall_top_r1, 0, 0.1745F, 0.5236F);
		back_right_wall_top_r1.texOffs(80, 861).addBox(0, -10, 0, 3, 10, 20, 0, false);

		back_right_wall_r1 = new ModelMapper(modelDataWrapper);
		back_right_wall_r1.setPos(74, 0, -83);
		left_engine.addChild(back_right_wall_r1);
		setRotationAngle(back_right_wall_r1, 0, 0.1745F, 0);
		back_right_wall_r1.texOffs(860, 583).addBox(0, 27, 0, 3, 10, 20, 0, false);

		back_left_wall_bottom_r1 = new ModelMapper(modelDataWrapper);
		back_left_wall_bottom_r1.setPos(110, 37, -83);
		left_engine.addChild(back_left_wall_bottom_r1);
		setRotationAngle(back_left_wall_bottom_r1, 0, -0.1745F, 0.5236F);
		back_left_wall_bottom_r1.texOffs(331, 860).addBox(-3, 0, 0, 3, 10, 20, 0, false);

		back_left_wall_top_r1 = new ModelMapper(modelDataWrapper);
		back_left_wall_top_r1.setPos(110, 27, -83);
		left_engine.addChild(back_left_wall_top_r1);
		setRotationAngle(back_left_wall_top_r1, 0, -0.1745F, -0.5236F);
		back_left_wall_top_r1.texOffs(859, 719).addBox(-3, -10, 0, 3, 10, 20, 0, false);

		back_left_wall_r1 = new ModelMapper(modelDataWrapper);
		back_left_wall_r1.setPos(110, 0, -83);
		left_engine.addChild(back_left_wall_r1);
		setRotationAngle(back_left_wall_r1, 0, -0.1745F, 0);
		back_left_wall_r1.texOffs(723, 858).addBox(-3, 27, 0, 3, 10, 20, 0, false);

		back_right_floor_r1 = new ModelMapper(modelDataWrapper);
		back_right_floor_r1.setPos(87, 50, -83);
		left_engine.addChild(back_right_floor_r1);
		setRotationAngle(back_right_floor_r1, 0.1745F, 0, 0.5236F);
		back_right_floor_r1.texOffs(0, 840).addBox(-10, -3, 0, 10, 3, 20, 0, false);

		back_left_floor_r1 = new ModelMapper(modelDataWrapper);
		back_left_floor_r1.setPos(97, 50, -83);
		left_engine.addChild(back_left_floor_r1);
		setRotationAngle(back_left_floor_r1, 0.1745F, 0, -0.5236F);
		back_left_floor_r1.texOffs(85, 838).addBox(0, -3, 0, 10, 3, 20, 0, false);

		back_floor_r1 = new ModelMapper(modelDataWrapper);
		back_floor_r1.setPos(0, 50, -83);
		left_engine.addChild(back_floor_r1);
		setRotationAngle(back_floor_r1, 0.1745F, 0, 0);
		back_floor_r1.texOffs(340, 837).addBox(87, -3, 0, 10, 3, 20, 0, false);

		front_right_roof_r1 = new ModelMapper(modelDataWrapper);
		front_right_roof_r1.setPos(87, 14, -99);
		left_engine.addChild(front_right_roof_r1);
		setRotationAngle(front_right_roof_r1, 0.1047F, 0, -0.5236F);
		front_right_roof_r1.texOffs(499, 851).addBox(-10, 0, -20, 10, 3, 20, 0, false);

		front_left_roof_r1 = new ModelMapper(modelDataWrapper);
		front_left_roof_r1.setPos(97, 14, -99);
		left_engine.addChild(front_left_roof_r1);
		setRotationAngle(front_left_roof_r1, 0.1047F, 0, 0.5236F);
		front_left_roof_r1.texOffs(439, 851).addBox(0, 0, -20, 10, 3, 20, 0, false);

		front_roof_r1 = new ModelMapper(modelDataWrapper);
		front_roof_r1.setPos(0, 14, -99);
		left_engine.addChild(front_roof_r1);
		setRotationAngle(front_roof_r1, 0.1047F, 0, 0);
		front_roof_r1.texOffs(225, 851).addBox(87, 0, -20, 10, 3, 20, 0, false);

		front_right_wall_bottom_r1 = new ModelMapper(modelDataWrapper);
		front_right_wall_bottom_r1.setPos(74, 37, -99);
		left_engine.addChild(front_right_wall_bottom_r1);
		setRotationAngle(front_right_wall_bottom_r1, 0, -0.1047F, -0.5236F);
		front_right_wall_bottom_r1.texOffs(867, 365).addBox(0, 0, -20, 3, 10, 20, 0, false);

		front_right_wall_top_r1 = new ModelMapper(modelDataWrapper);
		front_right_wall_top_r1.setPos(74, 27, -99);
		left_engine.addChild(front_right_wall_top_r1);
		setRotationAngle(front_right_wall_top_r1, 0, -0.1047F, 0.5236F);
		front_right_wall_top_r1.texOffs(126, 867).addBox(0, -10, -20, 3, 10, 20, 0, false);

		front_right_wall_r1 = new ModelMapper(modelDataWrapper);
		front_right_wall_r1.setPos(74, 0, -99);
		left_engine.addChild(front_right_wall_r1);
		setRotationAngle(front_right_wall_r1, 0, -0.1047F, 0);
		front_right_wall_r1.texOffs(873, 208).addBox(0, 27, -20, 3, 10, 20, 0, false);

		front_left_wall_bottom_r1 = new ModelMapper(modelDataWrapper);
		front_left_wall_bottom_r1.setPos(110, 37, -99);
		left_engine.addChild(front_left_wall_bottom_r1);
		setRotationAngle(front_left_wall_bottom_r1, 0, 0.1047F, 0.5236F);
		front_left_wall_bottom_r1.texOffs(865, 114).addBox(-3, 0, -20, 3, 10, 20, 0, false);

		front_left_wall_top_r1 = new ModelMapper(modelDataWrapper);
		front_left_wall_top_r1.setPos(110, 27, -99);
		left_engine.addChild(front_left_wall_top_r1);
		setRotationAngle(front_left_wall_top_r1, 0, 0.1047F, -0.5236F);
		front_left_wall_top_r1.texOffs(0, 863).addBox(-3, -10, -20, 3, 10, 20, 0, false);

		front_left_wall_r1 = new ModelMapper(modelDataWrapper);
		front_left_wall_r1.setPos(110, 0, -99);
		left_engine.addChild(front_left_wall_r1);
		setRotationAngle(front_left_wall_r1, 0, 0.1047F, 0);
		front_left_wall_r1.texOffs(862, 502).addBox(-3, 27, -20, 3, 10, 20, 0, false);

		front_right_floor_r1 = new ModelMapper(modelDataWrapper);
		front_right_floor_r1.setPos(87, 50, -99);
		left_engine.addChild(front_right_floor_r1);
		setRotationAngle(front_right_floor_r1, -0.1047F, 0, 0.5236F);
		front_right_floor_r1.texOffs(165, 851).addBox(-10, -3, -20, 10, 3, 20, 0, false);

		front_left_floor_r1 = new ModelMapper(modelDataWrapper);
		front_left_floor_r1.setPos(97, 50, -99);
		left_engine.addChild(front_left_floor_r1);
		setRotationAngle(front_left_floor_r1, -0.1047F, 0, -0.5236F);
		front_left_floor_r1.texOffs(846, 845).addBox(0, -3, -20, 10, 3, 20, 0, false);

		front_floor_r1 = new ModelMapper(modelDataWrapper);
		front_floor_r1.setPos(0, 50, -99);
		left_engine.addChild(front_floor_r1);
		setRotationAngle(front_floor_r1, -0.1047F, 0, 0);
		front_floor_r1.texOffs(40, 846).addBox(87, -3, -20, 10, 3, 20, 0, false);

		right_roof_r2 = new ModelMapper(modelDataWrapper);
		right_roof_r2.setPos(87, 14, 0);
		left_engine.addChild(right_roof_r2);
		setRotationAngle(right_roof_r2, 0, 0, -0.5236F);
		right_roof_r2.texOffs(852, 820).addBox(-10, 0, -99, 10, 3, 16, 0, false);

		left_roof_r2 = new ModelMapper(modelDataWrapper);
		left_roof_r2.setPos(97, 14, 0);
		left_engine.addChild(left_roof_r2);
		setRotationAngle(left_roof_r2, 0, 0, 0.5236F);
		left_roof_r2.texOffs(430, 819).addBox(0, 0, -99, 10, 3, 16, 0, false);

		right_wall_bottom_r1 = new ModelMapper(modelDataWrapper);
		right_wall_bottom_r1.setPos(74, 37, 0);
		left_engine.addChild(right_wall_bottom_r1);
		setRotationAngle(right_wall_bottom_r1, 0, 0, -0.5236F);
		right_wall_bottom_r1.texOffs(172, 874).addBox(0, 0, -99, 3, 10, 16, 0, false);

		right_wall_top_r3 = new ModelMapper(modelDataWrapper);
		right_wall_top_r3.setPos(74, 27, 0);
		left_engine.addChild(right_wall_top_r3);
		setRotationAngle(right_wall_top_r3, 0, 0, 0.5236F);
		right_wall_top_r3.texOffs(210, 874).addBox(0, -10, -99, 3, 10, 16, 0, false);

		left_wall_bottom_r1 = new ModelMapper(modelDataWrapper);
		left_wall_bottom_r1.setPos(110, 37, 0);
		left_engine.addChild(left_wall_bottom_r1);
		setRotationAngle(left_wall_bottom_r1, 0, 0, 0.5236F);
		left_wall_bottom_r1.texOffs(823, 868).addBox(-3, 0, -99, 3, 10, 16, 0, false);

		left_wall_top_r3 = new ModelMapper(modelDataWrapper);
		left_wall_top_r3.setPos(110, 27, 0);
		left_engine.addChild(left_wall_top_r3);
		setRotationAngle(left_wall_top_r3, 0, 0, -0.5236F);
		left_wall_top_r3.texOffs(410, 871).addBox(-3, -10, -99, 3, 10, 16, 0, false);

		right_floor_r2 = new ModelMapper(modelDataWrapper);
		right_floor_r2.setPos(87, 50, 0);
		left_engine.addChild(right_floor_r2);
		setRotationAngle(right_floor_r2, 0, 0, 0.5236F);
		right_floor_r2.texOffs(842, 440).addBox(-10, -3, -99, 10, 3, 16, 0, false);

		left_floor_r2 = new ModelMapper(modelDataWrapper);
		left_floor_r2.setPos(97, 50, 0);
		left_engine.addChild(left_floor_r2);
		setRotationAngle(left_floor_r2, 0, 0, -0.5236F);
		left_floor_r2.texOffs(554, 816).addBox(0, -3, -99, 10, 3, 16, 0, false);

		support_back_r1 = new ModelMapper(modelDataWrapper);
		support_back_r1.setPos(0, 28, -20);
		left_engine.addChild(support_back_r1);
		setRotationAngle(support_back_r1, 0.8203F, 0, 0);
		support_back_r1.texOffs(196, 777).addBox(89, -13, 0, 6, 13, 38, 0.1F, false);

		support_top_r1 = new ModelMapper(modelDataWrapper);
		support_top_r1.setPos(0, 11, -106);
		left_engine.addChild(support_top_r1);
		setRotationAngle(support_top_r1, 0.1222F, 0, 0);
		support_top_r1.texOffs(0, 749).addBox(89, 0, 0, 6, 3, 56, 0.1F, false);

		right_engine = new ModelMapper(modelDataWrapper);
		right_engine.setPos(0, 0, 0);
		exterior.addChild(right_engine);
		right_engine.texOffs(185, 712).addBox(-107, 17, -99, 30, 30, 35, 0, false);
		right_engine.texOffs(575, 165).addBox(-95, 6, -106, 6, 22, 86, 0, false);
		right_engine.texOffs(490, 816).addBox(-97, 47, -99, 10, 3, 16, 0, false);
		right_engine.texOffs(785, 868).addBox(-110, 27, -99, 3, 10, 16, 0, false);
		right_engine.texOffs(76, 808).addBox(-77, 27, -99, 3, 10, 16, 0, false);
		right_engine.texOffs(525, 784).addBox(-97, 14, -99, 10, 3, 16, 0, false);

		tail_right_roof_r2 = new ModelMapper(modelDataWrapper);
		tail_right_roof_r2.setPos(-89, 22, -64);
		right_engine.addChild(tail_right_roof_r2);
		setRotationAngle(tail_right_roof_r2, -0.2094F, 0, 0.5236F);
		tail_right_roof_r2.texOffs(280, 712).addBox(0, 0, 0, 6, 3, 29, 0, false);

		tail_left_roof_r2 = new ModelMapper(modelDataWrapper);
		tail_left_roof_r2.setPos(-95, 22, -64);
		right_engine.addChild(tail_left_roof_r2);
		setRotationAngle(tail_left_roof_r2, -0.2094F, 0, -0.5236F);
		tail_left_roof_r2.texOffs(484, 784).addBox(-6, 0, 0, 6, 3, 29, 0, false);

		tail_roof_r2 = new ModelMapper(modelDataWrapper);
		tail_roof_r2.setPos(0, 22, -64);
		right_engine.addChild(tail_roof_r2);
		setRotationAngle(tail_roof_r2, -0.2094F, 0, 0);
		tail_roof_r2.texOffs(554, 784).addBox(-95, 0, 0, 6, 3, 29, 0, false);

		tail_right_wall_bottom_r2 = new ModelMapper(modelDataWrapper);
		tail_right_wall_bottom_r2.setPos(-82, 35, -64);
		right_engine.addChild(tail_right_wall_bottom_r2);
		setRotationAngle(tail_right_wall_bottom_r2, 0, -0.2094F, 0.5236F);
		tail_right_wall_bottom_r2.texOffs(136, 809).addBox(-3, 0, 0, 3, 6, 29, 0, false);

		tail_right_wall_top_r2 = new ModelMapper(modelDataWrapper);
		tail_right_wall_top_r2.setPos(-82, 29, -64);
		right_engine.addChild(tail_right_wall_top_r2);
		setRotationAngle(tail_right_wall_top_r2, 0, -0.2094F, -0.5236F);
		tail_right_wall_top_r2.texOffs(41, 811).addBox(-3, -6, 0, 3, 6, 29, 0, false);

		tail_right_wall_r2 = new ModelMapper(modelDataWrapper);
		tail_right_wall_r2.setPos(-82, 0, -64);
		right_engine.addChild(tail_right_wall_r2);
		setRotationAngle(tail_right_wall_r2, 0, -0.2094F, 0);
		tail_right_wall_r2.texOffs(455, 816).addBox(-3, 29, 0, 3, 6, 29, 0, false);

		tail_left_wall_bottom_r2 = new ModelMapper(modelDataWrapper);
		tail_left_wall_bottom_r2.setPos(-102, 35, -64);
		right_engine.addChild(tail_left_wall_bottom_r2);
		setRotationAngle(tail_left_wall_bottom_r2, 0, 0.2094F, -0.5236F);
		tail_left_wall_bottom_r2.texOffs(519, 816).addBox(0, 0, 0, 3, 6, 29, 0, false);

		tail_left_wall_top_r2 = new ModelMapper(modelDataWrapper);
		tail_left_wall_top_r2.setPos(-102, 29, -64);
		right_engine.addChild(tail_left_wall_top_r2);
		setRotationAngle(tail_left_wall_top_r2, 0, 0.2094F, 0.5236F);
		tail_left_wall_top_r2.texOffs(817, 684).addBox(0, -6, 0, 3, 6, 29, 0, false);

		tail_left_wall_r2 = new ModelMapper(modelDataWrapper);
		tail_left_wall_r2.setPos(-102, 0, -64);
		right_engine.addChild(tail_left_wall_r2);
		setRotationAngle(tail_left_wall_r2, 0, 0.2094F, 0);
		tail_left_wall_r2.texOffs(817, 775).addBox(0, 29, 0, 3, 6, 29, 0, false);

		tail_right_floor_r2 = new ModelMapper(modelDataWrapper);
		tail_right_floor_r2.setPos(-89, 42, -64);
		right_engine.addChild(tail_right_floor_r2);
		setRotationAngle(tail_right_floor_r2, 0.2094F, 0, -0.5236F);
		tail_right_floor_r2.texOffs(786, 385).addBox(0, -3, 0, 6, 3, 29, 0, false);

		tail_left_floor_r2 = new ModelMapper(modelDataWrapper);
		tail_left_floor_r2.setPos(-95, 42, -64);
		right_engine.addChild(tail_left_floor_r2);
		setRotationAngle(tail_left_floor_r2, 0.2094F, 0, 0.5236F);
		tail_left_floor_r2.texOffs(786, 417).addBox(-6, -3, 0, 6, 3, 29, 0, false);

		tail_floor_r2 = new ModelMapper(modelDataWrapper);
		tail_floor_r2.setPos(0, 42, -64);
		right_engine.addChild(tail_floor_r2);
		setRotationAngle(tail_floor_r2, 0.2094F, 0, 0);
		tail_floor_r2.texOffs(595, 788).addBox(-95, -3, 0, 6, 3, 29, 0, false);

		back_right_roof_r2 = new ModelMapper(modelDataWrapper);
		back_right_roof_r2.setPos(-87, 14, -83);
		right_engine.addChild(back_right_roof_r2);
		setRotationAngle(back_right_roof_r2, -0.1745F, 0, 0.5236F);
		back_right_roof_r2.texOffs(818, 522).addBox(0, 0, 0, 10, 3, 20, 0, false);

		back_left_roof_r2 = new ModelMapper(modelDataWrapper);
		back_left_roof_r2.setPos(-97, 14, -83);
		right_engine.addChild(back_left_roof_r2);
		setRotationAngle(back_left_roof_r2, -0.1745F, 0, -0.5236F);
		back_left_roof_r2.texOffs(820, 603).addBox(-10, 0, 0, 10, 3, 20, 0, false);

		back_roof_r2 = new ModelMapper(modelDataWrapper);
		back_roof_r2.setPos(0, 14, -83);
		right_engine.addChild(back_roof_r2);
		setRotationAngle(back_roof_r2, -0.1745F, 0, 0);
		back_roof_r2.texOffs(823, 657).addBox(-97, 0, 0, 10, 3, 20, 0, false);

		back_right_wall_bottom_r2 = new ModelMapper(modelDataWrapper);
		back_right_wall_bottom_r2.setPos(-74, 37, -83);
		right_engine.addChild(back_right_wall_bottom_r2);
		setRotationAngle(back_right_wall_bottom_r2, 0, -0.1745F, 0.5236F);
		back_right_wall_bottom_r2.texOffs(760, 487).addBox(-3, 0, 0, 3, 10, 20, 0, false);

		back_right_wall_top_r2 = new ModelMapper(modelDataWrapper);
		back_right_wall_top_r2.setPos(-74, 27, -83);
		right_engine.addChild(back_right_wall_top_r2);
		setRotationAngle(back_right_wall_top_r2, 0, -0.1745F, -0.5236F);
		back_right_wall_top_r2.texOffs(760, 517).addBox(-3, -10, 0, 3, 10, 20, 0, false);

		back_right_wall_r2 = new ModelMapper(modelDataWrapper);
		back_right_wall_r2.setPos(-74, 0, -83);
		right_engine.addChild(back_right_wall_r2);
		setRotationAngle(back_right_wall_r2, 0, -0.1745F, 0);
		back_right_wall_r2.texOffs(755, 848).addBox(-3, 27, 0, 3, 10, 20, 0, false);

		back_left_wall_bottom_r2 = new ModelMapper(modelDataWrapper);
		back_left_wall_bottom_r2.setPos(-110, 37, -83);
		right_engine.addChild(back_left_wall_bottom_r2);
		setRotationAngle(back_left_wall_bottom_r2, 0, 0.1745F, -0.5236F);
		back_left_wall_bottom_r2.texOffs(285, 852).addBox(0, 0, 0, 3, 10, 20, 0, false);

		back_left_wall_top_r2 = new ModelMapper(modelDataWrapper);
		back_left_wall_top_r2.setPos(-110, 27, -83);
		right_engine.addChild(back_left_wall_top_r2);
		setRotationAngle(back_left_wall_top_r2, 0, 0.1745F, 0.5236F);
		back_left_wall_top_r2.texOffs(852, 680).addBox(0, -10, 0, 3, 10, 20, 0, false);

		back_left_wall_r2 = new ModelMapper(modelDataWrapper);
		back_left_wall_r2.setPos(-110, 0, -83);
		right_engine.addChild(back_left_wall_r2);
		setRotationAngle(back_left_wall_r2, 0, 0.1745F, 0);
		back_left_wall_r2.texOffs(852, 754).addBox(0, 27, 0, 3, 10, 20, 0, false);

		back_right_floor_r2 = new ModelMapper(modelDataWrapper);
		back_right_floor_r2.setPos(-87, 50, -83);
		right_engine.addChild(back_right_floor_r2);
		setRotationAngle(back_right_floor_r2, 0.1745F, 0, -0.5236F);
		back_right_floor_r2.texOffs(746, 823).addBox(0, -3, 0, 10, 3, 20, 0, false);

		back_left_floor_r2 = new ModelMapper(modelDataWrapper);
		back_left_floor_r2.setPos(-97, 50, -83);
		right_engine.addChild(back_left_floor_r2);
		setRotationAngle(back_left_floor_r2, 0.1745F, 0, 0.5236F);
		back_left_floor_r2.texOffs(825, 111).addBox(-10, -3, 0, 10, 3, 20, 0, false);

		back_floor_r2 = new ModelMapper(modelDataWrapper);
		back_floor_r2.setPos(0, 50, -83);
		right_engine.addChild(back_floor_r2);
		setRotationAngle(back_floor_r2, 0.1745F, 0, 0);
		back_floor_r2.texOffs(825, 134).addBox(-97, -3, 0, 10, 3, 20, 0, false);

		front_right_roof_r2 = new ModelMapper(modelDataWrapper);
		front_right_roof_r2.setPos(-87, 14, -99);
		right_engine.addChild(front_right_roof_r2);
		setRotationAngle(front_right_roof_r2, 0.1047F, 0, 0.5236F);
		front_right_roof_r2.texOffs(827, 385).addBox(0, 0, -20, 10, 3, 20, 0, false);

		front_left_roof_r2 = new ModelMapper(modelDataWrapper);
		front_left_roof_r2.setPos(-97, 14, -99);
		right_engine.addChild(front_left_roof_r2);
		setRotationAngle(front_left_roof_r2, 0.1047F, 0, -0.5236F);
		front_left_roof_r2.texOffs(827, 417).addBox(-10, 0, -20, 10, 3, 20, 0, false);

		front_roof_r2 = new ModelMapper(modelDataWrapper);
		front_roof_r2.setPos(0, 14, -99);
		right_engine.addChild(front_roof_r2);
		setRotationAngle(front_roof_r2, 0.1047F, 0, 0);
		front_roof_r2.texOffs(180, 828).addBox(-97, 0, -20, 10, 3, 20, 0, false);

		front_right_wall_bottom_r2 = new ModelMapper(modelDataWrapper);
		front_right_wall_bottom_r2.setPos(-74, 37, -99);
		right_engine.addChild(front_right_wall_bottom_r2);
		setRotationAngle(front_right_wall_bottom_r2, 0, 0.1047F, 0.5236F);
		front_right_wall_bottom_r2.texOffs(539, 854).addBox(-3, 0, -20, 3, 10, 20, 0, false);

		front_right_wall_top_r2 = new ModelMapper(modelDataWrapper);
		front_right_wall_top_r2.setPos(-74, 27, -99);
		right_engine.addChild(front_right_wall_top_r2);
		setRotationAngle(front_right_wall_top_r2, 0, 0.1047F, -0.5236F);
		front_right_wall_top_r2.texOffs(585, 855).addBox(-3, -10, -20, 3, 10, 20, 0, false);

		front_right_wall_r2 = new ModelMapper(modelDataWrapper);
		front_right_wall_r2.setPos(-74, 0, -99);
		right_engine.addChild(front_right_wall_r2);
		setRotationAngle(front_right_wall_r2, 0, 0.1047F, 0);
		front_right_wall_r2.texOffs(856, 626).addBox(-3, 27, -20, 3, 10, 20, 0, false);

		front_left_wall_bottom_r2 = new ModelMapper(modelDataWrapper);
		front_left_wall_bottom_r2.setPos(-110, 37, -99);
		right_engine.addChild(front_left_wall_bottom_r2);
		setRotationAngle(front_left_wall_bottom_r2, 0, -0.1047F, -0.5236F);
		front_left_wall_bottom_r2.texOffs(380, 857).addBox(0, 0, -20, 3, 10, 20, 0, false);

		front_left_wall_top_r2 = new ModelMapper(modelDataWrapper);
		front_left_wall_top_r2.setPos(-110, 27, -99);
		right_engine.addChild(front_left_wall_top_r2);
		setRotationAngle(front_left_wall_top_r2, 0, -0.1047F, 0.5236F);
		front_left_wall_top_r2.texOffs(631, 858).addBox(0, -10, -20, 3, 10, 20, 0, false);

		front_left_wall_r2 = new ModelMapper(modelDataWrapper);
		front_left_wall_r2.setPos(-110, 0, -99);
		right_engine.addChild(front_left_wall_r2);
		setRotationAngle(front_left_wall_r2, 0, -0.1047F, 0);
		front_left_wall_r2.texOffs(677, 858).addBox(0, 27, -20, 3, 10, 20, 0, false);

		front_right_floor_r2 = new ModelMapper(modelDataWrapper);
		front_right_floor_r2.setPos(-87, 50, -99);
		right_engine.addChild(front_right_floor_r2);
		setRotationAngle(front_right_floor_r2, -0.1047F, 0, -0.5236F);
		front_right_floor_r2.texOffs(240, 828).addBox(0, -3, -20, 10, 3, 20, 0, false);

		front_left_floor_r2 = new ModelMapper(modelDataWrapper);
		front_left_floor_r2.setPos(-97, 50, -99);
		right_engine.addChild(front_left_floor_r2);
		setRotationAngle(front_left_floor_r2, -0.1047F, 0, 0.5236F);
		front_left_floor_r2.texOffs(300, 829).addBox(-10, -3, -20, 10, 3, 20, 0, false);

		front_floor_r2 = new ModelMapper(modelDataWrapper);
		front_floor_r2.setPos(0, 50, -99);
		right_engine.addChild(front_floor_r2);
		setRotationAngle(front_floor_r2, -0.1047F, 0, 0);
		front_floor_r2.texOffs(833, 192).addBox(-97, -3, -20, 10, 3, 20, 0, false);

		right_roof_r3 = new ModelMapper(modelDataWrapper);
		right_roof_r3.setPos(-87, 14, 0);
		right_engine.addChild(right_roof_r3);
		setRotationAngle(right_roof_r3, 0, 0, 0.5236F);
		right_roof_r3.texOffs(445, 750).addBox(0, 0, -99, 10, 3, 16, 0, false);

		left_roof_r3 = new ModelMapper(modelDataWrapper);
		left_roof_r3.setPos(-97, 14, 0);
		right_engine.addChild(left_roof_r3);
		setRotationAngle(left_roof_r3, 0, 0, -0.5236F);
		left_roof_r3.texOffs(699, 754).addBox(-10, 0, -99, 10, 3, 16, 0, false);

		right_wall_bottom_r2 = new ModelMapper(modelDataWrapper);
		right_wall_bottom_r2.setPos(-74, 37, 0);
		right_engine.addChild(right_wall_bottom_r2);
		setRotationAngle(right_wall_bottom_r2, 0, 0, 0.5236F);
		right_wall_bottom_r2.texOffs(136, 771).addBox(-3, 0, -99, 3, 10, 16, 0, false);

		right_wall_top_r4 = new ModelMapper(modelDataWrapper);
		right_wall_top_r4.setPos(-74, 27, 0);
		right_engine.addChild(right_wall_top_r4);
		setRotationAngle(right_wall_top_r4, 0, 0, -0.5236F);
		right_wall_top_r4.texOffs(246, 777).addBox(-3, -10, -99, 3, 10, 16, 0, false);

		left_wall_bottom_r2 = new ModelMapper(modelDataWrapper);
		left_wall_bottom_r2.setPos(-110, 37, 0);
		right_engine.addChild(left_wall_bottom_r2);
		setRotationAngle(left_wall_bottom_r2, 0, 0, -0.5236F);
		left_wall_bottom_r2.texOffs(867, 408).addBox(0, 0, -99, 3, 10, 16, 0, false);

		left_wall_top_r4 = new ModelMapper(modelDataWrapper);
		left_wall_top_r4.setPos(-110, 27, 0);
		right_engine.addChild(left_wall_top_r4);
		setRotationAngle(left_wall_top_r4, 0, 0, 0.5236F);
		left_wall_top_r4.texOffs(868, 532).addBox(0, -10, -99, 3, 10, 16, 0, false);

		right_floor_r3 = new ModelMapper(modelDataWrapper);
		right_floor_r3.setPos(-87, 50, 0);
		right_engine.addChild(right_floor_r3);
		setRotationAngle(right_floor_r3, 0, 0, -0.5236F);
		right_floor_r3.texOffs(677, 791).addBox(0, -3, -99, 10, 3, 16, 0, false);

		left_floor_r3 = new ModelMapper(modelDataWrapper);
		left_floor_r3.setPos(-97, 50, 0);
		right_engine.addChild(left_floor_r3);
		setRotationAngle(left_floor_r3, 0, 0, 0.5236F);
		left_floor_r3.texOffs(747, 791).addBox(-10, -3, -99, 10, 3, 16, 0, false);

		support_back_r2 = new ModelMapper(modelDataWrapper);
		support_back_r2.setPos(0, 28, -20);
		right_engine.addChild(support_back_r2);
		setRotationAngle(support_back_r2, 0.8203F, 0, 0);
		support_back_r2.texOffs(577, 725).addBox(-95, -13, 0, 6, 13, 38, 0.1F, false);

		support_top_r2 = new ModelMapper(modelDataWrapper);
		support_top_r2.setPos(0, 11, -106);
		right_engine.addChild(support_top_r2);
		setRotationAngle(support_top_r2, 0.1222F, 0, 0);
		support_top_r2.texOffs(699, 732).addBox(-95, 0, 0, 6, 3, 56, 0.1F, false);

		window_interior = new ModelMapper(modelDataWrapper);
		window_interior.setPos(0, 24, 0);
		window_interior.texOffs(68, 749).addBox(-32, 8, -8, 32, 1, 16, 0, false);
		window_interior.texOffs(251, 109).addBox(-21, -17, -8, 7, 0, 16, 0, false);
		window_interior.texOffs(334, 109).addBox(-8, -26, -8, 8, 0, 16, 0, false);

		roof_side_r1 = new ModelMapper(modelDataWrapper);
		roof_side_r1.setPos(-8, -26, 0);
		window_interior.addChild(roof_side_r1);
		setRotationAngle(roof_side_r1, 0, 0, -0.1745F);
		roof_side_r1.texOffs(320, 242).addBox(-8, 0, -8, 8, 0, 16, 0, false);

		luggage_rack_top_r1 = new ModelMapper(modelDataWrapper);
		luggage_rack_top_r1.setPos(-13, -24, 0);
		window_interior.addChild(luggage_rack_top_r1);
		setRotationAngle(luggage_rack_top_r1, 0, 0, 0.3491F);
		luggage_rack_top_r1.texOffs(18, 0).addBox(-3, 0, -8, 3, 0, 16, 0, false);

		luggage_rack_front_r1 = new ModelMapper(modelDataWrapper);
		luggage_rack_front_r1.setPos(-12, -18, -8);
		window_interior.addChild(luggage_rack_front_r1);
		setRotationAngle(luggage_rack_front_r1, 0, 0, -0.1745F);
		luggage_rack_front_r1.texOffs(327, 612).addBox(0, -7, 0, 0, 7, 16, 0, false);

		luggage_rack_bottom_front_r1 = new ModelMapper(modelDataWrapper);
		luggage_rack_bottom_front_r1.setPos(-14, -17, 0);
		window_interior.addChild(luggage_rack_bottom_front_r1);
		setRotationAngle(luggage_rack_bottom_front_r1, 0, 0, -0.5236F);
		luggage_rack_bottom_front_r1.texOffs(18, 16).addBox(0, 0, -8, 3, 0, 16, 0, false);

		right_top_wall_r2 = new ModelMapper(modelDataWrapper);
		right_top_wall_r2.setPos(-22, -18, 0);
		window_interior.addChild(right_top_wall_r2);
		setRotationAngle(right_top_wall_r2, 0, 0, -0.384F);
		right_top_wall_r2.texOffs(255, 175).addBox(-5, 0, -8, 5, 0, 16, 0, false);

		window_interior_wall = new ModelMapper(modelDataWrapper);
		window_interior_wall.setPos(0, 24, 0);
		window_interior_wall.texOffs(673, 176).addBox(-30, -10, -8, 0, 12, 16, 0, false);

		right_upper_wall_r1 = new ModelMapper(modelDataWrapper);
		right_upper_wall_r1.setPos(-32, -10, 0);
		window_interior_wall.addChild(right_upper_wall_r1);
		setRotationAngle(right_upper_wall_r1, 0, 0, 0.5236F);
		right_upper_wall_r1.texOffs(673, 188).addBox(2, -9, -8, 0, 9, 16, 0, false);

		right_lower_wall_r1 = new ModelMapper(modelDataWrapper);
		right_lower_wall_r1.setPos(-30, 2, 0);
		window_interior_wall.addChild(right_lower_wall_r1);
		setRotationAngle(right_lower_wall_r1, 0, 0, -0.2967F);
		right_lower_wall_r1.texOffs(625, 227).addBox(0, 0, -8, 0, 7, 16, 0, false);

		window_interior_light = new ModelMapper(modelDataWrapper);
		window_interior_light.setPos(0, 24, 0);


		light_r1 = new ModelMapper(modelDataWrapper);
		light_r1.setPos(-21, -17, 0);
		window_interior_light.addChild(light_r1);
		setRotationAngle(light_r1, 0, 0, 0.5236F);
		light_r1.texOffs(18, 32).addBox(-2, 0, -8, 2, 0, 16, 0, false);

		window_interior_blank = new ModelMapper(modelDataWrapper);
		window_interior_blank.setPos(0, 24, 0);
		window_interior_blank.texOffs(94, 636).addBox(-32, 8, -4, 32, 1, 8, 0, false);
		window_interior_blank.texOffs(124, 139).addBox(-21, -17, -4, 7, 0, 8, 0, false);
		window_interior_blank.texOffs(279, 261).addBox(-8, -26, -4, 8, 0, 8, 0, false);

		roof_side_r2 = new ModelMapper(modelDataWrapper);
		roof_side_r2.setPos(-8, -26, 0);
		window_interior_blank.addChild(roof_side_r2);
		setRotationAngle(roof_side_r2, 0, 0, -0.1745F);
		roof_side_r2.texOffs(342, 125).addBox(-8, 0, -4, 8, 0, 8, 0, false);

		luggage_rack_top_r2 = new ModelMapper(modelDataWrapper);
		luggage_rack_top_r2.setPos(-13, -24, 0);
		window_interior_blank.addChild(luggage_rack_top_r2);
		setRotationAngle(luggage_rack_top_r2, 0, 0, 0.3491F);
		luggage_rack_top_r2.texOffs(26, 48).addBox(-3, 0, -4, 3, 0, 8, 0, false);

		luggage_rack_front_r2 = new ModelMapper(modelDataWrapper);
		luggage_rack_front_r2.setPos(-12, -18, 0);
		window_interior_blank.addChild(luggage_rack_front_r2);
		setRotationAngle(luggage_rack_front_r2, 0, 0, -0.1745F);
		luggage_rack_front_r2.texOffs(350, 146).addBox(0, -7, -4, 0, 7, 8, 0, false);

		luggage_rack_bottom_front_r2 = new ModelMapper(modelDataWrapper);
		luggage_rack_bottom_front_r2.setPos(-14, -17, 0);
		window_interior_blank.addChild(luggage_rack_bottom_front_r2);
		setRotationAngle(luggage_rack_bottom_front_r2, 0, 0, -0.5236F);
		luggage_rack_bottom_front_r2.texOffs(26, 56).addBox(0, 0, -4, 3, 0, 8, 0, false);

		right_top_wall_r3 = new ModelMapper(modelDataWrapper);
		right_top_wall_r3.setPos(-22, -18, 0);
		window_interior_blank.addChild(right_top_wall_r3);
		setRotationAngle(right_top_wall_r3, 0, 0, -0.384F);
		right_top_wall_r3.texOffs(8, 0).addBox(-5, 0, -4, 5, 0, 8, 0, false);

		window_interior_blank_wall = new ModelMapper(modelDataWrapper);
		window_interior_blank_wall.setPos(0, 24, 0);
		window_interior_blank_wall.texOffs(350, 125).addBox(-30, -10, -4, 0, 12, 8, 0, false);

		right_upper_wall_r2 = new ModelMapper(modelDataWrapper);
		right_upper_wall_r2.setPos(-32, -10, 0);
		window_interior_blank_wall.addChild(right_upper_wall_r2);
		setRotationAngle(right_upper_wall_r2, 0, 0, 0.5236F);
		right_upper_wall_r2.texOffs(350, 137).addBox(2, -9, -4, 0, 9, 8, 0, false);

		right_lower_wall_r2 = new ModelMapper(modelDataWrapper);
		right_lower_wall_r2.setPos(-30, 2, 0);
		window_interior_blank_wall.addChild(right_lower_wall_r2);
		setRotationAngle(right_lower_wall_r2, 0, 0, -0.2967F);
		right_lower_wall_r2.texOffs(350, 153).addBox(0, 0, -4, 0, 7, 8, 0, false);

		window_interior_blank_light = new ModelMapper(modelDataWrapper);
		window_interior_blank_light.setPos(0, 24, 0);


		light_r2 = new ModelMapper(modelDataWrapper);
		light_r2.setPos(-21, -17, 0);
		window_interior_blank_light.addChild(light_r2);
		setRotationAngle(light_r2, 0, 0, 0.5236F);
		light_r2.texOffs(0, 0).addBox(-2, 0, -4, 2, 0, 8, 0, false);

		door_left_exterior = new ModelMapper(modelDataWrapper);
		door_left_exterior.setPos(0, 24, 0);
		door_left_exterior.texOffs(41, 796).addBox(32, -10, -225, 0, 18, 12, 0, false);

		right_wall_front_r2 = new ModelMapper(modelDataWrapper);
		right_wall_front_r2.setPos(32, 0, -225);
		door_left_exterior.addChild(right_wall_front_r2);
		setRotationAngle(right_wall_front_r2, 0, 0.1745F, 0);
		right_wall_front_r2.texOffs(0, 859).addBox(0, -10, -4, 0, 18, 4, 0, false);

		right_top_wall_front_r1 = new ModelMapper(modelDataWrapper);
		right_top_wall_front_r1.setPos(32, -10, -225);
		door_left_exterior.addChild(right_top_wall_front_r1);
		setRotationAngle(right_top_wall_front_r1, 0, 0.1745F, -0.5236F);
		right_top_wall_front_r1.texOffs(0, 836).addBox(0, -13, -4, 0, 13, 4, 0, false);

		right_top_wall_r4 = new ModelMapper(modelDataWrapper);
		right_top_wall_r4.setPos(32, -10, 0);
		door_left_exterior.addChild(right_top_wall_r4);
		setRotationAngle(right_top_wall_r4, 0, 0, -0.5236F);
		right_top_wall_r4.texOffs(0, 812).addBox(0, -13, -225, 0, 13, 12, 0, false);

		top_back_r2 = new ModelMapper(modelDataWrapper);
		top_back_r2.setPos(9, -33, -191);
		door_left_exterior.addChild(top_back_r2);
		setRotationAngle(top_back_r2, 0.0873F, 0, 0.5236F);
		top_back_r2.texOffs(-16, 808).addBox(13, 0, -38, 9, 0, 16, 0, false);

		door_left_interior = new ModelMapper(modelDataWrapper);
		door_left_interior.setPos(0, 24, 0);
		door_left_interior.texOffs(94, 907).addBox(29, -10, -225, 3, 18, 12, 0, false);

		right_wall_front_r3 = new ModelMapper(modelDataWrapper);
		right_wall_front_r3.setPos(32, 0, -225);
		door_left_interior.addChild(right_wall_front_r3);
		setRotationAngle(right_wall_front_r3, 0, 0.1745F, 0);
		right_wall_front_r3.texOffs(0, 910).addBox(-3, -10, -4, 3, 18, 4, 0, false);

		right_top_wall_front_r2 = new ModelMapper(modelDataWrapper);
		right_top_wall_front_r2.setPos(32, -10, -225);
		door_left_interior.addChild(right_top_wall_front_r2);
		setRotationAngle(right_top_wall_front_r2, 0, 0.1745F, -0.5236F);
		right_top_wall_front_r2.texOffs(14, 915).addBox(-3, -13, -4, 3, 13, 4, 0, false);

		right_top_wall_r5 = new ModelMapper(modelDataWrapper);
		right_top_wall_r5.setPos(32, -10, 0);
		door_left_interior.addChild(right_top_wall_r5);
		setRotationAngle(right_top_wall_r5, 0, 0, -0.5236F);
		right_top_wall_r5.texOffs(16, 923).addBox(-3, -13, -225, 3, 13, 12, 0, false);

		top_back_r3 = new ModelMapper(modelDataWrapper);
		top_back_r3.setPos(9, -33, -191);
		door_left_interior.addChild(top_back_r3);
		setRotationAngle(top_back_r3, 0.0873F, 0, 0.5236F);
		top_back_r3.texOffs(42, 907).addBox(13, 0, -38, 9, 3, 16, 0, false);

		door_right_exterior = new ModelMapper(modelDataWrapper);
		door_right_exterior.setPos(0, 24, 0);
		door_right_exterior.texOffs(136, 794).addBox(-32, -10, -225, 0, 18, 12, 0, false);

		right_wall_front_r4 = new ModelMapper(modelDataWrapper);
		right_wall_front_r4.setPos(-32, 0, -225);
		door_right_exterior.addChild(right_wall_front_r4);
		setRotationAngle(right_wall_front_r4, 0, -0.1745F, 0);
		right_wall_front_r4.texOffs(8, 859).addBox(0, -10, -4, 0, 18, 4, 0, false);

		right_top_wall_front_r3 = new ModelMapper(modelDataWrapper);
		right_top_wall_front_r3.setPos(-32, -10, -225);
		door_right_exterior.addChild(right_top_wall_front_r3);
		setRotationAngle(right_top_wall_front_r3, 0, -0.1745F, 0.5236F);
		right_top_wall_front_r3.texOffs(8, 836).addBox(0, -13, -4, 0, 13, 4, 0, false);

		right_top_wall_r6 = new ModelMapper(modelDataWrapper);
		right_top_wall_r6.setPos(-32, -10, 0);
		door_right_exterior.addChild(right_top_wall_r6);
		setRotationAngle(right_top_wall_r6, 0, 0, 0.5236F);
		right_top_wall_r6.texOffs(26, 857).addBox(0, -13, -225, 0, 13, 12, 0, false);

		top_back_r4 = new ModelMapper(modelDataWrapper);
		top_back_r4.setPos(-9, -33, -191);
		door_right_exterior.addChild(top_back_r4);
		setRotationAngle(top_back_r4, 0.0873F, 0, -0.5236F);
		top_back_r4.texOffs(82, 808).addBox(-22, 0, -38, 9, 0, 16, 0, false);

		door_right_interior = new ModelMapper(modelDataWrapper);
		door_right_interior.setPos(0, 24, 0);
		door_right_interior.texOffs(28, 893).addBox(-32, -10, -225, 3, 18, 12, 0, false);

		right_wall_front_r5 = new ModelMapper(modelDataWrapper);
		right_wall_front_r5.setPos(-32, 0, -225);
		door_right_interior.addChild(right_wall_front_r5);
		setRotationAngle(right_wall_front_r5, 0, -0.1745F, 0);
		right_wall_front_r5.texOffs(14, 893).addBox(0, -10, -4, 3, 18, 4, 0, false);

		right_top_wall_front_r4 = new ModelMapper(modelDataWrapper);
		right_top_wall_front_r4.setPos(-32, -10, -225);
		door_right_interior.addChild(right_top_wall_front_r4);
		setRotationAngle(right_top_wall_front_r4, 0, -0.1745F, 0.5236F);
		right_top_wall_front_r4.texOffs(0, 893).addBox(0, -13, -4, 3, 13, 4, 0, false);

		right_top_wall_r7 = new ModelMapper(modelDataWrapper);
		right_top_wall_r7.setPos(-32, -10, 0);
		door_right_interior.addChild(right_top_wall_r7);
		setRotationAngle(right_top_wall_r7, 0, 0, 0.5236F);
		right_top_wall_r7.texOffs(76, 894).addBox(0, -13, -225, 3, 13, 12, 0, false);

		top_back_r5 = new ModelMapper(modelDataWrapper);
		top_back_r5.setPos(-9, -33, -191);
		door_right_interior.addChild(top_back_r5);
		setRotationAngle(top_back_r5, 0.0873F, 0, -0.5236F);
		top_back_r5.texOffs(46, 875).addBox(-22, 0, -38, 9, 3, 16, 0, false);

		head_interior = new ModelMapper(modelDataWrapper);
		head_interior.setPos(0, 24, 0);
		head_interior.texOffs(673, 192).addBox(-32, 8, -230, 64, 1, 32, 0, false);
		head_interior.texOffs(759, 240).addBox(-30, -26, -230, 60, 34, 0, 0, false);
		head_interior.texOffs(424, 777).addBox(8, -26, -206, 22, 34, 8, 0, false);
		head_interior.texOffs(313, 556).addBox(-30, -26, -206, 22, 34, 8, 0, false);
		head_interior.texOffs(92, 139).addBox(-8, -26, -206, 16, 0, 8, 0, false);

		right_door_top_r1 = new ModelMapper(modelDataWrapper);
		right_door_top_r1.setPos(-9, -33, -191);
		head_interior.addChild(right_door_top_r1);
		setRotationAngle(right_door_top_r1, 0.0873F, 0, -0.5236F);
		right_door_top_r1.texOffs(46, 879).addBox(-13, 0, -39, 0, 2, 17, 0, true);

		left_door_top_r1 = new ModelMapper(modelDataWrapper);
		left_door_top_r1.setPos(9, -33, -191);
		head_interior.addChild(left_door_top_r1);
		setRotationAngle(left_door_top_r1, 0.0873F, 0, 0.5236F);
		left_door_top_r1.texOffs(46, 877).addBox(13, 0, -39, 0, 2, 17, 0, false);

		roof_front_r1 = new ModelMapper(modelDataWrapper);
		roof_front_r1.setPos(0, -26, -206);
		head_interior.addChild(roof_front_r1);
		setRotationAngle(roof_front_r1, 0.0873F, 0, 0);
		roof_front_r1.texOffs(69, 556).addBox(-8, 0, -25, 16, 0, 25, 0, false);

		right_roof_side_r1 = new ModelMapper(modelDataWrapper);
		right_roof_side_r1.setPos(-8, -26, -206);
		head_interior.addChild(right_roof_side_r1);
		setRotationAngle(right_roof_side_r1, 0.0873F, 0, -0.1745F);
		right_roof_side_r1.texOffs(311, 192).addBox(-15, 0, -25, 15, 0, 25, 0, false);

		left_roof_side_r1 = new ModelMapper(modelDataWrapper);
		left_roof_side_r1.setPos(8, -26, -206);
		head_interior.addChild(left_roof_side_r1);
		setRotationAngle(left_roof_side_r1, 0.0873F, 0, 0.1745F);
		left_roof_side_r1.texOffs(311, 217).addBox(0, 0, -25, 15, 0, 25, 0, false);

		right_upper_wall_r3 = new ModelMapper(modelDataWrapper);
		right_upper_wall_r3.setPos(-32, -10, -206);
		head_interior.addChild(right_upper_wall_r3);
		setRotationAngle(right_upper_wall_r3, 0, -0.0436F, 0.5236F);
		right_upper_wall_r3.texOffs(699, 700).addBox(2, -17, -25, 0, 17, 25, 0, false);

		left_upper_wall_r1 = new ModelMapper(modelDataWrapper);
		left_upper_wall_r1.setPos(32, -10, -206);
		head_interior.addChild(left_upper_wall_r1);
		setRotationAngle(left_upper_wall_r1, 0, 0.0436F, -0.5236F);
		left_upper_wall_r1.texOffs(511, 701).addBox(-2, -17, -25, 0, 17, 25, 0, false);

		right_wall_r3 = new ModelMapper(modelDataWrapper);
		right_wall_r3.setPos(-30, 2, -206);
		head_interior.addChild(right_wall_r3);
		setRotationAngle(right_wall_r3, 0, -0.0436F, 0);
		right_wall_r3.texOffs(445, 713).addBox(0, -12, -25, 0, 12, 25, 0, false);

		left_wall_r3 = new ModelMapper(modelDataWrapper);
		left_wall_r3.setPos(30, 2, -206);
		head_interior.addChild(left_wall_r3);
		setRotationAngle(left_wall_r3, 0, 0.0436F, 0);
		left_wall_r3.texOffs(699, 717).addBox(0, -12, -25, 0, 12, 25, 0, false);

		right_lower_wall_r3 = new ModelMapper(modelDataWrapper);
		right_lower_wall_r3.setPos(-30, 2, -206);
		head_interior.addChild(right_lower_wall_r3);
		setRotationAngle(right_lower_wall_r3, 0, -0.0436F, -0.2967F);
		right_lower_wall_r3.texOffs(231, 114).addBox(0, 0, -25, 0, 7, 25, 0, false);

		left_lower_wall_r1 = new ModelMapper(modelDataWrapper);
		left_lower_wall_r1.setPos(30, 2, -206);
		head_interior.addChild(left_lower_wall_r1);
		setRotationAngle(left_lower_wall_r1, 0, 0.0436F, 0.2967F);
		left_lower_wall_r1.texOffs(575, 218).addBox(0, 0, -25, 0, 7, 25, 0, false);

		end_interior = new ModelMapper(modelDataWrapper);
		end_interior.setPos(0, 24, 0);
		end_interior.texOffs(79, 236).addBox(-32, 8, 106, 64, 1, 80, 0, false);
		end_interior.texOffs(758, 559).addBox(-30, -26, 186, 60, 34, 0, 0, false);
		end_interior.texOffs(354, 777).addBox(8, -26, 146, 17, 34, 18, 0, false);
		end_interior.texOffs(284, 777).addBox(-25, -26, 146, 17, 34, 18, 0, false);
		end_interior.texOffs(0, 175).addBox(-8, -26, 106, 16, 0, 81, 0, false);
		end_interior.texOffs(0, 0).addBox(14, -17, 106, 7, 0, 40, 0, false);
		end_interior.texOffs(0, 40).addBox(-21, -17, 106, 7, 0, 40, 0, false);

		right_luggage_rack_top_r1 = new ModelMapper(modelDataWrapper);
		right_luggage_rack_top_r1.setPos(-13, -24, 0);
		end_interior.addChild(right_luggage_rack_top_r1);
		setRotationAngle(right_luggage_rack_top_r1, 0, 0, 0.3491F);
		right_luggage_rack_top_r1.texOffs(0, 80).addBox(-3, 0, 106, 3, 0, 40, 0, false);

		left_luggage_rack_top_r1 = new ModelMapper(modelDataWrapper);
		left_luggage_rack_top_r1.setPos(13, -24, 0);
		end_interior.addChild(left_luggage_rack_top_r1);
		setRotationAngle(left_luggage_rack_top_r1, 0, 0, -0.3491F);
		left_luggage_rack_top_r1.texOffs(14, 40).addBox(0, 0, 106, 3, 0, 40, 0, false);

		right_luggage_rack_front_r1 = new ModelMapper(modelDataWrapper);
		right_luggage_rack_front_r1.setPos(-12, -18, 0);
		end_interior.addChild(right_luggage_rack_front_r1);
		setRotationAngle(right_luggage_rack_front_r1, 0, 0, -0.1745F);
		right_luggage_rack_front_r1.texOffs(287, 257).addBox(0, -7, 106, 0, 7, 40, 0, false);

		left_luggage_rack_front_r1 = new ModelMapper(modelDataWrapper);
		left_luggage_rack_front_r1.setPos(12, -18, 0);
		end_interior.addChild(left_luggage_rack_front_r1);
		setRotationAngle(left_luggage_rack_front_r1, 0, 0, 0.1745F);
		left_luggage_rack_front_r1.texOffs(0, 267).addBox(0, -7, 106, 0, 7, 40, 0, false);

		right_luggage_rack_bottom_front_r1 = new ModelMapper(modelDataWrapper);
		right_luggage_rack_bottom_front_r1.setPos(-14, -17, 0);
		end_interior.addChild(right_luggage_rack_bottom_front_r1);
		setRotationAngle(right_luggage_rack_bottom_front_r1, 0, 0, -0.5236F);
		right_luggage_rack_bottom_front_r1.texOffs(6, 80).addBox(0, 0, 106, 3, 0, 40, 0, false);

		left_luggage_rack_bottom_front_r1 = new ModelMapper(modelDataWrapper);
		left_luggage_rack_bottom_front_r1.setPos(14, -17, 0);
		end_interior.addChild(left_luggage_rack_bottom_front_r1);
		setRotationAngle(left_luggage_rack_bottom_front_r1, 0, 0, 0.5236F);
		left_luggage_rack_bottom_front_r1.texOffs(14, 0).addBox(-3, 0, 106, 3, 0, 40, 0, false);

		right_top_wall_r8 = new ModelMapper(modelDataWrapper);
		right_top_wall_r8.setPos(-22, -18, 0);
		end_interior.addChild(right_top_wall_r8);
		setRotationAngle(right_top_wall_r8, 0, 0, -0.384F);
		right_top_wall_r8.texOffs(105, 58).addBox(-5, 0, 106, 5, 0, 28, 0, false);

		left_top_wall_r2 = new ModelMapper(modelDataWrapper);
		left_top_wall_r2.setPos(22, -18, 0);
		end_interior.addChild(left_top_wall_r2);
		setRotationAngle(left_top_wall_r2, 0, 0, 0.384F);
		left_top_wall_r2.texOffs(105, 30).addBox(0, 0, 106, 5, 0, 28, 0, false);

		right_roof_side_r2 = new ModelMapper(modelDataWrapper);
		right_roof_side_r2.setPos(-8, -26, 0);
		end_interior.addChild(right_roof_side_r2);
		setRotationAngle(right_roof_side_r2, 0, 0, -0.1745F);
		right_roof_side_r2.texOffs(225, 192).addBox(-15, 0, 106, 15, 0, 81, 0, false);

		left_roof_side_r2 = new ModelMapper(modelDataWrapper);
		left_roof_side_r2.setPos(8, -26, 0);
		end_interior.addChild(left_roof_side_r2);
		setRotationAngle(left_roof_side_r2, 0, 0, 0.1745F);
		left_roof_side_r2.texOffs(246, 0).addBox(0, 0, 106, 15, 0, 81, 0, false);

		right_upper_wall_r4 = new ModelMapper(modelDataWrapper);
		right_upper_wall_r4.setPos(-32, -10, 106);
		end_interior.addChild(right_upper_wall_r4);
		setRotationAngle(right_upper_wall_r4, 0, 0.0873F, 0.5236F);
		right_upper_wall_r4.texOffs(374, 553).addBox(2, -23, 0, 0, 23, 81, 0, false);

		left_upper_wall_r2 = new ModelMapper(modelDataWrapper);
		left_upper_wall_r2.setPos(32, -10, 106);
		end_interior.addChild(left_upper_wall_r2);
		setRotationAngle(left_upper_wall_r2, 0, -0.0873F, -0.5236F);
		left_upper_wall_r2.texOffs(536, 553).addBox(-2, -23, 0, 0, 23, 81, 0, false);

		right_wall_r4 = new ModelMapper(modelDataWrapper);
		right_wall_r4.setPos(-30, 2, 106);
		end_interior.addChild(right_wall_r4);
		setRotationAngle(right_wall_r4, 0, 0.1309F, 0);
		right_wall_r4.texOffs(184, 555).addBox(0, -16, 0, 0, 16, 81, 0, false);

		left_wall_r4 = new ModelMapper(modelDataWrapper);
		left_wall_r4.setPos(30, 2, 106);
		end_interior.addChild(left_wall_r4);
		setRotationAngle(left_wall_r4, 0, -0.1309F, 0);
		left_wall_r4.texOffs(184, 571).addBox(0, -16, 0, 0, 16, 81, 0, false);

		right_lower_wall_r4 = new ModelMapper(modelDataWrapper);
		right_lower_wall_r4.setPos(-30, 2, 106);
		end_interior.addChild(right_lower_wall_r4);
		setRotationAngle(right_lower_wall_r4, 0, 0.1309F, -0.2967F);
		right_lower_wall_r4.texOffs(575, 192).addBox(0, 0, 0, 0, 10, 81, 0, false);

		left_lower_wall_r2 = new ModelMapper(modelDataWrapper);
		left_lower_wall_r2.setPos(30, 2, 106);
		end_interior.addChild(left_lower_wall_r2);
		setRotationAngle(left_lower_wall_r2, 0, -0.1309F, 0.2967F);
		left_lower_wall_r2.texOffs(575, 202).addBox(0, 0, 0, 0, 10, 81, 0, false);

		end_light = new ModelMapper(modelDataWrapper);
		end_light.setPos(0, 24, 0);


		left_light_r1 = new ModelMapper(modelDataWrapper);
		left_light_r1.setPos(21, -17, 0);
		end_light.addChild(left_light_r1);
		setRotationAngle(left_light_r1, 0, 0, -0.5236F);
		left_light_r1.texOffs(20, 0).addBox(0, 0, 106, 2, 0, 40, 0, false);

		right_light_r1 = new ModelMapper(modelDataWrapper);
		right_light_r1.setPos(-21, -17, 0);
		end_light.addChild(right_light_r1);
		setRotationAngle(right_light_r1, 0, 0, 0.5236F);
		right_light_r1.texOffs(20, 40).addBox(-2, 0, 106, 2, 0, 40, 0, false);

		emergency_exit = new ModelMapper(modelDataWrapper);
		emergency_exit.setPos(0, 24, 0);
		emergency_exit.texOffs(68, 763).addBox(30, -10, -12, 0, 12, 24, 0, false);
		emergency_exit.texOffs(511, 690).addBox(-30, -10, -12, 0, 12, 24, 0, false);

		right_upper_wall_r5 = new ModelMapper(modelDataWrapper);
		right_upper_wall_r5.setPos(-32, -10, 0);
		emergency_exit.addChild(right_upper_wall_r5);
		setRotationAngle(right_upper_wall_r5, 0, 0, 0.5236F);
		right_upper_wall_r5.texOffs(220, 661).addBox(2, -9, -12, 0, 9, 24, 0, false);

		right_lower_wall_r5 = new ModelMapper(modelDataWrapper);
		right_lower_wall_r5.setPos(-30, 2, 0);
		emergency_exit.addChild(right_lower_wall_r5);
		setRotationAngle(right_lower_wall_r5, 0, 0, -0.2967F);
		right_lower_wall_r5.texOffs(80, 283).addBox(0, 0, -12, 0, 7, 24, 0, false);

		left_upper_wall_r3 = new ModelMapper(modelDataWrapper);
		left_upper_wall_r3.setPos(32, -10, 0);
		emergency_exit.addChild(left_upper_wall_r3);
		setRotationAngle(left_upper_wall_r3, 0, 0, -0.5236F);
		left_upper_wall_r3.texOffs(184, 753).addBox(-2, -9, -12, 0, 9, 24, 0, false);

		left_lower_wall_r3 = new ModelMapper(modelDataWrapper);
		left_lower_wall_r3.setPos(30, 2, 0);
		emergency_exit.addChild(left_lower_wall_r3);
		setRotationAngle(left_lower_wall_r3, 0, 0, 0.2967F);
		left_lower_wall_r3.texOffs(0, 771).addBox(0, 0, -12, 0, 7, 24, 0, false);

		seat_nice = new ModelMapper(modelDataWrapper);
		seat_nice.setPos(0, 24, 0);
		seat_nice.texOffs(864, 102).addBox(-12, 1, -4, 24, 2, 6, 0, false);
		seat_nice.texOffs(873, 157).addBox(4.5F, -11, 2.5F, 7, 6, 1, 0, false);
		seat_nice.texOffs(889, 157).addBox(-3.5F, -11, 2.5F, 7, 6, 1, 0, false);
		seat_nice.texOffs(905, 157).addBox(-11.5F, -11, 2.5F, 7, 6, 1, 0, false);
		seat_nice.texOffs(47, 119).addBox(11.5F, -2, -3, 1, 1, 5, 0, false);
		seat_nice.texOffs(47, 119).addBox(3.5F, -2, -3, 1, 1, 5, 0, false);
		seat_nice.texOffs(47, 119).addBox(-4.5F, -2, -3, 1, 1, 5, 0, false);
		seat_nice.texOffs(47, 119).addBox(-12.5F, -2, -3, 1, 1, 5, 0, false);

		back_bottom_right_r2 = new ModelMapper(modelDataWrapper);
		back_bottom_right_r2.setPos(0, 3, 2);
		seat_nice.addChild(back_bottom_right_r2);
		setRotationAngle(back_bottom_right_r2, -0.1745F, 0, 0);
		back_bottom_right_r2.texOffs(907, 87).addBox(-11.5F, -11.2F, -1.2F, 7, 11, 1, 0.2F, false);
		back_bottom_right_r2.texOffs(891, 87).addBox(-3.5F, -11.2F, -1.2F, 7, 11, 1, 0.2F, false);
		back_bottom_right_r2.texOffs(875, 87).addBox(4.5F, -11.2F, -1.2F, 7, 11, 1, 0.2F, false);

		seat_normal = new ModelMapper(modelDataWrapper);
		seat_normal.setPos(0, 24, 0);
		seat_normal.texOffs(875, 79).addBox(-12, 1, -4, 24, 2, 6, 0, false);
		seat_normal.texOffs(825, 157).addBox(4.5F, -11, 2.5F, 7, 6, 1, 0, false);
		seat_normal.texOffs(841, 157).addBox(-3.5F, -11, 2.5F, 7, 6, 1, 0, false);
		seat_normal.texOffs(857, 157).addBox(-11.5F, -11, 2.5F, 7, 6, 1, 0, false);
		seat_normal.texOffs(0, 3).addBox(11.5F, -2, -3, 1, 1, 5, 0, false);
		seat_normal.texOffs(0, 3).addBox(3.5F, -2, -3, 1, 1, 5, 0, false);
		seat_normal.texOffs(0, 3).addBox(-4.5F, -2, -3, 1, 1, 5, 0, false);
		seat_normal.texOffs(0, 3).addBox(-12.5F, -2, -3, 1, 1, 5, 0, false);

		back_bottom_right_r3 = new ModelMapper(modelDataWrapper);
		back_bottom_right_r3.setPos(0, 3, 2);
		seat_normal.addChild(back_bottom_right_r3);
		setRotationAngle(back_bottom_right_r3, -0.1745F, 0, 0);
		back_bottom_right_r3.texOffs(826, 111).addBox(-11.5F, -11.2F, -1.2F, 7, 11, 1, 0.2F, false);
		back_bottom_right_r3.texOffs(810, 111).addBox(-3.5F, -11.2F, -1.2F, 7, 11, 1, 0.2F, false);
		back_bottom_right_r3.texOffs(794, 111).addBox(4.5F, -11.2F, -1.2F, 7, 11, 1, 0.2F, false);

		modelDataWrapper.setModelPart(textureWidth, textureHeight);
		exterior.setModelPart();
		window_interior.setModelPart();
		window_interior_wall.setModelPart();
		window_interior_light.setModelPart();
		window_interior_blank.setModelPart();
		window_interior_blank_wall.setModelPart();
		window_interior_blank_light.setModelPart();
		door_left_exterior.setModelPart();
		door_left_interior.setModelPart();
		door_right_exterior.setModelPart();
		door_right_interior.setModelPart();
		head_interior.setModelPart();
		end_interior.setModelPart();
		end_light.setModelPart();
		emergency_exit.setModelPart();
		seat_nice.setModelPart();
		seat_normal.setModelPart();
	}

	private static final int DOOR_MAX = 16;

	@Override
	public ModelA320 createNew(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		return new ModelA320(doorAnimationType, renderDoorOverlay);
	}

	@Override
	protected void baseTransform(PoseStack matrices) {
		matrices.translate(0, -3.5, 2.375);
	}

	@Override
	protected void renderWindowPositions(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
		switch (renderStage) {
			case LIGHTS:
				renderOnce(end_light, matrices, vertices, light, position);
				renderMirror(window_interior_blank_light, matrices, vertices, light, position - 194);

				for (int i = 0; i < 7; i++) {
					renderMirror(window_interior_light, matrices, vertices, light, position + i * 16 - 182);
				}

				renderMirror(window_interior_light, matrices, vertices, light, position - 70);
				renderMirror(window_interior_blank_light, matrices, vertices, light, position - 58);

				for (int i = 0; i < 10; i++) {
					renderMirror(window_interior_light, matrices, vertices, light, position + i * 16 - 46);
				}

				break;
			case INTERIOR:
				renderOnce(head_interior, matrices, vertices, light, position);
				renderOnce(end_interior, matrices, vertices, light, position);

				renderMirror(window_interior_blank, matrices, vertices, light, position - 194);
				renderMirror(window_interior_blank_wall, matrices, vertices, light, position - 194);

				for (int i = 0; i < 7; i++) {
					renderMirror(window_interior, matrices, vertices, light, position + i * 16 - 182);
					renderMirror(window_interior_wall, matrices, vertices, light, position + i * 16 - 182);
				}

				renderOnce(emergency_exit, matrices, vertices, light, position - 66);
				renderMirror(window_interior, matrices, vertices, light, position - 70);
				renderMirror(window_interior_blank, matrices, vertices, light, position - 58);

				for (int i = 0; i < 10; i++) {
					renderMirror(window_interior, matrices, vertices, light, position + i * 16 - 46);
					renderMirror(window_interior_wall, matrices, vertices, light, position + i * 16 - 46);
				}

				renderOnce(door_left_interior, matrices, vertices, light, -doorLeftX * 6, position + doorLeftZ);
				renderOnce(door_right_interior, matrices, vertices, light, doorRightX * 6, position + doorRightZ);

				if (renderDetails) {
					for (int i = 0; i < 12; i++) {
						renderOnce(seat_nice, matrices, vertices, light, -16, i * 12 - 189);
						renderOnce(seat_nice, matrices, vertices, light, 16, i * 12 - 189);
					}
					for (int i = 0; i < 18; i++) {
						renderOnce(seat_normal, matrices, vertices, light, -16, i * 11 - 47);
						renderOnce(seat_normal, matrices, vertices, light, 16, i * 11 - 47);
					}
				}

				break;
			case EXTERIOR:
				renderOnce(exterior, matrices, vertices, light, position);
				renderOnce(door_left_exterior, matrices, vertices, light, -doorLeftX * 6, position + doorLeftZ);
				renderOnce(door_right_exterior, matrices, vertices, light, doorRightX * 6, position + doorRightZ);
				break;
		}
	}

	@Override
	protected void renderDoorPositions(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
	}

	@Override
	protected void renderHeadPosition1(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean useHeadlights) {
	}

	@Override
	protected void renderHeadPosition2(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean useHeadlights) {
	}

	@Override
	protected void renderEndPosition1(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
	}

	@Override
	protected void renderEndPosition2(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
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
		return new int[]{0};
	}

	@Override
	protected int[] getEndPositions() {
		return new int[]{0, 0};
	}

	@Override
	protected int getDoorMax() {
		return DOOR_MAX;
	}
}