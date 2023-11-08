package org.mtr.mod.model;

import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.ModelPartExtension;
import org.mtr.mod.client.DoorAnimationType;
import org.mtr.mod.resource.RenderStage;

public class ModelA320 extends ModelSimpleTrainBase<ModelA320> {

	private final ModelPartExtension exterior;
	private final ModelPartExtension main;
	private final ModelPartExtension right_roof_r1;
	private final ModelPartExtension left_roof_r1;
	private final ModelPartExtension right_top_wall_r1;
	private final ModelPartExtension left_top_wall_r1;
	private final ModelPartExtension right_bottom_wall_r1;
	private final ModelPartExtension left_bottom_wall_r1;
	private final ModelPartExtension right_floor_r1;
	private final ModelPartExtension left_floor_r1;
	private final ModelPartExtension front;
	private final ModelPartExtension top_right_back_r1;
	private final ModelPartExtension top_left_back_r1;
	private final ModelPartExtension bottom_right_r1;
	private final ModelPartExtension bottom_left_r1;
	private final ModelPartExtension bottom_front_right_r1;
	private final ModelPartExtension bottom_front_left_r1;
	private final ModelPartExtension windscreen_lower_right_r1;
	private final ModelPartExtension windscreen_lower_left_r1;
	private final ModelPartExtension windscreen_right_r1;
	private final ModelPartExtension windscreen_left_r1;
	private final ModelPartExtension right_wall_front_r1;
	private final ModelPartExtension left_wall_front_r1;
	private final ModelPartExtension right_wall_top_r1;
	private final ModelPartExtension left_wall_top_r1;
	private final ModelPartExtension right_wall_lower_r1;
	private final ModelPartExtension left_wall_lower_r1;
	private final ModelPartExtension right_wall_r1;
	private final ModelPartExtension left_wall_r1;
	private final ModelPartExtension top_back_r1;
	private final ModelPartExtension top_front_r1;
	private final ModelPartExtension bottom_r1;
	private final ModelPartExtension bottom_front_r1;
	private final ModelPartExtension windscreen_main_r1;
	private final ModelPartExtension windscreen_lower_r1;
	private final ModelPartExtension front_lower_r1;
	private final ModelPartExtension front_upper_r1;
	private final ModelPartExtension tail;
	private final ModelPartExtension top_r1;
	private final ModelPartExtension bottom_back_r1;
	private final ModelPartExtension bottom_r2;
	private final ModelPartExtension bottom_right_r2;
	private final ModelPartExtension bottom_left_r2;
	private final ModelPartExtension side_right_r1;
	private final ModelPartExtension side_left_r1;
	private final ModelPartExtension right_roof_top_r1;
	private final ModelPartExtension left_roof_top_r1;
	private final ModelPartExtension right_wall_top_r2;
	private final ModelPartExtension left_wall_top_r2;
	private final ModelPartExtension right_wall_back_r1;
	private final ModelPartExtension left_wall_back_r1;
	private final ModelPartExtension right_wall_r2;
	private final ModelPartExtension left_wall_r2;
	private final ModelPartExtension left_wing;
	private final ModelPartExtension edge_wing_bottom_r1;
	private final ModelPartExtension edge_wing_middle_r1;
	private final ModelPartExtension outer_bottom_cover_r1;
	private final ModelPartExtension outer_top_cover_r1;
	private final ModelPartExtension middle_top_cover_r1;
	private final ModelPartExtension back_flat_bottom_edge_r1;
	private final ModelPartExtension back_flat_top_edge_r1;
	private final ModelPartExtension back_outer_bottom_edge_r1;
	private final ModelPartExtension back_outer_top_edge_r1;
	private final ModelPartExtension back_middle_bottom_edge_r1;
	private final ModelPartExtension back_middle_top_edge_r1;
	private final ModelPartExtension front_outer_top_edge_r1;
	private final ModelPartExtension front_outer_top_edge_r2;
	private final ModelPartExtension front_middle_bottom_edge_r1;
	private final ModelPartExtension front_middle_top_edge_r1;
	private final ModelPartExtension right_wing;
	private final ModelPartExtension edge_wing_bottom_r2;
	private final ModelPartExtension edge_wing_middle_r2;
	private final ModelPartExtension outer_bottom_cover_r2;
	private final ModelPartExtension outer_top_cover_r2;
	private final ModelPartExtension middle_top_cover_r2;
	private final ModelPartExtension back_flat_bottom_edge_r2;
	private final ModelPartExtension back_flat_top_edge_r2;
	private final ModelPartExtension back_outer_bottom_edge_r2;
	private final ModelPartExtension back_outer_top_edge_r2;
	private final ModelPartExtension back_middle_bottom_edge_r2;
	private final ModelPartExtension back_middle_top_edge_r2;
	private final ModelPartExtension front_outer_top_edge_r3;
	private final ModelPartExtension front_outer_top_edge_r4;
	private final ModelPartExtension front_middle_bottom_edge_r2;
	private final ModelPartExtension front_middle_top_edge_r2;
	private final ModelPartExtension back_top_wing;
	private final ModelPartExtension back_r1;
	private final ModelPartExtension front_bottom_r1;
	private final ModelPartExtension front_top_r1;
	private final ModelPartExtension back_left_wing;
	private final ModelPartExtension back_r2;
	private final ModelPartExtension side_r1;
	private final ModelPartExtension front_r1;
	private final ModelPartExtension back_right_wing;
	private final ModelPartExtension back_r3;
	private final ModelPartExtension side_r2;
	private final ModelPartExtension front_r2;
	private final ModelPartExtension bottom;
	private final ModelPartExtension back_top_right_r1;
	private final ModelPartExtension back_top_left_r1;
	private final ModelPartExtension back_side_right_r1;
	private final ModelPartExtension back_side_left_r1;
	private final ModelPartExtension back_bottom_right_r1;
	private final ModelPartExtension back_bottom_left_r1;
	private final ModelPartExtension front_top_right_r1;
	private final ModelPartExtension front_top_left_r1;
	private final ModelPartExtension front_side_right_r1;
	private final ModelPartExtension front_side_left_r1;
	private final ModelPartExtension front_bottom_right_r1;
	private final ModelPartExtension front_bottom_left_r1;
	private final ModelPartExtension side_right_r2;
	private final ModelPartExtension side_left_r2;
	private final ModelPartExtension bottom_right_r3;
	private final ModelPartExtension bottom_left_r3;
	private final ModelPartExtension back_side_r1;
	private final ModelPartExtension back_bottom_r1;
	private final ModelPartExtension front_side_r1;
	private final ModelPartExtension front_bottom_r2;
	private final ModelPartExtension left_engine;
	private final ModelPartExtension tail_right_roof_r1;
	private final ModelPartExtension tail_left_roof_r1;
	private final ModelPartExtension tail_roof_r1;
	private final ModelPartExtension tail_right_wall_bottom_r1;
	private final ModelPartExtension tail_right_wall_top_r1;
	private final ModelPartExtension tail_right_wall_r1;
	private final ModelPartExtension tail_left_wall_bottom_r1;
	private final ModelPartExtension tail_left_wall_top_r1;
	private final ModelPartExtension tail_left_wall_r1;
	private final ModelPartExtension tail_right_floor_r1;
	private final ModelPartExtension tail_left_floor_r1;
	private final ModelPartExtension tail_floor_r1;
	private final ModelPartExtension back_right_roof_r1;
	private final ModelPartExtension back_left_roof_r1;
	private final ModelPartExtension back_roof_r1;
	private final ModelPartExtension back_right_wall_bottom_r1;
	private final ModelPartExtension back_right_wall_top_r1;
	private final ModelPartExtension back_right_wall_r1;
	private final ModelPartExtension back_left_wall_bottom_r1;
	private final ModelPartExtension back_left_wall_top_r1;
	private final ModelPartExtension back_left_wall_r1;
	private final ModelPartExtension back_right_floor_r1;
	private final ModelPartExtension back_left_floor_r1;
	private final ModelPartExtension back_floor_r1;
	private final ModelPartExtension front_right_roof_r1;
	private final ModelPartExtension front_left_roof_r1;
	private final ModelPartExtension front_roof_r1;
	private final ModelPartExtension front_right_wall_bottom_r1;
	private final ModelPartExtension front_right_wall_top_r1;
	private final ModelPartExtension front_right_wall_r1;
	private final ModelPartExtension front_left_wall_bottom_r1;
	private final ModelPartExtension front_left_wall_top_r1;
	private final ModelPartExtension front_left_wall_r1;
	private final ModelPartExtension front_right_floor_r1;
	private final ModelPartExtension front_left_floor_r1;
	private final ModelPartExtension front_floor_r1;
	private final ModelPartExtension right_roof_r2;
	private final ModelPartExtension left_roof_r2;
	private final ModelPartExtension right_wall_bottom_r1;
	private final ModelPartExtension right_wall_top_r3;
	private final ModelPartExtension left_wall_bottom_r1;
	private final ModelPartExtension left_wall_top_r3;
	private final ModelPartExtension right_floor_r2;
	private final ModelPartExtension left_floor_r2;
	private final ModelPartExtension support_back_r1;
	private final ModelPartExtension support_top_r1;
	private final ModelPartExtension right_engine;
	private final ModelPartExtension tail_right_roof_r2;
	private final ModelPartExtension tail_left_roof_r2;
	private final ModelPartExtension tail_roof_r2;
	private final ModelPartExtension tail_right_wall_bottom_r2;
	private final ModelPartExtension tail_right_wall_top_r2;
	private final ModelPartExtension tail_right_wall_r2;
	private final ModelPartExtension tail_left_wall_bottom_r2;
	private final ModelPartExtension tail_left_wall_top_r2;
	private final ModelPartExtension tail_left_wall_r2;
	private final ModelPartExtension tail_right_floor_r2;
	private final ModelPartExtension tail_left_floor_r2;
	private final ModelPartExtension tail_floor_r2;
	private final ModelPartExtension back_right_roof_r2;
	private final ModelPartExtension back_left_roof_r2;
	private final ModelPartExtension back_roof_r2;
	private final ModelPartExtension back_right_wall_bottom_r2;
	private final ModelPartExtension back_right_wall_top_r2;
	private final ModelPartExtension back_right_wall_r2;
	private final ModelPartExtension back_left_wall_bottom_r2;
	private final ModelPartExtension back_left_wall_top_r2;
	private final ModelPartExtension back_left_wall_r2;
	private final ModelPartExtension back_right_floor_r2;
	private final ModelPartExtension back_left_floor_r2;
	private final ModelPartExtension back_floor_r2;
	private final ModelPartExtension front_right_roof_r2;
	private final ModelPartExtension front_left_roof_r2;
	private final ModelPartExtension front_roof_r2;
	private final ModelPartExtension front_right_wall_bottom_r2;
	private final ModelPartExtension front_right_wall_top_r2;
	private final ModelPartExtension front_right_wall_r2;
	private final ModelPartExtension front_left_wall_bottom_r2;
	private final ModelPartExtension front_left_wall_top_r2;
	private final ModelPartExtension front_left_wall_r2;
	private final ModelPartExtension front_right_floor_r2;
	private final ModelPartExtension front_left_floor_r2;
	private final ModelPartExtension front_floor_r2;
	private final ModelPartExtension right_roof_r3;
	private final ModelPartExtension left_roof_r3;
	private final ModelPartExtension right_wall_bottom_r2;
	private final ModelPartExtension right_wall_top_r4;
	private final ModelPartExtension left_wall_bottom_r2;
	private final ModelPartExtension left_wall_top_r4;
	private final ModelPartExtension right_floor_r3;
	private final ModelPartExtension left_floor_r3;
	private final ModelPartExtension support_back_r2;
	private final ModelPartExtension support_top_r2;
	private final ModelPartExtension window_interior;
	private final ModelPartExtension roof_side_r1;
	private final ModelPartExtension luggage_rack_top_r1;
	private final ModelPartExtension luggage_rack_front_r1;
	private final ModelPartExtension luggage_rack_bottom_front_r1;
	private final ModelPartExtension right_top_wall_r2;
	private final ModelPartExtension window_interior_wall;
	private final ModelPartExtension right_upper_wall_r1;
	private final ModelPartExtension right_lower_wall_r1;
	private final ModelPartExtension window_interior_light;
	private final ModelPartExtension light_r1;
	private final ModelPartExtension window_interior_blank;
	private final ModelPartExtension roof_side_r2;
	private final ModelPartExtension luggage_rack_top_r2;
	private final ModelPartExtension luggage_rack_front_r2;
	private final ModelPartExtension luggage_rack_bottom_front_r2;
	private final ModelPartExtension right_top_wall_r3;
	private final ModelPartExtension window_interior_blank_wall;
	private final ModelPartExtension right_upper_wall_r2;
	private final ModelPartExtension right_lower_wall_r2;
	private final ModelPartExtension window_interior_blank_light;
	private final ModelPartExtension light_r2;
	private final ModelPartExtension door_left_exterior;
	private final ModelPartExtension right_wall_front_r2;
	private final ModelPartExtension right_top_wall_front_r1;
	private final ModelPartExtension right_top_wall_r4;
	private final ModelPartExtension top_back_r2;
	private final ModelPartExtension door_left_interior;
	private final ModelPartExtension right_wall_front_r3;
	private final ModelPartExtension right_top_wall_front_r2;
	private final ModelPartExtension right_top_wall_r5;
	private final ModelPartExtension top_back_r3;
	private final ModelPartExtension door_right_exterior;
	private final ModelPartExtension right_wall_front_r4;
	private final ModelPartExtension right_top_wall_front_r3;
	private final ModelPartExtension right_top_wall_r6;
	private final ModelPartExtension top_back_r4;
	private final ModelPartExtension door_right_interior;
	private final ModelPartExtension right_wall_front_r5;
	private final ModelPartExtension right_top_wall_front_r4;
	private final ModelPartExtension right_top_wall_r7;
	private final ModelPartExtension top_back_r5;
	private final ModelPartExtension head_interior;
	private final ModelPartExtension right_door_top_r1;
	private final ModelPartExtension left_door_top_r1;
	private final ModelPartExtension roof_front_r1;
	private final ModelPartExtension right_roof_side_r1;
	private final ModelPartExtension left_roof_side_r1;
	private final ModelPartExtension right_upper_wall_r3;
	private final ModelPartExtension left_upper_wall_r1;
	private final ModelPartExtension right_wall_r3;
	private final ModelPartExtension left_wall_r3;
	private final ModelPartExtension right_lower_wall_r3;
	private final ModelPartExtension left_lower_wall_r1;
	private final ModelPartExtension end_interior;
	private final ModelPartExtension right_luggage_rack_top_r1;
	private final ModelPartExtension left_luggage_rack_top_r1;
	private final ModelPartExtension right_luggage_rack_front_r1;
	private final ModelPartExtension left_luggage_rack_front_r1;
	private final ModelPartExtension right_luggage_rack_bottom_front_r1;
	private final ModelPartExtension left_luggage_rack_bottom_front_r1;
	private final ModelPartExtension right_top_wall_r8;
	private final ModelPartExtension left_top_wall_r2;
	private final ModelPartExtension right_roof_side_r2;
	private final ModelPartExtension left_roof_side_r2;
	private final ModelPartExtension right_upper_wall_r4;
	private final ModelPartExtension left_upper_wall_r2;
	private final ModelPartExtension right_wall_r4;
	private final ModelPartExtension left_wall_r4;
	private final ModelPartExtension right_lower_wall_r4;
	private final ModelPartExtension left_lower_wall_r2;
	private final ModelPartExtension end_light;
	private final ModelPartExtension left_light_r1;
	private final ModelPartExtension right_light_r1;
	private final ModelPartExtension emergency_exit;
	private final ModelPartExtension right_upper_wall_r5;
	private final ModelPartExtension right_lower_wall_r5;
	private final ModelPartExtension left_upper_wall_r3;
	private final ModelPartExtension left_lower_wall_r3;
	private final ModelPartExtension seat_nice;
	private final ModelPartExtension back_bottom_right_r2;
	private final ModelPartExtension seat_normal;
	private final ModelPartExtension back_bottom_right_r3;

	public ModelA320() {
		this(DoorAnimationType.PLUG_SLOW, true);
	}

	protected ModelA320(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		super(1024, 1024, doorAnimationType, renderDoorOverlay);

		exterior = createModelPart();
		exterior.setPivot(0, 24, 0);


		main = createModelPart();
		main.setPivot(0, 0, 0);
		exterior.addChild(main);
		main.setTextureUVOffset(0, 0).addCuboid(-9, -33, -191, 18, 0, 367, 0, false);
		main.setTextureUVOffset(43, 0).addCuboid(-9, 33, -251, 18, 0, 360, 0, false);
		main.setTextureUVOffset(0, 20).addCuboid(32, -10, -225, 0, 20, 380, 0, false);
		main.setTextureUVOffset(0, 0).addCuboid(-32, -10, -225, 0, 20, 380, 0, false);

		right_roof_r1 = createModelPart();
		right_roof_r1.setPivot(-9, -33, 0);
		main.addChild(right_roof_r1);
		setRotationAngle(right_roof_r1, 0, 0, -0.5236F);
		right_roof_r1.setTextureUVOffset(161, 0).addCuboid(-17, 0, -191, 17, 0, 346, 0, false);

		left_roof_r1 = createModelPart();
		left_roof_r1.setPivot(9, -33, 0);
		main.addChild(left_roof_r1);
		setRotationAngle(left_roof_r1, 0, 0, 0.5236F);
		left_roof_r1.setTextureUVOffset(195, 0).addCuboid(0, 0, -191, 17, 0, 346, 0, false);

		right_top_wall_r1 = createModelPart();
		right_top_wall_r1.setPivot(-32, -10, 0);
		main.addChild(right_top_wall_r1);
		setRotationAngle(right_top_wall_r1, 0, 0, 0.5236F);
		right_top_wall_r1.setTextureUVOffset(0, 40).addCuboid(0, -17, -225, 0, 17, 380, 0, false);

		left_top_wall_r1 = createModelPart();
		left_top_wall_r1.setPivot(32, -10, 0);
		main.addChild(left_top_wall_r1);
		setRotationAngle(left_top_wall_r1, 0, 0, -0.5236F);
		left_top_wall_r1.setTextureUVOffset(0, 57).addCuboid(0, -17, -225, 0, 17, 380, 0, false);

		right_bottom_wall_r1 = createModelPart();
		right_bottom_wall_r1.setPivot(-32, 10, 0);
		main.addChild(right_bottom_wall_r1);
		setRotationAngle(right_bottom_wall_r1, 0, 0, -0.5236F);
		right_bottom_wall_r1.setTextureUVOffset(0, 111).addCuboid(0, 0, -251, 0, 17, 360, 0, false);

		left_bottom_wall_r1 = createModelPart();
		left_bottom_wall_r1.setPivot(32, 10, 0);
		main.addChild(left_bottom_wall_r1);
		setRotationAngle(left_bottom_wall_r1, 0, 0, 0.5236F);
		left_bottom_wall_r1.setTextureUVOffset(0, 94).addCuboid(0, 0, -251, 0, 17, 360, 0, false);

		right_floor_r1 = createModelPart();
		right_floor_r1.setPivot(-9, 33, 0);
		main.addChild(right_floor_r1);
		setRotationAngle(right_floor_r1, 0, 0, 0.5236F);
		right_floor_r1.setTextureUVOffset(113, 0).addCuboid(-17, 0, -251, 17, 0, 360, 0, false);

		left_floor_r1 = createModelPart();
		left_floor_r1.setPivot(9, 33, 0);
		main.addChild(left_floor_r1);
		setRotationAngle(left_floor_r1, 0, 0, -0.5236F);
		left_floor_r1.setTextureUVOffset(79, 0).addCuboid(0, 0, -251, 17, 0, 360, 0, false);

		front = createModelPart();
		front.setPivot(0, 0, 0);
		exterior.addChild(front);
		front.setTextureUVOffset(52, 110).addCuboid(-3, 8, -301, 6, 6, 0, 0, false);
		front.setTextureUVOffset(124, 897).addCuboid(17, -24, -229, 15, 32, 0, 0, false);
		front.setTextureUVOffset(124, 897).addCuboid(-32, -24, -229, 15, 32, 0, 0, true);
		front.setTextureUVOffset(154, 900).addCuboid(18, -25, -213, 14, 33, 0, 0, false);
		front.setTextureUVOffset(154, 900).addCuboid(-32, -25, -213, 14, 33, 0, 0, true);

		top_right_back_r1 = createModelPart();
		top_right_back_r1.setPivot(-9, -33, -191);
		front.addChild(top_right_back_r1);
		setRotationAngle(top_right_back_r1, 0.0873F, 0, -0.5236F);
		top_right_back_r1.setTextureUVOffset(0, 0).addCuboid(-22, 0, -64, 22, 0, 64, 0, false);

		top_left_back_r1 = createModelPart();
		top_left_back_r1.setPivot(9, -33, -191);
		front.addChild(top_left_back_r1);
		setRotationAngle(top_left_back_r1, 0.0873F, 0, 0.5236F);
		top_left_back_r1.setTextureUVOffset(0, 64).addCuboid(0, 0, -64, 22, 0, 64, 0, false);

		bottom_right_r1 = createModelPart();
		bottom_right_r1.setPivot(-9, 33, -251);
		front.addChild(bottom_right_r1);
		setRotationAngle(bottom_right_r1, -0.1396F, 0, 0.5236F);
		bottom_right_r1.setTextureUVOffset(72, 581).addCuboid(-17, 0, -22, 17, 0, 22, 0, false);

		bottom_left_r1 = createModelPart();
		bottom_left_r1.setPivot(9, 33, -251);
		front.addChild(bottom_left_r1);
		setRotationAngle(bottom_left_r1, -0.1396F, 0, -0.5236F);
		bottom_left_r1.setTextureUVOffset(305, 606).addCuboid(0, 0, -22, 17, 0, 22, 0, false);

		bottom_front_right_r1 = createModelPart();
		bottom_front_right_r1.setPivot(-3, 29, -280);
		front.addChild(bottom_front_right_r1);
		setRotationAngle(bottom_front_right_r1, -0.4363F, 0, 0.5236F);
		bottom_front_right_r1.setTextureUVOffset(78, 109).addCuboid(-18, 0, -21, 18, 0, 30, 0, false);

		bottom_front_left_r1 = createModelPart();
		bottom_front_left_r1.setPivot(3, 29, -280);
		front.addChild(bottom_front_left_r1);
		setRotationAngle(bottom_front_left_r1, -0.4363F, 0, -0.5236F);
		bottom_front_left_r1.setTextureUVOffset(201, 109).addCuboid(0, 0, -21, 18, 0, 30, 0, false);

		windscreen_lower_right_r1 = createModelPart();
		windscreen_lower_right_r1.setPivot(-9, -7, -280);
		front.addChild(windscreen_lower_right_r1);
		setRotationAngle(windscreen_lower_right_r1, -0.6109F, 1.0472F, 0);
		windscreen_lower_right_r1.setTextureUVOffset(758, 593).addCuboid(-39, 0, 0, 56, 10, 0, 0, false);

		windscreen_lower_left_r1 = createModelPart();
		windscreen_lower_left_r1.setPivot(9, -7, -280);
		front.addChild(windscreen_lower_left_r1);
		setRotationAngle(windscreen_lower_left_r1, -0.6109F, -1.0472F, 0);
		windscreen_lower_left_r1.setTextureUVOffset(760, 375).addCuboid(-17, 0, 0, 56, 10, 0, 0, false);

		windscreen_right_r1 = createModelPart();
		windscreen_right_r1.setPivot(-9, -7, -280);
		front.addChild(windscreen_right_r1);
		setRotationAngle(windscreen_right_r1, -0.5236F, 1.0472F, 0);
		windscreen_right_r1.setTextureUVOffset(68, 766).addCuboid(-42, -21, 0, 42, 21, 0, 0, false);

		windscreen_left_r1 = createModelPart();
		windscreen_left_r1.setPivot(9, -7, -280);
		front.addChild(windscreen_left_r1);
		setRotationAngle(windscreen_left_r1, -0.5236F, -1.0472F, 0);
		windscreen_left_r1.setTextureUVOffset(767, 754).addCuboid(0, -21, 0, 42, 21, 0, 0, false);

		right_wall_front_r1 = createModelPart();
		right_wall_front_r1.setPivot(-3, -1, -301);
		front.addChild(right_wall_front_r1);
		setRotationAngle(right_wall_front_r1, 0, -0.5236F, 0);
		right_wall_front_r1.setTextureUVOffset(0, 511).addCuboid(0, -2, 0, 0, 27, 45, 0, false);

		left_wall_front_r1 = createModelPart();
		left_wall_front_r1.setPivot(3, -1, -301);
		front.addChild(left_wall_front_r1);
		setRotationAngle(left_wall_front_r1, 0, 0.5236F, 0);
		left_wall_front_r1.setTextureUVOffset(0, 538).addCuboid(0, -2, 0, 0, 27, 45, 0, false);

		right_wall_top_r1 = createModelPart();
		right_wall_top_r1.setPivot(-32, -10, -225);
		front.addChild(right_wall_top_r1);
		setRotationAngle(right_wall_top_r1, 0, -0.1745F, 0.5236F);
		right_wall_top_r1.setTextureUVOffset(577, 714).addCuboid(0, -13, -19, 0, 13, 19, 0, false);

		left_wall_top_r1 = createModelPart();
		left_wall_top_r1.setPivot(32, -10, -225);
		front.addChild(left_wall_top_r1);
		setRotationAngle(left_wall_top_r1, 0, 0.1745F, -0.5236F);
		left_wall_top_r1.setTextureUVOffset(577, 727).addCuboid(0, -13, -19, 0, 13, 19, 0, false);

		right_wall_lower_r1 = createModelPart();
		right_wall_lower_r1.setPivot(-32, 10, -251);
		front.addChild(right_wall_lower_r1);
		setRotationAngle(right_wall_lower_r1, 0, -0.1745F, -0.5236F);
		right_wall_lower_r1.setTextureUVOffset(220, 680).addCuboid(0, 9, -22, 0, 9, 22, 0, false);

		left_wall_lower_r1 = createModelPart();
		left_wall_lower_r1.setPivot(32, 10, -251);
		front.addChild(left_wall_lower_r1);
		setRotationAngle(left_wall_lower_r1, 0, 0.1745F, 0.5236F);
		left_wall_lower_r1.setTextureUVOffset(627, 721).addCuboid(0, 9, -22, 0, 9, 22, 0, false);

		right_wall_r1 = createModelPart();
		right_wall_r1.setPivot(-32, 0, -225);
		front.addChild(right_wall_r1);
		setRotationAngle(right_wall_r1, 0, -0.1745F, 0);
		right_wall_r1.setTextureUVOffset(0, 137).addCuboid(0, -10, -38, 0, 28, 38, 0, false);

		left_wall_r1 = createModelPart();
		left_wall_r1.setPivot(32, 0, -225);
		front.addChild(left_wall_r1);
		setRotationAngle(left_wall_r1, 0, 0.1745F, 0);
		left_wall_r1.setTextureUVOffset(0, 165).addCuboid(0, -10, -38, 0, 28, 38, 0, false);

		top_back_r1 = createModelPart();
		top_back_r1.setPivot(0, -33, -191);
		front.addChild(top_back_r1);
		setRotationAngle(top_back_r1, 0.0873F, 0, 0);
		top_back_r1.setTextureUVOffset(128, 685).addCuboid(-9, 0, -56, 18, 0, 56, 0, false);

		top_front_r1 = createModelPart();
		top_front_r1.setPivot(0, -24.5002F, -254.5527F);
		front.addChild(top_front_r1);
		setRotationAngle(top_front_r1, 0.3491F, 0, 0);
		top_front_r1.setTextureUVOffset(556, 109).addCuboid(-12, -0.5F, -9.5F, 24, 0, 19, 0, false);

		bottom_r1 = createModelPart();
		bottom_r1.setPivot(0, 33, -251);
		front.addChild(bottom_r1);
		setRotationAngle(bottom_r1, -0.1396F, 0, 0);
		bottom_r1.setTextureUVOffset(545, 79).addCuboid(-9, 0, -30, 18, 0, 30, 0, false);

		bottom_front_r1 = createModelPart();
		bottom_front_r1.setPivot(0, 29, -280);
		front.addChild(bottom_front_r1);
		setRotationAngle(bottom_front_r1, -0.4363F, 0, 0);
		bottom_front_r1.setTextureUVOffset(114, 86).addCuboid(-3, 0, -19, 6, 0, 19, 0, false);

		windscreen_main_r1 = createModelPart();
		windscreen_main_r1.setPivot(0, -7, -280);
		front.addChild(windscreen_main_r1);
		setRotationAngle(windscreen_main_r1, 0.733F, 0, 0);
		windscreen_main_r1.setTextureUVOffset(304, 317).addCuboid(-9, 0, 0, 18, 0, 22, 0, false);

		windscreen_lower_r1 = createModelPart();
		windscreen_lower_r1.setPivot(0, -7, -280);
		front.addChild(windscreen_lower_r1);
		setRotationAngle(windscreen_lower_r1, 0.4363F, 0, 0);
		windscreen_lower_r1.setTextureUVOffset(558, 714).addCuboid(-9, 0, -19, 18, 0, 19, 0, false);

		front_lower_r1 = createModelPart();
		front_lower_r1.setPivot(0, 14, -301);
		front.addChild(front_lower_r1);
		setRotationAngle(front_lower_r1, -1.0472F, 0, 0);
		front_lower_r1.setTextureUVOffset(127, 307).addCuboid(-5, 0, 0, 10, 0, 9, 0, false);

		front_upper_r1 = createModelPart();
		front_upper_r1.setPivot(0, 8, -301);
		front.addChild(front_upper_r1);
		setRotationAngle(front_upper_r1, 1.0472F, 0, 0);
		front_upper_r1.setTextureUVOffset(327, 258).addCuboid(-6, 0, 0, 12, 0, 9, 0, false);

		tail = createModelPart();
		tail.setPivot(0, 0, 0);
		exterior.addChild(tail);
		tail.setTextureUVOffset(100, 256).addCuboid(-3, -22, 301, 6, 11, 0, 0, false);

		top_r1 = createModelPart();
		top_r1.setPivot(0, -22, 301);
		tail.addChild(top_r1);
		setRotationAngle(top_r1, -0.0873F, 0, 0);
		top_r1.setTextureUVOffset(155, 109).addCuboid(-6, 0, -127, 12, 0, 127, 0, false);

		bottom_back_r1 = createModelPart();
		bottom_back_r1.setPivot(0, -11, 301);
		tail.addChild(bottom_back_r1);
		setRotationAngle(bottom_back_r1, 0.2618F, 0, 0);
		bottom_back_r1.setTextureUVOffset(0, 175).addCuboid(-11, 0, -115, 22, 0, 115, 0, false);

		bottom_r2 = createModelPart();
		bottom_r2.setPivot(0, 33, 109);
		tail.addChild(bottom_r2);
		setRotationAngle(bottom_r2, 0.1745F, 0, 0);
		bottom_r2.setTextureUVOffset(223, 109).addCuboid(-11, 0, 0, 22, 0, 83, 0, false);

		bottom_right_r2 = createModelPart();
		bottom_right_r2.setPivot(-9, 33, 109);
		tail.addChild(bottom_right_r2);
		setRotationAngle(bottom_right_r2, 0.1396F, 0, 0.5236F);
		bottom_right_r2.setTextureUVOffset(497, 165).addCuboid(-17, 0, 0, 17, 0, 78, 0, false);

		bottom_left_r2 = createModelPart();
		bottom_left_r2.setPivot(9, 33, 109);
		tail.addChild(bottom_left_r2);
		setRotationAngle(bottom_left_r2, 0.1396F, 0, -0.5236F);
		bottom_left_r2.setTextureUVOffset(531, 165).addCuboid(0, 0, 0, 17, 0, 78, 0, false);

		side_right_r1 = createModelPart();
		side_right_r1.setPivot(-32, 10, 109);
		tail.addChild(side_right_r1);
		setRotationAngle(side_right_r1, 0, 0.1745F, -0.5236F);
		side_right_r1.setTextureUVOffset(0, 297).addCuboid(0, -15, 0, 0, 34, 191, 0, false);

		side_left_r1 = createModelPart();
		side_left_r1.setPivot(32, 10, 109);
		tail.addChild(side_left_r1);
		setRotationAngle(side_left_r1, 0, -0.1745F, 0.5236F);
		side_left_r1.setTextureUVOffset(0, 331).addCuboid(0, -15, 0, 0, 34, 191, 0, false);

		right_roof_top_r1 = createModelPart();
		right_roof_top_r1.setPivot(-9, -33, 155);
		tail.addChild(right_roof_top_r1);
		setRotationAngle(right_roof_top_r1, -0.0873F, 0, -0.5236F);
		right_roof_top_r1.setTextureUVOffset(0, 0).addCuboid(-17, 0, 0, 21, 0, 147, 0, false);

		left_roof_top_r1 = createModelPart();
		left_roof_top_r1.setPivot(9, -33, 155);
		tail.addChild(left_roof_top_r1);
		setRotationAngle(left_roof_top_r1, -0.0873F, 0, 0.5236F);
		left_roof_top_r1.setTextureUVOffset(42, 0).addCuboid(-4, 0, 0, 21, 0, 147, 0, false);

		right_wall_top_r2 = createModelPart();
		right_wall_top_r2.setPivot(-32, -10, 155);
		tail.addChild(right_wall_top_r2);
		setRotationAngle(right_wall_top_r2, 0, 0.0873F, 0.5236F);
		right_wall_top_r2.setTextureUVOffset(0, 214).addCuboid(0, -17, 0, 0, 17, 76, 0, false);

		left_wall_top_r2 = createModelPart();
		left_wall_top_r2.setPivot(32, -10, 155);
		tail.addChild(left_wall_top_r2);
		setRotationAngle(left_wall_top_r2, 0, -0.0873F, -0.5236F);
		left_wall_top_r2.setTextureUVOffset(184, 592).addCuboid(0, -17, 0, 0, 17, 76, 0, false);

		right_wall_back_r1 = createModelPart();
		right_wall_back_r1.setPivot(-3, 0, 301);
		tail.addChild(right_wall_back_r1);
		setRotationAngle(right_wall_back_r1, 0, 0.5236F, 0);
		right_wall_back_r1.setTextureUVOffset(342, 329).addCuboid(0, -22, -10, 0, 13, 10, 0, false);

		left_wall_back_r1 = createModelPart();
		left_wall_back_r1.setPivot(3, 0, 301);
		tail.addChild(left_wall_back_r1);
		setRotationAngle(left_wall_back_r1, 0, -0.5236F, 0);
		left_wall_back_r1.setTextureUVOffset(865, 205).addCuboid(0, -22, -10, 0, 13, 10, 0, false);

		right_wall_r2 = createModelPart();
		right_wall_r2.setPivot(-32, 0, 155);
		tail.addChild(right_wall_r2);
		setRotationAngle(right_wall_r2, 0, 0.1745F, 0);
		right_wall_r2.setTextureUVOffset(0, 6).addCuboid(0, -20, 0, 0, 14, 141, 0, false);

		left_wall_r2 = createModelPart();
		left_wall_r2.setPivot(32, 0, 155);
		tail.addChild(left_wall_r2);
		setRotationAngle(left_wall_r2, 0, -0.1745F, 0);
		left_wall_r2.setTextureUVOffset(0, 20).addCuboid(0, -20, 0, 0, 14, 141, 0, false);

		left_wing = createModelPart();
		left_wing.setPivot(0, 0, 0);
		exterior.addChild(left_wing);
		left_wing.setTextureUVOffset(410, 678).addCuboid(271, -58, 31, 0, 24, 36, 0, false);
		left_wing.setTextureUVOffset(633, 725).addCuboid(76, 8, -42, 5, 7, 56, 0, false);
		left_wing.setTextureUVOffset(511, 721).addCuboid(131, 3, -32, 5, 7, 56, 0, false);
		left_wing.setTextureUVOffset(758, 502).addCuboid(189, -3, -6, 5, 7, 50, 0, false);

		edge_wing_bottom_r1 = createModelPart();
		edge_wing_bottom_r1.setPivot(255, -18, 39);
		left_wing.addChild(edge_wing_bottom_r1);
		setRotationAngle(edge_wing_bottom_r1, 0, 0, 1.0472F);
		edge_wing_bottom_r1.setTextureUVOffset(0, 574).addCuboid(0, -12, -24, 0, 24, 36, 0, false);

		edge_wing_middle_r1 = createModelPart();
		edge_wing_middle_r1.setPivot(271, -34, 39);
		left_wing.addChild(edge_wing_middle_r1);
		setRotationAngle(edge_wing_middle_r1, 0, 0, 0.5236F);
		edge_wing_middle_r1.setTextureUVOffset(0, 598).addCuboid(0, 0, -18, 0, 12, 36, 0, false);

		outer_bottom_cover_r1 = createModelPart();
		outer_bottom_cover_r1.setPivot(255, -14, 35);
		left_wing.addChild(outer_bottom_cover_r1);
		setRotationAngle(outer_bottom_cover_r1, 0, -0.4712F, -0.1745F);
		outer_bottom_cover_r1.setTextureUVOffset(557, 147).addCuboid(-135, 0, 0, 125, 0, 18, 0, false);

		outer_top_cover_r1 = createModelPart();
		outer_top_cover_r1.setPivot(255, -18, 35);
		left_wing.addChild(outer_top_cover_r1);
		setRotationAngle(outer_top_cover_r1, 0, -0.4712F, -0.1745F);
		outer_top_cover_r1.setTextureUVOffset(685, 644).addCuboid(-72, 0, 0, 79, 0, 13, 0, false);

		middle_top_cover_r1 = createModelPart();
		middle_top_cover_r1.setPivot(32, 7, -80);
		left_wing.addChild(middle_top_cover_r1);
		setRotationAngle(middle_top_cover_r1, 0, -0.4712F, -0.0873F);
		middle_top_cover_r1.setTextureUVOffset(511, 0).addCuboid(0, 0, 0, 189, 0, 64, 0, false);

		back_flat_bottom_edge_r1 = createModelPart();
		back_flat_bottom_edge_r1.setPivot(32, 13, -9);
		left_wing.addChild(back_flat_bottom_edge_r1);
		setRotationAngle(back_flat_bottom_edge_r1, 0.2618F, 0, -0.0873F);
		back_flat_bottom_edge_r1.setTextureUVOffset(575, 79).addCuboid(-9, -1, -37, 85, 1, 49, 0, false);

		back_flat_top_edge_r1 = createModelPart();
		back_flat_top_edge_r1.setPivot(32, 7, -9);
		left_wing.addChild(back_flat_top_edge_r1);
		setRotationAngle(back_flat_top_edge_r1, -0.0873F, 0, -0.0873F);
		back_flat_top_edge_r1.setTextureUVOffset(720, 474).addCuboid(-1, 0, 0, 74, 1, 12, 0, false);

		back_outer_bottom_edge_r1 = createModelPart();
		back_outer_bottom_edge_r1.setPivot(255, -14, 35);
		left_wing.addChild(back_outer_bottom_edge_r1);
		setRotationAngle(back_outer_bottom_edge_r1, 0.0873F, -0.2793F, -0.1745F);
		back_outer_bottom_edge_r1.setTextureUVOffset(652, 697).addCuboid(-88, -1, 0, 85, 1, 12, 0, false);

		back_outer_top_edge_r1 = createModelPart();
		back_outer_top_edge_r1.setPivot(255, -18, 35);
		left_wing.addChild(back_outer_top_edge_r1);
		setRotationAngle(back_outer_top_edge_r1, -0.1745F, -0.2793F, -0.1745F);
		back_outer_top_edge_r1.setTextureUVOffset(725, 281).addCuboid(-66, 0, 0, 66, 1, 12, 0, false);

		back_middle_bottom_edge_r1 = createModelPart();
		back_middle_bottom_edge_r1.setPivot(32, 13, -30);
		left_wing.addChild(back_middle_bottom_edge_r1);
		setRotationAngle(back_middle_bottom_edge_r1, 0.2618F, -0.2793F, -0.0873F);
		back_middle_bottom_edge_r1.setTextureUVOffset(94, 606).addCuboid(73, -1, -17, 102, 1, 29, 0, false);

		back_middle_top_edge_r1 = createModelPart();
		back_middle_top_edge_r1.setPivot(32, 7, -30);
		left_wing.addChild(back_middle_top_edge_r1);
		setRotationAngle(back_middle_top_edge_r1, -0.0873F, -0.2793F, -0.0873F);
		back_middle_top_edge_r1.setTextureUVOffset(507, 347).addCuboid(-10, 0, 0, 194, 1, 12, 0, false);

		front_outer_top_edge_r1 = createModelPart();
		front_outer_top_edge_r1.setPivot(255, -14, 35);
		left_wing.addChild(front_outer_top_edge_r1);
		setRotationAngle(front_outer_top_edge_r1, -0.0873F, -0.4712F, -0.1745F);
		front_outer_top_edge_r1.setTextureUVOffset(460, 684).addCuboid(-98, -1, -14, 88, 1, 14, 0, false);

		front_outer_top_edge_r2 = createModelPart();
		front_outer_top_edge_r2.setPivot(255, -18, 35);
		left_wing.addChild(front_outer_top_edge_r2);
		setRotationAngle(front_outer_top_edge_r2, 0.0873F, -0.4712F, -0.1745F);
		front_outer_top_edge_r2.setTextureUVOffset(614, 710).addCuboid(-77, 0, -14, 77, 1, 14, 0, false);

		front_middle_bottom_edge_r1 = createModelPart();
		front_middle_bottom_edge_r1.setPivot(32, 13, -80);
		left_wing.addChild(front_middle_bottom_edge_r1);
		setRotationAngle(front_middle_bottom_edge_r1, -0.2618F, -0.4712F, -0.0873F);
		front_middle_bottom_edge_r1.setTextureUVOffset(331, 552).addCuboid(-7, -1, -14, 188, 1, 51, 0, false);

		front_middle_top_edge_r1 = createModelPart();
		front_middle_top_edge_r1.setPivot(32, 7, -80);
		left_wing.addChild(front_middle_top_edge_r1);
		setRotationAngle(front_middle_top_edge_r1, 0.0873F, -0.4712F, -0.0873F);
		front_middle_top_edge_r1.setTextureUVOffset(575, 64).addCuboid(-8, 0, -14, 191, 1, 14, 0, false);

		right_wing = createModelPart();
		right_wing.setPivot(0, 0, 0);
		exterior.addChild(right_wing);
		right_wing.setTextureUVOffset(287, 237).addCuboid(-271, -58, 31, 0, 24, 36, 0, false);
		right_wing.setTextureUVOffset(720, 398).addCuboid(-81, 8, -42, 5, 7, 56, 0, false);
		right_wing.setTextureUVOffset(445, 714).addCuboid(-136, 3, -32, 5, 7, 56, 0, false);
		right_wing.setTextureUVOffset(124, 749).addCuboid(-194, -3, -6, 5, 7, 50, 0, false);

		edge_wing_bottom_r2 = createModelPart();
		edge_wing_bottom_r2.setPivot(-255, -18, 39);
		right_wing.addChild(edge_wing_bottom_r2);
		setRotationAngle(edge_wing_bottom_r2, 0, 0, -1.0472F);
		edge_wing_bottom_r2.setTextureUVOffset(0, 195).addCuboid(0, -12, -24, 0, 24, 36, 0, false);

		edge_wing_middle_r2 = createModelPart();
		edge_wing_middle_r2.setPivot(-271, -34, 39);
		right_wing.addChild(edge_wing_middle_r2);
		setRotationAngle(edge_wing_middle_r2, 0, 0, -0.5236F);
		edge_wing_middle_r2.setTextureUVOffset(287, 268).addCuboid(0, 0, -18, 0, 12, 36, 0, false);

		outer_bottom_cover_r2 = createModelPart();
		outer_bottom_cover_r2.setPivot(-255, -14, 35);
		right_wing.addChild(outer_bottom_cover_r2);
		setRotationAngle(outer_bottom_cover_r2, 0, 0.4712F, 0.1745F);
		outer_bottom_cover_r2.setTextureUVOffset(557, 129).addCuboid(10, 0, 0, 125, 0, 18, 0, false);

		outer_top_cover_r2 = createModelPart();
		outer_top_cover_r2.setPivot(-255, -18, 35);
		right_wing.addChild(outer_top_cover_r2);
		setRotationAngle(outer_top_cover_r2, 0, 0.4712F, 0.1745F);
		outer_top_cover_r2.setTextureUVOffset(685, 631).addCuboid(-7, 0, 0, 79, 0, 13, 0, false);

		middle_top_cover_r2 = createModelPart();
		middle_top_cover_r2.setPivot(-32, 7, -80);
		right_wing.addChild(middle_top_cover_r2);
		setRotationAngle(middle_top_cover_r2, 0, 0.4712F, 0.0873F);
		middle_top_cover_r2.setTextureUVOffset(318, 488).addCuboid(-189, 0, 0, 189, 0, 64, 0, false);

		back_flat_bottom_edge_r2 = createModelPart();
		back_flat_bottom_edge_r2.setPivot(-32, 13, -9);
		right_wing.addChild(back_flat_bottom_edge_r2);
		setRotationAngle(back_flat_bottom_edge_r2, 0.2618F, 0, 0.0873F);
		back_flat_bottom_edge_r2.setTextureUVOffset(94, 556).addCuboid(-76, -1, -37, 85, 1, 49, 0, false);

		back_flat_top_edge_r2 = createModelPart();
		back_flat_top_edge_r2.setPivot(-32, 7, -9);
		right_wing.addChild(back_flat_top_edge_r2);
		setRotationAngle(back_flat_top_edge_r2, -0.0873F, 0, 0.0873F);
		back_flat_top_edge_r2.setTextureUVOffset(720, 461).addCuboid(-73, 0, 0, 74, 1, 12, 0, false);

		back_outer_bottom_edge_r2 = createModelPart();
		back_outer_bottom_edge_r2.setPivot(-255, -14, 35);
		right_wing.addChild(back_outer_bottom_edge_r2);
		setRotationAngle(back_outer_bottom_edge_r2, 0.0873F, 0.2793F, 0.1745F);
		back_outer_bottom_edge_r2.setTextureUVOffset(650, 684).addCuboid(3, -1, 0, 85, 1, 12, 0, false);

		back_outer_top_edge_r2 = createModelPart();
		back_outer_top_edge_r2.setPivot(-255, -18, 35);
		right_wing.addChild(back_outer_top_edge_r2);
		setRotationAngle(back_outer_top_edge_r2, -0.1745F, 0.2793F, 0.1745F);
		back_outer_top_edge_r2.setTextureUVOffset(206, 353).addCuboid(0, 0, 0, 66, 1, 12, 0, false);

		back_middle_bottom_edge_r2 = createModelPart();
		back_middle_bottom_edge_r2.setPivot(-32, 13, -30);
		right_wing.addChild(back_middle_bottom_edge_r2);
		setRotationAngle(back_middle_bottom_edge_r2, 0.2618F, 0.2793F, 0.0873F);
		back_middle_bottom_edge_r2.setTextureUVOffset(374, 604).addCuboid(-175, -1, -17, 102, 1, 29, 0, false);

		back_middle_top_edge_r2 = createModelPart();
		back_middle_top_edge_r2.setPivot(-32, 7, -30);
		right_wing.addChild(back_middle_top_edge_r2);
		setRotationAngle(back_middle_top_edge_r2, -0.0873F, 0.2793F, 0.0873F);
		back_middle_top_edge_r2.setTextureUVOffset(0, 367).addCuboid(-184, 0, 0, 194, 1, 12, 0, false);

		front_outer_top_edge_r3 = createModelPart();
		front_outer_top_edge_r3.setPivot(-255, -14, 35);
		right_wing.addChild(front_outer_top_edge_r3);
		setRotationAngle(front_outer_top_edge_r3, -0.0873F, 0.4712F, 0.1745F);
		front_outer_top_edge_r3.setTextureUVOffset(673, 225).addCuboid(10, -1, -14, 88, 1, 14, 0, false);

		front_outer_top_edge_r4 = createModelPart();
		front_outer_top_edge_r4.setPivot(-255, -18, 35);
		right_wing.addChild(front_outer_top_edge_r4);
		setRotationAngle(front_outer_top_edge_r4, 0.0873F, 0.4712F, 0.1745F);
		front_outer_top_edge_r4.setTextureUVOffset(446, 699).addCuboid(0, 0, -14, 77, 1, 14, 0, false);

		front_middle_bottom_edge_r2 = createModelPart();
		front_middle_bottom_edge_r2.setPivot(-32, 13, -80);
		right_wing.addChild(front_middle_bottom_edge_r2);
		setRotationAngle(front_middle_bottom_edge_r2, -0.2618F, 0.4712F, 0.0873F);
		front_middle_bottom_edge_r2.setTextureUVOffset(524, 295).addCuboid(-181, -1, -14, 188, 1, 51, 0, false);

		front_middle_top_edge_r2 = createModelPart();
		front_middle_top_edge_r2.setPivot(-32, 7, -80);
		right_wing.addChild(front_middle_top_edge_r2);
		setRotationAngle(front_middle_top_edge_r2, 0.0873F, 0.4712F, 0.0873F);
		front_middle_top_edge_r2.setTextureUVOffset(400, 360).addCuboid(-183, 0, -14, 191, 1, 14, 0, false);

		back_top_wing = createModelPart();
		back_top_wing.setPivot(0, 0, 0);
		exterior.addChild(back_top_wing);
		back_top_wing.setTextureUVOffset(0, 556).addCuboid(-1, -128, 194, 2, 103, 90, -0.1F, false);

		back_r1 = createModelPart();
		back_r1.setPivot(0, -128, 285);
		back_top_wing.addChild(back_r1);
		setRotationAngle(back_r1, -0.2269F, 0, 0);
		back_r1.setTextureUVOffset(28, 0).addCuboid(-1, 0, -1, 2, 106, 1, 0, false);

		front_bottom_r1 = createModelPart();
		front_bottom_r1.setPivot(0, -33, 162);
		back_top_wing.addChild(front_bottom_r1);
		setRotationAngle(front_bottom_r1, -1.1345F, 0, 0);
		front_bottom_r1.setTextureUVOffset(0, 749).addCuboid(-1, -30, 0, 2, 30, 16, 0.1F, false);

		front_top_r1 = createModelPart();
		front_top_r1.setPivot(0, -128, 258);
		back_top_wing.addChild(front_top_r1);
		setRotationAngle(front_top_r1, -0.6981F, 0, 0);
		front_top_r1.setTextureUVOffset(0, 0).addCuboid(-1, 0, 0, 2, 113, 12, 0, false);

		back_left_wing = createModelPart();
		back_left_wing.setPivot(0, 0, 0);
		exterior.addChild(back_left_wing);


		back_r2 = createModelPart();
		back_r2.setPivot(0, -15, 264);
		back_left_wing.addChild(back_r2);
		setRotationAngle(back_r2, 0, -0.2269F, -0.0873F);
		back_r2.setTextureUVOffset(673, 165).addCuboid(11, -1, -25, 93, 2, 25, 0, false);

		side_r1 = createModelPart();
		side_r1.setPivot(0, -15, 0);
		back_left_wing.addChild(side_r1);
		setRotationAngle(side_r1, 0, 0, -0.0873F);
		side_r1.setTextureUVOffset(511, 743).addCuboid(99, -1, 264, 2, 2, 24, 0.1F, false);

		front_r1 = createModelPart();
		front_r1.setPivot(0, -15, 204);
		back_left_wing.addChild(front_r1);
		setRotationAngle(front_r1, 0, -0.5411F, -0.0873F);
		front_r1.setTextureUVOffset(374, 657).addCuboid(25, -1, 0, 94, 2, 25, -0.1F, false);

		back_right_wing = createModelPart();
		back_right_wing.setPivot(0, 0, 0);
		exterior.addChild(back_right_wing);


		back_r3 = createModelPart();
		back_r3.setPivot(0, -15, 264);
		back_right_wing.addChild(back_r3);
		setRotationAngle(back_r3, 0, 0.2269F, 0.0873F);
		back_r3.setTextureUVOffset(612, 657).addCuboid(-104, -1, -25, 93, 2, 25, 0, false);

		side_r2 = createModelPart();
		side_r2.setPivot(0, -15, 0);
		back_right_wing.addChild(side_r2);
		setRotationAngle(side_r2, 0, 0, 0.0873F);
		side_r2.setTextureUVOffset(10, 101).addCuboid(-101, -1, 264, 2, 2, 24, 0.1F, false);

		front_r2 = createModelPart();
		front_r2.setPivot(0, -15, 204);
		back_right_wing.addChild(front_r2);
		setRotationAngle(front_r2, 0, 0.5411F, 0.0873F);
		front_r2.setTextureUVOffset(607, 604).addCuboid(-119, -1, 0, 94, 2, 25, -0.1F, false);

		bottom = createModelPart();
		bottom.setPivot(0, 0, 0);
		exterior.addChild(bottom);
		bottom.setTextureUVOffset(122, 0).addCuboid(-24, 36, -107, 48, 0, 109, 0, false);
		bottom.setTextureUVOffset(218, 341).addCuboid(-31, 13, -131, 62, 12, 0, 0, false);
		bottom.setTextureUVOffset(0, 284).addCuboid(-24, 24, 51, 48, 6, 0, 0, false);
		bottom.setTextureUVOffset(0, 226).addCuboid(32, 10, -107, 0, 18, 109, 0, false);
		bottom.setTextureUVOffset(0, 208).addCuboid(-32, 10, -107, 0, 18, 109, 0, false);

		back_top_right_r1 = createModelPart();
		back_top_right_r1.setPivot(-32, 28, 2);
		bottom.addChild(back_top_right_r1);
		setRotationAngle(back_top_right_r1, 0, 0.1745F, 0);
		back_top_right_r1.setTextureUVOffset(0, 78).addCuboid(0, -18, 0, 0, 18, 50, 0, false);

		back_top_left_r1 = createModelPart();
		back_top_left_r1.setPivot(32, 28, 2);
		bottom.addChild(back_top_left_r1);
		setRotationAngle(back_top_left_r1, 0, -0.1745F, 0);
		back_top_left_r1.setTextureUVOffset(0, 206).addCuboid(0, -18, 0, 0, 18, 50, 0, false);

		back_side_right_r1 = createModelPart();
		back_side_right_r1.setPivot(-32, 28, 2);
		bottom.addChild(back_side_right_r1);
		setRotationAngle(back_side_right_r1, 0, 0.1745F, -0.5236F);
		back_side_right_r1.setTextureUVOffset(159, 172).addCuboid(0, 0, 0, 0, 10, 50, 0, false);

		back_side_left_r1 = createModelPart();
		back_side_left_r1.setPivot(32, 28, 2);
		bottom.addChild(back_side_left_r1);
		setRotationAngle(back_side_left_r1, 0, -0.1745F, 0.5236F);
		back_side_left_r1.setTextureUVOffset(0, 224).addCuboid(0, 0, 0, 0, 10, 50, 0, false);

		back_bottom_right_r1 = createModelPart();
		back_bottom_right_r1.setPivot(-24, 36, 2);
		bottom.addChild(back_bottom_right_r1);
		setRotationAngle(back_bottom_right_r1, 0.0873F, 0, 0.5236F);
		back_bottom_right_r1.setTextureUVOffset(22, 80).addCuboid(-6, 0, 0, 6, 0, 30, 0, false);

		back_bottom_left_r1 = createModelPart();
		back_bottom_left_r1.setPivot(24, 36, 2);
		bottom.addChild(back_bottom_left_r1);
		setRotationAngle(back_bottom_left_r1, 0.0873F, 0, -0.5236F);
		back_bottom_left_r1.setTextureUVOffset(103, 0).addCuboid(0, 0, 0, 6, 0, 30, 0, false);

		front_top_right_r1 = createModelPart();
		front_top_right_r1.setPivot(-32, 28, -107);
		bottom.addChild(front_top_right_r1);
		setRotationAngle(front_top_right_r1, 0, -0.0873F, 0);
		front_top_right_r1.setTextureUVOffset(587, 632).addCuboid(0, -18, -25, 0, 18, 25, 0, false);

		front_top_left_r1 = createModelPart();
		front_top_left_r1.setPivot(32, 28, -107);
		bottom.addChild(front_top_left_r1);
		setRotationAngle(front_top_left_r1, 0, 0.0873F, 0);
		front_top_left_r1.setTextureUVOffset(627, 700).addCuboid(0, -18, -25, 0, 18, 25, 0, false);

		front_side_right_r1 = createModelPart();
		front_side_right_r1.setPivot(-32, 28, -107);
		bottom.addChild(front_side_right_r1);
		setRotationAngle(front_side_right_r1, 0, -0.1745F, -0.5236F);
		front_side_right_r1.setTextureUVOffset(673, 215).addCuboid(0, -4, -25, 0, 10, 25, 0, false);

		front_side_left_r1 = createModelPart();
		front_side_left_r1.setPivot(32, 28, -107);
		bottom.addChild(front_side_left_r1);
		setRotationAngle(front_side_left_r1, 0, 0.1745F, 0.5236F);
		front_side_left_r1.setTextureUVOffset(614, 674).addCuboid(0, -4, -25, 0, 10, 25, 0, false);

		front_bottom_right_r1 = createModelPart();
		front_bottom_right_r1.setPivot(-24, 36, -107);
		bottom.addChild(front_bottom_right_r1);
		setRotationAngle(front_bottom_right_r1, -0.2618F, 0, 0.5236F);
		front_bottom_right_r1.setTextureUVOffset(234, 203).addCuboid(-9, 0, -25, 9, 0, 25, 0, false);

		front_bottom_left_r1 = createModelPart();
		front_bottom_left_r1.setPivot(24, 36, -107);
		bottom.addChild(front_bottom_left_r1);
		setRotationAngle(front_bottom_left_r1, -0.2618F, 0, -0.5236F);
		front_bottom_left_r1.setTextureUVOffset(262, 236).addCuboid(0, 0, -25, 9, 0, 25, 0, false);

		side_right_r2 = createModelPart();
		side_right_r2.setPivot(-32, 28, 0);
		bottom.addChild(side_right_r2);
		setRotationAngle(side_right_r2, 0, 0, -0.5236F);
		side_right_r2.setTextureUVOffset(0, 244).addCuboid(0, 0, -107, 0, 6, 109, 0, false);

		side_left_r2 = createModelPart();
		side_left_r2.setPivot(32, 28, 0);
		bottom.addChild(side_left_r2);
		setRotationAngle(side_left_r2, 0, 0, 0.5236F);
		side_left_r2.setTextureUVOffset(0, 250).addCuboid(0, 0, -107, 0, 6, 109, 0, false);

		bottom_right_r3 = createModelPart();
		bottom_right_r3.setPivot(-24, 36, 0);
		bottom.addChild(bottom_right_r3);
		setRotationAngle(bottom_right_r3, 0, 0, 0.5236F);
		bottom_right_r3.setTextureUVOffset(0, 0).addCuboid(-6, 0, -107, 6, 0, 109, 0, false);

		bottom_left_r3 = createModelPart();
		bottom_left_r3.setPivot(24, 36, 0);
		bottom.addChild(bottom_left_r3);
		setRotationAngle(bottom_left_r3, 0, 0, -0.5236F);
		bottom_left_r3.setTextureUVOffset(12, 0).addCuboid(0, 0, -107, 6, 0, 109, 0, false);

		back_side_r1 = createModelPart();
		back_side_r1.setPivot(0, 30, 51);
		bottom.addChild(back_side_r1);
		setRotationAngle(back_side_r1, 0.6981F, 0, 0);
		back_side_r1.setTextureUVOffset(397, 375).addCuboid(-21, 0, -3, 42, 0, 3, 0, false);

		back_bottom_r1 = createModelPart();
		back_bottom_r1.setPivot(0, 36, 2);
		bottom.addChild(back_bottom_r1);
		setRotationAngle(back_bottom_r1, 0.0873F, 0, 0);
		back_bottom_r1.setTextureUVOffset(112, 175).addCuboid(-24, 0, 0, 48, 0, 47, 0, false);

		front_side_r1 = createModelPart();
		front_side_r1.setPivot(31, 25, -131);
		bottom.addChild(front_side_r1);
		setRotationAngle(front_side_r1, 0.5236F, 0, 0);
		front_side_r1.setTextureUVOffset(159, 232).addCuboid(-60, 0, 0, 58, 4, 0, 0, false);

		front_bottom_r2 = createModelPart();
		front_bottom_r2.setPivot(24, 36, -107);
		bottom.addChild(front_bottom_r2);
		setRotationAngle(front_bottom_r2, -0.3491F, 0, 0);
		front_bottom_r2.setTextureUVOffset(194, 317).addCuboid(-51, 0, -24, 54, 0, 24, 0, false);

		left_engine = createModelPart();
		left_engine.setPivot(0, 0, 0);
		exterior.addChild(left_engine);
		left_engine.setTextureUVOffset(315, 712).addCuboid(77, 17, -99, 30, 30, 35, 0, false);
		left_engine.setTextureUVOffset(276, 604).addCuboid(89, 6, -106, 6, 22, 86, 0, false);
		left_engine.setTextureUVOffset(618, 823).addCuboid(87, 47, -99, 10, 3, 16, 0, false);
		left_engine.setTextureUVOffset(861, 868).addCuboid(107, 27, -99, 3, 10, 16, 0, false);
		left_engine.setTextureUVOffset(248, 874).addCuboid(74, 27, -99, 3, 10, 16, 0, false);
		left_engine.setTextureUVOffset(682, 823).addCuboid(87, 14, -99, 10, 3, 16, 0, false);

		tail_right_roof_r1 = createModelPart();
		tail_right_roof_r1.setPivot(89, 22, -64);
		left_engine.addChild(tail_right_roof_r1);
		setRotationAngle(tail_right_roof_r1, -0.2094F, 0, -0.5236F);
		tail_right_roof_r1.setTextureUVOffset(636, 791).addCuboid(-6, 0, 0, 6, 3, 29, 0, false);

		tail_left_roof_r1 = createModelPart();
		tail_left_roof_r1.setPivot(95, 22, -64);
		left_engine.addChild(tail_left_roof_r1);
		setRotationAngle(tail_left_roof_r1, -0.2094F, 0, 0.5236F);
		tail_left_roof_r1.setTextureUVOffset(0, 808).addCuboid(0, 0, 0, 6, 3, 29, 0, false);

		tail_roof_r1 = createModelPart();
		tail_roof_r1.setPivot(0, 22, -64);
		left_engine.addChild(tail_roof_r1);
		setRotationAngle(tail_roof_r1, -0.2094F, 0, 0);
		tail_roof_r1.setTextureUVOffset(95, 806).addCuboid(89, 0, 0, 6, 3, 29, 0, false);

		tail_right_wall_bottom_r1 = createModelPart();
		tail_right_wall_bottom_r1.setPivot(82, 35, -64);
		left_engine.addChild(tail_right_wall_bottom_r1);
		setRotationAngle(tail_right_wall_bottom_r1, 0, 0.2094F, -0.5236F);
		tail_right_wall_bottom_r1.setTextureUVOffset(711, 823).addCuboid(0, 0, 0, 3, 6, 29, 0, false);

		tail_right_wall_top_r1 = createModelPart();
		tail_right_wall_top_r1.setPivot(82, 29, -64);
		left_engine.addChild(tail_right_wall_top_r1);
		setRotationAngle(tail_right_wall_top_r1, 0, 0.2094F, 0.5236F);
		tail_right_wall_top_r1.setTextureUVOffset(647, 823).addCuboid(0, -6, 0, 3, 6, 29, 0, false);

		tail_right_wall_r1 = createModelPart();
		tail_right_wall_r1.setPivot(82, 0, -64);
		left_engine.addChild(tail_right_wall_r1);
		setRotationAngle(tail_right_wall_r1, 0, 0.2094F, 0);
		tail_right_wall_r1.setTextureUVOffset(395, 822).addCuboid(0, 29, 0, 3, 6, 29, 0, false);

		tail_left_wall_bottom_r1 = createModelPart();
		tail_left_wall_bottom_r1.setPivot(102, 35, -64);
		left_engine.addChild(tail_left_wall_bottom_r1);
		setRotationAngle(tail_left_wall_bottom_r1, 0, -0.2094F, 0.5236F);
		tail_left_wall_bottom_r1.setTextureUVOffset(583, 820).addCuboid(-3, 0, 0, 3, 6, 29, 0, false);

		tail_left_wall_top_r1 = createModelPart();
		tail_left_wall_top_r1.setPivot(102, 29, -64);
		left_engine.addChild(tail_left_wall_top_r1);
		setRotationAngle(tail_left_wall_top_r1, 0, -0.2094F, -0.5236F);
		tail_left_wall_top_r1.setTextureUVOffset(818, 487).addCuboid(-3, -6, 0, 3, 6, 29, 0, false);

		tail_left_wall_r1 = createModelPart();
		tail_left_wall_r1.setPivot(102, 0, -64);
		left_engine.addChild(tail_left_wall_r1);
		setRotationAngle(tail_left_wall_r1, 0, -0.2094F, 0);
		tail_left_wall_r1.setTextureUVOffset(817, 810).addCuboid(-3, 29, 0, 3, 6, 29, 0, false);

		tail_right_floor_r1 = createModelPart();
		tail_right_floor_r1.setPivot(89, 42, -64);
		left_engine.addChild(tail_right_floor_r1);
		setRotationAngle(tail_right_floor_r1, 0.2094F, 0, 0.5236F);
		tail_right_floor_r1.setTextureUVOffset(794, 79).addCuboid(-6, -3, 0, 6, 3, 29, 0, false);

		tail_left_floor_r1 = createModelPart();
		tail_left_floor_r1.setPivot(95, 42, -64);
		left_engine.addChild(tail_left_floor_r1);
		setRotationAngle(tail_left_floor_r1, 0.2094F, 0, -0.5236F);
		tail_left_floor_r1.setTextureUVOffset(776, 791).addCuboid(0, -3, 0, 6, 3, 29, 0, false);

		tail_floor_r1 = createModelPart();
		tail_floor_r1.setPivot(0, 42, -64);
		left_engine.addChild(tail_floor_r1);
		setRotationAngle(tail_floor_r1, 0.2094F, 0, 0);
		tail_floor_r1.setTextureUVOffset(706, 791).addCuboid(89, -3, 0, 6, 3, 29, 0, false);

		back_right_roof_r1 = createModelPart();
		back_right_roof_r1.setPivot(87, 14, -83);
		left_engine.addChild(back_right_roof_r1);
		setRotationAngle(back_right_roof_r1, -0.1745F, 0, -0.5236F);
		back_right_roof_r1.setTextureUVOffset(835, 79).addCuboid(-10, 0, 0, 10, 3, 20, 0, false);

		back_left_roof_r1 = createModelPart();
		back_left_roof_r1.setPivot(97, 14, -83);
		left_engine.addChild(back_left_roof_r1);
		setRotationAngle(back_left_roof_r1, -0.1745F, 0, 0.5236F);
		back_left_roof_r1.setTextureUVOffset(786, 845).addCuboid(0, 0, 0, 10, 3, 20, 0, false);

		back_roof_r1 = createModelPart();
		back_roof_r1.setPivot(0, 14, -83);
		left_engine.addChild(back_roof_r1);
		setRotationAngle(back_roof_r1, -0.1745F, 0, 0);
		back_roof_r1.setTextureUVOffset(125, 844).addCuboid(87, 0, 0, 10, 3, 20, 0, false);

		back_right_wall_bottom_r1 = createModelPart();
		back_right_wall_bottom_r1.setPivot(74, 37, -83);
		left_engine.addChild(back_right_wall_bottom_r1);
		setRotationAngle(back_right_wall_bottom_r1, 0, 0.1745F, -0.5236F);
		back_right_wall_bottom_r1.setTextureUVOffset(861, 790).addCuboid(0, 0, 0, 3, 10, 20, 0, false);

		back_right_wall_top_r1 = createModelPart();
		back_right_wall_top_r1.setPivot(74, 27, -83);
		left_engine.addChild(back_right_wall_top_r1);
		setRotationAngle(back_right_wall_top_r1, 0, 0.1745F, 0.5236F);
		back_right_wall_top_r1.setTextureUVOffset(80, 861).addCuboid(0, -10, 0, 3, 10, 20, 0, false);

		back_right_wall_r1 = createModelPart();
		back_right_wall_r1.setPivot(74, 0, -83);
		left_engine.addChild(back_right_wall_r1);
		setRotationAngle(back_right_wall_r1, 0, 0.1745F, 0);
		back_right_wall_r1.setTextureUVOffset(860, 583).addCuboid(0, 27, 0, 3, 10, 20, 0, false);

		back_left_wall_bottom_r1 = createModelPart();
		back_left_wall_bottom_r1.setPivot(110, 37, -83);
		left_engine.addChild(back_left_wall_bottom_r1);
		setRotationAngle(back_left_wall_bottom_r1, 0, -0.1745F, 0.5236F);
		back_left_wall_bottom_r1.setTextureUVOffset(331, 860).addCuboid(-3, 0, 0, 3, 10, 20, 0, false);

		back_left_wall_top_r1 = createModelPart();
		back_left_wall_top_r1.setPivot(110, 27, -83);
		left_engine.addChild(back_left_wall_top_r1);
		setRotationAngle(back_left_wall_top_r1, 0, -0.1745F, -0.5236F);
		back_left_wall_top_r1.setTextureUVOffset(859, 719).addCuboid(-3, -10, 0, 3, 10, 20, 0, false);

		back_left_wall_r1 = createModelPart();
		back_left_wall_r1.setPivot(110, 0, -83);
		left_engine.addChild(back_left_wall_r1);
		setRotationAngle(back_left_wall_r1, 0, -0.1745F, 0);
		back_left_wall_r1.setTextureUVOffset(723, 858).addCuboid(-3, 27, 0, 3, 10, 20, 0, false);

		back_right_floor_r1 = createModelPart();
		back_right_floor_r1.setPivot(87, 50, -83);
		left_engine.addChild(back_right_floor_r1);
		setRotationAngle(back_right_floor_r1, 0.1745F, 0, 0.5236F);
		back_right_floor_r1.setTextureUVOffset(0, 840).addCuboid(-10, -3, 0, 10, 3, 20, 0, false);

		back_left_floor_r1 = createModelPart();
		back_left_floor_r1.setPivot(97, 50, -83);
		left_engine.addChild(back_left_floor_r1);
		setRotationAngle(back_left_floor_r1, 0.1745F, 0, -0.5236F);
		back_left_floor_r1.setTextureUVOffset(85, 838).addCuboid(0, -3, 0, 10, 3, 20, 0, false);

		back_floor_r1 = createModelPart();
		back_floor_r1.setPivot(0, 50, -83);
		left_engine.addChild(back_floor_r1);
		setRotationAngle(back_floor_r1, 0.1745F, 0, 0);
		back_floor_r1.setTextureUVOffset(340, 837).addCuboid(87, -3, 0, 10, 3, 20, 0, false);

		front_right_roof_r1 = createModelPart();
		front_right_roof_r1.setPivot(87, 14, -99);
		left_engine.addChild(front_right_roof_r1);
		setRotationAngle(front_right_roof_r1, 0.1047F, 0, -0.5236F);
		front_right_roof_r1.setTextureUVOffset(499, 851).addCuboid(-10, 0, -20, 10, 3, 20, 0, false);

		front_left_roof_r1 = createModelPart();
		front_left_roof_r1.setPivot(97, 14, -99);
		left_engine.addChild(front_left_roof_r1);
		setRotationAngle(front_left_roof_r1, 0.1047F, 0, 0.5236F);
		front_left_roof_r1.setTextureUVOffset(439, 851).addCuboid(0, 0, -20, 10, 3, 20, 0, false);

		front_roof_r1 = createModelPart();
		front_roof_r1.setPivot(0, 14, -99);
		left_engine.addChild(front_roof_r1);
		setRotationAngle(front_roof_r1, 0.1047F, 0, 0);
		front_roof_r1.setTextureUVOffset(225, 851).addCuboid(87, 0, -20, 10, 3, 20, 0, false);

		front_right_wall_bottom_r1 = createModelPart();
		front_right_wall_bottom_r1.setPivot(74, 37, -99);
		left_engine.addChild(front_right_wall_bottom_r1);
		setRotationAngle(front_right_wall_bottom_r1, 0, -0.1047F, -0.5236F);
		front_right_wall_bottom_r1.setTextureUVOffset(867, 365).addCuboid(0, 0, -20, 3, 10, 20, 0, false);

		front_right_wall_top_r1 = createModelPart();
		front_right_wall_top_r1.setPivot(74, 27, -99);
		left_engine.addChild(front_right_wall_top_r1);
		setRotationAngle(front_right_wall_top_r1, 0, -0.1047F, 0.5236F);
		front_right_wall_top_r1.setTextureUVOffset(126, 867).addCuboid(0, -10, -20, 3, 10, 20, 0, false);

		front_right_wall_r1 = createModelPart();
		front_right_wall_r1.setPivot(74, 0, -99);
		left_engine.addChild(front_right_wall_r1);
		setRotationAngle(front_right_wall_r1, 0, -0.1047F, 0);
		front_right_wall_r1.setTextureUVOffset(873, 208).addCuboid(0, 27, -20, 3, 10, 20, 0, false);

		front_left_wall_bottom_r1 = createModelPart();
		front_left_wall_bottom_r1.setPivot(110, 37, -99);
		left_engine.addChild(front_left_wall_bottom_r1);
		setRotationAngle(front_left_wall_bottom_r1, 0, 0.1047F, 0.5236F);
		front_left_wall_bottom_r1.setTextureUVOffset(865, 114).addCuboid(-3, 0, -20, 3, 10, 20, 0, false);

		front_left_wall_top_r1 = createModelPart();
		front_left_wall_top_r1.setPivot(110, 27, -99);
		left_engine.addChild(front_left_wall_top_r1);
		setRotationAngle(front_left_wall_top_r1, 0, 0.1047F, -0.5236F);
		front_left_wall_top_r1.setTextureUVOffset(0, 863).addCuboid(-3, -10, -20, 3, 10, 20, 0, false);

		front_left_wall_r1 = createModelPart();
		front_left_wall_r1.setPivot(110, 0, -99);
		left_engine.addChild(front_left_wall_r1);
		setRotationAngle(front_left_wall_r1, 0, 0.1047F, 0);
		front_left_wall_r1.setTextureUVOffset(862, 502).addCuboid(-3, 27, -20, 3, 10, 20, 0, false);

		front_right_floor_r1 = createModelPart();
		front_right_floor_r1.setPivot(87, 50, -99);
		left_engine.addChild(front_right_floor_r1);
		setRotationAngle(front_right_floor_r1, -0.1047F, 0, 0.5236F);
		front_right_floor_r1.setTextureUVOffset(165, 851).addCuboid(-10, -3, -20, 10, 3, 20, 0, false);

		front_left_floor_r1 = createModelPart();
		front_left_floor_r1.setPivot(97, 50, -99);
		left_engine.addChild(front_left_floor_r1);
		setRotationAngle(front_left_floor_r1, -0.1047F, 0, -0.5236F);
		front_left_floor_r1.setTextureUVOffset(846, 845).addCuboid(0, -3, -20, 10, 3, 20, 0, false);

		front_floor_r1 = createModelPart();
		front_floor_r1.setPivot(0, 50, -99);
		left_engine.addChild(front_floor_r1);
		setRotationAngle(front_floor_r1, -0.1047F, 0, 0);
		front_floor_r1.setTextureUVOffset(40, 846).addCuboid(87, -3, -20, 10, 3, 20, 0, false);

		right_roof_r2 = createModelPart();
		right_roof_r2.setPivot(87, 14, 0);
		left_engine.addChild(right_roof_r2);
		setRotationAngle(right_roof_r2, 0, 0, -0.5236F);
		right_roof_r2.setTextureUVOffset(852, 820).addCuboid(-10, 0, -99, 10, 3, 16, 0, false);

		left_roof_r2 = createModelPart();
		left_roof_r2.setPivot(97, 14, 0);
		left_engine.addChild(left_roof_r2);
		setRotationAngle(left_roof_r2, 0, 0, 0.5236F);
		left_roof_r2.setTextureUVOffset(430, 819).addCuboid(0, 0, -99, 10, 3, 16, 0, false);

		right_wall_bottom_r1 = createModelPart();
		right_wall_bottom_r1.setPivot(74, 37, 0);
		left_engine.addChild(right_wall_bottom_r1);
		setRotationAngle(right_wall_bottom_r1, 0, 0, -0.5236F);
		right_wall_bottom_r1.setTextureUVOffset(172, 874).addCuboid(0, 0, -99, 3, 10, 16, 0, false);

		right_wall_top_r3 = createModelPart();
		right_wall_top_r3.setPivot(74, 27, 0);
		left_engine.addChild(right_wall_top_r3);
		setRotationAngle(right_wall_top_r3, 0, 0, 0.5236F);
		right_wall_top_r3.setTextureUVOffset(210, 874).addCuboid(0, -10, -99, 3, 10, 16, 0, false);

		left_wall_bottom_r1 = createModelPart();
		left_wall_bottom_r1.setPivot(110, 37, 0);
		left_engine.addChild(left_wall_bottom_r1);
		setRotationAngle(left_wall_bottom_r1, 0, 0, 0.5236F);
		left_wall_bottom_r1.setTextureUVOffset(823, 868).addCuboid(-3, 0, -99, 3, 10, 16, 0, false);

		left_wall_top_r3 = createModelPart();
		left_wall_top_r3.setPivot(110, 27, 0);
		left_engine.addChild(left_wall_top_r3);
		setRotationAngle(left_wall_top_r3, 0, 0, -0.5236F);
		left_wall_top_r3.setTextureUVOffset(410, 871).addCuboid(-3, -10, -99, 3, 10, 16, 0, false);

		right_floor_r2 = createModelPart();
		right_floor_r2.setPivot(87, 50, 0);
		left_engine.addChild(right_floor_r2);
		setRotationAngle(right_floor_r2, 0, 0, 0.5236F);
		right_floor_r2.setTextureUVOffset(842, 440).addCuboid(-10, -3, -99, 10, 3, 16, 0, false);

		left_floor_r2 = createModelPart();
		left_floor_r2.setPivot(97, 50, 0);
		left_engine.addChild(left_floor_r2);
		setRotationAngle(left_floor_r2, 0, 0, -0.5236F);
		left_floor_r2.setTextureUVOffset(554, 816).addCuboid(0, -3, -99, 10, 3, 16, 0, false);

		support_back_r1 = createModelPart();
		support_back_r1.setPivot(0, 28, -20);
		left_engine.addChild(support_back_r1);
		setRotationAngle(support_back_r1, 0.8203F, 0, 0);
		support_back_r1.setTextureUVOffset(196, 777).addCuboid(89, -13, 0, 6, 13, 38, 0.1F, false);

		support_top_r1 = createModelPart();
		support_top_r1.setPivot(0, 11, -106);
		left_engine.addChild(support_top_r1);
		setRotationAngle(support_top_r1, 0.1222F, 0, 0);
		support_top_r1.setTextureUVOffset(0, 749).addCuboid(89, 0, 0, 6, 3, 56, 0.1F, false);

		right_engine = createModelPart();
		right_engine.setPivot(0, 0, 0);
		exterior.addChild(right_engine);
		right_engine.setTextureUVOffset(185, 712).addCuboid(-107, 17, -99, 30, 30, 35, 0, false);
		right_engine.setTextureUVOffset(575, 165).addCuboid(-95, 6, -106, 6, 22, 86, 0, false);
		right_engine.setTextureUVOffset(490, 816).addCuboid(-97, 47, -99, 10, 3, 16, 0, false);
		right_engine.setTextureUVOffset(785, 868).addCuboid(-110, 27, -99, 3, 10, 16, 0, false);
		right_engine.setTextureUVOffset(76, 808).addCuboid(-77, 27, -99, 3, 10, 16, 0, false);
		right_engine.setTextureUVOffset(525, 784).addCuboid(-97, 14, -99, 10, 3, 16, 0, false);

		tail_right_roof_r2 = createModelPart();
		tail_right_roof_r2.setPivot(-89, 22, -64);
		right_engine.addChild(tail_right_roof_r2);
		setRotationAngle(tail_right_roof_r2, -0.2094F, 0, 0.5236F);
		tail_right_roof_r2.setTextureUVOffset(280, 712).addCuboid(0, 0, 0, 6, 3, 29, 0, false);

		tail_left_roof_r2 = createModelPart();
		tail_left_roof_r2.setPivot(-95, 22, -64);
		right_engine.addChild(tail_left_roof_r2);
		setRotationAngle(tail_left_roof_r2, -0.2094F, 0, -0.5236F);
		tail_left_roof_r2.setTextureUVOffset(484, 784).addCuboid(-6, 0, 0, 6, 3, 29, 0, false);

		tail_roof_r2 = createModelPart();
		tail_roof_r2.setPivot(0, 22, -64);
		right_engine.addChild(tail_roof_r2);
		setRotationAngle(tail_roof_r2, -0.2094F, 0, 0);
		tail_roof_r2.setTextureUVOffset(554, 784).addCuboid(-95, 0, 0, 6, 3, 29, 0, false);

		tail_right_wall_bottom_r2 = createModelPart();
		tail_right_wall_bottom_r2.setPivot(-82, 35, -64);
		right_engine.addChild(tail_right_wall_bottom_r2);
		setRotationAngle(tail_right_wall_bottom_r2, 0, -0.2094F, 0.5236F);
		tail_right_wall_bottom_r2.setTextureUVOffset(136, 809).addCuboid(-3, 0, 0, 3, 6, 29, 0, false);

		tail_right_wall_top_r2 = createModelPart();
		tail_right_wall_top_r2.setPivot(-82, 29, -64);
		right_engine.addChild(tail_right_wall_top_r2);
		setRotationAngle(tail_right_wall_top_r2, 0, -0.2094F, -0.5236F);
		tail_right_wall_top_r2.setTextureUVOffset(41, 811).addCuboid(-3, -6, 0, 3, 6, 29, 0, false);

		tail_right_wall_r2 = createModelPart();
		tail_right_wall_r2.setPivot(-82, 0, -64);
		right_engine.addChild(tail_right_wall_r2);
		setRotationAngle(tail_right_wall_r2, 0, -0.2094F, 0);
		tail_right_wall_r2.setTextureUVOffset(455, 816).addCuboid(-3, 29, 0, 3, 6, 29, 0, false);

		tail_left_wall_bottom_r2 = createModelPart();
		tail_left_wall_bottom_r2.setPivot(-102, 35, -64);
		right_engine.addChild(tail_left_wall_bottom_r2);
		setRotationAngle(tail_left_wall_bottom_r2, 0, 0.2094F, -0.5236F);
		tail_left_wall_bottom_r2.setTextureUVOffset(519, 816).addCuboid(0, 0, 0, 3, 6, 29, 0, false);

		tail_left_wall_top_r2 = createModelPart();
		tail_left_wall_top_r2.setPivot(-102, 29, -64);
		right_engine.addChild(tail_left_wall_top_r2);
		setRotationAngle(tail_left_wall_top_r2, 0, 0.2094F, 0.5236F);
		tail_left_wall_top_r2.setTextureUVOffset(817, 684).addCuboid(0, -6, 0, 3, 6, 29, 0, false);

		tail_left_wall_r2 = createModelPart();
		tail_left_wall_r2.setPivot(-102, 0, -64);
		right_engine.addChild(tail_left_wall_r2);
		setRotationAngle(tail_left_wall_r2, 0, 0.2094F, 0);
		tail_left_wall_r2.setTextureUVOffset(817, 775).addCuboid(0, 29, 0, 3, 6, 29, 0, false);

		tail_right_floor_r2 = createModelPart();
		tail_right_floor_r2.setPivot(-89, 42, -64);
		right_engine.addChild(tail_right_floor_r2);
		setRotationAngle(tail_right_floor_r2, 0.2094F, 0, -0.5236F);
		tail_right_floor_r2.setTextureUVOffset(786, 385).addCuboid(0, -3, 0, 6, 3, 29, 0, false);

		tail_left_floor_r2 = createModelPart();
		tail_left_floor_r2.setPivot(-95, 42, -64);
		right_engine.addChild(tail_left_floor_r2);
		setRotationAngle(tail_left_floor_r2, 0.2094F, 0, 0.5236F);
		tail_left_floor_r2.setTextureUVOffset(786, 417).addCuboid(-6, -3, 0, 6, 3, 29, 0, false);

		tail_floor_r2 = createModelPart();
		tail_floor_r2.setPivot(0, 42, -64);
		right_engine.addChild(tail_floor_r2);
		setRotationAngle(tail_floor_r2, 0.2094F, 0, 0);
		tail_floor_r2.setTextureUVOffset(595, 788).addCuboid(-95, -3, 0, 6, 3, 29, 0, false);

		back_right_roof_r2 = createModelPart();
		back_right_roof_r2.setPivot(-87, 14, -83);
		right_engine.addChild(back_right_roof_r2);
		setRotationAngle(back_right_roof_r2, -0.1745F, 0, 0.5236F);
		back_right_roof_r2.setTextureUVOffset(818, 522).addCuboid(0, 0, 0, 10, 3, 20, 0, false);

		back_left_roof_r2 = createModelPart();
		back_left_roof_r2.setPivot(-97, 14, -83);
		right_engine.addChild(back_left_roof_r2);
		setRotationAngle(back_left_roof_r2, -0.1745F, 0, -0.5236F);
		back_left_roof_r2.setTextureUVOffset(820, 603).addCuboid(-10, 0, 0, 10, 3, 20, 0, false);

		back_roof_r2 = createModelPart();
		back_roof_r2.setPivot(0, 14, -83);
		right_engine.addChild(back_roof_r2);
		setRotationAngle(back_roof_r2, -0.1745F, 0, 0);
		back_roof_r2.setTextureUVOffset(823, 657).addCuboid(-97, 0, 0, 10, 3, 20, 0, false);

		back_right_wall_bottom_r2 = createModelPart();
		back_right_wall_bottom_r2.setPivot(-74, 37, -83);
		right_engine.addChild(back_right_wall_bottom_r2);
		setRotationAngle(back_right_wall_bottom_r2, 0, -0.1745F, 0.5236F);
		back_right_wall_bottom_r2.setTextureUVOffset(760, 487).addCuboid(-3, 0, 0, 3, 10, 20, 0, false);

		back_right_wall_top_r2 = createModelPart();
		back_right_wall_top_r2.setPivot(-74, 27, -83);
		right_engine.addChild(back_right_wall_top_r2);
		setRotationAngle(back_right_wall_top_r2, 0, -0.1745F, -0.5236F);
		back_right_wall_top_r2.setTextureUVOffset(760, 517).addCuboid(-3, -10, 0, 3, 10, 20, 0, false);

		back_right_wall_r2 = createModelPart();
		back_right_wall_r2.setPivot(-74, 0, -83);
		right_engine.addChild(back_right_wall_r2);
		setRotationAngle(back_right_wall_r2, 0, -0.1745F, 0);
		back_right_wall_r2.setTextureUVOffset(755, 848).addCuboid(-3, 27, 0, 3, 10, 20, 0, false);

		back_left_wall_bottom_r2 = createModelPart();
		back_left_wall_bottom_r2.setPivot(-110, 37, -83);
		right_engine.addChild(back_left_wall_bottom_r2);
		setRotationAngle(back_left_wall_bottom_r2, 0, 0.1745F, -0.5236F);
		back_left_wall_bottom_r2.setTextureUVOffset(285, 852).addCuboid(0, 0, 0, 3, 10, 20, 0, false);

		back_left_wall_top_r2 = createModelPart();
		back_left_wall_top_r2.setPivot(-110, 27, -83);
		right_engine.addChild(back_left_wall_top_r2);
		setRotationAngle(back_left_wall_top_r2, 0, 0.1745F, 0.5236F);
		back_left_wall_top_r2.setTextureUVOffset(852, 680).addCuboid(0, -10, 0, 3, 10, 20, 0, false);

		back_left_wall_r2 = createModelPart();
		back_left_wall_r2.setPivot(-110, 0, -83);
		right_engine.addChild(back_left_wall_r2);
		setRotationAngle(back_left_wall_r2, 0, 0.1745F, 0);
		back_left_wall_r2.setTextureUVOffset(852, 754).addCuboid(0, 27, 0, 3, 10, 20, 0, false);

		back_right_floor_r2 = createModelPart();
		back_right_floor_r2.setPivot(-87, 50, -83);
		right_engine.addChild(back_right_floor_r2);
		setRotationAngle(back_right_floor_r2, 0.1745F, 0, -0.5236F);
		back_right_floor_r2.setTextureUVOffset(746, 823).addCuboid(0, -3, 0, 10, 3, 20, 0, false);

		back_left_floor_r2 = createModelPart();
		back_left_floor_r2.setPivot(-97, 50, -83);
		right_engine.addChild(back_left_floor_r2);
		setRotationAngle(back_left_floor_r2, 0.1745F, 0, 0.5236F);
		back_left_floor_r2.setTextureUVOffset(825, 111).addCuboid(-10, -3, 0, 10, 3, 20, 0, false);

		back_floor_r2 = createModelPart();
		back_floor_r2.setPivot(0, 50, -83);
		right_engine.addChild(back_floor_r2);
		setRotationAngle(back_floor_r2, 0.1745F, 0, 0);
		back_floor_r2.setTextureUVOffset(825, 134).addCuboid(-97, -3, 0, 10, 3, 20, 0, false);

		front_right_roof_r2 = createModelPart();
		front_right_roof_r2.setPivot(-87, 14, -99);
		right_engine.addChild(front_right_roof_r2);
		setRotationAngle(front_right_roof_r2, 0.1047F, 0, 0.5236F);
		front_right_roof_r2.setTextureUVOffset(827, 385).addCuboid(0, 0, -20, 10, 3, 20, 0, false);

		front_left_roof_r2 = createModelPart();
		front_left_roof_r2.setPivot(-97, 14, -99);
		right_engine.addChild(front_left_roof_r2);
		setRotationAngle(front_left_roof_r2, 0.1047F, 0, -0.5236F);
		front_left_roof_r2.setTextureUVOffset(827, 417).addCuboid(-10, 0, -20, 10, 3, 20, 0, false);

		front_roof_r2 = createModelPart();
		front_roof_r2.setPivot(0, 14, -99);
		right_engine.addChild(front_roof_r2);
		setRotationAngle(front_roof_r2, 0.1047F, 0, 0);
		front_roof_r2.setTextureUVOffset(180, 828).addCuboid(-97, 0, -20, 10, 3, 20, 0, false);

		front_right_wall_bottom_r2 = createModelPart();
		front_right_wall_bottom_r2.setPivot(-74, 37, -99);
		right_engine.addChild(front_right_wall_bottom_r2);
		setRotationAngle(front_right_wall_bottom_r2, 0, 0.1047F, 0.5236F);
		front_right_wall_bottom_r2.setTextureUVOffset(539, 854).addCuboid(-3, 0, -20, 3, 10, 20, 0, false);

		front_right_wall_top_r2 = createModelPart();
		front_right_wall_top_r2.setPivot(-74, 27, -99);
		right_engine.addChild(front_right_wall_top_r2);
		setRotationAngle(front_right_wall_top_r2, 0, 0.1047F, -0.5236F);
		front_right_wall_top_r2.setTextureUVOffset(585, 855).addCuboid(-3, -10, -20, 3, 10, 20, 0, false);

		front_right_wall_r2 = createModelPart();
		front_right_wall_r2.setPivot(-74, 0, -99);
		right_engine.addChild(front_right_wall_r2);
		setRotationAngle(front_right_wall_r2, 0, 0.1047F, 0);
		front_right_wall_r2.setTextureUVOffset(856, 626).addCuboid(-3, 27, -20, 3, 10, 20, 0, false);

		front_left_wall_bottom_r2 = createModelPart();
		front_left_wall_bottom_r2.setPivot(-110, 37, -99);
		right_engine.addChild(front_left_wall_bottom_r2);
		setRotationAngle(front_left_wall_bottom_r2, 0, -0.1047F, -0.5236F);
		front_left_wall_bottom_r2.setTextureUVOffset(380, 857).addCuboid(0, 0, -20, 3, 10, 20, 0, false);

		front_left_wall_top_r2 = createModelPart();
		front_left_wall_top_r2.setPivot(-110, 27, -99);
		right_engine.addChild(front_left_wall_top_r2);
		setRotationAngle(front_left_wall_top_r2, 0, -0.1047F, 0.5236F);
		front_left_wall_top_r2.setTextureUVOffset(631, 858).addCuboid(0, -10, -20, 3, 10, 20, 0, false);

		front_left_wall_r2 = createModelPart();
		front_left_wall_r2.setPivot(-110, 0, -99);
		right_engine.addChild(front_left_wall_r2);
		setRotationAngle(front_left_wall_r2, 0, -0.1047F, 0);
		front_left_wall_r2.setTextureUVOffset(677, 858).addCuboid(0, 27, -20, 3, 10, 20, 0, false);

		front_right_floor_r2 = createModelPart();
		front_right_floor_r2.setPivot(-87, 50, -99);
		right_engine.addChild(front_right_floor_r2);
		setRotationAngle(front_right_floor_r2, -0.1047F, 0, -0.5236F);
		front_right_floor_r2.setTextureUVOffset(240, 828).addCuboid(0, -3, -20, 10, 3, 20, 0, false);

		front_left_floor_r2 = createModelPart();
		front_left_floor_r2.setPivot(-97, 50, -99);
		right_engine.addChild(front_left_floor_r2);
		setRotationAngle(front_left_floor_r2, -0.1047F, 0, 0.5236F);
		front_left_floor_r2.setTextureUVOffset(300, 829).addCuboid(-10, -3, -20, 10, 3, 20, 0, false);

		front_floor_r2 = createModelPart();
		front_floor_r2.setPivot(0, 50, -99);
		right_engine.addChild(front_floor_r2);
		setRotationAngle(front_floor_r2, -0.1047F, 0, 0);
		front_floor_r2.setTextureUVOffset(833, 192).addCuboid(-97, -3, -20, 10, 3, 20, 0, false);

		right_roof_r3 = createModelPart();
		right_roof_r3.setPivot(-87, 14, 0);
		right_engine.addChild(right_roof_r3);
		setRotationAngle(right_roof_r3, 0, 0, 0.5236F);
		right_roof_r3.setTextureUVOffset(445, 750).addCuboid(0, 0, -99, 10, 3, 16, 0, false);

		left_roof_r3 = createModelPart();
		left_roof_r3.setPivot(-97, 14, 0);
		right_engine.addChild(left_roof_r3);
		setRotationAngle(left_roof_r3, 0, 0, -0.5236F);
		left_roof_r3.setTextureUVOffset(699, 754).addCuboid(-10, 0, -99, 10, 3, 16, 0, false);

		right_wall_bottom_r2 = createModelPart();
		right_wall_bottom_r2.setPivot(-74, 37, 0);
		right_engine.addChild(right_wall_bottom_r2);
		setRotationAngle(right_wall_bottom_r2, 0, 0, 0.5236F);
		right_wall_bottom_r2.setTextureUVOffset(136, 771).addCuboid(-3, 0, -99, 3, 10, 16, 0, false);

		right_wall_top_r4 = createModelPart();
		right_wall_top_r4.setPivot(-74, 27, 0);
		right_engine.addChild(right_wall_top_r4);
		setRotationAngle(right_wall_top_r4, 0, 0, -0.5236F);
		right_wall_top_r4.setTextureUVOffset(246, 777).addCuboid(-3, -10, -99, 3, 10, 16, 0, false);

		left_wall_bottom_r2 = createModelPart();
		left_wall_bottom_r2.setPivot(-110, 37, 0);
		right_engine.addChild(left_wall_bottom_r2);
		setRotationAngle(left_wall_bottom_r2, 0, 0, -0.5236F);
		left_wall_bottom_r2.setTextureUVOffset(867, 408).addCuboid(0, 0, -99, 3, 10, 16, 0, false);

		left_wall_top_r4 = createModelPart();
		left_wall_top_r4.setPivot(-110, 27, 0);
		right_engine.addChild(left_wall_top_r4);
		setRotationAngle(left_wall_top_r4, 0, 0, 0.5236F);
		left_wall_top_r4.setTextureUVOffset(868, 532).addCuboid(0, -10, -99, 3, 10, 16, 0, false);

		right_floor_r3 = createModelPart();
		right_floor_r3.setPivot(-87, 50, 0);
		right_engine.addChild(right_floor_r3);
		setRotationAngle(right_floor_r3, 0, 0, -0.5236F);
		right_floor_r3.setTextureUVOffset(677, 791).addCuboid(0, -3, -99, 10, 3, 16, 0, false);

		left_floor_r3 = createModelPart();
		left_floor_r3.setPivot(-97, 50, 0);
		right_engine.addChild(left_floor_r3);
		setRotationAngle(left_floor_r3, 0, 0, 0.5236F);
		left_floor_r3.setTextureUVOffset(747, 791).addCuboid(-10, -3, -99, 10, 3, 16, 0, false);

		support_back_r2 = createModelPart();
		support_back_r2.setPivot(0, 28, -20);
		right_engine.addChild(support_back_r2);
		setRotationAngle(support_back_r2, 0.8203F, 0, 0);
		support_back_r2.setTextureUVOffset(577, 725).addCuboid(-95, -13, 0, 6, 13, 38, 0.1F, false);

		support_top_r2 = createModelPart();
		support_top_r2.setPivot(0, 11, -106);
		right_engine.addChild(support_top_r2);
		setRotationAngle(support_top_r2, 0.1222F, 0, 0);
		support_top_r2.setTextureUVOffset(699, 732).addCuboid(-95, 0, 0, 6, 3, 56, 0.1F, false);

		window_interior = createModelPart();
		window_interior.setPivot(0, 24, 0);
		window_interior.setTextureUVOffset(68, 749).addCuboid(-32, 8, -8, 32, 1, 16, 0, false);
		window_interior.setTextureUVOffset(251, 109).addCuboid(-21, -17, -8, 7, 0, 16, 0, false);
		window_interior.setTextureUVOffset(334, 109).addCuboid(-8, -26, -8, 8, 0, 16, 0, false);

		roof_side_r1 = createModelPart();
		roof_side_r1.setPivot(-8, -26, 0);
		window_interior.addChild(roof_side_r1);
		setRotationAngle(roof_side_r1, 0, 0, -0.1745F);
		roof_side_r1.setTextureUVOffset(320, 242).addCuboid(-8, 0, -8, 8, 0, 16, 0, false);

		luggage_rack_top_r1 = createModelPart();
		luggage_rack_top_r1.setPivot(-13, -24, 0);
		window_interior.addChild(luggage_rack_top_r1);
		setRotationAngle(luggage_rack_top_r1, 0, 0, 0.3491F);
		luggage_rack_top_r1.setTextureUVOffset(18, 0).addCuboid(-3, 0, -8, 3, 0, 16, 0, false);

		luggage_rack_front_r1 = createModelPart();
		luggage_rack_front_r1.setPivot(-12, -18, -8);
		window_interior.addChild(luggage_rack_front_r1);
		setRotationAngle(luggage_rack_front_r1, 0, 0, -0.1745F);
		luggage_rack_front_r1.setTextureUVOffset(327, 612).addCuboid(0, -7, 0, 0, 7, 16, 0, false);

		luggage_rack_bottom_front_r1 = createModelPart();
		luggage_rack_bottom_front_r1.setPivot(-14, -17, 0);
		window_interior.addChild(luggage_rack_bottom_front_r1);
		setRotationAngle(luggage_rack_bottom_front_r1, 0, 0, -0.5236F);
		luggage_rack_bottom_front_r1.setTextureUVOffset(18, 16).addCuboid(0, 0, -8, 3, 0, 16, 0, false);

		right_top_wall_r2 = createModelPart();
		right_top_wall_r2.setPivot(-22, -18, 0);
		window_interior.addChild(right_top_wall_r2);
		setRotationAngle(right_top_wall_r2, 0, 0, -0.384F);
		right_top_wall_r2.setTextureUVOffset(255, 175).addCuboid(-5, 0, -8, 5, 0, 16, 0, false);

		window_interior_wall = createModelPart();
		window_interior_wall.setPivot(0, 24, 0);
		window_interior_wall.setTextureUVOffset(673, 176).addCuboid(-30, -10, -8, 0, 12, 16, 0, false);

		right_upper_wall_r1 = createModelPart();
		right_upper_wall_r1.setPivot(-32, -10, 0);
		window_interior_wall.addChild(right_upper_wall_r1);
		setRotationAngle(right_upper_wall_r1, 0, 0, 0.5236F);
		right_upper_wall_r1.setTextureUVOffset(673, 188).addCuboid(2, -9, -8, 0, 9, 16, 0, false);

		right_lower_wall_r1 = createModelPart();
		right_lower_wall_r1.setPivot(-30, 2, 0);
		window_interior_wall.addChild(right_lower_wall_r1);
		setRotationAngle(right_lower_wall_r1, 0, 0, -0.2967F);
		right_lower_wall_r1.setTextureUVOffset(625, 227).addCuboid(0, 0, -8, 0, 7, 16, 0, false);

		window_interior_light = createModelPart();
		window_interior_light.setPivot(0, 24, 0);


		light_r1 = createModelPart();
		light_r1.setPivot(-21, -17, 0);
		window_interior_light.addChild(light_r1);
		setRotationAngle(light_r1, 0, 0, 0.5236F);
		light_r1.setTextureUVOffset(18, 32).addCuboid(-2, 0, -8, 2, 0, 16, 0, false);

		window_interior_blank = createModelPart();
		window_interior_blank.setPivot(0, 24, 0);
		window_interior_blank.setTextureUVOffset(94, 636).addCuboid(-32, 8, -4, 32, 1, 8, 0, false);
		window_interior_blank.setTextureUVOffset(124, 139).addCuboid(-21, -17, -4, 7, 0, 8, 0, false);
		window_interior_blank.setTextureUVOffset(279, 261).addCuboid(-8, -26, -4, 8, 0, 8, 0, false);

		roof_side_r2 = createModelPart();
		roof_side_r2.setPivot(-8, -26, 0);
		window_interior_blank.addChild(roof_side_r2);
		setRotationAngle(roof_side_r2, 0, 0, -0.1745F);
		roof_side_r2.setTextureUVOffset(342, 125).addCuboid(-8, 0, -4, 8, 0, 8, 0, false);

		luggage_rack_top_r2 = createModelPart();
		luggage_rack_top_r2.setPivot(-13, -24, 0);
		window_interior_blank.addChild(luggage_rack_top_r2);
		setRotationAngle(luggage_rack_top_r2, 0, 0, 0.3491F);
		luggage_rack_top_r2.setTextureUVOffset(26, 48).addCuboid(-3, 0, -4, 3, 0, 8, 0, false);

		luggage_rack_front_r2 = createModelPart();
		luggage_rack_front_r2.setPivot(-12, -18, 0);
		window_interior_blank.addChild(luggage_rack_front_r2);
		setRotationAngle(luggage_rack_front_r2, 0, 0, -0.1745F);
		luggage_rack_front_r2.setTextureUVOffset(350, 146).addCuboid(0, -7, -4, 0, 7, 8, 0, false);

		luggage_rack_bottom_front_r2 = createModelPart();
		luggage_rack_bottom_front_r2.setPivot(-14, -17, 0);
		window_interior_blank.addChild(luggage_rack_bottom_front_r2);
		setRotationAngle(luggage_rack_bottom_front_r2, 0, 0, -0.5236F);
		luggage_rack_bottom_front_r2.setTextureUVOffset(26, 56).addCuboid(0, 0, -4, 3, 0, 8, 0, false);

		right_top_wall_r3 = createModelPart();
		right_top_wall_r3.setPivot(-22, -18, 0);
		window_interior_blank.addChild(right_top_wall_r3);
		setRotationAngle(right_top_wall_r3, 0, 0, -0.384F);
		right_top_wall_r3.setTextureUVOffset(8, 0).addCuboid(-5, 0, -4, 5, 0, 8, 0, false);

		window_interior_blank_wall = createModelPart();
		window_interior_blank_wall.setPivot(0, 24, 0);
		window_interior_blank_wall.setTextureUVOffset(350, 125).addCuboid(-30, -10, -4, 0, 12, 8, 0, false);

		right_upper_wall_r2 = createModelPart();
		right_upper_wall_r2.setPivot(-32, -10, 0);
		window_interior_blank_wall.addChild(right_upper_wall_r2);
		setRotationAngle(right_upper_wall_r2, 0, 0, 0.5236F);
		right_upper_wall_r2.setTextureUVOffset(350, 137).addCuboid(2, -9, -4, 0, 9, 8, 0, false);

		right_lower_wall_r2 = createModelPart();
		right_lower_wall_r2.setPivot(-30, 2, 0);
		window_interior_blank_wall.addChild(right_lower_wall_r2);
		setRotationAngle(right_lower_wall_r2, 0, 0, -0.2967F);
		right_lower_wall_r2.setTextureUVOffset(350, 153).addCuboid(0, 0, -4, 0, 7, 8, 0, false);

		window_interior_blank_light = createModelPart();
		window_interior_blank_light.setPivot(0, 24, 0);


		light_r2 = createModelPart();
		light_r2.setPivot(-21, -17, 0);
		window_interior_blank_light.addChild(light_r2);
		setRotationAngle(light_r2, 0, 0, 0.5236F);
		light_r2.setTextureUVOffset(0, 0).addCuboid(-2, 0, -4, 2, 0, 8, 0, false);

		door_left_exterior = createModelPart();
		door_left_exterior.setPivot(0, 24, 0);
		door_left_exterior.setTextureUVOffset(41, 796).addCuboid(32, -10, -225, 0, 18, 12, 0, false);

		right_wall_front_r2 = createModelPart();
		right_wall_front_r2.setPivot(32, 0, -225);
		door_left_exterior.addChild(right_wall_front_r2);
		setRotationAngle(right_wall_front_r2, 0, 0.1745F, 0);
		right_wall_front_r2.setTextureUVOffset(0, 859).addCuboid(0, -10, -4, 0, 18, 4, 0, false);

		right_top_wall_front_r1 = createModelPart();
		right_top_wall_front_r1.setPivot(32, -10, -225);
		door_left_exterior.addChild(right_top_wall_front_r1);
		setRotationAngle(right_top_wall_front_r1, 0, 0.1745F, -0.5236F);
		right_top_wall_front_r1.setTextureUVOffset(0, 836).addCuboid(0, -13, -4, 0, 13, 4, 0, false);

		right_top_wall_r4 = createModelPart();
		right_top_wall_r4.setPivot(32, -10, 0);
		door_left_exterior.addChild(right_top_wall_r4);
		setRotationAngle(right_top_wall_r4, 0, 0, -0.5236F);
		right_top_wall_r4.setTextureUVOffset(0, 812).addCuboid(0, -13, -225, 0, 13, 12, 0, false);

		top_back_r2 = createModelPart();
		top_back_r2.setPivot(9, -33, -191);
		door_left_exterior.addChild(top_back_r2);
		setRotationAngle(top_back_r2, 0.0873F, 0, 0.5236F);
		top_back_r2.setTextureUVOffset(-16, 808).addCuboid(13, 0, -38, 9, 0, 16, 0, false);

		door_left_interior = createModelPart();
		door_left_interior.setPivot(0, 24, 0);
		door_left_interior.setTextureUVOffset(94, 907).addCuboid(29, -10, -225, 3, 18, 12, 0, false);

		right_wall_front_r3 = createModelPart();
		right_wall_front_r3.setPivot(32, 0, -225);
		door_left_interior.addChild(right_wall_front_r3);
		setRotationAngle(right_wall_front_r3, 0, 0.1745F, 0);
		right_wall_front_r3.setTextureUVOffset(0, 910).addCuboid(-3, -10, -4, 3, 18, 4, 0, false);

		right_top_wall_front_r2 = createModelPart();
		right_top_wall_front_r2.setPivot(32, -10, -225);
		door_left_interior.addChild(right_top_wall_front_r2);
		setRotationAngle(right_top_wall_front_r2, 0, 0.1745F, -0.5236F);
		right_top_wall_front_r2.setTextureUVOffset(14, 915).addCuboid(-3, -13, -4, 3, 13, 4, 0, false);

		right_top_wall_r5 = createModelPart();
		right_top_wall_r5.setPivot(32, -10, 0);
		door_left_interior.addChild(right_top_wall_r5);
		setRotationAngle(right_top_wall_r5, 0, 0, -0.5236F);
		right_top_wall_r5.setTextureUVOffset(16, 923).addCuboid(-3, -13, -225, 3, 13, 12, 0, false);

		top_back_r3 = createModelPart();
		top_back_r3.setPivot(9, -33, -191);
		door_left_interior.addChild(top_back_r3);
		setRotationAngle(top_back_r3, 0.0873F, 0, 0.5236F);
		top_back_r3.setTextureUVOffset(42, 907).addCuboid(13, 0, -38, 9, 3, 16, 0, false);

		door_right_exterior = createModelPart();
		door_right_exterior.setPivot(0, 24, 0);
		door_right_exterior.setTextureUVOffset(136, 794).addCuboid(-32, -10, -225, 0, 18, 12, 0, false);

		right_wall_front_r4 = createModelPart();
		right_wall_front_r4.setPivot(-32, 0, -225);
		door_right_exterior.addChild(right_wall_front_r4);
		setRotationAngle(right_wall_front_r4, 0, -0.1745F, 0);
		right_wall_front_r4.setTextureUVOffset(8, 859).addCuboid(0, -10, -4, 0, 18, 4, 0, false);

		right_top_wall_front_r3 = createModelPart();
		right_top_wall_front_r3.setPivot(-32, -10, -225);
		door_right_exterior.addChild(right_top_wall_front_r3);
		setRotationAngle(right_top_wall_front_r3, 0, -0.1745F, 0.5236F);
		right_top_wall_front_r3.setTextureUVOffset(8, 836).addCuboid(0, -13, -4, 0, 13, 4, 0, false);

		right_top_wall_r6 = createModelPart();
		right_top_wall_r6.setPivot(-32, -10, 0);
		door_right_exterior.addChild(right_top_wall_r6);
		setRotationAngle(right_top_wall_r6, 0, 0, 0.5236F);
		right_top_wall_r6.setTextureUVOffset(26, 857).addCuboid(0, -13, -225, 0, 13, 12, 0, false);

		top_back_r4 = createModelPart();
		top_back_r4.setPivot(-9, -33, -191);
		door_right_exterior.addChild(top_back_r4);
		setRotationAngle(top_back_r4, 0.0873F, 0, -0.5236F);
		top_back_r4.setTextureUVOffset(82, 808).addCuboid(-22, 0, -38, 9, 0, 16, 0, false);

		door_right_interior = createModelPart();
		door_right_interior.setPivot(0, 24, 0);
		door_right_interior.setTextureUVOffset(28, 893).addCuboid(-32, -10, -225, 3, 18, 12, 0, false);

		right_wall_front_r5 = createModelPart();
		right_wall_front_r5.setPivot(-32, 0, -225);
		door_right_interior.addChild(right_wall_front_r5);
		setRotationAngle(right_wall_front_r5, 0, -0.1745F, 0);
		right_wall_front_r5.setTextureUVOffset(14, 893).addCuboid(0, -10, -4, 3, 18, 4, 0, false);

		right_top_wall_front_r4 = createModelPart();
		right_top_wall_front_r4.setPivot(-32, -10, -225);
		door_right_interior.addChild(right_top_wall_front_r4);
		setRotationAngle(right_top_wall_front_r4, 0, -0.1745F, 0.5236F);
		right_top_wall_front_r4.setTextureUVOffset(0, 893).addCuboid(0, -13, -4, 3, 13, 4, 0, false);

		right_top_wall_r7 = createModelPart();
		right_top_wall_r7.setPivot(-32, -10, 0);
		door_right_interior.addChild(right_top_wall_r7);
		setRotationAngle(right_top_wall_r7, 0, 0, 0.5236F);
		right_top_wall_r7.setTextureUVOffset(76, 894).addCuboid(0, -13, -225, 3, 13, 12, 0, false);

		top_back_r5 = createModelPart();
		top_back_r5.setPivot(-9, -33, -191);
		door_right_interior.addChild(top_back_r5);
		setRotationAngle(top_back_r5, 0.0873F, 0, -0.5236F);
		top_back_r5.setTextureUVOffset(46, 875).addCuboid(-22, 0, -38, 9, 3, 16, 0, false);

		head_interior = createModelPart();
		head_interior.setPivot(0, 24, 0);
		head_interior.setTextureUVOffset(673, 192).addCuboid(-32, 8, -230, 64, 1, 32, 0, false);
		head_interior.setTextureUVOffset(759, 240).addCuboid(-30, -26, -230, 60, 34, 0, 0, false);
		head_interior.setTextureUVOffset(424, 777).addCuboid(8, -26, -206, 22, 34, 8, 0, false);
		head_interior.setTextureUVOffset(313, 556).addCuboid(-30, -26, -206, 22, 34, 8, 0, false);
		head_interior.setTextureUVOffset(92, 139).addCuboid(-8, -26, -206, 16, 0, 8, 0, false);

		right_door_top_r1 = createModelPart();
		right_door_top_r1.setPivot(-9, -33, -191);
		head_interior.addChild(right_door_top_r1);
		setRotationAngle(right_door_top_r1, 0.0873F, 0, -0.5236F);
		right_door_top_r1.setTextureUVOffset(46, 879).addCuboid(-13, 0, -39, 0, 2, 17, 0, true);

		left_door_top_r1 = createModelPart();
		left_door_top_r1.setPivot(9, -33, -191);
		head_interior.addChild(left_door_top_r1);
		setRotationAngle(left_door_top_r1, 0.0873F, 0, 0.5236F);
		left_door_top_r1.setTextureUVOffset(46, 877).addCuboid(13, 0, -39, 0, 2, 17, 0, false);

		roof_front_r1 = createModelPart();
		roof_front_r1.setPivot(0, -26, -206);
		head_interior.addChild(roof_front_r1);
		setRotationAngle(roof_front_r1, 0.0873F, 0, 0);
		roof_front_r1.setTextureUVOffset(69, 556).addCuboid(-8, 0, -25, 16, 0, 25, 0, false);

		right_roof_side_r1 = createModelPart();
		right_roof_side_r1.setPivot(-8, -26, -206);
		head_interior.addChild(right_roof_side_r1);
		setRotationAngle(right_roof_side_r1, 0.0873F, 0, -0.1745F);
		right_roof_side_r1.setTextureUVOffset(311, 192).addCuboid(-15, 0, -25, 15, 0, 25, 0, false);

		left_roof_side_r1 = createModelPart();
		left_roof_side_r1.setPivot(8, -26, -206);
		head_interior.addChild(left_roof_side_r1);
		setRotationAngle(left_roof_side_r1, 0.0873F, 0, 0.1745F);
		left_roof_side_r1.setTextureUVOffset(311, 217).addCuboid(0, 0, -25, 15, 0, 25, 0, false);

		right_upper_wall_r3 = createModelPart();
		right_upper_wall_r3.setPivot(-32, -10, -206);
		head_interior.addChild(right_upper_wall_r3);
		setRotationAngle(right_upper_wall_r3, 0, -0.0436F, 0.5236F);
		right_upper_wall_r3.setTextureUVOffset(699, 700).addCuboid(2, -17, -25, 0, 17, 25, 0, false);

		left_upper_wall_r1 = createModelPart();
		left_upper_wall_r1.setPivot(32, -10, -206);
		head_interior.addChild(left_upper_wall_r1);
		setRotationAngle(left_upper_wall_r1, 0, 0.0436F, -0.5236F);
		left_upper_wall_r1.setTextureUVOffset(511, 701).addCuboid(-2, -17, -25, 0, 17, 25, 0, false);

		right_wall_r3 = createModelPart();
		right_wall_r3.setPivot(-30, 2, -206);
		head_interior.addChild(right_wall_r3);
		setRotationAngle(right_wall_r3, 0, -0.0436F, 0);
		right_wall_r3.setTextureUVOffset(445, 713).addCuboid(0, -12, -25, 0, 12, 25, 0, false);

		left_wall_r3 = createModelPart();
		left_wall_r3.setPivot(30, 2, -206);
		head_interior.addChild(left_wall_r3);
		setRotationAngle(left_wall_r3, 0, 0.0436F, 0);
		left_wall_r3.setTextureUVOffset(699, 717).addCuboid(0, -12, -25, 0, 12, 25, 0, false);

		right_lower_wall_r3 = createModelPart();
		right_lower_wall_r3.setPivot(-30, 2, -206);
		head_interior.addChild(right_lower_wall_r3);
		setRotationAngle(right_lower_wall_r3, 0, -0.0436F, -0.2967F);
		right_lower_wall_r3.setTextureUVOffset(231, 114).addCuboid(0, 0, -25, 0, 7, 25, 0, false);

		left_lower_wall_r1 = createModelPart();
		left_lower_wall_r1.setPivot(30, 2, -206);
		head_interior.addChild(left_lower_wall_r1);
		setRotationAngle(left_lower_wall_r1, 0, 0.0436F, 0.2967F);
		left_lower_wall_r1.setTextureUVOffset(575, 218).addCuboid(0, 0, -25, 0, 7, 25, 0, false);

		end_interior = createModelPart();
		end_interior.setPivot(0, 24, 0);
		end_interior.setTextureUVOffset(79, 236).addCuboid(-32, 8, 106, 64, 1, 80, 0, false);
		end_interior.setTextureUVOffset(758, 559).addCuboid(-30, -26, 186, 60, 34, 0, 0, false);
		end_interior.setTextureUVOffset(354, 777).addCuboid(8, -26, 146, 17, 34, 18, 0, false);
		end_interior.setTextureUVOffset(284, 777).addCuboid(-25, -26, 146, 17, 34, 18, 0, false);
		end_interior.setTextureUVOffset(0, 175).addCuboid(-8, -26, 106, 16, 0, 81, 0, false);
		end_interior.setTextureUVOffset(0, 0).addCuboid(14, -17, 106, 7, 0, 40, 0, false);
		end_interior.setTextureUVOffset(0, 40).addCuboid(-21, -17, 106, 7, 0, 40, 0, false);

		right_luggage_rack_top_r1 = createModelPart();
		right_luggage_rack_top_r1.setPivot(-13, -24, 0);
		end_interior.addChild(right_luggage_rack_top_r1);
		setRotationAngle(right_luggage_rack_top_r1, 0, 0, 0.3491F);
		right_luggage_rack_top_r1.setTextureUVOffset(0, 80).addCuboid(-3, 0, 106, 3, 0, 40, 0, false);

		left_luggage_rack_top_r1 = createModelPart();
		left_luggage_rack_top_r1.setPivot(13, -24, 0);
		end_interior.addChild(left_luggage_rack_top_r1);
		setRotationAngle(left_luggage_rack_top_r1, 0, 0, -0.3491F);
		left_luggage_rack_top_r1.setTextureUVOffset(14, 40).addCuboid(0, 0, 106, 3, 0, 40, 0, false);

		right_luggage_rack_front_r1 = createModelPart();
		right_luggage_rack_front_r1.setPivot(-12, -18, 0);
		end_interior.addChild(right_luggage_rack_front_r1);
		setRotationAngle(right_luggage_rack_front_r1, 0, 0, -0.1745F);
		right_luggage_rack_front_r1.setTextureUVOffset(287, 257).addCuboid(0, -7, 106, 0, 7, 40, 0, false);

		left_luggage_rack_front_r1 = createModelPart();
		left_luggage_rack_front_r1.setPivot(12, -18, 0);
		end_interior.addChild(left_luggage_rack_front_r1);
		setRotationAngle(left_luggage_rack_front_r1, 0, 0, 0.1745F);
		left_luggage_rack_front_r1.setTextureUVOffset(0, 267).addCuboid(0, -7, 106, 0, 7, 40, 0, false);

		right_luggage_rack_bottom_front_r1 = createModelPart();
		right_luggage_rack_bottom_front_r1.setPivot(-14, -17, 0);
		end_interior.addChild(right_luggage_rack_bottom_front_r1);
		setRotationAngle(right_luggage_rack_bottom_front_r1, 0, 0, -0.5236F);
		right_luggage_rack_bottom_front_r1.setTextureUVOffset(6, 80).addCuboid(0, 0, 106, 3, 0, 40, 0, false);

		left_luggage_rack_bottom_front_r1 = createModelPart();
		left_luggage_rack_bottom_front_r1.setPivot(14, -17, 0);
		end_interior.addChild(left_luggage_rack_bottom_front_r1);
		setRotationAngle(left_luggage_rack_bottom_front_r1, 0, 0, 0.5236F);
		left_luggage_rack_bottom_front_r1.setTextureUVOffset(14, 0).addCuboid(-3, 0, 106, 3, 0, 40, 0, false);

		right_top_wall_r8 = createModelPart();
		right_top_wall_r8.setPivot(-22, -18, 0);
		end_interior.addChild(right_top_wall_r8);
		setRotationAngle(right_top_wall_r8, 0, 0, -0.384F);
		right_top_wall_r8.setTextureUVOffset(105, 58).addCuboid(-5, 0, 106, 5, 0, 28, 0, false);

		left_top_wall_r2 = createModelPart();
		left_top_wall_r2.setPivot(22, -18, 0);
		end_interior.addChild(left_top_wall_r2);
		setRotationAngle(left_top_wall_r2, 0, 0, 0.384F);
		left_top_wall_r2.setTextureUVOffset(105, 30).addCuboid(0, 0, 106, 5, 0, 28, 0, false);

		right_roof_side_r2 = createModelPart();
		right_roof_side_r2.setPivot(-8, -26, 0);
		end_interior.addChild(right_roof_side_r2);
		setRotationAngle(right_roof_side_r2, 0, 0, -0.1745F);
		right_roof_side_r2.setTextureUVOffset(225, 192).addCuboid(-15, 0, 106, 15, 0, 81, 0, false);

		left_roof_side_r2 = createModelPart();
		left_roof_side_r2.setPivot(8, -26, 0);
		end_interior.addChild(left_roof_side_r2);
		setRotationAngle(left_roof_side_r2, 0, 0, 0.1745F);
		left_roof_side_r2.setTextureUVOffset(246, 0).addCuboid(0, 0, 106, 15, 0, 81, 0, false);

		right_upper_wall_r4 = createModelPart();
		right_upper_wall_r4.setPivot(-32, -10, 106);
		end_interior.addChild(right_upper_wall_r4);
		setRotationAngle(right_upper_wall_r4, 0, 0.0873F, 0.5236F);
		right_upper_wall_r4.setTextureUVOffset(374, 553).addCuboid(2, -23, 0, 0, 23, 81, 0, false);

		left_upper_wall_r2 = createModelPart();
		left_upper_wall_r2.setPivot(32, -10, 106);
		end_interior.addChild(left_upper_wall_r2);
		setRotationAngle(left_upper_wall_r2, 0, -0.0873F, -0.5236F);
		left_upper_wall_r2.setTextureUVOffset(536, 553).addCuboid(-2, -23, 0, 0, 23, 81, 0, false);

		right_wall_r4 = createModelPart();
		right_wall_r4.setPivot(-30, 2, 106);
		end_interior.addChild(right_wall_r4);
		setRotationAngle(right_wall_r4, 0, 0.1309F, 0);
		right_wall_r4.setTextureUVOffset(184, 555).addCuboid(0, -16, 0, 0, 16, 81, 0, false);

		left_wall_r4 = createModelPart();
		left_wall_r4.setPivot(30, 2, 106);
		end_interior.addChild(left_wall_r4);
		setRotationAngle(left_wall_r4, 0, -0.1309F, 0);
		left_wall_r4.setTextureUVOffset(184, 571).addCuboid(0, -16, 0, 0, 16, 81, 0, false);

		right_lower_wall_r4 = createModelPart();
		right_lower_wall_r4.setPivot(-30, 2, 106);
		end_interior.addChild(right_lower_wall_r4);
		setRotationAngle(right_lower_wall_r4, 0, 0.1309F, -0.2967F);
		right_lower_wall_r4.setTextureUVOffset(575, 192).addCuboid(0, 0, 0, 0, 10, 81, 0, false);

		left_lower_wall_r2 = createModelPart();
		left_lower_wall_r2.setPivot(30, 2, 106);
		end_interior.addChild(left_lower_wall_r2);
		setRotationAngle(left_lower_wall_r2, 0, -0.1309F, 0.2967F);
		left_lower_wall_r2.setTextureUVOffset(575, 202).addCuboid(0, 0, 0, 0, 10, 81, 0, false);

		end_light = createModelPart();
		end_light.setPivot(0, 24, 0);


		left_light_r1 = createModelPart();
		left_light_r1.setPivot(21, -17, 0);
		end_light.addChild(left_light_r1);
		setRotationAngle(left_light_r1, 0, 0, -0.5236F);
		left_light_r1.setTextureUVOffset(20, 0).addCuboid(0, 0, 106, 2, 0, 40, 0, false);

		right_light_r1 = createModelPart();
		right_light_r1.setPivot(-21, -17, 0);
		end_light.addChild(right_light_r1);
		setRotationAngle(right_light_r1, 0, 0, 0.5236F);
		right_light_r1.setTextureUVOffset(20, 40).addCuboid(-2, 0, 106, 2, 0, 40, 0, false);

		emergency_exit = createModelPart();
		emergency_exit.setPivot(0, 24, 0);
		emergency_exit.setTextureUVOffset(68, 763).addCuboid(30, -10, -12, 0, 12, 24, 0, false);
		emergency_exit.setTextureUVOffset(511, 690).addCuboid(-30, -10, -12, 0, 12, 24, 0, false);

		right_upper_wall_r5 = createModelPart();
		right_upper_wall_r5.setPivot(-32, -10, 0);
		emergency_exit.addChild(right_upper_wall_r5);
		setRotationAngle(right_upper_wall_r5, 0, 0, 0.5236F);
		right_upper_wall_r5.setTextureUVOffset(220, 661).addCuboid(2, -9, -12, 0, 9, 24, 0, false);

		right_lower_wall_r5 = createModelPart();
		right_lower_wall_r5.setPivot(-30, 2, 0);
		emergency_exit.addChild(right_lower_wall_r5);
		setRotationAngle(right_lower_wall_r5, 0, 0, -0.2967F);
		right_lower_wall_r5.setTextureUVOffset(80, 283).addCuboid(0, 0, -12, 0, 7, 24, 0, false);

		left_upper_wall_r3 = createModelPart();
		left_upper_wall_r3.setPivot(32, -10, 0);
		emergency_exit.addChild(left_upper_wall_r3);
		setRotationAngle(left_upper_wall_r3, 0, 0, -0.5236F);
		left_upper_wall_r3.setTextureUVOffset(184, 753).addCuboid(-2, -9, -12, 0, 9, 24, 0, false);

		left_lower_wall_r3 = createModelPart();
		left_lower_wall_r3.setPivot(30, 2, 0);
		emergency_exit.addChild(left_lower_wall_r3);
		setRotationAngle(left_lower_wall_r3, 0, 0, 0.2967F);
		left_lower_wall_r3.setTextureUVOffset(0, 771).addCuboid(0, 0, -12, 0, 7, 24, 0, false);

		seat_nice = createModelPart();
		seat_nice.setPivot(0, 24, 0);
		seat_nice.setTextureUVOffset(864, 102).addCuboid(-12, 1, -4, 24, 2, 6, 0, false);
		seat_nice.setTextureUVOffset(873, 157).addCuboid(4.5F, -11, 2.5F, 7, 6, 1, 0, false);
		seat_nice.setTextureUVOffset(889, 157).addCuboid(-3.5F, -11, 2.5F, 7, 6, 1, 0, false);
		seat_nice.setTextureUVOffset(905, 157).addCuboid(-11.5F, -11, 2.5F, 7, 6, 1, 0, false);
		seat_nice.setTextureUVOffset(47, 119).addCuboid(11.5F, -2, -3, 1, 1, 5, 0, false);
		seat_nice.setTextureUVOffset(47, 119).addCuboid(3.5F, -2, -3, 1, 1, 5, 0, false);
		seat_nice.setTextureUVOffset(47, 119).addCuboid(-4.5F, -2, -3, 1, 1, 5, 0, false);
		seat_nice.setTextureUVOffset(47, 119).addCuboid(-12.5F, -2, -3, 1, 1, 5, 0, false);

		back_bottom_right_r2 = createModelPart();
		back_bottom_right_r2.setPivot(0, 3, 2);
		seat_nice.addChild(back_bottom_right_r2);
		setRotationAngle(back_bottom_right_r2, -0.1745F, 0, 0);
		back_bottom_right_r2.setTextureUVOffset(907, 87).addCuboid(-11.5F, -11.2F, -1.2F, 7, 11, 1, 0.2F, false);
		back_bottom_right_r2.setTextureUVOffset(891, 87).addCuboid(-3.5F, -11.2F, -1.2F, 7, 11, 1, 0.2F, false);
		back_bottom_right_r2.setTextureUVOffset(875, 87).addCuboid(4.5F, -11.2F, -1.2F, 7, 11, 1, 0.2F, false);

		seat_normal = createModelPart();
		seat_normal.setPivot(0, 24, 0);
		seat_normal.setTextureUVOffset(875, 79).addCuboid(-12, 1, -4, 24, 2, 6, 0, false);
		seat_normal.setTextureUVOffset(825, 157).addCuboid(4.5F, -11, 2.5F, 7, 6, 1, 0, false);
		seat_normal.setTextureUVOffset(841, 157).addCuboid(-3.5F, -11, 2.5F, 7, 6, 1, 0, false);
		seat_normal.setTextureUVOffset(857, 157).addCuboid(-11.5F, -11, 2.5F, 7, 6, 1, 0, false);
		seat_normal.setTextureUVOffset(0, 3).addCuboid(11.5F, -2, -3, 1, 1, 5, 0, false);
		seat_normal.setTextureUVOffset(0, 3).addCuboid(3.5F, -2, -3, 1, 1, 5, 0, false);
		seat_normal.setTextureUVOffset(0, 3).addCuboid(-4.5F, -2, -3, 1, 1, 5, 0, false);
		seat_normal.setTextureUVOffset(0, 3).addCuboid(-12.5F, -2, -3, 1, 1, 5, 0, false);

		back_bottom_right_r3 = createModelPart();
		back_bottom_right_r3.setPivot(0, 3, 2);
		seat_normal.addChild(back_bottom_right_r3);
		setRotationAngle(back_bottom_right_r3, -0.1745F, 0, 0);
		back_bottom_right_r3.setTextureUVOffset(826, 111).addCuboid(-11.5F, -11.2F, -1.2F, 7, 11, 1, 0.2F, false);
		back_bottom_right_r3.setTextureUVOffset(810, 111).addCuboid(-3.5F, -11.2F, -1.2F, 7, 11, 1, 0.2F, false);
		back_bottom_right_r3.setTextureUVOffset(794, 111).addCuboid(4.5F, -11.2F, -1.2F, 7, 11, 1, 0.2F, false);

		buildModel();
	}

	private static final int DOOR_MAX = 16;

	@Override
	public ModelA320 createNew(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		return new ModelA320(doorAnimationType, renderDoorOverlay);
	}

	@Override
	protected void baseTransform(GraphicsHolder graphicsHolder) {
		graphicsHolder.translate(0, -3.5, 2.375);
	}

	@Override
	protected void renderWindowPositions(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
		switch (renderStage) {
			case LIGHT:
				renderOnce(end_light, graphicsHolder, light, position);
				renderMirror(window_interior_blank_light, graphicsHolder, light, position - 194);

				for (int i = 0; i < 7; i++) {
					renderMirror(window_interior_light, graphicsHolder, light, position + i * 16 - 182);
				}

				renderMirror(window_interior_light, graphicsHolder, light, position - 70);
				renderMirror(window_interior_blank_light, graphicsHolder, light, position - 58);

				for (int i = 0; i < 10; i++) {
					renderMirror(window_interior_light, graphicsHolder, light, position + i * 16 - 46);
				}

				break;
			case INTERIOR:
				renderOnce(head_interior, graphicsHolder, light, position);
				renderOnce(end_interior, graphicsHolder, light, position);

				renderMirror(window_interior_blank, graphicsHolder, light, position - 194);
				renderMirror(window_interior_blank_wall, graphicsHolder, light, position - 194);

				for (int i = 0; i < 7; i++) {
					renderMirror(window_interior, graphicsHolder, light, position + i * 16 - 182);
					renderMirror(window_interior_wall, graphicsHolder, light, position + i * 16 - 182);
				}

				renderOnce(emergency_exit, graphicsHolder, light, position - 66);
				renderMirror(window_interior, graphicsHolder, light, position - 70);
				renderMirror(window_interior_blank, graphicsHolder, light, position - 58);

				for (int i = 0; i < 10; i++) {
					renderMirror(window_interior, graphicsHolder, light, position + i * 16 - 46);
					renderMirror(window_interior_wall, graphicsHolder, light, position + i * 16 - 46);
				}

				renderOnce(door_left_interior, graphicsHolder, light, -doorLeftX * 6, position + doorLeftZ);
				renderOnce(door_right_interior, graphicsHolder, light, doorRightX * 6, position + doorRightZ);

				if (renderDetails) {
					for (int i = 0; i < 12; i++) {
						renderOnce(seat_nice, graphicsHolder, light, -16, i * 12 - 189);
						renderOnce(seat_nice, graphicsHolder, light, 16, i * 12 - 189);
					}
					for (int i = 0; i < 18; i++) {
						renderOnce(seat_normal, graphicsHolder, light, -16, i * 11 - 47);
						renderOnce(seat_normal, graphicsHolder, light, 16, i * 11 - 47);
					}
				}

				break;
			case EXTERIOR:
				renderOnce(exterior, graphicsHolder, light, position);
				renderOnce(door_left_exterior, graphicsHolder, light, -doorLeftX * 6, position + doorLeftZ);
				renderOnce(door_right_exterior, graphicsHolder, light, doorRightX * 6, position + doorRightZ);
				break;
		}
	}

	@Override
	protected void renderDoorPositions(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
	}

	@Override
	protected void renderHeadPosition1(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean useHeadlights) {
	}

	@Override
	protected void renderHeadPosition2(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean useHeadlights) {
	}

	@Override
	protected void renderEndPosition1(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
	}

	@Override
	protected void renderEndPosition2(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
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