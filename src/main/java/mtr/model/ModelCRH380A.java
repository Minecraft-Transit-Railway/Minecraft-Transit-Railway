package mtr.model;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class ModelCRH380A extends ModelTrainBase {

	private final ModelPart window;
	private final ModelPart window_exterior;
	private final ModelPart floor_r1;
	private final ModelPart window_exterior_end;
	private final ModelPart floor_4_r1;
	private final ModelPart floor_3_r1;
	private final ModelPart head;
	private final ModelPart aero_4_r1;
	private final ModelPart aero_3_r1;
	private final ModelPart aero_2_r1;
	private final ModelPart aero_1_r1;
	private final ModelPart skirt_2_r1;
	private final ModelPart skirt_1_r1;
	private final ModelPart floor;
	private final ModelPart floor_7_r1;
	private final ModelPart floor_6_r1;
	private final ModelPart floor_5_r1;
	private final ModelPart floor_4_r2;
	private final ModelPart floor_3_r2;
	private final ModelPart floor_2_r1;
	private final ModelPart nose;
	private final ModelPart nose_13_r1;
	private final ModelPart nose_12_r1;
	private final ModelPart nose_11_r1;
	private final ModelPart nose_10_r1;
	private final ModelPart nose_9_r1;
	private final ModelPart nose_8_r1;
	private final ModelPart nose_7_r1;
	private final ModelPart nose_6_r1;
	private final ModelPart nose_5_r1;
	private final ModelPart nose_4_r1;
	private final ModelPart nose_3_r1;
	private final ModelPart nose_1_r1;
	private final ModelPart wall;
	private final ModelPart wall_8_r1;
	private final ModelPart wall_5_r1;
	private final ModelPart wall_4_r1;
	private final ModelPart wall_1_r1;
	private final ModelPart front_panel;
	private final ModelPart front_panel_5_r1;
	private final ModelPart front_panel_4_r1;
	private final ModelPart front_panel_3_r1;
	private final ModelPart front_panel_2_r1;
	private final ModelPart front_panel_1_r1;
	private final ModelPart cockpit;
	private final ModelPart windscreen_3_r1;
	private final ModelPart windscreen_2_r1;
	private final ModelPart windscreen_1_r1;
	private final ModelPart top_3_r1;
	private final ModelPart top_2_r1;
	private final ModelPart door;
	private final ModelPart doorframe;
	private final ModelPart doorframe_3_r1;
	private final ModelPart doorframe_1_r1;
	private final ModelPart door_exterior;
	private final ModelPart floor_2_r2;
	private final ModelPart floor_1_r1;
	private final ModelPart left_door_exterior;
	private final ModelPart right_door_exterior;
	private final ModelPart door_standard;
	private final ModelPart left_door;
	private final ModelPart right_door;
	private final ModelPart door_exterior_end;
	private final ModelPart floor_2_r3;
	private final ModelPart floor_1_r2;
	private final ModelPart left_door_exterior_end;
	private final ModelPart right_door_exterior_end;
	private final ModelPart roof;
	private final ModelPart outer_roof_2_r1;
	private final ModelPart outer_roof_3_r1;
	private final ModelPart outer_roof_4_r1;
	private final ModelPart inner_roof_2_r1;
	private final ModelPart inner_roof_3_r1;
	private final ModelPart roof_exterior;
	private final ModelPart outer_roof_3_r2;
	private final ModelPart outer_roof_4_r2;
	private final ModelPart outer_roof_5_r1;
	private final ModelPart roof_door;
	private final ModelPart inner_roof_3_r2;
	private final ModelPart inner_roof_2_r2;
	private final ModelPart outer_roof_2_r2;
	private final ModelPart outer_roof_1_r1;
	private final ModelPart roof_exterior_door;
	private final ModelPart outer_roof_5_r2;
	private final ModelPart outer_roof_4_r3;
	private final ModelPart outer_roof_3_r3;
	private final ModelPart end;
	private final ModelPart outer_roof_5_r3;
	private final ModelPart outer_roof_4_r4;
	private final ModelPart outer_roof_3_r4;
	private final ModelPart outer_roof_5_r4;
	private final ModelPart outer_roof_4_r5;
	private final ModelPart outer_roof_3_r5;
	private final ModelPart luggage_rack;
	private final ModelPart seat;
	private final ModelPart top_right_r1;
	private final ModelPart top_left_r1;
	private final ModelPart back_right_r1;
	private final ModelPart back_left_r1;
	private final ModelPart back_r1;
	private final ModelPart head_wall;
	private final ModelPart end_door;
	private final ModelPart headlights;
	private final ModelPart headlights_r1;
	private final ModelPart taillights;
	private final ModelPart taillights_r1;
	public ModelCRH380A() {
		textureWidth = 944;
		textureHeight = 944;
		window = new ModelPart(this);
		window.setPivot(0.0F, 24.0F, 0.0F);
		window.setTextureOffset(0, 0).addCuboid(-21.0F, -32.0F, 0.0F, 3.0F, 32.0F, 39.0F, 0.0F, false);
		window.setTextureOffset(0, 0).addCuboid(-21.0F, -32.0F, -39.0F, 3.0F, 32.0F, 39.0F, 0.0F, false);
		window.setTextureOffset(207, 63).addCuboid(-20.0F, -1.0F, 0.0F, 20.0F, 1.0F, 39.0F, 0.0F, false);
		window.setTextureOffset(207, 63).addCuboid(-20.0F, -1.0F, -39.0F, 20.0F, 1.0F, 39.0F, 0.0F, false);

		window_exterior = new ModelPart(this);
		window_exterior.setPivot(0.0F, 24.0F, 0.0F);
		window_exterior.setTextureOffset(0, 0).addCuboid(-21.0F, -37.0F, -156.0F, 0.0F, 37.0F, 312.0F, 0.0F, false);

		floor_r1 = new ModelPart(this);
		floor_r1.setPivot(-21.0F, 0.0F, 91.0F);
		window_exterior.addChild(floor_r1);
		setRotationAngle(floor_r1, 0.0F, 0.0F, -0.1745F);
		floor_r1.setTextureOffset(312, 37).addCuboid(0.0F, 0.0F, -247.0F, 1.0F, 8.0F, 312.0F, 0.0F, false);

		window_exterior_end = new ModelPart(this);
		window_exterior_end.setPivot(0.0F, 24.0F, 0.0F);
		window_exterior_end.setTextureOffset(0, 57).addCuboid(-21.0F, -37.0F, 0.0F, 0.0F, 37.0F, 78.0F, 0.0F, false);
		window_exterior_end.setTextureOffset(0, 57).addCuboid(21.0F, -37.0F, 0.0F, 0.0F, 37.0F, 78.0F, 0.0F, true);
		window_exterior_end.setTextureOffset(0, 0).addCuboid(-21.0F, -37.0F, 118.0F, 0.0F, 37.0F, 234.0F, 0.0F, false);
		window_exterior_end.setTextureOffset(0, 0).addCuboid(21.0F, -37.0F, 118.0F, 0.0F, 37.0F, 234.0F, 0.0F, false);

		floor_4_r1 = new ModelPart(this);
		floor_4_r1.setPivot(21.0F, 0.0F, 118.0F);
		window_exterior_end.addChild(floor_4_r1);
		setRotationAngle(floor_4_r1, 0.0F, 0.0F, 0.1745F);
		floor_4_r1.setTextureOffset(0, 349).addCuboid(-1.0F, 0.0F, 0.0F, 1.0F, 8.0F, 234.0F, 0.0F, false);
		floor_4_r1.setTextureOffset(88, 0).addCuboid(-1.0F, 0.0F, -118.0F, 1.0F, 8.0F, 78.0F, 0.0F, true);

		floor_3_r1 = new ModelPart(this);
		floor_3_r1.setPivot(-21.0F, 0.0F, 118.0F);
		window_exterior_end.addChild(floor_3_r1);
		setRotationAngle(floor_3_r1, 0.0F, 0.0F, -0.1745F);
		floor_3_r1.setTextureOffset(0, 349).addCuboid(0.0F, 0.0F, 0.0F, 1.0F, 8.0F, 234.0F, 0.0F, false);
		floor_3_r1.setTextureOffset(88, 0).addCuboid(0.0F, 0.0F, -118.0F, 1.0F, 8.0F, 78.0F, 0.0F, false);

		head = new ModelPart(this);
		head.setPivot(0.0F, 24.0F, 0.0F);


		aero_4_r1 = new ModelPart(this);
		aero_4_r1.setPivot(-21.0F, -5.0F, -45.0F);
		head.addChild(aero_4_r1);
		setRotationAngle(aero_4_r1, -0.0873F, -0.1396F, 1.1781F);
		aero_4_r1.setTextureOffset(90, 55).addCuboid(0.0F, -7.0F, -44.0F, 0.0F, 7.0F, 44.0F, 0.0F, false);

		aero_3_r1 = new ModelPart(this);
		aero_3_r1.setPivot(-21.0F, -5.0F, -24.0F);
		head.addChild(aero_3_r1);
		setRotationAngle(aero_3_r1, 0.0F, 0.0F, 1.1781F);
		aero_3_r1.setTextureOffset(0, 49).addCuboid(0.0F, -7.0F, -21.0F, 0.0F, 7.0F, 45.0F, 0.0F, false);

		aero_2_r1 = new ModelPart(this);
		aero_2_r1.setPivot(21.0F, -5.0F, -45.0F);
		head.addChild(aero_2_r1);
		setRotationAngle(aero_2_r1, -0.0873F, 0.1396F, -1.1781F);
		aero_2_r1.setTextureOffset(90, 55).addCuboid(0.0F, -7.0F, -44.0F, 0.0F, 7.0F, 44.0F, 0.0F, false);

		aero_1_r1 = new ModelPart(this);
		aero_1_r1.setPivot(21.0F, -5.0F, -24.0F);
		head.addChild(aero_1_r1);
		setRotationAngle(aero_1_r1, 0.0F, 0.0F, -1.1781F);
		aero_1_r1.setTextureOffset(0, 49).addCuboid(0.0F, -7.0F, -21.0F, 0.0F, 7.0F, 45.0F, 0.0F, false);

		skirt_2_r1 = new ModelPart(this);
		skirt_2_r1.setPivot(21.0F, 0.0F, 0.0F);
		head.addChild(skirt_2_r1);
		setRotationAngle(skirt_2_r1, 0.0436F, 0.2443F, 0.1745F);
		skirt_2_r1.setTextureOffset(0, 0).addCuboid(-1.0F, 0.0F, -86.0F, 1.0F, 8.0F, 86.0F, 0.0F, false);

		skirt_1_r1 = new ModelPart(this);
		skirt_1_r1.setPivot(-21.0F, 0.0F, 0.0F);
		head.addChild(skirt_1_r1);
		setRotationAngle(skirt_1_r1, 0.0436F, -0.2443F, -0.1745F);
		skirt_1_r1.setTextureOffset(0, 0).addCuboid(0.0F, 0.0F, -86.0F, 1.0F, 8.0F, 86.0F, 0.0F, true);

		floor = new ModelPart(this);
		floor.setPivot(0.0F, -14.0F, -114.0F);
		head.addChild(floor);
		floor.setTextureOffset(110, 135).addCuboid(-21.0F, 13.0F, 68.0F, 42.0F, 1.0F, 46.0F, 0.0F, false);
		floor.setTextureOffset(66, 199).addCuboid(-4.0F, 13.0F, 12.0F, 8.0F, 1.0F, 22.0F, 0.0F, false);

		floor_7_r1 = new ModelPart(this);
		floor_7_r1.setPivot(0.0F, 14.0F, 35.0F);
		floor.addChild(floor_7_r1);
		setRotationAngle(floor_7_r1, 0.0F, -1.4312F, 0.0F);
		floor_7_r1.setTextureOffset(177, 193).addCuboid(-10.0F, -1.0F, -16.0F, 47.0F, 1.0F, 21.0F, 0.0F, false);

		floor_6_r1 = new ModelPart(this);
		floor_6_r1.setPivot(0.0F, 14.0F, 35.0F);
		floor.addChild(floor_6_r1);
		setRotationAngle(floor_6_r1, 0.0F, -1.1432F, 0.0F);
		floor_6_r1.setTextureOffset(168, 63).addCuboid(-13.0F, -1.0F, -18.0F, 8.0F, 1.0F, 11.0F, 0.0F, false);

		floor_5_r1 = new ModelPart(this);
		floor_5_r1.setPivot(0.0F, 14.0F, 35.0F);
		floor.addChild(floor_5_r1);
		setRotationAngle(floor_5_r1, 0.0F, -0.5236F, 0.0F);
		floor_5_r1.setTextureOffset(108, 70).addCuboid(-8.0F, -1.0F, -22.0F, 8.0F, 1.0F, 6.0F, 0.0F, false);

		floor_4_r2 = new ModelPart(this);
		floor_4_r2.setPivot(0.0F, 14.0F, 35.0F);
		floor.addChild(floor_4_r2);
		setRotationAngle(floor_4_r2, 0.0F, 1.4312F, 0.0F);
		floor_4_r2.setTextureOffset(177, 193).addCuboid(-37.0F, -1.0F, -16.0F, 47.0F, 1.0F, 21.0F, 0.0F, false);

		floor_3_r2 = new ModelPart(this);
		floor_3_r2.setPivot(0.0F, 14.0F, 35.0F);
		floor.addChild(floor_3_r2);
		setRotationAngle(floor_3_r2, 0.0F, 1.1432F, 0.0F);
		floor_3_r2.setTextureOffset(168, 63).addCuboid(5.0F, -1.0F, -18.0F, 8.0F, 1.0F, 11.0F, 0.0F, false);

		floor_2_r1 = new ModelPart(this);
		floor_2_r1.setPivot(0.0F, 14.0F, 35.0F);
		floor.addChild(floor_2_r1);
		setRotationAngle(floor_2_r1, 0.0F, 0.5236F, 0.0F);
		floor_2_r1.setTextureOffset(108, 70).addCuboid(0.0F, -1.0F, -22.0F, 8.0F, 1.0F, 6.0F, 0.0F, false);

		nose = new ModelPart(this);
		nose.setPivot(0.0F, -14.0F, -114.0F);
		head.addChild(nose);
		nose.setTextureOffset(60, 199).addCuboid(-4.0F, 5.0F, 8.0F, 8.0F, 6.0F, 0.0F, 0.0F, false);

		nose_13_r1 = new ModelPart(this);
		nose_13_r1.setPivot(0.0F, 12.0F, 13.0F);
		nose.addChild(nose_13_r1);
		setRotationAngle(nose_13_r1, 0.0F, 1.3963F, 0.0F);
		nose_13_r1.setTextureOffset(195, 63).addCuboid(-15.0F, -8.0F, -13.0F, 12.0F, 9.0F, 0.0F, 0.0F, true);

		nose_12_r1 = new ModelPart(this);
		nose_12_r1.setPivot(0.0F, 12.0F, 13.0F);
		nose.addChild(nose_12_r1);
		setRotationAngle(nose_12_r1, 0.6597F, 1.1345F, 0.0F);
		nose_12_r1.setTextureOffset(198, 182).addCuboid(-17.0F, -9.0F, -8.0F, 11.0F, 6.0F, 0.0F, 0.0F, true);

		nose_11_r1 = new ModelPart(this);
		nose_11_r1.setPivot(0.0F, 12.0F, 13.0F);
		nose.addChild(nose_11_r1);
		setRotationAngle(nose_11_r1, 0.0F, 0.5236F, 0.0F);
		nose_11_r1.setTextureOffset(178, 100).addCuboid(-12.0F, -7.0F, -6.0F, 12.0F, 6.0F, 0.0F, 0.0F, true);

		nose_10_r1 = new ModelPart(this);
		nose_10_r1.setPivot(0.0F, 12.0F, 13.0F);
		nose.addChild(nose_10_r1);
		setRotationAngle(nose_10_r1, -0.7854F, 0.5236F, 0.0F);
		nose_10_r1.setTextureOffset(136, 73).addCuboid(-12.0F, -5.0F, -9.0F, 11.0F, 5.0F, 0.0F, 0.0F, true);

		nose_9_r1 = new ModelPart(this);
		nose_9_r1.setPivot(0.0F, 12.0F, 13.0F);
		nose.addChild(nose_9_r1);
		setRotationAngle(nose_9_r1, 0.829F, 0.5236F, 0.0F);
		nose_9_r1.setTextureOffset(0, 199).addCuboid(-12.0F, -6.0F, -3.0F, 11.0F, 6.0F, 0.0F, 0.0F, true);

		nose_8_r1 = new ModelPart(this);
		nose_8_r1.setPivot(0.0F, 12.0F, 13.0F);
		nose.addChild(nose_8_r1);
		setRotationAngle(nose_8_r1, 0.6597F, -1.1345F, 0.0F);
		nose_8_r1.setTextureOffset(198, 182).addCuboid(6.0F, -9.0F, -8.0F, 11.0F, 6.0F, 0.0F, 0.0F, false);

		nose_7_r1 = new ModelPart(this);
		nose_7_r1.setPivot(0.0F, 12.0F, 13.0F);
		nose.addChild(nose_7_r1);
		setRotationAngle(nose_7_r1, 0.0F, -1.3963F, 0.0F);
		nose_7_r1.setTextureOffset(195, 63).addCuboid(3.0F, -8.0F, -13.0F, 12.0F, 9.0F, 0.0F, 0.0F, false);

		nose_6_r1 = new ModelPart(this);
		nose_6_r1.setPivot(0.0F, 12.0F, 13.0F);
		nose.addChild(nose_6_r1);
		setRotationAngle(nose_6_r1, -0.7854F, -0.5236F, 0.0F);
		nose_6_r1.setTextureOffset(136, 73).addCuboid(1.0F, -5.0F, -9.0F, 11.0F, 5.0F, 0.0F, 0.0F, false);

		nose_5_r1 = new ModelPart(this);
		nose_5_r1.setPivot(0.0F, 12.0F, 13.0F);
		nose.addChild(nose_5_r1);
		setRotationAngle(nose_5_r1, 0.0F, -0.5236F, 0.0F);
		nose_5_r1.setTextureOffset(178, 100).addCuboid(0.0F, -7.0F, -6.0F, 12.0F, 6.0F, 0.0F, 0.0F, false);

		nose_4_r1 = new ModelPart(this);
		nose_4_r1.setPivot(0.0F, 12.0F, 13.0F);
		nose.addChild(nose_4_r1);
		setRotationAngle(nose_4_r1, 0.829F, -0.5236F, 0.0F);
		nose_4_r1.setTextureOffset(0, 199).addCuboid(1.0F, -6.0F, -3.0F, 11.0F, 6.0F, 0.0F, 0.0F, false);

		nose_3_r1 = new ModelPart(this);
		nose_3_r1.setPivot(0.0F, 12.0F, 13.0F);
		nose.addChild(nose_3_r1);
		setRotationAngle(nose_3_r1, -0.829F, 0.0F, 0.0F);
		nose_3_r1.setTextureOffset(198, 188).addCuboid(-4.0F, -6.0F, -8.0F, 8.0F, 5.0F, 0.0F, 0.0F, false);

		nose_1_r1 = new ModelPart(this);
		nose_1_r1.setPivot(0.0F, 12.0F, 13.0F);
		nose.addChild(nose_1_r1);
		setRotationAngle(nose_1_r1, 0.829F, 0.0F, 0.0F);
		nose_1_r1.setTextureOffset(0, 205).addCuboid(-4.0F, -5.0F, -2.0F, 8.0F, 6.0F, 0.0F, 0.0F, false);

		wall = new ModelPart(this);
		wall.setPivot(21.0F, 0.0F, 0.0F);
		head.addChild(wall);
		wall.setTextureOffset(0, 118).addCuboid(-6.0F, -28.0F, -91.0F, 0.0F, 27.0F, 54.0F, 0.0F, false);
		wall.setTextureOffset(90, 49).addCuboid(0.0F, -5.0F, -45.0F, 0.0F, 5.0F, 45.0F, 0.0F, false);
		wall.setTextureOffset(0, 118).addCuboid(-36.0F, -28.0F, -91.0F, 0.0F, 27.0F, 54.0F, 0.0F, false);
		wall.setTextureOffset(90, 49).addCuboid(-42.0F, -5.0F, -45.0F, 0.0F, 5.0F, 45.0F, 0.0F, false);

		wall_8_r1 = new ModelPart(this);
		wall_8_r1.setPivot(-42.0F, 0.0F, -45.0F);
		wall.addChild(wall_8_r1);
		setRotationAngle(wall_8_r1, 0.0F, -0.1309F, 0.0F);
		wall_8_r1.setTextureOffset(0, 57).addCuboid(0.0F, -5.0F, -45.0F, 0.0F, 5.0F, 45.0F, 0.0F, false);

		wall_5_r1 = new ModelPart(this);
		wall_5_r1.setPivot(-42.0F, 0.0F, 0.0F);
		wall.addChild(wall_5_r1);
		setRotationAngle(wall_5_r1, 0.0F, -0.1309F, 0.0F);
		wall_5_r1.setTextureOffset(108, 137).addCuboid(0.0F, -37.0F, -45.0F, 0.0F, 32.0F, 45.0F, 0.0F, false);

		wall_4_r1 = new ModelPart(this);
		wall_4_r1.setPivot(0.0F, 0.0F, -45.0F);
		wall.addChild(wall_4_r1);
		setRotationAngle(wall_4_r1, 0.0F, 0.1309F, 0.0F);
		wall_4_r1.setTextureOffset(0, 56).addCuboid(0.0F, -5.0F, -45.0F, 0.0F, 5.0F, 45.0F, 0.0F, false);

		wall_1_r1 = new ModelPart(this);
		wall_1_r1.setPivot(0.0F, 0.0F, 0.0F);
		wall.addChild(wall_1_r1);
		setRotationAngle(wall_1_r1, 0.0F, 0.1309F, 0.0F);
		wall_1_r1.setTextureOffset(108, 137).addCuboid(0.0F, -37.0F, -45.0F, 0.0F, 32.0F, 45.0F, 0.0F, false);

		front_panel = new ModelPart(this);
		front_panel.setPivot(21.0F, 0.0F, 0.0F);
		head.addChild(front_panel);


		front_panel_5_r1 = new ModelPart(this);
		front_panel_5_r1.setPivot(-36.0F, -11.0F, -97.0F);
		front_panel.addChild(front_panel_5_r1);
		setRotationAngle(front_panel_5_r1, 0.2182F, -0.2618F, 0.829F);
		front_panel_5_r1.setTextureOffset(0, 17).addCuboid(2.0F, -4.0F, -4.0F, 0.0F, 13.0F, 105.0F, 0.0F, false);

		front_panel_4_r1 = new ModelPart(this);
		front_panel_4_r1.setPivot(-33.0F, -14.0F, -101.0F);
		front_panel.addChild(front_panel_4_r1);
		setRotationAngle(front_panel_4_r1, 0.0547F, -0.2913F, 1.3722F);
		front_panel_4_r1.setTextureOffset(0, 0).addCuboid(3.214F, -9.036F, -2.192F, 0.0F, 16.0F, 106.0F, 0.0F, false);

		front_panel_3_r1 = new ModelPart(this);
		front_panel_3_r1.setPivot(-21.0F, -14.0F, -114.0F);
		front_panel.addChild(front_panel_3_r1);
		setRotationAngle(front_panel_3_r1, 0.2967F, 0.0F, 0.0F);
		front_panel_3_r1.setTextureOffset(46, 0).addCuboid(-7.0F, 6.0F, 10.0F, 14.0F, 0.0F, 42.0F, 0.0F, false);

		front_panel_2_r1 = new ModelPart(this);
		front_panel_2_r1.setPivot(-9.0F, -14.0F, -101.0F);
		front_panel.addChild(front_panel_2_r1);
		setRotationAngle(front_panel_2_r1, 0.0547F, 0.2913F, -1.3722F);
		front_panel_2_r1.setTextureOffset(0, 0).addCuboid(-3.214F, -9.036F, -2.192F, 0.0F, 16.0F, 106.0F, 0.0F, false);

		front_panel_1_r1 = new ModelPart(this);
		front_panel_1_r1.setPivot(-6.0F, -11.0F, -97.0F);
		front_panel.addChild(front_panel_1_r1);
		setRotationAngle(front_panel_1_r1, 0.2182F, 0.2618F, -0.829F);
		front_panel_1_r1.setTextureOffset(0, 17).addCuboid(-2.0F, -4.0F, -4.0F, 0.0F, 13.0F, 105.0F, 0.0F, false);

		cockpit = new ModelPart(this);
		cockpit.setPivot(23.0F, 0.0F, 0.0F);
		head.addChild(cockpit);
		cockpit.setTextureOffset(92, 42).addCuboid(-28.0F, -42.0F, -28.0F, 10.0F, 0.0F, 28.0F, 0.0F, false);

		windscreen_3_r1 = new ModelPart(this);
		windscreen_3_r1.setPivot(-28.0F, -42.0F, -28.0F);
		cockpit.addChild(windscreen_3_r1);
		setRotationAngle(windscreen_3_r1, 0.3527F, 0.3326F, -0.7256F);
		windscreen_3_r1.setTextureOffset(74, 0).addCuboid(-13.0F, 0.0F, -42.0F, 13.0F, 0.0F, 42.0F, 0.0F, true);

		windscreen_2_r1 = new ModelPart(this);
		windscreen_2_r1.setPivot(-18.0F, -42.0F, -28.0F);
		cockpit.addChild(windscreen_2_r1);
		setRotationAngle(windscreen_2_r1, 0.3527F, -0.3326F, 0.7256F);
		windscreen_2_r1.setTextureOffset(74, 0).addCuboid(0.0F, 0.0F, -42.0F, 13.0F, 0.0F, 42.0F, 0.0F, false);

		windscreen_1_r1 = new ModelPart(this);
		windscreen_1_r1.setPivot(-23.0F, -42.0F, -28.0F);
		cockpit.addChild(windscreen_1_r1);
		setRotationAngle(windscreen_1_r1, 0.48F, 0.0F, 0.0F);
		windscreen_1_r1.setTextureOffset(100, 0).addCuboid(-5.0F, 0.0F, -42.0F, 10.0F, 0.0F, 42.0F, 0.0F, false);

		top_3_r1 = new ModelPart(this);
		top_3_r1.setPivot(-28.0F, -42.0F, -14.0F);
		cockpit.addChild(top_3_r1);
		setRotationAngle(top_3_r1, 0.0F, 0.0F, -0.7418F);
		top_3_r1.setTextureOffset(60, 42).addCuboid(-16.0F, 0.0F, -14.0F, 16.0F, 0.0F, 28.0F, 0.0F, true);

		top_2_r1 = new ModelPart(this);
		top_2_r1.setPivot(-18.0F, -42.0F, -14.0F);
		cockpit.addChild(top_2_r1);
		setRotationAngle(top_2_r1, 0.0F, 0.0F, 0.7418F);
		top_2_r1.setTextureOffset(60, 42).addCuboid(0.0F, 0.0F, -14.0F, 16.0F, 0.0F, 28.0F, 0.0F, false);

		door = new ModelPart(this);
		door.setPivot(0.0F, 24.0F, 0.0F);
		door.setTextureOffset(301, 79).addCuboid(-21.0F, -37.0F, -20.0F, 4.0F, 37.0F, 24.0F, 0.0F, false);
		door.setTextureOffset(301, 79).addCuboid(17.0F, -37.0F, -20.0F, 4.0F, 37.0F, 24.0F, 0.0F, true);
		door.setTextureOffset(168, 0).addCuboid(-21.0F, -1.0F, -20.0F, 21.0F, 1.0F, 40.0F, 0.0F, false);
		door.setTextureOffset(168, 0).addCuboid(0.0F, -1.0F, -20.0F, 21.0F, 1.0F, 40.0F, 0.0F, true);
		door.setTextureOffset(333, 49).addCuboid(-21.0F, -42.0F, 17.0F, 15.0F, 42.0F, 3.0F, 0.0F, false);
		door.setTextureOffset(334, 140).addCuboid(-21.0F, -41.0F, -20.0F, 15.0F, 41.0F, 2.0F, 0.0F, false);
		door.setTextureOffset(168, 53).addCuboid(-6.0F, -41.0F, 18.0F, 6.0F, 6.0F, 2.0F, 0.0F, false);
		door.setTextureOffset(44, 199).addCuboid(-6.0F, -41.0F, -20.0F, 6.0F, 6.0F, 2.0F, 0.0F, false);
		door.setTextureOffset(333, 49).addCuboid(6.0F, -42.0F, 17.0F, 15.0F, 42.0F, 3.0F, 0.0F, true);
		door.setTextureOffset(334, 140).addCuboid(6.0F, -41.0F, -20.0F, 15.0F, 41.0F, 2.0F, 0.0F, true);
		door.setTextureOffset(168, 53).addCuboid(0.0F, -41.0F, 18.0F, 6.0F, 6.0F, 2.0F, 0.0F, true);
		door.setTextureOffset(44, 199).addCuboid(0.0F, -41.0F, -20.0F, 6.0F, 6.0F, 2.0F, 0.0F, true);
		door.setTextureOffset(0, 349).addCuboid(-17.0F, -41.0F, -1.0F, 11.0F, 41.0F, 1.0F, 0.0F, false);
		door.setTextureOffset(313, 172).addCuboid(-7.0F, -41.0F, -20.0F, 1.0F, 41.0F, 19.0F, 0.0F, false);
		door.setTextureOffset(0, 349).addCuboid(6.0F, -41.0F, -1.0F, 11.0F, 41.0F, 1.0F, 0.0F, true);
		door.setTextureOffset(313, 172).addCuboid(6.0F, -41.0F, -20.0F, 1.0F, 41.0F, 19.0F, 0.0F, true);

		doorframe = new ModelPart(this);
		doorframe.setPivot(0.0F, 0.0F, 0.0F);
		door.addChild(doorframe);
		doorframe.setTextureOffset(11, 0).addCuboid(-21.0F, -34.0F, 4.0F, 3.0F, 0.0F, 13.0F, 0.0F, false);
		doorframe.setTextureOffset(11, 0).addCuboid(17.0F, -34.0F, 4.0F, 3.0F, 0.0F, 13.0F, 0.0F, false);

		doorframe_3_r1 = new ModelPart(this);
		doorframe_3_r1.setPivot(17.0F, -34.0F, 107.0F);
		doorframe.addChild(doorframe_3_r1);
		setRotationAngle(doorframe_3_r1, 0.0F, 0.0F, -0.3927F);
		doorframe_3_r1.setTextureOffset(88, 57).addCuboid(0.0F, -3.0F, -103.0F, 0.0F, 3.0F, 13.0F, 0.0F, false);

		doorframe_1_r1 = new ModelPart(this);
		doorframe_1_r1.setPivot(-18.0F, -34.0F, 107.0F);
		doorframe.addChild(doorframe_1_r1);
		setRotationAngle(doorframe_1_r1, 0.0F, 0.0F, 0.3927F);
		doorframe_1_r1.setTextureOffset(88, 57).addCuboid(0.0F, -3.0F, -103.0F, 0.0F, 3.0F, 13.0F, 0.0F, false);

		door_exterior = new ModelPart(this);
		door_exterior.setPivot(0.0F, 24.0F, 0.0F);
		door_exterior.setTextureOffset(200, 247).addCuboid(-21.0F, -37.0F, -20.0F, 0.0F, 37.0F, 24.0F, 0.0F, false);
		door_exterior.setTextureOffset(246, 140).addCuboid(-21.0F, -37.0F, 17.0F, 0.0F, 37.0F, 3.0F, 0.0F, false);
		door_exterior.setTextureOffset(88, 60).addCuboid(-21.0F, -37.0F, 4.0F, 0.0F, 3.0F, 13.0F, 0.0F, false);
		door_exterior.setTextureOffset(200, 247).addCuboid(21.0F, -37.0F, -20.0F, 0.0F, 37.0F, 24.0F, 0.0F, true);
		door_exterior.setTextureOffset(246, 140).addCuboid(21.0F, -37.0F, 17.0F, 0.0F, 37.0F, 3.0F, 0.0F, true);
		door_exterior.setTextureOffset(88, 60).addCuboid(21.0F, -37.0F, 4.0F, 0.0F, 3.0F, 13.0F, 0.0F, true);
		door_exterior.setTextureOffset(0, 392).addCuboid(-21.0F, -1.0F, 4.0F, 1.0F, 1.0F, 13.0F, 0.0F, false);
		door_exterior.setTextureOffset(0, 392).addCuboid(20.0F, -1.0F, 4.0F, 1.0F, 1.0F, 13.0F, 0.0F, true);

		floor_2_r2 = new ModelPart(this);
		floor_2_r2.setPivot(21.0F, 0.0F, -98.0F);
		door_exterior.addChild(floor_2_r2);
		setRotationAngle(floor_2_r2, 0.0F, 0.0F, 0.1745F);
		floor_2_r2.setTextureOffset(283, 1).addCuboid(-1.0F, 0.0F, 78.0F, 1.0F, 8.0F, 40.0F, 0.0F, false);

		floor_1_r1 = new ModelPart(this);
		floor_1_r1.setPivot(-21.0F, 0.0F, -98.0F);
		door_exterior.addChild(floor_1_r1);
		setRotationAngle(floor_1_r1, 0.0F, 0.0F, -0.1745F);
		floor_1_r1.setTextureOffset(283, 1).addCuboid(0.0F, 0.0F, 78.0F, 1.0F, 8.0F, 40.0F, 0.0F, false);

		left_door_exterior = new ModelPart(this);
		left_door_exterior.setPivot(0.0F, 0.0F, 0.0F);
		door_exterior.addChild(left_door_exterior);
		left_door_exterior.setTextureOffset(391, 3).addCuboid(20.0F, -34.0F, 4.0F, 0.0F, 33.0F, 13.0F, 0.0F, true);

		right_door_exterior = new ModelPart(this);
		right_door_exterior.setPivot(0.0F, 0.0F, 0.0F);
		door_exterior.addChild(right_door_exterior);
		right_door_exterior.setTextureOffset(391, 3).addCuboid(-20.0F, -34.0F, 4.0F, 0.0F, 33.0F, 13.0F, 0.0F, false);

		door_standard = new ModelPart(this);
		door_standard.setPivot(0.0F, 24.0F, 0.0F);


		left_door = new ModelPart(this);
		left_door.setPivot(0.0F, 0.0F, 0.0F);
		door_standard.addChild(left_door);
		left_door.setTextureOffset(288, 137).addCuboid(19.0F, -34.0F, 4.0F, 1.0F, 33.0F, 13.0F, 0.0F, true);

		right_door = new ModelPart(this);
		right_door.setPivot(0.0F, 0.0F, 0.0F);
		door_standard.addChild(right_door);
		right_door.setTextureOffset(288, 137).addCuboid(-20.0F, -34.0F, 4.0F, 1.0F, 33.0F, 13.0F, 0.0F, false);

		door_exterior_end = new ModelPart(this);
		door_exterior_end.setPivot(0.0F, 24.0F, 0.0F);
		door_exterior_end.setTextureOffset(152, 247).addCuboid(-21.0F, -37.0F, -20.0F, 0.0F, 37.0F, 24.0F, 0.0F, false);
		door_exterior_end.setTextureOffset(240, 140).addCuboid(-21.0F, -37.0F, 17.0F, 0.0F, 37.0F, 3.0F, 0.0F, false);
		door_exterior_end.setTextureOffset(0, 21).addCuboid(-21.0F, -37.0F, 4.0F, 0.0F, 3.0F, 13.0F, 0.0F, false);
		door_exterior_end.setTextureOffset(152, 247).addCuboid(21.0F, -37.0F, -20.0F, 0.0F, 37.0F, 24.0F, 0.0F, true);
		door_exterior_end.setTextureOffset(240, 140).addCuboid(21.0F, -37.0F, 17.0F, 0.0F, 37.0F, 3.0F, 0.0F, true);
		door_exterior_end.setTextureOffset(0, 21).addCuboid(21.0F, -37.0F, 4.0F, 0.0F, 3.0F, 13.0F, 0.0F, true);
		door_exterior_end.setTextureOffset(0, 391).addCuboid(-21.0F, -1.0F, 4.0F, 1.0F, 1.0F, 13.0F, 0.0F, false);
		door_exterior_end.setTextureOffset(0, 391).addCuboid(20.0F, -1.0F, 4.0F, 1.0F, 1.0F, 13.0F, 0.0F, true);

		floor_2_r3 = new ModelPart(this);
		floor_2_r3.setPivot(21.0F, 0.0F, -98.0F);
		door_exterior_end.addChild(floor_2_r3);
		setRotationAngle(floor_2_r3, 0.0F, 0.0F, 0.1745F);
		floor_2_r3.setTextureOffset(246, 143).addCuboid(-1.0F, 0.0F, 78.0F, 1.0F, 8.0F, 40.0F, 0.0F, true);

		floor_1_r2 = new ModelPart(this);
		floor_1_r2.setPivot(-21.0F, 0.0F, -98.0F);
		door_exterior_end.addChild(floor_1_r2);
		setRotationAngle(floor_1_r2, 0.0F, 0.0F, -0.1745F);
		floor_1_r2.setTextureOffset(246, 143).addCuboid(0.0F, 0.0F, 78.0F, 1.0F, 8.0F, 40.0F, 0.0F, false);

		left_door_exterior_end = new ModelPart(this);
		left_door_exterior_end.setPivot(0.0F, 0.0F, 0.0F);
		door_exterior_end.addChild(left_door_exterior_end);
		left_door_exterior_end.setTextureOffset(365, 3).addCuboid(20.0F, -34.0F, 4.0F, 0.0F, 33.0F, 13.0F, 0.0F, true);

		right_door_exterior_end = new ModelPart(this);
		right_door_exterior_end.setPivot(0.0F, 0.0F, 0.0F);
		door_exterior_end.addChild(right_door_exterior_end);
		right_door_exterior_end.setTextureOffset(365, 3).addCuboid(-20.0F, -34.0F, 4.0F, 0.0F, 33.0F, 13.0F, 0.0F, false);

		roof = new ModelPart(this);
		roof.setPivot(0.0F, 24.0F, 0.0F);
		roof.setTextureOffset(240, 103).addCuboid(-18.0F, -32.0F, 0.0F, 8.0F, 1.0F, 39.0F, 0.0F, false);
		roof.setTextureOffset(249, 0).addCuboid(-13.0F, -39.0F, 0.0F, 3.0F, 0.0F, 39.0F, 0.0F, false);
		roof.setTextureOffset(0, 45).addCuboid(-18.0F, -34.0F, 0.0F, 0.0F, 2.0F, 39.0F, 0.0F, false);
		roof.setTextureOffset(223, 103).addCuboid(-4.0F, -40.6679F, 0.0F, 4.0F, 0.0F, 39.0F, 0.0F, false);
		roof.setTextureOffset(0, 271).addCuboid(-11.0F, -40.0F, 0.0F, 1.0F, 1.0F, 39.0F, 0.0F, false);
		roof.setTextureOffset(0, 271).addCuboid(-11.0F, -40.0F, -39.0F, 1.0F, 1.0F, 39.0F, 0.0F, false);
		roof.setTextureOffset(223, 103).addCuboid(-4.0F, -40.6679F, -39.0F, 4.0F, 0.0F, 39.0F, 0.0F, false);
		roof.setTextureOffset(0, 45).addCuboid(-18.0F, -34.0F, -39.0F, 0.0F, 2.0F, 39.0F, 0.0F, false);
		roof.setTextureOffset(249, 0).addCuboid(-13.0F, -39.0F, -39.0F, 3.0F, 0.0F, 39.0F, 0.0F, false);
		roof.setTextureOffset(240, 103).addCuboid(-18.0F, -32.0F, -39.0F, 8.0F, 1.0F, 39.0F, 0.0F, false);

		outer_roof_2_r1 = new ModelPart(this);
		outer_roof_2_r1.setPivot(-13.0F, -39.0F, 0.0F);
		roof.addChild(outer_roof_2_r1);
		setRotationAngle(outer_roof_2_r1, 0.0F, 0.0F, -0.3927F);
		outer_roof_2_r1.setTextureOffset(247, 63).addCuboid(-3.0F, 0.0F, -39.0F, 3.0F, 0.0F, 39.0F, 0.0F, false);
		outer_roof_2_r1.setTextureOffset(247, 63).addCuboid(-3.0F, 0.0F, 0.0F, 3.0F, 0.0F, 39.0F, 0.0F, false);

		outer_roof_3_r1 = new ModelPart(this);
		outer_roof_3_r1.setPivot(-14.0F, -39.0F, 0.0F);
		roof.addChild(outer_roof_3_r1);
		setRotationAngle(outer_roof_3_r1, 0.0F, 0.0F, -0.7854F);
		outer_roof_3_r1.setTextureOffset(243, 0).addCuboid(-4.1522F, -0.2346F, -39.0F, 3.0F, 0.0F, 39.0F, 0.0F, false);
		outer_roof_3_r1.setTextureOffset(243, 0).addCuboid(-4.1522F, -0.2346F, 0.0F, 3.0F, 0.0F, 39.0F, 0.0F, false);

		outer_roof_4_r1 = new ModelPart(this);
		outer_roof_4_r1.setPivot(-18.0F, -34.0F, 0.0F);
		roof.addChild(outer_roof_4_r1);
		setRotationAngle(outer_roof_4_r1, 0.0F, 0.0F, 0.3927F);
		outer_roof_4_r1.setTextureOffset(0, 40).addCuboid(0.0F, -3.0F, -39.0F, 0.0F, 3.0F, 39.0F, 0.0F, false);
		outer_roof_4_r1.setTextureOffset(0, 40).addCuboid(0.0F, -3.0F, 0.0F, 0.0F, 3.0F, 39.0F, 0.0F, false);

		inner_roof_2_r1 = new ModelPart(this);
		inner_roof_2_r1.setPivot(-5.1202F, -38.4588F, -26.0F);
		roof.addChild(inner_roof_2_r1);
		setRotationAngle(inner_roof_2_r1, 0.0F, 0.0F, -0.1047F);
		inner_roof_2_r1.setTextureOffset(213, 103).addCuboid(-2.8798F, -2.0F, -13.0F, 5.0F, 0.0F, 39.0F, 0.0F, false);
		inner_roof_2_r1.setTextureOffset(213, 103).addCuboid(-2.8798F, -2.0F, 26.0F, 5.0F, 0.0F, 39.0F, 0.0F, false);

		inner_roof_3_r1 = new ModelPart(this);
		inner_roof_3_r1.setPivot(-9.0655F, -37.834F, -26.0F);
		roof.addChild(inner_roof_3_r1);
		setRotationAngle(inner_roof_3_r1, 0.0F, 0.0F, -0.2094F);
		inner_roof_3_r1.setTextureOffset(229, 0).addCuboid(-1.0F, -2.0F, -13.0F, 4.0F, 0.0F, 39.0F, 0.0F, false);
		inner_roof_3_r1.setTextureOffset(229, 0).addCuboid(-1.0F, -2.0F, 26.0F, 4.0F, 0.0F, 39.0F, 0.0F, false);

		roof_exterior = new ModelPart(this);
		roof_exterior.setPivot(0.0F, 24.0F, 0.0F);
		roof_exterior.setTextureOffset(6, 0).addCuboid(-12.0F, -42.0F, 0.0F, 12.0F, 0.0F, 39.0F, 0.0F, false);
		roof_exterior.setTextureOffset(6, 0).addCuboid(-12.0F, -42.0F, -39.0F, 12.0F, 0.0F, 39.0F, 0.0F, false);

		outer_roof_3_r2 = new ModelPart(this);
		outer_roof_3_r2.setPivot(-21.0F, -37.0F, -19.5F);
		roof_exterior.addChild(outer_roof_3_r2);
		setRotationAngle(outer_roof_3_r2, 0.0F, 0.0F, 0.3054F);
		outer_roof_3_r2.setTextureOffset(0, 43).addCuboid(0.0F, -2.0F, -19.5F, 0.0F, 2.0F, 39.0F, 0.0F, false);
		outer_roof_3_r2.setTextureOffset(0, 43).addCuboid(0.0F, -2.0F, 19.5F, 0.0F, 2.0F, 39.0F, 0.0F, false);

		outer_roof_4_r2 = new ModelPart(this);
		outer_roof_4_r2.setPivot(-18.501F, -40.0F, -19.0F);
		roof_exterior.addChild(outer_roof_4_r2);
		setRotationAngle(outer_roof_4_r2, 0.0F, 0.0F, 1.0472F);
		outer_roof_4_r2.setTextureOffset(0, 37).addCuboid(0.0F, -0.82F, -20.0F, 0.0F, 3.0F, 39.0F, 0.0F, false);
		outer_roof_4_r2.setTextureOffset(0, 37).addCuboid(0.0F, -0.82F, 19.0F, 0.0F, 3.0F, 39.0F, 0.0F, false);

		outer_roof_5_r1 = new ModelPart(this);
		outer_roof_5_r1.setPivot(-12.0F, -42.0F, -19.0F);
		roof_exterior.addChild(outer_roof_5_r1);
		setRotationAngle(outer_roof_5_r1, 0.0F, 0.0F, -0.2705F);
		outer_roof_5_r1.setTextureOffset(201, 103).addCuboid(-6.0F, 0.0F, -20.0F, 6.0F, 0.0F, 39.0F, 0.0F, false);
		outer_roof_5_r1.setTextureOffset(201, 103).addCuboid(-6.0F, 0.0F, 19.0F, 6.0F, 0.0F, 39.0F, 0.0F, false);

		roof_door = new ModelPart(this);
		roof_door.setPivot(0.0F, 24.0F, 0.0F);
		roof_door.setTextureOffset(221, 0).addCuboid(-4.0F, -40.6679F, -20.0F, 4.0F, 0.0F, 39.0F, 0.0F, false);

		inner_roof_3_r2 = new ModelPart(this);
		inner_roof_3_r2.setPivot(-5.1202F, -38.4588F, -7.0F);
		roof_door.addChild(inner_roof_3_r2);
		setRotationAngle(inner_roof_3_r2, 0.0F, 0.0F, -0.1047F);
		inner_roof_3_r2.setTextureOffset(211, 0).addCuboid(-2.8798F, -2.0F, -13.0F, 5.0F, 0.0F, 39.0F, 0.0F, false);

		inner_roof_2_r2 = new ModelPart(this);
		inner_roof_2_r2.setPivot(-9.0655F, -37.834F, -7.0F);
		roof_door.addChild(inner_roof_2_r2);
		setRotationAngle(inner_roof_2_r2, 0.0F, 0.0F, -0.2094F);
		inner_roof_2_r2.setTextureOffset(30, 0).addCuboid(-4.0F, -2.0F, -13.0F, 7.0F, 0.0F, 39.0F, 0.0F, false);

		outer_roof_2_r2 = new ModelPart(this);
		outer_roof_2_r2.setPivot(-14.0F, -39.0F, 19.0F);
		roof_door.addChild(outer_roof_2_r2);
		setRotationAngle(outer_roof_2_r2, 0.0F, 0.0F, -0.7854F);
		outer_roof_2_r2.setTextureOffset(231, 103).addCuboid(-4.1522F, -0.2346F, -39.0F, 3.0F, 0.0F, 39.0F, 0.0F, false);

		outer_roof_1_r1 = new ModelPart(this);
		outer_roof_1_r1.setPivot(-13.0F, -39.0F, 19.0F);
		roof_door.addChild(outer_roof_1_r1);
		setRotationAngle(outer_roof_1_r1, 0.0F, 0.0F, -0.3927F);
		outer_roof_1_r1.setTextureOffset(237, 0).addCuboid(-3.0F, 0.0F, -39.0F, 3.0F, 0.0F, 39.0F, 0.0F, false);

		roof_exterior_door = new ModelPart(this);
		roof_exterior_door.setPivot(0.0F, 24.0F, 0.0F);
		roof_exterior_door.setTextureOffset(128, 0).addCuboid(-12.0F, -42.0F, -20.0F, 12.0F, 0.0F, 40.0F, 0.0F, false);

		outer_roof_5_r2 = new ModelPart(this);
		outer_roof_5_r2.setPivot(-12.0F, -42.0F, 0.0F);
		roof_exterior_door.addChild(outer_roof_5_r2);
		setRotationAngle(outer_roof_5_r2, 0.0F, 0.0F, -0.2705F);
		outer_roof_5_r2.setTextureOffset(152, 0).addCuboid(-6.0F, 0.0F, -20.0F, 6.0F, 0.0F, 40.0F, 0.0F, false);

		outer_roof_4_r3 = new ModelPart(this);
		outer_roof_4_r3.setPivot(-18.501F, -40.0F, 0.0F);
		roof_exterior_door.addChild(outer_roof_4_r3);
		setRotationAngle(outer_roof_4_r3, 0.0F, 0.0F, 1.0472F);
		outer_roof_4_r3.setTextureOffset(0, 31).addCuboid(0.0F, -0.82F, -20.0F, 0.0F, 3.0F, 40.0F, 0.0F, false);

		outer_roof_3_r3 = new ModelPart(this);
		outer_roof_3_r3.setPivot(-21.0F, -37.0F, -0.5F);
		roof_exterior_door.addChild(outer_roof_3_r3);
		setRotationAngle(outer_roof_3_r3, 0.0F, 0.0F, 0.3054F);
		outer_roof_3_r3.setTextureOffset(0, 34).addCuboid(0.0F, -2.0F, -19.5F, 0.0F, 2.0F, 40.0F, 0.0F, false);

		end = new ModelPart(this);
		end.setPivot(0.0F, 24.0F, 0.0F);
		end.setTextureOffset(0, 407).addCuboid(-12.0F, -42.0F, 20.0F, 12.0F, 4.0F, 0.0F, 0.0F, false);
		end.setTextureOffset(0, 407).addCuboid(0.0F, -42.0F, 20.0F, 12.0F, 4.0F, 0.0F, 0.0F, false);

		outer_roof_5_r3 = new ModelPart(this);
		outer_roof_5_r3.setPivot(12.0F, -42.0F, 0.0F);
		end.addChild(outer_roof_5_r3);
		setRotationAngle(outer_roof_5_r3, 0.0F, 0.0F, 0.2705F);
		outer_roof_5_r3.setTextureOffset(0, 407).addCuboid(0.0F, 0.0F, 20.0F, 6.0F, 2.0F, 0.0F, 0.0F, false);

		outer_roof_4_r4 = new ModelPart(this);
		outer_roof_4_r4.setPivot(18.501F, -40.0F, 0.0F);
		end.addChild(outer_roof_4_r4);
		setRotationAngle(outer_roof_4_r4, 0.0F, 0.0F, -1.0472F);
		outer_roof_4_r4.setTextureOffset(0, 407).addCuboid(-1.0F, -0.82F, 20.0F, 1.0F, 3.0F, 0.0F, 0.0F, false);

		outer_roof_3_r4 = new ModelPart(this);
		outer_roof_3_r4.setPivot(21.0F, -37.0F, -0.5F);
		end.addChild(outer_roof_3_r4);
		setRotationAngle(outer_roof_3_r4, 0.0F, 0.0F, -0.3054F);
		outer_roof_3_r4.setTextureOffset(0, 408).addCuboid(-1.0F, -2.0F, 20.5F, 1.0F, 4.0F, 0.0F, 0.0F, false);

		outer_roof_5_r4 = new ModelPart(this);
		outer_roof_5_r4.setPivot(-12.0F, -42.0F, 0.0F);
		end.addChild(outer_roof_5_r4);
		setRotationAngle(outer_roof_5_r4, 0.0F, 0.0F, -0.2705F);
		outer_roof_5_r4.setTextureOffset(0, 407).addCuboid(-6.0F, 0.0F, 20.0F, 6.0F, 2.0F, 0.0F, 0.0F, false);

		outer_roof_4_r5 = new ModelPart(this);
		outer_roof_4_r5.setPivot(-18.501F, -40.0F, 0.0F);
		end.addChild(outer_roof_4_r5);
		setRotationAngle(outer_roof_4_r5, 0.0F, 0.0F, 1.0472F);
		outer_roof_4_r5.setTextureOffset(0, 407).addCuboid(0.0F, -0.82F, 20.0F, 1.0F, 3.0F, 0.0F, 0.0F, false);

		outer_roof_3_r5 = new ModelPart(this);
		outer_roof_3_r5.setPivot(-21.0F, -37.0F, -0.5F);
		end.addChild(outer_roof_3_r5);
		setRotationAngle(outer_roof_3_r5, 0.0F, 0.0F, 0.3054F);
		outer_roof_3_r5.setTextureOffset(0, 408).addCuboid(0.0F, -2.0F, 20.5F, 1.0F, 4.0F, 0.0F, 0.0F, false);

		luggage_rack = new ModelPart(this);
		luggage_rack.setPivot(0.0F, 24.0F, 0.0F);
		luggage_rack.setTextureOffset(140, 42).addCuboid(-19.0F, -31.0F, -50.0F, 11.0F, 31.0F, 0.0F, 0.0F, false);
		luggage_rack.setTextureOffset(140, 42).addCuboid(-19.0F, -31.0F, -28.0F, 11.0F, 31.0F, 0.0F, 0.0F, false);
		luggage_rack.setTextureOffset(0, 199).addCuboid(-19.0F, -11.0F, -50.0F, 11.0F, 1.0F, 22.0F, 0.0F, false);
		luggage_rack.setTextureOffset(0, 199).addCuboid(-19.0F, -21.0F, -50.0F, 11.0F, 1.0F, 22.0F, 0.0F, false);

		seat = new ModelPart(this);
		seat.setPivot(0.0F, 24.0F, 0.0F);
		seat.setTextureOffset(130, 173).addCuboid(-3.0F, -7.0F, -3.0F, 6.0F, 1.0F, 6.0F, 0.0F, false);
		seat.setTextureOffset(30, 0).addCuboid(-1.5F, -18.4F, 5.0F, 3.0F, 2.0F, 1.0F, 0.0F, false);
		seat.setTextureOffset(168, 41).addCuboid(3.0F, -11.0F, -3.0F, 1.0F, 5.0F, 7.0F, 0.0F, false);
		seat.setTextureOffset(168, 41).addCuboid(-4.0F, -11.0F, -3.0F, 1.0F, 5.0F, 7.0F, 0.0F, true);

		top_right_r1 = new ModelPart(this);
		top_right_r1.setPivot(-1.5F, -16.4F, 4.5F);
		seat.addChild(top_right_r1);
		setRotationAngle(top_right_r1, 0.0017F, 0.0F, 0.2618F);
		top_right_r1.setTextureOffset(30, 3).addCuboid(-1.0F, -2.0F, 0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

		top_left_r1 = new ModelPart(this);
		top_left_r1.setPivot(1.5F, -16.4F, 4.5F);
		seat.addChild(top_left_r1);
		setRotationAngle(top_left_r1, 0.0017F, 0.0F, -0.2618F);
		top_left_r1.setTextureOffset(30, 3).addCuboid(0.0F, -2.0F, 0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

		back_right_r1 = new ModelPart(this);
		back_right_r1.setPivot(-1.5F, -5.0F, 2.0F);
		seat.addChild(back_right_r1);
		setRotationAngle(back_right_r1, -0.2618F, -0.1745F, 0.0873F);
		back_right_r1.setTextureOffset(26, 24).addCuboid(-1.5F, -11.75F, 0.0F, 2.0F, 10.0F, 1.0F, 0.0F, true);

		back_left_r1 = new ModelPart(this);
		back_left_r1.setPivot(1.5F, -5.0F, 2.0F);
		seat.addChild(back_left_r1);
		setRotationAngle(back_left_r1, -0.2618F, 0.1745F, -0.0873F);
		back_left_r1.setTextureOffset(26, 24).addCuboid(-0.5F, -11.75F, 0.0F, 2.0F, 10.0F, 1.0F, 0.0F, false);

		back_r1 = new ModelPart(this);
		back_r1.setPivot(3.0F, -5.0F, 2.0F);
		seat.addChild(back_r1);
		setRotationAngle(back_r1, -0.2618F, 0.0F, 0.0F);
		back_r1.setTextureOffset(24, 13).addCuboid(-4.5F, -12.0F, 0.0F, 3.0F, 10.0F, 1.0F, 0.0F, false);

		head_wall = new ModelPart(this);
		head_wall.setPivot(0.0F, 24.0F, 0.0F);
		head_wall.setTextureOffset(80, 271).addCuboid(-18.0F, -41.0F, 0.0F, 36.0F, 41.0F, 0.0F, 0.0F, false);

		end_door = new ModelPart(this);
		end_door.setPivot(0.0F, 24.0F, 0.0F);
		end_door.setTextureOffset(0, 0).addCuboid(-6.0F, -35.0F, 20.0F, 12.0F, 34.0F, 0.0F, 0.0F, false);
		end_door.setTextureOffset(0, 0).addCuboid(-6.0F, -35.0F, -20.0F, 12.0F, 34.0F, 0.0F, 0.0F, false);

		headlights = new ModelPart(this);
		headlights.setPivot(0.0F, 24.0F, 0.0F);


		headlights_r1 = new ModelPart(this);
		headlights_r1.setPivot(0.5F, -21.0F, -70.0F);
		headlights.addChild(headlights_r1);
		setRotationAngle(headlights_r1, -1.2654F, 0.0F, 0.0F);
		headlights_r1.setTextureOffset(325, 0).addCuboid(-8.0F, -4.0F, -0.2F, 15.0F, 5.0F, 0.0F, 0.0F, false);

		taillights = new ModelPart(this);
		taillights.setPivot(0.0F, 24.0F, 0.0F);


		taillights_r1 = new ModelPart(this);
		taillights_r1.setPivot(0.5F, -21.0F, -70.0F);
		taillights.addChild(taillights_r1);
		setRotationAngle(taillights_r1, -1.2654F, 0.0F, 0.0F);
		taillights_r1.setTextureOffset(325, 6).addCuboid(-8.0F, -4.0F, -0.2F, 15.0F, 5.0F, 0.0F, 0.0F, false);
	}

	private static final int DOOR_MAX = 13;
//	private static final ModelDoorOverlay MODEL_DOOR_OVERLAY = new ModelDoorOverlay(DOOR_MAX, 0, "door_overlay_crh380a.png", null);

	@Override
	protected void renderWindowPositions(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, boolean isEnd1Head, boolean isEnd2Head) {
		final boolean isEnd = isEnd1Head || isEnd2Head;

		if (!isEnd) {
			switch (renderStage) {
				case LIGHTS:
					renderMirror(roof, matrices, vertices, light, position - 117);
					renderMirror(roof, matrices, vertices, light, position - 39);
					renderMirror(roof, matrices, vertices, light, position + 39);
					renderMirror(roof, matrices, vertices, light, position + 117);
					break;
				case INTERIOR:
					renderMirror(window, matrices, vertices, light, position - 39);
					renderMirror(window, matrices, vertices, light, position - 117);
					renderMirror(window, matrices, vertices, light, position + 39);
					renderMirror(window, matrices, vertices, light, position + 117);
					if (renderDetails) {
						for (float z = position - 117; z <= position + 117; z += 19.5) {
							renderOnce(seat, matrices, vertices, light, 14, z);
							renderOnce(seat, matrices, vertices, light, 7.5F, z);
							renderOnce(seat, matrices, vertices, light, -7.5F, z);
							renderOnce(seat, matrices, vertices, light, -14, z);
						}
					}
					break;
				case EXTERIOR:
					renderMirror(window_exterior, matrices, vertices, light, position);
					renderMirror(roof_exterior, matrices, vertices, light, position - 117);
					renderMirror(roof_exterior, matrices, vertices, light, position - 39);
					renderMirror(roof_exterior, matrices, vertices, light, position + 39);
					renderMirror(roof_exterior, matrices, vertices, light, position + 117);
					break;

			}
		}
	}
	@Override
	protected void renderDoorPositions(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
		final boolean frontDoor = position > 0;
		final boolean isEnd = isEnd1Head || isEnd2Head;
		final boolean doorOpen = doorLeftZ > 0 || doorRightZ > 0;

		switch (renderStage) {
			case LIGHTS:
				if (frontDoor) {
					renderMirror(roof_door, matrices, vertices, light, !isEnd2Head ? position : position - 78);
				} else {
					renderMirror(roof_door, matrices, vertices, light, !isEnd1Head ? position : position + 78);
				}
				break;
			case INTERIOR:
				if (frontDoor) {
					left_door.setPivot(0, 0, -doorLeftZ);
					right_door.setPivot(0, 0, -doorRightZ);
					renderOnce(door_standard, matrices, vertices, light, !isEnd2Head ? position : position - 78);
					renderOnce(door, matrices, vertices, light, !isEnd2Head ? position : position - 78);
					renderOnce(end_door, matrices, vertices, light, !isEnd2Head ? position : position - 78);
					if (!isEnd2Head) {renderOnce(end, matrices, vertices, light, position);}
				} else {
					left_door.setPivot(0, 0,-doorRightZ);
					right_door.setPivot(0, 0, -doorLeftZ);
					renderOnceFlipped(door_standard, matrices, vertices, light, !isEnd1Head ? position : position + 78);
					renderOnceFlipped(door, matrices, vertices, light, !isEnd1Head ? position : position + 78);
					renderOnceFlipped(end_door, matrices, vertices, light, !isEnd1Head ? position : position + 78);
					if (!isEnd1Head) {renderOnceFlipped(end, matrices, vertices, light, position);}
				}
				break;
			case INTERIOR_TRANSLUCENT:
				if (!isEnd) {
					if (frontDoor) {
						renderOnce(luggage_rack, matrices, vertices, light, position);
					} else {
						renderOnceFlipped(luggage_rack, matrices, vertices, light, position);
					}
				}
				break;
			case EXTERIOR:
				if (frontDoor) {
					left_door_exterior.setPivot(0, 0, -doorLeftZ);
					left_door_exterior_end.setPivot(0, 0, -doorLeftZ);
					right_door_exterior.setPivot(0, 0, -doorRightZ);
					right_door_exterior_end.setPivot(0, 0,  -doorRightZ);
					renderOnce(isEnd2Head ? door_exterior_end : door_exterior, matrices, vertices, light, !isEnd2Head ? position : position - 78);
					renderMirror(roof_exterior_door, matrices, vertices, light, !isEnd2Head ? position : position - 78);
				} else {
					left_door_exterior.setPivot(0, 0, -doorRightZ);
					left_door_exterior_end.setPivot(0, 0, -doorRightZ);
					right_door_exterior.setPivot(0, 0, -doorLeftZ);
					right_door_exterior_end.setPivot(0, 0, -doorLeftZ);
					renderOnceFlipped(isEnd1Head ? door_exterior_end : door_exterior, matrices, vertices, light, !isEnd1Head ? position : position + 78);
					renderMirror(roof_exterior_door, matrices, vertices, light, !isEnd1Head ? position : position + 78);
				}
				break;
		}
	}

	@Override
	protected void renderHeadPosition1(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, boolean useHeadlights) {

		switch (renderStage) {
			case LIGHTS:
				renderMirror(roof, matrices, vertices, light, position + 19);
				renderMirror(roof, matrices, vertices, light, position + 137);
				renderMirror(roof, matrices, vertices, light, position + 215);
				renderMirror(roof, matrices, vertices, light, position + 293);
				break;
			case ALWAYS_ON_LIGHTS:
				renderOnce(useHeadlights ? headlights : taillights, matrices, vertices, light, position - 20);
				break;
			case INTERIOR:
				renderOnce(head, matrices, vertices, light, position - 20);
				if (renderDetails) {
					for (float z =  position - 10; z < position + 58; z += 19.5 ) {
						renderOnce(seat, matrices, vertices, light, 14, z);
						renderOnce(seat, matrices, vertices, light, 7.5F, z);
						renderOnce(seat, matrices, vertices, light, -7.5F, z);
						renderOnce(seat, matrices, vertices, light, -14, z);
					}
					for (float z =  position + 104; z < position + 332; z += 19.5 ) {
						renderOnce(seat, matrices, vertices, light, 14, z);
						renderOnce(seat, matrices, vertices, light, 7.5F, z);
						renderOnce(seat, matrices, vertices, light, -7.5F, z);
						renderOnce(seat, matrices, vertices, light, -14, z);
					}
				}
				renderMirror(window, matrices, vertices, light, position + 19);
				renderMirror(window, matrices, vertices, light, position + 137);
				renderMirror(window, matrices, vertices, light, position + 215);
				renderMirror(window, matrices, vertices, light, position + 293);
				renderOnce(head_wall, matrices, vertices, light, position - 20);
				break;
			case EXTERIOR:
				renderOnce(window_exterior_end, matrices, vertices, light, position - 20);
				renderMirror(roof_exterior_door, matrices, vertices, light, position + 17);
				renderMirror(roof_exterior, matrices, vertices, light, position + 19);
				renderMirror(roof_exterior, matrices, vertices, light, position + 137);
				renderMirror(roof_exterior, matrices, vertices, light, position + 215);
				renderMirror(roof_exterior, matrices, vertices, light, position + 293);
				break;
		}
	}

	@Override
	protected void renderHeadPosition2(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, boolean useHeadlights) {
		switch (renderStage) {
			case LIGHTS:
				renderMirror(roof, matrices, vertices, light, position - 19);
				renderMirror(roof, matrices, vertices, light, position - 137);
				renderMirror(roof, matrices, vertices, light, position - 215);
				renderMirror(roof, matrices, vertices, light, position - 293);
				break;
			case ALWAYS_ON_LIGHTS:
				renderOnceFlipped(useHeadlights ? headlights : taillights, matrices, vertices, light, position + 20);
				break;
			case INTERIOR:
				renderOnceFlipped(head, matrices, vertices, light, position + 20);
				if (renderDetails) {
					for (float z = position + 10; z > position - 58; z -= 19.5) {
						renderOnce(seat, matrices, vertices, light, 14, z);
						renderOnce(seat, matrices, vertices, light, 7.5F, z);
						renderOnce(seat, matrices, vertices, light, -7.5F, z);
						renderOnce(seat, matrices, vertices, light, -14, z);
					}
					for (float z = position - 104; z > position - 332; z -= 19.5) {
						renderOnce(seat, matrices, vertices, light, 14, z);
						renderOnce(seat, matrices, vertices, light, 7.5F, z);
						renderOnce(seat, matrices, vertices, light, -7.5F, z);
						renderOnce(seat, matrices, vertices, light, -14, z);
					}
				}
				renderMirror(window, matrices, vertices, light, position - 19);
				renderMirror(window, matrices, vertices, light, position - 137);
				renderMirror(window, matrices, vertices, light, position - 215);
				renderMirror(window, matrices, vertices, light, position - 293);
				renderOnce(head_wall, matrices, vertices, light, position + 20);
				break;
			case EXTERIOR:
				renderOnceFlipped(window_exterior_end, matrices, vertices, light, position + 20);
				renderMirror(roof_exterior_door, matrices, vertices, light, position - 17);
				renderMirror(roof_exterior, matrices, vertices, light, position - 19);
				renderMirror(roof_exterior, matrices, vertices, light, position - 137);
				renderMirror(roof_exterior, matrices, vertices, light, position - 215);
				renderMirror(roof_exterior, matrices, vertices, light, position - 293);
				break;
		}
	}

	@Override
	protected ModelDoorOverlay getModelDoorOverlay() {
//		return MODEL_DOOR_OVERLAY;
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
		return new int[]{-176, 176};
	}

	@Override
	protected int[] getEndPositions() { return new int[]{-176, 176}; }

	@Override
	protected float getDoorAnimationX(float value, boolean opening) {
		return 0;
	}

	@Override
	protected float getDoorAnimationZ(float value, boolean opening) {
		return smoothEnds(0, DOOR_MAX, 0, 0.5F, value);
	}

	protected void renderEndPosition1(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails) { }

	@Override
	protected void renderEndPosition2(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails) { }
}

