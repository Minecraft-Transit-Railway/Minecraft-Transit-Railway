package mtr.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mtr.mappings.ModelDataWrapper;
import mtr.mappings.ModelMapper;

public class ModelRTrain extends ModelTrainBase {
    private final ModelMapper window;
    private final ModelMapper upper_wall_r1;
    private final ModelMapper window_exterior_2;
    private final ModelMapper upper_wall_r2;
    private final ModelMapper window_exterior_1;
    private final ModelMapper upper_wall_r3;
    private final ModelMapper door_exterior_2;
    private final ModelMapper door_right_exterior_2;
    private final ModelMapper door_left_top_r1;
    private final ModelMapper door_left_exterior_2;
    private final ModelMapper door_right_top_r1;
    private final ModelMapper door_exterior_1;
    private final ModelMapper door_left_exterior_1;
    private final ModelMapper door_left_top_r2;
    private final ModelMapper door_right_exterior_1;
    private final ModelMapper door_right_top_r2;
    private final ModelMapper door;
    private final ModelMapper door_left;
    private final ModelMapper door_left_top_r3;
    private final ModelMapper door_right;
    private final ModelMapper door_right_top_r3;
    private final ModelMapper end;
    private final ModelMapper upper_wall_2_r1;
    private final ModelMapper upper_wall_1_r1;
    private final ModelMapper seat_end_1;
    private final ModelMapper seat_back_1_r1;
    private final ModelMapper seat_end_2;
    private final ModelMapper seat_back_2_r1;
    private final ModelMapper seat_bottom_2_r1;
    private final ModelMapper end_exterior;
    private final ModelMapper upper_wall_2_r2;
    private final ModelMapper upper_wall_1_r2;
    private final ModelMapper seat;
    private final ModelMapper seat_back_2_r2;
    private final ModelMapper door_light;
    private final ModelMapper light_3_r1;
    private final ModelMapper side_panel;
    private final ModelMapper side_panel_translucent;
    private final ModelMapper roof_window;
    private final ModelMapper inner_roof_2_r1;
    private final ModelMapper roof_light;
    private final ModelMapper roof_light_r1;
    private final ModelMapper roof_door;
    private final ModelMapper inner_roof_3_r1;
    private final ModelMapper roof_end;
    private final ModelMapper side_1;
    private final ModelMapper inner_roof_4_r1;
    private final ModelMapper side_2;
    private final ModelMapper inner_roof_5_r1;
    private final ModelMapper roof_exterior;
    private final ModelMapper outer_roof_4_r1;
    private final ModelMapper outer_roof_3_r1;
    private final ModelMapper outer_roof_2_r1;
    private final ModelMapper outer_roof_1_r1;
    private final ModelMapper roof_end_exterior;
    private final ModelMapper side_3;
    private final ModelMapper outer_roof_3_r2;
    private final ModelMapper outer_roof_5_r1;
    private final ModelMapper outer_roof_4_r2;
    private final ModelMapper outer_roof_2_r2;
    private final ModelMapper side_4;
    private final ModelMapper outer_roof_7_r1;
    private final ModelMapper outer_roof_9_r1;
    private final ModelMapper outer_roof_10_r1;
    private final ModelMapper outer_roof_8_r1;
    private final ModelMapper top_handrail;
    private final ModelMapper pole_bottom_diagonal_2_r1;
    private final ModelMapper pole_bottom_diagonal_1_r1;
    private final ModelMapper pole_top_diagonal_2_r1;
    private final ModelMapper pole_top_diagonal_1_r1;
    private final ModelMapper top_handrail_connector_bottom_3_r1;
    private final ModelMapper top_handrail_bottom_right_r1;
    private final ModelMapper top_handrail_bottom_left_r1;
    private final ModelMapper top_handrail_right_4_r1;
    private final ModelMapper top_handrail_right_3_r1;
    private final ModelMapper top_handrail_left_4_r1;
    private final ModelMapper top_handrail_left_3_r1;
    private final ModelMapper handrail_straps;
    private final ModelMapper handrail_strap_8_r1;
    private final ModelMapper head;
    private final ModelMapper upper_wall_2_r3;
    private final ModelMapper upper_wall_1_r3;
    private final ModelMapper head_exterior;
    private final ModelMapper upper_wall_2_r4;
    private final ModelMapper upper_wall_1_r4;
    private final ModelMapper bottom_r1;
    private final ModelMapper front_side_1;
    private final ModelMapper front_side_lower_3_r1;
    private final ModelMapper front_side_upper_3_r1;
    private final ModelMapper front_side_upper_1_r1;
    private final ModelMapper front_side_2;
    private final ModelMapper front_side_lower_4_r1;
    private final ModelMapper front_side_upper_4_r1;
    private final ModelMapper front_side_upper_2_r1;
    private final ModelMapper front_panel;
    private final ModelMapper panel_6_r1;
    private final ModelMapper panel_5_r1;
    private final ModelMapper panel_4_r1;
    private final ModelMapper panel_3_r1;
    private final ModelMapper panel_2_r1;
    private final ModelMapper panel_1_r1;
    private final ModelMapper nose;
    private final ModelMapper nose_edge;
    private final ModelMapper edge_6_r1;
    private final ModelMapper edge_5_r1;
    private final ModelMapper edge_4_r1;
    private final ModelMapper edge_3_r1;
    private final ModelMapper edge_2_r1;
    private final ModelMapper edge_1_r1;
    private final ModelMapper nose_top;
    private final ModelMapper nose_top_3_r1;
    private final ModelMapper nose_top_2_r1;
    private final ModelMapper nose_top_1_r1;
    private final ModelMapper driver_door;
    private final ModelMapper driver_door_edge_roof_2_r1;
    private final ModelMapper driver_door_edge_roof_1_r1;
    private final ModelMapper driver_door_edge_upper_2_r1;
    private final ModelMapper driver_door_upper_2_r1;
    private final ModelMapper roof_vent;
    private final ModelMapper vent_1_r1;
    private final ModelMapper vent_2_r1;
    private final ModelMapper roof_head_exterior;
    private final ModelMapper side_7;
    private final ModelMapper outer_roof_12_r1;
    private final ModelMapper outer_roof_11_r1;
    private final ModelMapper outer_roof_10_r2;
    private final ModelMapper outer_roof_9_r2;
    private final ModelMapper outer_roof_8_r2;
    private final ModelMapper outer_roof_4_r3;
    private final ModelMapper outer_roof_6_r1;
    private final ModelMapper outer_roof_5_r2;
    private final ModelMapper outer_roof_3_r3;
    private final ModelMapper side_8;
    private final ModelMapper outer_roof_17_r1;
    private final ModelMapper outer_roof_16_r1;
    private final ModelMapper outer_roof_15_r1;
    private final ModelMapper outer_roof_14_r1;
    private final ModelMapper outer_roof_13_r1;
    private final ModelMapper outer_roof_8_r3;
    private final ModelMapper outer_roof_10_r3;
    private final ModelMapper outer_roof_11_r2;
    private final ModelMapper outer_roof_9_r3;
    private final ModelMapper headlights;
    private final ModelMapper headlight_2_r1;
    private final ModelMapper headlight_1_r1;
    private final ModelMapper tail_lights;
    private final ModelMapper tail_light_2_r1;
    private final ModelMapper tail_light_1_r1;
    private final ModelMapper roof_head;
    private final ModelMapper side_5;
    private final ModelMapper inner_roof_5_r2;
    private final ModelMapper side_6;
    private final ModelMapper inner_roof_6_r1;
    private final ModelMapper door_light_on;
    private final ModelMapper light_r1;
    private final ModelMapper door_light_off;
    private final ModelMapper light_r2;
    private final ModelMapper end_handrail;
    private final ModelMapper pole_top_diagonal_1_r2;
    private final ModelMapper pole_top_diagonal_2_r2;
    private final ModelMapper pole_bottom_diagonal_1_r2;
    private final ModelMapper pole_bottom_diagonal_2_r2;

    public ModelRTrain() {
        final int textureWidth = 320;
        final int textureHeight = 320;

        final ModelDataWrapper modelDataWrapper = new ModelDataWrapper(this, textureWidth, textureHeight);

        window = new ModelMapper(modelDataWrapper);
        window.setPos(0, 24, 0);
        window.texOffs(0, 54).addBox(-20, 0, -24, 20, 1, 48, 0, false);
        window.texOffs(122, 122).addBox(-20, -14, -26, 2, 14, 52, 0, false);

        upper_wall_r1 = new ModelMapper(modelDataWrapper);
        upper_wall_r1.setPos(-20, -14, 0);
        window.addChild(upper_wall_r1);
        setRotationAngle(upper_wall_r1, 0, 0, 0.1107F);
        upper_wall_r1.texOffs(84, 51).addBox(0, -19, -26, 2, 19, 52, 0, false);

        window_exterior_2 = new ModelMapper(modelDataWrapper);
        window_exterior_2.setPos(0, 24, 0);
        window_exterior_2.texOffs(140, 25).addBox(20, 0, -24, 1, 7, 48, 0, false);
        window_exterior_2.texOffs(70, 88).addBox(20, -14, -26, 0, 14, 52, 0, true);

        upper_wall_r2 = new ModelMapper(modelDataWrapper);
        upper_wall_r2.setPos(20, -14, -8);
        window_exterior_2.addChild(upper_wall_r2);
        setRotationAngle(upper_wall_r2, 0, 0, -0.1107F);
        upper_wall_r2.texOffs(70, 70).addBox(0, -18, -18, 0, 18, 52, 0, true);

        window_exterior_1 = new ModelMapper(modelDataWrapper);
        window_exterior_1.setPos(0, 24, 0);
        window_exterior_1.texOffs(140, 25).addBox(-21, 0, -24, 1, 7, 48, 0, true);
        window_exterior_1.texOffs(70, 88).addBox(-19.999F, -14, -26, 0, 14, 52, 0, false);

        upper_wall_r3 = new ModelMapper(modelDataWrapper);
        upper_wall_r3.setPos(-34.4103F, -15.6011F, -7.8F);
        window_exterior_1.addChild(upper_wall_r3);
        setRotationAngle(upper_wall_r3, 0, 0, 0.1107F);
        upper_wall_r3.texOffs(70, 70).addBox(14.5F, -18, -18.2F, 0, 18, 52, 0, false);

        door_exterior_2 = new ModelMapper(modelDataWrapper);
        door_exterior_2.setPos(0, 24, 0);
        door_exterior_2.texOffs(50, 201).addBox(20, 0, -16, 1, 7, 32, 0, false);

        door_right_exterior_2 = new ModelMapper(modelDataWrapper);
        door_right_exterior_2.setPos(0, 0, 0);
        door_exterior_2.addChild(door_right_exterior_2);
        door_right_exterior_2.texOffs(32, 263).addBox(19.8F, -14, 0, 1, 14, 16, 0, false);

        door_left_top_r1 = new ModelMapper(modelDataWrapper);
        door_left_top_r1.setPos(20.8F, -14, 0);
        door_right_exterior_2.addChild(door_left_top_r1);
        setRotationAngle(door_left_top_r1, 0, 0, -0.1107F);
        door_left_top_r1.texOffs(237, 249).addBox(-1, -19, 0, 1, 19, 16, 0, false);

        door_left_exterior_2 = new ModelMapper(modelDataWrapper);
        door_left_exterior_2.setPos(0, 0, 0);
        door_exterior_2.addChild(door_left_exterior_2);
        door_left_exterior_2.texOffs(106, 254).addBox(19.8F, -14, -16, 1, 14, 16, 0, false);

        door_right_top_r1 = new ModelMapper(modelDataWrapper);
        door_right_top_r1.setPos(20.8F, -14, 0);
        door_left_exterior_2.addChild(door_right_top_r1);
        setRotationAngle(door_right_top_r1, 0, 0, -0.1107F);
        door_right_top_r1.texOffs(178, 134).addBox(-1, -19, -16, 1, 19, 16, 0, false);

        door_exterior_1 = new ModelMapper(modelDataWrapper);
        door_exterior_1.setPos(0, 24, 0);
        door_exterior_1.texOffs(50, 201).addBox(-21, 0, -16, 1, 7, 32, 0, true);

        door_left_exterior_1 = new ModelMapper(modelDataWrapper);
        door_left_exterior_1.setPos(0, 0, 0);
        door_exterior_1.addChild(door_left_exterior_1);
        door_left_exterior_1.texOffs(32, 263).addBox(-20.8F, -14, 0, 1, 14, 16, 0, true);

        door_left_top_r2 = new ModelMapper(modelDataWrapper);
        door_left_top_r2.setPos(-20.8F, -14, 0);
        door_left_exterior_1.addChild(door_left_top_r2);
        setRotationAngle(door_left_top_r2, 0, 0, 0.1107F);
        door_left_top_r2.texOffs(237, 249).addBox(0, -19, 0, 1, 19, 16, 0, true);

        door_right_exterior_1 = new ModelMapper(modelDataWrapper);
        door_right_exterior_1.setPos(0, 0, 0);
        door_exterior_1.addChild(door_right_exterior_1);
        door_right_exterior_1.texOffs(106, 254).addBox(-20.8F, -14, -16, 1, 14, 16, 0, true);

        door_right_top_r2 = new ModelMapper(modelDataWrapper);
        door_right_top_r2.setPos(-20.8F, -14, 0);
        door_right_exterior_1.addChild(door_right_top_r2);
        setRotationAngle(door_right_top_r2, 0, 0, 0.1107F);
        door_right_top_r2.texOffs(178, 134).addBox(0, -19, -16, 1, 19, 16, 0, true);

        door = new ModelMapper(modelDataWrapper);
        door.setPos(0, 24, 0);
        door.texOffs(178, 101).addBox(-20, 0, -16, 20, 1, 32, 0, false);
        door.texOffs(261, 62).addBox(-4, -37.25F, -4, 4, 1, 8, 0, false);

        door_left = new ModelMapper(modelDataWrapper);
        door_left.setPos(0, 0, 0);
        door.addChild(door_left);
        door_left.texOffs(116, 217).addBox(-19.8F, -14, 0, 0, 14, 16, 0, false);

        door_left_top_r3 = new ModelMapper(modelDataWrapper);
        door_left_top_r3.setPos(-20.8F, -14, 0);
        door_left.addChild(door_left_top_r3);
        setRotationAngle(door_left_top_r3, 0, 0, 0.1107F);
        door_left_top_r3.texOffs(42, 185).addBox(1, -19, 0, 0, 19, 16, 0, false);

        door_right = new ModelMapper(modelDataWrapper);
        door_right.setPos(0, 0, 0);
        door.addChild(door_right);
        door_right.texOffs(184, 216).addBox(-19.8F, -14, -16, 0, 14, 16, 0, false);

        door_right_top_r3 = new ModelMapper(modelDataWrapper);
        door_right_top_r3.setPos(-20.8F, -14, 0);
        door_right.addChild(door_right_top_r3);
        setRotationAngle(door_right_top_r3, 0, 0, 0.1107F);
        door_right_top_r3.texOffs(116, 9).addBox(1, -19, -16, 0, 19, 16, 0, false);

        end = new ModelMapper(modelDataWrapper);
        end.setPos(0, 24, 0);
        end.texOffs(140, 80).addBox(-20, 0, -12, 40, 1, 20, 0, false);
        end.texOffs(128, 232).addBox(18, -14, -12, 2, 14, 22, 0, false);
        end.texOffs(128, 232).addBox(-20, -14, -12, 2, 14, 22, 0, true);
        end.texOffs(176, 254).addBox(10, -33, -12, 9, 33, 5, 0, false);
        end.texOffs(176, 254).addBox(-19, -33, -12, 9, 33, 5, 0, true);
        end.texOffs(230, 170).addBox(-13, -38, -12, 26, 5, 5, 0, false);

        upper_wall_2_r1 = new ModelMapper(modelDataWrapper);
        upper_wall_2_r1.setPos(-20, -14, 24);
        end.addChild(upper_wall_2_r1);
        setRotationAngle(upper_wall_2_r1, 0, 0, 0.1107F);
        upper_wall_2_r1.texOffs(0, 103).addBox(0, -19, -36, 2, 19, 22, 0, true);

        upper_wall_1_r1 = new ModelMapper(modelDataWrapper);
        upper_wall_1_r1.setPos(20, -14, 24);
        end.addChild(upper_wall_1_r1);
        setRotationAngle(upper_wall_1_r1, 0, 0, -0.1107F);
        upper_wall_1_r1.texOffs(0, 103).addBox(-2, -19, -36, 2, 19, 22, 0, false);

        seat_end_1 = new ModelMapper(modelDataWrapper);
        seat_end_1.setPos(0, 0, 24);
        end.addChild(seat_end_1);
        seat_end_1.texOffs(154, 232).addBox(11, -6, -31, 7, 1, 16, 0, false);

        seat_back_1_r1 = new ModelMapper(modelDataWrapper);
        seat_back_1_r1.setPos(18, -6.5F, 0);
        seat_end_1.addChild(seat_back_1_r1);
        setRotationAngle(seat_back_1_r1, 0, 0, 0.0873F);
        seat_back_1_r1.texOffs(26, 231).addBox(-1, -6, -31, 1, 4, 16, 0, false);

        seat_end_2 = new ModelMapper(modelDataWrapper);
        seat_end_2.setPos(0, 0, 24);
        end.addChild(seat_end_2);


        seat_back_2_r1 = new ModelMapper(modelDataWrapper);
        seat_back_2_r1.setPos(-18, -6.5F, 0);
        seat_end_2.addChild(seat_back_2_r1);
        setRotationAngle(seat_back_2_r1, 0, 3.1416F, -0.1047F);
        seat_back_2_r1.texOffs(26, 231).addBox(-1, -6, 15, 1, 4, 16, 0, false);

        seat_bottom_2_r1 = new ModelMapper(modelDataWrapper);
        seat_bottom_2_r1.setPos(0, 0, 0);
        seat_end_2.addChild(seat_bottom_2_r1);
        setRotationAngle(seat_bottom_2_r1, 0, 3.1416F, 0);
        seat_bottom_2_r1.texOffs(154, 232).addBox(11, -6, 15, 7, 1, 16, 0, false);

        end_exterior = new ModelMapper(modelDataWrapper);
        end_exterior.setPos(0, 24, 0);
        end_exterior.texOffs(80, 243).addBox(20, 0, -12, 1, 7, 20, 0, true);
        end_exterior.texOffs(80, 243).addBox(-21, 0, -12, 1, 7, 20, 0, false);
        end_exterior.texOffs(0, 231).addBox(18, -14, -12, 2, 14, 22, 0, false);
        end_exterior.texOffs(0, 231).addBox(-20, -14, -12, 2, 14, 22, 0, true);
        end_exterior.texOffs(66, 270).addBox(10, -33, -12, 8, 33, 0, 0, false);
        end_exterior.texOffs(66, 270).addBox(-18, -33, -12, 8, 33, 0, 0, false);
        end_exterior.texOffs(101, 157).addBox(-18, -45, -12, 36, 12, 0, 0, false);

        upper_wall_2_r2 = new ModelMapper(modelDataWrapper);
        upper_wall_2_r2.setPos(-20, -14, 24);
        end_exterior.addChild(upper_wall_2_r2);
        setRotationAngle(upper_wall_2_r2, 0, 0, 0.1107F);
        upper_wall_2_r2.texOffs(140, 25).addBox(0, -18, -36, 2, 18, 22, 0, true);

        upper_wall_1_r2 = new ModelMapper(modelDataWrapper);
        upper_wall_1_r2.setPos(20, -14, 24);
        end_exterior.addChild(upper_wall_1_r2);
        setRotationAngle(upper_wall_1_r2, 0, 0, -0.1107F);
        upper_wall_1_r2.texOffs(140, 25).addBox(-2, -18, -36, 2, 18, 22, 0, false);

        seat = new ModelMapper(modelDataWrapper);
        seat.setPos(0, 24, 0);
        seat.texOffs(115, 279).addBox(-18, -6, -20, 7, 1, 40, 0, false);

        seat_back_2_r2 = new ModelMapper(modelDataWrapper);
        seat_back_2_r2.setPos(-18, -6.5F, 0.5F);
        seat.addChild(seat_back_2_r2);
        setRotationAngle(seat_back_2_r2, 0, 0, -0.0873F);
        seat_back_2_r2.texOffs(205, 15).addBox(0, -6, -0.5F, 1, 4, 20, 0, false);
        seat_back_2_r2.texOffs(205, 15).addBox(0, -6, -20.5F, 1, 4, 20, 0, false);

        door_light = new ModelMapper(modelDataWrapper);
        door_light.setPos(0, 24, 0);
        door_light.texOffs(21, 27).addBox(-3, -37.25F, -3, 3, 1, 0, 0, false);
        door_light.texOffs(0, 1).addBox(-3, -37.25F, -3, 0, 1, 6, 0, false);

        light_3_r1 = new ModelMapper(modelDataWrapper);
        light_3_r1.setPos(-1.5F, -36.75F, 3);
        door_light.addChild(light_3_r1);
        setRotationAngle(light_3_r1, 0, 3.1416F, 0);
        light_3_r1.texOffs(21, 27).addBox(-1.5F, -0.5F, 0, 3, 1, 0, 0, true);

        side_panel = new ModelMapper(modelDataWrapper);
        side_panel.setPos(0, 24, 0);
        side_panel.texOffs(227, 0).addBox(-18, -29, 0, 7, 24, 0, 0, false);

        side_panel_translucent = new ModelMapper(modelDataWrapper);
        side_panel_translucent.setPos(0, 24, 0);
        side_panel_translucent.texOffs(227, 135).addBox(-18, -28, 0, 6, 22, 0, 0, false);

        roof_window = new ModelMapper(modelDataWrapper);
        roof_window.setPos(0, 24, 0);
        roof_window.texOffs(54, 0).addBox(-16.0123F, -32.1399F, -26, 3, 0, 52, 0, false);
        roof_window.texOffs(60, 0).addBox(-10.1444F, -36.2357F, -26, 2, 0, 52, 0, false);
        roof_window.texOffs(32, 0).addBox(-6, -37.25F, -26, 6, 0, 52, 0, false);

        inner_roof_2_r1 = new ModelMapper(modelDataWrapper);
        inner_roof_2_r1.setPos(-13.0123F, -32.1409F, 26);
        roof_window.addChild(inner_roof_2_r1);
        setRotationAngle(inner_roof_2_r1, 0, 0, -0.9599F);
        inner_roof_2_r1.texOffs(44, 0).addBox(0, 0.001F, -52, 5, 0, 52, 0, false);

        roof_light = new ModelMapper(modelDataWrapper);
        roof_light.setPos(0, 24, 0);


        roof_light_r1 = new ModelMapper(modelDataWrapper);
        roof_light_r1.setPos(-8.1444F, -36.2367F, 26);
        roof_light.addChild(roof_light_r1);
        setRotationAngle(roof_light_r1, 0, 0, -0.4102F);
        roof_light_r1.texOffs(40, 54).addBox(0, 0.001F, -50, 3, 0, 48, 0, false);

        roof_door = new ModelMapper(modelDataWrapper);
        roof_door.setPos(0, 24, 0);
        roof_door.texOffs(88, 54).addBox(-18.0123F, -32.1399F, -14, 5, 0, 28, 0, false);
        roof_door.texOffs(98, 54).addBox(-10.1444F, -36.2357F, -14, 2, 0, 28, 0, false);
        roof_door.texOffs(0, 0).addBox(-6, -37.25F, -14, 6, 0, 28, 0, false);

        inner_roof_3_r1 = new ModelMapper(modelDataWrapper);
        inner_roof_3_r1.setPos(-13.0123F, -32.1409F, 26);
        roof_door.addChild(inner_roof_3_r1);
        setRotationAngle(inner_roof_3_r1, 0, 0, -0.9599F);
        inner_roof_3_r1.texOffs(0, 54).addBox(0, 0.001F, -40, 5, 0, 28, 0, false);

        roof_end = new ModelMapper(modelDataWrapper);
        roof_end.setPos(0, 24, 0);


        side_1 = new ModelMapper(modelDataWrapper);
        side_1.setPos(0, 0, 0);
        roof_end.addChild(side_1);
        side_1.texOffs(0, 17).addBox(-10.1444F, -36.2357F, -7, 2, 0, 17, 0, false);
        side_1.texOffs(13, 28).addBox(-16.0123F, -32.1399F, -7, 3, 0, 17, 0, false);
        side_1.texOffs(99, 82).addBox(-6, -37.25F, -7, 6, 0, 17, 0, false);

        inner_roof_4_r1 = new ModelMapper(modelDataWrapper);
        inner_roof_4_r1.setPos(-13.0123F, -32.1409F, 26);
        side_1.addChild(inner_roof_4_r1);
        setRotationAngle(inner_roof_4_r1, 0, 0, -0.9599F);
        inner_roof_4_r1.texOffs(0, 0).addBox(0, 0.001F, -33, 5, 0, 17, 0, false);

        side_2 = new ModelMapper(modelDataWrapper);
        side_2.setPos(0, 0, 0);
        roof_end.addChild(side_2);
        side_2.texOffs(0, 17).addBox(8.1444F, -36.2357F, -7, 2, 0, 17, 0, true);
        side_2.texOffs(13, 28).addBox(13.0123F, -32.1399F, -7, 3, 0, 17, 0, true);
        side_2.texOffs(99, 82).addBox(0, -37.25F, -7, 6, 0, 17, 0, true);

        inner_roof_5_r1 = new ModelMapper(modelDataWrapper);
        inner_roof_5_r1.setPos(10.1444F, -36.2367F, 26);
        side_2.addChild(inner_roof_5_r1);
        setRotationAngle(inner_roof_5_r1, 0, 0, 0.9599F);
        inner_roof_5_r1.texOffs(0, 0).addBox(0, 0.001F, -33, 5, 0, 17, 0, false);

        roof_exterior = new ModelMapper(modelDataWrapper);
        roof_exterior.setPos(0, 24, 0);
        roof_exterior.texOffs(0, 0).addBox(-5.9859F, -44.6423F, -20, 6, 0, 40, 0, false);

        outer_roof_4_r1 = new ModelMapper(modelDataWrapper);
        outer_roof_4_r1.setPos(-8.9401F, -44.1214F, -4);
        roof_exterior.addChild(outer_roof_4_r1);
        setRotationAngle(outer_roof_4_r1, 0, 0, -0.1745F);
        outer_roof_4_r1.texOffs(54, 54).addBox(-4, 0, -16, 7, 0, 40, 0, true);

        outer_roof_3_r1 = new ModelMapper(modelDataWrapper);
        outer_roof_3_r1.setPos(-14.178F, -42.6769F, -4);
        roof_exterior.addChild(outer_roof_3_r1);
        setRotationAngle(outer_roof_3_r1, 0, 0, -0.5236F);
        outer_roof_3_r1.texOffs(0, 54).addBox(-2.5F, 0, -16, 4, 0, 40, 0, true);

        outer_roof_2_r1 = new ModelMapper(modelDataWrapper);
        outer_roof_2_r1.setPos(-17.3427F, -39.6952F, -4);
        roof_exterior.addChild(outer_roof_2_r1);
        setRotationAngle(outer_roof_2_r1, 0, 0, -1.0472F);
        outer_roof_2_r1.texOffs(68, 54).addBox(-2, 0, -16, 4, 0, 40, 0, false);

        outer_roof_1_r1 = new ModelMapper(modelDataWrapper);
        outer_roof_1_r1.setPos(-20, -14, -4);
        roof_exterior.addChild(outer_roof_1_r1);
        setRotationAngle(outer_roof_1_r1, 0, 0, 0.1107F);
        outer_roof_1_r1.texOffs(0, 185).addBox(-1, -24, -16, 1, 6, 40, 0, false);

        roof_end_exterior = new ModelMapper(modelDataWrapper);
        roof_end_exterior.setPos(0, 24, 0);


        side_3 = new ModelMapper(modelDataWrapper);
        side_3.setPos(0, 0, 0);
        roof_end_exterior.addChild(side_3);
        side_3.texOffs(231, 39).addBox(-5.9859F, -44.6423F, -12, 6, 1, 22, 0, false);

        outer_roof_3_r2 = new ModelMapper(modelDataWrapper);
        outer_roof_3_r2.setPos(-17.3427F, -39.6952F, -4);
        side_3.addChild(outer_roof_3_r2);
        setRotationAngle(outer_roof_3_r2, 0, 0, -1.0472F);
        outer_roof_3_r2.texOffs(48, 240).addBox(-2, 0, -8, 4, 1, 22, 0, false);

        outer_roof_5_r1 = new ModelMapper(modelDataWrapper);
        outer_roof_5_r1.setPos(-8.9401F, -44.1214F, -4);
        side_3.addChild(outer_roof_5_r1);
        setRotationAngle(outer_roof_5_r1, 0, 0, -0.1745F);
        outer_roof_5_r1.texOffs(226, 226).addBox(-4, 0, -8, 7, 1, 22, 0, true);

        outer_roof_4_r2 = new ModelMapper(modelDataWrapper);
        outer_roof_4_r2.setPos(-14.178F, -42.6769F, -4);
        side_3.addChild(outer_roof_4_r2);
        setRotationAngle(outer_roof_4_r2, 0, 0, -0.5236F);
        outer_roof_4_r2.texOffs(0, 28).addBox(-2.5F, 0, -8, 4, 1, 22, 0, true);

        outer_roof_2_r2 = new ModelMapper(modelDataWrapper);
        outer_roof_2_r2.setPos(-20, -14, -4);
        side_3.addChild(outer_roof_2_r2);
        setRotationAngle(outer_roof_2_r2, 0, 0, 0.1107F);
        outer_roof_2_r2.texOffs(0, 72).addBox(-1, -24, -8, 1, 6, 22, 0, false);

        side_4 = new ModelMapper(modelDataWrapper);
        side_4.setPos(0, 0, 0);
        roof_end_exterior.addChild(side_4);
        side_4.texOffs(231, 39).addBox(-0.0141F, -44.6423F, -12, 6, 1, 22, 0, true);

        outer_roof_7_r1 = new ModelMapper(modelDataWrapper);
        outer_roof_7_r1.setPos(20.9939F, -14.1104F, -4);
        side_4.addChild(outer_roof_7_r1);
        setRotationAngle(outer_roof_7_r1, 0, 0, -0.1107F);
        outer_roof_7_r1.texOffs(0, 72).addBox(-1, -24, -8, 1, 6, 22, 0, true);

        outer_roof_9_r1 = new ModelMapper(modelDataWrapper);
        outer_roof_9_r1.setPos(15.0444F, -42.1768F, -4);
        side_4.addChild(outer_roof_9_r1);
        setRotationAngle(outer_roof_9_r1, 0, 0, 0.5236F);
        outer_roof_9_r1.texOffs(0, 28).addBox(-2.5F, 0, -8, 4, 1, 22, 0, false);

        outer_roof_10_r1 = new ModelMapper(modelDataWrapper);
        outer_roof_10_r1.setPos(9.9249F, -43.9477F, -4);
        side_4.addChild(outer_roof_10_r1);
        setRotationAngle(outer_roof_10_r1, 0, 0, 0.1745F);
        outer_roof_10_r1.texOffs(226, 226).addBox(-4, 0, -8, 7, 1, 22, 0, false);

        outer_roof_8_r1 = new ModelMapper(modelDataWrapper);
        outer_roof_8_r1.setPos(17.3427F, -39.6952F, -4);
        side_4.addChild(outer_roof_8_r1);
        setRotationAngle(outer_roof_8_r1, 0, 0, 1.0472F);
        outer_roof_8_r1.texOffs(48, 240).addBox(-2, 0, -8, 4, 1, 22, 0, true);

        top_handrail = new ModelMapper(modelDataWrapper);
        top_handrail.setPos(0, 24, 0);
        top_handrail.texOffs(313, 0).addBox(-3, -33, 33.166F, 3, 0, 0, 0.2F, false);
        top_handrail.texOffs(313, 0).addBox(-3, -33, -33.166F, 3, 0, 0, 0.2F, false);
        top_handrail.texOffs(319, 0).addBox(-5.3453F, -37.249F, -22, 0, 2, 0, 0.2F, false);
        top_handrail.texOffs(319, 0).addBox(-5.3453F, -37.249F, 0, 0, 2, 0, 0.2F, false);
        top_handrail.texOffs(319, 0).addBox(-5.3453F, -37.249F, 22, 0, 2, 0, 0.2F, false);
        top_handrail.texOffs(319, 0).addBox(0, -38, 33.166F, 0, 5, 0, 0.2F, false);
        top_handrail.texOffs(319, 0).addBox(0, -38, 11, 0, 9, 0, 0.2F, false);
        top_handrail.texOffs(316, 13).addBox(0, -23.2645F, 10.437F, 0, 6, 0, 0.2F, false);
        top_handrail.texOffs(316, 13).addBox(0, -23.2645F, 11.563F, 0, 6, 0, 0.2F, false);
        top_handrail.texOffs(319, 0).addBox(0, -12, 11, 0, 12, 0, 0.2F, false);

        pole_bottom_diagonal_2_r1 = new ModelMapper(modelDataWrapper);
        pole_bottom_diagonal_2_r1.setPos(0, -14.4002F, 11.2819F);
        top_handrail.addChild(pole_bottom_diagonal_2_r1);
        setRotationAngle(pole_bottom_diagonal_2_r1, -0.1047F, 0, 0);
        pole_bottom_diagonal_2_r1.texOffs(316, 19).addBox(0, -2.5F, 0, 0, 5, 0, 0.2F, false);

        pole_bottom_diagonal_1_r1 = new ModelMapper(modelDataWrapper);
        pole_bottom_diagonal_1_r1.setPos(0, -14.4002F, 10.7181F);
        top_handrail.addChild(pole_bottom_diagonal_1_r1);
        setRotationAngle(pole_bottom_diagonal_1_r1, 0.1047F, 0, 0);
        pole_bottom_diagonal_1_r1.texOffs(316, 19).addBox(0, -2.5F, 0, 0, 5, 0, 0.2F, false);

        pole_top_diagonal_2_r1 = new ModelMapper(modelDataWrapper);
        pole_top_diagonal_2_r1.setPos(0.2F, -28.8F, 10.8F);
        top_handrail.addChild(pole_top_diagonal_2_r1);
        setRotationAngle(pole_top_diagonal_2_r1, 0.1047F, 0, 0);
        pole_top_diagonal_2_r1.texOffs(316, 3).addBox(-0.2F, 0.2069F, 0.2F, 0, 5, 0, 0.2F, false);

        pole_top_diagonal_1_r1 = new ModelMapper(modelDataWrapper);
        pole_top_diagonal_1_r1.setPos(0.2F, -28.8F, 11.2F);
        top_handrail.addChild(pole_top_diagonal_1_r1);
        setRotationAngle(pole_top_diagonal_1_r1, -0.1047F, 0, 0);
        pole_top_diagonal_1_r1.texOffs(316, 3).addBox(-0.2F, 0.2069F, -0.2F, 0, 5, 0, 0.2F, false);

        top_handrail_connector_bottom_3_r1 = new ModelMapper(modelDataWrapper);
        top_handrail_connector_bottom_3_r1.setPos(-6.4333F, -32.9847F, -17.7F);
        top_handrail.addChild(top_handrail_connector_bottom_3_r1);
        setRotationAngle(top_handrail_connector_bottom_3_r1, 0, 0, 0.3927F);
        top_handrail_connector_bottom_3_r1.texOffs(319, 0).addBox(0.2F, -2.2F, 39.7F, 0, 2, 0, 0.2F, false);
        top_handrail_connector_bottom_3_r1.texOffs(319, 0).addBox(0.2F, -2.2F, 17.7F, 0, 2, 0, 0.2F, false);
        top_handrail_connector_bottom_3_r1.texOffs(319, 0).addBox(0.2F, -2.2F, -4.3F, 0, 2, 0, 0.2F, false);

        top_handrail_bottom_right_r1 = new ModelMapper(modelDataWrapper);
        top_handrail_bottom_right_r1.setPos(-6.9124F, -31, 7.1125F);
        top_handrail.addChild(top_handrail_bottom_right_r1);
        setRotationAngle(top_handrail_bottom_right_r1, 1.5708F, 0, 0);
        top_handrail_bottom_right_r1.texOffs(319, 0).addBox(0.6339F, -37, 2, 0, 30, 0, 0.2F, false);

        top_handrail_bottom_left_r1 = new ModelMapper(modelDataWrapper);
        top_handrail_bottom_left_r1.setPos(-6.9124F, -31, 6.8876F);
        top_handrail.addChild(top_handrail_bottom_left_r1);
        setRotationAngle(top_handrail_bottom_left_r1, -1.5708F, 0, 0);
        top_handrail_bottom_left_r1.texOffs(319, 0).addBox(0.6339F, -23, -2, 0, 30, 0, 0.2F, false);

        top_handrail_right_4_r1 = new ModelMapper(modelDataWrapper);
        top_handrail_right_4_r1.setPos(-5.9553F, -31, -30.5938F);
        top_handrail.addChild(top_handrail_right_4_r1);
        setRotationAngle(top_handrail_right_4_r1, 1.5708F, -0.5236F, 0);
        top_handrail_right_4_r1.texOffs(319, 0).addBox(0, -1.5F, 2, 0, 2, 0, 0.2F, false);

        top_handrail_right_3_r1 = new ModelMapper(modelDataWrapper);
        top_handrail_right_3_r1.setPos(-4.5723F, -31, -32.3428F);
        top_handrail.addChild(top_handrail_right_3_r1);
        setRotationAngle(top_handrail_right_3_r1, 1.5708F, -1.0472F, 0);
        top_handrail_right_3_r1.texOffs(319, 0).addBox(0, -1.5F, 2, 0, 2, 0, 0.2F, false);

        top_handrail_left_4_r1 = new ModelMapper(modelDataWrapper);
        top_handrail_left_4_r1.setPos(-5.9553F, -31, 30.5938F);
        top_handrail.addChild(top_handrail_left_4_r1);
        setRotationAngle(top_handrail_left_4_r1, -1.5708F, 0.5236F, 0);
        top_handrail_left_4_r1.texOffs(319, 0).addBox(0, -1.5F, -2, 0, 2, 0, 0.2F, false);

        top_handrail_left_3_r1 = new ModelMapper(modelDataWrapper);
        top_handrail_left_3_r1.setPos(-4.5723F, -31, 32.3428F);
        top_handrail.addChild(top_handrail_left_3_r1);
        setRotationAngle(top_handrail_left_3_r1, -1.5708F, 1.0472F, 0);
        top_handrail_left_3_r1.texOffs(319, 0).addBox(0, -1.5F, -2, 0, 2, 0, 0.2F, false);

        handrail_straps = new ModelMapper(modelDataWrapper);
        handrail_straps.setPos(0, 0, 0);
        top_handrail.addChild(handrail_straps);
        handrail_straps.texOffs(0, 32).addBox(-7.25F, -34, -20, 2, 4, 0, 0, false);
        handrail_straps.texOffs(0, 32).addBox(-7.25F, -34, -11, 2, 4, 0, 0, false);
        handrail_straps.texOffs(0, 32).addBox(-7.25F, -34, -2, 2, 4, 0, 0, false);
        handrail_straps.texOffs(0, 32).addBox(-7.25F, -34, 2, 2, 4, 0, 0, false);
        handrail_straps.texOffs(0, 32).addBox(-7.25F, -34, 11, 2, 4, 0, 0, false);
        handrail_straps.texOffs(0, 32).addBox(-7.25F, -34, 20, 2, 4, 0, 0, false);

        handrail_strap_8_r1 = new ModelMapper(modelDataWrapper);
        handrail_strap_8_r1.setPos(0, 0, 0);
        handrail_straps.addChild(handrail_strap_8_r1);
        setRotationAngle(handrail_strap_8_r1, 0, -1.5708F, 0);
        handrail_strap_8_r1.texOffs(0, 32).addBox(-34.166F, -34, 3, 2, 4, 0, 0, false);
        handrail_strap_8_r1.texOffs(0, 32).addBox(32.166F, -34, 3, 2, 4, 0, 0, false);

        head = new ModelMapper(modelDataWrapper);
        head.setPos(0, 24, 0);
        head.texOffs(42, 188).addBox(-20, 0, -4, 40, 1, 12, 0, false);
        head.texOffs(265, 28).addBox(18, -14, -4, 2, 14, 14, 0, false);
        head.texOffs(265, 28).addBox(-20, -14, -4, 2, 14, 14, 0, true);
        head.texOffs(218, 188).addBox(-18, -38, -4, 36, 38, 0, 0, false);

        upper_wall_2_r3 = new ModelMapper(modelDataWrapper);
        upper_wall_2_r3.setPos(-20, -14, 0);
        head.addChild(upper_wall_2_r3);
        setRotationAngle(upper_wall_2_r3, 0, 0, 0.1107F);
        upper_wall_2_r3.texOffs(250, 98).addBox(0, -19, -4, 2, 19, 14, 0, true);

        upper_wall_1_r3 = new ModelMapper(modelDataWrapper);
        upper_wall_1_r3.setPos(20, -14, 0);
        head.addChild(upper_wall_1_r3);
        setRotationAngle(upper_wall_1_r3, 0, 0, -0.1107F);
        upper_wall_1_r3.texOffs(250, 98).addBox(-2, -19, -4, 2, 19, 14, 0, false);

        head_exterior = new ModelMapper(modelDataWrapper);
        head_exterior.setPos(0, 24, 0);
        head_exterior.texOffs(178, 107).addBox(20, 0, -7, 1, 7, 15, 0, false);
        head_exterior.texOffs(178, 107).addBox(-21, 0, -7, 1, 7, 15, 0, true);
        head_exterior.texOffs(138, 34).addBox(-21, 0, -17, 1, 1, 10, 0, false);
        head_exterior.texOffs(138, 34).addBox(20, 0, -17, 1, 1, 10, 0, true);
        head_exterior.texOffs(0, 157).addBox(-20, 0, -25, 40, 7, 21, 0, false);
        head_exterior.texOffs(240, 67).addBox(18, -14, -7, 2, 14, 17, 0, false);
        head_exterior.texOffs(240, 67).addBox(-20, -14, -7, 2, 14, 17, 0, true);
        head_exterior.texOffs(146, 188).addBox(-18, -44, -4, 36, 44, 0, 0, false);

        upper_wall_2_r4 = new ModelMapper(modelDataWrapper);
        upper_wall_2_r4.setPos(-20, -14, 24);
        head_exterior.addChild(upper_wall_2_r4);
        setRotationAngle(upper_wall_2_r4, 0, 0, 0.1107F);
        upper_wall_2_r4.texOffs(0, 185).addBox(0, -18, -31, 2, 18, 17, 0, true);

        upper_wall_1_r4 = new ModelMapper(modelDataWrapper);
        upper_wall_1_r4.setPos(20, -14, 24);
        head_exterior.addChild(upper_wall_1_r4);
        setRotationAngle(upper_wall_1_r4, 0, 0, -0.1107F);
        upper_wall_1_r4.texOffs(0, 185).addBox(-2, -18, -31, 2, 18, 17, 0, false);

        bottom_r1 = new ModelMapper(modelDataWrapper);
        bottom_r1.setPos(-20, 7.1305F, -16.0086F);
        head_exterior.addChild(bottom_r1);
        setRotationAngle(bottom_r1, -0.0873F, 0, 0);
        bottom_r1.texOffs(91, 0).addBox(0, 0, -26, 40, 0, 25, 0, false);

        front_side_1 = new ModelMapper(modelDataWrapper);
        front_side_1.setPos(0, 0, 0);
        head_exterior.addChild(front_side_1);
        front_side_1.texOffs(0, 70).addBox(20, -14, -25, 0, 14, 8, 0, true);

        front_side_lower_3_r1 = new ModelMapper(modelDataWrapper);
        front_side_lower_3_r1.setPos(20, 0, -25);
        front_side_1.addChild(front_side_lower_3_r1);
        setRotationAngle(front_side_lower_3_r1, 0, 0.2182F, 0);
        front_side_lower_3_r1.texOffs(0, 94).addBox(0, -14, -9, 0, 21, 9, 0, true);

        front_side_upper_3_r1 = new ModelMapper(modelDataWrapper);
        front_side_upper_3_r1.setPos(20, -14, -25);
        front_side_1.addChild(front_side_upper_3_r1);
        setRotationAngle(front_side_upper_3_r1, 0, 0.2182F, -0.1107F);
        front_side_upper_3_r1.texOffs(0, 45).addBox(0, -24, -9, 0, 24, 9, 0, true);

        front_side_upper_1_r1 = new ModelMapper(modelDataWrapper);
        front_side_upper_1_r1.setPos(19.8274F, -13.9808F, -16.0152F);
        front_side_1.addChild(front_side_upper_1_r1);
        setRotationAngle(front_side_upper_1_r1, 0, 0, -0.1107F);
        front_side_upper_1_r1.texOffs(0, 6).addBox(0.1736F, -18, -8.9848F, 0, 18, 8, 0, true);

        front_side_2 = new ModelMapper(modelDataWrapper);
        front_side_2.setPos(0, 0, 0);
        head_exterior.addChild(front_side_2);
        front_side_2.texOffs(0, 70).addBox(-19.999F, -14, -24.9988F, 0, 14, 8, 0, false);

        front_side_lower_4_r1 = new ModelMapper(modelDataWrapper);
        front_side_lower_4_r1.setPos(-20, 7, -24.9988F);
        front_side_2.addChild(front_side_lower_4_r1);
        setRotationAngle(front_side_lower_4_r1, 0, -0.2182F, 0);
        front_side_lower_4_r1.texOffs(0, 94).addBox(0.001F, -21, -9, 0, 21, 9, 0, false);

        front_side_upper_4_r1 = new ModelMapper(modelDataWrapper);
        front_side_upper_4_r1.setPos(-19.999F, -13.9999F, -24.9988F);
        front_side_2.addChild(front_side_upper_4_r1);
        setRotationAngle(front_side_upper_4_r1, 0, -0.2182F, 0.1107F);
        front_side_upper_4_r1.texOffs(0, 45).addBox(0, -24, -9, 0, 24, 9, 0, false);

        front_side_upper_2_r1 = new ModelMapper(modelDataWrapper);
        front_side_upper_2_r1.setPos(-19.999F, -13.9999F, -16.9988F);
        front_side_2.addChild(front_side_upper_2_r1);
        setRotationAngle(front_side_upper_2_r1, 0, 0, 0.1107F);
        front_side_upper_2_r1.texOffs(0, 6).addBox(0, -18, -8, 0, 18, 8, 0, false);

        front_panel = new ModelMapper(modelDataWrapper);
        front_panel.setPos(0, 0, 0);
        head_exterior.addChild(front_panel);


        panel_6_r1 = new ModelMapper(modelDataWrapper);
        panel_6_r1.setPos(-7.5573F, -13.881F, -33.7427F);
        front_panel.addChild(panel_6_r1);
        setRotationAngle(panel_6_r1, -0.1309F, 0, 0);
        panel_6_r1.texOffs(0, 144).addBox(0, 0, 0, 15, 11, 0, 0, false);

        panel_5_r1 = new ModelMapper(modelDataWrapper);
        panel_5_r1.setPos(-7.2465F, -13.881F, -33.7767F);
        front_panel.addChild(panel_5_r1);
        setRotationAngle(panel_5_r1, -0.1309F, 0.2182F, 0);
        panel_5_r1.texOffs(256, 134).addBox(-12, 0, 0, 12, 11, 0, 0, false);

        panel_4_r1 = new ModelMapper(modelDataWrapper);
        panel_4_r1.setPos(7.132F, -13.881F, -33.7767F);
        front_panel.addChild(panel_4_r1);
        setRotationAngle(panel_4_r1, -0.1309F, -0.2182F, 0);
        panel_4_r1.texOffs(256, 145).addBox(0, 0, 0, 12, 11, 0, 0, false);

        panel_3_r1 = new ModelMapper(modelDataWrapper);
        panel_3_r1.setPos(5.3394F, -44.7906F, -25.6908F);
        front_panel.addChild(panel_3_r1);
        setRotationAngle(panel_3_r1, -0.2618F, -0.2182F, 0);
        panel_3_r1.texOffs(196, 0).addBox(0, 1, 0, 12, 31, 0, 0, false);

        panel_2_r1 = new ModelMapper(modelDataWrapper);
        panel_2_r1.setPos(-5.4539F, -44.7906F, -25.6908F);
        front_panel.addChild(panel_2_r1);
        setRotationAngle(panel_2_r1, -0.2618F, 0.2182F, 0);
        panel_2_r1.texOffs(84, 201).addBox(-12, 1, 0, 12, 31, 0, 0, false);

        panel_1_r1 = new ModelMapper(modelDataWrapper);
        panel_1_r1.setPos(-7.5573F, -44.7906F, -25.4605F);
        front_panel.addChild(panel_1_r1);
        setRotationAngle(panel_1_r1, -0.2618F, 0, 0);
        panel_1_r1.texOffs(204, 254).addBox(0, 1, 0, 15, 31, 0, 0, false);

        nose = new ModelMapper(modelDataWrapper);
        nose.setPos(0, 0, 0);
        head_exterior.addChild(nose);


        nose_edge = new ModelMapper(modelDataWrapper);
        nose_edge.setPos(0, 0, 0);
        nose.addChild(nose_edge);


        edge_6_r1 = new ModelMapper(modelDataWrapper);
        edge_6_r1.setPos(7.7367F, 6, -40.4098F);
        nose_edge.addChild(edge_6_r1);
        setRotationAngle(edge_6_r1, 0, 0, 0);
        edge_6_r1.texOffs(0, 0).addBox(-8, -7, 0, 8, 7, 0, 0, true);

        edge_5_r1 = new ModelMapper(modelDataWrapper);
        edge_5_r1.setPos(-7.7358F, 6, -40.4081F);
        nose_edge.addChild(edge_5_r1);
        setRotationAngle(edge_5_r1, 0, 0, 0);
        edge_5_r1.texOffs(0, 0).addBox(0, -7, 0, 8, 7, 0, 0, false);

        edge_4_r1 = new ModelMapper(modelDataWrapper);
        edge_4_r1.setPos(-15.3656F, 13, -38.0024F);
        nose_edge.addChild(edge_4_r1);
        setRotationAngle(edge_4_r1, 0, 0.3054F, 0);
        edge_4_r1.texOffs(0, 43).addBox(0, -14, 0, 8, 7, 0, 0, true);

        edge_3_r1 = new ModelMapper(modelDataWrapper);
        edge_3_r1.setPos(15.3664F, -2, -38.0042F);
        nose_edge.addChild(edge_3_r1);
        setRotationAngle(edge_3_r1, 0, -0.3054F, 0);
        edge_3_r1.texOffs(0, 43).addBox(-8, 1, 0, 8, 7, 0, 0, false);

        edge_2_r1 = new ModelMapper(modelDataWrapper);
        edge_2_r1.setPos(18.052F, 17, -33.7867F);
        nose_edge.addChild(edge_2_r1);
        setRotationAngle(edge_2_r1, 0, 0.5672F, 0);
        edge_2_r1.texOffs(24, 77).addBox(0.001F, -20, -5, 0, 9, 5, 0, true);

        edge_1_r1 = new ModelMapper(modelDataWrapper);
        edge_1_r1.setPos(-18.0521F, 7, -33.7855F);
        nose_edge.addChild(edge_1_r1);
        setRotationAngle(edge_1_r1, 0, -0.5672F, 0);
        edge_1_r1.texOffs(24, 77).addBox(0.001F, -10, -5, 0, 9, 5, 0, false);

        nose_top = new ModelMapper(modelDataWrapper);
        nose_top.setPos(0, 0, 0);
        nose.addChild(nose_top);


        nose_top_3_r1 = new ModelMapper(modelDataWrapper);
        nose_top_3_r1.setPos(7.4428F, -2.9742F, -35.178F);
        nose_top.addChild(nose_top_3_r1);
        setRotationAngle(nose_top_3_r1, 0.48F, 0, 0);
        nose_top_3_r1.texOffs(134, 65).addBox(-16, 0, -6, 17, 0, 6, 0, false);

        nose_top_2_r1 = new ModelMapper(modelDataWrapper);
        nose_top_2_r1.setPos(7.4428F, -2.9742F, -35.178F);
        nose_top.addChild(nose_top_2_r1);
        setRotationAngle(nose_top_2_r1, 0.48F, -0.2182F, 0);
        nose_top_2_r1.texOffs(20, 103).addBox(-1, 0, -6, 13, 0, 6, 0, true);

        nose_top_1_r1 = new ModelMapper(modelDataWrapper);
        nose_top_1_r1.setPos(-20.2491F, -2.9751F, -32.3648F);
        nose_top.addChild(nose_top_1_r1);
        setRotationAngle(nose_top_1_r1, 0.48F, 0.2182F, 0);
        nose_top_1_r1.texOffs(20, 103).addBox(1, 0, -6, 13, 0, 6, 0, false);

        driver_door = new ModelMapper(modelDataWrapper);
        driver_door.setPos(0, 0, 0);
        head_exterior.addChild(driver_door);
        driver_door.texOffs(190, 39).addBox(18, -14, -17, 1, 14, 10, 0, false);
        driver_door.texOffs(190, 39).addBox(-19, -14, -17, 1, 14, 10, 0, true);
        driver_door.texOffs(38, 54).addBox(19, -14, -17, 1, 14, 0, 0, false);
        driver_door.texOffs(38, 54).addBox(-20, -14, -17, 1, 14, 0, 0, false);

        driver_door_edge_roof_2_r1 = new ModelMapper(modelDataWrapper);
        driver_door_edge_roof_2_r1.setPos(-19.0061F, -13.8896F, 1);
        driver_door.addChild(driver_door_edge_roof_2_r1);
        setRotationAngle(driver_door_edge_roof_2_r1, 0, 0, 0.1107F);
        driver_door_edge_roof_2_r1.texOffs(11, 17).addBox(-1, -18, -18, 1, 0, 10, 0, true);

        driver_door_edge_roof_1_r1 = new ModelMapper(modelDataWrapper);
        driver_door_edge_roof_1_r1.setPos(20, -14, 1);
        driver_door.addChild(driver_door_edge_roof_1_r1);
        setRotationAngle(driver_door_edge_roof_1_r1, 0, 0, -0.1107F);
        driver_door_edge_roof_1_r1.texOffs(11, 17).addBox(-1, -18, -18, 1, 0, 10, 0, false);
        driver_door_edge_roof_1_r1.texOffs(26, 54).addBox(-1, -18, -18, 1, 18, 0, 0, false);
        driver_door_edge_roof_1_r1.texOffs(123, 201).addBox(-2, -18, -18, 1, 18, 10, 0, false);

        driver_door_edge_upper_2_r1 = new ModelMapper(modelDataWrapper);
        driver_door_edge_upper_2_r1.setPos(28.7003F, -8.589F, 1);
        driver_door.addChild(driver_door_edge_upper_2_r1);
        setRotationAngle(driver_door_edge_upper_2_r1, 0, 0, 0.1107F);
        driver_door_edge_upper_2_r1.texOffs(26, 54).addBox(-49, -18, -18, 1, 18, 0, 0, false);

        driver_door_upper_2_r1 = new ModelMapper(modelDataWrapper);
        driver_door_upper_2_r1.setPos(-20, -14, 0);
        driver_door.addChild(driver_door_upper_2_r1);
        setRotationAngle(driver_door_upper_2_r1, 0, 0, 0.1107F);
        driver_door_upper_2_r1.texOffs(123, 201).addBox(1, -18, -17, 1, 18, 10, 0, true);

        roof_vent = new ModelMapper(modelDataWrapper);
        roof_vent.setPos(0, 24, 0);
        roof_vent.texOffs(0, 0).addBox(-8, -45.25F, -4, 16, 2, 52, 0, false);

        vent_1_r1 = new ModelMapper(modelDataWrapper);
        vent_1_r1.setPos(8, -45.25F, 0);
        roof_vent.addChild(vent_1_r1);
        setRotationAngle(vent_1_r1, 0, 0, 0.3491F);
        vent_1_r1.texOffs(0, 103).addBox(0, 0, -4, 9, 2, 52, 0, true);

        vent_2_r1 = new ModelMapper(modelDataWrapper);
        vent_2_r1.setPos(-8, -45.25F, 0);
        roof_vent.addChild(vent_2_r1);
        setRotationAngle(vent_2_r1, 0, 0, -0.3491F);
        vent_2_r1.texOffs(0, 103).addBox(-9, 0, -4, 9, 2, 52, 0, false);

        roof_head_exterior = new ModelMapper(modelDataWrapper);
        roof_head_exterior.setPos(0, 24, 0);


        side_7 = new ModelMapper(modelDataWrapper);
        side_7.setPos(0, 0, 0);
        roof_head_exterior.addChild(side_7);
        side_7.texOffs(84, 205).addBox(-5.9859F, -44.6423F, -17, 6, 1, 27, 0, false);

        outer_roof_12_r1 = new ModelMapper(modelDataWrapper);
        outer_roof_12_r1.setPos(-20.9859F, -44.6413F, -16.9999F);
        side_7.addChild(outer_roof_12_r1);
        setRotationAngle(outer_roof_12_r1, 0.1309F, 0, 0);
        outer_roof_12_r1.texOffs(0, 34).addBox(15, 0, -9, 6, 0, 9, 0, false);

        outer_roof_11_r1 = new ModelMapper(modelDataWrapper);
        outer_roof_11_r1.setPos(-17.8032F, -42.5575F, -16.9999F);
        side_7.addChild(outer_roof_11_r1);
        setRotationAngle(outer_roof_11_r1, 0.1309F, 0, -0.1745F);
        outer_roof_11_r1.texOffs(27, 40).addBox(5, 0, -9, 7, 0, 9, 0, false);

        outer_roof_10_r2 = new ModelMapper(modelDataWrapper);
        outer_roof_10_r2.setPos(-16.3426F, -41.426F, -17);
        side_7.addChild(outer_roof_10_r2);
        setRotationAngle(outer_roof_10_r2, 0.1309F, 0, -0.5236F);
        outer_roof_10_r2.texOffs(9, 54).addBox(0, 0, -9, 4, 0, 9, 0, false);

        outer_roof_9_r2 = new ModelMapper(modelDataWrapper);
        outer_roof_9_r2.setPos(-18.3436F, -37.9636F, -17);
        side_7.addChild(outer_roof_9_r2);
        setRotationAngle(outer_roof_9_r2, 0.1309F, 0, -1.0472F);
        outer_roof_9_r2.texOffs(9, 63).addBox(0, 0, -9, 4, 0, 9, 0, false);

        outer_roof_8_r2 = new ModelMapper(modelDataWrapper);
        outer_roof_8_r2.setPos(-18.3436F, -37.9636F, -17);
        side_7.addChild(outer_roof_8_r2);
        setRotationAngle(outer_roof_8_r2, 0, -0.0873F, 0.1107F);
        outer_roof_8_r2.texOffs(140, 80).addBox(0, 0, -8, 1, 6, 8, 0, false);

        outer_roof_4_r3 = new ModelMapper(modelDataWrapper);
        outer_roof_4_r3.setPos(-17.3427F, -39.6952F, -4);
        side_7.addChild(outer_roof_4_r3);
        setRotationAngle(outer_roof_4_r3, 0, 0, -1.0472F);
        outer_roof_4_r3.texOffs(191, 226).addBox(-2, 0, -13, 4, 1, 27, 0, false);

        outer_roof_6_r1 = new ModelMapper(modelDataWrapper);
        outer_roof_6_r1.setPos(-8.9401F, -44.1214F, -4);
        side_7.addChild(outer_roof_6_r1);
        setRotationAngle(outer_roof_6_r1, 0, 0, -0.1745F);
        outer_roof_6_r1.texOffs(190, 39).addBox(-4, 0, -13, 7, 1, 27, 0, true);

        outer_roof_5_r2 = new ModelMapper(modelDataWrapper);
        outer_roof_5_r2.setPos(-14.178F, -42.6769F, -4);
        side_7.addChild(outer_roof_5_r2);
        setRotationAngle(outer_roof_5_r2, 0, 0, -0.5236F);
        outer_roof_5_r2.texOffs(227, 0).addBox(-2.5F, 0, -13, 4, 1, 27, 0, true);

        outer_roof_3_r3 = new ModelMapper(modelDataWrapper);
        outer_roof_3_r3.setPos(-20, -14, -4);
        side_7.addChild(outer_roof_3_r3);
        setRotationAngle(outer_roof_3_r3, 0, 0, 0.1107F);
        outer_roof_3_r3.texOffs(227, 134).addBox(-1, -24, -13, 1, 6, 27, 0, false);

        side_8 = new ModelMapper(modelDataWrapper);
        side_8.setPos(0, 0, 0);
        roof_head_exterior.addChild(side_8);
        side_8.texOffs(84, 205).addBox(-0.0141F, -44.6423F, -17, 6, 1, 27, 0, true);

        outer_roof_17_r1 = new ModelMapper(modelDataWrapper);
        outer_roof_17_r1.setPos(1.9859F, -44.6413F, -16.9999F);
        side_8.addChild(outer_roof_17_r1);
        setRotationAngle(outer_roof_17_r1, 0.1309F, 0, 0);
        outer_roof_17_r1.texOffs(0, 34).addBox(-2, 0, -9, 6, 0, 9, 0, true);

        outer_roof_16_r1 = new ModelMapper(modelDataWrapper);
        outer_roof_16_r1.setPos(8.9399F, -44.1204F, -16.9999F);
        side_8.addChild(outer_roof_16_r1);
        setRotationAngle(outer_roof_16_r1, 0.1309F, 0, 0.1745F);
        outer_roof_16_r1.texOffs(27, 40).addBox(-3, 0, -9, 7, 0, 9, 0, true);

        outer_roof_15_r1 = new ModelMapper(modelDataWrapper);
        outer_roof_15_r1.setPos(12.8788F, -43.4259F, -16.9999F);
        side_8.addChild(outer_roof_15_r1);
        setRotationAngle(outer_roof_15_r1, 0.1309F, 0, 0.5236F);
        outer_roof_15_r1.texOffs(9, 54).addBox(0, 0, -9, 4, 0, 9, 0, true);

        outer_roof_14_r1 = new ModelMapper(modelDataWrapper);
        outer_roof_14_r1.setPos(16.3428F, -41.4273F, -16.9998F);
        side_8.addChild(outer_roof_14_r1);
        setRotationAngle(outer_roof_14_r1, 0.1309F, 0, 1.0472F);
        outer_roof_14_r1.texOffs(9, 63).addBox(0, 0, -9, 4, 0, 9, 0, true);

        outer_roof_13_r1 = new ModelMapper(modelDataWrapper);
        outer_roof_13_r1.setPos(18.3436F, -37.9636F, -17);
        side_8.addChild(outer_roof_13_r1);
        setRotationAngle(outer_roof_13_r1, 0, 0.0873F, -0.1107F);
        outer_roof_13_r1.texOffs(140, 80).addBox(-1, 0, -8, 1, 6, 8, 0, true);

        outer_roof_8_r3 = new ModelMapper(modelDataWrapper);
        outer_roof_8_r3.setPos(20.9939F, -14.1104F, -4);
        side_8.addChild(outer_roof_8_r3);
        setRotationAngle(outer_roof_8_r3, 0, 0, -0.1107F);
        outer_roof_8_r3.texOffs(227, 134).addBox(-1, -24, -13, 1, 6, 27, 0, true);

        outer_roof_10_r3 = new ModelMapper(modelDataWrapper);
        outer_roof_10_r3.setPos(15.0444F, -42.1768F, -4);
        side_8.addChild(outer_roof_10_r3);
        setRotationAngle(outer_roof_10_r3, 0, 0, 0.5236F);
        outer_roof_10_r3.texOffs(227, 0).addBox(-2.5F, 0, -13, 4, 1, 27, 0, false);

        outer_roof_11_r2 = new ModelMapper(modelDataWrapper);
        outer_roof_11_r2.setPos(9.9249F, -43.9477F, -4);
        side_8.addChild(outer_roof_11_r2);
        setRotationAngle(outer_roof_11_r2, 0, 0, 0.1745F);
        outer_roof_11_r2.texOffs(190, 39).addBox(-4, 0, -13, 7, 1, 27, 0, false);

        outer_roof_9_r3 = new ModelMapper(modelDataWrapper);
        outer_roof_9_r3.setPos(17.3427F, -39.6952F, -4);
        side_8.addChild(outer_roof_9_r3);
        setRotationAngle(outer_roof_9_r3, 0, 0, 1.0472F);
        outer_roof_9_r3.texOffs(191, 226).addBox(-2, 0, -13, 4, 1, 27, 0, true);

        headlights = new ModelMapper(modelDataWrapper);
        headlights.setPos(0, 24, 0);


        headlight_2_r1 = new ModelMapper(modelDataWrapper);
        headlight_2_r1.setPos(7.132F, -13.881F, -33.7767F);
        headlights.addChild(headlight_2_r1);
        setRotationAngle(headlight_2_r1, -0.1309F, -0.2182F, 0);
        headlight_2_r1.texOffs(255, 249).addBox(0, 0, -0.01F, 12, 11, 0, 0, true);

        headlight_1_r1 = new ModelMapper(modelDataWrapper);
        headlight_1_r1.setPos(-7.2465F, -13.881F, -33.7767F);
        headlights.addChild(headlight_1_r1);
        setRotationAngle(headlight_1_r1, -0.1309F, 0.2182F, 0);
        headlight_1_r1.texOffs(255, 249).addBox(-12, 0, -0.01F, 12, 11, 0, 0, false);

        tail_lights = new ModelMapper(modelDataWrapper);
        tail_lights.setPos(0, 24, 0);


        tail_light_2_r1 = new ModelMapper(modelDataWrapper);
        tail_light_2_r1.setPos(7.132F, -13.881F, -33.7767F);
        tail_lights.addChild(tail_light_2_r1);
        setRotationAngle(tail_light_2_r1, -0.1309F, -0.2182F, 0);
        tail_light_2_r1.texOffs(26, 109).addBox(0, 0, -0.01F, 12, 11, 0, 0, true);

        tail_light_1_r1 = new ModelMapper(modelDataWrapper);
        tail_light_1_r1.setPos(-7.2465F, -13.881F, -33.7767F);
        tail_lights.addChild(tail_light_1_r1);
        setRotationAngle(tail_light_1_r1, -0.1309F, 0.2182F, 0);
        tail_light_1_r1.texOffs(26, 109).addBox(-12, 0, -0.01F, 12, 11, 0, 0, false);

        roof_head = new ModelMapper(modelDataWrapper);
        roof_head.setPos(0, 24, 0);


        side_5 = new ModelMapper(modelDataWrapper);
        side_5.setPos(0, 0, 0);
        roof_head.addChild(side_5);
        side_5.texOffs(3, 17).addBox(-10.1444F, -36.2357F, -4, 2, 0, 14, 0, false);
        side_5.texOffs(16, 28).addBox(-16.0123F, -32.1399F, -4, 3, 0, 14, 0, false);
        side_5.texOffs(102, 82).addBox(-6, -37.25F, -4, 6, 0, 14, 0, false);

        inner_roof_5_r2 = new ModelMapper(modelDataWrapper);
        inner_roof_5_r2.setPos(-13.0123F, -32.1409F, 26);
        side_5.addChild(inner_roof_5_r2);
        setRotationAngle(inner_roof_5_r2, 0, 0, -0.9599F);
        inner_roof_5_r2.texOffs(3, 0).addBox(0, 0.001F, -30, 5, 0, 14, 0, false);

        side_6 = new ModelMapper(modelDataWrapper);
        side_6.setPos(0, 0, 0);
        roof_head.addChild(side_6);
        side_6.texOffs(3, 17).addBox(8.1444F, -36.2357F, -4, 2, 0, 14, 0, true);
        side_6.texOffs(16, 28).addBox(13.0123F, -32.1399F, -4, 3, 0, 14, 0, true);
        side_6.texOffs(102, 82).addBox(0, -37.25F, -4, 6, 0, 14, 0, true);

        inner_roof_6_r1 = new ModelMapper(modelDataWrapper);
        inner_roof_6_r1.setPos(10.1444F, -36.2367F, 26);
        side_6.addChild(inner_roof_6_r1);
        setRotationAngle(inner_roof_6_r1, 0, 0, 0.9599F);
        inner_roof_6_r1.texOffs(3, 0).addBox(0, 0.001F, -30, 5, 0, 14, 0, false);

        door_light_on = new ModelMapper(modelDataWrapper);
        door_light_on.setPos(0, 24, 0);


        light_r1 = new ModelMapper(modelDataWrapper);
        light_r1.setPos(-20, -14, 0);
        door_light_on.addChild(light_r1);
        setRotationAngle(light_r1, 0, 0, 0.1107F);
        light_r1.texOffs(32, 319).addBox(-1, -19.25F, 0, 0, 0, 0, 0.4F, false);

        door_light_off = new ModelMapper(modelDataWrapper);
        door_light_off.setPos(0, 24, 0);


        light_r2 = new ModelMapper(modelDataWrapper);
        light_r2.setPos(-20, -14, 0);
        door_light_off.addChild(light_r2);
        setRotationAngle(light_r2, 0, 0, 0.1107F);
        light_r2.texOffs(30, 319).addBox(-1, -19.25F, 0, 0, 0, 0, 0.4F, false);

        end_handrail = new ModelMapper(modelDataWrapper);
        end_handrail.setPos(0, 24, 0);
        end_handrail.texOffs(319, 0).addBox(0, -12, 0, 0, 12, 0, 0.2F, false);
        end_handrail.texOffs(316, 13).addBox(0, -23.2645F, 0.563F, 0, 6, 0, 0.2F, false);
        end_handrail.texOffs(316, 13).addBox(0, -23.2645F, -0.563F, 0, 6, 0, 0.2F, false);
        end_handrail.texOffs(319, 0).addBox(0, -38, 0, 0, 9, 0, 0.2F, false);

        pole_top_diagonal_1_r2 = new ModelMapper(modelDataWrapper);
        pole_top_diagonal_1_r2.setPos(0.2F, -28.8F, 0.2F);
        end_handrail.addChild(pole_top_diagonal_1_r2);
        setRotationAngle(pole_top_diagonal_1_r2, -0.1047F, 0, 0);
        pole_top_diagonal_1_r2.texOffs(316, 3).addBox(-0.2F, 0.2069F, -0.2F, 0, 5, 0, 0.2F, false);

        pole_top_diagonal_2_r2 = new ModelMapper(modelDataWrapper);
        pole_top_diagonal_2_r2.setPos(0.2F, -28.8F, -0.2F);
        end_handrail.addChild(pole_top_diagonal_2_r2);
        setRotationAngle(pole_top_diagonal_2_r2, 0.1047F, 0, 0);
        pole_top_diagonal_2_r2.texOffs(316, 3).addBox(-0.2F, 0.2069F, 0.2F, 0, 5, 0, 0.2F, false);

        pole_bottom_diagonal_1_r2 = new ModelMapper(modelDataWrapper);
        pole_bottom_diagonal_1_r2.setPos(0, -14.4002F, -0.2819F);
        end_handrail.addChild(pole_bottom_diagonal_1_r2);
        setRotationAngle(pole_bottom_diagonal_1_r2, 0.1047F, 0, 0);
        pole_bottom_diagonal_1_r2.texOffs(316, 19).addBox(0, -2.5F, 0, 0, 5, 0, 0.2F, false);

        pole_bottom_diagonal_2_r2 = new ModelMapper(modelDataWrapper);
        pole_bottom_diagonal_2_r2.setPos(0, -14.4002F, 0.2819F);
        end_handrail.addChild(pole_bottom_diagonal_2_r2);
        setRotationAngle(pole_bottom_diagonal_2_r2, -0.1047F, 0, 0);
        pole_bottom_diagonal_2_r2.texOffs(316, 19).addBox(0, -2.5F, 0, 0, 5, 0, 0.2F, false);

        modelDataWrapper.setModelPart(textureWidth, textureHeight);
        window.setModelPart();
        window_exterior_1.setModelPart();
        window_exterior_2.setModelPart();
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
        end_exterior.setModelPart();
        seat.setModelPart();
        door_light.setModelPart();
        side_panel.setModelPart();
        side_panel_translucent.setModelPart();
        roof_window.setModelPart();
        roof_end.setModelPart();
        roof_head.setModelPart();
        roof_light.setModelPart();
        roof_door.setModelPart();
        roof_exterior.setModelPart();
        roof_head_exterior.setModelPart();
        roof_end_exterior.setModelPart();
        roof_vent.setModelPart();
        top_handrail.setModelPart();
        head.setModelPart();
        head_exterior.setModelPart();
        headlights.setModelPart();
        tail_lights.setModelPart();
        end_handrail.setModelPart();
        door_light_off.setModelPart();
        door_light_on.setModelPart();
    }

    private static final int DOOR_MAX = 14;

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
                    renderMirror(top_handrail, matrices, vertices, light, position);
                    renderMirror(seat, matrices, vertices, light, position);
                    renderMirror(side_panel, matrices, vertices, light, position - 20);
                    renderMirror(side_panel, matrices, vertices, light, position + 20);
                }
                break;
            case INTERIOR_TRANSLUCENT:
                renderMirror(side_panel_translucent, matrices, vertices, light, position - 20);
                renderMirror(side_panel_translucent, matrices, vertices, light, position + 20);
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
                renderMirror(door_light, matrices, vertices, light, position);
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
                    door_left_exterior_2.setOffset(0, 0, -doorRightZ);
                    door_right_exterior_2.setOffset(0, 0, doorRightZ);
                    renderOnceFlipped(door_exterior_2, matrices, vertices, light, position);
                } else {
                    door_left_exterior_1.setOffset(0, 0, doorRightZ);
                    door_right_exterior_1.setOffset(0, 0, -doorRightZ);
                    renderOnce(door_exterior_1, matrices, vertices, light, position);
                    door_left_exterior_2.setOffset(0, 0, -doorLeftZ);
                    door_right_exterior_2.setOffset(0, 0, doorLeftZ);
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
                renderMirror(roof_light, matrices, vertices, light, position + 20);
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
                renderOnce(roof_head_exterior, matrices, vertices, light, position);
                renderOnce(roof_vent, matrices, vertices, light, position);
                break;
        }
    }

    @Override
    protected void renderHeadPosition2(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean useHeadlights) {
        switch (renderStage) {
            case LIGHTS:
                renderMirror(roof_light, matrices, vertices, light, position - 20);
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
                renderOnceFlipped(roof_head_exterior, matrices, vertices, light, position);
                renderOnceFlipped(roof_vent, matrices, vertices, light, position);
                break;
        }
    }

    @Override
    protected void renderEndPosition1(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
        switch (renderStage) {
            case LIGHTS:
                renderMirror(roof_light, matrices, vertices, light, position + 17);
                break;
            case INTERIOR:
                renderOnce(end, matrices, vertices, light, position);
                if (renderDetails) {
                    renderOnce(roof_end, matrices, vertices, light, position);
                    renderMirror(side_panel, matrices, vertices, light, position +9);
                    renderOnce(end_handrail, matrices, vertices, light, position);
                }
                break;
            case INTERIOR_TRANSLUCENT:
                renderMirror(side_panel_translucent, matrices, vertices, light, position + 9);
                break;
            case EXTERIOR:
                renderOnce(end_exterior, matrices, vertices, light, position);
                renderOnce(roof_end_exterior, matrices, vertices, light, position);
                renderOnce(roof_vent, matrices, vertices, light, position);
                break;
        }
    }

    @Override
    protected void renderEndPosition2(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
        switch (renderStage) {
            case LIGHTS:
                renderMirror(roof_light, matrices, vertices, light, position - 17);
                break;
            case INTERIOR:
                renderOnceFlipped(end, matrices, vertices, light, position);
                if (renderDetails) {
                    renderOnceFlipped(roof_end, matrices, vertices, light, position);
                    renderMirror(side_panel, matrices, vertices, light, position -9);
                    renderOnceFlipped(end_handrail, matrices, vertices, light, position);
                }
                break;
            case INTERIOR_TRANSLUCENT:
                renderMirror(side_panel_translucent, matrices, vertices, light, position - 9);
                break;
            case EXTERIOR:
                renderOnceFlipped(end_exterior, matrices, vertices, light, position);
                renderOnceFlipped(roof_end_exterior, matrices, vertices, light, position);
                renderOnceFlipped(roof_vent, matrices, vertices, light, position);
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