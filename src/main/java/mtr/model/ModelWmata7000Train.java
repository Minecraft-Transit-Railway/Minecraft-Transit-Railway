	package mtr.model;

import net.minecraft.client.model.ModelPart;
	import net.minecraft.client.render.VertexConsumer;
	import net.minecraft.client.util.math.MatrixStack;

	public class ModelWmata7000Train extends ModelTrainBase {

private final ModelPart window;
	private final ModelPart walls_window;
	private final ModelPart wall_right_2_r1;
	private final ModelPart window_bottom_side;
	private final ModelPart bottom_wall_2_left_r1;
	private final ModelPart window_bottom_side2;
	private final ModelPart bottom_wall_2_left_r2;
	private final ModelPart window_exterior;
	private final ModelPart wall_left_1_r1;
	private final ModelPart bottom_wall_left_r1;
	private final ModelPart door;
	private final ModelPart door_top_2_r1;
	private final ModelPart door_right;
	private final ModelPart door_right_top_r1;
	private final ModelPart door_left;
	private final ModelPart door_left_top_r1;
	private final ModelPart door_exterior;
	private final ModelPart bottom_wall_right_r1;
	private final ModelPart door_right_exterior;
	private final ModelPart door_right_top_r2;
	private final ModelPart door_left_exterior;
	private final ModelPart door_left_top_r2;
	private final ModelPart door_sides;
	private final ModelPart door_side_top_1_r1;
	private final ModelPart end;
	private final ModelPart wall_right_2_r2;
	private final ModelPart wall_left_1_r2;
	private final ModelPart interor_back;
	private final ModelPart cab_door_back2;
	private final ModelPart wall_end_back_side_2_r1;
	private final ModelPart wall_end_back_side_1_r1;
	private final ModelPart wall_end_top_r1;
	private final ModelPart end_bottom;
	private final ModelPart bottom_wall_2_right_r1;
	private final ModelPart bottom_wall_2_left_r3;
	private final ModelPart end_exterior;
	private final ModelPart end_front_in_2_r1;
	private final ModelPart end_front_in_1_r1;
	private final ModelPart end_front_3_r1;
	private final ModelPart bottom_wall_right_r2;
	private final ModelPart bottom_wall_left_r2;
	private final ModelPart buttom_panel_right_6_r1;
	private final ModelPart buttom_panel_right_5_r1;
	private final ModelPart buttom_panel_6_r1;
	private final ModelPart buttom_panel_5_r1;
	private final ModelPart wall_left_1_r3;
	private final ModelPart wall_right_2_r3;
	private final ModelPart end_seat_handrail;
	private final ModelPart handrail_end_right;
	private final ModelPart cube_r1;
	private final ModelPart cube_r2;
	private final ModelPart cube_r3;
	private final ModelPart handrail_end_right2;
	private final ModelPart cube_r4;
	private final ModelPart handrail_end_right3;
	private final ModelPart cube_r5;
	private final ModelPart cube_r6;
	private final ModelPart cube_r7;
	private final ModelPart cube_r8;
	private final ModelPart handrail_end_left;
	private final ModelPart cube_r9;
	private final ModelPart cube_r10;
	private final ModelPart cube_r11;
	private final ModelPart handrail_end_left2;
	private final ModelPart cube_r12;
	private final ModelPart handrail_end_left3;
	private final ModelPart cube_r13;
	private final ModelPart cube_r14;
	private final ModelPart cube_r15;
	private final ModelPart cube_r16;
	private final ModelPart window_seat_handrail;
	private final ModelPart top_handrails;
	private final ModelPart handrail_window_left7;
	private final ModelPart cube_r17;
	private final ModelPart cube_r18;
	private final ModelPart cube_r19;
	private final ModelPart cube_r20;
	private final ModelPart handrail_window_left6;
	private final ModelPart cube_r21;
	private final ModelPart cube_r22;
	private final ModelPart cube_r23;
	private final ModelPart cube_r24;
	private final ModelPart cube_r25;
	private final ModelPart handrail_window_left5;
	private final ModelPart cube_r26;
	private final ModelPart cube_r27;
	private final ModelPart handrail_window_left4;
	private final ModelPart cube_r28;
	private final ModelPart cube_r29;
	private final ModelPart handrail_window_left3;
	private final ModelPart cube_r30;
	private final ModelPart cube_r31;
	private final ModelPart handrail_window_left2;
	private final ModelPart cube_r32;
	private final ModelPart cube_r33;
	private final ModelPart cube_r34;
	private final ModelPart cube_r35;
	private final ModelPart cube_r36;
	private final ModelPart handrail_window_left1;
	private final ModelPart cube_r37;
	private final ModelPart cube_r38;
	private final ModelPart cube_r39;
	private final ModelPart cube_r40;
	private final ModelPart handrail_window_right5;
	private final ModelPart cube_r41;
	private final ModelPart cube_r42;
	private final ModelPart handrail_window_right4;
	private final ModelPart cube_r43;
	private final ModelPart cube_r44;
	private final ModelPart handrail_window_right3;
	private final ModelPart cube_r45;
	private final ModelPart cube_r46;
	private final ModelPart handrail_window_right2;
	private final ModelPart cube_r47;
	private final ModelPart cube_r48;
	private final ModelPart cube_r49;
	private final ModelPart cube_r50;
	private final ModelPart cube_r51;
	private final ModelPart handrail_window_right1;
	private final ModelPart cube_r52;
	private final ModelPart cube_r53;
	private final ModelPart cube_r54;
	private final ModelPart cube_r55;
	private final ModelPart wall_replace;
	private final ModelPart wall_right_2_r4;
	private final ModelPart blue_floor;
	private final ModelPart wall_replace2;
	private final ModelPart wall_left_r1;
	private final ModelPart front_seat_handrail;
	private final ModelPart handrail_head_left;
	private final ModelPart cube_r56;
	private final ModelPart cube_r57;
	private final ModelPart cube_r58;
	private final ModelPart handrail_head_right;
	private final ModelPart cube_r59;
	private final ModelPart cube_r60;
	private final ModelPart cube_r61;
	private final ModelPart roof_head;
	private final ModelPart roof_window;
	private final ModelPart roof_end;
	private final ModelPart roof_door;
	private final ModelPart head;
	private final ModelPart wall_right_2_r5;
	private final ModelPart wall_left_1_r4;
	private final ModelPart front_top_r1;
	private final ModelPart head_bottom;
	private final ModelPart front_bottom_3_r1;
	private final ModelPart front_bottom_2_r1;
	private final ModelPart interor_cab_wall;
	private final ModelPart cab_door_back;
	private final ModelPart front_inside_2_r1;
	private final ModelPart front_inside_1_r1;
	private final ModelPart head_exterior;
	private final ModelPart buttom_panel_6_r2;
	private final ModelPart buttom_panel_5_r2;
	private final ModelPart floor_5_r1;
	private final ModelPart floor_4_r1;
	private final ModelPart head_bottom_out;
	private final ModelPart buttom_panel_right_6_r2;
	private final ModelPart buttom_panel_right_5_r2;
	private final ModelPart walls;
	private final ModelPart bottom_wall_right_r3;
	private final ModelPart bottom_wall_left_r3;
	private final ModelPart wall_right_2_r6;
	private final ModelPart wall_left_1_r5;
	private final ModelPart exteror_cab_front;
	private final ModelPart front_in_1_r1;
	private final ModelPart cab_door;
	private final ModelPart front_in_1_r2;
	private final ModelPart front_in_2_r1;
	private final ModelPart front_train_barrier;
	private final ModelPart roof_window_light;
	private final ModelPart roof_door_light;
	private final ModelPart roof_head_light;
	private final ModelPart roof_end_light;
	private final ModelPart tail_lights;
	private final ModelPart headlights;
	private final ModelPart headlights_end;
	private final ModelPart tail_lights_end;
	private final ModelPart door_light;
	private final ModelPart door_light_on;
	private final ModelPart door_light_off;
	private final ModelPart roof_window_exterior;
	private final ModelPart top_roof_r1;
	private final ModelPart top_roof_r2;
	private final ModelPart top_slant_right_r1;
	private final ModelPart top_roof_r3;
	private final ModelPart top_roof_r4;
	private final ModelPart top_slant_left_r1;
	private final ModelPart roof_head_exterior;
	private final ModelPart top_roof_r5;
	private final ModelPart top_roof_r6;
	private final ModelPart top_slant_right_r2;
	private final ModelPart top_roof_r7;
	private final ModelPart top_roof_r8;
	private final ModelPart top_slant_left_r2;
	private final ModelPart roof_end_exterior;
	private final ModelPart top_roof_r9;
	private final ModelPart top_roof_r10;
	private final ModelPart top_slant_right_r3;
	private final ModelPart top_roof_r11;
	private final ModelPart top_roof_r12;
	private final ModelPart top_slant_left_r3;
	private final ModelPart roof_door_exterior;
	private final ModelPart top_roof_r13;
	private final ModelPart top_roof_r14;
	private final ModelPart top_slant_right_r4;
	private final ModelPart top_roof_r15;
	private final ModelPart top_roof_r16;
	private final ModelPart top_slant_left_r4;
	private final ModelPart end_side_panels;
	private final ModelPart window_side_panels;
	private final ModelPart head_side_panels;
	private final ModelPart seat;
	private final ModelPart seat_structure;
	private final ModelPart cube_r62;
	private final ModelPart cube_r63;
	private final ModelPart cube_r64;
	private final ModelPart cube_r65;
	private final ModelPart cube_r66;
	private final ModelPart seat_handrails;
	private final ModelPart cube_r67;
	private final ModelPart cube_r68;
	private final ModelPart cube_r69;
	private final ModelPart seat_2;
	private final ModelPart cube_r70;
	private final ModelPart cube_r71;
	private final ModelPart cube_r72;
	private final ModelPart cube_r73;
	private final ModelPart cube_r74;
	private final ModelPart cube_r75;
	private final ModelPart cube_r76;
	private final ModelPart seat_3;
	private final ModelPart cube_r77;
	private final ModelPart cube_r78;
	private final ModelPart cube_r79;
	private final ModelPart cube_r80;
	private final ModelPart cube_r81;
	private final ModelPart cube_r82;
	private final ModelPart cube_r83;
	private final ModelPart path_map;
	private final ModelPart map_path_r1;
public ModelWmata7000Train() {
		textureWidth = 544;
		textureHeight = 544;
		window = new ModelPart(this);
		window.setPivot(0.0F, 24.0F, 0.0F);
		

		walls_window = new ModelPart(this);
		walls_window.setPivot(0.0F, -1.0F, -1.0F);
		window.addChild(walls_window);
		walls_window.setTextureOffset(326, 449).addCuboid(-21.0F, 1.0F, -39.0F, 21.0F, 1.0F, 80.0F, 0.0F, false);
		walls_window.setTextureOffset(154, 29).addCuboid(-22.15F, -9.9F, -44.0F, 2.0F, 11.0F, 90.0F, 0.0F, false);

		wall_right_2_r1 = new ModelPart(this);
		wall_right_2_r1.setPivot(3.2878F, -11.5941F, -8.0F);
		walls_window.addChild(wall_right_2_r1);
		setRotationAngle(wall_right_2_r1, 0.0F, 0.0F, 0.1309F);
		wall_right_2_r1.setTextureOffset(0, 119).addCuboid(-24.9978F, -24.0059F, -36.0F, 2.0F, 29.0F, 90.0F, 0.0F, false);

		window_bottom_side = new ModelPart(this);
		window_bottom_side.setPivot(0.0F, 0.0F, 43.0F);
		window.addChild(window_bottom_side);
		window_bottom_side.setTextureOffset(94, 188).addCuboid(-21.0F, -4.0F, -12.0F, 3.0F, 4.0F, 14.0F, 0.0F, false);

		bottom_wall_2_left_r1 = new ModelPart(this);
		bottom_wall_2_left_r1.setPivot(-19.75F, -4.25F, -4.5F);
		window_bottom_side.addChild(bottom_wall_2_left_r1);
		setRotationAngle(bottom_wall_2_left_r1, 0.0F, 0.0F, 0.6981F);
		bottom_wall_2_left_r1.setTextureOffset(188, 190).addCuboid(-1.5F, -1.0F, -7.5F, 3.0F, 2.0F, 14.0F, 0.0F, false);

		window_bottom_side2 = new ModelPart(this);
		window_bottom_side2.setPivot(0.0F, 0.0F, -27.0F);
		window.addChild(window_bottom_side2);
		window_bottom_side2.setTextureOffset(94, 188).addCuboid(-21.0F, -4.0F, -18.0F, 3.0F, 4.0F, 14.0F, 0.0F, false);

		bottom_wall_2_left_r2 = new ModelPart(this);
		bottom_wall_2_left_r2.setPivot(-19.75F, -4.25F, -11.5F);
		window_bottom_side2.addChild(bottom_wall_2_left_r2);
		setRotationAngle(bottom_wall_2_left_r2, 0.0F, 0.0F, 0.6981F);
		bottom_wall_2_left_r2.setTextureOffset(188, 190).addCuboid(-1.5F, -1.0F, -6.5F, 3.0F, 2.0F, 14.0F, 0.0F, false);

		window_exterior = new ModelPart(this);
		window_exterior.setPivot(0.0F, 24.0F, 0.0F);
		window_exterior.setTextureOffset(256, 211).addCuboid(-21.75F, 0.0F, -40.0F, 1.0F, 3.0F, 80.0F, 0.0F, false);
		window_exterior.setTextureOffset(94, 148).addCuboid(-22.151F, -10.9F, -45.0F, 2.0F, 11.0F, 90.0F, 0.0F, false);

		wall_left_1_r1 = new ModelPart(this);
		wall_left_1_r1.setPivot(3.2878F, -12.5941F, -1.0F);
		window_exterior.addChild(wall_left_1_r1);
		setRotationAngle(wall_left_1_r1, 0.0F, 0.0F, 0.1309F);
		wall_left_1_r1.setTextureOffset(60, 0).addCuboid(-25.0F, -24.0F, -44.0F, 2.0F, 29.0F, 90.0F, 0.0F, false);

		bottom_wall_left_r1 = new ModelPart(this);
		bottom_wall_left_r1.setPivot(-0.4068F, 4.1977F, -12.0F);
		window_exterior.addChild(bottom_wall_left_r1);
		setRotationAngle(bottom_wall_left_r1, -0.1309F, -1.5708F, 0.0F);
		bottom_wall_left_r1.setTextureOffset(154, 0).addCuboid(-27.9932F, -6.9977F, 21.0F, 80.0F, 3.0F, 0.0F, 0.0F, false);

		door = new ModelPart(this);
		door.setPivot(0.0F, 24.0F, 0.0F);
		door.setTextureOffset(9, 3).addCuboid(-17.875F, -36.0F, -11.5F, 1.0F, 0.0F, 23.0F, 0.0F, false);
		door.setTextureOffset(244, 478).addCuboid(-21.5F, 0.0F, -16.0F, 22.0F, 1.0F, 32.0F, 0.0F, false);

		door_top_2_r1 = new ModelPart(this);
		door_top_2_r1.setPivot(-19.4875F, -16.125F, 0.25F);
		door.addChild(door_top_2_r1);
		setRotationAngle(door_top_2_r1, 0.0F, 0.0F, 0.1309F);
		door_top_2_r1.setTextureOffset(0, 22).addCuboid(0.0F, -24.0F, -11.5F, 0.0F, 4.0F, 23.0F, 0.0F, false);

		door_right = new ModelPart(this);
		door_right.setPivot(0.0F, 0.0F, 0.0F);
		door.addChild(door_right);
		door_right.setTextureOffset(122, 107).addCuboid(-21.0F, -11.0F, 0.0F, 0.0F, 11.0F, 12.0F, 0.0F, false);

		door_right_top_r1 = new ModelPart(this);
		door_right_top_r1.setPivot(0.25F, 0.0F, 0.0F);
		door_right.addChild(door_right_top_r1);
		setRotationAngle(door_right_top_r1, 0.0F, 0.0F, 0.1309F);
		door_right_top_r1.setTextureOffset(54, 16).addCuboid(-22.5F, -34.13F, 0.0F, 0.0F, 26.0F, 12.0F, 0.0F, false);

		door_left = new ModelPart(this);
		door_left.setPivot(0.0F, 0.0F, 0.0F);
		door.addChild(door_left);
		door_left.setTextureOffset(28, 41).addCuboid(-21.0F, -11.0F, -12.0F, 0.0F, 11.0F, 12.0F, 0.0F, false);

		door_left_top_r1 = new ModelPart(this);
		door_left_top_r1.setPivot(0.25F, 0.0F, 0.0F);
		door_left.addChild(door_left_top_r1);
		setRotationAngle(door_left_top_r1, 0.0F, 0.0F, 0.1309F);
		door_left_top_r1.setTextureOffset(54, 42).addCuboid(-22.5F, -34.13F, -12.0F, 0.0F, 26.0F, 12.0F, 0.0F, false);

		door_exterior = new ModelPart(this);
		door_exterior.setPivot(0.0F, 24.0F, 0.0F);
		

		bottom_wall_right_r1 = new ModelPart(this);
		bottom_wall_right_r1.setPivot(-0.4068F, 4.1977F, -13.0F);
		door_exterior.addChild(bottom_wall_right_r1);
		setRotationAngle(bottom_wall_right_r1, -0.1309F, -1.5708F, 0.0F);
		bottom_wall_right_r1.setTextureOffset(94, 185).addCuboid(-3.0F, -7.0F, 21.0F, 32.0F, 3.0F, 0.0F, 0.0F, false);

		door_right_exterior = new ModelPart(this);
		door_right_exterior.setPivot(0.0F, 0.0F, 0.0F);
		door_exterior.addChild(door_right_exterior);
		door_right_exterior.setTextureOffset(94, 147).addCuboid(-21.25F, -11.0F, 0.0F, 0.0F, 11.0F, 12.0F, 0.0F, false);

		door_right_top_r2 = new ModelPart(this);
		door_right_top_r2.setPivot(0.25F, 0.0F, 0.0F);
		door_right_exterior.addChild(door_right_top_r2);
		setRotationAngle(door_right_top_r2, 0.0F, 0.0F, 0.1309F);
		door_right_top_r2.setTextureOffset(64, 107).addCuboid(-22.754F, -34.098F, 0.0F, 0.0F, 26.0F, 12.0F, 0.0F, false);

		door_left_exterior = new ModelPart(this);
		door_left_exterior.setPivot(0.0F, 0.0F, 0.0F);
		door_exterior.addChild(door_left_exterior);
		door_left_exterior.setTextureOffset(94, 136).addCuboid(-21.2601F, -11.0171F, -12.0F, 0.0F, 11.0F, 12.0F, 0.0F, false);

		door_left_top_r2 = new ModelPart(this);
		door_left_top_r2.setPivot(0.25F, 0.0F, 0.0F);
		door_left_exterior.addChild(door_left_top_r2);
		setRotationAngle(door_left_top_r2, 0.0F, 0.0F, 0.1309F);
		door_left_top_r2.setTextureOffset(28, 77).addCuboid(-22.765F, -34.1151F, -12.0F, 0.0F, 26.0F, 12.0F, 0.0F, false);

		door_sides = new ModelPart(this);
		door_sides.setPivot(0.0F, 0.0F, 0.0F);
		door_exterior.addChild(door_sides);
		door_sides.setTextureOffset(154, 9).addCuboid(-21.75F, 1.0F, -16.0F, 1.0F, 2.0F, 32.0F, 0.0F, false);
		door_sides.setTextureOffset(14, 3).addCuboid(-22.25F, 0.0F, -11.0F, 1.0F, 0.0F, 22.0F, 0.0F, false);
		door_sides.setTextureOffset(14, 3).addCuboid(-22.75F, 0.0F, -11.0F, 1.0F, 0.0F, 22.0F, 0.0F, false);
		door_sides.setTextureOffset(11, 3).addCuboid(-18.875F, -36.0F, -11.5F, 1.0F, 0.0F, 23.0F, 0.0F, false);

		door_side_top_1_r1 = new ModelPart(this);
		door_side_top_1_r1.setPivot(-21.425F, -16.125F, 0.25F);
		door_sides.addChild(door_side_top_1_r1);
		setRotationAngle(door_side_top_1_r1, 0.0F, 0.0F, 0.1309F);
		door_side_top_1_r1.setTextureOffset(0, 26).addCuboid(0.0F, -24.0F, -11.5F, 0.0F, 4.0F, 23.0F, 0.0F, false);

		end = new ModelPart(this);
		end.setPivot(0.0F, 24.0F, 0.0F);
		end.setTextureOffset(272, 130).addCuboid(-20.5F, 0.0F, -42.0F, 41.0F, 1.0F, 50.0F, 0.0F, false);
		end.setTextureOffset(61, 409).addCuboid(-22.15F, -10.9F, -44.0F, 2.0F, 11.0F, 57.0F, 0.0F, false);
		end.setTextureOffset(399, 210).addCuboid(20.15F, -10.9F, -44.0F, 2.0F, 11.0F, 57.0F, 0.0F, false);

		wall_right_2_r2 = new ModelPart(this);
		wall_right_2_r2.setPivot(-3.2878F, -12.5941F, 1.0F);
		end.addChild(wall_right_2_r2);
		setRotationAngle(wall_right_2_r2, 0.0F, 0.0F, -0.1309F);
		wall_right_2_r2.setTextureOffset(338, 181).addCuboid(22.9978F, -24.0059F, -45.0F, 2.0F, 29.0F, 57.0F, 0.0F, false);

		wall_left_1_r2 = new ModelPart(this);
		wall_left_1_r2.setPivot(3.2878F, -12.5941F, 1.0F);
		end.addChild(wall_left_1_r2);
		setRotationAngle(wall_left_1_r2, 0.0F, 0.0F, 0.1309F);
		wall_left_1_r2.setTextureOffset(122, 352).addCuboid(-24.9978F, -24.0059F, -45.0F, 2.0F, 29.0F, 57.0F, 0.0F, false);

		interor_back = new ModelPart(this);
		interor_back.setPivot(0.0F, 0.0F, 0.0F);
		end.addChild(interor_back);
		

		cab_door_back2 = new ModelPart(this);
		cab_door_back2.setPivot(0.0F, 0.0F, -36.0F);
		interor_back.addChild(cab_door_back2);
		setRotationAngle(cab_door_back2, -3.1416F, 0.0F, 3.1416F);
		cab_door_back2.setTextureOffset(218, 46).addCuboid(-6.0F, -36.0F, 5.5F, 12.0F, 36.0F, 0.0F, 0.0F, false);
		cab_door_back2.setTextureOffset(248, 9).addCuboid(-22.0F, -36.0F, 4.0F, 16.0F, 36.0F, 0.0F, 0.0F, false);
		cab_door_back2.setTextureOffset(188, 9).addCuboid(-21.0F, -13.0F, -3.0F, 14.0F, 13.0F, 7.0F, 0.0F, false);
		cab_door_back2.setTextureOffset(0, 186).addCuboid(7.0F, -13.0F, -3.0F, 14.0F, 13.0F, 7.0F, 0.0F, false);
		cab_door_back2.setTextureOffset(220, 148).addCuboid(6.0F, -36.0F, 4.0F, 16.0F, 36.0F, 0.0F, 0.0F, false);
		cab_door_back2.setTextureOffset(0, 0).addCuboid(-18.0F, -39.0F, 4.0F, 36.0F, 3.0F, 0.0F, 0.0F, false);

		wall_end_back_side_2_r1 = new ModelPart(this);
		wall_end_back_side_2_r1.setPivot(1.0F, 0.0F, 0.0F);
		cab_door_back2.addChild(wall_end_back_side_2_r1);
		setRotationAngle(wall_end_back_side_2_r1, 0.0F, -1.5708F, 0.0F);
		wall_end_back_side_2_r1.setTextureOffset(84, 145).addCuboid(4.0F, -36.0F, -5.0F, 2.0F, 36.0F, 0.0F, 0.0F, false);

		wall_end_back_side_1_r1 = new ModelPart(this);
		wall_end_back_side_1_r1.setPivot(-1.0F, 0.0F, 0.0F);
		cab_door_back2.addChild(wall_end_back_side_1_r1);
		setRotationAngle(wall_end_back_side_1_r1, 0.0F, -1.5708F, 0.0F);
		wall_end_back_side_1_r1.setTextureOffset(262, 130).addCuboid(4.0F, -36.0F, 5.0F, 2.0F, 36.0F, 0.0F, 0.0F, false);

		wall_end_top_r1 = new ModelPart(this);
		wall_end_top_r1.setPivot(0.0F, -58.0F, 47.0F);
		cab_door_back2.addChild(wall_end_top_r1);
		setRotationAngle(wall_end_top_r1, 1.5708F, 0.0F, 0.0F);
		wall_end_top_r1.setTextureOffset(56, 3).addCuboid(-6.0F, -43.0F, -22.0F, 12.0F, 2.0F, 0.0F, 0.0F, false);

		end_bottom = new ModelPart(this);
		end_bottom.setPivot(0.0F, 0.0F, 20.0F);
		end.addChild(end_bottom);
		end_bottom.setTextureOffset(42, 189).addCuboid(18.0F, -4.0F, -21.0F, 3.0F, 4.0F, 14.0F, 0.0F, false);
		end_bottom.setTextureOffset(94, 188).addCuboid(-21.0F, -4.0F, -21.0F, 3.0F, 4.0F, 14.0F, 0.0F, false);

		bottom_wall_2_right_r1 = new ModelPart(this);
		bottom_wall_2_right_r1.setPivot(-19.75F, -4.25F, -13.5F);
		end_bottom.addChild(bottom_wall_2_right_r1);
		setRotationAngle(bottom_wall_2_right_r1, 0.0F, 0.0F, 0.6981F);
		bottom_wall_2_right_r1.setTextureOffset(188, 190).addCuboid(-1.5F, -1.0F, -7.5F, 3.0F, 2.0F, 14.0F, 0.0F, false);

		bottom_wall_2_left_r3 = new ModelPart(this);
		bottom_wall_2_left_r3.setPivot(19.75F, -4.25F, -13.5F);
		end_bottom.addChild(bottom_wall_2_left_r3);
		setRotationAngle(bottom_wall_2_left_r3, 0.0F, 0.0F, -0.6981F);
		bottom_wall_2_left_r3.setTextureOffset(114, 192).addCuboid(-1.5F, -1.0F, -7.5F, 3.0F, 2.0F, 14.0F, 0.0F, false);

		end_exterior = new ModelPart(this);
		end_exterior.setPivot(0.0F, 24.0F, 0.0F);
		end_exterior.setTextureOffset(359, 380).addCuboid(-22.15F, -10.9F, -44.0F, 2.0F, 11.0F, 57.0F, 0.0F, false);
		end_exterior.setTextureOffset(0, 366).addCuboid(20.15F, -10.9F, -44.0F, 2.0F, 11.0F, 57.0F, 0.0F, false);
		end_exterior.setTextureOffset(208, 190).addCuboid(-6.5F, 0.0F, -47.0F, 13.0F, 3.0F, 7.0F, 0.0F, false);
		end_exterior.setTextureOffset(0, 238).addCuboid(-21.5F, 0.0F, -44.0F, 7.0F, 3.0F, 6.0F, 0.0F, false);
		end_exterior.setTextureOffset(62, 189).addCuboid(14.5F, 0.0F, -44.0F, 7.0F, 3.0F, 6.0F, 0.0F, false);
		end_exterior.setTextureOffset(420, 278).addCuboid(20.75F, 0.0F, -38.0F, 1.0F, 3.0F, 46.0F, 0.0F, false);
		end_exterior.setTextureOffset(420, 380).addCuboid(-21.75F, 0.0F, -38.0F, 1.0F, 3.0F, 46.0F, 0.0F, false);
		end_exterior.setTextureOffset(186, 46).addCuboid(-22.0F, -42.0F, -44.0F, 16.0F, 42.0F, 0.0F, 0.0F, false);
		end_exterior.setTextureOffset(28, 64).addCuboid(-6.0F, -42.0F, -44.0F, 12.0F, 6.0F, 0.0F, 0.0F, false);
		end_exterior.setTextureOffset(0, 3).addCuboid(6.0F, -42.0F, -44.0F, 16.0F, 42.0F, 0.0F, 0.0F, false);
		end_exterior.setTextureOffset(278, 211).addCuboid(-6.0F, -36.0F, -42.0F, 12.0F, 36.0F, 0.0F, 0.0F, false);

		end_front_in_2_r1 = new ModelPart(this);
		end_front_in_2_r1.setPivot(1.0F, 0.0F, -21.0F);
		end_exterior.addChild(end_front_in_2_r1);
		setRotationAngle(end_front_in_2_r1, 0.0F, -1.5708F, 0.0F);
		end_front_in_2_r1.setTextureOffset(262, 166).addCuboid(-23.0F, -36.0F, -5.0F, 2.0F, 36.0F, 0.0F, 0.0F, false);

		end_front_in_1_r1 = new ModelPart(this);
		end_front_in_1_r1.setPivot(-1.0F, 0.0F, -21.0F);
		end_exterior.addChild(end_front_in_1_r1);
		setRotationAngle(end_front_in_1_r1, 0.0F, -1.5708F, 0.0F);
		end_front_in_1_r1.setTextureOffset(248, 276).addCuboid(-23.0F, -36.0F, 5.0F, 2.0F, 36.0F, 0.0F, 0.0F, false);

		end_front_3_r1 = new ModelPart(this);
		end_front_3_r1.setPivot(0.0F, -58.0F, -10.0F);
		end_exterior.addChild(end_front_3_r1);
		setRotationAngle(end_front_3_r1, 1.5708F, 0.0F, 0.0F);
		end_front_3_r1.setTextureOffset(28, 70).addCuboid(-6.0F, -34.0F, -22.0F, 12.0F, 2.0F, 0.0F, 0.0F, false);

		bottom_wall_right_r2 = new ModelPart(this);
		bottom_wall_right_r2.setPivot(0.4068F, 4.1977F, -14.0F);
		end_exterior.addChild(bottom_wall_right_r2);
		setRotationAngle(bottom_wall_right_r2, -0.1309F, 1.5708F, 0.0F);
		bottom_wall_right_r2.setTextureOffset(0, 83).addCuboid(-22.0068F, -7.0007F, 21.0F, 52.0F, 3.0F, 0.0F, 0.0F, false);

		bottom_wall_left_r2 = new ModelPart(this);
		bottom_wall_left_r2.setPivot(-0.4068F, 4.1977F, -14.0F);
		end_exterior.addChild(bottom_wall_left_r2);
		setRotationAngle(bottom_wall_left_r2, -0.1309F, -1.5708F, 0.0F);
		bottom_wall_left_r2.setTextureOffset(0, 86).addCuboid(-29.9932F, -7.0007F, 21.0F, 52.0F, 3.0F, 0.0F, 0.0F, false);

		buttom_panel_right_6_r1 = new ModelPart(this);
		buttom_panel_right_6_r1.setPivot(9.7131F, 1.5F, -44.8861F);
		end_exterior.addChild(buttom_panel_right_6_r1);
		setRotationAngle(buttom_panel_right_6_r1, 0.0F, -0.1745F, 0.0F);
		buttom_panel_right_6_r1.setTextureOffset(154, 25).addCuboid(-3.5031F, -1.5F, -0.4939F, 10.0F, 3.0F, 6.0F, 0.0F, false);

		buttom_panel_right_5_r1 = new ModelPart(this);
		buttom_panel_right_5_r1.setPivot(-9.7131F, 1.5F, -44.8861F);
		end_exterior.addChild(buttom_panel_right_5_r1);
		setRotationAngle(buttom_panel_right_5_r1, 0.0F, 0.1745F, 0.0F);
		buttom_panel_right_5_r1.setTextureOffset(94, 170).addCuboid(-6.4969F, -1.5F, -0.4939F, 10.0F, 3.0F, 6.0F, 0.0F, false);

		buttom_panel_6_r1 = new ModelPart(this);
		buttom_panel_6_r1.setPivot(-21.5625F, 4.1F, -37.0F);
		end_exterior.addChild(buttom_panel_6_r1);
		setRotationAngle(buttom_panel_6_r1, 0.0F, 0.0F, -0.1309F);
		buttom_panel_6_r1.setTextureOffset(0, 61).addCuboid(0.0025F, -4.1F, -7.0F, 1.0F, 3.0F, 6.0F, 0.0F, false);

		buttom_panel_5_r1 = new ModelPart(this);
		buttom_panel_5_r1.setPivot(21.5625F, 4.1F, -37.0F);
		end_exterior.addChild(buttom_panel_5_r1);
		setRotationAngle(buttom_panel_5_r1, 0.0F, 0.0F, 0.1309F);
		buttom_panel_5_r1.setTextureOffset(46, 109).addCuboid(-1.0025F, -4.1F, -7.0F, 1.0F, 3.0F, 6.0F, 0.0F, false);

		wall_left_1_r3 = new ModelPart(this);
		wall_left_1_r3.setPivot(3.2878F, -12.5941F, 1.0F);
		end_exterior.addChild(wall_left_1_r3);
		setRotationAngle(wall_left_1_r3, 0.0F, 0.0F, 0.1309F);
		wall_left_1_r3.setTextureOffset(0, 280).addCuboid(-24.9978F, -24.0059F, -45.0F, 2.0F, 29.0F, 57.0F, 0.0F, false);

		wall_right_2_r3 = new ModelPart(this);
		wall_right_2_r3.setPivot(-3.2878F, -12.5941F, 1.0F);
		end_exterior.addChild(wall_right_2_r3);
		setRotationAngle(wall_right_2_r3, 0.0F, 0.0F, -0.1309F);
		wall_right_2_r3.setTextureOffset(237, 294).addCuboid(22.9978F, -24.0059F, -45.0F, 2.0F, 29.0F, 57.0F, 0.0F, false);

		end_seat_handrail = new ModelPart(this);
		end_seat_handrail.setPivot(0.0F, 24.0F, 0.0F);
		

		handrail_end_right = new ModelPart(this);
		handrail_end_right.setPivot(0.0F, 0.0F, 4.0F);
		end_seat_handrail.addChild(handrail_end_right);
		handrail_end_right.setTextureOffset(107, 298).addCuboid(-11.0F, -39.06F, 8.5F, 0.0F, 32.0F, 0.0F, 0.2F, false);

		cube_r1 = new ModelPart(this);
		cube_r1.setPivot(-10.6875F, -6.95F, 8.55F);
		handrail_end_right.addChild(cube_r1);
		setRotationAngle(cube_r1, -0.8598F, -0.6048F, -3.0693F);
		cube_r1.setTextureOffset(107, 298).addCuboid(0.2075F, 0.2F, -1.2F, 0.0F, 0.0F, 1.0F, 0.2F, false);

		cube_r2 = new ModelPart(this);
		cube_r2.setPivot(-12.375F, -7.3125F, 2.8125F);
		handrail_end_right.addChild(cube_r2);
		setRotationAngle(cube_r2, -1.399F, -1.1819F, 1.3833F);
		cube_r2.setTextureOffset(107, 298).addCuboid(1.005F, 0.0025F, -0.0025F, 2.0F, 0.0F, 0.0F, 0.2F, false);

		cube_r3 = new ModelPart(this);
		cube_r3.setPivot(-12.75F, -6.125F, 3.9375F);
		handrail_end_right.addChild(cube_r3);
		setRotationAngle(cube_r3, 0.0F, -1.2654F, 0.0F);
		cube_r3.setTextureOffset(107, 298).addCuboid(2.0F, 0.0F, -0.0075F, 2.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_end_right2 = new ModelPart(this);
		handrail_end_right2.setPivot(3.0F, 0.0F, -23.0F);
		end_seat_handrail.addChild(handrail_end_right2);
		handrail_end_right2.setTextureOffset(107, 298).addCuboid(-10.5F, -39.31F, 6.75F, 0.0F, 22.0F, 0.0F, 0.2F, false);

		cube_r4 = new ModelPart(this);
		cube_r4.setPivot(-10.25F, -17.1375F, 6.55F);
		handrail_end_right2.addChild(cube_r4);
		setRotationAngle(cube_r4, 0.0F, -1.5708F, 2.4871F);
		cube_r4.setTextureOffset(107, 298).addCuboid(0.2F, 0.2075F, -1.2F, 0.0F, 0.0F, 1.0F, 0.2F, false);

		handrail_end_right3 = new ModelPart(this);
		handrail_end_right3.setPivot(0.0F, 0.0F, 60.0F);
		end_seat_handrail.addChild(handrail_end_right3);
		handrail_end_right3.setTextureOffset(107, 298).addCuboid(-11.0F, -39.06F, -60.5F, 0.0F, 32.0F, 0.0F, 0.2F, false);
		handrail_end_right3.setTextureOffset(107, 298).addCuboid(-18.93F, -13.6F, -60.5F, 0.0F, 0.0F, 1.0F, 0.2F, false);
		handrail_end_right3.setTextureOffset(107, 298).addCuboid(-12.5F, -9.31F, -60.5F, 0.0F, 1.0F, 0.0F, 0.2F, false);
		handrail_end_right3.setTextureOffset(107, 298).addCuboid(-16.5F, -8.06F, -60.5F, 4.0F, 0.0F, 0.0F, 0.2F, false);
		handrail_end_right3.setTextureOffset(107, 298).addCuboid(-16.5F, -12.06F, -60.5F, 0.0F, 4.0F, 0.0F, 0.2F, false);

		cube_r5 = new ModelPart(this);
		cube_r5.setPivot(-12.5F, -9.5625F, -60.5F);
		handrail_end_right3.addChild(cube_r5);
		setRotationAngle(cube_r5, 0.0F, 0.0F, -1.0036F);
		cube_r5.setTextureOffset(107, 298).addCuboid(0.0F, -7.4975F, 0.0F, 0.0F, 9.0F, 0.0F, 0.2F, false);

		cube_r6 = new ModelPart(this);
		cube_r6.setPivot(-10.6875F, -6.95F, -60.55F);
		handrail_end_right3.addChild(cube_r6);
		setRotationAngle(cube_r6, 0.8598F, 0.6048F, -3.0693F);
		cube_r6.setTextureOffset(107, 298).addCuboid(0.2075F, 0.2F, 0.2F, 0.0F, 0.0F, 1.0F, 0.2F, false);

		cube_r7 = new ModelPart(this);
		cube_r7.setPivot(-12.375F, -7.3125F, -54.8125F);
		handrail_end_right3.addChild(cube_r7);
		setRotationAngle(cube_r7, 1.399F, 1.1819F, 1.3833F);
		cube_r7.setTextureOffset(107, 298).addCuboid(1.0F, 0.0025F, -0.0075F, 2.0F, 0.0F, 0.0F, 0.2F, false);

		cube_r8 = new ModelPart(this);
		cube_r8.setPivot(-12.75F, -6.125F, -55.9375F);
		handrail_end_right3.addChild(cube_r8);
		setRotationAngle(cube_r8, 0.0F, 1.2654F, 0.0F);
		cube_r8.setTextureOffset(107, 298).addCuboid(2.0F, 0.0F, -0.0025F, 2.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_end_left = new ModelPart(this);
		handrail_end_left.setPivot(0.0F, 0.0F, 4.0F);
		end_seat_handrail.addChild(handrail_end_left);
		handrail_end_left.setTextureOffset(107, 298).addCuboid(11.0F, -39.06F, 8.5F, 0.0F, 32.0F, 0.0F, 0.2F, false);

		cube_r9 = new ModelPart(this);
		cube_r9.setPivot(10.6875F, -6.95F, 8.55F);
		handrail_end_left.addChild(cube_r9);
		setRotationAngle(cube_r9, -0.8598F, 0.6048F, 3.0693F);
		cube_r9.setTextureOffset(107, 298).addCuboid(-0.2075F, 0.2F, -1.2F, 0.0F, 0.0F, 1.0F, 0.2F, false);

		cube_r10 = new ModelPart(this);
		cube_r10.setPivot(12.375F, -7.3125F, 2.8125F);
		handrail_end_left.addChild(cube_r10);
		setRotationAngle(cube_r10, -1.399F, 1.1819F, -1.3833F);
		cube_r10.setTextureOffset(107, 298).addCuboid(-3.075F, 0.0025F, -0.0025F, 2.0F, 0.0F, 0.0F, 0.2F, false);

		cube_r11 = new ModelPart(this);
		cube_r11.setPivot(12.75F, -6.125F, 3.9375F);
		handrail_end_left.addChild(cube_r11);
		setRotationAngle(cube_r11, 0.0F, 1.2654F, 0.0F);
		cube_r11.setTextureOffset(107, 298).addCuboid(-4.0F, 0.025F, -0.0075F, 2.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_end_left2 = new ModelPart(this);
		handrail_end_left2.setPivot(-3.0F, 0.0F, -23.0F);
		end_seat_handrail.addChild(handrail_end_left2);
		handrail_end_left2.setTextureOffset(107, 298).addCuboid(11.0F, -39.31F, 6.75F, 0.0F, 22.0F, 0.0F, 0.2F, false);

		cube_r12 = new ModelPart(this);
		cube_r12.setPivot(10.75F, -17.1375F, 6.55F);
		handrail_end_left2.addChild(cube_r12);
		setRotationAngle(cube_r12, 0.0F, 1.5708F, -2.4871F);
		cube_r12.setTextureOffset(107, 298).addCuboid(-0.2F, 0.2375F, -1.2F, 0.0F, 0.0F, 1.0F, 0.2F, false);

		handrail_end_left3 = new ModelPart(this);
		handrail_end_left3.setPivot(0.0F, 0.0F, 60.0F);
		end_seat_handrail.addChild(handrail_end_left3);
		handrail_end_left3.setTextureOffset(107, 298).addCuboid(11.0F, -39.06F, -60.5F, 0.0F, 32.0F, 0.0F, 0.2F, false);
		handrail_end_left3.setTextureOffset(107, 298).addCuboid(18.93F, -13.6F, -60.5F, 0.0F, 0.0F, 1.0F, 0.2F, false);
		handrail_end_left3.setTextureOffset(107, 298).addCuboid(12.5F, -9.31F, -60.5F, 0.0F, 1.0F, 0.0F, 0.2F, false);
		handrail_end_left3.setTextureOffset(107, 298).addCuboid(12.5F, -8.06F, -60.5F, 4.0F, 0.0F, 0.0F, 0.2F, false);
		handrail_end_left3.setTextureOffset(107, 298).addCuboid(16.5F, -12.06F, -60.5F, 0.0F, 4.0F, 0.0F, 0.2F, false);

		cube_r13 = new ModelPart(this);
		cube_r13.setPivot(12.5F, -9.5625F, -60.5F);
		handrail_end_left3.addChild(cube_r13);
		setRotationAngle(cube_r13, 0.0F, 0.0F, 1.0036F);
		cube_r13.setTextureOffset(107, 298).addCuboid(0.0F, -7.4975F, 0.0F, 0.0F, 9.0F, 0.0F, 0.2F, false);

		cube_r14 = new ModelPart(this);
		cube_r14.setPivot(10.6875F, -6.95F, -60.55F);
		handrail_end_left3.addChild(cube_r14);
		setRotationAngle(cube_r14, 0.8598F, -0.6048F, 3.0693F);
		cube_r14.setTextureOffset(107, 298).addCuboid(-0.2075F, 0.2F, 0.2F, 0.0F, 0.0F, 1.0F, 0.2F, false);

		cube_r15 = new ModelPart(this);
		cube_r15.setPivot(12.375F, -7.3125F, -54.8125F);
		handrail_end_left3.addChild(cube_r15);
		setRotationAngle(cube_r15, 1.399F, -1.1819F, -1.3833F);
		cube_r15.setTextureOffset(107, 298).addCuboid(-3.005F, 0.0025F, -0.0075F, 2.0F, 0.0F, 0.0F, 0.2F, false);

		cube_r16 = new ModelPart(this);
		cube_r16.setPivot(12.75F, -6.125F, -55.9375F);
		handrail_end_left3.addChild(cube_r16);
		setRotationAngle(cube_r16, 0.0F, -1.2654F, 0.0F);
		cube_r16.setTextureOffset(107, 298).addCuboid(-4.0F, 0.0F, -0.0025F, 2.0F, 0.0F, 0.0F, 0.2F, false);

		window_seat_handrail = new ModelPart(this);
		window_seat_handrail.setPivot(0.0F, 24.0F, 0.0F);
		

		top_handrails = new ModelPart(this);
		top_handrails.setPivot(0.0F, 0.0F, 0.0F);
		window_seat_handrail.addChild(top_handrails);
		top_handrails.setTextureOffset(113, 297).addCuboid(9.5F, -39.25F, 44.5F, 0.0F, 2.0F, 0.0F, 0.2F, false);
		top_handrails.setTextureOffset(110, 293).addCuboid(9.5F, -39.25F, 31.5F, 0.0F, 2.0F, 0.0F, 0.2F, false);
		top_handrails.setTextureOffset(110, 293).addCuboid(9.5F, -39.25F, 15.75F, 0.0F, 2.0F, 0.0F, 0.2F, false);
		top_handrails.setTextureOffset(110, 293).addCuboid(9.5F, -39.25F, 0.0F, 0.0F, 2.0F, 0.0F, 0.2F, false);
		top_handrails.setTextureOffset(110, 293).addCuboid(9.5F, -39.25F, -15.75F, 0.0F, 2.0F, 0.0F, 0.2F, false);
		top_handrails.setTextureOffset(110, 293).addCuboid(9.5F, -39.25F, -31.5F, 0.0F, 2.0F, 0.0F, 0.2F, false);
		top_handrails.setTextureOffset(110, 293).addCuboid(9.5F, -39.25F, -44.5F, 0.0F, 2.0F, 0.0F, 0.2F, false);
		top_handrails.setTextureOffset(110, 293).addCuboid(-9.5F, -39.25F, 44.5F, 0.0F, 2.0F, 0.0F, 0.2F, false);
		top_handrails.setTextureOffset(110, 293).addCuboid(-9.5F, -39.25F, 31.5F, 0.0F, 2.0F, 0.0F, 0.2F, false);
		top_handrails.setTextureOffset(110, 293).addCuboid(-9.5F, -39.25F, 15.75F, 0.0F, 2.0F, 0.0F, 0.2F, false);
		top_handrails.setTextureOffset(110, 293).addCuboid(-9.5F, -39.25F, 0.0F, 0.0F, 2.0F, 0.0F, 0.2F, false);
		top_handrails.setTextureOffset(110, 293).addCuboid(-9.5F, -39.25F, -15.75F, 0.0F, 2.0F, 0.0F, 0.2F, false);
		top_handrails.setTextureOffset(110, 293).addCuboid(-9.5F, -39.25F, -31.5F, 0.0F, 2.0F, 0.0F, 0.2F, false);
		top_handrails.setTextureOffset(110, 293).addCuboid(-9.5F, -39.25F, -44.5F, 0.0F, 2.0F, 0.0F, 0.2F, false);
		top_handrails.setTextureOffset(450, 451).addCuboid(9.5F, -37.06F, -44.5F, 0.0F, 0.0F, 45.0F, 0.2F, false);
		top_handrails.setTextureOffset(450, 451).addCuboid(9.5F, -37.06F, 0.25F, 0.0F, 0.0F, 44.0F, 0.2F, false);
		top_handrails.setTextureOffset(450, 451).addCuboid(-9.5F, -37.06F, -44.5F, 0.0F, 0.0F, 45.0F, 0.2F, false);
		top_handrails.setTextureOffset(450, 451).addCuboid(-9.5F, -37.06F, 0.5F, 0.0F, 0.0F, 44.0F, 0.2F, false);

		handrail_window_left7 = new ModelPart(this);
		handrail_window_left7.setPivot(0.0F, 0.0F, -25.0F);
		window_seat_handrail.addChild(handrail_window_left7);
		handrail_window_left7.setTextureOffset(105, 298).addCuboid(11.0F, -33.06F, -19.5F, 0.0F, 26.0F, 0.0F, 0.2F, false);

		cube_r17 = new ModelPart(this);
		cube_r17.setPivot(10.25F, -35.185F, -19.5F);
		handrail_window_left7.addChild(cube_r17);
		setRotationAngle(cube_r17, 0.0F, 0.0F, -0.3622F);
		cube_r17.setTextureOffset(105, 298).addCuboid(0.0F, -2.0F, 0.0F, 0.0F, 4.0F, 0.0F, 0.2F, false);

		cube_r18 = new ModelPart(this);
		cube_r18.setPivot(10.6875F, -6.95F, -19.55F);
		handrail_window_left7.addChild(cube_r18);
		setRotationAngle(cube_r18, 0.8598F, -0.6048F, 3.0693F);
		cube_r18.setTextureOffset(105, 298).addCuboid(-0.2075F, 0.2F, 0.2F, 0.0F, 0.0F, 1.0F, 0.2F, false);

		cube_r19 = new ModelPart(this);
		cube_r19.setPivot(12.375F, -7.3125F, -13.8125F);
		handrail_window_left7.addChild(cube_r19);
		setRotationAngle(cube_r19, 1.399F, -1.1819F, -1.3833F);
		cube_r19.setTextureOffset(105, 298).addCuboid(-3.0F, 0.0025F, 0.0025F, 2.0F, 0.0F, 0.0F, 0.2F, false);

		cube_r20 = new ModelPart(this);
		cube_r20.setPivot(12.75F, -6.125F, -14.9375F);
		handrail_window_left7.addChild(cube_r20);
		setRotationAngle(cube_r20, 0.0F, -1.2654F, 0.0F);
		cube_r20.setTextureOffset(105, 298).addCuboid(-4.0F, 0.0F, 0.0075F, 2.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_window_left6 = new ModelPart(this);
		handrail_window_left6.setPivot(0.0F, 0.0F, -81.0F);
		window_seat_handrail.addChild(handrail_window_left6);
		handrail_window_left6.setTextureOffset(105, 298).addCuboid(11.0F, -33.06F, 49.5F, 0.0F, 26.0F, 0.0F, 0.2F, false);
		handrail_window_left6.setTextureOffset(105, 298).addCuboid(18.93F, -13.62F, 48.5F, 0.0F, 0.0F, 1.0F, 0.2F, false);
		handrail_window_left6.setTextureOffset(105, 298).addCuboid(12.5F, -9.31F, 49.5F, 0.0F, 1.0F, 0.0F, 0.2F, false);
		handrail_window_left6.setTextureOffset(105, 298).addCuboid(12.5F, -8.06F, 49.5F, 4.0F, 0.0F, 0.0F, 0.2F, false);
		handrail_window_left6.setTextureOffset(105, 298).addCuboid(16.5F, -12.06F, 49.5F, 0.0F, 4.0F, 0.0F, 0.2F, false);

		cube_r21 = new ModelPart(this);
		cube_r21.setPivot(10.25F, -35.185F, 49.5F);
		handrail_window_left6.addChild(cube_r21);
		setRotationAngle(cube_r21, 0.0F, 0.0F, -0.3622F);
		cube_r21.setTextureOffset(105, 298).addCuboid(0.0F, -2.0F, 0.0F, 0.0F, 4.0F, 0.0F, 0.2F, false);

		cube_r22 = new ModelPart(this);
		cube_r22.setPivot(12.5F, -9.5625F, 49.5F);
		handrail_window_left6.addChild(cube_r22);
		setRotationAngle(cube_r22, 0.0F, 0.0F, 1.0036F);
		cube_r22.setTextureOffset(105, 298).addCuboid(0.0F, -7.4975F, 0.0F, 0.0F, 9.0F, 0.0F, 0.2F, false);

		cube_r23 = new ModelPart(this);
		cube_r23.setPivot(10.6875F, -6.95F, 49.55F);
		handrail_window_left6.addChild(cube_r23);
		setRotationAngle(cube_r23, -0.8598F, 0.6048F, 3.0693F);
		cube_r23.setTextureOffset(105, 298).addCuboid(-0.2075F, 0.2F, -1.2F, 0.0F, 0.0F, 1.0F, 0.2F, false);

		cube_r24 = new ModelPart(this);
		cube_r24.setPivot(12.375F, -7.3125F, 43.8125F);
		handrail_window_left6.addChild(cube_r24);
		setRotationAngle(cube_r24, -1.399F, 1.1819F, -1.3833F);
		cube_r24.setTextureOffset(105, 298).addCuboid(-3.0F, 0.0025F, 0.0075F, 2.0F, 0.0F, 0.0F, 0.2F, false);

		cube_r25 = new ModelPart(this);
		cube_r25.setPivot(12.75F, -6.125F, 44.9375F);
		handrail_window_left6.addChild(cube_r25);
		setRotationAngle(cube_r25, 0.0F, 1.2654F, 0.0F);
		cube_r25.setTextureOffset(105, 298).addCuboid(-4.0F, 0.0F, 0.0025F, 2.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_window_left5 = new ModelPart(this);
		handrail_window_left5.setPivot(-3.0F, 0.0F, -49.75F);
		window_seat_handrail.addChild(handrail_window_left5);
		handrail_window_left5.setTextureOffset(105, 298).addCuboid(11.0F, -33.31F, 34.0F, 0.0F, 16.0F, 0.0F, 0.2F, false);

		cube_r26 = new ModelPart(this);
		cube_r26.setPivot(11.75F, -35.185F, 34.0F);
		handrail_window_left5.addChild(cube_r26);
		setRotationAngle(cube_r26, 0.0F, 0.0F, 0.3622F);
		cube_r26.setTextureOffset(105, 298).addCuboid(0.0F, -2.0F, 0.0F, 0.0F, 4.0F, 0.0F, 0.2F, false);

		cube_r27 = new ModelPart(this);
		cube_r27.setPivot(10.75F, -17.1375F, 33.8F);
		handrail_window_left5.addChild(cube_r27);
		setRotationAngle(cube_r27, 0.0F, 1.5708F, -2.4871F);
		cube_r27.setTextureOffset(105, 298).addCuboid(-0.2F, 0.2375F, -1.2F, 0.0F, 0.0F, 1.0F, 0.2F, false);

		handrail_window_left4 = new ModelPart(this);
		handrail_window_left4.setPivot(-3.0F, 0.0F, -30.75F);
		window_seat_handrail.addChild(handrail_window_left4);
		handrail_window_left4.setTextureOffset(105, 298).addCuboid(11.0F, -33.31F, 30.75F, 0.0F, 16.0F, 0.0F, 0.2F, false);

		cube_r28 = new ModelPart(this);
		cube_r28.setPivot(11.75F, -35.185F, 30.75F);
		handrail_window_left4.addChild(cube_r28);
		setRotationAngle(cube_r28, 0.0F, 0.0F, 0.3622F);
		cube_r28.setTextureOffset(105, 298).addCuboid(0.0F, -2.0F, 0.0F, 0.0F, 4.0F, 0.0F, 0.2F, false);

		cube_r29 = new ModelPart(this);
		cube_r29.setPivot(10.75F, -17.1375F, 30.55F);
		handrail_window_left4.addChild(cube_r29);
		setRotationAngle(cube_r29, 0.0F, 1.5708F, -2.4871F);
		cube_r29.setTextureOffset(105, 298).addCuboid(-0.2F, 0.2375F, -1.2F, 0.0F, 0.0F, 1.0F, 0.2F, false);

		handrail_window_left3 = new ModelPart(this);
		handrail_window_left3.setPivot(-3.0F, 0.0F, -12.0F);
		window_seat_handrail.addChild(handrail_window_left3);
		handrail_window_left3.setTextureOffset(105, 298).addCuboid(11.0F, -33.31F, 27.75F, 0.0F, 16.0F, 0.0F, 0.2F, false);

		cube_r30 = new ModelPart(this);
		cube_r30.setPivot(11.75F, -35.185F, 27.75F);
		handrail_window_left3.addChild(cube_r30);
		setRotationAngle(cube_r30, 0.0F, 0.0F, 0.3622F);
		cube_r30.setTextureOffset(105, 298).addCuboid(0.0F, -2.0F, 0.0F, 0.0F, 4.0F, 0.0F, 0.2F, false);

		cube_r31 = new ModelPart(this);
		cube_r31.setPivot(10.75F, -17.1375F, 27.55F);
		handrail_window_left3.addChild(cube_r31);
		setRotationAngle(cube_r31, 0.0F, 1.5708F, -2.4871F);
		cube_r31.setTextureOffset(105, 298).addCuboid(-0.2F, 0.2075F, -1.2F, 0.0F, 0.0F, 1.0F, 0.2F, false);

		handrail_window_left2 = new ModelPart(this);
		handrail_window_left2.setPivot(0.0F, 0.0F, 73.0F);
		window_seat_handrail.addChild(handrail_window_left2);
		handrail_window_left2.setTextureOffset(105, 298).addCuboid(11.0F, -33.06F, -41.5F, 0.0F, 26.0F, 0.0F, 0.2F, false);
		handrail_window_left2.setTextureOffset(105, 298).addCuboid(18.93F, -13.6F, -41.5F, 0.0F, 0.0F, 1.0F, 0.2F, false);
		handrail_window_left2.setTextureOffset(105, 298).addCuboid(12.5F, -9.31F, -41.5F, 0.0F, 1.0F, 0.0F, 0.2F, false);
		handrail_window_left2.setTextureOffset(105, 298).addCuboid(12.5F, -8.06F, -41.5F, 4.0F, 0.0F, 0.0F, 0.2F, false);
		handrail_window_left2.setTextureOffset(105, 298).addCuboid(16.5F, -12.06F, -41.5F, 0.0F, 4.0F, 0.0F, 0.2F, false);

		cube_r32 = new ModelPart(this);
		cube_r32.setPivot(10.25F, -35.185F, -41.5F);
		handrail_window_left2.addChild(cube_r32);
		setRotationAngle(cube_r32, 0.0F, 0.0F, -0.3622F);
		cube_r32.setTextureOffset(105, 298).addCuboid(0.0F, -2.0F, 0.0F, 0.0F, 4.0F, 0.0F, 0.2F, false);

		cube_r33 = new ModelPart(this);
		cube_r33.setPivot(12.5F, -9.5625F, -41.5F);
		handrail_window_left2.addChild(cube_r33);
		setRotationAngle(cube_r33, 0.0F, 0.0F, 1.0036F);
		cube_r33.setTextureOffset(105, 298).addCuboid(0.0F, -7.4975F, 0.0F, 0.0F, 9.0F, 0.0F, 0.2F, false);

		cube_r34 = new ModelPart(this);
		cube_r34.setPivot(10.6875F, -6.95F, -41.55F);
		handrail_window_left2.addChild(cube_r34);
		setRotationAngle(cube_r34, 0.8598F, -0.6048F, 3.0693F);
		cube_r34.setTextureOffset(105, 298).addCuboid(-0.2075F, 0.2F, 0.2F, 0.0F, 0.0F, 1.0F, 0.2F, false);

		cube_r35 = new ModelPart(this);
		cube_r35.setPivot(12.375F, -7.3125F, -35.8125F);
		handrail_window_left2.addChild(cube_r35);
		setRotationAngle(cube_r35, 1.399F, -1.1819F, -1.3833F);
		cube_r35.setTextureOffset(105, 298).addCuboid(-3.005F, 0.0025F, -0.0075F, 2.0F, 0.0F, 0.0F, 0.2F, false);

		cube_r36 = new ModelPart(this);
		cube_r36.setPivot(12.75F, -6.125F, -36.9375F);
		handrail_window_left2.addChild(cube_r36);
		setRotationAngle(cube_r36, 0.0F, -1.2654F, 0.0F);
		cube_r36.setTextureOffset(105, 298).addCuboid(-4.0F, 0.005F, -0.0025F, 2.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_window_left1 = new ModelPart(this);
		handrail_window_left1.setPivot(0.0F, 0.0F, 17.0F);
		window_seat_handrail.addChild(handrail_window_left1);
		handrail_window_left1.setTextureOffset(105, 298).addCuboid(11.0F, -33.06F, 27.5F, 0.0F, 26.0F, 0.0F, 0.2F, false);

		cube_r37 = new ModelPart(this);
		cube_r37.setPivot(10.25F, -35.185F, 27.5F);
		handrail_window_left1.addChild(cube_r37);
		setRotationAngle(cube_r37, 0.0F, 0.0F, -0.3622F);
		cube_r37.setTextureOffset(105, 298).addCuboid(0.0F, -2.0F, 0.0F, 0.0F, 4.0F, 0.0F, 0.2F, false);

		cube_r38 = new ModelPart(this);
		cube_r38.setPivot(10.6875F, -6.95F, 27.55F);
		handrail_window_left1.addChild(cube_r38);
		setRotationAngle(cube_r38, -0.8598F, 0.6048F, 3.0693F);
		cube_r38.setTextureOffset(105, 298).addCuboid(-0.2075F, 0.2F, -1.2F, 0.0F, 0.0F, 1.0F, 0.2F, false);

		cube_r39 = new ModelPart(this);
		cube_r39.setPivot(12.375F, -7.3125F, 21.8125F);
		handrail_window_left1.addChild(cube_r39);
		setRotationAngle(cube_r39, -1.399F, 1.1819F, -1.3833F);
		cube_r39.setTextureOffset(105, 298).addCuboid(-3.075F, 0.0025F, -0.0025F, 2.0F, 0.0F, 0.0F, 0.2F, false);

		cube_r40 = new ModelPart(this);
		cube_r40.setPivot(12.75F, -6.125F, 22.9375F);
		handrail_window_left1.addChild(cube_r40);
		setRotationAngle(cube_r40, 0.0F, 1.2654F, 0.0F);
		cube_r40.setTextureOffset(105, 298).addCuboid(-4.0F, 0.025F, -0.0075F, 2.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_window_right5 = new ModelPart(this);
		handrail_window_right5.setPivot(3.0F, 0.0F, -12.0F);
		window_seat_handrail.addChild(handrail_window_right5);
		handrail_window_right5.setTextureOffset(105, 298).addCuboid(-11.0F, -33.31F, 27.75F, 0.0F, 16.0F, 0.0F, 0.2F, false);

		cube_r41 = new ModelPart(this);
		cube_r41.setPivot(-11.75F, -35.185F, 27.75F);
		handrail_window_right5.addChild(cube_r41);
		setRotationAngle(cube_r41, 0.0F, 0.0F, -0.3622F);
		cube_r41.setTextureOffset(105, 298).addCuboid(0.0F, -2.0F, 0.0F, 0.0F, 4.0F, 0.0F, 0.2F, false);

		cube_r42 = new ModelPart(this);
		cube_r42.setPivot(-10.75F, -17.1375F, 27.55F);
		handrail_window_right5.addChild(cube_r42);
		setRotationAngle(cube_r42, 0.0F, -1.5708F, 2.4871F);
		cube_r42.setTextureOffset(105, 298).addCuboid(0.2F, 0.2075F, -1.2F, 0.0F, 0.0F, 1.0F, 0.2F, false);

		handrail_window_right4 = new ModelPart(this);
		handrail_window_right4.setPivot(3.0F, 0.0F, -47.5F);
		window_seat_handrail.addChild(handrail_window_right4);
		handrail_window_right4.setTextureOffset(105, 298).addCuboid(-11.0F, -33.3F, 31.75F, 0.0F, 16.0F, 0.0F, 0.2F, false);

		cube_r43 = new ModelPart(this);
		cube_r43.setPivot(-11.75F, -35.185F, 30.75F);
		handrail_window_right4.addChild(cube_r43);
		setRotationAngle(cube_r43, 0.0F, 0.0F, -0.3622F);
		cube_r43.setTextureOffset(105, 298).addCuboid(0.0F, -2.0F, 1.0F, 0.0F, 4.0F, 0.0F, 0.2F, false);

		cube_r44 = new ModelPart(this);
		cube_r44.setPivot(-10.75F, -17.1375F, 30.55F);
		handrail_window_right4.addChild(cube_r44);
		setRotationAngle(cube_r44, 0.0F, -1.5708F, 2.4871F);
		cube_r44.setTextureOffset(105, 298).addCuboid(1.2F, 0.2075F, -1.2F, 0.0F, 0.0F, 1.0F, 0.2F, false);

		handrail_window_right3 = new ModelPart(this);
		handrail_window_right3.setPivot(3.0F, 0.0F, -30.75F);
		window_seat_handrail.addChild(handrail_window_right3);
		handrail_window_right3.setTextureOffset(105, 298).addCuboid(-11.0F, -33.31F, 30.75F, 0.0F, 16.0F, 0.0F, 0.2F, false);

		cube_r45 = new ModelPart(this);
		cube_r45.setPivot(-11.75F, -35.185F, 30.75F);
		handrail_window_right3.addChild(cube_r45);
		setRotationAngle(cube_r45, 0.0F, 0.0F, -0.3622F);
		cube_r45.setTextureOffset(105, 298).addCuboid(0.0F, -2.0F, 0.0F, 0.0F, 4.0F, 0.0F, 0.2F, false);

		cube_r46 = new ModelPart(this);
		cube_r46.setPivot(-10.75F, -17.1375F, 30.55F);
		handrail_window_right3.addChild(cube_r46);
		setRotationAngle(cube_r46, 0.0F, -1.5708F, 2.4871F);
		cube_r46.setTextureOffset(105, 298).addCuboid(0.2F, 0.2075F, -1.2F, 0.0F, 0.0F, 1.0F, 0.2F, false);

		handrail_window_right2 = new ModelPart(this);
		handrail_window_right2.setPivot(0.0F, 0.0F, 76.0F);
		window_seat_handrail.addChild(handrail_window_right2);
		handrail_window_right2.setTextureOffset(106, 302).addCuboid(-11.0F, -33.06F, -44.5F, 0.0F, 26.0F, 0.0F, 0.2F, false);
		handrail_window_right2.setTextureOffset(106, 302).addCuboid(-18.93F, -13.6F, -44.5F, 0.0F, 0.0F, 1.0F, 0.2F, false);
		handrail_window_right2.setTextureOffset(106, 302).addCuboid(-12.5F, -9.31F, -44.5F, 0.0F, 1.0F, 0.0F, 0.2F, false);
		handrail_window_right2.setTextureOffset(106, 302).addCuboid(-16.5F, -8.06F, -44.5F, 4.0F, 0.0F, 0.0F, 0.2F, false);
		handrail_window_right2.setTextureOffset(106, 302).addCuboid(-16.5F, -12.06F, -44.5F, 0.0F, 4.0F, 0.0F, 0.2F, false);

		cube_r47 = new ModelPart(this);
		cube_r47.setPivot(-12.5F, -9.5625F, -44.5F);
		handrail_window_right2.addChild(cube_r47);
		setRotationAngle(cube_r47, 0.0F, 0.0F, -1.0036F);
		cube_r47.setTextureOffset(106, 302).addCuboid(0.0F, -7.4975F, 0.0F, 0.0F, 9.0F, 0.0F, 0.2F, false);

		cube_r48 = new ModelPart(this);
		cube_r48.setPivot(-10.25F, -35.185F, -44.5F);
		handrail_window_right2.addChild(cube_r48);
		setRotationAngle(cube_r48, 0.0F, 0.0F, 0.3622F);
		cube_r48.setTextureOffset(106, 302).addCuboid(0.0F, -2.0F, 0.0F, 0.0F, 4.0F, 0.0F, 0.2F, false);

		cube_r49 = new ModelPart(this);
		cube_r49.setPivot(-10.6875F, -6.95F, -44.55F);
		handrail_window_right2.addChild(cube_r49);
		setRotationAngle(cube_r49, 0.8598F, 0.6048F, -3.0693F);
		cube_r49.setTextureOffset(106, 302).addCuboid(0.2075F, 0.2F, 0.2F, 0.0F, 0.0F, 1.0F, 0.2F, false);

		cube_r50 = new ModelPart(this);
		cube_r50.setPivot(-12.375F, -7.3125F, -38.8125F);
		handrail_window_right2.addChild(cube_r50);
		setRotationAngle(cube_r50, 1.399F, 1.1819F, 1.3833F);
		cube_r50.setTextureOffset(106, 302).addCuboid(1.0F, 0.0025F, -0.0875F, 2.0F, 0.0F, 0.0F, 0.2F, false);

		cube_r51 = new ModelPart(this);
		cube_r51.setPivot(-12.75F, -6.125F, -39.9375F);
		handrail_window_right2.addChild(cube_r51);
		setRotationAngle(cube_r51, 0.0F, 1.2654F, 0.0F);
		cube_r51.setTextureOffset(106, 302).addCuboid(2.0F, 0.0F, -0.0025F, 2.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_window_right1 = new ModelPart(this);
		handrail_window_right1.setPivot(0.0F, 0.0F, 17.0F);
		window_seat_handrail.addChild(handrail_window_right1);
		handrail_window_right1.setTextureOffset(112, 308).addCuboid(-11.0F, -33.06F, 27.5F, 0.0F, 26.0F, 0.0F, 0.2F, false);

		cube_r52 = new ModelPart(this);
		cube_r52.setPivot(-10.6875F, -6.95F, 27.55F);
		handrail_window_right1.addChild(cube_r52);
		setRotationAngle(cube_r52, -0.8598F, -0.6048F, -3.0693F);
		cube_r52.setTextureOffset(109, 322).addCuboid(0.2075F, 0.2F, -1.2F, 0.0F, 0.0F, 1.0F, 0.2F, false);

		cube_r53 = new ModelPart(this);
		cube_r53.setPivot(-12.375F, -7.3125F, 21.8125F);
		handrail_window_right1.addChild(cube_r53);
		setRotationAngle(cube_r53, -1.399F, -1.1819F, 1.3833F);
		cube_r53.setTextureOffset(109, 322).addCuboid(1.075F, 0.0025F, -0.0025F, 2.0F, 0.0F, 0.0F, 0.2F, false);

		cube_r54 = new ModelPart(this);
		cube_r54.setPivot(-12.75F, -6.125F, 22.9375F);
		handrail_window_right1.addChild(cube_r54);
		setRotationAngle(cube_r54, 0.0F, -1.2654F, 0.0F);
		cube_r54.setTextureOffset(109, 322).addCuboid(2.0F, 0.025F, -0.0075F, 2.0F, 0.0F, 0.0F, 0.2F, false);

		cube_r55 = new ModelPart(this);
		cube_r55.setPivot(-10.25F, -35.185F, 27.5F);
		handrail_window_right1.addChild(cube_r55);
		setRotationAngle(cube_r55, 0.0F, 0.0F, 0.3622F);
		cube_r55.setTextureOffset(109, 322).addCuboid(0.0F, -2.0F, 0.0F, 0.0F, 4.0F, 0.0F, 0.2F, false);

		wall_replace = new ModelPart(this);
		wall_replace.setPivot(3.2878F, -12.5941F, -26.0F);
		handrail_window_right1.addChild(wall_replace);
		wall_replace.setTextureOffset(94, 105).addCuboid(-23.4278F, 1.6941F, -36.0F, 0.0F, 11.0F, 14.0F, 0.0F, false);

		wall_right_2_r4 = new ModelPart(this);
		wall_right_2_r4.setPivot(0.0F, 0.0F, 0.0F);
		wall_replace.addChild(wall_right_2_r4);
		setRotationAngle(wall_right_2_r4, 0.0F, 0.0F, 0.1309F);
		wall_right_2_r4.setTextureOffset(0, 75).addCuboid(-22.9878F, -24.0059F, -36.0F, 0.0F, 29.0F, 14.0F, 0.0F, false);

		blue_floor = new ModelPart(this);
		blue_floor.setPivot(-3.2878F, 12.5941F, 8.0F);
		wall_replace.addChild(blue_floor);
		blue_floor.setTextureOffset(18, 28).addCuboid(-18.0F, -0.125F, -44.0F, 10.0F, 0.0F, 14.0F, 0.0F, false);

		wall_replace2 = new ModelPart(this);
		wall_replace2.setPivot(0.0F, 0.0F, 0.0F);
		wall_replace.addChild(wall_replace2);
		

		wall_left_r1 = new ModelPart(this);
		wall_left_r1.setPivot(0.0F, 0.0F, 0.0F);
		wall_replace2.addChild(wall_left_r1);
		setRotationAngle(wall_left_r1, 0.0F, 0.0F, 0.1309F);
		wall_left_r1.setTextureOffset(0, 435).addCuboid(-22.9878F, -20.0059F, 41.0F, 0.0F, 9.0F, 12.0F, 0.0F, false);

		front_seat_handrail = new ModelPart(this);
		front_seat_handrail.setPivot(0.0F, 24.0F, 0.0F);
		

		handrail_head_left = new ModelPart(this);
		handrail_head_left.setPivot(0.0F, 0.0F, 2.0F);
		front_seat_handrail.addChild(handrail_head_left);
		handrail_head_left.setTextureOffset(108, 303).addCuboid(11.0F, -39.06F, 10.5F, 0.0F, 32.0F, 0.0F, 0.2F, false);

		cube_r56 = new ModelPart(this);
		cube_r56.setPivot(10.6875F, -6.95F, 10.55F);
		handrail_head_left.addChild(cube_r56);
		setRotationAngle(cube_r56, -0.8598F, 0.6048F, 3.0693F);
		cube_r56.setTextureOffset(108, 303).addCuboid(-0.2075F, 0.2F, -1.2F, 0.0F, 0.0F, 1.0F, 0.2F, false);

		cube_r57 = new ModelPart(this);
		cube_r57.setPivot(12.375F, -7.3125F, 4.8125F);
		handrail_head_left.addChild(cube_r57);
		setRotationAngle(cube_r57, -1.399F, 1.1819F, -1.3833F);
		cube_r57.setTextureOffset(108, 303).addCuboid(-3.005F, 0.0025F, -0.0025F, 2.0F, 0.0F, 0.0F, 0.2F, false);

		cube_r58 = new ModelPart(this);
		cube_r58.setPivot(12.75F, -6.125F, 5.9375F);
		handrail_head_left.addChild(cube_r58);
		setRotationAngle(cube_r58, 0.0F, 1.2654F, 0.0F);
		cube_r58.setTextureOffset(108, 303).addCuboid(-4.0F, 0.025F, -0.0075F, 2.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_head_right = new ModelPart(this);
		handrail_head_right.setPivot(0.0F, 0.0F, 2.0F);
		front_seat_handrail.addChild(handrail_head_right);
		handrail_head_right.setTextureOffset(108, 303).addCuboid(-11.0F, -39.06F, 10.5F, 0.0F, 32.0F, 0.0F, 0.2F, false);

		cube_r59 = new ModelPart(this);
		cube_r59.setPivot(-10.6875F, -6.95F, 10.55F);
		handrail_head_right.addChild(cube_r59);
		setRotationAngle(cube_r59, -0.8598F, -0.6048F, -3.0693F);
		cube_r59.setTextureOffset(108, 303).addCuboid(0.2075F, 0.2F, -1.2F, 0.0F, 0.0F, 1.0F, 0.2F, false);

		cube_r60 = new ModelPart(this);
		cube_r60.setPivot(-12.375F, -7.3125F, 4.8125F);
		handrail_head_right.addChild(cube_r60);
		setRotationAngle(cube_r60, -1.399F, -1.1819F, 1.3833F);
		cube_r60.setTextureOffset(108, 303).addCuboid(1.075F, 0.0025F, -0.0025F, 2.0F, 0.0F, 0.0F, 0.2F, false);

		cube_r61 = new ModelPart(this);
		cube_r61.setPivot(-12.75F, -6.125F, 5.9375F);
		handrail_head_right.addChild(cube_r61);
		setRotationAngle(cube_r61, 0.0F, -1.2654F, 0.0F);
		cube_r61.setTextureOffset(108, 303).addCuboid(2.0F, 0.005F, -0.0075F, 2.0F, 0.0F, 0.0F, 0.2F, false);

		roof_head = new ModelPart(this);
		roof_head.setPivot(0.0F, 24.0F, 0.0F);
		roof_head.setTextureOffset(28, 28).addCuboid(-17.5F, -39.0F, -44.0F, 35.0F, 0.0F, 52.0F, 0.0F, false);

		roof_window = new ModelPart(this);
		roof_window.setPivot(0.0F, 24.0F, 0.0F);
		roof_window.setTextureOffset(0, 0).addCuboid(-17.5F, -39.0F, -40.0F, 35.0F, 0.0F, 80.0F, 0.0F, false);

		roof_end = new ModelPart(this);
		roof_end.setPivot(0.0F, 24.0F, 0.0F);
		roof_end.setTextureOffset(28, 28).addCuboid(-17.5F, -39.0F, -44.0F, 35.0F, 0.0F, 52.0F, 0.0F, false);

		roof_door = new ModelPart(this);
		roof_door.setPivot(0.0F, 24.0F, 0.0F);
		roof_door.setTextureOffset(48, 22).addCuboid(-17.5F, -39.0F, -16.0F, 35.0F, 0.0F, 32.0F, 0.0F, false);

		head = new ModelPart(this);
		head.setPivot(0.0F, 24.0F, 0.0F);
		head.setTextureOffset(114, 276).addCuboid(-22.0F, 0.0F, -38.0F, 44.0F, 1.0F, 46.0F, 0.0F, false);
		head.setTextureOffset(399, 124).addCuboid(-22.15F, -10.9F, -44.0F, 2.0F, 11.0F, 57.0F, 0.0F, false);
		head.setTextureOffset(183, 381).addCuboid(20.15F, -10.9F, -44.0F, 2.0F, 11.0F, 57.0F, 0.0F, false);

		wall_right_2_r5 = new ModelPart(this);
		wall_right_2_r5.setPivot(-3.2878F, -12.5941F, 3.0F);
		head.addChild(wall_right_2_r5);
		setRotationAngle(wall_right_2_r5, 0.0F, 0.0F, -0.1309F);
		wall_right_2_r5.setTextureOffset(61, 323).addCuboid(22.9978F, -24.0059F, -47.0F, 2.0F, 29.0F, 57.0F, 0.0F, false);

		wall_left_1_r4 = new ModelPart(this);
		wall_left_1_r4.setPivot(3.2878F, -12.5941F, 3.0F);
		head.addChild(wall_left_1_r4);
		setRotationAngle(wall_left_1_r4, 0.0F, 0.0F, 0.1309F);
		wall_left_1_r4.setTextureOffset(298, 323).addCuboid(-24.9978F, -23.9959F, -47.0F, 2.0F, 29.0F, 57.0F, 0.0F, false);

		front_top_r1 = new ModelPart(this);
		front_top_r1.setPivot(0.0F, -58.0F, 16.0F);
		head.addChild(front_top_r1);
		setRotationAngle(front_top_r1, 1.5708F, 0.0F, 0.0F);
		front_top_r1.setTextureOffset(154, 34).addCuboid(-7.0F, -27.0F, -22.0F, 14.0F, 5.0F, 0.0F, 0.0F, false);

		head_bottom = new ModelPart(this);
		head_bottom.setPivot(0.0F, 0.0F, 21.0F);
		head.addChild(head_bottom);
		head_bottom.setTextureOffset(130, 148).addCuboid(18.0F, -4.0F, -27.0F, 3.0F, 4.0F, 19.0F, 0.0F, true);
		head_bottom.setTextureOffset(130, 148).addCuboid(-21.0F, -4.0F, -27.0F, 3.0F, 4.0F, 19.0F, 0.0F, false);

		front_bottom_3_r1 = new ModelPart(this);
		front_bottom_3_r1.setPivot(-19.75F, -4.25F, -8.5F);
		head_bottom.addChild(front_bottom_3_r1);
		setRotationAngle(front_bottom_3_r1, 0.0F, 0.0F, 0.6981F);
		front_bottom_3_r1.setTextureOffset(139, 181).addCuboid(-1.5F, -1.0F, -18.5F, 3.0F, 2.0F, 19.0F, 0.0F, false);

		front_bottom_2_r1 = new ModelPart(this);
		front_bottom_2_r1.setPivot(19.75F, -4.25F, -13.5F);
		head_bottom.addChild(front_bottom_2_r1);
		setRotationAngle(front_bottom_2_r1, 0.0F, 0.0F, -0.6981F);
		front_bottom_2_r1.setTextureOffset(139, 181).addCuboid(-1.5F, -1.0F, -13.5F, 3.0F, 2.0F, 19.0F, 0.0F, false);

		interor_cab_wall = new ModelPart(this);
		interor_cab_wall.setPivot(0.0F, 0.0F, -1.0F);
		head.addChild(interor_cab_wall);
		

		cab_door_back = new ModelPart(this);
		cab_door_back.setPivot(0.0F, 0.0F, -13.0F);
		interor_cab_wall.addChild(cab_door_back);
		setRotationAngle(cab_door_back, -3.1416F, 0.0F, 3.1416F);
		cab_door_back.setTextureOffset(278, 81).addCuboid(-7.0F, -36.0F, -3.0F, 14.0F, 36.0F, 0.0F, 0.0F, false);
		cab_door_back.setTextureOffset(272, 130).addCuboid(-22.0F, -36.0F, -8.0F, 15.0F, 36.0F, 0.0F, 0.0F, false);
		cab_door_back.setTextureOffset(248, 81).addCuboid(7.0F, -36.0F, -8.0F, 15.0F, 36.0F, 0.0F, 0.0F, false);
		cab_door_back.setTextureOffset(154, 43).addCuboid(-22.0F, -39.0F, -8.0F, 44.0F, 3.0F, 0.0F, 0.0F, false);

		front_inside_2_r1 = new ModelPart(this);
		front_inside_2_r1.setPivot(1.0F, 0.0F, 0.0F);
		cab_door_back.addChild(front_inside_2_r1);
		setRotationAngle(front_inside_2_r1, 0.0F, -1.5708F, 0.0F);
		front_inside_2_r1.setTextureOffset(174, 148).addCuboid(-8.0F, -36.0F, -6.0F, 5.0F, 36.0F, 0.0F, 0.0F, false);

		front_inside_1_r1 = new ModelPart(this);
		front_inside_1_r1.setPivot(-1.0F, 0.0F, 0.0F);
		cab_door_back.addChild(front_inside_1_r1);
		setRotationAngle(front_inside_1_r1, 0.0F, -1.5708F, 0.0F);
		front_inside_1_r1.setTextureOffset(252, 148).addCuboid(-8.0F, -36.0F, 6.0F, 5.0F, 36.0F, 0.0F, 0.0F, false);

		head_exterior = new ModelPart(this);
		head_exterior.setPivot(0.0F, 24.0F, 0.0F);
		head_exterior.setTextureOffset(428, 68).addCuboid(-21.75F, 0.0F, -37.0F, 1.0F, 3.0F, 45.0F, 0.0F, false);
		head_exterior.setTextureOffset(0, 28).addCuboid(20.75F, 0.0F, -44.0F, 1.0F, 3.0F, 52.0F, 0.0F, false);
		head_exterior.setTextureOffset(36, 133).addCuboid(16.8F, 3.0F, -44.0F, 4.0F, 3.0F, 20.0F, 0.0F, false);
		head_exterior.setTextureOffset(0, 53).addCuboid(-20.8F, 3.0F, -44.0F, 4.0F, 3.0F, 20.0F, 0.0F, false);

		buttom_panel_6_r2 = new ModelPart(this);
		buttom_panel_6_r2.setPivot(-21.5625F, 4.1F, -30.0F);
		head_exterior.addChild(buttom_panel_6_r2);
		setRotationAngle(buttom_panel_6_r2, 0.0F, 0.0F, -0.1309F);
		buttom_panel_6_r2.setTextureOffset(154, 9).addCuboid(0.0F, -4.1F, -14.0F, 1.0F, 3.0F, 13.0F, 0.0F, false);

		buttom_panel_5_r2 = new ModelPart(this);
		buttom_panel_5_r2.setPivot(21.5625F, 4.1F, -37.0F);
		head_exterior.addChild(buttom_panel_5_r2);
		setRotationAngle(buttom_panel_5_r2, 0.0F, 0.0F, 0.1309F);
		buttom_panel_5_r2.setTextureOffset(60, 13).addCuboid(-1.0F, -4.1F, -7.0F, 1.0F, 3.0F, 6.0F, 0.0F, false);

		floor_5_r1 = new ModelPart(this);
		floor_5_r1.setPivot(-20.375F, 4.0F, -18.0F);
		head_exterior.addChild(floor_5_r1);
		setRotationAngle(floor_5_r1, 0.0F, 0.0F, -0.3054F);
		floor_5_r1.setTextureOffset(36, 5).addCuboid(-0.925F, -1.5F, -26.0F, 2.0F, 3.0F, 20.0F, 0.0F, false);

		floor_4_r1 = new ModelPart(this);
		floor_4_r1.setPivot(20.375F, 4.0F, -18.0F);
		head_exterior.addChild(floor_4_r1);
		setRotationAngle(floor_4_r1, 0.0F, 0.0F, 0.3054F);
		floor_4_r1.setTextureOffset(44, 166).addCuboid(-1.075F, -1.5F, -26.0F, 2.0F, 3.0F, 20.0F, 0.0F, false);

		head_bottom_out = new ModelPart(this);
		head_bottom_out.setPivot(0.0F, 0.1F, -23.0F);
		head_exterior.addChild(head_bottom_out);
		head_bottom_out.setTextureOffset(188, 29).addCuboid(-6.0F, -0.1F, -24.0F, 12.0F, 3.0F, 9.0F, 0.0F, false);
		head_bottom_out.setTextureOffset(210, 225).addCuboid(-21.5F, -0.1F, -21.0F, 7.0F, 3.0F, 8.0F, 0.0F, false);
		head_bottom_out.setTextureOffset(188, 222).addCuboid(14.5F, -0.1F, -21.0F, 7.0F, 3.0F, 8.0F, 0.0F, false);

		buttom_panel_right_6_r2 = new ModelPart(this);
		buttom_panel_right_6_r2.setPivot(9.7131F, 1.4F, -21.8861F);
		head_bottom_out.addChild(buttom_panel_right_6_r2);
		setRotationAngle(buttom_panel_right_6_r2, 0.0F, -0.1745F, 0.0F);
		buttom_panel_right_6_r2.setTextureOffset(188, 211).addCuboid(-4.5031F, -1.5F, -0.4939F, 11.0F, 3.0F, 8.0F, 0.0F, false);

		buttom_panel_right_5_r2 = new ModelPart(this);
		buttom_panel_right_5_r2.setPivot(-9.7131F, 1.4F, -21.8861F);
		head_bottom_out.addChild(buttom_panel_right_5_r2);
		setRotationAngle(buttom_panel_right_5_r2, 0.0F, 0.1745F, 0.0F);
		buttom_panel_right_5_r2.setTextureOffset(218, 214).addCuboid(-6.4969F, -1.5F, -0.4939F, 11.0F, 3.0F, 8.0F, 0.0F, false);

		walls = new ModelPart(this);
		walls.setPivot(0.0F, -1.0F, 0.0F);
		head_exterior.addChild(walls);
		walls.setTextureOffset(416, 0).addCuboid(-22.15F, -9.9F, -44.0F, 2.0F, 11.0F, 57.0F, 0.0F, false);
		walls.setTextureOffset(244, 409).addCuboid(20.15F, -9.9F, -44.0F, 2.0F, 11.0F, 57.0F, 0.0F, false);

		bottom_wall_right_r3 = new ModelPart(this);
		bottom_wall_right_r3.setPivot(0.4068F, 5.1977F, -12.0F);
		walls.addChild(bottom_wall_right_r3);
		setRotationAngle(bottom_wall_right_r3, -0.1309F, 1.5708F, 0.0F);
		bottom_wall_right_r3.setTextureOffset(154, 3).addCuboid(-20.0068F, -7.0007F, 21.0F, 52.0F, 3.0F, 0.0F, 0.0F, false);

		bottom_wall_left_r3 = new ModelPart(this);
		bottom_wall_left_r3.setPivot(-0.4068F, 5.1977F, -12.0F);
		walls.addChild(bottom_wall_left_r3);
		setRotationAngle(bottom_wall_left_r3, -0.1309F, -1.5708F, 0.0F);
		bottom_wall_left_r3.setTextureOffset(154, 6).addCuboid(-32.0032F, -7.0007F, 21.0F, 52.0F, 3.0F, 0.0F, 0.0F, false);

		wall_right_2_r6 = new ModelPart(this);
		wall_right_2_r6.setPivot(-3.2878F, -11.5941F, 3.0F);
		walls.addChild(wall_right_2_r6);
		setRotationAngle(wall_right_2_r6, 0.0F, 0.0F, -0.1309F);
		wall_right_2_r6.setTextureOffset(355, 24).addCuboid(22.9978F, -23.9959F, -47.0F, 2.0F, 29.0F, 57.0F, 0.0F, false);

		wall_left_1_r5 = new ModelPart(this);
		wall_left_1_r5.setPivot(3.2878F, -11.5941F, 3.0F);
		walls.addChild(wall_left_1_r5);
		setRotationAngle(wall_left_1_r5, 0.0F, 0.0F, 0.1309F);
		wall_left_1_r5.setTextureOffset(359, 294).addCuboid(-24.9978F, -23.9959F, -47.0F, 2.0F, 29.0F, 57.0F, 0.0F, false);

		exteror_cab_front = new ModelPart(this);
		exteror_cab_front.setPivot(0.0F, 0.0F, -9.0F);
		head_exterior.addChild(exteror_cab_front);
		exteror_cab_front.setTextureOffset(188, 148).addCuboid(-22.0F, -42.0F, -35.0F, 16.0F, 42.0F, 0.0F, 0.0F, false);
		exteror_cab_front.setTextureOffset(154, 46).addCuboid(6.0F, -42.0F, -35.0F, 16.0F, 42.0F, 0.0F, 0.0F, false);
		exteror_cab_front.setTextureOffset(134, 188).addCuboid(-6.0F, -42.0F, -35.0F, 12.0F, 6.0F, 0.0F, 0.0F, false);

		front_in_1_r1 = new ModelPart(this);
		front_in_1_r1.setPivot(0.0F, -59.0F, -1.0F);
		exteror_cab_front.addChild(front_in_1_r1);
		setRotationAngle(front_in_1_r1, 1.5708F, 0.0F, 0.0F);
		front_in_1_r1.setTextureOffset(62, 198).addCuboid(-7.0F, -34.0F, -23.0F, 14.0F, 5.0F, 0.0F, 0.0F, false);

		cab_door = new ModelPart(this);
		cab_door.setPivot(0.0F, 0.0F, 0.0F);
		exteror_cab_front.addChild(cab_door);
		cab_door.setTextureOffset(0, 280).addCuboid(-6.0F, -36.0F, -31.0F, 12.0F, 36.0F, 0.0F, 0.0F, false);

		front_in_1_r2 = new ModelPart(this);
		front_in_1_r2.setPivot(-1.0F, 0.0F, -12.0F);
		cab_door.addChild(front_in_1_r2);
		setRotationAngle(front_in_1_r2, 0.0F, 1.5708F, 0.0F);
		front_in_1_r2.setTextureOffset(280, 3).addCuboid(18.0F, -36.0F, 7.0F, 5.0F, 36.0F, 0.0F, 0.0F, false);

		front_in_2_r1 = new ModelPart(this);
		front_in_2_r1.setPivot(-1.0F, 0.0F, -12.0F);
		cab_door.addChild(front_in_2_r1);
		setRotationAngle(front_in_2_r1, 0.0F, -1.5708F, 0.0F);
		front_in_2_r1.setTextureOffset(24, 280).addCuboid(-23.0F, -36.0F, 5.0F, 5.0F, 36.0F, 0.0F, 0.0F, false);

		front_train_barrier = new ModelPart(this);
		front_train_barrier.setPivot(0.0F, 0.0F, 0.0F);
		head_exterior.addChild(front_train_barrier);
		front_train_barrier.setTextureOffset(68, 2).addCuboid(-20.25F, -17.0F, -47.0F, 0.0F, 12.0F, 3.0F, 0.3F, false);
		front_train_barrier.setTextureOffset(68, 7).addCuboid(-7.0F, -16.0F, -47.0F, 0.0F, 7.0F, 3.0F, 0.3F, false);
		front_train_barrier.setTextureOffset(68, 2).addCuboid(20.25F, -17.0F, -47.0F, 0.0F, 12.0F, 3.0F, 0.3F, false);
		front_train_barrier.setTextureOffset(68, 0).addCuboid(7.25F, -15.0F, -47.0F, 0.0F, 14.0F, 3.0F, 0.3F, false);

		roof_window_light = new ModelPart(this);
		roof_window_light.setPivot(0.0F, 24.0F, 0.0F);
		roof_window_light.setTextureOffset(248, 0).addCuboid(11.75F, -39.5F, -40.0F, 2.0F, 1.0F, 80.0F, 0.0F, false);
		roof_window_light.setTextureOffset(188, 130).addCuboid(-13.75F, -39.5F, -40.0F, 2.0F, 1.0F, 80.0F, 0.0F, false);

		roof_door_light = new ModelPart(this);
		roof_door_light.setPivot(0.0F, 24.0F, 0.0F);
		roof_door_light.setTextureOffset(94, 148).addCuboid(11.75F, -39.5F, -16.0F, 2.0F, 1.0F, 32.0F, 0.0F, false);
		roof_door_light.setTextureOffset(0, 133).addCuboid(-13.75F, -39.5F, -16.0F, 2.0F, 1.0F, 32.0F, 0.0F, false);

		roof_head_light = new ModelPart(this);
		roof_head_light.setPivot(0.0F, 24.0F, 0.0F);
		roof_head_light.setTextureOffset(0, 148).addCuboid(11.75F, -39.5F, -6.0F, 2.0F, 1.0F, 14.0F, 0.0F, false);
		roof_head_light.setTextureOffset(0, 133).addCuboid(-13.75F, -39.5F, -6.0F, 2.0F, 1.0F, 14.0F, 0.0F, false);

		roof_end_light = new ModelPart(this);
		roof_end_light.setPivot(0.0F, 24.0F, 0.0F);
		roof_end_light.setTextureOffset(305, 409).addCuboid(11.75F, -39.5F, -40.0F, 2.0F, 1.0F, 48.0F, 0.0F, false);
		roof_end_light.setTextureOffset(183, 332).addCuboid(-13.75F, -39.5F, -40.0F, 2.0F, 1.0F, 48.0F, 0.0F, false);

		tail_lights = new ModelPart(this);
		tail_lights.setPivot(0.0F, 24.0F, 0.0F);
		tail_lights.setTextureOffset(52, 103).addCuboid(-19.0F, -9.125F, -44.25F, 4.0F, 4.0F, 0.0F, 0.0F, false);
		tail_lights.setTextureOffset(40, 76).addCuboid(15.0F, -9.125F, -44.25F, 4.0F, 4.0F, 0.0F, 0.0F, false);
		tail_lights.setTextureOffset(50, 22).addCuboid(-16.5F, -38.75F, -44.25F, 3.0F, 3.0F, 0.0F, 0.0F, false);
		tail_lights.setTextureOffset(60, 22).addCuboid(13.5F, -38.75F, -44.25F, 3.0F, 3.0F, 0.0F, 0.0F, false);

		headlights = new ModelPart(this);
		headlights.setPivot(0.0F, 24.0F, 0.0F);
		headlights.setTextureOffset(28, 115).addCuboid(10.75F, -7.0F, -44.25F, 4.0F, 4.0F, 0.0F, 0.0F, false);
		headlights.setTextureOffset(32, 76).addCuboid(-14.75F, -7.0F, -44.25F, 4.0F, 4.0F, 0.0F, 0.0F, false);
		headlights.setTextureOffset(46, 49).addCuboid(-13.25F, -39.25F, -44.25F, 3.0F, 3.0F, 0.0F, 0.0F, false);
		headlights.setTextureOffset(0, 61).addCuboid(10.25F, -39.25F, -44.25F, 3.0F, 3.0F, 0.0F, 0.0F, false);

		headlights_end = new ModelPart(this);
		headlights_end.setPivot(0.0F, 24.0F, 0.0F);
		headlights_end.setTextureOffset(24, 76).addCuboid(11.0F, -7.5F, -44.25F, 4.0F, 4.0F, 0.0F, 0.0F, false);
		headlights_end.setTextureOffset(16, 76).addCuboid(-15.0F, -7.5F, -44.25F, 4.0F, 4.0F, 0.0F, 0.0F, false);
		headlights_end.setTextureOffset(44, 22).addCuboid(-14.0F, -39.5F, -44.25F, 3.0F, 3.0F, 0.0F, 0.0F, false);
		headlights_end.setTextureOffset(38, 42).addCuboid(11.0F, -39.5F, -44.25F, 3.0F, 3.0F, 0.0F, 0.0F, false);

		tail_lights_end = new ModelPart(this);
		tail_lights_end.setPivot(0.0F, 24.0F, 0.0F);
		tail_lights_end.setTextureOffset(8, 76).addCuboid(-19.5F, -9.5F, -44.25F, 4.0F, 4.0F, 0.0F, 0.0F, false);
		tail_lights_end.setTextureOffset(0, 76).addCuboid(15.5F, -9.5F, -44.25F, 4.0F, 4.0F, 0.0F, 0.0F, false);
		tail_lights_end.setTextureOffset(32, 42).addCuboid(-17.5F, -39.0F, -44.25F, 3.0F, 3.0F, 0.0F, 0.0F, false);
		tail_lights_end.setTextureOffset(38, 22).addCuboid(14.5F, -39.0F, -44.25F, 3.0F, 3.0F, 0.0F, 0.0F, false);

		door_light = new ModelPart(this);
		door_light.setPivot(0.0F, 24.0F, 0.0F);
		door_light.setTextureOffset(32, 26).addCuboid(-19.25F, -38.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

		door_light_on = new ModelPart(this);
		door_light_on.setPivot(0.0F, 24.0F, 0.0F);
		door_light_on.setTextureOffset(44, 42).addCuboid(-19.26F, -38.5F, -0.5F, 0.0F, 1.0F, 1.0F, 0.0F, false);

		door_light_off = new ModelPart(this);
		door_light_off.setPivot(0.0F, 24.0F, 0.0F);
		door_light_off.setTextureOffset(44, 41).addCuboid(-19.26F, -38.5F, -0.5F, 0.0F, 1.0F, 1.0F, 0.0F, false);

		roof_window_exterior = new ModelPart(this);
		roof_window_exterior.setPivot(0.0F, 24.0F, 0.0F);
		

		top_roof_r1 = new ModelPart(this);
		top_roof_r1.setPivot(-0.5F, -8.55F, 3.0F);
		roof_window_exterior.addChild(top_roof_r1);
		setRotationAngle(top_roof_r1, 0.0F, 0.0F, 1.6144F);
		top_roof_r1.setTextureOffset(160, 184).addCuboid(-33.0F, -3.0F, -43.0F, 0.0F, 4.0F, 80.0F, 0.0F, false);
		top_roof_r1.setTextureOffset(0, 188).addCuboid(-33.0F, -7.0F, -43.0F, 0.0F, 4.0F, 80.0F, 0.0F, false);

		top_roof_r2 = new ModelPart(this);
		top_roof_r2.setPivot(1.0718F, -8.6875F, 3.0F);
		roof_window_exterior.addChild(top_roof_r2);
		setRotationAngle(top_roof_r2, 0.0F, 0.0F, 1.6581F);
		top_roof_r2.setTextureOffset(160, 188).addCuboid(-33.0F, -8.0F, -43.0F, 0.0F, 4.0F, 80.0F, 0.0F, false);

		top_slant_right_r1 = new ModelPart(this);
		top_slant_right_r1.setPivot(-14.9214F, -40.2953F, -8.0F);
		roof_window_exterior.addChild(top_slant_right_r1);
		setRotationAngle(top_slant_right_r1, 0.0F, 0.0F, 1.3875F);
		top_slant_right_r1.setTextureOffset(168, 177).addCuboid(0.0F, -3.5F, -32.0F, 0.0F, 7.0F, 80.0F, 0.0F, false);

		top_roof_r3 = new ModelPart(this);
		top_roof_r3.setPivot(-1.0718F, -8.6865F, 3.0F);
		roof_window_exterior.addChild(top_roof_r3);
		setRotationAngle(top_roof_r3, 0.0F, 0.0F, -1.6581F);
		top_roof_r3.setTextureOffset(0, 192).addCuboid(33.0F, -8.0F, -43.0F, 0.0F, 4.0F, 80.0F, 0.0F, false);

		top_roof_r4 = new ModelPart(this);
		top_roof_r4.setPivot(0.5F, -8.55F, 3.0F);
		roof_window_exterior.addChild(top_roof_r4);
		setRotationAngle(top_roof_r4, 0.0F, 0.0F, -1.6144F);
		top_roof_r4.setTextureOffset(160, 192).addCuboid(33.0F, -7.0F, -43.0F, 0.0F, 4.0F, 80.0F, 0.0F, false);
		top_roof_r4.setTextureOffset(0, 196).addCuboid(33.0F, -3.0F, -43.0F, 0.0F, 4.0F, 80.0F, 0.0F, false);

		top_slant_left_r1 = new ModelPart(this);
		top_slant_left_r1.setPivot(14.9214F, -40.2953F, -8.0F);
		roof_window_exterior.addChild(top_slant_left_r1);
		setRotationAngle(top_slant_left_r1, 0.0F, 0.0F, -1.3875F);
		top_slant_left_r1.setTextureOffset(0, 181).addCuboid(0.0F, -3.5F, -32.0F, 0.0F, 7.0F, 80.0F, 0.0F, false);

		roof_head_exterior = new ModelPart(this);
		roof_head_exterior.setPivot(0.0F, 24.0F, 0.0F);
		

		top_roof_r5 = new ModelPart(this);
		top_roof_r5.setPivot(-0.5F, -8.55F, 17.0F);
		roof_head_exterior.addChild(top_roof_r5);
		setRotationAngle(top_roof_r5, 0.0F, 0.0F, 1.6144F);
		top_roof_r5.setTextureOffset(94, 60).addCuboid(-33.0F, -3.0F, -61.0F, 0.0F, 4.0F, 84.0F, 0.0F, false);
		top_roof_r5.setTextureOffset(0, 165).addCuboid(-33.0F, -7.0F, -61.0F, 0.0F, 4.0F, 84.0F, 0.0F, false);

		top_roof_r6 = new ModelPart(this);
		top_roof_r6.setPivot(1.0718F, -8.6875F, 17.0F);
		roof_head_exterior.addChild(top_roof_r6);
		setRotationAngle(top_roof_r6, 0.0F, 0.0F, 1.6581F);
		top_roof_r6.setTextureOffset(168, 165).addCuboid(-33.0F, -8.0F, -61.0F, 0.0F, 4.0F, 84.0F, 0.0F, false);

		top_slant_right_r2 = new ModelPart(this);
		top_slant_right_r2.setPivot(-14.9214F, -40.2953F, 6.0F);
		roof_head_exterior.addChild(top_slant_right_r2);
		setRotationAngle(top_slant_right_r2, 0.0F, 0.0F, 1.3875F);
		top_slant_right_r2.setTextureOffset(94, 46).addCuboid(0.0F, -3.5F, -50.0F, 0.0F, 7.0F, 84.0F, 0.0F, false);

		top_roof_r7 = new ModelPart(this);
		top_roof_r7.setPivot(-1.0718F, -8.6865F, 17.0F);
		roof_head_exterior.addChild(top_roof_r7);
		setRotationAngle(top_roof_r7, 0.0F, 0.0F, -1.6581F);
		top_roof_r7.setTextureOffset(0, 169).addCuboid(33.0F, -8.0F, -61.0F, 0.0F, 4.0F, 84.0F, 0.0F, false);

		top_roof_r8 = new ModelPart(this);
		top_roof_r8.setPivot(0.5F, -8.55F, 17.0F);
		roof_head_exterior.addChild(top_roof_r8);
		setRotationAngle(top_roof_r8, 0.0F, 0.0F, -1.6144F);
		top_roof_r8.setTextureOffset(168, 169).addCuboid(33.0F, -7.0F, -61.0F, 0.0F, 4.0F, 84.0F, 0.0F, false);
		top_roof_r8.setTextureOffset(0, 173).addCuboid(33.0F, -3.0F, -61.0F, 0.0F, 4.0F, 84.0F, 0.0F, false);

		top_slant_left_r2 = new ModelPart(this);
		top_slant_left_r2.setPivot(14.9214F, -40.2953F, 6.0F);
		roof_head_exterior.addChild(top_slant_left_r2);
		setRotationAngle(top_slant_left_r2, 0.0F, 0.0F, -1.3875F);
		top_slant_left_r2.setTextureOffset(94, 53).addCuboid(0.0F, -3.5F, -50.0F, 0.0F, 7.0F, 84.0F, 0.0F, false);

		roof_end_exterior = new ModelPart(this);
		roof_end_exterior.setPivot(0.0F, 24.0F, 0.0F);
		

		top_roof_r9 = new ModelPart(this);
		top_roof_r9.setPivot(-0.5F, -8.55F, 17.0F);
		roof_end_exterior.addChild(top_roof_r9);
		setRotationAngle(top_roof_r9, 0.0F, 0.0F, 1.6144F);
		top_roof_r9.setTextureOffset(94, 60).addCuboid(-33.0F, -3.0F, -61.0F, 0.0F, 4.0F, 84.0F, 0.0F, false);
		top_roof_r9.setTextureOffset(0, 165).addCuboid(-33.0F, -7.0F, -61.0F, 0.0F, 4.0F, 84.0F, 0.0F, false);

		top_roof_r10 = new ModelPart(this);
		top_roof_r10.setPivot(1.0718F, -8.6875F, 17.0F);
		roof_end_exterior.addChild(top_roof_r10);
		setRotationAngle(top_roof_r10, 0.0F, 0.0F, 1.6581F);
		top_roof_r10.setTextureOffset(168, 165).addCuboid(-33.0F, -8.0F, -61.0F, 0.0F, 4.0F, 84.0F, 0.0F, false);

		top_slant_right_r3 = new ModelPart(this);
		top_slant_right_r3.setPivot(-14.9214F, -40.2953F, 6.0F);
		roof_end_exterior.addChild(top_slant_right_r3);
		setRotationAngle(top_slant_right_r3, 0.0F, 0.0F, 1.3875F);
		top_slant_right_r3.setTextureOffset(94, 46).addCuboid(0.0F, -3.5F, -50.0F, 0.0F, 7.0F, 84.0F, 0.0F, false);

		top_roof_r11 = new ModelPart(this);
		top_roof_r11.setPivot(-1.0718F, -8.6865F, 17.0F);
		roof_end_exterior.addChild(top_roof_r11);
		setRotationAngle(top_roof_r11, 0.0F, 0.0F, -1.6581F);
		top_roof_r11.setTextureOffset(0, 169).addCuboid(33.0F, -8.0F, -61.0F, 0.0F, 4.0F, 84.0F, 0.0F, false);

		top_roof_r12 = new ModelPart(this);
		top_roof_r12.setPivot(0.5F, -8.55F, 17.0F);
		roof_end_exterior.addChild(top_roof_r12);
		setRotationAngle(top_roof_r12, 0.0F, 0.0F, -1.6144F);
		top_roof_r12.setTextureOffset(168, 169).addCuboid(33.0F, -7.0F, -61.0F, 0.0F, 4.0F, 84.0F, 0.0F, false);
		top_roof_r12.setTextureOffset(0, 173).addCuboid(33.0F, -3.0F, -61.0F, 0.0F, 4.0F, 84.0F, 0.0F, false);

		top_slant_left_r3 = new ModelPart(this);
		top_slant_left_r3.setPivot(14.9214F, -40.2953F, 6.0F);
		roof_end_exterior.addChild(top_slant_left_r3);
		setRotationAngle(top_slant_left_r3, 0.0F, 0.0F, -1.3875F);
		top_slant_left_r3.setTextureOffset(94, 53).addCuboid(0.0F, -3.5F, -50.0F, 0.0F, 7.0F, 84.0F, 0.0F, false);

		roof_door_exterior = new ModelPart(this);
		roof_door_exterior.setPivot(0.0F, 24.0F, 0.0F);
		

		top_roof_r13 = new ModelPart(this);
		top_roof_r13.setPivot(-0.5F, -8.55F, 14.0F);
		roof_door_exterior.addChild(top_roof_r13);
		setRotationAngle(top_roof_r13, 0.0F, 0.0F, 1.6144F);
		top_roof_r13.setTextureOffset(160, 232).addCuboid(-33.0F, -3.0F, -30.0F, 0.0F, 4.0F, 32.0F, 0.0F, false);
		top_roof_r13.setTextureOffset(1, 236).addCuboid(-33.0F, -7.0F, -30.0F, 0.0F, 4.0F, 32.0F, 0.0F, false);

		top_roof_r14 = new ModelPart(this);
		top_roof_r14.setPivot(1.0718F, -8.6875F, 14.0F);
		roof_door_exterior.addChild(top_roof_r14);
		setRotationAngle(top_roof_r14, 0.0F, 0.0F, 1.6581F);
		top_roof_r14.setTextureOffset(160, 236).addCuboid(-33.0F, -8.0F, -30.0F, 0.0F, 4.0F, 32.0F, 0.0F, false);

		top_slant_right_r4 = new ModelPart(this);
		top_slant_right_r4.setPivot(-14.9214F, -40.2953F, 3.0F);
		roof_door_exterior.addChild(top_slant_right_r4);
		setRotationAngle(top_slant_right_r4, 0.0F, 0.0F, 1.3875F);
		top_slant_right_r4.setTextureOffset(168, 226).addCuboid(0.0F, -3.5F, -19.0F, 0.0F, 7.0F, 32.0F, 0.0F, false);

		top_roof_r15 = new ModelPart(this);
		top_roof_r15.setPivot(-1.0718F, -8.6865F, 14.0F);
		roof_door_exterior.addChild(top_roof_r15);
		setRotationAngle(top_roof_r15, 0.0F, 0.0F, -1.6581F);
		top_roof_r15.setTextureOffset(0, 236).addCuboid(33.0F, -8.0F, -30.0F, 0.0F, 4.0F, 32.0F, 0.0F, false);

		top_roof_r16 = new ModelPart(this);
		top_roof_r16.setPivot(0.5F, -8.55F, 14.0F);
		roof_door_exterior.addChild(top_roof_r16);
		setRotationAngle(top_roof_r16, 0.0F, 0.0F, -1.6144F);
		top_roof_r16.setTextureOffset(0, 236).addCuboid(33.0F, -7.0F, -30.0F, 0.0F, 4.0F, 32.0F, 0.0F, false);
		top_roof_r16.setTextureOffset(80, 244).addCuboid(33.0F, -3.0F, -30.0F, 0.0F, 4.0F, 32.0F, 0.0F, false);

		top_slant_left_r4 = new ModelPart(this);
		top_slant_left_r4.setPivot(14.9214F, -40.2953F, 3.0F);
		roof_door_exterior.addChild(top_slant_left_r4);
		setRotationAngle(top_slant_left_r4, 0.0F, 0.0F, -1.3875F);
		top_slant_left_r4.setTextureOffset(80, 229).addCuboid(0.0F, -3.5F, -19.0F, 0.0F, 7.0F, 32.0F, 0.0F, false);

		end_side_panels = new ModelPart(this);
		end_side_panels.setPivot(0.0F, 24.0F, 0.0F);
		end_side_panels.setTextureOffset(38, 3).addCuboid(11.0F, -25.0F, 12.5F, 9.0F, 19.0F, 0.0F, 0.0F, true);
		end_side_panels.setTextureOffset(38, 3).addCuboid(-20.0F, -25.0F, 12.5F, 9.0F, 19.0F, 0.0F, 0.0F, false);

		window_side_panels = new ModelPart(this);
		window_side_panels.setPivot(0.0F, 24.0F, 0.0F);
		window_side_panels.setTextureOffset(38, 3).addCuboid(11.0F, -25.0F, -44.5F, 9.0F, 19.0F, 0.0F, 0.0F, true);
		window_side_panels.setTextureOffset(38, 3).addCuboid(11.0F, -25.0F, 44.5F, 9.0F, 19.0F, 0.0F, 0.0F, true);
		window_side_panels.setTextureOffset(38, 3).addCuboid(-20.0F, -25.0F, 44.5F, 9.0F, 19.0F, 0.0F, 0.0F, false);

		head_side_panels = new ModelPart(this);
		head_side_panels.setPivot(0.0F, 24.0F, 0.0F);
		head_side_panels.setTextureOffset(38, 3).addCuboid(11.0F, -25.0F, 12.5F, 9.0F, 19.0F, 0.0F, 0.0F, true);
		head_side_panels.setTextureOffset(38, 2).addCuboid(-20.0F, -25.0F, 12.5F, 9.0F, 19.0F, 0.0F, 0.0F, false);

		seat = new ModelPart(this);
		seat.setPivot(0.0F, 24.0F, 0.0F);
		

		seat_structure = new ModelPart(this);
		seat_structure.setPivot(-0.0026F, 6.7792F, -0.6276F);
		seat.addChild(seat_structure);
		seat_structure.setTextureOffset(0, 53).addCuboid(-3.9974F, -24.5292F, 3.2476F, 8.0F, 7.0F, 1.0F, 0.0F, false);
		seat_structure.setTextureOffset(148, 202).addCuboid(-5.9974F, -17.7792F, 3.2476F, 12.0F, 5.0F, 1.0F, 0.0F, false);

		cube_r62 = new ModelPart(this);
		cube_r62.setPivot(-10.4324F, -18.0292F, 3.7551F);
		seat_structure.addChild(cube_r62);
		setRotationAngle(cube_r62, 0.0F, 0.0F, 0.0F);
		cube_r62.setTextureOffset(0, 64).addCuboid(5.0049F, -6.5F, -0.5076F, 2.0F, 1.0F, 1.0F, 0.0F, false);

		cube_r63 = new ModelPart(this);
		cube_r63.setPivot(-1.8074F, -18.8417F, 3.7551F);
		seat_structure.addChild(cube_r63);
		setRotationAngle(cube_r63, 0.0F, 0.0F, 0.096F);
		cube_r63.setTextureOffset(52, 89).addCuboid(-4.0701F, -4.5F, -0.5076F, 2.0F, 6.0F, 1.0F, 0.0F, false);

		cube_r64 = new ModelPart(this);
		cube_r64.setPivot(10.4375F, -18.0292F, 3.7551F);
		seat_structure.addChild(cube_r64);
		setRotationAngle(cube_r64, 0.0F, 0.0F, 0.0F);
		cube_r64.setTextureOffset(8, 65).addCuboid(-7.0049F, -6.5F, -0.5076F, 2.0F, 1.0F, 1.0F, 0.0F, false);

		cube_r65 = new ModelPart(this);
		cube_r65.setPivot(1.8125F, -18.8417F, 3.7551F);
		seat_structure.addChild(cube_r65);
		setRotationAngle(cube_r65, 0.0F, 0.0F, -0.096F);
		cube_r65.setTextureOffset(52, 96).addCuboid(2.0701F, -4.4975F, -0.5076F, 2.0F, 6.0F, 1.0F, 0.0F, false);

		cube_r66 = new ModelPart(this);
		cube_r66.setPivot(0.0F, 0.0F, 0.0F);
		seat_structure.addChild(cube_r66);
		setRotationAngle(cube_r66, -0.0873F, 0.0F, 0.0F);
		cube_r66.setTextureOffset(36, 156).addCuboid(-5.9974F, -14.0092F, -4.7924F, 12.0F, 1.0F, 7.0F, 0.0F, false);

		seat_handrails = new ModelPart(this);
		seat_handrails.setPivot(14.75F, 0.0F, 30.5F);
		seat.addChild(seat_handrails);
		

		cube_r67 = new ModelPart(this);
		cube_r67.setPivot(-19.5F, -18.8875F, -27.45F);
		seat_handrails.addChild(cube_r67);
		setRotationAngle(cube_r67, 0.0F, -1.5708F, 1.5708F);
		cube_r67.setTextureOffset(108, 319).addCuboid(0.2F, -9.7925F, -0.2F, 0.0F, 10.0F, 0.0F, 0.2F, false);

		cube_r68 = new ModelPart(this);
		cube_r68.setPivot(-9.3125F, -18.8875F, -27.45F);
		seat_handrails.addChild(cube_r68);
		setRotationAngle(cube_r68, 0.0F, -1.5708F, 1.5708F);
		cube_r68.setTextureOffset(107, 313).addCuboid(0.1925F, 0.2075F, -1.2F, 0.0F, 0.0F, 1.0F, 0.2F, false);

		cube_r69 = new ModelPart(this);
		cube_r69.setPivot(-19.75F, -18.8875F, -27.45F);
		seat_handrails.addChild(cube_r69);
		setRotationAngle(cube_r69, 0.0F, -1.5708F, 1.5708F);
		cube_r69.setTextureOffset(105, 318).addCuboid(0.2F, 0.2275F, -1.2F, 0.0F, 0.0F, 1.0F, 0.2F, false);

		seat_2 = new ModelPart(this);
		seat_2.setPivot(0.0F, 24.0F, 0.0F);
		setRotationAngle(seat_2, 0.0F, 0.0F, 0.0F);
		

		cube_r70 = new ModelPart(this);
		cube_r70.setPivot(-0.0026F, -12.5F, 3.7526F);
		seat_2.addChild(cube_r70);
		setRotationAngle(cube_r70, 0.0F, -1.5708F, 0.0F);
		cube_r70.setTextureOffset(148, 202).addCuboid(-9.7474F, 1.5F, 3.2424F, 12.0F, 5.0F, 1.0F, 0.0F, false);

		cube_r71 = new ModelPart(this);
		cube_r71.setPivot(-10.5026F, -11.375F, 2.8752F);
		seat_2.addChild(cube_r71);
		setRotationAngle(cube_r71, 0.0F, -1.5708F, 0.0F);
		cube_r71.setTextureOffset(0, 64).addCuboid(-8.2974F, -6.375F, -7.2502F, 2.0F, 1.0F, 1.0F, 0.0F, false);

		cube_r72 = new ModelPart(this);
		cube_r72.setPivot(-1.8099F, -12.0625F, 3.7526F);
		seat_2.addChild(cube_r72);
		setRotationAngle(cube_r72, -1.5708F, -1.4748F, 1.5708F);
		cube_r72.setTextureOffset(52, 89).addCuboid(-9.5901F, -3.9375F, 1.4324F, 2.0F, 6.0F, 1.0F, 0.0F, false);

		cube_r73 = new ModelPart(this);
		cube_r73.setPivot(10.4349F, -11.25F, 3.7526F);
		seat_2.addChild(cube_r73);
		setRotationAngle(cube_r73, 0.0F, -1.5708F, 0.0F);
		cube_r73.setTextureOffset(8, 65).addCuboid(-0.3249F, -6.5F, 13.6824F, 2.0F, 1.0F, 1.0F, 0.0F, false);

		cube_r74 = new ModelPart(this);
		cube_r74.setPivot(1.8099F, -12.0625F, 3.7526F);
		seat_2.addChild(cube_r74);
		setRotationAngle(cube_r74, 1.5708F, -1.4748F, -1.5708F);
		cube_r74.setTextureOffset(52, 96).addCuboid(0.1401F, -4.6875F, 5.0524F, 2.0F, 6.0F, 1.0F, 0.0F, false);

		cube_r75 = new ModelPart(this);
		cube_r75.setPivot(-0.0026F, -12.25F, 3.7526F);
		seat_2.addChild(cube_r75);
		setRotationAngle(cube_r75, 0.0F, -1.5708F, 0.0F);
		cube_r75.setTextureOffset(0, 53).addCuboid(-7.7474F, -5.5F, 3.2424F, 8.0F, 7.0F, 1.0F, 0.0F, false);

		cube_r76 = new ModelPart(this);
		cube_r76.setPivot(-0.0026F, 6.7792F, -0.0026F);
		seat_2.addChild(cube_r76);
		setRotationAngle(cube_r76, 0.0F, -1.5708F, -0.0873F);
		cube_r76.setTextureOffset(36, 156).addCuboid(-5.9974F, -14.0092F, -4.7924F, 12.0F, 1.0F, 7.0F, 0.0F, false);

		seat_3 = new ModelPart(this);
		seat_3.setPivot(0.0F, 24.0F, 0.0F);
		

		cube_r77 = new ModelPart(this);
		cube_r77.setPivot(-3.7526F, -12.5F, 3.0026F);
		seat_3.addChild(cube_r77);
		setRotationAngle(cube_r77, 0.0F, -1.5708F, 0.0F);
		cube_r77.setTextureOffset(130, 171).addCuboid(-12.0074F, 1.5F, -0.5076F, 18.0F, 5.0F, 1.0F, 0.0F, false);

		cube_r78 = new ModelPart(this);
		cube_r78.setPivot(-3.8302F, -15.5466F, -7.505F);
		seat_3.addChild(cube_r78);
		setRotationAngle(cube_r78, 0.0F, -1.5708F, 0.0F);
		cube_r78.setTextureOffset(8, 61).addCuboid(-0.9198F, -2.2034F, -0.59F, 2.0F, 1.0F, 1.0F, 0.0F, false);

		cube_r79 = new ModelPart(this);
		cube_r79.setPivot(-3.8302F, -15.5466F, -7.505F);
		seat_3.addChild(cube_r79);
		setRotationAngle(cube_r79, -1.5708F, -1.4748F, 1.5708F);
		cube_r79.setTextureOffset(74, 5).addCuboid(-1.0498F, -1.29F, -0.59F, 2.0F, 6.0F, 1.0F, 0.0F, false);

		cube_r80 = new ModelPart(this);
		cube_r80.setPivot(-3.6838F, -15.5477F, 7.62F);
		seat_3.addChild(cube_r80);
		setRotationAngle(cube_r80, 0.0F, -1.5708F, 0.0F);
		cube_r80.setTextureOffset(8, 63).addCuboid(-1.1862F, -2.2023F, -0.445F, 2.0F, 1.0F, 1.0F, 0.0F, false);

		cube_r81 = new ModelPart(this);
		cube_r81.setPivot(-3.6838F, -15.5477F, 7.62F);
		seat_3.addChild(cube_r81);
		setRotationAngle(cube_r81, 1.5708F, -1.4748F, -1.5708F);
		cube_r81.setTextureOffset(74, 12).addCuboid(-1.0662F, -1.2925F, -0.445F, 2.0F, 6.0F, 1.0F, 0.0F, false);

		cube_r82 = new ModelPart(this);
		cube_r82.setPivot(-3.7526F, -12.25F, 3.0026F);
		seat_3.addChild(cube_r82);
		setRotationAngle(cube_r82, 0.0F, -1.5708F, 0.0F);
		cube_r82.setTextureOffset(222, 200).addCuboid(-10.0074F, -5.5F, -0.5076F, 14.0F, 7.0F, 1.0F, 0.0F, false);

		cube_r83 = new ModelPart(this);
		cube_r83.setPivot(-0.0026F, 6.7792F, 2.9974F);
		seat_3.addChild(cube_r83);
		setRotationAngle(cube_r83, 0.0F, -1.5708F, -0.0873F);
		cube_r83.setTextureOffset(99, 82).addCuboid(-12.0074F, -14.0092F, -4.7924F, 18.0F, 1.0F, 7.0F, 0.0F, false);

		path_map = new ModelPart(this);
		path_map.setPivot(0.0F, 24.0F, 0.0F);
		

		map_path_r1 = new ModelPart(this);
		map_path_r1.setPivot(3.2878F, -12.5941F, -9.0F);
		path_map.addChild(map_path_r1);
		setRotationAngle(map_path_r1, 0.0F, 0.0F, 0.1309F);
		map_path_r1.setTextureOffset(0, 457).addCuboid(-22.9878F, -23.2559F, 1.0F, 0.0F, 5.0F, 16.0F, 0.0F, false);
}

private static final int DOOR_MAX = 12;

		@Override
		protected void renderWindowPositions(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, boolean isEnd1Head, boolean isEnd2Head) {
			final boolean frontWindow = isIndex(0, position, getWindowPositions());
			final boolean endWindow = isIndex(-1, position, getWindowPositions());

			switch (renderStage) {
				case LIGHTS:
					renderOnce(roof_window_light, matrices, vertices, light, position);
					break;
				case INTERIOR:
					renderOnceFlipped(seat, matrices, vertices, light, 14F, position + 3.6f);
					renderOnce(seat, matrices, vertices, light, -14F, position - 3.6f);
					renderOnceFlipped(seat, matrices, vertices, light, -14F, position + 3.6f);
					renderOnce(seat, matrices, vertices, light, 14F, position - 3.6f);
					renderOnceFlipped(seat, matrices, vertices, light, -14F, position + 19f);
					renderOnceFlipped(seat, matrices, vertices, light, 14F, position + 19f);
					renderOnce(seat, matrices, vertices, light, -14F, position - 19);
					renderOnce(seat, matrices, vertices, light, 14F, position - 19);
					renderMirror(path_map, matrices, vertices, light, position);

					if (frontWindow) {
						renderOnceFlipped(seat_2, matrices, vertices, light, -15.2F, position - 38);
						renderOnce(seat_2, matrices, vertices, light, -15.2F, position - 38);
						renderOnce(seat_2, matrices, vertices, light, -15.2F, position + 38);
					}
					if (endWindow) {
						renderOnceFlipped(seat_2, matrices, vertices, light, -15.2F, position + 38);
						renderOnce(seat_2, matrices, vertices, light, -15.2F, position + 38);
						renderOnceFlipped(seat_2, matrices, vertices, light, -15.2F, position - 38);
					}
					renderMirror(window, matrices, vertices, light, position);
					if (renderDetails) {
						renderOnce(roof_window, matrices, vertices, light, position);
						if (frontWindow) {
							renderOnceFlipped(window_seat_handrail, matrices, vertices, light, position);
						}
						if (endWindow) {
							renderOnce(window_seat_handrail, matrices, vertices, light, position);
						}
					}
					break;
				case INTERIOR_TRANSLUCENT:
					if (frontWindow) {
						renderOnceFlipped(window_side_panels, matrices, vertices, light, position);
					}
					if (endWindow) {
						renderOnce(window_side_panels, matrices, vertices, light, position);
					}
					break;
				case EXTERIOR:
					renderMirror(window_exterior, matrices, vertices, light, position);
					renderOnce(roof_window_exterior, matrices, vertices, light, position);
					break;
			}
		}

		@Override
		protected void renderDoorPositions(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
			final boolean middleDoor = isIndex(getDoorPositions().length / 2, position, getDoorPositions());
			final boolean doorOpen = doorLeftZ > 0 || doorRightZ > 0;
			final boolean middleRoof = isIndex(1, position, getDoorPositions());

			switch (renderStage) {
				case LIGHTS:
					renderMirror(roof_door_light, matrices, vertices, light, position);
					if (middleDoor && doorOpen) {
						renderMirror(door_light_on, matrices, vertices, light, position);
					}
						break;
				case INTERIOR:
					door_right.setPivot(doorRightX, 0, doorRightZ);
					door_left.setPivot(doorRightX, 0, -doorRightZ);
					renderOnce(door, matrices, vertices, light, position);
					door_right.setPivot(doorLeftX, 0, doorLeftZ);
					door_left.setPivot(doorLeftX, 0, -doorLeftZ);
					renderOnceFlipped(door, matrices, vertices, light, position);

					if (renderDetails) {
						renderMirror(roof_door, matrices, vertices, light, position);
					}
					break;
				case EXTERIOR:
					door_right_exterior.setPivot(doorRightX, 0, doorRightZ);
					door_left_exterior.setPivot(doorRightX, 0, -doorRightZ);
					renderOnce(door_exterior, matrices, vertices, light, position);
					door_right_exterior.setPivot(doorLeftX, 0, doorLeftZ);
					door_left_exterior.setPivot(doorLeftX, 0, -doorLeftZ);
					renderOnceFlipped(door_exterior, matrices, vertices, light, position);
					if (middleRoof)
					renderOnce(roof_door_exterior, matrices, vertices, light, position);
					if (middleDoor && renderDetails) {
						renderMirror(door_light, matrices, vertices, light, position);
						if (!doorOpen) {
							renderMirror(door_light_off, matrices, vertices, light, position);
						}
						break;
					}
			}
		}

		@Override
		protected void renderHeadPosition1(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, boolean useHeadlights) {
			switch (renderStage) {
				case LIGHTS:
					renderOnce(roof_head_light, matrices, vertices, light, position);
					break;
				case ALWAYS_ON_LIGHTS:
					renderOnce(useHeadlights ? headlights : tail_lights, matrices, vertices, light, position);
					break;
				case INTERIOR:
					renderOnce(seat_3, matrices, vertices, light, -15.2F, position + 3f);
					renderOnceFlipped(seat_3, matrices, vertices, light, -15.2F, position + 3f);
					renderOnce(head, matrices, vertices, light, position);
					if (renderDetails) {
						renderOnce(roof_head, matrices, vertices, light, position);
					}
					renderOnce(front_seat_handrail, matrices, vertices, light, position);
					break;
				case INTERIOR_TRANSLUCENT:
					renderOnce(head_side_panels, matrices, vertices, light, position);
					break;
				case EXTERIOR:
					renderOnce(head_exterior, matrices, vertices, light, position);
					renderOnce(roof_head_exterior, matrices, vertices, light, position);
					break;

			}
		}

		@Override
		protected void renderHeadPosition2(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, boolean useHeadlights) {
			switch (renderStage) {
				case LIGHTS:
					renderOnceFlipped(roof_head_light, matrices, vertices, light, position);
					break;
				case ALWAYS_ON_LIGHTS:
					renderOnceFlipped(useHeadlights ? headlights : tail_lights, matrices, vertices, light, position);
					break;
				case INTERIOR:
					renderOnceFlipped(seat_3, matrices, vertices, light, -15.2F, position - 3f);
					renderOnce(seat_3, matrices, vertices, light, -15.2F, position - 3f);
					renderOnceFlipped(head, matrices, vertices, light, position);
					if (renderDetails) {
						renderOnceFlipped(roof_head, matrices, vertices, light, position);
					}
					renderOnceFlipped(front_seat_handrail, matrices, vertices, light, position);
					break;
				case INTERIOR_TRANSLUCENT:
					renderOnceFlipped(head_side_panels, matrices, vertices, light, position);
					break;
				case EXTERIOR:
					renderOnceFlipped(head_exterior, matrices, vertices, light, position);
					renderOnceFlipped(roof_head_exterior, matrices, vertices, light, position);
					break;
			}
		}

		@Override
		protected void renderEndPosition1(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails) {
			switch (renderStage) {
				case LIGHTS:
					renderOnce(roof_end_light, matrices, vertices, light, position);
					break;
				case INTERIOR:
					renderOnceFlipped(seat, matrices, vertices, light, -14F, position - 29);
					renderOnceFlipped(seat, matrices, vertices, light, 14F, position - 29);
					renderOnceFlipped(seat, matrices, vertices, light, -14F, position - 13);
					renderOnceFlipped(seat, matrices, vertices, light, 14F, position - 13);

					renderOnceFlipped(seat_2, matrices, vertices, light, -15.2F, position + 6);
					renderOnce(seat_2, matrices, vertices, light, -15.2F, position + 6);
					renderOnce(end, matrices, vertices, light, position);
					if (renderDetails) {
						renderOnce(roof_end, matrices, vertices, light, position);
						renderOnce(end_seat_handrail, matrices, vertices, light, position);
					}
					break;
				case INTERIOR_TRANSLUCENT:
					renderOnce(end_side_panels, matrices, vertices, light, position);
					break;
				case EXTERIOR:
					renderOnce(end_exterior, matrices, vertices, light, position);
					renderOnce(roof_end_exterior, matrices, vertices, light, position);
					break;
			}
		}

		@Override
		protected void renderEndPosition2(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails) {
			switch (renderStage) {
				case LIGHTS:
					renderOnceFlipped(roof_end_light, matrices, vertices, light, position);
					break;
				case INTERIOR:
					renderOnce(seat, matrices, vertices, light, -14F, position + 29);
					renderOnce(seat, matrices, vertices, light, 14F, position + 29);
					renderOnce(seat, matrices, vertices, light, -14F, position + 13);
					renderOnce(seat, matrices, vertices, light, 14F, position + 13);

					renderOnceFlipped(seat_2, matrices, vertices, light, -15.2F, position - 6);
					renderOnce(seat_2, matrices, vertices, light, -15.2F, position - 6);

					renderOnceFlipped(end, matrices, vertices, light, position);
					if (renderDetails) {
						renderOnceFlipped(roof_end, matrices, vertices, light, position);
						renderOnceFlipped(end_seat_handrail, matrices, vertices, light, position);
					}
					break;
				case INTERIOR_TRANSLUCENT:
					renderOnceFlipped(end_side_panels, matrices, vertices, light, position);
					break;
				case EXTERIOR:
					renderOnceFlipped(end_exterior, matrices, vertices, light, position);
					renderOnceFlipped(roof_end_exterior, matrices, vertices, light, position);
					break;
			}
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
			return new int[]{-56, 56};
		}

		@Override
		protected int[] getDoorPositions() {
			return new int[]{-112, 0, 112};
		}

		@Override
		protected int[] getEndPositions() {
			return new int[]{-136, 136};
		}


		@Override
		protected float getDoorAnimationX(float value, boolean opening) {
			return 0;
		}


		@Override
		protected float getDoorAnimationZ(float value, boolean opening) {
			if (opening) {
				if (value > 0.4) {
					return smoothEnds(DOOR_MAX - 1, DOOR_MAX - 0.5F, 0.4F, 0.6F, value);
				} else {
					return smoothEnds(-DOOR_MAX + 1, DOOR_MAX - 1, -0.4F, 0.4F, value);
				}
			} else {
				if (value > 0.2) {
					return smoothEnds(1, DOOR_MAX - 0.5F, 0.2F, 0.6F, value);
				} else {
					return smoothEnds(-1.5F, 1.5F, -0.4F, 0.4F, value);
				}

			}

		}
	}