package org.mtr.mod.render;

import org.mtr.core.data.LiftDirection;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArraySet;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityRenderer;
import org.mtr.mapping.mapper.DirectionHelper;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.PlayerHelper;
import org.mtr.mod.Init;
import org.mtr.mod.block.BlockLiftButtons;
import org.mtr.mod.block.IBlock;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.data.IGui;
import org.mtr.mod.item.ItemLiftButtonsLinkModifier;

public class RenderLiftButtons extends BlockEntityRenderer<BlockLiftButtons.BlockEntity> implements DirectionHelper, IGui, IBlock {

	private static final int HOVER_COLOR = 0xFFFFAAAA;
	private static final int PRESSED_COLOR = 0xFFFF0000;
	private static final Identifier BUTTON_TEXTURE = new Identifier(Init.MOD_ID, "textures/block/lift_button.png");

	public RenderLiftButtons(Argument dispatcher) {
		super(dispatcher);
	}

	@Override
	public void render(BlockLiftButtons.BlockEntity blockEntity, float tickDelta, GraphicsHolder graphicsHolder, int light, int overlay) {
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

		graphicsHolder.push();
		graphicsHolder.translate(0.5, 0, 0.5);

		// has down button, has up button, pressed down button, pressed up button
		final boolean[] buttonStates = {false, false, false, false};

		blockEntity.forEachTrackPosition(trackPosition -> {
			renderLiftObjectLink(graphicsHolder, world, blockPos, trackPosition, facing, holdingLinker);
			BlockLiftButtons.hasButtonsClient(trackPosition, buttonStates, (floorIndex, lift) -> {
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

		final HitResult hitResult = MinecraftClient.getInstance().getCrosshairTargetMapped();
		final boolean lookingAtTopHalf;
		final boolean lookingAtBottomHalf;
		if (hitResult == null || !IBlock.getStatePropertySafe(blockState, BlockLiftButtons.UNLOCKED)) {
			lookingAtTopHalf = false;
			lookingAtBottomHalf = false;
		} else {
			final Vector3d hitLocation = hitResult.getPos();
			final double hitY = MathHelper.fractionalPart(hitLocation.getYMapped());
			final boolean inBlock = hitY < 0.5 && Init.newBlockPos(hitLocation.getXMapped(), hitLocation.getYMapped(), hitLocation.getZMapped()).equals(blockPos);
			lookingAtTopHalf = inBlock && (!buttonStates[0] || hitY > 0.25);
			lookingAtBottomHalf = inBlock && (!buttonStates[1] || hitY < 0.25);
		}

		graphicsHolder.rotateYDegrees(-facing.asRotation());
		graphicsHolder.translate(0, 0, 0.4375 - SMALL_OFFSET);

		if (buttonStates[0]) {
			graphicsHolder.createVertexConsumer(buttonStates[2] || lookingAtBottomHalf ? MoreRenderLayers.getLight(BUTTON_TEXTURE, true) : MoreRenderLayers.getExterior(BUTTON_TEXTURE));
			IDrawing.drawTexture(graphicsHolder, -1.5F / 16, (buttonStates[1] ? 0.5F : 2.5F) / 16, 3F / 16, 3F / 16, 0, 0, 1, 1, facing, buttonStates[2] ? PRESSED_COLOR : lookingAtBottomHalf ? HOVER_COLOR : ARGB_GRAY, light);
		}
		if (buttonStates[1]) {
			graphicsHolder.createVertexConsumer(buttonStates[3] || lookingAtTopHalf ? MoreRenderLayers.getLight(BUTTON_TEXTURE, true) : MoreRenderLayers.getExterior(BUTTON_TEXTURE));
			IDrawing.drawTexture(graphicsHolder, -1.5F / 16, (buttonStates[0] ? 4.5F : 2.5F) / 16, 3F / 16, 3F / 16, 0, 1, 1, 0, facing, buttonStates[3] ? PRESSED_COLOR : lookingAtTopHalf ? HOVER_COLOR : ARGB_GRAY, light);
		}

		graphicsHolder.pop();
	}

	public static void renderLiftObjectLink(GraphicsHolder graphicsHolder, World world, BlockPos pos, BlockPos trackPosition, Direction facing, boolean holdingLinker) {
		if (holdingLinker) {
			final Direction trackFacing = IBlock.getStatePropertySafe(world, trackPosition, FACING);
			graphicsHolder.createVertexConsumer(RenderLayer.getLines());
			graphicsHolder.drawLineInWorld(
					trackPosition.getX() - pos.getX() + trackFacing.getOffsetX() / 2F,
					trackPosition.getY() - pos.getY() + 0.5F,
					trackPosition.getZ() - pos.getZ() + trackFacing.getOffsetZ() / 2F,
					facing.getOffsetX() / 2F,
					0.25F,
					facing.getOffsetZ() / 2F,
					ARGB_WHITE
			);
		}
	}
}
