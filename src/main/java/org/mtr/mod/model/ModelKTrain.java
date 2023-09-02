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

public class ModelKTrain extends ModelSimpleTrainBase<ModelKTrain> {

	private final ModelMapper window;
	private final ModelMapper upper_wall_r1;
	private final ModelMapper window_handrail;
	private final ModelMapper handrail_8_r1;
	private final ModelMapper top_handrail_6_r1;
	private final ModelMapper top_handrail_5_r1;
	private final ModelMapper top_handrail_4_r1;
	private final ModelMapper top_handrail_3_r1;
	private final ModelMapper top_handrail_2_r1;
	private final ModelMapper top_handrail_1_r1;
	private final ModelMapper handrail_5_r1;
	private final ModelMapper upper_wall_2_r1;
	private final ModelMapper seat;
	private final ModelMapper seat_back_r1;
	private final ModelMapper window_exterior;
	private final ModelMapper upper_wall_r2;
	private final ModelMapper window_exterior_end;
	private final ModelMapper upper_wall_r3;
	private final ModelMapper upper_wall_r4;
	private final ModelMapper side_panel;
	private final ModelMapper side_panel_translucent;
	private final ModelMapper roof_window;
	private final ModelMapper inner_roof_4_r1;
	private final ModelMapper inner_roof_3_r1;
	private final ModelMapper roof_door;
	private final ModelMapper inner_roof_4_r2;
	private final ModelMapper inner_roof_3_r2;
	private final ModelMapper handrail_2_r1;
	private final ModelMapper roof_exterior;
	private final ModelMapper outer_roof_5_r1;
	private final ModelMapper outer_roof_4_r1;
	private final ModelMapper outer_roof_3_r1;
	private final ModelMapper door;
	private final ModelMapper door_left;
	private final ModelMapper door_left_top_r1;
	private final ModelMapper door_right;
	private final ModelMapper door_right_top_r1;
	private final ModelMapper door_handrail;
	private final ModelMapper door_exterior;
	private final ModelMapper upper_wall_r5;
	private final ModelMapper door_left_exterior;
	private final ModelMapper door_left_top_r2;
	private final ModelMapper door_right_exterior;
	private final ModelMapper door_right_top_r2;
	private final ModelMapper door_exterior_end;
	private final ModelMapper upper_wall_r6;
	private final ModelMapper door_left_exterior_end;
	private final ModelMapper door_left_top_r3;
	private final ModelMapper door_right_exterior_end;
	private final ModelMapper door_right_top_r3;
	private final ModelMapper end;
	private final ModelMapper upper_wall_2_r2;
	private final ModelMapper upper_wall_1_r1;
	private final ModelMapper lower_wall_1_r1;
	private final ModelMapper end_exterior;
	private final ModelMapper upper_wall_2_r3;
	private final ModelMapper upper_wall_1_r2;
	private final ModelMapper roof_end;
	private final ModelMapper handrail_2_r2;
	private final ModelMapper inner_roof_1;
	private final ModelMapper inner_roof_4_r3;
	private final ModelMapper inner_roof_3_r3;
	private final ModelMapper inner_roof_2;
	private final ModelMapper inner_roof_4_r4;
	private final ModelMapper inner_roof_3_r4;
	private final ModelMapper roof_end_exterior;
	private final ModelMapper vent_2_r1;
	private final ModelMapper vent_1_r1;
	private final ModelMapper outer_roof_1;
	private final ModelMapper outer_roof_5_r2;
	private final ModelMapper outer_roof_4_r2;
	private final ModelMapper outer_roof_3_r2;
	private final ModelMapper outer_roof_2;
	private final ModelMapper outer_roof_5_r3;
	private final ModelMapper outer_roof_4_r3;
	private final ModelMapper outer_roof_3_r3;
	private final ModelMapper roof_light;
	private final ModelMapper roof_light_r1;
	private final ModelMapper roof_end_light;
	private final ModelMapper roof_light_2_r1;
	private final ModelMapper roof_light_1_r1;
	private final ModelMapper head;
	private final ModelMapper upper_wall_2_r4;
	private final ModelMapper upper_wall_1_r3;
	private final ModelMapper lower_wall_1_r2;
	private final ModelMapper head_exterior;
	private final ModelMapper upper_wall_2_r5;
	private final ModelMapper upper_wall_1_r4;
	private final ModelMapper front;
	private final ModelMapper front_bottom_2_r1;
	private final ModelMapper front_panel_4_r1;
	private final ModelMapper front_panel_3_r1;
	private final ModelMapper front_panel_1_r1;
	private final ModelMapper side_1;
	private final ModelMapper outer_roof_5_r4;
	private final ModelMapper outer_roof_4_r4;
	private final ModelMapper outer_roof_3_r4;
	private final ModelMapper outer_roof_2_r1;
	private final ModelMapper front_side_bottom_1_r1;
	private final ModelMapper front_side_lower_1_r1;
	private final ModelMapper front_side_upper_1_r1;
	private final ModelMapper side_2;
	private final ModelMapper outer_roof_5_r5;
	private final ModelMapper outer_roof_4_r5;
	private final ModelMapper outer_roof_3_r5;
	private final ModelMapper outer_roof_2_r2;
	private final ModelMapper front_side_bottom_2_r1;
	private final ModelMapper front_side_upper_2_r1;
	private final ModelMapper front_side_lower_2_r1;
	private final ModelMapper headlights;
	private final ModelMapper tail_lights;
	private final ModelMapper door_light;
	private final ModelMapper outer_roof_3_r6;
	private final ModelMapper door_light_on;
	private final ModelMapper light_r1;
	private final ModelMapper door_light_off;
	private final ModelMapper light_r2;

	protected final boolean isTcl;

	public ModelKTrain(boolean isTcl) {
		this(isTcl, DoorAnimationType.PLUG_SLOW, true);
	}

	protected ModelKTrain(boolean isTcl, DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		super(doorAnimationType, renderDoorOverlay);
		this.isTcl = isTcl;

		final int textureWidth = 320;
		final int textureHeight = 320;

		final ModelDataWrapper modelDataWrapper = new ModelDataWrapper(this, textureWidth, textureHeight);

		window = new ModelMapper(modelDataWrapper);
		window.setPos(0, 24, 0);
		window.texOffs(0, 42).addBox(-20, 0, -24, 20, 1, 48, 0, false);

		upper_wall_r1 = new ModelMapper(modelDataWrapper);
		upper_wall_r1.setPos(-21, -14, 0);
		window.addChild(upper_wall_r1);
		setRotationAngle(upper_wall_r1, 0, 0, 0.1107F);
		upper_wall_r1.texOffs(0, 129).addBox(1, -19, -22, 2, 19, 44, 0, false);

		window_handrail = new ModelMapper(modelDataWrapper);
		window_handrail.setPos(0, 24, 0);
		window_handrail.texOffs(29, 0).addBox(-1, -32, -21, 2, 4, 0, 0, false);
		window_handrail.texOffs(148, 169).addBox(-21, -14, 21, 4, 14, 6, 0, false);
		window_handrail.texOffs(168, 52).addBox(-21, -14, -27, 4, 14, 6, 0, false);
		window_handrail.texOffs(8, 0).addBox(0, -35, -9, 0, 35, 0, 0.2F, false);
		window_handrail.texOffs(8, 0).addBox(0, -35, 9, 0, 35, 0, 0.2F, false);
		window_handrail.texOffs(29, 0).addBox(-1, -32, 21, 2, 4, 0, 0, false);
		window_handrail.texOffs(29, 0).addBox(-1, -32, 15, 2, 4, 0, 0, false);
		window_handrail.texOffs(29, 0).addBox(-1, -32, 3, 2, 4, 0, 0, false);
		window_handrail.texOffs(29, 0).addBox(-1, -32, -3, 2, 4, 0, 0, false);
		window_handrail.texOffs(29, 0).addBox(-1, -32, -15, 2, 4, 0, 0, false);

		handrail_8_r1 = new ModelMapper(modelDataWrapper);
		handrail_8_r1.setPos(0, 0, 0);
		window_handrail.addChild(handrail_8_r1);
		setRotationAngle(handrail_8_r1, -1.5708F, 0, 0);
		handrail_8_r1.texOffs(0, 0).addBox(0, -24, -31.5F, 0, 48, 0, 0.2F, false);

		top_handrail_6_r1 = new ModelMapper(modelDataWrapper);
		top_handrail_6_r1.setPos(-12.0518F, -29.0895F, 9.5876F);
		window_handrail.addChild(top_handrail_6_r1);
		setRotationAngle(top_handrail_6_r1, 1.5708F, 0, -0.0436F);
		top_handrail_6_r1.texOffs(0, 0).addBox(0, -9.5F, 0, 0, 20, 0, 0.2F, false);

		top_handrail_5_r1 = new ModelMapper(modelDataWrapper);
		top_handrail_5_r1.setPos(-12.0377F, -28.7666F, 20.7938F);
		window_handrail.addChild(top_handrail_5_r1);
		setRotationAngle(top_handrail_5_r1, 1.0472F, 0, -0.0436F);
		top_handrail_5_r1.texOffs(0, 0).addBox(0, -0.5F, 0, 0, 1, 0, 0.2F, false);

		top_handrail_4_r1 = new ModelMapper(modelDataWrapper);
		top_handrail_4_r1.setPos(-11.9992F, -27.8844F, 21.6768F);
		window_handrail.addChild(top_handrail_4_r1);
		setRotationAngle(top_handrail_4_r1, 0.5236F, 0, -0.0436F);
		top_handrail_4_r1.texOffs(0, 0).addBox(0, -0.5F, 0, 0, 1, 0, 0.2F, false);

		top_handrail_3_r1 = new ModelMapper(modelDataWrapper);
		top_handrail_3_r1.setPos(-12.0518F, -29.0895F, -9.5876F);
		window_handrail.addChild(top_handrail_3_r1);
		setRotationAngle(top_handrail_3_r1, -1.5708F, 0, -0.0436F);
		top_handrail_3_r1.texOffs(0, 0).addBox(0, -9.5F, 0, 0, 20, 0, 0.2F, false);

		top_handrail_2_r1 = new ModelMapper(modelDataWrapper);
		top_handrail_2_r1.setPos(-12.0377F, -28.7666F, -20.7938F);
		window_handrail.addChild(top_handrail_2_r1);
		setRotationAngle(top_handrail_2_r1, -1.0472F, 0, -0.0436F);
		top_handrail_2_r1.texOffs(0, 0).addBox(0, -0.5F, 0, 0, 1, 0, 0.2F, false);

		top_handrail_1_r1 = new ModelMapper(modelDataWrapper);
		top_handrail_1_r1.setPos(-11.9992F, -27.8844F, -21.6768F);
		window_handrail.addChild(top_handrail_1_r1);
		setRotationAngle(top_handrail_1_r1, -0.5236F, 0, -0.0436F);
		top_handrail_1_r1.texOffs(0, 0).addBox(0, -0.5F, 0, 0, 1, 0, 0.2F, false);

		handrail_5_r1 = new ModelMapper(modelDataWrapper);
		handrail_5_r1.setPos(-11, -5, 0);
		window_handrail.addChild(handrail_5_r1);
		setRotationAngle(handrail_5_r1, 0, 0, -0.0436F);
		handrail_5_r1.texOffs(0, 0).addBox(0, -28.2F, -14, 0, 4, 0, 0.2F, false);
		handrail_5_r1.texOffs(0, 0).addBox(0, -28.2F, 14, 0, 4, 0, 0.2F, false);
		handrail_5_r1.texOffs(4, 0).addBox(0, -22.2F, 22, 0, 22, 0, 0.2F, false);
		handrail_5_r1.texOffs(4, 0).addBox(0, -22.2F, -22, 0, 22, 0, 0.2F, false);

		upper_wall_2_r1 = new ModelMapper(modelDataWrapper);
		upper_wall_2_r1.setPos(-21, -14, 0);
		window_handrail.addChild(upper_wall_2_r1);
		setRotationAngle(upper_wall_2_r1, 0, 0, 0.1107F);
		upper_wall_2_r1.texOffs(120, 166).addBox(0, -19, -27, 4, 19, 5, 0, false);
		upper_wall_2_r1.texOffs(211, 91).addBox(0, -19, 22, 4, 19, 5, 0, false);

		seat = new ModelMapper(modelDataWrapper);
		seat.setPos(0, 0, 0);
		window_handrail.addChild(seat);
		seat.texOffs(148, 145).addBox(-18, -6, -22, 7, 1, 44, 0, false);
		seat.texOffs(188, 73).addBox(-18, -5, -21, 5, 5, 42, 0, false);

		seat_back_r1 = new ModelMapper(modelDataWrapper);
		seat_back_r1.setPos(-17, -6, 0);
		seat.addChild(seat_back_r1);
		setRotationAngle(seat_back_r1, 0, 0, -0.0524F);
		seat_back_r1.texOffs(168, 0).addBox(-1, -8, -22, 1, 8, 44, 0, false);

		window_exterior = new ModelMapper(modelDataWrapper);
		window_exterior.setPos(0, 24, 0);
		window_exterior.texOffs(98, 145).addBox(-21, 0, -24, 1, 4, 48, 0, false);
		window_exterior.texOffs(0, 63).addBox(-21, -14, -26, 0, 14, 52, 0, false);

		upper_wall_r2 = new ModelMapper(modelDataWrapper);
		upper_wall_r2.setPos(-21, -14, 0);
		window_exterior.addChild(upper_wall_r2);
		setRotationAngle(upper_wall_r2, 0, 0, 0.1107F);
		upper_wall_r2.texOffs(0, 41).addBox(0, -22, -26, 0, 22, 52, 0, false);

		setRotationAngle(upper_wall_r2, 0, 0, 0.1107F);
		upper_wall_r2.texOffs(0, 41).addBox(0, -22, -26, 0, 22, 52, 0, false);

		window_exterior_end = new ModelMapper(modelDataWrapper);
		window_exterior_end.setPos(0, 24, 0);
		window_exterior_end.texOffs(87, 258).addBox(-21, 0, -24, 1, 4, 48, 0, false);
		window_exterior_end.texOffs(137, 228).addBox(-21, -14, -26, 0, 14, 52, 0, false);
		window_exterior_end.texOffs(87, 258).addBox(20, 0, -24, 1, 4, 48, 0, true);
		window_exterior_end.texOffs(137, 228).addBox(21, -14, -26, 0, 14, 52, 0, true);

		upper_wall_r3 = new ModelMapper(modelDataWrapper);
		upper_wall_r3.setPos(21, -14, 0);
		window_exterior_end.addChild(upper_wall_r3);
		setRotationAngle(upper_wall_r3, 0, 0, -0.1107F);
		upper_wall_r3.texOffs(137, 206).addBox(0, -22, -26, 0, 22, 52, 0, true);

		upper_wall_r4 = new ModelMapper(modelDataWrapper);
		upper_wall_r4.setPos(-21, -14, 0);
		window_exterior_end.addChild(upper_wall_r4);
		setRotationAngle(upper_wall_r4, 0, 0, 0.1107F);
		upper_wall_r4.texOffs(137, 206).addBox(0, -22, -26, 0, 22, 52, 0, false);

		side_panel = new ModelMapper(modelDataWrapper);
		side_panel.setPos(0, 24, 0);
		side_panel.texOffs(30, 143).addBox(-18, -34, 0, 7, 30, 0, 0, false);

		side_panel_translucent = new ModelMapper(modelDataWrapper);
		side_panel_translucent.setPos(0, 24, 0);
		side_panel_translucent.texOffs(293, 163).addBox(-18, -34, 0, 7, 30, 0, 0, false);

		roof_window = new ModelMapper(modelDataWrapper);
		roof_window.setPos(0, 24, 0);
		roof_window.texOffs(40, 42).addBox(-16, -32, -24, 4, 0, 48, 0, false);
		roof_window.texOffs(68, 42).addBox(-3, -34.5F, -24, 3, 0, 48, 0, false);

		inner_roof_4_r1 = new ModelMapper(modelDataWrapper);
		inner_roof_4_r1.setPos(-5.5473F, -34.2615F, 0);
		roof_window.addChild(inner_roof_4_r1);
		setRotationAngle(inner_roof_4_r1, 0, 0, -0.0873F);
		inner_roof_4_r1.texOffs(56, 42).addBox(-3, 0, -24, 6, 0, 48, 0, false);

		inner_roof_3_r1 = new ModelMapper(modelDataWrapper);
		inner_roof_3_r1.setPos(-12, -32, 0);
		roof_window.addChild(inner_roof_3_r1);
		setRotationAngle(inner_roof_3_r1, 0, 0, -0.5236F);
		inner_roof_3_r1.texOffs(48, 42).addBox(0, 0, -24, 4, 0, 48, 0, false);

		roof_door = new ModelMapper(modelDataWrapper);
		roof_door.setPos(0, 24, 0);
		roof_door.texOffs(132, 10).addBox(-18, -33, -16, 6, 1, 32, 0, false);
		roof_door.texOffs(84, 50).addBox(-3, -34.5F, -16, 3, 0, 32, 0, false);

		inner_roof_4_r2 = new ModelMapper(modelDataWrapper);
		inner_roof_4_r2.setPos(-5.5473F, -34.2615F, 0);
		roof_door.addChild(inner_roof_4_r2);
		setRotationAngle(inner_roof_4_r2, 0, 0, -0.0873F);
		inner_roof_4_r2.texOffs(72, 50).addBox(-3, 0, -16, 6, 0, 32, 0, false);

		inner_roof_3_r2 = new ModelMapper(modelDataWrapper);
		inner_roof_3_r2.setPos(-12, -32, 0);
		roof_door.addChild(inner_roof_3_r2);
		setRotationAngle(inner_roof_3_r2, 0, 0, -0.5236F);
		inner_roof_3_r2.texOffs(128, 93).addBox(0, 0, -16, 4, 0, 32, 0, true);

		handrail_2_r1 = new ModelMapper(modelDataWrapper);
		handrail_2_r1.setPos(0, 0, 0);
		roof_door.addChild(handrail_2_r1);
		setRotationAngle(handrail_2_r1, -1.5708F, 0, 0);
		handrail_2_r1.texOffs(0, 0).addBox(0, -16, -31.5F, 0, 32, 0, 0.2F, false);

		roof_exterior = new ModelMapper(modelDataWrapper);
		roof_exterior.setPos(0, 24, 0);
		roof_exterior.texOffs(82, 43).addBox(-5.728F, -41.8527F, -20, 6, 0, 40, 0, false);

		outer_roof_5_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_5_r1.setPos(-9.6672F, -41.1581F, 0);
		roof_exterior.addChild(outer_roof_5_r1);
		setRotationAngle(outer_roof_5_r1, 0, 0, -0.1745F);
		outer_roof_5_r1.texOffs(82, 93).addBox(-4, 0, -20, 8, 0, 40, 0, false);

		outer_roof_4_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_4_r1.setPos(-15.3385F, -39.4635F, 0);
		roof_exterior.addChild(outer_roof_4_r1);
		setRotationAngle(outer_roof_4_r1, 0, 0, -0.5236F);
		outer_roof_4_r1.texOffs(0, 42).addBox(-2, 0, -20, 4, 0, 40, 0, false);

		outer_roof_3_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_3_r1.setPos(-17.8206F, -37.1645F, 0);
		roof_exterior.addChild(outer_roof_3_r1);
		setRotationAngle(outer_roof_3_r1, 0, 0, -1.0472F);
		outer_roof_3_r1.texOffs(98, 93).addBox(-1.5F, 0, -20, 3, 0, 40, 0, false);

		door = new ModelMapper(modelDataWrapper);
		door.setPos(0, 24, 0);
		door.texOffs(164, 190).addBox(-20, 0, -16, 20, 1, 32, 0, false);

		door_left = new ModelMapper(modelDataWrapper);
		door_left.setPos(0, 0, 0);
		door.addChild(door_left);
		door_left.texOffs(116, 200).addBox(-21, -14, 0, 1, 14, 14, 0, false);

		door_left_top_r1 = new ModelMapper(modelDataWrapper);
		door_left_top_r1.setPos(-21, -14, 0);
		door_left.addChild(door_left_top_r1);
		setRotationAngle(door_left_top_r1, 0, 0, 0.1107F);
		door_left_top_r1.texOffs(274, 59).addBox(0, -19, 0, 1, 19, 14, 0, false);

		door_right = new ModelMapper(modelDataWrapper);
		door_right.setPos(0, 0, 0);
		door.addChild(door_right);
		door_right.texOffs(0, 192).addBox(-21, -14, -14, 1, 14, 14, 0, false);

		door_right_top_r1 = new ModelMapper(modelDataWrapper);
		door_right_top_r1.setPos(-21, -14, 0);
		door_right.addChild(door_right_top_r1);
		setRotationAngle(door_right_top_r1, 0, 0, 0.1107F);
		door_right_top_r1.texOffs(178, 0).addBox(0, -19, -14, 1, 19, 14, 0, false);

		door_handrail = new ModelMapper(modelDataWrapper);
		door_handrail.setPos(0, 24, 0);
		door_handrail.texOffs(8, 0).addBox(0, -35, 0, 0, 35, 0, 0.2F, false);

		door_exterior = new ModelMapper(modelDataWrapper);
		door_exterior.setPos(0, 24, 0);
		door_exterior.texOffs(114, 161).addBox(-21, 0, -16, 1, 4, 32, 0, false);

		upper_wall_r5 = new ModelMapper(modelDataWrapper);
		upper_wall_r5.setPos(-21, -14, 0);
		door_exterior.addChild(upper_wall_r5);
		setRotationAngle(upper_wall_r5, 0, 0, 0.1107F);
		upper_wall_r5.texOffs(77, 259).addBox(0, -22, -14, 1, 3, 28, 0, false);

		door_left_exterior = new ModelMapper(modelDataWrapper);
		door_left_exterior.setPos(0, 0, 0);
		door_exterior.addChild(door_left_exterior);
		door_left_exterior.texOffs(0, 135).addBox(-21, -14, 0, 0, 14, 14, 0, false);

		door_left_top_r2 = new ModelMapper(modelDataWrapper);
		door_left_top_r2.setPos(-21, -14, 0);
		door_left_exterior.addChild(door_left_top_r2);
		setRotationAngle(door_left_top_r2, 0, 0, 0.1107F);
		door_left_top_r2.texOffs(0, 115).addBox(0, -20, 0, 0, 20, 14, 0, false);

		door_right_exterior = new ModelMapper(modelDataWrapper);
		door_right_exterior.setPos(0, 0, 0);
		door_exterior.addChild(door_right_exterior);
		door_right_exterior.texOffs(262, 229).addBox(-21, -14, -14, 0, 14, 14, 0, false);

		door_right_top_r2 = new ModelMapper(modelDataWrapper);
		door_right_top_r2.setPos(-21, -14, 0);
		door_right_exterior.addChild(door_right_top_r2);
		setRotationAngle(door_right_top_r2, 0, 0, 0.1107F);
		door_right_top_r2.texOffs(262, 209).addBox(0, -20, -14, 0, 20, 14, 0, false);

		door_exterior_end = new ModelMapper(modelDataWrapper);
		door_exterior_end.setPos(0, 24, 0);
		door_exterior_end.texOffs(0, 282).addBox(-21, 0, -16, 1, 4, 32, 0, false);

		upper_wall_r6 = new ModelMapper(modelDataWrapper);
		upper_wall_r6.setPos(-21, -14, 0);
		door_exterior_end.addChild(upper_wall_r6);
		setRotationAngle(upper_wall_r6, 0, 0, 0.1107F);
		upper_wall_r6.texOffs(77, 259).addBox(0, -22, -14, 1, 3, 28, 0, false);

		door_left_exterior_end = new ModelMapper(modelDataWrapper);
		door_left_exterior_end.setPos(0, 0, 0);
		door_exterior_end.addChild(door_left_exterior_end);
		door_left_exterior_end.texOffs(0, 268).addBox(-21, -14, 0, 0, 14, 14, 0, false);

		door_left_top_r3 = new ModelMapper(modelDataWrapper);
		door_left_top_r3.setPos(-21, -14, 0);
		door_left_exterior_end.addChild(door_left_top_r3);
		setRotationAngle(door_left_top_r3, 0, 0, 0.1107F);
		door_left_top_r3.texOffs(0, 115).addBox(0, -20, 0, 0, 20, 14, 0, false);

		door_right_exterior_end = new ModelMapper(modelDataWrapper);
		door_right_exterior_end.setPos(0, 0, 0);
		door_exterior_end.addChild(door_right_exterior_end);
		door_right_exterior_end.texOffs(0, 282).addBox(-21, -14, -14, 0, 14, 14, 0, false);

		door_right_top_r3 = new ModelMapper(modelDataWrapper);
		door_right_top_r3.setPos(-21, -14, 0);
		door_right_exterior_end.addChild(door_right_top_r3);
		setRotationAngle(door_right_top_r3, 0, 0, 0.1107F);
		door_right_top_r3.texOffs(262, 209).addBox(0, -20, -14, 0, 20, 14, 0, false);

		end = new ModelMapper(modelDataWrapper);
		end.setPos(0, 24, 0);
		end.texOffs(168, 52).addBox(-20, 0, -12, 40, 1, 20, 0, false);
		end.texOffs(148, 169).addBox(-21, -14, 5, 4, 14, 6, 0, false);
		end.texOffs(51, 234).addBox(9.5F, -34, -12, 8, 34, 19, 0, true);
		end.texOffs(51, 234).addBox(-17.5F, -34, -12, 8, 34, 19, 0, false);
		end.texOffs(236, 174).addBox(-9.5F, -35, -12, 19, 2, 19, 0, false);

		upper_wall_2_r2 = new ModelMapper(modelDataWrapper);
		upper_wall_2_r2.setPos(-21, -14, 0);
		end.addChild(upper_wall_2_r2);
		setRotationAngle(upper_wall_2_r2, 0, 0, 0.1107F);
		upper_wall_2_r2.texOffs(211, 91).addBox(0, -19, 6, 4, 19, 5, 0, false);

		upper_wall_1_r1 = new ModelMapper(modelDataWrapper);
		upper_wall_1_r1.setPos(21, -14, 0);
		end.addChild(upper_wall_1_r1);
		setRotationAngle(upper_wall_1_r1, 0, 3.1416F, -0.1107F);
		upper_wall_1_r1.texOffs(120, 166).addBox(0, -19, -11, 4, 19, 5, 0, false);

		lower_wall_1_r1 = new ModelMapper(modelDataWrapper);
		lower_wall_1_r1.setPos(0, 0, 0);
		end.addChild(lower_wall_1_r1);
		setRotationAngle(lower_wall_1_r1, 0, 3.1416F, 0);
		lower_wall_1_r1.texOffs(168, 52).addBox(-21, -14, -11, 4, 14, 6, 0, false);

		end_exterior = new ModelMapper(modelDataWrapper);
		end_exterior.setPos(0, 24, 0);
		end_exterior.texOffs(148, 145).addBox(20, 0, -12, 1, 4, 20, 0, true);
		end_exterior.texOffs(148, 145).addBox(-21, 0, -12, 1, 4, 20, 0, false);
		end_exterior.texOffs(0, 246).addBox(18, -14, -12, 3, 14, 22, 0, true);
		end_exterior.texOffs(0, 246).addBox(-21, -14, -12, 3, 14, 22, 0, false);
		end_exterior.texOffs(248, 120).addBox(9.5F, -34, -12, 10, 34, 0, 0, true);
		end_exterior.texOffs(248, 120).addBox(-19.5F, -34, -12, 10, 34, 0, 0, false);
		end_exterior.texOffs(105, 248).addBox(-18, -41, -12, 36, 7, 0, 0, false);

		upper_wall_2_r3 = new ModelMapper(modelDataWrapper);
		upper_wall_2_r3.setPos(-21, -14, 0);
		end_exterior.addChild(upper_wall_2_r3);
		setRotationAngle(upper_wall_2_r3, 0, 0, 0.1107F);
		upper_wall_2_r3.texOffs(268, 98).addBox(0, -22, -12, 3, 22, 22, 0, false);

		upper_wall_1_r2 = new ModelMapper(modelDataWrapper);
		upper_wall_1_r2.setPos(21, -14, 0);
		end_exterior.addChild(upper_wall_1_r2);
		setRotationAngle(upper_wall_1_r2, 0, 0, -0.1107F);
		upper_wall_1_r2.texOffs(268, 98).addBox(-3, -22, -12, 3, 22, 22, 0, true);

		roof_end = new ModelMapper(modelDataWrapper);
		roof_end.setPos(0, 24, 0);


		handrail_2_r2 = new ModelMapper(modelDataWrapper);
		handrail_2_r2.setPos(0, 0, 0);
		roof_end.addChild(handrail_2_r2);
		setRotationAngle(handrail_2_r2, -1.5708F, 0, 0);
		handrail_2_r2.texOffs(0, 0).addBox(0, -40, -31.5F, 0, 16, 0, 0.2F, false);

		inner_roof_1 = new ModelMapper(modelDataWrapper);
		inner_roof_1.setPos(-2, -33, 16);
		roof_end.addChild(inner_roof_1);
		inner_roof_1.texOffs(128, 6).addBox(-16, 0, -12, 6, 1, 36, 0, false);
		inner_roof_1.texOffs(80, 42).addBox(-1, -1.5F, -12, 3, 0, 36, 0, false);

		inner_roof_4_r3 = new ModelMapper(modelDataWrapper);
		inner_roof_4_r3.setPos(-3.5473F, -1.2615F, -16);
		inner_roof_1.addChild(inner_roof_4_r3);
		setRotationAngle(inner_roof_4_r3, 0, 0, -0.0873F);
		inner_roof_4_r3.texOffs(68, 42).addBox(-3, 0, 4, 6, 0, 36, 0, false);

		inner_roof_3_r3 = new ModelMapper(modelDataWrapper);
		inner_roof_3_r3.setPos(-10, 1, -16);
		inner_roof_1.addChild(inner_roof_3_r3);
		setRotationAngle(inner_roof_3_r3, 0, 0, -0.5236F);
		inner_roof_3_r3.texOffs(116, 93).addBox(0, 0, 4, 4, 0, 36, 0, true);

		inner_roof_2 = new ModelMapper(modelDataWrapper);
		inner_roof_2.setPos(-2, -33, 16);
		roof_end.addChild(inner_roof_2);
		inner_roof_2.texOffs(80, 42).addBox(2, -1.5F, -12, 3, 0, 36, 0, true);
		inner_roof_2.texOffs(128, 6).addBox(14, 0, -12, 6, 1, 36, 0, true);

		inner_roof_4_r4 = new ModelMapper(modelDataWrapper);
		inner_roof_4_r4.setPos(7.5473F, -1.2615F, -16);
		inner_roof_2.addChild(inner_roof_4_r4);
		setRotationAngle(inner_roof_4_r4, 0, 0, 0.0873F);
		inner_roof_4_r4.texOffs(68, 42).addBox(-3, 0, 4, 6, 0, 36, 0, true);

		inner_roof_3_r4 = new ModelMapper(modelDataWrapper);
		inner_roof_3_r4.setPos(14, 1, -16);
		inner_roof_2.addChild(inner_roof_3_r4);
		setRotationAngle(inner_roof_3_r4, 0, 0, 0.5236F);
		inner_roof_3_r4.texOffs(108, 93).addBox(-4, 0, 4, 4, 0, 36, 0, true);

		roof_end_exterior = new ModelMapper(modelDataWrapper);
		roof_end_exterior.setPos(0, 24, 0);
		roof_end_exterior.texOffs(88, 43).addBox(-8, -43, 0, 16, 2, 48, 0, false);

		vent_2_r1 = new ModelMapper(modelDataWrapper);
		vent_2_r1.setPos(-8, -43, 0);
		roof_end_exterior.addChild(vent_2_r1);
		setRotationAngle(vent_2_r1, 0, 0, -0.3491F);
		vent_2_r1.texOffs(56, 93).addBox(-9, 0, 0, 9, 2, 48, 0, false);

		vent_1_r1 = new ModelMapper(modelDataWrapper);
		vent_1_r1.setPos(8, -43, 0);
		roof_end_exterior.addChild(vent_1_r1);
		setRotationAngle(vent_1_r1, 0, 0, 0.3491F);
		vent_1_r1.texOffs(56, 93).addBox(0, 0, 0, 9, 2, 48, 0, true);

		outer_roof_1 = new ModelMapper(modelDataWrapper);
		outer_roof_1.setPos(0, 0, 0);
		roof_end_exterior.addChild(outer_roof_1);
		outer_roof_1.texOffs(188, 120).addBox(-5.7289F, -41.8532F, -12, 6, 1, 20, 0, false);

		outer_roof_5_r2 = new ModelMapper(modelDataWrapper);
		outer_roof_5_r2.setPos(-9.4945F, -40.1738F, -8);
		outer_roof_1.addChild(outer_roof_5_r2);
		setRotationAngle(outer_roof_5_r2, 0, 0, -0.1745F);
		outer_roof_5_r2.texOffs(38, 193).addBox(-4, -1, -4, 8, 1, 20, 0, false);

		outer_roof_4_r2 = new ModelMapper(modelDataWrapper);
		outer_roof_4_r2.setPos(-14.4064F, -38.848F, -8);
		outer_roof_1.addChild(outer_roof_4_r2);
		setRotationAngle(outer_roof_4_r2, 0, 0, -0.5236F);
		outer_roof_4_r2.texOffs(0, 62).addBox(-2.5F, -1, -4, 4, 1, 20, 0, false);

		outer_roof_3_r2 = new ModelMapper(modelDataWrapper);
		outer_roof_3_r2.setPos(-16.7054F, -37.098F, -8);
		outer_roof_1.addChild(outer_roof_3_r2);
		setRotationAngle(outer_roof_3_r2, 0, 0, -1.0472F);
		outer_roof_3_r2.texOffs(98, 145).addBox(-2, -1, -4, 3, 1, 20, 0, false);

		outer_roof_2 = new ModelMapper(modelDataWrapper);
		outer_roof_2.setPos(0, 0, 0);
		roof_end_exterior.addChild(outer_roof_2);
		outer_roof_2.texOffs(188, 120).addBox(-0.2711F, -41.8532F, -12, 6, 1, 20, 0, true);

		outer_roof_5_r3 = new ModelMapper(modelDataWrapper);
		outer_roof_5_r3.setPos(9.4945F, -40.1738F, -8);
		outer_roof_2.addChild(outer_roof_5_r3);
		setRotationAngle(outer_roof_5_r3, 0, 0, 0.1745F);
		outer_roof_5_r3.texOffs(38, 193).addBox(-4, -1, -4, 8, 1, 20, 0, true);

		outer_roof_4_r3 = new ModelMapper(modelDataWrapper);
		outer_roof_4_r3.setPos(15.2724F, -38.348F, -8);
		outer_roof_2.addChild(outer_roof_4_r3);
		setRotationAngle(outer_roof_4_r3, 0, 0, 0.5236F);
		outer_roof_4_r3.texOffs(0, 62).addBox(-2.5F, -1, -4, 4, 1, 20, 0, true);

		outer_roof_3_r3 = new ModelMapper(modelDataWrapper);
		outer_roof_3_r3.setPos(16.7054F, -37.098F, -8);
		outer_roof_2.addChild(outer_roof_3_r3);
		setRotationAngle(outer_roof_3_r3, 0, 0, 1.0472F);
		outer_roof_3_r3.texOffs(98, 145).addBox(-1, -1, -4, 3, 1, 20, 0, true);

		roof_light = new ModelMapper(modelDataWrapper);
		roof_light.setPos(0, 24, 0);


		roof_light_r1 = new ModelMapper(modelDataWrapper);
		roof_light_r1.setPos(-5.8F, -33.8F, 0);
		roof_light.addChild(roof_light_r1);
		setRotationAngle(roof_light_r1, 0, 0, 0.1309F);
		roof_light_r1.texOffs(44, 144).addBox(-3, -1, -24, 3, 1, 48, 0, false);

		roof_end_light = new ModelMapper(modelDataWrapper);
		roof_end_light.setPos(0, 24, 0);


		roof_light_2_r1 = new ModelMapper(modelDataWrapper);
		roof_light_2_r1.setPos(-5.8F, -33.8F, 0);
		roof_end_light.addChild(roof_light_2_r1);
		setRotationAngle(roof_light_2_r1, 0, 0, 0.1309F);
		roof_light_2_r1.texOffs(56, 156).addBox(-3, -1, 4, 3, 1, 36, 0, false);

		roof_light_1_r1 = new ModelMapper(modelDataWrapper);
		roof_light_1_r1.setPos(5.8F, -33.8F, 0);
		roof_end_light.addChild(roof_light_1_r1);
		setRotationAngle(roof_light_1_r1, 0, 0, -0.1309F);
		roof_light_1_r1.texOffs(56, 156).addBox(0, -1, 4, 3, 1, 36, 0, true);

		head = new ModelMapper(modelDataWrapper);
		head.setPos(0, 24, 0);
		head.texOffs(184, 73).addBox(-20, 0, 4, 40, 1, 4, 0, false);
		head.texOffs(70, 143).addBox(-21, -14, 4, 4, 14, 7, 0, false);
		head.texOffs(190, 223).addBox(-18, -35, 4, 36, 35, 0, 0, false);

		upper_wall_2_r4 = new ModelMapper(modelDataWrapper);
		upper_wall_2_r4.setPos(-21, -14, 0);
		head.addChild(upper_wall_2_r4);
		setRotationAngle(upper_wall_2_r4, 0, 0, 0.1107F);
		upper_wall_2_r4.texOffs(48, 143).addBox(0, -19, 4, 4, 19, 7, 0, false);

		upper_wall_1_r3 = new ModelMapper(modelDataWrapper);
		upper_wall_1_r3.setPos(21, -14, 0);
		head.addChild(upper_wall_1_r3);
		setRotationAngle(upper_wall_1_r3, 0, 3.1416F, -0.1107F);
		upper_wall_1_r3.texOffs(170, 139).addBox(0, -19, -11, 4, 19, 7, 0, false);

		lower_wall_1_r2 = new ModelMapper(modelDataWrapper);
		lower_wall_1_r2.setPos(0, 0, 0);
		head.addChild(lower_wall_1_r2);
		setRotationAngle(lower_wall_1_r2, 0, 3.1416F, 0);
		lower_wall_1_r2.texOffs(158, 200).addBox(-21, -14, -11, 4, 14, 7, 0, false);

		head_exterior = new ModelMapper(modelDataWrapper);
		head_exterior.setPos(0, 24, 0);
		head_exterior.texOffs(10, 0).addBox(-21, 0, -31, 42, 7, 35, 0, false);
		head_exterior.texOffs(176, 0).addBox(20, 0, 4, 1, 4, 4, 0, true);
		head_exterior.texOffs(176, 0).addBox(-21, 0, 4, 1, 4, 4, 0, false);
		head_exterior.texOffs(120, 202).addBox(18, -14, -22, 3, 14, 32, 0, true);
		head_exterior.texOffs(120, 202).addBox(-21, -14, -22, 3, 14, 32, 0, false);
		head_exterior.texOffs(214, 0).addBox(-18, -41, 3, 36, 41, 0, 0, false);

		upper_wall_2_r5 = new ModelMapper(modelDataWrapper);
		upper_wall_2_r5.setPos(-21, -14, 0);
		head_exterior.addChild(upper_wall_2_r5);
		setRotationAngle(upper_wall_2_r5, 0, 0, 0.1107F);
		upper_wall_2_r5.texOffs(0, 192).addBox(0, -22, -22, 3, 22, 32, 0, false);

		upper_wall_1_r4 = new ModelMapper(modelDataWrapper);
		upper_wall_1_r4.setPos(21, -14, 0);
		head_exterior.addChild(upper_wall_1_r4);
		setRotationAngle(upper_wall_1_r4, 0, 0, -0.1107F);
		upper_wall_1_r4.texOffs(0, 192).addBox(-3, -22, -22, 3, 22, 32, 0, true);

		front = new ModelMapper(modelDataWrapper);
		front.setPos(0, 0, 0);
		head_exterior.addChild(front);
		front.texOffs(236, 215).addBox(-20, -8.8978F, -31.2245F, 40, 6, 0, 0, false);
		front.texOffs(0, 91).addBox(-20, 0, -32, 40, 2, 0, 0, false);

		front_bottom_2_r1 = new ModelMapper(modelDataWrapper);
		front_bottom_2_r1.setPos(0, 2, -32);
		front.addChild(front_bottom_2_r1);
		setRotationAngle(front_bottom_2_r1, 0.3491F, 0, 0);
		front_bottom_2_r1.texOffs(236, 209).addBox(-20, 0, 0, 40, 6, 0, 0, false);

		front_panel_4_r1 = new ModelMapper(modelDataWrapper);
		front_panel_4_r1.setPos(0, -29.5396F, -27.5854F);
		front.addChild(front_panel_4_r1);
		setRotationAngle(front_panel_4_r1, -0.2618F, 0, 0);
		front_panel_4_r1.texOffs(206, 156).addBox(-20, -11, 0, 40, 18, 0, 0, false);

		front_panel_3_r1 = new ModelMapper(modelDataWrapper);
		front_panel_3_r1.setPos(0, -15.8379F, -30.3109F);
		front.addChild(front_panel_3_r1);
		setRotationAngle(front_panel_3_r1, -0.1309F, 0, 0);
		front_panel_3_r1.texOffs(236, 195).addBox(-20, -7, 0, 40, 14, 0, 0, false);

		front_panel_1_r1 = new ModelMapper(modelDataWrapper);
		front_panel_1_r1.setPos(0, 0, -32);
		front.addChild(front_panel_1_r1);
		setRotationAngle(front_panel_1_r1, -0.2618F, 0, 0);
		front_panel_1_r1.texOffs(116, 197).addBox(-20, -3, 0, 40, 3, 0, 0, false);

		side_1 = new ModelMapper(modelDataWrapper);
		side_1.setPos(0, 0, 0);
		front.addChild(side_1);


		outer_roof_5_r4 = new ModelMapper(modelDataWrapper);
		outer_roof_5_r4.setPos(3.2289F, -40.6367F, -18.8935F);
		side_1.addChild(outer_roof_5_r4);
		setRotationAngle(outer_roof_5_r4, 0.1745F, 0, 0);
		outer_roof_5_r4.texOffs(16, 129).addBox(-3.5F, 0, -7, 6, 0, 14, 0, true);

		outer_roof_4_r4 = new ModelMapper(modelDataWrapper);
		outer_roof_4_r4.setPos(10.4418F, -39.7879F, -18.8937F);
		side_1.addChild(outer_roof_4_r4);
		setRotationAngle(outer_roof_4_r4, 0.1745F, 0, 0.1745F);
		outer_roof_4_r4.texOffs(84, 143).addBox(-5, 0, -7, 10, 0, 14, 0, true);

		outer_roof_3_r4 = new ModelMapper(modelDataWrapper);
		outer_roof_3_r4.setPos(13.583F, -39.4219F, -18.94F);
		side_1.addChild(outer_roof_3_r4);
		setRotationAngle(outer_roof_3_r4, 0.1309F, 0, 0.5236F);
		outer_roof_3_r4.texOffs(114, 0).addBox(-0.5F, 0, -8, 6, 0, 15, 0, true);

		outer_roof_2_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_2_r1.setPos(17.5562F, -37.0118F, -18.9933F);
		side_1.addChild(outer_roof_2_r1);
		setRotationAngle(outer_roof_2_r1, 0.0436F, 0, 1.0472F);
		outer_roof_2_r1.texOffs(129, 0).addBox(-1.5F, 0, -9, 4, 0, 16, 0, true);

		front_side_bottom_1_r1 = new ModelMapper(modelDataWrapper);
		front_side_bottom_1_r1.setPos(21, 0, -22);
		side_1.addChild(front_side_bottom_1_r1);
		setRotationAngle(front_side_bottom_1_r1, 0, 0.1745F, 0.1745F);
		front_side_bottom_1_r1.texOffs(48, 111).addBox(0, 0, -11, 0, 8, 18, 0, true);

		front_side_lower_1_r1 = new ModelMapper(modelDataWrapper);
		front_side_lower_1_r1.setPos(21, 0, -22);
		side_1.addChild(front_side_lower_1_r1);
		setRotationAngle(front_side_lower_1_r1, 0, 0.1745F, 0);
		front_side_lower_1_r1.texOffs(74, 182).addBox(0, -14, -11, 0, 14, 11, 0, true);

		front_side_upper_1_r1 = new ModelMapper(modelDataWrapper);
		front_side_upper_1_r1.setPos(21, -14, -22);
		side_1.addChild(front_side_upper_1_r1);
		setRotationAngle(front_side_upper_1_r1, 0, 0.1745F, -0.1107F);
		front_side_upper_1_r1.texOffs(98, 155).addBox(0, -23, -11, 0, 23, 11, 0, true);

		side_2 = new ModelMapper(modelDataWrapper);
		side_2.setPos(-21, 0, -9);
		front.addChild(side_2);


		outer_roof_5_r5 = new ModelMapper(modelDataWrapper);
		outer_roof_5_r5.setPos(17.7711F, -40.6367F, -9.8935F);
		side_2.addChild(outer_roof_5_r5);
		setRotationAngle(outer_roof_5_r5, 0.1745F, 0, 0);
		outer_roof_5_r5.texOffs(16, 129).addBox(-2.5F, 0, -7, 6, 0, 14, 0, false);

		outer_roof_4_r5 = new ModelMapper(modelDataWrapper);
		outer_roof_4_r5.setPos(10.5582F, -39.7879F, -9.8937F);
		side_2.addChild(outer_roof_4_r5);
		setRotationAngle(outer_roof_4_r5, 0.1745F, 0, -0.1745F);
		outer_roof_4_r5.texOffs(84, 143).addBox(-5, 0, -7, 10, 0, 14, 0, false);

		outer_roof_3_r5 = new ModelMapper(modelDataWrapper);
		outer_roof_3_r5.setPos(7.417F, -39.4219F, -9.94F);
		side_2.addChild(outer_roof_3_r5);
		setRotationAngle(outer_roof_3_r5, 0.1309F, 0, -0.5236F);
		outer_roof_3_r5.texOffs(114, 0).addBox(-5.5F, 0, -8, 6, 0, 15, 0, false);

		outer_roof_2_r2 = new ModelMapper(modelDataWrapper);
		outer_roof_2_r2.setPos(3.4438F, -37.0118F, -9.9933F);
		side_2.addChild(outer_roof_2_r2);
		setRotationAngle(outer_roof_2_r2, 0.0436F, 0, -1.0472F);
		outer_roof_2_r2.texOffs(129, 0).addBox(-2.5F, 0, -9, 4, 0, 16, 0, false);

		front_side_bottom_2_r1 = new ModelMapper(modelDataWrapper);
		front_side_bottom_2_r1.setPos(0, 0, -13);
		side_2.addChild(front_side_bottom_2_r1);
		setRotationAngle(front_side_bottom_2_r1, 0, -0.1745F, -0.1745F);
		front_side_bottom_2_r1.texOffs(48, 111).addBox(0, 0, -11, 0, 8, 18, 0, false);

		front_side_upper_2_r1 = new ModelMapper(modelDataWrapper);
		front_side_upper_2_r1.setPos(0, -14, -13);
		side_2.addChild(front_side_upper_2_r1);
		setRotationAngle(front_side_upper_2_r1, 0, -0.1745F, 0.1107F);
		front_side_upper_2_r1.texOffs(98, 155).addBox(0, -23, -11, 0, 23, 11, 0, false);

		front_side_lower_2_r1 = new ModelMapper(modelDataWrapper);
		front_side_lower_2_r1.setPos(0, 0, -13);
		side_2.addChild(front_side_lower_2_r1);
		setRotationAngle(front_side_lower_2_r1, 0, -0.1745F, 0);
		front_side_lower_2_r1.texOffs(74, 182).addBox(0, -14, -11, 0, 14, 11, 0, false);

		headlights = new ModelMapper(modelDataWrapper);
		headlights.setPos(0, 24, 0);
		headlights.texOffs(12, 56).addBox(9.5F, -8.8978F, -31.4F, 4, 4, 0, 0, true);
		headlights.texOffs(12, 56).addBox(-13.5F, -8.8978F, -31.4F, 4, 4, 0, 0, false);

		tail_lights = new ModelMapper(modelDataWrapper);
		tail_lights.setPos(0, 24, 0);
		tail_lights.texOffs(20, 56).addBox(13.5F, -8.8978F, -31.4F, 4, 4, 0, 0, true);
		tail_lights.texOffs(20, 56).addBox(-17.5F, -8.8978F, -31.4F, 4, 4, 0, 0, false);

		door_light = new ModelMapper(modelDataWrapper);
		door_light.setPos(0, 24, 0);


		outer_roof_3_r6 = new ModelMapper(modelDataWrapper);
		outer_roof_3_r6.setPos(-17.8206F, -37.1645F, 0);
		door_light.addChild(outer_roof_3_r6);
		setRotationAngle(outer_roof_3_r6, 0, 0, -1.0472F);
		outer_roof_3_r6.texOffs(25, 4).addBox(-1.5F, -0.1F, -1.5F, 3, 0, 3, 0, false);

		door_light_on = new ModelMapper(modelDataWrapper);
		door_light_on.setPos(0, 24, 0);


		light_r1 = new ModelMapper(modelDataWrapper);
		light_r1.setPos(-17.8206F, -37.1645F, 0);
		door_light_on.addChild(light_r1);
		setRotationAngle(light_r1, 0, 0, -1.0472F);
		light_r1.texOffs(12, 0).addBox(0, 0, 0, 0, 0, 0, 0.3F, false);

		door_light_off = new ModelMapper(modelDataWrapper);
		door_light_off.setPos(0, 24, 0);


		light_r2 = new ModelMapper(modelDataWrapper);
		light_r2.setPos(-17.8206F, -37.1645F, 0);
		door_light_off.addChild(light_r2);
		setRotationAngle(light_r2, 0, 0, -1.0472F);
		light_r2.texOffs(16, 0).addBox(0, 0, 0, 0, 0, 0, 0.3F, false);

		modelDataWrapper.setModelPart(textureWidth, textureHeight);
		window.setModelPart();
		window_handrail.setModelPart();
		window_exterior.setModelPart();
		window_exterior_end.setModelPart();
		side_panel.setModelPart();
		side_panel_translucent.setModelPart();
		roof_window.setModelPart();
		roof_door.setModelPart();
		roof_exterior.setModelPart();
		door.setModelPart();
		door_left.setModelPart(door.name);
		door_right.setModelPart(door.name);
		door_handrail.setModelPart();
		door_exterior.setModelPart();
		door_left_exterior.setModelPart(door_exterior.name);
		door_right_exterior.setModelPart(door_exterior.name);
		door_exterior_end.setModelPart();
		door_left_exterior_end.setModelPart(door_exterior_end.name);
		door_right_exterior_end.setModelPart(door_exterior_end.name);
		end.setModelPart();
		end_exterior.setModelPart();
		roof_end.setModelPart();
		roof_end_exterior.setModelPart();
		roof_light.setModelPart();
		roof_end_light.setModelPart();
		head.setModelPart();
		head_exterior.setModelPart();
		headlights.setModelPart();
		tail_lights.setModelPart();
		door_light.setModelPart();
		door_light_on.setModelPart();
		door_light_off.setModelPart();
	}

	private static final int DOOR_MAX = 13;
	private static final ModelDoorOverlay MODEL_DOOR_OVERLAY = new ModelDoorOverlay(DOOR_MAX, 6.34F, "door_overlay_k_train_left.png", "door_overlay_k_train_right.png");

	@Override
	public ModelKTrain createNew(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		return new ModelKTrain(isTcl, doorAnimationType, renderDoorOverlay);
	}

	@Override
	protected void renderWindowPositions(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
		switch (renderStage) {
			case LIGHTS:
				renderMirror(roof_light, matrices, vertices, light, position);
				break;
			case INTERIOR:
				renderMirror(window, matrices, vertices, light, position);
				if (renderDetails) {
					renderMirror(window_handrail, matrices, vertices, light, position);
					renderMirror(roof_window, matrices, vertices, light, position);
					renderMirror(side_panel, matrices, vertices, light, position - 22.1F);
					renderMirror(side_panel, matrices, vertices, light, position + 22.1F);
				}
				break;
			case INTERIOR_TRANSLUCENT:
				renderMirror(side_panel_translucent, matrices, vertices, light, position - 22.1F);
				renderMirror(side_panel_translucent, matrices, vertices, light, position + 22.1F);
				break;
			case EXTERIOR:
				if (isTcl && isIndex(0, position, getWindowPositions()) && isEnd1Head) {
					renderOnceFlipped(window_exterior_end, matrices, vertices, light, position);
				} else if (isTcl && isIndex(-1, position, getWindowPositions()) && isEnd2Head) {
					renderOnce(window_exterior_end, matrices, vertices, light, position);
				} else {
					renderMirror(window_exterior, matrices, vertices, light, position);
				}
				renderMirror(roof_exterior, matrices, vertices, light, position);
				break;
		}
	}

	@Override
	protected void renderDoorPositions(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
		final boolean firstDoor = isIndex(0, position, getDoorPositions());
		final boolean notLastDoor = !firstDoor && !isIndex(-1, position, getDoorPositions());
		final boolean doorOpen = doorLeftZ > 0 || doorRightZ > 0;

		switch (renderStage) {
			case LIGHTS:
				if (notLastDoor) {
					renderMirror(roof_light, matrices, vertices, light, position);
				}
				if (firstDoor && doorOpen) {
					renderMirror(door_light_on, matrices, vertices, light, 0);
				}
				break;
			case INTERIOR:
				door_left.setOffset(doorRightX, 0, doorRightZ);
				door_right.setOffset(doorRightX, 0, -doorRightZ);
				renderOnce(door, matrices, vertices, light, position);
				door_left.setOffset(doorLeftX, 0, doorLeftZ);
				door_right.setOffset(doorLeftX, 0, -doorLeftZ);
				renderOnceFlipped(door, matrices, vertices, light, position);

				if (renderDetails) {
					renderOnce(door_handrail, matrices, vertices, light, position);
					if (notLastDoor) {
						renderMirror(roof_door, matrices, vertices, light, position);
					}
				}
				break;
			case EXTERIOR:
				final boolean door1End = isIndex(0, position, getDoorPositions()) && isEnd1Head;
				final boolean door2End = isIndex(-1, position, getDoorPositions()) && isEnd2Head;

				if (isTcl && (door1End || door2End)) {
					door_left_exterior_end.setOffset(doorRightX, 0, doorRightZ);
					door_right_exterior_end.setOffset(doorRightX, 0, -doorRightZ);
					renderOnce(door_exterior_end, matrices, vertices, light, position);
				} else {
					door_left_exterior.setOffset(doorRightX, 0, doorRightZ);
					door_right_exterior.setOffset(doorRightX, 0, -doorRightZ);
					renderOnce(door_exterior, matrices, vertices, light, position);
				}

				if (isTcl && (door1End || door2End)) {
					door_left_exterior_end.setOffset(doorLeftX, 0, doorLeftZ);
					door_right_exterior_end.setOffset(doorLeftX, 0, -doorLeftZ);
					renderOnceFlipped(door_exterior_end, matrices, vertices, light, position);
				} else {
					door_left_exterior.setOffset(doorLeftX, 0, doorLeftZ);
					door_right_exterior.setOffset(doorLeftX, 0, -doorLeftZ);
					renderOnceFlipped(door_exterior, matrices, vertices, light, position);
				}

				renderMirror(roof_exterior, matrices, vertices, light, position);
				if (firstDoor && renderDetails) {
					renderMirror(door_light, matrices, vertices, light, 0);
					if (!doorOpen) {
						renderMirror(door_light_off, matrices, vertices, light, 0);
					}
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
				renderOnce(useHeadlights ? headlights : tail_lights, matrices, vertices, light, position);
				break;
			case INTERIOR:
				renderOnce(head, matrices, vertices, light, position);
				if (renderDetails) {
					renderOnce(roof_end, matrices, vertices, light, position);
				}
				break;
			case EXTERIOR:
				renderOnce(head_exterior, matrices, vertices, light, position);
				renderOnce(roof_end_exterior, matrices, vertices, light, position);
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
				renderOnceFlipped(useHeadlights ? headlights : tail_lights, matrices, vertices, light, position);
				break;
			case INTERIOR:
				renderOnceFlipped(head, matrices, vertices, light, position);
				if (renderDetails) {
					renderOnceFlipped(roof_end, matrices, vertices, light, position);
				}
				break;
			case EXTERIOR:
				renderOnceFlipped(head_exterior, matrices, vertices, light, position);
				renderOnceFlipped(roof_end_exterior, matrices, vertices, light, position);
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
	protected void renderEndPosition2(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
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
	protected int getDoorMax() {
		return DOOR_MAX;
	}

	@Override
	protected void renderTextDisplays(PoseStack matrices, MultiBufferSource vertexConsumers, Font font, MultiBufferSource.BufferSource immediate, Route thisRoute, Route nextRoute, Station thisStation, Station nextStation, Station lastStation, String customDestination, int car, int totalCars, boolean atPlatform, List<ScrollingText> scrollingTexts) {
		if (!isTcl) {
			renderFrontDestination(
					matrices, font, immediate,
					-0.8F, 0, getEndPositions()[0] / 16F - 2.21F, 0, -1.92F, -0.01F,
					-15, 0, 0.37F, 0.21F,
					0xFFFF9900, 0xFFFF0000, 2, getDestinationString(lastStation, customDestination, TextSpacingType.SPACE_CJK, true), true, car, totalCars
			);
		}
	}

	@Override
	protected String defaultDestinationString() {
		return "回廠|Depot";
	}
}