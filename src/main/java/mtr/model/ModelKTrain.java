package mtr.model;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class ModelKTrain extends ModelTrainBase {

	private final ModelPart window;
	private final ModelPart handrail_8_r1;
	private final ModelPart top_handrail_6_r1;
	private final ModelPart top_handrail_5_r1;
	private final ModelPart top_handrail_4_r1;
	private final ModelPart top_handrail_3_r1;
	private final ModelPart top_handrail_2_r1;
	private final ModelPart top_handrail_1_r1;
	private final ModelPart handrail_5_r1;
	private final ModelPart upper_wall_2_r1;
	private final ModelPart seat;
	private final ModelPart seat_back_r1;
	private final ModelPart window_exterior;
	private final ModelPart upper_wall_r1;
	private final ModelPart side_panel;
	private final ModelPart roof_window;
	private final ModelPart inner_roof_4_r1;
	private final ModelPart inner_roof_3_r1;
	private final ModelPart roof_door;
	private final ModelPart inner_roof_4_r2;
	private final ModelPart inner_roof_3_r2;
	private final ModelPart handrail_2_r1;
	private final ModelPart roof_exterior;
	private final ModelPart outer_roof_5_r1;
	private final ModelPart outer_roof_4_r1;
	private final ModelPart outer_roof_3_r1;
	private final ModelPart door;
	private final ModelPart door_left;
	private final ModelPart door_left_top_r1;
	private final ModelPart door_right;
	private final ModelPart door_right_top_r1;
	private final ModelPart door_exterior;
	private final ModelPart upper_wall_r2;
	private final ModelPart door_left_exterior;
	private final ModelPart door_left_top_r2;
	private final ModelPart door_right_exterior;
	private final ModelPart door_right_top_r2;
	private final ModelPart end;
	private final ModelPart upper_wall_2_r2;
	private final ModelPart upper_wall_1_r1;
	private final ModelPart lower_wall_1_r1;
	private final ModelPart end_exterior;
	private final ModelPart upper_wall_2_r3;
	private final ModelPart upper_wall_1_r2;
	private final ModelPart roof_end;
	private final ModelPart handrail_2_r2;
	private final ModelPart inner_roof_1;
	private final ModelPart inner_roof_4_r3;
	private final ModelPart inner_roof_3_r3;
	private final ModelPart inner_roof_2;
	private final ModelPart inner_roof_4_r4;
	private final ModelPart inner_roof_3_r4;
	private final ModelPart roof_end_exterior;
	private final ModelPart vent_2_r1;
	private final ModelPart vent_1_r1;
	private final ModelPart outer_roof_1;
	private final ModelPart outer_roof_5_r2;
	private final ModelPart outer_roof_4_r2;
	private final ModelPart outer_roof_3_r2;
	private final ModelPart outer_roof_2;
	private final ModelPart outer_roof_5_r3;
	private final ModelPart outer_roof_4_r3;
	private final ModelPart outer_roof_3_r3;
	private final ModelPart roof_light;
	private final ModelPart roof_light_r1;
	private final ModelPart roof_end_light;
	private final ModelPart roof_light_2_r1;
	private final ModelPart roof_light_1_r1;
	private final ModelPart head;
	private final ModelPart upper_wall_2_r4;
	private final ModelPart upper_wall_1_r3;
	private final ModelPart lower_wall_1_r2;
	private final ModelPart head_exterior;
	private final ModelPart upper_wall_2_r5;
	private final ModelPart upper_wall_1_r4;
	private final ModelPart front;
	private final ModelPart front_bottom_2_r1;
	private final ModelPart front_panel_4_r1;
	private final ModelPart front_panel_3_r1;
	private final ModelPart front_panel_1_r1;
	private final ModelPart side_1;
	private final ModelPart outer_roof_5_r4;
	private final ModelPart outer_roof_4_r4;
	private final ModelPart outer_roof_3_r4;
	private final ModelPart outer_roof_2_r1;
	private final ModelPart front_side_bottom_1_r1;
	private final ModelPart front_side_lower_1_r1;
	private final ModelPart front_side_upper_1_r1;
	private final ModelPart side_2;
	private final ModelPart outer_roof_5_r5;
	private final ModelPart outer_roof_4_r5;
	private final ModelPart outer_roof_3_r5;
	private final ModelPart outer_roof_2_r2;
	private final ModelPart front_side_bottom_2_r1;
	private final ModelPart front_side_upper_2_r1;
	private final ModelPart front_side_lower_2_r1;
	private final ModelPart headlights;
	private final ModelPart tail_lights;
	private final ModelPart door_light;
	private final ModelPart outer_roof_3_r6;
	private final ModelPart door_light_on;
	private final ModelPart light_r1;
	private final ModelPart door_light_off;
	private final ModelPart light_r2;

	public ModelKTrain() {
		textureWidth = 320;
		textureHeight = 320;
		window = new ModelPart(this);
		window.setPivot(0.0F, 24.0F, 0.0F);
		window.setTextureOffset(0, 42).addCuboid(-20.0F, 0.0F, -24.0F, 20.0F, 1.0F, 48.0F, 0.0F, false);
		window.setTextureOffset(148, 169).addCuboid(-21.0F, -14.0F, 21.0F, 4.0F, 14.0F, 6.0F, 0.0F, false);
		window.setTextureOffset(168, 52).addCuboid(-21.0F, -14.0F, -27.0F, 4.0F, 14.0F, 6.0F, 0.0F, false);
		window.setTextureOffset(8, 0).addCuboid(0.0F, -35.0F, -9.0F, 0.0F, 35.0F, 0.0F, 0.2F, false);
		window.setTextureOffset(8, 0).addCuboid(0.0F, -35.0F, 9.0F, 0.0F, 35.0F, 0.0F, 0.2F, false);
		window.setTextureOffset(29, 0).addCuboid(-1.0F, -32.0F, 21.0F, 2.0F, 4.0F, 0.0F, 0.0F, false);
		window.setTextureOffset(29, 0).addCuboid(-1.0F, -32.0F, 15.0F, 2.0F, 4.0F, 0.0F, 0.0F, false);
		window.setTextureOffset(29, 0).addCuboid(-1.0F, -32.0F, 3.0F, 2.0F, 4.0F, 0.0F, 0.0F, false);
		window.setTextureOffset(29, 0).addCuboid(-1.0F, -32.0F, -3.0F, 2.0F, 4.0F, 0.0F, 0.0F, false);
		window.setTextureOffset(29, 0).addCuboid(-1.0F, -32.0F, -15.0F, 2.0F, 4.0F, 0.0F, 0.0F, false);
		window.setTextureOffset(29, 0).addCuboid(-1.0F, -32.0F, -21.0F, 2.0F, 4.0F, 0.0F, 0.0F, false);

		handrail_8_r1 = new ModelPart(this);
		handrail_8_r1.setPivot(0.0F, 0.0F, 0.0F);
		window.addChild(handrail_8_r1);
		setRotationAngle(handrail_8_r1, -1.5708F, 0.0F, 0.0F);
		handrail_8_r1.setTextureOffset(0, 0).addCuboid(0.0F, -24.0F, -31.5F, 0.0F, 48.0F, 0.0F, 0.2F, false);

		top_handrail_6_r1 = new ModelPart(this);
		top_handrail_6_r1.setPivot(-12.0518F, -29.0895F, 9.5876F);
		window.addChild(top_handrail_6_r1);
		setRotationAngle(top_handrail_6_r1, 1.5708F, 0.0F, -0.0436F);
		top_handrail_6_r1.setTextureOffset(0, 0).addCuboid(0.0F, -9.5F, 0.0F, 0.0F, 20.0F, 0.0F, 0.2F, false);

		top_handrail_5_r1 = new ModelPart(this);
		top_handrail_5_r1.setPivot(-12.0377F, -28.7666F, 20.7938F);
		window.addChild(top_handrail_5_r1);
		setRotationAngle(top_handrail_5_r1, 1.0472F, 0.0F, -0.0436F);
		top_handrail_5_r1.setTextureOffset(0, 0).addCuboid(0.0F, -0.5F, 0.0F, 0.0F, 1.0F, 0.0F, 0.2F, false);

		top_handrail_4_r1 = new ModelPart(this);
		top_handrail_4_r1.setPivot(-11.9992F, -27.8844F, 21.6768F);
		window.addChild(top_handrail_4_r1);
		setRotationAngle(top_handrail_4_r1, 0.5236F, 0.0F, -0.0436F);
		top_handrail_4_r1.setTextureOffset(0, 0).addCuboid(0.0F, -0.5F, 0.0F, 0.0F, 1.0F, 0.0F, 0.2F, false);

		top_handrail_3_r1 = new ModelPart(this);
		top_handrail_3_r1.setPivot(-12.0518F, -29.0895F, -9.5876F);
		window.addChild(top_handrail_3_r1);
		setRotationAngle(top_handrail_3_r1, -1.5708F, 0.0F, -0.0436F);
		top_handrail_3_r1.setTextureOffset(0, 0).addCuboid(0.0F, -9.5F, 0.0F, 0.0F, 20.0F, 0.0F, 0.2F, false);

		top_handrail_2_r1 = new ModelPart(this);
		top_handrail_2_r1.setPivot(-12.0377F, -28.7666F, -20.7938F);
		window.addChild(top_handrail_2_r1);
		setRotationAngle(top_handrail_2_r1, -1.0472F, 0.0F, -0.0436F);
		top_handrail_2_r1.setTextureOffset(0, 0).addCuboid(0.0F, -0.5F, 0.0F, 0.0F, 1.0F, 0.0F, 0.2F, false);

		top_handrail_1_r1 = new ModelPart(this);
		top_handrail_1_r1.setPivot(-11.9992F, -27.8844F, -21.6768F);
		window.addChild(top_handrail_1_r1);
		setRotationAngle(top_handrail_1_r1, -0.5236F, 0.0F, -0.0436F);
		top_handrail_1_r1.setTextureOffset(0, 0).addCuboid(0.0F, -0.5F, 0.0F, 0.0F, 1.0F, 0.0F, 0.2F, false);

		handrail_5_r1 = new ModelPart(this);
		handrail_5_r1.setPivot(-11.0F, -5.0F, 0.0F);
		window.addChild(handrail_5_r1);
		setRotationAngle(handrail_5_r1, 0.0F, 0.0F, -0.0436F);
		handrail_5_r1.setTextureOffset(0, 0).addCuboid(0.0F, -28.2F, -14.0F, 0.0F, 4.0F, 0.0F, 0.2F, false);
		handrail_5_r1.setTextureOffset(0, 0).addCuboid(0.0F, -28.2F, 14.0F, 0.0F, 4.0F, 0.0F, 0.2F, false);
		handrail_5_r1.setTextureOffset(4, 0).addCuboid(0.0F, -22.2F, 22.0F, 0.0F, 22.0F, 0.0F, 0.2F, false);
		handrail_5_r1.setTextureOffset(4, 0).addCuboid(0.0F, -22.2F, -22.0F, 0.0F, 22.0F, 0.0F, 0.2F, false);

		upper_wall_2_r1 = new ModelPart(this);
		upper_wall_2_r1.setPivot(-21.0F, -14.0F, 0.0F);
		window.addChild(upper_wall_2_r1);
		setRotationAngle(upper_wall_2_r1, 0.0F, 0.0F, 0.1107F);
		upper_wall_2_r1.setTextureOffset(120, 166).addCuboid(0.0F, -19.0F, -27.0F, 4.0F, 19.0F, 5.0F, 0.0F, false);
		upper_wall_2_r1.setTextureOffset(211, 91).addCuboid(0.0F, -19.0F, 22.0F, 4.0F, 19.0F, 5.0F, 0.0F, false);
		upper_wall_2_r1.setTextureOffset(0, 129).addCuboid(1.0F, -19.0F, -22.0F, 2.0F, 19.0F, 44.0F, 0.0F, false);

		seat = new ModelPart(this);
		seat.setPivot(0.0F, 0.0F, 0.0F);
		window.addChild(seat);
		seat.setTextureOffset(148, 145).addCuboid(-18.0F, -6.0F, -22.0F, 7.0F, 1.0F, 44.0F, 0.0F, false);
		seat.setTextureOffset(188, 73).addCuboid(-18.0F, -5.0F, -21.0F, 5.0F, 5.0F, 42.0F, 0.0F, false);

		seat_back_r1 = new ModelPart(this);
		seat_back_r1.setPivot(-17.0F, -6.0F, 0.0F);
		seat.addChild(seat_back_r1);
		setRotationAngle(seat_back_r1, 0.0F, 0.0F, -0.0524F);
		seat_back_r1.setTextureOffset(168, 0).addCuboid(-1.0F, -8.0F, -22.0F, 1.0F, 8.0F, 44.0F, 0.0F, false);

		window_exterior = new ModelPart(this);
		window_exterior.setPivot(0.0F, 24.0F, 0.0F);
		window_exterior.setTextureOffset(98, 145).addCuboid(-21.0F, 0.0F, -24.0F, 1.0F, 4.0F, 48.0F, 0.0F, false);
		window_exterior.setTextureOffset(0, 63).addCuboid(-21.0F, -14.0F, -26.0F, 0.0F, 14.0F, 52.0F, 0.0F, false);

		upper_wall_r1 = new ModelPart(this);
		upper_wall_r1.setPivot(-21.0F, -14.0F, 0.0F);
		window_exterior.addChild(upper_wall_r1);
		setRotationAngle(upper_wall_r1, 0.0F, 0.0F, 0.1107F);
		upper_wall_r1.setTextureOffset(0, 41).addCuboid(0.0F, -22.0F, -26.0F, 0.0F, 22.0F, 52.0F, 0.0F, false);

		side_panel = new ModelPart(this);
		side_panel.setPivot(0.0F, 24.0F, 0.0F);
		side_panel.setTextureOffset(30, 143).addCuboid(-18.0F, -34.0F, 0.0F, 7.0F, 30.0F, 0.0F, 0.0F, false);

		roof_window = new ModelPart(this);
		roof_window.setPivot(0.0F, 24.0F, 0.0F);
		roof_window.setTextureOffset(40, 42).addCuboid(-16.0F, -32.0F, -24.0F, 4.0F, 0.0F, 48.0F, 0.0F, false);
		roof_window.setTextureOffset(68, 42).addCuboid(-3.0F, -34.5F, -24.0F, 3.0F, 0.0F, 48.0F, 0.0F, false);

		inner_roof_4_r1 = new ModelPart(this);
		inner_roof_4_r1.setPivot(-5.5473F, -34.2615F, 0.0F);
		roof_window.addChild(inner_roof_4_r1);
		setRotationAngle(inner_roof_4_r1, 0.0F, 0.0F, -0.0873F);
		inner_roof_4_r1.setTextureOffset(56, 42).addCuboid(-3.0F, 0.0F, -24.0F, 6.0F, 0.0F, 48.0F, 0.0F, false);

		inner_roof_3_r1 = new ModelPart(this);
		inner_roof_3_r1.setPivot(-12.0F, -32.0F, 0.0F);
		roof_window.addChild(inner_roof_3_r1);
		setRotationAngle(inner_roof_3_r1, 0.0F, 0.0F, -0.5236F);
		inner_roof_3_r1.setTextureOffset(48, 42).addCuboid(0.0F, 0.0F, -24.0F, 4.0F, 0.0F, 48.0F, 0.0F, false);

		roof_door = new ModelPart(this);
		roof_door.setPivot(0.0F, 24.0F, 0.0F);
		roof_door.setTextureOffset(132, 10).addCuboid(-18.0F, -33.0F, -16.0F, 6.0F, 1.0F, 32.0F, 0.0F, false);
		roof_door.setTextureOffset(84, 50).addCuboid(-3.0F, -34.5F, -16.0F, 3.0F, 0.0F, 32.0F, 0.0F, false);

		inner_roof_4_r2 = new ModelPart(this);
		inner_roof_4_r2.setPivot(-5.5473F, -34.2615F, 0.0F);
		roof_door.addChild(inner_roof_4_r2);
		setRotationAngle(inner_roof_4_r2, 0.0F, 0.0F, -0.0873F);
		inner_roof_4_r2.setTextureOffset(72, 50).addCuboid(-3.0F, 0.0F, -16.0F, 6.0F, 0.0F, 32.0F, 0.0F, false);

		inner_roof_3_r2 = new ModelPart(this);
		inner_roof_3_r2.setPivot(-12.0F, -32.0F, 0.0F);
		roof_door.addChild(inner_roof_3_r2);
		setRotationAngle(inner_roof_3_r2, 0.0F, 0.0F, -0.5236F);
		inner_roof_3_r2.setTextureOffset(128, 93).addCuboid(0.0F, 0.0F, -16.0F, 4.0F, 0.0F, 32.0F, 0.0F, true);

		handrail_2_r1 = new ModelPart(this);
		handrail_2_r1.setPivot(0.0F, 0.0F, 0.0F);
		roof_door.addChild(handrail_2_r1);
		setRotationAngle(handrail_2_r1, -1.5708F, 0.0F, 0.0F);
		handrail_2_r1.setTextureOffset(0, 0).addCuboid(0.0F, -16.0F, -31.5F, 0.0F, 32.0F, 0.0F, 0.2F, false);

		roof_exterior = new ModelPart(this);
		roof_exterior.setPivot(0.0F, 24.0F, 0.0F);
		roof_exterior.setTextureOffset(82, 43).addCuboid(-5.728F, -41.8527F, -20.0F, 6.0F, 0.0F, 40.0F, 0.0F, false);

		outer_roof_5_r1 = new ModelPart(this);
		outer_roof_5_r1.setPivot(-9.6672F, -41.1581F, 0.0F);
		roof_exterior.addChild(outer_roof_5_r1);
		setRotationAngle(outer_roof_5_r1, 0.0F, 0.0F, -0.1745F);
		outer_roof_5_r1.setTextureOffset(82, 93).addCuboid(-4.0F, 0.0F, -20.0F, 8.0F, 0.0F, 40.0F, 0.0F, false);

		outer_roof_4_r1 = new ModelPart(this);
		outer_roof_4_r1.setPivot(-15.3385F, -39.4635F, 0.0F);
		roof_exterior.addChild(outer_roof_4_r1);
		setRotationAngle(outer_roof_4_r1, 0.0F, 0.0F, -0.5236F);
		outer_roof_4_r1.setTextureOffset(0, 42).addCuboid(-2.0F, 0.0F, -20.0F, 4.0F, 0.0F, 40.0F, 0.0F, false);

		outer_roof_3_r1 = new ModelPart(this);
		outer_roof_3_r1.setPivot(-17.8206F, -37.1645F, 0.0F);
		roof_exterior.addChild(outer_roof_3_r1);
		setRotationAngle(outer_roof_3_r1, 0.0F, 0.0F, -1.0472F);
		outer_roof_3_r1.setTextureOffset(98, 93).addCuboid(-1.5F, 0.0F, -20.0F, 3.0F, 0.0F, 40.0F, 0.0F, false);

		door = new ModelPart(this);
		door.setPivot(0.0F, 24.0F, 0.0F);
		door.setTextureOffset(164, 190).addCuboid(-20.0F, 0.0F, -16.0F, 20.0F, 1.0F, 32.0F, 0.0F, false);
		door.setTextureOffset(8, 0).addCuboid(0.0F, -35.0F, 0.0F, 0.0F, 35.0F, 0.0F, 0.2F, false);

		door_left = new ModelPart(this);
		door_left.setPivot(0.0F, 0.0F, 0.0F);
		door.addChild(door_left);
		door_left.setTextureOffset(116, 200).addCuboid(-21.0F, -14.0F, 0.0F, 1.0F, 14.0F, 14.0F, 0.0F, false);

		door_left_top_r1 = new ModelPart(this);
		door_left_top_r1.setPivot(-21.0F, -14.0F, 0.0F);
		door_left.addChild(door_left_top_r1);
		setRotationAngle(door_left_top_r1, 0.0F, 0.0F, 0.1107F);
		door_left_top_r1.setTextureOffset(274, 59).addCuboid(0.0F, -19.0F, 0.0F, 1.0F, 19.0F, 14.0F, 0.0F, false);

		door_right = new ModelPart(this);
		door_right.setPivot(0.0F, 0.0F, 0.0F);
		door.addChild(door_right);
		door_right.setTextureOffset(0, 192).addCuboid(-21.0F, -14.0F, -14.0F, 1.0F, 14.0F, 14.0F, 0.0F, false);

		door_right_top_r1 = new ModelPart(this);
		door_right_top_r1.setPivot(-21.0F, -14.0F, 0.0F);
		door_right.addChild(door_right_top_r1);
		setRotationAngle(door_right_top_r1, 0.0F, 0.0F, 0.1107F);
		door_right_top_r1.setTextureOffset(178, 0).addCuboid(0.0F, -19.0F, -14.0F, 1.0F, 19.0F, 14.0F, 0.0F, false);

		door_exterior = new ModelPart(this);
		door_exterior.setPivot(0.0F, 24.0F, 0.0F);
		door_exterior.setTextureOffset(114, 161).addCuboid(-21.0F, 0.0F, -16.0F, 1.0F, 4.0F, 32.0F, 0.0F, false);

		upper_wall_r2 = new ModelPart(this);
		upper_wall_r2.setPivot(-21.0F, -14.0F, 0.0F);
		door_exterior.addChild(upper_wall_r2);
		setRotationAngle(upper_wall_r2, 0.0F, 0.0F, 0.1107F);
		upper_wall_r2.setTextureOffset(77, 259).addCuboid(0.0F, -22.0F, -14.0F, 1.0F, 3.0F, 28.0F, 0.0F, false);

		door_left_exterior = new ModelPart(this);
		door_left_exterior.setPivot(0.0F, 0.0F, 0.0F);
		door_exterior.addChild(door_left_exterior);
		door_left_exterior.setTextureOffset(0, 135).addCuboid(-21.0F, -14.0F, 0.0F, 0.0F, 14.0F, 14.0F, 0.0F, false);

		door_left_top_r2 = new ModelPart(this);
		door_left_top_r2.setPivot(-21.0F, -14.0F, 0.0F);
		door_left_exterior.addChild(door_left_top_r2);
		setRotationAngle(door_left_top_r2, 0.0F, 0.0F, 0.1107F);
		door_left_top_r2.setTextureOffset(0, 115).addCuboid(0.0F, -20.0F, 0.0F, 0.0F, 20.0F, 14.0F, 0.0F, false);

		door_right_exterior = new ModelPart(this);
		door_right_exterior.setPivot(0.0F, 0.0F, 0.0F);
		door_exterior.addChild(door_right_exterior);
		door_right_exterior.setTextureOffset(262, 229).addCuboid(-21.0F, -14.0F, -14.0F, 0.0F, 14.0F, 14.0F, 0.0F, false);

		door_right_top_r2 = new ModelPart(this);
		door_right_top_r2.setPivot(-21.0F, -14.0F, 0.0F);
		door_right_exterior.addChild(door_right_top_r2);
		setRotationAngle(door_right_top_r2, 0.0F, 0.0F, 0.1107F);
		door_right_top_r2.setTextureOffset(262, 209).addCuboid(0.0F, -20.0F, -14.0F, 0.0F, 20.0F, 14.0F, 0.0F, false);

		end = new ModelPart(this);
		end.setPivot(0.0F, 24.0F, 0.0F);
		end.setTextureOffset(168, 52).addCuboid(-20.0F, 0.0F, -12.0F, 40.0F, 1.0F, 20.0F, 0.0F, false);
		end.setTextureOffset(148, 169).addCuboid(-21.0F, -14.0F, 5.0F, 4.0F, 14.0F, 6.0F, 0.0F, false);
		end.setTextureOffset(51, 234).addCuboid(9.5F, -34.0F, -12.0F, 8.0F, 34.0F, 19.0F, 0.0F, true);
		end.setTextureOffset(51, 234).addCuboid(-17.5F, -34.0F, -12.0F, 8.0F, 34.0F, 19.0F, 0.0F, false);
		end.setTextureOffset(236, 174).addCuboid(-9.5F, -35.0F, -12.0F, 19.0F, 2.0F, 19.0F, 0.0F, false);

		upper_wall_2_r2 = new ModelPart(this);
		upper_wall_2_r2.setPivot(-21.0F, -14.0F, 0.0F);
		end.addChild(upper_wall_2_r2);
		setRotationAngle(upper_wall_2_r2, 0.0F, 0.0F, 0.1107F);
		upper_wall_2_r2.setTextureOffset(211, 91).addCuboid(0.0F, -19.0F, 6.0F, 4.0F, 19.0F, 5.0F, 0.0F, false);

		upper_wall_1_r1 = new ModelPart(this);
		upper_wall_1_r1.setPivot(21.0F, -14.0F, 0.0F);
		end.addChild(upper_wall_1_r1);
		setRotationAngle(upper_wall_1_r1, 0.0F, 3.1416F, -0.1107F);
		upper_wall_1_r1.setTextureOffset(120, 166).addCuboid(0.0F, -19.0F, -11.0F, 4.0F, 19.0F, 5.0F, 0.0F, false);

		lower_wall_1_r1 = new ModelPart(this);
		lower_wall_1_r1.setPivot(0.0F, 0.0F, 0.0F);
		end.addChild(lower_wall_1_r1);
		setRotationAngle(lower_wall_1_r1, 0.0F, 3.1416F, 0.0F);
		lower_wall_1_r1.setTextureOffset(168, 52).addCuboid(-21.0F, -14.0F, -11.0F, 4.0F, 14.0F, 6.0F, 0.0F, false);

		end_exterior = new ModelPart(this);
		end_exterior.setPivot(0.0F, 24.0F, 0.0F);
		end_exterior.setTextureOffset(148, 145).addCuboid(20.0F, 0.0F, -12.0F, 1.0F, 4.0F, 20.0F, 0.0F, true);
		end_exterior.setTextureOffset(148, 145).addCuboid(-21.0F, 0.0F, -12.0F, 1.0F, 4.0F, 20.0F, 0.0F, false);
		end_exterior.setTextureOffset(0, 246).addCuboid(18.0F, -14.0F, -12.0F, 3.0F, 14.0F, 22.0F, 0.0F, true);
		end_exterior.setTextureOffset(0, 246).addCuboid(-21.0F, -14.0F, -12.0F, 3.0F, 14.0F, 22.0F, 0.0F, false);
		end_exterior.setTextureOffset(248, 120).addCuboid(9.5F, -34.0F, -12.0F, 10.0F, 34.0F, 0.0F, 0.0F, true);
		end_exterior.setTextureOffset(248, 120).addCuboid(-19.5F, -34.0F, -12.0F, 10.0F, 34.0F, 0.0F, 0.0F, false);
		end_exterior.setTextureOffset(105, 248).addCuboid(-18.0F, -41.0F, -12.0F, 36.0F, 7.0F, 0.0F, 0.0F, false);

		upper_wall_2_r3 = new ModelPart(this);
		upper_wall_2_r3.setPivot(-21.0F, -14.0F, 0.0F);
		end_exterior.addChild(upper_wall_2_r3);
		setRotationAngle(upper_wall_2_r3, 0.0F, 0.0F, 0.1107F);
		upper_wall_2_r3.setTextureOffset(268, 98).addCuboid(0.0F, -22.0F, -12.0F, 3.0F, 22.0F, 22.0F, 0.0F, false);

		upper_wall_1_r2 = new ModelPart(this);
		upper_wall_1_r2.setPivot(21.0F, -14.0F, 0.0F);
		end_exterior.addChild(upper_wall_1_r2);
		setRotationAngle(upper_wall_1_r2, 0.0F, 0.0F, -0.1107F);
		upper_wall_1_r2.setTextureOffset(268, 98).addCuboid(-3.0F, -22.0F, -12.0F, 3.0F, 22.0F, 22.0F, 0.0F, true);

		roof_end = new ModelPart(this);
		roof_end.setPivot(0.0F, 24.0F, 0.0F);


		handrail_2_r2 = new ModelPart(this);
		handrail_2_r2.setPivot(0.0F, 0.0F, 0.0F);
		roof_end.addChild(handrail_2_r2);
		setRotationAngle(handrail_2_r2, -1.5708F, 0.0F, 0.0F);
		handrail_2_r2.setTextureOffset(0, 0).addCuboid(0.0F, -40.0F, -31.5F, 0.0F, 16.0F, 0.0F, 0.2F, false);

		inner_roof_1 = new ModelPart(this);
		inner_roof_1.setPivot(-2.0F, -33.0F, 16.0F);
		roof_end.addChild(inner_roof_1);
		inner_roof_1.setTextureOffset(128, 6).addCuboid(-16.0F, 0.0F, -12.0F, 6.0F, 1.0F, 36.0F, 0.0F, false);
		inner_roof_1.setTextureOffset(80, 42).addCuboid(-1.0F, -1.5F, -12.0F, 3.0F, 0.0F, 36.0F, 0.0F, false);

		inner_roof_4_r3 = new ModelPart(this);
		inner_roof_4_r3.setPivot(-3.5473F, -1.2615F, -16.0F);
		inner_roof_1.addChild(inner_roof_4_r3);
		setRotationAngle(inner_roof_4_r3, 0.0F, 0.0F, -0.0873F);
		inner_roof_4_r3.setTextureOffset(68, 42).addCuboid(-3.0F, 0.0F, 4.0F, 6.0F, 0.0F, 36.0F, 0.0F, false);

		inner_roof_3_r3 = new ModelPart(this);
		inner_roof_3_r3.setPivot(-10.0F, 1.0F, -16.0F);
		inner_roof_1.addChild(inner_roof_3_r3);
		setRotationAngle(inner_roof_3_r3, 0.0F, 0.0F, -0.5236F);
		inner_roof_3_r3.setTextureOffset(116, 93).addCuboid(0.0F, 0.0F, 4.0F, 4.0F, 0.0F, 36.0F, 0.0F, true);

		inner_roof_2 = new ModelPart(this);
		inner_roof_2.setPivot(-2.0F, -33.0F, 16.0F);
		roof_end.addChild(inner_roof_2);
		inner_roof_2.setTextureOffset(80, 42).addCuboid(2.0F, -1.5F, -12.0F, 3.0F, 0.0F, 36.0F, 0.0F, true);
		inner_roof_2.setTextureOffset(128, 6).addCuboid(14.0F, 0.0F, -12.0F, 6.0F, 1.0F, 36.0F, 0.0F, true);

		inner_roof_4_r4 = new ModelPart(this);
		inner_roof_4_r4.setPivot(7.5473F, -1.2615F, -16.0F);
		inner_roof_2.addChild(inner_roof_4_r4);
		setRotationAngle(inner_roof_4_r4, 0.0F, 0.0F, 0.0873F);
		inner_roof_4_r4.setTextureOffset(68, 42).addCuboid(-3.0F, 0.0F, 4.0F, 6.0F, 0.0F, 36.0F, 0.0F, true);

		inner_roof_3_r4 = new ModelPart(this);
		inner_roof_3_r4.setPivot(14.0F, 1.0F, -16.0F);
		inner_roof_2.addChild(inner_roof_3_r4);
		setRotationAngle(inner_roof_3_r4, 0.0F, 0.0F, 0.5236F);
		inner_roof_3_r4.setTextureOffset(108, 93).addCuboid(-4.0F, 0.0F, 4.0F, 4.0F, 0.0F, 36.0F, 0.0F, true);

		roof_end_exterior = new ModelPart(this);
		roof_end_exterior.setPivot(0.0F, 24.0F, 0.0F);
		roof_end_exterior.setTextureOffset(88, 43).addCuboid(-8.0F, -43.0F, 0.0F, 16.0F, 2.0F, 48.0F, 0.0F, false);

		vent_2_r1 = new ModelPart(this);
		vent_2_r1.setPivot(-8.0F, -43.0F, 0.0F);
		roof_end_exterior.addChild(vent_2_r1);
		setRotationAngle(vent_2_r1, 0.0F, 0.0F, -0.3491F);
		vent_2_r1.setTextureOffset(56, 93).addCuboid(-9.0F, 0.0F, 0.0F, 9.0F, 2.0F, 48.0F, 0.0F, false);

		vent_1_r1 = new ModelPart(this);
		vent_1_r1.setPivot(8.0F, -43.0F, 0.0F);
		roof_end_exterior.addChild(vent_1_r1);
		setRotationAngle(vent_1_r1, 0.0F, 0.0F, 0.3491F);
		vent_1_r1.setTextureOffset(56, 93).addCuboid(0.0F, 0.0F, 0.0F, 9.0F, 2.0F, 48.0F, 0.0F, true);

		outer_roof_1 = new ModelPart(this);
		outer_roof_1.setPivot(0.0F, 0.0F, 0.0F);
		roof_end_exterior.addChild(outer_roof_1);
		outer_roof_1.setTextureOffset(188, 120).addCuboid(-5.7289F, -41.8532F, -12.0F, 6.0F, 1.0F, 20.0F, 0.0F, false);

		outer_roof_5_r2 = new ModelPart(this);
		outer_roof_5_r2.setPivot(-9.4945F, -40.1738F, -8.0F);
		outer_roof_1.addChild(outer_roof_5_r2);
		setRotationAngle(outer_roof_5_r2, 0.0F, 0.0F, -0.1745F);
		outer_roof_5_r2.setTextureOffset(38, 193).addCuboid(-4.0F, -1.0F, -4.0F, 8.0F, 1.0F, 20.0F, 0.0F, false);

		outer_roof_4_r2 = new ModelPart(this);
		outer_roof_4_r2.setPivot(-14.4064F, -38.848F, -8.0F);
		outer_roof_1.addChild(outer_roof_4_r2);
		setRotationAngle(outer_roof_4_r2, 0.0F, 0.0F, -0.5236F);
		outer_roof_4_r2.setTextureOffset(0, 62).addCuboid(-2.5F, -1.0F, -4.0F, 4.0F, 1.0F, 20.0F, 0.0F, false);

		outer_roof_3_r2 = new ModelPart(this);
		outer_roof_3_r2.setPivot(-16.7054F, -37.098F, -8.0F);
		outer_roof_1.addChild(outer_roof_3_r2);
		setRotationAngle(outer_roof_3_r2, 0.0F, 0.0F, -1.0472F);
		outer_roof_3_r2.setTextureOffset(98, 145).addCuboid(-2.0F, -1.0F, -4.0F, 3.0F, 1.0F, 20.0F, 0.0F, false);

		outer_roof_2 = new ModelPart(this);
		outer_roof_2.setPivot(0.0F, 0.0F, 0.0F);
		roof_end_exterior.addChild(outer_roof_2);
		outer_roof_2.setTextureOffset(188, 120).addCuboid(-0.2711F, -41.8532F, -12.0F, 6.0F, 1.0F, 20.0F, 0.0F, true);

		outer_roof_5_r3 = new ModelPart(this);
		outer_roof_5_r3.setPivot(9.4945F, -40.1738F, -8.0F);
		outer_roof_2.addChild(outer_roof_5_r3);
		setRotationAngle(outer_roof_5_r3, 0.0F, 0.0F, 0.1745F);
		outer_roof_5_r3.setTextureOffset(38, 193).addCuboid(-4.0F, -1.0F, -4.0F, 8.0F, 1.0F, 20.0F, 0.0F, true);

		outer_roof_4_r3 = new ModelPart(this);
		outer_roof_4_r3.setPivot(15.2724F, -38.348F, -8.0F);
		outer_roof_2.addChild(outer_roof_4_r3);
		setRotationAngle(outer_roof_4_r3, 0.0F, 0.0F, 0.5236F);
		outer_roof_4_r3.setTextureOffset(0, 62).addCuboid(-2.5F, -1.0F, -4.0F, 4.0F, 1.0F, 20.0F, 0.0F, true);

		outer_roof_3_r3 = new ModelPart(this);
		outer_roof_3_r3.setPivot(16.7054F, -37.098F, -8.0F);
		outer_roof_2.addChild(outer_roof_3_r3);
		setRotationAngle(outer_roof_3_r3, 0.0F, 0.0F, 1.0472F);
		outer_roof_3_r3.setTextureOffset(98, 145).addCuboid(-1.0F, -1.0F, -4.0F, 3.0F, 1.0F, 20.0F, 0.0F, true);

		roof_light = new ModelPart(this);
		roof_light.setPivot(0.0F, 24.0F, 0.0F);


		roof_light_r1 = new ModelPart(this);
		roof_light_r1.setPivot(-5.8F, -33.8F, 0.0F);
		roof_light.addChild(roof_light_r1);
		setRotationAngle(roof_light_r1, 0.0F, 0.0F, 0.1309F);
		roof_light_r1.setTextureOffset(44, 144).addCuboid(-3.0F, -1.0F, -24.0F, 3.0F, 1.0F, 48.0F, 0.0F, false);

		roof_end_light = new ModelPart(this);
		roof_end_light.setPivot(0.0F, 24.0F, 0.0F);


		roof_light_2_r1 = new ModelPart(this);
		roof_light_2_r1.setPivot(-5.8F, -33.8F, 0.0F);
		roof_end_light.addChild(roof_light_2_r1);
		setRotationAngle(roof_light_2_r1, 0.0F, 0.0F, 0.1309F);
		roof_light_2_r1.setTextureOffset(56, 156).addCuboid(-3.0F, -1.0F, 4.0F, 3.0F, 1.0F, 36.0F, 0.0F, false);

		roof_light_1_r1 = new ModelPart(this);
		roof_light_1_r1.setPivot(5.8F, -33.8F, 0.0F);
		roof_end_light.addChild(roof_light_1_r1);
		setRotationAngle(roof_light_1_r1, 0.0F, 0.0F, -0.1309F);
		roof_light_1_r1.setTextureOffset(56, 156).addCuboid(0.0F, -1.0F, 4.0F, 3.0F, 1.0F, 36.0F, 0.0F, true);

		head = new ModelPart(this);
		head.setPivot(0.0F, 24.0F, 0.0F);
		head.setTextureOffset(184, 73).addCuboid(-20.0F, 0.0F, 4.0F, 40.0F, 1.0F, 4.0F, 0.0F, false);
		head.setTextureOffset(70, 143).addCuboid(-21.0F, -14.0F, 4.0F, 4.0F, 14.0F, 7.0F, 0.0F, false);
		head.setTextureOffset(190, 223).addCuboid(-18.0F, -35.0F, 4.0F, 36.0F, 35.0F, 0.0F, 0.0F, false);

		upper_wall_2_r4 = new ModelPart(this);
		upper_wall_2_r4.setPivot(-21.0F, -14.0F, 0.0F);
		head.addChild(upper_wall_2_r4);
		setRotationAngle(upper_wall_2_r4, 0.0F, 0.0F, 0.1107F);
		upper_wall_2_r4.setTextureOffset(48, 143).addCuboid(0.0F, -19.0F, 4.0F, 4.0F, 19.0F, 7.0F, 0.0F, false);

		upper_wall_1_r3 = new ModelPart(this);
		upper_wall_1_r3.setPivot(21.0F, -14.0F, 0.0F);
		head.addChild(upper_wall_1_r3);
		setRotationAngle(upper_wall_1_r3, 0.0F, 3.1416F, -0.1107F);
		upper_wall_1_r3.setTextureOffset(170, 139).addCuboid(0.0F, -19.0F, -11.0F, 4.0F, 19.0F, 7.0F, 0.0F, false);

		lower_wall_1_r2 = new ModelPart(this);
		lower_wall_1_r2.setPivot(0.0F, 0.0F, 0.0F);
		head.addChild(lower_wall_1_r2);
		setRotationAngle(lower_wall_1_r2, 0.0F, 3.1416F, 0.0F);
		lower_wall_1_r2.setTextureOffset(158, 200).addCuboid(-21.0F, -14.0F, -11.0F, 4.0F, 14.0F, 7.0F, 0.0F, false);

		head_exterior = new ModelPart(this);
		head_exterior.setPivot(0.0F, 24.0F, 0.0F);
		head_exterior.setTextureOffset(10, 0).addCuboid(-21.0F, 0.0F, -31.0F, 42.0F, 7.0F, 35.0F, 0.0F, false);
		head_exterior.setTextureOffset(176, 0).addCuboid(20.0F, 0.0F, 4.0F, 1.0F, 4.0F, 4.0F, 0.0F, true);
		head_exterior.setTextureOffset(176, 0).addCuboid(-21.0F, 0.0F, 4.0F, 1.0F, 4.0F, 4.0F, 0.0F, false);
		head_exterior.setTextureOffset(120, 202).addCuboid(18.0F, -14.0F, -22.0F, 3.0F, 14.0F, 32.0F, 0.0F, true);
		head_exterior.setTextureOffset(120, 202).addCuboid(-21.0F, -14.0F, -22.0F, 3.0F, 14.0F, 32.0F, 0.0F, false);
		head_exterior.setTextureOffset(214, 0).addCuboid(-18.0F, -41.0F, 3.0F, 36.0F, 41.0F, 0.0F, 0.0F, false);

		upper_wall_2_r5 = new ModelPart(this);
		upper_wall_2_r5.setPivot(-21.0F, -14.0F, 0.0F);
		head_exterior.addChild(upper_wall_2_r5);
		setRotationAngle(upper_wall_2_r5, 0.0F, 0.0F, 0.1107F);
		upper_wall_2_r5.setTextureOffset(0, 192).addCuboid(0.0F, -22.0F, -22.0F, 3.0F, 22.0F, 32.0F, 0.0F, false);

		upper_wall_1_r4 = new ModelPart(this);
		upper_wall_1_r4.setPivot(21.0F, -14.0F, 0.0F);
		head_exterior.addChild(upper_wall_1_r4);
		setRotationAngle(upper_wall_1_r4, 0.0F, 0.0F, -0.1107F);
		upper_wall_1_r4.setTextureOffset(0, 192).addCuboid(-3.0F, -22.0F, -22.0F, 3.0F, 22.0F, 32.0F, 0.0F, true);

		front = new ModelPart(this);
		front.setPivot(0.0F, 0.0F, 0.0F);
		head_exterior.addChild(front);
		front.setTextureOffset(236, 215).addCuboid(-20.0F, -8.8978F, -31.2245F, 40.0F, 6.0F, 0.0F, 0.0F, false);
		front.setTextureOffset(0, 91).addCuboid(-20.0F, 0.0F, -32.0F, 40.0F, 2.0F, 0.0F, 0.0F, false);

		front_bottom_2_r1 = new ModelPart(this);
		front_bottom_2_r1.setPivot(0.0F, 2.0F, -32.0F);
		front.addChild(front_bottom_2_r1);
		setRotationAngle(front_bottom_2_r1, 0.3491F, 0.0F, 0.0F);
		front_bottom_2_r1.setTextureOffset(236, 209).addCuboid(-20.0F, 0.0F, 0.0F, 40.0F, 6.0F, 0.0F, 0.0F, false);

		front_panel_4_r1 = new ModelPart(this);
		front_panel_4_r1.setPivot(0.0F, -29.5396F, -27.5854F);
		front.addChild(front_panel_4_r1);
		setRotationAngle(front_panel_4_r1, -0.2618F, 0.0F, 0.0F);
		front_panel_4_r1.setTextureOffset(206, 156).addCuboid(-20.0F, -11.0F, 0.0F, 40.0F, 18.0F, 0.0F, 0.0F, false);

		front_panel_3_r1 = new ModelPart(this);
		front_panel_3_r1.setPivot(0.0F, -15.8379F, -30.3109F);
		front.addChild(front_panel_3_r1);
		setRotationAngle(front_panel_3_r1, -0.1309F, 0.0F, 0.0F);
		front_panel_3_r1.setTextureOffset(236, 195).addCuboid(-20.0F, -7.0F, 0.0F, 40.0F, 14.0F, 0.0F, 0.0F, false);

		front_panel_1_r1 = new ModelPart(this);
		front_panel_1_r1.setPivot(0.0F, 0.0F, -32.0F);
		front.addChild(front_panel_1_r1);
		setRotationAngle(front_panel_1_r1, -0.2618F, 0.0F, 0.0F);
		front_panel_1_r1.setTextureOffset(116, 197).addCuboid(-20.0F, -3.0F, 0.0F, 40.0F, 3.0F, 0.0F, 0.0F, false);

		side_1 = new ModelPart(this);
		side_1.setPivot(0.0F, 0.0F, 0.0F);
		front.addChild(side_1);


		outer_roof_5_r4 = new ModelPart(this);
		outer_roof_5_r4.setPivot(3.2289F, -40.6367F, -18.8935F);
		side_1.addChild(outer_roof_5_r4);
		setRotationAngle(outer_roof_5_r4, 0.1745F, 0.0F, 0.0F);
		outer_roof_5_r4.setTextureOffset(16, 129).addCuboid(-3.5F, 0.0F, -7.0F, 6.0F, 0.0F, 14.0F, 0.0F, true);

		outer_roof_4_r4 = new ModelPart(this);
		outer_roof_4_r4.setPivot(10.4418F, -39.7879F, -18.8937F);
		side_1.addChild(outer_roof_4_r4);
		setRotationAngle(outer_roof_4_r4, 0.1745F, 0.0F, 0.1745F);
		outer_roof_4_r4.setTextureOffset(84, 143).addCuboid(-5.0F, 0.0F, -7.0F, 10.0F, 0.0F, 14.0F, 0.0F, true);

		outer_roof_3_r4 = new ModelPart(this);
		outer_roof_3_r4.setPivot(13.583F, -39.4219F, -18.94F);
		side_1.addChild(outer_roof_3_r4);
		setRotationAngle(outer_roof_3_r4, 0.1309F, 0.0F, 0.5236F);
		outer_roof_3_r4.setTextureOffset(114, 0).addCuboid(-0.5F, 0.0F, -8.0F, 6.0F, 0.0F, 15.0F, 0.0F, true);

		outer_roof_2_r1 = new ModelPart(this);
		outer_roof_2_r1.setPivot(17.5562F, -37.0118F, -18.9933F);
		side_1.addChild(outer_roof_2_r1);
		setRotationAngle(outer_roof_2_r1, 0.0436F, 0.0F, 1.0472F);
		outer_roof_2_r1.setTextureOffset(129, 0).addCuboid(-1.5F, 0.0F, -9.0F, 4.0F, 0.0F, 16.0F, 0.0F, true);

		front_side_bottom_1_r1 = new ModelPart(this);
		front_side_bottom_1_r1.setPivot(21.0F, 0.0F, -22.0F);
		side_1.addChild(front_side_bottom_1_r1);
		setRotationAngle(front_side_bottom_1_r1, 0.0F, 0.1745F, 0.1745F);
		front_side_bottom_1_r1.setTextureOffset(48, 111).addCuboid(0.0F, 0.0F, -11.0F, 0.0F, 8.0F, 18.0F, 0.0F, true);

		front_side_lower_1_r1 = new ModelPart(this);
		front_side_lower_1_r1.setPivot(21.0F, 0.0F, -22.0F);
		side_1.addChild(front_side_lower_1_r1);
		setRotationAngle(front_side_lower_1_r1, 0.0F, 0.1745F, 0.0F);
		front_side_lower_1_r1.setTextureOffset(74, 182).addCuboid(0.0F, -14.0F, -11.0F, 0.0F, 14.0F, 11.0F, 0.0F, true);

		front_side_upper_1_r1 = new ModelPart(this);
		front_side_upper_1_r1.setPivot(21.0F, -14.0F, -22.0F);
		side_1.addChild(front_side_upper_1_r1);
		setRotationAngle(front_side_upper_1_r1, 0.0F, 0.1745F, -0.1107F);
		front_side_upper_1_r1.setTextureOffset(98, 155).addCuboid(0.0F, -23.0F, -11.0F, 0.0F, 23.0F, 11.0F, 0.0F, true);

		side_2 = new ModelPart(this);
		side_2.setPivot(-21.0F, 0.0F, -9.0F);
		front.addChild(side_2);


		outer_roof_5_r5 = new ModelPart(this);
		outer_roof_5_r5.setPivot(17.7711F, -40.6367F, -9.8935F);
		side_2.addChild(outer_roof_5_r5);
		setRotationAngle(outer_roof_5_r5, 0.1745F, 0.0F, 0.0F);
		outer_roof_5_r5.setTextureOffset(16, 129).addCuboid(-2.5F, 0.0F, -7.0F, 6.0F, 0.0F, 14.0F, 0.0F, false);

		outer_roof_4_r5 = new ModelPart(this);
		outer_roof_4_r5.setPivot(10.5582F, -39.7879F, -9.8937F);
		side_2.addChild(outer_roof_4_r5);
		setRotationAngle(outer_roof_4_r5, 0.1745F, 0.0F, -0.1745F);
		outer_roof_4_r5.setTextureOffset(84, 143).addCuboid(-5.0F, 0.0F, -7.0F, 10.0F, 0.0F, 14.0F, 0.0F, false);

		outer_roof_3_r5 = new ModelPart(this);
		outer_roof_3_r5.setPivot(7.417F, -39.4219F, -9.94F);
		side_2.addChild(outer_roof_3_r5);
		setRotationAngle(outer_roof_3_r5, 0.1309F, 0.0F, -0.5236F);
		outer_roof_3_r5.setTextureOffset(114, 0).addCuboid(-5.5F, 0.0F, -8.0F, 6.0F, 0.0F, 15.0F, 0.0F, false);

		outer_roof_2_r2 = new ModelPart(this);
		outer_roof_2_r2.setPivot(3.4438F, -37.0118F, -9.9933F);
		side_2.addChild(outer_roof_2_r2);
		setRotationAngle(outer_roof_2_r2, 0.0436F, 0.0F, -1.0472F);
		outer_roof_2_r2.setTextureOffset(129, 0).addCuboid(-2.5F, 0.0F, -9.0F, 4.0F, 0.0F, 16.0F, 0.0F, false);

		front_side_bottom_2_r1 = new ModelPart(this);
		front_side_bottom_2_r1.setPivot(0.0F, 0.0F, -13.0F);
		side_2.addChild(front_side_bottom_2_r1);
		setRotationAngle(front_side_bottom_2_r1, 0.0F, -0.1745F, -0.1745F);
		front_side_bottom_2_r1.setTextureOffset(48, 111).addCuboid(0.0F, 0.0F, -11.0F, 0.0F, 8.0F, 18.0F, 0.0F, false);

		front_side_upper_2_r1 = new ModelPart(this);
		front_side_upper_2_r1.setPivot(0.0F, -14.0F, -13.0F);
		side_2.addChild(front_side_upper_2_r1);
		setRotationAngle(front_side_upper_2_r1, 0.0F, -0.1745F, 0.1107F);
		front_side_upper_2_r1.setTextureOffset(98, 155).addCuboid(0.0F, -23.0F, -11.0F, 0.0F, 23.0F, 11.0F, 0.0F, false);

		front_side_lower_2_r1 = new ModelPart(this);
		front_side_lower_2_r1.setPivot(0.0F, 0.0F, -13.0F);
		side_2.addChild(front_side_lower_2_r1);
		setRotationAngle(front_side_lower_2_r1, 0.0F, -0.1745F, 0.0F);
		front_side_lower_2_r1.setTextureOffset(74, 182).addCuboid(0.0F, -14.0F, -11.0F, 0.0F, 14.0F, 11.0F, 0.0F, false);

		headlights = new ModelPart(this);
		headlights.setPivot(0.0F, 24.0F, 0.0F);
		headlights.setTextureOffset(12, 56).addCuboid(9.5F, -8.8978F, -31.4F, 4.0F, 4.0F, 0.0F, 0.0F, true);
		headlights.setTextureOffset(12, 56).addCuboid(-13.5F, -8.8978F, -31.4F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		tail_lights = new ModelPart(this);
		tail_lights.setPivot(0.0F, 24.0F, 0.0F);
		tail_lights.setTextureOffset(20, 56).addCuboid(13.5F, -8.8978F, -31.4F, 4.0F, 4.0F, 0.0F, 0.0F, true);
		tail_lights.setTextureOffset(20, 56).addCuboid(-17.5F, -8.8978F, -31.4F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		door_light = new ModelPart(this);
		door_light.setPivot(0.0F, 24.0F, 0.0F);


		outer_roof_3_r6 = new ModelPart(this);
		outer_roof_3_r6.setPivot(-17.8206F, -37.1645F, 0.0F);
		door_light.addChild(outer_roof_3_r6);
		setRotationAngle(outer_roof_3_r6, 0.0F, 0.0F, -1.0472F);
		outer_roof_3_r6.setTextureOffset(25, 4).addCuboid(-1.5F, -0.1F, -1.5F, 3.0F, 0.0F, 3.0F, 0.0F, false);

		door_light_on = new ModelPart(this);
		door_light_on.setPivot(0.0F, 24.0F, 0.0F);


		light_r1 = new ModelPart(this);
		light_r1.setPivot(-17.8206F, -37.1645F, 0.0F);
		door_light_on.addChild(light_r1);
		setRotationAngle(light_r1, 0.0F, 0.0F, -1.0472F);
		light_r1.setTextureOffset(12, 0).addCuboid(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.3F, false);

		door_light_off = new ModelPart(this);
		door_light_off.setPivot(0.0F, 24.0F, 0.0F);


		light_r2 = new ModelPart(this);
		light_r2.setPivot(-17.8206F, -37.1645F, 0.0F);
		door_light_off.addChild(light_r2);
		setRotationAngle(light_r2, 0.0F, 0.0F, -1.0472F);
		light_r2.setTextureOffset(16, 0).addCuboid(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.3F, false);
	}

	private static final int DOOR_MAX = 13;
	private static final ModelDoorOverlay MODEL_DOOR_OVERLAY = new ModelDoorOverlay(DOOR_MAX, 6.34F, "door_overlay_k_train_left.png", "door_overlay_k_train_right.png");

	@Override
	protected void renderWindowPositions(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean isEnd1Head, boolean isEnd2Head) {
		switch (renderStage) {
			case LIGHTS:
				renderMirror(roof_light, matrices, vertices, light, position);
				break;
			case INTERIOR:
				renderMirror(window, matrices, vertices, light, position);
				renderMirror(roof_window, matrices, vertices, light, position);
				break;
			case INTERIOR_TRANSLUCENT:
				renderMirror(side_panel, matrices, vertices, light, position - 22.1F);
				renderMirror(side_panel, matrices, vertices, light, position + 22.1F);
				break;
			case EXTERIOR:
				renderMirror(window_exterior, matrices, vertices, light, position);
				renderMirror(roof_exterior, matrices, vertices, light, position);
				break;
		}
	}

	@Override
	protected void renderDoorPositions(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
		final boolean firstDoor = isIndex(0, position, getDoorPositions());
		final boolean notLastDoor = !firstDoor && !isIndex(-1, position, getDoorPositions());
		final boolean doorOpen = doorLeftZ > 0 || doorRightZ > 0;

		switch (renderStage) {
			case LIGHTS:
				if (notLastDoor) {
					renderMirror(roof_light, matrices, vertices, light, position);
				}
				if (firstDoor && doorOpen) {
					renderMirror(door_light_on, matrices, vertices, light, 0);
				}
				break;
			case INTERIOR:
				door_left.setPivot(doorRightX, 0, doorRightZ);
				door_right.setPivot(doorRightX, 0, -doorRightZ);
				renderOnce(door, matrices, vertices, light, position);
				door_left.setPivot(doorLeftX, 0, doorLeftZ);
				door_right.setPivot(doorLeftX, 0, -doorLeftZ);
				renderOnceFlipped(door, matrices, vertices, light, position);
				if (notLastDoor) {
					renderMirror(roof_door, matrices, vertices, light, position);
				}
				break;
			case EXTERIOR:
				door_left_exterior.setPivot(doorRightX, 0, doorRightZ);
				door_right_exterior.setPivot(doorRightX, 0, -doorRightZ);
				renderOnce(door_exterior, matrices, vertices, light, position);
				door_left_exterior.setPivot(doorLeftX, 0, doorLeftZ);
				door_right_exterior.setPivot(doorLeftX, 0, -doorLeftZ);
				renderOnceFlipped(door_exterior, matrices, vertices, light, position);
				renderMirror(roof_exterior, matrices, vertices, light, position);
				if (firstDoor) {
					renderMirror(door_light, matrices, vertices, light, 0);
					if (!doorOpen) {
						renderMirror(door_light_off, matrices, vertices, light, 0);
					}
				}
				break;
		}
	}

	@Override
	protected void renderHeadPosition1(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean useHeadlights) {
		switch (renderStage) {
			case LIGHTS:
				renderOnce(roof_end_light, matrices, vertices, light, position);
				renderOnce(useHeadlights ? headlights : tail_lights, matrices, vertices, light, position);
				break;
			case INTERIOR:
				renderOnce(head, matrices, vertices, light, position);
				renderOnce(roof_end, matrices, vertices, light, position);
				break;
			case EXTERIOR:
				renderOnce(head_exterior, matrices, vertices, light, position);
				renderOnce(roof_end_exterior, matrices, vertices, light, position);
				break;
		}
	}

	@Override
	protected void renderHeadPosition2(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean useHeadlights) {
		switch (renderStage) {
			case LIGHTS:
				renderOnceFlipped(roof_end_light, matrices, vertices, light, position);
				renderOnceFlipped(useHeadlights ? headlights : tail_lights, matrices, vertices, light, position);
				break;
			case INTERIOR:
				renderOnceFlipped(head, matrices, vertices, light, position);
				renderOnceFlipped(roof_end, matrices, vertices, light, position);
				break;
			case EXTERIOR:
				renderOnceFlipped(head_exterior, matrices, vertices, light, position);
				renderOnceFlipped(roof_end_exterior, matrices, vertices, light, position);
				break;
		}
	}

	@Override
	protected void renderEndPosition1(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position) {
		switch (renderStage) {
			case LIGHTS:
				renderOnce(roof_end_light, matrices, vertices, light, position);
				break;
			case INTERIOR:
				renderOnce(end, matrices, vertices, light, position);
				renderOnce(roof_end, matrices, vertices, light, position);
				break;
			case EXTERIOR:
				renderOnce(end_exterior, matrices, vertices, light, position);
				renderOnce(roof_end_exterior, matrices, vertices, light, position);
				break;
		}
	}

	@Override
	protected void renderEndPosition2(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position) {
		switch (renderStage) {
			case LIGHTS:
				renderOnceFlipped(roof_end_light, matrices, vertices, light, position);
				break;
			case INTERIOR:
				renderOnceFlipped(end, matrices, vertices, light, position);
				renderOnceFlipped(roof_end, matrices, vertices, light, position);
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
	protected ModelDoorOverlayTop getModelDoorOverlayTop() {
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
		return smoothEnds(-0.01F, -1.01F, 0, 0.1F, value);
	}

	@Override
	protected float getDoorAnimationZ(float value, boolean opening) {
		if (opening) {
			return smoothEnds(0, DOOR_MAX, 0.05F, 0.5F, value);
		} else {
			if (value > 0.5) {
				return smoothEnds(2, DOOR_MAX, 0.5F, 1, value);
			} else if (value < 0.3) {
				return smoothEnds(0, 2, 0.05F, 0.3F, value);
			} else {
				return 2;
			}
		}
	}
}