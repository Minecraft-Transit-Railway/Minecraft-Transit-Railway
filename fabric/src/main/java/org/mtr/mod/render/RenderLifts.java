package org.mtr.mod.render;

import org.mtr.core.data.Lift;
import org.mtr.core.data.LiftDirection;
import org.mtr.core.data.LiftFloor;
import org.mtr.core.data.Position;
import org.mtr.core.tool.Vector;
import org.mtr.mapping.holder.*;
import org.mtr.mod.Init;
import org.mtr.mod.Items;
import org.mtr.mod.block.BlockLiftTrackFloor;
import org.mtr.mod.client.ClientData;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.data.IGui;
import org.mtr.mod.model.ModelLift1;
import org.mtr.mod.model.ModelSmallCube;

public class RenderLifts implements IGui {

	private static final int LIFT_DISPLAY_COLOR = 0xFFFF0000;
	private static final Identifier LIFT_TEXTURE = new Identifier("mtr:textures/vehicle/lift_1.png");
	private static final ModelSmallCube MODEL_SMALL_CUBE = new ModelSmallCube(new Identifier("textures/block/redstone_block.png"));

	public static void render() {
		final MinecraftClient minecraftClient = MinecraftClient.getInstance();
		final ClientWorld clientWorld = minecraftClient.getWorldMapped();
		final ClientPlayerEntity clientPlayerEntity = minecraftClient.getPlayerMapped();
		if (clientWorld == null || clientPlayerEntity == null) {
			return;
		}

		final boolean isHoldingRefresher = clientPlayerEntity.isHolding(Items.LIFT_REFRESHER.get());

		ClientData.getInstance().lifts.forEach(lift -> {
			if (isHoldingRefresher) {
				final LiftFloor[] previousLiftFloor = {null};

				lift.iterateFloors(liftFloor -> {
					final StoredMatrixTransformations storedMatrixTransformations = new StoredMatrixTransformations(true);
					storedMatrixTransformations.add(graphicsHolder -> {
						final Position position = liftFloor.getPosition();
						graphicsHolder.translate(position.getX(), position.getY(), position.getZ());
					});
					MODEL_SMALL_CUBE.render(storedMatrixTransformations, MAX_LIGHT_GLOWING);

					if (previousLiftFloor[0] != null) {
						final Position position1 = liftFloor.getPosition();
						final Position position2 = previousLiftFloor[0].getPosition();
						RenderTrains.scheduleRender(RenderTrains.QueuedRenderLayer.LINES, (graphicsHolder, offset) -> graphicsHolder.drawLineInWorld(
								(float) (position1.getX() - offset.getXMapped() + 0.5),
								(float) (position1.getY() - offset.getYMapped() + 0.5),
								(float) (position1.getZ() - offset.getZMapped() + 0.5),
								(float) (position2.getX() - offset.getXMapped() + 0.5),
								(float) (position2.getY() - offset.getYMapped() + 0.5),
								(float) (position2.getZ() - offset.getZMapped() + 0.5),
								ARGB_WHITE
						));
					}

					previousLiftFloor[0] = liftFloor;
				});
			}

			final StoredMatrixTransformations storedMatrixTransformations = new StoredMatrixTransformations(true);
			final Vector position = lift.getPosition();
			storedMatrixTransformations.add(graphicsHolder -> {
				graphicsHolder.translate(position.x + lift.getOffsetX(), position.y + lift.getOffsetY(), position.z + lift.getOffsetZ());
				graphicsHolder.rotateYDegrees(lift.getAngle().angleDegrees);
				graphicsHolder.rotateXDegrees(180);
			});
			final BlockPos blockPos = Init.newBlockPos(position.x, position.y, position.z);
			final int light = LightmapTextureManager.pack(clientWorld.getLightLevel(LightType.getBlockMapped(), blockPos), clientWorld.getLightLevel(LightType.getSkyMapped(), blockPos));
			new ModelLift1((int) Math.round(lift.getHeight() * 2), (int) Math.round(lift.getWidth()), (int) Math.round(lift.getDepth()), lift.getIsDoubleSided()).render(
					storedMatrixTransformations,
					null,
					LIFT_TEXTURE,
					light,
					lift.getDoorValue() * 2, lift.getDoorValue() * 2, false,
					0, 1, true, true, false, true, false
			);

			for (int i = 0; i < (lift.getIsDoubleSided() ? 2 : 1); i++) {
				final boolean shouldRotate = i == 0;
				final StoredMatrixTransformations storedMatrixTransformationsNew = storedMatrixTransformations.copy();
				storedMatrixTransformationsNew.add(graphicsHolder -> {
					if (shouldRotate) {
						graphicsHolder.rotateYDegrees(180);
					}
					graphicsHolder.translate(0.875F, -1.5, lift.getDepth() / 2 - 0.25 - SMALL_OFFSET);
				});
				renderLiftDisplay(storedMatrixTransformationsNew, new World(clientWorld.data), lift, 0.1875F, 0.3125F);
			}
		});
	}

	public static void renderLiftDisplay(StoredMatrixTransformations storedMatrixTransformations, World world, Lift lift, float width, float height) {
		final LiftDirection liftDirection = lift.getDirection();
		final LiftFloor liftFloor = lift.getCurrentFloor();
		final BlockEntity floorEntity = world.getBlockEntity(Init.positionToBlockPos(liftFloor.getPosition()));
		final String floorNumber;

		if (floorEntity != null && floorEntity.data instanceof BlockLiftTrackFloor.BlockEntity) {
			floorNumber = ((BlockLiftTrackFloor.BlockEntity) floorEntity.data).getFloorNumber();
		} else {
			floorNumber = liftFloor.getNumber();
		}

		RenderTrains.scheduleRender(RenderTrains.QueuedRenderLayer.LIGHT_TRANSLUCENT, (graphicsHolder, offset) -> {
			storedMatrixTransformations.transform(graphicsHolder, offset);
			IDrawing.drawStringWithFont(graphicsHolder, floorNumber, IGui.HorizontalAlignment.CENTER, VerticalAlignment.BOTTOM, 0, height, width, -1, 18 / width, LIFT_DISPLAY_COLOR, false, MAX_LIGHT_GLOWING, null);
			graphicsHolder.pop();
		});

		if (liftDirection != LiftDirection.NONE) {
			RenderTrains.scheduleRender(new Identifier(Init.MOD_ID, "textures/block/sign/lift_arrow.png"), false, RenderTrains.QueuedRenderLayer.LIGHT_TRANSLUCENT, (graphicsHolder, offset) -> {
				storedMatrixTransformations.transform(graphicsHolder, offset);
				IDrawing.drawTexture(graphicsHolder, -width / 6, 0, width / 3, width / 3, 0, liftDirection == LiftDirection.UP ? 0 : 1, 1, liftDirection == LiftDirection.UP ? 1 : 0, Direction.UP, LIFT_DISPLAY_COLOR, MAX_LIGHT_GLOWING);
				graphicsHolder.pop();
			});
		}
	}
}
