package org.mtr.mod.model;

import org.mtr.mapping.holder.OverlayTexture;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.ModelPartExtension;
import org.mtr.mod.Keys;
import org.mtr.mod.client.DoorAnimationType;
import org.mtr.mod.resource.RenderStage;

public class ModelLift1 extends ModelTrainBase {

	private final ModelPartExtension main;
	private final ModelPartExtension main_ceiling;
	private final ModelPartExtension main_edge;
	private final ModelPartExtension main_edge_wall;
	private final ModelPartExtension main_edge_ceiling;
	private final ModelPartExtension main_corner;
	private final ModelPartExtension main_corner_wall;
	private final ModelPartExtension main_corner_ceiling;
	private final ModelPartExtension main_exterior;
	private final ModelPartExtension main_exterior_ceiling;
	private final ModelPartExtension main_exterior_edge;
	private final ModelPartExtension main_exterior_edge_ceiling;
	private final ModelPartExtension main_exterior_corner;
	private final ModelPartExtension main_exterior_corner_wall;
	private final ModelPartExtension main_exterior_corner_ceiling;
	private final ModelPartExtension main_light;
	private final ModelPartExtension door;
	private final ModelPartExtension door_left;
	private final ModelPartExtension door_right;
	private final ModelPartExtension door_wall;
	private final ModelPartExtension door_ceiling;
	private final ModelPartExtension door_exterior;
	private final ModelPartExtension door_left_exterior;
	private final ModelPartExtension door_right_exterior;
	private final ModelPartExtension door_wall_exterior;
	private final ModelPartExtension door_ceiling_exterior;
	private final ModelPartExtension wall_patch;
	private final ModelPartExtension wall_patch_wall;

	private final int heightCount;
	private final int heightOffset;
	private final int width;
	private final int depth;
	private final boolean isDoubleSided;

	public ModelLift1(int height, int width, int depth, boolean isDoubleSided) {
		super(128, 128, DoorAnimationType.CONSTANT, false);
		heightCount = height - 4;
		heightOffset = -heightCount * 8;
		this.width = width;
		this.depth = depth;
		this.isDoubleSided = isDoubleSided;

		main = createModelPart();
		main.setPivot(0, 24, 0);
		main.setTextureUVOffset(0, 34).addCuboid(-8, 0, -8, 16, 0, 16, 0, false);

		main_ceiling = createModelPart();
		main_ceiling.setPivot(0, 24, 0);
		main_ceiling.setTextureUVOffset(79, 44).addCuboid(-8, -32, -8, 16, 0, 16, 0, false);

		main_edge = createModelPart();
		main_edge.setPivot(0, 24, 0);
		main_edge.setTextureUVOffset(18, 44).addCuboid(-4, 0, -8, 8, 0, 6, 0, false);
		main_edge.setTextureUVOffset(76, 33).addCuboid(-4, -32, -3, 8, 32, 1, 0, false);
		main_edge.setTextureUVOffset(28, 52).addCuboid(-4, -13, -4, 8, 1, 1, 0, false);
		main_edge.setTextureUVOffset(26, 50).addCuboid(-4, -2, -4, 8, 1, 1, 0, false);

		main_edge_wall = createModelPart();
		main_edge_wall.setPivot(0, 24, 0);
		main_edge_wall.setTextureUVOffset(76, 33).addCuboid(-4, -40, -3, 8, 8, 1, 0, false);

		main_edge_ceiling = createModelPart();
		main_edge_ceiling.setPivot(0, 24, 0);
		main_edge_ceiling.setTextureUVOffset(97, 44).addCuboid(-4, -32, -8, 8, 0, 6, 0, false);

		main_corner = createModelPart();
		main_corner.setPivot(0, 24, 0);
		main_corner.setTextureUVOffset(20, 44).addCuboid(2, 0, -8, 6, 0, 6, 0, false);
		main_corner.setTextureUVOffset(112, 62).addCuboid(3, -32, -3, 5, 32, 1, 0, false);
		main_corner.setTextureUVOffset(29, 52).addCuboid(5, -13, -4, 3, 1, 1, 0, false);
		main_corner.setTextureUVOffset(27, 50).addCuboid(5, -2, -4, 3, 1, 1, 0, false);
		main_corner.setTextureUVOffset(104, 68).addCuboid(2, -32, -3, 1, 32, 1, 0, false);

		final ModelPartExtension handrail_bottom_r1 = main_corner.addChild();
		handrail_bottom_r1.setPivot(0, 0, 0);
		setRotationAngle(handrail_bottom_r1, 0, -1.5708F, 0);
		handrail_bottom_r1.setTextureUVOffset(36, 50).addCuboid(-8, -2, -4, 3, 1, 1, 0, false);
		handrail_bottom_r1.setTextureUVOffset(38, 52).addCuboid(-8, -13, -4, 3, 1, 1, 0, false);
		handrail_bottom_r1.setTextureUVOffset(112, 62).addCuboid(-8, -32, -3, 5, 32, 1, 0, false);

		main_corner_wall = createModelPart();
		main_corner_wall.setPivot(0, 24, 0);
		main_corner_wall.setTextureUVOffset(112, 62).addCuboid(3, -40, -3, 5, 8, 1, 0, false);
		main_corner_wall.setTextureUVOffset(104, 68).addCuboid(2, -40, -3, 1, 8, 1, 0, false);

		final ModelPartExtension wall_r1 = main_corner_wall.addChild();
		wall_r1.setPivot(0, 0, 0);
		setRotationAngle(wall_r1, 0, -1.5708F, 0);
		wall_r1.setTextureUVOffset(112, 62).addCuboid(-8, -40, -3, 5, 8, 1, 0, false);

		main_corner_ceiling = createModelPart();
		main_corner_ceiling.setPivot(0, 24, 0);
		main_corner_ceiling.setTextureUVOffset(99, 44).addCuboid(2, -32, -8, 6, 0, 6, 0, false);

		main_exterior = createModelPart();
		main_exterior.setPivot(0, 24, 0);
		main_exterior.setTextureUVOffset(0, 17).addCuboid(-8, 0, -8, 16, 1, 16, 0, false);

		main_exterior_ceiling = createModelPart();
		main_exterior_ceiling.setPivot(0, 24, 0);
		main_exterior_ceiling.setTextureUVOffset(0, 0).addCuboid(-8, -33, -8, 16, 1, 16, 0, false);

		main_exterior_edge = createModelPart();
		main_exterior_edge.setPivot(0, 24, 0);
		main_exterior_edge.setTextureUVOffset(18, 27).addCuboid(-4, 0, -8, 8, 1, 6, 0, false);

		main_exterior_edge_ceiling = createModelPart();
		main_exterior_edge_ceiling.setPivot(0, 24, 0);
		main_exterior_edge_ceiling.setTextureUVOffset(18, 10).addCuboid(-4, -33, -8, 8, 1, 6, 0, false);

		main_exterior_corner = createModelPart();
		main_exterior_corner.setPivot(0, 24, 0);
		main_exterior_corner.setTextureUVOffset(20, 27).addCuboid(2, 0, -8, 6, 1, 6, 0, false);

		main_exterior_corner_wall = createModelPart();
		main_exterior_corner_wall.setPivot(0, 24, 0);
		main_exterior_corner_wall.setTextureUVOffset(108, 68).addCuboid(2, -40, -3, 1, 8, 1, 0, false);

		main_exterior_corner_ceiling = createModelPart();
		main_exterior_corner_ceiling.setPivot(0, 24, 0);
		main_exterior_corner_ceiling.setTextureUVOffset(20, 10).addCuboid(2, -33, -8, 6, 1, 6, 0, false);

		main_light = createModelPart();
		main_light.setPivot(0, 24, 0);
		main_light.setTextureUVOffset(79, 28).addCuboid(-8, -32.5F, -8, 16, 0, 16, 0, false);

		door = createModelPart();
		door.setPivot(0, 24, 0);
		door.setTextureUVOffset(90, 66).addCuboid(-16, -32, 4, 4, 32, 3, 0, false);
		door.setTextureUVOffset(14, 84).addCuboid(12, -32, 4, 4, 32, 3, 0, false);
		door.setTextureUVOffset(20, 101).addCuboid(-16, 0, 0, 32, 0, 8, 0, false);

		door_left = createModelPart();
		door_left.setPivot(0, 24, 0);
		door_left.setTextureUVOffset(52, 68).addCuboid(-12, -32, 6, 12, 32, 0, 0, false);

		door_right = createModelPart();
		door_right.setPivot(0, 24, 0);
		door_right.setTextureUVOffset(28, 68).addCuboid(0, -32, 6, 12, 32, 0, 0, false);

		door_wall = createModelPart();
		door_wall.setPivot(0, 24, 0);
		door_wall.setTextureUVOffset(48, 0).addCuboid(-16, -40, 4, 32, 8, 3, 0, false);

		door_ceiling = createModelPart();
		door_ceiling.setPivot(0, 24, 0);
		door_ceiling.setTextureUVOffset(20, 101).addCuboid(-16, -32, 0, 32, 0, 8, 0, false);

		door_exterior = createModelPart();
		door_exterior.setPivot(0, 24, 0);
		door_exterior.setTextureUVOffset(0, 84).addCuboid(-16, -32, 4, 4, 32, 3, 0, false);
		door_exterior.setTextureUVOffset(76, 66).addCuboid(12, -32, 4, 4, 32, 3, 0, false);
		door_exterior.setTextureUVOffset(28, 109).addCuboid(-16, 0, 0, 32, 1, 8, 0, false);

		door_left_exterior = createModelPart();
		door_left_exterior.setPivot(0, 24, 0);
		door_left_exterior.setTextureUVOffset(0, 50).addCuboid(-12, -32, 6, 12, 32, 2, 0, false);

		door_right_exterior = createModelPart();
		door_right_exterior.setPivot(0, 24, 0);
		door_right_exterior.setTextureUVOffset(48, 34).addCuboid(0, -32, 6, 12, 32, 2, 0, false);

		door_wall_exterior = createModelPart();
		door_wall_exterior.setPivot(0, 24, 0);
		door_wall_exterior.setTextureUVOffset(48, 17).addCuboid(-16, -40, 5, 32, 8, 3, 0, false);

		door_ceiling_exterior = createModelPart();
		door_ceiling_exterior.setPivot(0, 24, 0);
		door_ceiling_exterior.setTextureUVOffset(0, 119).addCuboid(-16, -33, 0, 32, 1, 8, 0, false);

		wall_patch = createModelPart();
		wall_patch.setPivot(0, 24, 0);


		final ModelPartExtension wall_r2 = wall_patch.addChild();
		wall_r2.setPivot(0, 0, 0);
		setRotationAngle(wall_r2, 0, -1.5708F, 0);
		wall_r2.setTextureUVOffset(108, 95).addCuboid(0, -32, 13, 4, 32, 1, 0, false);
		wall_r2.setTextureUVOffset(30, 50).addCuboid(0, -2, 12, 4, 1, 1, 0, false);
		wall_r2.setTextureUVOffset(32, 52).addCuboid(0, -13, 12, 4, 1, 1, 0, false);

		final ModelPartExtension wall_r3 = wall_patch.addChild();
		wall_r3.setPivot(0, 0, 0);
		setRotationAngle(wall_r3, 0, 1.5708F, 0);
		wall_r3.setTextureUVOffset(108, 95).addCuboid(-4, -32, 13, 4, 32, 1, 0, false);
		wall_r3.setTextureUVOffset(30, 50).addCuboid(-4, -2, 12, 4, 1, 1, 0, false);
		wall_r3.setTextureUVOffset(32, 52).addCuboid(-4, -13, 12, 4, 1, 1, 0, false);

		wall_patch_wall = createModelPart();
		wall_patch_wall.setPivot(0, 24, 0);


		final ModelPartExtension wall_r4 = wall_patch_wall.addChild();
		wall_r4.setPivot(0, 0, 0);
		setRotationAngle(wall_r4, 0, -1.5708F, 0);
		wall_r4.setTextureUVOffset(108, 95).addCuboid(0, -40, 13, 4, 8, 1, 0, false);

		final ModelPartExtension wall_r5 = wall_patch_wall.addChild();
		wall_r5.setPivot(0, 0, 0);
		setRotationAngle(wall_r5, 0, 1.5708F, 0);
		wall_r5.setTextureUVOffset(108, 95).addCuboid(-4, -40, 13, 4, 8, 1, 0, false);

		buildModel();
	}

	@Override
	protected void render(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, int currentCar, int trainCars, boolean head1IsFront, boolean renderDetails) {
		for (int i = 0; i <= width; i++) {
			for (int j = 0; j <= depth; j++) {
				final float x = (i - width / 2F) * 16;
				final float z = (j - depth / 2F) * 16;

				final boolean edge1X = i == 0;
				final boolean edge2X = i == width;
				final boolean edge1Z = j == 0;
				final boolean edge2Z = j == depth;

				switch (renderStage) {
					case LIGHT:
						if (!edge1X && !edge2X && !edge1Z && !edge2Z) {
							ModelTrainBase.renderOnce(main_light, graphicsHolder, light, x, heightOffset, z);
						}
						break;
					case INTERIOR:
					case EXTERIOR:
						final ModelPartExtension mainPiece = renderStage == RenderStage.INTERIOR ? main : main_exterior;
						final ModelPartExtension mainCeilingPiece = renderStage == RenderStage.INTERIOR ? main_ceiling : main_exterior_ceiling;
						final ModelPartExtension mainEdgePiece = renderStage == RenderStage.INTERIOR ? main_edge : main_exterior_edge;
						final ModelPartExtension mainEdgeCeilingPiece = renderStage == RenderStage.INTERIOR ? main_edge_ceiling : main_exterior_edge_ceiling;
						final ModelPartExtension mainCornerPiece = renderStage == RenderStage.INTERIOR ? main_corner : main_exterior_corner;
						final ModelPartExtension mainCornerWallPiece = renderStage == RenderStage.INTERIOR ? main_corner_wall : main_exterior_corner_wall;
						final ModelPartExtension mainCornerCeilingPiece = renderStage == RenderStage.INTERIOR ? main_corner_ceiling : main_exterior_corner_ceiling;

						if (!edge1X && !edge2X && !edge1Z && !edge2Z) {
							ModelTrainBase.renderOnce(mainPiece, graphicsHolder, light, x, z);
							ModelTrainBase.renderOnce(mainCeilingPiece, graphicsHolder, light, x, heightOffset, z);
						}

						if (edge1X && !edge2X && !edge1Z && !edge2Z) {
							mainEdgePiece.render(graphicsHolder, x, z - 4, (float) -Math.PI / 2, light, OverlayTexture.getDefaultUvMapped());
							mainEdgePiece.render(graphicsHolder, x, z + 4, (float) -Math.PI / 2, light, OverlayTexture.getDefaultUvMapped());
							// TODO edge exterior pieces
							if (renderStage == RenderStage.INTERIOR) {
								renderWall(main_edge_wall, graphicsHolder, x, z - 4, (float) -Math.PI / 2, light);
								renderWall(main_edge_wall, graphicsHolder, x, z + 4, (float) -Math.PI / 2, light);
							}
							mainEdgeCeilingPiece.render(graphicsHolder, x, heightOffset, z - 4, (float) -Math.PI / 2, light, OverlayTexture.getDefaultUvMapped());
							mainEdgeCeilingPiece.render(graphicsHolder, x, heightOffset, z + 4, (float) -Math.PI / 2, light, OverlayTexture.getDefaultUvMapped());
						}
						if (!edge1X && edge2X && !edge1Z && !edge2Z) {
							mainEdgePiece.render(graphicsHolder, x, z - 4, (float) Math.PI / 2, light, OverlayTexture.getDefaultUvMapped());
							mainEdgePiece.render(graphicsHolder, x, z + 4, (float) Math.PI / 2, light, OverlayTexture.getDefaultUvMapped());
							// TODO edge exterior pieces
							if (renderStage == RenderStage.INTERIOR) {
								renderWall(main_edge_wall, graphicsHolder, x, z - 4, (float) Math.PI / 2, light);
								renderWall(main_edge_wall, graphicsHolder, x, z + 4, (float) Math.PI / 2, light);
							}
							mainEdgeCeilingPiece.render(graphicsHolder, x, heightOffset, z - 4, (float) Math.PI / 2, light, OverlayTexture.getDefaultUvMapped());
							mainEdgeCeilingPiece.render(graphicsHolder, x, heightOffset, z + 4, (float) Math.PI / 2, light, OverlayTexture.getDefaultUvMapped());
						}
						if (!edge1X && !edge2X && !edge1Z && edge2Z && !isDoubleSided) {
							ModelTrainBase.renderOnce(mainEdgePiece, graphicsHolder, light, x - 4, z);
							ModelTrainBase.renderOnce(mainEdgePiece, graphicsHolder, light, x + 4, z);
							// TODO edge exterior pieces
							if (renderStage == RenderStage.INTERIOR) {
								renderWallOnce(main_edge_wall, graphicsHolder, x - 4, z, light);
								renderWallOnce(main_edge_wall, graphicsHolder, x + 4, z, light);
							}
							ModelTrainBase.renderOnce(mainEdgeCeilingPiece, graphicsHolder, light, x - 4, heightOffset, z);
							ModelTrainBase.renderOnce(mainEdgeCeilingPiece, graphicsHolder, light, x + 4, heightOffset, z);
						}

						if (edge1X && !edge2X && !edge1Z && edge2Z && (width > 2 || !isDoubleSided)) {
							mainCornerPiece.render(graphicsHolder, x, z, 0, light, OverlayTexture.getDefaultUvMapped());
							// TODO edge exterior pieces
							if (renderStage == RenderStage.INTERIOR) {
								renderWall(mainCornerWallPiece, graphicsHolder, x, z, 0, light);
							}
							mainCornerCeilingPiece.render(graphicsHolder, x, heightOffset, z, 0, light, OverlayTexture.getDefaultUvMapped());
						}
						if (!edge1X && edge2X && !edge1Z && edge2Z && (width > 2 || !isDoubleSided)) {
							mainCornerPiece.render(graphicsHolder, x, z, (float) Math.PI / 2, light, OverlayTexture.getDefaultUvMapped());
							// TODO edge exterior pieces
							if (renderStage == RenderStage.INTERIOR) {
								renderWall(mainCornerWallPiece, graphicsHolder, x, z, (float) Math.PI / 2, light);
							}
							mainCornerCeilingPiece.render(graphicsHolder, x, heightOffset, z, (float) Math.PI / 2, light, OverlayTexture.getDefaultUvMapped());
						}
						if (edge1X && !edge2X && edge1Z && !edge2Z && width > 2) {
							mainCornerPiece.render(graphicsHolder, x, z, (float) -Math.PI / 2, light, OverlayTexture.getDefaultUvMapped());
							// TODO edge exterior pieces
							if (renderStage == RenderStage.INTERIOR) {
								renderWall(mainCornerWallPiece, graphicsHolder, x, z, (float) -Math.PI / 2, light);
							}
							mainCornerCeilingPiece.render(graphicsHolder, x, heightOffset, z, (float) -Math.PI / 2, light, OverlayTexture.getDefaultUvMapped());
						}
						if (!edge1X && edge2X && edge1Z && !edge2Z && width > 2) {
							mainCornerPiece.render(graphicsHolder, x, z, (float) Math.PI, light, OverlayTexture.getDefaultUvMapped());
							// TODO edge exterior pieces
							if (renderStage == RenderStage.INTERIOR) {
								renderWall(mainCornerWallPiece, graphicsHolder, x, z, (float) Math.PI, light);
							}
							mainCornerCeilingPiece.render(graphicsHolder, x, heightOffset, z, (float) Math.PI, light, OverlayTexture.getDefaultUvMapped());
						}

						break;
				}
			}
		}

		if (renderStage == RenderStage.INTERIOR || renderStage == RenderStage.EXTERIOR) {
			final ModelPartExtension doorLeftPiece = renderStage == RenderStage.INTERIOR ? door_left : door_left_exterior;
			final ModelPartExtension doorRightPiece = renderStage == RenderStage.INTERIOR ? door_right : door_right_exterior;
			final ModelPartExtension doorPiece = renderStage == RenderStage.INTERIOR ? door : door_exterior;
			final ModelPartExtension doorWallPiece = renderStage == RenderStage.INTERIOR ? door_wall : door_wall_exterior;
			final ModelPartExtension doorCeilingPiece = renderStage == RenderStage.INTERIOR ? door_ceiling : door_ceiling_exterior;
			final ModelPartExtension mainEdgePiece = renderStage == RenderStage.INTERIOR ? main_edge : main_exterior_edge;
			final ModelPartExtension mainEdgeCeilingPiece = renderStage == RenderStage.INTERIOR ? main_edge_ceiling : main_exterior_edge_ceiling;

			ModelTrainBase.renderOnceFlipped(doorLeftPiece, graphicsHolder, light, -doorLeftZ, 8 - depth * 8);
			ModelTrainBase.renderOnceFlipped(doorRightPiece, graphicsHolder, light, doorLeftZ, 8 - depth * 8);
			ModelTrainBase.renderOnceFlipped(doorPiece, graphicsHolder, light, 8 - depth * 8);
			renderWallOnceFlipped(doorWallPiece, graphicsHolder, 0, 8 - depth * 8, light);
			ModelTrainBase.renderOnceFlipped(doorCeilingPiece, graphicsHolder, light, 0, heightOffset, 8 - depth * 8);

			if (isDoubleSided) {
				ModelTrainBase.renderOnce(doorLeftPiece, graphicsHolder, light, -doorRightZ, -8 + depth * 8);
				ModelTrainBase.renderOnce(doorRightPiece, graphicsHolder, light, doorRightZ, -8 + depth * 8);
				ModelTrainBase.renderOnce(doorPiece, graphicsHolder, light, -8 + depth * 8);
				renderWallOnce(doorWallPiece, graphicsHolder, 0, -8 + depth * 8, light);
				ModelTrainBase.renderOnce(doorCeilingPiece, graphicsHolder, light, 0, heightOffset, -8 + depth * 8);
			}

			if (renderStage == RenderStage.INTERIOR && width == 2) {
				ModelTrainBase.renderOnceFlipped(wall_patch, graphicsHolder, light, 8 - depth * 8);
				renderWallOnceFlipped(wall_patch_wall, graphicsHolder, 0, 8 - depth * 8, light);
				if (isDoubleSided) {
					ModelTrainBase.renderOnce(wall_patch, graphicsHolder, light, -8 + depth * 8);
					renderWallOnce(wall_patch_wall, graphicsHolder, 0, -8 + depth * 8, light);
				}
			}

			for (int i = 1; i < width - 2; i++) {
				ModelTrainBase.renderOnceFlipped(mainEdgePiece, graphicsHolder, light, i * 8 - width * 8 + 4, -depth * 8);
				ModelTrainBase.renderOnceFlipped(mainEdgePiece, graphicsHolder, light, -i * 8 + width * 8 - 4, -depth * 8);
				// TODO edge exterior pieces
				if (renderStage == RenderStage.INTERIOR) {
					renderWallOnceFlipped(main_edge_wall, graphicsHolder, i * 8 - width * 8 + 4, -depth * 8, light);
					renderWallOnceFlipped(main_edge_wall, graphicsHolder, -i * 8 + width * 8 - 4, -depth * 8, light);
				}
				ModelTrainBase.renderOnceFlipped(mainEdgeCeilingPiece, graphicsHolder, light, i * 8 - width * 8 + 4, heightOffset, -depth * 8);
				ModelTrainBase.renderOnceFlipped(mainEdgeCeilingPiece, graphicsHolder, light, -i * 8 + width * 8 - 4, heightOffset, -depth * 8);
				if (isDoubleSided) {
					ModelTrainBase.renderOnce(mainEdgePiece, graphicsHolder, light, i * 8 - width * 8 + 4, depth * 8);
					ModelTrainBase.renderOnce(mainEdgePiece, graphicsHolder, light, -i * 8 + width * 8 - 4, depth * 8);
					// TODO edge exterior pieces
					if (renderStage == RenderStage.INTERIOR) {
						renderWallOnce(main_edge_wall, graphicsHolder, i * 8 - width * 8 + 4, depth * 8, light);
						renderWallOnce(main_edge_wall, graphicsHolder, -i * 8 + width * 8 - 4, depth * 8, light);
					}
					ModelTrainBase.renderOnce(mainEdgeCeilingPiece, graphicsHolder, light, i * 8 - width * 8 + 4, heightOffset, depth * 8);
					ModelTrainBase.renderOnce(mainEdgeCeilingPiece, graphicsHolder, light, -i * 8 + width * 8 - 4, heightOffset, depth * 8);
				}
			}
		}
	}

	@Override
	protected void baseTransform(GraphicsHolder graphicsHolder) {
		graphicsHolder.translate(0, Keys.MOD_VERSION.endsWith("1.16.5") ? 0 : -1.5, 0); // 1.16.5 lift offset bug
	}

	@Override
	protected int getDoorMax() {
		return 24 / 4;
	}

	private void renderWall(ModelPartExtension model, GraphicsHolder graphicsHolder, float x, float z, float rotateY, int light) {
		for (int i = 0; i < heightCount; i++) {
			model.render(graphicsHolder, x, -i * 8, z, rotateY, light, OverlayTexture.getDefaultUvMapped());
		}
	}

	private void renderWallOnce(ModelPartExtension model, GraphicsHolder graphicsHolder, float x, float z, int light) {
		for (int i = 0; i < heightCount; i++) {
			ModelTrainBase.renderOnce(model, graphicsHolder, light, x, -i * 8, z);
		}
	}

	private void renderWallOnceFlipped(ModelPartExtension model, GraphicsHolder graphicsHolder, float x, float z, int light) {
		for (int i = 0; i < heightCount; i++) {
			ModelTrainBase.renderOnceFlipped(model, graphicsHolder, light, x, -i * 8, z);
		}
	}
}
