package mtr.model;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class ModelLightRail extends ModelTrainBase {

	private final ModelPart window;
	private final ModelPart window_exterior;
	private final ModelPart door;
	private final ModelPart door_left;
	private final ModelPart door_right;
	private final ModelPart door_5;
	private final ModelPart door_left_5;
	private final ModelPart door_right_5;
	private final ModelPart door_handrails;
	private final ModelPart handrail_6_r1;
	private final ModelPart door_handrails_4;
	private final ModelPart handrail_6_r2;
	private final ModelPart handrail_5_r1;
	private final ModelPart handrail_3_r1;
	private final ModelPart handrail_2_r1;
	private final ModelPart door_handrails_5;
	private final ModelPart door_edge_8_r1;
	private final ModelPart door_edge_7_r1;
	private final ModelPart door_exterior;
	private final ModelPart door_left_exterior;
	private final ModelPart door_right_exterior;
	private final ModelPart door_exterior_5;
	private final ModelPart door_left_exterior_5;
	private final ModelPart door_right_exterior_5;
	private final ModelPart door_window;
	private final ModelPart door_window_handrails;
	private final ModelPart handrail_6_r3;
	private final ModelPart door_window_handrails_4;
	private final ModelPart handrail_12_r1;
	private final ModelPart handrail_9_r1;
	private final ModelPart handrail_7_r1;
	private final ModelPart door_window_handrails_5;
	private final ModelPart handrail_14_r1;
	private final ModelPart handrail_8_r1;
	private final ModelPart handrail_7_r2;
	private final ModelPart handrail_6_r4;
	private final ModelPart handrail_3_r2;
	private final ModelPart handrail_2_r2;
	private final ModelPart handrail_1_r1;
	private final ModelPart door_window_exterior;
	private final ModelPart roof;
	private final ModelPart inner_roof_2_r1;
	private final ModelPart roof_exterior;
	private final ModelPart outer_roof_3_r1;
	private final ModelPart outer_roof_2_r1;
	private final ModelPart roof_end_exterior;
	private final ModelPart bumper_2_r1;
	private final ModelPart bumper_1_r1;
	private final ModelPart outer_roof_6_r1;
	private final ModelPart outer_roof_5_r1;
	private final ModelPart outer_roof_3_r2;
	private final ModelPart outer_roof_2_r2;
	private final ModelPart roof_light;
	private final ModelPart light_3_r1;
	private final ModelPart light_1_r1;
	private final ModelPart roof_light_5;
	private final ModelPart end;
	private final ModelPart inner_roof_4_r1;
	private final ModelPart inner_roof_2_r2;
	private final ModelPart back_r1;
	private final ModelPart wall_diagonal_2_r1;
	private final ModelPart wall_diagonal_1_r1;
	private final ModelPart end_exterior;
	private final ModelPart back_r2;
	private final ModelPart wall_diagonal_2_r2;
	private final ModelPart wall_diagonal_1_r2;
	private final ModelPart head;
	private final ModelPart head_exterior;
	private final ModelPart wall_diagonal_2_r3;
	private final ModelPart wall_diagonal_1_r3;
	private final ModelPart head_exterior_1;
	private final ModelPart front_r1;
	private final ModelPart head_exterior_3_5;
	private final ModelPart front_r2;
	private final ModelPart head_exterior_4;
	private final ModelPart front_middle_r1;
	private final ModelPart front_top_r1;
	private final ModelPart destination_board_r1;
	private final ModelPart seat;
	private final ModelPart back_right_r1;
	private final ModelPart back_left_r1;
	private final ModelPart seat_green;
	private final ModelPart back_right_r2;
	private final ModelPart seat_purple;
	private final ModelPart back_right_r3;
	private final ModelPart vents_top;
	private final ModelPart headlights;
	private final ModelPart tail_lights;

	private final int phase;

	public ModelLightRail(int phase) {
		this.phase = phase;

		textureWidth = 368;
		textureHeight = 368;
		window = new ModelPart(this);
		window.setPivot(0.0F, 24.0F, 0.0F);
		window.setTextureOffset(98, 0).addCuboid(-20.0F, 0.0F, -32.0F, 20.0F, 1.0F, 64.0F, 0.0F, false);
		window.setTextureOffset(134, 134).addCuboid(-20.0F, -32.0F, -34.0F, 2.0F, 32.0F, 68.0F, 0.0F, false);

		window_exterior = new ModelPart(this);
		window_exterior.setPivot(0.0F, 24.0F, 0.0F);
		window_exterior.setTextureOffset(0, 192).addCuboid(-20.0F, 0.0F, -32.0F, 1.0F, 7.0F, 64.0F, 0.0F, false);
		window_exterior.setTextureOffset(0, 92).addCuboid(-20.0F, -32.0F, -34.0F, 0.0F, 32.0F, 68.0F, 0.0F, false);

		door = new ModelPart(this);
		door.setPivot(0.0F, 24.0F, 0.0F);
		door.setTextureOffset(206, 65).addCuboid(-20.0F, 0.0F, -16.0F, 20.0F, 1.0F, 32.0F, 0.0F, false);

		door_left = new ModelPart(this);
		door_left.setPivot(0.0F, 0.0F, 0.0F);
		door.addChild(door_left);
		door_left.setTextureOffset(168, 219).addCuboid(-20.0F, -32.0F, 0.0F, 0.0F, 32.0F, 15.0F, 0.0F, false);

		door_right = new ModelPart(this);
		door_right.setPivot(0.0F, 0.0F, 0.0F);
		door.addChild(door_right);
		door_right.setTextureOffset(206, 116).addCuboid(-20.0F, -32.0F, -15.0F, 0.0F, 32.0F, 15.0F, 0.0F, false);

		door_5 = new ModelPart(this);
		door_5.setPivot(0.0F, 24.0F, 0.0F);
		door_5.setTextureOffset(206, 65).addCuboid(-20.0F, 0.0F, -16.0F, 20.0F, 1.0F, 32.0F, 0.0F, false);

		door_left_5 = new ModelPart(this);
		door_left_5.setPivot(0.0F, 0.0F, 0.0F);
		door_5.addChild(door_left_5);
		door_left_5.setTextureOffset(167, 218).addCuboid(-20.0F, -32.0F, 0.0F, 0.0F, 32.0F, 16.0F, 0.0F, false);

		door_right_5 = new ModelPart(this);
		door_right_5.setPivot(0.0F, 0.0F, 0.0F);
		door_5.addChild(door_right_5);
		door_right_5.setTextureOffset(205, 115).addCuboid(-20.0F, -32.0F, -16.0F, 0.0F, 32.0F, 16.0F, 0.0F, false);

		door_handrails = new ModelPart(this);
		door_handrails.setPivot(0.0F, 24.0F, 0.0F);
		door_handrails.setTextureOffset(120, 0).addCuboid(-18.0F, -15.0F, 16.0F, 5.0F, 13.0F, 0.0F, 0.0F, false);
		door_handrails.setTextureOffset(120, 0).addCuboid(-18.0F, -15.0F, -16.0F, 5.0F, 13.0F, 0.0F, 0.0F, false);
		door_handrails.setTextureOffset(0, 0).addCuboid(-13.0F, -36.0F, 16.0F, 0.0F, 36.0F, 0.0F, 0.2F, false);
		door_handrails.setTextureOffset(0, 0).addCuboid(-13.0F, -36.0F, -16.0F, 0.0F, 36.0F, 0.0F, 0.2F, false);
		door_handrails.setTextureOffset(4, 0).addCuboid(-9.0F, -36.0F, 0.0F, 0.0F, 36.0F, 0.0F, 0.2F, false);
		door_handrails.setTextureOffset(0, 75).addCuboid(-15.0F, -32.0F, 20.0F, 4.0F, 5.0F, 0.0F, 0.0F, false);
		door_handrails.setTextureOffset(0, 75).addCuboid(-15.0F, -32.0F, 28.0F, 4.0F, 5.0F, 0.0F, 0.0F, false);
		door_handrails.setTextureOffset(0, 75).addCuboid(-15.0F, -32.0F, 36.0F, 4.0F, 5.0F, 0.0F, 0.0F, false);
		door_handrails.setTextureOffset(0, 75).addCuboid(-15.0F, -32.0F, 44.0F, 4.0F, 5.0F, 0.0F, 0.0F, false);
		door_handrails.setTextureOffset(0, 75).addCuboid(-15.0F, -32.0F, -44.0F, 4.0F, 5.0F, 0.0F, 0.0F, false);
		door_handrails.setTextureOffset(0, 75).addCuboid(-15.0F, -32.0F, -36.0F, 4.0F, 5.0F, 0.0F, 0.0F, false);
		door_handrails.setTextureOffset(0, 75).addCuboid(-15.0F, -32.0F, -28.0F, 4.0F, 5.0F, 0.0F, 0.0F, false);
		door_handrails.setTextureOffset(0, 75).addCuboid(-15.0F, -32.0F, -20.0F, 4.0F, 5.0F, 0.0F, 0.0F, false);

		handrail_6_r1 = new ModelPart(this);
		handrail_6_r1.setPivot(0.0F, 0.0F, 0.0F);
		door_handrails.addChild(handrail_6_r1);
		setRotationAngle(handrail_6_r1, -1.5708F, 0.0F, 0.0F);
		handrail_6_r1.setTextureOffset(8, 0).addCuboid(-13.0F, 16.0F, -32.0F, 0.0F, 32.0F, 0.0F, 0.2F, false);
		handrail_6_r1.setTextureOffset(8, 0).addCuboid(-13.0F, -48.0F, -32.0F, 0.0F, 32.0F, 0.0F, 0.2F, false);

		door_handrails_4 = new ModelPart(this);
		door_handrails_4.setPivot(0.0F, 24.0F, 0.0F);
		door_handrails_4.setTextureOffset(4, 6).addCuboid(-15.8F, -17.0F, -15.0F, 0.0F, 4.0F, 0.0F, 0.2F, false);
		door_handrails_4.setTextureOffset(4, 6).addCuboid(-15.8F, -17.0F, 15.0F, 0.0F, 4.0F, 0.0F, 0.2F, false);
		door_handrails_4.setTextureOffset(0, 75).addCuboid(-5.0F, -32.0F, -12.0F, 4.0F, 5.0F, 0.0F, 0.0F, false);
		door_handrails_4.setTextureOffset(0, 75).addCuboid(-5.0F, -32.0F, -4.0F, 4.0F, 5.0F, 0.0F, 0.0F, false);
		door_handrails_4.setTextureOffset(0, 75).addCuboid(-5.0F, -32.0F, 4.0F, 4.0F, 5.0F, 0.0F, 0.0F, false);
		door_handrails_4.setTextureOffset(0, 75).addCuboid(-5.0F, -32.0F, 12.0F, 4.0F, 5.0F, 0.0F, 0.0F, false);
		door_handrails_4.setTextureOffset(0, 75).addCuboid(-5.0F, -32.0F, 20.0F, 4.0F, 5.0F, 0.0F, 0.0F, false);
		door_handrails_4.setTextureOffset(0, 75).addCuboid(-5.0F, -32.0F, 28.0F, 4.0F, 5.0F, 0.0F, 0.0F, false);
		door_handrails_4.setTextureOffset(0, 75).addCuboid(-5.0F, -32.0F, 36.0F, 4.0F, 5.0F, 0.0F, 0.0F, false);
		door_handrails_4.setTextureOffset(0, 75).addCuboid(-5.0F, -32.0F, 44.0F, 4.0F, 5.0F, 0.0F, 0.0F, false);
		door_handrails_4.setTextureOffset(0, 75).addCuboid(-5.0F, -32.0F, -44.0F, 4.0F, 5.0F, 0.0F, 0.0F, false);
		door_handrails_4.setTextureOffset(0, 75).addCuboid(-5.0F, -32.0F, -36.0F, 4.0F, 5.0F, 0.0F, 0.0F, false);
		door_handrails_4.setTextureOffset(0, 75).addCuboid(-5.0F, -32.0F, -28.0F, 4.0F, 5.0F, 0.0F, 0.0F, false);
		door_handrails_4.setTextureOffset(0, 75).addCuboid(-5.0F, -32.0F, -20.0F, 4.0F, 5.0F, 0.0F, 0.0F, false);
		door_handrails_4.setTextureOffset(358, 0).addCuboid(-20.0F, -32.0F, 14.0F, 3.0F, 32.0F, 2.0F, 0.0F, false);
		door_handrails_4.setTextureOffset(346, 0).addCuboid(-20.0F, -32.0F, -16.0F, 3.0F, 32.0F, 2.0F, 0.0F, false);

		handrail_6_r2 = new ModelPart(this);
		handrail_6_r2.setPivot(-15.6F, -17.2F, 14.8F);
		door_handrails_4.addChild(handrail_6_r2);
		setRotationAngle(handrail_6_r2, 0.0F, 0.0F, -0.3491F);
		handrail_6_r2.setTextureOffset(4, 6).addCuboid(-0.2F, -5.2F, 0.2F, 0.0F, 5.0F, 0.0F, 0.2F, false);

		handrail_5_r1 = new ModelPart(this);
		handrail_5_r1.setPivot(-15.6F, -12.8F, 14.8F);
		door_handrails_4.addChild(handrail_5_r1);
		setRotationAngle(handrail_5_r1, 0.0F, 0.0F, 0.3491F);
		handrail_5_r1.setTextureOffset(4, 6).addCuboid(-0.2F, 0.2F, 0.2F, 0.0F, 5.0F, 0.0F, 0.2F, false);

		handrail_3_r1 = new ModelPart(this);
		handrail_3_r1.setPivot(-15.6F, -17.2F, -15.2F);
		door_handrails_4.addChild(handrail_3_r1);
		setRotationAngle(handrail_3_r1, 0.0F, 0.0F, -0.3491F);
		handrail_3_r1.setTextureOffset(4, 6).addCuboid(-0.2F, -5.2F, 0.2F, 0.0F, 5.0F, 0.0F, 0.2F, false);

		handrail_2_r1 = new ModelPart(this);
		handrail_2_r1.setPivot(-15.6F, -12.8F, -15.2F);
		door_handrails_4.addChild(handrail_2_r1);
		setRotationAngle(handrail_2_r1, 0.0F, 0.0F, 0.3491F);
		handrail_2_r1.setTextureOffset(4, 6).addCuboid(-0.2F, 0.2F, 0.2F, 0.0F, 5.0F, 0.0F, 0.2F, false);

		door_handrails_5 = new ModelPart(this);
		door_handrails_5.setPivot(0.0F, 24.0F, 0.0F);
		door_handrails_5.setTextureOffset(0, 3).addCuboid(-17.25F, -26.0F, 14.25F, 0.0F, 19.0F, 0.0F, 0.2F, false);
		door_handrails_5.setTextureOffset(0, 3).addCuboid(-17.25F, -26.0F, -14.25F, 0.0F, 19.0F, 0.0F, 0.2F, false);
		door_handrails_5.setTextureOffset(0, 75).addCuboid(-5.0F, -32.5F, -12.0F, 4.0F, 5.0F, 0.0F, 0.0F, false);
		door_handrails_5.setTextureOffset(0, 75).addCuboid(-5.0F, -32.5F, -4.0F, 4.0F, 5.0F, 0.0F, 0.0F, false);
		door_handrails_5.setTextureOffset(0, 75).addCuboid(-5.0F, -32.5F, 4.0F, 4.0F, 5.0F, 0.0F, 0.0F, false);
		door_handrails_5.setTextureOffset(0, 75).addCuboid(-5.0F, -32.5F, 12.0F, 4.0F, 5.0F, 0.0F, 0.0F, false);
		door_handrails_5.setTextureOffset(0, 75).addCuboid(-5.0F, -32.5F, 20.0F, 4.0F, 5.0F, 0.0F, 0.0F, false);
		door_handrails_5.setTextureOffset(0, 75).addCuboid(-5.0F, -32.5F, 28.0F, 4.0F, 5.0F, 0.0F, 0.0F, false);
		door_handrails_5.setTextureOffset(0, 75).addCuboid(-5.0F, -32.5F, 36.0F, 4.0F, 5.0F, 0.0F, 0.0F, false);
		door_handrails_5.setTextureOffset(0, 75).addCuboid(-5.0F, -32.5F, 44.0F, 4.0F, 5.0F, 0.0F, 0.0F, false);
		door_handrails_5.setTextureOffset(0, 75).addCuboid(-5.0F, -32.5F, -44.0F, 4.0F, 5.0F, 0.0F, 0.0F, false);
		door_handrails_5.setTextureOffset(0, 75).addCuboid(-5.0F, -32.5F, -36.0F, 4.0F, 5.0F, 0.0F, 0.0F, false);
		door_handrails_5.setTextureOffset(0, 75).addCuboid(-5.0F, -32.5F, -28.0F, 4.0F, 5.0F, 0.0F, 0.0F, false);
		door_handrails_5.setTextureOffset(0, 75).addCuboid(-5.0F, -32.5F, -20.0F, 4.0F, 5.0F, 0.0F, 0.0F, false);
		door_handrails_5.setTextureOffset(358, 0).addCuboid(-20.0F, -32.0F, 14.0F, 3.0F, 32.0F, 2.0F, 0.0F, false);
		door_handrails_5.setTextureOffset(346, 0).addCuboid(-20.0F, -32.0F, -16.0F, 3.0F, 32.0F, 2.0F, 0.0F, false);
		door_handrails_5.setTextureOffset(361, 38).addCuboid(-19.0F, -7.0F, -16.0F, 2.0F, 0.0F, 2.0F, 0.0F, false);
		door_handrails_5.setTextureOffset(363, 64).addCuboid(-18.25F, -26.0F, -15.25F, 2.0F, 19.0F, 0.0F, 0.0F, false);
		door_handrails_5.setTextureOffset(361, 38).addCuboid(-19.0F, -7.0F, 13.25F, 2.0F, 0.0F, 2.0F, 0.0F, false);

		door_edge_8_r1 = new ModelPart(this);
		door_edge_8_r1.setPivot(0.0F, 0.0F, 0.0F);
		door_handrails_5.addChild(door_edge_8_r1);
		setRotationAngle(door_edge_8_r1, 0.0F, 3.1416F, 0.0F);
		door_edge_8_r1.setTextureOffset(363, 64).addCuboid(16.75F, -26.0F, -15.25F, 2.0F, 19.0F, 0.0F, 0.0F, false);

		door_edge_7_r1 = new ModelPart(this);
		door_edge_7_r1.setPivot(0.0F, 0.0F, 0.0F);
		door_handrails_5.addChild(door_edge_7_r1);
		setRotationAngle(door_edge_7_r1, 0.0F, 0.0F, -3.1416F);
		door_edge_7_r1.setTextureOffset(361, 38).addCuboid(16.0F, 26.0F, 13.25F, 2.0F, 0.0F, 2.0F, 0.0F, false);
		door_edge_7_r1.setTextureOffset(361, 38).addCuboid(16.0F, 26.0F, -16.0F, 2.0F, 0.0F, 2.0F, 0.0F, false);

		door_exterior = new ModelPart(this);
		door_exterior.setPivot(0.0F, 24.0F, 0.0F);
		door_exterior.setTextureOffset(278, 29).addCuboid(-20.0F, 0.0F, -16.0F, 1.0F, 7.0F, 32.0F, 0.0F, false);
		door_exterior.setTextureOffset(0, 0).addCuboid(-21.0F, -34.0F, -48.0F, 1.0F, 2.0F, 96.0F, 0.0F, false);

		door_left_exterior = new ModelPart(this);
		door_left_exterior.setPivot(0.0F, 0.0F, 0.0F);
		door_exterior.addChild(door_left_exterior);
		door_left_exterior.setTextureOffset(234, 311).addCuboid(-21.0F, -32.0F, 0.0F, 1.0F, 33.0F, 15.0F, 0.0F, false);

		door_right_exterior = new ModelPart(this);
		door_right_exterior.setPivot(0.0F, 0.0F, 0.0F);
		door_exterior.addChild(door_right_exterior);
		door_right_exterior.setTextureOffset(202, 300).addCuboid(-21.0F, -32.0F, -15.0F, 1.0F, 33.0F, 15.0F, 0.0F, false);

		door_exterior_5 = new ModelPart(this);
		door_exterior_5.setPivot(0.0F, 24.0F, 0.0F);
		door_exterior_5.setTextureOffset(278, 29).addCuboid(-20.0F, 0.0F, -16.0F, 1.0F, 7.0F, 32.0F, 0.0F, false);
		door_exterior_5.setTextureOffset(0, 0).addCuboid(-21.0F, -34.0F, -48.0F, 1.0F, 2.0F, 96.0F, 0.0F, false);

		door_left_exterior_5 = new ModelPart(this);
		door_left_exterior_5.setPivot(0.0F, 0.0F, 0.0F);
		door_exterior_5.addChild(door_left_exterior_5);
		door_left_exterior_5.setTextureOffset(242, 311).addCuboid(-21.0F, -32.0F, 0.0F, 1.0F, 33.0F, 16.0F, 0.0F, false);

		door_right_exterior_5 = new ModelPart(this);
		door_right_exterior_5.setPivot(0.0F, 0.0F, 0.0F);
		door_exterior_5.addChild(door_right_exterior_5);
		door_right_exterior_5.setTextureOffset(202, 300).addCuboid(-21.0F, -32.0F, -16.0F, 1.0F, 33.0F, 16.0F, 0.0F, false);

		door_window = new ModelPart(this);
		door_window.setPivot(0.0F, 24.0F, 0.0F);
		door_window.setTextureOffset(202, 28).addCuboid(0.0F, 0.0F, -16.0F, 20.0F, 1.0F, 32.0F, 0.0F, false);
		door_window.setTextureOffset(0, 98).addCuboid(18.0F, -32.0F, -14.0F, 2.0F, 32.0F, 28.0F, 0.0F, true);

		door_window_handrails = new ModelPart(this);
		door_window_handrails.setPivot(0.0F, 24.0F, 0.0F);
		door_window_handrails.setTextureOffset(32, 98).addCuboid(7.0F, -15.0F, 16.0F, 11.0F, 13.0F, 0.0F, 0.0F, false);
		door_window_handrails.setTextureOffset(32, 98).addCuboid(7.0F, -15.0F, -16.0F, 11.0F, 13.0F, 0.0F, 0.0F, false);
		door_window_handrails.setTextureOffset(0, 0).addCuboid(7.0F, -36.0F, 16.0F, 0.0F, 36.0F, 0.0F, 0.2F, false);
		door_window_handrails.setTextureOffset(0, 0).addCuboid(7.0F, -36.0F, -16.0F, 0.0F, 36.0F, 0.0F, 0.2F, false);
		door_window_handrails.setTextureOffset(4, 0).addCuboid(7.0F, -36.0F, 0.0F, 0.0F, 36.0F, 0.0F, 0.2F, false);
		door_window_handrails.setTextureOffset(0, 75).addCuboid(5.0F, -32.0F, 12.0F, 4.0F, 5.0F, 0.0F, 0.0F, false);
		door_window_handrails.setTextureOffset(0, 75).addCuboid(5.0F, -32.0F, 4.0F, 4.0F, 5.0F, 0.0F, 0.0F, false);
		door_window_handrails.setTextureOffset(0, 75).addCuboid(5.0F, -32.0F, -4.0F, 4.0F, 5.0F, 0.0F, 0.0F, false);
		door_window_handrails.setTextureOffset(0, 75).addCuboid(5.0F, -32.0F, -12.0F, 4.0F, 5.0F, 0.0F, 0.0F, false);
		door_window_handrails.setTextureOffset(0, 75).addCuboid(5.0F, -32.0F, 20.0F, 4.0F, 5.0F, 0.0F, 0.0F, false);
		door_window_handrails.setTextureOffset(0, 75).addCuboid(5.0F, -32.0F, 28.0F, 4.0F, 5.0F, 0.0F, 0.0F, false);
		door_window_handrails.setTextureOffset(0, 75).addCuboid(5.0F, -32.0F, 36.0F, 4.0F, 5.0F, 0.0F, 0.0F, false);
		door_window_handrails.setTextureOffset(0, 75).addCuboid(5.0F, -32.0F, 44.0F, 4.0F, 5.0F, 0.0F, 0.0F, false);
		door_window_handrails.setTextureOffset(0, 75).addCuboid(5.0F, -32.0F, -44.0F, 4.0F, 5.0F, 0.0F, 0.0F, false);
		door_window_handrails.setTextureOffset(0, 75).addCuboid(5.0F, -32.0F, -36.0F, 4.0F, 5.0F, 0.0F, 0.0F, false);
		door_window_handrails.setTextureOffset(0, 75).addCuboid(5.0F, -32.0F, -28.0F, 4.0F, 5.0F, 0.0F, 0.0F, false);
		door_window_handrails.setTextureOffset(0, 75).addCuboid(5.0F, -32.0F, -20.0F, 4.0F, 5.0F, 0.0F, 0.0F, false);

		handrail_6_r3 = new ModelPart(this);
		handrail_6_r3.setPivot(0.0F, 0.0F, 0.0F);
		door_window_handrails.addChild(handrail_6_r3);
		setRotationAngle(handrail_6_r3, -1.5708F, 0.0F, 0.0F);
		handrail_6_r3.setTextureOffset(8, 0).addCuboid(7.0F, 16.0F, -32.0F, 0.0F, 32.0F, 0.0F, 0.2F, false);
		handrail_6_r3.setTextureOffset(8, 0).addCuboid(7.0F, -48.0F, -32.0F, 0.0F, 32.0F, 0.0F, 0.2F, false);
		handrail_6_r3.setTextureOffset(8, 0).addCuboid(7.0F, -16.0F, -32.0F, 0.0F, 32.0F, 0.0F, 0.2F, false);

		door_window_handrails_4 = new ModelPart(this);
		door_window_handrails_4.setPivot(0.0F, 24.0F, 0.0F);
		door_window_handrails_4.setTextureOffset(0, 98).addCuboid(5.0F, -13.0F, 16.0F, 13.0F, 11.0F, 0.0F, 0.0F, false);
		door_window_handrails_4.setTextureOffset(0, 98).addCuboid(5.0F, -13.0F, -16.0F, 13.0F, 11.0F, 0.0F, 0.0F, false);
		door_window_handrails_4.setTextureOffset(0, 9).addCuboid(5.6F, -27.25F, -16.0F, 0.0F, 15.0F, 0.0F, 0.2F, false);
		door_window_handrails_4.setTextureOffset(0, 9).addCuboid(5.6F, -27.25F, 16.0F, 0.0F, 15.0F, 0.0F, 0.2F, false);
		door_window_handrails_4.setTextureOffset(0, 75).addCuboid(4.4F, -32.0F, 8.0F, 4.0F, 5.0F, 0.0F, 0.0F, false);
		door_window_handrails_4.setTextureOffset(0, 75).addCuboid(4.4F, -32.0F, 0.0F, 4.0F, 5.0F, 0.0F, 0.0F, false);
		door_window_handrails_4.setTextureOffset(0, 75).addCuboid(4.4F, -32.0F, -8.0F, 4.0F, 5.0F, 0.0F, 0.0F, false);

		handrail_12_r1 = new ModelPart(this);
		handrail_12_r1.setPivot(0.0F, 0.0F, 0.0F);
		door_window_handrails_4.addChild(handrail_12_r1);
		setRotationAngle(handrail_12_r1, -1.5708F, 0.0F, 0.0F);
		handrail_12_r1.setTextureOffset(8, 0).addCuboid(-3.0F, -48.0F, -32.0F, 0.0F, 32.0F, 0.0F, 0.2F, false);
		handrail_12_r1.setTextureOffset(8, 0).addCuboid(-3.0F, -16.0F, -32.0F, 0.0F, 32.0F, 0.0F, 0.2F, false);
		handrail_12_r1.setTextureOffset(8, 0).addCuboid(-3.0F, 16.0F, -32.0F, 0.0F, 32.0F, 0.0F, 0.2F, false);
		handrail_12_r1.setTextureOffset(8, 0).addCuboid(6.4F, -16.0F, -32.0F, 0.0F, 32.0F, 0.0F, 0.2F, false);

		handrail_9_r1 = new ModelPart(this);
		handrail_9_r1.setPivot(6.0F, -28.25F, -16.0F);
		door_window_handrails_4.addChild(handrail_9_r1);
		setRotationAngle(handrail_9_r1, 0.0F, 0.0F, 0.1745F);
		handrail_9_r1.setTextureOffset(0, 30).addCuboid(-0.25F, -8.25F, 32.0F, 0.0F, 9.0F, 0.0F, 0.2F, false);
		handrail_9_r1.setTextureOffset(0, 30).addCuboid(-0.25F, -8.25F, 0.0F, 0.0F, 9.0F, 0.0F, 0.2F, false);

		handrail_7_r1 = new ModelPart(this);
		handrail_7_r1.setPivot(6.0F, -3.5F, -16.0F);
		door_window_handrails_4.addChild(handrail_7_r1);
		setRotationAngle(handrail_7_r1, 0.0F, 0.0F, -0.2182F);
		handrail_7_r1.setTextureOffset(0, 30).addCuboid(1.5F, -8.25F, 32.0F, 0.0F, 12.0F, 0.0F, 0.2F, false);
		handrail_7_r1.setTextureOffset(0, 30).addCuboid(1.5F, -8.25F, 0.0F, 0.0F, 12.0F, 0.0F, 0.2F, false);

		door_window_handrails_5 = new ModelPart(this);
		door_window_handrails_5.setPivot(0.0F, 24.0F, 0.0F);
		door_window_handrails_5.setTextureOffset(0, 98).addCuboid(5.0F, -12.75F, 16.0F, 13.0F, 13.0F, 0.0F, 0.0F, false);
		door_window_handrails_5.setTextureOffset(0, 98).addCuboid(5.0F, -12.75F, -16.0F, 13.0F, 13.0F, 0.0F, 0.0F, false);
		door_window_handrails_5.setTextureOffset(0, 30).addCuboid(5.6F, -13.0F, -16.0F, 0.0F, 11.0F, 0.0F, 0.2F, false);
		door_window_handrails_5.setTextureOffset(0, 30).addCuboid(5.6F, -13.0F, 16.0F, 0.0F, 11.0F, 0.0F, 0.2F, false);
		door_window_handrails_5.setTextureOffset(0, 30).addCuboid(9.2F, -36.2738F, -11.5F, 0.0F, 5.0F, 0.0F, 0.2F, false);
		door_window_handrails_5.setTextureOffset(0, 30).addCuboid(9.2F, -36.2738F, 11.5F, 0.0F, 5.0F, 0.0F, 0.2F, false);
		door_window_handrails_5.setTextureOffset(0, 75).addCuboid(6.9F, -31.75F, 10.0F, 4.0F, 5.0F, 0.0F, 0.0F, false);
		door_window_handrails_5.setTextureOffset(0, 75).addCuboid(7.15F, -31.75F, 3.5F, 4.0F, 5.0F, 0.0F, 0.0F, false);
		door_window_handrails_5.setTextureOffset(0, 75).addCuboid(7.15F, -31.75F, -3.5F, 4.0F, 5.0F, 0.0F, 0.0F, false);
		door_window_handrails_5.setTextureOffset(0, 75).addCuboid(7.15F, -31.75F, -10.0F, 4.0F, 5.0F, 0.0F, 0.0F, false);
		door_window_handrails_5.setTextureOffset(32, 94).addCuboid(17.99F, -31.5F, -18.0F, 0.0F, 18.0F, 4.0F, 0.0F, false);
		door_window_handrails_5.setTextureOffset(32, 94).addCuboid(17.99F, -31.5F, 14.0F, 0.0F, 18.0F, 4.0F, 0.0F, false);

		handrail_14_r1 = new ModelPart(this);
		handrail_14_r1.setPivot(0.0F, 0.0F, 0.0F);
		door_window_handrails_5.addChild(handrail_14_r1);
		setRotationAngle(handrail_14_r1, -1.5708F, 0.0F, 0.0F);
		handrail_14_r1.setTextureOffset(8, 0).addCuboid(-3.0F, -48.0F, -32.0F, 0.0F, 32.0F, 0.0F, 0.2F, false);
		handrail_14_r1.setTextureOffset(8, 0).addCuboid(-3.0F, -16.0F, -32.0F, 0.0F, 32.0F, 0.0F, 0.2F, false);
		handrail_14_r1.setTextureOffset(8, 0).addCuboid(-3.0F, 16.0F, -32.0F, 0.0F, 32.0F, 0.0F, 0.2F, false);
		handrail_14_r1.setTextureOffset(8, 0).addCuboid(9.15F, -16.0F, -31.25F, 0.0F, 32.0F, 0.0F, 0.2F, false);

		handrail_8_r1 = new ModelPart(this);
		handrail_8_r1.setPivot(5.9238F, -16.714F, 16.0F);
		door_window_handrails_5.addChild(handrail_8_r1);
		setRotationAngle(handrail_8_r1, 0.0F, 0.0F, 0.0873F);
		handrail_8_r1.setTextureOffset(0, 9).addCuboid(0.0F, -3.5F, 0.0F, 0.0F, 7.0F, 0.0F, 0.2F, false);

		handrail_7_r2 = new ModelPart(this);
		handrail_7_r2.setPivot(8.5226F, -29.7676F, 16.0F);
		door_window_handrails_5.addChild(handrail_7_r2);
		setRotationAngle(handrail_7_r2, 0.0F, 0.0F, 0.3491F);
		handrail_7_r2.setTextureOffset(0, 3).addCuboid(0.0F, -1.5F, 0.0F, 0.0F, 3.0F, 0.0F, 0.2F, false);

		handrail_6_r4 = new ModelPart(this);
		handrail_6_r4.setPivot(7.0994F, -24.3136F, 16.0F);
		door_window_handrails_5.addChild(handrail_6_r4);
		setRotationAngle(handrail_6_r4, 0.0F, 0.0F, 0.2182F);
		handrail_6_r4.setTextureOffset(0, 9).addCuboid(0.0F, -4.0F, 0.0F, 0.0F, 8.0F, 0.0F, 0.2F, false);

		handrail_3_r2 = new ModelPart(this);
		handrail_3_r2.setPivot(8.5935F, -30.2738F, -16.0F);
		door_window_handrails_5.addChild(handrail_3_r2);
		setRotationAngle(handrail_3_r2, 0.0F, 0.0F, 0.3491F);
		handrail_3_r2.setTextureOffset(0, 3).addCuboid(0.1065F, -1.0F, 0.0F, 0.0F, 3.0F, 0.0F, 0.2F, false);

		handrail_2_r2 = new ModelPart(this);
		handrail_2_r2.setPivot(5.6F, -18.75F, -16.0F);
		door_window_handrails_5.addChild(handrail_2_r2);
		setRotationAngle(handrail_2_r2, 0.0F, 0.0F, 0.0873F);
		handrail_2_r2.setTextureOffset(0, 9).addCuboid(0.5F, -1.5F, 0.0F, 0.0F, 7.0F, 0.0F, 0.2F, false);

		handrail_1_r1 = new ModelPart(this);
		handrail_1_r1.setPivot(7.0761F, -24.3188F, -16.0F);
		door_window_handrails_5.addChild(handrail_1_r1);
		setRotationAngle(handrail_1_r1, 0.0F, 0.0F, 0.2182F);
		handrail_1_r1.setTextureOffset(0, 9).addCuboid(0.0239F, -4.0F, 0.0F, 0.0F, 8.0F, 0.0F, 0.2F, false);

		door_window_exterior = new ModelPart(this);
		door_window_exterior.setPivot(0.0F, 24.0F, 0.0F);
		door_window_exterior.setTextureOffset(36, 263).addCuboid(19.0F, 0.0F, -16.0F, 1.0F, 7.0F, 32.0F, 0.0F, true);
		door_window_exterior.setTextureOffset(98, 0).addCuboid(20.0F, -32.0F, -14.0F, 0.0F, 32.0F, 28.0F, 0.0F, true);

		roof = new ModelPart(this);
		roof.setPivot(0.0F, 24.0F, 0.0F);
		roof.setTextureOffset(122, 0).addCuboid(-20.0F, -32.0F, -16.0F, 3.0F, 0.0F, 32.0F, 0.0F, false);
		roof.setTextureOffset(36, 36).addCuboid(-14.0F, -36.0F, -16.0F, 14.0F, 0.0F, 32.0F, 0.0F, false);

		inner_roof_2_r1 = new ModelPart(this);
		inner_roof_2_r1.setPivot(-17.0F, -32.0F, 0.0F);
		roof.addChild(inner_roof_2_r1);
		setRotationAngle(inner_roof_2_r1, 0.0F, 0.0F, -0.8727F);
		inner_roof_2_r1.setTextureOffset(109, 98).addCuboid(0.0F, 0.0F, -16.0F, 6.0F, 0.0F, 32.0F, 0.0F, false);

		roof_exterior = new ModelPart(this);
		roof_exterior.setPivot(0.0F, 24.0F, 0.0F);
		roof_exterior.setTextureOffset(0, 39).addCuboid(-20.0F, -36.0F, -16.0F, 0.0F, 4.0F, 32.0F, 0.0F, false);
		roof_exterior.setTextureOffset(242, 242).addCuboid(-17.0F, -39.0F, -16.0F, 17.0F, 1.0F, 32.0F, 0.0F, false);

		outer_roof_3_r1 = new ModelPart(this);
		outer_roof_3_r1.setPivot(-19.0F, -37.7329F, 0.0F);
		roof_exterior.addChild(outer_roof_3_r1);
		setRotationAngle(outer_roof_3_r1, 0.0F, 0.0F, -0.5236F);
		outer_roof_3_r1.setTextureOffset(121, 98).addCuboid(0.0F, 0.001F, -16.0F, 3.0F, 0.0F, 32.0F, 0.0F, false);

		outer_roof_2_r1 = new ModelPart(this);
		outer_roof_2_r1.setPivot(-20.0F, -36.0F, 0.0F);
		roof_exterior.addChild(outer_roof_2_r1);
		setRotationAngle(outer_roof_2_r1, 0.0F, 0.0F, -1.0472F);
		outer_roof_2_r1.setTextureOffset(60, 0).addCuboid(0.0F, 0.0F, -16.0F, 2.0F, 0.0F, 32.0F, 0.0F, false);

		roof_end_exterior = new ModelPart(this);
		roof_end_exterior.setPivot(0.0F, 24.0F, 0.0F);
		roof_end_exterior.setTextureOffset(202, 0).addCuboid(-17.0F, -39.0F, 16.0F, 34.0F, 1.0F, 27.0F, 0.0F, false);
		roof_end_exterior.setTextureOffset(266, 138).addCuboid(-10.0F, -2.0F, 43.0F, 20.0F, 3.0F, 5.0F, 0.0F, false);

		bumper_2_r1 = new ModelPart(this);
		bumper_2_r1.setPivot(20.0F, 0.0F, 16.0F);
		roof_end_exterior.addChild(bumper_2_r1);
		setRotationAngle(bumper_2_r1, 0.0F, -0.3491F, 0.0F);
		bumper_2_r1.setTextureOffset(202, 28).addCuboid(0.0F, -2.0F, 23.0F, 1.0F, 3.0F, 10.0F, 0.0F, true);
		bumper_2_r1.setTextureOffset(0, 225).addCuboid(0.0F, 0.0F, 0.0F, 0.0F, 7.0F, 23.0F, 0.0F, true);

		bumper_1_r1 = new ModelPart(this);
		bumper_1_r1.setPivot(-20.0F, 0.0F, 16.0F);
		roof_end_exterior.addChild(bumper_1_r1);
		setRotationAngle(bumper_1_r1, 0.0F, 0.3491F, 0.0F);
		bumper_1_r1.setTextureOffset(202, 28).addCuboid(-1.0F, -2.0F, 23.0F, 1.0F, 3.0F, 10.0F, 0.0F, false);
		bumper_1_r1.setTextureOffset(0, 225).addCuboid(0.0F, 0.0F, 0.0F, 0.0F, 7.0F, 23.0F, 0.0F, false);

		outer_roof_6_r1 = new ModelPart(this);
		outer_roof_6_r1.setPivot(20.0F, -36.0F, 16.0F);
		roof_end_exterior.addChild(outer_roof_6_r1);
		setRotationAngle(outer_roof_6_r1, -0.2967F, 0.0F, 1.0472F);
		outer_roof_6_r1.setTextureOffset(69, 65).addCuboid(-7.0F, 0.0F, 0.0F, 7.0F, 0.0F, 29.0F, 0.0F, true);

		outer_roof_5_r1 = new ModelPart(this);
		outer_roof_5_r1.setPivot(18.981F, -37.6982F, 16.0159F);
		roof_end_exterior.addChild(outer_roof_5_r1);
		setRotationAngle(outer_roof_5_r1, -0.1745F, 0.0F, 0.5236F);
		outer_roof_5_r1.setTextureOffset(70, 0).addCuboid(-11.0F, -0.0354F, -0.0226F, 11.0F, 0.0F, 28.0F, 0.0F, true);

		outer_roof_3_r2 = new ModelPart(this);
		outer_roof_3_r2.setPivot(-18.981F, -37.6982F, 16.0159F);
		roof_end_exterior.addChild(outer_roof_3_r2);
		setRotationAngle(outer_roof_3_r2, -0.1745F, 0.0F, -0.5236F);
		outer_roof_3_r2.setTextureOffset(70, 0).addCuboid(0.0F, -0.0354F, -0.0226F, 11.0F, 0.0F, 28.0F, 0.0F, false);

		outer_roof_2_r2 = new ModelPart(this);
		outer_roof_2_r2.setPivot(-20.0F, -36.0F, 16.0F);
		roof_end_exterior.addChild(outer_roof_2_r2);
		setRotationAngle(outer_roof_2_r2, -0.2967F, 0.0F, -1.0472F);
		outer_roof_2_r2.setTextureOffset(69, 65).addCuboid(0.0F, 0.0F, 0.0F, 7.0F, 0.0F, 29.0F, 0.0F, false);

		roof_light = new ModelPart(this);
		roof_light.setPivot(0.0F, 24.0F, 0.0F);
		roof_light.setTextureOffset(122, 32).addCuboid(-12.0F, -35.5F, -16.0F, 2.0F, 0.0F, 32.0F, 0.0F, false);

		light_3_r1 = new ModelPart(this);
		light_3_r1.setPivot(-10.0F, -35.5F, 0.0F);
		roof_light.addChild(light_3_r1);
		setRotationAngle(light_3_r1, 0.0F, 0.0F, -1.0472F);
		light_3_r1.setTextureOffset(126, 32).addCuboid(0.0F, 0.0F, -16.0F, 1.0F, 0.0F, 32.0F, 0.0F, false);

		light_1_r1 = new ModelPart(this);
		light_1_r1.setPivot(-12.0F, -35.5F, 0.0F);
		roof_light.addChild(light_1_r1);
		setRotationAngle(light_1_r1, 0.0F, 0.0F, 1.0472F);
		light_1_r1.setTextureOffset(127, 98).addCuboid(-1.0F, 0.0F, -16.0F, 1.0F, 0.0F, 32.0F, 0.0F, false);

		roof_light_5 = new ModelPart(this);
		roof_light_5.setPivot(0.0F, 24.0F, 0.0F);
		roof_light_5.setTextureOffset(122, 32).addCuboid(-12.0F, -36.0F, -16.0F, 2.0F, 0.0F, 32.0F, 0.0F, false);

		end = new ModelPart(this);
		end.setPivot(0.0F, 24.0F, 0.0F);
		end.setTextureOffset(0, 98).addCuboid(-20.0F, 0.0F, -16.0F, 40.0F, 1.0F, 61.0F, 0.0F, false);
		end.setTextureOffset(168, 234).addCuboid(-20.0F, -32.0F, -18.0F, 2.0F, 32.0F, 34.0F, 0.0F, false);
		end.setTextureOffset(168, 234).addCuboid(18.0F, -32.0F, -18.0F, 2.0F, 32.0F, 34.0F, 0.0F, true);
		end.setTextureOffset(278, 68).addCuboid(-8.0F, -9.0F, 44.0F, 16.0F, 9.0F, 0.0F, 0.0F, false);
		end.setTextureOffset(21, 25).addCuboid(-20.0F, -32.0F, 16.0F, 3.0F, 0.0F, 3.0F, 0.0F, false);
		end.setTextureOffset(9, 0).addCuboid(-14.0F, -36.0F, 16.0F, 28.0F, 0.0F, 27.0F, 0.0F, false);
		end.setTextureOffset(21, 25).addCuboid(17.0F, -32.0F, 16.0F, 3.0F, 0.0F, 3.0F, 0.0F, true);

		inner_roof_4_r1 = new ModelPart(this);
		inner_roof_4_r1.setPivot(17.0F, -32.0F, 0.0F);
		end.addChild(inner_roof_4_r1);
		setRotationAngle(inner_roof_4_r1, 0.0F, 0.0F, 0.8727F);
		inner_roof_4_r1.setTextureOffset(0, 0).addCuboid(-6.0F, 0.0F, 16.0F, 6.0F, 0.0F, 12.0F, 0.0F, true);

		inner_roof_2_r2 = new ModelPart(this);
		inner_roof_2_r2.setPivot(-17.0F, -32.0F, 0.0F);
		end.addChild(inner_roof_2_r2);
		setRotationAngle(inner_roof_2_r2, 0.0F, 0.0F, -0.8727F);
		inner_roof_2_r2.setTextureOffset(0, 0).addCuboid(0.0F, 0.0F, 16.0F, 6.0F, 0.0F, 12.0F, 0.0F, false);

		back_r1 = new ModelPart(this);
		back_r1.setPivot(0.0F, -9.0F, 46.0F);
		end.addChild(back_r1);
		setRotationAngle(back_r1, 0.0873F, 0.0F, 0.0F);
		back_r1.setTextureOffset(274, 28).addCuboid(-9.0F, -30.0F, -2.0F, 18.0F, 30.0F, 0.0F, 0.0F, false);

		wall_diagonal_2_r1 = new ModelPart(this);
		wall_diagonal_2_r1.setPivot(20.0F, 0.0F, 16.0F);
		end.addChild(wall_diagonal_2_r1);
		setRotationAngle(wall_diagonal_2_r1, 0.0F, -0.3491F, 0.0F);
		wall_diagonal_2_r1.setTextureOffset(0, 160).addCuboid(-2.0F, -36.0F, 0.0F, 0.0F, 36.0F, 32.0F, 0.0F, true);

		wall_diagonal_1_r1 = new ModelPart(this);
		wall_diagonal_1_r1.setPivot(-20.0F, 0.0F, 16.0F);
		end.addChild(wall_diagonal_1_r1);
		setRotationAngle(wall_diagonal_1_r1, 0.0F, 0.3491F, 0.0F);
		wall_diagonal_1_r1.setTextureOffset(0, 160).addCuboid(2.0F, -36.0F, 0.0F, 0.0F, 36.0F, 32.0F, 0.0F, false);

		end_exterior = new ModelPart(this);
		end_exterior.setPivot(0.0F, 24.0F, 0.0F);
		end_exterior.setTextureOffset(36, 263).addCuboid(-20.0F, 0.0F, -16.0F, 1.0F, 7.0F, 32.0F, 0.0F, false);
		end_exterior.setTextureOffset(36, 263).addCuboid(19.0F, 0.0F, -16.0F, 1.0F, 7.0F, 32.0F, 0.0F, true);
		end_exterior.setTextureOffset(66, 158).addCuboid(-20.0F, -32.0F, -18.0F, 0.0F, 32.0F, 34.0F, 0.0F, false);
		end_exterior.setTextureOffset(66, 158).addCuboid(20.0F, -32.0F, -18.0F, 0.0F, 32.0F, 34.0F, 0.0F, true);
		end_exterior.setTextureOffset(266, 146).addCuboid(-10.0F, -9.0F, 46.0F, 20.0F, 9.0F, 0.0F, 0.0F, false);

		back_r2 = new ModelPart(this);
		back_r2.setPivot(0.0F, -9.0F, 46.0F);
		end_exterior.addChild(back_r2);
		setRotationAngle(back_r2, 0.0873F, 0.0F, 0.0F);
		back_r2.setTextureOffset(0, 334).addCuboid(-11.0F, -30.0F, -1.0F, 22.0F, 30.0F, 1.0F, 0.0F, false);

		wall_diagonal_2_r2 = new ModelPart(this);
		wall_diagonal_2_r2.setPivot(20.0F, 0.0F, 16.0F);
		end_exterior.addChild(wall_diagonal_2_r2);
		setRotationAngle(wall_diagonal_2_r2, 0.0F, -0.3491F, 0.0F);
		wall_diagonal_2_r2.setTextureOffset(136, 128).addCuboid(0.0F, -37.0F, 0.0F, 0.0F, 37.0F, 32.0F, 0.0F, true);

		wall_diagonal_1_r2 = new ModelPart(this);
		wall_diagonal_1_r2.setPivot(-20.0F, 0.0F, 16.0F);
		end_exterior.addChild(wall_diagonal_1_r2);
		setRotationAngle(wall_diagonal_1_r2, 0.0F, 0.3491F, 0.0F);
		wall_diagonal_1_r2.setTextureOffset(136, 128).addCuboid(0.0F, -37.0F, 0.0F, 0.0F, 37.0F, 32.0F, 0.0F, false);

		head = new ModelPart(this);
		head.setPivot(0.0F, 24.0F, 0.0F);
		head.setTextureOffset(141, 98).addCuboid(-20.0F, 0.0F, -16.0F, 40.0F, 1.0F, 32.0F, 0.0F, false);
		head.setTextureOffset(206, 131).addCuboid(-20.0F, -32.0F, -16.0F, 2.0F, 32.0F, 34.0F, 0.0F, false);
		head.setTextureOffset(96, 229).addCuboid(18.0F, -32.0F, -16.0F, 2.0F, 32.0F, 34.0F, 0.0F, true);
		head.setTextureOffset(274, 204).addCuboid(-20.0F, -36.0F, -16.0F, 40.0F, 36.0F, 0.0F, 0.0F, false);

		head_exterior = new ModelPart(this);
		head_exterior.setPivot(0.0F, 24.0F, 0.0F);
		head_exterior.setTextureOffset(98, 65).addCuboid(-20.0F, 0.0F, -46.0F, 40.0F, 1.0F, 30.0F, 0.0F, false);
		head_exterior.setTextureOffset(136, 300).addCuboid(-20.0F, 0.0F, -16.0F, 1.0F, 7.0F, 32.0F, 0.0F, false);
		head_exterior.setTextureOffset(136, 300).addCuboid(19.0F, 0.0F, -16.0F, 1.0F, 7.0F, 32.0F, 0.0F, true);
		head_exterior.setTextureOffset(0, 229).addCuboid(-20.0F, -32.0F, -16.0F, 0.0F, 32.0F, 34.0F, 0.0F, false);
		head_exterior.setTextureOffset(206, 200).addCuboid(20.0F, -32.0F, -16.0F, 0.0F, 32.0F, 34.0F, 0.0F, true);
		head_exterior.setTextureOffset(240, 275).addCuboid(-20.0F, -36.0F, -16.0F, 40.0F, 36.0F, 0.0F, 0.0F, false);
		head_exterior.setTextureOffset(22, 228).addCuboid(-10.0F, -14.0F, -46.0F, 20.0F, 14.0F, 1.0F, 0.0F, false);
		head_exterior.setTextureOffset(0, 302).addCuboid(-18.0F, -36.0F, -38.0F, 36.0F, 0.0F, 22.0F, 0.0F, false);

		wall_diagonal_2_r3 = new ModelPart(this);
		wall_diagonal_2_r3.setPivot(20.0F, 0.0F, -16.0F);
		head_exterior.addChild(wall_diagonal_2_r3);
		setRotationAngle(wall_diagonal_2_r3, 0.0F, 0.3491F, 0.0F);
		wall_diagonal_2_r3.setTextureOffset(288, 279).addCuboid(-2.0F, -39.0F, -32.0F, 2.0F, 39.0F, 32.0F, 0.0F, true);

		wall_diagonal_1_r3 = new ModelPart(this);
		wall_diagonal_1_r3.setPivot(-20.0F, 0.0F, -16.0F);
		head_exterior.addChild(wall_diagonal_1_r3);
		setRotationAngle(wall_diagonal_1_r3, 0.0F, -0.3491F, 0.0F);
		wall_diagonal_1_r3.setTextureOffset(288, 279).addCuboid(0.0F, -39.0F, -32.0F, 2.0F, 39.0F, 32.0F, 0.0F, false);

		head_exterior_1 = new ModelPart(this);
		head_exterior_1.setPivot(0.0F, 24.0F, 0.0F);
		head_exterior_1.setTextureOffset(0, 75).addCuboid(-10.0F, -39.0F, -46.0F, 20.0F, 7.0F, 9.0F, 0.0F, false);

		front_r1 = new ModelPart(this);
		front_r1.setPivot(0.0F, -14.0F, -46.0F);
		head_exterior_1.addChild(front_r1);
		setRotationAngle(front_r1, -0.4189F, 0.0F, 0.0F);
		front_r1.setTextureOffset(141, 131).addCuboid(-13.0F, -20.0F, 0.0F, 26.0F, 20.0F, 0.0F, 0.0F, false);

		head_exterior_3_5 = new ModelPart(this);
		head_exterior_3_5.setPivot(0.0F, 24.0F, 0.0F);
		head_exterior_3_5.setTextureOffset(0, 75).addCuboid(-10.0F, -39.0F, -44.0F, 20.0F, 7.0F, 9.0F, 0.0F, false);

		front_r2 = new ModelPart(this);
		front_r2.setPivot(0.0F, -14.0F, -46.0F);
		head_exterior_3_5.addChild(front_r2);
		setRotationAngle(front_r2, -0.1134F, 0.0F, 0.0F);
		front_r2.setTextureOffset(141, 131).addCuboid(-13.0F, -20.0F, 0.0F, 26.0F, 20.0F, 0.0F, 0.0F, false);

		head_exterior_4 = new ModelPart(this);
		head_exterior_4.setPivot(0.0F, 24.0F, 0.0F);
		head_exterior_4.setTextureOffset(0, 75).addCuboid(-10.0F, -38.8073F, -43.2695F, 20.0F, 7.0F, 9.0F, 0.0F, false);

		front_middle_r1 = new ModelPart(this);
		front_middle_r1.setPivot(0.0F, -14.0F, -46.0F);
		head_exterior_4.addChild(front_middle_r1);
		setRotationAngle(front_middle_r1, -0.0262F, 0.0F, 0.0F);
		front_middle_r1.setTextureOffset(141, 143).addCuboid(-13.0F, -8.0F, 0.0F, 26.0F, 8.0F, 0.0F, 0.0F, false);

		front_top_r1 = new ModelPart(this);
		front_top_r1.setPivot(0.0F, -13.9405F, -46.0895F);
		head_exterior_4.addChild(front_top_r1);
		setRotationAngle(front_top_r1, -0.1614F, 0.0F, 0.0F);
		front_top_r1.setTextureOffset(141, 133).addCuboid(-13.0F, -18.0F, -1.0F, 26.0F, 10.0F, 0.0F, 0.0F, false);

		destination_board_r1 = new ModelPart(this);
		destination_board_r1.setPivot(3.0F, 0.143F, -0.2353F);
		head_exterior_4.addChild(destination_board_r1);
		setRotationAngle(destination_board_r1, -0.1309F, 0.0F, 0.0F);
		destination_board_r1.setTextureOffset(28, 57).addCuboid(-13.0F, -33.0F, -47.75F, 20.0F, 7.0F, 0.0F, 0.0F, false);

		seat = new ModelPart(this);
		seat.setPivot(0.0F, 24.0F, 0.0F);
		seat.setTextureOffset(72, 27).addCuboid(-3.0F, -5.0F, -3.0F, 6.0F, 1.0F, 6.0F, 0.0F, false);
		seat.setTextureOffset(18, 21).addCuboid(-2.5F, -16.5F, 4.5F, 5.0F, 3.0F, 1.0F, 0.0F, false);

		back_right_r1 = new ModelPart(this);
		back_right_r1.setPivot(-3.0F, -5.0F, 2.0F);
		seat.addChild(back_right_r1);
		setRotationAngle(back_right_r1, -0.2618F, 0.0F, 0.0873F);
		back_right_r1.setTextureOffset(24, 0).addCuboid(0.0F, -10.0F, 0.0F, 3.0F, 10.0F, 1.0F, 0.0F, false);

		back_left_r1 = new ModelPart(this);
		back_left_r1.setPivot(3.0F, -5.0F, 2.0F);
		seat.addChild(back_left_r1);
		setRotationAngle(back_left_r1, -0.2618F, 0.0F, -0.0873F);
		back_left_r1.setTextureOffset(24, 0).addCuboid(-3.0F, -10.0F, 0.0F, 3.0F, 10.0F, 1.0F, 0.0F, true);

		seat_green = new ModelPart(this);
		seat_green.setPivot(0.0F, 24.0F, 0.0F);
		seat_green.setTextureOffset(36, 32).addCuboid(-3.0F, -5.0F, -2.75F, 6.0F, 1.0F, 6.0F, 0.0F, false);
		seat_green.setTextureOffset(15, 41).addCuboid(-3.0F, -16.4216F, 4.008F, 6.0F, 3.0F, 1.0F, 0.0F, false);
		seat_green.setTextureOffset(45, 45).addCuboid(-3.0F, -8.0F, 2.25F, 6.0F, 3.0F, 1.0F, 0.0F, false);

		back_right_r2 = new ModelPart(this);
		back_right_r2.setPivot(-3.0F, -5.1969F, 1.9953F);
		seat_green.addChild(back_right_r2);
		setRotationAngle(back_right_r2, -0.3054F, 0.0F, 0.0F);
		back_right_r2.setTextureOffset(34, 43).addCuboid(0.0F, -8.75F, -0.6F, 3.0F, 6.0F, 1.0F, 0.0F, false);
		back_right_r2.setTextureOffset(34, 43).addCuboid(3.0F, -8.75F, -0.6F, 3.0F, 6.0F, 1.0F, 0.0F, true);

		seat_purple = new ModelPart(this);
		seat_purple.setPivot(0.0F, 24.0F, 0.0F);
		seat_purple.setTextureOffset(72, 27).addCuboid(-3.0F, -5.0F, -2.75F, 6.0F, 1.0F, 6.0F, 0.0F, false);
		seat_purple.setTextureOffset(17, 32).addCuboid(-3.0F, -16.4216F, 4.008F, 6.0F, 3.0F, 1.0F, 0.0F, false);
		seat_purple.setTextureOffset(18, 21).addCuboid(-3.0F, -8.0F, 2.25F, 6.0F, 3.0F, 1.0F, 0.0F, false);

		back_right_r3 = new ModelPart(this);
		back_right_r3.setPivot(-3.0F, -5.1969F, 1.9953F);
		seat_purple.addChild(back_right_r3);
		setRotationAngle(back_right_r3, -0.3054F, 0.0F, 0.0F);
		back_right_r3.setTextureOffset(24, 0).addCuboid(0.0F, -8.75F, -0.6F, 3.0F, 6.0F, 1.0F, 0.0F, false);
		back_right_r3.setTextureOffset(24, 0).addCuboid(3.0F, -8.75F, -0.6F, 3.0F, 6.0F, 1.0F, 0.0F, true);

		vents_top = new ModelPart(this);
		vents_top.setPivot(0.0F, 24.0F, 0.0F);
		vents_top.setTextureOffset(274, 169).addCuboid(-15.0F, -46.0F, 20.0F, 15.0F, 7.0F, 28.0F, 0.0F, false);
		vents_top.setTextureOffset(257, 103).addCuboid(-15.0F, -46.0F, -14.0F, 15.0F, 7.0F, 28.0F, 0.0F, false);
		vents_top.setTextureOffset(274, 169).addCuboid(-15.0F, -46.0F, -48.0F, 15.0F, 7.0F, 28.0F, 0.0F, false);

		headlights = new ModelPart(this);
		headlights.setPivot(0.0F, 24.0F, 0.0F);
		headlights.setTextureOffset(12, 12).addCuboid(-7.0F, -6.0F, -46.1F, 4.0F, 3.0F, 0.0F, 0.0F, false);
		headlights.setTextureOffset(12, 12).addCuboid(3.0F, -6.0F, -46.1F, 4.0F, 3.0F, 0.0F, 0.0F, true);

		tail_lights = new ModelPart(this);
		tail_lights.setPivot(0.0F, 24.0F, 0.0F);
		tail_lights.setTextureOffset(20, 12).addCuboid(-8.0F, -7.0F, 46.1F, 5.0F, 4.0F, 0.0F, 0.0F, false);
		tail_lights.setTextureOffset(20, 12).addCuboid(3.0F, -7.0F, 46.1F, 5.0F, 4.0F, 0.0F, 0.0F, true);
	}

	private static final int DOOR_MAX = 14;
	private static final ModelDoorOverlay MODEL_DOOR_OVERLAY = new ModelDoorOverlay(DOOR_MAX, 0, 14, "door_overlay_light_rail_left.png", "door_overlay_light_rail_right.png", true, false);
	private static final ModelDoorOverlay MODEL_DOOR_OVERLAY_3 = new ModelDoorOverlay(DOOR_MAX, 0, 14, "door_overlay_light_rail_3_left.png", "door_overlay_light_rail_3_right.png", true, false);
	private static final ModelDoorOverlay MODEL_DOOR_OVERLAY_4 = new ModelDoorOverlay(DOOR_MAX, 0, 14, "door_overlay_light_rail_4_left.png", "door_overlay_light_rail_4_right.png", true, false);
	private static final ModelDoorOverlay MODEL_DOOR_OVERLAY_5 = new ModelDoorOverlay(DOOR_MAX, 0, 14, "door_overlay_light_rail_5_left.png", "door_overlay_light_rail_5_right.png", true, false);

	@Override
	protected void renderWindowPositions(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, boolean isEnd1Head, boolean isEnd2Head) {
		switch (renderStage) {
			case INTERIOR:
				renderMirror(window, matrices, vertices, light, position);
				if (renderDetails) {
					boolean flipSeat = false;
					for (int z = position - 24; z <= position + 24; z += 16) {
						renderOnce(phase >= 4 ? flipSeat ? seat_purple : seat_green : seat, matrices, vertices, light, 15, z);
						renderOnce(phase >= 4 ? flipSeat ? seat_green : seat_purple : seat, matrices, vertices, light, -8.5F, z);
						renderOnce(phase >= 4 ? flipSeat ? seat_purple : seat_green : seat, matrices, vertices, light, -15, z);
						flipSeat = !flipSeat;
					}
				}
				break;
			case EXTERIOR:
				renderMirror(window_exterior, matrices, vertices, light, position);
				break;
		}

		for (int i = 0; i < 2; i++) {
			final int roofPosition = position + i * 32 - 16;
			switch (renderStage) {
				case LIGHTS:
					renderMirror(phase == 5 ? roof_light_5 : roof_light, matrices, vertices, light, roofPosition);
					break;
				case INTERIOR:
					if (renderDetails) {
						renderMirror(roof, matrices, vertices, light, roofPosition);
					}
					break;
				case EXTERIOR:
					renderMirror(roof_exterior, matrices, vertices, light, roofPosition);
					break;
			}
		}
	}

	@Override
	protected void renderDoorPositions(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
		switch (renderStage) {
			case LIGHTS:
				renderMirror(phase == 5 ? roof_light_5 : roof_light, matrices, vertices, light, position);
				break;
			case INTERIOR:
				(phase == 5 ? door_left_5 : door_left).setPivot(0, 0, doorLeftZ);
				(phase == 5 ? door_right_5 : door_right).setPivot(0, 0, -doorLeftZ);
				renderOnceFlipped(phase == 5 ? door_5 : door, matrices, vertices, light, position);
				renderOnceFlipped(door_window, matrices, vertices, light, position);
				if (renderDetails) {
					renderOnceFlipped(phase >= 4 ? phase == 4 ? door_handrails_4 : door_handrails_5 : door_handrails, matrices, vertices, light, position);
					renderOnceFlipped(phase >= 4 ? phase == 4 ? door_window_handrails_4 : door_window_handrails_5 : door_window_handrails, matrices, vertices, light, position);
					renderMirror(roof, matrices, vertices, light, position);
				}
				break;
			case EXTERIOR:
				(phase == 5 ? door_left_exterior_5 : door_left_exterior).setPivot(0, 0, doorLeftZ);
				(phase == 5 ? door_right_exterior_5 : door_right_exterior).setPivot(0, 0, -doorLeftZ);
				renderOnceFlipped(phase == 5 ? door_exterior_5 : door_exterior, matrices, vertices, light, position);
				renderOnceFlipped(door_window_exterior, matrices, vertices, light, position);
				renderMirror(roof_exterior, matrices, vertices, light, position);
				if (position == 0) {
					renderMirror(vents_top, matrices, vertices, light, position);
				}
				break;
		}
	}

	@Override
	protected void renderHeadPosition1(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, boolean useHeadlights) {
		switch (renderStage) {
			case LIGHTS:
				renderMirror(phase == 5 ? roof_light_5 : roof_light, matrices, vertices, light, position);
				break;
			case ALWAYS_ON_LIGHTS:
				renderOnce(headlights, matrices, vertices, light, position);
				break;
			case INTERIOR:
				renderOnce(head, matrices, vertices, light, position);
				if (renderDetails) {
					boolean flipSeat = false;
					for (int z = position - 8; z <= position + 8; z += 16) {
						renderOnce(phase >= 4 ? flipSeat ? seat_purple : seat_green : seat, matrices, vertices, light, 15, z);
						renderOnce(phase >= 4 ? flipSeat ? seat_green : seat_purple : seat, matrices, vertices, light, -8.5F, z);
						renderOnce(phase >= 4 ? flipSeat ? seat_purple : seat_green : seat, matrices, vertices, light, -15, z);
						flipSeat = !flipSeat;
					}
					renderMirror(roof, matrices, vertices, light, position);
				}
				break;
			case EXTERIOR:
				switch (phase) {
					case 1:
					case 2:
						renderOnce(head_exterior_1, matrices, vertices, light, position);
						break;
					case 3:
					case 5:
						renderOnce(head_exterior_3_5, matrices, vertices, light, position);
						break;
					case 4:
						renderOnce(head_exterior_4, matrices, vertices, light, position);
						break;
				}
				renderOnce(head_exterior, matrices, vertices, light, position);
				renderMirror(roof_exterior, matrices, vertices, light, position);
				renderOnceFlipped(roof_end_exterior, matrices, vertices, light, position);
				break;
		}
	}

	@Override
	protected void renderHeadPosition2(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, boolean useHeadlights) {
		switch (renderStage) {
			case LIGHTS:
				renderMirror(phase == 5 ? roof_light_5 : roof_light, matrices, vertices, light, position);
				break;
			case ALWAYS_ON_LIGHTS:
				renderOnce(tail_lights, matrices, vertices, light, position);
				break;
			case INTERIOR:
				renderOnce(end, matrices, vertices, light, position);
				if (renderDetails) {
					boolean flipSeat = false;
					for (int z = position - 8; z <= position + 8; z += 16) {
						renderOnce(phase >= 4 ? flipSeat ? seat_purple : seat_green : seat, matrices, vertices, light, 15, z);
						renderOnce(phase >= 4 ? flipSeat ? seat_green : seat_purple : seat, matrices, vertices, light, -8.5F, z);
						renderOnce(phase >= 4 ? flipSeat ? seat_purple : seat_green : seat, matrices, vertices, light, -15, z);
						flipSeat = !flipSeat;
					}
					renderMirror(roof, matrices, vertices, light, position);
				}
				break;
			case EXTERIOR:
				renderOnce(end_exterior, matrices, vertices, light, position);
				renderMirror(roof_exterior, matrices, vertices, light, position);
				renderOnce(roof_end_exterior, matrices, vertices, light, position);
				break;
		}
	}

	@Override
	protected void renderEndPosition1(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails) {
		renderHeadPosition1(matrices, vertices, renderStage, light, position, renderDetails, true);
	}

	@Override
	protected void renderEndPosition2(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails) {
		renderHeadPosition2(matrices, vertices, renderStage, light, position, renderDetails, false);
	}

	@Override
	protected ModelDoorOverlay getModelDoorOverlay() {
		switch (phase) {
			case 1:
			case 2:
				return MODEL_DOOR_OVERLAY;
			case 3:
				return MODEL_DOOR_OVERLAY_3;
			case 4:
				return MODEL_DOOR_OVERLAY_4;
			case 5:
				return MODEL_DOOR_OVERLAY_5;
		}
		return null;
	}

	@Override
	protected ModelDoorOverlayTopBase getModelDoorOverlayTop() {
		return null;
	}

	@Override
	protected int[] getWindowPositions() {
		return new int[]{-48, 48};
	}

	@Override
	protected int[] getDoorPositions() {
		return new int[]{-96, 0, 96};
	}

	@Override
	protected int[] getEndPositions() {
		return new int[]{-128, 128};
	}

	@Override
	protected float getDoorAnimationX(float value, boolean opening) {
		return 0;
	}

	@Override
	protected float getDoorAnimationZ(float value, boolean opening) {
		switch (phase) {
			case 1:
				if (opening) {
					if (value > 0.4) {
						return smoothEnds(DOOR_MAX - 0.5F, DOOR_MAX, 0.4F, 0.5F, value);
					} else {
						return smoothEnds(-DOOR_MAX + 0.5F, DOOR_MAX - 0.5F, -0.4F, 0.4F, value);
					}
				} else {
					if (value > 0.3) {
						return smoothEnds(1, DOOR_MAX, 0.3F, 0.5F, value);
					} else if (value > 0.1) {
						return smoothEnds(3, 1, 0.1F, 0.3F, value);
					} else {
						return smoothEnds(-3, 3, -0.1F, 0.1F, value);
					}
				}
			case 2:
			case 3:
			case 5:
				return smoothEnds(0, DOOR_MAX, 0, 0.5F, value);
			case 4:
				if (opening) {
					return smoothEnds(0, DOOR_MAX, 0, 0.5F, value);
				} else {
					if (value > 0.2) {
						return smoothEnds(1, DOOR_MAX, 0.2F, 0.5F, value);
					} else if (value > 0.1) {
						return 1;
					} else {
						return smoothEnds(0, 1, 0, 0.1F, value);
					}
				}
			default:
				return 0;
		}
	}
}