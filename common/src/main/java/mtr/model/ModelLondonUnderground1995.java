package mtr.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mtr.client.DoorAnimationType;
import mtr.data.Route;
import mtr.data.Station;
import mtr.mappings.ModelDataWrapper;
import mtr.mappings.ModelMapper;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;

public class ModelLondonUnderground1995 extends ModelSimpleTrainBase<ModelLondonUnderground1995> {

	private final ModelMapper window;
	private final ModelMapper window_1;
	private final ModelMapper roof_2_r1;
	private final ModelMapper roof_1_r1;
	private final ModelMapper window_side_pole_r1;
	private final ModelMapper window_top_r1;
	private final ModelMapper window_2;
	private final ModelMapper roof_3_r1;
	private final ModelMapper roof_2_r2;
	private final ModelMapper window_side_pole_r2;
	private final ModelMapper window_top_r2;
	private final ModelMapper window_exterior;
	private final ModelMapper window_exterior_1;
	private final ModelMapper roof_2_r3;
	private final ModelMapper roof_1_r2;
	private final ModelMapper window_top_r3;
	private final ModelMapper window_middle_r1;
	private final ModelMapper window_exterior_2;
	private final ModelMapper roof_3_r2;
	private final ModelMapper roof_2_r4;
	private final ModelMapper window_top_r4;
	private final ModelMapper window_middle_r2;
	private final ModelMapper door_left;
	private final ModelMapper door_left_part;
	private final ModelMapper door_left_top_r1;
	private final ModelMapper door_left_middle_r1;
	private final ModelMapper door_right;
	private final ModelMapper door_right_part;
	private final ModelMapper door_right_top_r1;
	private final ModelMapper door_right_middle_r1;
	private final ModelMapper door_left_exterior;
	private final ModelMapper door_left_exterior_part;
	private final ModelMapper door_left_top_r2;
	private final ModelMapper door_left_middle_r2;
	private final ModelMapper door_right_exterior;
	private final ModelMapper door_right_exterior_part;
	private final ModelMapper door_right_top_r2;
	private final ModelMapper door_right_middle_r2;
	private final ModelMapper seat;
	private final ModelMapper seat_1;
	private final ModelMapper handrail_3_r1;
	private final ModelMapper handrail_2_r1;
	private final ModelMapper seat_back_r1;
	private final ModelMapper seat_2;
	private final ModelMapper handrail_4_r1;
	private final ModelMapper handrail_3_r2;
	private final ModelMapper seat_back_r2;
	private final ModelMapper seat_wheelchair;
	private final ModelMapper seat_wheelchair_1;
	private final ModelMapper handrail_4_r2;
	private final ModelMapper handrail_2_r2;
	private final ModelMapper seat_back_r3;
	private final ModelMapper seat_wheelchair_2;
	private final ModelMapper handrail_5_r1;
	private final ModelMapper handrail_3_r3;
	private final ModelMapper seat_back_r4;
	private final ModelMapper window_light;
	private final ModelMapper light_2_r1;
	private final ModelMapper light_1_r1;
	private final ModelMapper window_light_end;
	private final ModelMapper light_3_r1;
	private final ModelMapper light_2_r2;
	private final ModelMapper window_light_head;
	private final ModelMapper light_2_r3;
	private final ModelMapper light_1_r2;
	private final ModelMapper side_panel;
	private final ModelMapper side_panel_r1;
	private final ModelMapper end;
	private final ModelMapper end_1;
	private final ModelMapper roof_2_r5;
	private final ModelMapper roof_1_r3;
	private final ModelMapper window_top_r5;
	private final ModelMapper window_middle_r3;
	private final ModelMapper front_box_top_r1;
	private final ModelMapper front_box_r1;
	private final ModelMapper front_side_r1;
	private final ModelMapper end_2;
	private final ModelMapper roof_3_r3;
	private final ModelMapper roof_2_r6;
	private final ModelMapper window_top_r6;
	private final ModelMapper window_middle_r4;
	private final ModelMapper front_box_top_r2;
	private final ModelMapper front_box_r2;
	private final ModelMapper front_side_r2;
	private final ModelMapper end_exterior;
	private final ModelMapper end_exterior_1;
	private final ModelMapper front_side_bottom_r1;
	private final ModelMapper front_side_6_r1;
	private final ModelMapper front_side_5_r1;
	private final ModelMapper front_side_4_r1;
	private final ModelMapper front_side_3_r1;
	private final ModelMapper front_side_2_r1;
	private final ModelMapper end_exterior_2;
	private final ModelMapper front_side_bottom_r2;
	private final ModelMapper front_side_7_r1;
	private final ModelMapper front_side_6_r2;
	private final ModelMapper front_side_5_r2;
	private final ModelMapper front_side_4_r2;
	private final ModelMapper front_side_3_r2;
	private final ModelMapper head;
	private final ModelMapper head_1;
	private final ModelMapper roof_2_r7;
	private final ModelMapper roof_1_r4;
	private final ModelMapper window_top_r7;
	private final ModelMapper window_middle_r5;
	private final ModelMapper head_2;
	private final ModelMapper roof_3_r4;
	private final ModelMapper roof_2_r8;
	private final ModelMapper window_top_r8;
	private final ModelMapper window_middle_r6;
	private final ModelMapper head_exterior;
	private final ModelMapper small_light_r1;
	private final ModelMapper front_side_right_r1;
	private final ModelMapper front_side_left_r1;
	private final ModelMapper head_exterior_1;
	private final ModelMapper front_side_bottom_r3;
	private final ModelMapper front_side_5_r3;
	private final ModelMapper front_side_4_r3;
	private final ModelMapper front_side_3_r3;
	private final ModelMapper front_side_1_r1;
	private final ModelMapper door_right_top_r3;
	private final ModelMapper door_right_middle_r3;
	private final ModelMapper roof_1_r5;
	private final ModelMapper window_top_r9;
	private final ModelMapper window_middle_r7;
	private final ModelMapper head_exterior_2;
	private final ModelMapper front_side_bottom_r4;
	private final ModelMapper front_side_6_r3;
	private final ModelMapper front_side_5_r4;
	private final ModelMapper front_side_4_r4;
	private final ModelMapper front_side_2_r2;
	private final ModelMapper door_right_top_r4;
	private final ModelMapper door_right_middle_r4;
	private final ModelMapper roof_2_r9;
	private final ModelMapper window_top_r10;
	private final ModelMapper window_middle_r8;
	private final ModelMapper logo;
	private final ModelMapper door_light_on;
	private final ModelMapper light_r1;
	private final ModelMapper door_light_off;
	private final ModelMapper light_r2;
	private final ModelMapper headlights;
	private final ModelMapper light_2_r4;
	private final ModelMapper light_1_r3;
	private final ModelMapper tail_lights;
	private final ModelMapper light_3_r2;
	private final ModelMapper light_2_r5;

	public ModelLondonUnderground1995() {
		this(DoorAnimationType.STANDARD, true);
	}

	private ModelLondonUnderground1995(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		super(doorAnimationType, renderDoorOverlay);
		final int textureWidth = 288;
		final int textureHeight = 288;

		final ModelDataWrapper modelDataWrapper = new ModelDataWrapper(this, textureWidth, textureHeight);

		window = new ModelMapper(modelDataWrapper);
		window.setPos(0, 24, 0);


		window_1 = new ModelMapper(modelDataWrapper);
		window_1.setPos(0, 0, 0);
		window.addChild(window_1);
		window_1.texOffs(88, 0).addBox(-20, 0, 0, 20, 1, 32, 0, false);
		window_1.texOffs(64, 128).addBox(-20, -6, 0, 3, 6, 36, 0, false);
		window_1.texOffs(30, 0).addBox(-3, -35, 0, 3, 0, 48, 0, false);
		window_1.texOffs(0, 112).addBox(-14.369F, -32.0747F, 36, 2, 1, 12, 0, false);

		roof_2_r1 = new ModelMapper(modelDataWrapper);
		roof_2_r1.setPos(-3, -35, 0);
		window_1.addChild(roof_2_r1);
		setRotationAngle(roof_2_r1, 0, 0, -0.5236F);
		roof_2_r1.texOffs(36, 0).addBox(-2, 0, 0, 2, 0, 48, 0, false);

		roof_1_r1 = new ModelMapper(modelDataWrapper);
		roof_1_r1.setPos(-9.771F, -32.5747F, 24);
		window_1.addChild(roof_1_r1);
		setRotationAngle(roof_1_r1, 0, 0, 1.0472F);
		roof_1_r1.texOffs(0, 2).addBox(0, -3, -24, 0, 6, 48, 0, false);

		window_side_pole_r1 = new ModelMapper(modelDataWrapper);
		window_side_pole_r1.setPos(-20, -12, 0);
		window_1.addChild(window_side_pole_r1);
		setRotationAngle(window_side_pole_r1, 0, 0, 0.1571F);
		window_side_pole_r1.texOffs(216, 237).addBox(0, -20, 26, 4, 27, 10, 0, false);
		window_side_pole_r1.texOffs(108, 176).addBox(0, -16, 0, 1, 16, 26, 0, false);

		window_top_r1 = new ModelMapper(modelDataWrapper);
		window_top_r1.setPos(-14.933F, -29.4388F, 0);
		window_1.addChild(window_top_r1);
		setRotationAngle(window_top_r1, 0, 0, 0.8378F);
		window_top_r1.texOffs(0, 147).addBox(-0.5F, -3, 0, 1, 6, 36, 0, false);

		window_2 = new ModelMapper(modelDataWrapper);
		window_2.setPos(0, 0, 0);
		window.addChild(window_2);
		window_2.texOffs(88, 0).addBox(0, 0, 0, 20, 1, 32, 0, true);
		window_2.texOffs(64, 128).addBox(17, -6, 0, 3, 6, 36, 0, true);
		window_2.texOffs(30, 0).addBox(0, -35, 0, 3, 0, 48, 0, true);
		window_2.texOffs(0, 112).addBox(12.369F, -32.0747F, 36, 2, 1, 12, 0, true);

		roof_3_r1 = new ModelMapper(modelDataWrapper);
		roof_3_r1.setPos(3, -35, 0);
		window_2.addChild(roof_3_r1);
		setRotationAngle(roof_3_r1, 0, 0, 0.5236F);
		roof_3_r1.texOffs(36, 0).addBox(0, 0, 0, 2, 0, 48, 0, true);

		roof_2_r2 = new ModelMapper(modelDataWrapper);
		roof_2_r2.setPos(9.771F, -32.5747F, 24);
		window_2.addChild(roof_2_r2);
		setRotationAngle(roof_2_r2, 0, 0, -1.0472F);
		roof_2_r2.texOffs(0, 2).addBox(0, -3, -24, 0, 6, 48, 0, true);

		window_side_pole_r2 = new ModelMapper(modelDataWrapper);
		window_side_pole_r2.setPos(20, -12, 0);
		window_2.addChild(window_side_pole_r2);
		setRotationAngle(window_side_pole_r2, 0, 0, -0.1571F);
		window_side_pole_r2.texOffs(216, 237).addBox(-4, -20, 26, 4, 27, 10, 0, true);
		window_side_pole_r2.texOffs(108, 176).addBox(-1, -16, 0, 1, 16, 26, 0, true);

		window_top_r2 = new ModelMapper(modelDataWrapper);
		window_top_r2.setPos(14.933F, -29.4388F, 0);
		window_2.addChild(window_top_r2);
		setRotationAngle(window_top_r2, 0, 0, -0.8378F);
		window_top_r2.texOffs(0, 147).addBox(-0.5F, -3, 0, 1, 6, 36, 0, true);

		window_exterior = new ModelMapper(modelDataWrapper);
		window_exterior.setPos(0, 24, 0);


		window_exterior_1 = new ModelMapper(modelDataWrapper);
		window_exterior_1.setPos(0, 0, 0);
		window_exterior.addChild(window_exterior_1);
		window_exterior_1.texOffs(42, 176).addBox(-21, 0, 0, 1, 4, 32, 0, false);
		window_exterior_1.texOffs(100, 41).addBox(-20, -12, 0, 0, 12, 36, 0, false);
		window_exterior_1.texOffs(22, 0).addBox(-4, -36, 0, 4, 0, 48, 0, false);

		roof_2_r3 = new ModelMapper(modelDataWrapper);
		roof_2_r3.setPos(-4, -36, 0);
		window_exterior_1.addChild(roof_2_r3);
		setRotationAngle(roof_2_r3, 0, 0, -0.1745F);
		roof_2_r3.texOffs(12, 0).addBox(-5, 0, 0, 5, 0, 48, 0, false);

		roof_1_r2 = new ModelMapper(modelDataWrapper);
		roof_1_r2.setPos(-11.0223F, -32.7667F, 18);
		window_exterior_1.addChild(roof_1_r2);
		setRotationAngle(roof_1_r2, 0, 0, -0.5236F);
		roof_1_r2.texOffs(0, 0).addBox(-3, -1, -18, 6, 2, 48, 0, false);

		window_top_r3 = new ModelMapper(modelDataWrapper);
		window_top_r3.setPos(-14.933F, -29.4388F, 0);
		window_exterior_1.addChild(window_top_r3);
		setRotationAngle(window_top_r3, 0, 0, 0.8378F);
		window_top_r3.texOffs(110, 134).addBox(-0.5F, -3, 0, 0, 6, 36, 0, false);

		window_middle_r1 = new ModelMapper(modelDataWrapper);
		window_middle_r1.setPos(-20, -12, 0);
		window_exterior_1.addChild(window_middle_r1);
		setRotationAngle(window_middle_r1, 0, 0, 0.1571F);
		window_middle_r1.texOffs(0, 95).addBox(0, -16, 0, 0, 16, 36, 0, false);

		window_exterior_2 = new ModelMapper(modelDataWrapper);
		window_exterior_2.setPos(0, 0, 0);
		window_exterior.addChild(window_exterior_2);
		window_exterior_2.texOffs(42, 176).addBox(20, 0, 0, 1, 4, 32, 0, true);
		window_exterior_2.texOffs(100, 41).addBox(20, -12, 0, 0, 12, 36, 0, true);
		window_exterior_2.texOffs(22, 0).addBox(0, -36, 0, 4, 0, 48, 0, true);

		roof_3_r2 = new ModelMapper(modelDataWrapper);
		roof_3_r2.setPos(4, -36, 0);
		window_exterior_2.addChild(roof_3_r2);
		setRotationAngle(roof_3_r2, 0, 0, 0.1745F);
		roof_3_r2.texOffs(12, 0).addBox(0, 0, 0, 5, 0, 48, 0, true);

		roof_2_r4 = new ModelMapper(modelDataWrapper);
		roof_2_r4.setPos(11.0223F, -32.7667F, 18);
		window_exterior_2.addChild(roof_2_r4);
		setRotationAngle(roof_2_r4, 0, 0, 0.5236F);
		roof_2_r4.texOffs(0, 0).addBox(-3, -1, -18, 6, 2, 48, 0, true);

		window_top_r4 = new ModelMapper(modelDataWrapper);
		window_top_r4.setPos(14.933F, -29.4388F, 0);
		window_exterior_2.addChild(window_top_r4);
		setRotationAngle(window_top_r4, 0, 0, -0.8378F);
		window_top_r4.texOffs(110, 134).addBox(0.5F, -3, 0, 0, 6, 36, 0, true);

		window_middle_r2 = new ModelMapper(modelDataWrapper);
		window_middle_r2.setPos(20, -12, 0);
		window_exterior_2.addChild(window_middle_r2);
		setRotationAngle(window_middle_r2, 0, 0, -0.1571F);
		window_middle_r2.texOffs(0, 95).addBox(0, -16, 0, 0, 16, 36, 0, true);

		door_left = new ModelMapper(modelDataWrapper);
		door_left.setPos(0, 24, 0);
		door_left.texOffs(0, 189).addBox(-20, 0, 0, 20, 1, 16, 0, false);

		door_left_part = new ModelMapper(modelDataWrapper);
		door_left_part.setPos(0, 0, 0);
		door_left.addChild(door_left_part);
		door_left_part.texOffs(70, 196).addBox(-19.8F, -12, 0, 0, 12, 22, 0, false);

		door_left_top_r1 = new ModelMapper(modelDataWrapper);
		door_left_top_r1.setPos(-15.6548F, -29.9327F, 11);
		door_left_part.addChild(door_left_top_r1);
		setRotationAngle(door_left_top_r1, 0, 0, 0.8378F);
		door_left_top_r1.texOffs(100, 67).addBox(0.5F, -3, -11, 0, 6, 22, 0, false);

		door_left_middle_r1 = new ModelMapper(modelDataWrapper);
		door_left_middle_r1.setPos(-20.8F, -12, 0);
		door_left_part.addChild(door_left_middle_r1);
		setRotationAngle(door_left_middle_r1, 0, 0, 0.1571F);
		door_left_middle_r1.texOffs(136, 162).addBox(1, -17, 0, 0, 17, 22, 0, false);

		door_right = new ModelMapper(modelDataWrapper);
		door_right.setPos(0, 24, 0);
		door_right.texOffs(160, 0).addBox(-20, 0, -16, 20, 1, 16, 0, false);

		door_right_part = new ModelMapper(modelDataWrapper);
		door_right_part.setPos(0, 0, 0);
		door_right.addChild(door_right_part);
		door_right_part.texOffs(24, 190).addBox(-19.8F, -12, -22, 0, 12, 22, 0, false);

		door_right_top_r1 = new ModelMapper(modelDataWrapper);
		door_right_top_r1.setPos(-15.6548F, -29.9327F, -11);
		door_right_part.addChild(door_right_top_r1);
		setRotationAngle(door_right_top_r1, 0, 0, 0.8378F);
		door_right_top_r1.texOffs(88, 17).addBox(0.5F, -3, -11, 0, 6, 22, 0, false);

		door_right_middle_r1 = new ModelMapper(modelDataWrapper);
		door_right_middle_r1.setPos(-20.8F, -12, 0);
		door_right_part.addChild(door_right_middle_r1);
		setRotationAngle(door_right_middle_r1, 0, 0, 0.1571F);
		door_right_middle_r1.texOffs(76, 154).addBox(1, -17, -22, 0, 17, 22, 0, false);

		door_left_exterior = new ModelMapper(modelDataWrapper);
		door_left_exterior.setPos(0, 24, 0);
		door_left_exterior.texOffs(238, 86).addBox(-21, 0, 0, 1, 4, 16, 0, false);

		door_left_exterior_part = new ModelMapper(modelDataWrapper);
		door_left_exterior_part.setPos(0, 0, 0);
		door_left_exterior.addChild(door_left_exterior_part);
		door_left_exterior_part.texOffs(220, 169).addBox(-20.8F, -12, 0, 1, 12, 22, 0, false);

		door_left_top_r2 = new ModelMapper(modelDataWrapper);
		door_left_top_r2.setPos(-15.6548F, -29.9327F, 11);
		door_left_exterior_part.addChild(door_left_top_r2);
		setRotationAngle(door_left_top_r2, 0, 0, 0.8378F);
		door_left_top_r2.texOffs(223, 209).addBox(-0.5F, -3, -11, 1, 6, 22, 0, false);

		door_left_middle_r2 = new ModelMapper(modelDataWrapper);
		door_left_middle_r2.setPos(-20.8F, -12, 0);
		door_left_exterior_part.addChild(door_left_middle_r2);
		setRotationAngle(door_left_middle_r2, 0, 0, 0.1571F);
		door_left_middle_r2.texOffs(46, 212).addBox(0, -17, 0, 1, 17, 22, 0, false);

		door_right_exterior = new ModelMapper(modelDataWrapper);
		door_right_exterior.setPos(0, 24, 0);
		door_right_exterior.texOffs(236, 0).addBox(-21, 0, -16, 1, 4, 16, 0, false);

		door_right_exterior_part = new ModelMapper(modelDataWrapper);
		door_right_exterior_part.setPos(0, 0, 0);
		door_right_exterior.addChild(door_right_exterior_part);
		door_right_exterior_part.texOffs(138, 218).addBox(-20.8F, -12, -22, 1, 12, 22, 0, false);

		door_right_top_r2 = new ModelMapper(modelDataWrapper);
		door_right_top_r2.setPos(-15.6548F, -29.9327F, -11);
		door_right_exterior_part.addChild(door_right_top_r2);
		setRotationAngle(door_right_top_r2, 0, 0, 0.8378F);
		door_right_top_r2.texOffs(222, 58).addBox(-0.5F, -3, -11, 1, 6, 22, 0, false);

		door_right_middle_r2 = new ModelMapper(modelDataWrapper);
		door_right_middle_r2.setPos(-20.8F, -12, 0);
		door_right_exterior_part.addChild(door_right_middle_r2);
		setRotationAngle(door_right_middle_r2, 0, 0, 0.1571F);
		door_right_middle_r2.texOffs(0, 206).addBox(0, -17, -22, 1, 17, 22, 0, false);

		seat = new ModelMapper(modelDataWrapper);
		seat.setPos(0, 24, 0);


		seat_1 = new ModelMapper(modelDataWrapper);
		seat_1.setPos(0, 0, 0);
		seat.addChild(seat_1);
		seat_1.texOffs(110, 100).addBox(-17, -6, 0, 10, 6, 32, 0, false);

		handrail_3_r1 = new ModelMapper(modelDataWrapper);
		handrail_3_r1.setPos(-7, -4, 0);
		seat_1.addChild(handrail_3_r1);
		setRotationAngle(handrail_3_r1, -1.5708F, 0, -0.0349F);
		handrail_3_r1.texOffs(0, 0).addBox(0, -31.5F, -28, 0, 32, 0, 0.2F, false);

		handrail_2_r1 = new ModelMapper(modelDataWrapper);
		handrail_2_r1.setPos(-7, -4, 0);
		seat_1.addChild(handrail_2_r1);
		setRotationAngle(handrail_2_r1, 0, 0, -0.0349F);
		handrail_2_r1.texOffs(0, 0).addBox(0, -30, 31.5F, 0, 30, 0, 0.2F, false);
		handrail_2_r1.texOffs(0, 0).addBox(0, -30, 13.5F, 0, 30, 0, 0.2F, false);

		seat_back_r1 = new ModelMapper(modelDataWrapper);
		seat_back_r1.setPos(-14, -6, 0);
		seat_1.addChild(seat_back_r1);
		setRotationAngle(seat_back_r1, 0, 0, -0.1745F);
		seat_back_r1.texOffs(164, 144).addBox(-4, -8, 0, 4, 8, 32, 0, false);

		seat_2 = new ModelMapper(modelDataWrapper);
		seat_2.setPos(0, 0, 0);
		seat.addChild(seat_2);
		seat_2.texOffs(110, 100).addBox(7, -6, 0, 10, 6, 32, 0, true);

		handrail_4_r1 = new ModelMapper(modelDataWrapper);
		handrail_4_r1.setPos(7, -4, 0);
		seat_2.addChild(handrail_4_r1);
		setRotationAngle(handrail_4_r1, -1.5708F, 0, 0.0349F);
		handrail_4_r1.texOffs(0, 0).addBox(0, -31.5F, -28, 0, 32, 0, 0.2F, true);

		handrail_3_r2 = new ModelMapper(modelDataWrapper);
		handrail_3_r2.setPos(7, -4, 0);
		seat_2.addChild(handrail_3_r2);
		setRotationAngle(handrail_3_r2, 0, 0, 0.0349F);
		handrail_3_r2.texOffs(0, 0).addBox(0, -30, 31.5F, 0, 30, 0, 0.2F, true);
		handrail_3_r2.texOffs(0, 0).addBox(0, -30, 13.5F, 0, 30, 0, 0.2F, true);

		seat_back_r2 = new ModelMapper(modelDataWrapper);
		seat_back_r2.setPos(14, -6, 0);
		seat_2.addChild(seat_back_r2);
		setRotationAngle(seat_back_r2, 0, 0, 0.1745F);
		seat_back_r2.texOffs(164, 144).addBox(0, -8, 0, 4, 8, 32, 0, true);

		seat_wheelchair = new ModelMapper(modelDataWrapper);
		seat_wheelchair.setPos(0, 24, 0);


		seat_wheelchair_1 = new ModelMapper(modelDataWrapper);
		seat_wheelchair_1.setPos(-7, -4, 0);
		seat_wheelchair.addChild(seat_wheelchair_1);
		seat_wheelchair_1.texOffs(228, 116).addBox(-10, -2, 0, 10, 6, 14, 0, false);
		seat_wheelchair_1.texOffs(222, 27).addBox(-12, -9, 14, 5, 13, 18, 0, false);

		handrail_4_r2 = new ModelMapper(modelDataWrapper);
		handrail_4_r2.setPos(0, 0, 0);
		seat_wheelchair_1.addChild(handrail_4_r2);
		setRotationAngle(handrail_4_r2, -1.5708F, 0, -0.0349F);
		handrail_4_r2.texOffs(0, 0).addBox(0, -13.5F, -28, 0, 14, 0, 0.2F, false);

		handrail_2_r2 = new ModelMapper(modelDataWrapper);
		handrail_2_r2.setPos(0, 0, 0);
		seat_wheelchair_1.addChild(handrail_2_r2);
		setRotationAngle(handrail_2_r2, 0, 0, -0.0349F);
		handrail_2_r2.texOffs(0, 0).addBox(0, -30, 13.5F, 0, 30, 0, 0.2F, false);

		seat_back_r3 = new ModelMapper(modelDataWrapper);
		seat_back_r3.setPos(-7, -2, 0);
		seat_wheelchair_1.addChild(seat_back_r3);
		setRotationAngle(seat_back_r3, 0, 0, -0.1745F);
		seat_back_r3.texOffs(116, 218).addBox(-4, -8, 0, 4, 8, 14, 0, false);

		seat_wheelchair_2 = new ModelMapper(modelDataWrapper);
		seat_wheelchair_2.setPos(7, -4, 0);
		seat_wheelchair.addChild(seat_wheelchair_2);
		seat_wheelchair_2.texOffs(228, 116).addBox(0, -2, 0, 10, 6, 14, 0, true);
		seat_wheelchair_2.texOffs(222, 27).addBox(7, -9, 14, 5, 13, 18, 0, true);

		handrail_5_r1 = new ModelMapper(modelDataWrapper);
		handrail_5_r1.setPos(0, 0, 0);
		seat_wheelchair_2.addChild(handrail_5_r1);
		setRotationAngle(handrail_5_r1, -1.5708F, 0, 0.0349F);
		handrail_5_r1.texOffs(0, 0).addBox(0, -13.5F, -28, 0, 14, 0, 0.2F, true);

		handrail_3_r3 = new ModelMapper(modelDataWrapper);
		handrail_3_r3.setPos(0, 0, 0);
		seat_wheelchair_2.addChild(handrail_3_r3);
		setRotationAngle(handrail_3_r3, 0, 0, 0.0349F);
		handrail_3_r3.texOffs(0, 0).addBox(0, -30, 13.5F, 0, 30, 0, 0.2F, true);

		seat_back_r4 = new ModelMapper(modelDataWrapper);
		seat_back_r4.setPos(7, -2, 0);
		seat_wheelchair_2.addChild(seat_back_r4);
		setRotationAngle(seat_back_r4, 0, 0, 0.1745F);
		seat_back_r4.texOffs(116, 218).addBox(0, -8, 0, 4, 8, 14, 0, true);

		window_light = new ModelMapper(modelDataWrapper);
		window_light.setPos(0, 24, 0);


		light_2_r1 = new ModelMapper(modelDataWrapper);
		light_2_r1.setPos(6.1463F, -34, 0);
		window_light.addChild(light_2_r1);
		setRotationAngle(light_2_r1, 0, 0, 0.7854F);
		light_2_r1.texOffs(48, 50).addBox(-1, -1, 0, 2, 2, 48, 0, true);

		light_1_r1 = new ModelMapper(modelDataWrapper);
		light_1_r1.setPos(-6.1463F, -34, 0);
		window_light.addChild(light_1_r1);
		setRotationAngle(light_1_r1, 0, 0, -0.7854F);
		light_1_r1.texOffs(48, 50).addBox(-1, -1, 0, 2, 2, 48, 0, false);

		window_light_end = new ModelMapper(modelDataWrapper);
		window_light_end.setPos(0, 24, 0);


		light_3_r1 = new ModelMapper(modelDataWrapper);
		light_3_r1.setPos(6.1463F, -34, 48);
		window_light_end.addChild(light_3_r1);
		setRotationAngle(light_3_r1, 0, 0, 0.7854F);
		light_3_r1.texOffs(88, 90).addBox(-1, -1, 0, 2, 2, 8, 0, true);

		light_2_r2 = new ModelMapper(modelDataWrapper);
		light_2_r2.setPos(-6.1463F, -34, 48);
		window_light_end.addChild(light_2_r2);
		setRotationAngle(light_2_r2, 0, 0, -0.7854F);
		light_2_r2.texOffs(88, 90).addBox(-1, -1, 0, 2, 2, 8, 0, false);

		window_light_head = new ModelMapper(modelDataWrapper);
		window_light_head.setPos(0, 24, 0);


		light_2_r3 = new ModelMapper(modelDataWrapper);
		light_2_r3.setPos(6.1463F, -34, 0);
		window_light_head.addChild(light_2_r3);
		setRotationAngle(light_2_r3, 0, 0, 0.7854F);
		light_2_r3.texOffs(71, 73).addBox(-1, -1, 0, 2, 2, 25, 0, true);

		light_1_r2 = new ModelMapper(modelDataWrapper);
		light_1_r2.setPos(-6.1463F, -34, 0);
		window_light_head.addChild(light_1_r2);
		setRotationAngle(light_1_r2, 0, 0, -0.7854F);
		light_1_r2.texOffs(71, 73).addBox(-1, -1, 0, 2, 2, 25, 0, false);

		side_panel = new ModelMapper(modelDataWrapper);
		side_panel.setPos(0, 24, 0);


		side_panel_r1 = new ModelMapper(modelDataWrapper);
		side_panel_r1.setPos(-8, -6, 0);
		side_panel.addChild(side_panel_r1);
		setRotationAngle(side_panel_r1, 0, 0, -0.0349F);
		side_panel_r1.texOffs(168, 252).addBox(-8, -28, 0, 8, 28, 0, 0, false);

		end = new ModelMapper(modelDataWrapper);
		end.setPos(0, 24, 0);
		end.texOffs(244, 237).addBox(-5, -36, 56, 10, 36, 0, 0, false);

		end_1 = new ModelMapper(modelDataWrapper);
		end_1.setPos(0, 0, 0);
		end.addChild(end_1);
		end_1.texOffs(122, 154).addBox(-20, 0, 48, 20, 1, 9, 0, false);
		end_1.texOffs(106, 132).addBox(-5.5F, -14, 50, 0, 14, 6, 0, false);
		end_1.texOffs(171, 138).addBox(-20, -12, 48, 1, 12, 6, 0, false);
		end_1.texOffs(8, 17).addBox(-3, -35, 48, 3, 0, 8, 0, false);

		roof_2_r5 = new ModelMapper(modelDataWrapper);
		roof_2_r5.setPos(-3, -35, 49);
		end_1.addChild(roof_2_r5);
		setRotationAngle(roof_2_r5, 0, 0, -0.5236F);
		roof_2_r5.texOffs(27, 0).addBox(-2, 0, -1, 2, 0, 8, 0, false);

		roof_1_r3 = new ModelMapper(modelDataWrapper);
		roof_1_r3.setPos(-17.8152F, -28.5077F, 49);
		end_1.addChild(roof_1_r3);
		setRotationAngle(roof_1_r3, 0, 0, 1.0472F);
		roof_1_r3.texOffs(24, 26).addBox(0.5F, -12, -1, 0, 6, 8, 0, false);

		window_top_r5 = new ModelMapper(modelDataWrapper);
		window_top_r5.setPos(-14.933F, -29.4388F, 49);
		end_1.addChild(window_top_r5);
		setRotationAngle(window_top_r5, 0, 0, 0.8378F);
		window_top_r5.texOffs(38, 156).addBox(-0.5F, -3, -1, 1, 6, 7, 0, false);

		window_middle_r3 = new ModelMapper(modelDataWrapper);
		window_middle_r3.setPos(-20, -12, 49);
		end_1.addChild(window_middle_r3);
		setRotationAngle(window_middle_r3, 0, 0, 0.1571F);
		window_middle_r3.texOffs(118, 45).addBox(0, -16, -1, 1, 16, 6, 0, false);

		front_box_top_r1 = new ModelMapper(modelDataWrapper);
		front_box_top_r1.setPos(-5.5F, -14, 56);
		end_1.addChild(front_box_top_r1);
		setRotationAngle(front_box_top_r1, 0.1745F, -0.1745F, 0);
		front_box_top_r1.texOffs(82, 22).addBox(-15, 0, -6, 15, 0, 6, 0, false);

		front_box_r1 = new ModelMapper(modelDataWrapper);
		front_box_r1.setPos(-5.5F, 0, 56);
		end_1.addChild(front_box_r1);
		setRotationAngle(front_box_r1, 0, -0.1745F, 0);
		front_box_r1.texOffs(0, 98).addBox(-15, -14, -5, 15, 14, 0, 0, false);

		front_side_r1 = new ModelMapper(modelDataWrapper);
		front_side_r1.setPos(-5, -36, 56);
		end_1.addChild(front_side_r1);
		setRotationAngle(front_side_r1, 0, -0.1745F, 0);
		front_side_r1.texOffs(0, 56).addBox(-16, 0, 0, 16, 36, 0, 0, false);

		end_2 = new ModelMapper(modelDataWrapper);
		end_2.setPos(0, 0, 0);
		end.addChild(end_2);
		end_2.texOffs(122, 154).addBox(0, 0, 48, 20, 1, 9, 0, true);
		end_2.texOffs(106, 132).addBox(5.5F, -14, 50, 0, 14, 6, 0, true);
		end_2.texOffs(171, 138).addBox(19, -12, 48, 1, 12, 6, 0, true);
		end_2.texOffs(8, 17).addBox(0, -35, 48, 3, 0, 8, 0, true);

		roof_3_r3 = new ModelMapper(modelDataWrapper);
		roof_3_r3.setPos(3, -35, 49);
		end_2.addChild(roof_3_r3);
		setRotationAngle(roof_3_r3, 0, 0, 0.5236F);
		roof_3_r3.texOffs(27, 0).addBox(0, 0, -1, 2, 0, 8, 0, true);

		roof_2_r6 = new ModelMapper(modelDataWrapper);
		roof_2_r6.setPos(17.8152F, -28.5077F, 49);
		end_2.addChild(roof_2_r6);
		setRotationAngle(roof_2_r6, 0, 0, -1.0472F);
		roof_2_r6.texOffs(24, 26).addBox(-0.5F, -12, -1, 0, 6, 8, 0, true);

		window_top_r6 = new ModelMapper(modelDataWrapper);
		window_top_r6.setPos(14.933F, -29.4388F, 49);
		end_2.addChild(window_top_r6);
		setRotationAngle(window_top_r6, 0, 0, -0.8378F);
		window_top_r6.texOffs(38, 156).addBox(-0.5F, -3, -1, 1, 6, 7, 0, true);

		window_middle_r4 = new ModelMapper(modelDataWrapper);
		window_middle_r4.setPos(20, -12, 49);
		end_2.addChild(window_middle_r4);
		setRotationAngle(window_middle_r4, 0, 0, -0.1571F);
		window_middle_r4.texOffs(118, 45).addBox(-1, -16, -1, 1, 16, 6, 0, true);

		front_box_top_r2 = new ModelMapper(modelDataWrapper);
		front_box_top_r2.setPos(5.5F, -14, 56);
		end_2.addChild(front_box_top_r2);
		setRotationAngle(front_box_top_r2, 0.1745F, 0.1745F, 0);
		front_box_top_r2.texOffs(82, 22).addBox(0, 0, -6, 15, 0, 6, 0, true);

		front_box_r2 = new ModelMapper(modelDataWrapper);
		front_box_r2.setPos(5.5F, 0, 56);
		end_2.addChild(front_box_r2);
		setRotationAngle(front_box_r2, 0, 0.1745F, 0);
		front_box_r2.texOffs(0, 98).addBox(0, -14, -5, 15, 14, 0, 0, true);

		front_side_r2 = new ModelMapper(modelDataWrapper);
		front_side_r2.setPos(5, -36, 56);
		end_2.addChild(front_side_r2);
		setRotationAngle(front_side_r2, 0, 0.1745F, 0);
		front_side_r2.texOffs(0, 56).addBox(0, 0, 0, 16, 36, 0, 0, true);

		end_exterior = new ModelMapper(modelDataWrapper);
		end_exterior.setPos(0, 24, 0);
		end_exterior.texOffs(0, 245).addBox(-5, -36, 57, 10, 36, 0, 0, false);
		end_exterior.texOffs(38, 147).addBox(-5, 0, 52, 10, 4, 5, 0, false);

		end_exterior_1 = new ModelMapper(modelDataWrapper);
		end_exterior_1.setPos(0, 0, 0);
		end_exterior.addChild(end_exterior_1);
		end_exterior_1.texOffs(100, 51).addBox(-21, 0, 32, 1, 4, 16, 0, false);
		end_exterior_1.texOffs(23, 56).addBox(-4, -36, 48, 4, 0, 9, 0, false);

		front_side_bottom_r1 = new ModelMapper(modelDataWrapper);
		front_side_bottom_r1.setPos(-5, -36, 57);
		end_exterior_1.addChild(front_side_bottom_r1);
		setRotationAngle(front_side_bottom_r1, 0, -0.1745F, 0);
		front_side_bottom_r1.texOffs(160, 17).addBox(-16, 36, -5, 16, 4, 5, 0, false);
		front_side_bottom_r1.texOffs(62, 56).addBox(-16, 0, 0, 16, 36, 0, 0, false);

		front_side_6_r1 = new ModelMapper(modelDataWrapper);
		front_side_6_r1.setPos(-4, -36, 40);
		end_exterior_1.addChild(front_side_6_r1);
		setRotationAngle(front_side_6_r1, 0, 0, -0.1745F);
		front_side_6_r1.texOffs(0, 28).addBox(-5, 0, 8, 5, 0, 9, 0, false);

		front_side_5_r1 = new ModelMapper(modelDataWrapper);
		front_side_5_r1.setPos(-11.0218F, -32.7659F, 58);
		end_exterior_1.addChild(front_side_5_r1);
		setRotationAngle(front_side_5_r1, 0, 0, -0.5236F);
		front_side_5_r1.texOffs(228, 136).addBox(-4, -1, -10, 7, 1, 9, 0, false);

		front_side_4_r1 = new ModelMapper(modelDataWrapper);
		front_side_4_r1.setPos(-15.8157F, -30.1796F, 11);
		end_exterior_1.addChild(front_side_4_r1);
		setRotationAngle(front_side_4_r1, 0, 0, 0.8378F);
		front_side_4_r1.texOffs(110, 144).addBox(-0.5F, -2, 37, 1, 5, 8, 0, false);

		front_side_3_r1 = new ModelMapper(modelDataWrapper);
		front_side_3_r1.setPos(-21, -12, 48);
		end_exterior_1.addChild(front_side_3_r1);
		setRotationAngle(front_side_3_r1, 0, 0.0873F, 0.1571F);
		front_side_3_r1.texOffs(247, 203).addBox(0, -19, 0, 1, 19, 7, 0, false);

		front_side_2_r1 = new ModelMapper(modelDataWrapper);
		front_side_2_r1.setPos(-21, 4, 48);
		end_exterior_1.addChild(front_side_2_r1);
		setRotationAngle(front_side_2_r1, 0, 0.0873F, 0);
		front_side_2_r1.texOffs(74, 131).addBox(0, -16, 0, 1, 16, 7, 0, false);

		end_exterior_2 = new ModelMapper(modelDataWrapper);
		end_exterior_2.setPos(0, 0, 0);
		end_exterior.addChild(end_exterior_2);
		end_exterior_2.texOffs(100, 51).addBox(20, 0, 32, 1, 4, 16, 0, true);
		end_exterior_2.texOffs(23, 56).addBox(0, -36, 48, 4, 0, 9, 0, true);

		front_side_bottom_r2 = new ModelMapper(modelDataWrapper);
		front_side_bottom_r2.setPos(5, -36, 57);
		end_exterior_2.addChild(front_side_bottom_r2);
		setRotationAngle(front_side_bottom_r2, 0, 0.1745F, 0);
		front_side_bottom_r2.texOffs(160, 17).addBox(0, 36, -5, 16, 4, 5, 0, true);
		front_side_bottom_r2.texOffs(62, 56).addBox(0, 0, 0, 16, 36, 0, 0, true);

		front_side_7_r1 = new ModelMapper(modelDataWrapper);
		front_side_7_r1.setPos(4, -36, 40);
		end_exterior_2.addChild(front_side_7_r1);
		setRotationAngle(front_side_7_r1, 0, 0, 0.1745F);
		front_side_7_r1.texOffs(0, 28).addBox(0, 0, 8, 5, 0, 9, 0, true);

		front_side_6_r2 = new ModelMapper(modelDataWrapper);
		front_side_6_r2.setPos(11.0218F, -32.7659F, 58);
		end_exterior_2.addChild(front_side_6_r2);
		setRotationAngle(front_side_6_r2, 0, 0, 0.5236F);
		front_side_6_r2.texOffs(228, 136).addBox(-3, -1, -10, 7, 1, 9, 0, true);

		front_side_5_r2 = new ModelMapper(modelDataWrapper);
		front_side_5_r2.setPos(15.8157F, -30.1796F, 11);
		end_exterior_2.addChild(front_side_5_r2);
		setRotationAngle(front_side_5_r2, 0, 0, -0.8378F);
		front_side_5_r2.texOffs(110, 144).addBox(-0.5F, -2, 37, 1, 5, 8, 0, true);

		front_side_4_r2 = new ModelMapper(modelDataWrapper);
		front_side_4_r2.setPos(21, -12, 48);
		end_exterior_2.addChild(front_side_4_r2);
		setRotationAngle(front_side_4_r2, 0, -0.0873F, -0.1571F);
		front_side_4_r2.texOffs(247, 203).addBox(-1, -19, 0, 1, 19, 7, 0, true);

		front_side_3_r2 = new ModelMapper(modelDataWrapper);
		front_side_3_r2.setPos(21, 4, 48);
		end_exterior_2.addChild(front_side_3_r2);
		setRotationAngle(front_side_3_r2, 0, -0.0873F, 0);
		front_side_3_r2.texOffs(74, 131).addBox(-1, -16, 0, 1, 16, 7, 0, true);

		head = new ModelMapper(modelDataWrapper);
		head.setPos(0, 24, 0);
		head.texOffs(162, 95).addBox(-19, -35, 25, 38, 35, 0, 0, false);

		head_1 = new ModelMapper(modelDataWrapper);
		head_1.setPos(0, 0, 0);
		head.addChild(head_1);
		head_1.texOffs(157, 69).addBox(-20, 0, 0, 20, 1, 25, 0, false);
		head_1.texOffs(189, 200).addBox(-20, -6, 0, 3, 6, 25, 0, false);
		head_1.texOffs(0, 0).addBox(-3, -35, 0, 3, 0, 25, 0, false);

		roof_2_r7 = new ModelMapper(modelDataWrapper);
		roof_2_r7.setPos(-3, -35, 0);
		head_1.addChild(roof_2_r7);
		setRotationAngle(roof_2_r7, 0, 0, -0.5236F);
		roof_2_r7.texOffs(6, 0).addBox(-2, 0, 0, 2, 0, 25, 0, false);

		roof_1_r4 = new ModelMapper(modelDataWrapper);
		roof_1_r4.setPos(-17.8152F, -28.5077F, 0);
		head_1.addChild(roof_1_r4);
		setRotationAngle(roof_1_r4, 0, 0, 1.0472F);
		roof_1_r4.texOffs(88, 8).addBox(0.5F, -12, 0, 0, 6, 25, 0, false);

		window_top_r7 = new ModelMapper(modelDataWrapper);
		window_top_r7.setPos(-14.933F, -29.4388F, 0);
		head_1.addChild(window_top_r7);
		setRotationAngle(window_top_r7, 0, 0, 0.8378F);
		window_top_r7.texOffs(47, 131).addBox(-0.5F, -3, 0, 1, 6, 25, 0, false);

		window_middle_r5 = new ModelMapper(modelDataWrapper);
		window_middle_r5.setPos(-20, -12, 0);
		head_1.addChild(window_middle_r5);
		setRotationAngle(window_middle_r5, 0, 0, 0.1571F);
		window_middle_r5.texOffs(162, 184).addBox(0, -16, 0, 1, 16, 25, 0, false);

		head_2 = new ModelMapper(modelDataWrapper);
		head_2.setPos(0, 0, 0);
		head.addChild(head_2);
		head_2.texOffs(157, 69).addBox(0, 0, 0, 20, 1, 25, 0, true);
		head_2.texOffs(189, 200).addBox(17, -6, 0, 3, 6, 25, 0, true);
		head_2.texOffs(0, 0).addBox(0, -35, 0, 3, 0, 25, 0, true);

		roof_3_r4 = new ModelMapper(modelDataWrapper);
		roof_3_r4.setPos(3, -35, 0);
		head_2.addChild(roof_3_r4);
		setRotationAngle(roof_3_r4, 0, 0, 0.5236F);
		roof_3_r4.texOffs(6, 0).addBox(0, 0, 0, 2, 0, 25, 0, true);

		roof_2_r8 = new ModelMapper(modelDataWrapper);
		roof_2_r8.setPos(17.8152F, -28.5077F, 0);
		head_2.addChild(roof_2_r8);
		setRotationAngle(roof_2_r8, 0, 0, -1.0472F);
		roof_2_r8.texOffs(88, 8).addBox(-0.5F, -12, 0, 0, 6, 25, 0, true);

		window_top_r8 = new ModelMapper(modelDataWrapper);
		window_top_r8.setPos(14.933F, -29.4388F, 0);
		head_2.addChild(window_top_r8);
		setRotationAngle(window_top_r8, 0, 0, -0.8378F);
		window_top_r8.texOffs(47, 131).addBox(-0.5F, -3, 0, 1, 6, 25, 0, true);

		window_middle_r6 = new ModelMapper(modelDataWrapper);
		window_middle_r6.setPos(20, -12, 0);
		head_2.addChild(window_middle_r6);
		setRotationAngle(window_middle_r6, 0, 0, -0.1571F);
		window_middle_r6.texOffs(162, 184).addBox(-1, -16, 0, 1, 16, 25, 0, true);

		head_exterior = new ModelMapper(modelDataWrapper);
		head_exterior.setPos(0, 24, 0);
		head_exterior.texOffs(142, 33).addBox(-20, -36, 26, 40, 36, 0, 0, false);
		head_exterior.texOffs(20, 245).addBox(-5, -36, 57, 10, 36, 0, 0, false);
		head_exterior.texOffs(189, 184).addBox(-5, 0, 52, 10, 4, 5, 0, false);

		small_light_r1 = new ModelMapper(modelDataWrapper);
		small_light_r1.setPos(5, -36, 57);
		head_exterior.addChild(small_light_r1);
		setRotationAngle(small_light_r1, 0, 0.1745F, 0);
		small_light_r1.texOffs(0, 189).addBox(2, 30, 0.05F, 5, 4, 0, 0, true);

		front_side_right_r1 = new ModelMapper(modelDataWrapper);
		front_side_right_r1.setPos(21, -12, 40);
		head_exterior.addChild(front_side_right_r1);
		setRotationAngle(front_side_right_r1, 0, -0.0873F, -0.1571F);
		front_side_right_r1.texOffs(136, 252).addBox(-1, -19, 0, 1, 19, 15, 0, true);

		front_side_left_r1 = new ModelMapper(modelDataWrapper);
		front_side_left_r1.setPos(-21, -12, 40);
		head_exterior.addChild(front_side_left_r1);
		setRotationAngle(front_side_left_r1, 0, 0.0873F, 0.1571F);
		front_side_left_r1.texOffs(184, 231).addBox(0, -19, 0, 1, 19, 15, 0, false);

		head_exterior_1 = new ModelMapper(modelDataWrapper);
		head_exterior_1.setPos(0, 0, 0);
		head_exterior.addChild(head_exterior_1);
		head_exterior_1.texOffs(100, 33).addBox(-21, 0, 0, 1, 4, 40, 0, false);
		head_exterior_1.texOffs(0, 100).addBox(-20, 0, 25, 20, 1, 30, 0, false);
		head_exterior_1.texOffs(70, 80).addBox(-20, -12, 0, 0, 12, 36, 0, false);
		head_exterior_1.texOffs(0, 0).addBox(-4, -36, 0, 4, 0, 40, 0, false);
		head_exterior_1.texOffs(92, 218).addBox(-20.8F, -12, 18, 1, 12, 22, 0, false);
		head_exterior_1.texOffs(0, 0).addBox(-4, -36, 40, 4, 0, 17, 0, false);

		front_side_bottom_r3 = new ModelMapper(modelDataWrapper);
		front_side_bottom_r3.setPos(-5, -36, 57);
		head_exterior_1.addChild(front_side_bottom_r3);
		setRotationAngle(front_side_bottom_r3, 0, -0.1745F, 0);
		front_side_bottom_r3.texOffs(111, 252).addBox(-15, 36, -5, 15, 4, 5, 0, false);
		front_side_bottom_r3.texOffs(0, 147).addBox(-15, 0, 0, 15, 36, 0, 0, false);

		front_side_5_r3 = new ModelMapper(modelDataWrapper);
		front_side_5_r3.setPos(-4, -36, 40);
		head_exterior_1.addChild(front_side_5_r3);
		setRotationAngle(front_side_5_r3, 0, 0, -0.1745F);
		front_side_5_r3.texOffs(83, 50).addBox(-5, 0, 0, 5, 0, 17, 0, false);
		front_side_5_r3.texOffs(12, 56).addBox(-5, 0, -40, 5, 0, 40, 0, false);

		front_side_4_r3 = new ModelMapper(modelDataWrapper);
		front_side_4_r3.setPos(-11.0218F, -32.7659F, 58);
		head_exterior_1.addChild(front_side_4_r3);
		setRotationAngle(front_side_4_r3, 0, 0, -0.5236F);
		front_side_4_r3.texOffs(61, 251).addBox(-4, -1, -18, 7, 1, 17, 0, false);

		front_side_3_r3 = new ModelMapper(modelDataWrapper);
		front_side_3_r3.setPos(-15.8157F, -30.1796F, 11);
		head_exterior_1.addChild(front_side_3_r3);
		setRotationAngle(front_side_3_r3, 0, 0, 0.8378F);
		front_side_3_r3.texOffs(93, 253).addBox(-0.5F, -2, 29, 1, 5, 16, 0, false);

		front_side_1_r1 = new ModelMapper(modelDataWrapper);
		front_side_1_r1.setPos(-21, 4, 40);
		head_exterior_1.addChild(front_side_1_r1);
		setRotationAngle(front_side_1_r1, 0, 0.0873F, 0);
		front_side_1_r1.texOffs(244, 154).addBox(0, -16, 0, 1, 16, 15, 0, false);

		door_right_top_r3 = new ModelMapper(modelDataWrapper);
		door_right_top_r3.setPos(-15.6548F, -29.9327F, 11);
		head_exterior_1.addChild(door_right_top_r3);
		setRotationAngle(door_right_top_r3, 0, 0, 0.8378F);
		door_right_top_r3.texOffs(0, 18).addBox(-0.5F, -3, 7, 1, 6, 22, 0, false);

		door_right_middle_r3 = new ModelMapper(modelDataWrapper);
		door_right_middle_r3.setPos(-20.8F, -12, 22);
		head_exterior_1.addChild(door_right_middle_r3);
		setRotationAngle(door_right_middle_r3, 0, 0, 0.1571F);
		door_right_middle_r3.texOffs(204, 130).addBox(0, -17, -4, 1, 17, 22, 0, false);

		roof_1_r5 = new ModelMapper(modelDataWrapper);
		roof_1_r5.setPos(-11.0223F, -32.7667F, 18);
		head_exterior_1.addChild(roof_1_r5);
		setRotationAngle(roof_1_r5, 0, 0, -0.5236F);
		roof_1_r5.texOffs(0, 56).addBox(-3, -1, -18, 6, 2, 40, 0, false);

		window_top_r9 = new ModelMapper(modelDataWrapper);
		window_top_r9.setPos(-14.933F, -29.4388F, 0);
		head_exterior_1.addChild(window_top_r9);
		setRotationAngle(window_top_r9, 0, 0, 0.8378F);
		window_top_r9.texOffs(38, 134).addBox(-0.5F, -3, 0, 0, 6, 36, 0, false);

		window_middle_r7 = new ModelMapper(modelDataWrapper);
		window_middle_r7.setPos(-20, -12, 0);
		head_exterior_1.addChild(window_middle_r7);
		setRotationAngle(window_middle_r7, 0, 0, 0.1571F);
		window_middle_r7.texOffs(70, 64).addBox(0, -16, 0, 0, 16, 36, 0, false);

		head_exterior_2 = new ModelMapper(modelDataWrapper);
		head_exterior_2.setPos(0, 0, 0);
		head_exterior.addChild(head_exterior_2);
		head_exterior_2.texOffs(100, 33).addBox(20, 0, 0, 1, 4, 40, 0, true);
		head_exterior_2.texOffs(0, 100).addBox(0, 0, 25, 20, 1, 30, 0, true);
		head_exterior_2.texOffs(70, 80).addBox(20, -12, 0, 0, 12, 36, 0, true);
		head_exterior_2.texOffs(0, 0).addBox(0, -36, 0, 4, 0, 40, 0, true);
		head_exterior_2.texOffs(92, 218).addBox(19.8F, -12, 18, 1, 12, 22, 0, true);
		head_exterior_2.texOffs(0, 0).addBox(0, -36, 40, 4, 0, 17, 0, true);

		front_side_bottom_r4 = new ModelMapper(modelDataWrapper);
		front_side_bottom_r4.setPos(5, -36, 57);
		head_exterior_2.addChild(front_side_bottom_r4);
		setRotationAngle(front_side_bottom_r4, 0, 0.1745F, 0);
		front_side_bottom_r4.texOffs(111, 252).addBox(0, 36, -5, 15, 4, 5, 0, true);
		front_side_bottom_r4.texOffs(0, 147).addBox(0, 0, 0, 15, 36, 0, 0, true);

		front_side_6_r3 = new ModelMapper(modelDataWrapper);
		front_side_6_r3.setPos(4, -36, 40);
		head_exterior_2.addChild(front_side_6_r3);
		setRotationAngle(front_side_6_r3, 0, 0, 0.1745F);
		front_side_6_r3.texOffs(83, 50).addBox(0, 0, 0, 5, 0, 17, 0, true);
		front_side_6_r3.texOffs(12, 56).addBox(0, 0, -40, 5, 0, 40, 0, true);

		front_side_5_r4 = new ModelMapper(modelDataWrapper);
		front_side_5_r4.setPos(11.0218F, -32.7659F, 58);
		head_exterior_2.addChild(front_side_5_r4);
		setRotationAngle(front_side_5_r4, 0, 0, 0.5236F);
		front_side_5_r4.texOffs(61, 251).addBox(-3, -1, -18, 7, 1, 17, 0, true);

		front_side_4_r4 = new ModelMapper(modelDataWrapper);
		front_side_4_r4.setPos(15.8157F, -30.1796F, 11);
		head_exterior_2.addChild(front_side_4_r4);
		setRotationAngle(front_side_4_r4, 0, 0, -0.8378F);
		front_side_4_r4.texOffs(93, 253).addBox(-0.5F, -2, 29, 1, 5, 16, 0, true);

		front_side_2_r2 = new ModelMapper(modelDataWrapper);
		front_side_2_r2.setPos(21, 4, 40);
		head_exterior_2.addChild(front_side_2_r2);
		setRotationAngle(front_side_2_r2, 0, -0.0873F, 0);
		front_side_2_r2.texOffs(244, 154).addBox(-1, -16, 0, 1, 16, 15, 0, true);

		door_right_top_r4 = new ModelMapper(modelDataWrapper);
		door_right_top_r4.setPos(15.6548F, -29.9327F, 11);
		head_exterior_2.addChild(door_right_top_r4);
		setRotationAngle(door_right_top_r4, 0, 0, -0.8378F);
		door_right_top_r4.texOffs(0, 18).addBox(-0.5F, -3, 7, 1, 6, 22, 0, true);

		door_right_middle_r4 = new ModelMapper(modelDataWrapper);
		door_right_middle_r4.setPos(20.8F, -12, 22);
		head_exterior_2.addChild(door_right_middle_r4);
		setRotationAngle(door_right_middle_r4, 0, 0, -0.1571F);
		door_right_middle_r4.texOffs(204, 130).addBox(-1, -17, -4, 1, 17, 22, 0, true);

		roof_2_r9 = new ModelMapper(modelDataWrapper);
		roof_2_r9.setPos(11.0223F, -32.7667F, 18);
		head_exterior_2.addChild(roof_2_r9);
		setRotationAngle(roof_2_r9, 0, 0, 0.5236F);
		roof_2_r9.texOffs(0, 56).addBox(-3, -1, -18, 6, 2, 40, 0, true);

		window_top_r10 = new ModelMapper(modelDataWrapper);
		window_top_r10.setPos(14.933F, -29.4388F, 0);
		head_exterior_2.addChild(window_top_r10);
		setRotationAngle(window_top_r10, 0, 0, -0.8378F);
		window_top_r10.texOffs(38, 134).addBox(0.5F, -3, 0, 0, 6, 36, 0, true);

		window_middle_r8 = new ModelMapper(modelDataWrapper);
		window_middle_r8.setPos(20, -12, 0);
		head_exterior_2.addChild(window_middle_r8);
		setRotationAngle(window_middle_r8, 0, 0, -0.1571F);
		window_middle_r8.texOffs(70, 64).addBox(0, -16, 0, 0, 16, 36, 0, true);

		logo = new ModelMapper(modelDataWrapper);
		logo.setPos(0, 24, 0);
		logo.texOffs(202, 9).addBox(-20.1F, -12, -4, 0, 8, 8, 0, false);

		door_light_on = new ModelMapper(modelDataWrapper);
		door_light_on.setPos(0, 24, 0);


		light_r1 = new ModelMapper(modelDataWrapper);
		light_r1.setPos(-14.933F, -29.4388F, 0);
		door_light_on.addChild(light_r1);
		setRotationAngle(light_r1, 0, 0, 0.8378F);
		light_r1.texOffs(3, 0).addBox(-1, -1, -1, 1, 1, 2, 0, false);

		door_light_off = new ModelMapper(modelDataWrapper);
		door_light_off.setPos(0, 24, 0);


		light_r2 = new ModelMapper(modelDataWrapper);
		light_r2.setPos(-14.933F, -29.4388F, 0);
		door_light_off.addChild(light_r2);
		setRotationAngle(light_r2, 0, 0, 0.8378F);
		light_r2.texOffs(3, 3).addBox(-1, -1, -1, 1, 1, 2, 0, false);

		headlights = new ModelMapper(modelDataWrapper);
		headlights.setPos(0, 24, 0);


		light_2_r4 = new ModelMapper(modelDataWrapper);
		light_2_r4.setPos(5, -36, 57);
		headlights.addChild(light_2_r4);
		setRotationAngle(light_2_r4, 0, 0.1745F, 0);
		light_2_r4.texOffs(0, 206).addBox(2, 26, 0.1F, 9, 4, 0, 0, true);

		light_1_r3 = new ModelMapper(modelDataWrapper);
		light_1_r3.setPos(-5, -36, 57);
		headlights.addChild(light_1_r3);
		setRotationAngle(light_1_r3, 0, -0.1745F, 0);
		light_1_r3.texOffs(0, 206).addBox(-11, 26, 0.1F, 9, 4, 0, 0, false);

		tail_lights = new ModelMapper(modelDataWrapper);
		tail_lights.setPos(0, 24, 0);


		light_3_r2 = new ModelMapper(modelDataWrapper);
		light_3_r2.setPos(5, -36, 57);
		tail_lights.addChild(light_3_r2);
		setRotationAngle(light_3_r2, 0, 0.1745F, 0);
		light_3_r2.texOffs(0, 210).addBox(2, 26, 0.1F, 9, 4, 0, 0, true);

		light_2_r5 = new ModelMapper(modelDataWrapper);
		light_2_r5.setPos(-5, -36, 57);
		tail_lights.addChild(light_2_r5);
		setRotationAngle(light_2_r5, 0, -0.1745F, 0);
		light_2_r5.texOffs(0, 210).addBox(-11, 26, 0.1F, 9, 4, 0, 0, false);

		modelDataWrapper.setModelPart(textureWidth, textureHeight);
		window.setModelPart();
		window_exterior.setModelPart();
		door_left.setModelPart();
		door_left_part.setModelPart(door_left.name);
		door_right.setModelPart();
		door_right_part.setModelPart(door_right.name);
		door_left_exterior.setModelPart();
		door_left_exterior_part.setModelPart(door_left_exterior.name);
		door_right_exterior.setModelPart();
		door_right_exterior_part.setModelPart(door_right_exterior.name);
		seat.setModelPart();
		seat_wheelchair.setModelPart();
		window_light.setModelPart();
		window_light_end.setModelPart();
		window_light_head.setModelPart();
		side_panel.setModelPart();
		end.setModelPart();
		end_exterior.setModelPart();
		head.setModelPart();
		head_exterior.setModelPart();
		logo.setModelPart();
		door_light_on.setModelPart();
		door_light_off.setModelPart();
		headlights.setModelPart();
		tail_lights.setModelPart();
	}

	private static final int DOOR_MAX = 12;

	@Override
	public ModelLondonUnderground1995 createNew(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		return new ModelLondonUnderground1995(doorAnimationType, renderDoorOverlay);
	}

	@Override
	protected void renderWindowPositions(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
		final boolean isEnd1 = isIndex(0, position, getWindowPositions());
		final boolean isEnd2 = isIndex(-1, position, getWindowPositions());
		final boolean useHead1 = isEnd1Head && isEnd1;
		final boolean useHead2 = isEnd2Head && isEnd2;

		switch (renderStage) {
			case LIGHTS:
				if (useHead1) {
					renderOnceFlipped(window_light_head, matrices, vertices, light, position);
					renderOnce(window_light, matrices, vertices, light, position);
				} else if (useHead2) {
					renderOnce(window_light_head, matrices, vertices, light, position);
					renderOnceFlipped(window_light, matrices, vertices, light, position);
				} else {
					renderMirror(window_light, matrices, vertices, light, position);
				}
				break;
			case INTERIOR:
				if (useHead1) {
					renderOnceFlipped(head, matrices, vertices, light, position);
					renderOnce(window, matrices, vertices, light, position);
				} else if (useHead2) {
					renderOnce(head, matrices, vertices, light, position);
					renderOnceFlipped(window, matrices, vertices, light, position);
				} else {
					renderMirror(window, matrices, vertices, light, position);
				}
				if (renderDetails) {
					renderOnceFlipped(isEnd1 || isEnd2 ? seat : seat_wheelchair, matrices, vertices, light, position + (useHead1 ? 9 : 0));
					renderOnce(isEnd1 || isEnd2 ? seat : seat_wheelchair, matrices, vertices, light, position - (useHead2 ? 9 : 0));
				}
				break;
			case INTERIOR_TRANSLUCENT:
				if (isEnd1 || isEnd2) {
					if (!useHead1) {
						renderMirror(side_panel, matrices, vertices, light, position - 31.5F);
					}
					if (!useHead2) {
						renderMirror(side_panel, matrices, vertices, light, position + 31.5F);
					}
				}
				break;
			case EXTERIOR:
				if (useHead1) {
					renderOnceFlipped(head_exterior, matrices, vertices, light, position);
					renderOnce(window_exterior, matrices, vertices, light, position);
				} else if (useHead2) {
					renderOnce(head_exterior, matrices, vertices, light, position);
					renderOnceFlipped(window_exterior, matrices, vertices, light, position);
				} else {
					renderMirror(window_exterior, matrices, vertices, light, position);
				}
				if (renderDetails && position == 0) {
					renderMirror(logo, matrices, vertices, light, position);
				}
				break;
		}
	}

	@Override
	protected void renderDoorPositions(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
		if (isEnd1Head && isIndex(0, position, getDoorPositions()) || isEnd2Head && isIndex(-1, position, getDoorPositions())) {
			return;
		}

		final boolean useEnd1 = isIndex(0, position, getDoorPositions());
		final boolean useEnd2 = isIndex(-1, position, getDoorPositions());
		final boolean isDoorLight1 = isIndex(1, position, getDoorPositions());
		final boolean isDoorLight2 = isIndex(-2, position, getDoorPositions());
		final boolean doorOpen = doorLeftZ > 0 || doorRightZ > 0;

		switch (renderStage) {
			case LIGHTS:
				if (doorOpen && renderDetails) {
					if (isDoorLight1) {
						renderMirror(door_light_on, matrices, vertices, light, position - 48);
					} else if (isDoorLight2) {
						renderMirror(door_light_on, matrices, vertices, light, position + 48);
					}
				}
				break;
			case INTERIOR:
				if (!useEnd1) {
					door_right_part.setOffset(0, 0, -doorRightZ);
					renderOnce(door_right, matrices, vertices, light, position);
					door_left_part.setOffset(0, 0, doorLeftZ);
					renderOnceFlipped(door_left, matrices, vertices, light, position);
				}
				if (!useEnd2) {
					door_left_part.setOffset(0, 0, doorRightZ);
					renderOnce(door_left, matrices, vertices, light, position);
					door_right_part.setOffset(0, 0, -doorLeftZ);
					renderOnceFlipped(door_right, matrices, vertices, light, position);
				}
				break;
			case EXTERIOR:
				if (!useEnd1) {
					door_right_exterior_part.setOffset(0, 0, -doorRightZ);
					renderOnce(door_right_exterior, matrices, vertices, light, position);
					door_left_exterior_part.setOffset(0, 0, doorLeftZ);
					renderOnceFlipped(door_left_exterior, matrices, vertices, light, position);
				}
				if (!useEnd2) {
					door_left_exterior_part.setOffset(0, 0, doorRightZ);
					renderOnce(door_left_exterior, matrices, vertices, light, position);
					door_right_exterior_part.setOffset(0, 0, -doorLeftZ);
					renderOnceFlipped(door_right_exterior, matrices, vertices, light, position);
				}
				if (!doorOpen && renderDetails) {
					if (isDoorLight1) {
						renderMirror(door_light_off, matrices, vertices, light, position - 48);
					} else if (isDoorLight2) {
						renderMirror(door_light_off, matrices, vertices, light, position + 48);
					}
				}
				break;
		}
	}

	@Override
	protected void renderHeadPosition1(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean useHeadlights) {
		if (renderStage == RenderStage.ALWAYS_ON_LIGHTS) {
			renderOnceFlipped(useHeadlights ? headlights : tail_lights, matrices, vertices, light, position);
		}
	}

	@Override
	protected void renderHeadPosition2(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean useHeadlights) {
		if (renderStage == RenderStage.ALWAYS_ON_LIGHTS) {
			renderOnce(useHeadlights ? headlights : tail_lights, matrices, vertices, light, position);
		}
	}

	@Override
	protected void renderEndPosition1(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
		switch (renderStage) {
			case LIGHTS:
				renderOnceFlipped(window_light_end, matrices, vertices, light, position);
				break;
			case INTERIOR:
				renderOnceFlipped(end, matrices, vertices, light, position);
				break;
			case EXTERIOR:
				renderOnceFlipped(end_exterior, matrices, vertices, light, position);
				break;
		}
	}

	@Override
	protected void renderEndPosition2(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
		switch (renderStage) {
			case LIGHTS:
				renderOnce(window_light_end, matrices, vertices, light, position);
				break;
			case INTERIOR:
				renderOnce(end, matrices, vertices, light, position);
				break;
			case EXTERIOR:
				renderOnce(end_exterior, matrices, vertices, light, position);
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
		return new int[]{-96, 0, 96};
	}

	@Override
	protected int[] getDoorPositions() {
		return new int[]{-144, -48, 48, 144};
	}

	@Override
	protected int[] getEndPositions() {
		return new int[]{-96, 96};
	}

	@Override
	protected int getDoorMax() {
		return DOOR_MAX;
	}

	@Override
	protected void renderTextDisplays(PoseStack matrices, Font font, MultiBufferSource.BufferSource immediate, Route thisRoute, Route nextRoute, Station thisStation, Station nextStation, Station lastStation, String customDestination, int car, int totalCars) {
		final String destinationString = getAlternatingString(getDestinationString(lastStation, customDestination, TextSpacingType.NORMAL, false));
		renderFrontDestination(
				matrices, font, immediate,
				0, -2.08F, getEndPositions()[0] / 16F - 3.56F, 0, 0, -0.01F,
				0, 0, 0.66F, 0.08F,
				0xFFFF9900, 0xFFFF9900, 1, destinationString, false, car, totalCars
		);
	}

	@Override
	protected String defaultDestinationString() {
		return "Not in Service";
	}
}