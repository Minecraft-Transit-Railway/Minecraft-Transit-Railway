package mtr.model;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class ModelGZMTRA1 extends ModelTrainBase {
    private final ModelPart window;
    private final ModelPart upper_wall_r1;
    private final ModelPart window_handrails;
    private final ModelPart handrail_8_r1;
    private final ModelPart handrail_6_r1;
    private final ModelPart seat;
    private final ModelPart seat_floor_r1;
    private final ModelPart seat_bottom_r1;
    private final ModelPart seat_back_r1;
    private final ModelPart window_exterior;
    private final ModelPart upper_wall_r2;
    private final ModelPart side_panel;
    private final ModelPart side_panel_6_r1;
    private final ModelPart side_panel_5_r1;
    private final ModelPart roof_window;
    private final ModelPart inner_roof_2_r1;
    private final ModelPart roof_door;
    private final ModelPart door_light_top_r1;
    private final ModelPart inner_roof_2_r2;
    private final ModelPart roof_exterior;
    private final ModelPart outer_roof_4_r1;
    private final ModelPart outer_roof_3_r1;
    private final ModelPart outer_roof_2_r1;
    private final ModelPart outer_roof_1_r1;
    private final ModelPart door;
    private final ModelPart door_right;
    private final ModelPart door_right_bottom_r1;
    private final ModelPart door_right_top_r1;
    private final ModelPart door_left;
    private final ModelPart door_left_bottom_r1;
    private final ModelPart door_left_top_r1;
    private final ModelPart door_handrail;
    private final ModelPart door_exterior;
    private final ModelPart upper_wall_r3;
    private final ModelPart door_left_exterior;
    private final ModelPart door_left_bottom_r2;
    private final ModelPart door_left_top_r2;
    private final ModelPart door_right_exterior;
    private final ModelPart door_right_bottom_r2;
    private final ModelPart door_right_top_r2;
    private final ModelPart end;
    private final ModelPart upper_wall_2_r1;
    private final ModelPart upper_wall_1_r1;
    private final ModelPart end_exterior;
    private final ModelPart upper_wall_2_r2;
    private final ModelPart upper_wall_1_r2;
    private final ModelPart roof_end;
    private final ModelPart inner_roof_5_r1;
    private final ModelPart inner_roof_2_r3;
    private final ModelPart roof_end_exterior;
    private final ModelPart outer_roof_9_r1;
    private final ModelPart outer_roof_8_r1;
    private final ModelPart outer_roof_7_r1;
    private final ModelPart outer_roof_6_r1;
    private final ModelPart outer_roof_4_r2;
    private final ModelPart outer_roof_3_r2;
    private final ModelPart outer_roof_2_r2;
    private final ModelPart outer_roof_1_r2;
    private final ModelPart roof_door_light;
    private final ModelPart roof_door_light_r1;
    private final ModelPart roof_end_light;
    private final ModelPart roof_end_light_2_r1;
    private final ModelPart roof_end_light_1_r1;
    private final ModelPart head;
    private final ModelPart handrail_12_r1;
    private final ModelPart upper_wall_2_r3;
    private final ModelPart upper_wall_1_r3;
    private final ModelPart head_exterior;
    private final ModelPart upper_wall_6_r1;
    private final ModelPart upper_wall_3_r1;
    private final ModelPart floor_3_r1;
    private final ModelPart font_panel_r1;
    private final ModelPart driver_door;
    private final ModelPart door_wall_4_r1;
    private final ModelPart door_wall_2_r1;
    private final ModelPart roof_1_r1;
    private final ModelPart roof_head;
    private final ModelPart inner_roof_5_r2;
    private final ModelPart inner_roof_2_r4;
    private final ModelPart roof_head_exterior;
    private final ModelPart outer_roof_9_r2;
    private final ModelPart outer_roof_8_r2;
    private final ModelPart outer_roof_7_r2;
    private final ModelPart outer_roof_6_r2;
    private final ModelPart outer_roof_4_r3;
    private final ModelPart outer_roof_3_r3;
    private final ModelPart outer_roof_2_r3;
    private final ModelPart outer_roof_1_r3;
    private final ModelPart roof_window_light;
    private final ModelPart roof_light_r1;
    private final ModelPart headlights;
    private final ModelPart headlight_4_r1;
    private final ModelPart tail_lights;
    private final ModelPart tail_lights_4_r1;
    private final ModelPart door_light_off;
    private final ModelPart door_light_bottom_r1;
    private final ModelPart door_light_on;
    private final ModelPart door_light_bottom_r2;
    private final ModelPart door_light_exterior_off;
    private final ModelPart door_light_exterior_on;
    public ModelGZMTRA1() {
        textureWidth = 320;
        textureHeight = 320;
        window = new ModelPart(this);
        window.setPivot(0.0F, 24.0F, 0.0F);
        window.setTextureOffset(0, 40).addCuboid(-20.0F, 0.0F, -24.0F, 20.0F, 1.0F, 48.0F, 0.0F, false);

        upper_wall_r1 = new ModelPart(this);
        upper_wall_r1.setPivot(0.0F, 0.0F, 0.0F);
        window.addChild(upper_wall_r1);
        setRotationAngle(upper_wall_r1, 0.0F, 0.0F, 0.0234F);
        upper_wall_r1.setTextureOffset(84, 40).addCuboid(-20.0F, -34.5163F, -26.0F, 2.0F, 21.0F, 52.0F, 0.0F, false);
        upper_wall_r1.setTextureOffset(54, 113).addCuboid(-20.0F, -13.5163F, -26.0F, 2.0F, 14.0F, 52.0F, 0.0F, false);

        window_handrails = new ModelPart(this);
        window_handrails.setPivot(0.0F, 24.0F, 0.0F);
        window_handrails.setTextureOffset(319, 0).addCuboid(-10.0F, -34.2F, -25.0F, 0.0F, 26.0F, 0.0F, 0.2F, false);
        window_handrails.setTextureOffset(319, 0).addCuboid(-10.0F, -34.2F, 25.0F, 0.0F, 26.0F, 0.0F, 0.2F, false);
        window_handrails.setTextureOffset(319, 0).addCuboid(0.0F, -34.0F, -13.0F, 0.0F, 34.0F, 0.0F, 0.2F, false);
        window_handrails.setTextureOffset(319, 0).addCuboid(0.0F, -34.0F, 13.0F, 0.0F, 34.0F, 0.0F, 0.2F, false);
        window_handrails.setTextureOffset(0, 0).addCuboid(-1.0F, -32.0F, 34.0F, 2.0F, 4.0F, 0.0F, 0.0F, false);
        window_handrails.setTextureOffset(0, 0).addCuboid(-1.0F, -32.0F, 26.0F, 2.0F, 4.0F, 0.0F, 0.0F, false);
        window_handrails.setTextureOffset(0, 0).addCuboid(-1.0F, -32.0F, 19.0F, 2.0F, 4.0F, 0.0F, 0.0F, false);
        window_handrails.setTextureOffset(0, 0).addCuboid(-1.0F, -32.0F, 7.0F, 2.0F, 4.0F, 0.0F, 0.0F, false);
        window_handrails.setTextureOffset(0, 0).addCuboid(-1.0F, -32.0F, 0.0F, 2.0F, 4.0F, 0.0F, 0.0F, false);
        window_handrails.setTextureOffset(0, 0).addCuboid(-1.0F, -32.0F, -7.0F, 2.0F, 4.0F, 0.0F, 0.0F, false);
        window_handrails.setTextureOffset(0, 0).addCuboid(-1.0F, -32.0F, -19.0F, 2.0F, 4.0F, 0.0F, 0.0F, false);
        window_handrails.setTextureOffset(0, 0).addCuboid(-1.0F, -32.0F, -26.0F, 2.0F, 4.0F, 0.0F, 0.0F, false);
        window_handrails.setTextureOffset(0, 0).addCuboid(-1.0F, -32.0F, -34.0F, 2.0F, 4.0F, 0.0F, 0.0F, false);

        handrail_8_r1 = new ModelPart(this);
        handrail_8_r1.setPivot(0.0F, 0.0F, 0.0F);
        window_handrails.addChild(handrail_8_r1);
        setRotationAngle(handrail_8_r1, -1.5708F, -1.5708F, 0.0F);
        handrail_8_r1.setTextureOffset(319, 0).addCuboid(25.0F, -18.0F, -16.5F, 0.0F, 8.0F, 0.0F, 0.2F, false);
        handrail_8_r1.setTextureOffset(319, 0).addCuboid(-25.0F, -18.0F, -16.5F, 0.0F, 8.0F, 0.0F, 0.2F, false);

        handrail_6_r1 = new ModelPart(this);
        handrail_6_r1.setPivot(0.0F, 0.0F, 0.0F);
        window_handrails.addChild(handrail_6_r1);
        setRotationAngle(handrail_6_r1, -1.5708F, 0.0F, 0.0F);
        handrail_6_r1.setTextureOffset(319, 0).addCuboid(-10.0F, -25.0F, -31.5F, 0.0F, 50.0F, 0.0F, 0.2F, false);
        handrail_6_r1.setTextureOffset(319, 0).addCuboid(0.0F, -40.0F, -31.5F, 0.0F, 80.0F, 0.0F, 0.2F, false);

        seat = new ModelPart(this);
        seat.setPivot(0.0F, 0.0F, 0.0F);
        window_handrails.addChild(seat);


        seat_floor_r1 = new ModelPart(this);
        seat_floor_r1.setPivot(-21.4619F, 16.6395F, -21.0F);
        seat.addChild(seat_floor_r1);
        setRotationAngle(seat_floor_r1, 0.0F, -1.5708F, -1.1345F);
        seat_floor_r1.setTextureOffset(20, 313).addCuboid(-3.5F, 0.0F, -24.5F, 49.0F, 0.0F, 7.0F, 0.0F, false);

        seat_bottom_r1 = new ModelPart(this);
        seat_bottom_r1.setPivot(-14.4684F, -5.7546F, 0.0F);
        seat.addChild(seat_bottom_r1);
        setRotationAngle(seat_bottom_r1, 0.0F, 0.0F, -0.0873F);
        seat_bottom_r1.setTextureOffset(140, 0).addCuboid(-3.5F, -0.5F, -25.0F, 7.0F, 1.0F, 50.0F, 0.0F, false);

        seat_back_r1 = new ModelPart(this);
        seat_back_r1.setPivot(-17.0F, -6.0F, 0.0F);
        seat.addChild(seat_back_r1);
        setRotationAngle(seat_back_r1, 0.0F, 0.0F, -0.0524F);
        seat_back_r1.setTextureOffset(142, 63).addCuboid(-1.0F, -8.0F, -25.0F, 1.0F, 8.0F, 50.0F, 0.0F, false);

        window_exterior = new ModelPart(this);
        window_exterior.setPivot(0.0F, 24.0F, 0.0F);
        window_exterior.setTextureOffset(0, 162).addCuboid(-21.0056F, -0.0075F, -24.0F, 1.0F, 4.0F, 48.0F, 0.0F, false);

        upper_wall_r2 = new ModelPart(this);
        upper_wall_r2.setPivot(0.0F, 0.0F, 0.0F);
        window_exterior.addChild(upper_wall_r2);
        setRotationAngle(upper_wall_r2, 0.0F, 0.0F, 0.0234F);
        upper_wall_r2.setTextureOffset(0, 89).addCuboid(-21.0F, -34.5163F, -26.0F, 1.0F, 21.0F, 52.0F, 0.0F, false);
        upper_wall_r2.setTextureOffset(110, 127).addCuboid(-21.0F, -13.5163F, -26.0F, 1.0F, 14.0F, 52.0F, 0.0F, false);

        side_panel = new ModelPart(this);
        side_panel.setPivot(0.0F, 24.0F, 0.0F);
        side_panel.setTextureOffset(299, 132).addCuboid(-18.5F, -17.9F, -0.5F, 9.0F, 18.0F, 1.0F, 0.0F, false);
        side_panel.setTextureOffset(314, 130).addCuboid(-11.5F, -8.15F, -0.5F, 2.0F, 0.0F, 1.0F, 0.0F, false);
        side_panel.setTextureOffset(1, 319).addCuboid(-11.5F, -4.9F, -0.5F, 2.0F, 0.0F, 1.0F, 0.0F, false);
        side_panel.setTextureOffset(1, 316).addCuboid(-18.0F, 0.1F, -0.5F, 5.0F, 0.0F, 1.0F, 0.0F, false);
        side_panel.setTextureOffset(316, 158).addCuboid(-18.1872F, -14.3382F, -0.5F, 1.0F, 0.0F, 1.0F, 0.0F, false);

        side_panel_6_r1 = new ModelPart(this);
        side_panel_6_r1.setPivot(-10.4704F, -8.3271F, 0.0F);
        side_panel.addChild(side_panel_6_r1);
        setRotationAngle(side_panel_6_r1, 0.0F, 0.0F, 0.7854F);
        side_panel_6_r1.setTextureOffset(1, 310).addCuboid(-9.25F, 0.5F, -0.5F, 9.0F, 0.0F, 1.0F, 0.0F, false);

        side_panel_5_r1 = new ModelPart(this);
        side_panel_5_r1.setPivot(-11.8614F, -0.3995F, 0.0F);
        side_panel.addChild(side_panel_5_r1);
        setRotationAngle(side_panel_5_r1, 0.0F, 0.0F, -1.1345F);
        side_panel_5_r1.setTextureOffset(1, 315).addCuboid(-1.5F, -1.5F, -0.5F, 6.0F, 0.0F, 1.0F, 0.0F, false);

        roof_window = new ModelPart(this);
        roof_window.setPivot(0.0F, 24.0F, 0.0F);
        roof_window.setTextureOffset(76, 40).addCuboid(-17.25F, -32.0F, -24.0F, 2.0F, 0.0F, 48.0F, 0.0F, false);
        roof_window.setTextureOffset(40, 40).addCuboid(-10.0F, -34.0F, -24.0F, 10.0F, 0.0F, 48.0F, 0.0F, false);

        inner_roof_2_r1 = new ModelPart(this);
        inner_roof_2_r1.setPivot(-16.1897F, -31.658F, 0.0F);
        roof_window.addChild(inner_roof_2_r1);
        setRotationAngle(inner_roof_2_r1, 0.0F, 0.0F, -0.3491F);
        inner_roof_2_r1.setTextureOffset(60, 40).addCuboid(1.0F, 0.0F, -24.0F, 6.0F, 0.0F, 48.0F, 0.0F, false);

        roof_door = new ModelPart(this);
        roof_door.setPivot(0.0F, 24.0F, 0.0F);
        roof_door.setTextureOffset(0, 40).addCuboid(-17.25F, -32.0F, -16.0F, 2.0F, 0.0F, 32.0F, 0.0F, false);
        roof_door.setTextureOffset(88, 0).addCuboid(-10.0F, -34.0F, -16.0F, 10.0F, 0.0F, 32.0F, 0.0F, false);
        roof_door.setTextureOffset(0, 0).addCuboid(-19.25F, -32.0F, -14.0F, 2.0F, 0.0F, 28.0F, 0.0F, false);

        door_light_top_r1 = new ModelPart(this);
        door_light_top_r1.setPivot(-8.1869F, -0.1919F, -0.5F);
        roof_door.addChild(door_light_top_r1);
        setRotationAngle(door_light_top_r1, 0.0F, 0.0F, -0.3491F);
        door_light_top_r1.setTextureOffset(0, 4).addCuboid(5.8931F, -33.024F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        inner_roof_2_r2 = new ModelPart(this);
        inner_roof_2_r2.setPivot(-16.1897F, -31.658F, 0.0F);
        roof_door.addChild(inner_roof_2_r2);
        setRotationAngle(inner_roof_2_r2, 0.0F, 0.0F, -0.3491F);
        inner_roof_2_r2.setTextureOffset(40, 89).addCuboid(1.0F, 0.0F, -16.0F, 6.0F, 0.0F, 32.0F, 0.0F, false);

        roof_exterior = new ModelPart(this);
        roof_exterior.setPivot(0.0F, 24.0F, 0.0F);
        roof_exterior.setTextureOffset(14, 89).addCuboid(-8.5628F, -41.6158F, -20.0F, 9.0F, 0.0F, 40.0F, 0.0F, false);

        outer_roof_4_r1 = new ModelPart(this);
        outer_roof_4_r1.setPivot(-19.0431F, -39.3671F, -0.25F);
        roof_exterior.addChild(outer_roof_4_r1);
        setRotationAngle(outer_roof_4_r1, 0.0F, 0.0F, -0.1745F);
        outer_roof_4_r1.setTextureOffset(100, 51).addCuboid(2.7115F, -0.3936F, -19.75F, 8.0F, 0.0F, 40.0F, 0.0F, false);

        outer_roof_3_r1 = new ModelPart(this);
        outer_roof_3_r1.setPivot(-31.8054F, -31.3539F, -0.25F);
        roof_exterior.addChild(outer_roof_3_r1);
        setRotationAngle(outer_roof_3_r1, 0.0F, 0.0F, -0.5236F);
        outer_roof_3_r1.setTextureOffset(0, 40).addCuboid(14.7417F, 0.0F, -19.75F, 3.0F, 0.0F, 40.0F, 0.0F, false);

        outer_roof_2_r1 = new ModelPart(this);
        outer_roof_2_r1.setPivot(-28.8094F, -21.8031F, -0.25F);
        roof_exterior.addChild(outer_roof_2_r1);
        setRotationAngle(outer_roof_2_r1, 0.0F, 0.0F, -1.0472F);
        outer_roof_2_r1.setTextureOffset(88, 40).addCuboid(17.5404F, 0.0F, -19.75F, 2.0F, 0.0F, 40.0F, 0.0F, false);

        outer_roof_1_r1 = new ModelPart(this);
        outer_roof_1_r1.setPivot(-40.2126F, -37.3454F, -0.25F);
        roof_exterior.addChild(outer_roof_1_r1);
        setRotationAngle(outer_roof_1_r1, 0.0F, 0.0F, 0.067F);
        outer_roof_1_r1.setTextureOffset(156, 11).addCuboid(20.1517F, -1.0F, -19.75F, 0.0F, 2.0F, 40.0F, 0.0F, false);

        door = new ModelPart(this);
        door.setPivot(0.0F, 24.0F, 0.0F);
        door.setTextureOffset(164, 142).addCuboid(-20.0F, 0.0F, -16.0F, 20.0F, 1.0F, 32.0F, 0.0F, false);

        door_right = new ModelPart(this);
        door_right.setPivot(0.0F, 0.0F, 0.0F);
        door.addChild(door_right);


        door_right_bottom_r1 = new ModelPart(this);
        door_right_bottom_r1.setPivot(0.0F, 0.0F, 0.0F);
        door_right.addChild(door_right_bottom_r1);
        setRotationAngle(door_right_bottom_r1, 0.0F, 0.0F, 0.0234F);
        door_right_bottom_r1.setTextureOffset(164, 142).addCuboid(-20.0F, -13.5F, -15.0F, 1.0F, 14.0F, 15.0F, 0.0F, false);

        door_right_top_r1 = new ModelPart(this);
        door_right_top_r1.setPivot(-0.0004F, 0.0163F, 0.0F);
        door_right.addChild(door_right_top_r1);
        setRotationAngle(door_right_top_r1, 0.0F, 0.0F, 0.0234F);
        door_right_top_r1.setTextureOffset(128, 121).addCuboid(-20.0F, -31.5163F, -15.0F, 1.0F, 18.0F, 15.0F, 0.0F, false);

        door_left = new ModelPart(this);
        door_left.setPivot(0.0F, 0.0F, 0.0F);
        door.addChild(door_left);


        door_left_bottom_r1 = new ModelPart(this);
        door_left_bottom_r1.setPivot(0.0F, 0.0F, 0.0F);
        door_left.addChild(door_left_bottom_r1);
        setRotationAngle(door_left_bottom_r1, 0.0F, 0.0F, 0.0234F);
        door_left_bottom_r1.setTextureOffset(204, 0).addCuboid(-20.0F, -13.5F, 0.0F, 1.0F, 14.0F, 15.0F, 0.0F, false);

        door_left_top_r1 = new ModelPart(this);
        door_left_top_r1.setPivot(-0.0004F, 0.0163F, 0.0F);
        door_left.addChild(door_left_top_r1);
        setRotationAngle(door_left_top_r1, 0.0F, 0.0F, 0.0234F);
        door_left_top_r1.setTextureOffset(0, 245).addCuboid(-20.0F, -31.5163F, 0.0F, 1.0F, 18.0F, 15.0F, 0.0F, false);

        door_handrail = new ModelPart(this);
        door_handrail.setPivot(0.0F, 24.0F, 0.0F);
        door_handrail.setTextureOffset(319, 0).addCuboid(0.0F, -33.0F, 0.0F, 0.0F, 33.0F, 0.0F, 0.2F, false);

        door_exterior = new ModelPart(this);
        door_exterior.setPivot(0.0F, 24.0F, 0.0F);
        door_exterior.setTextureOffset(204, 0).addCuboid(-21.0056F, -0.0075F, -16.0F, 1.0F, 4.0F, 32.0F, 0.0F, false);

        upper_wall_r3 = new ModelPart(this);
        upper_wall_r3.setPivot(0.0F, 0.0F, 0.0F);
        door_exterior.addChild(upper_wall_r3);
        setRotationAngle(upper_wall_r3, 0.0F, 0.0F, 0.0234F);
        upper_wall_r3.setTextureOffset(0, 214).addCuboid(-21.0F, -34.5163F, -14.0F, 2.0F, 3.0F, 28.0F, 0.0F, false);

        door_left_exterior = new ModelPart(this);
        door_left_exterior.setPivot(0.0F, 0.0F, 0.0F);
        door_exterior.addChild(door_left_exterior);


        door_left_bottom_r2 = new ModelPart(this);
        door_left_bottom_r2.setPivot(0.0F, 0.0F, 0.0F);
        door_left_exterior.addChild(door_left_bottom_r2);
        setRotationAngle(door_left_bottom_r2, 0.0F, 0.0F, 0.0234F);
        door_left_bottom_r2.setTextureOffset(238, 0).addCuboid(-20.0F, -13.5F, 0.0F, 1.0F, 14.0F, 15.0F, 0.0F, false);

        door_left_top_r2 = new ModelPart(this);
        door_left_top_r2.setPivot(-0.0004F, 0.0163F, 0.0F);
        door_left_exterior.addChild(door_left_top_r2);
        setRotationAngle(door_left_top_r2, 0.0F, 0.0F, 0.0234F);
        door_left_top_r2.setTextureOffset(253, 160).addCuboid(-20.0F, -31.5163F, 0.0F, 1.0F, 18.0F, 15.0F, 0.0F, false);

        door_right_exterior = new ModelPart(this);
        door_right_exterior.setPivot(0.0F, 0.0F, 0.0F);
        door_exterior.addChild(door_right_exterior);


        door_right_bottom_r2 = new ModelPart(this);
        door_right_bottom_r2.setPivot(0.0F, 0.0F, 0.0F);
        door_right_exterior.addChild(door_right_bottom_r2);
        setRotationAngle(door_right_bottom_r2, 0.0F, 0.0F, 0.0234F);
        door_right_bottom_r2.setTextureOffset(236, 142).addCuboid(-20.0F, -13.5F, -15.0F, 1.0F, 14.0F, 15.0F, 0.0F, false);

        door_right_top_r2 = new ModelPart(this);
        door_right_top_r2.setPivot(-0.0004F, 0.0163F, 0.0F);
        door_right_exterior.addChild(door_right_top_r2);
        setRotationAngle(door_right_top_r2, 0.0F, 0.0F, 0.0234F);
        door_right_top_r2.setTextureOffset(32, 245).addCuboid(-20.0F, -31.5163F, -15.0F, 1.0F, 18.0F, 15.0F, 0.0F, false);

        end = new ModelPart(this);
        end.setPivot(0.0F, 24.0F, 0.0F);
        end.setTextureOffset(166, 123).addCuboid(-20.0F, 0.0F, -9.0F, 40.0F, 1.0F, 18.0F, 0.0F, false);
        end.setTextureOffset(253, 251).addCuboid(9.5F, -34.0F, -9.0F, 11.0F, 34.0F, 1.0F, 0.0F, false);
        end.setTextureOffset(130, 236).addCuboid(-20.5F, -34.0F, -9.0F, 11.0F, 34.0F, 1.0F, 0.0F, false);
        end.setTextureOffset(84, 89).addCuboid(-9.5F, -34.0F, -9.0F, 19.0F, 2.0F, 1.0F, 0.0F, false);
        end.setTextureOffset(318, 0).addCuboid(-18.0F, -24.0F, 9.0F, 1.0F, 0.0F, 0.0F, 0.25F, false);
        end.setTextureOffset(319, 0).addCuboid(-16.5F, -24.0F, 9.0F, 0.0F, 14.0F, 0.0F, 0.25F, false);
        end.setTextureOffset(318, 0).addCuboid(-18.0F, -10.0F, 9.0F, 1.0F, 0.0F, 0.0F, 0.25F, false);
        end.setTextureOffset(318, 0).addCuboid(16.5F, -24.0F, 9.0F, 1.0F, 0.0F, 0.0F, 0.25F, false);
        end.setTextureOffset(319, 0).addCuboid(16.5F, -24.0F, 9.0F, 0.0F, 14.0F, 0.0F, 0.25F, false);
        end.setTextureOffset(318, 0).addCuboid(17.0F, -10.0F, 9.0F, 1.0F, 0.0F, 0.0F, 0.25F, false);
        end.setTextureOffset(318, 0).addCuboid(-9.5F, -24.0F, -8.5F, 1.0F, 0.0F, 0.0F, 0.25F, false);
        end.setTextureOffset(319, 0).addCuboid(-8.5F, -24.0F, -8.5F, 0.0F, 14.0F, 0.0F, 0.25F, false);
        end.setTextureOffset(318, 0).addCuboid(-10.0F, -10.0F, -8.5F, 1.0F, 0.0F, 0.0F, 0.25F, false);
        end.setTextureOffset(318, 0).addCuboid(8.5F, -24.0F, -8.5F, 1.0F, 0.0F, 0.0F, 0.25F, false);
        end.setTextureOffset(319, 0).addCuboid(8.5F, -24.0F, -8.5F, 0.0F, 14.0F, 0.0F, 0.25F, false);
        end.setTextureOffset(318, 0).addCuboid(9.0F, -10.0F, -8.5F, 1.0F, 0.0F, 0.0F, 0.25F, false);

        upper_wall_2_r1 = new ModelPart(this);
        upper_wall_2_r1.setPivot(0.0F, 0.0F, 0.0F);
        end.addChild(upper_wall_2_r1);
        setRotationAngle(upper_wall_2_r1, 0.0F, 0.0F, 0.0234F);
        upper_wall_2_r1.setTextureOffset(167, 238).addCuboid(-20.0F, -34.5163F, -8.0F, 2.0F, 19.0F, 18.0F, 0.0F, true);
        upper_wall_2_r1.setTextureOffset(241, 217).addCuboid(-20.0F, -15.5163F, -8.0F, 2.0F, 16.0F, 18.0F, 0.0F, true);

        upper_wall_1_r1 = new ModelPart(this);
        upper_wall_1_r1.setPivot(0.0F, 0.0F, 0.0F);
        end.addChild(upper_wall_1_r1);
        setRotationAngle(upper_wall_1_r1, 0.0F, 0.0F, -0.0234F);
        upper_wall_1_r1.setTextureOffset(167, 238).addCuboid(17.9948F, -34.5084F, -8.0F, 2.0F, 19.0F, 18.0F, 0.0F, false);
        upper_wall_1_r1.setTextureOffset(241, 217).addCuboid(17.9948F, -15.5084F, -8.0F, 2.0F, 16.0F, 18.0F, 0.0F, false);

        end_exterior = new ModelPart(this);
        end_exterior.setPivot(0.0F, 24.0F, 0.0F);
        end_exterior.setTextureOffset(0, 60).addCuboid(20.0F, 0.0F, -9.0F, 1.0F, 4.0F, 20.0F, 0.0F, false);
        end_exterior.setTextureOffset(0, 60).addCuboid(-21.0056F, -0.0075F, -9.0F, 1.0F, 4.0F, 20.0F, 0.0F, true);
        end_exterior.setTextureOffset(270, 55).addCuboid(9.5F, -32.0F, -9.0F, 12.0F, 32.0F, 0.0F, 0.0F, false);
        end_exterior.setTextureOffset(270, 55).addCuboid(-21.5F, -32.0F, -9.0F, 12.0F, 32.0F, 0.0F, 0.0F, true);
        end_exterior.setTextureOffset(194, 100).addCuboid(-20.5F, -42.0F, -9.0F, 41.0F, 10.0F, 0.0F, 0.0F, false);

        upper_wall_2_r2 = new ModelPart(this);
        upper_wall_2_r2.setPivot(0.0F, 0.0F, 0.0F);
        end_exterior.addChild(upper_wall_2_r2);
        setRotationAngle(upper_wall_2_r2, 0.0F, 0.0F, 0.0234F);
        upper_wall_2_r2.setTextureOffset(3, 165).addCuboid(-21.0F, -34.5163F, -9.0F, 1.0F, 19.0F, 19.0F, 0.0F, false);
        upper_wall_2_r2.setTextureOffset(210, 238).addCuboid(-21.0F, -15.5163F, -9.0F, 1.0F, 16.0F, 19.0F, 0.0F, false);

        upper_wall_1_r2 = new ModelPart(this);
        upper_wall_1_r2.setPivot(0.0F, 0.0F, 0.0F);
        end_exterior.addChild(upper_wall_1_r2);
        setRotationAngle(upper_wall_1_r2, 0.0F, 0.0F, -0.0234F);
        upper_wall_1_r2.setTextureOffset(3, 165).addCuboid(19.9948F, -34.5084F, -9.0F, 1.0F, 19.0F, 19.0F, 0.0F, true);
        upper_wall_1_r2.setTextureOffset(210, 238).addCuboid(19.9948F, -15.5084F, -9.0F, 1.0F, 16.0F, 19.0F, 0.0F, true);

        roof_end = new ModelPart(this);
        roof_end.setPivot(0.0F, 24.0F, 0.0F);
        roof_end.setTextureOffset(2, 60).addCuboid(-17.25F, -32.0F, -12.0F, 2.0F, 0.0F, 20.0F, 0.0F, false);
        roof_end.setTextureOffset(64, 113).addCuboid(-10.0F, -34.0F, -12.0F, 10.0F, 0.0F, 20.0F, 0.0F, false);
        roof_end.setTextureOffset(64, 113).addCuboid(0.0F, -34.0F, -12.0F, 10.0F, 0.0F, 20.0F, 0.0F, true);
        roof_end.setTextureOffset(2, 60).addCuboid(15.25F, -32.0F, -12.0F, 2.0F, 0.0F, 20.0F, 0.0F, false);

        inner_roof_5_r1 = new ModelPart(this);
        inner_roof_5_r1.setPivot(8.6718F, -34.3942F, 4.0F);
        roof_end.addChild(inner_roof_5_r1);
        setRotationAngle(inner_roof_5_r1, 0.0F, 0.0F, 0.3491F);
        inner_roof_5_r1.setTextureOffset(0, 40).addCuboid(1.0F, 0.0F, -16.0F, 6.0F, 0.0F, 20.0F, 0.0F, true);

        inner_roof_2_r3 = new ModelPart(this);
        inner_roof_2_r3.setPivot(-16.1897F, -31.658F, 4.0F);
        roof_end.addChild(inner_roof_2_r3);
        setRotationAngle(inner_roof_2_r3, 0.0F, 0.0F, -0.3491F);
        inner_roof_2_r3.setTextureOffset(0, 40).addCuboid(1.0F, 0.0F, -16.0F, 6.0F, 0.0F, 20.0F, 0.0F, false);

        roof_end_exterior = new ModelPart(this);
        roof_end_exterior.setPivot(0.0F, 24.0F, 0.0F);
        roof_end_exterior.setTextureOffset(0, 89).addCuboid(-8.5766F, -41.6137F, -12.0F, 9.0F, 0.0F, 20.0F, 0.0F, false);
        roof_end_exterior.setTextureOffset(0, 89).addCuboid(-0.4274F, -41.6057F, -12.0F, 9.0F, 0.0F, 20.0F, 0.0F, true);

        outer_roof_9_r1 = new ModelPart(this);
        outer_roof_9_r1.setPivot(-9.5826F, 4.1054F, 10.0F);
        roof_end_exterior.addChild(outer_roof_9_r1);
        setRotationAngle(outer_roof_9_r1, 0.0F, 0.0F, 0.1745F);
        outer_roof_9_r1.setTextureOffset(0, 109).addCuboid(9.9416F, -48.1693F, -22.0F, 8.0F, 0.0F, 20.0F, 0.0F, false);

        outer_roof_8_r1 = new ModelPart(this);
        outer_roof_8_r1.setPivot(-0.9358F, -8.8074F, 10.0F);
        roof_end_exterior.addChild(outer_roof_8_r1);
        setRotationAngle(outer_roof_8_r1, 0.0F, 0.0F, 0.5236F);
        outer_roof_8_r1.setTextureOffset(0, 0).addCuboid(-0.6476F, -35.8944F, -22.0F, 3.0F, 0.0F, 20.0F, 0.0F, false);

        outer_roof_7_r1 = new ModelPart(this);
        outer_roof_7_r1.setPivot(5.8007F, -18.0562F, 10.0F);
        roof_end_exterior.addChild(outer_roof_7_r1);
        setRotationAngle(outer_roof_7_r1, 0.0F, 0.0F, 1.0472F);
        outer_roof_7_r1.setTextureOffset(6, 60).addCuboid(-11.269F, -21.8031F, -22.0F, 2.0F, 0.0F, 20.0F, 0.0F, false);

        outer_roof_6_r1 = new ModelPart(this);
        outer_roof_6_r1.setPivot(42.633F, -0.0697F, 10.0F);
        roof_end_exterior.addChild(outer_roof_6_r1);
        setRotationAngle(outer_roof_6_r1, 0.0F, 0.0F, -0.067F);
        outer_roof_6_r1.setTextureOffset(0, 64).addCuboid(-20.0609F, -38.3454F, -22.0F, 0.0F, 2.0F, 20.0F, 0.0F, false);

        outer_roof_4_r2 = new ModelPart(this);
        outer_roof_4_r2.setPivot(0.7638F, 1.0786F, 8.0F);
        roof_end_exterior.addChild(outer_roof_4_r2);
        setRotationAngle(outer_roof_4_r2, 0.0F, 0.0F, -0.1745F);
        outer_roof_4_r2.setTextureOffset(0, 109).addCuboid(-9.7849F, -43.6656F, -20.0F, 8.0F, 0.0F, 20.0F, 0.0F, false);

        outer_roof_3_r2 = new ModelPart(this);
        outer_roof_3_r2.setPivot(11.4019F, -20.1032F, 8.0F);
        roof_end_exterior.addChild(outer_roof_3_r2);
        setRotationAngle(outer_roof_3_r2, 0.0F, 0.0F, -0.5236F);
        outer_roof_3_r2.setTextureOffset(0, 0).addCuboid(-17.0637F, -31.3539F, -20.0F, 3.0F, 0.0F, 20.0F, 0.0F, false);

        outer_roof_2_r2 = new ModelPart(this);
        outer_roof_2_r2.setPivot(4.4643F, -35.8506F, 8.0F);
        roof_end_exterior.addChild(outer_roof_2_r2);
        setRotationAngle(outer_roof_2_r2, 0.0F, 0.0F, -1.0472F);
        outer_roof_2_r2.setTextureOffset(6, 60).addCuboid(-11.269F, -21.8031F, -20.0F, 2.0F, 0.0F, 20.0F, 0.0F, false);

        outer_roof_1_r2 = new ModelPart(this);
        outer_roof_1_r2.setPivot(-2.6043F, 2.6094F, 9.75F);
        roof_end_exterior.addChild(outer_roof_1_r2);
        setRotationAngle(outer_roof_1_r2, 0.0F, 0.0F, 0.067F);
        outer_roof_1_r2.setTextureOffset(0, 64).addCuboid(-20.0609F, -38.3454F, -21.75F, 0.0F, 2.0F, 20.0F, 0.0F, true);

        roof_door_light = new ModelPart(this);
        roof_door_light.setPivot(0.0F, 24.0F, 0.0F);


        roof_door_light_r1 = new ModelPart(this);
        roof_door_light_r1.setPivot(-11.4912F, -33.3681F, 8.0F);
        roof_door_light.addChild(roof_door_light_r1);
        setRotationAngle(roof_door_light_r1, 0.0F, 0.0F, -0.3491F);
        roof_door_light_r1.setTextureOffset(0, 0).addCuboid(-1.0F, 0.0F, -24.0F, 2.0F, 0.0F, 32.0F, 0.0F, false);

        roof_end_light = new ModelPart(this);
        roof_end_light.setPivot(0.0F, 24.0F, 0.0F);


        roof_end_light_2_r1 = new ModelPart(this);
        roof_end_light_2_r1.setPivot(-14.3103F, -32.342F, 4.0F);
        roof_end_light.addChild(roof_end_light_2_r1);
        setRotationAngle(roof_end_light_2_r1, 0.0F, 0.0F, -0.3491F);
        roof_end_light_2_r1.setTextureOffset(16, 40).addCuboid(2.0F, 0.0F, -16.0F, 2.0F, 0.0F, 20.0F, 0.0F, true);

        roof_end_light_1_r1 = new ModelPart(this);
        roof_end_light_1_r1.setPivot(8.6718F, -34.3942F, 4.0F);
        roof_end_light.addChild(roof_end_light_1_r1);
        setRotationAngle(roof_end_light_1_r1, 0.0F, 0.0F, 0.3491F);
        roof_end_light_1_r1.setTextureOffset(16, 40).addCuboid(2.0F, 0.0F, -16.0F, 2.0F, 0.0F, 20.0F, 0.0F, false);

        head = new ModelPart(this);
        head.setPivot(0.0F, 24.0F, 0.0F);
        head.setTextureOffset(194, 90).addCuboid(-20.0F, 0.0F, -1.0F, 40.0F, 1.0F, 9.0F, 0.0F, false);
        head.setTextureOffset(194, 55).addCuboid(-19.0F, -35.0F, -0.99F, 38.0F, 35.0F, 0.0F, 0.0F, false);
        head.setTextureOffset(318, 0).addCuboid(-18.0F, -24.0F, 9.0F, 1.0F, 0.0F, 0.0F, 0.25F, false);
        head.setTextureOffset(319, 0).addCuboid(-16.5F, -24.0F, 9.0F, 0.0F, 14.0F, 0.0F, 0.25F, false);
        head.setTextureOffset(318, 0).addCuboid(-18.0F, -10.0F, 9.0F, 1.0F, 0.0F, 0.0F, 0.25F, false);
        head.setTextureOffset(318, 0).addCuboid(16.5F, -24.0F, 9.0F, 1.0F, 0.0F, 0.0F, 0.25F, false);
        head.setTextureOffset(319, 0).addCuboid(16.5F, -24.0F, 9.0F, 0.0F, 14.0F, 0.0F, 0.25F, false);
        head.setTextureOffset(318, 0).addCuboid(17.0F, -10.0F, 9.0F, 1.0F, 0.0F, 0.0F, 0.25F, false);
        head.setTextureOffset(319, 0).addCuboid(7.0F, -24.0F, 0.25F, 0.0F, 14.0F, 0.0F, 0.25F, false);
        head.setTextureOffset(319, 0).addCuboid(-7.0F, -24.0F, 0.25F, 0.0F, 14.0F, 0.0F, 0.25F, false);

        handrail_12_r1 = new ModelPart(this);
        handrail_12_r1.setPivot(0.0F, 0.0F, 0.0F);
        head.addChild(handrail_12_r1);
        setRotationAngle(handrail_12_r1, 0.0F, 1.5708F, 0.0F);
        handrail_12_r1.setTextureOffset(318, 0).addCuboid(-0.25F, -10.0F, -7.0F, 1.0F, 0.0F, 0.0F, 0.25F, false);
        handrail_12_r1.setTextureOffset(318, 0).addCuboid(-0.25F, -24.0F, -7.0F, 1.0F, 0.0F, 0.0F, 0.25F, false);
        handrail_12_r1.setTextureOffset(318, 0).addCuboid(-0.25F, -10.0F, 7.0F, 1.0F, 0.0F, 0.0F, 0.25F, false);
        handrail_12_r1.setTextureOffset(318, 0).addCuboid(-0.25F, -24.0F, 7.0F, 1.0F, 0.0F, 0.0F, 0.25F, false);

        upper_wall_2_r3 = new ModelPart(this);
        upper_wall_2_r3.setPivot(0.0F, 0.0F, 0.0F);
        head.addChild(upper_wall_2_r3);
        setRotationAngle(upper_wall_2_r3, 0.0F, 0.0F, 0.0234F);
        upper_wall_2_r3.setTextureOffset(259, 25).addCuboid(-20.0F, -34.5163F, -1.0F, 2.0F, 19.0F, 11.0F, 0.0F, true);
        upper_wall_2_r3.setTextureOffset(0, 9).addCuboid(-20.0F, -15.5163F, -1.0F, 2.0F, 16.0F, 11.0F, 0.0F, true);

        upper_wall_1_r3 = new ModelPart(this);
        upper_wall_1_r3.setPivot(0.0F, 0.0F, 0.0F);
        head.addChild(upper_wall_1_r3);
        setRotationAngle(upper_wall_1_r3, 0.0F, 0.0F, -0.0234F);
        upper_wall_1_r3.setTextureOffset(259, 25).addCuboid(17.9948F, -34.5084F, -1.0F, 2.0F, 19.0F, 11.0F, 0.0F, false);
        upper_wall_1_r3.setTextureOffset(0, 9).addCuboid(17.9948F, -15.5084F, -1.0F, 2.0F, 16.0F, 11.0F, 0.0F, false);

        head_exterior = new ModelPart(this);
        head_exterior.setPivot(0.0F, 24.0F, 0.0F);
        head_exterior.setTextureOffset(0, 0).addCuboid(-21.0F, 0.0F, -37.0F, 42.0F, 4.0F, 36.0F, 0.0F, false);
        head_exterior.setTextureOffset(110, 152).addCuboid(20.0F, 0.0F, -1.0F, 1.0F, 4.0F, 9.0F, 0.0F, false);
        head_exterior.setTextureOffset(110, 152).addCuboid(-21.0056F, -0.0075F, -1.0F, 1.0F, 4.0F, 9.0F, 0.0F, true);
        head_exterior.setTextureOffset(224, 316).addCuboid(-21.0F, 2.0075F, -37.1212F, 42.0F, 0.0F, 3.0F, 0.0F, false);
        head_exterior.setTextureOffset(182, 193).addCuboid(-19.0F, -42.0F, -1.1F, 38.0F, 42.0F, 0.0F, 0.0F, false);

        upper_wall_6_r1 = new ModelPart(this);
        upper_wall_6_r1.setPivot(0.0F, 0.0F, 0.0F);
        head_exterior.addChild(upper_wall_6_r1);
        setRotationAngle(upper_wall_6_r1, 0.0F, 0.0F, 0.0234F);
        upper_wall_6_r1.setTextureOffset(0, 40).addCuboid(-21.0F, -34.5163F, -37.0F, 1.0F, 19.0F, 9.0F, 0.0F, true);
        upper_wall_6_r1.setTextureOffset(156, 55).addCuboid(-21.0F, -34.5163F, -16.0F, 3.0F, 19.0F, 15.0F, 0.0F, true);
        upper_wall_6_r1.setTextureOffset(265, 100).addCuboid(-21.0F, -34.5163F, -1.0F, 1.0F, 19.0F, 11.0F, 0.0F, true);
        upper_wall_6_r1.setTextureOffset(0, 89).addCuboid(-21.0F, -15.5163F, -37.0F, 1.0F, 16.0F, 9.0F, 0.0F, true);
        upper_wall_6_r1.setTextureOffset(50, 179).addCuboid(-21.0F, -15.5163F, -16.0F, 3.0F, 16.0F, 15.0F, 0.0F, true);
        upper_wall_6_r1.setTextureOffset(0, 214).addCuboid(-21.0F, -15.5163F, -1.0F, 1.0F, 16.0F, 11.0F, 0.0F, true);

        upper_wall_3_r1 = new ModelPart(this);
        upper_wall_3_r1.setPivot(0.0F, 0.0F, 0.0F);
        head_exterior.addChild(upper_wall_3_r1);
        setRotationAngle(upper_wall_3_r1, 0.0F, 0.0F, -0.0234F);
        upper_wall_3_r1.setTextureOffset(0, 40).addCuboid(19.9948F, -34.5084F, -37.0F, 1.0F, 19.0F, 9.0F, 0.0F, false);
        upper_wall_3_r1.setTextureOffset(156, 55).addCuboid(17.9948F, -34.5084F, -16.0F, 3.0F, 19.0F, 15.0F, 0.0F, false);
        upper_wall_3_r1.setTextureOffset(265, 100).addCuboid(19.9948F, -34.5084F, -1.0F, 1.0F, 19.0F, 11.0F, 0.0F, false);
        upper_wall_3_r1.setTextureOffset(0, 89).addCuboid(19.9948F, -15.5084F, -37.0F, 1.0F, 16.0F, 9.0F, 0.0F, false);
        upper_wall_3_r1.setTextureOffset(50, 179).addCuboid(17.9948F, -15.5084F, -16.0F, 3.0F, 16.0F, 15.0F, 0.0F, false);
        upper_wall_3_r1.setTextureOffset(0, 214).addCuboid(19.9948F, -15.5084F, -1.0F, 1.0F, 16.0F, 11.0F, 0.0F, false);

        floor_3_r1 = new ModelPart(this);
        floor_3_r1.setPivot(0.0F, 3.0075F, -32.8712F);
        head_exterior.addChild(floor_3_r1);
        setRotationAngle(floor_3_r1, -0.3578F, 0.0F, 0.0F);
        floor_3_r1.setTextureOffset(221, 313).addCuboid(-21.0F, -0.5F, -1.75F, 42.0F, 0.0F, 6.0F, 0.0F, false);

        font_panel_r1 = new ModelPart(this);
        font_panel_r1.setPivot(0.0006F, -0.642F, -0.3324F);
        head_exterior.addChild(font_panel_r1);
        setRotationAngle(font_panel_r1, -0.1745F, 0.0F, 0.0F);
        font_panel_r1.setTextureOffset(98, 193).addCuboid(-21.0F, -36.0F, -36.0F, 42.0F, 43.0F, 0.0F, 0.0F, false);

        driver_door = new ModelPart(this);
        driver_door.setPivot(0.0F, 0.0F, 0.0F);
        head_exterior.addChild(driver_door);


        door_wall_4_r1 = new ModelPart(this);
        door_wall_4_r1.setPivot(0.0F, 0.0F, 0.0F);
        driver_door.addChild(door_wall_4_r1);
        setRotationAngle(door_wall_4_r1, 0.0F, 0.0F, 0.0234F);
        door_wall_4_r1.setTextureOffset(64, 262).addCuboid(-20.0F, -31.5163F, -28.0F, 1.0F, 20.0F, 12.0F, 0.0F, true);
        door_wall_4_r1.setTextureOffset(0, 117).addCuboid(-20.0F, -11.5163F, -28.0F, 1.0F, 12.0F, 12.0F, 0.0F, true);
        door_wall_4_r1.setTextureOffset(60, 121).addCuboid(-21.0F, -34.5163F, -28.0F, 3.0F, 3.0F, 12.0F, 0.0F, true);

        door_wall_2_r1 = new ModelPart(this);
        door_wall_2_r1.setPivot(0.0F, 0.0F, 0.0F);
        driver_door.addChild(door_wall_2_r1);
        setRotationAngle(door_wall_2_r1, 0.0F, 0.0F, -0.0234F);
        door_wall_2_r1.setTextureOffset(64, 262).addCuboid(18.9948F, -31.5084F, -28.0F, 1.0F, 20.0F, 12.0F, 0.0F, false);
        door_wall_2_r1.setTextureOffset(0, 117).addCuboid(18.9948F, -11.5084F, -28.0F, 1.0F, 12.0F, 12.0F, 0.0F, false);

        roof_1_r1 = new ModelPart(this);
        roof_1_r1.setPivot(40.9838F, -0.9508F, 0.0F);
        driver_door.addChild(roof_1_r1);
        setRotationAngle(roof_1_r1, 0.0F, 0.0F, -0.0234F);
        roof_1_r1.setTextureOffset(60, 121).addCuboid(-23.0F, -34.5163F, -28.0F, 3.0F, 3.0F, 12.0F, 0.0F, false);

        roof_head = new ModelPart(this);
        roof_head.setPivot(0.0F, 24.0F, 0.0F);
        roof_head.setTextureOffset(0, 0).addCuboid(-17.25F, -32.0F, -1.0F, 2.0F, 0.0F, 9.0F, 0.0F, false);
        roof_head.setTextureOffset(17, 129).addCuboid(-10.0F, -34.0F, -1.0F, 10.0F, 0.0F, 9.0F, 0.0F, false);
        roof_head.setTextureOffset(17, 129).addCuboid(0.0F, -34.0F, -1.0F, 10.0F, 0.0F, 9.0F, 0.0F, true);
        roof_head.setTextureOffset(0, 0).addCuboid(15.25F, -32.0F, -1.0F, 2.0F, 0.0F, 9.0F, 0.0F, true);

        inner_roof_5_r2 = new ModelPart(this);
        inner_roof_5_r2.setPivot(8.6718F, -34.3942F, 15.0F);
        roof_head.addChild(inner_roof_5_r2);
        setRotationAngle(inner_roof_5_r2, 0.0F, 0.0F, 0.3491F);
        inner_roof_5_r2.setTextureOffset(119, 113).addCuboid(1.0F, 0.0F, -16.0F, 6.0F, 0.0F, 9.0F, 0.0F, true);

        inner_roof_2_r4 = new ModelPart(this);
        inner_roof_2_r4.setPivot(-16.1897F, -31.658F, 15.0F);
        roof_head.addChild(inner_roof_2_r4);
        setRotationAngle(inner_roof_2_r4, 0.0F, 0.0F, -0.3491F);
        inner_roof_2_r4.setTextureOffset(119, 113).addCuboid(1.0F, 0.0F, -16.0F, 6.0F, 0.0F, 9.0F, 0.0F, false);

        roof_head_exterior = new ModelPart(this);
        roof_head_exterior.setPivot(0.0F, 24.0F, 0.0F);
        roof_head_exterior.setTextureOffset(71, 113).addCuboid(-8.5771F, -41.6153F, -31.0F, 9.0F, 0.0F, 39.0F, 0.0F, false);
        roof_head_exterior.setTextureOffset(71, 113).addCuboid(-0.4293F, -41.6062F, -31.0F, 9.0F, 0.0F, 39.0F, 0.0F, true);

        outer_roof_9_r2 = new ModelPart(this);
        outer_roof_9_r2.setPivot(-9.5845F, 4.106F, -11.0F);
        roof_head_exterior.addChild(outer_roof_9_r2);
        setRotationAngle(outer_roof_9_r2, 0.0F, 0.0F, 0.1745F);
        outer_roof_9_r2.setTextureOffset(117, 0).addCuboid(9.9416F, -48.1693F, -20.0F, 8.0F, 0.0F, 39.0F, 0.0F, true);

        outer_roof_8_r2 = new ModelPart(this);
        outer_roof_8_r2.setPivot(-0.9377F, -8.8069F, -11.0F);
        roof_head_exterior.addChild(outer_roof_8_r2);
        setRotationAngle(outer_roof_8_r2, 0.0F, 0.0F, 0.5236F);
        outer_roof_8_r2.setTextureOffset(0, 89).addCuboid(-0.6476F, -35.8944F, -20.0F, 3.0F, 0.0F, 39.0F, 0.0F, true);

        outer_roof_7_r2 = new ModelPart(this);
        outer_roof_7_r2.setPivot(5.7997F, -18.0562F, -11.0F);
        roof_head_exterior.addChild(outer_roof_7_r2);
        setRotationAngle(outer_roof_7_r2, 0.0F, 0.0F, 1.0472F);
        outer_roof_7_r2.setTextureOffset(6, 89).addCuboid(-11.269F, -21.8031F, -20.0F, 2.0F, 0.0F, 39.0F, 0.0F, true);

        outer_roof_6_r2 = new ModelPart(this);
        outer_roof_6_r2.setPivot(42.632F, -0.0697F, 8.0F);
        roof_head_exterior.addChild(outer_roof_6_r2);
        setRotationAngle(outer_roof_6_r2, 0.0F, 0.0F, -0.067F);
        outer_roof_6_r2.setTextureOffset(156, 14).addCuboid(-20.0609F, -38.3454F, -39.0F, 0.0F, 2.0F, 39.0F, 0.0F, false);

        outer_roof_4_r3 = new ModelPart(this);
        outer_roof_4_r3.setPivot(0.7632F, 1.078F, -11.0F);
        roof_head_exterior.addChild(outer_roof_4_r3);
        setRotationAngle(outer_roof_4_r3, 0.0F, 0.0F, -0.1745F);
        outer_roof_4_r3.setTextureOffset(117, 0).addCuboid(-9.7849F, -43.6656F, -20.0F, 8.0F, 0.0F, 39.0F, 0.0F, false);

        outer_roof_3_r3 = new ModelPart(this);
        outer_roof_3_r3.setPivot(11.401F, -20.1037F, -11.0F);
        roof_head_exterior.addChild(outer_roof_3_r3);
        setRotationAngle(outer_roof_3_r3, 0.0F, 0.0F, -0.5236F);
        outer_roof_3_r3.setTextureOffset(0, 89).addCuboid(-17.0637F, -31.3539F, -20.0F, 3.0F, 0.0F, 39.0F, 0.0F, false);

        outer_roof_2_r3 = new ModelPart(this);
        outer_roof_2_r3.setPivot(4.4634F, -35.8511F, -11.0F);
        roof_head_exterior.addChild(outer_roof_2_r3);
        setRotationAngle(outer_roof_2_r3, 0.0F, 0.0F, -1.0472F);
        outer_roof_2_r3.setTextureOffset(6, 89).addCuboid(-11.269F, -21.8031F, -20.0F, 2.0F, 0.0F, 39.0F, 0.0F, false);

        outer_roof_1_r3 = new ModelPart(this);
        outer_roof_1_r3.setPivot(-2.6053F, 2.6093F, 6.75F);
        roof_head_exterior.addChild(outer_roof_1_r3);
        setRotationAngle(outer_roof_1_r3, 0.0F, 0.0F, 0.067F);
        outer_roof_1_r3.setTextureOffset(156, 14).addCuboid(-20.0609F, -38.3454F, -37.75F, 0.0F, 2.0F, 39.0F, 0.0F, true);

        roof_window_light = new ModelPart(this);
        roof_window_light.setPivot(0.0F, 24.0F, 0.0F);


        roof_light_r1 = new ModelPart(this);
        roof_light_r1.setPivot(-11.4912F, -33.3681F, 0.0F);
        roof_window_light.addChild(roof_light_r1);
        setRotationAngle(roof_light_r1, 0.0F, 0.0F, -0.3491F);
        roof_light_r1.setTextureOffset(72, 40).addCuboid(-1.0F, 0.0F, -24.0F, 2.0F, 0.0F, 48.0F, 0.0F, false);

        headlights = new ModelPart(this);
        headlights.setPivot(0.0F, 24.0F, 0.0F);


        headlight_4_r1 = new ModelPart(this);
        headlight_4_r1.setPivot(0.0006F, -0.642F, -0.3324F);
        headlights.addChild(headlight_4_r1);
        setRotationAngle(headlight_4_r1, -0.1745F, 0.0F, 0.0F);
        headlight_4_r1.setTextureOffset(316, 100).addCuboid(11.25F, -29.5F, -36.0676F, 3.0F, 2.0F, 0.0F, 0.0F, false);
        headlight_4_r1.setTextureOffset(316, 100).addCuboid(-14.25F, -29.5F, -36.0676F, 3.0F, 2.0F, 0.0F, 0.0F, true);
        headlight_4_r1.setTextureOffset(80, 136).addCuboid(6.5F, 1.0F, -36.0676F, 13.0F, 5.0F, 0.0F, 0.0F, true);
        headlight_4_r1.setTextureOffset(80, 136).addCuboid(-19.5F, 1.0F, -36.0676F, 13.0F, 5.0F, 0.0F, 0.0F, false);

        tail_lights = new ModelPart(this);
        tail_lights.setPivot(0.0F, 24.0F, 0.0F);


        tail_lights_4_r1 = new ModelPart(this);
        tail_lights_4_r1.setPivot(0.0006F, -0.642F, -0.3324F);
        tail_lights.addChild(tail_lights_4_r1);
        setRotationAngle(tail_lights_4_r1, -0.1745F, 0.0F, 0.0F);
        tail_lights_4_r1.setTextureOffset(316, 102).addCuboid(14.0F, -29.75F, -36.0676F, 3.0F, 2.0F, 0.0F, 0.0F, false);
        tail_lights_4_r1.setTextureOffset(316, 102).addCuboid(-17.0F, -29.75F, -36.0676F, 3.0F, 2.0F, 0.0F, 0.0F, true);
        tail_lights_4_r1.setTextureOffset(54, 136).addCuboid(6.5F, 1.0F, -36.0676F, 13.0F, 5.0F, 0.0F, 0.0F, true);
        tail_lights_4_r1.setTextureOffset(54, 136).addCuboid(-19.5F, 1.0F, -36.0676F, 13.0F, 5.0F, 0.0F, 0.0F, false);

        door_light_off = new ModelPart(this);
        door_light_off.setPivot(0.0F, 24.0F, 0.0F);


        door_light_bottom_r1 = new ModelPart(this);
        door_light_bottom_r1.setPivot(-8.1869F, -0.1919F, -0.5F);
        door_light_off.addChild(door_light_bottom_r1);
        setRotationAngle(door_light_bottom_r1, 0.0F, 0.0F, -0.3491F);
        door_light_bottom_r1.setTextureOffset(3, 3).addCuboid(4.6431F, -33.024F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        door_light_on = new ModelPart(this);
        door_light_on.setPivot(0.0F, 24.0F, 0.0F);


        door_light_bottom_r2 = new ModelPart(this);
        door_light_bottom_r2.setPivot(-8.1869F, -0.1919F, -0.5F);
        door_light_on.addChild(door_light_bottom_r2);
        setRotationAngle(door_light_bottom_r2, 0.0F, 0.0F, -0.3491F);
        door_light_bottom_r2.setTextureOffset(314, 107).addCuboid(4.6431F, -33.024F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        door_light_exterior_off = new ModelPart(this);
        door_light_exterior_off.setPivot(0.0F, 24.0F, 0.0F);
        door_light_exterior_off.setTextureOffset(0, 5).addCuboid(-21.1871F, -33.9979F, -2.0F, 1.0F, 2.0F, 4.0F, 0.0F, false);

        door_light_exterior_on = new ModelPart(this);
        door_light_exterior_on.setPivot(0.0F, 24.0F, 0.0F);
        door_light_exterior_on.setTextureOffset(309, 110).addCuboid(-21.1871F, -33.9979F, -2.0F, 1.0F, 2.0F, 4.0F, 0.0F, false);
    }

    private static final int DOOR_MAX = 14;

    @Override
    protected void renderWindowPositions(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, boolean isEnd1Head, boolean isEnd2Head) {
        switch (renderStage) {
            case LIGHTS:
                renderMirror(roof_window_light, matrices, vertices, light, position);
                break;
            case INTERIOR:
                renderMirror(window, matrices, vertices, light, position);
                if (renderDetails) {
                    renderMirror(window_handrails, matrices, vertices, light, position);
                    renderMirror(roof_window, matrices, vertices, light, position);
                }
                break;
            case INTERIOR_TRANSLUCENT:
                renderMirror(side_panel, matrices, vertices, light, position - 25F);
                renderMirror(side_panel, matrices, vertices, light, position + 25F);
                break;
            case EXTERIOR:
                renderMirror(window_exterior, matrices, vertices, light, position);
                renderMirror(roof_exterior, matrices, vertices, light, position);
                break;
        }
    }

    @Override
    protected void renderDoorPositions(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
        final boolean doorOpen = doorLeftZ > 0 || doorRightZ > 0;

        switch (renderStage) {
            case LIGHTS:
                renderMirror(roof_door_light, matrices, vertices, light, position);
                if (doorOpen) {
                    renderMirror(door_light_exterior_on, matrices, vertices, light, position);
                    renderMirror(door_light_on, matrices, vertices, light, position);
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
                    if (!doorOpen) {
                        renderMirror(door_light_off, matrices, vertices, light, position);
                    }
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
                if (!doorOpen) {
                    renderMirror(door_light_exterior_off, matrices, vertices, light, position);
                }
                break;
        }
    }

    @Override
    protected void renderHeadPosition1(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, boolean useHeadlights) {
        switch (renderStage) {
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
                break;
        }
    }

    @Override
    protected void renderHeadPosition2(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, boolean useHeadlights) {
        switch (renderStage) {
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
                break;
        }
    }

    @Override
    protected void renderEndPosition1(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails) {
        switch (renderStage) {
            case LIGHTS:
                renderOnce(roof_end_light, matrices, vertices, light, position);
                break;
            case INTERIOR:
                renderOnce(end, matrices, vertices, light, position);
                if (renderDetails) {
                    renderOnce(roof_end, matrices, vertices, light, position);
                }
                break;
            case EXTERIOR:
                renderOnce(end_exterior, matrices, vertices, light, position);
                renderOnce(roof_end_exterior, matrices, vertices, light, position);
                break;
        }
    }

    @Override
    protected void renderEndPosition2(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails) {
        switch (renderStage) {
            case LIGHTS:
                renderOnceFlipped(roof_end_light, matrices, vertices, light, position);
                break;
            case INTERIOR:
                renderOnceFlipped(end, matrices, vertices, light, position);
                if (renderDetails) {
                    renderOnceFlipped(roof_end, matrices, vertices, light, position);
                }
                break;
            case EXTERIOR:
                renderOnceFlipped(end_exterior, matrices, vertices, light, position);
                renderOnceFlipped(roof_end_exterior, matrices, vertices, light, position);
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
    protected float getDoorAnimationX(float value, boolean opening) {
        return 0;
    }

    @Override
    protected float getDoorAnimationZ(float value, boolean opening) {
        if (opening) {
            if (value > 0.4) {
                return smoothEnds(DOOR_MAX - 1, DOOR_MAX - 0.5F, 0.4F, 0.5F, value);
            } else {
                return smoothEnds(-DOOR_MAX + 1, DOOR_MAX - 1, -0.4F, 0.4F, value);
            }
        } else {
            if (value > 0.2) {
                return smoothEnds(1, DOOR_MAX - 0.5F, 0.2F, 0.5F, value);
            } else if (value > 0.1) {
                return smoothEnds(1.5F, 1, 0.1F, 0.2F, value);
            } else {
                return smoothEnds(-1.5F, 1.5F, -0.1F, 0.1F, value);
            }
        }
    }
}