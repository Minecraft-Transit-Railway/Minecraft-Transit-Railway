package mtr.model;

import mtr.render.MoreRenderLayers;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;

public class ModelATrain extends ModelTrainBase {

	private final ModelPart window;
	private final ModelPart handrail_8_r1;
	private final ModelPart top_handrail_3_r1;
	private final ModelPart handrail_3_r1;
	private final ModelPart upper_wall_r1;
	private final ModelPart seat;
	private final ModelPart seat_back_r1;
	private final ModelPart window_exterior;
	private final ModelPart upper_wall_r2;
	private final ModelPart floor_r1;
	private final ModelPart window_exterior_end;
	private final ModelPart upper_wall_2_r1;
	private final ModelPart floor_2_r1;
	private final ModelPart upper_wall_1_r1;
	private final ModelPart floor_1_r1;
	private final ModelPart side_panel;
	private final ModelPart roof_window;
	private final ModelPart inner_roof_4_r1;
	private final ModelPart inner_roof_3_r1;
	private final ModelPart inner_roof_2_r1;
	private final ModelPart roof_door;
	private final ModelPart inner_roof_4_r2;
	private final ModelPart inner_roof_3_r2;
	private final ModelPart inner_roof_2_r2;
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
	private final ModelPart upper_wall_r3;
	private final ModelPart floor_r2;
	private final ModelPart door_left_exterior;
	private final ModelPart door_left_top_r2;
	private final ModelPart door_left_base_r1;
	private final ModelPart door_right_exterior;
	private final ModelPart door_right_top_r2;
	private final ModelPart door_right_base_r1;
	private final ModelPart door_exterior_end;
	private final ModelPart upper_wall_r4;
	private final ModelPart floor_r3;
	private final ModelPart door_left_exterior_end;
	private final ModelPart door_left_top_r3;
	private final ModelPart door_left_base_r2;
	private final ModelPart door_right_exterior_end;
	private final ModelPart door_right_top_r3;
	private final ModelPart door_right_base_r2;
	private final ModelPart end;
	private final ModelPart upper_wall_2_r2;
	private final ModelPart upper_wall_1_r2;
	private final ModelPart end_exterior;
	private final ModelPart upper_wall_2_r3;
	private final ModelPart upper_wall_1_r3;
	private final ModelPart floor_2_r2;
	private final ModelPart floor_1_r2;
	private final ModelPart roof_end;
	private final ModelPart handrail_2_r2;
	private final ModelPart inner_roof_1;
	private final ModelPart inner_roof_4_r3;
	private final ModelPart inner_roof_3_r3;
	private final ModelPart inner_roof_2_r3;
	private final ModelPart inner_roof_2;
	private final ModelPart inner_roof_4_r4;
	private final ModelPart inner_roof_3_r4;
	private final ModelPart inner_roof_2_r4;
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
	private final ModelPart upper_wall_1_r4;
	private final ModelPart head_exterior;
	private final ModelPart upper_wall_2_r5;
	private final ModelPart upper_wall_1_r5;
	private final ModelPart floor_2_r3;
	private final ModelPart floor_1_r3;
	private final ModelPart front;
	private final ModelPart front_bottom_5_r1;
	private final ModelPart front_bottom_4_r1;
	private final ModelPart front_bottom_2_r1;
	private final ModelPart front_bottom_1_r1;
	private final ModelPart front_panel_3_r1;
	private final ModelPart front_panel_2_r1;
	private final ModelPart front_panel_1_r1;
	private final ModelPart side_1;
	private final ModelPart front_middle_top_r1;
	private final ModelPart front_side_bottom_2_r1;
	private final ModelPart outer_roof_8_r1;
	private final ModelPart outer_roof_7_r1;
	private final ModelPart outer_roof_6_r1;
	private final ModelPart front_panel_8_r1;
	private final ModelPart front_panel_7_r1;
	private final ModelPart front_panel_6_r1;
	private final ModelPart front_panel_5_r1;
	private final ModelPart front_panel_4_r1;
	private final ModelPart front_side_upper_2_r1;
	private final ModelPart front_side_lower_2_r1;
	private final ModelPart side_2;
	private final ModelPart front_middle_top_r2;
	private final ModelPart front_side_bottom_2_r2;
	private final ModelPart outer_roof_8_r2;
	private final ModelPart outer_roof_7_r2;
	private final ModelPart outer_roof_6_r2;
	private final ModelPart front_panel_8_r2;
	private final ModelPart front_panel_7_r2;
	private final ModelPart front_panel_6_r2;
	private final ModelPart front_panel_5_r2;
	private final ModelPart front_panel_4_r2;
	private final ModelPart front_side_upper_2_r2;
	private final ModelPart front_side_lower_2_r2;
	private final ModelPart headlights;
	private final ModelPart tail_lights;

	public ModelATrain() {
		textureWidth = 336;
		textureHeight = 336;
		window = new ModelPart(this);
		window.setPivot(0.0F, 24.0F, 0.0F);
		window.setTextureOffset(0, 0).addCuboid(-20.0F, 0.0F, -24.0F, 20.0F, 1.0F, 48.0F, 0.0F, false);
		window.setTextureOffset(0, 120).addCuboid(-21.0F, -14.0F, -26.0F, 3.0F, 14.0F, 52.0F, 0.0F, false);
		window.setTextureOffset(4, 0).addCuboid(0.0F, -35.0F, 0.0F, 0.0F, 35.0F, 0.0F, 0.2F, false);
		window.setTextureOffset(4, 0).addCuboid(0.0F, -35.0F, -22.0F, 0.0F, 35.0F, 0.0F, 0.2F, false);
		window.setTextureOffset(4, 0).addCuboid(0.0F, -35.0F, 22.0F, 0.0F, 35.0F, 0.0F, 0.2F, false);
		window.setTextureOffset(42, 40).addCuboid(-1.0F, -32.0F, 16.5F, 2.0F, 4.0F, 0.0F, 0.0F, false);
		window.setTextureOffset(42, 40).addCuboid(-1.0F, -32.0F, 5.5F, 2.0F, 4.0F, 0.0F, 0.0F, false);
		window.setTextureOffset(42, 40).addCuboid(-1.0F, -32.0F, -5.5F, 2.0F, 4.0F, 0.0F, 0.0F, false);
		window.setTextureOffset(42, 40).addCuboid(-1.0F, -32.0F, -16.5F, 2.0F, 4.0F, 0.0F, 0.0F, false);

		handrail_8_r1 = new ModelPart(this);
		handrail_8_r1.setPivot(0.0F, 0.0F, 0.0F);
		window.addChild(handrail_8_r1);
		setRotationAngle(handrail_8_r1, -1.5708F, 0.0F, 0.0F);
		handrail_8_r1.setTextureOffset(0, 0).addCuboid(0.0F, -24.0F, -31.5F, 0.0F, 48.0F, 0.0F, 0.2F, false);

		top_handrail_3_r1 = new ModelPart(this);
		top_handrail_3_r1.setPivot(-11.0F, -5.0F, 0.0F);
		window.addChild(top_handrail_3_r1);
		setRotationAngle(top_handrail_3_r1, -1.5708F, 0.0F, -0.0698F);
		top_handrail_3_r1.setTextureOffset(0, 0).addCuboid(0.0F, -22.0F, -23.0F, 0.0F, 44.0F, 0.0F, 0.2F, false);

		handrail_3_r1 = new ModelPart(this);
		handrail_3_r1.setPivot(-11.0F, -5.0F, 0.0F);
		window.addChild(handrail_3_r1);
		setRotationAngle(handrail_3_r1, 0.0F, 0.0F, -0.0698F);
		handrail_3_r1.setTextureOffset(8, 0).addCuboid(0.0F, -27.2F, 22.0F, 0.0F, 27.0F, 0.0F, 0.2F, false);
		handrail_3_r1.setTextureOffset(0, 0).addCuboid(0.0F, -27.2F, 0.0F, 0.0F, 4.0F, 0.0F, 0.2F, false);
		handrail_3_r1.setTextureOffset(8, 0).addCuboid(0.0F, -27.2F, -22.0F, 0.0F, 27.0F, 0.0F, 0.2F, false);

		upper_wall_r1 = new ModelPart(this);
		upper_wall_r1.setPivot(-21.0F, -14.0F, 0.0F);
		window.addChild(upper_wall_r1);
		setRotationAngle(upper_wall_r1, 0.0F, 0.0F, 0.1396F);
		upper_wall_r1.setTextureOffset(0, 49).addCuboid(0.0F, -19.0F, -26.0F, 3.0F, 19.0F, 52.0F, 0.0F, false);

		seat = new ModelPart(this);
		seat.setPivot(0.0F, 0.0F, 0.0F);
		window.addChild(seat);
		seat.setTextureOffset(142, 76).addCuboid(-18.0F, -6.0F, -22.0F, 7.0F, 1.0F, 44.0F, 0.0F, false);
		seat.setTextureOffset(180, 121).addCuboid(-18.0F, -5.0F, -21.0F, 5.0F, 5.0F, 42.0F, 0.0F, false);

		seat_back_r1 = new ModelPart(this);
		seat_back_r1.setPivot(-17.0F, -6.0F, 0.0F);
		seat.addChild(seat_back_r1);
		setRotationAngle(seat_back_r1, 0.0F, 0.0F, -0.0524F);
		seat_back_r1.setTextureOffset(116, 175).addCuboid(-1.0F, -8.0F, -22.0F, 1.0F, 8.0F, 44.0F, 0.0F, false);

		window_exterior = new ModelPart(this);
		window_exterior.setPivot(0.0F, 24.0F, 0.0F);
		window_exterior.setTextureOffset(58, 73).addCuboid(-21.0F, -14.0F, -26.0F, 0.0F, 14.0F, 52.0F, 0.0F, false);

		upper_wall_r2 = new ModelPart(this);
		upper_wall_r2.setPivot(-21.0F, -14.0F, 0.0F);
		window_exterior.addChild(upper_wall_r2);
		setRotationAngle(upper_wall_r2, 0.0F, 0.0F, 0.1396F);
		upper_wall_r2.setTextureOffset(58, 0).addCuboid(0.0F, -23.0F, -26.0F, 0.0F, 23.0F, 52.0F, 0.0F, false);

		floor_r1 = new ModelPart(this);
		floor_r1.setPivot(-21.0F, 0.0F, 0.0F);
		window_exterior.addChild(floor_r1);
		setRotationAngle(floor_r1, 0.0F, 0.0F, -0.1745F);
		floor_r1.setTextureOffset(62, 139).addCuboid(0.0F, 0.0F, -24.0F, 1.0F, 8.0F, 48.0F, 0.0F, false);

		window_exterior_end = new ModelPart(this);
		window_exterior_end.setPivot(0.0F, 24.0F, 0.0F);
		window_exterior_end.setTextureOffset(136, 244).addCuboid(21.0F, -14.0F, -26.0F, 0.0F, 14.0F, 52.0F, 0.0F, true);
		window_exterior_end.setTextureOffset(136, 244).addCuboid(-21.0F, -14.0F, -26.0F, 0.0F, 14.0F, 52.0F, 0.0F, false);

		upper_wall_2_r1 = new ModelPart(this);
		upper_wall_2_r1.setPivot(-21.0F, -14.0F, 0.0F);
		window_exterior_end.addChild(upper_wall_2_r1);
		setRotationAngle(upper_wall_2_r1, 0.0F, 0.0F, 0.1396F);
		upper_wall_2_r1.setTextureOffset(58, 0).addCuboid(0.0F, -23.0F, -26.0F, 0.0F, 23.0F, 52.0F, 0.0F, false);

		floor_2_r1 = new ModelPart(this);
		floor_2_r1.setPivot(-21.0F, 0.0F, 0.0F);
		window_exterior_end.addChild(floor_2_r1);
		setRotationAngle(floor_2_r1, 0.0F, 0.0F, -0.1745F);
		floor_2_r1.setTextureOffset(8, 272).addCuboid(0.0F, 0.0F, -24.0F, 1.0F, 8.0F, 48.0F, 0.0F, false);

		upper_wall_1_r1 = new ModelPart(this);
		upper_wall_1_r1.setPivot(21.0F, -14.0F, 0.0F);
		window_exterior_end.addChild(upper_wall_1_r1);
		setRotationAngle(upper_wall_1_r1, 0.0F, 0.0F, -0.1396F);
		upper_wall_1_r1.setTextureOffset(58, 0).addCuboid(0.0F, -23.0F, -26.0F, 0.0F, 23.0F, 52.0F, 0.0F, true);

		floor_1_r1 = new ModelPart(this);
		floor_1_r1.setPivot(21.0F, 0.0F, 0.0F);
		window_exterior_end.addChild(floor_1_r1);
		setRotationAngle(floor_1_r1, 0.0F, 0.0F, 0.1745F);
		floor_1_r1.setTextureOffset(8, 272).addCuboid(-1.0F, 0.0F, -24.0F, 1.0F, 8.0F, 48.0F, 0.0F, true);

		side_panel = new ModelPart(this);
		side_panel.setPivot(0.0F, 24.0F, 0.0F);
		side_panel.setTextureOffset(90, 139).addCuboid(-18.0F, -35.0F, 0.0F, 7.0F, 30.0F, 0.0F, 0.0F, false);

		roof_window = new ModelPart(this);
		roof_window.setPivot(0.0F, 24.0F, 0.0F);
		roof_window.setTextureOffset(62, 0).addCuboid(-16.0F, -32.0F, -24.0F, 3.0F, 0.0F, 48.0F, 0.0F, false);
		roof_window.setTextureOffset(52, 0).addCuboid(-5.0F, -34.5F, -24.0F, 5.0F, 0.0F, 48.0F, 0.0F, false);

		inner_roof_4_r1 = new ModelPart(this);
		inner_roof_4_r1.setPivot(-3.5384F, -34.6286F, 0.0F);
		roof_window.addChild(inner_roof_4_r1);
		setRotationAngle(inner_roof_4_r1, 0.0F, 0.0F, -0.1396F);
		inner_roof_4_r1.setTextureOffset(40, 0).addCuboid(-6.0F, 0.0F, -24.0F, 6.0F, 0.0F, 48.0F, 0.0F, false);

		inner_roof_3_r1 = new ModelPart(this);
		inner_roof_3_r1.setPivot(-10.4309F, -33.4846F, 0.0F);
		roof_window.addChild(inner_roof_3_r1);
		setRotationAngle(inner_roof_3_r1, 0.0F, 0.0F, -0.3142F);
		inner_roof_3_r1.setTextureOffset(0, 49).addCuboid(-1.0F, 0.0F, -24.0F, 2.0F, 0.0F, 48.0F, 0.0F, false);

		inner_roof_2_r1 = new ModelPart(this);
		inner_roof_2_r1.setPivot(-13.0F, -32.0F, 0.0F);
		roof_window.addChild(inner_roof_2_r1);
		setRotationAngle(inner_roof_2_r1, 0.0F, 0.0F, -0.6283F);
		inner_roof_2_r1.setTextureOffset(68, 0).addCuboid(0.0F, 0.0F, -24.0F, 2.0F, 0.0F, 48.0F, 0.0F, false);

		roof_door = new ModelPart(this);
		roof_door.setPivot(0.0F, 24.0F, 0.0F);
		roof_door.setTextureOffset(208, 278).addCuboid(-18.0F, -33.0F, -16.0F, 5.0F, 1.0F, 32.0F, 0.0F, false);
		roof_door.setTextureOffset(68, 8).addCuboid(-5.0F, -34.5F, -16.0F, 5.0F, 0.0F, 32.0F, 0.0F, false);

		inner_roof_4_r2 = new ModelPart(this);
		inner_roof_4_r2.setPivot(-3.5384F, -34.6286F, 0.0F);
		roof_door.addChild(inner_roof_4_r2);
		setRotationAngle(inner_roof_4_r2, 0.0F, 0.0F, -0.1396F);
		inner_roof_4_r2.setTextureOffset(56, 8).addCuboid(-6.0F, 0.0F, -16.0F, 6.0F, 0.0F, 32.0F, 0.0F, false);

		inner_roof_3_r2 = new ModelPart(this);
		inner_roof_3_r2.setPivot(-10.4309F, -33.4846F, 0.0F);
		roof_door.addChild(inner_roof_3_r2);
		setRotationAngle(inner_roof_3_r2, 0.0F, 0.0F, -0.3142F);
		inner_roof_3_r2.setTextureOffset(126, 0).addCuboid(-1.0F, 0.0F, -16.0F, 2.0F, 0.0F, 32.0F, 0.0F, false);

		inner_roof_2_r2 = new ModelPart(this);
		inner_roof_2_r2.setPivot(-13.0F, -32.0F, 0.0F);
		roof_door.addChild(inner_roof_2_r2);
		setRotationAngle(inner_roof_2_r2, 0.0F, 0.0F, -0.6283F);
		inner_roof_2_r2.setTextureOffset(122, 0).addCuboid(0.0F, 0.0F, -16.0F, 2.0F, 0.0F, 32.0F, 0.0F, false);

		handrail_2_r1 = new ModelPart(this);
		handrail_2_r1.setPivot(0.0F, 0.0F, 0.0F);
		roof_door.addChild(handrail_2_r1);
		setRotationAngle(handrail_2_r1, -1.5708F, 0.0F, 0.0F);
		handrail_2_r1.setTextureOffset(0, 0).addCuboid(0.0F, -16.0F, -31.5F, 0.0F, 32.0F, 0.0F, 0.2F, false);

		roof_exterior = new ModelPart(this);
		roof_exterior.setPivot(0.0F, 24.0F, 0.0F);
		roof_exterior.setTextureOffset(80, 0).addCuboid(-5.3387F, -41.5712F, -20.0F, 6.0F, 0.0F, 40.0F, 0.0F, false);

		outer_roof_5_r1 = new ModelPart(this);
		outer_roof_5_r1.setPivot(-9.7703F, -40.7898F, 0.0F);
		roof_exterior.addChild(outer_roof_5_r1);
		setRotationAngle(outer_roof_5_r1, 0.0F, 0.0F, -0.1745F);
		outer_roof_5_r1.setTextureOffset(102, 76).addCuboid(-4.5F, 0.0F, -20.0F, 9.0F, 0.0F, 40.0F, 0.0F, false);

		outer_roof_4_r1 = new ModelPart(this);
		outer_roof_4_r1.setPivot(-15.501F, -39.2584F, 0.0F);
		roof_exterior.addChild(outer_roof_4_r1);
		setRotationAngle(outer_roof_4_r1, 0.0F, 0.0F, -0.5236F);
		outer_roof_4_r1.setTextureOffset(0, 0).addCuboid(-1.5F, 0.0F, -20.0F, 3.0F, 0.0F, 40.0F, 0.0F, false);

		outer_roof_3_r1 = new ModelPart(this);
		outer_roof_3_r1.setPivot(-17.3F, -37.6423F, 0.0F);
		roof_exterior.addChild(outer_roof_3_r1);
		setRotationAngle(outer_roof_3_r1, 0.0F, 0.0F, -1.0472F);
		outer_roof_3_r1.setTextureOffset(0, 49).addCuboid(-1.0F, 0.0F, -20.0F, 2.0F, 0.0F, 40.0F, 0.0F, false);

		door = new ModelPart(this);
		door.setPivot(0.0F, 24.0F, 0.0F);
		door.setTextureOffset(0, 195).addCuboid(-20.0F, 0.0F, -16.0F, 20.0F, 1.0F, 32.0F, 0.0F, false);
		door.setTextureOffset(4, 0).addCuboid(0.0F, -35.0F, 0.0F, 0.0F, 35.0F, 0.0F, 0.2F, false);

		door_left = new ModelPart(this);
		door_left.setPivot(0.0F, 0.0F, 0.0F);
		door.addChild(door_left);
		door_left.setTextureOffset(280, 186).addCuboid(-21.0F, -14.0F, 0.0F, 1.0F, 14.0F, 14.0F, 0.0F, false);

		door_left_top_r1 = new ModelPart(this);
		door_left_top_r1.setPivot(-20.8F, -14.0F, 0.0F);
		door_left.addChild(door_left_top_r1);
		setRotationAngle(door_left_top_r1, 0.0F, 0.0F, 0.1396F);
		door_left_top_r1.setTextureOffset(68, 279).addCuboid(-0.2F, -19.0F, 0.0F, 1.0F, 19.0F, 14.0F, 0.0F, false);

		door_right = new ModelPart(this);
		door_right.setPivot(0.0F, 0.0F, 0.0F);
		door.addChild(door_right);
		door_right.setTextureOffset(56, 233).addCuboid(-21.0F, -14.0F, -14.0F, 1.0F, 14.0F, 14.0F, 0.0F, false);

		door_right_top_r1 = new ModelPart(this);
		door_right_top_r1.setPivot(-20.8F, -14.0F, 0.0F);
		door_right.addChild(door_right_top_r1);
		setRotationAngle(door_right_top_r1, 0.0F, 0.0F, 0.1396F);
		door_right_top_r1.setTextureOffset(0, 190).addCuboid(-0.2F, -19.0F, -14.0F, 1.0F, 19.0F, 14.0F, 0.0F, false);

		door_exterior = new ModelPart(this);
		door_exterior.setPivot(0.0F, 24.0F, 0.0F);


		upper_wall_r3 = new ModelPart(this);
		upper_wall_r3.setPivot(-21.0F, -14.0F, 0.0F);
		door_exterior.addChild(upper_wall_r3);
		setRotationAngle(upper_wall_r3, 0.0F, 0.0F, 0.1396F);
		upper_wall_r3.setTextureOffset(72, 196).addCuboid(0.0F, -23.0F, -16.0F, 1.0F, 4.0F, 32.0F, 0.0F, false);

		floor_r2 = new ModelPart(this);
		floor_r2.setPivot(-21.0F, 0.0F, 0.0F);
		door_exterior.addChild(floor_r2);
		setRotationAngle(floor_r2, 0.0F, 0.0F, -0.1745F);
		floor_r2.setTextureOffset(56, 232).addCuboid(0.0F, 0.0F, -16.0F, 1.0F, 8.0F, 32.0F, 0.0F, false);

		door_left_exterior = new ModelPart(this);
		door_left_exterior.setPivot(0.0F, 0.0F, 0.0F);
		door_exterior.addChild(door_left_exterior);
		door_left_exterior.setTextureOffset(0, 287).addCuboid(-21.0F, -14.0F, 0.0F, 0.0F, 14.0F, 16.0F, 0.0F, false);

		door_left_top_r2 = new ModelPart(this);
		door_left_top_r2.setPivot(-21.0F, -14.0F, 0.0F);
		door_left_exterior.addChild(door_left_top_r2);
		setRotationAngle(door_left_top_r2, 0.0F, 0.0F, 0.1396F);
		door_left_top_r2.setTextureOffset(0, 265).addCuboid(0.0F, -22.0F, 0.0F, 0.0F, 22.0F, 16.0F, 0.0F, false);

		door_left_base_r1 = new ModelPart(this);
		door_left_base_r1.setPivot(-21.0F, 0.0F, 0.0F);
		door_left_exterior.addChild(door_left_base_r1);
		setRotationAngle(door_left_base_r1, 0.0F, 0.0F, -0.1745F);
		door_left_base_r1.setTextureOffset(0, 301).addCuboid(0.0F, 0.0F, 0.0F, 0.0F, 2.0F, 16.0F, 0.0F, false);

		door_right_exterior = new ModelPart(this);
		door_right_exterior.setPivot(0.0F, 0.0F, 0.0F);
		door_exterior.addChild(door_right_exterior);
		door_right_exterior.setTextureOffset(294, 76).addCuboid(-21.0F, -14.0F, -16.0F, 0.0F, 14.0F, 16.0F, 0.0F, false);

		door_right_top_r2 = new ModelPart(this);
		door_right_top_r2.setPivot(-21.0F, -14.0F, 0.0F);
		door_right_exterior.addChild(door_right_top_r2);
		setRotationAngle(door_right_top_r2, 0.0F, 0.0F, 0.1396F);
		door_right_top_r2.setTextureOffset(294, 54).addCuboid(0.0F, -22.0F, -16.0F, 0.0F, 22.0F, 16.0F, 0.0F, false);

		door_right_base_r1 = new ModelPart(this);
		door_right_base_r1.setPivot(-21.0F, 0.0F, 0.0F);
		door_right_exterior.addChild(door_right_base_r1);
		setRotationAngle(door_right_base_r1, 0.0F, 0.0F, -0.1745F);
		door_right_base_r1.setTextureOffset(294, 90).addCuboid(0.0F, 0.0F, -16.0F, 0.0F, 2.0F, 16.0F, 0.0F, false);

		door_exterior_end = new ModelPart(this);
		door_exterior_end.setPivot(0.0F, 24.0F, 0.0F);


		upper_wall_r4 = new ModelPart(this);
		upper_wall_r4.setPivot(-21.0F, -14.0F, 0.0F);
		door_exterior_end.addChild(upper_wall_r4);
		setRotationAngle(upper_wall_r4, 0.0F, 0.0F, 0.1396F);
		upper_wall_r4.setTextureOffset(72, 196).addCuboid(0.0F, -23.0F, -16.0F, 1.0F, 4.0F, 32.0F, 0.0F, false);

		floor_r3 = new ModelPart(this);
		floor_r3.setPivot(-21.0F, 0.0F, 0.0F);
		door_exterior_end.addChild(floor_r3);
		setRotationAngle(floor_r3, 0.0F, 0.0F, -0.1745F);
		floor_r3.setTextureOffset(240, 196).addCuboid(0.0F, 0.0F, -16.0F, 1.0F, 8.0F, 32.0F, 0.0F, false);

		door_left_exterior_end = new ModelPart(this);
		door_left_exterior_end.setPivot(0.0F, 0.0F, 0.0F);
		door_exterior_end.addChild(door_left_exterior_end);
		door_left_exterior_end.setTextureOffset(162, 109).addCuboid(-21.0F, -14.0F, 0.0F, 0.0F, 14.0F, 16.0F, 0.0F, false);

		door_left_top_r3 = new ModelPart(this);
		door_left_top_r3.setPivot(-21.0F, -14.0F, 0.0F);
		door_left_exterior_end.addChild(door_left_top_r3);
		setRotationAngle(door_left_top_r3, 0.0F, 0.0F, 0.1396F);
		door_left_top_r3.setTextureOffset(0, 265).addCuboid(0.0F, -22.0F, 0.0F, 0.0F, 22.0F, 16.0F, 0.0F, false);

		door_left_base_r2 = new ModelPart(this);
		door_left_base_r2.setPivot(-21.0F, 0.0F, 0.0F);
		door_left_exterior_end.addChild(door_left_base_r2);
		setRotationAngle(door_left_base_r2, 0.0F, 0.0F, -0.1745F);
		door_left_base_r2.setTextureOffset(162, 123).addCuboid(0.0F, 0.0F, 0.0F, 0.0F, 2.0F, 16.0F, 0.0F, false);

		door_right_exterior_end = new ModelPart(this);
		door_right_exterior_end.setPivot(0.0F, 0.0F, 0.0F);
		door_exterior_end.addChild(door_right_exterior_end);
		door_right_exterior_end.setTextureOffset(162, 125).addCuboid(-21.0F, -14.0F, -16.0F, 0.0F, 14.0F, 16.0F, 0.0F, false);

		door_right_top_r3 = new ModelPart(this);
		door_right_top_r3.setPivot(-21.0F, -14.0F, 0.0F);
		door_right_exterior_end.addChild(door_right_top_r3);
		setRotationAngle(door_right_top_r3, 0.0F, 0.0F, 0.1396F);
		door_right_top_r3.setTextureOffset(294, 54).addCuboid(0.0F, -22.0F, -16.0F, 0.0F, 22.0F, 16.0F, 0.0F, false);

		door_right_base_r2 = new ModelPart(this);
		door_right_base_r2.setPivot(-21.0F, 0.0F, 0.0F);
		door_right_exterior_end.addChild(door_right_base_r2);
		setRotationAngle(door_right_base_r2, 0.0F, 0.0F, -0.1745F);
		door_right_base_r2.setTextureOffset(162, 139).addCuboid(0.0F, 0.0F, -16.0F, 0.0F, 2.0F, 16.0F, 0.0F, false);

		end = new ModelPart(this);
		end.setPivot(0.0F, 24.0F, 0.0F);
		end.setTextureOffset(162, 175).addCuboid(-20.0F, 0.0F, -12.0F, 40.0F, 1.0F, 20.0F, 0.0F, false);
		end.setTextureOffset(110, 195).addCuboid(18.0F, -14.0F, 7.0F, 3.0F, 14.0F, 3.0F, 0.0F, true);
		end.setTextureOffset(110, 195).addCuboid(-21.0F, -14.0F, 7.0F, 3.0F, 14.0F, 3.0F, 0.0F, false);
		end.setTextureOffset(127, 231).addCuboid(9.5F, -34.0F, -12.0F, 9.0F, 34.0F, 19.0F, 0.0F, false);
		end.setTextureOffset(0, 228).addCuboid(-18.5F, -34.0F, -12.0F, 9.0F, 34.0F, 19.0F, 0.0F, false);
		end.setTextureOffset(79, 293).addCuboid(-9.5F, -35.0F, -12.0F, 19.0F, 2.0F, 19.0F, 0.0F, false);

		upper_wall_2_r2 = new ModelPart(this);
		upper_wall_2_r2.setPivot(-21.0F, -14.0F, 0.0F);
		end.addChild(upper_wall_2_r2);
		setRotationAngle(upper_wall_2_r2, 0.0F, 0.0F, 0.1396F);
		upper_wall_2_r2.setTextureOffset(248, 110).addCuboid(0.0F, -19.0F, 7.0F, 3.0F, 19.0F, 3.0F, 0.0F, false);

		upper_wall_1_r2 = new ModelPart(this);
		upper_wall_1_r2.setPivot(21.0F, -14.0F, 0.0F);
		end.addChild(upper_wall_1_r2);
		setRotationAngle(upper_wall_1_r2, 0.0F, 0.0F, -0.1396F);
		upper_wall_1_r2.setTextureOffset(164, 227).addCuboid(-3.0F, -19.0F, 7.0F, 3.0F, 19.0F, 3.0F, 0.0F, true);

		end_exterior = new ModelPart(this);
		end_exterior.setPivot(0.0F, 24.0F, 0.0F);
		end_exterior.setTextureOffset(266, 88).addCuboid(18.0F, -14.0F, -12.0F, 3.0F, 14.0F, 22.0F, 0.0F, true);
		end_exterior.setTextureOffset(0, 134).addCuboid(-21.0F, -14.0F, -12.0F, 3.0F, 14.0F, 22.0F, 0.0F, false);
		end_exterior.setTextureOffset(182, 0).addCuboid(9.5F, -34.0F, -12.0F, 9.0F, 34.0F, 0.0F, 0.0F, true);
		end_exterior.setTextureOffset(182, 0).addCuboid(-18.5F, -34.0F, -12.0F, 9.0F, 34.0F, 0.0F, 0.0F, false);
		end_exterior.setTextureOffset(240, 236).addCuboid(-18.0F, -41.0F, -12.0F, 36.0F, 8.0F, 0.0F, 0.0F, false);

		upper_wall_2_r3 = new ModelPart(this);
		upper_wall_2_r3.setPivot(-21.0F, -14.0F, 0.0F);
		end_exterior.addChild(upper_wall_2_r3);
		setRotationAngle(upper_wall_2_r3, 0.0F, 0.0F, 0.1396F);
		upper_wall_2_r3.setTextureOffset(183, 251).addCuboid(0.0F, -23.0F, -12.0F, 3.0F, 23.0F, 22.0F, 0.0F, false);

		upper_wall_1_r3 = new ModelPart(this);
		upper_wall_1_r3.setPivot(21.0F, -14.0F, 0.0F);
		end_exterior.addChild(upper_wall_1_r3);
		setRotationAngle(upper_wall_1_r3, 0.0F, 0.0F, -0.1396F);
		upper_wall_1_r3.setTextureOffset(252, 251).addCuboid(-3.0F, -23.0F, -12.0F, 3.0F, 23.0F, 22.0F, 0.0F, true);

		floor_2_r2 = new ModelPart(this);
		floor_2_r2.setPivot(-21.0F, 0.0F, 0.0F);
		end_exterior.addChild(floor_2_r2);
		setRotationAngle(floor_2_r2, 0.0F, 0.0F, -0.1745F);
		floor_2_r2.setTextureOffset(112, 139).addCuboid(0.0F, 0.0F, -12.0F, 1.0F, 8.0F, 20.0F, 0.0F, false);

		floor_1_r2 = new ModelPart(this);
		floor_1_r2.setPivot(21.0F, 0.0F, 0.0F);
		end_exterior.addChild(floor_1_r2);
		setRotationAngle(floor_1_r2, 0.0F, 0.0F, 0.1745F);
		floor_1_r2.setTextureOffset(112, 139).addCuboid(-1.0F, 0.0F, -12.0F, 1.0F, 8.0F, 20.0F, 0.0F, true);

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
		inner_roof_1.setTextureOffset(204, 274).addCuboid(-16.0F, 0.0F, -12.0F, 5.0F, 1.0F, 36.0F, 0.0F, false);
		inner_roof_1.setTextureOffset(64, 0).addCuboid(-3.0F, -1.5F, -12.0F, 5.0F, 0.0F, 36.0F, 0.0F, false);

		inner_roof_4_r3 = new ModelPart(this);
		inner_roof_4_r3.setPivot(-1.5384F, -1.6286F, -16.0F);
		inner_roof_1.addChild(inner_roof_4_r3);
		setRotationAngle(inner_roof_4_r3, 0.0F, 0.0F, -0.1396F);
		inner_roof_4_r3.setTextureOffset(52, 0).addCuboid(-6.0F, 0.0F, 4.0F, 6.0F, 0.0F, 36.0F, 0.0F, false);

		inner_roof_3_r3 = new ModelPart(this);
		inner_roof_3_r3.setPivot(-8.4309F, -0.4846F, -16.0F);
		inner_roof_1.addChild(inner_roof_3_r3);
		setRotationAngle(inner_roof_3_r3, 0.0F, 0.0F, -0.3142F);
		inner_roof_3_r3.setTextureOffset(8, 49).addCuboid(-1.0F, 0.0F, 4.0F, 2.0F, 0.0F, 36.0F, 0.0F, false);

		inner_roof_2_r3 = new ModelPart(this);
		inner_roof_2_r3.setPivot(-11.0F, 1.0F, -16.0F);
		inner_roof_1.addChild(inner_roof_2_r3);
		setRotationAngle(inner_roof_2_r3, 0.0F, 0.0F, -0.6283F);
		inner_roof_2_r3.setTextureOffset(96, 0).addCuboid(0.0F, 0.0F, 4.0F, 2.0F, 0.0F, 36.0F, 0.0F, false);

		inner_roof_2 = new ModelPart(this);
		inner_roof_2.setPivot(-2.0F, -33.0F, 16.0F);
		roof_end.addChild(inner_roof_2);
		inner_roof_2.setTextureOffset(204, 274).addCuboid(15.0F, 0.0F, -12.0F, 5.0F, 1.0F, 36.0F, 0.0F, true);
		inner_roof_2.setTextureOffset(64, 0).addCuboid(2.0F, -1.5F, -12.0F, 5.0F, 0.0F, 36.0F, 0.0F, true);

		inner_roof_4_r4 = new ModelPart(this);
		inner_roof_4_r4.setPivot(5.5384F, -1.6286F, -16.0F);
		inner_roof_2.addChild(inner_roof_4_r4);
		setRotationAngle(inner_roof_4_r4, 0.0F, 0.0F, 0.1396F);
		inner_roof_4_r4.setTextureOffset(52, 0).addCuboid(0.0F, 0.0F, 4.0F, 6.0F, 0.0F, 36.0F, 0.0F, true);

		inner_roof_3_r4 = new ModelPart(this);
		inner_roof_3_r4.setPivot(12.4309F, -0.4846F, -16.0F);
		inner_roof_2.addChild(inner_roof_3_r4);
		setRotationAngle(inner_roof_3_r4, 0.0F, 0.0F, 0.3142F);
		inner_roof_3_r4.setTextureOffset(8, 49).addCuboid(-1.0F, 0.0F, 4.0F, 2.0F, 0.0F, 36.0F, 0.0F, true);

		inner_roof_2_r4 = new ModelPart(this);
		inner_roof_2_r4.setPivot(15.0F, 1.0F, -16.0F);
		inner_roof_2.addChild(inner_roof_2_r4);
		setRotationAngle(inner_roof_2_r4, 0.0F, 0.0F, 0.6283F);
		inner_roof_2_r4.setTextureOffset(96, 0).addCuboid(-2.0F, 0.0F, 4.0F, 2.0F, 0.0F, 36.0F, 0.0F, true);

		roof_end_exterior = new ModelPart(this);
		roof_end_exterior.setPivot(0.0F, 24.0F, 0.0F);
		roof_end_exterior.setTextureOffset(62, 75).addCuboid(-8.0F, -42.5F, 0.0F, 16.0F, 2.0F, 48.0F, 0.0F, false);

		vent_2_r1 = new ModelPart(this);
		vent_2_r1.setPivot(-8.0F, -42.5F, 0.0F);
		roof_end_exterior.addChild(vent_2_r1);
		setRotationAngle(vent_2_r1, 0.0F, 0.0F, -0.3491F);
		vent_2_r1.setTextureOffset(88, 1).addCuboid(-9.0F, 0.0F, 0.0F, 9.0F, 2.0F, 48.0F, 0.0F, false);

		vent_1_r1 = new ModelPart(this);
		vent_1_r1.setPivot(8.0F, -42.5F, 0.0F);
		roof_end_exterior.addChild(vent_1_r1);
		setRotationAngle(vent_1_r1, 0.0F, 0.0F, 0.3491F);
		vent_1_r1.setTextureOffset(88, 1).addCuboid(0.0F, 0.0F, 0.0F, 9.0F, 2.0F, 48.0F, 0.0F, true);

		outer_roof_1 = new ModelPart(this);
		outer_roof_1.setPivot(0.0F, 0.0F, 0.0F);
		roof_end_exterior.addChild(outer_roof_1);
		outer_roof_1.setTextureOffset(0, 77).addCuboid(-5.3387F, -41.5712F, -12.0F, 6.0F, 1.0F, 20.0F, 0.0F, false);

		outer_roof_5_r2 = new ModelPart(this);
		outer_roof_5_r2.setPivot(-9.7703F, -40.7898F, 0.0F);
		outer_roof_1.addChild(outer_roof_5_r2);
		setRotationAngle(outer_roof_5_r2, 0.0F, 0.0F, -0.1745F);
		outer_roof_5_r2.setTextureOffset(212, 196).addCuboid(-4.5F, 0.0F, -12.0F, 9.0F, 1.0F, 20.0F, 0.0F, false);

		outer_roof_4_r2 = new ModelPart(this);
		outer_roof_4_r2.setPivot(-15.501F, -39.2584F, 0.0F);
		outer_roof_1.addChild(outer_roof_4_r2);
		setRotationAngle(outer_roof_4_r2, 0.0F, 0.0F, -0.5236F);
		outer_roof_4_r2.setTextureOffset(110, 195).addCuboid(-1.5F, 0.0F, -12.0F, 3.0F, 1.0F, 20.0F, 0.0F, false);

		outer_roof_3_r2 = new ModelPart(this);
		outer_roof_3_r2.setPivot(-17.3F, -37.6423F, 0.0F);
		outer_roof_1.addChild(outer_roof_3_r2);
		setRotationAngle(outer_roof_3_r2, 0.0F, 0.0F, -1.0472F);
		outer_roof_3_r2.setTextureOffset(162, 196).addCuboid(-1.0F, 0.0F, -12.0F, 2.0F, 1.0F, 20.0F, 0.0F, false);

		outer_roof_2 = new ModelPart(this);
		outer_roof_2.setPivot(0.0F, 0.0F, 0.0F);
		roof_end_exterior.addChild(outer_roof_2);
		outer_roof_2.setTextureOffset(0, 77).addCuboid(-0.6613F, -41.5712F, -12.0F, 6.0F, 1.0F, 20.0F, 0.0F, true);

		outer_roof_5_r3 = new ModelPart(this);
		outer_roof_5_r3.setPivot(9.7703F, -40.7898F, 0.0F);
		outer_roof_2.addChild(outer_roof_5_r3);
		setRotationAngle(outer_roof_5_r3, 0.0F, 0.0F, 0.1745F);
		outer_roof_5_r3.setTextureOffset(212, 196).addCuboid(-4.5F, 0.0F, -12.0F, 9.0F, 1.0F, 20.0F, 0.0F, true);

		outer_roof_4_r3 = new ModelPart(this);
		outer_roof_4_r3.setPivot(15.501F, -39.2584F, 0.0F);
		outer_roof_2.addChild(outer_roof_4_r3);
		setRotationAngle(outer_roof_4_r3, 0.0F, 0.0F, 0.5236F);
		outer_roof_4_r3.setTextureOffset(110, 195).addCuboid(-1.5F, 0.0F, -12.0F, 3.0F, 1.0F, 20.0F, 0.0F, true);

		outer_roof_3_r3 = new ModelPart(this);
		outer_roof_3_r3.setPivot(17.3F, -37.6423F, 0.0F);
		outer_roof_2.addChild(outer_roof_3_r3);
		setRotationAngle(outer_roof_3_r3, 0.0F, 0.0F, 1.0472F);
		outer_roof_3_r3.setTextureOffset(162, 196).addCuboid(-1.0F, 0.0F, -12.0F, 2.0F, 1.0F, 20.0F, 0.0F, true);

		roof_light = new ModelPart(this);
		roof_light.setPivot(0.0F, 24.0F, 0.0F);


		roof_light_r1 = new ModelPart(this);
		roof_light_r1.setPivot(-7.0F, -33.5F, 0.0F);
		roof_light.addChild(roof_light_r1);
		setRotationAngle(roof_light_r1, 0.0F, 0.0F, 0.1745F);
		roof_light_r1.setTextureOffset(154, 0).addCuboid(-3.0F, -1.0F, -24.0F, 3.0F, 1.0F, 48.0F, 0.0F, false);

		roof_end_light = new ModelPart(this);
		roof_end_light.setPivot(0.0F, 24.0F, 0.0F);


		roof_light_2_r1 = new ModelPart(this);
		roof_light_2_r1.setPivot(7.0F, -33.5F, 0.0F);
		roof_end_light.addChild(roof_light_2_r1);
		setRotationAngle(roof_light_2_r1, 0.0F, 0.0F, -0.1745F);
		roof_light_2_r1.setTextureOffset(166, 12).addCuboid(0.0F, -1.0F, 4.0F, 3.0F, 1.0F, 36.0F, 0.0F, false);

		roof_light_1_r1 = new ModelPart(this);
		roof_light_1_r1.setPivot(-7.0F, -33.5F, 0.0F);
		roof_end_light.addChild(roof_light_1_r1);
		setRotationAngle(roof_light_1_r1, 0.0F, 0.0F, 0.1745F);
		roof_light_1_r1.setTextureOffset(166, 12).addCuboid(-3.0F, -1.0F, 4.0F, 3.0F, 1.0F, 36.0F, 0.0F, false);

		head = new ModelPart(this);
		head.setPivot(0.0F, 24.0F, 0.0F);
		head.setTextureOffset(180, 168).addCuboid(-20.0F, 0.0F, 4.0F, 40.0F, 1.0F, 4.0F, 0.0F, false);
		head.setTextureOffset(90, 75).addCuboid(18.0F, -14.0F, 4.0F, 3.0F, 14.0F, 6.0F, 0.0F, true);
		head.setTextureOffset(90, 75).addCuboid(-21.0F, -14.0F, 4.0F, 3.0F, 14.0F, 6.0F, 0.0F, false);
		head.setTextureOffset(208, 0).addCuboid(-18.0F, -34.0F, 4.0F, 36.0F, 34.0F, 0.0F, 0.0F, false);

		upper_wall_2_r4 = new ModelPart(this);
		upper_wall_2_r4.setPivot(-21.0F, -14.0F, 0.0F);
		head.addChild(upper_wall_2_r4);
		setRotationAngle(upper_wall_2_r4, 0.0F, 0.0F, 0.1396F);
		upper_wall_2_r4.setTextureOffset(0, 72).addCuboid(0.0F, -19.0F, 4.0F, 3.0F, 19.0F, 6.0F, 0.0F, false);

		upper_wall_1_r4 = new ModelPart(this);
		upper_wall_1_r4.setPivot(21.0F, -14.0F, 0.0F);
		head.addChild(upper_wall_1_r4);
		setRotationAngle(upper_wall_1_r4, 0.0F, 0.0F, -0.1396F);
		upper_wall_1_r4.setTextureOffset(0, 72).addCuboid(-3.0F, -19.0F, 4.0F, 3.0F, 19.0F, 6.0F, 0.0F, true);

		head_exterior = new ModelPart(this);
		head_exterior.setPivot(0.0F, 24.0F, 0.0F);
		head_exterior.setTextureOffset(142, 53).addCuboid(-20.0F, 0.0F, -18.0F, 40.0F, 1.0F, 22.0F, 0.0F, false);
		head_exterior.setTextureOffset(232, 110).addCuboid(18.0F, -14.0F, -18.0F, 3.0F, 14.0F, 28.0F, 0.0F, true);
		head_exterior.setTextureOffset(232, 110).addCuboid(-21.0F, -14.0F, -18.0F, 3.0F, 14.0F, 28.0F, 0.0F, false);
		head_exterior.setTextureOffset(200, 76).addCuboid(-18.0F, -34.0F, 3.0F, 36.0F, 34.0F, 0.0F, 0.0F, false);

		upper_wall_2_r5 = new ModelPart(this);
		upper_wall_2_r5.setPivot(-21.0F, -14.0F, 0.0F);
		head_exterior.addChild(upper_wall_2_r5);
		setRotationAngle(upper_wall_2_r5, 0.0F, 0.0F, 0.1396F);
		upper_wall_2_r5.setTextureOffset(178, 199).addCuboid(0.0F, -23.0F, -18.0F, 3.0F, 23.0F, 28.0F, 0.0F, false);

		upper_wall_1_r5 = new ModelPart(this);
		upper_wall_1_r5.setPivot(21.0F, -14.0F, 0.0F);
		head_exterior.addChild(upper_wall_1_r5);
		setRotationAngle(upper_wall_1_r5, 0.0F, 0.0F, -0.1396F);
		upper_wall_1_r5.setTextureOffset(178, 199).addCuboid(-3.0F, -23.0F, -18.0F, 3.0F, 23.0F, 28.0F, 0.0F, true);

		floor_2_r3 = new ModelPart(this);
		floor_2_r3.setPivot(-21.0F, 0.0F, 0.0F);
		head_exterior.addChild(floor_2_r3);
		setRotationAngle(floor_2_r3, 0.0F, 0.0F, -0.1745F);
		floor_2_r3.setTextureOffset(96, 258).addCuboid(0.0F, 0.0F, -18.0F, 1.0F, 8.0F, 26.0F, 0.0F, false);

		floor_1_r3 = new ModelPart(this);
		floor_1_r3.setPivot(21.0F, 0.0F, 0.0F);
		head_exterior.addChild(floor_1_r3);
		setRotationAngle(floor_1_r3, 0.0F, 0.0F, 0.1745F);
		floor_1_r3.setTextureOffset(96, 258).addCuboid(-1.0F, 0.0F, -18.0F, 1.0F, 8.0F, 26.0F, 0.0F, true);

		front = new ModelPart(this);
		front.setPivot(0.0F, 0.0F, 0.0F);
		head_exterior.addChild(front);
		front.setTextureOffset(58, 49).addCuboid(-6.0F, 0.3452F, -39.9877F, 12.0F, 2.0F, 0.0F, 0.0F, false);
		front.setTextureOffset(208, 34).addCuboid(-21.0F, 0.0F, -18.0F, 42.0F, 8.0F, 0.0F, 0.0F, false);

		front_bottom_5_r1 = new ModelPart(this);
		front_bottom_5_r1.setPivot(0.0F, 5.5837F, -28.2322F);
		front.addChild(front_bottom_5_r1);
		setRotationAngle(front_bottom_5_r1, 1.3963F, 0.0F, 0.0F);
		front_bottom_5_r1.setTextureOffset(248, 49).addCuboid(-20.0F, -10.5F, 0.0F, 40.0F, 21.0F, 0.0F, 0.0F, false);

		front_bottom_4_r1 = new ModelPart(this);
		front_bottom_4_r1.setPivot(0.0F, 3.053F, -39.2803F);
		front.addChild(front_bottom_4_r1);
		setRotationAngle(front_bottom_4_r1, 0.7854F, 0.0F, 0.0F);
		front_bottom_4_r1.setTextureOffset(0, 98).addCuboid(-12.0F, -1.0F, 0.0F, 24.0F, 2.0F, 0.0F, 0.0F, false);

		front_bottom_2_r1 = new ModelPart(this);
		front_bottom_2_r1.setPivot(0.0F, -0.5418F, -39.5259F);
		front.addChild(front_bottom_2_r1);
		setRotationAngle(front_bottom_2_r1, -0.48F, 0.0F, 0.0F);
		front_bottom_2_r1.setTextureOffset(58, 120).addCuboid(-9.0F, -1.0F, 0.0F, 18.0F, 2.0F, 0.0F, 0.0F, false);

		front_bottom_1_r1 = new ModelPart(this);
		front_bottom_1_r1.setPivot(0.0F, -4.0F, -36.0F);
		front.addChild(front_bottom_1_r1);
		setRotationAngle(front_bottom_1_r1, -0.8727F, 0.0F, 0.0F);
		front_bottom_1_r1.setTextureOffset(58, 96).addCuboid(-12.0F, 0.0F, 0.0F, 24.0F, 4.0F, 0.0F, 0.0F, false);

		front_panel_3_r1 = new ModelPart(this);
		front_panel_3_r1.setPivot(0.0F, -33.4483F, -25.7447F);
		front.addChild(front_panel_3_r1);
		setRotationAngle(front_panel_3_r1, -0.6283F, 0.0F, 0.0F);
		front_panel_3_r1.setTextureOffset(232, 152).addCuboid(-12.0F, -5.5F, 0.0F, 24.0F, 11.0F, 0.0F, 0.0F, false);

		front_panel_2_r1 = new ModelPart(this);
		front_panel_2_r1.setPivot(0.0F, -21.3902F, -31.4497F);
		front.addChild(front_panel_2_r1);
		setRotationAngle(front_panel_2_r1, -0.3142F, 0.0F, 0.0F);
		front_panel_2_r1.setTextureOffset(90, 232).addCuboid(-12.0F, -8.0F, 0.0F, 24.0F, 16.0F, 0.0F, 0.0F, false);

		front_panel_1_r1 = new ModelPart(this);
		front_panel_1_r1.setPivot(0.0F, -4.0F, -36.0F);
		front.addChild(front_panel_1_r1);
		setRotationAngle(front_panel_1_r1, -0.2094F, 0.0F, 0.0F);
		front_panel_1_r1.setTextureOffset(200, 110).addCuboid(-12.0F, -10.0F, 0.0F, 24.0F, 10.0F, 0.0F, 0.0F, false);

		side_1 = new ModelPart(this);
		side_1.setPivot(0.0F, 0.0F, 0.0F);
		front.addChild(side_1);


		front_middle_top_r1 = new ModelPart(this);
		front_middle_top_r1.setPivot(0.0F, -41.5712F, -12.0F);
		side_1.addChild(front_middle_top_r1);
		setRotationAngle(front_middle_top_r1, 0.3491F, 0.0F, 0.0F);
		front_middle_top_r1.setTextureOffset(46, 75).addCuboid(-0.6613F, 0.001F, -12.0F, 6.0F, 0.0F, 12.0F, 0.0F, true);

		front_side_bottom_2_r1 = new ModelPart(this);
		front_side_bottom_2_r1.setPivot(18.3378F, 3.5923F, -29.3251F);
		side_1.addChild(front_side_bottom_2_r1);
		setRotationAngle(front_side_bottom_2_r1, 0.0F, 0.1745F, 0.1745F);
		front_side_bottom_2_r1.setTextureOffset(112, 157).addCuboid(0.0F, -4.0F, -6.5F, 0.0F, 8.0F, 18.0F, 0.0F, true);

		outer_roof_8_r1 = new ModelPart(this);
		outer_roof_8_r1.setPivot(15.889F, -37.9304F, -17.4163F);
		side_1.addChild(outer_roof_8_r1);
		setRotationAngle(outer_roof_8_r1, 0.1745F, 0.0F, 0.5236F);
		outer_roof_8_r1.setTextureOffset(13, 12).addCuboid(-2.5F, 0.0F, -3.5F, 5.0F, 0.0F, 9.0F, 0.0F, true);

		outer_roof_7_r1 = new ModelPart(this);
		outer_roof_7_r1.setPivot(17.2991F, -37.6418F, 0.0F);
		side_1.addChild(outer_roof_7_r1);
		setRotationAngle(outer_roof_7_r1, 0.0F, 0.0F, -0.5236F);
		outer_roof_7_r1.setTextureOffset(22, 25).addCuboid(0.0F, -1.0F, -19.0F, 0.0F, 2.0F, 7.0F, 0.0F, true);

		outer_roof_6_r1 = new ModelPart(this);
		outer_roof_6_r1.setPivot(11.8461F, -38.1654F, -18.1077F);
		side_1.addChild(outer_roof_6_r1);
		setRotationAngle(outer_roof_6_r1, 0.3491F, 0.0F, 0.1745F);
		outer_roof_6_r1.setTextureOffset(121, 139).addCuboid(-7.0F, 0.0F, -6.5F, 14.0F, 0.0F, 13.0F, 0.0F, true);

		front_panel_8_r1 = new ModelPart(this);
		front_panel_8_r1.setPivot(12.3948F, 1.3452F, -37.1405F);
		side_1.addChild(front_panel_8_r1);
		setRotationAngle(front_panel_8_r1, 0.0F, -0.4189F, 0.0F);
		front_panel_8_r1.setTextureOffset(212, 217).addCuboid(-7.0F, -4.0F, 0.0F, 14.0F, 8.0F, 0.0F, 0.0F, true);

		front_panel_7_r1 = new ModelPart(this);
		front_panel_7_r1.setPivot(12.0F, -4.0F, -36.0F);
		side_1.addChild(front_panel_7_r1);
		setRotationAngle(front_panel_7_r1, -0.5236F, -0.3491F, 0.0F);
		front_panel_7_r1.setTextureOffset(142, 116).addCuboid(-5.0F, 0.0F, 0.0F, 14.0F, 4.0F, 0.0F, 0.0F, true);

		front_panel_6_r1 = new ModelPart(this);
		front_panel_6_r1.setPivot(12.0F, -4.0F, -36.0F);
		side_1.addChild(front_panel_6_r1);
		setRotationAngle(front_panel_6_r1, -0.2094F, -0.3491F, 0.0F);
		front_panel_6_r1.setTextureOffset(162, 196).addCuboid(0.0F, -12.0F, 0.0F, 8.0F, 12.0F, 0.0F, 0.0F, true);

		front_panel_5_r1 = new ModelPart(this);
		front_panel_5_r1.setPivot(12.0F, -13.7818F, -33.9218F);
		side_1.addChild(front_panel_5_r1);
		setRotationAngle(front_panel_5_r1, -0.3142F, -0.3491F, 0.0F);
		front_panel_5_r1.setTextureOffset(112, 139).addCuboid(0.0F, -17.0F, 0.0F, 8.0F, 17.0F, 0.0F, 0.0F, true);

		front_panel_4_r1 = new ModelPart(this);
		front_panel_4_r1.setPivot(12.0F, -28.9987F, -28.9776F);
		side_1.addChild(front_panel_4_r1);
		setRotationAngle(front_panel_4_r1, -0.6283F, -0.3491F, 0.0F);
		front_panel_4_r1.setTextureOffset(0, 228).addCuboid(0.0F, -11.0F, 0.0F, 8.0F, 11.0F, 0.0F, 0.0F, true);

		front_side_upper_2_r1 = new ModelPart(this);
		front_side_upper_2_r1.setPivot(21.0F, -14.0F, -18.0F);
		side_1.addChild(front_side_upper_2_r1);
		setRotationAngle(front_side_upper_2_r1, 0.0F, 0.1745F, -0.1396F);
		front_side_upper_2_r1.setTextureOffset(0, 35).addCuboid(0.0F, -23.0F, -14.0F, 0.0F, 23.0F, 14.0F, 0.0F, true);

		front_side_lower_2_r1 = new ModelPart(this);
		front_side_lower_2_r1.setPivot(21.0F, 0.0F, -18.0F);
		side_1.addChild(front_side_lower_2_r1);
		setRotationAngle(front_side_lower_2_r1, 0.0F, 0.1745F, 0.0F);
		front_side_lower_2_r1.setTextureOffset(0, 103).addCuboid(0.0F, -14.0F, -17.0F, 0.0F, 14.0F, 17.0F, 0.0F, true);

		side_2 = new ModelPart(this);
		side_2.setPivot(-21.0F, 0.0F, -9.0F);
		front.addChild(side_2);


		front_middle_top_r2 = new ModelPart(this);
		front_middle_top_r2.setPivot(21.0F, -41.5712F, -3.0F);
		side_2.addChild(front_middle_top_r2);
		setRotationAngle(front_middle_top_r2, 0.3491F, 0.0F, 0.0F);
		front_middle_top_r2.setTextureOffset(46, 75).addCuboid(-5.3387F, 0.001F, -12.0F, 6.0F, 0.0F, 12.0F, 0.0F, false);

		front_side_bottom_2_r2 = new ModelPart(this);
		front_side_bottom_2_r2.setPivot(2.6622F, 3.5923F, -20.3251F);
		side_2.addChild(front_side_bottom_2_r2);
		setRotationAngle(front_side_bottom_2_r2, 0.0F, -0.1745F, -0.1745F);
		front_side_bottom_2_r2.setTextureOffset(112, 157).addCuboid(0.0F, -4.0F, -6.5F, 0.0F, 8.0F, 18.0F, 0.0F, false);

		outer_roof_8_r2 = new ModelPart(this);
		outer_roof_8_r2.setPivot(5.111F, -37.9304F, -8.4163F);
		side_2.addChild(outer_roof_8_r2);
		setRotationAngle(outer_roof_8_r2, 0.1745F, 0.0F, -0.5236F);
		outer_roof_8_r2.setTextureOffset(13, 12).addCuboid(-2.5F, 0.0F, -3.5F, 5.0F, 0.0F, 9.0F, 0.0F, false);

		outer_roof_7_r2 = new ModelPart(this);
		outer_roof_7_r2.setPivot(3.7009F, -37.6418F, 9.0F);
		side_2.addChild(outer_roof_7_r2);
		setRotationAngle(outer_roof_7_r2, 0.0F, 0.0F, 0.5236F);
		outer_roof_7_r2.setTextureOffset(22, 25).addCuboid(0.0F, -1.0F, -19.0F, 0.0F, 2.0F, 7.0F, 0.0F, false);

		outer_roof_6_r2 = new ModelPart(this);
		outer_roof_6_r2.setPivot(9.1539F, -38.1654F, -9.1077F);
		side_2.addChild(outer_roof_6_r2);
		setRotationAngle(outer_roof_6_r2, 0.3491F, 0.0F, -0.1745F);
		outer_roof_6_r2.setTextureOffset(121, 139).addCuboid(-7.0F, 0.0F, -6.5F, 14.0F, 0.0F, 13.0F, 0.0F, false);

		front_panel_8_r2 = new ModelPart(this);
		front_panel_8_r2.setPivot(8.6052F, 1.3452F, -28.1405F);
		side_2.addChild(front_panel_8_r2);
		setRotationAngle(front_panel_8_r2, 0.0F, 0.4189F, 0.0F);
		front_panel_8_r2.setTextureOffset(212, 217).addCuboid(-7.0F, -4.0F, 0.0F, 14.0F, 8.0F, 0.0F, 0.0F, false);

		front_panel_7_r2 = new ModelPart(this);
		front_panel_7_r2.setPivot(9.0F, -4.0F, -27.0F);
		side_2.addChild(front_panel_7_r2);
		setRotationAngle(front_panel_7_r2, -0.5236F, 0.3491F, 0.0F);
		front_panel_7_r2.setTextureOffset(142, 116).addCuboid(-9.0F, 0.0F, 0.0F, 14.0F, 4.0F, 0.0F, 0.0F, false);

		front_panel_6_r2 = new ModelPart(this);
		front_panel_6_r2.setPivot(9.0F, -4.0F, -27.0F);
		side_2.addChild(front_panel_6_r2);
		setRotationAngle(front_panel_6_r2, -0.2094F, 0.3491F, 0.0F);
		front_panel_6_r2.setTextureOffset(162, 196).addCuboid(-8.0F, -12.0F, 0.0F, 8.0F, 12.0F, 0.0F, 0.0F, false);

		front_panel_5_r2 = new ModelPart(this);
		front_panel_5_r2.setPivot(9.0F, -13.7818F, -24.9218F);
		side_2.addChild(front_panel_5_r2);
		setRotationAngle(front_panel_5_r2, -0.3142F, 0.3491F, 0.0F);
		front_panel_5_r2.setTextureOffset(112, 139).addCuboid(-8.0F, -17.0F, 0.0F, 8.0F, 17.0F, 0.0F, 0.0F, false);

		front_panel_4_r2 = new ModelPart(this);
		front_panel_4_r2.setPivot(9.0F, -28.9987F, -19.9776F);
		side_2.addChild(front_panel_4_r2);
		setRotationAngle(front_panel_4_r2, -0.6283F, 0.3491F, 0.0F);
		front_panel_4_r2.setTextureOffset(0, 228).addCuboid(-8.0F, -11.0F, 0.0F, 8.0F, 11.0F, 0.0F, 0.0F, false);

		front_side_upper_2_r2 = new ModelPart(this);
		front_side_upper_2_r2.setPivot(0.0F, -14.0F, -9.0F);
		side_2.addChild(front_side_upper_2_r2);
		setRotationAngle(front_side_upper_2_r2, 0.0F, -0.1745F, 0.1396F);
		front_side_upper_2_r2.setTextureOffset(0, 35).addCuboid(0.0F, -23.0F, -14.0F, 0.0F, 23.0F, 14.0F, 0.0F, false);

		front_side_lower_2_r2 = new ModelPart(this);
		front_side_lower_2_r2.setPivot(0.0F, 0.0F, -9.0F);
		side_2.addChild(front_side_lower_2_r2);
		setRotationAngle(front_side_lower_2_r2, 0.0F, -0.1745F, 0.0F);
		front_side_lower_2_r2.setTextureOffset(0, 103).addCuboid(0.0F, -14.0F, -17.0F, 0.0F, 14.0F, 17.0F, 0.0F, false);

		headlights = new ModelPart(this);
		headlights.setPivot(0.0F, 24.0F, 0.0F);
		headlights.setTextureOffset(30, 36).addCuboid(8.0F, -1.0F, -28.1F, 4.0F, 4.0F, 0.0F, 0.0F, false);
		headlights.setTextureOffset(8, 36).addCuboid(-12.0F, -1.0F, -28.1F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		tail_lights = new ModelPart(this);
		tail_lights.setPivot(0.0F, 24.0F, 0.0F);
		tail_lights.setTextureOffset(0, 36).addCuboid(10.0F, -1.0F, -28.1F, 4.0F, 4.0F, 0.0F, 0.0F, false);
		tail_lights.setTextureOffset(22, 34).addCuboid(-14.0F, -1.0F, -28.1F, 4.0F, 4.0F, 0.0F, 0.0F, false);
	}

	private static final int DOOR_MAX = 14;
	private static final ModelDoorOverlay MODEL_DOOR_OVERLAY = new ModelDoorOverlay();

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
				renderMirror(side_panel, matrices, vertices, light, position - 21.9F);
				renderMirror(side_panel, matrices, vertices, light, position + 21.9F);
				break;
			case EXTERIOR:
				if (position == getWindowPositions()[0] && isEnd1Head) {
					renderOnceFlipped(window_exterior_end, matrices, vertices, light, position);
				} else if (position == getWindowPositions()[getWindowPositions().length - 1] && isEnd2Head) {
					renderOnce(window_exterior_end, matrices, vertices, light, position);
				} else {
					renderMirror(window_exterior, matrices, vertices, light, position);
				}
				renderMirror(roof_exterior, matrices, vertices, light, position);
				break;
		}
	}

	@Override
	protected void renderDoorPositions(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, float doorLeftValue, float doorRightValue, boolean isEnd1Head, boolean isEnd2Head) {
		final float doorLeftZ = doorLeftValue * DOOR_MAX;
		final float doorRightZ = doorRightValue * DOOR_MAX;
		final float doorLeftX = doorLeftValue < 0.05 ? -doorLeftValue * 20 - 0.01F : -1.01F;
		final float doorRightX = doorRightValue < 0.05 ? -doorRightValue * 20 - 0.01F : -1.01F;

		final boolean notLastDoor = getDoorPositions().length > 4 && position != getDoorPositions()[0] && position != getDoorPositions()[4];

		switch (renderStage) {
			case LIGHTS:
				if (notLastDoor) {
					renderMirror(roof_light, matrices, vertices, light, position);
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
				final boolean door1End = position == getDoorPositions()[0] && isEnd1Head;
				final boolean door2End = position == getDoorPositions()[getDoorPositions().length - 1] && isEnd2Head;

				if (door1End || door2End) {
					door_left_exterior_end.setPivot(doorRightX, 0, doorRightZ);
					door_right_exterior_end.setPivot(doorRightX, 0, -doorRightZ);
					renderOnce(door_exterior_end, matrices, vertices, light, position);
				} else {
					door_left_exterior.setPivot(doorRightX, 0, doorRightZ);
					door_right_exterior.setPivot(doorRightX, 0, -doorRightZ);
					renderOnce(door_exterior, matrices, vertices, light, position);
				}

				if (door1End || door2End) {
					door_left_exterior_end.setPivot(doorLeftX, 0, doorLeftZ);
					door_right_exterior_end.setPivot(doorLeftX, 0, -doorLeftZ);
					renderOnceFlipped(door_exterior_end, matrices, vertices, light, position);
				} else {
					door_left_exterior.setPivot(doorLeftX, 0, doorLeftZ);
					door_right_exterior.setPivot(doorLeftX, 0, -doorLeftZ);
					renderOnceFlipped(door_exterior, matrices, vertices, light, position);
				}

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
	protected ModelDoorOverlayBase getModelDoorOverlay() {
		return MODEL_DOOR_OVERLAY;
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

	private static class ModelDoorOverlay extends ModelDoorOverlayBase {

		private final ModelPart door_left_overlay_interior;
		private final ModelPart door_left_top_2_r1;
		private final ModelPart door_right_overlay_interior;
		private final ModelPart door_right_top_2_r1;
		private final ModelPart door_right_bottom_r1;
		private final ModelPart door_left_overlay_exterior;
		private final ModelPart door_left_top_r1;
		private final ModelPart door_right_overlay_exterior;
		private final ModelPart door_right_top_r1;
		private final ModelPart wall_1;
		private final ModelPart upper_wall_2_r1;
		private final ModelPart wall_2;
		private final ModelPart upper_wall_4_r1;

		public ModelDoorOverlay() {
			textureWidth = 32;
			textureHeight = 32;
			door_left_overlay_interior = new ModelPart(this);
			door_left_overlay_interior.setPivot(0.0F, 24.0F, 0.0F);
			door_left_overlay_interior.setTextureOffset(0, 3).addCuboid(-19.7F, -14.5F, 0.0F, 0.0F, 12.0F, 16.0F, 0.0F, false);

			door_left_top_2_r1 = new ModelPart(this);
			door_left_top_2_r1.setPivot(-20.8F, -14.0F, 0.0F);
			door_left_overlay_interior.addChild(door_left_top_2_r1);
			setRotationAngle(door_left_top_2_r1, 0.0F, 0.0F, 0.1396F);
			door_left_top_2_r1.setTextureOffset(26, 1).addCuboid(1.1F, -19.0F, 12.0F, 0.0F, 19.0F, 2.0F, 0.0F, false);
			door_left_top_2_r1.setTextureOffset(0, -16).addCuboid(1.1F, -18.5F, 0.5F, 0.0F, 19.0F, 16.0F, 0.0F, false);

			door_right_overlay_interior = new ModelPart(this);
			door_right_overlay_interior.setPivot(0.0F, 24.0F, 0.0F);


			door_right_top_2_r1 = new ModelPart(this);
			door_right_top_2_r1.setPivot(-20.8F, -14.0F, 0.0F);
			door_right_overlay_interior.addChild(door_right_top_2_r1);
			setRotationAngle(door_right_top_2_r1, 0.0F, 3.1416F, 0.1396F);
			door_right_top_2_r1.setTextureOffset(1, 1).addCuboid(-1.1F, -19.0F, 13.0F, 0.0F, 19.0F, 2.0F, 0.0F, false);
			door_right_top_2_r1.setTextureOffset(0, -16).addCuboid(-1.1F, -18.5F, 0.5F, 0.0F, 19.0F, 16.0F, 0.0F, false);

			door_right_bottom_r1 = new ModelPart(this);
			door_right_bottom_r1.setPivot(0.0F, 0.0F, 0.0F);
			door_right_overlay_interior.addChild(door_right_bottom_r1);
			setRotationAngle(door_right_bottom_r1, 0.0F, 3.1416F, 0.0F);
			door_right_bottom_r1.setTextureOffset(0, 3).addCuboid(19.7F, -14.5F, 0.0F, 0.0F, 12.0F, 16.0F, 0.0F, false);

			door_left_overlay_exterior = new ModelPart(this);
			door_left_overlay_exterior.setPivot(0.0F, 24.0F, 0.0F);


			door_left_top_r1 = new ModelPart(this);
			door_left_top_r1.setPivot(-20.8F, -14.0F, 0.0F);
			door_left_overlay_exterior.addChild(door_left_top_r1);
			setRotationAngle(door_left_top_r1, 0.0F, 0.0F, 0.1396F);
			door_left_top_r1.setTextureOffset(0, -13).addCuboid(0.1F, -15.5F, 0.5F, 0.0F, 16.0F, 16.0F, 0.0F, false);

			door_right_overlay_exterior = new ModelPart(this);
			door_right_overlay_exterior.setPivot(0.0F, 24.0F, 0.0F);


			door_right_top_r1 = new ModelPart(this);
			door_right_top_r1.setPivot(-20.8F, -14.0F, 0.0F);
			door_right_overlay_exterior.addChild(door_right_top_r1);
			setRotationAngle(door_right_top_r1, 0.0F, 3.1416F, 0.1396F);
			door_right_top_r1.setTextureOffset(0, -13).addCuboid(-0.1F, -15.5F, 0.5F, 0.0F, 16.0F, 16.0F, 0.0F, false);

			wall_1 = new ModelPart(this);
			wall_1.setPivot(0.0F, 24.0F, 0.0F);
			wall_1.setTextureOffset(27, 19).addCuboid(-20.25F, -14.0F, -13.9F, 2.0F, 9.0F, 0.0F, 0.0F, false);

			upper_wall_2_r1 = new ModelPart(this);
			upper_wall_2_r1.setPivot(-20.0F, -14.0F, 0.0F);
			wall_1.addChild(upper_wall_2_r1);
			setRotationAngle(upper_wall_2_r1, 0.0F, 0.0F, 0.1396F);
			upper_wall_2_r1.setTextureOffset(27, 0).addCuboid(-0.25F, -19.0F, -13.89F, 2.0F, 3.0F, 0.0F, 0.0F, false);
			upper_wall_2_r1.setTextureOffset(27, 2).addCuboid(-0.25F, -3.0F, -13.89F, 2.0F, 3.0F, 0.0F, 0.0F, false);

			wall_2 = new ModelPart(this);
			wall_2.setPivot(0.0F, 24.0F, 0.0F);
			wall_2.setTextureOffset(1, 19).addCuboid(-20.25F, -14.0F, 13.9F, 2.0F, 9.0F, 0.0F, 0.0F, false);

			upper_wall_4_r1 = new ModelPart(this);
			upper_wall_4_r1.setPivot(-20.0F, -14.0F, 0.0F);
			wall_2.addChild(upper_wall_4_r1);
			setRotationAngle(upper_wall_4_r1, 0.0F, 0.0F, 0.1396F);
			upper_wall_4_r1.setTextureOffset(1, 0).addCuboid(-0.25F, -19.0F, 13.89F, 2.0F, 3.0F, 0.0F, 0.0F, false);
			upper_wall_4_r1.setTextureOffset(1, 2).addCuboid(-0.25F, -3.0F, 13.89F, 2.0F, 3.0F, 0.0F, 0.0F, false);
		}

		@Override
		protected void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, RenderStage renderStage, int light, int position, float doorLeftValue, float doorRightValue) {
			final float doorLeftZ = doorLeftValue * DOOR_MAX;
			final float doorRightZ = doorRightValue * DOOR_MAX;
			final float doorLeftX = doorLeftValue < 0.05 ? -doorLeftValue * 20 - 0.01F : -1.01F;
			final float doorRightX = doorRightValue < 0.05 ? -doorRightValue * 20 - 0.01F : -1.01F;
			switch (renderStage) {
				case INTERIOR:
					ModelTrainBase.renderOnce(door_left_overlay_interior, matrices, vertexConsumers.getBuffer(MoreRenderLayers.getInterior(DOOR_OVERLAY_TEXTURE_RIGHT)), light, doorRightX, position + doorRightZ);
					ModelTrainBase.renderOnce(door_right_overlay_interior, matrices, vertexConsumers.getBuffer(MoreRenderLayers.getInterior(DOOR_OVERLAY_TEXTURE_LEFT)), light, doorRightX, position - doorRightZ);
					ModelTrainBase.renderOnceFlipped(door_left_overlay_interior, matrices, vertexConsumers.getBuffer(MoreRenderLayers.getInterior(DOOR_OVERLAY_TEXTURE_RIGHT)), light, doorLeftX, position - doorLeftZ);
					ModelTrainBase.renderOnceFlipped(door_right_overlay_interior, matrices, vertexConsumers.getBuffer(MoreRenderLayers.getInterior(DOOR_OVERLAY_TEXTURE_LEFT)), light, doorLeftX, position + doorLeftZ);
					ModelTrainBase.renderMirror(wall_1, matrices, vertexConsumers.getBuffer(MoreRenderLayers.getInterior(DOOR_OVERLAY_TEXTURE_RIGHT)), light, position);
					ModelTrainBase.renderMirror(wall_2, matrices, vertexConsumers.getBuffer(MoreRenderLayers.getInterior(DOOR_OVERLAY_TEXTURE_LEFT)), light, position);
					break;
				case EXTERIOR:
					ModelTrainBase.renderOnce(door_left_overlay_exterior, matrices, vertexConsumers.getBuffer(MoreRenderLayers.getExterior(DOOR_OVERLAY_TEXTURE_LEFT)), light / 4 * 3, doorRightX, position + doorRightZ);
					ModelTrainBase.renderOnce(door_right_overlay_exterior, matrices, vertexConsumers.getBuffer(MoreRenderLayers.getExterior(DOOR_OVERLAY_TEXTURE_RIGHT)), light / 4 * 3, doorRightX, position - doorRightZ);
					ModelTrainBase.renderOnceFlipped(door_left_overlay_exterior, matrices, vertexConsumers.getBuffer(MoreRenderLayers.getExterior(DOOR_OVERLAY_TEXTURE_LEFT)), light / 4 * 3, doorLeftX, position - doorLeftZ);
					ModelTrainBase.renderOnceFlipped(door_right_overlay_exterior, matrices, vertexConsumers.getBuffer(MoreRenderLayers.getExterior(DOOR_OVERLAY_TEXTURE_RIGHT)), light / 4 * 3, doorLeftX, position + doorLeftZ);
					break;
			}
		}
	}
}