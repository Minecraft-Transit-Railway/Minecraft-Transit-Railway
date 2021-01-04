package mtr.model;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class ModelSP1900 extends ModelTrainBase {

	private final ModelPart window;
	private final ModelPart upper_wall_r1;
	private final ModelPart seat;
	private final ModelPart seat_back_top_r1;
	private final ModelPart side_handrail_left;
	private final ModelPart side_handrail_left_7_r1;
	private final ModelPart side_handrail_left_6_r1;
	private final ModelPart side_handrail_left_4_r1;
	private final ModelPart side_handrail_left_3_r1;
	private final ModelPart side_handrail_left_2_r1;
	private final ModelPart side_handrail_right;
	private final ModelPart side_handrail_left_7_r2;
	private final ModelPart side_handrail_left_6_r2;
	private final ModelPart side_handrail_left_4_r2;
	private final ModelPart side_handrail_left_3_r2;
	private final ModelPart side_handrail_left_2_r2;
	private final ModelPart door;
	private final ModelPart door_left;
	private final ModelPart door_left_top_r1;
	private final ModelPart door_right;
	private final ModelPart door_right_top_r1;
	private final ModelPart end;
	private final ModelPart upper_wall_2_r1;
	private final ModelPart upper_wall_1_r1;
	private final ModelPart side_handrail_1;
	private final ModelPart side_handrail_7_r1;
	private final ModelPart side_handrail_6_r1;
	private final ModelPart side_handrail_4_r1;
	private final ModelPart side_handrail_3_r1;
	private final ModelPart side_handrail_2_r1;
	private final ModelPart side_handrail_2;
	private final ModelPart side_handrail_7_r2;
	private final ModelPart side_handrail_6_r2;
	private final ModelPart side_handrail_4_r2;
	private final ModelPart side_handrail_3_r2;
	private final ModelPart side_handrail_2_r2;
	private final ModelPart seat_end_1;
	private final ModelPart seat_back_top_r2;
	private final ModelPart seat_end_2;
	private final ModelPart seat_back_top_r3;
	private final ModelPart roof;
	private final ModelPart inner_roof;
	private final ModelPart inner_roof_6_r1;
	private final ModelPart inner_roof_5_r1;
	private final ModelPart inner_roof_4_r1;
	private final ModelPart inner_roof_3_r1;
	private final ModelPart inner_roof_2_r1;
	private final ModelPart outer_roof;
	private final ModelPart outer_roof_4_r1;
	private final ModelPart outer_roof_3_r1;
	private final ModelPart outer_roof_2_r1;
	private final ModelPart outer_roof_1_r1;
	private final ModelPart light;
	private final ModelPart light_3_r1;
	private final ModelPart light_1_r1;
	private final ModelPart roof_end;
	private final ModelPart vent_2_r1;
	private final ModelPart vent_1_r1;
	private final ModelPart inner_roof_1;
	private final ModelPart inner_roof_6_r2;
	private final ModelPart inner_roof_5_r2;
	private final ModelPart inner_roof_4_r2;
	private final ModelPart inner_roof_3_r2;
	private final ModelPart inner_roof_2_r2;
	private final ModelPart inner_roof_2;
	private final ModelPart inner_roof_6_r3;
	private final ModelPart inner_roof_5_r3;
	private final ModelPart inner_roof_4_r3;
	private final ModelPart inner_roof_3_r3;
	private final ModelPart inner_roof_2_r3;
	private final ModelPart outer_roof_1;
	private final ModelPart outer_roof_3_r2;
	private final ModelPart outer_roof_2_r2;
	private final ModelPart outer_roof_1_r2;
	private final ModelPart outer_roof_2;
	private final ModelPart outer_roof_3_r3;
	private final ModelPart outer_roof_2_r3;
	private final ModelPart outer_roof_1_r3;
	private final ModelPart light_1;
	private final ModelPart light_3_r2;
	private final ModelPart light_1_r2;
	private final ModelPart light_2;
	private final ModelPart light_3_r3;
	private final ModelPart light_1_r3;
	private final ModelPart top_handrail;
	private final ModelPart top_handrail_bottom_right_r1;
	private final ModelPart top_handrail_right_3_r1;
	private final ModelPart top_handrail_right_2_r1;
	private final ModelPart top_handrail_bottom_left_r1;
	private final ModelPart top_handrail_left_3_r1;
	private final ModelPart top_handrail_left_2_r1;
	private final ModelPart handrail_strap;
	private final ModelPart tv_pole;
	private final ModelPart pole_5_r1;
	private final ModelPart pole_4_r1;
	private final ModelPart pole_2_r1;
	private final ModelPart pole_1_r1;
	private final ModelPart tv_right_r1;
	private final ModelPart tv_left_r1;
	private final ModelPart head;
	private final ModelPart driver_door_top_2_r1;
	private final ModelPart driver_door_top_1_r1;
	private final ModelPart upper_wall_2_r2;
	private final ModelPart upper_wall_1_r2;
	private final ModelPart seat_head_1;
	private final ModelPart seat_back_top_r4;
	private final ModelPart seat_head_2;
	private final ModelPart seat_back_top_r5;
	private final ModelPart side_handrail_head_1;
	private final ModelPart side_handrail_7_r3;
	private final ModelPart side_handrail_6_r3;
	private final ModelPart side_handrail_4_r3;
	private final ModelPart side_handrail_3_r3;
	private final ModelPart side_handrail_2_r3;
	private final ModelPart side_handrail_head_2;
	private final ModelPart side_handrail_7_r4;
	private final ModelPart side_handrail_6_r4;
	private final ModelPart side_handrail_4_r4;
	private final ModelPart side_handrail_3_r4;
	private final ModelPart side_handrail_2_r4;
	private final ModelPart front;
	private final ModelPart bottom_corner_2_r1;
	private final ModelPart bottom_corner_1_r1;
	private final ModelPart roof_corner_2_r1;
	private final ModelPart roof_corner_1_r1;
	private final ModelPart roof_middle_corner_2_r1;
	private final ModelPart roof_middle_corner_1_r1;
	private final ModelPart roof_side_2_r1;
	private final ModelPart roof_side_1_r1;
	private final ModelPart top_side_2_r1;
	private final ModelPart top_side_1_r1;
	private final ModelPart bottom_side_2_r1;
	private final ModelPart bottom_side_1_r1;
	private final ModelPart front_side_2_r1;
	private final ModelPart front_side_1_r1;
	private final ModelPart front_bottom_r1;
	private final ModelPart bottom_r1;
	private final ModelPart front_roof_r1;
	private final ModelPart front_middle_r1;
	private final ModelPart top_handrail_head;
	private final ModelPart top_handrail_bottom_right_r2;
	private final ModelPart top_handrail_right_3_r2;
	private final ModelPart top_handrail_right_2_r2;
	private final ModelPart top_handrail_bottom_left_r2;
	private final ModelPart top_handrail_left_3_r2;
	private final ModelPart top_handrail_left_2_r2;
	private final ModelPart handrail_strap_head;
	private final ModelPart bb_main;

	public ModelSP1900() {
		textureWidth = 384;
		textureHeight = 384;
		window = new ModelPart(this);
		window.setPivot(0.0F, 24.0F, 0.0F);
		window.setTextureOffset(58, 178).addCuboid(-21.0F, 0.0F, -16.0F, 21.0F, 7.0F, 32.0F, 0.0F, false);
		window.setTextureOffset(264, 60).addCuboid(-20.0F, -14.0F, -18.0F, 2.0F, 14.0F, 36.0F, 0.0F, false);
		window.setTextureOffset(40, 176).addCuboid(-18.0F, -34.0F, 16.0F, 7.0F, 30.0F, 0.0F, 0.0F, false);
		window.setTextureOffset(40, 176).addCuboid(-18.0F, -34.0F, -16.0F, 7.0F, 30.0F, 0.0F, 0.0F, false);

		upper_wall_r1 = new ModelPart(this);
		upper_wall_r1.setPivot(-20.0F, -14.0F, 0.0F);
		window.addChild(upper_wall_r1);
		setRotationAngle(upper_wall_r1, 0.0F, 0.0F, 0.1107F);
		upper_wall_r1.setTextureOffset(0, 249).addCuboid(0.0F, -19.0F, -18.0F, 2.0F, 19.0F, 36.0F, 0.0F, false);

		seat = new ModelPart(this);
		seat.setPivot(-9.0F, 0.0F, 0.0F);
		window.addChild(seat);
		seat.setTextureOffset(40, 249).addCuboid(-9.0F, -10.0F, -16.0F, 1.0F, 3.0F, 32.0F, 0.0F, false);
		seat.setTextureOffset(286, 110).addCuboid(-9.0F, -6.0F, -16.0F, 7.0F, 1.0F, 32.0F, 0.0F, false);

		seat_back_top_r1 = new ModelPart(this);
		seat_back_top_r1.setPivot(-8.0F, -10.0F, 0.0F);
		seat.addChild(seat_back_top_r1);
		setRotationAngle(seat_back_top_r1, 0.0F, 0.0F, -0.2094F);
		seat_back_top_r1.setTextureOffset(44, 296).addCuboid(-1.0F, -5.0F, -16.0F, 1.0F, 5.0F, 32.0F, 0.0F, false);

		side_handrail_left = new ModelPart(this);
		side_handrail_left.setPivot(0.0F, 0.0F, 0.0F);
		window.addChild(side_handrail_left);
		side_handrail_left.setTextureOffset(0, 0).addCuboid(-12.0F, -16.0F, 15.8F, 0.0F, 10.0F, 0.0F, 0.2F, false);
		side_handrail_left.setTextureOffset(0, 0).addCuboid(-10.3697F, -26.2455F, 15.8F, 0.0F, 3.0F, 0.0F, 0.2F, false);

		side_handrail_left_7_r1 = new ModelPart(this);
		side_handrail_left_7_r1.setPivot(-11.8689F, -31.7477F, 15.8F);
		side_handrail_left.addChild(side_handrail_left_7_r1);
		setRotationAngle(side_handrail_left_7_r1, 0.0F, 0.0F, -0.3491F);
		side_handrail_left_7_r1.setTextureOffset(0, 0).addCuboid(0.0F, -3.0F, 0.0F, 0.0F, 6.0F, 0.0F, 0.2F, false);

		side_handrail_left_6_r1 = new ModelPart(this);
		side_handrail_left_6_r1.setPivot(-10.5751F, -27.5926F, 15.8F);
		side_handrail_left.addChild(side_handrail_left_6_r1);
		setRotationAngle(side_handrail_left_6_r1, 0.0F, 0.0F, -0.1745F);
		side_handrail_left_6_r1.setTextureOffset(0, 0).addCuboid(0.0F, -1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 0.2F, false);

		side_handrail_left_4_r1 = new ModelPart(this);
		side_handrail_left_4_r1.setPivot(-10.5751F, -21.8985F, 15.8F);
		side_handrail_left.addChild(side_handrail_left_4_r1);
		setRotationAngle(side_handrail_left_4_r1, 0.0F, 0.0F, 0.1745F);
		side_handrail_left_4_r1.setTextureOffset(0, 0).addCuboid(0.0F, -1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 0.2F, false);

		side_handrail_left_3_r1 = new ModelPart(this);
		side_handrail_left_3_r1.setPivot(-11.1849F, -19.6228F, 15.8F);
		side_handrail_left.addChild(side_handrail_left_3_r1);
		setRotationAngle(side_handrail_left_3_r1, 0.0F, 0.0F, 0.3491F);
		side_handrail_left_3_r1.setTextureOffset(0, 0).addCuboid(0.0F, -1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 0.2F, false);

		side_handrail_left_2_r1 = new ModelPart(this);
		side_handrail_left_2_r1.setPivot(-11.7947F, -17.347F, 15.8F);
		side_handrail_left.addChild(side_handrail_left_2_r1);
		setRotationAngle(side_handrail_left_2_r1, 0.0F, 0.0F, 0.1745F);
		side_handrail_left_2_r1.setTextureOffset(0, 0).addCuboid(0.0F, -1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 0.2F, false);

		side_handrail_right = new ModelPart(this);
		side_handrail_right.setPivot(0.0F, 0.0F, 0.0F);
		window.addChild(side_handrail_right);
		side_handrail_right.setTextureOffset(0, 0).addCuboid(-12.0F, -16.0F, -15.8F, 0.0F, 10.0F, 0.0F, 0.2F, false);
		side_handrail_right.setTextureOffset(0, 0).addCuboid(-10.3697F, -26.2455F, -15.8F, 0.0F, 3.0F, 0.0F, 0.2F, false);

		side_handrail_left_7_r2 = new ModelPart(this);
		side_handrail_left_7_r2.setPivot(-11.8689F, -31.7477F, -15.8F);
		side_handrail_right.addChild(side_handrail_left_7_r2);
		setRotationAngle(side_handrail_left_7_r2, 0.0F, 0.0F, -0.3491F);
		side_handrail_left_7_r2.setTextureOffset(0, 0).addCuboid(0.0F, -3.0F, 0.0F, 0.0F, 6.0F, 0.0F, 0.2F, false);

		side_handrail_left_6_r2 = new ModelPart(this);
		side_handrail_left_6_r2.setPivot(-10.5751F, -27.5926F, -15.8F);
		side_handrail_right.addChild(side_handrail_left_6_r2);
		setRotationAngle(side_handrail_left_6_r2, 0.0F, 0.0F, -0.1745F);
		side_handrail_left_6_r2.setTextureOffset(0, 0).addCuboid(0.0F, -1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 0.2F, false);

		side_handrail_left_4_r2 = new ModelPart(this);
		side_handrail_left_4_r2.setPivot(-10.5751F, -21.8985F, -15.8F);
		side_handrail_right.addChild(side_handrail_left_4_r2);
		setRotationAngle(side_handrail_left_4_r2, 0.0F, 0.0F, 0.1745F);
		side_handrail_left_4_r2.setTextureOffset(0, 0).addCuboid(0.0F, -1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 0.2F, false);

		side_handrail_left_3_r2 = new ModelPart(this);
		side_handrail_left_3_r2.setPivot(-11.1849F, -19.6228F, -15.8F);
		side_handrail_right.addChild(side_handrail_left_3_r2);
		setRotationAngle(side_handrail_left_3_r2, 0.0F, 0.0F, 0.3491F);
		side_handrail_left_3_r2.setTextureOffset(0, 0).addCuboid(0.0F, -1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 0.2F, false);

		side_handrail_left_2_r2 = new ModelPart(this);
		side_handrail_left_2_r2.setPivot(-11.7947F, -17.347F, -15.8F);
		side_handrail_right.addChild(side_handrail_left_2_r2);
		setRotationAngle(side_handrail_left_2_r2, 0.0F, 0.0F, 0.1745F);
		side_handrail_left_2_r2.setTextureOffset(0, 0).addCuboid(0.0F, -1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 0.2F, false);

		door = new ModelPart(this);
		door.setPivot(0.0F, 24.0F, 0.0F);
		door.setTextureOffset(132, 0).addCuboid(-21.0F, 0.0F, -16.0F, 21.0F, 7.0F, 32.0F, 0.0F, false);

		door_left = new ModelPart(this);
		door_left.setPivot(0.0F, 0.0F, 0.0F);
		door.addChild(door_left);
		door_left.setTextureOffset(252, 0).addCuboid(-20.8F, -14.0F, 0.0F, 1.0F, 14.0F, 16.0F, 0.0F, false);

		door_left_top_r1 = new ModelPart(this);
		door_left_top_r1.setPivot(-19.8061F, -13.8896F, 0.0F);
		door_left.addChild(door_left_top_r1);
		setRotationAngle(door_left_top_r1, 0.0F, 0.0F, 0.1107F);
		door_left_top_r1.setTextureOffset(0, 249).addCuboid(-1.0F, -19.0F, 0.0F, 1.0F, 19.0F, 16.0F, 0.0F, false);

		door_right = new ModelPart(this);
		door_right.setPivot(0.0F, 0.0F, 0.0F);
		door.addChild(door_right);
		door_right.setTextureOffset(74, 249).addCuboid(-20.8F, -14.0F, -16.0F, 1.0F, 14.0F, 16.0F, 0.0F, false);

		door_right_top_r1 = new ModelPart(this);
		door_right_top_r1.setPivot(-19.8061F, -13.8896F, 0.0F);
		door_right.addChild(door_right_top_r1);
		setRotationAngle(door_right_top_r1, 0.0F, 0.0F, 0.1107F);
		door_right_top_r1.setTextureOffset(118, 251).addCuboid(-1.0F, -19.0F, -16.0F, 1.0F, 19.0F, 16.0F, 0.0F, false);

		end = new ModelPart(this);
		end.setPivot(0.0F, 24.0F, 0.0F);
		end.setTextureOffset(0, 55).addCuboid(-21.0F, 0.0F, -32.0F, 42.0F, 7.0F, 48.0F, 0.0F, false);
		end.setTextureOffset(40, 176).addCuboid(11.0F, -34.0F, 16.0F, 7.0F, 30.0F, 0.0F, 0.0F, true);
		end.setTextureOffset(40, 176).addCuboid(-18.0F, -34.0F, 16.0F, 7.0F, 30.0F, 0.0F, 0.0F, false);
		end.setTextureOffset(170, 197).addCuboid(18.0F, -14.0F, -36.0F, 2.0F, 14.0F, 54.0F, 0.0F, true);
		end.setTextureOffset(188, 124).addCuboid(-20.0F, -14.0F, -36.0F, 2.0F, 14.0F, 54.0F, 0.0F, false);
		end.setTextureOffset(0, 304).addCuboid(11.0F, -33.0F, -36.0F, 7.0F, 33.0F, 12.0F, 0.0F, false);
		end.setTextureOffset(190, 120).addCuboid(-18.0F, -33.0F, -36.0F, 7.0F, 33.0F, 12.0F, 0.0F, false);
		end.setTextureOffset(282, 192).addCuboid(-18.0F, -44.0F, -36.0F, 36.0F, 11.0F, 12.0F, 0.0F, false);

		upper_wall_2_r1 = new ModelPart(this);
		upper_wall_2_r1.setPivot(-20.0F, -14.0F, 0.0F);
		end.addChild(upper_wall_2_r1);
		setRotationAngle(upper_wall_2_r1, 0.0F, 0.0F, 0.1107F);
		upper_wall_2_r1.setTextureOffset(0, 176).addCuboid(0.0F, -19.0F, -36.0F, 2.0F, 19.0F, 54.0F, 0.0F, false);

		upper_wall_1_r1 = new ModelPart(this);
		upper_wall_1_r1.setPivot(20.0F, -14.0F, 0.0F);
		end.addChild(upper_wall_1_r1);
		setRotationAngle(upper_wall_1_r1, 0.0F, 0.0F, -0.1107F);
		upper_wall_1_r1.setTextureOffset(112, 178).addCuboid(-2.0F, -19.0F, -36.0F, 2.0F, 19.0F, 54.0F, 0.0F, true);

		side_handrail_1 = new ModelPart(this);
		side_handrail_1.setPivot(0.0F, 0.0F, 0.0F);
		end.addChild(side_handrail_1);
		side_handrail_1.setTextureOffset(0, 0).addCuboid(12.0F, -16.0F, 15.8F, 0.0F, 10.0F, 0.0F, 0.2F, false);
		side_handrail_1.setTextureOffset(0, 0).addCuboid(10.3697F, -26.2455F, 15.8F, 0.0F, 3.0F, 0.0F, 0.2F, false);

		side_handrail_7_r1 = new ModelPart(this);
		side_handrail_7_r1.setPivot(11.8689F, -31.7477F, 0.0F);
		side_handrail_1.addChild(side_handrail_7_r1);
		setRotationAngle(side_handrail_7_r1, 0.0F, 0.0F, 0.3491F);
		side_handrail_7_r1.setTextureOffset(0, 0).addCuboid(0.0F, -3.0F, 15.8F, 0.0F, 6.0F, 0.0F, 0.2F, false);

		side_handrail_6_r1 = new ModelPart(this);
		side_handrail_6_r1.setPivot(10.5751F, -27.5926F, 0.0F);
		side_handrail_1.addChild(side_handrail_6_r1);
		setRotationAngle(side_handrail_6_r1, 0.0F, 0.0F, 0.1745F);
		side_handrail_6_r1.setTextureOffset(0, 0).addCuboid(0.0F, -1.0F, 15.8F, 0.0F, 2.0F, 0.0F, 0.2F, false);

		side_handrail_4_r1 = new ModelPart(this);
		side_handrail_4_r1.setPivot(10.5751F, -21.8985F, 0.0F);
		side_handrail_1.addChild(side_handrail_4_r1);
		setRotationAngle(side_handrail_4_r1, 0.0F, 0.0F, -0.1745F);
		side_handrail_4_r1.setTextureOffset(0, 0).addCuboid(0.0F, -1.0F, 15.8F, 0.0F, 2.0F, 0.0F, 0.2F, false);

		side_handrail_3_r1 = new ModelPart(this);
		side_handrail_3_r1.setPivot(11.1849F, -19.6228F, 0.0F);
		side_handrail_1.addChild(side_handrail_3_r1);
		setRotationAngle(side_handrail_3_r1, 0.0F, 0.0F, -0.3491F);
		side_handrail_3_r1.setTextureOffset(0, 0).addCuboid(0.0F, -1.0F, 15.8F, 0.0F, 2.0F, 0.0F, 0.2F, false);

		side_handrail_2_r1 = new ModelPart(this);
		side_handrail_2_r1.setPivot(11.7947F, -17.347F, 0.0F);
		side_handrail_1.addChild(side_handrail_2_r1);
		setRotationAngle(side_handrail_2_r1, 0.0F, 0.0F, -0.1745F);
		side_handrail_2_r1.setTextureOffset(0, 0).addCuboid(0.0F, -1.0F, 15.8F, 0.0F, 2.0F, 0.0F, 0.2F, false);

		side_handrail_2 = new ModelPart(this);
		side_handrail_2.setPivot(0.0F, 0.0F, 0.0F);
		end.addChild(side_handrail_2);
		side_handrail_2.setTextureOffset(0, 0).addCuboid(-12.0F, -16.0F, 15.8F, 0.0F, 10.0F, 0.0F, 0.2F, false);
		side_handrail_2.setTextureOffset(0, 0).addCuboid(-10.3697F, -26.2455F, 15.8F, 0.0F, 3.0F, 0.0F, 0.2F, false);

		side_handrail_7_r2 = new ModelPart(this);
		side_handrail_7_r2.setPivot(-11.8689F, -31.7477F, 0.0F);
		side_handrail_2.addChild(side_handrail_7_r2);
		setRotationAngle(side_handrail_7_r2, 0.0F, 0.0F, -0.3491F);
		side_handrail_7_r2.setTextureOffset(0, 0).addCuboid(0.0F, -3.0F, 15.8F, 0.0F, 6.0F, 0.0F, 0.2F, false);

		side_handrail_6_r2 = new ModelPart(this);
		side_handrail_6_r2.setPivot(-10.5751F, -27.5926F, 0.0F);
		side_handrail_2.addChild(side_handrail_6_r2);
		setRotationAngle(side_handrail_6_r2, 0.0F, 0.0F, -0.1745F);
		side_handrail_6_r2.setTextureOffset(0, 0).addCuboid(0.0F, -1.0F, 15.8F, 0.0F, 2.0F, 0.0F, 0.2F, false);

		side_handrail_4_r2 = new ModelPart(this);
		side_handrail_4_r2.setPivot(-10.5751F, -21.8985F, 0.0F);
		side_handrail_2.addChild(side_handrail_4_r2);
		setRotationAngle(side_handrail_4_r2, 0.0F, 0.0F, 0.1745F);
		side_handrail_4_r2.setTextureOffset(0, 0).addCuboid(0.0F, -1.0F, 15.8F, 0.0F, 2.0F, 0.0F, 0.2F, false);

		side_handrail_3_r2 = new ModelPart(this);
		side_handrail_3_r2.setPivot(-11.1849F, -19.6228F, 0.0F);
		side_handrail_2.addChild(side_handrail_3_r2);
		setRotationAngle(side_handrail_3_r2, 0.0F, 0.0F, 0.3491F);
		side_handrail_3_r2.setTextureOffset(0, 0).addCuboid(0.0F, -1.0F, 15.8F, 0.0F, 2.0F, 0.0F, 0.2F, false);

		side_handrail_2_r2 = new ModelPart(this);
		side_handrail_2_r2.setPivot(-11.7947F, -17.347F, 0.0F);
		side_handrail_2.addChild(side_handrail_2_r2);
		setRotationAngle(side_handrail_2_r2, 0.0F, 0.0F, 0.1745F);
		side_handrail_2_r2.setTextureOffset(0, 0).addCuboid(0.0F, -1.0F, 15.8F, 0.0F, 2.0F, 0.0F, 0.2F, false);

		seat_end_1 = new ModelPart(this);
		seat_end_1.setPivot(0.0F, 0.0F, 0.0F);
		end.addChild(seat_end_1);
		seat_end_1.setTextureOffset(198, 265).addCuboid(17.0F, -10.0F, -24.0F, 1.0F, 3.0F, 40.0F, 0.0F, false);
		seat_end_1.setTextureOffset(228, 192).addCuboid(11.0F, -6.0F, -24.0F, 7.0F, 1.0F, 40.0F, 0.0F, false);

		seat_back_top_r2 = new ModelPart(this);
		seat_back_top_r2.setPivot(17.0F, -10.0F, 0.0F);
		seat_end_1.addChild(seat_back_top_r2);
		setRotationAngle(seat_back_top_r2, 0.0F, 0.0F, 0.2094F);
		seat_back_top_r2.setTextureOffset(76, 251).addCuboid(0.0F, -5.0F, -24.0F, 1.0F, 5.0F, 40.0F, 0.0F, false);

		seat_end_2 = new ModelPart(this);
		seat_end_2.setPivot(0.0F, 0.0F, 0.0F);
		end.addChild(seat_end_2);
		seat_end_2.setTextureOffset(198, 265).addCuboid(-18.0F, -10.0F, -24.0F, 1.0F, 3.0F, 40.0F, 0.0F, false);
		seat_end_2.setTextureOffset(228, 192).addCuboid(-18.0F, -6.0F, -24.0F, 7.0F, 1.0F, 40.0F, 0.0F, false);

		seat_back_top_r3 = new ModelPart(this);
		seat_back_top_r3.setPivot(-17.0F, -10.0F, 0.0F);
		seat_end_2.addChild(seat_back_top_r3);
		setRotationAngle(seat_back_top_r3, 0.0F, 0.0F, -0.2094F);
		seat_back_top_r3.setTextureOffset(76, 251).addCuboid(-1.0F, -5.0F, -24.0F, 1.0F, 5.0F, 40.0F, 0.0F, false);

		roof = new ModelPart(this);
		roof.setPivot(0.0F, 24.0F, 0.0F);


		inner_roof = new ModelPart(this);
		inner_roof.setPivot(-4.19F, -35.8985F, 0.0F);
		roof.addChild(inner_roof);
		inner_roof.setTextureOffset(0, 142).addCuboid(-13.81F, 3.8985F, -16.0F, 3.0F, 0.0F, 32.0F, 0.0F, false);
		inner_roof.setTextureOffset(0, 0).addCuboid(0.19F, -0.1015F, -16.0F, 4.0F, 0.0F, 32.0F, 0.0F, false);

		inner_roof_6_r1 = new ModelPart(this);
		inner_roof_6_r1.setPivot(0.0F, 0.0F, 0.0F);
		inner_roof.addChild(inner_roof_6_r1);
		setRotationAngle(inner_roof_6_r1, 0.0F, 0.0F, -0.2618F);
		inner_roof_6_r1.setTextureOffset(0, 110).addCuboid(-2.5F, 0.0F, -16.0F, 3.0F, 0.0F, 32.0F, 0.0F, false);

		inner_roof_5_r1 = new ModelPart(this);
		inner_roof_5_r1.setPivot(-7.1131F, 0.9059F, 0.0F);
		inner_roof.addChild(inner_roof_5_r1);
		setRotationAngle(inner_roof_5_r1, 0.0F, 0.0F, -0.2618F);
		inner_roof_5_r1.setTextureOffset(150, 144).addCuboid(-1.0F, 0.0F, -16.0F, 2.0F, 0.0F, 32.0F, 0.0F, false);

		inner_roof_4_r1 = new ModelPart(this);
		inner_roof_4_r1.setPivot(-8.0785F, 1.1656F, 0.0F);
		inner_roof.addChild(inner_roof_4_r1);
		setRotationAngle(inner_roof_4_r1, 0.0F, 0.0F, -0.5236F);
		inner_roof_4_r1.setTextureOffset(6, 142).addCuboid(-1.0F, 0.0F, -16.0F, 1.0F, 0.0F, 32.0F, 0.0F, false);

		inner_roof_3_r1 = new ModelPart(this);
		inner_roof_3_r1.setPivot(-8.9445F, 1.6656F, 0.0F);
		inner_roof.addChild(inner_roof_3_r1);
		setRotationAngle(inner_roof_3_r1, 0.0F, 0.0F, -1.0472F);
		inner_roof_3_r1.setTextureOffset(154, 112).addCuboid(-2.0F, 0.0F, -16.0F, 2.0F, 0.0F, 32.0F, 0.0F, false);

		inner_roof_2_r1 = new ModelPart(this);
		inner_roof_2_r1.setPivot(-10.81F, 3.8985F, 0.0F);
		inner_roof.addChild(inner_roof_2_r1);
		setRotationAngle(inner_roof_2_r1, 0.0F, 0.0F, -0.5236F);
		inner_roof_2_r1.setTextureOffset(154, 144).addCuboid(0.0F, 0.0F, -16.0F, 1.0F, 0.0F, 32.0F, 0.0F, false);

		outer_roof = new ModelPart(this);
		outer_roof.setPivot(0.0F, 0.0F, 0.0F);
		roof.addChild(outer_roof);
		outer_roof.setTextureOffset(116, 0).addCuboid(-6.0F, -44.0F, -16.0F, 6.0F, 0.0F, 32.0F, 0.0F, false);

		outer_roof_4_r1 = new ModelPart(this);
		outer_roof_4_r1.setPivot(-9.4468F, -43.3922F, 0.0F);
		outer_roof.addChild(outer_roof_4_r1);
		setRotationAngle(outer_roof_4_r1, 0.0F, 0.0F, -0.1745F);
		outer_roof_4_r1.setTextureOffset(100, 0).addCuboid(-4.5F, 0.0F, -16.0F, 8.0F, 0.0F, 32.0F, 0.0F, false);

		outer_roof_3_r1 = new ModelPart(this);
		outer_roof_3_r1.setPivot(-15.1775F, -41.8608F, 0.0F);
		outer_roof.addChild(outer_roof_3_r1);
		setRotationAngle(outer_roof_3_r1, 0.0F, 0.0F, -0.5236F);
		outer_roof_3_r1.setTextureOffset(0, 55).addCuboid(-1.5F, 0.0F, -16.0F, 3.0F, 0.0F, 32.0F, 0.0F, false);

		outer_roof_2_r1 = new ModelPart(this);
		outer_roof_2_r1.setPivot(-16.9765F, -40.2448F, 0.0F);
		outer_roof.addChild(outer_roof_2_r1);
		setRotationAngle(outer_roof_2_r1, 0.0F, 0.0F, -1.0472F);
		outer_roof_2_r1.setTextureOffset(128, 0).addCuboid(-1.0F, 0.0F, -16.0F, 2.0F, 0.0F, 32.0F, 0.0F, false);

		outer_roof_1_r1 = new ModelPart(this);
		outer_roof_1_r1.setPivot(-19.0F, -32.0F, 0.0F);
		outer_roof.addChild(outer_roof_1_r1);
		setRotationAngle(outer_roof_1_r1, 0.0F, 0.0F, 0.1107F);
		outer_roof_1_r1.setTextureOffset(290, 290).addCuboid(0.0F, -8.0F, -16.0F, 1.0F, 8.0F, 32.0F, 0.0F, false);

		light = new ModelPart(this);
		light.setPivot(-7.0376F, -35.0016F, 0.0F);
		roof.addChild(light);
		light.setTextureOffset(150, 112).addCuboid(-2.4335F, 0.2501F, -16.0F, 2.0F, 0.0F, 32.0F, 0.0F, false);

		light_3_r1 = new ModelPart(this);
		light_3_r1.setPivot(0.0F, 0.0F, 0.0F);
		light.addChild(light_3_r1);
		setRotationAngle(light_3_r1, 0.0F, 0.0F, -0.5236F);
		light_3_r1.setTextureOffset(6, 55).addCuboid(-0.5F, 0.0F, -16.0F, 1.0F, 0.0F, 32.0F, 0.0F, false);

		light_1_r1 = new ModelPart(this);
		light_1_r1.setPivot(-2.8665F, 0.0001F, 0.0F);
		light.addChild(light_1_r1);
		setRotationAngle(light_1_r1, 0.0F, 0.0F, 0.5236F);
		light_1_r1.setTextureOffset(6, 110).addCuboid(-0.5F, 0.0F, -16.0F, 1.0F, 0.0F, 32.0F, 0.0F, false);

		roof_end = new ModelPart(this);
		roof_end.setPivot(0.0F, 24.0F, 0.0F);
		roof_end.setTextureOffset(0, 110).addCuboid(-8.0F, -45.0F, -36.0F, 16.0F, 2.0F, 64.0F, 0.0F, false);

		vent_2_r1 = new ModelPart(this);
		vent_2_r1.setPivot(-8.0F, -45.0F, 0.0F);
		roof_end.addChild(vent_2_r1);
		setRotationAngle(vent_2_r1, 0.0F, 0.0F, -0.3491F);
		vent_2_r1.setTextureOffset(116, 46).addCuboid(-9.0F, 0.0F, -36.0F, 9.0F, 2.0F, 64.0F, 0.0F, true);

		vent_1_r1 = new ModelPart(this);
		vent_1_r1.setPivot(8.0F, -45.0F, 0.0F);
		roof_end.addChild(vent_1_r1);
		setRotationAngle(vent_1_r1, 0.0F, 0.0F, 0.3491F);
		vent_1_r1.setTextureOffset(96, 112).addCuboid(0.0F, 0.0F, -36.0F, 9.0F, 2.0F, 64.0F, 0.0F, false);

		inner_roof_1 = new ModelPart(this);
		inner_roof_1.setPivot(-4.19F, -35.8985F, 0.0F);
		roof_end.addChild(inner_roof_1);
		inner_roof_1.setTextureOffset(0, 110).addCuboid(-13.81F, 3.8985F, -24.0F, 3.0F, 0.0F, 40.0F, 0.0F, false);
		inner_roof_1.setTextureOffset(0, 55).addCuboid(0.19F, -0.1015F, -24.0F, 4.0F, 0.0F, 40.0F, 0.0F, false);

		inner_roof_6_r2 = new ModelPart(this);
		inner_roof_6_r2.setPivot(0.0F, 0.0F, 0.0F);
		inner_roof_1.addChild(inner_roof_6_r2);
		setRotationAngle(inner_roof_6_r2, 0.0F, 0.0F, -0.2618F);
		inner_roof_6_r2.setTextureOffset(104, 55).addCuboid(-2.5F, 0.0F, -24.0F, 3.0F, 0.0F, 40.0F, 0.0F, false);

		inner_roof_5_r2 = new ModelPart(this);
		inner_roof_5_r2.setPivot(-7.1131F, 0.9059F, 0.0F);
		inner_roof_1.addChild(inner_roof_5_r2);
		setRotationAngle(inner_roof_5_r2, 0.0F, 0.0F, -0.2618F);
		inner_roof_5_r2.setTextureOffset(122, 55).addCuboid(-1.0F, 0.0F, -24.0F, 2.0F, 0.0F, 40.0F, 0.0F, false);

		inner_roof_4_r2 = new ModelPart(this);
		inner_roof_4_r2.setPivot(-8.0785F, 1.1656F, 0.0F);
		inner_roof_1.addChild(inner_roof_4_r2);
		setRotationAngle(inner_roof_4_r2, 0.0F, 0.0F, -0.5236F);
		inner_roof_4_r2.setTextureOffset(138, 112).addCuboid(-1.0F, 0.0F, -24.0F, 1.0F, 0.0F, 40.0F, 0.0F, false);

		inner_roof_3_r2 = new ModelPart(this);
		inner_roof_3_r2.setPivot(-8.9445F, 1.6656F, 0.0F);
		inner_roof_1.addChild(inner_roof_3_r2);
		setRotationAngle(inner_roof_3_r2, 0.0F, 0.0F, -1.0472F);
		inner_roof_3_r2.setTextureOffset(126, 55).addCuboid(-2.0F, 0.0F, -24.0F, 2.0F, 0.0F, 40.0F, 0.0F, false);

		inner_roof_2_r2 = new ModelPart(this);
		inner_roof_2_r2.setPivot(-10.81F, 3.8985F, 0.0F);
		inner_roof_1.addChild(inner_roof_2_r2);
		setRotationAngle(inner_roof_2_r2, 0.0F, 0.0F, -0.5236F);
		inner_roof_2_r2.setTextureOffset(140, 112).addCuboid(0.0F, 0.0F, -24.0F, 1.0F, 0.0F, 40.0F, 0.0F, false);

		inner_roof_2 = new ModelPart(this);
		inner_roof_2.setPivot(4.19F, -35.8985F, 0.0F);
		roof_end.addChild(inner_roof_2);
		inner_roof_2.setTextureOffset(0, 110).addCuboid(10.81F, 3.8985F, -24.0F, 3.0F, 0.0F, 40.0F, 0.0F, true);
		inner_roof_2.setTextureOffset(0, 55).addCuboid(-4.19F, -0.1015F, -24.0F, 4.0F, 0.0F, 40.0F, 0.0F, true);

		inner_roof_6_r3 = new ModelPart(this);
		inner_roof_6_r3.setPivot(0.0F, 0.0F, 0.0F);
		inner_roof_2.addChild(inner_roof_6_r3);
		setRotationAngle(inner_roof_6_r3, 0.0F, 0.0F, 0.2618F);
		inner_roof_6_r3.setTextureOffset(104, 55).addCuboid(-0.5F, 0.0F, -24.0F, 3.0F, 0.0F, 40.0F, 0.0F, true);

		inner_roof_5_r3 = new ModelPart(this);
		inner_roof_5_r3.setPivot(7.1131F, 0.9059F, 0.0F);
		inner_roof_2.addChild(inner_roof_5_r3);
		setRotationAngle(inner_roof_5_r3, 0.0F, 0.0F, 0.2618F);
		inner_roof_5_r3.setTextureOffset(122, 55).addCuboid(-1.0F, 0.0F, -24.0F, 2.0F, 0.0F, 40.0F, 0.0F, true);

		inner_roof_4_r3 = new ModelPart(this);
		inner_roof_4_r3.setPivot(8.0785F, 1.1656F, 0.0F);
		inner_roof_2.addChild(inner_roof_4_r3);
		setRotationAngle(inner_roof_4_r3, 0.0F, 0.0F, 0.5236F);
		inner_roof_4_r3.setTextureOffset(138, 112).addCuboid(0.0F, 0.0F, -24.0F, 1.0F, 0.0F, 40.0F, 0.0F, true);

		inner_roof_3_r3 = new ModelPart(this);
		inner_roof_3_r3.setPivot(8.9445F, 1.6656F, 0.0F);
		inner_roof_2.addChild(inner_roof_3_r3);
		setRotationAngle(inner_roof_3_r3, 0.0F, 0.0F, 1.0472F);
		inner_roof_3_r3.setTextureOffset(126, 55).addCuboid(0.0F, 0.0F, -24.0F, 2.0F, 0.0F, 40.0F, 0.0F, true);

		inner_roof_2_r3 = new ModelPart(this);
		inner_roof_2_r3.setPivot(10.81F, 3.8985F, 0.0F);
		inner_roof_2.addChild(inner_roof_2_r3);
		setRotationAngle(inner_roof_2_r3, 0.0F, 0.0F, 0.5236F);
		inner_roof_2_r3.setTextureOffset(140, 112).addCuboid(-1.0F, 0.0F, -24.0F, 1.0F, 0.0F, 40.0F, 0.0F, true);

		outer_roof_1 = new ModelPart(this);
		outer_roof_1.setPivot(0.0F, 0.0F, 0.0F);
		roof_end.addChild(outer_roof_1);


		outer_roof_3_r2 = new ModelPart(this);
		outer_roof_3_r2.setPivot(-15.1775F, -41.8608F, 0.0F);
		outer_roof_1.addChild(outer_roof_3_r2);
		setRotationAngle(outer_roof_3_r2, 0.0F, 0.0F, -0.5236F);
		outer_roof_3_r2.setTextureOffset(94, 112).addCuboid(-1.5F, 0.0F, -36.0F, 3.0F, 0.0F, 52.0F, 0.0F, false);

		outer_roof_2_r2 = new ModelPart(this);
		outer_roof_2_r2.setPivot(-16.9765F, -40.2448F, 0.0F);
		outer_roof_1.addChild(outer_roof_2_r2);
		setRotationAngle(outer_roof_2_r2, 0.0F, 0.0F, -1.0472F);
		outer_roof_2_r2.setTextureOffset(104, 112).addCuboid(-1.0F, 0.0F, -36.0F, 2.0F, 0.0F, 52.0F, 0.0F, false);

		outer_roof_1_r2 = new ModelPart(this);
		outer_roof_1_r2.setPivot(-19.0F, -32.0F, 0.0F);
		outer_roof_1.addChild(outer_roof_1_r2);
		setRotationAngle(outer_roof_1_r2, 0.0F, 0.0F, 0.1107F);
		outer_roof_1_r2.setTextureOffset(210, 60).addCuboid(0.0F, -8.0F, -36.0F, 1.0F, 8.0F, 52.0F, 0.0F, false);

		outer_roof_2 = new ModelPart(this);
		outer_roof_2.setPivot(0.0F, 0.0F, 0.0F);
		roof_end.addChild(outer_roof_2);


		outer_roof_3_r3 = new ModelPart(this);
		outer_roof_3_r3.setPivot(15.1775F, -41.8608F, 0.0F);
		outer_roof_2.addChild(outer_roof_3_r3);
		setRotationAngle(outer_roof_3_r3, 0.0F, 0.0F, 0.5236F);
		outer_roof_3_r3.setTextureOffset(94, 112).addCuboid(-1.5F, 0.0F, -36.0F, 3.0F, 0.0F, 52.0F, 0.0F, true);

		outer_roof_2_r3 = new ModelPart(this);
		outer_roof_2_r3.setPivot(16.9765F, -40.2448F, 0.0F);
		outer_roof_2.addChild(outer_roof_2_r3);
		setRotationAngle(outer_roof_2_r3, 0.0F, 0.0F, 1.0472F);
		outer_roof_2_r3.setTextureOffset(104, 112).addCuboid(-1.0F, 0.0F, -36.0F, 2.0F, 0.0F, 52.0F, 0.0F, true);

		outer_roof_1_r3 = new ModelPart(this);
		outer_roof_1_r3.setPivot(19.0F, -32.0F, 0.0F);
		outer_roof_2.addChild(outer_roof_1_r3);
		setRotationAngle(outer_roof_1_r3, 0.0F, 0.0F, -0.1107F);
		outer_roof_1_r3.setTextureOffset(198, 0).addCuboid(-1.0F, -8.0F, -36.0F, 1.0F, 8.0F, 52.0F, 0.0F, true);

		light_1 = new ModelPart(this);
		light_1.setPivot(-7.0376F, -35.0016F, 0.0F);
		roof_end.addChild(light_1);
		light_1.setTextureOffset(118, 55).addCuboid(-2.4335F, 0.2501F, -24.0F, 2.0F, 0.0F, 40.0F, 0.0F, false);

		light_3_r2 = new ModelPart(this);
		light_3_r2.setPivot(0.0F, 0.0F, 0.0F);
		light_1.addChild(light_3_r2);
		setRotationAngle(light_3_r2, 0.0F, 0.0F, -0.5236F);
		light_3_r2.setTextureOffset(136, 55).addCuboid(-0.5F, 0.0F, -24.0F, 1.0F, 0.0F, 40.0F, 0.0F, false);

		light_1_r2 = new ModelPart(this);
		light_1_r2.setPivot(-2.8665F, 0.0001F, 0.0F);
		light_1.addChild(light_1_r2);
		setRotationAngle(light_1_r2, 0.0F, 0.0F, 0.5236F);
		light_1_r2.setTextureOffset(138, 55).addCuboid(-0.5F, 0.0F, -24.0F, 1.0F, 0.0F, 40.0F, 0.0F, false);

		light_2 = new ModelPart(this);
		light_2.setPivot(7.0376F, -35.0016F, 0.0F);
		roof_end.addChild(light_2);
		light_2.setTextureOffset(118, 55).addCuboid(0.4335F, 0.2501F, -24.0F, 2.0F, 0.0F, 40.0F, 0.0F, true);

		light_3_r3 = new ModelPart(this);
		light_3_r3.setPivot(0.0F, 0.0F, 0.0F);
		light_2.addChild(light_3_r3);
		setRotationAngle(light_3_r3, 0.0F, 0.0F, 0.5236F);
		light_3_r3.setTextureOffset(136, 55).addCuboid(-0.5F, 0.0F, -24.0F, 1.0F, 0.0F, 40.0F, 0.0F, true);

		light_1_r3 = new ModelPart(this);
		light_1_r3.setPivot(2.8665F, 0.0001F, 0.0F);
		light_2.addChild(light_1_r3);
		setRotationAngle(light_1_r3, 0.0F, 0.0F, -0.5236F);
		light_1_r3.setTextureOffset(138, 55).addCuboid(-0.5F, 0.0F, -24.0F, 1.0F, 0.0F, 40.0F, 0.0F, true);

		top_handrail = new ModelPart(this);
		top_handrail.setPivot(0.0F, 24.0F, 0.0F);
		top_handrail.setTextureOffset(0, 0).addCuboid(-5.0F, -36.0F, 15.8F, 0.0F, 3.0F, 0.0F, 0.2F, false);
		top_handrail.setTextureOffset(0, 0).addCuboid(-5.0F, -36.0F, -15.8F, 0.0F, 3.0F, 0.0F, 0.2F, false);

		top_handrail_bottom_right_r1 = new ModelPart(this);
		top_handrail_bottom_right_r1.setPivot(-5.0F, -31.0876F, -6.8876F);
		top_handrail.addChild(top_handrail_bottom_right_r1);
		setRotationAngle(top_handrail_bottom_right_r1, -1.5708F, 0.0F, 0.0F);
		top_handrail_bottom_right_r1.setTextureOffset(0, 0).addCuboid(0.0F, -7.0F, 0.0F, 0.0F, 14.0F, 0.0F, 0.2F, false);

		top_handrail_right_3_r1 = new ModelPart(this);
		top_handrail_right_3_r1.setPivot(-5.0F, -31.4108F, -14.5938F);
		top_handrail.addChild(top_handrail_right_3_r1);
		setRotationAngle(top_handrail_right_3_r1, 1.0472F, 0.0F, 0.0F);
		top_handrail_right_3_r1.setTextureOffset(0, 0).addCuboid(0.0F, -0.5F, 0.0F, 0.0F, 1.0F, 0.0F, 0.2F, false);

		top_handrail_right_2_r1 = new ModelPart(this);
		top_handrail_right_2_r1.setPivot(-5.0F, -32.2938F, -15.4768F);
		top_handrail.addChild(top_handrail_right_2_r1);
		setRotationAngle(top_handrail_right_2_r1, 0.5236F, 0.0F, 0.0F);
		top_handrail_right_2_r1.setTextureOffset(0, 0).addCuboid(0.0F, -0.5F, 0.0F, 0.0F, 1.0F, 0.0F, 0.2F, false);

		top_handrail_bottom_left_r1 = new ModelPart(this);
		top_handrail_bottom_left_r1.setPivot(-5.0F, -31.0876F, 6.8876F);
		top_handrail.addChild(top_handrail_bottom_left_r1);
		setRotationAngle(top_handrail_bottom_left_r1, -1.5708F, 0.0F, 0.0F);
		top_handrail_bottom_left_r1.setTextureOffset(0, 0).addCuboid(0.0F, -7.0F, 0.0F, 0.0F, 14.0F, 0.0F, 0.2F, false);

		top_handrail_left_3_r1 = new ModelPart(this);
		top_handrail_left_3_r1.setPivot(-5.0F, -31.4108F, 14.5938F);
		top_handrail.addChild(top_handrail_left_3_r1);
		setRotationAngle(top_handrail_left_3_r1, -1.0472F, 0.0F, 0.0F);
		top_handrail_left_3_r1.setTextureOffset(0, 0).addCuboid(0.0F, -0.5F, 0.0F, 0.0F, 1.0F, 0.0F, 0.2F, false);

		top_handrail_left_2_r1 = new ModelPart(this);
		top_handrail_left_2_r1.setPivot(-5.0F, -32.2938F, 15.4768F);
		top_handrail.addChild(top_handrail_left_2_r1);
		setRotationAngle(top_handrail_left_2_r1, -0.5236F, 0.0F, 0.0F);
		top_handrail_left_2_r1.setTextureOffset(0, 0).addCuboid(0.0F, -0.5F, 0.0F, 0.0F, 1.0F, 0.0F, 0.2F, false);

		handrail_strap = new ModelPart(this);
		handrail_strap.setPivot(0.0F, 0.0F, 0.0F);
		top_handrail.addChild(handrail_strap);
		handrail_strap.setTextureOffset(26, 7).addCuboid(-6.0F, -32.0F, -12.0F, 2.0F, 4.0F, 0.0F, 0.0F, false);
		handrail_strap.setTextureOffset(26, 7).addCuboid(-6.0F, -32.0F, -6.0F, 2.0F, 4.0F, 0.0F, 0.0F, false);
		handrail_strap.setTextureOffset(26, 7).addCuboid(-6.0F, -32.0F, 0.0F, 2.0F, 4.0F, 0.0F, 0.0F, false);
		handrail_strap.setTextureOffset(26, 7).addCuboid(-6.0F, -32.0F, 6.0F, 2.0F, 4.0F, 0.0F, 0.0F, false);
		handrail_strap.setTextureOffset(26, 7).addCuboid(-6.0F, -32.0F, 12.0F, 2.0F, 4.0F, 0.0F, 0.0F, false);

		tv_pole = new ModelPart(this);
		tv_pole.setPivot(0.0F, 24.0F, 0.0F);
		tv_pole.setTextureOffset(26, 40).addCuboid(-4.0F, -36.0F, -1.0F, 8.0F, 6.0F, 2.0F, 0.0F, false);
		tv_pole.setTextureOffset(4, 0).addCuboid(0.0F, -27.5F, 0.0F, 0.0F, 28.0F, 0.0F, 0.2F, false);
		tv_pole.setTextureOffset(4, 0).addCuboid(-1.0F, -27.5F, 0.0F, 2.0F, 0.0F, 0.0F, 0.2F, false);

		pole_5_r1 = new ModelPart(this);
		pole_5_r1.setPivot(-3.3885F, -29.8726F, 0.0F);
		tv_pole.addChild(pole_5_r1);
		setRotationAngle(pole_5_r1, 0.0F, 0.0F, 1.2217F);
		pole_5_r1.setTextureOffset(4, 0).addCuboid(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 0.0F, 0.2F, false);

		pole_4_r1 = new ModelPart(this);
		pole_4_r1.setPivot(-2.0683F, -28.1521F, 0.0F);
		tv_pole.addChild(pole_4_r1);
		setRotationAngle(pole_4_r1, 0.0F, 0.0F, 0.6109F);
		pole_4_r1.setTextureOffset(4, 0).addCuboid(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 0.0F, 0.2F, false);

		pole_2_r1 = new ModelPart(this);
		pole_2_r1.setPivot(2.0683F, -28.1521F, 0.0F);
		tv_pole.addChild(pole_2_r1);
		setRotationAngle(pole_2_r1, 0.0F, 0.0F, -0.6109F);
		pole_2_r1.setTextureOffset(4, 0).addCuboid(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 0.0F, 0.2F, false);

		pole_1_r1 = new ModelPart(this);
		pole_1_r1.setPivot(3.3885F, -29.8726F, 0.0F);
		tv_pole.addChild(pole_1_r1);
		setRotationAngle(pole_1_r1, 0.0F, 0.0F, -1.2217F);
		pole_1_r1.setTextureOffset(4, 0).addCuboid(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 0.0F, 0.2F, false);

		tv_right_r1 = new ModelPart(this);
		tv_right_r1.setPivot(0.0F, -33.5331F, -0.8686F);
		tv_pole.addChild(tv_right_r1);
		setRotationAngle(tv_right_r1, -0.1047F, 3.1416F, 0.0F);
		tv_right_r1.setTextureOffset(26, 95).addCuboid(-4.0F, -3.5F, -0.5F, 8.0F, 7.0F, 1.0F, 0.0F, false);

		tv_left_r1 = new ModelPart(this);
		tv_left_r1.setPivot(0.0F, -33.5331F, 0.8686F);
		tv_pole.addChild(tv_left_r1);
		setRotationAngle(tv_left_r1, -0.1047F, 0.0F, 0.0F);
		tv_left_r1.setTextureOffset(26, 95).addCuboid(-4.0F, -3.5F, -0.5F, 8.0F, 7.0F, 1.0F, 0.0F, false);

		head = new ModelPart(this);
		head.setPivot(0.0F, 24.0F, 0.0F);
		head.setTextureOffset(0, 0).addCuboid(-21.0F, 0.0F, -16.0F, 42.0F, 7.0F, 48.0F, 0.0F, false);
		head.setTextureOffset(122, 260).addCuboid(-20.0F, -14.0F, -18.0F, 2.0F, 14.0F, 36.0F, 0.0F, false);
		head.setTextureOffset(252, 0).addCuboid(18.0F, -14.0F, -18.0F, 2.0F, 14.0F, 36.0F, 0.0F, true);
		head.setTextureOffset(286, 233).addCuboid(-18.0F, -36.0F, 8.0F, 36.0F, 36.0F, 2.0F, 0.0F, false);
		head.setTextureOffset(40, 176).addCuboid(-18.0F, -34.0F, -16.0F, 7.0F, 30.0F, 0.0F, 0.0F, false);
		head.setTextureOffset(40, 176).addCuboid(11.0F, -34.0F, -16.0F, 7.0F, 30.0F, 0.0F, 0.0F, true);
		head.setTextureOffset(206, 0).addCuboid(-20.8F, -14.0F, 16.0F, 1.0F, 14.0F, 16.0F, 0.0F, false);
		head.setTextureOffset(132, 178).addCuboid(19.8F, -14.0F, 16.0F, 1.0F, 14.0F, 16.0F, 0.0F, true);

		driver_door_top_2_r1 = new ModelPart(this);
		driver_door_top_2_r1.setPivot(19.8061F, -13.8896F, 0.0F);
		head.addChild(driver_door_top_2_r1);
		setRotationAngle(driver_door_top_2_r1, 0.0F, 0.0F, -0.1107F);
		driver_door_top_2_r1.setTextureOffset(228, 192).addCuboid(0.0F, -19.0F, 16.0F, 1.0F, 19.0F, 16.0F, 0.0F, true);

		driver_door_top_1_r1 = new ModelPart(this);
		driver_door_top_1_r1.setPivot(-19.8061F, -13.8896F, 0.0F);
		head.addChild(driver_door_top_1_r1);
		setRotationAngle(driver_door_top_1_r1, 0.0F, 0.0F, 0.1107F);
		driver_door_top_1_r1.setTextureOffset(246, 120).addCuboid(-1.0F, -19.0F, 16.0F, 1.0F, 19.0F, 16.0F, 0.0F, false);

		upper_wall_2_r2 = new ModelPart(this);
		upper_wall_2_r2.setPivot(20.0F, -14.0F, 0.0F);
		head.addChild(upper_wall_2_r2);
		setRotationAngle(upper_wall_2_r2, 0.0F, 0.0F, -0.1107F);
		upper_wall_2_r2.setTextureOffset(246, 246).addCuboid(-2.0F, -19.0F, -18.0F, 2.0F, 19.0F, 36.0F, 0.0F, true);

		upper_wall_1_r2 = new ModelPart(this);
		upper_wall_1_r2.setPivot(-20.0F, -14.0F, 0.0F);
		head.addChild(upper_wall_1_r2);
		setRotationAngle(upper_wall_1_r2, 0.0F, 0.0F, 0.1107F);
		upper_wall_1_r2.setTextureOffset(246, 120).addCuboid(0.0F, -19.0F, -18.0F, 2.0F, 19.0F, 36.0F, 0.0F, false);

		seat_head_1 = new ModelPart(this);
		seat_head_1.setPivot(0.0F, 0.0F, 0.0F);
		head.addChild(seat_head_1);
		seat_head_1.setTextureOffset(162, 265).addCuboid(-18.0F, -10.0F, -16.0F, 1.0F, 3.0F, 24.0F, 0.0F, false);
		seat_head_1.setTextureOffset(198, 60).addCuboid(-18.0F, -6.0F, -16.0F, 7.0F, 1.0F, 24.0F, 0.0F, false);

		seat_back_top_r4 = new ModelPart(this);
		seat_back_top_r4.setPivot(-17.0F, -10.0F, 0.0F);
		seat_head_1.addChild(seat_back_top_r4);
		setRotationAngle(seat_back_top_r4, 0.0F, 0.0F, -0.2094F);
		seat_back_top_r4.setTextureOffset(170, 192).addCuboid(-1.0F, -5.0F, -16.0F, 1.0F, 5.0F, 24.0F, 0.0F, false);

		seat_head_2 = new ModelPart(this);
		seat_head_2.setPivot(0.0F, 0.0F, 0.0F);
		head.addChild(seat_head_2);
		seat_head_2.setTextureOffset(162, 265).addCuboid(17.0F, -10.0F, -16.0F, 1.0F, 3.0F, 24.0F, 0.0F, true);
		seat_head_2.setTextureOffset(198, 60).addCuboid(11.0F, -6.0F, -16.0F, 7.0F, 1.0F, 24.0F, 0.0F, true);

		seat_back_top_r5 = new ModelPart(this);
		seat_back_top_r5.setPivot(17.0F, -10.0F, 0.0F);
		seat_head_2.addChild(seat_back_top_r5);
		setRotationAngle(seat_back_top_r5, 0.0F, 0.0F, 0.2094F);
		seat_back_top_r5.setTextureOffset(170, 192).addCuboid(0.0F, -5.0F, -16.0F, 1.0F, 5.0F, 24.0F, 0.0F, true);

		side_handrail_head_1 = new ModelPart(this);
		side_handrail_head_1.setPivot(0.0F, 0.0F, 0.0F);
		head.addChild(side_handrail_head_1);
		side_handrail_head_1.setTextureOffset(0, 0).addCuboid(-12.0F, -16.0F, -15.8F, 0.0F, 10.0F, 0.0F, 0.2F, false);
		side_handrail_head_1.setTextureOffset(0, 0).addCuboid(-10.3697F, -26.2455F, -15.8F, 0.0F, 3.0F, 0.0F, 0.2F, false);

		side_handrail_7_r3 = new ModelPart(this);
		side_handrail_7_r3.setPivot(-11.8689F, -31.7477F, 0.0F);
		side_handrail_head_1.addChild(side_handrail_7_r3);
		setRotationAngle(side_handrail_7_r3, 0.0F, 0.0F, -0.3491F);
		side_handrail_7_r3.setTextureOffset(0, 0).addCuboid(0.0F, -3.0F, -15.8F, 0.0F, 6.0F, 0.0F, 0.2F, false);

		side_handrail_6_r3 = new ModelPart(this);
		side_handrail_6_r3.setPivot(-10.5751F, -27.5926F, 0.0F);
		side_handrail_head_1.addChild(side_handrail_6_r3);
		setRotationAngle(side_handrail_6_r3, 0.0F, 0.0F, -0.1745F);
		side_handrail_6_r3.setTextureOffset(0, 0).addCuboid(0.0F, -1.0F, -15.8F, 0.0F, 2.0F, 0.0F, 0.2F, false);

		side_handrail_4_r3 = new ModelPart(this);
		side_handrail_4_r3.setPivot(-10.5751F, -21.8985F, 0.0F);
		side_handrail_head_1.addChild(side_handrail_4_r3);
		setRotationAngle(side_handrail_4_r3, 0.0F, 0.0F, 0.1745F);
		side_handrail_4_r3.setTextureOffset(0, 0).addCuboid(0.0F, -1.0F, -15.8F, 0.0F, 2.0F, 0.0F, 0.2F, false);

		side_handrail_3_r3 = new ModelPart(this);
		side_handrail_3_r3.setPivot(-11.1849F, -19.6228F, 0.0F);
		side_handrail_head_1.addChild(side_handrail_3_r3);
		setRotationAngle(side_handrail_3_r3, 0.0F, 0.0F, 0.3491F);
		side_handrail_3_r3.setTextureOffset(0, 0).addCuboid(0.0F, -1.0F, -15.8F, 0.0F, 2.0F, 0.0F, 0.2F, false);

		side_handrail_2_r3 = new ModelPart(this);
		side_handrail_2_r3.setPivot(-11.7947F, -17.347F, 0.0F);
		side_handrail_head_1.addChild(side_handrail_2_r3);
		setRotationAngle(side_handrail_2_r3, 0.0F, 0.0F, 0.1745F);
		side_handrail_2_r3.setTextureOffset(0, 0).addCuboid(0.0F, -1.0F, -15.8F, 0.0F, 2.0F, 0.0F, 0.2F, false);

		side_handrail_head_2 = new ModelPart(this);
		side_handrail_head_2.setPivot(0.0F, 0.0F, 0.0F);
		head.addChild(side_handrail_head_2);
		side_handrail_head_2.setTextureOffset(0, 0).addCuboid(12.0F, -16.0F, -15.8F, 0.0F, 10.0F, 0.0F, 0.2F, false);
		side_handrail_head_2.setTextureOffset(0, 0).addCuboid(10.3697F, -26.2455F, -15.8F, 0.0F, 3.0F, 0.0F, 0.2F, false);

		side_handrail_7_r4 = new ModelPart(this);
		side_handrail_7_r4.setPivot(11.8689F, -31.7477F, 0.0F);
		side_handrail_head_2.addChild(side_handrail_7_r4);
		setRotationAngle(side_handrail_7_r4, 0.0F, 0.0F, 0.3491F);
		side_handrail_7_r4.setTextureOffset(0, 0).addCuboid(0.0F, -3.0F, -15.8F, 0.0F, 6.0F, 0.0F, 0.2F, false);

		side_handrail_6_r4 = new ModelPart(this);
		side_handrail_6_r4.setPivot(10.5751F, -27.5926F, 0.0F);
		side_handrail_head_2.addChild(side_handrail_6_r4);
		setRotationAngle(side_handrail_6_r4, 0.0F, 0.0F, 0.1745F);
		side_handrail_6_r4.setTextureOffset(0, 0).addCuboid(0.0F, -1.0F, -15.8F, 0.0F, 2.0F, 0.0F, 0.2F, false);

		side_handrail_4_r4 = new ModelPart(this);
		side_handrail_4_r4.setPivot(10.5751F, -21.8985F, 0.0F);
		side_handrail_head_2.addChild(side_handrail_4_r4);
		setRotationAngle(side_handrail_4_r4, 0.0F, 0.0F, -0.1745F);
		side_handrail_4_r4.setTextureOffset(0, 0).addCuboid(0.0F, -1.0F, -15.8F, 0.0F, 2.0F, 0.0F, 0.2F, false);

		side_handrail_3_r4 = new ModelPart(this);
		side_handrail_3_r4.setPivot(11.1849F, -19.6228F, 0.0F);
		side_handrail_head_2.addChild(side_handrail_3_r4);
		setRotationAngle(side_handrail_3_r4, 0.0F, 0.0F, -0.3491F);
		side_handrail_3_r4.setTextureOffset(0, 0).addCuboid(0.0F, -1.0F, -15.8F, 0.0F, 2.0F, 0.0F, 0.2F, false);

		side_handrail_2_r4 = new ModelPart(this);
		side_handrail_2_r4.setPivot(11.7947F, -17.347F, 0.0F);
		side_handrail_head_2.addChild(side_handrail_2_r4);
		setRotationAngle(side_handrail_2_r4, 0.0F, 0.0F, -0.1745F);
		side_handrail_2_r4.setTextureOffset(0, 0).addCuboid(0.0F, -1.0F, -15.8F, 0.0F, 2.0F, 0.0F, 0.2F, false);

		front = new ModelPart(this);
		front.setPivot(0.0F, 0.0F, 0.0F);
		head.addChild(front);


		bottom_corner_2_r1 = new ModelPart(this);
		bottom_corner_2_r1.setPivot(13.451F, 2.0181F, 50.7099F);
		front.addChild(bottom_corner_2_r1);
		setRotationAngle(bottom_corner_2_r1, -0.1745F, 0.5236F, 0.0873F);
		bottom_corner_2_r1.setTextureOffset(0, 162).addCuboid(-4.5F, -2.5F, 0.0F, 9.0F, 5.0F, 0.0F, 0.0F, false);

		bottom_corner_1_r1 = new ModelPart(this);
		bottom_corner_1_r1.setPivot(-13.451F, 2.0181F, 50.7099F);
		front.addChild(bottom_corner_1_r1);
		setRotationAngle(bottom_corner_1_r1, -0.1745F, -0.5236F, -0.0873F);
		bottom_corner_1_r1.setTextureOffset(40, 162).addCuboid(-4.5F, -2.5F, 0.0F, 9.0F, 5.0F, 0.0F, 0.0F, false);

		roof_corner_2_r1 = new ModelPart(this);
		roof_corner_2_r1.setPivot(16.7928F, -39.5613F, 34.8655F);
		front.addChild(roof_corner_2_r1);
		setRotationAngle(roof_corner_2_r1, 1.0472F, 0.0F, 1.0472F);
		roof_corner_2_r1.setTextureOffset(26, 3).addCuboid(-1.5F, -1.0F, 0.0F, 3.0F, 2.0F, 0.0F, 0.0F, false);

		roof_corner_1_r1 = new ModelPart(this);
		roof_corner_1_r1.setPivot(-16.7928F, -39.5613F, 34.8655F);
		front.addChild(roof_corner_1_r1);
		setRotationAngle(roof_corner_1_r1, 1.0472F, 0.0F, -1.0472F);
		roof_corner_1_r1.setTextureOffset(26, 5).addCuboid(-1.5F, -1.0F, 0.0F, 3.0F, 2.0F, 0.0F, 0.0F, false);

		roof_middle_corner_2_r1 = new ModelPart(this);
		roof_middle_corner_2_r1.setPivot(14.8026F, -41.2114F, 35.2985F);
		front.addChild(roof_middle_corner_2_r1);
		setRotationAngle(roof_middle_corner_2_r1, 1.0472F, 0.0F, 0.5236F);
		roof_middle_corner_2_r1.setTextureOffset(26, 26).addCuboid(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, 0.0F, false);

		roof_middle_corner_1_r1 = new ModelPart(this);
		roof_middle_corner_1_r1.setPivot(-14.8026F, -41.2114F, 35.2985F);
		front.addChild(roof_middle_corner_1_r1);
		setRotationAngle(roof_middle_corner_1_r1, 1.0472F, 0.0F, -0.5236F);
		roof_middle_corner_1_r1.setTextureOffset(26, 0).addCuboid(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, 0.0F, false);

		roof_side_2_r1 = new ModelPart(this);
		roof_side_2_r1.setPivot(9.6786F, -41.8273F, 36.5976F);
		front.addChild(roof_side_2_r1);
		setRotationAngle(roof_side_2_r1, 1.0472F, 0.0F, 0.1745F);
		roof_side_2_r1.setTextureOffset(96, 162).addCuboid(-4.0F, -3.0F, 0.0F, 8.0F, 6.0F, 0.0F, 0.0F, false);

		roof_side_1_r1 = new ModelPart(this);
		roof_side_1_r1.setPivot(-9.6786F, -41.8273F, 36.5976F);
		front.addChild(roof_side_1_r1);
		setRotationAngle(roof_side_1_r1, 1.0472F, 0.0F, -0.1745F);
		roof_side_1_r1.setTextureOffset(112, 164).addCuboid(-4.0F, -3.0F, 0.0F, 8.0F, 6.0F, 0.0F, 0.0F, false);

		top_side_2_r1 = new ModelPart(this);
		top_side_2_r1.setPivot(17.6914F, -26.7346F, 38.2783F);
		front.addChild(top_side_2_r1);
		setRotationAngle(top_side_2_r1, 0.0F, 1.309F, -0.1107F);
		top_side_2_r1.setTextureOffset(0, 110).addCuboid(-6.5F, -13.0F, 0.0F, 13.0F, 26.0F, 0.0F, 0.0F, false);

		top_side_1_r1 = new ModelPart(this);
		top_side_1_r1.setPivot(-17.6914F, -26.7346F, 38.2783F);
		front.addChild(top_side_1_r1);
		setRotationAngle(top_side_1_r1, 0.0F, -1.309F, 0.1107F);
		top_side_1_r1.setTextureOffset(0, 136).addCuboid(-6.5F, -13.0F, 0.0F, 13.0F, 26.0F, 0.0F, 0.0F, false);

		bottom_side_2_r1 = new ModelPart(this);
		bottom_side_2_r1.setPivot(18.3403F, -3.5F, 41.176F);
		front.addChild(bottom_side_2_r1);
		setRotationAngle(bottom_side_2_r1, 0.0F, 1.309F, 0.0F);
		bottom_side_2_r1.setTextureOffset(198, 85).addCuboid(-9.5F, -10.5F, 0.0F, 19.0F, 21.0F, 0.0F, 0.0F, false);

		bottom_side_1_r1 = new ModelPart(this);
		bottom_side_1_r1.setPivot(-18.3403F, -3.5F, 41.176F);
		front.addChild(bottom_side_1_r1);
		setRotationAngle(bottom_side_1_r1, 0.0F, -1.309F, 0.0F);
		bottom_side_1_r1.setTextureOffset(188, 265).addCuboid(-9.5F, -10.5F, 0.0F, 19.0F, 21.0F, 0.0F, 0.0F, false);

		front_side_2_r1 = new ModelPart(this);
		front_side_2_r1.setPivot(13.6611F, -21.1867F, 43.5695F);
		front.addChild(front_side_2_r1);
		setRotationAngle(front_side_2_r1, 0.3491F, 0.5236F, 0.0873F);
		front_side_2_r1.setTextureOffset(328, 22).addCuboid(-6.5F, -22.0F, 0.0F, 13.0F, 44.0F, 0.0F, 0.0F, false);

		front_side_1_r1 = new ModelPart(this);
		front_side_1_r1.setPivot(-13.6611F, -21.1867F, 43.5695F);
		front.addChild(front_side_1_r1);
		setRotationAngle(front_side_1_r1, 0.3491F, -0.5236F, -0.0873F);
		front_side_1_r1.setTextureOffset(0, 55).addCuboid(-6.5F, -22.0F, 0.0F, 13.0F, 44.0F, 0.0F, 0.0F, false);

		front_bottom_r1 = new ModelPart(this);
		front_bottom_r1.setPivot(0.0F, 1.7064F, 52.9026F);
		front.addChild(front_bottom_r1);
		setRotationAngle(front_bottom_r1, -0.1745F, 0.0F, 0.0F);
		front_bottom_r1.setTextureOffset(132, 39).addCuboid(-10.0F, -2.5F, 0.0F, 20.0F, 5.0F, 0.0F, 0.0F, false);

		bottom_r1 = new ModelPart(this);
		bottom_r1.setPivot(0.0F, 4.6192F, 42.7393F);
		front.addChild(bottom_r1);
		setRotationAngle(bottom_r1, -1.3526F, 0.0F, 0.0F);
		bottom_r1.setTextureOffset(292, 0).addCuboid(-21.0F, -11.0F, 0.0F, 42.0F, 22.0F, 0.0F, 0.0F, false);

		front_roof_r1 = new ModelPart(this);
		front_roof_r1.setPivot(0.0F, -42.5001F, 36.5976F);
		front.addChild(front_roof_r1);
		setRotationAngle(front_roof_r1, 1.0472F, 0.0F, 0.0F);
		front_roof_r1.setTextureOffset(132, 95).addCuboid(-6.0F, -3.0F, 0.0F, 12.0F, 6.0F, 0.0F, 0.0F, false);

		front_middle_r1 = new ModelPart(this);
		front_middle_r1.setPivot(0.0F, -21.4283F, 45.8123F);
		front.addChild(front_middle_r1);
		setRotationAngle(front_middle_r1, 0.3491F, 0.0F, 0.0F);
		front_middle_r1.setTextureOffset(0, 176).addCuboid(-10.0F, -22.0F, 0.0F, 20.0F, 44.0F, 0.0F, 0.0F, false);

		top_handrail_head = new ModelPart(this);
		top_handrail_head.setPivot(0.0F, 24.0F, 0.0F);
		top_handrail_head.setTextureOffset(0, 0).addCuboid(-5.0F, -36.0F, 3.8F, 0.0F, 3.0F, 0.0F, 0.2F, false);
		top_handrail_head.setTextureOffset(0, 0).addCuboid(-5.0F, -36.0F, -15.8F, 0.0F, 3.0F, 0.0F, 0.2F, false);

		top_handrail_bottom_right_r2 = new ModelPart(this);
		top_handrail_bottom_right_r2.setPivot(-5.0F, -31.0876F, -6.8876F);
		top_handrail_head.addChild(top_handrail_bottom_right_r2);
		setRotationAngle(top_handrail_bottom_right_r2, -1.5708F, 0.0F, 0.0F);
		top_handrail_bottom_right_r2.setTextureOffset(0, 0).addCuboid(0.0F, -1.0F, 0.0F, 0.0F, 8.0F, 0.0F, 0.2F, false);

		top_handrail_right_3_r2 = new ModelPart(this);
		top_handrail_right_3_r2.setPivot(-5.0F, -31.4108F, -14.5938F);
		top_handrail_head.addChild(top_handrail_right_3_r2);
		setRotationAngle(top_handrail_right_3_r2, 1.0472F, 0.0F, 0.0F);
		top_handrail_right_3_r2.setTextureOffset(0, 0).addCuboid(0.0F, -0.5F, 0.0F, 0.0F, 1.0F, 0.0F, 0.2F, false);

		top_handrail_right_2_r2 = new ModelPart(this);
		top_handrail_right_2_r2.setPivot(-5.0F, -32.2938F, -15.4768F);
		top_handrail_head.addChild(top_handrail_right_2_r2);
		setRotationAngle(top_handrail_right_2_r2, 0.5236F, 0.0F, 0.0F);
		top_handrail_right_2_r2.setTextureOffset(0, 0).addCuboid(0.0F, -0.5F, 0.0F, 0.0F, 1.0F, 0.0F, 0.2F, false);

		top_handrail_bottom_left_r2 = new ModelPart(this);
		top_handrail_bottom_left_r2.setPivot(-5.0F, -31.0876F, 0.8876F);
		top_handrail_head.addChild(top_handrail_bottom_left_r2);
		setRotationAngle(top_handrail_bottom_left_r2, -1.5708F, 0.0F, 0.0F);
		top_handrail_bottom_left_r2.setTextureOffset(0, 0).addCuboid(0.0F, -1.0F, 0.0F, 0.0F, 8.0F, 0.0F, 0.2F, false);

		top_handrail_left_3_r2 = new ModelPart(this);
		top_handrail_left_3_r2.setPivot(-5.0F, -31.4108F, 2.5938F);
		top_handrail_head.addChild(top_handrail_left_3_r2);
		setRotationAngle(top_handrail_left_3_r2, -1.0472F, 0.0F, 0.0F);
		top_handrail_left_3_r2.setTextureOffset(0, 0).addCuboid(0.0F, -0.5F, 0.0F, 0.0F, 1.0F, 0.0F, 0.2F, false);

		top_handrail_left_2_r2 = new ModelPart(this);
		top_handrail_left_2_r2.setPivot(-5.0F, -32.2938F, 3.4768F);
		top_handrail_head.addChild(top_handrail_left_2_r2);
		setRotationAngle(top_handrail_left_2_r2, -0.5236F, 0.0F, 0.0F);
		top_handrail_left_2_r2.setTextureOffset(0, 0).addCuboid(0.0F, -0.5F, 0.0F, 0.0F, 1.0F, 0.0F, 0.2F, false);

		handrail_strap_head = new ModelPart(this);
		handrail_strap_head.setPivot(0.0F, 0.0F, 0.0F);
		top_handrail_head.addChild(handrail_strap_head);
		handrail_strap_head.setTextureOffset(26, 7).addCuboid(-6.0F, -32.0F, -12.0F, 2.0F, 4.0F, 0.0F, 0.0F, false);
		handrail_strap_head.setTextureOffset(26, 7).addCuboid(-6.0F, -32.0F, -6.0F, 2.0F, 4.0F, 0.0F, 0.0F, false);
		handrail_strap_head.setTextureOffset(26, 7).addCuboid(-6.0F, -32.0F, 0.0F, 2.0F, 4.0F, 0.0F, 0.0F, false);

		bb_main = new ModelPart(this);
		bb_main.setPivot(0.0F, 24.0F, 0.0F);
		bb_main.setTextureOffset(4, 0).addCuboid(0.0F, -36.0F, 0.0F, 0.0F, 36.0F, 0.0F, 0.2F, false);
	}

	@Override
	public void render(MatrixStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		render(matrixStack, buffer, packedLight, 8, 3, true, false);
	}

	@Override
	protected void renderWindowPositions(MatrixStack matrices, VertexConsumer vertices, int light, int position) {
		window.setPivot(0, 8, position);
		setRotationAngle(window, 0, 0, 0);
		window.render(matrices, vertices, light, OverlayTexture.DEFAULT_UV);
		setRotationAngle(window, 0, (float) Math.PI, 0);
		window.render(matrices, vertices, light, OverlayTexture.DEFAULT_UV);

		roof.setPivot(0, 8, position);
		setRotationAngle(roof, 0, 0, 0);
		roof.render(matrices, vertices, light, OverlayTexture.DEFAULT_UV);
		setRotationAngle(roof, 0, (float) Math.PI, 0);
		roof.render(matrices, vertices, light, OverlayTexture.DEFAULT_UV);

		top_handrail.setPivot(0, 8, position);
		setRotationAngle(top_handrail, 0, 0, 0);
		top_handrail.render(matrices, vertices, light, OverlayTexture.DEFAULT_UV);
		setRotationAngle(top_handrail, 0, (float) Math.PI, 0);
		top_handrail.render(matrices, vertices, light, OverlayTexture.DEFAULT_UV);
	}

	@Override
	protected void renderDoorPositions(MatrixStack matrices, VertexConsumer vertices, int light, int position, float doorLeftValue, float doorRightValue) {
		door.setPivot(0, 8, position);

		door_left.setPivot(0, 0, doorRightValue);
		door_right.setPivot(0, 0, -doorRightValue);
		setRotationAngle(door, 0, 0, 0);
		door.render(matrices, vertices, light, OverlayTexture.DEFAULT_UV);

		door_left.setPivot(0, 0, doorLeftValue);
		door_right.setPivot(0, 0, -doorLeftValue);
		setRotationAngle(door, 0, (float) Math.PI, 0);
		door.render(matrices, vertices, light, OverlayTexture.DEFAULT_UV);

		roof.setPivot(0, 8, position);
		setRotationAngle(roof, 0, 0, 0);
		roof.render(matrices, vertices, light, OverlayTexture.DEFAULT_UV);
		setRotationAngle(roof, 0, (float) Math.PI, 0);
		roof.render(matrices, vertices, light, OverlayTexture.DEFAULT_UV);

		if (position == getDoorPositions()[1] || position == getDoorPositions()[3]) {
			bb_main.setPivot(0, 8, position);
			bb_main.render(matrices, vertices, light, OverlayTexture.DEFAULT_UV);
		} else {
			tv_pole.setPivot(0, 8, position);
			tv_pole.render(matrices, vertices, light, OverlayTexture.DEFAULT_UV);
		}
	}

	@Override
	protected void renderHeadPosition1(MatrixStack matrices, VertexConsumer vertices, int light, int position) {
		head.setPivot(0, 8, position);
		setRotationAngle(head, 0, (float) Math.PI, 0);
		head.render(matrices, vertices, light, OverlayTexture.DEFAULT_UV);

		roof_end.setPivot(0, 8, position);
		setRotationAngle(roof_end, 0, 0, 0);
		roof_end.render(matrices, vertices, light, OverlayTexture.DEFAULT_UV);

		top_handrail_head.setPivot(0, 8, position);
		setRotationAngle(top_handrail_head, 0, 0, 0);
		top_handrail_head.render(matrices, vertices, light, OverlayTexture.DEFAULT_UV);
		setRotationAngle(top_handrail_head, 0, (float) Math.PI, 0);
		top_handrail_head.render(matrices, vertices, light, OverlayTexture.DEFAULT_UV);
	}

	@Override
	protected void renderHeadPosition2(MatrixStack matrices, VertexConsumer vertices, int light, int position) {
		head.setPivot(0, 8, position);
		setRotationAngle(head, 0, 0, 0);
		head.render(matrices, vertices, light, OverlayTexture.DEFAULT_UV);

		roof_end.setPivot(0, 8, position);
		setRotationAngle(roof_end, 0, (float) Math.PI, 0);
		roof_end.render(matrices, vertices, light, OverlayTexture.DEFAULT_UV);

		top_handrail_head.setPivot(0, 8, position);
		setRotationAngle(top_handrail_head, 0, 0, 0);
		top_handrail_head.render(matrices, vertices, light, OverlayTexture.DEFAULT_UV);
		setRotationAngle(top_handrail_head, 0, (float) Math.PI, 0);
		top_handrail_head.render(matrices, vertices, light, OverlayTexture.DEFAULT_UV);
	}

	@Override
	protected void renderEndPosition1(MatrixStack matrices, VertexConsumer vertices, int light, int position) {
		end.setPivot(0, 8, position);
		setRotationAngle(end, 0, 0, 0);
		end.render(matrices, vertices, light, OverlayTexture.DEFAULT_UV);

		roof_end.setPivot(0, 8, position);
		setRotationAngle(roof_end, 0, 0, 0);
		roof_end.render(matrices, vertices, light, OverlayTexture.DEFAULT_UV);

		top_handrail.setPivot(0, 8, position);
		setRotationAngle(top_handrail, 0, 0, 0);
		top_handrail.render(matrices, vertices, light, OverlayTexture.DEFAULT_UV);
		setRotationAngle(top_handrail, 0, (float) Math.PI, 0);
		top_handrail.render(matrices, vertices, light, OverlayTexture.DEFAULT_UV);
	}

	@Override
	protected void renderEndPosition2(MatrixStack matrices, VertexConsumer vertices, int light, int position) {
		end.setPivot(0, 8, position);
		setRotationAngle(end, 0, (float) Math.PI, 0);
		end.render(matrices, vertices, light, OverlayTexture.DEFAULT_UV);

		roof_end.setPivot(0, 8, position);
		setRotationAngle(roof_end, 0, (float) Math.PI, 0);
		roof_end.render(matrices, vertices, light, OverlayTexture.DEFAULT_UV);

		top_handrail.setPivot(0, 8, position);
		setRotationAngle(top_handrail, 0, 0, 0);
		top_handrail.render(matrices, vertices, light, OverlayTexture.DEFAULT_UV);
		setRotationAngle(top_handrail, 0, (float) Math.PI, 0);
		top_handrail.render(matrices, vertices, light, OverlayTexture.DEFAULT_UV);
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
}