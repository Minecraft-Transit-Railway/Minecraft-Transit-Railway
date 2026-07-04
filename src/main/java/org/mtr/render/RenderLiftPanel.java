package org.mtr.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import org.mtr.MTR;
import org.mtr.MTRClient;
import org.mtr.block.BlockLiftButtons;
import org.mtr.block.BlockLiftPanelBase;
import org.mtr.block.BlockLiftTrackFloor;
import org.mtr.block.IBlock;
import org.mtr.client.DynamicTextureCache;
import org.mtr.client.IDrawing;
import org.mtr.client.MinecraftClientData;
import org.mtr.core.data.Lift;
import org.mtr.core.data.LiftDirection;
import org.mtr.data.IGui;
import org.mtr.font.FontRenderHelper;
import org.mtr.font.FontRenderOptions;
import org.mtr.item.ItemLiftButtonsLinkModifier;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import org.mtr.tool.Drawing;

import java.awt.*;

public class RenderLiftPanel<T extends BlockLiftPanelBase.BlockEntityBase> extends BlockEntityRendererExtension<T> implements IGui, IBlock {

	private final boolean isOdd;
	private final boolean isFlat;

	private static final ResourceLocation ARROW_TEXTURE = ResourceLocation.fromNamespaceAndPath(MTR.MOD_ID, "textures/block/lift_arrow.png");
	private static final float ARROW_SPEED = 0.04F;
	private static final int SLIDE_TIME = 5;
	private static final int SLIDE_INTERVAL = 50;
	private static final float PANEL_WIDTH = 1.125F;

	public RenderLiftPanel(boolean isOdd, boolean isFlat) {
		this.isOdd = isOdd;
		this.isFlat = isFlat;
	}

	@Override
	public void render(T blockEntity, PoseStack matrixStack2, MultiBufferSource vertexConsumerProvider, ClientLevel world, LocalPlayer player, float tickDelta, int light, int overlay) {
		final BlockPos trackPosition = blockEntity.getTrackPosition();
		if (trackPosition == null || !(world.getBlockState(trackPosition).getBlock() instanceof BlockLiftTrackFloor)) {
			return;
		}

		final BlockPos blockPos = blockEntity.getBlockPos();
		final BlockState blockState = world.getBlockState(blockPos);
		final Direction facing = IBlock.getStatePropertySafe(blockState, BlockStateProperties.HORIZONTAL_FACING);
		final boolean holdingLinker = player.isHolding(itemStack -> {
			final Item item = itemStack.getItem();
			return item instanceof ItemLiftButtonsLinkModifier || Block.byItem(item) instanceof BlockLiftButtons;
		});

		final StoredMatrixTransformations storedMatrixTransformations1 = new StoredMatrixTransformations(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5);

		// Render track link if holding linker item
		final Direction trackFacing = IBlock.getStatePropertySafe(world, trackPosition, BlockStateProperties.HORIZONTAL_FACING);
		RenderLiftButtons.renderLiftObjectLink(
			storedMatrixTransformations1,
			new Vec3(facing.getStepX() / 2F + facing.getClockWise().getStepX() * (isOdd ? 1 : 0.5), 0.5, facing.getStepZ() / 2F + facing.getClockWise().getStepZ() * (isOdd ? 1 : 0.5)),
			new Vec3(trackPosition.getX() - blockPos.getX() + trackFacing.getStepX() / 2F, trackPosition.getY() - blockPos.getY() + 0.5, trackPosition.getZ() - blockPos.getZ() + trackFacing.getStepZ() / 2F),
			holdingLinker
		);

		Lift lift = null;
		for (final Lift checkLift : MinecraftClientData.getInstance().lifts) {
			if (checkLift.getFloorIndex(MTR.blockPosToPosition(trackPosition)) >= 0) {
				lift = checkLift;
				break;
			}
		}

		if (lift != null) {
			final String currentFloorNumber = RenderLifts.getLiftDetails(world, lift, trackPosition).right().left();
			final ObjectObjectImmutablePair<LiftDirection, ObjectObjectImmutablePair<String, String>> liftDetails = RenderLifts.getLiftDetails(world, lift, MTR.positionToBlockPos(lift.getCurrentFloor().getPosition()));

			final StoredMatrixTransformations storedMatrixTransformations2 = storedMatrixTransformations1.copy();
			storedMatrixTransformations2.add(matrixStack -> {
				Drawing.rotateYDegrees(matrixStack, -facing.toYRot());
				matrixStack.translate(isOdd ? -1 : -0.5, 0, 0);
				Drawing.rotateZDegrees(matrixStack, 180);
				matrixStack.translate(0, 0, (isFlat ? 0.4375F : 0.25F) - SMALL_OFFSET * 2);
			});

			// Floor Number
			MainRenderer.scheduleTextRender((matrixStack, offset) -> {
				storedMatrixTransformations2.transform(matrixStack, offset);
				FontRenderHelper.render(matrixStack,
					currentFloorNumber,
					FontRenderOptions.builder()
						.horizontalTextAlignment(FontRenderOptions.Alignment.CENTER)
						.verticalTextAlignment(FontRenderOptions.Alignment.CENTER)
						.horizontalPositioning(FontRenderOptions.Alignment.CENTER)
						.verticalPositioning(FontRenderOptions.Alignment.CENTER)
						.offsetY(-0.47F)
						.horizontalSpace(0.1875F)
						.verticalSpace(0.1875F)
						.color(Color.BLACK)
						.textOverflow(FontRenderOptions.TextOverflow.COMPRESS)
						.build()
				);
				matrixStack.popPose();
			});

			renderLiftDisplay(storedMatrixTransformations2, liftDetails);
		}
	}

	private void renderLiftDisplay(StoredMatrixTransformations storedMatrixTransformations, ObjectObjectImmutablePair<LiftDirection, ObjectObjectImmutablePair<String, String>> liftDetails) {
		final LiftDirection liftDirection = liftDetails.left();
		final String floorNumber = liftDetails.right().left();
		final String floorDescription = liftDetails.right().right();
		final boolean noFloorNumber = floorNumber.isEmpty();
		final boolean noFloorDisplay = floorDescription.isEmpty();
		final int lineCount = (noFloorNumber ? 0 : floorNumber.split("\\|").length) + (noFloorDisplay ? 0 : floorDescription.split("\\|").length);
		final float lineHeight = 1F / lineCount;
		final float gameTick = MTRClient.getGameTick();
		final boolean goingUp = liftDirection == LiftDirection.UP;
		final float arrowSize = PANEL_WIDTH / 6;
		final float y = -arrowSize - 0.125F;

		// Arrow
		if (liftDirection != LiftDirection.NONE) {
			final float uv = (gameTick * ARROW_SPEED) % 1;
			final int color = goingUp ? 0xFF00FF00 : 0xFFFF0000;
			MainRenderer.scheduleRender(ARROW_TEXTURE, false, QueuedRenderLayer.LIGHT_TRANSLUCENT, (matrixStack, vertexConsumer, offset) -> {
				storedMatrixTransformations.transform(matrixStack, offset);
				IDrawing.drawTexture(matrixStack, vertexConsumer, -PANEL_WIDTH / 2 - arrowSize, y, arrowSize, arrowSize, 0, (goingUp ? 0 : 1) + uv, 1, (goingUp ? 1 : 0) + uv, Direction.UP, color, DEFAULT_LIGHT);
				IDrawing.drawTexture(matrixStack, vertexConsumer, PANEL_WIDTH / 2, y, arrowSize, arrowSize, 0, (goingUp ? 0 : 1) + uv, 1, (goingUp ? 1 : 0) + uv, Direction.UP, color, DEFAULT_LIGHT);
				matrixStack.popPose();
			});
		}

		// Floor Display
		if (!noFloorNumber || !noFloorDisplay) {
			float uvOffset = 0;
			if (lineCount > 1) {
				uvOffset = (float) Math.floor((gameTick % (SLIDE_INTERVAL * lineCount)) / SLIDE_INTERVAL) * lineHeight;
				if ((gameTick % SLIDE_INTERVAL) > SLIDE_INTERVAL - SLIDE_TIME) {
					uvOffset += lineHeight * ((gameTick % SLIDE_INTERVAL) - SLIDE_INTERVAL + SLIDE_TIME) / SLIDE_TIME;
				}
			}
			final float uv = (goingUp ? -1 : 1) * uvOffset;
			final String text = String.format("%s%s%s", floorNumber, noFloorNumber || noFloorDisplay ? "" : "|", floorDescription);
			MainRenderer.scheduleRender(DynamicTextureCache.instance.getLiftPanelDisplay(text, 0xFFAA00).identifier, false, QueuedRenderLayer.LIGHT_TRANSLUCENT, (matrixStack, vertexConsumer, offset) -> {
				storedMatrixTransformations.transform(matrixStack, offset);
				IDrawing.drawTexture(matrixStack, vertexConsumer, -PANEL_WIDTH / 2, y, PANEL_WIDTH, arrowSize, 0, uv, 1, lineHeight + uv, Direction.UP, ARGB_WHITE, DEFAULT_LIGHT);
				matrixStack.popPose();
			});
		}
	}
}
