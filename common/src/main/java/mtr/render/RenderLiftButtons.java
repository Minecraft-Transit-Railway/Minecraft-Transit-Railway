package mtr.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import mtr.block.BlockLiftButtons;
import mtr.block.IBlock;
import mtr.client.IDrawing;
import mtr.data.IGui;
import mtr.entity.EntityLift;
import mtr.item.ItemLiftButtonsLinkModifier;
import mtr.mappings.BlockEntityRendererMapper;
import mtr.mappings.Utilities;
import net.minecraft.client.Minecraft;
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
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class RenderLiftButtons extends BlockEntityRendererMapper<BlockLiftButtons.TileEntityLiftButtons> implements IGui, IBlock {

	private static final int PRESSED_COLOR = 0xFFFF0000;
	private static final int HOVER_COLOR = 0xFFFFAAAA;
	private static final ResourceLocation BUTTON_TEXTURE = new ResourceLocation("mtr:textures/block/lift_button.png");

	public RenderLiftButtons(BlockEntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public void render(BlockLiftButtons.TileEntityLiftButtons entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
		final Level world = entity.getLevel();
		if (world == null) {
			return;
		}

		final Player player = Minecraft.getInstance().player;
		if (player == null) {
			return;
		}

		final BlockPos pos = entity.getBlockPos();
		final BlockState state = world.getBlockState(pos);
		final Direction facing = IBlock.getStatePropertySafe(state, HorizontalDirectionalBlock.FACING);
		if (RenderTrains.shouldNotRender(pos, RenderTrains.maxTrainRenderDistance, null)) {
			return;
		}

		final boolean holdingLinker = Utilities.isHolding(player, item -> item instanceof ItemLiftButtonsLinkModifier || Block.byItem(item) instanceof BlockLiftButtons);

		matrices.pushPose();
		matrices.translate(0.5, 0, 0.5);

		final boolean[] buttonStates = {false, false, false, false};
		entity.forEachTrackPosition(world, (trackPosition, trackFloorTileEntity) -> {
			if (holdingLinker) {
				final Direction trackFacing = IBlock.getStatePropertySafe(world, trackPosition, HorizontalDirectionalBlock.FACING);
				IDrawing.drawLine(matrices, vertexConsumers, trackPosition.getX() - pos.getX() + trackFacing.getStepX() / 2F, trackPosition.getY() - pos.getY() + 0.5F, trackPosition.getZ() - pos.getZ() + trackFacing.getStepZ() / 2F, facing.getStepX() / 2F, 0.25F, facing.getStepZ() / 2F, 0xFF, 0xFF, 0xFF);
			}

			final EntityLift entityLift = trackFloorTileEntity.getEntityLift();
			if (entityLift != null) {
				entityLift.hasButton(trackPosition.getY(), buttonStates);
				if (entityLift.hasStoppingFloorsClient(trackPosition.getY(), true)) {
					buttonStates[2] = true;
				}
				if (entityLift.hasStoppingFloorsClient(trackPosition.getY(), false)) {
					buttonStates[3] = true;
				}
			}
		});

		final HitResult hitResult = Minecraft.getInstance().hitResult;
		final boolean lookingAtTopHalf;
		final boolean lookingAtBottomHalf;
		if (hitResult == null) {
			lookingAtTopHalf = false;
			lookingAtBottomHalf = false;
		} else {
			final Vec3 hitLocation = hitResult.getLocation();
			final double hitX = hitLocation.x - Math.floor(hitLocation.x);
			final double hitY = hitLocation.y - Math.floor(hitLocation.y);
			final double hitZ = hitLocation.z - Math.floor(hitLocation.z);
			final boolean inBlock = hitX > 0 && hitY > 0 && hitZ > 0 && new BlockPos(hitLocation).equals(pos);
			lookingAtTopHalf = inBlock && (!buttonStates[1] || hitY > 0.25 && hitY < 0.5);
			lookingAtBottomHalf = inBlock && (!buttonStates[0] || hitY < 0.25);
		}

		matrices.mulPose(Vector3f.YN.rotationDegrees(facing.toYRot()));
		matrices.translate(0, 0, 0.4375 - SMALL_OFFSET);

		if (buttonStates[0]) {
			final VertexConsumer vertexConsumer = vertexConsumers.getBuffer(buttonStates[2] || lookingAtTopHalf ? MoreRenderLayers.getLight(BUTTON_TEXTURE, true) : MoreRenderLayers.getExterior(BUTTON_TEXTURE));
			IDrawing.drawTexture(matrices, vertexConsumer, -1.5F / 16, (buttonStates[1] ? 4.5F : 2.5F) / 16, 3F / 16, 3F / 16, 0, 1, 1, 0, facing, buttonStates[2] ? PRESSED_COLOR : lookingAtTopHalf ? HOVER_COLOR : ARGB_GRAY, light);
		}
		if (buttonStates[1]) {
			final VertexConsumer vertexConsumer = vertexConsumers.getBuffer(buttonStates[3] || lookingAtBottomHalf ? MoreRenderLayers.getLight(BUTTON_TEXTURE, true) : MoreRenderLayers.getExterior(BUTTON_TEXTURE));
			IDrawing.drawTexture(matrices, vertexConsumer, -1.5F / 16, (buttonStates[0] ? 0.5F : 2.5F) / 16, 3F / 16, 3F / 16, 0, 0, 1, 1, facing, buttonStates[3] ? PRESSED_COLOR : lookingAtBottomHalf ? HOVER_COLOR : ARGB_GRAY, light);
		}

		matrices.popPose();
	}
}
