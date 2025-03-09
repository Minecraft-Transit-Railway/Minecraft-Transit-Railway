package org.mtr.render;

import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.Item;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
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
import org.mtr.item.ItemLiftButtonsLinkModifier;

public class RenderLiftPanel<T extends BlockLiftPanelBase.BlockEntityBase> extends BlockEntityRendererExtension<T> implements IGui, IBlock {

	private final boolean isOdd;
	private final boolean isFlat;

	private static final Identifier ARROW_TEXTURE = Identifier.of(MTR.MOD_ID, "textures/block/lift_arrow.png");
	private static final float ARROW_SPEED = 0.04F;
	private static final int SLIDE_TIME = 5;
	private static final int SLIDE_INTERVAL = 50;
	private static final float PANEL_WIDTH = 1.125F;

	public RenderLiftPanel(boolean isOdd, boolean isFlat) {
		this.isOdd = isOdd;
		this.isFlat = isFlat;
	}

	@Override
	public void render(T entity, ClientWorld world, ClientPlayerEntity player, float tickDelta, int light, int overlay) {
		final BlockPos trackPosition = entity.getTrackPosition();
		if (trackPosition == null || !(world.getBlockState(trackPosition).getBlock() instanceof BlockLiftTrackFloor)) {
			return;
		}

		final BlockPos blockPos = entity.getPos();
		final BlockState blockState = world.getBlockState(blockPos);
		final Direction facing = IBlock.getStatePropertySafe(blockState, Properties.FACING);
		final boolean holdingLinker = player.isHolding(itemStack -> {
			final Item item = itemStack.getItem();
			return item instanceof ItemLiftButtonsLinkModifier || Block.getBlockFromItem(item) instanceof BlockLiftButtons;
		});

		final StoredMatrixTransformations storedMatrixTransformations1 = new StoredMatrixTransformations(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5);

		// Render track link if holding linker item
		final Direction trackFacing = IBlock.getStatePropertySafe(world, trackPosition, Properties.FACING);
		RenderLiftButtons.renderLiftObjectLink(
				storedMatrixTransformations1,
				new Vec3d(facing.getOffsetX() / 2F + facing.rotateYClockwise().getOffsetX() * (isOdd ? 1 : 0.5), 0.5, facing.getOffsetZ() / 2F + facing.rotateYClockwise().getOffsetZ() * (isOdd ? 1 : 0.5)),
				new Vec3d(trackPosition.getX() - blockPos.getX() + trackFacing.getOffsetX() / 2F, trackPosition.getY() - blockPos.getY() + 0.5, trackPosition.getZ() - blockPos.getZ() + trackFacing.getOffsetZ() / 2F),
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
				IDrawing.rotateYDegrees(matrixStack, -facing.getPositiveHorizontalDegrees());
				matrixStack.translate(isOdd ? -1 : -0.5, 0, 0);
				IDrawing.rotateZDegrees(matrixStack, 180);
				matrixStack.translate(0, 0, (isFlat ? 0.4375F : 0.25F) - SMALL_OFFSET * 2);
			});

			// Floor Number
			MainRenderer.scheduleRender(QueuedRenderLayer.TEXT, (matrixStack, vertexConsumer, offset) -> {
				storedMatrixTransformations2.transform(matrixStack, offset);
//				IDrawing.drawStringWithFont(matrixStack, vertexConsumer, currentFloorNumber, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, 0, -0.47F, 0.1875F, 0.1875F, 1, ARGB_BLACK, false, DEFAULT_LIGHT, null);
				matrixStack.pop();
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
				matrixStack.pop();
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
				matrixStack.pop();
			});
		}
	}
}
