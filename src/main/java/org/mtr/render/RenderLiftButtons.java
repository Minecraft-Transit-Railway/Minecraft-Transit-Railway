package org.mtr.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.mtr.MTR;
import org.mtr.block.BlockLiftButtons;
import org.mtr.block.BlockLiftTrackFloor;
import org.mtr.block.IBlock;
import org.mtr.client.IDrawing;
import org.mtr.core.data.Lift;
import org.mtr.core.data.LiftDirection;
import org.mtr.data.IGui;
import org.mtr.item.ItemLiftButtonsLinkModifier;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArraySet;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import org.mtr.tool.Drawing;

import java.util.Comparator;

public class RenderLiftButtons extends BlockEntityRendererExtension<BlockLiftButtons.LiftButtonsBlockEntity> implements IGui, IBlock {

	private static final int HOVER_COLOR = 0xFFFFAAAA;
	private static final int PRESSED_COLOR = 0xFFFF0000;
	private static final ResourceLocation BUTTON_TEXTURE = ResourceLocation.fromNamespaceAndPath(MTR.MOD_ID, "textures/block/lift_button.png");

	@Override
	public void render(BlockLiftButtons.LiftButtonsBlockEntity blockEntity, PoseStack matrixStack2, MultiBufferSource vertexConsumerProvider, ClientLevel world, LocalPlayer player, float tickDelta, int light, int overlay) {
		final BlockPos blockPos = blockEntity.getBlockPos();
		final BlockState blockState = world.getBlockState(blockPos);
		final Direction facing = IBlock.getStatePropertySafe(blockState, BlockStateProperties.HORIZONTAL_FACING);
		final boolean holdingLinker = player.isHolding(itemStack -> itemStack.getItem() instanceof ItemLiftButtonsLinkModifier || Block.byItem(itemStack.getItem()) instanceof BlockLiftButtons);

		final StoredMatrixTransformations storedMatrixTransformations1 = new StoredMatrixTransformations(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5);

		// Array order: has down button, has up button, pressed down button, pressed up button
		final boolean[] buttonStates = {false, false, false, false};
		final ObjectArrayList<ObjectObjectImmutablePair<BlockPos, Lift>> sortedPositionsAndLifts = new ObjectArrayList<>();

		blockEntity.forEachTrackPosition(trackPosition -> {
			// Render track link if holding linker item
			if (world.getBlockState(trackPosition).getBlock() instanceof BlockLiftTrackFloor) {
				final Direction trackFacing = IBlock.getStatePropertySafe(world, trackPosition, BlockStateProperties.HORIZONTAL_FACING);
				renderLiftObjectLink(
					storedMatrixTransformations1,
					new Vec3(facing.getStepX() / 2F, 0.5, facing.getStepZ() / 2F),
					new Vec3(trackPosition.getX() - blockPos.getX() + trackFacing.getStepX() / 2F, trackPosition.getY() - blockPos.getY() + 0.5, trackPosition.getZ() - blockPos.getZ() + trackFacing.getStepZ() / 2F),
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
		sortedPositionsAndLifts.sort(Comparator.comparingInt(sortedPositionAndLift -> blockPos.distManhattan(sortedPositionAndLift.left())));

		// Check whether the player is looking at the top or bottom button
		final HitResult hitResult = Minecraft.getInstance().hitResult;
		final boolean lookingAtTopHalf;
		final boolean lookingAtBottomHalf;
		if (player.isSpectator() || hitResult == null || !IBlock.getStatePropertySafe(blockState, BlockLiftButtons.UNLOCKED)) {
			lookingAtTopHalf = false;
			lookingAtBottomHalf = false;
		} else {
			final Vec3 hitLocation = hitResult.getLocation();
			final double hitY = Mth.frac(hitLocation.y);
			final boolean inBlock = hitY < 0.5 && BlockPos.containing(hitLocation.x, hitLocation.y, hitLocation.z).equals(blockPos);
			lookingAtTopHalf = inBlock && (!buttonStates[0] || hitY > 0.25);
			lookingAtBottomHalf = inBlock && (!buttonStates[1] || hitY < 0.25);
		}

		final StoredMatrixTransformations storedMatrixTransformations2 = storedMatrixTransformations1.copy();
		storedMatrixTransformations2.add(matrixStack -> {
			Drawing.rotateYDegrees(matrixStack, -facing.toYRot());
			matrixStack.translate(0, 0, 0.4375 - SMALL_OFFSET);
		});

		// Render buttons
		if (buttonStates[0]) {
			MainRenderer.scheduleRender(BUTTON_TEXTURE, false, buttonStates[2] || lookingAtBottomHalf ? QueuedRenderLayer.LIGHT_TRANSLUCENT : QueuedRenderLayer.EXTERIOR, (matrixStack, vertexConsumer, offset) -> {
				storedMatrixTransformations2.transform(matrixStack, offset);
				IDrawing.drawTexture(matrixStack, vertexConsumer, -1.5F / 16, (buttonStates[1] ? 0.5F : 2.5F) / 16, 3F / 16, 3F / 16, 0, 0, 1, 1, facing, buttonStates[2] ? PRESSED_COLOR : (lookingAtBottomHalf ? HOVER_COLOR : ARGB_GRAY), light);
				matrixStack.popPose();
			});
		}
		if (buttonStates[1]) {
			MainRenderer.scheduleRender(BUTTON_TEXTURE, false, buttonStates[3] || lookingAtTopHalf ? QueuedRenderLayer.LIGHT_TRANSLUCENT : QueuedRenderLayer.EXTERIOR, (matrixStack, vertexConsumer, offset) -> {
				storedMatrixTransformations2.transform(matrixStack, offset);
				IDrawing.drawTexture(matrixStack, vertexConsumer, -1.5F / 16, (buttonStates[0] ? 4.5F : 2.5F) / 16, 3F / 16, 3F / 16, 0, 1, 1, 0, facing, buttonStates[3] ? PRESSED_COLOR : (lookingAtTopHalf ? HOVER_COLOR : ARGB_GRAY), light);
				matrixStack.popPose();
			});
		}

		// Render the floor display
		if (!sortedPositionsAndLifts.isEmpty()) {
			final int count = Math.min(2, sortedPositionsAndLifts.size());
			final float width = count == 1 ? 0.25F : 0.375F;

			final StoredMatrixTransformations storedMatrixTransformations3 = storedMatrixTransformations2.copy();
			storedMatrixTransformations3.add(matrixStack -> {
				Drawing.rotateZDegrees(matrixStack, 180);
				matrixStack.translate(-width / 2, 0, 0);
			});

			// Render the black background
			MainRenderer.scheduleRender(ResourceLocation.fromNamespaceAndPath(MTR.MOD_ID, "textures/block/black.png"), false, QueuedRenderLayer.EXTERIOR, (matrixStack, vertexConsumer, offset) -> {
				storedMatrixTransformations3.transform(matrixStack, offset);
				IDrawing.drawTexture(matrixStack, vertexConsumer, 0, -0.9375F, width, 0.40625F, Direction.UP, light);
				matrixStack.popPose();
			});

			// Check if the two closest lifts are visually in order, based on the direction the buttons are facing
			final boolean reverseRendering = count > 1 && reverseRendering(facing.getCounterClockWise(), sortedPositionsAndLifts.get(0).left(), sortedPositionsAndLifts.get(1).left());
			for (int i = 0; i < count; i++) {
				final double x = ((reverseRendering ? count - i - 1 : i) + 0.5) * width / count;
				final StoredMatrixTransformations storedMatrixTransformations4 = storedMatrixTransformations3.copy();
				storedMatrixTransformations4.add(matrixStack -> matrixStack.translate(x, -0.875, -SMALL_OFFSET));
				RenderLifts.renderLiftDisplay(storedMatrixTransformations4, world, sortedPositionsAndLifts.get(i).right(), width / count, 0.3125F);
			}
		}
	}

	public static void renderLiftObjectLink(StoredMatrixTransformations storedMatrixTransformations, Vec3 position1, Vec3 position2, boolean holdingLinker) {
		if (holdingLinker) {
			MainRenderer.scheduleRender(QueuedRenderLayer.LINES, (matrixStack, vertexConsumer, offset) -> {
				storedMatrixTransformations.transform(matrixStack, offset);
				IDrawing.drawLineInWorld(
					matrixStack,
					vertexConsumer,
					(float) position1.x,
					(float) position1.y,
					(float) position1.z,
					(float) position2.x,
					(float) position2.y,
					(float) position2.z,
					ARGB_WHITE
				);
				matrixStack.popPose();
			});
		}
	}

	private static boolean reverseRendering(Direction direction, BlockPos blockPos1, BlockPos blockPos2) {
		if (direction.getStepX() != 0) {
			return Math.signum(blockPos2.getX() - blockPos1.getX()) == direction.getStepX();
		} else if (direction.getStepZ() != 0) {
			return Math.signum(blockPos2.getZ() - blockPos1.getZ()) == direction.getStepZ();
		} else {
			return false;
		}
	}
}
