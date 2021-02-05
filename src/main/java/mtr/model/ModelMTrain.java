package mtr.model;

import mtr.gui.IGui;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;

public class ModelMTrain extends ModelTrainBase {

	private final ModelPart window;
	private final ModelPart handrail_8_r1;
	private final ModelPart top_handrail_6_r1;
	private final ModelPart top_handrail_5_r1;
	private final ModelPart top_handrail_4_r1;
	private final ModelPart top_handrail_3_r1;
	private final ModelPart top_handrail_2_r1;
	private final ModelPart top_handrail_1_r1;
	private final ModelPart handrail_5_r1;
	private final ModelPart upper_wall_r1;
	private final ModelPart seat;
	private final ModelPart seat_back_r1;
	private final ModelPart window_exterior;
	private final ModelPart upper_wall_r2;
	private final ModelPart side_panel;
	private final ModelPart roof_window;
	private final ModelPart inner_roof_4_r1;
	private final ModelPart inner_roof_2_r1;
	private final ModelPart roof_door;
	private final ModelPart handrail_2_r1;
	private final ModelPart inner_roof_4_r2;
	private final ModelPart inner_roof_2_r2;
	private final ModelPart roof_exterior;
	private final ModelPart outer_roof_5_r1;
	private final ModelPart outer_roof_4_r1;
	private final ModelPart outer_roof_3_r1;
	private final ModelPart outer_roof_2_r1;
	private final ModelPart outer_roof_1_r1;
	private final ModelPart door;
	private final ModelPart door_left;
	private final ModelPart door_left_top_r1;
	private final ModelPart door_right;
	private final ModelPart door_right_top_r1;
	private final ModelPart door_exterior;
	private final ModelPart door_left_exterior;
	private final ModelPart door_left_top_r2;
	private final ModelPart door_right_exterior;
	private final ModelPart door_right_top_r2;
	private final ModelPart end;
	private final ModelPart upper_wall_2_r1;
	private final ModelPart upper_wall_1_r1;
	private final ModelPart end_exterior;
	private final ModelPart upper_wall_2_r2;
	private final ModelPart upper_wall_1_r2;
	private final ModelPart roof_end;
	private final ModelPart handrail_2_r2;
	private final ModelPart inner_roof_7_r1;
	private final ModelPart inner_roof_1;
	private final ModelPart inner_roof_6_r1;
	private final ModelPart inner_roof_4_r3;
	private final ModelPart inner_roof_2_r3;
	private final ModelPart inner_roof_2;
	private final ModelPart inner_roof_6_r2;
	private final ModelPart inner_roof_4_r4;
	private final ModelPart inner_roof_2_r4;
	private final ModelPart roof_end_exterior;
	private final ModelPart vent_2_r1;
	private final ModelPart vent_1_r1;
	private final ModelPart outer_roof_1;
	private final ModelPart outer_roof_5_r2;
	private final ModelPart outer_roof_4_r2;
	private final ModelPart outer_roof_3_r2;
	private final ModelPart outer_roof_2_r2;
	private final ModelPart outer_roof_1_r2;
	private final ModelPart outer_roof_2;
	private final ModelPart outer_roof_5_r3;
	private final ModelPart outer_roof_4_r3;
	private final ModelPart outer_roof_3_r3;
	private final ModelPart outer_roof_2_r3;
	private final ModelPart outer_roof_1_r3;
	private final ModelPart roof_light;
	private final ModelPart roof_light_r1;
	private final ModelPart roof_end_light;
	private final ModelPart light_5_r1;
	private final ModelPart light_4_r1;
	private final ModelPart light_3_r1;
	private final ModelPart light_2_r1;
	private final ModelPart light_1_r1;
	private final ModelPart head;
	private final ModelPart upper_wall_2_r3;
	private final ModelPart upper_wall_1_r3;
	private final ModelPart head_exterior;
	private final ModelPart driver_door_upper_2_r1;
	private final ModelPart driver_door_upper_1_r1;
	private final ModelPart front;
	private final ModelPart bottom_r1;
	private final ModelPart front_middle_top_r1;
	private final ModelPart front_panel_r1;
	private final ModelPart side_1;
	private final ModelPart front_side_bottom_1_r1;
	private final ModelPart outer_roof_4_r4;
	private final ModelPart outer_roof_1_r4;
	private final ModelPart outer_roof_2_r4;
	private final ModelPart outer_roof_3_r4;
	private final ModelPart front_side_lower_1_r1;
	private final ModelPart front_side_upper_1_r1;
	private final ModelPart side_2;
	private final ModelPart front_side_bottom_2_r1;
	private final ModelPart outer_roof_8_r1;
	private final ModelPart outer_roof_7_r1;
	private final ModelPart outer_roof_6_r1;
	private final ModelPart outer_roof_5_r4;
	private final ModelPart front_side_upper_2_r1;
	private final ModelPart front_side_lower_2_r1;
	private final ModelPart headlights;
	private final ModelPart tail_lights;

	public ModelMTrain() {
		textureWidth = 320;
		textureHeight = 320;
		window = new ModelPart(this);
		window.setPivot(0.0F, 24.0F, 0.0F);
		window.setTextureOffset(0, 0).addCuboid(-20.0F, 0.0F, -24.0F, 20.0F, 1.0F, 48.0F, 0.0F, false);
		window.setTextureOffset(117, 0).addCuboid(-20.0F, -14.0F, -26.0F, 2.0F, 14.0F, 52.0F, 0.0F, false);
		window.setTextureOffset(8, 0).addCuboid(0.0F, -33.0F, -9.0F, 0.0F, 33.0F, 0.0F, 0.2F, false);
		window.setTextureOffset(8, 0).addCuboid(0.0F, -33.0F, 9.0F, 0.0F, 33.0F, 0.0F, 0.2F, false);
		window.setTextureOffset(28, 28).addCuboid(-1.0F, -32.0F, 21.0F, 2.0F, 4.0F, 0.0F, 0.0F, false);
		window.setTextureOffset(28, 28).addCuboid(-1.0F, -32.0F, 15.0F, 2.0F, 4.0F, 0.0F, 0.0F, false);
		window.setTextureOffset(28, 28).addCuboid(-1.0F, -32.0F, 3.0F, 2.0F, 4.0F, 0.0F, 0.0F, false);
		window.setTextureOffset(28, 28).addCuboid(-1.0F, -32.0F, -3.0F, 2.0F, 4.0F, 0.0F, 0.0F, false);
		window.setTextureOffset(28, 28).addCuboid(-1.0F, -32.0F, -15.0F, 2.0F, 4.0F, 0.0F, 0.0F, false);
		window.setTextureOffset(28, 28).addCuboid(-1.0F, -32.0F, -21.0F, 2.0F, 4.0F, 0.0F, 0.0F, false);

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
		handrail_5_r1.setTextureOffset(4, 3).addCuboid(0.0F, -22.2F, 22.0F, 0.0F, 22.0F, 0.0F, 0.2F, false);
		handrail_5_r1.setTextureOffset(4, 1).addCuboid(0.0F, -24.0F, 0.0F, 0.0F, 24.0F, 0.0F, 0.2F, false);
		handrail_5_r1.setTextureOffset(4, 3).addCuboid(0.0F, -22.2F, -22.0F, 0.0F, 22.0F, 0.0F, 0.2F, false);

		upper_wall_r1 = new ModelPart(this);
		upper_wall_r1.setPivot(-20.0F, -14.0F, 0.0F);
		window.addChild(upper_wall_r1);
		setRotationAngle(upper_wall_r1, 0.0F, 0.0F, 0.1107F);
		upper_wall_r1.setTextureOffset(0, 82).addCuboid(0.0F, -19.0F, -26.0F, 2.0F, 19.0F, 52.0F, 0.0F, false);

		seat = new ModelPart(this);
		seat.setPivot(0.0F, 0.0F, 0.0F);
		window.addChild(seat);
		seat.setTextureOffset(0, 172).addCuboid(-18.0F, -6.0F, -22.0F, 7.0F, 1.0F, 44.0F, 0.0F, false);
		seat.setTextureOffset(106, 170).addCuboid(-18.0F, -5.0F, -21.0F, 6.0F, 5.0F, 42.0F, 0.0F, false);

		seat_back_r1 = new ModelPart(this);
		seat_back_r1.setPivot(-17.0F, -6.0F, 0.0F);
		seat.addChild(seat_back_r1);
		setRotationAngle(seat_back_r1, 0.0F, 0.0F, -0.0524F);
		seat_back_r1.setTextureOffset(173, 0).addCuboid(-1.0F, -8.0F, -22.0F, 1.0F, 8.0F, 44.0F, 0.0F, false);

		window_exterior = new ModelPart(this);
		window_exterior.setPivot(0.0F, 24.0F, 0.0F);
		window_exterior.setTextureOffset(56, 170).addCuboid(-21.0F, 0.0F, -24.0F, 1.0F, 4.0F, 48.0F, 0.0F, false);
		window_exterior.setTextureOffset(104, 104).addCuboid(-20.0F, -14.0F, -26.0F, 0.0F, 14.0F, 52.0F, 0.0F, false);

		upper_wall_r2 = new ModelPart(this);
		upper_wall_r2.setPivot(-20.0F, -14.0F, 0.0F);
		window_exterior.addChild(upper_wall_r2);
		setRotationAngle(upper_wall_r2, 0.0F, 0.0F, 0.1107F);
		upper_wall_r2.setTextureOffset(0, 101).addCuboid(0.0F, -19.0F, -26.0F, 0.0F, 19.0F, 52.0F, 0.0F, false);

		side_panel = new ModelPart(this);
		side_panel.setPivot(0.0F, 24.0F, 0.0F);
		side_panel.setTextureOffset(153, 283).addCuboid(-18.0F, -34.0F, 0.0F, 7.0F, 30.0F, 0.0F, 0.0F, false);

		roof_window = new ModelPart(this);
		roof_window.setPivot(0.0F, 24.0F, 0.0F);
		roof_window.setTextureOffset(38, 82).addCuboid(-16.0F, -32.0F, -24.0F, 3.0F, 0.0F, 48.0F, 0.0F, false);
		roof_window.setTextureOffset(76, 0).addCuboid(-10.0F, -34.0F, -24.0F, 7.0F, 0.0F, 48.0F, 0.0F, false);
		roof_window.setTextureOffset(48, 82).addCuboid(-2.0F, -33.0F, -24.0F, 2.0F, 0.0F, 48.0F, 0.0F, false);

		inner_roof_4_r1 = new ModelPart(this);
		inner_roof_4_r1.setPivot(-2.0F, -33.0F, 0.0F);
		roof_window.addChild(inner_roof_4_r1);
		setRotationAngle(inner_roof_4_r1, 0.0F, 0.0F, 0.5236F);
		inner_roof_4_r1.setTextureOffset(44, 82).addCuboid(-2.0F, 0.0F, -24.0F, 2.0F, 0.0F, 48.0F, 0.0F, false);

		inner_roof_2_r1 = new ModelPart(this);
		inner_roof_2_r1.setPivot(-13.0F, -32.0F, 0.0F);
		roof_window.addChild(inner_roof_2_r1);
		setRotationAngle(inner_roof_2_r1, 0.0F, 0.0F, -0.5236F);
		inner_roof_2_r1.setTextureOffset(90, 0).addCuboid(0.0F, 0.0F, -24.0F, 4.0F, 0.0F, 48.0F, 0.0F, false);

		roof_door = new ModelPart(this);
		roof_door.setPivot(0.0F, 24.0F, 0.0F);
		roof_door.setTextureOffset(0, 172).addCuboid(-19.0F, -32.0F, -16.0F, 6.0F, 0.0F, 32.0F, 0.0F, false);
		roof_door.setTextureOffset(84, 117).addCuboid(-10.0F, -34.0F, -16.0F, 7.0F, 0.0F, 32.0F, 0.0F, false);
		roof_door.setTextureOffset(12, 82).addCuboid(-2.0F, -33.0F, -16.0F, 2.0F, 0.0F, 32.0F, 0.0F, false);

		handrail_2_r1 = new ModelPart(this);
		handrail_2_r1.setPivot(0.0F, 0.0F, 0.0F);
		roof_door.addChild(handrail_2_r1);
		setRotationAngle(handrail_2_r1, -1.5708F, 0.0F, 0.0F);
		handrail_2_r1.setTextureOffset(0, 0).addCuboid(0.0F, -16.0F, -31.5F, 0.0F, 32.0F, 0.0F, 0.2F, false);

		inner_roof_4_r2 = new ModelPart(this);
		inner_roof_4_r2.setPivot(-2.0F, -33.0F, 0.0F);
		roof_door.addChild(inner_roof_4_r2);
		setRotationAngle(inner_roof_4_r2, 0.0F, 0.0F, 0.5236F);
		inner_roof_4_r2.setTextureOffset(128, 0).addCuboid(-2.0F, 0.0F, -16.0F, 2.0F, 0.0F, 32.0F, 0.0F, false);

		inner_roof_2_r2 = new ModelPart(this);
		inner_roof_2_r2.setPivot(-13.0F, -32.0F, 0.0F);
		roof_door.addChild(inner_roof_2_r2);
		setRotationAngle(inner_roof_2_r2, 0.0F, 0.0F, -0.5236F);
		inner_roof_2_r2.setTextureOffset(0, 0).addCuboid(0.0F, 0.0F, -16.0F, 4.0F, 0.0F, 32.0F, 0.0F, true);

		roof_exterior = new ModelPart(this);
		roof_exterior.setPivot(0.0F, 24.0F, 0.0F);
		roof_exterior.setTextureOffset(133, 0).addCuboid(-6.0F, -42.0F, -20.0F, 6.0F, 0.0F, 40.0F, 0.0F, false);

		outer_roof_5_r1 = new ModelPart(this);
		outer_roof_5_r1.setPivot(-9.9394F, -41.3064F, 0.0F);
		roof_exterior.addChild(outer_roof_5_r1);
		setRotationAngle(outer_roof_5_r1, 0.0F, 0.0F, -0.1745F);
		outer_roof_5_r1.setTextureOffset(60, 82).addCuboid(-4.0F, 0.0F, -20.0F, 8.0F, 0.0F, 40.0F, 0.0F, false);

		outer_roof_4_r1 = new ModelPart(this);
		outer_roof_4_r1.setPivot(-15.1778F, -39.8628F, 0.0F);
		roof_exterior.addChild(outer_roof_4_r1);
		setRotationAngle(outer_roof_4_r1, 0.0F, 0.0F, -0.5236F);
		outer_roof_4_r1.setTextureOffset(0, 0).addCuboid(-1.5F, 0.0F, -20.0F, 3.0F, 0.0F, 40.0F, 0.0F, false);

		outer_roof_3_r1 = new ModelPart(this);
		outer_roof_3_r1.setPivot(-16.9769F, -38.2468F, 0.0F);
		roof_exterior.addChild(outer_roof_3_r1);
		setRotationAngle(outer_roof_3_r1, 0.0F, 0.0F, -1.0472F);
		outer_roof_3_r1.setTextureOffset(0, 82).addCuboid(-1.0F, 0.0F, -20.0F, 2.0F, 0.0F, 40.0F, 0.0F, false);

		outer_roof_2_r1 = new ModelPart(this);
		outer_roof_2_r1.setPivot(-17.5872F, -36.3872F, 0.0F);
		roof_exterior.addChild(outer_roof_2_r1);
		setRotationAngle(outer_roof_2_r1, 0.0F, 0.0F, 0.1107F);
		outer_roof_2_r1.setTextureOffset(0, 182).addCuboid(0.0F, -1.0F, -20.0F, 0.0F, 2.0F, 40.0F, 0.0F, false);

		outer_roof_1_r1 = new ModelPart(this);
		outer_roof_1_r1.setPivot(-20.0F, -14.0F, 0.0F);
		roof_exterior.addChild(outer_roof_1_r1);
		setRotationAngle(outer_roof_1_r1, 0.0F, 0.0F, 0.1107F);
		outer_roof_1_r1.setTextureOffset(162, 210).addCuboid(-1.0F, -22.0F, -20.0F, 1.0F, 4.0F, 40.0F, 0.0F, false);

		door = new ModelPart(this);
		door.setPivot(0.0F, 24.0F, 0.0F);
		door.setTextureOffset(185, 95).addCuboid(-20.0F, 0.0F, -16.0F, 20.0F, 1.0F, 32.0F, 0.0F, false);
		door.setTextureOffset(8, 0).addCuboid(0.0F, -33.0F, 0.0F, 0.0F, 33.0F, 0.0F, 0.2F, false);

		door_left = new ModelPart(this);
		door_left.setPivot(0.0F, 0.0F, 0.0F);
		door.addChild(door_left);
		door_left.setTextureOffset(286, 49).addCuboid(-20.8F, -14.0F, 0.0F, 1.0F, 14.0F, 15.0F, 0.0F, false);

		door_left_top_r1 = new ModelPart(this);
		door_left_top_r1.setPivot(-20.8F, -14.0F, 0.0F);
		door_left.addChild(door_left_top_r1);
		setRotationAngle(door_left_top_r1, 0.0F, 0.0F, 0.1107F);
		door_left_top_r1.setTextureOffset(185, 0).addCuboid(0.0F, -19.0F, 0.0F, 1.0F, 19.0F, 15.0F, 0.0F, false);

		door_right = new ModelPart(this);
		door_right.setPivot(0.0F, 0.0F, 0.0F);
		door.addChild(door_right);
		door_right.setTextureOffset(34, 264).addCuboid(-20.8F, -14.0F, -15.0F, 1.0F, 14.0F, 15.0F, 0.0F, false);

		door_right_top_r1 = new ModelPart(this);
		door_right_top_r1.setPivot(-20.8F, -14.0F, 0.0F);
		door_right.addChild(door_right_top_r1);
		setRotationAngle(door_right_top_r1, 0.0F, 0.0F, 0.1107F);
		door_right_top_r1.setTextureOffset(0, 58).addCuboid(0.0F, -19.0F, -15.0F, 1.0F, 19.0F, 15.0F, 0.0F, false);

		door_exterior = new ModelPart(this);
		door_exterior.setPivot(0.0F, 24.0F, 0.0F);
		door_exterior.setTextureOffset(0, 224).addCuboid(-21.0F, 0.0F, -16.0F, 1.0F, 4.0F, 32.0F, 0.0F, false);

		door_left_exterior = new ModelPart(this);
		door_left_exterior.setPivot(0.0F, 0.0F, 0.0F);
		door_exterior.addChild(door_left_exterior);
		door_left_exterior.setTextureOffset(190, 239).addCuboid(-20.8F, -14.0F, 0.0F, 0.0F, 14.0F, 15.0F, 0.0F, false);

		door_left_top_r2 = new ModelPart(this);
		door_left_top_r2.setPivot(-20.8F, -14.0F, 0.0F);
		door_left_exterior.addChild(door_left_top_r2);
		setRotationAngle(door_left_top_r2, 0.0F, 0.0F, 0.1107F);
		door_left_top_r2.setTextureOffset(0, 209).addCuboid(0.0F, -19.0F, 0.0F, 0.0F, 19.0F, 15.0F, 0.0F, false);

		door_right_exterior = new ModelPart(this);
		door_right_exterior.setPivot(0.0F, 0.0F, 0.0F);
		door_exterior.addChild(door_right_exterior);
		door_right_exterior.setTextureOffset(34, 226).addCuboid(-20.8F, -14.0F, -15.0F, 0.0F, 14.0F, 15.0F, 0.0F, false);

		door_right_top_r2 = new ModelPart(this);
		door_right_top_r2.setPivot(-20.8F, -14.0F, 0.0F);
		door_right_exterior.addChild(door_right_top_r2);
		setRotationAngle(door_right_top_r2, 0.0F, 0.0F, 0.1107F);
		door_right_top_r2.setTextureOffset(185, 80).addCuboid(0.0F, -19.0F, -15.0F, 0.0F, 19.0F, 15.0F, 0.0F, false);

		end = new ModelPart(this);
		end.setPivot(0.0F, 24.0F, 0.0F);
		end.setTextureOffset(185, 128).addCuboid(-20.0F, 0.0F, -12.0F, 40.0F, 1.0F, 20.0F, 0.0F, false);
		end.setTextureOffset(0, 92).addCuboid(18.0F, -14.0F, 7.0F, 2.0F, 14.0F, 3.0F, 0.0F, true);
		end.setTextureOffset(0, 92).addCuboid(-20.0F, -14.0F, 7.0F, 2.0F, 14.0F, 3.0F, 0.0F, false);
		end.setTextureOffset(229, 250).addCuboid(9.5F, -34.0F, -12.0F, 9.0F, 34.0F, 19.0F, 0.0F, true);
		end.setTextureOffset(226, 149).addCuboid(-18.5F, -34.0F, -12.0F, 9.0F, 34.0F, 19.0F, 0.0F, false);

		upper_wall_2_r1 = new ModelPart(this);
		upper_wall_2_r1.setPivot(-20.0F, -14.0F, 0.0F);
		end.addChild(upper_wall_2_r1);
		setRotationAngle(upper_wall_2_r1, 0.0F, 0.0F, 0.1107F);
		upper_wall_2_r1.setTextureOffset(22, 92).addCuboid(0.0F, -19.0F, 7.0F, 2.0F, 19.0F, 3.0F, 0.0F, false);

		upper_wall_1_r1 = new ModelPart(this);
		upper_wall_1_r1.setPivot(20.0F, -14.0F, 0.0F);
		end.addChild(upper_wall_1_r1);
		setRotationAngle(upper_wall_1_r1, 0.0F, 0.0F, -0.1107F);
		upper_wall_1_r1.setTextureOffset(22, 92).addCuboid(-2.0F, -19.0F, 7.0F, 2.0F, 19.0F, 3.0F, 0.0F, true);

		end_exterior = new ModelPart(this);
		end_exterior.setPivot(0.0F, 24.0F, 0.0F);
		end_exterior.setTextureOffset(0, 192).addCuboid(20.0F, 0.0F, -12.0F, 1.0F, 4.0F, 20.0F, 0.0F, true);
		end_exterior.setTextureOffset(0, 192).addCuboid(-21.0F, 0.0F, -12.0F, 1.0F, 4.0F, 20.0F, 0.0F, false);
		end_exterior.setTextureOffset(44, 279).addCuboid(18.0F, -14.0F, -12.0F, 2.0F, 14.0F, 22.0F, 0.0F, true);
		end_exterior.setTextureOffset(44, 279).addCuboid(-20.0F, -14.0F, -12.0F, 2.0F, 14.0F, 22.0F, 0.0F, false);
		end_exterior.setTextureOffset(88, 0).addCuboid(9.5F, -34.0F, -12.0F, 9.0F, 34.0F, 0.0F, 0.0F, false);
		end_exterior.setTextureOffset(88, 0).addCuboid(-18.5F, -34.0F, -12.0F, 9.0F, 34.0F, 0.0F, 0.0F, true);
		end_exterior.setTextureOffset(225, 57).addCuboid(-18.0F, -41.0F, -12.0F, 36.0F, 7.0F, 0.0F, 0.0F, false);

		upper_wall_2_r2 = new ModelPart(this);
		upper_wall_2_r2.setPivot(-20.0F, -14.0F, 0.0F);
		end_exterior.addChild(upper_wall_2_r2);
		setRotationAngle(upper_wall_2_r2, 0.0F, 0.0F, 0.1107F);
		upper_wall_2_r2.setTextureOffset(97, 264).addCuboid(0.0F, -19.0F, -12.0F, 2.0F, 19.0F, 22.0F, 0.0F, false);

		upper_wall_1_r2 = new ModelPart(this);
		upper_wall_1_r2.setPivot(20.0F, -14.0F, 0.0F);
		end_exterior.addChild(upper_wall_1_r2);
		setRotationAngle(upper_wall_1_r2, 0.0F, 0.0F, -0.1107F);
		upper_wall_1_r2.setTextureOffset(97, 264).addCuboid(-2.0F, -19.0F, -12.0F, 2.0F, 19.0F, 22.0F, 0.0F, true);

		roof_end = new ModelPart(this);
		roof_end.setPivot(0.0F, 24.0F, 0.0F);


		handrail_2_r2 = new ModelPart(this);
		handrail_2_r2.setPivot(0.0F, 0.0F, 0.0F);
		roof_end.addChild(handrail_2_r2);
		setRotationAngle(handrail_2_r2, -1.5708F, 0.0F, 0.0F);
		handrail_2_r2.setTextureOffset(0, 0).addCuboid(0.0F, -40.0F, -31.5F, 0.0F, 16.0F, 0.0F, 0.2F, false);

		inner_roof_7_r1 = new ModelPart(this);
		inner_roof_7_r1.setPivot(0.0F, -33.0F, 16.0F);
		roof_end.addChild(inner_roof_7_r1);
		setRotationAngle(inner_roof_7_r1, -0.5236F, 0.0F, 0.0F);
		inner_roof_7_r1.setTextureOffset(6, 49).addCuboid(-2.0F, 0.0F, -2.0F, 4.0F, 0.0F, 2.0F, 0.0F, false);

		inner_roof_1 = new ModelPart(this);
		inner_roof_1.setPivot(-2.0F, -33.0F, 16.0F);
		roof_end.addChild(inner_roof_1);
		inner_roof_1.setTextureOffset(32, 82).addCuboid(-17.0F, 1.0F, -12.0F, 6.0F, 0.0F, 36.0F, 0.0F, false);
		inner_roof_1.setTextureOffset(81, 66).addCuboid(-8.0F, -1.0F, -28.0F, 10.0F, 0.0F, 52.0F, 0.0F, false);
		inner_roof_1.setTextureOffset(0, 49).addCuboid(0.0F, 0.0F, 0.0F, 2.0F, 0.0F, 24.0F, 0.0F, false);

		inner_roof_6_r1 = new ModelPart(this);
		inner_roof_6_r1.setPivot(0.0F, 0.0F, 0.0F);
		inner_roof_1.addChild(inner_roof_6_r1);
		setRotationAngle(inner_roof_6_r1, -0.5236F, 0.0F, 0.5236F);
		inner_roof_6_r1.setTextureOffset(6, 51).addCuboid(-2.0F, 0.0F, -2.0F, 2.0F, 0.0F, 2.0F, 0.0F, false);

		inner_roof_4_r3 = new ModelPart(this);
		inner_roof_4_r3.setPivot(0.0F, 0.0F, -16.0F);
		inner_roof_1.addChild(inner_roof_4_r3);
		setRotationAngle(inner_roof_4_r3, 0.0F, 0.0F, 0.5236F);
		inner_roof_4_r3.setTextureOffset(4, 49).addCuboid(-2.0F, 0.0F, 16.0F, 2.0F, 0.0F, 24.0F, 0.0F, false);

		inner_roof_2_r3 = new ModelPart(this);
		inner_roof_2_r3.setPivot(-11.0F, 1.0F, -16.0F);
		inner_roof_1.addChild(inner_roof_2_r3);
		setRotationAngle(inner_roof_2_r3, 0.0F, 0.0F, -0.5236F);
		inner_roof_2_r3.setTextureOffset(78, 0).addCuboid(0.0F, 0.0F, 4.0F, 4.0F, 0.0F, 36.0F, 0.0F, true);

		inner_roof_2 = new ModelPart(this);
		inner_roof_2.setPivot(-2.0F, -33.0F, 16.0F);
		roof_end.addChild(inner_roof_2);
		inner_roof_2.setTextureOffset(32, 82).addCuboid(15.0F, 1.0F, -12.0F, 6.0F, 0.0F, 36.0F, 0.0F, true);
		inner_roof_2.setTextureOffset(81, 66).addCuboid(2.0F, -1.0F, -28.0F, 10.0F, 0.0F, 52.0F, 0.0F, true);
		inner_roof_2.setTextureOffset(0, 49).addCuboid(2.0F, 0.0F, 0.0F, 2.0F, 0.0F, 24.0F, 0.0F, true);

		inner_roof_6_r2 = new ModelPart(this);
		inner_roof_6_r2.setPivot(4.0F, 0.0F, 0.0F);
		inner_roof_2.addChild(inner_roof_6_r2);
		setRotationAngle(inner_roof_6_r2, -0.5236F, 0.0F, -0.5236F);
		inner_roof_6_r2.setTextureOffset(6, 51).addCuboid(0.0F, 0.0F, -2.0F, 2.0F, 0.0F, 2.0F, 0.0F, true);

		inner_roof_4_r4 = new ModelPart(this);
		inner_roof_4_r4.setPivot(4.0F, 0.0F, -16.0F);
		inner_roof_2.addChild(inner_roof_4_r4);
		setRotationAngle(inner_roof_4_r4, 0.0F, 0.0F, -0.5236F);
		inner_roof_4_r4.setTextureOffset(4, 49).addCuboid(0.0F, 0.0F, 16.0F, 2.0F, 0.0F, 24.0F, 0.0F, true);

		inner_roof_2_r4 = new ModelPart(this);
		inner_roof_2_r4.setPivot(15.0F, 1.0F, -16.0F);
		inner_roof_2.addChild(inner_roof_2_r4);
		setRotationAngle(inner_roof_2_r4, 0.0F, 0.0F, 0.5236F);
		inner_roof_2_r4.setTextureOffset(70, 0).addCuboid(-4.0F, 0.0F, 4.0F, 4.0F, 0.0F, 36.0F, 0.0F, true);

		roof_end_exterior = new ModelPart(this);
		roof_end_exterior.setPivot(0.0F, 24.0F, 0.0F);
		roof_end_exterior.setTextureOffset(105, 105).addCuboid(-8.0F, -43.0F, 0.0F, 16.0F, 2.0F, 48.0F, 0.0F, false);

		vent_2_r1 = new ModelPart(this);
		vent_2_r1.setPivot(-8.0F, -43.0F, 0.0F);
		roof_end_exterior.addChild(vent_2_r1);
		setRotationAngle(vent_2_r1, 0.0F, 0.0F, -0.3491F);
		vent_2_r1.setTextureOffset(160, 160).addCuboid(-9.0F, 0.0F, 0.0F, 9.0F, 2.0F, 48.0F, 0.0F, false);

		vent_1_r1 = new ModelPart(this);
		vent_1_r1.setPivot(8.0F, -43.0F, 0.0F);
		roof_end_exterior.addChild(vent_1_r1);
		setRotationAngle(vent_1_r1, 0.0F, 0.0F, 0.3491F);
		vent_1_r1.setTextureOffset(160, 160).addCuboid(0.0F, 0.0F, 0.0F, 9.0F, 2.0F, 48.0F, 0.0F, true);

		outer_roof_1 = new ModelPart(this);
		outer_roof_1.setPivot(0.0F, 0.0F, 0.0F);
		roof_end_exterior.addChild(outer_roof_1);
		outer_roof_1.setTextureOffset(147, 293).addCuboid(-6.0F, -42.0F, -12.0F, 6.0F, 1.0F, 20.0F, 0.0F, false);

		outer_roof_5_r2 = new ModelPart(this);
		outer_roof_5_r2.setPivot(-9.7656F, -40.3206F, 0.0F);
		outer_roof_1.addChild(outer_roof_5_r2);
		setRotationAngle(outer_roof_5_r2, 0.0F, 0.0F, -0.1745F);
		outer_roof_5_r2.setTextureOffset(262, 182).addCuboid(-4.0F, -1.0F, -12.0F, 8.0F, 1.0F, 20.0F, 0.0F, false);

		outer_roof_4_r2 = new ModelPart(this);
		outer_roof_4_r2.setPivot(-14.6775F, -38.9948F, 0.0F);
		outer_roof_1.addChild(outer_roof_4_r2);
		setRotationAngle(outer_roof_4_r2, 0.0F, 0.0F, -0.5236F);
		outer_roof_4_r2.setTextureOffset(259, 64).addCuboid(-1.5F, -1.0F, -12.0F, 3.0F, 1.0F, 20.0F, 0.0F, false);

		outer_roof_3_r2 = new ModelPart(this);
		outer_roof_3_r2.setPivot(-16.1105F, -37.7448F, 0.0F);
		outer_roof_1.addChild(outer_roof_3_r2);
		setRotationAngle(outer_roof_3_r2, 0.0F, 0.0F, -1.0472F);
		outer_roof_3_r2.setTextureOffset(0, 289).addCuboid(-1.0F, -1.0F, -12.0F, 2.0F, 1.0F, 20.0F, 0.0F, false);

		outer_roof_2_r2 = new ModelPart(this);
		outer_roof_2_r2.setPivot(-17.587F, -36.3849F, 0.0F);
		outer_roof_1.addChild(outer_roof_2_r2);
		setRotationAngle(outer_roof_2_r2, 0.0F, 0.0F, 0.1107F);
		outer_roof_2_r2.setTextureOffset(110, 129).addCuboid(0.0F, -1.0F, -12.0F, 1.0F, 2.0F, 20.0F, 0.0F, false);

		outer_roof_1_r2 = new ModelPart(this);
		outer_roof_1_r2.setPivot(-20.0F, -14.0F, 0.0F);
		outer_roof_1.addChild(outer_roof_1_r2);
		setRotationAngle(outer_roof_1_r2, 0.0F, 0.0F, 0.1107F);
		outer_roof_1_r2.setTextureOffset(185, 268).addCuboid(-1.0F, -22.0F, -12.0F, 1.0F, 4.0F, 20.0F, 0.0F, false);

		outer_roof_2 = new ModelPart(this);
		outer_roof_2.setPivot(0.0F, 0.0F, 0.0F);
		roof_end_exterior.addChild(outer_roof_2);
		outer_roof_2.setTextureOffset(147, 293).addCuboid(0.0F, -42.0F, -12.0F, 6.0F, 1.0F, 20.0F, 0.0F, true);

		outer_roof_5_r3 = new ModelPart(this);
		outer_roof_5_r3.setPivot(9.7656F, -40.3206F, 0.0F);
		outer_roof_2.addChild(outer_roof_5_r3);
		setRotationAngle(outer_roof_5_r3, 0.0F, 0.0F, 0.1745F);
		outer_roof_5_r3.setTextureOffset(262, 182).addCuboid(-4.0F, -1.0F, -12.0F, 8.0F, 1.0F, 20.0F, 0.0F, true);

		outer_roof_4_r3 = new ModelPart(this);
		outer_roof_4_r3.setPivot(14.6775F, -38.9948F, 0.0F);
		outer_roof_2.addChild(outer_roof_4_r3);
		setRotationAngle(outer_roof_4_r3, 0.0F, 0.0F, 0.5236F);
		outer_roof_4_r3.setTextureOffset(259, 64).addCuboid(-1.5F, -1.0F, -12.0F, 3.0F, 1.0F, 20.0F, 0.0F, true);

		outer_roof_3_r3 = new ModelPart(this);
		outer_roof_3_r3.setPivot(16.1105F, -37.7448F, 0.0F);
		outer_roof_2.addChild(outer_roof_3_r3);
		setRotationAngle(outer_roof_3_r3, 0.0F, 0.0F, 1.0472F);
		outer_roof_3_r3.setTextureOffset(0, 289).addCuboid(-1.0F, -1.0F, -12.0F, 2.0F, 1.0F, 20.0F, 0.0F, true);

		outer_roof_2_r3 = new ModelPart(this);
		outer_roof_2_r3.setPivot(17.587F, -36.3849F, 0.0F);
		outer_roof_2.addChild(outer_roof_2_r3);
		setRotationAngle(outer_roof_2_r3, 0.0F, 0.0F, -0.1107F);
		outer_roof_2_r3.setTextureOffset(110, 129).addCuboid(-1.0F, -1.0F, -12.0F, 1.0F, 2.0F, 20.0F, 0.0F, true);

		outer_roof_1_r3 = new ModelPart(this);
		outer_roof_1_r3.setPivot(20.0F, -14.0F, 0.0F);
		outer_roof_2.addChild(outer_roof_1_r3);
		setRotationAngle(outer_roof_1_r3, 0.0F, 0.0F, -0.1107F);
		outer_roof_1_r3.setTextureOffset(185, 268).addCuboid(0.0F, -22.0F, -12.0F, 1.0F, 4.0F, 20.0F, 0.0F, true);

		roof_light = new ModelPart(this);
		roof_light.setPivot(0.0F, 24.0F, 0.0F);


		roof_light_r1 = new ModelPart(this);
		roof_light_r1.setPivot(-2.0F, -33.0F, 0.0F);
		roof_light.addChild(roof_light_r1);
		setRotationAngle(roof_light_r1, 0.0F, 0.0F, 0.5236F);
		roof_light_r1.setTextureOffset(0, 82).addCuboid(-2.0F, -0.1F, -24.0F, 2.0F, 0.0F, 48.0F, 0.0F, false);

		roof_end_light = new ModelPart(this);
		roof_end_light.setPivot(0.0F, 24.0F, 0.0F);


		light_5_r1 = new ModelPart(this);
		light_5_r1.setPivot(2.0F, -33.0F, 0.0F);
		roof_end_light.addChild(light_5_r1);
		setRotationAngle(light_5_r1, 0.0F, 0.0F, -0.5236F);
		light_5_r1.setTextureOffset(24, 82).addCuboid(0.0F, -0.1F, 16.0F, 2.0F, 0.0F, 24.0F, 0.0F, false);

		light_4_r1 = new ModelPart(this);
		light_4_r1.setPivot(2.0F, -33.0F, 16.0F);
		roof_end_light.addChild(light_4_r1);
		setRotationAngle(light_4_r1, -0.5236F, 0.0F, -0.5236F);
		light_4_r1.setTextureOffset(10, 51).addCuboid(0.0F, -0.1F, -2.0F, 2.0F, 0.0F, 2.0F, 0.0F, true);

		light_3_r1 = new ModelPart(this);
		light_3_r1.setPivot(0.0F, -33.0F, 16.0F);
		roof_end_light.addChild(light_3_r1);
		setRotationAngle(light_3_r1, -0.5236F, 0.0F, 0.0F);
		light_3_r1.setTextureOffset(14, 49).addCuboid(-2.0F, -0.1F, -2.0F, 4.0F, 0.0F, 2.0F, 0.0F, false);

		light_2_r1 = new ModelPart(this);
		light_2_r1.setPivot(-2.0F, -33.0F, 16.0F);
		roof_end_light.addChild(light_2_r1);
		setRotationAngle(light_2_r1, -0.5236F, 0.0F, 0.5236F);
		light_2_r1.setTextureOffset(10, 51).addCuboid(-2.0F, -0.1F, -2.0F, 2.0F, 0.0F, 2.0F, 0.0F, false);

		light_1_r1 = new ModelPart(this);
		light_1_r1.setPivot(-2.0F, -33.0F, 0.0F);
		roof_end_light.addChild(light_1_r1);
		setRotationAngle(light_1_r1, 0.0F, 0.0F, 0.5236F);
		light_1_r1.setTextureOffset(24, 82).addCuboid(-2.0F, -0.1F, 16.0F, 2.0F, 0.0F, 24.0F, 0.0F, false);

		head = new ModelPart(this);
		head.setPivot(0.0F, 24.0F, 0.0F);
		head.setTextureOffset(219, 35).addCuboid(-20.0F, 0.0F, 4.0F, 40.0F, 1.0F, 4.0F, 0.0F, false);
		head.setTextureOffset(185, 128).addCuboid(18.0F, -14.0F, 4.0F, 2.0F, 14.0F, 6.0F, 0.0F, true);
		head.setTextureOffset(185, 128).addCuboid(-20.0F, -14.0F, 4.0F, 2.0F, 14.0F, 6.0F, 0.0F, false);
		head.setTextureOffset(80, 222).addCuboid(-18.0F, -34.0F, 4.0F, 36.0F, 34.0F, 0.0F, 0.0F, false);

		upper_wall_2_r3 = new ModelPart(this);
		upper_wall_2_r3.setPivot(-20.0F, -14.0F, 0.0F);
		head.addChild(upper_wall_2_r3);
		setRotationAngle(upper_wall_2_r3, 0.0F, 0.0F, 0.1107F);
		upper_wall_2_r3.setTextureOffset(132, 118).addCuboid(0.0F, -19.0F, 4.0F, 2.0F, 19.0F, 6.0F, 0.0F, false);

		upper_wall_1_r3 = new ModelPart(this);
		upper_wall_1_r3.setPivot(20.0F, -14.0F, 0.0F);
		head.addChild(upper_wall_1_r3);
		setRotationAngle(upper_wall_1_r3, 0.0F, 0.0F, -0.1107F);
		upper_wall_1_r3.setTextureOffset(132, 118).addCuboid(-2.0F, -19.0F, 4.0F, 2.0F, 19.0F, 6.0F, 0.0F, true);

		head_exterior = new ModelPart(this);
		head_exterior.setPivot(0.0F, 24.0F, 0.0F);
		head_exterior.setTextureOffset(153, 66).addCuboid(-21.0F, 0.0F, -18.0F, 42.0F, 7.0F, 22.0F, 0.0F, false);
		head_exterior.setTextureOffset(153, 77).addCuboid(20.0F, 0.0F, 4.0F, 1.0F, 7.0F, 4.0F, 0.0F, true);
		head_exterior.setTextureOffset(153, 77).addCuboid(-21.0F, 0.0F, 4.0F, 1.0F, 7.0F, 4.0F, 0.0F, false);
		head_exterior.setTextureOffset(51, 241).addCuboid(18.0F, -14.0F, -9.0F, 2.0F, 14.0F, 19.0F, 0.0F, true);
		head_exterior.setTextureOffset(51, 241).addCuboid(-20.0F, -14.0F, -9.0F, 2.0F, 14.0F, 19.0F, 0.0F, false);
		head_exterior.setTextureOffset(276, 86).addCuboid(18.0F, -14.0F, -18.0F, 1.0F, 14.0F, 9.0F, 0.0F, true);
		head_exterior.setTextureOffset(276, 86).addCuboid(-19.0F, -14.0F, -18.0F, 1.0F, 14.0F, 9.0F, 0.0F, false);
		head_exterior.setTextureOffset(219, 0).addCuboid(-18.0F, -34.0F, 3.0F, 36.0F, 34.0F, 0.0F, 0.0F, false);

		driver_door_upper_2_r1 = new ModelPart(this);
		driver_door_upper_2_r1.setPivot(-20.0F, -14.0F, 0.0F);
		head_exterior.addChild(driver_door_upper_2_r1);
		setRotationAngle(driver_door_upper_2_r1, 0.0F, 0.0F, 0.1107F);
		driver_door_upper_2_r1.setTextureOffset(0, 172).addCuboid(1.0F, -19.0F, -18.0F, 1.0F, 19.0F, 9.0F, 0.0F, false);
		driver_door_upper_2_r1.setTextureOffset(160, 170).addCuboid(0.0F, -19.0F, -9.0F, 2.0F, 19.0F, 19.0F, 0.0F, false);

		driver_door_upper_1_r1 = new ModelPart(this);
		driver_door_upper_1_r1.setPivot(20.0F, -14.0F, 0.0F);
		head_exterior.addChild(driver_door_upper_1_r1);
		setRotationAngle(driver_door_upper_1_r1, 0.0F, 0.0F, -0.1107F);
		driver_door_upper_1_r1.setTextureOffset(0, 172).addCuboid(-2.0F, -19.0F, -18.0F, 1.0F, 19.0F, 9.0F, 0.0F, true);
		driver_door_upper_1_r1.setTextureOffset(160, 170).addCuboid(-2.0F, -19.0F, -9.0F, 2.0F, 19.0F, 19.0F, 0.0F, true);

		front = new ModelPart(this);
		front.setPivot(0.0F, 0.0F, 0.0F);
		head_exterior.addChild(front);
		front.setTextureOffset(225, 52).addCuboid(-19.0F, 0.0F, -28.0F, 38.0F, 5.0F, 0.0F, 0.0F, false);

		bottom_r1 = new ModelPart(this);
		bottom_r1.setPivot(0.0F, 7.0F, 4.0F);
		front.addChild(bottom_r1);
		setRotationAngle(bottom_r1, -0.0698F, 0.0F, 0.0F);
		bottom_r1.setTextureOffset(0, 49).addCuboid(-21.0F, 0.0F, -33.0F, 42.0F, 0.0F, 33.0F, 0.0F, false);

		front_middle_top_r1 = new ModelPart(this);
		front_middle_top_r1.setPivot(0.0F, -42.0F, -12.0F);
		front.addChild(front_middle_top_r1);
		setRotationAngle(front_middle_top_r1, 0.3491F, 0.0F, 0.0F);
		front_middle_top_r1.setTextureOffset(143, 95).addCuboid(-6.0F, 0.0F, -10.0F, 12.0F, 0.0F, 10.0F, 0.0F, false);

		front_panel_r1 = new ModelPart(this);
		front_panel_r1.setPivot(0.0F, 0.0F, -28.0F);
		front.addChild(front_panel_r1);
		setRotationAngle(front_panel_r1, -0.1745F, 0.0F, 0.0F);
		front_panel_r1.setTextureOffset(204, 210).addCuboid(-19.0F, -40.0F, 0.0F, 38.0F, 40.0F, 0.0F, 0.0F, false);

		side_1 = new ModelPart(this);
		side_1.setPivot(0.0F, 0.0F, 0.0F);
		front.addChild(side_1);
		side_1.setTextureOffset(22, 0).addCuboid(19.0F, -14.0F, -18.0F, 1.0F, 14.0F, 0.0F, 0.0F, true);

		front_side_bottom_1_r1 = new ModelPart(this);
		front_side_bottom_1_r1.setPivot(21.0F, 0.0F, -13.0F);
		side_1.addChild(front_side_bottom_1_r1);
		setRotationAngle(front_side_bottom_1_r1, 0.0F, 0.1745F, 0.1745F);
		front_side_bottom_1_r1.setTextureOffset(263, 17).addCuboid(0.0F, 0.0F, -16.0F, 0.0F, 7.0F, 23.0F, 0.0F, true);

		outer_roof_4_r4 = new ModelPart(this);
		outer_roof_4_r4.setPivot(6.0F, -42.0F, -12.0F);
		side_1.addChild(outer_roof_4_r4);
		setRotationAngle(outer_roof_4_r4, 0.3491F, 0.0F, 0.1745F);
		outer_roof_4_r4.setTextureOffset(142, 66).addCuboid(0.0F, 0.0F, -11.0F, 11.0F, 0.0F, 11.0F, 0.0F, true);

		outer_roof_1_r4 = new ModelPart(this);
		outer_roof_1_r4.setPivot(20.0F, -14.0F, 0.0F);
		side_1.addChild(outer_roof_1_r4);
		setRotationAngle(outer_roof_1_r4, 0.0F, 0.0F, -0.1107F);
		outer_roof_1_r4.setTextureOffset(0, 49).addCuboid(0.0F, -22.0F, -18.0F, 1.0F, 4.0F, 6.0F, 0.0F, true);
		outer_roof_1_r4.setTextureOffset(46, 0).addCuboid(-1.0F, -19.0F, -18.0F, 1.0F, 19.0F, 0.0F, 0.0F, true);

		outer_roof_2_r4 = new ModelPart(this);
		outer_roof_2_r4.setPivot(17.587F, -36.3849F, 0.0F);
		side_1.addChild(outer_roof_2_r4);
		setRotationAngle(outer_roof_2_r4, 0.0F, 0.0F, -0.1107F);
		outer_roof_2_r4.setTextureOffset(24, 26).addCuboid(0.0F, -1.0F, -18.0F, 0.0F, 2.0F, 6.0F, 0.0F, true);

		outer_roof_3_r4 = new ModelPart(this);
		outer_roof_3_r4.setPivot(15.813F, -37.5414F, -17.4163F);
		side_1.addChild(outer_roof_3_r4);
		setRotationAngle(outer_roof_3_r4, 0.1745F, 0.0F, 0.7418F);
		outer_roof_3_r4.setTextureOffset(89, 122).addCuboid(-3.5F, 0.0F, -5.5F, 7.0F, 0.0F, 11.0F, 0.0F, true);

		front_side_lower_1_r1 = new ModelPart(this);
		front_side_lower_1_r1.setPivot(20.0F, 0.0F, -18.0F);
		side_1.addChild(front_side_lower_1_r1);
		setRotationAngle(front_side_lower_1_r1, 0.0F, 0.1745F, 0.0F);
		front_side_lower_1_r1.setTextureOffset(207, 257).addCuboid(0.0F, -14.0F, -11.0F, 0.0F, 20.0F, 11.0F, 0.0F, true);

		front_side_upper_1_r1 = new ModelPart(this);
		front_side_upper_1_r1.setPivot(20.0F, -14.0F, -18.0F);
		side_1.addChild(front_side_upper_1_r1);
		setRotationAngle(front_side_upper_1_r1, 0.0F, 0.1745F, -0.1107F);
		front_side_upper_1_r1.setTextureOffset(93, 245).addCuboid(0.0F, -23.0F, -11.0F, 0.0F, 23.0F, 11.0F, 0.0F, true);

		side_2 = new ModelPart(this);
		side_2.setPivot(-21.0F, 0.0F, -9.0F);
		front.addChild(side_2);
		side_2.setTextureOffset(22, 0).addCuboid(1.0F, -14.0F, -9.0F, 1.0F, 14.0F, 0.0F, 0.0F, false);

		front_side_bottom_2_r1 = new ModelPart(this);
		front_side_bottom_2_r1.setPivot(0.0F, 0.0F, -4.0F);
		side_2.addChild(front_side_bottom_2_r1);
		setRotationAngle(front_side_bottom_2_r1, 0.0F, -0.1745F, -0.1745F);
		front_side_bottom_2_r1.setTextureOffset(263, 17).addCuboid(0.0F, 0.0F, -16.0F, 0.0F, 7.0F, 23.0F, 0.0F, false);

		outer_roof_8_r1 = new ModelPart(this);
		outer_roof_8_r1.setPivot(5.187F, -37.5414F, -8.4163F);
		side_2.addChild(outer_roof_8_r1);
		setRotationAngle(outer_roof_8_r1, 0.1745F, 0.0F, -0.7418F);
		outer_roof_8_r1.setTextureOffset(89, 122).addCuboid(-3.5F, 0.0F, -5.5F, 7.0F, 0.0F, 11.0F, 0.0F, false);

		outer_roof_7_r1 = new ModelPart(this);
		outer_roof_7_r1.setPivot(3.413F, -36.3849F, 9.0F);
		side_2.addChild(outer_roof_7_r1);
		setRotationAngle(outer_roof_7_r1, 0.0F, 0.0F, 0.1107F);
		outer_roof_7_r1.setTextureOffset(24, 26).addCuboid(0.0F, -1.0F, -18.0F, 0.0F, 2.0F, 6.0F, 0.0F, false);

		outer_roof_6_r1 = new ModelPart(this);
		outer_roof_6_r1.setPivot(15.0F, -42.0F, -3.0F);
		side_2.addChild(outer_roof_6_r1);
		setRotationAngle(outer_roof_6_r1, 0.3491F, 0.0F, -0.1745F);
		outer_roof_6_r1.setTextureOffset(142, 66).addCuboid(-11.0F, 0.0F, -11.0F, 11.0F, 0.0F, 11.0F, 0.0F, false);

		outer_roof_5_r4 = new ModelPart(this);
		outer_roof_5_r4.setPivot(1.0F, -14.0F, 9.0F);
		side_2.addChild(outer_roof_5_r4);
		setRotationAngle(outer_roof_5_r4, 0.0F, 0.0F, 0.1107F);
		outer_roof_5_r4.setTextureOffset(0, 49).addCuboid(-1.0F, -22.0F, -18.0F, 1.0F, 4.0F, 6.0F, 0.0F, false);
		outer_roof_5_r4.setTextureOffset(46, 0).addCuboid(0.0F, -19.0F, -18.0F, 1.0F, 19.0F, 0.0F, 0.0F, false);

		front_side_upper_2_r1 = new ModelPart(this);
		front_side_upper_2_r1.setPivot(1.0F, -14.0F, -9.0F);
		side_2.addChild(front_side_upper_2_r1);
		setRotationAngle(front_side_upper_2_r1, 0.0F, -0.1745F, 0.1107F);
		front_side_upper_2_r1.setTextureOffset(93, 245).addCuboid(0.0F, -23.0F, -11.0F, 0.0F, 23.0F, 11.0F, 0.0F, false);

		front_side_lower_2_r1 = new ModelPart(this);
		front_side_lower_2_r1.setPivot(1.0F, 0.0F, -9.0F);
		side_2.addChild(front_side_lower_2_r1);
		setRotationAngle(front_side_lower_2_r1, 0.0F, -0.1745F, 0.0F);
		front_side_lower_2_r1.setTextureOffset(207, 257).addCuboid(0.0F, -14.0F, -11.0F, 0.0F, 20.0F, 11.0F, 0.0F, false);

		headlights = new ModelPart(this);
		headlights.setPivot(0.0F, 24.0F, 0.0F);
		headlights.setTextureOffset(102, 36).addCuboid(8.0F, -1.0F, -28.1F, 4.0F, 4.0F, 0.0F, 0.0F, true);
		headlights.setTextureOffset(102, 36).addCuboid(-12.0F, -1.0F, -28.1F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		tail_lights = new ModelPart(this);
		tail_lights.setPivot(0.0F, 24.0F, 0.0F);
		tail_lights.setTextureOffset(110, 36).addCuboid(10.0F, -1.0F, -28.1F, 4.0F, 4.0F, 0.0F, 0.0F, true);
		tail_lights.setTextureOffset(110, 36).addCuboid(-14.0F, -1.0F, -28.1F, 4.0F, 4.0F, 0.0F, 0.0F, false);
	}

	private static final int DOOR_MAX = 14;

	@Override
	protected void renderWindowPositions(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position) {
		switch (renderStage) {
			case LIGHTS:
				renderMirror(roof_light, matrices, vertices, light, position);
				break;
			case INTERIOR:
				renderMirror(window, matrices, vertices, light, position);
				renderMirror(roof_window, matrices, vertices, light, position);
				break;
			case INTERIOR_TRANSLUCENT:
				renderMirror(side_panel, matrices, vertices, light, position - 21.9F);
				renderMirror(side_panel, matrices, vertices, light, position + 21.9F);
				break;
			case EXTERIOR:
				renderMirror(window_exterior, matrices, vertices, light, position);
				renderMirror(roof_exterior, matrices, vertices, light, position);
				break;
		}
	}

	@Override
	protected void renderDoorPositions(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, float doorLeftValue, float doorRightValue) {
		final float doorLeft = doorLeftValue * DOOR_MAX;
		final float doorRight = doorRightValue * DOOR_MAX;
		final boolean notLastDoor = getDoorPositions().length > 4 && position != getDoorPositions()[0] && position != getDoorPositions()[4];

		switch (renderStage) {
			case LIGHTS:
				if (notLastDoor) {
					renderMirror(roof_light, matrices, vertices, light, position);
				}
				break;
			case INTERIOR:
				door_left.setPivot(0, 0, doorRight);
				door_right.setPivot(0, 0, -doorRight);
				renderOnce(door, matrices, vertices, light, position);
				door_left.setPivot(0, 0, doorLeft);
				door_right.setPivot(0, 0, -doorLeft);
				renderOnceFlipped(door, matrices, vertices, light, position);
				if (notLastDoor) {
					renderMirror(roof_door, matrices, vertices, light, position);
				}
				break;
			case EXTERIOR:
				door_left_exterior.setPivot(0, 0, doorRight);
				door_right_exterior.setPivot(0, 0, -doorRight);
				renderOnce(door_exterior, matrices, vertices, light, position);
				door_left_exterior.setPivot(0, 0, doorLeft);
				door_right_exterior.setPivot(0, 0, -doorLeft);
				renderOnceFlipped(door_exterior, matrices, vertices, light, position);
				renderMirror(roof_exterior, matrices, vertices, light, position);
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
	protected void renderDoorLabels(MatrixStack matrices, VertexConsumerProvider vertexConsumers, RenderStage renderStage, int light, float positionScaled, float doorLeftValue, float doorRightValue) {
		final int colorAdjustment = 0xFFB0B0B0;
		final int colorAdjustmentDark = 0xFF707070;
		final float doorLeft = doorLeftValue * DOOR_MAX / 16;
		final float doorRight = doorRightValue * DOOR_MAX / 16;

		matrices.push();
		matrices.translate(1.25, -0.875, 0);
		matrices.multiply(Vector3f.NEGATIVE_Z.getDegreesQuaternion(6.34F));
		if (renderStage == RenderStage.EXTERIOR) {
			IGui.drawTexture(matrices, vertexConsumers, "mtr:textures/sign/door_left.png", 0.0625F, -0.91F, positionScaled - 0.3485F - doorLeft, 0.0625F, -0.765F, positionScaled - 0.2375F - doorLeft, 0, 0, 1, 1, colorAdjustmentDark, light);
			IGui.drawTexture(matrices, vertexConsumers, "mtr:textures/sign/door_right.png", 0.0625F, -0.91F, positionScaled + 0.2375F + doorLeft, 0.0625F, -0.765F, positionScaled + 0.3485F + doorLeft, 0, 0, 1, 1, colorAdjustmentDark, light);
		} else {
			IGui.drawTexture(matrices, vertexConsumers, "mtr:textures/sign/gap_left.png", -0.013F, -1.12F, positionScaled + 0.7F + doorLeft, -0.013F, -1.03F, positionScaled + 0.2375F + doorLeft, 0, 0, 1, 1, colorAdjustment, light);
			IGui.drawTexture(matrices, vertexConsumers, "mtr:textures/sign/gap_right.png", -0.013F, -1.12F, positionScaled - 0.2375F - doorLeft, -0.013F, -1.03F, positionScaled - 0.7F - doorLeft, 0, 0, 1, 1, colorAdjustment, light);
			IGui.drawTexture(matrices, vertexConsumers, "mtr:textures/sign/door_left.png", -0.013F, -0.91F, positionScaled + 0.3485F + doorLeft, -0.013F, -0.765F, positionScaled + 0.2375F + doorLeft, 0, 0, 1, 1, colorAdjustment, light);
			IGui.drawTexture(matrices, vertexConsumers, "mtr:textures/sign/door_right.png", -0.013F, -0.91F, positionScaled - 0.2375F - doorLeft, -0.013F, -0.765F, positionScaled - 0.3485F - doorLeft, 0, 0, 1, 1, colorAdjustment, light);
			IGui.drawTexture(matrices, vertexConsumers, "mtr:textures/sign/stripe.png", -0.013F, -1.125F, positionScaled + 0.875F + doorLeft, -0.013F, 0, positionScaled + 0.8125F + doorLeft, 1, 0, 0, 24, colorAdjustment, light);
			IGui.drawTexture(matrices, vertexConsumers, "mtr:textures/sign/stripe.png", -0.013F, -1.125F, positionScaled - 0.8125F - doorLeft, -0.013F, 0, positionScaled - 0.875F - doorLeft, 0, 0, 1, 24, colorAdjustment, light);
			IGui.drawTexture(matrices, vertexConsumers, "mtr:textures/sign/stripe.png", -0.075F, -1.3125F, positionScaled + 0.874F, -0.0125F, 0, positionScaled + 0.874F, 0, 0.1F, 1, 28.1F, colorAdjustment, light);
			IGui.drawTexture(matrices, vertexConsumers, "mtr:textures/sign/stripe.png", -0.0125F, -1.3125F, positionScaled - 0.874F, -0.075F, 0, positionScaled - 0.874F, 1, 0.1F, 0, 28.1F, colorAdjustment, light);
		}
		matrices.pop();

		if (renderStage == RenderStage.INTERIOR) {
			IGui.drawTexture(matrices, vertexConsumers, "mtr:textures/sign/stripe.png", 1.237F, -0.91F, positionScaled + 0.875F + doorLeft, 1.237F, -0.16F, positionScaled + 0.8125F + doorLeft, 1, 0.25F, 0, 16.25F, colorAdjustment, light);
			IGui.drawTexture(matrices, vertexConsumers, "mtr:textures/sign/stripe.png", 1.237F, -0.91F, positionScaled - 0.8125F - doorLeft, 1.237F, -0.16F, positionScaled - 0.875F - doorLeft, 0, 0.25F, 1, 16.25F, colorAdjustment, light);
			IGui.drawTexture(matrices, vertexConsumers, "mtr:textures/sign/stripe.png", 1.175F, -0.875F, positionScaled + 0.8745F, 1.2375F, -0.3125F, positionScaled + 0.8745F, 0, 0, 1, 12, colorAdjustment, light);
			IGui.drawTexture(matrices, vertexConsumers, "mtr:textures/sign/stripe.png", 1.2375F, -0.875F, positionScaled - 0.8745F, 1.175F, -0.3125F, positionScaled - 0.8745F, 1, 0, 0, 12, colorAdjustment, light);
		}

		matrices.push();
		matrices.translate(-1.25, -0.875, 0);
		matrices.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(6.34F));
		if (renderStage == RenderStage.EXTERIOR) {
			IGui.drawTexture(matrices, vertexConsumers, "mtr:textures/sign/door_left.png", -0.0625F, -0.91F, positionScaled + 0.3485F + doorRight, -0.0625F, -0.765F, positionScaled + 0.2375F + doorRight, 0, 0, 1, 1, colorAdjustmentDark, light);
			IGui.drawTexture(matrices, vertexConsumers, "mtr:textures/sign/door_right.png", -0.0625F, -0.91F, positionScaled - 0.2375F - doorRight, -0.0625F, -0.765F, positionScaled - 0.3485F - doorRight, 0, 0, 1, 1, colorAdjustmentDark, light);
		} else {
			IGui.drawTexture(matrices, vertexConsumers, "mtr:textures/sign/gap_left.png", 0.013F, -1.12F, positionScaled - 0.7F - doorRight, 0.013F, -1.03F, positionScaled - 0.2375F - doorRight, 0, 0, 1, 1, colorAdjustment, light);
			IGui.drawTexture(matrices, vertexConsumers, "mtr:textures/sign/gap_right.png", 0.013F, -1.12F, positionScaled + 0.2375F + doorRight, 0.013F, -1.03F, positionScaled + 0.7F + doorRight, 0, 0, 1, 1, colorAdjustment, light);
			IGui.drawTexture(matrices, vertexConsumers, "mtr:textures/sign/door_left.png", 0.013F, -0.91F, positionScaled - 0.3485F - doorRight, 0.013F, -0.765F, positionScaled - 0.2375F - doorRight, 0, 0, 1, 1, colorAdjustment, light);
			IGui.drawTexture(matrices, vertexConsumers, "mtr:textures/sign/door_right.png", 0.013F, -0.91F, positionScaled + 0.2375F + doorRight, 0.013F, -0.765F, positionScaled + 0.3485F + doorRight, 0, 0, 1, 1, colorAdjustment, light);
			IGui.drawTexture(matrices, vertexConsumers, "mtr:textures/sign/stripe.png", 0.013F, -1.125F, positionScaled - 0.875F - doorRight, 0.013F, 0, positionScaled - 0.8125F - doorRight, 1, 0, 0, 24, colorAdjustment, light);
			IGui.drawTexture(matrices, vertexConsumers, "mtr:textures/sign/stripe.png", 0.013F, -1.125F, positionScaled + 0.8125F + doorRight, 0.013F, 0, positionScaled + 0.875F + doorRight, 0, 0, 1, 24, colorAdjustment, light);
			IGui.drawTexture(matrices, vertexConsumers, "mtr:textures/sign/stripe.png", 0.075F, -1.3125F, positionScaled - 0.874F, 0.0125F, 0, positionScaled - 0.874F, 0, 0.1F, 1, 28.1F, colorAdjustment, light);
			IGui.drawTexture(matrices, vertexConsumers, "mtr:textures/sign/stripe.png", 0.0125F, -1.3125F, positionScaled + 0.874F, 0.075F, 0, positionScaled + 0.874F, 1, 0.1F, 0, 28.1F, colorAdjustment, light);
		}
		matrices.pop();

		if (renderStage == RenderStage.INTERIOR) {
			IGui.drawTexture(matrices, vertexConsumers, "mtr:textures/sign/stripe.png", -1.237F, -0.91F, positionScaled - 0.875F - doorRight, -1.237F, -0.16F, positionScaled - 0.8125F - doorRight, 1, 0.25F, 0, 16.25F, colorAdjustment, light);
			IGui.drawTexture(matrices, vertexConsumers, "mtr:textures/sign/stripe.png", -1.237F, -0.91F, positionScaled + 0.8125F + doorRight, -1.237F, -0.16F, positionScaled + 0.875F + doorRight, 0, 0.25F, 1, 16.25F, colorAdjustment, light);
			IGui.drawTexture(matrices, vertexConsumers, "mtr:textures/sign/stripe.png", -1.175F, -0.875F, positionScaled - 0.8745F, -1.2375F, -0.3125F, positionScaled - 0.8745F, 0, 0, 1, 12, colorAdjustment, light);
			IGui.drawTexture(matrices, vertexConsumers, "mtr:textures/sign/stripe.png", -1.2375F, -0.875F, positionScaled + 0.8745F, -1.175F, -0.3125F, positionScaled + 0.8745F, 1, 0, 0, 12, colorAdjustment, light);
		}
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
}