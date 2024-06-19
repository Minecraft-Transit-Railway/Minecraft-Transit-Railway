package org.mtr.mod.render;

import org.mtr.core.data.Lift;
import org.mtr.core.data.LiftDirection;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityRenderer;
import org.mtr.mapping.mapper.DirectionHelper;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.PlayerHelper;
import org.mtr.mod.Init;
import org.mtr.mod.InitClient;
import org.mtr.mod.block.BlockLiftButtons;
import org.mtr.mod.block.BlockLiftPanelBase;
import org.mtr.mod.block.BlockLiftTrackFloor;
import org.mtr.mod.block.IBlock;
import org.mtr.mod.client.DynamicTextureCache;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.client.MinecraftClientData;
import org.mtr.mod.data.IGui;
import org.mtr.mod.item.ItemLiftButtonsLinkModifier;

public class RenderLiftPanel<T extends BlockLiftPanelBase.BlockEntityBase> extends BlockEntityRenderer<T> implements DirectionHelper, IGui, IBlock {

	private final boolean isOdd;
	private final boolean isFlat;

	private static final Identifier ARROW_TEXTURE = new Identifier(Init.MOD_ID, "textures/block/lift_arrow.png");
	private static final float ARROW_SPEED = 0.04F;
	private static final int SLIDE_TIME = 5;
	private static final int SLIDE_INTERVAL = 50;
	private static final float PANEL_WIDTH = 1.125F;

	public RenderLiftPanel(Argument dispatcher, boolean isOdd, boolean isFlat) {
		super(dispatcher);
		this.isOdd = isOdd;
		this.isFlat = isFlat;
	}

	@Override
	public void render(T blockEntity, float tickDelta, GraphicsHolder graphicsHolder1, int light, int overlay) {
		final World world = blockEntity.getWorld2();
		if (world == null) {
			return;
		}

		final ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().getPlayerMapped();
		if (clientPlayerEntity == null) {
			return;
		}

		final BlockPos trackPosition = blockEntity.getTrackPosition();
		if (trackPosition == null || !(world.getBlockState(trackPosition).getBlock().data instanceof BlockLiftTrackFloor)) {
			return;
		}

		final BlockPos blockPos = blockEntity.getPos2();
		final BlockState blockState = world.getBlockState(blockPos);
		final Direction facing = IBlock.getStatePropertySafe(blockState, FACING);
		final boolean holdingLinker = PlayerHelper.isHolding(PlayerEntity.cast(clientPlayerEntity), item -> item.data instanceof ItemLiftButtonsLinkModifier || Block.getBlockFromItem(item).data instanceof BlockLiftButtons);

		final StoredMatrixTransformations storedMatrixTransformations1 = new StoredMatrixTransformations(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5);

		// Render track link if holding linker item
		final Direction trackFacing = IBlock.getStatePropertySafe(world, trackPosition, FACING);
		RenderLiftButtons.renderLiftObjectLink(
				storedMatrixTransformations1, world,
				new Vector3d(facing.getOffsetX() / 2F + facing.rotateYClockwise().getOffsetX() * (isOdd ? 1 : 0.5), 0.5, facing.getOffsetZ() / 2F + facing.rotateYClockwise().getOffsetZ() * (isOdd ? 1 : 0.5)),
				new Vector3d(trackPosition.getX() - blockPos.getX() + trackFacing.getOffsetX() / 2F, trackPosition.getY() - blockPos.getY() + 0.5, trackPosition.getZ() - blockPos.getZ() + trackFacing.getOffsetZ() / 2F),
				holdingLinker
		);

		Lift lift = null;
		for (final Lift checkLift : MinecraftClientData.getInstance().lifts) {
			if (checkLift.getFloorIndex(Init.blockPosToPosition(trackPosition)) >= 0) {
				lift = checkLift;
				break;
			}
		}

		if (lift != null) {
			final String currentFloorNumber = RenderLifts.getLiftDetails(world, lift, trackPosition).right().left();
			final ObjectObjectImmutablePair<LiftDirection, ObjectObjectImmutablePair<String, String>> liftDetails = RenderLifts.getLiftDetails(world, lift, Init.positionToBlockPos(lift.getCurrentFloor().getPosition()));

			final StoredMatrixTransformations storedMatrixTransformations2 = storedMatrixTransformations1.copy();
			storedMatrixTransformations2.add(graphicsHolder -> {
				graphicsHolder.rotateYDegrees(-facing.asRotation());
				graphicsHolder.translate(isOdd ? -1 : -0.5, 0, 0);
				graphicsHolder.rotateZDegrees(180);
				graphicsHolder.translate(0, 0, (isFlat ? 0.4375F : 0.25F) - SMALL_OFFSET * 2);
			});

			// Floor Number
			MainRenderer.scheduleRender(QueuedRenderLayer.TEXT, (graphicsHolder, offset) -> {
				storedMatrixTransformations2.transform(graphicsHolder, offset);
				IDrawing.drawStringWithFont(graphicsHolder, currentFloorNumber, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, 0, -0.47F, 0.1875F, 0.1875F, 1, ARGB_BLACK, false, GraphicsHolder.getDefaultLight(), null);
				graphicsHolder.pop();
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
		final float gameTick = InitClient.getGameTick();
		final boolean goingUp = liftDirection == LiftDirection.UP;
		final float arrowSize = PANEL_WIDTH / 6;
		final float y = -arrowSize - 0.125F;

		// Arrow
		if (liftDirection != LiftDirection.NONE) {
			final float uv = (gameTick * ARROW_SPEED) % 1;
			final int color = goingUp ? 0xFF00FF00 : 0xFFFF0000;
			MainRenderer.scheduleRender(ARROW_TEXTURE, false, QueuedRenderLayer.LIGHT_TRANSLUCENT, (graphicsHolder, offset) -> {
				storedMatrixTransformations.transform(graphicsHolder, offset);
				IDrawing.drawTexture(graphicsHolder, -PANEL_WIDTH / 2 - arrowSize, y, arrowSize, arrowSize, 0, (goingUp ? 0 : 1) + uv, 1, (goingUp ? 1 : 0) + uv, Direction.UP, color, GraphicsHolder.getDefaultLight());
				IDrawing.drawTexture(graphicsHolder, PANEL_WIDTH / 2, y, arrowSize, arrowSize, 0, (goingUp ? 0 : 1) + uv, 1, (goingUp ? 1 : 0) + uv, Direction.UP, color, GraphicsHolder.getDefaultLight());
				graphicsHolder.pop();
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
			MainRenderer.scheduleRender(DynamicTextureCache.instance.getLiftPanelDisplay(text, 0xFFAA00).identifier, false, QueuedRenderLayer.LIGHT_TRANSLUCENT, (graphicsHolder, offset) -> {
				storedMatrixTransformations.transform(graphicsHolder, offset);
				IDrawing.drawTexture(graphicsHolder, -PANEL_WIDTH / 2, y, PANEL_WIDTH, arrowSize, 0, uv, 1, lineHeight + uv, Direction.UP, ARGB_WHITE, GraphicsHolder.getDefaultLight());
				graphicsHolder.pop();
			});
		}
	}
}
