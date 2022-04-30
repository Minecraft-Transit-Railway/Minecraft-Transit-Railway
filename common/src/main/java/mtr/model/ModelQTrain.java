package mtr.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mtr.mappings.ModelDataWrapper;
import mtr.mappings.ModelMapper;

public class ModelQTrain extends ModelTrainBase {
    private final ModelMapper window;
    private final ModelMapper upper_wall_r1;
    private final ModelMapper window_exterior_1;
    private final ModelMapper upper_wall_r2;
    private final ModelMapper window_exterior_2;
    private final ModelMapper upper_wall_r3;
    private final ModelMapper side_panel;
    private final ModelMapper seat_back_2_r1;
    private final ModelMapper handrail_4_r1;
    private final ModelMapper handrail_3_r1;
    private final ModelMapper handrail_2_r1;
    private final ModelMapper side_panel_translucent;
    private final ModelMapper roof_door;
    private final ModelMapper inner_roof_5_r1;
    private final ModelMapper inner_roof_4_r1;
    private final ModelMapper inner_roof_2_r1;
    private final ModelMapper roof_window;
    private final ModelMapper inner_roof_2_r2;
    private final ModelMapper window_handrails;
    private final ModelMapper pole_bottom_diagonal_2_r1;
    private final ModelMapper pole_bottom_diagonal_1_r1;
    private final ModelMapper pole_top_diagonal_2_r1;
    private final ModelMapper pole_top_diagonal_1_r1;
    private final ModelMapper top_handrail_connector_bottom_3_r1;
    private final ModelMapper top_handrail_bottom_right_r1;
    private final ModelMapper top_handrail_bottom_left_r1;
    private final ModelMapper top_handrail_right_4_r1;
    private final ModelMapper top_handrail_right_3_r1;
    private final ModelMapper top_handrail_right_2_r1;
    private final ModelMapper top_handrail_left_4_r1;
    private final ModelMapper top_handrail_left_3_r1;
    private final ModelMapper top_handrail_left_2_r1;
    private final ModelMapper handrail_strap_3;
    private final ModelMapper seat;
    private final ModelMapper seat_back_r1;
    private final ModelMapper roof_exterior;
    private final ModelMapper outer_roof_4_r1;
    private final ModelMapper outer_roof_3_r1;
    private final ModelMapper outer_roof_2_r1;
    private final ModelMapper outer_roof_1_r1;
    private final ModelMapper door;
    private final ModelMapper door_left;
    private final ModelMapper door_left_top_r1;
    private final ModelMapper door_right;
    private final ModelMapper door_right_top_r1;
    private final ModelMapper door_exterior_1;
    private final ModelMapper door_left_exterior_1;
    private final ModelMapper door_left_top_1_r1;
    private final ModelMapper door_right_exterior_1;
    private final ModelMapper door_right_top_1_r1;
    private final ModelMapper door_exterior_2;
    private final ModelMapper door_left_exterior_2;
    private final ModelMapper door_left_top_2_r1;
    private final ModelMapper door_right_exterior_2;
    private final ModelMapper door_right_top_2_r1;
    private final ModelMapper end;
    private final ModelMapper upper_wall_2_r1;
    private final ModelMapper upper_wall_1_r1;
    private final ModelMapper end_exterior_1;
    private final ModelMapper upper_wall_2_r2;
    private final ModelMapper upper_wall_1_r2;
    private final ModelMapper end_exterior_2;
    private final ModelMapper upper_wall_3_r1;
    private final ModelMapper upper_wall_2_r3;
    private final ModelMapper roof_end_exterior;
    private final ModelMapper vent_2_r1;
    private final ModelMapper vent_1_r1;
    private final ModelMapper outer_roof_1;
    private final ModelMapper outer_roof_4_r2;
    private final ModelMapper outer_roof_3_r2;
    private final ModelMapper outer_roof_2_r2;
    private final ModelMapper outer_roof_1_r2;
    private final ModelMapper outer_roof_2;
    private final ModelMapper outer_roof_4_r3;
    private final ModelMapper outer_roof_3_r3;
    private final ModelMapper outer_roof_2_r3;
    private final ModelMapper outer_roof_1_r3;
    private final ModelMapper roof_light;
    private final ModelMapper roof_end_light;
    private final ModelMapper roof_head_exterior;
    private final ModelMapper vent_3_r1;
    private final ModelMapper vent_2_r2;
    private final ModelMapper outer_roof_4;
    private final ModelMapper outer_roof_7_r1;
    private final ModelMapper outer_roof_6_r1;
    private final ModelMapper outer_roof_4_r4;
    private final ModelMapper outer_roof_3_r4;
    private final ModelMapper outer_roof_2_r4;
    private final ModelMapper outer_roof_3;
    private final ModelMapper outer_roof_7_r2;
    private final ModelMapper outer_roof_6_r2;
    private final ModelMapper outer_roof_4_r5;
    private final ModelMapper outer_roof_3_r5;
    private final ModelMapper outer_roof_2_r5;
    private final ModelMapper head;
    private final ModelMapper upper_wall_2_r4;
    private final ModelMapper upper_wall_1_r3;
    private final ModelMapper head_exterior;
    private final ModelMapper upper_wall_2_r5;
    private final ModelMapper upper_wall_1_r4;
    private final ModelMapper front;
    private final ModelMapper side_1;
    private final ModelMapper outer_roof_5_r1;
    private final ModelMapper outer_roof_4_r6;
    private final ModelMapper outer_roof_3_r6;
    private final ModelMapper outer_roof_2_r6;
    private final ModelMapper front_side_bottom_2_r1;
    private final ModelMapper front_side_bottom_1_r1;
    private final ModelMapper front_side_upper_1_r1;
    private final ModelMapper front_side_lower_1_r1;
    private final ModelMapper side_2;
    private final ModelMapper outer_roof_5_r2;
    private final ModelMapper outer_roof_4_r7;
    private final ModelMapper outer_roof_3_r7;
    private final ModelMapper outer_roof_2_r7;
    private final ModelMapper front_side_bottom_4_r1;
    private final ModelMapper front_side_bottom_3_r1;
    private final ModelMapper front_side_upper_2_r1;
    private final ModelMapper front_side_lower_2_r1;
    private final ModelMapper front_panel;
    private final ModelMapper panel_5_r1;
    private final ModelMapper panel_8_r1;
    private final ModelMapper panel_9_r1;
    private final ModelMapper panel_6_r1;
    private final ModelMapper panel_3_r1;
    private final ModelMapper panel_8_r2;
    private final ModelMapper panel_7_r1;
    private final ModelMapper panel_5_r2;
    private final ModelMapper panel_4_r1;
    private final ModelMapper panel_4_r2;
    private final ModelMapper panel_3_r2;
    private final ModelMapper panel_2_r1;
    private final ModelMapper roof_door_light;
    private final ModelMapper door_light_on;
    private final ModelMapper light_r1;
    private final ModelMapper door_light_off;
    private final ModelMapper light_r2;
    private final ModelMapper headlight;
    private final ModelMapper headlight_2_r1;
    private final ModelMapper headlight_1_r1;
    private final ModelMapper tail_light;
    private final ModelMapper tail_light_2_r1;
    private final ModelMapper tail_light_1_r1;

    public ModelQTrain() {
        final int textureWidth = 320;
        final int textureHeight = 320;

        final ModelDataWrapper modelDataWrapper = new ModelDataWrapper(this, textureWidth, textureHeight);

        window = new ModelMapper(modelDataWrapper);
        window.setPos(0, 24, 0);
        window.texOffs(58, 46).addBox(-19, 0, -24, 19, 1, 48, 0, false);
        window.texOffs(58, 95).addBox(-20, -14, -27, 2, 14, 54, 0, false);

        upper_wall_r1 = new ModelMapper(modelDataWrapper);
        upper_wall_r1.setPos(-20, -14, 0);
        window.addChild(upper_wall_r1);
        setRotationAngle(upper_wall_r1, 0, 0, 0.1107F);
        upper_wall_r1.texOffs(0, 46).addBox(0, -19, -27, 2, 19, 54, 0, false);

        window_exterior_1 = new ModelMapper(modelDataWrapper);
        window_exterior_1.setPos(0, 24, 0);
        window_exterior_1.texOffs(58, 163).addBox(-20, 0, -24, 1, 4, 48, 0, false);
        window_exterior_1.texOffs(144, 0).addBox(-20, -14, -26, 1, 14, 52, 0, false);

        upper_wall_r2 = new ModelMapper(modelDataWrapper);
        upper_wall_r2.setPos(-20, -14, 0);
        window_exterior_1.addChild(upper_wall_r2);
        setRotationAngle(upper_wall_r2, 0, 0, 0.1107F);
        upper_wall_r2.texOffs(0, 119).addBox(0, -19, -26, 1, 19, 52, 0, false);

        window_exterior_2 = new ModelMapper(modelDataWrapper);
        window_exterior_2.setPos(0, 24, 0);
        window_exterior_2.texOffs(58, 163).addBox(19, 0, -24, 1, 4, 48, 0, true);
        window_exterior_2.texOffs(144, 0).addBox(19, -14, -26, 1, 14, 52, 0, true);

        upper_wall_r3 = new ModelMapper(modelDataWrapper);
        upper_wall_r3.setPos(19.0061F, -13.8896F, 0);
        window_exterior_2.addChild(upper_wall_r3);
        setRotationAngle(upper_wall_r3, 0, 0, -0.1107F);
        upper_wall_r3.texOffs(0, 119).addBox(0, -19, -26, 1, 19, 52, 0, true);

        side_panel = new ModelMapper(modelDataWrapper);
        side_panel.setPos(0, 24, 0);
        side_panel.texOffs(316, 19).addBox(-11, -13, 0, 0, 8, 0, 0.2F, false);
        side_panel.texOffs(16, 8).addBox(-18.7F, -11.5F, -0.5F, 8, 3, 1, 0, false);

        seat_back_2_r1 = new ModelMapper(modelDataWrapper);
        seat_back_2_r1.setPos(-14.3117F, -11.0463F, 0);
        side_panel.addChild(seat_back_2_r1);
        setRotationAngle(seat_back_2_r1, 0, 0, 0.1533F);
        seat_back_2_r1.texOffs(20, 35).addBox(-3.5F, -1, -0.5F, 7, 2, 1, 0, false);

        handrail_4_r1 = new ModelMapper(modelDataWrapper);
        handrail_4_r1.setPos(-13.8714F, -28.8479F, 0);
        side_panel.addChild(handrail_4_r1);
        setRotationAngle(handrail_4_r1, 0, 0, -0.4363F);
        handrail_4_r1.texOffs(319, 0).addBox(0, 1.5F, 0, 0, 1, 0, 0.2F, false);

        handrail_3_r1 = new ModelMapper(modelDataWrapper);
        handrail_3_r1.setPos(-14.8066F, -28.1477F, 0);
        side_panel.addChild(handrail_3_r1);
        setRotationAngle(handrail_3_r1, 0, 0, 1.9199F);
        handrail_3_r1.texOffs(319, 0).addBox(0, -1.5F, 0, 0, 3, 0, 0.2F, false);

        handrail_2_r1 = new ModelMapper(modelDataWrapper);
        handrail_2_r1.setPos(-11.1549F, -14.3636F, 0);
        side_panel.addChild(handrail_2_r1);
        setRotationAngle(handrail_2_r1, 0, 0, -0.1309F);
        handrail_2_r1.texOffs(316, 6).addBox(0, -12, 0, 0, 13, 0, 0.2F, false);

        side_panel_translucent = new ModelMapper(modelDataWrapper);
        side_panel_translucent.setPos(0, 24, 0);
        side_panel_translucent.texOffs(260, 261).addBox(-18, -29, 0, 7, 23, 0, 0, false);

        roof_door = new ModelMapper(modelDataWrapper);
        roof_door.setPos(0, 24, 0);
        roof_door.texOffs(0, 46).addBox(-18.0123F, -31.8899F, -13, 5, 0, 26, 0, false);
        roof_door.texOffs(82, 0).addBox(-10, -33.8899F, -18, 10, 0, 36, 0, false);

        inner_roof_5_r1 = new ModelMapper(modelDataWrapper);
        inner_roof_5_r1.setPos(-15.5123F, -31.8899F, 14.5F);
        roof_door.addChild(inner_roof_5_r1);
        setRotationAngle(inner_roof_5_r1, 0, -1.5708F, 0);
        inner_roof_5_r1.texOffs(0, 5).addBox(-1.5F, 0, -2.5F, 5, 0, 3, 0, true);

        inner_roof_4_r1 = new ModelMapper(modelDataWrapper);
        inner_roof_4_r1.setPos(-14.5123F, -31.8899F, -15.5F);
        roof_door.addChild(inner_roof_4_r1);
        setRotationAngle(inner_roof_4_r1, 0, -1.5708F, 0);
        inner_roof_4_r1.texOffs(0, 5).addBox(-2.5F, 0, -1.5F, 5, 0, 3, 0, false);

        inner_roof_2_r1 = new ModelMapper(modelDataWrapper);
        inner_roof_2_r1.setPos(-13.0118F, -31.89F, 8);
        roof_door.addChild(inner_roof_2_r1);
        setRotationAngle(inner_roof_2_r1, 0, 0, -0.5236F);
        inner_roof_2_r1.texOffs(102, 0).addBox(0, 0, -26, 4, 0, 36, 0, false);

        roof_window = new ModelMapper(modelDataWrapper);
        roof_window.setPos(0, 24, 0);
        roof_window.texOffs(42, 46).addBox(-16.0123F, -31.8899F, -22, 3, 0, 44, 0, false);
        roof_window.texOffs(14, 46).addBox(-10, -33.8899F, -22, 10, 0, 44, 0, false);

        inner_roof_2_r2 = new ModelMapper(modelDataWrapper);
        inner_roof_2_r2.setPos(-13.0118F, -31.89F, 8);
        roof_window.addChild(inner_roof_2_r2);
        setRotationAngle(inner_roof_2_r2, 0, 0, -0.5236F);
        inner_roof_2_r2.texOffs(34, 46).addBox(0, 0, -30, 4, 0, 44, 0, false);

        window_handrails = new ModelMapper(modelDataWrapper);
        window_handrails.setPos(0, 24, 0);
        window_handrails.texOffs(319, 0).addBox(-2, -34.4F, 15.8F, 0, 3, 0, 0.2F, false);
        window_handrails.texOffs(319, 0).addBox(-2, -34.4F, -15.8F, 0, 3, 0, 0.2F, false);
        window_handrails.texOffs(319, 0).addBox(-8.4923F, -33.8142F, -11, 0, 1, 0, 0.2F, false);
        window_handrails.texOffs(319, 0).addBox(-8.4923F, -33.8142F, 0, 0, 1, 0, 0.2F, false);
        window_handrails.texOffs(319, 0).addBox(-8.4923F, -33.8142F, 11, 0, 1, 0, 0.2F, false);
        window_handrails.texOffs(319, 0).addBox(0, -34, 11, 0, 5, 0, 0.2F, false);
        window_handrails.texOffs(316, 13).addBox(0, -23.2645F, 10.437F, 0, 6, 0, 0.2F, false);
        window_handrails.texOffs(316, 13).addBox(0, -23.2645F, 11.563F, 0, 6, 0, 0.2F, false);
        window_handrails.texOffs(319, 0).addBox(0, -12, 11, 0, 12, 0, 0.2F, false);

        pole_bottom_diagonal_2_r1 = new ModelMapper(modelDataWrapper);
        pole_bottom_diagonal_2_r1.setPos(0, -14.4002F, 11.2819F);
        window_handrails.addChild(pole_bottom_diagonal_2_r1);
        setRotationAngle(pole_bottom_diagonal_2_r1, -0.1047F, 0, 0);
        pole_bottom_diagonal_2_r1.texOffs(316, 19).addBox(0, -2.5F, 0, 0, 5, 0, 0.2F, false);

        pole_bottom_diagonal_1_r1 = new ModelMapper(modelDataWrapper);
        pole_bottom_diagonal_1_r1.setPos(0, -14.4002F, 10.7181F);
        window_handrails.addChild(pole_bottom_diagonal_1_r1);
        setRotationAngle(pole_bottom_diagonal_1_r1, 0.1047F, 0, 0);
        pole_bottom_diagonal_1_r1.texOffs(316, 19).addBox(0, -2.5F, 0, 0, 5, 0, 0.2F, false);

        pole_top_diagonal_2_r1 = new ModelMapper(modelDataWrapper);
        pole_top_diagonal_2_r1.setPos(0.2F, -28.8F, 10.8F);
        window_handrails.addChild(pole_top_diagonal_2_r1);
        setRotationAngle(pole_top_diagonal_2_r1, 0.1047F, 0, 0);
        pole_top_diagonal_2_r1.texOffs(316, 3).addBox(-0.2F, 0.2069F, 0.2F, 0, 5, 0, 0.2F, false);

        pole_top_diagonal_1_r1 = new ModelMapper(modelDataWrapper);
        pole_top_diagonal_1_r1.setPos(0.2F, -28.8F, 11.2F);
        window_handrails.addChild(pole_top_diagonal_1_r1);
        setRotationAngle(pole_top_diagonal_1_r1, -0.1047F, 0, 0);
        pole_top_diagonal_1_r1.texOffs(316, 3).addBox(-0.2F, 0.2069F, -0.2F, 0, 5, 0, 0.2F, false);

        top_handrail_connector_bottom_3_r1 = new ModelMapper(modelDataWrapper);
        top_handrail_connector_bottom_3_r1.setPos(-8.0559F, -32.2607F, -2.8876F);
        window_handrails.addChild(top_handrail_connector_bottom_3_r1);
        setRotationAngle(top_handrail_connector_bottom_3_r1, 0, 0, -0.7854F);
        top_handrail_connector_bottom_3_r1.texOffs(319, 0).addBox(0, -0.5F, 13.8876F, 0, 2, 0, 0.2F, false);
        top_handrail_connector_bottom_3_r1.texOffs(319, 0).addBox(0, -0.5F, 2.8876F, 0, 2, 0, 0.2F, false);
        top_handrail_connector_bottom_3_r1.texOffs(319, 0).addBox(0, -0.5F, -8.1124F, 0, 2, 0, 0.2F, false);

        top_handrail_bottom_right_r1 = new ModelMapper(modelDataWrapper);
        top_handrail_bottom_right_r1.setPos(-6.9124F, -31, -6.8876F);
        window_handrails.addChild(top_handrail_bottom_right_r1);
        setRotationAngle(top_handrail_bottom_right_r1, 1.5708F, 0, 0);
        top_handrail_bottom_right_r1.texOffs(319, 0).addBox(0, -7, 0, 0, 14, 0, 0.2F, false);

        top_handrail_bottom_left_r1 = new ModelMapper(modelDataWrapper);
        top_handrail_bottom_left_r1.setPos(-6.9124F, -31, 6.8876F);
        window_handrails.addChild(top_handrail_bottom_left_r1);
        setRotationAngle(top_handrail_bottom_left_r1, -1.5708F, 0, 0);
        top_handrail_bottom_left_r1.texOffs(319, 0).addBox(0, -7, 0, 0, 14, 0, 0.2F, false);

        top_handrail_right_4_r1 = new ModelMapper(modelDataWrapper);
        top_handrail_right_4_r1.setPos(-6.5892F, -31, -14.5938F);
        window_handrails.addChild(top_handrail_right_4_r1);
        setRotationAngle(top_handrail_right_4_r1, 1.5708F, -0.5236F, 0);
        top_handrail_right_4_r1.texOffs(319, 0).addBox(0, -0.5F, 0, 0, 1, 0, 0.2F, false);

        top_handrail_right_3_r1 = new ModelMapper(modelDataWrapper);
        top_handrail_right_3_r1.setPos(-5.7062F, -31, -15.4768F);
        window_handrails.addChild(top_handrail_right_3_r1);
        setRotationAngle(top_handrail_right_3_r1, 1.5708F, -1.0472F, 0);
        top_handrail_right_3_r1.texOffs(319, 0).addBox(0, -0.5F, 0, 0, 1, 0, 0.2F, false);

        top_handrail_right_2_r1 = new ModelMapper(modelDataWrapper);
        top_handrail_right_2_r1.setPos(-5, -33.5F, -15.8F);
        window_handrails.addChild(top_handrail_right_2_r1);
        setRotationAngle(top_handrail_right_2_r1, 0, 0, 1.5708F);
        top_handrail_right_2_r1.texOffs(319, 0).addBox(2.5F, -5, 0, 0, 5, 0, 0.2F, false);

        top_handrail_left_4_r1 = new ModelMapper(modelDataWrapper);
        top_handrail_left_4_r1.setPos(-6.5892F, -31, 14.5938F);
        window_handrails.addChild(top_handrail_left_4_r1);
        setRotationAngle(top_handrail_left_4_r1, -1.5708F, 0.5236F, 0);
        top_handrail_left_4_r1.texOffs(319, 0).addBox(0, -0.5F, 0, 0, 1, 0, 0.2F, false);

        top_handrail_left_3_r1 = new ModelMapper(modelDataWrapper);
        top_handrail_left_3_r1.setPos(-5.7062F, -31, 15.4768F);
        window_handrails.addChild(top_handrail_left_3_r1);
        setRotationAngle(top_handrail_left_3_r1, -1.5708F, 1.0472F, 0);
        top_handrail_left_3_r1.texOffs(319, 0).addBox(0, -0.5F, 0, 0, 1, 0, 0.2F, false);

        top_handrail_left_2_r1 = new ModelMapper(modelDataWrapper);
        top_handrail_left_2_r1.setPos(-2.5F, -31, 15.8F);
        window_handrails.addChild(top_handrail_left_2_r1);
        setRotationAngle(top_handrail_left_2_r1, 0, 0, -1.5708F);
        top_handrail_left_2_r1.texOffs(319, 0).addBox(0, -2.5F, 0, 0, 5, 0, 0.2F, false);

        handrail_strap_3 = new ModelMapper(modelDataWrapper);
        handrail_strap_3.setPos(0, 0, 0);
        window_handrails.addChild(handrail_strap_3);
        handrail_strap_3.texOffs(0, 0).addBox(-8, -32, -13, 2, 4, 0, 0, false);
        handrail_strap_3.texOffs(0, 0).addBox(-8, -32, -9, 2, 4, 0, 0, false);
        handrail_strap_3.texOffs(0, 0).addBox(-8, -32, -4, 2, 4, 0, 0, false);
        handrail_strap_3.texOffs(0, 0).addBox(-8, -32, 4, 2, 4, 0, 0, false);
        handrail_strap_3.texOffs(0, 0).addBox(-8, -32, 9, 2, 4, 0, 0, false);
        handrail_strap_3.texOffs(0, 0).addBox(-8, -32, 13, 2, 4, 0, 0, false);

        seat = new ModelMapper(modelDataWrapper);
        seat.setPos(0, 0, 0);
        window_handrails.addChild(seat);
        seat.texOffs(188, 145).addBox(-18, -6, -22, 7, 1, 44, 0, false);

        seat_back_r1 = new ModelMapper(modelDataWrapper);
        seat_back_r1.setPos(-18, -6.5F, 0);
        seat.addChild(seat_back_r1);
        setRotationAngle(seat_back_r1, 0, 0, -0.0873F);
        seat_back_r1.texOffs(0, 190).addBox(0, -6, -22.5F, 1, 4, 45, 0, false);

        roof_exterior = new ModelMapper(modelDataWrapper);
        roof_exterior.setPos(0, 24, 0);
        roof_exterior.texOffs(52, 46).addBox(-5.7229F, -41.9636F, -20, 6, 0, 40, 0, false);

        outer_roof_4_r1 = new ModelMapper(modelDataWrapper);
        outer_roof_4_r1.setPos(-9.662F, -41.269F, 0);
        roof_exterior.addChild(outer_roof_4_r1);
        setRotationAngle(outer_roof_4_r1, 0, 0, -0.1745F);
        outer_roof_4_r1.texOffs(76, 95).addBox(-4, 0, -20, 8, 0, 40, 0, false);

        outer_roof_3_r1 = new ModelMapper(modelDataWrapper);
        outer_roof_3_r1.setPos(-15.3333F, -39.5745F, 0);
        roof_exterior.addChild(outer_roof_3_r1);
        setRotationAngle(outer_roof_3_r1, 0, 0, -0.5236F);
        outer_roof_3_r1.texOffs(0, 46).addBox(-2, 0, -20, 4, 0, 40, 0, false);

        outer_roof_2_r1 = new ModelMapper(modelDataWrapper);
        outer_roof_2_r1.setPos(-17.8145F, -37.2749F, 0);
        roof_exterior.addChild(outer_roof_2_r1);
        setRotationAngle(outer_roof_2_r1, 0, 0, -1.0472F);
        outer_roof_2_r1.texOffs(92, 95).addBox(-1.5F, 0, -20, 3, 0, 40, 0, false);

        outer_roof_1_r1 = new ModelMapper(modelDataWrapper);
        outer_roof_1_r1.setPos(-20, -14, 0);
        roof_exterior.addChild(outer_roof_1_r1);
        setRotationAngle(outer_roof_1_r1, 0, 0, 0.1107F);
        outer_roof_1_r1.texOffs(188, 195).addBox(-1, -22, -20, 1, 4, 40, 0, false);

        door = new ModelMapper(modelDataWrapper);
        door.setPos(0, 24, 0);
        door.texOffs(124, 195).addBox(-20, 0, -16, 20, 1, 32, 0, false);

        door_left = new ModelMapper(modelDataWrapper);
        door_left.setPos(0, 0, 0);
        door.addChild(door_left);
        door_left.texOffs(108, 181).addBox(-20, -14, 0, 0, 14, 14, 0, false);

        door_left_top_r1 = new ModelMapper(modelDataWrapper);
        door_left_top_r1.setPos(-21, -14, 0);
        door_left.addChild(door_left_top_r1);
        setRotationAngle(door_left_top_r1, 0, 0, 0.1107F);
        door_left_top_r1.texOffs(196, 76).addBox(1, -19, 0, 0, 19, 14, 0, false);

        door_right = new ModelMapper(modelDataWrapper);
        door_right.setPos(0, 0, 0);
        door.addChild(door_right);
        door_right.texOffs(156, 23).addBox(-20, -14, -14, 0, 14, 14, 0, false);

        door_right_top_r1 = new ModelMapper(modelDataWrapper);
        door_right_top_r1.setPos(-21, -14, 0);
        door_right.addChild(door_right_top_r1);
        setRotationAngle(door_right_top_r1, 0, 0, 0.1107F);
        door_right_top_r1.texOffs(188, 155).addBox(1, -19, -14, 0, 19, 14, 0, false);

        door_exterior_1 = new ModelMapper(modelDataWrapper);
        door_exterior_1.setPos(0, 24, 0);
        door_exterior_1.texOffs(232, 55).addBox(-20, 0, -16, 1, 4, 32, 0, false);

        door_left_exterior_1 = new ModelMapper(modelDataWrapper);
        door_left_exterior_1.setPos(0, 0, 0);
        door_exterior_1.addChild(door_left_exterior_1);
        door_left_exterior_1.texOffs(50, 261).addBox(-21, -14, 0, 1, 16, 14, 0, false);

        door_left_top_1_r1 = new ModelMapper(modelDataWrapper);
        door_left_top_1_r1.setPos(-21, -14, 0);
        door_left_exterior_1.addChild(door_left_top_1_r1);
        setRotationAngle(door_left_top_1_r1, 0, 0, 0.1107F);
        door_left_top_1_r1.texOffs(246, 144).addBox(0, -19, 0, 1, 19, 14, 0, false);

        door_right_exterior_1 = new ModelMapper(modelDataWrapper);
        door_right_exterior_1.setPos(0, 0, 0);
        door_exterior_1.addChild(door_right_exterior_1);
        door_right_exterior_1.texOffs(196, 195).addBox(-21, -14, -14, 1, 16, 14, 0, false);

        door_right_top_1_r1 = new ModelMapper(modelDataWrapper);
        door_right_top_1_r1.setPos(-21, -14, 0);
        door_right_exterior_1.addChild(door_right_top_1_r1);
        setRotationAngle(door_right_top_1_r1, 0, 0, 0.1107F);
        door_right_top_1_r1.texOffs(0, 0).addBox(0, -19, -14, 1, 19, 14, 0, false);

        door_exterior_2 = new ModelMapper(modelDataWrapper);
        door_exterior_2.setPos(0, 24, 0);
        door_exterior_2.texOffs(232, 55).addBox(19, 0, -16, 1, 4, 32, 0, true);

        door_left_exterior_2 = new ModelMapper(modelDataWrapper);
        door_left_exterior_2.setPos(0, 0, 0);
        door_exterior_2.addChild(door_left_exterior_2);
        door_left_exterior_2.texOffs(50, 261).addBox(20, -14, 0, 1, 16, 14, 0, true);

        door_left_top_2_r1 = new ModelMapper(modelDataWrapper);
        door_left_top_2_r1.setPos(20.0061F, -13.8896F, 0);
        door_left_exterior_2.addChild(door_left_top_2_r1);
        setRotationAngle(door_left_top_2_r1, 0, 0, -0.1107F);
        door_left_top_2_r1.texOffs(246, 144).addBox(0, -19, 0, 1, 19, 14, 0, true);

        door_right_exterior_2 = new ModelMapper(modelDataWrapper);
        door_right_exterior_2.setPos(0, 0, 0);
        door_exterior_2.addChild(door_right_exterior_2);
        door_right_exterior_2.texOffs(196, 195).addBox(20, -14, -14, 1, 16, 14, 0, true);

        door_right_top_2_r1 = new ModelMapper(modelDataWrapper);
        door_right_top_2_r1.setPos(20.0061F, -13.8896F, 0);
        door_right_exterior_2.addChild(door_right_top_2_r1);
        setRotationAngle(door_right_top_2_r1, 0, 0, -0.1107F);
        door_right_top_2_r1.texOffs(0, 0).addBox(0, -19, -14, 1, 19, 14, 0, true);

        end = new ModelMapper(modelDataWrapper);
        end.setPos(0, 24, 0);
        end.texOffs(144, 66).addBox(-20, 0, -12, 40, 1, 20, 0, false);
        end.texOffs(126, 228).addBox(9.5F, -35, -12, 9, 35, 18, 0, false);
        end.texOffs(126, 228).addBox(-18.5F, -35, -12, 9, 35, 18, 0, true);
        end.texOffs(230, 91).addBox(-9.5F, -35, -12, 19, 3, 18, 0, false);
        end.texOffs(90, 119).addBox(-20, -14, 6, 2, 14, 5, 0, true);
        end.texOffs(0, 119).addBox(18, -14, 6, 2, 14, 5, 0, false);

        upper_wall_2_r1 = new ModelMapper(modelDataWrapper);
        upper_wall_2_r1.setPos(-10.8104F, -10.5767F, -41);
        end.addChild(upper_wall_2_r1);
        setRotationAngle(upper_wall_2_r1, 0, 0, -0.1107F);
        upper_wall_2_r1.texOffs(245, 61).addBox(29, -19, 47, 2, 19, 5, 0, false);

        upper_wall_1_r1 = new ModelMapper(modelDataWrapper);
        upper_wall_1_r1.setPos(-20.9939F, -14.1104F, -41);
        end.addChild(upper_wall_1_r1);
        setRotationAngle(upper_wall_1_r1, 0, 0, 0.1107F);
        upper_wall_1_r1.texOffs(246, 261).addBox(1, -19, 47, 2, 19, 5, 0, true);

        end_exterior_1 = new ModelMapper(modelDataWrapper);
        end_exterior_1.setPos(0, 24, 0);
        end_exterior_1.texOffs(244, 120).addBox(19, 0, -12, 1, 4, 20, 0, true);
        end_exterior_1.texOffs(0, 190).addBox(-20, 0, -12, 1, 4, 20, 0, false);
        end_exterior_1.texOffs(146, 0).addBox(18, -14, -12, 2, 14, 23, 0, true);
        end_exterior_1.texOffs(146, 0).addBox(-20, -14, -12, 2, 14, 23, 0, false);
        end_exterior_1.texOffs(144, 95).addBox(9.5F, -34, -12, 9, 34, 0, 0, false);
        end_exterior_1.texOffs(144, 95).addBox(-18.5F, -34, -12, 9, 34, 0, 0, true);
        end_exterior_1.texOffs(198, 45).addBox(-18, -41, -12, 36, 7, 0, 0, false);

        upper_wall_2_r2 = new ModelMapper(modelDataWrapper);
        upper_wall_2_r2.setPos(-20, -14, 0);
        end_exterior_1.addChild(upper_wall_2_r2);
        setRotationAngle(upper_wall_2_r2, 0, 0, 0.1107F);
        upper_wall_2_r2.texOffs(0, 119).addBox(0, -19, -12, 2, 19, 23, 0, false);

        upper_wall_1_r2 = new ModelMapper(modelDataWrapper);
        upper_wall_1_r2.setPos(20, -14, 0);
        end_exterior_1.addChild(upper_wall_1_r2);
        setRotationAngle(upper_wall_1_r2, 0, 0, -0.1107F);
        upper_wall_1_r2.texOffs(0, 119).addBox(-2, -19, -12, 2, 19, 23, 0, true);

        end_exterior_2 = new ModelMapper(modelDataWrapper);
        end_exterior_2.setPos(0, 24, 0);
        end_exterior_2.texOffs(244, 120).addBox(19, 0, -12, 1, 4, 20, 0, true);
        end_exterior_2.texOffs(0, 190).addBox(-20, 0, -12, 1, 4, 20, 0, false);
        end_exterior_2.texOffs(180, 239).addBox(18, -14, -12, 2, 14, 23, 0, true);
        end_exterior_2.texOffs(180, 239).addBox(-20, -14, -12, 2, 14, 23, 0, false);
        end_exterior_2.texOffs(144, 95).addBox(9.5F, -34, -12, 9, 34, 0, 0, false);
        end_exterior_2.texOffs(144, 95).addBox(-18.5F, -34, -12, 9, 34, 0, 0, true);
        end_exterior_2.texOffs(198, 45).addBox(-18, -41, -12, 36, 7, 0, 0, false);

        upper_wall_3_r1 = new ModelMapper(modelDataWrapper);
        upper_wall_3_r1.setPos(-20, -14, 0);
        end_exterior_2.addChild(upper_wall_3_r1);
        setRotationAngle(upper_wall_3_r1, 0, 0, 0.1107F);
        upper_wall_3_r1.texOffs(0, 239).addBox(0, -19, -12, 2, 19, 23, 0, false);

        upper_wall_2_r3 = new ModelMapper(modelDataWrapper);
        upper_wall_2_r3.setPos(20, -14, 0);
        end_exterior_2.addChild(upper_wall_2_r3);
        setRotationAngle(upper_wall_2_r3, 0, 0, -0.1107F);
        upper_wall_2_r3.texOffs(0, 239).addBox(-2, -19, -12, 2, 19, 23, 0, true);

        roof_end_exterior = new ModelMapper(modelDataWrapper);
        roof_end_exterior.setPos(0, 24, 0);
        roof_end_exterior.texOffs(116, 95).addBox(-8, -43, 0, 16, 2, 48, 0, false);

        vent_2_r1 = new ModelMapper(modelDataWrapper);
        vent_2_r1.setPos(-8, -43, 0);
        roof_end_exterior.addChild(vent_2_r1);
        setRotationAngle(vent_2_r1, 0, 0, -0.3491F);
        vent_2_r1.texOffs(122, 145).addBox(-9, 0, 0, 9, 2, 48, 0, false);

        vent_1_r1 = new ModelMapper(modelDataWrapper);
        vent_1_r1.setPos(8, -43, 0);
        roof_end_exterior.addChild(vent_1_r1);
        setRotationAngle(vent_1_r1, 0, 0, 0.3491F);
        vent_1_r1.texOffs(122, 145).addBox(0, 0, 0, 9, 2, 48, 0, true);

        outer_roof_1 = new ModelMapper(modelDataWrapper);
        outer_roof_1.setPos(0, 0, 0);
        roof_end_exterior.addChild(outer_roof_1);
        outer_roof_1.texOffs(207, 239).addBox(-5.7219F, -41.9631F, -12, 6, 1, 20, 0, false);

        outer_roof_4_r2 = new ModelMapper(modelDataWrapper);
        outer_roof_4_r2.setPos(-9.4875F, -40.2837F, -8);
        outer_roof_1.addChild(outer_roof_4_r2);
        setRotationAngle(outer_roof_4_r2, 0, 0, -0.1745F);
        outer_roof_4_r2.texOffs(108, 163).addBox(-4, -1, -4, 8, 1, 20, 0, false);

        outer_roof_3_r2 = new ModelMapper(modelDataWrapper);
        outer_roof_3_r2.setPos(-14.3994F, -38.9579F, -8);
        outer_roof_1.addChild(outer_roof_3_r2);
        setRotationAngle(outer_roof_3_r2, 0, 0, -0.5236F);
        outer_roof_3_r2.texOffs(94, 215).addBox(-2.5F, -1, -4, 4, 1, 20, 0, false);

        outer_roof_2_r2 = new ModelMapper(modelDataWrapper);
        outer_roof_2_r2.setPos(-16.6984F, -37.2079F, -8);
        outer_roof_1.addChild(outer_roof_2_r2);
        setRotationAngle(outer_roof_2_r2, 0, 0, -1.0472F);
        outer_roof_2_r2.texOffs(239, 240).addBox(-2, -1, -4, 3, 1, 20, 0, false);

        outer_roof_1_r2 = new ModelMapper(modelDataWrapper);
        outer_roof_1_r2.setPos(-20, -14, 0);
        outer_roof_1.addChild(outer_roof_1_r2);
        setRotationAngle(outer_roof_1_r2, 0, 0, 0.1107F);
        outer_roof_1_r2.texOffs(188, 145).addBox(-1, -22, -12, 1, 4, 20, 0, false);

        outer_roof_2 = new ModelMapper(modelDataWrapper);
        outer_roof_2.setPos(0, 0, 0);
        roof_end_exterior.addChild(outer_roof_2);
        outer_roof_2.texOffs(207, 239).addBox(-0.2781F, -41.9631F, -12, 6, 1, 20, 0, true);

        outer_roof_4_r3 = new ModelMapper(modelDataWrapper);
        outer_roof_4_r3.setPos(9.4875F, -40.2837F, -8);
        outer_roof_2.addChild(outer_roof_4_r3);
        setRotationAngle(outer_roof_4_r3, 0, 0, 0.1745F);
        outer_roof_4_r3.texOffs(108, 163).addBox(-4, -1, -4, 8, 1, 20, 0, true);

        outer_roof_3_r3 = new ModelMapper(modelDataWrapper);
        outer_roof_3_r3.setPos(15.2654F, -38.4579F, -8);
        outer_roof_2.addChild(outer_roof_3_r3);
        setRotationAngle(outer_roof_3_r3, 0, 0, 0.5236F);
        outer_roof_3_r3.texOffs(94, 215).addBox(-2.5F, -1, -4, 4, 1, 20, 0, true);

        outer_roof_2_r3 = new ModelMapper(modelDataWrapper);
        outer_roof_2_r3.setPos(16.6984F, -37.2079F, -8);
        outer_roof_2.addChild(outer_roof_2_r3);
        setRotationAngle(outer_roof_2_r3, 0, 0, 1.0472F);
        outer_roof_2_r3.texOffs(239, 240).addBox(-1, -1, -4, 3, 1, 20, 0, true);

        outer_roof_1_r3 = new ModelMapper(modelDataWrapper);
        outer_roof_1_r3.setPos(20, -14, 0);
        outer_roof_2.addChild(outer_roof_1_r3);
        setRotationAngle(outer_roof_1_r3, 0, 0, -0.1107F);
        outer_roof_1_r3.texOffs(188, 145).addBox(0, -22, -12, 1, 4, 20, 0, true);

        roof_light = new ModelMapper(modelDataWrapper);
        roof_light.setPos(0, 24, 0);
        roof_light.texOffs(0, 46).addBox(-7.75F, -33.891F, -24, 2, 0, 48, 0, false);

        roof_end_light = new ModelMapper(modelDataWrapper);
        roof_end_light.setPos(0, 24, 0);
        roof_end_light.texOffs(0, 0).addBox(-7.75F, -33.891F, 6, 2, 0, 34, 0, false);
        roof_end_light.texOffs(0, 0).addBox(5.75F, -33.891F, 6, 2, 0, 34, 0, false);

        roof_head_exterior = new ModelMapper(modelDataWrapper);
        roof_head_exterior.setPos(0, 24, 0);
        roof_head_exterior.texOffs(116, 95).addBox(-8, -43, 0, 16, 2, 48, 0, false);

        vent_3_r1 = new ModelMapper(modelDataWrapper);
        vent_3_r1.setPos(-8, -43, 0);
        roof_head_exterior.addChild(vent_3_r1);
        setRotationAngle(vent_3_r1, 0, 0, -0.3491F);
        vent_3_r1.texOffs(122, 145).addBox(-9, 0, 0, 9, 2, 48, 0, false);

        vent_2_r2 = new ModelMapper(modelDataWrapper);
        vent_2_r2.setPos(8, -43, 0);
        roof_head_exterior.addChild(vent_2_r2);
        setRotationAngle(vent_2_r2, 0, 0, 0.3491F);
        vent_2_r2.texOffs(122, 145).addBox(0, 0, 0, 9, 2, 48, 0, true);

        outer_roof_4 = new ModelMapper(modelDataWrapper);
        outer_roof_4.setPos(0, 0, 6);
        roof_head_exterior.addChild(outer_roof_4);
        outer_roof_4.texOffs(47, 190).addBox(-0.2781F, -41.9631F, -18, 6, 1, 20, 0, true);

        outer_roof_7_r1 = new ModelMapper(modelDataWrapper);
        outer_roof_7_r1.setPos(20.1411F, -14.0157F, -15.9351F);
        outer_roof_4.addChild(outer_roof_7_r1);
        setRotationAngle(outer_roof_7_r1, 0, 0.0698F, -0.1107F);
        outer_roof_7_r1.texOffs(146, 0).addBox(-1, -22, -11, 2, 1, 9, 0, true);

        outer_roof_6_r1 = new ModelMapper(modelDataWrapper);
        outer_roof_6_r1.setPos(20, -14, 0);
        outer_roof_4.addChild(outer_roof_6_r1);
        setRotationAngle(outer_roof_6_r1, 0, 0, -0.1107F);
        outer_roof_6_r1.texOffs(24, 28).addBox(0, -22, -18, 1, 1, 6, 0, true);
        outer_roof_6_r1.texOffs(47, 215).addBox(0, -22, -12, 1, 4, 14, 0, true);

        outer_roof_4_r4 = new ModelMapper(modelDataWrapper);
        outer_roof_4_r4.setPos(9.4875F, -40.2837F, -8);
        outer_roof_4.addChild(outer_roof_4_r4);
        setRotationAngle(outer_roof_4_r4, 0, 0, 0.1745F);
        outer_roof_4_r4.texOffs(54, 119).addBox(-4, -1, -10, 8, 1, 20, 0, true);

        outer_roof_3_r4 = new ModelMapper(modelDataWrapper);
        outer_roof_3_r4.setPos(15.2654F, -38.4579F, -8);
        outer_roof_4.addChild(outer_roof_3_r4);
        setRotationAngle(outer_roof_3_r4, 0, 0, 0.5236F);
        outer_roof_3_r4.texOffs(0, 72).addBox(-2.5F, -1, -10, 4, 1, 20, 0, true);

        outer_roof_2_r4 = new ModelMapper(modelDataWrapper);
        outer_roof_2_r4.setPos(16.6984F, -37.2079F, -8);
        outer_roof_4.addChild(outer_roof_2_r4);
        setRotationAngle(outer_roof_2_r4, 0, 0, 1.0472F);
        outer_roof_2_r4.texOffs(118, 115).addBox(-1, -1, -10, 3, 1, 20, 0, true);

        outer_roof_3 = new ModelMapper(modelDataWrapper);
        outer_roof_3.setPos(0, 0, 6);
        roof_head_exterior.addChild(outer_roof_3);
        outer_roof_3.texOffs(47, 190).addBox(-5.7219F, -41.9631F, -18, 6, 1, 20, 0, false);

        outer_roof_7_r2 = new ModelMapper(modelDataWrapper);
        outer_roof_7_r2.setPos(-22.7756F, -14.3084F, 21.9723F);
        outer_roof_3.addChild(outer_roof_7_r2);
        setRotationAngle(outer_roof_7_r2, 0, -0.0698F, 0.1107F);
        outer_roof_7_r2.texOffs(146, 0).addBox(-1, -22, -49, 2, 1, 9, 0, false);

        outer_roof_6_r2 = new ModelMapper(modelDataWrapper);
        outer_roof_6_r2.setPos(-20, -14, 0);
        outer_roof_3.addChild(outer_roof_6_r2);
        setRotationAngle(outer_roof_6_r2, 0, 0, 0.1107F);
        outer_roof_6_r2.texOffs(24, 28).addBox(-1, -22, -18, 1, 1, 6, 0, false);
        outer_roof_6_r2.texOffs(47, 215).addBox(-1, -22, -12, 1, 4, 14, 0, false);

        outer_roof_4_r5 = new ModelMapper(modelDataWrapper);
        outer_roof_4_r5.setPos(-9.4875F, -40.2837F, -8);
        outer_roof_3.addChild(outer_roof_4_r5);
        setRotationAngle(outer_roof_4_r5, 0, 0, -0.1745F);
        outer_roof_4_r5.texOffs(54, 119).addBox(-4, -1, -10, 8, 1, 20, 0, false);

        outer_roof_3_r5 = new ModelMapper(modelDataWrapper);
        outer_roof_3_r5.setPos(-14.3994F, -38.9579F, -8);
        outer_roof_3.addChild(outer_roof_3_r5);
        setRotationAngle(outer_roof_3_r5, 0, 0, -0.5236F);
        outer_roof_3_r5.texOffs(0, 72).addBox(-2.5F, -1, -10, 4, 1, 20, 0, false);

        outer_roof_2_r5 = new ModelMapper(modelDataWrapper);
        outer_roof_2_r5.setPos(-16.6984F, -37.2079F, -8);
        outer_roof_3.addChild(outer_roof_2_r5);
        setRotationAngle(outer_roof_2_r5, 0, 0, -1.0472F);
        outer_roof_2_r5.texOffs(118, 115).addBox(-2, -1, -10, 3, 1, 20, 0, false);

        head = new ModelMapper(modelDataWrapper);
        head.setPos(0, 24, 0);
        head.texOffs(144, 87).addBox(-20, 0, 6, 40, 1, 2, 0, false);
        head.texOffs(0, 119).addBox(18, -14, 6, 2, 14, 5, 0, false);
        head.texOffs(90, 119).addBox(-20, -14, 6, 2, 14, 5, 0, true);
        head.texOffs(230, 195).addBox(-18.5F, -35, 6, 37, 35, 0, 0, false);

        upper_wall_2_r4 = new ModelMapper(modelDataWrapper);
        upper_wall_2_r4.setPos(-20, -14, 0);
        head.addChild(upper_wall_2_r4);
        setRotationAngle(upper_wall_2_r4, 0, 0, 0.1107F);
        upper_wall_2_r4.texOffs(246, 261).addBox(0, -19, 6, 2, 19, 5, 0, true);

        upper_wall_1_r3 = new ModelMapper(modelDataWrapper);
        upper_wall_1_r3.setPos(20, -14, 0);
        head.addChild(upper_wall_1_r3);
        setRotationAngle(upper_wall_1_r3, 0, 0, -0.1107F);
        upper_wall_1_r3.texOffs(245, 61).addBox(-2, -19, 6, 2, 19, 5, 0, false);

        head_exterior = new ModelMapper(modelDataWrapper);
        head_exterior.setPos(0, 24, 0);
        head_exterior.texOffs(60, 215).addBox(19, -14, -21, 1, 14, 32, 0, false);
        head_exterior.texOffs(60, 215).addBox(-20, -14, -21, 1, 14, 32, 0, true);
        head_exterior.texOffs(0, 0).addBox(-20, 0, -33, 40, 4, 38, 0, false);
        head_exterior.texOffs(11, 225).addBox(19, 0, 5, 1, 4, 4, 0, false);
        head_exterior.texOffs(11, 225).addBox(-20, 0, 5, 1, 4, 4, 0, true);
        head_exterior.texOffs(198, 41).addBox(-20, 0, 3, 40, 1, 3, 0, false);
        head_exterior.texOffs(198, 0).addBox(-18.5F, -41, 5, 37, 41, 0, 0, false);
        head_exterior.texOffs(40, 310).addBox(-15.4897F, 2.3061F, -32.1965F, 4, 4, 5, 0, false);
        head_exterior.texOffs(40, 310).addBox(11.5103F, 2.3061F, -32.1965F, 4, 4, 5, 0, false);

        upper_wall_2_r5 = new ModelMapper(modelDataWrapper);
        upper_wall_2_r5.setPos(-20, -14, -15);
        head_exterior.addChild(upper_wall_2_r5);
        setRotationAngle(upper_wall_2_r5, 0, 0, 0.1107F);
        upper_wall_2_r5.texOffs(196, 87).addBox(0, -21, -6, 1, 21, 32, 0, true);

        upper_wall_1_r4 = new ModelMapper(modelDataWrapper);
        upper_wall_1_r4.setPos(20, -14, 0);
        head_exterior.addChild(upper_wall_1_r4);
        setRotationAngle(upper_wall_1_r4, 0, 0, -0.1107F);
        upper_wall_1_r4.texOffs(196, 87).addBox(-1, -21, -21, 1, 21, 32, 0, false);

        front = new ModelMapper(modelDataWrapper);
        front.setPos(0, 0, 0);
        head_exterior.addChild(front);


        side_1 = new ModelMapper(modelDataWrapper);
        side_1.setPos(0, 0, 0);
        front.addChild(side_1);


        outer_roof_5_r1 = new ModelMapper(modelDataWrapper);
        outer_roof_5_r1.setPos(3.2219F, -40.7466F, -18.8935F);
        side_1.addChild(outer_roof_5_r1);
        setRotationAngle(outer_roof_5_r1, 0.1745F, 0, 0);
        outer_roof_5_r1.texOffs(12, 72).addBox(-3.5F, 0, -9, 6, 0, 16, 0, false);

        outer_roof_4_r6 = new ModelMapper(modelDataWrapper);
        outer_roof_4_r6.setPos(10.4347F, -39.8969F, -18.8935F);
        side_1.addChild(outer_roof_4_r6);
        setRotationAngle(outer_roof_4_r6, 0.1745F, 0, 0.1745F);
        outer_roof_4_r6.texOffs(11, 119).addBox(-5, 0, -9, 10, 0, 16, 0, false);

        outer_roof_3_r6 = new ModelMapper(modelDataWrapper);
        outer_roof_3_r6.setPos(14.4421F, -39.0318F, -18.94F);
        side_1.addChild(outer_roof_3_r6);
        setRotationAngle(outer_roof_3_r6, 0.1309F, 0, 0.5236F);
        outer_roof_3_r6.texOffs(0, 46).addBox(-1.5F, 0, -8, 4, 0, 15, 0, false);

        outer_roof_2_r6 = new ModelMapper(modelDataWrapper);
        outer_roof_2_r6.setPos(17.7118F, -37.2157F, -12.993F);
        side_1.addChild(outer_roof_2_r6);
        setRotationAngle(outer_roof_2_r6, 0.1178F, 0, 1.0472F);
        outer_roof_2_r6.texOffs(0, 119).addBox(-1.5F, 0, -14, 3, 0, 15, 0, false);

        front_side_bottom_2_r1 = new ModelMapper(modelDataWrapper);
        front_side_bottom_2_r1.setPos(23.7436F, -2.2438F, -20);
        side_1.addChild(front_side_bottom_2_r1);
        setRotationAngle(front_side_bottom_2_r1, 0, 0, 0.2618F);
        front_side_bottom_2_r1.texOffs(0, 68).addBox(-2, 7, -1, 0, 5, 26, 0, false);

        front_side_bottom_1_r1 = new ModelMapper(modelDataWrapper);
        front_side_bottom_1_r1.setPos(21.153F, 0.1678F, -20.0075F);
        side_1.addChild(front_side_bottom_1_r1);
        setRotationAngle(front_side_bottom_1_r1, 0, 0.1222F, 0.2618F);
        front_side_bottom_1_r1.texOffs(0, 23).addBox(0, 4, -11, 0, 5, 10, 0, true);

        front_side_upper_1_r1 = new ModelMapper(modelDataWrapper);
        front_side_upper_1_r1.setPos(20, -14, -21);
        side_1.addChild(front_side_upper_1_r1);
        setRotationAngle(front_side_upper_1_r1, 0, 0.1222F, -0.1107F);
        front_side_upper_1_r1.texOffs(0, 51).addBox(0, -22, -10, 0, 22, 10, 0, true);

        front_side_lower_1_r1 = new ModelMapper(modelDataWrapper);
        front_side_lower_1_r1.setPos(20.9925F, 0, -21.1219F);
        side_1.addChild(front_side_lower_1_r1);
        setRotationAngle(front_side_lower_1_r1, 0, 0.1222F, 0);
        front_side_lower_1_r1.texOffs(54, 109).addBox(-1, -14, -10, 0, 18, 10, 0, true);

        side_2 = new ModelMapper(modelDataWrapper);
        side_2.setPos(0, 0, 0);
        front.addChild(side_2);


        outer_roof_5_r2 = new ModelMapper(modelDataWrapper);
        outer_roof_5_r2.setPos(-3.2219F, -40.7466F, -18.8935F);
        side_2.addChild(outer_roof_5_r2);
        setRotationAngle(outer_roof_5_r2, 0.1745F, 0, 0);
        outer_roof_5_r2.texOffs(12, 72).addBox(-2.5F, 0, -9, 6, 0, 16, 0, true);

        outer_roof_4_r7 = new ModelMapper(modelDataWrapper);
        outer_roof_4_r7.setPos(-10.4347F, -39.8969F, -18.8935F);
        side_2.addChild(outer_roof_4_r7);
        setRotationAngle(outer_roof_4_r7, 0.1745F, 0, -0.1745F);
        outer_roof_4_r7.texOffs(11, 119).addBox(-5, 0, -9, 10, 0, 16, 0, true);

        outer_roof_3_r7 = new ModelMapper(modelDataWrapper);
        outer_roof_3_r7.setPos(-13.0465F, -36.6147F, -9.4949F);
        side_2.addChild(outer_roof_3_r7);
        setRotationAngle(outer_roof_3_r7, 0.1309F, 0, -0.5236F);
        outer_roof_3_r7.texOffs(0, 46).addBox(-2.5F, -4, -17, 4, 0, 15, 0, true);

        outer_roof_2_r7 = new ModelMapper(modelDataWrapper);
        outer_roof_2_r7.setPos(-18.4243F, -37.627F, -6.0415F);
        side_2.addChild(outer_roof_2_r7);
        setRotationAngle(outer_roof_2_r7, 0.1178F, 0, -1.0472F);
        outer_roof_2_r7.texOffs(0, 119).addBox(-1.5F, 0, -21, 3, 0, 15, 0, true);

        front_side_bottom_4_r1 = new ModelMapper(modelDataWrapper);
        front_side_bottom_4_r1.setPos(-19.8799F, -3.2792F, -20);
        side_2.addChild(front_side_bottom_4_r1);
        setRotationAngle(front_side_bottom_4_r1, 0, 0, -0.2618F);
        front_side_bottom_4_r1.texOffs(0, 68).addBox(-2, 7, -1, 0, 5, 26, 0, true);

        front_side_bottom_3_r1 = new ModelMapper(modelDataWrapper);
        front_side_bottom_3_r1.setPos(-19.8589F, 4.9974F, -20.0073F);
        side_2.addChild(front_side_bottom_3_r1);
        setRotationAngle(front_side_bottom_3_r1, 0, -0.1222F, -0.2618F);
        front_side_bottom_3_r1.texOffs(0, 23).addBox(0, -1, -11, 0, 5, 10, 0, false);

        front_side_upper_2_r1 = new ModelMapper(modelDataWrapper);
        front_side_upper_2_r1.setPos(-20, -14, -21);
        side_2.addChild(front_side_upper_2_r1);
        setRotationAngle(front_side_upper_2_r1, 0, -0.1222F, 0.1107F);
        front_side_upper_2_r1.texOffs(0, 51).addBox(0, -22, -10, 0, 22, 10, 0, false);

        front_side_lower_2_r1 = new ModelMapper(modelDataWrapper);
        front_side_lower_2_r1.setPos(-19.0065F, 0, -20.878F);
        side_2.addChild(front_side_lower_2_r1);
        setRotationAngle(front_side_lower_2_r1, 0, -0.1222F, 0);
        front_side_lower_2_r1.texOffs(54, 109).addBox(-1, -14, -10, 0, 18, 10, 0, false);

        front_panel = new ModelMapper(modelDataWrapper);
        front_panel.setPos(0, 0, 2.75F);
        front.addChild(front_panel);


        panel_5_r1 = new ModelMapper(modelDataWrapper);
        panel_5_r1.setPos(24.1146F, -0.1939F, -35.4421F);
        front_panel.addChild(panel_5_r1);
        setRotationAngle(panel_5_r1, 0, 0, 0);
        panel_5_r1.texOffs(144, 66).addBox(-24.5573F, -13.5F, 2, 8, 12, 0, 0, true);

        panel_8_r1 = new ModelMapper(modelDataWrapper);
        panel_8_r1.setPos(7.5573F, -13.6939F, -33.4421F);
        front_panel.addChild(panel_8_r1);
        setRotationAngle(panel_8_r1, -0.1309F, 0, 0);
        panel_8_r1.texOffs(230, 261).addBox(-8, -27, 0, 8, 27, 0, 0, true);

        panel_9_r1 = new ModelMapper(modelDataWrapper);
        panel_9_r1.setPos(7.4897F, -13.6939F, -33.4465F);
        front_panel.addChild(panel_9_r1);
        setRotationAngle(panel_9_r1, -0.1309F, -0.1309F, 0);
        panel_9_r1.texOffs(80, 261).addBox(0, -27, 0, 13, 27, 0, 0, true);

        panel_6_r1 = new ModelMapper(modelDataWrapper);
        panel_6_r1.setPos(26.0925F, -0.1939F, -33.0147F);
        front_panel.addChild(panel_6_r1);
        setRotationAngle(panel_6_r1, 0, -0.1309F, 0);
        panel_6_r1.texOffs(144, 163).addBox(-18.5F, -13.5F, 2, 13, 12, 0, 0, true);

        panel_3_r1 = new ModelMapper(modelDataWrapper);
        panel_3_r1.setPos(15.127F, 0.2727F, -33.998F);
        front_panel.addChild(panel_3_r1);
        setRotationAngle(panel_3_r1, 0.2618F, -0.1309F, 0);
        panel_3_r1.texOffs(79, 190).addBox(-7.5F, -1.5F, 2, 13, 11, 0, 0, true);

        panel_8_r2 = new ModelMapper(modelDataWrapper);
        panel_8_r2.setPos(-7.4897F, -13.6939F, -33.4465F);
        front_panel.addChild(panel_8_r2);
        setRotationAngle(panel_8_r2, -0.1309F, 0.1309F, 0);
        panel_8_r2.texOffs(80, 261).addBox(-13, -27, 0, 13, 27, 0, 0, false);

        panel_7_r1 = new ModelMapper(modelDataWrapper);
        panel_7_r1.setPos(-7.4897F, -13.6939F, -33.4465F);
        front_panel.addChild(panel_7_r1);
        setRotationAngle(panel_7_r1, -0.1309F, 0, 0);
        panel_7_r1.texOffs(230, 261).addBox(0, -27, 0, 8, 27, 0, 0, false);

        panel_5_r2 = new ModelMapper(modelDataWrapper);
        panel_5_r2.setPos(-26.0925F, -0.1939F, -33.0147F);
        front_panel.addChild(panel_5_r2);
        setRotationAngle(panel_5_r2, 0, 0.1309F, 0);
        panel_5_r2.texOffs(144, 163).addBox(5.5F, -13.5F, 2, 13, 12, 0, 0, false);

        panel_4_r1 = new ModelMapper(modelDataWrapper);
        panel_4_r1.setPos(-24.1146F, -0.1939F, -35.4421F);
        front_panel.addChild(panel_4_r1);
        setRotationAngle(panel_4_r1, 0, 0, 0);
        panel_4_r1.texOffs(144, 66).addBox(16.5573F, -13.5F, 2, 8, 12, 0, 0, false);

        panel_4_r2 = new ModelMapper(modelDataWrapper);
        panel_4_r2.setPos(24.1146F, 0.2727F, -34.9857F);
        front_panel.addChild(panel_4_r2);
        setRotationAngle(panel_4_r2, 0.2618F, 0, 0);
        panel_4_r2.texOffs(146, 10).addBox(-24.5573F, -1.5F, 2, 8, 11, 0, 0, true);

        panel_3_r2 = new ModelMapper(modelDataWrapper);
        panel_3_r2.setPos(-24.1146F, 0.2727F, -34.9857F);
        front_panel.addChild(panel_3_r2);
        setRotationAngle(panel_3_r2, 0.2618F, 0, 0);
        panel_3_r2.texOffs(146, 10).addBox(16.5573F, -1.5F, 2, 8, 11, 0, 0, false);

        panel_2_r1 = new ModelMapper(modelDataWrapper);
        panel_2_r1.setPos(-15.127F, 0.2727F, -33.998F);
        front_panel.addChild(panel_2_r1);
        setRotationAngle(panel_2_r1, 0.2618F, 0.1309F, 0);
        panel_2_r1.texOffs(79, 190).addBox(-5.5F, -1.5F, 2, 13, 11, 0, 0, false);

        roof_door_light = new ModelMapper(modelDataWrapper);
        roof_door_light.setPos(0, 24, 0);
        roof_door_light.texOffs(8, 0).addBox(-4, -33.891F, -4, 4, 0, 8, 0, false);

        door_light_on = new ModelMapper(modelDataWrapper);
        door_light_on.setPos(0, 24, 0);


        light_r1 = new ModelMapper(modelDataWrapper);
        light_r1.setPos(-20, -14, 0);
        door_light_on.addChild(light_r1);
        setRotationAngle(light_r1, 0, 0, 0.1107F);
        light_r1.texOffs(32, 319).addBox(-1, -19.5F, 0, 0, 0, 0, 0.4F, false);

        door_light_off = new ModelMapper(modelDataWrapper);
        door_light_off.setPos(0, 24, 0);


        light_r2 = new ModelMapper(modelDataWrapper);
        light_r2.setPos(-20, -14, 0);
        door_light_off.addChild(light_r2);
        setRotationAngle(light_r2, 0, 0, 0.1107F);
        light_r2.texOffs(30, 319).addBox(-1, -19.5F, 0, 0, 0, 0, 0.4F, false);

        headlight = new ModelMapper(modelDataWrapper);
        headlight.setPos(0, 24, 0);


        headlight_2_r1 = new ModelMapper(modelDataWrapper);
        headlight_2_r1.setPos(2.1299F, -1.6939F, -29.4301F);
        headlight.addChild(headlight_2_r1);
        setRotationAngle(headlight_2_r1, 0, -0.1309F, 0);
        headlight_2_r1.texOffs(106, 286).addBox(5.1487F, -9, -1.9652F, 10, 9, 0, 0, true);

        headlight_1_r1 = new ModelMapper(modelDataWrapper);
        headlight_1_r1.setPos(-26.0925F, -0.1939F, -30.2647F);
        headlight.addChild(headlight_1_r1);
        setRotationAngle(headlight_1_r1, 0, 0.1309F, 0);
        headlight_1_r1.texOffs(106, 286).addBox(8.5F, -10.5F, 1.99F, 10, 9, 0, 0, false);

        tail_light = new ModelMapper(modelDataWrapper);
        tail_light.setPos(0, 24, 0);


        tail_light_2_r1 = new ModelMapper(modelDataWrapper);
        tail_light_2_r1.setPos(2.1299F, -1.6939F, -29.4301F);
        tail_light.addChild(tail_light_2_r1);
        setRotationAngle(tail_light_2_r1, 0, -0.1309F, 0);
        tail_light_2_r1.texOffs(106, 295).addBox(5.1487F, -9, -1.9652F, 10, 9, 0, 0, true);

        tail_light_1_r1 = new ModelMapper(modelDataWrapper);
        tail_light_1_r1.setPos(-26.0925F, -0.1939F, -30.2647F);
        tail_light.addChild(tail_light_1_r1);
        setRotationAngle(tail_light_1_r1, 0, 0.1309F, 0);
        tail_light_1_r1.texOffs(106, 295).addBox(8.5F, -10.5F, 1.99F, 10, 9, 0, 0, false);

        modelDataWrapper.setModelPart(textureWidth, textureHeight);
        window.setModelPart();
        window_exterior_1.setModelPart();
        window_exterior_2.setModelPart();
        window_handrails.setModelPart();
        side_panel_translucent.setModelPart();
        side_panel.setModelPart();
        roof_window.setModelPart();
        roof_door.setModelPart();
        roof_exterior.setModelPart();
        door.setModelPart();
        door_left.setModelPart(door.name);
        door_right.setModelPart(door.name);
        door_exterior_1.setModelPart();
        door_left_exterior_1.setModelPart(door_exterior_1.name);
        door_right_exterior_1.setModelPart(door_exterior_1.name);
        door_exterior_2.setModelPart();
        door_left_exterior_2.setModelPart(door_exterior_2.name);
        door_right_exterior_2.setModelPart(door_exterior_2.name);
        end.setModelPart();
        roof_end_exterior.setModelPart();
        roof_light.setModelPart();
        roof_door_light.setModelPart();
        roof_end_light.setModelPart();
        roof_head_exterior.setModelPart();
        head.setModelPart();
        head_exterior.setModelPart();
        headlight.setModelPart();
        tail_light.setModelPart();
        door_light_off.setModelPart();
        door_light_on.setModelPart();
    }

    private static final int DOOR_MAX = 13;
    private static final ModelDoorOverlay MODEL_DOOR_OVERLAY = new ModelDoorOverlay(DOOR_MAX, 6.34F, "door_overlay_q_train_left.png", "door_overlay_q_train_right.png");

    @Override
    protected void renderWindowPositions(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
        switch (renderStage) {
            case LIGHTS:
                renderMirror(roof_light, matrices, vertices, light, position);
                break;
            case INTERIOR:
                renderMirror(window, matrices, vertices, light, position);
                if (renderDetails) {
                    renderMirror(roof_window, matrices, vertices, light, position);
                    renderMirror(window_handrails, matrices, vertices, light, position);
                    renderMirror(side_panel, matrices, vertices, light, position - 22);
                    renderMirror(side_panel, matrices, vertices, light, position + 22);
                }
                break;
            case INTERIOR_TRANSLUCENT:
                renderMirror(side_panel_translucent, matrices, vertices, light, position - 22);
                renderMirror(side_panel_translucent, matrices, vertices, light, position + 22);
                break;
            case EXTERIOR:
                if (isEnd2Head) {
                    renderOnceFlipped(window_exterior_1, matrices, vertices, light, position);
                    renderOnceFlipped(window_exterior_2, matrices, vertices, light, position);
                } else {
                    renderOnce(window_exterior_1, matrices, vertices, light, position);
                    renderOnce(window_exterior_2, matrices, vertices, light, position);
                }
                renderMirror(roof_exterior, matrices, vertices, light, position);
                break;
        }
    }

    @Override
    protected void renderDoorPositions(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
        final boolean middleDoor = isIndex(getDoorPositions().length / 2, position, getDoorPositions());
        final boolean doorOpen = doorLeftZ > 0 || doorRightZ > 0;
        final boolean notLastDoor = !isIndex(0, position, getDoorPositions()) && !isIndex(-1, position, getDoorPositions());

        switch (renderStage) {
            case LIGHTS:
                renderMirror(roof_door_light, matrices, vertices, light, position);
                if (notLastDoor) {
                    renderMirror(roof_light, matrices, vertices, light, position);
                }
                if (middleDoor && doorOpen && renderDetails) {
                    renderMirror(door_light_on, matrices, vertices, light, position - 40);
                }
                break;
            case INTERIOR:
                door_left.setOffset(0, 0, doorRightZ);
                door_right.setOffset(0, 0, -doorRightZ);
                renderOnce(door, matrices, vertices, light, position);
                door_left.setOffset(0, 0, doorLeftZ);
                door_right.setOffset(0, 0, -doorLeftZ);
                renderOnceFlipped(door, matrices, vertices, light, position);

                if (renderDetails) {
                    renderMirror(roof_door, matrices, vertices, light, position);
                }
                break;
            case EXTERIOR:
                if (isEnd2Head) {
                    door_left_exterior_1.setOffset(0, 0, doorLeftZ);
                    door_right_exterior_1.setOffset(0, 0, -doorLeftZ);
                    renderOnceFlipped(door_exterior_1, matrices, vertices, light, position);
                    door_left_exterior_2.setOffset(0, 0, doorRightZ);
                    door_right_exterior_2.setOffset(0, 0, -doorRightZ);
                    renderOnceFlipped(door_exterior_2, matrices, vertices, light, position);
                } else {
                    door_left_exterior_1.setOffset(0, 0, doorRightZ);
                    door_right_exterior_1.setOffset(0, 0, -doorRightZ);
                    renderOnce(door_exterior_1, matrices, vertices, light, position);
                    door_left_exterior_2.setOffset(0, 0, doorLeftZ);
                    door_right_exterior_2.setOffset(0, 0, -doorLeftZ);
                    renderOnce(door_exterior_2, matrices, vertices, light, position);
                }
                renderMirror(roof_exterior, matrices, vertices, light, position);
                if (middleDoor && !doorOpen && renderDetails) {
                    renderMirror(door_light_off, matrices, vertices, light, position - 40);
                }
                break;
        }
    }

    @Override
    protected void renderHeadPosition1(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean useHeadlights) {
        switch (renderStage) {
            case LIGHTS:
                renderOnce(roof_end_light, matrices, vertices, light, position);
                break;
            case ALWAYS_ON_LIGHTS:
                renderOnce(useHeadlights ? headlight : tail_light, matrices, vertices, light, position);
                break;
            case INTERIOR:
                renderOnce(head, matrices, vertices, light, position);
                break;
            case EXTERIOR:
                renderOnce(head_exterior, matrices, vertices, light, position);
                renderOnce(roof_head_exterior, matrices, vertices, light, position);
                break;
        }
    }

    @Override
    protected void renderHeadPosition2(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean useHeadlights) {
        switch (renderStage) {
            case LIGHTS:
                renderOnceFlipped(roof_end_light, matrices, vertices, light, position);
                break;
            case ALWAYS_ON_LIGHTS:
                renderOnceFlipped(useHeadlights ? headlight : tail_light, matrices, vertices, light, position);
                break;
            case INTERIOR:
                renderOnceFlipped(head, matrices, vertices, light, position);
                break;
            case EXTERIOR:
                renderOnceFlipped(head_exterior, matrices, vertices, light, position);
                renderOnceFlipped(roof_head_exterior, matrices, vertices, light, position);
                break;
        }
    }

    @Override
    protected void renderEndPosition1(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
        switch (renderStage) {
            case LIGHTS:
                renderOnce(roof_end_light, matrices, vertices, light, position);
                break;
            case INTERIOR:
                renderOnce(end, matrices, vertices, light, position);
                break;
            case EXTERIOR:
                renderOnce(end_exterior_2, matrices, vertices, light, position);
                renderOnce(roof_end_exterior, matrices, vertices, light, position);
                break;
        }
    }

    @Override
    protected void renderEndPosition2(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
        switch (renderStage) {
            case LIGHTS:
                renderOnceFlipped(roof_end_light, matrices, vertices, light, position);
                break;
            case INTERIOR:
                renderOnceFlipped(end, matrices, vertices, light, position);
                break;
            case EXTERIOR:
                renderOnceFlipped(end_exterior_2, matrices, vertices, light, position);
                renderOnceFlipped(roof_end_exterior, matrices, vertices, light, position);
                break;
        }
    }

    @Override
    protected ModelDoorOverlay getModelDoorOverlay() {
        return MODEL_DOOR_OVERLAY;
    }

    @Override
    protected ModelDoorOverlayTopBase getModelDoorOverlayTop() {
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
    protected int[] getBogiePositions() {
        return new int[]{-136, 136};
    }

    @Override
    protected float getDoorAnimationX(float value, boolean opening) {
        return 0;
    }

    @Override
    protected float getDoorAnimationZ(float value, boolean opening) {
        return smoothEnds(0, DOOR_MAX, 0, 0.5F, value);
    }
}