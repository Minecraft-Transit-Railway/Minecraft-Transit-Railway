package org.mtr.mod.model;

import org.mtr.core.data.InterchangeColorsForStationName;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.ModelPartExtension;
import org.mtr.mod.client.DoorAnimationType;
import org.mtr.mod.client.ScrollingText;
import org.mtr.mod.render.StoredMatrixTransformations;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ModelKTrain extends ModelSimpleTrainBase<ModelKTrain> {

	private final ModelPartExtension window;
	private final ModelPartExtension upper_wall_r1;
	private final ModelPartExtension window_handrail;
	private final ModelPartExtension handrail_8_r1;
	private final ModelPartExtension top_handrail_6_r1;
	private final ModelPartExtension top_handrail_5_r1;
	private final ModelPartExtension top_handrail_4_r1;
	private final ModelPartExtension top_handrail_3_r1;
	private final ModelPartExtension top_handrail_2_r1;
	private final ModelPartExtension top_handrail_1_r1;
	private final ModelPartExtension handrail_5_r1;
	private final ModelPartExtension upper_wall_2_r1;
	private final ModelPartExtension seat;
	private final ModelPartExtension seat_back_r1;
	private final ModelPartExtension window_exterior;
	private final ModelPartExtension upper_wall_r2;
	private final ModelPartExtension window_exterior_end;
	private final ModelPartExtension upper_wall_r3;
	private final ModelPartExtension upper_wall_r4;
	private final ModelPartExtension side_panel;
	private final ModelPartExtension side_panel_translucent;
	private final ModelPartExtension roof_window;
	private final ModelPartExtension inner_roof_4_r1;
	private final ModelPartExtension inner_roof_3_r1;
	private final ModelPartExtension roof_door;
	private final ModelPartExtension inner_roof_4_r2;
	private final ModelPartExtension inner_roof_3_r2;
	private final ModelPartExtension handrail_2_r1;
	private final ModelPartExtension roof_exterior;
	private final ModelPartExtension outer_roof_5_r1;
	private final ModelPartExtension outer_roof_4_r1;
	private final ModelPartExtension outer_roof_3_r1;
	private final ModelPartExtension door;
	private final ModelPartExtension door_left;
	private final ModelPartExtension door_left_top_r1;
	private final ModelPartExtension door_right;
	private final ModelPartExtension door_right_top_r1;
	private final ModelPartExtension door_handrail;
	private final ModelPartExtension door_exterior;
	private final ModelPartExtension upper_wall_r5;
	private final ModelPartExtension door_left_exterior;
	private final ModelPartExtension door_left_top_r2;
	private final ModelPartExtension door_right_exterior;
	private final ModelPartExtension door_right_top_r2;
	private final ModelPartExtension door_exterior_end;
	private final ModelPartExtension upper_wall_r6;
	private final ModelPartExtension door_left_exterior_end;
	private final ModelPartExtension door_left_top_r3;
	private final ModelPartExtension door_right_exterior_end;
	private final ModelPartExtension door_right_top_r3;
	private final ModelPartExtension end;
	private final ModelPartExtension upper_wall_2_r2;
	private final ModelPartExtension upper_wall_1_r1;
	private final ModelPartExtension lower_wall_1_r1;
	private final ModelPartExtension end_exterior;
	private final ModelPartExtension upper_wall_2_r3;
	private final ModelPartExtension upper_wall_1_r2;
	private final ModelPartExtension roof_end;
	private final ModelPartExtension handrail_2_r2;
	private final ModelPartExtension inner_roof_1;
	private final ModelPartExtension inner_roof_4_r3;
	private final ModelPartExtension inner_roof_3_r3;
	private final ModelPartExtension inner_roof_2;
	private final ModelPartExtension inner_roof_4_r4;
	private final ModelPartExtension inner_roof_3_r4;
	private final ModelPartExtension roof_end_exterior;
	private final ModelPartExtension vent_2_r1;
	private final ModelPartExtension vent_1_r1;
	private final ModelPartExtension outer_roof_1;
	private final ModelPartExtension outer_roof_5_r2;
	private final ModelPartExtension outer_roof_4_r2;
	private final ModelPartExtension outer_roof_3_r2;
	private final ModelPartExtension outer_roof_2;
	private final ModelPartExtension outer_roof_5_r3;
	private final ModelPartExtension outer_roof_4_r3;
	private final ModelPartExtension outer_roof_3_r3;
	private final ModelPartExtension roof_light;
	private final ModelPartExtension roof_light_r1;
	private final ModelPartExtension roof_end_light;
	private final ModelPartExtension roof_light_2_r1;
	private final ModelPartExtension roof_light_1_r1;
	private final ModelPartExtension head;
	private final ModelPartExtension upper_wall_2_r4;
	private final ModelPartExtension upper_wall_1_r3;
	private final ModelPartExtension lower_wall_1_r2;
	private final ModelPartExtension head_exterior;
	private final ModelPartExtension upper_wall_2_r5;
	private final ModelPartExtension upper_wall_1_r4;
	private final ModelPartExtension front;
	private final ModelPartExtension front_bottom_2_r1;
	private final ModelPartExtension front_panel_4_r1;
	private final ModelPartExtension front_panel_3_r1;
	private final ModelPartExtension front_panel_1_r1;
	private final ModelPartExtension side_1;
	private final ModelPartExtension outer_roof_5_r4;
	private final ModelPartExtension outer_roof_4_r4;
	private final ModelPartExtension outer_roof_3_r4;
	private final ModelPartExtension outer_roof_2_r1;
	private final ModelPartExtension front_side_bottom_1_r1;
	private final ModelPartExtension front_side_lower_1_r1;
	private final ModelPartExtension front_side_upper_1_r1;
	private final ModelPartExtension side_2;
	private final ModelPartExtension outer_roof_5_r5;
	private final ModelPartExtension outer_roof_4_r5;
	private final ModelPartExtension outer_roof_3_r5;
	private final ModelPartExtension outer_roof_2_r2;
	private final ModelPartExtension front_side_bottom_2_r1;
	private final ModelPartExtension front_side_upper_2_r1;
	private final ModelPartExtension front_side_lower_2_r1;
	private final ModelPartExtension headlights;
	private final ModelPartExtension tail_lights;
	private final ModelPartExtension door_light;
	private final ModelPartExtension outer_roof_3_r6;
	private final ModelPartExtension door_light_on;
	private final ModelPartExtension light_r1;
	private final ModelPartExtension door_light_off;
	private final ModelPartExtension light_r2;

	protected final boolean isTcl;

	public ModelKTrain(boolean isTcl) {
		this(isTcl, DoorAnimationType.PLUG_SLOW, true);
	}

	protected ModelKTrain(boolean isTcl, DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		super(320, 320, doorAnimationType, renderDoorOverlay);
		this.isTcl = isTcl;

		final int textureWidth = 320;
		final int textureHeight = 320;

		window = createModelPart();
		window.setPivot(0, 24, 0);
		window.setTextureUVOffset(0, 42).addCuboid(-20, 0, -24, 20, 1, 48, 0, false);

		upper_wall_r1 = createModelPart();
		upper_wall_r1.setPivot(-21, -14, 0);
		window.addChild(upper_wall_r1);
		setRotationAngle(upper_wall_r1, 0, 0, 0.1107F);
		upper_wall_r1.setTextureUVOffset(0, 129).addCuboid(1, -19, -22, 2, 19, 44, 0, false);

		window_handrail = createModelPart();
		window_handrail.setPivot(0, 24, 0);
		window_handrail.setTextureUVOffset(29, 0).addCuboid(-1, -32, -21, 2, 4, 0, 0, false);
		window_handrail.setTextureUVOffset(148, 169).addCuboid(-21, -14, 21, 4, 14, 6, 0, false);
		window_handrail.setTextureUVOffset(168, 52).addCuboid(-21, -14, -27, 4, 14, 6, 0, false);
		window_handrail.setTextureUVOffset(8, 0).addCuboid(0, -35, -9, 0, 35, 0, 0.2F, false);
		window_handrail.setTextureUVOffset(8, 0).addCuboid(0, -35, 9, 0, 35, 0, 0.2F, false);
		window_handrail.setTextureUVOffset(29, 0).addCuboid(-1, -32, 21, 2, 4, 0, 0, false);
		window_handrail.setTextureUVOffset(29, 0).addCuboid(-1, -32, 15, 2, 4, 0, 0, false);
		window_handrail.setTextureUVOffset(29, 0).addCuboid(-1, -32, 3, 2, 4, 0, 0, false);
		window_handrail.setTextureUVOffset(29, 0).addCuboid(-1, -32, -3, 2, 4, 0, 0, false);
		window_handrail.setTextureUVOffset(29, 0).addCuboid(-1, -32, -15, 2, 4, 0, 0, false);

		handrail_8_r1 = createModelPart();
		handrail_8_r1.setPivot(0, 0, 0);
		window_handrail.addChild(handrail_8_r1);
		setRotationAngle(handrail_8_r1, -1.5708F, 0, 0);
		handrail_8_r1.setTextureUVOffset(0, 0).addCuboid(0, -24, -31.5F, 0, 48, 0, 0.2F, false);

		top_handrail_6_r1 = createModelPart();
		top_handrail_6_r1.setPivot(-12.0518F, -29.0895F, 9.5876F);
		window_handrail.addChild(top_handrail_6_r1);
		setRotationAngle(top_handrail_6_r1, 1.5708F, 0, -0.0436F);
		top_handrail_6_r1.setTextureUVOffset(0, 0).addCuboid(0, -9.5F, 0, 0, 20, 0, 0.2F, false);

		top_handrail_5_r1 = createModelPart();
		top_handrail_5_r1.setPivot(-12.0377F, -28.7666F, 20.7938F);
		window_handrail.addChild(top_handrail_5_r1);
		setRotationAngle(top_handrail_5_r1, 1.0472F, 0, -0.0436F);
		top_handrail_5_r1.setTextureUVOffset(0, 0).addCuboid(0, -0.5F, 0, 0, 1, 0, 0.2F, false);

		top_handrail_4_r1 = createModelPart();
		top_handrail_4_r1.setPivot(-11.9992F, -27.8844F, 21.6768F);
		window_handrail.addChild(top_handrail_4_r1);
		setRotationAngle(top_handrail_4_r1, 0.5236F, 0, -0.0436F);
		top_handrail_4_r1.setTextureUVOffset(0, 0).addCuboid(0, -0.5F, 0, 0, 1, 0, 0.2F, false);

		top_handrail_3_r1 = createModelPart();
		top_handrail_3_r1.setPivot(-12.0518F, -29.0895F, -9.5876F);
		window_handrail.addChild(top_handrail_3_r1);
		setRotationAngle(top_handrail_3_r1, -1.5708F, 0, -0.0436F);
		top_handrail_3_r1.setTextureUVOffset(0, 0).addCuboid(0, -9.5F, 0, 0, 20, 0, 0.2F, false);

		top_handrail_2_r1 = createModelPart();
		top_handrail_2_r1.setPivot(-12.0377F, -28.7666F, -20.7938F);
		window_handrail.addChild(top_handrail_2_r1);
		setRotationAngle(top_handrail_2_r1, -1.0472F, 0, -0.0436F);
		top_handrail_2_r1.setTextureUVOffset(0, 0).addCuboid(0, -0.5F, 0, 0, 1, 0, 0.2F, false);

		top_handrail_1_r1 = createModelPart();
		top_handrail_1_r1.setPivot(-11.9992F, -27.8844F, -21.6768F);
		window_handrail.addChild(top_handrail_1_r1);
		setRotationAngle(top_handrail_1_r1, -0.5236F, 0, -0.0436F);
		top_handrail_1_r1.setTextureUVOffset(0, 0).addCuboid(0, -0.5F, 0, 0, 1, 0, 0.2F, false);

		handrail_5_r1 = createModelPart();
		handrail_5_r1.setPivot(-11, -5, 0);
		window_handrail.addChild(handrail_5_r1);
		setRotationAngle(handrail_5_r1, 0, 0, -0.0436F);
		handrail_5_r1.setTextureUVOffset(0, 0).addCuboid(0, -28.2F, -14, 0, 4, 0, 0.2F, false);
		handrail_5_r1.setTextureUVOffset(0, 0).addCuboid(0, -28.2F, 14, 0, 4, 0, 0.2F, false);
		handrail_5_r1.setTextureUVOffset(4, 0).addCuboid(0, -22.2F, 22, 0, 22, 0, 0.2F, false);
		handrail_5_r1.setTextureUVOffset(4, 0).addCuboid(0, -22.2F, -22, 0, 22, 0, 0.2F, false);

		upper_wall_2_r1 = createModelPart();
		upper_wall_2_r1.setPivot(-21, -14, 0);
		window_handrail.addChild(upper_wall_2_r1);
		setRotationAngle(upper_wall_2_r1, 0, 0, 0.1107F);
		upper_wall_2_r1.setTextureUVOffset(120, 166).addCuboid(0, -19, -27, 4, 19, 5, 0, false);
		upper_wall_2_r1.setTextureUVOffset(211, 91).addCuboid(0, -19, 22, 4, 19, 5, 0, false);

		seat = createModelPart();
		seat.setPivot(0, 0, 0);
		window_handrail.addChild(seat);
		seat.setTextureUVOffset(148, 145).addCuboid(-18, -6, -22, 7, 1, 44, 0, false);
		seat.setTextureUVOffset(188, 73).addCuboid(-18, -5, -21, 5, 5, 42, 0, false);

		seat_back_r1 = createModelPart();
		seat_back_r1.setPivot(-17, -6, 0);
		seat.addChild(seat_back_r1);
		setRotationAngle(seat_back_r1, 0, 0, -0.0524F);
		seat_back_r1.setTextureUVOffset(168, 0).addCuboid(-1, -8, -22, 1, 8, 44, 0, false);

		window_exterior = createModelPart();
		window_exterior.setPivot(0, 24, 0);
		window_exterior.setTextureUVOffset(98, 145).addCuboid(-21, 0, -24, 1, 4, 48, 0, false);
		window_exterior.setTextureUVOffset(0, 63).addCuboid(-21, -14, -26, 0, 14, 52, 0, false);

		upper_wall_r2 = createModelPart();
		upper_wall_r2.setPivot(-21, -14, 0);
		window_exterior.addChild(upper_wall_r2);
		setRotationAngle(upper_wall_r2, 0, 0, 0.1107F);
		upper_wall_r2.setTextureUVOffset(0, 41).addCuboid(0, -22, -26, 0, 22, 52, 0, false);

		setRotationAngle(upper_wall_r2, 0, 0, 0.1107F);
		upper_wall_r2.setTextureUVOffset(0, 41).addCuboid(0, -22, -26, 0, 22, 52, 0, false);

		window_exterior_end = createModelPart();
		window_exterior_end.setPivot(0, 24, 0);
		window_exterior_end.setTextureUVOffset(87, 258).addCuboid(-21, 0, -24, 1, 4, 48, 0, false);
		window_exterior_end.setTextureUVOffset(137, 228).addCuboid(-21, -14, -26, 0, 14, 52, 0, false);
		window_exterior_end.setTextureUVOffset(87, 258).addCuboid(20, 0, -24, 1, 4, 48, 0, true);
		window_exterior_end.setTextureUVOffset(137, 228).addCuboid(21, -14, -26, 0, 14, 52, 0, true);

		upper_wall_r3 = createModelPart();
		upper_wall_r3.setPivot(21, -14, 0);
		window_exterior_end.addChild(upper_wall_r3);
		setRotationAngle(upper_wall_r3, 0, 0, -0.1107F);
		upper_wall_r3.setTextureUVOffset(137, 206).addCuboid(0, -22, -26, 0, 22, 52, 0, true);

		upper_wall_r4 = createModelPart();
		upper_wall_r4.setPivot(-21, -14, 0);
		window_exterior_end.addChild(upper_wall_r4);
		setRotationAngle(upper_wall_r4, 0, 0, 0.1107F);
		upper_wall_r4.setTextureUVOffset(137, 206).addCuboid(0, -22, -26, 0, 22, 52, 0, false);

		side_panel = createModelPart();
		side_panel.setPivot(0, 24, 0);
		side_panel.setTextureUVOffset(30, 143).addCuboid(-18, -34, 0, 7, 30, 0, 0, false);

		side_panel_translucent = createModelPart();
		side_panel_translucent.setPivot(0, 24, 0);
		side_panel_translucent.setTextureUVOffset(293, 163).addCuboid(-18, -34, 0, 7, 30, 0, 0, false);

		roof_window = createModelPart();
		roof_window.setPivot(0, 24, 0);
		roof_window.setTextureUVOffset(40, 42).addCuboid(-16, -32, -24, 4, 0, 48, 0, false);
		roof_window.setTextureUVOffset(68, 42).addCuboid(-3, -34.5F, -24, 3, 0, 48, 0, false);

		inner_roof_4_r1 = createModelPart();
		inner_roof_4_r1.setPivot(-5.5473F, -34.2615F, 0);
		roof_window.addChild(inner_roof_4_r1);
		setRotationAngle(inner_roof_4_r1, 0, 0, -0.0873F);
		inner_roof_4_r1.setTextureUVOffset(56, 42).addCuboid(-3, 0, -24, 6, 0, 48, 0, false);

		inner_roof_3_r1 = createModelPart();
		inner_roof_3_r1.setPivot(-12, -32, 0);
		roof_window.addChild(inner_roof_3_r1);
		setRotationAngle(inner_roof_3_r1, 0, 0, -0.5236F);
		inner_roof_3_r1.setTextureUVOffset(48, 42).addCuboid(0, 0, -24, 4, 0, 48, 0, false);

		roof_door = createModelPart();
		roof_door.setPivot(0, 24, 0);
		roof_door.setTextureUVOffset(132, 10).addCuboid(-18, -33, -16, 6, 1, 32, 0, false);
		roof_door.setTextureUVOffset(84, 50).addCuboid(-3, -34.5F, -16, 3, 0, 32, 0, false);

		inner_roof_4_r2 = createModelPart();
		inner_roof_4_r2.setPivot(-5.5473F, -34.2615F, 0);
		roof_door.addChild(inner_roof_4_r2);
		setRotationAngle(inner_roof_4_r2, 0, 0, -0.0873F);
		inner_roof_4_r2.setTextureUVOffset(72, 50).addCuboid(-3, 0, -16, 6, 0, 32, 0, false);

		inner_roof_3_r2 = createModelPart();
		inner_roof_3_r2.setPivot(-12, -32, 0);
		roof_door.addChild(inner_roof_3_r2);
		setRotationAngle(inner_roof_3_r2, 0, 0, -0.5236F);
		inner_roof_3_r2.setTextureUVOffset(128, 93).addCuboid(0, 0, -16, 4, 0, 32, 0, true);

		handrail_2_r1 = createModelPart();
		handrail_2_r1.setPivot(0, 0, 0);
		roof_door.addChild(handrail_2_r1);
		setRotationAngle(handrail_2_r1, -1.5708F, 0, 0);
		handrail_2_r1.setTextureUVOffset(0, 0).addCuboid(0, -16, -31.5F, 0, 32, 0, 0.2F, false);

		roof_exterior = createModelPart();
		roof_exterior.setPivot(0, 24, 0);
		roof_exterior.setTextureUVOffset(82, 43).addCuboid(-5.728F, -41.8527F, -20, 6, 0, 40, 0, false);

		outer_roof_5_r1 = createModelPart();
		outer_roof_5_r1.setPivot(-9.6672F, -41.1581F, 0);
		roof_exterior.addChild(outer_roof_5_r1);
		setRotationAngle(outer_roof_5_r1, 0, 0, -0.1745F);
		outer_roof_5_r1.setTextureUVOffset(82, 93).addCuboid(-4, 0, -20, 8, 0, 40, 0, false);

		outer_roof_4_r1 = createModelPart();
		outer_roof_4_r1.setPivot(-15.3385F, -39.4635F, 0);
		roof_exterior.addChild(outer_roof_4_r1);
		setRotationAngle(outer_roof_4_r1, 0, 0, -0.5236F);
		outer_roof_4_r1.setTextureUVOffset(0, 42).addCuboid(-2, 0, -20, 4, 0, 40, 0, false);

		outer_roof_3_r1 = createModelPart();
		outer_roof_3_r1.setPivot(-17.8206F, -37.1645F, 0);
		roof_exterior.addChild(outer_roof_3_r1);
		setRotationAngle(outer_roof_3_r1, 0, 0, -1.0472F);
		outer_roof_3_r1.setTextureUVOffset(98, 93).addCuboid(-1.5F, 0, -20, 3, 0, 40, 0, false);

		door = createModelPart();
		door.setPivot(0, 24, 0);
		door.setTextureUVOffset(164, 190).addCuboid(-20, 0, -16, 20, 1, 32, 0, false);

		door_left = createModelPart();
		door_left.setPivot(0, 0, 0);
		door.addChild(door_left);
		door_left.setTextureUVOffset(116, 200).addCuboid(-21, -14, 0, 1, 14, 14, 0, false);

		door_left_top_r1 = createModelPart();
		door_left_top_r1.setPivot(-21, -14, 0);
		door_left.addChild(door_left_top_r1);
		setRotationAngle(door_left_top_r1, 0, 0, 0.1107F);
		door_left_top_r1.setTextureUVOffset(274, 59).addCuboid(0, -19, 0, 1, 19, 14, 0, false);

		door_right = createModelPart();
		door_right.setPivot(0, 0, 0);
		door.addChild(door_right);
		door_right.setTextureUVOffset(0, 192).addCuboid(-21, -14, -14, 1, 14, 14, 0, false);

		door_right_top_r1 = createModelPart();
		door_right_top_r1.setPivot(-21, -14, 0);
		door_right.addChild(door_right_top_r1);
		setRotationAngle(door_right_top_r1, 0, 0, 0.1107F);
		door_right_top_r1.setTextureUVOffset(178, 0).addCuboid(0, -19, -14, 1, 19, 14, 0, false);

		door_handrail = createModelPart();
		door_handrail.setPivot(0, 24, 0);
		door_handrail.setTextureUVOffset(8, 0).addCuboid(0, -35, 0, 0, 35, 0, 0.2F, false);

		door_exterior = createModelPart();
		door_exterior.setPivot(0, 24, 0);
		door_exterior.setTextureUVOffset(114, 161).addCuboid(-21, 0, -16, 1, 4, 32, 0, false);

		upper_wall_r5 = createModelPart();
		upper_wall_r5.setPivot(-21, -14, 0);
		door_exterior.addChild(upper_wall_r5);
		setRotationAngle(upper_wall_r5, 0, 0, 0.1107F);
		upper_wall_r5.setTextureUVOffset(77, 259).addCuboid(0, -22, -14, 1, 3, 28, 0, false);

		door_left_exterior = createModelPart();
		door_left_exterior.setPivot(0, 0, 0);
		door_exterior.addChild(door_left_exterior);
		door_left_exterior.setTextureUVOffset(0, 135).addCuboid(-21, -14, 0, 0, 14, 14, 0, false);

		door_left_top_r2 = createModelPart();
		door_left_top_r2.setPivot(-21, -14, 0);
		door_left_exterior.addChild(door_left_top_r2);
		setRotationAngle(door_left_top_r2, 0, 0, 0.1107F);
		door_left_top_r2.setTextureUVOffset(0, 115).addCuboid(0, -20, 0, 0, 20, 14, 0, false);

		door_right_exterior = createModelPart();
		door_right_exterior.setPivot(0, 0, 0);
		door_exterior.addChild(door_right_exterior);
		door_right_exterior.setTextureUVOffset(262, 229).addCuboid(-21, -14, -14, 0, 14, 14, 0, false);

		door_right_top_r2 = createModelPart();
		door_right_top_r2.setPivot(-21, -14, 0);
		door_right_exterior.addChild(door_right_top_r2);
		setRotationAngle(door_right_top_r2, 0, 0, 0.1107F);
		door_right_top_r2.setTextureUVOffset(262, 209).addCuboid(0, -20, -14, 0, 20, 14, 0, false);

		door_exterior_end = createModelPart();
		door_exterior_end.setPivot(0, 24, 0);
		door_exterior_end.setTextureUVOffset(0, 282).addCuboid(-21, 0, -16, 1, 4, 32, 0, false);

		upper_wall_r6 = createModelPart();
		upper_wall_r6.setPivot(-21, -14, 0);
		door_exterior_end.addChild(upper_wall_r6);
		setRotationAngle(upper_wall_r6, 0, 0, 0.1107F);
		upper_wall_r6.setTextureUVOffset(77, 259).addCuboid(0, -22, -14, 1, 3, 28, 0, false);

		door_left_exterior_end = createModelPart();
		door_left_exterior_end.setPivot(0, 0, 0);
		door_exterior_end.addChild(door_left_exterior_end);
		door_left_exterior_end.setTextureUVOffset(0, 268).addCuboid(-21, -14, 0, 0, 14, 14, 0, false);

		door_left_top_r3 = createModelPart();
		door_left_top_r3.setPivot(-21, -14, 0);
		door_left_exterior_end.addChild(door_left_top_r3);
		setRotationAngle(door_left_top_r3, 0, 0, 0.1107F);
		door_left_top_r3.setTextureUVOffset(0, 115).addCuboid(0, -20, 0, 0, 20, 14, 0, false);

		door_right_exterior_end = createModelPart();
		door_right_exterior_end.setPivot(0, 0, 0);
		door_exterior_end.addChild(door_right_exterior_end);
		door_right_exterior_end.setTextureUVOffset(0, 282).addCuboid(-21, -14, -14, 0, 14, 14, 0, false);

		door_right_top_r3 = createModelPart();
		door_right_top_r3.setPivot(-21, -14, 0);
		door_right_exterior_end.addChild(door_right_top_r3);
		setRotationAngle(door_right_top_r3, 0, 0, 0.1107F);
		door_right_top_r3.setTextureUVOffset(262, 209).addCuboid(0, -20, -14, 0, 20, 14, 0, false);

		end = createModelPart();
		end.setPivot(0, 24, 0);
		end.setTextureUVOffset(168, 52).addCuboid(-20, 0, -12, 40, 1, 20, 0, false);
		end.setTextureUVOffset(148, 169).addCuboid(-21, -14, 5, 4, 14, 6, 0, false);
		end.setTextureUVOffset(51, 234).addCuboid(9.5F, -34, -12, 8, 34, 19, 0, true);
		end.setTextureUVOffset(51, 234).addCuboid(-17.5F, -34, -12, 8, 34, 19, 0, false);
		end.setTextureUVOffset(236, 174).addCuboid(-9.5F, -35, -12, 19, 2, 19, 0, false);

		upper_wall_2_r2 = createModelPart();
		upper_wall_2_r2.setPivot(-21, -14, 0);
		end.addChild(upper_wall_2_r2);
		setRotationAngle(upper_wall_2_r2, 0, 0, 0.1107F);
		upper_wall_2_r2.setTextureUVOffset(211, 91).addCuboid(0, -19, 6, 4, 19, 5, 0, false);

		upper_wall_1_r1 = createModelPart();
		upper_wall_1_r1.setPivot(21, -14, 0);
		end.addChild(upper_wall_1_r1);
		setRotationAngle(upper_wall_1_r1, 0, 3.1416F, -0.1107F);
		upper_wall_1_r1.setTextureUVOffset(120, 166).addCuboid(0, -19, -11, 4, 19, 5, 0, false);

		lower_wall_1_r1 = createModelPart();
		lower_wall_1_r1.setPivot(0, 0, 0);
		end.addChild(lower_wall_1_r1);
		setRotationAngle(lower_wall_1_r1, 0, 3.1416F, 0);
		lower_wall_1_r1.setTextureUVOffset(168, 52).addCuboid(-21, -14, -11, 4, 14, 6, 0, false);

		end_exterior = createModelPart();
		end_exterior.setPivot(0, 24, 0);
		end_exterior.setTextureUVOffset(148, 145).addCuboid(20, 0, -12, 1, 4, 20, 0, true);
		end_exterior.setTextureUVOffset(148, 145).addCuboid(-21, 0, -12, 1, 4, 20, 0, false);
		end_exterior.setTextureUVOffset(0, 246).addCuboid(18, -14, -12, 3, 14, 22, 0, true);
		end_exterior.setTextureUVOffset(0, 246).addCuboid(-21, -14, -12, 3, 14, 22, 0, false);
		end_exterior.setTextureUVOffset(248, 120).addCuboid(9.5F, -34, -12, 10, 34, 0, 0, true);
		end_exterior.setTextureUVOffset(248, 120).addCuboid(-19.5F, -34, -12, 10, 34, 0, 0, false);
		end_exterior.setTextureUVOffset(105, 248).addCuboid(-18, -41, -12, 36, 7, 0, 0, false);

		upper_wall_2_r3 = createModelPart();
		upper_wall_2_r3.setPivot(-21, -14, 0);
		end_exterior.addChild(upper_wall_2_r3);
		setRotationAngle(upper_wall_2_r3, 0, 0, 0.1107F);
		upper_wall_2_r3.setTextureUVOffset(268, 98).addCuboid(0, -22, -12, 3, 22, 22, 0, false);

		upper_wall_1_r2 = createModelPart();
		upper_wall_1_r2.setPivot(21, -14, 0);
		end_exterior.addChild(upper_wall_1_r2);
		setRotationAngle(upper_wall_1_r2, 0, 0, -0.1107F);
		upper_wall_1_r2.setTextureUVOffset(268, 98).addCuboid(-3, -22, -12, 3, 22, 22, 0, true);

		roof_end = createModelPart();
		roof_end.setPivot(0, 24, 0);


		handrail_2_r2 = createModelPart();
		handrail_2_r2.setPivot(0, 0, 0);
		roof_end.addChild(handrail_2_r2);
		setRotationAngle(handrail_2_r2, -1.5708F, 0, 0);
		handrail_2_r2.setTextureUVOffset(0, 0).addCuboid(0, -40, -31.5F, 0, 16, 0, 0.2F, false);

		inner_roof_1 = createModelPart();
		inner_roof_1.setPivot(-2, -33, 16);
		roof_end.addChild(inner_roof_1);
		inner_roof_1.setTextureUVOffset(128, 6).addCuboid(-16, 0, -12, 6, 1, 36, 0, false);
		inner_roof_1.setTextureUVOffset(80, 42).addCuboid(-1, -1.5F, -12, 3, 0, 36, 0, false);

		inner_roof_4_r3 = createModelPart();
		inner_roof_4_r3.setPivot(-3.5473F, -1.2615F, -16);
		inner_roof_1.addChild(inner_roof_4_r3);
		setRotationAngle(inner_roof_4_r3, 0, 0, -0.0873F);
		inner_roof_4_r3.setTextureUVOffset(68, 42).addCuboid(-3, 0, 4, 6, 0, 36, 0, false);

		inner_roof_3_r3 = createModelPart();
		inner_roof_3_r3.setPivot(-10, 1, -16);
		inner_roof_1.addChild(inner_roof_3_r3);
		setRotationAngle(inner_roof_3_r3, 0, 0, -0.5236F);
		inner_roof_3_r3.setTextureUVOffset(116, 93).addCuboid(0, 0, 4, 4, 0, 36, 0, true);

		inner_roof_2 = createModelPart();
		inner_roof_2.setPivot(-2, -33, 16);
		roof_end.addChild(inner_roof_2);
		inner_roof_2.setTextureUVOffset(80, 42).addCuboid(2, -1.5F, -12, 3, 0, 36, 0, true);
		inner_roof_2.setTextureUVOffset(128, 6).addCuboid(14, 0, -12, 6, 1, 36, 0, true);

		inner_roof_4_r4 = createModelPart();
		inner_roof_4_r4.setPivot(7.5473F, -1.2615F, -16);
		inner_roof_2.addChild(inner_roof_4_r4);
		setRotationAngle(inner_roof_4_r4, 0, 0, 0.0873F);
		inner_roof_4_r4.setTextureUVOffset(68, 42).addCuboid(-3, 0, 4, 6, 0, 36, 0, true);

		inner_roof_3_r4 = createModelPart();
		inner_roof_3_r4.setPivot(14, 1, -16);
		inner_roof_2.addChild(inner_roof_3_r4);
		setRotationAngle(inner_roof_3_r4, 0, 0, 0.5236F);
		inner_roof_3_r4.setTextureUVOffset(108, 93).addCuboid(-4, 0, 4, 4, 0, 36, 0, true);

		roof_end_exterior = createModelPart();
		roof_end_exterior.setPivot(0, 24, 0);
		roof_end_exterior.setTextureUVOffset(88, 43).addCuboid(-8, -43, 0, 16, 2, 48, 0, false);

		vent_2_r1 = createModelPart();
		vent_2_r1.setPivot(-8, -43, 0);
		roof_end_exterior.addChild(vent_2_r1);
		setRotationAngle(vent_2_r1, 0, 0, -0.3491F);
		vent_2_r1.setTextureUVOffset(56, 93).addCuboid(-9, 0, 0, 9, 2, 48, 0, false);

		vent_1_r1 = createModelPart();
		vent_1_r1.setPivot(8, -43, 0);
		roof_end_exterior.addChild(vent_1_r1);
		setRotationAngle(vent_1_r1, 0, 0, 0.3491F);
		vent_1_r1.setTextureUVOffset(56, 93).addCuboid(0, 0, 0, 9, 2, 48, 0, true);

		outer_roof_1 = createModelPart();
		outer_roof_1.setPivot(0, 0, 0);
		roof_end_exterior.addChild(outer_roof_1);
		outer_roof_1.setTextureUVOffset(188, 120).addCuboid(-5.7289F, -41.8532F, -12, 6, 1, 20, 0, false);

		outer_roof_5_r2 = createModelPart();
		outer_roof_5_r2.setPivot(-9.4945F, -40.1738F, -8);
		outer_roof_1.addChild(outer_roof_5_r2);
		setRotationAngle(outer_roof_5_r2, 0, 0, -0.1745F);
		outer_roof_5_r2.setTextureUVOffset(38, 193).addCuboid(-4, -1, -4, 8, 1, 20, 0, false);

		outer_roof_4_r2 = createModelPart();
		outer_roof_4_r2.setPivot(-14.4064F, -38.848F, -8);
		outer_roof_1.addChild(outer_roof_4_r2);
		setRotationAngle(outer_roof_4_r2, 0, 0, -0.5236F);
		outer_roof_4_r2.setTextureUVOffset(0, 62).addCuboid(-2.5F, -1, -4, 4, 1, 20, 0, false);

		outer_roof_3_r2 = createModelPart();
		outer_roof_3_r2.setPivot(-16.7054F, -37.098F, -8);
		outer_roof_1.addChild(outer_roof_3_r2);
		setRotationAngle(outer_roof_3_r2, 0, 0, -1.0472F);
		outer_roof_3_r2.setTextureUVOffset(98, 145).addCuboid(-2, -1, -4, 3, 1, 20, 0, false);

		outer_roof_2 = createModelPart();
		outer_roof_2.setPivot(0, 0, 0);
		roof_end_exterior.addChild(outer_roof_2);
		outer_roof_2.setTextureUVOffset(188, 120).addCuboid(-0.2711F, -41.8532F, -12, 6, 1, 20, 0, true);

		outer_roof_5_r3 = createModelPart();
		outer_roof_5_r3.setPivot(9.4945F, -40.1738F, -8);
		outer_roof_2.addChild(outer_roof_5_r3);
		setRotationAngle(outer_roof_5_r3, 0, 0, 0.1745F);
		outer_roof_5_r3.setTextureUVOffset(38, 193).addCuboid(-4, -1, -4, 8, 1, 20, 0, true);

		outer_roof_4_r3 = createModelPart();
		outer_roof_4_r3.setPivot(15.2724F, -38.348F, -8);
		outer_roof_2.addChild(outer_roof_4_r3);
		setRotationAngle(outer_roof_4_r3, 0, 0, 0.5236F);
		outer_roof_4_r3.setTextureUVOffset(0, 62).addCuboid(-2.5F, -1, -4, 4, 1, 20, 0, true);

		outer_roof_3_r3 = createModelPart();
		outer_roof_3_r3.setPivot(16.7054F, -37.098F, -8);
		outer_roof_2.addChild(outer_roof_3_r3);
		setRotationAngle(outer_roof_3_r3, 0, 0, 1.0472F);
		outer_roof_3_r3.setTextureUVOffset(98, 145).addCuboid(-1, -1, -4, 3, 1, 20, 0, true);

		roof_light = createModelPart();
		roof_light.setPivot(0, 24, 0);


		roof_light_r1 = createModelPart();
		roof_light_r1.setPivot(-5.8F, -33.8F, 0);
		roof_light.addChild(roof_light_r1);
		setRotationAngle(roof_light_r1, 0, 0, 0.1309F);
		roof_light_r1.setTextureUVOffset(44, 144).addCuboid(-3, -1, -24, 3, 1, 48, 0, false);

		roof_end_light = createModelPart();
		roof_end_light.setPivot(0, 24, 0);


		roof_light_2_r1 = createModelPart();
		roof_light_2_r1.setPivot(-5.8F, -33.8F, 0);
		roof_end_light.addChild(roof_light_2_r1);
		setRotationAngle(roof_light_2_r1, 0, 0, 0.1309F);
		roof_light_2_r1.setTextureUVOffset(56, 156).addCuboid(-3, -1, 4, 3, 1, 36, 0, false);

		roof_light_1_r1 = createModelPart();
		roof_light_1_r1.setPivot(5.8F, -33.8F, 0);
		roof_end_light.addChild(roof_light_1_r1);
		setRotationAngle(roof_light_1_r1, 0, 0, -0.1309F);
		roof_light_1_r1.setTextureUVOffset(56, 156).addCuboid(0, -1, 4, 3, 1, 36, 0, true);

		head = createModelPart();
		head.setPivot(0, 24, 0);
		head.setTextureUVOffset(184, 73).addCuboid(-20, 0, 4, 40, 1, 4, 0, false);
		head.setTextureUVOffset(70, 143).addCuboid(-21, -14, 4, 4, 14, 7, 0, false);
		head.setTextureUVOffset(190, 223).addCuboid(-18, -35, 4, 36, 35, 0, 0, false);

		upper_wall_2_r4 = createModelPart();
		upper_wall_2_r4.setPivot(-21, -14, 0);
		head.addChild(upper_wall_2_r4);
		setRotationAngle(upper_wall_2_r4, 0, 0, 0.1107F);
		upper_wall_2_r4.setTextureUVOffset(48, 143).addCuboid(0, -19, 4, 4, 19, 7, 0, false);

		upper_wall_1_r3 = createModelPart();
		upper_wall_1_r3.setPivot(21, -14, 0);
		head.addChild(upper_wall_1_r3);
		setRotationAngle(upper_wall_1_r3, 0, 3.1416F, -0.1107F);
		upper_wall_1_r3.setTextureUVOffset(170, 139).addCuboid(0, -19, -11, 4, 19, 7, 0, false);

		lower_wall_1_r2 = createModelPart();
		lower_wall_1_r2.setPivot(0, 0, 0);
		head.addChild(lower_wall_1_r2);
		setRotationAngle(lower_wall_1_r2, 0, 3.1416F, 0);
		lower_wall_1_r2.setTextureUVOffset(158, 200).addCuboid(-21, -14, -11, 4, 14, 7, 0, false);

		head_exterior = createModelPart();
		head_exterior.setPivot(0, 24, 0);
		head_exterior.setTextureUVOffset(10, 0).addCuboid(-21, 0, -31, 42, 7, 35, 0, false);
		head_exterior.setTextureUVOffset(176, 0).addCuboid(20, 0, 4, 1, 4, 4, 0, true);
		head_exterior.setTextureUVOffset(176, 0).addCuboid(-21, 0, 4, 1, 4, 4, 0, false);
		head_exterior.setTextureUVOffset(120, 202).addCuboid(18, -14, -22, 3, 14, 32, 0, true);
		head_exterior.setTextureUVOffset(120, 202).addCuboid(-21, -14, -22, 3, 14, 32, 0, false);
		head_exterior.setTextureUVOffset(214, 0).addCuboid(-18, -41, 3, 36, 41, 0, 0, false);

		upper_wall_2_r5 = createModelPart();
		upper_wall_2_r5.setPivot(-21, -14, 0);
		head_exterior.addChild(upper_wall_2_r5);
		setRotationAngle(upper_wall_2_r5, 0, 0, 0.1107F);
		upper_wall_2_r5.setTextureUVOffset(0, 192).addCuboid(0, -22, -22, 3, 22, 32, 0, false);

		upper_wall_1_r4 = createModelPart();
		upper_wall_1_r4.setPivot(21, -14, 0);
		head_exterior.addChild(upper_wall_1_r4);
		setRotationAngle(upper_wall_1_r4, 0, 0, -0.1107F);
		upper_wall_1_r4.setTextureUVOffset(0, 192).addCuboid(-3, -22, -22, 3, 22, 32, 0, true);

		front = createModelPart();
		front.setPivot(0, 0, 0);
		head_exterior.addChild(front);
		front.setTextureUVOffset(236, 215).addCuboid(-20, -8.8978F, -31.2245F, 40, 6, 0, 0, false);
		front.setTextureUVOffset(0, 91).addCuboid(-20, 0, -32, 40, 2, 0, 0, false);

		front_bottom_2_r1 = createModelPart();
		front_bottom_2_r1.setPivot(0, 2, -32);
		front.addChild(front_bottom_2_r1);
		setRotationAngle(front_bottom_2_r1, 0.3491F, 0, 0);
		front_bottom_2_r1.setTextureUVOffset(236, 209).addCuboid(-20, 0, 0, 40, 6, 0, 0, false);

		front_panel_4_r1 = createModelPart();
		front_panel_4_r1.setPivot(0, -29.5396F, -27.5854F);
		front.addChild(front_panel_4_r1);
		setRotationAngle(front_panel_4_r1, -0.2618F, 0, 0);
		front_panel_4_r1.setTextureUVOffset(206, 156).addCuboid(-20, -11, 0, 40, 18, 0, 0, false);

		front_panel_3_r1 = createModelPart();
		front_panel_3_r1.setPivot(0, -15.8379F, -30.3109F);
		front.addChild(front_panel_3_r1);
		setRotationAngle(front_panel_3_r1, -0.1309F, 0, 0);
		front_panel_3_r1.setTextureUVOffset(236, 195).addCuboid(-20, -7, 0, 40, 14, 0, 0, false);

		front_panel_1_r1 = createModelPart();
		front_panel_1_r1.setPivot(0, 0, -32);
		front.addChild(front_panel_1_r1);
		setRotationAngle(front_panel_1_r1, -0.2618F, 0, 0);
		front_panel_1_r1.setTextureUVOffset(116, 197).addCuboid(-20, -3, 0, 40, 3, 0, 0, false);

		side_1 = createModelPart();
		side_1.setPivot(0, 0, 0);
		front.addChild(side_1);


		outer_roof_5_r4 = createModelPart();
		outer_roof_5_r4.setPivot(3.2289F, -40.6367F, -18.8935F);
		side_1.addChild(outer_roof_5_r4);
		setRotationAngle(outer_roof_5_r4, 0.1745F, 0, 0);
		outer_roof_5_r4.setTextureUVOffset(16, 129).addCuboid(-3.5F, 0, -7, 6, 0, 14, 0, true);

		outer_roof_4_r4 = createModelPart();
		outer_roof_4_r4.setPivot(10.4418F, -39.7879F, -18.8937F);
		side_1.addChild(outer_roof_4_r4);
		setRotationAngle(outer_roof_4_r4, 0.1745F, 0, 0.1745F);
		outer_roof_4_r4.setTextureUVOffset(84, 143).addCuboid(-5, 0, -7, 10, 0, 14, 0, true);

		outer_roof_3_r4 = createModelPart();
		outer_roof_3_r4.setPivot(13.583F, -39.4219F, -18.94F);
		side_1.addChild(outer_roof_3_r4);
		setRotationAngle(outer_roof_3_r4, 0.1309F, 0, 0.5236F);
		outer_roof_3_r4.setTextureUVOffset(114, 0).addCuboid(-0.5F, 0, -8, 6, 0, 15, 0, true);

		outer_roof_2_r1 = createModelPart();
		outer_roof_2_r1.setPivot(17.5562F, -37.0118F, -18.9933F);
		side_1.addChild(outer_roof_2_r1);
		setRotationAngle(outer_roof_2_r1, 0.0436F, 0, 1.0472F);
		outer_roof_2_r1.setTextureUVOffset(129, 0).addCuboid(-1.5F, 0, -9, 4, 0, 16, 0, true);

		front_side_bottom_1_r1 = createModelPart();
		front_side_bottom_1_r1.setPivot(21, 0, -22);
		side_1.addChild(front_side_bottom_1_r1);
		setRotationAngle(front_side_bottom_1_r1, 0, 0.1745F, 0.1745F);
		front_side_bottom_1_r1.setTextureUVOffset(48, 111).addCuboid(0, 0, -11, 0, 8, 18, 0, true);

		front_side_lower_1_r1 = createModelPart();
		front_side_lower_1_r1.setPivot(21, 0, -22);
		side_1.addChild(front_side_lower_1_r1);
		setRotationAngle(front_side_lower_1_r1, 0, 0.1745F, 0);
		front_side_lower_1_r1.setTextureUVOffset(74, 182).addCuboid(0, -14, -11, 0, 14, 11, 0, true);

		front_side_upper_1_r1 = createModelPart();
		front_side_upper_1_r1.setPivot(21, -14, -22);
		side_1.addChild(front_side_upper_1_r1);
		setRotationAngle(front_side_upper_1_r1, 0, 0.1745F, -0.1107F);
		front_side_upper_1_r1.setTextureUVOffset(98, 155).addCuboid(0, -23, -11, 0, 23, 11, 0, true);

		side_2 = createModelPart();
		side_2.setPivot(-21, 0, -9);
		front.addChild(side_2);


		outer_roof_5_r5 = createModelPart();
		outer_roof_5_r5.setPivot(17.7711F, -40.6367F, -9.8935F);
		side_2.addChild(outer_roof_5_r5);
		setRotationAngle(outer_roof_5_r5, 0.1745F, 0, 0);
		outer_roof_5_r5.setTextureUVOffset(16, 129).addCuboid(-2.5F, 0, -7, 6, 0, 14, 0, false);

		outer_roof_4_r5 = createModelPart();
		outer_roof_4_r5.setPivot(10.5582F, -39.7879F, -9.8937F);
		side_2.addChild(outer_roof_4_r5);
		setRotationAngle(outer_roof_4_r5, 0.1745F, 0, -0.1745F);
		outer_roof_4_r5.setTextureUVOffset(84, 143).addCuboid(-5, 0, -7, 10, 0, 14, 0, false);

		outer_roof_3_r5 = createModelPart();
		outer_roof_3_r5.setPivot(7.417F, -39.4219F, -9.94F);
		side_2.addChild(outer_roof_3_r5);
		setRotationAngle(outer_roof_3_r5, 0.1309F, 0, -0.5236F);
		outer_roof_3_r5.setTextureUVOffset(114, 0).addCuboid(-5.5F, 0, -8, 6, 0, 15, 0, false);

		outer_roof_2_r2 = createModelPart();
		outer_roof_2_r2.setPivot(3.4438F, -37.0118F, -9.9933F);
		side_2.addChild(outer_roof_2_r2);
		setRotationAngle(outer_roof_2_r2, 0.0436F, 0, -1.0472F);
		outer_roof_2_r2.setTextureUVOffset(129, 0).addCuboid(-2.5F, 0, -9, 4, 0, 16, 0, false);

		front_side_bottom_2_r1 = createModelPart();
		front_side_bottom_2_r1.setPivot(0, 0, -13);
		side_2.addChild(front_side_bottom_2_r1);
		setRotationAngle(front_side_bottom_2_r1, 0, -0.1745F, -0.1745F);
		front_side_bottom_2_r1.setTextureUVOffset(48, 111).addCuboid(0, 0, -11, 0, 8, 18, 0, false);

		front_side_upper_2_r1 = createModelPart();
		front_side_upper_2_r1.setPivot(0, -14, -13);
		side_2.addChild(front_side_upper_2_r1);
		setRotationAngle(front_side_upper_2_r1, 0, -0.1745F, 0.1107F);
		front_side_upper_2_r1.setTextureUVOffset(98, 155).addCuboid(0, -23, -11, 0, 23, 11, 0, false);

		front_side_lower_2_r1 = createModelPart();
		front_side_lower_2_r1.setPivot(0, 0, -13);
		side_2.addChild(front_side_lower_2_r1);
		setRotationAngle(front_side_lower_2_r1, 0, -0.1745F, 0);
		front_side_lower_2_r1.setTextureUVOffset(74, 182).addCuboid(0, -14, -11, 0, 14, 11, 0, false);

		headlights = createModelPart();
		headlights.setPivot(0, 24, 0);
		headlights.setTextureUVOffset(12, 56).addCuboid(9.5F, -8.8978F, -31.4F, 4, 4, 0, 0, true);
		headlights.setTextureUVOffset(12, 56).addCuboid(-13.5F, -8.8978F, -31.4F, 4, 4, 0, 0, false);

		tail_lights = createModelPart();
		tail_lights.setPivot(0, 24, 0);
		tail_lights.setTextureUVOffset(20, 56).addCuboid(13.5F, -8.8978F, -31.4F, 4, 4, 0, 0, true);
		tail_lights.setTextureUVOffset(20, 56).addCuboid(-17.5F, -8.8978F, -31.4F, 4, 4, 0, 0, false);

		door_light = createModelPart();
		door_light.setPivot(0, 24, 0);


		outer_roof_3_r6 = createModelPart();
		outer_roof_3_r6.setPivot(-17.8206F, -37.1645F, 0);
		door_light.addChild(outer_roof_3_r6);
		setRotationAngle(outer_roof_3_r6, 0, 0, -1.0472F);
		outer_roof_3_r6.setTextureUVOffset(25, 4).addCuboid(-1.5F, -0.1F, -1.5F, 3, 0, 3, 0, false);

		door_light_on = createModelPart();
		door_light_on.setPivot(0, 24, 0);


		light_r1 = createModelPart();
		light_r1.setPivot(-17.8206F, -37.1645F, 0);
		door_light_on.addChild(light_r1);
		setRotationAngle(light_r1, 0, 0, -1.0472F);
		light_r1.setTextureUVOffset(12, 0).addCuboid(0, 0, 0, 0, 0, 0, 0.3F, false);

		door_light_off = createModelPart();
		door_light_off.setPivot(0, 24, 0);


		light_r2 = createModelPart();
		light_r2.setPivot(-17.8206F, -37.1645F, 0);
		door_light_off.addChild(light_r2);
		setRotationAngle(light_r2, 0, 0, -1.0472F);
		light_r2.setTextureUVOffset(16, 0).addCuboid(0, 0, 0, 0, 0, 0, 0.3F, false);

		buildModel();
	}

	private static final int DOOR_MAX = 13;
	private static final ModelDoorOverlay MODEL_DOOR_OVERLAY = new ModelDoorOverlay(DOOR_MAX, 6.34F, "door_overlay_k_train_left.png", "door_overlay_k_train_right.png");

	@Override
	public ModelKTrain createNew(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		return new ModelKTrain(isTcl, doorAnimationType, renderDoorOverlay);
	}

	@Override
	protected void renderWindowPositions(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
		switch (renderStage) {
			case LIGHT:
				renderMirror(roof_light, graphicsHolder, light, position);
				break;
			case INTERIOR:
				renderMirror(window, graphicsHolder, light, position);
				if (renderDetails) {
					renderMirror(window_handrail, graphicsHolder, light, position);
					renderMirror(roof_window, graphicsHolder, light, position);
					renderMirror(side_panel, graphicsHolder, light, position - 22.1F);
					renderMirror(side_panel, graphicsHolder, light, position + 22.1F);
				}
				break;
			case INTERIOR_TRANSLUCENT:
				renderMirror(side_panel_translucent, graphicsHolder, light, position - 22.1F);
				renderMirror(side_panel_translucent, graphicsHolder, light, position + 22.1F);
				break;
			case EXTERIOR:
				if (isTcl && isIndex(0, position, getWindowPositions()) && isEnd1Head) {
					renderOnceFlipped(window_exterior_end, graphicsHolder, light, position);
				} else if (isTcl && isIndex(-1, position, getWindowPositions()) && isEnd2Head) {
					renderOnce(window_exterior_end, graphicsHolder, light, position);
				} else {
					renderMirror(window_exterior, graphicsHolder, light, position);
				}
				renderMirror(roof_exterior, graphicsHolder, light, position);
				break;
		}
	}

	@Override
	protected void renderDoorPositions(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
		final boolean firstDoor = isIndex(0, position, getDoorPositions());
		final boolean notLastDoor = !firstDoor && !isIndex(-1, position, getDoorPositions());
		final boolean doorOpen = doorLeftZ > 0 || doorRightZ > 0;

		switch (renderStage) {
			case LIGHT:
				if (notLastDoor) {
					renderMirror(roof_light, graphicsHolder, light, position);
				}
				if (firstDoor && doorOpen) {
					renderMirror(door_light_on, graphicsHolder, light, 0);
				}
				break;
			case INTERIOR:
				door_left.setOffset(doorRightX, 0, doorRightZ);
				door_right.setOffset(doorRightX, 0, -doorRightZ);
				renderOnce(door, graphicsHolder, light, position);
				door_left.setOffset(doorLeftX, 0, doorLeftZ);
				door_right.setOffset(doorLeftX, 0, -doorLeftZ);
				renderOnceFlipped(door, graphicsHolder, light, position);

				if (renderDetails) {
					renderOnce(door_handrail, graphicsHolder, light, position);
					if (notLastDoor) {
						renderMirror(roof_door, graphicsHolder, light, position);
					}
				}
				break;
			case EXTERIOR:
				final boolean door1End = isIndex(0, position, getDoorPositions()) && isEnd1Head;
				final boolean door2End = isIndex(-1, position, getDoorPositions()) && isEnd2Head;

				if (isTcl && (door1End || door2End)) {
					door_left_exterior_end.setOffset(doorRightX, 0, doorRightZ);
					door_right_exterior_end.setOffset(doorRightX, 0, -doorRightZ);
					renderOnce(door_exterior_end, graphicsHolder, light, position);
				} else {
					door_left_exterior.setOffset(doorRightX, 0, doorRightZ);
					door_right_exterior.setOffset(doorRightX, 0, -doorRightZ);
					renderOnce(door_exterior, graphicsHolder, light, position);
				}

				if (isTcl && (door1End || door2End)) {
					door_left_exterior_end.setOffset(doorLeftX, 0, doorLeftZ);
					door_right_exterior_end.setOffset(doorLeftX, 0, -doorLeftZ);
					renderOnceFlipped(door_exterior_end, graphicsHolder, light, position);
				} else {
					door_left_exterior.setOffset(doorLeftX, 0, doorLeftZ);
					door_right_exterior.setOffset(doorLeftX, 0, -doorLeftZ);
					renderOnceFlipped(door_exterior, graphicsHolder, light, position);
				}

				renderMirror(roof_exterior, graphicsHolder, light, position);
				if (firstDoor && renderDetails) {
					renderMirror(door_light, graphicsHolder, light, 0);
					if (!doorOpen) {
						renderMirror(door_light_off, graphicsHolder, light, 0);
					}
				}
				break;
		}
	}

	@Override
	protected void renderHeadPosition1(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean useHeadlights) {
		switch (renderStage) {
			case LIGHT:
				renderOnce(roof_end_light, graphicsHolder, light, position);
				break;
			case ALWAYS_ON_LIGHT:
				renderOnce(useHeadlights ? headlights : tail_lights, graphicsHolder, light, position);
				break;
			case INTERIOR:
				renderOnce(head, graphicsHolder, light, position);
				if (renderDetails) {
					renderOnce(roof_end, graphicsHolder, light, position);
				}
				break;
			case EXTERIOR:
				renderOnce(head_exterior, graphicsHolder, light, position);
				renderOnce(roof_end_exterior, graphicsHolder, light, position);
				break;
		}
	}

	@Override
	protected void renderHeadPosition2(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean useHeadlights) {
		switch (renderStage) {
			case LIGHT:
				renderOnceFlipped(roof_end_light, graphicsHolder, light, position);
				break;
			case ALWAYS_ON_LIGHT:
				renderOnceFlipped(useHeadlights ? headlights : tail_lights, graphicsHolder, light, position);
				break;
			case INTERIOR:
				renderOnceFlipped(head, graphicsHolder, light, position);
				if (renderDetails) {
					renderOnceFlipped(roof_end, graphicsHolder, light, position);
				}
				break;
			case EXTERIOR:
				renderOnceFlipped(head_exterior, graphicsHolder, light, position);
				renderOnceFlipped(roof_end_exterior, graphicsHolder, light, position);
				break;
		}
	}

	@Override
	protected void renderEndPosition1(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
		switch (renderStage) {
			case LIGHT:
				renderOnce(roof_end_light, graphicsHolder, light, position);
				break;
			case INTERIOR:
				renderOnce(end, graphicsHolder, light, position);
				if (renderDetails) {
					renderOnce(roof_end, graphicsHolder, light, position);
				}
				break;
			case EXTERIOR:
				renderOnce(end_exterior, graphicsHolder, light, position);
				renderOnce(roof_end_exterior, graphicsHolder, light, position);
				break;
		}
	}

	@Override
	protected void renderEndPosition2(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
		switch (renderStage) {
			case LIGHT:
				renderOnceFlipped(roof_end_light, graphicsHolder, light, position);
				break;
			case INTERIOR:
				renderOnceFlipped(end, graphicsHolder, light, position);
				if (renderDetails) {
					renderOnceFlipped(roof_end, graphicsHolder, light, position);
				}
				break;
			case EXTERIOR:
				renderOnceFlipped(end_exterior, graphicsHolder, light, position);
				renderOnceFlipped(roof_end_exterior, graphicsHolder, light, position);
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
	protected void renderTextDisplays(StoredMatrixTransformations storedMatrixTransformations, int thisRouteColor, String thisRouteName, String thisRouteNumber, String thisStationName, String thisRouteDestination, String nextStationName, Consumer<BiConsumer<String, InterchangeColorsForStationName>> getInterchanges, int car, int totalCars, boolean atPlatform, boolean isTerminating, ObjectArrayList<ScrollingText> scrollingTexts) {
		if (!isTcl) {
			renderFrontDestination(
					storedMatrixTransformations,
					-0.8F, 0, getEndPositions()[0] / 16F - 2.21F, 0, -1.92F, -0.01F,
					-15, 0, 0.37F, 0.21F,
					0xFFFF9900, 0xFFFF0000, 2, getDestinationString(thisRouteDestination, TextSpacingType.SPACE_CJK, true), true, car, totalCars
			);
		}
	}

	@Override
	protected String defaultDestinationString() {
		return "回廠|Depot";
	}
}