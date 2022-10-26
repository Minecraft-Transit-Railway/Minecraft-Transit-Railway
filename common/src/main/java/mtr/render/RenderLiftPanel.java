package mtr.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.math.Vector3f;
import mtr.MTRClient;
import mtr.block.BlockLiftPanelBase;
import mtr.block.BlockLiftTrackFloor;
import mtr.block.IBlock;
import mtr.block.ITripleBlock;
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

public class RenderLiftPanel<T extends BlockLiftPanelBase.TileEntityLiftPanel1Base> extends BlockEntityRendererMapper<T> {

	private final boolean isOdd;
	private final boolean isFlat;

	private static final ResourceLocation ARROW_TEXTURE = new ResourceLocation("mtr:textures/block/lift_arrow.png");
	private static final float ARROW_SPEED = 0.04F;
	private static final int SLIDE_TIME = 5;
	private static final int SLIDE_INTERVAL = 50;
	private static final float PANEL_WIDTH = 1.125F;

	public RenderLiftPanel(BlockEntityRenderDispatcher dispatcher, boolean isOdd, boolean isFlat) {
		super(dispatcher);
		this.isOdd = isOdd;
		this.isFlat = isFlat;
	}

	@Override
	public void render(T entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int combinedOverlay) {
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
		if (!isOdd && IBlock.getStatePropertySafe(state, IBlock.SIDE) == IBlock.EnumSide.RIGHT || isOdd && !IBlock.getStatePropertySafe(state, ITripleBlock.ODD)) {
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

		final Direction facing = IBlock.getStatePropertySafe(state, HorizontalDirectionalBlock.FACING);
		final boolean holdingLinker = Utilities.isHolding(player, item -> item instanceof ItemLiftButtonsLinkModifier || Block.byItem(item) instanceof BlockLiftPanelBase);

		matrices.pushPose();
		matrices.translate(0.5, 0, 0.5);
		RenderLiftButtons.renderLiftObjectLink(matrices, vertexConsumers, world, pos, trackPosition, facing, holdingLinker);

		Lift lift = null;
		for (final Lift checkLift : ClientData.LIFTS) {
			if (checkLift.hasFloor(trackPosition)) {
				lift = checkLift;
				break;
			}
		}

		if (lift != null) {
			final String[] text = ClientData.DATA_CACHE.requestLiftFloorText(lift.getCurrentFloorBlockPos());
			matrices.mulPose(Vector3f.YN.rotationDegrees(facing.toYRot()));
			matrices.mulPose(Vector3f.ZP.rotationDegrees(180));
			matrices.translate(isOdd ? 0 : 0.5, 0, 0);

			// Floor Number
			matrices.pushPose();
			matrices.translate(0, 0, (isFlat ? 0.4375F : 0.25F) - SMALL_OFFSET * 2);
			final MultiBufferSource.BufferSource immediate = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
			IDrawing.drawStringWithFont(matrices, textRenderer, immediate, ClientData.DATA_CACHE.requestLiftFloorText(trackPosition)[0], HorizontalAlignment.CENTER, VerticalAlignment.CENTER, 0, -0.47F, 0.1875F, 0.1875F, 1, ARGB_BLACK, false, MAX_LIGHT_GLOWING, null);
			immediate.endBatch();
			matrices.popPose();

			renderLiftDisplay(matrices, vertexConsumers, isFlat ? 0.4375F : 0.25F, text[0], text[1], lift.getLiftDirection());
		}
		matrices.popPose();
	}

	private void renderLiftDisplay(PoseStack matrices, MultiBufferSource vertexConsumers, float zOffset, String floorNumber, String floorDisplay, Lift.LiftDirection liftDirection) {
		matrices.pushPose();
		matrices.translate(0, 0, zOffset - SMALL_OFFSET * 2);

		final boolean noFloorNumber = floorNumber.isEmpty();
		final boolean noFloorDisplay = floorDisplay.isEmpty();
		final int lineCount = (noFloorNumber ? 0 : floorNumber.split("\\|").length) + (noFloorDisplay ? 0 : floorDisplay.split("\\|").length);
		final float lineHeight = 1F / lineCount;
		final float gameTick = MTRClient.getGameTick();
		final boolean goingUp = liftDirection == Lift.LiftDirection.UP;
		final float arrowSize = PANEL_WIDTH / 6;
		final float y = -arrowSize - 0.125F;

		// Arrow
		if (liftDirection != Lift.LiftDirection.NONE) {
			final float uv = (gameTick * ARROW_SPEED) % 1;
			final int color = goingUp ? 0xFF00FF00 : 0xFFFF0000;
			IDrawing.drawTexture(matrices, vertexConsumers.getBuffer(MoreRenderLayers.getLight(ARROW_TEXTURE, false)), -PANEL_WIDTH / 2 - arrowSize, y, arrowSize, arrowSize, 0, (goingUp ? 0 : 1) + uv, 1, (goingUp ? 1 : 0) + uv, Direction.UP, color, MAX_LIGHT_GLOWING);
			IDrawing.drawTexture(matrices, vertexConsumers.getBuffer(MoreRenderLayers.getLight(ARROW_TEXTURE, false)), PANEL_WIDTH / 2, y, arrowSize, arrowSize, 0, (goingUp ? 0 : 1) + uv, 1, (goingUp ? 1 : 0) + uv, Direction.UP, color, MAX_LIGHT_GLOWING);
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
			final String text = String.format("%s%s%s", floorNumber, noFloorNumber || noFloorDisplay ? "" : "|", floorDisplay);
			IDrawing.drawTexture(matrices, vertexConsumers.getBuffer(MoreRenderLayers.getLight(ClientData.DATA_CACHE.getLiftPanelDisplay(text, 0xFFAA00).resourceLocation, false)), -PANEL_WIDTH / 2, y, PANEL_WIDTH, arrowSize, 0, uv, 1, lineHeight + uv, Direction.UP, ARGB_WHITE, MAX_LIGHT_GLOWING);
		}

		matrices.popPose();
	}
}
