package mtr.model;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class ModelCRH380A extends ModelTrainBase {

	private final ModelRenderer window;
	private final ModelRenderer window_exterior;
	private final ModelRenderer floor_r1;
	private final ModelRenderer window_exterior_end;
	private final ModelRenderer floor_4_r1;
	private final ModelRenderer floor_3_r1;
	private final ModelRenderer head;
	private final ModelRenderer aero_4_r1;
	private final ModelRenderer aero_3_r1;
	private final ModelRenderer aero_2_r1;
	private final ModelRenderer aero_1_r1;
	private final ModelRenderer skirt_2_r1;
	private final ModelRenderer skirt_1_r1;
	private final ModelRenderer floor;
	private final ModelRenderer floor_7_r1;
	private final ModelRenderer floor_6_r1;
	private final ModelRenderer floor_5_r1;
	private final ModelRenderer floor_4_r2;
	private final ModelRenderer floor_3_r2;
	private final ModelRenderer floor_2_r1;
	private final ModelRenderer nose;
	private final ModelRenderer nose_13_r1;
	private final ModelRenderer nose_12_r1;
	private final ModelRenderer nose_11_r1;
	private final ModelRenderer nose_10_r1;
	private final ModelRenderer nose_9_r1;
	private final ModelRenderer nose_8_r1;
	private final ModelRenderer nose_7_r1;
	private final ModelRenderer nose_6_r1;
	private final ModelRenderer nose_5_r1;
	private final ModelRenderer nose_4_r1;
	private final ModelRenderer nose_3_r1;
	private final ModelRenderer nose_1_r1;
	private final ModelRenderer wall;
	private final ModelRenderer wall_8_r1;
	private final ModelRenderer wall_5_r1;
	private final ModelRenderer wall_4_r1;
	private final ModelRenderer wall_1_r1;
	private final ModelRenderer front_panel;
	private final ModelRenderer front_panel_5_r1;
	private final ModelRenderer front_panel_4_r1;
	private final ModelRenderer front_panel_3_r1;
	private final ModelRenderer front_panel_2_r1;
	private final ModelRenderer front_panel_1_r1;
	private final ModelRenderer cockpit;
	private final ModelRenderer windscreen_3_r1;
	private final ModelRenderer windscreen_2_r1;
	private final ModelRenderer windscreen_1_r1;
	private final ModelRenderer top_3_r1;
	private final ModelRenderer top_2_r1;
	private final ModelRenderer door;
	private final ModelRenderer doorframe;
	private final ModelRenderer doorframe_1_r1;
	private final ModelRenderer door_exterior;
	private final ModelRenderer floor_r2;
	private final ModelRenderer door_exterior_end;
	private final ModelRenderer floor_r3;
	private final ModelRenderer roof;
	private final ModelRenderer inner_roof_3_r1;
	private final ModelRenderer inner_roof_2_r1;
	private final ModelRenderer outer_roof_4_r1;
	private final ModelRenderer outer_roof_3_r1;
	private final ModelRenderer outer_roof_2_r1;
	private final ModelRenderer roof_exterior;
	private final ModelRenderer outer_roof_5_r1;
	private final ModelRenderer outer_roof_4_r2;
	private final ModelRenderer outer_roof_3_r2;
	private final ModelRenderer roof_door;
	private final ModelRenderer inner_roof_3_r2;
	private final ModelRenderer inner_roof_2_r2;
	private final ModelRenderer outer_roof_2_r2;
	private final ModelRenderer outer_roof_1_r1;
	private final ModelRenderer roof_exterior_door;
	private final ModelRenderer outer_roof_5_r2;
	private final ModelRenderer outer_roof_4_r3;
	private final ModelRenderer outer_roof_3_r3;
	private final ModelRenderer luggage_rack;
	private final ModelRenderer seat;
	private final ModelRenderer top_right_r1;
	private final ModelRenderer top_left_r1;
	private final ModelRenderer back_right_r1;
	private final ModelRenderer back_left_r1;
	private final ModelRenderer back_r1;
	private final ModelRenderer bb_main;

	public ModelCRH380A() {
		textureWidth = 944;
		textureHeight = 944;

		window = new ModelRenderer(this);
		window.setRotationPoint(0.0F, 24.0F, 90.0F);
		window.setTextureOffset(0, 0).addBox(-21.0F, -32.0F, -90.0F, 3.0F, 32.0F, 39.0F, 0.0F, false);
		window.setTextureOffset(207, 63).addBox(-20.0F, -1.0F, -90.0F, 20.0F, 1.0F, 39.0F, 0.0F, false);

		window_exterior = new ModelRenderer(this);
		window_exterior.setRotationPoint(0.0F, 24.0F, 91.0F);
		window_exterior.setTextureOffset(0, 0).addBox(-21.0F, -37.0F, 27.0F, 0.0F, 37.0F, 312.0F, 0.0F, false);

		floor_r1 = new ModelRenderer(this);
		floor_r1.setRotationPoint(-21.0F, 0.0F, 0.0F);
		window_exterior.addChild(floor_r1);
		setRotationAngle(floor_r1, 0.0F, 0.0F, -0.1745F);
		floor_r1.setTextureOffset(312, 37).addBox(0.0F, 0.0F, 27.0F, 1.0F, 8.0F, 312.0F, 0.0F, false);

		window_exterior_end = new ModelRenderer(this);
		window_exterior_end.setRotationPoint(0.0F, 24.0F, 0.0F);
		window_exterior_end.setTextureOffset(0, 57).addBox(-21.0F, -37.0F, 0.0F, 0.0F, 37.0F, 78.0F, 0.0F, false);
		window_exterior_end.setTextureOffset(0, 57).addBox(21.0F, -37.0F, 0.0F, 0.0F, 37.0F, 78.0F, 0.0F, true);
		window_exterior_end.setTextureOffset(0, 0).addBox(-21.0F, -37.0F, 118.0F, 0.0F, 37.0F, 234.0F, 0.0F, false);
		window_exterior_end.setTextureOffset(0, 0).addBox(21.0F, -37.0F, 118.0F, 0.0F, 37.0F, 234.0F, 0.0F, false);

		floor_4_r1 = new ModelRenderer(this);
		floor_4_r1.setRotationPoint(21.0F, 0.0F, 118.0F);
		window_exterior_end.addChild(floor_4_r1);
		setRotationAngle(floor_4_r1, 0.0F, 0.0F, 0.1745F);
		floor_4_r1.setTextureOffset(0, 350).addBox(-1.0F, 0.0F, 0.0F, 1.0F, 8.0F, 234.0F, 0.0F, false);
		floor_4_r1.setTextureOffset(88, 0).addBox(-1.0F, 0.0F, -118.0F, 1.0F, 8.0F, 78.0F, 0.0F, true);

		floor_3_r1 = new ModelRenderer(this);
		floor_3_r1.setRotationPoint(-21.0F, 0.0F, 118.0F);
		window_exterior_end.addChild(floor_3_r1);
		setRotationAngle(floor_3_r1, 0.0F, 0.0F, -0.1745F);
		floor_3_r1.setTextureOffset(0, 349).addBox(0.0F, 0.0F, 0.0F, 1.0F, 8.0F, 234.0F, 0.0F, false);
		floor_3_r1.setTextureOffset(88, 0).addBox(0.0F, 0.0F, -118.0F, 1.0F, 8.0F, 78.0F, 0.0F, false);

		head = new ModelRenderer(this);
		head.setRotationPoint(21.0F, 24.0F, 0.0F);
		

		aero_4_r1 = new ModelRenderer(this);
		aero_4_r1.setRotationPoint(-42.0F, -5.0F, -45.0F);
		head.addChild(aero_4_r1);
		setRotationAngle(aero_4_r1, -0.0873F, -0.1396F, 1.1781F);
		aero_4_r1.setTextureOffset(90, 55).addBox(0.0F, -7.0F, -44.0F, 0.0F, 7.0F, 44.0F, 0.0F, false);

		aero_3_r1 = new ModelRenderer(this);
		aero_3_r1.setRotationPoint(-42.0F, -5.0F, -24.0F);
		head.addChild(aero_3_r1);
		setRotationAngle(aero_3_r1, 0.0F, 0.0F, 1.1781F);
		aero_3_r1.setTextureOffset(0, 49).addBox(0.0F, -7.0F, -21.0F, 0.0F, 7.0F, 45.0F, 0.0F, false);

		aero_2_r1 = new ModelRenderer(this);
		aero_2_r1.setRotationPoint(0.0F, -5.0F, -45.0F);
		head.addChild(aero_2_r1);
		setRotationAngle(aero_2_r1, -0.0873F, 0.1396F, -1.1781F);
		aero_2_r1.setTextureOffset(90, 55).addBox(0.0F, -7.0F, -44.0F, 0.0F, 7.0F, 44.0F, 0.0F, false);

		aero_1_r1 = new ModelRenderer(this);
		aero_1_r1.setRotationPoint(0.0F, -5.0F, -24.0F);
		head.addChild(aero_1_r1);
		setRotationAngle(aero_1_r1, 0.0F, 0.0F, -1.1781F);
		aero_1_r1.setTextureOffset(0, 49).addBox(0.0F, -7.0F, -21.0F, 0.0F, 7.0F, 45.0F, 0.0F, false);

		skirt_2_r1 = new ModelRenderer(this);
		skirt_2_r1.setRotationPoint(0.0F, 0.0F, 0.0F);
		head.addChild(skirt_2_r1);
		setRotationAngle(skirt_2_r1, 0.0436F, 0.2443F, 0.1745F);
		skirt_2_r1.setTextureOffset(0, 0).addBox(-1.0F, 0.0F, -86.0F, 1.0F, 8.0F, 86.0F, 0.0F, false);

		skirt_1_r1 = new ModelRenderer(this);
		skirt_1_r1.setRotationPoint(-42.0F, 0.0F, 0.0F);
		head.addChild(skirt_1_r1);
		setRotationAngle(skirt_1_r1, 0.0436F, -0.2443F, -0.1745F);
		skirt_1_r1.setTextureOffset(0, 0).addBox(0.0F, 0.0F, -86.0F, 1.0F, 8.0F, 86.0F, 0.0F, true);

		floor = new ModelRenderer(this);
		floor.setRotationPoint(-21.0F, -14.0F, -114.0F);
		head.addChild(floor);
		floor.setTextureOffset(110, 135).addBox(-21.0F, 13.0F, 68.0F, 42.0F, 1.0F, 46.0F, 0.0F, false);
		floor.setTextureOffset(66, 199).addBox(-4.0F, 13.0F, 12.0F, 8.0F, 1.0F, 22.0F, 0.0F, false);

		floor_7_r1 = new ModelRenderer(this);
		floor_7_r1.setRotationPoint(0.0F, 14.0F, 35.0F);
		floor.addChild(floor_7_r1);
		setRotationAngle(floor_7_r1, 0.0F, -1.4312F, 0.0F);
		floor_7_r1.setTextureOffset(177, 193).addBox(-10.0F, -1.0F, -16.0F, 47.0F, 1.0F, 21.0F, 0.0F, false);

		floor_6_r1 = new ModelRenderer(this);
		floor_6_r1.setRotationPoint(0.0F, 14.0F, 35.0F);
		floor.addChild(floor_6_r1);
		setRotationAngle(floor_6_r1, 0.0F, -1.1432F, 0.0F);
		floor_6_r1.setTextureOffset(168, 63).addBox(-13.0F, -1.0F, -18.0F, 8.0F, 1.0F, 11.0F, 0.0F, false);

		floor_5_r1 = new ModelRenderer(this);
		floor_5_r1.setRotationPoint(0.0F, 14.0F, 35.0F);
		floor.addChild(floor_5_r1);
		setRotationAngle(floor_5_r1, 0.0F, -0.5236F, 0.0F);
		floor_5_r1.setTextureOffset(108, 70).addBox(-8.0F, -1.0F, -22.0F, 8.0F, 1.0F, 6.0F, 0.0F, false);

		floor_4_r2 = new ModelRenderer(this);
		floor_4_r2.setRotationPoint(0.0F, 14.0F, 35.0F);
		floor.addChild(floor_4_r2);
		setRotationAngle(floor_4_r2, 0.0F, 1.4312F, 0.0F);
		floor_4_r2.setTextureOffset(177, 193).addBox(-37.0F, -1.0F, -16.0F, 47.0F, 1.0F, 21.0F, 0.0F, false);

		floor_3_r2 = new ModelRenderer(this);
		floor_3_r2.setRotationPoint(0.0F, 14.0F, 35.0F);
		floor.addChild(floor_3_r2);
		setRotationAngle(floor_3_r2, 0.0F, 1.1432F, 0.0F);
		floor_3_r2.setTextureOffset(168, 63).addBox(5.0F, -1.0F, -18.0F, 8.0F, 1.0F, 11.0F, 0.0F, false);

		floor_2_r1 = new ModelRenderer(this);
		floor_2_r1.setRotationPoint(0.0F, 14.0F, 35.0F);
		floor.addChild(floor_2_r1);
		setRotationAngle(floor_2_r1, 0.0F, 0.5236F, 0.0F);
		floor_2_r1.setTextureOffset(108, 70).addBox(0.0F, -1.0F, -22.0F, 8.0F, 1.0F, 6.0F, 0.0F, false);

		nose = new ModelRenderer(this);
		nose.setRotationPoint(-21.0F, -14.0F, -114.0F);
		head.addChild(nose);
		nose.setTextureOffset(60, 199).addBox(-4.0F, 5.0F, 8.0F, 8.0F, 6.0F, 0.0F, 0.0F, false);

		nose_13_r1 = new ModelRenderer(this);
		nose_13_r1.setRotationPoint(0.0F, 12.0F, 13.0F);
		nose.addChild(nose_13_r1);
		setRotationAngle(nose_13_r1, 0.0F, 1.3963F, 0.0F);
		nose_13_r1.setTextureOffset(195, 63).addBox(-15.0F, -8.0F, -13.0F, 12.0F, 9.0F, 0.0F, 0.0F, true);

		nose_12_r1 = new ModelRenderer(this);
		nose_12_r1.setRotationPoint(0.0F, 12.0F, 13.0F);
		nose.addChild(nose_12_r1);
		setRotationAngle(nose_12_r1, 0.6597F, 1.1345F, 0.0F);
		nose_12_r1.setTextureOffset(198, 182).addBox(-17.0F, -9.0F, -8.0F, 11.0F, 6.0F, 0.0F, 0.0F, true);

		nose_11_r1 = new ModelRenderer(this);
		nose_11_r1.setRotationPoint(0.0F, 12.0F, 13.0F);
		nose.addChild(nose_11_r1);
		setRotationAngle(nose_11_r1, 0.0F, 0.5236F, 0.0F);
		nose_11_r1.setTextureOffset(178, 100).addBox(-12.0F, -7.0F, -6.0F, 12.0F, 6.0F, 0.0F, 0.0F, true);

		nose_10_r1 = new ModelRenderer(this);
		nose_10_r1.setRotationPoint(0.0F, 12.0F, 13.0F);
		nose.addChild(nose_10_r1);
		setRotationAngle(nose_10_r1, -0.7854F, 0.5236F, 0.0F);
		nose_10_r1.setTextureOffset(136, 73).addBox(-12.0F, -5.0F, -9.0F, 11.0F, 5.0F, 0.0F, 0.0F, true);

		nose_9_r1 = new ModelRenderer(this);
		nose_9_r1.setRotationPoint(0.0F, 12.0F, 13.0F);
		nose.addChild(nose_9_r1);
		setRotationAngle(nose_9_r1, 0.829F, 0.5236F, 0.0F);
		nose_9_r1.setTextureOffset(0, 199).addBox(-12.0F, -6.0F, -3.0F, 11.0F, 6.0F, 0.0F, 0.0F, true);

		nose_8_r1 = new ModelRenderer(this);
		nose_8_r1.setRotationPoint(0.0F, 12.0F, 13.0F);
		nose.addChild(nose_8_r1);
		setRotationAngle(nose_8_r1, 0.6597F, -1.1345F, 0.0F);
		nose_8_r1.setTextureOffset(198, 182).addBox(6.0F, -9.0F, -8.0F, 11.0F, 6.0F, 0.0F, 0.0F, false);

		nose_7_r1 = new ModelRenderer(this);
		nose_7_r1.setRotationPoint(0.0F, 12.0F, 13.0F);
		nose.addChild(nose_7_r1);
		setRotationAngle(nose_7_r1, 0.0F, -1.3963F, 0.0F);
		nose_7_r1.setTextureOffset(195, 63).addBox(3.0F, -8.0F, -13.0F, 12.0F, 9.0F, 0.0F, 0.0F, false);

		nose_6_r1 = new ModelRenderer(this);
		nose_6_r1.setRotationPoint(0.0F, 12.0F, 13.0F);
		nose.addChild(nose_6_r1);
		setRotationAngle(nose_6_r1, -0.7854F, -0.5236F, 0.0F);
		nose_6_r1.setTextureOffset(136, 73).addBox(1.0F, -5.0F, -9.0F, 11.0F, 5.0F, 0.0F, 0.0F, false);

		nose_5_r1 = new ModelRenderer(this);
		nose_5_r1.setRotationPoint(0.0F, 12.0F, 13.0F);
		nose.addChild(nose_5_r1);
		setRotationAngle(nose_5_r1, 0.0F, -0.5236F, 0.0F);
		nose_5_r1.setTextureOffset(178, 100).addBox(0.0F, -7.0F, -6.0F, 12.0F, 6.0F, 0.0F, 0.0F, false);

		nose_4_r1 = new ModelRenderer(this);
		nose_4_r1.setRotationPoint(0.0F, 12.0F, 13.0F);
		nose.addChild(nose_4_r1);
		setRotationAngle(nose_4_r1, 0.829F, -0.5236F, 0.0F);
		nose_4_r1.setTextureOffset(0, 199).addBox(1.0F, -6.0F, -3.0F, 11.0F, 6.0F, 0.0F, 0.0F, false);

		nose_3_r1 = new ModelRenderer(this);
		nose_3_r1.setRotationPoint(0.0F, 12.0F, 13.0F);
		nose.addChild(nose_3_r1);
		setRotationAngle(nose_3_r1, -0.829F, 0.0F, 0.0F);
		nose_3_r1.setTextureOffset(198, 188).addBox(-4.0F, -6.0F, -8.0F, 8.0F, 5.0F, 0.0F, 0.0F, false);

		nose_1_r1 = new ModelRenderer(this);
		nose_1_r1.setRotationPoint(0.0F, 12.0F, 13.0F);
		nose.addChild(nose_1_r1);
		setRotationAngle(nose_1_r1, 0.829F, 0.0F, 0.0F);
		nose_1_r1.setTextureOffset(0, 205).addBox(-4.0F, -5.0F, -2.0F, 8.0F, 6.0F, 0.0F, 0.0F, false);

		wall = new ModelRenderer(this);
		wall.setRotationPoint(0.0F, 0.0F, 0.0F);
		head.addChild(wall);
		wall.setTextureOffset(0, 118).addBox(-6.0F, -28.0F, -91.0F, 0.0F, 27.0F, 54.0F, 0.0F, false);
		wall.setTextureOffset(90, 49).addBox(0.0F, -5.0F, -45.0F, 0.0F, 5.0F, 45.0F, 0.0F, false);
		wall.setTextureOffset(0, 118).addBox(-36.0F, -28.0F, -91.0F, 0.0F, 27.0F, 54.0F, 0.0F, false);
		wall.setTextureOffset(90, 49).addBox(-42.0F, -5.0F, -45.0F, 0.0F, 5.0F, 45.0F, 0.0F, false);

		wall_8_r1 = new ModelRenderer(this);
		wall_8_r1.setRotationPoint(-42.0F, 0.0F, -45.0F);
		wall.addChild(wall_8_r1);
		setRotationAngle(wall_8_r1, 0.0F, -0.1309F, 0.0F);
		wall_8_r1.setTextureOffset(0, 57).addBox(0.0F, -5.0F, -45.0F, 0.0F, 5.0F, 45.0F, 0.0F, false);

		wall_5_r1 = new ModelRenderer(this);
		wall_5_r1.setRotationPoint(-42.0F, 0.0F, 0.0F);
		wall.addChild(wall_5_r1);
		setRotationAngle(wall_5_r1, 0.0F, -0.1309F, 0.0F);
		wall_5_r1.setTextureOffset(108, 137).addBox(0.0F, -37.0F, -45.0F, 0.0F, 32.0F, 45.0F, 0.0F, false);

		wall_4_r1 = new ModelRenderer(this);
		wall_4_r1.setRotationPoint(0.0F, 0.0F, -45.0F);
		wall.addChild(wall_4_r1);
		setRotationAngle(wall_4_r1, 0.0F, 0.1309F, 0.0F);
		wall_4_r1.setTextureOffset(0, 56).addBox(0.0F, -5.0F, -45.0F, 0.0F, 5.0F, 45.0F, 0.0F, false);

		wall_1_r1 = new ModelRenderer(this);
		wall_1_r1.setRotationPoint(0.0F, 0.0F, 0.0F);
		wall.addChild(wall_1_r1);
		setRotationAngle(wall_1_r1, 0.0F, 0.1309F, 0.0F);
		wall_1_r1.setTextureOffset(108, 137).addBox(0.0F, -37.0F, -45.0F, 0.0F, 32.0F, 45.0F, 0.0F, false);

		front_panel = new ModelRenderer(this);
		front_panel.setRotationPoint(0.0F, 0.0F, 0.0F);
		head.addChild(front_panel);
		

		front_panel_5_r1 = new ModelRenderer(this);
		front_panel_5_r1.setRotationPoint(-36.0F, -11.0F, -97.0F);
		front_panel.addChild(front_panel_5_r1);
		setRotationAngle(front_panel_5_r1, 0.2182F, -0.2618F, 0.829F);
		front_panel_5_r1.setTextureOffset(0, 17).addBox(2.0F, -4.0F, -4.0F, 0.0F, 13.0F, 105.0F, 0.0F, false);

		front_panel_4_r1 = new ModelRenderer(this);
		front_panel_4_r1.setRotationPoint(-33.0F, -14.0F, -101.0F);
		front_panel.addChild(front_panel_4_r1);
		setRotationAngle(front_panel_4_r1, 0.0547F, -0.2913F, 1.3722F);
		front_panel_4_r1.setTextureOffset(0, 0).addBox(3.214F, -9.036F, -2.192F, 0.0F, 16.0F, 106.0F, 0.0F, false);

		front_panel_3_r1 = new ModelRenderer(this);
		front_panel_3_r1.setRotationPoint(-21.0F, -14.0F, -114.0F);
		front_panel.addChild(front_panel_3_r1);
		setRotationAngle(front_panel_3_r1, 0.2967F, 0.0F, 0.0F);
		front_panel_3_r1.setTextureOffset(46, 0).addBox(-7.0F, 6.0F, 10.0F, 14.0F, 0.0F, 42.0F, 0.0F, false);

		front_panel_2_r1 = new ModelRenderer(this);
		front_panel_2_r1.setRotationPoint(-9.0F, -14.0F, -101.0F);
		front_panel.addChild(front_panel_2_r1);
		setRotationAngle(front_panel_2_r1, 0.0547F, 0.2913F, -1.3722F);
		front_panel_2_r1.setTextureOffset(0, 0).addBox(-3.214F, -9.036F, -2.192F, 0.0F, 16.0F, 106.0F, 0.0F, false);

		front_panel_1_r1 = new ModelRenderer(this);
		front_panel_1_r1.setRotationPoint(-6.0F, -11.0F, -97.0F);
		front_panel.addChild(front_panel_1_r1);
		setRotationAngle(front_panel_1_r1, 0.2182F, 0.2618F, -0.829F);
		front_panel_1_r1.setTextureOffset(0, 17).addBox(-2.0F, -4.0F, -4.0F, 0.0F, 13.0F, 105.0F, 0.0F, false);

		cockpit = new ModelRenderer(this);
		cockpit.setRotationPoint(2.0F, 0.0F, 0.0F);
		head.addChild(cockpit);
		cockpit.setTextureOffset(92, 42).addBox(-28.0F, -42.0F, -28.0F, 10.0F, 0.0F, 28.0F, 0.0F, false);

		windscreen_3_r1 = new ModelRenderer(this);
		windscreen_3_r1.setRotationPoint(-28.0F, -42.0F, -28.0F);
		cockpit.addChild(windscreen_3_r1);
		setRotationAngle(windscreen_3_r1, 0.3527F, 0.3326F, -0.7256F);
		windscreen_3_r1.setTextureOffset(74, 0).addBox(-13.0F, 0.0F, -42.0F, 13.0F, 0.0F, 42.0F, 0.0F, true);

		windscreen_2_r1 = new ModelRenderer(this);
		windscreen_2_r1.setRotationPoint(-18.0F, -42.0F, -28.0F);
		cockpit.addChild(windscreen_2_r1);
		setRotationAngle(windscreen_2_r1, 0.3527F, -0.3326F, 0.7256F);
		windscreen_2_r1.setTextureOffset(74, 0).addBox(0.0F, 0.0F, -42.0F, 13.0F, 0.0F, 42.0F, 0.0F, false);

		windscreen_1_r1 = new ModelRenderer(this);
		windscreen_1_r1.setRotationPoint(-23.0F, -42.0F, -28.0F);
		cockpit.addChild(windscreen_1_r1);
		setRotationAngle(windscreen_1_r1, 0.48F, 0.0F, 0.0F);
		windscreen_1_r1.setTextureOffset(100, 0).addBox(-5.0F, 0.0F, -42.0F, 10.0F, 0.0F, 42.0F, 0.0F, false);

		top_3_r1 = new ModelRenderer(this);
		top_3_r1.setRotationPoint(-28.0F, -42.0F, -14.0F);
		cockpit.addChild(top_3_r1);
		setRotationAngle(top_3_r1, 0.0F, 0.0F, -0.7418F);
		top_3_r1.setTextureOffset(60, 42).addBox(-16.0F, 0.0F, -14.0F, 16.0F, 0.0F, 28.0F, 0.0F, true);

		top_2_r1 = new ModelRenderer(this);
		top_2_r1.setRotationPoint(-18.0F, -42.0F, -14.0F);
		cockpit.addChild(top_2_r1);
		setRotationAngle(top_2_r1, 0.0F, 0.0F, 0.7418F);
		top_2_r1.setTextureOffset(60, 42).addBox(0.0F, 0.0F, -14.0F, 16.0F, 0.0F, 28.0F, 0.0F, false);

		door = new ModelRenderer(this);
		door.setRotationPoint(0.0F, 24.0F, 0.0F);
		door.setTextureOffset(301, 79).addBox(-21.0F, -37.0F, 78.0F, 4.0F, 37.0F, 24.0F, 0.0F, false);
		door.setTextureOffset(168, 0).addBox(-21.0F, -1.0F, 78.0F, 21.0F, 1.0F, 40.0F, 0.0F, false);
		door.setTextureOffset(333, 49).addBox(-21.0F, -42.0F, 115.0F, 15.0F, 42.0F, 3.0F, 0.0F, false);
		door.setTextureOffset(334, 140).addBox(-21.0F, -41.0F, 78.0F, 15.0F, 41.0F, 2.0F, 0.0F, false);
		door.setTextureOffset(168, 53).addBox(-6.0F, -41.0F, 116.0F, 6.0F, 6.0F, 2.0F, 0.0F, false);
		door.setTextureOffset(44, 199).addBox(-6.0F, -41.0F, 78.0F, 6.0F, 6.0F, 2.0F, 0.0F, false);
		door.setTextureOffset(0, 349).addBox(-17.0F, -41.0F, 99.0F, 11.0F, 41.0F, 1.0F, 0.0F, false);
		door.setTextureOffset(313, 172).addBox(-7.0F, -41.0F, 80.0F, 1.0F, 41.0F, 19.0F, 0.0F, false);

		doorframe = new ModelRenderer(this);
		doorframe.setRotationPoint(0.0F, 0.0F, 91.0F);
		door.addChild(doorframe);
		doorframe.setTextureOffset(11, 0).addBox(-21.0F, -34.0F, 11.0F, 3.0F, 0.0F, 13.0F, 0.0F, false);

		doorframe_1_r1 = new ModelRenderer(this);
		doorframe_1_r1.setRotationPoint(-18.0F, -34.0F, 12.0F);
		doorframe.addChild(doorframe_1_r1);
		setRotationAngle(doorframe_1_r1, 0.0F, 0.0F, 0.3927F);
		doorframe_1_r1.setTextureOffset(88, 57).addBox(0.0F, -3.0F, -1.0F, 0.0F, 3.0F, 13.0F, 0.0F, false);

		door_exterior = new ModelRenderer(this);
		door_exterior.setRotationPoint(-42.0F, 24.0F, 0.0F);
		door_exterior.setTextureOffset(200, 247).addBox(21.0F, -37.0F, 78.0F, 0.0F, 37.0F, 24.0F, 0.0F, false);
		door_exterior.setTextureOffset(246, 140).addBox(21.0F, -37.0F, 115.0F, 0.0F, 37.0F, 3.0F, 0.0F, false);
		door_exterior.setTextureOffset(88, 60).addBox(21.0F, -37.0F, 102.0F, 0.0F, 3.0F, 13.0F, 0.0F, false);
		door_exterior.setTextureOffset(0, 392).addBox(21.0F, -1.0F, 102.0F, 1.0F, 1.0F, 13.0F, 0.0F, false);
		door_exterior.setTextureOffset(288, 137).addBox(22.0F, -34.0F, 102.0F, 1.0F, 33.0F, 13.0F, 0.0F, false);

		floor_r2 = new ModelRenderer(this);
		floor_r2.setRotationPoint(21.0F, 0.0F, 0.0F);
		door_exterior.addChild(floor_r2);
		setRotationAngle(floor_r2, 0.0F, 0.0F, -0.1745F);
		floor_r2.setTextureOffset(283, 1).addBox(0.0F, 0.0F, 78.0F, 1.0F, 8.0F, 40.0F, 0.0F, false);

		door_exterior_end = new ModelRenderer(this);
		door_exterior_end.setRotationPoint(-42.0F, 24.0F, 0.0F);
		door_exterior_end.setTextureOffset(152, 247).addBox(21.0F, -37.0F, 78.0F, 0.0F, 37.0F, 24.0F, 0.0F, false);
		door_exterior_end.setTextureOffset(240, 140).addBox(21.0F, -37.0F, 115.0F, 0.0F, 37.0F, 3.0F, 0.0F, false);
		door_exterior_end.setTextureOffset(0, 21).addBox(21.0F, -37.0F, 102.0F, 0.0F, 3.0F, 13.0F, 0.0F, false);
		door_exterior_end.setTextureOffset(0, 391).addBox(21.0F, -1.0F, 102.0F, 1.0F, 1.0F, 13.0F, 0.0F, false);
		door_exterior_end.setTextureOffset(292, 50).addBox(22.0F, -34.0F, 102.0F, 1.0F, 33.0F, 13.0F, 0.0F, false);

		floor_r3 = new ModelRenderer(this);
		floor_r3.setRotationPoint(21.0F, 0.0F, 0.0F);
		door_exterior_end.addChild(floor_r3);
		setRotationAngle(floor_r3, 0.0F, 0.0F, -0.1745F);
		floor_r3.setTextureOffset(246, 143).addBox(0.0F, 0.0F, 78.0F, 1.0F, 8.0F, 40.0F, 0.0F, false);

		roof = new ModelRenderer(this);
		roof.setRotationPoint(0.0F, 24.0F, -1.0F);
		roof.setTextureOffset(240, 103).addBox(-18.0F, -32.0F, 1.0F, 8.0F, 1.0F, 39.0F, 0.0F, false);
		roof.setTextureOffset(249, 0).addBox(-13.0F, -39.0F, 1.0F, 3.0F, 0.0F, 39.0F, 0.0F, false);
		roof.setTextureOffset(0, 45).addBox(-18.0F, -34.0F, 1.0F, 0.0F, 2.0F, 39.0F, 0.0F, false);
		roof.setTextureOffset(223, 103).addBox(-4.0F, -40.6679F, 1.0F, 4.0F, 0.0F, 39.0F, 0.0F, false);
		roof.setTextureOffset(0, 271).addBox(-11.0F, -40.0F, 1.0F, 1.0F, 1.0F, 39.0F, 0.0F, false);

		inner_roof_3_r1 = new ModelRenderer(this);
		inner_roof_3_r1.setRotationPoint(-9.0655F, -37.834F, 14.0F);
		roof.addChild(inner_roof_3_r1);
		setRotationAngle(inner_roof_3_r1, 0.0F, 0.0F, -0.2094F);
		inner_roof_3_r1.setTextureOffset(229, 0).addBox(-1.0F, -2.0F, -13.0F, 4.0F, 0.0F, 39.0F, 0.0F, false);

		inner_roof_2_r1 = new ModelRenderer(this);
		inner_roof_2_r1.setRotationPoint(-5.1202F, -38.4588F, 14.0F);
		roof.addChild(inner_roof_2_r1);
		setRotationAngle(inner_roof_2_r1, 0.0F, 0.0F, -0.1047F);
		inner_roof_2_r1.setTextureOffset(213, 103).addBox(-2.8798F, -2.0F, -13.0F, 5.0F, 0.0F, 39.0F, 0.0F, false);

		outer_roof_4_r1 = new ModelRenderer(this);
		outer_roof_4_r1.setRotationPoint(-18.0F, -34.0F, 40.0F);
		roof.addChild(outer_roof_4_r1);
		setRotationAngle(outer_roof_4_r1, 0.0F, 0.0F, 0.3927F);
		outer_roof_4_r1.setTextureOffset(0, 40).addBox(0.0F, -3.0F, -39.0F, 0.0F, 3.0F, 39.0F, 0.0F, false);

		outer_roof_3_r1 = new ModelRenderer(this);
		outer_roof_3_r1.setRotationPoint(-14.0F, -39.0F, 40.0F);
		roof.addChild(outer_roof_3_r1);
		setRotationAngle(outer_roof_3_r1, 0.0F, 0.0F, -0.7854F);
		outer_roof_3_r1.setTextureOffset(243, 0).addBox(-4.1522F, -0.2346F, -39.0F, 3.0F, 0.0F, 39.0F, 0.0F, false);

		outer_roof_2_r1 = new ModelRenderer(this);
		outer_roof_2_r1.setRotationPoint(-13.0F, -39.0F, 40.0F);
		roof.addChild(outer_roof_2_r1);
		setRotationAngle(outer_roof_2_r1, 0.0F, 0.0F, -0.3927F);
		outer_roof_2_r1.setTextureOffset(247, 63).addBox(-3.0F, 0.0F, -39.0F, 3.0F, 0.0F, 39.0F, 0.0F, false);

		roof_exterior = new ModelRenderer(this);
		roof_exterior.setRotationPoint(-3.0F, 24.0F, 20.0F);
		roof_exterior.setTextureOffset(6, 0).addBox(-9.0F, -42.0F, -20.0F, 12.0F, 0.0F, 39.0F, 0.0F, false);

		outer_roof_5_r1 = new ModelRenderer(this);
		outer_roof_5_r1.setRotationPoint(-9.0F, -42.0F, 0.0F);
		roof_exterior.addChild(outer_roof_5_r1);
		setRotationAngle(outer_roof_5_r1, 0.0F, 0.0F, -0.2705F);
		outer_roof_5_r1.setTextureOffset(201, 103).addBox(-6.0F, 0.0F, -20.0F, 6.0F, 0.0F, 39.0F, 0.0F, false);

		outer_roof_4_r2 = new ModelRenderer(this);
		outer_roof_4_r2.setRotationPoint(-15.501F, -40.0F, 0.0F);
		roof_exterior.addChild(outer_roof_4_r2);
		setRotationAngle(outer_roof_4_r2, 0.0F, 0.0F, 1.0472F);
		outer_roof_4_r2.setTextureOffset(0, 37).addBox(0.0F, -0.82F, -20.0F, 0.0F, 3.0F, 39.0F, 0.0F, false);

		outer_roof_3_r2 = new ModelRenderer(this);
		outer_roof_3_r2.setRotationPoint(-18.0F, -37.0F, -0.5F);
		roof_exterior.addChild(outer_roof_3_r2);
		setRotationAngle(outer_roof_3_r2, 0.0F, 0.0F, 0.3054F);
		outer_roof_3_r2.setTextureOffset(0, 43).addBox(0.0F, -2.0F, -19.5F, 0.0F, 2.0F, 39.0F, 0.0F, false);

		roof_door = new ModelRenderer(this);
		roof_door.setRotationPoint(0.0F, 24.0F, 78.0F);
		roof_door.setTextureOffset(221, 0).addBox(-4.0F, -40.6679F, 1.0F, 4.0F, 0.0F, 39.0F, 0.0F, false);

		inner_roof_3_r2 = new ModelRenderer(this);
		inner_roof_3_r2.setRotationPoint(-5.1202F, -38.4588F, 14.0F);
		roof_door.addChild(inner_roof_3_r2);
		setRotationAngle(inner_roof_3_r2, 0.0F, 0.0F, -0.1047F);
		inner_roof_3_r2.setTextureOffset(211, 0).addBox(-2.8798F, -2.0F, -13.0F, 5.0F, 0.0F, 39.0F, 0.0F, false);

		inner_roof_2_r2 = new ModelRenderer(this);
		inner_roof_2_r2.setRotationPoint(-9.0655F, -37.834F, 14.0F);
		roof_door.addChild(inner_roof_2_r2);
		setRotationAngle(inner_roof_2_r2, 0.0F, 0.0F, -0.2094F);
		inner_roof_2_r2.setTextureOffset(30, 0).addBox(-4.0F, -2.0F, -13.0F, 7.0F, 0.0F, 39.0F, 0.0F, false);

		outer_roof_2_r2 = new ModelRenderer(this);
		outer_roof_2_r2.setRotationPoint(-14.0F, -39.0F, 40.0F);
		roof_door.addChild(outer_roof_2_r2);
		setRotationAngle(outer_roof_2_r2, 0.0F, 0.0F, -0.7854F);
		outer_roof_2_r2.setTextureOffset(231, 103).addBox(-4.1522F, -0.2346F, -39.0F, 3.0F, 0.0F, 39.0F, 0.0F, false);

		outer_roof_1_r1 = new ModelRenderer(this);
		outer_roof_1_r1.setRotationPoint(-13.0F, -39.0F, 40.0F);
		roof_door.addChild(outer_roof_1_r1);
		setRotationAngle(outer_roof_1_r1, 0.0F, 0.0F, -0.3927F);
		outer_roof_1_r1.setTextureOffset(237, 0).addBox(-3.0F, 0.0F, -39.0F, 3.0F, 0.0F, 39.0F, 0.0F, false);

		roof_exterior_door = new ModelRenderer(this);
		roof_exterior_door.setRotationPoint(-3.0F, 24.0F, 21.0F);
		roof_exterior_door.setTextureOffset(128, 0).addBox(-9.0F, -42.0F, 57.0F, 12.0F, 0.0F, 40.0F, 0.0F, false);

		outer_roof_5_r2 = new ModelRenderer(this);
		outer_roof_5_r2.setRotationPoint(-9.0F, -42.0F, 77.0F);
		roof_exterior_door.addChild(outer_roof_5_r2);
		setRotationAngle(outer_roof_5_r2, 0.0F, 0.0F, -0.2705F);
		outer_roof_5_r2.setTextureOffset(152, 0).addBox(-6.0F, 0.0F, -20.0F, 6.0F, 0.0F, 40.0F, 0.0F, false);

		outer_roof_4_r3 = new ModelRenderer(this);
		outer_roof_4_r3.setRotationPoint(-15.501F, -40.0F, 77.0F);
		roof_exterior_door.addChild(outer_roof_4_r3);
		setRotationAngle(outer_roof_4_r3, 0.0F, 0.0F, 1.0472F);
		outer_roof_4_r3.setTextureOffset(0, 31).addBox(0.0F, -0.82F, -20.0F, 0.0F, 3.0F, 40.0F, 0.0F, false);

		outer_roof_3_r3 = new ModelRenderer(this);
		outer_roof_3_r3.setRotationPoint(-18.0F, -37.0F, 76.5F);
		roof_exterior_door.addChild(outer_roof_3_r3);
		setRotationAngle(outer_roof_3_r3, 0.0F, 0.0F, 0.3054F);
		outer_roof_3_r3.setTextureOffset(0, 34).addBox(0.0F, -2.0F, -19.5F, 0.0F, 2.0F, 40.0F, 0.0F, false);

		luggage_rack = new ModelRenderer(this);
		luggage_rack.setRotationPoint(0.0F, 24.0F, -1.0F);
		luggage_rack.setTextureOffset(140, 42).addBox(-19.0F, -31.0F, 10.0F, 11.0F, 31.0F, 0.0F, 0.0F, false);
		luggage_rack.setTextureOffset(140, 42).addBox(-19.0F, -31.0F, 32.0F, 11.0F, 31.0F, 0.0F, 0.0F, false);
		luggage_rack.setTextureOffset(0, 199).addBox(-19.0F, -11.0F, 10.0F, 11.0F, 1.0F, 22.0F, 0.0F, false);
		luggage_rack.setTextureOffset(0, 199).addBox(-19.0F, -21.0F, 10.0F, 11.0F, 1.0F, 22.0F, 0.0F, false);

		seat = new ModelRenderer(this);
		seat.setRotationPoint(0.0F, 24.0F, 0.0F);
		seat.setTextureOffset(130, 173).addBox(-3.0F, -5.0F, -3.0F, 6.0F, 1.0F, 6.0F, 0.0F, false);
		seat.setTextureOffset(30, 0).addBox(-1.5F, -16.4F, 4.5F, 3.0F, 2.0F, 1.0F, 0.0F, false);
		seat.setTextureOffset(168, 41).addBox(3.0F, -9.0F, -3.0F, 1.0F, 5.0F, 7.0F, 0.0F, false);

		top_right_r1 = new ModelRenderer(this);
		top_right_r1.setRotationPoint(-1.5F, -16.4F, 4.5F);
		seat.addChild(top_right_r1);
		setRotationAngle(top_right_r1, 0.0017F, 0.0F, 0.2618F);
		top_right_r1.setTextureOffset(30, 3).addBox(0.0F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);

		top_left_r1 = new ModelRenderer(this);
		top_left_r1.setRotationPoint(1.5F, -16.4F, 4.5F);
		seat.addChild(top_left_r1);
		setRotationAngle(top_left_r1, 0.0017F, 0.0F, -0.2618F);
		top_left_r1.setTextureOffset(30, 3).addBox(-1.0F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);

		back_right_r1 = new ModelRenderer(this);
		back_right_r1.setRotationPoint(-1.5F, -5.0F, 2.0F);
		seat.addChild(back_right_r1);
		setRotationAngle(back_right_r1, -0.2618F, -0.1745F, 0.0873F);
		back_right_r1.setTextureOffset(26, 24).addBox(-1.5F, -9.75F, 0.0F, 2.0F, 10.0F, 1.0F, 0.0F, true);

		back_left_r1 = new ModelRenderer(this);
		back_left_r1.setRotationPoint(1.5F, -5.0F, 2.0F);
		seat.addChild(back_left_r1);
		setRotationAngle(back_left_r1, -0.2618F, 0.1745F, -0.0873F);
		back_left_r1.setTextureOffset(26, 24).addBox(-0.5F, -9.75F, 0.0F, 2.0F, 10.0F, 1.0F, 0.0F, false);

		back_r1 = new ModelRenderer(this);
		back_r1.setRotationPoint(3.0F, -5.0F, 2.0F);
		seat.addChild(back_r1);
		setRotationAngle(back_r1, -0.2618F, 0.0F, 0.0F);
		back_r1.setTextureOffset(24, 13).addBox(-4.5F, -10.0F, 0.0F, 3.0F, 10.0F, 1.0F, 0.0F, false);

		bb_main = new ModelRenderer(this);
		bb_main.setRotationPoint(0.0F, 24.0F, 0.0F);
		bb_main.setTextureOffset(0, 0).addBox(-6.0F, -35.0F, 79.0F, 12.0F, 34.0F, 0.0F, 0.0F, false);
		bb_main.setTextureOffset(80, 271).addBox(-18.0F, -41.0F, 0.0F, 36.0F, 41.0F, 0.0F, 0.0F, false);
	}

	@Override
	public void setRotationAngles(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		//previously the render function, render code was moved to a method below
	}

	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		window.render(matrixStack, buffer, packedLight, packedOverlay);
		window_exterior.render(matrixStack, buffer, packedLight, packedOverlay);
		window_exterior_end.render(matrixStack, buffer, packedLight, packedOverlay);
		head.render(matrixStack, buffer, packedLight, packedOverlay);
		door.render(matrixStack, buffer, packedLight, packedOverlay);
		door_exterior.render(matrixStack, buffer, packedLight, packedOverlay);
		door_exterior_end.render(matrixStack, buffer, packedLight, packedOverlay);
		roof.render(matrixStack, buffer, packedLight, packedOverlay);
		roof_exterior.render(matrixStack, buffer, packedLight, packedOverlay);
		roof_door.render(matrixStack, buffer, packedLight, packedOverlay);
		roof_exterior_door.render(matrixStack, buffer, packedLight, packedOverlay);
		luggage_rack.render(matrixStack, buffer, packedLight, packedOverlay);
		seat.render(matrixStack, buffer, packedLight, packedOverlay);
		bb_main.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}