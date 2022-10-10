package mtr.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.math.Vector3f;
import mtr.MTRClient;
import mtr.block.BlockLiftPanel1;
import mtr.block.BlockLiftTrackFloor;
import mtr.block.IBlock;
import mtr.client.ClientData;
import mtr.client.IDrawing;
import mtr.data.Lift;
import mtr.item.ItemLiftButtonsLinkModifier;
import mtr.mappings.BlockEntityRendererMapper;
import mtr.mappings.Utilities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;

import static mtr.data.IGui.*;

public class RenderLiftPanel extends BlockEntityRendererMapper<BlockLiftPanel1.TileEntityLiftPanel> {

	private float uvShiftArrow = 0;
	private float nextFloorUV = 0;
	private float currentFloorUV = 0;
	private float tickElapsed = 0;

	private static final ResourceLocation ARROW_TEXTURE = new ResourceLocation("mtr:textures/block/lift_arrow.png");
	private static final float ARROW_SPEED = 0.04F;
	private static final float FLOOR_SLIDE_SPEED = 0.1F;
	private static final int SLIDE_INTERVAL = 50;
	private static final float MAX_WIDTH = 1.2F;

	public RenderLiftPanel(BlockEntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public void render(BlockLiftPanel1.TileEntityLiftPanel entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int combinedOverlay) {
		final Level world = entity.getLevel();
		if (world == null) {
			return;
		}

		final Player player = Minecraft.getInstance().player;
		if (player == null) {
			return;
		}

		final BlockPos pos = entity.getBlockPos();
		if (RenderTrains.shouldNotRender(pos, RenderTrains.maxTrainRenderDistance, null)) {
			return;
		}

		final BlockState state = world.getBlockState(pos);
		if (!IBlock.getStatePropertySafe(state, BlockLiftPanel1.LEFT)) {
			return;
		}

		final Font textRenderer = Minecraft.getInstance().font;
		if (textRenderer == null) {
			return;
		}

		final BlockPos trackPosition = entity.getTrackPosition(world);
		if (trackPosition == null) {
			return;
		}

		final BlockLiftTrackFloor.TileEntityLiftTrackFloor trackFloorTileEntity = (BlockLiftTrackFloor.TileEntityLiftTrackFloor) world.getBlockEntity(trackPosition);
		if (trackFloorTileEntity == null) {
			return;
		}

		Lift lift = null;
		for (final Lift checkLift : ClientData.LIFTS) {
			if (checkLift.hasFloor(trackPosition)) {
				lift = checkLift;
				break;
			}
		}
		if (lift == null) {
			return;
		}

		final Direction facing = IBlock.getStatePropertySafe(state, HorizontalDirectionalBlock.FACING).getOpposite();
		final boolean holdingLinker = Utilities.isHolding(player, item -> item instanceof ItemLiftButtonsLinkModifier || Block.byItem(item) instanceof BlockLiftPanel1);
		final String[] text = ClientData.DATA_CACHE.requestLiftFloorText(lift.getCurrentFloorBlockPos());

		matrices.pushPose();
		matrices.translate(0.5, 0, 0.5);
		RenderLiftButtons.renderLiftObjectLink(matrices, vertexConsumers, world, pos, trackPosition, facing, holdingLinker);
		matrices.mulPose(Vector3f.YN.rotationDegrees(facing.toYRot()));
		matrices.translate(0, 0, 0.4375 - SMALL_OFFSET);

		matrices.mulPose(Vector3f.ZP.rotationDegrees(180));
		matrices.translate(MAX_WIDTH * (0.5 - 1 / 2F), 0, 0);
		matrices.translate(0, -0.875, -SMALL_OFFSET);

		// Floor Number
		matrices.pushPose();
		matrices.translate(0.5F, 0.5F, 0);
		final MultiBufferSource.BufferSource immediate = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
		IDrawing.drawStringWithFont(matrices, textRenderer, immediate, ClientData.DATA_CACHE.requestLiftFloorText(trackPosition)[0], HorizontalAlignment.CENTER, VerticalAlignment.CENTER, -SMALL_OFFSET, 0, 0.16F, 0.16F, 1F, ARGB_BLACK, false, MAX_LIGHT_GLOWING, null);
		immediate.endBatch();
		matrices.popPose();

		renderLiftDisplay(matrices, vertexConsumers, facing, pos, text[0], text[1], lift.getLiftDirection());

		matrices.translate(MAX_WIDTH, 0, 0);
		matrices.popPose();
	}

	private void renderLiftDisplay(PoseStack matrices, MultiBufferSource vertexConsumers, Direction facing, BlockPos pos, String floorNumber, String floorDisplay, Lift.LiftDirection liftDirection) {
		final boolean noFloorNumber = floorNumber.isEmpty();
		final boolean noFloorDisplay = floorDisplay.isEmpty();
		final float lineHeight = 1F / ((noFloorNumber ? 0 : floorNumber.split("\\|").length) + (noFloorDisplay ? 0 : floorDisplay.split("\\|").length));
		final float delta = MTRClient.getLastFrameDuration();
		final boolean goingUp = liftDirection == Lift.LiftDirection.UP;

		tickElapsed += delta;

		if (tickElapsed >= SLIDE_INTERVAL) {
			nextFloorUV += lineHeight;
			tickElapsed = 0;
		}

		if (nextFloorUV > currentFloorUV) {
			currentFloorUV += Math.min(FLOOR_SLIDE_SPEED * lineHeight * delta, nextFloorUV - currentFloorUV);
		} else if (nextFloorUV % lineHeight != 0) {
			nextFloorUV = 0;
			currentFloorUV = 0;
		}

		// Arrow
		if (liftDirection != Lift.LiftDirection.NONE) {
			uvShiftArrow += (ARROW_SPEED * delta) % 2;
			IDrawing.drawTexture(matrices, vertexConsumers.getBuffer(MoreRenderLayers.getLight(ARROW_TEXTURE, true)), -0.06F, 0.65F, MAX_WIDTH / 8F, MAX_WIDTH / 8F, 0, (goingUp ? 0 : 1) + uvShiftArrow, 1, (goingUp ? 1 : 0) + uvShiftArrow, Direction.UP, ARGB_WHITE, MAX_LIGHT_GLOWING);
			IDrawing.drawTexture(matrices, vertexConsumers.getBuffer(MoreRenderLayers.getLight(ARROW_TEXTURE, true)), MAX_WIDTH / 1.3F, 0.65F, MAX_WIDTH / 8F, MAX_WIDTH / 8F, 0, (goingUp ? 0 : 1) + uvShiftArrow, 1, (goingUp ? 1 : 0) + uvShiftArrow, Direction.UP, ARGB_WHITE, MAX_LIGHT_GLOWING);
		}

		if (!noFloorNumber || !noFloorDisplay) {
			// Floor Display
			matrices.pushPose();
			matrices.translate(0.07F, 0.63F, 0);
			matrices.scale(0.017F, 0.017F, 0.017F);
			final String text = String.format("%s%s%s", floorNumber, noFloorNumber || noFloorDisplay ? "" : "|", floorDisplay);
			IDrawing.drawTexture(matrices, vertexConsumers.getBuffer(MoreRenderLayers.getLight(ClientData.DATA_CACHE.getLiftPanelDisplay(text, 16755200).resourceLocation, true)), 0, 0.5F, 50, 10, 0, goingUp ? (0 - currentFloorUV) : (0 + currentFloorUV), 1, goingUp ? (lineHeight - currentFloorUV) : (lineHeight + currentFloorUV), facing, ARGB_WHITE, MAX_LIGHT_GLOWING);
			matrices.popPose();
		}
	}
}
