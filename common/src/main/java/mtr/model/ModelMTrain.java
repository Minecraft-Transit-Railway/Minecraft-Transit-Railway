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

public class ModelMTrain extends ModelSimpleTrainBase<ModelMTrain> {

	private final ModelMapper window;
	private final ModelMapper upper_wall_r1;
	private final ModelMapper window_handrails;
	private final ModelMapper handrail_8_r1;
	private final ModelMapper top_handrail_6_r1;
	private final ModelMapper top_handrail_5_r1;
	private final ModelMapper top_handrail_4_r1;
	private final ModelMapper top_handrail_3_r1;
	private final ModelMapper top_handrail_2_r1;
	private final ModelMapper top_handrail_1_r1;
	private final ModelMapper handrail_5_r1;
	private final ModelMapper seat;
	private final ModelMapper seat_back_r1;
	private final ModelMapper window_exterior;
	private final ModelMapper upper_wall_r2;
	private final ModelMapper side_panel;
	private final ModelMapper side_panel_translucent;
	private final ModelMapper roof_window;
	private final ModelMapper inner_roof_4_r1;
	private final ModelMapper inner_roof_2_r1;
	private final ModelMapper roof_door;
	private final ModelMapper handrail_2_r1;
	private final ModelMapper inner_roof_4_r2;
	private final ModelMapper inner_roof_2_r2;
	private final ModelMapper roof_exterior;
	private final ModelMapper outer_roof_5_r1;
	private final ModelMapper outer_roof_4_r1;
	private final ModelMapper outer_roof_3_r1;
	private final ModelMapper outer_roof_2_r1;
	private final ModelMapper outer_roof_1_r1;
	private final ModelMapper door;
	private final ModelMapper door_left;
	private final ModelMapper door_left_top_r1;
	private final ModelMapper door_right;
	private final ModelMapper door_right_top_r1;
	private final ModelMapper door_handrail;
	private final ModelMapper door_exterior;
	private final ModelMapper door_left_exterior;
	private final ModelMapper door_left_top_r2;
	private final ModelMapper door_right_exterior;
	private final ModelMapper door_right_top_r2;
	private final ModelMapper end;
	private final ModelMapper upper_wall_2_r1;
	private final ModelMapper upper_wall_1_r1;
	private final ModelMapper end_exterior;
	private final ModelMapper upper_wall_2_r2;
	private final ModelMapper upper_wall_1_r2;
	private final ModelMapper roof_end;
	private final ModelMapper handrail_2_r2;
	private final ModelMapper inner_roof_7_r1;
	private final ModelMapper inner_roof_1;
	private final ModelMapper inner_roof_6_r1;
	private final ModelMapper inner_roof_4_r3;
	private final ModelMapper inner_roof_2_r3;
	private final ModelMapper inner_roof_2;
	private final ModelMapper inner_roof_6_r2;
	private final ModelMapper inner_roof_4_r4;
	private final ModelMapper inner_roof_2_r4;
	private final ModelMapper roof_end_exterior;
	private final ModelMapper vent_2_r1;
	private final ModelMapper vent_1_r1;
	private final ModelMapper outer_roof_1;
	private final ModelMapper outer_roof_5_r2;
	private final ModelMapper outer_roof_4_r2;
	private final ModelMapper outer_roof_3_r2;
	private final ModelMapper outer_roof_2_r2;
	private final ModelMapper outer_roof_1_r2;
	private final ModelMapper outer_roof_2;
	private final ModelMapper outer_roof_5_r3;
	private final ModelMapper outer_roof_4_r3;
	private final ModelMapper outer_roof_3_r3;
	private final ModelMapper outer_roof_2_r3;
	private final ModelMapper outer_roof_1_r3;
	private final ModelMapper roof_light;
	private final ModelMapper roof_light_r1;
	private final ModelMapper roof_end_light;
	private final ModelMapper light_5_r1;
	private final ModelMapper light_4_r1;
	private final ModelMapper light_3_r1;
	private final ModelMapper light_2_r1;
	private final ModelMapper light_1_r1;
	private final ModelMapper head;
	private final ModelMapper upper_wall_2_r3;
	private final ModelMapper upper_wall_1_r3;
	private final ModelMapper head_exterior;
	private final ModelMapper driver_door_upper_2_r1;
	private final ModelMapper driver_door_upper_1_r1;
	private final ModelMapper front;
	private final ModelMapper bottom_r1;
	private final ModelMapper front_middle_top_r1;
	private final ModelMapper front_panel_r1;
	private final ModelMapper side_1;
	private final ModelMapper front_side_bottom_1_r1;
	private final ModelMapper outer_roof_4_r4;
	private final ModelMapper outer_roof_1_r4;
	private final ModelMapper outer_roof_2_r4;
	private final ModelMapper outer_roof_3_r4;
	private final ModelMapper front_side_lower_1_r1;
	private final ModelMapper front_side_upper_1_r1;
	private final ModelMapper side_2;
	private final ModelMapper front_side_bottom_2_r1;
	private final ModelMapper outer_roof_8_r1;
	private final ModelMapper outer_roof_7_r1;
	private final ModelMapper outer_roof_6_r1;
	private final ModelMapper outer_roof_5_r4;
	private final ModelMapper front_side_upper_2_r1;
	private final ModelMapper front_side_lower_2_r1;
	private final ModelMapper headlights;
	private final ModelMapper tail_lights;
	private final ModelMapper door_light;
	private final ModelMapper outer_roof_1_r5;
	private final ModelMapper door_light_on;
	private final ModelMapper light_r1;
	private final ModelMapper door_light_off;
	private final ModelMapper light_r2;

	public ModelMTrain() {
		this(DoorAnimationType.BOUNCY_1, true);
	}

	protected ModelMTrain(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		super(doorAnimationType, renderDoorOverlay);
		final int textureWidth = 320;
		final int textureHeight = 320;

		final ModelDataWrapper modelDataWrapper = new ModelDataWrapper(this, textureWidth, textureHeight);

		window = new ModelMapper(modelDataWrapper);
		window.setPos(0, 24, 0);
		window.texOffs(0, 0).addBox(-20, 0, -24, 20, 1, 48, 0, false);
		window.texOffs(117, 0).addBox(-20, -14, -26, 2, 14, 52, 0, false);

		upper_wall_r1 = new ModelMapper(modelDataWrapper);
		upper_wall_r1.setPos(-20, -14, 0);
		window.addChild(upper_wall_r1);
		setRotationAngle(upper_wall_r1, 0, 0, 0.1107F);
		upper_wall_r1.texOffs(0, 82).addBox(0, -19, -26, 2, 19, 52, 0, false);

		window_handrails = new ModelMapper(modelDataWrapper);
		window_handrails.setPos(0, 24, 0);
		window_handrails.texOffs(28, 28).addBox(-1, -32, -21, 2, 4, 0, 0, false);
		window_handrails.texOffs(8, 0).addBox(0, -33, -9, 0, 33, 0, 0.2F, false);
		window_handrails.texOffs(8, 0).addBox(0, -33, 9, 0, 33, 0, 0.2F, false);
		window_handrails.texOffs(28, 28).addBox(-1, -32, 21, 2, 4, 0, 0, false);
		window_handrails.texOffs(28, 28).addBox(-1, -32, 15, 2, 4, 0, 0, false);
		window_handrails.texOffs(28, 28).addBox(-1, -32, 3, 2, 4, 0, 0, false);
		window_handrails.texOffs(28, 28).addBox(-1, -32, -3, 2, 4, 0, 0, false);
		window_handrails.texOffs(28, 28).addBox(-1, -32, -15, 2, 4, 0, 0, false);

		handrail_8_r1 = new ModelMapper(modelDataWrapper);
		handrail_8_r1.setPos(0, 0, 0);
		window_handrails.addChild(handrail_8_r1);
		setRotationAngle(handrail_8_r1, -1.5708F, 0, 0);
		handrail_8_r1.texOffs(0, 0).addBox(0, -24, -31.5F, 0, 48, 0, 0.2F, false);

		top_handrail_6_r1 = new ModelMapper(modelDataWrapper);
		top_handrail_6_r1.setPos(-12.0518F, -29.0895F, 9.5876F);
		window_handrails.addChild(top_handrail_6_r1);
		setRotationAngle(top_handrail_6_r1, 1.5708F, 0, -0.0436F);
		top_handrail_6_r1.texOffs(0, 0).addBox(0, -9.5F, 0, 0, 20, 0, 0.2F, false);

		top_handrail_5_r1 = new ModelMapper(modelDataWrapper);
		top_handrail_5_r1.setPos(-12.0377F, -28.7666F, 20.7938F);
		window_handrails.addChild(top_handrail_5_r1);
		setRotationAngle(top_handrail_5_r1, 1.0472F, 0, -0.0436F);
		top_handrail_5_r1.texOffs(0, 0).addBox(0, -0.5F, 0, 0, 1, 0, 0.2F, false);

		top_handrail_4_r1 = new ModelMapper(modelDataWrapper);
		top_handrail_4_r1.setPos(-11.9992F, -27.8844F, 21.6768F);
		window_handrails.addChild(top_handrail_4_r1);
		setRotationAngle(top_handrail_4_r1, 0.5236F, 0, -0.0436F);
		top_handrail_4_r1.texOffs(0, 0).addBox(0, -0.5F, 0, 0, 1, 0, 0.2F, false);

		top_handrail_3_r1 = new ModelMapper(modelDataWrapper);
		top_handrail_3_r1.setPos(-12.0518F, -29.0895F, -9.5876F);
		window_handrails.addChild(top_handrail_3_r1);
		setRotationAngle(top_handrail_3_r1, -1.5708F, 0, -0.0436F);
		top_handrail_3_r1.texOffs(0, 0).addBox(0, -9.5F, 0, 0, 20, 0, 0.2F, false);

		top_handrail_2_r1 = new ModelMapper(modelDataWrapper);
		top_handrail_2_r1.setPos(-12.0377F, -28.7666F, -20.7938F);
		window_handrails.addChild(top_handrail_2_r1);
		setRotationAngle(top_handrail_2_r1, -1.0472F, 0, -0.0436F);
		top_handrail_2_r1.texOffs(0, 0).addBox(0, -0.5F, 0, 0, 1, 0, 0.2F, false);

		top_handrail_1_r1 = new ModelMapper(modelDataWrapper);
		top_handrail_1_r1.setPos(-11.9992F, -27.8844F, -21.6768F);
		window_handrails.addChild(top_handrail_1_r1);
		setRotationAngle(top_handrail_1_r1, -0.5236F, 0, -0.0436F);
		top_handrail_1_r1.texOffs(0, 0).addBox(0, -0.5F, 0, 0, 1, 0, 0.2F, false);

		handrail_5_r1 = new ModelMapper(modelDataWrapper);
		handrail_5_r1.setPos(-11, -5, 0);
		window_handrails.addChild(handrail_5_r1);
		setRotationAngle(handrail_5_r1, 0, 0, -0.0436F);
		handrail_5_r1.texOffs(0, 0).addBox(0, -28.2F, -14, 0, 4, 0, 0.2F, false);
		handrail_5_r1.texOffs(0, 0).addBox(0, -28.2F, 14, 0, 4, 0, 0.2F, false);
		handrail_5_r1.texOffs(4, 3).addBox(0, -22.2F, 22, 0, 22, 0, 0.2F, false);
		handrail_5_r1.texOffs(4, 1).addBox(0, -24, 0, 0, 24, 0, 0.2F, false);
		handrail_5_r1.texOffs(4, 3).addBox(0, -22.2F, -22, 0, 22, 0, 0.2F, false);

		seat = new ModelMapper(modelDataWrapper);
		seat.setPos(0, 0, 0);
		window_handrails.addChild(seat);
		seat.texOffs(0, 172).addBox(-18, -6, -22, 7, 1, 44, 0, false);
		seat.texOffs(106, 170).addBox(-18, -5, -21, 6, 5, 42, 0, false);

		seat_back_r1 = new ModelMapper(modelDataWrapper);
		seat_back_r1.setPos(-17, -6, 0);
		seat.addChild(seat_back_r1);
		setRotationAngle(seat_back_r1, 0, 0, -0.0524F);
		seat_back_r1.texOffs(173, 0).addBox(-1, -8, -22, 1, 8, 44, 0, false);

		window_exterior = new ModelMapper(modelDataWrapper);
		window_exterior.setPos(0, 24, 0);
		window_exterior.texOffs(56, 170).addBox(-21, 0, -24, 1, 4, 48, 0, false);
		window_exterior.texOffs(104, 104).addBox(-20, -14, -26, 0, 14, 52, 0, false);

		upper_wall_r2 = new ModelMapper(modelDataWrapper);
		upper_wall_r2.setPos(-20, -14, 0);
		window_exterior.addChild(upper_wall_r2);
		setRotationAngle(upper_wall_r2, 0, 0, 0.1107F);
		upper_wall_r2.texOffs(0, 101).addBox(0, -19, -26, 0, 19, 52, 0, false);

		side_panel = new ModelMapper(modelDataWrapper);
		side_panel.setPos(0, 24, 0);
		side_panel.texOffs(153, 283).addBox(-18, -34, 0, 7, 30, 0, 0, false);

		side_panel_translucent = new ModelMapper(modelDataWrapper);
		side_panel_translucent.setPos(0, 24, 0);
		side_panel_translucent.texOffs(285, 273).addBox(-18, -34, 0, 7, 30, 0, 0, false);

		roof_window = new ModelMapper(modelDataWrapper);
		roof_window.setPos(0, 24, 0);
		roof_window.texOffs(38, 82).addBox(-16, -32, -24, 3, 0, 48, 0, false);
		roof_window.texOffs(76, 0).addBox(-10, -34, -24, 7, 0, 48, 0, false);
		roof_window.texOffs(48, 82).addBox(-2, -33, -24, 2, 0, 48, 0, false);

		inner_roof_4_r1 = new ModelMapper(modelDataWrapper);
		inner_roof_4_r1.setPos(-2, -33, 0);
		roof_window.addChild(inner_roof_4_r1);
		setRotationAngle(inner_roof_4_r1, 0, 0, 0.5236F);
		inner_roof_4_r1.texOffs(44, 82).addBox(-2, 0, -24, 2, 0, 48, 0, false);

		inner_roof_2_r1 = new ModelMapper(modelDataWrapper);
		inner_roof_2_r1.setPos(-13, -32, 0);
		roof_window.addChild(inner_roof_2_r1);
		setRotationAngle(inner_roof_2_r1, 0, 0, -0.5236F);
		inner_roof_2_r1.texOffs(90, 0).addBox(0, 0, -24, 4, 0, 48, 0, false);

		roof_door = new ModelMapper(modelDataWrapper);
		roof_door.setPos(0, 24, 0);
		roof_door.texOffs(0, 172).addBox(-19, -32, -16, 6, 0, 32, 0, false);
		roof_door.texOffs(84, 117).addBox(-10, -34, -16, 7, 0, 32, 0, false);
		roof_door.texOffs(12, 82).addBox(-2, -33, -16, 2, 0, 32, 0, false);

		handrail_2_r1 = new ModelMapper(modelDataWrapper);
		handrail_2_r1.setPos(0, 0, 0);
		roof_door.addChild(handrail_2_r1);
		setRotationAngle(handrail_2_r1, -1.5708F, 0, 0);
		handrail_2_r1.texOffs(0, 0).addBox(0, -16, -31.5F, 0, 32, 0, 0.2F, false);

		inner_roof_4_r2 = new ModelMapper(modelDataWrapper);
		inner_roof_4_r2.setPos(-2, -33, 0);
		roof_door.addChild(inner_roof_4_r2);
		setRotationAngle(inner_roof_4_r2, 0, 0, 0.5236F);
		inner_roof_4_r2.texOffs(128, 0).addBox(-2, 0, -16, 2, 0, 32, 0, false);

		inner_roof_2_r2 = new ModelMapper(modelDataWrapper);
		inner_roof_2_r2.setPos(-13, -32, 0);
		roof_door.addChild(inner_roof_2_r2);
		setRotationAngle(inner_roof_2_r2, 0, 0, -0.5236F);
		inner_roof_2_r2.texOffs(0, 0).addBox(0, 0, -16, 4, 0, 32, 0, true);

		roof_exterior = new ModelMapper(modelDataWrapper);
		roof_exterior.setPos(0, 24, 0);
		roof_exterior.texOffs(133, 0).addBox(-6, -42, -20, 6, 0, 40, 0, false);

		outer_roof_5_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_5_r1.setPos(-9.9394F, -41.3064F, 0);
		roof_exterior.addChild(outer_roof_5_r1);
		setRotationAngle(outer_roof_5_r1, 0, 0, -0.1745F);
		outer_roof_5_r1.texOffs(60, 82).addBox(-4, 0, -20, 8, 0, 40, 0, false);

		outer_roof_4_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_4_r1.setPos(-15.1778F, -39.8628F, 0);
		roof_exterior.addChild(outer_roof_4_r1);
		setRotationAngle(outer_roof_4_r1, 0, 0, -0.5236F);
		outer_roof_4_r1.texOffs(0, 0).addBox(-1.5F, 0, -20, 3, 0, 40, 0, false);

		outer_roof_3_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_3_r1.setPos(-16.9769F, -38.2468F, 0);
		roof_exterior.addChild(outer_roof_3_r1);
		setRotationAngle(outer_roof_3_r1, 0, 0, -1.0472F);
		outer_roof_3_r1.texOffs(0, 82).addBox(-1, 0, -20, 2, 0, 40, 0, false);

		outer_roof_2_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_2_r1.setPos(-17.5872F, -36.3872F, 0);
		roof_exterior.addChild(outer_roof_2_r1);
		setRotationAngle(outer_roof_2_r1, 0, 0, 0.1107F);
		outer_roof_2_r1.texOffs(0, 182).addBox(0, -1, -20, 0, 2, 40, 0, false);

		outer_roof_1_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_1_r1.setPos(-20, -14, 0);
		roof_exterior.addChild(outer_roof_1_r1);
		setRotationAngle(outer_roof_1_r1, 0, 0, 0.1107F);
		outer_roof_1_r1.texOffs(162, 210).addBox(-1, -22, -20, 1, 4, 40, 0, false);

		door = new ModelMapper(modelDataWrapper);
		door.setPos(0, 24, 0);
		door.texOffs(185, 95).addBox(-20, 0, -16, 20, 1, 32, 0, false);

		door_left = new ModelMapper(modelDataWrapper);
		door_left.setPos(0, 0, 0);
		door.addChild(door_left);
		door_left.texOffs(286, 49).addBox(-20.8F, -14, 0, 1, 14, 15, 0, false);

		door_left_top_r1 = new ModelMapper(modelDataWrapper);
		door_left_top_r1.setPos(-20.8F, -14, 0);
		door_left.addChild(door_left_top_r1);
		setRotationAngle(door_left_top_r1, 0, 0, 0.1107F);
		door_left_top_r1.texOffs(185, 0).addBox(0, -19, 0, 1, 19, 15, 0, false);

		door_right = new ModelMapper(modelDataWrapper);
		door_right.setPos(0, 0, 0);
		door.addChild(door_right);
		door_right.texOffs(34, 264).addBox(-20.8F, -14, -15, 1, 14, 15, 0, false);

		door_right_top_r1 = new ModelMapper(modelDataWrapper);
		door_right_top_r1.setPos(-20.8F, -14, 0);
		door_right.addChild(door_right_top_r1);
		setRotationAngle(door_right_top_r1, 0, 0, 0.1107F);
		door_right_top_r1.texOffs(0, 58).addBox(0, -19, -15, 1, 19, 15, 0, false);

		door_handrail = new ModelMapper(modelDataWrapper);
		door_handrail.setPos(0, 24, 0);
		door_handrail.texOffs(8, 0).addBox(0, -33, 0, 0, 33, 0, 0.2F, false);

		door_exterior = new ModelMapper(modelDataWrapper);
		door_exterior.setPos(0, 24, 0);
		door_exterior.texOffs(0, 224).addBox(-21, 0, -16, 1, 4, 32, 0, false);

		door_left_exterior = new ModelMapper(modelDataWrapper);
		door_left_exterior.setPos(0, 0, 0);
		door_exterior.addChild(door_left_exterior);
		door_left_exterior.texOffs(190, 239).addBox(-20.8F, -14, 0, 0, 14, 15, 0, false);

		door_left_top_r2 = new ModelMapper(modelDataWrapper);
		door_left_top_r2.setPos(-20.8F, -14, 0);
		door_left_exterior.addChild(door_left_top_r2);
		setRotationAngle(door_left_top_r2, 0, 0, 0.1107F);
		door_left_top_r2.texOffs(0, 209).addBox(0, -19, 0, 0, 19, 15, 0, false);

		door_right_exterior = new ModelMapper(modelDataWrapper);
		door_right_exterior.setPos(0, 0, 0);
		door_exterior.addChild(door_right_exterior);
		door_right_exterior.texOffs(34, 226).addBox(-20.8F, -14, -15, 0, 14, 15, 0, false);

		door_right_top_r2 = new ModelMapper(modelDataWrapper);
		door_right_top_r2.setPos(-20.8F, -14, 0);
		door_right_exterior.addChild(door_right_top_r2);
		setRotationAngle(door_right_top_r2, 0, 0, 0.1107F);
		door_right_top_r2.texOffs(185, 80).addBox(0, -19, -15, 0, 19, 15, 0, false);

		end = new ModelMapper(modelDataWrapper);
		end.setPos(0, 24, 0);
		end.texOffs(185, 128).addBox(-20, 0, -12, 40, 1, 20, 0, false);
		end.texOffs(0, 92).addBox(18, -14, 7, 2, 14, 3, 0, true);
		end.texOffs(0, 92).addBox(-20, -14, 7, 2, 14, 3, 0, false);
		end.texOffs(229, 250).addBox(9.5F, -34, -12, 9, 34, 19, 0, true);
		end.texOffs(226, 149).addBox(-18.5F, -34, -12, 9, 34, 19, 0, false);

		upper_wall_2_r1 = new ModelMapper(modelDataWrapper);
		upper_wall_2_r1.setPos(-20, -14, 0);
		end.addChild(upper_wall_2_r1);
		setRotationAngle(upper_wall_2_r1, 0, 0, 0.1107F);
		upper_wall_2_r1.texOffs(22, 92).addBox(0, -19, 7, 2, 19, 3, 0, false);

		upper_wall_1_r1 = new ModelMapper(modelDataWrapper);
		upper_wall_1_r1.setPos(20, -14, 0);
		end.addChild(upper_wall_1_r1);
		setRotationAngle(upper_wall_1_r1, 0, 0, -0.1107F);
		upper_wall_1_r1.texOffs(22, 92).addBox(-2, -19, 7, 2, 19, 3, 0, true);

		end_exterior = new ModelMapper(modelDataWrapper);
		end_exterior.setPos(0, 24, 0);
		end_exterior.texOffs(0, 192).addBox(20, 0, -12, 1, 4, 20, 0, true);
		end_exterior.texOffs(0, 192).addBox(-21, 0, -12, 1, 4, 20, 0, false);
		end_exterior.texOffs(44, 279).addBox(18, -14, -12, 2, 14, 22, 0, true);
		end_exterior.texOffs(44, 279).addBox(-20, -14, -12, 2, 14, 22, 0, false);
		end_exterior.texOffs(88, 0).addBox(9.5F, -34, -12, 9, 34, 0, 0, false);
		end_exterior.texOffs(88, 0).addBox(-18.5F, -34, -12, 9, 34, 0, 0, true);
		end_exterior.texOffs(225, 57).addBox(-18, -41, -12, 36, 7, 0, 0, false);

		upper_wall_2_r2 = new ModelMapper(modelDataWrapper);
		upper_wall_2_r2.setPos(-20, -14, 0);
		end_exterior.addChild(upper_wall_2_r2);
		setRotationAngle(upper_wall_2_r2, 0, 0, 0.1107F);
		upper_wall_2_r2.texOffs(97, 264).addBox(0, -19, -12, 2, 19, 22, 0, false);

		upper_wall_1_r2 = new ModelMapper(modelDataWrapper);
		upper_wall_1_r2.setPos(20, -14, 0);
		end_exterior.addChild(upper_wall_1_r2);
		setRotationAngle(upper_wall_1_r2, 0, 0, -0.1107F);
		upper_wall_1_r2.texOffs(97, 264).addBox(-2, -19, -12, 2, 19, 22, 0, true);

		roof_end = new ModelMapper(modelDataWrapper);
		roof_end.setPos(0, 24, 0);


		handrail_2_r2 = new ModelMapper(modelDataWrapper);
		handrail_2_r2.setPos(0, 0, 0);
		roof_end.addChild(handrail_2_r2);
		setRotationAngle(handrail_2_r2, -1.5708F, 0, 0);
		handrail_2_r2.texOffs(0, 0).addBox(0, -40, -31.5F, 0, 16, 0, 0.2F, false);

		inner_roof_7_r1 = new ModelMapper(modelDataWrapper);
		inner_roof_7_r1.setPos(0, -33, 16);
		roof_end.addChild(inner_roof_7_r1);
		setRotationAngle(inner_roof_7_r1, -0.5236F, 0, 0);
		inner_roof_7_r1.texOffs(6, 49).addBox(-2, 0, -2, 4, 0, 2, 0, false);

		inner_roof_1 = new ModelMapper(modelDataWrapper);
		inner_roof_1.setPos(-2, -33, 16);
		roof_end.addChild(inner_roof_1);
		inner_roof_1.texOffs(32, 82).addBox(-17, 1, -12, 6, 0, 36, 0, false);
		inner_roof_1.texOffs(81, 66).addBox(-8, -1, -28, 10, 0, 52, 0, false);
		inner_roof_1.texOffs(0, 49).addBox(0, 0, 0, 2, 0, 24, 0, false);

		inner_roof_6_r1 = new ModelMapper(modelDataWrapper);
		inner_roof_6_r1.setPos(0, 0, 0);
		inner_roof_1.addChild(inner_roof_6_r1);
		setRotationAngle(inner_roof_6_r1, -0.5236F, 0, 0.5236F);
		inner_roof_6_r1.texOffs(6, 51).addBox(-2, 0, -2, 2, 0, 2, 0, false);

		inner_roof_4_r3 = new ModelMapper(modelDataWrapper);
		inner_roof_4_r3.setPos(0, 0, -16);
		inner_roof_1.addChild(inner_roof_4_r3);
		setRotationAngle(inner_roof_4_r3, 0, 0, 0.5236F);
		inner_roof_4_r3.texOffs(4, 49).addBox(-2, 0, 16, 2, 0, 24, 0, false);

		inner_roof_2_r3 = new ModelMapper(modelDataWrapper);
		inner_roof_2_r3.setPos(-11, 1, -16);
		inner_roof_1.addChild(inner_roof_2_r3);
		setRotationAngle(inner_roof_2_r3, 0, 0, -0.5236F);
		inner_roof_2_r3.texOffs(78, 0).addBox(0, 0, 4, 4, 0, 36, 0, true);

		inner_roof_2 = new ModelMapper(modelDataWrapper);
		inner_roof_2.setPos(-2, -33, 16);
		roof_end.addChild(inner_roof_2);
		inner_roof_2.texOffs(32, 82).addBox(15, 1, -12, 6, 0, 36, 0, true);
		inner_roof_2.texOffs(81, 66).addBox(2, -1, -28, 10, 0, 52, 0, true);
		inner_roof_2.texOffs(0, 49).addBox(2, 0, 0, 2, 0, 24, 0, true);

		inner_roof_6_r2 = new ModelMapper(modelDataWrapper);
		inner_roof_6_r2.setPos(4, 0, 0);
		inner_roof_2.addChild(inner_roof_6_r2);
		setRotationAngle(inner_roof_6_r2, -0.5236F, 0, -0.5236F);
		inner_roof_6_r2.texOffs(6, 51).addBox(0, 0, -2, 2, 0, 2, 0, true);

		inner_roof_4_r4 = new ModelMapper(modelDataWrapper);
		inner_roof_4_r4.setPos(4, 0, -16);
		inner_roof_2.addChild(inner_roof_4_r4);
		setRotationAngle(inner_roof_4_r4, 0, 0, -0.5236F);
		inner_roof_4_r4.texOffs(4, 49).addBox(0, 0, 16, 2, 0, 24, 0, true);

		inner_roof_2_r4 = new ModelMapper(modelDataWrapper);
		inner_roof_2_r4.setPos(15, 1, -16);
		inner_roof_2.addChild(inner_roof_2_r4);
		setRotationAngle(inner_roof_2_r4, 0, 0, 0.5236F);
		inner_roof_2_r4.texOffs(70, 0).addBox(-4, 0, 4, 4, 0, 36, 0, true);

		roof_end_exterior = new ModelMapper(modelDataWrapper);
		roof_end_exterior.setPos(0, 24, 0);
		roof_end_exterior.texOffs(105, 105).addBox(-8, -43, 0, 16, 2, 48, 0, false);

		vent_2_r1 = new ModelMapper(modelDataWrapper);
		vent_2_r1.setPos(-8, -43, 0);
		roof_end_exterior.addChild(vent_2_r1);
		setRotationAngle(vent_2_r1, 0, 0, -0.3491F);
		vent_2_r1.texOffs(160, 160).addBox(-9, 0, 0, 9, 2, 48, 0, false);

		vent_1_r1 = new ModelMapper(modelDataWrapper);
		vent_1_r1.setPos(8, -43, 0);
		roof_end_exterior.addChild(vent_1_r1);
		setRotationAngle(vent_1_r1, 0, 0, 0.3491F);
		vent_1_r1.texOffs(160, 160).addBox(0, 0, 0, 9, 2, 48, 0, true);

		outer_roof_1 = new ModelMapper(modelDataWrapper);
		outer_roof_1.setPos(0, 0, 0);
		roof_end_exterior.addChild(outer_roof_1);
		outer_roof_1.texOffs(147, 293).addBox(-6, -42, -12, 6, 1, 20, 0, false);

		outer_roof_5_r2 = new ModelMapper(modelDataWrapper);
		outer_roof_5_r2.setPos(-9.7656F, -40.3206F, 0);
		outer_roof_1.addChild(outer_roof_5_r2);
		setRotationAngle(outer_roof_5_r2, 0, 0, -0.1745F);
		outer_roof_5_r2.texOffs(262, 182).addBox(-4, -1, -12, 8, 1, 20, 0, false);

		outer_roof_4_r2 = new ModelMapper(modelDataWrapper);
		outer_roof_4_r2.setPos(-14.6775F, -38.9948F, 0);
		outer_roof_1.addChild(outer_roof_4_r2);
		setRotationAngle(outer_roof_4_r2, 0, 0, -0.5236F);
		outer_roof_4_r2.texOffs(259, 64).addBox(-1.5F, -1, -12, 3, 1, 20, 0, false);

		outer_roof_3_r2 = new ModelMapper(modelDataWrapper);
		outer_roof_3_r2.setPos(-16.1105F, -37.7448F, 0);
		outer_roof_1.addChild(outer_roof_3_r2);
		setRotationAngle(outer_roof_3_r2, 0, 0, -1.0472F);
		outer_roof_3_r2.texOffs(0, 289).addBox(-1, -1, -12, 2, 1, 20, 0, false);

		outer_roof_2_r2 = new ModelMapper(modelDataWrapper);
		outer_roof_2_r2.setPos(-17.587F, -36.3849F, 0);
		outer_roof_1.addChild(outer_roof_2_r2);
		setRotationAngle(outer_roof_2_r2, 0, 0, 0.1107F);
		outer_roof_2_r2.texOffs(110, 129).addBox(0, -1, -12, 1, 2, 20, 0, false);

		outer_roof_1_r2 = new ModelMapper(modelDataWrapper);
		outer_roof_1_r2.setPos(-20, -14, 0);
		outer_roof_1.addChild(outer_roof_1_r2);
		setRotationAngle(outer_roof_1_r2, 0, 0, 0.1107F);
		outer_roof_1_r2.texOffs(185, 268).addBox(-1, -22, -12, 1, 4, 20, 0, false);

		outer_roof_2 = new ModelMapper(modelDataWrapper);
		outer_roof_2.setPos(0, 0, 0);
		roof_end_exterior.addChild(outer_roof_2);
		outer_roof_2.texOffs(147, 293).addBox(0, -42, -12, 6, 1, 20, 0, true);

		outer_roof_5_r3 = new ModelMapper(modelDataWrapper);
		outer_roof_5_r3.setPos(9.7656F, -40.3206F, 0);
		outer_roof_2.addChild(outer_roof_5_r3);
		setRotationAngle(outer_roof_5_r3, 0, 0, 0.1745F);
		outer_roof_5_r3.texOffs(262, 182).addBox(-4, -1, -12, 8, 1, 20, 0, true);

		outer_roof_4_r3 = new ModelMapper(modelDataWrapper);
		outer_roof_4_r3.setPos(14.6775F, -38.9948F, 0);
		outer_roof_2.addChild(outer_roof_4_r3);
		setRotationAngle(outer_roof_4_r3, 0, 0, 0.5236F);
		outer_roof_4_r3.texOffs(259, 64).addBox(-1.5F, -1, -12, 3, 1, 20, 0, true);

		outer_roof_3_r3 = new ModelMapper(modelDataWrapper);
		outer_roof_3_r3.setPos(16.1105F, -37.7448F, 0);
		outer_roof_2.addChild(outer_roof_3_r3);
		setRotationAngle(outer_roof_3_r3, 0, 0, 1.0472F);
		outer_roof_3_r3.texOffs(0, 289).addBox(-1, -1, -12, 2, 1, 20, 0, true);

		outer_roof_2_r3 = new ModelMapper(modelDataWrapper);
		outer_roof_2_r3.setPos(17.587F, -36.3849F, 0);
		outer_roof_2.addChild(outer_roof_2_r3);
		setRotationAngle(outer_roof_2_r3, 0, 0, -0.1107F);
		outer_roof_2_r3.texOffs(110, 129).addBox(-1, -1, -12, 1, 2, 20, 0, true);

		outer_roof_1_r3 = new ModelMapper(modelDataWrapper);
		outer_roof_1_r3.setPos(20, -14, 0);
		outer_roof_2.addChild(outer_roof_1_r3);
		setRotationAngle(outer_roof_1_r3, 0, 0, -0.1107F);
		outer_roof_1_r3.texOffs(185, 268).addBox(0, -22, -12, 1, 4, 20, 0, true);

		roof_light = new ModelMapper(modelDataWrapper);
		roof_light.setPos(0, 24, 0);


		roof_light_r1 = new ModelMapper(modelDataWrapper);
		roof_light_r1.setPos(-2, -33, 0);
		roof_light.addChild(roof_light_r1);
		setRotationAngle(roof_light_r1, 0, 0, 0.5236F);
		roof_light_r1.texOffs(0, 82).addBox(-2, -0.1F, -24, 2, 0, 48, 0, false);

		roof_end_light = new ModelMapper(modelDataWrapper);
		roof_end_light.setPos(0, 24, 0);


		light_5_r1 = new ModelMapper(modelDataWrapper);
		light_5_r1.setPos(2, -33, 0);
		roof_end_light.addChild(light_5_r1);
		setRotationAngle(light_5_r1, 0, 0, -0.5236F);
		light_5_r1.texOffs(24, 82).addBox(0, -0.1F, 16, 2, 0, 24, 0, false);

		light_4_r1 = new ModelMapper(modelDataWrapper);
		light_4_r1.setPos(2, -33, 16);
		roof_end_light.addChild(light_4_r1);
		setRotationAngle(light_4_r1, -0.5236F, 0, -0.5236F);
		light_4_r1.texOffs(10, 51).addBox(0, -0.1F, -2, 2, 0, 2, 0, true);

		light_3_r1 = new ModelMapper(modelDataWrapper);
		light_3_r1.setPos(0, -33, 16);
		roof_end_light.addChild(light_3_r1);
		setRotationAngle(light_3_r1, -0.5236F, 0, 0);
		light_3_r1.texOffs(14, 49).addBox(-2, -0.1F, -2, 4, 0, 2, 0, false);

		light_2_r1 = new ModelMapper(modelDataWrapper);
		light_2_r1.setPos(-2, -33, 16);
		roof_end_light.addChild(light_2_r1);
		setRotationAngle(light_2_r1, -0.5236F, 0, 0.5236F);
		light_2_r1.texOffs(10, 51).addBox(-2, -0.1F, -2, 2, 0, 2, 0, false);

		light_1_r1 = new ModelMapper(modelDataWrapper);
		light_1_r1.setPos(-2, -33, 0);
		roof_end_light.addChild(light_1_r1);
		setRotationAngle(light_1_r1, 0, 0, 0.5236F);
		light_1_r1.texOffs(24, 82).addBox(-2, -0.1F, 16, 2, 0, 24, 0, false);

		head = new ModelMapper(modelDataWrapper);
		head.setPos(0, 24, 0);
		head.texOffs(219, 35).addBox(-20, 0, 4, 40, 1, 4, 0, false);
		head.texOffs(185, 128).addBox(18, -14, 4, 2, 14, 6, 0, true);
		head.texOffs(185, 128).addBox(-20, -14, 4, 2, 14, 6, 0, false);
		head.texOffs(80, 222).addBox(-18, -34, 4, 36, 34, 0, 0, false);

		upper_wall_2_r3 = new ModelMapper(modelDataWrapper);
		upper_wall_2_r3.setPos(-20, -14, 0);
		head.addChild(upper_wall_2_r3);
		setRotationAngle(upper_wall_2_r3, 0, 0, 0.1107F);
		upper_wall_2_r3.texOffs(132, 118).addBox(0, -19, 4, 2, 19, 6, 0, false);

		upper_wall_1_r3 = new ModelMapper(modelDataWrapper);
		upper_wall_1_r3.setPos(20, -14, 0);
		head.addChild(upper_wall_1_r3);
		setRotationAngle(upper_wall_1_r3, 0, 0, -0.1107F);
		upper_wall_1_r3.texOffs(132, 118).addBox(-2, -19, 4, 2, 19, 6, 0, true);

		head_exterior = new ModelMapper(modelDataWrapper);
		head_exterior.setPos(0, 24, 0);
		head_exterior.texOffs(153, 66).addBox(-21, 0, -18, 42, 7, 22, 0, false);
		head_exterior.texOffs(153, 77).addBox(20, 0, 4, 1, 7, 4, 0, true);
		head_exterior.texOffs(153, 77).addBox(-21, 0, 4, 1, 7, 4, 0, false);
		head_exterior.texOffs(51, 241).addBox(18, -14, -9, 2, 14, 19, 0, true);
		head_exterior.texOffs(51, 241).addBox(-20, -14, -9, 2, 14, 19, 0, false);
		head_exterior.texOffs(276, 86).addBox(18, -14, -18, 1, 14, 9, 0, true);
		head_exterior.texOffs(276, 86).addBox(-19, -14, -18, 1, 14, 9, 0, false);
		head_exterior.texOffs(219, 0).addBox(-18, -34, 3, 36, 34, 0, 0, false);

		driver_door_upper_2_r1 = new ModelMapper(modelDataWrapper);
		driver_door_upper_2_r1.setPos(-20, -14, 0);
		head_exterior.addChild(driver_door_upper_2_r1);
		setRotationAngle(driver_door_upper_2_r1, 0, 0, 0.1107F);
		driver_door_upper_2_r1.texOffs(0, 172).addBox(1, -19, -18, 1, 19, 9, 0, false);
		driver_door_upper_2_r1.texOffs(160, 170).addBox(0, -19, -9, 2, 19, 19, 0, false);

		driver_door_upper_1_r1 = new ModelMapper(modelDataWrapper);
		driver_door_upper_1_r1.setPos(20, -14, 0);
		head_exterior.addChild(driver_door_upper_1_r1);
		setRotationAngle(driver_door_upper_1_r1, 0, 0, -0.1107F);
		driver_door_upper_1_r1.texOffs(0, 172).addBox(-2, -19, -18, 1, 19, 9, 0, true);
		driver_door_upper_1_r1.texOffs(160, 170).addBox(-2, -19, -9, 2, 19, 19, 0, true);

		front = new ModelMapper(modelDataWrapper);
		front.setPos(0, 0, 0);
		head_exterior.addChild(front);
		front.texOffs(225, 52).addBox(-19, 0, -28, 38, 5, 0, 0, false);

		bottom_r1 = new ModelMapper(modelDataWrapper);
		bottom_r1.setPos(0, 7, 4);
		front.addChild(bottom_r1);
		setRotationAngle(bottom_r1, -0.0698F, 0, 0);
		bottom_r1.texOffs(0, 49).addBox(-21, 0, -33, 42, 0, 33, 0, false);

		front_middle_top_r1 = new ModelMapper(modelDataWrapper);
		front_middle_top_r1.setPos(0, -42, -12);
		front.addChild(front_middle_top_r1);
		setRotationAngle(front_middle_top_r1, 0.3491F, 0, 0);
		front_middle_top_r1.texOffs(143, 95).addBox(-6, 0, -10, 12, 0, 10, 0, false);

		front_panel_r1 = new ModelMapper(modelDataWrapper);
		front_panel_r1.setPos(0, 0, -28);
		front.addChild(front_panel_r1);
		setRotationAngle(front_panel_r1, -0.1745F, 0, 0);
		front_panel_r1.texOffs(204, 210).addBox(-19, -40, 0, 38, 40, 0, 0, false);

		side_1 = new ModelMapper(modelDataWrapper);
		side_1.setPos(0, 0, 0);
		front.addChild(side_1);
		side_1.texOffs(22, 0).addBox(19, -14, -18, 1, 14, 0, 0, true);

		front_side_bottom_1_r1 = new ModelMapper(modelDataWrapper);
		front_side_bottom_1_r1.setPos(21, 0, -13);
		side_1.addChild(front_side_bottom_1_r1);
		setRotationAngle(front_side_bottom_1_r1, 0, 0.1745F, 0.1745F);
		front_side_bottom_1_r1.texOffs(263, 17).addBox(0, 0, -16, 0, 7, 23, 0, true);

		outer_roof_4_r4 = new ModelMapper(modelDataWrapper);
		outer_roof_4_r4.setPos(6, -42, -12);
		side_1.addChild(outer_roof_4_r4);
		setRotationAngle(outer_roof_4_r4, 0.3491F, 0, 0.1745F);
		outer_roof_4_r4.texOffs(142, 66).addBox(0, 0, -11, 11, 0, 11, 0, true);

		outer_roof_1_r4 = new ModelMapper(modelDataWrapper);
		outer_roof_1_r4.setPos(20, -14, 0);
		side_1.addChild(outer_roof_1_r4);
		setRotationAngle(outer_roof_1_r4, 0, 0, -0.1107F);
		outer_roof_1_r4.texOffs(0, 49).addBox(0, -22, -18, 1, 4, 6, 0, true);
		outer_roof_1_r4.texOffs(46, 0).addBox(-1, -19, -18, 1, 19, 0, 0, true);

		outer_roof_2_r4 = new ModelMapper(modelDataWrapper);
		outer_roof_2_r4.setPos(17.587F, -36.3849F, 0);
		side_1.addChild(outer_roof_2_r4);
		setRotationAngle(outer_roof_2_r4, 0, 0, -0.1107F);
		outer_roof_2_r4.texOffs(24, 26).addBox(0, -1, -18, 0, 2, 6, 0, true);

		outer_roof_3_r4 = new ModelMapper(modelDataWrapper);
		outer_roof_3_r4.setPos(15.813F, -37.5414F, -17.4163F);
		side_1.addChild(outer_roof_3_r4);
		setRotationAngle(outer_roof_3_r4, 0.1745F, 0, 0.7418F);
		outer_roof_3_r4.texOffs(89, 122).addBox(-3.5F, 0, -5.5F, 7, 0, 11, 0, true);

		front_side_lower_1_r1 = new ModelMapper(modelDataWrapper);
		front_side_lower_1_r1.setPos(20, 0, -18);
		side_1.addChild(front_side_lower_1_r1);
		setRotationAngle(front_side_lower_1_r1, 0, 0.1745F, 0);
		front_side_lower_1_r1.texOffs(207, 257).addBox(0, -14, -11, 0, 20, 11, 0, true);

		front_side_upper_1_r1 = new ModelMapper(modelDataWrapper);
		front_side_upper_1_r1.setPos(20, -14, -18);
		side_1.addChild(front_side_upper_1_r1);
		setRotationAngle(front_side_upper_1_r1, 0, 0.1745F, -0.1107F);
		front_side_upper_1_r1.texOffs(93, 245).addBox(0, -23, -11, 0, 23, 11, 0, true);

		side_2 = new ModelMapper(modelDataWrapper);
		side_2.setPos(-21, 0, -9);
		front.addChild(side_2);
		side_2.texOffs(22, 0).addBox(1, -14, -9, 1, 14, 0, 0, false);

		front_side_bottom_2_r1 = new ModelMapper(modelDataWrapper);
		front_side_bottom_2_r1.setPos(0, 0, -4);
		side_2.addChild(front_side_bottom_2_r1);
		setRotationAngle(front_side_bottom_2_r1, 0, -0.1745F, -0.1745F);
		front_side_bottom_2_r1.texOffs(263, 17).addBox(0, 0, -16, 0, 7, 23, 0, false);

		outer_roof_8_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_8_r1.setPos(5.187F, -37.5414F, -8.4163F);
		side_2.addChild(outer_roof_8_r1);
		setRotationAngle(outer_roof_8_r1, 0.1745F, 0, -0.7418F);
		outer_roof_8_r1.texOffs(89, 122).addBox(-3.5F, 0, -5.5F, 7, 0, 11, 0, false);

		outer_roof_7_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_7_r1.setPos(3.413F, -36.3849F, 9);
		side_2.addChild(outer_roof_7_r1);
		setRotationAngle(outer_roof_7_r1, 0, 0, 0.1107F);
		outer_roof_7_r1.texOffs(24, 26).addBox(0, -1, -18, 0, 2, 6, 0, false);

		outer_roof_6_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_6_r1.setPos(15, -42, -3);
		side_2.addChild(outer_roof_6_r1);
		setRotationAngle(outer_roof_6_r1, 0.3491F, 0, -0.1745F);
		outer_roof_6_r1.texOffs(142, 66).addBox(-11, 0, -11, 11, 0, 11, 0, false);

		outer_roof_5_r4 = new ModelMapper(modelDataWrapper);
		outer_roof_5_r4.setPos(1, -14, 9);
		side_2.addChild(outer_roof_5_r4);
		setRotationAngle(outer_roof_5_r4, 0, 0, 0.1107F);
		outer_roof_5_r4.texOffs(0, 49).addBox(-1, -22, -18, 1, 4, 6, 0, false);
		outer_roof_5_r4.texOffs(46, 0).addBox(0, -19, -18, 1, 19, 0, 0, false);

		front_side_upper_2_r1 = new ModelMapper(modelDataWrapper);
		front_side_upper_2_r1.setPos(1, -14, -9);
		side_2.addChild(front_side_upper_2_r1);
		setRotationAngle(front_side_upper_2_r1, 0, -0.1745F, 0.1107F);
		front_side_upper_2_r1.texOffs(93, 245).addBox(0, -23, -11, 0, 23, 11, 0, false);

		front_side_lower_2_r1 = new ModelMapper(modelDataWrapper);
		front_side_lower_2_r1.setPos(1, 0, -9);
		side_2.addChild(front_side_lower_2_r1);
		setRotationAngle(front_side_lower_2_r1, 0, -0.1745F, 0);
		front_side_lower_2_r1.texOffs(207, 257).addBox(0, -14, -11, 0, 20, 11, 0, false);

		headlights = new ModelMapper(modelDataWrapper);
		headlights.setPos(0, 24, 0);
		headlights.texOffs(102, 36).addBox(8, -1, -28.1F, 4, 4, 0, 0, true);
		headlights.texOffs(102, 36).addBox(-12, -1, -28.1F, 4, 4, 0, 0, false);

		tail_lights = new ModelMapper(modelDataWrapper);
		tail_lights.setPos(0, 24, 0);
		tail_lights.texOffs(110, 36).addBox(10, -1, -28.1F, 4, 4, 0, 0, true);
		tail_lights.texOffs(110, 36).addBox(-14, -1, -28.1F, 4, 4, 0, 0, false);

		door_light = new ModelMapper(modelDataWrapper);
		door_light.setPos(0, 24, 0);


		outer_roof_1_r5 = new ModelMapper(modelDataWrapper);
		outer_roof_1_r5.setPos(-20, -14, 0);
		door_light.addChild(outer_roof_1_r5);
		setRotationAngle(outer_roof_1_r5, 0, 0, 0.1107F);
		outer_roof_1_r5.texOffs(24, 34).addBox(-1.1F, -22, -2, 0, 4, 4, 0, false);

		door_light_on = new ModelMapper(modelDataWrapper);
		door_light_on.setPos(0, 24, 0);


		light_r1 = new ModelMapper(modelDataWrapper);
		light_r1.setPos(-20, -14, 0);
		door_light_on.addChild(light_r1);
		setRotationAngle(light_r1, 0, 0, 0.1107F);
		light_r1.texOffs(22, 39).addBox(-1, -20, 0, 0, 0, 0, 0.4F, false);

		door_light_off = new ModelMapper(modelDataWrapper);
		door_light_off.setPos(0, 24, 0);


		light_r2 = new ModelMapper(modelDataWrapper);
		light_r2.setPos(-20, -14, 0);
		door_light_off.addChild(light_r2);
		setRotationAngle(light_r2, 0, 0, 0.1107F);
		light_r2.texOffs(22, 41).addBox(-1, -20, 0, 0, 0, 0, 0.4F, false);

		modelDataWrapper.setModelPart(textureWidth, textureHeight);
		window.setModelPart();
		window_handrails.setModelPart();
		window_exterior.setModelPart();
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

	private static final int DOOR_MAX = 14;
	private static final ModelDoorOverlay MODEL_DOOR_OVERLAY = new ModelDoorOverlay(DOOR_MAX, 6.34F, "door_overlay_m_train_left.png", "door_overlay_m_train_right.png");

	@Override
	public ModelMTrain createNew(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		return new ModelMTrain(doorAnimationType, renderDoorOverlay);
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
	protected void renderDoorPositions(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
		final boolean middleDoor = isIndex(getDoorPositions().length / 2, position, getDoorPositions());
		final boolean doorOpen = doorLeftZ > 0 || doorRightZ > 0;
		final boolean notLastDoor = !isIndex(0, position, getDoorPositions()) && !isIndex(-1, position, getDoorPositions());

		switch (renderStage) {
			case LIGHTS:
				if (notLastDoor) {
					renderMirror(roof_light, matrices, vertices, light, position);
				}
				if (middleDoor && doorOpen) {
					renderMirror(door_light_on, matrices, vertices, light, position - 30);
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
					renderOnce(door_handrail, matrices, vertices, light, position);
					if (notLastDoor) {
						renderMirror(roof_door, matrices, vertices, light, position);
					}
				}
				break;
			case EXTERIOR:
				door_left_exterior.setOffset(0, 0, doorRightZ);
				door_right_exterior.setOffset(0, 0, -doorRightZ);
				renderOnce(door_exterior, matrices, vertices, light, position);
				door_left_exterior.setOffset(0, 0, doorLeftZ);
				door_right_exterior.setOffset(0, 0, -doorLeftZ);
				renderOnceFlipped(door_exterior, matrices, vertices, light, position);
				renderMirror(roof_exterior, matrices, vertices, light, position);
				if (middleDoor && renderDetails) {
					renderMirror(door_light, matrices, vertices, light, position - 30);
					if (!doorOpen) {
						renderMirror(door_light_off, matrices, vertices, light, position - 30);
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
		renderFrontDestination(
				matrices, font, immediate,
				-0.8F, 0, getEndPositions()[0] / 16F - 1.75F, 0, -1.97F, -0.01F,
				-10, 0, 0.37F, 0.21F,
				0xFFFF9900, 0xFFFF0000, 3, getDestinationString(lastStation, customDestination, TextSpacingType.SPACE_CJK, true), true, car, totalCars
		);
	}

	@Override
	protected String defaultDestinationString() {
		return "回廠|Depot";
	}
}