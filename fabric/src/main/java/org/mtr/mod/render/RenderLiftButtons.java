package org.mtr.mod.render;

import org.mtr.core.data.Lift;
import org.mtr.core.data.LiftDirection;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArraySet;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityRenderer;
import org.mtr.mapping.mapper.DirectionHelper;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.PlayerHelper;
import org.mtr.mod.Init;
import org.mtr.mod.block.BlockLiftButtons;
import org.mtr.mod.block.BlockLiftTrackFloor;
import org.mtr.mod.block.IBlock;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.data.IGui;
import org.mtr.mod.item.ItemLiftButtonsLinkModifier;

import java.util.Comparator;

public class RenderLiftButtons extends BlockEntityRenderer<BlockLiftButtons.BlockEntity> implements DirectionHelper, IGui, IBlock {

	private static final int HOVER_COLOR = 0xFFFFAAAA;
	private static final int PRESSED_COLOR = 0xFFFF0000;
	private static final Identifier BUTTON_TEXTURE = new Identifier(Init.MOD_ID, "textures/block/lift_button.png");

	public RenderLiftButtons(Argument dispatcher) {
		super(dispatcher);
	}

	@Override
	public void render(BlockLiftButtons.BlockEntity blockEntity, float tickDelta, GraphicsHolder graphicsHolder1, int light, int overlay) {
		final World world = blockEntity.getWorld2();
		if (world == null) {
			return;
		}

		final ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().getPlayerMapped();
		if (clientPlayerEntity == null) {
			return;
		}

		final BlockPos blockPos = blockEntity.getPos2();
		final BlockState blockState = world.getBlockState(blockPos);
		final Direction facing = IBlock.getStatePropertySafe(blockState, FACING);
		final boolean holdingLinker = PlayerHelper.isHolding(PlayerEntity.cast(clientPlayerEntity), item -> item.data instanceof ItemLiftButtonsLinkModifier || Block.getBlockFromItem(item).data instanceof BlockLiftButtons);

		final StoredMatrixTransformations storedMatrixTransformations1 = new StoredMatrixTransformations(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5);

		// Array order: has down button, has up button, pressed down button, pressed up button
		final boolean[] buttonStates = {false, false, false, false};
		final ObjectArrayList<ObjectObjectImmutablePair<BlockPos, Lift>> sortedPositionsAndLifts = new ObjectArrayList<>();

		blockEntity.forEachTrackPosition(trackPosition -> {
			// Render track link if holding linker item
			if (world.getBlockState(trackPosition).getBlock().data instanceof BlockLiftTrackFloor) {
				final Direction trackFacing = IBlock.getStatePropertySafe(world, trackPosition, FACING);
				renderLiftObjectLink(
						storedMatrixTransformations1, world,
						new Vector3d(facing.getOffsetX() / 2F, 0.5, facing.getOffsetZ() / 2F),
						new Vector3d(trackPosition.getX() - blockPos.getX() + trackFacing.getOffsetX() / 2F, trackPosition.getY() - blockPos.getY() + 0.5, trackPosition.getZ() - blockPos.getZ() + trackFacing.getOffsetZ() / 2F),
						holdingLinker
				);
			}

			// Figure out whether the up and down buttons should be rendered
			BlockLiftButtons.hasButtonsClient(trackPosition, buttonStates, (floorIndex, lift) -> {
				sortedPositionsAndLifts.add(new ObjectObjectImmutablePair<>(trackPosition, lift));
				final ObjectArraySet<LiftDirection> instructionDirections = lift.hasInstruction(floorIndex);
				instructionDirections.forEach(liftDirection -> {
					switch (liftDirection) {
						case DOWN:
							buttonStates[2] = true;
							break;
						case UP:
							buttonStates[3] = true;
							break;
					}
				});
			});
		});

		// Sort list and only render the two closest lifts
		sortedPositionsAndLifts.sort(Comparator.comparingInt(sortedPositionAndLift -> blockPos.getManhattanDistance(new Vector3i(sortedPositionAndLift.left().data))));

		// Check whether the player is looking at the top or bottom button
		final HitResult hitResult = MinecraftClient.getInstance().getCrosshairTargetMapped();
		final boolean lookingAtTopHalf;
		final boolean lookingAtBottomHalf;
		if (clientPlayerEntity.isSpectator() || hitResult == null || !IBlock.getStatePropertySafe(blockState, BlockLiftButtons.UNLOCKED)) {
			lookingAtTopHalf = false;
			lookingAtBottomHalf = false;
		} else {
			final Vector3d hitLocation = hitResult.getPos();
			final double hitY = MathHelper.fractionalPart(hitLocation.getYMapped());
			final boolean inBlock = hitY < 0.5 && Init.newBlockPos(hitLocation.getXMapped(), hitLocation.getYMapped(), hitLocation.getZMapped()).equals(blockPos);
			lookingAtTopHalf = inBlock && (!buttonStates[0] || hitY > 0.25);
			lookingAtBottomHalf = inBlock && (!buttonStates[1] || hitY < 0.25);
		}

		final StoredMatrixTransformations storedMatrixTransformations2 = storedMatrixTransformations1.copy();
		storedMatrixTransformations2.add(graphicsHolder -> {
			graphicsHolder.rotateYDegrees(-facing.asRotation());
			graphicsHolder.translate(0, 0, 0.4375 - SMALL_OFFSET);
		});

		// Render buttons
		if (buttonStates[0]) {
			MainRenderer.scheduleRender(BUTTON_TEXTURE, false, buttonStates[2] || lookingAtBottomHalf ? QueuedRenderLayer.LIGHT_TRANSLUCENT : QueuedRenderLayer.EXTERIOR, (graphicsHolder, offset) -> {
				storedMatrixTransformations2.transform(graphicsHolder, offset);
				IDrawing.drawTexture(graphicsHolder, -1.5F / 16, (buttonStates[1] ? 0.5F : 2.5F) / 16, 3F / 16, 3F / 16, 0, 0, 1, 1, facing, buttonStates[2] ? PRESSED_COLOR : lookingAtBottomHalf ? HOVER_COLOR : ARGB_GRAY, light);
				graphicsHolder.pop();
			});
		}
		if (buttonStates[1]) {
			MainRenderer.scheduleRender(BUTTON_TEXTURE, false, buttonStates[3] || lookingAtTopHalf ? QueuedRenderLayer.LIGHT_TRANSLUCENT : QueuedRenderLayer.EXTERIOR, (graphicsHolder, offset) -> {
				storedMatrixTransformations2.transform(graphicsHolder, offset);
				IDrawing.drawTexture(graphicsHolder, -1.5F / 16, (buttonStates[0] ? 4.5F : 2.5F) / 16, 3F / 16, 3F / 16, 0, 1, 1, 0, facing, buttonStates[3] ? PRESSED_COLOR : lookingAtTopHalf ? HOVER_COLOR : ARGB_GRAY, light);
				graphicsHolder.pop();
			});
		}

		// Render the floor display
		if (!sortedPositionsAndLifts.isEmpty()) {
			final int count = Math.min(2, sortedPositionsAndLifts.size());
			final float width = count == 1 ? 0.25F : 0.375F;

			final StoredMatrixTransformations storedMatrixTransformations3 = storedMatrixTransformations2.copy();
			storedMatrixTransformations3.add(graphicsHolder -> {
				graphicsHolder.rotateZDegrees(180);
				graphicsHolder.translate(-width / 2, 0, 0);
			});

			// Render the black background
			MainRenderer.scheduleRender(new Identifier(Init.MOD_ID, "textures/block/black.png"), false, QueuedRenderLayer.EXTERIOR, (graphicsHolder, offset) -> {
				storedMatrixTransformations3.transform(graphicsHolder, offset);
				IDrawing.drawTexture(graphicsHolder, 0, -0.9375F, width, 0.40625F, Direction.UP, light);
				graphicsHolder.pop();
			});

			// Check if the two closest lifts are visually in order, based on the direction the buttons are facing
			final boolean reverseRendering = count > 1 && reverseRendering(facing.rotateYCounterclockwise(), sortedPositionsAndLifts.get(0).left(), sortedPositionsAndLifts.get(1).left());
			for (int i = 0; i < count; i++) {
				final double x = ((reverseRendering ? count - i - 1 : i) + 0.5) * width / count;
				final StoredMatrixTransformations storedMatrixTransformations4 = storedMatrixTransformations3.copy();
				storedMatrixTransformations4.add(graphicsHolder -> graphicsHolder.translate(x, -0.875, -SMALL_OFFSET));
				RenderLifts.renderLiftDisplay(storedMatrixTransformations4, world, sortedPositionsAndLifts.get(i).right(), width / count, 0.3125F);
			}
		}
	}

	public static void renderLiftObjectLink(StoredMatrixTransformations storedMatrixTransformations, World world, Vector3d position1, Vector3d position2, boolean holdingLinker) {
		if (holdingLinker) {
			MainRenderer.scheduleRender(QueuedRenderLayer.LINES, (graphicsHolder, offset) -> {
				storedMatrixTransformations.transform(graphicsHolder, offset);
				graphicsHolder.drawLineInWorld(
						(float) position1.getXMapped(),
						(float) position1.getYMapped(),
						(float) position1.getZMapped(),
						(float) position2.getXMapped(),
						(float) position2.getYMapped(),
						(float) position2.getZMapped(),
						ARGB_WHITE
				);
				graphicsHolder.pop();
			});
		}
	}

	private static boolean reverseRendering(Direction direction, BlockPos blockPos1, BlockPos blockPos2) {
		if (direction.getOffsetX() != 0) {
			return Math.signum(blockPos2.getX() - blockPos1.getX()) == direction.getOffsetX();
		} else if (direction.getOffsetZ() != 0) {
			return Math.signum(blockPos2.getZ() - blockPos1.getZ()) == direction.getOffsetZ();
		} else {
			return false;
		}
	}
}
