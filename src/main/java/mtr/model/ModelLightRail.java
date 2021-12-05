package mtr.model;

import minecraftmappings.ModelDataWrapper;
import minecraftmappings.ModelMapper;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class ModelLightRail extends ModelTrainBase {

	private final ModelMapper window;
	private final ModelMapper window_exterior;
	private final ModelMapper door;
	private final ModelMapper door_left;
	private final ModelMapper door_right;
	private final ModelMapper door_5;
	private final ModelMapper door_left_5;
	private final ModelMapper door_right_5;
	private final ModelMapper door_handrails;
	private final ModelMapper handrail_6_r1;
	private final ModelMapper door_handrails_4;
	private final ModelMapper handrail_6_r2;
	private final ModelMapper handrail_5_r1;
	private final ModelMapper handrail_3_r1;
	private final ModelMapper handrail_2_r1;
	private final ModelMapper door_handrails_5;
	private final ModelMapper door_edge_8_r1;
	private final ModelMapper door_edge_7_r1;
	private final ModelMapper door_exterior;
	private final ModelMapper door_left_exterior;
	private final ModelMapper door_right_exterior;
	private final ModelMapper door_exterior_5;
	private final ModelMapper door_left_exterior_5;
	private final ModelMapper door_right_exterior_5;
	private final ModelMapper door_window;
	private final ModelMapper door_window_handrails;
	private final ModelMapper handrail_6_r3;
	private final ModelMapper door_window_handrails_4;
	private final ModelMapper handrail_12_r1;
	private final ModelMapper handrail_9_r1;
	private final ModelMapper handrail_7_r1;
	private final ModelMapper door_window_handrails_5;
	private final ModelMapper handrail_14_r1;
	private final ModelMapper handrail_8_r1;
	private final ModelMapper handrail_7_r2;
	private final ModelMapper handrail_6_r4;
	private final ModelMapper handrail_3_r2;
	private final ModelMapper handrail_2_r2;
	private final ModelMapper handrail_1_r1;
	private final ModelMapper door_window_exterior;
	private final ModelMapper roof;
	private final ModelMapper inner_roof_2_r1;
	private final ModelMapper roof_exterior;
	private final ModelMapper outer_roof_3_r1;
	private final ModelMapper outer_roof_2_r1;
	private final ModelMapper roof_end_exterior;
	private final ModelMapper bumper_2_r1;
	private final ModelMapper bumper_1_r1;
	private final ModelMapper outer_roof_6_r1;
	private final ModelMapper outer_roof_5_r1;
	private final ModelMapper outer_roof_3_r2;
	private final ModelMapper outer_roof_2_r2;
	private final ModelMapper roof_light;
	private final ModelMapper light_3_r1;
	private final ModelMapper light_1_r1;
	private final ModelMapper roof_light_5;
	private final ModelMapper end;
	private final ModelMapper inner_roof_4_r1;
	private final ModelMapper inner_roof_2_r2;
	private final ModelMapper back_r1;
	private final ModelMapper wall_diagonal_2_r1;
	private final ModelMapper wall_diagonal_1_r1;
	private final ModelMapper end_exterior;
	private final ModelMapper back_r2;
	private final ModelMapper wall_diagonal_2_r2;
	private final ModelMapper wall_diagonal_1_r2;
	private final ModelMapper head;
	private final ModelMapper head_exterior;
	private final ModelMapper wall_diagonal_2_r3;
	private final ModelMapper wall_diagonal_1_r3;
	private final ModelMapper head_exterior_1;
	private final ModelMapper front_r1;
	private final ModelMapper head_exterior_3_5;
	private final ModelMapper front_r2;
	private final ModelMapper head_exterior_4;
	private final ModelMapper front_middle_r1;
	private final ModelMapper front_top_r1;
	private final ModelMapper destination_board_r1;
	private final ModelMapper seat;
	private final ModelMapper back_right_r1;
	private final ModelMapper back_left_r1;
	private final ModelMapper seat_green;
	private final ModelMapper back_right_r2;
	private final ModelMapper seat_purple;
	private final ModelMapper back_right_r3;
	private final ModelMapper vents_top;
	private final ModelMapper headlights;
	private final ModelMapper tail_lights;

	private final int phase;

	public ModelLightRail(int phase) {
		this.phase = phase;

		final int textureWidth = 368;
		final int textureHeight = 368;

		final ModelDataWrapper modelDataWrapper = new ModelDataWrapper(this, textureWidth, textureHeight);

		window = new ModelMapper(modelDataWrapper);
		window.setPivot(0, 24, 0);
		window.setTextureOffset(98, 0).addCuboid(-20, 0, -32, 20, 1, 64, 0, false);
		window.setTextureOffset(134, 134).addCuboid(-20, -32, -34, 2, 32, 68, 0, false);

		window_exterior = new ModelMapper(modelDataWrapper);
		window_exterior.setPivot(0, 24, 0);
		window_exterior.setTextureOffset(0, 192).addCuboid(-20, 0, -32, 1, 7, 64, 0, false);
		window_exterior.setTextureOffset(0, 92).addCuboid(-20, -32, -34, 0, 32, 68, 0, false);

		door = new ModelMapper(modelDataWrapper);
		door.setPivot(0, 24, 0);
		door.setTextureOffset(206, 65).addCuboid(-20, 0, -16, 20, 1, 32, 0, false);

		door_left = new ModelMapper(modelDataWrapper);
		door_left.setPivot(0, 0, 0);
		door.addChild(door_left);
		door_left.setTextureOffset(168, 219).addCuboid(-20, -32, 0, 0, 32, 15, 0, false);

		door_right = new ModelMapper(modelDataWrapper);
		door_right.setPivot(0, 0, 0);
		door.addChild(door_right);

		door_right.setTextureOffset(206, 116).addCuboid(-20, -32, -15, 0, 32, 15, 0, false);

		door_5 = new ModelMapper(modelDataWrapper);
		door_5.setPivot(0, 24, 0);
		door_5.setTextureOffset(206, 65).addCuboid(-20, 0, -16, 20, 1, 32, 0, false);

		door_left_5 = new ModelMapper(modelDataWrapper);
		door_left_5.setPivot(0, 0, 0);
		door_5.addChild(door_left_5);
		door_left_5.setTextureOffset(167, 218).addCuboid(-20, -32, 0, 0, 32, 16, 0, false);

		door_right_5 = new ModelMapper(modelDataWrapper);
		door_right_5.setPivot(0, 0, 0);
		door_5.addChild(door_right_5);
		door_right_5.setTextureOffset(205, 115).addCuboid(-20, -32, -16, 0, 32, 16, 0, false);

		door_handrails = new ModelMapper(modelDataWrapper);
		door_handrails.setPivot(0, 24, 0);
		door_handrails.setTextureOffset(120, 0).addCuboid(-18, -15, 16, 5, 13, 0, 0, false);
		door_handrails.setTextureOffset(120, 0).addCuboid(-18, -15, -16, 5, 13, 0, 0, false);
		door_handrails.setTextureOffset(0, 0).addCuboid(-13, -36, 16, 0, 36, 0, 0.2F, false);
		door_handrails.setTextureOffset(0, 0).addCuboid(-13, -36, -16, 0, 36, 0, 0.2F, false);
		door_handrails.setTextureOffset(4, 0).addCuboid(-9, -36, 0, 0, 36, 0, 0.2F, false);
		door_handrails.setTextureOffset(0, 75).addCuboid(-15, -32, 20, 4, 5, 0, 0, false);
		door_handrails.setTextureOffset(0, 75).addCuboid(-15, -32, 28, 4, 5, 0, 0, false);
		door_handrails.setTextureOffset(0, 75).addCuboid(-15, -32, 36, 4, 5, 0, 0, false);
		door_handrails.setTextureOffset(0, 75).addCuboid(-15, -32, 44, 4, 5, 0, 0, false);
		door_handrails.setTextureOffset(0, 75).addCuboid(-15, -32, -44, 4, 5, 0, 0, false);
		door_handrails.setTextureOffset(0, 75).addCuboid(-15, -32, -36, 4, 5, 0, 0, false);
		door_handrails.setTextureOffset(0, 75).addCuboid(-15, -32, -28, 4, 5, 0, 0, false);
		door_handrails.setTextureOffset(0, 75).addCuboid(-15, -32, -20, 4, 5, 0, 0, false);

		handrail_6_r1 = new ModelMapper(modelDataWrapper);
		handrail_6_r1.setPivot(0, 0, 0);

		door_handrails.addChild(handrail_6_r1);
		setRotationAngle(handrail_6_r1, -1.5708F, 0, 0);
		handrail_6_r1.setTextureOffset(8, 0).addCuboid(-13, 16, -32, 0, 32, 0, 0.2F, false);
		handrail_6_r1.setTextureOffset(8, 0).addCuboid(-13, -48, -32, 0, 32, 0, 0.2F, false);

		door_handrails_4 = new ModelMapper(modelDataWrapper);
		door_handrails_4.setPivot(0, 24, 0);
		door_handrails_4.setTextureOffset(4, 6).addCuboid(-15.8F, -17, -15, 0, 4, 0, 0.2F, false);
		door_handrails_4.setTextureOffset(4, 6).addCuboid(-15.8F, -17, 15, 0, 4, 0, 0.2F, false);
		door_handrails_4.setTextureOffset(0, 75).addCuboid(-5, -32, -12, 4, 5, 0, 0, false);
		door_handrails_4.setTextureOffset(0, 75).addCuboid(-5, -32, -4, 4, 5, 0, 0, false);
		door_handrails_4.setTextureOffset(0, 75).addCuboid(-5, -32, 4, 4, 5, 0, 0, false);
		door_handrails_4.setTextureOffset(0, 75).addCuboid(-5, -32, 12, 4, 5, 0, 0, false);
		door_handrails_4.setTextureOffset(0, 75).addCuboid(-5, -32, 20, 4, 5, 0, 0, false);
		door_handrails_4.setTextureOffset(0, 75).addCuboid(-5, -32, 28, 4, 5, 0, 0, false);
		door_handrails_4.setTextureOffset(0, 75).addCuboid(-5, -32, 36, 4, 5, 0, 0, false);
		door_handrails_4.setTextureOffset(0, 75).addCuboid(-5, -32, 44, 4, 5, 0, 0, false);
		door_handrails_4.setTextureOffset(0, 75).addCuboid(-5, -32, -44, 4, 5, 0, 0, false);
		door_handrails_4.setTextureOffset(0, 75).addCuboid(-5, -32, -36, 4, 5, 0, 0, false);
		door_handrails_4.setTextureOffset(0, 75).addCuboid(-5, -32, -28, 4, 5, 0, 0, false);
		door_handrails_4.setTextureOffset(0, 75).addCuboid(-5, -32, -20, 4, 5, 0, 0, false);
		door_handrails_4.setTextureOffset(358, 0).addCuboid(-20, -32, 14, 3, 32, 2, 0, false);
		door_handrails_4.setTextureOffset(346, 0).addCuboid(-20, -32, -16, 3, 32, 2, 0, false);

		handrail_6_r2 = new ModelMapper(modelDataWrapper);
		handrail_6_r2.setPivot(-15.6F, -17.2F, 14.8F);
		door_handrails_4.addChild(handrail_6_r2);
		setRotationAngle(handrail_6_r2, 0, 0, -0.3491F);
		handrail_6_r2.setTextureOffset(4, 6).addCuboid(-0.2F, -5.2F, 0.2F, 0, 5, 0, 0.2F, false);

		handrail_5_r1 = new ModelMapper(modelDataWrapper);
		handrail_5_r1.setPivot(-15.6F, -12.8F, 14.8F);
		door_handrails_4.addChild(handrail_5_r1);
		setRotationAngle(handrail_5_r1, 0, 0, 0.3491F);
		handrail_5_r1.setTextureOffset(4, 6).addCuboid(-0.2F, 0.2F, 0.2F, 0, 5, 0, 0.2F, false);

		handrail_3_r1 = new ModelMapper(modelDataWrapper);
		handrail_3_r1.setPivot(-15.6F, -17.2F, -15.2F);
		door_handrails_4.addChild(handrail_3_r1);
		setRotationAngle(handrail_3_r1, 0, 0, -0.3491F);
		handrail_3_r1.setTextureOffset(4, 6).addCuboid(-0.2F, -5.2F, 0.2F, 0, 5, 0, 0.2F, false);

		handrail_2_r1 = new ModelMapper(modelDataWrapper);
		handrail_2_r1.setPivot(-15.6F, -12.8F, -15.2F);
		door_handrails_4.addChild(handrail_2_r1);
		setRotationAngle(handrail_2_r1, 0, 0, 0.3491F);
		handrail_2_r1.setTextureOffset(4, 6).addCuboid(-0.2F, 0.2F, 0.2F, 0, 5, 0, 0.2F, false);


		door_handrails_5 = new ModelMapper(modelDataWrapper);
		door_handrails_5.setPivot(0, 24, 0);
		door_handrails_5.setTextureOffset(0, 3).addCuboid(-17.25F, -26, 14.25F, 0, 19, 0, 0.2F, false);
		door_handrails_5.setTextureOffset(0, 3).addCuboid(-17.25F, -26, -14.25F, 0, 19, 0, 0.2F, false);
		door_handrails_5.setTextureOffset(0, 75).addCuboid(-5, -32.5F, -12, 4, 5, 0, 0, false);
		door_handrails_5.setTextureOffset(0, 75).addCuboid(-5, -32.5F, -4, 4, 5, 0, 0, false);
		door_handrails_5.setTextureOffset(0, 75).addCuboid(-5, -32.5F, 4, 4, 5, 0, 0, false);
		door_handrails_5.setTextureOffset(0, 75).addCuboid(-5, -32.5F, 12, 4, 5, 0, 0, false);
		door_handrails_5.setTextureOffset(0, 75).addCuboid(-5, -32.5F, 20, 4, 5, 0, 0, false);
		door_handrails_5.setTextureOffset(0, 75).addCuboid(-5, -32.5F, 28, 4, 5, 0, 0, false);
		door_handrails_5.setTextureOffset(0, 75).addCuboid(-5, -32.5F, 36, 4, 5, 0, 0, false);
		door_handrails_5.setTextureOffset(0, 75).addCuboid(-5, -32.5F, 44, 4, 5, 0, 0, false);
		door_handrails_5.setTextureOffset(0, 75).addCuboid(-5, -32.5F, -44, 4, 5, 0, 0, false);
		door_handrails_5.setTextureOffset(0, 75).addCuboid(-5, -32.5F, -36, 4, 5, 0, 0, false);
		door_handrails_5.setTextureOffset(0, 75).addCuboid(-5, -32.5F, -28, 4, 5, 0, 0, false);
		door_handrails_5.setTextureOffset(0, 75).addCuboid(-5, -32.5F, -20, 4, 5, 0, 0, false);
		door_handrails_5.setTextureOffset(358, 0).addCuboid(-20, -32, 14, 3, 32, 2, 0, false);
		door_handrails_5.setTextureOffset(346, 0).addCuboid(-20, -32, -16, 3, 32, 2, 0, false);
		door_handrails_5.setTextureOffset(361, 38).addCuboid(-19, -7, -16, 2, 0, 2, 0, false);
		door_handrails_5.setTextureOffset(363, 64).addCuboid(-18.25F, -26, -15.25F, 2, 19, 0, 0, false);
		door_handrails_5.setTextureOffset(361, 38).addCuboid(-19, -7, 13.25F, 2, 0, 2, 0, false);

		door_edge_8_r1 = new ModelMapper(modelDataWrapper);
		door_edge_8_r1.setPivot(0, 0, 0);
		door_handrails_5.addChild(door_edge_8_r1);
		setRotationAngle(door_edge_8_r1, 0, 3.1416F, 0);
		door_edge_8_r1.setTextureOffset(363, 64).addCuboid(16.75F, -26, -15.25F, 2, 19, 0, 0, false);

		door_edge_7_r1 = new ModelMapper(modelDataWrapper);
		door_edge_7_r1.setPivot(0, 0, 0);
		door_handrails_5.addChild(door_edge_7_r1);
		setRotationAngle(door_edge_7_r1, 0, 0, -3.1416F);
		door_edge_7_r1.setTextureOffset(361, 38).addCuboid(16, 26, 13.25F, 2, 0, 2, 0, false);
		door_edge_7_r1.setTextureOffset(361, 38).addCuboid(16, 26, -16, 2, 0, 2, 0, false);

		door_exterior = new ModelMapper(modelDataWrapper);
		door_exterior.setPivot(0, 24, 0);
		door_exterior.setTextureOffset(278, 29).addCuboid(-20, 0, -16, 1, 7, 32, 0, false);
		door_exterior.setTextureOffset(0, 0).addCuboid(-21, -34, -48, 1, 2, 96, 0, false);

		door_left_exterior = new ModelMapper(modelDataWrapper);
		door_left_exterior.setPivot(0, 0, 0);
		door_exterior.addChild(door_left_exterior);
		door_left_exterior.setTextureOffset(234, 311).addCuboid(-21, -32, 0, 1, 33, 15, 0, false);

		door_right_exterior = new ModelMapper(modelDataWrapper);
		door_right_exterior.setPivot(0, 0, 0);
		door_exterior.addChild(door_right_exterior);

		door_right_exterior.setTextureOffset(202, 300).addCuboid(-21, -32, -15, 1, 33, 15, 0, false);

		door_exterior_5 = new ModelMapper(modelDataWrapper);
		door_exterior_5.setPivot(0, 24, 0);
		door_exterior_5.setTextureOffset(278, 29).addCuboid(-20, 0, -16, 1, 7, 32, 0, false);
		door_exterior_5.setTextureOffset(0, 0).addCuboid(-21, -34, -48, 1, 2, 96, 0, false);

		door_left_exterior_5 = new ModelMapper(modelDataWrapper);
		door_left_exterior_5.setPivot(0, 0, 0);
		door_exterior_5.addChild(door_left_exterior_5);
		door_left_exterior_5.setTextureOffset(242, 311).addCuboid(-21, -32, 0, 1, 33, 16, 0, false);

		door_right_exterior_5 = new ModelMapper(modelDataWrapper);
		door_right_exterior_5.setPivot(0, 0, 0);
		door_exterior_5.addChild(door_right_exterior_5);
		door_right_exterior_5.setTextureOffset(202, 300).addCuboid(-21, -32, -16, 1, 33, 16, 0, false);

		door_window = new ModelMapper(modelDataWrapper);
		door_window.setPivot(0, 24, 0);
		door_window.setTextureOffset(202, 28).addCuboid(0, 0, -16, 20, 1, 32, 0, false);
		door_window.setTextureOffset(0, 98).addCuboid(18, -32, -14, 2, 32, 28, 0, true);

		door_window_handrails = new ModelMapper(modelDataWrapper);
		door_window_handrails.setPivot(0, 24, 0);
		door_window_handrails.setTextureOffset(32, 98).addCuboid(7, -15, 16, 11, 13, 0, 0, false);
		door_window_handrails.setTextureOffset(32, 98).addCuboid(7, -15, -16, 11, 13, 0, 0, false);
		door_window_handrails.setTextureOffset(0, 0).addCuboid(7, -36, 16, 0, 36, 0, 0.2F, false);
		door_window_handrails.setTextureOffset(0, 0).addCuboid(7, -36, -16, 0, 36, 0, 0.2F, false);
		door_window_handrails.setTextureOffset(4, 0).addCuboid(7, -36, 0, 0, 36, 0, 0.2F, false);
		door_window_handrails.setTextureOffset(0, 75).addCuboid(5, -32, 12, 4, 5, 0, 0, false);
		door_window_handrails.setTextureOffset(0, 75).addCuboid(5, -32, 4, 4, 5, 0, 0, false);
		door_window_handrails.setTextureOffset(0, 75).addCuboid(5, -32, -4, 4, 5, 0, 0, false);
		door_window_handrails.setTextureOffset(0, 75).addCuboid(5, -32, -12, 4, 5, 0, 0, false);
		door_window_handrails.setTextureOffset(0, 75).addCuboid(5, -32, 20, 4, 5, 0, 0, false);
		door_window_handrails.setTextureOffset(0, 75).addCuboid(5, -32, 28, 4, 5, 0, 0, false);
		door_window_handrails.setTextureOffset(0, 75).addCuboid(5, -32, 36, 4, 5, 0, 0, false);
		door_window_handrails.setTextureOffset(0, 75).addCuboid(5, -32, 44, 4, 5, 0, 0, false);
		door_window_handrails.setTextureOffset(0, 75).addCuboid(5, -32, -44, 4, 5, 0, 0, false);
		door_window_handrails.setTextureOffset(0, 75).addCuboid(5, -32, -36, 4, 5, 0, 0, false);
		door_window_handrails.setTextureOffset(0, 75).addCuboid(5, -32, -28, 4, 5, 0, 0, false);
		door_window_handrails.setTextureOffset(0, 75).addCuboid(5, -32, -20, 4, 5, 0, 0, false);

		handrail_6_r3 = new ModelMapper(modelDataWrapper);
		handrail_6_r3.setPivot(0, 0, 0);

		door_window_handrails.addChild(handrail_6_r3);
		setRotationAngle(handrail_6_r3, -1.5708F, 0, 0);
		handrail_6_r3.setTextureOffset(8, 0).addCuboid(7, 16, -32, 0, 32, 0, 0.2F, false);
		handrail_6_r3.setTextureOffset(8, 0).addCuboid(7, -48, -32, 0, 32, 0, 0.2F, false);
		handrail_6_r3.setTextureOffset(8, 0).addCuboid(7, -16, -32, 0, 32, 0, 0.2F, false);

		door_window_handrails_4 = new ModelMapper(modelDataWrapper);
		door_window_handrails_4.setPivot(0, 24, 0);
		door_window_handrails_4.setTextureOffset(0, 98).addCuboid(5, -13, 16, 13, 11, 0, 0, false);
		door_window_handrails_4.setTextureOffset(0, 98).addCuboid(5, -13, -16, 13, 11, 0, 0, false);
		door_window_handrails_4.setTextureOffset(0, 9).addCuboid(5.6F, -27.25F, -16, 0, 15, 0, 0.2F, false);
		door_window_handrails_4.setTextureOffset(0, 9).addCuboid(5.6F, -27.25F, 16, 0, 15, 0, 0.2F, false);
		door_window_handrails_4.setTextureOffset(0, 75).addCuboid(4.4F, -32, 8, 4, 5, 0, 0, false);
		door_window_handrails_4.setTextureOffset(0, 75).addCuboid(4.4F, -32, 0, 4, 5, 0, 0, false);
		door_window_handrails_4.setTextureOffset(0, 75).addCuboid(4.4F, -32, -8, 4, 5, 0, 0, false);

		handrail_12_r1 = new ModelMapper(modelDataWrapper);
		handrail_12_r1.setPivot(0, 0, 0);
		door_window_handrails_4.addChild(handrail_12_r1);
		setRotationAngle(handrail_12_r1, -1.5708F, 0, 0);
		handrail_12_r1.setTextureOffset(8, 0).addCuboid(-3, -48, -32, 0, 32, 0, 0.2F, false);
		handrail_12_r1.setTextureOffset(8, 0).addCuboid(-3, -16, -32, 0, 32, 0, 0.2F, false);
		handrail_12_r1.setTextureOffset(8, 0).addCuboid(-3, 16, -32, 0, 32, 0, 0.2F, false);
		handrail_12_r1.setTextureOffset(8, 0).addCuboid(6.4F, -16, -32, 0, 32, 0, 0.2F, false);

		handrail_9_r1 = new ModelMapper(modelDataWrapper);
		handrail_9_r1.setPivot(6, -28.25F, -16);
		door_window_handrails_4.addChild(handrail_9_r1);
		setRotationAngle(handrail_9_r1, 0, 0, 0.1745F);
		handrail_9_r1.setTextureOffset(0, 30).addCuboid(-0.25F, -8.25F, 32, 0, 9, 0, 0.2F, false);
		handrail_9_r1.setTextureOffset(0, 30).addCuboid(-0.25F, -8.25F, 0, 0, 9, 0, 0.2F, false);

		handrail_7_r1 = new ModelMapper(modelDataWrapper);
		handrail_7_r1.setPivot(6, -3.5F, -16);
		door_window_handrails_4.addChild(handrail_7_r1);

		setRotationAngle(handrail_7_r1, 0, 0, -0.2182F);
		handrail_7_r1.setTextureOffset(0, 30).addCuboid(1.5F, -8.25F, 32, 0, 12, 0, 0.2F, false);
		handrail_7_r1.setTextureOffset(0, 30).addCuboid(1.5F, -8.25F, 0, 0, 12, 0, 0.2F, false);

		door_window_handrails_5 = new ModelMapper(modelDataWrapper);
		door_window_handrails_5.setPivot(0, 24, 0);
		door_window_handrails_5.setTextureOffset(0, 98).addCuboid(5, -12.75F, 16, 13, 13, 0, 0, false);
		door_window_handrails_5.setTextureOffset(0, 98).addCuboid(5, -12.75F, -16, 13, 13, 0, 0, false);
		door_window_handrails_5.setTextureOffset(0, 30).addCuboid(5.6F, -13, -16, 0, 11, 0, 0.2F, false);
		door_window_handrails_5.setTextureOffset(0, 30).addCuboid(5.6F, -13, 16, 0, 11, 0, 0.2F, false);
		door_window_handrails_5.setTextureOffset(0, 30).addCuboid(9.2F, -36.2738F, -11.5F, 0, 5, 0, 0.2F, false);
		door_window_handrails_5.setTextureOffset(0, 30).addCuboid(9.2F, -36.2738F, 11.5F, 0, 5, 0, 0.2F, false);
		door_window_handrails_5.setTextureOffset(0, 75).addCuboid(6.9F, -31.75F, 10, 4, 5, 0, 0, false);
		door_window_handrails_5.setTextureOffset(0, 75).addCuboid(7.15F, -31.75F, 3.5F, 4, 5, 0, 0, false);
		door_window_handrails_5.setTextureOffset(0, 75).addCuboid(7.15F, -31.75F, -3.5F, 4, 5, 0, 0, false);
		door_window_handrails_5.setTextureOffset(0, 75).addCuboid(7.15F, -31.75F, -10, 4, 5, 0, 0, false);
		door_window_handrails_5.setTextureOffset(32, 94).addCuboid(17.99F, -31.5F, -18, 0, 18, 4, 0, false);
		door_window_handrails_5.setTextureOffset(32, 94).addCuboid(17.99F, -31.5F, 14, 0, 18, 4, 0, false);

		handrail_14_r1 = new ModelMapper(modelDataWrapper);
		handrail_14_r1.setPivot(0, 0, 0);
		door_window_handrails_5.addChild(handrail_14_r1);
		setRotationAngle(handrail_14_r1, -1.5708F, 0, 0);
		handrail_14_r1.setTextureOffset(8, 0).addCuboid(-3, -48, -32, 0, 32, 0, 0.2F, false);
		handrail_14_r1.setTextureOffset(8, 0).addCuboid(-3, -16, -32, 0, 32, 0, 0.2F, false);
		handrail_14_r1.setTextureOffset(8, 0).addCuboid(-3, 16, -32, 0, 32, 0, 0.2F, false);
		handrail_14_r1.setTextureOffset(8, 0).addCuboid(9.15F, -16, -31.25F, 0, 32, 0, 0.2F, false);

		handrail_8_r1 = new ModelMapper(modelDataWrapper);
		handrail_8_r1.setPivot(5.9238F, -16.714F, 16);
		door_window_handrails_5.addChild(handrail_8_r1);
		setRotationAngle(handrail_8_r1, 0, 0, 0.0873F);
		handrail_8_r1.setTextureOffset(0, 9).addCuboid(0, -3.5F, 0, 0, 7, 0, 0.2F, false);

		handrail_7_r2 = new ModelMapper(modelDataWrapper);
		handrail_7_r2.setPivot(8.5226F, -29.7676F, 16);
		door_window_handrails_5.addChild(handrail_7_r2);
		setRotationAngle(handrail_7_r2, 0, 0, 0.3491F);
		handrail_7_r2.setTextureOffset(0, 3).addCuboid(0, -1.5F, 0, 0, 3, 0, 0.2F, false);

		handrail_6_r4 = new ModelMapper(modelDataWrapper);
		handrail_6_r4.setPivot(7.0994F, -24.3136F, 16);
		door_window_handrails_5.addChild(handrail_6_r4);
		setRotationAngle(handrail_6_r4, 0, 0, 0.2182F);
		handrail_6_r4.setTextureOffset(0, 9).addCuboid(0, -4, 0, 0, 8, 0, 0.2F, false);

		handrail_3_r2 = new ModelMapper(modelDataWrapper);
		handrail_3_r2.setPivot(8.5935F, -30.2738F, -16);
		door_window_handrails_5.addChild(handrail_3_r2);
		setRotationAngle(handrail_3_r2, 0, 0, 0.3491F);
		handrail_3_r2.setTextureOffset(0, 3).addCuboid(0.1065F, -1, 0, 0, 3, 0, 0.2F, false);

		handrail_2_r2 = new ModelMapper(modelDataWrapper);
		handrail_2_r2.setPivot(5.6F, -18.75F, -16);
		door_window_handrails_5.addChild(handrail_2_r2);
		setRotationAngle(handrail_2_r2, 0, 0, 0.0873F);
		handrail_2_r2.setTextureOffset(0, 9).addCuboid(0.5F, -1.5F, 0, 0, 7, 0, 0.2F, false);

		handrail_1_r1 = new ModelMapper(modelDataWrapper);
		handrail_1_r1.setPivot(7.0761F, -24.3188F, -16);
		door_window_handrails_5.addChild(handrail_1_r1);
		setRotationAngle(handrail_1_r1, 0, 0, 0.2182F);
		handrail_1_r1.setTextureOffset(0, 9).addCuboid(0.0239F, -4, 0, 0, 8, 0, 0.2F, false);

		door_window_exterior = new ModelMapper(modelDataWrapper);
		door_window_exterior.setPivot(0, 24, 0);
		door_window_exterior.setTextureOffset(36, 263).addCuboid(19, 0, -16, 1, 7, 32, 0, true);
		door_window_exterior.setTextureOffset(98, 0).addCuboid(20, -32, -14, 0, 32, 28, 0, true);

		roof = new ModelMapper(modelDataWrapper);
		roof.setPivot(0, 24, 0);
		roof.setTextureOffset(122, 0).addCuboid(-20, -32, -16, 3, 0, 32, 0, false);
		roof.setTextureOffset(36, 36).addCuboid(-14, -36, -16, 14, 0, 32, 0, false);

		inner_roof_2_r1 = new ModelMapper(modelDataWrapper);
		inner_roof_2_r1.setPivot(-17, -32, 0);

		roof.addChild(inner_roof_2_r1);
		setRotationAngle(inner_roof_2_r1, 0, 0, -0.8727F);
		inner_roof_2_r1.setTextureOffset(109, 98).addCuboid(0, 0, -16, 6, 0, 32, 0, false);

		roof_exterior = new ModelMapper(modelDataWrapper);
		roof_exterior.setPivot(0, 24, 0);
		roof_exterior.setTextureOffset(0, 39).addCuboid(-20, -36, -16, 0, 4, 32, 0, false);
		roof_exterior.setTextureOffset(242, 242).addCuboid(-17, -39, -16, 17, 1, 32, 0, false);

		outer_roof_3_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_3_r1.setPivot(-19, -37.7329F, 0);
		roof_exterior.addChild(outer_roof_3_r1);
		setRotationAngle(outer_roof_3_r1, 0, 0, -0.5236F);
		outer_roof_3_r1.setTextureOffset(121, 98).addCuboid(0, 0.001F, -16, 3, 0, 32, 0, false);

		outer_roof_2_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_2_r1.setPivot(-20, -36, 0);
		roof_exterior.addChild(outer_roof_2_r1);
		setRotationAngle(outer_roof_2_r1, 0, 0, -1.0472F);
		outer_roof_2_r1.setTextureOffset(60, 0).addCuboid(0, 0, -16, 2, 0, 32, 0, false);

		roof_end_exterior = new ModelMapper(modelDataWrapper);
		roof_end_exterior.setPivot(0, 24, 0);
		roof_end_exterior.setTextureOffset(202, 0).addCuboid(-17, -39, 16, 34, 1, 27, 0, false);
		roof_end_exterior.setTextureOffset(266, 138).addCuboid(-10, -2, 43, 20, 3, 5, 0, false);

		bumper_2_r1 = new ModelMapper(modelDataWrapper);
		bumper_2_r1.setPivot(20, 0, 16);
		roof_end_exterior.addChild(bumper_2_r1);
		setRotationAngle(bumper_2_r1, 0, -0.3491F, 0);
		bumper_2_r1.setTextureOffset(202, 28).addCuboid(0, -2, 23, 1, 3, 10, 0, true);
		bumper_2_r1.setTextureOffset(0, 225).addCuboid(0, 0, 0, 0, 7, 23, 0, true);

		bumper_1_r1 = new ModelMapper(modelDataWrapper);
		bumper_1_r1.setPivot(-20, 0, 16);
		roof_end_exterior.addChild(bumper_1_r1);
		setRotationAngle(bumper_1_r1, 0, 0.3491F, 0);
		bumper_1_r1.setTextureOffset(202, 28).addCuboid(-1, -2, 23, 1, 3, 10, 0, false);
		bumper_1_r1.setTextureOffset(0, 225).addCuboid(0, 0, 0, 0, 7, 23, 0, false);

		outer_roof_6_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_6_r1.setPivot(20, -36, 16);
		roof_end_exterior.addChild(outer_roof_6_r1);
		setRotationAngle(outer_roof_6_r1, -0.2967F, 0, 1.0472F);
		outer_roof_6_r1.setTextureOffset(69, 65).addCuboid(-7, 0, 0, 7, 0, 29, 0, true);

		outer_roof_5_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_5_r1.setPivot(18.981F, -37.6982F, 16.0159F);
		roof_end_exterior.addChild(outer_roof_5_r1);
		setRotationAngle(outer_roof_5_r1, -0.1745F, 0, 0.5236F);
		outer_roof_5_r1.setTextureOffset(70, 0).addCuboid(-11, -0.0354F, -0.0226F, 11, 0, 28, 0, true);

		outer_roof_3_r2 = new ModelMapper(modelDataWrapper);
		outer_roof_3_r2.setPivot(-18.981F, -37.6982F, 16.0159F);
		roof_end_exterior.addChild(outer_roof_3_r2);
		setRotationAngle(outer_roof_3_r2, -0.1745F, 0, -0.5236F);
		outer_roof_3_r2.setTextureOffset(70, 0).addCuboid(0, -0.0354F, -0.0226F, 11, 0, 28, 0, false);

		outer_roof_2_r2 = new ModelMapper(modelDataWrapper);
		outer_roof_2_r2.setPivot(-20, -36, 16);
		roof_end_exterior.addChild(outer_roof_2_r2);
		setRotationAngle(outer_roof_2_r2, -0.2967F, 0, -1.0472F);
		outer_roof_2_r2.setTextureOffset(69, 65).addCuboid(0, 0, 0, 7, 0, 29, 0, false);

		roof_light = new ModelMapper(modelDataWrapper);
		roof_light.setPivot(0, 24, 0);
		roof_light.setTextureOffset(122, 32).addCuboid(-12, -35.5F, -16, 2, 0, 32, 0, false);

		light_3_r1 = new ModelMapper(modelDataWrapper);
		light_3_r1.setPivot(-10, -35.5F, 0);
		roof_light.addChild(light_3_r1);
		setRotationAngle(light_3_r1, 0, 0, -1.0472F);
		light_3_r1.setTextureOffset(126, 32).addCuboid(0, 0, -16, 1, 0, 32, 0, false);

		light_1_r1 = new ModelMapper(modelDataWrapper);
		light_1_r1.setPivot(-12, -35.5F, 0);
		roof_light.addChild(light_1_r1);

		setRotationAngle(light_1_r1, 0, 0, 1.0472F);
		light_1_r1.setTextureOffset(127, 98).addCuboid(-1, 0, -16, 1, 0, 32, 0, false);

		roof_light_5 = new ModelMapper(modelDataWrapper);
		roof_light_5.setPivot(0, 24, 0);
		roof_light_5.setTextureOffset(122, 32).addCuboid(-12, -36, -16, 2, 0, 32, 0, false);

		end = new ModelMapper(modelDataWrapper);
		end.setPivot(0, 24, 0);
		end.setTextureOffset(0, 98).addCuboid(-20, 0, -16, 40, 1, 61, 0, false);
		end.setTextureOffset(168, 234).addCuboid(-20, -32, -18, 2, 32, 34, 0, false);
		end.setTextureOffset(168, 234).addCuboid(18, -32, -18, 2, 32, 34, 0, true);
		end.setTextureOffset(278, 68).addCuboid(-8, -9, 44, 16, 9, 0, 0, false);
		end.setTextureOffset(21, 25).addCuboid(-20, -32, 16, 3, 0, 3, 0, false);
		end.setTextureOffset(9, 0).addCuboid(-14, -36, 16, 28, 0, 27, 0, false);
		end.setTextureOffset(21, 25).addCuboid(17, -32, 16, 3, 0, 3, 0, true);

		inner_roof_4_r1 = new ModelMapper(modelDataWrapper);
		inner_roof_4_r1.setPivot(17, -32, 0);

		end.addChild(inner_roof_4_r1);
		setRotationAngle(inner_roof_4_r1, 0, 0, 0.8727F);
		inner_roof_4_r1.setTextureOffset(0, 0).addCuboid(-6, 0, 16, 6, 0, 12, 0, true);

		inner_roof_2_r2 = new ModelMapper(modelDataWrapper);
		inner_roof_2_r2.setPivot(-17, -32, 0);
		end.addChild(inner_roof_2_r2);
		setRotationAngle(inner_roof_2_r2, 0, 0, -0.8727F);
		inner_roof_2_r2.setTextureOffset(0, 0).addCuboid(0, 0, 16, 6, 0, 12, 0, false);

		back_r1 = new ModelMapper(modelDataWrapper);
		back_r1.setPivot(0, -9, 46);
		end.addChild(back_r1);
		setRotationAngle(back_r1, 0.0873F, 0, 0);
		back_r1.setTextureOffset(274, 28).addCuboid(-9, -30, -2, 18, 30, 0, 0, false);

		wall_diagonal_2_r1 = new ModelMapper(modelDataWrapper);
		wall_diagonal_2_r1.setPivot(20, 0, 16);
		end.addChild(wall_diagonal_2_r1);
		setRotationAngle(wall_diagonal_2_r1, 0, -0.3491F, 0);
		wall_diagonal_2_r1.setTextureOffset(0, 160).addCuboid(-2, -36, 0, 0, 36, 32, 0, true);

		wall_diagonal_1_r1 = new ModelMapper(modelDataWrapper);
		wall_diagonal_1_r1.setPivot(-20, 0, 16);
		end.addChild(wall_diagonal_1_r1);
		setRotationAngle(wall_diagonal_1_r1, 0, 0.3491F, 0);
		wall_diagonal_1_r1.setTextureOffset(0, 160).addCuboid(2, -36, 0, 0, 36, 32, 0, false);

		end_exterior = new ModelMapper(modelDataWrapper);
		end_exterior.setPivot(0, 24, 0);
		end_exterior.setTextureOffset(36, 263).addCuboid(-20, 0, -16, 1, 7, 32, 0, false);
		end_exterior.setTextureOffset(36, 263).addCuboid(19, 0, -16, 1, 7, 32, 0, true);
		end_exterior.setTextureOffset(66, 158).addCuboid(-20, -32, -18, 0, 32, 34, 0, false);
		end_exterior.setTextureOffset(66, 158).addCuboid(20, -32, -18, 0, 32, 34, 0, true);
		end_exterior.setTextureOffset(266, 146).addCuboid(-10, -9, 46, 20, 9, 0, 0, false);

		back_r2 = new ModelMapper(modelDataWrapper);
		back_r2.setPivot(0, -9, 46);
		end_exterior.addChild(back_r2);
		setRotationAngle(back_r2, 0.0873F, 0, 0);
		back_r2.setTextureOffset(0, 334).addCuboid(-11, -30, -1, 22, 30, 1, 0, false);

		wall_diagonal_2_r2 = new ModelMapper(modelDataWrapper);
		wall_diagonal_2_r2.setPivot(20, 0, 16);
		end_exterior.addChild(wall_diagonal_2_r2);
		setRotationAngle(wall_diagonal_2_r2, 0, -0.3491F, 0);
		wall_diagonal_2_r2.setTextureOffset(136, 128).addCuboid(0, -37, 0, 0, 37, 32, 0, true);

		wall_diagonal_1_r2 = new ModelMapper(modelDataWrapper);
		wall_diagonal_1_r2.setPivot(-20, 0, 16);
		end_exterior.addChild(wall_diagonal_1_r2);
		setRotationAngle(wall_diagonal_1_r2, 0, 0.3491F, 0);
		wall_diagonal_1_r2.setTextureOffset(136, 128).addCuboid(0, -37, 0, 0, 37, 32, 0, false);

		head = new ModelMapper(modelDataWrapper);
		head.setPivot(0, 24, 0);
		head.setTextureOffset(141, 98).addCuboid(-20, 0, -16, 40, 1, 32, 0, false);
		head.setTextureOffset(206, 131).addCuboid(-20, -32, -16, 2, 32, 34, 0, false);
		head.setTextureOffset(96, 229).addCuboid(18, -32, -16, 2, 32, 34, 0, true);
		head.setTextureOffset(274, 204).addCuboid(-20, -36, -16, 40, 36, 0, 0, false);

		head_exterior = new ModelMapper(modelDataWrapper);
		head_exterior.setPivot(0, 24, 0);
		head_exterior.setTextureOffset(98, 65).addCuboid(-20, 0, -46, 40, 1, 30, 0, false);
		head_exterior.setTextureOffset(136, 300).addCuboid(-20, 0, -16, 1, 7, 32, 0, false);
		head_exterior.setTextureOffset(136, 300).addCuboid(19, 0, -16, 1, 7, 32, 0, true);
		head_exterior.setTextureOffset(0, 229).addCuboid(-20, -32, -16, 0, 32, 34, 0, false);
		head_exterior.setTextureOffset(206, 200).addCuboid(20, -32, -16, 0, 32, 34, 0, true);
		head_exterior.setTextureOffset(240, 275).addCuboid(-20, -36, -16, 40, 36, 0, 0, false);
		head_exterior.setTextureOffset(22, 228).addCuboid(-10, -14, -46, 20, 14, 1, 0, false);
		head_exterior.setTextureOffset(0, 302).addCuboid(-18, -36, -38, 36, 0, 22, 0, false);

		wall_diagonal_2_r3 = new ModelMapper(modelDataWrapper);
		wall_diagonal_2_r3.setPivot(20, 0, -16);
		head_exterior.addChild(wall_diagonal_2_r3);
		setRotationAngle(wall_diagonal_2_r3, 0, 0.3491F, 0);
		wall_diagonal_2_r3.setTextureOffset(288, 279).addCuboid(-2, -39, -32, 2, 39, 32, 0, true);

		wall_diagonal_1_r3 = new ModelMapper(modelDataWrapper);
		wall_diagonal_1_r3.setPivot(-20, 0, -16);
		head_exterior.addChild(wall_diagonal_1_r3);
		setRotationAngle(wall_diagonal_1_r3, 0, -0.3491F, 0);
		wall_diagonal_1_r3.setTextureOffset(288, 279).addCuboid(0, -39, -32, 2, 39, 32, 0, false);

		head_exterior_1 = new ModelMapper(modelDataWrapper);
		head_exterior_1.setPivot(0, 24, 0);
		head_exterior_1.setTextureOffset(0, 75).addCuboid(-10, -39, -46, 20, 7, 9, 0, false);

		front_r1 = new ModelMapper(modelDataWrapper);
		front_r1.setPivot(0, -14, -46);
		head_exterior_1.addChild(front_r1);
		setRotationAngle(front_r1, -0.4189F, 0, 0);
		front_r1.setTextureOffset(141, 131).addCuboid(-13, -20, 0, 26, 20, 0, 0, false);

		head_exterior_3_5 = new ModelMapper(modelDataWrapper);
		head_exterior_3_5.setPivot(0, 24, 0);
		head_exterior_3_5.setTextureOffset(0, 75).addCuboid(-10, -39, -44, 20, 7, 9, 0, false);

		front_r2 = new ModelMapper(modelDataWrapper);
		front_r2.setPivot(0, -14, -46);
		head_exterior_3_5.addChild(front_r2);
		setRotationAngle(front_r2, -0.1134F, 0, 0);
		front_r2.setTextureOffset(141, 131).addCuboid(-13, -20, 0, 26, 20, 0, 0, false);

		head_exterior_4 = new ModelMapper(modelDataWrapper);
		head_exterior_4.setPivot(0, 24, 0);
		head_exterior_4.setTextureOffset(0, 75).addCuboid(-10, -38.8073F, -43.2695F, 20, 7, 9, 0, false);

		front_middle_r1 = new ModelMapper(modelDataWrapper);
		front_middle_r1.setPivot(0, -14, -46);
		head_exterior_4.addChild(front_middle_r1);
		setRotationAngle(front_middle_r1, -0.0262F, 0, 0);
		front_middle_r1.setTextureOffset(141, 143).addCuboid(-13, -8, 0, 26, 8, 0, 0, false);

		front_top_r1 = new ModelMapper(modelDataWrapper);
		front_top_r1.setPivot(0, -13.9405F, -46.0895F);
		head_exterior_4.addChild(front_top_r1);
		setRotationAngle(front_top_r1, -0.1614F, 0, 0);
		front_top_r1.setTextureOffset(141, 133).addCuboid(-13, -18, -1, 26, 10, 0, 0, false);

		destination_board_r1 = new ModelMapper(modelDataWrapper);
		destination_board_r1.setPivot(3, 0.143F, -0.2353F);
		head_exterior_4.addChild(destination_board_r1);
		setRotationAngle(destination_board_r1, -0.1309F, 0, 0);
		destination_board_r1.setTextureOffset(28, 57).addCuboid(-13, -33, -47.75F, 20, 7, 0, 0, false);

		seat = new ModelMapper(modelDataWrapper);
		seat.setPivot(0, 24, 0);
		seat.setTextureOffset(72, 27).addCuboid(-3, -5, -3, 6, 1, 6, 0, false);
		seat.setTextureOffset(18, 21).addCuboid(-2.5F, -16.5F, 4.5F, 5, 3, 1, 0, false);

		back_right_r1 = new ModelMapper(modelDataWrapper);
		back_right_r1.setPivot(-3, -5, 2);
		seat.addChild(back_right_r1);
		setRotationAngle(back_right_r1, -0.2618F, 0, 0.0873F);
		back_right_r1.setTextureOffset(24, 0).addCuboid(0, -10, 0, 3, 10, 1, 0, false);

		back_left_r1 = new ModelMapper(modelDataWrapper);
		back_left_r1.setPivot(3, -5, 2);
		seat.addChild(back_left_r1);
		setRotationAngle(back_left_r1, -0.2618F, 0, -0.0873F);
		back_left_r1.setTextureOffset(24, 0).addCuboid(-3, -10, 0, 3, 10, 1, 0, true);

		seat_green = new ModelMapper(modelDataWrapper);
		seat_green.setPivot(0, 24, 0);
		seat_green.setTextureOffset(36, 32).addCuboid(-3, -5, -2.75F, 6, 1, 6, 0, false);
		seat_green.setTextureOffset(15, 41).addCuboid(-3, -16.4216F, 4.008F, 6, 3, 1, 0, false);
		seat_green.setTextureOffset(45, 45).addCuboid(-3, -8, 2.25F, 6, 3, 1, 0, false);

		back_right_r2 = new ModelMapper(modelDataWrapper);
		back_right_r2.setPivot(-3, -5.1969F, 1.9953F);
		seat_green.addChild(back_right_r2);
		setRotationAngle(back_right_r2, -0.3054F, 0, 0);
		back_right_r2.setTextureOffset(34, 43).addCuboid(0, -8.75F, -0.6F, 3, 6, 1, 0, false);
		back_right_r2.setTextureOffset(34, 43).addCuboid(3, -8.75F, -0.6F, 3, 6, 1, 0, true);

		seat_purple = new ModelMapper(modelDataWrapper);
		seat_purple.setPivot(0, 24, 0);
		seat_purple.setTextureOffset(72, 27).addCuboid(-3, -5, -2.75F, 6, 1, 6, 0, false);
		seat_purple.setTextureOffset(17, 32).addCuboid(-3, -16.4216F, 4.008F, 6, 3, 1, 0, false);
		seat_purple.setTextureOffset(18, 21).addCuboid(-3, -8, 2.25F, 6, 3, 1, 0, false);

		back_right_r3 = new ModelMapper(modelDataWrapper);
		back_right_r3.setPivot(-3, -5.1969F, 1.9953F);
		seat_purple.addChild(back_right_r3);
		setRotationAngle(back_right_r3, -0.3054F, 0, 0);
		back_right_r3.setTextureOffset(24, 0).addCuboid(0, -8.75F, -0.6F, 3, 6, 1, 0, false);
		back_right_r3.setTextureOffset(24, 0).addCuboid(3, -8.75F, -0.6F, 3, 6, 1, 0, true);

		vents_top = new ModelMapper(modelDataWrapper);
		vents_top.setPivot(0, 24, 0);
		vents_top.setTextureOffset(274, 169).addCuboid(-15, -46, 20, 15, 7, 28, 0, false);
		vents_top.setTextureOffset(257, 103).addCuboid(-15, -46, -14, 15, 7, 28, 0, false);
		vents_top.setTextureOffset(274, 169).addCuboid(-15, -46, -48, 15, 7, 28, 0, false);

		headlights = new ModelMapper(modelDataWrapper);
		headlights.setPivot(0, 24, 0);
		headlights.setTextureOffset(12, 12).addCuboid(-7, -6, -46.1F, 4, 3, 0, 0, false);
		headlights.setTextureOffset(12, 12).addCuboid(3, -6, -46.1F, 4, 3, 0, 0, true);

		tail_lights = new ModelMapper(modelDataWrapper);
		tail_lights.setPivot(0, 24, 0);
		tail_lights.setTextureOffset(20, 12).addCuboid(-8, -7, 46.1F, 5, 4, 0, 0, false);
		tail_lights.setTextureOffset(20, 12).addCuboid(3, -7, 46.1F, 5, 4, 0, 0, true);

		modelDataWrapper.setModelPart(textureWidth, textureHeight);
		window.setModelPart();
		window_exterior.setModelPart();
		door.setModelPart();
		door_left.setModelPart(door.name);
		door_right.setModelPart(door.name);
		door_5.setModelPart();
		door_left_5.setModelPart(door_5.name);
		door_right_5.setModelPart(door_5.name);
		door_handrails.setModelPart();
		door_handrails_4.setModelPart();
		door_handrails_5.setModelPart();
		door_exterior.setModelPart();
		door_left_exterior.setModelPart(door_exterior.name);
		door_right_exterior.setModelPart(door_exterior.name);
		door_exterior_5.setModelPart();
		door_left_exterior_5.setModelPart(door_exterior_5.name);
		door_right_exterior_5.setModelPart(door_exterior_5.name);
		door_window.setModelPart();
		door_window_handrails.setModelPart();
		door_window_handrails_4.setModelPart();
		door_window_handrails_5.setModelPart();
		door_window_exterior.setModelPart();
		roof.setModelPart();
		roof_exterior.setModelPart();
		roof_end_exterior.setModelPart();
		roof_light.setModelPart();
		roof_light_5.setModelPart();
		end.setModelPart();
		end_exterior.setModelPart();
		head.setModelPart();
		head_exterior.setModelPart();
		head_exterior_1.setModelPart();
		head_exterior_3_5.setModelPart();
		head_exterior_4.setModelPart();
		seat.setModelPart();
		seat_green.setModelPart();
		seat_purple.setModelPart();
		vents_top.setModelPart();
		headlights.setModelPart();
		tail_lights.setModelPart();
	}

	private static final int DOOR_MAX = 14;
	private static final ModelDoorOverlay MODEL_DOOR_OVERLAY = new ModelDoorOverlay(DOOR_MAX, 0, 14, "door_overlay_light_rail_left.png", "door_overlay_light_rail_right.png", true, false);
	private static final ModelDoorOverlay MODEL_DOOR_OVERLAY_3 = new ModelDoorOverlay(DOOR_MAX, 0, 14, "door_overlay_light_rail_3_left.png", "door_overlay_light_rail_3_right.png", true, false);
	private static final ModelDoorOverlay MODEL_DOOR_OVERLAY_4 = new ModelDoorOverlay(DOOR_MAX, 0, 14, "door_overlay_light_rail_4_left.png", "door_overlay_light_rail_4_right.png", true, false);
	private static final ModelDoorOverlay MODEL_DOOR_OVERLAY_5 = new ModelDoorOverlay(DOOR_MAX, 0, 14, "door_overlay_light_rail_5_left.png", "door_overlay_light_rail_5_right.png", true, false);

	@Override
	protected void renderWindowPositions(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
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
				(phase == 5 ? door_left_5 : door_left).setOffset(0, 0, doorLeftZ);
				(phase == 5 ? door_right_5 : door_right).setOffset(0, 0, -doorLeftZ);
				renderOnceFlipped(phase == 5 ? door_5 : door, matrices, vertices, light, position);
				renderOnceFlipped(door_window, matrices, vertices, light, position);
				if (renderDetails) {
					renderOnceFlipped(phase >= 4 ? phase == 4 ? door_handrails_4 : door_handrails_5 : door_handrails, matrices, vertices, light, position);
					renderOnceFlipped(phase >= 4 ? phase == 4 ? door_window_handrails_4 : door_window_handrails_5 : door_window_handrails, matrices, vertices, light, position);
					renderMirror(roof, matrices, vertices, light, position);
				}
				break;
			case EXTERIOR:
				(phase == 5 ? door_left_exterior_5 : door_left_exterior).setOffset(0, 0, doorLeftZ);
				(phase == 5 ? door_right_exterior_5 : door_right_exterior).setOffset(0, 0, -doorLeftZ);
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
	protected void renderHeadPosition1(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean useHeadlights) {
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
	protected void renderHeadPosition2(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean useHeadlights) {
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
	protected void renderEndPosition1(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
		renderHeadPosition1(matrices, vertices, renderStage, light, position, renderDetails, doorLeftX, doorRightX, doorLeftZ, doorRightZ, true);
	}

	@Override
	protected void renderEndPosition2(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
		renderHeadPosition2(matrices, vertices, renderStage, light, position, renderDetails, doorLeftX, doorRightX, doorLeftZ, doorRightZ, false);
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