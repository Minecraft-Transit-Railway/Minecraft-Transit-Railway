	package mtr.model;

import net.minecraft.client.model.ModelPart;
	import net.minecraft.client.render.VertexConsumer;
	import net.minecraft.client.util.math.MatrixStack;

	public class ModelWmata7000Train extends ModelTrainBase {

private final ModelPart window;
	private final ModelPart walls_window;
	private final ModelPart wall_right_2_r1;
	private final ModelPart wall_left_1_r1;
	private final ModelPart window_bottom_side;
	private final ModelPart bottom_wall_3_right_r1;
	private final ModelPart bottom_wall_2_left_r1;
	private final ModelPart window_bottom_side2;
	private final ModelPart bottom_wall_2_right_r1;
	private final ModelPart bottom_wall_2_left_r2;
	private final ModelPart window_display;
	private final ModelPart window_exterior;
	private final ModelPart wall_right_2_r2;
	private final ModelPart wall_left_1_r2;
	private final ModelPart bottom_wall_right_r1;
	private final ModelPart bottom_wall_left_r1;
	private final ModelPart door;
	private final ModelPart door_top_2_r1;
	private final ModelPart door_right;
	private final ModelPart door_right_top_r1;
	private final ModelPart door_left;
	private final ModelPart door_left_top_r1;
	private final ModelPart door_exterior;
	private final ModelPart bottom_wall_right_r2;
	private final ModelPart door_right_exterior;
	private final ModelPart door_right_top_r2;
	private final ModelPart door_left_exterior;
	private final ModelPart door_left_top_r2;
	private final ModelPart door_sides;
	private final ModelPart door_side_top_1_r1;
	private final ModelPart end;
	private final ModelPart wall_right_2_r3;
	private final ModelPart wall_left_1_r3;
	private final ModelPart interor_back;
	private final ModelPart cab_door_back2;
	private final ModelPart wall_end_back_side_2_r1;
	private final ModelPart wall_end_back_side_1_r1;
	private final ModelPart wall_end_top_r1;
	private final ModelPart end_bottom;
	private final ModelPart bottom_wall_2_right_r2;
	private final ModelPart bottom_wall_2_left_r3;
	private final ModelPart end_exterior;
	private final ModelPart end_front_in_2_r1;
	private final ModelPart end_front_in_1_r1;
	private final ModelPart end_front_3_r1;
	private final ModelPart bottom_wall_right_r3;
	private final ModelPart bottom_wall_left_r2;
	private final ModelPart buttom_panel_right_6_r1;
	private final ModelPart buttom_panel_right_5_r1;
	private final ModelPart buttom_panel_6_r1;
	private final ModelPart buttom_panel_5_r1;
	private final ModelPart wall_left_1_r4;
	private final ModelPart wall_right_2_r4;
	private final ModelPart end_seat_handrail;
	private final ModelPart end_seat_right;
	private final ModelPart cube_r1;
	private final ModelPart end_seat_right2;
	private final ModelPart cube_r2;
	private final ModelPart end_seat_right3;
	private final ModelPart cube_r3;
	private final ModelPart end_seat_left;
	private final ModelPart cube_r4;
	private final ModelPart end_seat_left2;
	private final ModelPart cube_r5;
	private final ModelPart end_seat_left3;
	private final ModelPart cube_r6;
	private final ModelPart handrail_end_right;
	private final ModelPart cube_r7;
	private final ModelPart cube_r8;
	private final ModelPart cube_r9;
	private final ModelPart handrail_end_right2;
	private final ModelPart cube_r10;
	private final ModelPart handrail_end_right3;
	private final ModelPart cube_r11;
	private final ModelPart cube_r12;
	private final ModelPart cube_r13;
	private final ModelPart cube_r14;
	private final ModelPart handrail_end_left;
	private final ModelPart cube_r15;
	private final ModelPart cube_r16;
	private final ModelPart cube_r17;
	private final ModelPart handrail_end_left2;
	private final ModelPart cube_r18;
	private final ModelPart handrail_end_left3;
	private final ModelPart cube_r19;
	private final ModelPart cube_r20;
	private final ModelPart cube_r21;
	private final ModelPart cube_r22;
	private final ModelPart window_seat_handrail;
	private final ModelPart cube_r23;
	private final ModelPart top_handrails;
	private final ModelPart win_seat_left;
	private final ModelPart cube_r24;
	private final ModelPart win_seat_right;
	private final ModelPart cube_r25;
	private final ModelPart win_seat_left6;
	private final ModelPart cube_r26;
	private final ModelPart win_seat_left2;
	private final ModelPart cube_r27;
	private final ModelPart win_seat_left3;
	private final ModelPart cube_r28;
	private final ModelPart win_seat_left4;
	private final ModelPart cube_r29;
	private final ModelPart win_seat_right2;
	private final ModelPart cube_r30;
	private final ModelPart win_seat_right3;
	private final ModelPart cube_r31;
	private final ModelPart win_seat_right4;
	private final ModelPart cube_r32;
	private final ModelPart win_seat_right5;
	private final ModelPart cube_r33;
	private final ModelPart win_seat_left5;
	private final ModelPart cube_r34;
	private final ModelPart handrail_window_left7;
	private final ModelPart cube_r35;
	private final ModelPart cube_r36;
	private final ModelPart cube_r37;
	private final ModelPart cube_r38;
	private final ModelPart handrail_window_left6;
	private final ModelPart cube_r39;
	private final ModelPart cube_r40;
	private final ModelPart cube_r41;
	private final ModelPart cube_r42;
	private final ModelPart cube_r43;
	private final ModelPart handrail_window_left5;
	private final ModelPart cube_r44;
	private final ModelPart cube_r45;
	private final ModelPart handrail_window_left4;
	private final ModelPart cube_r46;
	private final ModelPart cube_r47;
	private final ModelPart handrail_window_left3;
	private final ModelPart cube_r48;
	private final ModelPart cube_r49;
	private final ModelPart handrail_window_left2;
	private final ModelPart cube_r50;
	private final ModelPart cube_r51;
	private final ModelPart cube_r52;
	private final ModelPart cube_r53;
	private final ModelPart cube_r54;
	private final ModelPart handrail_window_left1;
	private final ModelPart cube_r55;
	private final ModelPart cube_r56;
	private final ModelPart cube_r57;
	private final ModelPart cube_r58;
	private final ModelPart handrail_window_right5;
	private final ModelPart cube_r59;
	private final ModelPart cube_r60;
	private final ModelPart cube_r61;
	private final ModelPart handrail_window_right4;
	private final ModelPart cube_r62;
	private final ModelPart cube_r63;
	private final ModelPart handrail_window_right3;
	private final ModelPart cube_r64;
	private final ModelPart cube_r65;
	private final ModelPart handrail_window_right2;
	private final ModelPart cube_r66;
	private final ModelPart cube_r67;
	private final ModelPart cube_r68;
	private final ModelPart cube_r69;
	private final ModelPart handrail_window_right1;
	private final ModelPart cube_r70;
	private final ModelPart cube_r71;
	private final ModelPart cube_r72;
	private final ModelPart blue_floor;
	private final ModelPart front_seat_handrail;
	private final ModelPart handrail_head_left;
	private final ModelPart cube_r73;
	private final ModelPart cube_r74;
	private final ModelPart cube_r75;
	private final ModelPart handrail_head_right;
	private final ModelPart cube_r76;
	private final ModelPart cube_r77;
	private final ModelPart cube_r78;
	private final ModelPart seat_left;
	private final ModelPart cube_r79;
	private final ModelPart seat_right;
	private final ModelPart cube_r80;
	private final ModelPart roof_head;
	private final ModelPart roof_window;
	private final ModelPart roof_end;
	private final ModelPart roof_door;
	private final ModelPart head;
	private final ModelPart wall_right_2_r5;
	private final ModelPart wall_left_1_r5;
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
	private final ModelPart bottom_wall_right_r4;
	private final ModelPart bottom_wall_left_r3;
	private final ModelPart wall_right_2_r6;
	private final ModelPart wall_left_1_r6;
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
public ModelWmata7000Train() {
		textureWidth = 64;
		textureHeight = 64;
		window = new ModelPart(this);
		window.setPivot(0.0F, 24.0F, 0.0F);
		

		walls_window = new ModelPart(this);
		walls_window.setPivot(0.0F, -1.0F, -1.0F);
		window.addChild(walls_window);
		walls_window.setTextureOffset(98, 73).addCuboid(-21.0F, 1.0F, -44.0F, 42.0F, 1.0F, 90.0F, 0.0F, false);
		walls_window.setTextureOffset(0, 51).addCuboid(-22.15F, -9.9F, -49.0F, 2.0F, 11.0F, 100.0F, 0.0F, false);
		walls_window.setTextureOffset(0, 51).addCuboid(20.15F, -9.9F, -49.0F, 2.0F, 11.0F, 100.0F, 0.0F, true);

		wall_right_2_r1 = new ModelPart(this);
		wall_right_2_r1.setPivot(-3.2878F, -11.5941F, 0.0F);
		walls_window.addChild(wall_right_2_r1);
		setRotationAngle(wall_right_2_r1, 0.0F, 0.0F, -0.1309F);
		wall_right_2_r1.setTextureOffset(0, 51).addCuboid(22.9978F, -24.0059F, -49.0F, 2.0F, 29.0F, 100.0F, 0.0F, true);

		wall_left_1_r1 = new ModelPart(this);
		wall_left_1_r1.setPivot(3.2878F, -11.5941F, 0.0F);
		walls_window.addChild(wall_left_1_r1);
		setRotationAngle(wall_left_1_r1, 0.0F, 0.0F, 0.1309F);
		wall_left_1_r1.setTextureOffset(0, 51).addCuboid(-24.9978F, -24.0059F, -49.0F, 2.0F, 29.0F, 100.0F, 0.0F, false);

		window_bottom_side = new ModelPart(this);
		window_bottom_side.setPivot(0.0F, 0.0F, 40.0F);
		window.addChild(window_bottom_side);
		window_bottom_side.setTextureOffset(0, 0).addCuboid(18.0F, -4.0F, -4.0F, 3.0F, 4.0F, 14.0F, 0.0F, false);
		window_bottom_side.setTextureOffset(0, 0).addCuboid(-21.0F, -4.0F, -4.0F, 3.0F, 4.0F, 14.0F, 0.0F, true);

		bottom_wall_3_right_r1 = new ModelPart(this);
		bottom_wall_3_right_r1.setPivot(-19.75F, -4.25F, 3.5F);
		window_bottom_side.addChild(bottom_wall_3_right_r1);
		setRotationAngle(bottom_wall_3_right_r1, 0.0F, 0.0F, 0.6981F);
		bottom_wall_3_right_r1.setTextureOffset(0, 0).addCuboid(-1.5F, -1.0F, -7.5F, 3.0F, 2.0F, 14.0F, 0.0F, true);

		bottom_wall_2_left_r1 = new ModelPart(this);
		bottom_wall_2_left_r1.setPivot(19.75F, -4.25F, 3.5F);
		window_bottom_side.addChild(bottom_wall_2_left_r1);
		setRotationAngle(bottom_wall_2_left_r1, 0.0F, 0.0F, -0.6981F);
		bottom_wall_2_left_r1.setTextureOffset(0, 0).addCuboid(-1.5F, -1.0F, -7.5F, 3.0F, 2.0F, 14.0F, 0.0F, false);

		window_bottom_side2 = new ModelPart(this);
		window_bottom_side2.setPivot(0.0F, 0.0F, -40.0F);
		window.addChild(window_bottom_side2);
		window_bottom_side2.setTextureOffset(0, 0).addCuboid(18.0F, -4.0F, -10.0F, 3.0F, 4.0F, 14.0F, 0.0F, false);
		window_bottom_side2.setTextureOffset(0, 0).addCuboid(-21.0F, -4.0F, -10.0F, 3.0F, 4.0F, 14.0F, 0.0F, true);

		bottom_wall_2_right_r1 = new ModelPart(this);
		bottom_wall_2_right_r1.setPivot(-19.75F, -4.25F, -3.5F);
		window_bottom_side2.addChild(bottom_wall_2_right_r1);
		setRotationAngle(bottom_wall_2_right_r1, 0.0F, 0.0F, 0.6981F);
		bottom_wall_2_right_r1.setTextureOffset(0, 0).addCuboid(-1.5F, -1.0F, -6.5F, 3.0F, 2.0F, 14.0F, 0.0F, true);

		bottom_wall_2_left_r2 = new ModelPart(this);
		bottom_wall_2_left_r2.setPivot(19.75F, -4.25F, -3.5F);
		window_bottom_side2.addChild(bottom_wall_2_left_r2);
		setRotationAngle(bottom_wall_2_left_r2, 0.0F, 0.0F, -0.6981F);
		bottom_wall_2_left_r2.setTextureOffset(0, 0).addCuboid(-1.5F, -1.0F, -6.5F, 3.0F, 2.0F, 14.0F, 0.0F, false);

		window_display = new ModelPart(this);
		window_display.setPivot(0.0F, 24.0F, 0.0F);
		

		window_exterior = new ModelPart(this);
		window_exterior.setPivot(0.0F, 24.0F, 0.0F);
		window_exterior.setTextureOffset(121, 115).addCuboid(20.75F, 0.0F, -45.0F, 1.0F, 3.0F, 90.0F, 0.0F, true);
		window_exterior.setTextureOffset(121, 115).addCuboid(-21.75F, 0.0F, -45.0F, 1.0F, 3.0F, 90.0F, 0.0F, false);
		window_exterior.setTextureOffset(0, 51).addCuboid(-22.151F, -10.9F, -50.0F, 2.0F, 11.0F, 100.0F, 0.0F, false);
		window_exterior.setTextureOffset(0, 51).addCuboid(20.151F, -10.9F, -50.0F, 2.0F, 11.0F, 100.0F, 0.0F, true);

		wall_right_2_r2 = new ModelPart(this);
		wall_right_2_r2.setPivot(-3.2878F, -12.5941F, -1.0F);
		window_exterior.addChild(wall_right_2_r2);
		setRotationAngle(wall_right_2_r2, 0.0F, 0.0F, -0.1309F);
		wall_right_2_r2.setTextureOffset(0, 51).addCuboid(23.0F, -24.0F, -49.0F, 2.0F, 29.0F, 100.0F, 0.0F, true);

		wall_left_1_r2 = new ModelPart(this);
		wall_left_1_r2.setPivot(3.2878F, -12.5941F, -1.0F);
		window_exterior.addChild(wall_left_1_r2);
		setRotationAngle(wall_left_1_r2, 0.0F, 0.0F, 0.1309F);
		wall_left_1_r2.setTextureOffset(0, 51).addCuboid(-25.0F, -24.0F, -49.0F, 2.0F, 29.0F, 100.0F, 0.0F, false);

		bottom_wall_right_r1 = new ModelPart(this);
		bottom_wall_right_r1.setPivot(0.4068F, 4.1977F, -12.0F);
		window_exterior.addChild(bottom_wall_right_r1);
		setRotationAngle(bottom_wall_right_r1, -0.1309F, 1.5708F, 0.0F);
		bottom_wall_right_r1.setTextureOffset(0, 51).addCuboid(-57.0068F, -6.9977F, 21.0F, 90.0F, 3.0F, 0.0F, 0.0F, true);

		bottom_wall_left_r1 = new ModelPart(this);
		bottom_wall_left_r1.setPivot(-0.4068F, 4.1977F, -12.0F);
		window_exterior.addChild(bottom_wall_left_r1);
		setRotationAngle(bottom_wall_left_r1, -0.1309F, -1.5708F, 0.0F);
		bottom_wall_left_r1.setTextureOffset(0, 51).addCuboid(-32.9932F, -6.9977F, 21.0F, 90.0F, 3.0F, 0.0F, 0.0F, false);

		door = new ModelPart(this);
		door.setPivot(0.0F, 24.0F, 0.0F);
		door.setTextureOffset(235, 149).addCuboid(-17.875F, -36.0F, -11.5F, 1.0F, 0.0F, 23.0F, 0.0F, false);
		door.setTextureOffset(0, 0).addCuboid(-21.5F, 0.0F, -16.0F, 22.0F, 1.0F, 32.0F, 0.0F, false);

		door_top_2_r1 = new ModelPart(this);
		door_top_2_r1.setPivot(-19.4875F, -16.125F, 0.25F);
		door.addChild(door_top_2_r1);
		setRotationAngle(door_top_2_r1, 0.0F, 0.0F, 0.1309F);
		door_top_2_r1.setTextureOffset(2, 30).addCuboid(0.0F, -24.0F, -11.5F, 0.0F, 4.0F, 23.0F, 0.0F, false);

		door_right = new ModelPart(this);
		door_right.setPivot(0.0F, 0.0F, 0.0F);
		door.addChild(door_right);
		door_right.setTextureOffset(181, 178).addCuboid(-21.25F, -11.0F, 0.0F, 0.0F, 11.0F, 12.0F, 0.0F, false);

		door_right_top_r1 = new ModelPart(this);
		door_right_top_r1.setPivot(0.0F, 0.0F, 0.0F);
		door_right.addChild(door_right_top_r1);
		setRotationAngle(door_right_top_r1, 0.0F, 0.0F, 0.1309F);
		door_right_top_r1.setTextureOffset(84, 159).addCuboid(-22.5F, -34.13F, 0.0F, 0.0F, 26.0F, 12.0F, 0.0F, false);

		door_left = new ModelPart(this);
		door_left.setPivot(0.0F, 0.0F, 0.0F);
		door.addChild(door_left);
		door_left.setTextureOffset(191, 123).addCuboid(-21.25F, -11.0F, -12.0F, 0.0F, 11.0F, 12.0F, 0.0F, false);

		door_left_top_r1 = new ModelPart(this);
		door_left_top_r1.setPivot(0.0F, 0.0F, 0.0F);
		door_left.addChild(door_left_top_r1);
		setRotationAngle(door_left_top_r1, 0.0F, 0.0F, 0.1309F);
		door_left_top_r1.setTextureOffset(0, 136).addCuboid(-22.5F, -34.13F, -12.0F, 0.0F, 26.0F, 12.0F, 0.0F, false);

		door_exterior = new ModelPart(this);
		door_exterior.setPivot(0.0F, 24.0F, 0.0F);
		

		bottom_wall_right_r2 = new ModelPart(this);
		bottom_wall_right_r2.setPivot(-0.4068F, 4.1977F, -13.0F);
		door_exterior.addChild(bottom_wall_right_r2);
		setRotationAngle(bottom_wall_right_r2, -0.1309F, -1.5708F, 0.0F);
		bottom_wall_right_r2.setTextureOffset(0, 51).addCuboid(-3.0F, -7.0F, 21.0F, 32.0F, 3.0F, 0.0F, 0.0F, false);

		door_right_exterior = new ModelPart(this);
		door_right_exterior.setPivot(0.0F, 0.0F, 0.0F);
		door_exterior.addChild(door_right_exterior);
		door_right_exterior.setTextureOffset(191, 123).addCuboid(-21.5F, -11.0F, 0.0F, 0.0F, 11.0F, 12.0F, 0.0F, false);

		door_right_top_r2 = new ModelPart(this);
		door_right_top_r2.setPivot(0.0F, 0.0F, 0.0F);
		door_right_exterior.addChild(door_right_top_r2);
		setRotationAngle(door_right_top_r2, 0.0F, 0.0F, 0.1309F);
		door_right_top_r2.setTextureOffset(84, 159).addCuboid(-22.754F, -34.098F, 0.0F, 0.0F, 26.0F, 12.0F, 0.0F, false);

		door_left_exterior = new ModelPart(this);
		door_left_exterior.setPivot(0.0F, 0.0F, 0.0F);
		door_exterior.addChild(door_left_exterior);
		door_left_exterior.setTextureOffset(181, 178).addCuboid(-21.5101F, -11.0171F, -12.0F, 0.0F, 11.0F, 12.0F, 0.0F, false);

		door_left_top_r2 = new ModelPart(this);
		door_left_top_r2.setPivot(0.0F, 0.0F, 0.0F);
		door_left_exterior.addChild(door_left_top_r2);
		setRotationAngle(door_left_top_r2, 0.0F, 0.0F, 0.1309F);
		door_left_top_r2.setTextureOffset(0, 136).addCuboid(-22.765F, -34.1151F, -12.0F, 0.0F, 26.0F, 12.0F, 0.0F, false);

		door_sides = new ModelPart(this);
		door_sides.setPivot(0.0F, 0.0F, 0.0F);
		door_exterior.addChild(door_sides);
		door_sides.setTextureOffset(0, 51).addCuboid(-21.75F, 1.0F, -16.0F, 1.0F, 2.0F, 32.0F, 0.0F, false);
		door_sides.setTextureOffset(42, 137).addCuboid(-22.5F, 0.0F, -11.0F, 1.0F, 1.0F, 22.0F, 0.0F, false);
		door_sides.setTextureOffset(235, 149).addCuboid(-18.875F, -36.0F, -11.5F, 1.0F, 0.0F, 23.0F, 0.0F, false);

		door_side_top_1_r1 = new ModelPart(this);
		door_side_top_1_r1.setPivot(-21.425F, -16.125F, 0.25F);
		door_sides.addChild(door_side_top_1_r1);
		setRotationAngle(door_side_top_1_r1, 0.0F, 0.0F, 0.1309F);
		door_side_top_1_r1.setTextureOffset(2, 30).addCuboid(0.0F, -24.0F, -11.5F, 0.0F, 4.0F, 23.0F, 0.0F, false);

		end = new ModelPart(this);
		end.setPivot(0.0F, 24.0F, 0.0F);
		end.setTextureOffset(0, 60).addCuboid(-20.5F, 0.0F, -41.0F, 41.0F, 1.0F, 49.0F, 0.0F, false);
		end.setTextureOffset(0, 51).addCuboid(-22.15F, -10.9F, -43.0F, 2.0F, 11.0F, 56.0F, 0.0F, false);
		end.setTextureOffset(0, 51).addCuboid(20.15F, -10.9F, -43.0F, 2.0F, 11.0F, 56.0F, 0.0F, true);

		wall_right_2_r3 = new ModelPart(this);
		wall_right_2_r3.setPivot(-3.2878F, -12.5941F, -5.0F);
		end.addChild(wall_right_2_r3);
		setRotationAngle(wall_right_2_r3, 0.0F, 0.0F, -0.1309F);
		wall_right_2_r3.setTextureOffset(0, 51).addCuboid(22.9978F, -24.0059F, -38.0F, 2.0F, 29.0F, 56.0F, 0.0F, true);

		wall_left_1_r3 = new ModelPart(this);
		wall_left_1_r3.setPivot(3.2878F, -12.5941F, -5.0F);
		end.addChild(wall_left_1_r3);
		setRotationAngle(wall_left_1_r3, 0.0F, 0.0F, 0.1309F);
		wall_left_1_r3.setTextureOffset(0, 51).addCuboid(-24.9978F, -24.0059F, -38.0F, 2.0F, 29.0F, 56.0F, 0.0F, false);

		interor_back = new ModelPart(this);
		interor_back.setPivot(0.0F, 0.0F, 0.0F);
		end.addChild(interor_back);
		

		cab_door_back2 = new ModelPart(this);
		cab_door_back2.setPivot(0.0F, 0.0F, -36.0F);
		interor_back.addChild(cab_door_back2);
		setRotationAngle(cab_door_back2, -3.1416F, 0.0F, 3.1416F);
		cab_door_back2.setTextureOffset(121, 115).addCuboid(-7.0F, -36.0F, 4.5F, 14.0F, 36.0F, 0.0F, 0.0F, false);
		cab_door_back2.setTextureOffset(145, 115).addCuboid(-22.0F, -36.0F, 3.0F, 15.0F, 36.0F, 0.0F, 0.0F, false);
		cab_door_back2.setTextureOffset(145, 115).addCuboid(-21.0F, -13.0F, -3.0F, 14.0F, 13.0F, 6.0F, 0.0F, false);
		cab_door_back2.setTextureOffset(145, 115).addCuboid(7.0F, -13.0F, -3.0F, 14.0F, 13.0F, 6.0F, 0.0F, true);
		cab_door_back2.setTextureOffset(145, 115).addCuboid(7.0F, -36.0F, 3.0F, 15.0F, 36.0F, 0.0F, 0.0F, false);
		cab_door_back2.setTextureOffset(145, 115).addCuboid(-22.0F, -39.0F, 3.0F, 44.0F, 3.0F, 0.0F, 0.0F, false);

		wall_end_back_side_2_r1 = new ModelPart(this);
		wall_end_back_side_2_r1.setPivot(1.0F, 0.0F, 0.0F);
		cab_door_back2.addChild(wall_end_back_side_2_r1);
		setRotationAngle(wall_end_back_side_2_r1, 0.0F, -1.5708F, 0.0F);
		wall_end_back_side_2_r1.setTextureOffset(121, 115).addCuboid(3.0F, -36.0F, -6.0F, 2.0F, 36.0F, 0.0F, 0.0F, false);

		wall_end_back_side_1_r1 = new ModelPart(this);
		wall_end_back_side_1_r1.setPivot(-1.0F, 0.0F, 0.0F);
		cab_door_back2.addChild(wall_end_back_side_1_r1);
		setRotationAngle(wall_end_back_side_1_r1, 0.0F, -1.5708F, 0.0F);
		wall_end_back_side_1_r1.setTextureOffset(121, 115).addCuboid(3.0F, -36.0F, 6.0F, 2.0F, 36.0F, 0.0F, 0.0F, false);

		wall_end_top_r1 = new ModelPart(this);
		wall_end_top_r1.setPivot(0.0F, -58.0F, 47.0F);
		cab_door_back2.addChild(wall_end_top_r1);
		setRotationAngle(wall_end_top_r1, 1.5708F, 0.0F, 0.0F);
		wall_end_top_r1.setTextureOffset(0, 33).addCuboid(-7.0F, -44.0F, -22.0F, 14.0F, 2.0F, 0.0F, 0.0F, false);

		end_bottom = new ModelPart(this);
		end_bottom.setPivot(0.0F, 0.0F, 23.0F);
		end.addChild(end_bottom);
		end_bottom.setTextureOffset(0, 0).addCuboid(18.0F, -4.0F, -24.0F, 3.0F, 4.0F, 14.0F, 0.0F, false);
		end_bottom.setTextureOffset(0, 0).addCuboid(-21.0F, -4.0F, -24.0F, 3.0F, 4.0F, 14.0F, 0.0F, true);

		bottom_wall_2_right_r2 = new ModelPart(this);
		bottom_wall_2_right_r2.setPivot(-19.75F, -4.25F, -16.5F);
		end_bottom.addChild(bottom_wall_2_right_r2);
		setRotationAngle(bottom_wall_2_right_r2, 0.0F, 0.0F, 0.6981F);
		bottom_wall_2_right_r2.setTextureOffset(0, 0).addCuboid(-1.5F, -1.0F, -7.5F, 3.0F, 2.0F, 14.0F, 0.0F, true);

		bottom_wall_2_left_r3 = new ModelPart(this);
		bottom_wall_2_left_r3.setPivot(19.75F, -4.25F, -16.5F);
		end_bottom.addChild(bottom_wall_2_left_r3);
		setRotationAngle(bottom_wall_2_left_r3, 0.0F, 0.0F, -0.6981F);
		bottom_wall_2_left_r3.setTextureOffset(0, 0).addCuboid(-1.5F, -1.0F, -7.5F, 3.0F, 2.0F, 14.0F, 0.0F, false);

		end_exterior = new ModelPart(this);
		end_exterior.setPivot(0.0F, 24.0F, 0.0F);
		end_exterior.setTextureOffset(0, 51).addCuboid(-22.15F, -10.9F, -43.0F, 2.0F, 11.0F, 56.0F, 0.0F, false);
		end_exterior.setTextureOffset(0, 51).addCuboid(20.15F, -10.9F, -43.0F, 2.0F, 11.0F, 56.0F, 0.0F, true);
		end_exterior.setTextureOffset(114, 0).addCuboid(-6.5F, 0.0F, -46.0F, 13.0F, 3.0F, 7.0F, 0.0F, false);
		end_exterior.setTextureOffset(114, 0).addCuboid(-21.5F, 0.0F, -43.0F, 7.0F, 3.0F, 6.0F, 0.0F, false);
		end_exterior.setTextureOffset(114, 0).addCuboid(14.5F, 0.0F, -43.0F, 7.0F, 3.0F, 6.0F, 0.0F, false);
		end_exterior.setTextureOffset(39, 91).addCuboid(20.75F, 0.0F, -37.0F, 1.0F, 3.0F, 45.0F, 0.0F, false);
		end_exterior.setTextureOffset(0, 124).addCuboid(-21.75F, 0.0F, -43.0F, 1.0F, 3.0F, 51.0F, 0.0F, false);
		end_exterior.setTextureOffset(145, 115).addCuboid(-22.0F, -42.0F, -43.0F, 15.0F, 42.0F, 0.0F, 0.0F, false);
		end_exterior.setTextureOffset(0, 33).addCuboid(-7.0F, -42.0F, -43.0F, 14.0F, 6.0F, 0.0F, 0.0F, false);
		end_exterior.setTextureOffset(137, 115).addCuboid(7.0F, -42.0F, -43.0F, 15.0F, 42.0F, 0.0F, 0.0F, false);
		end_exterior.setTextureOffset(121, 115).addCuboid(-7.0F, -36.0F, -41.0F, 14.0F, 36.0F, 0.0F, 0.0F, false);

		end_front_in_2_r1 = new ModelPart(this);
		end_front_in_2_r1.setPivot(1.0F, 0.0F, -20.0F);
		end_exterior.addChild(end_front_in_2_r1);
		setRotationAngle(end_front_in_2_r1, 0.0F, -1.5708F, 0.0F);
		end_front_in_2_r1.setTextureOffset(121, 115).addCuboid(-23.0F, -36.0F, -6.0F, 2.0F, 36.0F, 0.0F, 0.0F, false);

		end_front_in_1_r1 = new ModelPart(this);
		end_front_in_1_r1.setPivot(-1.0F, 0.0F, -20.0F);
		end_exterior.addChild(end_front_in_1_r1);
		setRotationAngle(end_front_in_1_r1, 0.0F, -1.5708F, 0.0F);
		end_front_in_1_r1.setTextureOffset(121, 115).addCuboid(-23.0F, -36.0F, 6.0F, 2.0F, 36.0F, 0.0F, 0.0F, false);

		end_front_3_r1 = new ModelPart(this);
		end_front_3_r1.setPivot(0.0F, -58.0F, -9.0F);
		end_exterior.addChild(end_front_3_r1);
		setRotationAngle(end_front_3_r1, 1.5708F, 0.0F, 0.0F);
		end_front_3_r1.setTextureOffset(0, 33).addCuboid(-7.0F, -34.0F, -22.0F, 14.0F, 2.0F, 0.0F, 0.0F, false);

		bottom_wall_right_r3 = new ModelPart(this);
		bottom_wall_right_r3.setPivot(0.4068F, 4.1977F, -20.0F);
		end_exterior.addChild(bottom_wall_right_r3);
		setRotationAngle(bottom_wall_right_r3, -0.1309F, 1.5708F, 0.0F);
		bottom_wall_right_r3.setTextureOffset(0, 51).addCuboid(-28.0068F, -7.0007F, 21.0F, 51.0F, 3.0F, 0.0F, 0.0F, true);

		bottom_wall_left_r2 = new ModelPart(this);
		bottom_wall_left_r2.setPivot(-0.4068F, 4.1977F, -20.0F);
		end_exterior.addChild(bottom_wall_left_r2);
		setRotationAngle(bottom_wall_left_r2, -0.1309F, -1.5708F, 0.0F);
		bottom_wall_left_r2.setTextureOffset(0, 51).addCuboid(-22.9932F, -7.0007F, 21.0F, 51.0F, 3.0F, 0.0F, 0.0F, false);

		buttom_panel_right_6_r1 = new ModelPart(this);
		buttom_panel_right_6_r1.setPivot(9.7131F, 1.5F, -43.8861F);
		end_exterior.addChild(buttom_panel_right_6_r1);
		setRotationAngle(buttom_panel_right_6_r1, 0.0F, -0.1745F, 0.0F);
		buttom_panel_right_6_r1.setTextureOffset(0, 43).addCuboid(-3.5031F, -1.5F, -0.4939F, 10.0F, 3.0F, 6.0F, 0.0F, true);

		buttom_panel_right_5_r1 = new ModelPart(this);
		buttom_panel_right_5_r1.setPivot(-9.7131F, 1.5F, -43.8861F);
		end_exterior.addChild(buttom_panel_right_5_r1);
		setRotationAngle(buttom_panel_right_5_r1, 0.0F, 0.1745F, 0.0F);
		buttom_panel_right_5_r1.setTextureOffset(0, 43).addCuboid(-6.4969F, -1.5F, -0.4939F, 10.0F, 3.0F, 6.0F, 0.0F, false);

		buttom_panel_6_r1 = new ModelPart(this);
		buttom_panel_6_r1.setPivot(-21.5625F, 4.1F, -36.0F);
		end_exterior.addChild(buttom_panel_6_r1);
		setRotationAngle(buttom_panel_6_r1, 0.0F, 0.0F, -0.1309F);
		buttom_panel_6_r1.setTextureOffset(114, 0).addCuboid(0.0025F, -4.1F, -7.0F, 1.0F, 3.0F, 6.0F, 0.0F, true);

		buttom_panel_5_r1 = new ModelPart(this);
		buttom_panel_5_r1.setPivot(21.5625F, 4.1F, -36.0F);
		end_exterior.addChild(buttom_panel_5_r1);
		setRotationAngle(buttom_panel_5_r1, 0.0F, 0.0F, 0.1309F);
		buttom_panel_5_r1.setTextureOffset(114, 0).addCuboid(-1.0025F, -4.1F, -7.0F, 1.0F, 3.0F, 6.0F, 0.0F, false);

		wall_left_1_r4 = new ModelPart(this);
		wall_left_1_r4.setPivot(3.2878F, -12.5941F, -5.0F);
		end_exterior.addChild(wall_left_1_r4);
		setRotationAngle(wall_left_1_r4, 0.0F, 0.0F, 0.1309F);
		wall_left_1_r4.setTextureOffset(0, 51).addCuboid(-24.9978F, -24.0059F, -38.0F, 2.0F, 29.0F, 56.0F, 0.0F, false);

		wall_right_2_r4 = new ModelPart(this);
		wall_right_2_r4.setPivot(-3.2878F, -12.5941F, -5.0F);
		end_exterior.addChild(wall_right_2_r4);
		setRotationAngle(wall_right_2_r4, 0.0F, 0.0F, -0.1309F);
		wall_right_2_r4.setTextureOffset(0, 51).addCuboid(22.9978F, -24.0059F, -38.0F, 2.0F, 29.0F, 56.0F, 0.0F, true);

		end_seat_handrail = new ModelPart(this);
		end_seat_handrail.setPivot(0.0F, 24.0F, 0.0F);
		

		end_seat_right = new ModelPart(this);
		end_seat_right.setPivot(0.25F, -1.75F, -1.0F);
		end_seat_handrail.addChild(end_seat_right);
		end_seat_right.setTextureOffset(0, 0).addCuboid(-19.75F, -17.25F, 1.0F, 1.0F, 13.0F, 12.0F, 0.0F, true);

		cube_r1 = new ModelPart(this);
		cube_r1.setPivot(1.5F, 0.0F, -16.0F);
		end_seat_right.addChild(cube_r1);
		setRotationAngle(cube_r1, 0.0F, 0.0F, -0.0873F);
		cube_r1.setTextureOffset(0, 0).addCuboid(-20.0F, -7.0F, 17.0F, 7.0F, 1.0F, 12.0F, 0.0F, true);

		end_seat_right2 = new ModelPart(this);
		end_seat_right2.setPivot(-36.25F, -1.75F, 22.0F);
		end_seat_handrail.addChild(end_seat_right2);
		setRotationAngle(end_seat_right2, 0.0F, 1.5708F, 0.0F);
		end_seat_right2.setTextureOffset(0, 0).addCuboid(37.75F, -17.25F, 16.0F, 1.0F, 13.0F, 12.0F, 0.0F, false);

		cube_r2 = new ModelPart(this);
		cube_r2.setPivot(-0.5F, 0.0F, 0.0F);
		end_seat_right2.addChild(cube_r2);
		setRotationAngle(cube_r2, 0.0F, 0.0F, 0.0873F);
		cube_r2.setTextureOffset(0, 0).addCuboid(30.93F, -8.57F, 16.0F, 7.0F, 1.0F, 12.0F, 0.0F, false);

		end_seat_right3 = new ModelPart(this);
		end_seat_right3.setPivot(-36.25F, -1.75F, 8.0F);
		end_seat_handrail.addChild(end_seat_right3);
		setRotationAngle(end_seat_right3, 0.0F, 1.5708F, 0.0F);
		end_seat_right3.setTextureOffset(0, 0).addCuboid(39.75F, -17.25F, 16.0F, 1.0F, 13.0F, 12.0F, 0.0F, false);

		cube_r3 = new ModelPart(this);
		cube_r3.setPivot(-0.5F, 0.0F, 0.0F);
		end_seat_right3.addChild(cube_r3);
		setRotationAngle(cube_r3, 0.0F, 0.0F, 0.0873F);
		cube_r3.setTextureOffset(0, 0).addCuboid(32.92F, -8.74F, 16.0F, 7.0F, 1.0F, 12.0F, 0.0F, false);

		end_seat_left = new ModelPart(this);
		end_seat_left.setPivot(0.75F, -1.75F, 4.0F);
		end_seat_handrail.addChild(end_seat_left);
		end_seat_left.setTextureOffset(0, 0).addCuboid(17.75F, -17.25F, -4.0F, 1.0F, 13.0F, 12.0F, 0.0F, false);

		cube_r4 = new ModelPart(this);
		cube_r4.setPivot(-2.5F, 0.0F, -20.0F);
		end_seat_left.addChild(cube_r4);
		setRotationAngle(cube_r4, 0.0F, 0.0F, 0.0873F);
		cube_r4.setTextureOffset(0, 0).addCuboid(13.0F, -7.0F, 16.0F, 7.0F, 1.0F, 12.0F, 0.0F, false);

		end_seat_left2 = new ModelPart(this);
		end_seat_left2.setPivot(-8.25F, -1.75F, 24.0F);
		end_seat_handrail.addChild(end_seat_left2);
		setRotationAngle(end_seat_left2, 0.0F, 1.5708F, 0.0F);
		end_seat_left2.setTextureOffset(0, 0).addCuboid(39.75F, -17.25F, 17.0F, 1.0F, 13.0F, 12.0F, 0.0F, false);

		cube_r5 = new ModelPart(this);
		cube_r5.setPivot(-0.5F, 0.0F, 0.0F);
		end_seat_left2.addChild(cube_r5);
		setRotationAngle(cube_r5, 0.0F, 0.0F, 0.0873F);
		cube_r5.setTextureOffset(0, 0).addCuboid(32.92F, -8.74F, 17.0F, 7.0F, 1.0F, 12.0F, 0.0F, false);

		end_seat_left3 = new ModelPart(this);
		end_seat_left3.setPivot(-8.25F, -1.75F, 8.0F);
		end_seat_handrail.addChild(end_seat_left3);
		setRotationAngle(end_seat_left3, 0.0F, 1.5708F, 0.0F);
		end_seat_left3.setTextureOffset(0, 0).addCuboid(39.75F, -17.25F, 17.0F, 1.0F, 13.0F, 12.0F, 0.0F, false);

		cube_r6 = new ModelPart(this);
		cube_r6.setPivot(-0.5F, 0.0F, 0.0F);
		end_seat_left3.addChild(cube_r6);
		setRotationAngle(cube_r6, 0.0F, 0.0F, 0.0873F);
		cube_r6.setTextureOffset(0, 0).addCuboid(32.92F, -8.74F, 17.0F, 7.0F, 1.0F, 12.0F, 0.0F, false);

		handrail_end_right = new ModelPart(this);
		handrail_end_right.setPivot(0.0F, 0.0F, 4.0F);
		end_seat_handrail.addChild(handrail_end_right);
		handrail_end_right.setTextureOffset(0, 0).addCuboid(-11.0F, -39.06F, 8.5F, 0.0F, 32.0F, 0.0F, 0.2F, true);

		cube_r7 = new ModelPart(this);
		cube_r7.setPivot(-10.6875F, -6.95F, 8.55F);
		handrail_end_right.addChild(cube_r7);
		setRotationAngle(cube_r7, -0.8598F, -0.6048F, -3.0693F);
		cube_r7.setTextureOffset(0, 0).addCuboid(0.2075F, 0.2F, -1.2F, 0.0F, 0.0F, 1.0F, 0.2F, false);

		cube_r8 = new ModelPart(this);
		cube_r8.setPivot(-12.375F, -7.3125F, 2.8125F);
		handrail_end_right.addChild(cube_r8);
		setRotationAngle(cube_r8, -1.399F, -1.1819F, 1.3833F);
		cube_r8.setTextureOffset(0, 0).addCuboid(1.005F, 0.0025F, -0.0025F, 2.0F, 0.0F, 0.0F, 0.2F, true);

		cube_r9 = new ModelPart(this);
		cube_r9.setPivot(-12.75F, -6.125F, 3.9375F);
		handrail_end_right.addChild(cube_r9);
		setRotationAngle(cube_r9, 0.0F, -1.2654F, 0.0F);
		cube_r9.setTextureOffset(0, 0).addCuboid(2.0F, 0.0F, -0.0075F, 2.0F, 0.0F, 0.0F, 0.2F, true);

		handrail_end_right2 = new ModelPart(this);
		handrail_end_right2.setPivot(3.0F, 0.0F, -23.0F);
		end_seat_handrail.addChild(handrail_end_right2);
		handrail_end_right2.setTextureOffset(0, 0).addCuboid(-11.0F, -39.31F, 6.75F, 0.0F, 22.0F, 0.0F, 0.2F, true);

		cube_r10 = new ModelPart(this);
		cube_r10.setPivot(-10.75F, -17.1375F, 6.55F);
		handrail_end_right2.addChild(cube_r10);
		setRotationAngle(cube_r10, 0.0F, -1.5708F, 2.4871F);
		cube_r10.setTextureOffset(0, 0).addCuboid(0.2F, 0.2075F, -1.2F, 0.0F, 0.0F, 1.0F, 0.2F, false);

		handrail_end_right3 = new ModelPart(this);
		handrail_end_right3.setPivot(0.0F, 0.0F, 60.0F);
		end_seat_handrail.addChild(handrail_end_right3);
		handrail_end_right3.setTextureOffset(0, 0).addCuboid(-11.0F, -39.06F, -60.5F, 0.0F, 32.0F, 0.0F, 0.2F, true);
		handrail_end_right3.setTextureOffset(0, 0).addCuboid(-18.93F, -13.6F, -60.5F, 0.0F, 0.0F, 1.0F, 0.2F, true);
		handrail_end_right3.setTextureOffset(0, 0).addCuboid(-12.5F, -9.31F, -60.5F, 0.0F, 1.0F, 0.0F, 0.2F, true);
		handrail_end_right3.setTextureOffset(0, 0).addCuboid(-16.5F, -8.06F, -60.5F, 4.0F, 0.0F, 0.0F, 0.2F, true);
		handrail_end_right3.setTextureOffset(0, 0).addCuboid(-16.5F, -12.06F, -60.5F, 0.0F, 4.0F, 0.0F, 0.2F, true);

		cube_r11 = new ModelPart(this);
		cube_r11.setPivot(-12.5F, -9.5625F, -60.5F);
		handrail_end_right3.addChild(cube_r11);
		setRotationAngle(cube_r11, 0.0F, 0.0F, -1.0036F);
		cube_r11.setTextureOffset(0, 0).addCuboid(0.0F, -7.4975F, 0.0F, 0.0F, 9.0F, 0.0F, 0.2F, true);

		cube_r12 = new ModelPart(this);
		cube_r12.setPivot(-10.6875F, -6.95F, -60.55F);
		handrail_end_right3.addChild(cube_r12);
		setRotationAngle(cube_r12, 0.8598F, 0.6048F, -3.0693F);
		cube_r12.setTextureOffset(0, 0).addCuboid(0.2075F, 0.2F, 0.2F, 0.0F, 0.0F, 1.0F, 0.2F, false);

		cube_r13 = new ModelPart(this);
		cube_r13.setPivot(-12.375F, -7.3125F, -54.8125F);
		handrail_end_right3.addChild(cube_r13);
		setRotationAngle(cube_r13, 1.399F, 1.1819F, 1.3833F);
		cube_r13.setTextureOffset(0, 0).addCuboid(1.0F, 0.0025F, -0.0075F, 2.0F, 0.0F, 0.0F, 0.2F, true);

		cube_r14 = new ModelPart(this);
		cube_r14.setPivot(-12.75F, -6.125F, -55.9375F);
		handrail_end_right3.addChild(cube_r14);
		setRotationAngle(cube_r14, 0.0F, 1.2654F, 0.0F);
		cube_r14.setTextureOffset(0, 0).addCuboid(2.0F, 0.0F, -0.0025F, 2.0F, 0.0F, 0.0F, 0.2F, true);

		handrail_end_left = new ModelPart(this);
		handrail_end_left.setPivot(0.0F, 0.0F, 4.0F);
		end_seat_handrail.addChild(handrail_end_left);
		handrail_end_left.setTextureOffset(0, 0).addCuboid(11.0F, -39.06F, 8.5F, 0.0F, 32.0F, 0.0F, 0.2F, false);

		cube_r15 = new ModelPart(this);
		cube_r15.setPivot(10.6875F, -6.95F, 8.55F);
		handrail_end_left.addChild(cube_r15);
		setRotationAngle(cube_r15, -0.8598F, 0.6048F, 3.0693F);
		cube_r15.setTextureOffset(0, 0).addCuboid(-0.2075F, 0.2F, -1.2F, 0.0F, 0.0F, 1.0F, 0.2F, true);

		cube_r16 = new ModelPart(this);
		cube_r16.setPivot(12.375F, -7.3125F, 2.8125F);
		handrail_end_left.addChild(cube_r16);
		setRotationAngle(cube_r16, -1.399F, 1.1819F, -1.3833F);
		cube_r16.setTextureOffset(0, 0).addCuboid(-3.075F, 0.0025F, -0.0025F, 2.0F, 0.0F, 0.0F, 0.2F, false);

		cube_r17 = new ModelPart(this);
		cube_r17.setPivot(12.75F, -6.125F, 3.9375F);
		handrail_end_left.addChild(cube_r17);
		setRotationAngle(cube_r17, 0.0F, 1.2654F, 0.0F);
		cube_r17.setTextureOffset(0, 0).addCuboid(-4.0F, 0.025F, -0.0075F, 2.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_end_left2 = new ModelPart(this);
		handrail_end_left2.setPivot(-3.0F, 0.0F, -23.0F);
		end_seat_handrail.addChild(handrail_end_left2);
		handrail_end_left2.setTextureOffset(0, 0).addCuboid(11.0F, -39.31F, 6.75F, 0.0F, 22.0F, 0.0F, 0.2F, false);

		cube_r18 = new ModelPart(this);
		cube_r18.setPivot(10.75F, -17.1375F, 6.55F);
		handrail_end_left2.addChild(cube_r18);
		setRotationAngle(cube_r18, 0.0F, 1.5708F, -2.4871F);
		cube_r18.setTextureOffset(0, 0).addCuboid(-0.2F, 0.2375F, -1.2F, 0.0F, 0.0F, 1.0F, 0.2F, true);

		handrail_end_left3 = new ModelPart(this);
		handrail_end_left3.setPivot(0.0F, 0.0F, 60.0F);
		end_seat_handrail.addChild(handrail_end_left3);
		handrail_end_left3.setTextureOffset(0, 0).addCuboid(11.0F, -39.06F, -60.5F, 0.0F, 32.0F, 0.0F, 0.2F, false);
		handrail_end_left3.setTextureOffset(0, 0).addCuboid(18.93F, -13.6F, -60.5F, 0.0F, 0.0F, 1.0F, 0.2F, false);
		handrail_end_left3.setTextureOffset(0, 0).addCuboid(12.5F, -9.31F, -60.5F, 0.0F, 1.0F, 0.0F, 0.2F, false);
		handrail_end_left3.setTextureOffset(0, 0).addCuboid(12.5F, -8.06F, -60.5F, 4.0F, 0.0F, 0.0F, 0.2F, false);
		handrail_end_left3.setTextureOffset(0, 0).addCuboid(16.5F, -12.06F, -60.5F, 0.0F, 4.0F, 0.0F, 0.2F, false);

		cube_r19 = new ModelPart(this);
		cube_r19.setPivot(12.5F, -9.5625F, -60.5F);
		handrail_end_left3.addChild(cube_r19);
		setRotationAngle(cube_r19, 0.0F, 0.0F, 1.0036F);
		cube_r19.setTextureOffset(0, 0).addCuboid(0.0F, -7.4975F, 0.0F, 0.0F, 9.0F, 0.0F, 0.2F, false);

		cube_r20 = new ModelPart(this);
		cube_r20.setPivot(10.6875F, -6.95F, -60.55F);
		handrail_end_left3.addChild(cube_r20);
		setRotationAngle(cube_r20, 0.8598F, -0.6048F, 3.0693F);
		cube_r20.setTextureOffset(0, 0).addCuboid(-0.2075F, 0.2F, 0.2F, 0.0F, 0.0F, 1.0F, 0.2F, true);

		cube_r21 = new ModelPart(this);
		cube_r21.setPivot(12.375F, -7.3125F, -54.8125F);
		handrail_end_left3.addChild(cube_r21);
		setRotationAngle(cube_r21, 1.399F, -1.1819F, -1.3833F);
		cube_r21.setTextureOffset(0, 0).addCuboid(-3.005F, 0.0025F, -0.0075F, 2.0F, 0.0F, 0.0F, 0.2F, false);

		cube_r22 = new ModelPart(this);
		cube_r22.setPivot(12.75F, -6.125F, -55.9375F);
		handrail_end_left3.addChild(cube_r22);
		setRotationAngle(cube_r22, 0.0F, -1.2654F, 0.0F);
		cube_r22.setTextureOffset(0, 0).addCuboid(-4.0F, 0.0F, -0.0025F, 2.0F, 0.0F, 0.0F, 0.2F, false);

		window_seat_handrail = new ModelPart(this);
		window_seat_handrail.setPivot(0.0F, 24.0F, 0.0F);
		

		cube_r23 = new ModelPart(this);
		cube_r23.setPivot(-10.25F, -35.185F, 36.5F);
		window_seat_handrail.addChild(cube_r23);
		setRotationAngle(cube_r23, 0.0F, 0.0F, 0.3622F);
		cube_r23.setTextureOffset(0, 0).addCuboid(0.0F, -2.0F, 0.0F, 0.0F, 4.0F, 0.0F, 0.2F, false);

		top_handrails = new ModelPart(this);
		top_handrails.setPivot(0.0F, 0.0F, 0.0F);
		window_seat_handrail.addChild(top_handrails);
		top_handrails.setTextureOffset(0, 0).addCuboid(9.5F, -39.25F, 49.5F, 0.0F, 2.0F, 0.0F, 0.2F, false);
		top_handrails.setTextureOffset(0, 0).addCuboid(9.5F, -39.25F, 36.5F, 0.0F, 2.0F, 0.0F, 0.2F, false);
		top_handrails.setTextureOffset(0, 0).addCuboid(9.5F, -39.25F, 18.75F, 0.0F, 2.0F, 0.0F, 0.2F, false);
		top_handrails.setTextureOffset(0, 0).addCuboid(9.5F, -39.25F, 0.0F, 0.0F, 2.0F, 0.0F, 0.2F, false);
		top_handrails.setTextureOffset(0, 0).addCuboid(9.5F, -39.25F, -19.0F, 0.0F, 2.0F, 0.0F, 0.2F, false);
		top_handrails.setTextureOffset(0, 0).addCuboid(9.5F, -39.25F, -36.5F, 0.0F, 2.0F, 0.0F, 0.2F, false);
		top_handrails.setTextureOffset(0, 0).addCuboid(9.5F, -39.25F, -49.5F, 0.0F, 2.0F, 0.0F, 0.2F, false);
		top_handrails.setTextureOffset(0, 0).addCuboid(-9.5F, -39.25F, 49.5F, 0.0F, 2.0F, 0.0F, 0.2F, true);
		top_handrails.setTextureOffset(0, 0).addCuboid(-9.5F, -39.25F, 36.5F, 0.0F, 2.0F, 0.0F, 0.2F, true);
		top_handrails.setTextureOffset(0, 0).addCuboid(-9.5F, -39.25F, 18.75F, 0.0F, 2.0F, 0.0F, 0.2F, true);
		top_handrails.setTextureOffset(0, 0).addCuboid(-9.5F, -39.25F, 0.0F, 0.0F, 2.0F, 0.0F, 0.2F, true);
		top_handrails.setTextureOffset(0, 0).addCuboid(-9.5F, -39.25F, -19.0F, 0.0F, 2.0F, 0.0F, 0.2F, true);
		top_handrails.setTextureOffset(0, 0).addCuboid(-9.5F, -39.25F, -36.5F, 0.0F, 2.0F, 0.0F, 0.2F, true);
		top_handrails.setTextureOffset(0, 0).addCuboid(-9.5F, -39.25F, -49.5F, 0.0F, 2.0F, 0.0F, 0.2F, true);
		top_handrails.setTextureOffset(0, 0).addCuboid(9.5F, -37.06F, -49.5F, 0.0F, 0.0F, 99.0F, 0.2F, false);
		top_handrails.setTextureOffset(0, 0).addCuboid(-9.5F, -37.06F, -49.5F, 0.0F, 0.0F, 99.0F, 0.2F, true);

		win_seat_left = new ModelPart(this);
		win_seat_left.setPivot(-0.25F, -1.75F, 21.0F);
		window_seat_handrail.addChild(win_seat_left);
		win_seat_left.setTextureOffset(0, 0).addCuboid(18.75F, -17.25F, 16.0F, 1.0F, 13.0F, 12.0F, 0.0F, false);

		cube_r24 = new ModelPart(this);
		cube_r24.setPivot(-1.5F, 0.0F, -1.0F);
		win_seat_left.addChild(cube_r24);
		setRotationAngle(cube_r24, 0.0F, 0.0F, 0.0873F);
		cube_r24.setTextureOffset(0, 0).addCuboid(13.0F, -7.0F, 17.0F, 7.0F, 1.0F, 12.0F, 0.0F, false);

		win_seat_right = new ModelPart(this);
		win_seat_right.setPivot(0.25F, -1.75F, 20.0F);
		window_seat_handrail.addChild(win_seat_right);
		win_seat_right.setTextureOffset(0, 0).addCuboid(-19.75F, -17.25F, 17.0F, 1.0F, 13.0F, 12.0F, 0.0F, true);

		cube_r25 = new ModelPart(this);
		cube_r25.setPivot(1.5F, 0.0F, 0.0F);
		win_seat_right.addChild(cube_r25);
		setRotationAngle(cube_r25, 0.0F, 0.0F, -0.0873F);
		cube_r25.setTextureOffset(0, 0).addCuboid(-20.0F, -7.0F, 17.0F, 7.0F, 1.0F, 12.0F, 0.0F, true);

		win_seat_left6 = new ModelPart(this);
		win_seat_left6.setPivot(-0.25F, -1.75F, -67.0F);
		window_seat_handrail.addChild(win_seat_left6);
		win_seat_left6.setTextureOffset(0, 0).addCuboid(18.75F, -17.25F, 18.0F, 1.0F, 13.0F, 12.0F, 0.0F, false);

		cube_r26 = new ModelPart(this);
		cube_r26.setPivot(-1.5F, 0.0F, 2.0F);
		win_seat_left6.addChild(cube_r26);
		setRotationAngle(cube_r26, 0.0F, 0.0F, 0.0873F);
		cube_r26.setTextureOffset(0, 0).addCuboid(13.0F, -7.0F, 16.0F, 7.0F, 1.0F, 12.0F, 0.0F, false);

		win_seat_left2 = new ModelPart(this);
		win_seat_left2.setPivot(-7.25F, -1.75F, 30.0F);
		window_seat_handrail.addChild(win_seat_left2);
		setRotationAngle(win_seat_left2, 0.0F, 1.5708F, 0.0F);
		win_seat_left2.setTextureOffset(0, 0).addCuboid(10.75F, -17.25F, 16.0F, 1.0F, 13.0F, 12.0F, 0.0F, false);

		cube_r27 = new ModelPart(this);
		cube_r27.setPivot(-0.5F, 0.0F, 0.0F);
		win_seat_left2.addChild(cube_r27);
		setRotationAngle(cube_r27, 0.0F, 0.0F, 0.0873F);
		cube_r27.setTextureOffset(0, 0).addCuboid(4.03F, -6.21F, 16.0F, 7.0F, 1.0F, 12.0F, 0.0F, false);

		win_seat_left3 = new ModelPart(this);
		win_seat_left3.setPivot(-7.25F, -1.75F, 9.75F);
		window_seat_handrail.addChild(win_seat_left3);
		setRotationAngle(win_seat_left3, 0.0F, 1.5708F, 0.0F);
		win_seat_left3.setTextureOffset(0, 0).addCuboid(8.75F, -17.25F, 16.0F, 1.0F, 13.0F, 12.0F, 0.0F, false);

		cube_r28 = new ModelPart(this);
		cube_r28.setPivot(-0.5F, 0.0F, 0.0F);
		win_seat_left3.addChild(cube_r28);
		setRotationAngle(cube_r28, 0.0F, 0.0F, 0.0873F);
		cube_r28.setTextureOffset(0, 0).addCuboid(2.04F, -6.04F, 16.0F, 7.0F, 1.0F, 12.0F, 0.0F, false);

		win_seat_left4 = new ModelPart(this);
		win_seat_left4.setPivot(36.75F, -1.75F, -31.75F);
		window_seat_handrail.addChild(win_seat_left4);
		setRotationAngle(win_seat_left4, 0.0F, -1.5708F, 0.0F);
		win_seat_left4.setTextureOffset(0, 0).addCuboid(30.75F, -17.25F, 16.0F, 1.0F, 13.0F, 12.0F, 0.0F, false);

		cube_r29 = new ModelPart(this);
		cube_r29.setPivot(-0.5F, 0.0F, 0.0F);
		win_seat_left4.addChild(cube_r29);
		setRotationAngle(cube_r29, 0.0F, 0.0F, 0.0873F);
		cube_r29.setTextureOffset(0, 0).addCuboid(23.95F, -8.95F, 16.0F, 7.0F, 1.0F, 12.0F, 0.0F, false);

		win_seat_right2 = new ModelPart(this);
		win_seat_right2.setPivot(-36.75F, -1.75F, 29.0F);
		window_seat_handrail.addChild(win_seat_right2);
		setRotationAngle(win_seat_right2, 0.0F, 1.5708F, 0.0F);
		win_seat_right2.setTextureOffset(0, 0).addCuboid(9.75F, -17.25F, 16.0F, 1.0F, 13.0F, 12.0F, 0.0F, false);

		cube_r30 = new ModelPart(this);
		cube_r30.setPivot(-0.5F, 0.0F, 0.0F);
		win_seat_right2.addChild(cube_r30);
		setRotationAngle(cube_r30, 0.0F, 0.0F, 0.0873F);
		cube_r30.setTextureOffset(0, 0).addCuboid(3.04F, -6.12F, 16.0F, 7.0F, 1.0F, 12.0F, 0.0F, false);

		win_seat_right3 = new ModelPart(this);
		win_seat_right3.setPivot(-36.75F, -1.75F, 12.75F);
		window_seat_handrail.addChild(win_seat_right3);
		setRotationAngle(win_seat_right3, 0.0F, 1.5708F, 0.0F);
		win_seat_right3.setTextureOffset(0, 0).addCuboid(11.75F, -17.25F, 16.0F, 1.0F, 13.0F, 12.0F, 0.0F, false);

		cube_r31 = new ModelPart(this);
		cube_r31.setPivot(-0.5F, 0.0F, 0.0F);
		win_seat_right3.addChild(cube_r31);
		setRotationAngle(cube_r31, 0.0F, 0.0F, 0.0873F);
		cube_r31.setTextureOffset(0, 0).addCuboid(5.04F, -6.3F, 16.0F, 7.0F, 1.0F, 12.0F, 0.0F, false);

		win_seat_right4 = new ModelPart(this);
		win_seat_right4.setPivot(7.25F, -1.75F, -28.75F);
		window_seat_handrail.addChild(win_seat_right4);
		setRotationAngle(win_seat_right4, 0.0F, -1.5708F, 0.0F);
		win_seat_right4.setTextureOffset(0, 0).addCuboid(27.75F, -17.25F, 16.0F, 1.0F, 13.0F, 12.0F, 0.0F, false);

		cube_r32 = new ModelPart(this);
		cube_r32.setPivot(-0.5F, 0.0F, 0.0F);
		win_seat_right4.addChild(cube_r32);
		setRotationAngle(cube_r32, 0.0F, 0.0F, 0.0873F);
		cube_r32.setTextureOffset(0, 0).addCuboid(20.96F, -7.69F, 16.0F, 7.0F, 1.0F, 12.0F, 0.0F, false);

		win_seat_right5 = new ModelPart(this);
		win_seat_right5.setPivot(7.25F, -1.75F, -47.0F);
		window_seat_handrail.addChild(win_seat_right5);
		setRotationAngle(win_seat_right5, 0.0F, -1.5708F, 0.0F);
		win_seat_right5.setTextureOffset(0, 0).addCuboid(27.75F, -17.25F, 16.0F, 1.0F, 13.0F, 12.0F, 0.0F, false);

		cube_r33 = new ModelPart(this);
		cube_r33.setPivot(-0.5F, 0.0F, 0.0F);
		win_seat_right5.addChild(cube_r33);
		setRotationAngle(cube_r33, 0.0F, 0.0F, 0.0873F);
		cube_r33.setTextureOffset(0, 0).addCuboid(20.96F, -7.69F, 16.0F, 7.0F, 1.0F, 12.0F, 0.0F, false);

		win_seat_left5 = new ModelPart(this);
		win_seat_left5.setPivot(36.75F, -1.75F, -47.0F);
		window_seat_handrail.addChild(win_seat_left5);
		setRotationAngle(win_seat_left5, 0.0F, -1.5708F, 0.0F);
		win_seat_left5.setTextureOffset(0, 0).addCuboid(27.75F, -17.25F, 16.0F, 1.0F, 13.0F, 12.0F, 0.0F, false);

		cube_r34 = new ModelPart(this);
		cube_r34.setPivot(-0.5F, 0.0F, 0.0F);
		win_seat_left5.addChild(cube_r34);
		setRotationAngle(cube_r34, 0.0F, 0.0F, 0.0873F);
		cube_r34.setTextureOffset(0, 0).addCuboid(20.96F, -7.69F, 16.0F, 7.0F, 1.0F, 12.0F, 0.0F, false);

		handrail_window_left7 = new ModelPart(this);
		handrail_window_left7.setPivot(0.0F, 0.0F, -25.0F);
		window_seat_handrail.addChild(handrail_window_left7);
		handrail_window_left7.setTextureOffset(0, 0).addCuboid(11.0F, -33.06F, -24.5F, 0.0F, 26.0F, 0.0F, 0.2F, false);

		cube_r35 = new ModelPart(this);
		cube_r35.setPivot(10.25F, -35.185F, -24.5F);
		handrail_window_left7.addChild(cube_r35);
		setRotationAngle(cube_r35, 0.0F, 0.0F, -0.3622F);
		cube_r35.setTextureOffset(0, 0).addCuboid(0.0F, -2.0F, 0.0F, 0.0F, 4.0F, 0.0F, 0.2F, true);

		cube_r36 = new ModelPart(this);
		cube_r36.setPivot(10.6875F, -6.95F, -24.55F);
		handrail_window_left7.addChild(cube_r36);
		setRotationAngle(cube_r36, 0.8598F, -0.6048F, 3.0693F);
		cube_r36.setTextureOffset(0, 0).addCuboid(-0.2075F, 0.2F, 0.2F, 0.0F, 0.0F, 1.0F, 0.2F, true);

		cube_r37 = new ModelPart(this);
		cube_r37.setPivot(12.375F, -7.3125F, -18.8125F);
		handrail_window_left7.addChild(cube_r37);
		setRotationAngle(cube_r37, 1.399F, -1.1819F, -1.3833F);
		cube_r37.setTextureOffset(0, 0).addCuboid(-3.0F, 0.0025F, 0.0025F, 2.0F, 0.0F, 0.0F, 0.2F, false);

		cube_r38 = new ModelPart(this);
		cube_r38.setPivot(12.75F, -6.125F, -19.9375F);
		handrail_window_left7.addChild(cube_r38);
		setRotationAngle(cube_r38, 0.0F, -1.2654F, 0.0F);
		cube_r38.setTextureOffset(0, 0).addCuboid(-4.0F, 0.0F, 0.0075F, 2.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_window_left6 = new ModelPart(this);
		handrail_window_left6.setPivot(0.0F, 0.0F, -81.0F);
		window_seat_handrail.addChild(handrail_window_left6);
		handrail_window_left6.setTextureOffset(0, 0).addCuboid(11.0F, -33.06F, 44.5F, 0.0F, 26.0F, 0.0F, 0.2F, false);
		handrail_window_left6.setTextureOffset(0, 0).addCuboid(18.93F, -13.62F, 43.5F, 0.0F, 0.0F, 1.0F, 0.2F, false);
		handrail_window_left6.setTextureOffset(0, 0).addCuboid(12.5F, -9.31F, 44.5F, 0.0F, 1.0F, 0.0F, 0.2F, false);
		handrail_window_left6.setTextureOffset(0, 0).addCuboid(12.5F, -8.06F, 44.5F, 4.0F, 0.0F, 0.0F, 0.2F, false);
		handrail_window_left6.setTextureOffset(0, 0).addCuboid(16.5F, -12.06F, 44.5F, 0.0F, 4.0F, 0.0F, 0.2F, false);

		cube_r39 = new ModelPart(this);
		cube_r39.setPivot(10.25F, -35.185F, 44.5F);
		handrail_window_left6.addChild(cube_r39);
		setRotationAngle(cube_r39, 0.0F, 0.0F, -0.3622F);
		cube_r39.setTextureOffset(0, 0).addCuboid(0.0F, -2.0F, 0.0F, 0.0F, 4.0F, 0.0F, 0.2F, true);

		cube_r40 = new ModelPart(this);
		cube_r40.setPivot(12.5F, -9.5625F, 44.5F);
		handrail_window_left6.addChild(cube_r40);
		setRotationAngle(cube_r40, 0.0F, 0.0F, 1.0036F);
		cube_r40.setTextureOffset(0, 0).addCuboid(0.0F, -7.4975F, 0.0F, 0.0F, 9.0F, 0.0F, 0.2F, false);

		cube_r41 = new ModelPart(this);
		cube_r41.setPivot(10.6875F, -6.95F, 44.55F);
		handrail_window_left6.addChild(cube_r41);
		setRotationAngle(cube_r41, -0.8598F, 0.6048F, 3.0693F);
		cube_r41.setTextureOffset(0, 0).addCuboid(-0.2075F, 0.2F, -1.2F, 0.0F, 0.0F, 1.0F, 0.2F, true);

		cube_r42 = new ModelPart(this);
		cube_r42.setPivot(12.375F, -7.3125F, 38.8125F);
		handrail_window_left6.addChild(cube_r42);
		setRotationAngle(cube_r42, -1.399F, 1.1819F, -1.3833F);
		cube_r42.setTextureOffset(0, 0).addCuboid(-3.0F, 0.0025F, 0.0075F, 2.0F, 0.0F, 0.0F, 0.2F, false);

		cube_r43 = new ModelPart(this);
		cube_r43.setPivot(12.75F, -6.125F, 39.9375F);
		handrail_window_left6.addChild(cube_r43);
		setRotationAngle(cube_r43, 0.0F, 1.2654F, 0.0F);
		cube_r43.setTextureOffset(0, 0).addCuboid(-4.0F, 0.0F, 0.0025F, 2.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_window_left5 = new ModelPart(this);
		handrail_window_left5.setPivot(-3.0F, 0.0F, -49.75F);
		window_seat_handrail.addChild(handrail_window_left5);
		handrail_window_left5.setTextureOffset(0, 0).addCuboid(11.0F, -33.31F, 30.75F, 0.0F, 16.0F, 0.0F, 0.2F, false);

		cube_r44 = new ModelPart(this);
		cube_r44.setPivot(11.75F, -35.185F, 30.75F);
		handrail_window_left5.addChild(cube_r44);
		setRotationAngle(cube_r44, 0.0F, 0.0F, 0.3622F);
		cube_r44.setTextureOffset(0, 0).addCuboid(0.0F, -2.0F, 0.0F, 0.0F, 4.0F, 0.0F, 0.2F, false);

		cube_r45 = new ModelPart(this);
		cube_r45.setPivot(10.75F, -17.1375F, 30.55F);
		handrail_window_left5.addChild(cube_r45);
		setRotationAngle(cube_r45, 0.0F, 1.5708F, -2.4871F);
		cube_r45.setTextureOffset(0, 0).addCuboid(-0.2F, 0.2375F, -1.2F, 0.0F, 0.0F, 1.0F, 0.2F, true);

		handrail_window_left4 = new ModelPart(this);
		handrail_window_left4.setPivot(-3.0F, 0.0F, -30.75F);
		window_seat_handrail.addChild(handrail_window_left4);
		handrail_window_left4.setTextureOffset(0, 0).addCuboid(11.0F, -33.31F, 30.75F, 0.0F, 16.0F, 0.0F, 0.2F, false);

		cube_r46 = new ModelPart(this);
		cube_r46.setPivot(11.75F, -35.185F, 30.75F);
		handrail_window_left4.addChild(cube_r46);
		setRotationAngle(cube_r46, 0.0F, 0.0F, 0.3622F);
		cube_r46.setTextureOffset(0, 0).addCuboid(0.0F, -2.0F, 0.0F, 0.0F, 4.0F, 0.0F, 0.2F, false);

		cube_r47 = new ModelPart(this);
		cube_r47.setPivot(10.75F, -17.1375F, 30.55F);
		handrail_window_left4.addChild(cube_r47);
		setRotationAngle(cube_r47, 0.0F, 1.5708F, -2.4871F);
		cube_r47.setTextureOffset(0, 0).addCuboid(-0.2F, 0.2375F, -1.2F, 0.0F, 0.0F, 1.0F, 0.2F, true);

		handrail_window_left3 = new ModelPart(this);
		handrail_window_left3.setPivot(-3.0F, 0.0F, -12.0F);
		window_seat_handrail.addChild(handrail_window_left3);
		handrail_window_left3.setTextureOffset(0, 0).addCuboid(11.0F, -33.31F, 30.75F, 0.0F, 16.0F, 0.0F, 0.2F, false);

		cube_r48 = new ModelPart(this);
		cube_r48.setPivot(11.75F, -35.185F, 30.75F);
		handrail_window_left3.addChild(cube_r48);
		setRotationAngle(cube_r48, 0.0F, 0.0F, 0.3622F);
		cube_r48.setTextureOffset(0, 0).addCuboid(0.0F, -2.0F, 0.0F, 0.0F, 4.0F, 0.0F, 0.2F, false);

		cube_r49 = new ModelPart(this);
		cube_r49.setPivot(10.75F, -17.1375F, 30.55F);
		handrail_window_left3.addChild(cube_r49);
		setRotationAngle(cube_r49, 0.0F, 1.5708F, -2.4871F);
		cube_r49.setTextureOffset(0, 0).addCuboid(-0.2F, 0.2075F, -1.2F, 0.0F, 0.0F, 1.0F, 0.2F, true);

		handrail_window_left2 = new ModelPart(this);
		handrail_window_left2.setPivot(0.0F, 0.0F, 73.0F);
		window_seat_handrail.addChild(handrail_window_left2);
		handrail_window_left2.setTextureOffset(0, 0).addCuboid(11.0F, -33.06F, -36.5F, 0.0F, 26.0F, 0.0F, 0.2F, false);
		handrail_window_left2.setTextureOffset(0, 0).addCuboid(18.93F, -13.6F, -36.5F, 0.0F, 0.0F, 1.0F, 0.2F, false);
		handrail_window_left2.setTextureOffset(0, 0).addCuboid(12.5F, -9.31F, -36.5F, 0.0F, 1.0F, 0.0F, 0.2F, false);
		handrail_window_left2.setTextureOffset(0, 0).addCuboid(12.5F, -8.06F, -36.5F, 4.0F, 0.0F, 0.0F, 0.2F, false);
		handrail_window_left2.setTextureOffset(0, 0).addCuboid(16.5F, -12.06F, -36.5F, 0.0F, 4.0F, 0.0F, 0.2F, false);

		cube_r50 = new ModelPart(this);
		cube_r50.setPivot(10.25F, -35.185F, -36.5F);
		handrail_window_left2.addChild(cube_r50);
		setRotationAngle(cube_r50, 0.0F, 0.0F, -0.3622F);
		cube_r50.setTextureOffset(0, 0).addCuboid(0.0F, -2.0F, 0.0F, 0.0F, 4.0F, 0.0F, 0.2F, true);

		cube_r51 = new ModelPart(this);
		cube_r51.setPivot(12.5F, -9.5625F, -36.5F);
		handrail_window_left2.addChild(cube_r51);
		setRotationAngle(cube_r51, 0.0F, 0.0F, 1.0036F);
		cube_r51.setTextureOffset(0, 0).addCuboid(0.0F, -7.4975F, 0.0F, 0.0F, 9.0F, 0.0F, 0.2F, false);

		cube_r52 = new ModelPart(this);
		cube_r52.setPivot(10.6875F, -6.95F, -36.55F);
		handrail_window_left2.addChild(cube_r52);
		setRotationAngle(cube_r52, 0.8598F, -0.6048F, 3.0693F);
		cube_r52.setTextureOffset(0, 0).addCuboid(-0.2075F, 0.2F, 0.2F, 0.0F, 0.0F, 1.0F, 0.2F, true);

		cube_r53 = new ModelPart(this);
		cube_r53.setPivot(12.375F, -7.3125F, -30.8125F);
		handrail_window_left2.addChild(cube_r53);
		setRotationAngle(cube_r53, 1.399F, -1.1819F, -1.3833F);
		cube_r53.setTextureOffset(0, 0).addCuboid(-3.005F, 0.0025F, -0.0075F, 2.0F, 0.0F, 0.0F, 0.2F, false);

		cube_r54 = new ModelPart(this);
		cube_r54.setPivot(12.75F, -6.125F, -31.9375F);
		handrail_window_left2.addChild(cube_r54);
		setRotationAngle(cube_r54, 0.0F, -1.2654F, 0.0F);
		cube_r54.setTextureOffset(0, 0).addCuboid(-4.0F, 0.005F, -0.0025F, 2.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_window_left1 = new ModelPart(this);
		handrail_window_left1.setPivot(0.0F, 0.0F, 17.0F);
		window_seat_handrail.addChild(handrail_window_left1);
		handrail_window_left1.setTextureOffset(0, 0).addCuboid(11.0F, -33.06F, 32.5F, 0.0F, 26.0F, 0.0F, 0.2F, false);

		cube_r55 = new ModelPart(this);
		cube_r55.setPivot(10.25F, -35.185F, 32.5F);
		handrail_window_left1.addChild(cube_r55);
		setRotationAngle(cube_r55, 0.0F, 0.0F, -0.3622F);
		cube_r55.setTextureOffset(0, 0).addCuboid(0.0F, -2.0F, 0.0F, 0.0F, 4.0F, 0.0F, 0.2F, true);

		cube_r56 = new ModelPart(this);
		cube_r56.setPivot(10.6875F, -6.95F, 32.55F);
		handrail_window_left1.addChild(cube_r56);
		setRotationAngle(cube_r56, -0.8598F, 0.6048F, 3.0693F);
		cube_r56.setTextureOffset(0, 0).addCuboid(-0.2075F, 0.2F, -1.2F, 0.0F, 0.0F, 1.0F, 0.2F, true);

		cube_r57 = new ModelPart(this);
		cube_r57.setPivot(12.375F, -7.3125F, 26.8125F);
		handrail_window_left1.addChild(cube_r57);
		setRotationAngle(cube_r57, -1.399F, 1.1819F, -1.3833F);
		cube_r57.setTextureOffset(0, 0).addCuboid(-3.075F, 0.0025F, -0.0025F, 2.0F, 0.0F, 0.0F, 0.2F, false);

		cube_r58 = new ModelPart(this);
		cube_r58.setPivot(12.75F, -6.125F, 27.9375F);
		handrail_window_left1.addChild(cube_r58);
		setRotationAngle(cube_r58, 0.0F, 1.2654F, 0.0F);
		cube_r58.setTextureOffset(0, 0).addCuboid(-4.0F, 0.025F, -0.0075F, 2.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_window_right5 = new ModelPart(this);
		handrail_window_right5.setPivot(3.0F, 0.0F, -12.0F);
		window_seat_handrail.addChild(handrail_window_right5);
		handrail_window_right5.setTextureOffset(0, 0).addCuboid(-11.0F, -33.31F, 30.75F, 0.0F, 16.0F, 0.0F, 0.2F, true);

		cube_r59 = new ModelPart(this);
		cube_r59.setPivot(-11.75F, -35.185F, 30.75F);
		handrail_window_right5.addChild(cube_r59);
		setRotationAngle(cube_r59, 0.0F, 0.0F, -0.3622F);
		cube_r59.setTextureOffset(0, 0).addCuboid(0.0F, -2.0F, 0.0F, 0.0F, 4.0F, 0.0F, 0.2F, true);

		cube_r60 = new ModelPart(this);
		cube_r60.setPivot(-13.25F, -35.185F, 61.5F);
		handrail_window_right5.addChild(cube_r60);
		setRotationAngle(cube_r60, 0.0F, 0.0F, 0.3622F);
		cube_r60.setTextureOffset(0, 0).addCuboid(0.0F, -2.0F, 0.0F, 0.0F, 4.0F, 0.0F, 0.2F, false);

		cube_r61 = new ModelPart(this);
		cube_r61.setPivot(-10.75F, -17.1375F, 30.55F);
		handrail_window_right5.addChild(cube_r61);
		setRotationAngle(cube_r61, 0.0F, -1.5708F, 2.4871F);
		cube_r61.setTextureOffset(0, 0).addCuboid(0.2F, 0.2075F, -1.2F, 0.0F, 0.0F, 1.0F, 0.2F, false);

		handrail_window_right4 = new ModelPart(this);
		handrail_window_right4.setPivot(3.0F, 0.0F, -49.5F);
		window_seat_handrail.addChild(handrail_window_right4);
		handrail_window_right4.setTextureOffset(0, 0).addCuboid(-11.0F, -33.3F, 30.75F, 0.0F, 16.0F, 0.0F, 0.2F, true);

		cube_r62 = new ModelPart(this);
		cube_r62.setPivot(-11.75F, -35.185F, 30.75F);
		handrail_window_right4.addChild(cube_r62);
		setRotationAngle(cube_r62, 0.0F, 0.0F, -0.3622F);
		cube_r62.setTextureOffset(0, 0).addCuboid(0.0F, -2.0F, 0.0F, 0.0F, 4.0F, 0.0F, 0.2F, true);

		cube_r63 = new ModelPart(this);
		cube_r63.setPivot(-10.75F, -17.1375F, 30.55F);
		handrail_window_right4.addChild(cube_r63);
		setRotationAngle(cube_r63, 0.0F, -1.5708F, 2.4871F);
		cube_r63.setTextureOffset(0, 0).addCuboid(0.2F, 0.2075F, -1.2F, 0.0F, 0.0F, 1.0F, 0.2F, false);

		handrail_window_right3 = new ModelPart(this);
		handrail_window_right3.setPivot(3.0F, 0.0F, -30.75F);
		window_seat_handrail.addChild(handrail_window_right3);
		handrail_window_right3.setTextureOffset(0, 0).addCuboid(-11.0F, -33.31F, 30.75F, 0.0F, 16.0F, 0.0F, 0.2F, true);

		cube_r64 = new ModelPart(this);
		cube_r64.setPivot(-11.75F, -35.185F, 30.75F);
		handrail_window_right3.addChild(cube_r64);
		setRotationAngle(cube_r64, 0.0F, 0.0F, -0.3622F);
		cube_r64.setTextureOffset(0, 0).addCuboid(0.0F, -2.0F, 0.0F, 0.0F, 4.0F, 0.0F, 0.2F, true);

		cube_r65 = new ModelPart(this);
		cube_r65.setPivot(-10.75F, -17.1375F, 30.55F);
		handrail_window_right3.addChild(cube_r65);
		setRotationAngle(cube_r65, 0.0F, -1.5708F, 2.4871F);
		cube_r65.setTextureOffset(0, 0).addCuboid(0.2F, 0.2075F, -1.2F, 0.0F, 0.0F, 1.0F, 0.2F, false);

		handrail_window_right2 = new ModelPart(this);
		handrail_window_right2.setPivot(0.0F, 0.0F, 73.0F);
		window_seat_handrail.addChild(handrail_window_right2);
		handrail_window_right2.setTextureOffset(0, 0).addCuboid(-11.0F, -33.06F, -36.5F, 0.0F, 26.0F, 0.0F, 0.2F, true);
		handrail_window_right2.setTextureOffset(0, 0).addCuboid(-18.93F, -13.6F, -36.5F, 0.0F, 0.0F, 1.0F, 0.2F, true);
		handrail_window_right2.setTextureOffset(0, 0).addCuboid(-12.5F, -9.31F, -36.5F, 0.0F, 1.0F, 0.0F, 0.2F, true);
		handrail_window_right2.setTextureOffset(0, 0).addCuboid(-16.5F, -8.06F, -36.5F, 4.0F, 0.0F, 0.0F, 0.2F, true);
		handrail_window_right2.setTextureOffset(0, 0).addCuboid(-16.5F, -12.06F, -36.5F, 0.0F, 4.0F, 0.0F, 0.2F, true);

		cube_r66 = new ModelPart(this);
		cube_r66.setPivot(-12.5F, -9.5625F, -36.5F);
		handrail_window_right2.addChild(cube_r66);
		setRotationAngle(cube_r66, 0.0F, 0.0F, -1.0036F);
		cube_r66.setTextureOffset(0, 0).addCuboid(0.0F, -7.4975F, 0.0F, 0.0F, 9.0F, 0.0F, 0.2F, true);

		cube_r67 = new ModelPart(this);
		cube_r67.setPivot(-10.6875F, -6.95F, -36.55F);
		handrail_window_right2.addChild(cube_r67);
		setRotationAngle(cube_r67, 0.8598F, 0.6048F, -3.0693F);
		cube_r67.setTextureOffset(0, 0).addCuboid(0.2075F, 0.2F, 0.2F, 0.0F, 0.0F, 1.0F, 0.2F, false);

		cube_r68 = new ModelPart(this);
		cube_r68.setPivot(-12.375F, -7.3125F, -30.8125F);
		handrail_window_right2.addChild(cube_r68);
		setRotationAngle(cube_r68, 1.399F, 1.1819F, 1.3833F);
		cube_r68.setTextureOffset(0, 0).addCuboid(1.0F, 0.0025F, -0.0875F, 2.0F, 0.0F, 0.0F, 0.2F, true);

		cube_r69 = new ModelPart(this);
		cube_r69.setPivot(-12.75F, -6.125F, -31.9375F);
		handrail_window_right2.addChild(cube_r69);
		setRotationAngle(cube_r69, 0.0F, 1.2654F, 0.0F);
		cube_r69.setTextureOffset(0, 0).addCuboid(2.0F, 0.0F, -0.0025F, 2.0F, 0.0F, 0.0F, 0.2F, true);

		handrail_window_right1 = new ModelPart(this);
		handrail_window_right1.setPivot(0.0F, 0.0F, 17.0F);
		window_seat_handrail.addChild(handrail_window_right1);
		handrail_window_right1.setTextureOffset(0, 0).addCuboid(-11.0F, -33.06F, 32.5F, 0.0F, 26.0F, 0.0F, 0.2F, true);

		cube_r70 = new ModelPart(this);
		cube_r70.setPivot(-10.6875F, -6.95F, 32.55F);
		handrail_window_right1.addChild(cube_r70);
		setRotationAngle(cube_r70, -0.8598F, -0.6048F, -3.0693F);
		cube_r70.setTextureOffset(0, 0).addCuboid(0.2075F, 0.2F, -1.2F, 0.0F, 0.0F, 1.0F, 0.2F, false);

		cube_r71 = new ModelPart(this);
		cube_r71.setPivot(-12.375F, -7.3125F, 26.8125F);
		handrail_window_right1.addChild(cube_r71);
		setRotationAngle(cube_r71, -1.399F, -1.1819F, 1.3833F);
		cube_r71.setTextureOffset(0, 0).addCuboid(1.075F, 0.0025F, -0.0025F, 2.0F, 0.0F, 0.0F, 0.2F, true);

		cube_r72 = new ModelPart(this);
		cube_r72.setPivot(-12.75F, -6.125F, 27.9375F);
		handrail_window_right1.addChild(cube_r72);
		setRotationAngle(cube_r72, 0.0F, -1.2654F, 0.0F);
		cube_r72.setTextureOffset(0, 0).addCuboid(2.0F, 0.025F, -0.0075F, 2.0F, 0.0F, 0.0F, 0.2F, true);

		blue_floor = new ModelPart(this);
		blue_floor.setPivot(0.0F, 0.0F, -1.0F);
		window_seat_handrail.addChild(blue_floor);
		blue_floor.setTextureOffset(0, 0).addCuboid(-18.0F, -0.125F, -49.0F, 10.0F, 0.0F, 14.0F, 0.0F, false);

		front_seat_handrail = new ModelPart(this);
		front_seat_handrail.setPivot(0.0F, 24.0F, 0.0F);
		

		handrail_head_left = new ModelPart(this);
		handrail_head_left.setPivot(0.0F, 0.0F, 2.0F);
		front_seat_handrail.addChild(handrail_head_left);
		handrail_head_left.setTextureOffset(0, 0).addCuboid(11.0F, -39.06F, 10.5F, 0.0F, 32.0F, 0.0F, 0.2F, false);

		cube_r73 = new ModelPart(this);
		cube_r73.setPivot(10.6875F, -6.95F, 10.55F);
		handrail_head_left.addChild(cube_r73);
		setRotationAngle(cube_r73, -0.8598F, 0.6048F, 3.0693F);
		cube_r73.setTextureOffset(0, 0).addCuboid(-0.2075F, 0.2F, -1.2F, 0.0F, 0.0F, 1.0F, 0.2F, true);

		cube_r74 = new ModelPart(this);
		cube_r74.setPivot(12.375F, -7.3125F, 4.8125F);
		handrail_head_left.addChild(cube_r74);
		setRotationAngle(cube_r74, -1.399F, 1.1819F, -1.3833F);
		cube_r74.setTextureOffset(0, 0).addCuboid(-3.005F, 0.0025F, -0.0025F, 2.0F, 0.0F, 0.0F, 0.2F, false);

		cube_r75 = new ModelPart(this);
		cube_r75.setPivot(12.75F, -6.125F, 5.9375F);
		handrail_head_left.addChild(cube_r75);
		setRotationAngle(cube_r75, 0.0F, 1.2654F, 0.0F);
		cube_r75.setTextureOffset(0, 0).addCuboid(-4.0F, 0.025F, -0.0075F, 2.0F, 0.0F, 0.0F, 0.2F, false);

		handrail_head_right = new ModelPart(this);
		handrail_head_right.setPivot(0.0F, 0.0F, 2.0F);
		front_seat_handrail.addChild(handrail_head_right);
		handrail_head_right.setTextureOffset(0, 0).addCuboid(-11.0F, -39.06F, 10.5F, 0.0F, 32.0F, 0.0F, 0.2F, true);

		cube_r76 = new ModelPart(this);
		cube_r76.setPivot(-10.6875F, -6.95F, 10.55F);
		handrail_head_right.addChild(cube_r76);
		setRotationAngle(cube_r76, -0.8598F, -0.6048F, -3.0693F);
		cube_r76.setTextureOffset(0, 0).addCuboid(0.2075F, 0.2F, -1.2F, 0.0F, 0.0F, 1.0F, 0.2F, false);

		cube_r77 = new ModelPart(this);
		cube_r77.setPivot(-12.375F, -7.3125F, 4.8125F);
		handrail_head_right.addChild(cube_r77);
		setRotationAngle(cube_r77, -1.399F, -1.1819F, 1.3833F);
		cube_r77.setTextureOffset(0, 0).addCuboid(1.075F, 0.0025F, -0.0025F, 2.0F, 0.0F, 0.0F, 0.2F, true);

		cube_r78 = new ModelPart(this);
		cube_r78.setPivot(-12.75F, -6.125F, 5.9375F);
		handrail_head_right.addChild(cube_r78);
		setRotationAngle(cube_r78, 0.0F, -1.2654F, 0.0F);
		cube_r78.setTextureOffset(0, 0).addCuboid(2.0F, 0.005F, -0.0075F, 2.0F, 0.0F, 0.0F, 0.2F, true);

		seat_left = new ModelPart(this);
		seat_left.setPivot(-1.25F, -1.75F, 2.0F);
		front_seat_handrail.addChild(seat_left);
		seat_left.setTextureOffset(0, 0).addCuboid(19.75F, -17.25F, -8.0F, 1.0F, 13.0F, 18.0F, 0.0F, false);

		cube_r79 = new ModelPart(this);
		cube_r79.setPivot(-0.5F, 0.0F, -18.0F);
		seat_left.addChild(cube_r79);
		setRotationAngle(cube_r79, 0.0F, 0.0F, 0.0873F);
		cube_r79.setTextureOffset(0, 0).addCuboid(13.0F, -7.0F, 10.0F, 7.0F, 1.0F, 18.0F, 0.0F, false);

		seat_right = new ModelPart(this);
		seat_right.setPivot(1.75F, -1.75F, 2.0F);
		front_seat_handrail.addChild(seat_right);
		seat_right.setTextureOffset(0, 0).addCuboid(-21.25F, -17.25F, -8.0F, 1.0F, 13.0F, 18.0F, 0.0F, true);

		cube_r80 = new ModelPart(this);
		cube_r80.setPivot(0.0F, 0.0F, -18.0F);
		seat_right.addChild(cube_r80);
		setRotationAngle(cube_r80, 0.0F, 0.0F, -0.0873F);
		cube_r80.setTextureOffset(0, 0).addCuboid(-20.0F, -7.0F, 10.0F, 7.0F, 1.0F, 18.0F, 0.0F, true);

		roof_head = new ModelPart(this);
		roof_head.setPivot(0.0F, 24.0F, 0.0F);
		roof_head.setTextureOffset(98, 73).addCuboid(-17.5F, -39.0F, -43.0F, 35.0F, 0.0F, 56.0F, 0.0F, false);
		roof_head.setTextureOffset(98, 73).addCuboid(-17.5F, -39.0F, -43.0F, 35.0F, 3.0F, 67.0F, 0.0F, false);

		roof_window = new ModelPart(this);
		roof_window.setPivot(0.0F, 24.0F, 0.0F);
		roof_window.setTextureOffset(98, 73).addCuboid(-17.5F, -39.0F, -50.0F, 35.0F, 0.0F, 100.0F, 0.0F, false);

		roof_end = new ModelPart(this);
		roof_end.setPivot(0.0F, 24.0F, 0.0F);
		roof_end.setTextureOffset(98, 73).addCuboid(-17.5F, -39.0F, -43.0F, 35.0F, 0.0F, 56.0F, 0.0F, false);
		roof_end.setTextureOffset(98, 73).addCuboid(-17.5F, -39.0F, -43.0F, 35.0F, 3.0F, 67.0F, 0.0F, false);

		roof_door = new ModelPart(this);
		roof_door.setPivot(0.0F, 24.0F, 0.0F);
		roof_door.setTextureOffset(98, 73).addCuboid(-17.5F, -39.0F, -16.0F, 35.0F, 0.0F, 32.0F, 0.0F, false);

		head = new ModelPart(this);
		head.setPivot(0.0F, 24.0F, 0.0F);
		head.setTextureOffset(98, 73).addCuboid(-22.0F, 0.0F, -38.0F, 44.0F, 1.0F, 46.0F, 0.0F, false);
		head.setTextureOffset(0, 51).addCuboid(-22.15F, -10.9F, -41.0F, 2.0F, 11.0F, 54.0F, 0.0F, false);
		head.setTextureOffset(0, 51).addCuboid(20.15F, -10.9F, -43.0F, 2.0F, 11.0F, 56.0F, 0.0F, true);

		wall_right_2_r5 = new ModelPart(this);
		wall_right_2_r5.setPivot(-3.2878F, -12.5941F, -3.0F);
		head.addChild(wall_right_2_r5);
		setRotationAngle(wall_right_2_r5, 0.0F, 0.0F, -0.1309F);
		wall_right_2_r5.setTextureOffset(0, 51).addCuboid(22.9978F, -24.0059F, -38.0F, 2.0F, 29.0F, 54.0F, 0.0F, true);

		wall_left_1_r5 = new ModelPart(this);
		wall_left_1_r5.setPivot(3.2878F, -12.5941F, -3.0F);
		head.addChild(wall_left_1_r5);
		setRotationAngle(wall_left_1_r5, 0.0F, 0.0F, 0.1309F);
		wall_left_1_r5.setTextureOffset(0, 51).addCuboid(-24.9978F, -23.9959F, -38.0F, 2.0F, 29.0F, 54.0F, 0.0F, false);

		front_top_r1 = new ModelPart(this);
		front_top_r1.setPivot(0.0F, -58.0F, 16.0F);
		head.addChild(front_top_r1);
		setRotationAngle(front_top_r1, 1.5708F, 0.0F, 0.0F);
		front_top_r1.setTextureOffset(0, 33).addCuboid(-7.0F, -27.0F, -22.0F, 14.0F, 5.0F, 0.0F, 0.0F, false);

		head_bottom = new ModelPart(this);
		head_bottom.setPivot(0.0F, 0.0F, 21.0F);
		head.addChild(head_bottom);
		head_bottom.setTextureOffset(0, 0).addCuboid(18.0F, -4.0F, -27.0F, 3.0F, 4.0F, 19.0F, 0.0F, false);
		head_bottom.setTextureOffset(0, 0).addCuboid(-21.0F, -4.0F, -27.0F, 3.0F, 4.0F, 19.0F, 0.0F, true);

		front_bottom_3_r1 = new ModelPart(this);
		front_bottom_3_r1.setPivot(-19.75F, -4.25F, -14.5F);
		head_bottom.addChild(front_bottom_3_r1);
		setRotationAngle(front_bottom_3_r1, 0.0F, 0.0F, 0.6981F);
		front_bottom_3_r1.setTextureOffset(0, 0).addCuboid(-1.5F, -1.0F, -12.5F, 3.0F, 2.0F, 19.0F, 0.0F, true);

		front_bottom_2_r1 = new ModelPart(this);
		front_bottom_2_r1.setPivot(19.75F, -4.25F, -16.5F);
		head_bottom.addChild(front_bottom_2_r1);
		setRotationAngle(front_bottom_2_r1, 0.0F, 0.0F, -0.6981F);
		front_bottom_2_r1.setTextureOffset(0, 0).addCuboid(-1.5F, -1.0F, -10.5F, 3.0F, 2.0F, 19.0F, 0.0F, false);

		interor_cab_wall = new ModelPart(this);
		interor_cab_wall.setPivot(0.0F, 0.0F, 2.0F);
		head.addChild(interor_cab_wall);
		

		cab_door_back = new ModelPart(this);
		cab_door_back.setPivot(0.0F, 0.0F, -13.0F);
		interor_cab_wall.addChild(cab_door_back);
		setRotationAngle(cab_door_back, -3.1416F, 0.0F, 3.1416F);
		cab_door_back.setTextureOffset(121, 115).addCuboid(-7.0F, -36.0F, -1.0F, 14.0F, 36.0F, 0.0F, 0.0F, false);
		cab_door_back.setTextureOffset(145, 115).addCuboid(-22.0F, -36.0F, -5.0F, 15.0F, 36.0F, 0.0F, 0.0F, false);
		cab_door_back.setTextureOffset(145, 115).addCuboid(7.0F, -36.0F, -5.0F, 15.0F, 36.0F, 0.0F, 0.0F, false);
		cab_door_back.setTextureOffset(145, 115).addCuboid(-22.0F, -39.0F, -5.0F, 44.0F, 3.0F, 0.0F, 0.0F, false);

		front_inside_2_r1 = new ModelPart(this);
		front_inside_2_r1.setPivot(1.0F, 0.0F, 0.0F);
		cab_door_back.addChild(front_inside_2_r1);
		setRotationAngle(front_inside_2_r1, 0.0F, -1.5708F, 0.0F);
		front_inside_2_r1.setTextureOffset(121, 115).addCuboid(-5.0F, -36.0F, -6.0F, 5.0F, 36.0F, 0.0F, 0.0F, false);

		front_inside_1_r1 = new ModelPart(this);
		front_inside_1_r1.setPivot(-1.0F, 0.0F, 0.0F);
		cab_door_back.addChild(front_inside_1_r1);
		setRotationAngle(front_inside_1_r1, 0.0F, -1.5708F, 0.0F);
		front_inside_1_r1.setTextureOffset(121, 115).addCuboid(-5.0F, -36.0F, 6.0F, 5.0F, 36.0F, 0.0F, 0.0F, false);

		head_exterior = new ModelPart(this);
		head_exterior.setPivot(0.0F, 24.0F, 0.0F);
		head_exterior.setTextureOffset(0, 124).addCuboid(-21.75F, 0.0F, -43.0F, 1.0F, 3.0F, 51.0F, 0.0F, false);
		head_exterior.setTextureOffset(39, 91).addCuboid(20.75F, 0.0F, -43.0F, 1.0F, 3.0F, 51.0F, 0.0F, false);
		head_exterior.setTextureOffset(39, 91).addCuboid(16.8F, 3.0F, -43.0F, 4.0F, 3.0F, 17.0F, 0.0F, false);
		head_exterior.setTextureOffset(39, 91).addCuboid(-20.8F, 3.0F, -43.0F, 4.0F, 3.0F, 17.0F, 0.0F, true);

		buttom_panel_6_r2 = new ModelPart(this);
		buttom_panel_6_r2.setPivot(-21.5625F, 4.1F, -36.0F);
		head_exterior.addChild(buttom_panel_6_r2);
		setRotationAngle(buttom_panel_6_r2, 0.0F, 0.0F, -0.1309F);
		buttom_panel_6_r2.setTextureOffset(114, 0).addCuboid(0.0F, -4.1F, -7.0F, 1.0F, 3.0F, 6.0F, 0.0F, true);

		buttom_panel_5_r2 = new ModelPart(this);
		buttom_panel_5_r2.setPivot(21.5625F, 4.1F, -36.0F);
		head_exterior.addChild(buttom_panel_5_r2);
		setRotationAngle(buttom_panel_5_r2, 0.0F, 0.0F, 0.1309F);
		buttom_panel_5_r2.setTextureOffset(114, 0).addCuboid(-1.0F, -4.1F, -7.0F, 1.0F, 3.0F, 6.0F, 0.0F, false);

		floor_5_r1 = new ModelPart(this);
		floor_5_r1.setPivot(-20.375F, 4.0F, -17.0F);
		head_exterior.addChild(floor_5_r1);
		setRotationAngle(floor_5_r1, 0.0F, 0.0F, -0.3054F);
		floor_5_r1.setTextureOffset(39, 91).addCuboid(-0.925F, -1.5F, -26.0F, 2.0F, 3.0F, 17.0F, 0.0F, true);

		floor_4_r1 = new ModelPart(this);
		floor_4_r1.setPivot(20.375F, 4.0F, -17.0F);
		head_exterior.addChild(floor_4_r1);
		setRotationAngle(floor_4_r1, 0.0F, 0.0F, 0.3054F);
		floor_4_r1.setTextureOffset(39, 91).addCuboid(-1.075F, -1.5F, -26.0F, 2.0F, 3.0F, 17.0F, 0.0F, false);

		head_bottom_out = new ModelPart(this);
		head_bottom_out.setPivot(0.0F, 0.1F, -23.0F);
		head_exterior.addChild(head_bottom_out);
		head_bottom_out.setTextureOffset(114, 0).addCuboid(-6.5F, -0.1F, -23.0F, 13.0F, 3.0F, 9.0F, 0.0F, false);
		head_bottom_out.setTextureOffset(114, 0).addCuboid(-21.5F, -0.1F, -20.0F, 7.0F, 3.0F, 8.0F, 0.0F, false);
		head_bottom_out.setTextureOffset(114, 0).addCuboid(14.5F, -0.1F, -20.0F, 7.0F, 3.0F, 8.0F, 0.0F, false);

		buttom_panel_right_6_r2 = new ModelPart(this);
		buttom_panel_right_6_r2.setPivot(9.7131F, 1.4F, -20.8861F);
		head_bottom_out.addChild(buttom_panel_right_6_r2);
		setRotationAngle(buttom_panel_right_6_r2, 0.0F, -0.1745F, 0.0F);
		buttom_panel_right_6_r2.setTextureOffset(0, 43).addCuboid(-3.5031F, -1.5F, -0.4939F, 10.0F, 3.0F, 8.0F, 0.0F, true);

		buttom_panel_right_5_r2 = new ModelPart(this);
		buttom_panel_right_5_r2.setPivot(-9.7131F, 1.4F, -20.8861F);
		head_bottom_out.addChild(buttom_panel_right_5_r2);
		setRotationAngle(buttom_panel_right_5_r2, 0.0F, 0.1745F, 0.0F);
		buttom_panel_right_5_r2.setTextureOffset(0, 43).addCuboid(-6.4969F, -1.5F, -0.4939F, 10.0F, 3.0F, 8.0F, 0.0F, false);

		walls = new ModelPart(this);
		walls.setPivot(0.0F, -1.0F, 0.0F);
		head_exterior.addChild(walls);
		walls.setTextureOffset(0, 51).addCuboid(-22.15F, -9.9F, -43.0F, 2.0F, 11.0F, 56.0F, 0.0F, false);
		walls.setTextureOffset(0, 51).addCuboid(20.15F, -9.9F, -43.0F, 2.0F, 11.0F, 56.0F, 0.0F, true);

		bottom_wall_right_r4 = new ModelPart(this);
		bottom_wall_right_r4.setPivot(0.4068F, 5.1977F, -18.0F);
		walls.addChild(bottom_wall_right_r4);
		setRotationAngle(bottom_wall_right_r4, -0.1309F, 1.5708F, 0.0F);
		bottom_wall_right_r4.setTextureOffset(0, 51).addCuboid(-26.0068F, -7.0007F, 21.0F, 51.0F, 3.0F, 0.0F, 0.0F, true);

		bottom_wall_left_r3 = new ModelPart(this);
		bottom_wall_left_r3.setPivot(-0.4068F, 5.1977F, -18.0F);
		walls.addChild(bottom_wall_left_r3);
		setRotationAngle(bottom_wall_left_r3, -0.1309F, -1.5708F, 0.0F);
		bottom_wall_left_r3.setTextureOffset(0, 51).addCuboid(-25.0032F, -7.0007F, 21.0F, 51.0F, 3.0F, 0.0F, 0.0F, false);

		wall_right_2_r6 = new ModelPart(this);
		wall_right_2_r6.setPivot(-3.2878F, -11.5941F, -3.0F);
		walls.addChild(wall_right_2_r6);
		setRotationAngle(wall_right_2_r6, 0.0F, 0.0F, -0.1309F);
		wall_right_2_r6.setTextureOffset(0, 51).addCuboid(22.9978F, -23.9959F, -40.0F, 2.0F, 29.0F, 56.0F, 0.0F, true);

		wall_left_1_r6 = new ModelPart(this);
		wall_left_1_r6.setPivot(3.2878F, -11.5941F, -3.0F);
		walls.addChild(wall_left_1_r6);
		setRotationAngle(wall_left_1_r6, 0.0F, 0.0F, 0.1309F);
		wall_left_1_r6.setTextureOffset(0, 51).addCuboid(-24.9978F, -23.9959F, -40.0F, 2.0F, 29.0F, 56.0F, 0.0F, false);

		exteror_cab_front = new ModelPart(this);
		exteror_cab_front.setPivot(0.0F, 0.0F, -2.0F);
		head_exterior.addChild(exteror_cab_front);
		exteror_cab_front.setTextureOffset(145, 115).addCuboid(-22.0F, -42.0F, -41.0F, 15.0F, 42.0F, 0.0F, 0.0F, false);
		exteror_cab_front.setTextureOffset(145, 115).addCuboid(7.0F, -42.0F, -41.0F, 15.0F, 42.0F, 0.0F, 0.0F, false);
		exteror_cab_front.setTextureOffset(0, 33).addCuboid(-7.0F, -42.0F, -41.0F, 14.0F, 6.0F, 0.0F, 0.0F, false);

		front_in_1_r1 = new ModelPart(this);
		front_in_1_r1.setPivot(0.0F, -59.0F, -7.0F);
		exteror_cab_front.addChild(front_in_1_r1);
		setRotationAngle(front_in_1_r1, 1.5708F, 0.0F, 0.0F);
		front_in_1_r1.setTextureOffset(0, 33).addCuboid(-7.0F, -34.0F, -23.0F, 14.0F, 5.0F, 0.0F, 0.0F, false);

		cab_door = new ModelPart(this);
		cab_door.setPivot(0.0F, 0.0F, 0.0F);
		exteror_cab_front.addChild(cab_door);
		cab_door.setTextureOffset(121, 115).addCuboid(-7.0F, -36.0F, -37.0F, 14.0F, 36.0F, 0.0F, 0.0F, false);

		front_in_1_r2 = new ModelPart(this);
		front_in_1_r2.setPivot(1.0F, 0.0F, -18.0F);
		cab_door.addChild(front_in_1_r2);
		setRotationAngle(front_in_1_r2, 0.0F, -1.5708F, 0.0F);
		front_in_1_r2.setTextureOffset(121, 115).addCuboid(-23.0F, -36.0F, -6.0F, 5.0F, 36.0F, 0.0F, 0.0F, false);

		front_in_2_r1 = new ModelPart(this);
		front_in_2_r1.setPivot(-1.0F, 0.0F, -18.0F);
		cab_door.addChild(front_in_2_r1);
		setRotationAngle(front_in_2_r1, 0.0F, -1.5708F, 0.0F);
		front_in_2_r1.setTextureOffset(121, 115).addCuboid(-23.0F, -36.0F, 6.0F, 5.0F, 36.0F, 0.0F, 0.0F, false);

		front_train_barrier = new ModelPart(this);
		front_train_barrier.setPivot(0.0F, 0.0F, 0.0F);
		head_exterior.addChild(front_train_barrier);
		front_train_barrier.setTextureOffset(0, 0).addCuboid(-20.75F, -14.0F, -46.0F, 0.0F, 10.0F, 3.0F, 0.3F, false);
		front_train_barrier.setTextureOffset(0, 0).addCuboid(-7.75F, -14.0F, -46.0F, 0.0F, 5.0F, 3.0F, 0.3F, false);
		front_train_barrier.setTextureOffset(0, 0).addCuboid(20.75F, -14.0F, -46.0F, 0.0F, 10.0F, 3.0F, 0.3F, true);
		front_train_barrier.setTextureOffset(0, 0).addCuboid(7.75F, -14.0F, -46.0F, 0.0F, 13.0F, 3.0F, 0.3F, true);

		roof_window_light = new ModelPart(this);
		roof_window_light.setPivot(0.0F, 24.0F, 0.0F);
		roof_window_light.setTextureOffset(98, 73).addCuboid(11.75F, -39.5F, -50.0F, 2.0F, 1.0F, 100.0F, 0.0F, false);
		roof_window_light.setTextureOffset(98, 73).addCuboid(-13.75F, -39.5F, -50.0F, 2.0F, 1.0F, 100.0F, 0.0F, true);

		roof_door_light = new ModelPart(this);
		roof_door_light.setPivot(0.0F, 24.0F, 0.0F);
		roof_door_light.setTextureOffset(98, 73).addCuboid(11.75F, -39.5F, -16.0F, 2.0F, 1.0F, 32.0F, 0.0F, false);
		roof_door_light.setTextureOffset(98, 73).addCuboid(-13.75F, -39.5F, -16.0F, 2.0F, 1.0F, 32.0F, 0.0F, true);

		roof_head_light = new ModelPart(this);
		roof_head_light.setPivot(0.0F, 27.0F, 0.0F);
		roof_head_light.setTextureOffset(98, 73).addCuboid(11.75F, -39.5F, -6.0F, 2.0F, 1.0F, 30.0F, 0.0F, false);
		roof_head_light.setTextureOffset(98, 73).addCuboid(-13.75F, -39.5F, -6.0F, 2.0F, 1.0F, 30.0F, 0.0F, true);

		roof_end_light = new ModelPart(this);
		roof_end_light.setPivot(0.0F, 27.0F, 0.0F);
		roof_end_light.setTextureOffset(98, 73).addCuboid(11.75F, -39.5F, -39.0F, 2.0F, 1.0F, 63.0F, 0.0F, false);
		roof_end_light.setTextureOffset(98, 73).addCuboid(-13.75F, -39.5F, -39.0F, 2.0F, 1.0F, 63.0F, 0.0F, true);

		tail_lights = new ModelPart(this);
		tail_lights.setPivot(0.0F, 24.0F, 0.0F);
		tail_lights.setTextureOffset(0, 0).addCuboid(-19.0F, -10.0F, -43.25F, 4.0F, 4.0F, 0.0F, 0.0F, false);
		tail_lights.setTextureOffset(0, 0).addCuboid(15.0F, -10.0F, -43.25F, 4.0F, 4.0F, 0.0F, 0.0F, false);
		tail_lights.setTextureOffset(0, 0).addCuboid(-17.0F, -39.0F, -43.25F, 3.0F, 3.0F, 0.0F, 0.0F, false);
		tail_lights.setTextureOffset(0, 0).addCuboid(14.0F, -39.0F, -43.25F, 3.0F, 3.0F, 0.0F, 0.0F, false);

		headlights = new ModelPart(this);
		headlights.setPivot(0.0F, 24.0F, 0.0F);
		headlights.setTextureOffset(0, 0).addCuboid(10.5F, -8.0F, -43.25F, 4.0F, 4.0F, 0.0F, 0.0F, false);
		headlights.setTextureOffset(0, 0).addCuboid(-14.5F, -8.0F, -43.25F, 4.0F, 4.0F, 0.0F, 0.0F, false);
		headlights.setTextureOffset(0, 0).addCuboid(-13.5F, -39.0F, -43.25F, 3.0F, 3.0F, 0.0F, 0.0F, false);
		headlights.setTextureOffset(0, 0).addCuboid(10.5F, -39.0F, -43.25F, 3.0F, 3.0F, 0.0F, 0.0F, false);

		headlights_end = new ModelPart(this);
		headlights_end.setPivot(0.0F, 24.0F, 0.0F);
		headlights_end.setTextureOffset(0, 0).addCuboid(10.5F, -8.0F, -43.25F, 4.0F, 4.0F, 0.0F, 0.0F, false);
		headlights_end.setTextureOffset(0, 0).addCuboid(-14.5F, -8.0F, -43.25F, 4.0F, 4.0F, 0.0F, 0.0F, false);
		headlights_end.setTextureOffset(0, 0).addCuboid(-13.5F, -39.0F, -43.25F, 3.0F, 3.0F, 0.0F, 0.0F, false);
		headlights_end.setTextureOffset(0, 0).addCuboid(10.5F, -39.0F, -43.25F, 3.0F, 3.0F, 0.0F, 0.0F, false);

		tail_lights_end = new ModelPart(this);
		tail_lights_end.setPivot(0.0F, 24.0F, 0.0F);
		tail_lights_end.setTextureOffset(0, 0).addCuboid(-19.0F, -10.0F, -43.25F, 4.0F, 4.0F, 0.0F, 0.0F, false);
		tail_lights_end.setTextureOffset(0, 0).addCuboid(15.0F, -10.0F, -43.25F, 4.0F, 4.0F, 0.0F, 0.0F, false);
		tail_lights_end.setTextureOffset(0, 0).addCuboid(-17.0F, -39.0F, -43.25F, 3.0F, 3.0F, 0.0F, 0.0F, false);
		tail_lights_end.setTextureOffset(0, 0).addCuboid(14.0F, -39.0F, -43.25F, 3.0F, 3.0F, 0.0F, 0.0F, false);

		door_light = new ModelPart(this);
		door_light.setPivot(0.0F, 24.0F, 0.0F);
		door_light.setTextureOffset(0, 0).addCuboid(-19.25F, -38.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

		door_light_on = new ModelPart(this);
		door_light_on.setPivot(0.0F, 24.0F, 0.0F);
		door_light_on.setTextureOffset(0, 0).addCuboid(-19.26F, -38.5F, -0.5F, 0.0F, 1.0F, 1.0F, 0.0F, false);

		door_light_off = new ModelPart(this);
		door_light_off.setPivot(0.0F, 24.0F, 0.0F);
		door_light_off.setTextureOffset(0, 0).addCuboid(-19.26F, -38.5F, -0.5F, 0.0F, 1.0F, 1.0F, 0.0F, false);

		roof_window_exterior = new ModelPart(this);
		roof_window_exterior.setPivot(0.0F, 24.0F, 0.0F);
		

		top_roof_r1 = new ModelPart(this);
		top_roof_r1.setPivot(-0.5F, -8.55F, 11.0F);
		roof_window_exterior.addChild(top_roof_r1);
		setRotationAngle(top_roof_r1, 0.0F, 0.0F, 1.6144F);
		top_roof_r1.setTextureOffset(80, 56).addCuboid(-33.0F, -3.0F, -61.0F, 0.0F, 4.0F, 100.0F, 0.0F, false);
		top_roof_r1.setTextureOffset(80, 64).addCuboid(-33.0F, -7.0F, -61.0F, 0.0F, 4.0F, 100.0F, 0.0F, false);

		top_roof_r2 = new ModelPart(this);
		top_roof_r2.setPivot(1.0718F, -8.6875F, 11.0F);
		roof_window_exterior.addChild(top_roof_r2);
		setRotationAngle(top_roof_r2, 0.0F, 0.0F, 1.6581F);
		top_roof_r2.setTextureOffset(80, 72).addCuboid(-33.0F, -8.0F, -61.0F, 0.0F, 4.0F, 100.0F, 0.0F, false);

		top_slant_right_r1 = new ModelPart(this);
		top_slant_right_r1.setPivot(-14.9214F, -40.2953F, 0.0F);
		roof_window_exterior.addChild(top_slant_right_r1);
		setRotationAngle(top_slant_right_r1, 0.0F, 0.0F, 1.3875F);
		top_slant_right_r1.setTextureOffset(78, 46).addCuboid(0.0F, -3.5F, -50.0F, 0.0F, 7.0F, 100.0F, 0.0F, true);

		top_roof_r3 = new ModelPart(this);
		top_roof_r3.setPivot(-1.0718F, -8.6865F, 11.0F);
		roof_window_exterior.addChild(top_roof_r3);
		setRotationAngle(top_roof_r3, 0.0F, 0.0F, -1.6581F);
		top_roof_r3.setTextureOffset(80, 72).addCuboid(33.0F, -8.0F, -61.0F, 0.0F, 4.0F, 100.0F, 0.0F, true);

		top_roof_r4 = new ModelPart(this);
		top_roof_r4.setPivot(0.5F, -8.55F, 11.0F);
		roof_window_exterior.addChild(top_roof_r4);
		setRotationAngle(top_roof_r4, 0.0F, 0.0F, -1.6144F);
		top_roof_r4.setTextureOffset(80, 64).addCuboid(33.0F, -7.0F, -61.0F, 0.0F, 4.0F, 100.0F, 0.0F, true);
		top_roof_r4.setTextureOffset(80, 56).addCuboid(33.0F, -3.0F, -61.0F, 0.0F, 4.0F, 100.0F, 0.0F, true);

		top_slant_left_r1 = new ModelPart(this);
		top_slant_left_r1.setPivot(14.9214F, -40.2953F, 0.0F);
		roof_window_exterior.addChild(top_slant_left_r1);
		setRotationAngle(top_slant_left_r1, 0.0F, 0.0F, -1.3875F);
		top_slant_left_r1.setTextureOffset(78, 46).addCuboid(0.0F, -3.5F, -50.0F, 0.0F, 7.0F, 100.0F, 0.0F, false);

		roof_head_exterior = new ModelPart(this);
		roof_head_exterior.setPivot(0.0F, 24.0F, 0.0F);
		

		top_roof_r5 = new ModelPart(this);
		top_roof_r5.setPivot(-0.5F, -8.55F, 11.0F);
		roof_head_exterior.addChild(top_roof_r5);
		setRotationAngle(top_roof_r5, 0.0F, 0.0F, 1.6144F);
		top_roof_r5.setTextureOffset(80, 56).addCuboid(-33.0F, -3.0F, -54.0F, 0.0F, 4.0F, 56.0F, 0.0F, false);
		top_roof_r5.setTextureOffset(80, 64).addCuboid(-33.0F, -7.0F, -54.0F, 0.0F, 4.0F, 56.0F, 0.0F, false);

		top_roof_r6 = new ModelPart(this);
		top_roof_r6.setPivot(1.0718F, -8.6875F, 11.0F);
		roof_head_exterior.addChild(top_roof_r6);
		setRotationAngle(top_roof_r6, 0.0F, 0.0F, 1.6581F);
		top_roof_r6.setTextureOffset(80, 72).addCuboid(-33.0F, -8.0F, -54.0F, 0.0F, 4.0F, 56.0F, 0.0F, false);

		top_slant_right_r2 = new ModelPart(this);
		top_slant_right_r2.setPivot(-14.9214F, -40.2953F, 0.0F);
		roof_head_exterior.addChild(top_slant_right_r2);
		setRotationAngle(top_slant_right_r2, 0.0F, 0.0F, 1.3875F);
		top_slant_right_r2.setTextureOffset(78, 46).addCuboid(0.0F, -3.5F, -43.0F, 0.0F, 7.0F, 56.0F, 0.0F, true);

		top_roof_r7 = new ModelPart(this);
		top_roof_r7.setPivot(-1.0718F, -8.6865F, 11.0F);
		roof_head_exterior.addChild(top_roof_r7);
		setRotationAngle(top_roof_r7, 0.0F, 0.0F, -1.6581F);
		top_roof_r7.setTextureOffset(80, 72).addCuboid(33.0F, -8.0F, -54.0F, 0.0F, 4.0F, 56.0F, 0.0F, true);

		top_roof_r8 = new ModelPart(this);
		top_roof_r8.setPivot(0.5F, -8.55F, 11.0F);
		roof_head_exterior.addChild(top_roof_r8);
		setRotationAngle(top_roof_r8, 0.0F, 0.0F, -1.6144F);
		top_roof_r8.setTextureOffset(80, 64).addCuboid(33.0F, -7.0F, -54.0F, 0.0F, 4.0F, 56.0F, 0.0F, true);
		top_roof_r8.setTextureOffset(80, 56).addCuboid(33.0F, -3.0F, -54.0F, 0.0F, 4.0F, 56.0F, 0.0F, true);

		top_slant_left_r2 = new ModelPart(this);
		top_slant_left_r2.setPivot(14.9214F, -40.2953F, 0.0F);
		roof_head_exterior.addChild(top_slant_left_r2);
		setRotationAngle(top_slant_left_r2, 0.0F, 0.0F, -1.3875F);
		top_slant_left_r2.setTextureOffset(78, 46).addCuboid(0.0F, -3.5F, -43.0F, 0.0F, 7.0F, 56.0F, 0.0F, false);

		roof_end_exterior = new ModelPart(this);
		roof_end_exterior.setPivot(0.0F, 24.0F, 0.0F);
		

		top_roof_r9 = new ModelPart(this);
		top_roof_r9.setPivot(-0.5F, -8.55F, 11.0F);
		roof_end_exterior.addChild(top_roof_r9);
		setRotationAngle(top_roof_r9, 0.0F, 0.0F, 1.6144F);
		top_roof_r9.setTextureOffset(80, 56).addCuboid(-33.0F, -3.0F, -54.0F, 0.0F, 4.0F, 56.0F, 0.0F, false);
		top_roof_r9.setTextureOffset(80, 64).addCuboid(-33.0F, -7.0F, -54.0F, 0.0F, 4.0F, 56.0F, 0.0F, false);

		top_roof_r10 = new ModelPart(this);
		top_roof_r10.setPivot(1.0718F, -8.6875F, 11.0F);
		roof_end_exterior.addChild(top_roof_r10);
		setRotationAngle(top_roof_r10, 0.0F, 0.0F, 1.6581F);
		top_roof_r10.setTextureOffset(80, 72).addCuboid(-33.0F, -8.0F, -54.0F, 0.0F, 4.0F, 56.0F, 0.0F, false);

		top_slant_right_r3 = new ModelPart(this);
		top_slant_right_r3.setPivot(-14.9214F, -40.2953F, 0.0F);
		roof_end_exterior.addChild(top_slant_right_r3);
		setRotationAngle(top_slant_right_r3, 0.0F, 0.0F, 1.3875F);
		top_slant_right_r3.setTextureOffset(78, 46).addCuboid(0.0F, -3.5F, -43.0F, 0.0F, 7.0F, 56.0F, 0.0F, true);

		top_roof_r11 = new ModelPart(this);
		top_roof_r11.setPivot(-1.0718F, -8.6865F, 11.0F);
		roof_end_exterior.addChild(top_roof_r11);
		setRotationAngle(top_roof_r11, 0.0F, 0.0F, -1.6581F);
		top_roof_r11.setTextureOffset(80, 72).addCuboid(33.0F, -8.0F, -54.0F, 0.0F, 4.0F, 56.0F, 0.0F, true);

		top_roof_r12 = new ModelPart(this);
		top_roof_r12.setPivot(0.5F, -8.55F, 11.0F);
		roof_end_exterior.addChild(top_roof_r12);
		setRotationAngle(top_roof_r12, 0.0F, 0.0F, -1.6144F);
		top_roof_r12.setTextureOffset(80, 64).addCuboid(33.0F, -7.0F, -54.0F, 0.0F, 4.0F, 56.0F, 0.0F, true);
		top_roof_r12.setTextureOffset(80, 56).addCuboid(33.0F, -3.0F, -54.0F, 0.0F, 4.0F, 56.0F, 0.0F, true);

		top_slant_left_r3 = new ModelPart(this);
		top_slant_left_r3.setPivot(14.9214F, -40.2953F, 0.0F);
		roof_end_exterior.addChild(top_slant_left_r3);
		setRotationAngle(top_slant_left_r3, 0.0F, 0.0F, -1.3875F);
		top_slant_left_r3.setTextureOffset(78, 46).addCuboid(0.0F, -3.5F, -43.0F, 0.0F, 7.0F, 56.0F, 0.0F, false);

		roof_door_exterior = new ModelPart(this);
		roof_door_exterior.setPivot(0.0F, 24.0F, 0.0F);
		

		top_roof_r13 = new ModelPart(this);
		top_roof_r13.setPivot(-0.5F, -8.55F, 14.0F);
		roof_door_exterior.addChild(top_roof_r13);
		setRotationAngle(top_roof_r13, 0.0F, 0.0F, 1.6144F);
		top_roof_r13.setTextureOffset(80, 56).addCuboid(-33.0F, -3.0F, -30.0F, 0.0F, 4.0F, 32.0F, 0.0F, false);
		top_roof_r13.setTextureOffset(80, 64).addCuboid(-33.0F, -7.0F, -30.0F, 0.0F, 4.0F, 32.0F, 0.0F, false);

		top_roof_r14 = new ModelPart(this);
		top_roof_r14.setPivot(1.0718F, -8.6875F, 14.0F);
		roof_door_exterior.addChild(top_roof_r14);
		setRotationAngle(top_roof_r14, 0.0F, 0.0F, 1.6581F);
		top_roof_r14.setTextureOffset(80, 72).addCuboid(-33.0F, -8.0F, -30.0F, 0.0F, 4.0F, 32.0F, 0.0F, false);

		top_slant_right_r4 = new ModelPart(this);
		top_slant_right_r4.setPivot(-14.9214F, -40.2953F, 3.0F);
		roof_door_exterior.addChild(top_slant_right_r4);
		setRotationAngle(top_slant_right_r4, 0.0F, 0.0F, 1.3875F);
		top_slant_right_r4.setTextureOffset(78, 46).addCuboid(0.0F, -3.5F, -19.0F, 0.0F, 7.0F, 32.0F, 0.0F, true);

		top_roof_r15 = new ModelPart(this);
		top_roof_r15.setPivot(-1.0718F, -8.6865F, 14.0F);
		roof_door_exterior.addChild(top_roof_r15);
		setRotationAngle(top_roof_r15, 0.0F, 0.0F, -1.6581F);
		top_roof_r15.setTextureOffset(80, 72).addCuboid(33.0F, -8.0F, -30.0F, 0.0F, 4.0F, 32.0F, 0.0F, true);

		top_roof_r16 = new ModelPart(this);
		top_roof_r16.setPivot(0.5F, -8.55F, 14.0F);
		roof_door_exterior.addChild(top_roof_r16);
		setRotationAngle(top_roof_r16, 0.0F, 0.0F, -1.6144F);
		top_roof_r16.setTextureOffset(80, 64).addCuboid(33.0F, -7.0F, -30.0F, 0.0F, 4.0F, 32.0F, 0.0F, true);
		top_roof_r16.setTextureOffset(80, 56).addCuboid(33.0F, -3.0F, -30.0F, 0.0F, 4.0F, 32.0F, 0.0F, true);

		top_slant_left_r4 = new ModelPart(this);
		top_slant_left_r4.setPivot(14.9214F, -40.2953F, 3.0F);
		roof_door_exterior.addChild(top_slant_left_r4);
		setRotationAngle(top_slant_left_r4, 0.0F, 0.0F, -1.3875F);
		top_slant_left_r4.setTextureOffset(78, 46).addCuboid(0.0F, -3.5F, -19.0F, 0.0F, 7.0F, 32.0F, 0.0F, false);

		end_side_panels = new ModelPart(this);
		end_side_panels.setPivot(0.0F, 24.0F, 0.0F);
		end_side_panels.setTextureOffset(0, 0).addCuboid(11.0F, -25.0F, 12.5F, 9.0F, 19.0F, 0.0F, 0.0F, false);
		end_side_panels.setTextureOffset(0, 0).addCuboid(-20.0F, -25.0F, 12.5F, 9.0F, 19.0F, 0.0F, 0.0F, true);

		window_side_panels = new ModelPart(this);
		window_side_panels.setPivot(0.0F, 24.0F, 0.0F);
		window_side_panels.setTextureOffset(0, 0).addCuboid(11.0F, -25.0F, -49.5F, 9.0F, 19.0F, 0.0F, 0.0F, false);
		window_side_panels.setTextureOffset(0, 0).addCuboid(11.0F, -25.0F, 49.5F, 9.0F, 19.0F, 0.0F, 0.0F, false);
		window_side_panels.setTextureOffset(0, 0).addCuboid(-20.0F, -25.0F, 49.5F, 9.0F, 19.0F, 0.0F, 0.0F, true);

		head_side_panels = new ModelPart(this);
		head_side_panels.setPivot(0.0F, 24.0F, 0.0F);
		head_side_panels.setTextureOffset(0, 0).addCuboid(11.0F, -25.0F, 12.5F, 9.0F, 19.0F, 0.0F, 0.0F, false);
		head_side_panels.setTextureOffset(0, 0).addCuboid(-20.0F, -25.0F, 12.5F, 9.0F, 19.0F, 0.0F, 0.0F, true);
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
					renderOnce(window, matrices, vertices, light, position);
					if (renderDetails) {
						renderOnce(roof_window, matrices, vertices, light, position);
						if (frontWindow) {
							renderOnceFlipped(window_display, matrices, vertices, light, position);
							renderOnceFlipped(window_seat_handrail, matrices, vertices, light, position);
						}
						if (endWindow) {
							renderOnce(window_display, matrices, vertices, light, position);
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
					renderOnce(window_exterior, matrices, vertices, light, position);
					renderOnce(roof_window_exterior, matrices, vertices, light, position);
					break;
			}
		}

		@Override
		protected void renderDoorPositions(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
			final boolean middleDoor = isIndex(getDoorPositions().length / 2, position, getDoorPositions());
			final boolean doorOpen = doorLeftZ > 0 || doorRightZ > 0;

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
			return new int[]{-61, 61};
		}

		@Override
		protected int[] getDoorPositions() {
			return new int[]{-122, 0, 122};
		}

		@Override
		protected int[] getEndPositions() {
			return new int[]{-146, 146};
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