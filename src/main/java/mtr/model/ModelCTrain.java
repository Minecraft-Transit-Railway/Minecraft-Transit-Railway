package mtr.model;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class ModelCTrain extends ModelTrainBase {
    private final ModelPart window;
    private final ModelPart upper_wall_2_r1;
    private final ModelPart upper_wall_1_r1;
    private final ModelPart upper_wall_r1;
    private final ModelPart window_handrails;
    private final ModelPart handrail_17_r1;
    private final ModelPart handrail_16_r1;
    private final ModelPart handrail_15_r1;
    private final ModelPart handrail_14_r1;
    private final ModelPart handrail_13_r1;
    private final ModelPart handrail_12_r1;
    private final ModelPart handrail_11_r1;
    private final ModelPart handrail_10_r1;
    private final ModelPart handrail_8_r1;
    private final ModelPart top_handrail_4_r1;
    private final ModelPart top_handrail_3_r1;
    private final ModelPart handrail_5_r1;
    private final ModelPart handrail_4_r1;
    private final ModelPart handrail_3_r1;
    private final ModelPart handrail_1_r1;
    private final ModelPart seat;
    private final ModelPart seat_back_r1;
    private final ModelPart window_exterior;
    private final ModelPart upper_wall_r2;
    private final ModelPart side_panel;
    private final ModelPart side_panel_translucent;
    private final ModelPart roof_window;
    private final ModelPart inner_roof_6_r1;
    private final ModelPart inner_roof_5_r1;
    private final ModelPart inner_roof_4_r1;
    private final ModelPart inner_roof_3_r1;
    private final ModelPart roof_door;
    private final ModelPart inner_roof_8_r1;
    private final ModelPart inner_roof_7_r1;
    private final ModelPart inner_roof_6_r2;
    private final ModelPart inner_roof_5_r2;
    private final ModelPart inner_roof_4_r2;
    private final ModelPart inner_roof_3_r2;
    private final ModelPart inner_roof_2_r1;
    private final ModelPart inner_roof_1_r1;
    private final ModelPart roof_exterior;
    private final ModelPart outer_roof_4_r1;
    private final ModelPart outer_roof_3_r1;
    private final ModelPart outer_roof_2_r1;
    private final ModelPart outer_roof_1_r1;
    private final ModelPart door;
    private final ModelPart door_left;
    private final ModelPart door_left_top_r1;
    private final ModelPart door_right;
    private final ModelPart door_right_top_r1;
    private final ModelPart door_handrail;
    private final ModelPart door_exterior;
    private final ModelPart door_left_exterior;
    private final ModelPart door_left_top_r2;
    private final ModelPart door_right_exterior;
    private final ModelPart door_right_top_r2;
    private final ModelPart end;
    private final ModelPart upper_wall_2_r2;
    private final ModelPart upper_wall_1_r2;
    private final ModelPart end_exterior;
    private final ModelPart upper_wall_2_r3;
    private final ModelPart upper_wall_1_r3;
    private final ModelPart roof_end_exterior;
    private final ModelPart vent_2_r1;
    private final ModelPart vent_1_r1;
    private final ModelPart outer_roof_1;
    private final ModelPart outer_roof_4_r2;
    private final ModelPart outer_roof_3_r2;
    private final ModelPart outer_roof_2_r2;
    private final ModelPart outer_roof_1_r2;
    private final ModelPart outer_roof_2;
    private final ModelPart outer_roof_4_r3;
    private final ModelPart outer_roof_3_r3;
    private final ModelPart outer_roof_2_r3;
    private final ModelPart outer_roof_1_r3;
    private final ModelPart roof_light;
    private final ModelPart roof_light_r1;
    private final ModelPart roof_end_light;
    private final ModelPart roof_light_2_r1;
    private final ModelPart roof_light_1_r1;
    private final ModelPart roof_head_exterior;
    private final ModelPart vent_2_r2;
    private final ModelPart vent_1_r2;
    private final ModelPart outer_roof_3;
    private final ModelPart outer_roof_7_r1;
    private final ModelPart outer_roof_6_r1;
    private final ModelPart outer_roof_4_r4;
    private final ModelPart outer_roof_3_r4;
    private final ModelPart outer_roof_2_r4;
    private final ModelPart outer_roof_4;
    private final ModelPart outer_roof_7_r2;
    private final ModelPart outer_roof_6_r2;
    private final ModelPart outer_roof_4_r5;
    private final ModelPart outer_roof_3_r5;
    private final ModelPart outer_roof_2_r5;
    private final ModelPart head;
    private final ModelPart driver_wall_2_r1;
    private final ModelPart upper_wall_2_r4;
    private final ModelPart upper_wall_1_r4;
    private final ModelPart head_exterior;
    private final ModelPart upper_wall_2_r5;
    private final ModelPart upper_wall_1_r5;
    private final ModelPart front;
    private final ModelPart side_1;
    private final ModelPart outer_roof_5_r1;
    private final ModelPart outer_roof_4_r6;
    private final ModelPart outer_roof_3_r6;
    private final ModelPart outer_roof_2_r6;
    private final ModelPart front_side_bottom_2_r1;
    private final ModelPart front_side_bottom_1_r1;
    private final ModelPart front_side_upper_1_r1;
    private final ModelPart front_side_lower_1_r1;
    private final ModelPart side_2;
    private final ModelPart outer_roof_5_r2;
    private final ModelPart outer_roof_4_r7;
    private final ModelPart outer_roof_3_r7;
    private final ModelPart outer_roof_2_r7;
    private final ModelPart front_side_bottom_4_r1;
    private final ModelPart front_side_bottom_3_r1;
    private final ModelPart front_side_upper_2_r1;
    private final ModelPart front_side_lower_2_r1;
    private final ModelPart front_panel;
    private final ModelPart panel_12_r1;
    private final ModelPart panel_11_r1;
    private final ModelPart panel_10_r1;
    private final ModelPart panel_9_r1;
    private final ModelPart panel_8_r1;
    private final ModelPart panel_7_r1;
    private final ModelPart panel_6_r1;
    private final ModelPart panel_5_r1;
    private final ModelPart panel_4_r1;
    private final ModelPart panel_3_r1;
    private final ModelPart panel_2_r1;
    private final ModelPart panel_1_r1;
    private final ModelPart headlights;
    private final ModelPart headlight_2_r1;
    private final ModelPart headlight_1_r1;
    private final ModelPart tail_lights;
    private final ModelPart tail_light_2_r1;
    private final ModelPart tail_light_1_r1;
    private final ModelPart door_light_on;
    private final ModelPart light_r1;
    private final ModelPart door_light_off;
    private final ModelPart light_r2;
    public ModelCTrain() {
        textureWidth = 320;
        textureHeight = 320;
        window = new ModelPart(this);
        window.setPivot(0.0F, 24.0F, 0.0F);
        window.setTextureOffset(0, 40).addCuboid(-20.0F, 0.0F, -24.0F, 20.0F, 1.0F, 48.0F, 0.0F, false);
        window.setTextureOffset(168, 0).addCuboid(-20.0061F, -13.8896F, -22.0F, 2.0F, 14.0F, 44.0F, 0.0F, false);
        window.setTextureOffset(0, 160).addCuboid(-20.0F, -14.0F, 21.99F, 3.0F, 14.0F, 5.0F, 0.0F, false);
        window.setTextureOffset(22, 160).addCuboid(-20.0F, -14.0F, -26.99F, 3.0F, 14.0F, 5.0F, 0.0F, false);

        upper_wall_2_r1 = new ModelPart(this);
        upper_wall_2_r1.setPivot(-20.9939F, -14.1104F, 41.0F);
        window.addChild(upper_wall_2_r1);
        setRotationAngle(upper_wall_2_r1, 0.0F, 0.0F, 0.1107F);
        upper_wall_2_r1.setTextureOffset(187, 269).addCuboid(1.0F, -19.0F, -67.99F, 3.0F, 19.0F, 5.0F, 0.0F, false);

        upper_wall_1_r1 = new ModelPart(this);
        upper_wall_1_r1.setPivot(-20.9939F, -14.1104F, -41.0F);
        window.addChild(upper_wall_1_r1);
        setRotationAngle(upper_wall_1_r1, 0.0F, 0.0F, 0.1107F);
        upper_wall_1_r1.setTextureOffset(268, 44).addCuboid(1.0F, -19.0F, 62.99F, 3.0F, 19.0F, 5.0F, 0.0F, false);

        upper_wall_r1 = new ModelPart(this);
        upper_wall_r1.setPivot(-21.0F, -14.0F, 0.0F);
        window.addChild(upper_wall_r1);
        setRotationAngle(upper_wall_r1, 0.0F, 0.0F, 0.1107F);
        upper_wall_r1.setTextureOffset(0, 163).addCuboid(1.0F, -16.0F, -22.0F, 2.0F, 16.0F, 44.0F, 0.0F, false);

        window_handrails = new ModelPart(this);
        window_handrails.setPivot(0.0F, 24.0F, 0.0F);
        window_handrails.setTextureOffset(316, 17).addCuboid(-11.3135F, -18.3633F, -22.0F, 0.0F, 5.0F, 0.0F, 0.2F, false);
        window_handrails.setTextureOffset(316, 0).addCuboid(0.0F, -35.0F, -9.0F, 0.0F, 35.0F, 0.0F, 0.2F, false);
        window_handrails.setTextureOffset(316, 0).addCuboid(0.0F, -35.0F, 9.0F, 0.0F, 35.0F, 0.0F, 0.2F, false);
        window_handrails.setTextureOffset(316, 17).addCuboid(-11.3135F, -18.3633F, 22.0F, 0.0F, 5.0F, 0.0F, 0.2F, false);
        window_handrails.setTextureOffset(8, 5).addCuboid(-1.0F, -32.0F, 21.0F, 2.0F, 4.0F, 0.0F, 0.0F, false);
        window_handrails.setTextureOffset(8, 5).addCuboid(-1.0F, -32.0F, 15.0F, 2.0F, 4.0F, 0.0F, 0.0F, false);
        window_handrails.setTextureOffset(8, 5).addCuboid(-1.0F, -32.0F, 3.0F, 2.0F, 4.0F, 0.0F, 0.0F, false);
        window_handrails.setTextureOffset(8, 5).addCuboid(-1.0F, -32.0F, -3.0F, 2.0F, 4.0F, 0.0F, 0.0F, false);
        window_handrails.setTextureOffset(8, 5).addCuboid(-1.0F, -32.0F, -15.0F, 2.0F, 4.0F, 0.0F, 0.0F, false);
        window_handrails.setTextureOffset(8, 5).addCuboid(-1.0F, -32.0F, -21.0F, 2.0F, 4.0F, 0.0F, 0.0F, false);

        handrail_17_r1 = new ModelPart(this);
        handrail_17_r1.setPivot(-12.6434F, -3.3539F, 0.0F);
        window_handrails.addChild(handrail_17_r1);
        setRotationAngle(handrail_17_r1, 0.0F, 0.0F, 0.0873F);
        handrail_17_r1.setTextureOffset(316, 9).addCuboid(0.0F, -19.45F, -22.0F, 0.0F, 4.0F, 0.0F, 0.2F, false);

        handrail_16_r1 = new ModelPart(this);
        handrail_16_r1.setPivot(-12.6434F, -3.3539F, 0.0F);
        window_handrails.addChild(handrail_16_r1);
        setRotationAngle(handrail_16_r1, 0.0F, 0.0F, 0.0873F);
        handrail_16_r1.setTextureOffset(316, 9).addCuboid(0.0F, -19.45F, 22.0F, 0.0F, 4.0F, 0.0F, 0.2F, false);

        handrail_15_r1 = new ModelPart(this);
        handrail_15_r1.setPivot(-10.1335F, -27.6076F, 20.7938F);
        window_handrails.addChild(handrail_15_r1);
        setRotationAngle(handrail_15_r1, 1.0472F, 0.0F, 0.2618F);
        handrail_15_r1.setTextureOffset(319, 0).addCuboid(0.0F, -0.5F, 0.0F, 0.0F, 1.0F, 0.0F, 0.2F, false);

        handrail_14_r1 = new ModelPart(this);
        handrail_14_r1.setPivot(-10.362F, -26.7547F, 21.6768F);
        window_handrails.addChild(handrail_14_r1);
        setRotationAngle(handrail_14_r1, 0.5236F, 0.0F, 0.2618F);
        handrail_14_r1.setTextureOffset(319, 0).addCuboid(0.0F, -0.5F, 0.0F, 0.0F, 1.0F, 0.0F, 0.2F, false);

        handrail_13_r1 = new ModelPart(this);
        handrail_13_r1.setPivot(-10.3923F, -26.6416F, -22.5259F);
        window_handrails.addChild(handrail_13_r1);
        setRotationAngle(handrail_13_r1, -1.0472F, 0.0F, 0.2618F);
        handrail_13_r1.setTextureOffset(319, 0).addCuboid(0.0F, -2.5F, 0.0F, 0.0F, 1.0F, 0.0F, 0.2F, false);

        handrail_12_r1 = new ModelPart(this);
        handrail_12_r1.setPivot(-10.362F, -26.7546F, -21.6768F);
        window_handrails.addChild(handrail_12_r1);
        setRotationAngle(handrail_12_r1, -0.5236F, 0.0F, 0.2618F);
        handrail_12_r1.setTextureOffset(319, 0).addCuboid(0.0F, -0.5F, 0.0F, 0.0F, 1.0F, 0.0F, 0.2F, false);

        handrail_11_r1 = new ModelPart(this);
        handrail_11_r1.setPivot(-10.253F, -28.0759F, 22.0F);
        window_handrails.addChild(handrail_11_r1);
        setRotationAngle(handrail_11_r1, 0.0F, 0.0F, 0.1309F);
        handrail_11_r1.setTextureOffset(316, 6).addCuboid(0.0F, 2.0F, -44.0F, 0.0F, 3.0F, 0.0F, 0.2F, false);

        handrail_10_r1 = new ModelPart(this);
        handrail_10_r1.setPivot(-10.253F, -28.0759F, 22.0F);
        window_handrails.addChild(handrail_10_r1);
        setRotationAngle(handrail_10_r1, 0.0F, 0.0F, 0.1309F);
        handrail_10_r1.setTextureOffset(316, 6).addCuboid(0.0F, 2.0F, 0.0F, 0.0F, 3.0F, 0.0F, 0.2F, false);

        handrail_8_r1 = new ModelPart(this);
        handrail_8_r1.setPivot(0.0F, 0.0F, 0.0F);
        window_handrails.addChild(handrail_8_r1);
        setRotationAngle(handrail_8_r1, -1.5708F, 0.0F, 0.0F);
        handrail_8_r1.setTextureOffset(319, 0).addCuboid(0.0F, -40.0F, -31.5F, 0.0F, 80.0F, 0.0F, 0.2F, false);

        top_handrail_4_r1 = new ModelPart(this);
        top_handrail_4_r1.setPivot(-9.8084F, -27.8551F, 0.6584F);
        window_handrails.addChild(top_handrail_4_r1);
        setRotationAngle(top_handrail_4_r1, -1.5708F, 0.0F, 0.2618F);
        top_handrail_4_r1.setTextureOffset(319, 0).addCuboid(-0.25F, -19.4292F, 0.0F, 0.0F, 20.0F, 0.0F, 0.2F, false);

        top_handrail_3_r1 = new ModelPart(this);
        top_handrail_3_r1.setPivot(-9.8084F, -27.855F, -0.5168F);
        window_handrails.addChild(top_handrail_3_r1);
        setRotationAngle(top_handrail_3_r1, -1.5708F, 0.0F, 0.2618F);
        top_handrail_3_r1.setTextureOffset(319, 0).addCuboid(-0.25F, -0.4292F, 0.0F, 0.0F, 20.0F, 0.0F, 0.2F, false);

        handrail_5_r1 = new ModelPart(this);
        handrail_5_r1.setPivot(-9.4113F, -31.2686F, -20.0876F);
        window_handrails.addChild(handrail_5_r1);
        setRotationAngle(handrail_5_r1, 0.0F, 0.0F, 0.2618F);
        handrail_5_r1.setTextureOffset(319, 0).addCuboid(0.25F, -3.0F, 6.0876F, 0.0F, 6.0F, 0.0F, 0.2F, false);

        handrail_4_r1 = new ModelPart(this);
        handrail_4_r1.setPivot(-9.4113F, -31.2686F, 20.0876F);
        window_handrails.addChild(handrail_4_r1);
        setRotationAngle(handrail_4_r1, 0.0F, 0.0F, 0.2618F);
        handrail_4_r1.setTextureOffset(319, 0).addCuboid(0.25F, -3.0F, -6.0876F, 0.0F, 6.0F, 0.0F, 0.2F, false);

        handrail_3_r1 = new ModelPart(this);
        handrail_3_r1.setPivot(-10.5822F, -4.8127F, 0.0F);
        window_handrails.addChild(handrail_3_r1);
        setRotationAngle(handrail_3_r1, 0.0F, 0.0F, -0.0873F);
        handrail_3_r1.setTextureOffset(319, 0).addCuboid(0.0F, -8.2F, 22.0F, 0.0F, 6.0F, 0.0F, 0.2F, false);

        handrail_1_r1 = new ModelPart(this);
        handrail_1_r1.setPivot(-10.5822F, -4.8127F, 0.0F);
        window_handrails.addChild(handrail_1_r1);
        setRotationAngle(handrail_1_r1, 0.0F, 0.0F, -0.0873F);
        handrail_1_r1.setTextureOffset(319, 0).addCuboid(0.0F, -8.2F, -22.0F, 0.0F, 6.0F, 0.0F, 0.2F, false);

        seat = new ModelPart(this);
        seat.setPivot(0.0F, 0.0F, 0.0F);
        window_handrails.addChild(seat);
        seat.setTextureOffset(160, 91).addCuboid(-18.0F, -6.0F, -22.0F, 7.0F, 1.0F, 44.0F, 0.0F, false);

        seat_back_r1 = new ModelPart(this);
        seat_back_r1.setPivot(-17.0F, -6.0F, 0.0F);
        seat.addChild(seat_back_r1);
        setRotationAngle(seat_back_r1, 0.0F, 0.0F, -0.0524F);
        seat_back_r1.setTextureOffset(174, 136).addCuboid(-1.0F, -8.0F, -22.0F, 1.0F, 8.0F, 44.0F, 0.0F, false);

        window_exterior = new ModelPart(this);
        window_exterior.setPivot(0.0F, 24.0F, 0.0F);
        window_exterior.setTextureOffset(124, 159).addCuboid(-21.0F, 0.0F, -24.0F, 1.0F, 4.0F, 48.0F, 0.0F, false);
        window_exterior.setTextureOffset(106, 91).addCuboid(-20.0F, -14.0F, -26.0F, 1.0F, 14.0F, 52.0F, 0.0F, false);

        upper_wall_r2 = new ModelPart(this);
        upper_wall_r2.setPivot(-20.0F, -14.0F, 0.0F);
        window_exterior.addChild(upper_wall_r2);
        setRotationAngle(upper_wall_r2, 0.0F, 0.0F, 0.1107F);
        upper_wall_r2.setTextureOffset(0, 89).addCuboid(0.0F, -19.0F, -26.0F, 1.0F, 19.0F, 52.0F, 0.0F, false);

        side_panel = new ModelPart(this);
        side_panel.setPivot(0.0F, 24.0F, 0.0F);
        side_panel.setTextureOffset(262, 188).addCuboid(-18.0F, -34.0F, 0.0F, 8.0F, 30.0F, 0.0F, 0.0F, false);

        side_panel_translucent = new ModelPart(this);
        side_panel_translucent.setPivot(0.0F, 24.0F, 0.0F);
        side_panel_translucent.setTextureOffset(150, 211).addCuboid(-18.0F, -34.0F, 0.0F, 8.0F, 30.0F, 0.0F, 0.0F, false);

        roof_window = new ModelPart(this);
        roof_window.setPivot(0.0F, 24.0F, 0.0F);
        roof_window.setTextureOffset(86, 40).addCuboid(-16.2497F, -29.2929F, -22.0F, 1.0F, 0.0F, 44.0F, 0.0F, false);
        roof_window.setTextureOffset(84, 40).addCuboid(-0.5151F, -35.1216F, -22.0F, 1.0F, 0.0F, 44.0F, 0.0F, false);

        inner_roof_6_r1 = new ModelPart(this);
        inner_roof_6_r1.setPivot(-12.598F, -31.9445F, -6.0F);
        roof_window.addChild(inner_roof_6_r1);
        setRotationAngle(inner_roof_6_r1, 0.0F, 0.0F, -0.7854F);
        inner_roof_6_r1.setTextureOffset(44, 40).addCuboid(-3.75F, 0.0F, -16.0F, 4.0F, 0.0F, 44.0F, 0.0F, false);

        inner_roof_5_r1 = new ModelPart(this);
        inner_roof_5_r1.setPivot(-2.5075F, -34.9472F, -14.0F);
        roof_window.addChild(inner_roof_5_r1);
        setRotationAngle(inner_roof_5_r1, 0.0F, 0.0F, -0.0873F);
        inner_roof_5_r1.setTextureOffset(52, 40).addCuboid(-2.0F, 0.0F, -8.0F, 4.0F, 0.0F, 44.0F, 0.0F, false);

        inner_roof_4_r1 = new ModelPart(this);
        inner_roof_4_r1.setPivot(-6.9741F, -34.3814F, -6.0F);
        roof_window.addChild(inner_roof_4_r1);
        setRotationAngle(inner_roof_4_r1, 0.0F, 0.0F, -0.1309F);
        inner_roof_4_r1.setTextureOffset(0, 40).addCuboid(-2.0F, 0.0F, -16.0F, 2.0F, 0.0F, 44.0F, 0.0F, false);

        inner_roof_3_r1 = new ModelPart(this);
        inner_roof_3_r1.setPivot(-12.4212F, -32.1213F, -6.0F);
        roof_window.addChild(inner_roof_3_r1);
        setRotationAngle(inner_roof_3_r1, 0.0F, 0.0F, -0.5236F);
        inner_roof_3_r1.setTextureOffset(60, 40).addCuboid(0.0F, 0.0F, -16.0F, 4.0F, 0.0F, 44.0F, 0.0F, false);

        roof_door = new ModelPart(this);
        roof_door.setPivot(0.0F, 24.0F, 0.0F);
        roof_door.setTextureOffset(0, 89).addCuboid(-0.5151F, -35.1216F, -18.0F, 1.0F, 0.0F, 36.0F, 0.0F, false);

        inner_roof_8_r1 = new ModelPart(this);
        inner_roof_8_r1.setPivot(-12.1039F, -33.0696F, -17.0F);
        roof_door.addChild(inner_roof_8_r1);
        setRotationAngle(inner_roof_8_r1, 0.0F, 0.0F, -0.7854F);
        inner_roof_8_r1.setTextureOffset(0, 9).addCuboid(-5.8949F, -1.5548F, 30.0F, 5.0F, 2.0F, 0.0F, 0.0F, false);

        inner_roof_7_r1 = new ModelPart(this);
        inner_roof_7_r1.setPivot(-14.8961F, -31.0606F, -13.0F);
        roof_door.addChild(inner_roof_7_r1);
        setRotationAngle(inner_roof_7_r1, 0.0F, 3.1416F, -0.7854F);
        inner_roof_7_r1.setTextureOffset(0, 9).addCuboid(-2.5F, -1.0F, 0.0F, 5.0F, 2.0F, 0.0F, 0.0F, true);

        inner_roof_6_r2 = new ModelPart(this);
        inner_roof_6_r2.setPivot(-14.1883F, -30.3528F, -15.5F);
        roof_door.addChild(inner_roof_6_r2);
        setRotationAngle(inner_roof_6_r2, 0.0F, 1.5708F, -0.7854F);
        inner_roof_6_r2.setTextureOffset(17, 68).addCuboid(-2.5F, 0.0F, -2.5F, 5.0F, 0.0F, 5.0F, 0.0F, false);

        inner_roof_5_r2 = new ModelPart(this);
        inner_roof_5_r2.setPivot(-14.189F, -30.3535F, 15.5F);
        roof_door.addChild(inner_roof_5_r2);
        setRotationAngle(inner_roof_5_r2, 0.0F, 1.5708F, -0.7854F);
        inner_roof_5_r2.setTextureOffset(17, 68).addCuboid(-2.5F, 0.0F, -2.5F, 5.0F, 0.0F, 5.0F, 0.0F, true);

        inner_roof_4_r2 = new ModelPart(this);
        inner_roof_4_r2.setPivot(-2.5075F, -34.9472F, -8.0F);
        roof_door.addChild(inner_roof_4_r2);
        setRotationAngle(inner_roof_4_r2, 0.0F, 0.0F, -0.0873F);
        inner_roof_4_r2.setTextureOffset(0, 40).addCuboid(-2.0F, 0.0F, -10.0F, 4.0F, 0.0F, 36.0F, 0.0F, false);

        inner_roof_3_r2 = new ModelPart(this);
        inner_roof_3_r2.setPivot(-6.9741F, -34.3814F, 0.0F);
        roof_door.addChild(inner_roof_3_r2);
        setRotationAngle(inner_roof_3_r2, 0.0F, 0.0F, -0.1309F);
        inner_roof_3_r2.setTextureOffset(88, 0).addCuboid(-2.0F, 0.0F, -18.0F, 2.0F, 0.0F, 36.0F, 0.0F, false);

        inner_roof_2_r1 = new ModelPart(this);
        inner_roof_2_r1.setPivot(-10.6892F, -33.1213F, 0.0F);
        roof_door.addChild(inner_roof_2_r1);
        setRotationAngle(inner_roof_2_r1, 0.0F, 3.1416F, -0.5236F);
        inner_roof_2_r1.setTextureOffset(80, 0).addCuboid(-2.0F, 0.0F, -18.0F, 4.0F, 0.0F, 36.0F, 0.0F, true);

        inner_roof_1_r1 = new ModelPart(this);
        inner_roof_1_r1.setPivot(-12.4217F, -32.1212F, 0.0F);
        roof_door.addChild(inner_roof_1_r1);
        setRotationAngle(inner_roof_1_r1, 0.0F, 0.0F, -0.0413F);
        inner_roof_1_r1.setTextureOffset(42, 89).addCuboid(-6.0F, 0.0F, -13.0F, 6.0F, 0.0F, 26.0F, 0.0F, false);

        roof_exterior = new ModelPart(this);
        roof_exterior.setPivot(0.0F, 24.0F, 0.0F);
        roof_exterior.setTextureOffset(0, 89).addCuboid(-5.7229F, -41.9636F, -20.0F, 6.0F, 0.0F, 40.0F, 0.0F, false);

        outer_roof_4_r1 = new ModelPart(this);
        outer_roof_4_r1.setPivot(-9.662F, -41.269F, 0.0F);
        roof_exterior.addChild(outer_roof_4_r1);
        setRotationAngle(outer_roof_4_r1, 0.0F, 0.0F, -0.1745F);
        outer_roof_4_r1.setTextureOffset(72, 40).addCuboid(-4.0F, 0.0F, -20.0F, 8.0F, 0.0F, 40.0F, 0.0F, false);

        outer_roof_3_r1 = new ModelPart(this);
        outer_roof_3_r1.setPivot(-15.3333F, -39.5745F, 0.0F);
        roof_exterior.addChild(outer_roof_3_r1);
        setRotationAngle(outer_roof_3_r1, 0.0F, 0.0F, -0.5236F);
        outer_roof_3_r1.setTextureOffset(14, 89).addCuboid(-2.0F, 0.0F, -20.0F, 4.0F, 0.0F, 40.0F, 0.0F, false);

        outer_roof_2_r1 = new ModelPart(this);
        outer_roof_2_r1.setPivot(-17.8145F, -37.2749F, 0.0F);
        roof_exterior.addChild(outer_roof_2_r1);
        setRotationAngle(outer_roof_2_r1, 0.0F, 0.0F, -1.0472F);
        outer_roof_2_r1.setTextureOffset(22, 89).addCuboid(-1.5F, 0.0F, -20.0F, 3.0F, 0.0F, 40.0F, 0.0F, false);

        outer_roof_1_r1 = new ModelPart(this);
        outer_roof_1_r1.setPivot(-20.0F, -14.0F, 0.0F);
        roof_exterior.addChild(outer_roof_1_r1);
        setRotationAngle(outer_roof_1_r1, 0.0F, 0.0F, 0.1107F);
        outer_roof_1_r1.setTextureOffset(52, 207).addCuboid(-1.0F, -22.0F, -20.0F, 1.0F, 4.0F, 40.0F, 0.0F, false);

        door = new ModelPart(this);
        door.setPivot(0.0F, 24.0F, 0.0F);
        door.setTextureOffset(190, 188).addCuboid(-20.0F, 0.0F, -16.0F, 20.0F, 1.0F, 32.0F, 0.0F, false);

        door_left = new ModelPart(this);
        door_left.setPivot(0.0F, 0.0F, 0.0F);
        door.addChild(door_left);
        door_left.setTextureOffset(0, 170).addCuboid(-20.0F, -14.0F, 0.0F, 0.0F, 14.0F, 14.0F, 0.0F, false);

        door_left_top_r1 = new ModelPart(this);
        door_left_top_r1.setPivot(-21.0F, -14.0F, 0.0F);
        door_left.addChild(door_left_top_r1);
        setRotationAngle(door_left_top_r1, 0.0F, 0.0F, 0.1107F);
        door_left_top_r1.setTextureOffset(75, 146).addCuboid(1.0F, -19.0F, 0.0F, 0.0F, 19.0F, 14.0F, 0.0F, false);

        door_right = new ModelPart(this);
        door_right.setPivot(0.0F, 0.0F, 0.0F);
        door.addChild(door_right);
        door_right.setTextureOffset(0, 26).addCuboid(-20.0F, -14.0F, -14.0F, 0.0F, 14.0F, 14.0F, 0.0F, false);

        door_right_top_r1 = new ModelPart(this);
        door_right_top_r1.setPivot(-21.0F, -14.0F, 0.0F);
        door_right.addChild(door_right_top_r1);
        setRotationAngle(door_right_top_r1, 0.0F, 0.0F, 0.1107F);
        door_right_top_r1.setTextureOffset(0, 75).addCuboid(1.0F, -19.0F, -14.0F, 0.0F, 19.0F, 14.0F, 0.0F, false);

        door_handrail = new ModelPart(this);
        door_handrail.setPivot(0.0F, 24.0F, 0.0F);
        door_handrail.setTextureOffset(316, 0).addCuboid(0.0F, -35.0F, 0.0F, 0.0F, 35.0F, 0.0F, 0.2F, false);

        door_exterior = new ModelPart(this);
        door_exterior.setPivot(0.0F, 24.0F, 0.0F);
        door_exterior.setTextureOffset(222, 242).addCuboid(-21.0F, 0.0F, -16.0F, 1.0F, 4.0F, 32.0F, 0.0F, false);

        door_left_exterior = new ModelPart(this);
        door_left_exterior.setPivot(0.0F, 0.0F, 0.0F);
        door_exterior.addChild(door_left_exterior);
        door_left_exterior.setTextureOffset(220, 136).addCuboid(-21.0F, -14.0F, 0.0F, 1.0F, 14.0F, 14.0F, 0.0F, false);

        door_left_top_r2 = new ModelPart(this);
        door_left_top_r2.setPivot(-21.0F, -14.0F, 0.0F);
        door_left_exterior.addChild(door_left_top_r2);
        setRotationAngle(door_left_top_r2, 0.0F, 0.0F, 0.1107F);
        door_left_top_r2.setTextureOffset(160, 91).addCuboid(0.0F, -19.0F, 0.0F, 1.0F, 19.0F, 14.0F, 0.0F, false);

        door_right_exterior = new ModelPart(this);
        door_right_exterior.setPivot(0.0F, 0.0F, 0.0F);
        door_exterior.addChild(door_right_exterior);
        door_right_exterior.setTextureOffset(168, 211).addCuboid(-21.0F, -14.0F, -14.0F, 1.0F, 14.0F, 14.0F, 0.0F, false);

        door_right_top_r2 = new ModelPart(this);
        door_right_top_r2.setPivot(-21.0F, -14.0F, 0.0F);
        door_right_exterior.addChild(door_right_top_r2);
        setRotationAngle(door_right_top_r2, 0.0F, 0.0F, 0.1107F);
        door_right_top_r2.setTextureOffset(0, 0).addCuboid(0.0F, -19.0F, -14.0F, 1.0F, 19.0F, 14.0F, 0.0F, false);

        end = new ModelPart(this);
        end.setPivot(0.0F, 24.0F, 0.0F);
        end.setTextureOffset(168, 58).addCuboid(-20.0F, 0.0F, -12.0F, 40.0F, 1.0F, 20.0F, 0.0F, false);
        end.setTextureOffset(0, 233).addCuboid(9.5F, -35.0F, -12.0F, 9.0F, 35.0F, 18.0F, 0.0F, false);
        end.setTextureOffset(200, 221).addCuboid(-18.5F, -35.0F, -12.0F, 9.0F, 35.0F, 18.0F, 0.0F, false);
        end.setTextureOffset(54, 251).addCuboid(-9.5F, -35.1227F, -12.0F, 19.0F, 3.0F, 18.0F, 0.0F, false);
        end.setTextureOffset(135, 91).addCuboid(-20.0F, -14.0F, 6.0F, 3.0F, 14.0F, 5.0F, 0.0F, false);
        end.setTextureOffset(108, 91).addCuboid(17.0F, -14.0F, 6.0F, 3.0F, 14.0F, 5.0F, 0.0F, false);

        upper_wall_2_r2 = new ModelPart(this);
        upper_wall_2_r2.setPivot(-10.8104F, -10.5767F, -41.0F);
        end.addChild(upper_wall_2_r2);
        setRotationAngle(upper_wall_2_r2, 0.0F, 0.0F, -0.1107F);
        upper_wall_2_r2.setTextureOffset(94, 207).addCuboid(28.0F, -19.0F, 47.0F, 3.0F, 19.0F, 5.0F, 0.0F, false);

        upper_wall_1_r2 = new ModelPart(this);
        upper_wall_1_r2.setPivot(-20.9939F, -14.1104F, -41.0F);
        end.addChild(upper_wall_1_r2);
        setRotationAngle(upper_wall_1_r2, 0.0F, 0.0F, 0.1107F);
        upper_wall_1_r2.setTextureOffset(0, 223).addCuboid(1.0F, -19.0F, 47.0F, 3.0F, 19.0F, 5.0F, 0.0F, false);

        end_exterior = new ModelPart(this);
        end_exterior.setPivot(0.0F, 24.0F, 0.0F);
        end_exterior.setTextureOffset(0, 160).addCuboid(20.0F, 0.0F, -12.0F, 1.0F, 4.0F, 20.0F, 0.0F, true);
        end_exterior.setTextureOffset(0, 160).addCuboid(-21.0F, 0.0F, -12.0F, 1.0F, 4.0F, 20.0F, 0.0F, false);
        end_exterior.setTextureOffset(256, 221).addCuboid(18.0F, -14.0F, -12.0F, 2.0F, 14.0F, 23.0F, 0.0F, true);
        end_exterior.setTextureOffset(105, 251).addCuboid(-20.0F, -14.0F, -12.0F, 2.0F, 14.0F, 23.0F, 0.0F, false);
        end_exterior.setTextureOffset(82, 91).addCuboid(9.5F, -34.0F, -12.0F, 9.0F, 34.0F, 0.0F, 0.0F, false);
        end_exterior.setTextureOffset(82, 91).addCuboid(-18.5F, -34.0F, -12.0F, 9.0F, 34.0F, 0.0F, 0.0F, true);
        end_exterior.setTextureOffset(168, 79).addCuboid(-18.0F, -41.0F, -12.0F, 36.0F, 7.0F, 0.0F, 0.0F, false);

        upper_wall_2_r3 = new ModelPart(this);
        upper_wall_2_r3.setPivot(-20.0F, -14.0F, 0.0F);
        end_exterior.addChild(upper_wall_2_r3);
        setRotationAngle(upper_wall_2_r3, 0.0F, 0.0F, 0.1107F);
        upper_wall_2_r3.setTextureOffset(108, 91).addCuboid(0.0F, -19.0F, -12.0F, 2.0F, 19.0F, 23.0F, 0.0F, false);

        upper_wall_1_r3 = new ModelPart(this);
        upper_wall_1_r3.setPivot(20.0F, -14.0F, 0.0F);
        end_exterior.addChild(upper_wall_1_r3);
        setRotationAngle(upper_wall_1_r3, 0.0F, 0.0F, -0.1107F);
        upper_wall_1_r3.setTextureOffset(48, 160).addCuboid(-2.0F, -19.0F, -12.0F, 2.0F, 19.0F, 23.0F, 0.0F, true);

        roof_end_exterior = new ModelPart(this);
        roof_end_exterior.setPivot(0.0F, 24.0F, 0.0F);
        roof_end_exterior.setTextureOffset(88, 41).addCuboid(-8.0F, -43.0F, 0.0F, 16.0F, 2.0F, 48.0F, 0.0F, false);

        vent_2_r1 = new ModelPart(this);
        vent_2_r1.setPivot(-8.0F, -43.0F, 0.0F);
        roof_end_exterior.addChild(vent_2_r1);
        setRotationAngle(vent_2_r1, 0.0F, 0.0F, -0.3491F);
        vent_2_r1.setTextureOffset(58, 157).addCuboid(-9.0F, 0.0F, 0.0F, 9.0F, 2.0F, 48.0F, 0.0F, false);

        vent_1_r1 = new ModelPart(this);
        vent_1_r1.setPivot(8.0F, -43.0F, 0.0F);
        roof_end_exterior.addChild(vent_1_r1);
        setRotationAngle(vent_1_r1, 0.0F, 0.0F, 0.3491F);
        vent_1_r1.setTextureOffset(58, 157).addCuboid(0.0F, 0.0F, 0.0F, 9.0F, 2.0F, 48.0F, 0.0F, true);

        outer_roof_1 = new ModelPart(this);
        outer_roof_1.setPivot(0.0F, 0.0F, 0.0F);
        roof_end_exterior.addChild(outer_roof_1);
        outer_roof_1.setTextureOffset(252, 79).addCuboid(-5.7219F, -41.9631F, -12.0F, 6.0F, 1.0F, 20.0F, 0.0F, false);

        outer_roof_4_r2 = new ModelPart(this);
        outer_roof_4_r2.setPivot(-9.4875F, -40.2837F, -8.0F);
        outer_roof_1.addChild(outer_roof_4_r2);
        setRotationAngle(outer_roof_4_r2, 0.0F, 0.0F, -0.1745F);
        outer_roof_4_r2.setTextureOffset(36, 223).addCuboid(-4.0F, -1.0F, -4.0F, 8.0F, 1.0F, 20.0F, 0.0F, false);

        outer_roof_3_r2 = new ModelPart(this);
        outer_roof_3_r2.setPivot(-14.3994F, -38.9579F, -8.0F);
        outer_roof_1.addChild(outer_roof_3_r2);
        setRotationAngle(outer_roof_3_r2, 0.0F, 0.0F, -0.5236F);
        outer_roof_3_r2.setTextureOffset(124, 157).addCuboid(-2.5F, -1.0F, -4.0F, 4.0F, 1.0F, 20.0F, 0.0F, false);

        outer_roof_2_r2 = new ModelPart(this);
        outer_roof_2_r2.setPivot(-16.6984F, -37.2079F, -8.0F);
        outer_roof_1.addChild(outer_roof_2_r2);
        setRotationAngle(outer_roof_2_r2, 0.0F, 0.0F, -1.0472F);
        outer_roof_2_r2.setTextureOffset(260, 129).addCuboid(-2.0F, -1.0F, -4.0F, 3.0F, 1.0F, 20.0F, 0.0F, false);

        outer_roof_1_r2 = new ModelPart(this);
        outer_roof_1_r2.setPivot(-20.0F, -14.0F, 0.0F);
        outer_roof_1.addChild(outer_roof_1_r2);
        setRotationAngle(outer_roof_1_r2, 0.0F, 0.0F, 0.1107F);
        outer_roof_1_r2.setTextureOffset(0, 56).addCuboid(-1.0F, -22.0F, -12.0F, 1.0F, 4.0F, 20.0F, 0.0F, false);

        outer_roof_2 = new ModelPart(this);
        outer_roof_2.setPivot(0.0F, 0.0F, 0.0F);
        roof_end_exterior.addChild(outer_roof_2);
        outer_roof_2.setTextureOffset(252, 79).addCuboid(-0.2781F, -41.9631F, -12.0F, 6.0F, 1.0F, 20.0F, 0.0F, true);

        outer_roof_4_r3 = new ModelPart(this);
        outer_roof_4_r3.setPivot(9.4875F, -40.2837F, -8.0F);
        outer_roof_2.addChild(outer_roof_4_r3);
        setRotationAngle(outer_roof_4_r3, 0.0F, 0.0F, 0.1745F);
        outer_roof_4_r3.setTextureOffset(36, 223).addCuboid(-4.0F, -1.0F, -4.0F, 8.0F, 1.0F, 20.0F, 0.0F, true);

        outer_roof_3_r3 = new ModelPart(this);
        outer_roof_3_r3.setPivot(15.2654F, -38.4579F, -8.0F);
        outer_roof_2.addChild(outer_roof_3_r3);
        setRotationAngle(outer_roof_3_r3, 0.0F, 0.0F, 0.5236F);
        outer_roof_3_r3.setTextureOffset(124, 157).addCuboid(-2.5F, -1.0F, -4.0F, 4.0F, 1.0F, 20.0F, 0.0F, true);

        outer_roof_2_r3 = new ModelPart(this);
        outer_roof_2_r3.setPivot(16.6984F, -37.2079F, -8.0F);
        outer_roof_2.addChild(outer_roof_2_r3);
        setRotationAngle(outer_roof_2_r3, 0.0F, 0.0F, 1.0472F);
        outer_roof_2_r3.setTextureOffset(260, 129).addCuboid(-1.0F, -1.0F, -4.0F, 3.0F, 1.0F, 20.0F, 0.0F, true);

        outer_roof_1_r3 = new ModelPart(this);
        outer_roof_1_r3.setPivot(20.0F, -14.0F, 0.0F);
        outer_roof_2.addChild(outer_roof_1_r3);
        setRotationAngle(outer_roof_1_r3, 0.0F, 0.0F, -0.1107F);
        outer_roof_1_r3.setTextureOffset(0, 56).addCuboid(0.0F, -22.0F, -12.0F, 1.0F, 4.0F, 20.0F, 0.0F, true);

        roof_light = new ModelPart(this);
        roof_light.setPivot(0.0F, 24.0F, 0.0F);
        setRotationAngle(roof_light, 0.0F, 3.1416F, 0.0F);


        roof_light_r1 = new ModelPart(this);
        roof_light_r1.setPivot(-4.6051F, -33.9585F, -16.0F);
        roof_light.addChild(roof_light_r1);
        setRotationAngle(roof_light_r1, 0.0F, 0.0F, -0.4363F);
        roof_light_r1.setTextureOffset(54, 91).addCuboid(-2.5605F, -1.6937F, -8.0F, 3.0F, 1.0F, 48.0F, 0.0F, false);

        roof_end_light = new ModelPart(this);
        roof_end_light.setPivot(0.0F, 24.0F, 0.0F);


        roof_light_2_r1 = new ModelPart(this);
        roof_light_2_r1.setPivot(6.5273F, -33.0622F, -48.0F);
        roof_end_light.addChild(roof_light_2_r1);
        setRotationAngle(roof_light_2_r1, 0.0F, 0.0F, 0.4363F);
        roof_light_2_r1.setTextureOffset(220, 136).addCuboid(-2.5605F, -1.6937F, 54.0F, 3.0F, 1.0F, 34.0F, 0.0F, true);

        roof_light_1_r1 = new ModelPart(this);
        roof_light_1_r1.setPivot(-4.6051F, -33.9585F, -32.0F);
        roof_end_light.addChild(roof_light_1_r1);
        setRotationAngle(roof_light_1_r1, 0.0F, 0.0F, -0.4363F);
        roof_light_1_r1.setTextureOffset(220, 136).addCuboid(-2.5605F, -1.6937F, 38.0F, 3.0F, 1.0F, 34.0F, 0.0F, false);

        roof_head_exterior = new ModelPart(this);
        roof_head_exterior.setPivot(0.0F, 24.0F, 0.0F);
        roof_head_exterior.setTextureOffset(88, 41).addCuboid(-8.0F, -43.0F, 0.0F, 16.0F, 2.0F, 48.0F, 0.0F, false);

        vent_2_r2 = new ModelPart(this);
        vent_2_r2.setPivot(-8.0F, -43.0F, 0.0F);
        roof_head_exterior.addChild(vent_2_r2);
        setRotationAngle(vent_2_r2, 0.0F, 0.0F, -0.3491F);
        vent_2_r2.setTextureOffset(58, 157).addCuboid(-9.0F, 0.0F, 0.0F, 9.0F, 2.0F, 48.0F, 0.0F, false);

        vent_1_r2 = new ModelPart(this);
        vent_1_r2.setPivot(8.0F, -43.0F, 0.0F);
        roof_head_exterior.addChild(vent_1_r2);
        setRotationAngle(vent_1_r2, 0.0F, 0.0F, 0.3491F);
        vent_1_r2.setTextureOffset(58, 157).addCuboid(0.0F, 0.0F, 0.0F, 9.0F, 2.0F, 48.0F, 0.0F, true);

        outer_roof_3 = new ModelPart(this);
        outer_roof_3.setPivot(0.0F, 0.0F, 6.0F);
        roof_head_exterior.addChild(outer_roof_3);
        outer_roof_3.setTextureOffset(0, 109).addCuboid(-5.7219F, -41.9631F, -18.0F, 6.0F, 1.0F, 20.0F, 0.0F, false);

        outer_roof_7_r1 = new ModelPart(this);
        outer_roof_7_r1.setPivot(-22.7756F, -14.3084F, 21.9723F);
        outer_roof_3.addChild(outer_roof_7_r1);
        setRotationAngle(outer_roof_7_r1, 0.0F, -0.0698F, 0.1107F);
        outer_roof_7_r1.setTextureOffset(0, 310).addCuboid(-1.0F, -22.0F, -49.0F, 2.0F, 1.0F, 9.0F, 0.0F, false);

        outer_roof_6_r1 = new ModelPart(this);
        outer_roof_6_r1.setPivot(-20.0F, -14.0F, 0.0F);
        outer_roof_3.addChild(outer_roof_6_r1);
        setRotationAngle(outer_roof_6_r1, 0.0F, 0.0F, 0.1107F);
        outer_roof_6_r1.setTextureOffset(83, 126).addCuboid(-1.0F, -22.0F, -18.0F, 1.0F, 1.0F, 6.0F, 0.0F, false);
        outer_roof_6_r1.setTextureOffset(14, 184).addCuboid(-1.0F, -22.0F, -12.0F, 1.0F, 4.0F, 14.0F, 0.0F, false);

        outer_roof_4_r4 = new ModelPart(this);
        outer_roof_4_r4.setPivot(-9.4875F, -40.2837F, -8.0F);
        outer_roof_3.addChild(outer_roof_4_r4);
        setRotationAngle(outer_roof_4_r4, 0.0F, 0.0F, -0.1745F);
        outer_roof_4_r4.setTextureOffset(94, 211).addCuboid(-4.0F, -1.0F, -10.0F, 8.0F, 1.0F, 20.0F, 0.0F, false);

        outer_roof_3_r4 = new ModelPart(this);
        outer_roof_3_r4.setPivot(-14.3994F, -38.9579F, -8.0F);
        outer_roof_3.addChild(outer_roof_3_r4);
        setRotationAngle(outer_roof_3_r4, 0.0F, 0.0F, -0.5236F);
        outer_roof_3_r4.setTextureOffset(54, 115).addCuboid(-2.5F, -1.0F, -10.0F, 4.0F, 1.0F, 20.0F, 0.0F, false);

        outer_roof_2_r4 = new ModelPart(this);
        outer_roof_2_r4.setPivot(-16.6984F, -37.2079F, -8.0F);
        outer_roof_3.addChild(outer_roof_2_r4);
        setRotationAngle(outer_roof_2_r4, 0.0F, 0.0F, -1.0472F);
        outer_roof_2_r4.setTextureOffset(124, 178).addCuboid(-2.0F, -1.0F, -10.0F, 3.0F, 1.0F, 20.0F, 0.0F, false);

        outer_roof_4 = new ModelPart(this);
        outer_roof_4.setPivot(0.0F, 0.0F, 6.0F);
        roof_head_exterior.addChild(outer_roof_4);
        outer_roof_4.setTextureOffset(0, 109).addCuboid(-0.2781F, -41.9631F, -18.0F, 6.0F, 1.0F, 20.0F, 0.0F, true);

        outer_roof_7_r2 = new ModelPart(this);
        outer_roof_7_r2.setPivot(20.1411F, -14.0157F, -15.9351F);
        outer_roof_4.addChild(outer_roof_7_r2);
        setRotationAngle(outer_roof_7_r2, 0.0F, 0.0698F, -0.1107F);
        outer_roof_7_r2.setTextureOffset(0, 310).addCuboid(-1.0F, -22.0F, -11.0F, 2.0F, 1.0F, 9.0F, 0.0F, true);

        outer_roof_6_r2 = new ModelPart(this);
        outer_roof_6_r2.setPivot(20.0F, -14.0F, 0.0F);
        outer_roof_4.addChild(outer_roof_6_r2);
        setRotationAngle(outer_roof_6_r2, 0.0F, 0.0F, -0.1107F);
        outer_roof_6_r2.setTextureOffset(83, 126).addCuboid(0.0F, -22.0F, -18.0F, 1.0F, 1.0F, 6.0F, 0.0F, true);
        outer_roof_6_r2.setTextureOffset(14, 184).addCuboid(0.0F, -22.0F, -12.0F, 1.0F, 4.0F, 14.0F, 0.0F, true);

        outer_roof_4_r5 = new ModelPart(this);
        outer_roof_4_r5.setPivot(9.4875F, -40.2837F, -8.0F);
        outer_roof_4.addChild(outer_roof_4_r5);
        setRotationAngle(outer_roof_4_r5, 0.0F, 0.0F, 0.1745F);
        outer_roof_4_r5.setTextureOffset(94, 211).addCuboid(-4.0F, -1.0F, -10.0F, 8.0F, 1.0F, 20.0F, 0.0F, true);

        outer_roof_3_r5 = new ModelPart(this);
        outer_roof_3_r5.setPivot(15.2654F, -38.4579F, -8.0F);
        outer_roof_4.addChild(outer_roof_3_r5);
        setRotationAngle(outer_roof_3_r5, 0.0F, 0.0F, 0.5236F);
        outer_roof_3_r5.setTextureOffset(54, 115).addCuboid(-2.5F, -1.0F, -10.0F, 4.0F, 1.0F, 20.0F, 0.0F, true);

        outer_roof_2_r5 = new ModelPart(this);
        outer_roof_2_r5.setPivot(16.6984F, -37.2079F, -8.0F);
        outer_roof_4.addChild(outer_roof_2_r5);
        setRotationAngle(outer_roof_2_r5, 0.0F, 0.0F, 1.0472F);
        outer_roof_2_r5.setTextureOffset(124, 178).addCuboid(-1.0F, -1.0F, -10.0F, 3.0F, 1.0F, 20.0F, 0.0F, true);

        head = new ModelPart(this);
        head.setPivot(0.0F, 24.0F, 0.0F);
        head.setTextureOffset(216, 41).addCuboid(-20.0F, 0.0F, 6.0F, 40.0F, 1.0F, 2.0F, 0.0F, false);
        head.setTextureOffset(108, 91).addCuboid(17.0F, -14.0F, 6.0F, 3.0F, 14.0F, 5.0F, 0.0F, false);
        head.setTextureOffset(135, 91).addCuboid(-20.0F, -14.0F, 6.0F, 3.0F, 14.0F, 5.0F, 0.0F, false);
        head.setTextureOffset(128, 0).addCuboid(-18.5F, -35.0F, 6.0F, 37.0F, 35.0F, 0.0F, 0.0F, false);

        driver_wall_2_r1 = new ModelPart(this);
        driver_wall_2_r1.setPivot(0.0F, -35.5F, 6.0F);
        head.addChild(driver_wall_2_r1);
        setRotationAngle(driver_wall_2_r1, 0.0F, 0.0F, -3.1416F);
        driver_wall_2_r1.setTextureOffset(128, 0).addCuboid(-18.5F, -0.5F, 0.0F, 37.0F, 1.0F, 0.0F, 0.0F, false);

        upper_wall_2_r4 = new ModelPart(this);
        upper_wall_2_r4.setPivot(-20.1988F, -14.0221F, 0.0F);
        head.addChild(upper_wall_2_r4);
        setRotationAngle(upper_wall_2_r4, 0.0F, 0.0F, 0.1107F);
        upper_wall_2_r4.setTextureOffset(0, 223).addCuboid(0.2F, -19.0F, 6.0F, 3.0F, 19.0F, 5.0F, 0.0F, false);

        upper_wall_1_r4 = new ModelPart(this);
        upper_wall_1_r4.setPivot(20.1988F, -14.0221F, 0.0F);
        head.addChild(upper_wall_1_r4);
        setRotationAngle(upper_wall_1_r4, 0.0F, 0.0F, -0.1107F);
        upper_wall_1_r4.setTextureOffset(94, 207).addCuboid(-3.2F, -19.0F, 6.0F, 3.0F, 19.0F, 5.0F, 0.0F, false);

        head_exterior = new ModelPart(this);
        head_exterior.setPivot(0.0F, 24.0F, 0.0F);
        head_exterior.setTextureOffset(218, 79).addCuboid(19.0F, -14.0F, -21.0F, 1.0F, 14.0F, 32.0F, 0.0F, false);
        head_exterior.setTextureOffset(218, 79).addCuboid(-20.0F, -14.0F, -21.0F, 1.0F, 14.0F, 32.0F, 0.0F, true);
        head_exterior.setTextureOffset(0, 0).addCuboid(-20.0F, 0.0F, -33.0F, 40.0F, 4.0F, 36.0F, 0.0F, false);
        head_exterior.setTextureOffset(174, 157).addCuboid(20.0F, 0.0F, -6.0F, 1.0F, 4.0F, 15.0F, 0.0F, false);
        head_exterior.setTextureOffset(174, 157).addCuboid(-21.0F, 0.0F, -6.0F, 1.0F, 4.0F, 15.0F, 0.0F, true);
        head_exterior.setTextureOffset(218, 125).addCuboid(-20.0F, 0.0F, 3.0F, 40.0F, 1.0F, 3.0F, 0.0F, false);
        head_exterior.setTextureOffset(216, 0).addCuboid(-18.5F, -41.0F, 6.0F, 37.0F, 41.0F, 0.0F, 0.0F, false);

        upper_wall_2_r5 = new ModelPart(this);
        upper_wall_2_r5.setPivot(-20.0F, -14.0F, -15.0F);
        head_exterior.addChild(upper_wall_2_r5);
        setRotationAngle(upper_wall_2_r5, 0.0F, 0.0F, 0.1107F);
        upper_wall_2_r5.setTextureOffset(134, 211).addCuboid(0.0F, -21.0F, -6.0F, 1.0F, 21.0F, 32.0F, 0.0F, true);

        upper_wall_1_r5 = new ModelPart(this);
        upper_wall_1_r5.setPivot(20.0F, -14.0F, 0.0F);
        head_exterior.addChild(upper_wall_1_r5);
        setRotationAngle(upper_wall_1_r5, 0.0F, 0.0F, -0.1107F);
        upper_wall_1_r5.setTextureOffset(134, 211).addCuboid(-1.0F, -21.0F, -21.0F, 1.0F, 21.0F, 32.0F, 0.0F, false);

        front = new ModelPart(this);
        front.setPivot(0.0F, 0.0F, 0.0F);
        head_exterior.addChild(front);


        side_1 = new ModelPart(this);
        side_1.setPivot(0.0F, 0.0F, 0.0F);
        front.addChild(side_1);


        outer_roof_5_r1 = new ModelPart(this);
        outer_roof_5_r1.setPivot(3.2219F, -40.7466F, -18.8935F);
        side_1.addChild(outer_roof_5_r1);
        setRotationAngle(outer_roof_5_r1, 0.1745F, 0.0F, 0.0F);
        outer_roof_5_r1.setTextureOffset(8, 54).addCuboid(-3.5F, 0.0F, -7.0F, 6.0F, 0.0F, 14.0F, 0.0F, false);

        outer_roof_4_r6 = new ModelPart(this);
        outer_roof_4_r6.setPivot(10.4347F, -39.8969F, -18.8935F);
        side_1.addChild(outer_roof_4_r6);
        setRotationAngle(outer_roof_4_r6, 0.1745F, 0.0F, 0.1745F);
        outer_roof_4_r6.setTextureOffset(2, 0).addCuboid(-5.0F, 0.0F, -7.0F, 10.0F, 0.0F, 14.0F, 0.0F, false);

        outer_roof_3_r6 = new ModelPart(this);
        outer_roof_3_r6.setPivot(14.4421F, -39.0318F, -18.94F);
        side_1.addChild(outer_roof_3_r6);
        setRotationAngle(outer_roof_3_r6, 0.1309F, 0.0F, 0.5236F);
        outer_roof_3_r6.setTextureOffset(14, 40).addCuboid(-1.5F, 0.0F, -7.0F, 4.0F, 0.0F, 14.0F, 0.0F, false);

        outer_roof_2_r6 = new ModelPart(this);
        outer_roof_2_r6.setPivot(17.7118F, -37.2157F, -12.993F);
        side_1.addChild(outer_roof_2_r6);
        setRotationAngle(outer_roof_2_r6, 0.1178F, 0.0F, 1.0472F);
        outer_roof_2_r6.setTextureOffset(16, 14).addCuboid(-1.5F, 0.0F, -13.0F, 3.0F, 0.0F, 14.0F, 0.0F, false);

        front_side_bottom_2_r1 = new ModelPart(this);
        front_side_bottom_2_r1.setPivot(23.7436F, -2.2438F, -20.0F);
        side_1.addChild(front_side_bottom_2_r1);
        setRotationAngle(front_side_bottom_2_r1, 0.0F, 0.0F, 0.2618F);
        front_side_bottom_2_r1.setTextureOffset(0, 263).addCuboid(-2.0F, 7.0F, -1.0F, 0.0F, 3.0F, 26.0F, 0.0F, true);

        front_side_bottom_1_r1 = new ModelPart(this);
        front_side_bottom_1_r1.setPivot(21.153F, 0.1678F, -20.0075F);
        side_1.addChild(front_side_bottom_1_r1);
        setRotationAngle(front_side_bottom_1_r1, 0.0F, 0.1222F, 0.2618F);
        front_side_bottom_1_r1.setTextureOffset(0, 23).addCuboid(0.0F, 4.0F, -11.0F, 0.0F, 3.0F, 10.0F, 0.0F, false);

        front_side_upper_1_r1 = new ModelPart(this);
        front_side_upper_1_r1.setPivot(20.077F, -14.0086F, -20.003F);
        side_1.addChild(front_side_upper_1_r1);
        setRotationAngle(front_side_upper_1_r1, 0.0F, 0.1222F, -0.1107F);
        front_side_upper_1_r1.setTextureOffset(0, 44).addCuboid(0.0446F, -22.0F, -10.999F, 0.0F, 22.0F, 10.0F, 0.0F, true);

        front_side_lower_1_r1 = new ModelPart(this);
        front_side_lower_1_r1.setPivot(20.9925F, 0.0F, -21.1219F);
        side_1.addChild(front_side_lower_1_r1);
        setRotationAngle(front_side_lower_1_r1, 0.0F, 0.1222F, 0.0F);
        front_side_lower_1_r1.setTextureOffset(0, 98).addCuboid(-1.0F, -14.0F, -10.0F, 0.0F, 18.0F, 10.0F, 0.0F, true);

        side_2 = new ModelPart(this);
        side_2.setPivot(0.0F, 0.0F, 0.0F);
        front.addChild(side_2);


        outer_roof_5_r2 = new ModelPart(this);
        outer_roof_5_r2.setPivot(-3.2219F, -40.7466F, -18.8935F);
        side_2.addChild(outer_roof_5_r2);
        setRotationAngle(outer_roof_5_r2, 0.1745F, 0.0F, 0.0F);
        outer_roof_5_r2.setTextureOffset(8, 54).addCuboid(-2.5F, 0.0F, -7.0F, 6.0F, 0.0F, 14.0F, 0.0F, true);

        outer_roof_4_r7 = new ModelPart(this);
        outer_roof_4_r7.setPivot(-10.4347F, -39.8969F, -18.8935F);
        side_2.addChild(outer_roof_4_r7);
        setRotationAngle(outer_roof_4_r7, 0.1745F, 0.0F, -0.1745F);
        outer_roof_4_r7.setTextureOffset(2, 0).addCuboid(-5.0F, 0.0F, -7.0F, 10.0F, 0.0F, 14.0F, 0.0F, true);

        outer_roof_3_r7 = new ModelPart(this);
        outer_roof_3_r7.setPivot(-13.0465F, -36.6147F, -9.4949F);
        side_2.addChild(outer_roof_3_r7);
        setRotationAngle(outer_roof_3_r7, 0.1309F, 0.0F, -0.5236F);
        outer_roof_3_r7.setTextureOffset(14, 40).addCuboid(-2.5F, -4.0F, -16.0F, 4.0F, 0.0F, 14.0F, 0.0F, true);

        outer_roof_2_r7 = new ModelPart(this);
        outer_roof_2_r7.setPivot(-18.4243F, -37.627F, -6.0415F);
        side_2.addChild(outer_roof_2_r7);
        setRotationAngle(outer_roof_2_r7, 0.1178F, 0.0F, -1.0472F);
        outer_roof_2_r7.setTextureOffset(16, 14).addCuboid(-1.5F, 0.0F, -20.0F, 3.0F, 0.0F, 14.0F, 0.0F, true);

        front_side_bottom_4_r1 = new ModelPart(this);
        front_side_bottom_4_r1.setPivot(-19.8799F, -3.2792F, -20.0F);
        side_2.addChild(front_side_bottom_4_r1);
        setRotationAngle(front_side_bottom_4_r1, 0.0F, 0.0F, -0.2618F);
        front_side_bottom_4_r1.setTextureOffset(0, 263).addCuboid(-2.0F, 7.0F, -1.0F, 0.0F, 3.0F, 26.0F, 0.0F, false);

        front_side_bottom_3_r1 = new ModelPart(this);
        front_side_bottom_3_r1.setPivot(-19.8589F, 4.9974F, -20.0073F);
        side_2.addChild(front_side_bottom_3_r1);
        setRotationAngle(front_side_bottom_3_r1, 0.0F, -0.1222F, -0.2618F);
        front_side_bottom_3_r1.setTextureOffset(0, 23).addCuboid(0.0F, -1.0F, -11.0F, 0.0F, 3.0F, 10.0F, 0.0F, true);

        front_side_upper_2_r1 = new ModelPart(this);
        front_side_upper_2_r1.setPivot(-19.922F, -13.9913F, -21.9969F);
        side_2.addChild(front_side_upper_2_r1);
        setRotationAngle(front_side_upper_2_r1, 0.0F, -0.1222F, 0.1107F);
        front_side_upper_2_r1.setTextureOffset(0, 44).addCuboid(0.0446F, -22.0F, -9.001F, 0.0F, 22.0F, 10.0F, 0.0F, false);

        front_side_lower_2_r1 = new ModelPart(this);
        front_side_lower_2_r1.setPivot(-19.0065F, 0.0F, -20.878F);
        side_2.addChild(front_side_lower_2_r1);
        setRotationAngle(front_side_lower_2_r1, 0.0F, -0.1222F, 0.0F);
        front_side_lower_2_r1.setTextureOffset(0, 98).addCuboid(-1.0F, -14.0F, -10.0F, 0.0F, 18.0F, 10.0F, 0.0F, false);

        front_panel = new ModelPart(this);
        front_panel.setPivot(0.0F, 0.0F, 2.75F);
        front.addChild(front_panel);


        panel_12_r1 = new ModelPart(this);
        panel_12_r1.setPivot(-17.0573F, 8.3006F, -43.2568F);
        front_panel.addChild(panel_12_r1);
        setRotationAngle(panel_12_r1, -0.2618F, 0.0F, 0.0F);
        panel_12_r1.setTextureOffset(174, 188).addCuboid(9.5F, -50.5F, 2.0F, 15.0F, 17.0F, 0.0F, 0.0F, false);

        panel_11_r1 = new ModelPart(this);
        panel_11_r1.setPivot(-29.1968F, 11.1984F, -41.2713F);
        front_panel.addChild(panel_11_r1);
        setRotationAngle(panel_11_r1, -0.2618F, 0.1309F, 0.0F);
        panel_11_r1.setTextureOffset(218, 86).addCuboid(7.5F, -53.5F, 2.0F, 13.0F, 17.0F, 0.0F, 0.0F, false);

        panel_10_r1 = new ModelPart(this);
        panel_10_r1.setPivot(-14.609F, 9.2665F, -46.5012F);
        front_panel.addChild(panel_10_r1);
        setRotationAngle(panel_10_r1, -0.2618F, -0.1309F, 0.0F);
        panel_10_r1.setTextureOffset(236, 221).addCuboid(23.5F, -51.5F, 2.0F, 13.0F, 17.0F, 0.0F, 0.0F, false);

        panel_9_r1 = new ModelPart(this);
        panel_9_r1.setPivot(-15.3333F, 1.4805F, -40.9989F);
        front_panel.addChild(panel_9_r1);
        setRotationAngle(panel_9_r1, -0.1309F, -0.1309F, 0.0F);
        panel_9_r1.setTextureOffset(94, 232).addCuboid(23.5F, -25.5F, 2.0F, 13.0F, 10.0F, 0.0F, 0.0F, false);

        panel_8_r1 = new ModelPart(this);
        panel_8_r1.setPivot(-24.4901F, 4.4548F, -36.1661F);
        front_panel.addChild(panel_8_r1);
        setRotationAngle(panel_8_r1, -0.1309F, 0.1309F, 0.0F);
        panel_8_r1.setTextureOffset(120, 232).addCuboid(3.5F, -28.5F, 2.0F, 13.0F, 10.0F, 0.0F, 0.0F, false);

        panel_7_r1 = new ModelPart(this);
        panel_7_r1.setPivot(-17.0573F, -0.5024F, -37.7047F);
        front_panel.addChild(panel_7_r1);
        setRotationAngle(panel_7_r1, -0.1309F, 0.0F, 0.0F);
        panel_7_r1.setTextureOffset(16, 223).addCuboid(9.5F, -23.5F, 2.0F, 15.0F, 10.0F, 0.0F, 0.0F, false);

        panel_6_r1 = new ModelPart(this);
        panel_6_r1.setPivot(-15.5952F, -0.1257F, -39.01F);
        front_panel.addChild(panel_6_r1);
        setRotationAngle(panel_6_r1, 0.0F, -0.1309F, 0.0F);
        panel_6_r1.setTextureOffset(252, 100).addCuboid(23.5F, -13.5F, 2.0F, 13.0F, 10.0F, 0.0F, 0.0F, false);

        panel_5_r1 = new ModelPart(this);
        panel_5_r1.setPivot(-26.1601F, -0.1257F, -33.5279F);
        front_panel.addChild(panel_5_r1);
        setRotationAngle(panel_5_r1, 0.0F, 0.1309F, 0.0F);
        panel_5_r1.setTextureOffset(256, 258).addCuboid(5.5F, -13.5F, 2.0F, 13.0F, 10.0F, 0.0F, 0.0F, false);

        panel_4_r1 = new ModelPart(this);
        panel_4_r1.setPivot(-17.0573F, -0.1257F, -35.9598F);
        front_panel.addChild(panel_4_r1);
        setRotationAngle(panel_4_r1, 0.0F, 0.0F, 0.0F);
        panel_4_r1.setTextureOffset(160, 124).addCuboid(9.5F, -13.5F, 2.0F, 15.0F, 10.0F, 0.0F, 0.0F, false);

        panel_3_r1 = new ModelPart(this);
        panel_3_r1.setPivot(-17.0573F, 0.2729F, -34.9867F);
        front_panel.addChild(panel_3_r1);
        setRotationAngle(panel_3_r1, 0.2618F, 0.0F, 0.0F);
        panel_3_r1.setTextureOffset(0, 130).addCuboid(9.5F, -3.5F, 2.0F, 15.0F, 11.0F, 0.0F, 0.0F, false);

        panel_2_r1 = new ModelPart(this);
        panel_2_r1.setPivot(-15.127F, 0.2727F, -33.998F);
        front_panel.addChild(panel_2_r1);
        setRotationAngle(panel_2_r1, 0.2618F, 0.1309F, 0.0F);
        panel_2_r1.setTextureOffset(176, 91).addCuboid(-5.5F, -3.5F, 2.0F, 13.0F, 11.0F, 0.0F, 0.0F, false);

        panel_1_r1 = new ModelPart(this);
        panel_1_r1.setPivot(16.0039F, 0.2729F, -33.8685F);
        front_panel.addChild(panel_1_r1);
        setRotationAngle(panel_1_r1, 0.2618F, -0.1309F, 0.0F);
        panel_1_r1.setTextureOffset(191, 157).addCuboid(-8.5F, -3.5F, 2.0F, 13.0F, 11.0F, 0.0F, 0.0F, false);

        headlights = new ModelPart(this);
        headlights.setPivot(0.0F, 24.0F, 0.0F);
        setRotationAngle(headlights, -0.0175F, 0.0F, 0.0F);


        headlight_2_r1 = new ModelPart(this);
        headlight_2_r1.setPivot(-9.5402F, -8.0789F, -31.1818F);
        headlights.addChild(headlight_2_r1);
        setRotationAngle(headlight_2_r1, 0.0F, 0.1309F, 0.0F);
        headlight_2_r1.setTextureOffset(0, 5).addCuboid(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 0.0F, 0.0F, true);

        headlight_1_r1 = new ModelPart(this);
        headlight_1_r1.setPivot(9.4256F, -8.0789F, -31.1818F);
        headlights.addChild(headlight_1_r1);
        setRotationAngle(headlight_1_r1, 0.0F, -0.1309F, 0.0F);
        headlight_1_r1.setTextureOffset(0, 5).addCuboid(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

        tail_lights = new ModelPart(this);
        tail_lights.setPivot(0.0F, 24.0F, 0.0F);
        setRotationAngle(tail_lights, -0.0175F, 0.0F, 0.0F);


        tail_light_2_r1 = new ModelPart(this);
        tail_light_2_r1.setPivot(-11.0273F, -10.5789F, -30.986F);
        tail_lights.addChild(tail_light_2_r1);
        setRotationAngle(tail_light_2_r1, 0.0F, 0.1309F, 0.0F);
        tail_light_2_r1.setTextureOffset(0, 0).addCuboid(-3.5F, -0.5F, 0.0F, 7.0F, 5.0F, 0.0F, 0.0F, true);

        tail_light_1_r1 = new ModelPart(this);
        tail_light_1_r1.setPivot(10.9128F, -10.5789F, -30.986F);
        tail_lights.addChild(tail_light_1_r1);
        setRotationAngle(tail_light_1_r1, 0.0F, -0.1309F, 0.0F);
        tail_light_1_r1.setTextureOffset(0, 0).addCuboid(-3.5F, -0.5F, 0.0F, 7.0F, 5.0F, 0.0F, 0.0F, false);

        door_light_on = new ModelPart(this);
        door_light_on.setPivot(0.0F, 24.0F, 0.0F);


        light_r1 = new ModelPart(this);
        light_r1.setPivot(-20.0F, -14.0F, 0.0F);
        door_light_on.addChild(light_r1);
        setRotationAngle(light_r1, 0.0F, 0.0F, 0.1107F);
        light_r1.setTextureOffset(32, 319).addCuboid(-1.0F, -19.5F, 0.0F, 0.0F, 0.0F, 0.0F, 0.4F, false);

        door_light_off = new ModelPart(this);
        door_light_off.setPivot(0.0F, 24.0F, 0.0F);


        light_r2 = new ModelPart(this);
        light_r2.setPivot(-20.0F, -14.0F, 0.0F);
        door_light_off.addChild(light_r2);
        setRotationAngle(light_r2, 0.0F, 0.0F, 0.1107F);
        light_r2.setTextureOffset(30, 319).addCuboid(-1.0F, -19.5F, 0.0F, 0.0F, 0.0F, 0.0F, 0.4F, false);
    }

    private static final int DOOR_MAX = 13;
    private static final ModelDoorOverlay MODEL_DOOR_OVERLAY = new ModelDoorOverlay(DOOR_MAX, 6.34F, "door_overlay_c_train_left.png", "door_overlay_c_train_right.png");

    @Override
    protected void renderWindowPositions(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
        switch (renderStage) {
            case LIGHTS:
                renderMirror(roof_light, matrices, vertices, light, position);
                break;
            case INTERIOR:
                renderMirror(window, matrices, vertices, light, position);
                if (renderDetails) {
                    renderMirror(window_handrails, matrices, vertices, light, position);
                    renderMirror(roof_window, matrices, vertices, light, position);
                    renderMirror(side_panel, matrices, vertices, light, position - 22F);
                    renderMirror(side_panel, matrices, vertices, light, position + 22F);
                }
                break;
            case INTERIOR_TRANSLUCENT:
                renderMirror(side_panel_translucent, matrices, vertices, light, position - 22F);
                renderMirror(side_panel_translucent, matrices, vertices, light, position + 22F);
                break;
            case EXTERIOR:
                renderMirror(window_exterior, matrices, vertices, light, position);
                renderMirror(roof_exterior, matrices, vertices, light, position);
                break;
        }
    }

    @Override
    protected void renderDoorPositions(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
        final boolean middleDoor = isIndex(getDoorPositions().length / 2, position, getDoorPositions());
        final boolean doorOpen = doorLeftZ > 0 || doorRightZ > 0;
        final boolean notLastDoor = !isIndex(0, position, getDoorPositions()) && !isIndex(-1, position, getDoorPositions());

        switch (renderStage) {
            case LIGHTS:
                if (notLastDoor) {
                    renderMirror(roof_light, matrices, vertices, light, position);
                }
                if (middleDoor && doorOpen && renderDetails) {
                    renderMirror(door_light_on, matrices, vertices, light, position - 40);
                }
                break;
            case INTERIOR:
                door_left.setPivot(0, 0, doorRightZ);
                door_right.setPivot(0, 0, -doorRightZ);
                renderOnce(door, matrices, vertices, light, position);
                door_left.setPivot(0, 0, doorLeftZ);
                door_right.setPivot(0, 0, -doorLeftZ);
                renderOnceFlipped(door, matrices, vertices, light, position);

                if (renderDetails) {
                    renderOnce(door_handrail, matrices, vertices, light, position);
                    renderMirror(roof_door, matrices, vertices, light, position);
                }
                break;
            case EXTERIOR:
                door_left_exterior.setPivot(0, 0, doorRightZ);
                door_right_exterior.setPivot(0, 0, -doorRightZ);
                renderOnce(door_exterior, matrices, vertices, light, position);
                door_left_exterior.setPivot(0, 0, doorLeftZ);
                door_right_exterior.setPivot(0, 0, -doorLeftZ);
                renderOnceFlipped(door_exterior, matrices, vertices, light, position);
                renderMirror(roof_exterior, matrices, vertices, light, position);
                if (middleDoor && renderDetails) {
                    if (!doorOpen) {
                        renderMirror(door_light_off, matrices, vertices, light, position - 40);
                    }
                }
                break;
        }
    }
    @Override
    protected void renderHeadPosition1(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean useHeadlights) {
        switch (renderStage) {
            case LIGHTS:
                renderOnce(roof_end_light, matrices, vertices, light, position);
                break;
            case ALWAYS_ON_LIGHTS:
                renderOnce(useHeadlights ? headlights : tail_lights, matrices, vertices, light, position);
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
    protected void renderHeadPosition2(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean useHeadlights) {
        switch (renderStage) {
            case LIGHTS:
                renderOnceFlipped(roof_end_light, matrices, vertices, light, position);
                break;
            case ALWAYS_ON_LIGHTS:
                renderOnceFlipped(useHeadlights ? headlights : tail_lights, matrices, vertices, light, position);
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
    protected void renderEndPosition1(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
        switch (renderStage) {
            case LIGHTS:
                renderOnce(roof_end_light, matrices, vertices, light, position);
                break;
            case INTERIOR:
                renderOnce(end, matrices, vertices, light, position);
                break;
            case EXTERIOR:
                renderOnce(end_exterior, matrices, vertices, light, position);
                renderOnce(roof_end_exterior, matrices, vertices, light, position);
                break;
        }
    }

    @Override
    protected void renderEndPosition2(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
        switch (renderStage) {
            case LIGHTS:
                renderOnceFlipped(roof_end_light, matrices, vertices, light, position);
                break;
            case INTERIOR:
                renderOnceFlipped(end, matrices, vertices, light, position);
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
    protected float getDoorAnimationX(float value, boolean opening) {
        return 0;
    }

    @Override
    protected float getDoorAnimationZ(float value, boolean opening) {
        return smoothEnds(0, DOOR_MAX, 0, 0.5F, value);
    }
}