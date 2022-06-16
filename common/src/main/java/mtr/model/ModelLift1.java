package mtr.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mtr.EntityTypes;
import mtr.mappings.ModelDataWrapper;
import mtr.mappings.ModelMapper;
import net.minecraft.client.renderer.texture.OverlayTexture;

public class ModelLift1 extends ModelTrainBase {

	private final ModelMapper main;
	private final ModelMapper main_edge;
	private final ModelMapper main_corner;
	private final ModelMapper handrail_bottom_r1;
	private final ModelMapper main_exterior;
	private final ModelMapper main_exterior_edge;
	private final ModelMapper main_exterior_corner;
	private final ModelMapper main_light;
	private final ModelMapper door;
	private final ModelMapper door_left;
	private final ModelMapper door_right;
	private final ModelMapper door_exterior;
	private final ModelMapper door_left_exterior;
	private final ModelMapper door_right_exterior;
	private final ModelMapper wall_patch;
	private final ModelMapper handrail_bottom_r2;
	private final ModelMapper handrail_bottom_r3;

	private final int width;
	private final int depth;
	private final boolean isDoubleSided;

	public ModelLift1(EntityTypes.LiftType liftType) {
		width = liftType.width;
		depth = liftType.depth;
		isDoubleSided = liftType.isDoubleSided;

		final int textureWidth = 128;
		final int textureHeight = 128;

		final ModelDataWrapper modelDataWrapper = new ModelDataWrapper(this, textureWidth, textureHeight);

		main = new ModelMapper(modelDataWrapper);
		main.setPos(0, 24, 0);
		main.texOffs(0, 34).addBox(-8, 0, -8, 16, 0, 16, 0, false);
		main.texOffs(32, 17).addBox(-8, -32, -8, 16, 0, 16, 0, false);

		main_edge = new ModelMapper(modelDataWrapper);
		main_edge.setPos(0, 24, 0);
		main_edge.texOffs(18, 44).addBox(-4, 0, -8, 8, 0, 6, 0, false);
		main_edge.texOffs(54, 17).addBox(-4, -32, -8, 8, 0, 6, 0, false);
		main_edge.texOffs(76, 33).addBox(-4, -32, -3, 8, 32, 1, 0, false);
		main_edge.texOffs(28, 52).addBox(-4, -13, -4, 8, 1, 1, 0, false);
		main_edge.texOffs(26, 50).addBox(-4, -2, -4, 8, 1, 1, 0, false);

		main_corner = new ModelMapper(modelDataWrapper);
		main_corner.setPos(0, 24, 0);
		main_corner.texOffs(20, 44).addBox(2, 0, -8, 6, 0, 6, 0, false);
		main_corner.texOffs(52, 17).addBox(2, -32, -8, 6, 0, 6, 0, false);
		main_corner.texOffs(95, 33).addBox(3, -32, -3, 5, 32, 1, 0, false);
		main_corner.texOffs(29, 52).addBox(5, -13, -4, 3, 1, 1, 0, false);
		main_corner.texOffs(27, 50).addBox(5, -2, -4, 3, 1, 1, 0, false);
		main_corner.texOffs(104, 68).addBox(2, -32, -3, 1, 32, 1, 0, false);

		handrail_bottom_r1 = new ModelMapper(modelDataWrapper);
		handrail_bottom_r1.setPos(0, 0, 0);
		main_corner.addChild(handrail_bottom_r1);
		setRotationAngle(handrail_bottom_r1, 0, -1.5708F, 0);
		handrail_bottom_r1.texOffs(36, 50).addBox(-8, -2, -4, 3, 1, 1, 0, false);
		handrail_bottom_r1.texOffs(38, 52).addBox(-8, -13, -4, 3, 1, 1, 0, false);
		handrail_bottom_r1.texOffs(95, 33).addBox(-8, -32, -3, 5, 32, 1, 0, false);

		main_exterior = new ModelMapper(modelDataWrapper);
		main_exterior.setPos(0, 24, 0);
		main_exterior.texOffs(0, 17).addBox(-8, 0, -8, 16, 1, 16, 0, false);
		main_exterior.texOffs(0, 0).addBox(-8, -33, -8, 16, 1, 16, 0, false);

		main_exterior_edge = new ModelMapper(modelDataWrapper);
		main_exterior_edge.setPos(0, 24, 0);
		main_exterior_edge.texOffs(18, 27).addBox(-4, 0, -8, 8, 1, 6, 0, false);
		main_exterior_edge.texOffs(18, 10).addBox(-4, -33, -8, 8, 1, 6, 0, false);

		main_exterior_corner = new ModelMapper(modelDataWrapper);
		main_exterior_corner.setPos(0, 24, 0);
		main_exterior_corner.texOffs(20, 27).addBox(2, 0, -8, 6, 1, 6, 0, false);
		main_exterior_corner.texOffs(20, 10).addBox(2, -33, -8, 6, 1, 6, 0, false);
		main_exterior_corner.texOffs(108, 68).addBox(2, -32, -3, 1, 32, 1, 0, false);

		main_light = new ModelMapper(modelDataWrapper);
		main_light.setPos(0, 24, 0);
		main_light.texOffs(32, 0).addBox(-8, -32.5F, -8, 16, 0, 16, 0, false);

		door = new ModelMapper(modelDataWrapper);
		door.setPos(0, 24, 0);
		door.texOffs(90, 66).addBox(-16, -32, 4, 4, 32, 3, 0, false);
		door.texOffs(14, 84).addBox(12, -32, 4, 4, 32, 3, 0, false);
		door.texOffs(20, 101).addBox(-16, 0, 0, 32, 0, 8, 0, false);
		door.texOffs(20, 101).addBox(-16, -32, 0, 32, 0, 8, 0, false);

		door_left = new ModelMapper(modelDataWrapper);
		door_left.setPos(0, 0, 0);
		door.addChild(door_left);
		door_left.texOffs(52, 68).addBox(-12, -32, 6, 12, 32, 0, 0, false);

		door_right = new ModelMapper(modelDataWrapper);
		door_right.setPos(0, 0, 0);
		door.addChild(door_right);
		door_right.texOffs(28, 68).addBox(0, -32, 6, 12, 32, 0, 0, false);

		door_exterior = new ModelMapper(modelDataWrapper);
		door_exterior.setPos(0, 24, 0);
		door_exterior.texOffs(0, 84).addBox(-16, -32, 4, 4, 32, 3, 0, false);
		door_exterior.texOffs(76, 66).addBox(12, -32, 4, 4, 32, 3, 0, false);
		door_exterior.texOffs(28, 109).addBox(-16, 0, 0, 32, 1, 8, 0, false);
		door_exterior.texOffs(0, 119).addBox(-16, -33, 0, 32, 1, 8, 0, false);

		door_left_exterior = new ModelMapper(modelDataWrapper);
		door_left_exterior.setPos(0, 0, 0);
		door_exterior.addChild(door_left_exterior);
		door_left_exterior.texOffs(0, 50).addBox(-12, -32, 6, 12, 32, 2, 0, false);

		door_right_exterior = new ModelMapper(modelDataWrapper);
		door_right_exterior.setPos(0, 0, 0);
		door_exterior.addChild(door_right_exterior);
		door_right_exterior.texOffs(48, 34).addBox(0, -32, 6, 12, 32, 2, 0, false);

		wall_patch = new ModelMapper(modelDataWrapper);
		wall_patch.setPos(0, 24, 0);


		handrail_bottom_r2 = new ModelMapper(modelDataWrapper);
		handrail_bottom_r2.setPos(0, 0, 0);
		wall_patch.addChild(handrail_bottom_r2);
		setRotationAngle(handrail_bottom_r2, 0, 1.5708F, 0);
		handrail_bottom_r2.texOffs(30, 50).addBox(-4, -2, 12, 4, 1, 1, 0, false);
		handrail_bottom_r2.texOffs(32, 52).addBox(-4, -13, 12, 4, 1, 1, 0, false);
		handrail_bottom_r2.texOffs(107, 33).addBox(-4, -32, 13, 4, 32, 1, 0, false);

		handrail_bottom_r3 = new ModelMapper(modelDataWrapper);
		handrail_bottom_r3.setPos(0, 0, 0);
		wall_patch.addChild(handrail_bottom_r3);
		setRotationAngle(handrail_bottom_r3, 0, -1.5708F, 0);
		handrail_bottom_r3.texOffs(30, 50).addBox(0, -2, 12, 4, 1, 1, 0, false);
		handrail_bottom_r3.texOffs(32, 52).addBox(0, -13, 12, 4, 1, 1, 0, false);
		handrail_bottom_r3.texOffs(107, 33).addBox(0, -32, 13, 4, 32, 1, 0, false);

		modelDataWrapper.setModelPart(textureWidth, textureHeight);
		main.setModelPart();
		main_edge.setModelPart();
		main_corner.setModelPart();
		main_exterior.setModelPart();
		main_exterior_edge.setModelPart();
		main_exterior_corner.setModelPart();
		main_light.setModelPart();
		door.setModelPart();
		door_left.setModelPart(door.name);
		door_right.setModelPart(door.name);
		door_exterior.setModelPart();
		door_left_exterior.setModelPart(door_exterior.name);
		door_right_exterior.setModelPart(door_exterior.name);
		wall_patch.setModelPart();
	}

	@Override
	protected void render(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, int currentCar, int trainCars, boolean head1IsFront, boolean renderDetails) {
		for (int i = 0; i <= width; i++) {
			for (int j = 0; j <= depth; j++) {
				final float x = (i - width / 2F) * 16;
				final float z = (j - depth / 2F) * 16;

				final boolean edge1X = i == 0;
				final boolean edge2X = i == width;
				final boolean edge1Z = j == 0;
				final boolean edge2Z = j == depth;

				switch (renderStage) {
					case LIGHTS:
						if (!edge1X && !edge2X && !edge1Z && !edge2Z) {
							ModelTrainBase.renderOnce(main_light, matrices, vertices, light, x, z);
						}
						break;
					case INTERIOR:
					case EXTERIOR:
						final ModelMapper mainPiece = renderStage == RenderStage.INTERIOR ? main : main_exterior;
						final ModelMapper mainEdgePiece = renderStage == RenderStage.INTERIOR ? main_edge : main_exterior_edge;
						final ModelMapper mainCornerPiece = renderStage == RenderStage.INTERIOR ? main_corner : main_exterior_corner;

						if (!edge1X && !edge2X && !edge1Z && !edge2Z) {
							ModelTrainBase.renderOnce(mainPiece, matrices, vertices, light, x, z);
						}

						if (edge1X && !edge2X && !edge1Z && !edge2Z) {
							mainEdgePiece.render(matrices, vertices, x, z - 4, (float) -Math.PI / 2, light, OverlayTexture.NO_OVERLAY);
							mainEdgePiece.render(matrices, vertices, x, z + 4, (float) -Math.PI / 2, light, OverlayTexture.NO_OVERLAY);
						}
						if (!edge1X && edge2X && !edge1Z && !edge2Z) {
							mainEdgePiece.render(matrices, vertices, x, z - 4, (float) Math.PI / 2, light, OverlayTexture.NO_OVERLAY);
							mainEdgePiece.render(matrices, vertices, x, z + 4, (float) Math.PI / 2, light, OverlayTexture.NO_OVERLAY);
						}
						if (!edge1X && !edge2X && !edge1Z && edge2Z && !isDoubleSided) {
							ModelTrainBase.renderOnce(mainEdgePiece, matrices, vertices, light, x - 4, z);
							ModelTrainBase.renderOnce(mainEdgePiece, matrices, vertices, light, x + 4, z);
						}

						if (edge1X && !edge2X && !edge1Z && edge2Z && (width > 2 || !isDoubleSided)) {
							mainCornerPiece.render(matrices, vertices, x, z, 0, light, OverlayTexture.NO_OVERLAY);
						}
						if (!edge1X && edge2X && !edge1Z && edge2Z && (width > 2 || !isDoubleSided)) {
							mainCornerPiece.render(matrices, vertices, x, z, (float) Math.PI / 2, light, OverlayTexture.NO_OVERLAY);
						}
						if (edge1X && !edge2X && edge1Z && !edge2Z && width > 2) {
							mainCornerPiece.render(matrices, vertices, x, z, (float) -Math.PI / 2, light, OverlayTexture.NO_OVERLAY);
						}
						if (!edge1X && edge2X && edge1Z && !edge2Z && width > 2) {
							mainCornerPiece.render(matrices, vertices, x, z, (float) Math.PI, light, OverlayTexture.NO_OVERLAY);
						}

						break;
				}
			}
		}

		if (renderStage == RenderStage.INTERIOR || renderStage == RenderStage.EXTERIOR) {
			final ModelMapper doorLeftPiece = renderStage == RenderStage.INTERIOR ? door_left : door_left_exterior;
			final ModelMapper doorRightPiece = renderStage == RenderStage.INTERIOR ? door_right : door_right_exterior;
			final ModelMapper doorPiece = renderStage == RenderStage.INTERIOR ? door : door_exterior;
			final ModelMapper mainEdgePiece = renderStage == RenderStage.INTERIOR ? main_edge : main_exterior_edge;

			doorLeftPiece.setOffset(-doorLeftZ, 0, 0);
			doorRightPiece.setOffset(doorLeftZ, 0, 0);
			ModelTrainBase.renderOnceFlipped(doorPiece, matrices, vertices, light, 8 - depth * 8);

			if (isDoubleSided) {
				doorLeftPiece.setOffset(-doorRightZ, 0, 0);
				doorRightPiece.setOffset(doorRightZ, 0, 0);
				ModelTrainBase.renderOnce(doorPiece, matrices, vertices, light, -8 + depth * 8);
			}

			if (renderStage == RenderStage.INTERIOR && width == 2) {
				ModelTrainBase.renderOnceFlipped(wall_patch, matrices, vertices, light, 8 - depth * 8);
				if (isDoubleSided) {
					ModelTrainBase.renderOnce(wall_patch, matrices, vertices, light, -8 + depth * 8);
				}
			}

			for (int i = 1; i < width - 2; i++) {
				ModelTrainBase.renderOnceFlipped(mainEdgePiece, matrices, vertices, light, i * 8 - width * 8 + 4, -depth * 8);
				ModelTrainBase.renderOnceFlipped(mainEdgePiece, matrices, vertices, light, -i * 8 + width * 8 - 4, -depth * 8);
				if (isDoubleSided) {
					ModelTrainBase.renderOnce(mainEdgePiece, matrices, vertices, light, i * 8 - width * 8 + 4, depth * 8);
					ModelTrainBase.renderOnce(mainEdgePiece, matrices, vertices, light, -i * 8 + width * 8 - 4, depth * 8);
				}
			}
		}
	}

	@Override
	protected int[] getBogiePositions() {
		return new int[0];
	}

	@Override
	protected float getDoorAnimationX(float value, boolean opening) {
		return 0;
	}

	@Override
	protected float getDoorAnimationZ(float value, boolean opening) {
		return value / 2;
	}
}
