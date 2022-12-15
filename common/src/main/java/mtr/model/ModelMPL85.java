package mtr.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mtr.client.DoorAnimationType;
import mtr.mappings.ModelDataWrapper;
import mtr.mappings.ModelMapper;

public class ModelMPL85 extends ModelSimpleTrainBase<ModelMPL85> {

	private final ModelMapper window;
	private final ModelMapper roof_4_r1;
	private final ModelMapper roof_3_r1;
	private final ModelMapper roof_2_r1;
	private final ModelMapper wall_top_r1;
	private final ModelMapper wall_bottom_r1;
	private final ModelMapper window_exterior;
	private final ModelMapper roof_3_r2;
	private final ModelMapper roof_2_r2;
	private final ModelMapper roof_1_r1;
	private final ModelMapper wall_top_r2;
	private final ModelMapper wall_bottom_r2;
	private final ModelMapper door;
	private final ModelMapper roof_4_r2;
	private final ModelMapper roof_3_r3;
	private final ModelMapper roof_2_r3;
	private final ModelMapper wall_top_2_r1;
	private final ModelMapper wall_bottom_2_r1;
	private final ModelMapper door_left;
	private final ModelMapper door_top_r1;
	private final ModelMapper door_bottom_r1;
	private final ModelMapper door_right;
	private final ModelMapper door_top_r2;
	private final ModelMapper door_bottom_r2;
	private final ModelMapper door_exterior;
	private final ModelMapper wall_top_3_r1;
	private final ModelMapper wall_bottom_3_r1;
	private final ModelMapper door_left_exterior;
	private final ModelMapper door_top_r3;
	private final ModelMapper door_bottom_r3;
	private final ModelMapper door_right_exterior;
	private final ModelMapper door_top_r4;
	private final ModelMapper door_bottom_r4;
	private final ModelMapper roof_door_exterior_1;
	private final ModelMapper roof_3_r4;
	private final ModelMapper roof_2_r4;
	private final ModelMapper roof_1_r2;
	private final ModelMapper roof_door_exterior_2;
	private final ModelMapper roof_4_r3;
	private final ModelMapper roof_3_r5;
	private final ModelMapper roof_2_r5;
	private final ModelMapper roof_door_exterior_3;
	private final ModelMapper roof_5_r1;
	private final ModelMapper roof_4_r4;
	private final ModelMapper roof_3_r6;
	private final ModelMapper roof_window_exterior;
	private final ModelMapper roof_6_r1;
	private final ModelMapper roof_5_r2;
	private final ModelMapper roof_4_r5;
	private final ModelMapper seat_1;
	private final ModelMapper handrail_3_r1;
	private final ModelMapper handrail_2_r1;
	private final ModelMapper seat_2;
	private final ModelMapper handrail_3_r2;
	private final ModelMapper handrail_2_r2;
	private final ModelMapper seat_3;
	private final ModelMapper back_upper_diagonal_2_r1;
	private final ModelMapper back_upper_diagonal_1_r1;
	private final ModelMapper seat_diagonal_2_r1;
	private final ModelMapper seat_diagonal_1_r1;
	private final ModelMapper light_window;
	private final ModelMapper light_r1;
	private final ModelMapper light_door;
	private final ModelMapper light_r2;
	private final ModelMapper middle_handrail;
	private final ModelMapper top_handrail_5_r1;
	private final ModelMapper top_handrail_4_r1;
	private final ModelMapper top_handrail_3_r1;
	private final ModelMapper top_handrail_2_r1;
	private final ModelMapper handrail_16_r1;
	private final ModelMapper handrail_15_r1;
	private final ModelMapper handrail_14_r1;
	private final ModelMapper handrail_13_r1;
	private final ModelMapper handrail_12_r1;
	private final ModelMapper handrail_11_r1;
	private final ModelMapper handrail_10_r1;
	private final ModelMapper handrail_9_r1;
	private final ModelMapper handrail_7_r1;
	private final ModelMapper handrail_6_r1;
	private final ModelMapper handrail_5_r1;
	private final ModelMapper handrail_4_r1;
	private final ModelMapper handrail_3_r3;
	private final ModelMapper handrail_2_r3;
	private final ModelMapper head;
	private final ModelMapper side_1;
	private final ModelMapper bar_2_r1;
	private final ModelMapper front_11_r1;
	private final ModelMapper front_10_r1;
	private final ModelMapper front_9_r1;
	private final ModelMapper front_8_r1;
	private final ModelMapper front_4_r1;
	private final ModelMapper front_3_r1;
	private final ModelMapper front_2_r1;
	private final ModelMapper roof_8_r1;
	private final ModelMapper roof_7_r1;
	private final ModelMapper roof_4_r6;
	private final ModelMapper roof_3_r7;
	private final ModelMapper roof_2_r6;
	private final ModelMapper wall_top_r3;
	private final ModelMapper wall_bottom_r3;
	private final ModelMapper side_2;
	private final ModelMapper bar_3_r1;
	private final ModelMapper front_12_r1;
	private final ModelMapper front_11_r2;
	private final ModelMapper front_10_r2;
	private final ModelMapper front_9_r2;
	private final ModelMapper front_5_r1;
	private final ModelMapper front_4_r2;
	private final ModelMapper front_3_r2;
	private final ModelMapper roof_9_r1;
	private final ModelMapper roof_8_r2;
	private final ModelMapper roof_5_r3;
	private final ModelMapper roof_4_r7;
	private final ModelMapper roof_3_r8;
	private final ModelMapper wall_top_r4;
	private final ModelMapper wall_bottom_r4;
	private final ModelMapper head_exterior;
	private final ModelMapper side_1_exterior;
	private final ModelMapper roof_8_r3;
	private final ModelMapper roof_7_r2;
	private final ModelMapper roof_6_r2;
	private final ModelMapper roof_5_r4;
	private final ModelMapper roof_3_r9;
	private final ModelMapper roof_2_r7;
	private final ModelMapper roof_1_r3;
	private final ModelMapper front_14_r1;
	private final ModelMapper front_13_r1;
	private final ModelMapper front_12_r2;
	private final ModelMapper front_11_r3;
	private final ModelMapper front_10_r3;
	private final ModelMapper front_9_r3;
	private final ModelMapper front_8_r2;
	private final ModelMapper front_7_r1;
	private final ModelMapper front_6_r1;
	private final ModelMapper front_5_r2;
	private final ModelMapper front_4_r3;
	private final ModelMapper front_3_r3;
	private final ModelMapper wall_top_r5;
	private final ModelMapper wall_bottom_r5;
	private final ModelMapper side_2_exterior;
	private final ModelMapper roof_9_r2;
	private final ModelMapper roof_8_r4;
	private final ModelMapper roof_7_r3;
	private final ModelMapper roof_6_r3;
	private final ModelMapper roof_4_r8;
	private final ModelMapper roof_3_r10;
	private final ModelMapper roof_2_r8;
	private final ModelMapper front_15_r1;
	private final ModelMapper front_14_r2;
	private final ModelMapper front_13_r2;
	private final ModelMapper front_12_r3;
	private final ModelMapper front_11_r4;
	private final ModelMapper front_10_r4;
	private final ModelMapper front_9_r4;
	private final ModelMapper front_8_r3;
	private final ModelMapper front_7_r2;
	private final ModelMapper front_6_r2;
	private final ModelMapper front_5_r3;
	private final ModelMapper front_4_r4;
	private final ModelMapper wall_top_r6;
	private final ModelMapper wall_bottom_r6;
	private final ModelMapper door_light_on;
	private final ModelMapper light_r3;
	private final ModelMapper door_light_off;
	private final ModelMapper light_r4;
	private final ModelMapper headlights;
	private final ModelMapper headlight_4_r1;
	private final ModelMapper headlight_3_r1;
	private final ModelMapper headlight_2_r1;
	private final ModelMapper headlight_1_r1;
	private final ModelMapper tail_lights;
	private final ModelMapper headlight_4_r2;
	private final ModelMapper headlight_3_r2;
	private final ModelMapper headlight_2_r2;
	private final ModelMapper headlight_1_r2;

	public ModelMPL85() {
		this(DoorAnimationType.PLUG_FAST, true);
	}

	protected ModelMPL85(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		super(doorAnimationType, renderDoorOverlay);
		final int textureWidth = 256;
		final int textureHeight = 256;

		final ModelDataWrapper modelDataWrapper = new ModelDataWrapper(this, textureWidth, textureHeight);

		window = new ModelMapper(modelDataWrapper);
		window.setPos(0, 24, 0);
		window.texOffs(32, 94).addBox(0, 0, -14, 19, 2, 28, 0, false);
		window.texOffs(130, 132).addBox(20, -10, -14, 1, 5, 28, 0, false);
		window.texOffs(4, 42).addBox(16.734F, -33.9011F, -14, 2, 0, 28, 0, false);
		window.texOffs(126, 0).addBox(0, -37.3304F, -14, 7, 0, 28, 0, false);

		roof_4_r1 = new ModelMapper(modelDataWrapper);
		roof_4_r1.setPos(10.0171F, -36.9817F, 0);
		window.addChild(roof_4_r1);
		setRotationAngle(roof_4_r1, 0, 0, 0.0873F);
		roof_4_r1.texOffs(0, 126).addBox(-4, 0, -14, 8, 0, 28, 0, false);

		roof_3_r1 = new ModelMapper(modelDataWrapper);
		roof_3_r1.setPos(14.5019F, -35.7671F, 0);
		window.addChild(roof_3_r1);
		setRotationAngle(roof_3_r1, 0, 0, 1.0472F);
		roof_3_r1.texOffs(8, 0).addBox(-1, 0, -14, 2, 0, 28, 0, false);

		roof_2_r1 = new ModelMapper(modelDataWrapper);
		roof_2_r1.setPos(15.8679F, -34.4011F, 0);
		window.addChild(roof_2_r1);
		setRotationAngle(roof_2_r1, 0, 0, 0.5236F);
		roof_2_r1.texOffs(0, 42).addBox(-1, 0, -14, 2, 0, 28, 0, false);

		wall_top_r1 = new ModelMapper(modelDataWrapper);
		wall_top_r1.setPos(21, -10, 0);
		window.addChild(wall_top_r1);
		setRotationAngle(wall_top_r1, 0, 0, -0.0873F);
		wall_top_r1.texOffs(124, 12).addBox(-1, -27, -14, 1, 27, 28, 0, false);

		wall_bottom_r1 = new ModelMapper(modelDataWrapper);
		wall_bottom_r1.setPos(21, -5, 0);
		window.addChild(wall_bottom_r1);
		setRotationAngle(wall_bottom_r1, 0, 0, 0.1745F);
		wall_bottom_r1.texOffs(44, 126).addBox(-1, 0, -14, 1, 7, 28, 0, false);

		window_exterior = new ModelMapper(modelDataWrapper);
		window_exterior.setPos(0, 24, 0);
		window_exterior.texOffs(0, 133).addBox(21, -10, -14, 0, 5, 28, 0, false);
		window_exterior.texOffs(30, 126).addBox(-0.0293F, -41.2781F, -14, 7, 0, 28, 0, false);

		roof_3_r2 = new ModelMapper(modelDataWrapper);
		roof_3_r2.setPos(11.9516F, -40.8423F, 0);
		window_exterior.addChild(roof_3_r2);
		setRotationAngle(roof_3_r2, 0, 0, 0.0873F);
		roof_3_r2.texOffs(108, 125).addBox(-5, 0, -14, 10, 0, 28, 0, false);

		roof_2_r2 = new ModelMapper(modelDataWrapper);
		roof_2_r2.setPos(17.3157F, -40.0862F, 0);
		window_exterior.addChild(roof_2_r2);
		setRotationAngle(roof_2_r2, 0, 0, -0.8727F);
		roof_2_r2.texOffs(74, 124).addBox(0, -0.5F, -14, 0, 1, 28, 0, false);

		roof_1_r1 = new ModelMapper(modelDataWrapper);
		roof_1_r1.setPos(18.2163F, -37.8329F, 0);
		window_exterior.addChild(roof_1_r1);
		setRotationAngle(roof_1_r1, 0, 0, -0.2618F);
		roof_1_r1.texOffs(56, 137).addBox(0, -2, -14, 0, 4, 28, 0, false);

		wall_top_r2 = new ModelMapper(modelDataWrapper);
		wall_top_r2.setPos(21, -10, 0);
		window_exterior.addChild(wall_top_r2);
		setRotationAngle(wall_top_r2, 0, 0, -0.0873F);
		wall_top_r2.texOffs(80, 97).addBox(0, -26, -14, 0, 26, 28, 0, false);

		wall_bottom_r2 = new ModelMapper(modelDataWrapper);
		wall_bottom_r2.setPos(21, -5, 0);
		window_exterior.addChild(wall_bottom_r2);
		setRotationAngle(wall_bottom_r2, 0, 0, 0.1745F);
		wall_bottom_r2.texOffs(102, 125).addBox(0, 0, -14, 0, 7, 28, 0, false);

		door = new ModelMapper(modelDataWrapper);
		door.setPos(0, 24, 0);
		door.texOffs(0, 0).addBox(0, 0, -20, 19, 2, 40, 0, false);
		door.texOffs(162, 165).addBox(20, -10, -20, 1, 5, 4, 0, false);
		door.texOffs(162, 93).addBox(20, -10, 16, 1, 5, 4, 0, false);
		door.texOffs(104, 203).addBox(17, -36, -20, 3, 36, 4, 0, false);
		door.texOffs(0, 192).addBox(17, -36, 16, 3, 36, 4, 0, false);
		door.texOffs(6, 209).addBox(16.734F, -35.9011F, -20, 2, 2, 40, 0, false);
		door.texOffs(56, 42).addBox(0, -37.3304F, -20, 7, 0, 40, 0, false);

		roof_4_r2 = new ModelMapper(modelDataWrapper);
		roof_4_r2.setPos(10.0171F, -36.9817F, 0);
		door.addChild(roof_4_r2);
		setRotationAngle(roof_4_r2, 0, 0, 0.0873F);
		roof_4_r2.texOffs(40, 42).addBox(-4, 0, -20, 8, 0, 40, 0, false);

		roof_3_r3 = new ModelMapper(modelDataWrapper);
		roof_3_r3.setPos(14.5019F, -35.7671F, 0);
		door.addChild(roof_3_r3);
		setRotationAngle(roof_3_r3, 0, 0, 1.0472F);
		roof_3_r3.texOffs(8, 82).addBox(-1, 0, -20, 2, 0, 40, 0, false);

		roof_2_r3 = new ModelMapper(modelDataWrapper);
		roof_2_r3.setPos(15.8679F, -34.4011F, 0);
		door.addChild(roof_2_r3);
		setRotationAngle(roof_2_r3, 0, 0, 0.5236F);
		roof_2_r3.texOffs(12, 82).addBox(-1, 0, -20, 2, 0, 40, 0, false);

		wall_top_2_r1 = new ModelMapper(modelDataWrapper);
		wall_top_2_r1.setPos(21, -10, 0);
		door.addChild(wall_top_2_r1);
		setRotationAngle(wall_top_2_r1, 0, 0, -0.0873F);
		wall_top_2_r1.texOffs(14, 204).addBox(-1, -26, 16, 1, 26, 4, 0, false);
		wall_top_2_r1.texOffs(24, 204).addBox(-1, -26, -20, 1, 26, 4, 0, false);

		wall_bottom_2_r1 = new ModelMapper(modelDataWrapper);
		wall_bottom_2_r1.setPos(21, -5, 0);
		door.addChild(wall_bottom_2_r1);
		setRotationAngle(wall_bottom_2_r1, 0, 0, 0.1745F);
		wall_bottom_2_r1.texOffs(114, 160).addBox(-1, 0, 16, 1, 7, 4, 0, false);
		wall_bottom_2_r1.texOffs(160, 139).addBox(-1, 0, -20, 1, 7, 4, 0, false);

		door_left = new ModelMapper(modelDataWrapper);
		door_left.setPos(0, 0, 0);
		door.addChild(door_left);
		door_left.texOffs(188, 52).addBox(20, -10, -16, 1, 5, 16, 0, false);

		door_top_r1 = new ModelMapper(modelDataWrapper);
		door_top_r1.setPos(21, -10, 0);
		door_left.addChild(door_top_r1);
		setRotationAngle(door_top_r1, 0, 0, -0.0873F);
		door_top_r1.texOffs(96, 160).addBox(-1, -26, -16, 1, 26, 16, 0, false);

		door_bottom_r1 = new ModelMapper(modelDataWrapper);
		door_bottom_r1.setPos(21, -5, 0);
		door_left.addChild(door_bottom_r1);
		setRotationAngle(door_bottom_r1, 0, 0, 0.1745F);
		door_bottom_r1.texOffs(182, 29).addBox(-1, 0, -16, 1, 7, 16, 0, false);

		door_right = new ModelMapper(modelDataWrapper);
		door_right.setPos(0, 0, 0);
		door.addChild(door_right);
		door_right.texOffs(36, 188).addBox(20, -10, 0, 1, 5, 16, 0, false);

		door_top_r2 = new ModelMapper(modelDataWrapper);
		door_top_r2.setPos(21, -10, 0);
		door_right.addChild(door_top_r2);
		setRotationAngle(door_top_r2, 0, 0, -0.0873F);
		door_top_r2.texOffs(0, 73).addBox(-1, -26, 0, 1, 26, 16, 0, false);

		door_bottom_r2 = new ModelMapper(modelDataWrapper);
		door_bottom_r2.setPos(21, -5, 0);
		door_right.addChild(door_bottom_r2);
		setRotationAngle(door_bottom_r2, 0, 0, 0.1745F);
		door_bottom_r2.texOffs(54, 169).addBox(-1, 0, 0, 1, 7, 16, 0, false);

		door_exterior = new ModelMapper(modelDataWrapper);
		door_exterior.setPos(0, 24, 0);
		door_exterior.texOffs(20, 126).addBox(21, -10, -20, 0, 5, 4, 0, false);
		door_exterior.texOffs(32, 112).addBox(21, -10, 16, 0, 5, 4, 0, false);

		wall_top_3_r1 = new ModelMapper(modelDataWrapper);
		wall_top_3_r1.setPos(21, -10, 0);
		door_exterior.addChild(wall_top_3_r1);
		setRotationAngle(wall_top_3_r1, 0, 0, -0.0873F);
		wall_top_3_r1.texOffs(70, 202).addBox(0, -26, 16, 0, 26, 4, 0, false);
		wall_top_3_r1.texOffs(78, 202).addBox(0, -26, -20, 0, 26, 4, 0, false);

		wall_bottom_3_r1 = new ModelMapper(modelDataWrapper);
		wall_bottom_3_r1.setPos(21, -5, 0);
		door_exterior.addChild(wall_bottom_3_r1);
		setRotationAngle(wall_bottom_3_r1, 0, 0, 0.1745F);
		wall_bottom_3_r1.texOffs(112, 100).addBox(0, 0, 16, 0, 7, 4, 0, false);
		wall_bottom_3_r1.texOffs(146, 0).addBox(0, 0, -20, 0, 7, 4, 0, false);

		door_left_exterior = new ModelMapper(modelDataWrapper);
		door_left_exterior.setPos(0, 0, 0);
		door_exterior.addChild(door_left_exterior);
		door_left_exterior.texOffs(154, 18).addBox(21, -10, -16, 0, 5, 16, 0, false);

		door_top_r3 = new ModelMapper(modelDataWrapper);
		door_top_r3.setPos(21, -10, 0);
		door_left_exterior.addChild(door_top_r3);
		setRotationAngle(door_top_r3, 0, 0, -0.0873F);
		door_top_r3.texOffs(0, 150).addBox(0, -26, -16, 0, 26, 16, 0, false);

		door_bottom_r3 = new ModelMapper(modelDataWrapper);
		door_bottom_r3.setPos(21, -5, 0);
		door_left_exterior.addChild(door_bottom_r3);
		setRotationAngle(door_bottom_r3, 0, 0, 0.1745F);
		door_bottom_r3.texOffs(72, 153).addBox(0, 0, -16, 0, 7, 16, 0, false);

		door_right_exterior = new ModelMapper(modelDataWrapper);
		door_right_exterior.setPos(0, 0, 0);
		door_exterior.addChild(door_right_exterior);
		door_right_exterior.texOffs(154, 13).addBox(21, -10, 0, 0, 5, 16, 0, false);

		door_top_r4 = new ModelMapper(modelDataWrapper);
		door_top_r4.setPos(21, -10, 0);
		door_right_exterior.addChild(door_top_r4);
		setRotationAngle(door_top_r4, 0, 0, -0.0873F);
		door_top_r4.texOffs(130, 149).addBox(0, -26, 0, 0, 26, 16, 0, false);

		door_bottom_r4 = new ModelMapper(modelDataWrapper);
		door_bottom_r4.setPos(21, -5, 0);
		door_right_exterior.addChild(door_bottom_r4);
		setRotationAngle(door_bottom_r4, 0, 0, 0.1745F);
		door_bottom_r4.texOffs(0, 100).addBox(0, 0, 0, 0, 7, 16, 0, false);

		roof_door_exterior_1 = new ModelMapper(modelDataWrapper);
		roof_door_exterior_1.setPos(0, 24, 0);
		roof_door_exterior_1.texOffs(72, 0).addBox(-0.0293F, -41.2781F, -20, 7, 0, 40, 0, false);

		roof_3_r4 = new ModelMapper(modelDataWrapper);
		roof_3_r4.setPos(11.9516F, -40.8423F, 0);
		roof_door_exterior_1.addChild(roof_3_r4);
		setRotationAngle(roof_3_r4, 0, 0, 0.0873F);
		roof_3_r4.texOffs(20, 42).addBox(-5, 0, -20, 10, 0, 40, 0, false);

		roof_2_r4 = new ModelMapper(modelDataWrapper);
		roof_2_r4.setPos(17.3157F, -40.0862F, 0);
		roof_door_exterior_1.addChild(roof_2_r4);
		setRotationAngle(roof_2_r4, 0, 0, -0.8727F);
		roof_2_r4.texOffs(0, 85).addBox(0, -0.5F, -20, 0, 1, 40, 0, false);

		roof_1_r2 = new ModelMapper(modelDataWrapper);
		roof_1_r2.setPos(18.2163F, -37.8329F, 0);
		roof_door_exterior_1.addChild(roof_1_r2);
		setRotationAngle(roof_1_r2, 0, 0, -0.2618F);
		roof_1_r2.texOffs(60, 50).addBox(0, -2, -20, 0, 4, 40, 0, false);

		roof_door_exterior_2 = new ModelMapper(modelDataWrapper);
		roof_door_exterior_2.setPos(0, 24, 0);
		roof_door_exterior_2.texOffs(70, 42).addBox(-0.0293F, -41.2781F, -20, 7, 0, 40, 0, false);

		roof_4_r3 = new ModelMapper(modelDataWrapper);
		roof_4_r3.setPos(11.9516F, -40.8423F, 0);
		roof_door_exterior_2.addChild(roof_4_r3);
		setRotationAngle(roof_4_r3, 0, 0, 0.0873F);
		roof_4_r3.texOffs(0, 42).addBox(-5, 0, -20, 10, 0, 40, 0, false);

		roof_3_r5 = new ModelMapper(modelDataWrapper);
		roof_3_r5.setPos(17.3157F, -40.0862F, 0);
		roof_door_exterior_2.addChild(roof_3_r5);
		setRotationAngle(roof_3_r5, 0, 0, -0.8727F);
		roof_3_r5.texOffs(80, 84).addBox(0, -0.5F, -20, 0, 1, 40, 0, false);

		roof_2_r5 = new ModelMapper(modelDataWrapper);
		roof_2_r5.setPos(18.2163F, -37.8329F, 0);
		roof_door_exterior_2.addChild(roof_2_r5);
		setRotationAngle(roof_2_r5, 0, 0, -0.2618F);
		roof_2_r5.texOffs(60, 46).addBox(0, -2, -20, 0, 4, 40, 0, false);

		roof_door_exterior_3 = new ModelMapper(modelDataWrapper);
		roof_door_exterior_3.setPos(0, 24, 0);
		roof_door_exterior_3.texOffs(58, 0).addBox(-0.0293F, -41.2781F, -20, 7, 0, 40, 0, false);

		roof_5_r1 = new ModelMapper(modelDataWrapper);
		roof_5_r1.setPos(11.9516F, -40.8423F, 0);
		roof_door_exterior_3.addChild(roof_5_r1);
		setRotationAngle(roof_5_r1, 0, 0, 0.0873F);
		roof_5_r1.texOffs(38, 0).addBox(-5, 0, -20, 10, 0, 40, 0, false);

		roof_4_r4 = new ModelMapper(modelDataWrapper);
		roof_4_r4.setPos(17.3157F, -40.0862F, 0);
		roof_door_exterior_3.addChild(roof_4_r4);
		setRotationAngle(roof_4_r4, 0, 0, -0.8727F);
		roof_4_r4.texOffs(0, 84).addBox(0, -0.5F, -20, 0, 1, 40, 0, false);

		roof_3_r6 = new ModelMapper(modelDataWrapper);
		roof_3_r6.setPos(18.2163F, -37.8329F, 0);
		roof_door_exterior_3.addChild(roof_3_r6);
		setRotationAngle(roof_3_r6, 0, 0, -0.2618F);
		roof_3_r6.texOffs(60, 42).addBox(0, -2, -20, 0, 4, 40, 0, false);

		roof_window_exterior = new ModelMapper(modelDataWrapper);
		roof_window_exterior.setPos(0, 24, 0);
		roof_window_exterior.texOffs(16, 126).addBox(-0.0293F, -41.2781F, -14, 7, 0, 28, 0, false);

		roof_6_r1 = new ModelMapper(modelDataWrapper);
		roof_6_r1.setPos(11.9516F, -40.8423F, 0);
		roof_window_exterior.addChild(roof_6_r1);
		setRotationAngle(roof_6_r1, 0, 0, 0.0873F);
		roof_6_r1.texOffs(98, 0).addBox(-5, 0, -14, 10, 0, 28, 0, false);

		roof_5_r2 = new ModelMapper(modelDataWrapper);
		roof_5_r2.setPos(17.3157F, -40.0862F, 0);
		roof_window_exterior.addChild(roof_5_r2);
		setRotationAngle(roof_5_r2, 0, 0, -0.8727F);
		roof_5_r2.texOffs(74, 123).addBox(0, -0.5F, -14, 0, 1, 28, 0, false);

		roof_4_r5 = new ModelMapper(modelDataWrapper);
		roof_4_r5.setPos(18.2163F, -37.8329F, 0);
		roof_window_exterior.addChild(roof_4_r5);
		setRotationAngle(roof_4_r5, 0, 0, -0.2618F);
		roof_4_r5.texOffs(56, 133).addBox(0, -2, -14, 0, 4, 28, 0, false);

		seat_1 = new ModelMapper(modelDataWrapper);
		seat_1.setPos(0, 24, 0);
		seat_1.texOffs(162, 93).addBox(15, -8, -11, 5, 8, 17, 0, false);
		seat_1.texOffs(160, 139).addBox(11, -9, -10, 9, 1, 16, 0, false);
		seat_1.texOffs(72, 187).addBox(18, -12, -10, 2, 3, 16, 0, false);
		seat_1.texOffs(16, 185).addBox(18, -19, -10, 2, 3, 16, 0, false);
		seat_1.texOffs(194, 126).addBox(11, -19, -11, 9, 11, 1, 0, false);
		seat_1.texOffs(250, 0).addBox(11.5F, -21, -10.5F, 0, 6, 0, 0.2F, false);

		handrail_3_r1 = new ModelMapper(modelDataWrapper);
		handrail_3_r1.setPos(10.0471F, -35.6984F, -10.5F);
		seat_1.addChild(handrail_3_r1);
		setRotationAngle(handrail_3_r1, 0, 0, -0.1309F);
		handrail_3_r1.texOffs(250, 0).addBox(0, -2, 0, 0, 6, 0, 0.2F, false);

		handrail_2_r1 = new ModelMapper(modelDataWrapper);
		handrail_2_r1.setPos(11.7F, -21.2F, 0);
		seat_1.addChild(handrail_2_r1);
		setRotationAngle(handrail_2_r1, 0, 0, -0.0873F);
		handrail_2_r1.texOffs(250, 0).addBox(-0.2F, -10.2F, -10.5F, 0, 10, 0, 0.2F, false);

		seat_2 = new ModelMapper(modelDataWrapper);
		seat_2.setPos(0, 24, 0);
		seat_2.texOffs(161, 68).addBox(15, -8, -6, 5, 8, 17, 0, false);
		seat_2.texOffs(160, 122).addBox(11, -9, -6, 9, 1, 16, 0, false);
		seat_2.texOffs(173, 184).addBox(18, -12, -6, 2, 3, 16, 0, false);
		seat_2.texOffs(184, 156).addBox(18, -19, -6, 2, 3, 16, 0, false);
		seat_2.texOffs(144, 220).addBox(11, -19, 10, 9, 11, 1, 0, false);
		seat_2.texOffs(250, 0).addBox(11.5F, -21, 10.5F, 0, 6, 0, 0.2F, false);

		handrail_3_r2 = new ModelMapper(modelDataWrapper);
		handrail_3_r2.setPos(10.0471F, -35.6984F, -10.5F);
		seat_2.addChild(handrail_3_r2);
		setRotationAngle(handrail_3_r2, 0, 0, -0.1309F);
		handrail_3_r2.texOffs(250, 0).addBox(0, -4, 21, 0, 8, 0, 0.2F, false);

		handrail_2_r2 = new ModelMapper(modelDataWrapper);
		handrail_2_r2.setPos(11.7F, -21.2F, 0);
		seat_2.addChild(handrail_2_r2);
		setRotationAngle(handrail_2_r2, 0, 0, -0.0873F);
		handrail_2_r2.texOffs(250, 0).addBox(-0.2F, -10.2F, 10.5F, 0, 10, 0, 0.2F, false);

		seat_3 = new ModelMapper(modelDataWrapper);
		seat_3.setPos(0, 24, 0);
		seat_3.texOffs(118, 218).addBox(15, -8, -8, 5, 8, 16, 0, false);
		seat_3.texOffs(154, 12).addBox(10, -9, -8, 10, 1, 16, 0, false);
		seat_3.texOffs(32, 166).addBox(17, -12, -8, 3, 3, 16, 0, false);
		seat_3.texOffs(162, 165).addBox(17, -19, -8, 3, 3, 16, 0, false);
		seat_3.texOffs(162, 107).addBox(11, -12, 8, 7, 3, 0, 0, false);
		seat_3.texOffs(162, 107).addBox(11, -12, -8, 7, 3, 0, 0, false);

		back_upper_diagonal_2_r1 = new ModelMapper(modelDataWrapper);
		back_upper_diagonal_2_r1.setPos(18, 0, -8);
		seat_3.addChild(back_upper_diagonal_2_r1);
		setRotationAngle(back_upper_diagonal_2_r1, 0, -0.1745F, 0);
		back_upper_diagonal_2_r1.texOffs(124, 71).addBox(0, -19, 0, 1, 3, 6, 0, false);
		back_upper_diagonal_2_r1.texOffs(0, 135).addBox(0, -12, 0, 1, 3, 6, 0, false);

		back_upper_diagonal_1_r1 = new ModelMapper(modelDataWrapper);
		back_upper_diagonal_1_r1.setPos(18, 0, 8);
		seat_3.addChild(back_upper_diagonal_1_r1);
		setRotationAngle(back_upper_diagonal_1_r1, 0, 0.1745F, 0);
		back_upper_diagonal_1_r1.texOffs(0, 72).addBox(0, -19, -6, 1, 3, 6, 0, false);
		back_upper_diagonal_1_r1.texOffs(0, 144).addBox(0, -12, -6, 1, 3, 6, 0, false);

		seat_diagonal_2_r1 = new ModelMapper(modelDataWrapper);
		seat_diagonal_2_r1.setPos(11, 0, -8);
		seat_3.addChild(seat_diagonal_2_r1);
		setRotationAngle(seat_diagonal_2_r1, 0, -0.1745F, 0);
		seat_diagonal_2_r1.texOffs(152, 87).addBox(0, -9, 0, 1, 1, 6, 0, false);

		seat_diagonal_1_r1 = new ModelMapper(modelDataWrapper);
		seat_diagonal_1_r1.setPos(11, 0, 8);
		seat_3.addChild(seat_diagonal_1_r1);
		setRotationAngle(seat_diagonal_1_r1, 0, 0.1745F, 0);
		seat_diagonal_1_r1.texOffs(12, 130).addBox(0, -9, -6, 1, 1, 6, 0, false);

		light_window = new ModelMapper(modelDataWrapper);
		light_window.setPos(0, 24, 0);


		light_r1 = new ModelMapper(modelDataWrapper);
		light_r1.setPos(10, -37, 0);
		light_window.addChild(light_r1);
		setRotationAngle(light_r1, 0, 0, 0.0873F);
		light_r1.texOffs(6, 209).addBox(-5, -0.1F, -14, 6, 0, 28, 0, false);

		light_door = new ModelMapper(modelDataWrapper);
		light_door.setPos(0, 24, 0);


		light_r2 = new ModelMapper(modelDataWrapper);
		light_r2.setPos(10, -37, 0);
		light_door.addChild(light_r2);
		setRotationAngle(light_r2, 0, 0, 0.0873F);
		light_r2.texOffs(10, 209).addBox(-5, -0.1F, -20, 6, 0, 40, 0, false);

		middle_handrail = new ModelMapper(modelDataWrapper);
		middle_handrail.setPos(0, 24, 0);
		middle_handrail.texOffs(250, 0).addBox(0, -12, 0, 0, 12, 0, 0.2F, false);
		middle_handrail.texOffs(250, 0).addBox(0, -22.0809F, -1.1969F, 0, 1, 0, 0.2F, false);
		middle_handrail.texOffs(250, 0).addBox(0, -37, 0, 0, 5, 0, 0.2F, false);
		middle_handrail.texOffs(183, 219).addBox(0, -35, -18, 0, 0, 36, 0.2F, false);
		middle_handrail.texOffs(36, 52).addBox(-1, -35.5F, -16, 2, 4, 0, 0, false);
		middle_handrail.texOffs(36, 48).addBox(-1, -35.5F, 16, 2, 4, 0, 0, false);

		top_handrail_5_r1 = new ModelMapper(modelDataWrapper);
		top_handrail_5_r1.setPos(0, -36.6392F, 19.8392F);
		middle_handrail.addChild(top_handrail_5_r1);
		setRotationAngle(top_handrail_5_r1, 1.0472F, 0, 0);
		top_handrail_5_r1.texOffs(217, 253).addBox(0, 0, -1, 0, 0, 2, 0.2F, false);

		top_handrail_4_r1 = new ModelMapper(modelDataWrapper);
		top_handrail_4_r1.setPos(0, -34.8F, 18.2F);
		middle_handrail.addChild(top_handrail_4_r1);
		setRotationAngle(top_handrail_4_r1, 0.5236F, 0, 0);
		top_handrail_4_r1.texOffs(218, 254).addBox(0, -0.2F, 0.2F, 0, 0, 1, 0.2F, false);

		top_handrail_3_r1 = new ModelMapper(modelDataWrapper);
		top_handrail_3_r1.setPos(0, -36.6392F, -19.8392F);
		middle_handrail.addChild(top_handrail_3_r1);
		setRotationAngle(top_handrail_3_r1, -1.0472F, 0, 0);
		top_handrail_3_r1.texOffs(217, 253).addBox(0, 0, -1, 0, 0, 2, 0.2F, false);

		top_handrail_2_r1 = new ModelMapper(modelDataWrapper);
		top_handrail_2_r1.setPos(0, -34.8F, -18.2F);
		middle_handrail.addChild(top_handrail_2_r1);
		setRotationAngle(top_handrail_2_r1, -0.5236F, 0, 0);
		top_handrail_2_r1.texOffs(218, 254).addBox(0, -0.2F, -1.2F, 0, 0, 1, 0.2F, false);

		handrail_16_r1 = new ModelMapper(modelDataWrapper);
		handrail_16_r1.setPos(0, -21.5809F, -0.2992F);
		middle_handrail.addChild(handrail_16_r1);
		setRotationAngle(handrail_16_r1, -0.1745F, 2.0944F, 0);
		handrail_16_r1.texOffs(250, 0).addBox(-0.2591F, -10.0003F, -1.9152F, 0, 4, 0, 0.2F, false);

		handrail_15_r1 = new ModelMapper(modelDataWrapper);
		handrail_15_r1.setPos(0, -21.5809F, -0.2992F);
		middle_handrail.addChild(handrail_15_r1);
		setRotationAngle(handrail_15_r1, -0.1745F, -2.0944F, 0);
		handrail_15_r1.texOffs(250, 0).addBox(0.2591F, -10.0003F, -1.9152F, 0, 4, 0, 0.2F, false);

		handrail_14_r1 = new ModelMapper(modelDataWrapper);
		handrail_14_r1.setPos(0, -21.5809F, -0.2992F);
		middle_handrail.addChild(handrail_14_r1);
		setRotationAngle(handrail_14_r1, -0.1745F, 0, 0);
		handrail_14_r1.texOffs(250, 0).addBox(0, -10.0782F, -1.4732F, 0, 4, 0, 0.2F, false);

		handrail_13_r1 = new ModelMapper(modelDataWrapper);
		handrail_13_r1.setPos(0, -21.5809F, -0.2992F);
		middle_handrail.addChild(handrail_13_r1);
		setRotationAngle(handrail_13_r1, -0.0873F, 2.0944F, 0);
		handrail_13_r1.texOffs(250, 0).addBox(-0.2591F, -5.7625F, -1.4017F, 0, 5, 0, 0.2F, false);

		handrail_12_r1 = new ModelMapper(modelDataWrapper);
		handrail_12_r1.setPos(0, -21.5809F, -0.2992F);
		middle_handrail.addChild(handrail_12_r1);
		setRotationAngle(handrail_12_r1, -0.0873F, -2.0944F, 0);
		handrail_12_r1.texOffs(250, 0).addBox(0.2591F, -5.7625F, -1.4017F, 0, 5, 0, 0.2F, false);

		handrail_11_r1 = new ModelMapper(modelDataWrapper);
		handrail_11_r1.setPos(0, -21.5809F, -0.2992F);
		middle_handrail.addChild(handrail_11_r1);
		setRotationAngle(handrail_11_r1, -0.0873F, 0, 0);
		handrail_11_r1.texOffs(250, 0).addBox(0, -5.8017F, -0.9545F, 0, 5, 0, 0.2F, false);

		handrail_10_r1 = new ModelMapper(modelDataWrapper);
		handrail_10_r1.setPos(0, -21.5809F, -0.2992F);
		middle_handrail.addChild(handrail_10_r1);
		setRotationAngle(handrail_10_r1, 0, 2.0944F, 0);
		handrail_10_r1.texOffs(250, 0).addBox(-0.2591F, -0.5F, -1.3465F, 0, 1, 0, 0.2F, false);

		handrail_9_r1 = new ModelMapper(modelDataWrapper);
		handrail_9_r1.setPos(0, -21.5809F, -0.2992F);
		middle_handrail.addChild(handrail_9_r1);
		setRotationAngle(handrail_9_r1, 0, -2.0944F, 0);
		handrail_9_r1.texOffs(250, 0).addBox(0.2591F, -0.5F, -1.3465F, 0, 1, 0, 0.2F, false);

		handrail_7_r1 = new ModelMapper(modelDataWrapper);
		handrail_7_r1.setPos(0, -21.5809F, -0.2992F);
		middle_handrail.addChild(handrail_7_r1);
		setRotationAngle(handrail_7_r1, 0.0873F, 2.0944F, 0);
		handrail_7_r1.texOffs(250, 0).addBox(-0.2591F, 0.7625F, -1.4017F, 0, 5, 0, 0.2F, false);

		handrail_6_r1 = new ModelMapper(modelDataWrapper);
		handrail_6_r1.setPos(0, -21.5809F, -0.2992F);
		middle_handrail.addChild(handrail_6_r1);
		setRotationAngle(handrail_6_r1, 0.0873F, -2.0944F, 0);
		handrail_6_r1.texOffs(250, 0).addBox(0.2591F, 0.7625F, -1.4017F, 0, 5, 0, 0.2F, false);

		handrail_5_r1 = new ModelMapper(modelDataWrapper);
		handrail_5_r1.setPos(0, -21.5809F, -0.2992F);
		middle_handrail.addChild(handrail_5_r1);
		setRotationAngle(handrail_5_r1, 0.0873F, 0, 0);
		handrail_5_r1.texOffs(250, 0).addBox(0, 0.8017F, -0.9545F, 0, 5, 0, 0.2F, false);

		handrail_4_r1 = new ModelMapper(modelDataWrapper);
		handrail_4_r1.setPos(0, -21.5809F, -0.2992F);
		middle_handrail.addChild(handrail_4_r1);
		setRotationAngle(handrail_4_r1, 0.1745F, 2.0944F, 0);
		handrail_4_r1.texOffs(250, 0).addBox(-0.2591F, 6.0003F, -1.9152F, 0, 4, 0, 0.2F, false);

		handrail_3_r3 = new ModelMapper(modelDataWrapper);
		handrail_3_r3.setPos(0, -21.5809F, -0.2992F);
		middle_handrail.addChild(handrail_3_r3);
		setRotationAngle(handrail_3_r3, 0.1745F, -2.0944F, 0);
		handrail_3_r3.texOffs(250, 0).addBox(0.2591F, 6.0003F, -1.9152F, 0, 4, 0, 0.2F, false);

		handrail_2_r3 = new ModelMapper(modelDataWrapper);
		handrail_2_r3.setPos(0, -21.5809F, -0.2992F);
		middle_handrail.addChild(handrail_2_r3);
		setRotationAngle(handrail_2_r3, 0.1745F, 0, 0);
		handrail_2_r3.texOffs(250, 0).addBox(0, 6.0782F, -1.4732F, 0, 4, 0, 0.2F, false);

		head = new ModelMapper(modelDataWrapper);
		head.setPos(0, 24, 0);


		side_1 = new ModelMapper(modelDataWrapper);
		side_1.setPos(0, 0, 0);
		head.addChild(side_1);
		side_1.texOffs(98, 94).addBox(0, 0, -14, 19, 2, 26, 0, false);
		side_1.texOffs(194, 109).addBox(20, -10, 0, 1, 5, 12, 0, false);
		side_1.texOffs(30, 42).addBox(16.734F, -33.9011F, 6, 2, 0, 6, 0, false);
		side_1.texOffs(0, 69).addBox(0, -37.3304F, 9, 7, 0, 3, 0, false);
		side_1.texOffs(124, 67).addBox(0, -33.9011F, -10, 19, 0, 16, 0, false);
		side_1.texOffs(14, 0).addBox(0, -10, -14, 6, 10, 1, 0, false);
		side_1.texOffs(157, 122).addBox(0, -20, -10, 6, 5, 3, 0, false);

		bar_2_r1 = new ModelMapper(modelDataWrapper);
		bar_2_r1.setPos(6, 0, -10);
		side_1.addChild(bar_2_r1);
		setRotationAngle(bar_2_r1, 0, -0.3491F, 0);
		bar_2_r1.texOffs(188, 73).addBox(0, -19, 0, 15, 3, 2, 0, false);

		front_11_r1 = new ModelMapper(modelDataWrapper);
		front_11_r1.setPos(15.9636F, -23.2081F, -8.1364F);
		side_1.addChild(front_11_r1);
		setRotationAngle(front_11_r1, -0.1745F, -0.7854F, 0);
		front_11_r1.texOffs(190, 0).addBox(-3, -13.5F, -0.5F, 8, 27, 1, 0, false);

		front_10_r1 = new ModelMapper(modelDataWrapper);
		front_10_r1.setPos(21, -10, 0);
		side_1.addChild(front_10_r1);
		setRotationAngle(front_10_r1, 0, 0.1745F, -0.0873F);
		front_10_r1.texOffs(153, 184).addBox(-1, -27, -9, 1, 27, 9, 0, false);

		front_9_r1 = new ModelMapper(modelDataWrapper);
		front_9_r1.setPos(21, 0, 0);
		side_1.addChild(front_9_r1);
		setRotationAngle(front_9_r1, 0, 0.1745F, 0);
		front_9_r1.texOffs(200, 20).addBox(-1, -10, -9, 1, 5, 9, 0, false);

		front_8_r1 = new ModelMapper(modelDataWrapper);
		front_8_r1.setPos(17.6158F, -7.5F, -9.7886F);
		side_1.addChild(front_8_r1);
		setRotationAngle(front_8_r1, 0, -0.7854F, 0);
		front_8_r1.texOffs(160, 130).addBox(-3, -2.5F, -0.5F, 6, 5, 1, 0, false);

		front_4_r1 = new ModelMapper(modelDataWrapper);
		front_4_r1.setPos(21, -5, 0);
		side_1.addChild(front_4_r1);
		setRotationAngle(front_4_r1, 0, 0.1745F, 0.1745F);
		front_4_r1.texOffs(194, 139).addBox(-1, 0, -10, 1, 6, 10, 0, false);

		front_3_r1 = new ModelMapper(modelDataWrapper);
		front_3_r1.setPos(17.1096F, -2.5531F, -9.9895F);
		side_1.addChild(front_3_r1);
		setRotationAngle(front_3_r1, 0.0873F, -0.7854F, 0);
		front_3_r1.texOffs(0, 81).addBox(-3.5F, -2.5F, -0.5F, 7, 6, 1, 0, false);

		front_2_r1 = new ModelMapper(modelDataWrapper);
		front_2_r1.setPos(6, 0, -14);
		side_1.addChild(front_2_r1);
		setRotationAngle(front_2_r1, 0, -0.1745F, 0);
		front_2_r1.texOffs(168, 0).addBox(0, -10, 0, 10, 10, 1, 0, false);

		roof_8_r1 = new ModelMapper(modelDataWrapper);
		roof_8_r1.setPos(7.5F, -36.2001F, 8.4821F);
		side_1.addChild(roof_8_r1);
		setRotationAngle(roof_8_r1, 1.0472F, 0, 0);
		roof_8_r1.texOffs(157, 156).addBox(-7.5F, 0, -1.5F, 15, 0, 3, 0, false);

		roof_7_r1 = new ModelMapper(modelDataWrapper);
		roof_7_r1.setPos(8.5F, -34.4011F, 6.866F);
		side_1.addChild(roof_7_r1);
		setRotationAngle(roof_7_r1, 0.5236F, 0, 0);
		roof_7_r1.texOffs(124, 122).addBox(-8.5F, 0, -1, 17, 0, 2, 0, false);

		roof_4_r6 = new ModelMapper(modelDataWrapper);
		roof_4_r6.setPos(10.0171F, -36.9817F, 10);
		side_1.addChild(roof_4_r6);
		setRotationAngle(roof_4_r6, 0, 0, 0.0873F);
		roof_4_r6.texOffs(120, 67).addBox(-4, 0, -2, 8, 0, 4, 0, false);

		roof_3_r7 = new ModelMapper(modelDataWrapper);
		roof_3_r7.setPos(14.5019F, -35.7671F, 9.5F);
		side_1.addChild(roof_3_r7);
		setRotationAngle(roof_3_r7, 0, 0, 1.0472F);
		roof_3_r7.texOffs(19, 48).addBox(-1, 0, -2.5F, 2, 0, 5, 0, false);

		roof_2_r6 = new ModelMapper(modelDataWrapper);
		roof_2_r6.setPos(15.8679F, -34.4011F, 9);
		side_1.addChild(roof_2_r6);
		setRotationAngle(roof_2_r6, 0, 0, 0.5236F);
		roof_2_r6.texOffs(18, 42).addBox(-1, 0, -3, 2, 0, 6, 0, false);

		wall_top_r3 = new ModelMapper(modelDataWrapper);
		wall_top_r3.setPos(21, -10, 0);
		side_1.addChild(wall_top_r3);
		setRotationAngle(wall_top_r3, 0, 0, -0.0873F);
		wall_top_r3.texOffs(0, 0).addBox(-1, -27, 0, 1, 27, 12, 0, false);

		wall_bottom_r3 = new ModelMapper(modelDataWrapper);
		wall_bottom_r3.setPos(21, -5, 0);
		side_1.addChild(wall_bottom_r3);
		setRotationAngle(wall_bottom_r3, 0, 0, 0.1745F);
		wall_bottom_r3.texOffs(193, 175).addBox(-1, 0, 0, 1, 6, 12, 0, false);

		side_2 = new ModelMapper(modelDataWrapper);
		side_2.setPos(0, 0, 0);
		head.addChild(side_2);
		side_2.texOffs(98, 94).addBox(-19, 0, -14, 19, 2, 26, 0, true);
		side_2.texOffs(194, 109).addBox(-21, -10, 0, 1, 5, 12, 0, true);
		side_2.texOffs(30, 42).addBox(-18.734F, -33.9011F, 6, 2, 0, 6, 0, true);
		side_2.texOffs(0, 69).addBox(-7, -37.3304F, 9, 7, 0, 3, 0, true);
		side_2.texOffs(124, 67).addBox(-19, -33.9011F, -10, 19, 0, 16, 0, true);
		side_2.texOffs(14, 0).addBox(-6, -10, -14, 6, 10, 1, 0, true);
		side_2.texOffs(157, 122).addBox(-6, -20, -10, 6, 5, 3, 0, true);

		bar_3_r1 = new ModelMapper(modelDataWrapper);
		bar_3_r1.setPos(-6, 0, -10);
		side_2.addChild(bar_3_r1);
		setRotationAngle(bar_3_r1, 0, 0.3491F, 0);
		bar_3_r1.texOffs(188, 73).addBox(-15, -19, 0, 15, 3, 2, 0, true);

		front_12_r1 = new ModelMapper(modelDataWrapper);
		front_12_r1.setPos(-15.9636F, -23.2081F, -8.1364F);
		side_2.addChild(front_12_r1);
		setRotationAngle(front_12_r1, -0.1745F, 0.7854F, 0);
		front_12_r1.texOffs(190, 0).addBox(-5, -13.5F, -0.5F, 8, 27, 1, 0, true);

		front_11_r2 = new ModelMapper(modelDataWrapper);
		front_11_r2.setPos(-21, -10, 0);
		side_2.addChild(front_11_r2);
		setRotationAngle(front_11_r2, 0, -0.1745F, 0.0873F);
		front_11_r2.texOffs(153, 184).addBox(0, -27, -9, 1, 27, 9, 0, true);

		front_10_r2 = new ModelMapper(modelDataWrapper);
		front_10_r2.setPos(-21, 0, 0);
		side_2.addChild(front_10_r2);
		setRotationAngle(front_10_r2, 0, -0.1745F, 0);
		front_10_r2.texOffs(200, 20).addBox(0, -10, -9, 1, 5, 9, 0, true);

		front_9_r2 = new ModelMapper(modelDataWrapper);
		front_9_r2.setPos(-17.6158F, -7.5F, -9.7886F);
		side_2.addChild(front_9_r2);
		setRotationAngle(front_9_r2, 0, 0.7854F, 0);
		front_9_r2.texOffs(160, 130).addBox(-3, -2.5F, -0.5F, 6, 5, 1, 0, true);

		front_5_r1 = new ModelMapper(modelDataWrapper);
		front_5_r1.setPos(-21, -5, 0);
		side_2.addChild(front_5_r1);
		setRotationAngle(front_5_r1, 0, -0.1745F, -0.1745F);
		front_5_r1.texOffs(194, 139).addBox(0, 0, -10, 1, 6, 10, 0, true);

		front_4_r2 = new ModelMapper(modelDataWrapper);
		front_4_r2.setPos(-17.1096F, -2.5531F, -9.9895F);
		side_2.addChild(front_4_r2);
		setRotationAngle(front_4_r2, 0.0873F, 0.7854F, 0);
		front_4_r2.texOffs(0, 81).addBox(-3.5F, -2.5F, -0.5F, 7, 6, 1, 0, true);

		front_3_r2 = new ModelMapper(modelDataWrapper);
		front_3_r2.setPos(-6, 0, -14);
		side_2.addChild(front_3_r2);
		setRotationAngle(front_3_r2, 0, 0.1745F, 0);
		front_3_r2.texOffs(168, 0).addBox(-10, -10, 0, 10, 10, 1, 0, true);

		roof_9_r1 = new ModelMapper(modelDataWrapper);
		roof_9_r1.setPos(-7.5F, -36.2001F, 8.4821F);
		side_2.addChild(roof_9_r1);
		setRotationAngle(roof_9_r1, 1.0472F, 0, 0);
		roof_9_r1.texOffs(157, 156).addBox(-7.5F, 0, -1.5F, 15, 0, 3, 0, true);

		roof_8_r2 = new ModelMapper(modelDataWrapper);
		roof_8_r2.setPos(-8.5F, -34.4011F, 6.866F);
		side_2.addChild(roof_8_r2);
		setRotationAngle(roof_8_r2, 0.5236F, 0, 0);
		roof_8_r2.texOffs(124, 122).addBox(-8.5F, 0, -1, 17, 0, 2, 0, true);

		roof_5_r3 = new ModelMapper(modelDataWrapper);
		roof_5_r3.setPos(-10.0171F, -36.9817F, 10);
		side_2.addChild(roof_5_r3);
		setRotationAngle(roof_5_r3, 0, 0, -0.0873F);
		roof_5_r3.texOffs(120, 67).addBox(-4, 0, -2, 8, 0, 4, 0, true);

		roof_4_r7 = new ModelMapper(modelDataWrapper);
		roof_4_r7.setPos(-14.5019F, -35.7671F, 9.5F);
		side_2.addChild(roof_4_r7);
		setRotationAngle(roof_4_r7, 0, 0, -1.0472F);
		roof_4_r7.texOffs(19, 48).addBox(-1, 0, -2.5F, 2, 0, 5, 0, true);

		roof_3_r8 = new ModelMapper(modelDataWrapper);
		roof_3_r8.setPos(-15.8679F, -34.4011F, 9);
		side_2.addChild(roof_3_r8);
		setRotationAngle(roof_3_r8, 0, 0, -0.5236F);
		roof_3_r8.texOffs(18, 42).addBox(-1, 0, -3, 2, 0, 6, 0, true);

		wall_top_r4 = new ModelMapper(modelDataWrapper);
		wall_top_r4.setPos(-21, -10, 0);
		side_2.addChild(wall_top_r4);
		setRotationAngle(wall_top_r4, 0, 0, 0.0873F);
		wall_top_r4.texOffs(0, 0).addBox(0, -27, 0, 1, 27, 12, 0, true);

		wall_bottom_r4 = new ModelMapper(modelDataWrapper);
		wall_bottom_r4.setPos(-21, -5, 0);
		side_2.addChild(wall_bottom_r4);
		setRotationAngle(wall_bottom_r4, 0, 0, -0.1745F);
		wall_bottom_r4.texOffs(193, 175).addBox(0, 0, 0, 1, 6, 12, 0, true);

		head_exterior = new ModelMapper(modelDataWrapper);
		head_exterior.setPos(0, 24, 0);


		side_1_exterior = new ModelMapper(modelDataWrapper);
		side_1_exterior.setPos(0, 0, 0);
		head_exterior.addChild(side_1_exterior);
		side_1_exterior.texOffs(98, 101).addBox(21, -10, 0, 0, 5, 12, 0, false);
		side_1_exterior.texOffs(112, 94).addBox(0, -10, -14, 6, 10, 0, 0, false);
		side_1_exterior.texOffs(79, 94).addBox(-0.0293F, -41.2781F, -7, 7, 0, 19, 0, false);

		roof_8_r3 = new ModelMapper(modelDataWrapper);
		roof_8_r3.setPos(3, -40.5084F, -7.6415F);
		side_1_exterior.addChild(roof_8_r3);
		setRotationAngle(roof_8_r3, -0.8727F, 0, 0);
		roof_8_r3.texOffs(32, 166).addBox(-3, -1.5F, 0, 6, 3, 0, 0, false);

		roof_7_r2 = new ModelMapper(modelDataWrapper);
		roof_7_r2.setPos(9.3275F, -40.5084F, -6.9567F);
		side_1_exterior.addChild(roof_7_r2);
		setRotationAngle(roof_7_r2, -0.8727F, -0.1745F, 0);
		roof_7_r2.texOffs(140, 87).addBox(-4.5F, -1.5F, 0, 9, 3, 0, 0, false);

		roof_6_r2 = new ModelMapper(modelDataWrapper);
		roof_6_r2.setPos(14.1804F, -40.5084F, -4.9389F);
		side_1_exterior.addChild(roof_6_r2);
		setRotationAngle(roof_6_r2, -0.8727F, -0.7854F, 0);
		roof_6_r2.texOffs(140, 90).addBox(-4, -1.5F, 0, 8, 3, 0, 0, false);

		roof_5_r4 = new ModelMapper(modelDataWrapper);
		roof_5_r4.setPos(17.7514F, -38.226F, -1.9696F);
		side_1_exterior.addChild(roof_5_r4);
		setRotationAngle(roof_5_r4, 0, 0.1745F, -0.2618F);
		roof_5_r4.texOffs(8, 68).addBox(0, -2.5F, -2, 0, 5, 4, 0, false);

		roof_3_r9 = new ModelMapper(modelDataWrapper);
		roof_3_r9.setPos(11.9516F, -40.8423F, 0);
		side_1_exterior.addChild(roof_3_r9);
		setRotationAngle(roof_3_r9, 0, 0, 0.0873F);
		roof_3_r9.texOffs(0, 70).addBox(-5, 0, -7, 10, 0, 19, 0, false);

		roof_2_r7 = new ModelMapper(modelDataWrapper);
		roof_2_r7.setPos(17.3157F, -40.0862F, 0);
		side_1_exterior.addChild(roof_2_r7);
		setRotationAngle(roof_2_r7, 0, 0, -0.8727F);
		roof_2_r7.texOffs(0, 23).addBox(0, -0.5F, -4, 0, 1, 16, 0, false);

		roof_1_r3 = new ModelMapper(modelDataWrapper);
		roof_1_r3.setPos(18.2163F, -37.8329F, 0);
		side_1_exterior.addChild(roof_1_r3);
		setRotationAngle(roof_1_r3, 0, 0, -0.2618F);
		roof_1_r3.texOffs(0, 114).addBox(0, -2, 0, 0, 4, 12, 0, false);

		front_14_r1 = new ModelMapper(modelDataWrapper);
		front_14_r1.setPos(0, -10, -14);
		side_1_exterior.addChild(front_14_r1);
		setRotationAngle(front_14_r1, -0.1745F, 0, 0);
		front_14_r1.texOffs(118, 203).addBox(0, -30, 0, 6, 30, 0, 0, false);

		front_13_r1 = new ModelMapper(modelDataWrapper);
		front_13_r1.setPos(6, -10, -14);
		side_1_exterior.addChild(front_13_r1);
		setRotationAngle(front_13_r1, -0.1745F, -0.1745F, 0);
		front_13_r1.texOffs(173, 203).addBox(0, -30, 0, 10, 30, 0, 0, false);

		front_12_r2 = new ModelMapper(modelDataWrapper);
		front_12_r2.setPos(16.6707F, -23.2081F, -7.4293F);
		side_1_exterior.addChild(front_12_r2);
		setRotationAngle(front_12_r2, -0.1745F, -0.7854F, 0);
		front_12_r2.texOffs(193, 203).addBox(-4, -16.5F, -0.5F, 8, 30, 0, 0, false);

		front_11_r3 = new ModelMapper(modelDataWrapper);
		front_11_r3.setPos(21, -10, 0);
		side_1_exterior.addChild(front_11_r3);
		setRotationAngle(front_11_r3, 0, 0.1745F, -0.0873F);
		front_11_r3.texOffs(130, 182).addBox(0, -27, -9, 0, 27, 9, 0, false);

		front_10_r3 = new ModelMapper(modelDataWrapper);
		front_10_r3.setPos(21, 0, 0);
		side_1_exterior.addChild(front_10_r3);
		setRotationAngle(front_10_r3, 0, 0.1745F, 0);
		front_10_r3.texOffs(0, 121).addBox(0, -10, -9, 0, 5, 9, 0, false);

		front_9_r3 = new ModelMapper(modelDataWrapper);
		front_9_r3.setPos(17.6158F, -7.5F, -9.7886F);
		side_1_exterior.addChild(front_9_r3);
		setRotationAngle(front_9_r3, 0, -0.7854F, 0);
		front_9_r3.texOffs(162, 102).addBox(-3, -2.5F, -0.5F, 6, 5, 0, 0, false);

		front_8_r2 = new ModelMapper(modelDataWrapper);
		front_8_r2.setPos(15.9306F, 5.3106F, -9.5176F);
		side_1_exterior.addChild(front_8_r2);
		setRotationAngle(front_8_r2, 0.1745F, -0.7854F, 0);
		front_8_r2.texOffs(0, 0).addBox(-3, -5.5F, -0.5F, 6, 11, 0, 0, false);

		front_7_r1 = new ModelMapper(modelDataWrapper);
		front_7_r1.setPos(6, 0, -14);
		side_1_exterior.addChild(front_7_r1);
		setRotationAngle(front_7_r1, 0.2618F, -0.1745F, 0);
		front_7_r1.texOffs(126, 28).addBox(0, 0, 0, 11, 11, 0, 0, false);

		front_6_r1 = new ModelMapper(modelDataWrapper);
		front_6_r1.setPos(0, 0, -14);
		side_1_exterior.addChild(front_6_r1);
		setRotationAngle(front_6_r1, 0.2618F, 0, 0);
		front_6_r1.texOffs(26, 28).addBox(0, 0, 0, 6, 11, 0, 0, false);

		front_5_r2 = new ModelMapper(modelDataWrapper);
		front_5_r2.setPos(21, -5, 0);
		side_1_exterior.addChild(front_5_r2);
		setRotationAngle(front_5_r2, 0, 0.1745F, 0.1745F);
		front_5_r2.texOffs(182, 42).addBox(0, 0, -10, 0, 16, 10, 0, false);

		front_4_r3 = new ModelMapper(modelDataWrapper);
		front_4_r3.setPos(17.1096F, -2.5531F, -9.9895F);
		side_1_exterior.addChild(front_4_r3);
		setRotationAngle(front_4_r3, 0.0873F, -0.7854F, 0);
		front_4_r3.texOffs(160, 150).addBox(-3.5F, -2.5F, -0.5F, 7, 5, 0, 0, false);

		front_3_r3 = new ModelMapper(modelDataWrapper);
		front_3_r3.setPos(6, 0, -14);
		side_1_exterior.addChild(front_3_r3);
		setRotationAngle(front_3_r3, 0, -0.1745F, 0);
		front_3_r3.texOffs(200, 34).addBox(0, -10, 0, 10, 10, 0, 0, false);

		wall_top_r5 = new ModelMapper(modelDataWrapper);
		wall_top_r5.setPos(21, -10, 0);
		side_1_exterior.addChild(wall_top_r5);
		setRotationAngle(wall_top_r5, 0, 0, -0.0873F);
		wall_top_r5.texOffs(0, 30).addBox(0, -27, 0, 0, 27, 12, 0, false);

		wall_bottom_r5 = new ModelMapper(modelDataWrapper);
		wall_bottom_r5.setPos(21, -5, 0);
		side_1_exterior.addChild(wall_bottom_r5);
		setRotationAngle(wall_bottom_r5, 0, 0, 0.1745F);
		wall_bottom_r5.texOffs(189, 81).addBox(0, 0, 0, 0, 16, 12, 0, false);

		side_2_exterior = new ModelMapper(modelDataWrapper);
		side_2_exterior.setPos(0, 0, 0);
		head_exterior.addChild(side_2_exterior);
		side_2_exterior.texOffs(98, 101).addBox(-21, -10, 0, 0, 5, 12, 0, true);
		side_2_exterior.texOffs(112, 94).addBox(-6, -10, -14, 6, 10, 0, 0, true);
		side_2_exterior.texOffs(79, 94).addBox(-6.9707F, -41.2781F, -7, 7, 0, 19, 0, true);

		roof_9_r2 = new ModelMapper(modelDataWrapper);
		roof_9_r2.setPos(-3, -40.5084F, -7.6415F);
		side_2_exterior.addChild(roof_9_r2);
		setRotationAngle(roof_9_r2, -0.8727F, 0, 0);
		roof_9_r2.texOffs(32, 166).addBox(-3, -1.5F, 0, 6, 3, 0, 0, true);

		roof_8_r4 = new ModelMapper(modelDataWrapper);
		roof_8_r4.setPos(-9.3275F, -40.5084F, -6.9567F);
		side_2_exterior.addChild(roof_8_r4);
		setRotationAngle(roof_8_r4, -0.8727F, 0.1745F, 0);
		roof_8_r4.texOffs(140, 87).addBox(-4.5F, -1.5F, 0, 9, 3, 0, 0, true);

		roof_7_r3 = new ModelMapper(modelDataWrapper);
		roof_7_r3.setPos(-14.1804F, -40.5084F, -4.9389F);
		side_2_exterior.addChild(roof_7_r3);
		setRotationAngle(roof_7_r3, -0.8727F, 0.7854F, 0);
		roof_7_r3.texOffs(140, 90).addBox(-4, -1.5F, 0, 8, 3, 0, 0, true);

		roof_6_r3 = new ModelMapper(modelDataWrapper);
		roof_6_r3.setPos(-17.7514F, -38.226F, -1.9696F);
		side_2_exterior.addChild(roof_6_r3);
		setRotationAngle(roof_6_r3, 0, -0.1745F, 0.2618F);
		roof_6_r3.texOffs(8, 68).addBox(0, -2.5F, -2, 0, 5, 4, 0, true);

		roof_4_r8 = new ModelMapper(modelDataWrapper);
		roof_4_r8.setPos(-11.9516F, -40.8423F, 0);
		side_2_exterior.addChild(roof_4_r8);
		setRotationAngle(roof_4_r8, 0, 0, -0.0873F);
		roof_4_r8.texOffs(0, 70).addBox(-5, 0, -7, 10, 0, 19, 0, true);

		roof_3_r10 = new ModelMapper(modelDataWrapper);
		roof_3_r10.setPos(-17.3157F, -40.0862F, 0);
		side_2_exterior.addChild(roof_3_r10);
		setRotationAngle(roof_3_r10, 0, 0, 0.8727F);
		roof_3_r10.texOffs(0, 23).addBox(0, -0.5F, -4, 0, 1, 16, 0, true);

		roof_2_r8 = new ModelMapper(modelDataWrapper);
		roof_2_r8.setPos(-18.2163F, -37.8329F, 0);
		side_2_exterior.addChild(roof_2_r8);
		setRotationAngle(roof_2_r8, 0, 0, 0.2618F);
		roof_2_r8.texOffs(0, 114).addBox(0, -2, 0, 0, 4, 12, 0, true);

		front_15_r1 = new ModelMapper(modelDataWrapper);
		front_15_r1.setPos(0, -10, -14);
		side_2_exterior.addChild(front_15_r1);
		setRotationAngle(front_15_r1, -0.1745F, 0, 0);
		front_15_r1.texOffs(118, 203).addBox(-6, -30, 0, 6, 30, 0, 0, true);

		front_14_r2 = new ModelMapper(modelDataWrapper);
		front_14_r2.setPos(-6, -10, -14);
		side_2_exterior.addChild(front_14_r2);
		setRotationAngle(front_14_r2, -0.1745F, 0.1745F, 0);
		front_14_r2.texOffs(173, 203).addBox(-10, -30, 0, 10, 30, 0, 0, true);

		front_13_r2 = new ModelMapper(modelDataWrapper);
		front_13_r2.setPos(-16.6707F, -23.2081F, -7.4293F);
		side_2_exterior.addChild(front_13_r2);
		setRotationAngle(front_13_r2, -0.1745F, 0.7854F, 0);
		front_13_r2.texOffs(193, 203).addBox(-4, -16.5F, -0.5F, 8, 30, 0, 0, true);

		front_12_r3 = new ModelMapper(modelDataWrapper);
		front_12_r3.setPos(-21, -10, 0);
		side_2_exterior.addChild(front_12_r3);
		setRotationAngle(front_12_r3, 0, -0.1745F, 0.0873F);
		front_12_r3.texOffs(130, 182).addBox(0, -27, -9, 0, 27, 9, 0, true);

		front_11_r4 = new ModelMapper(modelDataWrapper);
		front_11_r4.setPos(-21, 0, 0);
		side_2_exterior.addChild(front_11_r4);
		setRotationAngle(front_11_r4, 0, -0.1745F, 0);
		front_11_r4.texOffs(0, 121).addBox(0, -10, -9, 0, 5, 9, 0, true);

		front_10_r4 = new ModelMapper(modelDataWrapper);
		front_10_r4.setPos(-17.6158F, -7.5F, -9.7886F);
		side_2_exterior.addChild(front_10_r4);
		setRotationAngle(front_10_r4, 0, 0.7854F, 0);
		front_10_r4.texOffs(162, 102).addBox(-3, -2.5F, -0.5F, 6, 5, 0, 0, true);

		front_9_r4 = new ModelMapper(modelDataWrapper);
		front_9_r4.setPos(-15.9306F, 5.3106F, -9.5176F);
		side_2_exterior.addChild(front_9_r4);
		setRotationAngle(front_9_r4, 0.1745F, 0.7854F, 0);
		front_9_r4.texOffs(0, 0).addBox(-3, -5.5F, -0.5F, 6, 11, 0, 0, true);

		front_8_r3 = new ModelMapper(modelDataWrapper);
		front_8_r3.setPos(-6, 0, -14);
		side_2_exterior.addChild(front_8_r3);
		setRotationAngle(front_8_r3, 0.2618F, 0.1745F, 0);
		front_8_r3.texOffs(126, 28).addBox(-11, 0, 0, 11, 11, 0, 0, true);

		front_7_r2 = new ModelMapper(modelDataWrapper);
		front_7_r2.setPos(0, 0, -14);
		side_2_exterior.addChild(front_7_r2);
		setRotationAngle(front_7_r2, 0.2618F, 0, 0);
		front_7_r2.texOffs(26, 28).addBox(-6, 0, 0, 6, 11, 0, 0, true);

		front_6_r2 = new ModelMapper(modelDataWrapper);
		front_6_r2.setPos(-21, -5, 0);
		side_2_exterior.addChild(front_6_r2);
		setRotationAngle(front_6_r2, 0, -0.1745F, -0.1745F);
		front_6_r2.texOffs(182, 42).addBox(0, 0, -10, 0, 16, 10, 0, true);

		front_5_r3 = new ModelMapper(modelDataWrapper);
		front_5_r3.setPos(-17.1096F, -2.5531F, -9.9895F);
		side_2_exterior.addChild(front_5_r3);
		setRotationAngle(front_5_r3, 0.0873F, 0.7854F, 0);
		front_5_r3.texOffs(160, 150).addBox(-3.5F, -2.5F, -0.5F, 7, 5, 0, 0, true);

		front_4_r4 = new ModelMapper(modelDataWrapper);
		front_4_r4.setPos(-6, 0, -14);
		side_2_exterior.addChild(front_4_r4);
		setRotationAngle(front_4_r4, 0, 0.1745F, 0);
		front_4_r4.texOffs(200, 34).addBox(-10, -10, 0, 10, 10, 0, 0, true);

		wall_top_r6 = new ModelMapper(modelDataWrapper);
		wall_top_r6.setPos(-21, -10, 0);
		side_2_exterior.addChild(wall_top_r6);
		setRotationAngle(wall_top_r6, 0, 0, 0.0873F);
		wall_top_r6.texOffs(0, 30).addBox(0, -27, 0, 0, 27, 12, 0, true);

		wall_bottom_r6 = new ModelMapper(modelDataWrapper);
		wall_bottom_r6.setPos(-21, -5, 0);
		side_2_exterior.addChild(wall_bottom_r6);
		setRotationAngle(wall_bottom_r6, 0, 0, -0.1745F);
		wall_bottom_r6.texOffs(189, 81).addBox(0, 0, 0, 0, 16, 12, 0, true);

		door_light_on = new ModelMapper(modelDataWrapper);
		door_light_on.setPos(0, 24, 0);


		light_r3 = new ModelMapper(modelDataWrapper);
		light_r3.setPos(18.5F, -38, 0);
		door_light_on.addChild(light_r3);
		setRotationAngle(light_r3, 0, 0, -0.2618F);
		light_r3.texOffs(28, 2).addBox(-1, -1, -0.5F, 1, 1, 1, 0, false);

		door_light_off = new ModelMapper(modelDataWrapper);
		door_light_off.setPos(0, 24, 0);


		light_r4 = new ModelMapper(modelDataWrapper);
		light_r4.setPos(18.5F, -38, 0);
		door_light_off.addChild(light_r4);
		setRotationAngle(light_r4, 0, 0, -0.2618F);
		light_r4.texOffs(28, 0).addBox(-1, -1, -0.5F, 1, 1, 1, 0, false);

		headlights = new ModelMapper(modelDataWrapper);
		headlights.setPos(0, 24, 0);


		headlight_4_r1 = new ModelMapper(modelDataWrapper);
		headlight_4_r1.setPos(-6, 0, -14);
		headlights.addChild(headlight_4_r1);
		setRotationAngle(headlight_4_r1, 0.2618F, 0.1745F, 0);
		headlight_4_r1.texOffs(208, 0).addBox(-11, 0, -0.05F, 11, 11, 0, 0, true);

		headlight_3_r1 = new ModelMapper(modelDataWrapper);
		headlight_3_r1.setPos(-15.9306F, 5.3106F, -9.5176F);
		headlights.addChild(headlight_3_r1);
		setRotationAngle(headlight_3_r1, 0.1745F, 0.7854F, 0);
		headlight_3_r1.texOffs(230, 0).addBox(-3, -5.5F, -0.55F, 6, 11, 0, 0, true);

		headlight_2_r1 = new ModelMapper(modelDataWrapper);
		headlight_2_r1.setPos(15.9306F, 5.3106F, -9.5176F);
		headlights.addChild(headlight_2_r1);
		setRotationAngle(headlight_2_r1, 0.1745F, -0.7854F, 0);
		headlight_2_r1.texOffs(230, 0).addBox(-3, -5.5F, -0.55F, 6, 11, 0, 0, false);

		headlight_1_r1 = new ModelMapper(modelDataWrapper);
		headlight_1_r1.setPos(6, 0, -14);
		headlights.addChild(headlight_1_r1);
		setRotationAngle(headlight_1_r1, 0.2618F, -0.1745F, 0);
		headlight_1_r1.texOffs(208, 0).addBox(0, 0, -0.05F, 11, 11, 0, 0, false);

		tail_lights = new ModelMapper(modelDataWrapper);
		tail_lights.setPos(0, 24, 0);


		headlight_4_r2 = new ModelMapper(modelDataWrapper);
		headlight_4_r2.setPos(-6, 0, -14);
		tail_lights.addChild(headlight_4_r2);
		setRotationAngle(headlight_4_r2, 0.2618F, 0.1745F, 0);
		headlight_4_r2.texOffs(211, 11).addBox(-11, 0, -0.05F, 11, 11, 0, 0, true);

		headlight_3_r2 = new ModelMapper(modelDataWrapper);
		headlight_3_r2.setPos(-15.9306F, 5.3106F, -9.5176F);
		tail_lights.addChild(headlight_3_r2);
		setRotationAngle(headlight_3_r2, 0.1745F, 0.7854F, 0);
		headlight_3_r2.texOffs(233, 11).addBox(-3, -5.5F, -0.55F, 6, 11, 0, 0, true);

		headlight_2_r2 = new ModelMapper(modelDataWrapper);
		headlight_2_r2.setPos(15.9306F, 5.3106F, -9.5176F);
		tail_lights.addChild(headlight_2_r2);
		setRotationAngle(headlight_2_r2, 0.1745F, -0.7854F, 0);
		headlight_2_r2.texOffs(233, 11).addBox(-3, -5.5F, -0.55F, 6, 11, 0, 0, false);

		headlight_1_r2 = new ModelMapper(modelDataWrapper);
		headlight_1_r2.setPos(6, 0, -14);
		tail_lights.addChild(headlight_1_r2);
		setRotationAngle(headlight_1_r2, 0.2618F, -0.1745F, 0);
		headlight_1_r2.texOffs(211, 11).addBox(0, 0, -0.05F, 11, 11, 0, 0, false);

		modelDataWrapper.setModelPart(textureWidth, textureHeight);
		window.setModelPart();
		window_exterior.setModelPart();
		door.setModelPart();
		door_left.setModelPart(door.name);
		door_right.setModelPart(door.name);
		door_exterior.setModelPart();
		door_left_exterior.setModelPart(door_exterior.name);
		door_right_exterior.setModelPart(door_exterior.name);
		roof_door_exterior_1.setModelPart();
		roof_door_exterior_2.setModelPart();
		roof_door_exterior_3.setModelPart();
		seat_1.setModelPart();
		seat_2.setModelPart();
		seat_3.setModelPart();
		light_window.setModelPart();
		light_door.setModelPart();
		middle_handrail.setModelPart();
		head.setModelPart();
		head_exterior.setModelPart();
		door_light_on.setModelPart();
		door_light_off.setModelPart();
		headlights.setModelPart();
		tail_lights.setModelPart();
	}

	private static final int DOOR_MAX = 16;

	@Override
	public ModelMPL85 createNew(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		return new ModelMPL85(doorAnimationType, renderDoorOverlay);
	}

	@Override
	protected void renderWindowPositions(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
		switch (renderStage) {
			case LIGHTS:
				renderMirror(light_window, matrices, vertices, light, position);
				break;
			case INTERIOR:
				renderMirror(window, matrices, vertices, light, position);
				break;
			case EXTERIOR:
				renderMirror(window_exterior, matrices, vertices, light, position);
				break;
		}
	}

	@Override
	protected void renderDoorPositions(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
		switch (renderStage) {
			case LIGHTS:
				renderMirror(light_door, matrices, vertices, light, position);
				if (renderDetails) {
					if (doorLeftZ > 0) {
						renderOnce(door_light_on, matrices, vertices, light, position);
					}
					if (doorRightZ > 0) {
						renderOnceFlipped(door_light_on, matrices, vertices, light, position);
					}
				}
				break;
			case INTERIOR:
				door_left.setOffset(-doorLeftX, 0, -doorLeftZ);
				door_right.setOffset(-doorLeftX, 0, doorLeftZ);
				renderOnce(door, matrices, vertices, light, position);
				door_left.setOffset(-doorRightX, 0, -doorRightZ);
				door_right.setOffset(-doorRightX, 0, doorRightZ);
				renderOnceFlipped(door, matrices, vertices, light, position);
				if (renderDetails) {
					renderOnce(seat_1, matrices, vertices, light, position + 34);
					renderOnceFlipped(seat_2, matrices, vertices, light, position + 34);
					renderMirror(seat_3, matrices, vertices, light, position + 48);
					renderOnceFlipped(seat_1, matrices, vertices, light, position - 34);
					renderOnce(seat_2, matrices, vertices, light, position - 34);
					renderOnce(middle_handrail, matrices, vertices, light, position);
					if (!isIndex(-1, position, getDoorPositions())) {
						renderOnce(middle_handrail, matrices, vertices, light, position + 48);
					}
				}
				break;
			case EXTERIOR:
				door_left_exterior.setOffset(-doorLeftX, 0, -doorLeftZ);
				door_right_exterior.setOffset(-doorLeftX, 0, doorLeftZ);
				renderOnce(door_exterior, matrices, vertices, light, position);
				door_left_exterior.setOffset(-doorRightX, 0, -doorRightZ);
				door_right_exterior.setOffset(-doorRightX, 0, doorRightZ);
				renderOnceFlipped(door_exterior, matrices, vertices, light, position);
				renderMirror(isIndex(0, position, getDoorPositions()) ? roof_door_exterior_1 : isIndex(1, position, getDoorPositions()) ? roof_door_exterior_2 : roof_door_exterior_3, matrices, vertices, light, position);
				if (renderDetails) {
					if (doorLeftZ == 0) {
						renderOnce(door_light_off, matrices, vertices, light, position);
					}
					if (doorRightZ == 0) {
						renderOnceFlipped(door_light_off, matrices, vertices, light, position);
					}
				}
				break;
		}
	}

	@Override
	protected void renderHeadPosition1(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean useHeadlights) {
		renderEndPosition1(matrices, vertices, renderStage, light, position, renderDetails, useHeadlights, true);
	}

	@Override
	protected void renderHeadPosition2(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean useHeadlights) {
		renderEndPosition2(matrices, vertices, renderStage, light, position, useHeadlights, true);
	}

	@Override
	protected void renderEndPosition1(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
		renderEndPosition1(matrices, vertices, renderStage, light, position, renderDetails, true, false);
	}

	@Override
	protected void renderEndPosition2(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
		renderEndPosition2(matrices, vertices, renderStage, light, position, true, false);
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
		return new int[]{-130, -62, -34, 34, 62, 130};
	}

	@Override
	protected int[] getDoorPositions() {
		return new int[]{-96, 0, 96};
	}

	@Override
	protected int[] getEndPositions() {
		return new int[]{-156, 156};
	}

	@Override
	protected int getDoorMax() {
		return DOOR_MAX;
	}

	private void renderEndPosition1(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, boolean useHeadlights, boolean isHead) {
		switch (renderStage) {
			case ALWAYS_ON_LIGHTS:
				if (isHead) {
					renderOnce(useHeadlights ? headlights : tail_lights, matrices, vertices, light, position);
				}
				break;
			case INTERIOR:
				renderOnce(head, matrices, vertices, light, position);
				if (renderDetails) {
					renderMirror(seat_3, matrices, vertices, light, position + 12);
				}
				break;
			case EXTERIOR:
				renderOnce(head_exterior, matrices, vertices, light, position);
				break;
		}
	}

	private void renderEndPosition2(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean useHeadlights, boolean isHead) {
		switch (renderStage) {
			case ALWAYS_ON_LIGHTS:
				if (isHead) {
					renderOnceFlipped(useHeadlights ? headlights : tail_lights, matrices, vertices, light, position);
				}
				break;
			case INTERIOR:
				renderOnceFlipped(head, matrices, vertices, light, position);
				break;
			case EXTERIOR:
				renderOnceFlipped(head_exterior, matrices, vertices, light, position);
				break;
		}
	}
}