package mtr.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mtr.client.DoorAnimationType;
import mtr.client.ScrollingText;
import mtr.data.Route;
import mtr.data.Station;
import mtr.mappings.ModelDataWrapper;
import mtr.mappings.ModelMapper;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;

import java.util.List;

public class ModelR211 extends ModelSimpleTrainBase<ModelR211> {

	private final ModelMapper window_exterior;
	private final ModelMapper upper_wall_r1;
	private final ModelMapper window_handrails;
	private final ModelMapper headrail_right;
	private final ModelMapper handrail_turn_1_r1;
	private final ModelMapper handrail_middle_4_r1;
	private final ModelMapper handrail_middle_3_r1;
	private final ModelMapper handrail_middle_2_r1;
	private final ModelMapper handrail_turn_1_r2;
	private final ModelMapper headrail_left;
	private final ModelMapper handrail_turn_1_r3;
	private final ModelMapper handrail_middle_4_r2;
	private final ModelMapper handrail_middle_3_r2;
	private final ModelMapper handrail_middle_2_r2;
	private final ModelMapper handrail_turn_1_r4;
	private final ModelMapper handrail_mid;
	private final ModelMapper handrail_middle_4_r3;
	private final ModelMapper handrail_middle_3_r3;
	private final ModelMapper handrail_middle_2_r3;
	private final ModelMapper handrail_turn_1_r5;
	private final ModelMapper headrail_up;
	private final ModelMapper handrail_up_3_r1;
	private final ModelMapper handrail_top_1_r1;
	private final ModelMapper handrail_top_2_r1;
	private final ModelMapper seat;
	private final ModelMapper seat_bottom_r1;
	private final ModelMapper seat_back_3_r1;
	private final ModelMapper window_exterior_end;
	private final ModelMapper upper_wall_2_r1;
	private final ModelMapper upper_wall_1_r1;
	private final ModelMapper window;
	private final ModelMapper wall_1_r1;
	private final ModelMapper side_panel;
	private final ModelMapper end;
	private final ModelMapper upper_wall_2_r2;
	private final ModelMapper upper_wall_1_r2;
	private final ModelMapper end_exterior;
	private final ModelMapper upper_wall_2_r3;
	private final ModelMapper upper_wall_1_r3;
	private final ModelMapper end_bottom_out;
	private final ModelMapper buttom_panel_right_2_r1;
	private final ModelMapper buttom_panel_left_2_r1;
	private final ModelMapper end_back;
	private final ModelMapper front_right_panel_4_r1;
	private final ModelMapper front_right_panel_3_r1;
	private final ModelMapper roof_end_exterior;
	private final ModelMapper outer_roof_6_r1;
	private final ModelMapper outer_roof_5_r1;
	private final ModelMapper outer_roof_4_r1;
	private final ModelMapper outer_roof_3_r1;
	private final ModelMapper outer_roof_2_r1;
	private final ModelMapper outer_roof_5_r2;
	private final ModelMapper outer_roof_4_r2;
	private final ModelMapper outer_roof_3_r2;
	private final ModelMapper outer_roof_2_r2;
	private final ModelMapper outer_roof_1_r1;
	private final ModelMapper end_gangway;
	private final ModelMapper end_gangway_exterior;
	private final ModelMapper upper_wall_3_r1;
	private final ModelMapper upper_wall_2_r4;
	private final ModelMapper roof_end_gangway_exterior;
	private final ModelMapper outer_roof_7_r1;
	private final ModelMapper outer_roof_6_r2;
	private final ModelMapper outer_roof_5_r3;
	private final ModelMapper outer_roof_4_r3;
	private final ModelMapper outer_roof_3_r3;
	private final ModelMapper outer_roof_6_r3;
	private final ModelMapper outer_roof_5_r4;
	private final ModelMapper outer_roof_4_r4;
	private final ModelMapper outer_roof_3_r4;
	private final ModelMapper outer_roof_2_r3;
	private final ModelMapper door;
	private final ModelMapper door_edge_4_r1;
	private final ModelMapper door_edge_3_r1;
	private final ModelMapper door_edge_2_r1;
	private final ModelMapper door_right;
	private final ModelMapper door_right_top_r1;
	private final ModelMapper door_left;
	private final ModelMapper door_side_top_r1;
	private final ModelMapper door_exterior;
	private final ModelMapper door_right_exterior;
	private final ModelMapper door_right_top_r2;
	private final ModelMapper door_left_exterior;
	private final ModelMapper door_left_top_r1;
	private final ModelMapper door_sides;
	private final ModelMapper door_side_top_2_r1;
	private final ModelMapper door_side_top_1_r1;
	private final ModelMapper door_end_exterior;
	private final ModelMapper door_right_exterior_end;
	private final ModelMapper door_right_top_r3;
	private final ModelMapper door_left_exterior_end;
	private final ModelMapper door_left_top_r2;
	private final ModelMapper door_sides_end;
	private final ModelMapper door_side_top_3_r1;
	private final ModelMapper door_side_top_2_r2;
	private final ModelMapper handrail_door;
	private final ModelMapper handrail_curve;
	private final ModelMapper handrail_curve_12_r1;
	private final ModelMapper handrail_curve_10_r1;
	private final ModelMapper handrail_curve_9_r1;
	private final ModelMapper handrail_curve_7_r1;
	private final ModelMapper handrail_curve_6_r1;
	private final ModelMapper handrail_curve_5_r1;
	private final ModelMapper handrail_curve_3_r1;
	private final ModelMapper handrail_curve_11_r1;
	private final ModelMapper handrail_curve_9_r2;
	private final ModelMapper handrail_curve_8_r1;
	private final ModelMapper handrail_curve_5_r2;
	private final ModelMapper handrail_curve_4_r1;
	private final ModelMapper handrail_curve_2_r1;
	private final ModelMapper head;
	private final ModelMapper upper_wall_2_r5;
	private final ModelMapper upper_wall_1_r4;
	private final ModelMapper head_exterior;
	private final ModelMapper upper_wall_2_r6;
	private final ModelMapper upper_wall_1_r5;
	private final ModelMapper bumper;
	private final ModelMapper bumper_2_r1;
	private final ModelMapper bumper_1_r1;
	private final ModelMapper head_back;
	private final ModelMapper front_right_panel_3_r2;
	private final ModelMapper front_right_panel_2_r1;
	private final ModelMapper roof_head_exterior;
	private final ModelMapper outer_roof_7_r2;
	private final ModelMapper outer_roof_6_r4;
	private final ModelMapper outer_roof_5_r5;
	private final ModelMapper outer_roof_4_r5;
	private final ModelMapper outer_roof_3_r5;
	private final ModelMapper outer_roof_6_r5;
	private final ModelMapper outer_roof_5_r6;
	private final ModelMapper outer_roof_4_r6;
	private final ModelMapper outer_roof_3_r6;
	private final ModelMapper outer_roof_2_r4;
	private final ModelMapper roof_exterior;
	private final ModelMapper outer_roof_5_r7;
	private final ModelMapper outer_roof_4_r7;
	private final ModelMapper outer_roof_3_r7;
	private final ModelMapper outer_roof_2_r5;
	private final ModelMapper outer_roof_1_r2;
	private final ModelMapper roof_door;
	private final ModelMapper inner_roof_4_r1;
	private final ModelMapper inner_roof_2_r1;
	private final ModelMapper roof_window;
	private final ModelMapper inner_roof_9_r1;
	private final ModelMapper inner_roof_6_r1;
	private final ModelMapper inner_roof_4_r2;
	private final ModelMapper inner_roof_3_r1;
	private final ModelMapper roof_end;
	private final ModelMapper side_1;
	private final ModelMapper inner_roof_5_r1;
	private final ModelMapper inner_roof_3_r2;
	private final ModelMapper side_2;
	private final ModelMapper inner_roof_7_r1;
	private final ModelMapper inner_roof_6_r2;
	private final ModelMapper mid_roof;
	private final ModelMapper roof_end_gangway;
	private final ModelMapper mid_roof_gangway;
	private final ModelMapper roof_light;
	private final ModelMapper destination_display_end_interior;
	private final ModelMapper display_6_r1;
	private final ModelMapper display_5_r1;
	private final ModelMapper display_4_r1;
	private final ModelMapper display_3_r1;
	private final ModelMapper roof_head;
	private final ModelMapper inner_roof_6_r3;
	private final ModelMapper inner_roof_3_r3;
	private final ModelMapper destination_display;
	private final ModelMapper display_7_r1;
	private final ModelMapper display_6_r2;
	private final ModelMapper headlights;
	private final ModelMapper headlights_2_r1;
	private final ModelMapper headlights_1_r1;
	private final ModelMapper tail_lights;
	private final ModelMapper tail_lights_2_r1;
	private final ModelMapper tail_lights_1_r1;
	private final ModelMapper door_light_interior_off;
	private final ModelMapper light_2_r1;
	private final ModelMapper door_light_interior_on;
	private final ModelMapper light_3_r1;

	protected final boolean openGangway;

	public ModelR211(boolean openGangway) {
		this(openGangway, DoorAnimationType.R211, true);
	}

	protected ModelR211(boolean openGangway, DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		super(doorAnimationType, renderDoorOverlay);
		this.openGangway = openGangway;

		final int textureWidth = 360;
		final int textureHeight = 360;

		final ModelDataWrapper modelDataWrapper = new ModelDataWrapper(this, textureWidth, textureHeight);

		window_exterior = new ModelMapper(modelDataWrapper);
		window_exterior.setPos(0, 24, 0);
		window_exterior.texOffs(0, 212).addBox(-21.5F, 0, -24, 1, 3, 48, 0, false);
		window_exterior.texOffs(112, 0).addBox(-21.5F, -13, -27, 1, 13, 54, 0, false);

		upper_wall_r1 = new ModelMapper(modelDataWrapper);
		upper_wall_r1.setPos(-21.5F, -13, 0);
		window_exterior.addChild(upper_wall_r1);
		setRotationAngle(upper_wall_r1, 0, 0, 0.1047F);
		upper_wall_r1.texOffs(56, 23).addBox(0, -23, -27, 1, 23, 54, 0, false);

		window_handrails = new ModelMapper(modelDataWrapper);
		window_handrails.setPos(0, 24, 0);


		headrail_right = new ModelMapper(modelDataWrapper);
		headrail_right.setPos(0, 0, 0);
		window_handrails.addChild(headrail_right);
		headrail_right.texOffs(343, 0).addBox(-19.5F, -4.5F, 24, 8, 0, 0, 0.2F, false);

		handrail_turn_1_r1 = new ModelMapper(modelDataWrapper);
		handrail_turn_1_r1.setPos(-10.9858F, -31.518F, 23.5636F);
		headrail_right.addChild(handrail_turn_1_r1);
		setRotationAngle(handrail_turn_1_r1, -0.7854F, 0, -0.1745F);
		handrail_turn_1_r1.texOffs(343, 0).addBox(0, 0, -0.5F, 0, 0, 1, 0.2F, false);

		handrail_middle_4_r1 = new ModelMapper(modelDataWrapper);
		handrail_middle_4_r1.setPos(-10.1951F, -27.0336F, 24);
		headrail_right.addChild(handrail_middle_4_r1);
		setRotationAngle(handrail_middle_4_r1, 0, 0, -0.1745F);
		handrail_middle_4_r1.texOffs(359, 27).addBox(0, -4, 0, 0, 8, 0, 0.2F, false);

		handrail_middle_3_r1 = new ModelMapper(modelDataWrapper);
		handrail_middle_3_r1.setPos(-9.3199F, -21.2212F, 24);
		headrail_right.addChild(handrail_middle_3_r1);
		setRotationAngle(handrail_middle_3_r1, 0, 0, -0.0873F);
		handrail_middle_3_r1.texOffs(342, 0).addBox(0, -1.5F, 0, 0, 3, 0, 0.2F, false);

		handrail_middle_2_r1 = new ModelMapper(modelDataWrapper);
		handrail_middle_2_r1.setPos(-9.7993F, -12.3899F, 24);
		headrail_right.addChild(handrail_middle_2_r1);
		setRotationAngle(handrail_middle_2_r1, 0, 0, 0.0873F);
		handrail_middle_2_r1.texOffs(359, 30).addBox(0, -7, 0, 0, 14, 0, 0.2F, false);

		handrail_turn_1_r2 = new ModelMapper(modelDataWrapper);
		handrail_turn_1_r2.setPos(-11.3F, -4.3F, 0);
		headrail_right.addChild(handrail_turn_1_r2);
		setRotationAngle(handrail_turn_1_r2, 0, 0, 0.8727F);
		handrail_turn_1_r2.texOffs(343, 0).addBox(-0.2F, -1.2F, 24, 0, 1, 0, 0.2F, false);

		headrail_left = new ModelMapper(modelDataWrapper);
		headrail_left.setPos(0, 0, 0);
		window_handrails.addChild(headrail_left);
		headrail_left.texOffs(343, 0).addBox(-19.5F, -4.5F, -24, 8, 0, 0, 0.2F, false);

		handrail_turn_1_r3 = new ModelMapper(modelDataWrapper);
		handrail_turn_1_r3.setPos(-10.9858F, -31.518F, -23.5636F);
		headrail_left.addChild(handrail_turn_1_r3);
		setRotationAngle(handrail_turn_1_r3, 0.7854F, 0, -0.1745F);
		handrail_turn_1_r3.texOffs(343, 0).addBox(0, 0, -0.5F, 0, 0, 1, 0.2F, false);

		handrail_middle_4_r2 = new ModelMapper(modelDataWrapper);
		handrail_middle_4_r2.setPos(-10.1951F, -27.0336F, -24);
		headrail_left.addChild(handrail_middle_4_r2);
		setRotationAngle(handrail_middle_4_r2, 0, 0, -0.1745F);
		handrail_middle_4_r2.texOffs(359, 27).addBox(0, -4, 0, 0, 8, 0, 0.2F, false);

		handrail_middle_3_r2 = new ModelMapper(modelDataWrapper);
		handrail_middle_3_r2.setPos(-9.3199F, -21.2212F, -24);
		headrail_left.addChild(handrail_middle_3_r2);
		setRotationAngle(handrail_middle_3_r2, 0, 0, -0.0873F);
		handrail_middle_3_r2.texOffs(342, 0).addBox(0, -1.5F, 0, 0, 3, 0, 0.2F, false);

		handrail_middle_2_r2 = new ModelMapper(modelDataWrapper);
		handrail_middle_2_r2.setPos(-9.7993F, -12.3899F, -24);
		headrail_left.addChild(handrail_middle_2_r2);
		setRotationAngle(handrail_middle_2_r2, 0, 0, 0.0873F);
		handrail_middle_2_r2.texOffs(359, 30).addBox(0, -7, 0, 0, 14, 0, 0.2F, false);

		handrail_turn_1_r4 = new ModelMapper(modelDataWrapper);
		handrail_turn_1_r4.setPos(-11.3F, -4.3F, 0);
		headrail_left.addChild(handrail_turn_1_r4);
		setRotationAngle(handrail_turn_1_r4, 0, 0, 0.8727F);
		handrail_turn_1_r4.texOffs(343, 0).addBox(-0.2F, -1.2F, -24, 0, 1, 0, 0.2F, false);

		handrail_mid = new ModelMapper(modelDataWrapper);
		handrail_mid.setPos(0, 0, 0);
		window_handrails.addChild(handrail_mid);
		handrail_mid.texOffs(343, 0).addBox(-16.5F, -4.5F, -3.5F, 5, 0, 0, 0.2F, false);

		handrail_middle_4_r3 = new ModelMapper(modelDataWrapper);
		handrail_middle_4_r3.setPos(-10.5424F, -29.0032F, -3.5F);
		handrail_mid.addChild(handrail_middle_4_r3);
		setRotationAngle(handrail_middle_4_r3, 0, 0, -0.1745F);
		handrail_middle_4_r3.texOffs(359, 27).addBox(0, -3, 0, 0, 9, 0, 0.2F, false);

		handrail_middle_3_r3 = new ModelMapper(modelDataWrapper);
		handrail_middle_3_r3.setPos(-9.3199F, -21.2212F, -3.5F);
		handrail_mid.addChild(handrail_middle_3_r3);
		setRotationAngle(handrail_middle_3_r3, 0, 0, -0.0873F);
		handrail_middle_3_r3.texOffs(342, 0).addBox(0, -1.5F, 0, 0, 3, 0, 0.2F, false);

		handrail_middle_2_r3 = new ModelMapper(modelDataWrapper);
		handrail_middle_2_r3.setPos(-9.7993F, -12.3899F, -3.5F);
		handrail_mid.addChild(handrail_middle_2_r3);
		setRotationAngle(handrail_middle_2_r3, 0, 0, 0.0873F);
		handrail_middle_2_r3.texOffs(359, 30).addBox(0, -7, 0, 0, 14, 0, 0.2F, false);

		handrail_turn_1_r5 = new ModelMapper(modelDataWrapper);
		handrail_turn_1_r5.setPos(-11.3F, -4.3F, 0);
		handrail_mid.addChild(handrail_turn_1_r5);
		setRotationAngle(handrail_turn_1_r5, 0, 0, 0.8727F);
		handrail_turn_1_r5.texOffs(343, 0).addBox(-0.2F, -1.2F, -3.5F, 0, 1, 0, 0.2F, false);

		headrail_up = new ModelMapper(modelDataWrapper);
		headrail_up.setPos(0, 0, 0);
		window_handrails.addChild(headrail_up);
		headrail_up.texOffs(343, 0).addBox(-10.6132F, -35.3491F, 0, 0, 1, 0, 0.2F, false);
		headrail_up.texOffs(343, 0).addBox(-10.6132F, -35.3491F, -20, 0, 1, 0, 0.2F, false);
		headrail_up.texOffs(343, 0).addBox(-10.6132F, -35.3491F, 20, 0, 1, 0, 0.2F, false);

		handrail_up_3_r1 = new ModelMapper(modelDataWrapper);
		handrail_up_3_r1.setPos(-10.8185F, -33.002F, 0);
		headrail_up.addChild(handrail_up_3_r1);
		setRotationAngle(handrail_up_3_r1, 0, 0, 0.1745F);
		handrail_up_3_r1.texOffs(343, 0).addBox(0, -1, 20, 0, 2, 0, 0.2F, false);
		handrail_up_3_r1.texOffs(343, 0).addBox(0, -1, -20, 0, 2, 0, 0.2F, false);
		handrail_up_3_r1.texOffs(343, 0).addBox(0, -1, 0, 0, 2, 0, 0.2F, false);

		handrail_top_1_r1 = new ModelMapper(modelDataWrapper);
		handrail_top_1_r1.setPos(-11.0616F, -31.9478F, -11.5101F);
		headrail_up.addChild(handrail_top_1_r1);
		setRotationAngle(handrail_top_1_r1, -1.5708F, 0, -0.1745F);
		handrail_top_1_r1.texOffs(359, 30).addBox(0, -11.5F, 0, 0, 23, 0, 0.2F, false);

		handrail_top_2_r1 = new ModelMapper(modelDataWrapper);
		handrail_top_2_r1.setPos(-11.0616F, -31.9478F, 11.5101F);
		headrail_up.addChild(handrail_top_2_r1);
		setRotationAngle(handrail_top_2_r1, -1.5708F, 0, -0.1745F);
		handrail_top_2_r1.texOffs(359, 30).addBox(0, -11.5F, 0, 0, 23, 0, 0.2F, false);

		seat = new ModelMapper(modelDataWrapper);
		seat.setPos(0, 0, 0);
		window_handrails.addChild(seat);
		seat.texOffs(122, 168).addBox(-19.9F, -10.75F, -24, 2, 5, 48, 0, false);
		seat.texOffs(175, 174).addBox(-19.55F, -6, -23.5F, 3, 4, 47, 0, false);

		seat_bottom_r1 = new ModelMapper(modelDataWrapper);
		seat_bottom_r1.setPos(0, -1.75F, 2.5F);
		seat.addChild(seat_bottom_r1);
		setRotationAngle(seat_bottom_r1, 0, 0, -0.0873F);
		seat_bottom_r1.texOffs(56, 167).addBox(-19.9F, -6, -26.5F, 9, 1, 48, 0, false);

		seat_back_3_r1 = new ModelMapper(modelDataWrapper);
		seat_back_3_r1.setPos(-17.9F, -10.75F, 2.5F);
		seat.addChild(seat_back_3_r1);
		setRotationAngle(seat_back_3_r1, 0, 0, -0.1309F);
		seat_back_3_r1.texOffs(168, 0).addBox(-2, -5, -26.5F, 2, 5, 48, 0, false);

		window_exterior_end = new ModelMapper(modelDataWrapper);
		window_exterior_end.setPos(0, 24, 0);
		window_exterior_end.texOffs(192, 53).addBox(-21.5F, 0, -24, 1, 3, 48, 0, false);
		window_exterior_end.texOffs(192, 53).addBox(20.5F, 0, -24, 1, 3, 48, 0, true);
		window_exterior_end.texOffs(82, 100).addBox(-21.5F, -13, -27, 1, 13, 54, 0, false);
		window_exterior_end.texOffs(82, 100).addBox(20.5F, -13, -27, 1, 13, 54, 0, true);

		upper_wall_2_r1 = new ModelMapper(modelDataWrapper);
		upper_wall_2_r1.setPos(-9.3302F, -9.7596F, 0);
		window_exterior_end.addChild(upper_wall_2_r1);
		setRotationAngle(upper_wall_2_r1, 0, 0, -0.1047F);
		upper_wall_2_r1.texOffs(0, 0).addBox(30, -23, -27, 1, 23, 54, 0, true);

		upper_wall_1_r1 = new ModelMapper(modelDataWrapper);
		upper_wall_1_r1.setPos(-21.5F, -13, 0);
		window_exterior_end.addChild(upper_wall_1_r1);
		setRotationAngle(upper_wall_1_r1, 0, 0, 0.1047F);
		upper_wall_1_r1.texOffs(0, 0).addBox(0, -23, -27, 1, 23, 54, 0, false);

		window = new ModelMapper(modelDataWrapper);
		window.setPos(0, 24, 0);
		window.texOffs(0, 100).addBox(-20, 0, -24, 20, 1, 48, 0, false);
		window.texOffs(0, 149).addBox(-21.5F, -13, -25, 2, 13, 50, 0, false);

		wall_1_r1 = new ModelMapper(modelDataWrapper);
		wall_1_r1.setPos(-21.5F, -13, 0);
		window.addChild(wall_1_r1);
		setRotationAngle(wall_1_r1, 0, 0, 0.1047F);
		wall_1_r1.texOffs(138, 67).addBox(0, -21, -25, 2, 21, 50, 0, false);

		side_panel = new ModelMapper(modelDataWrapper);
		side_panel.setPos(0, 24, 0);
		side_panel.texOffs(0, 314).addBox(-20, -32, 0, 11, 29, 0, 0, false);

		end = new ModelMapper(modelDataWrapper);
		end.setPos(0, 24, 0);
		end.texOffs(149, 225).addBox(-20.5F, 0, -9, 41, 1, 17, 0, false);
		end.texOffs(285, 296).addBox(-7, -33, -9, 14, 33, 0, 0, false);
		end.texOffs(114, 294).addBox(7, -33, -9, 13, 33, 1, 0, true);
		end.texOffs(114, 294).addBox(-20, -33, -9, 13, 33, 1, 0, false);
		end.texOffs(48, 40).addBox(19.5F, -13, 8, 2, 13, 1, 0, false);
		end.texOffs(48, 40).addBox(-21.5F, -13, 8, 2, 13, 1, 0, true);
		end.texOffs(122, 167).addBox(-20.5F, -34, -8, 3, 21, 16, 0, false);
		end.texOffs(122, 167).addBox(17.5F, -34, -8, 3, 21, 16, 0, true);
		end.texOffs(168, 0).addBox(-19.5F, -13, -8, 5, 13, 16, 0, false);
		end.texOffs(168, 0).addBox(14.5F, -13, -8, 5, 13, 16, 0, true);

		upper_wall_2_r2 = new ModelMapper(modelDataWrapper);
		upper_wall_2_r2.setPos(-21.5F, -13, 0);
		end.addChild(upper_wall_2_r2);
		setRotationAngle(upper_wall_2_r2, 0, 0, 0.1047F);
		upper_wall_2_r2.texOffs(14, 90).addBox(0, -21, 8, 2, 21, 1, 0, true);

		upper_wall_1_r2 = new ModelMapper(modelDataWrapper);
		upper_wall_1_r2.setPos(21.5F, -13, 0);
		end.addChild(upper_wall_1_r2);
		setRotationAngle(upper_wall_1_r2, 0, 0, -0.1047F);
		upper_wall_1_r2.texOffs(14, 90).addBox(-2, -21, 8, 2, 21, 1, 0, false);

		end_exterior = new ModelMapper(modelDataWrapper);
		end_exterior.setPos(0, 24, 0);
		end_exterior.texOffs(147, 81).addBox(20.5F, 0, -11, 1, 3, 19, 0, false);
		end_exterior.texOffs(0, 18).addBox(19.5F, -13, -11, 2, 13, 22, 0, false);
		end_exterior.texOffs(147, 81).addBox(-21.5F, 0, -11, 1, 3, 19, 0, true);
		end_exterior.texOffs(0, 18).addBox(-21.5F, -13, -11, 2, 13, 22, 0, true);

		upper_wall_2_r3 = new ModelMapper(modelDataWrapper);
		upper_wall_2_r3.setPos(-21.5F, -13, 0);
		end_exterior.addChild(upper_wall_2_r3);
		setRotationAngle(upper_wall_2_r3, 0, 0, 0.1047F);
		upper_wall_2_r3.texOffs(0, 95).addBox(0, -23, -11, 2, 23, 22, 0, true);

		upper_wall_1_r3 = new ModelMapper(modelDataWrapper);
		upper_wall_1_r3.setPos(21.5F, -13, 0);
		end_exterior.addChild(upper_wall_1_r3);
		setRotationAngle(upper_wall_1_r3, 0, 0, -0.1047F);
		upper_wall_1_r3.texOffs(0, 95).addBox(-2, -23, -11, 2, 23, 22, 0, false);

		end_bottom_out = new ModelMapper(modelDataWrapper);
		end_bottom_out.setPos(0, 0, 0);
		end_exterior.addChild(end_bottom_out);
		end_bottom_out.texOffs(54, 191).addBox(-0.057F, 0, -14.3646F, 9, 3, 5, 0, false);
		end_bottom_out.texOffs(54, 191).addBox(-8.943F, 0, -14.3646F, 9, 3, 5, 0, true);

		buttom_panel_right_2_r1 = new ModelMapper(modelDataWrapper);
		buttom_panel_right_2_r1.setPos(-21.5F, 0, -11);
		end_bottom_out.addChild(buttom_panel_right_2_r1);
		setRotationAngle(buttom_panel_right_2_r1, 0, 0.2618F, 0);
		buttom_panel_right_2_r1.texOffs(0, 140).addBox(0, 0, 0, 13, 3, 3, 0, true);

		buttom_panel_left_2_r1 = new ModelMapper(modelDataWrapper);
		buttom_panel_left_2_r1.setPos(21.5F, 0, -11);
		end_bottom_out.addChild(buttom_panel_left_2_r1);
		setRotationAngle(buttom_panel_left_2_r1, 0, -0.2618F, 0);
		buttom_panel_left_2_r1.texOffs(0, 140).addBox(-13, 0, 0, 13, 3, 3, 0, false);

		end_back = new ModelMapper(modelDataWrapper);
		end_back.setPos(0, 0, 0);
		end_exterior.addChild(end_back);
		end_back.texOffs(158, 0).addBox(-8, -33, -13, 1, 33, 3, 0, false);
		end_back.texOffs(158, 0).addBox(7, -33, -13, 1, 33, 3, 0, true);
		end_back.texOffs(138, 103).addBox(-8, -42, -13, 16, 9, 4, 0, false);
		end_back.texOffs(86, 291).addBox(-7, -33, -11, 14, 33, 0, 0, false);

		front_right_panel_4_r1 = new ModelMapper(modelDataWrapper);
		front_right_panel_4_r1.setPos(8, -42, -13);
		end_back.addChild(front_right_panel_4_r1);
		setRotationAngle(front_right_panel_4_r1, 0, -0.192F, 0);
		front_right_panel_4_r1.texOffs(58, 291).addBox(0, 0, 0, 14, 42, 0, 0, true);

		front_right_panel_3_r1 = new ModelMapper(modelDataWrapper);
		front_right_panel_3_r1.setPos(-8, -42, -13);
		end_back.addChild(front_right_panel_3_r1);
		setRotationAngle(front_right_panel_3_r1, 0, 0.192F, 0);
		front_right_panel_3_r1.texOffs(58, 291).addBox(-14, 0, 0, 14, 42, 0, 0, false);

		roof_end_exterior = new ModelMapper(modelDataWrapper);
		roof_end_exterior.setPos(0, 0, 0);
		end_exterior.addChild(roof_end_exterior);
		roof_end_exterior.texOffs(105, 100).addBox(-4, -41.375F, -15, 4, 0, 23, 0, false);
		roof_end_exterior.texOffs(105, 100).addBox(0, -41.375F, -15, 4, 0, 23, 0, true);

		outer_roof_6_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_6_r1.setPos(4, -41.375F, 0);
		roof_end_exterior.addChild(outer_roof_6_r1);
		setRotationAngle(outer_roof_6_r1, 0, 0, 0.0873F);
		outer_roof_6_r1.texOffs(119, 0).addBox(0, 0, -15, 6, 0, 23, 0, true);

		outer_roof_5_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_5_r1.setPos(12.392F, -40.205F, 0);
		roof_end_exterior.addChild(outer_roof_5_r1);
		setRotationAngle(outer_roof_5_r1, 0, 0, 0.2618F);
		outer_roof_5_r1.texOffs(23, 77).addBox(-2.5F, 0, -15, 5, 0, 23, 0, true);

		outer_roof_4_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_4_r1.setPos(16.2163F, -39.0449F, 0);
		roof_end_exterior.addChild(outer_roof_4_r1);
		setRotationAngle(outer_roof_4_r1, 0, 0, 0.3491F);
		outer_roof_4_r1.texOffs(105, 123).addBox(-1.5F, 0, -15, 3, 0, 23, 0, true);

		outer_roof_3_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_3_r1.setPos(18.2687F, -37.7659F, 0);
		roof_end_exterior.addChild(outer_roof_3_r1);
		setRotationAngle(outer_roof_3_r1, 0, 0, 0.8727F);
		outer_roof_3_r1.texOffs(131, 0).addBox(-1, 0, -15, 2, 0, 23, 0, true);

		outer_roof_2_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_2_r1.setPos(18.6114F, -35.9228F, 0);
		roof_end_exterior.addChild(outer_roof_2_r1);
		setRotationAngle(outer_roof_2_r1, 0, 0, 1.3788F);
		outer_roof_2_r1.texOffs(166, 285).addBox(-1, -0.5F, -15, 2, 1, 23, 0, true);

		outer_roof_5_r2 = new ModelMapper(modelDataWrapper);
		outer_roof_5_r2.setPos(-4, -41.375F, 0);
		roof_end_exterior.addChild(outer_roof_5_r2);
		setRotationAngle(outer_roof_5_r2, 0, 0, -0.0873F);
		outer_roof_5_r2.texOffs(119, 0).addBox(-6, 0, -15, 6, 0, 23, 0, false);

		outer_roof_4_r2 = new ModelMapper(modelDataWrapper);
		outer_roof_4_r2.setPos(-12.392F, -40.205F, 0);
		roof_end_exterior.addChild(outer_roof_4_r2);
		setRotationAngle(outer_roof_4_r2, 0, 0, -0.2618F);
		outer_roof_4_r2.texOffs(23, 77).addBox(-2.5F, 0, -15, 5, 0, 23, 0, false);

		outer_roof_3_r2 = new ModelMapper(modelDataWrapper);
		outer_roof_3_r2.setPos(-16.2163F, -39.0449F, 0);
		roof_end_exterior.addChild(outer_roof_3_r2);
		setRotationAngle(outer_roof_3_r2, 0, 0, -0.3491F);
		outer_roof_3_r2.texOffs(105, 123).addBox(-1.5F, 0, -15, 3, 0, 23, 0, false);

		outer_roof_2_r2 = new ModelMapper(modelDataWrapper);
		outer_roof_2_r2.setPos(-18.2687F, -37.7659F, 0);
		roof_end_exterior.addChild(outer_roof_2_r2);
		setRotationAngle(outer_roof_2_r2, 0, 0, -0.8727F);
		outer_roof_2_r2.texOffs(131, 0).addBox(-1, 0, -15, 2, 0, 23, 0, false);

		outer_roof_1_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_1_r1.setPos(-18.6114F, -35.9228F, 0);
		roof_end_exterior.addChild(outer_roof_1_r1);
		setRotationAngle(outer_roof_1_r1, 0, 0, -1.3788F);
		outer_roof_1_r1.texOffs(166, 285).addBox(-1, -0.5F, -15, 2, 1, 23, 0, false);

		end_gangway = new ModelMapper(modelDataWrapper);
		end_gangway.setPos(0, 24, 0);
		end_gangway.texOffs(50, 221).addBox(-20.5F, 0, -9, 41, 1, 17, 0, false);
		end_gangway.texOffs(0, 263).addBox(8.5F, -32.875F, -9, 11, 33, 18, 0, false);
		end_gangway.texOffs(131, 243).addBox(-19.5F, -32.875F, -9, 11, 33, 18, 0, false);

		end_gangway_exterior = new ModelMapper(modelDataWrapper);
		end_gangway_exterior.setPos(0, 24, 0);
		end_gangway_exterior.texOffs(303, 211).addBox(20.5F, 0, -9, 1, 3, 17, 0, false);
		end_gangway_exterior.texOffs(54, 149).addBox(19.5F, -13, -9, 2, 13, 20, 0, false);
		end_gangway_exterior.texOffs(303, 211).addBox(-21.5F, 0, -9, 1, 3, 17, 0, true);
		end_gangway_exterior.texOffs(54, 149).addBox(-21.5F, -13, -9, 2, 13, 20, 0, true);
		end_gangway_exterior.texOffs(231, 301).addBox(8.5F, -33, -9, 12, 33, 0, 0, false);
		end_gangway_exterior.texOffs(231, 301).addBox(-20.5F, -33, -9, 12, 33, 0, 0, true);
		end_gangway_exterior.texOffs(192, 104).addBox(-20, -41.875F, -9, 20, 9, 0, 0, false);
		end_gangway_exterior.texOffs(192, 104).addBox(0, -41.875F, -9, 20, 9, 0, 0, true);

		upper_wall_3_r1 = new ModelMapper(modelDataWrapper);
		upper_wall_3_r1.setPos(-21.5F, -13, 0);
		end_gangway_exterior.addChild(upper_wall_3_r1);
		setRotationAngle(upper_wall_3_r1, 0, 0, 0.1047F);
		upper_wall_3_r1.texOffs(0, 149).addBox(0, -23, -9, 2, 23, 20, 0, true);

		upper_wall_2_r4 = new ModelMapper(modelDataWrapper);
		upper_wall_2_r4.setPos(21.5F, -13, 0);
		end_gangway_exterior.addChild(upper_wall_2_r4);
		setRotationAngle(upper_wall_2_r4, 0, 0, -0.1047F);
		upper_wall_2_r4.texOffs(0, 149).addBox(-2, -23, -9, 2, 23, 20, 0, false);

		roof_end_gangway_exterior = new ModelMapper(modelDataWrapper);
		roof_end_gangway_exterior.setPos(0, 0, 0);
		end_gangway_exterior.addChild(roof_end_gangway_exterior);
		roof_end_gangway_exterior.texOffs(50, 239).addBox(-4, -41.375F, -9, 4, 1, 17, 0, false);
		roof_end_gangway_exterior.texOffs(50, 239).addBox(0, -41.375F, -9, 4, 1, 17, 0, true);

		outer_roof_7_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_7_r1.setPos(4, -41.375F, 0);
		roof_end_gangway_exterior.addChild(outer_roof_7_r1);
		setRotationAngle(outer_roof_7_r1, 0, 0, 0.0873F);
		outer_roof_7_r1.texOffs(88, 129).addBox(0, 0, -9, 6, 1, 17, 0, true);

		outer_roof_6_r2 = new ModelMapper(modelDataWrapper);
		outer_roof_6_r2.setPos(12.392F, -40.205F, 0);
		roof_end_gangway_exterior.addChild(outer_roof_6_r2);
		setRotationAngle(outer_roof_6_r2, 0, 0, 0.2618F);
		outer_roof_6_r2.texOffs(192, 67).addBox(-2.5F, 0, -9, 5, 1, 17, 0, true);

		outer_roof_5_r3 = new ModelMapper(modelDataWrapper);
		outer_roof_5_r3.setPos(16.2163F, -39.0449F, 0);
		roof_end_gangway_exterior.addChild(outer_roof_5_r3);
		setRotationAngle(outer_roof_5_r3, 0, 0, 0.3491F);
		outer_roof_5_r3.texOffs(294, 0).addBox(-1.5F, 0, -9, 3, 1, 17, 0, true);

		outer_roof_4_r3 = new ModelMapper(modelDataWrapper);
		outer_roof_4_r3.setPos(18.2687F, -37.7659F, 0);
		roof_end_gangway_exterior.addChild(outer_roof_4_r3);
		setRotationAngle(outer_roof_4_r3, 0, 0, 0.8727F);
		outer_roof_4_r3.texOffs(301, 130).addBox(-1, 0, -9, 2, 1, 17, 0, true);

		outer_roof_3_r3 = new ModelMapper(modelDataWrapper);
		outer_roof_3_r3.setPos(18.6114F, -35.9228F, 0);
		roof_end_gangway_exterior.addChild(outer_roof_3_r3);
		setRotationAngle(outer_roof_3_r3, 0, 0, 1.3788F);
		outer_roof_3_r3.texOffs(303, 193).addBox(-1, -0.5F, -9, 2, 1, 17, 0, true);

		outer_roof_6_r3 = new ModelMapper(modelDataWrapper);
		outer_roof_6_r3.setPos(-4, -41.375F, 0);
		roof_end_gangway_exterior.addChild(outer_roof_6_r3);
		setRotationAngle(outer_roof_6_r3, 0, 0, -0.0873F);
		outer_roof_6_r3.texOffs(88, 129).addBox(-6, 0, -9, 6, 1, 17, 0, false);

		outer_roof_5_r4 = new ModelMapper(modelDataWrapper);
		outer_roof_5_r4.setPos(-12.392F, -40.205F, 0);
		roof_end_gangway_exterior.addChild(outer_roof_5_r4);
		setRotationAngle(outer_roof_5_r4, 0, 0, -0.2618F);
		outer_roof_5_r4.texOffs(192, 67).addBox(-2.5F, 0, -9, 5, 1, 17, 0, false);

		outer_roof_4_r4 = new ModelMapper(modelDataWrapper);
		outer_roof_4_r4.setPos(-16.2163F, -39.0449F, 0);
		roof_end_gangway_exterior.addChild(outer_roof_4_r4);
		setRotationAngle(outer_roof_4_r4, 0, 0, -0.3491F);
		outer_roof_4_r4.texOffs(294, 0).addBox(-1.5F, 0, -9, 3, 1, 17, 0, false);

		outer_roof_3_r4 = new ModelMapper(modelDataWrapper);
		outer_roof_3_r4.setPos(-18.2687F, -37.7659F, 0);
		roof_end_gangway_exterior.addChild(outer_roof_3_r4);
		setRotationAngle(outer_roof_3_r4, 0, 0, -0.8727F);
		outer_roof_3_r4.texOffs(301, 130).addBox(-1, 0, -9, 2, 1, 17, 0, false);

		outer_roof_2_r3 = new ModelMapper(modelDataWrapper);
		outer_roof_2_r3.setPos(-18.6114F, -35.9228F, 0);
		roof_end_gangway_exterior.addChild(outer_roof_2_r3);
		setRotationAngle(outer_roof_2_r3, 0, 0, -1.3788F);
		outer_roof_2_r3.texOffs(303, 193).addBox(-1, -0.5F, -9, 2, 1, 17, 0, false);

		door = new ModelMapper(modelDataWrapper);
		door.setPos(0, 24, 0);
		door.texOffs(220, 0).addBox(-21, 0, -16, 21, 1, 32, 0, false);

		door_edge_4_r1 = new ModelMapper(modelDataWrapper);
		door_edge_4_r1.setPos(-21.5F, -13, 15);
		door.addChild(door_edge_4_r1);
		setRotationAngle(door_edge_4_r1, 0, 1.5708F, 0.1047F);
		door_edge_4_r1.texOffs(0, 90).addBox(0, -21, 0.5F, 2, 21, 2, 0, false);
		door_edge_4_r1.texOffs(0, 90).addBox(28, -21, 0.5F, 2, 21, 2, 0, true);

		door_edge_3_r1 = new ModelMapper(modelDataWrapper);
		door_edge_3_r1.setPos(-20, -6.5F, 14);
		door.addChild(door_edge_3_r1);
		setRotationAngle(door_edge_3_r1, 0, 1.5708F, 0);
		door_edge_3_r1.texOffs(96, 126).addBox(-1, -6.5F, -1, 2, 13, 2, 0, false);

		door_edge_2_r1 = new ModelMapper(modelDataWrapper);
		door_edge_2_r1.setPos(-20, -6.5F, -14);
		door.addChild(door_edge_2_r1);
		setRotationAngle(door_edge_2_r1, 0, 1.5708F, 0);
		door_edge_2_r1.texOffs(96, 126).addBox(-1, -6.5F, -1, 2, 13, 2, 0, true);

		door_right = new ModelMapper(modelDataWrapper);
		door_right.setPos(0, 0, 0);
		door.addChild(door_right);
		door_right.texOffs(269, 212).addBox(-21, -13, 0, 1, 13, 13, 0, false);

		door_right_top_r1 = new ModelMapper(modelDataWrapper);
		door_right_top_r1.setPos(-21, -13, 0);
		door_right.addChild(door_right_top_r1);
		setRotationAngle(door_right_top_r1, 0, 0, 0.1047F);
		door_right_top_r1.texOffs(203, 296).addBox(0, -20, 0, 1, 20, 13, 0, false);

		door_left = new ModelMapper(modelDataWrapper);
		door_left.setPos(0, 0, 0);
		door.addChild(door_left);
		door_left.texOffs(220, 0).addBox(-21, -13, -13, 1, 13, 13, 0, false);

		door_side_top_r1 = new ModelMapper(modelDataWrapper);
		door_side_top_r1.setPos(-21, -13, -2);
		door_left.addChild(door_side_top_r1);
		setRotationAngle(door_side_top_r1, 0, 0, 0.1047F);
		door_side_top_r1.texOffs(142, 296).addBox(0, -20, -11, 1, 20, 13, 0, false);

		door_exterior = new ModelMapper(modelDataWrapper);
		door_exterior.setPos(0, 24, 0);


		door_right_exterior = new ModelMapper(modelDataWrapper);
		door_right_exterior.setPos(0, 0, 0);
		door_exterior.addChild(door_right_exterior);
		door_right_exterior.texOffs(164, 125).addBox(-21, -13, 0, 0, 13, 13, 0, false);

		door_right_top_r2 = new ModelMapper(modelDataWrapper);
		door_right_top_r2.setPos(-21, -13, 0);
		door_right_exterior.addChild(door_right_top_r2);
		setRotationAngle(door_right_top_r2, 0, 0, 0.1047F);
		door_right_top_r2.texOffs(222, 230).addBox(0, -20, 0, 0, 20, 13, 0, false);

		door_left_exterior = new ModelMapper(modelDataWrapper);
		door_left_exterior.setPos(0, 0, 0);
		door_exterior.addChild(door_left_exterior);
		door_left_exterior.texOffs(144, 155).addBox(-21, -13, -13, 0, 13, 13, 0, false);

		door_left_top_r1 = new ModelMapper(modelDataWrapper);
		door_left_top_r1.setPos(-21, -13, 0);
		door_left_exterior.addChild(door_left_top_r1);
		setRotationAngle(door_left_top_r1, 0, 0, 0.1047F);
		door_left_top_r1.texOffs(189, 230).addBox(0, -20, -13, 0, 20, 13, 0, false);

		door_sides = new ModelMapper(modelDataWrapper);
		door_sides.setPos(0, 0, 0);
		door_exterior.addChild(door_sides);
		door_sides.texOffs(269, 210).addBox(-21.5F, 0, -16, 1, 3, 32, 0, false);
		door_sides.texOffs(282, 266).addBox(-22, 0, -13, 1, 1, 26, 0, false);

		door_side_top_2_r1 = new ModelMapper(modelDataWrapper);
		door_side_top_2_r1.setPos(-18.9095F, -32.8894F, 0);
		door_sides.addChild(door_side_top_2_r1);
		setRotationAngle(door_side_top_2_r1, 0, 0, 0.1047F);
		door_side_top_2_r1.texOffs(98, 100).addBox(-1, 0, -13, 2, 0, 26, 0, false);

		door_side_top_1_r1 = new ModelMapper(modelDataWrapper);
		door_side_top_1_r1.setPos(-21.5F, -13, 0);
		door_sides.addChild(door_side_top_1_r1);
		setRotationAngle(door_side_top_1_r1, 0, 0, 0.1047F);
		door_side_top_1_r1.texOffs(112, 17).addBox(0, -23, -13, 0, 3, 26, 0, false);

		door_end_exterior = new ModelMapper(modelDataWrapper);
		door_end_exterior.setPos(0, 24, 0);


		door_right_exterior_end = new ModelMapper(modelDataWrapper);
		door_right_exterior_end.setPos(0, 0, 0);
		door_end_exterior.addChild(door_right_exterior_end);
		door_right_exterior_end.texOffs(138, 125).addBox(-21, -13, 0, 0, 13, 13, 0, false);

		door_right_top_r3 = new ModelMapper(modelDataWrapper);
		door_right_top_r3.setPos(-21, -13, 0);
		door_right_exterior_end.addChild(door_right_top_r3);
		setRotationAngle(door_right_top_r3, 0, 0, 0.1047F);
		door_right_top_r3.texOffs(102, 226).addBox(0, -20, 0, 0, 20, 13, 0, false);

		door_left_exterior_end = new ModelMapper(modelDataWrapper);
		door_left_exterior_end.setPos(0, 0, 0);
		door_end_exterior.addChild(door_left_exterior_end);
		door_left_exterior_end.texOffs(0, 64).addBox(-21, -13, -13, 0, 13, 13, 0, false);

		door_left_top_r2 = new ModelMapper(modelDataWrapper);
		door_left_top_r2.setPos(-21, -13, 0);
		door_left_exterior_end.addChild(door_left_top_r2);
		setRotationAngle(door_left_top_r2, 0, 0, 0.1047F);
		door_left_top_r2.texOffs(24, 136).addBox(0, -20, -13, 0, 20, 13, 0, false);

		door_sides_end = new ModelMapper(modelDataWrapper);
		door_sides_end.setPos(0, 0, 0);
		door_end_exterior.addChild(door_sides_end);
		door_sides_end.texOffs(219, 266).addBox(-21.5F, 0, -16, 1, 3, 32, 0, false);
		door_sides_end.texOffs(282, 266).addBox(-22, 0, -13, 1, 1, 26, 0, false);

		door_side_top_3_r1 = new ModelMapper(modelDataWrapper);
		door_side_top_3_r1.setPos(-18.9094F, -32.8894F, 0);
		door_sides_end.addChild(door_side_top_3_r1);
		setRotationAngle(door_side_top_3_r1, 0, 0, 0.1047F);
		door_side_top_3_r1.texOffs(98, 100).addBox(-1, 0, -13, 2, 0, 26, 0, false);

		door_side_top_2_r2 = new ModelMapper(modelDataWrapper);
		door_side_top_2_r2.setPos(-21.5F, -13, 0);
		door_sides_end.addChild(door_side_top_2_r2);
		setRotationAngle(door_side_top_2_r2, 0, 0, 0.1047F);
		door_side_top_2_r2.texOffs(112, 14).addBox(0, -23, -13, 0, 3, 26, 0, false);

		handrail_door = new ModelMapper(modelDataWrapper);
		handrail_door.setPos(0, 24, 0);
		handrail_door.texOffs(359, 77).addBox(0, -14.25F, 0, 0, 15, 0, 0.2F, false);
		handrail_door.texOffs(359, 29).addBox(0, -37, 0, 0, 6, 0, 0.2F, false);

		handrail_curve = new ModelMapper(modelDataWrapper);
		handrail_curve.setPos(0, 0, 0);
		handrail_door.addChild(handrail_curve);
		handrail_curve.texOffs(330, 0).addBox(0, -30.9085F, -0.5F, 0, 0, 1, 0.2F, false);
		handrail_curve.texOffs(330, 0).addBox(0, -14.25F, -0.5F, 0, 0, 1, 0.2F, false);

		handrail_curve_12_r1 = new ModelMapper(modelDataWrapper);
		handrail_curve_12_r1.setPos(0, -14.5328F, -1.0464F);
		handrail_curve.addChild(handrail_curve_12_r1);
		setRotationAngle(handrail_curve_12_r1, -0.7854F, 0, 0);
		handrail_curve_12_r1.texOffs(330, 0).addBox(0, 0, 0, 0, 0, 0, 0.2F, false);

		handrail_curve_10_r1 = new ModelMapper(modelDataWrapper);
		handrail_curve_10_r1.setPos(0, -14.806F, -1.256F);
		handrail_curve.addChild(handrail_curve_10_r1);
		setRotationAngle(handrail_curve_10_r1, -1.0472F, 0, 0);
		handrail_curve_10_r1.texOffs(330, 0).addBox(0, 0, 0, 0, 0, 0, 0.2F, false);

		handrail_curve_9_r1 = new ModelMapper(modelDataWrapper);
		handrail_curve_9_r1.setPos(0, -14.3232F, -0.7732F);
		handrail_curve.addChild(handrail_curve_9_r1);
		setRotationAngle(handrail_curve_9_r1, -0.5236F, 0, 0);
		handrail_curve_9_r1.texOffs(330, 0).addBox(0, 0, 0, 0, 0, 0, 0.2F, false);

		handrail_curve_7_r1 = new ModelMapper(modelDataWrapper);
		handrail_curve_7_r1.setPos(0, -22.5793F, -1.3293F);
		handrail_curve.addChild(handrail_curve_7_r1);
		setRotationAngle(handrail_curve_7_r1, 0, 0, -1.5708F);
		handrail_curve_7_r1.texOffs(328, 0).addBox(-7.5F, 0, 0, 15, 0, 0, 0.2F, false);
		handrail_curve_7_r1.texOffs(328, 0).addBox(-7.5F, 0, 2.6585F, 15, 0, 0, 0.2F, false);

		handrail_curve_6_r1 = new ModelMapper(modelDataWrapper);
		handrail_curve_6_r1.setPos(0, -30.3525F, -1.256F);
		handrail_curve.addChild(handrail_curve_6_r1);
		setRotationAngle(handrail_curve_6_r1, 1.0472F, 0, 0);
		handrail_curve_6_r1.texOffs(330, 0).addBox(0, 0, 0, 0, 0, 0, 0.2F, false);

		handrail_curve_5_r1 = new ModelMapper(modelDataWrapper);
		handrail_curve_5_r1.setPos(0, -30.6257F, -1.0464F);
		handrail_curve.addChild(handrail_curve_5_r1);
		setRotationAngle(handrail_curve_5_r1, 0.7854F, 0, 0);
		handrail_curve_5_r1.texOffs(330, 0).addBox(0, 0, 0, 0, 0, 0, 0.2F, false);

		handrail_curve_3_r1 = new ModelMapper(modelDataWrapper);
		handrail_curve_3_r1.setPos(0, -30.8353F, -0.7732F);
		handrail_curve.addChild(handrail_curve_3_r1);
		setRotationAngle(handrail_curve_3_r1, 0.5236F, 0, 0);
		handrail_curve_3_r1.texOffs(330, 0).addBox(0, 0, 0, 0, 0, 0, 0.2F, false);

		handrail_curve_11_r1 = new ModelMapper(modelDataWrapper);
		handrail_curve_11_r1.setPos(0, -14.5328F, 1.0464F);
		handrail_curve.addChild(handrail_curve_11_r1);
		setRotationAngle(handrail_curve_11_r1, 0.7854F, 0, 0);
		handrail_curve_11_r1.texOffs(330, 0).addBox(0, 0, 0, 0, 0, 0, 0.2F, false);

		handrail_curve_9_r2 = new ModelMapper(modelDataWrapper);
		handrail_curve_9_r2.setPos(0, -14.806F, 1.256F);
		handrail_curve.addChild(handrail_curve_9_r2);
		setRotationAngle(handrail_curve_9_r2, 1.0472F, 0, 0);
		handrail_curve_9_r2.texOffs(330, 0).addBox(0, 0, 0, 0, 0, 0, 0.2F, false);

		handrail_curve_8_r1 = new ModelMapper(modelDataWrapper);
		handrail_curve_8_r1.setPos(0, -14.3232F, 0.7732F);
		handrail_curve.addChild(handrail_curve_8_r1);
		setRotationAngle(handrail_curve_8_r1, 0.5236F, 0, 0);
		handrail_curve_8_r1.texOffs(330, 0).addBox(0, 0, 0, 0, 0, 0, 0.2F, false);

		handrail_curve_5_r2 = new ModelMapper(modelDataWrapper);
		handrail_curve_5_r2.setPos(0, -30.3525F, 1.256F);
		handrail_curve.addChild(handrail_curve_5_r2);
		setRotationAngle(handrail_curve_5_r2, -1.0472F, 0, 0);
		handrail_curve_5_r2.texOffs(330, 0).addBox(0, 0, 0, 0, 0, 0, 0.2F, false);

		handrail_curve_4_r1 = new ModelMapper(modelDataWrapper);
		handrail_curve_4_r1.setPos(0, -30.6257F, 1.0464F);
		handrail_curve.addChild(handrail_curve_4_r1);
		setRotationAngle(handrail_curve_4_r1, -0.7854F, 0, 0);
		handrail_curve_4_r1.texOffs(330, 0).addBox(0, 0, 0, 0, 0, 0, 0.2F, false);

		handrail_curve_2_r1 = new ModelMapper(modelDataWrapper);
		handrail_curve_2_r1.setPos(0, -30.8353F, 0.7732F);
		handrail_curve.addChild(handrail_curve_2_r1);
		setRotationAngle(handrail_curve_2_r1, -0.5236F, 0, 0);
		handrail_curve_2_r1.texOffs(330, 0).addBox(0, 0, 0, 0, 0, 0, 0.2F, false);

		head = new ModelMapper(modelDataWrapper);
		head.setPos(0, 24, 0);
		head.texOffs(117, 126).addBox(-20.5F, -13, 7, 1, 13, 2, 0, false);
		head.texOffs(117, 126).addBox(19.5F, -13, 7, 1, 13, 2, 0, true);
		head.texOffs(192, 113).addBox(-21, 0, 7, 42, 1, 1, 0, false);
		head.texOffs(228, 174).addBox(-20, -36, 7, 40, 36, 0, 0, false);

		upper_wall_2_r5 = new ModelMapper(modelDataWrapper);
		upper_wall_2_r5.setPos(21.5F, -13, 0);
		head.addChild(upper_wall_2_r5);
		setRotationAngle(upper_wall_2_r5, 0, 0, -0.1047F);
		upper_wall_2_r5.texOffs(8, 90).addBox(-2, -21, 7, 1, 21, 2, 0, true);

		upper_wall_1_r4 = new ModelMapper(modelDataWrapper);
		upper_wall_1_r4.setPos(-21.5F, -13, 0);
		head.addChild(upper_wall_1_r4);
		setRotationAngle(upper_wall_1_r4, 0, 0, 0.1047F);
		upper_wall_1_r4.texOffs(8, 90).addBox(1, -21, 7, 1, 21, 2, 0, false);

		head_exterior = new ModelMapper(modelDataWrapper);
		head_exterior.setPos(0, 24, 0);
		head_exterior.texOffs(272, 130).addBox(-21.5F, 0, -19, 1, 3, 27, 0, false);
		head_exterior.texOffs(189, 243).addBox(-21.5F, -13, -18, 2, 13, 29, 0, false);
		head_exterior.texOffs(272, 130).addBox(20.5F, 0, -19, 1, 3, 27, 0, true);
		head_exterior.texOffs(189, 243).addBox(19.5F, -13, -18, 2, 13, 29, 0, true);
		head_exterior.texOffs(163, 138).addBox(-20, 0, -22, 40, 1, 29, 0, false);
		head_exterior.texOffs(242, 53).addBox(-19.5F, -42, 7, 39, 42, 0, 0, false);

		upper_wall_2_r6 = new ModelMapper(modelDataWrapper);
		upper_wall_2_r6.setPos(21.5F, -13, 1);
		head_exterior.addChild(upper_wall_2_r6);
		setRotationAngle(upper_wall_2_r6, 0, 0, -0.1047F);
		upper_wall_2_r6.texOffs(69, 239).addBox(-2, -23, -19, 2, 23, 29, 0, true);

		upper_wall_1_r5 = new ModelMapper(modelDataWrapper);
		upper_wall_1_r5.setPos(-21.5F, -13, 1);
		head_exterior.addChild(upper_wall_1_r5);
		setRotationAngle(upper_wall_1_r5, 0, 0, 0.1047F);
		upper_wall_1_r5.texOffs(69, 239).addBox(0, -23, -19, 2, 23, 29, 0, false);

		bumper = new ModelMapper(modelDataWrapper);
		bumper.setPos(0, 0.1F, -17);
		head_exterior.addChild(bumper);
		bumper.texOffs(54, 155).addBox(-4.5855F, -0.1F, -8.1564F, 5, 3, 3, 0, false);
		bumper.texOffs(54, 155).addBox(-0.4145F, -0.1F, -8.1564F, 5, 3, 3, 0, true);

		bumper_2_r1 = new ModelMapper(modelDataWrapper);
		bumper_2_r1.setPos(-21.5F, -0.1F, -2);
		bumper.addChild(bumper_2_r1);
		setRotationAngle(bumper_2_r1, 0, 0.3491F, 0);
		bumper_2_r1.texOffs(112, 46).addBox(0, 0, 0, 18, 3, 3, 0, true);

		bumper_1_r1 = new ModelMapper(modelDataWrapper);
		bumper_1_r1.setPos(21.5F, -0.1F, -2);
		bumper.addChild(bumper_1_r1);
		setRotationAngle(bumper_1_r1, 0, -0.3491F, 0);
		bumper_1_r1.texOffs(112, 46).addBox(-18, 0, 0, 18, 3, 3, 0, false);

		head_back = new ModelMapper(modelDataWrapper);
		head_back.setPos(0, 0, -17);
		head_exterior.addChild(head_back);
		head_back.texOffs(52, 0).addBox(-8, -33, -5, 1, 33, 0, 0, false);
		head_back.texOffs(32, 0).addBox(7, -33, -5, 1, 33, 0, 0, false);
		head_back.texOffs(54, 182).addBox(-8, -42, -5, 16, 9, 0, 0, false);
		head_back.texOffs(294, 95).addBox(-7, -33, -5, 14, 34, 0, 0, false);

		front_right_panel_3_r2 = new ModelMapper(modelDataWrapper);
		front_right_panel_3_r2.setPos(8, -42, -5);
		head_back.addChild(front_right_panel_3_r2);
		setRotationAngle(front_right_panel_3_r2, 0, -0.3491F, 0);
		front_right_panel_3_r2.texOffs(174, 168).addBox(0, 0, 0, 15, 42, 0, 0, false);

		front_right_panel_2_r1 = new ModelMapper(modelDataWrapper);
		front_right_panel_2_r1.setPos(-8, -42, -5);
		head_back.addChild(front_right_panel_2_r1);
		setRotationAngle(front_right_panel_2_r1, 0, 0.3491F, 0);
		front_right_panel_2_r1.texOffs(0, 212).addBox(-15, 0, 0, 15, 42, 0, 0, false);

		roof_head_exterior = new ModelMapper(modelDataWrapper);
		roof_head_exterior.setPos(0, 0, 4);
		head_exterior.addChild(roof_head_exterior);
		roof_head_exterior.texOffs(54, 100).addBox(-4, -41.375F, -26, 4, 0, 34, 0, false);
		roof_head_exterior.texOffs(54, 100).addBox(0, -41.375F, -26, 4, 0, 34, 0, true);

		outer_roof_7_r2 = new ModelMapper(modelDataWrapper);
		outer_roof_7_r2.setPos(4, -41.375F, -3);
		roof_head_exterior.addChild(outer_roof_7_r2);
		setRotationAngle(outer_roof_7_r2, 0, 0, 0.0873F);
		outer_roof_7_r2.texOffs(86, 0).addBox(0, 0, -23, 6, 0, 34, 0, false);

		outer_roof_6_r4 = new ModelMapper(modelDataWrapper);
		outer_roof_6_r4.setPos(12.392F, -40.205F, -3);
		roof_head_exterior.addChild(outer_roof_6_r4);
		setRotationAngle(outer_roof_6_r4, 0, 0, 0.2618F);
		outer_roof_6_r4.texOffs(98, 0).addBox(-2.5F, 0, -23, 5, 0, 34, 0, true);

		outer_roof_5_r5 = new ModelMapper(modelDataWrapper);
		outer_roof_5_r5.setPos(16.2163F, -39.0449F, -3);
		roof_head_exterior.addChild(outer_roof_5_r5);
		setRotationAngle(outer_roof_5_r5, 0, 0, 0.3491F);
		outer_roof_5_r5.texOffs(0, 0).addBox(-1.5F, 0, -23, 3, 0, 34, 0, true);

		outer_roof_4_r5 = new ModelMapper(modelDataWrapper);
		outer_roof_4_r5.setPos(18.2687F, -37.7659F, -3);
		roof_head_exterior.addChild(outer_roof_4_r5);
		setRotationAngle(outer_roof_4_r5, 0, 0, 0.8727F);
		outer_roof_4_r5.texOffs(0, 77).addBox(-1, 0, -23, 2, 0, 34, 0, true);

		outer_roof_3_r5 = new ModelMapper(modelDataWrapper);
		outer_roof_3_r5.setPos(18.6114F, -35.9228F, -3);
		roof_head_exterior.addChild(outer_roof_3_r5);
		setRotationAngle(outer_roof_3_r5, 0, 0, 1.3788F);
		outer_roof_3_r5.texOffs(256, 95).addBox(-1, -0.5F, -23, 2, 1, 34, 0, true);

		outer_roof_6_r5 = new ModelMapper(modelDataWrapper);
		outer_roof_6_r5.setPos(-4, -41.375F, -3);
		roof_head_exterior.addChild(outer_roof_6_r5);
		setRotationAngle(outer_roof_6_r5, 0, 0, -0.0873F);
		outer_roof_6_r5.texOffs(86, 0).addBox(-6, 0, -23, 6, 0, 34, 0, true);

		outer_roof_5_r6 = new ModelMapper(modelDataWrapper);
		outer_roof_5_r6.setPos(-12.392F, -40.205F, -3);
		roof_head_exterior.addChild(outer_roof_5_r6);
		setRotationAngle(outer_roof_5_r6, 0, 0, -0.2618F);
		outer_roof_5_r6.texOffs(98, 0).addBox(-2.5F, 0, -23, 5, 0, 34, 0, false);

		outer_roof_4_r6 = new ModelMapper(modelDataWrapper);
		outer_roof_4_r6.setPos(-16.2163F, -39.0449F, -3);
		roof_head_exterior.addChild(outer_roof_4_r6);
		setRotationAngle(outer_roof_4_r6, 0, 0, -0.3491F);
		outer_roof_4_r6.texOffs(0, 0).addBox(-1.5F, 0, -23, 3, 0, 34, 0, false);

		outer_roof_3_r6 = new ModelMapper(modelDataWrapper);
		outer_roof_3_r6.setPos(-18.2687F, -37.7659F, -3);
		roof_head_exterior.addChild(outer_roof_3_r6);
		setRotationAngle(outer_roof_3_r6, 0, 0, -0.8727F);
		outer_roof_3_r6.texOffs(0, 77).addBox(-1, 0, -23, 2, 0, 34, 0, false);

		outer_roof_2_r4 = new ModelMapper(modelDataWrapper);
		outer_roof_2_r4.setPos(-18.6114F, -35.9228F, -3);
		roof_head_exterior.addChild(outer_roof_2_r4);
		setRotationAngle(outer_roof_2_r4, 0, 0, -1.3788F);
		outer_roof_2_r4.texOffs(256, 95).addBox(-1, -0.5F, -23, 2, 1, 34, 0, false);

		roof_exterior = new ModelMapper(modelDataWrapper);
		roof_exterior.setPos(0, 24, 0);
		roof_exterior.texOffs(72, 0).addBox(-4, -41.375F, -20, 4, 0, 40, 0, false);

		outer_roof_5_r7 = new ModelMapper(modelDataWrapper);
		outer_roof_5_r7.setPos(-4, -41.375F, 4);
		roof_exterior.addChild(outer_roof_5_r7);
		setRotationAngle(outer_roof_5_r7, 0, 0, -0.0873F);
		outer_roof_5_r7.texOffs(0, 0).addBox(-6, 0, -24, 6, 0, 40, 0, false);

		outer_roof_4_r7 = new ModelMapper(modelDataWrapper);
		outer_roof_4_r7.setPos(-12.392F, -40.205F, 4);
		roof_exterior.addChild(outer_roof_4_r7);
		setRotationAngle(outer_roof_4_r7, 0, 0, -0.2618F);
		outer_roof_4_r7.texOffs(56, 0).addBox(-2.5F, 0, -24, 5, 0, 40, 0, false);

		outer_roof_3_r7 = new ModelMapper(modelDataWrapper);
		outer_roof_3_r7.setPos(-16.2163F, -39.0449F, 4);
		roof_exterior.addChild(outer_roof_3_r7);
		setRotationAngle(outer_roof_3_r7, 0, 0, -0.3491F);
		outer_roof_3_r7.texOffs(0, 77).addBox(-1.5F, 0, -24, 3, 0, 40, 0, false);

		outer_roof_2_r5 = new ModelMapper(modelDataWrapper);
		outer_roof_2_r5.setPos(-18.2687F, -37.7659F, 4);
		roof_exterior.addChild(outer_roof_2_r5);
		setRotationAngle(outer_roof_2_r5, 0, 0, -0.8727F);
		outer_roof_2_r5.texOffs(66, 0).addBox(-1, 0, -24, 2, 0, 40, 0, false);

		outer_roof_1_r2 = new ModelMapper(modelDataWrapper);
		outer_roof_1_r2.setPos(-18.6114F, -35.9228F, 4);
		roof_exterior.addChild(outer_roof_1_r2);
		setRotationAngle(outer_roof_1_r2, 0, 0, -1.3788F);
		outer_roof_1_r2.texOffs(225, 225).addBox(-1, -0.5F, -24, 2, 1, 40, 0, false);

		roof_door = new ModelMapper(modelDataWrapper);
		roof_door.setPos(0, 24, 0);
		roof_door.texOffs(94, 100).addBox(-17.9149F, -32.7849F, -13, 2, 0, 26, 0, false);
		roof_door.texOffs(4, 77).addBox(-11.3838F, -34.8979F, -13, 2, 0, 26, 0, false);
		roof_door.texOffs(70, 100).addBox(-6.6649F, -36.1658F, -13, 7, 0, 26, 0, false);

		inner_roof_4_r1 = new ModelMapper(modelDataWrapper);
		inner_roof_4_r1.setPos(-9.3834F, -34.898F, 3);
		roof_door.addChild(inner_roof_4_r1);
		setRotationAngle(inner_roof_4_r1, 0, 0, -0.4363F);
		inner_roof_4_r1.texOffs(0, 0).addBox(0, 0, -16, 3, 0, 26, 0, false);

		inner_roof_2_r1 = new ModelMapper(modelDataWrapper);
		inner_roof_2_r1.setPos(-15.9149F, -32.7849F, 3);
		roof_door.addChild(inner_roof_2_r1);
		setRotationAngle(inner_roof_2_r1, 0, 0, -0.4363F);
		inner_roof_2_r1.texOffs(84, 100).addBox(0, 0, -16, 5, 0, 26, 0, false);

		roof_window = new ModelMapper(modelDataWrapper);
		roof_window.setPos(0, 24, 0);
		roof_window.texOffs(38, 0).addBox(-17.9149F, -32.7849F, -27, 2, 0, 54, 0, false);
		roof_window.texOffs(34, 0).addBox(-11.3838F, -34.8979F, -27, 2, 0, 54, 0, false);
		roof_window.texOffs(2, 0).addBox(-6.6649F, -36.1658F, -27, 7, 0, 54, 0, false);

		inner_roof_9_r1 = new ModelMapper(modelDataWrapper);
		inner_roof_9_r1.setPos(-23.3588F, 10.4067F, -26.999F);
		roof_window.addChild(inner_roof_9_r1);
		setRotationAngle(inner_roof_9_r1, 0, -1.5708F, -0.4363F);
		inner_roof_9_r1.texOffs(26, 33).addBox(54, -37, -30, 0, 1, 5, 0, true);
		inner_roof_9_r1.texOffs(26, 33).addBox(0, -37, -30, 0, 1, 5, 0, false);

		inner_roof_6_r1 = new ModelMapper(modelDataWrapper);
		inner_roof_6_r1.setPos(-9.3834F, -34.898F, 3);
		roof_window.addChild(inner_roof_6_r1);
		setRotationAngle(inner_roof_6_r1, 0, 0, -0.4363F);
		inner_roof_6_r1.texOffs(22, 0).addBox(0, 0, -30, 3, 0, 54, 0, false);

		inner_roof_4_r2 = new ModelMapper(modelDataWrapper);
		inner_roof_4_r2.setPos(-11.2897F, -34.4754F, 0);
		roof_window.addChild(inner_roof_4_r2);
		setRotationAngle(inner_roof_4_r2, 0, 0, -0.2182F);
		inner_roof_4_r2.texOffs(16, 0).addBox(-2.9063F, -0.4226F, -27, 3, 0, 54, 0, false);

		inner_roof_3_r1 = new ModelMapper(modelDataWrapper);
		inner_roof_3_r1.setPos(-15.8212F, -32.3623F, 3);
		roof_window.addChild(inner_roof_3_r1);
		setRotationAngle(inner_roof_3_r1, 0, 0, -0.6109F);
		inner_roof_3_r1.texOffs(28, 0).addBox(0.0937F, -0.4226F, -30, 3, 0, 54, 0, false);

		roof_end = new ModelMapper(modelDataWrapper);
		roof_end.setPos(0, 24, 0);


		side_1 = new ModelMapper(modelDataWrapper);
		side_1.setPos(0, 0, 0);
		roof_end.addChild(side_1);
		side_1.texOffs(168, 29).addBox(-17.9149F, -33.7849F, -7, 4, 1, 18, 0, true);
		side_1.texOffs(10, 10).addBox(-11.3838F, -34.8979F, 3, 2, 0, 8, 0, true);

		inner_roof_5_r1 = new ModelMapper(modelDataWrapper);
		inner_roof_5_r1.setPos(-9.3834F, -34.898F, 3);
		side_1.addChild(inner_roof_5_r1);
		setRotationAngle(inner_roof_5_r1, 0, 0, -0.4363F);
		inner_roof_5_r1.texOffs(8, 0).addBox(0, 0, -2, 3, 0, 10, 0, true);

		inner_roof_3_r2 = new ModelMapper(modelDataWrapper);
		inner_roof_3_r2.setPos(-14.6848F, -32.1477F, 3);
		side_1.addChild(inner_roof_3_r2);
		setRotationAngle(inner_roof_3_r2, 0, 0, -0.6912F);
		inner_roof_3_r2.texOffs(78, 183).addBox(1, -1, 0, 4, 1, 8, 0, true);

		side_2 = new ModelMapper(modelDataWrapper);
		side_2.setPos(0, 0, 0);
		roof_end.addChild(side_2);
		side_2.texOffs(168, 29).addBox(13.9149F, -33.7849F, -7, 4, 1, 18, 0, false);
		side_2.texOffs(10, 10).addBox(9.3838F, -34.8979F, 3, 2, 0, 8, 0, false);

		inner_roof_7_r1 = new ModelMapper(modelDataWrapper);
		inner_roof_7_r1.setPos(13.9156F, -32.7856F, 11);
		side_2.addChild(inner_roof_7_r1);
		setRotationAngle(inner_roof_7_r1, 0, 0, 0.6912F);
		inner_roof_7_r1.texOffs(78, 183).addBox(-4, -0.999F, -8, 4, 1, 8, 0, false);

		inner_roof_6_r2 = new ModelMapper(modelDataWrapper);
		inner_roof_6_r2.setPos(6.6644F, -36.1659F, 3);
		side_2.addChild(inner_roof_6_r2);
		setRotationAngle(inner_roof_6_r2, 0, 0, 0.4363F);
		inner_roof_6_r2.texOffs(8, 0).addBox(0, 0, -2, 3, 0, 10, 0, false);

		mid_roof = new ModelMapper(modelDataWrapper);
		mid_roof.setPos(0, 0, 0);
		roof_end.addChild(mid_roof);
		mid_roof.texOffs(112, 67).addBox(-18, -36.7849F, -9, 36, 4, 2, 0, false);
		mid_roof.texOffs(54, 149).addBox(7.9149F, -36.7849F, -7, 6, 4, 2, 0, false);
		mid_roof.texOffs(54, 149).addBox(-13.9149F, -36.7849F, -7, 6, 4, 2, 0, true);

		roof_end_gangway = new ModelMapper(modelDataWrapper);
		roof_end_gangway.setPos(0, 24, 0);
		roof_end_gangway.texOffs(96, 40).addBox(-17.9149F, -33.7849F, 9, 4, 1, 2, 0, false);
		roof_end_gangway.texOffs(96, 40).addBox(13.9149F, -33.7849F, 9, 4, 1, 2, 0, true);

		mid_roof_gangway = new ModelMapper(modelDataWrapper);
		mid_roof_gangway.setPos(0, 0, 0);
		roof_end_gangway.addChild(mid_roof_gangway);
		mid_roof_gangway.texOffs(268, 33).addBox(-8.5F, -34.875F, -9, 17, 2, 16, 0, false);
		mid_roof_gangway.texOffs(32, 140).addBox(8.5F, -36.875F, 7, 6, 4, 2, 0, false);
		mid_roof_gangway.texOffs(32, 140).addBox(-14.5F, -36.875F, 7, 6, 4, 2, 0, true);

		roof_light = new ModelMapper(modelDataWrapper);
		roof_light.setPos(0, 24, 0);
		roof_light.texOffs(0, 77).addBox(-11.3838F, -34.9F, -13, 2, 0, 26, 0, false);

		destination_display_end_interior = new ModelMapper(modelDataWrapper);
		destination_display_end_interior.setPos(0, 24, 0);
		destination_display_end_interior.texOffs(269, 245).addBox(-0.5032F, -36.4362F, -7, 9, 2, 18, 0, false);
		destination_display_end_interior.texOffs(269, 245).addBox(-8.4964F, -36.4363F, -7, 9, 2, 18, 0, true);

		display_6_r1 = new ModelMapper(modelDataWrapper);
		display_6_r1.setPos(-24.0533F, -3.2172F, 42.001F);
		destination_display_end_interior.addChild(display_6_r1);
		setRotationAngle(display_6_r1, 0, 0, 0.48F);
		display_6_r1.texOffs(0, 149).addBox(-1.6162F, -35.875F, -39, 1, 1, 8, 0, true);

		display_5_r1 = new ModelMapper(modelDataWrapper);
		display_5_r1.setPos(4.8028F, -41.5322F, 9.501F);
		destination_display_end_interior.addChild(display_5_r1);
		setRotationAngle(display_5_r1, 0, 0, -0.48F);
		display_5_r1.texOffs(0, 149).addBox(0, 7, -6.5F, 1, 1, 8, 0, false);

		display_4_r1 = new ModelMapper(modelDataWrapper);
		display_4_r1.setPos(13.7899F, -31.7933F, 21);
		destination_display_end_interior.addChild(display_4_r1);
		setRotationAngle(display_4_r1, 0, 0, 0.295F);
		display_4_r1.texOffs(134, 28).addBox(-6.1257F, -2.9925F, -26, 6, 2, 8, 0, false);

		display_3_r1 = new ModelMapper(modelDataWrapper);
		display_3_r1.setPos(-7.7652F, -33.617F, 21);
		destination_display_end_interior.addChild(display_3_r1);
		setRotationAngle(display_3_r1, 0, 0, -0.295F);
		display_3_r1.texOffs(134, 28).addBox(-6.1257F, -2.9925F, -26, 6, 2, 8, 0, true);

		roof_head = new ModelMapper(modelDataWrapper);
		roof_head.setPos(0, 24, 0);
		roof_head.texOffs(14, 18).addBox(-17.9149F, -32.7849F, 7, 2, 0, 4, 0, false);
		roof_head.texOffs(18, 10).addBox(-11.3838F, -34.8979F, 7, 2, 0, 4, 0, false);
		roof_head.texOffs(18, 10).addBox(9.3838F, -34.8979F, 7, 2, 0, 4, 0, true);
		roof_head.texOffs(14, 18).addBox(15.9149F, -32.7849F, 7, 2, 0, 4, 0, true);

		inner_roof_6_r3 = new ModelMapper(modelDataWrapper);
		inner_roof_6_r3.setPos(11.3834F, -34.898F, 19);
		roof_head.addChild(inner_roof_6_r3);
		setRotationAngle(inner_roof_6_r3, 0, 0, 0.4363F);
		inner_roof_6_r3.texOffs(22, 34).addBox(0, 0, -12, 5, 0, 4, 0, true);

		inner_roof_3_r3 = new ModelMapper(modelDataWrapper);
		inner_roof_3_r3.setPos(-15.9149F, -32.7849F, 27);
		roof_head.addChild(inner_roof_3_r3);
		setRotationAngle(inner_roof_3_r3, 0, 0, -0.4363F);
		inner_roof_3_r3.texOffs(22, 34).addBox(0, 0, -20, 5, 0, 4, 0, false);

		destination_display = new ModelMapper(modelDataWrapper);
		destination_display.setPos(0, 0, 4);
		roof_head.addChild(destination_display);
		destination_display.texOffs(269, 245).addBox(-0.5032F, -36.4362F, 3, 9, 2, 18, 0, false);
		destination_display.texOffs(269, 245).addBox(-8.4964F, -36.4363F, 3, 9, 2, 18, 0, true);

		display_7_r1 = new ModelMapper(modelDataWrapper);
		display_7_r1.setPos(-24.0533F, -3.2172F, 52.001F);
		destination_display.addChild(display_7_r1);
		setRotationAngle(display_7_r1, 0, 0, 0.48F);
		display_7_r1.texOffs(110, 241).addBox(-1.6162F, -35.875F, -49, 1, 1, 18, 0, true);

		display_6_r2 = new ModelMapper(modelDataWrapper);
		display_6_r2.setPos(4.8028F, -41.5322F, 19.501F);
		destination_display.addChild(display_6_r2);
		setRotationAngle(display_6_r2, 0, 0, -0.48F);
		display_6_r2.texOffs(110, 241).addBox(0, 7, -16.5F, 1, 1, 18, 0, false);

		headlights = new ModelMapper(modelDataWrapper);
		headlights.setPos(0, 24, 0);


		headlights_2_r1 = new ModelMapper(modelDataWrapper);
		headlights_2_r1.setPos(8, -42, -22);
		headlights.addChild(headlights_2_r1);
		setRotationAngle(headlights_2_r1, 0, -0.3491F, 0);
		headlights_2_r1.texOffs(0, 29).addBox(3, 29, -0.1F, 10, 10, 0, 0, true);

		headlights_1_r1 = new ModelMapper(modelDataWrapper);
		headlights_1_r1.setPos(-8, -42, -22);
		headlights.addChild(headlights_1_r1);
		setRotationAngle(headlights_1_r1, 0, 0.3491F, 0);
		headlights_1_r1.texOffs(0, 29).addBox(-13, 29, -0.1F, 10, 10, 0, 0, false);

		tail_lights = new ModelMapper(modelDataWrapper);
		tail_lights.setPos(0, 24, 0);


		tail_lights_2_r1 = new ModelMapper(modelDataWrapper);
		tail_lights_2_r1.setPos(8, -42, -22);
		tail_lights.addChild(tail_lights_2_r1);
		setRotationAngle(tail_lights_2_r1, 0, -0.3491F, 0);
		tail_lights_2_r1.texOffs(96, 43).addBox(9, 28, -0.05F, 5, 5, 0, 0, true);

		tail_lights_1_r1 = new ModelMapper(modelDataWrapper);
		tail_lights_1_r1.setPos(-8, -42, -22);
		tail_lights.addChild(tail_lights_1_r1);
		setRotationAngle(tail_lights_1_r1, 0, 0.3491F, 0);
		tail_lights_1_r1.texOffs(96, 43).addBox(-14, 28, -0.05F, 5, 5, 0, 0, false);

		door_light_interior_off = new ModelMapper(modelDataWrapper);
		door_light_interior_off.setPos(0, 24, 0);


		light_2_r1 = new ModelMapper(modelDataWrapper);
		light_2_r1.setPos(-9.6762F, -32.7244F, 13.2F);
		door_light_interior_off.addChild(light_2_r1);
		setRotationAngle(light_2_r1, 0, 0, 0.1047F);
		light_2_r1.texOffs(351, 10).addBox(-7, 1.75F, -27.2F, 0, 0, 0, 0.3F, false);
		light_2_r1.texOffs(351, 10).addBox(-7, 1.75F, 0.8F, 0, 0, 0, 0.3F, false);

		door_light_interior_on = new ModelMapper(modelDataWrapper);
		door_light_interior_on.setPos(0, 24, 0);


		light_3_r1 = new ModelMapper(modelDataWrapper);
		light_3_r1.setPos(-9.6762F, -32.7244F, 13.2F);
		door_light_interior_on.addChild(light_3_r1);
		setRotationAngle(light_3_r1, 0, 0, 0.1047F);
		light_3_r1.texOffs(351, 15).addBox(-7, 1.75F, -27.2F, 0, 0, 0, 0.3F, false);
		light_3_r1.texOffs(351, 15).addBox(-7, 1.75F, 0.8F, 0, 0, 0, 0.3F, false);

		modelDataWrapper.setModelPart(textureWidth, textureHeight);
		window_exterior.setModelPart();
		window_handrails.setModelPart();
		window_exterior_end.setModelPart();
		window.setModelPart();
		side_panel.setModelPart();
		end.setModelPart();
		end_exterior.setModelPart();
		end_gangway.setModelPart();
		end_gangway_exterior.setModelPart();
		door.setModelPart();
		door_left.setModelPart(door.name);
		door_right.setModelPart(door.name);
		door_exterior.setModelPart();
		door_left_exterior.setModelPart(door_exterior.name);
		door_right_exterior.setModelPart(door_exterior.name);
		door_end_exterior.setModelPart();
		door_left_exterior_end.setModelPart(door_end_exterior.name);
		door_right_exterior_end.setModelPart(door_end_exterior.name);
		handrail_door.setModelPart();
		head.setModelPart();
		head_exterior.setModelPart();
		roof_exterior.setModelPart();
		roof_door.setModelPart();
		roof_window.setModelPart();
		roof_end.setModelPart();
		roof_end_gangway.setModelPart();
		roof_light.setModelPart();
		destination_display_end_interior.setModelPart();
		roof_head.setModelPart();
		headlights.setModelPart();
		tail_lights.setModelPart();
		door_light_interior_off.setModelPart();
		door_light_interior_on.setModelPart();
	}

	private static final int DOOR_MAX = 13;

	@Override
	public ModelR211 createNew(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		return new ModelR211(openGangway, doorAnimationType, renderDoorOverlay);
	}

	@Override
	protected void renderWindowPositions(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
		switch (renderStage) {
			case LIGHTS:
				renderMirror(roof_light, matrices, vertices, light, position);
				renderMirror(roof_light, matrices, vertices, light, position - 15);
				renderMirror(roof_light, matrices, vertices, light, position + 15);
				break;
			case INTERIOR:
				renderMirror(window, matrices, vertices, light, position);
				if (renderDetails) {
					renderMirror(roof_window, matrices, vertices, light, position);
					renderMirror(window_handrails, matrices, vertices, light, position);
					renderMirror(side_panel, matrices, vertices, light, position + 24.1F);
					renderMirror(side_panel, matrices, vertices, light, position - 24.1F);
				}
				break;
			case EXTERIOR:
				renderMirror(roof_exterior, matrices, vertices, light, position);
				if (isIndex(0, position, getWindowPositions()) && isEnd1Head) {
					renderOnceFlipped(window_exterior_end, matrices, vertices, light, position);
				} else if (isIndex(-1, position, getWindowPositions()) && isEnd2Head) {
					renderOnce(window_exterior_end, matrices, vertices, light, position);
				} else {
					renderMirror(window_exterior, matrices, vertices, light, position);
				}
		}
	}

	@Override
	protected void renderDoorPositions(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
		final boolean doorOpen = doorLeftZ > 0 || doorRightZ > 0;

		switch (renderStage) {
			case LIGHTS:
				renderMirror(roof_light, matrices, vertices, light, position);
				if (doorOpen) {
					renderMirror(door_light_interior_on, matrices, vertices, light, position);
				}
				break;
			case INTERIOR:
				door_right.setOffset(doorRightX, 0, doorRightZ);
				door_left.setOffset(doorRightX, 0, -doorRightZ);
				renderOnce(door, matrices, vertices, light, position);
				door_right.setOffset(doorLeftX, 0, doorLeftZ);
				door_left.setOffset(doorLeftX, 0, -doorLeftZ);
				renderOnceFlipped(door, matrices, vertices, light, position);
				if (!doorOpen) {
					renderMirror(door_light_interior_off, matrices, vertices, light, position);
				}
				if (renderDetails) {
					renderOnce(handrail_door, matrices, vertices, light, position);
					renderMirror(roof_door, matrices, vertices, light, position);
				}
				break;
			case EXTERIOR:
				final boolean door1End = isIndex(0, position, getDoorPositions()) && isEnd1Head;
				final boolean door2End = isIndex(-1, position, getDoorPositions()) && isEnd2Head;

				if (door1End || door2End) {
					door_right_exterior_end.setOffset(doorRightX, 0, doorRightZ);
					door_left_exterior_end.setOffset(doorRightX, 0, -doorRightZ);
					renderOnce(door_end_exterior, matrices, vertices, light, position);
				} else {
					door_right_exterior.setOffset(doorRightX, 0, doorRightZ);
					door_left_exterior.setOffset(doorRightX, 0, -doorRightZ);
					renderOnce(door_exterior, matrices, vertices, light, position);
				}

				if (door1End || door2End) {
					door_right_exterior_end.setOffset(doorLeftX, 0, doorLeftZ);
					door_left_exterior_end.setOffset(doorLeftX, 0, -doorLeftZ);
					renderOnceFlipped(door_end_exterior, matrices, vertices, light, position);
				} else {
					door_right_exterior.setOffset(doorLeftX, 0, doorLeftZ);
					door_left_exterior.setOffset(doorLeftX, 0, -doorLeftZ);
					renderOnceFlipped(door_exterior, matrices, vertices, light, position);
				}
				renderMirror(roof_exterior, matrices, vertices, light, position);
		}
	}

	@Override
	protected void renderHeadPosition1(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean useHeadlights) {
		switch (renderStage) {
			case LIGHTS:
				renderOnce(roof_light, matrices, vertices, light, position + 20);
				renderOnceFlipped(roof_light, matrices, vertices, light, position + 20);
				break;
			case ALWAYS_ON_LIGHTS:
				renderOnce(useHeadlights ? headlights : tail_lights, matrices, vertices, light, position);
				break;
			case INTERIOR:
				renderOnce(head, matrices, vertices, light, position);
				if (renderDetails) {
					renderOnce(roof_head, matrices, vertices, light, position);
				}
				break;
			case EXTERIOR:
				renderOnce(head_exterior, matrices, vertices, light, position);
				break;
		}
	}

	@Override
	protected void renderHeadPosition2(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean useHeadlights) {
		switch (renderStage) {
			case LIGHTS:
				renderOnce(roof_light, matrices, vertices, light, position - 20);
				renderOnceFlipped(roof_light, matrices, vertices, light, position - 20);
				break;
			case ALWAYS_ON_LIGHTS:
				renderOnceFlipped(useHeadlights ? headlights : tail_lights, matrices, vertices, light, position);
				break;
			case INTERIOR:
				renderOnceFlipped(head, matrices, vertices, light, position);
				if (renderDetails) {
					renderOnceFlipped(roof_head, matrices, vertices, light, position);
				}
				break;
			case EXTERIOR:
				renderOnceFlipped(head_exterior, matrices, vertices, light, position);
				break;
		}
	}

	@Override
	protected void renderEndPosition1(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
		switch (renderStage) {
			case LIGHTS:
				if (!openGangway) {
					renderOnce(roof_light, matrices, vertices, light, position + 16);
					renderOnceFlipped(roof_light, matrices, vertices, light, position + 16);
				}
				break;
			case INTERIOR:
				if (openGangway) {
					renderOnce(end_gangway, matrices, vertices, light, position);
				} else {
					renderOnce(end, matrices, vertices, light, position);
				}
				if (renderDetails) {
					if (openGangway) {
						renderOnce(roof_end_gangway, matrices, vertices, light, position);
						renderOnce(destination_display_end_interior, matrices, vertices, light, position + 8);
					} else {
						renderOnce(roof_end, matrices, vertices, light, position);
						renderOnce(destination_display_end_interior, matrices, vertices, light, position);
					}
				}
				break;
			case EXTERIOR:
				if (openGangway) {
					renderOnce(end_gangway_exterior, matrices, vertices, light, position);
				} else {
					renderOnce(end_exterior, matrices, vertices, light, position);
				}
				break;
		}
	}

	@Override
	protected void renderEndPosition2(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
		switch (renderStage) {
			case LIGHTS:
				if (!openGangway) {
					renderOnce(roof_light, matrices, vertices, light, position - 16);
					renderOnceFlipped(roof_light, matrices, vertices, light, position - 16);
				}
				break;
			case INTERIOR:
				if (openGangway) {
					renderOnceFlipped(end_gangway, matrices, vertices, light, position);
				} else {
					renderOnceFlipped(end, matrices, vertices, light, position);
				}
				if (renderDetails) {
					if (openGangway) {
						renderOnceFlipped(roof_end_gangway, matrices, vertices, light, position);
						renderOnceFlipped(destination_display_end_interior, matrices, vertices, light, position - 8);
					} else {
						renderOnceFlipped(roof_end, matrices, vertices, light, position);
						renderOnceFlipped(destination_display_end_interior, matrices, vertices, light, position);
					}
				}
				break;
			case EXTERIOR:
				if (openGangway) {
					renderOnceFlipped(end_gangway_exterior, matrices, vertices, light, position);
				} else {
					renderOnceFlipped(end_exterior, matrices, vertices, light, position);
				}
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
		return new int[]{-80, 0, 80};
	}

	@Override
	protected int[] getDoorPositions() {
		return new int[]{-120, -40, 40, 120};
	}

	@Override
	protected int[] getEndPositions() {
		return new int[]{-144, 144};
	}

	@Override
	protected int getDoorMax() {
		return DOOR_MAX;
	}

	@Override
	protected void renderTextDisplays(PoseStack matrices, MultiBufferSource vertexConsumers, Font font, MultiBufferSource.BufferSource immediate, Route thisRoute, Route nextRoute, Station thisStation, Station nextStation, Station lastStation, String customDestination, int car, int totalCars, boolean atPlatform, List<ScrollingText> scrollingTexts) {
		final String routeNumber = thisRoute == null ? "" : thisRoute.lightRailRouteNumber;
		renderFrontDestination(
				matrices, font, immediate,
				0, -2.26F, getEndPositions()[0] / 16F - 1.37F, 0, 0, -0.01F,
				0, 0, 0.44F, 0.12F,
				ARGB_WHITE, ARGB_WHITE, 1, getDestinationString(lastStation, customDestination, TextSpacingType.NORMAL, true), true, car, totalCars
		);
		renderFrontDestination(
				matrices, font, immediate,
				0.5F, 0, getEndPositions()[0] / 16F - 1.37F, 0.35F, -1.57F, -0.01F,
				0, -20, 0.4F, 0.36F,
				ARGB_WHITE, ARGB_WHITE, 1, routeNumber, false, car, totalCars
		);
	}

	@Override
	protected String defaultDestinationString() {
		return "Not in Service";
	}
}
